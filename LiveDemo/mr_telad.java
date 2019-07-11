//Date 07/09/2006

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import javax.swing.JPanel;import javax.swing.JButton;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable;
import javax.swing.JComponent;import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.InputVerifier;
import java.awt.Dimension;

public class mr_telad extends cl_pbase
{
	private JComboBox cmbMKTTP;
	private JButton btnREWRK;
	private JButton btnPRINT;       
	private cl_JTable tblLODDTL, tblLODSQ,tblDSPST,tblDODEL;     
	private JTextField txtLADNO;
	private JTextField txtLADDT;
	private JTextField txtDORNO;
	private JTextField txtDOANO;
	private JTextField txtDORDT;
	private JTextField txtDOADT;
	private JTextField txtSLSTP;
	private JTextField txtDSPTP;
	private JTextField txtAUTBY;
	
	private JTextField txtGINNO;
	private JTextField txtLRYNO;
	private JTextField txtCNTNO;
	private JTextField txtSELNO;

	private JTextField txtMATCT;
	private JTextField txtLRNO;
	private JTextField txtLRDT;
	private JTextField txtLICNO;
	private JTextField txtLICDT;
	private JTextField txtLDGSQ;
	private JTextField txtEXSNO;
	private JTextField txtREMDS;
	
	private JCheckBox chkPRINT;
	private JCheckBox chkDSPST;
	private JCheckBox chkLDGSQ;	
	private JCheckBox chkCHKFL;
	
	JTextField txtEDITR,txtREQQT;
	
	private JPanel pnlDSPST;
	private JPanel pnlLDGSQ;
	private JPanel pnlDODEL;
	
	//JOptionPane LM_OPTNPN;
	 
	JTextField txtBYRCD;
	JLabel lblBYRNM,lblBAD01, lblBAD02, lblBAD03;
	JTextField txtCNSCD, txtFRMNO, txtFRMTP;
	JLabel lblCNSNM,lblCAD01, lblCAD02, lblCAD03;
	JTextField txtTRPCD;
	JLabel lblTRPNM;
	//String LM_MKTTP="01";
	String strMKTTP="01";
	
	private JLabel lblPRDDS;
	private JLabel lblPKGTP;
	private JLabel lblLADQT;
	
	//private JTextField
	String strGINNO,strLADNO,strLADTM,strLADDT,strDORNO,strDOANO,strDORDT,strDOADT,strSALTP,strZONCD;
	String strSALTP_DEX = "03";
	String strZONCD_EXP = "12";
	String strDTPCD,strPMTCD,strDTPDS,strAUTBY;
    String strLRYNO,strCNTDS,strTSLNO,strLDGSQ,strCSLNO,strSLRFL,LM_SLRDS;
    String strREMDS;
    String strBYRCD,strGRPCD,strBYRNM,strBAD01, strBAD02, strBAD03,strCFTAG;
    String strCNSCD,strCNSNM,strCAD01, strCAD02, strCAD03;
    String strTRPCD, strTRPNM;
	String strFILRF, strDELAD, strPRDRF;
   
  //String LM_INSSTR1, LM_INSSTR2;
	String  strWHRSTR;
  //String L_ADDSTR;

    String strPRDCD, strPKGTP, strPRDDS, strDORQT, strLADQT,strBALQT; 
    String strSTKQT, strREQQT, strREQPK, strPKGWT;
   // String LM_PRDCD_1, LM_PKGTP_1, LM_REQQT_1;
	
	private Vector<String> vtrPRDCD;
	private Vector<String> vtrPKGTP;
	private Vector<String> vtrPRDDS;
	private Vector<String> vtrSTKQT;
	private Vector<String> vtrLADRT;
	private Vector<String> vtrPRDRF;
	private Vector<String> vtrEXCRT; 
	private Vector<String> vtrEDCRT;
	private Vector<String> vtrEHCRT;
	private Vector<String> vtrLOTRF;
	

    String strIAMNO;
    String strPORNO;
    String strPORDT;
    String strINDNO;
    String strINVNO;
    String strFRMNO;
    String strFRMTP;
    String strDSRTP;
    String strDSRCD;
    String strDSTDS;
    String strTMOCD;
    String strCURCD;
    String strLR_NO;
    String strLR_DT;
    String strCPTVL;
    String strLADRT;
    String strLOTRF;
    String strPRDTP;
    String strECHRT;
    String strEXCRT;
    String strEDCRT;
    String strEHCRT;
    String strSTSFL;
    String strRSLNO;
    String strSTSCH;
    String strTRNFL;
    String strTSTFL;
    String strADLNO;
    String strADLDT;
   //String LM_UOMCD;
    String strLUSBY;
    String strLUPDT;
    String strSTSFL1;
	JLabel lblLCCQT;
	
	final int TB1_CHKFL = 0;
    final int TB1_PRDCD = 1;
    final int TB1_PRDDS = 2;
    final int TB1_PKGTP = 3;
	final int TB1_DORQT = 4;
    final int TB1_BALQT = 5;
    final int TB1_REQQT = 6;
    final int TB1_REQPK = 7;
    final int TB1_PKGWT = 8;
    final int TB1_STKQT = 9;
	
	final int TB2_CHKFL = 0;
	final int TB2_GINNO = 1;
    final int TB2_LRYNO = 2;
    final int TB2_DORNO = 3;
    final int TB2_BYRNM = 4;
    final int TB2_LADNO = 5;
    final int TB2_DSTDS = 6;
    final int TB2_LDGSQ = 7;
	
	final int TB3_CHKFL = 0;
	final int TB3_LRYNO = 1;
    final int TB3_LADTM = 2;
    final int TB3_ALOTM = 3;
    final int TB3_LODTM = 4;
    final int TB3_STSDS = 5;
    final int TB3_GINNO = 6;
    final int TB3_LADNO = 7;
    final int TB3_PRDDS = 8;
    final int TB3_PKGTP = 9;
    final int TB3_REQQT = 10;
    final int TB3_DORNO = 11;
    final int TB3_TRPNM = 12;
	

	final int TB4_CHKFL = 0;
	final int TB4_DORNO = 1;
    final int TB4_DORQT = 2;
    final int TB4_DSPDT = 3;
	final int TB4_LADNO = 4;
    final int TB4_SRLNO = 5;
	final int TB4_DELTP = 6;

	
	private String strTX_CMPCD;	
	private String strTX_SYSCD;
	private String strTX_SBSTP;
	private String strTX_DOCTP;
	private String strTX_DOCNO;
	private String strTX_PRDCD;
												/** Array elements for Doc.wise Tax detail */
	private int intTXDOC_TOT   = 3;
    private int intAE_TX_EXCVL = 0;
    private int intAE_TX_EDCVL = 1;
    private int intAE_TX_EHCVL = 2;
	private Hashtable<String,String[]> hstTXDOC;	
	
	boolean flgPOSTINVFL;				// Flag for Data Entry after Invoice Generation
	
	//double LM_REQQT0 = 0;
    double dblDORQT = 0;
    double dblLADQT = 0;
    double dblREQQT = 0;
    double dblBALQT = 0;
    double dblPKGWT = 0;
   // double LM_REQPK1 = 0;
	
	ResultSet rstRSSET;
	//CallableStatement calDORWK;
	CallableStatement calUPDDO;
	PreparedStatement PRE_getDORDTL0;
	PreparedStatement PRE_getBYRDTL;
	PreparedStatement PRE_setCODDS;
	PreparedStatement PRE_getDORDTL1_OTH;
    PreparedStatement PRE_getDORDTL1_MOD;
    PreparedStatement PRE_getDORDTL1_ADD;
	PreparedStatement PRE_exeUPDREM;
	PreparedStatement PRE_exeUPDWBT; 
	PreparedStatement PRE_exeSAVETRN;
	PreparedStatement PRE_exeUPDIVT;  
	PreparedStatement PRE_getLADDTL0;
	PreparedStatement PRE_setREMDTL;
	PreparedStatement PRE_setLADDTL1;
	PreparedStatement PRE_exeUPDIVT_POST;   
	PreparedStatement PRE_chkDELIVT1;      
	PreparedStatement PRE_exeDELIVT;            
    PreparedStatement PRE_exeDELIVT1;    
	PreparedStatement PRE_chkDELIVT;      
	PreparedStatement PRE_exeDELREM;            
	PreparedStatement PRE_chkMSTDEL;
	 
	mr_telad()
	{
		super(2);
		try
		{			
			setMatrix(22,8);
			
			vtrPRDCD = new Vector<String>();
			vtrPRDDS = new Vector<String>();
			vtrPKGTP = new Vector<String>();
			vtrSTKQT = new Vector<String>();
			vtrLADRT = new Vector<String>();
			vtrPRDRF = new Vector<String>();
			vtrEXCRT = new Vector<String>();
			vtrEDCRT = new Vector<String>();
			vtrEHCRT = new Vector<String>();
            vtrLOTRF = new Vector<String>();
			hstTXDOC = new Hashtable<String,String[]>();
			
			cmbMKTTP = new JComboBox();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='MST'"
			+ " AND CMT_CGSTP = 'COXXMKT'  AND isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD ";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET!=null)
			{
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP  =  nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  ");
					L_strTEMP +=  "   ";
					L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbMKTTP.addItem(L_strTEMP);	
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			add(new JLabel("Mkt.Type"),1,1,1,1,this,'L');
			add(cmbMKTTP,2,1,1,1,this,'L');
			add(new JLabel("L.A.No"),1,2,1,1,this,'L');
			add(txtLADNO = new TxtLimit(8),2,2,1,0.75,this,'L');
			
			add(new JLabel("L.A.Date"),1,3,1,1,this,'L');
			add(txtLADDT = new TxtLimit(18),2,3,1,1,this,'L');
			
			add(new JLabel("DO.No/Amd.No."),1,4,1,1,this,'L');
			add(txtDORNO = new TxtLimit(8),2,4,1,0.75,this,'L');
			add(txtDOANO = new TxtLimit(8),2,4,1,0.25,this,'R');
			
			add(new JLabel("DO Date"),1,5,1,1,this,'L');
			add(txtDORDT = new TxtDate(),2,5,1,0.75,this,'L');

			add(new JLabel("Amd.Date"),1,6,1,1,this,'L');
			add(txtDOADT = new TxtDate(),2,6,	1,0.75,this,'L');
			
			add(new JLabel("Sales Type"),1,7,1,1,this,'L');
			add(txtSLSTP = new TxtLimit(8),2,7,1,1,this,'L');
			
			add(new JLabel("Dsp. Type"),1,8,1,1,this,'L');
			add(txtDSPTP = new TxtLimit(8),2,8,1,1,this,'L');
			
			add(new JLabel("Auth By"),3,1,1,1,this,'L');
			add(txtAUTBY = new TxtLimit(5),4,1,1,0.5,this,'L');
			
			add(new JLabel("Security No"),3,2,1,1,this,'L');
			add(txtGINNO = new TxtLimit(8),4,2,1,0.75,this,'L');
			
			add(new JLabel("Lorry No"),3,3,1,1,this,'L');
			add(txtLRYNO = new TxtLimit(15),4,3,1,1,this,'L');
			
			add(new JLabel("Container"),3,4,1,1,this,'L');
			add(txtCNTNO = new TxtLimit(15),4,4,1,1,this,'L');
			
			add(new JLabel("Seal No"),3,5,1,1,this,'L');
			add(txtSELNO = new TxtLimit(10),4,5,1,1,this,'L');
			
			add(new JLabel("Mat. Category"),3,6,1,1,this,'L');
			add(txtMATCT = new TxtLimit(15),4,6,1,1,this,'L');
			
			add(btnREWRK = new JButton("REWORK"),4,7,1,1,this,'L');
			
			add(new JLabel("L.R.No"),5,1,1,1,this,'L');
			add(txtLRNO = new TxtLimit(10),6,1,1,1,this,'L');
			
			add(new JLabel("L.R. Date"),5,2,1,1,this,'L');
			add(txtLRDT = new TxtDate(),6,2,1,1,this,'L');
			
			add(new JLabel("A/Lic.No"),5,3,1,1,this,'L');
			add(txtLICNO = new TxtLimit(10),6,3,1,1,this,'L');
			
			add(new JLabel("A/Lic.Date"),5,4,1,1,this,'L');
			add(txtLICDT = new TxtDate(),6,4,1,1,this,'L');
			
			add(new JLabel("Ldg Seq"),5,5,1,1,this,'L');
			add(txtLDGSQ = new TxtLimit(1),6,5,1,0.25,this,'L');
			
			add(btnPRINT = new JButton("PRINT"),6,7,1,1,this,'L');
			
			add(new JLabel("Ex. Seal No."),5,6,1,1,this,'L');
			add(txtEXSNO = new TxtLimit(10),6,6,1,1,this,'L');
			
			add(new JLabel("Remark"),7,1,1,1,this,'L');
			add(txtREMDS = new TxtLimit(200),7,2,1,7,this,'L');
			
			
			add(new JLabel("Transp:"),8,1,1,0.5,this,'L');
			add(txtTRPCD = new TxtLimit(5),8,1,1,0.5,this,'R');
			add(lblTRPNM = new JLabel(""),8,2,1,3,this,'L');
			
			//add(txtTRNCD = new TxtLimit(5),9,1,1,1,this,'L');
			//add(txtTRNNM = new TxtLimit(40),9,2,1,4,this,'L');
			//add(chkPRINT = new JCheckBox("Print U/Seq"),9,6,1,1,this,'L');
			add(chkLDGSQ = new JCheckBox("Def.Ldg.Seq"),8,7,1,1,this,'L');
			add(chkDSPST = new JCheckBox("Disp.Status"),8,8,1,1,this,'L');
			
			add(new JLabel("Buyer"),9,1,1,0.5,this,'L');
			add(txtBYRCD = new TxtLimit(5),9,1,1,0.5,this,'R');
			add(lblBYRNM = new JLabel(""),9,2,1,4,this,'L');
			add(lblBAD01 = new JLabel(""),10,2,1,4,this,'L');
			add(lblBAD02 = new JLabel(""),11,2,1,4,this,'L');
			add(lblBAD03 = new JLabel(""),12,2,1,4,this,'L');

			add(new JLabel("P.Form No.:"),8,5,1,0.5,this,'L');
			add(txtFRMNO = new TxtLimit(10),8,5,1,0.5,this,'R');
			add(txtFRMTP = new TxtLimit(2),8,7,1,0.5,this,'L');
			
			
			add(new JLabel("Cons.:"),9,5,1,0.5,this,'L');
			add(txtCNSCD = new TxtLimit(5),9,5,1,0.5,this,'R');
			add(lblCNSNM = new JLabel(""),9,6,1,4,this,'L');
			add(lblCAD01 = new JLabel(""),10,6,1,4,this,'L');
			add(lblCAD02 = new JLabel(""),11,6,1,4,this,'L');
			add(lblCAD03 = new JLabel(""),12,6,1,4,this,'L');
			
			add(new JLabel("Load Carrying Capacity"),20,1,1,2,this,'L');
			
			String[] L_strTBLHD = {"S.NO","Prod Code","Discription","Pkg.Type","D.O.Qty ","Bal.Qty","Req.Qty","Pkgs","P.WT.","Stock"};
			int[] L_intCOLSZ = {15,80,140,70,70,80,80,80,75,60};
			tblLODDTL = crtTBLPNL1(this,L_strTBLHD,100,13,1,7,8,L_intCOLSZ,new int[]{0});
			
			tblLODDTL.setInputVerifier(new TBLINPVF());
			tblLODDTL.addKeyListener(this);
			tblLODDTL.setCellEditor(TB1_CHKFL,chkCHKFL = new JCheckBox());
			tblLODDTL.setCellEditor(TB1_REQQT,txtREQQT = new TxtLimit(8));
		///	tblLODDTL.setCellEditor(TB1_RCLNO,txtRCLNO1 = new TxtLimit(2));
			chkCHKFL.addKeyListener(this);
			txtREQQT.addKeyListener(this);
			txtREQQT.addFocusListener(this);
			
			
			vldINVER objINVER=new vldINVER();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objINVER);
			}		
			System.out.println("in 1");
			 PRE_getDORDTL0  = cl_dat.M_conSPDBA_pbst.prepareStatement("Select DOT_AMDNO, DOT_DORDT, DOT_AMDDT, DOT_TRPCD, DOT_INDNO,DOT_TMOCD,DOT_AMDNO, DOT_AMDDT, DOT_LOTRF, IN_SALTP, IN_ZONCD, IN_DSRTP, IN_DSRCD, IN_PORNO, IN_PORDT, IN_DTPCD, IN_PMTCD, IN_BYRCD, IN_CFTAG, IN_CNSCD, IN_CPTVL, IN_AMDNO, IN_CURCD, IN_ECHRT,IN_FILRF, IN_DELAD, RM_REMDS, DOT_STSFL from MR_INMST,MR_DOTRN "
            +" left outer join MR_RMMST on RM_CMPCD = DOT_CMPCD and RM_MKTTP=DOT_MKTTP and RM_TRNTP='DO' and RM_DOCNO=DOT_DORNO where IN_STSFL in ('H','O','P','C','I','A','1','2','3') and DOT_CMPCD = IN_CMPCD and DOT_MKTTP=IN_MKTTP and DOT_INDNO=IN_INDNO and DOT_STSFL<>'X' and IN_STSFL<>'X' and DOT_CMPCD = ? and DOT_MKTTP = ? and DOT_DORNO = ? order by DOT_STSFL");
			 
			 System.out.println("in 2");
			 PRE_getBYRDTL =cl_dat.M_conSPDBA_pbst.prepareStatement("Select PT_PRTTP, PT_PRTCD, PT_PRTNM, PT_GRPCD, PT_ADR01, PT_ADR02, PT_ADR03, PT_TSTFL from CO_PTMST where PT_PRTTP + PT_PRTCD in (?,?,?)");
			 PRE_setCODDS = cl_dat.M_conSPDBA_pbst.prepareStatement("Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP + CMT_CGSTP + CMT_CODCD in (?,?)");
			 
			 System.out.println("in 3");
			 PRE_getDORDTL1_OTH = cl_dat.M_conSPDBA_pbst.prepareStatement("select DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,isnull(INT_PRDRF,' ') INT_PRDRF,DOT_STSFL,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) ST_STKQT FROM MR_INTRN,MR_DOTRN left outer join FG_STMST on DOT_CMPCD=ST_CMPCD and DOT_PRDCD=ST_PRDCD AND DOT_PKGTP=ST_PKGTP where DOT_CMPCD = INT_CMPCD AND DOT_MKTTP = INT_MKTTP and DOT_INDNO = INT_INDNO and DOT_PRDCD = INT_PRDCD and DOT_PKGTP = INT_PKGTP  and  INT_STSFL in ('H','O','P','C','I','A','1','2','3') and DOT_CMPCD = ? AND DOT_MKTTP = ? and DOT_DORNO = ? and DOT_STSFL<>'X' and DOT_PRDCD in (select IVT_PRDCD from MR_IVTRN where IVT_CMPCD = ? AND IVT_MKTTP = ? and IVT_LADNO = ?)"
            +"  GROUP BY DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,INT_PRDRF,DOT_STSFL");

			 System.out.println("in 4");
            PRE_getDORDTL1_MOD = cl_dat.M_conSPDBA_pbst.prepareStatement("select DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,isnull(INT_PRDRF,' ') INT_PRDRF,DOT_STSFL,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) ST_STKQT FROM MR_INTRN,MR_DOTRN left outer join FG_STMST on DOT_CMPCD = ST_CMPCD AND DOT_PRDCD=ST_PRDCD AND DOT_PKGTP=ST_PKGTP where DOT_CMPCD = INT_CMPCD AND DOT_MKTTP = INT_MKTTP and DOT_INDNO = INT_INDNO and DOT_PRDCD = INT_PRDCD and DOT_PKGTP = INT_PKGTP and INT_STSFL in ('H','O','P','C','I','A','1','2','3') and DOT_STSFL<>'X' AND DOT_CMPCD = ? and DOT_MKTTP = ? and DOT_DORNO = ? and  DOT_DORQT >= DOT_LADQT"
            +" and DOT_STSFL <> 'X' and dot_prdcd not in (select ivt_prdcd from mr_ivtrn where ivt_cmpcd = ? and ivt_mkttp = ? and ivt_ladno = ?  and ivt_stsfl not in ('A','L','D')) GROUP BY DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,INT_PRDRF,DOT_STSFL");

			System.out.println("in 5");
			PRE_getDORDTL1_ADD = cl_dat.M_conSPDBA_pbst.prepareStatement("select DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,isnull(INT_PRDRF,' ') INT_PRDRF,DOT_STSFL,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) ST_STKQT FROM MR_INTRN,MR_DOTRN left outer join FG_STMST "
            +" on DOT_CMPCD = ST_CMPCD and DOT_PRDCD=ST_PRDCD AND DOT_PKGTP=ST_PKGTP where DOT_CMPCD = INT_CMPCD and DOT_MKTTP = INT_MKTTP and DOT_INDNO = INT_INDNO and DOT_PRDCD = INT_PRDCD and DOT_PKGTP = INT_PKGTP  and  INT_STSFL in ('H','O','P','C','I','A','1','2','3') and DOT_STSFL <> 'X' and DOT_CMPCD = ? and DOT_MKTTP = ? and DOT_DORNO = ? and  DOT_DORQT > DOT_LADQT"
            +"  GROUP BY DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,INT_PRDRF,DOT_STSFL");
			
			System.out.println("in 6");
			PRE_exeUPDREM = cl_dat.M_conSPDBA_pbst.prepareStatement("select * from MR_RMMST where RM_CMPCD = ? and RM_MKTTP = ?  and RM_TRNTP = ?  and RM_DOCNO = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			System.out.println("in 7");
			PRE_exeUPDWBT = cl_dat.M_conSPDBA_pbst.prepareStatement("update MM_WBTRN set WB_STSFL = '1', WB_ORDRF = ?, WB_LRYNO = ? , WB_PRTCD = ? , WB_PRTDS = ? , WB_TPRCD = ? , WB_TPRDS = ? , WB_LUSBY = ? , WB_LUPDT = ?  where WB_CMPCD = ? and WB_DOCTP = '03' and WB_DOCNO = ? and wb_stsfl='0'");
			
			System.out.println("in 8");
			PRE_exeSAVETRN  = cl_dat.M_conSPDBA_pbst.prepareStatement("select count(*) from MR_IVTRN where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? and IVT_PRDCD = ? and IVT_PKGTP = ?");
			
			System.out.println("in 9");
			PRE_exeUPDIVT = cl_dat.M_conSPDBA_pbst.prepareStatement("select * from MR_IVTRN  where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? and IVT_PRDCD = ? and IVT_PKGTP = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			System.out.println("in 10");
			PRE_getLADDTL0 = cl_dat.M_conSPDBA_pbst.prepareStatement("Select IVT_LADDT, IVT_DORNO, IVT_DOANO, IVT_INDNO, IVT_INVNO, IVT_AUTBY, IVT_LRYNO, IVT_FRMNO, IVT_FRMTP, IVT_TRPCD, IVT_CNTDS, IVT_TSLNO, IVT_UNLSQ, IVT_CSLNO, IVT_SLRFL, IVT_GINNO, IVT_LR_NO, IVT_LR_DT, IVT_ADLNO, IVT_ADLDT, IVT_STSFL from MR_IVTRN where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? ");
			
			System.out.println("in 11");
			PRE_setREMDTL = cl_dat.M_conSPDBA_pbst.prepareStatement("Select RM_REMDS from MR_RMMST where RM_CMPCD = ? and RM_MKTTP = ? and RM_TRNTP = ? and RM_DOCNO = ?");
			
			System.out.println("in 12");
			PRE_setLADDTL1  = cl_dat.M_conSPDBA_pbst.prepareStatement("select IVT_PRDCD,IVT_PKGTP,IVT_REQQT,IVT_STSFL from MR_IVTRN where  IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ?");
			
			System.out.println("in 13");
			PRE_exeUPDIVT_POST = cl_dat.M_conSPDBA_pbst.prepareStatement("select * from MR_IVTRN  where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			System.out.println("in 14");
			PRE_chkDELIVT1 = cl_dat.M_conSPDBA_pbst.prepareStatement("select ist_issqt,ist_stsfl from FG_ISTRN where ist_cmpcd = ? and ist_isstp = '10' and ist_mkttp = ? and ist_issno = ? and ist_prdcd = ? and ist_pkgtp = ? ");
			
			System.out.println("in 15");
			PRE_exeDELIVT = cl_dat.M_conSPDBA_pbst.prepareStatement("update MR_IVTRN set IVT_REQQT = 0.000,IVT_STSFL='X' ,IVT_LUSBY = ? , IVT_LUPDT = ?  where IVT_CMPCD = ? and IVT_MKTTP = ?  and IVT_LADNO = ?  and  IVT_PRDCD = ?  and IVT_PKGTP = ?");

			System.out.println("in 16");
            PRE_exeDELIVT1 = cl_dat.M_conSPDBA_pbst.prepareStatement("update MM_WBTRN set WB_STSFL = '0'  where WB_CMPCD = ? and WB_DOCTP = '03' and WB_DOCNO = ? and WB_ORDRF = ?  and WB_STSFL <> '9'");
			
			System.out.println("in 17");
			PRE_chkDELIVT  = cl_dat.M_conSPDBA_pbst.prepareStatement("select ist_prdcd,ist_issqt,ist_stsfl from FG_ISTRN where IST_CMPCD = ? and IST_ISSTP='10' and IST_MKTTP = ? and ist_issno = ?  ");
			
			System.out.println("in 18");
			PRE_exeDELREM = cl_dat.M_conSPDBA_pbst.prepareStatement("update MR_RMMST set RM_STSFL = 'X', RM_LUSBY = ? , RM_LUPDT = ?  where RM_CMPCD = ? and RM_MKTTP = ?  and RM_TRNTP = ?  and RM_DOCNO = ? ");
			
			System.out.println("in 19");
			PRE_chkMSTDEL   = cl_dat.M_conSPDBA_pbst.prepareStatement("select count(*) from MR_IVTRN where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? and IVT_STSFL <> 'X'");
			
			//calDORWK=cl_dat.M_conSPDBA_pbst.prepareCall("{ call rwkDOTRN(?,?)}");

			System.out.println("in 20");
			calUPDDO =cl_dat.M_conSPDBA_pbst.prepareCall("{ call updDOTRN_LAD(?,?,?)}");
		}
		catch(Exception E)
		{
			setMSG(E,"Constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==chkDSPST)
				dspDSPST();
			
			if(M_objSOURC==chkLDGSQ)
				getLDGSQ(txtLADNO.getText());
			
			if(M_objSOURC==cmbMKTTP)
			{
				strMKTTP=cmbMKTTP.getSelectedItem().toString().substring(0,2);
			}
			
			if(M_objSOURC==btnPRINT)
			{
				//cl_dat.ocl_dat.M_PPPRGCD = "MR_TPLAD";
				mr_tplad objTPLAD = new mr_tplad();
				objTPLAD.prnALLREC_F(strMKTTP,strLADNO,strLADNO);
				JComboBox L_cmbLOCAL = objTPLAD.getPRNLS();
				objTPLAD.doPRINT(cl_dat.M_strREPSTR_pbst+"mr_tpla1.doc",L_cmbLOCAL.getSelectedIndex());
				cmbMKTTP.requestFocus();
			}
			if(M_objSOURC==btnREWRK)
			{
				//System.out.println("Before call");
				calUPDDO.setString(1,cl_dat.M_strCMPCD_pbst);
				calUPDDO.setString(2,strMKTTP);
				calUPDDO.setString(3,txtDORNO.getText().trim());
				calUPDDO.executeUpdate();
				cl_dat.M_conSPDBA_pbst.commit();
				JOptionPane.showMessageDialog(this," Reworking completed successfully","Message",JOptionPane.INFORMATION_MESSAGE);
				//System.out.println("After call");
				
			}
			if (M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					cmbMKTTP.setEnabled(true); 
					cmbMKTTP.requestFocus();
					txtLADNO.setEnabled(false);
					txtLADDT.setText(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText());
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					cmbMKTTP.setEnabled(true); 
					txtDORNO.setEnabled(false);
					cmbMKTTP.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					cmbMKTTP.setEnabled(true); 
					txtLADNO.setEnabled(true);
					txtDORNO.setEnabled(false);
					tblLODDTL.setEnabled(true);
					cmbMKTTP.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					setENBL(false);
					cmbMKTTP.setEnabled(true); 
					txtLADNO.setEnabled(true);
					btnPRINT.setEnabled(true);
					chkLDGSQ.setEnabled(true);
					chkDSPST.setEnabled(true);					
					cmbMKTTP.requestFocus();
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"actionPerformed");
		}
	}

	/**
	 * Super Class metdhod overrided to inhance its funcationality, to enable disable 
	 * components according to requriement.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtDOANO.setEnabled(false);
		txtDOADT.setEnabled(false);
		txtLADDT.setEnabled(false);
		txtAUTBY.setEnabled(false);
		txtSLSTP.setEnabled(false);
		txtDSPTP.setEnabled(false);
		txtLRYNO.setEnabled(false);
		txtTRPCD.setEnabled(false);
	
		//txtTRPNM.setEnabled(false);
		txtBYRCD.setEnabled(false);
		
		txtCNSCD.setEnabled(false);
		
		tblLODDTL.cmpEDITR[TB1_PRDCD].setEnabled(false);
		tblLODDTL.cmpEDITR[TB1_PRDDS].setEnabled(false);
		tblLODDTL.cmpEDITR[TB1_PKGTP].setEnabled(false);
		tblLODDTL.cmpEDITR[TB1_DORQT].setEnabled(false);
		tblLODDTL.cmpEDITR[TB1_BALQT].setEnabled(false);
		tblLODDTL.cmpEDITR[TB1_PKGWT].setEnabled(false);
		tblLODDTL.cmpEDITR[TB1_STKQT].setEnabled(false);
	}
	/**
	 * KeyPress 
	*/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			//System.out.println("QUERY11 = "+M_strSQLQRY);
			String L_strMKTTP=cmbMKTTP.getSelectedItem().toString().substring(0,2);
			strMKTTP=L_strMKTTP;
			
			if(M_objSOURC==txtLADNO)
			{
				M_strHLPFLD="txtLADNO";
				String L_ARRHDR[] ={"LA No.","Sec.No.","Date","Customer","Status"};
				M_strSQLQRY = "Select IVT_LADNO,IVT_GINNO,IVT_LADDT,PT_PRTNM,IVT_STSFL from MR_IVTRN, CO_PTMST where PT_PRTTP='C'  and IVT_BYRCD = PT_PRTCD  and IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP = '"+L_strMKTTP+"'";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					M_strSQLQRY+= " and IVT_STSFL in ('A','P') and IVT_LADQT=0  and IVT_LADNO not in (select IVT_LADNO from MR_IVTRN where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_STSFL in ('L','D')) ";
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					M_strSQLQRY+= " and IVT_STSFL <> 'X'";
				}
				if(txtLADNO.getText().trim().length() >0)
					M_strSQLQRY += " and IVT_LADNO like '" + txtLADNO.getText().trim() + "%' ";
				M_strSQLQRY += " order by IVT_GINNO desc, IVT_LADDT desc,IVT_LADNO desc";
				System.out.println("QUERY = "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,5,"CT");
			}
					

			if(M_objSOURC==txtFRMNO)
			{
				M_strHLPFLD="txtFRMNO";
				String L_ARRHDR[] ={"Form No.","Type"};
				M_strSQLQRY = "Select PFT_FRMNO,PFT_FRMTP from MR_PFTRN where PFT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PFT_PRTTP = 'C' and PFT_PRTCD = '"+txtBYRCD.getText()+"' and isnull(PFT_INVNO,'')='' order by PFT_FRMNO";
				//System.out.println("QUERY = "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
			
			if(M_objSOURC==txtDORNO)
			{
				M_strHLPFLD="txtDORNO";
				String L_ARRHDR[] ={"DO.No.","Amd.No.","Date","Amd.Dt."};
				M_strSQLQRY = "Select DOT_DORNO,DOT_AMDNO,DOT_DORDT,DOT_AMDDT from MR_DOTRN where DOT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOT_MKTTP = '"+L_strMKTTP+"' and DOT_STSFL in  ('A','H','1')";
				
				M_strSQLQRY += " and DOT_DORQT>DOT_LADQT ";
				if(txtDORNO.getText().trim().length() >0)
					M_strSQLQRY += " and DOT_DORNO like '" + txtDORNO.getText().trim() + "%' ";
				M_strSQLQRY += " order by DOT_DORDT desc";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");            
			}
			
			if(M_objSOURC==txtGINNO)
			{
				M_strHLPFLD="txtGINNO";
				String L_ARRHDR[] ={"Sec.No.","Lorry No.","Date","L.C.Capacity"};
                M_strSQLQRY = "Select distinct WB_DOCNO,WB_LRYNO,WB_GINDT,WB_LCCQT from MM_WBTRN where WB_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and WB_DOCTP='03' and WB_STSFL = '0' ";
				if(txtGINNO.getText().trim().length() >0)
					M_strSQLQRY += " and WB_DOCNO like '" + txtGINNO.getText().trim() + "%' ";
				
				M_strSQLQRY += " order by WB_DOCNO";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");            
			}
			if(M_objSOURC==txtMATCT)
			{
				M_strHLPFLD="txtMATCT";
				String L_ARRHDR[] ={"Code","Description"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MRXXMTP'  order by CMT_CODCD";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");            
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC==cmbMKTTP)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					txtDORNO.requestFocus();
				else
					txtLADNO.requestFocus();
			}
			if(M_objSOURC==txtLADNO)
			{
				String L_strLADNO = txtLADNO.getText();
				strMKTTP=cmbMKTTP.getSelectedItem().toString().substring(0,2);
				if(L_strLADNO.trim().length()<8)
					return ;
					
				if(!getLADDTL0(strMKTTP, L_strLADNO))
				{		
					//System.out.println("KKKKKKKKKKKKKKKKKKK");
					//setMSG("SOme Problem",'E');
					return ;
				}
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) && strSTSCH.equals("X"))
						return ;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) && strSTSCH.equals("D"))
						return ;
					setLADDTL0();
					if(!getDODTL(txtDORNO.getText().trim()))
					{
						setMSG(strMKTTP+"/"+txtDORNO+" not found in DO/Indent/Remarks record",'E');
						return ;
					}
					if(!getBYRDTL())
					{
						setMSG("Error in party code",'E');
						return ;
					}
					if(!setCODDS())
					{
						setMSG("Error in code transaction details",'E');
						return ;
					}
					//System.out.println("IN 2LAST");
					//getGRDREC();
					if(!getDATA())
					{
						String L_strLADDT=txtLADDT.getText().trim();
						clrCOMP_1();
						txtLADDT.setText(L_strLADDT);	
						setMSG("LA details are not available",'E');
						return ;
					}
					setLADDTL1(strMKTTP,L_strLADNO);
					setREMDTL(strMKTTP,"LA",L_strLADNO);
					//setREMDTL(LM_MKTTP,"LA",strLADNO);
				txtGINNO.requestFocus();
			}
			
			if(M_objSOURC==txtDORNO)
			{
				setMSG(" ",'N');
				//clrCOMP_1();
				strMKTTP=cmbMKTTP.getSelectedItem().toString().substring(0,2);
				
				if(!getDODTL(txtDORNO.getText().trim()))
				{
				   setMSG(strMKTTP+"/"+strDORNO+" not found in DO/Indent/Remarks record",'E');
				   return;
				}
				String L_strSQLQRY = "select dod_prdcd,dod_pkgtp,dod_dspdt,count(*) from mr_dodel where dod_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and dod_dorno='"+txtDORNO.getText()+"' and isnull(dod_dorqt,0)>0 and dod_stsfl <> 'X'  group by dod_prdcd,dod_pkgtp,dod_dspdt having count(*)>1";
				System.out.println(L_strSQLQRY);
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET != null && L_rstRSSET.next())
				{
					setMSG("Problem in Delivery Schedule (Multiple entries with same date)",'E');
					L_rstRSSET.close();
				    return;
				}
				
				if(!getBYRDTL())
				{
					setMSG("Error in party code",'E');
					return;
				}
				
				if(!setCODDS())
				{
					setMSG("Error in code transaction details",'E');
					return ;
				}
				
				if(!getDATA())
				{
					String L_strLADDT=txtLADDT.getText().trim();
					clrCOMP_1();
					txtLADDT.setText(L_strLADDT);	
					setMSG("Invalid D.O. or Completed D.O.",'E');
					return ;
				}
				txtLADNO.setText(getLADNO());
				txtLRDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
				txtAUTBY.setText(cl_dat.M_strUSRCD_pbst);
				txtMATCT.setText("1 Fresh");
				txtLDGSQ.setText("1");
				txtGINNO.requestFocus();
				//System.out.println("IN 2LAST");
				//getGRDREC();
				//System.out.println("IN LAST");
				//txtSLRFL.setText(strSLRFL);
			}
			if(M_objSOURC==txtGINNO)
			{
				txtCNTNO.requestFocus();
			}
			
			if(M_objSOURC==txtCNTNO)
			{
				txtSELNO.requestFocus();
			}
			if(M_objSOURC==txtSELNO)
			{
				txtMATCT.requestFocus();
			}
			
			if(M_objSOURC==txtMATCT)
			{
				txtLRNO.requestFocus();
			}
			if(M_objSOURC==txtLRNO)
			{
				txtLRDT.requestFocus();
			}
			if(M_objSOURC==txtLRDT)
			{
				txtLICNO.requestFocus();
			}
			if(M_objSOURC==txtLICNO)
			{
				txtLICDT.requestFocus();
			}

			if(M_objSOURC==txtLICDT)
			{
				txtLDGSQ.requestFocus();
			}
			if(M_objSOURC==txtLDGSQ)
			{
				txtEXSNO.requestFocus();
			}
			if(M_objSOURC==txtEXSNO)
			{
				txtREMDS.requestFocus();
			}
			if(M_objSOURC==txtREMDS)
			{
				txtFRMNO.requestFocus();
			}
			if(M_objSOURC==txtFRMNO)
			{
				tblLODDTL.setRowSelectionInterval(tblLODDTL.getSelectedRow(),tblLODDTL.getSelectedRow());		
				tblLODDTL.setColumnSelectionInterval(TB1_REQQT,TB1_REQQT);		
				tblLODDTL.editCellAt(tblLODDTL.getSelectedRow(),TB1_REQQT);
				tblLODDTL.cmpEDITR[TB1_REQQT].requestFocus();
			}
		}
		}
		catch(Exception E)
		{
			setMSG(E," KeyPressed ");
		}
	}
	
	
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtLADNO"))
			{
				txtLADNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtFRMNO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtFRMNO.setText(L_STRTKN1.nextToken());
				txtFRMTP.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtDORNO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtDORNO.setText(L_STRTKN1.nextToken());
				txtDOANO.setText(L_STRTKN1.nextToken());
				txtDORDT.setText(L_STRTKN1.nextToken());
				txtDOADT.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtGINNO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtGINNO.setText(L_STRTKN1.nextToken());
				txtLRYNO.setText(L_STRTKN1.nextToken());
				
			}
			if(M_strHLPFLD.equals("txtMATCT"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				String L_strMATCT=L_STRTKN1.nextToken();
				L_strMATCT +=" "+L_STRTKN1.nextToken();
				txtMATCT.setText(L_strMATCT);
			}
		}
		catch(Exception E)
		{
			setMSG(E," exeHLPOK ");
		}
	}
	/**
	 * Function for fetching DO details
	 * This function fetch DO Details from  MR_DOTRN,MR_INMST Left outer join MR_RMMST
	 */
	private boolean getDODTL(String P_strDORNO)
	{
		try
		{
			setMSG("Picking up D.O. details",'N');
			PRE_getDORDTL0.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_getDORDTL0.setString(2,strMKTTP);
			PRE_getDORDTL0.setString(3,P_strDORNO);
			//System.out.println("strMKTTP "+strMKTTP);
			//System.out.println("P_strDORNO = "+ P_strDORNO);
			M_rstRSSET = PRE_getDORDTL0.executeQuery();
			
			if ((!M_rstRSSET.next())|| M_rstRSSET == null)
			{
				setMSG("No records available",'E');
				return false;
			}
			strSTSFL1 = getRSTVAL(M_rstRSSET,"DOT_STSFL","C");
			if (strSTSFL1.equals("X"))
			{
				setMSG("D.O. is cancelled",'E');
			    return false;
			}
			strDOANO = getRSTVAL(M_rstRSSET,"DOT_AMDNO","C");
			strDORDT = getRSTVAL(M_rstRSSET,"DOT_DORDT","D");
			strINDNO = getRSTVAL(M_rstRSSET,"DOT_INDNO","C");
			strTMOCD = getRSTVAL(M_rstRSSET,"DOT_TMOCD","C");
			strDOANO = getRSTVAL(M_rstRSSET,"DOT_AMDNO","C");
			strDOADT = getRSTVAL(M_rstRSSET,"DOT_AMDDT","D");
			strLOTRF = getRSTVAL(M_rstRSSET,"DOT_LOTRF","C");
			//if (cl_dat.ocl_dat.M_FLGOPT == 'A')
			strTRPCD = getRSTVAL(M_rstRSSET,"DOT_TRPCD","C");
			strZONCD = getRSTVAL(M_rstRSSET,"IN_ZONCD","C");
			strSALTP = getRSTVAL(M_rstRSSET,"IN_SALTP","C");
			strDTPCD = getRSTVAL(M_rstRSSET,"IN_DTPCD","C");
			strPMTCD = getRSTVAL(M_rstRSSET,"IN_PMTCD","C");
			strBYRCD = getRSTVAL(M_rstRSSET,"IN_BYRCD","C");
			strFILRF = getRSTVAL(M_rstRSSET,"IN_FILRF","C");
			strDELAD = getRSTVAL(M_rstRSSET,"IN_DELAD","C");
			strCFTAG = getRSTVAL(M_rstRSSET,"IN_CFTAG","C");
			
			strCNSCD = getRSTVAL(M_rstRSSET,"IN_CNSCD","C");
			strDSRTP = getRSTVAL(M_rstRSSET,"IN_DSRTP","C");
			strDSRCD = getRSTVAL(M_rstRSSET,"IN_DSRCD","C");
			strPORNO = getRSTVAL(M_rstRSSET,"IN_PORNO","C");
			strPORDT = getRSTVAL(M_rstRSSET,"IN_PORDT","D");
			strCPTVL = getRSTVAL(M_rstRSSET,"IN_CPTVL","C");
			strIAMNO = getRSTVAL(M_rstRSSET,"IN_AMDNO","C");
			
			strCURCD = getRSTVAL(M_rstRSSET,"IN_CURCD","C");
			strECHRT = getRSTVAL(M_rstRSSET,"IN_ECHRT","C");
			strREMDS = getRSTVAL(M_rstRSSET,"RM_REMDS","C");

			crtTXDOC(strMKTTP,strINDNO);
			txtDOANO.setText(strDOANO);
			txtDORDT.setText(strDORDT);
			txtDOADT.setText(strDOADT);
			txtTRPCD.setText(strTRPCD);
			txtBYRCD.setText(strBYRCD);
			txtCNSCD.setText(strCNSCD);
			txtREMDS.setText(strREMDS);			
									
			M_rstRSSET.close();
		}
		catch(Exception E)
		{
			setMSG(E,"GetDODTL");
		}
		return true;
	}
	/**
	 *  Function for fetching data for Buyer Details And Cinsignee Details
	 */
	private boolean getBYRDTL()
	{
		boolean L_flgBYRFL = false, L_flgCNSFL = false, L_flgTRPFL = false, L_flgRETFL = false;
		try
		{
			setMSG("Picking up Buyer details",'N');
			String L_strPRTTP, L_strPRTCD;
			//LM_STRQRY = "Select PT_PRTNM, PT_ADR01, PT_ADR02, PT_ADR03, PT_TSTFL from CO_PTMST ";
			//LM_STRQRY += " where PT_PRTTP = '"+LP_PRTTP+"' and PT_PRTCD = '"+LP_PRTCD+"'";
			//strBYRCD=txtBYRCD.getText().trim();
			//strCNSCD=txtCNSCD.getText().trim();
			//strTRPCD=txtTRPCD.getText().trim();
			System.out.println("C"+strBYRCD+ "   " +"C"+strCNSCD+"   "+"T"+strTRPCD);	
			PRE_getBYRDTL.setString(1,"C"+strBYRCD);
			PRE_getBYRDTL.setString(2,"C"+strCNSCD);
			PRE_getBYRDTL.setString(3,"T"+strTRPCD);
			ResultSet L_rstRSSET = PRE_getBYRDTL.executeQuery();
			
			if ((!L_rstRSSET.next()) || L_rstRSSET == null)
			{
				return false;
			}
			while(true)
			{
				L_strPRTTP = getRSTVAL(L_rstRSSET,"PT_PRTTP","C");
				L_strPRTCD = getRSTVAL(L_rstRSSET,"PT_PRTCD","C");
				//System.out.println( " PRTCD = "+L_strPRTCD);
				if(L_strPRTTP.equals("C")  && L_strPRTCD.equals(strBYRCD))
				{
					strBYRNM = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
					strBAD01 = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
					strBAD02 = getRSTVAL(L_rstRSSET,"PT_ADR02","C");
					strBAD03 = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
					strTSTFL = getRSTVAL(L_rstRSSET,"PT_TSTFL","C");
					strGRPCD = getRSTVAL(L_rstRSSET,"PT_GRPCD","C");
					lblBYRNM.setText(strBYRNM);
					lblBAD01.setText(strBAD01);
					lblBAD02.setText(strBAD02);
					lblBAD03.setText(strBAD03);
					//System.out.println( " L_flgBYRFL1 = "+L_flgBYRFL);
					L_flgBYRFL = true;
					//System.out.println( " L_flgBYRFL2 = "+L_flgBYRFL);
				}
				if(L_strPRTTP.equals("C")  && L_strPRTCD.equals(strCNSCD))
				{
					strCNSNM = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
					strCAD01 = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
					strCAD02 = getRSTVAL(L_rstRSSET,"PT_ADR02","C");
					strCAD03 = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
					//strTSTFL = getRSTVAL(L_rstRSSET,"PT_TSTFL","C");
					strDSTDS = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
					strDSTDS = getSUBSTR(strDSTDS,0,14);
					lblCNSNM.setText("");
					lblCAD01.setText("");
					lblCAD02.setText("");
					lblCAD03.setText("");
					if(!txtBYRCD.getText().equals(txtCNSCD.getText()))
				   {
						lblCNSNM.setText(strCNSNM);
						lblCAD01.setText(strCAD01);
						lblCAD02.setText(strCAD02);
						lblCAD03.setText(strCAD03);
					}
					
					//System.out.println( " L_flgCNSFL = "+L_flgBYRFL);
					L_flgCNSFL = true;
					//System.out.println( " L_flgCNSFL = "+L_flgBYRFL);
				}
				if(L_strPRTTP.equals("T")  && L_strPRTCD.equals(strTRPCD))
				{
	                strTRPNM = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
		            lblTRPNM.setText(strTRPNM);
					L_flgTRPFL = true;
				}
				if(!L_rstRSSET.next())
	                break;
		    }
			L_rstRSSET.close();
			L_flgRETFL = true;
			if (!L_flgBYRFL)
			{	
                L_flgRETFL = false;
                setMSG("Buyer details not found",'E');
			}
			else if(!L_flgCNSFL)
			{
				L_flgRETFL = false;
                setMSG("Consignee details not found",'E');
			}
			else if (!L_flgTRPFL)
			{
                L_flgRETFL = false;
                setMSG("Transporter details not found",'E');
			}
		}
		catch (Exception L_EX_BYRDTL)
		{
			setMSG(L_EX_BYRDTL,"getBYRDTL");                                                    
		}
		return L_flgRETFL;
	}
	
	/**
	 * Function for getting data from Code transaction table
	 */
	private boolean setCODDS()
	{
		String L_strCGMTP, L_strCGSTP, L_strCODCD, L_strCODDS;
		boolean L_flgSALFL = false, L_flgDTPFL = false, L_flgRETFL = false;
		try
		{

			PRE_setCODDS.setString(1,"SYSMR00SAL"+strSALTP);
			PRE_setCODDS.setString(2,"SYSMRXXDTP"+strDTPCD);
			ResultSet L_rstRSSET = PRE_setCODDS.executeQuery();
			if (!L_rstRSSET.next() || L_rstRSSET == null)
			{
                return false;
			}
			while(true)
			{
                L_strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                L_strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                L_strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                L_strCODDS = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                if (L_strCGMTP.equals("SYS") && L_strCGSTP.equals("MR00SAL") && L_strCODCD.equals(strSALTP))
                {
					txtSLSTP.setText(L_strCODDS);
                    L_flgSALFL = true;
                }
                if (L_strCGMTP.equals("SYS") && L_strCGSTP.equals("MRXXDTP") && L_strCODCD.equals(strDTPCD))
                {
					txtDSPTP.setText(L_strCODDS);
                    L_flgDTPFL = true;
                }
                if (!L_rstRSSET.next())
                        break;
			}
			L_rstRSSET.close();
			L_flgRETFL = true;
			if (!L_flgSALFL)
			{
                setMSG("Invalid Sale Type ",'E');
                L_flgRETFL = false;
			}
			else if (!L_flgDTPFL)
			{
                setMSG("Invalid Despatch Type ",'E');
                L_flgRETFL = false;
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setCODDS");                                                    
		}
		return L_flgRETFL;
	}

	/** One time data capturing for Doc.Tax 
		*	into the Hash Table
		*/
	private void crtTXDOC(String P_strMKTTP,String LP_INDNO)
	{
		String L_strSQLQRY = "";
		try
		{
			hstTXDOC.clear();
		    L_strSQLQRY = "select * from co_txdoc where tx_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and tx_syscd='MR' and tx_sbstp='"+strMKTTP+"' and tx_doctp='IND' and tx_docno='"+LP_INDNO+"' and tx_prdcd='XXXXXXXXXX'";
			System.out.println(L_strSQLQRY);
		    ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
		    if(L_rstRSSET == null || !L_rstRSSET.next())
			{
				setMSG("Tax Records not found in CO_TXDOC",'E');
		        return;
			}
		    while(true)
			{
				strTX_CMPCD = cl_dat.M_strCMPCD_pbst;
				strTX_SYSCD = getRSTVAL(L_rstRSSET,"TX_SYSCD","C");
		        strTX_SBSTP = getRSTVAL(L_rstRSSET,"TX_SBSTP","C");
		        strTX_DOCTP = getRSTVAL(L_rstRSSET,"TX_DOCTP","C");
		        strTX_DOCNO = getRSTVAL(L_rstRSSET,"TX_DOCNO","C");
		        strTX_PRDCD = getRSTVAL(L_rstRSSET,"TX_PRDCD","C");
		        String[] staTXDOC = new String[intTXDOC_TOT];
		        staTXDOC[intAE_TX_EXCVL] = getRSTVAL(L_rstRSSET,"TX_EXCVL","N");
		        staTXDOC[intAE_TX_EDCVL] = getRSTVAL(L_rstRSSET,"TX_EDCVL","N");
		        staTXDOC[intAE_TX_EHCVL] = getRSTVAL(L_rstRSSET,"TX_EHCVL","N");
		        hstTXDOC.put(strTX_CMPCD+strTX_SYSCD+strTX_SBSTP+strTX_DOCTP+strTX_DOCNO+strTX_PRDCD,staTXDOC);
		        if (!L_rstRSSET.next())
		            break;
			}
		    L_rstRSSET.close();
			//System.out.println("IN crtTXDOC");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtTXDOC");
		}
		return;
	}


	/** Function for  Picking up Doc.Tax Details
	 * 
	*/
	private String getTXDOC(String LP_CMPCD,String LP_SYSCD, String LP_SBSTP, String LP_DOCTP, String LP_DOCNO, String LP_PRDCD, String LP_FLDNM)
	{
		//System.out.println("Received : "+LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD+LP_FLDNM);
		String L_strRETRN = "0.00";
		try
		{
			if(!hstTXDOC.containsKey(LP_CMPCD+LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD))
				return L_strRETRN;
	        String[] staTXDOC = (String[])hstTXDOC.get(LP_CMPCD+LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD);
		        if (LP_FLDNM.equals("EXCVL"))
	                L_strRETRN = staTXDOC[intAE_TX_EXCVL];
		        else if (LP_FLDNM.equals("EDCVL"))
	                L_strRETRN = staTXDOC[intAE_TX_EDCVL];
		        else if (LP_FLDNM.equals("EHCVL"))
	                L_strRETRN = staTXDOC[intAE_TX_EHCVL];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getTXDOC");
		}
		return L_strRETRN;
	}
/**
 * Function for fetching up D.O. grade details
 */
	
	public boolean getDATA()
	{
		
		
		vtrPRDCD.clear();
		vtrPRDDS.clear();
		vtrPKGTP.clear();
		vtrSTKQT.clear();
		vtrLADRT.clear();
		vtrPRDRF.clear();
		vtrEXCRT.clear();
		vtrEDCRT.clear();
		vtrEHCRT.clear();
        vtrLOTRF.clear();
		tblLODDTL.clrTABLE();
		int L_intRCCNT=0;
		String L_strDORNO=txtDORNO.getText().trim();
		strLADNO=txtLADNO.getText().trim();
		setMSG("Picking up D.O. Grades details",'N');
		
		try
		{
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				PRE_getDORDTL1_ADD.setString(1,cl_dat.M_strCMPCD_pbst);
				PRE_getDORDTL1_ADD.setString(2,strMKTTP);
				PRE_getDORDTL1_ADD.setString(3,L_strDORNO);
				M_rstRSSET = PRE_getDORDTL1_ADD.executeQuery();			
			}
			else if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				//String L_strTEMP = "select DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,DOT_STSFL,sum(ST_STKQT-ST_ALOQT) ST_STKQT FROM MR_DOTRN,MR_INTRN left outer join FG_STMST on DOT_PRDCD=ST_PRDCD AND DOT_PKGTP=ST_PKGTP where DOT_MKTTP = INT_MKTTP and DOT_INDNO = INT_INDNO and DOT_PRDCD = INT_PRDCD and DOT_PKGTP = INT_PKGTP and INT_STSFL in ('H','O','P','C','I','A','1','2','3') and DOT_STSFL<>'X' and DOT_MKTTP = '"+strMKTTP+"'and DOT_DORNO = '"+L_strDORNO+ "' and  DOT_DORQT >= DOT_LADQT " ;
				//L_strTEMP += " and DOT_STSFL <> 'X' and dot_prdcd not in (select ivt_prdcd from mr_ivtrn where ivt_ladno = '"+strLADNO+"'  and ivt_stsfl not in ('A','L','D')) GROUP BY DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORQT,DOT_LADQT,DOT_LOTRF,INT_BASRT, INT_EXCRT,DOT_STSFL";
				PRE_getDORDTL1_MOD.setString(1,cl_dat.M_strCMPCD_pbst);
				PRE_getDORDTL1_MOD.setString(2,strMKTTP);
                PRE_getDORDTL1_MOD.setString(3,L_strDORNO);
				PRE_getDORDTL1_MOD.setString(4,cl_dat.M_strCMPCD_pbst);
				PRE_getDORDTL1_MOD.setString(5,strMKTTP);
                PRE_getDORDTL1_MOD.setString(6,strLADNO);
                M_rstRSSET = PRE_getDORDTL1_MOD.executeQuery();
				//System.out.println("L-str == "+L_strTEMP);
				//M_rstRSSET=cl_dat.exeSQLQRY(L_strTEMP);
				//setLADDTL1(strMKTTP,strLADNO);
			}
			else 
			{
				PRE_getDORDTL1_OTH.setString(1,cl_dat.M_strCMPCD_pbst);
				PRE_getDORDTL1_OTH.setString(2,strMKTTP);
                PRE_getDORDTL1_OTH.setString(3,L_strDORNO);
				PRE_getDORDTL1_OTH.setString(4,cl_dat.M_strCMPCD_pbst);
                PRE_getDORDTL1_OTH.setString(5,strMKTTP);
                PRE_getDORDTL1_OTH.setString(6,strLADNO);
                M_rstRSSET=PRE_getDORDTL1_OTH.executeQuery();				
				//setLADDTL1(strMKTTP,strLADNO);
			}
			int i=0;
			int L_intCANDO=0;
			String L_strPKGWT="";
			String L_strPKGTP="";
			
			
			if(tblLODDTL.isEditing())
				tblLODDTL.getCellEditor().stopCellEditing();
			tblLODDTL.setRowSelectionInterval(0,0);
			tblLODDTL.setColumnSelectionInterval(0,0);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					//System.out.println("IN 0000LAST");
					strPRDCD = getRSTVAL(M_rstRSSET,"DOT_PRDCD","C");	
					strPRDDS = getRSTVAL(M_rstRSSET,"DOT_PRDDS","C");
					//strPKGTP = getRSTVAL(rstRSSET,"DOT_PKGTP","C");
					strDORQT = getRSTVAL(M_rstRSSET,"DOT_DORQT","C");
					strLADQT = getRSTVAL(M_rstRSSET,"DOT_LADQT","C");
					strPKGTP = getRSTVAL(M_rstRSSET,"DOT_PKGTP","C");
					strLOTRF = getRSTVAL(M_rstRSSET,"DOT_LOTRF","C");
					strLADRT = getRSTVAL(M_rstRSSET,"INT_BASRT","C");
					strPRDRF = getRSTVAL(M_rstRSSET,"INT_PRDRF","C");
					strEXCRT = getTXDOC(cl_dat.M_strCMPCD_pbst,"MR",strMKTTP,"IND",strINDNO,"XXXXXXXXXX","EXCVL");
					strEDCRT = "0.00";
					strEHCRT = "0.00";
					if(Double.parseDouble(strEXCRT)>0)
						{strEDCRT = "0.30";strEHCRT = "0.10";}
					strSTKQT = getRSTVAL(M_rstRSSET,"ST_STKQT","C");
					strSTSFL1 = getRSTVAL(M_rstRSSET,"DOT_STSFL","C");

					if (strSTSFL1.equals("X"))
					{
						L_intCANDO += 1;
						continue;
					}

					dblDORQT = Double.parseDouble(strDORQT);
					dblLADQT = Double.parseDouble(strLADQT);
					//System.out.println(" dblDORQT = "+dblDORQT);
					//System.out.println(" dblLADQT = "+dblLADQT);
					//strBALQT  = setFMT("RND",String.valueOf(dblDORQT-dblLADQT),3);
					strBALQT  = setNumberFormat((dblDORQT-dblLADQT),3).toString();
					strPKGWT= getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",strPKGTP);
					//strPKGWT  = cl_dat.ocl_dat.getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",strPKGTP);
					//System.out.println(" strBALQT = "+strBALQT);
					
					vtrPRDCD.addElement(strPRDCD);
					vtrPRDDS.addElement(strPRDDS);
					vtrPKGTP.addElement(strPKGTP);
					vtrSTKQT.addElement(strSTKQT);
					vtrLADRT.addElement(strLADRT);
					vtrPRDRF.addElement(strPRDRF);
					vtrLOTRF.addElement(strLOTRF);
					vtrEXCRT.addElement(strEXCRT);
					vtrEDCRT.addElement(strEDCRT);
					vtrEHCRT.addElement(strEHCRT);
				
					
					//System.out.println(" dblBALQT = "+strBALQT);
					
		           	tblLODDTL.setValueAt(strPRDCD,i,TB1_PRDCD);
					//System.out.println("IN LAST2");
	              	tblLODDTL.setValueAt(strPRDDS,i,TB1_PRDDS);
					//System.out.println("IN LAST22");
					//L_strPKGTP=M_rstRSSET.getString("DOT_PKGTP");
	              	tblLODDTL.setValueAt(strPKGTP,i,TB1_PKGTP);
					//System.out.println("IN LAST222");
	              	tblLODDTL.setValueAt(strDORQT,i,TB1_DORQT);
					//System.out.println("IN LAST2222");
					tblLODDTL.setValueAt(strBALQT,i,TB1_BALQT);
					
					//System.out.println("IN LAST22222");
					//L_strPKGWT= getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",L_strPKGTP);
					//System.out.println(" ---"+L_strPKGWT);
					tblLODDTL.setValueAt(strPKGWT,i,TB1_PKGWT);
					tblLODDTL.setValueAt(strSTKQT,i,TB1_STKQT);
					//System.out.println("IN LAST22222");
					i++;
					L_intRCCNT++;
				}
				 if(L_intCANDO>0)
				{
					if(L_intCANDO ==i)
					{
						tblLODDTL.clrTABLE();
						setMSG("D.O. is Cancelled",'E');
						return false;
					}
				}
			}
			if(L_intRCCNT==0)
			{
				return false;
			}
				
		}
		catch(Exception E)
		{
			setMSG(E,"IN Getdata");
		}
		return true;
	}
	
	/**
	 * Function for validation 
	 */
	public boolean vldDATA()
	{
		String strTEMP="";
		int L_intRWCNT=0;
			
		if(tblLODDTL.isEditing())
				tblLODDTL.getCellEditor().stopCellEditing();
		tblLODDTL.setRowSelectionInterval(0,0);
		tblLODDTL.setColumnSelectionInterval(0,0);
		if(txtDORNO.getText().trim().length()!=8)
		{
			setMSG("Invalid DO Number..",'E');
			txtDORNO.requestFocus();
			return false;
		}
		if(txtLRYNO.getText().trim().length()==0)
		{
			if(cmbMKTTP.getSelectedItem().toString().substring(0,2).equals("03"))
				return true;
					
			setMSG("Invalid Gate in number..",'E');
			txtGINNO.requestFocus();
			return false;
		}
		if(txtLDGSQ.getText().trim().length()==0)
		{
			setMSG("Loading Seq. can't blank..",'E');
			txtLDGSQ.requestFocus();
			return false;
		}
		if(txtBYRCD.getText().trim().length()!=5)
		{
			setMSG("Invalid Buyer Code",'E');
			return false;
		}
	
		if(txtCNSCD.getText().trim().length()!=5)
		{
			setMSG("Invalid Consignee Code ",'E');
			return false;
		}
			
		for(int i=0;i<tblLODDTL.getRowCount();i++)
    	{
			if(tblLODDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
    		{
				strTEMP = nvlSTRVL(tblLODDTL.getValueAt(i,TB1_REQQT).toString(),"");
    			if(strTEMP.length() == 0)
    			{
    				setMSG("Requested Quantity  Can not be Blank..",'E');
					return false;
    			}
				strTEMP = nvlSTRVL(tblLODDTL.getValueAt(i,TB1_REQPK).toString(),"");
    			if(strTEMP.length() ==0)
    			{
    				setMSG("Packages can't be Blank..",'E');
					return false;
    			}
				L_intRWCNT++;
			}
		}
		if(L_intRWCNT>6)
		{
			setMSG(" No. of Grades more than  6.. ",'E');
			return false;
		}
		if(L_intRWCNT==0)
		{
			setMSG(" No grade selected.. ",'E');
			return false;
		}
		
		return true;
	}
	
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	*/
	public void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			//dblADJVL=0.0;
			if(!vldDATA())
			{
				return;
			}
			
			strDORNO = txtDORNO.getText().trim();
			strAUTBY = txtAUTBY.getText();
			strTRPCD = txtTRPCD.getText();
			strLRYNO = txtLRYNO.getText();
			strLR_NO = txtLRNO.getText();
			strLR_DT = txtLRDT.getText();
			strADLNO = txtLICNO.getText();
			strADLDT = txtLICDT.getText();
			strCNTDS = txtCNTNO.getText();
			strGINNO = txtGINNO.getText();
			strSLRFL = txtMATCT.getText().trim().substring(0,1);
			strTSLNO = txtSELNO.getText();
			strLDGSQ = getSUBSTR(txtLDGSQ.getText(),0,2);
			strCSLNO = txtEXSNO.getText();
			strREMDS = txtREMDS.getText();
			strFRMNO = txtFRMNO.getText();
			strFRMTP = txtFRMTP.getText();
			strSTSFL = "A";
			strTRNFL = "0";
			strRSLNO = "";
			strLUSBY = cl_dat.M_strUSRCD_pbst;
			strLUPDT = cl_dat.M_strLOGDT_pbst;
			setCursor(cl_dat.M_curWTSTS_pbst);
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				exeSAVETBL();
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				
				exeUPDREM();
                exeUPDWBT();
				if(flgPOSTINVFL)
				{
					//System.out.println("flgPOSTINVFL 01= "+flgPOSTINVFL);
					exeUPDIVT_POST();
					//System.out.println("flgPOSTINVFL After 1 = "+flgPOSTINVFL);
				}
				else
				{
					//System.out.println("flgPOSTINVFL 02 = "+flgPOSTINVFL);
					exeSAVETBL();
					//System.out.println("flgPOSTINVFL After 2  = "+flgPOSTINVFL);
				}
			}			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				strLADNO = getLADNO();
				JOptionPane.showMessageDialog(this," LA No. assigned is :  "+strLADNO,"Message",JOptionPane.INFORMATION_MESSAGE);
				exeUPDREM();
                exeUPDWBT();
				exeSAVETBL();
				updLADNO(txtLADNO.getText().trim());
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					String L_strLADNO=txtLADNO.getText().trim();
					//cl_dat.M_conSPDBA_pbst.commit();
					System.out.println("Before call");
					calUPDDO.setString(1,cl_dat.M_strCMPCD_pbst);
					calUPDDO.setString(2,strMKTTP);
					calUPDDO.setString(3,txtDORNO.getText().trim());
					System.out.println("strMKTTP = "+strMKTTP);
					System.out.println("txtDORNO = "+txtDORNO.getText().trim());
					calUPDDO.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();
					//System.out.println("After call");
					clrCOMP_1();	
					txtLADNO.setText(L_strLADNO);
					txtLADDT.setText(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText());
					setMSG("Saved Successfully ",'N');
				}
			}
			else
				setMSG("Error In Saving",'E');	
				
			
		}
		catch(Exception E)
		{
			setMSG(E,"ExeSAVE");
		}
	}
	/**
	 * Function for Update remark
	 * for this first it check that the remark is already available then it modified 
	 * if there is no Remark found in remark table then it enter the remark in remark table
	 */
	public void exeUPDREM()
	{
//			PRE_exeUPDREM = cl_dat.M_conSPDBA_pbst.prepareStatement("select * from MR_RMMST where RM_CMPCD = ? and RM_MKTTP = ?  and RM_TRNTP = ?  and RM_DOCNO = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		try
		{
			String L_strREMDS=txtREMDS.getText().trim();
			String L_strSTSFL="A";
			if(txtREMDS.getText().trim().equals("")||txtREMDS.getText().trim().length()==0)
				return;
			String L_strLADNO=txtLADNO.getText().trim();
			PRE_exeUPDREM.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_exeUPDREM.setString(2,strMKTTP);
			PRE_exeUPDREM.setString(3,"LA");
			PRE_exeUPDREM.setString(4,L_strLADNO);
			M_rstRSSET = PRE_exeUPDREM.executeQuery();
			if(M_rstRSSET.next())
			{
				M_rstRSSET.updateString("RM_REMDS",L_strREMDS);
			    M_rstRSSET.updateString("RM_STSFL",L_strSTSFL);
			    M_rstRSSET.updateString("RM_LUSBY",cl_dat.M_strUSRCD_pbst);
			    M_rstRSSET.updateDate("RM_LUPDT",setSQLDAT1(cl_dat.M_strLOGDT_pbst));
			    M_rstRSSET.updateRow();
			}
			else
			{
				//System.out.println("IN ELSE REMARK");
			    M_rstRSSET.moveToInsertRow();
			    M_rstRSSET.updateString("RM_CMPCD",cl_dat.M_strCMPCD_pbst);
			    M_rstRSSET.updateString("RM_MKTTP",strMKTTP);
			    M_rstRSSET.updateString("RM_TRNTP","LA");
			    M_rstRSSET.updateString("RM_DOCNO",L_strLADNO);
			    M_rstRSSET.updateString("RM_REMDS",L_strREMDS);
			    M_rstRSSET.updateString("RM_STSFL",L_strSTSFL);
			    M_rstRSSET.updateString("RM_LUSBY",cl_dat.M_strUSRCD_pbst);
			    M_rstRSSET.updateDate("RM_LUPDT",setSQLDAT1(cl_dat.M_strLOGDT_pbst));
			    M_rstRSSET.insertRow();
			    M_rstRSSET.moveToCurrentRow();
				//cl_dat.ocl_dat.exeDBCMT("SP","ACT","CMT");
				//cl_dat.M_conSPDBA_pbst.commit();
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
        }
		catch (Exception L_EX)
        {
			setMSG(L_EX,"exeUPDREM");
        }
	}
	
	/**
	 * Update status flag of MM_WBTRN table 
	*/
	private void exeUPDWBT()
	{
//			PRE_exeUPDWBT = cl_dat.M_conSPDBA_pbst.prepareStatement("update MM_WBTRN set WB_STSFL = '1', WB_ORDRF = ?, WB_LRYNO = ? , WB_PRTCD = ? , WB_PRTDS = ? , WB_TPRCD = ? , WB_TPRDS = ? , WB_LUSBY = ? , WB_LUPDT = ?  where WB_CMPCD = ? and WB_DOCTP = '03' and WB_DOCNO = ? and wb_stsfl='0'");
		try 
		{
		    PRE_exeUPDWBT.setString(1,txtLADNO.getText().trim());
		    PRE_exeUPDWBT.setString(2,txtLRYNO.getText().trim());
		    PRE_exeUPDWBT.setString(3,txtBYRCD.getText().trim());
		    PRE_exeUPDWBT.setString(4,lblBYRNM.getText().trim());
		    PRE_exeUPDWBT.setString(5,txtTRPCD.getText().trim());
		    PRE_exeUPDWBT.setString(6,lblTRPNM.getText().trim());
		    PRE_exeUPDWBT.setString(7,cl_dat.M_strUSRCD_pbst);
		    PRE_exeUPDWBT.setDate(8,setSQLDAT1(cl_dat.M_strLOGDT_pbst));
		    PRE_exeUPDWBT.setString(9,cl_dat.M_strCMPCD_pbst);
		    PRE_exeUPDWBT.setString(10,txtGINNO.getText().trim());
		    PRE_exeUPDWBT.executeUpdate();
			//cl_dat.ocl_dat.exeDBCMT("SP","ACT","CMT");
			//cl_dat.M_conSPDBA_pbst.commit();
	    }
		catch (Exception L_EX) 
		{
            setMSG(L_EX,"exeUPDWBT");
        }
	}
	/**
	 * Function for updating LR_No And LR_date of MR_IVTRN table
	 */
	private void exeUPDIVT_POST()
	{                        
		try
		{
//			PRE_exeUPDIVT_POST = cl_dat.M_conSPDBA_pbst.prepareStatement("select * from MR_IVTRN  where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? ",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			PRE_exeUPDIVT_POST.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_exeUPDIVT_POST.setString(2,strMKTTP);
			PRE_exeUPDIVT_POST.setString(3,strLADNO);
			ResultSet L_rstRSSET = PRE_exeUPDIVT_POST.executeQuery();

			if(L_rstRSSET.next())
			{
				L_rstRSSET.updateString("IVT_LR_NO",strLR_NO);
				L_rstRSSET.updateDate("IVT_LR_DT",setSQLDAT1(strLR_DT));
				L_rstRSSET.updateString("IVT_LUSBY",strLUSBY);
				L_rstRSSET.updateDate("IVT_LUPDT",setSQLDAT1(cl_dat.M_strLOGDT_pbst));
				L_rstRSSET.updateRow();
			} 
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"exeUPDIVT_POST");
		}
	}
/**
 * Function for saving Product table details
 */
	private void exeSAVETBL()
	{
		try
		{
			int i = 0;
			
			for(i=0;i<tblLODDTL.getRowCount();i++)
    		{
				if(tblLODDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
    			{
			
					strPRDCD = tblLODDTL.getValueAt(i,TB1_PRDCD).toString().trim();
					if (strPRDCD.substring(0,2).equals("51"))
					{
						strPRDTP = "01";
					}
					else if (strPRDCD.substring(0,2).equals("52"))
					{
						strPRDTP = "02";
					}
					int L_intRECNT = 0;
					if (strPRDCD.length()==0)
					{
						break;
					}
					strDORNO = nvlSTRVL(txtDORNO.getText(),"");
					strPKGTP = tblLODDTL.getValueAt(i,TB1_PKGTP).toString().trim();
					strREQQT = tblLODDTL.getValueAt(i,TB1_REQQT).toString().trim();
					//strPRDDS = getPRDDS(strPRDCD);
					strPRDDS = getVTRVL(vtrPRDDS,strPRDCD,strPKGTP);
					strSTKQT = getVTRVL(vtrSTKQT,strPRDCD,strPKGTP);
					strLADRT = getVTRVL(vtrLADRT,strPRDCD,strPKGTP);
					strPRDRF = getVTRVL(vtrPRDRF,strPRDCD,strPKGTP);
					strEXCRT = getVTRVL(vtrEXCRT,strPRDCD,strPKGTP);
					strEDCRT = getVTRVL(vtrEDCRT,strPRDCD,strPKGTP);
					strEHCRT = getVTRVL(vtrEHCRT,strPRDCD,strPKGTP);
					strLOTRF = getVTRVL(vtrLOTRF,strPRDCD,strPKGTP);
					strPKGWT = getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",strPKGTP);
					
					M_strSQLQRY = "select count(*) from MR_IVTRN where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP = '"+strMKTTP+"' and IVT_LADNO = '"+strLADNO+"' and IVT_PRDCD = '"+strPRDCD+"' and IVT_PKGTP = '"+strPKGTP+"'";
					//System.out.println("M_strSQLQRY ="+M_strSQLQRY);
					
//			PRE_exeSAVETRN  = cl_dat.M_conSPDBA_pbst.prepareStatement("select count(*) from MR_IVTRN where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? and IVT_PRDCD = ? and IVT_PKGTP = ?");
					PRE_exeSAVETRN.setString(1,cl_dat.M_strCMPCD_pbst);
					PRE_exeSAVETRN.setString(2,strMKTTP);
					PRE_exeSAVETRN.setString(3,strLADNO);	
					PRE_exeSAVETRN.setString(4,strPRDCD);
					PRE_exeSAVETRN.setString(5,strPKGTP);
					L_intRECNT = getPRPCNT(PRE_exeSAVETRN);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						if(!chkDELIVT1(strMKTTP,strLADNO,strPRDCD,strPKGTP))
						{
							i += 1;
							continue;
						}
					}
					//System.out.println("\n L_intRECNT 4444444444444444   ="+L_intRECNT);
					if(L_intRECNT > 0)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							System.out.println("\n IN Modification exeUPDIVT()");
							if(!chkDODEL())
							{
								System.out.println("NOT SAVE");
								JOptionPane.showMessageDialog(this," please check against Delivery schedule date,","Error Message",JOptionPane.INFORMATION_MESSAGE);
								setMSG("please check against Delivery schedule date",'E');
								return;
							}
							System.out.println(" SAVEing ");
							updDODEL();
							exeUPDIVT();
							//System.out.println("\n IN22 Modification exeUPDIVT()");
							setMSG(strLADNO+" Updated Successfully",'N');
						}
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						{
							if(exeDELIVT())
							{
								if(exeDELREM(strMKTTP,"LA",strLADNO))
								{
									M_strSQLQRY = "UPDATE MR_DODEL set DOD_LADNO = ' ' where DOD_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOD_MKTTP='"+strMKTTP+"' AND DOD_DORNO='"+txtDORNO.getText().trim()+"' AND DOD_PRDCD='"+strPRDCD+"' AND DOD_PKGTP='"+strPKGTP+"' AND DOD_LADNO = '"+strLADNO+"'  and isnull(dod_dorqt,0)>0 and dod_stsfl <> 'X' ";
									System.out.println(M_strSQLQRY);
									//cl_dat.exeSQLUPD(M_strSQLQRY);
									cl_dat.exeSQLUPD(M_strSQLQRY," ");

									setMSG(strLADNO+" Deleted Successfully",'N');
								}
							}
							else
							   setMSG(strLADNO+" Not Deleted",'E');
						}
						else
						{
							JOptionPane.showMessageDialog(this," Invalid option selected","Error Message",JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{
						
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							System.out.println("BEfore exeUPDIVT");
							if(!chkDODEL())
							{
								System.out.println("NOT SAVE");
								JOptionPane.showMessageDialog(this," please check against Delivery schedule date,","Error Message",JOptionPane.INFORMATION_MESSAGE);
								setMSG("please check against Delivery schedule date",'E');
								return;
							}
							System.out.println(" SAVEing ");
							updDODEL();
							exeUPDIVT();
							System.out.println("After  exeUPDIVT");
							setMSG(strLADNO+" Inserted Successfully",'N');
						}
						else
						{
							JOptionPane.showMessageDialog(this," Invalid option selected","Error Message",JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "select * from co_txdoc where tx_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and tx_syscd='MR' and tx_sbstp = '"+strMKTTP+"' and tx_doctp = 'IND' and tx_docno = '"+strINDNO+"' and tx_prdcd='XXXXXXXXXX'";
				System.out.println("\n  Tax Insert  "+M_strSQLQRY);
				rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if (rstRSSET.next() && rstRSSET != null)
					saveTXDOC(rstRSSET);
				if(rstRSSET!=null)
					rstRSSET.close();
				
				M_strSQLQRY = "select count(distinct ivt_ladno) ivt_ladct from mr_ivtrn where ivt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_ginno = '"+txtGINNO.getText()+"' and ivt_stsfl <> 'X'";
				System.out.println(M_strSQLQRY);
				rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if (rstRSSET.next() && rstRSSET != null)
				{
					if(Double.parseDouble(getRSTVAL(rstRSSET,"IVT_LADCT","N"))>1)
					{
						chkLDGSQ.setSelected(true);
						getLDGSQ(txtLADNO.getText());
					}
				}
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"exeSAVETBL");
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
		{
			System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 
	/**
	 * Function for generate new L.A. number
	 */
	public String getLADNO()
    {
		String L_strDOCTP="",L_strLADNO  = "",  L_strCODCD = "", L_strCCSVL = "0",L_CHP01="";// for DOC
		L_strDOCTP=(cmbMKTTP.getSelectedItem().toString()).substring(0,2);
	
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MR"+L_strDOCTP+"LAD'  and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP +"'  and  isnull(CMT_STSFL,'') <> 'X'";
			System.out.println(" M_strSQLQRY ="+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					//L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					System.out.println("L_CHP01 = "+L_CHP01);
					if(L_CHP01.trim().length() >0)
					{
						setMSG("dataBase IN USE",'E');
						M_rstRSSET.close();
						return null;
					}
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			//M_strSQLQRY = "Update CO_CDTRN set ";
			//M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			//M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			//M_strSQLQRY += " and CMT_CGSTP = 'MR"+L_strDOCTP+"LAD'";	
			//M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP+ "'";
			//System.out.println(" M_strSQLQRY ="+M_strSQLQRY);
			//cl_dat.M_flgLCUPD_pbst = true;
			//cl_dat.exeSQLUPD(M_strSQLQRY," ");
			if(cl_dat.exeDBCMT("genDOCNO"))
			{
				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
				for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
					L_strLADNO += "0";
				L_strCCSVL = L_strLADNO + L_strCCSVL;
				L_strLADNO = L_strCODCD + L_strCCSVL;
				txtLADNO.setText(L_strLADNO);
			}
			else 
				return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genDOCNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_strLADNO;
	}
	
	/**
	 * Function for getting package wait from CO_CDTRN table
	 */
	public String getPRMCOD(String P_strFLDNM, String P_strMGPTP, String P_strSGPTP, String L_strCODCD)
	{
		String L_strTEMP = "";
		String L_strSQLQRY ="select " + P_strFLDNM ;
		L_strSQLQRY += " from CO_CDTRN where CMT_CGMTP = "+ "'" + P_strMGPTP.trim() +"'" ;
		L_strSQLQRY += " AND CMT_CGSTP = " + "'" + P_strSGPTP.trim() + "'";
		L_strSQLQRY += " AND CMT_CODCD = " + "'" + L_strCODCD.trim() + "'";
		try
		{
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET.next())	
				L_strTEMP = L_rstRSSET.getString(1);
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
		catch(Exception L_SE)
		{
			//System.out.println("Error in getPRMCOD : "+L_SE.toString()); 
			setMSG(L_SE,"getPRMCOD");
		}
		return L_strTEMP;
	}
	/**
	 * Function for geting data from Vector 
	 * 
	*/

	private String getVTRVL(Vector LP_VTRNM,String LP_PRDCD, String LP_PKGTP) 
	{
		int i = 0;
		for(i = 0;i < LP_VTRNM.size();i++)
		{
			if(LP_PRDCD.equals(vtrPRDCD.get(i).toString().trim()) && LP_PKGTP.equals(vtrPKGTP.get(i).toString().trim())) 
			{
				break;
			} 
		} 
		return(LP_VTRNM.get(i).toString().trim());
	} 	
	/**
	 * Fonction for count the result Set
	*/
	private int getPRPCNT(PreparedStatement LP_PRPSTM)
	{
		int L_RETVAL = -1;
		try
		{
			M_rstRSSET = LP_PRPSTM.executeQuery();
			if(M_rstRSSET.next())
            L_RETVAL = M_rstRSSET.getInt(1);
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPRCNT");                                                           
			L_RETVAL = -1;
		} 
		return L_RETVAL;
	}
	
	public boolean chkDODEL()
	{
		try
		{
			ResultSet L_rstRSSET;
			cl_dat.M_flgLCUPD_pbst = true;
			int L_intRSSET=0;
			M_strSQLQRY = "select count(*) from MR_DODEL where DOD_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOD_MKTTP = '"+strMKTTP+"' and DOD_LADNO = 'XXXXXXXX' and DOD_PRDCD = '"+strPRDCD+"' and DOD_PKGTP = '"+strPKGTP+"' and DOD_DORNO='"+strDORNO+"'  and isnull(dod_dorqt,0)>0 and dod_stsfl <> 'X'";
			System.out.println("M_strSQLQRY  chkDODEL ="+M_strSQLQRY);
			L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET!=null && L_rstRSSET.next())
			{
				L_intRSSET=L_rstRSSET.getInt(1);
			}
			if(L_intRSSET>0)
			{
				cl_dat.M_flgLCUPD_pbst = true;
				return true;
			}
			else
			{
				cl_dat.M_flgLCUPD_pbst = false;
				return false;
			}
		}
		catch(Exception E)
		{
			setMSG(E,"updDODEL");
		}
		return true;
	}
		
		
	private void updDODEL()
	{
		try
		{
			M_strSQLQRY = "UPDATE MR_DODEL set DOD_LADNO = '"+strLADNO+"' where DOD_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOD_MKTTP='"+strMKTTP+"' ";
			M_strSQLQRY += " AND DOD_DORNO='"+txtDORNO.getText().trim()+"' AND DOD_PRDCD='"+strPRDCD+"' AND DOD_PKGTP='"+strPKGTP+"' AND DOD_LADNO = 'XXXXXXXX' and isnull(dod_dorqt,0)>0 and dod_stsfl <> 'X'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch(Exception E)
		{
			setMSG(E,"updDODEL");
		}
				

	}
	
	/**
	 * Function for Update or insert  into IVTRN table using updatable ResultSet
	 */
	
	private void exeUPDIVT()
	{                        
		try
        {
			String L_strLADNO=txtLADNO.getText().trim();
			strLR_DT = txtLRDT.getText();
			//System.out.println("IN 1 L_strLADNO" + L_strLADNO);
//			PRE_exeUPDIVT = cl_dat.M_conSPDBA_pbst.prepareStatement("select * from MR_IVTRN  where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? and IVT_PRDCD = ? and IVT_PKGTP = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			PRE_exeUPDIVT.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_exeUPDIVT.setString(2,strMKTTP);
			PRE_exeUPDIVT.setString(3,L_strLADNO);
			PRE_exeUPDIVT.setString(4,strPRDCD);
			PRE_exeUPDIVT.setString(5,strPKGTP);
			//System.out.println("IN 1 exeUPDIVT = " + PRE_exeUPDIVT);
			ResultSet L_rstRSSET = PRE_exeUPDIVT.executeQuery();
			//System.out.println("IN 1 exeUPDIVT = " + PRE_exeUPDIVT);

			//System.out.println("IN exeUPDIVT");
            if(L_rstRSSET.next())
			{
                L_rstRSSET.updateString("IVT_TMOCD",strTMOCD);
                L_rstRSSET.updateString("IVT_AUTBY",strAUTBY);
                L_rstRSSET.updateString("IVT_LRYNO",strLRYNO);
                L_rstRSSET.updateString("IVT_CNTDS",strCNTDS);
                L_rstRSSET.updateString("IVT_LR_NO",strLR_NO);
                L_rstRSSET.updateDate("IVT_LR_DT",setSQLDAT1(strLR_DT));
                L_rstRSSET.updateString("IVT_ADLNO",strADLNO);
                L_rstRSSET.updateDate("IVT_ADLDT",setSQLDAT1(strADLDT));
                L_rstRSSET.updateString("IVT_SLRFL",strSLRFL);
                L_rstRSSET.updateString("IVT_TSLNO",strTSLNO);
				//System.out.println("Lod Seq");
                L_rstRSSET.updateInt("IVT_UNLSQ",Integer.parseInt(strLDGSQ));
				//System.out.println("Lod Seq"+strLDGSQ);
                L_rstRSSET.updateString("IVT_CSLNO",strCSLNO);
                L_rstRSSET.updateString("IVT_TSTFL",strTSTFL);
                L_rstRSSET.updateDouble("IVT_REQQT",Double.parseDouble(strREQQT));
                L_rstRSSET.updateString("IVT_STSFL",strSTSFL);
                L_rstRSSET.updateString("IVT_GINNO",strGINNO);
                L_rstRSSET.updateString("IVT_SALTP",strSALTP);
                L_rstRSSET.updateString("IVT_ZONCD",strZONCD);
                L_rstRSSET.updateString("IVT_TRPCD",strTRPCD);
                L_rstRSSET.updateString("IVT_CURCD",strCURCD);
                L_rstRSSET.updateDouble("IVT_ECHRT",Double.parseDouble(strECHRT));
                L_rstRSSET.updateString("IVT_DSTDS",strDSTDS);
                L_rstRSSET.updateDouble("IVT_CPTVL",Double.parseDouble(strCPTVL));
                L_rstRSSET.updateDouble("IVT_PKGWT",Double.parseDouble(strPKGWT));
                L_rstRSSET.updateString("IVT_DTPCD",strDTPCD);
                L_rstRSSET.updateString("IVT_PMTCD",strPMTCD);
                L_rstRSSET.updateString("IVT_RSLNO",strRSLNO);
                L_rstRSSET.updateString("IVT_PRDDS",strPRDDS);
                L_rstRSSET.updateString("IVT_PORNO",strPORNO);
                L_rstRSSET.updateDate("IVT_PORDT",setSQLDAT1(strPORDT));
                L_rstRSSET.updateString("IVT_LOTRF",strLOTRF);
                L_rstRSSET.updateString("IVT_FILRF",strFILRF);
                L_rstRSSET.updateString("IVT_DELAD",strDELAD);
                L_rstRSSET.updateString("IVT_PRDRF",strPRDRF);
                L_rstRSSET.updateString("IVT_TRNFL","0");
                L_rstRSSET.updateString("IVT_PRDRF",strPRDRF);
                L_rstRSSET.updateDouble("IVT_LADRT",Double.parseDouble(strLADRT));
                L_rstRSSET.updateDouble("IVT_EXCRT",Double.parseDouble(strEXCRT));
                L_rstRSSET.updateDouble("IVT_EDCRT",Double.parseDouble(strEDCRT));
                L_rstRSSET.updateDouble("IVT_EHCRT",Double.parseDouble(strEHCRT));
                L_rstRSSET.updateString("IVT_UOMCD","MT");
                L_rstRSSET.updateString("IVT_LUSBY",strLUSBY);
                L_rstRSSET.updateString("IVT_SBSCD",strZONCD+strSALTP+"00");
				L_rstRSSET.updateString("IVT_SBSCD1",strZONCD+getPRDTP(strPRDCD)+"00");
                L_rstRSSET.updateDate("IVT_LUPDT",setSQLDAT1(cl_dat.M_strLOGDT_pbst));
                L_rstRSSET.updateRow();
            }
            else
            {
                L_rstRSSET.moveToInsertRow();
				//System.out.println( "1 strMKTTP =  "+strMKTTP);
                L_rstRSSET.updateString("IVT_CMPCD",cl_dat.M_strCMPCD_pbst);
                L_rstRSSET.updateString("IVT_MKTTP",strMKTTP);
				
				//System.out.println( "Else 2 L_L_strLADNO =  "+L_strLADNO);
                L_rstRSSET.updateString("IVT_LADNO",L_strLADNO);
				
				//System.out.println( "3 strPRDCD =  "+strPRDCD);
                L_rstRSSET.updateString("IVT_PRDCD",strPRDCD);
				
				//System.out.println( "4 strPRDTP =  "+strPRDTP);
                L_rstRSSET.updateString("IVT_PRDTP",strPRDTP);
				
				//System.out.println( "5 strPKGTP =  "+strPKGTP);
                L_rstRSSET.updateString("IVT_PKGTP",strPKGTP);
				
				
                //L_rstRSSET.updateTimestamp("IVT_LADDT",cc_dattm.occ_dattm.setSQLTIM(strLADTM.substring(0,10)+" 00:01"));
                //L_rstRSSET.updateTimestamp("IVT_LADDT",cc_dattm.occ_dattm.setSQLTIM(strLADTM));
				//System.out.println( "6 strLADTM =  "+strLADTM);
				L_rstRSSET.updateTimestamp("IVT_LADDT",setSQLTIM(strLADTM));
				//M_fmtDBDTM.format(M_fmtLCDTM.parse(txtRCTDT.getText()+" "+tblENTTB1.getValueAt(i,TB1_STRTM).toString()))
				
				//System.out.println( "7 strDORNO =  "+strDORNO);
                L_rstRSSET.updateString("IVT_DORNO",strDORNO);
                L_rstRSSET.updateString("IVT_DOANO",strDOANO);
				
				//System.out.println( "8 strDORDT =  "+strDORDT);
                L_rstRSSET.updateDate("IVT_DORDT",setSQLDAT1(strDORDT));
				
				//System.out.println( "9 strINDNO =  "+strINDNO);
                L_rstRSSET.updateString("IVT_INDNO",strINDNO);
				
				//System.out.println( "10 strGINNO =  "+strGINNO);
				L_rstRSSET.updateString("IVT_GINNO",strGINNO);
				
				//System.out.println( "11 strZONCD =  "+strZONCD);
                L_rstRSSET.updateString("IVT_ZONCD",strZONCD);
				
				//System.out.println( "12 strSALTP =  "+strSALTP);
                L_rstRSSET.updateString("IVT_SALTP",strSALTP);
				
				//System.out.println( "13 strCNSCD =  "+strCNSCD);
                L_rstRSSET.updateString("IVT_CNSCD",strCNSCD);
				
				//System.out.println( "14 strBYRCD =  "+strBYRCD);
                L_rstRSSET.updateString("IVT_BYRCD",strBYRCD);
                L_rstRSSET.updateString("IVT_GRPCD",strGRPCD);
				
				//System.out.println( "15 strDSRCD =  "+strDSRCD);
				L_rstRSSET.updateString("IVT_DSRTP",strDSRTP);
				L_rstRSSET.updateString("IVT_DSRCD",strDSRCD);
				
				//System.out.println( "16 strTMOCD =  "+strTMOCD);
                L_rstRSSET.updateString("IVT_TMOCD",strTMOCD);
				
				//System.out.println( "17 strCURCD =  "+strCURCD);
                L_rstRSSET.updateString("IVT_CURCD",strCURCD);
				
				//System.out.println( "18 strAUTBY =  "+strAUTBY);
                L_rstRSSET.updateString("IVT_AUTBY",strAUTBY);
				
				//System.out.println( "19 strLRYNO =  "+strLRYNO);
                L_rstRSSET.updateString("IVT_LRYNO",strLRYNO);
				
                L_rstRSSET.updateString("IVT_FRMNO",strFRMNO);
                L_rstRSSET.updateString("IVT_FRMTP",strFRMTP);
				//System.out.println( "20 strTRPCD =  "+strTRPCD);
                L_rstRSSET.updateString("IVT_TRPCD",strTRPCD);
				
				//System.out.println( "21 strCNTDS =  "+strCNTDS);
                L_rstRSSET.updateString("IVT_CNTDS",strCNTDS);
				
                L_rstRSSET.updateString("IVT_CFTAG",strCFTAG);
				//System.out.println( "22 strECHRT =  "+strECHRT);
                L_rstRSSET.updateDouble("IVT_ECHRT",Double.parseDouble(strECHRT));
				
				//System.out.println( "23 strDSTDS =  "+strDSTDS);
				L_rstRSSET.updateString("IVT_DSTDS",strDSTDS);
				
				
				//System.out.println( "24 strLR_NO =  "+strLR_NO);
                L_rstRSSET.updateString("IVT_LR_NO",strLR_NO);
				
				//System.out.println( "25 strLR_DT =  "+strLR_DT);
                L_rstRSSET.updateDate("IVT_LR_DT",setSQLDAT1(strLR_DT));
				
				//System.out.println( "26 strADLNO =  "+strADLNO);
                L_rstRSSET.updateString("IVT_ADLNO",strADLNO);
				
				
				//System.out.println( "27 strADLDT =  "+strADLDT);
                L_rstRSSET.updateDate("IVT_ADLDT",setSQLDAT1(strADLDT));
				
				//System.out.println( "28 strCPTVL =  "+strCPTVL);
                L_rstRSSET.updateDouble("IVT_CPTVL",Double.parseDouble(strCPTVL));
				
				//System.out.println( "29 strPKGWT =  "+strPKGWT);
                L_rstRSSET.updateDouble("IVT_PKGWT",Double.parseDouble(strPKGWT));
				
				//System.out.println( "30 strREQQT =  "+strREQQT);
                L_rstRSSET.updateDouble("IVT_REQQT",Double.parseDouble(strREQQT));
				
                L_rstRSSET.updateString("IVT_TRNFL","0");
				
				//System.out.println( "31 strSTSFL =  "+strSTSFL);
                L_rstRSSET.updateString("IVT_STSFL",strSTSFL);
				
				//System.out.println( "32 strLUSBY =  "+strLUSBY);
                L_rstRSSET.updateString("IVT_LUSBY",strLUSBY);
				
				//System.out.println( "33 strLUPDT =  "+strLUPDT);
				L_rstRSSET.updateDate("IVT_LUPDT",setSQLDAT1(strLUPDT));
				
				//System.out.println( "34 strDTPCD =  "+strDTPCD);
                L_rstRSSET.updateString("IVT_DTPCD",strDTPCD);
                
				//System.out.println( "35 strPMTCD =  "+strPMTCD);
				L_rstRSSET.updateString("IVT_PMTCD",strPMTCD);
				
				//System.out.println( "36 strTSTFL =  "+strTSTFL);
                L_rstRSSET.updateString("IVT_TSTFL",strTSTFL);
				
				//System.out.println( "\n 37 strMKTTP =  ");
                L_rstRSSET.updateString("IVT_EPIFL"," ");
				
				//System.out.println( "38 strSLRFL =  "+strSLRFL);
                L_rstRSSET.updateString("IVT_SLRFL",strSLRFL);
				
				//System.out.println( "39 strTSLNO =  "+strTSLNO);
                L_rstRSSET.updateString("IVT_TSLNO",strTSLNO);
				
				//System.out.println( "40 strLDGSQ =  "+strLDGSQ);
                L_rstRSSET.updateInt("IVT_UNLSQ",Integer.parseInt(strLDGSQ));
				
				//System.out.println( "41 strCSLNO =  "+strCSLNO);
                L_rstRSSET.updateString("IVT_CSLNO",strCSLNO);
				
				//System.out.println( "42 strRSLNO =  "+strRSLNO);
                L_rstRSSET.updateString("IVT_RSLNO",strRSLNO);
				
				//System.out.println( "43 strPRDDS =  "+strPRDDS);
                L_rstRSSET.updateString("IVT_PRDDS",strPRDDS);
				
				//System.out.println( "44 strPORNO =  "+strPORNO);
                L_rstRSSET.updateString("IVT_PORNO",strPORNO);
				
				//System.out.println( "45 strPORDT =  "+strPORDT);
                L_rstRSSET.updateDate("IVT_PORDT",setSQLDAT1(strPORDT));
				
				//System.out.println( "46 strLOTRF =  "+strLOTRF);
                L_rstRSSET.updateString("IVT_LOTRF",strLOTRF);
                L_rstRSSET.updateString("IVT_FILRF",strFILRF);
                L_rstRSSET.updateString("IVT_DELAD",strDELAD);
                L_rstRSSET.updateString("IVT_PRDRF",strPRDRF);
				
				//System.out.println( "47 strLADRT =  "+strLADRT);
				
				//System.out.println( "6 strLADRT =  "+strLADRT);
                L_rstRSSET.updateDouble("IVT_LADRT",Double.parseDouble(strLADRT));
				
				//System.out.println( "48 strEXCRT =  "+strEXCRT);
                L_rstRSSET.updateDouble("IVT_EXCRT",Double.parseDouble(strEXCRT));
				
				//System.out.println( "49 strEDCRT =  "+strEDCRT);
				L_rstRSSET.updateDouble("IVT_EDCRT",Double.parseDouble(strEDCRT));
				L_rstRSSET.updateDouble("IVT_EHCRT",Double.parseDouble(strEHCRT));
				
                L_rstRSSET.updateDouble("IVT_EDCVL",0.000);
                L_rstRSSET.updateDouble("IVT_EHCVL",0.000);
                L_rstRSSET.updateString("IVT_UOMCD","MT");
                L_rstRSSET.updateString("IVT_SBSCD",strZONCD+strSALTP+"00");
				L_rstRSSET.updateString("IVT_SBSCD1",strZONCD+getPRDTP(strPRDCD)+"00");
                L_rstRSSET.insertRow();
                L_rstRSSET.moveToCurrentRow();
                //cl_dat.ocl_dat.exeDBCMT("SP","ACT","CMT");
				//cl_dat.M_conSPDBA_pbst.commit();
				//System.out.println("IN LAST");
            }
			if(L_rstRSSET!=null)
				L_rstRSSET.close();///////////////////////
			
			
			
			M_strSQLQRY = "select * from co_txdoc where tx_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and tx_syscd='MR' and tx_sbstp = '"+strMKTTP+"' and tx_doctp = 'IND' and tx_docno = '"+strINDNO+"' and tx_prdcd='"+strPRDCD+"'";
			System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if (rstRSSET.next() && rstRSSET != null)
				saveTXDOC(rstRSSET);
			if(rstRSSET!=null)
				rstRSSET.close();
			/*M_strSQLQRY = "select count(distinct ivt_ladno) ivt_ladct from mr_ivtrn where ivt_ginno = '"+txtGINNO.getText()+"' and ivt_stsfl <> 'X'";
			System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if (rstRSSET.next() && rstRSSET != null)
				if(Double.parseDouble(getRSTVAL(rstRSSET,"IVT_LADCT","N"))>1)
				{
					chkLDGSQ.setSelected(true);
					getLDGSQ(txtLADNO.getText());
				}
			
			*/

		}
		catch (Exception L_EX)
        {
			setMSG(L_EX,"rrrr exeUPDIVT");
        }
	}
	
	
	
	/**
	 *  Method to update the last Document No.in the CO_CDTRN
	*/
	private void updLADNO(String P_strDOCNO)
	{
		try
		{
			String L_strMKTTP="";
		
			L_strMKTTP=(cmbMKTTP.getSelectedItem().toString()).substring(0,2) ;
			
			
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += "CMT_CCSVL = '" + P_strDOCNO.substring(3,8) + "' ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP ='MR"+L_strMKTTP+"LAD'";
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3)+ L_strMKTTP+"'";			
			System.out.println("UPDLADNO"+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			//exeSRLSET("D"+cl_dat.M_strCMPCD_pbst,"MR"+LM_MKTTP+"LAD",cl_dat.M_strFNNYR1_pbst.substring(3)+LM_MKTTP,strLADNO.substring(3,8));
		}
		catch(Exception e)
		{
			setMSG(e,"UPDLADNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * Function for conversion timestamp format
	 */
	public java.sql.Timestamp setSQLTIM(String LP_TIMFMT)
	{
		java.sql.Timestamp LP_SQLTIM = new java.sql.Timestamp(1234);
		//System.out.println("LP_SQLTIM = "+LP_SQLTIM);
		//System.out.println("LP_TIMFMT = "+LP_TIMFMT);
		
		try
		{
			if(LP_TIMFMT != null && LP_TIMFMT.length() == 16)
			{
				String LP_HOUR = LP_TIMFMT.substring(11,13).trim();
                String LP_MIN  = LP_TIMFMT.substring(14,16).trim();
                String LP_YEAR = LP_TIMFMT.substring(6,10).trim();
                String LP_MNTH = LP_TIMFMT.substring(3,5).trim();
                String LP_DAY  = LP_TIMFMT.substring(0,2).trim();
                LP_SQLTIM = java.sql.Timestamp.valueOf(LP_YEAR+"-"+LP_MNTH+"-"+LP_DAY+" "+LP_HOUR+":"+LP_MIN+":00.000000");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setSQLTIM");
		}
		//System.out.println("\n LP_SQLTIM = "+LP_SQLTIM);
        return LP_SQLTIM;
	}
	
	/**
	 * Function for Converting Simple date format into Data bese format
	 */
	public java.sql.Date setSQLDAT1(String LP_DATFMT)
	{
		java.sql.Date LP_SQLDAT = new java.sql.Date(1234);
		try
		{
			if(LP_DATFMT != null && LP_DATFMT.length() == 10)
			{
				String LP_YEAR = LP_DATFMT.substring(6,10).trim();
				String LP_MNTH = LP_DATFMT.substring(3,5).trim();
				String LP_DAY = LP_DATFMT.substring(0,2).trim();
				LP_SQLDAT = java.sql.Date.valueOf(LP_YEAR+"-"+LP_MNTH+"-"+LP_DAY);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setSQLDAT1");
		}
		return LP_SQLDAT;
	}
	/**
	 * Function for display remark details
	 */
	
   private void setREMDTL(String P_strMKTTP,String P_strTRNTP, String P_strDOCNO) 
	{
		String L_strREMDS="";
		try
		{
			setMSG("Picking up Remark",'N');
			//M_strSQLQRY = "Select RM_REMDS from MR_RMMST ";
			//M_strSQLQRY += " where RM_MKTTP = '"+P_strMKTTP+"' and RM_TRNTP = '"+LP_TRNTP+"' and RM_DOCNO = '"+LP_DOCNO+"'";

//			PRE_setREMDTL = cl_dat.M_conSPDBA_pbst.prepareStatement("Select RM_REMDS from MR_RMMST where RM_CMPCD = ? and RM_MKTTP = ? and RM_TRNTP = ? and RM_DOCNO = ?");
			PRE_setREMDTL.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_setREMDTL.setString(2,P_strMKTTP);
			PRE_setREMDTL.setString(3,P_strTRNTP);
			PRE_setREMDTL.setString(4,P_strDOCNO);
			ResultSet L_rstRSSET = PRE_setREMDTL.executeQuery();
			strREMDS = "";
			if (L_rstRSSET.next() && L_rstRSSET != null)
			{
				txtREMDS.setText(nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),""));
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setREMDTL");                                                    
		}
   }
    /**
     * Function for dispaly L.A. details at the time of Enqury, Modification And Delation
     */
	private boolean setLADDTL1(String P_strMKTTP, String P_strLADNO)
	{
		try
		{
			//M_strSQLQRY = "select IVT_PRDCD, IVT_PKGTP,IVT_REQQT from MR_IVTRN " ;
			//M_strSQLQRY += " where IVT_MKTTP = '"+P_strMKTTP+"' and IVT_LADNO = '"+P_strLADNO+"'";
			String L_strPRDCD="";
			String L_strPKGTP="";
			String L_strREQQT="";
			double L_dblBALQT=0.0;
			double L_dblPKGWT=0.0;
			double L_dblREQQT=0.0;
			boolean LM_ALLCANC=true;
			
//			PRE_setLADDTL1  = cl_dat.M_conSPDBA_pbst.prepareStatement("select IVT_PRDCD,IVT_PKGTP,IVT_REQQT,IVT_STSFL from MR_IVTRN where  IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ?");
			PRE_setLADDTL1.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_setLADDTL1.setString(2,P_strMKTTP);
			PRE_setLADDTL1.setString(3,P_strLADNO);
			ResultSet L_rstRSSET = PRE_setLADDTL1.executeQuery();
			if ((!L_rstRSSET.next()) || (L_rstRSSET == null))
			{
				setMSG("Grade level details not found in MR_IVTRN",'E');
				return false;
            }
			int L_ROWVL=0;
			double L_DORQT1;
           
			while (true) 
			{
				L_strPRDCD  = getRSTVAL(L_rstRSSET,"IVT_PRDCD","C");
			    L_strPKGTP  = getRSTVAL(L_rstRSSET,"IVT_PKGTP","C");
			    L_strREQQT  = getRSTVAL(L_rstRSSET,"IVT_REQQT","C");
				//System.out.println("JJJJJJJJJ"+L_strREQQT);
                if(!getRSTVAL(L_rstRSSET,"IVT_STSFL","C").equals("X"))
                {
                    LM_ALLCANC = false;
					//L_ROWVL = getTBLROW(LM_PRDCD_1,LM_PKGTP_1);
					//L_ROWVL     = getTBLROW(LM_PRDCD_1,LM_PKGTP_1);
					for(int i = 0;i< tblLODDTL.getRowCount()-1;i++)
					{
						L_ROWVL=i;
						if(L_strPRDCD.equals(vtrPRDCD.get(i).toString().trim()) && L_strPKGTP.equals(vtrPKGTP.get(i).toString().trim()))
						{
							break;
						}
					}
					strBALQT= tblLODDTL.getValueAt(L_ROWVL,TB1_BALQT).toString().trim();
					//System.out.println("LLLLL = "+strBALQT);
					L_dblPKGWT = Double.parseDouble(tblLODDTL.getValueAt(L_ROWVL,TB1_PKGWT).toString());
					L_dblREQQT = Double.parseDouble(L_strREQQT);
					L_dblPKGWT =L_dblREQQT/L_dblPKGWT;
					L_dblBALQT= Double.parseDouble(strBALQT)+ Double.parseDouble(L_strREQQT);
					
					//System.out.println("LLLLLAAA = "+L_dblPKGWT);
					strBALQT = setNumberFormat((L_dblBALQT),3).toString();
					tblLODDTL.setValueAt(strBALQT,L_ROWVL,TB1_BALQT);
					tblLODDTL.setValueAt(L_strREQQT,L_ROWVL,TB1_REQQT);
                    tblLODDTL.setValueAt(setNumberFormat((L_dblPKGWT),0).toString(),L_ROWVL,TB1_REQPK);
					//L_ROWVL++;
				}
				if(!L_rstRSSET.next()) 
				{
					break;
				}
            }
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
            if(LM_ALLCANC)
			{
				setMSG("Already Cancelled",'E'); 
				return false;
			}
        }
		catch (Exception L_EX)
        {
			setMSG(L_EX,"setLADDTL1");							   
        }
		return true;
	}

	/**
	 * Function for saving or updating of Tax Records
	 */
   	private void saveTXDOC(ResultSet LP_RSLSET)
	{
		try
		{
			if(!cl_dat.M_flgLCUPD_pbst)
				return;
			strWHRSTR =  "TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD = 'MR' and "
					 +"TX_SBSTP = '" +strMKTTP+"' and "
					 +"TX_DOCTP = 'LAD' and "
					 +"TX_DOCNO = '" +strLADNO+"' and "
					 +"TX_PRDCD = '" +getRSTVAL(LP_RSLSET,"TX_PRDCD","C")+"'";
			
			System.out.println("strWHRSTR = " +strWHRSTR);
			boolean LM_flgCHK_EXIST =  chkEXIST("CO_TXDOC",strWHRSTR);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && LM_flgCHK_EXIST)
			{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXDOC");
				//cl_dat.ocl_dat.M_LCLUPD = false;
				return;
			}
			if(!LM_flgCHK_EXIST)
			{
				M_strSQLQRY="insert into CO_TXDOC (TX_CMPCD,TX_SYSCD ,TX_SBSTP ,TX_DOCTP ,TX_DOCNO ,TX_PRDCD ,TX_SBSCD ,TX_TRNTP ,TX_DSBVL ,TX_DSBFL ,TX_EXCVL ,TX_EXCFL ,TX_PNFVL ,TX_PNFFL ,TX_CSTVL ,TX_CSTFL ,TX_STXVL ,TX_STXDS ,TX_STXFL ,TX_OCTVL ,TX_OCTFL ,TX_FRTVL ,TX_FRTFL ,TX_INSVL ,TX_INSFL ,TX_CDSVL ,TX_CDSFL ,TX_INCVL ,TX_INCFL ,TX_ENCVL ,TX_ENCFL ,TX_FNIVL ,TX_FNIFL ,TX_CDUVL ,TX_CDUFL ,TX_CLRVL ,TX_CLRFL ,TX_VATVL ,TX_VATFL ,TX_SCHVL ,TX_SCHFL ,TX_CVDVL ,TX_CVDFL ,TX_WCTVL ,TX_WCTFL ,TX_OTHVL ,TX_OTHDS ,TX_STSFL ,TX_TRNFL ,TX_LUSBY ,TX_LUPDT ,TX_OTHFL ,TX_RSTVL ,TX_RSTFL ,TX_AMDNO ,TX_EDCVL ,TX_EDCFL,TX_EHCVL ,TX_EHCFL) values ("
				+setINSSTR("TX_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
				+setINSSTR("TX_SYSCD","MR","C")
				+setINSSTR("TX_SBSTP",strMKTTP,"C")
				+setINSSTR("TX_DOCTP","LAD","C")
				+setINSSTR("TX_DOCNO",strLADNO,"C")
				+setINSSTR("TX_PRDCD",getRSTVAL(LP_RSLSET,"TX_PRDCD","C"),"C")
				+setINSSTR("TX_SBSCD",getRSTVAL(LP_RSLSET,"TX_SBSCD","C"),"C")
				+setINSSTR("TX_TRNTP",getRSTVAL(LP_RSLSET,"TX_TRNTP","C"),"C")
				+setINSSTR("TX_DSBVL",getRSTVAL(LP_RSLSET,"TX_DSBVL","N"),"N")
				+setINSSTR("TX_DSBFL",getRSTVAL(LP_RSLSET,"TX_DSBFL","C"),"C")
				+setINSSTR("TX_EXCVL",getRSTVAL(LP_RSLSET,"TX_EXCVL","N"),"N")
				+setINSSTR("TX_EXCFL",getRSTVAL(LP_RSLSET,"TX_EXCFL","C"),"C")
				+setINSSTR("TX_PNFVL",getRSTVAL(LP_RSLSET,"TX_PNFVL","N"),"N")
				+setINSSTR("TX_PNFFL",getRSTVAL(LP_RSLSET,"TX_PNFFL","C"),"C")
				+setINSSTR("TX_CSTVL",getRSTVAL(LP_RSLSET,"TX_CSTVL","N"),"N")
				+setINSSTR("TX_CSTFL",getRSTVAL(LP_RSLSET,"TX_CSTFL","C"),"C")
				+setINSSTR("TX_STXVL",getRSTVAL(LP_RSLSET,"TX_STXVL","N"),"N")
				+setINSSTR("TX_STXDS",getRSTVAL(LP_RSLSET,"TX_STXDS","C"),"C")
				+setINSSTR("TX_STXFL",getRSTVAL(LP_RSLSET,"TX_STXFL","C"),"C")
				+setINSSTR("TX_OCTVL",getRSTVAL(LP_RSLSET,"TX_OCTVL","N"),"N")
				+setINSSTR("TX_OCTFL",getRSTVAL(LP_RSLSET,"TX_OCTFL","C"),"C")
				+setINSSTR("TX_FRTVL",getRSTVAL(LP_RSLSET,"TX_FRTVL","N"),"N")
				+setINSSTR("TX_FRTFL",getRSTVAL(LP_RSLSET,"TX_FRTFL","C"),"C")
				+setINSSTR("TX_INSVL",getRSTVAL(LP_RSLSET,"TX_INSVL","N"),"N")
				+setINSSTR("TX_INSFL",getRSTVAL(LP_RSLSET,"TX_INSFL","C"),"C")
				+setINSSTR("TX_CDSVL",getRSTVAL(LP_RSLSET,"TX_CDSVL","N"),"N")
				+setINSSTR("TX_CDSFL",getRSTVAL(LP_RSLSET,"TX_CDSFL","C"),"C")
				+setINSSTR("TX_INCVL",getRSTVAL(LP_RSLSET,"TX_INCVL","N"),"N")
				+setINSSTR("TX_INCFL",getRSTVAL(LP_RSLSET,"TX_INCFL","C"),"C")
				+setINSSTR("TX_ENCVL",getRSTVAL(LP_RSLSET,"TX_ENCVL","N"),"N")
				+setINSSTR("TX_ENCFL",getRSTVAL(LP_RSLSET,"TX_ENCFL","C"),"C")
				+setINSSTR("TX_FNIVL",getRSTVAL(LP_RSLSET,"TX_FNIVL","N"),"N")
				+setINSSTR("TX_FNIFL",getRSTVAL(LP_RSLSET,"TX_FNIFL","C"),"C")
				+setINSSTR("TX_CDUVL",getRSTVAL(LP_RSLSET,"TX_CDUVL","N"),"N")
				+setINSSTR("TX_CDUFL",getRSTVAL(LP_RSLSET,"TX_CDUFL","C"),"C")
				+setINSSTR("TX_CLRVL",getRSTVAL(LP_RSLSET,"TX_CLRVL","N"),"N")
				+setINSSTR("TX_CLRFL",getRSTVAL(LP_RSLSET,"TX_CLRFL","C"),"C")
				+setINSSTR("TX_VATVL",getRSTVAL(LP_RSLSET,"TX_VATVL","N"),"N")
				+setINSSTR("TX_VATFL",getRSTVAL(LP_RSLSET,"TX_VATFL","C"),"C")
				+setINSSTR("TX_SCHVL",getRSTVAL(LP_RSLSET,"TX_SCHVL","N"),"N")
				+setINSSTR("TX_SCHFL",getRSTVAL(LP_RSLSET,"TX_SCHFL","C"),"C")
				+setINSSTR("TX_CVDVL",getRSTVAL(LP_RSLSET,"TX_CVDVL","N"),"N")
				+setINSSTR("TX_CVDFL",getRSTVAL(LP_RSLSET,"TX_CVDFL","C"),"C")
				+setINSSTR("TX_WCTVL",getRSTVAL(LP_RSLSET,"TX_WCTVL","N"),"N")
				+setINSSTR("TX_WCTFL",getRSTVAL(LP_RSLSET,"TX_WCTFL","C"),"C")
				+setINSSTR("TX_OTHVL",getRSTVAL(LP_RSLSET,"TX_OTHVL","N"),"N")
				+setINSSTR("TX_OTHDS",getRSTVAL(LP_RSLSET,"TX_OTHDS","C"),"C")
				+setINSSTR("TX_STSFL",getRSTVAL(LP_RSLSET,"TX_STSFL","C"),"C")
				+setINSSTR("TX_TRNFL",getRSTVAL(LP_RSLSET,"TX_TRNFL","C"),"C")
				+setINSSTR("TX_LUSBY",getRSTVAL(LP_RSLSET,"TX_LUSBY","C"),"C")
				+setINSSTR("TX_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+setINSSTR("TX_OTHFL",getRSTVAL(LP_RSLSET,"TX_OTHFL","C"),"C")
				+setINSSTR("TX_RSTVL",getRSTVAL(LP_RSLSET,"TX_RSTVL","N"),"N")
				+setINSSTR("TX_RSTFL",getRSTVAL(LP_RSLSET,"TX_RSTFL","C"),"C")
				+setINSSTR("TX_AMDNO",getRSTVAL(LP_RSLSET,"TX_AMDNO","C"),"C")
				+setINSSTR("TX_EDCVL",getRSTVAL(LP_RSLSET,"TX_EDCVL","N"),"N")
				+setINSSTR("TX_EHCFL",getRSTVAL(LP_RSLSET,"TX_EDCFL","N"),"C")
				+setINSSTR("TX_EHCVL",getRSTVAL(LP_RSLSET,"TX_EHCVL","N"),"N")
				+"'"+getRSTVAL(LP_RSLSET,"TX_EHCFL","C")+"')";
			}
			else if(LM_flgCHK_EXIST)
			{
				M_strSQLQRY="update CO_TXDOC set "
				+setUPDSTR("TX_SBSCD",getRSTVAL(LP_RSLSET,"TX_SBSCD","C"),"C")
				+setUPDSTR("TX_TRNTP",getRSTVAL(LP_RSLSET,"TX_TRNTP","C"),"C")
				+setUPDSTR("TX_DSBVL",getRSTVAL(LP_RSLSET,"TX_DSBVL","N"),"N")
				+setUPDSTR("TX_DSBFL",getRSTVAL(LP_RSLSET,"TX_DSBFL","C"),"C")
				+setUPDSTR("TX_EXCVL",getRSTVAL(LP_RSLSET,"TX_EXCVL","N"),"N")
				+setUPDSTR("TX_EXCFL",getRSTVAL(LP_RSLSET,"TX_EXCFL","C"),"C")
				+setUPDSTR("TX_PNFVL",getRSTVAL(LP_RSLSET,"TX_PNFVL","N"),"N")
				+setUPDSTR("TX_PNFFL",getRSTVAL(LP_RSLSET,"TX_PNFFL","C"),"C")
				+setUPDSTR("TX_CSTVL",getRSTVAL(LP_RSLSET,"TX_CSTVL","N"),"N")
				+setUPDSTR("TX_CSTFL",getRSTVAL(LP_RSLSET,"TX_CSTFL","C"),"C")
				+setUPDSTR("TX_STXVL",getRSTVAL(LP_RSLSET,"TX_STXVL","N"),"N")
				+setUPDSTR("TX_STXDS",getRSTVAL(LP_RSLSET,"TX_STXDS","C"),"C")
				+setUPDSTR("TX_STXFL",getRSTVAL(LP_RSLSET,"TX_STXFL","C"),"C")
				+setUPDSTR("TX_OCTVL",getRSTVAL(LP_RSLSET,"TX_OCTVL","N"),"N")
				+setUPDSTR("TX_OCTFL",getRSTVAL(LP_RSLSET,"TX_OCTFL","C"),"C")
				+setUPDSTR("TX_FRTVL",getRSTVAL(LP_RSLSET,"TX_FRTVL","N"),"N")
				+setUPDSTR("TX_FRTFL",getRSTVAL(LP_RSLSET,"TX_FRTFL","C"),"C")
				+setUPDSTR("TX_INSVL",getRSTVAL(LP_RSLSET,"TX_INSVL","N"),"N")
				+setUPDSTR("TX_INSFL",getRSTVAL(LP_RSLSET,"TX_INSFL","C"),"C")
				+setUPDSTR("TX_CDSVL",getRSTVAL(LP_RSLSET,"TX_CDSVL","N"),"N")
				+setUPDSTR("TX_CDSFL",getRSTVAL(LP_RSLSET,"TX_CDSFL","C"),"C")
				+setUPDSTR("TX_INCVL",getRSTVAL(LP_RSLSET,"TX_INCVL","N"),"N")
				+setUPDSTR("TX_INCFL",getRSTVAL(LP_RSLSET,"TX_INCFL","C"),"C")
				+setUPDSTR("TX_ENCVL",getRSTVAL(LP_RSLSET,"TX_ENCVL","N"),"N")
				+setUPDSTR("TX_ENCFL",getRSTVAL(LP_RSLSET,"TX_ENCFL","C"),"C")
				+setUPDSTR("TX_FNIVL",getRSTVAL(LP_RSLSET,"TX_FNIVL","N"),"N")
				+setUPDSTR("TX_FNIFL",getRSTVAL(LP_RSLSET,"TX_FNIFL","C"),"C")
				+setUPDSTR("TX_CDUVL",getRSTVAL(LP_RSLSET,"TX_CDUVL","N"),"N")
				+setUPDSTR("TX_CDUFL",getRSTVAL(LP_RSLSET,"TX_CDUFL","C"),"C")
				+setUPDSTR("TX_CLRVL",getRSTVAL(LP_RSLSET,"TX_CLRVL","N"),"N")
				+setUPDSTR("TX_CLRFL",getRSTVAL(LP_RSLSET,"TX_CLRFL","C"),"C")
				+setUPDSTR("TX_VATVL",getRSTVAL(LP_RSLSET,"TX_VATVL","N"),"N")
				+setUPDSTR("TX_VATFL",getRSTVAL(LP_RSLSET,"TX_VATFL","C"),"C")
				+setUPDSTR("TX_SCHVL",getRSTVAL(LP_RSLSET,"TX_SCHVL","N"),"N")
				+setUPDSTR("TX_SCHFL",getRSTVAL(LP_RSLSET,"TX_SCHFL","C"),"C")
				+setUPDSTR("TX_CVDVL",getRSTVAL(LP_RSLSET,"TX_CVDVL","N"),"N")
				+setUPDSTR("TX_CVDFL",getRSTVAL(LP_RSLSET,"TX_CVDFL","C"),"C")
				+setUPDSTR("TX_WCTVL",getRSTVAL(LP_RSLSET,"TX_WCTVL","N"),"N")
				+setUPDSTR("TX_WCTFL",getRSTVAL(LP_RSLSET,"TX_WCTFL","C"),"C")
				+setUPDSTR("TX_OTHVL",getRSTVAL(LP_RSLSET,"TX_OTHVL","N"),"N")
				+setUPDSTR("TX_OTHDS",getRSTVAL(LP_RSLSET,"TX_OTHDS","C"),"C")
				+setUPDSTR("TX_STSFL",getRSTVAL(LP_RSLSET,"TX_STSFL","C"),"C")
				+setUPDSTR("TX_TRNFL",getRSTVAL(LP_RSLSET,"TX_TRNFL","C"),"C")
				+setUPDSTR("TX_LUSBY",getRSTVAL(LP_RSLSET,"TX_LUSBY","C"),"C")
				+setUPDSTR("TX_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+setUPDSTR("TX_OTHFL",getRSTVAL(LP_RSLSET,"TX_OTHFL","C"),"C")
				+setUPDSTR("TX_RSTVL",getRSTVAL(LP_RSLSET,"TX_RSTVL","N"),"N")
				+setUPDSTR("TX_RSTFL",getRSTVAL(LP_RSLSET,"TX_RSTFL","C"),"C")
				+setUPDSTR("TX_AMDNO",getRSTVAL(LP_RSLSET,"TX_AMDNO","C"),"C")
				+setUPDSTR("TX_EDCVL",getRSTVAL(LP_RSLSET,"TX_EDCVL","N"),"N")
				+setUPDSTR("TX_EDCFL",getRSTVAL(LP_RSLSET,"TX_EDCFL","N"),"C")
				+setUPDSTR("TX_EHCVL",getRSTVAL(LP_RSLSET,"TX_EHCVL","N"),"N")
				+" TX_EHCFL = '"+getRSTVAL(LP_RSLSET,"TX_EHCFL","C")+"'"
				+" where "+ strWHRSTR;
			}
			System.out.println(" saveTXDOC = "+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"saveTXDOC");
		}
	}
	
	
	/**
	 * Function which validate the loading if there is any Loading against  LA's Grade then it return false 
	 * And after that user can not modify or delete that records 
	*/
	private boolean chkDELIVT1(String P_strMKTTP,String P_strLADNO,String P_strPRDCD,String P_strPKGTP)
	{
		String L_strMSGSTR, L_strPRDDS, L_strISSQT, L_strSTSFL;
		double L_dblISSQT;
		boolean L_flgRETFL = true;
		try
		{
			L_strMSGSTR = "";
          //LM_STRSQL = "select ist_issqt,ist_stsfl from FG_ISTRN where ist_isstp = '10' and ist_mkttp = ? and ist_issno = ? and ist_prdcd = ? and ist_pkgtp = ? ";
//			PRE_chkDELIVT1 = cl_dat.M_conSPDBA_pbst.prepareStatement("select ist_issqt,ist_stsfl from FG_ISTRN where ist_cmpcd = ? and ist_isstp = '10' and ist_mkttp = ? and ist_issno = ? and ist_prdcd = ? and ist_pkgtp = ? ");
			PRE_chkDELIVT1.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_chkDELIVT1.setString(2,P_strMKTTP);
			PRE_chkDELIVT1.setString(3,P_strLADNO);
			PRE_chkDELIVT1.setString(4,P_strPRDCD);
			PRE_chkDELIVT1.setString(5,P_strPKGTP);
			rstRSSET = PRE_chkDELIVT1.executeQuery();
			String LM_LADQT;
			if (!rstRSSET.next() || rstRSSET == null)
			{
				return true;
			}
			while (true)
			{
				L_strMSGSTR = "";
                L_strISSQT = getRSTVAL(rstRSSET,"IST_ISSQT","C");
                L_strSTSFL = getRSTVAL(rstRSSET,"IST_STSFL","C");
				L_strPRDDS = getVTRVL(vtrPRDDS,P_strPRDCD,P_strPKGTP);
               // L_strPRDDS = getPRDDS(P_strPRDCD);
                L_dblISSQT = Double.parseDouble(L_strISSQT);
                if (L_dblISSQT>0)
                {
					if(L_strSTSFL.equals("1"))
						L_strMSGSTR = L_strISSQT + " MT is already Allocated against "+L_strPRDDS;
                    if(L_strSTSFL.equals("2"))
                        L_strMSGSTR = L_strISSQT + " MT is already Loaded against "+L_strPRDDS;
                }

                if(!L_strMSGSTR.equals(""))
					break;
                if(!rstRSSET.next() || rstRSSET == null)
					break;
           }
           if(rstRSSET!=null)
				rstRSSET.close();
           if(!L_strMSGSTR.equals(""))
           {
                JOptionPane.showMessageDialog(this,L_strMSGSTR,"Error Message",JOptionPane.INFORMATION_MESSAGE);
                L_flgRETFL = false;
           }
		}
		catch (Exception L_EX)
		{
             setMSG(L_EX,"chkDELIVT1");
		}
		return L_flgRETFL;
	}
	/**
	 * Function for deletion remark 
	 */
	private boolean exeDELREM(String P_strMKTTP, String P_strTRNTP, String P_strLADNO) 
	{
		try
        {
			//if(chkMSTDEL(LP_MKTTP, LP_DOCNO)) 
			//{
//			PRE_chkMSTDEL   = cl_dat.M_conSPDBA_pbst.prepareStatement("select count(*) from MR_IVTRN where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? and IVT_STSFL <> 'X'");
			PRE_chkMSTDEL.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_chkMSTDEL.setString(2,P_strMKTTP);
            PRE_chkMSTDEL.setString(3,P_strLADNO);
            int L_intRECNT = getPRPCNT(PRE_chkMSTDEL);
            if(L_intRECNT>0)
            {
                return false;
            }
			else
			{
			//PRE_exeDELREM = cl_dat.M_conSPDBA_pbst.prepareStatement("update MR_RMMST set RM_STSFL = 'X', RM_LUSBY = ? , RM_LUPDT = ?  where RM_CMPCD = ? and RM_MKTTP = ?  and RM_TRNTP = ?  and RM_DOCNO = ? ");
				PRE_exeDELREM.setString(1,strLUSBY);
                PRE_exeDELREM.setDate(2,setSQLDAT1(strLUPDT));
                PRE_exeDELREM.setString(3,cl_dat.M_strCMPCD_pbst);
				PRE_exeDELREM.setString(4,P_strMKTTP);
                PRE_exeDELREM.setString(5,P_strTRNTP);
                PRE_exeDELREM.setString(6,P_strLADNO);
                PRE_exeDELREM.executeUpdate();
				//cl_dat.ocl_dat.exeDBCMT("SP","ACT","CMT");
				//cl_dat.M_conSPDBA_pbst.commit();
			}
			
        }
		catch (Exception L_EX)
        {
			setMSG(L_EX,"exeDELREM");
        }
		return true;
	}
	
	
	/** Generating string for Insertion Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	*/
	private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 
	{
		try 
		{
			//System.out.println("setINSSTR : "+LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
			if (LP_FLDTP.equals("C"))
				return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
	 		else if (LP_FLDTP.equals("N"))
				return   nvlSTRVL(LP_FLDVL,"0") + ",";
	 		else if (LP_FLDTP.equals("D"))
				return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
			else if (LP_FLDTP.equals("T"))
				//return   (LP_FLDVL.length()>10) ? ("'"+cc_dattm.occ_dattm.setDTMFMT(LP_FLDVL)+"',") : "null,";
				return   (LP_FLDVL.length()>10) ? ("'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_FLDVL))+"',") : "null,";
				
			else return " ";
		}
		catch (Exception L_EX) 
		{
			setMSG("Error in setINSSTR : "+L_EX,'E');
		}
		return " ";
	}
	

	/** Generating string for Updation Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 * */
	private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 
	{
		try 
		{
			//System.out.println("setUPDSTR : "+LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
			if (LP_FLDTP.equals("C"))
				return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
	 		else if (LP_FLDTP.equals("N"))
				return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
	 		else if (LP_FLDTP.equals("D"))
				return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
			else if (LP_FLDTP.equals("T"))
				return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_FLDVL))+"',") : "null,"));
				//return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+cc_dattm.occ_dattm.setDTMFMT(LP_FLDVL)+"',") : "null,"));
			else return " ";
		}
		catch (Exception L_EX) 
		{
			setMSG("Error in setUPDSTR : "+L_EX,'E');
		}
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
			System.out.println( "chkEXIST = "+M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				L_flgCHKFL = true;
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)	
		{
			setMSG("Error in chkEXIST : "+L_EX,'E');
		}
		return L_flgCHKFL;
	}
		
	private class vldINVER extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				java.sql.Date L_datTMPDT;
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtDORNO)
				{
					strLADTM = cl_dat.M_txtCLKDT_pbst.getText()+""+cl_dat.M_txtCLKTM_pbst.getText();
					//System.out.println(" dddddddd "+strLADTM);
				}
				if(input==txtLADNO)
				{
					
				}			
				if(input==txtGINNO)
				{
					if(txtGINNO.getText().trim().length()<8)
						return true;
					else
					{
						M_strSQLQRY = "Select distinct WB_DOCNO,WB_GINDT,WB_LRYNO from MM_WBTRN where WB_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and WB_DOCTP = '03' and WB_DOCNO = '"+txtGINNO.getText().trim()+"'";
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET!=null && M_rstRSSET.next())
						{
							txtLRYNO.setText(nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"  "));
							M_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Gate In Number ",'E');
							return false;
						}
					}
				}
			}
			catch(Exception E)
			{
				setMSG(E,"Inputverifier ");
			}
			return true;
		}
	}
	/**
	 * Boolean function for fetching L.A. details
	 */
	private boolean getLADDTL0(String P_strMKTTP, String P_strLADNO) 
	{
		try 
		{
			java.sql.Timestamp L_datTMPDT1;
			setMSG("Picking up L.Advice details",'N');
			//M_strSQLQRY = "Select IVT_LADDT, IVT_DORNO, IVT_INDNO, IVT_AUTBY,";
			//M_strSQLQRY += "IVT_LRYNO, IVT_FRMNO, IVT_FRMTP, IVT_TRPCD, IVT_CNTDS, IVT_TSLNO, IVT_UNLSQ, IVT_SLRFL, IVT_GINNO, IVT_LR_NO, IVT_LR_DT, IVT_ADLNO, IVT_ADLDT, IVT_STSFL from MR_IVTRN where IVT_MKTTP = '"+P_strMKTTP+"' and IVT_LADNO = '"+P_strLADNO+"'";
			//M_strSQLQRY ="Select IVT_LADDT, IVT_DORNO, IVT_INDNO, IVT_INVNO, IVT_AUTBY, IVT_LRYNO, IVT_TRPCD, IVT_CNTDS, IVT_TSLNO, IVT_UNLSQ, IVT_CSLNO, IVT_SLRFL, IVT_GINNO, IVT_LR_NO, IVT_LR_DT, IVT_ADLNO, IVT_ADLDT, IVT_STSFL from MR_IVTRN where IVT_MKTTP = '"+P_strMKTTP+"' and IVT_LADNO = '"+P_strLADNO+"'";
			
			//System.out.println("getLADDTL0 : "+M_strSQLQRY);
//			PRE_getLADDTL0 = cl_dat.M_conSPDBA_pbst.prepareStatement("Select IVT_LADDT, IVT_DORNO, IVT_DOANO, IVT_INDNO, IVT_INVNO, IVT_AUTBY, IVT_LRYNO, IVT_FRMNO, IVT_FRMTP, IVT_TRPCD, IVT_CNTDS, IVT_TSLNO, IVT_UNLSQ, IVT_CSLNO, IVT_SLRFL, IVT_GINNO, IVT_LR_NO, IVT_LR_DT, IVT_ADLNO, IVT_ADLDT, IVT_STSFL from MR_IVTRN where IVT_CMPCD = ? and IVT_MKTTP = ? and IVT_LADNO = ? ");

			PRE_getLADDTL0.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_getLADDTL0.setString(2,P_strMKTTP);
			PRE_getLADDTL0.setString(3,P_strLADNO);
			rstRSSET = PRE_getLADDTL0.executeQuery();
			if ((!rstRSSET.next()) || rstRSSET == null) 
			{
				setMSG("Not found in MR_IVTRN",'E');
				return false;
			}
	
			//strLADTM = getRSTVAL(rstRSSET,"IVT_LADDT","T");
			//strLADTM = rstRSSET.getString("IVT_LADDT").substring(0,16);
			L_datTMPDT1 = rstRSSET.getTimestamp("IVT_LADDT");
			strLADTM=M_fmtLCDTM.format(L_datTMPDT1);
			//System.out.println("in  getLADDTL0 "+strLADTM);
			strDORNO = getRSTVAL(rstRSSET,"IVT_DORNO","C");
			strDOANO = getRSTVAL(rstRSSET,"IVT_DOANO","C");
			strINDNO = getRSTVAL(rstRSSET,"IVT_INDNO","C");
			strINVNO = getRSTVAL(rstRSSET,"IVT_INVNO","C");
			strAUTBY = getRSTVAL(rstRSSET,"IVT_AUTBY","C");
			strTRPCD = getRSTVAL(rstRSSET,"IVT_TRPCD","C");
			strLRYNO = getRSTVAL(rstRSSET,"IVT_LRYNO","C");
			strFRMNO = getRSTVAL(rstRSSET,"IVT_FRMNO","C");
			strFRMTP = getRSTVAL(rstRSSET,"IVT_FRMTP","C");
			strCNTDS = getRSTVAL(rstRSSET,"IVT_CNTDS","C");
			strTSLNO = getRSTVAL(rstRSSET,"IVT_TSLNO","C");
			strLDGSQ = getRSTVAL(rstRSSET,"IVT_UNLSQ","N");
			strCSLNO = getRSTVAL(rstRSSET,"IVT_CSLNO","C");
			strSLRFL = getRSTVAL(rstRSSET,"IVT_SLRFL","C");
			strGINNO = getRSTVAL(rstRSSET,"IVT_GINNO","C");
			strLR_NO = getRSTVAL(rstRSSET,"IVT_LR_NO","C");
			strLR_DT = getRSTVAL(rstRSSET,"IVT_LR_DT","D");
			//System.out.println("strLR_DT "+ strLR_DT);
			strADLNO = getRSTVAL(rstRSSET,"IVT_ADLNO","C");
			strADLDT = getRSTVAL(rstRSSET,"IVT_ADLDT","D");
			strSTSCH = getRSTVAL(rstRSSET,"IVT_STSFL","C");
			//strLADDT = getSUBSTR(strLADTM,0,10);
			System.out.println("strSTSCH = "+strSTSCH);
			if(rstRSSET!=null)
				rstRSSET.close();
			//System.out.println("strLADDT = "+strLADDT);
			flgPOSTINVFL = false;
			if(strSTSCH.equals("D") || strINVNO.length()==8)
				flgPOSTINVFL = true;
			if (!exeSTSCH(strSTSCH))
			{
				//System.out.println("strSTSCH = "+strSTSCH);
				return false;
			}
			//if (exeALOCH()) {
			//        return false;
			//}
		} // try
		catch (Exception L_EX) 
		{
			setMSG(L_EX,"getLADDTL0");							   
		}
		// catch (SQLException L_EX) {
		return true;
	} // private boolean getLADDTL0() {

	
	private boolean setLADDTL0()
	{
		try
		{
			String L_strMATCT="";
			//System.out.println("strTSLNO 6 : "+strTSLNO);
			txtLADDT.setText(strLADTM);
			txtDORNO.setText(strDORNO);
			txtDOANO.setText(strDOANO);
			//txtINDNO.setText(strINDNO);
			
			txtAUTBY.setText(strAUTBY);
			txtTRPCD.setText(strTRPCD);
			txtLRYNO.setText(strLRYNO);
			txtCNTNO.setText(strCNTDS);
			txtSELNO.setText(strTSLNO);
			txtEXSNO.setText(strCSLNO);
			txtFRMNO.setText(strFRMNO);
			txtFRMTP.setText(strFRMTP);
			txtLDGSQ.setText(strLDGSQ);
			//txtSLRFL.setText(strSLRFL);
			//txtMATCT.setText(strSLRFL);
			if(strSLRFL.equals("1"))
				L_strMATCT=strSLRFL+" Fresh";
			else
				L_strMATCT=strSLRFL+" Sales Return";
			
			txtMATCT.setText(L_strMATCT);
			
			txtGINNO.setText(strGINNO);
			txtLRNO.setText(strLR_NO);
			txtLRDT.setText(strLR_DT);
			txtLICNO.setText(strADLNO);
			txtLICDT.setText(strADLDT);
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setLADDTL0");                                                           
		}
		return true;
	}

	/**
	 * 
	*/
	private boolean exeDELIVT()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
            {
				JOptionPane.showMessageDialog(this," Invalid option","Error Message",JOptionPane.INFORMATION_MESSAGE);                  
				return false;
            }
            if (!chkDELIVT(strMKTTP,strLADNO))
				return false;
			
			//System.out.println("IN exeDELIVT");
			
            PRE_exeDELIVT.setString(1,strLUSBY);
            PRE_exeDELIVT.setDate(2,setSQLDAT1(strLUPDT));
            PRE_exeDELIVT.setString(3,cl_dat.M_strCMPCD_pbst);
			PRE_exeDELIVT.setString(4,strMKTTP);
            PRE_exeDELIVT.setString(5,strLADNO);
            PRE_exeDELIVT.setString(6,strPRDCD);
            PRE_exeDELIVT.setString(7,strPKGTP);
            PRE_exeDELIVT.executeUpdate();
            //cl_dat.ocl_dat.exeDBCMT("SP","ACT","CMT");
//            PRE_exeDELIVT1 = cl_dat.M_conSPDBA_pbst.prepareStatement("update MM_WBTRN set WB_STSFL = '0'  where WB_CMPCD = ? and WB_DOCTP = '03' and WB_DOCNO = ? and WB_ORDRF = ?  and WB_STSFL <> '9'");
			PRE_exeDELIVT1.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_exeDELIVT1.setString(2,strGINNO);
            PRE_exeDELIVT1.setString(3,strLADNO);
            PRE_exeDELIVT1.executeUpdate();
            //cl_dat.ocl_dat.exeDBCMT("SP","ACT","CMT");
			//cl_dat.M_conSPDBA_pbst.commit();
			//System.out.println("IN  Last exeDELIVT");
        }
        catch (Exception L_EX)
        {
			setMSG(L_EX,"exeDELIVT");
        }
		return true;
	}

	
	/**
	 */
	private boolean chkDELIVT(String P_strMKTTP,String P_strLADNO)
	{
		String L_strMSSTR, L_strPRDCD, L_strPRDDS, L_strISSQT, L_strSTSFL;
		double L_dblISSQT;
		boolean L_flgRETFL = true;
		try
		{
			L_strMSSTR = "";
			//LM_STRSQL = "select ist_prdcd,ist_issqt,ist_stsfl from FG_ISTRN where IST_ISSTP='10' and IST_MKTTP = ? and ist_issno = ?  ";
//			PRE_chkDELIVT  = cl_dat.M_conSPDBA_pbst.prepareStatement("select ist_prdcd,ist_issqt,ist_stsfl from FG_ISTRN where IST_CMPCD = ? and IST_ISSTP='10' and IST_MKTTP = ? and ist_issno = ?  ");
			PRE_chkDELIVT.setString(1,cl_dat.M_strCMPCD_pbst);
			PRE_chkDELIVT.setString(2,P_strMKTTP);
			PRE_chkDELIVT.setString(3,P_strLADNO);
			rstRSSET = PRE_chkDELIVT.executeQuery();
			String LM_LADQT;
			if (!rstRSSET.next() || rstRSSET == null)
			{
				return true;
			}
	        while (true)
			{
			    L_strPRDCD = getRSTVAL(rstRSSET,"IST_PRDCD","C");
                L_strISSQT = getRSTVAL(rstRSSET,"IST_ISSQT","C");
                L_strSTSFL = getRSTVAL(rstRSSET,"IST_STSFL","C");
                //L_strPRDCD = getPRDDS(L_strPRDCD);
                L_dblISSQT = Double.parseDouble(L_strISSQT);
                if (L_dblISSQT>0)
                {
					if(L_strSTSFL.equals("1"))
						L_strMSSTR = L_strISSQT + " MT is already Allocated against "+L_strPRDCD;
                    if(L_strSTSFL.equals("2"))
                        L_strMSSTR = L_strISSQT + " MT is already Loaded against "+L_strPRDCD;
                }
				if(!L_strMSSTR.equals(""))
					break;
                if(!rstRSSET.next() || rstRSSET == null)
					break;
			}
			if(rstRSSET!=null)
				rstRSSET.close();
			if(!L_strMSSTR.equals(""))
			{
				JOptionPane.showMessageDialog(this,L_strMSSTR,"Error Message",JOptionPane.INFORMATION_MESSAGE);
                L_flgRETFL = false;
            }
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"chkDELIVT");
		}
		return L_flgRETFL;
	}
	/**
	* Function for checking status of L.A. details 
	*/
	private boolean exeSTSCH(String LP_STSCH)
	{
		String L_strCHMSG = "";
		try
		{
			//if(cl_dat.ocl_dat.M_FLGOPT == 'M' || cl_dat.ocl_dat.M_FLGOPT == 'D')
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				if (LP_STSCH.equals("L"))
				{
					L_strCHMSG = "Already loaded";
				}
				else if (LP_STSCH.equals("D"))
				{
					L_strCHMSG = "Already despatched";
				}
				if (L_strCHMSG.length()>0)
				{
					setMSG(L_strCHMSG,'E');
				  //JOptionPane.showMessageDialog(this,L_strCHMSG,"Transaction Status",JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"exeSTSCH");                                                           
		} 
		return true;
	}

	
	/**
	 * Displays loading status for current day 
	 *
	 */
	private void dspDSPST()
	{
		try
		{
			//DISPLAY WINDOW FOR SHOWING CURRENT LOADING STATUS
			if(chkDSPST.isSelected()==false)
				return;
			//LM_ROWCNT = 200;
			java.sql.Timestamp L_datTMPDT1;
			
			if(pnlDSPST==null)
			{
				pnlDSPST=new JPanel(null);
				String[] L_staCOLHD = {"S.N","Lorry No.","Prep.At","Aloc.At","Load At","Status","Sec.No.","LA No.","Grade","P.Type","Qty","DO No.","Transporter"};
				int[] L_inaCOLSZ = {20,100,40,40,40,100,80,80,90,50,60,80,80,90};
				tblDSPST = crtTBLPNL1(pnlDSPST,L_staCOLHD,500,2,1,7,7,L_inaCOLSZ,new int[]{0});
			}
			String L_strLADTM="";
			String L_strALOTM="";
			String L_strLODTM="";
			String L_strSTSDS = "";
			String L_strSTSFL ="";
			tblDSPST.clrTABLE();
			//M_strSQLQRY = "select ivt_lryno, ivt_ginno,ivt_ladno,ivt_prdds,ivt_pkgtp,ivt_reqqt,ivt_ladqt,ivt_laddt,ivt_alodt,ivt_loddt,ivt_stsfl,ivt_dorno,ivt_trpcd,pt_prtnm from mr_ivtrn,co_ptmst where pt_prttp='T' and pt_prtcd=ivt_trpcd and ivt_mkttp in ('01','03','04','05') and (date(ivt_laddt) = "+cc_dattm.occ_dattm.setDBSDT(cl_dat.M_strLOGDT_pbst)+" or date(ivt_alodt) = "+cc_dattm.occ_dattm.setDBSDT(cl_dat.M_strLOGDT_pbst)+") order by ivt_lryno,ivt_ladno,ivt_prdds";
			M_strSQLQRY = "select ivt_lryno, ivt_ginno,ivt_ladno,ivt_prdds,ivt_pkgtp,ivt_reqqt,ivt_ladqt,ivt_laddt,ivt_alodt,ivt_loddt,ivt_stsfl,ivt_dorno,ivt_doano,ivt_trpcd,pt_prtnm from mr_ivtrn,co_ptmst where pt_prttp='T' and pt_prtcd=ivt_trpcd and ivt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_mkttp in ('01','03','04','05') and (CONVERT(varchar,ivt_laddt,103) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' or CONVERT(varchar,ivt_alodt,103) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"') order by ivt_lryno,ivt_ladno,ivt_prdds";
			System.out.println("dspDSPST = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i =0;
			if(M_rstRSSET.next())
			{
				while (true)
				{
					L_datTMPDT1 = M_rstRSSET.getTimestamp("IVT_LADDT");
					System.out.println(L_strALOTM);
					if(L_datTMPDT1!=null)
						L_strLADTM=M_fmtLCDTM.format(L_datTMPDT1);
					System.out.println("01 : "+L_strLADTM);
					
					L_datTMPDT1 = M_rstRSSET.getTimestamp("IVT_ALODT");
					if(L_datTMPDT1!=null)
						L_strALOTM=M_fmtLCDTM.format(L_datTMPDT1);
					System.out.println("02 : "+L_strALOTM);
					
					L_datTMPDT1 = M_rstRSSET.getTimestamp("IVT_LODDT");
					if(L_datTMPDT1!=null)
						L_strLODTM=M_fmtLCDTM.format(L_datTMPDT1);
					System.out.println("03 : "+L_strLODTM);
					
					if(L_strLADTM.length()>10)
						L_strLADTM = L_strLADTM.substring(11,16);
					if(L_strALOTM.length()>10)
						L_strALOTM = L_strALOTM.substring(11,16);
					if(L_strLODTM.length()>10)
						L_strLODTM = L_strLODTM.substring(11,16);
					L_strSTSFL = getRSTVAL(M_rstRSSET,"IVT_STSFL","C");
				
					if(L_strSTSFL.equalsIgnoreCase("A") && L_strALOTM.length()<2)
						L_strSTSDS = "LA prepared";
					else if(L_strSTSFL.equalsIgnoreCase("A") && L_strALOTM.length()>2)
						L_strSTSDS = "Allocated";
					else if(L_strSTSFL.equalsIgnoreCase("2"))
						L_strSTSDS = "Loaded";
					if(L_strSTSFL.equalsIgnoreCase("L"))
						L_strSTSDS = "Authorised for Invoice";
					if(L_strSTSFL.equalsIgnoreCase("D"))
						L_strSTSDS = "Despatched";
					if(L_strSTSFL.equalsIgnoreCase("X"))
						L_strSTSDS = "Cancelled";
				
					System.out.println("04 upper table");
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"IVT_LRYNO","C"),i,TB3_LRYNO);
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"IVT_GINNO","C"),i,TB3_GINNO);
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"IVT_LADNO","C"),i,TB3_LADNO);
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"IVT_PRDDS","C"),i,TB3_PRDDS);
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"IVT_PKGTP","C"),i,TB3_PKGTP);
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"IVT_REQQT","N"),i,TB3_REQQT);
					//LM_DSTTBL.setValueAt(getRSTVAL(M_rstRSSET,"IVT_LADQT","N"),i,TB4_LADQT);
					tblDSPST.setValueAt(L_strLADTM,i,TB3_LADTM);
					tblDSPST.setValueAt(L_strALOTM,i,TB3_ALOTM);
					tblDSPST.setValueAt(L_strLODTM,i,TB3_LODTM);
					tblDSPST.setValueAt(L_strSTSDS,i,TB3_STSDS);
					System.out.println("05 upper table");
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"IVT_DORNO","C"),i,TB3_DORNO);
					//System.out.println("upper table2");
					tblDSPST.setValueAt(getRSTVAL(M_rstRSSET,"PT_PRTNM","C"),i,TB3_TRPNM);
					System.out.println(" 06 upper table3");
					i++;
					if(!M_rstRSSET.next())
						break;
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			
			pnlDSPST.setSize(100,100);
			pnlDSPST.setPreferredSize(new Dimension(700,250));
			//System.out.println("Today's loading status");
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDSPST,"Today's loading status",JOptionPane.OK_CANCEL_OPTION);
			//int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDSPST,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
		}

		catch (Exception L_EX)
		{
			setMSG("Error in dspDSPST : "+L_EX,'E');
		}
	}

	/**
	 * Capturing Loading Sequence And display on Screen
	 *
	*/
	private void getLDGSQ(String P_strLADNO)
	{
		try
		{		
			//DISPLAY WINDOW FOR SHOWING CURRENT LOADING STATUS
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(chkLDGSQ.isSelected()==false)
				return;
			if(P_strLADNO.length()!=8)
				return;
			if(pnlLDGSQ==null)
			{
				pnlLDGSQ=new JPanel(null);
				String[] L_staCOLHD = {"S.N","Sec.No.","Lorry No.","D.O.No.","Customer","L.A.No.","Destn.","Ldg.Seq"};
				int[] L_inaCOLSZ = {20,70,100,70,130,70,130,60};
				tblLODSQ = crtTBLPNL1(pnlLDGSQ,L_staCOLHD,100,2,1,7,7,L_inaCOLSZ,new int[]{0});
			}
			//M_strSQLQRY = "select distinct ivt_ginno,ivt_lryno,ivt_dorno,pt_prtnm ivt_byrnm,ivt_ladno,cmt_codds,IVT_UNLSQ from mr_ivtrn,mr_inmst,co_ptmst,co_cdtrn where ivt_ginno in (select isnull(ivt_ginno,'') from mr_ivtrn where ivt_ladno = '"+P_strLADNO+"') and in_mkttp=ivt_mkttp and in_indno=ivt_indno and cmt_cgmtp = 'SYS' and cmt_cgstp='COXXDST' and cmt_codcd=IN_DSTCD and pt_prttp='C' and ivt_byrcd=pt_prtcd and upper(ivt_stsfl) <> 'X' order by ivt_unlsq";
			M_strSQLQRY = "select distinct a.ivt_ginno,a.ivt_lryno,a.ivt_dorno,pt_prtnm ivt_byrnm,a.ivt_ladno,cmt_codds,a.IVT_UNLSQ from spldata.mr_ivtrn  a,spldata.mr_inmst,spldata.co_ptmst,spldata.co_cdtrn where ivt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_ginno in (select b.ivt_ginno from spldata.mr_ivtrn b where b.ivt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and b.ivt_ladno = '"+P_strLADNO+"') and in_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and in_mkttp=ivt_mkttp and in_indno=a.ivt_indno and cmt_cgmtp = 'SYS' and cmt_cgstp='COXXDST' and cmt_codcd=IN_DSTCD and pt_prttp='C' and a.ivt_byrcd=pt_prtcd and upper(a.ivt_stsfl) <> 'X' order by a.ivt_unlsq";


			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
				return;
			int i =0;
			tblLODSQ.clrTABLE();
			while (true)
			{
				tblLODSQ.setValueAt(getRSTVAL(M_rstRSSET,"IVT_GINNO","C"),i,TB2_GINNO);
				tblLODSQ.setValueAt(getRSTVAL(M_rstRSSET,"IVT_LRYNO","C"),i,TB2_LRYNO);
				tblLODSQ.setValueAt(getRSTVAL(M_rstRSSET,"IVT_DORNO","C"),i,TB2_DORNO);
				tblLODSQ.setValueAt(getRSTVAL(M_rstRSSET,"IVT_BYRNM","C"),i,TB2_BYRNM);
				tblLODSQ.setValueAt(getRSTVAL(M_rstRSSET,"IVT_LADNO","C"),i,TB2_LADNO);
				tblLODSQ.setValueAt(getRSTVAL(M_rstRSSET,"CMT_CODDS","C"),i,TB2_DSTDS);
				tblLODSQ.setValueAt(getRSTVAL(M_rstRSSET,"IVT_UNLSQ","N"),i,TB2_LDGSQ);
				i++;
				if(!M_rstRSSET.next())
					break;
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			pnlLDGSQ.setSize(100,100);
			pnlLDGSQ.setPreferredSize(new Dimension(700,250));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlLDGSQ,"Loading Sequence",JOptionPane.OK_CANCEL_OPTION);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch (Exception L_EX)
		{
			setMSG("Error in getLDGSQ : "+L_EX,'E');
		}
	}

	public void showDODEL(int P_intROWID)		
	{
		try
		{
			//System.out.println(" P_strPRDCD = "+P_strPRDCD);
			//System.out.println(" P_strPKGTP = "+P_strPKGTP);
			String L_strPRDCD="";
			String L_strPKGTP="";
			String L_strPRDDS="";
			String L_strLADQT="";
			boolean L_flgCheck=true;
			//System.out.println("P_intROWID = "+P_intROWID);
			
			
			String L_strSRLNO="";
			L_strPRDCD=tblLODDTL.getValueAt(P_intROWID,TB1_PRDCD).toString().trim();
			L_strPKGTP=tblLODDTL.getValueAt(P_intROWID,TB1_PKGTP).toString().trim();
			L_strPRDDS=tblLODDTL.getValueAt(P_intROWID,TB1_PRDDS).toString().trim();
			L_strLADQT=tblLODDTL.getValueAt(P_intROWID,TB1_REQQT).toString().trim();
			//System.out.println("L_strLADQT = "+L_strLADQT);
			
			//System.out.println(" L_strPRDCD = "+L_strPRDCD);
			//System.out.println(" L_strPKGTP = "+L_strPKGTP);
			if(pnlDODEL==null)
			{
				pnlDODEL=new JPanel(null);
				lblPRDDS=new JLabel("");
				lblPKGTP=new JLabel("");
				lblLADQT=new JLabel("");
				add(lblPRDDS,1,1,1,2,pnlDODEL,'L');
				add(lblPKGTP,2,1,1,2,pnlDODEL,'L');
				add(lblLADQT,1,4,1,2,pnlDODEL,'L');
				String[] L_staCOLHD = {"S.N","D.O. No.","D.O. Qty","dsp Date","L.A. No","Desp Sequance","Del Type"};
				int[] L_inaCOLSZ = {40,70,70,70,70,60,60};
				tblDODEL = crtTBLPNL1(pnlDODEL,L_staCOLHD,100,3,1,5,5,L_inaCOLSZ,new int[]{0});
			}
			lblPRDDS.setText("");
			lblPKGTP.setText("");
			lblLADQT.setText("");
			
			lblPRDDS.setText("Prod Dsp : "+L_strPRDDS);
			lblPKGTP.setText("Pkg Type : "+L_strPKGTP);
			lblLADQT.setText("L.A. Qty : "+L_strLADQT);
			String P_strLADNO=txtLADNO.getText().trim();
			//System.out.println(" P_strLADNO = "+P_strLADNO);
			M_strSQLQRY = "select distinct DOD_DORNO,isnull(DOD_DORQT,0)-isnull(DOD_LADQT,0) DOD_DORQT,DOD_DSPDT,DOD_LADNO,DOD_SRLNO,DOD_DELTP from MR_DODEL where DOD_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOD_MKTTP='"+strMKTTP+"' AND DOD_DORNO='"+txtDORNO.getText().trim()+"' AND DOD_PRDCD='"+L_strPRDCD+"' AND DOD_PKGTP='"+L_strPKGTP+"' and (isnull(DOD_DORQT,0))>(isnull(DOD_LADQT,0))  and isnull(dod_dorqt,0)>0 and dod_stsfl <> 'X' order by DOD_DSPDT";
			System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
			{
				System.out.println("IN NULL Result Set");
				if(!(strMKTTP).equals((txtDORNO.getText().trim()).substring(1,3)))
					JOptionPane.showMessageDialog(this,"Please check Market type And DO Number","Message",JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(this,"Mkt Type/Do.No ("+strMKTTP+" / "+txtDORNO.getText().trim()+") Not Found in DODEL","Message",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			int i =0;
			tblDODEL.clrTABLE();
			while (true)
			{
				tblDODEL.setValueAt(getRSTVAL(M_rstRSSET,"DOD_DORNO","C"),i,TB4_DORNO);
				tblDODEL.setValueAt(getRSTVAL(M_rstRSSET,"DOD_DORQT","C"),i,TB4_DORQT);
				tblDODEL.setValueAt(getRSTVAL(M_rstRSSET,"DOD_DSPDT","D"),i,TB4_DSPDT);
				tblDODEL.setValueAt(getRSTVAL(M_rstRSSET,"DOD_LADNO","C"),i,TB4_LADNO);				
				tblDODEL.setValueAt(getRSTVAL(M_rstRSSET,"DOD_SRLNO","C"),i,TB4_SRLNO);
			  //System.out.println(" iiiiiii"+(getRSTVAL(M_rstRSSET,"DOD_DELTP","C")));
				tblDODEL.setValueAt(getRSTVAL(M_rstRSSET,"DOD_DELTP","C"),i,TB4_DELTP);
				i++;
				if(!M_rstRSSET.next())
					break;
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			pnlDODEL.setSize(70,70);
			pnlDODEL.setPreferredSize(new Dimension(500,200));
			i=0;
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDODEL,"dispatch Details",JOptionPane.OK_CANCEL_OPTION);
			//System.out.println("L_intOPTN = "+L_intOPTN);
			if(L_intOPTN == 0)
			{
				//System.out.println("JJJJJJJJJJ");
				String L_strLADDT="";
				String strDSPDT="";
				String L_strDLTTP="";
			//	int L_intOPTN1=1;
				cl_dat.M_flgLCUPD_pbst = true;
				for(int j=0;j<tblDODEL.getRowCount();j++)
				{
					//System.out.println("JJJJJJJJJJ FOR.......");
					L_strSRLNO=tblDODEL.getValueAt(j,TB4_SRLNO).toString().trim();
					L_strDLTTP=tblDODEL.getValueAt(j,TB4_DELTP).toString().trim();
					if(L_strSRLNO.equals(""))
						break;
					if(tblDODEL.getValueAt(j,TB4_CHKFL).toString().equals("true"))
    				{
						//System.out.println("JJJJJJJJJJ FOR.......IF ");
						strDSPDT=tblDODEL.getValueAt(j,TB4_DSPDT).toString().trim();
						L_strLADDT=(txtLADDT.getText().trim()).substring(0,10);
						if(M_fmtLCDAT.parse(strDSPDT).compareTo(M_fmtLCDAT.parse(L_strLADDT))>0)
						{
							if(L_strDLTTP.equals("S"))
							   	L_flgCheck=false;
							else
								L_flgCheck=true;
						}
						L_strSRLNO=tblDODEL.getValueAt(j,TB4_SRLNO).toString().trim();
						
						M_strSQLQRY = "UPDATE MR_DODEL set DOD_LADNO = 'XXXXXXXX' where DOD_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOD_MKTTP='"+strMKTTP+"' AND DOD_DORNO='"+txtDORNO.getText().trim()+"' AND DOD_PRDCD='"+L_strPRDCD+"' AND DOD_PKGTP='"+L_strPKGTP+"' AND DOD_SRLNO = '"+L_strSRLNO+"'  and isnull(dod_dorqt,0)>0 and dod_stsfl <> 'X'";
						System.out.println(M_strSQLQRY);
							//cl_dat.exeSQLUPD(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"");
						if(cl_dat.exeDBCMT("exeSAVE"))
						{
							System.out.println("Update MR_DODEL ");
							setMSG("Update Successfully",'N');
						}
						i++;
					}
				}
			}
			System.out.println("L_flgCheck = "+L_flgCheck);
			//System.out.println("L_intOPTN i = "+i);
			if(L_intOPTN !=0 ||i==0)
			{
				 JOptionPane.showMessageDialog(this," Data will not be saved , Please check against D.O. schedule ","Message",JOptionPane.INFORMATION_MESSAGE);
			}
			if(!L_flgCheck ) 
			{
				JOptionPane.showMessageDialog(this," Loading Advice Date is before Dispatch Date","Message",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch (Exception L_EX)
		{
			setMSG("Error in showDODEL : "+L_EX,'E');
		}
	}
	/**
	 *  Table Input Verifier Class for Validation
	*/
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				String L_strDOCNO="";
				java.sql.Date L_datTMPDT;
				
				if(getSource()==tblLODDTL)
				{
					if(P_intCOLID>0)
						if(P_intCOLID!=TB1_CHKFL)
							if(((JTextField)tblLODDTL.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
								return true;
					
					if(P_intCOLID==TB1_REQQT)
					{ 
						strREQQT = tblLODDTL.getValueAt(P_intROWID,TB1_REQQT).toString().trim();
						strSTKQT = tblLODDTL.getValueAt(P_intROWID,TB1_STKQT).toString().trim();
						double L_dblREQQT = Double.parseDouble(strREQQT);
						double L_dblSTKQT = Double.parseDouble(strSTKQT);
						//System.out.println("L_dblREQQT= "+L_dblREQQT);
						//System.out.println("L_dblSTLQT= "+L_dblSTKQT);
						if(L_dblREQQT>L_dblSTKQT)
						{
							setMSG("Requested L.A. Quantity is not available in Stocks ",'E');
							JOptionPane.showMessageDialog(this.getSource()," Stock Not Available","Warning",JOptionPane.INFORMATION_MESSAGE);
							//return false;
						}
						
						dblREQQT = Double.parseDouble(strREQQT);
						strBALQT = tblLODDTL.getValueAt(tblLODDTL.getSelectedRow(),TB1_BALQT).toString();
						dblBALQT = Double.parseDouble(strBALQT);

						strPKGWT = tblLODDTL.getValueAt(tblLODDTL.getSelectedRow(),TB1_PKGWT).toString();
						dblPKGWT = Double.parseDouble(strPKGWT);
						if (dblPKGWT == 0.000) 
						{
							dblPKGWT = 0.001;
						}
						
						double L_dblPKGNO = Double.parseDouble(nvlSTRVL(strREQQT,"0.00"))/dblPKGWT;
						L_dblPKGNO = Float.parseFloat(setNumberFormat(L_dblPKGNO,3));
						int L_intPKGNO = new Float(L_dblPKGNO).intValue();
						if(L_intPKGNO != L_dblPKGNO)
						{
							setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(dblPKGWT,3),'E'); 
							return false;
						}
						if (dblREQQT <= dblBALQT) 
						{
							showDODEL(P_intROWID);
							tblLODDTL.setValueAt(new Boolean(true),tblLODDTL.getSelectedRow(),TB1_CHKFL);
							strREQPK  = setNumberFormat((dblREQQT/dblPKGWT),0).toString();
							tblLODDTL.setValueAt(strREQPK,tblLODDTL.getSelectedRow(),TB1_REQPK);
							tblLODDTL.setRowSelectionInterval(tblLODDTL.getSelectedRow(),tblLODDTL.getSelectedRow()+1);
							tblLODDTL.setColumnSelectionInterval(TB1_REQQT,TB1_REQQT);
							setMSG("Valid Req.Qty ",'N');
						} 
						else 
						{
							setMSG("Req.Qty should not exceed "+setNumberFormat((dblBALQT),3).toString(),'E');
							return false;
						}						
					}					
				}
			}
			catch(Exception E)
			{
				setMSG(E,"TAbleINPut ");
			}
			return true;
		}
	}

	
	void clrCOMP()
	{
		String L_strLADDT = txtLADDT.getText();
		super.clrCOMP();
		txtLADDT.setText(L_strLADDT);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
		{
			inlTBLEDIT(tblLODDTL);
		}
	}
	
	
/** Restoring default Key Values after clearing components 
	 * on the entry screen
	 */
	private void clrCOMP_1()
	{
		try
		{
			String L_strDORNO = txtDORNO.getText();
			String L_strLADNO = txtLADNO.getText();
			clrCOMP();
			txtDORNO.setText(L_strDORNO);
			txtLADNO.setText(L_strLADNO);
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP_1");}
	}

	
/** Initializing table editing before poulating/capturing data
 */
private void inlTBLEDIT(JTable P_tblTBLNM)
{
	if(!P_tblTBLNM.isEditing())
		return;
	P_tblTBLNM.getCellEditor().stopCellEditing();
	P_tblTBLNM.setRowSelectionInterval(0,0);
	P_tblTBLNM.setColumnSelectionInterval(0,0);
			
}
	

}