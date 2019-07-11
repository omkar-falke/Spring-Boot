/*
System Name   : Human Resource Management System
Program Name  : Suitable Lots Query / Report
Program Desc. : Gives Details of the lots for a particular party as per user requirement.
Author        : Ms. Aditi M. Kulkarni
Date          : 22 March 2007
Version       : FIMS 1.0

List of tables used :
Table Name                  Primary key                               Operation done
                                                             Insert   Update   Query   Delete	
-------------------------------------------------------------------------------------------------------------------
HR_GRMST                     GR_GRDCD                          #         #       #
HR_EMPST                     EP_EMPNO                          #         #       #
-------------------------------------------------------------------------------------------------------------------
*/

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;import java.awt.Color;
import java.sql.ResultSet;import javax.swing.JPanel;import javax.swing.JTabbedPane;
import java.sql.CallableStatement;import javax.swing.JComboBox;
import javax.swing.JOptionPane;import java.awt.event.MouseEvent;

class hr_tehis extends cl_pbase
{
    JOptionPane L_OPTNPN;
	private String strPREVDT;            /** Variable for previous date */
	private String strFILNM;             /** String for generated Report File Name */
    private int intRECCT = 0;            /** Integer for counting the records fetched from DB */
    int intTBINDX=0;
	JTextField txtEMPNO;                 /** JTextField to display Employee No. */
    JTextField txtSHRNM;                 /** JTextField to display Initials */
    JTextField txtFSTNM;                 /** JTextField to display First Name */
    JTextField txtMDLNM;                 /** JTextField to display Middle Name */
    JTextField txtLSTNM;                 /** JTextField to display Last Name*/
    JTextField txtJONDT;                 /** JTextField to display Joining date */
    JTextField txtCRFDT;                 /** JTextField to display Confirmation date */
    private TBLINPVF objTBLVRF; 
	private INPVF oINPVF;
    private  JPanel	pnlHSTRY;            /** Panel for History details */
	private  JPanel	pnlGRADE;            /** Panel for Grade details */
	private  JPanel pnlTOP;
	JComboBox cmbEDCAT,cmbEDLVL,cmbDESGN,cmbMMGRD,cmbDPTNM;
    private  JTabbedPane tbpMAIN;
    private  cl_JTable tblHSTRY;         /** Panel for Employee History Details (TB1) */
    private  cl_JTable tblGRADE;         /** Panel for Grade Details (TB2) */
    int TB1_CHKFL = 0; 
    int TB1_GRDCD = 1;     String strGRDCD;               JTextField txtGRDCD;
    int TB1_STRDT = 2;     String strSTRDT;               JTextField txtSTRDT;
    int TB1_ENDDT = 3;     String strENDDT;               JTextField txtENDDT;
    int TB1_SPALW = 4;     String strSPALW,strSPALW1;     JTextField txtSPALW;
    int TB1_LNSUB = 5;     String strLNSUB,strLNSUB1;     JTextField txtLNSUB;
    int TB1_CONVY = 6;     String strCONVY,strCONVY1;     JTextField txtCONVY;
    int TB1_CHEDN = 7;     String strCHEDN,strCHEDN1;     JTextField txtCHEDN;
    int TB1_MDALW = 8;     String strMDALW,strMDALW1;     JTextField txtMDALW;
    int TB1_LTALW = 9;     String strLTALW,strLTALW1;     JTextField txtLTALW;
    int TB1_PFALW = 10;    String strPFALW,strPFALW1;     JTextField txtPFALW;  
    int TB1_SAALW = 11;    String strSAALW,strSAALW1;     JTextField txtSAALW;
    int TB1_HRALW = 12;    String strHRALW,strHRALW1;     JTextField txtHRALW;
    int TB1_DNALW = 13;    String strDNALW,strDNALW1;     JTextField txtDNALW;
    int TB1_WKALW = 14;    String strWKALW,strWKALW1;     JTextField txtWKALW;
    int TB1_WSALW = 15;    String strWSALW,strWSALW1;     JTextField txtWSALW;
    int TB1_EMPCT = 16;    String strEMPCT;               JTextField txtEMPCT;
    int intROWCNT=0;
	int intROWCNT1=0;
	
	int TB2_CHKFL = 0;									
    int TB2_FMDAT = 1;	//JTextField txtFMDAT;
	int TB2_TODAT = 2;	//JTextField txtTODAT;
	int TB2_CMPRF = 3;	JTextField txtCMPRF;
	int TB2_DESGN = 4;	JTextField txtDESGN;								
    int TB2_MMGRD = 5;	JTextField txtMMGRD;
	int TB2_DPTCD = 6;	JTextField txtDPTCD;
	int TB2_DPTNM = 7;	JTextField txtDPTNM;
	int TB2_EPLOC = 8;	JTextField txtEPLOC;
	int TB2_TRFDT = 9;	//JTextField txtTRFDT;
    int TB2_BASAL = 10;	JTextField txtBASAL;
    int TB2_DNALW = 11;	JTextField txtDNALW1;
	int TB2_PPSAL = 12;	JTextField txtPPSAL;
    int TB2_TELAL = 13;	JTextField txtTELAL;
	int TB2_VHMAL = 14;	JTextField txtVHMAL;
    int TB2_GRSAL = 15;	JTextField txtGRSAL;
	int TB2_CTCVL = 16;	JTextField txtCTCVL;
	
	    
	/**String Arrays for components of respective Combos	 */
    private  final  String[] staEDCAT_fn=new String[] {"Select","Technical","Non-Technical","Management","Others"},
	staEDLVL_fn=new String[] {"Select","Under-Graduate","Graduate","Post Graduate","Diploma","PG Diploma","Doctorate"},
	staDPTDS_fn=new String[] {"Select"},staDESGN_fn=new String[] {"Select"},staGRADE_fn=new String[]{"Select"};
	private boolean flgHSTRY,flgHLPOK=false;
	private  Hashtable hstEPLOC;
	
	/* Constructor */	
	hr_tehis()
	{
	    super(2);
		try
		{
		    String L_strDATA ="";	
			pnlTOP = new JPanel(null); 
		    pnlHSTRY = new JPanel(null);
		    pnlGRADE = new JPanel(null);
		    
		    setMatrix(20,8);
		    L_OPTNPN = new JOptionPane();
			tbpMAIN = new JTabbedPane();
            
            //***** ADDING GRADE DETAILS*****//
			add(new JLabel("Grade History"),1,1,1,1,pnlGRADE,'L');
			tbpMAIN.addTab("GRADE",pnlGRADE);
            add(tbpMAIN,1,1,15,8,this,'L');
            
            //CREATING TABLE FOR GRADE HISTORY
            String[] L_strTBLHD1 = {"FL","Grade","From Date","To Date","Spcl Allw","Lunch Sbdy","Conveyance","Chld Edn Allw","Medical Allw","LTA","PF","Super Ann","HRA","DAA","Work Allw","Washing Allw","Emp Cat"};
			int[] L_intCOLSZ1 = {20,40,70,70,80,80,80,80,80,80,80,80,80,80,80,80,55};
			tblGRADE = crtTBLPNL1(pnlGRADE,L_strTBLHD1,600,2,1,10,8,L_intCOLSZ1,new int[]{0});
			
			objTBLVRF = new TBLINPVF();
			oINPVF=new INPVF();
			tblGRADE.addKeyListener(this);
			
			
			tblGRADE.setCellEditor(TB1_GRDCD,txtGRDCD=new TxtLimit(5));
			tblGRADE.setCellEditor(TB1_STRDT,txtSTRDT=new TxtDate());
			tblGRADE.setCellEditor(TB1_ENDDT,txtENDDT=new TxtDate());
			tblGRADE.setCellEditor(TB1_SPALW,txtSPALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_LNSUB,txtLNSUB=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_CONVY,txtCONVY=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_CHEDN,txtCHEDN=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_MDALW,txtMDALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_LTALW,txtLTALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_PFALW,txtPFALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_SAALW,txtSAALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_HRALW,txtHRALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_DNALW,txtDNALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_WKALW,txtWKALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_WSALW,txtWSALW=new TxtNumLimit(8.2));
			tblGRADE.setCellEditor(TB1_EMPCT,txtEMPCT=new TxtLimit(1));
			
			
			String[] L_strTBLHD2 = {"FL","From Date","To Date","Company Name","Designation","Grade","Dept Cd","Dept Name","Location","Transfer Dt","Basic","DA","Prsnl Pay","Tel Allow","Veh Allow","Gross","CTC"};
			int[] L_intCOLSZ2 =    {20,70,70,50,50,50,50,100,50,70,50,70,70,70,70,70,70};
			tblHSTRY = crtTBLPNL1(pnlHSTRY,L_strTBLHD2,20,7,1,6,8,L_intCOLSZ2,new int[]{0});
			
			tblHSTRY.addKeyListener(this);
			
			//tblHSTRY.setCellEditor(TB2_FMDAT,txtFMDAT=new TxtDate());
			//tblHSTRY.setCellEditor(TB2_TODAT,txtTODAT=new TxtDate());
			tblHSTRY.setCellEditor(TB2_CMPRF,txtCMPRF=new TxtLimit(40));
			tblHSTRY.setCellEditor(TB2_DESGN,txtDESGN=new TxtLimit(10));
			tblHSTRY.setCellEditor(TB2_MMGRD,txtMMGRD=new TxtLimit(10));
			tblHSTRY.setCellEditor(TB2_DPTCD,txtDPTCD=new TxtLimit(3));
			tblHSTRY.setCellEditor(TB2_DPTNM,txtDPTNM=new TxtLimit(40));				                
			tblHSTRY.setCellEditor(TB2_EPLOC,txtEPLOC=new TxtLimit(15));
			//tblHSTRY.setCellEditor(TB2_TRFDT,txtTRFDT=new TxtDate());
			tblHSTRY.setCellEditor(TB2_BASAL,txtBASAL=new TxtNumLimit(5));
			tblHSTRY.setCellEditor(TB2_DNALW,txtDNALW=new TxtNumLimit(5));
			tblHSTRY.setCellEditor(TB2_PPSAL,txtPPSAL=new TxtNumLimit(5));
			tblHSTRY.setCellEditor(TB2_TELAL,txtTELAL=new TxtNumLimit(8.2));
			tblHSTRY.setCellEditor(TB2_VHMAL,txtVHMAL=new TxtNumLimit(8.2));
			tblHSTRY.setCellEditor(TB2_GRSAL,txtGRSAL=new TxtLimit(7));
			tblHSTRY.setCellEditor(TB2_CTCVL,txtCTCVL=new TxtNumLimit(8.2));
			
			//*****ADDING HISTORY DETAILS*****//
			add(new JLabel("Employee History"),1,1,1,1,pnlHSTRY,'L');
			tbpMAIN.addTab("HISTORY",pnlHSTRY);
			add(tbpMAIN,1,1,15,8,this,'L');	    
			add(new JLabel("Emp. No."),1,3,1,1,pnlHSTRY,'L');
			add(new JLabel("Initials"),1,4,1,1,pnlHSTRY,'L');
			add(new JLabel("First Name"),1,5,1,1.5,pnlHSTRY,'L');
			add(new JLabel("Middle Name"),1,6,1,1.5,pnlHSTRY,'L');
			add(new JLabel("Last Name"),1,7,1,1.5,pnlHSTRY,'L');
			add(new JLabel("Emp. Name"),2,2,1,1.5,pnlHSTRY,'L');
			add(txtEMPNO = new TxtLimit(5),2,3,1,1,pnlHSTRY,'L');
			add(txtSHRNM = new TxtLimit(3),2,4,1,1,pnlHSTRY,'L');
			add(txtFSTNM = new TxtLimit(10),2,5,1,1,pnlHSTRY,'L');
			add(txtMDLNM = new TxtLimit(10),2,6,1,1,pnlHSTRY,'L');
			add(txtLSTNM = new TxtLimit(10),2,7,1,1,pnlHSTRY,'L');
			add(new JLabel("Joining Date:"),3,2,1,1.5,pnlHSTRY,'L');
			add(txtJONDT = new TxtLimit(10),3,3,1,1,pnlHSTRY,'L');
            add(new JLabel("Qualification:"),3,5,1,1.5,pnlHSTRY,'L');
            add(cmbEDCAT = new JComboBox(staEDCAT_fn),3,6,1,1,pnlHSTRY,'L');
            add(cmbEDLVL = new JComboBox(staEDLVL_fn),3,7,1,1,pnlHSTRY,'L');							    
		    add(new JLabel("Confirmation Date:"),4,2,1,1.5,pnlHSTRY,'L');
			add(txtCRFDT = new TxtLimit(10),4,3,1,1,pnlHSTRY,'L');
			add(new JLabel("Desg./Grade:"),4,5,1,1.5,pnlHSTRY,'L');
            add(cmbDESGN = new JComboBox(),4,6,1,1,pnlHSTRY,'L');
            cmbDESGN.addItem("Select");
            add(cmbMMGRD = new JComboBox(),4,7,1,1,pnlHSTRY,'L');
            cmbMMGRD.addItem("Select");	
			add(new JLabel("Department:"),5,2,1,1.5,pnlHSTRY,'L');
			add(cmbDPTNM = new JComboBox(),5,3,1,1,pnlHSTRY,'L');
			cmbDPTNM.addItem("Select");
			hstEPLOC = new Hashtable();
			
			//CREATING TABLE FOR EMPLOYEE HISTORY
			
            tbpMAIN.addKeyListener(this);
			txtGRDCD.addKeyListener(this);
			txtEMPCT.addKeyListener(this);
			txtGRDCD.addFocusListener(this);
            txtEMPNO.setInputVerifier(oINPVF);
			tblGRADE.setInputVerifier(objTBLVRF);
			tblHSTRY.setInputVerifier(objTBLVRF);
			tbpMAIN.addMouseListener(this);
			txtDPTCD.addKeyListener(this);
			txtCMPRF.addKeyListener(this);
			txtDESGN.addKeyListener(this);
			txtMMGRD.addKeyListener(this);
			txtVHMAL.addKeyListener(this);
			txtEPLOC.addKeyListener(this);
			//txtFMDAT.addFocusListener(this);
			//txtTODAT.addFocusListener(this);
			txtCMPRF.addFocusListener(this);
			txtDPTNM.addFocusListener(this);
			
			M_strSQLQRY="SELECT  CMT_SHRDS,CMT_CODCD from co_cdTRN  WHERE CMT_CGSTP='COXXDSG' or (CMT_CGSTP='COXXDPT' and CMT_CGMTP='SYS') order by CMT_CODCD";
			System.out.println("1 "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			while(M_rstRSSET.next())
			{
				cmbDESGN.addItem(M_rstRSSET.getString(1));
			}
			M_rstRSSET.close();
			M_strSQLQRY="SELECT  GR_GRDCD from hr_GRMST";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			System.out.println("2 "+M_strSQLQRY);
			M_strSQLQRY="SELECT CMT_SHRDS,CMT_CODCD from co_cdTRN  WHERE CMT_CGSTP='COXXDTP' or (CMT_CGSTP='COXXDPT' and CMT_CGMTP='SYS') order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			System.out.println("3 "+M_strSQLQRY);
			while(M_rstRSSET.next())
			{
					cmbDPTNM.addItem(M_rstRSSET.getString(1));
			}
			M_rstRSSET.close();

			M_strSQLQRY="SELECT GR_GRDCD from hr_GRMST";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			System.out.println("4 "+M_strSQLQRY);
			while(M_rstRSSET.next())
				cmbMMGRD.addItem(M_rstRSSET.getString(1));

			/*M_strSQLQRY="SELECT CMT_CODDS,substr(CMT_CODCD,3,2) from CO_CDTRN where CMT_CGMTP='SYS' and ";
			M_strSQLQRY+=" CMT_CGSTP='COXXSBS' and CMT_CODCD like 'HR%' ";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			while(M_rstRSSET.next())
			{
				hstEPLOC.put(M_rstRSSET.getString(1),M_rstRSSET.getString(2));	
			}*/
	   	}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"Constructor");
	    }
	}       // end of constructor
	
	/* super class Method overrided to enhance its functionality, to enable & disable components 
       according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{   
         super.setENBL(L_flgSTAT);
		 txtEMPNO.setEnabled(true);
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(tbpMAIN.getSelectedIndex()==0)
			{	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(tblGRADE.getSelectedRow()<intROWCNT)
					{	
						((JTextField)tblGRADE.cmpEDITR[TB1_GRDCD]).setEditable(false);
						((JTextField)tblGRADE.cmpEDITR[TB1_STRDT]).setEditable(false);
						((JTextField)tblGRADE.cmpEDITR[TB1_ENDDT]).setEditable(false);
					}
					else
					{	
						((JTextField)tblGRADE.cmpEDITR[TB1_GRDCD]).setEditable(true);
						((JTextField)tblGRADE.cmpEDITR[TB1_STRDT]).setEditable(true);
						((JTextField)tblGRADE.cmpEDITR[TB1_ENDDT]).setEditable(true);
					}	
				}
			}
			if(tbpMAIN.getSelectedIndex()==1)
			{	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(tblHSTRY.getSelectedRow()<intROWCNT1)
					{	
						((JTextField)tblHSTRY.cmpEDITR[TB2_FMDAT]).setEditable(false);
						((JTextField)tblHSTRY.cmpEDITR[TB2_TODAT]).setEditable(false);
						((JTextField)tblHSTRY.cmpEDITR[TB2_CMPRF]).setEditable(false);
						((JTextField)tblHSTRY.cmpEDITR[TB2_DPTNM]).setEditable(false);
					}
					else
					{	
						((JTextField)tblHSTRY.cmpEDITR[TB2_FMDAT]).setEditable(true);
						((JTextField)tblHSTRY.cmpEDITR[TB2_TODAT]).setEditable(true);
						((JTextField)tblHSTRY.cmpEDITR[TB2_CMPRF]).setEditable(true);
						((JTextField)tblHSTRY.cmpEDITR[TB2_DPTNM]).setEditable(true);
					}	
				}
				if(M_objSOURC == tblHSTRY.cmpEDITR[TB2_DPTNM])
				{	
					if(tblHSTRY.getValueAt(tblHSTRY.getSelectedRow(),TB2_DPTCD).toString().equals("999"))	
					{	
						((JTextField)tblHSTRY.cmpEDITR[TB2_DPTNM]).setEditable(true);
					}	
					else
						((JTextField)tblHSTRY.cmpEDITR[TB2_DPTNM]).setEditable(false);
				}
				if(L_FE.getSource().equals(((JTextField)tblHSTRY.cmpEDITR[TB2_FMDAT])) && ((JTextField)tblHSTRY.cmpEDITR[TB2_FMDAT]).getText().length()<10)
					((JTextField)tblHSTRY.cmpEDITR[TB2_FMDAT]).setText("01/04/20");
				if(L_FE.getSource().equals(((JTextField)tblHSTRY.cmpEDITR[TB2_TODAT])) && ((JTextField)tblHSTRY.cmpEDITR[TB2_TODAT]).getText().length()<10)
					((JTextField)tblHSTRY.cmpEDITR[TB2_TODAT]).setText("31/03/20");
				if(L_FE.getSource().equals(txtCMPRF) && ((JTextField)tblHSTRY.cmpEDITR[TB2_CMPRF]).getText().length()<10)
					((JTextField)tblHSTRY.cmpEDITR[TB2_CMPRF]).setText(cl_dat.M_strCMPNM_pbst);
			}
		}	
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}
	
	void clrCOMP()
	{
		super.clrCOMP();
		txtEMPNO.setEnabled(true);
	}
	

	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			String L_strNewval1,L_strNewval2;
			try
			{
			    if(getSource()==tblGRADE)
			    {
					
			        if(P_intCOLID == TB1_GRDCD)
    			    {
						if(tblGRADE.getValueAt(P_intROWID,TB1_GRDCD).toString().length()>0 && flgHLPOK==false)
						{	
							M_strSQLQRY = "Select distinct GR_GRDCD,GR_EMPCT from HR_GRMST where ";
							M_strSQLQRY +="GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRDCD = '"+tblGRADE.getValueAt(P_intROWID,P_intCOLID).toString().trim().toUpperCase()+"'";
							System.out.println("5 "+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								tblGRADE.setValueAt(tblGRADE.getValueAt(P_intROWID,TB1_GRDCD).toString().trim().toUpperCase(),P_intROWID,P_intCOLID);
								tblGRADE.setValueAt(M_rstRSSET.getString("GR_EMPCT"),P_intROWID,TB1_EMPCT);
							}	
							else 
							{
								setMSG("Enter Valid Grade Code",'E');
								return false;	
							}
						}
						else
							flgHLPOK=false;
						setMSG("",'E');
    			    }
					if(P_intCOLID == TB1_STRDT)
    			    {
						if(M_fmtLCDAT.parse(tblGRADE.getValueAt(P_intROWID,TB1_STRDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
						{
							setMSG("Date can not be greater than todays date..",'E');
							return false;
						}
						
						L_strNewval1=tblGRADE.getValueAt(P_intROWID,TB1_GRDCD).toString();
						L_strNewval2=tblGRADE.getValueAt(P_intROWID,TB1_STRDT).toString();
						
						for(int i=0;i<tblGRADE.getRowCount();i++)
						if(tblGRADE.getValueAt(i,TB1_GRDCD).toString().length()>0 && i!=P_intROWID)
						{
							if(L_strNewval1.equals(tblGRADE.getValueAt(i,TB1_GRDCD).toString()) && M_fmtLCDAT.parse(L_strNewval2).compareTo(M_fmtLCDAT.parse(tblGRADE.getValueAt(i,TB1_STRDT).toString()))==0) 
							{
								setMSG("Start Date Related To Given Grade Code Already Exist",'E');
								return false;
							}
						}
						setMSG("",'E');
					}	
					if(P_intCOLID==TB1_ENDDT)	
					{
						if(M_fmtLCDAT.parse(tblGRADE.getValueAt(P_intROWID,TB1_ENDDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
						{
							setMSG("Date can not be greater than todays date..",'E');
							return false;
						}
						if(M_fmtLCDAT.parse(tblGRADE.getValueAt(P_intROWID,TB1_STRDT).toString()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText())) > 0)
						{
							setMSG("Date can not be less than From date..",'E');
							return false;
						}
						setMSG("",'E');
					}	
			    }
				if(getSource()==tblHSTRY)
				{	
					if(tblHSTRY.getValueAt(P_intROWID,P_intCOLID).toString().length()==0)
						return true;
						
					if(P_intCOLID == TB2_FMDAT)
    				{
						String L_STRMSG=vldDATE(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString());
						if(L_STRMSG!=null)
						{
							JOptionPane.showMessageDialog(null,L_STRMSG,"Invalid Date ..",JOptionPane.ERROR_MESSAGE);
							return false;
						}
						if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
						{
							setMSG("From date can not be greater than Today's date..",'E');
							return false;
						}
						if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString()).compareTo(M_fmtLCDAT.parse(txtJONDT.getText()))<0)
						{
							setMSG("From date can not be less than Joining date..",'E');
							return false;
						}
						/*if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString()).getYear().equals(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString()).getYear())
						{	
							setMSG("Month must be Apr..",'E');
							return false;
						}
						if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString()).getMonth() != 4)
						{	
							setMSG("Month must be Apr..",'E');
							return false;
						}*/
						
						L_strNewval1=tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString();
						//for(int i=0;i<tblHSTRY.getRowCount();i++)
						for(int i=0;i<P_intROWID;i++)	
						if(tblHSTRY.getValueAt(i,TB2_FMDAT).toString().length()>0 && i!=P_intROWID)
						{
							if(M_fmtLCDAT.parse(L_strNewval1).compareTo(M_fmtLCDAT.parse(tblHSTRY.getValueAt(i,TB2_FMDAT).toString()))<0) 
							{
								setMSG("Enter Valid From Date..",'E');
								return false;
							}
							if(M_fmtLCDAT.parse(L_strNewval1).compareTo(M_fmtLCDAT.parse(tblHSTRY.getValueAt(i,TB2_TODAT).toString()))<0) 
							{
								setMSG("Enter Valid To Date..",'E');
								return false;
							}
						}
						setMSG("",'E');
					}
					if(P_intCOLID == TB2_TODAT)
    				{
						String L_STRMSG=vldDATE(tblHSTRY.getValueAt(P_intROWID,TB2_TODAT).toString());
						if(L_STRMSG!=null)
						{
							JOptionPane.showMessageDialog(null,L_STRMSG,"Invalid Date ..",JOptionPane.ERROR_MESSAGE);
							return false;
						}
						if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_TODAT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
						{
							setMSG("To date can not be greater than Today's date..",'E');
							return false;
						}
						if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString()).compareTo(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_TODAT).toString()))>0)
						{
							setMSG("To date can not be less than From date..",'E');
							return false;
						}
						/*if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_TODAT).toString()).getMonth() != 2)
						{	
							setMSG("Month must be March..",'E');
							return false;
						}*/	
						setMSG("",'E');
					}	
					
					if(P_intCOLID == TB2_DPTCD)
    				{
						if(tblHSTRY.getValueAt(P_intROWID,TB2_DPTCD).toString().length()<3)
						{
							setMSG("Enter Valid Dept Code",'E');
							return false;
						}
						if(tblHSTRY.getValueAt(P_intROWID,TB2_DPTCD).toString().length()==3 && flgHLPOK==false)
						{	
							M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
							M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
							M_strSQLQRY += " and CMT_STSFL <> 'X' AND CMT_CODCD='"+tblHSTRY.getValueAt(P_intROWID,TB2_DPTCD).toString().trim().toUpperCase()+"' order by CMT_CODCD";
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							System.out.println("7 "+M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET !=null)
							{
								tblHSTRY.setValueAt(tblHSTRY.getValueAt(P_intROWID,TB2_DPTCD).toString().trim().toUpperCase(),P_intROWID,P_intCOLID);
								if(tblHSTRY.getValueAt(P_intROWID,TB2_DPTCD).toString().equals("999"))
								{	
									if(tblHSTRY.getValueAt(P_intROWID,TB2_DPTNM).toString().length()==0)
									tblHSTRY.setValueAt("",P_intROWID,TB2_DPTNM);
								}	
								else
									tblHSTRY.setValueAt(M_rstRSSET.getString("CMT_CODDS"),P_intROWID,TB2_DPTNM);
							}	  
							else
							{
								setMSG("Enter Valid Dept Code",'E');
								return false;
							}	
						}
						else
							flgHLPOK=false;
						setMSG("",'E');
					}
					
					if(P_intCOLID == TB2_DESGN)
    				{
						if(tblHSTRY.getValueAt(P_intROWID,TB2_DESGN).toString().length()>0)
						{	
							M_strSQLQRY="SELECT CMT_CODCD from co_cdTRN  WHERE CMT_CGSTP='COXXDSG' and CMT_CGMTP='DSG' and ";
							M_strSQLQRY+=" CMT_CODCD='"+tblHSTRY.getValueAt(P_intROWID,TB2_DESGN).toString().trim().toUpperCase()+"' order by CMT_CODCD";
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
							System.out.println("8 "+M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET !=null)
							{
								tblHSTRY.setValueAt(tblHSTRY.getValueAt(P_intROWID,TB2_DESGN).toString().trim().toUpperCase(),P_intROWID,P_intCOLID);
							}	  
							else
							{
								setMSG("Enter Valid Designation Code",'E');
								return false;
							}	
							setMSG("",'E');
						}
					}	
					if(P_intCOLID == TB2_MMGRD)
    				{
						if(tblHSTRY.getValueAt(P_intROWID,TB2_MMGRD).toString().length()>0)
						{	
							M_strSQLQRY="SELECT GR_GRDCD from hr_GRMST";
							M_strSQLQRY+=" where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRDCD='"+tblHSTRY.getValueAt(P_intROWID,TB2_MMGRD).toString().trim().toUpperCase()+"'";
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
							System.out.println("9 "+M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET !=null)
							{
								tblHSTRY.setValueAt(tblHSTRY.getValueAt(P_intROWID,TB2_MMGRD).toString().trim().toUpperCase(),P_intROWID,P_intCOLID);
							}	  
							else
							{
								setMSG("Enter Valid Grade Code",'E');
								return false;
							}	
							setMSG("",'E');
						}
					}
					if(P_intCOLID == TB2_EPLOC)
    				{
						if(tblHSTRY.getValueAt(P_intROWID,TB2_EPLOC).toString().length()>0)
						{	
							if(tblHSTRY.getValueAt(P_intROWID,TB2_EPLOC).toString().length()<2)
							{
								setMSG("Enter Valid Employee Location",'E');
								return false;
							}
							M_strSQLQRY="SELECT SUBSTRING(CMT_CODCD,3,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and ";
							M_strSQLQRY+=" CMT_CGSTP='COXXSBS' and CMT_CODCD like 'HR%' and SUBSTRING(CMT_CODCD,3,2)='"+tblHSTRY.getValueAt(P_intROWID,TB2_EPLOC)+"'";
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
							System.out.println("10 "+M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET !=null)
							{
								tblHSTRY.setValueAt(tblHSTRY.getValueAt(P_intROWID,TB2_EPLOC),P_intROWID,P_intCOLID);
							}	  
							else
							{
								setMSG("Enter Valid Employee Location",'E');
								return false;
							}
							/*M_strSQLQRY="SELECT substr(CMT_CODCD,3,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and ";
							M_strSQLQRY+=" CMT_CGSTP='COXXSBS' and CMT_CODCD like 'HR%' and CMT_CODDS='"+tblHSTRY.getValueAt(tblHSTRY.getSelectedRow(),TB2_EPLOC)+"'";
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
							if(M_rstRSSET.next() && M_rstRSSET !=null)
							{
								tblHSTRY.setValueAt(txtEPLOC.getText(),P_intROWID,P_intCOLID);
							}	  
							else
							{
								setMSG("Enter Valid Employee Location",'E');
								return false;
							}*/
							
							setMSG("",'E');
						}
					}
					if(P_intCOLID == TB2_TRFDT)
    				{
						String L_STRMSG=vldDATE(tblHSTRY.getValueAt(P_intROWID,TB2_TRFDT).toString());
						if(L_STRMSG!=null)
						{
							JOptionPane.showMessageDialog(null,L_STRMSG,"Invalid Date ..",JOptionPane.ERROR_MESSAGE);
							return false;
						}
						if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_TRFDT).toString()).compareTo(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_TODAT).toString())) > 0)
						{
							setMSG("Transfer Date can not be greater than To date..",'E');
							return false;
						}	
						if(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_TRFDT).toString()).compareTo(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWID,TB2_FMDAT).toString())) < 0)
						{
							setMSG("Transfer Date can not be less than From date..",'E');
							return false;
						}	
						setMSG("",'E');
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"class TBLINPVF");
			}
			return true;
		}
    }
	
	/** Validate data entered by user, Format all text and calculate salary */
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			//System.out.println("inside verify");
			try
			{
				if(input==txtEMPNO)
				{
						M_rstRSSET=null;
						M_strSQLQRY = "select distinct EP_EMPNO from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText()+"' order by EP_EMPNO";
						M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
						System.out.println("11 "+M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							txtEMPNO.setText(txtEMPNO.getText());
							//getDATA();
						}
						else
						{
							setMSG("Enter Valid Employee No",'E');
							txtEMPNO.setEnabled(true);
							return false;
						}	
				setMSG("",'E');
				}	
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"class INPVF");		
			}
			return true;
		}
	}	
	
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			super.mouseReleased(L_ME);
			if(M_objSOURC == tbpMAIN)
			{
				if(tbpMAIN.getSelectedIndex()==0)
				{
					intTBINDX =0; 
				}
				else if(tbpMAIN.getSelectedIndex()==1)
				{
					intTBINDX =1 ;
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}
		
	private  boolean vldHSTRY(int P_intROWNO)
	{
		try
		{
		   if(tblHSTRY.getValueAt(P_intROWNO,TB2_CMPRF).toString().equals(""))
		   {
				setMSG("Please Enter Company Name",'E');
				return false;
		   }
		   if(tblHSTRY.getValueAt(P_intROWNO,TB2_FMDAT).toString().equals(""))
		   {
				setMSG("Please Enter From Date",'E');
				return false;
		   }
		   if(tblHSTRY.getValueAt(P_intROWNO,TB2_TODAT).toString().equals(""))
		   {
				setMSG("Please Enter To Date",'E');
				return false;
		   }
		   if(tblHSTRY.getValueAt(P_intROWNO,TB2_DESGN).toString().equals(""))
		   {
				setMSG("Please Enter Designation",'E');
				return false;
		   }
		 
			if(tblHSTRY.getValueAt(P_intROWNO,TB2_MMGRD).toString().equals(""))
			{
					setMSG("Please Enter Grade",'E');
					return false;
			}
			if(tblHSTRY.getValueAt(P_intROWNO,TB2_DPTCD).toString().equals(""))
			{
					setMSG("Please Enter Department Code",'E');
					return false;
			}
			if(tblHSTRY.getValueAt(P_intROWNO,TB2_DPTNM).toString().equals(""))
			{
					setMSG("Please Enter Department Name",'E');
					return false;
			}
		   if(tblHSTRY.getValueAt(P_intROWNO,TB2_EPLOC).toString().equals(""))
		   {
				setMSG("Please Enter Employee Location",'E');
				return false;
		   }
		   if(tblHSTRY.getValueAt(P_intROWNO,TB2_BASAL).toString().equals(""))
		   {
				setMSG("Please Enter Basic Salary",'E');
				return false;
		   }
		   if(tblHSTRY.getValueAt(P_intROWNO,TB2_GRSAL).toString().equals(""))
		   {
				setMSG("Please Enter Gross Salary",'E');
				return false;
		   }
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldHSTRY");
		}
		return true;
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode()==L_KE.VK_LEFT)
			{
				if(M_objSOURC == tbpMAIN)
				{
					//System.out.println(">>"+tbpMAIN.getSelectedIndex());
					if(tbpMAIN.getSelectedIndex()==0)
					{
						intTBINDX =1; 
					}
					else if(tbpMAIN.getSelectedIndex()==1)
					{
						intTBINDX =0 ;
					}
				}
			}				
			if(L_KE.getKeyCode()==L_KE.VK_RIGHT)
			{
				if(M_objSOURC == tbpMAIN)
				{
					if(tbpMAIN.getSelectedIndex()==0)
					{
						intTBINDX =1; 
					}
					else if(tbpMAIN.getSelectedIndex()==1)
					{
						intTBINDX =0 ;
					}
				}
			}				
		    if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
    			if(tbpMAIN.getSelectedIndex()==0)
				{		
					if(M_objSOURC == tblGRADE.cmpEDITR[TB1_GRDCD])
    				{
        			    M_strHLPFLD = "txtGRDCD";	
        			    M_strSQLQRY = "Select distinct GR_GRDCD,GR_EMPCT from HR_GRMST where GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' order by GR_GRDCD";
        				cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade Category", "Employee Category"},2,"CT",new int[]{107,400});
    				}
				}
				else if(tbpMAIN.getSelectedIndex()==1)
				{
					if(M_objSOURC == txtEMPNO)
					{
						M_strHLPFLD = "txtEMPNO";
						M_strSQLQRY = "select distinct EP_EMPNO,EP_FULNM from HR_EPMST where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' order by EP_EMPNO";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Employee No","Employee Name"},2,"CT",new int[]{107,400});
					}   
					if(M_objSOURC == tblHSTRY.cmpEDITR[TB2_DPTCD])
    				{
        			    M_strHLPFLD = "txtDPTCD";	
        			    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
						M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
						M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Dept Code", "Dept Name"},2,"CT",new int[]{107,400});
    				}
					if(M_objSOURC == tblHSTRY.cmpEDITR[TB2_DESGN])
    				{
						M_strHLPFLD = "txtDESGN";	
						M_strSQLQRY="SELECT  CMT_CODCD,CMT_SHRDS from co_cdTRN  WHERE CMT_CGSTP='COXXDSG' and CMT_CGMTP='DSG' order by CMT_CODCD";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Dsgn Code", "Designation"},2,"CT",new int[]{107,400});
					}	
					if(M_objSOURC == tblHSTRY.cmpEDITR[TB2_MMGRD])
    				{
						M_strHLPFLD = "txtMMGRD";	
						M_strSQLQRY="Select distinct CMT_CODCD from CO_CDTRN where CMT_CGMTP = 'S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXGRD' order by CMT_CODCD";
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade Code"},1,"CT",new int[]{400});
					}	
					if(M_objSOURC == tblHSTRY.cmpEDITR[TB2_EPLOC])
    				{
						M_strHLPFLD = "txtEPLOC";	
						M_strSQLQRY="SELECT SUBSTRING(CMT_CODCD,3,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and ";
						M_strSQLQRY+=" CMT_CGSTP='COXXSBS' and CMT_CODCD like 'HR%'";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Locn Code", "Location"},2,"CT",new int[]{107,400});

						/*M_strSQLQRY="SELECT CMT_CODDS,substr(CMT_CODCD,3,2) from CO_CDTRN where CMT_CGMTP='SYS' and ";
						M_strSQLQRY+=" CMT_CGSTP='COXXSBS' and CMT_CODCD like 'HR%'";
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Location","Locn Code"},2,"CT",new int[]{107,400});*/
					}
					//System.out.println("hi");
				}	
			}
			else if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
			    
    	    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		//System.out.println("inside action");
		try
		{			
			if(intTBINDX==0)
				tbpMAIN.setSelectedIndex(0);
			else
				tbpMAIN.setSelectedIndex(1);
			if(tbpMAIN.getSelectedIndex()==0)
			{		
				if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
				{
					((JTextField)tblGRADE.cmpEDITR[TB1_EMPCT]).setEditable(false);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))		
					{
						getGRDREC();
						setENBL(false);	
						((JCheckBox)tblGRADE.cmpEDITR[TB1_CHKFL]).setEnabled(true);
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{	
						getGRDREC();
						((JTextField)tblGRADE.cmpEDITR[TB1_GRDCD]).setEditable(false);
						((JTextField)tblGRADE.cmpEDITR[TB1_STRDT]).setEditable(false);
						((JTextField)tblGRADE.cmpEDITR[TB1_ENDDT]).setEditable(false);
					}	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						getGRDREC();
					}	
				}
			}
			if(tbpMAIN.getSelectedIndex()==1)
			{	
				if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
				{
					tblHSTRY.clrTABLE();
					clrCOMP();
					tbpMAIN.setSelectedIndex(1);
					if(tblHSTRY.isEditing())
						tblHSTRY.getCellEditor().stopCellEditing();
					tblHSTRY.setRowSelectionInterval(0,0);
					tblHSTRY.setColumnSelectionInterval(0,0);
				}	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))		
				{
					txtEMPNO.setEnabled(true);
					((JCheckBox)tblHSTRY.cmpEDITR[TB2_CHKFL]).setEnabled(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{	
					((JTextField)tblHSTRY.cmpEDITR[TB2_FMDAT]).setEditable(false);
					((JTextField)tblHSTRY.cmpEDITR[TB2_TODAT]).setEditable(false);
					((JTextField)tblHSTRY.cmpEDITR[TB2_CMPRF]).setEditable(false);
					((JTextField)tblHSTRY.cmpEDITR[TB2_DPTNM]).setEditable(false);
				}	
				if(M_objSOURC == txtEMPNO)
    			{
					getDATA();
    			}
			}
		}
	    catch(Exception L_EA)
	    {
	        setMSG(L_EA,"Action Performed");
	    }
	}
			   
	
	/* method for Help*/	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD == "txtEMPNO")
			{
				txtEMPNO.setText(cl_dat.M_strHLPSTR_pbst);
			}	
			if(M_strHLPFLD == "txtGRDCD")
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtGRDCD.setText(L_STRTKN1.nextToken());
				tblGRADE.setValueAt(L_STRTKN1.nextToken(),tblGRADE.getSelectedRow(),TB1_EMPCT);
				flgHLPOK=true;
			}
			if(M_strHLPFLD == "txtDPTCD")
			{
				String L_tok1;
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_tok1=L_STRTKN1.nextToken();
				txtDPTCD.setText(L_tok1);
				if(L_tok1.equals("999"))
					tblHSTRY.setValueAt("",tblHSTRY.getSelectedRow(),TB2_DPTNM);
				else
					tblHSTRY.setValueAt(L_STRTKN1.nextToken(),tblHSTRY.getSelectedRow(),TB2_DPTNM);
				flgHLPOK=true;
			}
			if(M_strHLPFLD == "txtDESGN")
			{
				txtDESGN.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtMMGRD")
			{
				txtMMGRD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtEPLOC")
			{
				txtEPLOC.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}
	
	/* method to populat the textfields when user enters the EMPNO.*/
	public void getDATA()
    {
        try
        {
			txtEMPNO.setEnabled(false);
            txtSHRNM.setEnabled(false);
            txtFSTNM.setEnabled(false);
            txtMDLNM.setEnabled(false);
            txtLSTNM.setEnabled(false);
            txtJONDT.setEnabled(false);
            txtCRFDT.setEnabled(false);
            cmbEDCAT.setEnabled(false);
            cmbEDLVL.setEnabled(false);
            cmbDESGN.setEnabled(false);
            cmbMMGRD.setEnabled(false);
            cmbDPTNM.setEnabled(false);
			
			int L_intCNT=0;			
            String strEMPNO = txtEMPNO.getText();
			M_strSQLQRY = "Select EP_SHRNM,EP_FSTNM,EP_MDLNM,EP_LSTNM,EP_DESGN,EP_QUALN,EP_JONDT,EP_DPTNM,EP_MMGRD,EP_CRFDT from HR_EPMST";
            M_strSQLQRY += " where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '"+ strEMPNO +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY); 
			System.out.println("12 "+M_strSQLQRY);
            while(M_rstRSSET.next())
    		{
				txtSHRNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_SHRNM").trim(),""));
    		    txtFSTNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_FSTNM").trim(),""));
    		    txtMDLNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_MDLNM").trim(),""));
    		    txtLSTNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_LSTNM").trim(),""));
				if(M_rstRSSET.getDate("EP_JONDT")==null)
					txtJONDT.setText("");
				else	
    				txtJONDT.setText(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_JONDT")),""));
    		    
				if(M_rstRSSET.getDate("EP_CRFDT")==null)
					txtCRFDT.setText("");
				else	
					txtCRFDT.setText(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_CRFDT")),""));
    		    cmbDESGN.setSelectedItem(nvlSTRVL(M_rstRSSET.getString("EP_DESGN"),"Select"));
				cmbMMGRD.setSelectedItem(nvlSTRVL(M_rstRSSET.getString("EP_MMGRD"),"Select"));
				cmbDPTNM.setSelectedItem(nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),"Select"));
				StringTokenizer st; 
    		    st = new StringTokenizer(nvlSTRVL(M_rstRSSET.getString("EP_QUALN"),""),"|");
				if(st.hasMoreElements())
					cmbEDCAT.setSelectedItem(st.nextToken());
				if(st.hasMoreElements())
					cmbEDLVL.setSelectedItem(st.nextToken());
    		}
			if(tblHSTRY.isEditing())
				tblHSTRY.getCellEditor().stopCellEditing();
			tblHSTRY.setRowSelectionInterval(0,0);
			tblHSTRY.setColumnSelectionInterval(0,0);
			
			M_strSQLQRY = "Select EH_STRDT,EH_ENDDT,EH_CMPRF,EH_DESGN,EH_MMGRD,EH_DPTCD,EH_EPLOC,EH_TRFDT,EH_BASAL,";
			M_strSQLQRY +="EH_DNALW,EH_PPSAL,EH_TELAL,EH_VHMAL,EH_GRSAL,EH_CTCVL from HR_EHTRN";
            M_strSQLQRY += " where EH_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EH_EMPNO = '"+strEMPNO+"'";
			System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY); 
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					tblHSTRY.setValueAt(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("EH_STRDT")).toString(),""),L_intCNT,TB2_FMDAT);
					tblHSTRY.setValueAt(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("EH_ENDDT")).toString(),""),L_intCNT,TB2_TODAT);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_CMPRF"),""),L_intCNT,TB2_CMPRF);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_DESGN"),""),L_intCNT,TB2_DESGN);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_MMGRD"),""),L_intCNT,TB2_MMGRD);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_DPTCD"),""),L_intCNT,TB2_DPTCD);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_EPLOC"),""),L_intCNT,TB2_EPLOC);
					if(M_rstRSSET.getDate("EH_TRFDT")==null)
					{
						tblHSTRY.setValueAt("",L_intCNT,TB2_TRFDT);
					}	
					else
						tblHSTRY.setValueAt(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("EH_TRFDT")).toString(),""),L_intCNT,TB2_TRFDT);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_BASAL"),""),L_intCNT,TB2_BASAL);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_DNALW"),""),L_intCNT,TB2_DNALW);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_PPSAL"),""),L_intCNT,TB2_PPSAL);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_TELAL"),""),L_intCNT,TB2_TELAL);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_VHMAL"),""),L_intCNT,TB2_VHMAL);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_GRSAL"),""),L_intCNT,TB2_GRSAL);
					tblHSTRY.setValueAt(nvlSTRVL(M_rstRSSET.getString("EH_CTCVL"),""),L_intCNT,TB2_CTCVL);
					L_intCNT++;
				}	
			}	
			intROWCNT1=L_intCNT;
        }
        catch(Exception L_EX)
        {
            setMSG(L_EX,"getDATA");
        }
    }
    

    
    /** method to validate grade  */
    private boolean vldGRADE(int P_intROWNO)
    {
        try
        {
           if(tblGRADE.getValueAt(P_intROWNO,TB1_GRDCD).toString().equals(""))
		   {
				setMSG("Please Enter Grade Code",'E');
				return false;
		   }
		   if(tblGRADE.getValueAt(P_intROWNO,TB1_STRDT).toString().equals(""))
		   {
				setMSG("Please Enter From Date",'E');
				return false;
		   }
		   if(tblGRADE.getValueAt(P_intROWNO,TB1_ENDDT).toString().equals(""))
		   {
				setMSG("Please Enter To Date",'E');
				return false;
		   }
        }
        catch(Exception L_EX)
		{
			setMSG(L_EX,"vldGRADE");
		}
		return true;
    }
    

    	
	/* method to save data   */
	void exeSAVE()
    {
		int P_intROWNO;
       try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(tbpMAIN.getSelectedIndex()==0)
			{	

					for(P_intROWNO=0;P_intROWNO<tblGRADE.getRowCount();P_intROWNO++)
					{		
						if(tblGRADE.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
						{
							if(!vldGRADE(P_intROWNO))
							{
								return;
							}
						}
					}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
				{
				    for(P_intROWNO=0;P_intROWNO<tblGRADE.getRowCount();P_intROWNO++)
					{
						if(tblGRADE.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
						{
							exeADDREC(P_intROWNO);
						}
					}
				}
				 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				 {
				   for(P_intROWNO=0;P_intROWNO<tblGRADE.getRowCount();P_intROWNO++)
					{
						if(tblGRADE.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
						{
								exeMODREC(P_intROWNO);
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					} 
				   tblGRADE.setEnabled(true);
				 }
				 
				 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
				{
				    for(P_intROWNO=0;P_intROWNO<tblGRADE.getRowCount();P_intROWNO++)
					{
						if(tblGRADE.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
						{
							exeDELREC(P_intROWNO);
						}
					}
				}
			}	 
			else if(tbpMAIN.getSelectedIndex()==1)
			{
				for(P_intROWNO=0;P_intROWNO<tblHSTRY.getRowCount();P_intROWNO++)
				{
					if(tblHSTRY.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
					{
						if(!vldHSTRY(P_intROWNO))
						{
							return;
						}
					}
				}
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
				{
				    for(P_intROWNO=0;P_intROWNO<tblHSTRY.getRowCount();P_intROWNO++)
					{
						if(tblHSTRY.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
						{
							exeADDREC1(P_intROWNO);
						}
					}
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				 {
				   for(P_intROWNO=0;P_intROWNO<tblHSTRY.getRowCount();P_intROWNO++)
					{
						if(tblHSTRY.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
						{
								exeMODREC1(P_intROWNO);
						}
					} 
				 }
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
				{
				    for(P_intROWNO=0;P_intROWNO<tblHSTRY.getRowCount();P_intROWNO++)
					{
						if(tblHSTRY.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
						{
							//exeDELREC1(P_intROWNO);
						}
					}
				}
			}	
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("record updated successfully",'N');
				tblGRADE.clrTABLE();			
				clrCOMP();
			}
			else
			{
			   	JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
			    setMSG("Error in updating data..",'E');
			}
			 
        }
        catch(Exception L_EX)
        {
           setMSG(L_EX,"exeSAVE"); 
        }
    }
    
    /* On Save Button click data is inserted or updated into the respective tables */
	private void exeADDREC(int P_intROWNO)
	{ 
	  try
	  {
		 if(P_intROWNO <intROWCNT)
		 {
			 exeMODREC(P_intROWNO);
		 }	
		 else
		 {	 
			strGRDCD=tblGRADE.getValueAt(P_intROWNO,TB1_GRDCD).toString();
			strSTRDT=tblGRADE.getValueAt(P_intROWNO,TB1_STRDT).toString();
			strENDDT=tblGRADE.getValueAt(P_intROWNO,TB1_ENDDT).toString();
			strSPALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_SPALW).toString(),"0.00");
			strSPALW = setNumberFormat(Double.parseDouble(strSPALW1),2);
			//strLNSUB1=tblGRADE.getValueAt(P_intROWNO,TB1_LNSUB).toString();
			strLNSUB1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_LNSUB).toString(),"0.00");
			strLNSUB = setNumberFormat(Double.parseDouble(strLNSUB1),2);
			strCONVY1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_CONVY).toString(),"0.00");
			strCONVY = setNumberFormat(Double.parseDouble(strCONVY1),2);
			strCHEDN1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_CHEDN).toString(),"0.00");
			strCHEDN = setNumberFormat(Double.parseDouble(strCHEDN1),2);
			strMDALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_MDALW).toString(),"0.00");
			strMDALW = setNumberFormat(Double.parseDouble(strMDALW1),2);
			strLTALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_LTALW).toString(),"0.00");
			strLTALW = setNumberFormat(Double.parseDouble(strLTALW1),2);
			strPFALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_PFALW).toString(),"0.00");
			strPFALW = setNumberFormat(Double.parseDouble(strPFALW1),2);
			strSAALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_SAALW).toString(),"0.00");
			strSAALW = setNumberFormat(Double.parseDouble(strSAALW1),2);
			strHRALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_HRALW).toString(),"0.00");
			strHRALW = setNumberFormat(Double.parseDouble(strHRALW1),2);
			strDNALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_DNALW).toString(),"0.00");
			strDNALW = setNumberFormat(Double.parseDouble(strDNALW1),2);
			strWKALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_WKALW).toString(),"0.00");
			strWKALW = setNumberFormat(Double.parseDouble(strWKALW1),2);
			strWSALW1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_WSALW).toString(),"0.00");
			strWSALW = setNumberFormat(Double.parseDouble(strWSALW1),2);
			strEMPCT=tblGRADE.getValueAt(P_intROWNO,TB1_EMPCT).toString();
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			M_strSQLQRY = "Insert into HR_GRHST(GR_CMPCD,GR_GRDCD,GR_STRDT,GR_ENDDT,GR_SPALW,GR_LNSUB,";
			M_strSQLQRY += "GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,";
			M_strSQLQRY += "GR_DNALW,GR_WKALW,GR_WSALW,GR_EMPCT,GR_LUSBY,GR_LUPDT) ";
			M_strSQLQRY += " values (";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+strGRDCD+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"',";
			M_strSQLQRY += "'"+strSPALW+"',";
			M_strSQLQRY += "'"+strLNSUB+"',";
			M_strSQLQRY += "'"+strCONVY+"',";
			M_strSQLQRY += "'"+strCHEDN+"',";
			M_strSQLQRY += "'"+strMDALW+"',";
			M_strSQLQRY += "'"+strLTALW+"',";
			M_strSQLQRY += "'"+strPFALW+"',";
			M_strSQLQRY += "'"+strSAALW+"',";
			M_strSQLQRY += "'"+strHRALW+"',";
			M_strSQLQRY += "'"+strDNALW+"',";
			M_strSQLQRY += "'"+strWKALW+"',";
			M_strSQLQRY += "'"+strWSALW+"',";
			M_strSQLQRY += "'"+strEMPCT+"',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		 }	
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeADDREC");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeADDREC"); 
      }
	}
	
	private void exeADDREC1(int P_intROWNO)
	{ 
	  try
	  {
		  if(P_intROWNO <intROWCNT1)
		 {
			 exeMODREC1(P_intROWNO);
		 }	
		 else
		 {	 
			M_strSQLQRY = "Insert into HR_EHTRN(EH_CMPCD,EH_EMPNO,EH_STRDT,EH_ENDDT,EH_CMPRF,EH_DESGN,EH_MMGRD,EH_DPTCD,EH_EPLOC,EH_TRFDT,EH_BASAL,";
			M_strSQLQRY +="EH_DNALW,EH_PPSAL,EH_TELAL,EH_VHMAL,EH_GRSAL,EH_CTCVL,EH_HRSBS)";
			M_strSQLQRY += " values (";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+txtEMPNO.getText()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWNO,TB2_FMDAT).toString()))+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWNO,TB2_TODAT).toString()))+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_CMPRF).toString(),"")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_DESGN).toString(),"")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_MMGRD).toString(),"")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_DPTCD).toString(),"")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_EPLOC).toString(),"")+"',";
			if(tblHSTRY.getValueAt(P_intROWNO,TB2_TRFDT).toString().toString().length()>0)
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWNO,TB2_TRFDT).toString()))+"',";
			else
				M_strSQLQRY += "null,";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_BASAL).toString(),"0")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_DNALW).toString(),"0")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_PPSAL).toString(),"0")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_TELAL).toString(),"0.00")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_VHMAL).toString(),"0.00")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_GRSAL).toString(),"0")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_CTCVL).toString(),"0")+"',";
			M_strSQLQRY += "'"+nvlSTRVL(M_strSBSCD,"0")+"')";
			//System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		 }	
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeADDREC");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeADDREC1"); 
      }
	}
	
	
	/* method for inserting data into receipt transaction table i.e FG_RCTRN */	
	
	private void getGRDREC()
	{
		int L_CNT = 0;
		try
	    {
			if(tblGRADE.isEditing())
				tblGRADE.getCellEditor().stopCellEditing();
			tblGRADE.setRowSelectionInterval(0,0);
			tblGRADE.setColumnSelectionInterval(0,0);
			
			tblGRADE.clrTABLE();
			cl_dat.M_flgLCUPD_pbst = true;
	        ResultSet L_rstRSLSET;
	        M_strSQLQRY = "Select GR_GRDCD,GR_STRDT,GR_ENDDT,GR_SPALW,GR_LNSUB,GR_CONVY,";
		    M_strSQLQRY += "GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,";
		    M_strSQLQRY += "GR_WKALW,GR_WSALW,GR_EMPCT from HR_GRHST where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <> 'X'"; 
			M_strSQLQRY += "order by GR_GRDCD,GR_STRDT";
	        System.out.println(M_strSQLQRY);
			L_rstRSLSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(L_rstRSLSET != null)
			{
				//System.out.println("RS NOT NULL");
				while(L_rstRSLSET.next())
				{
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_GRDCD"),"").trim(),L_CNT,TB1_GRDCD);  
				  tblGRADE.setValueAt(M_fmtLCDAT.format(L_rstRSLSET.getDate("GR_STRDT")),L_CNT,TB1_STRDT);
				  tblGRADE.setValueAt(M_fmtLCDAT.format(L_rstRSLSET.getDate("GR_ENDDT")),L_CNT,TB1_ENDDT);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_SPALW"),"").trim(),L_CNT,TB1_SPALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_LNSUB"),"").trim(),L_CNT,TB1_LNSUB);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_CONVY"),"").trim(),L_CNT,TB1_CONVY);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_CHEDN"),"").trim(),L_CNT,TB1_CHEDN);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_MDALW"),"").trim(),L_CNT,TB1_MDALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_LTALW"),"").trim(),L_CNT,TB1_LTALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_PFALW"),"").trim(),L_CNT,TB1_PFALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_SAALW"),"").trim(),L_CNT,TB1_SAALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_HRALW"),"").trim(),L_CNT,TB1_HRALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_DNALW"),"").trim(),L_CNT,TB1_DNALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_WKALW"),"").trim(),L_CNT,TB1_WKALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_WSALW"),"").trim(),L_CNT,TB1_WSALW);
				  tblGRADE.setValueAt(nvlSTRVL(L_rstRSLSET.getString("GR_EMPCT"),"").trim(),L_CNT,TB1_EMPCT);
				  L_CNT++;
				}
			}
			intROWCNT=L_CNT;
	    }
	    catch(Exception L_EX)
	    {
	      setMSG(L_EX,"getGRDREC");		
	    }
	}
	
	private void exeDELREC(int P_intROWNO)
	{ 
	  try
	  {
		 strGRDCD=tblGRADE.getValueAt(P_intROWNO,TB1_GRDCD).toString();
	     strSTRDT=tblGRADE.getValueAt(P_intROWNO,TB1_STRDT).toString();
	     strENDDT=tblGRADE.getValueAt(P_intROWNO,TB1_ENDDT).toString();
	     strSPALW1=tblGRADE.getValueAt(P_intROWNO,TB1_SPALW).toString();
	     strSPALW = setNumberFormat(Double.parseDouble(strSPALW1),2);
	     strLNSUB1=tblGRADE.getValueAt(P_intROWNO,TB1_LNSUB).toString();
	     strLNSUB = setNumberFormat(Double.parseDouble(strLNSUB1),2);
	     strCONVY1=tblGRADE.getValueAt(P_intROWNO,TB1_CONVY).toString();
	     strCONVY = setNumberFormat(Double.parseDouble(strCONVY1),2);
	     strCHEDN1=tblGRADE.getValueAt(P_intROWNO,TB1_CHEDN).toString();
	     strCHEDN = setNumberFormat(Double.parseDouble(strCHEDN1),2);
	     strMDALW1=tblGRADE.getValueAt(P_intROWNO,TB1_MDALW).toString();
	     strMDALW = setNumberFormat(Double.parseDouble(strMDALW1),2);
	     strLTALW1=tblGRADE.getValueAt(P_intROWNO,TB1_LTALW).toString();
	     strLTALW = setNumberFormat(Double.parseDouble(strLTALW1),2);
	     strPFALW1=tblGRADE.getValueAt(P_intROWNO,TB1_PFALW).toString();
	     strPFALW = setNumberFormat(Double.parseDouble(strPFALW1),2);
	     strSAALW1=tblGRADE.getValueAt(P_intROWNO,TB1_SAALW).toString();
	     strSAALW = setNumberFormat(Double.parseDouble(strSAALW1),2);
	     strHRALW1=tblGRADE.getValueAt(P_intROWNO,TB1_HRALW).toString();
	     strHRALW = setNumberFormat(Double.parseDouble(strHRALW1),2);
	     strDNALW1=tblGRADE.getValueAt(P_intROWNO,TB1_DNALW).toString();
	     strDNALW = setNumberFormat(Double.parseDouble(strDNALW1),2);
	     strWKALW1=tblGRADE.getValueAt(P_intROWNO,TB1_WKALW).toString();
	     strWKALW = setNumberFormat(Double.parseDouble(strWKALW1),2);
	     strWSALW1=tblGRADE.getValueAt(P_intROWNO,TB1_WSALW).toString();
	     strWSALW = setNumberFormat(Double.parseDouble(strWSALW1),2);
	     strEMPCT=tblGRADE.getValueAt(P_intROWNO,TB1_EMPCT).toString();
			
	     M_strSQLQRY="UPDATE HR_GRHST SET ";
		 M_strSQLQRY += " GR_STSFL = 'X' ";
		 M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRDCD = '"+strGRDCD+"' and GR_STRDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"'";
		 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
 	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELREC");		
	  }
	}  
	
	private void exeDELREC1(int P_intROWNO)
	{ 
	  try
	  {
	     M_strSQLQRY="UPDATE HR_EHTRN SET ";
		 M_strSQLQRY += " EH_STSFL = 'X' ";
		 M_strSQLQRY += " where EH_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EH_EMPNO = '"+txtEMPNO.getText()+"' and EH_STRDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWNO,TB2_FMDAT).toString()))+"'";
		 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
 	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELREC1");		
	  }
	}  
		  
	/* method for enquiry of records  */ 

	private void exeMODREC(int P_intROWNO)
	{
	    try
	    {
		 strGRDCD=tblGRADE.getValueAt(P_intROWNO,TB1_GRDCD).toString();
	     strSTRDT=tblGRADE.getValueAt(P_intROWNO,TB1_STRDT).toString();
	     strENDDT=tblGRADE.getValueAt(P_intROWNO,TB1_ENDDT).toString();
	     strSPALW1=tblGRADE.getValueAt(P_intROWNO,TB1_SPALW).toString();
	     strSPALW = setNumberFormat(Double.parseDouble(strSPALW1),2);
	     strLNSUB1=tblGRADE.getValueAt(P_intROWNO,TB1_LNSUB).toString();
	     strLNSUB = setNumberFormat(Double.parseDouble(strLNSUB1),2);
	     strCONVY1=tblGRADE.getValueAt(P_intROWNO,TB1_CONVY).toString();
	     strCONVY = setNumberFormat(Double.parseDouble(strCONVY1),2);
	     strCHEDN1=tblGRADE.getValueAt(P_intROWNO,TB1_CHEDN).toString();
	     strCHEDN = setNumberFormat(Double.parseDouble(strCHEDN1),2);
	     strMDALW1=tblGRADE.getValueAt(P_intROWNO,TB1_MDALW).toString();
	     strMDALW = setNumberFormat(Double.parseDouble(strMDALW1),2);
	     strLTALW1=tblGRADE.getValueAt(P_intROWNO,TB1_LTALW).toString();
	     strLTALW = setNumberFormat(Double.parseDouble(strLTALW1),2);
	     strPFALW1=tblGRADE.getValueAt(P_intROWNO,TB1_PFALW).toString();
	     strPFALW = setNumberFormat(Double.parseDouble(strPFALW1),2);
	     strSAALW1=tblGRADE.getValueAt(P_intROWNO,TB1_SAALW).toString();
	     strSAALW = setNumberFormat(Double.parseDouble(strSAALW1),2);
	     strHRALW1=tblGRADE.getValueAt(P_intROWNO,TB1_HRALW).toString();
	     strHRALW = setNumberFormat(Double.parseDouble(strHRALW1),2);
	     strDNALW1=tblGRADE.getValueAt(P_intROWNO,TB1_DNALW).toString();
	     strDNALW = setNumberFormat(Double.parseDouble(strDNALW1),2);
	     strWKALW1=tblGRADE.getValueAt(P_intROWNO,TB1_WKALW).toString();
	     strWKALW = setNumberFormat(Double.parseDouble(strWKALW1),2);
	     strWSALW1=tblGRADE.getValueAt(P_intROWNO,TB1_WSALW).toString();
	     strWSALW = setNumberFormat(Double.parseDouble(strWSALW1),2);
	     strEMPCT=tblGRADE.getValueAt(P_intROWNO,TB1_EMPCT).toString();
			
	     M_strSQLQRY="UPDATE HR_GRHST SET ";
		 M_strSQLQRY += " GR_GRDCD = '"+strGRDCD+"',";
		 M_strSQLQRY += " GR_STRDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"',";
		 M_strSQLQRY += " GR_ENDDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"',";
		 M_strSQLQRY += " GR_SPALW = '"+strSPALW1+"',";
		 M_strSQLQRY += " GR_LNSUB = '"+strLNSUB1+"',";
		 M_strSQLQRY += " GR_CONVY = '"+strCONVY1+"',";
		 M_strSQLQRY += " GR_CHEDN = '"+strCHEDN+"',";
		 M_strSQLQRY += " GR_MDALW = '"+strMDALW+"',";
		 M_strSQLQRY += " GR_LTALW = '"+strLTALW+"',";
		 M_strSQLQRY += " GR_PFALW = '"+strPFALW+"',";
		 M_strSQLQRY += " GR_SAALW = '"+strSAALW+"',";
		 M_strSQLQRY += " GR_HRALW = '"+strHRALW+"',";
		 M_strSQLQRY += " GR_DNALW = '"+strDNALW+"',";
		 M_strSQLQRY += " GR_WKALW = '"+strWKALW+"',";
		 M_strSQLQRY += " GR_WSALW = '"+strWSALW+"',";
		 M_strSQLQRY += " GR_EMPCT = '"+strEMPCT+"',";
		 M_strSQLQRY += " GR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
		 M_strSQLQRY += " GR_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
		 M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRDCD = '"+strGRDCD+"' and GR_STRDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"'";
		 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	    }
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODREC");
	    }
	}
	
	private void exeMODREC1(int P_intROWNO)
	{
	    try
	    {
	     M_strSQLQRY="UPDATE HR_EHTRN SET ";
		 M_strSQLQRY += " EH_ENDDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWNO,TB2_TODAT).toString()))+"',";				
		 M_strSQLQRY += " EH_CMPRF = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_CMPRF).toString(),"")+"',";
		 M_strSQLQRY += " EH_DESGN = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_DESGN).toString(),"")+"',";
		 M_strSQLQRY += " EH_MMGRD = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_MMGRD).toString(),"")+"',";
		 M_strSQLQRY += " EH_DPTCD = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_DPTCD).toString(),"")+"',";
		 M_strSQLQRY += " EH_EPLOC = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_EPLOC).toString(),"")+"',";
		 if(tblHSTRY.getValueAt(P_intROWNO,TB2_TRFDT).toString().equals(""))
			M_strSQLQRY +=" EH_TRFDT =null,";
		 else
			 M_strSQLQRY += " EH_TRFDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWNO,TB2_TRFDT).toString()))+"',";
		 M_strSQLQRY += " EH_BASAL = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_BASAL).toString(),"")+"',";
		 M_strSQLQRY += " EH_DNALW = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_DNALW).toString(),"")+"',";
		 M_strSQLQRY += " EH_PPSAL = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_PPSAL).toString(),"")+"',";
		 M_strSQLQRY += " EH_TELAL = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_TELAL).toString(),"")+"',";
		 M_strSQLQRY += " EH_VHMAL = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_VHMAL).toString(),"")+"',";
		 M_strSQLQRY += " EH_GRSAL = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_GRSAL).toString(),"")+"',";
		 M_strSQLQRY += " EH_CTCVL = '"+nvlSTRVL(tblHSTRY.getValueAt(P_intROWNO,TB2_CTCVL).toString(),"")+"'";
		 M_strSQLQRY += " where EH_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EH_EMPNO = '"+txtEMPNO.getText()+"' and EH_STRDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblHSTRY.getValueAt(P_intROWNO,TB2_FMDAT).toString()))+"'";
		 //System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
		 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODREC1");
	    }
	}

}

