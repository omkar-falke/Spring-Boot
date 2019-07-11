/*
System Name   : Marketing System
Program Name  : Invoice Entry.
Author        : Mr. Zaheer A. Khan
Date          : 08/11/2006
System        : 
Version       : MR v2.0.0
Modificaitons : 
Modified By   :
Modified Date :  12/11/2006 
Modified det. :
Version       :
*/

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;import java.awt.Color;
import java.sql.ResultSet;import javax.swing.JPanel;import javax.swing.JTabbedPane;
import java.sql.CallableStatement;import javax.swing.JComboBox;import java.util.Vector;
import java.util.Hashtable;import java.util.Enumeration;import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.util.Calendar;
/**
System Name  : Marketing System.
 
Program Name : Invoice Entry

Purpose : This Program is used to adition ,Deletion And Enquery of Invoice Details .


List of tables used :
Table Name     Primary key                                 Operation done
                                                            Insert   Update   Query   Delete	
---------------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD						            #
CO_PTMST       PT_PRTTP,PT_PRTCD									            #
CO_TXDOC       TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD      #              #      
FG_ISTRN       IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDCD,
			   IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PKGTP,
               IST_MNLCD                                         #              #
FG_STMST       ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,
               ST_PKGTP,ST_MNLCD                                                #
MR_DOTRN       DOT_MKTTP,DOT_DORNO,DOT_PRDCD,DOT_PKGTP                          #
MR_INMST       IN_MKTTP,IN_INDNO									            #
MR_INTRN       INT_MKTTP,INT_INDNO,INT_PRDCD,INT_PKGTP                          #
MR_IVTRN       IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP           #              # 
--------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtINVNO    IVT_INVNO       MR_IVTRN      VARCHAR(8)     Invoice Number
txtDORNO    DOT_DORNO       MR_DOTRN      VARCHAR(8)     D.O. Number
--------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
	For Insert new Invoice Details 
		1)Most of the data come from MR_DOTRN,MR_INMST,MR_INTRN  on this condition
			1)DOT_MKTTP=IN_MKTTP
            2)AND DOT_INDNO=IN_INDNO
            3)AND INT_MKTTP=DOT_MKTTP 
            4)AND INT_INDNO=DOT_INDNO
            5)AND INT_PRDCD=DOT_PRDCD
            6)AND INT_PKGTP=DOT_PKGTP
            7)AND DOT_DORNO=given D.O. Number

   <B>To Delete Records : </B>
		For the effect of deletation of Invoice details from table QC_SMTRN are marked as 'X' for
   
       Invoice will be deleted on Invoice number condition and Status flag will be 'X'
			1)IVT_STSFL='X'
            2)TX_STSFL='X'
            3)IS_STSFL='X'
       and Quantity of Stock master will be increased as per issue Lot Quantity
       

   <Validation>
      1)Invoice Quantity can't be more then Balance D.O. Quantity
      2)Issue Lot Quantity can't be more then Stock Quantity.
      3)Sum of Issue Lot Quantity can't be more then Invoice Quantity.

	
 */


class mr_teinv extends cl_pbase
{
	private JTextField txtBYRCD,txtBYRNM,txtDSRCD,txtDSRNM,txtTRPCD,txtTRPNM,txtINVNO,txtINVDT,txtSALTP,txtSTPDS,txtPORNO,txtDORNO,txtDORDT;
	private JTextField txtLR_NO,txtLR_DT,txtLRYNO,txtCNTDS,txtDSTDS,txtDSTCD;
	private JTextField txtTINNO,txtECCNO,txtPORDT,txtCNSCD,txtCNSNM;
	private JTextField txtCAD01,txtCAD02,txtCAD03;
	private JTextField txtTSLNO,txtRSLNO,txtADLNO,txtADLDT;
	private JTextField txtINVQT,txtGRAD1,txtLOTQT,txtINVVL;
	
	private cl_JTable tblGRDTL,tblCOTAX,tblGRTAX,tblLTDTL;    
	private TBLINPVF objTBLVRF; 
	private JCheckBox chkCHKFL;
    private JComboBox cmbMKTTP;	

	private JButton btnPROCS;       /** JButton Variable for Tax Process   */
	private JTabbedPane tbpMAIN;	/**Panel for Common Details	 */
	private JPanel pnlCODTL;		/**Panel for common tax details	 */
	private JPanel pnlCOTAX;		/**Panel for grade wise details	 */
	private JPanel pnlGRDTL;		/**Panel for grade wise tax details	 */
	private JPanel pnlGRTAX;        /**Panel for Invoice Details  */
	private JPanel pnlINVTL;        /**Panel for Lot Details */
	private JPanel pnlLTDTL; 
	
	
	private final int TB1_CHKFL=0;
	private final int TB1_GRADE=1;
	private final int TB1_BALQT=2;
	private final int TB1_INVQT=3;
	private final int TB1_RATE= 4;
	private final int TB1_PKGWT=5;
	private final int TB1_INVPK=6;
	private final int TB1_VALUE=7;
	private final int TB1_NTVAL=8;
	private final int TB1_PKGTP=9;
	private final int TB1_PRDCD=10;
	
	private final int TB2_CHKFL=0;
	private final int TB2_TAXCD=1;
	private final int TB2_TAXDS=2;
	private final int TB2_TAXVL=3;
	private final int TB2_TAXFL=4;
	private final int TB2_TAXPS=5;

	private final int TB3_CHKFL=0;
	private final int TB3_PRDCD=1;
	private final int TB3_PRDDS=2;
	private final int TB3_TAXCD=3;
	private final int TB3_TAXDS=4;
	private final int TB3_TAXVL=5;
	private final int TB3_TAXFL=6;
	
	private final int TB4_CHKFL=0;
	private final int TB4_PRDCD=1;
	private final int TB4_GRADE=2;
	private final int TB4_LOTNO=3;
	private final int TB4_PKGTP=4;
	private final int TB4_STKQT=5;
	private final int TB4_ISSQT=6;
	private final int TB4_ISSPK=7;
	private final int TB4_MNLCD=8;
	private final int TB4_PKGCT=9;
	private final int TB4_RCLNO=10;
	private final int TB4_PKGWT=11;
	private final int TB4_PRDTP=12;
	
	private String strTEMP;
	private String strSTSFL="1";         /**String Variable for Sataus Flag*/
	private Vector<String> vtrTAXCD;             /**Vector  Variable for  Tax Code */
	private Hashtable<String,String> hstTAXCD;          /**HashTable Variable for tax code */
	private Hashtable<String,String> hstTXCAT;          /**HashTable  Variable for tax Category*/
	private Hashtable<String,String> hstTAXCD1;         /**HashTable Variable for tax code */
	private Hashtable<String,String> hstGRDTL;          /**HashTable Variable for Grade Details*/
	private Hashtable<String,String> hstNEW;          

	private String strMKTTP="01"; /**String Variable for Market Type  */
	private String strINDNO="";   /**String Variable for Indent Number*/
	private String strTSLNO="";   /**String Variable for Transporter seal Number*/
	private String strRSLNO="";   /**String Variable for Railway Seal Number*/
	private String strADLNO="";   /**String Variable for Advance Licence Number*/
	private String strADLDT="";   /**String Variable for Advance Licence date*/
	
	private String strDTPCD="";   /**String Variable for Delivery Type */
	private String strECHRT="";   /**String Variable for  Exchange Rate*/
	private String strTMOCD="";   /**String Variable for mode of Transport */
	private String strCURCD="";   /**String Variable for Currency  code*/
	private String strCNTDS="";   /**String Variable for container Description*/
	private String strCPTVL="";   /**String Variable for Captive value*/
	private String strASSVL="";   /**String Variable for Assesible Value*/
	private String strINVPK="";   /**String Variable for invoice Packages*/
	private String strDSTDS="";   /**String Variable for distination Description*/
	private String strTSTFL="";   /**String Variable for Test Certificate flag*/
	private String strTPRCD="";   /**String Variable for transporter Code */
	private String strZONCD="";   /**String Variable for Zone Code*/
	private String strBKGDT="";   /**String Variable for Order Booking Date*/
	private String strDSRTP="";   /**String Variable for Distributor Type*/
	private String strYOPFL ="";
	private String strYSTDT ="";
	private String strREFDT ="";
	private double dblYOPVL =0.00;
	private double dblITMVL=0.00;    
	private double dblEXGRT=0.0;
	private double dblTXVAL =0.0;
	private double dblBILVL =0.0;
	boolean flgFOUND=false;
	StringTokenizer L_STRTKN;
	private final String strPRDTP="01"; 
	private JLabel lblINVQT ;
	private JLabel lblPKGS ;
	private inpVRFY objINPVR = new inpVRFY();	
	CallableStatement cstPLTRN_INV,rwkPLTRN_PRT;
	private boolean flgLTVLD = true;
	private boolean flgSTVLD = true;
	private String strYREND = "31/03/2009";
    private String strYRDGT = "";
	public mr_teinv()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			tbpMAIN  = new JTabbedPane();
			pnlCODTL = new JPanel(null);
			pnlGRDTL = new JPanel(null);

			pnlCOTAX = new JPanel(null);
			pnlGRTAX = new JPanel(null);
			pnlINVTL = new JPanel(null);
			pnlLTDTL = new JPanel(null);
			
			vtrTAXCD=new Vector<String>(5,1);
			hstTAXCD=new Hashtable<String,String> (5);
			hstTXCAT=new Hashtable<String,String>();
			hstTAXCD1=new Hashtable<String,String>(5);
			hstGRDTL=new Hashtable<String,String>();
			hstNEW=new Hashtable<String,String> (30);

            strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "0" : "9";
					
			flgLTVLD = true;
        	flgSTVLD = true;	
			
			String L_strHSVAL="";
			add(new JLabel("Invoice Type "),1,1,1,1,this,'L');
			add(cmbMKTTP = new JComboBox(),2,1,1,1,this,'L');
		    cmbMKTTP.addItem("01 F.G.");
		    cmbMKTTP.addItem("02 Styrene");
		    cmbMKTTP.addItem("03 Captive Consumption");
		    cmbMKTTP.addItem("09 Other");
		    
	        add(new JLabel("Invoice No. "),3,1,1,1,this,'L');
			add(txtINVNO = new TxtLimit(8),4,1,1,1,this,'L');
			
			add(new JLabel("Invoice Date "),1,2,1,1,this,'L');
			add(txtINVDT = new TxtDate(),2,2,1,1,this,'L');
			
			add(new JLabel("D.O. No. "),1,3,1,1,this,'L');
			add(txtDORNO = new TxtLimit(8),2,3,1,1,this,'L');
			
			add(new JLabel("D.O. Date "),1,4,1,1,this,'L');
			add(txtDORDT = new TxtDate(),2,4,1,1,this,'L');
			
			add(new JLabel("LR No. "),1,5,1,1,this,'L');
			add(txtLR_NO = new TxtLimit(10),2,5,1,1,this,'L');
			
			add(new JLabel("LR Date "),1,6,1,1,this,'L');
			add(txtLR_DT = new TxtDate(),2,6,1,1,this,'L');
			
			add(new JLabel("Lorry No. "),1,7,1,1,this,'L');
			add(txtLRYNO = new TxtLimit(10),2,7,1,1,this,'L');
			
			add(new JLabel("Container No. "),1,8,1,1,this,'L');
			add(txtCNTDS = new TxtLimit(20),2,8,1,1,this,'L');
			
			add(new JLabel("Inv. Value "),3,6,1,1,this,'L');
			add(txtINVVL = new TxtLimit(20),3,7,1,1,this,'L');
			add(btnPROCS = new JButton("Process"),3,8,1,1,this,'L');
			
			add(new JLabel("Sale Type"),1,1,1,1,pnlCODTL,'L');
			add(txtSALTP = new TxtLimit(8),2,1,1,0.5,pnlCODTL,'L');
			add(txtSTPDS = new TxtLimit(20),2,2,1,1.5,pnlCODTL,'R');
			add(new JLabel("Order Ref."),1,3,1,1.5,pnlCODTL,'L');
			add(txtPORNO = new TxtLimit(8),2,3,1,1.5,pnlCODTL,'L');
			
			add(new JLabel("Ord.Ref Date"),1,5,1,1,pnlCODTL,'L');
			add(txtPORDT = new TxtDate(),2,5,1,1,pnlCODTL,'L');
			
			add(new JLabel("Destination"),1,6,1,1,pnlCODTL,'L');
			add(txtDSTCD = new TxtLimit(10),2,6,1,.5,pnlCODTL,'L');
			add(txtDSTDS = new TxtLimit(10),2,7,1,1.5,pnlCODTL,'R');

			add(new JLabel("Code "),3,2,1,1,pnlCODTL,'L');
			add(new JLabel("Name "),3,3,1,1,pnlCODTL,'L');
			//add(new JLabel("Consignee Name "),4,4,1,2,pnlCODTL,'L');
			add(new JLabel("Distributer"),4,1,1,1,pnlCODTL,'L');
			add(txtDSRCD = new TxtLimit(5),4,2,1,1,pnlCODTL,'L');
			add(txtDSRNM = new TxtLimit(40),4,3,1,3,pnlCODTL,'L');
			
			add(new JLabel("Buyer"),5,1,1,1,pnlCODTL,'L');
			add(txtBYRCD = new TxtLimit(5),5,2,1,1,pnlCODTL,'L');
			add(txtBYRNM = new TxtLimit(40),5,3,1,3,pnlCODTL,'L');
			
			add(new JLabel("Transporter"),6,1,1,1,pnlCODTL,'L');
			add(txtTRPCD = new TxtLimit(5),6,2,1,1,pnlCODTL,'L');
			add(txtTRPNM = new TxtLimit(40),6,3,1,3,pnlCODTL,'L');
			
			add(new JLabel("Consignee"),7,1,1,1,pnlCODTL,'L');
			add(txtCNSCD = new TxtLimit(5),7,2,1,1,pnlCODTL,'L');
			add(txtCNSNM = new TxtLimit(40),7,3,1,3,pnlCODTL,'L');
			
			add(new JLabel("Ecc Number"),7,6,1,1,pnlCODTL,'L');
			add(txtECCNO = new TxtLimit(20),7,7,1,1.5,pnlCODTL,'L');

			add(new JLabel("Tin Number"),8,6,1,1,pnlCODTL,'L');
			add(txtTINNO = new TxtLimit(20),8,7,1,1.5,pnlCODTL,'L');

			add(new JLabel("Address"),8,1,1,1,pnlCODTL,'L');
			add(txtCAD01 = new TxtLimit(30),8,2,1,4,pnlCODTL,'L');
			add(txtCAD02 = new TxtLimit(30),9,2,1,4,pnlCODTL,'L');
			add(txtCAD03 = new TxtLimit(30),10,2,1,4,pnlCODTL,'L');
					
			add(new JLabel("Trp.Seal No."),1,1,1,1,pnlINVTL,'L');
			add(txtTSLNO = new TxtLimit(10),2,1,1,1,pnlINVTL,'L');
			add(new JLabel("Rly.Seal No."),1,2,1,1,pnlINVTL,'L');
			add(txtRSLNO = new TxtLimit(10),2,2,1,1,pnlINVTL,'L');
			add(new JLabel("Adv. Lic. No."),1,3,1,1,pnlINVTL,'L');
			add(txtADLNO = new TxtLimit(10),2,3,1,1,pnlINVTL,'L');
			add(new JLabel("Adv. Lic. Date"),1,4,1,1,pnlINVTL,'L');
			add(txtADLDT = new TxtDate(),2,4,1,1,pnlINVTL,'L');
						
			String[] L_strTBLHD1 = {"FL","Grade","Bal.Qty","Inv Qty","Rate ","Pkg Wt.","No.of Pkg","Value","Net Value","Pkg Type","Prd. Code"};
			int[] L_intCOLSZ1 = {20,90,80,80,80,80,100,100,100,60,80};
			tblGRDTL = crtTBLPNL1(pnlGRDTL,L_strTBLHD1,50,1,1,9,7.7,L_intCOLSZ1,new int[]{0});
			tblCOTAX=crtTBLPNL1(pnlCOTAX,new String[]{"FL","Code","Description","Value","Amt./Percent","Tax Value"},20,1,1,7,7.7,new int[]{20,100,220,120,100,150},new int[]{0});
			tblGRTAX=crtTBLPNL1(pnlGRTAX,new String[]{"FL","Prd.Cd.","Grade","Tax Cd.","Description","Value","Amt/Prct","Tax Value"},20,1,1,7,7.7,new int[]{20,90,100,60,180,100,90,80},new int[]{0});
			
			tblLTDTL=crtTBLPNL1(pnlLTDTL,new String[]{"FL","Prd. Code","Grade","Lot No","Pkt Type","Stk. Qty.","Lot Qty","No.of Pkg","Main Loc","Pkg Category","Rcl. No","Pkg WT","Prd. Type"},200,1,1,7,7.7,new int[]{20,100,90,90,90,90,80,80,80,80,80,80,80,60},new int[]{0});
			add(lblINVQT = new JLabel("  "),10,5,1,1,pnlLTDTL,'L');
			add(lblPKGS = new JLabel("  "),10,6,1,1,pnlLTDTL,'L');				
		
			tbpMAIN.add("Common Details",pnlCODTL);
			tbpMAIN.add("Grade Details",pnlGRDTL);
			tbpMAIN.add("Common Tax",pnlCOTAX);
			tbpMAIN.add("Gradewise Tax",pnlGRTAX);
			tbpMAIN.add("Invoice Details",pnlINVTL);
			tbpMAIN.add("Lot Details",pnlLTDTL);
			
			add(tbpMAIN,5,1,12,7.8,this,'L');

			objTBLVRF = new TBLINPVF();
			tblGRDTL.setInputVerifier(objTBLVRF);
			tblLTDTL.setInputVerifier(objTBLVRF);
			
			
			tblGRDTL.addFocusListener(this);
			tblGRDTL.addKeyListener(this);
			tblLTDTL.addFocusListener(this);
			tblLTDTL.addKeyListener(this);
			
			//tblGRDTL.setInputVerifier(new TBLINPVF());
			//tblGRDTL.addKeyListener(this);
			tblGRDTL.setCellEditor(TB1_CHKFL,chkCHKFL = new JCheckBox());
			tblGRDTL.setCellEditor(TB1_INVQT,txtINVQT = new TxtLimit(8));
			tblLTDTL.setCellEditor(TB4_PRDCD,txtGRAD1 = new TxtLimit(10));
			tblLTDTL.setCellEditor(TB4_ISSQT,txtLOTQT = new TxtLimit(10));
						
		//	tblLODDTL.setCellEditor(TB1_RCLNO,txtRCLNO1 = new TxtLimit(2));
			chkCHKFL.addKeyListener(this);
			txtINVQT.addKeyListener(this);
			txtINVQT.addFocusListener(this);
			
			txtGRAD1.addKeyListener(this);
			txtGRAD1.addFocusListener(this);
			
			M_strSQLQRY = " SELECT * FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXTAX' AND CMT_CHP01='01' order by CMT_CCSVL,CMT_NMP01";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_strHSVAL = nvlSTRVL(M_rstRSSET.getString("CMT_NMP01"),"") ;// Multiplier
				L_strHSVAL += "|"+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"") ;// Multiplier
			    // added
			    L_strHSVAL += "|"+nvlSTRVL(M_rstRSSET.getString("CMT_MODLS")," "+"|") ;// Multiplier
				hstTXCAT.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CHP01"));
				hstTAXCD.put(M_rstRSSET.getString("CMT_CODCD"),L_strHSVAL);
				//vtrTAXCD.addElement(M_rstRSSET.getString("CMT_CODCD"));
				vtrTAXCD.add(M_rstRSSET.getString("CMT_CODCD"));
				hstTAXCD1.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  "),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"  "));
				//vtrTAXTP.addElement(M_rstRSSET.getString("CMT_CHP01"));
			}
			
			
			
			/*M_strSQLQRY = "Select substr(CMT_CODCD,1,3)CMT_CODCD,CMT_CODDS,CMT_CCSVL  from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'COXXTAX' AND CMT_CHP01='01' order by CMT_CCSVL";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					vtrTAXCD.add(M_rstRSSET.getString("CMT_CODCD"));
					hstTAXCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  "),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"  "));
					//hstTAXCD1.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  "),nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"  "));
				}
			}
		*/
		getREFDT();
		txtINVDT.setInputVerifier(objINPVR);	
		cstPLTRN_INV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_INV(?,?,?)");
		rwkPLTRN_PRT = cl_dat.M_conSPDBA_pbst.prepareCall("call rwkPLTRN_PRT(?,?,?,?,?,?)");
			setENBL(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"Constructor");
		}
		System.out.println(cl_dat.M_strFNNYR1_pbst.substring(3));
	}
	
	/**
	 * Super Class metdhod overrided to inhance its funcationality, to enable disable 
	 * components according to requriement.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		
		super.setENBL(L_flgSTAT);	
		
		txtINVDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
		txtSALTP.setEnabled(false);
		txtSTPDS.setEnabled(false);
		txtPORNO.setEnabled(false);
		txtPORDT.setEnabled(false);
		txtDSTCD.setEnabled(false);
		txtDSRCD.setEnabled(false);
		txtDSRNM.setEnabled(false);
		txtBYRCD.setEnabled(false);
		txtBYRNM.setEnabled(false);
		txtTRPCD.setEnabled(false);
		txtTRPNM.setEnabled(false);
		txtCNSCD.setEnabled(false);
		txtCNSNM.setEnabled(false);
		txtECCNO.setEnabled(false);
		txtTINNO.setEnabled(false);
		txtCAD01.setEnabled(false);
		txtCAD02.setEnabled(false);
		txtCAD03.setEnabled(false);
		txtINVVL.setEnabled(false);
		txtDSTDS.setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_GRADE].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_BALQT].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_RATE].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_PKGWT].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_INVPK].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_VALUE].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_NTVAL].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_PKGTP].setEnabled(false);
		tblGRDTL.cmpEDITR[TB1_PRDCD].setEnabled(false);
		tblCOTAX.setEnabled(false);
		tblGRTAX.setEnabled(false);
		cmbMKTTP.setEnabled(true);
	
		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
		{
			txtINVNO.setEnabled(false);
			txtINVDT.setEnabled(true);
			//txtINVDT.setEnabled(false);
		}
		else
		{
			txtINVNO.setEnabled(true);
			txtINVDT.setEnabled(false);
		}
		for(int i=1;i<=12;i++)
		{
			if(i==1 || i==6)
				tblLTDTL.cmpEDITR[i].setEnabled(true);
			else
				tblLTDTL.cmpEDITR[i].setEnabled(false);
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == btnPROCS)
		{
			dblITMVL=0.00;
			dblEXGRT=0.0;
			dblTXVAL =0.0;
			dblBILVL =0.0;
			
			if(tblGRDTL.isEditing())
				tblGRDTL.getCellEditor().stopCellEditing();
			tblGRDTL.setColumnSelectionInterval(0,0);
			tblGRDTL.setRowSelectionInterval(0,0);
			calINVVL();
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
		    {
		         txtINVDT.setText(strREFDT);
		         cmbMKTTP.requestFocus();
		         //txtINVDT.requestFocus();
		    }
		    else
		        cmbMKTTP.requestFocus();
		       // txtINVNO.requestFocus();
		}
		/*if(M_objSOURC == cmbMKTTP)
		{
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
		        txtINVDT.requestFocus();
		    else
		        txtINVNO.requestFocus();       
		}*/
		
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
		
			
			if(M_objSOURC==txtINVNO)
			{
				M_strHLPFLD="txtINVNO";
				String L_ARRHDR[] ={"Invoice No.","Invoice Date"};
				M_strSQLQRY = "Select distinct IVT_INVNO,CONVERT(varchar,IVT_INVDT,103) from MR_IVTRN where ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(IVT_STSFL,'')<>'X' ";  // DOT_STSFL in  ('A','H','1') AND ";
				//M_strSQLQRY += " (isnull(DOT_DORQT,0)>isnull(DOT_LADQT,0)) ";
				if(txtINVNO.getText().trim().length() >0)
					M_strSQLQRY += " AND  IVT_INVNO like '"+ txtINVNO.getText().trim() + "%' ";
				M_strSQLQRY += " AND IVT_MKTTP ='"+ cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'";			
				M_strSQLQRY += " order by IVT_INVNO desc";
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");            
			}
			
			if(M_objSOURC==txtDORNO)
			{
				M_strHLPFLD="txtDORNO";
				String L_ARRHDR[] ={"D.O. No.","Date","Order No."};
				M_strSQLQRY = "Select distinct DOT_DORNO,DOT_DORDT,DOT_INDNO from MR_DOTRN where DOT_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and DOT_STSFL ='1' AND ";
				M_strSQLQRY += " (isnull(DOT_DORQT,0)>isnull(DOT_LADQT,0)) ";
				if(txtDORNO.getText().trim().length() >0)
					M_strSQLQRY += " and DOT_DORNO like '" + txtDORNO.getText().trim() + "%' ";
			    M_strSQLQRY += " AND DOT_MKTTP ='"+ cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'";		
				M_strSQLQRY += " order by DOT_INDNO ";
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");            
			}
			if(M_objSOURC==txtGRAD1)
			{
				M_strHLPFLD="txtGRAD1";
				String L_strCONDT="";
				for(int i=0;i<tblGRDTL.getRowCount();i++)
				{
					if(tblGRDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						L_strCONDT+="'"+tblGRDTL.getValueAt(i,TB1_PRDCD).toString()+ tblGRDTL.getValueAt(i,TB1_PKGTP).toString()+"',";
					}
				}
				L_strCONDT=L_strCONDT.substring(0,L_strCONDT.length()-1);
				//M_strSQLQRY="Select ST_PRDTP,ST_PRDCD,ST_PKGTP,ST_LOTNO,ST_MNLCD,ST_PKGTP,ST_PKGCT,ST_RCLNO";
				M_strSQLQRY="Select ST_PRDCD,PR_PRDDS,ST_LOTNO,ST_PKGTP,ST_STKQT,ST_MNLCD,ST_PKGCT,ST_RCLNO,ST_PKGWT ";
				M_strSQLQRY +=" from CO_PRMST,FG_STMST where PR_PRDCD=ST_PRDCD AND PR_PRDTP=ST_PRDTP";
				M_strSQLQRY +="	AND ST_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ST_PRDTP='"+strPRDTP+"' AND ST_PRDCD + ST_PKGTP in("+L_strCONDT+")";
				M_strSQLQRY +=" AND isnull(ST_STKQT,0) >0 ORDER BY ST_PRDCD,ST_LOTNO";
				//if(txtGRAD1.getText().trim().length() >0)
				//	M_strSQLQRY += " and DOT_DORNO like '" + txtGRAD1.getText().trim() + "%' ";
				//M_strSQLQRY += " order by DOT_DORDT desc";
				System.out.println(M_strSQLQRY);
				int []  L_inaCOLSZ = new int[]{100,100,150,80,100,80,80,50,50};
				cl_hlp(M_strSQLQRY,3,1,new String[]{"Prd Desc","Prd Code","Lot No","Pkg Type","Stk Qty","Main Loc","Pkg Ctg.","Rcl No","Pkg WT"},9,"CT",L_inaCOLSZ);    
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			if(M_objSOURC==txtINVNO)
				getDATA();

			if(M_objSOURC==txtINVDT)
				txtDORNO.requestFocus();
			if(M_objSOURC==txtDORNO)
				dspDATA();
			if(M_objSOURC==txtDORNO)
				txtLR_NO.requestFocus();
			if(M_objSOURC==txtLR_NO)
				txtLR_DT.requestFocus();
			if(M_objSOURC==txtLR_DT)
				txtLRYNO.requestFocus();
			if(M_objSOURC==txtLRYNO)
			{
				txtLRYNO.setText(txtLRYNO.getText().toUpperCase());
				txtCNTDS.requestFocus();
			}
			if(M_objSOURC==txtTSLNO)
				txtRSLNO.requestFocus();
			if(M_objSOURC==txtRSLNO)
				txtADLNO.requestFocus();
			if(M_objSOURC==txtADLNO)
				txtADLDT.requestFocus();
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
		    String L_strLOTNO ="";
			//setMSG("",'N');
			
			if(M_strHLPFLD.equals("txtINVNO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtINVNO.setText(L_STRTKN1.nextToken());
				txtINVDT.setText(L_STRTKN1.nextToken());
			}
			
			if(M_strHLPFLD.equals("txtDORNO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtDORNO.setText(L_STRTKN1.nextToken());
				txtDORDT.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtGRAD1"))
			{			
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				//L_STRTKN.nextToken();
				txtGRAD1.setText(L_STRTKN.nextToken());
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_GRADE);
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_LOTNO);
				L_strLOTNO = tblLTDTL.getValueAt(tblLTDTL.getSelectedRow(),TB4_LOTNO).toString();
				for(int i=0;i<=tblLTDTL.getSelectedRow()-1;i++)
        		{
        			if(tblLTDTL.getValueAt(i,TB4_LOTNO).toString().trim().length() >0)
        			if(tblLTDTL.getValueAt(i,TB4_LOTNO).toString().trim().equals(L_strLOTNO.trim()))
        			{
        			    JOptionPane.showMessageDialog(this,"Duplicate Lot No :  "+L_strLOTNO,"Message",JOptionPane.INFORMATION_MESSAGE);
        				setMSG("Duplicate Lot No ..",'E');
        				return ;
        			}
        		}
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_PKGTP);
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_STKQT);
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_MNLCD);
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_PKGCT);
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_RCLNO);
				tblLTDTL.setValueAt(L_STRTKN.nextToken().toString(),tblLTDTL.getSelectedRow(),TB4_PKGWT);
				tblLTDTL.setValueAt(strPRDTP,tblLTDTL.getSelectedRow(),TB4_PRDTP);
				
				
				//tblLTDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim(),tblLTDTL.getSelectedRow(),TB4_GRADE);
				//txtINVNO.setText(L_STRTKN.nextToken());
				//System.out.println("LLLLLLLLL"+L_STRTKN.nextToken());
				//tblLTDTL.setValueAt(L_STRTKN.nextToken(),tblLTDTL.getSelectedRow(),TB4_PRDDS);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	private void dspDATA()
	{
		try
		{
			int i=0;
			
			tblGRDTL.clrTABLE();
			tblLTDTL.clrTABLE();
			tblCOTAX.clrTABLE();
			tblGRTAX.clrTABLE();
			
			M_strSQLQRY="Select IN_MKTTP,IN_INDNO,IN_SALTP,IN_PORNO,IN_PORDT,IN_DSTCD,IN_DSRCD,IN_DSRNM,IN_BYRCD,IN_BYRNM, ";
			M_strSQLQRY +="IN_TRPCD,IN_TRPNM,IN_CNSCD,IN_CNSNM,IN_CPTVL,IN_DTPCD,";
			M_strSQLQRY +="DOT_TMOCD,IN_ECHRT,IN_BKGDT,IN_CURCD,IN_DSRTP,";
			M_strSQLQRY +="DOT_PRDCD,DOT_PRDDS,(isnull(DOT_DORQT,0)-isnull(DOT_LADQT,0))DOT_BALQT,DOT_DORDT ";
			M_strSQLQRY +=",DOT_PKGTP,DOT_DORPK,DOT_PKGWT,DOT_TRPCD,INT_BASRT,DOT_STSFL from MR_DOTRN,MR_INMST,MR_INTRN where DOT_CMPCD = IN_CMPCD and DOT_MKTTP=IN_MKTTP AND ";
			M_strSQLQRY +=" DOT_INDNO=IN_INDNO AND INT_CMPCD=DOT_CMPCD and INT_MKTTP=DOT_MKTTP AND INT_INDNO=DOT_INDNO AND INT_PRDCD=DOT_PRDCD AND INT_PKGTP=DOT_PKGTP  AND DOT_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and DOT_DORNO='"+txtDORNO.getText().trim()+"' AND DOT_STSFL <> 'X'";// AND DOT_STSFL ='1'";
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			txtTRPCD.setText("");
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
				    /*if((!nvlSTRVL(M_rstRSSET.getString("DOT_STSFL"),"").equals("1"))||(!nvlSTRVL(M_rstRSSET.getString("DOT_STSFL"),"").equals("A"))||(!nvlSTRVL(M_rstRSSET.getString("DOT_STSFL"),"").equals("1")))
				    {
				        setMSG("D.O. is not authorised..",'E');
				        return; 
				    }*/
				    if(!nvlSTRVL(M_rstRSSET.getString("DOT_STSFL"),"").equals("1"))
				    {
				        setMSG("D.O. is not authorised..",'E');
				        return; 
				    }
				  	strMKTTP=nvlSTRVL(M_rstRSSET.getString("IN_MKTTP"),"");
					strDTPCD=nvlSTRVL(M_rstRSSET.getString("IN_DTPCD"),"");
					strTMOCD=nvlSTRVL(M_rstRSSET.getString("DOT_TMOCD"),"");
					strECHRT=nvlSTRVL(M_rstRSSET.getString("IN_ECHRT"),"");
					if(M_rstRSSET.getDate("IN_BKGDT") !=null)
						strBKGDT= M_fmtLCDAT.format(M_rstRSSET.getDate("IN_BKGDT"));
					else strBKGDT ="";
					//System.out.println("strBKGDT " +strBKGDT);
					strCURCD=nvlSTRVL(M_rstRSSET.getString("IN_CURCD"),"");
					strCPTVL=nvlSTRVL(M_rstRSSET.getString("IN_CPTVL"),"");
					strDSRTP=nvlSTRVL(M_rstRSSET.getString("IN_DSRTP"),"");
					
					strINDNO=nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),"");
					txtDORDT.setText(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("DOT_DORDT")).toString(),""));
                    txtSALTP.setText(nvlSTRVL(M_rstRSSET.getString("in_saltp"),""));					
///   txtSALTP.setText(cl_dat.getPRMCOD("CMT_CODDS","SYS","MRXXSAL",nvlSTRVL(M_rstRSSET.getString("IN_SALTP"),"")));
					if(i ==0)
					{
					    M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' AND CMT_CGSTP='MR00SAL' AND CMT_CCSVL='"+nvlSTRVL(M_rstRSSET.getString("IN_SALTP"),"")+"'";
            			ResultSet L_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
            			if(L_rstRSSET!=null && L_rstRSSET.next())
            			{
            				txtSTPDS.setText(nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
            				L_rstRSSET.close();
            			}
					}
					txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("IN_PORNO"),""));
					txtPORDT.setText(nvlSTRVL(M_rstRSSET.getString("IN_PORDT"),""));
					txtTRPCD.setText(nvlSTRVL(M_rstRSSET.getString("DOT_TRPCD"),""));
					//txtTRPNM.setText(nvlSTRVL(M_rstRSSET.getString("IN_TRPNM"),""));
					txtDSTCD.setText(nvlSTRVL(M_rstRSSET.getString("IN_DSTCD"),""));
					txtDSRCD.setText(nvlSTRVL(M_rstRSSET.getString("IN_DSRCD"),""));
					txtDSRNM.setText(nvlSTRVL(M_rstRSSET.getString("IN_DSRNM"),""));
					txtBYRCD.setText(nvlSTRVL(M_rstRSSET.getString("IN_BYRCD"),""));
					txtBYRNM.setText(nvlSTRVL(M_rstRSSET.getString("IN_BYRNM"),""));
					
					txtCNSCD.setText(nvlSTRVL(M_rstRSSET.getString("IN_CNSCD"),""));
					txtCNSNM.setText(nvlSTRVL(M_rstRSSET.getString("IN_CNSNM"),""));
					
					tblGRDTL.setValueAt(M_rstRSSET.getString("DOT_PRDDS"),i,TB1_GRADE);
					tblGRDTL.setValueAt(M_rstRSSET.getString("DOT_BALQT"),i,TB1_BALQT);
					tblGRDTL.setValueAt(M_rstRSSET.getString("INT_BASRT"),i,TB1_RATE);
					tblGRDTL.setValueAt(M_rstRSSET.getString("DOT_PKGWT"),i,TB1_PKGWT);
					//tblGRDTL.setValueAt(M_rstRSSET.getString("DOT_DORPK"),i,TB1_NOPKG);
					tblGRDTL.setValueAt(M_rstRSSET.getString("DOT_PKGTP"),i,TB1_PKGTP);
					tblGRDTL.setValueAt(M_rstRSSET.getString("DOT_PRDCD"),i,TB1_PRDCD);
					//txtPORDT.setText(nvlSTRVL(M_rstRSSET.getString("IN_PORDT"),""));
					i++;
					
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			M_strSQLQRY="Select PT_PRTNM from CO_PTMST where PT_PRTTP='T' AND PT_PRTCD='"+txtTRPCD.getText().trim()+"'";
            			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
            			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
            			if(M_rstRSSET!=null && M_rstRSSET.next())
            			{
            				txtTRPNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
            			}
            			if(M_rstRSSET!=null)
            				M_rstRSSET.close();
			M_strSQLQRY="Select PT_TSTFL,PT_ADR01,PT_ADR02,PT_ADR03,PT_ECCNO,PT_TINNO,PT_YOPVL,PT_YSTDT,PT_YOPFL from CO_PTMST where PT_PRTTP='C' AND PT_PRTCD='"+txtCNSCD.getText().trim()+"'";
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				txtCAD01.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""));
				txtCAD02.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),""));
				txtCAD03.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),""));
				txtECCNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_ECCNO"),""));
				txtTINNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_TINNO"),""));
				strTSTFL=nvlSTRVL(M_rstRSSET.getString("PT_TSTFL"),"");
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			
			M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' AND CMT_CGSTP='COXXDST' AND CMT_CODCD='"+txtDSTCD.getText().trim()+"'";
			M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				txtDSTDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			getCOTAX(strINDNO,"IND");

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspDATA");
		}
	}
	
	public void getCOTAX(String P_strDOCNO,String P_strDOCTP)
	{
		try
		{
			String L_strTAXCD="";
			String L_strTAXFL="";
			String L_strTAXVL="";
			String L_strPRDCD="";								
			M_strSQLQRY="select * from CO_TXDOC where TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_DOCNO='"+P_strDOCNO+"' AND TX_DOCTP='"+P_strDOCTP+"'  AND isnull(TX_STSFL,'') <> 'X'";
    		M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
    		if(M_rstRSSET!=null)
    		{
    			int i=0;
    			int intCOMTX=0;
    			while(M_rstRSSET.next())
    			{
    				for(int j=0;j<vtrTAXCD.size();j++)
    				{		
    					L_strTAXCD=(String)vtrTAXCD.get(j);
    					L_strTAXFL="TX_"+L_strTAXCD+"FL";
						//System.out.println("L_strTAXFL = "+L_strTAXFL);
    					String L_strTAXFL1=nvlSTRVL(M_rstRSSET.getString(L_strTAXFL),"null");
						//System.out.println("L_strTAXFL1 = "+L_strTAXFL1);
    					if(!L_strTAXFL1.equals("null"))
    					{
							//tblCOTAX.cmpEDITR[TB2_CHKFL].setEnabled(true);
							L_strTAXVL="TX_"+L_strTAXCD+"VL";
							L_strPRDCD=nvlSTRVL(M_rstRSSET.getString("TX_PRDCD"),"null");
							//System.out.println("L_strPRDCD = "+L_strPRDCD);
							if(!L_strPRDCD.equals("null")&& !L_strPRDCD.equals("XXXXXXXXXX") )
							{
								//System.out.println("L_strPRDCD = "+L_strPRDCD);
								tblGRTAX.setValueAt(new Boolean(true),i,TB2_CHKFL);
								tblGRTAX.setValueAt(L_strPRDCD,i,TB3_PRDCD);
								tblGRTAX.setValueAt(L_strTAXCD,i,TB3_TAXCD);
								tblGRTAX.setValueAt((String)hstTAXCD.get(L_strTAXCD),i,TB3_TAXDS);
								tblGRTAX.setValueAt(M_rstRSSET.getString(L_strTAXVL),i,TB3_TAXFL);
								tblGRTAX.setValueAt(L_strTAXFL1,i,TB3_TAXVL);
							}
							else
							{
    							tblCOTAX.setValueAt(new Boolean(true),i,TB2_CHKFL);
    							tblCOTAX.setValueAt(L_strTAXCD,i,TB2_TAXCD);
								tblCOTAX.setValueAt((String)hstTAXCD1.get(L_strTAXCD),i,TB2_TAXDS);
								tblCOTAX.setValueAt(M_rstRSSET.getString(L_strTAXVL),i,TB2_TAXVL);
								tblCOTAX.setValueAt(L_strTAXFL1,i,TB2_TAXFL);
								i++;
							}
    					}
					}
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCOTAX");
		}
	
	}
	
	public boolean vldDATA()
	{
		try
		{
			flgLTVLD = true;
        	flgSTVLD = true;
        	if(cmbMKTTP.getSelectedItem().toString().substring(0,2).equals("02"))
        	{
        	   	flgLTVLD = false;
            	flgSTVLD = false;
        	}
        	String L_strPRDCD="";
			String L_strPRDCD1="";
			String L_strLTQTY="";
			String L_strINVQT="";
			double L_dblINVQT=0.0;
			double L_dblLTQTY=0.0;
			int L_intCOUNT=0;
			hstGRDTL.clear();
		    setMSG("",'N');
			strTEMP=txtINVVL.getText().trim();
			if(strTEMP.length()==0)
			{
				setMSG("Please Click on Process Button for Tax Calculation ",'E');
				return false;
			}
			if(txtINVDT.getText().trim().length()==0)
			{
					setMSG("Please Enter Invoice Date ..",'E');
					txtINVDT.requestFocus();
					return false;
			}
			if(M_fmtLCDAT.parse(txtINVDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Invoice Date can not be greater than today's date..",'E');
				txtINVDT.requestFocus();
				return false;
			}
			
			//strTEMP=txtINVNO.getText().trim();
			//if(strTEMP.length()==0)
			//{
			//	setMSG("Invoice Number can't be Blank",'E');
			//	return false;
			//}
			
			/*strTEMP=txtLR_NO.getText().trim();
			if(strTEMP.length()==0)
			{
				setMSG("LR No can't be Blank",'E');
				return false;
			}
		
			strTEMP=txtLR_DT.getText().trim();
			if(strTEMP.length()==0)
			{
				setMSG("LR Date can't be Blank",'E');
				return false;
			}*/
		
			strTEMP=txtLRYNO.getText().trim();
			if(strTEMP.length()==0)
			{
				setMSG("Lorry No can't be Blank",'E');
				return false;
			}
		
			isEDIT();
			for(int i=0;i<tblGRDTL.getRowCount();i++)
			{
				if(tblGRDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					hstGRDTL.put(tblGRDTL.getValueAt(i,TB1_PRDCD).toString(),tblGRDTL.getValueAt(i,TB1_INVQT).toString());			
					L_intCOUNT++;
					
				}
			}
		
			if(L_intCOUNT==0)
				return false;
			L_intCOUNT=0;
			Enumeration enmCODKEYS=hstGRDTL.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				L_strPRDCD = (String)enmCODKEYS.nextElement();
				L_strINVQT=hstGRDTL.get(L_strPRDCD).toString();
				L_dblINVQT=Double.parseDouble(L_strINVQT);
				L_dblLTQTY=0.0;
				for(int i=0;i<tblLTDTL.getRowCount();i++)
				{
					if(tblLTDTL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
					{
						L_strPRDCD1=tblLTDTL.getValueAt(i,TB4_PRDCD).toString();
						if(L_strPRDCD1.equals(L_strPRDCD))
						{
							L_strLTQTY=tblLTDTL.getValueAt(i,TB4_ISSQT).toString();
							L_dblLTQTY+=Double.parseDouble(L_strLTQTY);
							L_intCOUNT++;
						}
					}
				}
				if(flgLTVLD)
				{
    				if(L_dblINVQT!=Double.parseDouble(setNumberFormat(L_dblLTQTY,3)))
    				{
    					setMSG("Invoice Quantity and Lot Quantity is not equals",'E');
    					return false;
    				}
				}
			}
			if((L_intCOUNT==0) && (flgLTVLD)) // Lot validation is applicable
			{
				setMSG("Please Enter Lot Quantity",'E');
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	
	public void isEDIT()
	{
		if(tblGRDTL.isEditing())
			tblGRDTL.getCellEditor().stopCellEditing();
		tblGRDTL.setColumnSelectionInterval(0,0);
		tblGRDTL.setRowSelectionInterval(0,0);
			
		if(tblCOTAX.isEditing())
			tblCOTAX.getCellEditor().stopCellEditing();
		tblCOTAX.setColumnSelectionInterval(0,0);
		tblCOTAX.setRowSelectionInterval(0,0);
			
		if(tblGRTAX.isEditing())
			tblGRTAX.getCellEditor().stopCellEditing();
		tblGRTAX.setColumnSelectionInterval(0,0);
		tblGRTAX.setRowSelectionInterval(0,0);
			
		if(tblLTDTL.isEditing())
			tblLTDTL.getCellEditor().stopCellEditing();
		tblLTDTL.setColumnSelectionInterval(0,0);
		tblLTDTL.setRowSelectionInterval(0,0);
	}
	
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	*/
	void exeSAVE()
	{
		try
		{
			
			String L_strINVNO="";
			cl_dat.M_flgLCUPD_pbst = true;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				if(!delINVIC())
				    return;
				//System.out.println("cl_dat.M_flgLCUPD_pbst = "+cl_dat.M_flgLCUPD_pbst);
				if(!cl_dat.M_flgLCUPD_pbst)
				 	return;
			}
			else
			{
				if(!vldDATA())
					return ;
				genINVNO();
				//System.out.println("INNNNNNNN = "+txtINVVL.getText().trim());
				M_calLOCAL.setTime(M_fmtLCDAT.parse(txtINVDT.getText().trim()));       
				M_calLOCAL.add(Calendar.DATE,Integer.parseInt(nvlSTRVL(strCPTVL,"0")));                     
				String strPMDDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   
				//System.out.println("1");
				for(int i=0;i<tblGRDTL.getRowCount();i++)
				{
					if(tblGRDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY=" INSERT INTO MR_IVTRN (";
						M_strSQLQRY +="IVT_CMPCD,IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP,IVT_LADDT,";
						M_strSQLQRY +="IVT_DORNO,IVT_INDNO,IVT_INVNO,IVT_INVDT,IVT_SALTP,";
						M_strSQLQRY +="IVT_CNSCD,IVT_BYRCD,IVT_DSRCD,IVT_TMOCD,IVT_CURCD,";
						M_strSQLQRY +="IVT_ECHRT,IVT_LRYNO,IVT_CNTDS,IVT_LR_NO,IVT_LR_DT,";
						M_strSQLQRY +="IVT_PMDDT,IVT_CPTVL,IVT_ASSVL,IVT_INVRT,IVT_PKGWT,";
						M_strSQLQRY +="IVT_LADQT,IVT_REQQT,IVT_INVQT,IVT_INVPK,IVT_DSTDS,";
						M_strSQLQRY +="IVT_DTPCD,IVT_TSTFL,IVT_TSLNO,IVT_RSLNO,IVT_ADLNO,";
						M_strSQLQRY +="IVT_ADLDT,IVT_PRDDS,IVT_PRDTP,IVT_UOMCD,IVT_DORDT,";
						M_strSQLQRY +="IVT_LODDT,IVT_TRPCD,IVT_PORNO,IVT_PORDT,IVT_ZONCD,";
						M_strSQLQRY +="IVT_INVVL,IVT_NETVL,IVT_EPIFL,IVT_BKGDT,IVT_DSRTP,";
						M_strSQLQRY +="IVT_EXCVL,IVT_EDCVL,IVT_EHCVL,IVT_CVDVL,IVT_ACVVL,";
						M_strSQLQRY +="IVT_TRNFL,IVT_STSFL,IVT_LUSBY,IVT_LUPDT)";
						//M_strSQLQRY +="IVT_LUSBY,IVT_LUPDT)";
						
						M_strSQLQRY +=" Values ('"+cl_dat.M_strCMPCD_pbst+"','"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"',";
						M_strSQLQRY +="'"+txtINVNO.getText().trim()+"',";
						M_strSQLQRY +="'"+tblGRDTL.getValueAt(i,TB1_PRDCD).toString() +"',";
						M_strSQLQRY +="'"+tblGRDTL.getValueAt(i,TB1_PKGTP).toString() +"',";	
						M_strSQLQRY +="'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtINVDT.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',"; //Time stamp
						
						M_strSQLQRY +="'"+txtDORNO.getText().trim()+"',";
						M_strSQLQRY +="'"+strINDNO+"',";
						M_strSQLQRY +="'"+txtINVNO.getText().trim()+"',";
						M_strSQLQRY +="'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtINVDT.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";//Time stamp
						M_strSQLQRY +="'"+txtSALTP.getText().trim()+"',";
						
						M_strSQLQRY +="'"+txtCNSCD.getText().trim()+"',";
						M_strSQLQRY +="'"+txtBYRCD.getText().trim()+"',";
						M_strSQLQRY +="'"+txtDSRCD.getText().trim()+"',";
						M_strSQLQRY +="'"+nvlSTRVL(strTMOCD,"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(strCURCD,"")+"',";
						
						M_strSQLQRY +=nvlSTRVL(strECHRT,"")+",";
						M_strSQLQRY +="'"+nvlSTRVL(txtLRYNO.getText().trim(),"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(txtCNTDS.getText().trim(),"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(txtLR_NO.getText().trim(),"")+"',";
						M_strSQLQRY +=((txtLR_DT.getText().trim().length()==10) ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtLR_DT.getText().trim()))+"'": "null")+", ";// Date Type
						//M_strSQLQRY +=((txtLR_DT.getText().trim().length()==10) ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtLR_DT.getText().trim()))+"'" : "null")+", "; //Date type
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strPMDDT))+"',";
						M_strSQLQRY +=""+nvlSTRVL(strCPTVL,"0")+",";
						M_strSQLQRY +=""+nvlSTRVL(tblGRDTL.getValueAt(i,TB1_VALUE).toString(),"0")+",";
						M_strSQLQRY +=""+nvlSTRVL(tblGRDTL.getValueAt(i,TB1_RATE).toString(),"0")+",";
						M_strSQLQRY +=""+nvlSTRVL(tblGRDTL.getValueAt(i,TB1_PKGWT).toString(),"0")+",";
						
						M_strSQLQRY +=""+nvlSTRVL(tblGRDTL.getValueAt(i,TB1_INVQT).toString(),"0")+",";
						M_strSQLQRY +=""+nvlSTRVL(tblGRDTL.getValueAt(i,TB1_INVQT).toString(),"0")+",";
						M_strSQLQRY +=""+nvlSTRVL(tblGRDTL.getValueAt(i,TB1_INVQT).toString(),"0")+",";
						M_strSQLQRY +=""+nvlSTRVL(tblGRDTL.getValueAt(i,TB1_INVPK).toString(),"0")+",";
						if(txtDSTDS.getText().length() >14)
						    M_strSQLQRY +="'"+nvlSTRVL(txtDSTDS.getText().trim(),"").substring(0,14)+"',";
						else
					    	M_strSQLQRY +="'"+nvlSTRVL(txtDSTDS.getText().trim(),"")+"',";
						
						M_strSQLQRY +="'"+nvlSTRVL(strDTPCD,"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(strTSTFL,"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(txtTSLNO.getText().trim(),"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(txtRSLNO.getText().trim(),"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(txtADLNO.getText().trim(),"")+"',";
						
						M_strSQLQRY +=((txtADLDT.getText().trim().length()==10) ?"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtADLDT.getText().trim()))+"'" : "null")+", ";  //Date Type
						M_strSQLQRY +="'"+tblGRDTL.getValueAt(i,TB1_GRADE).toString() +"',";
						M_strSQLQRY +="'"+strPRDTP+"',";
						M_strSQLQRY +="'MT',";
						M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDORDT.getText().trim())) +"',"; // Date type
												
						M_strSQLQRY +="'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtINVDT.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',"; // Time Stamp
						M_strSQLQRY +="'"+nvlSTRVL(txtTRPCD.getText().trim(),"")+"',";
						M_strSQLQRY +="'"+nvlSTRVL(txtPORNO.getText().trim(),"")+"',"; 
						M_strSQLQRY +=((txtPORDT.getText().trim().length()==10) ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPORDT.getText().trim()))+"'" : "null")+", "; //Date Type
						M_strSQLQRY +="'"+nvlSTRVL(strZONCD,"")+"',";
						
						M_strSQLQRY +=""+txtINVVL.getText().trim()+",";
						M_strSQLQRY +=""+tblGRDTL.getValueAt(i,TB1_NTVAL).toString() +",";						
						M_strSQLQRY +="'',";
						M_strSQLQRY +=((strBKGDT.trim().length()==10) ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strBKGDT))+"'" : "null")+", "; //Date Type
///						M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strBKGDT.trim())) +"',"; // Date type
						M_strSQLQRY +="'"+nvlSTRVL(strDSRTP,"")+"',";
					////////// block added on 06/09/2007 to back update individual values	
						if(hstNEW.containsKey(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|EXC"))
							M_strSQLQRY += hstNEW.get(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|EXC").toString() +",";
						else
							M_strSQLQRY +="0,";
						if(hstNEW.containsKey(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|EDC"))
							M_strSQLQRY += hstNEW.get(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|EDC").toString() +",";
						else
							M_strSQLQRY +="0,";

						if(hstNEW.containsKey(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|EHC"))
							M_strSQLQRY += hstNEW.get(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|EHC").toString() +",";
						else
							M_strSQLQRY +="0,";

						if(hstNEW.containsKey(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|CVD"))
							M_strSQLQRY += hstNEW.get(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|CVD").toString() +",";
						else
							M_strSQLQRY +="0,";

						if(hstNEW.containsKey(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|ACV"))
							M_strSQLQRY += hstNEW.get(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim()+"|ACV").toString() +",";
						else
							M_strSQLQRY +="0,";
////////// end of block added on 06/09/2007 to back update individual values
						M_strSQLQRY += getUSGDTL("Inv",'I',strSTSFL)+")";
						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						M_strSQLQRY = "update MR_IVTRN set IVT_INVNO = IVT_INVNO where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' and IVT_LADNO = '"+txtINVNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						//System.out.println("Insert Query = "+M_strSQLQRY);
					
			
    					/*M_strSQLQRY="UPDATE MR_DOTRN SET DOT_LADQT= isnull(DOT_LADQT,0) + " + tblGRDTL.getValueAt(i,TB1_INVQT).toString();
    					M_strSQLQRY += " WHERE DOT_MKTTP  = '"+strMKTTP +"'";
    					M_strSQLQRY +=" AND DOT_DORNO='"+txtDORNO.getText().trim() +"'";
    					M_strSQLQRY +=" AND DOT_PRDCD='"+tblGRDTL.getValueAt(i,TB1_PRDCD).toString() +"'";
    					M_strSQLQRY +=" AND DOT_PKGTP='"+tblGRDTL.getValueAt(i,TB1_PKGTP).toString() +"'";
    					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
                        
                        M_strSQLQRY="UPDATE MR_INTRN SET INT_LADQT= isnull(INT_LADQT,0) + "+tblGRDTL.getValueAt(i,TB1_INVQT).toString()+" , ";
                        M_strSQLQRY += " INT_INVQT= isnull(INT_INVQT,0) + "+tblGRDTL.getValueAt(i,TB1_INVQT).toString();
    					M_strSQLQRY += " WHERE INT_MKTTP  = '"+strMKTTP +"'";
    					M_strSQLQRY +=" AND INT_INDNO='"+strINDNO +"'";
    					M_strSQLQRY +=" AND INT_PRDCD='"+tblGRDTL.getValueAt(i,TB1_PRDCD).toString() +"'";
    					M_strSQLQRY +=" AND INT_PKGTP='"+tblGRDTL.getValueAt(i,TB1_PKGTP).toString() +"'";
    					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD"); */      		
    		
					}
				}
				insCOTAX();
				insLTDTL();
				if(cl_dat.M_flgLCUPD_pbst)
				{
					updDOTRN();
					updDOCNO(txtINVNO.getText().trim());
					if(!txtSALTP.getText().equals("04"))
					{
    						cstPLTRN_INV.setString(1,cl_dat.M_strCMPCD_pbst);
	    					cstPLTRN_INV.setString(2,strMKTTP);
    						cstPLTRN_INV.setString(3,txtINVNO.getText().trim());
    						cstPLTRN_INV.executeUpdate();
					}
					cl_dat.M_conSPDBA_pbst.commit();
					if(cl_dat.M_flgLCUPD_pbst)
						JOptionPane.showMessageDialog(this,"Please Note down Invoice No :  "+txtINVNO.getText().trim(),"Message",JOptionPane.INFORMATION_MESSAGE);
				}
				
				int i=0;
			}
			
				if(cl_dat.exeDBCMT("exeSave"))
				{
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				    {
				        txtINVDT.setText(strREFDT);
				        txtINVDT.requestFocus();
				    }
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				    {
				        M_strSQLQRY="Select PT_YOPVL,PT_YSTDT,PT_YOPFL from CO_PTMST where PT_PRTTP='C' AND PT_PRTCD='"+txtBYRCD.getText().trim()+"'";
            			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
            			if(M_rstRSSET!=null && M_rstRSSET.next())
            			{
            				if(M_rstRSSET.getDate("PT_YSTDT") !=null)
            				    strYSTDT = M_fmtLCDAT.format(M_rstRSSET.getDate("PT_YSTDT"));
            				else strYSTDT ="01/07/2006";    
            				dblYOPVL = M_rstRSSET.getDouble("PT_YOPVL");
            				strYOPFL=nvlSTRVL(M_rstRSSET.getString("PT_YOPFL"),"");
            			}
            			if(M_rstRSSET!=null)
            				M_rstRSSET.close();
				        // Rework the part ledger here
				        //System.out.println("Before Rework");
				        
				        rwkPLTRN_PRT.setString(1,cl_dat.M_strCMPCD_pbst);
        				rwkPLTRN_PRT.setString(2,"C");
        				rwkPLTRN_PRT.setString(3,txtBYRCD.getText().trim());
        				strYSTDT =  strYSTDT.substring(6,10) + "-" + strYSTDT.substring(3,5) + "-" + strYSTDT.substring(0,2);
					  	rwkPLTRN_PRT.setDate(4,java.sql.Date.valueOf(strYSTDT));
        				rwkPLTRN_PRT.setDouble(5,dblYOPVL);
        				rwkPLTRN_PRT.setString(6,strYOPFL);
        				rwkPLTRN_PRT.executeUpdate();
        				//System.out.println("After Rework");
    				    cl_dat.M_conSPDBA_pbst.commit();
				        //call shndata.rwkPLTRN_PRT('01','C','A0016','07/01/2006',74445,'DB');
				    }
				  	L_strINVNO=txtINVNO.getText().trim();
					lblINVQT.setText("");
					clrCOMP();
					txtINVDT.setText(strREFDT);
					txtINVDT.requestFocus();
					//txtINVNO.setText(L_strINVNO);
					
					//L_strDOCNO=txtDOCNO.getText().trim();
					setMSG("Data Saved successfully",'N');
					
				}
				else
				{
				    setMSG("Error in updating ..",'E');
				}
			//}
			
		}
		catch(Exception L_EX)
		{
			//System.out.println("Insert Query IN Exe = "+M_strSQLQRY);
			setMSG(L_EX,"ExeSave");
		}
	}
		
	public String genINVNO()
	{
		String L_strDOCTP="",L_strDOCNO  = "",  L_strCODCD = "", L_strCCSVL = "0",L_CHP02="";// for DOC
		try
		{
						
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP ='MRXXINV'  and ";
			//M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) +"00'  and  isnull(CMT_STSFL,'') <> 'X'";
			M_strSQLQRY += " CMT_CODCD = '" + strYRDGT +"00'  and  isnull(CMT_STSFL,'') <> 'X'";

			//System.out.println("M_strSQLQRY"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"");
					/*if(L_CHP02.trim().length() >0)  // check removed on 18/12/2006
					{
						setMSG("dataBase IN USE",'E');
						M_rstRSSET.close();
						return null;
					}*/
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP02 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MRXXINV'";	
//			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3)+ "00'";
			M_strSQLQRY += " and CMT_CODCD = '" + strYRDGT+ "00'";

			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY," ");
			//System.out.println("L_strCCSVL"+L_strCCSVL);
			if(cl_dat.exeDBCMT("genDOCNO"))
			{
				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
				for(int i=L_strCCSVL.length();i<5; i++)				//for padding zero(s)
					L_strDOCNO += "0";
				
				//System.out.println("L_strCCSVL"+L_strCCSVL);
				L_strCCSVL = L_strDOCNO + L_strCCSVL;
				L_strDOCNO = L_strCODCD + L_strCCSVL;
				txtINVNO.setText(L_strDOCNO);
			}
			else 
				return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genDOCNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_strDOCNO;
	}	
		
	private void insCOTAX()
	{
	   try
	   {
		   
	        String L_strDOCTP ="",L_strTRNTP ="",L_strTRNTP1="",strTXVLA="",strTXVLD="",L_strMKTTP="";
	         int L_intCOLA =0;
	        int L_intCOLD =0; 
	      	M_strSQLQRY  = "insert into CO_TXDOC (TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,";
			M_strSQLQRY += "TX_PRDCD,TX_SBSCD,TX_TRNTP, ";
			for(int i=0;i<tblCOTAX.getRowCount();i++)
			{
				if(tblCOTAX.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
				{
					//L_strDOCTP =tblCOTAX.getValueAt(i,TB2_TXMOD).toString();
					//L_strTRNTP=strTRNTP+L_strDOCTP;
					M_strSQLQRY += "TX_"+tblCOTAX.getValueAt(i,TB2_TAXCD).toString()+"VL,";
					M_strSQLQRY += " TX_"+tblCOTAX.getValueAt(i,TB2_TAXCD).toString()+"FL,";
					strTXVLA +=tblCOTAX.getValueAt(i,TB2_TAXVL).toString()+",'"+tblCOTAX.getValueAt(i,TB2_TAXFL).toString()+"',";
					L_intCOLA++;
				}
			}
			M_strSQLQRY += " TX_STSFL,TX_TRNFL,TX_LUSBY,TX_LUPDT )values(";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'MR',";
			M_strSQLQRY += "'"+strMKTTP+"',";
			M_strSQLQRY += "'INV',";
			M_strSQLQRY += "'"+txtINVNO.getText().trim()+"',";
			M_strSQLQRY += "'XXXXXXXXXX',";
			M_strSQLQRY += "'"+M_strSBSCD+"',";
			M_strSQLQRY += "'M',";
			M_strSQLQRY += strTXVLA;
			M_strSQLQRY += getUSGDTL("TX_",'I',"0")+")";
    		
			if(L_intCOLA>0)
			{
				//System.out.println("TAX ENTRY = "+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			
	   }
	   catch(Exception L_E)
	   {
		   setMSG(L_E,"insCOTAX");
	   }
	}
	
	public void insLTDTL()
	{
		try
		{
			for(int i=0;i<tblLTDTL.getRowCount();i++)
			{
				if(tblLTDTL.getValueAt(i,TB4_CHKFL).toString().trim().equals("true"))
				{
					M_strSQLQRY="Insert Into FG_ISTRN(IST_CMPCD,"; 
					M_strSQLQRY +=" IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDCD,IST_PRDTP, ";
					M_strSQLQRY +=" IST_LOTNO,IST_PKGTP,IST_MNLCD,IST_ISSDT,IST_ISSQT, ";
					M_strSQLQRY +=" IST_ISSPK,IST_PKGCT,IST_RCLNO,IST_SALTP,IST_MKTTP, ";
					M_strSQLQRY +=" IST_AUTDT,IST_STSFL,IST_TRNFL,IST_LUSBY,IST_LUPDT) Values(";
					
					M_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"',";
										
					M_strSQLQRY +=" '01',";
					M_strSQLQRY +=" '10',";
					M_strSQLQRY +="'"+txtINVNO.getText().trim()+"',";
					M_strSQLQRY +="'"+tblLTDTL.getValueAt(i,TB4_PRDCD).toString()+"',";
					M_strSQLQRY +="'"+strPRDTP+"',";
					
					M_strSQLQRY +="'"+tblLTDTL.getValueAt(i,TB4_LOTNO).toString()+"',";
					M_strSQLQRY +="'"+tblLTDTL.getValueAt(i,TB4_PKGTP).toString()+"',";
					M_strSQLQRY +="'"+tblLTDTL.getValueAt(i,TB4_MNLCD).toString()+"',";
					M_strSQLQRY +=((txtINVDT.getText().trim().length()==10) ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINVDT.getText().trim()))+"'" : "null")+", "; //Date type
					M_strSQLQRY +=""+tblLTDTL.getValueAt(i,TB4_ISSQT).toString()+",";
					
					M_strSQLQRY +=""+tblLTDTL.getValueAt(i,TB4_ISSPK).toString()+",";
					M_strSQLQRY +="'"+tblLTDTL.getValueAt(i,TB4_PKGCT).toString()+"',";
					M_strSQLQRY +="'"+tblLTDTL.getValueAt(i,TB4_RCLNO).toString()+"',";
					M_strSQLQRY +="'"+txtSALTP.getText().trim()+"',";
					M_strSQLQRY +="'"+strMKTTP+"',";
					
					M_strSQLQRY +=((txtINVDT.getText().trim().length()==10) ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINVDT.getText().trim()))+"'" : "null")+", "; //Date type
					M_strSQLQRY +=" '2',";
					M_strSQLQRY +=" '0',";
					M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
					//M_strSQLQRY += getUSGDTL("LT_",'I',"0")+")";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insLTDTL");
		}
	}
	
	public void updDOTRN()
	{
		try
		{
			for(int i=0;i<tblGRDTL.getRowCount();i++)
			{
				if(tblGRDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY="UPDATE MR_DOTRN SET DOT_LADQT= isnull(DOT_LADQT,0) + "+tblGRDTL.getValueAt(i,TB1_INVQT).toString()+" ,DOT_INVQT= isnull(DOT_INVQT,0) + "+tblGRDTL.getValueAt(i,TB1_INVQT).toString() ;
					M_strSQLQRY +=",DOT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="DOT_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
					M_strSQLQRY += " WHERE DOT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOT_MKTTP = '" +strMKTTP+ "'";
					M_strSQLQRY +=" AND DOT_DORNO='"+txtDORNO.getText().trim()+"'";
					M_strSQLQRY += " AND DOT_PRDCD = '" +tblGRDTL.getValueAt(i,TB1_PRDCD).toString() + "'";
					M_strSQLQRY += " AND DOT_PKGTP = '" +tblGRDTL.getValueAt(i,TB1_PKGTP).toString()+ "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					  
                    M_strSQLQRY="UPDATE MR_INTRN SET INT_LADQT= isnull(INT_LADQT,0) + "+tblGRDTL.getValueAt(i,TB1_INVQT).toString()+" , ";
                    M_strSQLQRY += " INT_INVQT= isnull(INT_INVQT,0) + "+tblGRDTL.getValueAt(i,TB1_INVQT).toString();
					M_strSQLQRY += " WHERE INT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and INT_MKTTP  = '"+strMKTTP +"'";
					M_strSQLQRY +=" AND INT_INDNO='"+strINDNO +"'";
					M_strSQLQRY +=" AND INT_PRDCD='"+tblGRDTL.getValueAt(i,TB1_PRDCD).toString() +"'";
					M_strSQLQRY +=" AND INT_PKGTP='"+tblGRDTL.getValueAt(i,TB1_PKGTP).toString() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD"); 
				}
			}
			
			for(int i=0;i<tblLTDTL.getRowCount();i++)
			{
				if(tblLTDTL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
				{
					
					M_strSQLQRY="UPDATE FG_STMST SET ST_STKQT= isnull(ST_STKQT,0)- "+tblLTDTL.getValueAt(i,TB4_ISSQT).toString()+" , ";
					M_strSQLQRY +="ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="ST_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
					M_strSQLQRY += " WHERE ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ST_WRHTP = '01'";
					M_strSQLQRY +=" AND ST_PRDTP='"+tblLTDTL.getValueAt(i,TB4_PRDTP).toString() +"'";
					M_strSQLQRY +=" AND ST_LOTNO='"+tblLTDTL.getValueAt(i,TB4_LOTNO).toString() +"'";
					M_strSQLQRY +=" AND ST_RCLNO='"+tblLTDTL.getValueAt(i,TB4_RCLNO).toString() +"'";
					M_strSQLQRY +=" AND ST_PKGTP='"+tblLTDTL.getValueAt(i,TB4_PKGTP).toString() +"'";
					M_strSQLQRY +=" AND ST_MNLCD='"+tblLTDTL.getValueAt(i,TB4_MNLCD).toString() +"'";
					M_strSQLQRY +=" AND ST_PRDTP='"+tblLTDTL.getValueAt(i,TB4_PRDTP).toString() +"'";
					
					//System.out.println("UPDATE "+M_strSQLQRY);
					//cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
				}
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updDOTRN");
		}
	}
	
	/**
	 *  Method to update the last Document No.in the CO_CDTRN
	*/
	private void updDOCNO(String P_strDOCNO)
	{
		try
		{
			String L_strDOCTP="";
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP02 ='',CMT_CCSVL = '" + P_strDOCNO.substring(3,8) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP ='MRXXINV'";
//			M_strSQLQRY += " and CMT_CODCD = '"+ cl_dat.M_strFNNYR1_pbst.substring(3) + "00'";			
			M_strSQLQRY += " and CMT_CODCD = '"+ strYRDGT + "00'";			

			//System.out.println("updDOCNO "+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeDOCNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	public void getDATA()
	{
		try
		{
			int i=0;
			tblGRDTL.clrTABLE();
			tblLTDTL.clrTABLE();
			tblCOTAX.clrTABLE();
			tblGRTAX.clrTABLE();
			
			isEDIT();
			
			M_strSQLQRY="Select * from MR_IVTRN where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_LADNO='"+txtINVNO.getText().trim()+"' AND IVT_MKTTP='"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' AND isnull(IVT_STSFL,'') <> 'X'";
			//System.out.println("i= "+i);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					//tblGRDTL.setValueAt(new Boolean(true),i,TB1_CHKFL);
					txtLR_NO.setText(getRSTVAL(M_rstRSSET,"IVT_LR_NO","C"));
					
					txtLR_DT.setText(getRSTVAL(M_rstRSSET,"IVT_LR_DT","D"));
					
					txtDORNO.setText(getRSTVAL(M_rstRSSET,"IVT_DORNO","C"));
					
					txtDORDT.setText(getRSTVAL(M_rstRSSET,"IVT_DORDT","D"));
					
					txtINVVL.setText(getRSTVAL(M_rstRSSET,"IVT_INVVL","C"));
					txtLRYNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),""));
					txtDSTDS.setText(nvlSTRVL(M_rstRSSET.getString("IVT_DSTDS"),""));
					
					txtCNTDS.setText(nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),""));
					txtBYRCD.setText(nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),""));
					txtDSRCD.setText(nvlSTRVL(M_rstRSSET.getString("IVT_DSRCD"),""));
					txtTRPCD.setText(nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),""));
					txtCNSCD.setText(nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),""));
					txtSALTP.setText(nvlSTRVL(M_rstRSSET.getString("IVT_SALTP"),""));
					txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_PORNO"),""));
					txtPORDT.setText(getRSTVAL(M_rstRSSET,"IVT_PORDT","D"));
					txtTSLNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_TSLNO"),""));
					txtRSLNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_RSLNO"),""));
					txtADLNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_ADLNO"),""));
					txtRSLNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_RSLNO"),""));
					txtADLDT.setText(getRSTVAL(M_rstRSSET,"IVT_ADLDT","D"));
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_PRDCD"),i,TB1_PRDCD);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_PRDDS"),i,TB1_GRADE);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_PKGTP"),i,TB1_PKGTP);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_INVRT"),i,TB1_RATE);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_INVQT"),i,TB1_INVQT);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_PKGWT"),i,TB1_PKGWT);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_INVPK"),i,TB1_INVPK);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_ASSVL"),i,TB1_VALUE);
					tblGRDTL.setValueAt(M_rstRSSET.getString("IVT_NETVL"),i,TB1_NTVAL);
					
					i++;
				}
			}
			if(i==0)
			{
			    lblINVQT.setText("");
				clrCOMP();
				setMSG("No data Found",'E');
				return;
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			getCOTAX(txtINVNO.getText().trim(),"INV");
			getLTDTL();
			
			txtBYRNM.setText(getPRDTL(txtBYRCD.getText().trim(),"C"));
			txtDSRNM.setText(getPRDTL(txtDSRCD.getText().trim(),"D"));
			txtTRPNM.setText(getPRDTL(txtTRPCD.getText().trim(),"T"));
			txtCNSNM.setText(getPRDTL(txtCNSCD.getText().trim(),"C"));
						
			M_strSQLQRY="Select PT_TSTFL,PT_ADR01,PT_ADR02,PT_ADR03,PT_ECCNO,PT_TINNO from CO_PTMST where PT_PRTTP='C' AND PT_PRTCD='"+txtCNSCD.getText().trim()+"'";
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				txtCAD01.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""));
				txtCAD02.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),""));
				txtCAD03.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),""));
				txtECCNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_ECCNO"),""));
				txtTINNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_TINNO"),""));
				strTSTFL=nvlSTRVL(M_rstRSSET.getString("PT_TSTFL"),"");
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
				
	}
	
	public String getPRDTL(String P_strCOND1,String P_strCOND2)
	{
		String L_strRETRN="";
		try
		{
			
			M_strSQLQRY="Select PT_PRTNM from CO_PTMST where PT_PRTCD='"+P_strCOND1+"' and  PT_PRTTP='"+P_strCOND2+"'";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				L_strRETRN=M_rstRSSET.getString("PT_PRTNM");
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDTL");
		}
		

		return L_strRETRN;
	}
	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	*/
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
		////System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
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
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 
	public void getLTDTL()
	{
		try
		{
			int i=0;
			//M_strSQLQRY="Select * from FG_ISTRN  where IST_ISSNO='"+txtINVNO.getText().trim()+"'";
			M_strSQLQRY="Select IST_PRDCD,IST_LOTNO,IST_PKGTP,IST_ISSQT,IST_ISSPK,IST_MNLCD,IST_PKGCT,IST_RCLNO,IST_PRDTP,PR_PRDDS ";
			M_strSQLQRY +="from CO_PRMST,FG_ISTRN  where  IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PR_PRDCD=IST_PRDCD  ";
			M_strSQLQRY +="	AND IST_PRDTP=PR_PRDTP AND IST_ISSNO='"+txtINVNO.getText().trim()+"'";
			
			//System.out.println("getLTDTL"+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					tblLTDTL.setValueAt(new Boolean(true),i,TB4_CHKFL);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_PRDCD"),""),i,TB4_PRDCD);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""),i,TB4_GRADE);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),""),i,TB4_LOTNO);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_PKGTP"),""),i,TB4_PKGTP);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_ISSQT"),""),i,TB4_ISSQT);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_ISSPK"),""),i,TB4_ISSPK);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_MNLCD"),""),i,TB4_MNLCD);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_PKGCT"),""),i,TB4_PKGCT);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_RCLNO"),""),i,TB4_RCLNO);
					tblLTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_PRDTP"),""),i,TB4_PRDTP);
					i++;
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLTDTL");
		}
	}
	
	public boolean delINVIC()
	{
		try
		{
		    double L_dblADJVL =0;
			M_strSQLQRY ="SELECT isnull(PL_ADJVL,0) L_ADJVL from MR_PLTRN WHERE PL_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PL_DOCNO ='"+ txtINVNO.getText()+"' AND PL_DOCTP ='21'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			    if(M_rstRSSET.next())
			    {
			        L_dblADJVL = M_rstRSSET.getDouble("L_ADJVL");
			    }
			}
			if(L_dblADJVL >0)
			{
			    setMSG("Payment has been adjusted, Invoice can not be deleted..",'E');
			    return false;
			}
			M_strSQLQRY ="SELECT COUNT(*) L_CNT from MR_PTTRN WHERE PT_INVNO ='"+ txtINVNO.getText()+"' and isnull(PT_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			    if(M_rstRSSET.next())
			    {
			        if(M_rstRSSET.getInt("L_CNT") >0)
			        {
        			    setMSG("Further transactions has been done., Invoice can not be deleted..",'E');
        			    return false;
        			}
			    }
			}
			if(L_dblADJVL >0)
			{
			    setMSG("Payment has been adjusted, Invoice can not be deleted..",'E');
			    return false;
			}
			M_strSQLQRY = " update  MR_IVTRN set IVT_STSFL='X' where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_INVNO='"+txtINVNO.getText()+"' ";
			//System.out.println("update  MR_IVRTN"+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	
			M_strSQLQRY = " update  CO_TXDOC set TX_STSFL='X' where TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_DOCNO ='"+txtINVNO.getText()+"' AND TX_DOCTP='INV'";
			//System.out.println("update  CO_TXDOC"+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		
			M_strSQLQRY = " update  FG_ISTRN set IST_STSFL='X' where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_ISSNO ='"+txtINVNO.getText()+"' ";
			//System.out.println("update  FG_ISTRN"+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		
			for(int i=0;i<tblLTDTL.getRowCount();i++)
			{
				if(tblLTDTL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY="UPDATE FG_STMST SET ST_STKQT= isnull(ST_STKQT,0)+ "+tblLTDTL.getValueAt(i,TB4_ISSQT).toString()+" , ";
					M_strSQLQRY +="ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="ST_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
					M_strSQLQRY += " WHERE ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ST_WRHTP = '01'";
					M_strSQLQRY +=" AND ST_PRDTP='"+tblLTDTL.getValueAt(i,TB4_PRDTP).toString() +"'";
					M_strSQLQRY +=" AND ST_LOTNO='"+tblLTDTL.getValueAt(i,TB4_LOTNO).toString() +"'";
					M_strSQLQRY +=" AND ST_RCLNO='"+tblLTDTL.getValueAt(i,TB4_RCLNO).toString() +"'";
					M_strSQLQRY +=" AND ST_PKGTP='"+tblLTDTL.getValueAt(i,TB4_PKGTP).toString() +"'";
					M_strSQLQRY +=" AND ST_MNLCD='"+tblLTDTL.getValueAt(i,TB4_MNLCD).toString() +"'";
					M_strSQLQRY +=" AND ST_PRDTP='"+tblLTDTL.getValueAt(i,TB4_PRDTP).toString() +"'";
				
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			for(int i=0;i<tblGRDTL.getRowCount();i++)
			{
				if(tblGRDTL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY="UPDATE MR_DOTRN SET DOT_LADQT= isnull(DOT_LADQT,0) - "+tblGRDTL.getValueAt(i,TB1_INVQT).toString()+" , ";
					M_strSQLQRY += " WHERE DOT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and DOT_MKTTP  = '"+strMKTTP +"'";
					M_strSQLQRY +=" AND DOT_DORNO='"+txtDORNO.getText().trim() +"'";
					M_strSQLQRY +=" AND DOT_PRDCD='"+tblGRDTL.getValueAt(i,TB1_PRDCD).toString() +"'";
					M_strSQLQRY +=" AND DOT_PKGTP='"+tblGRDTL.getValueAt(i,TB1_PKGTP).toString() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					M_strSQLQRY="UPDATE MR_INTRN SET INT_LADQT= isnull(INT_LADQT,0) - "+tblGRDTL.getValueAt(i,TB1_INVQT).toString()+" , ";
                    M_strSQLQRY += " INT_INVQT= isnull(INT_INVQT,0) - "+tblGRDTL.getValueAt(i,TB1_INVQT).toString()+" , ";
					M_strSQLQRY += " WHERE INT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and INT_MKTTP  = '"+strMKTTP +"'";
					M_strSQLQRY +=" AND INT_INDNO='"+strINDNO +"'";
					M_strSQLQRY +=" AND INT_PRDCD='"+tblGRDTL.getValueAt(i,TB1_PRDCD).toString() +"'";
					M_strSQLQRY +=" AND INT_PKGTP='"+tblGRDTL.getValueAt(i,TB1_PKGTP).toString() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");   
				}
			}
			M_strSQLQRY ="DELETE from MR_PLTRN WHERE PL_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PL_DOCNO ='"+ txtINVNO.getText()+"' AND PL_DOCTP ='21'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"delINVIC");
		}
		return true;
	} 
		
	/**
	    Jtable for Common Tax and Item Tax is navigated and Hashtables are maintained for
	    Common Tax codes as (Code,P/A + TAXVL)
	    and Itemwise Tax code (Item Code + TAX Code,P/A + TAXVL)
	
	*/
	private String calINVVL()
	{
		double L_dblITVAL=0.00;
		double L_dblTOTAL=0.00;
		double L_dblFRACT=0.00;
		boolean flgOTTAX = false;
		String L_strTXOCD ="";
		String L_strTXOCD1 ="";
	
		try
	    {
			ResultSet L_rstRSSET;
			Hashtable<String,String> hstITTAX = new Hashtable<String,String>();
			Hashtable<String,String> hstCOTAX = new Hashtable<String,String>();
			Hashtable<String,String> hstITCOD = new Hashtable<String,String>();
			Hashtable<String,Double> hstTXVAL = new Hashtable<String,Double>();
			Hashtable<String,Double> hstTAXVL = new Hashtable<String,Double>(); // to store total tax value and print
			if(hstNEW !=null)
				hstNEW.clear();
			String L_strTEMP ="";
			String L_strMULT ="";
			String L_strBILNO="",L_strMATCD;
			String L_strTAXVL=" ",L_strITMCD="",L_strTAXCD="",L_strTAXFL="";
	        double dblTXVAL = 0.0;
	        double L_dblTAXVL = 0.0;
		  double L_dblITTAX =0.0;
			//System.out.println("L_strTAXVL0 "+L_strTAXVL);
			for(int j=0;j<tblCOTAX.getRowCount();j++)
			{
				//System.out.println("0000000 ");
				if(tblCOTAX.getValueAt(j,0).toString().equals("true"))
				{
					//System.out.println("1111111111111 ");
					L_strTAXCD = tblCOTAX.getValueAt(j,1).toString();
					L_strTAXFL = tblCOTAX.getValueAt(j,4).toString();
					L_strTAXVL = tblCOTAX.getValueAt(j,3).toString();
					
					//System.out.println("L_strTAXCD = "+L_strTAXCD);
					//System.out.println("L_strTAXVL = "+L_strTAXVL);
					if(L_strTAXFL.equals("P"))
					L_strTAXVL = String.valueOf(Double.parseDouble(L_strTAXVL)/100);
					// put in a hashtable 
					if(L_strTAXFL.equals("P"))
						hstCOTAX.put(L_strTAXCD,"P"+L_strTAXVL); 
					else
						hstCOTAX.put(L_strTAXCD,"A"+L_strTAXVL); 
				}
			}
			//System.out.println("2222222222222 ");
			for(int j=0;j<tblGRTAX.getRowCount();j++)
			{
				if(tblGRTAX.getValueAt(j,0).toString().equals("true"))
				{
					//System.out.println("444444444444");
					L_strITMCD = tblGRTAX.getValueAt(j,TB3_PRDCD).toString();
					L_strTAXCD = tblGRTAX.getValueAt(j,TB3_TAXCD).toString();
					L_strTAXFL = tblGRTAX.getValueAt(j,TB3_TAXFL).toString();
					L_strTAXVL = tblGRTAX.getValueAt(j,TB3_TAXVL).toString();
					
					//.println("L_strTAXVL "+L_strTAXVL);
					if(L_strTAXFL.equals("P"))
					L_strTAXVL = String.valueOf(Double.parseDouble(L_strTAXVL)/100);
					// put in a hashtable 
					if(L_strTAXFL.equals("P"))
						hstITTAX.put(L_strITMCD+L_strTAXCD,"P"+L_strTAXVL); 
					else
						hstITTAX.put(L_strITMCD+L_strTAXCD,"A"+L_strTAXVL); 
				}
			}
			//System.out.println("555555555555");
			//L_strBILNO = txtSRLNO.getText().trim();
			dblBILVL =0;
			for(int j=0;j<tblGRDTL.getRowCount();j++)
			{
				if(tblGRDTL.getValueAt(j,0).toString().equals("true"))
				{
					//System.out.println( "TB1_INVQT = "+tblGRDTL.getValueAt(j,TB1_INVQT).toString());
					//System.out.println( "TB1_RATE = "+tblGRDTL.getValueAt(j,TB1_RATE).toString());
					
					L_dblITVAL = (Double.parseDouble(tblGRDTL.getValueAt(j,TB1_INVQT).toString()))* (Double.parseDouble(tblGRDTL.getValueAt(j,TB1_RATE).toString()));//*Double.parseDouble(txtEXGRT.getText())	;
					L_dblTOTAL +=L_dblITVAL;
					
				}
			}
		//	System.out.println("L_dblTOTAL = "+L_dblTOTAL);
			for(int j=0;j<tblGRDTL.getRowCount();j++)
			{
				if(tblGRDTL.getValueAt(j,0).toString().equals("true"))
				{
					L_strMATCD =  tblGRDTL.getValueAt(j,TB1_PRDCD).toString();
					L_dblITVAL =  (Double.parseDouble(tblGRDTL.getValueAt(j,TB1_INVQT).toString()))
							      *(Double.parseDouble(tblGRDTL.getValueAt(j,TB1_RATE).toString()));//*Double.parseDouble(txtEXGRT.getText())	;
					L_dblFRACT =  L_dblITVAL /L_dblTOTAL;
					
					//System.out.println("\n L_dblFRACT = "+L_dblFRACT);
					
					dblTXVAL =0;
					for(int i=0;i<vtrTAXCD.size();i++)
					{
						L_strTAXCD = vtrTAXCD.elementAt(i).toString();
						//dblTXVAL =0;
						//Item wise tax
						//System.out.println("L_strTAXCD "+L_strTAXCD);
						if(hstITTAX.get(L_strMATCD+L_strTAXCD)==null)
						{ 
							// Item wise tax is not present
						}
						else
						//if(hstITTAX.contains(L_strMATCD+L_strTAXCD))
						{
							// Item wise tax is present
							L_strTEMP = hstITTAX.get(L_strMATCD+L_strTAXCD).toString();
							L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
							L_strMULT = L_STRTKN.nextToken();
							L_STRTKN.nextToken();
						      //L_strTXOCD = L_STRTKN.nextToken(); 
						     ////////////////////////////////////////					
							// Added on 17/08/07 for including EDC and EHC on Excise and CVD both( at all places)
							L_strTXOCD1 = L_STRTKN.nextToken(); 
							if(L_strTXOCD1.trim().length() == 0)
								L_strTXOCD ="";
							else
							{
								L_STRTKN=new StringTokenizer(L_strTXOCD1,"_");
								while(L_STRTKN.hasMoreTokens())
								{
									L_strTXOCD = L_STRTKN.nextToken(); 
									if(hstTXVAL.containsKey(L_strTXOCD))
									{
										break;
									}
								}
							}
							////////////////////////////////////////					

							if(L_strTEMP.substring(0,1).equals("P"))
							{
								//if(L_strTAXCD.equals("EXC"))
								if(L_strTXOCD.trim().length() >0)
								    hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))));
								else
								    hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))));
								dblTXVAL = L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);
								if(L_strTXOCD.trim().length() >0)
								    L_dblITVAL = L_dblITVAL + Double.parseDouble(hstTXVAL.get(L_strMATCD+L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
								else
								    L_dblITVAL += L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
							}
							else
							{									
								//	if(L_strTAXCD.equals("EXC"))
								hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(Double.parseDouble(L_strTEMP.substring(1))));
								dblTXVAL = (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor
							//	if(L_strTXOCD.trim().length() >0)
							//	    L_dblITVAL = L_dblITVAL + (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
							//	else
							   	L_dblITVAL += (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
									
							}
							for(int k=0;k<tblGRTAX.getRowCount();k++)
							{
								if(tblGRTAX.getValueAt(k,0).toString().equals("true"))
								{
									if((L_strITMCD+L_strTAXCD).equals(tblGRTAX.getValueAt(k,1).toString()+tblGRTAX.getValueAt(k,3).toString()))
									{
									   flgFOUND = true;
									   if(L_strTXOCD.trim().length() >0)
									   {    
	                                        tblGRTAX.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strMATCD+L_strTAXCD).toString()),2),k,6);   
	                                       // tblGRDTL.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strMATCD+L_strTAXCD).toString()),2),j,TBL_VATBL);   
	                                    }
									    else
	                                    {
										    tblGRTAX.setValueAt(setNumberFormat(dblTXVAL,2),k,6);   
	                                        //tblGRDTL.setValueAt(setNumberFormat(dblTXVAL,2),j,TBL_VATBL);   
	                                    }
									}
								}
							}	
						}
						// Common Tax
						if(hstCOTAX.get(L_strTAXCD)== null)
						{
							// // common tax is not present
						}
						else
						{
							L_strTEMP = hstCOTAX.get(L_strTAXCD).toString();
				//			System.out.println("L_strTEMP = "+L_strTEMP);
							L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
							L_strMULT = L_STRTKN.nextToken();
							L_STRTKN.nextToken();
						      //L_strTXOCD = L_STRTKN.nextToken(); 
  			////////////////////////////////////////					
							// Added on 17/08/07 for including EDC and EHC on Excise and CVD both( at all places)
							L_strTXOCD1 = L_STRTKN.nextToken(); 
							if(L_strTXOCD1.trim().length() == 0)
								L_strTXOCD ="";
							else
							{
								L_STRTKN=new StringTokenizer(L_strTXOCD1,"_");
								while(L_STRTKN.hasMoreTokens())
								{
									L_strTXOCD = L_STRTKN.nextToken(); 
									if(hstTXVAL.containsKey(L_strTXOCD))
									{
										break;
									}
								}
							}
							////////////////////////////////////////			
					//		System.out.println("L_strTXOCD = "+L_strTXOCD);
							if(L_strTEMP.substring(0,1).equals("P"))
							{
								//if(L_strTAXCD.equals("EXC"))
								dblTXVAL = (L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add
								
						//		System.out.println("dblTXVAL = "+dblTXVAL);
								
								if(L_strTXOCD.trim().length() >0)
								{
								    hstTXVAL.put(L_strTAXCD,new Double(Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))));
								    /// new
								    if(hstTAXVL.containsKey(L_strTAXCD))
								    {
								        L_dblTAXVL = Double.parseDouble(hstTAXVL.get(L_strTAXCD).toString()) + Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)));
								        // Tax is present in hashtable 
								        hstTAXVL.put(L_strTAXCD,new Double(L_dblTAXVL));    
								    }
								    else
								    {
								        // tax is not present in hashtable
								        hstTAXVL.put(L_strTAXCD,new Double(Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))));    
								    }
								    /// end new
								}
								else
								{
								    hstTXVAL.put(L_strTAXCD,new Double(L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))));
								   /// new
								    if(hstTAXVL.containsKey(L_strTAXCD))
								    {
								        L_dblTAXVL = Double.parseDouble(hstTAXVL.get(L_strTAXCD).toString())+(L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1))));
								        hstTAXVL.put(L_strTAXCD,new Double(L_dblTAXVL));    
								    }
								    else
								    {
								        hstTAXVL.put(L_strTAXCD,new Double(L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))));
								    }
								}
								if(L_strTXOCD.trim().length() >0)
								{
								    L_dblITTAX = (Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));
								    L_dblITVAL = L_dblITVAL+(Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
								}
								else
								{
								    L_dblITTAX = (L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));
								    L_dblITVAL = L_dblITVAL+(L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
								}
							}
							else 
							{
								dblTXVAL = L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
								L_dblITVAL =L_dblITVAL +L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
								L_dblITTAX = dblTXVAL;
							}
							///System.out.println(L_strMATCD +"|"+L_strTAXCD+" : " +L_dblITTAX);
						      hstNEW.put(L_strMATCD +"|"+L_strTAXCD,String.valueOf(L_dblITTAX));

						//	System.out.println("L_dblITVAL ="+L_dblITVAL);
							// Printing part
							/*for(int x=0;x<tblCOTAX.getRowCount();x++)
							{
								if(tblCOTAX.getValueAt(x,0).toString().equals("true"))
								{
									if(L_strTAXCD.equals(tblCOTAX.getValueAt(x,TB2_TAXCD).toString()))
									{
										flgFOUND = true;
										if(L_strTXOCD.trim().length() >0)
										{
											tblCOTAX.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strTAXCD).toString()),2),x,5);   
											//if(L_strTAXCD.equals("VAT"))
											//	tblGRDTL.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strTAXCD).toString()),2),j,TBL_VATBL);
											//System.out.println("VAT from 1" +setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strTAXCD).toString()),2));
										}
										else
										{
	    								  	tblCOTAX.setValueAt(setNumberFormat(dblTXVAL,2),x,5);   
	    								  //	if(L_strTAXCD.equals("VAT"))
	    								  	//    tblGRDTL.setValueAt(setNumberFormat(dblTXVAL,2),j,TBL_VATBL);
	                                        System.out.println("VAT from " +setNumberFormat(dblTXVAL,2));
										}
									}
									
								}
							}*/
						}
					}
					// Update the Item Value to ITVAL in POMST.
					//if(!txtCURCD.getText().trim().equals("01"))
					//{
			//			dblTXVAL = dblTXVAL*dblEXGRT;
			//			L_dblITVAL = L_dblITVAL*dblEXGRT;
					//}
				    tblGRDTL.setValueAt(setNumberFormat(L_dblITVAL,2),j,TB1_NTVAL);
					dblBILVL +=L_dblITVAL;
				}
			 }// end for 
		
			for(int j=0;j<tblGRDTL.getRowCount();j++)
			{
				if(tblGRDTL.getValueAt(j,0).toString().equals("true"))
				{
					
				}
			}
			// Rounded Off on 21/04/2007
			txtINVVL.setText(setNumberFormat(dblBILVL,0));
			for(int x=0;x<tblCOTAX.getRowCount();x++)
			{
				if(tblCOTAX.getValueAt(x,0).toString().equals("true"))
				{
				    L_strTAXCD = tblCOTAX.getValueAt(x,TB2_TAXCD).toString();
				    tblCOTAX.setValueAt(setNumberFormat(Double.parseDouble(hstTAXVL.get(L_strTAXCD).toString()),2),x,5);   
				  //  System.out.println(L_strTAXCD+" : "+setNumberFormat(Double.parseDouble(hstTAXVL.get(L_strTAXCD).toString()),2));
				}
			}
		

			 //System.out.println(dblBILVL*Double.parseDouble(txtEXGRT.getText().trim()));
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"calINVVL");
		}
		return setNumberFormat(dblBILVL,2);
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
				String L_strREQQT="";
				String L_strSTKQT="";
				String L_strBALQT="";
				String L_strPKGWT="0";
				
				if(getSource()==tblGRDTL)
				{
					if(P_intCOLID>0)
						if(P_intCOLID!=TB1_CHKFL)
							if(((JTextField)tblGRDTL.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
								return true;
					
					if(P_intCOLID==TB1_INVQT)
					{ 
						L_strREQQT = tblGRDTL.getValueAt(P_intROWID,TB1_INVQT).toString().trim();
						//System.out.println("INVQT="+L_strREQQT);
						L_strBALQT = tblGRDTL.getValueAt(P_intROWID,TB1_BALQT).toString().trim();
						//System.out.println("BALQY="+L_strBALQT);
						double L_dblREQQT = Double.parseDouble(L_strREQQT);
						double L_dblBALQT = Double.parseDouble(L_strBALQT);
						//System.out.println("L_dblREQQT= "+L_dblREQQT);
						//System.out.println("L_dblSTLQT= "+L_dblBALQT);
						if(L_dblREQQT>L_dblBALQT)
						{
							setMSG("Requested Inv Quantity is More than Available Quantity",'E');
							//JOptionPane.showMessageDialog(this," Invalid option selected","Error Message",JOptionPane.INFORMATION_MESSAGE);
							//return false;
						}
						
						//dblREQQT = Double.parseDouble(L_strREQQT);
						//strBALQT = tblLODDTL.getValueAt(tblLODDTL.getSelectedRow(),TB1_BALQT).toString();
						//dblBALQT = Double.parseDouble(strBALQT);
						L_strPKGWT = tblGRDTL.getValueAt(P_intROWID,TB1_PKGWT).toString();
						//System.out.println("L_strPKGWT ="+L_strPKGWT);
						String L_strRATE=tblGRDTL.getValueAt(P_intROWID,TB1_RATE).toString();
						double L_dblRATE=Double.parseDouble(L_strRATE);
						double dblPKGWT = Double.parseDouble(L_strPKGWT);
						if (dblPKGWT == 0.000) 
						{
							dblPKGWT = 0.001;
						}
						double L_dblPKGNO = Double.parseDouble(nvlSTRVL(L_strREQQT,"0.00"))/dblPKGWT;
						L_dblPKGNO = Float.parseFloat(setNumberFormat(L_dblPKGNO,3));
						int L_intPKGNO = new Float(L_dblPKGNO).intValue();
						if(L_intPKGNO != L_dblPKGNO)
						{
							setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(dblPKGWT,3),'E'); 
							return false;
						}
						if (L_dblREQQT <= L_dblBALQT) 
						{
							//showLTDTL(P_intROWID);
							//tblLODDTL.setValueAt(new Boolean(true),tblLODDTL.getSelectedRow(),TB1_CHKFL);
							String strREQPK  = setNumberFormat((L_dblREQQT/dblPKGWT),0).toString();
							tblGRDTL.setValueAt(strREQPK,tblGRDTL.getSelectedRow(),TB1_INVPK);
							String strVALUE  = setNumberFormat((L_dblREQQT*L_dblRATE),2).toString();
							tblGRDTL.setValueAt(strVALUE,tblGRDTL.getSelectedRow(),TB1_VALUE);
						//	tblLODDTL.setRowSelectionInterval(tblLODDTL.getSelectedRow(),tblLODDTL.getSelectedRow()+1);
						//	tblLODDTL.setColumnSelectionInterval(TB1_REQQT,TB1_REQQT);
							setMSG("Valid Req.Qty ",'N');
						} 
						else 
						{
							setMSG("Req.Qty should not exceed "+setNumberFormat((L_dblBALQT),3).toString(),'E');
							return false;
						}						
					}					
				}
				if(getSource()==tblLTDTL)
				{
					
					if(P_intCOLID==TB4_ISSQT)
					{
						String L_strPRDCD="";
						String L_strPRDCD1="";
						String L_strLTQTY="";
						String L_strLOTPK="";
						String L_strINVQT="";
						double L_dblINVQT=0.0;
						double L_dblLTQTY=0.0;
						double L_dblTLTQT=0.0;
						double L_intLOTPK=0;
						int L_intCOUNT=0;
						String L_strTEMP="";
						hstGRDTL.clear();
						
						String L_strLOTQT = tblLTDTL.getValueAt(P_intROWID,TB4_ISSQT).toString().trim();
						L_strPKGWT = tblLTDTL.getValueAt(P_intROWID,TB4_PKGWT).toString().trim();
						L_strSTKQT = tblLTDTL.getValueAt(P_intROWID,TB4_STKQT).toString().trim();
						double L_dblLOTQT=Double.parseDouble(L_strLOTQT);
						double L_dblPKGWT=Double.parseDouble(L_strPKGWT);
						double L_dblSTKQT=Double.parseDouble(L_strSTKQT);
						
						for(int i=0;i<tblGRDTL.getRowCount();i++)
						{
							if(tblGRDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
							{
								hstGRDTL.put(tblGRDTL.getValueAt(i,TB1_PRDCD).toString(),tblGRDTL.getValueAt(i,TB1_INVQT).toString());			
								L_intCOUNT++;
					
							}
						}
		
						if(L_intCOUNT==0)
							return false;
						L_intCOUNT=0;
						Enumeration enmCODKEYS=hstGRDTL.keys();
						while(enmCODKEYS.hasMoreElements())
						{
							L_strPRDCD = (String)enmCODKEYS.nextElement();
							L_strINVQT=hstGRDTL.get(L_strPRDCD).toString();
							L_dblINVQT=Double.parseDouble(L_strINVQT);
							L_dblLTQTY=0.0;
							L_intLOTPK=0;
							for(int i=0;i<tblLTDTL.getRowCount();i++)
							{
								if(tblLTDTL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
								{
									L_strPRDCD1=tblLTDTL.getValueAt(i,TB4_PRDCD).toString();
									if(L_strPRDCD1.equals(L_strPRDCD))
									{
										L_strLTQTY=tblLTDTL.getValueAt(i,TB4_ISSQT).toString();
									//	L_strLOTPK=tblLTDTL.getValueAt(i,TB4_ISSPK).toString();
									//	L_intLOTPK+=Integer.parseInt(L_strLOTPK);
										L_dblLTQTY+=Double.parseDouble(L_strLTQTY);
										L_dblTLTQT+=Double.parseDouble(L_strLTQTY); // TOTAL LOT QTY
										L_intCOUNT++;
									}
								}
							}
							//System.out.println(L_dblINVQT);
							//System.out.println(L_dblLTQTY);
							lblINVQT.setText(setNumberFormat(L_dblTLTQT,3));
							//lblPKGS.setText(String.valueOf(L_intLOTPK));
							setMSG("",'N');
							if(L_dblINVQT<Double.parseDouble(setNumberFormat(L_dblLTQTY,3)))
							{
								setMSG("Lot Quantity is More Then Invoice Quantity",'E');
								return false;
							}
							else
							    setMSG("",'N');
						}				
							
						if(L_dblLOTQT<=L_dblSTKQT)
						{
							String strREQPK  = setNumberFormat((L_dblLOTQT/L_dblPKGWT),0).toString();
							//System.out.println("strREQPK = "+strREQPK);
							tblLTDTL.setValueAt(strREQPK,tblLTDTL.getSelectedRow(),TB4_ISSPK);
						}
						else 
						{
							setMSG("Req.Qty should not exceed "+setNumberFormat((L_dblSTKQT),3).toString(),'E');
							return false;
						}
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"TBLINPVF");
			}
			return true;
		}
	}
	public void getREFDT()//get reference date
	{
		try
		{
			java.sql.Date L_strTEMP=null;
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				L_rstRSSET.close();
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
				strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				System.out.println("REFDT = "+strREFDT);
				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
		}
	}
	class inpVRFY extends InputVerifier 
	{
	    String L_strTEMP ="";
		public boolean verify(JComponent input) 
		{
		    try
		    {
		        if(input == txtINVDT)
    			{
    			    M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));
    			   // M_calLOCAL.add(java.util.Calendar.DATE,+1);
    			    L_strTEMP = M_fmtLCDAT.format(M_calLOCAL.getTime());
    			    if(M_fmtLCDAT.parse(txtINVDT.getText().trim()).compareTo(M_fmtLCDAT.parse(strREFDT))< 0)
    				{
        		        setMSG("Please Enter Invoice Date as "+L_strTEMP,'E');
        		        return false;
    				}
    				else if(M_fmtLCDAT.parse(txtINVDT.getText().trim()).compareTo(M_fmtLCDAT.parse(strREFDT))> 0)
    				{
        		        setMSG("Please Enter Invoice Date as "+L_strTEMP,'E');
        		        return false;
    				}
    				/*else if(M_fmtLCDAT.parse(txtINVDT.getText().trim()).compareTo(M_fmtLCDAT.parse(strREFDT))== 0)
    				{
        		        setMSG("Day closing is over ,Please Enter Invoice Date as "+L_strTEMP,'E');
        		        return false;
    				}*/
    			}
    			
		    }
		    catch(Exception L_E)
		    {
		        setMSG(L_E,"verify");
		        return false;
		    }
		    return true;
		}
	}
	
}
/*
for(int i=0;i<L_intROWID-1;i++)
		{
			if(tblINDTL.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
			if(tblINDTL.getValueAt(i,TBL_MATCD).toString().trim().equals(L_strMATCD.trim()))
			{
				setMSG("Duplicate entry ..",'E');
				return false;
			}
		}
*/