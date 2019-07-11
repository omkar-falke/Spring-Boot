/*
System Name   : Management Information System
Program Name  : Hardware Inventory
Program Desc. : Entry Screen for Hardware Inatallation.
Author        : Mrs.Dipti.S.Shinde.
Date          : 8th July 2005
System        : System admin 
Version       : MMS v2.0.0
Modificaitons : Addition of one panel(Location)and Some Columns and Modifications of Table Structure. 
Modified By   : Mrs.Dipti.S.Shinde.
Modified Date : 25th Aug 2005
Modified det. : 
Version       : MMS v2.0.0
*/

import javax.swing.JTabbedPane;import javax.swing.JTable;
import java.sql.ResultSet;import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.MouseEvent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JLabel;import javax.swing.JPanel;
/**
<PRE style = font-size : 10 pt >
<b>System Name :</b> Management Information System.
 
<b>Program Name :</b> Hardware Licences Installations

<b>Purpose :</b> This module is applicable to enter Hardware Installations & Location Details &
Location wise Hardware installations Details.
			
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
HW_LCMST        LC_LOCCD                      #        #      #       #       
HW_MCMST        MC_SRLNO,MC_MACNM,MC_LOCCD    #        #      #       #       
HW_PRMST        PR_SRLNO                      #        #      #       #       
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name  Column Name     Table name      Type/Size        Description
----------------------------------------------------------------------------------------------------------
cmbFLDNM	LC_LOCDS,		HW_LCMST    	 Varchar(10)     Location Master 
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description     Display Columns           Table Name
----------------------------------------------------------------------------------------------------------
txtLOC	    LC_LOCDS        HW_LCMST         Varchar(10)      Location Master
txtHDDDS    MC_HDDDS        HW_MCMST         Varchar(15)      Machine Master
txtLOCCD    MC_LOCCD        HW_MCMST         Varchar(10)     
txtCPUDS    MC_CPUDS        HW_MCMST         Varchar(30)       
txtOSNAM    MC_OSNAM        HW_MCMST         Varchar(15)         
txtVENDS    MC_VENDS        HW_MCMST         Varchar(30)         
txtCDRDS    MC_CDRDS        HW_MCMST         Varchar(30)          
txtRAMDS    MC_RAMDS        HW_MCMST         Varchar(15)         
txtLOCCD2   PR_LOCCD        HW_PRMST         Varchar(10)      Peripheral Master
txtPORDT    MC_PORDT        HW_MCMST         DATE()           
txtINSDT    MC_INSDT        HW_MCMST         DATE()           
txtEXPDT    MC_EXPDT        HW_MCMST         DATE()    
txtHODNM    LC_HODNM        HW_LCMST         Varchar(5)       Head of the departmane number.        
----------------------------------------------------------------------------------------------------------

Validations :
	* 1)Date should be in date format. 	
	* 2)While Adding new Record Value of HW_SRLNO,HW_MACNM shouldnot Blank.	
	* 3)To add new Record in the Printer Table,PR_SRLNO should not be Blank.	
	* 4)To add new Record in the location Table, LC_SRLNO should not be Blank.	
*/
public class sa_mehwm extends cl_pbase //implements ChangeListener//,PopupMenuListener
{
	private JTextField txtLOC;	
    private int intROWCNT =500;    
	private JTabbedPane jtpMANTAB;
    private JPanel pnlHWDTL,pnlADDTL,pnlPRDTL,pnlFRUDT,pnlLOCTN;
    private cl_JTable tblHWDTL,tblADDTL,tblPRDTL,tblFRUDT,tblLOCTN;
	private JLabel lblFLDNM,llbLOCDS;
	private int intSELRO;
	private final int TB1_CHKFL =0;		//Column Index for tblHWDTL.
	private final int TB1_MCHCT =1;
	private final int TB1_SRLNO =2;
	private final int TB1_LOCCD =3;
	private final int TB1_MACNM =4;
	private final int TB1_STSFL =5;
	private final int TB1_MATCD =6;
	private final int TB1_MAKDS =7;
	private final int TB1_MDLNO =8;
	private final int TB1_ACTRF =9;
	private final int TB1_CPUDS =10;
	private final int TB1_RAMDS =11;
	private final int TB1_HDDDS =12;
	private final int TB1_CDRDS =13;
	private final int TB1_KYBCD =14;
	private final int TB1_MOUCD =15;

	private final int TB2_CHKFL =0;		//Column Index for tblADDTL.
	private final int TB2_MACNM =1;
	private final int TB2_VENDS =2;
	private final int TB2_PORNO =3;
	private final int TB2_PORDT =4;
	private final int TB2_PORVL =5;
	private final int TB2_LICTP =6;
	private final int TB2_LICNO =7;
	private final int TB2_STRTP =8;
	private final int TB2_GRNRF =9;
	private final int TB2_INSDT =10;
	private final int TB2_EXPDT =11;
	private final int TB2_INSRM =12;
	private final int TB2_HRCRF =13;
	private final int TB2_SFCRF =14;
	private final int TB2_CRDBY =15;
		
	private final int TB3_CHKFL =0;		//Column index for tblADDDL.
	private final int TB3_MACNM =1;
	private final int TB3_MONDS =2;
	private final int TB3_OSNAM =3;
	private final int TB3_FRUMB =4;
	private final int TB3_FRUHD =5;
	private final int TB3_FRUPS =6;
	private final int TB3_FRUFD =7;
	private final int TB3_PRTPS =8;
	private final int TB3_PRTHD =9;
	private final int TB3_REMDS =10;	
										//Column index for tblPRDTL.
	private final int TB4_CHKFL =0;
	private final int TB4_PRFCT =1;
	private final int TB4_SRLNO =2;
	private final int TB4_MDLNO =3;
	private final int TB4_STSFL =4;
	private final int TB4_PRFDS =5;
	private final int TB4_LOCCD =6;
	private final int TB4_MATCD =7;
	private final int TB4_ACTRF =8;
	private final int TB4_VENDS =9;
	private final int TB4_PORNO =10;
	private final int TB4_PORDT =11;
	private final int TB4_PORVL =12;
	private final int TB4_STRTP =13;
	private final int TB4_GRNNO =14;
	private final int TB4_INSDT =15;
	private final int TB4_EXPDT =16;
	private final int TB4_INSRM =17;
	private final int TB4_HRCRF =18;
	private final int TB4_SFCRF =19;
	private final int TB4_CRDBY =20;
	
	private final int TB5_CHKFL =0;		/// Column index for tblLOCTN
	private final int TB5_LOCCD =1;
	private final int TB5_LOCDS =2;
	private final int TB5_ADR01 =3;
	private final int TB5_HODNM =4;
	private final int TB5_DPTCD =5;
	private final int TB5_SWTNO =6;
	private final int TB5_IPADD =7;
		
	private ButtonGroup btgOPTON;
	private JRadioButton rdbPRF;
	private JRadioButton rdbMAC;
	private JRadioButton rdbLOC;
	private JTextField txtLOCCD;
	private JTextField txtCPUDS;
	private JTextField txtOSNAM;
	private JTextField txtVENDS;
	private JTextField txtCDRDS;
	private JTextField txtRAMDS;
	private JTextField txtLOCCD2;
	private JTextField txtPORDT;
	private JTextField txtINSDT;
	private JTextField txtEXPDT;
	private JTextField txtHDDDS;
	private JTextField txtSRLNO;	
	private JTextField txtMATCD;
	private JTextField txtMATCD1;
	private JTextField txtACTRF;
	private JTextField txtPRFCT;
	private JTextField txtACTRF1;
	private JTextField txtPRFDS;
	private JTextField txtDPTCD;
	private JTextField txtHODNM;
	private JTextField txtADR01;
	private JTextField txtPSRLNO;
	private JTextField txtGRNNO;
	private JTextField txtGRNRF;
	private JTextField txtPSYSTP;
	private JTextField txtLSYSTP;
	private JTextField txtHSYSTP;
	private JTextField txtPINSDT;
	private JTextField txtPPORDT;
	private JTextField txtPEXPDT;
	private JTextField txtHSTRTP;
	private JTextField txtPSTRTP;
	private JTextField txtLICTP;
	private JTextField txtMAINL;
	private JTextField txtMCHCT;
	private JTextField txtMDLNO;
	private JTextField txtMAKDS;
	private JCheckBox chkSTSFL,chkSTSFL1;
	
	private JTextField txtKYBCD;
	private JTextField txtMOUCD;
	private JTextField txtMACNM;	
	private JTextField txtLOCCD1;
	private JTextField txtMONDS;
	private JTextField txtMACNM1;
	private JTextField txtPORVL;
	private JTextField txtHRCRF2;	// Additional Details Table 2
	private JTextField txtSFCRF2;
	private JTextField txtHRCRF4;	//peripheral details  Table 4
	private JTextField txtSFCRF4;
	private JTextField txtPORNO;
	private JTextField txtPORNO4;
	private int intROWCT;
    sa_mehwm()
	{
	  super(2);
		try
		{			
			setMatrix(20,8);
			add(llbLOCDS = new JLabel("System Type"),2,2,1,1,this,'L');
			add(txtMAINL = new JTextField(),2,3,1,1,this,'L'); 			
			add(lblFLDNM = new JLabel("Location Code"),2,4,1,1,this,'L');			
			add(txtLOC = new JTextField(),2,5,1,1,this,'L');
			btgOPTON = new ButtonGroup();
			add(rdbMAC = new JRadioButton("Computer",true),3,2,1,1,this,'R');
			add(rdbPRF = new JRadioButton("Peripheral",false),3,3,1,1,this,'L');
			add(rdbLOC = new JRadioButton("Location",false),3,4,1,1,this,'L');
				
			btgOPTON.add(rdbMAC);
			btgOPTON.add(rdbPRF);
			btgOPTON.add(rdbLOC);
				
			pnlADDTL = new JPanel();
			pnlHWDTL = new JPanel();
			pnlPRDTL = new JPanel();
			pnlFRUDT = new JPanel();
			pnlLOCTN = new JPanel();
				
			jtpMANTAB = new JTabbedPane();			
			pnlADDTL.setLayout(null);
			String[] L_strTBLHD1 = {"Select","Machine Name","Vendor","PO.No","PorDate","PorVL","Lic tp","LICNO","Str.type","GRIN No","InstDate","Exp.date","Inst. Remark","Hard Copy Ref.","Soft Copy Ref.","Coordinated By"};// Table Header
			int[] L_intCOLSZ1 = {40,100,100,100,80,80,80,80,20,80,80,80,150,100,100,40}; // Columnm Size in Table  
			tblADDTL = crtTBLPNL1(pnlADDTL,L_strTBLHD1,intROWCNT,1,1,12,7.9,L_intCOLSZ1,new int[]{0});
			add(pnlADDTL,1,1,10,9,jtpMANTAB,'L');			
		
			tblADDTL.setCellEditor(TB2_VENDS, txtVENDS = new TxtLimit(30));		txtVENDS.addKeyListener(this);	txtVENDS.addFocusListener(this);
			tblADDTL.setCellEditor(TB2_GRNRF, txtGRNRF = new TxtLimit(15));		txtGRNRF.addKeyListener(this);	txtGRNRF.addFocusListener(this);
			//tblADDTL.setCellEditor(TB2_PORDT, txtPORDT = new TxtDate());		txtPORDT.addKeyListener(this);	txtPORDT.addFocusListener(this);
			//tblADDTL.setCellEditor(TB2_INSDT, txtINSDT = new TxtDate());		txtINSDT.addKeyListener(this);	txtINSDT.addFocusListener(this);
			//tblADDTL.setCellEditor(TB2_EXPDT, txtEXPDT = new TxtDate());		txtEXPDT.addKeyListener(this);	txtEXPDT.addFocusListener(this);
			tblADDTL.setCellEditor(TB2_PORDT, txtPORDT = new JTextField());		txtPORDT.addKeyListener(this);	txtPORDT.addFocusListener(this);
			tblADDTL.setCellEditor(TB2_INSDT, txtINSDT = new JTextField());		txtINSDT.addKeyListener(this);	txtINSDT.addFocusListener(this);
			tblADDTL.setCellEditor(TB2_EXPDT, txtEXPDT = new JTextField());		txtEXPDT.addKeyListener(this);	txtEXPDT.addFocusListener(this);
			tblADDTL.setCellEditor(TB2_STRTP, txtHSTRTP = new TxtLimit(2));		txtHSTRTP.addKeyListener(this);	txtHSTRTP.addFocusListener(this);
			tblADDTL.setCellEditor(TB2_LICTP, txtLICTP = new TxtLimit(2));		txtLICTP.addKeyListener(this);	txtLICTP.addFocusListener(this);		
			tblADDTL.setCellEditor(TB2_MACNM, txtMACNM1 = new JTextField());	txtMACNM1.addKeyListener(this);	txtMACNM1.addFocusListener(this);			
			tblADDTL.setCellEditor(TB2_SFCRF, txtSFCRF2 = new JTextField());	txtSFCRF2.addKeyListener(this);	txtMACNM1.addFocusListener(this);
			tblADDTL.setCellEditor(TB2_HRCRF, txtHRCRF2 = new JTextField());	txtHRCRF2.addKeyListener(this);	txtMACNM1.addFocusListener(this);			
			tblADDTL.setCellEditor(TB2_PORNO, txtPORNO = new JTextField());		txtPORNO.addKeyListener(this);	txtMACNM1.addFocusListener(this);			
			
			pnlHWDTL.setLayout(null);
			String[] L_strTBLHD2 = {"Select","Machine Type","M/C Serial","Location","M/C Name","Usages Status","Material code","Make","Model","Accounts Reference","CPU Desc","RAM Desc","Harddisc","CD Drive","KeyBrd","Mouse"};// Table Header
			int[]L_intCOLSZ2 = {10,40,80,80,80,20,100,80,80,80,100,100,80,40,40,40}; // Columnm Size in Table  
			tblHWDTL = crtTBLPNL1(pnlHWDTL,L_strTBLHD2,intROWCNT,1,1,12,7.9,L_intCOLSZ2,new int[]{0,TB1_STSFL});
			add(pnlHWDTL,1,1,10,9,jtpMANTAB,'L');
			
			tblHWDTL.setCellEditor(TB1_MCHCT, txtMCHCT = new TxtLimit(2));		txtMCHCT.addKeyListener(this);		txtMCHCT.addFocusListener(this);
			tblHWDTL.setCellEditor(TB1_MATCD, txtMATCD1 = new TxtLimit(10));	txtMATCD1.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_ACTRF, txtACTRF = new TxtLimit(15));		txtACTRF.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_SRLNO, txtSRLNO = new TxtLimit(30));		txtSRLNO.addKeyListener(this);		txtSRLNO.addFocusListener(this);
			tblHWDTL.setCellEditor(TB1_LOCCD, txtLOCCD = new TxtLimit(10));		txtLOCCD.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_HDDDS, txtHDDDS = new TxtLimit(15));		txtHDDDS.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_CPUDS, txtCPUDS = new TxtLimit(30));		txtCPUDS.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_CDRDS, txtCDRDS = new TxtLimit(30));		txtCDRDS.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_RAMDS, txtRAMDS = new TxtLimit(15));		txtRAMDS.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_STSFL,chkSTSFL=new JCheckBox());
			tblHWDTL.setCellEditor(TB1_MDLNO, txtMDLNO = new TxtLimit(20));		txtMDLNO.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_MAKDS, txtMAKDS = new TxtLimit(20));		txtMAKDS.addKeyListener(this);			
			tblHWDTL.setCellEditor(TB1_KYBCD, txtKYBCD = new JTextField());		txtKYBCD.addKeyListener(this);
			tblHWDTL.setCellEditor(TB1_MOUCD, txtMOUCD = new JTextField(20));	txtMOUCD.addKeyListener(this);			
			((JCheckBox) tblHWDTL.cmpEDITR[TB1_STSFL]).addMouseListener(this);
			
			pnlFRUDT.setLayout(null);
			String[] L_strTBLHD3 = {"Select","M/C Name","Monitor","OS Name","FRU MB","FRUHD","FRU PS","Part No","Part No-HDD","PartNo-FDD","Remark"};// Table Header
			int[]L_intCOLSZ3 = {40,100,100,80,60,60,60,60,60,60,60}; // Columnm Size in Table  
			tblFRUDT = crtTBLPNL1(pnlFRUDT,L_strTBLHD3,intROWCNT,1,1,12,7.9,L_intCOLSZ3,new int[]{0});
			add(pnlFRUDT,1,1,10,9,jtpMANTAB,'L');
			
			tblFRUDT.setCellEditor(TB3_OSNAM, txtOSNAM = new TxtLimit(15));		txtOSNAM.addKeyListener(this);
			tblFRUDT.setCellEditor(TB3_MACNM, txtMACNM = new JTextField());		txtMACNM.addKeyListener(this);
			tblFRUDT.setCellEditor(TB3_MONDS, txtMONDS = new JTextField());		txtMONDS.addKeyListener(this);
		
			pnlPRDTL.setLayout(null);
			String[] L_strTBLHD4 = {"Select","Peripheral Categary","Serial No","Model No","Usages Status","Peripheral Desc.","Location","Material code","Account Reference","Vendors","PORNO","PORDATE","PORVALUE","Str.Type","GRIN No","Inst.Date","Exp.Date","Inst. Remark","Hard Copy Ref.","Soft Copy Ref.","Coordinated By"};// Table Header
			int[]L_intCOLSZ4 = {10,40,100,100,20,80,80,80,60,60,60,60,60,20,80,80,80,150,100,100,40}; //Columnm Size in Table  
			tblPRDTL = crtTBLPNL1(pnlPRDTL,L_strTBLHD4,intROWCNT,1,1,12,7.9,L_intCOLSZ4,new int[]{0,TB4_STSFL});
			add(pnlPRDTL,1,1,10,9,jtpMANTAB,'L');
			
			tblPRDTL.setCellEditor(TB4_PRFCT, txtPRFCT = new TxtLimit(2));		txtPRFCT.addKeyListener(this);			txtPRFCT.addFocusListener(this);
			tblPRDTL.setCellEditor(TB4_MATCD, txtMATCD = new TxtLimit(10));		txtMATCD.addKeyListener(this);			
			tblPRDTL.setCellEditor(TB4_ACTRF, txtACTRF = new TxtLimit(10));		txtACTRF.addKeyListener(this);
			tblPRDTL.setCellEditor(TB4_PRFDS, txtPRFDS = new TxtLimit(30));		txtPRFDS.addKeyListener(this);			
			tblPRDTL.setCellEditor(TB4_SRLNO, txtPSRLNO = new TxtLimit(15));	txtPSRLNO.addKeyListener(this);			txtPSRLNO.addFocusListener(this);
			tblPRDTL.setCellEditor(TB4_LOCCD, txtLOCCD1 = new TxtLimit(10));	txtLOCCD1.addKeyListener(this);			
			tblPRDTL.setCellEditor(TB4_GRNNO, txtGRNNO = new TxtLimit(8));		txtGRNNO.addKeyListener(this);
			tblPRDTL.setCellEditor(TB4_STSFL,chkSTSFL1=new JCheckBox());
			//tblPRDTL.setCellEditor(TB4_PORDT, txtPPORDT = new TxtDate());		txtPPORDT.addKeyListener(this);			
			//tblPRDTL.setCellEditor(TB4_INSDT, txtPINSDT = new TxtDate());		txtPINSDT.addKeyListener(this);
			//tblPRDTL.setCellEditor(TB4_EXPDT, txtPEXPDT = new TxtDate());		txtPEXPDT.addKeyListener(this);
			tblPRDTL.setCellEditor(TB4_PORDT, txtPPORDT = new JTextField());	txtPPORDT.addKeyListener(this);			
			tblPRDTL.setCellEditor(TB4_INSDT, txtPINSDT = new JTextField());	txtPINSDT.addKeyListener(this);
			tblPRDTL.setCellEditor(TB4_EXPDT, txtPEXPDT = new JTextField());	txtPEXPDT.addKeyListener(this);			
			tblPRDTL.setCellEditor(TB4_STRTP, txtPSTRTP = new TxtLimit(2));		txtPSTRTP.addKeyListener(this);
			tblPRDTL.setCellEditor(TB4_SFCRF, txtSFCRF4 = new TxtLimit(15));	txtSFCRF4.addKeyListener(this);
			tblPRDTL.setCellEditor(TB4_HRCRF, txtHRCRF4 = new TxtLimit(15));	txtHRCRF4.addKeyListener(this);			
			tblPRDTL.setCellEditor(TB4_PORNO, txtPORNO4 = new TxtLimit(10));	txtPORNO4.addKeyListener(this);			
			((JCheckBox) tblPRDTL.cmpEDITR[TB4_STSFL]).addMouseListener(this);
			
			tblPRDTL.setCellEditor(TB4_STRTP, txtPSTRTP = new TxtLimit(2));		txtPSTRTP.addKeyListener(this);		
			pnlLOCTN.setLayout(null);
			String[] L_strTBLHD5 = {"Select","Location code","Location Desc.","Address","HOD NAME","Dept No","Switch No","IP Address"};// Table Header
			int[]L_intCOLSZ5 = {30,80,140,100,120,80,70,120}; //Columnm Size in Table  
			tblLOCTN = crtTBLPNL1(pnlLOCTN,L_strTBLHD5,intROWCNT,1,1,12,7.9,L_intCOLSZ5,new int[]{0});
			add(pnlLOCTN,1,1,10,9,jtpMANTAB,'L');
			
			tblLOCTN.setCellEditor(TB5_DPTCD, txtDPTCD = new TxtLimit(3));		txtDPTCD.addKeyListener(this);
			tblLOCTN.setCellEditor(TB5_HODNM, txtHODNM = new TxtLimit(40));		txtHODNM.addKeyListener(this);
			tblLOCTN.setCellEditor(TB5_DPTCD, txtDPTCD = new TxtLimit(3));		txtDPTCD.addKeyListener(this);
			tblLOCTN.setCellEditor(TB5_ADR01, txtADR01 = new TxtLimit(30));		txtADR01.addKeyListener(this);
			tblLOCTN.setCellEditor(TB5_LOCCD, txtLOCCD2 = new TxtLimit(10));	txtLOCCD2.addKeyListener(this);		txtLOCCD2.addFocusListener(this);

			jtpMANTAB.addTab("Hardware Details",pnlHWDTL);
			jtpMANTAB.addTab("FRU and Other Details",pnlFRUDT);
			jtpMANTAB.addTab("Additional Details",pnlADDTL);
			jtpMANTAB.addTab("Peripheral Details",pnlPRDTL);
			jtpMANTAB.addTab("Location Details",pnlLOCTN);				
			add(jtpMANTAB,5,1,14,8,this,'L');
			setENBL(false);		
		}   
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super class method overrided to enable & disable the Component.
	 * @param L_flgSTAT boolean argument to pass the state for the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);		
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
		{
			txtMAINL.setEnabled(true);
			txtLOC.setEnabled(true);
			rdbPRF.setEnabled(true);
			rdbMAC.setEnabled(true);
			rdbLOC.setEnabled(true);		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				tblHWDTL.cmpEDITR[TB1_CHKFL].setEnabled(true);
				tblPRDTL.cmpEDITR[TB4_CHKFL].setEnabled(true);
				tblLOCTN.cmpEDITR[TB5_CHKFL].setEnabled(true);
			}
		}
		if(rdbMAC.isSelected())
		{
			jtpMANTAB.setEnabledAt(3,false);
			jtpMANTAB.setEnabledAt(4,false);
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
	     super.actionPerformed(L_AE);
		 try
		 {			
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{   
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{					
					setMSG("Please Select Option..",'N');						
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						setENBL(true);
					else
						setENBL(false);
					txtMAINL.requestFocus();
					txtMAINL.setText(M_strSBSCD.substring(0,2));
				}
				else
				{					
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					setMSG("select option..",'N');
					setENBL(false);
				}	
			}
		 	else if(M_objSOURC==txtLOC)
			{			 			
				tblHWDTL.clrTABLE();
				tblADDTL.clrTABLE();
				tblFRUDT.clrTABLE();
				tblPRDTL.clrTABLE();
				
				inlTBLEDIT(tblHWDTL);
				inlTBLEDIT(tblADDTL);
				inlTBLEDIT(tblFRUDT);
				inlTBLEDIT(tblPRDTL);
				getDATA();				
			}		
		 	else if(M_objSOURC==rdbPRF)
			{
				jtpMANTAB.setSelectedIndex(3);	
				jtpMANTAB.setEnabledAt(3,true);			
				jtpMANTAB.setEnabledAt(0,false);
				jtpMANTAB.setEnabledAt(1,false);
				jtpMANTAB.setEnabledAt(2,false);
				jtpMANTAB.setEnabledAt(4,false);		
				setMSG("select printer details",'N');	
				tblPRDTL.clrTABLE();
				inlTBLEDIT(tblPRDTL);
				getDATA();													
			}
			 else if(M_objSOURC==rdbMAC)
			 {
				jtpMANTAB.setSelectedIndex(0);
				jtpMANTAB.setEnabledAt(0,true);
				jtpMANTAB.setEnabledAt(1,true);
				jtpMANTAB.setEnabledAt(2,true);
				jtpMANTAB.setEnabledAt(3,false);
				jtpMANTAB.setEnabledAt(4,false);
				inlTBLEDIT(tblHWDTL);
				inlTBLEDIT(tblADDTL);
				inlTBLEDIT(tblFRUDT);
				setMSG("select Hardware details",'N');
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					getDATA();				
				else
				{
					tblPRDTL.clrTABLE();
					tblLOCTN.clrTABLE();
				}						
			}
			else if(M_objSOURC==rdbLOC)
			{
				txtLOC.setText(txtLOC.getText().trim().toUpperCase());
				jtpMANTAB.setSelectedIndex(4);
				jtpMANTAB.setEnabledAt(4,true);
				jtpMANTAB.setEnabledAt(0,false);
				jtpMANTAB.setEnabledAt(1,false);
				jtpMANTAB.setEnabledAt(2,false);
				jtpMANTAB.setEnabledAt(3,false);
				inlTBLEDIT(tblLOCTN);
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					getDATA();
				else
				{
					tblHWDTL.clrTABLE();
					tblADDTL.clrTABLE();
					tblFRUDT.clrTABLE();
					tblPRDTL.clrTABLE();
				}
			}
			else if(M_objSOURC == txtMAINL)
			{
				if(txtMAINL.getText().trim().length()>0)
				{
					txtLOC.requestFocus();
					setMSG("Please Enter Location Code, if Perticular Department Details Requried..",'N');
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}	
	}
	
	
	 public void mouseReleased(MouseEvent L_KE)
	 {
  	    super.mouseReleased(L_KE);
		try
		{ 	
			 if(L_KE.getSource().equals(chkSTSFL))
			 {
				if(chkSTSFL.isSelected())
					tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			 }
			 else if(L_KE.getSource().equals(chkSTSFL1))
			 {
				if(chkSTSFL1.isSelected())
					tblPRDTL.setValueAt(new Boolean(true),tblPRDTL.getSelectedRow(),TB4_CHKFL);
			 }
		}
		catch(Exception L_EX) 
		{
			setMSG(L_EX,"mouseReleased");
		}
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == tblHWDTL.cmpEDITR[TB1_MCHCT])
		{			
			if(tblHWDTL.getSelectedRow() < intROWCT)
				 ((JTextField)tblHWDTL.cmpEDITR[TB1_MCHCT]).setEditable(false);
			else				
				((JTextField)tblHWDTL.cmpEDITR[TB1_MCHCT]).setEditable(true);		
		}
		else if(M_objSOURC == tblHWDTL.cmpEDITR[TB1_SRLNO])
		{
			if(tblHWDTL.getSelectedRow() < intROWCT)
				 ((JTextField)tblHWDTL.cmpEDITR[TB1_SRLNO]).setEditable(false);
			else				
				((JTextField)tblHWDTL.cmpEDITR[TB1_SRLNO]).setEditable(true);		
		}
		else if(M_objSOURC == tblLOCTN.cmpEDITR[TB5_LOCCD]) //location Master
		{			
			if(tblLOCTN.getSelectedRow() < intROWCT)
				 ((JTextField)tblLOCTN.cmpEDITR[TB5_LOCCD]).setEditable(false);
			else				
				((JTextField)tblLOCTN.cmpEDITR[TB5_LOCCD]).setEditable(true);		
		}				
		else if(M_objSOURC == tblPRDTL.cmpEDITR[TB4_PRFCT])// Periferal Master
		{			
			if(tblPRDTL.getSelectedRow() < intROWCT)
				 ((JTextField)tblPRDTL.cmpEDITR[TB4_PRFCT]).setEditable(false);
			else				
				((JTextField)tblPRDTL.cmpEDITR[TB4_PRFCT]).setEditable(true);		
		}
		if(M_objSOURC == tblPRDTL.cmpEDITR[TB4_SRLNO])
		{			
			if(tblPRDTL.getSelectedRow() < intROWCT)
				 ((JTextField)tblPRDTL.cmpEDITR[TB4_SRLNO]).setEditable(false);
			else				
				((JTextField)tblPRDTL.cmpEDITR[TB4_SRLNO]).setEditable(true);		
		}		
		if(M_objSOURC == txtLOC)
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG("press F1 to select the value..",'N');
			else
				setMSG(" ",'N');
		}				
	}
	public void keyPressed(KeyEvent L_KE)
	{ 
		super.keyPressed(L_KE);
   		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			if((M_objSOURC == txtPORNO4) && (M_strSBSCD.substring(0,2).equals("02")))
			{
				M_strHLPFLD = "txtPORNO4";
				intSELRO = tblPRDTL.getSelectedRow();
				cl_dat.M_flgHELPFL_pbst = true;					
				M_strSQLQRY = " SELECT PO_PORNO,PT_PRTNM,PO_PORVL,PO_MATCD,PO_MATDS from MM_POMST,CO_PTMST"; 
				M_strSQLQRY += " where PO_STRTP = '21' and isnull(PO_STSFL,' ') <> 'X'";
				M_strSQLQRY += " AND PT_PRTCD = PO_VENCD AND PT_PRTTP ='S'";
				if((tblADDTL.getValueAt(tblADDTL.getSelectedRow(),TB2_PORNO).toString().trim().length()) > 0)
					M_strSQLQRY += " AND PO_PORNO like '"+ txtPORNO.getText().trim() +"%'";				
				M_strSQLQRY += " order by PO_MATDS";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"PO Number","Vendor Desc","POR Value","Material Code","Material Desc"},5,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}	
			else if((M_objSOURC == txtPORNO) && (M_strSBSCD.substring(0,2).equals("02")))
			{
				M_strHLPFLD = "txtPORNO";
				intSELRO = tblADDTL.getSelectedRow();
				cl_dat.M_flgHELPFL_pbst = true;					
				M_strSQLQRY = " SELECT PO_PORNO,PT_PRTNM,PO_PORVL,PO_MATDS from MM_POMST,CO_PTMST"; //PO_MATDS,
				M_strSQLQRY += " where PO_STRTP = '21' and isnull(PO_STSFL,' ') <> 'X'";
				M_strSQLQRY += " AND PT_PRTCD = PO_VENCD AND PT_PRTTP ='S'";
				if((tblADDTL.getValueAt(tblADDTL.getSelectedRow(),TB2_PORNO).toString().trim().length()) > 0)
					M_strSQLQRY += " AND PO_PORNO like '"+ txtPORNO.getText().trim() +"%'";				
				M_strSQLQRY += " order by PO_MATDS";								
				cl_hlp(M_strSQLQRY,1,1,new String[]{"PO Number","Vendor Desc","POR Value","Material Desc"},4,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				
			if(M_objSOURC == txtGRNRF)
			{
				/*if(tblPRDTL.getValueAt(tblPRDTL.getSelectedRow(),TB4_MATCD).toString().trim() != 10)
				{
					setMSG("Please Enter Valid Material Code, Or Press F1 to select from List..",'E');
					tblPRDTL.editCellAt(tblPRDTL.getSelectedRow(),TB4_MATCD);
					tblPRDTL.cmpEDITR[TB4_MATCD].requestFocus();
					return;
				}*/								
				M_strHLPFLD = "txtGRNRF";
				intSELRO = tblADDTL.getSelectedRow();
				cl_dat.M_flgHELPFL_pbst = true;					
				M_strSQLQRY = " SELECT GR_GRNNO,GR_PORNO,GR_VENNM,GR_PORVL,GR_MATCD,CT_MATDS from CO_CTMST,MM_GRMST where CT_MATCD = GR_MATCD";	
				//M_strSQLQRY += " AND CT_MATCD like '99404%' OR CT_MATCD like '85%' OR CT_MATCD like '9911%'";				
				//if((tblADDTL.getValueAt(tblADDTL.getSelectedRow(),TB4_MATCD).toString().trim().length()) == 10 )
				//	M_strSQLQRY += " GR_MATCD = '"+ tblADDTL.getValueAt(tblADDTL.getSelectedRow(),TB4_MATCD).toString().trim() +"' AND";
				M_strSQLQRY += " AND GR_STRTP = '01' and isnull(GR_STSFL,' ') <> 'X'";
				if((tblADDTL.getValueAt(tblADDTL.getSelectedRow(),TB2_GRNRF).toString().trim().length()) > 0)
					M_strSQLQRY += " AND GR_GRNNO like '"+ txtGRNNO.getText().trim() +"%'";				
				M_strSQLQRY += " order by CT_MATDS";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Number","PO Number","Vendor Desc","POR Value","Material Code","Material Desc."},6,"CT");								
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtGRNNO)
			{
				/*if(tblPRDTL.getValueAt(tblPRDTL.getSelectedRow(),TB4_MATCD).toString().trim() != 10)
				{
					setMSG("Please Enter Valid Material Code, Or Press F1 to select from List..",'E');
					tblPRDTL.editCellAt(tblPRDTL.getSelectedRow(),TB4_MATCD);
					tblPRDTL.cmpEDITR[TB4_MATCD].requestFocus();
					return;
				}*/					   				
				M_strHLPFLD = "txtGRNNO";
				intSELRO = tblPRDTL.getSelectedRow();
				cl_dat.M_flgHELPFL_pbst = true;					
				M_strSQLQRY = " SELECT GR_GRNNO,GR_PORNO,GR_VENNM,GR_MATCD,GR_PORVL from MM_GRMST where ";				
				if((tblPRDTL.getValueAt(tblPRDTL.getSelectedRow(),TB4_MATCD).toString().trim().length()) == 10 )
					M_strSQLQRY += " GR_MATCD = '"+ tblPRDTL.getValueAt(tblPRDTL.getSelectedRow(),TB4_MATCD).toString().trim() +"' AND";
				M_strSQLQRY += " GR_STRTP = '01' and isnull(GR_STSFL,' ') <> 'X'";
				if((tblPRDTL.getValueAt(tblPRDTL.getSelectedRow(),TB4_GRNNO).toString().trim().length()) > 0)
					M_strSQLQRY += " AND GR_GRNNO like '"+ txtGRNNO.getText().trim() +"%'";				
				M_strSQLQRY += " order by GR_GRNNO";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Number","PO Number","Vendor Desc","Material Code","POR Value"},5,"CT");								
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtADR01)
			{				
				M_strHLPFLD = "txtADR01";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct LC_ADR01 from HW_lcMST where LC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(LC_STSFL,' ') <> 'X' and LC_SBSCD in "+M_strSBSLS;
				if(txtADR01.getText().length() >0)
					M_strSQLQRY += " and LC_ADR01 like '"+ txtPRFDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Monitor Description"},1,"CT");								
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_objSOURC == txtPRFDS)
			{
				M_strHLPFLD = "txtPRFDS";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct PR_PRFDS from HW_PRMST where PR_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(PR_STSFL,' ') <> 'X' and PR_SBSCD in "+M_strSBSLS;
				if(txtPRFDS.getText().length() >0)
					M_strSQLQRY += " and PR_PRFDS like '"+ txtPRFDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Monitor Description"},1,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				
			else if(M_objSOURC == txtSFCRF2)
			{
				M_strHLPFLD = "txtSFCRF2";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HW"+txtMAINL.getText().trim()+"SFC' and isnull(CMT_STSFL,' ') <> 'X' order by CMT_CODCD";
				if(txtSFCRF2.getText().length() >0)
					M_strSQLQRY += " and CMT_CODDS like '"+ txtSFCRF2.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"code","Description"},2,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				
			else if(M_objSOURC == txtSFCRF4)
			{
				M_strHLPFLD = "txtSFCRF4";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HW"+txtMAINL.getText().trim()+"SFC' and isnull(CMT_STSFL,' ') <> 'X' order by CMT_CODCD";
				if(txtSFCRF4.getText().length() >0)
					M_strSQLQRY += " and CMT_CODDS like '"+ txtSFCRF4.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"code","Description"},2,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				
			else if(M_objSOURC == txtHRCRF2)
			{
				M_strHLPFLD = "txtHRCRF2";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HW"+txtMAINL.getText().trim()+"HRC' and isnull(CMT_STSFL,' ') <> 'X' order by CMT_CODCD";
				if(txtHRCRF2.getText().length() >0)
					M_strSQLQRY += " and CMT_CODDS like '"+ txtHRCRF2.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"code","Description"},2,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				
			else if(M_objSOURC == txtHRCRF4)
			{
				M_strHLPFLD = "txtHRCRF4";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HW"+txtMAINL.getText().trim()+"HRC' and isnull(CMT_STSFL,' ') <> 'X' order by CMT_CODCD";
				if(txtHRCRF4.getText().length() >0)
					M_strSQLQRY += " and CMT_CODDS like '"+ txtHRCRF4.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"code","Description"},2,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				
			else if(M_objSOURC == txtMONDS)
			{
				M_strHLPFLD = "txtMONDS";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct MC_MONDS from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtMONDS.getText().length() >0)
					M_strSQLQRY += " and MC_VENDS like '"+ txtMONDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Monitor Description"},1,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtMACNM1)
			{
				M_strHLPFLD = "txtMACNM1";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct MC_MACNM from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtMACNM1.getText().length() >0)
					M_strSQLQRY += " and MC_MACNM like '"+txtMACNM1.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Machine Name"},1,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(M_objSOURC == txtLOCCD2)
			{
				M_strHLPFLD = "txtLOCCD2";
				cl_dat.M_flgHELPFL_pbst = true;					
				M_strSQLQRY=" SELECT  LC_LOCCD,LC_LOCDS from HW_LCMST where isnull(LC_STSFL,' ') <> 'X' and LC_SYSTP = '"+txtMAINL.getText().trim()+"' and LC_SBSCD in "+M_strSBSLS;
				if(txtLOCCD2.getText().length() >0)
					M_strSQLQRY += " and LC_LOCCD like '"+ txtLOCCD2.getText().trim().toUpperCase()+"%'";
				M_strSQLQRY += " order by LC_LOCCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Code","Description"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtLOCCD1)
			{
				M_strHLPFLD = "txtLOCCD1";
				cl_dat.M_flgHELPFL_pbst = true;					
				M_strSQLQRY=" SELECT  LC_LOCCD,LC_LOCDS from HW_LCMST where isnull(LC_STSFL,' ') <> 'X' and LC_SYSTP = '"+txtMAINL.getText().trim()+"' and LC_SBSCD in "+M_strSBSLS;
				if(txtLOCCD1.getText().length() >0)
					M_strSQLQRY += " and LC_LOCCD like '"+ txtLOCCD1.getText().trim().toUpperCase()+"%'";
				M_strSQLQRY += " order by LC_LOCCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Code","Description"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_objSOURC == txtMACNM)
			{
				M_strHLPFLD = "txtMACNM";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct MC_MACNM from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtMACNM.getText().length() >0)
					M_strSQLQRY += " and MC_MACNM like '"+txtMACNM.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Machine Name"},1,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			 			 
			if(M_objSOURC == txtMOUCD)
			{
				M_strHLPFLD = "txtMOUCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct MC_MOUCD from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtMOUCD.getText().length() >0)
					M_strSQLQRY += " and MC_MOUCD like '"+txtMOUCD.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Mouse"},1,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtKYBCD)
			{
				M_strHLPFLD = "txtKYBCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct MC_KYBCD from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtKYBCD.getText().length() >0)
					M_strSQLQRY += " and txtKYBCD like '"+txtKYBCD.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Key Board"},1,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				
			else if(M_objSOURC == txtMAKDS)
			{								
				M_strHLPFLD = "txtMAKDS";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct MC_MAKDS from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtMAKDS.getText().length() >0)
					M_strSQLQRY += " and MC_MAKDS like '"+txtMAKDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Manufaturar"},1,"CT");				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtMDLNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strHLPFLD = "txtMDLNO";
				M_strSQLQRY=" SELECT distinct MC_MDLNO from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtMDLNO.getText().length() >0)
					M_strSQLQRY += " and MC_MDLNO like '"+txtMDLNO.getText().trim()+"%'";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Model Number"},1,"CT");		
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtMAINL)
			{			
				M_strHLPFLD = "txtMAINL";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT substring(CMT_CODCD,3,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP = 'COXXSBS' and CMT_CODCD like 'SA%' and SUBSTRING(CMT_CODCD,3,2) + '0000' in "+M_strSBSLS;
				if(txtMAINL.getText().length() >0)			
					M_strSQLQRY += " and substring(CMT_CODCD,3,2) like '"+txtMAINL.getText().trim()+"%'";
				M_strSQLQRY += " order By CMT_CODCD ";		
				cl_hlp(M_strSQLQRY,1,1,new String[]{"System code","description"},2,"CT");										
			}
			else if(M_objSOURC == txtLOC)
			{
				M_strHLPFLD = "txtLOC";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = " Select distinct LC_LOCCD,LC_LOCDS from HW_LCMST where LC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(LC_STSFL,' ') <> 'X' and LC_SBSCD in "+M_strSBSLS;
				if(txtLOC.getText().trim().length()>0)
				 	M_strSQLQRY += " and LC_LOCCD like '"+ txtLOC.getText().trim().toUpperCase()+"%'"; 
				M_strSQLQRY += " order by LC_LOCCD DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Location code","Description"},2,"CT");
			}	
			else if(M_objSOURC==txtDPTCD)
			{						
				M_strHLPFLD = "txtDPTCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP = 'COXXDPT' ";
				if(txtLOCCD.getText().length() >0)
					M_strSQLQRY += " and CMT_CODCD like '"+txtLOCCD.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Dept Code","Dept name"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			 }
			else if(M_objSOURC==txtLOCCD)
			{									
				M_strHLPFLD = "txtLOCCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				txtLOC.setText(txtLOC.getText().trim().toUpperCase());
				M_strSQLQRY=" SELECT  LC_LOCCD,LC_LOCDS from HW_LCMST where isnull(LC_STSFL,' ') <> 'X' and LC_SYSTP = '"+txtMAINL.getText().trim()+"' and LC_SBSCD in "+M_strSBSLS;
				if(txtLOCCD.getText().length() >0)
					M_strSQLQRY += " and LC_LOCCD like '"+txtLOC.getText().trim().toUpperCase()+"%'";
				M_strSQLQRY += " order by LC_LOCCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Code","Description"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==txtMATCD1)
			{									
				M_strHLPFLD = "txtMATCD1";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT  CT_MATCD,CT_MATDS from CO_CTMST where CT_MATCD like '99404%' OR CT_MATCD like '85%' OR CT_MATCD like '9911%'";
				if(txtMATCD1.getText().length() >0)				
					M_strSQLQRY += " and CT_MATCD like '"+txtMATCD1.getText().trim()+"%'";
				M_strSQLQRY += " order by CT_MATDS";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","material Name"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==txtMATCD)
			{									
				M_strHLPFLD = "txtMATCD";
				M_strSQLQRY=" SELECT  CT_MATCD,CT_MATDS from CO_CTMST where CT_MATCD like '99404%' OR CT_MATCD like '85%' OR CT_MATCD like '9911%'";
				if(txtMATCD.getText().length() >0)				
					M_strSQLQRY += " and CT_MATCD like '"+txtMATCD.getText().trim()+"%'";				
				M_strSQLQRY += " order by CT_MATDS";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","material Name"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC== txtPRFCT)
			{						
				M_strHLPFLD = "txtPRFCT";
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP = 'HWXXPRF' ";
				if(txtPRFCT.getText().length() >0)
					M_strSQLQRY += " and CMT_CODCD like '"+ txtPRFCT.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"PRF Categary","Description"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_objSOURC==txtHODNM)
			{
				M_strHLPFLD = "txtHODNM";
				/*M_strSQLQRY=" SELECT EP_EMPNO,EP_FULNM from HR_EPMST where isnull(EP_STSFL,' ') <> 'X' ";
				if(txtHODNM.getText().length() >0)			
					M_strSQLQRY += " and EP_EMPNO like '"+txtHODNM.getText().trim()+"%'";			
				M_strSQLQRY +=" order by EP_FULNM";*/
				M_strSQLQRY=" SELECT distinct LC_HODNM from HW_LCMST where LC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(LC_STSFL,' ') <> 'X' and LC_SBSCD in "+M_strSBSLS;
				if(txtHODNM.getText().length() >0)
					M_strSQLQRY += " and LC_HODNM like '"+txtHODNM.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"HOD Name"},1,"CT");//					
				setCursor(cl_dat.M_curDFSTS_pbst);
			 }
			else if(M_objSOURC==txtHDDDS)
			{								
				M_strHLPFLD = "txtHDDDS";
				M_strSQLQRY=" SELECT distinct MC_HDDDS from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X' and MC_SBSCD in "+M_strSBSLS;
				if(txtHDDDS.getText().length() >0)
					M_strSQLQRY += " and MC_HDDDS like '"+txtHDDDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"HardDisk"},1,"CT");		
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_objSOURC==txtCPUDS)
			{								
				M_strHLPFLD = "txtCPUDS";
				M_strSQLQRY=" SELECT distinct MC_CPUDS from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X'  and MC_SBSCD in "+M_strSBSLS;
				if(txtCPUDS.getText().length() >0)
					M_strSQLQRY += " and MC_CPUDS like '"+txtCPUDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"CPU Type"},1,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==txtOSNAM)
			{							
				M_strHLPFLD = "txtOSNAM";
				M_strSQLQRY=" SELECT distinct MC_OSNAM from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X'  and MC_SBSCD in "+M_strSBSLS;
				if(txtOSNAM.getText().length() >0)
					M_strSQLQRY += " and MC_OSNAM like '"+txtOSNAM.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Operating System"},1,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==txtCDRDS)
			{										 
				M_strHLPFLD = "txtCDRDS";
				M_strSQLQRY=" SELECT distinct MC_CDRDS from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X'  and MC_SBSCD in "+M_strSBSLS;
				if(txtCDRDS.getText().length() >0)
					M_strSQLQRY += " and MC_CDRDS like '"+txtCDRDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"CD drive"},1,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==txtRAMDS)
			{								
				M_strHLPFLD = "txtRAMDS";
				M_strSQLQRY=" SELECT distinct MC_RAMDS from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X'  and MC_SBSCD in "+M_strSBSLS;
				if(txtRAMDS.getText().length() >0)
					M_strSQLQRY += " and MC_RAMDS like '"+txtRAMDS.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Ram type"},1,"CT");		
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==txtVENDS)
			{						
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{		
					M_strHLPFLD = "txtVENDS";
					M_strSQLQRY=" SELECT distinct MC_VENDS from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(MC_STSFL,' ') <> 'X'  and MC_SBSCD in "+M_strSBSLS;
					if(txtVENDS.getText().length() >0)
						M_strSQLQRY += " and MC_VENDS like '"+txtVENDS.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"vendors"},1,"CT");
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_objSOURC== txtMCHCT)
			{									
				M_strHLPFLD = "txtMCHCT";
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='HWXXMCH' ";
				if(txtMCHCT.getText().length() >0)			
					M_strSQLQRY += " and CMT_CODCD like '"+ txtMCHCT.getText().trim()+"%'";			
				cl_hlp(M_strSQLQRY,1,1,new String[]{"MCH Categary","Description"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}		
		}
	}
	/**
	*  Super Class method overrided to execuate the F1 Help.
	*/
	public void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD == "txtPORNO4")
			{
				inlTBLEDIT(tblPRDTL);
				cl_dat.M_flgHELPFL_pbst = false;				
				tblPRDTL.setValueAt(cl_dat.M_strHLPSTR_pbst,intSELRO,TB4_PORNO);
				tblPRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),intSELRO,TB4_VENDS);			
				tblPRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intSELRO,TB4_PORVL);
				tblPRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),intSELRO,TB4_MATCD);
				tblPRDTL.setValueAt("21",intSELRO,TB4_STRTP);
				tblPRDTL.setValueAt(new Boolean(true),intSELRO,TB4_CHKFL);
			}
			else if(M_strHLPFLD == "txtPORNO")
			{
				inlTBLEDIT(tblADDTL);
				cl_dat.M_flgHELPFL_pbst = false;				
				tblADDTL.setValueAt(cl_dat.M_strHLPSTR_pbst,intSELRO,TB2_PORNO);
				tblADDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),intSELRO,TB2_VENDS);			
				tblADDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intSELRO,TB2_PORVL);
				tblADDTL.setValueAt("21",intSELRO,TB2_STRTP);
				//tblADDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),intSELRO,TB2_MATDS);				
				tblADDTL.setValueAt(new Boolean(true),intSELRO,TB2_CHKFL);
			}
			else if(M_strHLPFLD == "txtGRNRF")
			{
				inlTBLEDIT(tblADDTL);
				cl_dat.M_flgHELPFL_pbst = false;				
				tblADDTL.setValueAt(cl_dat.M_strHLPSTR_pbst,intSELRO,TB2_GRNRF);
				tblADDTL.setValueAt("01",intSELRO,TB2_STRTP);
				tblADDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),intSELRO,TB2_PORNO);
				tblADDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intSELRO,TB2_VENDS);			
				tblADDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim(),intSELRO,TB2_PORVL);
				tblADDTL.setValueAt(new Boolean(true),intSELRO,TB4_CHKFL);
			}
			else if(M_strHLPFLD == "txtGRNNO")
			{
				inlTBLEDIT(tblPRDTL);
				cl_dat.M_flgHELPFL_pbst = false;				
				tblPRDTL.setValueAt(cl_dat.M_strHLPSTR_pbst,intSELRO,TB4_GRNNO);
				tblPRDTL.setValueAt("01",intSELRO,TB4_STRTP);
				tblPRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),intSELRO,TB4_PORNO);
				tblPRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intSELRO,TB4_VENDS);
				tblPRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),intSELRO,TB4_MATCD);				
				tblPRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim(),intSELRO,TB4_PORVL);
				tblPRDTL.setValueAt(new Boolean(true),intSELRO,TB4_CHKFL);				
			}
			else if(M_strHLPFLD == "txtADR01")
			{
				txtADR01.setText(cl_dat.M_strHLPSTR_pbst);				
				tblLOCTN.setValueAt(new Boolean(true),tblLOCTN.getSelectedRow(),TB5_CHKFL);
				cl_dat.M_flgHELPFL_pbst = false;	
			}
			else if(M_strHLPFLD == "txtMONDS")
			{
				txtMONDS.setText(cl_dat.M_strHLPSTR_pbst);
				tblFRUDT.setValueAt(new Boolean(true),tblFRUDT.getSelectedRow(),TB3_CHKFL);
				cl_dat.M_flgHELPFL_pbst = false;	
			}
			else if(M_strHLPFLD == "txtPRFDS")
			{
				txtPRFDS.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;
				tblPRDTL.setValueAt(new Boolean(true),tblPRDTL.getSelectedRow(),TB4_CHKFL);
			}
			else if(M_strHLPFLD == "txtMACNM1")
			{
				txtMACNM1.setText(cl_dat.M_strHLPSTR_pbst);
				tblADDTL.setValueAt(new Boolean(true),tblADDTL.getSelectedRow(),TB2_CHKFL);
				cl_dat.M_flgHELPFL_pbst = false;	
			}
			else if(M_strHLPFLD == "txtMACNM")
			{
				txtMACNM.setText(cl_dat.M_strHLPSTR_pbst);
				tblFRUDT.setValueAt(new Boolean(true),tblFRUDT.getSelectedRow(),TB3_CHKFL);
				cl_dat.M_flgHELPFL_pbst = false;	
			}
			else if(M_strHLPFLD == "txtLOCCD2")
			{
				txtLOCCD2.setText(cl_dat.M_strHLPSTR_pbst);
				tblLOCTN.setValueAt(new Boolean(true),tblLOCTN.getSelectedRow(),TB5_CHKFL);
				cl_dat.M_flgHELPFL_pbst = false;	
			}
			else if(M_strHLPFLD == "txtLOCCD1")
			{
				txtLOCCD1.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;	
				tblPRDTL.setValueAt(new Boolean(true),tblPRDTL.getSelectedRow(),TB4_CHKFL);
			}
			else if(M_strHLPFLD == "txtMOUCD")
			{
				txtMOUCD.setText(cl_dat.M_strHLPSTR_pbst);
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
				cl_dat.M_flgHELPFL_pbst = false;	
			}
			else if(M_strHLPFLD == "txtKYBCD")
			{
				txtKYBCD.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;	
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtMCHCT")
			{
				txtMCHCT.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;	
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtMAINL")
			{
				txtMAINL.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;
			}
			else if(M_strHLPFLD == "txtDPTCD")			
			{
				txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;
				tblLOCTN.setValueAt(new Boolean(true),tblLOCTN.getSelectedRow(),TB5_CHKFL);
			}
			else if(M_strHLPFLD == "txtLOC")			
			{
				txtLOC.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;	
			}
			else if(M_strHLPFLD == "txtMATCD")
			{
				txtMATCD.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;	
				tblPRDTL.setValueAt(new Boolean(true),tblPRDTL.getSelectedRow(),TB4_CHKFL);
			}
			else if(M_strHLPFLD == "txtMATCD1")
			{
				txtMATCD1.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtPRFCT")
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtPRFCT.setText(cl_dat.M_strHLPSTR_pbst);					
				//txtPRFDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
				tblPRDTL.setValueAt(new Boolean(true),tblPRDTL.getSelectedRow(),TB4_CHKFL);
			}			
			else if(M_strHLPFLD == "txtHODNM")
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtHODNM.setText(cl_dat.M_strHLPSTR_pbst);
				tblLOCTN.setValueAt(new Boolean(true),tblLOCTN.getSelectedRow(),TB5_CHKFL);
			}
			else if(M_strHLPFLD == "txtLOCCD")
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtLOCCD.setText(cl_dat.M_strHLPSTR_pbst);
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtHDDDS")
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtHDDDS.setText(cl_dat.M_strHLPSTR_pbst);					
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtCPUDS")
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtCPUDS.setText(cl_dat.M_strHLPSTR_pbst);
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtOSNAM")
			{
				txtOSNAM.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblFRUDT.setValueAt(new Boolean(true),tblFRUDT.getSelectedRow(),TB3_CHKFL);
			}
			else if(M_strHLPFLD == "txtCDRDS")
			{
				txtCDRDS.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtRAMDS")
			{
				txtRAMDS.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}			
			else if(M_strHLPFLD == "txtVENDS")
			{
				txtVENDS.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblADDTL.setValueAt(new Boolean(true),tblADDTL.getSelectedRow(),TB2_CHKFL);
			}
			else if(M_strHLPFLD == "txtMAKDS")
			{
				txtMAKDS.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtMDLNO")
			{
				txtMDLNO.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtHRCRF2")
			{
				txtHRCRF2.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtHRCRF4")
			{
				txtHRCRF4.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}			
			else if(M_strHLPFLD == "txtSFCRF2")
			{
				txtSFCRF2.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
			else if(M_strHLPFLD == "txtSFCRF4")
			{
				txtSFCRF4.setText(cl_dat.M_strHLPSTR_pbst);	
				cl_dat.M_flgHELPFL_pbst = false;
				tblHWDTL.setValueAt(new Boolean(true),tblHWDTL.getSelectedRow(),TB1_CHKFL);
			}
		}
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Method to insert & update the data in the data base.
	 */
	void exeSAVE()
	{
		setMSG("",'E');
		if (!vldDATA())
			return;						
		try
		{	
			inlTBLEDIT(tblHWDTL);
			inlTBLEDIT(tblADDTL);			
			inlTBLEDIT(tblFRUDT);
			inlTBLEDIT(tblLOCTN);
			if(rdbMAC.isSelected())//||(JTabbedPane.getSelectedIndex(0||1||2)))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;									
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{
					for(int i=0;i<tblHWDTL.getRowCount();i++)
					{					
						if(tblHWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
						{					
							if(i >= intROWCT)
							{
								M_strSQLQRY ="INSERT INTO HW_MCMST(MC_STSFL,MC_MCHCT,MC_MATCD,MC_ACTRF,MC_SRLNO,MC_MAKDS,MC_MDLNO,MC_KYBCD,MC_MOUCD,MC_MACNM,MC_CPUDS,MC_RAMDS,MC_HDDDS,MC_CDRDS,MC_LOCCD,";
								M_strSQLQRY +="MC_VENDS,MC_PORNO,MC_PORDT,MC_PORVL,MC_INSDT,MC_EXPDT,MC_MONDS,MC_OSNAM,MC_FRUMB,";
								M_strSQLQRY +="MC_FRUHD,MC_FRUPS,MC_FRUFD,MC_PRTPS,MC_PRTHD,MC_REMDS,MC_LUSBY,MC_LUPDT,MC_SYSTP,MC_STRTP,MC_LICTP,MC_LICNO,MC_GRNRF,MC_INSRM,MC_HRCRF,MC_SFCRF,MC_CRDBY,MC_SBSCD)Values(";
								if(tblHWDTL.getValueAt(i,TB1_STSFL).toString().trim().equals("true"))
									M_strSQLQRY += "'1',";
								else
									M_strSQLQRY += "'0',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_MCHCT).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_MATCD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_ACTRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_SRLNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_MAKDS).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_MDLNO).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_KYBCD).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_MOUCD).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_MACNM).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_CPUDS).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_RAMDS).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_HDDDS).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_CDRDS).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'" + tblHWDTL.getValueAt(i,TB1_LOCCD).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_VENDS).toString().trim()+"',";	
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_PORNO).toString().trim().toUpperCase()+"',";								
								if(tblADDTL.getValueAt(i,TB2_PORDT).toString().trim().length() >0)
									M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_PORDT).toString().trim()))+"',";
								else
									M_strSQLQRY += null +",";
								if(tblADDTL.getValueAt(i,TB2_PORVL).toString().trim().length() >0)
									M_strSQLQRY +=""+ tblADDTL.getValueAt(i,TB2_PORVL)+",";	
								else
									M_strSQLQRY += null +",";
								if(tblADDTL.getValueAt(i,TB2_INSDT).toString().trim().length() >0)
									M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_INSDT).toString().trim()))+"',";
								else
									M_strSQLQRY += null +",";
								if(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim().length() >0)
									M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim()))+"',";
								else
									M_strSQLQRY += null +",";
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_MONDS).toString().trim()+"',";	
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_OSNAM).toString().trim()+"',";	
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_FRUMB).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_FRUHD).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_FRUPS).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_FRUFD).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_PRTPS).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_PRTHD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'"+tblFRUDT.getValueAt(i,TB3_REMDS).toString().trim()+"',";	
								M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
								M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
								M_strSQLQRY += "'" +txtMAINL.getText().trim()+"',";							
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_STRTP).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_LICTP).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_LICNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_GRNRF).toString().trim().toUpperCase()+"',";
								
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_INSRM).toString().trim()+"',";
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_HRCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_SFCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_CRDBY).toString().trim().toUpperCase()+"',";

								M_strSQLQRY += "'" + M_strSBSCD+"')";									
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
							else					
							{
								M_strSQLQRY ="UPDATE HW_MCMST SET ";
								if(tblHWDTL.getValueAt(i,TB1_STSFL).toString().trim().equals("true"))
									M_strSQLQRY +=" MC_STSFL = '1',";
								else
									M_strSQLQRY +=" MC_STSFL = '0',";
								M_strSQLQRY += "MC_MATCD = " + "'" + tblHWDTL.getValueAt(i,TB1_MATCD).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_ACTRF = " + "'" + tblHWDTL.getValueAt(i,TB1_ACTRF).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_MAKDS = " + "'" + tblHWDTL.getValueAt(i,TB1_MAKDS).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_MDLNO = " + "'" + tblHWDTL.getValueAt(i,TB1_MDLNO).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_KYBCD = " + "'" + tblHWDTL.getValueAt(i,TB1_KYBCD).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_MOUCD = " + "'" + tblHWDTL.getValueAt(i,TB1_MOUCD).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_MACNM = " + "'" + tblHWDTL.getValueAt(i,TB1_MACNM).toString().trim().toUpperCase() +"',";	
								M_strSQLQRY += "MC_CPUDS = " + "'" + tblHWDTL.getValueAt(i,TB1_CPUDS).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_RAMDS = " + "'" + tblHWDTL.getValueAt(i,TB1_RAMDS).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_HDDDS = " + "'" + tblHWDTL.getValueAt(i,TB1_HDDDS).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_CDRDS = " + "'" + tblHWDTL.getValueAt(i,TB1_CDRDS).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_LOCCD = " + "'" + tblHWDTL.getValueAt(i,TB1_LOCCD).toString().trim().toUpperCase() +"',";							
								M_strSQLQRY += "MC_VENDS = " + "'" + tblADDTL.getValueAt(i,TB2_VENDS).toString().trim() +"',";
								M_strSQLQRY += "MC_PORNO = " + "'" + tblADDTL.getValueAt(i,TB2_PORNO).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_LICTP = " + "'" + tblADDTL.getValueAt(i,TB2_LICTP).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_LICNO = " + "'" + tblADDTL.getValueAt(i,TB2_LICNO).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_GRNRF = " + "'" + tblADDTL.getValueAt(i,TB2_GRNRF).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_STRTP = " + "'" + tblADDTL.getValueAt(i,TB2_STRTP).toString().trim() +"',";
								if(tblADDTL.getValueAt(i,TB2_PORDT).toString().trim().length() >0)							
									M_strSQLQRY +="MC_PORDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_PORDT).toString().trim()))+"',";							
								M_strSQLQRY +="MC_PORVL = "+tblADDTL.getValueAt(i,TB2_PORVL)+",";
								if(tblADDTL.getValueAt(i,TB2_INSDT).toString().trim().length() >0)							
									M_strSQLQRY +="MC_INSDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_INSDT).toString().trim()))+"',";							
								if(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim().length() >0)							
									M_strSQLQRY +="MC_EXPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim()))+"',";							
								M_strSQLQRY += "MC_MONDS = '" + tblFRUDT.getValueAt(i,TB3_MONDS).toString().trim() +"',";
								M_strSQLQRY += "MC_OSNAM = '" + tblFRUDT.getValueAt(i,TB3_OSNAM).toString().trim() +"',";
								M_strSQLQRY += "MC_FRUMB = '" + tblFRUDT.getValueAt(i,TB3_FRUMB).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_FRUHD = '" + tblFRUDT.getValueAt(i,TB3_FRUHD).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_FRUPS = '" + tblFRUDT.getValueAt(i,TB3_FRUPS).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_FRUFD = '" + tblFRUDT.getValueAt(i,TB3_FRUFD).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_PRTPS = '" + tblFRUDT.getValueAt(i,TB3_PRTPS).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_PRTHD = '" + tblFRUDT.getValueAt(i,TB3_PRTHD).toString().trim().toUpperCase() +"',";
								M_strSQLQRY += "MC_REMDS = '" + tblFRUDT.getValueAt(i,TB3_REMDS).toString().trim() +"',";
								M_strSQLQRY += "MC_LUSBY = '" + cl_dat.M_strUSRCD_pbst+"',";
								M_strSQLQRY += "MC_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";

								M_strSQLQRY += "MC_INSRM = '" + tblADDTL.getValueAt(i,TB2_INSRM).toString().trim()+"',";
								M_strSQLQRY += "MC_HRCRF = '" + tblADDTL.getValueAt(i,TB2_HRCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "MC_SFCRF = '" + tblADDTL.getValueAt(i,TB2_SFCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "MC_CRDBY = '" + tblADDTL.getValueAt(i,TB2_CRDBY).toString().trim().toUpperCase()+"',";

								M_strSQLQRY += "MC_SBSCD = '" + M_strSBSCD+"'";
								M_strSQLQRY += " WHERE MC_SYSTP = '"+txtMAINL.getText().trim()+"' and MC_MCHCT = " + "'" + tblHWDTL.getValueAt(i,TB1_MCHCT).toString().trim()+"'  and MC_SBSCD in "+M_strSBSLS;
								M_strSQLQRY += " and  MC_SRLNO = " + "'" + tblHWDTL.getValueAt(i,TB1_SRLNO).toString().trim()+"'";	
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
								System.out.println("M_strSQLQRY"+M_strSQLQRY);
							 }
							this.setCursor(cl_dat.M_curDFSTS_pbst);
						}
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);						
					for(int i=0;i<tblHWDTL.getRowCount();i++)
					{
						if(tblHWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
						{
							M_strSQLQRY =" UPDATE HW_MCMST SET ";
							M_strSQLQRY +="MC_LUSBY = "+"'"+cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY +="MC_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
							M_strSQLQRY += "MC_STSFL = 'X'";
							M_strSQLQRY += " WHERE MC_SYSTP = '"+txtMAINL.getText().trim()+"' and MC_MCHCT = "+"'"+tblHWDTL.getValueAt(i,TB1_MCHCT).toString().trim()+"'  and MC_SBSCD in "+M_strSBSLS;
							M_strSQLQRY += " and  MC_SRLNO = "+"'"+tblHWDTL.getValueAt(i,TB1_SRLNO).toString().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
						}
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if(rdbLOC.isSelected())
			{
				cl_dat.M_flgLCUPD_pbst = true;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);					
					for(int i=0;i<tblLOCTN.getRowCount();i++)
					{
						if(tblLOCTN.getValueAt(i,TB5_CHKFL).toString().trim().equals("true"))
						{
							if(i >= intROWCT)
							{
								M_strSQLQRY ="INSERT INTO HW_LCMST(LC_LOCCD,LC_LOCDS,LC_ADR01,LC_DPTCD,LC_HODNM,LC_IPADD,LC_SYSTP,LC_SWTNO,LC_LUSBY,LC_LUPDT,LC_SBSCD) values(";
								M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_LOCCD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_LOCDS).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_ADR01).toString().trim()+"',";
								M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_DPTCD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_HODNM).toString().trim()+"',";
								M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_IPADD).toString().trim().toUpperCase()+"',";
								//M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_SYSTP).toString().trim()+"',";
								M_strSQLQRY += "'"+txtMAINL.getText().trim()+"',";
								M_strSQLQRY +="'"+	tblLOCTN.getValueAt(i,TB5_SWTNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	cl_dat.M_strUSRCD_pbst+"',";
								M_strSQLQRY +="'"+	M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
								M_strSQLQRY +="'"+	M_strSBSCD+"')";
							}
							else
							{								
								M_strSQLQRY =" UPDATE HW_LCMST SET ";
								M_strSQLQRY += "LC_LOCDS = '" + tblLOCTN.getValueAt(i,TB5_LOCDS).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "LC_ADR01 = '" + tblLOCTN.getValueAt(i,TB5_ADR01).toString().trim()+"',";	
								M_strSQLQRY += "LC_DPTCD = '" + tblLOCTN.getValueAt(i,TB5_DPTCD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "LC_HODNM = '" + tblLOCTN.getValueAt(i,TB5_HODNM).toString().trim()+"',";
								M_strSQLQRY += "LC_IPADD = '" + tblLOCTN.getValueAt(i,TB5_IPADD).toString().trim().toUpperCase()+"',";
								//M_strSQLQRY += "LC_SYSTP = '" + tblLOCTN.getValueAt(i,TB5_SYSTP).toString().trim()+"',";
								M_strSQLQRY += "LC_SWTNO = '" + tblLOCTN.getValueAt(i,TB5_SWTNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "LC_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
								M_strSQLQRY += "LC_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
								M_strSQLQRY += "LC_SBSCD = '"+M_strSBSCD+"'";
								M_strSQLQRY += "  WHERE LC_SYSTP ='"+txtMAINL.getText().trim()+"' and LC_LOCCD = '"+tblLOCTN.getValueAt(i,TB5_LOCCD).toString().trim()+"'  and LC_SBSCD in "+M_strSBSLS;
								System.out.println("update>>>>"+M_strSQLQRY);
							}							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
					}					
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					 this.setCursor(cl_dat.M_curWTSTS_pbst);
					 for(int i=0;i<tblLOCTN.getRowCount();i++)
					 {
						if(tblLOCTN.getValueAt(i,TB5_CHKFL).toString().trim().equals("true"))
						{
							//M_strSQLQRY ="Delete from HW_LCMST";
							M_strSQLQRY ="UPDATE HW_LCMST SET ";
							M_strSQLQRY += "LC_STSFL = 'X',";
							M_strSQLQRY +=" LC_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY +=" LC_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
							M_strSQLQRY += " WHERE LC_SYSTP ='"+txtMAINL.getText().trim()+"' and LC_LOCCD = '"+tblLOCTN.getValueAt(i,TB5_LOCCD).toString().trim()+"'  and LC_SBSCD in "+M_strSBSLS;						
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(rdbPRF.isSelected())// for peripheral master................
			{
				inlTBLEDIT(tblPRDTL);
				cl_dat.M_flgLCUPD_pbst = true;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);				
					for(int i=0;i<tblPRDTL.getRowCount();i++)
					{
						if(tblPRDTL.getValueAt(i,TB4_CHKFL).toString().trim().equals("true"))
						{
							if(i >= intROWCT)
							{
								M_strSQLQRY ="INSERT INTO HW_PRMST(PR_STSFL,PR_MDLNO,PR_PRFCT,PR_SRLNO,PR_LOCCD,PR_PRFDS,PR_MATCD,PR_ACTRF,PR_SYSTP,PR_VENDS,";
								M_strSQLQRY +="PR_PORNO,PR_PORDT,PR_PORVL,PR_INSDT,PR_EXPDT,PR_STRTP,PR_GRNNO,PR_LUSBY,PR_LUPDT,PR_INSRM,PR_HRCRF,PR_SFCRF,PR_CRDBY,PR_SBSCD) values(";
								if(tblPRDTL.getValueAt(i,TB4_STSFL).toString().trim().equals("true"))
									M_strSQLQRY += "'1',";
								else
									M_strSQLQRY += "'0',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_MDLNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_PRFCT).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_SRLNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_LOCCD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_PRFDS).toString().trim()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_MATCD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_ACTRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+  txtMAINL.getText().trim()+"',";								
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_VENDS).toString().trim()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_PORNO).toString().trim().toUpperCase()+"',";
								if(tblPRDTL.getValueAt(i,TB4_PORDT).toString().trim().length() >0)
									M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblPRDTL.getValueAt(i,TB4_PORDT).toString().trim()))+"',";
								else
									M_strSQLQRY += null +",";
								if(tblPRDTL.getValueAt(i,TB4_PORVL).toString().trim().length() >0)
									M_strSQLQRY +=" "+	tblPRDTL.getValueAt(i,TB4_PORVL).toString().trim().toUpperCase()+" ,";
								else
									M_strSQLQRY += " 0.0 ,";					
								if(tblPRDTL.getValueAt(i,TB4_INSDT).toString().trim().length() >0)
									M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblPRDTL.getValueAt(i,TB4_INSDT).toString().trim()))+"',";
								else
									M_strSQLQRY += null +",";							
								if(tblPRDTL.getValueAt(i,TB4_EXPDT).toString().trim().length() >0)
									M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblPRDTL.getValueAt(i,TB4_EXPDT).toString().trim()))+"',";
								else
									M_strSQLQRY += null +",";							
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_STRTP).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	tblPRDTL.getValueAt(i,TB4_GRNNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	cl_dat.M_strUSRCD_pbst+"',";
								M_strSQLQRY +="'"+	M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
								M_strSQLQRY += "'" + tblPRDTL.getValueAt(i,TB4_INSRM).toString().trim()+"',";
								M_strSQLQRY += "'" + tblPRDTL.getValueAt(i,TB4_HRCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblPRDTL.getValueAt(i,TB4_SFCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "'" + tblPRDTL.getValueAt(i,TB4_CRDBY).toString().trim().toUpperCase()+"',";
								M_strSQLQRY +="'"+	M_strSBSCD+"')";
							}
							else
							{
								M_strSQLQRY =" UPDATE HW_PRMST SET ";
								if(tblPRDTL.getValueAt(i,TB4_STSFL).toString().trim().equals("true"))
									M_strSQLQRY +=" PR_STSFL = '1',";
								else
									M_strSQLQRY +=" PR_STSFL = '0',";
								M_strSQLQRY += "PR_MDLNO = '" +	tblPRDTL.getValueAt(i,TB4_MDLNO).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "PR_PRFDS = '" + tblPRDTL.getValueAt(i,TB4_PRFDS).toString().trim()+"',";
								M_strSQLQRY += "PR_MATCD = '" + tblPRDTL.getValueAt(i,TB4_MATCD).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "PR_LOCCD = '" + tblPRDTL.getValueAt(i,TB4_LOCCD).toString().trim().toUpperCase()+"',";	
								M_strSQLQRY += "PR_ACTRF = '" + tblPRDTL.getValueAt(i,TB4_ACTRF).toString().trim().toUpperCase()+"',";								
								M_strSQLQRY += "PR_VENDS = '" + tblPRDTL.getValueAt(i,TB4_VENDS).toString().trim()+"',";
								M_strSQLQRY += "PR_PORNO = '" + tblPRDTL.getValueAt(i,TB4_PORNO).toString().trim().toUpperCase()+"',";
								if(tblPRDTL.getValueAt(i,TB4_PORVL).toString().trim().length() >0)
									M_strSQLQRY += "PR_PORVL =  " + tblPRDTL.getValueAt(i,TB4_PORVL).toString().trim().toUpperCase()+" ,";
								else
									M_strSQLQRY += "PR_PORVL = 0.0 ,";									
								M_strSQLQRY += "PR_STRTP = '" + tblPRDTL.getValueAt(i,TB4_STRTP).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "PR_GRNNO = '" + tblPRDTL.getValueAt(i,TB4_GRNNO).toString().trim().toUpperCase()+"',";
								if(tblPRDTL.getValueAt(i,TB4_PORDT).toString().trim().length() >0)								
									M_strSQLQRY +="PR_PORDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(tblPRDTL.getValueAt(i,TB4_PORDT).toString().trim()))+"',";								
								if(tblPRDTL.getValueAt(i,TB4_INSDT).toString().trim().length() >0)								
									M_strSQLQRY +="PR_INSDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(tblPRDTL.getValueAt(i,TB4_INSDT).toString().trim()))+"',";								
								if(tblPRDTL.getValueAt(i,TB4_EXPDT).toString().trim().length() >0)								
									M_strSQLQRY +="PR_EXPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(tblPRDTL.getValueAt(i,TB4_EXPDT).toString().trim()))+"',";								
								M_strSQLQRY += "PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
								M_strSQLQRY += "PR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";

								M_strSQLQRY += "PR_INSRM = '" + tblPRDTL.getValueAt(i,TB4_INSRM).toString().trim()+"',";
								M_strSQLQRY += "PR_HRCRF = '" + tblPRDTL.getValueAt(i,TB4_HRCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "PR_SFCRF = '" + tblPRDTL.getValueAt(i,TB4_SFCRF).toString().trim().toUpperCase()+"',";
								M_strSQLQRY += "PR_CRDBY = '" + tblPRDTL.getValueAt(i,TB4_CRDBY).toString().trim().toUpperCase()+"',";

								M_strSQLQRY += "PR_SBSCD= '"+M_strSBSCD+"'";
								M_strSQLQRY += "  WHERE PR_SYSTP ='"+txtMAINL.getText().trim()+"' and PR_SRLNO = '"+tblPRDTL.getValueAt(i,TB4_SRLNO).toString().trim()+"' and PR_SBSCD in "+M_strSBSLS;
								M_strSQLQRY += " and PR_PRFCT = '" + tblPRDTL.getValueAt(i,TB4_PRFCT).toString().trim()+"'";
								System.out.println("update>>>>"+M_strSQLQRY);
							}							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");							
						}
					}									
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					 this.setCursor(cl_dat.M_curWTSTS_pbst);
					 for(int i=0;i<tblPRDTL.getRowCount();i++)
					 {
						if(tblPRDTL.getValueAt(i,TB4_CHKFL).toString().trim().equals("true"))
						{
							//M_strSQLQRY ="Delete from HW_PRMST";
							M_strSQLQRY ="UPDATE HW_PRMST SET ";
							M_strSQLQRY += "PR_STSFL = 'X',";
							M_strSQLQRY +=" PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY +=" PR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
							M_strSQLQRY += " WHERE PR_SYSTP ='"+txtMAINL.getText().trim()+"' and PR_SRLNO = '"+tblPRDTL.getValueAt(i,TB4_SRLNO).toString().trim()+"'  and PR_SBSCD in "+M_strSBSLS;
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
					}					
				}				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}	
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
			 	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			 		setMSG(" Data Saved Successfully..",'N'); 
			 	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			 		setMSG(" Data Modified Successfully..",'N'); 
			 	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			 		setMSG("Data Deleted Successsfully ..",'N');
			 	clrCOMP();
			}
			else
			{
			 	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			 		setMSG("Error in saving details..",'E'); 
			 	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			 	   setMSG("Error in Modified Data details..",'E'); 
			 	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			 		setMSG("Error in Deleting data..",'E');
			}	                                                            		 	                                          
		}
		catch(Exception L_SE)
		{
		 	setMSG(L_SE,"exeSAVE");
		}	
	}
	/**
	 * Method to fetch data from the data base & to display it in the corresponding JTable.
	 */
	private void getDATA()
	{        
		try  
		{    
			intROWCT = 0;
			if(rdbMAC.isSelected())
			{
				int L_ROWNO = 0;
				String L_strTEMP="";
				java.sql.Date L_tmpDATE;
				if(txtLOC.getText().trim().length()>0)
					txtLOC.setText(txtLOC.getText().trim().toUpperCase());				
				M_strSQLQRY ="Select * from HW_MCMST ";
				M_strSQLQRY+= "where isnull(MC_STSFL,' ') <> 'X' and MC_SYSTP ='"+txtMAINL.getText().trim()+"' and MC_SBSCD in "+M_strSBSLS;
				if(txtLOC.getText().trim().length()>0)
				 	M_strSQLQRY += " and MC_LOCCD ='"+txtLOC.getText().trim().toUpperCase()+"'";
				M_strSQLQRY += " order by MC_LOCCD";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
						
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_SRLNO"),""),L_ROWNO,TB1_SRLNO);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MAKDS"),""),L_ROWNO,TB1_MAKDS);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MDLNO"),""),L_ROWNO,TB1_MDLNO);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_KYBCD"),""),L_ROWNO,TB1_KYBCD);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MOUCD"),""),L_ROWNO,TB1_MOUCD);
						tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MCHCT"),""),L_ROWNO,TB1_MCHCT);
						tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MATCD"),""),L_ROWNO,TB1_MATCD);
						tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_ACTRF"),""),L_ROWNO,TB1_ACTRF);
						tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MACNM"),""),L_ROWNO,TB1_MACNM);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_CPUDS"),""),L_ROWNO,TB1_CPUDS);
						tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_RAMDS"),""),L_ROWNO,TB1_RAMDS);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_HDDDS"),""),L_ROWNO,TB1_HDDDS);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_CDRDS"),""),L_ROWNO,TB1_CDRDS);
					 	tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_LOCCD"),""),L_ROWNO,TB1_LOCCD);
						//tblHWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_SYSTP"),""),L_ROWNO,TB1_SYSTP);
						
					 	tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MACNM"),""),L_ROWNO,TB2_MACNM);
					 	tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_VENDS"),""),L_ROWNO,TB2_VENDS);
					 	tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_PORNO"),""),L_ROWNO,TB2_PORNO);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_STRTP"),""),L_ROWNO,TB2_STRTP);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_LICTP"),""),L_ROWNO,TB2_LICTP);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_LICNO"),""),L_ROWNO,TB2_LICNO);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_GRNRF"),""),L_ROWNO,TB2_GRNRF);

						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_INSRM"),""),L_ROWNO,TB2_INSRM);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_HRCRF"),""),L_ROWNO,TB2_HRCRF);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_SFCRF"),""),L_ROWNO,TB2_SFCRF);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_CRDBY"),""),L_ROWNO,TB2_CRDBY);
						
						tblADDTL.setValueAt(M_rstRSSET.getDate("MC_PORDT")!=null?M_fmtLCDAT.format(M_rstRSSET.getDate("MC_PORDT")):"",L_ROWNO,TB2_PORDT);
						tblADDTL.setValueAt(M_rstRSSET.getDate("MC_INSDT")!=null?M_fmtLCDAT.format(M_rstRSSET.getDate("MC_INSDT")):"",L_ROWNO,TB2_INSDT);
						tblADDTL.setValueAt(M_rstRSSET.getDate("MC_EXPDT")!=null?M_fmtLCDAT.format(M_rstRSSET.getDate("MC_EXPDT")):"",L_ROWNO,TB2_EXPDT);
						tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_PORVL"),"0"),L_ROWNO,TB2_PORVL);		
						/*L_tmpDATE = M_rstRSSET.getDate("MC_PORDT");
					 	L_strTEMP="";
					 		if (L_tmpDATE != null)
								L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
					 		tblADDTL.setValueAt(L_strTEMP,L_ROWNO,TB2_PORDT);
						 	
					 	L_tmpDATE = M_rstRSSET.getDate("MC_INSDT");
					 	L_strTEMP="";
							if (L_tmpDATE != null)
								L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
					 		tblADDTL.setValueAt(L_strTEMP,L_ROWNO,TB2_INSDT);
						 	
						L_tmpDATE = M_rstRSSET.getDate("MC_EXPDT");
						L_strTEMP="";
							if (L_tmpDATE != null)
								L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						tblADDTL.setValueAt(L_strTEMP,L_ROWNO,TB2_EXPDT);*/
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MACNM"),""),L_ROWNO,TB3_MACNM);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_MONDS"),""),L_ROWNO,TB3_MONDS);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_OSNAM"),""),L_ROWNO,TB3_OSNAM);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_FRUMB"),""),L_ROWNO,TB3_FRUMB);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_FRUHD"),""),L_ROWNO,TB3_FRUHD);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_FRUPS"),""),L_ROWNO,TB3_FRUPS);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_FRUFD"),""),L_ROWNO,TB3_FRUFD);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_PRTPS"),""),L_ROWNO,TB3_PRTPS);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_PRTHD"),""),L_ROWNO,TB3_PRTHD);
						tblFRUDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MC_REMDS"),""),L_ROWNO,TB3_REMDS);
						
						if(nvlSTRVL(M_rstRSSET.getString("MC_STSFL"),"").equals("1"))
							tblHWDTL.setValueAt(new Boolean(true),L_ROWNO,TB1_STSFL);
						//else 
						//	tblHWDTL.setValueAt(new Boolean(false),L_ROWNO,TB1_STSFL);
						L_ROWNO ++;
					}
					intROWCT = L_ROWNO;
					M_rstRSSET.close();
				}
			}
			if(rdbPRF.isSelected())
			{
				int L_ROWNO1 = 0;
				//String L_strTEMP="";
				//java.sql.Date L_tmpDATE;
				M_strSQLQRY ="Select PR_STSFL,PR_SRLNO,PR_PRFDS,PR_LOCCD,PR_PRFCT,PR_MATCD,PR_ACTRF,PR_MDLNO,PR_VENDS,PR_PORNO,PR_INSRM,PR_CRDBY,PR_PORVL,CONVERT(varchar,PR_PORDT,103) PR_PORDT,CONVERT(varchar,PR_INSDT,103) PR_INSDT,CONVERT(varchar,PR_EXPDT,103) PR_EXPDT,PR_STRTP,PR_GRNNO from HW_PRMST where PR_SYSTP = '"+txtMAINL.getText().trim()+"' and isnull(PR_STSFL,' ') <> 'X'  and PR_SBSCD in "+M_strSBSLS;								
				if(txtLOC.getText().trim().length()>0)									
					M_strSQLQRY += " and PR_LOCCD = '"+txtLOC.getText().trim().toUpperCase()+"'";	
				M_strSQLQRY += " order by PR_PRFCT";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
						/*if(nvlSTRVL(M_rstRSSET.getString("PR_STSFL"),"").equals("1"))
							tblPRDTL.setValueAt(new Boolean(true),L_ROWNO1,TB4_STSFL);
						else
							tblPRDTL.setValueAt(new Boolean(false),L_ROWNO1,TB4_STSFL);*/
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_STSFL"),"").equals("1")?(new Boolean(true)):new Boolean(false),L_ROWNO1,TB4_STSFL);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_SRLNO"),""),L_ROWNO1,TB4_SRLNO);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PRFDS"),""),L_ROWNO1,TB4_PRFDS);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_LOCCD"),""),L_ROWNO1,TB4_LOCCD);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PRFCT"),""),L_ROWNO1,TB4_PRFCT);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_MATCD"),""),L_ROWNO1,TB4_MATCD);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_ACTRF"),""),L_ROWNO1,TB4_ACTRF);
						//tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_SYSTP"),""),L_ROWNO1,TB4_SYSTP);
						//tblPRDTL.getValueAt(i,TB4_MDLNO)
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_MDLNO"),""),L_ROWNO1,TB4_MDLNO);						
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_VENDS"),""),L_ROWNO1,TB4_VENDS);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PORNO"),""),L_ROWNO1,TB4_PORNO);
				
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_INSRM"),""),L_ROWNO1,TB4_INSRM);
						//tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_HRCRF"),""),L_ROWNO1,TB4_HRCRF);
						//tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_SFCRF"),""),L_ROWNO1,TB4_SFCRF);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_CRDBY"),""),L_ROWNO1,TB4_CRDBY);
						
						tblPRDTL.setValueAt(M_rstRSSET.getDate("PR_PORDT")!=null?M_fmtLCDAT.format(M_rstRSSET.getDate("PR_PORDT")):"",L_ROWNO1,TB4_PORDT);
						tblPRDTL.setValueAt(M_rstRSSET.getDate("PR_INSDT")!=null?M_fmtLCDAT.format(M_rstRSSET.getDate("PR_INSDT")):"",L_ROWNO1,TB4_INSDT);
						
						tblPRDTL.setValueAt(M_rstRSSET.getDate("PR_EXPDT")!=null?M_fmtLCDAT.format(M_rstRSSET.getDate("PR_EXPDT")):"",L_ROWNO1,TB4_EXPDT);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PORVL"),""),L_ROWNO1,TB4_PORVL);
						
						/*	L_tmpDATE = M_rstRSSET.getDate("PR_PORDT");
			 			L_strTEMP="";
			 				if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						tblPRDTL.setValueAt(L_strTEMP,L_ROWNO1,TB4_PORDT);
						
						L_tmpDATE = M_rstRSSET.getDate("PR_INSDT");
			 			L_strTEMP="";
			 				if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						tblPRDTL.setValueAt(L_strTEMP,L_ROWNO1,TB4_INSDT);
			 			
			 			L_tmpDATE = M_rstRSSET.getDate("PR_EXPDT");
			 			L_strTEMP="";
			 				if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						tblPRDTL.setValueAt(L_strTEMP,L_ROWNO1,TB4_EXPDT);*/
			 			
			 			tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_STRTP"),""),L_ROWNO1,TB4_STRTP);
						tblPRDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_GRNNO"),""),L_ROWNO1,TB4_GRNNO);

						L_ROWNO1 ++;
					}
					intROWCT = L_ROWNO1;
					M_rstRSSET.close();
				}
		     }
			 if(rdbLOC.isSelected())
			 {
			 	int L_ROWNO2 = 0;
			 	M_strSQLQRY ="Select * from HW_LCMST where  isnull(LC_STSFL,' ') <> 'X' and LC_SYSTP ='" +txtMAINL.getText().trim()+"'  and LC_SBSCD in "+M_strSBSLS;
				if(txtLOC.getText().trim().length()>0)
					 M_strSQLQRY += " and LC_LOCCD ='"+txtLOC.getText().trim().toUpperCase()+"'";
				 //M_strSQLQRY ="Select LC_LOCCD,LC_LOCDS,LC_IPADD,LC_ADR01,LC_HODNM,LC_DPTCD  from HW_LCMST where  isnull(LC_STSFL,' ') <> 'X' ";
				 System.out.println(M_strSQLQRY);
			 	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			 	if(M_rstRSSET !=null)
			 	{
			 		while(M_rstRSSET.next())
			 		{
			 			tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_LOCCD"),""),L_ROWNO2,TB5_LOCCD);
			 			tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_LOCDS"),""),L_ROWNO2,TB5_LOCDS);
			 			tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_ADR01"),""),L_ROWNO2,TB5_ADR01);
			 			tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_DPTCD"),""),L_ROWNO2,TB5_DPTCD);
			 			tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_HODNM"),""),L_ROWNO2,TB5_HODNM);
			 			tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_IPADD"),""),L_ROWNO2,TB5_IPADD);
			 			//tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_SYSTP"),""),L_ROWNO1,TB5_SYSTP);
			 			tblLOCTN.setValueAt(nvlSTRVL(M_rstRSSET.getString("LC_SWTNO"),""),L_ROWNO2,TB5_SWTNO);
			 			L_ROWNO2 ++;
			 		}
					intROWCT = L_ROWNO2;
			 		M_rstRSSET.close();
			 	}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
		}
	}	
	/**
	 * Method to Stop the CellEditor. As Data in the Cell is not Saved if it is Selected.
	 */
	private void inlTBLEDIT(JTable P_tblTBLNM)
	{
		if(P_tblTBLNM.isEditing())
			P_tblTBLNM.getCellEditor().stopCellEditing();
		P_tblTBLNM.setRowSelectionInterval(0,0);
		P_tblTBLNM.setColumnSelectionInterval(0,0);			
	}
	/**
	 * Method to validate the data before the execuation of the SQL Queries.
	 */
	boolean vldDATA()
	{
		try
		{
			inlTBLEDIT(tblHWDTL);			
			inlTBLEDIT(tblPRDTL);
			inlTBLEDIT(tblLOCTN);
			for(int i=0;i<intROWCNT;i++)
			{
				if(tblADDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					tblHWDTL.setValueAt(tblADDTL.getValueAt(i,TB1_CHKFL),i,TB2_CHKFL);
					//tblADDTL.setValueAt(tblHWDTL.getValueAt(i,TB1_MACNM),i,TB2_MACNM);
					//tblFRUDT.setValueAt(tblHWDTL.getValueAt(i,TB1_CHKFL),i,TB3_CHKFL);
					//tblFRUDT.setValueAt(tblHWDTL.getValueAt(i,TB1_MACNM),i,TB3_MACNM);
					continue;
				}
				else if(tblFRUDT.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					tblHWDTL.setValueAt(tblFRUDT.getValueAt(i,TB1_CHKFL),i,TB2_CHKFL);
					//tblADDTL.setValueAt(tblHWDTL.getValueAt(i,TB1_MACNM),i,TB2_MACNM);
					//tblFRUDT.setValueAt(tblHWDTL.getValueAt(i,TB1_CHKFL),i,TB3_CHKFL);
					//tblFRUDT.setValueAt(tblHWDTL.getValueAt(i,TB1_MACNM),i,TB3_MACNM);
				}
			}	
			
			String strTEMP ="";;			
			int j = 0;
			int L_intRECCT = 0;
			if(txtMAINL.getText().trim().length() == 0)
			{
				setMSG("System Type cannot be Blank, Press F1 to select from List..",'E');
				return false;
			}			
			boolean L_flgCHKFL= false;
			for(int i=0; i<tblHWDTL.getRowCount(); i++)
			{				
				if (tblHWDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{	
					L_flgCHKFL= true;
					break;
				}		
			}	
			for(int i=0; i<tblADDTL.getRowCount(); i++)
			{				
				if (tblADDTL.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{				
					L_flgCHKFL= true;
					break;
				}
			}
			for(int i=0; i<tblPRDTL.getRowCount(); i++)
			{				
				if (tblPRDTL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
				{				
					L_flgCHKFL= true;
					break;
				}	
			}	
			for(int i=0; i<tblLOCTN.getRowCount(); i++)
			{				
				if (tblLOCTN.getValueAt(i,TB5_CHKFL).toString().equals("true"))
				{				
					L_flgCHKFL= true;
					break;
				}	
			}	
			if (L_flgCHKFL== false)
			{
				setMSG("No row Selected..",'E');				
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{				
				if(rdbMAC.isSelected())
				{
					L_intRECCT = 0;
					j = 0;
					for(int i=0; i<tblHWDTL.getRowCount(); i++)
					{
						if (tblHWDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
						{
					   		if(tblHWDTL.getValueAt(i,TB1_SRLNO).toString().length()==0)
					   		{
								setMSG("Hard ware SRLNO isnot Blank..",'E');	
								tblHWDTL.editCellAt(tblHWDTL.getSelectedRow(),TB1_SRLNO);
								tblHWDTL.cmpEDITR[TB1_SRLNO].requestFocus();
								return false;
							}
							if(tblHWDTL.getValueAt(i,TB1_MCHCT).toString().length()==0)
					   		{
								setMSG("Hardware Category Code is not Blank..",'E');
								tblHWDTL.editCellAt(tblHWDTL.getSelectedRow(),TB1_MCHCT);
								tblHWDTL.cmpEDITR[TB1_MCHCT].requestFocus();
								return false;
							}
							// to check duplicate Key Fields
							strTEMP = tblHWDTL.getValueAt(i,TB1_MCHCT).toString().trim();
							strTEMP += tblHWDTL.getValueAt(i,TB1_SRLNO).toString().trim();
							strTEMP = strTEMP.toUpperCase().trim();
							
							L_intRECCT = 0;
							for(j=0; j<tblHWDTL.getRowCount(); j++)
							{
								if((tblHWDTL.getValueAt(j,TB1_MCHCT).toString().trim() + tblHWDTL.getValueAt(j,TB1_SRLNO).toString().trim()).equalsIgnoreCase (strTEMP))
									L_intRECCT++;																
								if(L_intRECCT >1)
								{
									setMSG("Hardware Category & Serial Number already exists, Please try another..",'E');								
									tblHWDTL.editCellAt(i,TB1_SRLNO);
									tblHWDTL.cmpEDITR[TB1_SRLNO].requestFocus();
									return false;
								}
							}							
						///Location Code	
							M_strSQLQRY ="Select LC_LOCCD from HW_LCMST where LC_LOCCD = '"+ tblHWDTL.getValueAt(i,TB1_LOCCD).toString() +"'";							
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							{
								if(!M_rstRSSET.next())
								{
									setMSG("Invalid Location Code",'E');
									tblHWDTL.editCellAt(i,TB1_LOCCD);
									tblHWDTL.cmpEDITR[TB1_LOCCD].requestFocus();
									M_rstRSSET.close();
									return false;
								}
								M_rstRSSET.close();
							}
						}
					}					
				}
				else if(rdbPRF.isSelected())
				{
					for(int i=0; i<tblPRDTL.getRowCount(); i++)
					{
						if (tblPRDTL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
						{
							if(tblPRDTL.getValueAt(i,TB4_SRLNO).toString().length()==0)
							{
								setMSG("Printer SRLNO is Compulsory field",'E');	
								tblPRDTL.editCellAt(tblPRDTL.getSelectedRow(),TB4_SRLNO);
								tblPRDTL.cmpEDITR[TB4_SRLNO].requestFocus();
								return false;
							}
							if(tblPRDTL.getValueAt(i,TB4_PRFCT).toString().length()==0)
							{
								setMSG("Hardware Category is Compulsory field",'E');	
								tblPRDTL.editCellAt(tblPRDTL.getSelectedRow(),TB4_PRFCT);
								tblPRDTL.cmpEDITR[TB4_PRFCT].requestFocus();
								return false;
							}
							// to check duplicate Key Fields
							strTEMP = tblPRDTL.getValueAt(i,TB4_PRFCT).toString().trim();
							strTEMP += tblPRDTL.getValueAt(i,TB4_SRLNO).toString().trim();
							strTEMP = strTEMP.toUpperCase().trim();
							L_intRECCT = 0;
							for(j=0; j<tblPRDTL.getRowCount(); j++)
							{	
								if((tblPRDTL.getValueAt(j,TB4_PRFCT).toString().trim() + tblPRDTL.getValueAt(j,TB4_SRLNO).toString().trim()).equalsIgnoreCase (strTEMP))
									L_intRECCT++;								
								if(L_intRECCT >1)
								{
									setMSG("Hardware Category & Serial Number already exists, Please try another..",'E');								
									tblPRDTL.editCellAt(i,TB4_SRLNO);
									tblPRDTL.cmpEDITR[TB4_SRLNO].requestFocus();
									return false;
								}
							}
							///Location Code validation
							M_strSQLQRY ="Select LC_LOCCD from HW_LCMST where LC_LOCCD = '"+ tblPRDTL.getValueAt(i,TB4_LOCCD).toString() +"'";							
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							{
								if(!M_rstRSSET.next())
								{
									setMSG("Invalid Location Code",'E');
									tblPRDTL.editCellAt(i,TB4_LOCCD);
									tblPRDTL.cmpEDITR[TB4_LOCCD].requestFocus();
									M_rstRSSET.close();
									return false;
								}
								M_rstRSSET.close();
							}								
						}																		
					}				
				}
				else if(rdbLOC.isSelected())
				{
					L_intRECCT = 0;
					j = 0;
					for(int i=0; i<tblLOCTN.getRowCount(); i++)
					{
						if (tblLOCTN.getValueAt(i,TB5_CHKFL).toString().equals("true"))
						{
							if(tblLOCTN.getValueAt(i,TB5_LOCCD).toString().length()==0)
							{
								setMSG("Location code is Compulsory field",'E');
								tblLOCTN.editCellAt(i,TB5_LOCCD);
								tblLOCTN.cmpEDITR[TB5_LOCCD].requestFocus();
								return false;
							}
							// to check duplicate Key Fields							
							strTEMP = tblLOCTN.getValueAt(i,TB5_LOCCD).toString().trim();
							strTEMP = strTEMP.toUpperCase().trim();							
							L_intRECCT = 0;
							for(j=0; j<tblLOCTN.getRowCount(); j++)
							{									
								if(tblLOCTN.getValueAt(j,TB5_LOCCD).toString().trim().equalsIgnoreCase(strTEMP))
									L_intRECCT++;								
								if(L_intRECCT >1)
								{
									setMSG("Duplicate Location Code, Please try another..",'E');								
									tblLOCTN.editCellAt(i,TB5_LOCCD);
									tblLOCTN.cmpEDITR[TB5_LOCCD].requestFocus();
									return false;
								}
							}							
						}
					}
				}												
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vlddata");
			return false;
		}
		return true;
	}		
}