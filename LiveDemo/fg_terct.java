//Date 21/08/2006
//Zaheer Alam Khan

/**System Name : Finished Goods Information management System. 
 * Program for Finishing Good Receipt Entry (FG_TERCT)
 *  
 * Purpose: This program is used to enter the Finished Goods Receipt(Bagging Details) in the ware house, for specified date.
 */

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
//import cl_eml.*;
import javax.swing.JCheckBox;
import javax.swing.undo.*;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Calendar;	
import java.sql.ResultSet;
import javax.swing.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Cursor;
import java.sql.Timestamp;
import java.sql.CallableStatement;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>  Customer Order Booking Entry</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                  Form for Customer Order                  Entry by Regional Offices.       (To be extended to Distributors in future)&nbsp; </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Marketing System Enhancement Proposal by      Mr. SRD      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\fg_terct.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\fg_terct.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>10/11/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD></TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD></TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD></TD>    <TD> </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD></TD>    <TD></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class fg_terct extends cl_pbase //implements ChangeListener//,PopupMenuListener
{
	/**Constructor for the form<br>
	 * Retrieves market type/delivery type/payment type and transport type  options from CO_CDTRN and populates respective combos.<br>
	 * Starts thread for retrieving Distributor details along with curren year series for INDNO
	 */
	private CallableStatement cstLWMST;
	private JComboBox cmbWRHTP;/** String variable for W/H type box.*/	
    private JTextField txtLOTNO1,txtLOTNO2,txtPKGTP1,txtPKGTP2,txtMCHNO1,txtMNLCD1,txtMNLCD2,txtSHFCD,txtSHFCD2;
	private JTextField txtREMDS,txtTOTQT,txtTOTPK,txtISSRF,txtGINNO,txtDSPLT,txtEDITR,txtDSBEDT,txtJBWPRD,txtRCLNO1,txtRCLNO2,txtGRADE2,txtLOTDTNO, txtISSNO;
	private JCheckBox chkCHKFL1, chkCHKFL2;
	private TxtDate txtRCTDT;					//TxtLimit for Receipt Date
	//private TxtTime txtRCTTM;   
	private TxtLimit txtRCTTP;                  //TxtLimit for showing Receipt Type
	private TxtLimit txtRCTNO;					//TxtLimit for Receipt No 
	private TxtLimit txtLOTNO;                  //TxtLimit for LOt Number No 
	private TxtLimit txtPRDTP; /*,txtPRDCD;*/   // TxtLimit for Product Type 

	private TxtDate txtRETDAT;                  // TxtDate for  Transaction Date of Retention/Comp Tag
	private TxtLimit txtRETLOT;                 // TxtLimit for  Lot Number of Retention/Comp Tag
	private TxtLimit txtAUTFL;                   //TxtLimit for Auth SL
	//private String txtREFDT;                   
	private JPanel pnlRETCP;                    // Jpanel for Retention Completion Tag entry
	private JCheckBox chkINRDSP;
	private JCheckBox chkAUTOISS;
	private JCheckBox chkAUTISVW;
	private JCheckBox chkVWATIS;
	private JCheckBox chkRETCPL;              //JCheckBox for Completion & Retention Tag Entry
	private JCheckBox chkAUTOLOT;             //JCheckBox For AutoLot Generation 
	//private JLabel lblPRDCD,lblPRDCD1;
	private JLabel lblLOCBAG,lblMCHBAG,lblJBWPRD, lblJBWPRD1, lblTOL,lblDSPLT,lblGINNO, lblISSRF;
	
	Vector vtrCMPFL;
	private ButtonGroup btgRETCPL;              // ButtonGroup for adding Radio Button
	private JRadioButton rdbTRNDT;              //JRadioButton for Transaction Date Selection
	private JRadioButton rdbLOTNO;              //JRadioButton for Lot Number Selection
	ResultSet L_rstRSSET;  
	private cl_JTable tblRETCP;                //JTable for Showing the Retention Completion tag form
	private cl_JTable tblENTTB1;			   // JTable for Production Receipt table.*/	
	private cl_JTable tblENTTB2;			   // JTable variable for Non-Production Receipt table.*/
	
	private String strHLPFLD,strRCTNO,strRCTDT,strPRDDS,strSTRTM,strENDTM,strLOCQT,strNCSVL,strUCLTG,strHRSTR,strISSDT,strPKGCT,strAUTDT,strISSNO,strCPRCD,strTDSFL,strJBWPRD,strJBWLOT;
	private String strMCHNO,strMNLOC,strSTKTP,strGINNO,strISSRF,strMISCT,strSHFCD,strSTRCT,strENDCT,strBAGPK,strCCSVL,strCHP01,strPKGTP,strDSPETM,strDSPRET,strUPDSTR,strFILNM,strTPRCD,strJBWRCL,strBGRCLN;
	private String strRCTTP,strWRHTP,strACTTXT,strSQLSTR,strMNLCD,strPRDTP,strLOTNO,strRCLNO,strREMDS,strBAGQT,strCHP02,strSTSFL,strAUTBY,strPRDCD,strRCTQT,strUCLQT,strREFDT,strISSDT_AI,strSTRDT_AI,strENDDT_AI,strPRVDT_AI,strPRVDT1_AI,strERRSTR;
	String LM_TRNTP = "RC"; //RC is assigned to RM_TRNTP column within the FG_RMMST table
	private String strWHRSTR;  // String variable for Storing the Query   
	private JLabel lblRETCL;  //JLable for Showing the message on Retention Completion Tag window
	private String strNXDYDT; //Next Day Date String used to determine the next day date
									
	private int TB1_CHKFL = 0; // Element for production bagging  
    private int TB1_LOTNO = 1;
    private int TB1_RCLNO = 2;
    private int TB1_STRTM = 3;
    private int TB1_ENDTM = 4;
    private int TB1_REMDS = 5;
    private int TB1_STRCT = 6;
    private int TB1_ENDCT = 7;
    private int TB1_PKGTP = 8;
    private int TB1_BAGQT = 9;
    private int TB1_BAGPK = 10;
    private int TB1_MISCT = 11;
    private int TB1_MCHNO = 12;
    private int TB1_SHFCD = 13;
    private int TB1_MNLCD = 14;
    private int TB1_UCLTG = 15;
    private int TB1_AUTFL = 16;
	// Element for Non-production bagging	
    private int TB2_CHKFL = 0;
    private int TB2_LOTNO = 1;
    private int TB2_RCLNO = 2;
    private int TB2_PRDCD = 3;
    private int TB2_MNLCD = 4;
    private int TB2_PKGTP = 5;
    private int TB2_RCTQT = 6;
    private int TB2_RCTPK = 7;
    private int TB2_SHFCD = 8;
    private int TB2_AUTFL = 9;
	// Element for Retention comp tag
	private int TB3_CHKFL=0;
	private int TB3_PRDTP=1;
	private int TB3_RCLNO=2;
	private int TB3_LOTNO=3;
	private int TB3_GRADE=4;
	private int TB3_CYLNO=5;
	private int TB3_RETTG=6;
	private int TB3_CMPFL=7;
	private int intCNTRW =0;
	private int intCNTRW1 =0;
	private boolean flgCHK_EXIST = false; 
	private boolean flgLWMST = false;    // flag indicates that entries will be accepted in Table 1
	private boolean flgMODRCPT = false;   //Flag to indicate that receipt entry is continued in earlier(saved) records
	private boolean flgRCTNO_NEW = false; // Flage to indicate new Receipt No Generation
	private boolean flgLOTNO_NEW = false; //
	
	private int intCDTRN_TOT    = 9;			// Array elements for hstCDTRN 
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
	
	private Hashtable<String,String[]> hstCDTRN;		// Hash table for storing Code Transaction detail
	private Hashtable<String,String> hstCODDS;		// Hash table for storing Code Description
	private Vector<String[]> vtrHLPARR;		// Vector for help display array
	private Object[] arrHSTKEY;     //object variable for
	
	private boolean flgJBLOT = false;		//Job Lot Flag
	private boolean flgRMTCHK= false;		//Remote Database Updation Flag
	private boolean flgJOBFL = false;		//Processed Material Flag
	private boolean flgUPMTFG= false;		//Processed Material (Returned Unprocessed) Flag
	private boolean flgNOPFLG= false;		//Non-Production Receipt JTable Flag
	private boolean flgFRPRO = false;		//Production Receipt JTable Flag
	private boolean flgRTNFL = false;		//Sales Return Flag
	private boolean flgSTKAD = false;		//Stock Adjustment Flag
	private boolean flgRPKFL = false;		//Repacking Flag
	private boolean flgINVFL = false;		//Invoice Availability for Sales Return
	private boolean flgRCTFL = false;       //for setting InValid Lot Message
	private boolean flgEMPFL = false;       //for empty field checking
	private boolean flgLOCFL = false;       //for invalid location checking
	private boolean flgLTRCT = false;       // Lot Receipt Flag for Receipt Type(22,23)
	
    private String strYRDGT;         //String Variable for taking Year Digit
	//private String strCOFFDT = "09/22/2008";    //Cut-off date for Auto-issue Generation i.e. Dtae from which Aut-Issue feature is implemented
	private String strCOFFDT = "10/09/2008";    //Cut-off date for Auto-issue Generation i.e. Dtae from which Aut-Issue feature is implemented
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;
	private String L_strSQLQRY;	
	private String strFRSPD="10";
	private String strREPAC="15";
	private String strJBWRC="21";
	private String strJBWLT="22";
	private String strSLSRT="30";
	
	private CallableStatement calFGLWM;
	private CallableStatement calFGRCT;
	//private CallableStatement calDATFL;
	private CallableStatement calPTTRN;
	private CallableStatement calPLTRN;
	double dblAVLQT=0;
	private String strSTRTM_DAY = "07:00";
	cl_eml ocl_eml = new cl_eml();
	
	
	fg_terct()
	{
		super(2);
		try
		{
			hstCDTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			vtrHLPARR = new Vector<String[]>();
			
			hstCDTRN.clear();
            hstCODDS.clear();
			vtrHLPARR.clear();
			
			setMatrix(20,12);
			add(new JLabel("W/H type"),1,1,1,1,this,'L');
			add(cmbWRHTP = new JComboBox(),2,1,1,0.75,this,'L');
			cmbWRHTP.addItem("01");
			
			add(new JLabel("Receipt Type"),1,2,1,2,this,'L');
			add(txtRCTTP = new TxtLimit(2),2,2,1,2,this,'L');
			
			add(new JLabel("Receipt Date"),1,4,1,2,this,'L');
			add(txtRCTDT = new TxtDate(),2,4,1,2,this,'L');
			
			add(new JLabel("Prd.Type"),1,6,1,2,this,'L');
			add(txtPRDTP = new TxtLimit(2),2,6,1,2,this,'L');
			
			add(new JLabel("Receipt.No"),1,8,1,2,this,'L');
			add(txtRCTNO = new TxtLimit(8),2,8,1,2,this,'L');
			
			add(new JLabel("Remark"),3,2,1,1,this,'L');
			add(txtREMDS = new JTextField(),4,2,1,2,this,'L');
		
			add(new JLabel("G.In No"),3,4,1,2,this,'L');
			add(txtGINNO = new TxtLimit(8),4,4,1,2,this,'L');
			add(new JLabel("Inv.No"),3,6,1,2,this,'L');
			add(txtISSRF = new TxtLimit(8),4,6,1,2,this,'L');
			add(new JLabel("Qty.Total"),3,8,1,1,this,'L');
			add(txtTOTQT = new TxtLimit(12),4,8,1,2.7,this,'L');
			add(new JLabel("Pkg.Total"),3,11,1,1,this,'L');
			add(txtTOTPK = new TxtLimit(12),4,11,1,2,this,'L');
			add(chkRETCPL = new JCheckBox("Retention/Cmpl.Tag"),2,11,1,3,this,'L');
			//add(new JLabel("Retention/Cmpl.Tag"),2,12,1,1,this,'L');
						
			add(new JLabel("Production Receipt(Bagging)"),5,1,1,5,this,'L');
			tblENTTB1=crtTBLPNL1(this,new String[]{"Status","Lot No.","Rcl.","Start Time","End Time","Remark","Start Count","End Count","Package type","Qty Bag","Pkgs","Miss Count","M/C","Shift","Main L.","UCL","Auth"},50,6,1,5,12,new int[]{20,65,30,45,45,55,70,70,40,50,50,30,40,30,40,40,35},new int[]{0});
			tblENTTB1.setInputVerifier(new TBLINPVF());
			tblENTTB1.addKeyListener(this);
			tblENTTB1.setCellEditor(TB1_CHKFL,chkCHKFL1 = new JCheckBox());
			tblENTTB1.setCellEditor(TB1_LOTNO,txtLOTNO1 = new TxtLimit(8));
			tblENTTB1.setCellEditor(TB1_RCLNO,txtRCLNO1 = new TxtLimit(2));
			chkCHKFL1.addKeyListener(this);
			txtLOTNO1.addKeyListener(this);
			txtRCLNO1.addFocusListener(this);
			txtLOTNO1.addFocusListener(this);
			txtRCLNO1.addKeyListener(this);
					
			tblENTTB1.setCellEditor(TB1_PKGTP,txtPKGTP1 = new TxtLimit(2));
			txtPKGTP1.addKeyListener(this);
			
			tblENTTB1.setCellEditor(TB1_MCHNO,txtMCHNO1 = new TxtLimit(2));
			txtMCHNO1.addKeyListener(this);
		
			tblENTTB1.setCellEditor(TB1_MNLCD,txtMNLCD1 = new TxtLimit(8));
			txtMNLCD1.addKeyListener(this);
			
			tblENTTB1.setCellEditor(TB1_SHFCD,txtSHFCD = new TxtLimit(1));
			txtSHFCD.addKeyListener(this);
			tblENTTB1.setCellEditor(TB1_AUTFL,txtAUTFL = new TxtLimit(10));
			txtAUTFL.addKeyListener(this);
			
			add(new JLabel("Non-Production Receipt"),11,1,1,5,this,'L');
			tblENTTB2=crtTBLPNL1(this,new String[]{"Status","Lot No.","Rcl.No","Grade","Main Loc","Package Type","RctQty","RctPkgs","Shift","Auth.St."},50,12,1,5,12,new int[]{20,100,85,95,75,75,75,75,75,80},new int[]{0});
			
			tblENTTB2.setInputVerifier(new TBLINPVF());
			tblENTTB2.addKeyListener(this);
			tblENTTB2.setCellEditor(TB2_CHKFL,chkCHKFL2 = new JCheckBox());
			tblENTTB2.setCellEditor(TB2_LOTNO,txtLOTNO2 = new TxtLimit(8));
			
			chkCHKFL2.addKeyListener(this);
			txtLOTNO2.addKeyListener(this);
			txtLOTNO2.addFocusListener(this);
			tblENTTB2.setCellEditor(TB2_RCLNO,txtRCLNO2 = new TxtLimit(2));
			txtRCLNO2.addKeyListener(this);
			txtRCLNO2.addFocusListener(this);
			
			tblENTTB2.setCellEditor(TB2_MNLCD,txtMNLCD2 = new TxtLimit(8));
			txtMNLCD2.addKeyListener(this);
			txtMNLCD2.addFocusListener(this);
			
			tblENTTB2.setCellEditor(TB2_PKGTP,txtPKGTP2 = new TxtLimit(8));
			txtPKGTP2.addKeyListener(this);
			txtPKGTP2.addFocusListener(this);
			
			tblENTTB2.setCellEditor(TB2_SHFCD,txtSHFCD2 = new TxtLimit(1));
			txtSHFCD2.addKeyListener(this);
			txtSHFCD2.addFocusListener(this);
			
			add(chkINRDSP = new JCheckBox("Indirect Receipt Display",false),17,1,1,2.5,this,'L');
			add(chkAUTOISS = new JCheckBox("Auto Issue Generation",false),18,1,1,2.5,this,'L');
			add(new JLabel("Issue No."),18,5,1,2,this,'L');
			add(txtISSNO = new TxtLimit(8),18,6,1,2,this,'L');
			add(chkAUTISVW = new JCheckBox(" View Auto Issue ",false),18,8,1,2.5,this,'L');
			
			add(chkAUTOLOT = new JCheckBox("Auto Lot Generation",true),17,4,1,2.5,this,'L');
			
			INPVF objFGIPV=new INPVF();
		
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objFGIPV);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
				}
			}
			chkAUTOLOT.setVisible(false);
			getREFDT();
			crtCDTRN("'SYSFGXXRTP','MSTCOXXPRD','SYSFGXXPKG','SYSFGXXMCH','SYSPRXXCYL','M"+cl_dat.M_strCMPCD_pbst+"COXXSHF'","",hstCDTRN);//create code transaction
			strYRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4);
			System.out.println("strYRDGT "+strYRDGT);
			
			calFGLWM =cl_dat.M_conSPDBA_pbst.prepareCall("{call updFGLWM_AUT(?,?,?,?,?)}");
			calFGRCT =cl_dat.M_conSPDBA_pbst.prepareCall("{call updFGRCT_AUT(?,?,?,?,?)}");
			//calDATFL =cl_dat.M_conSPDBA_pbst.prepareCall("{call updEXDATFL(?,?,?)}");
			//calPTTRN =cl_dat.M_conSPDBA_pbst.prepareCall("{call updPTTRN_FGR(?,?)}");
			//calPLTRN =cl_dat.M_conSPDBA_pbst.prepareCall("{call updPLTRN_FGR(?,?)}");
			
			setENBL(false);
			chkAUTOISS.setEnabled(false);
			chkAUTISVW.setEnabled(true);
		}
		catch (Exception e)
		{
			setMSG(e,"Child.Constructor");
		}
		
	}
	/**
	 *  Function for setting enabled or disabled 
	*/
	void setENBL(boolean L_STAT)
	{
		try
		{
			super.setENBL(L_STAT);// default false
			txtGINNO.setEnabled(false);
			txtISSRF.setEnabled(false);
			txtTOTQT.setEnabled(false);
			txtTOTPK.setEnabled(false);
			chkRETCPL.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				txtRCTNO.setEnabled(false);
			}
			((JTextField) tblENTTB2.cmpEDITR[TB2_PRDCD]).setEnabled(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setENBL");
		}
	}	
/**
 * Function for getting Reference Date
*/	
	public void getREFDT()//get reference date
	{
		try
		{
			Date L_strTEMP=null;
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD in ('DOCDT','AUTIS')";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET == null)
				return;
			//String L_strREFDT = "";
			while(L_rstRSSET.next())
			{
				if(L_rstRSSET.getString("CMT_CODCD").equalsIgnoreCase("DOCDT"))
				{
					strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
					//L_strREFDT = strREFDT;
					M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
					M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
					strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				}
				else if(L_rstRSSET.getString("CMT_CODCD").equalsIgnoreCase("AUTIS"))
				{
					strSTRDT_AI = L_rstRSSET.getString("CMT_CHP01").trim();
					strENDDT_AI = L_rstRSSET.getString("CMT_CHP02").trim();
					strPRVDT_AI = L_rstRSSET.getString("CMT_CCSVL").trim();
					M_calLOCAL.setTime(M_fmtLCDAT.parse(strPRVDT_AI));   
					M_calLOCAL.add(Calendar.DATE,+1);                     
					strPRVDT1_AI = M_fmtLCDAT.format(M_calLOCAL.getTime()); 
					//System.out.println("strPRVDT_AI : "+strPRVDT_AI);
					//System.out.println("strPRVDT1_AI : "+strPRVDT1_AI);
					
				}
			}
			strPRVDT1_AI = (M_fmtLCDAT.parse(strPRVDT1_AI).compareTo(M_fmtLCDAT.parse(strREFDT))>0 ? strREFDT : strPRVDT1_AI);
			strPRVDT1_AI = (M_fmtLCDAT.parse(strPRVDT1_AI).compareTo(M_fmtLCDAT.parse(strENDDT_AI))>0 ? strENDDT_AI : strPRVDT1_AI);
			//System.out.println("strPRVDT1_AI : "+strPRVDT1_AI);
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
		}
	}
	/**<b>TASKS :</b><br>
	 * &nbsp&nbsp&nbspSource=cmbPMTCD/cmbDTPCD : Show/hide related details' fields depending on item selected<br>&nbsp&nbsp&nbsp&nbsp&nbspSelection Index used for this. There fore, change in display order should br taken care
	 * &nbsp&nbsp&nbspSource=cmbINDNO : Display respective Distributor details in address and ORDNO text Fields<br>	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		//strACTTXT = L_AE.getActionCommand();
		try
		{	
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
					setENBL(false);
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
				{
					setENBL(true);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
				{
					setENBL(true);
					
					tblENTTB1.cmpEDITR[TB1_LOTNO].setEnabled(false);
				}
				else
				{
					setENBL(false);
					cmbWRHTP.setEnabled(true);
					txtRCTTP.setEnabled(true);
					txtPRDTP.setEnabled(true);
					txtRCTNO.setEnabled(true);
					
				}
				chkAUTOISS.setEnabled(false);
			}
			if(M_objSOURC==chkAUTOISS)
			{
				int L_intOPTN=0;
				if(chkAUTOISS.isSelected())
				{	
					setCursor(cl_dat.M_curWTSTS_pbst);
					L_intOPTN=JOptionPane.showConfirmDialog( this,"Generate Auto-Issue for PS ? ",null,JOptionPane.OK_CANCEL_OPTION);
					if(L_intOPTN==0)
  					 	exeISSUE_NOTE_GEN("'01'","302");
					L_intOPTN=JOptionPane.showConfirmDialog( this,"Generate Auto-Issue for SPS ? ",null,JOptionPane.OK_CANCEL_OPTION);
					if(L_intOPTN==0)
						exeISSUE_NOTE_GEN("'02','05'","111");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				chkAUTOISS.setSelected(false);
			}
			if(M_objSOURC==chkAUTISVW)
			{
				if(chkAUTISVW.isSelected())
				{	
					setCursor(cl_dat.M_curWTSTS_pbst);
					exeISSUE_VW();
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				chkAUTISVW.setSelected(false);
			}
			if(M_objSOURC==chkAUTOLOT)
			{
				if(chkAUTOLOT.isSelected())
				{
					//if(txtRCTTP.getText().trim().equals("21") && txtPRDTP.getText().trim().equals("02"))
					if(flgJOBFL && txtPRDTP.getText().trim().equals("02"))
					{
						strPRDCD = getJOBPRD();
						//System.out.println("STRPRODUCT CODE"+strPRDCD);
						((JTextField) tblENTTB2.cmpEDITR[TB2_LOTNO]).setText(getLOTNO());
					}
				}
			}
			if(M_objSOURC==chkRETCPL)
			{
				if(chkRETCPL.isSelected())
				{
					getRETCPL();// call retention comp tag method
				}
			}
			if(M_objSOURC==rdbTRNDT)// if selected date 
				{txtRETLOT.setVisible(rdbTRNDT.isSelected() ? false : true); txtRETDAT.setVisible(rdbTRNDT.isSelected() ? true : false);}
			
			
			if(M_objSOURC==rdbLOTNO)// if selected lotno
				{txtRETLOT.setVisible(rdbLOTNO.isSelected() ? true : false); txtRETDAT.setVisible(rdbLOTNO.isSelected() ? false : true);}
			
			if(M_objSOURC==txtRCTTP)
			{
				if(flgLWMST)
				{
					tblENTTB2.setEnabled(false);
					tblENTTB1.setEnabled(true);
				}
				else
				{
					tblENTTB1.setEnabled(false);
					tblENTTB2.setEnabled(true);
					if(!flgRTNFL)
					{
						txtGINNO.setText("");
						txtISSRF.setText("");
						txtGINNO.setEnabled(false);
						txtISSRF.setEnabled(false);
					}
					else
					{
						txtGINNO.setEnabled(true);
						txtISSRF.setEnabled(true);
					}
				}
			}
			if(M_objSOURC==txtISSNO)
			{
				M_strSQLQRY = "select max(lw_issrf)lw_issrf from fg_lwmst where lw_issrf is not null and lw_issrf like '"+ cl_dat.M_strFNNYR_pbst.substring(3)+"%'";
				  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				 // System.out.println(">>>>SetText>>>>"+M_strSQLQRY);
				  if(M_rstRSSET != null)   
			      { 				 
			        while(M_rstRSSET.next())
					{
				      txtISSNO.setText( M_rstRSSET.getString("lw_issrf"));
					}
			      }  
			}
			
		}
		catch(Exception e)
		{
			setMSG(e,"Child.actionPerformed");
		}
	}	
	/**
	 *  Function which create form for retention comp tag 
	*/
	private void getRETCPL()
	{
		ResultSet L_rstRSSET = null;
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(pnlRETCP==null)
			{
				pnlRETCP=new JPanel(null);
				btgRETCPL=new ButtonGroup();
				rdbTRNDT=new JRadioButton("Transaction Date",true);
				rdbLOTNO=new JRadioButton("Lot No");
				btgRETCPL.add(rdbTRNDT);
				btgRETCPL.add(rdbLOTNO);
				lblRETCL=new JLabel("LOt Details for Completion & Retention Tag Entry");
				String[] L_staCOLHD = {"Sel","Prod Type","Rcl No","Lot No","Grade","Silo No","Retention Tag","Completion Tag"};
				int[] L_inaCOLSZ = {20,60,50,100,110,100,90,90};
				tblRETCP = crtTBLPNL1(pnlRETCP,L_staCOLHD,30,4,1,6,10,L_inaCOLSZ,new int[]{0,6,7});
				tblRETCP.setInputVerifier(new TBLINPVF());
				tblRETCP.setRowSelectionInterval(0,0);
				tblRETCP.setColumnSelectionInterval(0,0);
				add(lblRETCL,1,1,1,6,pnlRETCP,'L');
				//add(btgRETCPL,2,1,1,4,pnlRETCP,'L');
				add(rdbTRNDT,2,1,1,2,pnlRETCP,'L');
				add(rdbLOTNO,3,1,1,2,pnlRETCP,'L');
				add(txtRETLOT = new TxtLimit(8),3,3,1,1,pnlRETCP,'L');
				add(txtRETDAT = new TxtDate(),3,3,1,1,pnlRETCP,'L');
			}
			tblRETCP.clrTABLE();
			txtRETDAT.setText(txtRCTDT.getText());
			rdbTRNDT.setSelected(true);
			txtRETDAT.setVisible(true);
			txtRETLOT.setVisible(false);
			setCursor(cl_dat.M_curDFSTS_pbst);
			pnlRETCP.setSize(100,100);
			pnlRETCP.setPreferredSize(new Dimension(700,250));
			boolean L_flgEXIST = false;
			cl_dat.M_flgLCUPD_pbst = true;
			getRETLOT();
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlRETCP,"Retention/Cmpl.Tag",JOptionPane.OK_CANCEL_OPTION);
			if(L_intOPTN==0)
			{
				updLTMST();
			}				
		}
		catch (Exception L_EX)
		{
			setMSG("Error in  getRETCPL: "+L_EX,'E');
		}
	}
	/**User friendly messagees	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(L_FE.getSource().equals(txtRCTTP))
			{
	   			setMSG("Press F1 to select the Receipt Type ",'N');
			}
			if(L_FE.getSource().equals(txtRCTDT))
			{
                setMSG("Enter Test Date in format dd/mm/yyyy",'N');
			}
			if(L_FE.getSource().equals(txtREMDS))
			{
   				setMSG("Enter Remark. ",'N');
			}	
			if(L_FE.getSource().equals(txtPRDTP))
			{
   				setMSG("Press F1 to select the Product Type. ",'N');
			}
			
			if(M_objSOURC==txtLOTNO1) 
			{
				setMSG("Press F1 to select the Lot No. & then press Enter.",'N');
				if(tblENTTB1.getSelectedRow()<intCNTRW)
					((JTextField)tblENTTB1.cmpEDITR[TB1_LOTNO]).setEditable(false);
				else
					((JTextField)tblENTTB1.cmpEDITR[TB1_LOTNO]).setEditable(true);
			}
				
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_RCLNO])) 
			{
				setMSG("Enter Reclassification Number ",'N');
				if(tblENTTB1.getSelectedRow()<intCNTRW)
					((JTextField)tblENTTB1.cmpEDITR[TB1_RCLNO]).setEditable(false);
				else
					((JTextField)tblENTTB1.cmpEDITR[TB1_RCLNO]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM])) 
			{
				setMSG("Enter Start Time",'N');
				if(tblENTTB1.getSelectedRow()<intCNTRW)
					((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM]).setEditable(false);
				else
					((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_ENDTM])) 
			{
				setMSG("Enter End Time",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_REMDS])) 
			{
					setMSG("Enter Remark",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_STRCT])) 
			{
				setMSG("Enter Start Count",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_ENDCT])) 
			{
				setMSG("Enter End Count",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_PKGTP])) 
			{
				setMSG("Enter Package type or press F1 for Help..",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_BAGQT])) 
			{
				setMSG("Qty Bagged",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_BAGPK])) 
			{
				setMSG("Enter Bagged Packages",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_MISCT])) 
			{
				setMSG("Enter Miss count",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_MCHNO])) 
			{
				setMSG("Enter M/C No or press F1 for help..  ",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_SHFCD])) 
			{
				setMSG("Enter Shift",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_MNLCD])) 
			{
				setMSG("Enter Main Location or perss F1 for help..",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_UCLTG])) 
			{
				setMSG("Enter UCL Tag",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_AUTFL])) 
			{
				setMSG("Enter Auth",'N');
			}
			if(M_objSOURC==txtLOTNO2) 
			{
				setMSG("Press F1 to select the Lot No. & then press Enter.",'N');	
				if(tblENTTB2.getSelectedRow()<intCNTRW1)
					((JTextField)tblENTTB2.cmpEDITR[TB2_LOTNO]).setEditable(false);
				else
					((JTextField)tblENTTB2.cmpEDITR[TB2_LOTNO]).setEditable(true);
			}
			if(M_objSOURC==txtRCLNO2) 
			{
				setMSG("Enter Reclassification Number ",'N');
				if(tblENTTB2.getSelectedRow()<intCNTRW1)
					((JTextField)tblENTTB2.cmpEDITR[TB2_RCLNO]).setEditable(false);
				else
					((JTextField)tblENTTB2.cmpEDITR[TB2_RCLNO]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblENTTB2.cmpEDITR[TB2_PRDCD])) 
			{
				setMSG("Enter Grade",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB2.cmpEDITR[TB2_MNLCD])) 
			{
				setMSG("Enter Main Locationr or press F1 for help ",'N');
				if(tblENTTB2.getSelectedRow()<intCNTRW1)
					((JTextField)tblENTTB2.cmpEDITR[TB2_MNLCD]).setEditable(false);
				else
					((JTextField)tblENTTB2.cmpEDITR[TB2_MNLCD]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblENTTB2.cmpEDITR[TB2_PKGTP])) 
			{
				setMSG("Enter Package type or press F1 for help..",'N');
				if(tblENTTB2.getSelectedRow()<intCNTRW1)
					((JTextField)tblENTTB2.cmpEDITR[TB2_PKGTP]).setEditable(false);
				else
					((JTextField)tblENTTB2.cmpEDITR[TB2_PKGTP]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblENTTB2.cmpEDITR[TB2_RCTQT])) 
			{
				setMSG("Enter Receipt Quantity",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB2.cmpEDITR[TB2_SHFCD])) 
			{
				setMSG("Enter Shift",'N');
			}
			if(M_objSOURC==((JTextField)tblENTTB2.cmpEDITR[TB2_AUTFL])) 
			{
				setMSG("Enter Auth",'N');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"TEIND.FocusGained"+M_objSOURC);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**<b>TASKS : </B><br>
	 * Source = cmbMKTTP : start thread for collecting tax details	 */
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
		}
		catch(Exception e)
		{
			setMSG(e,"Child.FocusLost");
			
		}
	}
	/**<b>TASKS : </b><br>
		 * &nbsp&nbsp&nbspSource = txtBYRCD : HELP by buyer code(F2) as well as buyer name(F1)<br>
		* &nbsp&nbsp&nbspSource = txtCONCD : HELP by consignee code(F2) as well as consignee name(F1)<br> 
		 * &nbsp&nbsp&nbspFocus navigation
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
		try
		{
			if(L_KE.getKeyCode()==L_KE.VK_ENTER) //virtual key Enter 
			{
				if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
					{
						cmbWRHTP.requestFocus();
					}
					else
					{
						setENBL(false);
					}
				}
				if(M_objSOURC==cmbWRHTP)
				{
					txtRCTTP.requestFocus();
				}
				if(M_objSOURC==txtRCTTP)
				{
					if(vldRCTTP())
					{
						if(!hstCDTRN.containsKey("SYSFGXXRTP"+txtRCTTP.getText()))// receipt type check in Hashtable
						{
							setMSG("Invalid Receipt Type Enter",'E');	
							txtRCTTP.requestFocus(); 
							return;
						}
						//if((txtRCTTP.getText().equals("30"))&& (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)))
						//if((txtRCTTP.getText().equals(strSLSRT))&& (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)))
						if(flgRTNFL && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)))
						{
						txtGINNO.setEnabled(true);
						txtISSRF.setEnabled(true);
						}
						((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)) ?   txtRCTDT : txtPRDTP).requestFocus(); 
					}
				}
				if(M_objSOURC==txtRCTDT)
				{
					//((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst)) ?    txtREMDS : txtRCTDT).requestFocus();
					txtPRDTP.requestFocus(); 
				}
				if(M_objSOURC==txtRCTNO)
				{
					//((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst)) ?    txtREMDS : txtRCTDT).requestFocus();
					txtREMDS.requestFocus(); 
				}
				if(M_objSOURC==txtREMDS)
				{
					//txtPRDTP.requestFocus();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					{
						if(!flgLWMST)
						{
							if(flgRTNFL)
								txtGINNO.requestFocus();
							else
							{
								tblENTTB2.setRowSelectionInterval(tblENTTB2.getSelectedRow(),tblENTTB2.getSelectedRow());		
								tblENTTB2.setColumnSelectionInterval(TB2_LOTNO,TB2_LOTNO);		
								tblENTTB2.editCellAt(tblENTTB2.getSelectedRow(),TB2_LOTNO);
								tblENTTB2.cmpEDITR[TB2_LOTNO].requestFocus();
							}
						}
						else
						{
							tblENTTB1.setRowSelectionInterval(tblENTTB1.getSelectedRow(),tblENTTB1.getSelectedRow());		
							tblENTTB1.setColumnSelectionInterval(TB1_LOTNO,TB1_LOTNO);		
							tblENTTB1.editCellAt(tblENTTB1.getSelectedRow(),TB1_LOTNO);
							tblENTTB1.cmpEDITR[TB1_LOTNO].requestFocus();
						}
					}
					else
					{
						if(!flgLWMST)
						{
							if(flgRTNFL)
								txtGINNO.requestFocus();
							else
							{
								tblENTTB2.setRowSelectionInterval(tblENTTB2.getSelectedRow(),tblENTTB2.getSelectedRow());		
								tblENTTB2.setColumnSelectionInterval(TB2_LOTNO,TB2_LOTNO);		
								tblENTTB2.editCellAt(tblENTTB2.getSelectedRow(),TB2_LOTNO);
								tblENTTB2.cmpEDITR[TB2_LOTNO].requestFocus();
							}
						}
						else
						{
							tblENTTB1.setRowSelectionInterval(tblENTTB1.getSelectedRow(),tblENTTB1.getSelectedRow());		
							tblENTTB1.setColumnSelectionInterval(TB1_LOTNO,TB1_LOTNO);		
							tblENTTB1.editCellAt(tblENTTB1.getSelectedRow(),TB1_LOTNO);
							tblENTTB1.cmpEDITR[TB1_LOTNO].requestFocus();
						}
					}
						
				}
				if(M_objSOURC==txtPRDTP)
				{
					txtRCTNO.setText("");
					chkRETCPL.setEnabled(true);
					tblENTTB1.clrTABLE();
					tblENTTB2.clrTABLE();
					//if(txtRCTTP.getText().equals("21") || txtRCTTP.getText().equals("22") || txtRCTTP.getText().equals("30"))
					//if(txtRCTTP.getText().equals(strJBWRC) || txtRCTTP.getText().equals(strJBWLT) || txtRCTTP.getText().equals(strSLSRT))
					flgMODRCPT = false;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					{
							if(flgLWMST)
								setLWMST();
							else
								setRCTRN();
							txtREMDS.requestFocus();
					}
					else
					{
						txtRCTNO.requestFocus();
					}
				}
				if(M_objSOURC==txtGINNO)
				{
					if(exeGINNO())
					{
						txtISSRF.setEnabled(true);
						txtISSRF.requestFocus();
					}
					else
					{
						setMSG("Invalid Gate In No.",'E');
						txtGINNO.requestFocus();
					}
				}
				if(M_objSOURC==txtISSRF)
				{
					if(txtISSRF.getText().trim().length()!=8)
					{
						setMSG("Invalid Invoice Number Enter",'N');
						txtISSRF.requestFocus();
					}
					else
					{	
						tblENTTB2.setRowSelectionInterval(tblENTTB2.getSelectedRow(),tblENTTB2.getSelectedRow());		
						tblENTTB2.setColumnSelectionInterval(TB2_LOTNO,TB2_LOTNO);		
						tblENTTB2.editCellAt(tblENTTB2.getSelectedRow(),TB2_LOTNO);
						tblENTTB2.cmpEDITR[TB2_LOTNO].requestFocus();
					}
				}
				if(M_objSOURC==txtRETDAT)
				{
					getRETLOT();
				}
				if(M_objSOURC==txtRETLOT)
				{
					getRETLOT();
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_LOTNO])) 
				{
					setMSG("Enter Reclassification Number ",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_RCLNO])) 
				{
					setMSG("Enter Start Time",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM])) 
				{
					setMSG("Enter End Time",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_ENDTM])) 
				{
					setMSG("Enter Remark",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_REMDS])) 
				{
					setMSG("Enter Start Count",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_STRCT])) 
				{
					setMSG("Enter End Count",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_ENDCT])) 
				{
					setMSG("Enter Package type or press F1 for Help..",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_PKGTP])) 
				{
					setMSG("Qty Bagged",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_BAGQT])) 
				{
					setMSG("Enter Bagged Packages",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_BAGPK])) 
				{
					setMSG("Enter Miss count",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_MISCT])) 
				{
					setMSG("Enter M/C No or press F1 for help..  ",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_MCHNO])) 
				{
					setMSG("Enter Shift",'N');
				}
				if(M_objSOURC==((JTextField)tblENTTB1.cmpEDITR[TB1_SHFCD])) 
				{
					setMSG("Enter Main Location or perss F1 for help..",'N');
				}
			}
			if(L_KE.getKeyCode()==L_KE.VK_F1) // virtual key F1
			{
				if(M_objSOURC==txtRCTTP)// Create F1 for Receipt Type
				{
					M_strHLPFLD = "txtRCTTP";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and";
				    M_strSQLQRY +=" CMT_CGSTP = 'FGXXRTP' and CMT_CHP02 <> '4' order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[] {"Code","Description"},2,"CT");
				}
				if(M_objSOURC==txtRCTNO)   // Create F1 for  Receipt Number
				{
					M_strHLPFLD = "txtRCTNO";
					if(flgLWMST)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)))
					    {
							M_strSQLQRY = "Select distinct LW_RCTNO,LW_RCTDT,LW_PRDTP from FG_LWMST where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and LW_RCTTP='"+txtRCTTP.getText()+"' AND LW_PRDTP='"+txtPRDTP.getText().trim()+"' and LW_STSFL = '1' order by LW_RCTDT desc,lw_rctno desc";
					    }
						else
							M_strSQLQRY = "Select distinct LW_RCTNO,LW_RCTDT,LW_PRDTP from FG_LWMST where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and LW_RCTTP='"+txtRCTTP.getText()+"' AND LW_PRDTP='"+txtPRDTP.getText().trim()+"' and LW_STSFL <> 'X' order by LW_RCTDT desc,lw_rctno desc";
					}
					else if(!flgLWMST)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)))
					    {
							M_strSQLQRY = "Select distinct RCT_RCTNO,RCT_RCTDT,RCT_PRDTP from FG_RCTRN where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and RCT_RCTTP='"+txtRCTTP.getText()+"' AND RCT_PRDTP='"+txtPRDTP.getText().trim()+"' and RCT_STSFL = '1' order by RCT_RCTDT desc,rct_rctno desc";
						}
						else
						{
							M_strSQLQRY = "Select distinct RCT_RCTNO,RCT_RCTDT,RCT_PRDTP from FG_RCTRN where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and RCT_RCTTP='"+txtRCTTP.getText()+"' AND RCT_PRDTP='"+txtPRDTP.getText().trim()+"' and RCT_STSFL <> 'X' order by RCT_RCTDT desc,rct_rctno desc";
						}
					}
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Receipt Number","Receipt Date","Product Type"},3,"CT");
				}
				if(M_objSOURC==txtPRDTP)   //Create F1 for production type
				{
					M_strHLPFLD = "txtPRDTP";
					cl_hlp(getHLPVTR("PRDTP",txtPRDTP.getText()),2,1,new String[] {"Code","Description"},2,"CT");
				}
				if(M_objSOURC==txtGINNO)  //F-1 on Gate-In No.
				{
					
					M_strHLPFLD = "txtGINNO";
					String L_ARRHDR[] ={ "G.In No.","G.In Date","Inv.No."};
					if(!flgRTNFL)
						return;
					M_strSQLQRY = "Select distinct WB_DOCNO, CONVERT(varchar,WB_REPTM,103) WB_DOCDT, WB_PORNO from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '04' and WB_STSFL <> 'X'  order by WB_DOCNO desc ";			 
					//System.out.println("Gate in no ="+M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"G.In No.","G.In Date","Inv.No."},3,"CT");
				}
				if(M_objSOURC==txtISSRF) 
				{
					M_strHLPFLD = "txtISSRF";
					if(flgRTNFL)
					{
						String L_strMKTTP = "";
						if(txtPRDTP.getText().equals("01") ||txtPRDTP.getText().equals("02"))
							L_strMKTTP = "01";
						else
							L_strMKTTP = txtPRDTP.getText();
						M_strSQLQRY = "Select distinct IVT_INVNO,IVT_INVDT from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP =  '"+L_strMKTTP+"' and ivt_invqt>0  and len(ivt_invno)=8";
						//M_strSQLQRY += " AND IVT_BYRCD=PT_PRTCD AND PT_PRTTP='C' ";     
						if(txtISSRF.getText().trim().length()>0)
						{
							M_strSQLQRY += " AND IVT_INVNO like '"+txtISSRF.getText().trim() +"%'";
						}
						M_strSQLQRY += " order by IVT_INVDT desc";
					}
					
					else
					{
						M_strSQLQRY = "Select distinct IST_ISSNO,IST_AUTDT from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+cmbWRHTP.getSelectedItem()+"' and IST_ISSTP = '"+txtRCTTP.getText()+"' and IST_PRDTP = '"+txtPRDTP.getText()+"' and IST_STSFL = '2'";
					
						if(txtISSRF.getText().trim().length()>0)
						{
							M_strSQLQRY += " AND IST_ISSNO like '"+txtISSRF.getText().trim() +"%'";
						}
						M_strSQLQRY += "  order by IST_ISSNO desc";
					}					
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Issue/Invoice No.","Issue/Invoice Date"},2,"CT");
				}
				if(M_objSOURC == txtLOTNO1)   //Create F1 for Table1 Lot No
				{
					M_strHLPFLD = "txtLOTNO1";
					M_strSQLQRY = "Select LT_LOTNO,SUBSTRING(PR_PRDDS,1,15) PR_PRDDS,max(LT_RCLNO) LT_RCLNO from pr_ltmst,co_prmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ltrim(str(LT_PRDCD,20,0))=PR_PRDCD and (LT_RCTRF is null or LT_RCTRF=' ')";
					if(txtLOTNO1.getText().trim().length()>0)
					{
						M_strSQLQRY += " AND LT_LOTNO like '"+txtLOTNO1.getText().trim() + "%'";
					}
					M_strSQLQRY += " and LT_PRDTP='"+txtPRDTP.getText()+"' and LT_STSFL = '0'";
					M_strSQLQRY += " group by LT_LOTNO,SUBSTRING(PR_PRDDS,1,15) order by LT_LOTNO,PR_PRDDS,LT_RCLNO desc";
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"LOT NO.","Grade","RCl No."},3,"CT");//
				}
				if(M_objSOURC == txtPKGTP1)//Create F-1 on Package Type for Production JTable
				{
					M_strHLPFLD = "txtPKGTP1";
					cl_hlp(getHLPVTR("PKGTP1",((JTextField)tblENTTB1.cmpEDITR[TB1_PKGTP]).getText()) ,2,1,new String[] {"Code","Description"},2,"CT");
					
				}
				if(M_objSOURC == txtMCHNO1)	//Create F-1 on Machine No. for Production JTable
				{
					M_strHLPFLD = "txtMCHNO1";
					cl_hlp(getHLPVTR("MCHNO",((JTextField)tblENTTB1.cmpEDITR[TB1_MCHNO]).getText()) ,2,1,new String[] {"Code","Description"},2,"CT");
				}
				if(M_objSOURC == txtMNLCD1)// Create	F-1 on Main Location of Production JTable
				{
					M_strHLPFLD = "txtMNLCD1";
					M_strSQLQRY= "Select LC_MNLCD,(LC_MAXQT-LC_STKQT)LC_AVLQT from FG_LCMST where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LC_STSFL not in ('X','2') ORDER BY LC_MNLCD";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Main Loc.","Allowable Qty"},2,"CT");
				}
				if(M_objSOURC == txtLOTNO2)// Create F-1 on Lot No. for Non-Production
				{
					M_strHLPFLD = "txtLOTNO2";
					
					//if(txtRCTTP.getText().equals("30"))   //F-1 on Issue Lot No. for Non-Production JTable
					//if(txtRCTTP.getText().equals(strSLSRT))
					if(flgRTNFL)
					{
						M_strSQLQRY = "Select IST_LOTNO,IST_RCLNO from FG_ISTRN,MR_IVTRN where IVT_INVNO='"+txtISSRF.getText()+"'";
						M_strSQLQRY += " and IST_ISSNO=IVT_LADNO and IST_CMPCD=IVT_CMPCD and IST_MKTTP=IVT_MKTTP  AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP='"+txtPRDTP.getText()+"'";
						if(txtLOTNO2.getText().trim().length()>0)
						{
							M_strSQLQRY += " AND IST_LOTNO like '"+txtLOTNO2.getText().trim() + "%'";
						}
						M_strSQLQRY +=	"order by IST_LOTNO,IST_RCLNO desc";
						//System.out.println("001");
					}
					else
					{
						if(flgLTRCT)
						{
							M_strSQLQRY  = "Select LT_LOTNO,max(LT_RCLNO) LT_RCLNO from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_STSFL = '0' and";
							M_strSQLQRY += " LT_PRDTP='"+txtPRDTP.getText()+"' and LT_LOTNO like '08%' ";
							
						}
						else
						{
							M_strSQLQRY = "Select LT_LOTNO,max(LT_RCLNO) LT_RCLNO from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_STSFL = '0' and";
							M_strSQLQRY += " LT_PRDTP='"+txtPRDTP.getText()+"'";
						}
						if(txtLOTNO2.getText().trim().length()>0)
						{
							M_strSQLQRY += " AND LT_LOTNO like '"+txtLOTNO2.getText().trim() + "%'";
						}
						M_strSQLQRY +="  group by LT_LOTNO order by LT_LOTNO,LT_RCLNO";
					}
					System.out.println("LOTNO2 ="+M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Lot No.","ReClassification No."},2,"CT");
				}
				if(M_objSOURC == txtMNLCD2)// Create F-1 on Main Location of Non- Production JTable
				{
					M_strHLPFLD = "txtMNLCD2";
					if(flgRTNFL)
					{
						M_strSQLQRY= "Select LC_MNLCD,LC_MAXQT from FG_LCMST ORDER BY LC_MNLCD";
						cl_hlp(M_strSQLQRY ,1,1,new String[] {"Main Loc.", "Maximum Quantity."},2,"CT");
					}
					else
					{
						M_strSQLQRY="Select LC_MNLCD from FG_LCMST where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LC_STSFL not in ('X','2')";
						cl_hlp(M_strSQLQRY ,1,1,new String[] {"Main Loc."},1,"CT");
					}
					
				}
				if(M_objSOURC == txtPKGTP2)// Create	F-1 on Main Location of Production JTable
				{
					M_strHLPFLD = "txtPKGTP2";
					M_strSQLQRY="Select CMT_CODCD,CMT_NCSVL from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'FGXXPKG'";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Package Type.","Package Weight"},2,"CT");
				}
				if(M_objSOURC ==txtSHFCD)
				{
					M_strHLPFLD ="txtSHFCD";
					cl_hlp(getHLPVTR("SHFCD",((JTextField)tblENTTB1.cmpEDITR[TB1_SHFCD]).getText()) ,2,1,new String[] {"Shift","Start Time"},2,"CT");
				}
				if(M_objSOURC ==txtSHFCD2)
				{
					M_strHLPFLD ="txtSHFCD2";
					cl_hlp(getHLPVTR("SHFCD",((JTextField)tblENTTB2.cmpEDITR[TB2_SHFCD]).getText()) ,2,1,new String[] {"Shift","Start Time"},2,"CT");
				}
				if(M_objSOURC == txtRETLOT)   //Create F1 for JOptionPane Lot No
				{
					M_strHLPFLD = "txtRETLOT";
					M_strSQLQRY = "Select LT_LOTNO,SUBSTRING(PR_PRDDS,1,15) PR_PRDDS,max(LT_RCLNO) LT_RCLNO from pr_ltmst,co_prmst where ltrim(str(LT_PRDCD,20,0))=PR_PRDCD and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (LT_RCTRF is null or LT_RCTRF=' ')";
					if(txtLOTNO1.getText().trim().length()>0)
					{
						M_strSQLQRY += " AND LT_LOTNO like '"+txtRETLOT.getText().trim() + "%'";
					}
					M_strSQLQRY += " and LT_PRDTP='"+txtPRDTP.getText()+"' and LT_STSFL = '0'";
					M_strSQLQRY += " group by LT_LOTNO,SUBSTRING(PR_PRDDS,1,15) order by LT_LOTNO,PR_PRDDS,LT_RCLNO desc";
					System.out.println(M_strSQLQRY );
					
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"LOT NO.","Grade","RCl No."},3,"CT");//				}
				}
			}
		}
		catch(Exception e)	
		{
			setMSG(e,"Child.KeyPressed");
		}
		finally
		{
			 this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/** Function for getting value from F1 after clicking OK 
	 * 
	*/
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtRCTTP"))
			{
				txtRCTTP.setText(L_STRTKN.nextToken());
				txtRCTTP.requestFocus();
			}
			if(M_strHLPFLD.equals("txtPRDTP"))
			{
				txtPRDTP.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtRCTNO"))
			{
				txtRCTNO.setText(L_STRTKN.nextToken());
				txtRCTNO.requestFocus();
			}
			if(M_strHLPFLD.equals("txtGINNO"))
			{
				txtGINNO.setText(L_STRTKN.nextToken());
				txtGINNO.requestFocus();
			}
			if(M_strHLPFLD.equals("txtISSRF"))
			{
				txtISSRF.setText(L_STRTKN.nextToken());
				txtISSRF.requestFocus();
			}
			if(M_strHLPFLD.equals("txtLOTNO1"))
			{
				txtLOTNO1.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtPKGTP1"))
			{
				txtPKGTP1.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtMCHNO1"))
			{
				txtMCHNO1.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtMNLCD1"))
			{
				txtMNLCD1.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtLOTNO2"))
			{
				txtLOTNO2.setText(L_STRTKN.nextToken());
				tblENTTB2.setRowSelectionInterval(tblENTTB2.getSelectedRow(),tblENTTB2.getSelectedRow());		
				tblENTTB2.setColumnSelectionInterval(TB2_LOTNO,TB2_LOTNO);		
				tblENTTB2.editCellAt(tblENTTB2.getSelectedRow(),TB2_LOTNO);
				tblENTTB2.cmpEDITR[TB2_LOTNO].requestFocus();
			}
			if(M_strHLPFLD.equals("txtMNLCD2"))
			{
				txtMNLCD2.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtPKGTP2"))
			{
				txtPKGTP2.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtSHFCD"))
			{
				txtSHFCD.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtRETLOT"))
			{
				txtRETLOT.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtSHFCD2"))
			{
				txtSHFCD2.setText(L_STRTKN.nextToken());
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
		}
	}
/** Data validation before saving the record
 */
	boolean vldDATA()
	{
		try
		{
			String strTEMP="";
			//System.out.println("strTEMP");
			if(tblENTTB1.isEditing())
				tblENTTB1.getCellEditor().stopCellEditing();
			if(tblENTTB2.isEditing())
				tblENTTB2.getCellEditor().stopCellEditing();
			
			if(txtRCTTP.getText().trim().length()==0)
			{
				setMSG("Receipt Type can not be blank..",'E');
				txtRCTTP.requestFocus();
				return false;
			}
			if(txtRCTDT.getText().trim().length()==0)
			{
				setMSG("Receipt Date can not be blank..",'E');
				txtRCTDT.requestFocus();
				return false;
			}
			if(txtPRDTP.getText().trim().length()==0)
			{
				setMSG("Product Type can not be blank..",'E');
				txtPRDTP.requestFocus();
				return false;
			}
			
			for(int i=0;i<tblENTTB1.getRowCount();i++)
    		{
				if(tblENTTB1.getValueAt(i,TB1_CHKFL).toString().equals("true"))
    			{
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_LOTNO).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Lot Number can not be blank..",'E');
						return false;
    				}
					
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_RCLNO).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("ReClassification  Number can not be blank..",'E');
						return false;
    				}
					
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_STRTM).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Start Time can not be blank..",'E');
						return false;
    				}
					
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_ENDCT).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("End Count can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_PKGTP).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Package Type can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_SHFCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Shift can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_MCHNO).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Machine Number can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB1.getValueAt(i,TB1_MNLCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Main Location can not be blank..",'E');
						return false;
    				}
				}
			}
			for(int i=0;i<tblENTTB2.getRowCount();i++)
    		{
				if(tblENTTB2.getValueAt(i,TB2_CHKFL).toString().equals("true"))
    			{
					strTEMP = nvlSTRVL(tblENTTB2.getValueAt(i,TB2_LOTNO).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Lot Number can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB2.getValueAt(i,TB2_RCLNO).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("ReClassification Number can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB2.getValueAt(i,TB2_MNLCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Main Location can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB2.getValueAt(i,TB2_PKGTP).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Package Type can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB2.getValueAt(i,TB2_RCTQT).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Receipt Quantity can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB2.getValueAt(i,TB2_RCTPK).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Receipt Package can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblENTTB2.getValueAt(i,TB2_SHFCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Shift can not be blank..",'E');
						return false;
    				}
				}
			}

		}
		catch (Exception e)
		{
			setMSG(e,"vldDATA");
			return false;
		}
		return true;
	}

	/**
	 *   Validating & Saving Data into Lot working master table
	*/
	void exeSAVE()
	{
		try
		{
			//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			//{
			//	chkISSUE(txtRCTNO.getText().trim());  //to check whether any pending issue is their
			//}	
			
			if(!vldDATA())
				return;
				
			cl_dat.M_flgLCUPD_pbst = true;
			//System.out.println("exeSAVE");
//			if(txtRCTTP.getText().equals("10") || txtRCTTP.getText().equals("15"))
			//if(txtRCTTP.getText().equals(strFRSPD) || txtRCTTP.getText().equals(strREPAC))
			if(flgLWMST)
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					exeSAVE_LWMST();//Method for insert and update the data into the FG_LWMST table
				else
					exeAUTH_LWMST();
			}
			else
			if(!flgLWMST)
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					exeSAVE_RCTRN();//Method for insert and update the data into the FG_RCTRN table
					//if(txtRCTTP.getText().equals("21") && txtPRDTP.getText().equals("02"))
					if(flgJOBFL && txtPRDTP.getText().equals("02"))
						exeSAVE_LTMST();//Method for insert the data into the PR_LTMST table
				}
				else
					exeAUTH_RCTRN();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				String	L_strRCTDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRCTDT.getText().trim()));
				//System.out.println("L_strRCTDT"+L_strRCTDT);
				//calDATFL.setString(1,"RC");		
				//calDATFL.setString(2,L_strRCTDT);
				//calDATFL.executeUpdate();
				//cl_dat.M_conSPDBA_pbst.commit();
				//System.out.println("After  StoreProcedure calDATFL");
			}
			//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			//{
			//	exeISSUE_NOTE_GEN();
			//}							
			String L_strRCTNO=txtRCTNO.getText().trim();
			clrCOMP();	
			txtRCTNO.setText(L_strRCTNO);
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
			cl_dat.M_flgLCUPD_pbst=false;
		}
	}
	
	private void  exeISSUE_VW()
	{
		
		try
		{
				
		 String M_strSQLQRY = "";
		  ResultSet M_rstRSSET;
		  setMSG("Generating....",'N');
		  
			cl_JTable tblMATCD;
			JPanel pnlISSVW;
			pnlISSVW = new JPanel();
		    int L_ROWNO=0;
		    
		    double L_dblTOTQT = 0;
		    String strTOTQT = "0";
		    String strTOAMT;
		    
		    double L_dblTOTQT1 = 0;
		    String strTOTQT1 = "0";
		    String strTOAMT1;
		   
			String strOLD_MATCD="";  
			String strNEW_MATCD="";
			String strOLD_MATCD1="";  
			String strNEW_MATCD1="";
		    
			int TB_CHKFL =0;
			int TB_MATCD =1;
			int TB_TAGNO =2;
			int TB_MATDS =3;
			int TB_RCTDT =4;
			int TB_LOTNO =5;
			int TB_PRDDS =6;
			int TB_PKGTP =7;
			int TB_BAGQT =8;
			int TB_BAGPK =9;
			int TB_TOTBG =10;

		  pnlISSVW.setLayout(null);	
		  String[] LM_TBLHD = {"","Mat.Code","Tag No","Description","Bagg.Date","Lot No.","Grade","Pkg.Type","Qty.","Bags","Total Bags"};
		  int[] LM_COLSZ = {5,70,70,120,50,70,60,50,50,60,60};
	      tblMATCD = crtTBLPNL1(pnlISSVW,LM_TBLHD,100,1,1,8,7,LM_COLSZ,new int[]{0});
	     
	      M_strSQLQRY = "select lw_matcd,cmt_codds,lw_mchno,sum(lw_bagqt) lw_bagqt,sum(lw_bagpk) lw_bagpk,lw_rctdt,lw_lotno,pr_prdds,lw_pkgtp from fg_lwmst,pr_ltmst,spldata.co_prmst,co_cdtrn  where lw_issrf='"+txtISSNO.getText()+"'and lw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'and lw_prdtp=lt_prdtp and lw_lotno=lt_lotno ";  
		  M_strSQLQRY +="and lw_rclno = lt_rclno and ltrim(str(lt_prdcd,20,0)) = pr_prdcd and cmt_cgmtp='S01' and cmt_cgstp = 'FGXXPKD' and cmt_chp01 = lw_matcd  group by lw_matcd,cmt_codds,lw_mchno,lw_lotno,pr_prdds,lw_pkgtp,lw_rctdt order by lw_matcd,lw_mchno";
		  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		  System.out.println(">>>>MATCD>>>>"+M_strSQLQRY);
	
		  
		 		if(M_rstRSSET != null)   
		        { 
		        				 
		    		while(M_rstRSSET.next())
					{ 
		    		  strNEW_MATCD=M_fmtLCDAT.format(M_rstRSSET.getDate("lw_matcd"));
		    	      if(!strOLD_MATCD.equals(strNEW_MATCD) && !strOLD_MATCD.equals(""))
					  { 
		    	    	L_ROWNO++;   
						tblMATCD.setValueAt(setNumberFormat(L_dblTOTQT,0),L_ROWNO,TB_TOTBG);
						L_dblTOTQT=0;
					  }
		    	      strOLD_MATCD=M_fmtLCDAT.format(M_rstRSSET.getDate("lw_matcd")); 
		    	      L_ROWNO++;
		    	        tblMATCD.setValueAt(M_rstRSSET.getString("lw_matcd"),L_ROWNO,TB_MATCD);
						tblMATCD.setValueAt(M_rstRSSET.getString("lw_mchno"),L_ROWNO,TB_TAGNO);
		    			tblMATCD.setValueAt(M_rstRSSET.getString("cmt_codds"),L_ROWNO,TB_MATDS);
		    			tblMATCD.setValueAt(M_rstRSSET.getString("lw_rctdt"),L_ROWNO,TB_RCTDT);
		    			tblMATCD.setValueAt(M_rstRSSET.getString("lw_lotno"),L_ROWNO,TB_LOTNO);
		    			tblMATCD.setValueAt(M_rstRSSET.getString("pr_prdds"),L_ROWNO,TB_PRDDS);
		    			tblMATCD.setValueAt(M_rstRSSET.getString("lw_pkgtp"),L_ROWNO,TB_PKGTP);
		    			tblMATCD.setValueAt(M_rstRSSET.getString("lw_bagqt"),L_ROWNO,TB_BAGQT);
		    			tblMATCD.setValueAt(M_rstRSSET.getString("lw_bagpk"),L_ROWNO,TB_BAGPK);
		    			strTOAMT = setNumberFormat(M_rstRSSET.getDouble("lw_bagpk"),2).toString();
		    			L_dblTOTQT += Double.parseDouble(strTOAMT);
		    	        
		    	     }
		    		 L_ROWNO++;	
		    		 tblMATCD.setValueAt(setNumberFormat(L_dblTOTQT,0),L_ROWNO,TB_TOTBG);
		    		M_rstRSSET.close();
		        }
		 	setCursor(cl_dat.M_curWTSTS_pbst);    		
			cl_JTable tblISSVW;
		    int L_ROWNO1 =0;
		    
			int TB1_CHKFL =0;
			int TB1_ISSNO =1;
			int TB1_MATCD =2;
			int TB1_TAGNO =3;
			int TB1_MATDS =4;
			int TB1_ISSQT =5;
			int TB1_TOTBG =6;	    
		  
		  String[] LM_TBLHD1 = {"","Issue No.","Mat.Code","Tag No.","Description","Bags","Total Bags"};
		  int[] LM_COLSZ1 = {5,70,70,70,120,70,70};
	      tblISSVW = crtTBLPNL1( pnlISSVW,LM_TBLHD1,80,10,1,8,5,LM_COLSZ1,new int[]{0});			
	      M_strSQLQRY = "select is_issno,is_tagno,is_matcd,cmt_codds,is_issqt from mm_ismst,co_cdtrn  where is_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and  cmt_cgmtp='S01' and cmt_cgstp = 'FGXXPKD' and cmt_chp01 = is_matcd and is_issno='"+txtISSNO.getText()+"'";
	      M_strSQLQRY += "order by is_matcd,is_tagno";
		  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		  System.out.println(">>>>Issue View>>>>"+M_strSQLQRY);
		
		  	
		 		if(M_rstRSSET != null)   
		        {  
		        				 
		    		while(M_rstRSSET.next())
					{
		    			 
			    		  strNEW_MATCD1=M_fmtLCDAT.format(M_rstRSSET.getDate("is_matcd"));
			    	      if(!strOLD_MATCD1.equals(strNEW_MATCD1) && !strOLD_MATCD1.equals(""))
						  { 
			    	    	L_ROWNO1++;   
			    	    	tblISSVW.setValueAt(setNumberFormat(L_dblTOTQT1,0),L_ROWNO1,TB1_TOTBG);
							L_dblTOTQT1=0;
						  }
			    	        strOLD_MATCD1=M_fmtLCDAT.format(M_rstRSSET.getDate("is_matcd")); 
			    	        L_ROWNO1++;	
			    	        
			    	        tblISSVW.setValueAt(M_rstRSSET.getString("is_issno"),L_ROWNO1,TB1_ISSNO);
			    			tblISSVW.setValueAt(M_rstRSSET.getString("is_matcd"),L_ROWNO1,TB1_MATCD);
			    			tblISSVW.setValueAt(M_rstRSSET.getString("is_tagno"),L_ROWNO1,TB1_TAGNO);
			    			tblISSVW.setValueAt(M_rstRSSET.getString("cmt_codds"),L_ROWNO1,TB1_MATDS);
			    			tblISSVW.setValueAt(M_rstRSSET.getString("is_issqt"),L_ROWNO1,TB1_ISSQT);
			    			strTOAMT1 = setNumberFormat(M_rstRSSET.getDouble("is_issqt"),2).toString();
			    			L_dblTOTQT1 += Double.parseDouble(strTOAMT1); 
					}
		    		L_ROWNO1++;	
		    		tblISSVW.setValueAt(setNumberFormat(L_dblTOTQT,0),L_ROWNO1,TB1_TOTBG);
		    		M_rstRSSET.close();
		        }
		 		setCursor(cl_dat.M_curWTSTS_pbst); 
		       		 		
		         pnlISSVW.setSize(650,650);
		         pnlISSVW.setPreferredSize(new Dimension(650,650));
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlISSVW," View Issue",JOptionPane.OK_CANCEL_OPTION);
				
		}
		
		catch(Exception E)
		{
			setMSG("error..",'E');
			System.out.println("ERROR in  exeISSUE_VEW() :"+E);
		}	
							  
	}

	
	private void exeISSUE_NOTE_GEN(String LP_PRDTP,String LP_DPTCD)
	{
		try{
			setMSG("Generating....",'N');
			M_strSQLQRY = " select max(LW_RCTDT) LW_ISSDT  from fg_lwmst where  LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lw_rcttp in ('10','15') and isnull(LW_ISSRF,'') = '' and lw_stsfl = '2' and lw_rctdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT_AI))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strPRVDT1_AI))+"' and lw_prdtp in ("+LP_PRDTP+")";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET == null || !M_rstRSSET.next())
				{exeAUTIS_POST();cl_dat.exeDBCMT("exeAUTIS_POST");return;}
			
			strISSDT_AI = M_fmtLCDAT.format(M_rstRSSET.getDate("LW_ISSDT"));
			/** This part of validation is introduced to prevent user from generating Auto-issue in next month , before PSL processing of completed month.
			 */
			if(!strISSDT_AI.substring(3,5).equals(cl_dat.M_strLOGDT_pbst.substring(3,5)))
				{setMSG("Current Month is "+cl_dat.M_strLOGDT_pbst.substring(3,5)+" mismatch with  Issue Month (data) = "+strISSDT_AI.substring(3,5),'E'); exeAUTIS_POST();return;}
			if(!strENDDT_AI.substring(3,5).equals(cl_dat.M_strLOGDT_pbst.substring(3,5)))
				{setMSG("Current Month is "+cl_dat.M_strLOGDT_pbst.substring(3,5)+" mismatch with  Issue Month (range) = "+strENDDT_AI.substring(3,5),'E'); exeAUTIS_POST();return;}
			System.out.println("Current Month is "+cl_dat.M_strLOGDT_pbst.substring(3,5)+"  Issue Month = "+strISSDT_AI.substring(3,5));
				
			int cntRWCNT =0;
			int TB_CHKFL =0;
			int TB_MATCD =1;
			int TB_MATDS =2;
			int TB_ISSQT =3;
			int TB_STKQT =4;
			int TB_WAVRT =5;
			int TB_BATNO =6;
			int TB_OWNCD =7;
			int TB_CCTCD =8;
			int TB_USGCD =9;
	
			JTextField txtREMDS_ISS;
			JLabel lblREMDS_ISS;
			JCheckBox chkCHKFL_ISS;
			JTextField txtBAGPK_ISS,txtMATCD_ISS,txtMATDS_ISS,txtBATNO_ISS,txtOWNCD_ISS,txtISSQT_ISS,txtCCTCD_ISS,txtUSGCD_ISS;
			JLabel lblLABEL;
			cl_JTable tblISSNT;
			JPanel pnlISSNT;
			lblLABEL = new JLabel("Auto Issue Will Be Generated With Following Details,Please Confirm \n\n\n\n");
			lblLABEL.setForeground(Color.BLUE);    
			pnlISSNT = new JPanel();
			
			pnlISSNT.setLayout(new BorderLayout());
			pnlISSNT.add(lblLABEL,BorderLayout.NORTH);
			txtREMDS_ISS = new JTextField();
			lblREMDS_ISS = new JLabel("Enter Remark");
			pnlISSNT.add(lblREMDS_ISS,BorderLayout.SOUTH);
			pnlISSNT.add(txtREMDS_ISS,BorderLayout.SOUTH);
			txtREMDS_ISS.setText("Auto generated Packing Material Issue note as on "+strPRVDT1_AI);
			//add(lblLABEL,1,1,1,6,pnlISSNT,'L');
			//pnlISSNT.setLayout(new GridLayout(1,0));
			//pnlISSNT.setSize(700,500);
			tblISSNT = crtTBLPNL1(pnlISSNT,new String[]{"FL","Mat. Code","Description","IssQt","StkQt","WAR","Batch/Lot","Owner","Cost Cntr","Usage"},50,1,1,7.5,15,new int[]{20,80,100,40,70,40,40,40,80,80},new int[]{0});
			tblISSNT.setCellEditor(TB_CHKFL,chkCHKFL_ISS = new JCheckBox());
			tblISSNT.setCellEditor(TB_MATCD,txtMATCD_ISS = new TxtLimit(10));
			tblISSNT.setCellEditor(TB_MATDS,txtMATDS_ISS = new TxtLimit(60));
			tblISSNT.setCellEditor(TB_BATNO,txtBATNO_ISS = new TxtLimit(20));
			tblISSNT.setCellEditor(TB_OWNCD,txtOWNCD_ISS = new TxtLimit(3));
			tblISSNT.setCellEditor(TB_ISSQT,txtISSQT_ISS = new TxtNumLimit(10.3));
			tblISSNT.setCellEditor(TB_STKQT,txtISSQT_ISS = new TxtNumLimit(12.3));
			tblISSNT.setCellEditor(TB_WAVRT,txtISSQT_ISS = new TxtNumLimit(10.2));
			tblISSNT.setCellEditor(TB_CCTCD,txtCCTCD_ISS = new TxtLimit(15));
            tblISSNT.setCellEditor(TB_USGCD,txtUSGCD_ISS = new TxtLimit(2));
			
			setMSG("Calling Proc",'N');
			cstLWMST = cl_dat.M_conSPDBA_pbst.prepareCall("{call setLWMST_PKGGR(?,?,?)}");
			cstLWMST.setString(1,cl_dat.M_strCMPCD_pbst);
			cstLWMST.setString(2,M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT_AI)));
			cstLWMST.setString(3,M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT_AI)));
			cstLWMST.executeUpdate();
			setMSG("Proc called",'N');
			
			M_strSQLQRY = " select lw_matcd,SUBSTRING(lw_pkggr,8,7) lw_tagno,st_matds,sum(lw_bagpk) bagpk, isnull(st_stkqt,0) st_stkqt,isnull(st_wavrt,0) st_wavrt from fg_lwmst,mm_stmst";
			M_strSQLQRY +="	where st_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and st_strtp='01' and lw_matcd=st_matcd and LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(LW_ISSRF,'') = '' and len(rtrim(ltrim(isnull(lw_matcd,''))))=10 and lw_stsfl = '2' and lw_rctdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT_AI))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strPRVDT1_AI))+"' and lw_prdtp in ("+LP_PRDTP+")";
			M_strSQLQRY +=" group by lw_matcd,SUBSTRING(lw_pkggr,8,7),st_matds,st_stkqt,st_wavrt";

			System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{	
					tblISSNT.setValueAt(new Boolean(true),cntRWCNT,TB_CHKFL);
					tblISSNT.setValueAt(M_rstRSSET.getString("lw_matcd"),cntRWCNT,TB_MATCD);
					tblISSNT.setValueAt(M_rstRSSET.getString("st_matds"),cntRWCNT,TB_MATDS);
					tblISSNT.setValueAt("99MIPN999",cntRWCNT,TB_BATNO);
					tblISSNT.setValueAt("MHD",cntRWCNT,TB_OWNCD);
					tblISSNT.setValueAt(M_rstRSSET.getString("bagpk"),cntRWCNT,TB_ISSQT);
					tblISSNT.setValueAt(M_rstRSSET.getString("st_stkqt"),cntRWCNT,TB_STKQT);
					tblISSNT.setValueAt(M_rstRSSET.getString("st_wavrt"),cntRWCNT,TB_WAVRT);
					tblISSNT.setValueAt(M_rstRSSET.getString("lw_tagno"),cntRWCNT,TB_CCTCD);
					tblISSNT.setValueAt("03",cntRWCNT,TB_USGCD);
					cntRWCNT++;	
				}	
			}
			//JOptionPane.showMessageDialog(this,pnlISSNT,"Issue Note",JOptionPane.OK_CANCEL_OPTION);
			
			int intCONFIRM=JOptionPane.showConfirmDialog(this,pnlISSNT,"Issue Note",JOptionPane.OK_CANCEL_OPTION);
			if(intCONFIRM==2)
			{	
				return;
			}
			boolean flgSELRW=false;////flag to check whether atleast 1 row from tblTEEXR1 is selected
			for(int P_intROWNO=0;P_intROWNO<tblISSNT.getRowCount();P_intROWNO++)
			{
				if(tblISSNT.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
				{
					flgSELRW=true;
				}
			}
			if(flgSELRW==false)
			{	
				JOptionPane.showConfirmDialog( this,"No Data Is Selected For Auto Issue Generation",null,JOptionPane.OK_CANCEL_OPTION);
			}	
			else
			{	
				String L_strMATCD="",L_strMATDS="",L_strBATNO="",L_strOWNCD="",L_strISSQT="",L_strSTKQT="",L_strWAVRT="",L_strCCTCD="",L_strUSGCD="",L_strREMDS="";
				boolean L_flgFIRST = true;
			
				String L_strPOWNER ="",L_strISSLS ="";
				String L_strISSNO ="";
				L_strISSNO = genISSNO("3"); // For Material Type 1, stores and spares
				System.out.println("L_strISSNO>>>>"+L_strISSNO);
				L_strREMDS = txtREMDS_ISS.getText();
			
				M_strSQLQRY = "Insert into MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,";
        		M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,";
        		M_strSQLQRY += "RM_LUPDT) values (";
				M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+ "',";
        		M_strSQLQRY += "'01',";
        		M_strSQLQRY += "'IS',";
        		M_strSQLQRY += "'3',";
        		M_strSQLQRY += "'"+L_strISSNO+"',";
        		M_strSQLQRY += "'"+L_strREMDS+"'," ;
        		M_strSQLQRY += "'0'," ;
				M_strSQLQRY += "'2'," ;
				M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
				System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			
				for(int i=0;i<tblISSNT.getRowCount();i++)
				{
				    if(tblISSNT.getValueAt(i,TB_CHKFL).toString().equals("true"))
				    {
						L_strMATCD = tblISSNT.getValueAt(i,TB_MATCD).toString();
						L_strMATDS = tblISSNT.getValueAt(i,TB_MATDS).toString();
						L_strBATNO = tblISSNT.getValueAt(i,TB_BATNO).toString();
						L_strOWNCD = tblISSNT.getValueAt(i,TB_OWNCD).toString();
						L_strISSQT = tblISSNT.getValueAt(i,TB_ISSQT).toString();
						L_strSTKQT = tblISSNT.getValueAt(i,TB_STKQT).toString();
						L_strWAVRT = tblISSNT.getValueAt(i,TB_WAVRT).toString();
						L_strCCTCD = tblISSNT.getValueAt(i,TB_CCTCD).toString();
						L_strUSGCD = tblISSNT.getValueAt(i,TB_USGCD).toString();
							
						M_strSQLQRY = "Insert into MM_ISMST(IS_CMPCD,IS_STRTP,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_MATTP,IS_TAGNO,";
        				M_strSQLQRY +="IS_ISSDT,IS_DPTCD,IS_PREBY,IS_AUTBY,IS_REQQT,IS_ISSQT,IS_ISSRT,IS_USGTP,IS_CCTCD,IS_TRNFL,";
						M_strSQLQRY +="IS_STSFL,IS_LUSBY,IS_LUPDT,IS_BATNO,IS_GRNNO,IS_CONQT,IS_MMSBS,IS_PREDT,IS_AUTDT,IS_ISSVL,IS_MDVQT";
        				M_strSQLQRY +=") values (";
        				M_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY +="'01',";
						M_strSQLQRY +="'01',";
						M_strSQLQRY +="'"+L_strISSNO+"',";
						M_strSQLQRY +="'"+L_strMATCD+"',";
						M_strSQLQRY +="'3',";
        				M_strSQLQRY +="'"+L_strCCTCD+"',";
						//M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strISSDT_AI))+"',";
						M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY +="'"+LP_DPTCD+"',";
						M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY +="'SYS',";
						M_strSQLQRY +=setNumberFormat(Double.parseDouble(L_strISSQT),3)+",";
						//M_strSQLQRY +=setNumberFormat(Double.parseDouble(L_strISSQT),3)+",";
						M_strSQLQRY +="0.000,";
						M_strSQLQRY +=setNumberFormat(Double.parseDouble(L_strWAVRT),2)+",";
						M_strSQLQRY +="'04',";
						M_strSQLQRY +="'"+L_strCCTCD+"',";
						M_strSQLQRY +="'0',";
						M_strSQLQRY +="'1',";
						M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
        				M_strSQLQRY +="'"+L_strISSNO+"',";
						M_strSQLQRY +="'"+L_strISSNO+"',";
						M_strSQLQRY +="0,";
						M_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"0100',";
						M_strSQLQRY +="'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
						M_strSQLQRY +="'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
						M_strSQLQRY +=setNumberFormat(Double.parseDouble(L_strWAVRT)*Double.parseDouble(L_strISSQT),2)+",";
						M_strSQLQRY +="0)";
						System.out.println(M_strSQLQRY);
        				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}

				M_strSQLQRY = "Update CO_CDTRN set ";
				M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + L_strISSNO.substring(3) + "',";
    			M_strSQLQRY += getUSGDTL("CMT",'U',"");
    			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
    			M_strSQLQRY += " and CMT_CGSTP = 'MM01ISS'";	
    			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + "4" + "3" + "'";			
				System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			
				M_strSQLQRY = "Update FG_LWMST set ";
				M_strSQLQRY += " LW_ISSRF ='"+L_strISSNO+"'";
    			M_strSQLQRY += " where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lw_rcttp in ('10','15') and isnull(LW_ISSRF,'') = '' and len(rtrim(ltrim(isnull(lw_matcd,''))))=10 and lw_stsfl = '2' and lw_rctdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT_AI))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strPRVDT1_AI))+"' and lw_prdtp in ("+LP_PRDTP+")";
				System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			    exeAUTIS_POST();
				if(cl_dat.exeDBCMT("genISSNT"))
				{
				    setMSG("Generated Auto Issue nos. "+L_strISSLS,'N');
					JOptionPane.showMessageDialog( this,"Auto Issue Note No."+L_strISSNO+" Generated for "+strPRVDT1_AI,null,JOptionPane.OK_CANCEL_OPTION);
				}
			}
		}
		catch(Exception E)
		{
			setMSG("error..",'E');
			System.out.println("ERROR in exeISSUE_NOTE_GEN()  :"+E);
		}	
	}	
	
	private void exeAUTIS_POST()
	{
		try{
				M_strSQLQRY = "Update CO_CDTRN set ";
				M_strSQLQRY += " CMT_CCSVL ='"+strPRVDT1_AI+"',";
    			M_strSQLQRY += getUSGDTL("CMT",'U',"");
    			M_strSQLQRY += " where CMT_CGMTP = 'S"+cl_dat.M_strCMPCD_pbst+"'";
    			M_strSQLQRY += " and CMT_CGSTP = 'FGXXREF'";	
    			M_strSQLQRY += " and CMT_CODCD = 'AUTIS'";			
				System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
				System.out.println("Date posted : "+strPRVDT1_AI);
				setMSG("Date posted : "+strPRVDT1_AI,'N');
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception E)
		{
			System.out.println("ERROR in exeAUTIS_POST()  :"+E);
		}	
	}	

	
	/*
	private void chkISSUE(String LP_RCTNO)
	{
		try{
			int cntRWCNT =0;
			int TB_CHKFL =0;
			int TB_RCTNO =1;
			int TB_BAGPK =2;
	
			JCheckBox chkCHKFL_ISS;
			JTextField txtRCTNO_ISS,txtBAGPK_ISS;
			JLabel lblLABEL;
			cl_JTable tblISSNT;
			JPanel pnlISSNT;
			lblLABEL = new JLabel("Pending Issue Notes\n\n\n\n");
			lblLABEL.setForeground(Color.BLUE);   
			pnlISSNT = new JPanel();
			
			pnlISSNT.setLayout(new BorderLayout());
			pnlISSNT.add(lblLABEL,BorderLayout.NORTH);

			tblISSNT = crtTBLPNL1(pnlISSNT,new String[]{"FL","Receipt No","IssQt"},50,1,1,7.5,15,new int[]{20,80,100},new int[]{0});
			tblISSNT.setCellEditor(TB_CHKFL,chkCHKFL_ISS = new JCheckBox());
			tblISSNT.setCellEditor(TB_RCTNO,txtRCTNO_ISS = new TxtLimit(10));
			tblISSNT.setCellEditor(TB_BAGPK,txtBAGPK_ISS = new TxtLimit(10));
			
			M_strSQLQRY = " select lw_rctno,sum(lw_bagpk) bagpk from fg_lwmst";
			M_strSQLQRY +=" where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lw_rcttp in('10','15') and and lw_rctdt > '"+strCOFFDT+"' and isnull(LW_ISSRF,'') = ''";
			M_strSQLQRY +=" group by lw_rctno";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{	
					tblISSNT.setValueAt(new Boolean(true),cntRWCNT,TB_CHKFL);
					tblISSNT.setValueAt(M_rstRSSET.getString("lw_rctno"),cntRWCNT,TB_RCTNO);
					tblISSNT.setValueAt(M_rstRSSET.getString("bagpk"),cntRWCNT,TB_BAGPK);
					cntRWCNT++;	
				}	
			}
			if(cntRWCNT>0)
				JOptionPane.showMessageDialog( this,pnlISSNT,"Pending Issue Notes",JOptionPane.OK_CANCEL_OPTION);
		}
		catch(Exception E)
		{
			System.out.println("ERROR in chkISSUE()  :"+E);
		}	
	}
*/
	private String genISSNO(String P_strMATTP)
	{
		String L_ISSNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for issues;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM01ISS' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + "4" + P_strMATTP + "'";
			System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
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
			M_strSQLQRY += " and CMT_CGSTP = 'MM01ISS'";	
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
	
	/**
	 *  Function for insert and update the data into the FG_LWMST table(Production receipt (bagging ))
	*/
	private void exeSAVE_LWMST()
	{
		try
		{
			String L_strSQLQRY = "";
			flgRCTNO_NEW = true;
			int L_intSELROW = 0;
			for(int i=0;i<tblENTTB1.getRowCount();i++)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
					break;
				if(tblENTTB1.getValueAt(i,TB1_LOTNO).toString().length()<8)
					break;
				if(tblENTTB1.getValueAt(i,TB1_CHKFL).toString().equals("false"))
				{
					continue;
				}
				L_intSELROW++;
				strWHRSTR =  " LW_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and LW_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and "
					        +" LW_RCTTP = '"+txtRCTTP.getText()+"' and "
					        +" LW_RCTNO = '"+txtRCTNO.getText()+"' and "
					        +" LW_PRDTP = '"+txtPRDTP.getText()+"' and "
					        +" LW_LOTNO = '"+tblENTTB1.getValueAt(i,TB1_LOTNO).toString()+"' and "
					        +" LW_RCLNO = '"+tblENTTB1.getValueAt(i,TB1_RCLNO).toString()+"' and "
					        +" LW_STRTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtRCTDT.getText()+" "+tblENTTB1.getValueAt(i,TB1_STRTM).toString()))+"'";
										
				flgCHK_EXIST =  chkEXIST("FG_LWMST", strWHRSTR);//this first check the strWHRSTR content are allready exist in FG_LWMST table.
				//System.out.println("flgCHK_EXIST =" +flgCHK_EXIST);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
				{
					if(!flgMODRCPT)
						JOptionPane.showMessageDialog(this,"Record already exists in FG_LWMST");
						continue;
				}
				if(!flgCHK_EXIST && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst)))// if not exist insert command is fire.
				{
					if(!flgMODRCPT)
						if(!getRCTNO())
							continue;
					//getRCTNO();
					
					inlTBLEDIT(tblENTTB1);
					
					L_strSQLQRY = "insert into FG_LWMST(LW_CMPCD,LW_WRHTP,LW_RCTTP,LW_RCTNO,LW_RCTDT,LW_PRDTP,LW_LOTNO,LW_RCLNO,LW_STRTM,LW_ENDTM,LW_REMDS,LW_STRCT,LW_ENDCT,LW_PKGTP,LW_BAGQT,LW_BAGPK,LW_MISCT,LW_MCHNO,LW_SHFCD,LW_MNLCD,LW_UCLTG,LW_STSFL,LW_TRNFL,LW_LUSBY,LW_LUPDT) values (";
					L_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
					L_strSQLQRY += "'"+strWRHTP+"',";
					L_strSQLQRY += "'"+txtRCTTP.getText()+"',";
					L_strSQLQRY += "'"+txtRCTNO.getText()+"',";
					L_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRCTDT.getText()))+"',";// Dt convert into mm/dd/yyyy
					L_strSQLQRY += "'"+txtPRDTP.getText()+"',";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_LOTNO).toString()+"',";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_RCLNO).toString()+"',";
					L_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtRCTDT.getText()+" "+tblENTTB1.getValueAt(i,TB1_STRTM).toString()))+"',";
					L_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtRCTDT.getText()+" "+tblENTTB1.getValueAt(i,TB1_ENDTM).toString()))+"',";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_REMDS).toString()+"',";
					L_strSQLQRY += ""+tblENTTB1.getValueAt(i,TB1_STRCT).toString()+",";	
					L_strSQLQRY += ""+tblENTTB1.getValueAt(i,TB1_ENDCT).toString()+",";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_PKGTP).toString()+"',";
					L_strSQLQRY += ""+tblENTTB1.getValueAt(i,TB1_BAGQT).toString()+",";
					L_strSQLQRY += ""+tblENTTB1.getValueAt(i,TB1_BAGPK).toString()+",";
					L_strSQLQRY += ""+tblENTTB1.getValueAt(i,TB1_MISCT).toString()+",";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_MCHNO).toString()+"',";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_SHFCD).toString()+"',";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_MNLCD).toString()+"',";
					L_strSQLQRY += "'"+tblENTTB1.getValueAt(i,TB1_UCLTG).toString()+"',";
					L_strSQLQRY += "'1',";
					L_strSQLQRY += "'0',";
					L_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
					L_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
				}
				else if(flgCHK_EXIST)// if exist in db then update command is fire.
				{
					//System.out.println("Update mode");
					L_strSQLQRY ="Update FG_LWMST set ";
					L_strSQLQRY += " LW_RCTDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRCTDT.getText()))+"',";// Dt convert into mm/dd/yyyy
					L_strSQLQRY += " LW_PRDTP='"+txtPRDTP.getText()+"',";
					L_strSQLQRY += " LW_ENDTM='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtRCTDT.getText()+" "+tblENTTB1.getValueAt(i,TB1_ENDTM).toString()))+"',";
					L_strSQLQRY += " LW_REMDS='"+tblENTTB1.getValueAt(i,TB1_REMDS).toString()+"',";
					L_strSQLQRY += " LW_STRCT="+tblENTTB1.getValueAt(i,TB1_STRCT).toString()+",";	
					L_strSQLQRY += " LW_ENDCT="+tblENTTB1.getValueAt(i,TB1_ENDCT).toString()+",";
					L_strSQLQRY += " LW_PKGTP='"+tblENTTB1.getValueAt(i,TB1_PKGTP).toString()+"',";
					L_strSQLQRY += " LW_BAGQT="+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) ? "0.000" : tblENTTB1.getValueAt(i,TB1_BAGQT).toString())+",";
					L_strSQLQRY += " LW_BAGPK="+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) ? "0" : tblENTTB1.getValueAt(i,TB1_BAGPK).toString())+",";
					L_strSQLQRY += " LW_MISCT="+tblENTTB1.getValueAt(i,TB1_MISCT).toString()+",";
					L_strSQLQRY += " LW_MCHNO='"+tblENTTB1.getValueAt(i,TB1_MCHNO).toString()+"',";
					L_strSQLQRY += " LW_SHFCD='"+tblENTTB1.getValueAt(i,TB1_SHFCD).toString()+"',";
					L_strSQLQRY += " LW_MNLCD='"+tblENTTB1.getValueAt(i,TB1_MNLCD).toString()+"',";
					L_strSQLQRY += " LW_STSFL= '"+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) ? "X" : "1")+"', ";
					L_strSQLQRY += " LW_TRNFL='0',";
					L_strSQLQRY += " LW_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
					L_strSQLQRY += " LW_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
					L_strSQLQRY += " where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR;
				}
				//System.out.println("setLCLUPD"+L_strSQLQRY);
				cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD"); // for execute Query
				modRMMST();// call Remark Master Table
				if(!flgCHK_EXIST)//
				{
					L_strSQLQRY = "update CO_CDTRN set CMT_CCSVL = '"+txtRCTNO.getText().substring(3,8)+"' where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXRCT' and cmt_CODCD='"+strYRDGT+txtRCTTP.getText()+"'";
					cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
				}
				if(cl_dat.exeDBCMT("exeSAVE_LWMST"))
				{
					setMSG("Saved Successfully exeSave_LWMST",'N');
				}
				else
					setMSG("Error In Saving",'E');	
			}
			if(L_intSELROW ==0)
			{
				setMSG("No rows selcted",'E');
				return;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE_LWMST");
		}
	}
	/**
	 *  Function for insert and update the data into the FG_RCTRN table(Non-Production receipt (bagging ))
	*/
	private void exeSAVE_RCTRN()
	{
		try
		{
			flgRCTNO_NEW = true;
			int L_intSELROW = 0;
			String L_strSQLQRY = "";
			String L_strPKGCT="";
			for(int i=0;i<tblENTTB2.getRowCount();i++)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
					break;
				if(tblENTTB2.getValueAt(i,TB2_LOTNO).toString().length()<8)
					break;
				if(tblENTTB2.getValueAt(i,TB2_CHKFL).toString().equals("false"))
				{	
					continue;
				}
				L_intSELROW++;
				strWHRSTR=" RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and "
								+" RCT_RCTTP = '"+txtRCTTP.getText()+"' and "
								+" RCT_RCTNO = '"+txtRCTNO.getText()+"' and "
								+" RCT_PRDTP = '"+txtPRDTP.getText()+"' and "
								+" RCT_LOTNO = '"+tblENTTB2.getValueAt(i,TB2_LOTNO).toString()+"' and "
								+" RCT_RCLNO = '"+tblENTTB2.getValueAt(i,TB2_RCLNO).toString()+"' and "
								+" RCT_MNLCD = '"+tblENTTB2.getValueAt(i,TB2_MNLCD).toString()+"' and "
								+" RCT_PKGTP = '"+tblENTTB2.getValueAt(i,TB2_PKGTP).toString()+"'";
				flgCHK_EXIST =  chkEXIST("FG_RCTRN", strWHRSTR);	
	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
				{
				
					if(!flgMODRCPT)
						JOptionPane.showMessageDialog(this,"Record already exists in FG_RCTRN");
					continue;
				}
				if(!flgCHK_EXIST)// if not exist insert command is fire.
				{
					if(!flgMODRCPT)
						if(!getRCTNO())
							continue;
					//getRCTNO();
					inlTBLEDIT(tblENTTB2);
					L_strSQLQRY = "insert into FG_RCTRN(RCT_CMPCD,RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_RCTDT,RCT_PRDTP,RCT_GINNO,RCT_ISSRF,RCT_LOTNO,RCT_RCLNO,RCT_MNLCD,RCT_PKGTP,RCT_PKGCT,RCT_RCTQT,RCT_RCTPK,RCT_SHFCD,RCT_STKTP,RCT_STSFL,RCT_TRNFL,RCT_LUSBY,RCT_LUPDT) values (";
					L_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"',";
					strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
					L_strSQLQRY +="'"+strWRHTP+"',";
					L_strSQLQRY += "'"+txtRCTTP.getText()+"',";
					L_strSQLQRY += "'"+txtRCTNO.getText()+"',";
					L_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRCTDT.getText()))+"',";// Dt convert into mm/dd/yyyy
					L_strSQLQRY += "'"+txtPRDTP.getText()+"',";
					L_strSQLQRY += "'"+txtGINNO.getText()+"',";
					L_strSQLQRY += "'"+txtISSRF.getText()+"',";
					L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_LOTNO).toString()+"',";
					L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_RCLNO).toString()+"',";
					//L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_PRDCD).toString()+"',";
					L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_MNLCD).toString()+"',";
					L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_PKGTP).toString()+"',";
					L_strPKGCT = getCDTRN("SYSFGXXPKG"+tblENTTB2.getValueAt(i,TB2_PKGTP).toString(),"CMT_CCSVL",hstCDTRN);
					L_strSQLQRY += "'"+L_strPKGCT+"',";
					L_strSQLQRY += ""+tblENTTB2.getValueAt(i,TB2_RCTQT).toString()+",";
					L_strSQLQRY += ""+tblENTTB2.getValueAt(i,TB2_RCTPK).toString()+",";
					L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_SHFCD).toString()+"',";
					L_strSQLQRY += "'"+getCDTRN("SYSFGXXRTP"+txtRCTTP.getText(),"CMT_CHP01",hstCDTRN)+"',";
					L_strSQLQRY += "'1',";
					L_strSQLQRY += "'0',";
					L_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
					L_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
				}
				else if(flgCHK_EXIST)// if exist in db then update command is fire.
				{
					L_strSQLQRY = "update FG_RCTRN set";
					L_strSQLQRY +=" RCT_RCTDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRCTDT.getText()))+"',";
					L_strSQLQRY +=" RCT_RCTQT="+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) ? "0.000" : tblENTTB2.getValueAt(i,TB2_RCTQT))+",";
					L_strSQLQRY +=" RCT_RCTPK="+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) ? "0" :tblENTTB2.getValueAt(i,TB2_RCTPK))+",";
					L_strSQLQRY +=" RCT_SHFCD='"+tblENTTB2.getValueAt(i,TB2_SHFCD).toString()+"',";
					L_strSQLQRY += "RCT_STKTP = '"+getCDTRN("SYSFGXXRTP"+txtRCTTP.getText(),"CMT_CHP01",hstCDTRN)+"',";
					L_strSQLQRY +=" RCT_STSFL = '"+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) ? "X" : "1")+"',";
					L_strSQLQRY +=" RCT_TRNFL='0',";
					L_strSQLQRY +=" RCT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
					L_strSQLQRY +=" RCT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
					L_strSQLQRY +=" where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR;
				}
				//System.out.println("QUERY FG_RCTRN= "+L_strSQLQRY);
				cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD"); // For execute Query
				modRMMST();// call Remark Master Table
				if(!flgCHK_EXIST)//
				{
					L_strSQLQRY = "update co_CDTRN set CMT_CCSVL = '"+txtRCTNO.getText().substring(3,8)+"' where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXRCT' and cmt_CODCD='"+strYRDGT+txtRCTTP.getText()+"'";
					cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
				}
				if(cl_dat.exeDBCMT("exeSAVE_RCTRN"))
				{
					setMSG("Saved Successfully exeSAVE_RCTRN",'N');
				}
				else
				{
					setMSG("Error In Saving",'E');	
				}
			}
			if(L_intSELROW ==0)
				{setMSG("No rows selcted",'E'); return;}
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE_RCTRN");
		}
	}	
	/** Function for insert  the data into the PR_LTMST table when new lot no generate.
	 * 
	*/
	private void exeSAVE_LTMST()
	{
		try
		{  
			for(int i=0;i<tblENTTB2.getRowCount();i++)
			{
				if(tblENTTB2.getValueAt(i,TB2_LOTNO).toString().length()<8)
					break;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					if(tblENTTB2.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					{
						L_strSQLQRY ="select count(*) LT_RECCT from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '"+txtPRDTP.getText()+"'";
						L_strSQLQRY += " and LT_LOTNO = '"+tblENTTB2.getValueAt(i,TB2_LOTNO).toString()+"'";
						L_strSQLQRY += " and LT_RCLNO = '"+tblENTTB2.getValueAt(i,TB2_RCLNO).toString()+"'";
						//System.out.println(L_strSQLQRY);
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
						if(L_rstRSSET !=null && L_rstRSSET.next())
							if((L_rstRSSET.getInt("LT_RECCT")>0))
								continue;
						L_strSQLQRY = "Insert into PR_LTMST(LT_CMPCD,LT_PRDTP,LT_LOTNO,LT_RCLNO,LT_PRDCD,LT_TPRCD,LT_CPRCD,";
						L_strSQLQRY += "LT_LINNO,LT_CYLNO,LT_PSTDT,LT_PENDT,LT_CLSFL,LT_RESFL,LT_STSFL,";
						L_strSQLQRY += "LT_TRNFL,LT_IPRDS,LT_LUSBY,LT_LUPDT) values (";
						L_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"',";
						L_strSQLQRY +="'"+txtPRDTP.getText()+"',";
						L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_LOTNO).toString()+"',";
						L_strSQLQRY += "'"+tblENTTB2.getValueAt(i,TB2_RCLNO).toString()+"',";
						L_strSQLQRY += "'"+strPRDCD+"',";
						L_strSQLQRY += "'"+strPRDCD+"',";
						L_strSQLQRY += "'"+strPRDCD+"',";
						L_strSQLQRY += "'01',";
						L_strSQLQRY += "'01',";
						L_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
						L_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
						L_strSQLQRY += "'0',";
						L_strSQLQRY += "'0',";
						L_strSQLQRY += "'2',";
						L_strSQLQRY += "'0',";
						L_strSQLQRY += "'SCP',";
						L_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						L_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
						//System.out.println(L_strSQLQRY);
						cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
					}	
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE_LTMST"))
			{
				setMSG("Saved Successfully exeSAVE_LTMST",'N');
			}
			else
			{
				setMSG("Error In Saving",'E');	
			}			
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE_LTMST");
		}
	}	
	/**
	 * Checks whether the remark exists within the Remark Master Table //i.e FG_RMMST
	 */
	private void modRMMST()  
	{
		try
		{				
			M_strSQLQRY  = " RM_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RM_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"'";
			M_strSQLQRY += " and RM_TRNTP = '"+LM_TRNTP+"'";
			M_strSQLQRY += " and RM_DOCTP = '"+txtRCTTP.getText()+"'";
			M_strSQLQRY+= " and RM_DOCNO = '"+txtRCTNO.getText()+"'";
			if(chkEXIST("FG_RMMST", M_strSQLQRY))
				updRMMST();
			else
				insRMMST();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"modRMMST");
		}
	}
	/**
	 * Update the Remark within the Remark Master Table i.e FG_RMMST
	*/
	private void updRMMST()
	{
		try
		{
			M_strSQLQRY = "Update FG_RMMST set ";
			M_strSQLQRY += "RM_REMDS = '"+txtREMDS.getText()+"',";
			M_strSQLQRY += "RM_TRNFL = '0',";
			M_strSQLQRY += "RM_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "RM_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
			M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"'";
			M_strSQLQRY += " and RM_TRNTP = '"+LM_TRNTP+"'";
			M_strSQLQRY += " and RM_DOCTP = '"+txtRCTTP.getText()+"'";
			M_strSQLQRY += " and RM_DOCNO = '"+txtRCTNO.getText()+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			if(cl_dat.exeDBCMT("UPDRMMST"))
			{
				setMSG("update Successfully remark",'N');
			}
			else
			{
				setMSG("Error In Saving 'FG_RMMST'",'E');	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updRMMST");
		}
	}
	/**
	 *    Inserts Remark within the Remark Master Table i.e FG_RMMST
	*/
	private void insRMMST()  
	{
		try
		{
			M_strSQLQRY  = "Insert into FG_RMMST(RM_CMPCD,RM_WRHTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,";
			M_strSQLQRY += "RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT) values (";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+cmbWRHTP.getSelectedItem().toString()+"',";
			M_strSQLQRY += "'"+LM_TRNTP+"',";
			M_strSQLQRY += "'"+txtRCTTP.getText()+"',";
			M_strSQLQRY += "'"+txtRCTNO.getText()+"',";
			M_strSQLQRY += "'"+txtREMDS.getText()+"',";
			M_strSQLQRY += getUSGDTL("RM",'I',"0")+")";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			if(cl_dat.exeDBCMT("insRMMST"))
			{
				setMSG("Saved Successfully remark",'N');
			}
			else
			{
				setMSG("Error In Saving FG_RMMST",'E');	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insRMMST");
		}
	}
	/**
	 * Function for geting Grade  
	*/
	private void getGRADE(int LP_intROWID)
	{
		try
		{//
			String L_strLOTNO=tblENTTB2.getValueAt(LP_intROWID,TB2_LOTNO).toString().trim();
			String L_strRCLNO=tblENTTB2.getValueAt(LP_intROWID,TB2_RCLNO).toString().trim();
			//M_strSQLQRY="Select PR_PRDCD,PR_PRDDS from PR_LTMST,CO_PRMST where SUBSTRING(PR_PRDCD,1,2) = '"+((txtPRDTP.getText().equals("01")==true) ? "51" : "52")+ "' AND LT_LOTNO='"+ L_strLOTNO +"' AND LT_RCLNO ='"+L_strRCLNO+"' AND LT_PRDCD=PR_PRDCD order by PR_PRDCD";
			M_strSQLQRY="Select PR_PRDCD,PR_PRDDS from PR_LTMST,CO_PRMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO='"+ L_strLOTNO +"' AND LT_RCLNO ='"+L_strRCLNO+"' AND ltrim(str(LT_PRDCD,20,0))=PR_PRDCD order by PR_PRDCD";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					String L_strPRDDS=null;
					L_strPRDDS=M_rstRSSET.getString("PR_PRDDS");
					tblENTTB2.setValueAt(L_strPRDDS,LP_intROWID,TB2_PRDCD); 
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"GetGRADE");
		}
	}	
/**
 *  method for generating new Receipt No.
*/	
	private boolean getRCTNO()
	{
		try
		{
			//System.out.println("flgRCTNO_NEW = "+flgRCTNO_NEW);
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				return true;		// DOC->document  FGXXXRCT=Finish Good xx Receipt no    
			//if(!flgRCTNO_NEW)		//  first check receipt no 
			//	return true;
		
			if(txtRCTNO.getText().trim().length()==0)
			{
				M_strSQLQRY="Select * from CO_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXRCT' and cmt_CODCD='"+strYRDGT+txtRCTTP.getText()+"'";
				System.out.println("getRCTNO "+M_strSQLQRY);
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET==null || (!M_rstRSSET.next()))
				{
					setMSG("Receipt series not found ..",'E');
					cl_dat.M_flgLCUPD_pbst = false;
					return false;
				}
				String L_strRCTNO=null;
				L_strRCTNO=Integer.toString(Integer.parseInt(M_rstRSSET.getString("CMT_CCSVL"))+1);// taken next receipt no (increase by 1)with parseInt.
				txtRCTNO.setText(L_strRCTNO);
				if(L_strRCTNO.length()<5)//
				{
					for(int i=0;i<=5;i++)
					{
						txtRCTNO.setText("0"+txtRCTNO.getText());
						if(txtRCTNO.getText().length()==5)
							break;
					}
				}	
				L_strRCTNO = txtRCTNO.getText();
				txtRCTNO.setText(strYRDGT+txtRCTTP.getText()+L_strRCTNO);
				if(M_rstRSSET!=null)
					M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRCTNO"); 
			return false;
		}
		flgRCTNO_NEW = false;
		return true;
	}
	
/**
 *	sets value for Job Work Receipt Product Code
*/
	private String getJOBPRD()  
	{
		String L_strRETSTR = "";
		try
		{
			 M_strSQLQRY = "Select * from CO_PRMST where PR_PRDCD = (select ltrim(str(LT_PRDCD,20,0)) from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = (select max(LT_LOTNO) from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO like '999%' and LT_PRDTP = '"+txtPRDTP.getText().trim()+"'))";
			 M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			 if(M_rstRSSET !=null)		 
			 {
				 if(M_rstRSSET.next())
				 {
				 	 L_strRETSTR = M_rstRSSET.getString("PR_PRDCD");
				 }
			 }
			 if(M_rstRSSET != null)
				 M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getJOBPRD");
		}
		return L_strRETSTR;
	}
/**
 *  method for generating new lotno.
 */
	private String getLOTNO()
	{
		String L_strYRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4);
		String L_strRETSTR = "999"+L_strYRDGT+"0001";
		 try
		 {
			 String L_strLOTNO=null;
			 String L_strRCLNO=null;
			 int i=0;
			 M_strSQLQRY="Select max(LT_LOTNO) LT_LOTNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(LT_LOTNO,1,4) like '999"+L_strYRDGT+"%' and LT_PRDTP = '"+txtPRDTP.getText()+"' ";
			 //System.out.println(M_strSQLQRY);
			 M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			 if(M_rstRSSET!=null)
			 {
				 if(M_rstRSSET.next())
				 {
					 if(M_rstRSSET.getString("LT_LOTNO")==null)
						 return L_strRETSTR;
					 L_strRETSTR=Integer.toString(Integer.parseInt(M_rstRSSET.getString("LT_LOTNO"))+1);
					 M_rstRSSET.close();
				 }
			 }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLOTNO"); 
		}
		return L_strRETSTR;
	}
	/**  
	 * 
	*/
	
	private void setLWMST()
	{
		try
		{
			intCNTRW=0;
			M_strSQLQRY="Select distinct LW_RCTNO from FG_LWMST where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_WRHTP='"+cmbWRHTP.getSelectedItem()+"' AND LW_RCTTP = '"+txtRCTTP.getText()+"' and LW_RCTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRCTDT.getText()))+"'  and LW_STSFL = '1' and LW_PRDTP = '"+txtPRDTP.getText()+"'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			txtRCTNO.setText(L_rstRSSET.getString("LW_RCTNO"));
			if(L_rstRSSET!=null)
				L_rstRSSET.close();

			if (setRCTDTL_LWMST(cmbWRHTP.getSelectedItem().toString().trim(),txtRCTTP.getText(),txtRCTNO.getText(),"CLEAR"))
				flgMODRCPT = true;
		}
		catch (Exception e)
		{
			setMSG(e,"setLWMST");
		}
	}
	/**
	 * 
	*/
	private void setRCTRN()
	{
		try
		{
			intCNTRW1=0;
			M_strSQLQRY="Select distinct RCT_RCTNO from FG_RCTRN where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP='"+cmbWRHTP.getSelectedItem()+"' AND RCT_RCTTP = '"+txtRCTTP.getText()+"' and RCT_RCTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRCTDT.getText()))+"' and RCT_STSFL = '1' and RCT_PRDTP = '"+txtPRDTP.getText().trim()+"' ";
			//System.out.println("set"+M_strSQLQRY +"\n");
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			txtRCTNO.setText(L_rstRSSET.getString("RCT_RCTNO"));
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
			if (setRCTDTL_RCTRN(cmbWRHTP.getSelectedItem().toString().trim(),txtRCTTP.getText(),txtRCTNO.getText(),"CLEAR"))
				flgMODRCPT = true;
		}
		catch (Exception e)
		{
			setMSG(e,"setRCTRN");
		}
	}
	/**
	 *   get the data from FG_LWMST and store in tblENTTB1
	*/
	private boolean setRCTDTL_LWMST(String LP_WRHTP, String LP_RCTTP, String LP_RCTNO, String LP_CLRFL)
	{
		intCNTRW=0;
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			{
				((JCheckBox) tblENTTB2.cmpEDITR[TB2_CHKFL]).setEnabled(false);
				((JCheckBox) tblENTTB1.cmpEDITR[TB1_CHKFL]).setEnabled(true);
			}
			double L_dblTOTQT=0.0;
			double L_dblTOTPK=0.0;
			String L_strBAGQT="";
		    double L_dblBAGQT=0.0;
			String L_strBAGPK="";
		    double L_dblBAGPK=0.0;
			tblENTTB1.clrTABLE();
			tblENTTB2.clrTABLE();
			
			L_strSQLQRY="Select RM_REMDS from FG_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP='"+LP_WRHTP+"' AND RM_TRNTP='"+LM_TRNTP+"' AND  RM_DOCTP='"+LP_RCTTP+"' AND RM_DOCNO = '"+LP_RCTNO+"'" ;
			ResultSet LP_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			if(LP_rstRSSET != null)
			{
				if(LP_rstRSSET.next())
				{
					txtREMDS.setText(LP_rstRSSET.getString("RM_REMDS"));
				}
			}
			if(LP_rstRSSET!=null)
				LP_rstRSSET.close();
						
			M_strSQLQRY = "Select * from FG_LWMST where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_WRHTP = '"+LP_WRHTP+"' and  LW_RCTTP = '"+LP_RCTTP+"' and LW_RCTNO = '"+LP_RCTNO+"' and LW_PRDTP='"+txtPRDTP.getText().trim()+"' ";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
			{
				M_strSQLQRY +="	and LW_STSFL = '1'";
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			{
				M_strSQLQRY +="	and LW_STSFL <> 'X'";
			}
            else
				M_strSQLQRY +="	and LW_STSFL not in('2','X')";
				
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if (L_rstRSSET == null || !L_rstRSSET.next())
			{
				setMSG("Record not found in FG_LWMST",'E'); 
				return false;
			}
			txtRCTDT.setText(M_fmtLCDAT.format(L_rstRSSET.getDate("LW_RCTDT")));
			txtPRDTP.setText(L_rstRSSET.getString("LW_PRDTP"));
			if(tblENTTB1.isEditing())
			tblENTTB1.getCellEditor().stopCellEditing();
			tblENTTB1.setRowSelectionInterval(0,0);
			tblENTTB1.setColumnSelectionInterval(0,0);
			int i=0;
			for(i=0;i<tblENTTB1.getRowCount();i++)
			{
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_LOTNO"),i,TB1_LOTNO);// 
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_RCLNO"),i,TB1_RCLNO);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_STRTM").substring(11,16),i,TB1_STRTM);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_ENDTM").substring(11,16),i,TB1_ENDTM);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_REMDS"),i,TB1_REMDS);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_STRCT"),i,TB1_STRCT);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_ENDCT"),i,TB1_ENDCT);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_PKGTP"),i,TB1_PKGTP);
				L_strBAGQT=L_rstRSSET.getString("LW_BAGQT");
				L_dblBAGQT=Double.parseDouble(L_strBAGQT);
				tblENTTB1.setValueAt(L_strBAGQT,i,TB1_BAGQT);
				L_strBAGPK=L_rstRSSET.getString("LW_BAGPK");
				L_dblBAGPK=Double.parseDouble(L_strBAGPK);
				tblENTTB1.setValueAt(L_strBAGPK,i,TB1_BAGPK);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_MISCT"),i,TB1_MISCT);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_MCHNO"),i,TB1_MCHNO);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_SHFCD"),i,TB1_SHFCD);
				tblENTTB1.setValueAt(L_rstRSSET.getString("LW_MNLCD"),i,TB1_MNLCD);
				L_dblTOTQT +=L_dblBAGQT;
				L_dblTOTPK +=L_dblBAGPK;
				
				intCNTRW++;
				if(!L_rstRSSET.next())             
					break;				
			}
			txtTOTQT.setText(setNumberFormat(L_dblTOTQT,3));
			txtTOTPK.setText(setNumberFormat(L_dblTOTPK,3));
		}
		catch(Exception e)
		{
			setMSG(e,"INPVF");
			return false;
		}
		 return true;
	}
	/**
	 * get the data from FG_RCTRN and store in tblENTTB2
	*/
	private boolean setRCTDTL_RCTRN(String LP_WRHTP, String LP_RCTTP, String LP_RCTNO, String LP_CLRFL)
	{
		intCNTRW1=0;
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			{
				((JCheckBox) tblENTTB2.cmpEDITR[TB2_CHKFL]).setEnabled(true);
				((JCheckBox) tblENTTB1.cmpEDITR[TB1_CHKFL]).setEnabled(false);
			}
			double L_dblTOTQT=0.0;
			double L_dblTOTPK=0.0;
			String L_strBAGQT="";
		    double L_dblBAGQT=0.0;
			String L_strBAGPK="";
		    double L_dblBAGPK=0.0;
			tblENTTB1.clrTABLE();
			tblENTTB2.clrTABLE();
			L_strSQLQRY="Select RM_REMDS from FG_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP='"+LP_WRHTP+"' AND RM_TRNTP='"+LM_TRNTP+"' AND  RM_DOCTP='"+LP_RCTTP+"' AND RM_DOCNO = '"+LP_RCTNO+"'" ;
			System.out.println("NEW QUERY="+L_strSQLQRY);
			ResultSet LP_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			if(LP_rstRSSET != null)
			{
				if(LP_rstRSSET.next())
				{
					txtREMDS.setText(LP_rstRSSET.getString("RM_REMDS"));
				}
				LP_rstRSSET.close();
			}
			//M_strSQLQRY = "Select * from FG_RCTRN where RCT_WRHTP = '"+LP_WRHTP+"' and  RCT_RCTTP = '"+LP_RCTTP+"' and RCT_RCTNO = '"+LP_RCTNO+"' AND RCT_PRDTP='"+txtPRDTP.getText().trim()+"' ";
			M_strSQLQRY = "Select PR_PRDDS,RCT_RCTDT,RCT_PRDTP,RCT_LOTNO,RCT_RCLNO,RCT_MNLCD,RCT_PKGTP,RCT_RCTQT,RCT_RCTPK,RCT_GINNO,RCT_ISSRF,RCT_SHFCD,RCT_STSFL  from FG_RCTRN,co_prmst,pr_ltmst ";
			M_strSQLQRY += " where RCT_WRHTP = '"+LP_WRHTP+"' and  RCT_RCTTP = '"+LP_RCTTP+"' and RCT_RCTNO = '"+LP_RCTNO+"' AND RCT_PRDTP='"+txtPRDTP.getText().trim()+"' ";
			M_strSQLQRY += " and LT_CMPCD=RCT_CMPCD and LT_PRDTP=RCT_PRDTP and LT_LOTNO=RCT_LOTNO and LT_RCLNO=RCT_RCLNO and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ltrim(str(LT_PRDCD,20,0)) = PR_PRDCD ";
			System.out.println(M_strSQLQRY);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
			{
				M_strSQLQRY +="	AND RCT_STSFL = '1'";
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			{
				M_strSQLQRY +=" AND RCT_STSFL  <> 'X'";
			}
            else
				M_strSQLQRY +="	and RCT_STSFL  not in('2','X')";
			System.out.println("Select "+M_strSQLQRY);
			
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if (L_rstRSSET == null || !L_rstRSSET.next())
			{
				setMSG("Record not found in FG_RCTRN",'E');
				return false;
			}
			if(tblENTTB2.isEditing())
			tblENTTB2.getCellEditor().stopCellEditing();
			tblENTTB2.setRowSelectionInterval(0,0);
			tblENTTB2.setColumnSelectionInterval(0,0);
			txtRCTDT.setText(M_fmtLCDAT.format(L_rstRSSET.getDate("RCT_RCTDT")));
		
			txtPRDTP.setText(L_rstRSSET.getString("RCT_PRDTP"));
			for(int i=0;i<tblENTTB2.getRowCount();i++)
			{
				tblENTTB2.setValueAt(L_rstRSSET.getString("RCT_LOTNO"),i,TB2_LOTNO); 
		
				tblENTTB2.setValueAt(L_rstRSSET.getString("RCT_RCLNO"),i,TB2_RCLNO);
		
				tblENTTB2.setValueAt(L_rstRSSET.getString("PR_PRDDS"),i,TB2_PRDCD);
		
				tblENTTB2.setValueAt(L_rstRSSET.getString("RCT_MNLCD"),i,TB2_MNLCD);
		
				tblENTTB2.setValueAt(L_rstRSSET.getString("RCT_PKGTP"),i,TB2_PKGTP);
		
				L_strBAGQT=L_rstRSSET.getString("RCT_RCTQT");
		
				L_dblBAGQT=Double.parseDouble(L_strBAGQT);
				tblENTTB2.setValueAt(L_strBAGQT,i,TB2_RCTQT);
				L_strBAGPK=L_rstRSSET.getString("RCT_RCTPK");
				L_dblBAGPK=Double.parseDouble(L_strBAGPK);
				tblENTTB2.setValueAt(L_strBAGPK,i,TB2_RCTPK);
				tblENTTB2.setValueAt(L_rstRSSET.getString("RCT_SHFCD"),i,TB2_SHFCD);
				L_dblTOTQT +=L_dblBAGQT;
				L_dblTOTPK +=L_dblBAGPK;
				if(flgRTNFL)
				{
					txtGINNO.setText(L_rstRSSET.getString("RCT_GINNO"));
					txtISSRF.setText(L_rstRSSET.getString("RCT_ISSRF"));
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
					tblENTTB2.setValueAt(L_rstRSSET.getString("RCT_STSFL"),i,TB2_AUTFL);
					
				intCNTRW1 ++;
				if(!L_rstRSSET.next())
					break;
			}
			txtTOTQT.setText(setNumberFormat(L_dblTOTQT,3));
			txtTOTPK.setText(setNumberFormat(L_dblTOTPK,3));
			if(L_rstRSSET!=null)
				L_rstRSSET.close();

		}
		catch (Exception e)
		{
			setMSG(e,"setRCTDTL_RCTRN");
			return false;
		}
			return true;
	}
	/** Input Verifier
 */	
	private class INPVF extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtRCTTP)
				{
					if(!hstCDTRN.containsKey("SYSFGXXRTP"+txtRCTTP.getText()))//// receipt type check in Hashtable
					{
						setMSG("Invalid Receipt Type",'E');
						txtRCTTP.requestFocus();
						return false;
					}
					//flgLWMST = (txtRCTTP.getText().equals("10") || txtRCTTP.getText().equals("15")) ?  true :  false;
					//	flgLWMST = (txtRCTTP.getText().equals(strFRSPD) || txtRCTTP.getText().equals(strREPAC)) ?  true :  false;
					if(flgFRPRO)
						flgLWMST =true;
					else
						flgLWMST =false;
					
					//System.out.println("flgLWMST =  "+flgLWMST);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						(flgLWMST ?   tblENTTB1 : tblENTTB2).setEnabled(true);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
						txtRCTDT.setText(strREFDT);
					return true;
				}
				if(input==txtRCTNO)
				{
					//if(txtRCTTP.getText().equals("10") || txtRCTTP.getText().equals("15"))
					//if(txtRCTTP.getText().equals(strFRSPD) || txtRCTTP.getText().equals(strREPAC))
					if(flgLWMST)
					{
						if (!setRCTDTL_LWMST(cmbWRHTP.getSelectedItem().toString().trim(),txtRCTTP.getText(),txtRCTNO.getText(),"CLEAR"))
							return false;
					}
					else if(!flgLWMST)
					{
						if (!setRCTDTL_RCTRN(cmbWRHTP.getSelectedItem().toString().trim(),txtRCTTP.getText(),txtRCTNO.getText(),"CLEAR"))
							return false;
					}
				}
				if(input==txtPRDTP)
				{
					if(!hstCDTRN.containsKey("MSTCOXXPRD"+txtPRDTP.getText()))
					{
						setMSG("Invalid Product Type",'E'); 
						return false;
					}
				//	if(txtRCTTP.getText().equals(strJBWRC) && txtPRDTP.getText().equals("02"))
					//if(txtRCTTP.getText().equals(strJBWRC) && txtPRDTP.getText().equals("02"))
					if(flgJOBFL && txtPRDTP.getText().equals("02"))
					{
						chkAUTOLOT.setVisible(true);
					}
					else
						chkAUTOLOT.setVisible(false);
				/*	
					flgMODRCPT = false;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))					
					{
						if(flgLWMST)
							setLWMST();
						else
							setRCTRN();
					}
				*/
				}
				if(input==txtRCTDT)
				{
					if(!txtRCTDT.getText().equals(strREFDT))
					{
					
						//JOptionPane.showMessageDialog(this,"Please Login with "+strREFDT,"Date Enter Status",JOptionPane.INFORMATION_MESSAGE);
						setMSG("Please enter Receipt date as "+strREFDT,'E');
						return false;
					}
					if(!txtRCTDT.getText().equals(strPRVDT1_AI)  && cl_dat.M_strCMPCD_pbst.equals("01"))
					{
					
						//JOptionPane.showMessageDialog(this,"Please Login with "+strREFDT,"Date Enter Status",JOptionPane.INFORMATION_MESSAGE);
						setMSG("Last Auto Issue Generation Date : "+strPRVDT1_AI,'E');
						return false;
					}
					
					if(M_fmtLCDAT.parse(txtRCTDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Invalid Date ...",'E');
						return false;
					}
				}
				
				if(input==txtISSRF)
				{
					if(txtISSRF.getText().trim().length()==8)
					{
						if(flgRTNFL)
						{
							M_strSQLQRY = "Select * from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO = '"+txtISSRF.getText()+"'";
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							if(M_rstRSSET.next())
							{
								M_rstRSSET.close();
								return true;
							}
							else
							{
								setMSG("Invalid Invoice Number",'E');
								return false;
							}
						}
					}
				}				
			}
			catch (Exception e)
			{
				setMSG(e,"INPVF");
				return false;
			}
			return true;
		}
	}
	/** 
	 * Table Input Verifier
	*/
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				if(getSource()==tblENTTB1)
				{
					if(P_intCOLID>0)
						if(P_intCOLID!=TB1_AUTFL)
							if(((JTextField)tblENTTB1.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
								return true;
					if(P_intCOLID==TB1_LOTNO)//Validates Lot No. for Production JTable
					{
						strWHRSTR ="LT_PRDTP = '"+txtPRDTP.getText()+"' and LT_LOTNO = '"+((JTextField)tblENTTB1.cmpEDITR[TB1_LOTNO]).getText()+"'";
						M_strSQLQRY = "select LT_LOTNO, max(LT_RCLNO) LT_RCLNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR+" and LT_STSFL<> '1' group By LT_LOTNO ";
						//M_strSQLQRY = "Select LT_LOTNO,SUBSTRING(PR_PRDDS,1,15) PR_PRDDS,max(LT_RCLNO) LT_RCLNO from pr_ltmst,co_prmst where LT_PRDCD=PR_PRDCD and (LT_RCTRF is null or LT_RCTRF=' ')";
						//System.out.println("LOTNO "+M_strSQLQRY);
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(!L_rstRSSET.next() || L_rstRSSET == null)
						{
							setMSG("Invalid Lot No..",'E');
							//txtLOTNO1.requestFocus();
							setMSG("Invalid Lot Number ",'E');
							return false;
						}
						tblENTTB1.setValueAt(L_rstRSSET.getString("LT_RCLNO"),tblENTTB1.getSelectedRow(),TB1_RCLNO);
						L_rstRSSET.close();
						if(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_STRTM).toString().trim().length()!=5)
						{
							tblENTTB1.setValueAt(cl_dat.M_txtCLKTM_pbst.getText().trim(),tblENTTB1.getSelectedRow(),TB1_STRTM);
							int L_intENDMIN  =0;
							int L_intENDHR=0;
							String L_strENDMIN=cl_dat.M_txtCLKTM_pbst.getText().trim().substring(3,5);
							String L_strENDHR =cl_dat.M_txtCLKTM_pbst.getText().trim().substring(0,2);
							if(L_strENDMIN.equals("00"))
							{
								L_strENDMIN="01";
							}
							else if(L_strENDMIN.equals("59"))
							{
								L_strENDMIN="00";
								L_intENDHR=Integer.parseInt(L_strENDHR)+1;
								if(L_intENDHR==24)
								{
									L_strENDHR="00";
								}
								else
								{
									L_strENDHR=""+L_intENDHR;
								}
							}
							else
							{
								L_intENDMIN=Integer.parseInt(L_strENDMIN)+1;
								L_strENDMIN=""+L_intENDMIN;
								if(L_strENDMIN.length()<2)
								{
									L_strENDMIN="0"+L_intENDMIN;
								}						
							}
							L_strENDHR=L_strENDHR+":"+L_strENDMIN;
							tblENTTB1.setValueAt(L_strENDHR,tblENTTB1.getSelectedRow(),TB1_ENDTM);
						}
					}
					else if(P_intCOLID==TB1_RCLNO)
					{
						if(flgRPKFL)
						{
							if(tblENTTB1.getValueAt(P_intROWID,TB1_REMDS).toString().trim().length()==0)
								tblENTTB1.setValueAt("Repacking", P_intROWID,TB1_REMDS);
							if(P_intROWID==0)
							{
								if(tblENTTB1.getValueAt(P_intROWID,TB1_STRCT).toString().trim().length()==0)
									tblENTTB1.setValueAt("000", P_intROWID,TB1_STRCT);
								if(tblENTTB1.getValueAt(P_intROWID,TB1_ENDCT).toString().trim().length()==0)
									tblENTTB1.setValueAt("001", P_intROWID,TB1_ENDCT);
								if(tblENTTB1.getValueAt(P_intROWID,TB1_PKGTP).toString().trim().length()==0)
									tblENTTB1.setValueAt("99", P_intROWID,TB1_PKGTP);
								if(tblENTTB1.getValueAt(P_intROWID,TB1_MCHNO).toString().trim().length()==0)
									tblENTTB1.setValueAt("99", P_intROWID,TB1_MCHNO);
							}
						}
						if(P_intROWID>0)
						{
							
							int L_intENDMIN=0;
							int L_intENDHR=0;
							
							String L_strENDMIN=(tblENTTB1.getValueAt(P_intROWID-1,TB1_ENDTM).toString().trim()).substring(3,5);
							String L_strENDHR =(tblENTTB1.getValueAt(P_intROWID-1,TB1_ENDTM).toString().trim()).substring(0,2);
							if(L_strENDMIN.equals("00"))
							{
								L_strENDMIN="01";
							}
							else if(L_strENDMIN.equals("59"))
							{
								L_strENDMIN="00";
								L_intENDHR=Integer.parseInt(L_strENDHR)+1;
								if(L_intENDHR==24)
								{
									L_strENDHR="00";
								}
								else
								{
									L_strENDHR=""+L_intENDHR;
								}
							}
							else
							{
								L_intENDMIN=Integer.parseInt(L_strENDMIN)+1;
								L_strENDMIN=""+L_intENDMIN;
								if(L_strENDMIN.length()<2)
								{
									L_strENDMIN="0"+L_intENDMIN;
								}						
							}
							L_strENDHR=L_strENDHR+":"+L_strENDMIN;
							//System.out.println("End TIME"+L_strENDHR);
							if(tblENTTB1.getValueAt(P_intROWID,TB1_ENDTM).toString().trim().length()==0)
							   	tblENTTB1.setValueAt(L_strENDHR,tblENTTB1.getSelectedRow(),TB1_ENDTM);
							String L_strSTRCT = tblENTTB1.getValueAt(P_intROWID-1,TB1_STRCT).toString().trim();
							String L_strENDCT = tblENTTB1.getValueAt(P_intROWID-1,TB1_ENDCT).toString().trim();
							String L_strPKGTP = tblENTTB1.getValueAt(P_intROWID-1,TB1_PKGTP).toString().trim();
							String L_strMCHNO = tblENTTB1.getValueAt(P_intROWID-1,TB1_MCHNO).toString().trim();
							String L_strMNLCD = tblENTTB1.getValueAt(P_intROWID-1,TB1_MNLCD).toString().trim();

							if(tblENTTB1.getValueAt(P_intROWID,TB1_STRCT).toString().trim().length()==0)
								tblENTTB1.setValueAt(L_strENDCT, P_intROWID,TB1_STRCT);
							if(tblENTTB1.getValueAt(P_intROWID,TB1_PKGTP).toString().trim().length()==0)
								tblENTTB1.setValueAt(L_strPKGTP, P_intROWID,TB1_PKGTP);
							if(tblENTTB1.getValueAt(P_intROWID,TB1_MCHNO).toString().trim().length()==0)
								tblENTTB1.setValueAt(L_strMCHNO, P_intROWID,TB1_MCHNO);
							if(tblENTTB1.getValueAt(P_intROWID,TB1_MNLCD).toString().trim().length()==0)
								tblENTTB1.setValueAt(L_strMNLCD, P_intROWID,TB1_MNLCD);
						}
						
						
						
					}
					else if(P_intCOLID==TB1_STRTM)
					{
						String L_strSTRTM=((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM]).getText();
						if(Integer.parseInt(L_strSTRTM.substring(0,2))>=24)
						{
							setMSG("Invalid Start Time ",'E');
							return false;
						}
						tblENTTB1.setValueAt(getSHFCD(tblENTTB1.getValueAt(P_intROWID,TB1_STRTM).toString().trim()),P_intROWID,TB1_SHFCD);
					}
					else if(P_intCOLID==TB1_ENDTM)
					{
						String L_strSTRTM=((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM]).getText();
						String L_strENDTM=((JTextField)tblENTTB1.cmpEDITR[TB1_ENDTM]).getText();
						if(Integer.parseInt(L_strENDTM.substring(0,2))>=24)
						{
							setMSG("Invalid End Time ",'E');
							return false;
						}
						if(L_strENDTM.length()<5)
						{
							setMSG("Invalid End Time ",'E');
							return false;
						}							
						if(L_strSTRTM.compareTo(L_strENDTM)>0)
						{
							if(((Integer.parseInt(L_strSTRTM.substring(0,2))>=23) ||(Integer.parseInt(L_strSTRTM.substring(0,2))>=19)) && ((Integer.parseInt(L_strENDTM.substring(0,2))<24)||(Integer.parseInt(L_strENDTM.substring(0,2))<7)))
							{
								return true;
							}
							else
							{
								setMSG("Invalid End Time ",'E');
								return false;
							}
						}
						return true;
					}
					else if(P_intCOLID==TB1_STRCT)//Validates Start Count for Production JTable
					{
						String L_strSTRCT = tblENTTB1.getValueAt(P_intROWID,TB1_STRCT).toString().trim();
						if(L_strSTRCT.equals(""))
							return false;
						else
						{
							int L_intSTRCT = Integer.parseInt(L_strSTRCT);
							if(L_intSTRCT < 0)
								return false;
							else
								return true;
						}
					}
					else if(P_intCOLID==TB1_ENDCT)//Validates End Count for Production JTable
					{
						String L_strENDCT = tblENTTB1.getValueAt(P_intROWID,TB1_ENDCT).toString().trim();
						if(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_ENDCT).toString().trim().equals(""))
							return false;
						else
						{
							int L_intSTRCT = Integer.parseInt(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_STRCT).toString().trim());
							int L_intENDCT = Integer.parseInt(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_ENDCT).toString().trim());
							int L_intBAGPK = L_intENDCT-L_intSTRCT;
							if(L_intSTRCT > L_intENDCT)
								return false;
							else
							{
								tblENTTB1.setValueAt(setNumberFormat(L_intBAGPK,0),tblENTTB1.getSelectedRow(),TB1_BAGPK);
								return true;
							}
						}
					}
					else if(P_intCOLID==TB1_PKGTP)// 
					{
						String L_strPKGTP =((JTextField)tblENTTB1.cmpEDITR[TB1_PKGTP]).getText();
						if(L_strPKGTP.equals("99"))
							return true;
						if(!hstCDTRN.containsKey("SYSFGXXPKG"+L_strPKGTP))
						{
							if(L_strPKGTP.length()==0)
							{
								return false;
							}
							else
							{	
								setMSG("Invalid package type",'E');
								return false;
							}
						}
						double L_dblNCSVL = Double.parseDouble(getCDTRN("SYSFGXXPKG"+L_strPKGTP,"CMT_NCSVL",hstCDTRN));
						//System.out.println("Pkg Wt."+L_dblNCSVL);
						int L_intSTRCT = Integer.parseInt(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_STRCT).toString().trim());
						int L_intENDCT = Integer.parseInt(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_ENDCT).toString().trim());
						int L_intBAGPK = L_intENDCT-L_intSTRCT;					
						double L_dblBAGQT = L_intBAGPK * L_dblNCSVL;
						tblENTTB1.setValueAt(setNumberFormat(L_dblBAGQT,3),tblENTTB1.getSelectedRow(),TB1_BAGQT);
						return true;
					}
					else if(P_intCOLID==TB1_BAGQT)
					{
						
						String L_strPKGTP =  tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_PKGTP).toString().trim();
						if(flgRPKFL || L_strPKGTP.equals("99"))
							return true;
						//if(L_strPKGTP.equals("99"))
						//	return true;
						double L_dblNCSVL = Double.parseDouble(getCDTRN("SYSFGXXPKG"+tblENTTB1.getValueAt(P_intROWID,TB1_PKGTP).toString().trim(),"CMT_NCSVL",hstCDTRN));
						double L_dblBAGQT = Double.parseDouble(((JTextField)tblENTTB1.cmpEDITR[TB1_BAGQT]).getText());
						
						double L_dblBAGPK=L_dblBAGQT/L_dblNCSVL;
						int L_intSTRCT = Integer.parseInt(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_STRCT).toString().trim());
						int L_intENDCT = Integer.parseInt(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_ENDCT).toString().trim());
						//System.out.println("Entered : "+L_dblBAGQT);
						//System.out.println("Bags : "+(L_intENDCT-L_intSTRCT));
						//System.out.println("Pkg.Wt. : "+L_dblNCSVL);
						//System.out.println("Calc.Qty. : "+((L_intENDCT-L_intSTRCT)*L_dblNCSVL));
						//System.out.println("Calc.Qty1. : "+setNumberFormat(((L_intENDCT-L_intSTRCT)*L_dblNCSVL),3));
						if(L_dblBAGQT>Double.parseDouble((setNumberFormat(((L_intENDCT-L_intSTRCT)*L_dblNCSVL),3))))
						{
							return false;
						}
						double L_dblMISCT = (L_intENDCT-L_intSTRCT) - L_dblBAGPK;
						tblENTTB1.setValueAt(setNumberFormat(L_dblBAGPK,0),tblENTTB1.getSelectedRow(),TB1_BAGPK);	
						tblENTTB1.setValueAt(setNumberFormat(L_dblMISCT,0),tblENTTB1.getSelectedRow(),TB1_MISCT);
						txtTOTQT.setText(setNumberFormat(getTOTQT(tblENTTB1,TB1_BAGQT),3));
						return true;	
					}
					else if(P_intCOLID==TB1_BAGPK)
					{
						String L_strPKGTP =  tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_PKGTP).toString().trim();
						if(!flgRPKFL && !L_strPKGTP.equals("99") )
						{
							//System.out.println(L_strPKGTP);
							double L_dblSTRCT = Double.parseDouble(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_STRCT).toString().trim());
							double L_dblENDCT = Double.parseDouble(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_ENDCT).toString().trim());
							double L_dblBAGPK = Double.parseDouble(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_BAGPK).toString().trim());
							double L_dblNCSVL = Double.parseDouble(getCDTRN("SYSFGXXPKG"+tblENTTB1.getValueAt(P_intROWID,TB1_PKGTP).toString().trim(),"CMT_NCSVL",hstCDTRN));
							double L_dblBAGQT = L_dblBAGPK * L_dblNCSVL;
							tblENTTB1.setValueAt(setNumberFormat(L_dblBAGQT,3),tblENTTB1.getSelectedRow(),TB1_BAGQT);
							if(L_dblBAGPK>(L_dblENDCT-L_dblSTRCT))
							{
								setMSG("Invalid Package Quantity",'E');
								return false;					   
							}
							double L_dblMISCT=(L_dblENDCT-L_dblSTRCT)-L_dblBAGPK;
							tblENTTB1.setValueAt(setNumberFormat(L_dblMISCT,0),tblENTTB1.getSelectedRow(),TB1_MISCT);
							txtTOTPK.setText(setNumberFormat(getTOTQT(tblENTTB1,TB1_BAGPK),0));
							return true;	
						}
					}
					else if(P_intCOLID==TB1_MISCT)
					{
						String L_strPKGTP =  tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_PKGTP).toString().trim();
						double L_dblSTRCT = Double.parseDouble(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_STRCT).toString().trim());
						double L_dblENDCT = Double.parseDouble(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_ENDCT).toString().trim());
						double L_dblBAGPK = Double.parseDouble(tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_BAGPK).toString().trim());
						String  L_strMISCT = ((JTextField)tblENTTB1.cmpEDITR[TB1_MISCT]).getText();
						double L_dblMISCT=Double.parseDouble(L_strMISCT);
						if(flgRPKFL ||L_strPKGTP.equals("99"))
						{	//return true;
							if(L_dblMISCT>(L_dblENDCT-L_dblSTRCT))
							{
								setMSG("Wrong Mis Count",'E');
								return false;								   
							}
						}
						else
						{
							if(L_dblMISCT>(L_dblENDCT-L_dblSTRCT))
							{
								setMSG("Wrong Mis Count",'E');
								return false;								   
							}
							double L_dblMISCT1=(L_dblENDCT-L_dblSTRCT)-L_dblBAGPK;
							double L_dblBAGPK1 = (L_dblENDCT-L_dblSTRCT)-L_dblMISCT;
							tblENTTB1.setValueAt(setNumberFormat(L_dblBAGPK1,0),tblENTTB1.getSelectedRow(),TB1_BAGPK);	
							double L_dblNCSVL = Double.parseDouble(getCDTRN("SYSFGXXPKG"+tblENTTB1.getValueAt(P_intROWID,TB1_PKGTP).toString().trim(),"CMT_NCSVL",hstCDTRN));
							double L_dblBAGQT = ((L_dblENDCT-L_dblSTRCT)-L_dblMISCT)*L_dblNCSVL;
							tblENTTB1.setValueAt(setNumberFormat(L_dblBAGQT,3),tblENTTB1.getSelectedRow(),TB1_BAGQT);	
							txtTOTPK.setText(setNumberFormat(getTOTQT(tblENTTB1,TB1_BAGPK),0));
							txtTOTQT.setText(setNumberFormat(getTOTQT(tblENTTB1,TB1_BAGQT),3));
							if(L_dblMISCT!=(L_dblENDCT-L_dblSTRCT)-L_dblBAGPK1)
							{
								L_dblMISCT=(L_dblENDCT-L_dblSTRCT)-L_dblBAGPK1;
								setMSG("Wrong Mis Count, Mis count is "+L_dblMISCT,'E');
								return false;					   
							}
							return true;	
						}
					}
					else if(P_intCOLID==TB1_MCHNO)//Validates Machine No. for Production JTable
					{
						String L_strMCHNO = tblENTTB1.getValueAt(P_intROWID,TB1_MCHNO).toString().trim();
						if(L_strMCHNO.equals("99"))
							return true;
						if(!hstCDTRN.containsKey("SYSFGXXMCH"+L_strMCHNO))
						{
							if(L_strMCHNO.length()==0)
							{
								return false;
							}
							else
							{	
								setMSG("Invalid Machine No",'E');
								return false;
							}
						}		
					}	
					else if(P_intCOLID==TB1_MNLCD)//Validates Main Location for Production JTable
					{
						tblENTTB1.setValueAt(((JTextField)tblENTTB1.cmpEDITR[TB1_MNLCD]).getText().toUpperCase(),P_intROWID,TB1_MNLCD);
						strWHRSTR ="LC_MNLCD = '"+((JTextField)tblENTTB1.cmpEDITR[TB1_MNLCD]).getText().toUpperCase()+"'";
						
						M_strSQLQRY = "select LC_MNLCD from FG_LCMST where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR+" ";
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(!L_rstRSSET.next() || L_rstRSSET == null)
						{
							setMSG("Invalid Main Location..",'E');
							txtMNLCD1.requestFocus();
							return false;
						}
						if(L_rstRSSET!=null)
							L_rstRSSET.close();
						if(!vldMNLCD_SLR(tblENTTB1.getValueAt(P_intROWID,TB1_LOTNO).toString(),tblENTTB1.getValueAt(P_intROWID,TB1_RCLNO).toString(),((JTextField)tblENTTB1.cmpEDITR[TB1_MNLCD]).getText(),flgRTNFL))
							return false;
						if(!exeMAXQT(strWHRSTR))
							setMSG("Bagged Qty should not exceed "+String.valueOf(setNumberFormat(dblAVLQT,3))+" mt.",'E');
						//JOptionPane.showMessageDialog(null,"Bagged Qty should not exceed "+String.valueOf(setNumberFormat(dblAVLQT,3))+" mt."," Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
						//return false;
					}
					else if(P_intCOLID==TB1_SHFCD)
					{
						String L_strSHFCD = ((JTextField)tblENTTB1.cmpEDITR[TB1_SHFCD]).getText().toUpperCase();
						String L_strSTRTM=((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM]).getText();
						String L_strENDTM=((JTextField)tblENTTB1.cmpEDITR[TB1_ENDTM]).getText();
						//String L_strSTRTM = tblENTTB1.getValueAt(P_intROWID,TB1_STRTM).toString().trim();
						//String L_strENDTM = tblENTTB1.getValueAt(P_intROWID,TB1_ENDTM).toString().trim();
						String L_strSTRTM1= getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHFCD,"CMT_CHP01",hstCDTRN);
						String L_strENDTM1= getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHFCD,"CMT_CHP02",hstCDTRN);
						if(!hstCDTRN.containsKey("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHFCD))
						{
							setMSG("Invalid shift ",'E');
							//return false;
						}
						if(L_strSTRTM1.compareTo(L_strSTRTM)>0 || L_strENDTM1.compareTo(L_strENDTM)<0)
						{
							//if(((Integer.parseInt(L_strSTRTM.substring(0,2))>=23) ||(Integer.parseInt(L_strSTRTM.substring(0,2))>=19)) && ((Integer.parseInt(L_strENDTM.substring(0,2))<24)||(Integer.parseInt(L_strENDTM.substring(0,2))<7)))
							if(((L_strSTRTM.compareTo(getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+"C","CMT_CHP01",hstCDTRN))>0)||(L_strSTRTM.compareTo(getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+"E","CMT_CHP01",hstCDTRN))>0)) && ((Integer.parseInt(L_strENDTM.substring(0,2))<24)||(L_strENDTM.compareTo(getCDTRN("MSTCOXXSHF"+"E","CMT_CHP02",hstCDTRN))<0)))
							{
								//if((Integer.parseInt(L_strSTRTM.substring(0,2))>=23)&& (L_strSHFCD.equals("C")))
								if((L_strSTRTM.compareTo(getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+"C","CMT_CHP01",hstCDTRN))>0)&& (L_strSHFCD.equals("C")))
								{
									//System.out.println("IN  C IF LOOP");
									return true;
								}
								else if(L_strSHFCD.equals("E"))
								{
									//System.out.println("IN IF E  IF LOOP");
									return true;
								}
								else
								{
									//setMSG("Invalid Shift 2last",'E');
									setMSG("Invalid Shift",'E');
									//return false;
								}
							}
							else
							{
								//setMSG("Invalid Shift LAST ",'E');
								setMSG("Invalid Shift ",'E');
								//return false;
							}
						}
					}	
					else if(P_intCOLID==TB1_AUTFL)
					{
						String L_strLOTNO = tblENTTB1.getValueAt(P_intROWID,TB1_LOTNO).toString().trim();
						String L_strRLCNO = tblENTTB1.getValueAt(P_intROWID,TB1_RCLNO).toString().trim();
						String L_strSTRTM=((JTextField)tblENTTB1.cmpEDITR[TB1_STRTM]).getText();
						String L_strENDTM=((JTextField)tblENTTB1.cmpEDITR[TB1_ENDTM]).getText();
						String L_strSTRCT = tblENTTB1.getValueAt(P_intROWID,TB1_STRCT).toString().trim();
						String L_strENDCT = tblENTTB1.getValueAt(P_intROWID,TB1_ENDCT).toString().trim();
						String L_strPKGTP = tblENTTB1.getValueAt(P_intROWID,TB1_PKGTP).toString().trim();
						String L_strMCHNO = tblENTTB1.getValueAt(P_intROWID,TB1_MCHNO).toString().trim();
						String L_strMNLCD = tblENTTB1.getValueAt(P_intROWID,TB1_MNLCD).toString().trim();
						if(tblENTTB1.getValueAt(P_intROWID+1,TB1_LOTNO).toString().trim().length()==0)
							tblENTTB1.setValueAt(L_strLOTNO, P_intROWID+1,TB1_LOTNO); 
						if(tblENTTB1.getValueAt(P_intROWID+1,TB1_RCLNO).toString().trim().length()==0)
							tblENTTB1.setValueAt(L_strRLCNO, P_intROWID+1,TB1_RCLNO);
						if(tblENTTB1.getValueAt(P_intROWID+1,TB1_STRTM).toString().trim().length()==0)
							tblENTTB1.setValueAt(L_strENDTM, P_intROWID+1,TB1_STRTM);
					/*	if(tblENTTB1.getValueAt(P_intROWID+1,TB1_STRCT).toString().trim().length()==0)
							tblENTTB1.setValueAt(L_strENDCT, P_intROWID+1,TB1_STRCT);
						if(tblENTTB1.getValueAt(P_intROWID+1,TB1_PKGTP).toString().trim().length()==0)
							tblENTTB1.setValueAt(L_strPKGTP, P_intROWID+1,TB1_PKGTP);
						if(tblENTTB1.getValueAt(P_intROWID+1,TB1_PKGTP).toString().trim().length()==0)
							tblENTTB1.setValueAt(L_strMCHNO, P_intROWID+1,TB1_PKGTP);
						if(tblENTTB1.getValueAt(P_intROWID+1,TB1_MNLCD).toString().trim().length()==0)
							tblENTTB1.setValueAt(L_strMNLCD, P_intROWID+1,TB1_MNLCD);
						*/
					}
			    }
				if(getSource()==tblENTTB2)
				{
					if(P_intCOLID>0)
						if(P_intCOLID!=TB2_AUTFL)
							if(((JTextField)tblENTTB2.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
							return true;
					if(P_intCOLID==TB2_LOTNO)//Validates Lot No. for Non-Production JTable
					{
						if(txtPRDTP.getText().trim().equals("02")&& ! flgRTNFL)
						{
							tblENTTB2.setValueAt("00",P_intROWID,TB2_RCLNO);
							tblENTTB2.setValueAt("SCP",P_intROWID,TB2_PRDCD);
						}
						else
						{
							//if(txtRCTTP.getText().equals("30"))
							//if(txtRCTTP.getText().equals(strSLSRT))
							if(flgRTNFL)
							{
								M_strSQLQRY = "Select IST_LOTNO,IST_RCLNO from FG_ISTRN,MR_IVTRN where IVT_INVNO='"+txtISSRF.getText()+"' ";
								M_strSQLQRY += " and IST_ISSNO=IVT_LADNO and IST_MKTTP=IVT_MKTTP and IST_CMPCD=IVT_CMPCD and IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_LOTNO='"+((JTextField)tblENTTB2.cmpEDITR[TB2_LOTNO]).getText()+"' AND IST_PRDTP='"+txtPRDTP.getText().trim()+"'";
							}
							else
							{				
								strWHRSTR ="LT_PRDTP = '"+txtPRDTP.getText()+"' and LT_LOTNO = '"+((JTextField)tblENTTB2.cmpEDITR[TB2_LOTNO]).getText()+"'";
								M_strSQLQRY = "select LT_LOTNO, max(LT_RCLNO) LT_RCLNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR+" group by LT_LOTNO";
							}
							//System.out.println("INPUT = "+M_strSQLQRY);
							ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
							if(!L_rstRSSET.next() || L_rstRSSET == null)
							{
								setMSG("Invalid Lot No..",'E');
								txtLOTNO2.requestFocus();
								return false;
							}
							
							//tblENTTB2.setValueAt(L_rstRSSET.getString("LT_RCLNO"),P_intROWID,TB2_RCLNO);
							tblENTTB2.setValueAt(L_rstRSSET.getString(2),P_intROWID,TB2_RCLNO);
							//System.out.println("LAST");
							L_rstRSSET.close();
							getGRADE(P_intROWID);
						}
					}
					else if(P_intCOLID==TB2_MNLCD)//Validates Main Location for Non-Production JTable
					{
						String L_strLOTNO1="";
						String L_strMNLCD1="";
						strWHRSTR ="LC_MNLCD = '"+((JTextField)tblENTTB2.cmpEDITR[TB2_MNLCD]).getText()+"'";
						M_strSQLQRY = "select LC_MNLCD from FG_LCMST where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR+" ";
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(!L_rstRSSET.next() || L_rstRSSET == null)
						{
							setMSG("Invalid Main Location..",'E');
							txtMNLCD2.requestFocus();
							return false;
						}
						if(!vldMNLCD_SLR(tblENTTB2.getValueAt(P_intROWID,TB2_LOTNO).toString(),tblENTTB2.getValueAt(P_intROWID,TB2_RCLNO).toString(),((JTextField)tblENTTB2.cmpEDITR[TB2_MNLCD]).getText(),flgRTNFL))
							return false;
						String L_strLOTNO = tblENTTB2.getValueAt(P_intROWID,TB2_LOTNO).toString().trim();
						String L_strMNLCD = ((JTextField)tblENTTB2.cmpEDITR[TB2_MNLCD]).getText().trim();
						for(int i=0;i<P_intROWID;i++)
						{
							L_strLOTNO1 = tblENTTB2.getValueAt(i,TB2_LOTNO).toString().trim();
							L_strMNLCD1 = tblENTTB2.getValueAt(i,TB2_MNLCD).toString().trim();
						//	System.out.println("LOT NO ="+L_strLOTNO1);
						//	System.out.println("LOT NO ="+L_strMNLCD1);
							if((L_strLOTNO1.equals(L_strLOTNO))&&(L_strMNLCD1.equals(L_strMNLCD)))
							{
								setMSG("Entry with same Lot No.& Main Location is not valid ",'E');
								return false;
							}
						}
					}
					else if(P_intCOLID==TB2_PKGTP)//Validates Package Type for Non-Production JTable
					{
						String L_strPKGTP = ((JTextField)tblENTTB2.cmpEDITR[TB2_PKGTP]).getText();
						if(L_strPKGTP.equals("99"))
							return true;
						if(!hstCDTRN.containsKey("SYSFGXXPKG"+L_strPKGTP))
						{
							setMSG("Invalid package type Tab2",'E');
							return false;
						}
					}
					else if(P_intCOLID==TB2_RCTQT)
					{
						double L_dblNCSVL = Double.parseDouble(getCDTRN("SYSFGXXPKG"+tblENTTB2.getValueAt(P_intROWID,TB2_PKGTP).toString().trim(),"CMT_NCSVL",hstCDTRN));	
						double L_dblRCTQT = Double.parseDouble(tblENTTB2.getValueAt(tblENTTB2.getSelectedRow(),TB2_RCTQT).toString().trim()); 		
						double L_RCTPK = L_dblRCTQT / L_dblNCSVL;	
												
						//if(flgRTNFL)
						//if(txtRCTTP.getText().equals("30"))
						//if(txtRCTTP.getText().equals(strSLSRT))
						if(flgRTNFL)
						{
							if(!vldRCTQT_SLR(tblENTTB2,TB2_LOTNO,TB2_RCLNO,TB2_RCTQT))
								return false;
							else
							{
								tblENTTB2.setValueAt(setNumberFormat(L_RCTPK,3),tblENTTB2.getSelectedRow(),TB2_RCTPK);	
								txtTOTQT.setText(setNumberFormat(getTOTQT(tblENTTB2,TB2_RCTQT),3));
								txtTOTPK.setText(setNumberFormat(getTOTQT(tblENTTB2,TB2_RCTPK),3));
							}
						}
						else
						{
							tblENTTB2.setValueAt(setNumberFormat(L_RCTPK,3),tblENTTB2.getSelectedRow(),TB2_RCTPK);	
							txtTOTQT.setText(setNumberFormat(getTOTQT(tblENTTB2,TB2_RCTQT),3));
							txtTOTPK.setText(setNumberFormat(getTOTQT(tblENTTB2,TB2_RCTPK),3));
						}
					}
					else if(P_intCOLID==TB2_SHFCD)
					{
						if(!hstCDTRN.containsKey("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+((JTextField)tblENTTB2.cmpEDITR[TB2_SHFCD]).getText()))
						{
							setMSG("Invalid Shift",'E');
							//return false;
						}					
					}
					else if(P_intCOLID==TB2_AUTFL)
					{
						String L_strLOTNO = tblENTTB2.getValueAt(P_intROWID,TB2_LOTNO).toString().trim();
						if(tblENTTB2.getValueAt(P_intROWID+1,TB2_LOTNO).toString().trim().length()==0)
							tblENTTB2.setValueAt(L_strLOTNO, P_intROWID+1,TB2_LOTNO); 
					}
				}
				if(getSource()==tblRETCP)
				{
					if(P_intCOLID==TB3_CMPFL || P_intCOLID==TB3_RETTG)
					{
						tblRETCP.setValueAt(new Boolean(true),TB3_CHKFL,tblRETCP.getSelectedRow());
					}
				}
}
			catch(Exception e)
			{
				setMSG(e,"TableInputVerifier");
				return false;
			}
			return true;
		}
	}
	
	
	/** Validating Location code for preventing Sales Return material mix-up with fresh stock
	 */
	private boolean vldMNLCD_SLR(String LP_LOTNO, String LP_RCLNO, String LP_MNLCD, boolean LP_RTNFL)
	{
		boolean L_flgRETFL = true;
		try
		{		
			String L_strSTSFL_CHK = LP_RTNFL ? " <> '2'" : " = '2' ";  // ST_STSFL = '2' in FG_STMST indicates sales return stock
			M_strSQLQRY = "Select ST_MNLCD from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_LOTNO = '"+LP_LOTNO +"' and ST_RCLNO = '"+LP_RCLNO+"' and ST_MNLCD = '"+LP_MNLCD+"' and ST_STSFL "+L_strSTSFL_CHK+" and isnull(ST_STKQT,0)>0";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println("Sales Ret. mix-up validation : "+M_strSQLQRY);
			L_flgRETFL = M_rstRSSET.next() ? false : true;
	        M_rstRSSET.close();
			String L_strVLDMSG = LP_RTNFL ? " Fresh Material of "+LP_LOTNO+" aleady Exists at Location "+LP_MNLCD : " Sales Return Material of "+LP_LOTNO+" aleady Exists at Location "+LP_MNLCD;
			if(!L_flgRETFL)
				setMSG(L_strVLDMSG,'E');
		}
		catch(Exception e)
		{
			setMSG(e,"vldMNLCD_SLR");
			return L_flgRETFL;
		}
		return L_flgRETFL;
	}
	
	
	
	/**
	 */
	private boolean exeMAXQT(String L_strMNLCD)
	{
		try
		{		
			dblAVLQT=0;
			double L_dblMAXQT=0.0;
			double L_dblSTQTY=0.0;
			String L_strBAGQT = tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_BAGQT).toString().trim();
			//String L_strMNLCD = tblENTTB1.getValueAt(tblENTTB1.getSelectedRow(),TB1_MNLCD).toString().trim();
			M_strSQLQRY = "Select LC_MAXQT,LC_STKQT from FG_LCMST where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+L_strMNLCD+" ";
			//System.out.println("\n QTY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_dblMAXQT = Double.parseDouble(M_rstRSSET.getString("LC_MAXQT").toString().trim()); 
				//System.out.println(L_dblMAXQT);
				L_dblSTQTY = Double.parseDouble(M_rstRSSET.getString("LC_STKQT").toString().trim()); 
				//System.out.println(L_dblSTQTY);
				double L_dblEXTQT = (L_dblMAXQT*5)/100;
				dblAVLQT = (L_dblMAXQT+L_dblEXTQT) - L_dblSTQTY;
				double L_dblBAGQT = Double.parseDouble(L_strBAGQT);
				if(L_dblBAGQT <= dblAVLQT)
				{
					M_rstRSSET.close();
					return true;
				}
				else
					return false;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeMAXQT");
		}
		return false;
	}
	/**
	 *  Checking key in table for record existance
	*/
	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
	{
		boolean L_flgCHKFL = false;
		try
		{
			M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				L_flgCHKFL = true;
				L_rstRSSET.close();
			}
			if(L_rstRSSET!=null)
				L_rstRSSET.close();

		}
		catch (Exception L_EX)	
		{
			setMSG("Error in chkEXIST : "+L_EX,'E');
		}
		return L_flgCHKFL;
	}
		
	/** One time data capturing for specified codes from CO_CDTRN
	  * into the Hash Table
	*/
	 private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
	{
		String L_strSQLQRY = "";
	    try
	    {
	        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	            setMSG("Records not found in CO_CDTRN",'E');
				return;
	        }
	        while(true)
	        {
				strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
	            strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
	            strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
	            String[] staCDTRN = new String[intCDTRN_TOT];
	            staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
	            staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
	            staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
	            staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
	            staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
	            staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
	            staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
	            staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
	            staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
	            LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
				hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
	            if (!L_rstRSSET.next())
	               break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX,"crtCDTRN");
	    }
	}
	/** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	*/
	private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
	{
		try
		{
			if(!hstCDTRN.containsKey(LP_CDTRN_KEY))
			{
				setMSG(LP_CDTRN_KEY+" not found in hstCDTRN",'E');
			}
		    if (LP_FLDNM.equals("CMT_CODCD"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
		    else if (LP_FLDNM.equals("CMT_CODDS"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
		    else if (LP_FLDNM.equals("CMT_SHRDS"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
		    else if (LP_FLDNM.equals("CMT_CHP01"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
		    else if (LP_FLDNM.equals("CMT_CHP02"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
		    else if (LP_FLDNM.equals("CMT_NMP01"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
		    else if (LP_FLDNM.equals("CMT_NMP02"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
		    else if (LP_FLDNM.equals("CMT_NCSVL"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
		    else if (LP_FLDNM.equals("CMT_CCSVL"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
		return "";
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
	/** Function for getting Total Quantity  */
	private double getTOTQT(JTable LP_TBLNM,int LP_intFLDNO) 
	{
		double L_dblTOTQT=0;
		for(int i=0 ;i<LP_TBLNM.getRowCount();i++)
		{
			if(LP_TBLNM.getValueAt(i,LP_intFLDNO).toString().length()==0)
				break;
			L_dblTOTQT +=Double.parseDouble(LP_TBLNM.getValueAt(i,LP_intFLDNO).toString());
		}
			return L_dblTOTQT;
	}
	
	/**  Validating Receipt Quantity
	 */
	private boolean vldRCTQT_SLR(JTable P_tblENTTB2,int P_intLOTNO,int P_intRCLNO,int P_intRCTQT)
	{ 
		try
		{    
			String L_strINVQT = "";
			String L_strRCTQT_SLR = "";
			String L_strLOCQT="";
			
			String L_strLOTNO  = P_tblENTTB2.getValueAt(P_tblENTTB2.getSelectedRow(),P_intLOTNO).toString().trim();
			String L_strRCLNO  = P_tblENTTB2.getValueAt(P_tblENTTB2.getSelectedRow(),P_intRCLNO).toString().trim();
			String L_strLOCQT1 = P_tblENTTB2.getValueAt(P_tblENTTB2.getSelectedRow(),P_intRCTQT).toString().trim();
			float L_fltSUMQT = 0;
			for(int i=0;i<=P_tblENTTB2.getSelectedRow();i++)
			{
				//if(LM_ENTTB2.getValueAt(i,TB2_CHKFL).toString().trim().equals("true")){	
				String L_strLOTNO1 = P_tblENTTB2.getValueAt(i,P_intLOTNO).toString().trim();
				String L_strRCLNO1 = P_tblENTTB2.getValueAt(i,P_intRCLNO).toString().trim();
				if(L_strLOTNO1.equals(L_strLOTNO) && L_strRCLNO1.equals(L_strRCLNO))
				{
					L_strLOCQT = P_tblENTTB2.getValueAt(i,P_intRCTQT).toString().trim();
					float L_fltLOCQT = Float.parseFloat(L_strLOCQT);
					L_fltLOCQT = L_fltLOCQT + L_fltSUMQT;
					L_fltSUMQT = L_fltLOCQT;
				}
			}
			M_strSQLQRY = "Select isnull(sum(IVT_INVQT),0) from FG_ISTRN,MR_IVTRN where IVT_INVNO='"+txtISSRF.getText()+"'";
			M_strSQLQRY += " and IST_MKTTP=IVT_MKTTP  and IST_ISSNO=IVT_LADNO and IST_CMPCD=IVT_CMPCD and IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_LOTNO = '"+L_strLOTNO+"' and IST_RCLNO = '"+L_strRCLNO+"'";
			//System.out.println("QYERY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			L_strINVQT = M_rstRSSET.next() ? M_rstRSSET.getString(1) : "0.000";
			
			M_strSQLQRY = "Select isnull(sum(isnull(RCT_RCTQT,0)),0) from FG_RCTRN where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTTP = '30' and RCT_ISSRF = '"+txtISSRF.getText()+"'";
			M_strSQLQRY += " and RCT_LOTNO = '"+L_strLOTNO+"' and RCT_RCLNO = '"+L_strRCLNO+"'";
			//System.out.println("QYERY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			L_strRCTQT_SLR = M_rstRSSET.next() ? M_rstRSSET.getString(1) : "0.000";

			M_rstRSSET.close();
			float L_fltISLCQT = Float.parseFloat(L_strINVQT)-Float.parseFloat(L_strRCTQT_SLR);
			if(L_fltSUMQT > L_fltISLCQT)
				{setMSG("Quantity is more than Balance Invoice Quantity : "+setNumberFormat(L_fltISLCQT,3),'E'); return false;}
			setMSG(" ",'N');
			return true;
					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeTB2QTY");
		}
		return false;
	}
			
	/**
	 *  Function to Compare  Shift start time and return Shift 
	*/
	private String getSHFCD(String LP_STRTM)
	{
		String L_strRETSTR = "";
		if((LP_STRTM.compareTo(getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHFA","CMT_CHP01",hstCDTRN))>=0) && (getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHFB","CMT_CHP01",hstCDTRN).compareTo(LP_STRTM))>=0)
			L_strRETSTR = "A";
		else if((LP_STRTM.compareTo(getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHFB","CMT_CHP01",hstCDTRN))>=0) && (getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHFC","CMT_CHP01",hstCDTRN).compareTo(LP_STRTM))>=0)
			L_strRETSTR = "B";
		else if((LP_STRTM.compareTo(getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHFC","CMT_CHP01",hstCDTRN))>0) || (LP_STRTM.compareTo(strSTRTM_DAY))<0)
			L_strRETSTR = "C";
		return L_strRETSTR;
	}
	private Vector getHLPVTR(String LP_HLPTP, String LP_TXTVL)
	{
		vtrHLPARR.clear();
		if (LP_HLPTP.equals("PKGTP1"))
		{
			setHST_ARR(hstCDTRN);
			for(int i=0;i<arrHSTKEY.length;i++)
				if(arrHSTKEY[i].toString().substring(0,10).equals("SYSFGXXPKG"))
					vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
		}
		else if (LP_HLPTP.equals("PRDTP"))
		{
			setHST_ARR(hstCDTRN);
			for(int i=0;i<arrHSTKEY.length;i++)
				if(arrHSTKEY[i].toString().substring(0,10).equals("MSTCOXXPRD"))
					vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
		}
		else if (LP_HLPTP.equals("RCTTP"))
		{
			setHST_ARR(hstCDTRN);
			for(int i=0;i<arrHSTKEY.length;i++)
				if(arrHSTKEY[i].toString().substring(0,10).equals("SYSFGXXRTP"))
					vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
		}
		else if (LP_HLPTP.equals("MCHNO"))
		{
			setHST_ARR(hstCDTRN);
			for(int i=0;i<arrHSTKEY.length;i++)
				if(arrHSTKEY[i].toString().substring(0,10).equals("SYSFGXXMCH"))
					vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
		}
		else if (LP_HLPTP.equals("SHFCD"))
		{
			setHST_ARR(hstCDTRN);
			for(int i=0;i<arrHSTKEY.length;i++)
				if(arrHSTKEY[i].toString().substring(0,10).equals("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"))
					vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CHP01",hstCDTRN)});
		}
		return vtrHLPARR;
	}
			
	/**  Conversion of Hash table keys to sorted array
	 *   For retrieving elements in sorted order
	 */
	private void setHST_ARR(Hashtable LP_HSTNM)
	{
		try
		{
			arrHSTKEY = LP_HSTNM.keySet().toArray();
			Arrays.sort(arrHSTKEY);
		}
		catch(Exception e){setMSG(e,"setHST_ARR");}
	}
	
	/**
	 * Function for fetching Data to  Retention Completion Tag And Show in Table
	 * According to the Condition
	*/
	private void getRETLOT()
	{
		try
		{
			vtrCMPFL = new Vector();
			int LM_CNT = 0;
			String L_strRETTG="";
			String L_strCMPFL="";
			tblRETCP.clrTABLE();
			if(rdbTRNDT.isSelected())
			{
				M_strSQLQRY = "Select distinct LW_PRDTP,LW_LOTNO,LW_RCLNO,PR_PRDDS,LT_CYLNO,LT_RETQT,LT_STSFL from ";
				M_strSQLQRY += " fg_lwmst,pr_ltmst,co_prmst where lw_CMPCD=lt_cmpcd and lw_prdtp=lt_prdtp and lw_lotno=lt_lotno and lw_rclno=lt_rclno ";
				M_strSQLQRY += " and pr_prdcd=ltrim(str(LT_PRDCD,20,0)) and LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lw_rctdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRETDAT.getText()))+"'";
				M_strSQLQRY += " order by lw_prdtp,lw_rclno,lw_lotno desc";
			}
			else if(rdbLOTNO.isSelected())
			{
				M_strSQLQRY  = "Select lt_prdtp lw_prdtp,lt_lotno lw_lotno,lt_rclno lw_rclno, pr_prdds,lt_cylno,lt_retqt,lt_stsfl from";
				M_strSQLQRY += " pr_ltmst,co_prmst where ltrim(str(lt_prdcd,20,0))=pr_prdcd ";
				M_strSQLQRY += " and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_lotno='"+txtRETLOT.getText().trim()+"'";
				M_strSQLQRY += " order by lt_prdtp,lt_rclno,lt_lotno desc";
			}
			M_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
			if (M_rstRSSET == null || !M_rstRSSET.next())
			{
				setMSG("Record not found in FG_LWMST",'E'); 
			}
			tblRETCP.clrTABLE();
			if(tblRETCP.isEditing())
			tblRETCP.getCellEditor().stopCellEditing();
			tblRETCP.setRowSelectionInterval(0,0);
			tblRETCP.setColumnSelectionInterval(0,0);
			for(int i=0;i<tblRETCP.getRowCount();i++)
			{
				tblRETCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("LW_PRDTP"),""),i,TB3_PRDTP); 
				tblRETCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("LW_RCLNO"),""),i,TB3_RCLNO);
				tblRETCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("LW_LOTNO"),""),i,TB3_LOTNO);
				tblRETCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""),i,TB3_GRADE);
				tblRETCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),""),i,TB3_CYLNO);
				//tblRETCP.setValueAt(getCDTRN("SYSPRXXCYL"+M_rstRSSET.getString("LT_CYLNO"),"CMT_SHRDS",hstCDTRN),i,TB3_CYLNO);
				tblRETCP.setValueAt((nvlSTRVL(M_rstRSSET.getString("LT_RETQT"),"").equals("0.025") ? new Boolean(true) : new Boolean(false)),i,TB3_RETTG);
				tblRETCP.setValueAt((nvlSTRVL(M_rstRSSET.getString("LT_STSFL"),"").equals("1") ? new Boolean(true) : new Boolean(false)),i,TB3_CMPFL);
				if(!M_rstRSSET.next())
					break;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception E)
		{
			setMSG(E,"getRETLOT");
		}
	}
	/**
	  * @return void
	  * Updating Start Time, Receipt Ref. & Bagged Qty. within Product Lot Master Table 
	  * if the Completion Flag Status entered against the Lot is 'Y' i.e PR_LTMST Table	
	*/
	private void updLTMST()
	{
		String L_strPRDTP = "";
		String L_strLOTNO = "";
		String L_strRCLNO= "";
		String L_strRETTG="";
		String L_strCMPFL="";
		String L_strSQLQRY="";
		String L_strSTRTM= "null";
		String L_strENDTM = "null";
		cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			for(int i=0;i < tblRETCP.getRowCount();i++)
			{
				L_strPRDTP = tblRETCP.getValueAt(i,TB3_PRDTP).toString().trim();
				L_strLOTNO = tblRETCP.getValueAt(i,TB3_LOTNO).toString().trim();
				L_strRCLNO = tblRETCP.getValueAt(i,TB3_RCLNO).toString().trim();
				L_strRETTG = tblRETCP.getValueAt(i,TB3_RETTG).toString().trim();
				L_strCMPFL = tblRETCP.getValueAt(i,TB3_CMPFL).toString().trim();
				if(L_strCMPFL.equals("false") && L_strRETTG.equals("false"))
				{
					if((tblRETCP.getValueAt(i,TB3_CHKFL).toString().trim()).equals("false"))
						continue;
				}
				//System.out.println(L_strCMPFL);
				M_strSQLQRY = "Update PR_LTMST set ";
				if(L_strCMPFL.equals("true"))
				{
					L_strSTRTM = getLOTSTM(L_strPRDTP,L_strLOTNO,L_strRCLNO);
				    L_strENDTM = getLOTETM(L_strPRDTP,L_strLOTNO,L_strRCLNO);
					M_strSQLQRY += "LT_BSTDT = "+L_strSTRTM+",";
					M_strSQLQRY += "LT_BENDT = "+L_strENDTM+",";
				}
				M_strSQLQRY += "LT_STSFL = '"+(L_strCMPFL.equals("true") ? "1" : "0")+"',";
				M_strSQLQRY += "LT_RETQT = "+(L_strRETTG.equals("true") ? "0.025" : "0.000")+",";
				M_strSQLQRY += "LT_TRNFL = '0',";
				M_strSQLQRY += "LT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "LT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
				M_strSQLQRY += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '"+L_strPRDTP+"'";
				M_strSQLQRY += " and LT_LOTNO = '"+L_strLOTNO+"'";
				M_strSQLQRY += " and LT_RCLNO = '"+L_strRCLNO+"'";
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");                  
				if(cl_dat.exeDBCMT("updLTMST"))
				{
					setMSG("Saved Successfully LTMST",'N');
				}
			}
		    if(cl_dat.M_flgLCUPD_pbst)		  
				setMSG("Updation Completed Successfully.",'N');
		    else
				setMSG("Updation not Completed Successfully.",'E');
		}
	    catch(Exception L_EX)
		{
			cl_dat.M_flgLCUPD_pbst = false;
		    setMSG(L_EX,"UpdLTMST");
		}
	}
	/** Function for fetching Min Start Time of perticuler Lot No.  */
	private String getLOTSTM(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO)
	{ 
		Timestamp L_tmsSTRTM = null;
		String L_strSTRTM="null";
		//String str="";
		String L_strSQLQRY ="";
		try
		{		
			L_strSQLQRY = "Select min(LW_STRTM) LW_STRTM from FG_LWMST where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_PRDTP = '"+LP_PRDTP+"' and";
			L_strSQLQRY += " LW_LOTNO='"+LP_LOTNO+"' and LW_RCLNO='"+LP_RCLNO+"' order by LW_STRTM";
			M_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_tmsSTRTM=M_rstRSSET.getTimestamp("LW_STRTM");
			}	
			if(L_tmsSTRTM!=null)
			{
				L_strSTRTM = "'"+M_fmtDBDTM.format(L_tmsSTRTM).toString()+"'";
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLOTSTM Start Time");
		}
		return L_strSTRTM;
		
	}
	/** Function for fetching Max End Date of perticuler Lot No.  */
	private String getLOTETM(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO)
	{ 
		String L_strENDTM  = "null";
		Timestamp L_tmsENDTM  = null;
		String L_strSQLQRY ="";
		try
		{		
			L_strSQLQRY = "Select max(LW_ENDTM) LW_ENDTM from FG_LWMST where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_PRDTP = '"+LP_PRDTP+"' and";
			L_strSQLQRY += " LW_LOTNO='"+LP_LOTNO+"' and LW_RCLNO='"+LP_RCLNO+"' order by LW_ENDTM desc ";
			M_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_tmsENDTM =M_rstRSSET.getTimestamp("LW_ENDTM");
			}
			if(L_tmsENDTM!=null)
			{
				L_strENDTM = "'"+M_fmtDBDTM.format(L_tmsENDTM)+"'".toString();
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLOTETM End Time");
		}
		return L_strENDTM;
	}
	/**
	 */
	private boolean exeGINNO()  //validates Entered Gate In No.with the database.
	{
		try
		{
			
			boolean LM_INVFL = true;
			boolean L_flgRTNFL=true;
			String L_strGINNO=txtGINNO.getText().trim();
			if(L_strGINNO.length()==0)
				return true;
			if(L_flgRTNFL)
			{
				M_strSQLQRY = "Select * from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCNO = '"+L_strGINNO+"' and WB_DOCTP = '04'";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					M_rstRSSET.close();
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeGINNO");
		}
		return false;
	}
	
	/**
	 */
	private void exeAUTH_LWMST()
	{
		try
		{
			if(txtPRDTP.getText().trim().equals("01"))
			{
				M_strSQLQRY = "Select LT_PRDTP,LT_LOTNO, LT_IPRDS from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND len(rtrim(ltrim(LT_IPRDS)))=0 and LT_PRDTP = '01' and LT_CMPCD + LT_PRDTP + LT_LOTNO + LT_RCLNO in (select LW_CMPCD + LW_PRDTP + LW_LOTNO + LW_RCLNO from FG_LWMST ";
				M_strSQLQRY += " where LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"'"
						        +" and LW_RCTTP = '"+txtRCTTP.getText()+"'"
						        +" and LW_RCTNO = '"+txtRCTNO.getText()+"'"
						        +" and LW_BAGQT > 0"
						        +" and LW_STSFL <> 'X')";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				if(M_rstRSSET != null && M_rstRSSET.next())
				{
					String L_strLOTMSG = "Grade not mentioned by Oprerations for following Lots \n";
					while(true)
					{
						L_strLOTMSG = L_strLOTMSG + M_rstRSSET.getString("LT_LOTNO")+"\n";
						if(!M_rstRSSET.next())
							break;
					}
					JOptionPane.showMessageDialog(this,L_strLOTMSG+"\n Authorisation can not proceed");
					M_rstRSSET.close();
					return;
				}
			}
			/*M_strSQLQRY  ="update FG_LWMST SET ";
			M_strSQLQRY += " LW_STSFL= '2', ";
			M_strSQLQRY += " LW_TRNFL='0',";
			M_strSQLQRY += " LW_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += " LW_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
			M_strSQLQRY += " where LW_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and "
					        +" LW_RCTTP = '"+txtRCTTP.getText()+"' and "
					        +" LW_RCTNO = '"+txtRCTNO.getText()+"' and "
					        +" LW_PRDTP = '"+txtPRDTP.getText()+"' ";
			M_strSQLQRY += " and isnull(LW_STSFL,'') <> 'X'";
			*/
			//System.out.println("Before StoreProcedure");
			String L_strWRHTP=cmbWRHTP.getSelectedItem().toString();
			String L_strRCTTP=txtRCTTP.getText().trim();
			String L_strRCTNO=txtRCTNO.getText().trim();
			String L_strAUTBY = cl_dat.M_strUSRCD_pbst;
			//System.out.println("L_strWRHTP "+L_strWRHTP);
			//System.out.println("L_strRCTTP "+L_strRCTTP);
			//System.out.println("L_strRCTNO "+L_strRCTNO);
			
			calFGLWM.setString(1,cl_dat.M_strCMPCD_pbst);
			calFGLWM.setString(2,L_strWRHTP);
			calFGLWM.setString(3,L_strRCTTP);
			calFGLWM.setString(4,L_strRCTNO);
			calFGLWM.setString(5,L_strAUTBY);
			calFGLWM.executeUpdate();
			cl_dat.M_conSPDBA_pbst.commit();
			
			//System.out.println("After  StoreProcedure");
			//cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeAUTH"))
				{
					setMSG("Data Authorized successfully",'N');
				}
			}
			else
			{
				setMSG("Error In Saving Data",'E');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeAUTH_LWMST ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**
	 */
	private void exeAUTH_RCTRN()
	{
		try
		{
			/*M_strSQLQRY = "update FG_RCTRN set";
			M_strSQLQRY +=" RCT_STSFL = '2',";
			M_strSQLQRY +=" RCT_TRNFL='0',";
			M_strSQLQRY +=" RCT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY +=" RCT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
			M_strSQLQRY +=" where RCT_WRHTP = '"+cmbWRHTP.getSelectedItem().toString()+"' and "
						+" RCT_RCTTP = '"+txtRCTTP.getText()+"' and "
						+" RCT_RCTNO = '"+txtRCTNO.getText()+"' and "
						+" RCT_PRDTP = '"+txtPRDTP.getText()+"'  and isnull(RCT_STSFL,'') <> 'X'";
			System.out.println("AUTH="+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			*/
			
			
			//System.out.println("Before StoreProcedure RCTRN");
			
			String L_strWRHTP=cmbWRHTP.getSelectedItem().toString();
			String L_strRCTTP=txtRCTTP.getText().trim();
			String L_strRCTNO=txtRCTNO.getText().trim();
			String L_strAUTBY = cl_dat.M_strUSRCD_pbst;
			//System.out.println("L_strWRHTP "+L_strWRHTP);
			//System.out.println("L_strRCTTP "+L_strRCTTP);
			//System.out.println("L_strRCTNO "+L_strRCTNO);
						
			calFGRCT.setString(1,cl_dat.M_strCMPCD_pbst);
			calFGRCT.setString(2,L_strWRHTP);
			calFGRCT.setString(3,L_strRCTTP);
			calFGRCT.setString(4,L_strRCTNO);
			calFGRCT.setString(5,L_strAUTBY);
			
			calFGRCT.executeUpdate();
			cl_dat.M_conSPDBA_pbst.commit();
			if(flgRTNFL)
			{
				//System.out.println("IN Salse Return");
				
				//calPTTRN.setString(1,"01");
				//calPTTRN.setString(2,L_strRCTNO);
				//calPTTRN.executeUpdate();
				//cl_dat.M_conSPDBA_pbst.commit();
				
				//calPLTRN.setString(1,"01");
				//calPLTRN.setString(2,txtISSRF.getText().trim());
				//calPLTRN.executeUpdate();
				//System.out.println("After Sales Return");
				//cl_dat.M_conSPDBA_pbst.commit();
				
				//M_strSQLQRY = "Select distinct PT_CRDTP,PT_DOCRF from MR_PTTRN where PT_CRDTP in ('04','32') and PT_INVNO = '"+ txtISSRF.getText().trim()+"'"; 
				//ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				M_strSQLQRY = "Select ivt_invno,ivt_prdds,pt_prtnm,sum(rct_rctqt) rct_rctqt from FG_RCTRN, MR_IVTRN, CO_PTMST where RCT_RCTNO = '"+L_strRCTNO+"' and RCT_ISSRF = IVT_INVNO and RCT_CMPCD = IVT_CMPCD and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_PRTTP = 'C' and ivt_byrcd = pt_prtcd group by ivt_invno,ivt_prdds,pt_prtnm"; 
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				String L_strSLRMSG = "";
				if(L_rstRSSET != null && L_rstRSSET.next())
				{
					while(true)
					{
						L_strSLRMSG += "Party : "+L_rstRSSET.getString("PT_PRTNM")+"Inv.No. : "+L_rstRSSET.getString("IVT_INVNO")+"Grade : "+L_rstRSSET.getString("IVT_PRDDS")+"Qty : "+L_rstRSSET.getString("RCT_RCTQT")+"\n";
						if(L_rstRSSET.next())
							break;
					}
				}
				ocl_eml.sendfile("EXT","cms@spl.co.in","","Sales Return Intimation","Sales Return receipt recorded at MHD with following details \n"+L_strSLRMSG);
			}
			
			//System.out.println("After  StoreProcedure RCTRN");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeAUTH"))
				{
					setMSG("Data Authorized successfully",'N');
				}
			}
			else
			{
				setMSG("Error In Saving Data",'E');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeAUTH_RCTRN ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

	/**
	 */
	private boolean vldRCTTP()        //validates Entered Receipt Type
	{
		try
		{
			flgJBLOT = false;
			flgRTNFL = false;
			flgSTKAD = false;
			flgLTRCT= false;
			flgRPKFL = false;
			flgFRPRO = false;
			flgNOPFLG = true;
			String L_strCCSVL="";
			String L_strCHP01="";
			String L_strCHP02="";
			M_strSQLQRY = "Select * from CO_CDTRN where CMT_CGMTP='SYS' and";
			M_strSQLQRY += " CMT_CGSTP = 'FGXXRTP' and CMT_CODCD='"+txtRCTTP.getText()+"'";
			M_strSQLQRY += " and CMT_CHP02 <> '4'";
			//System.out.println("M_strSQLQRY: "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				if(M_rstRSSET.next())
				{
					L_strCCSVL = M_rstRSSET.getString("CMT_CCSVL").toString().trim();
					L_strCHP01 = M_rstRSSET.getString("CMT_CHP01").toString().trim();
					L_strCHP02 = M_rstRSSET.getString("CMT_CHP02").toString().trim();
					//System.out.println(L_strCHP01);
					//System.out.println(L_strCHP02);
					//System.out.println(L_strCCSVL);
					if(L_strCCSVL.equals("0") && L_strCHP01.equals("1"))
					{
						flgNOPFLG = false;
						flgFRPRO = true;         // Fresh Production Receipt
						if(L_strCHP01.equals("1") && L_strCHP02.equals("0"))
							flgRPKFL = true;		// Repacking Receipt
					}
					else if((L_strCCSVL.equals("1") && L_strCHP01.equals("1") && L_strCHP02.equals("1")))
					{
						flgJOBFL= true;             // Job Work Reciept(21)
					}
					else if((L_strCCSVL.equals("1") && L_strCHP01.equals("2") && L_strCHP02.equals("3")))
					{
						flgRTNFL = true;        // Sales Return Receipt (30)
					}
					else if((L_strCCSVL.equals("1") && L_strCHP01.equals("X") && L_strCHP02.equals("1")))
					{
						flgSTKAD = true;            // Stock Adjustment Receipt (50)
					}
					else if((L_strCCSVL.equals("1") && L_strCHP01.equals("1") && L_strCHP02.equals("2")))
					{
						flgLTRCT = true;                 //Job Work (Lot) Receipt(22), OutStanding Receipt (23)
					}
					return true;
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldRCTTP");
		}
		return false;
	}	

/** Initializing table editing before poulating/capturing data
 * 
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

