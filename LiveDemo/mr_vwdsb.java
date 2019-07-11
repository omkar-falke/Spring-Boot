/**/

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.io.DataOutputStream;import java.io.FileOutputStream;

public class mr_vwdsb extends JFrame implements ActionListener,MouseListener,Runnable
{
	boolean flgSLSDL_STRT = false;
	boolean flgSLSDL = false;
	int[] matchCR;
	int[] matchDBCR;
	JTabbedPane jtpMANTAB; 
	JTabbedPane jtpRCVDL; 
	JTabbedPane jtpSLSDL;
	JTabbedPane jtpIVTDL;
	JTabbedPane jtpSTRDL;
	JTabbedPane jtpCNSDL;
	JWindow wndMAINWD,wndDSPTBL;
	JDialog wndMAINDL;
	Cursor curWTSTS = new Cursor(Cursor.WAIT_CURSOR);
	Cursor curDFSTS = new Cursor(Cursor.DEFAULT_CURSOR);
	static String M_strCMPCD_pbst = "01";
	static String M_strINCHK_pbst = "";
	static String M_strCMPNM_prst = "";
	private SimpleDateFormat fmtDMYDT = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat fmtMDYDT = new SimpleDateFormat("MM/dd/yyyy");
	String strCURDT="";
	String strCURDT_DB="";
	String strENDMN="";
	String strENDMN_DB="";
	String strENDWK="";
	String strENDWK_DB="";
	String strYSTDT="";
	int intCSTIN = 100000;
	////Receivables///////////////////////////////////////////////////////////////////////
	JTable tblCATDL;    
	JTable tblZONDL;
	JTable tblDSTDL;
	JTable tblINVDL;
	mr_vwdsb_RightTableCellRenderer rightRENDERER;
	mr_vwdsb_CenterTableCellRenderer centerRENDERER;
	
	TableColumnModel tcm,tcmZON,tcmDSTDL,tcmINVDL,tcmSLSDL,tcmPRDDL_SLS,tcmGRDDL_SLS,tcmPRDDL_IVT,tcmGRDDL_IVT,tcmLOTDL_IVT,tcmSTRDL1,tcmSTRDL2,tcmSTRDL3,tcmSTRDL4,tcmCNSDL,tcmCNSDL_MN,tcmCNSDL_DL,tcmCNSDL_CP;
	
	JPanel pnlRCVDL;  
	JPanel pnlDSTDL,pnlINVDL;  
	JPanel pnlDSPREC;
	JButton btnEXIT;
	JButton btnGRAPH;
	JLabel lblMSG_RCV,lblMSG_SLS,lblMSG_IVT,lblMSG_STR,lblMSG_CNS,lblMSG_CNS1,lblMSG_CNS2,lblMSG_CNS3;
	JLabel lblDSPL_RCV,lblDSPL_SLS,lblDSPL_IVT,lblDSPL_STR,lblDSPL_CNS;
	JLabel lblCATDL;
	JLabel lblZONDL;
	double[] arrDAYCL= new double[7];
	double[] arrOSTDT= new double[7];
	double[] arrOVDDT= new double[7];
	double[] arrOVDWK= new double[7];
	double[] arrOVDMN= new double[7];
	Double dblOSTDT=0.0;
	Double dblWEST_OSTDT=0.0,dblCENTRAL_OSTDT=0.0,dblNORTH_OSTDT=0.0,dblEAST_OSTDT=0.0,dblSOUTH_OSTDT=0.0,dblEXPORT_OSTDT=0.0;

	Double dblOVDDT=0.0;
	Double dblWEST_OVDDT=0.0,dblCENTRAL_OVDDT=0.0,dblNORTH_OVDDT=0.0,dblEAST_OVDDT=0.0,dblSOUTH_OVDDT=0.0,dblEXPORT_OVDDT=0.0;

	private Vector<String> vtrDAYCL_DB=new Vector<String>();
	private Vector<String> vtrDAYCL_CR=new Vector<String>();

	private Vector<String> vtrOVDDT_DB=new Vector<String>();
	private Vector<String> vtrOVDDT_CR=new Vector<String>();

	private Vector<String> vtrOSTDT_DB=new Vector<String>();
	private Vector<String> vtrOSTDT_CR=new Vector<String>();

	private Vector<String> vtrOVDWK_DB=new Vector<String>();
	private Vector<String> vtrOVDWK_CR=new Vector<String>();

	private Vector<String> vtrOVDMN_DB=new Vector<String>();
	private Vector<String> vtrOVDMN_CR=new Vector<String>();
	
	/////////tblCATDL////////////////////
	////columns
	final int TB_CHKFL =0; 
	final int TB_DTAIL =1; 
	final int TB_BALAM =2;
	////rows
	final int TB_DAYCL =0; 
	final int TB_OSTDT =1; 
	final int TB_OVDDT =2;
	final int TB_OVDWK =3;
	final int TB_OVDMN =4;
	/////////////////////////////////////

	/////////tblZONDL////////////////////
	/////columns
	final int TB1_CHKFL =0; 
	final int TB1_DTAIL =1; 
	final int TB1_BALAM =2;
	/////rows
	final int TB1_WEST =0; 
	final int TB1_CENTRAL =1; 
	final int TB1_NORTH =2;
	final int TB1_EAST =3;
	final int TB1_SOUTH =4;
	final int TB1_EXPORT =5;

	/////////tblDSTDL////////////////////
	/////columns
	final int TB2_CHKFL =0; 
	final int TB2_DSTNM =1; 
	final int TB2_CSTNM =2; 
	final int TB2_BALAM =3;
	final int TB2_CSTCD =6;
	final int TB2_CR=4;
	final int TB2_DIFF=5;
	////////////////////////////////////////////////////////////////////////////////////////

	/////////tblINVDL////////////////////
	/////columns
	final int TB3_CHKFL =0; 
	final int TB3_DOCTP =1; 
	final int TB3_DOCNO =2; 
	final int TB3_DOCDT =3;
	final int TB3_DOCVL =4;
	final int TB3_ADJVL =5;
	final int TB3_BALAM =6;
	////////////////////////////////////////////////////////////////////////////////////////

	///////Sales Analysis//////////////////////////////////////////////////////////////////
	int intSTRMN=4,intENDMN=3;  //from 1 April to 31 March
	int curYEAR;
	int prvYEAR1;
	int prvYEAR2;
	int prvYEAR3;
	int prvYEAR4;
	JTable tblSLSDL;
	JTable tblPRDDL_SLS;
	JTable tblGRDDL_SLS;
	JPanel pnlSLSDL;
	JPanel pnlGRDDL_SLS;
	JPanel pnlPRDDL_SLS;
	private Vector<String> vtrSLSDL=new Vector<String>();
	private Hashtable<String,double[]> hstSLSDL=new Hashtable<String,double[]>();
	private Hashtable<String,String> hstPRDCD=new Hashtable<String,String>();
	private Hashtable<String,String> hstPRDDS=new Hashtable<String,String>();
	private Hashtable<String,String> hstGRDCD=new Hashtable<String,String>();
	private Hashtable<String,String> hstGRDDS=new Hashtable<String,String>();
	private Hashtable<String,String> hstDPTCD=new Hashtable<String,String>();
	/////////tblSLSDL////////////////////
	////columns
	final int TB4_CHKFL =0; 
	final int TB4_YEAR =1; 
	final int TB4_PRDQT =2; 
	final int TB4_RCLQT =3;
	final int TB4_CPTQT =4;
	final int TB4_SALQT =5;
	final int TB4_RETQT =6;

	////rows
	final int TB4_CUR =0; 
	final int TB4_PRV1 =1; 
	final int TB4_PRV2 =2; 
	final int TB4_PRV3 =3; 
	/////////////////////////////////////
	/////////tblGRDDL_SLS////////////////////
	////columns
	final int TB5_CHKFL =0; 
	final int TB5_PRDDS =1; 
	final int TB5_PRDQT =2; 
	final int TB5_RCLQT =3;
	final int TB5_CPTQT =4;
	final int TB5_SALQT =5;
	final int TB5_RETQT =6;
	/////////////////////////////////////
	/////////tblPRDDL_SLS////////////////////
	////columns
	final int TB6_CHKFL =0; 
	final int TB6_GRDDS =1; 
	final int TB6_PRDQT =2; 
	final int TB6_RCLQT =3;
	final int TB6_CPTQT =4;
	final int TB6_SALQT =5;
	final int TB6_RETQT =6;
	/////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	
	///////Inventory Details//////////////////////////////////////////////////////////////////
	JTable tblPRDDL_IVT;
	JTable tblGRDDL_IVT;
	JTable tblLOTDL_IVT;
	JPanel pnlIVTDL;
	JPanel pnlGRDDL_IVT;
	JPanel pnlLOTDL_IVT;
	Vector<String> vtrIVTDL = new  Vector<String>();
	
	/////////tblPRDDL_IVT////////////////////
	////columns
	final int TB7_CHKFL =0; 
	final int TB7_PRDDS =1; 
	final int TB7_IVTQT =2; 
	final int TB7_IVTVL =3;
	final int TB7_IVTCC =4;
	/////////////////////////////////////

	/////////tblGRDDL_IVT////////////////////
	////columns
	final int TB8_CHKFL =0; 
	final int TB8_GRDDS =1; 
	final int TB8_IVTQT =2; 
	final int TB8_IVTVL =3;
	final int TB8_IVTCC =4;
	/////////////////////////////////////
	/////////tblLOTDL_IVT////////////////////
	////columns
	final int TB9_CHKFL =0; 
	final int TB9_LOTNO =1; 
	final int TB9_LOTDT =2; 
	final int TB9_IVTQT =3;
	final int TB9_IVTVL =4;
	final int TB9_IVTCC =5;
	final int TB9_NOFDS =6;
	/////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////

	///////Stores Details//////////////////////////////////////////////////////////////////
	JTable tblSTRDL1;
	JTable tblSTRDL2;
	JTable tblSTRDL3;
	JTable tblSTRDL4;
	JPanel pnlSTRDL1,pnlSTRDL2,pnlSTRDL3,pnlSTRDL4;
	Vector<String> vtrSTRDL = new  Vector<String>();
	Hashtable<String,String> hstGRPCD=new Hashtable<String,String>();
				
	////columns (tblSTRDL1-2-3)
	final int TB_CHKFL_ST =0; 
	final int TB_GRPCD_ST =1; 
	final int TB_GRPDS_ST =2; 
	//final int TB_STKQT_ST =3;
	final int TB_IVTVL_ST =3;
	final int TB_IVTCC_ST =4;
	final int TB_NOFIT_ST =5;
	/////////////////////////////////////

	//ST_MATCD,ST_UOMCD,ST_STKQT,ST_PGRDT,NMDAYS,YCLRT,YCLVL,IVTCC
	
	////columns(tblSTRDL4)
	final int TB_CHKFL_ST1 =0; 
	final int TB_GRPCD_ST1 =1; 
	final int TB_GRPDS_ST1 =2; 
	final int TB_UOMCD_ST1 =3; 
	final int TB_STKQT_ST1 =4;
	final int TB_PGRDT_ST1 =5; 
	final int TB_NOFDS_ST1 =6; 
	final int TB_YCLRT_ST1 =7; 
	final int TB_IVTVL_ST1 =8;
	final int TB_IVTCC_ST1 =9;
	/////////////////////////////////////
	

	///////Consumptions Details//////////////////////////////////////////////////////////////////
	String[] staMONTH = {"January","February","March","April","May","June","July","August","Sepember","October","November","December"};
	int intSTRMN_JUL=7,intENDMN_JUN=6;  //from 1 july to 30 june
	int curYEAR_JJ;
	int prvYEAR1_JJ;
	int prvYEAR2_JJ;
	int prvYEAR3_JJ;
	int prvYEAR4_JJ;
	
	/////////tblCNSDL////////////////////
	////columns
	final int TB_CHKFL_CNS =0; 
	final int TB_DPTCD =1; 
	final int TB_DPTDS =2; 
	final int TB_YEAR3 =3; 
	final int TB_YEAR2 =4;
	final int TB_YEAR1 =5;
	final int TB_YEAR =6;
	
	/////////tblCNSDL_MN////////////////////
	////columns
	final int TB_CHKFL_CNS1 =0; 
	final int TB_MONTH1 =1; 
	final int TB_MONTH =2; 
	final int TB_VALUE =3; 
	
	////columns(tblCNSDL_DL)
	final int TB_CHKFL_DL =0; 
	final int TB_MATCD_DL =1; 
	final int TB_MATDS_DL =2; 
	final int TB_UOMCD_DL =3; 
	final int TB_ISSDT_DL =4;
	final int TB_ISSRT_DL =5; 
	final int TB_ISSQT_DL =6; 
	final int TB_ISSVL_DL =7; 
	
	/////////tblCNSDL_CP////////////////////
	////columns(tblCNSDL_CP)
	final int TB_CHKFL_CP =0; 
	final int TB_MATCD_CP =1; 
	final int TB_MATDS_CP =2; 
	final int TB_UOMCD_CP =3; 
	final int TB_YEAR3_QT =4; 
	final int TB_YEAR3_VL =5; 
	final int TB_YEAR2_QT =6; 
	final int TB_YEAR2_VL =7; 
	final int TB_YEAR1_QT =8; 
	final int TB_YEAR1_VL =9; 
	final int TB_YEAR_QT =10; 
	final int TB_YEAR_VL =11; 
	
	JTable tblCNSDL,tblCNSDL_MN,tblCNSDL_DL,tblCNSDL_CP;
	JPanel pnlCNSDL,pnlCNSDL1,pnlCNSDL2;
	Vector<String> vtrCNSDL = new  Vector<String>();
	Vector<String> vtrDPTDL = new  Vector<String>();
	
	/////////////////////////////////////
	
	///////////////////////////////////////////////////////////////////////////////////////
	private Thread thrDATA_RCV=new Thread(this);
	private Thread thrDATA_SLS=new Thread(this);
	private Thread thrDATA_IVT=new Thread(this);
	private Thread thrDATA_STR=new Thread(this);
	private Thread thrDATA_CNS=new Thread(this);
	mr_vwdsb()
	{
		try{
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			opnDBCON();
			jtpMANTAB = new JTabbedPane();
			jtpMANTAB.setSize(900,700);
			jtpMANTAB.setLocation(20,20);
			java.util.Date datCURDT = new java.util.Date();
			java.util.Date datTMPDT = new java.util.Date();

			strCURDT = fmtDMYDT.format(datCURDT);
			strCURDT_DB=fmtMDYDT.format(fmtDMYDT.parse(strCURDT));
			//strDBSDT = fmtMDYDT.format(datCURDT);
			java.util.Calendar calLOCAL = Calendar.getInstance();

			/////years////APR-MARCH
			if(Integer.parseInt(strCURDT.substring(3,5))>intENDMN)
				curYEAR = Integer.parseInt(strCURDT.substring(6,10))+1;
			else
				curYEAR = Integer.parseInt(strCURDT.substring(6,10));
			prvYEAR1 = curYEAR-1;
			prvYEAR2 = curYEAR-2;
			prvYEAR3 = curYEAR-3;
			prvYEAR4 = curYEAR-4;
			
			/////years////JULY-JUNE
			if(Integer.parseInt(strCURDT.substring(3,5))>intENDMN_JUN)
				curYEAR_JJ = Integer.parseInt(strCURDT.substring(6,10))+1;
			else
				curYEAR_JJ = Integer.parseInt(strCURDT.substring(6,10));
			prvYEAR1_JJ = curYEAR_JJ-1;
			prvYEAR2_JJ = curYEAR_JJ-2;
			prvYEAR3_JJ = curYEAR_JJ-3;
			prvYEAR4_JJ = curYEAR_JJ-4;
			
			////yesterday date
			calLOCAL.setTime(datCURDT);      
			calLOCAL.add(Calendar.DATE,-1);    
			datTMPDT = calLOCAL.getTime();
			strYSTDT = fmtDMYDT.format(datTMPDT);

			////end of the week
			calLOCAL.setTime(datCURDT);
			calLOCAL.add(Calendar.DAY_OF_YEAR, 8 - calLOCAL.get(Calendar.DAY_OF_WEEK));
			datTMPDT = calLOCAL.getTime();
			strENDWK = fmtDMYDT.format(datTMPDT);
			strENDWK_DB = fmtMDYDT.format(fmtDMYDT.parse(strENDWK));
			////end of the month
			calLOCAL.setTime(datCURDT);
			calLOCAL.add(Calendar.MONTH,1);
			calLOCAL.set(Calendar.DAY_OF_MONTH,1); 
			calLOCAL.add(Calendar.DATE,-1);
			datTMPDT = calLOCAL.getTime();
			strENDMN = fmtDMYDT.format(datTMPDT);
			strENDMN_DB = fmtMDYDT.format(fmtDMYDT.parse(strENDMN));
			////Receivables/////////////////
			pnlDSPREC = new JPanel();
			pnlDSPREC.removeAll();
			pnlDSPREC.setLayout(null);
			jtpRCVDL = new JTabbedPane();
			pnlRCVDL = new JPanel();
			pnlDSTDL = new JPanel();
			pnlINVDL = new JPanel();
			jtpMANTAB.add(pnlRCVDL);
			////////////////////////////////
			
			
			////Sales Analysis///////
			pnlSLSDL = new JPanel();
			pnlPRDDL_SLS = new JPanel();
			pnlGRDDL_SLS = new JPanel();
			jtpMANTAB.add(pnlSLSDL);
			jtpSLSDL = new JTabbedPane();
			//////////////////////////
			
			////Inventory Details///////
			pnlIVTDL = new JPanel();
			pnlGRDDL_IVT = new JPanel();
			pnlLOTDL_IVT = new JPanel();
			jtpMANTAB.add(pnlIVTDL);
			jtpIVTDL = new JTabbedPane();
			//////////////////////////

			////Stores Details///////
			pnlSTRDL1 = new JPanel();
			pnlSTRDL2 = new JPanel();
			pnlSTRDL3 = new JPanel();
			pnlSTRDL4 = new JPanel();
			jtpMANTAB.add(pnlSTRDL1);
			jtpSTRDL = new JTabbedPane();
			//////////////////////////

			////Consumptions Details///////
			pnlCNSDL = new JPanel();
			pnlCNSDL1 = new JPanel();
			pnlCNSDL2 = new JPanel();
			jtpCNSDL = new JTabbedPane();
			jtpMANTAB.add(pnlCNSDL);
			//////////////////////////
			
			////Display Receivables details////////////////////////////
			jtpRCVDL.add(pnlDSTDL);
			jtpRCVDL.add(pnlINVDL);
			jtpRCVDL.setSize(875,375);
			jtpRCVDL.setLocation(20,275);
			pnlRCVDL.setLayout(null);
			pnlDSTDL.setLayout(null);
			pnlINVDL.setLayout(null);
			lblDSPL_RCV = new JLabel("Amount In Lacs");
			lblDSPL_RCV.setBounds(50,1,500,20);
			lblDSPL_RCV.setForeground(Color.blue);
			pnlRCVDL.add(lblDSPL_RCV);

			lblMSG_RCV = new JLabel("");
			lblMSG_RCV.setBounds(50,25,500,20);
			lblMSG_RCV.setForeground(Color.red);
			pnlRCVDL.add(lblMSG_RCV);

			String[] L_strCOLHD = {"Flag","Category","Amount"};
			int[] L_intCOLSZ = {20,400,200};
			
			rightRENDERER=new mr_vwdsb_RightTableCellRenderer();
			centerRENDERER=new mr_vwdsb_CenterTableCellRenderer();
			
			tblCATDL = crtTBLPNL1(pnlRCVDL,L_strCOLHD,10,3,50,400,200,L_intCOLSZ,new int[]{0});	
			
			tcm=tblCATDL.getColumnModel();
			tcm.getColumn(TB_BALAM).setCellRenderer(rightRENDERER);
	        
			tblCATDL.setValueAt("Days Collection",TB_DAYCL,TB_DTAIL);
			tblCATDL.setValueAt("Outstanding As On Date",TB_OSTDT,TB_DTAIL);
			tblCATDL.setValueAt("OverDue As On Date",TB_OVDDT,TB_DTAIL);
			tblCATDL.setValueAt("OverDue For The Week",TB_OVDWK,TB_DTAIL);
			tblCATDL.setValueAt("OverDue For The Month",TB_OVDMN,TB_DTAIL);
			tblCATDL.addMouseListener(this);
			tblCATDL.setSelectionForeground(Color.red);
			
			lblCATDL = new JLabel("");
			lblCATDL.setBounds(525,25,500,20);
			lblCATDL.setForeground(Color.blue);
			pnlRCVDL.add(lblCATDL);

			String[] L_strCOLHD1 = {"Flag1","Zones","Amount"};
			int[] L_intCOLSZ1 = {20,200,200};
			tblZONDL = crtTBLPNL1(pnlRCVDL,L_strCOLHD1,10,500,50,400,200,L_intCOLSZ1,new int[]{0});	
			tblZONDL.setSelectionForeground(Color.red);
			 
			tcmZON=tblZONDL.getColumnModel();
			tcmZON.getColumn(TB1_BALAM).setCellRenderer(rightRENDERER);
	       
			
			tblZONDL.setValueAt("NORTH",TB1_NORTH,TB1_DTAIL);
			tblZONDL.setValueAt("EAST",TB1_EAST,TB1_DTAIL);
			tblZONDL.setValueAt("SOUTH",TB1_SOUTH,TB1_DTAIL);
			tblZONDL.setValueAt("WEST",TB1_WEST,TB1_DTAIL);
			tblZONDL.setValueAt("CENTRAL",TB1_CENTRAL,TB1_DTAIL);
			//tblZONDL.setValueAt("EXPORT",TB1_EXPORT,TB1_DTAIL);
			tblZONDL.addMouseListener(this);

			lblZONDL = new JLabel("");
			lblZONDL.setBounds(50,250,500,20);
			lblZONDL.setForeground(Color.blue);
			pnlRCVDL.add(lblZONDL);

			String[] L_strCOLHD2 = {"Flag","Distributor","Customer","Amt(DB)","Amt(CR)","Amt(DB-CR)","Customer Code"};
			int[] L_intCOLSZ2 = {20,300,300,250,250,250,10};
			
			tblDSTDL = crtTBLPNL1(pnlDSTDL,L_strCOLHD2,5000,3,3,875,350,L_intCOLSZ2,new int[]{0});	
			tblDSTDL.setSelectionForeground(Color.red);
			tblDSTDL.addMouseListener(this);
			tcmDSTDL=tblDSTDL.getColumnModel();
			
			 
			tcmDSTDL.getColumn(TB2_BALAM).setCellRenderer(rightRENDERER);
			tcmDSTDL.getColumn(TB2_CR).setCellRenderer(rightRENDERER);
			tcmDSTDL.getColumn(TB2_DIFF).setCellRenderer(rightRENDERER);
			tcmDSTDL.getColumn(TB2_CSTCD).setCellRenderer(centerRENDERER);
			 

			String[] L_strCOLHD3 = {"Flag","Doc Type","Doc No","Doc Dt","Doc Value","Adjusted Value","Amount"};
			int[] L_intCOLSZ3 = {20,150,150,150,150,150,150,150};
			tblINVDL = crtTBLPNL1(pnlINVDL,L_strCOLHD3,5000,3,3,875,350,L_intCOLSZ3,new int[]{0});	
			tblINVDL.setSelectionForeground(Color.red);
			
			tcmINVDL=tblINVDL.getColumnModel();
			tcmINVDL.getColumn(TB3_ADJVL).setCellRenderer(rightRENDERER);
			tcmINVDL.getColumn(TB3_BALAM).setCellRenderer(rightRENDERER);
			tcmINVDL.getColumn(TB3_DOCVL).setCellRenderer(rightRENDERER);
			
			jtpRCVDL.addTab("Distributor Details",pnlDSTDL);
			jtpRCVDL.addTab("Invoice Details",pnlINVDL);

			pnlDSPREC.add(jtpMANTAB);
			pnlRCVDL.add(jtpRCVDL);
			
			/////////////////////////////////////////////////////////////////////////////////////

			
			////Didsplay  Sales Analysis////////////////////////////////////////////////////////
			
			jtpSLSDL.add(pnlPRDDL_SLS);
			jtpSLSDL.add(pnlGRDDL_SLS);
			jtpSLSDL.setSize(875,375);
			jtpSLSDL.setLocation(20,200);
			pnlSLSDL.setLayout(null);
			jtpSLSDL.add(pnlPRDDL_SLS);
			jtpSLSDL.add(pnlGRDDL_SLS);
			
			lblDSPL_SLS = new JLabel("Year is From 1 April to 31 March");
			lblDSPL_SLS.setBounds(50,1,500,20);
			lblDSPL_SLS.setForeground(Color.blue);
			pnlSLSDL.add(lblDSPL_SLS);
			
			lblMSG_SLS = new JLabel("");
			lblMSG_SLS.setBounds(50,25,500,20);
			lblMSG_SLS.setForeground(Color.red);
			pnlSLSDL.add(lblMSG_SLS);
			
			String[] L_strCOLHD4 = {"Flag","Year","Prodn Qty","Recycled","Captive Consumption","Sales Qty","Sales Return Qty"};
			int[] L_intCOLSZ4 = {20,200,200,200,200,200,200,200};
			tblSLSDL = crtTBLPNL1(pnlSLSDL,L_strCOLHD4,5,3,50,900,140,L_intCOLSZ4,new int[]{0});	
			
			tcmSLSDL=tblSLSDL.getColumnModel();
			tcmSLSDL.getColumn(TB4_PRDQT).setCellRenderer(rightRENDERER);
			tcmSLSDL.getColumn(TB4_CPTQT).setCellRenderer(rightRENDERER);
			tcmSLSDL.getColumn(TB4_RCLQT).setCellRenderer(rightRENDERER);
			tcmSLSDL.getColumn(TB4_SALQT).setCellRenderer(rightRENDERER);
			tcmSLSDL.getColumn(TB4_RETQT).setCellRenderer(rightRENDERER);
			
			tblSLSDL.setValueAt(prvYEAR1+"-"+curYEAR,TB4_CUR,TB4_YEAR);
			tblSLSDL.setValueAt(prvYEAR2+"-"+prvYEAR1,TB4_PRV1,TB4_YEAR);
			tblSLSDL.setValueAt(prvYEAR3+"-"+prvYEAR2,TB4_PRV2,TB4_YEAR);
			tblSLSDL.setValueAt(prvYEAR4+"-"+prvYEAR3,TB4_PRV3,TB4_YEAR);
			tblSLSDL.addMouseListener(this);
			tblSLSDL.setSelectionForeground(Color.red);
			
			String[] L_strCOLHD5 = {"Flag","PRODUCT","Prodn Qty","Recycled","Captive Consumption","Sales Qty","Sales Return Qty"};
			int[] L_intCOLSZ5 = {20,200,200,200,200,200,200,200};
			tblPRDDL_SLS = crtTBLPNL1(pnlPRDDL_SLS,L_strCOLHD5,1000,3,50,900,350,L_intCOLSZ5,new int[]{0});	
			tblPRDDL_SLS.addMouseListener(this);
			
			tcmPRDDL_SLS=tblPRDDL_SLS.getColumnModel();
			tcmPRDDL_SLS.getColumn(TB5_PRDQT).setCellRenderer(rightRENDERER);
			tcmPRDDL_SLS.getColumn(TB5_CPTQT).setCellRenderer(rightRENDERER);
			tcmPRDDL_SLS.getColumn(TB5_RCLQT).setCellRenderer(rightRENDERER);
			tcmPRDDL_SLS.getColumn(TB5_SALQT).setCellRenderer(rightRENDERER);
			tcmPRDDL_SLS.getColumn(TB5_RETQT).setCellRenderer(rightRENDERER);
			tblPRDDL_SLS.setSelectionForeground(Color.red);
			
			String[] L_strCOLHD6 = {"Flag","GRADE","Prodn Qty","Recycled","Captive Consumption","Sales Qty","Sales Return Qty"};
			int[] L_intCOLSZ6 = {20,200,200,200,200,200,200,200};
			tblGRDDL_SLS = crtTBLPNL1(pnlGRDDL_SLS,L_strCOLHD6,1000,3,50,900,350,L_intCOLSZ6,new int[]{0});	
			
			tcmGRDDL_SLS=tblGRDDL_SLS.getColumnModel();
			tcmGRDDL_SLS.getColumn(TB6_PRDQT).setCellRenderer(rightRENDERER);
			tcmGRDDL_SLS.getColumn(TB6_CPTQT).setCellRenderer(rightRENDERER);
			tcmGRDDL_SLS.getColumn(TB6_RCLQT).setCellRenderer(rightRENDERER);
			tcmGRDDL_SLS.getColumn(TB6_SALQT).setCellRenderer(rightRENDERER);
			tcmGRDDL_SLS.getColumn(TB6_RETQT).setCellRenderer(rightRENDERER);
			tblGRDDL_SLS.setSelectionForeground(Color.red);
			
			jtpSLSDL.addTab("PRODUCTS",pnlPRDDL_SLS);
			jtpSLSDL.addTab("GRADES",pnlGRDDL_SLS);
			pnlSLSDL.add(jtpSLSDL);
			////////////////////////////////////////////////////////////////////////////////////
			
			////Didsplay Inventory Details////////////////////////////////////////////////////////
			
			jtpIVTDL.add(pnlGRDDL_IVT);
			jtpIVTDL.add(pnlLOTDL_IVT);
			jtpIVTDL.setSize(875,375);
			jtpIVTDL.setLocation(20,300);
			pnlIVTDL.setLayout(null);
			
			lblDSPL_IVT = new JLabel("Amount In Lacs");
			lblDSPL_IVT.setBounds(50,1,500,20);
			lblDSPL_IVT.setForeground(Color.blue);
			pnlIVTDL.add(lblDSPL_IVT);
			
			lblMSG_IVT = new JLabel("");
			lblMSG_IVT.setBounds(50,25,500,20);
			lblMSG_IVT.setForeground(Color.red);
			pnlIVTDL.add(lblMSG_IVT);
		
			String[] L_strCOLHD7 = {"Flag","PRODUCT","Inventory Qty","Inventory Value","Inventory Carrying Cost"};
			int[] L_intCOLSZ7 = {20,200,200,200,200};
			tblPRDDL_IVT = crtTBLPNL1(pnlIVTDL,L_strCOLHD7,100,3,50,900,250,L_intCOLSZ7,new int[]{0});	
			tblPRDDL_IVT.addMouseListener(this);
			tcmPRDDL_IVT=tblPRDDL_IVT.getColumnModel();
			tcmPRDDL_IVT.getColumn(TB7_IVTQT).setCellRenderer(rightRENDERER);
			tcmPRDDL_IVT.getColumn(TB7_IVTVL).setCellRenderer(rightRENDERER);
			tcmPRDDL_IVT.getColumn(TB7_IVTCC).setCellRenderer(rightRENDERER);
			tblPRDDL_IVT.addMouseListener(this);
			tblPRDDL_IVT.setSelectionForeground(Color.red);
			
			String[] L_strCOLHD8 = {"Flag","GRADE","Inventory Qty","Inventory Value","Inventory Carrying Cost"};
			int[] L_intCOLSZ8 = {20,200,200,200,200};
			tblGRDDL_IVT = crtTBLPNL1(pnlGRDDL_IVT,L_strCOLHD8,500,3,50,900,350,L_intCOLSZ8,new int[]{0});	
			tcmGRDDL_IVT=tblGRDDL_IVT.getColumnModel();
			tcmGRDDL_IVT.getColumn(TB8_IVTQT).setCellRenderer(rightRENDERER);
			tcmGRDDL_IVT.getColumn(TB8_IVTVL).setCellRenderer(rightRENDERER);
			tcmGRDDL_IVT.getColumn(TB8_IVTCC).setCellRenderer(rightRENDERER);
			tblGRDDL_IVT.addMouseListener(this);
			tblGRDDL_IVT.setSelectionForeground(Color.red);
			
			String[] L_strCOLHD9 = {"Flag","LOT NO","LOT DATE","Inventory Qty","Inventory Value","Inventory Carrying Cost","No. Of Days"};
			int[] L_intCOLSZ9 = {20,200,200,200,200,200,200};
			tblLOTDL_IVT = crtTBLPNL1(pnlLOTDL_IVT,L_strCOLHD9,1000,3,50,900,350,L_intCOLSZ9,new int[]{0});	
			tcmLOTDL_IVT=tblLOTDL_IVT.getColumnModel();
			tcmLOTDL_IVT.getColumn(TB9_IVTQT).setCellRenderer(rightRENDERER);
			tcmLOTDL_IVT.getColumn(TB9_IVTVL).setCellRenderer(rightRENDERER);
			tcmLOTDL_IVT.getColumn(TB9_IVTCC).setCellRenderer(rightRENDERER);
			tcmLOTDL_IVT.getColumn(TB9_NOFDS).setCellRenderer(rightRENDERER);
			tblLOTDL_IVT.setSelectionForeground(Color.red);
			
			jtpIVTDL.addTab("GRADES",pnlGRDDL_IVT);
			jtpIVTDL.addTab("LOTS",pnlLOTDL_IVT);
			pnlIVTDL.add(jtpIVTDL);
			////////////////////////////////////////////////////////////////////////////////////

			jtpMANTAB.addTab("Receivables",pnlRCVDL);
			jtpMANTAB.addTab("Sales Analysis",pnlSLSDL);
			jtpMANTAB.addTab("FG Inventory",pnlIVTDL);
			jtpMANTAB.addTab("Stores Inventory",pnlSTRDL1);
			jtpMANTAB.addTab("Consumptions Details",pnlCNSDL);
			
			btnEXIT = new JButton("EXIT");
			btnGRAPH = new JButton("Graph");
			btnEXIT.setBounds(925,1,75,20);
			pnlDSPREC.add(btnEXIT);
			btnGRAPH.setBounds(850,1,75,20);
			pnlDSPREC.add(btnGRAPH);
			wndMAINWD = new JWindow(this);
			wndMAINDL = new JDialog(this);
			wndMAINDL.setTitle("Supreme Petrochem Limited");
			wndMAINWD = new JWindow(wndMAINDL);
			wndMAINDL.setSize(1000,800);
			wndMAINDL.getContentPane().add(pnlDSPREC);
			wndMAINDL.toFront();
			btnEXIT.addActionListener(this);
			btnGRAPH.addActionListener(this);
			jtpMANTAB.addMouseListener(this);
						
			//////creating hashtables for product codes, product desc. and grade codes
			
			////Didsplay Stores Details////////////////////////////////////////////////////////
			
			pnlSTRDL1.setLayout(null);
			pnlSTRDL2.setLayout(null);
			pnlSTRDL3.setLayout(null);
			pnlSTRDL4.setLayout(null);
			jtpSTRDL.add(pnlSTRDL2);
			jtpSTRDL.add(pnlSTRDL3);
			jtpSTRDL.add(pnlSTRDL4);
			jtpSTRDL.setSize(875,375);
			jtpSTRDL.setLocation(20,300);
			lblDSPL_STR = new JLabel("Amount In Lacs");
			lblDSPL_STR.setBounds(50,1,500,20);
			lblDSPL_STR.setForeground(Color.blue);
			pnlSTRDL1.add(lblDSPL_STR);
			
			lblMSG_STR = new JLabel("");
			lblMSG_STR.setBounds(50,25,500,20);
			lblMSG_STR.setForeground(Color.red);
			pnlSTRDL1.add(lblMSG_STR);
			
	
			//String[] L_strCOLHD_STR = {"Flag","Group Code","Group Description","Inventory Qty","Inventory Value","Inventory Carrying Cost"};
			//int[] L_intCOLSZ_STR = {20,200,200,200,200,200};
			String[] L_strCOLHD_STR = {"Flag","Group","Description","Inventory Value","Carrying Cost","No. Of Items"};
			int[] L_intCOLSZ_STR = {20,120,500,100,100,100};
			tblSTRDL1 = crtTBLPNL1(pnlSTRDL1,L_strCOLHD_STR,200,3,50,900,250,L_intCOLSZ_STR,new int[]{0});	
			tblSTRDL1.addMouseListener(this);
			tblSTRDL1.setSelectionForeground(Color.red);

			String[] L_strCOLHD1_STR = {"Flag","Sub Group","Description","Inventory Value","Carrying Cost","No. Of Items"};
			int[] L_intCOLSZ1_STR = {20,120,500,100,100,100};
			tblSTRDL2 = crtTBLPNL1(pnlSTRDL2,L_strCOLHD1_STR,500,3,50,850,300,L_intCOLSZ1_STR,new int[]{0});	
			tblSTRDL2.addMouseListener(this);
			tblSTRDL2.setSelectionForeground(Color.red);
			
			String[] L_strCOLHD2_STR = {"Flag","Sub-Sub Group","Description","Inventory Value","Carrying Cost","No. Of Items"};
			int[] L_intCOLSZ2_STR = {20,150,500,100,100,100};
			tblSTRDL3 = crtTBLPNL1(pnlSTRDL3,L_strCOLHD2_STR,500,3,50,850,300,L_intCOLSZ2_STR,new int[]{0});	
			tblSTRDL3.addMouseListener(this);
			tblSTRDL3.setSelectionForeground(Color.red);
			
			//ST_MATCD,ST_UOMCD,ST_STKQT,ST_PGRDT,NMDAYS,YCLRT,YCLVL,IVTCC
			String[] L_strCOLHD3_STR = {"Flag","Material","Description","UOM","Quantity","Receipt Dt","No.Of Days","Yr Closing Rt","Value","Carrying Cost"};
			int[] L_intCOLSZ3_STR = {20,150,400,50,100,150,150,100,100,150};
			tblSTRDL4 = crtTBLPNL1(pnlSTRDL4,L_strCOLHD3_STR,500,3,50,850,300,L_intCOLSZ3_STR,new int[]{0});	
			tblSTRDL4.addMouseListener(this);
			tblSTRDL4.setSelectionForeground(Color.red);
			
			tcmSTRDL1=tblSTRDL1.getColumnModel();
			tcmSTRDL1.getColumn(TB_GRPCD_ST).setCellRenderer(rightRENDERER);
			//tcmSTRDL1.getColumn(TB_STKQT_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL1.getColumn(TB_IVTVL_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL1.getColumn(TB_IVTCC_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL1.getColumn(TB_NOFIT_ST).setCellRenderer(rightRENDERER);
			
			tcmSTRDL2=tblSTRDL2.getColumnModel();
			tcmSTRDL2.getColumn(TB_GRPCD_ST).setCellRenderer(rightRENDERER);
			//tcmSTRDL3.getColumn(TB_STKQT_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL2.getColumn(TB_IVTVL_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL2.getColumn(TB_IVTCC_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL2.getColumn(TB_NOFIT_ST).setCellRenderer(rightRENDERER);
			
			tcmSTRDL3=tblSTRDL3.getColumnModel();
			tcmSTRDL3.getColumn(TB_GRPCD_ST).setCellRenderer(rightRENDERER);
			//tcmSTRDL3.getColumn(TB_STKQT_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL3.getColumn(TB_IVTVL_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL3.getColumn(TB_IVTCC_ST).setCellRenderer(rightRENDERER);
			tcmSTRDL3.getColumn(TB_NOFIT_ST).setCellRenderer(rightRENDERER);
			
			tcmSTRDL4=tblSTRDL4.getColumnModel();
			tcmSTRDL4.getColumn(TB_GRPCD_ST1).setCellRenderer(rightRENDERER);
			tcmSTRDL4.getColumn(TB_STKQT_ST1).setCellRenderer(rightRENDERER);
			tcmSTRDL4.getColumn(TB_NOFDS_ST1).setCellRenderer(rightRENDERER);
			tcmSTRDL4.getColumn(TB_YCLRT_ST1).setCellRenderer(rightRENDERER);
			tcmSTRDL4.getColumn(TB_IVTVL_ST1).setCellRenderer(rightRENDERER);
			tcmSTRDL4.getColumn(TB_IVTCC_ST1).setCellRenderer(rightRENDERER);
			
			jtpSTRDL.addTab("Sub Group",pnlSTRDL2);
			jtpSTRDL.addTab("Sub-Sub Group",pnlSTRDL3);
			jtpSTRDL.addTab("Material Detail",pnlSTRDL4);
			pnlSTRDL1.add(jtpSTRDL);
			/////////////////////////////////////////////////////////////////////////////////////
			
			////Didsplay consumptions Details////////////////////////////////////////////////////////
			pnlCNSDL.setLayout(null);
			pnlCNSDL1.setLayout(null);
			pnlCNSDL2.setLayout(null);
			lblDSPL_CNS = new JLabel("Year is From 1 July to 30 June");
			lblDSPL_CNS.setBounds(50,1,500,20);
			lblDSPL_CNS.setForeground(Color.blue);
			pnlCNSDL.add(lblDSPL_CNS);
			
			lblMSG_CNS = new JLabel("Amount In Lacs");
			lblMSG_CNS.setBounds(50,25,500,20);
			lblMSG_CNS.setForeground(Color.blue);
			pnlCNSDL.add(lblMSG_CNS);

			jtpCNSDL.add(pnlCNSDL1);
			jtpCNSDL.add(pnlCNSDL2);
			jtpCNSDL.setSize(875,375);
			jtpCNSDL.setLocation(20,300);

			String[] L_strCOLHD_CNS = {"","Dept","Dept.desc",""+prvYEAR3_JJ,""+prvYEAR2_JJ,""+prvYEAR1_JJ,""+curYEAR_JJ};
			int[] L_intCOLSZ_CNS = {20,10,200,200,200,200,200};
			tblCNSDL = crtTBLPNL1(pnlCNSDL,L_strCOLHD_CNS,200,3,50,500,250,L_intCOLSZ_CNS,new int[]{0});	
			tblCNSDL.addMouseListener(this);
			tblCNSDL.setSelectionForeground(Color.red);
			
			tcmCNSDL=tblCNSDL.getColumnModel();
			tcmCNSDL.getColumn(TB_DPTCD).setCellRenderer(rightRENDERER);
			tcmCNSDL.getColumn(TB_YEAR3).setCellRenderer(rightRENDERER);
			tcmCNSDL.getColumn(TB_YEAR2).setCellRenderer(rightRENDERER);
			tcmCNSDL.getColumn(TB_YEAR1).setCellRenderer(rightRENDERER);
			tcmCNSDL.getColumn(TB_YEAR).setCellRenderer(rightRENDERER);
			
			lblMSG_CNS1 = new JLabel("");
			lblMSG_CNS1.setBounds(500,25,500,20);
			lblMSG_CNS1.setForeground(Color.blue);
			pnlCNSDL.add(lblMSG_CNS1);
			
			String[] L_strCOLHD_CNS1 = {"","","Month","Value"};
			int[] L_intCOLSZ_CNS1 = {30,30,250,250};
			tblCNSDL_MN = crtTBLPNL1(pnlCNSDL,L_strCOLHD_CNS1,15,500,50,400,250,L_intCOLSZ_CNS1,new int[]{0});	
			tblCNSDL_MN.addMouseListener(this);
			tblCNSDL_MN.setSelectionForeground(Color.red);
			tcmCNSDL_MN=tblCNSDL_MN.getColumnModel();
			tcmCNSDL_MN.getColumn(TB_VALUE).setCellRenderer(rightRENDERER);
			
			lblMSG_CNS2 = new JLabel("");
			lblMSG_CNS2.setBounds(3,25,500,20);
			lblMSG_CNS2.setForeground(Color.blue);
			pnlCNSDL1.add(lblMSG_CNS2);
			
			String[] L_strCOLHD_CNS2 = {"Flag","Material","Description","UOM","Issue Date","Rate","Quantity","Value"};
			int[] L_intCOLSZ_CNS2 = {20,150,400,50,100,150,150,150};
			tblCNSDL_DL = crtTBLPNL1(pnlCNSDL1,L_strCOLHD_CNS2,500,3,50,850,300,L_intCOLSZ_CNS2,new int[]{0});
			tblCNSDL_DL.setSelectionForeground(Color.red);
			tcmCNSDL_DL=tblCNSDL_DL.getColumnModel();
			tcmCNSDL_DL.getColumn(TB_ISSRT_DL).setCellRenderer(rightRENDERER);
			tcmCNSDL_DL.getColumn(TB_ISSQT_DL).setCellRenderer(rightRENDERER);
			tcmCNSDL_DL.getColumn(TB_ISSVL_DL).setCellRenderer(rightRENDERER);

			lblMSG_CNS3 = new JLabel("");
			lblMSG_CNS3.setBounds(3,25,500,20);
			lblMSG_CNS3.setForeground(Color.blue);
			pnlCNSDL2.add(lblMSG_CNS3);
			
			String[] L_strCOLHD_CNS3 = {"Flag","Material","Description","UOM",prvYEAR3_JJ+" QTY","VAL",prvYEAR2_JJ+" QTY","VAL",prvYEAR1_JJ+" QTY","VAL",curYEAR_JJ+" QTY","VAL"};
			int[] L_intCOLSZ_CNS3 = {20,150,400,50,150,150,150,150,150,150,150,150};
			tblCNSDL_CP = crtTBLPNL1(pnlCNSDL2,L_strCOLHD_CNS3,1500,3,50,850,300,L_intCOLSZ_CNS3,new int[]{0});
			tblCNSDL_CP.setSelectionForeground(Color.red);
			tcmCNSDL_CP=tblCNSDL_CP.getColumnModel();
			
			tcmCNSDL_CP.getColumn(TB_YEAR3_QT).setCellRenderer(rightRENDERER);
			tcmCNSDL_CP.getColumn(TB_YEAR3_VL).setCellRenderer(rightRENDERER);
			tcmCNSDL_CP.getColumn(TB_YEAR2_QT).setCellRenderer(rightRENDERER);
			tcmCNSDL_CP.getColumn(TB_YEAR2_VL).setCellRenderer(rightRENDERER);
			tcmCNSDL_CP.getColumn(TB_YEAR1_QT).setCellRenderer(rightRENDERER);
			tcmCNSDL_CP.getColumn(TB_YEAR1_VL).setCellRenderer(rightRENDERER);
			tcmCNSDL_CP.getColumn(TB_YEAR_QT).setCellRenderer(rightRENDERER);
			tcmCNSDL_CP.getColumn(TB_YEAR_VL).setCellRenderer(rightRENDERER);
			
			jtpCNSDL.addTab("Monthly Consumption",pnlCNSDL1);
			jtpCNSDL.addTab("Consumption Pattern",pnlCNSDL2);
			pnlCNSDL.add(jtpCNSDL);
			/////////////////////////////////////////////////////////////////////////////////////
			ResultSet L_rstRSSET = null;
			String strSQLQRY1;
			strSQLQRY1 = " Select substr(cmt_codcd,1,4) PRDCD,cmt_codds PRDDS from co_cdtrn";
			strSQLQRY1+= " where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR'";
			L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					if(!hstPRDCD.containsKey(L_rstRSSET.getString("PRDCD")))
						hstPRDCD.put(L_rstRSSET.getString("PRDCD"),L_rstRSSET.getString("PRDDS"));
					if(!hstPRDDS.containsKey(L_rstRSSET.getString("PRDDS")))
						hstPRDDS.put(L_rstRSSET.getString("PRDDS"),L_rstRSSET.getString("PRDCD"));
				}
			}
			
			strSQLQRY1 = " Select PR_PRDCD PRDCD,PR_PRDDS PRDDS from CO_PRMST";
			L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					if(!hstGRDCD.containsKey(L_rstRSSET.getString("PRDCD")))
						hstGRDCD.put(L_rstRSSET.getString("PRDCD"),L_rstRSSET.getString("PRDDS"));
					if(!hstGRDDS.containsKey(L_rstRSSET.getString("PRDDS")))
						hstGRDDS.put(L_rstRSSET.getString("PRDDS"),L_rstRSSET.getString("PRDCD"));
				}
			}
			L_rstRSSET.close();
			
			strSQLQRY1 = " Select CT_MATCD GRPCD,CT_MATDS GRPDS from CO_CTMST";
			strSQLQRY1+= " where ifNull(CT_STSFL,'X')<>'X'";
			strSQLQRY1+= " order by CT_MATCD";
			L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					if(!hstGRPCD.containsKey(L_rstRSSET.getString("GRPCD")))
						hstGRPCD.put(L_rstRSSET.getString("GRPCD"),L_rstRSSET.getString("GRPDS"));
				}
			}
			L_rstRSSET.close();

			strSQLQRY1 = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT'";
			L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					if(!hstDPTCD.containsKey(L_rstRSSET.getString("cmt_codcd")))
						hstDPTCD.put(L_rstRSSET.getString("cmt_codcd"),L_rstRSSET.getString("cmt_shrds"));
				}
			}
			L_rstRSSET.close();
			
			/////////////////////////////////////////////////////////////////////////////
			
			thrDATA_RCV.start();
			thrDATA_SLS.start();
			thrDATA_IVT.start();
			thrDATA_STR.start();
			thrDATA_CNS.start();

			btnGRAPH.setVisible(false);
			wndMAINDL.setVisible(true);
			lblMSG_RCV.setText("Please Wait....");
		}	
		catch(Exception L_EX)
		{
			System.out.println("inside constructor() : "+L_EX);
		}
	}


	public void run()
	{
		try
		{
			if(Thread.currentThread()==thrDATA_RCV)
			{
				String strWHRSTR="";
				// Days Collection
				for(int i=0;i<7;i++)
					arrDAYCL[i] = 0.0;
				arrDAYCL = getDSPMSG_DAYCL(TB_DAYCL,vtrDAYCL_DB,vtrDAYCL_CR);
				//strWHRSTR="";
				//arrDAYCL = getDSPMSG_DAYCL(TB_DAYCL,strWHRSTR,vtrDAYCL_DB,vtrDAYCL_CR);

				// Outstanding As On Date
				for(int i=0;i<7;i++)
					arrOSTDT[i] = 0.0;
				strWHRSTR="";
				arrOSTDT = getDSPMSG(TB_OSTDT,strWHRSTR,vtrOSTDT_DB,vtrOSTDT_CR);
				 
				// Overdue As On Date
				for(int i=0;i<7;i++)
					arrOVDDT[i] = 0.0;
				strWHRSTR=" AND (DAYS(date('"+strCURDT_DB+"'))-DAYS(PL_DUEDT))>=0";
				//strWHRSTR=" AND (DAYS(date('04/29/2009'))-DAYS(PL_DUEDT))>=0";
				arrOVDDT = getDSPMSG(TB_OVDDT,strWHRSTR,vtrOVDDT_DB,vtrOVDDT_CR);
				
				// Overdue As On Week
				
				 for(int i=0;i<7;i++)
					arrOVDWK[i] = 0.0;
				strWHRSTR=" AND (DAYS(date('"+strENDWK_DB+"'))-DAYS(PL_DUEDT))>=0";
				//strWHRSTR=" AND (DAYS(date('05/01/2009'))-DAYS(PL_DUEDT))>=0";
				arrOVDWK = getDSPMSG(TB_OVDWK,strWHRSTR,vtrOVDWK_DB,vtrOVDWK_CR);

				// Overdue As On Month
				for(int i=0;i<7;i++)
					arrOVDMN[i] = 0.0;
				strWHRSTR=" AND (DAYS(date('"+strENDMN_DB+"'))-DAYS(PL_DUEDT))>=0";
				//strWHRSTR=" AND (DAYS(date('04/30/2009'))-DAYS(PL_DUEDT))>=0";
				arrOVDMN = getDSPMSG(TB_OVDMN,strWHRSTR,vtrOVDMN_DB,vtrOVDMN_CR);
				 
			}	
			else if(Thread.currentThread()==thrDATA_SLS)
			{
				pnlSLSDL.setCursor(curWTSTS);
				ResultSet L_rstRSSET = null;
				String strSQLQRY1;
				lblMSG_SLS.setText("Please Wait....");
				//// For Prodn Qty/////////////////////////////
				//// Production receipts & Job Work Receipts
				strSQLQRY1 = " select rct_rctdt YR_DATE,lt_prdcd PRDCD,sum(rct_rctqt) QTY";
				strSQLQRY1+= " from fg_rctrn,pr_ltmst";
				strSQLQRY1+= " where rct_cmpcd=lt_cmpcd and rct_cmpcd = '"+M_strCMPCD_pbst+"' and ifnull(rct_rctqt,0)>0 and rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_rclno = lt_rclno and rct_rcttp in ('10','15','21','22','23') and year(rct_rctdt) >='"+prvYEAR4+"' and rct_stsfl='2' and rct_rctqt>0";
				strSQLQRY1+= " group by rct_rctdt,lt_prdcd order by rct_rctdt,lt_prdcd";
				//System.out.println("strSQLQRY1>>"+strSQLQRY1);
				L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
				getSLSDL(L_rstRSSET,TB4_PRDQT);
				L_rstRSSET.close();
				///////////////////////////////////////////////
				
				//// For Recycled/////////////////////////////
				//// PS captive to PS & SPS captive to SPS
				strSQLQRY1 = " select ivt_invdt YR_DATE,ivt_prdcd PRDCD,sum(ivt_invqt) QTY";
				strSQLQRY1+= " from mr_ivtrn";
				strSQLQRY1+= " where ivt_cmpcd = '"+M_strCMPCD_pbst+"' and ifnull(ivt_invqt,0)>0 and  ivt_saltp='16'   and ((ivt_byrcd = 'S7773'  and substr(ivt_prdcd,1,2) = '51') or (ivt_byrcd = 'S7771'  and substr(ivt_prdcd,1,2) in ('52','54')  and IVT_PRDCD<> '5211951680')) and year(ivt_invdt) >='"+prvYEAR4+"'";
				strSQLQRY1+= " group by ivt_invdt,ivt_prdcd order by ivt_invdt,ivt_prdcd";
				L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
				getSLSDL(L_rstRSSET,TB4_RCLQT);
				L_rstRSSET.close();
				///////////////////////////////////////////////
				
				//// For Captive Consumption/////////////////////////////
				strSQLQRY1 = " select ivt_invdt YR_DATE,ivt_prdcd PRDCD,sum(ivt_invqt) QTY";
				strSQLQRY1+= " from mr_ivtrn";
				strSQLQRY1+= " where ivt_cmpcd = '"+M_strCMPCD_pbst+"' and ifnull(ivt_invqt,0)>0 and  ivt_saltp='16'  and year(ivt_invdt) >='"+prvYEAR4+"'";
				strSQLQRY1+= " group by ivt_invdt,ivt_prdcd order by ivt_invdt,ivt_prdcd";
				L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
				getSLSDL(L_rstRSSET,TB4_CPTQT);
				L_rstRSSET.close();
				///////////////////////////////////////////////
				
				////Sale QTY/////////////////////////////
				//// Domestic despatch & Export Despatch & Deemed Export Despatch
				strSQLQRY1 = " select ivt_invdt YR_DATE,ivt_prdcd PRDCD,sum(ivt_invqt) QTY";
				strSQLQRY1+= " from mr_ivtrn";
				strSQLQRY1+= " where ivt_cmpcd = '"+M_strCMPCD_pbst+"' and ifnull(ivt_invqt,0)>0 and   ivt_saltp in ('01','03','04','05','12') and ivt_slrfl='1' and year(ivt_invdt) >='"+prvYEAR4+"' and ivt_invqt>0";
				strSQLQRY1+= " group by ivt_invdt,ivt_prdcd order by ivt_invdt,ivt_prdcd";
				L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
				getSLSDL(L_rstRSSET,TB4_SALQT);
				L_rstRSSET.close();
				///////////////////////////////////////////////
				
				////Sales Return QTY/////////////////////////////
				strSQLQRY1 = " select rct_rctdt YR_DATE,lt_prdcd PRDCD,sum(rct_rctqt) QTY";
				strSQLQRY1+= " from fg_rctrn,pr_ltmst";
				strSQLQRY1+= " where rct_cmpcd=lt_cmpcd and rct_cmpcd = '"+M_strCMPCD_pbst+"' and ifnull(rct_rctqt,0)>0 and  rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_rclno = lt_rclno and rct_rcttp = '30' and year(rct_rctdt) >='"+prvYEAR4+"' and rct_stsfl='2'";
				strSQLQRY1+= " group by rct_rctdt,lt_prdcd order by rct_rctdt,lt_prdcd";
				L_rstRSSET = cl_dat.exeSQLQRY(strSQLQRY1);
				getSLSDL(L_rstRSSET,TB4_RETQT);
				L_rstRSSET.close();
				///////////////////////////////////////////////
				lblMSG_SLS.setText("");
				flgSLSDL = true;
				pnlSLSDL.setCursor(curDFSTS);
				exeSORT(vtrSLSDL);
			}
			else if(Thread.currentThread()==thrDATA_IVT)
			{
				pnlIVTDL.setCursor(curWTSTS);
				ResultSet L_rstRSSET = null;
				String strSQLQRY1;
				String OLD_PRDCD="",NEW_PRDCD="";
				String[] staIVTDL=null;
				double L_dblIVTQT=0.0,L_dblIVTVL=0.0,L_dblIVTCC=0.0,L_dblTOTQT=0.0,L_dblTOTVL=0.0,L_dblTOTCC=0.0;
				int intROWNO=0;
				Statement stmTVTDL = cl_dat.M_conSPDBA_pbst.createStatement();
				//strSQLQRY1 = " Select ST_PRDCD PRDCD,ST_LOTNO LOTNO,LT_PSTDT LOTDT,sum(ST_DOSQT+ST_DOUQT) IVTQT,PR_AVGRT IVTCC";
				strSQLQRY1 = " Select ST_PRDCD PRDCD,ST_LOTNO LOTNO,DATE(LT_PSTDT) LOTDT,sum(ST_DOSQT+ST_DOUQT) IVTQT,(PR_AVGRT*sum(ST_DOSQT+ST_DOUQT)) IVTVL,((PR_AVGRT*sum(ST_DOSQT+ST_DOUQT))*(0.1)/365) * (days(current_date)-days(date(LT_PSTDT))) IVTCC,days(current_date)-days(date(LT_PSTDT)) NOFDS";
				strSQLQRY1+= " from CO_PRMST,FG_STMST,PR_LTMST";
				strSQLQRY1+= " where ST_PRDCD=PR_PRDCD and ST_CMPCD=LT_CMPCD and ST_PRDTP = LT_PRDTP and ST_LOTNO = LT_LOTNO and ST_RCLNO = LT_RCLNO and ST_CMPCD='01' AND (ST_DOSQT+ST_DOUQT) > 0 and date(LT_PSTDT) <=current_date";
				strSQLQRY1+= " group by ST_PRDCD,ST_LOTNO,LT_PSTDT,PR_AVGRT";
				strSQLQRY1+= " order by ST_PRDCD,ST_LOTNO,LT_PSTDT";
				//System.out.println("strSQLQRY1>>"+strSQLQRY1);
				L_rstRSSET = stmTVTDL.executeQuery(strSQLQRY1);
				if(L_rstRSSET != null)
				{
					while(L_rstRSSET.next())
					{
						vtrIVTDL.add(L_rstRSSET.getString("PRDCD")+"|"+L_rstRSSET.getString("LOTNO")+"|"+fmtDMYDT.format(L_rstRSSET.getDate("LOTDT"))+"|"+L_rstRSSET.getString("IVTQT")+"|"+L_rstRSSET.getString("IVTVL")+"|"+L_rstRSSET.getString("IVTCC")+"|"+L_rstRSSET.getString("NOFDS"));
					}
				}
				for(int i=0;i<vtrIVTDL.size();i++)
				{
					staIVTDL=null;
					staIVTDL = vtrIVTDL.get(i).replace('|','~').split("~");
					NEW_PRDCD = staIVTDL[0].substring(0,4);
					if(!NEW_PRDCD.equals(OLD_PRDCD) && !OLD_PRDCD.equals(""))
					{
						tblPRDDL_IVT.setValueAt(hstPRDCD.get(OLD_PRDCD),intROWNO,TB7_PRDDS);
						tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB7_IVTQT);
						tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB7_IVTVL);
						tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB7_IVTCC);
						L_dblTOTQT += L_dblIVTQT;
						L_dblTOTVL += L_dblIVTVL;
						L_dblTOTCC += L_dblIVTCC;
						intROWNO++;
						L_dblIVTQT=0.0;
						L_dblIVTVL=0.0;
						L_dblIVTCC=0.0;
					}
					L_dblIVTQT += Double.parseDouble(staIVTDL[3]);
					L_dblIVTVL += Double.parseDouble(staIVTDL[4]);
					L_dblIVTCC += Double.parseDouble(staIVTDL[5]);
					OLD_PRDCD = staIVTDL[0].substring(0,4);
				}
				tblPRDDL_IVT.setValueAt(hstPRDCD.get(OLD_PRDCD),intROWNO,TB7_PRDDS);
				tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB7_IVTQT);
				tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB7_IVTVL);
				tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB7_IVTCC);
				L_dblTOTQT += L_dblIVTQT;
				L_dblTOTVL += L_dblIVTVL;
				L_dblTOTCC += L_dblIVTCC;
				intROWNO+=2;
				tblPRDDL_IVT.setValueAt("TOTAL",intROWNO,TB7_PRDDS);
				tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblTOTQT,3),intROWNO,TB7_IVTQT);
				tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblTOTVL/intCSTIN,3),intROWNO,TB7_IVTVL);
				tblPRDDL_IVT.setValueAt(setNumberFormat(L_dblTOTCC/intCSTIN,3),intROWNO,TB7_IVTCC);
				pnlIVTDL.setCursor(curDFSTS);
				L_rstRSSET.close();
			}
			else if(Thread.currentThread()==thrDATA_STR)
			{
				pnlSTRDL1.setCursor(curWTSTS);
				ResultSet L_rstRSSET1 = null;
				String strSQLQRY1;
				Statement stmSTRDL = cl_dat.M_conSPDBA_pbst.createStatement();
				ResultSet M_rstRSSET1 = null;
				String OLD_GRPCD="",NEW_GRPCD="";
				String[] staSTRDL=null;
				double L_dblIVTQT=0.0,L_dblIVTVL=0.0,L_dblIVTCC=0.0,L_dblTOTQT=0.0,L_dblTOTVL=0.0,L_dblTOTCC=0.0;
				int L_intNOFIT=0,L_intTOTIT=0;
				int intROWNO=0;
				String strPGRDT="",strNMDAYS="",strIVTCC="";
				strSQLQRY1 = " SELECT ifNull(ST_MATCD,'X') GRPCD,ifNull(ST_UOMCD,'X') UOMCD,ifNull(ST_STKQT,0) STKQT,ST_PGRDT PGRDT,days(CURRENT_DATE) - days(ST_PGRDT) NMDAYS,ifNull(STP_YCLRT,0) YCLRT,(ifNull(STP_YCLRT,0)*ifNull(ST_STKQT,0)) YCLVL,((STP_YCLRT*ST_STKQT)*(0.1)/365) * (days(current_date)-days(date(ST_PGRDT))) IVTCC";
				strSQLQRY1+= " FROM MM_STMST,MM_STPRC";
				strSQLQRY1+= " WHERE ST_CMPCD=STP_CMPCD and ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND ST_CMPCD='"+M_strCMPCD_pbst+"' AND ifnull(ST_STKQT,0) > 0 AND ST_STRTP in ('01','07','08')";
				strSQLQRY1+= " group BY ST_MATCD,ST_MATDS,ST_UOMCD,ST_STKQT,ST_PGRDT,STP_YCLRT";
				strSQLQRY1+= " ORDER BY ST_MATCD";
				//System.out.println("strSQLQRY1>>"+strSQLQRY1);
				M_rstRSSET1 = stmSTRDL.executeQuery(strSQLQRY1);
				if(M_rstRSSET1 != null)
				{
					while(M_rstRSSET1.next())
					{
						try{
						if(M_rstRSSET1.getDate("PGRDT") == null)
							strPGRDT="X";
						else
							strPGRDT=fmtDMYDT.format(M_rstRSSET1.getDate("PGRDT"));
						}catch(Exception E){System.out.println("inside run_in()"+E);}
						if(M_rstRSSET1.getString("NMDAYS") == null)
							strNMDAYS="X";
						else
							strNMDAYS=M_rstRSSET1.getString("NMDAYS");
						if(M_rstRSSET1.getString("IVTCC") == null)
							strIVTCC="0";
						else
							strIVTCC=M_rstRSSET1.getString("IVTCC");
						vtrSTRDL.add(M_rstRSSET1.getString("GRPCD")+"|"+M_rstRSSET1.getString("UOMCD")+"|"+M_rstRSSET1.getString("STKQT")+"|"+strPGRDT+"|"+strNMDAYS+"|"+M_rstRSSET1.getString("YCLRT")+"|"+M_rstRSSET1.getString("YCLVL")+"|"+strIVTCC);
					}
				}
				for(int i=0;i<vtrSTRDL.size();i++)
				{
					staSTRDL=null;
					staSTRDL = vtrSTRDL.get(i).replace('|','~').split("~");
					NEW_GRPCD = staSTRDL[0].substring(0,2);
					if(!NEW_GRPCD.equals(OLD_GRPCD) && !OLD_GRPCD.equals(""))
					{
						tblSTRDL1.setValueAt(OLD_GRPCD,intROWNO,TB_GRPCD_ST);
						if(hstGRPCD.containsKey(OLD_GRPCD+"0000000A"))
							tblSTRDL1.setValueAt(hstGRPCD.get(OLD_GRPCD+"0000000A"),intROWNO,TB_GRPDS_ST);
						//tblSTRDL1.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB_STKQT_ST);
						tblSTRDL1.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB_IVTVL_ST);
						tblSTRDL1.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB_IVTCC_ST);
						tblSTRDL1.setValueAt(L_intNOFIT,intROWNO,TB_NOFIT_ST);
						//L_dblTOTQT += L_dblIVTQT;
						L_dblTOTVL += L_dblIVTVL;
						L_dblTOTCC += L_dblIVTCC;
						L_intTOTIT += L_intNOFIT;
						intROWNO++;
						L_dblIVTQT=0.0;
						L_dblIVTVL=0.0;
						L_dblIVTCC=0.0;
						L_intNOFIT = 0;
					}
					L_dblIVTQT += Double.parseDouble(staSTRDL[2]);
					L_dblIVTVL += Double.parseDouble(staSTRDL[6]);
					L_dblIVTCC += Double.parseDouble(staSTRDL[7]);
					L_intNOFIT++;
					OLD_GRPCD = staSTRDL[0].substring(0,2);
				}
				tblSTRDL1.setValueAt(OLD_GRPCD,intROWNO,TB_GRPCD_ST);
				if(hstGRPCD.containsKey(OLD_GRPCD+"0000000A"))
					tblSTRDL1.setValueAt(hstGRPCD.get(OLD_GRPCD+"0000000A"),intROWNO,TB_GRPDS_ST);
				//tblSTRDL1.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB_STKQT_ST);
				tblSTRDL1.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB_IVTVL_ST);
				tblSTRDL1.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB_IVTCC_ST);
				tblSTRDL1.setValueAt(L_intNOFIT,intROWNO,TB_NOFIT_ST);
				//L_dblTOTQT += L_dblIVTQT;
				L_dblTOTVL += L_dblIVTVL;
				L_dblTOTCC += L_dblIVTCC;
				L_intTOTIT += L_intNOFIT;
				intROWNO+=2;
				tblSTRDL1.setValueAt("TOTAL",intROWNO,TB_GRPDS_ST);
				//tblSTRDL1.setValueAt(setNumberFormat(L_dblTOTQT,3),intROWNO,TB_STKQT_ST);
				tblSTRDL1.setValueAt(setNumberFormat(L_dblTOTVL/intCSTIN,3),intROWNO,TB_IVTVL_ST);
				tblSTRDL1.setValueAt(setNumberFormat(L_dblTOTCC/intCSTIN,3),intROWNO,TB_IVTCC_ST);
				tblSTRDL1.setValueAt(L_intTOTIT,intROWNO,TB_NOFIT_ST);
				pnlSTRDL1.setCursor(curDFSTS);
				M_rstRSSET1.close();
			}
			else if(Thread.currentThread()==thrDATA_CNS)
			{
				String strSQLQRY1="";
				ResultSet M_rstRSSET1 = null;
				Statement stmCNSDL = cl_dat.M_conSPDBA_pbst.createStatement();
				String OLD_GRPCD="",NEW_GRPCD="";
				String[] staSTRDL=null;
				double L_dblIVTQT=0.0,L_dblIVTVL=0.0,L_dblIVTCC=0.0,L_dblTOTQT=0.0,L_dblTOTVL=0.0,L_dblTOTCC=0.0;
				int intROWNO=0;
				String strISSDT="",strUOMCD="";
				strSQLQRY1 = " Select IS_DPTCD DPTCD,IS_MATCD MATCD,CT_UOMCD UOMCD,IS_ISSDT ISSDT,sum(ifNull(IS_ISSQT,0)) ISSQT,ifNull(IS_ISSRT,0) ISSRT,sum(ifNull(IS_ISSVL,0)) ISSVL";
				strSQLQRY1+= " from MM_ISMST,CO_CTMST";
				strSQLQRY1+= " where IS_CMPCD='01' AND IS_STRTP = '01' and IS_ISSTP ='01' AND  year(IS_ISSDT) >= '"+prvYEAR4_JJ+"' and CT_MATCD=IS_MATCD and is_issqt >0 and ifnull(is_stsfl,'X')<>'X' and IS_ISSDT is not null";
				strSQLQRY1+= " group by IS_MATCD,CT_UOMCD,IS_DPTCD,IS_ISSDT,IS_ISSRT";
				strSQLQRY1+= " order by IS_ISSDT,IS_DPTCD,IS_MATCD";
				System.out.println("strSQLQRY1>>"+strSQLQRY1);
				M_rstRSSET1 = stmCNSDL.executeQuery(strSQLQRY1);
				if(M_rstRSSET1 != null)
				{
					while(M_rstRSSET1.next())
					{
						int L_intYEAR=0;

						strISSDT=M_rstRSSET1.getString("ISSDT");
							//strISSDT=fmtDMYDT.format(M_rstRSSET1.getDate("ISSDT"));

						if(M_rstRSSET1.getString("UOMCD") == null)
							strUOMCD="X";
						else
							strUOMCD=M_rstRSSET1.getString("UOMCD");
						
						
						if(Integer.parseInt(strISSDT.substring(3,5))>intENDMN_JUN)
						{
							L_intYEAR = Integer.parseInt("20"+strISSDT.substring(6,8))+1;
						}
						else
						{
							L_intYEAR = Integer.parseInt("20"+strISSDT.substring(6,8));
						}
						vtrCNSDL.add(M_rstRSSET1.getString("DPTCD")+"|"+L_intYEAR+"|"+M_rstRSSET1.getString("MATCD")+"|"+strUOMCD+"|"+strISSDT+"|"+M_rstRSSET1.getString("ISSQT")+"|"+M_rstRSSET1.getString("ISSRT")+"|"+M_rstRSSET1.getString("ISSVL"));
						if(!vtrDPTDL.contains(M_rstRSSET1.getString("DPTCD")))
							vtrDPTDL.add(M_rstRSSET1.getString("DPTCD"));
						exeSORT(vtrDPTDL);
					}
				}
				M_rstRSSET1.close();
				System.out.println("hstDPTCD>>"+hstDPTCD);
				for(int i=0;i<vtrDPTDL.size();i++)
				{
					tblCNSDL.setValueAt(vtrDPTDL.get(i),i,TB_DPTCD);
					if(hstDPTCD.containsKey(vtrDPTDL.get(i)))
						tblCNSDL.setValueAt(hstDPTCD.get(vtrDPTDL.get(i)),i,TB_DPTDS);
					else
						tblCNSDL.setValueAt("",i,TB_DPTDS);
				}
				
				for(int i=0;i<tblCNSDL.getRowCount();i++)
					for(int j=3;j<tblCNSDL.getColumnCount();j++)
				{
					if(tblCNSDL.getValueAt(i,TB_DPTCD).toString().length()>0)
					setTBLCNS(i,j);
				}
			}
		}
		catch(Exception E)
		{
			System.out.println("inside run() : "+E);
		}
	}	
	
	/**method to display component on screen*/
	private void setTBLCNS(int LP_ROW,int LP_COL)
	{
		try
		{
			double L_TOTVL = 0;
			String[] staCNSDL=null;
	
			for(int i=0;i<vtrCNSDL.size();i++)
			{
				staCNSDL=null;
				staCNSDL = vtrCNSDL.get(i).replace('|','~').split("~");
				if(staCNSDL[0].equals(tblCNSDL.getValueAt(LP_ROW,TB_DPTCD))
				&& staCNSDL[1].equals(tblCNSDL.getColumnName(LP_COL)) )
				{
					L_TOTVL+=Double.parseDouble(staCNSDL[7]);
				}
			}
			tblCNSDL.setValueAt(setNumberFormat(L_TOTVL/intCSTIN,3),LP_ROW,LP_COL);
		}
		catch(Exception L_EX)
		{
			System.out.println("getSLSDL(): "+L_EX);
		}
	}

	/**method to display component on screen*/
	private void getSLSDL(ResultSet LP_RSSET,int LP_COL)
	{
		double dblCUR=0,dblPRV1=0,dblPRV2=0,dblPRV3=0;
		String L_strDATE = "";
		int L_intYEAR = 0;
		try
		{
			if(LP_RSSET!=null)
			{
				while(LP_RSSET.next())
				{
					try{
					L_strDATE = fmtDMYDT.format(LP_RSSET.getDate("YR_DATE"));
					}catch(Exception E){System.out.println("getSLSDL_in"+E);}
					L_intYEAR=0;
					if(Integer.parseInt(L_strDATE.substring(3,5))>intENDMN)
					{
						L_intYEAR = Integer.parseInt(L_strDATE.substring(6,10))+1;
						//System.out.println(L_strDATE+" "+L_intYEAR);
					}
					else
					{
						L_intYEAR = Integer.parseInt(L_strDATE.substring(6,10));
						//System.out.println(L_strDATE+" "+L_intYEAR);
					}
					
					if(hstSLSDL.containsKey(L_intYEAR+"|"+LP_RSSET.getString("PRDCD")))
					{
						double[] L_dblTEMP = hstSLSDL.get(L_intYEAR+"|"+LP_RSSET.getString("PRDCD"));
						L_dblTEMP[LP_COL-2]=L_dblTEMP[LP_COL-2]+LP_RSSET.getDouble("QTY");
						hstSLSDL.put(L_intYEAR+"|"+LP_RSSET.getString("PRDCD"),L_dblTEMP);
					}
					else
					{
						double[] L_dblTEMP={0,0,0,0,0};
						L_dblTEMP[LP_COL-2]=LP_RSSET.getDouble("QTY");
						hstSLSDL.put(L_intYEAR+"|"+LP_RSSET.getString("PRDCD"),L_dblTEMP);
					}
					if(!vtrSLSDL.contains(L_intYEAR+"|"+LP_RSSET.getString("PRDCD")))
						vtrSLSDL.add(L_intYEAR+"|"+LP_RSSET.getString("PRDCD"));
					if(L_intYEAR == curYEAR)
						dblCUR += LP_RSSET.getDouble("QTY");
					if(L_intYEAR == prvYEAR1)
						dblPRV1 += LP_RSSET.getDouble("QTY");
					if(L_intYEAR == prvYEAR2)
						dblPRV2 += LP_RSSET.getDouble("QTY");
					if(L_intYEAR == prvYEAR3)
						dblPRV3 += LP_RSSET.getDouble("QTY");
				}
			}
			tblSLSDL.setValueAt(setNumberFormat(dblCUR,3),TB4_CUR,LP_COL);
			tblSLSDL.setValueAt(setNumberFormat(dblPRV1,3),TB4_PRV1,LP_COL);
			tblSLSDL.setValueAt(setNumberFormat(dblPRV2,3),TB4_PRV2,LP_COL);
			tblSLSDL.setValueAt(setNumberFormat(dblPRV3,3),TB4_PRV3,LP_COL);
			LP_RSSET.close();
		}
		catch(Exception L_EX){
			System.out.println("getSLSDL(): "+L_EX);
		}
	}

	/**method to display component on screen*/
	private void getPRDDL_SLS(String LP_YEAR)
	{
		try
		{
			int intROW = 0;
			int i=0;
			Double L_dblPRDQT=0.0,L_dblSALQT=0.0,L_dblRETQT=0.0,L_dblCPTQT=0.0,L_dblRCLQT=0.0;
			Double L_dblPRDTT=0.0,L_dblSALTT=0.0,L_dblRETTT=0.0,L_dblCPTTT=0.0,L_dblRCLTT=0.0;
			String L_strOLD_PRDCD="";
			String L_strNEW_PRDCD="";
			for(i=0;i<vtrSLSDL.size();i++)
			{
				if(vtrSLSDL.get(i).toString().substring(0,4).equals(LP_YEAR))
				{
					L_strNEW_PRDCD=vtrSLSDL.get(i).toString().substring(5,9);
					if(!L_strNEW_PRDCD.equals(L_strOLD_PRDCD) && !L_strOLD_PRDCD.equals(""))
					{
						tblPRDDL_SLS.setValueAt(hstPRDCD.get(L_strOLD_PRDCD),intROW,TB5_PRDDS);
						tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblPRDQT,3),intROW,TB5_PRDQT);
						L_dblPRDTT+=L_dblPRDQT;
						tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblCPTQT,3),intROW,TB5_CPTQT);
						L_dblCPTTT+=L_dblCPTQT;
						tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblRCLQT,3),intROW,TB5_RCLQT);
						L_dblRCLTT+=L_dblRCLQT;
						tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblSALQT,3),intROW,TB5_SALQT);
						L_dblSALTT+=L_dblSALQT;
						tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblRETQT,3),intROW,TB5_RETQT);
						L_dblRETTT+=L_dblRETQT;
						L_dblPRDQT=0.0;L_dblSALQT=0.0;L_dblRETQT=0.0;L_dblCPTQT=0.0;L_dblRCLQT=0.0;
						intROW++;
					}
					L_strOLD_PRDCD = L_strNEW_PRDCD;
					double[] L_TEMP = hstSLSDL.get(vtrSLSDL.get(i).toString());
					L_dblPRDQT+=L_TEMP[0];
					L_dblCPTQT+=L_TEMP[1];
					L_dblRCLQT+=L_TEMP[2];
					L_dblSALQT+=L_TEMP[3];
					L_dblRETQT+=L_TEMP[4];
				}
			}
			
			tblPRDDL_SLS.setValueAt(hstPRDCD.get(L_strOLD_PRDCD),intROW,TB5_PRDDS);
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblPRDQT,3),intROW,TB5_PRDQT);
			L_dblPRDTT+=L_dblPRDQT;
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblCPTQT,3),intROW,TB5_CPTQT);
			L_dblCPTTT+=L_dblCPTQT;
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblRCLQT,3),intROW,TB5_RCLQT);
			L_dblRCLTT+=L_dblRCLQT;
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblSALQT,3),intROW,TB5_SALQT);
			L_dblSALTT+=L_dblSALQT;
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblRETQT,3),intROW,TB5_RETQT);
			L_dblRETTT+=L_dblRETQT;
			L_dblPRDQT=0.0;L_dblSALQT=0.0;L_dblRETQT=0.0;L_dblCPTQT=0.0;L_dblRCLQT=0.0;
			intROW++;
						
			intROW++;
			tblPRDDL_SLS.setValueAt("TOTAL",intROW,TB5_PRDDS);
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblPRDTT,3),intROW,TB5_PRDQT);
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblCPTTT,3),intROW,TB5_CPTQT);
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblRCLTT,3),intROW,TB5_RCLQT);
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblSALTT,3),intROW,TB5_SALQT);
			tblPRDDL_SLS.setValueAt(setNumberFormat(L_dblRETTT,3),intROW,TB5_RETQT);
		}
		catch(Exception L_EX){
			System.out.println("getPRDDL_SLS(): "+L_EX);
		}
	}
	
	
	private void getGRDDL_SLS(String LP_YEAR,String LP_PRDDS)
	{
		try
		{
			int intROW = 0;
			int i=0;
			Double L_dblGRDTT=0.0,L_dblSALTT=0.0,L_dblRETTT=0.0,L_dblCPTTT=0.0,L_dblRCLTT=0.0;
			for(i=0;i<vtrSLSDL.size();i++)
			{
				if(vtrSLSDL.get(i).toString().substring(0,4).equals(LP_YEAR) 
				&& vtrSLSDL.get(i).toString().substring(5,9).equals(hstPRDDS.get(LP_PRDDS)) )
				{
					double[] L_TEMP = hstSLSDL.get(vtrSLSDL.get(i).toString());
					
					//tblGRDDL_SLS.setValueAt(vtrSLSDL.get(i).toString().substring(5,15),intROW,TB6_GRDDS);
					tblGRDDL_SLS.setValueAt(hstGRDCD.get(vtrSLSDL.get(i).toString().substring(5,15)),intROW,TB6_GRDDS);
					tblGRDDL_SLS.setValueAt(setNumberFormat(L_TEMP[0],3),intROW,TB6_PRDQT);
					L_dblGRDTT+=L_TEMP[0];
					tblGRDDL_SLS.setValueAt(setNumberFormat(L_TEMP[1],3),intROW,TB6_CPTQT);
					L_dblCPTTT+=L_TEMP[1];
					tblGRDDL_SLS.setValueAt(setNumberFormat(L_TEMP[2],3),intROW,TB6_RCLQT);
					L_dblRCLTT+=L_TEMP[2];
					tblGRDDL_SLS.setValueAt(setNumberFormat(L_TEMP[3],3),intROW,TB6_SALQT);
					L_dblSALTT+=L_TEMP[3];
					tblGRDDL_SLS.setValueAt(setNumberFormat(L_TEMP[4],3),intROW,TB6_RETQT);
					L_dblRETTT+=L_TEMP[4];
					intROW++;
				}
			}
			intROW++;
			tblGRDDL_SLS.setValueAt("TOTAL",intROW,TB6_GRDDS);
			tblGRDDL_SLS.setValueAt(setNumberFormat(L_dblGRDTT,3),intROW,TB6_PRDQT);
			tblGRDDL_SLS.setValueAt(setNumberFormat(L_dblCPTTT,3),intROW,TB6_CPTQT);
			tblGRDDL_SLS.setValueAt(setNumberFormat(L_dblRCLTT,3),intROW,TB6_RCLQT);
			tblGRDDL_SLS.setValueAt(setNumberFormat(L_dblSALTT,3),intROW,TB6_SALQT);
			tblGRDDL_SLS.setValueAt(setNumberFormat(L_dblRETTT,3),intROW,TB6_RETQT);
		}
		catch(Exception L_EX){
			System.out.println("getGRDDL_SLS(): "+L_EX);
		}
	}
	
	
	/**method to display component on screen*/
	private double[] getDSPMSG_DAYCL(int LP_ROWNO,Vector<String> LP_VTR_DB,Vector<String> LP_VTR_CR)
	{
		String strSQLQRY1;
		ResultSet rstRSSET1_DAY=null;
		double[] L_ARRDBL = new double[7];
		try
		{
			for(int i=0;i<7;i++)
				L_ARRDBL[i]=0.0;
			//strSQLQRY1 = " SELECT D.PT_PRTNM DSTNM,C.PT_PRTNM CSTNM,C.PT_ZONCD,(ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) BALAM FROM MR_PLTRN,CO_PTMST C,CO_PTMST D";
			//strSQLQRY1+= " where D.PT_PRTTP='D' and D.PT_PRTCD=C.PT_DSRCD";
			//strSQLQRY1+= " and (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND C.PT_PRTTP=PL_PRTTP AND C.PT_PRTCD=PL_PRTCD AND  PL_CMPCD='01' AND (PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE '3%') AND (DAYS(date('"+fmtMDYDT.format(fmtDMYDT.parse(strCURDT))+"'))-DAYS(PL_DUEDT))>0";
			//strSQLQRY1+= " order by D.PT_PRTNM,C.PT_PRTNM";

			strSQLQRY1 = " SELECT D.PT_PRTNM DSTNM,C.PT_PRTNM CSTNM,C.PT_ZONCD,sum(PR_RCTVL) BALAM,D.PT_PRTCD DSTCD,C.PT_PRTCD CSTCD";
			strSQLQRY1+= " FROM MR_PRTRN,CO_PTMST C,CO_PTMST D";
			strSQLQRY1+= " where D.PT_PRTTP='D' and D.PT_PRTCD=C.PT_DSRCD and ifnull(PR_RCTVL,0)  >0 AND C.PT_PRTTP = 'C' and  C.PT_PRTTP=PR_PRTTP AND C.PT_PRTCD=PR_PRTCD AND  PR_CMPCD='"+M_strCMPCD_pbst+"' AND C.PT_ZONCD  not in ('11','51','52','99')";
			strSQLQRY1+= " AND PR_DOCDT BETWEEN '"+fmtMDYDT.format(fmtDMYDT.parse(strYSTDT))+"' AND '"+fmtMDYDT.format(fmtDMYDT.parse(strYSTDT))+"'   AND  ifnull(PR_STSFL,'') <> 'X'";
			strSQLQRY1+= " group by D.PT_PRTNM,C.PT_PRTNM,C.PT_ZONCD,D.PT_PRTCD,C.PT_PRTCD order by D.PT_PRTNM,C.PT_PRTNM, C.PT_ZONCD";

			//System.out.println("getDSPMSG_DAYCL>>>>"+strSQLQRY1);
			rstRSSET1_DAY = cl_dat.exeSQLQRY1(strSQLQRY1);
			if(rstRSSET1_DAY!=null)
			{
				while(rstRSSET1_DAY.next())
				{
					LP_VTR_DB.add(rstRSSET1_DAY.getString("PT_ZONCD")+"|"+rstRSSET1_DAY.getString("DSTNM")+"|"+rstRSSET1_DAY.getString("CSTNM")+"|"+rstRSSET1_DAY.getString("BALAM")+"|"+rstRSSET1_DAY.getString("CSTCD"));
					L_ARRDBL[0]+=rstRSSET1_DAY.getDouble("BALAM");
					if(rstRSSET1_DAY.getString("PT_ZONCD").equals("02"))
						L_ARRDBL[1]+=rstRSSET1_DAY.getDouble("BALAM");
					else if(rstRSSET1_DAY.getString("PT_ZONCD").equals("03"))
						L_ARRDBL[2]+=rstRSSET1_DAY.getDouble("BALAM");
					else if(rstRSSET1_DAY.getString("PT_ZONCD").equals("04"))
						L_ARRDBL[3]+=rstRSSET1_DAY.getDouble("BALAM");
					else if(rstRSSET1_DAY.getString("PT_ZONCD").equals("06"))
						L_ARRDBL[4]+=rstRSSET1_DAY.getDouble("BALAM");
					else if(rstRSSET1_DAY.getString("PT_ZONCD").equals("07"))
						L_ARRDBL[5]+=rstRSSET1_DAY.getDouble("BALAM");
					else if(rstRSSET1_DAY.getString("PT_ZONCD").equals("11"))
						L_ARRDBL[6]+=rstRSSET1_DAY.getDouble("BALAM");
				}
			}

			tblCATDL.setValueAt(String.valueOf(setNumberFormat((L_ARRDBL[0]/intCSTIN),3)),LP_ROWNO,TB_BALAM);
			lblMSG_RCV.setText("");
			rstRSSET1_DAY.close();
			return L_ARRDBL;
		}
		catch(Exception L_EX){
			System.out.println("getDSPMSG_DAYCL(): "+L_EX);
		}
		return L_ARRDBL;
	}


	/**method to display component on screen*/
	private double[] getDSPMSG(int LP_ROWNO,String LP_WHRSTR,Vector<String> LP_VTR_DB,Vector<String> LP_VTR_CR)
	{
		String strSQLQRY1;
		ResultSet rstRSSET1_DSP;
		double[] L_ARRDBL = new double[7];
		try
		{
			for(int i=0;i<7;i++)
				L_ARRDBL[i]=0.0;

			// previous qry changed on 12/01
			//strSQLQRY1 = " SELECT D.PT_PRTNM DSTNM,C.PT_PRTNM CSTNM,C.PT_ZONCD,sum((ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0))) BALAM,D.PT_PRTCD DSTCD,C.PT_PRTCD CSTCD";
			//strSQLQRY1+= " FROM MR_PLTRN,CO_PTMST C,CO_PTMST D ";
			//strSQLQRY1+= " where D.PT_PRTTP='D' and D.PT_PRTCD=C.PT_DSRCD";
			//strSQLQRY1+= " and (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND C.PT_PRTTP = 'C' and C.PT_PRTTP=PL_PRTTP AND C.PT_PRTCD=PL_PRTCD AND  PL_CMPCD='"+M_strCMPCD_pbst+"' AND C.PT_ZONCD not in ('11','51','52','99') AND (PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE '3%')"+LP_WHRSTR;
			//strSQLQRY1+= " group by D.PT_PRTNM,C.PT_PRTNM,C.PT_ZONCD,D.PT_PRTCD,C.PT_PRTCD order by D.PT_PRTNM,C.PT_PRTNM,C.PT_ZONCD";
//			SELECT  B.PT_ZONCD,C.CMT_CODDS ZONDS,A.PT_DSRCD,B.PT_PRTNM DSRNM,PL_PRTCD,A.PT_PRTNM PRTNM, D.CMT_CHP01 DOCTP1 , sum(PL_DOCVL) PL_DOCVL,sum(ifnull(PL_ADJVL,0)) PL_ADJVL,sum(PL_DOCVL-ifnull(PL_ADJVL,0)) BALVL  FROM MR_PLTRN, CO_PTMST A,CO_PTMST B,CO_CDTRN C,  CO_CDTRN D WHERE PL_PRTTP='C' and  PL_PRTTP=A.PT_PRTTP AND  PL_PRTCD=A.PT_PRTCD AND B.PT_PRTTP='D'  AND A.PT_DSRCD=B.PT_PRTCD  AND PL_CMPCD='01' AND PL_STSFL != 'X' AND  PL_SBSCD NOT IN ('111200') AND  C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MR00ZON' AND B.PT_ZONCD=C.CMT_CODCD  AND (D.CMT_CGMTP='D01' OR D.CMT_CGMTP = 'SYS') and d.cmt_chp01 = 'DB' AND ((D.CMT_CGSTP='MRXXPTT' and substr(D.CMT_CODCD,1,1) in ('0','9') AND SUBSTR(D.CMT_CODCD,2,2)=PL_DOCTP) OR (D.CMT_CGSTP='MRXXOCN' AND D.CMT_CODCD=PL_DOCTP)) AND ((PL_DOCVL-ifnull(PL_ADJVL,0)) > 0 OR A.PT_YOPVL > 0) AND (PL_DOCDT >= '01/07/2006' OR SUBSTR(PL_DOCNO,4,5)='77777') AND ifnull(PL_ADJVL,0) <= PL_DOCVL AND LENGTH(TRIM(IFNULL(PL_DOCNO,''))) = 8 AND PL_PRTCD='V0215' AND (DAYS(date('01/12/2009'))-DAYS(PL_DUEDT))>=0 group by B.PT_ZONCD,C.CMT_CODDS ,A.PT_DSRCD,B.PT_PRTNM,PL_PRTCD,A.PT_PRTNM, D.CMT_CHP01 ORDER BY C.CMT_CODDS,B.PT_PRTNM,A.PT_PRTNM,D.CMT_CHP01 ;
			
			strSQLQRY1 = " SELECT  B.PT_PRTNM DSTNM,A.PT_PRTNM CSTNM,B.PT_ZONCD,sum((ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0))) BALAM,B.PT_PRTCD DSTCD,A.PT_PRTCD CSTCD";
			strSQLQRY1+= " FROM MR_PLTRN,CO_PTMST A,CO_PTMST B,CO_CDTRN C,CO_CDTRN D";
			strSQLQRY1+= " WHERE PL_PRTTP='C' and PL_PRTTP=A.PT_PRTTP AND  PL_PRTCD=A.PT_PRTCD AND B.PT_PRTTP='D'  AND A.PT_DSRCD=B.PT_PRTCD  AND PL_CMPCD='01' AND PL_STSFL != 'X' AND  PL_SBSCD NOT IN ('111200') AND  C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MR00ZON' AND B.PT_ZONCD=C.CMT_CODCD  AND (D.CMT_CGMTP='D01' OR D.CMT_CGMTP = 'SYS')";
			strSQLQRY1+= " and d.cmt_chp01 = 'DB' AND ((D.CMT_CGSTP='MRXXPTT' and substr(D.CMT_CODCD,1,1) in ('0','9') AND SUBSTR(D.CMT_CODCD,2,2)=PL_DOCTP) OR (D.CMT_CGSTP='MRXXOCN' AND D.CMT_CODCD=PL_DOCTP)) AND ((PL_DOCVL-ifnull(PL_ADJVL,0)) > 0 OR A.PT_YOPVL > 0) AND (PL_DOCDT >= '01/07/2006' OR SUBSTR(PL_DOCNO,4,5)='77777')";
			strSQLQRY1+= " AND ifnull(PL_ADJVL,0) <= PL_DOCVL AND LENGTH(TRIM(IFNULL(PL_DOCNO,''))) = 8"+LP_WHRSTR;
			strSQLQRY1+= " group by B.PT_PRTNM,A.PT_PRTNM,B.PT_ZONCD,B.PT_PRTCD,A.PT_PRTCD order by B.PT_PRTNM,A.PT_PRTNM,B.PT_ZONCD";
			//if(LP_ROWNO==TB_OVDWK)
			//System.out.println("strSQLQRY1"+strSQLQRY1);
			rstRSSET1_DSP = cl_dat.exeSQLQRY1(strSQLQRY1);
			if(rstRSSET1_DSP!=null)
			{
				while(rstRSSET1_DSP.next())
				{
					LP_VTR_DB.add(rstRSSET1_DSP.getString("PT_ZONCD")+"|"+rstRSSET1_DSP.getString("DSTNM")+"|"+rstRSSET1_DSP.getString("CSTNM")+"|"+rstRSSET1_DSP.getString("BALAM")+"|"+rstRSSET1_DSP.getString("CSTCD"));
					L_ARRDBL[0]+=rstRSSET1_DSP.getDouble("BALAM");
					if(rstRSSET1_DSP.getString("PT_ZONCD").equals("02"))
						L_ARRDBL[1]+=rstRSSET1_DSP.getDouble("BALAM");
					else if(rstRSSET1_DSP.getString("PT_ZONCD").equals("03"))
						L_ARRDBL[2]+=rstRSSET1_DSP.getDouble("BALAM");
					else if(rstRSSET1_DSP.getString("PT_ZONCD").equals("04"))
						L_ARRDBL[3]+=rstRSSET1_DSP.getDouble("BALAM");
					else if(rstRSSET1_DSP.getString("PT_ZONCD").equals("06"))
						L_ARRDBL[4]+=rstRSSET1_DSP.getDouble("BALAM");
					else if(rstRSSET1_DSP.getString("PT_ZONCD").equals("07"))
						L_ARRDBL[5]+=rstRSSET1_DSP.getDouble("BALAM");
					else if(rstRSSET1_DSP.getString("PT_ZONCD").equals("11"))
						L_ARRDBL[6]+=rstRSSET1_DSP.getDouble("BALAM");
				}
			}
			rstRSSET1_DSP.close();
			//strSQLQRY1 = " SELEC	T D.PT_PRTNM DSTNM,C.PT_PRTNM CSTNM,C.PT_ZONCD,sum((ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0))) BALAM,D.PT_PRTCD DSTCD,C.PT_PRTCD CSTCD";
			//strSQLQRY1+= " FROM MR_PLTRN,CO_PTMST C,CO_PTMST D ";
			//strSQLQRY1+= " where D.PT_PRTTP='D' and D.PT_PRTCD=C.PT_DSRCD";
			//strSQLQRY1+= " and (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND C.PT_PRTTP = 'C' and C.PT_PRTTP=PL_PRTTP AND C.PT_PRTCD=PL_PRTCD AND  PL_CMPCD='"+M_strCMPCD_pbst+"' AND C.PT_ZONCD not in ('11','51','52','99') AND (PL_DOCTP LIKE  '0%' OR PL_DOCTP LIKE '1%')"+LP_WHRSTR;
			//strSQLQRY1+= " group by D.PT_PRTNM,C.PT_PRTNM,C.PT_ZONCD,D.PT_PRTCD,C.PT_PRTCD order by D.PT_PRTNM,C.PT_PRTNM,C.PT_ZONCD";

			strSQLQRY1 = " SELECT  B.PT_PRTNM DSTNM,A.PT_PRTNM CSTNM,B.PT_ZONCD,sum((ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0))) BALAM,B.PT_PRTCD DSTCD,A.PT_PRTCD CSTCD";
			strSQLQRY1+= " FROM MR_PLTRN,CO_PTMST A,CO_PTMST B,CO_CDTRN C,CO_CDTRN D";
			strSQLQRY1+= " WHERE PL_PRTTP='C' and PL_PRTTP=A.PT_PRTTP AND  PL_PRTCD=A.PT_PRTCD AND B.PT_PRTTP='D'  AND A.PT_DSRCD=B.PT_PRTCD  AND PL_CMPCD='01' AND PL_STSFL != 'X' AND  PL_SBSCD NOT IN ('111200') AND  C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MR00ZON' AND B.PT_ZONCD=C.CMT_CODCD  AND (D.CMT_CGMTP='D01' OR D.CMT_CGMTP = 'SYS')";
			strSQLQRY1+= " and d.cmt_chp01 = 'CR' AND ((D.CMT_CGSTP='MRXXPTT' and substr(D.CMT_CODCD,1,1) in ('0','9') AND SUBSTR(D.CMT_CODCD,2,2)=PL_DOCTP) OR (D.CMT_CGSTP='MRXXOCN' AND D.CMT_CODCD=PL_DOCTP)) AND ((PL_DOCVL-ifnull(PL_ADJVL,0)) > 0 OR A.PT_YOPVL > 0) AND (PL_DOCDT >= '01/07/2006' OR SUBSTR(PL_DOCNO,4,5)='77777')";
			strSQLQRY1+= " AND ifnull(PL_ADJVL,0) <= PL_DOCVL AND LENGTH(TRIM(IFNULL(PL_DOCNO,''))) = 8";
			strSQLQRY1+= " group by B.PT_PRTNM,A.PT_PRTNM,B.PT_ZONCD,B.PT_PRTCD,A.PT_PRTCD order by B.PT_PRTNM,A.PT_PRTNM,B.PT_ZONCD";
			//if(LP_ROWNO==TB_OVDWK)
			//System.out.println("strSQLQRY2"+strSQLQRY1);
			ResultSet rstRSSET2_DSP = cl_dat.exeSQLQRY1(strSQLQRY1);
			if(rstRSSET2_DSP!=null)
			{
				while(rstRSSET2_DSP.next())
				{
					LP_VTR_CR.add(rstRSSET2_DSP.getString("PT_ZONCD")+"|"+rstRSSET2_DSP.getString("DSTNM")+"|"+rstRSSET2_DSP.getString("CSTNM")+"|"+rstRSSET2_DSP.getString("BALAM")+"|"+rstRSSET2_DSP.getString("CSTCD"));
					L_ARRDBL[0]-=rstRSSET2_DSP.getDouble("BALAM");
					if(rstRSSET2_DSP.getString("PT_ZONCD").equals("02"))
						L_ARRDBL[1]-=rstRSSET2_DSP.getDouble("BALAM");
					else if(rstRSSET2_DSP.getString("PT_ZONCD").equals("03"))
						L_ARRDBL[2]-=rstRSSET2_DSP.getDouble("BALAM");
					else if(rstRSSET2_DSP.getString("PT_ZONCD").equals("04"))
						L_ARRDBL[3]-=rstRSSET2_DSP.getDouble("BALAM");
					else if(rstRSSET2_DSP.getString("PT_ZONCD").equals("06"))
						L_ARRDBL[4]-=rstRSSET2_DSP.getDouble("BALAM");
					else if(rstRSSET2_DSP.getString("PT_ZONCD").equals("07"))
						L_ARRDBL[5]-=rstRSSET2_DSP.getDouble("BALAM");
					else if(rstRSSET2_DSP.getString("PT_ZONCD").equals("11"))
						L_ARRDBL[6]-=rstRSSET2_DSP.getDouble("BALAM");
				}
			}

			tblCATDL.setValueAt(String.valueOf(setNumberFormat((L_ARRDBL[0]/intCSTIN),3)),LP_ROWNO,TB_BALAM);
			rstRSSET2_DSP.close();
			return L_ARRDBL;
		}
		catch(Exception L_EX){
			System.out.println("getDSPMSG(): "+L_EX);
		}
		return L_ARRDBL;
	}


	public void mousePressed(MouseEvent L_ME)	{		try
		{
			if(jtpMANTAB.getTitleAt(jtpMANTAB.getSelectedIndex()).toString().equals("Receivables"))
				btnGRAPH.setVisible(false);
			else
				btnGRAPH.setVisible(true);
			
			//if(L_ME.getSource() == jtpMANTAB)
			//{
			//	int intSELPG = jtpMANTAB.getSelectedIndex();
			//	if(intSELPG == 1  && !flgSLSDL_STRT)
			//	{
			//		flgSLSDL_STRT = true;
			//		try{
			//		thrDATA_SLS.start();
			//		}catch(Exception E){System.out.println(thrDATA_SLS.getState());};
			//	}
			//}
			//else 
			
			if(L_ME.getSource() == tblCNSDL_MN)
		    {
				clrTABLE(tblCNSDL_DL);
				//System.out.println(tblCNSDL.getValueAt(tblCNSDL.getSelectedRow(),TB_DPTCD)+" "+tblCNSDL.getColumnName(tblCNSDL.getSelectedColumn())+" "+tblCNSDL_MN.getValueAt(tblCNSDL_MN.getSelectedRow(),TB_MONTH1));
				setCNSDL_DL(tblCNSDL.getValueAt(tblCNSDL.getSelectedRow(),TB_DPTCD).toString(),tblCNSDL.getColumnName(tblCNSDL.getSelectedColumn()).toString(),tblCNSDL_MN.getValueAt(tblCNSDL_MN.getSelectedRow(),TB_MONTH1).toString());
			}
			else if(L_ME.getSource() == tblCNSDL)
			{
				clrTABLE(tblCNSDL_MN);
				clrTABLE(tblCNSDL_DL);
				clrTABLE(tblCNSDL_CP);
				lblMSG_CNS1.setText("");
				lblMSG_CNS2.setText("");
				lblMSG_CNS3.setText("");
				if(tblCNSDL.getSelectedColumn() > 2)
				{
					jtpCNSDL.setSelectedIndex(0);
					setCNSDL_MN(tblCNSDL.getSelectedRow(),tblCNSDL.getSelectedColumn());
				}
				else if(tblCNSDL.getSelectedColumn() <= 2)
				{
					jtpCNSDL.setSelectedIndex(1);
					setCNSDL_CP(tblCNSDL.getValueAt(tblCNSDL.getSelectedRow(),TB_DPTCD).toString());
				}
			}
			else if(L_ME.getSource() == tblSTRDL1)
			{
				clrTABLE(tblSTRDL2);
				clrTABLE(tblSTRDL3);
				clrTABLE(tblSTRDL4);
				jtpSTRDL.setSelectedIndex(0);
				setGRPDL(tblSTRDL1,tblSTRDL2,2,4,"00000A");
			}
			else if(L_ME.getSource() == tblSTRDL2)
			{
				clrTABLE(tblSTRDL3);
				clrTABLE(tblSTRDL4);
				jtpSTRDL.setSelectedIndex(1);
				setGRPDL(tblSTRDL2,tblSTRDL3,4,6,"000A");
			}
			else if(L_ME.getSource() == tblSTRDL3)
			{
				clrTABLE(tblSTRDL4);
				jtpSTRDL.setSelectedIndex(2);
				String[] staSTRDL=null;
				double L_dblTOTQT=0.0,L_dblTOTVL=0.0,L_dblTOTCC=0.0;
				int intROWNO=0;
				String strGRPCD = tblSTRDL3.getValueAt(tblSTRDL3.getSelectedRow(),TB_GRPCD_ST).toString();
				for(int i=0;i<vtrSTRDL.size();i++)
				{
					staSTRDL=null;
					staSTRDL = vtrSTRDL.get(i).replace('|','~').split("~");
					if(strGRPCD.equals(staSTRDL[0].substring(0,6)))
				    {
						tblSTRDL4.setValueAt(staSTRDL[0],intROWNO,TB_GRPCD_ST1);
						if(hstGRPCD.containsKey(staSTRDL[0]))
							tblSTRDL4.setValueAt(hstGRPCD.get(staSTRDL[0]),intROWNO,TB_GRPDS_ST1);
						tblSTRDL4.setValueAt(staSTRDL[1],intROWNO,TB_UOMCD_ST1);
						tblSTRDL4.setValueAt(setNumberFormat(Double.parseDouble(staSTRDL[2]),3),intROWNO,TB_STKQT_ST1);
						tblSTRDL4.setValueAt(staSTRDL[3],intROWNO,TB_PGRDT_ST1);
						tblSTRDL4.setValueAt(staSTRDL[4],intROWNO,TB_NOFDS_ST1);
						
						tblSTRDL4.setValueAt(setNumberFormat(Double.parseDouble(staSTRDL[5])/intCSTIN,3),intROWNO,TB_YCLRT_ST1);
						tblSTRDL4.setValueAt(setNumberFormat(Double.parseDouble(staSTRDL[6])/intCSTIN,3),intROWNO,TB_IVTVL_ST1);
						tblSTRDL4.setValueAt(setNumberFormat(Double.parseDouble(staSTRDL[7])/intCSTIN,3),intROWNO,TB_IVTCC_ST1);
						
						L_dblTOTQT += Double.parseDouble(staSTRDL[2]);
						L_dblTOTVL += Double.parseDouble(staSTRDL[6]);
						L_dblTOTCC += Double.parseDouble(staSTRDL[7]);
						intROWNO++;
					}
				}
				intROWNO+=2;
				tblSTRDL4.setValueAt("TOTAL",intROWNO,TB_YCLRT_ST1);
				tblSTRDL4.setValueAt(setNumberFormat(L_dblTOTVL/intCSTIN,3),intROWNO,TB_IVTVL_ST1);
				tblSTRDL4.setValueAt(setNumberFormat(L_dblTOTCC/intCSTIN,3),intROWNO,TB_IVTCC_ST1);
			}
			else if(L_ME.getSource() == tblPRDDL_IVT)
			{
				clrTABLE(tblGRDDL_IVT);
				clrTABLE(tblLOTDL_IVT);
				jtpIVTDL.setSelectedIndex(0);
				String OLD_GRDCD="",NEW_GRDCD="";
				String[] staIVTDL=null;
				double L_dblIVTQT=0.0,L_dblIVTVL=0.0,L_dblIVTCC=0.0,L_dblTOTQT=0.0,L_dblTOTVL=0.0,L_dblTOTCC=.0;
				int intROWNO=0;
				tblGRDDL_IVT.setValueAt("PRODUCT",intROWNO,TB8_GRDDS);
				tblGRDDL_IVT.setValueAt(tblPRDDL_IVT.getValueAt(tblPRDDL_IVT.getSelectedRow(),TB7_PRDDS).toString(),intROWNO,TB8_IVTQT);
				intROWNO+=2;
				String strPRDCD = hstPRDDS.get(tblPRDDL_IVT.getValueAt(tblPRDDL_IVT.getSelectedRow(),TB7_PRDDS).toString());
				for(int i=0;i<vtrIVTDL.size();i++)
				{
					staIVTDL=null;
					staIVTDL = vtrIVTDL.get(i).replace('|','~').split("~");
					if(strPRDCD.equals(staIVTDL[0].substring(0,4)))
				    {
						NEW_GRDCD = staIVTDL[0];
						if(!NEW_GRDCD.equals(OLD_GRDCD) && !OLD_GRDCD.equals(""))
						{
							tblGRDDL_IVT.setValueAt(hstGRDCD.get(OLD_GRDCD),intROWNO,TB8_GRDDS);
							tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB8_IVTQT);
							tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB8_IVTVL);
							tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB8_IVTCC);
							L_dblTOTQT += L_dblIVTQT;
							L_dblTOTVL+= L_dblIVTVL;
							L_dblTOTCC+= L_dblIVTCC;
							intROWNO++;
							L_dblIVTQT=0.0;
							L_dblIVTVL=0.0;
							L_dblIVTCC=0.0;
						}
						L_dblIVTQT += Double.parseDouble(staIVTDL[3]);
						L_dblIVTVL += Double.parseDouble(staIVTDL[4]);
						L_dblIVTCC += Double.parseDouble(staIVTDL[5]);
						OLD_GRDCD = staIVTDL[0];
					}
				}
				tblGRDDL_IVT.setValueAt(hstGRDCD.get(OLD_GRDCD),intROWNO,TB8_GRDDS);
				tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB8_IVTQT);
				tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB8_IVTVL);
				tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB8_IVTCC);
				L_dblTOTQT += L_dblIVTQT;
				L_dblTOTVL+= L_dblIVTVL;
				L_dblTOTCC+= L_dblIVTCC;
				
				intROWNO+=2;
				tblGRDDL_IVT.setValueAt("TOTAL",intROWNO,TB8_GRDDS);
				tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblTOTQT,3),intROWNO,TB8_IVTQT);
				tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblTOTVL/intCSTIN,3),intROWNO,TB8_IVTVL);
				tblGRDDL_IVT.setValueAt(setNumberFormat(L_dblTOTCC/intCSTIN,3),intROWNO,TB8_IVTCC);
			}
			else if(L_ME.getSource() == tblGRDDL_IVT)
			{
				clrTABLE(tblLOTDL_IVT);
				jtpIVTDL.setSelectedIndex(1);
				String[] staIVTDL=null;
				double L_dblTOTQT=0.0,L_dblTOTVL=0.0,L_dblTOTCC=0.0;
				int intROWNO=0;
				
				tblLOTDL_IVT.setValueAt("GRADE",intROWNO,TB9_LOTNO);
				tblLOTDL_IVT.setValueAt(tblGRDDL_IVT.getValueAt(tblGRDDL_IVT.getSelectedRow(),TB8_GRDDS).toString(),intROWNO,TB9_LOTDT);
				intROWNO+=2;		
				String strGRDCD = hstGRDDS.get(tblGRDDL_IVT.getValueAt(tblGRDDL_IVT.getSelectedRow(),TB8_GRDDS).toString());
				for(int i=0;i<vtrIVTDL.size();i++)
				{
					staIVTDL=null;
					staIVTDL = vtrIVTDL.get(i).replace('|','~').split("~");
					if(strGRDCD.equals(staIVTDL[0]))
				    {
						tblLOTDL_IVT.setValueAt(staIVTDL[1],intROWNO,TB9_LOTNO);
						tblLOTDL_IVT.setValueAt(staIVTDL[2],intROWNO,TB9_LOTDT);
						tblLOTDL_IVT.setValueAt(setNumberFormat(Double.parseDouble(staIVTDL[3]),3),intROWNO,TB9_IVTQT);
						tblLOTDL_IVT.setValueAt(setNumberFormat(Double.parseDouble(staIVTDL[4])/intCSTIN,3),intROWNO,TB9_IVTVL);
						tblLOTDL_IVT.setValueAt(setNumberFormat(Double.parseDouble(staIVTDL[5])/intCSTIN,3),intROWNO,TB9_IVTCC);
						tblLOTDL_IVT.setValueAt(staIVTDL[6],intROWNO,TB9_NOFDS);
						
						L_dblTOTQT += Double.parseDouble(staIVTDL[3]);
						L_dblTOTVL += Double.parseDouble(staIVTDL[4]);
						L_dblTOTCC += Double.parseDouble(staIVTDL[5]);
						intROWNO++;
					}
				}
				intROWNO+=2;
				tblLOTDL_IVT.setValueAt("TOTAL",intROWNO,TB9_LOTDT);
				tblLOTDL_IVT.setValueAt(setNumberFormat(L_dblTOTQT,3),intROWNO,TB9_IVTQT);
				tblLOTDL_IVT.setValueAt(setNumberFormat(L_dblTOTVL/intCSTIN,3),intROWNO,TB9_IVTVL);
				tblLOTDL_IVT.setValueAt(setNumberFormat(L_dblTOTCC/intCSTIN,3),intROWNO,TB9_IVTCC);
			}
			else if(L_ME.getSource() == tblSLSDL)
			{
			   if(flgSLSDL)
			   {
				    if(tblSLSDL.getValueAt(tblSLSDL.getSelectedRow(),TB4_YEAR).toString().equals(""))
					{	
						lblMSG_SLS.setText("Please Select Year.....");
						return;
					}
					lblMSG_SLS.setText("");
					clrTABLE(tblPRDDL_SLS);
					clrTABLE(tblGRDDL_SLS);
					jtpSLSDL.setSelectedIndex(0);
					getPRDDL_SLS(tblSLSDL.getValueAt(tblSLSDL.getSelectedRow(),TB4_YEAR).toString().substring(5,9));
			   }
			}
			else if(L_ME.getSource() == tblPRDDL_SLS)
			{
				clrTABLE(tblGRDDL_SLS);
				jtpSLSDL.setSelectedIndex(1);
				getGRDDL_SLS(tblSLSDL.getValueAt(tblSLSDL.getSelectedRow(),TB4_YEAR).toString().substring(5,9),tblPRDDL_SLS.getValueAt(tblPRDDL_SLS.getSelectedRow(),TB5_PRDDS).toString());
			}
			else if(L_ME.getSource() == tblCATDL)
			{
				//if(tblCATDL.getSelectedColumn()==TB_CHKFL)
				//{
				if(tblCATDL.getValueAt(tblCATDL.getSelectedRow(),TB_BALAM).toString().equals(""))
				{	
					lblMSG_RCV.setText("Please wait.....");
					return;
				}
				lblMSG_RCV.setText("");
				tblCATDL.setValueAt(new Boolean(true),tblCATDL.getSelectedRow(),TB_CHKFL);
				for(int i=0;i<tblCATDL.getRowCount();i++)
				{
					if(tblCATDL.getValueAt(i,TB_CHKFL).toString().equals("true"))
					{
						if(i != tblCATDL.getSelectedRow())
							tblCATDL.setValueAt(new Boolean(false),i,TB_CHKFL);
					}
				}
				for(int j=0;j<tblZONDL.getRowCount();j++)
				{
					if(tblZONDL.getValueAt(j,TB1_CHKFL).toString().equals("true"))
						tblZONDL.setValueAt(new Boolean(false),j,TB1_CHKFL);
					tblZONDL.setValueAt("",j,2);
				}
				clrTABLE(tblDSTDL);
				clrTABLE(tblINVDL);
				jtpRCVDL.setSelectedIndex(0);
				lblCATDL.setText(tblCATDL.getValueAt(tblCATDL.getSelectedRow(),TB_DTAIL).toString());
				lblZONDL.setText("");
				if(tblCATDL.getSelectedRow()==TB_OVDDT)
					exeZONDSP(arrOVDDT);
				if(tblCATDL.getSelectedRow()==TB_OSTDT)
					exeZONDSP(arrOSTDT);
				if(tblCATDL.getSelectedRow()==TB_DAYCL)
					exeZONDSP(arrDAYCL);
				if(tblCATDL.getSelectedRow()==TB_OVDWK)
					exeZONDSP(arrOVDWK);
				if(tblCATDL.getSelectedRow()==TB_OVDMN)
					exeZONDSP(arrOVDMN);
				//}
			}	
			else if(L_ME.getSource() == tblZONDL)
			{
				//if(tblZONDL.getSelectedColumn()==TB1_CHKFL)
				//{
				tblZONDL.setValueAt(new Boolean(true),tblZONDL.getSelectedRow(),TB1_CHKFL);
				for(int i=0;i<tblZONDL.getRowCount();i++)
				{
					if(tblZONDL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						if(i != tblZONDL.getSelectedRow())
							tblZONDL.setValueAt(new Boolean(false),i,TB1_CHKFL);
					}
				}
				clrTABLE(tblDSTDL);
				clrTABLE(tblINVDL);
				jtpRCVDL.setSelectedIndex(0);
				lblZONDL.setText(tblCATDL.getValueAt(tblCATDL.getSelectedRow(),TB_DTAIL).toString()+" ("+tblZONDL.getValueAt(tblZONDL.getSelectedRow(),TB1_DTAIL).toString()+")");

				Vector<String> L_vtrXXXDT_DB = new Vector<String>();
				Vector<String> L_vtrXXXDT_CR = new Vector<String>();
				for(int i=0;i<tblCATDL.getRowCount();i++)
				{
					if(tblCATDL.getValueAt(i,TB_CHKFL).toString().equals("true"))
					{
						if(i==TB_OSTDT)
						{	
							L_vtrXXXDT_DB=vtrOSTDT_DB;
							L_vtrXXXDT_CR=vtrOSTDT_CR;
						}
						if(i==TB_OVDDT)
						{	
							L_vtrXXXDT_DB=vtrOVDDT_DB;
							L_vtrXXXDT_CR=vtrOVDDT_CR;
						}	
						if(i==TB_DAYCL)
						{	
							L_vtrXXXDT_DB=vtrDAYCL_DB;
							L_vtrXXXDT_CR=vtrDAYCL_CR;
						}
						if(i==TB_OVDWK)
						{	
							L_vtrXXXDT_DB=vtrOVDWK_DB;
							L_vtrXXXDT_CR=vtrOVDWK_CR;
						}
						if(i==TB_OVDMN)
						{	
							L_vtrXXXDT_DB=vtrOVDMN_DB;
							L_vtrXXXDT_CR=vtrOVDMN_CR;
						}
					}
				}
				if(tblZONDL.getSelectedRow()==TB1_NORTH)
					exeDSTDSP("02",L_vtrXXXDT_DB,L_vtrXXXDT_CR);
				else if(tblZONDL.getSelectedRow()==TB1_EAST)
					exeDSTDSP("03",L_vtrXXXDT_DB,L_vtrXXXDT_CR);
				else if(tblZONDL.getSelectedRow()==TB1_SOUTH)
					exeDSTDSP("04",L_vtrXXXDT_DB,L_vtrXXXDT_CR);
				else if(tblZONDL.getSelectedRow()==TB1_WEST)
					exeDSTDSP("06",L_vtrXXXDT_DB,L_vtrXXXDT_CR);					
				else if(tblZONDL.getSelectedRow()==TB1_CENTRAL)
					exeDSTDSP("07",L_vtrXXXDT_DB,L_vtrXXXDT_CR);
				//else if(tblZONDL.getSelectedRow()==TB1_EXPORT)
				//	exeDSTDSP("11",L_vtrXXXDT_DB,L_vtrXXXDT_CR);
				//}
			}
			else if(L_ME.getSource() == tblDSTDL)
			{
				//if(tblDSTDL.getSelectedColumn()==TB2_CHKFL)
				//{
				tblDSTDL.setValueAt(new Boolean(true),tblDSTDL.getSelectedRow(),TB2_CHKFL);
				for(int i=0;i<tblDSTDL.getRowCount();i++)
				{
					if(tblDSTDL.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					{
						if(i != tblDSTDL.getSelectedRow())
							tblDSTDL.setValueAt(new Boolean(false),i,TB2_CHKFL);
					}
				}
				clrTABLE(tblINVDL);

				String strWHRSTR="";
				String L_strCSTCD = tblDSTDL.getValueAt(tblDSTDL.getSelectedRow(),TB2_CSTCD).toString();
				for(int i=0;i<tblCATDL.getRowCount();i++)
				{
					if(tblCATDL.getValueAt(i,TB_CHKFL).toString().equals("true"))
					{
						if(i==TB_OSTDT)
						{	
							strWHRSTR="";
							//System.out.println("i >>"+i);
							exeINVDSP(strWHRSTR,L_strCSTCD);
						}
						if(i==TB_OVDDT)
						{	
							strWHRSTR=" AND (DAYS(date('"+fmtMDYDT.format(fmtDMYDT.parse(strCURDT))+"'))-DAYS(PL_DUEDT))>=0";
							//System.out.println("i >>"+i);
							exeINVDSP(strWHRSTR,L_strCSTCD);
						}	
						if(i==TB_DAYCL)
						{	
							//strWHRSTR="";
							//System.out.println("i >>"+i);
							exeINVDSP_DAYCL(L_strCSTCD);
						}	
						if(i==TB_OVDWK)
						{	
							strWHRSTR=" AND (DAYS(date('"+fmtMDYDT.format(fmtDMYDT.parse(strENDWK))+"'))-DAYS(PL_DUEDT))>=0";
							//System.out.println("i >>"+i);
							exeINVDSP(strWHRSTR,L_strCSTCD);
						}	
						if(i==TB_OVDMN)
						{	
							strWHRSTR=" AND (DAYS(date('"+fmtMDYDT.format(fmtDMYDT.parse(strENDMN))+"'))-DAYS(PL_DUEDT))>=0";
							//System.out.println("i >>"+i);
							exeINVDSP(strWHRSTR,L_strCSTCD);
						}	
					}
				}
				//}
			}
		}
		catch(Exception e)
		{
			System.out.println("inside mousepressed() : "+e);	
		}
	}
	
	private void setCNSDL_CP(String LP_DPTCD)
	{
		pnlCNSDL.setCursor(curWTSTS);
		if(hstDPTCD.containsKey(LP_DPTCD))
			lblMSG_CNS3.setText(hstDPTCD.get(LP_DPTCD));
		int ROW = 0;
		String[] staCNSDL=null;
		double dblYEAR3_QT_TT = 0,dblYEAR3_VL_TT = 0;
		double dblYEAR2_QT_TT = 0,dblYEAR2_VL_TT = 0;
		double dblYEAR1_QT_TT = 0,dblYEAR1_VL_TT = 0;
		double dblYEAR_QT_TT = 0,dblYEAR_VL_TT = 0;

		try{
			Vector<String> L_vtrMATCD = new Vector<String>();
			for(int i=0;i<vtrCNSDL.size();i++)
			{
				staCNSDL=null;
				staCNSDL = vtrCNSDL.get(i).replace('|','~').split("~");
				if(staCNSDL[0].equals(LP_DPTCD))
				{
					if(!L_vtrMATCD.contains(staCNSDL[2]))
						L_vtrMATCD.add(staCNSDL[2]);
				}
			}
			exeSORT(L_vtrMATCD);
			for(int i=0;i<L_vtrMATCD.size();i++)
			{
				String L_strMATCD = L_vtrMATCD.get(i);
				String L_strUOMCD = "";
				double dblYEAR3_QT = 0,dblYEAR3_VL = 0;
				double dblYEAR2_QT = 0,dblYEAR2_VL = 0;
				double dblYEAR1_QT = 0,dblYEAR1_VL = 0;
				double dblYEAR_QT = 0,dblYEAR_VL = 0;
				for(int j=0;j<vtrCNSDL.size();j++)
				{
					staCNSDL=null;
					staCNSDL = vtrCNSDL.get(j).replace('|','~').split("~");
					L_strUOMCD = staCNSDL[3];
					if(staCNSDL[0].equals(LP_DPTCD) && staCNSDL[2].equals(L_strMATCD))
					{
						if(Integer.parseInt(staCNSDL[1])==prvYEAR3_JJ)
						{
							dblYEAR3_QT+=Double.parseDouble(staCNSDL[5]);
							dblYEAR3_VL+=Double.parseDouble(staCNSDL[7]);
						}
						else if(Integer.parseInt(staCNSDL[1])==prvYEAR2_JJ)
						{
							dblYEAR2_QT+=Double.parseDouble(staCNSDL[5]);
							dblYEAR2_VL+=Double.parseDouble(staCNSDL[7]);
						}
						else if(Integer.parseInt(staCNSDL[1])==prvYEAR1_JJ)
						{
							dblYEAR1_QT+=Double.parseDouble(staCNSDL[5]);
							dblYEAR1_VL+=Double.parseDouble(staCNSDL[7]);
						}				
						else if(Integer.parseInt(staCNSDL[1])==curYEAR_JJ)
						{
							dblYEAR_QT+=Double.parseDouble(staCNSDL[5]);
							dblYEAR_VL+=Double.parseDouble(staCNSDL[7]);
						}
					}
				}
				
				tblCNSDL_CP.setValueAt(L_strMATCD,ROW,TB_MATCD_CP);
				if(hstGRPCD.containsKey(L_strMATCD))
					tblCNSDL_CP.setValueAt(hstGRPCD.get(L_strMATCD),ROW,TB_MATDS_CP);
				tblCNSDL_CP.setValueAt(L_strUOMCD,ROW,TB_UOMCD_CP);
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR3_QT,3),ROW,TB_YEAR3_QT);
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR3_VL/intCSTIN,3),ROW,TB_YEAR3_VL);			
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR2_QT,3),ROW,TB_YEAR2_QT);
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR2_VL/intCSTIN,3),ROW,TB_YEAR2_VL);			
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR1_QT,3),ROW,TB_YEAR1_QT);
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR1_VL/intCSTIN,3),ROW,TB_YEAR1_VL);			
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR_QT,3),ROW,TB_YEAR_QT);
				tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR_VL/intCSTIN,3),ROW,TB_YEAR_VL);			
				ROW++;
				
				dblYEAR3_QT_TT+=dblYEAR3_QT;
				dblYEAR3_VL_TT+=dblYEAR3_VL;
				dblYEAR2_QT_TT+=dblYEAR2_QT;
				dblYEAR2_VL_TT+=dblYEAR2_VL;
				dblYEAR1_QT_TT+=dblYEAR1_QT;
				dblYEAR1_VL_TT+=dblYEAR1_VL;
				dblYEAR_QT_TT+=dblYEAR_QT;
				dblYEAR_VL_TT+=dblYEAR_VL;
			}
			ROW++;
			tblCNSDL_CP.setValueAt("TOTAL",ROW,TB_UOMCD_CP);
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR3_QT_TT,3),ROW,TB_YEAR3_QT);
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR3_VL_TT/intCSTIN,3),ROW,TB_YEAR3_VL);			
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR2_QT_TT,3),ROW,TB_YEAR2_QT);
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR2_VL_TT/intCSTIN,3),ROW,TB_YEAR2_VL);			
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR1_QT_TT,3),ROW,TB_YEAR1_QT);
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR1_VL_TT/intCSTIN,3),ROW,TB_YEAR1_VL);			
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR_QT_TT,3),ROW,TB_YEAR_QT);
			tblCNSDL_CP.setValueAt(setNumberFormat(dblYEAR_VL_TT/intCSTIN,3),ROW,TB_YEAR_VL);
		}
		catch(Exception E)
		{
			System.out.println("inside setCNSDL_CP() : "+E);
		}
		pnlCNSDL.setCursor(curDFSTS);
	}
	
	private void setCNSDL_DL(String LP_DPTCD,String LP_YEAR,String LP_MONTH)
	{
		double dblTOTQT = 0,dblTOTRT = 0,dblTOTVL = 0;
		int ROW = 0;
		String[] staCNSDL=null;
		try
		{
			for(int i=0;i<vtrCNSDL.size();i++)
			{
				if(hstDPTCD.containsKey(LP_DPTCD))
					lblMSG_CNS2.setText(hstDPTCD.get(LP_DPTCD)+" "+LP_YEAR+" "+staMONTH[Integer.parseInt(LP_MONTH)-1]);
				staCNSDL=null;
				staCNSDL = vtrCNSDL.get(i).replace('|','~').split("~");
				if(staCNSDL[0].equals(LP_DPTCD)
				&& staCNSDL[1].equals(LP_YEAR)
				&& Integer.parseInt(staCNSDL[4].substring(3,5))== Integer.parseInt(LP_MONTH))
				{
					tblCNSDL_DL.setValueAt(staCNSDL[2],ROW,TB_MATCD_DL);
					if(hstGRPCD.containsKey(staCNSDL[2]))
						tblCNSDL_DL.setValueAt(hstGRPCD.get(staCNSDL[2]),ROW,TB_MATDS_DL);
					//tblCNSDL_DL.setValueAt(staCNSDL[2],ROW,TB_MATDS_DL);
					tblCNSDL_DL.setValueAt(staCNSDL[3],ROW,TB_UOMCD_DL);
					tblCNSDL_DL.setValueAt(staCNSDL[4].substring(0,6)+"20"+staCNSDL[4].substring(6,8),ROW,TB_ISSDT_DL);
					tblCNSDL_DL.setValueAt(setNumberFormat(Double.parseDouble(staCNSDL[6])/intCSTIN,3),ROW,TB_ISSRT_DL);
					tblCNSDL_DL.setValueAt(staCNSDL[5],ROW,TB_ISSQT_DL);
					tblCNSDL_DL.setValueAt(setNumberFormat(Double.parseDouble(staCNSDL[7])/intCSTIN,3),ROW,TB_ISSVL_DL);
					dblTOTRT+=Double.parseDouble(staCNSDL[6]);
					dblTOTQT+=Double.parseDouble(staCNSDL[5]);
					dblTOTVL+=Double.parseDouble(staCNSDL[7]);
					ROW++;
				}
			}
			ROW++;
			tblCNSDL_DL.setValueAt("TOTAL",ROW,TB_ISSRT_DL);
			tblCNSDL_DL.setValueAt(setNumberFormat(dblTOTQT,3),ROW,TB_ISSQT_DL);
			tblCNSDL_DL.setValueAt(setNumberFormat(dblTOTVL/intCSTIN,3),ROW,TB_ISSVL_DL);
		}
		catch(Exception E)
		{
			System.out.println("inside setCNSDL_DL() : "+E);
		}
	}
	private void setCNSDL_MN(int LP_ROW,int LP_COL)
	{
		lblMSG_CNS1.setText(tblCNSDL.getValueAt(LP_ROW,TB_DPTDS)+" "+tblCNSDL.getColumnName(LP_COL));
		double dblTOTAL = 0;
		int ROW = 0;
		String[] staCNSDL=null;
		double[] dbaMNTOT= new double[12];
		for(int i=0;i<12;i++)
			dbaMNTOT[i] = 0;
		for(int i=0;i<vtrCNSDL.size();i++)
		{
			staCNSDL=null;
			staCNSDL = vtrCNSDL.get(i).replace('|','~').split("~");
			if(staCNSDL[0].equals(tblCNSDL.getValueAt(LP_ROW,TB_DPTCD))
			&& staCNSDL[1].equals(tblCNSDL.getColumnName(LP_COL)))
			{
				int L_intMONTH = Integer.parseInt(staCNSDL[4].substring(3,5));
				dbaMNTOT[L_intMONTH-1]+=Double.parseDouble(staCNSDL[7]);
			}
		}
		for(int i=0;i<12;i++)
		{
			tblCNSDL_MN.setValueAt(staMONTH[i],ROW,TB_MONTH);
			tblCNSDL_MN.setValueAt(setNumberFormat(dbaMNTOT[i]/intCSTIN,3),ROW,TB_VALUE);
			tblCNSDL_MN.setValueAt(i+1,ROW,TB_MONTH1);
			dblTOTAL += dbaMNTOT[i];
			ROW++;
		}
		ROW++;
		tblCNSDL_MN.setValueAt("TOTAL",ROW,TB_MONTH);
		tblCNSDL_MN.setValueAt(setNumberFormat(dblTOTAL/intCSTIN,3),ROW,TB_VALUE);
	}
	
	public void setGRPDL(JTable TBL_SRC,JTable TBL_TRG,int LP_SRCGR,int LP_TRGGR,String LP_STRGR)
	{
		String[] staSTRDL=null;
		String OLD_GRPCD="",NEW_GRPCD="";
		double L_dblIVTQT=0.0,L_dblIVTVL=0.0,L_dblIVTCC=0.0,L_dblTOTQT=0.0,L_dblTOTVL=0.0,L_dblTOTCC=.0;
		int L_intNOFIT = 0,L_intTOTIT = 0;
		int intROWNO=0;
		String strGRPCD = TBL_SRC.getValueAt(TBL_SRC.getSelectedRow(),TB_GRPCD_ST).toString();
		for(int i=0;i<vtrSTRDL.size();i++)
		{
			staSTRDL=null;
			staSTRDL = vtrSTRDL.get(i).replace('|','~').split("~");
			if(strGRPCD.equals(staSTRDL[0].substring(0,LP_SRCGR)))
			{
				NEW_GRPCD = staSTRDL[0].substring(0,LP_TRGGR);
				if(!NEW_GRPCD.equals(OLD_GRPCD) && !OLD_GRPCD.equals(""))
				{
					TBL_TRG.setValueAt(OLD_GRPCD,intROWNO,TB_GRPCD_ST);
					if(hstGRPCD.containsKey(OLD_GRPCD+LP_STRGR))
						TBL_TRG.setValueAt(hstGRPCD.get(OLD_GRPCD+LP_STRGR),intROWNO,TB_GRPDS_ST);
					//TBL_TRG.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB_STKQT_ST);
					TBL_TRG.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB_IVTVL_ST);
					TBL_TRG.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB_IVTCC_ST);
					TBL_TRG.setValueAt(L_intNOFIT,intROWNO,TB_NOFIT_ST);
					L_dblTOTQT += L_dblIVTQT;
					L_dblTOTVL += L_dblIVTVL;
					L_dblTOTCC += L_dblIVTCC;
					L_intTOTIT += L_intNOFIT;
					intROWNO++;
					L_dblIVTQT=0.0;
					L_dblIVTVL=0.0;
					L_dblIVTCC=0.0;
					L_intNOFIT=0;
				}
				//L_dblIVTQT += Double.parseDouble(staSTRDL[2]);
				L_dblIVTVL += Double.parseDouble(staSTRDL[6]);
				L_dblIVTCC += Double.parseDouble(staSTRDL[7]);
				L_intNOFIT++;
				OLD_GRPCD = staSTRDL[0].substring(0,LP_TRGGR);
			}
		}
		TBL_TRG.setValueAt(OLD_GRPCD,intROWNO,TB_GRPCD_ST);
		if(hstGRPCD.containsKey(OLD_GRPCD+LP_STRGR))
			TBL_TRG.setValueAt(hstGRPCD.get(OLD_GRPCD+LP_STRGR),intROWNO,TB_GRPDS_ST);
		//TBL_TRG.setValueAt(setNumberFormat(L_dblIVTQT,3),intROWNO,TB_STKQT_ST);
		TBL_TRG.setValueAt(setNumberFormat(L_dblIVTVL/intCSTIN,3),intROWNO,TB_IVTVL_ST);
		TBL_TRG.setValueAt(setNumberFormat(L_dblIVTCC/intCSTIN,3),intROWNO,TB_IVTCC_ST);
		TBL_TRG.setValueAt(L_intNOFIT,intROWNO,TB_NOFIT_ST);
		//L_dblTOTQT += L_dblIVTQT;
		L_dblTOTVL += L_dblIVTVL;
		L_dblTOTCC += L_dblIVTCC;
		L_intTOTIT += L_intNOFIT;
		intROWNO+=2;
		TBL_TRG.setValueAt("TOTAL",intROWNO,TB_GRPDS_ST);
		//TBL_TRG.setValueAt(setNumberFormat(L_dblTOTQT,3),intROWNO,TB_STKQT_ST);
		TBL_TRG.setValueAt(setNumberFormat(L_dblTOTVL/intCSTIN,3),intROWNO,TB_IVTVL_ST);
		TBL_TRG.setValueAt(setNumberFormat(L_dblTOTCC/intCSTIN,3),intROWNO,TB_IVTCC_ST);
		TBL_TRG.setValueAt(L_intTOTIT,intROWNO,TB_NOFIT_ST);
	}
	
	public void exeINVDSP(String LP_WHRSTR,String LP_STRCSTCD)
	{
		try{
			pnlDSTDL.setCursor(curWTSTS);
			String strSQLQRY2;
			ResultSet rstRSSET2;			
			int L_intROWCNT=0;
			double L_dblTOTAL_DB=0.0;
			double L_dblTOTAL_CR=0.0;
			//strSQLQRY2 = " SELECT CMT_CODDS,PL_DOCNO,PL_DOCDT,PL_DOCVL,PL_ADJVL,(ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) BALAM";
			//strSQLQRY2+= " from MR_PLTRN left outer join co_cdtrn on";
			//strSQLQRY2+= " CMT_CGMTP='SYS' and CMT_CGSTP='MRXXPTT' and CMT_CODCD=PL_DOCTP";
			//strSQLQRY2+= " where (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND PL_PRTTP='C' and PL_PRTCD = '"+LP_STRCSTCD+"' and  PL_CMPCD='"+M_strCMPCD_pbst+"' AND(PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE '3%')"+LP_WHRSTR;
			//strSQLQRY2+= " order by PL_DOCTP,PL_DOCNO";

			strSQLQRY2 = " SELECT  D.CMT_CODDS,PL_DOCNO,PL_DOCDT,PL_DOCVL,PL_ADJVL,(ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) BALAM ";
			strSQLQRY2+= " FROM MR_PLTRN,CO_PTMST A,CO_PTMST B,CO_CDTRN C,CO_CDTRN D";
			strSQLQRY2+= " WHERE PL_PRTTP='C' and PL_PRTTP=A.PT_PRTTP AND  PL_PRTCD=A.PT_PRTCD AND B.PT_PRTTP='D'  AND A.PT_DSRCD=B.PT_PRTCD  AND PL_CMPCD='01' AND PL_STSFL != 'X' AND  PL_SBSCD NOT IN ('111200') AND  C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MR00ZON' AND B.PT_ZONCD=C.CMT_CODCD  AND (D.CMT_CGMTP='D01' OR D.CMT_CGMTP = 'SYS')";
			strSQLQRY2+= " and d.cmt_chp01 = 'DB' AND ((D.CMT_CGSTP='MRXXPTT' and substr(D.CMT_CODCD,1,1) in ('0','9') AND SUBSTR(D.CMT_CODCD,2,2)=PL_DOCTP) OR (D.CMT_CGSTP='MRXXOCN' AND D.CMT_CODCD=PL_DOCTP)) AND ";
			strSQLQRY2+= " ((PL_DOCVL-ifnull(PL_ADJVL,0)) > 0 OR A.PT_YOPVL > 0) AND (PL_DOCDT >= '01/07/2006' OR SUBSTR(PL_DOCNO,4,5)='77777') AND ifnull(PL_ADJVL,0) <= PL_DOCVL AND LENGTH(TRIM(IFNULL(PL_DOCNO,''))) = 8 and PL_PRTCD = '"+LP_STRCSTCD+"'"+LP_WHRSTR;
			strSQLQRY2+= " order by PL_DOCTP,PL_DOCNO";

			//System.out.println("strSQLQRY1"+strSQLQRY2);
			rstRSSET2 = cl_dat.exeSQLQRY2(strSQLQRY2);
			tblINVDL.setValueAt(tblDSTDL.getValueAt(tblDSTDL.getSelectedRow(),TB2_DSTNM).toString(),L_intROWCNT,TB3_DOCTP);
			tblINVDL.setValueAt(tblDSTDL.getValueAt(tblDSTDL.getSelectedRow(),TB2_CSTNM).toString(),L_intROWCNT,TB3_DOCNO);
			tblINVDL.setValueAt(tblDSTDL.getValueAt(tblDSTDL.getSelectedRow(),TB2_BALAM).toString(),L_intROWCNT,TB3_DOCDT);
			L_intROWCNT+=2;
			tblINVDL.setValueAt("DEBIT",L_intROWCNT,TB3_DOCTP);
			L_intROWCNT++;
			if(rstRSSET2!=null)
			{
				while(rstRSSET2.next())
				{
					tblINVDL.setValueAt(rstRSSET2.getString("CMT_CODDS"),L_intROWCNT,TB3_DOCTP);
					tblINVDL.setValueAt(rstRSSET2.getString("PL_DOCNO"),L_intROWCNT,TB3_DOCNO);	
					tblINVDL.setValueAt(fmtDMYDT.format(rstRSSET2.getDate("PL_DOCDT")),L_intROWCNT,TB3_DOCDT);
					tblINVDL.setValueAt(String.valueOf(setNumberFormat(rstRSSET2.getDouble("PL_DOCVL")/intCSTIN,3)),L_intROWCNT,TB3_DOCVL);
					tblINVDL.setValueAt(String.valueOf(setNumberFormat(rstRSSET2.getDouble("PL_ADJVL")/intCSTIN,3)),L_intROWCNT,TB3_ADJVL);
					tblINVDL.setValueAt(String.valueOf(setNumberFormat(rstRSSET2.getDouble("BALAM")/intCSTIN,3)),L_intROWCNT,TB3_BALAM);
					L_dblTOTAL_DB+=rstRSSET2.getDouble("BALAM");
					L_intROWCNT++;
				}
			}
			L_intROWCNT++;
			tblINVDL.setValueAt("TOTAL(DEBIT)",L_intROWCNT,TB3_ADJVL);
			tblINVDL.setValueAt(String.valueOf(setNumberFormat(L_dblTOTAL_DB/intCSTIN,3)),L_intROWCNT,TB3_BALAM);
			L_intROWCNT++;	
			//strSQLQRY2 = " SELECT CMT_CODDS,PL_DOCNO,PL_DOCDT,PL_DOCVL,PL_ADJVL,(ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) BALAM";
			//strSQLQRY2+= " from MR_PLTRN left outer join co_cdtrn on";
			//strSQLQRY2+= " CMT_CGMTP='SYS' and CMT_CGSTP='MRXXPTT' and CMT_CODCD=PL_DOCTP";
			//strSQLQRY2+= " where (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND PL_PRTTP='C' and PL_PRTCD = '"+LP_STRCSTCD+"' and  PL_CMPCD='"+M_strCMPCD_pbst+"' AND(PL_DOCTP LIKE  '0%' OR PL_DOCTP LIKE '1%')"+LP_WHRSTR;
			//strSQLQRY2+= " order by PL_DOCTP,PL_DOCNO";
			rstRSSET2.close();
			strSQLQRY2 = " SELECT  D.CMT_CODDS,PL_DOCNO,PL_DOCDT,PL_DOCVL,PL_ADJVL,(ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) BALAM ";
			strSQLQRY2+= " FROM MR_PLTRN,CO_PTMST A,CO_PTMST B,CO_CDTRN C,CO_CDTRN D";
			strSQLQRY2+= " WHERE PL_PRTTP='C' and PL_PRTTP=A.PT_PRTTP AND  PL_PRTCD=A.PT_PRTCD AND B.PT_PRTTP='D'  AND A.PT_DSRCD=B.PT_PRTCD  AND PL_CMPCD='01' AND PL_STSFL != 'X' AND  PL_SBSCD NOT IN ('111200') AND  C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MR00ZON' AND B.PT_ZONCD=C.CMT_CODCD  AND (D.CMT_CGMTP='D01' OR D.CMT_CGMTP = 'SYS')";
			strSQLQRY2+= " and d.cmt_chp01 = 'CR' AND ((D.CMT_CGSTP='MRXXPTT' and substr(D.CMT_CODCD,1,1) in ('0','9') AND SUBSTR(D.CMT_CODCD,2,2)=PL_DOCTP) OR (D.CMT_CGSTP='MRXXOCN' AND D.CMT_CODCD=PL_DOCTP)) AND ";
			strSQLQRY2+= " ((PL_DOCVL-ifnull(PL_ADJVL,0)) > 0 OR A.PT_YOPVL > 0) AND (PL_DOCDT >= '01/07/2006' OR SUBSTR(PL_DOCNO,4,5)='77777') AND ifnull(PL_ADJVL,0) <= PL_DOCVL AND LENGTH(TRIM(IFNULL(PL_DOCNO,''))) = 8 and PL_PRTCD = '"+LP_STRCSTCD+"'";
			strSQLQRY2+= " order by PL_DOCTP,PL_DOCNO";
			//System.out.println("strSQLQRY2"+strSQLQRY2);
			rstRSSET2 = cl_dat.exeSQLQRY2(strSQLQRY2);
			L_intROWCNT+=2;
			tblINVDL.setValueAt("CREDIT",L_intROWCNT,TB3_DOCTP);
			L_intROWCNT++;
			if(rstRSSET2!=null)
			{
				while(rstRSSET2.next())
				{
					tblINVDL.setValueAt(rstRSSET2.getString("CMT_CODDS"),L_intROWCNT,TB3_DOCTP);
					tblINVDL.setValueAt(rstRSSET2.getString("PL_DOCNO"),L_intROWCNT,TB3_DOCNO);	
					tblINVDL.setValueAt(fmtDMYDT.format(rstRSSET2.getDate("PL_DOCDT")),L_intROWCNT,TB3_DOCDT);
					tblINVDL.setValueAt(String.valueOf(setNumberFormat(rstRSSET2.getDouble("PL_DOCVL")/intCSTIN,3)),L_intROWCNT,TB3_DOCVL);
					tblINVDL.setValueAt(String.valueOf(setNumberFormat(rstRSSET2.getDouble("PL_ADJVL")/intCSTIN,3)),L_intROWCNT,TB3_ADJVL);
					tblINVDL.setValueAt(String.valueOf(setNumberFormat(rstRSSET2.getDouble("BALAM")/intCSTIN,3)),L_intROWCNT,TB3_BALAM);
					L_dblTOTAL_CR+=rstRSSET2.getDouble("BALAM");
					L_intROWCNT++;
				}
			}
			L_intROWCNT++;
			tblINVDL.setValueAt("TOTAL(CREDIT)",L_intROWCNT,TB3_ADJVL);
			tblINVDL.setValueAt(String.valueOf(setNumberFormat(L_dblTOTAL_CR/intCSTIN,3)),L_intROWCNT,TB3_BALAM);
			L_intROWCNT++;
			L_intROWCNT++;
			tblINVDL.setValueAt("TOTAL(DEBIT-CREDIT)",L_intROWCNT,TB3_ADJVL);
			tblINVDL.setValueAt(String.valueOf(setNumberFormat((L_dblTOTAL_DB-L_dblTOTAL_CR)/intCSTIN,3)),L_intROWCNT,TB3_BALAM);
			jtpRCVDL.setSelectedIndex(1);
			rstRSSET2.close();
		}
		catch(Exception E)
		{
			System.out.println("Inside exeDSTDSP() : "+E);
		}
		pnlDSTDL.setCursor(curDFSTS);
	}

	public void exeINVDSP_DAYCL(String LP_STRCSTCD)
	{
		try{
			pnlDSTDL.setCursor(curWTSTS);
			String strSQLQRY2;
			ResultSet rstRSSET2;			
			int L_intROWCNT=0;
			double L_dblTOTAL=0.0;
			strSQLQRY2 = " SELECT CMT_CODDS,PR_DOCNO,PR_DOCDT,PR_RCTVL BALAM";
			strSQLQRY2+= " from MR_PRTRN left outer join co_cdtrn on";
			strSQLQRY2+= " CMT_CGMTP='SYS' and CMT_CGSTP='MRXXPTT' and CMT_CODCD=PR_DOCTP";
			strSQLQRY2+= " where PR_RCTVL >0 AND PR_PRTTP='C' and PR_PRTCD = '"+LP_STRCSTCD+"' and PR_CMPCD='"+M_strCMPCD_pbst+"' and PR_DOCDT BETWEEN '"+fmtMDYDT.format(fmtDMYDT.parse(strYSTDT))+"' AND '"+fmtMDYDT.format(fmtDMYDT.parse(strYSTDT))+"' AND  ifnull(PR_STSFL,'') <> 'X'";
			strSQLQRY2+= " order by PR_DOCTP,PR_DOCNO";

			//System.out.println("exeINVDSP_DAYCL>>>>"+strSQLQRY2);
			rstRSSET2 = cl_dat.exeSQLQRY2(strSQLQRY2);
			tblINVDL.setValueAt(tblDSTDL.getValueAt(tblDSTDL.getSelectedRow(),TB2_DSTNM).toString(),L_intROWCNT,TB3_DOCTP);
			tblINVDL.setValueAt(tblDSTDL.getValueAt(tblDSTDL.getSelectedRow(),TB2_CSTNM).toString(),L_intROWCNT,TB3_DOCNO);
			tblINVDL.setValueAt(tblDSTDL.getValueAt(tblDSTDL.getSelectedRow(),TB2_BALAM).toString(),L_intROWCNT,TB3_DOCDT);
			L_intROWCNT+=2;
			L_intROWCNT++;
			if(rstRSSET2!=null)
			{
				while(rstRSSET2.next())
				{
					tblINVDL.setValueAt(rstRSSET2.getString("CMT_CODDS"),L_intROWCNT,TB3_DOCTP);
					tblINVDL.setValueAt(rstRSSET2.getString("PR_DOCNO"),L_intROWCNT,TB3_DOCNO);	
					tblINVDL.setValueAt(rstRSSET2.getString("PR_DOCDT"),L_intROWCNT,TB3_DOCDT);
					tblINVDL.setValueAt(String.valueOf(setNumberFormat(rstRSSET2.getDouble("BALAM")/intCSTIN,3)),L_intROWCNT,TB3_DOCVL);
					L_dblTOTAL+=rstRSSET2.getDouble("BALAM");
					L_intROWCNT++;
				}
			}
			L_intROWCNT++;
			tblINVDL.setValueAt("TOTAL",L_intROWCNT,TB3_DOCDT);
			tblINVDL.setValueAt(String.valueOf(setNumberFormat(L_dblTOTAL/intCSTIN,3)),L_intROWCNT,TB3_DOCVL);
			L_intROWCNT++;	
			jtpRCVDL.setSelectedIndex(1);	
			rstRSSET2.close();
		}
		catch(Exception E)
		{
			System.out.println("Inside exeDSTDSP() : "+E);
		}
		pnlDSTDL.setCursor(curDFSTS);
	}

	public void exeZONDSP(double[] LP_ARRDBL)
	{
		Double L_dblTOTAL=0.0;
		tblZONDL.setValueAt(String.valueOf(setNumberFormat((LP_ARRDBL[1]/intCSTIN),3)),TB1_NORTH,TB1_BALAM);
		tblZONDL.setValueAt(String.valueOf(setNumberFormat((LP_ARRDBL[2]/intCSTIN),3)),TB1_EAST,TB1_BALAM);
		tblZONDL.setValueAt(String.valueOf(setNumberFormat((LP_ARRDBL[3]/intCSTIN),3)),TB1_SOUTH,TB1_BALAM);
		tblZONDL.setValueAt(String.valueOf(setNumberFormat((LP_ARRDBL[4]/intCSTIN),3)),TB1_WEST,TB1_BALAM);
		tblZONDL.setValueAt(String.valueOf(setNumberFormat((LP_ARRDBL[5]/intCSTIN),3)),TB1_CENTRAL,TB1_BALAM);
		//tblZONDL.setValueAt(String.valueOf(setNumberFormat((LP_ARRDBL[6]/intCSTIN),3)),TB1_EXPORT,TB1_BALAM);

		L_dblTOTAL=(LP_ARRDBL[1]/intCSTIN)+(LP_ARRDBL[2]/intCSTIN)+(LP_ARRDBL[3]/intCSTIN)+(LP_ARRDBL[4]/intCSTIN)+(LP_ARRDBL[5]/intCSTIN)+(LP_ARRDBL[6]/intCSTIN);
		tblZONDL.setValueAt("TOTAL",TB1_EXPORT+1,TB1_DTAIL);
		tblZONDL.setValueAt(String.valueOf(setNumberFormat(L_dblTOTAL,3)),TB1_EXPORT+1,TB1_BALAM);
	}

	


	public void exeDSTDSP(String LP_ZONCD,Vector<String> LP_VTR_DB,Vector<String> LP_VTR_CR)	{
		String [] staDATA = null; 
		String [] staDATA2=null;
		boolean L_DAYCL=false;
		int L_intROWCNT=0;
		double L_dblTOTAL_DB=0.0;
		double L_dblTOTAL_CR=0.0;
		double L_dblTOTAL_DIFF=0.0;
		Vector<String> L_VTRDBCR=new Vector<String>();

		//copy DB to VTRDBCR
		for(int i=0;i<LP_VTR_DB.size();i++)
		{  if(LP_VTR_DB.get(i).toString().substring(0,2).equals(LP_ZONCD))   
			L_VTRDBCR.add(LP_VTR_DB.get(i));
		}

		for(int i=0;i<tblCATDL.getRowCount();i++)
		{
			if(tblCATDL.getValueAt(i,TB_CHKFL).toString().equals("true"))
			{
				if(i==TB_DAYCL)
					L_DAYCL=true;
			}
		}
		//tblDSTDL.scrollRectToVisible(getCellRect(0,0,false));
		/*if(L_DAYCL != true)
		{		
			tblDSTDL.setValueAt("DEBIT",L_intROWCNT,TB2_DSTNM);
			L_intROWCNT+=2;		}*/

		/////////////////////////modification///////////////////
		int tmp=0;
		matchCR=new int[LP_VTR_CR.size()]; //record unmatched position in VTR_CR
		matchDBCR=new int[L_VTRDBCR.size()];
		int flag=0;//not matching 

		///it searches for the matching record and addes CR amt to the existing string in vector
		for(int i=0;i<LP_VTR_CR.size();i++)
		{ if(LP_VTR_CR.get(i).toString().substring(0,2).equals(LP_ZONCD))   
		{
			flag=0;
			staDATA=null;
			staDATA = LP_VTR_CR.get(i).replace('|','~').split("~");
			for(int j=0;j<L_VTRDBCR.size();j++)
			{				
				staDATA2=L_VTRDBCR.get(j).replace('|','~').split("~");
				if((LP_VTR_CR.get(i).toString().substring(0,2).equals(LP_ZONCD)))//compare vector to this  main combo option)//compare vector to this  main combo option
				{

					if(staDATA[4].trim().equalsIgnoreCase(staDATA2[4]))
					{	flag=1;  //mathced CR and DB rec
					matchDBCR[j]=1;//1 indiactes matched position in CR
					tmp=j;
					//String L_strCR=String.valueOf(setNumberFormat((Double.parseDouble(LP_VTR_CR.get(i))/intCSTIN),3));
					String str=L_VTRDBCR.get(j).toString().concat("|"+LP_VTR_CR.get(i).replace('|','~').split("~")[3]);

					//System.out.println("*****"+str);//to check new vtr
					L_VTRDBCR.removeElementAt(j);///??????????????????????????????????????????//
					L_VTRDBCR.add(j,str);
					}
				}		

			}
			if(flag==1)
			{
				matchCR[i]=1;//1 indiactes matched position in CR 

			}
		}
		}//search for common record ends


		///for testing matching records from VTRDBCR and non match from VTRCR
		/*for(int i=0;i<LP_VTR_CR.size();i++)/////////////////////////chk here as CR added is not shown
		{
			if(matchCR[i]==0)//1 indiactes matched 
				System.out.println("????"+LP_VTR_CR.get(i));
		}
		for(int i=0;i<L_VTRDBCR.size();i++)/////////////////////////chk here as CR added is not shown
		{
			if(matchDBCR[i]==1)//1 indiactes matched 
			System.out.println("<<<,"+L_VTRDBCR.get(i));
		}*/

////////////////////last added code

		for(int i=0;i<L_VTRDBCR.size();i++)  ///to display in the table all values from vector VTRDBCR
		{
			if(L_VTRDBCR.get(i).toString().substring(0,2).equals(LP_ZONCD))   
			{
				if(matchDBCR[i]==1)//matched rec in DBCR vector
				{

					staDATA = L_VTRDBCR.get(i).replace('|','~').split("~");
					//System.out.println("/////"+L_VTRDBCR.get(i));
					tblDSTDL.setValueAt(staDATA[1],L_intROWCNT,TB2_DSTNM);
					tblDSTDL.setValueAt(staDATA[2],L_intROWCNT,TB2_CSTNM);
					Double L_dblDB=Double.parseDouble(staDATA[3]);
					String L_strDB=String.valueOf(setNumberFormat((Double.parseDouble(staDATA[3])/intCSTIN),3));
					tblDSTDL.setValueAt(L_strDB,L_intROWCNT,TB2_BALAM);
					tblDSTDL.setValueAt(staDATA[4],L_intROWCNT,TB2_CSTCD);
					Double L_dblCR=Double.parseDouble(staDATA[5]);
					String L_strCR=String.valueOf(setNumberFormat((Double.parseDouble(staDATA[5])/intCSTIN),3));
					tblDSTDL.setValueAt(L_strCR,L_intROWCNT,TB2_CR);
					Double L_dblDIFF=L_dblDB-L_dblCR;				
					String L_strDIFF=String.valueOf(setNumberFormat((Double.parseDouble(L_strDB)-Double.parseDouble(L_strCR)),3));
					tblDSTDL.setValueAt(String.valueOf(setNumberFormat((Double.parseDouble(L_strDB)-Double.parseDouble(L_strCR)),3)),L_intROWCNT,TB2_DIFF);
					L_dblTOTAL_DB+=L_dblDB;
					L_dblTOTAL_CR+=L_dblCR;
					L_dblTOTAL_DIFF+=L_dblDIFF;
					L_intROWCNT++;
				}
				if(matchDBCR[i]==0)//not matching record from DBCR
				{ 
					staDATA2=L_VTRDBCR.get(i).replace('|','~').split("~");
					tblDSTDL.setValueAt(staDATA2[1],L_intROWCNT,TB2_DSTNM);
					tblDSTDL.setValueAt(staDATA2[2],L_intROWCNT,TB2_CSTNM);
					Double L_dblDB=Double.parseDouble(staDATA2[3]);
					String L_strDB=String.valueOf(setNumberFormat((Double.parseDouble(staDATA2[3])/intCSTIN),3));
					tblDSTDL.setValueAt(L_strDB,L_intROWCNT,TB2_BALAM);
					tblDSTDL.setValueAt(staDATA2[4],L_intROWCNT,TB2_CSTCD);
					String L_strCR="0.0";
					tblDSTDL.setValueAt(L_strCR,L_intROWCNT,TB2_CR);//put 0 in CR col
					String L_strDIFF=String.valueOf(setNumberFormat((Double.parseDouble(L_strDB)-Double.parseDouble(L_strCR)),3));
					Double L_dblDIFF=L_dblDB-Double.parseDouble(L_strCR);
					tblDSTDL.setValueAt(String.valueOf(setNumberFormat((Double.parseDouble(L_strDB)-Double.parseDouble(L_strCR)),3)),L_intROWCNT,TB2_DIFF);
					L_dblTOTAL_DB+=L_dblDB;
					L_dblTOTAL_DIFF+=L_dblDIFF;
					L_intROWCNT++;


				}
			}

		}

		for(int i=0;i<LP_VTR_CR.size();i++)
		{if(LP_VTR_CR.get(i).toString().substring(0,2).equals(LP_ZONCD))   
		{
			if(matchCR[i]==0)//not matching record from CR
			{
				//staDATA2=null;
				staDATA2 = LP_VTR_CR.get(i).replace('|','~').split("~");
				tblDSTDL.setValueAt(staDATA2[1],L_intROWCNT,TB2_DSTNM);
				tblDSTDL.setValueAt(staDATA2[2],L_intROWCNT,TB2_CSTNM);
				Double L_dblCR=Double.parseDouble(staDATA2[3]);
				String L_strCR=String.valueOf(setNumberFormat((Double.parseDouble(staDATA2[3])/intCSTIN),3));
				tblDSTDL.setValueAt(L_strCR,L_intROWCNT,TB2_CR);
				tblDSTDL.setValueAt(staDATA2[4],L_intROWCNT,TB2_CSTCD);
				String L_strDB="0.0";
				tblDSTDL.setValueAt(L_strDB,L_intROWCNT,TB2_BALAM);//put 0 in DB col
				Double L_dblDIFF=Double.parseDouble(L_strDB)-L_dblCR;

				String L_strDIFF=String.valueOf(setNumberFormat((Double.parseDouble(L_strDB)-Double.parseDouble(L_strCR)),3));

				tblDSTDL.setValueAt(String.valueOf(setNumberFormat((Double.parseDouble(L_strDB)-Double.parseDouble(L_strCR)),3)),L_intROWCNT,TB2_DIFF);
				L_dblTOTAL_CR+=L_dblCR;
				L_dblTOTAL_DIFF+=L_dblDIFF;
				L_intROWCNT++;				

			}		
		}
		}

		tblDSTDL.setValueAt("TOTAL",L_intROWCNT+2,TB2_CSTNM);
		tblDSTDL.setValueAt(String.valueOf(setNumberFormat(L_dblTOTAL_DB/intCSTIN,3)),L_intROWCNT+2,TB2_BALAM);
		tblDSTDL.setValueAt(String.valueOf(setNumberFormat(L_dblTOTAL_CR/intCSTIN,3)),L_intROWCNT+2,TB2_CR);
		tblDSTDL.setValueAt(String.valueOf(setNumberFormat(L_dblTOTAL_DIFF/intCSTIN,3)),L_intROWCNT+2,TB2_DIFF);

		/*
		for(int i=0;i<LP_VTR_CR.size();i++)
		{
			if(matchCR[i]==0)//not matching record from CR
			{staDATA2=null;
			staDATA2 = LP_VTR_CR.get(i).replace('|','~').split("~");
			System.out.print("LLLLLL  :"+LP_VTR_CR.get(i));
			}
		}
		 */	


	}	

	public void mouseReleased(MouseEvent L_ME)	{		try
		{
		}
		catch(Exception e){System.out.println("inside mousereleased() : "+L_ME);}
	}
	public void mouseExited(MouseEvent L_ME)	{		try
		{
		}
		catch(Exception e){System.out.println("inside mouseexited() : "+L_ME);}
	}
	public void mouseEntered(MouseEvent L_ME)	{		try
		{
		}
		catch(Exception e){}
	}

	/**
	 * <b>TASKS : </b><br>
	 * Reset System idle time timer at every click<br>
	 * Copy refernce of soure to M_objSOURC
	 */	public void mouseClicked(MouseEvent L_ME)	{		try
		{
		}
		catch(Exception L_EX){}
	}

	private void opnDBCON(){
		try{
			//cl_dat.ocl_dat.M_conSPDBA_pbst = cl_dat.ocl_dat.setCONDTB("01","spldata","FIMS","FIMS");
			setCONACT("01","spldata","FIMS","FIMS");		
			if(cl_dat.M_conSPDBA_pbst != null){
				cl_dat.M_stmSPDBA_pbst = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
				cl_dat.M_stmSPDBQ_pbst  = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
				cl_dat.M_stmSPDBQ_pbst1 = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			}

		}catch(Exception L_EX){
			System.out.println("opnDBCON: "+L_EX);
		}
	}

	private void clsDBCON(){
		try{
			if(cl_dat.M_conSPDBA_pbst != null){
				cl_dat.M_conSPDBA_pbst.commit();
				cl_dat.M_stmSPDBA_pbst.close();
				cl_dat.M_stmSPDBQ_pbst.close();
				cl_dat.M_stmSPDBQ_pbst1.close();
				cl_dat.M_conSPDBA_pbst.close();
			}
		}catch(Exception L_EX){
			System.out.println("clsDBCON: "+L_EX);
		}
	}

	public  void setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX)
	{
		try
		{
			cl_dat.M_conSPDBA_pbst = this.setCONDTB(LP_SYSLC,LP_XXLBX, LP_XXUSX, LP_XXPWX);
			if(cl_dat.M_conSPDBA_pbst != null)
			{
				cl_dat.M_conSPDBA_pbst.rollback();
				cl_dat.M_stmSPDBA_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
				cl_dat.M_stmSPDBQ_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
				cl_dat.M_stmSPDBQ_pbst1 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
                cl_dat.M_stmSPDBQ_pbst2 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
                cl_dat.M_stmSPDBQ_pbst3 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
			}
		}
		catch(Exception L_EX)
		{
			System.out.println("Error in setCONTACT"+L_EX);
		}
	}

	/** Establishes connection to DB.
	 * Called internally from
	 * {@link  fr_log#setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX) }
	 */
	private  Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
	{
		Connection LM_CONDTB=null;
		try
		{       String L_STRURL = "", L_STRDRV = "";
		if(LP_SYSLC.equals("01"))
		{
			L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
			L_STRURL = "jdbc:as400://SPLWS01/";
			Class.forName(L_STRDRV);
		}
		else if(LP_SYSLC.equals("02"))
		{
			int port = 50000;
			LP_DTBUS = "SPLDATA";
			LP_DTBPW = "SPLDATA";

			L_STRURL = "jdbc:db2://" + "splhos01" + ":" + 50000 + "/" ;
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		}
		else if(LP_SYSLC.equals("03"))
		{
			L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
			L_STRURL = "jdbc:as400://MUMAS2/";
			Class.forName(L_STRDRV);
		}
		L_STRURL = L_STRURL + LP_DTBLB;
		LM_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);

		if(LM_CONDTB == null)
			return null;
		LM_CONDTB.setAutoCommit(false);

		SQLWarning L_STRWRN = LM_CONDTB.getWarnings();
		if ( L_STRWRN != null )
			System.out.println("Warning in setCONDTB : "+L_STRWRN);
		return LM_CONDTB;
		}
		catch(java.lang.Exception L_EX)
		{
			System.out.println("setCONDTB" + L_EX.toString());
			return null;
		}

	}

	public Statement chkCONSTM(Connection LP_CONVAL,String LP_QRYTP){
		try{
			if (LP_CONVAL != null){
				if(LP_QRYTP.equals("Q"))	
					return LP_CONVAL.createStatement();
				else if(LP_QRYTP.equals(""))
					return LP_CONVAL.createStatement();
			}
		}catch(Exception L_EX){}
		return null;			
	}

	public static void main(String args[])
	{
		try
		{
			mr_vwdsb omr_vwdsb = new mr_vwdsb();
			omr_vwdsb.setVisible(true);
		}
		catch(Exception E)
		{
			System.out.println("inside main() : "+E);
		} 
	}



	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			if(L_AE.getSource() == btnEXIT)
			{	
				clsDBCON();
				System.exit(0);
			}
			else if(L_AE.getSource() == btnGRAPH)
			{	
				if(jtpMANTAB.getTitleAt(jtpMANTAB.getSelectedIndex()).toString().equals("Sales Analysis"))
				{
					String strXVAL = "";
					String strPRDQT = "";
					String strRCLQT = "";
					String strCPTQT = "";
					String strSALQT = "";
					String strRETQT = "";
					for(int i=3;i>=0;i--)
					{
						if(!strXVAL.equals(""))
							strXVAL += ",";
						strXVAL += tblSLSDL.getValueAt(i,TB4_YEAR).toString();
						
						if(!strPRDQT.equals(""))
							strPRDQT += ",";
						strPRDQT += tblSLSDL.getValueAt(i,TB4_PRDQT).toString();
						
						if(!strRCLQT.equals(""))
							strRCLQT += ",";
						strRCLQT += tblSLSDL.getValueAt(i,TB4_RCLQT).toString();
						
						if(!strCPTQT.equals(""))
							strCPTQT += ",";
						strCPTQT += tblSLSDL.getValueAt(i,TB4_CPTQT).toString();
						
						if(!strSALQT.equals(""))
							strSALQT += ",";
						strSALQT += tblSLSDL.getValueAt(i,TB4_SALQT).toString();
						
						if(!strRETQT.equals(""))
							strRETQT += ",";
						strRETQT += tblSLSDL.getValueAt(i,TB4_RETQT).toString();
					}
				
					cl_graphs.exeMUL_BAR_GRAPH("Year","Production Quantity (MT)",strXVAL,5,"Production Qty.",strPRDQT,"Recycled Qty.",strRCLQT,"Captive Consumption",strCPTQT,"Sales Qty.",strSALQT,"Sales Return Qty.",strRETQT);
				}
				else if(jtpMANTAB.getTitleAt(jtpMANTAB.getSelectedIndex()).toString().equals("FG Inventory"))
				{	
					String strXVAL = "";
					String strYVAL = "";
	
					for(int i=0;i<tblPRDDL_IVT.getRowCount();i++)
					{
						if(tblPRDDL_IVT.getValueAt(i,TB7_PRDDS).toString().length()>0 && !tblPRDDL_IVT.getValueAt(i,TB7_PRDDS).toString().equals("TOTAL"))
						{
							if(!strXVAL.equals(""))
							strXVAL += ",";
							strXVAL += tblPRDDL_IVT.getValueAt(i,TB7_PRDDS).toString().replace(',',' ');
						}
					}
					for(int i=0;i<tblPRDDL_IVT.getRowCount();i++)
					{
						if(tblPRDDL_IVT.getValueAt(i,TB7_PRDDS).toString().length()>0 && !tblPRDDL_IVT.getValueAt(i,TB7_PRDDS).toString().equals("TOTAL"))
						{
							if(!strYVAL.equals(""))
							strYVAL += ",";
							strYVAL += tblPRDDL_IVT.getValueAt(i,TB7_IVTCC).toString();
						}
					}
					cl_graphs.exeBAR_GRAPH("Product","Inventory Carrying Cost (Lacs)","FG Inventory",500,500,strXVAL,strYVAL);
				}
				else if(jtpMANTAB.getTitleAt(jtpMANTAB.getSelectedIndex()).toString().equals("Stores Inventory"))
				{	
					String strXVAL = "";
					String strYVAL = "";
	
					//JTable tblSTRDL1;
					//final int TB_GRPDS_ST =2; 
					//final int TB_IVTCC_ST =4;
					for(int i=0;i<tblSTRDL1.getRowCount();i++)
					{
						if(tblSTRDL1.getValueAt(i,TB_GRPDS_ST).toString().length()>0 && !tblSTRDL1.getValueAt(i,TB_GRPDS_ST).toString().equals("TOTAL"))
						{
							if(!strXVAL.equals(""))
							strXVAL += ",";
							strXVAL += tblSTRDL1.getValueAt(i,TB_GRPDS_ST).toString().replace(',',' ');
						}
					}
					for(int i=0;i<tblSTRDL1.getRowCount();i++)
					{
						if(tblSTRDL1.getValueAt(i,TB_GRPDS_ST).toString().length()>0 && !tblSTRDL1.getValueAt(i,TB_GRPDS_ST).toString().equals("TOTAL"))
						{
							if(!strYVAL.equals(""))
							strYVAL += ",";
							strYVAL += tblSTRDL1.getValueAt(i,TB_IVTCC_ST).toString();
						}
					}
					cl_graphs.exeBAR_GRAPH("Group","Inventory Carrying Cost (Lacs)","Stores Inventory",1300,700,strXVAL,strYVAL);
				}
			}
		}	
		catch(Exception E)
		{
			System.out.println("Inside actionPerformed() : "+E);
		}
	}

	/*private void exeMUL_BAR_GRAPH(String LP_XVAL,int LP_TOTVL,String LP_STR1,String LP_VAL1,String LP_STR2,String LP_VAL2,String LP_STR3,String LP_VAL3,String LP_STR4,String LP_VAL4,String LP_STR5,String LP_VAL5)
	{
		FileOutputStream F_OUT ;
		DataOutputStream D_OUT ;
		
		try
		{
			F_OUT=new FileOutputStream("C:\\reports\\graph.html");
			D_OUT=new DataOutputStream(F_OUT); 
			
		
			D_OUT.writeBytes("<applet code='swiftchart.class' width='500' height='500'>");
			D_OUT.writeBytes("<param name='chart_type' value='bar'>");
			D_OUT.writeBytes("<param name='x_axis_font_orientation' value='HORIZONTAL'>");
			D_OUT.writeBytes("<param name='applet_bg' value='666699'>");
			D_OUT.writeBytes("<param name='chart_bg' value='CC9900'>");
			D_OUT.writeBytes("<param name='title_text' value='Horizontal bar chart with labels'>");
			D_OUT.writeBytes("<param name='title_font_size' value='18'>");
			D_OUT.writeBytes("<param name='x_axis_font_size' value='12'>");
			D_OUT.writeBytes("<param name='y_axis_font_size' value='12'>");
			D_OUT.writeBytes("<param name='legend_position' value='TOP'>");
			D_OUT.writeBytes("<param name='legend_border_color' value='CCDDFF'>");
			D_OUT.writeBytes("<param name='legend_font_size' value='12'>");
			D_OUT.writeBytes("<param name='data_value' value='NONE'>");
			D_OUT.writeBytes("<param name='data_value_font_color' value='000000'>");
			D_OUT.writeBytes("<param name='data_value_font_size' value='12'>");
			D_OUT.writeBytes("<param name='grid_line_hor' value='N'>");
			D_OUT.writeBytes("<param name='grid_line_ver' value='N'>");
			D_OUT.writeBytes("<param name='x_value' value='"+LP_XVAL+"'>");
			int x = 0;
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s1_value' value='"+LP_VAL1+"'>");
				D_OUT.writeBytes("<param name='s1_label' value='"+LP_STR1+"'>");
				D_OUT.writeBytes("<param name='s1_color' value='993300'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s2_value' value='"+LP_VAL2+"'>");
				D_OUT.writeBytes("<param name='s2_label' value='"+LP_STR2+"'>");
				D_OUT.writeBytes("<param name='s2_color' value='009933'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s3_value' value='"+LP_VAL3+"'>");
				D_OUT.writeBytes("<param name='s3_label' value='"+LP_STR3+"'>");
				D_OUT.writeBytes("<param name='s3_color' value='003399'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s4_value' value='"+LP_VAL4+"'>");
				D_OUT.writeBytes("<param name='s4_label' value='"+LP_STR4+"'>");
				D_OUT.writeBytes("<param name='s4_color' value='003300'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s5_value' value='"+LP_VAL5+"'>");
				D_OUT.writeBytes("<param name='s5_label' value='"+LP_STR5+"'>");
				D_OUT.writeBytes("<param name='s5_color' value='330000'>");
				x++;
			}
			x=0;
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s1_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s2_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s3_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s4_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s5_bar_fill' value='3'>");
				x++;
			}
			D_OUT.writeBytes("<param name='chart_bg_fill' value='5'>");
			D_OUT.writeBytes("<param name='bg_fill' value='4'>");
			D_OUT.writeBytes("</applet>");
				
			Runtime r = Runtime.getRuntime();
			Process p = null;					    
			p  = r.exec("c:\\windows\\iexplore.exe C:\\reports\\graph.html"); 
		}
		catch(Exception E)
		{
			System.out.println(E);
		}
	}
	
	private void exeBAR_GRAPH(String LP_TITLE,String LP_SUB,int LP_WIDTH,int LP_HEIGHT,String LP_XVAL,String LP_YVAL)
	{
		FileOutputStream F_OUT ;
		DataOutputStream D_OUT ;
		
		try
		{
			F_OUT=new FileOutputStream("C:\\reports\\graph.html");
			D_OUT=new DataOutputStream(F_OUT); 
			
			D_OUT.writeBytes("<applet code='swiftchart.class' width='"+LP_WIDTH+"' height='"+LP_HEIGHT+"'>");
			D_OUT.writeBytes("<param name='x_axis_font_orientation' value='RIGHT'>");
			D_OUT.writeBytes("<param name='chart_type' value='bar'>");
			D_OUT.writeBytes("<param name='applet_bg' value='EEEEEE'>");
			D_OUT.writeBytes("<param name='chart_bg' value='66CCFF'>");
			D_OUT.writeBytes("<param name='title_text' value='Bar chart with labels'>");
			D_OUT.writeBytes("<param name='title_font_size' value='18'>");
			D_OUT.writeBytes("<param name='title_sub1_text' value='"+LP_SUB+"'>");
			D_OUT.writeBytes("<param name='legend_position' value='NONE'>");
			D_OUT.writeBytes("<param name='data_value_font_size' value='10'>");
			D_OUT.writeBytes("<param name='data_value_font_orientation' value='right'>");
			D_OUT.writeBytes("<param name='data_value' value='inside'>");
			D_OUT.writeBytes("<param name='data_value_font_color' value='990000'>");
			D_OUT.writeBytes("<param name='grid_line_hor' value='Y'>");
			D_OUT.writeBytes("<param name='grid_line_hor_type' value='0'>");
			D_OUT.writeBytes("<param name='x_value' value='"+LP_XVAL+"'>");
			D_OUT.writeBytes("<param name='s1_value' value='"+LP_YVAL+"'>");
			D_OUT.writeBytes("<param name='s1_label' value='Serie 1'>");
			D_OUT.writeBytes("<param name='s1_color' value='FFCC00'>");
			D_OUT.writeBytes("<param name='x_axis_title' value='Y'>");
			D_OUT.writeBytes("<param name='x_axis_title_text' value='"+LP_TITLE+"'>");
			D_OUT.writeBytes("<param name='x_axis_title_font_style' value='BOLD'>");
			D_OUT.writeBytes("<param name='y_unit' value='500'>");
			D_OUT.writeBytes("/applet>");

				
			Runtime r = Runtime.getRuntime();
			Process p = null;					    
			p  = r.exec("c:\\windows\\iexplore.exe C:\\reports\\graph.html"); 
		}
		catch(Exception E)
		{
			System.out.println(E);
		}
	}
	*/
	
	protected String setNumberFormat(double P_dblNUMBR,int P_intFRCTN)
	{
		NumberFormat L_fmtNUMFM=NumberFormat.getNumberInstance();
		L_fmtNUMFM.setMaximumFractionDigits(P_intFRCTN);
		L_fmtNUMFM.setMinimumFractionDigits(P_intFRCTN);
		String L_strTEMP=(L_fmtNUMFM.format(P_dblNUMBR));
		StringTokenizer L_stkSTTKN=new StringTokenizer(L_strTEMP,",");
		L_strTEMP="";
		while(L_stkSTTKN.hasMoreTokens())
		{
			L_strTEMP+=L_stkSTTKN.nextToken();
		}
		return L_strTEMP;
	}


	/*
	 *Cretaes JTable on the passed Panel
	 */
	public JTable crtTBLPNL1(JPanel LP_TBLPNL,String[] LP_COLHD,int LP_ROWCNT,int LP_XPOS,int LP_YPOS,int LP_WID,int LP_HGT,int[] LP_ARRGSZ,int LP_CHKCOL[]){ 
		try{
			cl_tab2 L_TBLOBJ1; 
			JPanel pnlTAB1 = new JPanel();

			// Object[][] L_TBLDT1;
			//L_TBLDT1 = crtTBLDAT(LP_ROWCNT,LP_COLHD.length); // Creating the Object Data

			int LP_COLCNT=LP_COLHD.length;
			Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];
			L_TBLDT = crtTBLDAT(LP_ROWCNT,LP_COLHD.length);
			for(int j = 0;j < LP_ROWCNT;j++)
			{
				for(int i=0;i<LP_COLCNT;i++)
				{
					L_TBLDT[j][i]="";
					for(int k=0;k<LP_CHKCOL.length;k++)
					{
						if(i==LP_CHKCOL[k])
							L_TBLDT[j][i]=new Boolean(false);
					}

				}
			}
			L_TBLOBJ1 = new cl_tab2(L_TBLDT,LP_COLHD);
			JTable L_TBL1 = new JTable(L_TBLOBJ1); 
			L_TBL1.setBackground(new Color(213,213,255));
			int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			JScrollPane jspTBL1 = new JScrollPane(L_TBL1,v1,h1);
			jspTBL1.setPreferredSize(new Dimension(LP_WID-25,LP_HGT-25));			jspTBL1.setLocation(0,100);
			pnlTAB1.removeAll();
			setCOLWDT(L_TBL1,LP_COLHD,LP_ARRGSZ);
			pnlTAB1.add(jspTBL1);
			pnlTAB1.setSize(LP_WID,LP_HGT);
			pnlTAB1.setLocation(LP_XPOS,LP_YPOS);
			LP_TBLPNL.add(pnlTAB1);
			LP_TBLPNL.updateUI();
			return L_TBL1;
		}catch(Exception L_EX){
			System.out.println("crtTBLPNL1 "+L_EX);
		}
		return null;
	}


	public void setCOLWDT(JTable LP_TBLNM,String[] LP_TBLHDG,int[] LP_WID){
		for(int i=0;i<LP_TBLHDG.length;i++){
			TableColumn L_TBLCOL = LP_TBLNM.getColumn(LP_TBLHDG[i]);
			if(LP_WID[i] !=0)
				L_TBLCOL.setPreferredWidth(LP_WID[i]);
		}
	}


	public Object[][] crtTBLDAT(int LP_ROWCNT,int LP_COLCNT){
		int i = 0;
		Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];;
		for(int j = 0;j < LP_ROWCNT;j++){
			i = 0;
			for( int k = i;k < LP_COLCNT;k++){
				L_TBLDT[j][k] = "";
			}
		}
		return L_TBLDT;
	}


	/**Clears contents of table	 */
	public void clrTABLE(JTable P_TBL)
	{
		P_TBL.setRowSelectionInterval(0,0);
		P_TBL.setColumnSelectionInterval(0,0);
		P_TBL.scrollRectToVisible(P_TBL.getCellRect(0,0,false));
		for(int i=0;i<P_TBL.getRowCount();i++)
		{
			if(P_TBL.getValueAt(i,0).toString().equals("true"))
				P_TBL.setValueAt(new Boolean(false),i,0);
		}

		for(int i=1;i<P_TBL.getColumnCount();i++)
		{
			for(int j=0;j<P_TBL.getRowCount();j++)
			{
				P_TBL.setValueAt("",j,i);
			}
		}
	}
	private  void  exeSORT(Vector<String> LP_VTR)
	{
	    try
	    {
	 	   for(int i=0; i<LP_VTR.size();i++)
	 	   {
	 		  for(int j=i+1;j<LP_VTR.size();j++)
	 		  {
	 			 if(LP_VTR.get(j).toString().compareTo(LP_VTR.get(i).toString())<1)
	 			 {
	 				 String L_strTEMP = LP_VTR.get(j).toString();
	 				 LP_VTR.set(j,LP_VTR.get(i));
	 				 LP_VTR.set(i,L_strTEMP);
	 			 } 	   
	 		  }
	 	   }   
	    }
	    catch(Exception L_E)
	    {
			System.out.println("exeSORT : "+L_E);
	    }  
	}
}	

class mr_vwdsb_RightTableCellRenderer extends DefaultTableCellRenderer {    
	  protected  mr_vwdsb_RightTableCellRenderer() {   
	    setHorizontalAlignment(JLabel.RIGHT);  }    
	     
	}   

class mr_vwdsb_CenterTableCellRenderer extends DefaultTableCellRenderer {    
	  protected  mr_vwdsb_CenterTableCellRenderer() {   
	    setHorizontalAlignment(JLabel.CENTER);  }    
	     
	}   



