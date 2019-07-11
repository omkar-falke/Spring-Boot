import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.sql.ResultSet;import java.sql.CallableStatement;import java.sql.PreparedStatement;import java.sql.SQLException;import java.sql.Date;import java.sql.Timestamp;
import java.util.StringTokenizer;import java.util.Vector;import java.util.Enumeration;import java.util.Hashtable;
import javax.swing.event.ChangeEvent;import javax.swing.event.ChangeListener;

class mm_tepor extends cl_pbase implements ChangeListener
{
	private JTabbedPane tbpMAIN;
	private CallableStatement cstAMDPR;			// amendment procedure
	private JPanel pnlCODTL,pnlITTAX,pnlCOTAX,pnlITDTL,pnlPORMK,pnlDLSCH;
	private JTextField txtPORNO,txtBYRCD,txtBYRNM,txtVENCD,txtVENNM;
	private JTextField txtORDDT,txtAMDNO,txtAMDDT,txtEFFDT,txtCMPDT,txtQTNNO,txtQTNDT,txtQTNTP;
	private JTextField txtSHRDS,txtREMDS,txtEXGRT,txtCURCD,txtCURDS,txtCRDDY;
    private JTextField txtMATCD,txtINDNO,txtORDQT,txtPOUOM,txtPMTRF,txtINDNT;
	private JTextArea txaPORMK;
	private JButton btnPRNT;
	private JComboBox cmbPORTP,cmbDELTP,cmbDSTCD,cmbPOSRL;
	private JRadioButton rdoONEDL,rdoMLTDL;
	private ButtonGroup bgrDLOPT;
	private cl_JTable tblITTAX,tblCOTAX,tblITDTL,tblDLSCH;
	private ResultSet rstRSSET; 
	private TableInputVerifier objTBLVRF;
	private Vector<String> vtrCURCD,vtrUOMCD,vtrTAXCD;
	private java.util.Vector<String> vtrTAXTP = new java.util.Vector<String>();
	private StringTokenizer L_STRTKN;
	private Vector<String> vtrDPTCD = new Vector<String>();
	private String strUSRNM ="",strSTXDS="",strOTHDS="",strGTXDS="",strGOTDS; // gradewise other tax description
    
	private final int TBL_CHKFL =0;
	private final int TBL_INDNO =1;
	private final int TBL_MATCD =2;
	private final int TBL_UOMCD =3;
	private final int TBL_POUOM =4;
	private final int TBL_MPKQT =5;
	private final int TBL_CNVFT =6;
	private final int TBL_ORDQT =7;
	private final int TBL_PORRT =8;
	private final int TBL_PERRT =9;
	private final int TBL_TCFFL =10;
	private final int TBL_INSFL =11;
	private final int TBL_MATDS =12;
	private final int TBL_DPTCD =13;
	private final int TBL_STSFL =14;
	private final int TB1_MATCD =1;
	
	private final int TB4_CHKFL =0;		// Delivery Schedule table
	private final int TB4_INDNO =1;
	private final int TB4_MATCD =2;
	private final int TB4_EDLDT =3;
	private final int TB4_EDLQT =4;
	
	private final String strRGPOR_fn ="01";	// Regular P.O
	private final String strRTCON_fn ="03"; // Rate Contract
	private final String strINDTR_fn ="10"; // Indent Transaction
	private final String strPRJTP_fn ="05"; // Project Orders
    private int intCTXRW =0,intGTXRW =0,intDLRWCT =0,intINDCT =0;
	private String strTRNTP ="PO";
	private String strSRLNO ="",strPRAMD ="",strPRTNM ="",strPORNO ="",strPORTP ="",strINDNO ="",strDPTCD ="";
	private java.sql.Date datTEMP;	
	private String strTEMP="";
	private boolean flgPORRM = false;
	private boolean flgPOANX = false;
	private boolean flgCOTAX = false;
	private boolean flgTXSPC = false;
	private boolean flgITTAX = false;
	private boolean flgAMDFL = false;
	private boolean flgEXCFL = false;
	private Hashtable<String,String> hstTXCAT,hstDLSCH,hstITTAX,hstPENQT,hstINDNO,hstACPQT,hstBILQT,hstTAXCD;//,hstTAXSP;
	private Hashtable<String,Double> hstDLQTY;
	private Vector<String> vtrINMAT; 
	private PreparedStatement pstmDODREC,pstmINSREC,pstmINSRMK,pstmUPDREC,pstmINSIND;
	private INPVF objINPVF = new INPVF();	
	private Vector<String> vtrGRPCD	;
	mm_tepor()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(btnPRNT = new JButton("Print"),1,8,1,1,this,'L');
			add(new JLabel("P.O. Type"),2,1,1,1,this,'L');
			add(cmbPORTP=new JComboBox(),2,2,1,2,this,'L');
			add(new JLabel("P.O Series/No."),2,4,1,1,this,'L');
			add(cmbPOSRL=new JComboBox(),2,5,1,1,this,'L');
			add(txtPORNO=new TxtLimit(8),2,6,1,1,this,'L');
			add(new JLabel("Order Date"),2,7,1,1,this,'L');
			add(txtORDDT=new TxtDate(),2,8,1,1,this,'L');
			add(new JLabel("Buyer"),3,1,1,1,this,'L');
			add(txtBYRCD=new TxtLimit(3),3,2,1,0.5,this,'L');
			add(txtBYRNM=new TxtLimit(45),3,3,1,1.5,this,'R');
			add(new JLabel("Supplier"),3,4,1,1,this,'L');
			add(txtVENCD=new TxtLimit(5),3,5,1,1,this,'L');
			add(txtVENNM=new TxtLimit(45),3,6,1,3,this,'L');
			
			add(new JLabel("Short Desc."),4,1,1,1,this,'L');
			add(txtSHRDS=new TxtLimit(70),4,2,1,7,this,'L');
            add(new JLabel("Indent No."),5,1,1,1,this,'L');
            add(txtINDNT=new TxtLimit(8),5,2,1,1,this,'L');
			setMatrix(20,6);
			
		//CREATING COMMON DETAILS AS FIRST TABBED PANE		
				
			pnlCODTL = new JPanel(null);
			pnlCOTAX = new JPanel(null);
			pnlITTAX = new JPanel(null);
			pnlITDTL = new JPanel(null);
			pnlPORMK = new JPanel(null);
			add(new JLabel("Effective Date"),1,1,1,1,pnlCODTL,'L');
			add(txtEFFDT=new TxtDate(),1,2,1,1,pnlCODTL,'L');
			add(new JLabel("Completion Date"),1,3,1,1,pnlCODTL,'L');
			add(txtCMPDT=new TxtDate(),1,4,1,1,pnlCODTL,'L');
			add(new JLabel("Amd. No./Date"),1,5,1,1,pnlCODTL,'L');
			add(txtAMDNO=new TxtLimit(2),1,6,1,0.3,pnlCODTL,'L');
			add(txtAMDDT=new TxtDate(),1,6,1,0.7,pnlCODTL,'R');
			
			add(new JLabel("Qtn. Type"),2,1,1,1,pnlCODTL,'L');
			add(txtQTNTP=new JTextField(),2,2,1,1,pnlCODTL,'L');
			add(new JLabel("Qtn. NO"),2,3,1,1,pnlCODTL,'L');
			add(txtQTNNO=new TxtLimit(15),2,4,1,1,pnlCODTL,'L');
			add(new JLabel("Qtn. Date"),2,5,1,1,pnlCODTL,'L');
			add(txtQTNDT=new TxtDate(),2,6,1,1,pnlCODTL,'L');
	
			add(new JLabel("Cur. Code "),3,1,1,1,pnlCODTL,'L');
			add(txtCURCD=new TxtLimit(2),3,2,1,1,pnlCODTL,'L');
			add(new JLabel("Description "),3,3,1,1,pnlCODTL,'L');
			add(txtCURDS=new JTextField(),3,4,1,1,pnlCODTL,'L');
			add(new JLabel("Exg.Rate "),3,5,1,1,pnlCODTL,'L');
			add(txtEXGRT=new TxtNumLimit(10.2),3,6,1,1,pnlCODTL,'R');
			
			add(new JLabel("Delivery Type"),4,1,1,1,pnlCODTL,'L');
			add(cmbDELTP=new JComboBox(),4,2,1,1,pnlCODTL,'L');
			add(new JLabel("Delivery At"),4,3,1,1,pnlCODTL,'L');
			add(cmbDSTCD=new JComboBox(),4,4,1,2,pnlCODTL,'L');
			
			add(new JLabel("Payment Type"),5,1,1,1,pnlCODTL,'L');
			add(txtPMTRF=new TxtLimit(30),5,2,1,2,pnlCODTL,'L');
			add(new JLabel("Credit Days"),5,4,1,1,pnlCODTL,'L');
			add(txtCRDDY=new TxtNumLimit(3.0),5,5,1,1,pnlCODTL,'L');
			
			add(new JLabel("Remarks"),6,1,1,1,pnlCODTL,'L');
			add(txtREMDS=new TxtLimit(200),6,2,1,5,pnlCODTL,'L');
				
	//CREATING COMMON TAX DETIALS PANEL AS SECOND TABBED PANE		
			tblCOTAX=crtTBLPNL1(pnlCOTAX=new JPanel(null),new String[]{"FL","Code","Description","Amt./Percent","Value","Proc.Seq."},100,1,1,8,6,new int[]{20,100,200,100,150,100},new int[]{0});
			tblCOTAX.setCellEditor(2,new TxtLimit(30));
            tblCOTAX.setCellEditor(3,new TxtLimit(1));
			tblCOTAX.setCellEditor(4,new TxtNumLimit(10.2)); // changes at H.O.
			tblCOTAX.setCellEditor(5,new TxtNumLimit(2.0));
	//CREATING GRADE DETAILS PANEL AS THIRD TABBED PANE					
			tblITDTL=crtTBLPNL1(pnlITDTL=new JPanel(null),new String[]{"FL","Indent No.","Item Code","UOM ","P.UOM","Min.Pkg.qty.","Conv.fac.","P.O.Qty","P.O.Rate","Rate Per","T.C Flag","Ins. Flag","Mat Desc","D","S"},400,1,1,9,5.9,new int[]{20,70,80,60,75,55,50,75,75,60,35,35,100,5,2},new int[]{0,10,11});
			tblITDTL.setCellEditor(13,new TxtLimit(3));
			tblITDTL.setCellEditor(12,new TxtLimit(45));
	//CREATING GRADE TAX DETAILS PANEL AS FOURTH TABBED PANE		
			tblITTAX=crtTBLPNL1(pnlITTAX=new JPanel(null),new String[]{"FL","Item Code","tax","Description","Amt./Percent","Value","Proc.Seq."},400,1,1,8,6,new int[]{20,100,60,200,100,100,90},new int[]{0});
			
			tblITTAX.setCellEditor(5,new TxtNumLimit(10.2));
		
		//CREATING STATUS  DETAILS PANEL AS FIFTH TABBED PANE		
		    tblDLSCH=crtTBLPNL1(pnlDLSCH=new JPanel(null),new String[]{"FL","Indent No.","Item Code","Exp. Del. Date","Del.Qty."},400,1,1,8,4,new int[]{20,80,80,100,100},new int[]{0});
			add(rdoONEDL=new JRadioButton("Single Delivery"),1,5,1,1,pnlDLSCH,'L');
			add(rdoMLTDL=new JRadioButton("Multiple Delivery"),2,5,1,1,pnlDLSCH,'L');
			bgrDLOPT = new ButtonGroup();
			bgrDLOPT.add(rdoONEDL);
			bgrDLOPT.add(rdoMLTDL);
			txaPORMK=new JTextArea();
			txaPORMK.setFont(new Font("Courier New",Font.PLAIN,15));
		//	txaPORMK.setColumns(20);
			pnlPORMK.add(txaPORMK);
			int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			JScrollPane jspPORMK = new JScrollPane(txaPORMK,v1,h1);
			add(jspPORMK,1,1,7.5,6,pnlPORMK,'L');
			
			//ADDING ALL PANELS IN TABBED PANE			
			tbpMAIN=new JTabbedPane();
			tbpMAIN.add("Common Details",pnlCODTL);
			tbpMAIN.add("Item Details",pnlITDTL);
			tbpMAIN.add("Common Tax",pnlCOTAX);
			tbpMAIN.add("Itemwise Tax",pnlITTAX);
			tbpMAIN.add("Del.Schedule",pnlDLSCH);
			tbpMAIN.add("Annexure",pnlPORMK);
			tbpMAIN.addChangeListener(this);
			//ADDING TABBED PANE TO SCREEN			
			add(tbpMAIN,6,1,12,8,this,'L');
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_teporTBLINVFR();
			objINPVF = new INPVF();
			txtVENCD.setInputVerifier(objINPVF);
			tblITDTL.setInputVerifier(objTBLVRF);
			tblCOTAX.setInputVerifier(objTBLVRF);
			tblITTAX.setInputVerifier(objTBLVRF);
			tblDLSCH.setInputVerifier(objTBLVRF);
		    vtrCURCD = new Vector<String>();
			vtrTAXCD = new Vector<String>();
			vtrUOMCD = new Vector<String>();
			M_strSQLQRY = "SELECT CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,cmt_chp01 from CO_CDTRN where cmt_CGMTP + CMT_CGSTP IN('SYSMRXXDTP','MSTCOXXCUR','MSTCOXXUOM','SYSMMXXPOT') and isnull(CMT_STSFL,'') <>'X'";
			String L_strDATA ="";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cmbDELTP.addItem("Select");
			cmbPORTP.addItem("Select");
			cmbPOSRL.addItem("Select");
			if(M_rstRSSET != null)	
			{
				while(M_rstRSSET.next())
				{
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("MRXXDTP"))
					{
						L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),3) +M_rstRSSET.getString("CMT_CODDS");
				  		cmbDELTP.addItem(L_strDATA);
					}
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("MMXXPOT"))
					{
						L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),3) +M_rstRSSET.getString("CMT_CODDS");
				  		cmbPORTP.addItem(L_strDATA);
					}
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXUOM"))
					{
						vtrUOMCD.addElement(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),3) +M_rstRSSET.getString("CMT_CODDS"));
					}
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXUOM"))
					{
						vtrCURCD.addElement(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),3) +M_rstRSSET.getString("CMT_CODDS"));
					}
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='SYS' and cmt_cgstp ='COXXDST' AND CMT_CHP01 ='MM' and isnull(CMT_STSFL,'') <>'X'";
			L_strDATA ="";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cmbDSTCD.addItem("Select");
			if(M_rstRSSET != null)	
			{
				while(M_rstRSSET.next())
				{
					L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),4) +M_rstRSSET.getString("CMT_CODDS");
			  		cmbDSTCD.addItem(L_strDATA);
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "SELECT US_USRCD,US_USRNM from SA_USMST where us_usrcd  ='"+cl_dat.M_strUSRCD_pbst+"'"+" and isnull(US_STSFL,'') <>'X'";
			L_strDATA ="";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)	
			{
				if(M_rstRSSET.next())
				{
					strUSRNM = nvlSTRVL(M_rstRSSET.getString("US_USRNM"),"");
				}
				M_rstRSSET.close();
			}
		tblITDTL.setCellEditor(TBL_MATCD,txtMATCD = new TxtLimit(10));
		tblITDTL.setCellEditor(TBL_ORDQT,txtORDQT = new TxtNumLimit(10.3));
		tblITDTL.setCellEditor(TBL_INDNO,txtINDNO = new TxtLimit(8));
		tblITDTL.setCellEditor(TBL_POUOM,txtPOUOM = new TxtLimit(3));

		txtMATCD.addFocusListener(this);txtMATCD.addKeyListener(this);
		txtORDQT.addFocusListener(this);txtORDQT.addKeyListener(this);
		txtINDNO.addFocusListener(this);txtINDNO.addKeyListener(this);
		txtPOUOM.addFocusListener(this);txtPOUOM.addKeyListener(this);

		setENBL(false);
		pstmINSREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
		"INSERT INTO MM_POMST(PO_CMPCD,PO_MMSBS,PO_STRTP,PO_PORTP,PO_PORNO,PO_MATCD,PO_MATDS,PO_INDTP,"+
			"PO_INDNO,PO_PORDT,PO_AMDNO,PO_SHRDS,PO_BUYCD,PO_EFFDT,PO_CMPDT,PO_VENTP,PO_VENCD,PO_CURCD,"+
			"PO_EXGRT,PO_PORVL,PO_QTNTP,PO_QTNNO,PO_QTNDT,PO_PREBY,PO_PREDT,PO_DELTP,PO_DSTCD,PO_UOMCD,PO_UCNVL,"+
			"PO_PORRT,PO_PERRT,PO_PORQT,PO_DELCT,PO_ITVAL,PO_TCFFL,PO_INSFL,PO_DPTCD,PO_PMTRF,PO_CRDDY,PO_TRNFL,PO_STSFL,PO_LUSBY,PO_LUPDT)VALUES("+
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"	
		);
		pstmUPDREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
		"UPDATE MM_POMST set PO_SHRDS = ?,PO_EFFDT = ?,PO_CMPDT= ?,PO_VENCD=?,PO_CURCD =?,"+
			"PO_EXGRT = ?,PO_QTNTP =?,PO_QTNNO =?,PO_QTNDT=?,PO_DELTP=?,PO_DSTCD=?,PO_UOMCD =?,PO_UCNVL=?,"+
			"PO_PORRT=?,PO_PERRT=?,PO_PORQT=?,PO_TCFFL=?,PO_INSFL=?,PO_DPTCD=?,PO_PMTRF=?,PO_CRDDY=?,PO_TRNFL=?,PO_LUSBY=?,PO_LUPDT=?,PO_MATDS =?,PO_AMDNO =?,PO_STSFL =? "+
			" where PO_CMPCD = ? AND PO_MMSBS = ? and PO_STRTP = ? and PO_PORNO =? and po_indno = ? and PO_MATCD = ?"	
		);
		pstmDODREC = cl_dat.M_conSPDBA_pbst.prepareStatement("INSERT INTO MM_PODEL(POD_CMPCD,POD_MMSBS,POD_STRTP,POD_PORTP,POD_PORNO,POD_MATCD,POD_INDNO,"+
			"POD_EDLDT,POD_EDLQT,POD_AMDNO,POD_TRNFL,POD_STSFL,POD_LUSBY,POD_LUPDT)VALUES("+
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?)"	
		);
		pstmINSRMK =cl_dat.M_conSPDBA_pbst.prepareStatement(
		"INSERT INTO MM_RMMST(RM_CMPCD,RM_MMSBS,RM_STRTP,RM_DOCTP,RM_TRNTP,RM_REMTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES("+
		"?,?,?,?,?,?,?,?,?,?,?,?)");
		cstAMDPR = cl_dat.M_conSPDBA_pbst.prepareCall("{call mm_adprc(?,?,?,?,?)}");			

		pstmINSIND = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"INSERT INTO MM_INMST(IN_CMPCD,IN_MMSBS,IN_STRTP,IN_INDNO,IN_AMDNO,IN_MATCD,IN_INDDT,"+
						"IN_INDQT,IN_AUTQT,IN_URGTG,IN_DPTCD,IN_PREBY,IN_PREDT,"+
					"IN_REQDT,IN_EXPDT,IN_PORBY,IN_INSFL,IN_TCFFL,IN_INDVL,IN_STSFL,IN_TRNFL,IN_LUSBY,IN_LUPDT,IN_FRWBY,IN_FRWTO,IN_FRWDT,IN_INDTP)VALUES("+
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"	
					);
		hstDLSCH = new Hashtable<String,String>();
		hstACPQT = new Hashtable<String,String>();
		hstBILQT = new Hashtable<String,String>();
		hstDLQTY = new Hashtable<String,Double>();
		hstITTAX = new Hashtable<String,String>();
		hstPENQT = new Hashtable<String,String>();
		hstINDNO = new Hashtable<String,String>();
		hstTAXCD = new Hashtable<String,String>();
		vtrINMAT = new Vector<String>();
		if(hstTXCAT==null) hstTXCAT=new Hashtable<String,String>(10,0.2f);
		crtHSTTXCAT();
		crtHSTXCD();
	  }
	  catch(Exception L_E)
	  {
		  setMSG(L_E,"constructor");
	  }
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtBYRNM.setEnabled(false);
		txtVENNM.setEnabled(false);
		txtCURDS.setEnabled(false);
		txtORDDT.setEnabled(false);
		txtAMDNO.setEnabled(false);
		txtAMDDT.setEnabled(false);
		btnPRNT.setEnabled(false);
		tblITDTL.cmpEDITR[3].setEnabled(false);
		tblITDTL.cmpEDITR[13].setEnabled(false);
		tblCOTAX.cmpEDITR[5].setEnabled(false);
		tblITTAX.cmpEDITR[6].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				cmbPORTP.setEnabled(false);
				cmbPOSRL.setEnabled(false);
				tblCOTAX.setEnabled(true);
				tblITTAX.setEnabled(true);
				tblITDTL.cmpEDITR[TBL_PORRT].setEnabled(true);
				tblITDTL.cmpEDITR[TBL_PERRT].setEnabled(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				tblITDTL.cmpEDITR[TBL_PORRT].setEnabled(true);
				tblITDTL.cmpEDITR[TBL_PERRT].setEnabled(true);
				tblCOTAX.setEnabled(true);
				tblITTAX.setEnabled(true);
			    txtINDNT.setEnabled(true);
			/*	if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
					txtPORNO.setEnabled(true);
				else
					txtPORNO.setEnabled(false);*/
				cmbPORTP.setEnabled(true);
				cmbPOSRL.setEnabled(true);
				txtBYRCD.setText(cl_dat.M_strUSRCD_pbst);
				txtBYRNM.setText(strUSRNM);
				txtORDDT.setText(cl_dat.M_strLOGDT_pbst);
				if(cmbPOSRL.getItemCount() >1)
					if(cmbPOSRL.getSelectedItem().toString().substring(3).equals("L"))
					{
						txtCURCD.setText("01");
						txtCURDS.setText("Rupees");
						txtEXGRT.setText("1.00");
						txtCURCD.setEnabled(false);
						txtEXGRT.setEnabled(false);
					}
					else
					{
						txtCURCD.setText("");
						txtCURDS.setText("");
						txtEXGRT.setText("");
						txtCURCD.setEnabled(true);
						txtEXGRT.setEnabled(true);
					}
				txtEFFDT.setText(cl_dat.M_strLOGDT_pbst);
			//	if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
			//		tblITDTL.cmpEDITR[TBL_INDNO].setEnabled(false);
			}
			else
			{
				txtPORNO.setEnabled(true);
			    txtINDNT.setEnabled(false);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				tblITDTL.cmpEDITR[0].setEnabled(true);	
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				btnPRNT.setEnabled(true);
		}
	}
	public void stateChanged(ChangeEvent L_CE)
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
			return;
		if(tblCOTAX.isEditing())
			tblCOTAX.getCellEditor().stopCellEditing();
	//	if(tblITDTL.isEditing())
	//		tblITDTL.getCellEditor().stopCellEditing();
		if(tblITTAX.isEditing())
			tblITTAX.getCellEditor().stopCellEditing();
		if(tblDLSCH.isEditing())
			tblDLSCH.getCellEditor().stopCellEditing();
		tblITDTL.setRowSelectionInterval(0,0);
		tblITDTL.setColumnSelectionInterval(0,0);
		tblDLSCH.setRowSelectionInterval(0,0);
		tblDLSCH.setColumnSelectionInterval(0,0);
		tblCOTAX.setRowSelectionInterval(0,0);
		tblCOTAX.setColumnSelectionInterval(0,0);
		tblITTAX.setRowSelectionInterval(0,0);
		tblITTAX.setColumnSelectionInterval(0,0);
		if(L_CE.getSource().equals("tblMAIN"))
		{
			if(tbpMAIN.getSelectedIndex() == 2)
			{
				if(flgAMDFL)
				{
					if(hstBILQT.size() >0)
					{
						tbpMAIN.setSelectedIndex(0);
						setMSG("Bill is passed, Tax modification is not allowed..",'E');
					}
				}
			}
		}
	}
	public void itemStateChanged(ItemEvent L_IE)
	{
		ResultSet L_rstCDTRN;
		super.itemStateChanged(L_IE);
		if(M_objSOURC == cmbPORTP)
		{
			try
			{
				if(M_strSBSCD==null)
					return;
				String L_strDATA ="";
				if(M_strSBSCD !=null)
				{
					cmbPOSRL.removeAllItems();
					cmbPOSRL.addItem("Select");
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_SHRDS,CMT_CHP01,CMT_NCSVL from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4) +"POR'";
					M_strSQLQRY += " AND CMT_CHP02 like '%"+cmbPORTP.getSelectedItem().toString().substring(0,2)+"%'";
					L_rstCDTRN = cl_dat.exeSQLQRY0(M_strSQLQRY);
					if(L_rstCDTRN != null)
					{
						while(L_rstCDTRN.next())
						{
							L_strDATA = L_rstCDTRN.getString("CMT_SHRDS");
							cmbPOSRL.addItem(L_strDATA);
							vtrDPTCD.addElement(L_rstCDTRN.getString("CMT_NCSVL"));
						}
						L_rstCDTRN.close();	
					}
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
		super.actionPerformed(L_AE);
		if(M_objSOURC == btnPRNT)
		{
			if(txtPORNO.getText().trim().length() ==8)
				strPORNO = txtPORNO.getText().trim();
			if(strPORNO.length() ==0)
			{
				setMSG("P.O. number is not specified..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				strPORTP = cmbPORTP.getSelectedItem().toString().substring(0,2);
			mm_rppor objPORRP  = new mm_rppor(M_strSBSCD);
			objPORRP.getALLREC(strPORNO,strPORNO,strPORTP);
			JComboBox L_cmbLOCAL = objPORRP.getPRNLS();
			objPORRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rppor.doc",L_cmbLOCAL.getSelectedIndex());
				
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
            intINDCT=0;
          	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				setENBL(false);
				txtPORNO.setEnabled(true);
			}
		}
        else if(M_objSOURC == txtINDNT)
		{
			if(tblITDTL.isEditing())
				tblITDTL.getCellEditor().stopCellEditing();
			tblITDTL.setColumnSelectionInterval(0,0);
			tblITDTL.setRowSelectionInterval(0,0);
            getINDDT(cmbPORTP.getSelectedItem().toString().substring(0,2));
        }
		else if(M_objSOURC == txtMATCD)
		{
			try
			{
				if(tblITDTL.isEditing())
					tblITDTL.getCellEditor().stopCellEditing();
				tblITDTL.setColumnSelectionInterval(0,0);
				tblITDTL.setRowSelectionInterval(0,0);
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD,CT_PKGQT from CO_CTMST WHERE "
							  +" isnull(CT_STSFL,'') not in ('X','9') AND CT_MATCD ='"+txtMATCD.getText().trim() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),tblITDTL.getSelectedRow(),TBL_UOMCD);
						tblITDTL.setValueAt(strDPTCD,tblITDTL.getSelectedRow(),TBL_DPTCD);
						strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),"");
						if(Double.parseDouble(strTEMP) ==0)
						     strTEMP ="1.000";
						tblITDTL.setValueAt(strTEMP,tblITDTL.getSelectedRow(),TBL_MPKQT);
						tblITDTL.setValueAt("1.00",tblITDTL.getSelectedRow(),TBL_PERRT);
						tblITDTL.setValueAt(tblITDTL.getValueAt(intINDCT,TBL_UOMCD),intINDCT,TBL_POUOM);
						tblITDTL.setValueAt("1.000",intINDCT,TBL_CNVFT);
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"txtMATCD.Action");
			}
            //getINDDT(cmbPORTP.getSelectedItem().toString().substring(0,2));
        }
		else if(M_objSOURC == txtCURCD)
		{
			// Currency Code Rupee
			txtEXGRT.setText("");
			if(txtCURCD.getText().trim().equals("01"))
				txtEXGRT.setText("1.00");
        }
		else if(M_objSOURC == txtPORNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				if(getPINQT())
					getDATA();
				setENBL(false);
				txtPORNO.setEnabled(true);
		
			}
			else
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					getDATA();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				cmbPORTP.setEnabled(false);
				cmbPOSRL.setEnabled(false);
			}
		}
		else if(M_objSOURC == cmbPORTP)
		{
			/*if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
			{
				tblITDTL.cmpEDITR[TBL_INDNO].setEnabled(false);
				txtPORNO.setEnabled(true);
			}
			else
			{
				tblITDTL.cmpEDITR[TBL_INDNO].setEnabled(true);
				txtPORNO.setEnabled(false);
			}*/
					
		}
		else if(M_objSOURC == cmbPOSRL)
		{
			//if(!cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
			txtPORNO.setEnabled(false);
			txtBYRCD.setText(cl_dat.M_strUSRCD_pbst);
			txtBYRNM.setText(strUSRNM);
			txtORDDT.setText(cl_dat.M_strLOGDT_pbst);
			if(cmbPOSRL.getItemCount() >1)
				if(cmbPOSRL.getSelectedItem().toString().substring(3).equals("L"))
				{
					txtCURCD.setText("01");
					txtCURDS.setText("Rupees");
					txtEXGRT.setText("1.00");
					txtCURCD.setEnabled(false);
					txtEXGRT.setEnabled(false);
				}
				else
				{
					txtCURCD.setText("");
					txtCURDS.setText("");
					txtEXGRT.setText("");
					txtCURCD.setEnabled(true);
					txtEXGRT.setEnabled(true);
				}
			txtEFFDT.setText(cl_dat.M_strLOGDT_pbst);
		}
		else if((M_objSOURC == rdoONEDL)||(M_objSOURC == rdoMLTDL))
		{
			tblDLSCH.clrTABLE();
			if(hstDLSCH !=null)
				hstDLSCH.clear();
			if(hstDLQTY !=null)
				hstDLQTY.clear();
			if((rdoONEDL.isSelected())||(rdoMLTDL.isSelected()))
			{
				if(txtCMPDT.getText().trim().length() ==0)
				{
					setMSG("Enter P.O. Completion Date first ..",'E');
					return;
				}
				int L_intDLRWCT =0;
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						if(rdoONEDL.isSelected())
						{
							tblDLSCH.setValueAt(Boolean.TRUE,L_intDLRWCT,TBL_CHKFL);
							tblDLSCH.setValueAt(tblITDTL.getValueAt(i,TBL_INDNO).toString(),L_intDLRWCT,TB4_INDNO);
							tblDLSCH.setValueAt(tblITDTL.getValueAt(i,TBL_MATCD).toString(),L_intDLRWCT,TB4_MATCD);
							tblDLSCH.setValueAt(txtCMPDT.getText().trim(),L_intDLRWCT,TB4_EDLDT);
							tblDLSCH.setValueAt(tblITDTL.getValueAt(i,TBL_ORDQT).toString(),L_intDLRWCT,TB4_EDLQT);
							L_intDLRWCT++;
						}
						strTEMP = tblITDTL.getValueAt(i,TBL_INDNO).toString()+tblITDTL.getValueAt(i,TBL_MATCD).toString();
						hstDLSCH.put(strTEMP,tblITDTL.getValueAt(i,TBL_ORDQT).toString());		
						
					}
				}
			}
			
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
				if(txtMATCD.getText().trim().length() ==0)
				{
					setMSG("Please enter the Group code(First two digits of item code)..",'N'); 
					return;
				}
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCDF2";
				if(cmbPORTP.getSelectedItem().toString().trim().substring(0,2).equals(strPRJTP_fn))
				{
					String L_ARRHDR[] = {"Item Code","Description","UOM","Min Pkg."};
					M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD,CT_PKGQT from CO_CTMST WHERE isnull(CT_STSFL,'') not in ('X','9')";
					M_strSQLQRY +=" AND CT_GRPCD  ='95' order by ct_matcd";
					cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,4,"CT");
				}
				else
				{
					String L_ARRHDR[] = {"Item Code","Description"};
					M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
					M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
					if(txtMATCD.getText().trim().length() >0)
						M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
					M_strSQLQRY += " Order by ct_matds ";
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLSZ);
				}
			}
			else if(M_objSOURC == txtVENCD)
			{
				if(txtVENCD.getText().trim().length() ==0)
				{
					setMSG("Please enter the First digits of Vendor Code..",'N'); 
					setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtVENCDF2";
				String L_ARRHDR[] = {"Supplier Code","Name"};
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
				M_strSQLQRY += " where PT_PRTCD ='S' and isnull(PT_STSFL,'') <>'X'";
				if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " and PT_PRTCD like '" + txtVENCD.getText().trim() + "%'";
				M_strSQLQRY += " Order by pt_prtnm ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC == txtMATCD)
			{
				if(txtMATCD.getText().trim().length() ==0)
				{
					setMSG("Please enter the Group code(First two digits of item code)..",'N'); 
					return;
				}
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				if(cmbPORTP.getSelectedItem().toString().trim().substring(0,2).equals(strPRJTP_fn))
				{
					String L_ARRHDR[] = {"Item Code","Description","UOM","Min Pkg."};
					M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD,CT_PKGQT from CO_CTMST WHERE isnull(CT_STSFL,'') not in ('X','9')";
					M_strSQLQRY +=" AND CT_GRPCD  ='95' order by ct_matcd";
					cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,4,"CT");
				}
				else
				{
					String L_ARRHDR[] = {"Item Code","Description"};
					M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
					M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
					if(txtMATCD.getText().trim().length() >0)
						M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
					M_strSQLQRY += " Order by ct_matcd ";
					cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT",L_inaCOLSZ);
				}
			}
			else if(M_objSOURC == txtINDNT)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtINDNT";
					String L_ARRHDR[] = {"Indent No.","Item Code","Description","Pending Qty."};
				M_strSQLQRY = "Select IN_INDNO,IN_MATCD,CT_MATDS,isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0) L_PQTY from MM_INMST,CO_CTMST WHERE IN_MATCD = CT_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') <>'X'";
				M_strSQLQRY +=" AND (isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)) >0";
				M_strSQLQRY +=" and IN_MMSBS ='"+M_strSBSCD+"' AND IN_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				if(txtINDNT.getText().trim().length() >0)
					M_strSQLQRY +=" and IN_INDNO like'"+txtINDNT.getText().trim()+"%'";
				if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strRGPOR_fn)) // Regular P.O.
					M_strSQLQRY +=" and IN_INDTP ='01'";
				else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strRTCON_fn)) // Rate Contract
					M_strSQLQRY +=" and IN_INDTP ='02'";
				M_strSQLQRY += " Order by in_indno,in_matcd ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
			}
			else if(M_objSOURC == txtPORNO)
			{
				/*if(txtPORNO.getText().trim().length() ==0)
				{
					setMSG("Please enter the Group code(First two digits of item code)..",'N'); 
					return;
				}*/
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPORNO";
				String L_ARRHDR[] = {"P.O No.","Short Description"};
				M_strSQLQRY = "Select distinct PO_PORNO,PO_SHRDS from MM_POMST ";
				M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"'" +" and isnull(PO_STSFL,'') <>'X'";
				if(txtPORNO.getText().trim().length() >0)
					M_strSQLQRY += " and PO_PORNO like '" + txtPORNO.getText().trim() + "%'";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					M_strSQLQRY += " and isnull(PO_STSFL,'') ='O'";
				M_strSQLQRY += " Order by po_porno desc ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtVENCD)
			{
				int[] L_inaCOLVN = new int[]{100,400};
				if(txtVENCD.getText().trim().length() ==0)
				{
					setMSG("Please enter the First digits of Vendor Code..",'N'); 
					return;
				}
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtVENCD";
				String L_ARRHDR[] = {"Supplier Code","Name"};
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
				M_strSQLQRY += " where PT_PRTTP ='S' and isnull(PT_STSFL,'') <>'X'";
				if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " and PT_PRTCD like '" + txtVENCD.getText().trim().toUpperCase() + "%'";
				M_strSQLQRY += " Order by pt_prtcd ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLVN);
			}
			else if(M_objSOURC == txtPMTRF)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPMTRF";
				String L_ARRHDR[] = {"Code","Payment Terms"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_cgmtp ='MST' and cmt_cgstp ='COXXPMT' ";
				M_strSQLQRY += " and isnull(CMT_STSFL,'') <>'X'";
				if(txtPMTRF.getText().trim().length() >0)
					M_strSQLQRY += " and CMT_CODDS like '" + txtPMTRF.getText().trim().toUpperCase() + "%'";
				M_strSQLQRY += " Order by cmt_codds ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
			}
			else if(M_objSOURC == txtINDNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtINDNO";
				String L_ARRHDR[] = {"Indent number","Item Code","Description","UOM","Pending Qty.","Dept","pkq","T.C FLag","Ins. Flag"};
				M_strSQLQRY = "Select IN_INDNO,IN_MATCD,CT_MATDS,CT_UOMCD,isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0) L_PQTY,IN_DPTCD,CT_PKGQT,IN_TCFFL,IN_INSFL from MM_INMST,CO_CTMST WHERE IN_MATCD = CT_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') <>'X'";
				M_strSQLQRY +=" AND IN_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				if(txtINDNO.getText().trim().length() >0)
						M_strSQLQRY += " and IN_INDNO like '" + txtINDNO.getText().trim() + "%'";
				M_strSQLQRY +=" AND (isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)) >0";
				if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strRGPOR_fn)) // Regular P.O.
					M_strSQLQRY +=" and IN_INDTP ='01'";
				else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strRTCON_fn)) // Rate Contract
					M_strSQLQRY +=" and IN_INDTP ='02'";
			
				M_strSQLQRY +=" Order by IN_URGTG,IN_INDNO DESC ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,9,"CT",new int[]{60,80,200,40,60,70,0,0,0});
			}
			else if(M_objSOURC == txtCURCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtCURCD";
				String L_ARRHDR[] = {"Currency Code","Description"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP ='MST' and CMT_CGSTP ='COXXCUR' and isnull(CMT_STSFL,'') <>'X'";
				if(txtCURCD.getText().trim().length() >0)
					M_strSQLQRY += " and CMT_CODCD like '" + txtCURCD.getText().trim() + "%'";
				M_strSQLQRY += " Order by cmt_codcd ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
			}
			else if(M_objSOURC == txtPOUOM)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPOUOM";
				String L_ARRHDR[] = {"UOM Code","Description"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP ='MST' and CMT_CGSTP ='COXXUOM' and isnull(CMT_STSFL,'') <>'X'";
				if(txtPOUOM.getText().trim().length() >0)
					M_strSQLQRY += " and CMT_CODCD like '" + txtPOUOM.getText().trim() + "%'";
				M_strSQLQRY += " Order by cmt_codcd ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
			}
			else if(M_objSOURC==tblCOTAX.cmpEDITR[1])
			{
				M_strHLPFLD = "tblCOTAX";
				cl_hlp("Select CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' order by CMT_CODCD" ,2,1,new String[] {"Code","Name"},6,"CT");
			}
			else if(M_objSOURC==tblITTAX.cmpEDITR[2])
			{
				M_strHLPFLD = "tblITTAX";
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' ";
				
				int i =0;
				boolean L_flgFOUND = false;
				for(i=0;i<tblCOTAX.getRowCount();i++)
				{
					//if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					if(tblCOTAX.getValueAt(i,1).toString().trim().length() >0)
					{
						L_flgFOUND = true;
						if( i >0)
							M_strSQLQRY +=	",'"+tblCOTAX.getValueAt(i,1).toString()	+"'";
						else
						{
							M_strSQLQRY += " AND CMT_CODCD not in('";
							M_strSQLQRY +=	tblCOTAX.getValueAt(i,1).toString()	+"'";
						}
					}
				}
				if(L_flgFOUND)
					M_strSQLQRY += ")"	;
				M_strSQLQRY += " order by CMT_CODCD";
				cl_hlp(M_strSQLQRY,2,1,new String[] {"Code","Name"},6,"CT");
			}
			else if(M_objSOURC == tblITTAX.cmpEDITR[1])
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "tblITTAX1";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				M_strSQLQRY += " AND CT_MATCD IN(";
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					//if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					if(tblITDTL.getValueAt(i,TBL_INDNO).toString().trim().length() >0)
					{
						if( i >0)
							M_strSQLQRY +=	",'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()	+"'";
						else
							M_strSQLQRY +=	"'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()	+"'";
					}
				}
				M_strSQLQRY += ") Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == tblDLSCH.cmpEDITR[1])
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "tblDLSCH";
				String L_ARRHDR[] = {"Indent No. ","Item Code"};
				M_strSQLQRY = "Select IN_INDNO,IN_MATCD,CT_MATDS from MM_INMST,CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				M_strSQLQRY += " AND IN_MATCD = CT_MATCD and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDNO IN (";
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					//if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					if(tblITDTL.getValueAt(i,TBL_INDNO).toString().trim().length() >0)
					{
						if( i >0)
							M_strSQLQRY +=	",'"+tblITDTL.getValueAt(i,TBL_INDNO).toString()	+"'";
						else
							M_strSQLQRY +=	"'"+tblITDTL.getValueAt(i,TBL_INDNO).toString()	+"'";
						strTEMP = tblITDTL.getValueAt(i,TBL_INDNO).toString();
					}
				}
				M_strSQLQRY += ") AND IN_MATCD IN(";
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					//if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					if(tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
					{
						if( i >0)
							M_strSQLQRY +=	",'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()	+"'";
						else
							M_strSQLQRY +=	"'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()	+"'";
						strTEMP += tblITDTL.getValueAt(i,TBL_MATCD).toString();
					//	if(hstDLSCH.contains(strTEMP) = null)
						hstDLSCH.put(strTEMP,tblITDTL.getValueAt(i,TBL_ORDQT).toString());
						strTEMP ="";
					}
				}
				M_strSQLQRY += ") Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT",L_inaCOLSZ);
			}
		setCursor(cl_dat.M_curDFSTS_pbst);
		}
		else if(L_KE.getKeyCode() == L_KE.VK_F9)
		{
			try
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtMATCD)
				{
					String L_strLVLNO ="";
					String L_strMATCD = txtMATCD.getText().trim();//tblITDTL.getValueAt(tblITDTL.getSelectedRow(),TBL_MATCD).toString();
					if(L_strMATCD.length() !=10)
					{
						setMSG("F9 help is allowed on Item code,please enter full code..",'E');
						setCursor(cl_dat.M_curDFSTS_pbst);
						return;
					}
					M_strSQLQRY = "SELECT CT_LVLRF from CO_CTMST where ct_codtp ='CD' and ct_matcd ='"+L_strMATCD +"'";
					M_strSQLQRY += " AND isnull(ct_stsfl,'') <>'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						if(M_rstRSSET.next())
							L_strLVLNO = nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),"");
						M_rstRSSET.close();
					}
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMATCDF9";
					String L_ARRHDR[] = {"Description"};
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
			if(M_objSOURC == cmbPORTP)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					cmbPOSRL.requestFocus();
					setMSG("Please select the type of series..",'N');
					cmbPOSRL.requestFocus();
				}
				else
				{
					txtPORNO.requestFocus();
					setMSG("Please enter the P.O.No.F1 to select from the list..",'N');
				}
			}
			else if(M_objSOURC == cmbPOSRL)
			{
				/*if(cmbPOSRL.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
				{
					txtPORNO.requestFocus();
					setMSG("Please enter the Project P.O. Number..",'N');
				}
				else
				{*/
					txtBYRCD.requestFocus();
					txtBYRCD.setText(cl_dat.M_strUSRCD_pbst);
					setMSG("Please enter the Buyer code..",'N');
				//}
			}
			else if(M_objSOURC == txtBYRCD)
			{
				txtVENCD.requestFocus();
				setMSG("Enter the Vendor code or Type First digit of code and press F1..",'N'); 
			}
			else if(M_objSOURC == txtVENCD)
			{
			
				txtVENCD.setText(txtVENCD.getText().trim().toUpperCase());
				if(!vldVENCD(txtVENCD.getText().trim()))
				   setMSG("Invalid Vendor",'E');
				else
				{
					txtVENNM.setText(strPRTNM);
					txtSHRDS.requestFocus();
					setMSG("Enter the P.O. Short Description..",'N'); 
				}
			}
			else if(M_objSOURC == txtSHRDS)
			{
				txtSHRDS.setText(txtSHRDS.getText().toUpperCase());
				txtEFFDT.requestFocus();
				setMSG("Enter the P.O. Effective date ..",'N'); 
			}
			else if(M_objSOURC == txtEFFDT)
			{
				txtCMPDT.requestFocus();
				setMSG("Enter the P.O. completion date ..",'N'); 
			}
			else if(M_objSOURC == txtCMPDT)
			{
				txtQTNTP.requestFocus();
				setMSG("Enter the Quotation Type ..",'N'); 
			}
			else if(M_objSOURC == txtQTNTP)
			{
				txtQTNNO.requestFocus();
				setMSG("Enter the Quotation No. ..",'N'); 
			}
			else if(M_objSOURC == txtQTNNO)
			{
				txtQTNDT.requestFocus();
				setMSG("Enter the Quotation date ..",'N'); 
			}
			else if(M_objSOURC == txtQTNDT)
			{
				txtQTNDT.transferFocus();
				setMSG("Enter the Currency Code..",'N'); 
			}
			else if(M_objSOURC == txtCURCD)
			{
				txtCURDS.setText(cl_dat.getPRMCOD("CMT_CODDS","MST","COXXCUR",txtCURCD.getText().trim()));
				txtEXGRT.requestFocus();
				setMSG("Enter the Currency Exchange Rate ..",'N'); 
			}
			else if(M_objSOURC == txtEXGRT)
			{
				cmbDELTP.requestFocus();
				setMSG("Select the Delivery type ..",'N'); 
			}
			else if(M_objSOURC == cmbDELTP)
			{
				cmbDSTCD.requestFocus();
				setMSG("Select the Destination ..",'N'); 
			}
			else if(M_objSOURC == cmbDSTCD)
			{
				txtPMTRF.requestFocus();
				setMSG("Enter the Payment Terms ..",'N'); 
			}
			else if(M_objSOURC == txtPMTRF)
			{
				txtPMTRF.setText(txtPMTRF.getText().trim().toUpperCase());
				txtCRDDY.requestFocus();
				setMSG("Enter the Credit days ..",'N'); 
			}
			else if(M_objSOURC == txtCRDDY)
			{
				txtREMDS.requestFocus();
				setMSG("Enter the P.O Remarks ..",'N'); 
			}
			else if(M_objSOURC == txtREMDS)
			{
				tbpMAIN.setSelectedIndex(1);
				setMSG("Enter the Item details ..",'N'); 
			}
		}
	}
	void exeHLPOK()
	{
		String L_strMATCD ="";
		int L_intROWID = tblITDTL.getSelectedRow();
		StringTokenizer L_STRTKN;
		String L_strPINQT ="0";
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtINDNT"))
			{
				txtINDNT.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtINDNO"))
			{
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				String L_ARRHDR[] = {"Indent number","Item Code","Description","UOM","Pending Qty.","Dept","pkq","T.C FLag","Ins. Flag"};
				strTEMP = L_STRTKN.nextToken();
				txtINDNO.setText(strTEMP);
				L_strMATCD = L_STRTKN.nextToken();
				tblITDTL.setValueAt(L_strMATCD,L_intROWID,TBL_MATCD);
				tblITDTL.setValueAt(L_STRTKN.nextToken(),L_intROWID,TBL_MATDS);
				tblITDTL.setValueAt(L_STRTKN.nextToken(),L_intROWID,TBL_UOMCD);
				L_strPINQT = L_STRTKN.nextToken(); // pending indent	qty.
				tblITDTL.setValueAt(L_strPINQT,L_intROWID,TBL_ORDQT);
				hstPENQT.put(strTEMP+L_strMATCD,L_strPINQT);
				tblITDTL.setValueAt("1.00",L_intROWID,TBL_PERRT);
				tblITDTL.setValueAt(tblITDTL.getValueAt(L_intROWID,TBL_UOMCD),L_intROWID,TBL_POUOM);
				tblITDTL.setValueAt("1.000",L_intROWID,TBL_CNVFT);
				tblITDTL.setValueAt(L_STRTKN.nextToken(),L_intROWID,TBL_DPTCD);	
				strTEMP ="0";
				strTEMP = L_STRTKN.nextToken();
				if(Double.parseDouble(strTEMP) ==0)
					strTEMP ="1.000";
				tblITDTL.setValueAt(strTEMP,L_intROWID,TBL_MPKQT);
				strTEMP ="";
				strTEMP = nvlSTRVL(L_STRTKN.nextToken(),"N");
				if(strTEMP.equals("Y"))
					tblITDTL.setValueAt(Boolean.TRUE,L_intROWID,TBL_TCFFL);
				else	
					tblITDTL.setValueAt(Boolean.FALSE,L_intROWID,TBL_TCFFL);
				strTEMP = nvlSTRVL(L_STRTKN.nextToken(),"N");
				if(strTEMP.equals("Y"))
					tblITDTL.setValueAt(Boolean.TRUE,L_intROWID,TBL_INSFL);
				else	
					tblITDTL.setValueAt(Boolean.FALSE,L_intROWID,TBL_INSFL);
				tblITDTL.cmpEDITR[TBL_POUOM].requestFocus();
			}
			else if((M_strHLPFLD.equals("txtVENCD"))||(M_strHLPFLD.equals("txtVENCDF2")))
			{
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtVENCD.setText(L_STRTKN.nextToken());
				txtVENNM.setText(L_STRTKN.nextToken());
				if(vldVENCD(txtVENCD.getText().trim()))
			    {
					txtSHRDS.requestFocus();
				}
			}
			else if(M_strHLPFLD.equals("txtPORNO"))
				txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtPMTRF"))
				txtPMTRF.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtPOUOM"))
				txtPOUOM.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtCURCD"))
			{
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtCURCD.setText(L_STRTKN.nextToken());
				txtCURDS.setText(L_STRTKN.nextToken());
				txtEXGRT.requestFocus();
			}
			else if((M_strHLPFLD.equals("txtMATCD"))||(M_strHLPFLD.equals("txtMATCDF2")))
			{
				/*if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
				{
					L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
					L_strMATCD = L_STRTKN.nextToken();
					tblITDTL.setValueAt(strINDNO,L_intROWID,TBL_INDNO);
					txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
					//tblITDTL.setValueAt(cl_dat.M_strHLPSTR_pbst,L_intROWID,TBL_MATCD);
					L_STRTKN.nextToken();
					tblITDTL.setValueAt(L_STRTKN.nextToken(),L_intROWID,TBL_UOMCD);
					tblITDTL.setValueAt("1.00",L_intROWID,TBL_PERRT);
					tblITDTL.setValueAt(tblITDTL.getValueAt(L_intROWID,TBL_UOMCD),L_intROWID,TBL_POUOM);
					tblITDTL.setValueAt("1.000",L_intROWID,TBL_CNVFT);
					tblITDTL.setValueAt(strDPTCD,L_intROWID,TBL_DPTCD);	
					strTEMP ="0";
					strTEMP = L_STRTKN.nextToken();
					if(Double.parseDouble(strTEMP) ==0)
						strTEMP ="1.000";
					tblITDTL.setValueAt(strTEMP,L_intROWID,TBL_MPKQT);
					tblITDTL.cmpEDITR[TBL_POUOM].requestFocus();
				}
				else
			   {*/
					L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
					L_STRTKN.nextToken();
					txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				//}
				
				if(tblITDTL.isEditing())
					tblITDTL.getCellEditor().stopCellEditing();
			}
			else if(M_strHLPFLD.equals("tblCOTAX"))
			{
				//CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_NCSVL,CMT_CHP02,CMT_CCSVL 3,4 
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				tblCOTAX.setValueAt(Boolean.TRUE,tblCOTAX.getSelectedRow(),0);
				String L_strTEMP=L_STRTKN.nextToken();
				if(hstTXCAT==null) hstTXCAT=new Hashtable<String,String>(10,0.2f);
				((JTextField)tblCOTAX.cmpEDITR[1]).setText(L_strTEMP);
			//	hstCOTAX.put(L_strTEMP,"");
				tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),2);
				hstTXCAT.put(L_strTEMP,L_STRTKN.nextToken());
				tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),4);
				strTEMP = L_STRTKN.nextToken().toUpperCase();
				if(strTEMP.equals("%"))
					tblCOTAX.setValueAt("P",tblCOTAX.getSelectedRow(),3);
				else
					tblCOTAX.setValueAt(strTEMP,tblCOTAX.getSelectedRow(),3);
				tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),5);
			}
			else if(M_strHLPFLD.equals("tblITTAX"))
			{
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				String L_strTEMP=L_STRTKN.nextToken();
				if(hstTXCAT==null) hstTXCAT=new Hashtable<String,String>(10,0.2f);
				((JTextField)tblITTAX.cmpEDITR[2]).setText(L_strTEMP);
				tblITTAX.setValueAt(Boolean.TRUE,tblITTAX.getSelectedRow(),0);
				tblITTAX.setValueAt(L_STRTKN.nextToken(),tblITTAX.getSelectedRow(),3);
				hstTXCAT.put(L_strTEMP,L_STRTKN.nextToken());
				tblITTAX.setValueAt(L_STRTKN.nextToken(),tblITTAX.getSelectedRow(),5);
				strTEMP = L_STRTKN.nextToken();
					tblITTAX.setValueAt(L_STRTKN.nextToken(),tblITTAX.getSelectedRow(),6);
				if(strTEMP.equals("%"))
					tblITTAX.setValueAt("P",tblITTAX.getSelectedRow(),4);
				else
					tblITTAX.setValueAt(strTEMP,tblITTAX.getSelectedRow(),4);
			}
			else if(M_strHLPFLD.equals("tblITTAX1"))
			{
				((JTextField)tblITTAX.cmpEDITR[1]).setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("tblDLSCH"))
			{
				String L_strTEMP ="";
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				tblDLSCH.setValueAt(Boolean.TRUE,tblDLSCH.getSelectedRow(),TB4_CHKFL);
				strTEMP=L_STRTKN.nextToken();
				L_strTEMP = strTEMP;
				((JTextField)tblDLSCH.cmpEDITR[1]).setText(strTEMP);
				strTEMP ="";
				strTEMP=L_STRTKN.nextToken();
				tblDLSCH.setValueAt(strTEMP,tblDLSCH.getSelectedRow(),TB4_MATCD);
				L_strTEMP +=strTEMP;
				tblDLSCH.setValueAt(txtCMPDT.getText().trim(),tblDLSCH.getSelectedRow(),TB4_EDLDT);
				tblDLSCH.setValueAt(hstDLSCH.get(L_strTEMP).toString(),tblDLSCH.getSelectedRow(),TB4_EDLQT);
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	boolean vldDATA()
	{
		try
		{
			int L_intITCNT =0,L_intDLCNT=0;
			double L_dblTPOQT=0.0,L_dblTDLQT =0.0;
			if(!vldVENCD(txtVENCD.getText().trim()))
			{
				return false;
			}
			if(cmbPORTP.getSelectedIndex() ==0)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					setMSG("Please select P.O Type..",'E');
					return false;
				}
			}
			else if(cmbPOSRL.getSelectedIndex() ==0)
			{
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					setMSG("Please select P.O Series..",'E');
					return false;
				}
			}
			else if(txtORDDT.getText().length() ==0)
			{
				setMSG("Please enter Order Date ..",'E');
				return false;
			}
			else if(txtBYRCD.getText().length() == 0)
			{
				setMSG("Please enter the buyer code..",'E');
				return false;
			}
			else if(txtVENCD.getText().length() ==0)
			{
				setMSG("Please enter Vendor code..",'E');
				return false;
			}
			else if(txtEFFDT.getText().length() ==0)
			{
				setMSG("Please enter P.O Effective date..",'E');
				return false;
			}
			else if(txtCMPDT.getText().length() ==0)
			{
				setMSG("Please enter Completion Date..",'E');
				return false;
			}
			else if(txtCURCD.getText().length() ==0)
			{
				setMSG("Please enter Currency code..",'E');
				return false;
			}
			else if(txtEXGRT.getText().length() ==0)
			{
				setMSG("Please enter Exg. Rate..",'E');
				return false;
			}
			else if(txtSHRDS.getText().length() ==0)
			{
				setMSG("P.O. Short Desc cannot be blank..",'E');
				return false;
			}
			else if(cmbDSTCD.getSelectedIndex() ==0)
			{
				setMSG("Please select Destination Code..",'E');
				return false;
			}
			// In case of Rupee Exg. rate  not required.(exg rate 1)
			else if(!txtCURCD.getText().trim().equals("01"))
			{
				if(Double.parseDouble(txtEXGRT.getText())<= 1)
				{
					setMSG("Please enter Exg. Rate..",'E');
					return false;
				}
			}
			else if(M_fmtLCDAT.parse(txtEFFDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtCMPDT.getText()))>0)
			{
				setMSG("P.O. Effective date can not be greater than P.O.Compl.date..",'E');
				return false;
			}
			
			else if((txtREMDS.getText().indexOf("'")>=0)||(txtREMDS.getText().indexOf("\"")>=0)||(txtREMDS.getText().indexOf("\\")>=0))
			{
				setMSG("Special characters LIKE \' and \" and \\ are not allowed in Remarks ",'E');
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(!cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
				if(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(txtCMPDT.getText()))>0)
				{
					setMSG("P.O. Completion date can not be less than today\'s date..",'E');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				String L_strINDNO ="",L_strMATCD ="",L_strTEMP ="";
				if(!getPINQT())
				{
					setMSG("P.O. quantity is greater than pending Indent qty,can not save..",'E');
					return false;
				}
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					L_strINDNO = tblITDTL.getValueAt(i,TBL_INDNO).toString();
					L_strMATCD = tblITDTL.getValueAt(i,TBL_MATCD).toString();
					if(L_strMATCD.length() >0)
					{
						if(hstPENQT !=null)
						{
							if(!hstPENQT.containsKey((String)L_strINDNO+L_strMATCD))
							{
								System.out.println("pending indent qty not found");
								setMSG("Check pending Indent qty for item at row "+(i+1),'E');
								return false;
							}
							L_strTEMP = hstPENQT.get(L_strINDNO+L_strMATCD).toString();
							if(Double.parseDouble(L_strTEMP) < Double.parseDouble(tblITDTL.getValueAt(i,TBL_ORDQT).toString()))
						    {
								setMSG("P.O.qty can not be greater than pending indent quantity ..",'E');
								return false;
							}
						}
					}
				}
			}
			/////
			strSTXDS ="";
			strOTHDS ="";
			String L_strTAXFL ="",L_strTAXVL ="";
			for(int i=0;i<tblCOTAX.getRowCount();i++)
			{
				if((tblCOTAX.getValueAt(i,0)== Boolean.TRUE)&&(tblCOTAX.getValueAt(i,1).toString().length() >0))
				{
					if(tblCOTAX.getValueAt(i,1).toString().equals("STX"))
						strSTXDS =tblCOTAX.getValueAt(i,2).toString();
					if(tblCOTAX.getValueAt(i,1).toString().equals("OTH"))
						strOTHDS =tblCOTAX.getValueAt(i,2).toString();
						System.out.println(strOTHDS);
					
					L_strTAXFL = tblCOTAX.getValueAt(i,3).toString();
					L_strTAXVL = tblCOTAX.getValueAt(i,4).toString();
					if((L_strTAXFL.length() ==0)&&(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
					{
						// in case of Addition Tax flag can not be kept blank.
						setMSG("Tax Flag can not be kept blank in common taxes..",'E');
						return false;
					}
					else if(L_strTAXVL.length() ==0)
					{
						setMSG("Tax Value can not be kept blank in common taxes..",'E');
							return false;
					}
					else if(L_strTAXFL.length() >0)
					{
						if(Double.parseDouble(L_strTAXVL) > 0)
						{
							// Flags should be 'A' or 'P' for value >0
							if((!L_strTAXFL.equals("A"))&&(!L_strTAXFL.equals("P")))
							{
								setMSG("For Tax value >0, Flags can be 'A' or 'P' only ..",'E');
								return false;
							}
						}
						else
						{
							if((!L_strTAXFL.equals("I"))&&(!L_strTAXFL.equals("X")))
							{
								setMSG("For Tax value  = 0, Flags can be 'I' or 'X' only ..",'E');
								return false;
							}
						}
					}
				}
			}
			strGTXDS ="";
			strGOTDS =""; // Gradewise other tax
			// check for duplicate tax code
			//////
			for(int i=0;i<tblITTAX.getRowCount();i++)
			{
				 boolean L_flgFOUND = false;
			    
				if(tblITTAX.getValueAt(i,TBL_CHKFL)== Boolean.TRUE)
				{
					L_flgFOUND = false;
					L_strTAXFL = tblITTAX.getValueAt(i,4).toString();
					L_strTAXVL = tblITTAX.getValueAt(i,5).toString();
					strTEMP = tblITTAX.getValueAt(i,1).toString();
					if(strTEMP.length() ==0)
					{
						setMSG("Item Code can not be kept blank in Itemwise tax..",'E');
						return false;
					}
					strTEMP = tblITTAX.getValueAt(i,2).toString();
					if(strTEMP.length() ==0)
					{
						setMSG("Tax Code can not be kept blank in Itemwise tax..",'E');
						return false;
					}
					else if((L_strTAXFL.length() ==0)&&(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
					{
						// in case of Addition Tax flag can not be kept blank.
						setMSG("Tax Flag can not be kept blank in Itemwise tax..",'E');
						return false;
					}
					else if(L_strTAXVL.length() ==0)
					{
						setMSG("Tax Value can not be kept blank in Itemwise tax..",'E');
						return false;
					}
					else if(L_strTAXFL.length() >0)
					{
						if(Double.parseDouble(L_strTAXVL) > 0)
						{
							// Flags should be 'A' or 'P' for value >0
							if((!L_strTAXFL.equals("A"))&&(!L_strTAXFL.equals("P")))
							{
								setMSG("For Tax value >0, Flags can be 'A' or 'P' only ..",'E');
								return false;
							}
						}
						else
						{
							if((!L_strTAXFL.equals("I"))&&(!L_strTAXFL.equals("X")))
							{
								setMSG("For Tax value  = 0, Flags can be 'I' or 'X' only ..",'E');
								return false;
							}
						}
					}
				/// Added on 15/09/2004	
				   strTEMP = tblITTAX.getValueAt(i,2).toString().trim();
				   for(int j=0;j<tblCOTAX.getRowCount();j++)
				   {
					   if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					   {
							if(tblCOTAX.getValueAt(j,0) == Boolean.TRUE)
							{
								   if(strTEMP.equals(tblCOTAX.getValueAt(j,1).toString().trim()))
								   {
										setMSG("Tax code "+strTEMP +" is taken in both Common/ Item wise taxes..",'E');   								
										return false;
								   }
							}
					   }
					   else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					      if(strTEMP.equals(tblCOTAX.getValueAt(j,1).toString().trim()))
						   {
								setMSG("Tax code "+strTEMP +" is taken in both Common/ Item wise taxes..",'E');   				
								return false;
						   }
					   
				   }
				   strTEMP =tblITTAX.getValueAt(i,1).toString().trim();
				   for(int j=0;j<tblITDTL.getRowCount();j++)
				   {
					 if(tblITDTL.getValueAt(j,2).toString().length()> 0 ) // Ite,m code is present
					   // checking whether the item code in Item wise tax is present or not 
					  	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							if(tblITTAX.getValueAt(i,0) == Boolean.TRUE)
							if(strTEMP.equals(tblITDTL.getValueAt(j,2).toString().trim()))
							{
								L_flgFOUND = true;
								break;
							}
						}
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							
							if(tblITDTL.getValueAt(j,2).toString().length()> 0 ) // Ite,m code is present
							 {
								   if(strTEMP.equals(tblITDTL.getValueAt(j,2).toString().trim()))
							 	   {
							 			L_flgFOUND = true;
										break;
							 	   }
							 }
						}
				   }
					if(!L_flgFOUND)
					{
						setMSG("Wrong Item Code in Item wise Tax ..",'E');
						return false;
					}
				}
				/// end Added on 15/09/2004	
			}
			////////
			Hashtable<String,String> L_hstMATCD = new Hashtable<String,String>();
			String L_strMATCD ="";
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))&&(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
			{
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						if(tblITDTL.getValueAt(i,TBL_PORRT).toString().trim().length() ==0)
						{
							setMSG("Enter P.O. Rate..",'E');
							return false;
						}
						//if((tblITDTL.getValueAt(i,TBL_INDNO).toString().trim().length() ==0)&&(!cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn)))
						if(tblITDTL.getValueAt(i,TBL_INDNO).toString().trim().length() ==0)
						{
							setMSG("Indent No. is not given in Row No. .."+ i,'E');
							return false;
						}
						if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
						{
							if(tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().length() > 2)
						    {
								if(!tblITDTL.getValueAt(i,TBL_MATCD).toString().substring(0,2).equals("95"))	
								{
									setMSG("Only 95 group items can be taken for Project P.O. geneartion..",'E');
									return false;
								}
							}
						}
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							if(!vtrGRPCD.contains((String)tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().substring(0,2)))
							{
								setMSG("Vendor is not approved for group "+tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().substring(0,2)+"..",'E');
								//return false;
							}
						}
						L_intITCNT++;
						L_dblTPOQT += Double.parseDouble(tblITDTL.getValueAt(i,TBL_ORDQT).toString());
					}
					L_strMATCD = tblITDTL.getValueAt(i,TBL_MATCD).toString();
					if(L_strMATCD.length() >0)
					{
						if(!L_hstMATCD.containsKey((String)L_strMATCD))
						{
							L_hstMATCD.put(L_strMATCD,tblITDTL.getValueAt(i,TBL_PORRT).toString());
						}
						else
						{
							if(tblITDTL.getValueAt(i,TBL_PORRT).toString().length() >0)
							if(Double.parseDouble(tblITDTL.getValueAt(i,TBL_PORRT).toString()) != Double.parseDouble(L_hstMATCD.get(L_strMATCD).toString()))
							{
								setMSG("Two Rates for same Item code not allowed.. ",'E');
								return false;
							}
						}
					}
				}
				if(L_intITCNT ==0)
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)){
					setMSG("Enter at least one item under Item Details ..",'E');
					return false;
					}
				}
				else
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						for(int i=0;i<tblDLSCH.getRowCount();i++)
						{
							if(tblDLSCH.getValueAt(i,TBL_CHKFL).toString().equals("true"))
							{
								L_intDLCNT++;
								L_dblTDLQT += Double.parseDouble(tblDLSCH.getValueAt(i,TB4_EDLQT).toString());
								if(tblDLSCH.getValueAt(i,TB4_EDLDT).toString().length() ==0)
								{
									setMSG("Enter Date in Delivery Schedule..",'E');
									return false;
								}
							}
						}
						if(L_intDLCNT ==0)
						{
							setMSG("Enter Delivery schedule.. ",'E');
							return false;
						}
						if(L_intITCNT > L_intDLCNT)			// if Item count is greater than no. of rows entered in Delivery table
						{
							setMSG("Improper Delivery schedule entry..,Please Correct",'E');
							return false;
						}
						if(L_dblTPOQT != L_dblTDLQT)
						{
							setMSG("P.O qty. and Del. schedule. qty. does not match.. ",'E');
							return false;
						}
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				// Blocking of Non approved vendor at Authorisation
				//vldVENCD(txtVENCD.getText().trim());
				vtrGRPCD = new Vector<String>();
				// MATERIAL Group validation is applicable for Groups other than
				// 47_49_60_62_85_86_95_98_99
				// To skip these codes from validation, theses are added in Vector<String> of group codes for all vendors.
				String[] L_strGRPCD = new String[]{"47","49","60","62","85","86","95","98","99"};
				for (int i=0;i<L_strGRPCD.length;i++)
					vtrGRPCD.addElement(L_strGRPCD[i]);
				M_strSQLQRY ="SELECT AV_GRPCD FROM MM_AVMST WHERE  AV_PRTTP ='S' and AV_PRTCD ='"+txtVENCD.getText().trim() +"' and isnull(AV_STSFL,'') <>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
						vtrGRPCD.addElement(M_rstRSSET.getString("AV_GRPCD"));
					M_rstRSSET.close();
				}
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					if(tblITDTL.getValueAt(i,TBL_MATCD).toString().length() >2)
					{
						if(!vtrGRPCD.contains((String)tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().substring(0,2)))
						{
							setMSG("Vendor is not approved for group "+tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().substring(0,2)+"..",'E');
								return false;
						}
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)&& flgAMDFL)
			{
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						if(hstACPQT.containsKey(tblITDTL.getValueAt(i,TBL_MATCD).toString()))
						{
							if(Float.parseFloat(hstACPQT.get(tblITDTL.getValueAt(i,TBL_MATCD).toString()).toString()) >0)
							{
								setMSG("Accepted Quantity > 0,Cancellation is not allowed for "+tblITDTL.getValueAt(i,TBL_MATCD).toString(),'E');
								setCursor(cl_dat.M_curDFSTS_pbst);
								return false;
							}
						}
					}
				}
			}
			if(tblITDTL.isEditing())
			{
				if(tblITDTL.getValueAt(tblITDTL.getSelectedRow(),tblITDTL.getSelectedColumn()).toString().length()>0)
				{
					mm_teporTBLINVFR obj=new mm_teporTBLINVFR();
					obj.setSource(tblITDTL);
					if(obj.verify(tblITDTL.getSelectedRow(),tblITDTL.getSelectedColumn()))
						tblITDTL.getCellEditor().stopCellEditing();
					else
						return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
			return false;
		}
		return true;
	}
	void exeSAVE()
	{
		try{
		String L_strDELDT="";
		
		if(!vldDATA())
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
			return;
		}
        intINDCT =0;
		if(tblITDTL.isEditing())
			tblITDTL.getCellEditor().stopCellEditing();
		if(tblCOTAX.isEditing())
			tblCOTAX.getCellEditor().stopCellEditing();
		if(tblITTAX.isEditing())
			tblITTAX.getCellEditor().stopCellEditing();
		if(tblDLSCH.isEditing())
			tblDLSCH.getCellEditor().stopCellEditing();
		if(hstPENQT !=null)
			hstPENQT.clear();
		if(hstINDNO !=null)
			hstINDNO.clear();
		
		cl_dat.M_flgLCUPD_pbst = true;
		setCursor(cl_dat.M_curWTSTS_pbst);
		setMSG("Saving of data in progress..",'N');
		cl_dat.M_btnSAVE_pbst.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
		{
			M_strSQLQRY = "UPDATE MM_POMST SET PO_AUTBY ='"+cl_dat.M_strUSRCD_pbst+"',PO_AUTDT = current_date,PO_STSFL ='A',PO_TRNFL ='0',PO_LUPDT = CURRENT_DATE";
			M_strSQLQRY +=" where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY +=" and PO_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
			M_strSQLQRY +=" and PO_PORNO ='"+txtPORNO.getText().trim()+"' AND PO_stsfl ='O'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				mm_rppor obj = new mm_rppor(M_strSBSCD,hstTAXCD,vtrTAXCD,vtrTAXTP,txtCURCD.getText().trim(),Double.parseDouble(txtEXGRT.getText().trim()));
		    	obj.calPOVAL(txtPORNO.getText().trim(),txtAMDNO.getText().trim(),'A');
			    setMSG("P.O. has been Authorised..",'N');
				//Moved to constructor, as Vector<String> was increasing everytime, leading to wrong value calculation
				//crtHSTXCD();
				if(!txtAMDNO.getText().trim().equals("00"))
				{
				M_strSQLQRY = "SELECT sp_ystdt,sp_yendt,sp_stsfl from sa_spmst where "+
								  " sp_syscd ='MM' ";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					String L_strPRCFL ="",L_strYSTDT="",L_strYENDT="";
					if(M_rstRSSET !=null)
					{
						if(M_rstRSSET.next())
						{
							datTEMP = M_rstRSSET.getDate("SP_YSTDT");
							if(datTEMP !=null)
							{
								L_strYSTDT = M_fmtLCDAT.format(datTEMP);
							}
							L_strPRCFL = M_rstRSSET.getString("SP_STSFL");
						}
						M_rstRSSET.close();
					}
					cstAMDPR.setString(1,cl_dat.M_strCMPCD_pbst);
					cstAMDPR.setString(2,M_strSBSCD.substring(2,4));
					cstAMDPR.setString(3,txtPORNO.getText().trim());
					strTEMP = L_strYSTDT.substring(6,10) + "-" +  L_strYSTDT.substring(3,5) + "-" +  L_strYSTDT.substring(0,2);
					cstAMDPR.setDate(4,Date.valueOf(strTEMP));
					cstAMDPR.setString(5,L_strPRCFL);
					cstAMDPR.executeUpdate();
                    // Added this check on 20/06/2005 for ARC Amendment provision
                    if(!cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strRTCON_fn))			
					    adjISSVL(L_strPRCFL,L_strYSTDT);
					if(cl_dat.exeDBCMT("adjISSVL"))
					{
						setMSG("Saved sucessfully..",'N');
					}
					else
						setMSG("Error in value adjustment procedure ..",'E');
				}
				flgAMDFL = false;
				clrCOMP();
	            setENBL(false);
				txtPORNO.requestFocus();
				setCursor(cl_dat.M_curDFSTS_pbst);
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			txtAMDNO.setText("00");
			/*if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
			{
				if(!genPRJIN())
				{
					setCursor(cl_dat.M_curDFSTS_pbst);
					setMSG("Error in generating P.O. Number..",'E');
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					cl_dat.M_flgLCUPD_pbst = false;
					return;
				}
			}
			if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
			{
				strDPTCD =vtrDPTCD.elementAt(cmbPOSRL.getSelectedIndex()+1).toString(); 
			}
			else*/
			if(!genPORNO())
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG("Error in generating P.O. Number..",'E');
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				cl_dat.M_flgLCUPD_pbst = false;
				return;
			}
			for(int i=0;i<tblITDTL.getRowCount();i++)
			{
				if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(!insPODTL(i))
					{
						cl_dat.M_flgLCUPD_pbst = false;
						return;
					}
				}
			}
			for(int i=0;i<tblDLSCH.getRowCount();i++)
			{
				if(tblDLSCH.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					pstmDODREC.setString(1,cl_dat.M_strCMPCD_pbst);
					pstmDODREC.setString(2,M_strSBSCD);
					pstmDODREC.setString(3,M_strSBSCD.substring(2,4));
					pstmDODREC.setString(4,cmbPORTP.getSelectedItem().toString().substring(0,2));
					pstmDODREC.setString(5,txtPORNO.getText().trim());
					pstmDODREC.setString(6,tblDLSCH.getValueAt(i,TB4_MATCD).toString());
				/*	if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
						pstmDODREC.setString(6,strINDNO);
					else*/
						pstmDODREC.setString(7,tblDLSCH.getValueAt(i,TB4_INDNO).toString());
					L_strDELDT = tblDLSCH.getValueAt(i,TB4_EDLDT).toString();
					strTEMP = L_strDELDT.substring(6,10) + "-" +  L_strDELDT.substring(3,5) + "-" +  L_strDELDT.substring(0,2);
					pstmDODREC.setDate(8,Date.valueOf(strTEMP));
					pstmDODREC.setFloat(9,Float.valueOf(tblDLSCH.getValueAt(i,TB4_EDLQT).toString()).floatValue());
					pstmDODREC.setString(10,txtAMDNO.getText().trim());
					pstmDODREC.setString(11,"0");
					pstmDODREC.setString(12,"");
					pstmDODREC.setString(13,cl_dat.M_strUSRCD_pbst);
					strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
					pstmDODREC.setDate(14,Date.valueOf(strTEMP));
					pstmDODREC.executeUpdate();
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				//if(!cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
				//{
					M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CCSVL = '"+strSRLNO +"',CMT_CHP01 =''";
					M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"POR'";
					M_strSQLQRY += " AND CMT_SHRDS = '" + cmbPOSRL.getSelectedItem().toString()+ "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				//}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			if(txtREMDS.getText().trim().length() >0)
				insRMMST("PO","POR",txtREMDS.getText().trim());
			savPOANX();
			if(!crtCOTAX(txtPORNO.getText().trim()))
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setMSG("Error In adding common Tax ..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = false;
				return;
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					intDLRWCT =0;
					setMSG("Record Saved",'N');
					if(hstDLSCH !=null)
						hstDLSCH.clear();
					if(hstDLQTY !=null)
						hstDLQTY.clear();
					if(hstITTAX !=null)
						hstITTAX.clear();
					if(hstACPQT !=null)
						hstACPQT.clear();
					if(hstBILQT !=null)
						hstBILQT.clear();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						JOptionPane.showMessageDialog(this,"Please, Note down the P.O No. " + txtPORNO.getText().trim(),"P.O. No.",JOptionPane.INFORMATION_MESSAGE);
						strPORNO = txtPORNO.getText().trim();
						strPORTP = cmbPORTP.getSelectedItem().toString().substring(0,2);
						btnPRNT.setEnabled(true);
						clrCOMP();
					//	if(!cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
							txtPORNO.setEnabled(false);
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			if(flgAMDFL)
			{
				M_strSQLQRY = "Insert into mm_pomam select * from mm_pomst where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND po_strtp ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY += " AND po_porno ='"+txtPORNO.getText().trim() +"'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			delPODTL();
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{
			if(flgAMDFL)
			{
				M_strSQLQRY = "Insert into mm_pomam select * from mm_pomst where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND po_strtp ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY += " AND po_porno ='"+txtPORNO.getText().trim() +"'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				// create a record in amendment file and then update the P.O details
			}
			updPODTL();
		}
	}
	catch(Exception L_E)
	{
		try
		{
			setMSG(L_E,"exeSAVE");
			if(cl_dat.M_conSPDBA_pbst !=null)
				cl_dat.M_conSPDBA_pbst.rollback();
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(SQLException L_SE)
		{
			setMSG(L_E,"rollback");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	finally
	{	
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
	}
	}
	private boolean getDATA()
	{
		try
		{
			ResultSet L_rstRSSET;
			int L_intRECCT =0,i=0;
			String L_strPORTP ="";
			tblITDTL.clrTABLE();
			tblCOTAX.clrTABLE();
			tblITTAX.clrTABLE();
			tblDLSCH.clrTABLE();
			txtREMDS.setText("");
			txaPORMK.setText("");
			if(tblITDTL.isEditing())
			tblITDTL.getCellEditor().stopCellEditing();
			if(tblCOTAX.isEditing())
				tblCOTAX.getCellEditor().stopCellEditing();
			if(tblITTAX.isEditing())
				tblITTAX.getCellEditor().stopCellEditing();
			if(tblDLSCH.isEditing())
				tblDLSCH.getCellEditor().stopCellEditing();
			tblITDTL.setColumnSelectionInterval(0,0);
			tblITDTL.setRowSelectionInterval(0,0);
			tblCOTAX.setColumnSelectionInterval(0,0);
			tblCOTAX.setRowSelectionInterval(0,0);
			tblITTAX.setColumnSelectionInterval(0,0);
			tblITTAX.setRowSelectionInterval(0,0);
			tblDLSCH.setColumnSelectionInterval(0,0);
			tblDLSCH.setRowSelectionInterval(0,0);
			
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strAMDNO ="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_rstRSSET = cl_dat.exeSQLQRY("select distinct po_amdno from mm_pomst where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND po_stsfl <>'X'");
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						if(!nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),"").equals("00"))
						{
							L_strAMDNO ="00";
							setMSG("Modification is not allowed..",'E');
							//return false;
						}
					}
				M_rstRSSET.close();	
				}
			}
			M_strSQLQRY ="Select * from MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
			M_strSQLQRY +=" AND PO_PORNO ='"+txtPORNO.getText().trim()+"' AND isnull(PO_STSFL,'') <>'X'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				M_strSQLQRY +=" AND isnull(PO_STSFL,'') ='O'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				M_strSQLQRY +=" AND isnull(PO_ACPQT,0) =0";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				// if amendended then only authorised records will be shown
				if(!L_strAMDNO.equals("00")) 
					M_strSQLQRY +=" order by po_stsfl"; // order bys stsfl is taken else
				else																// check ahead will fail
					M_strSQLQRY +=" AND isnull(PO_STSFL,'') ='O'";
			}				
			M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET ==null)
			{
				setMSG("Error in Getting the data..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
			flgAMDFL = false;
			vtrINMAT.clear();
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					vtrINMAT.add(L_intRECCT,nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),"")+ nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),""));
					if(L_intRECCT ==0)
					{
						String L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("PO_STSFL"),"");
						txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),""));
						if(L_strSTSFL.equals("A")||L_strSTSFL.equals("H")||L_strSTSFL.equals("C"))
						{
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							{
								setMSG("Already Authorised..",'E');
								setCursor(cl_dat.M_curDFSTS_pbst);
								return false;
							}
							//else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
							else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							{
								txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),""));
								int L_SELOPT =0;
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
									L_SELOPT = JOptionPane.showConfirmDialog(this,"P.O. is Authorised,Do you want to Cancel the order..?","Cancellation confirmation ",JOptionPane.YES_NO_OPTION); 
								else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
								{
									L_SELOPT = JOptionPane.showConfirmDialog(this,"P.O. is Authorised,Do you want to Amend the order..?","Amendment confirmation ",JOptionPane.YES_NO_OPTION); 
									tblCOTAX.setEnabled(true);
									tblITTAX.setEnabled(true);
									tblITDTL.cmpEDITR[TBL_PORRT].setEnabled(true);
									tblITDTL.cmpEDITR[TBL_PERRT].setEnabled(true);
								}
								strTEMP ="";
								if(L_SELOPT ==0)
								{
									hstACPQT.clear();
									flgAMDFL = true;
									txtAMDNO.setEnabled(false);
									strPRAMD = txtAMDNO.getText().trim();
									int L_intTEMP = Integer.parseInt(txtAMDNO.getText().trim()) + 1;
									for(int j=0;j<2-String.valueOf(L_intTEMP).toString().length();j++)
										strTEMP +=0;
									strTEMP = strTEMP +String.valueOf(L_intTEMP);
								//	txtAMDNO.setText(strTEMP+String.valueOf(L_intTEMP));
									txtAMDNO.setText(strTEMP);
									txtAMDDT.setText(cl_dat.M_strLOGDT_pbst);
									txtVENCD.setEnabled(false);
									
								}
								else
								{
									flgAMDFL = false;
									txtVENCD.setEnabled(true);
									setCursor(cl_dat.M_curDFSTS_pbst);
									return false;
								}
							}
					
						}
						L_strPORTP = nvlSTRVL(M_rstRSSET.getString("PO_PORTP"),"");
						for(i=0;i<cmbPORTP.getItemCount();i++)
						{
							if(cmbPORTP.getItemAt(i).toString().substring(0,2).equals(L_strPORTP))
							{
								cmbPORTP.setSelectedIndex(i);
								break;
							}
						}
						datTEMP = M_rstRSSET.getDate("PO_PORDT");
						if(datTEMP !=null)
						{
							txtORDDT.setText(M_fmtLCDAT.format(datTEMP));
						}
						txtSHRDS.setText(nvlSTRVL(M_rstRSSET.getString("PO_SHRDS"),""));
						txtBYRCD.setText(nvlSTRVL(M_rstRSSET.getString("PO_BUYCD"),""));
						datTEMP = M_rstRSSET.getDate("PO_AMDDT");
						if(datTEMP !=null)
						{
							txtAMDDT.setText(M_fmtLCDAT.format(datTEMP));
						}
						datTEMP = M_rstRSSET.getDate("PO_EFFDT");
						if(datTEMP !=null)
						{
							txtEFFDT.setText(M_fmtLCDAT.format(datTEMP));
						}
						datTEMP = M_rstRSSET.getDate("PO_CMPDT");
						if(datTEMP !=null)
						{
							txtCMPDT.setText(M_fmtLCDAT.format(datTEMP));
						}
						strTEMP = nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),"");
						txtVENCD.setText(strTEMP);
						M_strSQLQRY = "SELECT PT_PRTNM from CO_PTMST where ";
						M_strSQLQRY += " PT_PRTTP ='S' AND isnull(PT_STSFL,'') <>'X' and PT_PRTCD ='"+strTEMP+"'";
						rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(rstRSSET !=null)
						{
							if(rstRSSET.next())
								txtVENNM.setText(nvlSTRVL(rstRSSET.getString("PT_PRTNM"),""));
							rstRSSET.close();
						}
						strTEMP = nvlSTRVL(M_rstRSSET.getString("PO_CURCD"),"");
						txtCURCD.setText(strTEMP);
						txtCURDS.setText(cl_dat.getPRMCOD("CMT_CODDS","MST","COXXCUR",strTEMP));
						txtEXGRT.setText(nvlSTRVL(M_rstRSSET.getString("PO_EXGRT"),""));
						txtQTNTP.setText(nvlSTRVL(M_rstRSSET.getString("PO_QTNTP"),""));
						txtQTNNO.setText(nvlSTRVL(M_rstRSSET.getString("PO_QTNNO"),""));
						txtCRDDY.setText(nvlSTRVL(M_rstRSSET.getString("PO_CRDDY"),""));
						datTEMP = M_rstRSSET.getDate("PO_QTNDT");
						if(datTEMP !=null)
						{
							txtQTNDT.setText(M_fmtLCDAT.format(datTEMP));
						}
						strTEMP = (nvlSTRVL(M_rstRSSET.getString("PO_DELTP"),""));
						for(i=0;i<cmbDELTP.getItemCount();i++)
						{
							if(cmbDELTP.getItemAt(i).toString().substring(0,2).equals(strTEMP))
							{
								cmbDELTP.setSelectedIndex(i);
								break;
							}
						}
						txtPMTRF.setText(nvlSTRVL(M_rstRSSET.getString("PO_PMTRF"),""));
						strTEMP = (nvlSTRVL(M_rstRSSET.getString("PO_DSTCD"),""));
						for(i=0;i<cmbDSTCD.getItemCount();i++)
						{
							if(cmbDSTCD.getItemAt(i).toString().substring(0,3).equals(strTEMP))
							{
								cmbDSTCD.setSelectedIndex(i);
								break;
							}
						}
						if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
						{
							strINDNO =nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),"");
							strDPTCD =nvlSTRVL(M_rstRSSET.getString("PO_DPTCD"),"");
						}
					}
					if(flgAMDFL)
					{
						M_strSQLQRY  = "SELECT GR_MATCD,sum(isnull(GR_ACPQT,0)) L_ACPQT,sum(isnull(GR_BILQT,0)) L_BILQT from MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
						M_strSQLQRY  +=" and GR_PORNO ='"+txtPORNO.getText().trim()+"' group by gr_strtp,gr_matcd";
						L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						hstACPQT.clear();
						hstBILQT.clear();
						if(L_rstRSSET !=null)
						{
							while(L_rstRSSET.next())
							{
								hstACPQT.put(L_rstRSSET.getString("GR_MATCD"),nvlSTRVL(L_rstRSSET.getString("L_ACPQT"),"0.00"));
								if(L_rstRSSET.getDouble("L_BILQT") >0)
								{	
									tblCOTAX.setEnabled(false);
									tblITTAX.setEnabled(false);
									/* Comment this temporarily for rate amendment after bill passing */
									//tblITDTL.cmpEDITR[TBL_PORRT].setEnabled(false);
									tblITDTL.cmpEDITR[TBL_PERRT].setEnabled(false);
								}
								hstBILQT.put(L_rstRSSET.getString("GR_MATCD"),nvlSTRVL(L_rstRSSET.getString("L_BILQT"),"0.00"));
							}
							L_rstRSSET.close();
						}
					}
					//UOM","Min.Pkg.qty.","Del. Date"}
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),""),L_intRECCT,TBL_INDNO);
					
					
					strTEMP = nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),"");
					tblITDTL.setValueAt(strTEMP,L_intRECCT,TBL_MATCD);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_UOMCD"),""),L_intRECCT,TBL_POUOM);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_UCNVL"),"0"),L_intRECCT,TBL_CNVFT);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),"0"),L_intRECCT,TBL_PORRT);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PERRT"),"0"),L_intRECCT,TBL_PERRT);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0"),L_intRECCT,TBL_ORDQT);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_MATDS"),""),L_intRECCT,TBL_MATDS);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_DPTCD")," "),L_intRECCT,TBL_DPTCD);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_STSFL")," "),L_intRECCT,TBL_STSFL);
					if(nvlSTRVL(M_rstRSSET.getString("PO_TCFFL"),"N").equals("N"))
						tblITDTL.setValueAt(Boolean.FALSE,L_intRECCT,TBL_TCFFL);
					else
						tblITDTL.setValueAt(Boolean.TRUE,L_intRECCT,TBL_TCFFL);
					if(nvlSTRVL(M_rstRSSET.getString("PO_INSFL"),"N").equals("N"))
						tblITDTL.setValueAt(Boolean.FALSE,L_intRECCT,TBL_INSFL);
					else
						tblITDTL.setValueAt(Boolean.TRUE,L_intRECCT,TBL_INSFL);
					M_strSQLQRY = "SELECT CT_MATDS,CT_UOMCD,CT_PKGQT from CO_CTMST where ";
						M_strSQLQRY += " isnull(CT_STSFL,'') <>'X' and CT_MATCD ='"+strTEMP+"'";
						rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(rstRSSET !=null)
						{
							if(rstRSSET.next())
							{
								tblITDTL.setValueAt(nvlSTRVL(rstRSSET.getString("CT_UOMCD"),""),L_intRECCT,TBL_UOMCD);
								tblITDTL.setValueAt(nvlSTRVL(rstRSSET.getString("CT_PKGQT"),""),L_intRECCT,TBL_MPKQT);
								//tblITDTL.setValueAt(nvlSTRVL(rstRSSET.getString("CT_MATDS"),""),L_intRECCT,TBL_MATDS);
			
							}
							rstRSSET.close();
						}
					L_intRECCT++;
					// DELEVERY DATE
				}
				M_rstRSSET.close();
			}
			if(L_intRECCT ==0)
			{
				setMSG("PO details are not found..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}		
			M_strSQLQRY = "SELECT * FROM MM_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
				M_strSQLQRY += " AND RM_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY += " AND RM_DOCTP ='"+strTRNTP +"'";
				M_strSQLQRY += " AND RM_TRNTP ='"+strTRNTP +"'";
				M_strSQLQRY += " AND RM_REMTP ='POR'";
				M_strSQLQRY += " AND RM_DOCNO ='"+txtPORNO.getText().trim()+"'";
				//	AND isnull(RM_STSFL,'') <>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				flgPORRM = false;
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getString("RM_REMTP").equals("POR"))
						{
							if(!M_rstRSSET.getString("RM_STSFL").equals("X"))
							{
								txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
							}
							flgPORRM = true;
						}
					}
					M_rstRSSET.close();
				}
					M_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXDOC where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD='MM' and TX_DOCTP='POR' and "
							+"tx_DOCNO='"+txtPORNO.getText().trim()+"' and "
							+"TX_SBSCD='"+M_strSBSCD+"' and isnull(TX_STSFL,'') <>'X'");
					flgCOTAX = false;
					if(hstITTAX !=null)
						hstITTAX.clear();
					if(M_rstRSSET!=null)
					{
						//int i=0;
						intGTXRW =0;
						intCTXRW =0;
						while(M_rstRSSET.next())
						{
							strTEMP = M_rstRSSET.getString("TX_PRDCD");
							if(strTEMP.equalsIgnoreCase("XXXXXXXXXX"))
							{
								flgCOTAX = true;

								dspCOTAX(M_rstRSSET,"TX_DSBVL");
								dspCOTAX(M_rstRSSET,"TX_EXCVL");
								dspCOTAX(M_rstRSSET,"TX_PNFVL");
								dspCOTAX(M_rstRSSET,"TX_CSTVL");
								dspCOTAX(M_rstRSSET,"TX_STXVL");
								dspCOTAX(M_rstRSSET,"TX_OCTVL");
								dspCOTAX(M_rstRSSET,"TX_FRTVL");
								dspCOTAX(M_rstRSSET,"TX_INSVL");
								dspCOTAX(M_rstRSSET,"TX_CDSVL");
								dspCOTAX(M_rstRSSET,"TX_INCVL");
								dspCOTAX(M_rstRSSET,"TX_ENCVL");
								dspCOTAX(M_rstRSSET,"TX_FNIVL");
								dspCOTAX(M_rstRSSET,"TX_CDUVL");
								dspCOTAX(M_rstRSSET,"TX_CLRVL");
								dspCOTAX(M_rstRSSET,"TX_OTHVL");
								dspCOTAX(M_rstRSSET,"TX_RSTVL");
								dspCOTAX(M_rstRSSET,"TX_SCHVL");
								dspCOTAX(M_rstRSSET,"TX_CVDVL");	
								dspCOTAX(M_rstRSSET,"TX_WCTVL");	
								dspCOTAX(M_rstRSSET,"TX_VATVL");	
								dspCOTAX(M_rstRSSET,"TX_EDCVL");
								dspCOTAX(M_rstRSSET,"TX_EHCVL");
								dspCOTAX(M_rstRSSET,"TX_SVCVL");

							}
							else
							{
								if(hstITTAX !=null)
								if(!hstITTAX.containsKey(strTEMP));
									hstITTAX.put(strTEMP,"");
								dspGRTAX(M_rstRSSET,"TX_DSBVL");
								dspGRTAX(M_rstRSSET,"TX_EXCVL");
								dspGRTAX(M_rstRSSET,"TX_PNFVL");
								dspGRTAX(M_rstRSSET,"TX_CSTVL");
								dspGRTAX(M_rstRSSET,"TX_STXVL");
								dspGRTAX(M_rstRSSET,"TX_OCTVL");
								dspGRTAX(M_rstRSSET,"TX_FRTVL");
								dspGRTAX(M_rstRSSET,"TX_INSVL");
								dspGRTAX(M_rstRSSET,"TX_CDSVL");
								dspGRTAX(M_rstRSSET,"TX_INCVL");
								dspGRTAX(M_rstRSSET,"TX_ENCVL");
								dspGRTAX(M_rstRSSET,"TX_FNIVL");
								dspGRTAX(M_rstRSSET,"TX_CDUVL");
								dspGRTAX(M_rstRSSET,"TX_CLRVL");
								dspGRTAX(M_rstRSSET,"TX_OTHVL");
								dspGRTAX(M_rstRSSET,"TX_RSTVL");
								dspGRTAX(M_rstRSSET,"TX_SCHVL");
								dspGRTAX(M_rstRSSET,"TX_CVDVL");	
								dspGRTAX(M_rstRSSET,"TX_WCTVL");	
								dspGRTAX(M_rstRSSET,"TX_VATVL");	
								dspGRTAX(M_rstRSSET,"TX_EDCVL");
								dspGRTAX(M_rstRSSET,"TX_EHCVL");
								
							}
						}
						M_rstRSSET.close();
					}
					M_strSQLQRY="Select * from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD='MM' "
						+"and TXT_DOCTP='POR' and TXT_SBSCD='"+M_strSBSCD+"' and "
						+"txT_DOCNO='"+txtPORNO.getText()+"' and isnull(TXT_STSFL,'') <>'X'"// and "
	//					+"TXT_PRTTP='C'"
						;
					flgTXSPC = false;
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						int L_intROWCT=0;
						L_intROWCT= intCTXRW;
						flgTXSPC = true;
						while(M_rstRSSET.next())
						{
							if(M_rstRSSET.getString("TXT_PRDCD").equals("XXXXXXXXXX"))
							{
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODCD"),L_intROWCT,1);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODDS"),L_intROWCT,2);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODFL"),L_intROWCT,3);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODVL"),L_intROWCT,4);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_PRCSQ"),L_intROWCT++,5);
							}
							else
							{
								strTEMP = M_rstRSSET.getString("TXT_PRDCD");
								hstITTAX.put(strTEMP,"");
								tblITTAX.setValueAt(strTEMP,intGTXRW,1);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODCD"),intGTXRW,2);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODDS"),intGTXRW,3);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODFL"),intGTXRW,4);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODVL"),intGTXRW,5);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_PRCSQ"),intGTXRW,6);
								intGTXRW++;
							
							}
						}
						M_rstRSSET.close();	
					}
				M_strSQLQRY =  " SELECT * FROM MM_PODEL WHERE POD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND POD_MMSBS =";
				M_strSQLQRY += "'"+M_strSBSCD+"'";
				M_strSQLQRY += " AND POD_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
				M_strSQLQRY += " AND POD_PORNO = '"+txtPORNO.getText().trim()+"' AND isnull(POD_STSFL,'') <>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				tblDLSCH.clrTABLE();
				i=0;
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
						tblDLSCH.setValueAt(nvlSTRVL(M_rstRSSET.getString("POD_INDNO"),""),i,TB4_INDNO);
						tblDLSCH.setValueAt(nvlSTRVL(M_rstRSSET.getString("POD_MATCD"),""),i,TB4_MATCD);
						tblDLSCH.setValueAt(nvlSTRVL(M_rstRSSET.getString("POD_EDLDT"),""),i,TB4_EDLDT);
						tblDLSCH.setValueAt(nvlSTRVL(M_rstRSSET.getString("POD_EDLQT"),"0"),i,TB4_EDLQT);
						i++;
					}
					M_rstRSSET.close();
				}
				i=0;
				flgPOANX = false;
				M_strSQLQRY =  " SELECT PO_PORMK,PO_STSFL FROM MM_PORMK WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS =";
				M_strSQLQRY += "'"+M_strSBSCD+"'";
				M_strSQLQRY += " AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
				M_strSQLQRY += " AND PO_PORNO = '"+txtPORNO.getText().trim()+"'";
			//	M_strSQLQRY += " AND PO_STSFL <>'X'";
				txaPORMK.setText("");
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						if(!nvlSTRVL(M_rstRSSET.getString("PO_STSFL"),"").equals("X"))
							txaPORMK.setText(nvlSTRVL(M_rstRSSET.getString("PO_PORMK"),""));
						flgPOANX = true;
					}
					M_rstRSSET.close();
				}
	}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
			//System.out.println("Error in getdata");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
		return true;
	}
	private void getMATDT(String P_strMATCD,int P_intROWID) 
	{
		try
		{
			M_strSQLQRY ="SELECT CT_UOMCD,CT_PKGQT,CT_MATDS FROM CO_CTMST WHERE CT_MATCD ='"+P_strMATCD.trim() +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),P_intROWID,TBL_UOMCD);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),"0"),P_intROWID,TBL_MPKQT);
					tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"0"),P_intROWID,TBL_MATDS);
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getMATDT");
		}
	}
	private void crtHSTTXCAT()
	{
		try
		{		//RETRIEVING TAX CATAGORY DETAILS
			M_strSQLQRY="Select CMT_CODCD,CMT_CODDS,CMT_NCSVL,CMT_CHP02,CMT_CHP01,CMT_CCSVL from  CO_CDTRN where CMT_CGSTP='COXXTAX' ";
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET!=null)
			{
				hstTXCAT=new Hashtable<String,String>(10,0.2f);
				int i=0;
				tblCOTAX.clrTABLE();
				while(L_rstRSSET.next())
				{
					hstTXCAT.put(L_rstRSSET.getString("CMT_CODCD"),L_rstRSSET.getString("CMT_CHP01"));
				}
				L_rstRSSET.close();
			}
		}
		catch(Exception e)
		{setMSG(e,"While retrieving Tax details : "+M_strSQLQRY );}
	}
	private boolean genPORNO()
	{
		try
		{
			String L_strPORNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01 ="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_NCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"POR' and ";
			M_strSQLQRY += " CMT_SHRDS = '" + cmbPOSRL.getSelectedItem().toString()+ "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),"");
				}
				M_rstRSSET.close();
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
			txtPORNO.setText(L_strPORNO);
			txtAMDNO.setText("00");
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genPORNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
	}
	boolean insRMMST(String P_strTRNTP,String P_strREMTP,String P_strREMDS)
	{
				
		String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
		try
		{
			pstmINSRMK.setString(1,cl_dat.M_strCMPCD_pbst);
			pstmINSRMK.setString(2,M_strSBSCD);
			pstmINSRMK.setString(3,M_strSBSCD.substring(2,4));
			pstmINSRMK.setString(4,P_strTRNTP);
			pstmINSRMK.setString(5,P_strTRNTP);
			pstmINSRMK.setString(6,P_strREMTP);
			pstmINSRMK.setString(7,txtPORNO.getText().trim());
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
	@SuppressWarnings("unchecked") private boolean crtCOTAX(String P_strPORNO)
	{
		//try
	//	{
			M_strSQLQRY ="";
			Hashtable L_hstTXCT1=new Hashtable(10,0.2f);
			Vector L_vtrTXCT2=new Vector(10,2);
		for(int i=0;i<tblCOTAX.getRowCount();i++)
		{
			//COLLECTING DOCUMENT LEVEL COMMON TAX DETAILS 
			String L_strTEMP=nvlSTRVL(tblCOTAX.getValueAt(i,1).toString(),"");
			if((tblCOTAX.getValueAt(i,0) ==Boolean.TRUE)&&(L_strTEMP.length()>0))
			{
				if(hstTXCAT.get(L_strTEMP).toString().equals("01"))//FOR CO_TXDOC
				{
					if(tblCOTAX.getValueAt(i,1).toString().length() >0)
						L_hstTXCT1.put(L_strTEMP,tblCOTAX.getValueAt(i,4).toString()+",'"+tblCOTAX.getValueAt(i,3).toString().toUpperCase()+"',");
				}
				else//FOR CO_TXSPC
				{
					if(tblCOTAX.getValueAt(i,1).toString().length() >0)
						L_vtrTXCT2.addElement("'"+L_strTEMP+"','"+tblCOTAX.getValueAt(i,2).toString()+"','XXXXXXXXXX',"+tblCOTAX.getValueAt(i,4).toString()+(tblCOTAX.getValueAt(i,3).toString().equals("P") ? ",'P','" : ",'A','")+tblCOTAX.getValueAt(i,5).toString()+"',");
				}
			}
		}
		if(L_hstTXCT1.size()>0)
		{
			M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_AMDNO,TX_PRDCD,TX_SBSCD,TX_TRNTP,TX_DSBVL,TX_DSBFL,TX_EXCVL,TX_EXCFL,TX_PNFVL,TX_PNFFL,TX_CSTVL,TX_CSTFL,TX_STXVL,TX_STXFL,TX_STXDS,TX_OCTVL,TX_OCTFL,TX_FRTVL,TX_FRTFL,TX_INSVL,TX_INSFL,TX_CDSVL,TX_CDSFL,TX_INCVL,TX_INCFL,TX_ENCVL,TX_ENCFL,TX_FNIVL,TX_FNIFL,TX_CDUVL,TX_CDUFL,TX_CLRVL,TX_CLRFL,TX_OTHVL,TX_OTHFL,TX_OTHDS,TX_RSTVL,TX_RSTFL,"+
						"TX_CVDVL,TX_CVDFL,TX_EDCVL,TX_EDCFL,TX_EHCVL,TX_EHCFL,TX_SCHVL,TX_SCHFL,TX_VATVL,TX_VATFL,TX_WCTVL,TX_WCTFL,TX_TRNFL,TX_STSFL,TX_LUSBY,TX_LUPDT) values("
				+"'"+cl_dat.M_strCMPCD_pbst +"',"//TX_SBSTP
				+"'MM',"//TX_SYSCD
				+"'"+M_strSBSCD.substring(2,4) +"',"//TX_SBSTP
				+"'POR',"//TX_DOCTP
				+"'"+P_strPORNO+"',"//TX_DOCNO
                +"'"+nvlSTRVL(txtAMDNO.getText().trim(),"00")+"',"//TX_AMDNO
				+"'XXXXXXXXXX',"//TX_PRDCD
				+"'"+M_strSBSCD+"',"//TX_SBSCD
				+"'M',"//TX_TRNTP
				+(L_hstTXCT1.containsKey("DSB")==true ? L_hstTXCT1.get("DSB").toString() : "0,'',")//TX_DSBVL//TX_DSBFL
				+(L_hstTXCT1.containsKey("EXC")==true ? L_hstTXCT1.get("EXC").toString() : "0,'',")//TX_EXCVL//TX_EXCFL
				+(L_hstTXCT1.containsKey("PNF")==true ? L_hstTXCT1.get("PNF").toString() : "0,'',")//TX_PNFVL//TX_PNFFL
				+(L_hstTXCT1.containsKey("CST")==true ? L_hstTXCT1.get("CST").toString() : "0,'',")//TX_CSTVL//TX_CSTFL
				+(L_hstTXCT1.containsKey("STX")==true ? L_hstTXCT1.get("STX").toString() : "0,'',")//TX_STXVL//TX_STXFL;
				+"'"+strSTXDS+"',"
				+(L_hstTXCT1.containsKey("OCT")==true ? L_hstTXCT1.get("OCT").toString() : "0,'',")//TX_OCTVL//TX_OCTFL
				+(L_hstTXCT1.containsKey("FRT")==true ? L_hstTXCT1.get("FRT").toString() : "0,'',")//TX_FRTVL//TX_FRTFL
				+(L_hstTXCT1.containsKey("INS")==true ? L_hstTXCT1.get("INS").toString() : "0,'',")//TX_INSVL//TX_INSFL
				+(L_hstTXCT1.containsKey("CDS")==true ? L_hstTXCT1.get("CDS").toString() : "0,'',")//TX_CDSVL//TX_CDSFL
				+(L_hstTXCT1.containsKey("INC")==true ? L_hstTXCT1.get("INC").toString() : "0,'',")//TX_INCVL//TX_INCFL
				+(L_hstTXCT1.containsKey("ENC")==true ? L_hstTXCT1.get("ENC").toString() : "0,'',")//TX_ENCVL//TX_ENCFL
				+(L_hstTXCT1.containsKey("FNI")==true ? L_hstTXCT1.get("FNI").toString() : "0,'',")//TX_FNIVL//TX_FNIFL
				+(L_hstTXCT1.containsKey("CDU")==true ? L_hstTXCT1.get("CDU").toString() : "0,'',")//TX_CDUVL//TX_CDUFL
				+(L_hstTXCT1.containsKey("CLR")==true ? L_hstTXCT1.get("CLR").toString() : "0,'',")//TX_CLRVL//TX_CLRFL
				+(L_hstTXCT1.containsKey("OTH")==true ? L_hstTXCT1.get("OTH").toString() : "0,'',")//TX_OTHVL//TX_OTHFL;
				+"'"+strOTHDS+"',"
				+(L_hstTXCT1.containsKey("RST")==true ? L_hstTXCT1.get("RST").toString() : "0,'',")//TX_rstVL//TX_RSTFL
				+(L_hstTXCT1.containsKey("CVD")==true ? L_hstTXCT1.get("CVD").toString() : "0,'',")//TX_CVDVL//TX_CVDFL
				+(L_hstTXCT1.containsKey("EDC")==true ? L_hstTXCT1.get("EDC").toString() : "0,'',")//TX_EDCVL//TX_EDCFL		
				+(L_hstTXCT1.containsKey("EHC")==true ? L_hstTXCT1.get("EHC").toString() : "0,'',")//TX_EHCVL//TX_EHCFL		
				+(L_hstTXCT1.containsKey("SCH")==true ? L_hstTXCT1.get("SCH").toString() : "0,'',")//TX_SCHVL//TX_SCHFL		
				+(L_hstTXCT1.containsKey("VAT")==true ? L_hstTXCT1.get("VAT").toString() : "0,'',")//TX_VATVL//TX_VATFL				
				+(L_hstTXCT1.containsKey("WCT")==true ? L_hstTXCT1.get("WCT").toString() : "0,'',")//TX_WCTVL//TX_WCTFL		
				+getUSGDTL("TX",'I',"")+")";
			if(M_strSQLQRY.trim().length() >0)
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
	// creating grdaewise taxes
		M_strSQLQRY ="";
		if(L_hstTXCT1 !=null)
			L_hstTXCT1.clear();
		L_hstTXCT1=new Hashtable(10,0.2f);
		for(int i=0;i<tblITTAX.getRowCount();i++)
		{//GETHERING INFORMATIOJN ABT GRADE WISE MASTER TAXES
			String L_strTEMP=nvlSTRVL(tblITTAX.getValueAt(i,2).toString(),"");
			if((tblITTAX.getValueAt(i,0)==Boolean.TRUE)&&(L_strTEMP.length()>0))
			{
				String L_strPRDCD=tblITTAX.getValueAt(i,1).toString();
				for(int j=0;j<tblITDTL.getRowCount();j++)
				{
					if(nvlSTRVL(tblITDTL.getValueAt(j,TB1_MATCD).toString(),"").length()>0)
						if(tblITDTL.getValueAt(j,TB1_MATCD).toString().equals(L_strPRDCD))
						{
							L_strPRDCD=tblITDTL.getValueAt(j,TB1_MATCD).toString();
							break;
						}
				}
				if(hstTXCAT.get(L_strTEMP).toString().equals("01"))
				{
					if(tblITTAX.getValueAt(i,2).toString().trim().equals("STX"))
					   strGTXDS = tblITTAX.getValueAt(i,3).toString();
					else
						strGTXDS ="";
					if(tblITTAX.getValueAt(i,2).toString().trim().equals("OTH"))
					   strGOTDS = tblITTAX.getValueAt(i,3).toString();
					else
						strGOTDS ="";
					if(!L_hstTXCT1.containsKey(L_strPRDCD))
					 {
						 Hashtable hst=new Hashtable(5,0.2f);
						if(tblITTAX.getValueAt(i,2).toString().trim().equals("STX"))
							hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+",'"+(tblITTAX.getValueAt(i,4).toString().toUpperCase()+"','"+strGTXDS+"',"));
						else if(tblITTAX.getValueAt(i,2).toString().trim().equals("OTH"))
							hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+",'"+(tblITTAX.getValueAt(i,4).toString().toUpperCase()+"','"+strGOTDS+"',"));
						else
							hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+",'"+(tblITTAX.getValueAt(i,4).toString().toUpperCase()+"',"));
						 L_hstTXCT1.put(L_strPRDCD,hst);
					 }
					else
					{
						if(tblITTAX.getValueAt(i,2).toString().trim().equals("STX"))
							((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','"+strGTXDS+"'," : ",'A','"+strGTXDS+"',"));
						else if(tblITTAX.getValueAt(i,2).toString().trim().equals("OTH"))
							((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','"+strGOTDS+"'," : ",'A','"+strGOTDS+"',"));
						else
							((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P'," : ",'A',"));
					}
				}
				else
				{
					// test afterwards 
					//	if(Float.parseFloat(tblITTAX.getValueAt(i,3).toString())>0.0f)
					if(tblITTAX.getValueAt(i,2).toString().length() >0)
					L_vtrTXCT2.addElement("'"+L_strTEMP+"','"+tblITTAX.getValueAt(i,3).toString()+"','"+L_strPRDCD+"',"+tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','" : ",'A','")+tblITTAX.getValueAt(i,6).toString()+"',");
				}
			}
		}
		if(L_hstTXCT1.size()>0)
		{
			int cnt =1;
			Enumeration L_enmTXKEYS=L_hstTXCT1.keys();
			while(L_enmTXKEYS.hasMoreElements())
			{
				M_strSQLQRY="";
				String L_strGRDCD=(String)L_enmTXKEYS.nextElement();
				Hashtable L_hstGRDTX=(Hashtable)L_hstTXCT1.get(L_strGRDCD);
				M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_AMDNO,TX_PRDCD,TX_SBSCD,TX_TRNTP,"+
							"TX_DSBVL,TX_DSBFL,TX_EXCVL,TX_EXCFL,TX_PNFVL,TX_PNFFL,TX_CSTVL,TX_CSTFL,TX_STXVL,TX_STXFL,TX_STXDS,TX_OCTVL,TX_OCTFL,TX_FRTVL,TX_FRTFL,TX_INSVL,TX_INSFL,TX_CDSVL,TX_CDSFL,TX_INCVL,TX_INCFL,TX_ENCVL,TX_ENCFL,TX_FNIVL,TX_FNIFL,TX_CDUVL,TX_CDUFL,TX_CLRVL,TX_CLRFL,TX_OTHVL,TX_OTHFL,TX_OTHDS,TX_RSTVL,TX_RSTFL,"+
							"TX_CVDVL,TX_CVDFL,TX_EDCVL,TX_EDCFL,TX_EHCVL,TX_EHCFL,TX_SCHVL,TX_SCHFL,TX_VATVL,TX_VATFL,TX_WCTVL,TX_WCTFL,TX_TRNFL,TX_STSFL,TX_LUSBY,TX_LUPDT) values("
					+"'"+cl_dat.M_strCMPCD_pbst +"',"//TX_SBSTP
					+"'MM',"//TX_SYSCD
					+"'"+M_strSBSCD.substring(2,4) +"',"//TX_SBSTP
					+"'POR',"//TX_DOCTP
					+"'"+P_strPORNO+"',"//TX_DOCNO
					+"'"+nvlSTRVL(txtAMDNO.getText().trim(),"00")+"',"//TX_AMDNO
					+"'"+L_strGRDCD+"',"//TX_PRDCD
					+"'"+M_strSBSCD+"',"//TX_SBSCD
					+"'M',"//TX_TRNTP
					+(L_hstGRDTX.containsKey("DSB")==true ? L_hstGRDTX.get("DSB").toString() : "0,'',")//TX_DSBVL//TX_DSBFL
					+(L_hstGRDTX.containsKey("EXC")==true ? L_hstGRDTX.get("EXC").toString() : "0,'',")//TX_EXCVL//TX_EXCFL
					+(L_hstGRDTX.containsKey("PNF")==true ? L_hstGRDTX.get("PNF").toString() : "0,'',")//TX_PNFVL//TX_PNFFL
					+(L_hstGRDTX.containsKey("CST")==true ? L_hstGRDTX.get("CST").toString() : "0,'',")//TX_CSTVL//TX_CSTFL
					+(L_hstGRDTX.containsKey("STX")==true ? L_hstGRDTX.get("STX").toString() : "0,'','',")//TX_STXVL//TX_STXFL//TX_STXDS
					+(L_hstGRDTX.containsKey("OCT")==true ? L_hstGRDTX.get("OCT").toString() : "0,'',")//TX_OCTVL//TX_OCTFL
					+(L_hstGRDTX.containsKey("FRT")==true ? L_hstGRDTX.get("FRT").toString() : "0,'',")//TX_FRTVL//TX_FRTFL
					+(L_hstGRDTX.containsKey("INS")==true ? L_hstGRDTX.get("INS").toString() : "0,'',")//TX_INSVL//TX_INSFL
					+(L_hstGRDTX.containsKey("CDS")==true ? L_hstGRDTX.get("CDS").toString() : "0,'',")//TX_CDSVL//TX_CDSFL
					+(L_hstGRDTX.containsKey("INC")==true ? L_hstGRDTX.get("INC").toString() : "0,'',")//TX_INCVL//TX_INCFL
					+(L_hstGRDTX.containsKey("ENC")==true ? L_hstGRDTX.get("ENC").toString() : "0,'',")//TX_ENCVL//TX_ENCFL
					+(L_hstGRDTX.containsKey("FNI")==true ? L_hstGRDTX.get("FNI").toString() : "0,'',")//TX_FNIVL//TX_FNIFL
					+(L_hstGRDTX.containsKey("CDU")==true ? L_hstGRDTX.get("CDU").toString() : "0,'',")//TX_CDUVL//TX_CDUFL
					+(L_hstGRDTX.containsKey("CLR")==true ? L_hstGRDTX.get("CLR").toString() : "0,'',")//TX_CLRVL//TX_CLRFL
					+(L_hstGRDTX.containsKey("OTH")==true ? L_hstGRDTX.get("OTH").toString() : "0,'','',")//TX_OTHVL//TX_OTHFL//TX_OTHDS		
					+(L_hstGRDTX.containsKey("RST")==true ? L_hstGRDTX.get("RST").toString() : "0,'',")//TX_RSTVL//TX_RSTFL
					+(L_hstGRDTX.containsKey("CVD")==true ? L_hstGRDTX.get("CVD").toString() : "0,'',")//TX_CVDVL//TX_CVDFL
					+(L_hstGRDTX.containsKey("EDC")==true ? L_hstGRDTX.get("EDC").toString() : "0,'',")//TX_EDCVL//TX_EDCFL
					+(L_hstGRDTX.containsKey("EHC")==true ? L_hstGRDTX.get("EHC").toString() : "0,'',")//TX_EDCVL//TX_EDCFL
					+(L_hstGRDTX.containsKey("SCH")==true ? L_hstGRDTX.get("SCH").toString() : "0,'',")//TX_SCHVL//TX_SCHFL
					+(L_hstGRDTX.containsKey("VAT")==true ? L_hstGRDTX.get("VAT").toString() : "0,'',")//TX_VATVL//TX_VATFL
					+(L_hstGRDTX.containsKey("WCT")==true ? L_hstGRDTX.get("WCT").toString() : "0,'',")//TX_WCTVL//TX_WCTFL
							
					+getUSGDTL("TX",'I',"")+")";
					cnt++;							
					if(M_strSQLQRY.trim().length() >0)
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			
		}
		for(int i=0;i<L_vtrTXCT2.size();i++)
		{
			M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_CODCD,"+
				"TXT_CODDS,TXT_PRDCD,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,"+
				"TXT_TRNFL,TXT_STSFL,TXT_LUSBY,TXT_LUPDT) values ("
				+"'"+cl_dat.M_strCMPCD_pbst+"','MM',"//TXT_SYSCD,
				+"'"+M_strSBSCD.substring(2,4) +"',"//TX_SBSTP
				+"'POR',"//TXT_DOCTP,
				+"'"+P_strPORNO+"',"//TXT_DOCNO,
				+L_vtrTXCT2.elementAt(i).toString()//TXT_CODCD,
				//TXT_CODDS,//TXT_PRDCD,//TXT_CODVL,//TXT_CODFL,//TXT_PRCSQ,
				+"'"+M_strSBSCD+"',"//TXT_SBSCD,
				+getUSGDTL("TXT",'I',"")+")"; //TXT_LUPDT
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
	/*	}
	//	catch(Exception L_E)
	//	{
	//		
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG("Error in Saving Tax Details..",'E');
		    cl_dat.M_btnSAVE_pbst.setEnabled(true);
			return false;
		}*/
		return true;
	}
	private boolean vldVENCD(String P_strVENCD)
	{
		try
		{
			String L_strPRTNM="";
			int L_intDAYS =0;
			M_strSQLQRY ="SELECT PT_PRTCD,PT_PRTNM,(days(current_date)-days(PT_EVLDT))L_DAYS FROM CO_PTMST WHERE PT_PRTTP ='S' and PT_PRTCD ='"+P_strVENCD.trim().toUpperCase() +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			txtVENCD.setText(txtVENCD.getText().toUpperCase());
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					strPRTNM = L_strPRTNM;
				//	txtVENNM.setText(L_strPRTNM);
					L_intDAYS = M_rstRSSET.getInt("L_DAYS");
					//return true;
				}
				M_rstRSSET.close();
			}
		//	M_strSQLQRY ="SELECT MAX(CTP_LPODT),days(current_date)-days(MAX(CTP_LPODT))L_DAYS FROM CO_CTPTR WHERE CTP_PRTTP ='S' and CTP_PRTCD ='"+P_strVENCD.trim() +"'";
//Earlier Query			M_strSQLQRY ="SELECT MAX(PO_PORDT)L_PORDT,days(current_date)-days(MAX(PO_PORDT))L_DAYS FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'') <>'X' and PO_VENCD ='"+P_strVENCD.trim() +"'";
			//Removed company code by Vijay on 09/03/2009
			M_strSQLQRY ="SELECT MAX(PO_PORDT)L_PORDT,days(current_date)-days(MAX(PO_PORDT))L_DAYS FROM MM_POMST WHERE isnull(PO_STSFL,'') <>'X' and PO_VENCD ='"+P_strVENCD.trim() +"'";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			java.sql.Date datTEMP;
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					datTEMP = M_rstRSSET.getDate("L_PORDT");
					if(datTEMP == null)
					{
						if(L_intDAYS > 730)
						{
							JOptionPane.showMessageDialog(this,"No order is placed,Please Approve the vendor","Vendor",JOptionPane.INFORMATION_MESSAGE);
							return false;
						}
					}
					if(M_rstRSSET.getInt("L_DAYS")>730)
					{
						if(L_intDAYS > 730)
						{
							JOptionPane.showMessageDialog(this,"No order is placed for last two years, Re-evaluate the Vendor..","Vendor",JOptionPane.INFORMATION_MESSAGE);
							setMSG("No Order has been Placed for last 2 years ..",'E');
						}
						//return false;
					}
					//System.out.println(L_intDAYS);
				}
				else
				{
					setMSG("Last Order date not found for checking..",'E');
				}
				M_rstRSSET.close();
			}
			
			vtrGRPCD = new Vector<String>();
			// MATERIAL Group validation is applicable for Groups other than
			// 47_49_60_62_85_86_95_98_99
			// To skip these codes from validation, theses are added in Vector<String> of group codes for all vendors.
			String[] L_strGRPCD = new String[]{"47","49","60","62","85","86","95","98","99"};
			for (int i=0;i<L_strGRPCD.length;i++)
				vtrGRPCD.addElement(L_strGRPCD[i]);
			M_strSQLQRY ="SELECT AV_GRPCD FROM MM_AVMST WHERE  AV_PRTTP ='S' and AV_PRTCD ='"+P_strVENCD.trim() +"' and isnull(AV_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
					vtrGRPCD.addElement(M_rstRSSET.getString("AV_GRPCD"));
				M_rstRSSET.close();
			}
			return true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldPRTCD");
			return false;
		}	
		
	}
	private void dspCOTAX(ResultSet P_rstRSSET,String P_strCOLNM) throws Exception
	{
	String L_strTEMP1;
	
		if(P_rstRSSET.getString(P_strCOLNM)!=null)
		{
			int i=0;
			for(i=0;i<tblCOTAX.getRowCount();i++)
				if(tblCOTAX.getValueAt(i,1).toString().length()==0)
					break;
	
			//if(Float.parseFloat(nvlSTRVL(P_rstRSSET.getString(P_strCOLNM),"0.0f")))
			//{
				strTEMP = nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),"");
				if(strTEMP.length() >0)
			    {
					tblCOTAX.setValueAt(P_strCOLNM.substring(3,P_strCOLNM.length()-2),i,1);
					tblCOTAX.setValueAt(P_rstRSSET.getString(P_strCOLNM),i,4);
					tblCOTAX.setValueAt(strTEMP,i,3);
					strTEMP = nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),"");
					if((P_strCOLNM.equals("TX_STXVL"))||(P_strCOLNM.equals("TX_OTHVL")))
					{
						tblCOTAX.setValueAt(nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"DS"),""),i,2);
					}
					else
						tblCOTAX.setValueAt(cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXTAX",tblCOTAX.getValueAt(i,1).toString()),i,2);
				}
			//}
				intCTXRW =i;
		}
	}
	
	private void dspGRTAX(ResultSet P_rstRSSET,String P_strCOLNM)throws Exception
	{
		if(P_rstRSSET.getString(P_strCOLNM)!=null)
		{
			int i=0;
			for(i=0;i<tblITTAX.getRowCount();i++)
				if(tblITTAX.getValueAt(i,1).toString().length()==0)
					break;
			strTEMP = nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),"");
			if(strTEMP.length() >0)
			{
				tblITTAX.setValueAt(nvlSTRVL(P_rstRSSET.getString("TX_PRDCD"),""),intGTXRW,1);
				tblITTAX.setValueAt(P_strCOLNM.substring(3,6),intGTXRW,2);
				tblITTAX.setValueAt(P_rstRSSET.getString(P_strCOLNM),intGTXRW,5);
				if((P_strCOLNM.equals("TX_STXVL"))||(P_strCOLNM.equals("TX_OTHVL")))
				{
					tblITTAX.setValueAt(nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"DS"),""),intGTXRW,3);
				}
				else
					tblITTAX.setValueAt(cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXTAX",tblITTAX.getValueAt(intGTXRW,2).toString()),intGTXRW,3);
				tblITTAX.setValueAt(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),intGTXRW,4);
				intGTXRW++;
			}
		}
		
	}
	private void delPODTL()
	{
		try
		{
			int L_intSELCT =0;  // Items selected
			int L_intITMCT =0;  // Totl Items
			cl_dat.M_flgLCUPD_pbst = true;
			for(int i=0;i<tblITDTL.getRowCount();i++)
			{
				if(tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
				{
					L_intITMCT++; // No. Of items in the table
					if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
					}
				}
			}
			if(L_intSELCT == L_intITMCT)  //"ALL Items are selected"
			{
				// Cancel all items of P.O
				M_strSQLQRY = "";
				M_strSQLQRY =  "UPDATE MM_POMST SET ";
				M_strSQLQRY += getUSGDTL("PO",'U',"X");
				M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
				M_strSQLQRY += "PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				M_strSQLQRY += "PO_PORNO = '"+txtPORNO.getText().trim()+"' AND isnull(PO_ACPQT,0) =0";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				// Cancel all deliver Schedule
				M_strSQLQRY = " UPDATE MM_PODEL SET ";
				M_strSQLQRY +=getUSGDTL("POD",'U',"X");
				M_strSQLQRY += " WHERE POD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND POD_MMSBS ='"+M_strSBSCD+"' AND ";
				M_strSQLQRY += "POD_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				M_strSQLQRY += "POD_PORNO = '"+txtPORNO.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				
				// Cancel all taxes Related of P.O
				M_strSQLQRY = " UPDATE CO_TXDOC SET ";
				M_strSQLQRY +=getUSGDTL("TX",'U',"X");
				M_strSQLQRY += " WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSCD ='"+M_strSBSCD+"' AND ";
				M_strSQLQRY += " TX_DOCTP = 'POR' ";
				M_strSQLQRY += " AND TX_SBSTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				M_strSQLQRY += "TX_DOCNO = '"+txtPORNO.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				
				M_strSQLQRY = "UPDATE MM_PORMK SET ";
				M_strSQLQRY += getUSGDTL("PO",'U',"X");
				M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
				M_strSQLQRY += "PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				M_strSQLQRY += "PO_PORNO = '"+txtPORNO.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY," ");
				
				M_strSQLQRY = "UPDATE MM_RMMST SET ";
				M_strSQLQRY += getUSGDTL("RM",'U',"X");
				M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
				M_strSQLQRY += " AND RM_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY += " AND RM_DOCTP ='"+strTRNTP +"'";
				M_strSQLQRY += " AND RM_TRNTP ='"+strTRNTP +"'";
				M_strSQLQRY += " AND RM_REMTP ='POR'";
				M_strSQLQRY += " AND RM_DOCNO ='"+txtPORNO.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY," ");
			}
			// Some item may be selected, or none may be seleted and only TAX
			String L_strSTSFL ="";
			if(L_intSELCT >0)
			{
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					L_strSTSFL ="X";
					if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY = " UPDATE MM_POMST SET PO_FRCQT = isnull(PO_PORQT,0)-isnull(PO_ACPQT,0),";
						M_strSQLQRY +=getUSGDTL("PO",'U',L_strSTSFL);
						M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "PO_PORNO = '"+txtPORNO.getText().trim()+"'";
						M_strSQLQRY += " AND PO_INDNO = '"+tblITDTL.getValueAt(i,TBL_INDNO).toString().trim()+"'";
						M_strSQLQRY += " AND PO_MATCD = '"+tblITDTL.getValueAt(i,TBL_MATCD).toString().trim()+"' and isnull(po_acpqt,0) =0";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						// Cancel all deliver Schedule
						M_strSQLQRY = " UPDATE MM_PODEL SET ";
						M_strSQLQRY +=getUSGDTL("POD",'U',"X");
						M_strSQLQRY += " WHERE POD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND POD_MMSBS ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "POD_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "POD_PORNO = '"+txtPORNO.getText().trim()+"'";
						M_strSQLQRY += " AND POD_INDNO = '"+tblITDTL.getValueAt(i,TBL_INDNO).toString().trim()+"'";
						M_strSQLQRY += " AND POD_MATCD = '"+tblITDTL.getValueAt(i,TBL_MATCD).toString().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"");
				
						// Cancel all taxes Related of P.O
						M_strSQLQRY = " UPDATE CO_TXDOC SET ";
						M_strSQLQRY +=getUSGDTL("TX",'U',"X");
						M_strSQLQRY += " WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSCD ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "TX_DOCTP = 'POR' ";
						M_strSQLQRY += " AND TX_SBSTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "TX_DOCNO = '"+txtPORNO.getText().trim()+"'";
						M_strSQLQRY += " AND TX_PRDCD = '"+tblITDTL.getValueAt(i,TBL_MATCD).toString().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"");
							
					}// end if condition
				} // end For loop
			}
			if(cl_dat.exeDBCMT("delPODTL"))
		    {
				setMSG("Data has been deleted Suceesfully..",'N');
				clrCOMP();
				flgAMDFL = false;
			}
			else
			{
				setMSG("Error in deleting the data..",'E');
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"delPODTL");
		}
		finally
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
		}
	}
	private boolean insPODTL(int i)
	{
		try
		{
			pstmINSREC.setString(1,cl_dat.M_strCMPCD_pbst);
			pstmINSREC.setString(2,M_strSBSCD);
			pstmINSREC.setString(3,M_strSBSCD.substring(2,4));
			pstmINSREC.setString(4,cmbPORTP.getSelectedItem().toString().substring(0,2));
			pstmINSREC.setString(5,txtPORNO.getText().trim());
			pstmINSREC.setString(6,tblITDTL.getValueAt(i,TBL_MATCD).toString());
			pstmINSREC.setString(7,tblITDTL.getValueAt(i,TBL_MATDS).toString().toUpperCase());
			pstmINSREC.setString(8,M_strSBSCD.substring(2,4));
		//	if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
		//		pstmINSREC.setString(8,strINDNO);
		//	else
			pstmINSREC.setString(9,tblITDTL.getValueAt(i,TBL_INDNO).toString());
			strTEMP = txtORDDT.getText().trim().substring(6,10) + "-" +  txtORDDT.getText().trim().substring(3,5) + "-" +  txtORDDT.getText().trim().substring(0,2);
			pstmINSREC.setDate(10,Date.valueOf(strTEMP));
			pstmINSREC.setString(11,txtAMDNO.getText().trim());
			pstmINSREC.setString(12,txtSHRDS.getText().trim().toUpperCase());
			pstmINSREC.setString(13,txtBYRCD.getText().trim());
			strTEMP = txtEFFDT.getText().trim().substring(6,10) + "-" +  txtEFFDT.getText().trim().substring(3,5) + "-" +  txtEFFDT.getText().trim().substring(0,2);
			pstmINSREC.setDate(14,Date.valueOf(strTEMP));
			strTEMP = txtCMPDT.getText().trim().substring(6,10) + "-" +  txtCMPDT.getText().trim().substring(3,5) + "-" +  txtCMPDT.getText().trim().substring(0,2);
			pstmINSREC.setDate(15,Date.valueOf(strTEMP));
			pstmINSREC.setString(16,"S");// VEntp
			pstmINSREC.setString(17,txtVENCD.getText().trim().toUpperCase());
			pstmINSREC.setString(18,txtCURCD.getText().trim());
			pstmINSREC.setFloat(19,Float.valueOf(txtEXGRT.getText().trim()).floatValue());
		//	L_PORVL =0;
			pstmINSREC.setFloat(20,0);//porvl
			pstmINSREC.setString(21,txtQTNTP.getText().trim());
			pstmINSREC.setString(22,txtQTNNO.getText().trim());
			if(txtQTNDT.getText().trim().length() ==10)
			{
				strTEMP = txtQTNDT.getText().trim().substring(6,10) + "-" +  txtQTNDT.getText().trim().substring(3,5) + "-" +  txtQTNDT.getText().trim().substring(0,2);
				pstmINSREC.setDate(23,Date.valueOf(strTEMP));
			}
			else
				pstmINSREC.setDate(23,null);
			pstmINSREC.setString(24,cl_dat.M_strUSRCD_pbst);
			strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
			pstmINSREC.setDate(25,Date.valueOf(strTEMP));
			pstmINSREC.setString(26,cmbDELTP.getSelectedItem().toString().substring(0,2));
			pstmINSREC.setString(27,cmbDSTCD.getSelectedItem().toString().substring(0,3));
			pstmINSREC.setString(28,tblITDTL.getValueAt(i,TBL_POUOM).toString());
			pstmINSREC.setFloat(29,Float.valueOf(tblITDTL.getValueAt(i,TBL_CNVFT).toString()).floatValue());
			pstmINSREC.setFloat(30,Float.valueOf(tblITDTL.getValueAt(i,TBL_PORRT).toString()).floatValue());
			pstmINSREC.setFloat(31,Float.valueOf(tblITDTL.getValueAt(i,TBL_PERRT).toString()).floatValue());
			pstmINSREC.setFloat(32,Float.valueOf(tblITDTL.getValueAt(i,TBL_ORDQT).toString()).floatValue());
			pstmINSREC.setFloat(33,Float.valueOf("1").floatValue()); // del. count
			//L_PORVL = Float.valueOf(tblITDTL.getValueAt(i,TBL_ORDQT).toString()).floatValue()* Float.valueOf(tblITDTL.getValueAt(i,TBL_PORRT).toString()).floatValue(); // to be calculated
			pstmINSREC.setFloat(34,0); 
			if(tblITDTL.getValueAt(i,TBL_TCFFL).toString().equals("true"))
				pstmINSREC.setString(35,"Y");
			else
				pstmINSREC.setString(35,"N");
			if(tblITDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
				pstmINSREC.setString(36,"Y");
			else
				pstmINSREC.setString(36,"N");
		/*	if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
			{
				pstmINSREC.setString(36,strDPTCD.substring(0,3));
			}
			else*/
			pstmINSREC.setString(37,tblITDTL.getValueAt(i,TBL_DPTCD).toString().substring(0,3));
			pstmINSREC.setString(38,txtPMTRF.getText().trim().toUpperCase());
			pstmINSREC.setString(39,txtCRDDY.getText().trim());
			pstmINSREC.setString(40,"0");
			pstmINSREC.setString(41,"O"); // FRESH ORDER ,ORDERED
			pstmINSREC.setString(42,cl_dat.M_strUSRCD_pbst);
			strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
			pstmINSREC.setDate(43,Date.valueOf(strTEMP));
			pstmINSREC.executeUpdate();
		}
		catch(Exception L_E)
		{
			setMSG("Error in insPODTL",'E');
			//System.out.println(L_E.toString());
			return false;
		}
		return true;
	}
	private void updPODTL()
	{
		try
		{
			String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
		String L_strPORBY,L_strEXPDT,L_strREQDT,L_strPREDTM;
			int intSELROW =0;
			cl_dat.M_flgLCUPD_pbst = true;
			for(int i=0;i<tblITDTL.getRowCount();i++)
			{
				if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(vtrINMAT.contains((String)tblITDTL.getValueAt(i,TBL_INDNO).toString()+tblITDTL.getValueAt(i,TBL_MATCD).toString()))
				    {
						/*if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
						{
							// if updating P.O. quantity update the indent quantity
							M_strSQLQRY ="UPDATE MM_INMST SET IN_INDQT ="+tblITDTL.getValueAt(i,TBL_ORDQT).toString();
							M_strSQLQRY +=",IN_AUTQT ="+tblITDTL.getValueAt(i,TBL_ORDQT).toString();
							M_strSQLQRY +=" WHERE IN_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
							M_strSQLQRY += " AND IN_INDNO ='"+strINDNO +"'";
							M_strSQLQRY += " AND IN_MATCD ='"+tblITDTL.getValueAt(i,TBL_MATCD).toString().toUpperCase() +"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}*/
						pstmUPDREC.setString(1,txtSHRDS.getText().trim().toUpperCase());
						strTEMP = txtEFFDT.getText().trim().substring(6,10) + "-" +  txtEFFDT.getText().trim().substring(3,5) + "-" +  txtEFFDT.getText().trim().substring(0,2);
						pstmUPDREC.setDate(2,Date.valueOf(strTEMP));
						strTEMP = txtCMPDT.getText().trim().substring(6,10) + "-" +  txtCMPDT.getText().trim().substring(3,5) + "-" +  txtCMPDT.getText().trim().substring(0,2);
						pstmUPDREC.setDate(3,Date.valueOf(strTEMP));
						pstmUPDREC.setString(4,txtVENCD.getText().trim().toUpperCase());
						pstmUPDREC.setString(5,txtCURCD.getText().trim());
						pstmUPDREC.setFloat(6,Float.valueOf(txtEXGRT.getText().trim()).floatValue());
						pstmUPDREC.setString(7,txtQTNTP.getText().trim());
						pstmUPDREC.setString(8,txtQTNNO.getText().trim());
						if(txtQTNDT.getText().trim().length() ==10)
						{
							strTEMP = txtQTNDT.getText().trim().substring(6,10) + "-" +  txtQTNDT.getText().trim().substring(3,5) + "-" +  txtQTNDT.getText().trim().substring(0,2);
							pstmUPDREC.setDate(9,Date.valueOf(strTEMP));
						}
						else
							pstmUPDREC.setDate(9,null);
						pstmUPDREC.setString(10,cmbDELTP.getSelectedItem().toString().substring(0,2));
						pstmUPDREC.setString(11,cmbDSTCD.getSelectedItem().toString().substring(0,3));
						pstmUPDREC.setString(12,tblITDTL.getValueAt(i,TBL_POUOM).toString());
						pstmUPDREC.setFloat(13,Float.valueOf(tblITDTL.getValueAt(i,TBL_CNVFT).toString()).floatValue());
						pstmUPDREC.setFloat(14,Float.valueOf(tblITDTL.getValueAt(i,TBL_PORRT).toString()).floatValue());
						pstmUPDREC.setFloat(15,Float.valueOf(tblITDTL.getValueAt(i,TBL_PERRT).toString()).floatValue());
						pstmUPDREC.setFloat(16,Float.valueOf(tblITDTL.getValueAt(i,TBL_ORDQT).toString()).floatValue());
						if(tblITDTL.getValueAt(i,TBL_TCFFL).toString().equals("true"))
							pstmUPDREC.setString(17,"Y");
						else
							pstmUPDREC.setString(17,"N");
						if(tblITDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
							pstmUPDREC.setString(18,"Y");
						else
							pstmUPDREC.setString(18,"N");
						pstmUPDREC.setString(19,tblITDTL.getValueAt(i,TBL_DPTCD).toString().substring(0,3));
						pstmUPDREC.setString(20,txtPMTRF.getText().trim().toUpperCase());
						pstmUPDREC.setString(21,txtCRDDY.getText().trim());
						pstmUPDREC.setString(22,"0");
						pstmUPDREC.setString(23,cl_dat.M_strUSRCD_pbst);
						strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
						pstmUPDREC.setDate(24,Date.valueOf(strTEMP));
						pstmUPDREC.setString(25,tblITDTL.getValueAt(i,TBL_MATDS).toString());
						pstmUPDREC.setString(26,txtAMDNO.getText().trim());		
						if(flgAMDFL)
						{
							pstmUPDREC.setString(27,"O");
						}
						else
						pstmUPDREC.setString(27,tblITDTL.getValueAt(i,TBL_STSFL).toString());
						pstmUPDREC.setString(28,cl_dat.M_strCMPCD_pbst);
						pstmUPDREC.setString(29,M_strSBSCD);
						pstmUPDREC.setString(30,M_strSBSCD.substring(2,4));
						pstmUPDREC.setString(31,txtPORNO.getText().trim());
						pstmUPDREC.setString(32,tblITDTL.getValueAt(i,TBL_INDNO).toString());
						pstmUPDREC.setString(33,tblITDTL.getValueAt(i,TBL_MATCD).toString());
						pstmUPDREC.executeUpdate();
					}
					else
					{
			
						/*if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
						{
							L_strPREDTM = cl_dat.M_txtCLKDT_pbst.getText().substring(6,10) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(3,5) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(0,2) + " "+cl_dat.M_txtCLKTM_pbst.getText().trim() + ":00.000000000";
							L_strREQDT = null;
							L_strEXPDT = null;
							L_strPORBY = null;
							L_strREQDT = txtCMPDT.getText().trim().substring(6,10) + "-" + txtCMPDT.getText().trim().substring(3,5) + "-" + txtCMPDT.getText().trim().substring(0,2);
							L_strEXPDT = L_strREQDT;
							L_strPORBY = cl_dat.M_strLOGDT_pbst.trim().substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
							pstmINSIND.setString(1,M_strSBSCD);
							pstmINSIND.setString(2,M_strSBSCD.substring(2,4));
							pstmINSIND.setString(3,strINDNO);
							pstmINSIND.setString(4,"00");
							pstmINSIND.setString(5,tblITDTL.getValueAt(i,TBL_MATCD).toString().toUpperCase());
							pstmINSIND.setDate(6,Date.valueOf(L_strDATE));
							pstmINSIND.setFloat(7,Float.valueOf(tblITDTL.getValueAt(i,TBL_ORDQT).toString()).floatValue());
							pstmINSIND.setFloat(8,0); // Auth Qty 0
							pstmINSIND.setString(9,"N"); // Urgency Tag Normal
							pstmINSIND.setString(10,"aaa");
							pstmINSIND.setString(11,cl_dat.M_strUSRCD_pbst);
							if(L_strPREDTM !=null)
								pstmINSIND.setTimestamp(12,Timestamp.valueOf(L_strPREDTM));
							else
								pstmINSIND.setTimestamp(12,null);
							if(L_strREQDT !=null)
								pstmINSIND.setDate(13,Date.valueOf(L_strREQDT));
							else
								pstmINSIND.setDate(13,null);
							if(L_strEXPDT !=null)
								pstmINSIND.setDate(14,Date.valueOf(L_strEXPDT));
							else
								pstmINSIND.setDate(14,null);
							if(L_strPORBY !=null)
								pstmINSIND.setDate(15,Date.valueOf(L_strPORBY));
							else
								pstmINSIND.setDate(15,null);
							if(tblITDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
								pstmINSIND.setString(16,"Y");
							else
								pstmINSIND.setString(16,"N");
							if(tblITDTL.getValueAt(i,TBL_TCFFL).toString().equals("true"))
								pstmINSIND.setString(17,"Y");
							else
								pstmINSIND.setString(17,"N");
							strTEMP = tblITDTL.getValueAt(i,TBL_ORDQT).toString();
							pstmINSIND.setFloat(18,0);  // Indent Value 0
							pstmINSIND.setString(19,"0");
							pstmINSIND.setString(20,"0");
							pstmINSIND.setString(21,cl_dat.M_strUSRCD_pbst);
							pstmINSIND.setDate(22,Date.valueOf(L_strDATE));
							pstmINSIND.setString(23,"");
							pstmINSIND.setString(24,"");
							pstmINSIND.setTimestamp(25,null);
							pstmINSIND.setString(26,"01");
							pstmINSIND.executeUpdate();
						
							M_strSQLQRY ="UPDATE MM_INMST SET IN_STSFL ='4',IN_AUTDT = current_timestamp,IN_AUTQT = IN_INDQT ";
							M_strSQLQRY +=" WHERE IN_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
							M_strSQLQRY += " AND IN_INDNO ='"+strINDNO +"'";
							M_strSQLQRY += " AND IN_MATCD ='"+tblITDTL.getValueAt(i,TBL_MATCD).toString().toUpperCase() +"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}*/
						insPODTL(i);
					}
					intSELROW++;
				}
			}
			// commented on 06/08/2004 for amendment
		//	if(intSELROW ==0)
		//	{
				// Update common details
				M_strSQLQRY = "UPDATE MM_POMST SET PO_SHRDS ='"+txtSHRDS.getText().trim() +"',"+
					          " PO_EFFDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtEFFDT.getText().trim()))+"',"+
							  " PO_CMPDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCMPDT.getText().trim()))+     "',"+
							  " PO_VENCD ='"+txtVENCD.getText().trim()+"',"+
							  " PO_CURCD ='"+txtCURCD.getText().trim()+"',"+
							  " PO_EXGRT ="+txtEXGRT.getText().trim()+","+
							  " PO_QTNTP ='"+txtQTNTP.getText().trim()+"',"+
							  " PO_QTNNO ='"+txtQTNNO.getText().trim()+"',";
							  if(txtQTNDT.getText().trim().length() == 10)
									M_strSQLQRY+=   " PO_QTNDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtQTNDT.getText().trim()))+"',";
				M_strSQLQRY+= " PO_DELTP ='"+cmbDELTP.getSelectedItem().toString().substring(0,2)+"',"+
							  " PO_DSTCD ='"+cmbDSTCD.getSelectedItem().toString().substring(0,3)+"',"+
							  " PO_PMTRF ='"+txtPMTRF.getText().trim()+"',";
				M_strSQLQRY+= " PO_CRDDY ='"+txtCRDDY.getText().trim()+"',";
				M_strSQLQRY+= " PO_AMDNO ='"+txtAMDNO.getText().trim()+"',";
				if(txtAMDDT.getText().trim().length() == 10)
				M_strSQLQRY+=   " PO_AMDDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtAMDDT.getText().trim()))+"',";
			    if(flgAMDFL)
				{
					M_strSQLQRY+= "PO_AUTBY ='',";
					if(intSELROW ==0)
						M_strSQLQRY+= getUSGDTL("PO",'U',"O");
					else
						M_strSQLQRY+= getUSGDTL("PO",'U',null);			   	
				}
				else
					M_strSQLQRY+= getUSGDTL("PO",'U',null);			   	
				M_strSQLQRY+=  " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS = '"+M_strSBSCD+"' AND PO_STRTP = '"+
							   M_strSBSCD.substring(2,4)+"' and PO_PORNO ='"+
							   txtPORNO.getText().trim() +"' AND isnull(PO_STSFL,'') <>'X'";
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		//	}
			if(!updCOTAX())
			{
				setMSG("Error in Updating Common Taxes..",'E');
				return;
			}
			if(!updITTAX())
			{
				setMSG("Error in Updating Item Wise Taxes..",'E');
				return;
			}
				// P.O. Annexture modification
				if(txaPORMK.getText().trim().length() ==0)
				{
					if(flgPOANX)
					{
						M_strSQLQRY = "INSERT INTO MM_PORAM select * from mm_pormk ";
						M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "PO_PORNO = '"+txtPORNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
						// Annexture was present it has been deleted by user
						M_strSQLQRY = "UPDATE MM_PORMK SET PO_AMDNO ='"+txtAMDNO.getText().trim() +"',";
						M_strSQLQRY += getUSGDTL("PO",'U',"X");
						M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "PO_PORNO = '"+txtPORNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
				else
				{
					if(flgPOANX)
					{
						M_strSQLQRY = "INSERT INTO MM_PORAM select * from mm_pormk ";
						M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "PO_PORNO = '"+txtPORNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
						// Length >0 and remark was present ,update
						M_strSQLQRY = "UPDATE MM_PORMK SET PO_PORMK ='"+txaPORMK.getText().trim() +"',";
						M_strSQLQRY += getUSGDTL("PO",'U',"");
						M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "PO_PORNO = '"+txtPORNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else // Insert
					{
						M_strSQLQRY = "INSERT INTO MM_PORMK(PO_CMPCD,PO_MMSBS,PO_STRTP,PO_PORTP,PO_PORNO,PO_AMDNO,PO_PORMK,PO_TRNFL,PO_STSFL,PO_LUSBY,PO_LUPDT) VALUES(";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'"+M_strSBSCD+"',";
						M_strSQLQRY += "'"+M_strSBSCD.substring(2,4)+"',";
						M_strSQLQRY += "'"+cmbPORTP.getSelectedItem().toString().substring(0,2)+"',";
						M_strSQLQRY += "'"+txtPORNO.getText().trim()+"',";
						M_strSQLQRY += "'"+txtAMDNO.getText().trim()+"',";
						M_strSQLQRY += "'"+txaPORMK.getText().trim()+"',";
						M_strSQLQRY += getUSGDTL("PO",'I',"")+")";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
					}
				}
				if(txtREMDS.getText().trim().length() == 0)
				{
					if(flgPORRM)
					{
						M_strSQLQRY = "UPDATE MM_RMMST SET ";
						M_strSQLQRY += getUSGDTL("RM",'U',"X");
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
						M_strSQLQRY += " AND RM_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
						M_strSQLQRY += " AND RM_DOCTP ='"+strTRNTP +"'";
						M_strSQLQRY += " AND RM_TRNTP ='"+strTRNTP +"'";
						M_strSQLQRY += " AND RM_REMTP ='POR'";
						M_strSQLQRY += " AND RM_DOCNO ='"+txtPORNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
				else
				{
					if(flgPORRM)
					{
						M_strSQLQRY = "UPDATE MM_RMMST SET RM_REMDS ='"+txtREMDS.getText().trim() +"',";
						M_strSQLQRY += getUSGDTL("RM",'U',"");
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
						M_strSQLQRY += " AND RM_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
						M_strSQLQRY += " AND RM_DOCTP ='"+strTRNTP +"'";
						M_strSQLQRY += " AND RM_TRNTP ='"+strTRNTP +"'";
						M_strSQLQRY += " AND RM_REMTP ='POR'";
						M_strSQLQRY += " AND RM_DOCNO ='"+txtPORNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else //insert
					{
						insRMMST("PO","POR",txtREMDS.getText().trim());
					}
							
			}
			if(cl_dat.exeDBCMT("updPODTL"))
		    {
				setMSG("Data has been Updated Suceesfully..",'N');
				clrCOMP();
			}
			else
			{
				//setMSG("Error in modification updPODTL..",'E');
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"updPODTL");
		}
		finally
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
		}
	}

	private void savPOANX()
	{
		if(txaPORMK.getText().trim().length() >0)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
			{
				
				M_strSQLQRY = "INSERT INTO MM_PORMK(PO_CMPCD,PO_MMSBS,PO_STRTP,PO_PORTP,PO_PORNO,PO_PORMK,PO_TRNFL,PO_STSFL,PO_LUSBY,PO_LUPDT) VALUES(";
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
				M_strSQLQRY += "'"+M_strSBSCD+"',";
				M_strSQLQRY += "'"+M_strSBSCD.substring(2,4)+"',";
				M_strSQLQRY += "'"+cmbPORTP.getSelectedItem().toString().substring(0,2)+"',";
				M_strSQLQRY += "'"+txtPORNO.getText().trim()+"',";
				M_strSQLQRY += "'"+txaPORMK.getText().trim()+"',";
				M_strSQLQRY += getUSGDTL("PO",'I',"")+")";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
				
				M_strSQLQRY = "UPDATE MM_PORMK SET ";
				M_strSQLQRY += " PO_pormk ='"+txaPORMK.getText().trim()+"',";
				M_strSQLQRY +=   getUSGDTL("PO",'U',"X");
				M_strSQLQRY += " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"' AND ";
				M_strSQLQRY += " PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				M_strSQLQRY += " PO_PORNO = '"+txtPORNO.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
	
		}
		
	}
private class mm_teporTBLINVFR extends TableInputVerifier
{
	
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(getSource() == tblITDTL)
				{
					if(P_intCOLID==TBL_PORRT)
					{
					    strTEMP = tblITDTL.getValueAt(P_intROWID,TBL_PORRT).toString();
						if(tblITDTL.getValueAt(P_intROWID,TBL_MATCD).toString().trim().length() ==0)
							return true;
						if(strTEMP.length() ==0)
						{
							setMSG("Enter P.O. Rate..",'E');
							return false;
						}
						if(Float.parseFloat(strTEMP) == 0)
						{
							setMSG("P.O.Rate cannot be zero..",'E');
							return false;
						}
					}
					if(P_intCOLID==TBL_ORDQT)
					{
						String L_strHSKEY ="";
						tblDLSCH.clrTABLE();
						strTEMP = tblITDTL.getValueAt(P_intROWID,TBL_ORDQT).toString();
						if((txtAMDNO.getText().equals("00"))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))	
						{
							L_strHSKEY =tblITDTL.getValueAt(P_intROWID,TBL_INDNO).toString()+tblITDTL.getValueAt(P_intROWID,TBL_MATCD).toString();;
							if(hstPENQT !=null)
							if(Double.parseDouble(strTEMP) >Double.parseDouble(hstPENQT.get(L_strHSKEY).toString()))
							{
								setMSG("Order Qty. can not be greater than Pending Indent qty..",'E');
						        return false;
							}
						}
						else
						{
							// For project items indent is generated afterwards
						//	if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
						//		return true;;
							M_strSQLQRY = "SELECT isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0) L_PINQT,PO_PORQT,PO_ACPQT FROM MM_POMST,MM_INMST WHERE PO_STRTP = IN_STRTP AND PO_INDNO = IN_INDNO AND PO_MATCD = IN_MATCD AND PO_CMPCD = IN_CMPCD AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STSFL <>'X' AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STSFL ='4'";
							M_strSQLQRY +=" AND PO_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
							M_strSQLQRY +=" and po_porno ='"+txtPORNO.getText().trim() +"'";
							M_strSQLQRY +=" and po_indno ='"+tblITDTL.getValueAt(P_intROWID,TBL_INDNO).toString()+"'";
							M_strSQLQRY +=" and po_matcd ='"+tblITDTL.getValueAt(P_intROWID,TBL_MATCD).toString()+"'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET == null)
							{
                            	return false;
							}
							if(M_rstRSSET.next()) 
							{
								double L_dblPORQT = M_rstRSSET.getDouble("PO_PORQT");
								if(L_dblPORQT == M_rstRSSET.getDouble("PO_ACPQT"))
								{
									setMSG("Ordered Qty. has been accepted .can not amend..",'E');
									if(Double.parseDouble(tblITDTL.getValueAt(P_intROWID,TBL_ORDQT).toString()) !=L_dblPORQT)
									{	
                                      	return false;
									//	commented on 19/01/2005
									//	if(Double.parseDouble(tblITDTL.getValueAt(P_intROWID,TBL_ORDQT).toString()) ==L_dblPORQT)

										//	return true;
										//else
										//{
										//	return false;
										//}
									}
								}
								if(Double.parseDouble(strTEMP) >M_rstRSSET.getDouble("L_PINQT"))
								{
									setMSG("Order Qty. can not be greater than pending Indent qty..",'E');
									//tblITDTL.setValueAt(setNumberFormat(L_dblPORQT,3),P_intROWID,TBL_ORDQT);
							        return false;
								}	
								else if(Double.parseDouble(strTEMP) <M_rstRSSET.getDouble("PO_ACPQT"))
								{
									setMSG("Order Qty. can not be smaller than accepted qty..",'E');
									//tblITDTL.setValueAt(setNumberFormat(L_dblPORQT,3),P_intROWID,TBL_ORDQT);
                            		return false;
								}	
								return true;
							}
							else
							{
							    // New Item from indent
							    M_strSQLQRY = "SELECT isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0) L_PINQT FROM MM_INMST WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STSFL ='4'";
							    M_strSQLQRY +=" AND IN_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
							    M_strSQLQRY +=" and IN_indno ='"+tblITDTL.getValueAt(P_intROWID,TBL_INDNO).toString()+"'";
							    M_strSQLQRY +=" and IN_matcd ='"+tblITDTL.getValueAt(P_intROWID,TBL_MATCD).toString()+"'";
    							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    							if(M_rstRSSET == null)
    							{
                           			return false;
    							}
    							if(M_rstRSSET.next()) 
						    	{
							    	if(Double.parseDouble(strTEMP) >M_rstRSSET.getDouble("L_PINQT"))
    								{
    									setMSG("Order Qty. can not be greater than pending Indent qty..",'E');
    									//tblITDTL.setValueAt(setNumberFormat(L_dblPORQT,3),P_intROWID,TBL_ORDQT);
    						            return false;
    								}
							    }
                         	}
							
						}
					}
					if(P_intCOLID==TBL_MATDS)
					{
						strTEMP = tblITDTL.getValueAt(P_intROWID,TBL_MATDS).toString();
						if(strTEMP.length() > 45)
						{
							setMSG("Material desc. can not be more than 45 characters",'E');
							return false;
						}
						else 
						{
							for(int i=0;i<strTEMP.length();i++)
							{
								if((strTEMP.indexOf("'")>=0)||(strTEMP.indexOf("\\")>=0))
								{
									setMSG("Special characters are not allowed in material desc ",'E');
									return false;
								}
							}
						}
					}
					if(P_intCOLID==TBL_MATCD)
					{
						/*if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strPRJTP_fn))
						{
							strTEMP = tblITDTL.getValueAt(P_intROWID,TBL_MATCD).toString();
							if(strTEMP !=null)
							if(strTEMP.length()== 10)
							{
								for(int i=0;i<=P_intROWID-1;i++)
								{
									if(tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
									if(tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().equals(strTEMP.trim()))
									{
										setMSG("Duplicate Item Code ..",'E');
										return false;
									}
								}
								if(!vldMATCD(strTEMP,P_intROWID))
								{
									return false;
								}
								else
								{
									tblITDTL.setValueAt(strINDNO,P_intROWID,TBL_INDNO);
									tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),P_intROWID,TBL_UOMCD);
									tblITDTL.setValueAt("1.00",P_intROWID,TBL_PERRT);
									tblITDTL.setValueAt(tblITDTL.getValueAt(P_intROWID,TBL_UOMCD),P_intROWID,TBL_POUOM);
									tblITDTL.setValueAt("1.000",P_intROWID,TBL_CNVFT);
									tblITDTL.setValueAt(strDPTCD,P_intROWID,TBL_DPTCD);	
									strTEMP ="0";
									strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),"");
									if(Double.parseDouble(strTEMP) ==0)
										strTEMP ="1.000";
									tblITDTL.setValueAt(strTEMP,P_intROWID,TBL_MPKQT);
									tblITDTL.cmpEDITR[TBL_POUOM].requestFocus();
								}
							}
						}*/
					}
				}
				else if(getSource() == tblDLSCH)
				{
					if(P_intCOLID==TB4_EDLDT)
					{
					    strTEMP = tblDLSCH.getValueAt(P_intROWID,TB4_EDLDT).toString();
						if(strTEMP.length() ==0)
							return true;
						if(M_fmtLCDAT.parse(strTEMP).compareTo(M_fmtLCDAT.parse(txtCMPDT.getText()))>0)
						{
							setMSG("Expected del. date can not be greater than P.O.Compl.date..",'E');
							return false;
						}
						if(M_fmtLCDAT.parse(strTEMP).compareTo(M_fmtLCDAT.parse(txtEFFDT.getText()))<0)
						{
							setMSG("Expected del. date can not be smaller than P.O.Eff. date..",'E');
							return false;
						}
					}
					if(P_intCOLID==TB4_EDLQT)
					{
						
					    strTEMP = tblDLSCH.getValueAt(P_intROWID,TB4_EDLQT).toString();
						if(strTEMP.length() ==0)
							return true;
						String L_HSKEY = tblDLSCH.getValueAt(P_intROWID,TB4_INDNO).toString()+tblDLSCH.getValueAt(P_intROWID,TB4_MATCD).toString();
						double L_dblPOQTY = Double.parseDouble(hstDLSCH.get(L_HSKEY).toString());
									
						if(hstDLQTY.get(L_HSKEY) == null)
						{
							hstDLQTY.put(L_HSKEY,new Double(Double.parseDouble(strTEMP)));
						}
						else
						hstDLQTY.put(L_HSKEY,new Double(Double.valueOf(hstDLQTY.get(L_HSKEY).toString()).doubleValue()+Double.parseDouble(strTEMP)));
						double L_dblDLQTY = Double.parseDouble(hstDLQTY.get(L_HSKEY).toString());
						if( L_dblPOQTY > L_dblDLQTY)
						{
							tblDLSCH.setValueAt(tblDLSCH.getValueAt(P_intROWID,TB4_INDNO).toString(),P_intROWID+1,TB4_INDNO);
							tblDLSCH.setValueAt(tblDLSCH.getValueAt(P_intROWID,TB4_MATCD).toString(),P_intROWID+1,TB4_MATCD);
							tblDLSCH.setValueAt(tblDLSCH.getValueAt(P_intROWID,TB4_EDLDT).toString(),P_intROWID+1,TB4_EDLDT);
							tblDLSCH.setValueAt(String.valueOf(L_dblPOQTY-L_dblDLQTY),P_intROWID+1,TB4_EDLQT);
							
						}
					}
			    }
				else if(getSource() == tblCOTAX)
				{
					if(P_intCOLID== 3)  
					{
						strTEMP = tblCOTAX.getValueAt(P_intROWID,3).toString().toUpperCase();
						tblCOTAX.setValueAt(strTEMP,P_intROWID,3);
						if((strTEMP.equals("I"))||(strTEMP.equals("X"))||(strTEMP.equals("")))
						{
							tblCOTAX.setValueAt("0.00",P_intROWID,4);
						}
					}
					if(P_intCOLID== 4)  // value
					{
						strTEMP = tblCOTAX.getValueAt(P_intROWID,4).toString();
						if((strTEMP.length() >0)&& (tblCOTAX.getValueAt(P_intROWID,3).toString().length() >0))
						if(Double.parseDouble(strTEMP) == 0)
						{
							if(!tblCOTAX.getValueAt(P_intROWID,3).toString().equals(""))
							if(!tblCOTAX.getValueAt(P_intROWID,3).toString().equals("I"))
								if(!tblCOTAX.getValueAt(P_intROWID,3).toString().equals("X"))
								{
									setMSG("For FLag 'A' or 'P' Value can not be 0,",'E');
									return false;
								}
								else setMSG("",'N');
						}
						else
						{
							if(!tblCOTAX.getValueAt(P_intROWID,3).toString().equals(""))
							if(!tblCOTAX.getValueAt(P_intROWID,3).toString().equals("P"))
								if(!tblCOTAX.getValueAt(P_intROWID,3).toString().equals("A"))
								{
									setMSG("For Flag 'I' or 'X' value can not be > 0",'E');
									return false;
								}
							else setMSG("",'N');
						}
						
					}
					
				}
				else if(getSource() == tblITTAX)
				{
					if(P_intCOLID== 5)  // value
					{
						strTEMP = tblITTAX.getValueAt(P_intROWID,5).toString();
						if((strTEMP.length() >0)&& (tblITTAX.getValueAt(P_intROWID,4).toString().length() >0))
						if(Double.parseDouble(strTEMP) == 0)
						{
							if(!tblITTAX.getValueAt(P_intROWID,4).toString().toUpperCase().equals("I"))
								if(!tblITTAX.getValueAt(P_intROWID,4).toString().toUpperCase().equals("X"))
								{
									setMSG("For Flag 'I' or 'X' value can not be > 0",'E');
									return false;
								}
							else setMSG("",'N');
						}
						else
						{
							if(!tblITTAX.getValueAt(P_intROWID,4).toString().equals("P"))
								if(!tblITTAX.getValueAt(P_intROWID,4).toString().equals("A"))
								{
									setMSG("For Flag 'A' or 'P' value can not be 0",'E');
									return false;
								}
							else setMSG("",'N');
						}
						
					}
						
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
			}
			return true;
		}
}
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		if(input == txtVENCD)
		{
			if(txtVENCD.getText().trim().length() == 0)
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				return true;
			}
			/*if(!vldVENCD(txtVENCD.getText().trim()))
			{
				setMSG("Invalid Supplier code..",'E');
				return false;
			}*/
				
		}
		return true;	
	}
}
private boolean updCOTAX()
{
	try
	{
	String L_strCTXUPD ="";
	String L_strCTXIN1 ="";
	String L_strCTXIN2 ="";
	int L_intFRST =0;
	if(flgAMDFL) // in case of amendment
	{
		if(flgCOTAX)
		{
			setMSG("Inserting Common Tax ammendment data ..",'N');
			M_strSQLQRY ="INSERT INTO CO_TXDAM SELECT * FROM CO_TXDOC  where TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD ='MM' ";
			M_strSQLQRY += " AND TX_DOCTP ='POR' AND TX_PRDCD ='XXXXXXXXXX' AND TX_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
			M_strSQLQRY += " AND TX_DOCNO ='"+txtPORNO.getText().trim()+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
			M_strSQLQRY ="UPDATE CO_TXDOC set tx_amdno ='"+txtAMDNO.getText().trim() +"'"+" WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD ='MM'  ";
			M_strSQLQRY += " AND TX_DOCTP ='POR' AND TX_PRDCD ='XXXXXXXXXX' AND TX_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
			M_strSQLQRY += " AND TX_DOCNO ='"+txtPORNO.getText().trim()+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
		}
		M_strSQLQRY ="INSERT INTO CO_TXSAM SELECT * FROM CO_TXSPC WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND ";
		M_strSQLQRY += " TXT_DOCTP ='POR' AND TXT_PRDCD ='XXXXXXXXXX' AND TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
		M_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
		cl_dat.exeSQLUPD(M_strSQLQRY,"");		
		M_strSQLQRY ="UPDATE CO_TXSPC set txt_amdno ='"+txtAMDNO.getText().trim() +"'"+" WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXt_SYSCD ='MM' AND ";
		M_strSQLQRY += " TXt_DOCTP ='POR' AND TXt_PRDCD ='XXXXXXXXXX' AND TXt_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
		M_strSQLQRY += " AND TXt_DOCNO ='"+txtPORNO.getText().trim()+"'";
		cl_dat.exeSQLUPD(M_strSQLQRY,"");	
	}
	for(int i=0;i<tblCOTAX.getRowCount();i++)
	{
		//if(present in hash table update else insert
		if(tblCOTAX.getValueAt(i,TBL_CHKFL).toString().equals("true"))
		{
			if(hstTXCAT.get(tblCOTAX.getValueAt(i,1).toString()).toString().equals("01"))//FOR CO_TXDOC
			{	
				if(flgCOTAX) // Common TAx is present
				{
					if(L_intFRST == 0) // First Item
					{
						L_strCTXUPD +="TX_"+tblCOTAX.getValueAt(i,1).toString()+"FL ='"+tblCOTAX.getValueAt(i,3).toString()+"',";
						L_strCTXUPD +="TX_"+tblCOTAX.getValueAt(i,1).toString()+"VL ="+tblCOTAX.getValueAt(i,4).toString();
						if((tblCOTAX.getValueAt(i,1).toString().equals("STX"))||(tblCOTAX.getValueAt(i,1).toString().equals("OTH")))
						{
							L_strCTXUPD +=",TX_"+tblCOTAX.getValueAt(i,1).toString()+"DS ='"+tblCOTAX.getValueAt(i,2).toString()+"'";
						}
					}
					else
					{
						L_strCTXUPD +=",TX_"+tblCOTAX.getValueAt(i,1).toString()+"FL ='"+tblCOTAX.getValueAt(i,3).toString()+"',";
						L_strCTXUPD +="TX_"+tblCOTAX.getValueAt(i,1).toString()+"VL ="+tblCOTAX.getValueAt(i,4).toString();
						if((tblCOTAX.getValueAt(i,1).toString().equals("STX"))||(tblCOTAX.getValueAt(i,1).toString().equals("OTH")))
							L_strCTXUPD +=",TX_"+tblCOTAX.getValueAt(i,1).toString()+"DS ='"+tblCOTAX.getValueAt(i,2).toString()+"'";
					}
					L_intFRST++;
				}
				else
				{
					if(L_intFRST == 0) // First Item
					{
						L_strCTXIN1 +="TX_"+tblCOTAX.getValueAt(i,1).toString()+"FL,"+"TX_"+tblCOTAX.getValueAt(i,1).toString()+"VL";
						L_strCTXIN2 +="'"+tblCOTAX.getValueAt(i,3).toString()+"',"+tblCOTAX.getValueAt(i,4).toString();
								
						if(tblCOTAX.getValueAt(i,1).toString().equals("STX")||tblCOTAX.getValueAt(i,1).toString().equals("OTH"))
						{
							L_strCTXIN1 +=",TX_"+tblCOTAX.getValueAt(i,1).toString()+"DS";
							L_strCTXIN2 +=",'"+tblCOTAX.getValueAt(i,2).toString()+"'"; // For Desc
						}
					}
					else
					{
						L_strCTXIN1 +=",TX_"+tblCOTAX.getValueAt(i,1).toString()+"FL,"+"TX_"+tblCOTAX.getValueAt(i,1).toString()+"VL";
						L_strCTXIN2 +=",'"+tblCOTAX.getValueAt(i,3).toString()+"',"+tblCOTAX.getValueAt(i,4).toString();
								
						if(tblCOTAX.getValueAt(i,1).toString().equals("STX")||tblCOTAX.getValueAt(i,1).toString().equals("OTH"))
						{
							L_strCTXIN1 +=",TX_"+tblCOTAX.getValueAt(i,1).toString()+"DS";
							L_strCTXIN2 +=",'"+tblCOTAX.getValueAt(i,2).toString()+"'"; // For Desc
						}
					}
					L_intFRST++;
				}
			}
			else
			{
				String L_strSQLQRY = "SELECT COUNT(*) L_CNT FROM CO_TXSPC ";
				L_strSQLQRY += " WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP ='POR' AND TXT_PRDCD ='XXXXXXXXXX' AND TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
				L_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
				L_strSQLQRY += " AND TXT_CODCD ='"+tblCOTAX.getValueAt(i,1).toString()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt("L_CNT") == 0)
						{
							L_strSQLQRY ="INSERT INTO CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,";
							L_strSQLQRY += "TXT_SBSCD,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_TRNFL,TXT_STSFL,TXT_LUSBY,TXT_LUPDT,TXT_AMDNO) VALUES(";
							L_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"','MM','"+M_strSBSCD.substring(2,4) +"','POR',"+
										   "'"+txtPORNO.getText().trim()+"',"+
										   "'XXXXXXXXXX',"+
										   "'"+tblCOTAX.getValueAt(i,1).toString()+"',"+
										   "'"+tblCOTAX.getValueAt(i,2).toString()+"',"+
										   "'"+M_strSBSCD+"',"+
										   tblCOTAX.getValueAt(i,4).toString()+","+
										   "'"+tblCOTAX.getValueAt(i,3).toString()+"',"+
										   "'"+tblCOTAX.getValueAt(i,5).toString()+"',";
							L_strSQLQRY += getUSGDTL("TXT",'I',"")+",'"+txtAMDNO.getText().trim()+"')";
							cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
						}
						else
						{
							// if user tries to remove the specific tax i.e blanks out the flag
							// it is marked as deleted , else update the details
							
							if(tblCOTAX.getValueAt(i,3).toString().equals(""))
							{
								L_strSQLQRY ="UPDATE CO_TXSPC SET TXT_AMDNO ='"+txtAMDNO.getText().trim()+"',";
								L_strSQLQRY += getUSGDTL("TXT",'U',"X");	
								L_strSQLQRY += " WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP ='POR' AND TXT_PRDCD ='XXXXXXXXXX' AND TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
								L_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
								L_strSQLQRY += " AND TXT_CODCD ='"+tblCOTAX.getValueAt(i,1).toString()+"'";
								cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");	
							}
							else
							{
								L_strSQLQRY ="UPDATE CO_TXSPC SET TXT_CODDS ='"+tblCOTAX.getValueAt(i,2).toString()+"',"+
											" TXT_CODFL ='"+tblCOTAX.getValueAt(i,3).toString()+"',"+		
								            " TXT_CODVL ="+tblCOTAX.getValueAt(i,4).toString()+","+
											" TXT_AMDNO ='"+txtAMDNO.getText().trim()+"',";	
								L_strSQLQRY += getUSGDTL("TXT",'U',"");					 
								L_strSQLQRY += " WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP ='POR' AND TXT_PRDCD ='XXXXXXXXXX' AND TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
								L_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
								L_strSQLQRY += " AND TXT_CODCD ='"+tblCOTAX.getValueAt(i,1).toString()+"'";
								cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");	
							}
						}
					}
			}
		}
	}
// Cancel all taxes Related of P.O
	if(L_intFRST >0)
	{
		if(flgCOTAX)
		{
			M_strSQLQRY = " UPDATE CO_TXDOC SET ";
			M_strSQLQRY +=" TX_AMDNO = '"+txtAMDNO.getText().trim()+"',";
			M_strSQLQRY +=L_strCTXUPD+",";
			M_strSQLQRY +=getUSGDTL("TX",'U'," ");
			M_strSQLQRY += " WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSCD ='"+M_strSBSCD+"' AND ";
			M_strSQLQRY += "TX_DOCTP = 'POR' ";
			M_strSQLQRY += " AND TX_SBSTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
			M_strSQLQRY += "TX_DOCNO = '"+txtPORNO.getText().trim()+"'";
			M_strSQLQRY += " AND TX_PRDCD = 'XXXXXXXXXX'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		else
		{
			//insert
			M_strSQLQRY = "INSERT INTO CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_AMDNO,TX_SBSCD,TX_TRNTP,";
			M_strSQLQRY += L_strCTXIN1 +",TX_TRNFL,TX_STSFL,TX_LUSBY,TX_LUPDT)values(";
			M_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"','MM','"+M_strSBSCD.substring(2,4)+"','POR',";
			M_strSQLQRY +="'"+txtPORNO.getText().trim()+"','XXXXXXXXXX','"+txtAMDNO.getText().trim() +"',";
			M_strSQLQRY +="'"+M_strSBSCD+"','M',";
			M_strSQLQRY += L_strCTXIN2 +","+getUSGDTL("TX",'I',"")+")";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
				
	}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"updCOTAX");
		return false;
	}
	return true;
}
private boolean updITTAX()
{
	try
	{
		String L_strITXUPD ="";
		String L_strITXIN1 ="";
		String L_strITXIN2 ="";
		setMSG("Updating Item wise Tax..",'N');
		for(int i=0;i<tblITTAX.getRowCount();i++)
		{
			//if(present in hash table update else insert
			if(tblITTAX.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
			if(hstTXCAT.get(tblITTAX.getValueAt(i,2).toString()).toString().equals("01"))//FOR CO_TXDOC
			{	
				if(hstITTAX.containsKey(tblITTAX.getValueAt(i,1).toString()))
			    {
					flgITTAX = true;	
				}
				else
					flgITTAX = false;	
				if(flgITTAX)
				{
					if(flgAMDFL)
					{
						M_strSQLQRY = " insert into co_txdam select * from co_txdoc ";
						M_strSQLQRY += " WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSCD ='"+M_strSBSCD+"' AND ";
						M_strSQLQRY += "TX_DOCTP = 'POR' ";
						M_strSQLQRY += " AND TX_SBSTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
						M_strSQLQRY += "TX_DOCNO = '"+txtPORNO.getText().trim()+"'";
						M_strSQLQRY += " AND TX_PRDCD = '"+tblITTAX.getValueAt(i,1).toString()+ "'"; 
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						M_strSQLQRY ="UPDATE CO_TXDOC set tx_amdno ='"+txtAMDNO.getText().trim() +"'"+" WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD ='MM' ";
						M_strSQLQRY += " AND TX_DOCTP ='POR' AND TX_PRDCD <> 'XXXXXXXXXX' AND TX_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
						M_strSQLQRY += " AND TX_DOCNO ='"+txtPORNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}			 
					M_strSQLQRY = " UPDATE CO_TXDOC SET ";
					M_strSQLQRY += " TX_AMDNO ='"+txtAMDNO.getText().trim() +"',";
					M_strSQLQRY	+= "TX_"+tblITTAX.getValueAt(i,2).toString()+"FL ='"+
								   tblITTAX.getValueAt(i,4).toString()+"',";
					M_strSQLQRY	+= "TX_"+tblITTAX.getValueAt(i,2).toString()+"VL ="+
								   tblITTAX.getValueAt(i,5).toString();
					if(tblITTAX.getValueAt(i,2).toString().equals("STX")||tblITTAX.getValueAt(i,2).toString().equals("OTH"))
					{
						M_strSQLQRY +=",TX_"+tblITTAX.getValueAt(i,2).toString()+"DS ='"+tblITTAX.getValueAt(i,3).toString()+"'";
					}	
					M_strSQLQRY +=","+getUSGDTL("TX",'U'," ");
					M_strSQLQRY += " WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSCD ='"+M_strSBSCD+"' AND ";
					M_strSQLQRY += "TX_DOCTP = 'POR' ";
					M_strSQLQRY += " AND TX_SBSTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
					M_strSQLQRY += "TX_DOCNO = '"+txtPORNO.getText().trim()+"'";
					M_strSQLQRY += " AND TX_PRDCD = '"+tblITTAX.getValueAt(i,1).toString()+ "'"; 
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
				else
				{
					//insert
					M_strSQLQRY = "INSERT INTO CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_SBSCD,TX_TRNTP,TX_AMDNO,";
		// Modified on 14/12/2004, Review if some problem comes
		//*******
		 			L_strITXIN1 ="TX_"+tblITTAX.getValueAt(i,2).toString()+"FL,"+"TX_"+tblITTAX.getValueAt(i,2).toString()+"VL";
					L_strITXIN2 ="'"+tblITTAX.getValueAt(i,4).toString()+"',"+tblITTAX.getValueAt(i,5).toString();
				
				//	L_strITXIN1 +="TX_"+tblITTAX.getValueAt(i,2).toString()+"FL,"+"TX_"+tblITTAX.getValueAt(i,2).toString()+"VL";
				//	L_strITXIN2 +="'"+tblITTAX.getValueAt(i,4).toString()+"',"+tblITTAX.getValueAt(i,5).toString();
		
		//*******
					if(tblITTAX.getValueAt(i,2).toString().equals("STX")||tblITTAX.getValueAt(i,2).toString().equals("OTH"))
					{
						L_strITXIN1 +=",TX_"+tblITTAX.getValueAt(i,2).toString()+"DS";
						L_strITXIN2 +=",'"+tblITTAX.getValueAt(i,3).toString()+"'"; // For Desc
					}
					M_strSQLQRY += L_strITXIN1 +",TX_TRNFL,TX_STSFL,TX_LUSBY,TX_LUPDT)values(";
					M_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"','MM','"+M_strSBSCD.substring(2,4)+"','POR','"+txtPORNO.getText().trim()+"',";
					M_strSQLQRY +="'"+tblITTAX.getValueAt(i,1).toString().trim() +"',";
					M_strSQLQRY +="'"+M_strSBSCD+"','M','"+txtAMDNO.getText().trim() +"',";
					M_strSQLQRY += L_strITXIN2 +","+getUSGDTL("TX",'I',"")+")";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					hstITTAX.put(tblITTAX.getValueAt(i,1).toString(),"");
				}
			}
			else
			{
				String L_strSQLQRY = "SELECT COUNT(*) L_CNT FROM CO_TXSPC ";
				L_strSQLQRY += " WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP ='POR' AND TXT_PRDCD ='"+tblITTAX.getValueAt(i,1).toString()+"' AND TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
				L_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
				L_strSQLQRY += " AND TXT_CODCD ='"+tblITTAX.getValueAt(i,2).toString()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt("L_CNT") == 0)
						{
							L_strSQLQRY ="INSERT INTO CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,";
							L_strSQLQRY += "TXT_SBSCD,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_TRNFL,TXT_STSFL,TXT_LUSBY,TXT_LUPDT,TXT_AMDNO) VALUES(";
							L_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"','MM','"+M_strSBSCD.substring(2,4) +"','POR',"+
										   "'"+txtPORNO.getText().trim()+"',"+
										   "'"+tblITTAX.getValueAt(i,1).toString()+"',"+
										   "'"+tblITTAX.getValueAt(i,2).toString()+"',"+
										   "'"+tblITTAX.getValueAt(i,3).toString()+"',"+
										   "'"+M_strSBSCD+"',"+
										   tblITTAX.getValueAt(i,5).toString()+","+
										   "'"+tblITTAX.getValueAt(i,4).toString()+"',"+
										   "'"+tblITTAX.getValueAt(i,6).toString()+"',";
							L_strSQLQRY += getUSGDTL("TXT",'I',"")+",'00')";
							cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
						}
						else
						{
							if(flgAMDFL)
							{
								L_strSQLQRY ="INSERT INTO CO_TXSAM select * from co_txspc where ";
								L_strSQLQRY += " WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP ='POR' AND  TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
								L_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
								L_strSQLQRY += " AND TXT_PRDCD ='"+tblITTAX.getValueAt(i,1).toString()+"'";
								L_strSQLQRY += " AND TXT_CODCD ='"+tblITTAX.getValueAt(i,2).toString()+"'";
								cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");		
								M_strSQLQRY ="UPDATE CO_TXSAM set txt_amdno ='"+txtAMDNO.getText().trim() +"'"+" WHERE TXt_SYSCD ='MM' AND ";
								M_strSQLQRY += " TXt_DOCTP ='POR' AND TXt_PRDCD <> 'XXXXXXXXXX' AND TXt_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
								M_strSQLQRY += " AND TXt_DOCNO ='"+txtPORNO.getText().trim()+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
							}
							if(tblITTAX.getValueAt(i,4).toString().equals(""))
							{
								L_strSQLQRY ="UPDATE CO_TXSPC SET "+
								" TXT_AMDNO ='"+txtAMDNO.getText().trim()+"',";
								L_strSQLQRY += getUSGDTL("TXT",'U',"X");					 
								L_strSQLQRY += " WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP ='POR' AND  TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
								L_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
								L_strSQLQRY += " AND TXT_PRDCD ='"+tblITTAX.getValueAt(i,1).toString()+"'";
								L_strSQLQRY += " AND TXT_CODCD ='"+tblITTAX.getValueAt(i,2).toString()+"'";
								cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");		
							 }
							else
							{
								L_strSQLQRY ="UPDATE CO_TXSPC SET TXT_CODDS ='"+tblITTAX.getValueAt(i,3).toString()+"',"+
								" TXT_CODFL ='"+tblITTAX.getValueAt(i,4).toString()+"',"+		
				                " TXT_CODVL ="+tblITTAX.getValueAt(i,5).toString()+","+
								" TXT_AMDNO ='"+txtAMDNO.getText().trim()+"',";
								L_strSQLQRY += getUSGDTL("TXT",'U',"");					 
								L_strSQLQRY += " WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP ='POR' AND  TXT_SBSTP ='"+M_strSBSCD.substring(2,4)+"'";
								L_strSQLQRY += " AND TXT_DOCNO ='"+txtPORNO.getText().trim()+"'";
								L_strSQLQRY += " AND TXT_PRDCD ='"+tblITTAX.getValueAt(i,1).toString()+"'";
								L_strSQLQRY += " AND TXT_CODCD ='"+tblITTAX.getValueAt(i,2).toString()+"'";
								cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");		
							}
						}
					}
			}
			
			}
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"Item Tax");
		return false;	
	}
	return true;
}
private void getINDDT(String L_strPORTP)
{
   try
   {
        String L_strINDNO,L_strMATCD,L_strPINQT;
        M_strSQLQRY ="";
		int L_intRECCT =0;
        M_strSQLQRY = "Select IN_INDNO,IN_MATCD,CT_MATDS,CT_UOMCD,isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0) L_PQTY,IN_DPTCD,CT_PKGQT,IN_TCFFL,IN_INSFL from MM_INMST,CO_CTMST WHERE IN_MATCD = CT_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') <>'X'";
        M_strSQLQRY +=" AND (isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)) >0";
        M_strSQLQRY +=" and IN_MMSBS ='"+M_strSBSCD+"' AND IN_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
        M_strSQLQRY +=" and IN_INDNO ='"+txtINDNT.getText().trim()+"'";
		if(L_strPORTP.equals(strRGPOR_fn)) // Regular P.O.
			M_strSQLQRY +=" and IN_INDTP ='01'";
		else if(L_strPORTP.equals(strRTCON_fn)) // Rate Contract
			M_strSQLQRY +=" and IN_INDTP ='02'";
		if(hstINDNO.get(txtINDNT.getText().trim())== null)
		{
			//*** changed on 06/09/2004 for rate Contract
			//hstINDNO.put(txtINDNT.getText().trim(),"");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
			   L_intRECCT++;
			   L_strINDNO = nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),"");
			   tblITDTL.setValueAt(L_strINDNO,intINDCT,TBL_INDNO);
			   L_strMATCD = nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),"");

			   tblITDTL.setValueAt(L_strMATCD,intINDCT,TBL_MATCD);
			   tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),intINDCT,TBL_UOMCD);
			   L_strPINQT = nvlSTRVL(M_rstRSSET.getString("L_PQTY"),"");
			   tblITDTL.setValueAt(L_strPINQT,intINDCT,TBL_ORDQT);
			   tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),""),intINDCT,TBL_DPTCD);
			   strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),"");
			   if(Double.parseDouble(strTEMP) ==0)
			        strTEMP ="1.000";
			   tblITDTL.setValueAt(strTEMP,intINDCT,TBL_MPKQT);

			   tblITDTL.setValueAt("1.00",intINDCT,TBL_PERRT);
			   tblITDTL.setValueAt(tblITDTL.getValueAt(intINDCT,TBL_UOMCD),intINDCT,TBL_POUOM);
			   tblITDTL.setValueAt("1.000",intINDCT,TBL_CNVFT);
			   strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_TCFFL"),"");
			   if(strTEMP.equals("Y"))
			         tblITDTL.setValueAt(Boolean.TRUE,intINDCT,TBL_TCFFL);
			   else 
			         tblITDTL.setValueAt(Boolean.FALSE,intINDCT,TBL_TCFFL);
			   strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_INSFL"),"");
			   if(strTEMP.equals("Y"))
			        tblITDTL.setValueAt(Boolean.TRUE,intINDCT,TBL_INSFL);
			   else 
			        tblITDTL.setValueAt(Boolean.FALSE,intINDCT,TBL_INSFL);

			   hstPENQT.put(L_strINDNO+L_strMATCD,L_strPINQT);
			   intINDCT++;
			}
			if(L_intRECCT ==0)
			 setMSG("Invalid Indent No...",'E');
			else
			{
				// If indent is of that category 
				setMSG("",'N');
				//*** changed on 06/09/2004 for rate Contract
				hstINDNO.put(txtINDNT.getText().trim(),"");
			}
		}
		else
		{
			setMSG("Given Indent no. is already taken ..",'E');  
		}
		  txtINDNT.setText("");
    }
    catch(Exception L_E)
    {
        setMSG(L_E,"grtINDDT");
    }
}
private void crtHSTXCD()
{
	try
	{
		String L_strHSVAL ="",
		M_strSQLQRY = " SELECT * FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXTAX' order by CMT_CCSVL,CMT_NMP01";
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(L_rstRSSET !=null)
				
		while(L_rstRSSET.next())
		{
			L_strHSVAL = nvlSTRVL(L_rstRSSET.getString("CMT_NMP01"),"") ;// Multiplier
			L_strHSVAL += "|"+nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"") ;// Multiplier
		   // ADDED
		    L_strHSVAL += "|"+nvlSTRVL(L_rstRSSET.getString("CMT_MODLS")," "+"|") ;// Multiplier

		// Commented on 19/01/2005 for making calPOVAL as common procedure
		//	if(nvlSTRVL(L_rstRSSET.getString("CMT_CHP01"),"").equals("01"))
		//	{
				hstTAXCD.put(L_rstRSSET.getString("CMT_CODCD"),L_strHSVAL);
				vtrTAXCD.addElement(L_rstRSSET.getString("CMT_CODCD"));
		//	}
		//	else 
		//		hstTAXSP.put(L_rstRSSET.getString("CMT_CODCD"),L_strHSVAL);
			vtrTAXTP.addElement(L_rstRSSET.getString("CMT_CHP01"));
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"crtHSTCD");
	}
}
private void adjISSVL(String L_strSTSFL,String L_strYSTDT)
{
   try
   {
	double L_dblSTKQT =0.0,L_dblPISVL=0.0,L_dblISSVL=0.0,L_dblISSRT=0.0,L_dblADJVL=0.0;
	String L_strPISNO,L_strTAGNO="",L_strMATCD,L_strISSDT="";
	ResultSet L_rstRSSET,L_rstRSSET1 ;
	// Checking the previous Issue number, if stock qty is 0 then load the previous issue.  
	M_strSQLQRY = "SELECT ST_MATCD,ST_STKQT,ST_PISNO from MM_STMST,MM_GRMST WHERE ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_CMPCD = GR_CMPCD "+
				  " GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_PORNO ='"+txtPORNO.getText().trim()+"' and isnull(GR_ACPQT,0) >0 and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
	M_strSQLQRY +=" AND ST_MATCD IN(SELECT PO_MATCD FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
	M_strSQLQRY +=" AND PO_PORNO = '"+txtPORNO.getText().trim() +"') and isnull(st_stkqt,0) =0 and isnull(st_pisno,'') <> ''";
	L_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
	if(L_rstRSSET1 !=null)
	while(L_rstRSSET1.next())
	{
		L_dblSTKQT = L_rstRSSET1.getDouble("ST_STKQT");
		L_strPISNO  = nvlSTRVL(L_rstRSSET1.getString("ST_PISNO"),"");
		L_strMATCD  = L_rstRSSET1.getString("ST_MATCD");
		if(L_strPISNO.length() >0)
		{
			M_strSQLQRY = "SELECT IS_TAGNO,IS_ISSVL,isnull(STP_CISVL,0),IS_ISSQT,CONVERT(varchar,IS_AUTDT,103) L_ISSDT,"+
			"isnull(STP_YOSVL,0)+isnull(STP_CRCVL,0)+isnull(STP_CSAVL,0)-(isnull(STP_CISVL,0)-isnull(STP_CMRVL,0))-(isnull(STP_YCLRT,0)*isnull(STP_YCSQT,0))L_ISSVL "+
			" FROM MM_ISMST,MM_STPRC WHERE IS_STRTP = STP_STRTP AND IS_MATCD = STP_MATCD AND IS_CMPCD = STP_CMPCD AND IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
			" AND IS_ISSNO ='"+L_strPISNO +"' AND IS_MATCD ='"+L_strMATCD +"' and isnull(is_stsfl,'')='2' and isnull(is_issqt,0) >0";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					L_dblPISVL = L_rstRSSET.getDouble("IS_ISSVL");
				//	L_dblISSVL = L_rstRSSET.getDouble("L_ISSVL");
					L_dblADJVL = L_rstRSSET.getDouble("L_ISSVL");
					L_dblISSVL = L_dblPISVL+L_dblADJVL ;
					L_dblISSRT = L_dblISSVL/L_rstRSSET.getDouble("IS_ISSQT");
					L_strTAGNO  = L_rstRSSET.getString("IS_TAGNO");
					datTEMP = L_rstRSSET.getDate("L_ISSDT");
					if(datTEMP !=null)
						L_strISSDT = M_fmtLCDAT.format(datTEMP);
					// Issue belongs to previous year
					if(M_fmtLCDAT.parse(L_strISSDT).compareTo(M_fmtLCDAT.parse(L_strYSTDT))<0)
					{
						// Processing is not done fo the prevous year
						if(!L_strSTSFL.equals("1"))
						{
							M_strSQLQRY = " UPDATE mm_ismst set is_issvl ="+L_dblISSVL +
								 ", IS_ISSRT ="+L_dblISSRT +
								 " WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4) +"'"+
								 " and IS_ISSNO = '"+L_strPISNO +"'"+
								 " and IS_MATCD = '"+L_strMATCD +"'"+
								 " and IS_TAGNO = '"+L_strTAGNO +"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							M_strSQLQRY = " UPDATE mm_stprc set stp_cisvl = isnull(stp_cisvl,0) + "+(L_dblISSVL -L_dblPISVL) ;
							M_strSQLQRY += " where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_strtp ='"+M_strSBSCD.substring(2,4) +"'";
							M_strSQLQRY += " and stp_matcd ='"+L_strMATCD +"' and isnull(stp_cisqt,0) >0";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
					else
					{
						M_strSQLQRY = " UPDATE mm_ismst set is_issvl ="+L_dblISSVL +
								  ", IS_ISSRT ="+L_dblISSRT +
								 " WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4) +"'"+
								 " and IS_ISSNO = '"+L_strPISNO +"'"+
								 " and IS_MATCD = '"+L_strMATCD +"'"+
								 " and IS_TAGNO = '"+L_strTAGNO +"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						M_strSQLQRY = " UPDATE mm_stprc set stp_cisvl = isnull(stp_cisvl,0) + "+(L_dblISSVL -L_dblPISVL) ;
						M_strSQLQRY += " where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_strtp ='"+M_strSBSCD.substring(2,4) +"'";
						M_strSQLQRY += " and stp_matcd ='"+L_strMATCD +"' and isnull(stp_cisqt,0) >0";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
				L_rstRSSET.close();
			}
		}
	}
	if(L_rstRSSET1 !=null)
		L_rstRSSET1.close();
   }
   catch(Exception L_E)
   {
	   setMSG(L_E,"adjISSVL");
   }
}
boolean getPINQT()
{
	try
	{
		double L_dblPINQT =0.0;
		String L_strINDNO="",L_strMATCD ="",L_strPINQT="";
		
		M_strSQLQRY ="SELECT PO_PORNO,PO_INDNO,PO_MATCD,PO_PORQT,PO_STSFL,isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)L_PINQT from "
					 +"MM_POMST,MM_INMST WHERE PO_CMPCD + PO_STRTP + PO_INDNO + PO_MATCD = IN_CMPCD + IN_STRTP + IN_INDNO + IN_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STSFL ='4' "
					// +"MM_POMST,MM_INMST WHERE PO_STRTP = IN_STRTP AND PO_INDNO = IN_INDNO AND PO_MATCD = IN_MATCD AND IN_STSFL ='4' "
					 +"AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STSFL ='O' AND PO_PORNO ='"+txtPORNO.getText().trim()+"'and PO_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				// PROBLEM IF THIS SEQUENCE IS USED IN QUERY WHY ???
					 // +"AND PO_STSFL ='O' and PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND PO_PORNO ='"+txtPORNO.getText().trim()+"'";
	//	java.sql.Statement stat = cl_dat.M_conSPDBA_pbst.createStatement();
	//	System.out.println(M_strSQLQRY);
        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
     //   System.out.println(M_rstRSSET);
		if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_strINDNO = M_rstRSSET.getString("PO_INDNO");
				L_strMATCD = M_rstRSSET.getString("PO_MATCD");
				L_strPINQT = nvlSTRVL(M_rstRSSET.getString("L_PINQT"),"0");
				L_dblPINQT = Double.parseDouble(L_strPINQT);
				if(hstPENQT !=null)
					hstPENQT.put(L_strINDNO+L_strMATCD,L_strPINQT);
				L_dblPINQT = M_rstRSSET.getDouble("L_PINQT");
				if(L_dblPINQT == 0)
				{
					setMSG("No Quantity is Pending on Indent for Item "+L_strMATCD +"..",'E'); 				
					return false;				
				}
			}
	}
	catch(SQLException L_E)
	{
		setMSG("Error in getting pending Indent quantity..",'E');
		return false;
	}
	return true;
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
			}
			M_rstRSSET.close();
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
		strINDNO = L_strINDNO;
		return true;
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"genINDNO");
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
}
/**
 *  Generates an Indent For the Project Purchase Order , and Authorises it. 
 * 
 * */
private boolean genPRJIN()
{
	try
	{
		String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
		String L_strPORBY,L_strEXPDT,L_strREQDT,L_strPREDTM;
		
		if(!genINDNO())
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			return false;
		}
		for(int i=0;i<tblITDTL.getRowCount();i++)
		{
			
			if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_strPREDTM = cl_dat.M_txtCLKDT_pbst.getText().substring(6,10) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(3,5) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(0,2) + " "+cl_dat.M_txtCLKTM_pbst.getText().trim() + ":00.000000000";
				L_strREQDT = null;
				L_strEXPDT = null;
				L_strPORBY = null;
				L_strREQDT = txtCMPDT.getText().trim().substring(6,10) + "-" + txtCMPDT.getText().trim().substring(3,5) + "-" + txtCMPDT.getText().trim().substring(0,2);
				L_strEXPDT = L_strREQDT;
				L_strPORBY = cl_dat.M_strLOGDT_pbst.trim().substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
				pstmINSIND.setString(1,cl_dat.M_strCMPCD_pbst);
				pstmINSIND.setString(2,M_strSBSCD);
				pstmINSIND.setString(3,M_strSBSCD.substring(2,4));
				pstmINSIND.setString(4,strINDNO);
				pstmINSIND.setString(5,"00");
				pstmINSIND.setString(6,tblITDTL.getValueAt(i,TBL_MATCD).toString().toUpperCase());
				pstmINSIND.setDate(7,Date.valueOf(L_strDATE));
				pstmINSIND.setFloat(8,Float.valueOf(tblITDTL.getValueAt(i,TBL_ORDQT).toString()).floatValue());
				pstmINSIND.setFloat(9,0); // Auth Qty 0
				pstmINSIND.setString(10,"N"); // Urgency Tag Normal
				pstmINSIND.setString(11,"aaa");
				pstmINSIND.setString(12,cl_dat.M_strUSRCD_pbst);
				if(L_strPREDTM !=null)
					pstmINSIND.setTimestamp(13,Timestamp.valueOf(L_strPREDTM));
				else
					pstmINSIND.setTimestamp(13,null);
				if(L_strREQDT !=null)
					pstmINSIND.setDate(14,Date.valueOf(L_strREQDT));
				else
					pstmINSIND.setDate(14,null);
				if(L_strEXPDT !=null)
					pstmINSIND.setDate(15,Date.valueOf(L_strEXPDT));
				else
					pstmINSIND.setDate(15,null);
				if(L_strPORBY !=null)
					pstmINSIND.setDate(16,Date.valueOf(L_strPORBY));
				else
					pstmINSIND.setDate(16,null);
				if(tblITDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
					pstmINSIND.setString(17,"Y");
				else
					pstmINSIND.setString(17,"N");
				if(tblITDTL.getValueAt(i,TBL_TCFFL).toString().equals("true"))
					pstmINSIND.setString(18,"Y");
				else
					pstmINSIND.setString(18,"N");
				strTEMP = tblITDTL.getValueAt(i,TBL_ORDQT).toString();
				pstmINSIND.setFloat(19,0);  // Indent Value 0
				pstmINSIND.setString(20,"0");
				pstmINSIND.setString(21,"0");
				pstmINSIND.setString(22,cl_dat.M_strUSRCD_pbst);
				pstmINSIND.setDate(23,Date.valueOf(L_strDATE));
				pstmINSIND.setString(24,"");
				pstmINSIND.setString(25,"");
				pstmINSIND.setTimestamp(26,null);
				pstmINSIND.setString(27,"01");
				pstmINSIND.executeUpdate();
			}
		}
		if(cl_dat.M_flgLCUPD_pbst)
		{
			M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CCSVL = '"+strSRLNO +"',CMT_CHP01 =''";
			M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2)+"IND' ";
			M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		if(cl_dat.M_flgLCUPD_pbst)
		{
			M_strSQLQRY ="UPDATE MM_INMST SET IN_STSFL ='4',IN_AUTDT = current_timestamp,IN_AUTQT = IN_INDQT ";
			M_strSQLQRY +=" WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
			M_strSQLQRY += " AND IN_INDNO ='"+strINDNO +"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		insRMMST("IN","IND","Auto Generated Indent For Project P.O");
	}
	catch(Exception L_E)
	{
		cl_dat.M_flgLCUPD_pbst = false;
		setMSG("Error in Generating Indent ..",'E');
		return false;
	}
	return true;
}
private boolean vldMATCD(String P_strMATCD,int P_intROWID)
{
	try
	{
		M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD,CT_PKGQT from CO_CTMST WHERE isnull(CT_STSFL,'') not in ('X','9')"
					  +" AND CT_MATCD ='"+P_strMATCD+"'" 
					  + " AND CT_GRPCD  ='95' order by ct_matcd";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
		if(M_rstRSSET !=null)
		if(M_rstRSSET.next())
		{
			return true;
		}
		if(M_rstRSSET !=null)
			M_rstRSSET.close();	
		setMSG("Invalid Material Code.Press F1 for help",'E');
		if(M_rstRSSET != null)
			M_rstRSSET.close();
	}catch(Exception e){
		setMSG(e,"vldMATCD");
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	return false;
}
/*
checks for amendment -
if GRIN is accepted PO_PORQT  should not be less than < GR_ACPQT
value should be recalculated.
if GRIN is accepted then value adjustment to GR_PORVL AND CUMM. RCT VL should be done,
for current year GRIN. if GRIN is of prv. year check the processing flag and update
same for issue value calculation 
*/

}