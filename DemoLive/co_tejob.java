/*
System Name   : System Administration
Program Name  : Pending job Status
Program Desc. : Entry Screen forJob Status.
Author        : Mr. S.R.Mehesare
Date          : 2th Feb 2006
System        : System admin 
Version       : MMS v2.0.0
Modificaitons :  
Modified By   : 
Modified Date : 
Modified det. : 
Version       : 
*/

import java.sql.ResultSet;

import javax.swing.*;
import java.awt.Color;
import java.util.Hashtable;
import javax.swing.border.*;
import java.awt.Component;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Arrays;
import java.awt.Cursor;

/**
 * <P>
 * <PRE style = font-size : 10 pt > <b>System Name :</b> Sytem Admin.
 * 
 * <b>Program Name :</b> Pending Job Status and Call Log Book Status.
 * 
 * <b>Purpose :</b> This Module capture Pending Job Status and Call Log Book
 * Status.Here we canbe able add new Records, Modify existing Records and to
 * Delete unwanted records.
 * 
 * List of tables used : Table Name Primary key Operation done Insert Update
 * Query Delete
 * ----------------------------------------------------------------------------------------------------------
 * SA_JBMST JB_SRLNO # # # #
 * ----------------------------------------------------------------------------------------------------------
 * 
 * List of fields accepted/displayed on screen : Field Name Column Name Table
 * name Type/Size Description
 * ----------------------------------------------------------------------------------------------------------
 * 
 * ----------------------------------------------------------------------------------------------------------
 * 
 * List of fields with help facility : Field Name Display Description Display
 * Columns Table Name
 * ----------------------------------------------------------------------------------------------------------
 * 
 * ----------------------------------------------------------------------------------------------------------
 * Validations : 1)Date should be in date format.
 */

class co_tejob extends cl_pbase implements MouseListener
{
	//private JComboBox cmbGRPCD;
	private JComboBox cmbSYSCD;
	private JComboBox cmbJOBTP;
	private JComboBox cmbJOBOR;
	private JComboBox cmbJOBCT;
	private JComboBox cmbDVCNM;
	
	private JTextField txtGRPCD;
	private JTextField txtSRLNO;
	private JTextField txtREPBY;
	private JTextField txtREPDP;	
	private JTextField txtREPDT;
	private JTextField txtREPTM;
	private JTextField txtJOBDS;
	//private JTextField txtDVCNM;
	private JTextField txtMDLNO;
	private JTextField txtHWSRL;
	private JTextField txtSFTCD;
	private JTextField txtSWVER;
	private JTextField txtPRGCD;	
	private JTextField txtPLNDT;
	private JTextField txtPLNDY;	
	private JTextField txtSTRDT;
	private JTextField txtSTRTM;
	private JTextField txtENDDT;
	private JTextField txtENDTM;
		
	private JTextField txtALCTO;
	private JTextField txtACTDY;
	private JTextField txtREMDS;
	
	private JTextField txtGRPCD1;
	private JTextField txtSYSCD;
	private JTextField txtJOBTP;
	private JTextField txtJOBOR;
	private JTextField txtJOBCT;
	private JTextField txtDVCNM;
	private JTextField txtPRTNO;
	private JCheckBox chkCPAFL;
	private JCheckBox chkOJTFL;
	private JButton btnDSPLY;
	private JCheckBox chkINPRG;
	private JCheckBox chkPRTNO1;
	private JTextArea txtSTSDS;
	private JLabel lblSTSDS;
	//txtGRPCD	txtSYSCD	txtJOBTP	txtJOBOR	txtJOBCT	txtDVCNM
	
	private JTextField txtJOBDS1;
	private JTextField txtPRTNO1;
	private JTextField txtPLNDY1;
	private JTextField txtSTRDT1;
	private JTextField txtSTRTM1;
	private JTextField txtENDDT1;
	private JTextField txtENDTM1;
	private JTextField txtPRNDT1;
	private JTextField txtALCTO1;
	
	
	private String L_strVCATE="";
	private cl_JTable tblJOBDL;			/** textFields are attached to tables column */
	private JTabbedPane jtpMANTAB;          /** main tab for visitors tables */
	private JPanel pnlJBADD;
	private JPanel pnlSTSDS;
	private JPanel pnlJBMOD;	           /** panel for Visitor details and contractors details */
	
	private final int TB1_CHKFL = 0;
	private final int TB1_SRLNO = 1;
	private final int TB1_SYSCD = 2;
	private final int TB1_JOBDS = 3;
	private final int TB1_PRTNO = 4;
	private final int TB1_PLNDY = 5;
	private final int TB1_JOBCT = 6;
	private final int TB1_STRDT = 7;	
	private final int TB1_ENDDT = 8;
	private final int TB1_PLNDT = 9;
	private final int TB1_REPDT = 10;
	private final int TB1_REPBY = 11;
	private final int TB1_JOBTP = 12;
	private final int TB1_JOBOR = 13;
	private final int TB1_DVCNM = 14;
	private final int TB1_ALCTO = 15;

	private int intRECCT = 0;
	private String strCOMTP;
	private String strSYSCD;
	private String strJOBTP;
	private String strJOBOR;
	private JLabel lbl0,lbl4;
	private boolean flgDATA = false;
	
	String strSRLNO;	
	Hashtable<String,String> hstCODDS = new Hashtable<String,String>();
	Hashtable<String,String> hstCODD1 = new Hashtable<String,String>();
	Hashtable<String,String> hstGRPCD = new Hashtable<String,String>();
	int intROWCT = 0;
	co_tejob()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			pnlJBADD = new JPanel();
			pnlSTSDS = new JPanel();
			pnlJBMOD = new JPanel();
			pnlJBADD.setLayout(null);
			pnlSTSDS.setLayout(null);

			add(lbl0 = new JLabel("Complaint Registration Details"),1,2,1,3,pnlJBADD,'L');
			lbl0.setForeground(Color.blue);
			
			add(new JLabel("System Code"),2,2,1,1,pnlJBADD,'R');
			add(cmbSYSCD = new JComboBox(),2,3,1,2,pnlJBADD,'L');	
			add(txtGRPCD = new JTextField(),2,5,1,1.3,pnlJBADD,'L');
			//add(new JLabel("Group"),2,4,1,.7,pnlJBADD,'R');
			//add(cmbGRPCD = new JComboBox(),2,5,1,1.3,pnlJBADD,'L');
			add(new JLabel("Job Type"),2,6,1,.7,pnlJBADD,'R');
			add(cmbJOBTP = new JComboBox(),2,7,1,1.4,pnlJBADD,'L');
			
			add(new JLabel("Job Origin "),3,2,1,1,pnlJBADD,'L');
			add(cmbJOBOR = new JComboBox(),3,3,1,1.3,pnlJBADD,'L');			
			add(new JLabel("Category"),3,4,1,.7,pnlJBADD,'R');
			add(cmbJOBCT = new JComboBox(),3,5,1,1.3,pnlJBADD,'L');
			add(new JLabel("Serial No."),3,6,1,.7,pnlJBADD,'R');
			add(txtSRLNO = new TxtLimit(8),3,7,1,1,pnlJBADD,'L');

			add(new JLabel("Reported By"),4,2,1,1,pnlJBADD,'L');
			add(txtREPBY = new TxtLimit(3),4,3,1,1.3,pnlJBADD,'L');						
			add(new JLabel("Dept."),4,4,1,.7,pnlJBADD,'R');
			add(txtREPDP = new TxtNumLimit(3),4,5,1,1.3,pnlJBADD,'L');			
			add(new JLabel("DateTime"),4,6,1,.7,pnlJBADD,'R');
			add(txtREPDT = new TxtDate(),4,7,1,1,pnlJBADD,'L');			
			add(txtREPTM = new TxtTime(),4,8,1,.7,pnlJBADD,'L');

			add(new JLabel("Job Description"),5,2,1,1,pnlJBADD,'L');
			add(txtJOBDS = new TxtLimit(100),5,3,1,5.7,pnlJBADD,'L');			
			
			add(lbl4 = new JLabel("Plainning Details"),6,2,1,3,pnlJBADD,'L');
			lbl4.setForeground(Color.blue);
						
			add(new JLabel("Planned Date"),7,2,1,1,pnlJBADD,'R');
			add(txtPLNDT = new TxtDate(),7,3,1,1,pnlJBADD,'L');
			add(new JLabel("Planned Days"),7,4,1,1,pnlJBADD,'L');
			add(txtPLNDY = new TxtNumLimit(5.2),7,5,1,1,pnlJBADD,'L');			

            add(new JLabel("Start Date"),8,2,1,1,pnlJBADD,'L');
			add(txtSTRDT = new TxtDate(),8,3,1,1,pnlJBADD,'L');
			add(new JLabel("Start Time"),8,4,1,1,pnlJBADD,'L');
			add(txtSTRTM = new TxtTime(),8,5,1,1,pnlJBADD,'L');
			add(new JLabel("Allocated To"),8,6,1,.9,pnlJBADD,'R');
			add(txtALCTO = new JTextField(),8,7,1,1,pnlJBADD,'L');
			
			add(new JLabel("End Date"),9,2,1,1,pnlJBADD,'L');
			add(txtENDDT = new TxtDate(),9,3,1,1,pnlJBADD,'L');
			add(new JLabel("End Time"),9,4,1,1,pnlJBADD,'L');
			add(txtENDTM = new TxtTime(),9,5,1,1,pnlJBADD,'L');
			add(new JLabel("Actual Days"),9,6,1,.9,pnlJBADD,'R');
			add(txtACTDY = new JTextField(),9,7,1,1,pnlJBADD,'L');

			add(new JLabel("Remark"),10,2,1,1,pnlJBADD,'L');
			add(txtREMDS = new TxtLimit(100),10,3,1,5,pnlJBADD,'L');
			
			add(new JLabel("Priority No(10-90)"),11,2,1,1,pnlJBADD,'L');
			add(txtPRTNO = new TxtNumLimit(2),11,3,1,1,pnlJBADD,'L');
			
			add(chkCPAFL = new JCheckBox("CAPA flag"),11,5,1,2,pnlJBADD,'L');
			
			add(chkOJTFL = new JCheckBox("OJT flag"),11,7,1,2,pnlJBADD,'L');

			add(new JLabel("H/W Device"),12,2,1,1,pnlJBADD,'R');
			add(cmbDVCNM = new JComboBox(),12,3,1,1.2,pnlJBADD,'L');
			add(new JLabel("Model"),12,4,1,.7,pnlJBADD,'R');
			add(txtMDLNO = new TxtLimit(20),12,5,1,3,pnlJBADD,'L');

			add(new JLabel("Device Sr.No."),13,2,1,1,pnlJBADD,'R');
			add(txtHWSRL = new TxtLimit(30),13,3,1,5,pnlJBADD,'L');
			
			add(new JLabel("S/W Name"),14,2,1,1,pnlJBADD,'R');
			add(txtSFTCD = new TxtLimit(15),14,3,1,1.2,pnlJBADD,'L');
			add(new JLabel("Version"),14,4,1,.7,pnlJBADD,'R');
			add(txtSWVER = new TxtLimit(15),14,5,1,3,pnlJBADD,'L');

			add(new JLabel("ERP Module"),15,2,1,1,pnlJBADD,'R');
			add(txtPRGCD = new TxtLimit(10),15,3,1,1.2,pnlJBADD,'L');			

			add(lblSTSDS = new JLabel("Enter Program Description"),2,2,1,2,pnlSTSDS,'L');
			//add(txtSTSDS = new JTextArea(),5,2,8,6,pnlSTSDS,'L');			
			lblSTSDS.setForeground(Color.blue);
			//txtSTSDS.setBorder(new EtchedBorder(Color.black,Color.lightGray));

			txtSTSDS = new JTextArea();	
			JScrollPane scrollPane=new JScrollPane(txtSTSDS);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 			scrollPane.setBounds(10, 10, 300, 200);
			add(scrollPane,5,2,8,6,pnlSTSDS,'L');

			jtpMANTAB = new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			pnlJBMOD.setLayout(null);
			
			add(new JLabel("Main Group"),1,1,1,1,pnlJBMOD,'R');
			add(txtGRPCD1 = new JTextField(),1,2,1,1,pnlJBMOD,'L');
			add(new JLabel("System Code"),1,3,1,1,pnlJBMOD,'R');
			add(txtSYSCD = new TxtLimit(2),1,4,1,1,pnlJBMOD,'L');
			add(new JLabel("Job Type"),1,5,1,1,pnlJBMOD,'R');
			add(txtJOBTP = new TxtLimit(2),1,6,1,1,pnlJBMOD,'L');
			add(chkINPRG = new JCheckBox("In Progress"),1,7,1,1,pnlJBMOD,'L');
			add(chkPRTNO1 = new JCheckBox("Priority Wise"),1,8,1,1,pnlJBMOD,'L');
										 
			add(new JLabel("Job Origin"),2,1,1,1,pnlJBMOD,'R');			
			add(txtJOBOR = new TxtLimit(15),2,2,1,1,pnlJBMOD,'L');
			add(new JLabel("Category"),2,3,1,1,pnlJBMOD,'R');
			add(txtJOBCT = new TxtLimit(1),2,4,1,1,pnlJBMOD,'L');
			add(new JLabel("Device Type"),2,5,1,1,pnlJBMOD,'R');
			add(txtDVCNM = new TxtLimit(10),2,6,1,1,pnlJBMOD,'L');
			add(btnDSPLY = new JButton("Display"),2,7,1,1,pnlJBMOD,'L');			
			
			String[] L_strCOLHD = {"Select","Serial No.","Sys","Description","Priority","Pln Days","Category","Start Date Time","End Date Time","Planing Date","Report Date","Reported By","Job Type","JobOrigin","Device Details","Allocated To"};
      		int[] L_intCOLSZ = {10,70,40,350,50,50,30,100,100,100,100,80,60,60,100,80,70};
			tblJOBDL = crtTBLPNL1(pnlJBMOD,L_strCOLHD,500,3,1,13.5,7.9,L_intCOLSZ,new int[]{0});
			tblJOBDL.addMouseListener(this);
			tblJOBDL.addFocusListener(this);
			jtpMANTAB.addTab("Call Details",pnlJBADD);
			jtpMANTAB.addTab("Status Description",pnlSTSDS);
			jtpMANTAB.addTab("Call Log ",pnlJBMOD);
			add(jtpMANTAB,1,1,18,8,this,'L');	

			setENBL(false);			
		
			M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
			+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXGRP'"
			+" AND isnull(CMT_STSFL,'')<>'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstCODD1.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			
			M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
			+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXJOB'"
			+" AND isnull(CMT_STSFL,'')<>'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strCODCD="",L_strCODDS="";
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					hstCODDS.put(L_strCODCD,L_strCODDS);
					if(L_strCODCD.substring(0,1).equals("1"))
						cmbJOBTP.addItem(L_strCODCD+" "+L_strCODDS);
					else if(L_strCODCD.substring(0,1).equals("2"))
						cmbJOBOR.addItem(L_strCODCD+" "+L_strCODDS);					
					else if(L_strCODCD.substring(0,1).equals("4"))
						cmbDVCNM.addItem(L_strCODCD+" "+L_strCODDS);
					else 
						cmbJOBCT.addItem(L_strCODCD+" "+L_strCODDS);
				}
				M_rstRSSET.close();
			}
			
			cmbSYSCD.removeActionListener(this);
			M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS,CMT_CHP01 from CO_CDTRN where"
				+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXGRP'"
				+" AND isnull(CMT_STSFL,'')<>'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			String L_strCODCD="",L_strCODDS="";
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbSYSCD.addItem(L_strCODCD);
					hstGRPCD.put(L_strCODCD,nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),""));
				}
				M_rstRSSET.close();
			}
			cmbSYSCD.addActionListener(this);
			
			tblJOBDL.setCellEditor(TB1_JOBDS, txtJOBDS1 = new TxtLimit(100));	txtJOBDS1.addKeyListener(this);txtJOBDS1.addFocusListener(this);
			tblJOBDL.setCellEditor(TB1_PRTNO, txtPRTNO1 = new TxtNumLimit(2));	txtPRTNO1.addKeyListener(this);
			tblJOBDL.setCellEditor(TB1_PLNDY, txtPLNDY1 = new TxtNumLimit(5.2));txtPLNDY1.addKeyListener(this);
			tblJOBDL.setCellEditor(TB1_ALCTO, txtALCTO1 = new TxtLimit(3));		txtALCTO1.addKeyListener(this);				
			tblJOBDL.setSelectionForeground(Color.blue);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super class method overrided to enable & disable the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		if(L_flgSTAT == true)
			txtGRPCD.setText(hstGRPCD.get(cmbSYSCD.getSelectedItem().toString()).toString());
		txtGRPCD.setEnabled(false);
		txtREPDT.setEnabled(L_flgSTAT);
		txtREPDP.setEnabled(L_flgSTAT);
		txtREMDS.setEnabled(L_flgSTAT);
		txtPRTNO.setEnabled(L_flgSTAT);
		txtSTSDS.setEnabled(L_flgSTAT);
		txtSTRDT.setEnabled(L_flgSTAT);
		txtENDDT.setEnabled(L_flgSTAT);
		txtPLNDY.setEnabled(L_flgSTAT);
		txtREPBY.setEnabled(L_flgSTAT);
		txtJOBDS.setEnabled(L_flgSTAT);
		txtALCTO.setEnabled(L_flgSTAT);
		txtPLNDT.setEnabled(L_flgSTAT);
		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			txtSRLNO.setEnabled(false);
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			txtSRLNO.setEnabled(true);
		
		tblJOBDL.cmpEDITR[TB1_SYSCD].setEnabled(false);
		//tblJOBDL.cmpEDITR[TB1_JOBCT].setEnabled(false);
		tblJOBDL.cmpEDITR[TB1_REPDT].setEnabled(false);
		tblJOBDL.cmpEDITR[TB1_REPBY].setEnabled(false);
		tblJOBDL.cmpEDITR[TB1_JOBTP].setEnabled(false);
		tblJOBDL.cmpEDITR[TB1_JOBOR].setEnabled(false);
		tblJOBDL.cmpEDITR[TB1_DVCNM].setEnabled(false);
		tblJOBDL.cmpEDITR[TB1_SRLNO].setEnabled(false);
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
		{	
//			System.out.println("DELETE");
			chkINPRG.setEnabled(true);
			chkPRTNO1.setEnabled(true);
			cmbSYSCD.setEnabled(true);
			txtGRPCD1.setEnabled(true);
			txtSYSCD.setEnabled(true);
			txtJOBTP.setEnabled(true);
			txtJOBOR.setEnabled(true);
			txtJOBCT.setEnabled(true);
			txtDVCNM.setEnabled(true);
			btnDSPLY.setEnabled(true);
			tblJOBDL.cmpEDITR[TB1_CHKFL].setEnabled(true);
			tblJOBDL.cmpEDITR[TB1_JOBDS].setEnabled(false);
			tblJOBDL.cmpEDITR[TB1_PRTNO].setEnabled(false);
			tblJOBDL.cmpEDITR[TB1_PLNDY].setEnabled(false);
			tblJOBDL.cmpEDITR[TB1_JOBCT].setEnabled(false);
			tblJOBDL.cmpEDITR[TB1_STRDT].setEnabled(false);
			tblJOBDL.cmpEDITR[TB1_ENDDT].setEnabled(false);
			tblJOBDL.cmpEDITR[TB1_ALCTO].setEnabled(false);
		}
	}
	void clrTEXT()
	{
		txtREPDT.setText("");
		txtREPDP.setText("");
		txtREMDS.setText("");
		txtPRTNO.setText("");
		chkCPAFL.setSelected(false);
		chkOJTFL.setSelected(false);
		txtSTSDS.setText("");
		txtSTRDT.setText("");
		txtENDDT.setText("");
		txtPLNDY.setText("");
		txtREPBY.setText("");
		txtJOBDS.setText("");
		txtALCTO.setText("");
		txtPLNDT.setText("");
		txtSRLNO.setText("");
	}
	public void actionPerformed(ActionEvent L_AE)
	{       
		super.actionPerformed(L_AE);    
		try 
		{   
 			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{   
				clrTEXT();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					if(flgDATA == false)
					{
						flgDATA = true;						
						cmbDVCNM.setEnabled(false);
						txtMDLNO.setEnabled(false);
						txtHWSRL.setEnabled(false);
					}
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
					{
						setENBL(false);
						txtGRPCD.setText(hstGRPCD.get(cmbSYSCD.getSelectedItem().toString()).toString());
						txtSRLNO.setEnabled(true);
						return;
					}
					else
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							txtREPBY.requestFocus();
							setMSG("Please Enter Initial of the Person who reported the complaint..",'N');						
						}
						else 
						{
							txtSRLNO.requestFocus();
							setMSG("Please Enter Serial Number..",'N');
						}	
						setENBL(true);															
					}
				}
				else				
					setENBL(false);
			}
			else if(M_objSOURC == cmbSYSCD)
			{
				String L_strGRPCD = hstGRPCD.get(cmbSYSCD.getSelectedItem().toString()).toString();				
				txtGRPCD.setText(L_strGRPCD);
				if(L_strGRPCD.equals("HARDWARE"))
				{
					cmbDVCNM.setEnabled(true);
					txtMDLNO.setEnabled(true);
					txtHWSRL.setEnabled(true);
				}
				else
				{
					cmbDVCNM.setEnabled(false);
					txtMDLNO.setEnabled(false);
					txtHWSRL.setEnabled(false);
				}
			}			
			else if(M_objSOURC == txtSRLNO)
			{
				if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) && (txtSRLNO.getText().trim().length() == 8))
					getDATA();
			}
			if((M_objSOURC == cmbSYSCD) || (M_objSOURC == cmbJOBTP) || (M_objSOURC == cmbJOBOR) ||(M_objSOURC == cmbJOBCT))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					txtREPBY.requestFocus();
					setMSG("Please Enter Initial of the Person who reported the complaint..",'N');						
				}
				else 
				{
					txtSRLNO.requestFocus();
					setMSG("Please Enter Serial Number..",'N');
				}
			}
			if(M_objSOURC == btnDSPLY)
			{
				tblJOBDL.clrTABLE();
				if(tblJOBDL.isEditing())
					tblJOBDL.getCellEditor().stopCellEditing();
				tblJOBDL.setRowSelectionInterval(0,0);
				tblJOBDL.setColumnSelectionInterval(0,0);	
				if(chkINPRG.isSelected() ==  true)
				{
					M_strSQLQRY=" SELECT * from SA_JBMST "
					+" where isnull(JB_STSFL,'')<>'X'"
					+" AND JB_STRDT is not null"
					+" AND JB_ENDDT is null"
					+" Order by JB_SYSCD,JB_JOBTP";
					M_strSQLQRY+= chkPRTNO1.isSelected() ? ",JB_PRTNO;" : ";";
				}
				else
				{
					M_strSQLQRY=" SELECT * from SA_JBMST "
						+" where isnull(JB_STSFL,'')<>'X' AND JB_STSFL <> '9'";				
					if(txtGRPCD1.getText().length() == 2)
						M_strSQLQRY += " AND JB_GRPCD = '"+txtGRPCD1.getText().trim().toUpperCase()+"'";
					if(txtSYSCD.getText().length() == 2)
						M_strSQLQRY += " AND JB_SYSCD = '"+txtSYSCD.getText().trim()+"'";
					if(txtJOBTP.getText().length() ==2)
						M_strSQLQRY += " AND JB_JOBTP = '"+txtJOBTP.getText().trim()+"'";
					if(txtJOBOR.getText().length() == 2)
						M_strSQLQRY += " AND JB_JOBOR = '"+txtJOBOR.getText().trim()+"'";
					if(txtJOBCT.getText().length() == 1)
						M_strSQLQRY += " AND JB_JOBCT = '"+txtJOBCT.getText().trim().toUpperCase()+"'";
					if(txtDVCNM.getText().length() > 0)
						M_strSQLQRY += " AND JB_DVCNM like '"+txtDVCNM.getText().trim()+"%'";
					M_strSQLQRY +=" Order by JB_SYSCD,JB_JOBTP";
					M_strSQLQRY += chkPRTNO1.isSelected() ? ",JB_PRTNO" : "";
				}
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
				if(M_rstRSSET != null)
				{
					int L_intROWNO = 0;
					String L_strTEMP = "";
					java.sql.Timestamp L_tmsTEMP;
					java.sql.Date L_tmpDATE;
					while(M_rstRSSET.next())
					{
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_SYSCD"),""),L_intROWNO,TB1_SYSCD); 						
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),""),L_intROWNO,TB1_JOBDS);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_PRTNO"),""),L_intROWNO,TB1_PRTNO);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_PLNDY"),""),L_intROWNO,TB1_PLNDY);							
						
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBCT"),"");
						if(hstCODDS.containsKey(L_strTEMP))
							tblJOBDL.setValueAt(L_strTEMP,L_intROWNO,TB1_JOBCT);						//+" "+hstCODDS.get(L_strTEMP).toString()
						
						L_tmsTEMP = M_rstRSSET.getTimestamp("JB_STRDT");
						if(L_tmsTEMP != null)
							L_strTEMP = M_fmtLCDTM.format(L_tmsTEMP);
						else
							L_strTEMP = "";						
						tblJOBDL.setValueAt(L_strTEMP,L_intROWNO,TB1_STRDT);
						
						L_tmsTEMP = M_rstRSSET.getTimestamp("JB_ENDDT");
						if(L_tmsTEMP != null)
							L_strTEMP = M_fmtLCDTM.format(L_tmsTEMP);
						else
							L_strTEMP = "";							
						tblJOBDL.setValueAt(L_strTEMP,L_intROWNO,TB1_ENDDT);
												
						L_tmpDATE = M_rstRSSET.getDate("JB_PLNDT");
						if(L_tmpDATE != null)
							L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						else
							L_strTEMP = "";
						tblJOBDL.setValueAt(L_strTEMP,L_intROWNO,TB1_PLNDT);
						
						L_tmsTEMP = M_rstRSSET.getTimestamp("JB_REPDT");
						if(L_tmsTEMP != null)
							L_strTEMP = M_fmtLCDTM.format(L_tmsTEMP);
						else
							L_strTEMP = "";
						tblJOBDL.setValueAt(L_strTEMP,L_intROWNO,TB1_REPDT);						
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_REPBY"),""),L_intROWNO,TB1_REPBY);
						//tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_JOBTP"),""),L_intROWNO,TB1_JOBTP);
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBTP"),"");
						if(hstCODDS.containsKey(L_strTEMP))
							tblJOBDL.setValueAt(hstCODDS.get(L_strTEMP).toString(),L_intROWNO,TB1_JOBTP);
						
						//tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_JOBOR"),""),L_intROWNO,TB1_JOBOR);
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBOR"),"");
						if(hstCODDS.containsKey(L_strTEMP))
							tblJOBDL.setValueAt(hstCODDS.get(L_strTEMP).toString(),L_intROWNO,TB1_JOBOR);
						
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_DVCNM"),""),L_intROWNO,TB1_DVCNM);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_ALCTO"),""),L_intROWNO,TB1_ALCTO);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_SRLNO"),""),L_intROWNO,TB1_SRLNO);
						L_intROWNO++;
					}
					M_rstRSSET.close();
					intROWCT = L_intROWNO;
				}				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");			
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == tblJOBDL.cmpEDITR[TB1_JOBDS])
		{
			if(tblJOBDL.getSelectedRow() < intROWCT)			
				((JTextField)tblJOBDL.cmpEDITR[TB1_JOBDS]).setEditable(true);
			else
				((JTextField)tblJOBDL.cmpEDITR[TB1_JOBDS]).setEditable(false);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{ 
		super.keyPressed(L_KE);
		try
		 {
			if(L_KE.getKeyCode()== L_KE.VK_F1)
			{				
				if(M_objSOURC == txtREPBY)
				{
					M_strHLPFLD = "txtREPBY";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT EP_SHRNM,EP_FULNM,CMT_CODCD,CMT_CODDS from HR_EPMST,CO_CDTRN where"
						+" CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXDPT'"
						+" AND EP_DPTCD = CMT_CODCD";
					if(txtREPBY.getText().length() >0)
						M_strSQLQRY += " AND EP_SHRNM like '"+txtREPBY.getText().trim().toUpperCase()+"%'";

					cl_hlp(M_strSQLQRY,1,1,new String[]{"Employee Initial","Name","Department Code","Description"},4,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}					
				else if(M_objSOURC == txtREPDP)
				{
					M_strHLPFLD = "txtREPDP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXDPT'";
					if(txtREPDP.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtREPDP.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Department code","Department Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSRLNO)
				{
					M_strHLPFLD = "txtSRLNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_SRLNO,JB_JOBDS from SA_JBMST "
						+" where JB_SRLNO like '_"+ txtGRPCD.getText().substring(0,2).toUpperCase()+"%'"
						+" AND isnull(JB_STSFL,'')<>'X'";
					if(txtSRLNO.getText().length() >0)				
						M_strSQLQRY += " AND JB_SRLNO like '"+txtSRLNO.getText().trim()+"%'";				
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Serial No","Job Description"},2,"CT",new int[]{70,550});
					setCursor(cl_dat.M_curDFSTS_pbst);
				}				
				
				else if(M_objSOURC == txtMDLNO)
				{
					M_strHLPFLD = "txtMDLNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_MDLNO,JB_HWSRL from SA_JBMST "
						+" where JB_SRLNO like '_"+ txtGRPCD.getText().substring(0,2).toUpperCase()+"%'"
						+" AND JB_DVCNM = '"+ cmbDVCNM.getSelectedItem().toString()+"'"
						+" AND isnull(JB_STSFL,'')<>'X'";
					if(txtMDLNO.getText().length() >0)				
						M_strSQLQRY += " AND JB_MDLNO like '"+txtMDLNO.getText().trim()+"%'";				
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Model No","Serial Number"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtHWSRL)
				{
					M_strHLPFLD = "txtHWSRL";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_HWSRL from SA_JBMST "
						+" where JB_SRLNO like '_"+ txtGRPCD.getText().substring(0,2).toUpperCase()+"%'"
						+" AND JB_DVCNM = '"+ cmbDVCNM.getSelectedItem().toString() +"'"
						+" AND JB_MDLNO = '"+ txtMDLNO.getText().trim() +"'"
						+" AND isnull(JB_STSFL,'')<>'X'";
					if(txtHWSRL.getText().length() >0)				
						M_strSQLQRY += " AND JB_HWSRL like '"+txtHWSRL.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Serial Number"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSFTCD)
				{
					M_strHLPFLD = "txtSFTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_SFTCD from SA_JBMST"						
						+" where JB_SRLNO like '_"+ txtGRPCD.getText().substring(0,2).toUpperCase()+"%'"
						+" AND isnull(JB_STSFL,'')<>'X'";
					if(txtHWSRL.getText().length() >0)				
						M_strSQLQRY += " AND JB_SFTCD like '"+txtSFTCD.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Serial Number"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSWVER)
				{
					M_strHLPFLD = "txtSWVER";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_SWVER from SA_JBMST"						
						+" where isnull(JB_STSFL,'')<>'X'"
						+" AND JB_SRLNO like '_"+ txtGRPCD.getText().substring(0,2).toUpperCase()+"%'"
						+" AND JB_SFTCD = '"+ txtSFTCD.getText().trim() +"'";
					if(txtSWVER.getText().length() >0)				
						M_strSQLQRY += " AND JB_SWVER like '"+txtSWVER.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Software Version"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtPRGCD)
				{
					M_strHLPFLD = "txtPRGCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_PRGCD from SA_JBMST"
						+" where isnull(JB_STSFL,'')<>'X'"
						+" AND JB_SRLNO like '_"+ txtGRPCD.getText().substring(0,2).toUpperCase()+"%'";
					if(txtPRGCD.getText().length() >0)				
						M_strSQLQRY += " AND JB_PRGCD like '"+txtPRGCD.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Module Name"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}				
				else if(M_objSOURC == txtGRPCD1)
				{
					M_strHLPFLD = "txtGRPCD1";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct CMT_CHP01 from CO_CDTRN"		
						+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXGRP'"
						+" AND isnull(CMT_STSFL,'')<>'X'";										
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Complaint Type"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSYSCD)
				{
					M_strHLPFLD = "txtSYSCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct CMT_CODCD,CMT_CODDS from SA_JBMST,CO_CDTRN"						
						+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXGRP'"
						+" AND isnull(CMT_STSFL,'')<>'X'";					
					if(txtGRPCD1.getText().length() > 0)
						M_strSQLQRY += " AND CMT_CHP01 = '"+txtGRPCD1.getText().trim()+"'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"System Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtJOBTP)
				{
					M_strHLPFLD = "txtJOBTP";				
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN"
						+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXJOB'"
						+" AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like '1%'";					
					if(txtJOBTP.getText().length() > 0)
						M_strSQLQRY += " AND CMT_CODDS like '"+txtJOBTP.getText().trim()+"%'";				
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Type Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
					
					/*M_strHLPFLD = "txtJOBTP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_JOBTP from SA_JBMST"
						+" where isnull(JB_STSFL,'')<>'X'";
					if(txtJOBTP.getText().length() >0)
						M_strSQLQRY += " AND JB_JOBTP like '"+txtJOBTP.getText().trim()+"%'";					
					if(txtSYSCD.getText().length() >0)
						M_strSQLQRY += " AND JB_SYSCD = '"+txtSYSCD.getText().trim()+"'";
					if(txtGRPCD1.getText().length() == 2)
						M_strSQLQRY += " AND JB_GRPCD like '"+txtGRPCD1.getText().trim().toUpperCase()+"%'";				
					if(txtJOBOR.getText().length() == 2)
						M_strSQLQRY += " AND JB_JOBOR = '"+txtJOBOR.getText().trim()+"'";
					if(txtJOBCT.getText().length() == 1)
						M_strSQLQRY += " AND JB_JOBCT = '"+txtJOBCT.getText().trim().toUpperCase()+"'";
					if(txtDVCNM.getText().length() > 0)
						M_strSQLQRY += " AND JB_DVCNM = '"+txtDVCNM.getText().trim()+"'";
																		
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Type"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);*/					
				}
				else if(M_objSOURC == txtJOBOR)
				{
					M_strHLPFLD = "txtJOBOR";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN"
						+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXJOB'"
						+" AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like '2%'";
					if(txtJOBOR.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODDS like '"+txtJOBOR.getText().trim()+"%'";				
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Origin Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				/*	M_strHLPFLD = "txtJOBOR";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_JOBOR from SA_JBMST"
						+" where isnull(JB_STSFL,'')<>'X'";
					if(txtJOBOR.getText().length() >0)				
						M_strSQLQRY += " AND JB_JOBOR like '"+txtJOBOR.getText().trim()+"%'";
					if(txtJOBTP.getText().length() == 2)
						M_strSQLQRY += " AND JB_JOBTP = '"+txtJOBTP.getText().trim()+"'";
					if(txtSYSCD.getText().length()  == 2)
						M_strSQLQRY += " AND JB_SYSCD = '"+txtSYSCD.getText().trim()+"'";
					if(txtGRPCD1.getText().length() == 2)
						M_strSQLQRY += " AND JB_GRPCD like '"+txtGRPCD1.getText().trim().toUpperCase()+"%'";
					if(txtJOBCT.getText().length() == 1)
						M_strSQLQRY += " AND JB_JOBCT = '"+txtJOBCT.getText().trim().toUpperCase()+"'";
					if(txtDVCNM.getText().length() > 0)
						M_strSQLQRY += " AND JB_DVCNM like '"+txtDVCNM.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Origin"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);*/
				}
				else if(M_objSOURC == txtJOBCT)
				{
					M_strHLPFLD = "txtJOBCT";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_JOBCT from SA_JBMST"
						+" where isnull(JB_STSFL,'')<>'X'";
					if(txtJOBCT.getText().length() >0)				
						M_strSQLQRY += " AND JB_JOBCT like '"+txtJOBCT.getText().trim()+"%'";
					if(txtJOBOR.getText().length() == 2)
						M_strSQLQRY += " AND JB_JOBOR = '"+txtJOBOR.getText().trim()+"'";
					if(txtJOBTP.getText().length() == 2)
						M_strSQLQRY += " AND JB_JOBTP = '"+txtJOBTP.getText().trim()+"'";
					if(txtSYSCD.getText().length() == 2)
						M_strSQLQRY += " AND JB_SYSCD = '"+txtSYSCD.getText().trim()+"'";
					if(txtGRPCD1.getText().length() == 2)
						M_strSQLQRY += " AND JB_GRPCD like '"+txtGRPCD1.getText().trim().toUpperCase()+"%'";
					if(txtDVCNM.getText().length() > 0)
						M_strSQLQRY += " AND JB_DVCNM = '"+txtDVCNM.getText().trim()+"'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Category"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtDVCNM)
				{
					M_strHLPFLD = "txtDVCNM";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct JB_DVCNM from SA_JBMST"
						+" where isnull(JB_STSFL,'')<>'X'";
					if(txtDVCNM.getText().length() >0)				
						M_strSQLQRY += " AND JB_DVCNM like '"+txtDVCNM.getText().trim()+"%'";
					if(txtJOBCT.getText().length() == 1)
						M_strSQLQRY += " AND JB_JOBCT = "+txtJOBCT.getText().trim()+"'";
					if(txtJOBOR.getText().length() == 2)
						M_strSQLQRY += " AND JB_JOBOR = '"+txtJOBOR.getText().trim()+"'";
					if(txtJOBTP.getText().length() == 2)
						M_strSQLQRY += " AND JB_JOBTP = '"+txtJOBTP.getText().trim()+"'";
					if(txtSYSCD.getText().length() == 2)
						M_strSQLQRY += " AND JB_SYSCD = '"+txtSYSCD.getText().trim()+"'";
					if(txtGRPCD1.getText().length() == 2)
						M_strSQLQRY += " AND JB_GRPCD like '"+txtGRPCD1.getText().trim().toUpperCase()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Device Type"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if (L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtSRLNO)
				{					
					txtREPBY.requestFocus();
					setMSG("please Enter the initial of the person who Reported the Compaint..",'N');
				}
				else if(M_objSOURC == txtREPBY)
				{
					txtREPBY.setText(txtREPBY.getText().trim().toUpperCase());
					txtREPDP.requestFocus();
					setMSG("Enter a Job Reported date..",'N');
				}				
				else if(M_objSOURC == txtREPDP)
				{
					txtREPDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREPDT.requestFocus();
					setMSG("Enter a Job Reported date..",'N');
				}
				else if(M_objSOURC == txtREPDT)
				{
					txtREPTM.requestFocus();
					txtREPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					setMSG("Enter a Job Description..",'N');
				}
				else if(M_objSOURC == txtREPTM)
				{
					txtJOBDS.requestFocus();
					setMSG("Enter a Job Description..",'N');
				}				
					
				else if(M_objSOURC == txtJOBDS)
				{
					txtPLNDT.requestFocus();
					txtPLNDT.setText(cl_dat.M_strLOGDT_pbst);
					setMSG("Please Enter a the Software Code or Press F1 to select from List..",'N');					
				}
				else if(M_objSOURC == cmbDVCNM)
				{
					txtMDLNO.requestFocus();
					setMSG("Please Enter a Model No or press F1 from the List..",'N');
				}
				else if(M_objSOURC == txtMDLNO)
				{
					txtHWSRL.requestFocus();
					setMSG("Please Enter a Serial No of the HARDWARE or Press F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtHWSRL)
				{
					txtSFTCD.requestFocus();
					setMSG("Please Enter a the Software Code or Press F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtSFTCD)
				{
					txtSWVER.requestFocus();
					setMSG("Please Enter Version No of the Software..",'N');
				}
				else if(M_objSOURC == txtSWVER)
				{
					txtPRGCD.requestFocus();
					setMSG("Please Enter a the SPLERP Program Code or F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtPRGCD)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}				
				else if(M_objSOURC == txtPLNDT)
				{
					txtPLNDY.requestFocus();
					setMSG("Enter a Planned Days..",'N');
				}
				else if(M_objSOURC == txtPLNDY)
				{
					txtSTRDT.requestFocus();
					txtSTRDT.setText(cl_dat.M_strLOGDT_pbst);
					setMSG("Enter a Start Date..",'N');
				}
				else if(M_objSOURC == txtSTRDT)
				{
					txtSTRTM.requestFocus();
					txtSTRTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					setMSG("Enter a Start Job Time..",'N');
				}
				else if(M_objSOURC == txtSTRTM)
				{
					txtALCTO.requestFocus();
					setMSG("Enter a the Initial of the Person who has Allocated the Job..",'N');
				}
				else if(M_objSOURC == txtALCTO)
				{
					txtENDDT.requestFocus();
					txtENDDT.setText(cl_dat.M_strLOGDT_pbst);
					setMSG("Enter a Job End Date..",'N');
				}
				else if(M_objSOURC == txtENDDT)
				{
					txtENDTM.requestFocus();
					txtENDTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					setMSG("Enter a Job End Time..",'N');
				}
				else if(M_objSOURC == txtENDTM)
				{	
					txtREMDS.requestFocus();
					setMSG("Enter a Remark..",'N');
				}
				else if(M_objSOURC == txtREMDS)
				{	
					txtPRTNO.requestFocus();
					setMSG("Enter Priority No..",'N');
				}
				else if(M_objSOURC == txtPRTNO)
				{	
					chkCPAFL.requestFocus();
					setMSG("CAPA required?..",'N');
				}
				else if(M_objSOURC == chkCPAFL)
				{	
					chkOJTFL.requestFocus();
					setMSG("Any On Job Training?..",'N');
				}
				else if(M_objSOURC == chkOJTFL)
				{	
					if(txtGRPCD.getText().equals("HARDWARE"))
					{
						cmbDVCNM.requestFocus();
						setMSG("Please Select the Device Type..",'N');
					}
					else
					{
						txtSFTCD.requestFocus();
						setMSG("Please Enter a the Software Code or Press F1 to select from List..",'N');
					}
				}
				else if(M_objSOURC == txtGRPCD1)
				{
					txtSYSCD.requestFocus();
					setMSG("Please Enter a System Code press F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtSYSCD)
				{
					txtJOBTP.requestFocus();
					setMSG("Please Enter a Job Type or press F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtJOBTP)
				{
					txtJOBOR.requestFocus();
					setMSG("Please Enter a Job Origin or press F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtJOBOR)
				{
					txtJOBCT.requestFocus();
					setMSG("Please Enter a Job Category or press F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtJOBCT)
				{
					txtDVCNM.requestFocus();
					setMSG("Please Enter a Device Type or press F1 to select from List..",'N');
				}
				else if(M_objSOURC == txtDVCNM)
				{
					btnDSPLY.requestFocus();
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"VK_F1");			
		}
	}
	/**
	 *Super Class method overrided to execuate the F1 help for Selected TextField.
	 */
	public void exeHLPOK()
	{		
		super.exeHLPOK();		  	
		if(M_strHLPFLD == "txtREPBY")
		{
			txtREPBY.setText(cl_dat.M_strHLPSTR_pbst);
		 	txtREPDP.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
		}
		else if(M_strHLPFLD == "txtREPDP")
	    {			
			txtREPDP.setText(cl_dat.M_strHLPSTR_pbst);
		}
		else if(M_strHLPFLD.equals("txtSRLNO"))
		{
		  	txtSRLNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			txtJOBDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD == "txtMDLNO")
			txtMDLNO.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtHWSRL")
			txtHWSRL.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtSFTCD")
			txtSFTCD.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtSWVER")
			txtSWVER.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtPRGCD")
			txtPRGCD.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtGRPCD1")
			txtGRPCD1.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtSYSCD")
			txtSYSCD.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtJOBTP")
			txtJOBTP.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtJOBOR")
			txtJOBOR.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtJOBCT")
			txtJOBCT.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD == "txtDVCNM")
			txtDVCNM.setText(cl_dat.M_strHLPSTR_pbst);
 	}
	/**
	 * Super Class method to perform insert & update operation On the data base.
	 */
	void exeSAVE()
	{
		try
		{
			String L_strSTSFL = "0";
			cl_dat.M_flgLCUPD_pbst = true;
			if(jtpMANTAB.getSelectedIndex() == 0 || jtpMANTAB.getSelectedIndex() == 1)
			{
				if (!vldDATA())
					return;	
				else 
					setMSG("",'E');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgLCUPD_pbst = true;
					String L_strTEMP1 = "";			
					String L_strCODCD = cl_dat.M_strFNNYR_pbst.substring(3,4)+"00";
					M_strSQLQRY = "Select CMT_CCSVL from CO_CDTRN where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='COXXJOB' AND isnull(CMT_STSFL,'')<>'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					
					if(M_rstRSSET!= null)
					{				
						if(M_rstRSSET.next())				
							L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),""); 				
						M_rstRSSET.close();
					}						
					int L_intTSTNO = Integer.valueOf(L_strTEMP1).intValue()+1;
					strSRLNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + txtGRPCD.getText().substring(0,2).toUpperCase()
						+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
						+ String.valueOf(L_intTSTNO);					
					txtSRLNO.setText(strSRLNO);
					
					// Status flag
					if((txtSTRDT.getText().length()>0)&&(txtENDDT.getText().length()>0))
						L_strSTSFL = "9";
					else
						L_strSTSFL = "0";
					
					M_strSQLQRY = "INSERT INTO SA_JBMST(JB_GRPCD,JB_SYSCD,JB_JOBTP,JB_JOBOR,JB_JOBCT,"
						+"JB_SRLNO,JB_REPBY,JB_REPDP,JB_REPDT,JB_JOBDS,JB_DVCNM,JB_MDLNO,JB_HWSRL,"
						+"JB_SFTCD,JB_SWVER,JB_PRGCD,JB_PLNDT,JB_PLNDY,JB_STRDT,JB_ALCTO,JB_ENDDT,JB_REMDS,JB_PRTNO,JB_CPAFL,JB_OJTFL,JB_STSDS,"
						+"JB_TRNFL,JB_STSFL,JB_LUSBY,JB_LUPDT)"
						+" VALUES('" + txtGRPCD.getText().substring(0,2).toUpperCase()+"',"
						+"'"+ cmbSYSCD.getSelectedItem().toString().substring(0,2)+"',"
						+"'"+ cmbJOBTP.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
						+"'"+ cmbJOBOR.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
						+"'"+ cmbJOBCT.getSelectedItem().toString().substring(0,1).toUpperCase() +"',"
						+"'"+ strSRLNO +"',"
						+"'"+ txtREPBY.getText().trim().toUpperCase() +"',"
						+""+ txtREPDP.getText().trim() +",";
					if(txtREPDT.getText().length()>0)
						M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtREPDT.getText().trim()+" "+txtREPTM.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";
										
					M_strSQLQRY += "'" + txtJOBDS.getText().trim() +"',";
					if(txtGRPCD.getText().equals("HARDWARE"))
						M_strSQLQRY += "'"+ cmbDVCNM.getSelectedItem().toString() +"',";
					else 
						M_strSQLQRY += "'',";
					
					M_strSQLQRY += "'"+ txtMDLNO.getText().trim() +"',"
						+"'"+ txtHWSRL.getText().trim() +"',"
						+"'"+ txtSFTCD.getText().trim() +"',"
						+"'"+ txtSWVER.getText().trim() +"',"
						+"'"+ txtPRGCD.getText().trim() +"',";					
					if(txtPLNDT.getText().length()>0)
						M_strSQLQRY += "'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPLNDT.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";					
					if(txtPLNDY.getText().length()>0)
						M_strSQLQRY += txtPLNDY.getText()+",";
					else
						M_strSQLQRY += 0 +",";															
					if(txtSTRDT.getText().length()>0)
						M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSTRDT.getText().trim()+" "+txtSTRTM.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";
					M_strSQLQRY += "'"+ txtALCTO.getText().trim()+"',";
					if(txtENDDT.getText().length()>0)
						M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtENDDT.getText().trim()+" "+txtENDTM.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";
					M_strSQLQRY += "'" + txtREMDS.getText().trim()+"',";
					M_strSQLQRY += "'" + txtPRTNO.getText().trim()+"',";
					M_strSQLQRY += chkCPAFL.isSelected() ? "'Y'," : "'N',";
					M_strSQLQRY += chkOJTFL.isSelected() ? "'Y'," : "'N',";
					M_strSQLQRY += "'" + txtSTSDS.getText().trim()+"',"
					+"'0',"	//TRNFL					
					+"'"+ L_strSTSFL+"',"	//STSFL
					+"'"+ cl_dat.M_strUSRCD_pbst+"',"
					+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
//					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					// Update the Serial Number 
					if(cl_dat.M_flgLCUPD_pbst == true)
					{
						JOptionPane.showMessageDialog(this,"Please Note the Job Number\n"+strSRLNO,"",JOptionPane.ERROR_MESSAGE);
						M_strSQLQRY = "Update CO_CDTRN set CMT_CCSVL ='"+ strSRLNO.substring(3) +"' where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='COXXJOB'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if((txtSTRDT.getText().length()>0)&&(txtENDDT.getText().length()>0))
						L_strSTSFL = "9";
					else
						L_strSTSFL = "0";
					
					M_strSQLQRY ="UPDATE SA_JBMST SET "						
						+" JB_SYSCD = '"+ cmbSYSCD.getSelectedItem().toString().substring(0,2)+"',"
						+" JB_JOBTP = '"+ cmbJOBTP.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
						+" JB_JOBOR = '"+ cmbJOBOR.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
						+" JB_JOBCT = '"+ cmbJOBCT.getSelectedItem().toString().substring(0,1).toUpperCase() +"',"						
						+" JB_REPBY = '"+ txtREPBY.getText().trim() +"',"
						+" JB_REPDP = "+ txtREPDP.getText().trim() +",";
					
					if(txtREPDT.getText().length()>0)
						M_strSQLQRY += "JB_REPDT ='" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtREPDT.getText().trim()+" "+txtREPTM.getText().trim()))+"',";
					M_strSQLQRY +=" JB_JOBDS = '" + txtJOBDS.getText().trim() +"',";
					if(txtGRPCD.getText().equals("HARDWARE"))
						M_strSQLQRY +=" JB_DVCNM = '"+ cmbDVCNM.getSelectedItem().toString() +"',";
					M_strSQLQRY += " JB_MDLNO = '"+ txtMDLNO.getText().trim() +"',"
						+" JB_HWSRL = '"+ txtHWSRL.getText().trim() +"',"
						+" JB_SFTCD = '"+ txtSFTCD.getText().trim() +"',"
						+" JB_SWVER = '"+ txtSWVER.getText().trim() +"',"
						+" JB_PRGCD = '"+ txtPRGCD.getText().trim() +"',";
					if(txtPLNDT.getText().length()>0)
						M_strSQLQRY += "JB_PLNDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPLNDT.getText().trim()))+"',";
					else
						M_strSQLQRY += "JB_PLNDT = "+ null +",";					
					if(txtPLNDY.getText().length()>0)
						M_strSQLQRY +=" JB_PLNDY = "+ txtPLNDY.getText()+",";
					else
						M_strSQLQRY +=" JB_PLNDY = "+ 0+",";															
					if(txtSTRDT.getText().length()>0)
						M_strSQLQRY += "JB_STRDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSTRDT.getText().trim()+" "+txtSTRTM.getText().trim()))+"',";
					else
						M_strSQLQRY += "JB_STRDT = "+ null +",";
					M_strSQLQRY += " JB_ALCTO ='"+ txtALCTO.getText().trim()+"',";
					if(txtENDDT.getText().length()>0)
						M_strSQLQRY += "JB_ENDDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtENDDT.getText().trim()+" "+txtENDTM.getText().trim()))+"',";
					else
						M_strSQLQRY +="JB_ENDDT = "+ null +",";
					M_strSQLQRY += "JB_REMDS = '" + txtREMDS.getText().trim()+"',";
					M_strSQLQRY += "JB_PRTNO = '" + txtPRTNO.getText().trim()+"',";
					M_strSQLQRY += chkCPAFL.isSelected() ? "JB_CPAFL = 'Y'," : "JB_CPAFL = 'N',";
					M_strSQLQRY += chkOJTFL.isSelected() ? "JB_OJTFL = 'Y'," : "JB_OJTFL = 'N',";
					M_strSQLQRY += "JB_STSDS = '" + txtSTSDS.getText().trim()+"',"
					+" JB_TRNFL = '0',"	//TRNFL
					+" JB_STSFL = '"+ L_strSTSFL+ "',"	//STSFL
					+" JB_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',"
					+" JB_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+" WHERE JB_SRLNO = '"+txtSRLNO.getText().trim()+"'";						
//					System.out.println("M_strSQLQRY"+M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");										
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					M_strSQLQRY ="UPDATE SA_JBMST SET JB_STSFL ='X',"
					+" JB_TRNFL = '0',"	//TRNFL					
					+" JB_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',"
					+" JB_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+" WHERE JB_SRLNO = '"+txtSRLNO.getText().trim()+"'";
//					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
				}
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					//clrCOMP();
					clrTEXT();					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						setMSG(" Data Saved Successfully..",'N'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						setMSG(" Data Modified Successfully..",'N'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						setMSG("Data Deleted Successsfully ..",'N');					
				}
				else
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						setMSG("Error in saving details..",'E'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					   setMSG("Error in Data Modification..",'E'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						setMSG("Error in Deleting data..",'E');
				}
			}															
			else if(jtpMANTAB.getSelectedIndex() == 2)//for table modification allowed..
			{			
				if (!vldTDATA())
					return;	
				else 
					setMSG("",'E');
				if(tblJOBDL.isEditing())
					tblJOBDL.getCellEditor().stopCellEditing();
				tblJOBDL.setRowSelectionInterval(0,0);
				tblJOBDL.setColumnSelectionInterval(0,0);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{										
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String L_strTEMP = "";					
					for(int i =0;i<100;i++)
					{
						if(tblJOBDL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
						{							
							if((tblJOBDL.getValueAt(i,TB1_STRDT).toString().trim().length()>0) && (tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim().length()>0))
								L_strSTSFL = "9";
							else
								L_strSTSFL = "0";
							
							M_strSQLQRY = "Update SA_JBMST set"
							//+" JB_JOBTP = '"+ tblJOBDL.getValueAt(i,TB1_JOBTP).toString() +"',"
							//+" JB_JOBOR = '"+ tblJOBDL.getValueAt(i,TB1_JOBOR).toString() +"',"
							+" JB_REPBY = '"+ tblJOBDL.getValueAt(i,TB1_REPBY).toString() +"',"
							+" JB_JOBDS = '"+ tblJOBDL.getValueAt(i,TB1_JOBDS).toString() +"',"					
							+" JB_PRTNO = '"+ tblJOBDL.getValueAt(i,TB1_PRTNO).toString() +"',"			 
							+" JB_DVCNM = '"+ tblJOBDL.getValueAt(i,TB1_DVCNM).toString() +"',"
							+" JB_ALCTO = '"+ tblJOBDL.getValueAt(i,TB1_ALCTO).toString() +"',";						
								L_strTEMP = tblJOBDL.getValueAt(i,TB1_PLNDT).toString();
							if(L_strTEMP.length()>0)
								M_strSQLQRY += "JB_PLNDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTEMP))+"',";
							else
								M_strSQLQRY += "JB_PLNDT = "+ null +",";					
							if(tblJOBDL.getValueAt(i,TB1_PLNDY).toString().length()>0)
								M_strSQLQRY +=" JB_PLNDY = "+ tblJOBDL.getValueAt(i,TB1_PLNDY).toString()+",";
							else
								M_strSQLQRY +=" JB_PLNDY = "+0+",";
							
							if(tblJOBDL.getValueAt(i,TB1_STRDT).toString().trim().length()>0)
								M_strSQLQRY += "JB_STRDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(tblJOBDL.getValueAt(i,TB1_STRDT).toString().trim()))+"',";
							else
								M_strSQLQRY += "JB_STRDT = "+ null +",";														
							if(tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim().length()>0)
								M_strSQLQRY += "JB_ENDDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim()))+"',";
							else
								M_strSQLQRY +="JB_ENDDT = "+ null +",";
							M_strSQLQRY +=" JB_TRNFL = '0',"	//TRNFL
							+" JB_STSFL = '"+ L_strSTSFL +"',"	//STSFL
							+" JB_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',"
							+" JB_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
							+" where JB_SRLNO ='" +tblJOBDL.getValueAt(i,TB1_SRLNO).toString()+"'";
							System.out.println(M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");							 
						}						
					}					
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					for(int i =0;i<intROWCT;i++)
					{
						if(tblJOBDL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
						{					
							M_strSQLQRY ="UPDATE SA_JBMST SET JB_STSFL ='X',"
							+" JB_TRNFL = '0',"	//TRNFL					
							+" JB_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',"
							+" JB_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
							+" WHERE JB_SRLNO = '"+tblJOBDL.getValueAt(i,TB1_SRLNO).toString()+"'";
							System.out.println(M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}			
				if(cl_dat.exeDBCMT("exeSAVE"))
				{			
					tblJOBDL.clrTABLE();
					//clrCOMP();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						setMSG(" Data Modified Successfully..",'N'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						setMSG("Data Deleted Successsfully ..",'N');					
				}
				else
				{					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					   setMSG("Error in Data Modification..",'E'); 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						setMSG("Error in Deleting data..",'E');
				}
			}			
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"exeADDREC");
		}	
	}
	/**
	 * Method to fetch data from the database for Specified Serial Number.
	 */
	private void getDATA()
	{
		try
		{ 
			int L_intROWNO =0,L_intREPDP=0,L_dblPLNDY=0;
			java.sql.Date L_tmpDATE;
			String L_strTEMP="",L_strSTRTM="",L_strENDTM="";
			java.sql.Timestamp L_tmpTIME;
			int L_intSRLNO=0;

			M_strSQLQRY  =" Select * from SA_JBMST "
				+" where isnull(JB_STSFL,' ') <> 'X'"
				+" AND JB_SRLNO = '" + txtSRLNO.getText()+"'";						
			M_rstRSSET =  cl_dat.exeSQLQRY2(M_strSQLQRY);			
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{					
					cmbSYSCD.removeActionListener(this);
					cmbJOBTP.removeActionListener(this);
					cmbJOBCT.removeActionListener(this);
					//String s1 = nvlSTRVL(M_rstRSSET.getString("JB_GRPCD"),"").toString();
					//System.out.println("SS1"+s1);
					//L_strTEMP = String.valueOf(hstGRPCD.get(s1)).toString();
					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_SYSCD"),"").toString();
					cmbSYSCD.setSelectedItem(L_strTEMP +" "+hstCODD1.get(L_strTEMP));
					txtGRPCD.setText(hstGRPCD.get(L_strTEMP +" "+hstCODD1.get(L_strTEMP).toString()).toString());
					
					
					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBTP"),"");
					cmbJOBTP.setSelectedItem(L_strTEMP+" "+hstCODDS.get(L_strTEMP).toString());
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBOR"),"");
					cmbJOBOR.setSelectedItem(L_strTEMP+" "+hstCODDS.get(L_strTEMP).toString());
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBCT"),"");
					cmbJOBCT.setSelectedItem(L_strTEMP+" "+hstCODDS.get(L_strTEMP).toString());
//					cmbGRPCD.addActionListener(this);
					cmbSYSCD.addActionListener(this);
					cmbJOBTP.addActionListener(this);
					cmbJOBCT.addActionListener(this);
					
					txtREPBY.setText(nvlSTRVL(M_rstRSSET.getString("JB_REPBY"),""));
					txtREPDP.setText(nvlSTRVL(M_rstRSSET.getString("JB_REPDP"),""));
					L_tmpTIME = M_rstRSSET.getTimestamp("JB_REPDT");
		 			if (L_tmpTIME != null)
					{
						L_strSTRTM = M_fmtLCDTM.format(L_tmpTIME);
						txtREPDT.setText(L_strSTRTM.substring(0,10));
						if(L_strSTRTM.toString().length()>=11)
							txtREPTM.setText(L_strSTRTM.substring(11));
					}
										
					txtJOBDS.setText(nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),""));
					cmbDVCNM.setSelectedItem(nvlSTRVL(M_rstRSSET.getString("JB_DVCNM"),""));
					txtMDLNO.setText(nvlSTRVL(M_rstRSSET.getString("JB_MDLNO"),""));
					txtHWSRL.setText(nvlSTRVL(M_rstRSSET.getString("JB_HWSRL"),""));
					txtSFTCD.setText(nvlSTRVL(M_rstRSSET.getString("JB_SFTCD"),""));
					txtSWVER.setText(nvlSTRVL(M_rstRSSET.getString("JB_SWVER"),""));
					txtPRGCD.setText(nvlSTRVL(M_rstRSSET.getString("JB_PRGCD"),""));
					
					L_tmpDATE = M_rstRSSET.getDate("JB_PLNDT");					
					if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
					else
						L_strTEMP="";
					txtPLNDT.setText(L_strTEMP);
					txtPLNDY.setText(nvlSTRVL(M_rstRSSET.getString("JB_PLNDY"),""));
					
					L_tmpTIME = M_rstRSSET.getTimestamp("JB_STRDT");
		 			if (L_tmpTIME != null)
					{
						L_strSTRTM = M_fmtLCDTM.format(L_tmpTIME);
						txtSTRDT.setText(L_strSTRTM.substring(0,10));
						if(L_strSTRTM.toString().length()>=11)
							txtSTRTM.setText(L_strSTRTM.substring(11));
					}
					txtALCTO.setText(nvlSTRVL(M_rstRSSET.getString("JB_ALCTO"),""));
					L_tmpTIME = M_rstRSSET.getTimestamp("JB_ENDDT");
		 			if (L_tmpTIME != null)
					{
						L_strENDTM = M_fmtLCDTM.format(L_tmpTIME);
						txtENDDT.setText(L_strENDTM.substring(0,10));
						if(L_strENDTM.toString().length()>=11)
							txtENDTM.setText(L_strENDTM.substring(11));
					}					
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("JB_REMDS"),""));
					txtPRTNO.setText(nvlSTRVL(M_rstRSSET.getString("JB_PRTNO"),""));
					if(nvlSTRVL(M_rstRSSET.getString("JB_CPAFL"),"").equals("Y"))
							chkCPAFL.setSelected(true);
					if(nvlSTRVL(M_rstRSSET.getString("JB_OJTFL"),"").equals("Y"))
							chkOJTFL.setSelected(true);
					txtSTSDS.setText(nvlSTRVL(M_rstRSSET.getString("JB_STSDS"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getdata");
		}
	}
	/**
	 * Method to validate the data before execuation of the SQL Queries.
	 */
	boolean vldDATA()
	{
		try
		{		
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(txtSRLNO.getText().length() == 0)
				{
					setMSG("Serial Number Cannot be Blank..",'E');
					return false;				
				}
			}
			if(txtSTRDT.getText().trim().length() != 0)
			{
				if(txtSTRTM.getText().trim().length() == 0)
				{
					setMSG("Start Time can not be blank..",'E');
					txtSTRTM.requestFocus();
					return false;
				}
				if(M_fmtLCDTM.parse(txtSTRDT.getText().trim()+" "+txtSTRTM.getText().trim()).compareTo(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))>0)
				{
					setMSG("Start Date Time can not be greater than current Date Time..",'E');
					txtSTRDT.requestFocus();
					return false;
				}
			}			
			if(txtENDDT.getText().trim().length() != 0)
			{
				if(txtENDTM.getText().trim().length() == 0)
				{
					setMSG("End Time can not be blank..",'E');
					txtSTRTM.requestFocus();
					return false;
				}
				if(M_fmtLCDTM.parse(txtENDDT.getText().trim()+" "+txtENDTM.getText().trim()).compareTo(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))>0)
				{
					setMSG("Start Date Time can not be greater than current Date Time..",'E');
					txtENDDT.requestFocus();
					return false;
				}
			}
			if((txtSTRDT.getText().trim().length() != 0) && (txtENDDT.getText().trim().length() != 0))
			{
				if(M_fmtLCDTM.parse(txtSTRDT.getText().trim()+" "+txtSTRTM.getText().trim()).compareTo(M_fmtLCDTM.parse(txtENDDT.getText().trim()+" "+txtENDTM.getText().trim()))>0)
				{
					setMSG("End Date Time must be greater than Start Date Time..",'E');
					txtENDDT.requestFocus();
					return false;
				}
			}
			if(txtPRTNO.getText().trim().length() == 0)
			{
				setMSG("Please Enter Priority No...",'E');
				txtPRTNO.requestFocus();				
				return false;				
			}	
			else if(txtPLNDY.getText().length() == 0)
			{
				setMSG("Planned Days Cannot be Blank..",'E');
				txtPLNDY.requestFocus();
				return false;				
			}
			else if(txtJOBDS.getText().length() == 0)
			{
				setMSG("Job Description Cannot be Blank..",'E');
				return false;				
			}
			else if(txtPLNDY.getText().length() != 0)
			{
				double L_dblPLNDY = Double.valueOf(txtPLNDY.getText()).doubleValue();
				if((L_dblPLNDY >= 3) && (cmbJOBCT.getSelectedIndex() != 0))
				{
					setMSG("Please Select Appropriate Job Category according to Planned Days..",'E');
					return false;
				}
				else if((L_dblPLNDY < 3)&&(L_dblPLNDY > 1) && (cmbJOBCT.getSelectedIndex() != 1))
				{
					setMSG("Please Select Appropriate Job Category according to Planned Days..",'E');
					return false;
				}
				else if((L_dblPLNDY < 1) && (cmbJOBCT.getSelectedIndex() != 2))
				{
					setMSG("Please Select Appropriate Job Category according to Planned Days..",'E');
					return false;
				}
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vlddata");
			return false;
		}
	}
	boolean vldTDATA()
	{
		try
		{
			if(tblJOBDL.isEditing())
				tblJOBDL.getCellEditor().stopCellEditing();
			tblJOBDL.setRowSelectionInterval(0,0);
			tblJOBDL.setColumnSelectionInterval(0,0);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				setMSG("Addition of new Record is not allowed here..",'E');
				return false;
			}			
			for(int i =0;i<intROWCT;i++)
			{				
				if(tblJOBDL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
				{
					String str = tblJOBDL.getValueAt(i,TB1_SRLNO).toString();					
					if(str.length() == 0)							
					{
						setMSG("Serial No cannot be blank..",'E');
						return false;
					}
					if((tblJOBDL.getValueAt(i,TB1_STRDT).toString().trim().length()>0) && (tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim().length()>0))
					{
						if(M_fmtLCDTM.parse(tblJOBDL.getValueAt(i,TB1_STRDT).toString().trim()).compareTo(M_fmtLCDTM.parse(tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim()))>0)
						{
							setMSG("End Date Time must be greater than Start Date Time..",'E');
							return false;
						}
					}
				/*	if
					{
						if(tblJOBDL.getValueAt(i,TB1_STRTM).toString().trim().length()== 0)
						{
							setMSG("Start Time cannot be blank..",'E');
							return false;
						}
					}*/
					/*(tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim().length()>0)
					{
						if(tblJOBDL.getValueAt(i,TB1_ENDTM).toString().trim().length()== 0)
						{
							setMSG("End Time cannot be blank..",'E');
							return false;
						}
					}*/
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTDATA");
		}
		return true;		
	}
}
//		+" JB_MDLNO = '"+ tblJOBDL.getValueAt(i,TB1_MDLNO).toString() +"',"
					//		+" JB_HWSRL = '"+ tblJOBDL.getValueAt(i,TB1_HWSRL).toString() +"',"
							//+" JB_SFTCD = '"+ tblJOBDL.getValueAt(i,TB1_SFTCD).toString() +"',"
							//+" JB_SWVER = '"+ tblJOBDL.getValueAt(i,TB1_SWVER).toString() +"',"
						//	+" JB_PRGCD = '"+ tblJOBDL.getValueAt(i,TB1_PRGCD).toString() +"',"
/*else if(jtpMANTAB.getSelectedIndex() == 1)
					{
						L_intROWNO =0;
						//tblJOBDL.setValueAt(M_rstRSSET.getString("JB_SRLNO"),L_intROWNO,TB1_SRLNO);
						//tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_REMDS"),""),L_intROWNO,TB1_REMDS);
						//tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_ALCTO"),""),L_intROWNO,TB1_ALCTO);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_REPBY"),""),L_intROWNO,TB1_REPBY);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_JOBTP"),""),L_intROWNO,TB1_JOBTP);		
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_JOBOR"),""),L_intROWNO,TB1_JOBOR);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),""),L_intROWNO,TB1_JOBDS);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_MACNM"),""),L_intROWNO,TB1_MACNM);
						tblJOBDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_PRNDT"),""),L_intROWNO,TB1_PRNDT);
						L_intREPDP=M_rstRSSET.getInt("JB_REPDP");
						tblJOBDL.setValueAt(String.valueOf(L_intREPDP),L_intROWNO,TB1_REPDP);
						tblJOBDL.setValueAt(M_rstRSSET.getString("JB_SYSCD"),L_intROWNO,TB1_SYSCD);
						L_dblPLNDY=M_rstRSSET.getInt("JB_PLNDY");
						tblJOBDL.setValueAt(String.valueOf(L_dblPLNDY),L_intROWNO,TB1_PLNDY);
						L_tmpDATE = M_rstRSSET.getDate("JB_REPDT");
						L_strTEMP="";
						if (L_tmpDATE != null)
							L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						tblJOBDL.setValueAt(L_strTEMP,L_intROWNO,TB1_REPDT);
						L_tmpDATE = M_rstRSSET.getDate("JB_PLNDT");
						L_strTEMP="";
						if (L_tmpDATE != null)
							L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						tblJOBDL.setValueAt(L_strTEMP,L_intROWNO,TB1_PLNDT);
						L_tmpTIME = M_rstRSSET.getTimestamp("JB_STRDT");
		 				if (L_tmpTIME != null)
						{
							L_strSTRTM = M_fmtLCDTM.format(L_tmpTIME);
							tblJOBDL.setValueAt(L_strSTRTM,L_intROWNO,TB1_STRDT);
						}
						L_tmpTIME = M_rstRSSET.getTimestamp("JB_ENDDT");
		 				if (L_tmpTIME != null)
						{
							L_strENDTM = M_fmtLCDTM.format(L_tmpTIME);
							tblJOBDL.setValueAt(L_strENDTM,L_intROWNO,TB1_ENDDT);
						}
						L_intROWNO ++;
					}
					intRECCT++;
			}
			M_rstRSSET.close();
			}
			else
				setMSG("Data not found for Gate Out..",'E');*/

/*else if(M_objSOURC == btnUPDAT)
			{
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{		
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String L_strTEMP = "";
					if(tblJOBDL.isEditing())
						tblJOBDL.getCellEditor().stopCellEditing();
					tblJOBDL.setRowSelectionInterval(0,0);
					tblJOBDL.setColumnSelectionInterval(0,0);
					for(int i =0;i<intROWCT;i++)
					{
						if(tblJOBDL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
						{
							if(tblJOBDL.getValueAt(i,TB1_SRLNO).toString().length()==0)							
								continue;							
							M_strSQLQRY = "Update SA_JBMST set"
							+" JB_JOBTP = '"+ tblJOBDL.getValueAt(i,TB1_JOBTP).toString() +"',"
							+" JB_JOBOR = '"+ tblJOBDL.getValueAt(i,TB1_JOBOR).toString() +"',"
							+" JB_REPBY = '"+ tblJOBDL.getValueAt(i,TB1_REPBY).toString() +"',"
							+" JB_JOBDS = '"+ tblJOBDL.getValueAt(i,TB1_JOBDS).toString() +"',"					
							+" JB_DVCNM = '"+ tblJOBDL.getValueAt(i,TB1_DVCNM).toString() +"',"
							+" JB_ALCTO = '"+ tblJOBDL.getValueAt(i,TB1_ALCTO).toString() +"',";						
								L_strTEMP = tblJOBDL.getValueAt(i,TB1_PLNDT).toString();
							if(L_strTEMP.length()>0)
								M_strSQLQRY += "JB_PLNDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTEMP))+"',";
							else
								M_strSQLQRY += "JB_PLNDT = "+ null +",";					
							if(txtPLNDY.getText().length()>0)
								M_strSQLQRY +=" JB_PLNDY = "+ tblJOBDL.getValueAt(i,TB1_PLNDY).toString()+",";
							else
								M_strSQLQRY +=" JB_PLNDY = "+0+",";
							
							if((tblJOBDL.getValueAt(i,TB1_STRDT).toString().trim().length()>0) && (tblJOBDL.getValueAt(i,TB1_STRTM).toString().trim().length()>0))
								M_strSQLQRY += "JB_STRDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(tblJOBDL.getValueAt(i,TB1_STRDT).toString().trim()+" "+tblJOBDL.getValueAt(i,TB1_STRTM).toString().trim()))+"',";
							else
								M_strSQLQRY += "JB_STRDT = "+ null +",";														
							if((tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim().length()>0) && (tblJOBDL.getValueAt(i,TB1_ENDTM).toString().trim().length()>0))
								M_strSQLQRY += "JB_ENDDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(tblJOBDL.getValueAt(i,TB1_ENDDT).toString().trim()+" "+tblJOBDL.getValueAt(i,TB1_ENDTM).toString().trim()))+"',";
							else
								M_strSQLQRY +="JB_ENDDT = "+ null +",";
							M_strSQLQRY +=" JB_TRNFL = '0',"	//TRNFL
							+" JB_STSFL = 'U',"	//STSFL
							+" JB_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',"
							+" JB_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
							+" where JB_SRLNO ='" +tblJOBDL.getValueAt(i,TB1_SRLNO).toString()+"'";
							System.out.println(M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.exeDBCMT("exeSAVE"))
							{					
								setMSG(" Data Modified Successfully..",'N');
								clrCOMP();
							}
							else							
								setMSG("Error in Modified Data details..",'E'); 
						}						
					}					
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else				
					setMSG("Only Modification is allowed here..",'E');				
			}*/
//tblJOBDL.setCellEditor(TB1_STRDT, txtSTRDT1 = new JTextField());	txtSTRDT1.addKeyListener(this);			
		//	tblJOBDL.setCellEditor(TB1_ENDDT, txtENDDT1 = new JTextField());	txtENDDT1.addKeyListener(this);			
			//tblJOBDL.setCellEditor(TB1_PLNDT, txtPRNDT1 = new JTextField());	txtPRNDT1.addKeyListener(this);
/*M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXGRP'"
						+" AND isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CHP01 ='"+ cmbGRPCD.getSelectedItem().toString()+"'";						
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							while(M_rstRSSET.next())
								cmbSYSCD.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
							M_rstRSSET.close();
						}*/