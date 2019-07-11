/**
System Name:Human Resource System.
 
Program Name: Production Request Authorization
Author : SSG
Purpose : This module use for entering & monitoring various status of Production Request.
 
Source Directory: f:\source\splerp3\pr_terqa.java                         
Executable Directory: F:\exec\splerp3\pr_terqa.class

 
List of tables used:
Table Name		Primary key											Operation done
															Insert	Update	   Query    Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
PR_RQTRN		RQ_DOCNO, RQ_CMPCD                            	  /           /         
                 
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on screen:
Field Name		Column Name		Table name		Type/Size	Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtDOCNO;       RQ_DOCNO
txtPRDDS;		RQ_PRDDS
txtPRDCT;		RQ_PRDCT
txtPRTNM;		RQ_PRTNM
txtREQQT; 		RQ_REQQT
txtDOCDT;  		RQ_DOCDT
txtSTSFL; 		RQ_STSFL
txtDCLDT; 		RQ_DCLDT
txtLINNO; 		RQ_LINNO
txtEDLDT; 		RQ_EDLDT
txtLOTNO;		RQ_LOTNO
txtPLCDT; 		RQ_PLCDT
txtRMCDT;		RQ_RMCDT
txtREMDS;		RQ_REMDS
-----------------------------------------------------------------------------------------------------------------------------------------------------


List of fields with help facility: 
Field Name	Display Description		    Display Columns		Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtLINNO    Line No                     CMT_CODCD       CO_CDTRN    
														CMT_CGMTP='SYS',
														CMT_CGSTP='PRXXLIN',
														CMT_CCSVL like '02_%'
-----------------------------------------------------------------------------------------------------------------------------------------------------


Validations & Other Information:
    - Enter Valid Status & Line No.
  	- If enter declaration date ,while enter Line no.
 	- If enter delivery date ,while enter Lot no.
 	- declaration & Delivery Date should not be greater than PR Received on Date.
 	- Delivery Date should not be greater than current Date.
 	
    
Requirements:
	- Genereted month wise PR Received first tab.
	1)In first tab display -Registered,cleared for prodn,Plan clearance,under prodn,RM clearance & under testing status.
	2)In Second tab-on hold,Planning & cancelled Status will be display.
	3)In third tab display pending for RM Status 
	4)In forth tab display completed PR.
	- While Select Plan clearance,cleared for prodn & RM clearance Status,it will be display Plan, Cleared for Prodn & RM clearance Date (i.e.current date)Resp which can be modify.
	- Display Marketing Remark  & production Remark of selected Row from pr_rmmst, pr_rqtrn table resp. 
	- Provision for generating Production Request status report.
	- Highlight Registered,Plan clearance & RM clearance Status with red color,Under Prodn Status with blue & Cancelled & under testing with gray color.

**/



import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;
import java.io.DataOutputStream;
import java.io.FileOutputStream;


class pr_terqa extends cl_pbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"pr_rprqa.html";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    
    private JTextField txtPLCQT1,txtPRCQT1,txtDCLQT1;
    JLabel lblREQQT,lblPLCQT,lblPRCQT,lblDCLQT,lblQTYVL,lblERMSG;
	private JLabel lblREMRK_PR,lblREMRK1_PR,lblREMRK2_PR,lblREMRK3_PR;
	private JLabel lblREMRK_MR,lblREMRK1_MR,lblREMRK2_MR,lblREMRK3_MR;
	
	private  cl_JTable tblRQREC,tblRQPEN,tblRMPEN,tblRQCMP,tblPRCQT,tblPLCQT,tblDCLQT;

	private TBLINPVF objTBLVRF;
	
	private JTabbedPane jtpMANTAB;                        
	private JPanel pnlRQREC,pnlRQPEN,pnlRMPEN,pnlRQCMP,pnlQTYVL; 
	
	private JRadioButton  rdbDOCNO,rdbSTSFL;
	private ButtonGroup   btgREPDL;
	
	private Hashtable<String,String> hstSTSDS;
	private Hashtable<String,String> hstSTSCD;
	private Hashtable<String,String> hstMODLS;
	private Hashtable<String,String> hstQTYVL;
		
	private JComboBox cmbSTSFL1,cmbSTSFL2,cmbSTSFL3;
	private JCheckBox chkREPFL;
	
    int TB1_CHKFL = 0; 				JCheckBox chkCHKFL,chkCHKFL1,chkCHKFL2,chkCHKFL3;
	int TB1_DOCNO = 1;              JTextField txtDOCNO;
	int TB1_PRDTP = 2;              JTextField txtPRDTP;
	int TB1_PRDDS = 3;              JTextField txtPRDDS;
	int TB1_PRDCT = 4;              JTextField txtPRDCT;
	int TB1_PRTNM = 5;              JTextField txtPRTNM;
	int TB1_REQQT = 6; 				JTextField txtREQQT; 
	int TB1_PLCQT = 7;              JTextField txtPLCQT; 
	int TB1_PRCQT = 8;              JTextField txtPRCQT; 
	int TB1_DCLQT = 9; 			    JTextField txtDCLQT;
	int TB1_DOCDT = 10;             JTextField txtDOCDT;  
	int TB1_PLCDT = 11;             JTextField txtPLCDT; 
	int TB1_PRCDT = 12;             JTextField txtPRCDT; 
	int TB1_EDLDT = 13;             JTextField txtEDLDT;
	int TB1_STSFL = 14; 			JTextField txtSTSFL; 
	int TB1_QTYAT = 15; 			JCheckBox chkQTYAT1,chkQTYAT2; 
	int TB1_DCLDT = 16;             JTextField txtDCLDT; 
	int TB1_LINNO = 17;             JTextField txtLINNO; 
	int TB1_LOTNO = 18; 			JTextField txtLOTNO;
	int TB1_RMCDT = 19; 			JTextField txtRMCDT;
	int TB1_REMDS = 20;             JTextField txtREMDS;
	
	String[] M_strMONTH = {"January", "February","March", "April", "May", "June", "July","August", "September", "October", "November","December"};
	int L_ROWCNT=0,L_ROWCNT1=0,L_ROWCNT2=0,L_ROWCNT3=0;
	String L_strWHRSTR="";
	int L_ROWNO1=0,L_ROWNO2=0,L_ROWNO3=0;
	
	pr_terqa()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,20);	
		
			pnlRQREC = new JPanel();
			pnlRQPEN = new JPanel();
			pnlRMPEN = new JPanel();
			pnlRQCMP = new JPanel();
			pnlRQREC.setLayout(null);
			pnlRQPEN.setLayout(null);
			pnlRMPEN.setLayout(null);
			pnlRQCMP.setLayout(null);
		
			jtpMANTAB=new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			
			jtpMANTAB.add(pnlRQREC,"PR Received in the month of..");
			jtpMANTAB.add(pnlRQPEN,"PR Pending for Clearance/On Hold/Cancelled");
			jtpMANTAB.add(pnlRMPEN,"PR Pending for Raw Material & Additives");
			jtpMANTAB.add(pnlRQCMP,"PR Completed");
			
			cmbSTSFL1 = new JComboBox();
			cmbSTSFL1.addItem("Select");
			cmbSTSFL2 = new JComboBox();
			cmbSTSFL2.addItem("Select");
			cmbSTSFL3 = new JComboBox();
			cmbSTSFL3.addItem("Select");
		
    		String[] L_strTBLHD = {"","PR No.","PROD TP","GRADE","PRODUCT CATEGORY","CUSTOMER","QUANTITY MT","PLAN CLR QTY","PROD CLR QTY","DECL QTY","PR RECEIVED ON","PLAN CLR ON","PROD CLR ON","DECL CLR ON","STATUS","QTY AT","DELIVERY DATE","LINE NUMBER","LOT NUMBER","RM CLR ON","REMARKS"};
    		int[] L_intCOLSZ = {10,70,20,200,70,60,50,50,50,50,40,40,40,40,100,10,70,70,70,70,100};
    		tblRQREC= crtTBLPNL1(pnlRQREC,L_strTBLHD,500,2,1,10,19,L_intCOLSZ,new int[]{0,TB1_QTYAT});
    		tblRQPEN= crtTBLPNL1(pnlRQPEN,L_strTBLHD,500,1,1,10,19,L_intCOLSZ,new int[]{0,TB1_QTYAT});
    		tblRMPEN= crtTBLPNL1(pnlRMPEN,L_strTBLHD,500,1,1,10,19,L_intCOLSZ,new int[]{0,TB1_QTYAT});
    		tblRQCMP= crtTBLPNL1(pnlRQCMP,L_strTBLHD,500,1,1,10,19,L_intCOLSZ,new int[]{0,TB1_QTYAT});
    		
    		tblPRCQT= crtTBLPNL1(pnlRQREC,L_strTBLHD,500,13,1,4,19,L_intCOLSZ,new int[]{0,TB1_QTYAT});
    		tblPLCQT= crtTBLPNL1(pnlRQPEN,L_strTBLHD,500,12,1,4,19,L_intCOLSZ,new int[]{0,TB1_QTYAT});
    		tblDCLQT= crtTBLPNL1(pnlRQCMP,L_strTBLHD,500,12,1,4,19,L_intCOLSZ,new int[]{0,TB1_QTYAT});
    		
    		
    		tblRQREC.addKeyListener(this);
    		tblRQPEN.addKeyListener(this);
    		tblRMPEN.addKeyListener(this);
    		
    		cmbSTSFL1.addFocusListener(this);
    		cmbSTSFL1.addActionListener(this);
    		
    		cmbSTSFL2.addFocusListener(this);
    		cmbSTSFL2.addActionListener(this);
    	
    		cmbSTSFL3.addFocusListener(this);
    		cmbSTSFL3.addActionListener(this);
    		
    		
    		tblRQREC.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
    		tblRQREC.setCellEditor(TB1_QTYAT,chkQTYAT1=new JCheckBox());
    		tblRQREC.setCellEditor(TB1_REQQT,txtREQQT = new TxtNumLimit(12.3)); 
    		tblRQREC.setCellEditor(TB1_STSFL,txtSTSFL= new TxtLimit(15)); 
    		tblRQREC.setCellEditor(TB1_DCLDT,txtDCLDT=new TxtDate());
    		tblRQREC.setCellEditor(TB1_LINNO,txtLINNO=new TxtLimit(2));
    		tblRQREC.setCellEditor(TB1_EDLDT,txtEDLDT=new TxtDate());
    		tblRQREC.setCellEditor(TB1_LOTNO,txtLOTNO = new TxtLimit(8));
    		tblRQREC.setCellEditor(TB1_PRCQT,txtPRCQT=new TxtNumLimit(12.3));
    		tblRQREC.setCellEditor(TB1_PLCQT,txtPLCQT=new TxtNumLimit(12.3));
    		tblRQREC.setCellEditor(TB1_DCLQT,txtDCLQT=new TxtNumLimit(12.3));
    		tblRQREC.setCellEditor(TB1_PRCDT,txtPRCDT=new TxtDate());
    		tblRQREC.setCellEditor(TB1_PLCDT,txtPLCDT=new TxtDate());
    		tblRQREC.setCellEditor(TB1_RMCDT,txtRMCDT=new TxtDate());
    		tblRQREC.setCellEditor(TB1_REMDS,txtREMDS = new TxtLimit(50));
    		
    		tblRQPEN.setCellEditor(TB1_CHKFL,chkCHKFL1=new JCheckBox());
    		tblRQPEN.setCellEditor(TB1_QTYAT,chkQTYAT2=new JCheckBox());
    		tblRQPEN.setCellEditor(TB1_STSFL,txtSTSFL = new TxtLimit(15));
    		tblRQPEN.setCellEditor(TB1_PLCQT,txtPLCQT = new TxtNumLimit(12.3));
    		tblRQPEN.setCellEditor(TB1_PLCDT,txtPLCDT = new TxtDate());
    		tblRQPEN.setCellEditor(TB1_REMDS,txtREMDS = new TxtLimit(50));
    		
    		tblRMPEN.setCellEditor(TB1_CHKFL,chkCHKFL2=new JCheckBox());
    		tblRMPEN.setCellEditor(TB1_STSFL,txtSTSFL = new TxtLimit(15));
    		tblRMPEN.setCellEditor(TB1_RMCDT,txtRMCDT = new TxtDate());
    		tblRMPEN.setCellEditor(TB1_REMDS,txtREMDS = new TxtLimit(50));
    		
    		tblRQCMP.setCellEditor(TB1_CHKFL,chkCHKFL3=new JCheckBox());
    		
			tblRQREC.setCellEditor(TB1_STSFL,cmbSTSFL1);
			tblRQPEN.setCellEditor(TB1_STSFL,cmbSTSFL2);
			tblRMPEN.setCellEditor(TB1_STSFL,cmbSTSFL3);
    		
    		txtLINNO.addKeyListener(this);
    		
			objTBLVRF = new TBLINPVF();
			tblRQREC.setInputVerifier(objTBLVRF);
			tblRQPEN.setInputVerifier(objTBLVRF);
			
			((JCheckBox) tblRQREC.cmpEDITR[TB1_QTYAT]).addMouseListener(this);
			
			((JCheckBox) tblRQPEN.cmpEDITR[TB1_QTYAT]).addMouseListener(this);
			
			add(lblREMRK_MR=new JLabel(),14,1,1,15,pnlRQREC,'L'); 
			add(lblREMRK1_MR=new JLabel(),14,1,1,15,pnlRQPEN,'L');
			add(lblREMRK2_MR = new JLabel(),14,1,1,15,pnlRMPEN,'L'); 
			add(lblREMRK3_MR = new JLabel(),14,1,1,15,pnlRQCMP,'L'); 
			
			add(lblREMRK_PR=new JLabel(),15,1,1,15,pnlRQREC,'L'); 
			add(lblREMRK1_PR=new JLabel(),15,1,1,15,pnlRQPEN,'L');
			add(lblREMRK2_PR= new JLabel(),15,1,1,15,pnlRMPEN,'L'); 
			add(lblREMRK3_PR= new JLabel(),15,1,1,15,pnlRQCMP,'L'); 
			
			chkCHKFL.addMouseListener(this);
			chkCHKFL1.addMouseListener(this);
			chkCHKFL2.addMouseListener(this);
			chkCHKFL3.addMouseListener(this);
			
			add(new JLabel("Order By:"),1,12,1,2,pnlRQREC,'L'); 
			
			add(rdbSTSFL=new JRadioButton("Status"),1,14,1,2,pnlRQREC,'L');
			add(rdbDOCNO=new JRadioButton("PR No"),1,16,1,2,pnlRQREC,'L');
			
			btgREPDL=new ButtonGroup();
			btgREPDL.add(rdbDOCNO); 
			btgREPDL.add(rdbSTSFL);           
			rdbSTSFL.setSelected(true);
			
			add(chkREPFL=new JCheckBox("Report"),1,18,1,2,pnlRQREC,'L');
			
			add(jtpMANTAB,1,1,18,20,this,'L');
			
			tblRQREC.setEnabled(false);
			((JTextField)tblRQREC.cmpEDITR[TB1_REQQT]).setEditable(true);
			((JTextField)tblRQREC.cmpEDITR[TB1_DCLDT]).setEditable(true);
			((JTextField)tblRQREC.cmpEDITR[TB1_LINNO]).setEditable(true);
			((JTextField)tblRQREC.cmpEDITR[TB1_EDLDT]).setEditable(true);
			((JTextField)tblRQREC.cmpEDITR[TB1_LOTNO]).setEditable(true);
			((JTextField)tblRQREC.cmpEDITR[TB1_PRCDT]).setEditable(true);
			((JTextField)tblRQREC.cmpEDITR[TB1_PLCDT]).setEditable(true);
			((JTextField)tblRQREC.cmpEDITR[TB1_RMCDT]).setEditable(true);
			
			tblRQPEN.setEnabled(false);
			((JTextField)tblRQPEN.cmpEDITR[TB1_PLCDT]).setEnabled(true);
			((JTextField)tblRQPEN.cmpEDITR[TB1_REMDS]).setEnabled(true);
			((JComboBox)tblRQPEN.cmpEDITR[TB1_STSFL]).setEnabled(true);
			
			tblRMPEN.setEnabled(false);
			((JTextField)tblRMPEN.cmpEDITR[TB1_RMCDT]).setEnabled(true);
			((JTextField)tblRMPEN.cmpEDITR[TB1_REMDS]).setEnabled(true);
			((JComboBox)tblRMPEN.cmpEDITR[TB1_STSFL]).setEnabled(true);
			
			tblRQCMP.setEnabled(false);
			tblPRCQT.setEnabled(false);
			tblPLCQT.setEnabled(false);
			tblDCLQT.setEnabled(false);
			
    		hstSTSCD = new Hashtable<String,String>();
    		hstSTSDS = new Hashtable<String,String>();
    		hstMODLS = new Hashtable<String,String>();
    		hstQTYVL = new Hashtable<String,String>();
    		
    		/**add Status in combobox with tabwise no(cmt_chp01).
    		 * Hashtable to store Status code ,Status Description & Mod list Status.
    		 * **/
    		M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_MODLS from CO_CDTRN ";
			M_strSQLQRY += " WHERE CMT_CGMTP='STS' AND CMT_CGSTP='PRXXREQ' order by CMT_CODCD";
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>>M_strSQLQRY>>>>"+M_strSQLQRY);	
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstSTSDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					hstSTSCD.put(M_rstRSSET.getString("CMT_CODDS"),M_rstRSSET.getString("CMT_CODCD"));
					hstMODLS.put(M_rstRSSET.getString("CMT_CODCD"),nvlSTRVL(M_rstRSSET.getString("CMT_MODLS"),""));
					if(M_rstRSSET.getString("CMT_CHP01").contains("1"))
						cmbSTSFL1.addItem(hstSTSDS.get(M_rstRSSET.getString("CMT_CODCD")));
					if(M_rstRSSET.getString("CMT_CHP01").contains("2"))
						cmbSTSFL2.addItem(hstSTSDS.get(M_rstRSSET.getString("CMT_CODCD")));
					if(M_rstRSSET.getString("CMT_CHP01").contains("3"))
						cmbSTSFL3.addItem(hstSTSDS.get(M_rstRSSET.getString("CMT_CODCD")));
					
				}
				M_rstRSSET.close();
			}
			
			
			M_strSQLQRY=" SELECT RQ1_TRNQT,RQ1_DOCNO from PR_RQTR1 ";
			M_strSQLQRY+=" WHERE RQ1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>>M_strSQLQRY>>>>"+M_strSQLQRY);	
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstQTYVL.put(M_rstRSSET.getString("RQ1_DOCNO"),M_rstRSSET.getString("RQ1_TRNQT"));
				}
				M_rstRSSET.close();
			}
			
			
			L_strWHRSTR= "pt_prtcd=RQ_PRTCD and pt_prttp='C' and PR_PRDCD=RQ_PRDCD and PR_PRDTP=RQ_PRDTP AND A.CMT_CGMTP='STS' AND A.CMT_CGSTP='PRXXREQ' AND A.CMT_CODCD=RQ_STSFL and RQ_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' AND B.CMT_CGMTP='MST' AND B.CMT_CGSTP='COXXPGR' AND B.CMT_CODCD=substr(RQ_PRDCD,1,4)||'00000A'";
			
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{ 
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
					{
						getDATA();
						getDATA1();
					}
				}
			}
			else if(M_objSOURC==cmbSTSFL1)
		    {
				selCHKBX(tblRQREC);
			
				if(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().length()>0)
				if(!(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("Select"))
				{
					if(hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("03")
					|| hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("04"))
				    	tblRQREC.setValueAt(cl_dat.M_strLOGDT_pbst,tblRQREC.getSelectedRow(),TB1_PRCDT);
					else if(hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("05")
					|| hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("09"))
						tblRQREC.setValueAt(cl_dat.M_strLOGDT_pbst,tblRQREC.getSelectedRow(),TB1_EDLDT);
				}
				
				setCOLOR(tblRQREC);
		    }
			else if(M_objSOURC==cmbSTSFL2)
		    {
				selCHKBX(tblRQPEN);
				if(tblRQPEN.getValueAt(tblRQPEN.getSelectedRow(),TB1_STSFL).toString().length()>0)
				if(!(tblRQPEN.getValueAt(tblRQPEN.getSelectedRow(),TB1_STSFL).toString().trim()).equals("Select"))
				if(hstSTSCD.get(tblRQPEN.getValueAt(tblRQPEN.getSelectedRow(),TB1_STSFL).toString().trim()).equals("01")
				|| hstSTSCD.get(tblRQPEN.getValueAt(tblRQPEN.getSelectedRow(),TB1_STSFL).toString().trim()).equals("12"))
					tblRQPEN.setValueAt(cl_dat.M_strLOGDT_pbst,tblRQPEN.getSelectedRow(),TB1_PLCDT);
				
				setCOLOR(tblRQPEN);	
		    }
			else if(M_objSOURC==cmbSTSFL3)
		    {
				selCHKBX(tblRMPEN);
				
				if(tblRMPEN.getValueAt(tblRMPEN.getSelectedRow(),TB1_STSFL).toString().length()>0 && !(tblRMPEN.getValueAt(tblRMPEN.getSelectedRow(),TB1_STSFL).toString().trim()).equals("Select"))
    			if(hstSTSCD.get(tblRMPEN.getValueAt(tblRMPEN.getSelectedRow(),TB1_STSFL).toString().trim()).equals("02"))
    				tblRMPEN.setValueAt(cl_dat.M_strLOGDT_pbst,tblRMPEN.getSelectedRow(),TB1_RMCDT);
				
				setCOLOR(tblRMPEN);	
		    }
			else if(M_objSOURC == chkREPFL)
			{
				exeREPFL();//Generate report
			}
			else if(M_objSOURC == rdbDOCNO)
			{
				if(rdbDOCNO.isSelected())
					getDATA();//fetch Doc No wise record in table & report
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"actionPerformed");		
		}
	}
	/**method to set Color as per Status
	 * Highlight Selected Row**/
	public void setCOLOR(cl_JTable LP_TBL)
	{
		try
		{
			if(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDDS).toString().trim().length()>0 )
			{
				if(!LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().equals("Select"))
				{
					if(hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("00"))
						LP_TBL.setSelectionForeground(Color.red);
					else if(hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("01"))
						LP_TBL.setSelectionForeground(Color.red);
					else if(hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("02"))
						LP_TBL.setSelectionForeground(Color.red);
					else if(hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("04"))
						LP_TBL.setSelectionForeground(Color.blue);
					else if(hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("06"))
						LP_TBL.setSelectionForeground(Color.gray);
					else if(hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("19"))
						LP_TBL.setSelectionForeground(Color.gray);
					else
						LP_TBL.setSelectionForeground(Color.black);
					
					for(int P_intROWNO=0;P_intROWNO<LP_TBL.getSelectedRow();P_intROWNO++)
					{
						if(LP_TBL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
						{
							if(hstSTSCD.get(LP_TBL.getValueAt(P_intROWNO,TB1_STSFL).toString().trim()).equals("00") )
								LP_TBL.setRowColor(P_intROWNO,Color.red);
						
							else if(hstSTSCD.get(LP_TBL.getValueAt(P_intROWNO,TB1_STSFL).toString().trim()).equals("01"))
								LP_TBL.setRowColor(P_intROWNO,Color.red);
						   
							else if(hstSTSCD.get(LP_TBL.getValueAt(P_intROWNO,TB1_STSFL).toString().trim()).equals("02"))
								LP_TBL.setRowColor(P_intROWNO,Color.red);
						
							else if(hstSTSCD.get(LP_TBL.getValueAt(P_intROWNO,TB1_STSFL).toString().trim()).equals("04"))
								LP_TBL.setRowColor(P_intROWNO,Color.blue);
							
							else if(hstSTSCD.get(LP_TBL.getValueAt(P_intROWNO,TB1_STSFL).toString().trim()).equals("06"))
								LP_TBL.setRowColor(P_intROWNO,Color.gray);
							
							else if(hstSTSCD.get(LP_TBL.getValueAt(P_intROWNO,TB1_STSFL).toString().trim()).equals("19"))
								LP_TBL.setRowColor(P_intROWNO,Color.gray);
							else
								LP_TBL.setRowColor(P_intROWNO,Color.black);
						}
					}
				}
				else
					LP_TBL.setSelectionForeground(Color.black);
				
			}	
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"setCOLOR");		
		}
	}
		
	/** Method to request Focus in all TextField of component,when press ENTER & Display help screen when press F1 **/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				
			}
			else if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
			    if(M_objSOURC == txtLINNO)
				{
					M_strHLPFLD = "txtLINNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='PRXXLIN' AND CMT_CCSVL like '02_%' ";
					M_strSQLQRY += " order by CMT_CODCD";
					//System.out.println("txtDOCNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Line No","Decription"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"keypressed");		
		}		
		
	}
	/**method to display status in combobox with Mod list wise status**/
	void selCHKBX(cl_JTable LP_TBL)
	{
		try
		{
			if(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDDS).toString().length()>0 )
			{
				if(!(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("Select"))
				{
					LP_TBL.setValueAt(new Boolean(true),LP_TBL.getSelectedRow(),TB1_CHKFL);
					
				}
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"selCHKBX");		
		}
	}
	
	/**method to display status in combobox with Mod list wise status**/
	/*void dspSTSFL( cl_JTable LP_TBL,JComboBox cmbCMBOX)
	{
		try
		{
			//String[] L_strMODLS=null;
			//String L_strITEM=LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString();
			
			if(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDDS).toString().length()>0 )
			{
				if(!(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("Select"))
				if(!L_strITEM.equals("Select"))
				{
					for(int i=cmbCMBOX.getItemCount()-1;i>0;i--)
					{
						cmbCMBOX.removeItemAt(i);
					}
				    if(hstMODLS.containsKey(hstSTSCD.get(L_strITEM.trim())))
					{
						cmbCMBOX.addItem(hstSTSDS.get(hstSTSCD.get(L_strITEM.trim())));
						L_strMODLS=hstMODLS.get(hstSTSCD.get(L_strITEM.trim())).split("_");  
						for(int j=0;j<L_strMODLS.length;j++)
						{
							cmbCMBOX.addItem(hstSTSDS.get(L_strMODLS[j]));
						}
					}
				}
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"dspSTSFL");		
		}
	}*/

	
	/**Method to
	 *
	 * Creates JoptionPane for Status(01,03,05).
	 * Enter Quantity in JoptionPane & display in table.
	 * Validation to Enter quantity in JoptionPane(there enter Quantity must be less than Request Qty).
	 * L_strPLCQT,L_strPRCQT &  L_strDCLQT variable to store total Plan,Prodn & Declaration clr Qty resp. & display in respective labels.
	 * While select "OK" button of JoptionPane. it will be insert/update records of pr_rqtr1 table.

	**/
	private void dspQTYVL(cl_JTable LP_TBL,JTextField LP_TXT,String LP_STR,String LP_PRSTS,String LP_DRSTS,int LP_COLDT,int LP_COLQT)
	{
		try
		{
			float L_fltQTYVL_TXT=0,L_fltQTYVL_TBL=0;
			float fltQTYCT=0;
			
			if(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDDS).toString().length()>0 )
			if(!(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString().trim()).equals("Select"))
			{
			
				LP_TBL.setValueAt(new Boolean(true),LP_TBL.getSelectedRow(),TB1_CHKFL);
				//if(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),LP_COLDT).toString().length()==0)
					LP_TBL.setValueAt(cl_dat.M_strLOGDT_pbst,LP_TBL.getSelectedRow(),LP_COLDT);
			
				M_strSQLQRY=" SELECT RQ1_TRNQT,RQ1_DOCNO FROM PR_RQTR1 ";
				M_strSQLQRY+=" WHERE RQ1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and RQ1_DOCNO='"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_DOCNO).toString()+"' ";
				M_strSQLQRY +=" and RQ1_PRDTP='"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDTP).toString()+"'";
				M_strSQLQRY +=" and RQ1_STSFL='"+LP_PRSTS+"'";
				//	M_strSQLQRY +=" and RQ1_STSFL='"+hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString())+"'";
				//M_strSQLQRY += " AND RQ1_TRNDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),LP_CDT).toString()))+"'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				System.out.println(">>>>M_strSQLQRY>>>>"+M_strSQLQRY);	
				if(M_rstRSSET != null )
				{
					while(M_rstRSSET.next())
					{
						if(M_rstRSSET.getFloat("RQ1_TRNQT")>0)
						{	
							hstQTYVL.put(M_rstRSSET.getString("RQ1_DOCNO"),M_rstRSSET.getString("RQ1_TRNQT"));
							fltQTYCT+=M_rstRSSET.getFloat("RQ1_TRNQT");// Previous clr Qty
						}	
					}
				}
			
				M_rstRSSET.close();
				
				System.out.println("fltQTYCT"+fltQTYCT);
			
				//if(!LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_REQQT).toString().equals(String.valueOf(setNumberFormat(fltQTYCT,3))))
				{
					//if(pnlQTYVL==null)
					{
						pnlQTYVL=new JPanel(null);
						
						add(new JLabel("Requested Qty:"),1,1,1,2,pnlQTYVL,'L');
						add(lblREQQT=new JLabel(""),1,3,1,2,pnlQTYVL,'L');
						lblREQQT.setForeground(Color.red);
						
						add(new JLabel(LP_STR),2,1,1,3,pnlQTYVL,'L');
						add(LP_TXT = new JTextField(),2,5,1,2,pnlQTYVL,'L');
						
						add(lblQTYVL=new JLabel(String.valueOf(setNumberFormat(fltQTYCT,3))),2,4,1,1,pnlQTYVL,'L');
						lblQTYVL.setForeground(Color.red);
					
						add(lblERMSG=new JLabel(""),3,1,1,1,pnlQTYVL,'L');
						lblERMSG.setForeground(Color.red);
						
						lblREQQT.setText("");
					}
					
					lblREQQT.setText(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_REQQT).toString());
					//Calculate remaining clr Qty
					if(fltQTYCT>Float.parseFloat(lblREQQT.getText()))
						L_fltQTYVL_TXT=0;
					else
						L_fltQTYVL_TXT=Float.parseFloat(lblREQQT.getText())-fltQTYCT;
					LP_TXT.setText(setNumberFormat(L_fltQTYVL_TXT,3));
					
					pnlQTYVL.setSize(100,100);
					pnlQTYVL.setPreferredSize(new Dimension(400,100));
					int L_intOPTN=JOptionPane.showConfirmDialog( LP_TBL,pnlQTYVL,"Quantity details",JOptionPane.OK_CANCEL_OPTION);
					
					if(L_intOPTN == 0)
					{
						//if(!vldQTYVL())
						//	return;
						//total of clr Qty that display in table.
						L_fltQTYVL_TBL=Float.parseFloat(LP_TXT.getText())+fltQTYCT;
						
						LP_TBL.setValueAt(setNumberFormat(L_fltQTYVL_TBL,3),LP_TBL.getSelectedRow(),LP_COLQT);
						
						/*if(!LP_TBL.getValueAt(LP_TBL.getSelectedRow(),LP_COLQT).equals(lblREQQT.getText()))
							LP_TBL.setValueAt(hstSTSDS.get(LP_PRSTS),LP_TBL.getSelectedRow(),TB1_STSFL);
						else 
							LP_TBL.setValueAt(hstSTSDS.get(LP_DRSTS),LP_TBL.getSelectedRow(),TB1_STSFL);*/
						
						String strSQLQRY=" select count(*) from PR_RQTR1 where";
						strSQLQRY+=" RQ1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and RQ1_DOCNO='"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_DOCNO).toString()+"' ";
						strSQLQRY +=" and RQ1_PRDTP='"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDTP).toString()+"'";
						strSQLQRY += " and RQ1_STSFL='"+hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString())+"'";
						strSQLQRY += " AND RQ1_TRNDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),LP_COLDT).toString()))+"'";
						
						ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
						//System.out.println(">>>Count>>"+strSQLQRY);
						if(rstRSSET.next() && rstRSSET != null)
						{
							if(rstRSSET.getInt(1)>0)
							{
								M_strSQLQRY = " Update PR_RQTR1 set";
							    	
						    	//M_strSQLQRY += " RQ1_TRNQT= '"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),LP_QTY).toString()+"'";
								M_strSQLQRY += " RQ1_TRNQT= '"+LP_TXT.getText()+"'";
								
								M_strSQLQRY += " where RQ1_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
								M_strSQLQRY += " AND RQ1_DOCNO= '"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_DOCNO).toString()+"'";
								M_strSQLQRY += " AND RQ1_PRDTP= '"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDTP).toString()+"'";
								M_strSQLQRY += " AND RQ1_TRNDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),LP_COLDT).toString()))+"'";
								M_strSQLQRY += " AND RQ1_STSFL= '"+hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString())+"'";
								System.out.println(">>>update>>"+M_strSQLQRY);  
								cl_dat.M_flgLCUPD_pbst = true;
								cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
								
							}
							else
							{
					            M_strSQLQRY =" insert into PR_RQTR1 (RQ1_TRNQT,RQ1_STSFL,RQ1_TRNDT,RQ1_DOCNO,RQ1_PRDTP,RQ1_CMPCD) values (";
					            if(LP_TXT.getText().length()>0)
					            	M_strSQLQRY += "'"+LP_TXT.getText()+"',";
					            else
					            	M_strSQLQRY += "'0.000',";
					            M_strSQLQRY += "'"+LP_PRSTS+"',";
					          //  M_strSQLQRY += "'"+hstSTSCD.get(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_STSFL).toString())+"',";
					            M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),LP_COLDT).toString()))+"',";
								M_strSQLQRY += "'"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_DOCNO).toString()+"',";
								M_strSQLQRY += "'"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_PRDTP).toString()+"',";
								M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"')";	
							//	M_strSQLQRY += "'"+M_strSBSCD+"')";
								
								System.out.println(">>>Insert>>"+ M_strSQLQRY );
								cl_dat.M_flgLCUPD_pbst = true;
								cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
							}
						}
						if(hstQTYVL.containsKey(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_DOCNO).toString()))
							LP_TBL.setRowColor(LP_TBL.getSelectedRow(),Color.orange.darker());
						getDATA1();
					}
					
				}
			}
			
		}
		catch (Exception L_EX)
		{
			//L_EX.printStackTrace();
			setMSG("Error in dspQTYVL : "+L_EX,'E');
		}
	}
	
	/**Method to display Marketing Remark (pr_rmmst) & Production Remark(pr_rqtrn) of Selected row**/
	void dspREMDS( cl_JTable LP_TBL,JLabel LP_LBLMR,JLabel LP_LBLPR)
	{
		try
		{
			if(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_CHKFL).toString().equals("true"))
			{
				if(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_REMDS).toString().length()>0)
				{
					LP_LBLPR.setText("Production Remark:  "+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_REMDS).toString());
					LP_LBLPR.setForeground(Color.red);
				}
				else
					LP_LBLPR.setText("");
				
				String L_strSQLQRY= " select RM_REMDS from PR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and RM_TRNTP ='RQ' and RM_DOCNO='"+LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB1_DOCNO).toString()+"' and RM_DOCTP ='PR' ";
				//L_strSQLQRY+= "and RM_PRDTP='"+txtPRDTP.getText()+"'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY); 
				if(L_rstRSSET != null &&  L_rstRSSET.next())
				{
					LP_LBLMR.setText("Marketing Remark:  "+nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),""));	
					LP_LBLMR.setForeground(Color.blue);
				}
				else
					LP_LBLMR.setText("");
			}
			else
			{
				LP_LBLMR.setText("");
				LP_LBLPR.setText("");
			}
		}
		catch(Exception e1)
		{
			setMSG(e1,"dspREMDS");
		}
	 }
	
	public void mouseReleased(MouseEvent L_KE)
	 {
  	    super.mouseReleased(L_KE);
		try
		{ 	
			 if(L_KE.getSource().equals(chkQTYAT1))
			 {
				 if(!tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().equals("Select"))
				 {
					if(hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("03")
					|| hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("04"))
					{
						if(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_PLCQT).toString().length()>0)
							dspQTYVL(tblRQREC,txtPRCQT1,"Prodn Clearance Qty:","04","03",TB1_PRCDT,TB1_PRCQT);
						else
							setMSG("First Completed Plan Clearance Qty",'N');
					}
					else if(hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("09")
					|| hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("05"))
					{
						if(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_PRCQT).toString().length()>0)
							dspQTYVL(tblRQREC,txtDCLQT1,"Declaration Clearance Qty:","09","05",TB1_EDLDT,TB1_DCLQT);
						else
							setMSG("First Completed Prodn Clearance Qty",'N');
					}
					else if(hstSTSCD.get(tblRQREC.getValueAt(tblRQREC.getSelectedRow(),TB1_STSFL).toString().trim()).equals("01"))
							dspQTYVL(tblRQREC,txtPLCQT1,"Plan Clearance Qty:","12","01",TB1_PLCDT,TB1_PLCQT);
				 }
				
			 }
			 if(L_KE.getSource().equals(chkQTYAT2))
			 {
				 if(!tblRQPEN.getValueAt(tblRQPEN.getSelectedRow(),TB1_STSFL).toString().equals("Select"))
				 {
					if(hstSTSCD.get(tblRQPEN.getValueAt(tblRQPEN.getSelectedRow(),TB1_STSFL).toString().trim()).equals("01")
					|| hstSTSCD.get(tblRQPEN.getValueAt(tblRQPEN.getSelectedRow(),TB1_STSFL).toString().trim()).equals("12"))
						dspQTYVL(tblRQPEN,txtPLCQT1,"Plan Clearance Qty:","12","01",TB1_PLCDT,TB1_PLCQT);
				 }
			 }
			 if(L_KE.getSource().equals(chkCHKFL))
		     {
				dspREMDS(tblRQREC,lblREMRK_MR,lblREMRK_PR);
				
		     }
			 else if(L_KE.getSource().equals(chkCHKFL1))
		     {
				 dspREMDS(tblRQPEN,lblREMRK1_MR,lblREMRK1_PR);
		     }
			 else if(L_KE.getSource().equals(chkCHKFL2))
		     {
				dspREMDS(tblRMPEN,lblREMRK2_MR,lblREMRK2_PR);
		     }
			 else if(L_KE.getSource().equals(chkCHKFL3))
		     {
				dspREMDS(tblRQCMP,lblREMRK3_MR,lblREMRK3_PR);
		     }
		}
		catch(Exception e1)
		{
			setMSG(e1,"mouseReleased");
		}
	 }
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtLINNO"))
			{
			      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			      txtLINNO.setText(L_STRTKN.nextToken()); 
			}
		}
		catch(Exception E_VR)
		{
			E_VR.printStackTrace();
			setMSG(E_VR,"exeHLPOK()");		
		}
	}	
	void clrCOMP()
	{
		try
		{
			inlTBLEDIT(tblRQREC);
			tblRQREC.clrTABLE();
			inlTBLEDIT(tblRQPEN);
			tblRQPEN.clrTABLE();
			inlTBLEDIT(tblRMPEN);
			tblRMPEN.clrTABLE();
			inlTBLEDIT(tblRQCMP);
			tblRQCMP.clrTABLE();
			
			inlTBLEDIT(tblPRCQT);
			tblPRCQT.clrTABLE();
			inlTBLEDIT(tblPLCQT);
			tblPLCQT.clrTABLE();
			inlTBLEDIT(tblDCLQT);
			tblDCLQT.clrTABLE();
			
			lblREMRK_MR.setText("");
			lblREMRK1_MR.setText("");
			lblREMRK2_MR.setText("");
			lblREMRK3_MR.setText("");
			lblREMRK_PR.setText("");
			lblREMRK1_PR.setText("");
			lblREMRK2_PR.setText("");
			lblREMRK3_PR.setText("");
			rdbDOCNO.setSelected(false);
			rdbSTSFL.setSelected(false);
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"clrCOMP()");		
		}
	}	
	
	/**Validation for enter only selected row data**/
	boolean vldDATA()
	{
		try
		{
			for(int P_intROWNO=0;P_intROWNO<L_ROWCNT;P_intROWNO++)
			{
				if(tblRQREC.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					if(tblRQREC.getValueAt(P_intROWNO,TB1_REQQT).toString().length()==0)
					{
						setMSG("Please Enter Quantity for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
						return false;
						
					}
					else if(tblRQREC.getValueAt(P_intROWNO,TB1_STSFL).toString().equals("Select"))
					{
						setMSG("Please Select Valid Status for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
						return false;
						
					}
					else if(tblRQREC.getValueAt(P_intROWNO,TB1_STSFL).toString().equals(hstSTSDS.get("03")))
					{
						if(tblRQREC.getValueAt(P_intROWNO,TB1_PLCQT).toString().length()==0)
						{
							setMSG("Please First Cleared Plan Qty for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
							return false;
						}
						if(tblRQREC.getValueAt(P_intROWNO,TB1_PRCQT).toString().length()==0)
						{
							setMSG("Please Enter Prodn Clearance Qty for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
							return false;
						}
					}
					else if(tblRQREC.getValueAt(P_intROWNO,TB1_STSFL).toString().equals(hstSTSDS.get("05")))
					{
						if(tblRQREC.getValueAt(P_intROWNO,TB1_PRCQT).toString().length()==0)
						{
							setMSG("Please First Cleared Prodn Qty for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
							return false;
						}
						else if(tblRQREC.getValueAt(P_intROWNO,TB1_PLCQT).toString().length()==0)
						{
							setMSG("Please First Cleared Plan Qty for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
							return false;
						}
						else if(tblRQREC.getValueAt(P_intROWNO,TB1_DCLQT).toString().length()==0)
						{
							setMSG("Please Enter Declaration Clearance Qty for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
							return false;
						}
						
					}
					else if(tblRQREC.getValueAt(P_intROWNO,TB1_LINNO).toString().length()==0)
					{
						if(tblRQREC.getValueAt(P_intROWNO,TB1_DCLDT).toString().length()>0)
						{
							setMSG("Please Enter Line No for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" ..",'E');
							return false;
						}
					}
					else if(tblRQREC.getValueAt(P_intROWNO,TB1_LOTNO).toString().length()==0)
					{
						if(tblRQREC.getValueAt(P_intROWNO,TB1_EDLDT).toString().length()>0)
						{
							setMSG("Please Enter Lot No for PR No "+ tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO)+" ..",'E');
							return false;
						}
					}
				}
				for(int i=0;i<L_ROWCNT1;i++)
				{
					if(tblRQPEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						if(tblRQPEN.getValueAt(i,TB1_STSFL).toString().equals("Select"))
						{
							setMSG("Please Select Valid Status for PR No "+ tblRQPEN.getValueAt(i,TB1_DOCNO)+" in the table..",'E');
							return false;
							
						}
						else if(tblRQPEN.getValueAt(P_intROWNO,TB1_STSFL).toString().equals(hstSTSDS.get("01")))
						{
							if(tblRQPEN.getValueAt(P_intROWNO,TB1_PLCQT).toString().length()==0)
							{
								setMSG("Please Enter Plan Clearance Qty for PR No "+ tblRQPEN.getValueAt(P_intROWNO,TB1_DOCNO)+" in the table..",'E');
								return false;
							}
						}
					}
					
				}
				for(int j=0;j<L_ROWCNT2;j++)
				{
					if(tblRMPEN.getValueAt(j,TB1_CHKFL).toString().equals("true"))
					{
						if(tblRMPEN.getValueAt(j,TB1_STSFL).toString().equals("Select"))
						{
							setMSG("Please Select Valid Status for PR No "+ tblRMPEN.getValueAt(j,TB1_DOCNO)+" in the table..",'E');
							return false;
							
						}
					}
				}
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATA()");		
		}
		return true;
	}
	
	/**Validation to select atleast 1 row from three tables.**/
	private boolean vldPRDRQ()
	{
		boolean flgSELRW=false;////flag to check whether atleast 1 row is selected
		
		for(int P_intROWNO=0;P_intROWNO<tblRQREC.getRowCount();P_intROWNO++)
		{
			if(tblRQREC.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true")
			|| tblRQPEN.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true")
			|| tblRMPEN.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
			{
				flgSELRW=true;
			}
		}
		if(flgSELRW==false)
		{
			setMSG("Please Select atleast 1 row from the Table",'E');
			return false;
		}
		
		return true;
	}
	
	/**Validation For Quantity in Joptionpane**/
	boolean vldQTYVL()
	{
		try
		{
			if(lblPLCQT.getText().length()>0 && txtPLCQT.getText().length()>0)
			if(Integer.valueOf(lblPLCQT.getText())+Integer.valueOf(txtPLCQT.getText())>Integer.valueOf(lblREQQT.getText()))
			{
				lblERMSG.setText("Plan Clearance Qty can not be greater than Requested Qty");
				return false;
			}
			else if(lblPRCQT.getText().length()>0 && txtPRCQT.getText().length()>0)
			if(Integer.valueOf(lblPRCQT.getText())+Integer.valueOf(txtPRCQT.getText())>Integer.valueOf(lblREQQT.getText()))
			{
				lblERMSG.setText("Prodn Clearance Qty can not be greater than Requested Qty");
				return false;
			}
			else if(lblDCLQT.getText().length()>0 && txtDCLQT.getText().length()>0)
			if(Integer.valueOf(lblDCLQT.getText())+Integer.valueOf(txtDCLQT.getText())>Integer.valueOf(lblREQQT.getText()))
			{
				lblERMSG.setText("Declaration Qty can not be greater than Requested Qty");
				return false;
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldQTYVL()");		
		}
		lblERMSG.setText("");
		return true;
	}
	
   /**authorize selected row record after click on authorized button**/
	void exeSAVE()
	{
		try
		{
			int P_intROWNO=0;
			if(!vldDATA()) 
			{
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
			{
				if(!vldPRDRQ())/**validation to Authorize  **/
				{
					return;
				}
				exeAUTREC(P_intROWNO); //update record
				setMSG("Records Authorized Successfully..",'N');	
						
			}
			if(cl_dat.exeDBCMT("exeSAVE")) 
			{
				setMSG("Record Updated Successfully...",'N');		
			}
			else
			{
			    setMSG("Error in updating data..",'E');
			}
		}
		catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeSAVE"); 
	    }
	} 
	
	/** Method to update PR_RQTRN record .
	 *  */
	private void exeAUTREC(int P_intROWNO)
	{ 
	  try
	  {
		  	this.setCursor(cl_dat.M_curWTSTS_pbst);
		  	/**for PR Received in the month of..*/
		  	for(P_intROWNO=0;P_intROWNO<L_ROWCNT;P_intROWNO++)
			{
				if(tblRQREC.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY =" Update PR_RQTRN set";
					
					if(tblRQREC.getValueAt(P_intROWNO,TB1_PRDCT).toString().length()>0)
						M_strSQLQRY +=" RQ_PRDCT='"+tblRQREC.getValueAt(P_intROWNO,TB1_PRDCT).toString()+"',";
					
					M_strSQLQRY +=" RQ_REQQT='"+tblRQREC.getValueAt(P_intROWNO,TB1_REQQT).toString()+"',";
					
					if(tblRQREC.getValueAt(P_intROWNO,TB1_DCLQT).toString().length()>0)
						M_strSQLQRY +=" RQ_DCLQT="+tblRQREC.getValueAt(P_intROWNO,TB1_DCLQT).toString()+",";
					else
						M_strSQLQRY +=" RQ_DCLQT=0,";
				
					if(tblRQREC.getValueAt(P_intROWNO,TB1_DCLDT).toString().length()>0)
						M_strSQLQRY +=" RQ_DCLDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWNO,TB1_DCLDT).toString()))+"',";
					
					if(tblRQREC.getValueAt(P_intROWNO,TB1_LINNO).toString().length()>0)
						M_strSQLQRY +=" RQ_LINNO='"+tblRQREC.getValueAt(P_intROWNO,TB1_LINNO).toString()+"',";
				
					if(tblRQREC.getValueAt(P_intROWNO,TB1_EDLDT).toString().length()>0)
						M_strSQLQRY +=" RQ_EDLDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWNO,TB1_EDLDT).toString()))+"',";
				
					if(tblRQREC.getValueAt(P_intROWNO,TB1_LOTNO).toString().length()>0)
						M_strSQLQRY +=" RQ_LOTNO='"+tblRQREC.getValueAt(P_intROWNO,TB1_LOTNO).toString()+"',";
					
					if(tblRQREC.getValueAt(P_intROWNO,TB1_PRCDT).toString().length()>0)
							M_strSQLQRY +=" RQ_PRCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWNO,TB1_PRCDT).toString()))+"',";
					
					if(tblRQREC.getValueAt(P_intROWNO,TB1_PRCQT).toString().length()>0)
						M_strSQLQRY +=" RQ_PRCQT="+Float.parseFloat(tblRQREC.getValueAt(P_intROWNO,TB1_PRCQT).toString())+",";
					else
						M_strSQLQRY +=" RQ_PRCQT=0,";
					
					if(tblRQREC.getValueAt(P_intROWNO,TB1_PLCQT).toString().length()>0)
						M_strSQLQRY +=" RQ_PCLQT="+tblRQREC.getValueAt(P_intROWNO,TB1_PLCQT).toString()+",";
					
					if(tblRQREC.getValueAt(P_intROWNO,TB1_PLCDT).toString().length()>0)
						M_strSQLQRY +=" RQ_PLCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWNO,TB1_PLCDT).toString()))+"',";
					if(tblRQREC.getValueAt(P_intROWNO,TB1_RMCDT).toString().length()>0)
						M_strSQLQRY +=" RQ_RMCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWNO,TB1_RMCDT).toString()))+"',";
				
					if(tblRQREC.getValueAt(P_intROWNO,TB1_REMDS).toString().length()>0)
						M_strSQLQRY +=" RQ_REMDS='"+tblRQREC.getValueAt(P_intROWNO,TB1_REMDS).toString()+"',";
					
					M_strSQLQRY +=" RQ_STSFL='"+hstSTSCD.get(tblRQREC.getValueAt(P_intROWNO,TB1_STSFL).toString())+"',";
					M_strSQLQRY +=" RQ_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +=" RQ_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY +=" where RQ_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
					M_strSQLQRY +=" AND RQ_DOCNO= '"+tblRQREC.getValueAt(P_intROWNO,TB1_DOCNO).toString()+"'";
					
					System.out.println(">>>UPDATE>>"+M_strSQLQRY);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
		  	
		  	/**for the PR Pending for Clearance/On Hold/Cancelled.**/
		  	for(P_intROWNO=0;P_intROWNO<L_ROWCNT1;P_intROWNO++)
			{
				if(tblRQPEN.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY =" Update PR_RQTRN set";
					//M_strSQLQRY +=" RQ_REQQT='"+tblRQPEN.getValueAt(P_intROWNO,TB1_REQQT).toString()+"',";
					
					if(tblRQPEN.getValueAt(P_intROWNO,TB1_PLCQT).toString().length()>0)
						M_strSQLQRY +=" RQ_PCLQT="+tblRQPEN.getValueAt(P_intROWNO,TB1_PLCQT).toString()+",";
					else
						M_strSQLQRY +=" RQ_PCLQT=0,";
					if(tblRQPEN.getValueAt(P_intROWNO,TB1_PLCDT).toString().length()>0)
						M_strSQLQRY +=" RQ_PLCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRQPEN.getValueAt(P_intROWNO,TB1_PLCDT).toString()))+"',";
					
					if(tblRQPEN.getValueAt(P_intROWNO,TB1_REMDS).toString().length()>0)
						M_strSQLQRY +=" RQ_REMDS='"+tblRQPEN.getValueAt(P_intROWNO,TB1_REMDS).toString()+"',";
					
					M_strSQLQRY +=" RQ_STSFL='"+hstSTSCD.get(tblRQPEN.getValueAt(P_intROWNO,TB1_STSFL).toString())+"',";
					M_strSQLQRY +=" RQ_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +=" RQ_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY +=" where RQ_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
					M_strSQLQRY +=" AND RQ_DOCNO= '"+tblRQPEN.getValueAt(P_intROWNO,TB1_DOCNO).toString()+"'";
					
					System.out.println(">>>UPDATE1>>"+M_strSQLQRY);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
		  	
			/**PR Pending for Raw Material & Additives.*/
		  	for(P_intROWNO=0;P_intROWNO<L_ROWCNT2;P_intROWNO++)
			{
				if(tblRMPEN.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY =" Update PR_RQTRN set";
					//M_strSQLQRY +=" RQ_REQQT='"+tblRMPEN.getValueAt(P_intROWNO,TB1_REQQT).toString()+"',";
					if(tblRMPEN.getValueAt(P_intROWNO,TB1_RMCDT).toString().length()>0)
						M_strSQLQRY +=" RQ_RMCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRMPEN.getValueAt(P_intROWNO,TB1_RMCDT).toString()))+"',";
					
					if(tblRMPEN.getValueAt(P_intROWNO,TB1_REMDS).toString().length()>0)
						M_strSQLQRY +=" RQ_REMDS='"+tblRMPEN.getValueAt(P_intROWNO,TB1_REMDS).toString()+"',";
					
					M_strSQLQRY +=" RQ_STSFL='"+hstSTSCD.get(tblRMPEN.getValueAt(P_intROWNO,TB1_STSFL).toString())+"',";
					M_strSQLQRY +=" RQ_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +=" RQ_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY +=" where RQ_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
					M_strSQLQRY +=" AND RQ_DOCNO= '"+tblRMPEN.getValueAt(P_intROWNO,TB1_DOCNO).toString()+"'";
					
					System.out.println(">>>UPDATE2>>"+M_strSQLQRY);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
		//	M_strSQLQRY +=" AND RQ_PRDTP = '"+M_strSBSCD.substring(2,4)+"' ";
		  	if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Record Updated Successfully...",'N');	
				getDATA();	 /**fetch Exit pass entry & authorized data after that update.**/		
			}	
		  }
		  catch(Exception L_EX)
	      {
			 this.setCursor(cl_dat.M_curDFSTS_pbst);
	         setMSG(L_EX,"exeAUTREC()"); 
	      }
	}
	
	/**Method to display Resultset value in table **/
	private void getRSTVAL(ResultSet LP_RSLSET, JTable LP_TBL,int LP_ROW) 
	{
	    try
	    {
	    	LP_TBL.setValueAt(LP_RSLSET.getString("RQ_DOCNO"),LP_ROW,TB1_DOCNO);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("RQ_PRDTP"),LP_ROW,TB1_PRDTP);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("PR_PRDDS"),LP_ROW,TB1_PRDDS);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("CMT_SHRDS"),LP_ROW,TB1_PRDCT);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("PT_PRTNM"),LP_ROW,TB1_PRTNM);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_DOCDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_DOCDT")):"",LP_ROW,TB1_DOCDT);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("RQ_REQQT"),LP_ROW,TB1_REQQT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("CMT_CODDS"),""),LP_ROW,TB1_STSFL);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_DCLDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_DCLDT")):"",LP_ROW,TB1_DCLDT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_LINNO"),""),LP_ROW,TB1_LINNO);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_EDLDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_EDLDT")):"",LP_ROW,TB1_EDLDT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_LOTNO"),""),LP_ROW,TB1_LOTNO);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_PLCDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_PLCDT")):"",LP_ROW,TB1_PLCDT);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_PRCDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_PRCDT")):"",LP_ROW,TB1_PRCDT);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_RMCDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_RMCDT")):"",LP_ROW,TB1_RMCDT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_PCLQT"),""),LP_ROW,TB1_PLCQT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_PRCQT"),""),LP_ROW,TB1_PRCQT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_DCLQT"),""),LP_ROW,TB1_DCLQT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_REMDS"),""),LP_ROW,TB1_REMDS);
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRSTVAL");
		}

	} 
	
	/**
	 * Method to get Data from PR_RQTRN in .
	 * first tab-tblRQREQ_remaining all
	 * second tab-tblRQPEN_Planning,On hold & cancelled
	 * third tab-tblRMPEN_RM pending
	 * fourth tab-tblRQCMP_completed
	 * Highlight row for specific Status in table.
	 * */
	private void getDATA() 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			inlTBLEDIT(tblRQREC);
			tblRQREC.clrTABLE();
			inlTBLEDIT(tblRQPEN);
			tblRQPEN.clrTABLE();
			inlTBLEDIT(tblRMPEN);
			tblRMPEN.clrTABLE();
			inlTBLEDIT(tblRQCMP);
			tblRQCMP.clrTABLE();
		
			setMSG("Fetching Records ...",'N');
			String L_strDOCDT_NEW="",L_strDOCDT_OLD="",L_strSTSFL="",L_strSTSFL_OLD="";
			
			L_ROWCNT=0;
			L_ROWCNT1=0;
			L_ROWCNT2=0;
			L_ROWCNT3=0;
			
						
			M_strSQLQRY= " SELECT RQ_DOCNO,RQ_PRDTP,RQ_DOCDT,RQ_REQDT,RQ_REQQT,RQ_STSFL,RQ_PRDCT,RQ_DCLDT,RQ_LOTNO,RQ_LINNO,RQ_REMDS,RQ_PRDCD,RQ_PRTCD,RQ_PLCDT,RQ_PRCDT,RQ_EDLDT,RQ_RMCDT,RQ_PCLQT,RQ_PRCQT,RQ_DCLQT,PT_PRTNM,PR_PRDDS,A.CMT_CODDS,B.CMT_SHRDS";
			M_strSQLQRY+= " from PR_RQTRN,co_ptmst,co_prmst,CO_CDTRN A,CO_CDTRN B ";
			M_strSQLQRY+= " WHERE "+L_strWHRSTR+" ";
			//M_strSQLQRY+= " where pt_prtcd=RQ_PRTCD and pt_prttp='C' and PR_PRDCD=RQ_PRDCD and PR_PRDTP=RQ_PRDTP";
			//M_strSQLQRY+= "  AND A.CMT_CGMTP='STS' AND A.CMT_CGSTP='PRXXREQ' AND A.CMT_CODCD=RQ_STSFL and RQ_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
			//M_strSQLQRY+= "  AND B.CMT_CGMTP='MST' AND B.CMT_CGSTP='COXXPGR' AND B.CMT_CODCD=substr(RQ_PRDCD,1,4)||'00000A'";
			if(rdbSTSFL.isSelected())
				M_strSQLQRY+= " order by year(RQ_DOCDT),month(RQ_DOCDT),RQ_STSFL,RQ_DOCNO";
			else if(rdbDOCNO.isSelected())
				M_strSQLQRY+= " order by year(RQ_DOCDT),month(RQ_DOCDT),RQ_DOCNO,RQ_STSFL";
			
			System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strDOCDT_NEW =M_fmtLCDAT.format(M_rstRSSET.getDate("RQ_DOCDT"));
					L_strSTSFL=M_rstRSSET.getString("RQ_STSFL");
				
					if(!L_strDOCDT_NEW.substring(3,5).equals(L_strDOCDT_OLD))
					{
						L_strDOCDT_OLD=L_strDOCDT_NEW.substring(3,5);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strDOCDT_NEW));
						String L_strMONTH=M_strMONTH[M_calLOCAL.get(Calendar.MONTH)];
						
						tblRQREC.setValueAt(L_strMONTH.substring(0,3)+","+M_calLOCAL.get(Calendar.YEAR),L_ROWCNT,TB1_DOCNO);
						tblRQREC.setCellColor(L_ROWCNT,TB1_DOCNO,Color.blue);
						
						L_ROWCNT+=1;
					}
					if(L_strSTSFL.equals("11") || L_strSTSFL.equals("12") || L_strSTSFL.equals("19"))
					{
						if(L_strSTSFL.equals("19"))
							tblRQPEN.setRowColor(L_ROWCNT1,Color.gray);
		
						getRSTVAL(M_rstRSSET,tblRQPEN,L_ROWCNT1);
						L_ROWCNT1++;
					}
					else if(L_strSTSFL.equals("21"))
					{
						getRSTVAL(M_rstRSSET,tblRMPEN,L_ROWCNT2);
						L_ROWCNT2++;
					}
					else if(L_strSTSFL.equals("05"))
					{
						getRSTVAL(M_rstRSSET,tblRQCMP,L_ROWCNT3);
						L_ROWCNT3++;
					}
					else
					{
						if(L_strSTSFL.equals("00") || L_strSTSFL.equals("02"))
							tblRQREC.setRowColor(L_ROWCNT,Color.red);
						else if(L_strSTSFL.equals("04"))
							tblRQREC.setRowColor(L_ROWCNT,Color.blue);
						else if(L_strSTSFL.equals("06"))
							tblRQREC.setRowColor(L_ROWCNT,Color.gray);
						else
							tblRQREC.setRowColor(L_ROWCNT,Color.black);
						
					
						getRSTVAL(M_rstRSSET,tblRQREC,L_ROWCNT);
						if(hstQTYVL.containsKey(tblRQREC.getValueAt(L_ROWCNT,TB1_DOCNO).toString()))
						{
							if(L_strSTSFL.equals("01")) 
								tblRQREC.setRowColor(L_ROWCNT,Color.orange.darker());
							else if(L_strSTSFL.equals("03") || L_strSTSFL.equals("04")) 
								tblRQREC.setRowColor(L_ROWCNT,Color.green.darker());
							else
								tblRQREC.setRowColor(L_ROWCNT,Color.red);
						}
						L_ROWCNT++;
						
					}
				}
			}
			M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA()");
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	private void getRSTVAL1(ResultSet LP_RSLSET, JTable LP_TBL,int LP_ROW) 
	{
	    try
	    {
	    	LP_TBL.setValueAt(LP_RSLSET.getString("RQ1_DOCNO"),LP_ROW,TB1_DOCNO);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("RQ1_PRDTP"),LP_ROW,TB1_PRDTP);
	    	LP_TBL.setValueAt(hstSTSDS.get(LP_RSLSET.getString("RQ1_STSFL")),LP_ROW,TB1_STSFL);
	    	
	    	LP_TBL.setValueAt(LP_RSLSET.getString("PR_PRDDS"),LP_ROW,TB1_PRDDS);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("CMT_SHRDS"),LP_ROW,TB1_PRDCT);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("PT_PRTNM"),LP_ROW,TB1_PRTNM);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_DOCDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_DOCDT")):"",LP_ROW,TB1_DOCDT);
	    	LP_TBL.setValueAt(LP_RSLSET.getString("RQ_REQQT"),LP_ROW,TB1_REQQT);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_DCLDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_DCLDT")):"",LP_ROW,TB1_DCLDT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_LINNO"),""),LP_ROW,TB1_LINNO);
	    	LP_TBL.setValueAt(!(LP_RSLSET.getDate("RQ_RMCDT")==null)?M_fmtLCDAT.format(LP_RSLSET.getDate("RQ_RMCDT")):"",LP_ROW,TB1_RMCDT);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_REMDS"),""),LP_ROW,TB1_REMDS);
	    	LP_TBL.setValueAt(nvlSTRVL(LP_RSLSET.getString("RQ_LOTNO"),""),LP_ROW,TB1_LOTNO);
	    	
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRSTVAL1");
		}

	} 
	private void getDATA1() 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			inlTBLEDIT(tblPRCQT);
			tblPRCQT.clrTABLE();
			inlTBLEDIT(tblPLCQT);
			tblPLCQT.clrTABLE();
			inlTBLEDIT(tblDCLQT);
			tblDCLQT.clrTABLE();
			setMSG("Fetching Records ...",'N');
			
			L_ROWNO1=0;
			L_ROWNO2=0;
			L_ROWNO3=0;
			
			
			M_strSQLQRY= " SELECT RQ1_DOCNO,RQ1_PRDTP,RQ1_TRNQT,RQ1_TRNDT,RQ1_STSFL,RQ_DOCDT,RQ_REQDT,RQ_REQQT,RQ_PRDCT,RQ_DCLDT,RQ_LOTNO,RQ_LINNO,RQ_REMDS,RQ_PRDCD,RQ_PRTCD,RQ_RMCDT,PT_PRTNM,PR_PRDDS,A.CMT_CODDS,B.CMT_SHRDS";
			M_strSQLQRY+= " from PR_RQTR1,PR_RQTRN,co_ptmst,co_prmst,CO_CDTRN A,CO_CDTRN B ";
			M_strSQLQRY+= " WHERE RQ1_DOCNO=RQ_DOCNO AND RQ1_CMPCD=RQ_CMPCD AND RQ1_PRDTP=RQ_PRDTP AND "+L_strWHRSTR+" ";
			if(rdbSTSFL.isSelected())
				M_strSQLQRY+= " order by RQ1_STSFL,RQ1_DOCNO";
			else if(rdbDOCNO.isSelected())
				M_strSQLQRY+= " order by RQ1_DOCNO,RQ1_STSFL";
			
			System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("RQ1_STSFL").equals("12") || M_rstRSSET.getString("RQ1_STSFL").equals("04"))
			    	{
						getRSTVAL1(M_rstRSSET,tblPRCQT,L_ROWNO1);
						if(M_rstRSSET.getString("RQ1_STSFL").equals("12"))
				    	{
							tblPRCQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("RQ1_TRNQT"),""),L_ROWNO1,TB1_PLCQT);
							tblPRCQT.setValueAt(!(M_rstRSSET.getDate("RQ1_TRNDT")==null)?M_fmtLCDAT.format(M_rstRSSET.getDate("RQ1_TRNDT")):"",L_ROWNO1,TB1_PLCDT);
							tblPRCQT.setRowColor(L_ROWNO1,Color.orange.darker());
								
				    	}
				    	if(M_rstRSSET.getString("RQ1_STSFL").equals("04"))
				    	{
				    		tblPRCQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("RQ1_TRNQT"),""),L_ROWNO1,TB1_PRCQT);
				    		tblPRCQT.setValueAt(!(M_rstRSSET.getDate("RQ1_TRNDT")==null)?M_fmtLCDAT.format(M_rstRSSET.getDate("RQ1_TRNDT")):"",L_ROWNO1,TB1_PRCDT);
				    		tblPRCQT.setRowColor(L_ROWNO1,Color.green.darker());
				    	}
				    	
						L_ROWNO1++;
			    	}
				
					/*if(M_rstRSSET.getString("RQ1_STSFL").equals("12"))
			    	{
						getRSTVAL1(M_rstRSSET,tblPRCQT,L_ROWNO1,TB1_PLCQT,TB1_PLCDT);
						L_ROWNO1++;
			    	}
					/*else if(M_rstRSSET.getString("RQ1_STSFL").equals("12"))
			    	{
						getRSTVAL1(M_rstRSSET,tblPLCQT,L_ROWNO2,TB1_PLCQT,TB1_PLCDT);
						L_ROWNO2++;
			    	}*/
					if(M_rstRSSET.getString("RQ1_STSFL").equals("09") )
			    	{
						getRSTVAL1(M_rstRSSET,tblDCLQT,L_ROWNO3);
						tblDCLQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("RQ1_TRNQT"),""),L_ROWNO3,TB1_DCLQT);
						tblDCLQT.setValueAt(!(M_rstRSSET.getDate("RQ1_TRNDT")==null)?M_fmtLCDAT.format(M_rstRSSET.getDate("RQ1_TRNDT")):"",L_ROWNO3,TB1_DCLDT);
						L_ROWNO3++;
			    	}
				}
			}
			M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA1()");
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	void inlTBLEDIT(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}	
	
	void genRPTFL()
	{
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			genRPHDR();
			
			for(int i=0;i<L_ROWCNT;i++)
			{
				if(tblRQREC.getValueAt(i,TB1_PRDDS).toString().length()==0)
				{
					D_OUT.writeBytes("<b>");
					D_OUT.writeBytes("\n"+padSTRING('R',"Production Request Received in the month of "+tblRQREC.getValueAt(i,TB1_DOCNO).toString(),60));
					D_OUT.writeBytes("</b>");
				}
				else
				{
					D_OUT.writeBytes(padSTRING('R',tblRQREC.getValueAt(i,TB1_DOCNO).toString(),10));
					D_OUT.writeBytes(padSTRING('R',tblRQREC.getValueAt(i,TB1_PRDDS).toString(),33));
					D_OUT.writeBytes(padSTRING('R',tblRQREC.getValueAt(i,TB1_PRDCT).toString(),13));
					D_OUT.writeBytes(padSTRING('R',tblRQREC.getValueAt(i,TB1_PRTNM).toString(),40));
					D_OUT.writeBytes(padSTRING('L',tblRQREC.getValueAt(i,TB1_REQQT).toString(),7));
					D_OUT.writeBytes(padSTRING('L',tblRQREC.getValueAt(i,TB1_DOCDT).toString(),13));
					D_OUT.writeBytes("  "+padSTRING('R',tblRQREC.getValueAt(i,TB1_STSFL).toString(),25));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(tblRQREC.getValueAt(i,TB1_DCLDT).toString(),"-"),17));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(tblRQREC.getValueAt(i,TB1_LINNO).toString(),"-"),10));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(tblRQREC.getValueAt(i,TB1_EDLDT).toString(),"-"),21));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(tblRQREC.getValueAt(i,TB1_LOTNO).toString(),"-"),12));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(tblRQREC.getValueAt(i,TB1_REMDS).toString(),"-"),30));
				}
					crtNWLIN();
			}
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes("\n"+padSTRING('R',"Production Request Pending for Clearance /On Hold /Planning",60)+"\n");
			D_OUT.writeBytes("</b>");
			for(int i=0;i<L_ROWCNT1;i++)
			{
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_DOCNO).toString(),10));
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_PRDDS).toString(),33));
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_PRDCT).toString(),13));
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_PRTNM).toString(),40));
				D_OUT.writeBytes(padSTRING('L',tblRQPEN.getValueAt(i,TB1_REQQT).toString(),7));
				D_OUT.writeBytes(padSTRING('L',tblRQPEN.getValueAt(i,TB1_DOCDT).toString(),13));
				D_OUT.writeBytes("  "+padSTRING('R',"",25));
			//	D_OUT.writeBytes("\t"+padSTRING('R',tblRQPEN.getValueAt(i,TB1_STSFL).toString(),30));
				D_OUT.writeBytes(padSTRING('R',nvlSTRVL(tblRQPEN.getValueAt(i,TB1_DCLDT).toString(),""),17));
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_LINNO).toString(),10));
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_EDLDT).toString(),21));
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_LOTNO).toString(),12));
				D_OUT.writeBytes(padSTRING('R',tblRQPEN.getValueAt(i,TB1_REMDS).toString(),30));
				crtNWLIN();
			}
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes("\n"+padSTRING('R',"Production Request PENDING FOR Raw Material and Additives",60)+"\n");
			D_OUT.writeBytes("</b>");
			for(int i=0;i<L_ROWCNT2;i++)
			{
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_DOCNO).toString(),10));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_PRDDS).toString(),33));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_PRDCT).toString(),13));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_PRTNM).toString(),40));
				D_OUT.writeBytes(padSTRING('L',tblRMPEN.getValueAt(i,TB1_REQQT).toString(),7));
				D_OUT.writeBytes(padSTRING('L',tblRMPEN.getValueAt(i,TB1_DOCDT).toString(),13));
				D_OUT.writeBytes("  "+padSTRING('R',"",25));
				//D_OUT.writeBytes("\t"+padSTRING('R',tblRMPEN.getValueAt(i,TB1_STSFL).toString(),30));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_DCLDT).toString(),17));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_LINNO).toString(),10));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_EDLDT).toString(),21));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_LOTNO).toString(),12));
				D_OUT.writeBytes(padSTRING('R',tblRMPEN.getValueAt(i,TB1_REMDS).toString(),30));
				crtNWLIN();
			}
			
			genRPFTR();
			
			D_OUT.close();
			F_OUT.close();
		     
			if(M_rstRSSET==null)
			{
				M_rstRSSET.close();
			}	
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	void genRPHDR()
	{
		try
		{
			/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}*/		
			//if(M_rdbHTML.isSelected())
			//{
				D_OUT.writeBytes("<b>");
				//D_OUT.writeBytes("<HTML><HEAD><Title>Lotwise Bagging Despatch Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<HTML><HEAD></HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");   
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			//}	
			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		//prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,155));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
    		//prnFMTCHR(D_OUT,M_strNOBOLD);
    		D_OUT.writeBytes(padSTRING('R',"PRODUCTION REQUESTS STATUS OF SPS PLANT",155));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
			crtNWLIN();
			
			
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("PR. NO    GRADE                            PRODUCT CAT  CUSTOMER                            QUANTITY MT   PR REC ON   STATUS                  DELIVERY DATE    LINE NO   DATE OF DECLARATION  LOT NUMBER  REMARKS                       ");
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			// 	prnFMTCHR(D_OUT,M_strNOBOLD);
		//	if(M_rdbHTML.isSelected())
			D_OUT.writeBytes("</b>");
			
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
			
		}
	}
	
	
/**print Footer**/
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			//	prnFMTCHR(D_OUT,M_strEJT);		
			//if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(cl_dat.M_intLINNO_pbst >72)
			{	
				genRPFTR();
				genRPHDR();			
			}			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}
	
	void exeREPFL()
	{
		try
		{
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(chkREPFL.isSelected())
			     strRPFNM = strRPLOC + "pr_rprqa.html";
			
	    		genRPTFL();
				
			if(chkREPFL.isSelected())
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
				
				//p  = r.exec("D:\\Program Files\\Microsoft Office\\Office12\\excel.exe "+strRPFNM);
				//p  = r.exec("c:\\windows\\excel.exe "+strRPFNM);
				//p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				   p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeREPFL");
		}
	}
	
	/**class to verify DOC No & Work Shift**/
	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{	
			try
			{
				 if(getSource()==tblRQREC)
			     {
					if(tblRQREC.getValueAt(P_intROWID,P_intCOLID).toString().length()== 0)
						return true;	
					if(P_intCOLID==TB1_LINNO)
					{
						M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN ";
						M_strSQLQRY += " WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='PRXXLIN' AND CMT_CCSVL like '02_%' ";
						M_strSQLQRY += " AND CMT_CODCD='"+tblRQREC.getValueAt(P_intROWID,TB1_LINNO).toString().trim()+"'"	;
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							tblRQREC.setValueAt(M_rstRSSET.getString("CMT_CODCD"),P_intROWID,TB1_LINNO);
							setMSG("",'N');
						}
						else
						{
							setMSG("Enter Valid Line No",'E');
							return false;
						}
					}
					else if(P_intCOLID==TB1_DCLDT)
				    {
						if(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWID,TB1_DCLDT).toString().trim()).compareTo(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWID,TB1_DOCDT).toString().trim()))<0)
						{
							setMSG("Delivery Date Should be greater than PR Received On Date..",'E');
							return false;
						}
						else
							setMSG("",'N');
				    }
					else if(P_intCOLID==TB1_EDLDT)
				    {
						if(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWID,TB1_EDLDT).toString().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
						{
							setMSG("Declaration Date Should not be greater than Current Date..",'E');
							return false;
						}
						else if(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWID,TB1_EDLDT).toString().trim()).compareTo(M_fmtLCDAT.parse(tblRQREC.getValueAt(P_intROWID,TB1_DOCDT).toString().trim()))<0)
						{
							setMSG("Declaration Date Should be greater than PR Received On Date..",'E');
							return false;
						}
						else
							setMSG("",'N');	
				    }
					else if(P_intCOLID==TB1_STSFL)
				    {
						if(tblRQREC.getValueAt(P_intROWID,TB1_STSFL).toString().trim().equals(hstSTSDS.get("03")))
						{
							if(!tblRQREC.getValueAt(P_intROWID,TB1_PRCQT).toString().trim().equals(tblRQREC.getValueAt(P_intROWID,TB1_PLCQT).toString().trim()))
							{
								tblRQREC.setValueAt(hstSTSDS.get("04"),P_intROWID,TB1_STSFL);
								setMSG("",'N');
							}
							else
							{
								tblRQREC.setValueAt(hstSTSDS.get("03"),P_intROWID,TB1_STSFL);
								return false;
							}
						}
						else if(tblRQREC.getValueAt(P_intROWID,TB1_STSFL).toString().trim().equals(hstSTSDS.get("05")))
						{
							if(!tblRQREC.getValueAt(P_intROWID,TB1_DCLQT).toString().trim().equals(tblRQREC.getValueAt(P_intROWID,TB1_PRCQT).toString().trim()))
							{
								tblRQREC.setValueAt(hstSTSDS.get("09"),P_intROWID,TB1_STSFL);
								setMSG("",'N');
							}
							else
							{
								tblRQREC.setValueAt(hstSTSDS.get("05"),P_intROWID,TB1_STSFL);
								return false;
							}
						}
				    }
					
			     }
				 if(getSource()==tblRQPEN)
			     {
					if(P_intCOLID==TB1_STSFL)
				    {
						if(tblRQPEN.getValueAt(P_intROWID,TB1_STSFL).toString().trim().equals(hstSTSDS.get("01")))
						if(!tblRQPEN.getValueAt(P_intROWID,TB1_PLCQT).toString().trim().equals(tblRQPEN.getValueAt(P_intROWID,TB1_REQQT).toString().trim()))
						{
							tblRQPEN.setValueAt(hstSTSDS.get("12"),P_intROWID,TB1_STSFL);
							setMSG("",'N');
						}
						else
						{
							tblRQPEN.setValueAt(hstSTSDS.get("01"),P_intROWID,TB1_STSFL);
							return false;
						}
				    }
			     }
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"class TBLINPVF");
			}
			setMSG("",'N');
			return true;
		}
    }
}
	
	
	
	