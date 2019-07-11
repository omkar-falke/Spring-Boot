/*
System Name   : Material Management System
Program Name  : New Product code Entry for Technical use.
Program Desc. : Program to Enter & modify the Product Code used for Technical use.
Author        : Mr.S.R.Mehesare
Date          : 12/11/2005
System        : 
Version       : MMS v2.0.0
Modificaitons : 
Modified By   :
Modified Date :
Modified det. :
Version       :

   Product code Structure
                                                        Quality Flag
                                                  Colour Code      |
                               % Addition level (primary)   |      |
                          Additive/Filler (Secondary) |     |      |
                      Prod.Category (Secondary) |     |     |      |
                    Modified By(Primary)  |     |     |     |      |
              Additive/Filler(Base) |     |     |     |     |      |
          Prod.category(Base) |     |     |     |     |     |      |
   BasePol.Ref.(Main)   |     |     |     |     |     |     |      |
Main Group (51)   |     |     |     |     |     |     |     |      |
            |     |     |     |     |     |     |     |     |      |
           xx xx xx xx xx xx xx xx xx xx xx xx xx xx xx xx xx xxxx x
           12 34 56 78 90 12 34 56 78 90 12 34 56 78 90 12 34 5678 9
               |     |     |     |     |     |     |     |       |
Base Polymer Type    |     |     |     |     |     |     |       |
      BasePol.Ref.Sub-code |     |     |     |     |     |       |
             Modified By(Base)   |     |     |     |     |       |
               Prod.category(Primary)  |     |     |     |       |
                    Additive/Filler(Primary) |     |     |       |
                            Modified By(Secondary) |     |       |
                                % Addition level (Base)  |       |
                                % Addition level (secondary)     |
                                                    Colour Serial No.

																					Table
 MGR  01-02      Main Group                                         "51"			
 PMR  03-04		 Base Polymer Type                                  MST/PRXXPMR		co_cdtrn
 PRM  05-06      Base Polymer Reference (Main Code)					RF/xx__0A/00	pr_cdwrk	xx = PMR
 PRS  07-08      Base Polymer Reference (Sub Code)					RF/xxxx__/00	pr_cdwrk	xxxx = PMR + PRM
 PC0  09-10      Product Category (Base)                            MST/PRXXPCT		co_cdtrn
 PM0  11-12      Modification By									PC/xx__0A/00	pr_cdwrk	xx = PC0
 PA0  13-14      Additive / Filler Description						PC/xxxx__/00	pr_cdwrk	xxxx = PC0 + PM0
 PC1  15-16      Product Category (Primary)							MST/PRXXPCT		co_cdtrn
 PM1  17-18      Modification By									PC/xx__0A/00	pr_cdwrk	xx = PC1
 PA1  19-20      Additive / Filler Description						PC/xxxx__/00	pr_cdwrk	xxxx = PC1 + PM1
 PC2  21-22      Product Category (Secondary)						MST/PRXXPCT		co_cdtrn
 PM2  23-24      Modification By									PC/xx__0A/00	pr_cdwrk	xx = PC2
 PA2  25-26      Additive / Filler Description						PC/xxxx__/00	pr_cdwrk	xxxx = PC2 + PM2
 BSP  27-28      % Addition level (Base)
 PRP  29-30      % Addition level (primary)
 SCP  31-32      % Addition level (secondary)
 PCC  33-34      Colour Code										CS/__000A/00	pr_cdwrk
 PCS  35-38      Colour Serial No.									CS/xx____/00	pr_cdwrk	xx = PCC
 QTY  39         Quality Flag										MST/PRXXQFT		co_cdtrn



	String strMGRCD = "Main Group";
	String strPMRCD = "Base Polymer Type";
	String strPC0CD = "1st Modfn.Type      :";
	String strPM0CD = "..    Modf.By  (I)  :";
	String strPA0CD = "..    Add./Flr (I)  :";
	String strPC1CD = "2nd Modfn.Type      :";
	String strPM1CD = "..    Modf.By  (II) :";
	String strPA1CD = "..    Add./Flr (II) :";
	String strPC2CD = "3rd Modfn.Type      :";
	String strPM2CD = "..    Modf.By  (III):";
	String strPA2CD = "..    Add./Flr (III):";

	String strXXPVL = "..     % Addition Level:";

	String strPCCCD = "..     Colour Code     :";

	String strPRXCD = "..     Base Poly.Ref   :";

	String strQFLCD = "..     Quality Flag";


*/

import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JComponent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.sql.ResultSet;import javax.swing.JTable;import java.awt.event.FocusEvent;
import javax.swing.JList;import javax.swing.JCheckBox;import javax.swing.JScrollPane;import javax.swing.JButton;
import java.awt.Dimension;import javax.swing.InputVerifier;import java.awt.Color;
import java.util.Date;import java.util.Hashtable;import java.util.Vector;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;

/**<PRE>
<B>Program Name : </B> New Product code Entry for Technical use.

<B>Program Desc. :</B> Program to Enter & modify the Product Code used for Technical use.
			
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	 Update	 Query	Delete	
----------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		    Table name		     Type/Size         Description
----------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description             Display Columns      Table Name
----------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------
Program details:- 

Validations :	



Product Code Structure 
 Code Digit      Description
 MGR  01-02      Main Group
 PMR  03-04		 Base Polymer Type
 PRM  05-06      Base Polymer Reference (Main Code)
 PRS  07-08      Base Polymer Reference (Sub Code)
 PC0  09-10      Product Category (Base)
 PM0  11-12      Modification By
 PA0  13-14      Additive / Filler Description
 PC1  15-16      Product Category (Primary)
 PM1  17-18      Modification By
 PA1  19-20      Additive / Filler Description
 PC2  21-22      Product Category (Secondary)
 PM2  23-24      Modification By
 PA2  25-26      Additive / Filler Description
 BSP  27-28      % Addition level (Base)
 PRP  29-30      % Addition level (primary)
 SCP  31-32      % Addition level (secondary)
 PCC  33-34      Colour Code
 PCS  35-38      Colour Serial No.
 QTY  39         Quality Flag




</I>
*/
class co_mepcm extends cl_pbase  
{									/** JTextField to display & to enter Old Product Code.*/
	private JTextField txtOPRCD;	/** JTextField to display & to enter Old Product Description.*/
	private JTextField txtOPRDS;	/** JTextField to display Product Code for Commercial Purpose.*/
	private JTextField txtCPRCD;	/** JTextField to display & to enter Product description for Commercial Purpose.*/
	private JTextField txtCPRDS;	/** JTextField to display Product Code for Technical Purpose.*/
	private JTextField txtCGRDS;	/** JTextField to display Grade description for commercial purpose.*/
	private JTextField txtTPRCD;	/** JTextField to display & to enter Product Code Description for Technical Purpose.*/
	private JTextField txtTPRDS;								

	private JTextField txtBSPCD;
	private JTextField txtPRPCD;
	private JTextField txtSCPCD;

	private JTextField txtPRMCD;
	private JTextField txtPRSCD;	
	
	
	private JTextField txtCSRDS;
	private JTextField txtCODE;  // for 2 digit colour code
	private JTextField txtCODE1; // for 4 digit Code. color Serial Number.
	private JTextField txtCODDS;
	
	private JTextField txtMGRCD;
	private JTextField txtPMRCD;
	private JTextField txtPC0CD;
	private JTextField txtPM0CD;
	private JTextField txtPA0CD;
	private JTextField txtPC1CD;
	private JTextField txtPM1CD;
	private JTextField txtPA1CD;
	private JTextField txtPC2CD;
	private JTextField txtPM2CD;
	private JTextField txtPA2CD;
	private JTextField txtPCCCD;
	private JTextField txtPCSCD;
	private JTextField txtQFLCD;
	

	private int intMGRCD = 2;
	private int intPMRCD = 4;
	private int intPRMCD = 6;
	private int intPRSCD = 8;
	private int intPC0CD = 10;
	private int intPM0CD = 12;
	private int intPA0CD = 14;
	private int intPC1CD = 16;
	private int intPM1CD = 18;
	private int intPA1CD = 20;
	private int intPC2CD = 22;
	private int intPM2CD = 24;
	private int intPA2CD = 26;
	private int intBSPCD = 28;
	private int intPRPCD = 30;
	private int intSCPCD = 32;
	private int intPCCCD = 34;
	private int intPCSCD = 38;
	private int intQFLCD = 39;
	
	
	private TxtLimit txtNEWCD;
	private TxtLimit txtNEWDS;

	private int intTB2_CHKFL = 0;
	private int intTB2_HDRCD1 = 1;
	private int intTB2_HDRDS = 2;
	
	private int intTB3_CHKFL = 0;
	private int intTB3_SUBCD = 1;
	private int intTB3_HDRDS = 2;
	
	private int intADDDTL = 9;			
	private JLabel lblADDDTL;			// Heading (label) for JList displaying Additive details
	private JList lstADDDTL;			// JList for displaying Additive details
	private Vector<String> vtrADDDTL;			// vector for displaying Additive details
	private JCheckBox chkADDDTL;
	

	boolean flgCHK_EXIST = false;
	
	private JComboBox cmbMGRCD;
	private JComboBox cmbPMRCD;
	private JComboBox cmbPC0CD;
	private JComboBox cmbPM0CD;
	private JComboBox cmbPA0CD;
	private JComboBox cmbPC1CD;
	private JComboBox cmbPM1CD;
	private JComboBox cmbPA1CD;
	private JComboBox cmbPC2CD;
	private JComboBox cmbPM2CD;
	private JComboBox cmbPA2CD;
	private JComboBox cmbPCCCD;	
	private JComboBox cmbPCSCD;
	private JComboBox cmbPRMCD;
	private JComboBox cmbPRSCD;
	private JComboBox cmbQFLCD;				/** JPanel to add Components to the JOtionPane*/
	private JPanel pnlOPTPN;	
	private JPanel pnlADDDTL;	
	private JPanel pnlADDCAT;	
	private JPanel pnlADDCOL;	
	private JPanel pnlPRMCD;	
	private JPanel pnlPRSCD;	
	private cl_JTable tblDATA;
	private cl_JTable tblADDDTL;
	private cl_JTable tblADDCAT;
	private cl_JTable tblADDCOL;
	private Vector<String> vtrLSTDL;
	private String strTEMP;
	private String strTPRCD;
	private String strCPRCD;
	private Hashtable<String,String> hstPMRAB;
	private Hashtable<String,String> hstPC0AB;
	private Hashtable<String,String> hstPCCAB;
	//private JCheckBox chkCGRDS;
	private INPVF objINPVR = new INPVF();			
	Dimension dimNEWCMB = new Dimension(400,20);
	Dimension dimORGCMB = new Dimension(180,20);
	Dimension dimORGCMB1 = new Dimension(80,20);

	String strMGRCD = "Main Group";
	String strPMRCD = "Base Polymer Type";
	String strPC0CD = "1st Modfn.Type      :";
	String strPM0CD = "..    Modf.By  (I)  :";
	String strPA0CD = "..    Add./Flr (I)  :";
	String strPC1CD = "2nd Modfn.Type      :";
	String strPM1CD = "..    Modf.By  (II) :";
	String strPA1CD = "..    Add./Flr (II) :";
	String strPC2CD = "3rd Modfn.Type      :";
	String strPM2CD = "..    Modf.By  (III):";
	String strPA2CD = "..    Add./Flr (III):";

	String strXXPVL = "..     % Addition Level:";

	String strPCCCD = "..     Colour Code     :";

	String strPRXCD = "..     Base Poly.Ref   :";

	String strQFLCD = "..     Quality Flag";
	JLabel lblADDCT = new JLabel("");
	JLabel lblPRMDS = new JLabel("");
	JLabel lblPRSDS = new JLabel("");
	JButton btnADDCT = new JButton("Add Cat.");
	String strADDCT = "";

	String strPCSCD = "Colour Serial No.";
	String strPRMCD = "Main Ref.Code";
	String strPRSCD = "Sub Ref.Code";
	
	String strBSPCD = "Base  %";
	String strPRPCD = "Prim. %";
	String strSCPCD = "Secnd %";
	
	co_mepcm()    
	{
		super(1);
		try
		{
			lstADDDTL = new JList();
			vtrADDDTL = new Vector<String>();
			hstPMRAB = new Hashtable<String,String>();
			hstPC0AB = new Hashtable<String,String>();
			hstPCCAB = new Hashtable<String,String>();
			setMatrix(20,12);
			
			String[] L_strTBLHD1 = {"Select","Description","Technical Code"};
			int[] L_intCOLSZ1 = {20,100,250}; 
			tblDATA = crtTBLPNL1(this,L_strTBLHD1,100,5,8,8,5,L_intCOLSZ1,new int[]{0});
			
			add(new JLabel("Code"),1,3,1,2,this,'L');
			add(new JLabel("Description"),1,8,1,5,this,'L');

			add(new JLabel("Technical  :"),2,1,1,2,this,'L');
			add(txtTPRCD = new TxtLimit(intQFLCD),2,3,1,5,this,'L');
			add(txtTPRDS = new TxtLimit(100),2,8,1,5,this,'L');
			
			add(new JLabel("Commercial :"),3,1,1,2,this,'L');
			add(txtCPRCD = new TxtLimit(10),3,3,1,1.25,this,'L');
			add(new JLabel("Grade"),3,4,1,0.75,this,'R');
			add(txtCGRDS = new TxtLimit(20),3,5,1,2,this,'L');						
			//add(chkCGRDS = new JCheckBox("External"),3,7,1,1,this,'L');						
			add(txtCPRDS = new TxtLimit(50),3,8,1,5,this,'L');						
			
			add(new JLabel("Old Code"),4,1,1,2,this,'L');
			add(txtOPRCD = new TxtLimit(10),4,3,1,1.25,this,'L');
			add(txtOPRDS = new TxtLimit(45),4,5,1,3,this,'L');
			add(cmbMGRCD = new JComboBox(),4,8,1,5,this,'L');
			add(txtMGRCD = new JTextField(),4,8,1,5,this,'L');
			
			//add(new JLabel(strMGRCD),5,1,1,3,this,'L');
			//add(cmbMGRCD = new JComboBox(),5,4,1,4,this,'L');
			//add(txtMGRCD = new JTextField(),5,4,1,4,this,'L');
			
			add(new JLabel(strPMRCD),5,1,1,3,this,'L');
			add(cmbPMRCD = new JComboBox(),5,4,1,4,this,'L');
			add(txtPMRCD = new JTextField(),5,4,1,4,this,'L');
			
			add(new JLabel(strPRXCD),6,1,1,3,this,'L');
			add(cmbPRMCD = new JComboBox(),6,4,1,2,this,'L');
			add(txtPRMCD = new JTextField(),6,4,1,2,this,'L');
			
			add(cmbPRSCD = new JComboBox(),6,6,1,2,this,'L');
			add(txtPRSCD = new JTextField(),6,6,1,2,this,'L');
			

			add(new JLabel(strPC0CD),7,1,1,3,this,'L');
			add(cmbPC0CD = new JComboBox(),7,4,1,4,this,'L');
			add(txtPC0CD = new JTextField(),7,4,1,4,this,'L');
			
			add(new JLabel(strPM0CD),8,1,1,3,this,'L');
			add(cmbPM0CD = new JComboBox(),8,4,1,4,this,'L');
			add(txtPM0CD = new JTextField(),8,4,1,4,this,'L');
			
			add(new JLabel(strPA0CD),9,1,1,3,this,'L');
			add(cmbPA0CD = new JComboBox(),9,4,1,4,this,'L');
			add(txtPA0CD = new JTextField(),9,4,1,4,this,'L');

			
			add(new JLabel(strPC1CD),10,1,1,3,this,'L');
			add(cmbPC1CD = new JComboBox(),10,4,1,4,this,'L');
			add(txtPC1CD = new JTextField(),10,4,1,4,this,'L');

			add(new JLabel(strPM1CD),11,1,1,3,this,'L');
			add(cmbPM1CD = new JComboBox(),11,4,1,4,this,'L');
			add(txtPM1CD = new JTextField(),11,4,1,4,this,'L');
			
			add(new JLabel(strPA1CD),12,1,1,3,this,'L');
			add(cmbPA1CD = new JComboBox(),12,4,1,4,this,'L');
			add(txtPA1CD = new JTextField(),12,4,1,4,this,'L');

			add(new JLabel(strPC2CD),13,1,1,3,this,'L');
			add(cmbPC2CD = new JComboBox(),13,4,1,4,this,'L');
			add(txtPC2CD = new JTextField(),13,4,1,4,this,'L');

			
			add(new JLabel(strPM2CD),14,1,1,3,this,'L');
			add(cmbPM2CD = new JComboBox(),14,4,1,4,this,'L');
			add(txtPM2CD = new JTextField(),14,4,1,4,this,'L');
			
			add(new JLabel(strPA2CD),15,1,1,3,this,'L');
			add(cmbPA2CD = new JComboBox(),15,4,1,4,this,'L');
			add(txtPA2CD = new JTextField(),15,4,1,4,this,'L');

			add(new JLabel(strXXPVL),16,1,1,3,this,'L');
			//add(new JLabel(strPRPCD),16,4,1,1,this,'L');
			add(txtBSPCD = new TxtNumLimit(2.0),16,4,1,.5,this,'L');
			add(new JLabel("%"),16,4,1,.3,this,'R');
			
			add(txtPRPCD = new TxtNumLimit(2.0),16,5,1,.5,this,'L');
			add(new JLabel("%"),16,5,1,.3,this,'R');
			
			//add(new JLabel(strSCPCD),16,6,1,1,this,'L');
			add(txtSCPCD = new TxtNumLimit(2.0),16,6,1,.5,this,'L');
			add(new JLabel("%"),16,6,1,.3,this,'R');
			
			add(new JLabel(strPCCCD),17,1,1,3,this,'L');			
			add(cmbPCCCD = new JComboBox(),17,4,1,2,this,'L');
			add(txtPCCCD = new JTextField(),17,4,1,2,this,'L');

			add(lblADDDTL = new JLabel(""),14,8,1,5,this,'L');
						
			//add(new JLabel(strPCSCD),15,1,1,3,this,'L');			
			add(cmbPCSCD = new JComboBox(),17,6,1,2,this,'L');
			add(txtPCSCD = new JTextField(),17,6,1,2,this,'L');

			add(new JScrollPane(lstADDDTL),15,8,4,5,this,'L');
			add(chkADDDTL = new JCheckBox("Additives Maint."),18,6,1,2,this,'L');

			add(new JLabel(strQFLCD),18,1,1,3,this,'L');
			add(cmbQFLCD = new JComboBox(),18,4,1,2,this,'L');
			add(txtQFLCD = new JTextField(),18,4,1,2,this,'L');											

			add(lblADDCT,13,8,1,2,this,'L');
			add(btnADDCT,13,11,1,2,this,'L');
			
			add(lblPRMDS,17,8,1,4,this,'L');
			add(lblPRSDS,18,8,1,4,this,'L');
			
			lstADDDTL.setBackground(Color.CYAN);
			vtrADDDTL.clear();
			lstADDDTL.setListData(vtrADDDTL);
			lstADDDTL.setVisible(false);
			chkADDDTL.setVisible(false);
			
			
			setENBL(false);
			txtTPRCD.setText("");
			txtCPRCD.setText("");
			vtrLSTDL = new Vector<String>();
			vtrLSTDL.addElement("1234567890");
			txtCODE = new TxtNumLimit(2);
			txtCODE1 = new TxtNumLimit(4);
			txtCODE.setInputVerifier(objINPVR);
			txtCODE1.setInputVerifier(objINPVR);
			txtPRPCD.setInputVerifier(objINPVR);
			txtSCPCD.setInputVerifier(objINPVR);			
			txtPRSCD.setInputVerifier(objINPVR);
		}   
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}   
	}
	
	
	/** 
	 */
	public void setADDDTL(String LP_HDRCT,String LP_HDRCD)
	{
		try
		{
			String L_strHDRDS_00 = "";
			lblADDDTL.setText(L_strHDRDS_00);
			String L_strSQLQRY = "Select CD_SUBCD,CD_HDRDS from PR_CDWRK where CD_HDRCT = '"+LP_HDRCT+"' and CD_HDRCD = '"+LP_HDRCD+"' order by CD_SUBCD asc";
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
			//System.out.println(L_strSQLQRY);
			if(L_rstRSSET==null || !L_rstRSSET.next())
				{setMSG("No Records Found",'E');}	
			int i=0;
			vtrADDDTL.clear();
			while(true)
			{
				String L_strSUBCD = getRSTVAL(L_rstRSSET,"CD_SUBCD","C");
				String L_strHDRDS = getRSTVAL(L_rstRSSET,"CD_HDRDS","C");
				if(L_strSUBCD.equals("00"))
				{
					L_strHDRDS_00 = L_strHDRDS;
					lblADDDTL.setText(L_strHDRDS_00);
					if(!L_rstRSSET.next())
						break;
					continue;
				}
				vtrADDDTL.addElement(L_strSUBCD+"    "+L_strHDRDS);
				i++;
				if(!L_rstRSSET.next())
					break;
			}
			lstADDDTL.setListData(vtrADDDTL);
			lstADDDTL.setVisible(true);
			chkADDDTL.setVisible(true);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setADDLST");}
	}
	

/**
 */	
private void cmbRESIZE(JComboBox LP_CMBNM, String LP_RSZTP)
{
	try
	{
		//LP_CMBNM.removeActionListener(this);
		if(LP_RSZTP.equalsIgnoreCase("Enlarge"))
			{ dimORGCMB = LP_CMBNM.getSize(); LP_CMBNM.setSize(dimNEWCMB);}
		if(LP_RSZTP.equalsIgnoreCase("Normal"))
			LP_CMBNM.setSize(dimORGCMB);
		//if(LP_RSZTP.equalsIgnoreCase("Normal1"))
		//	LP_CMBNM.setSize(dimORGCMB1);
		LP_CMBNM.revalidate();
		//LP_CMBNM.addActionListener(this);
	}   
	catch(Exception L_EX)
	{   
		setMSG(L_EX,"cmbRESIZE");
	}   
}

	
	
	/**
	 */
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
			super.focusLost(L_FE);
			{
				if(M_objSOURC==txtTPRDS)
					txtTPRDS.setText(txtTPRDS.getText().trim().toUpperCase());
				if(M_objSOURC==txtCPRDS)
					txtCPRDS.setText(txtCPRDS.getText().trim().toUpperCase());
				if(M_objSOURC==cmbPMRCD)
					{getCPRCD();cmbRESIZE(cmbPMRCD,"Normal");}
				else if(M_objSOURC==cmbPC0CD)
					cmbRESIZE(cmbPC0CD,"Normal");
				else if(M_objSOURC==cmbPM0CD)
					cmbRESIZE(cmbPM0CD,"Normal");
				else if(M_objSOURC==cmbPA0CD)
					cmbRESIZE(cmbPA0CD,"Normal");
				else if(M_objSOURC==cmbPC1CD)
					cmbRESIZE(cmbPC1CD,"Normal");
				else if(M_objSOURC==cmbPM1CD)
					cmbRESIZE(cmbPM1CD,"Normal");
				else if(M_objSOURC==cmbPA1CD)
					cmbRESIZE(cmbPA1CD,"Normal");
				else if(M_objSOURC==cmbPC2CD)
					cmbRESIZE(cmbPC2CD,"Normal");
				else if(M_objSOURC==cmbPM2CD)
					cmbRESIZE(cmbPM2CD,"Normal");
				else if(M_objSOURC==cmbPA2CD)
					cmbRESIZE(cmbPA2CD,"Normal");
				else if(M_objSOURC==cmbPCCCD)
					{}//cmbRESIZE(cmbPCCCD,"Normal");
				else if(M_objSOURC==cmbPCSCD)
					{}//cmbRESIZE(cmbPCSCD,"Normal");
				else if(M_objSOURC==cmbPRMCD)
					{}//cmbRESIZE(cmbPRMCD,"Normal");
				else if(M_objSOURC==cmbPRSCD)
					{}//cmbRESIZE(cmbPRSCD,"Normal");
				}

			}
		catch (Exception L_EX)
			{setMSG(L_EX,"focusLost");}
	}
	
	/** Displaying product reference Maincode description
	 */
    private void setPRMDS(String LP_PMRCD, String LP_PRMCD)
	{
		try
		{
			ResultSet L_rstRSSET = null;
			String L_strWHRSTR = " CD_HDRCT = 'RF' and CD_HDRCD = '"+LP_PMRCD+LP_PRMCD+"0A' and CD_SUBCD = '00'";
			M_strSQLQRY = "select CD_HDRCD, CD_HDRDS from PR_CDWRK where "+L_strWHRSTR;
			//System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
				{lblPRMDS.setText(L_rstRSSET.getString("CD_HDRDS")); L_rstRSSET.close();}
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setPRMDS");}
	}
	
	/** Displaying product reference subcode description
	 */
    private void setPRSDS(String LP_PMRCD, String LP_PRMCD, String LP_PRSCD)
	{
		try
		{
			ResultSet L_rstRSSET = null;
			String L_strWHRSTR = " CD_HDRCT = 'RF' and CD_HDRCD = '"+LP_PMRCD+LP_PRMCD+LP_PRSCD+"'  and CD_SUBCD = '00'";
			M_strSQLQRY = "select CD_HDRCD, CD_HDRDS from PR_CDWRK where "+L_strWHRSTR;
			//System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
				{lblPRSDS.setText(L_rstRSSET.getString("CD_HDRDS")); L_rstRSSET.close();}
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setPRMDS");}
	}
	
	
	
	/**
	 * Super Class method overrided to enable & disable the Components.
	 * @param L_flgSTAT boolean argument to pass State of the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{      
		super.setENBL(L_flgSTAT);
		cmbMGRCD.removeAllItems();
		cmbPMRCD.removeAllItems();
		cmbPC0CD.removeAllItems();
		cmbPM0CD.removeAllItems();
		cmbPA0CD.removeAllItems();
		cmbPC1CD.removeAllItems();
		cmbPM1CD.removeAllItems();
		cmbPA1CD.removeAllItems();
		cmbPC2CD.removeAllItems();
		cmbPM2CD.removeAllItems();
		cmbPA2CD.removeAllItems();
		cmbPCCCD.removeAllItems();
		cmbPCSCD.removeAllItems();
		cmbPRMCD.removeAllItems();
		cmbPRSCD.removeAllItems();
		cmbQFLCD.removeAllItems();
		if(L_flgSTAT == false)		
			return;			

		btnADDCT.setVisible(false);
		
		txtCGRDS.setEnabled(true);
		cmbPMRCD.setEnabled(false);
		cmbPC0CD.setEnabled(false);
		cmbPM0CD.setEnabled(false);
		cmbPA0CD.setEnabled(false);
		cmbPC1CD.setEnabled(false);
		cmbPM1CD.setEnabled(false);
		cmbPA1CD.setEnabled(false);
		cmbPC2CD.setEnabled(false);
		cmbPM2CD.setEnabled(false);
		cmbPA2CD.setEnabled(false);
		cmbPCCCD.setEnabled(false);
		cmbQFLCD.setEnabled(false);
		txtBSPCD.setEnabled(false);
		txtPRPCD.setEnabled(false);
		txtSCPCD.setEnabled(false);
		cmbPCSCD.setEnabled(false);
		cmbPRMCD.setEnabled(false);
		cmbPRSCD.setEnabled(false);
		txtOPRDS.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			txtCPRCD.setEnabled(false);			
			txtTPRCD.setEnabled(false);												
		}		
	}
	

	    /** Looping back if option is not selected (combobox to combobox)
	     */
		private  boolean chkSELECT_CC(JComboBox LP_CMB1, JComboBox LP_CMB2, int LP_DGTNO)
		{
			if(LP_CMB1.getSelectedItem().equals("Select"))
			{
				txtTPRCD.setText(txtTPRCD.getText().substring(0,LP_DGTNO));
				getTBLDT(txtTPRCD.getText());
				setENBLE(LP_CMB1);
				LP_CMB2.setEnabled(false);
				return true;
			}
			return false;
		}


	    /** Looping back if option is not selected (combobox to TextField)
	     */
		private  boolean chkSELECT_CT(JComboBox LP_CMB, JTextField LP_TXT, int LP_DGTNO)
		{
			if(LP_CMB.getSelectedItem().equals("Select"))
			{
				txtTPRCD.setText(txtTPRCD.getText().substring(0,LP_DGTNO));
				getTBLDT(txtTPRCD.getText());
				setENBLE(LP_CMB);
				LP_TXT.setEnabled(false);
				return true;
			}
			return false;
		}
		
		
		
	    /** Looping back if option is not selected (TextField to TextField)
	     */
		private  boolean chkSELECT_TT(JTextField LP_TXT1, JTextField LP_TXT2, int LP_LEN,int LP_DGTNO)
		{
			if(LP_TXT1.getText().trim().length() != LP_LEN)
			{		
				txtTPRCD.setText(txtTPRCD.getText().substring(0,LP_DGTNO));
				getTBLDT(txtTPRCD.getText());
				setENBLE(LP_TXT1);
				LP_TXT2.setEnabled(false);
				return true;
			}
			return false;
		}
		

			    /** Looping back if option is not selected (TextField to ComboBox)
	     */
		private  boolean chkSELECT_TC(JTextField LP_TXT, JComboBox LP_CMB, int LP_LEN,int LP_DGTNO)
		{
			if(LP_TXT.getText().trim().length() != LP_LEN)
			{		
				txtTPRCD.setText(txtTPRCD.getText().substring(0,LP_DGTNO));
				getTBLDT(txtTPRCD.getText());
				setENBLE(LP_TXT);
				LP_CMB.setEnabled(false);
				return true;
			}
			return false;
		}

		
	/**
     */	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);    
		try 
		{ 
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() > 0)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						setENBL(true);
						txtTPRDS.requestFocus();
						clrCOMP_1();
						txtTPRDS.setEnabled(true);
						cmbMGRCD.setVisible(true);
						cmbPMRCD.setVisible(true);
						cmbPC0CD.setVisible(true);
						cmbPM0CD.setVisible(true);
						cmbPA0CD.setVisible(true);
						cmbPC1CD.setVisible(true);
						cmbPM1CD.setVisible(true);
						cmbPA1CD.setVisible(true);
						cmbPC2CD.setVisible(true);
						cmbPM2CD.setVisible(true);
						cmbPA2CD.setVisible(true);
						cmbPCCCD.setVisible(true);
						cmbPCSCD.setVisible(true);
						cmbPRMCD.setVisible(true);
						cmbPRSCD.setVisible(true);
						cmbQFLCD.setVisible(true);
						txtMGRCD.setVisible(false);
						txtPMRCD.setVisible(false);
						txtPC0CD.setVisible(false);
						txtPM0CD.setVisible(false);
						txtPA0CD.setVisible(false);
						txtPC1CD.setVisible(false);
						txtPM1CD.setVisible(false);
						txtPA1CD.setVisible(false);
						txtPC2CD.setVisible(false);
						txtPM2CD.setVisible(false);
						txtPA2CD.setVisible(false);
						txtPCCCD.setVisible(false);
						txtPCSCD.setVisible(false);
						txtQFLCD.setVisible(false);
						txtTPRDS.requestFocus();
					}
					else
					{
						setENBL(false);
						txtTPRCD.requestFocus();
						txtPMRCD.setVisible(true);
						txtPC0CD.setVisible(true);
						txtPM0CD.setVisible(true);
						txtPA0CD.setVisible(true);
						txtPC1CD.setVisible(false);
						txtPM1CD.setVisible(true);
						txtPA1CD.setVisible(true);
						txtPC2CD.setVisible(false);
						txtPM2CD.setVisible(true);
						txtPA2CD.setVisible(true);
						txtPCCCD.setVisible(true);
						txtPCSCD.setVisible(true);
						txtPRMCD.setVisible(true);
						txtPRSCD.setVisible(true);
						txtQFLCD.setVisible(true);
						txtMGRCD.setVisible(true);
						cmbMGRCD.setVisible(false);
						cmbPMRCD.setVisible(false);
						cmbPC0CD.setVisible(false);
						cmbPM0CD.setVisible(false);
						cmbPA0CD.setVisible(false);
						cmbPC1CD.setVisible(false);
						cmbPM1CD.setVisible(false);
						cmbPA1CD.setVisible(false);
						cmbPC2CD.setVisible(false);
						cmbPM2CD.setVisible(false);
						cmbPA2CD.setVisible(false);
						cmbPCCCD.setVisible(false);
						cmbPCSCD.setVisible(false);
						cmbPRMCD.setVisible(false);
						cmbPRSCD.setVisible(false);
						cmbQFLCD.setVisible(false);
						txtCPRCD.setEnabled(true);
						txtOPRCD.setEnabled(true);
						txtTPRCD.setEnabled(true);
					}					
				}
				else
					setENBL(false);				
			}			
			else if(M_objSOURC == btnADDCT)
				exeADDNEW();
			else if(M_objSOURC == cmbMGRCD)
			{
				if(cmbMGRCD.getSelectedItem().equals("Select"))
				{					
					txtCPRCD.setText("");
					txtTPRCD.setText("");
					tblDATA.clrTABLE();
					setENBLE(cmbMGRCD);
					return;
				}
				else
				{
					setCursor(cl_dat.M_curWTSTS_pbst);					
					txtCPRCD.setText(cmbMGRCD.getSelectedItem().toString().substring(0,2));					
					txtTPRCD.setText(cmbMGRCD.getSelectedItem().toString().substring(0,2));										
					getTBLDT(txtTPRCD.getText());
					cmbPMRCD.setEnabled(true);
					String L_strTEMP = "";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CCSVL  from CO_CDTRN where CMT_CGMTP = 'MST'";
					M_strSQLQRY += " AND CMT_CGSTP = 'PRXXPMR' AND isnull(CMT_STSFL,'')<> 'X'";
					//M_strSQLQRY += " AND CMT_CODCD like '"+ cmbMGRCD.getSelectedItem().toString().substring(0,2) +"__'";
					M_strSQLQRY += " order by CMT_CODCD";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					cmbPMRCD.removeAllItems();
					if(L_rstRSSET != null)
					{
						cmbPMRCD.addItem("Select");
						hstPMRAB.clear();
						while(L_rstRSSET.next())
						{
							cmbPMRCD.addItem(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
							hstPMRAB.put(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET.getString("CMT_CCSVL"),""));
						}
						L_rstRSSET.close();
					}
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if(M_objSOURC == cmbPMRCD)
			{				
				txtCPRCD.setText(txtTPRCD.getText().substring(0,intMGRCD));
				if (chkSELECT_CC(cmbPMRCD,cmbPRMCD,intMGRCD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtCPRCD.setText(txtTPRCD.getText().substring(0,intMGRCD)+cmbPMRCD.getSelectedItem().toString().substring(0,2));
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intMGRCD)+cmbPMRCD.getSelectedItem().toString().substring(0,2));					
				if(txtCPRDS.getText().length()==0)
					txtCPRDS.setText(cmbPMRCD.getSelectedItem().toString().substring(3));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,3,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='RF' and SUBSTRING(CD_HDRCD,1,2) = '"+cmbPMRCD.getSelectedItem().toString().substring(0,2)+"' ";
				M_strSQLQRY += " AND CD_HDRCD  like '____0A'   and CD_SUBCD = '00'  order by CD_HDRCD";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbPRMCD.removeAllItems();
				if(M_rstRSSET != null)
				{						
					cmbPRMCD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPRMCD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				setNEXT_CMB(cmbPRMCD);
			}
			else if(M_objSOURC == cmbPRMCD)
			{
				if (chkSELECT_CC(cmbPRMCD,cmbPRSCD,intPMRCD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPMRCD)+cmbPRMCD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,5,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='RF'";
				M_strSQLQRY += " AND CD_HDRCD like '"+ cmbPMRCD.getSelectedItem().toString().trim().substring(0,2)+cmbPRMCD.getSelectedItem().toString().trim().substring(0,2)+"__'";			
				M_strSQLQRY += " AND CD_HDRCD not like '____0A'  and CD_SUBCD = '00'";
				M_strSQLQRY += " order by CD_HDRCD";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbPRSCD.removeAllItems();
				if(M_rstRSSET != null)
				{						
					cmbPRSCD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPRSCD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				setNEXT_CMB(cmbPRSCD);
			}
			else if(M_objSOURC == cmbPRSCD)
			{				
				if (chkSELECT_CC(cmbPRSCD,cmbPC0CD,intPRMCD))
					return;
				intADDDTL = 3;
				setADDDTL("RF",cmbPMRCD.getSelectedItem().toString().substring(0,2)+cmbPRMCD.getSelectedItem().toString().substring(0,2)+cmbPRSCD.getSelectedItem().toString().substring(0,2));
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPRMCD)+cmbPRSCD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				System.out.println("*"+cmbPRSCD.getSelectedItem().toString().substring(3).trim()+"*");
				System.out.println("*"+txtOPRDS.getText()+"*");
				//txtCGRDS.setText("SP"+txtCPRCD.getText().substring(2));
				//if(cmbPRSCD.getSelectedItem().toString().substring(3).trim().equalsIgnoreCase(txtOPRDS.getText()))
				//	txtCGRDS.setText(txtOPRDS.getText());
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CGSTP,CMT_CCSVL from CO_CDTRN where CMT_CGMTP = 'MST'";
				M_strSQLQRY += " AND CMT_CGSTP ='PRXXPCT' AND isnull(CMT_STSFL,'')<> 'X' ORDER BY CMT_CODCD";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				cmbPC0CD.removeAllItems();
				if(M_rstRSSET != null)
				{
					hstPC0AB.clear();
					cmbPC0CD.addItem("Select");
					while(M_rstRSSET.next())
					{
						cmbPC0CD.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						hstPC0AB.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),""));
					}
					M_rstRSSET.close();
				}
				cmbPC0CD.setEnabled(true);					
				setCursor(cl_dat.M_curDFSTS_pbst);
				setNEXT_CMB(cmbPC0CD);
				}
			else if(M_objSOURC == cmbPC0CD)
			{
				if (chkSELECT_CC(cmbPC0CD,cmbPM0CD,intPRSCD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPRSCD)+cmbPC0CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				//setCGRDS_1(hstPMRAB.get(txtTPRCD.getText().substring(intPMRCD-2,intPMRCD)).toString(),hstPC0AB.get(txtTPRCD.getText().substring(intPC0CD-2,intPC0CD)).toString());
				//txtCPRDS.setText(cmbPC0CD.getSelectedItem().toString().substring(3));
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,3,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC' ";
				M_strSQLQRY += " AND substring(CD_HDRCD,1,2)= '"+ cmbPC0CD.getSelectedItem().toString().trim().substring(0,2)+"'  and CD_SUBCD = '00'";
				M_strSQLQRY += " AND CD_HDRCD like '____0A'";
				M_strSQLQRY += "  order by CD_HDRCD";					
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				cmbPM0CD.removeAllItems();
				if(M_rstRSSET != null)
				{
					cmbPM0CD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPM0CD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
				if(cmbPC0CD.getSelectedIndex()==1 && cmbPC0CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					cmbPM0CD.setSelectedIndex(1);
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPC0CD)+"0000");
				}
				setNEXT_CMB(cmbPM0CD);
			}
			else if(M_objSOURC == cmbPM0CD)
			{
				if (chkSELECT_CC(cmbPM0CD,cmbPA0CD,intPC0CD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPC0CD)+cmbPM0CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,5,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
				M_strSQLQRY += " AND CD_HDRCD like '"+ cmbPC0CD.getSelectedItem().toString().trim().substring(0,2)+cmbPM0CD.getSelectedItem().toString().trim().substring(0,2)+"__'";
				M_strSQLQRY += " AND CD_HDRCD not like '____0A'  and CD_SUBCD = '00'";
				M_strSQLQRY += " order by CD_HDRCD";					
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbPA0CD.removeAllItems();
				if(M_rstRSSET != null)
				{						
					cmbPA0CD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPA0CD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				if(cmbPM0CD.getSelectedIndex()==1 && cmbPM0CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					cmbPA0CD.setSelectedIndex(1);
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPM0CD)+"00");
				}
				setNEXT_CMB(cmbPA0CD);
			}
			else if(M_objSOURC == cmbPA0CD)
			{				
				if (chkSELECT_CC(cmbPA0CD,cmbPC1CD,intPM0CD))
					return;
				intADDDTL = 0;
				setADDDTL("PC",cmbPC0CD.getSelectedItem().toString().substring(0,2)+cmbPM0CD.getSelectedItem().toString().substring(0,2)+cmbPA0CD.getSelectedItem().toString().substring(0,2));
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPM0CD)+cmbPA0CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CGSTP from CO_CDTRN where CMT_CGMTP = 'MST'";
				M_strSQLQRY += " AND CMT_CGSTP ='PRXXPCT' AND isnull(CMT_STSFL,'')<> 'X' ORDER BY CMT_CODCD";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				cmbPC1CD.removeAllItems();
				if(M_rstRSSET != null)
				{
					cmbPC1CD.addItem("Select");
					while(M_rstRSSET.next())										
						cmbPC1CD.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					M_rstRSSET.close();
				}
				cmbPC1CD.setEnabled(true);
				setNEXT_CMB(cmbPC1CD);
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == cmbPC1CD)
			{
				if (chkSELECT_CC(cmbPC1CD,cmbPM1CD,intPA0CD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPA0CD)+cmbPC1CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,3,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC' ";
				M_strSQLQRY += " AND substring(CD_HDRCD,1,2)= '"+ cmbPC1CD.getSelectedItem().toString().trim().substring(0,2)+"'  and CD_SUBCD = '00'";
				M_strSQLQRY += " AND CD_HDRCD like '____0A'";
				M_strSQLQRY += "  order by CD_HDRCD";					
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				cmbPM1CD.removeAllItems();
				if(M_rstRSSET != null)
				{
					cmbPM1CD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPM1CD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				setCursor(cl_dat.M_curDFSTS_pbst);								
				if(cmbPC1CD.getSelectedIndex()==1 && cmbPC1CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					cmbPM1CD.setSelectedIndex(1);
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPC1CD)+"0000");
				}
				setNEXT_CMB(cmbPM1CD);
			}
			else if(M_objSOURC == cmbPM1CD)
			{
				if (chkSELECT_CC(cmbPM1CD,cmbPA1CD,intPC1CD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPC1CD)+cmbPM1CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,5,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
				M_strSQLQRY += " AND CD_HDRCD like '"+ cmbPC1CD.getSelectedItem().toString().trim().substring(0,2)+cmbPM1CD.getSelectedItem().toString().trim().substring(0,2)+"__'";			
				M_strSQLQRY += " AND CD_HDRCD not like '____0A'  and CD_SUBCD = '00'";
				M_strSQLQRY += " order by CD_HDRCD";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbPA1CD.removeAllItems();
				if(M_rstRSSET != null)
				{						
					cmbPA1CD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPA1CD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				if(cmbPM1CD.getSelectedIndex()==1 && cmbPM1CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					cmbPA1CD.setSelectedIndex(1);
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPM1CD)+"00");
				}
				setNEXT_CMB(cmbPA1CD);
			}
			else if(M_objSOURC == cmbPA1CD)
			{				
				if (chkSELECT_CC(cmbPA1CD,cmbPC2CD,intPM1CD))
					return;
				intADDDTL = 1;
				setADDDTL("PC",cmbPC1CD.getSelectedItem().toString().substring(0,2)+cmbPM1CD.getSelectedItem().toString().substring(0,2)+cmbPA1CD.getSelectedItem().toString().substring(0,2));
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPM1CD)+cmbPA1CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CGSTP from CO_CDTRN where CMT_CGMTP = 'MST'";
				M_strSQLQRY += " AND CMT_CGSTP ='PRXXPCT' AND isnull(CMT_STSFL,'')<> 'X' ORDER BY CMT_CODCD";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				cmbPC2CD.removeAllItems();
				if(M_rstRSSET != null)
				{
					cmbPC2CD.addItem("Select");
					while(M_rstRSSET.next())										
						cmbPC2CD.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));						
					M_rstRSSET.close();
				}
				cmbPC2CD.setEnabled(true);
				setNEXT_CMB(cmbPC2CD);
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == cmbPC2CD)
			{
				if (chkSELECT_CC(cmbPC2CD,cmbPM2CD,intPA1CD))
					return;
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPA1CD)+cmbPC2CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,3,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
				M_strSQLQRY += " AND substring(CD_HDRCD,1,2)= '"+ cmbPC2CD.getSelectedItem().toString().trim().substring(0,2)+"'";
				M_strSQLQRY += " AND CD_HDRCD like '____0A'  and CD_SUBCD = '00'";
				M_strSQLQRY += "  order by CD_HDRCD";					
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				cmbPM2CD.removeAllItems();
				if(M_rstRSSET != null)
				{
					cmbPM2CD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPM2CD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				if(cmbPC2CD.getSelectedIndex()==1 && cmbPC2CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					cmbPM2CD.setSelectedIndex(1);
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPC2CD)+"0000");
				}
				setNEXT_CMB(cmbPM2CD);
				setCursor(cl_dat.M_curDFSTS_pbst);								
			}
			else if(M_objSOURC == cmbPM2CD)
			{
				if (chkSELECT_CC(cmbPM2CD,cmbPA2CD,intPC2CD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPC2CD)+cmbPM2CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,5,2) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
				M_strSQLQRY += " AND CD_HDRCD like '"+ cmbPC2CD.getSelectedItem().toString().trim().substring(0,2)+cmbPM2CD.getSelectedItem().toString().trim().substring(0,2)+"__'";			
				M_strSQLQRY += " AND CD_HDRCD not like '____0A'  and CD_SUBCD = '00'";
				M_strSQLQRY += " order by CD_HDRCD";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbPA2CD.removeAllItems();
				if(M_rstRSSET != null)
				{						
					cmbPA2CD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPA2CD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				if(cmbPM2CD.getSelectedIndex()==1 && cmbPM2CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					cmbPA2CD.setSelectedIndex(1);
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPM2CD)+"00");
				}
				setNEXT_CMB(cmbPA2CD);
			}
			else if(M_objSOURC == cmbPA2CD)
			{				
				if (chkSELECT_CT(cmbPA2CD,txtBSPCD,intPM2CD))
					return;
				intADDDTL = 2;
				setADDDTL("PC",cmbPC2CD.getSelectedItem().toString().substring(0,2)+cmbPM2CD.getSelectedItem().toString().substring(0,2)+cmbPA2CD.getSelectedItem().toString().substring(0,2));
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPM2CD)+cmbPA2CD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				txtBSPCD.setEnabled(true);
				if(cmbPC0CD.getSelectedIndex()==1 && cmbPC0CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					txtBSPCD.setText("00");
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPA2CD)+"00");
				}
				setNEXT_TXT(txtBSPCD);
			}
			else if(M_objSOURC == txtBSPCD)
			{
				if (chkSELECT_TT(txtBSPCD,txtPRPCD,2,intPA2CD))
					return;
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPA2CD)+txtBSPCD.getText().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				if(cmbPC1CD.getSelectedIndex()==1 && cmbPC1CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					txtPRPCD.setText("00");
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intBSPCD)+"00");
				}
				setNEXT_TXT(txtPRPCD);
			}
			else if(M_objSOURC == txtPRPCD)
			{
				if (chkSELECT_TT(txtPRPCD,txtSCPCD,2,intBSPCD))
					return;
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intBSPCD)+txtPRPCD.getText().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				if(cmbPC2CD.getSelectedIndex()==1 && cmbPC2CD.getSelectedItem().toString().substring(0,2).equals("00"))
				{
					txtSCPCD.setText("00");
					txtTPRCD.setText(txtTPRCD.getText().substring(0,intPRPCD)+"00");
				}
				setNEXT_TXT(txtSCPCD);
			}
			else if(M_objSOURC == txtSCPCD)
			{
				if (chkSELECT_TC(txtSCPCD,cmbPCCCD,2,intPRPCD))
					return;
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPRPCD)+txtSCPCD.getText().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,1,2) CD_HDRCD,CD_HDRDS,CD_ABBCD from PR_CDWRK where CD_HDRCT ='CS'";
				M_strSQLQRY += " AND CD_HDRCD like '__000A'   and CD_SUBCD = '00'  order by CD_HDRCD";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbPCCCD.removeAllItems();
				if(M_rstRSSET != null)
				{	
					hstPCCAB.clear();
					cmbPCCCD.addItem("Select");
					while(M_rstRSSET.next())
					{
						cmbPCCCD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
						hstPCCAB.put(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),""),nvlSTRVL(M_rstRSSET.getString("CD_ABBCD"),""));
					}
					M_rstRSSET.close();
				}
				setNEXT_CMB(cmbPCCCD);
			}
			else if(M_objSOURC == cmbPCCCD)
			{
				if (chkSELECT_CC(cmbPCCCD,cmbPCSCD,intSCPCD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intSCPCD)+cmbPCCCD.getSelectedItem().toString().substring(0,2));
				getTBLDT(txtTPRCD.getText());
				M_strSQLQRY = "Select distinct substring(CD_HDRCD,3,4) CD_HDRCD,CD_HDRDS from PR_CDWRK where CD_HDRCT ='CS' and SUBSTRING(CD_HDRCD,1,2) = '"+cmbPCCCD.getSelectedItem().toString().substring(0,2)+"' ";
				M_strSQLQRY += " AND CD_HDRCD not like '__000A'   and CD_SUBCD = '00'  order by CD_HDRCD";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbPCSCD.removeAllItems();
				if(M_rstRSSET != null)
				{						
					cmbPCSCD.addItem("Select");
					while(M_rstRSSET.next())
						cmbPCSCD.addItem(nvlSTRVL(M_rstRSSET.getString("CD_HDRCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
					M_rstRSSET.close();
				}
				setNEXT_CMB(cmbPCSCD);
			}
			else if(M_objSOURC == cmbPCSCD)
			{
				if (chkSELECT_CC(cmbPCSCD,cmbQFLCD,intPCCCD))
					return;
				setCursor(cl_dat.M_curWTSTS_pbst);
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPCCCD)+cmbPCSCD.getSelectedItem().toString().substring(0,4));
				getTBLDT(txtTPRCD.getText());
				cmbQFLCD.setEnabled(true);
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY = "Select distinct CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='MST'";
				M_strSQLQRY += " AND CMT_CGSTP ='PRXXQFT' AND isnull(CMT_STSFL,'')<>'X'";
				M_strSQLQRY += " order by CMT_CODCD";					
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
				cmbQFLCD.removeAllItems();
				if(M_rstRSSET != null)
				{						
					cmbQFLCD.addItem("Select");
					while(M_rstRSSET.next())
						cmbQFLCD.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					M_rstRSSET.close();
				}					
				setNEXT_CMB(cmbQFLCD);
			}
			else if(M_objSOURC == cmbQFLCD)
			{
				if (chkSELECT_CT(cmbQFLCD,txtOPRDS,intPCSCD))
					return;
				lstADDDTL.setVisible(false);
				chkADDDTL.setVisible(false);
				lblADDDTL.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtTPRCD.setText(txtTPRCD.getText().substring(0,intPCSCD)+cmbQFLCD.getSelectedItem().toString().substring(0,1));
				setCGRDS_1(hstPMRAB.get(txtTPRCD.getText().substring(intPMRCD-2,intPMRCD)).toString(),hstPC0AB.get(txtTPRCD.getText().substring(intPC0CD-2,intPC0CD)).toString());
				getTBLDT(txtTPRCD.getText());	
			}
			else if(M_objSOURC == chkADDDTL)
			{
				if(intADDDTL == 0)
					exeADDDSP("PC",cmbPC0CD.getSelectedItem().toString().substring(0,2)+cmbPM0CD.getSelectedItem().toString().substring(0,2)+cmbPA0CD.getSelectedItem().toString().substring(0,2));
				else if(intADDDTL == 1)
					exeADDDSP("PC",cmbPC1CD.getSelectedItem().toString().substring(0,2)+cmbPM1CD.getSelectedItem().toString().substring(0,2)+cmbPA1CD.getSelectedItem().toString().substring(0,2));
				else if(intADDDTL == 2)
					exeADDDSP("PC",cmbPC2CD.getSelectedItem().toString().substring(0,2)+cmbPM2CD.getSelectedItem().toString().substring(0,2)+cmbPA2CD.getSelectedItem().toString().substring(0,2));
				else if(intADDDTL == 3)
					exeADDDSP("RF",cmbPMRCD.getSelectedItem().toString().substring(0,2)+cmbPRMCD.getSelectedItem().toString().substring(0,2)+cmbPRSCD.getSelectedItem().toString().substring(0,2));
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}
		
	}	
	
	/**
	*/
	private void setCGRDS_1(String LP_PMRAB, String LP_PC0AB)
    {
		try
		{
			//if(chkCGRDS.isSelected())
			//	return;
			if(txtTPRCD.getText().length() != 39)
				{setMSG("Coding incomplete for Grade Description generation",'E'); return;}
			String L_strGRSRL=chkGRSRL(LP_PMRAB+LP_PC0AB);
			String L_strCGRDS_PCC = hstPCCAB.get(txtTPRCD.getText().substring(intPCCCD-2,intPCCCD)).toString();
			String L_strCGRDS_PCS = txtTPRCD.getText().substring(intPCSCD-4,intPCSCD);
			txtCGRDS.setText(LP_PMRAB+LP_PC0AB+L_strGRSRL+" "+L_strCGRDS_PCC+" "+L_strCGRDS_PCS);

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setCGRDS_1");
		}
	}
		

	/**
	 */
	private String chkGRSRL(String LP_GRPFX)
	{
		String L_strRUNNO = "";
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "select distinct SUBSTRING(pr_cgrds,1,8) PR_GRPFX  from PR_PRMST where SUBSTRING(PR_TPRCD,1,32) + SUBSTRING(PR_TPRCD,39,1) = '"+txtTPRCD.getText().substring(0,32)+txtTPRCD.getText().substring(38)+"'";
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				while(true)
				{
					if(!L_rstRSSET.getString("PR_GRPFX").substring(0,4).equals(LP_GRPFX))
						setMSG("Invalid Grade Description "+L_rstRSSET.getString("PR_GRPFX").substring(0,4)+" found in "+LP_GRPFX+" series ...",'E');
					else
						L_strRUNNO = L_rstRSSET.getString("PR_GRPFX").substring(4);
					if(!L_rstRSSET.next())
						break;
				}
			}
			if(L_strRUNNO.length()==4)
				return L_strRUNNO;
			
			M_strSQLQRY = "select distinct SUBSTRING(pr_cgrds,1,8) PR_GRPFX  from PR_PRMST where SUBSTRING(PR_CGRDS,1,4) = '"+LP_GRPFX+"' order by SUBSTRING(pr_cgrds,1,8)";
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if(L_rstRSSET == null || !L_rstRSSET.next())
				return "0001";
			L_strRUNNO = "0000";
			while(true)
			{
				if(Integer.parseInt(L_rstRSSET.getString("PR_GRPFX").substring(4,8))-Integer.parseInt(L_strRUNNO)>1)
					break;
				L_strRUNNO = L_rstRSSET.getString("PR_GRPFX").substring(4,8); 
				if(!L_rstRSSET.next())
					break;
			}
			L_strRUNNO = Integer.toString(Integer.parseInt(L_strRUNNO)+1);
			if(L_strRUNNO.length()<4)
			{
				for(int i=0;i<=4;i++)
				{
					L_strRUNNO = "0"+L_strRUNNO;
					if(L_strRUNNO.length()==4)
						break;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkGRSRL");
		}
		return L_strRUNNO;
	}
	
	/**
	 */
	private void setNEXT_CMB(JComboBox LP_CMBNM)
	{
		try
		{
					LP_CMBNM.setEnabled(true);
					LP_CMBNM.requestFocus();
					setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setNEXT_CMB");
		}
	}

	
	/**
	 */
	private void setNEXT_TXT(JTextField LP_TXTNM)
	{
		try
		{
					LP_TXTNM.setEnabled(true);
					LP_TXTNM.requestFocus();
					setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setNEXT_TXT");
		}
	}
	
	
	
	/**
	 */
	public void focusGained(FocusEvent L_KE)
	{
		super.focusGained(L_KE);
		try
		{
			if(M_objSOURC != btnADDCT)
				{btnADDCT.setVisible(false); lblADDCT.setVisible(false);}
			if(M_objSOURC == txtOPRCD)
				setMSG("Please Enter Old Product Code Or Press F1 to select from List..",'N');
			else if(M_objSOURC == txtCPRCD)
				setMSG("Please Enter Commercial Product Code Or Press F1 to select from List..",'N');
			else if(M_objSOURC == txtCGRDS)
				setMSG("Please Enter Commercial Product Grade.",'N');
			else if(M_objSOURC == txtCPRDS)
				setMSG("Please Enter Commercial Product Code Description..",'N');
			else if(M_objSOURC == txtTPRCD)
				setMSG("Please Enter Technical Product Code Or Press F1 to select from List..",'N');
			else if(M_objSOURC == txtTPRDS)
				setMSG("Please Enter Technical Product Code Description..",'N');
			else if(M_objSOURC == cmbMGRCD)
				setMSG("Please select "+strMGRCD,'N');
			else if(M_objSOURC == cmbPMRCD)
				setMSG("Please select "+strPMRCD,'N');
			else if(M_objSOURC == cmbPC0CD)
				{setMSG("Please select "+strPC0CD,'N');cmbRESIZE(cmbPC0CD,"Enlarge");}
			else if(M_objSOURC == cmbPM0CD)
				{setMSG("Please select Primary "+strPM0CD,'N');cmbRESIZE(cmbPM0CD,"Enlarge");dspADDBTN("PM0CD");}
			else if(M_objSOURC == cmbPA0CD)
				{setMSG("Please select Primary "+strPA0CD,'N');cmbRESIZE(cmbPA0CD,"Enlarge");dspADDBTN("PA0CD");}
			else if(M_objSOURC == cmbPC1CD)
				{setMSG("Please select "+strPC1CD,'N');cmbRESIZE(cmbPC1CD,"Enlarge");}
			else if(M_objSOURC == cmbPM1CD)
				{setMSG("Please select Primary "+strPM1CD,'N');cmbRESIZE(cmbPM1CD,"Enlarge");dspADDBTN("PM1CD");}
			else if(M_objSOURC == cmbPA1CD)
				{setMSG("Please select Primary "+strPA1CD,'N');cmbRESIZE(cmbPA1CD,"Enlarge");dspADDBTN("PA1CD");}
			else if(M_objSOURC == cmbPC2CD)
				{setMSG("Please select "+strPC2CD,'N');cmbRESIZE(cmbPC2CD,"Enlarge");}
			else if(M_objSOURC == cmbPM2CD)
				{setMSG("Please select "+strPM2CD,'N');cmbRESIZE(cmbPM2CD,"Enlarge");dspADDBTN("PM2CD");}
			else if(M_objSOURC == cmbPA2CD)
				{setMSG("Please select "+strPA2CD,'N');cmbRESIZE(cmbPA2CD,"Enlarge");dspADDBTN("PA2CD");}
			else if(M_objSOURC == cmbPCCCD)
				{setMSG("Please select "+strPCCCD,'N');/*cmbRESIZE(cmbPCCCD,"Enlarge");*/dspADDBTN("PCCCD");}
			else if(M_objSOURC == cmbPCSCD)
				{setMSG("Please select "+strPCSCD,'N');/*cmbRESIZE(cmbPCSCD,"Enlarge");*/dspADDBTN("PCSCD");}
			else if(M_objSOURC == cmbPRMCD)
				{setMSG("Please select "+strPRMCD,'N');/*cmbRESIZE(cmbPRMCD,"Enlarge")*/;dspADDBTN("PRMCD");}
			else if(M_objSOURC == cmbPRSCD)
				{setMSG("Please select "+strPRSCD,'N');/*cmbRESIZE(cmbPRSCD,"Enlarge")*/;dspADDBTN("PRSCD");}
			else if(M_objSOURC == cmbQFLCD)
				setMSG("Please select "+strQFLCD,'N');
			else if(M_objSOURC == txtSCPCD)
				setMSG("Please enter "+strSCPCD,'N');
			else if(M_objSOURC == txtCODDS)
				setMSG("Please enter Code Description..",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"FocusGained");
		}
	}

	
	
/**
 */
	private void dspADDBTN(String LP_ADDCT)
	{
		try
		{
			if(LP_ADDCT.equalsIgnoreCase("PM0CD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPM0CD);btnADDCT.setVisible(true);strADDCT="PM0CD";}
			else if(LP_ADDCT.equalsIgnoreCase("PA0CD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPA0CD);btnADDCT.setVisible(true);strADDCT="PA0CD";}
			else if(LP_ADDCT.equalsIgnoreCase("PM1CD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPM1CD);btnADDCT.setVisible(true);strADDCT="PM1CD";}
			else if(LP_ADDCT.equalsIgnoreCase("PA1CD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPA1CD);btnADDCT.setVisible(true);strADDCT="PA1CD";}
			else if(LP_ADDCT.equalsIgnoreCase("PM2CD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPM2CD);btnADDCT.setVisible(true);strADDCT="PM2CD";}
			else if(LP_ADDCT.equalsIgnoreCase("PA2CD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPA2CD);btnADDCT.setVisible(true);strADDCT="PA2CD";}
			else if(LP_ADDCT.equalsIgnoreCase("PCCCD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPCCCD);btnADDCT.setVisible(true);strADDCT="PCCCD";}
			else if(LP_ADDCT.equalsIgnoreCase("PCSCD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPCSCD);btnADDCT.setVisible(true);strADDCT="PCSCD";}
			else if(LP_ADDCT.equalsIgnoreCase("PRMCD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPRMCD);btnADDCT.setVisible(true);strADDCT="PRMCD";}
			else if(LP_ADDCT.equalsIgnoreCase("PRSCD"))
				{lblADDCT.setVisible(true);lblADDCT.setText(strPRSCD);btnADDCT.setVisible(true);strADDCT="PRSCD";}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspADDBTN");
		}
	}
	
	
/**
 */	
private void exeADDNEW()
{
	try
	{
			//System.out.println("strADDCT : "+strADDCT);
			if(strADDCT.equalsIgnoreCase("PM0CD"))
			{
				String L_strPC0CD = "";
				if(cmbPC0CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select"+strPC0CD+" At First..",'E');
					return;
				}
				L_strPC0CD = cmbPC0CD.getSelectedItem().toString().substring(0,2);
				exeADDCAT_PC(strPM0CD,"PC",L_strPC0CD,"xx","0A",2);
				cmbPA0CD.setEnabled(true);
			}										
			else if(strADDCT.equalsIgnoreCase("PA0CD"))
			{
				if(!cmbPA0CD.getSelectedItem().toString().equalsIgnoreCase("Select"))
					exeADDDSP("PC",cmbPC0CD.getSelectedItem().toString().substring(0,2)+cmbPM0CD.getSelectedItem().toString().substring(0,2)+cmbPA0CD.getSelectedItem().toString().substring(0,2));
				String L_strPC0CD = "";
				String L_strPM0CD = "";
				if(cmbPC0CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPA0CD+" At First..",'E');
					return;
				}
				if(cmbPM0CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select Product Category At First..",'E');
					return;
				}
				L_strPM0CD = cmbPM0CD.getSelectedItem().toString().substring(0,2);				
				L_strPC0CD = cmbPC0CD.getSelectedItem().toString().substring(0,2);				
				
				exeADDCAT_PC(strPA0CD,"PC",L_strPC0CD,L_strPM0CD,"xx",3);
				cmbPM1CD.setEnabled(true);
			}	
			if(strADDCT.equalsIgnoreCase("PM1CD"))
			{
				String L_strPC1CD = "";
				if(cmbPC1CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select"+strPC1CD+" At First..",'E');
					return;
				}
				L_strPC1CD = cmbPC1CD.getSelectedItem().toString().substring(0,2);
				exeADDCAT_PC(strPM1CD,"PC",L_strPC1CD,"xx","0A",2);
				cmbPA1CD.setEnabled(true);
			}										
			else if(strADDCT.equalsIgnoreCase("PA1CD"))
			{
				if(!cmbPA1CD.getSelectedItem().toString().equalsIgnoreCase("Select"))
					exeADDDSP("PC",cmbPC1CD.getSelectedItem().toString().substring(0,2)+cmbPM1CD.getSelectedItem().toString().substring(0,2)+cmbPA1CD.getSelectedItem().toString().substring(0,2));
				String L_strPC1CD = "";
				String L_strPM1CD = "";
				if(cmbPC1CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPC1CD+" At First..",'E');
					return;
				}
				if(cmbPM1CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select Product Category At First..",'E');
					return;
				}
				L_strPM1CD = cmbPM1CD.getSelectedItem().toString().substring(0,2);				
				L_strPC1CD = cmbPC1CD.getSelectedItem().toString().substring(0,2);				
				
				exeADDCAT_PC(strPA1CD,"PC",L_strPC1CD,L_strPM1CD,"xx",2);
				cmbPM2CD.setEnabled(true);
			}	
			else if(strADDCT.equalsIgnoreCase("PM2CD"))
			{
				String L_strPC2CD = "";
				if(cmbPC2CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPC2CD+" At First..",'E');
					return;
				}
				L_strPC2CD = cmbPC2CD.getSelectedItem().toString().substring(0,2);
				exeADDCAT_PC(strPM2CD,"PC",L_strPC2CD,"xx","0A",2);
				cmbPA2CD.setEnabled(true);
			}										
			else if(strADDCT.equalsIgnoreCase("PA2CD"))
			{
				if(!cmbPA2CD.getSelectedItem().toString().equalsIgnoreCase("Select"))
					exeADDDSP("PC",cmbPC2CD.getSelectedItem().toString().substring(0,2)+cmbPM2CD.getSelectedItem().toString().substring(0,2)+cmbPA2CD.getSelectedItem().toString().substring(0,2));
				String L_strPC2CD = "";
				String L_strPM2CD = "";
				if(cmbPC2CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPC2CD+" At First..",'E');
					return;
				}
				if(cmbPM2CD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPM2CD+" At First..",'E');
					return;
				}
				L_strPM2CD = cmbPM2CD.getSelectedItem().toString().substring(0,2);				
				L_strPC2CD = cmbPC2CD.getSelectedItem().toString().substring(0,2);
					
				exeADDCAT_PC(strPA2CD,"PC",L_strPC2CD,L_strPM2CD,"xx",3);
				txtPRPCD.setEnabled(true);
			}	
			else if(strADDCT.equalsIgnoreCase("PCCCD"))
			{
				exeADDCAT1(strPCCCD,"CS","xx","000A",1);
				cmbPCSCD.setEnabled(true);
			}										
			else if(strADDCT.equalsIgnoreCase("PCSCD"))
			{
				String L_strPCCCD = "";
				if(cmbPCCCD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPCCCD+" At First..",'E');
					return;
				}
				L_strPCCCD = cmbPCCCD.getSelectedItem().toString().substring(0,2);				
				exeADDCAT1(strPCSCD,"CS",L_strPCCCD,"xxxx",2);
				cmbPRMCD.setEnabled(true);
			}										
			else if(strADDCT.equalsIgnoreCase("PRMCD"))
			{
				String L_strPMRCD = "";
				if(cmbPMRCD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPMRCD+" At First..",'E');
					return;
				}
				L_strPMRCD = cmbPMRCD.getSelectedItem().toString().substring(0,2);
				exeADDCAT_RF("Base Poly.Ref Main Code : ","RF",L_strPMRCD,"xx","0A",2);
				cmbPRSCD.setEnabled(true);
			}										
			else if(strADDCT.equalsIgnoreCase("PRSCD"))
			{
				if(!cmbPRSCD.getSelectedItem().toString().equalsIgnoreCase("Select"))
					exeADDDSP("RF",cmbPMRCD.getSelectedItem().toString().substring(0,2)+cmbPRMCD.getSelectedItem().toString().substring(0,2)+cmbPRSCD.getSelectedItem().toString().substring(0,2));
				String L_strPMRCD = "";
				String L_strPRMCD = "";
				if(cmbPMRCD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPMRCD+" At First..",'E');
					return;
				}
				if(cmbPRMCD.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please select "+strPRMCD+" At First..",'E');
					return;
				}
				L_strPMRCD = cmbPMRCD.getSelectedItem().toString().substring(0,2);				
				L_strPRMCD = cmbPRMCD.getSelectedItem().toString().substring(0,2);
					
				exeADDCAT_RF("Base Pol.Ref.Sub-code ","RF",L_strPMRCD,L_strPRMCD,"xx",3);
				cmbQFLCD.setEnabled(true);
			}	
				setCursor(cl_dat.M_curDFSTS_pbst);
	}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeADDNEW");
		}
	}





    /**
     */
	public void keyPressed(KeyEvent L_KE)
	{ 
		super.keyPressed(L_KE);		
		if((L_KE.getKeyCode()== L_KE.VK_F1))
		{
			if(M_objSOURC == txtTPRCD)
			{
				txtTPRDS.setText("");
				M_strHLPFLD = "txtTPRCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				setCursor(cl_dat.M_curWTSTS_pbst);								
				M_strSQLQRY =" SELECT PR_TPRCD,PR_TPRDS,PR_OPRCD,PR_OPRDS,PR_CPRCD,PR_CGRDS,PR_CPRDS from PR_PRMST where isnull(PR_STSFL,' ') <> 'X'";
				if(txtTPRCD.getText().trim().length()>0)
					M_strSQLQRY += " AND PR_TPRCD like '"+ txtTPRCD.getText().trim()+"%'";
				M_strSQLQRY += " order by PR_TPRCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Technical Code","Desc","Old Code","Desc","Com.Code","Com.Grade","Desc"},7,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(M_objSOURC == txtTPRDS)
			{
				//txtTPRDS.setText("");
				M_strHLPFLD = "txtTPRDS";
				cl_dat.M_flgHELPFL_pbst = true;	
				setCursor(cl_dat.M_curWTSTS_pbst);								
				M_strSQLQRY =" SELECT distinct PR_TPRDS from PR_PRMST where isnull(PR_STSFL,' ') <> 'X'";
				if(txtTPRDS.getText().trim().length()>0)
					M_strSQLQRY += " AND PR_TPRDS like '"+ txtTPRDS.getText().trim()+"%'";
				M_strSQLQRY += " order by PR_TPRDS";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Technical Descr"},1,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtCPRCD)
			{
				txtCPRDS.setText("");
				M_strHLPFLD = "txtCPRCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY =" SELECT PR_CPRCD,PR_CGRDS,PR_CPRDS,PR_OPRCD,PR_OPRDS,PR_TPRCD,PR_TPRDS from PR_PRMST where isnull(PR_STSFL,' ') <> 'X'";				
				if(txtCPRCD.getText().trim().length()>0)
					M_strSQLQRY += " AND PR_CPRCD like '"+ txtCPRCD.getText().trim()+"%'";
				M_strSQLQRY += " order by PR_CPRCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Comm.Code","Grade","Description","Old Code","Desc","Technical Code","Desc"},7,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_objSOURC == txtCPRDS)
			{
				//txtCPRDS.setText("");
				M_strHLPFLD = "txtCPRDS";
				cl_dat.M_flgHELPFL_pbst = true;	
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY =" SELECT distinct PR_CPRDS from PR_PRMST where isnull(PR_STSFL,' ') <> 'X'";				
				if(txtCPRDS.getText().trim().length()>0)
					M_strSQLQRY += " AND PR_CPRDS like '"+ txtCPRDS.getText().trim()+"%'";
				M_strSQLQRY += " order by PR_CPRDS";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Comm.Description"},1,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_objSOURC == txtOPRCD)
			{
				txtOPRDS.setText("");
				M_strHLPFLD = "txtOPRCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
				{
					M_strSQLQRY =" SELECT PR_PRDCD,PR_PRDDS from CO_PRMST where isnull(Pr_STSFL,' ') <> 'X'";
					if(txtPRMCD.getText().trim().length()==2)
						M_strSQLQRY += " AND PR_PRDCD like '____"+ txtPRMCD.getText().trim()+"%'";
					if(txtPRSCD.getText().trim().length() == 2)
						M_strSQLQRY += " AND PR_PRDCD like '______"+ txtPRSCD.getText().trim()+"%'";
					if(txtOPRCD.getText().length()>0)
						M_strSQLQRY += " AND PR_PRDCD like '"+txtOPRCD.getText().trim()+"%'";
					M_strSQLQRY += " order by PR_PRDCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"CT");
				}
				else 
				{
					M_strSQLQRY =" SELECT PR_OPRCD,PR_OPRDS,PR_TPRCD,PR_TPRDS,PR_CPRCD,PR_CGRDS,PR_CPRDS from PR_PRMST where isnull(PR_STSFL,' ') <> 'X'";				
					if(txtCPRCD.getText().trim().length()>0)
						M_strSQLQRY += " AND PR_CPRCD ='"+ txtCPRCD.getText().trim()+"'";
					M_strSQLQRY += " order by PR_TPRCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Old Code","Description","Technical Code","Desc","Comm.Code","Grade","Desc"},7,"CT");
				}				
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtPRMCD)
			{
				//SELECT  substring(PR_PRDCD,5,2) PRDCD,MIN(PR_PRDDS) PRDDS from CO_PRMST where ifnull(PR_STSFL,' ') <> 'X'  AND SUBSTRING(PR_PRDCD,1,4)='5111'  GROUP BY substring(PR_PRDCD,5,2)
				//UNION SELECT SUBSTRING(PR_TPRCD,27,2) PRDCD,MIN(PR_TPRDS) PRDDS FROM PR_PRMST WHERE SUBSTRING(PR_TPRCD,1,4)='5101' GROUP BY SUBSTRING(PR_TPRCD,27,2) ORDER BY PRDCD;
				//if(txtPRMCD.getText().length()>0)
				//	M_strSQLQRY += " AND PR_PRDCD like '____"+txtPRMCD.getText().trim()+"%'";
				/** Product ref.main code is taken from old product code polystyrene (5th & 6th digit) and from 
				 *  current tech.product code category (27th & 28th digit) and corresponding category description, if available is 
				 * picked up from product code work table (pr_cdwrk RF/xxxx0A)
				 */
				M_strHLPFLD = "txtPRMCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				setCursor(cl_dat.M_curWTSTS_pbst);								
				M_strSQLQRY ="";				

				//if(txtOPRCD.getText().trim().length()==10)
	            //     M_strSQLQRY += "SELECT SUBSTRING(PR_PRDCD,5,2) PRDCD,CD_HDRDS, MIN(PR_PRDDS) PR_PRDDS FROM CO_PRMST  left outer join pr_cdwrk  on CD_HDRCT='RF' and CD_HDRCD=SUBSTRING(PR_PRDCD,3,2)||SUBSTRING(PR_PRDCD,5,2) ||'0A' where ifnull(PR_STSFL,' ') <> 'X' AND SUBSTRING(pr_prdcd,1,2) = '51' group by SUBSTRING(PR_PRDCD,5,2),cd_hdrds UNION ";
				//else
	            //     M_strSQLQRY += "SELECT SUBSTRING(PR_PRDCD,5,2) PRDCD,'      ' CD_HDRDS, MIN(PR_PRDDS) PR_PRDDS FROM CO_PRMST   where ifnull(PR_STSFL,' ') <> 'X' and SUBSTRING(pr_prdcd,1,2) = '51'  group by SUBSTRING(PR_PRDCD,5,2),'      ' UNION ";
				//if(txtTPRCD.getText().substring(4,6).equals("00"))
				//	M_strSQLQRY = "SELECT SUBSTRING(PR_PRDCD,5,2) PRDCD, '  ' CD_HDRDS, MIN(PR_PRDDS) PRDDS  FROM CO_PRMST where SUBSTRING(pr_prdcd,1,4)='"+txtOPRCD.getText().substring(0,4)+"' group by SUBSTRING(PR_PRDCD,5,2), '  ' order by SUBSTRING(PR_PRDCD,5,2)";
				//else
				//	M_strSQLQRY = "SELECT SUBSTRING(PR_TPRCD,27,2) PRDCD, '  ' CD_HDRDS, MIN(PR_TPRDS) PRDDS  FROM PR_PRMST  WHERE SUBSTRING(PR_TPRCD,3,2)='"+txtTPRCD.getText().trim().substring(2,4)+"' GROUP BY SUBSTRING(PR_TPRCD,27,2),'  '  ORDER BY SUBSTRING(PR_TPRCD,27,2)";
					//M_strSQLQRY = "SELECT SUBSTRING(PR_TPRCD,27,2) PRDCD, CD_HDRDS, MIN(PR_TPRDS) PRDDS  FROM PR_PRMST left outer join pr_cdwrk on CD_HDRCT='RF' and CD_HDRCD=SUBSTRING(PR_TPRCD,3,2)||SUBSTRING(PR_TPRCD,27,2) ||'0A'  WHERE SUBSTRING(PR_TPRCD,3,2)='"+txtTPRCD.getText().trim().substring(2,4)+"' GROUP BY SUBSTRING(PR_TPRCD,27,2),CD_HDRDS  ORDER BY PRDCD";
				M_strSQLQRY = "SELECT SUBSTRING(CD_HDRCD,3,2) CD_HDRCD, CD_HDRDS FROM PR_CDWRK where cd_hdrct = 'RF' and CD_HDRCD like '"+cmbPMRCD.getSelectedItem().toString().substring(0,2)+"__0A'  and CD_SUBCD = '00' order by SUBSTRING(CD_HDRCD,3,2)";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Code","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtPRSCD)
			{				
				M_strHLPFLD = "txtPRSCD";
				cl_dat.M_flgHELPFL_pbst = true;	
				setCursor(cl_dat.M_curWTSTS_pbst);								
				M_strSQLQRY ="";				
				M_strSQLQRY = "SELECT SUBSTRING(CD_HDRCD,5,2) CD_HDRCD, CD_HDRDS FROM PR_CDWRK where cd_hdrct = 'RF' and SUBSTRING(CD_HDRCD,1,4) = '"+cmbPMRCD.getSelectedItem().toString().substring(0,2)+txtPRMCD.getText()+"' and SUBSTRING(cd_hdrcd,5,2)<>'0A'  and CD_SUBCD = '00' order by SUBSTRING(CD_HDRCD,5,2)";
				
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Code","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		else if((L_KE.getKeyCode()== L_KE.VK_ENTER))
		{
			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{
				if(M_objSOURC == txtTPRCD)
				{
					if(txtTPRCD.getText().trim().length() == intQFLCD)
						getDATA();
					else 
						setMSG("Please Enter valid Technical Code ..",'E');
				}
				else if(M_objSOURC == txtCPRCD)
				{
					if(txtTPRCD.getText().trim().length() == intQFLCD)
						getDATA();
					else
						setMSG("Please Enter valid Technical Code ..",'E');
				}
				else if(M_objSOURC == txtOPRCD)
				{
					if(txtTPRCD.getText().trim().length() == intQFLCD)
						getDATA();
					else 
						setMSG("Please Enter valid Technical Code ..",'E');
				}
			}
			else if(M_objSOURC == txtTPRDS)
			{
				txtCPRDS.requestFocus();
				txtCPRDS.setEnabled(true);
			}
			//else if(M_objSOURC == txtCGRDS)
			//{
			//	txtCPRDS.requestFocus();
			//	txtCPRDS.setEnabled(true);
			//}
			else if(M_objSOURC == txtCPRDS)
			{
				txtOPRCD.requestFocus();
				txtOPRCD.setEnabled(true);
			}
			else if(M_objSOURC == txtOPRCD)
			{
				try
				{
					//if(txtOPRCD.getText().trim().length()==10)
					//{
						setNEXT_CMB(cmbMGRCD);
						String L_strTEMP = "";	
						setCursor(cl_dat.M_curWTSTS_pbst);						
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'MST'";
						M_strSQLQRY += " AND CMT_CGSTP = 'PRXXPMG' AND isnull(CMT_STSFL,'')<> 'X'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						cmbMGRCD.removeAllItems();						
						if(M_rstRSSET != null)
						{							
							cmbMGRCD.addItem("Select");
							while(M_rstRSSET.next())							
								cmbMGRCD.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
							M_rstRSSET.close();
						}						
						setCursor(cl_dat.M_curDFSTS_pbst);
						//}	else{}
							
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"M_objSOURC == txtOPRCD");
				}
			}
			/*else if(M_objSOURC == txtOPRCD)
			{
				if(txtOPRCD.getText().trim().length() == 10)				
					cmbMGRCD.requestFocus();
				else
					setMSG("Please Enter valid Product code Or Press F1 to select from List..",'E');
			}*/
			else if(M_objSOURC == txtCODE)
			{
				if(txtCODE.getText().trim().length()==2)
					txtCODDS.requestFocus();
				else
					setMSG("Please Enter vallid Code or Press Fq to select from List..",'E');
			}
			else if(M_objSOURC == txtCODE1)
			{
				if(txtCODE1.getText().trim().length() == 4)
					txtCODDS.requestFocus();
				else
					setMSG("Please Enter valid Code Or Press F1 to select from List..",'E');
			}
			if(M_objSOURC == cmbMGRCD)
			{
				if(cmbMGRCD.getSelectedIndex()>0)
					cmbPMRCD.requestFocus();
			}
			else if(M_objSOURC == cmbPMRCD)
			{
				if(cmbPMRCD.getSelectedIndex()>0)
					cmbPC0CD.requestFocus();
			}
			else if(M_objSOURC == cmbPC0CD)
			{
				if(cmbPC0CD.getSelectedIndex()>0)
					cmbPM0CD.requestFocus();
			}
			else if(M_objSOURC == cmbPM0CD)
			{
				if(cmbPM0CD.getSelectedIndex()>0)
					cmbPA0CD.requestFocus();
			}
			else if(M_objSOURC == cmbPA0CD)
			{
				if(cmbPA0CD.getSelectedIndex()>0)
					txtPC1CD.requestFocus();
			}
			else if(M_objSOURC == cmbPC1CD)
			{
				if(cmbPC1CD.getSelectedIndex()>0)
					cmbPM1CD.requestFocus();
			}
			else if(M_objSOURC == cmbPM1CD)
			{
				if(cmbPM1CD.getSelectedIndex()>0)
					cmbPA1CD.requestFocus();
			}
			else if(M_objSOURC == cmbPA1CD)
			{
				if(cmbPA1CD.getSelectedIndex()>0)
					txtPC2CD.requestFocus();
			}
			else if(M_objSOURC == cmbPC2CD)
			{
				if(cmbPC2CD.getSelectedIndex()>0)
					cmbPM2CD.requestFocus();
			}
			else if(M_objSOURC == cmbPM2CD)
			{
				if(cmbPM2CD.getSelectedIndex()>0)
					cmbPA2CD.requestFocus();
			}
			else if(M_objSOURC == cmbPA2CD)
			{
				if(cmbPA2CD.getSelectedIndex()>0)
					txtBSPCD.requestFocus();
			}
			else if(M_objSOURC == txtBSPCD)
			{
				if(txtBSPCD.getText().length() == 2)
					txtPRPCD.requestFocus();
			}
			else if(M_objSOURC == txtPRPCD)
			{
				if(txtPRPCD.getText().length() == 2)
					txtSCPCD.requestFocus();
			}
			else if(M_objSOURC == txtSCPCD)
			{
				if(txtSCPCD.getText().length() == 2)
					cmbPCCCD.requestFocus();
			}
			else if(M_objSOURC == cmbPCCCD)
			{
				if(cmbPCCCD.getSelectedIndex()>0)
					cmbPCSCD.requestFocus();
			}
			else if(M_objSOURC == cmbPCSCD)
			{
				if(cmbPCSCD.getSelectedIndex()>0)
					txtPRMCD.requestFocus();
			}
			else if(M_objSOURC == txtPRMCD)
			{
				if(txtPRMCD.getText().length() == 2)
					txtPRSCD.requestFocus();
			}
			else if(M_objSOURC == txtPRSCD)
			{
				if(txtPRSCD.getText().trim().length() == 2)
					cmbQFLCD.requestFocus();
			}
			else if(M_objSOURC == cmbQFLCD)
			{
				if(cmbQFLCD.getSelectedIndex()>0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}			
		}
	}
	/**
	 * Super class Method to execuate the F1 help.
	 */
	public void exeHLPOK()
	{		
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtTPRCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);			
			txtTPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{
				txtOPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));
				txtOPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)));			
				txtCPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)));
				txtCGRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)));			
				txtCPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),6)));			
			}
		}
		if(M_strHLPFLD.equals("txtTPRDS"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTPRDS.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD.equals("txtCPRCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtCPRCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtCGRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			txtCPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{
				txtOPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));
				txtOPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)));
				txtTPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)));
				txtTPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)));
			}
		}		
		if(M_strHLPFLD.equals("txtCPRDS"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtCPRDS.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD.equals("txtOPRCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;			
			txtOPRCD.setText(cl_dat.M_strHLPSTR_pbst);			
			txtOPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{				
				txtTPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));				
				txtTPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)));				
				txtCPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)));				
				txtCGRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)));
				txtCPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),6)));
			}
		}
		//else if(M_strHLPFLD.equals("txtPRMCD"))
		//{
		//	cl_dat.M_flgHELPFL_pbst = false;
		//	txtPRMCD.setText(cl_dat.M_strHLPSTR_pbst);
		//}
		//else if(M_strHLPFLD.equals("txtPRSCD"))
		//{
		//	cl_dat.M_flgHELPFL_pbst = false;
		//	txtPRSCD.setText(cl_dat.M_strHLPSTR_pbst);
		//}
	}	
	/**
	 * Method to Enable & disable the Components according to the codes Specified.
	 * @param P_cmbSELCT JTextField teferance to pass selected JTexstField.
	 */
	void setENBLE(JTextField P_cmbSELCT)
	{
		cmbQFLCD.removeAllItems();
		cmbPRSCD.removeAllItems();
		cmbPRMCD.removeAllItems();
		cmbPCCCD.removeAllItems();
		cmbPCSCD.removeAllItems();
		cmbQFLCD.setEnabled(false);			
		cmbPRSCD.setEnabled(false);			
		cmbPRMCD.setEnabled(false);			
		cmbPCSCD.setEnabled(false);			
		cmbPCCCD.setEnabled(false);			
		if(txtSCPCD == P_cmbSELCT)
			return;
		txtSCPCD.setText("");
		txtSCPCD.setEnabled(false);		
		if(txtPRPCD == P_cmbSELCT)
			return;
		//txtPRPCD.setText("");
		//txtPRPCD.setEnabled(false);		
	}
	/**
	 * Method to Enable & disable the Components according to the codes Specified.
	 * @param P_cmbSELCT JComboBox teferance to pass selected JTexstField.
	 */
	void setENBLE(JComboBox P_cmbSELCT)
	{		
		if(cmbQFLCD == P_cmbSELCT)		
			return;		
		cmbQFLCD.removeAllItems();
		cmbQFLCD.setEnabled(false);			
		if(cmbPCSCD == P_cmbSELCT)		
			return;		
		cmbPCSCD.removeAllItems();
		cmbPCSCD.setEnabled(false);
		if(cmbPCCCD == P_cmbSELCT)		
			return;		
		cmbPCCCD.removeAllItems();
		cmbPCCCD.setEnabled(false);
		txtSCPCD.setText("");
		txtSCPCD.setEnabled(false);		
		txtPRPCD.setText("");
		txtPRPCD.setEnabled(false);
		if(cmbPA2CD == P_cmbSELCT)		
			return;		
		cmbPA2CD.removeAllItems();
		cmbPA2CD.setEnabled(false);
		if(cmbPM2CD == P_cmbSELCT)		
			return;		
		cmbPM2CD.removeAllItems();
		cmbPM2CD.setEnabled(false);
		if(cmbPC2CD == P_cmbSELCT)		
			return;		
		cmbPC2CD.removeAllItems();
		cmbPC2CD.setEnabled(false);
		if(cmbPA1CD == P_cmbSELCT)		
			return;		
		cmbPA1CD.removeAllItems();
		cmbPA1CD.setEnabled(false);
		if(cmbPM1CD == P_cmbSELCT)		
			return;		
		cmbPM1CD.removeAllItems();
		cmbPM1CD.setEnabled(false);
		if(cmbPC1CD == P_cmbSELCT)		
			return;		
		cmbPC1CD.removeAllItems();
		cmbPC1CD.setEnabled(false);
		if(cmbPA0CD == P_cmbSELCT)		
			return;		
		cmbPA0CD.removeAllItems();
		cmbPA0CD.setEnabled(false);
		if(cmbPM0CD == P_cmbSELCT)		
			return;		
		cmbPM0CD.removeAllItems();
		cmbPM0CD.setEnabled(false);
		if(cmbPC0CD == P_cmbSELCT)		
			return;		
		cmbPC0CD.removeAllItems();
		cmbPC0CD.setEnabled(false);
		if(cmbPRSCD == P_cmbSELCT)		
			return;		
		cmbPRSCD.removeAllItems();
		cmbPRSCD.setEnabled(false);
		if(cmbPRMCD == P_cmbSELCT)		
			return;		
		cmbPRMCD.removeAllItems();
		cmbPRMCD.setEnabled(false);
		if(cmbPMRCD == P_cmbSELCT)		
			return;		
		cmbPMRCD.removeAllItems();
		cmbPMRCD.setEnabled(false);		
		this.setCursor(cl_dat.M_curDFSTS_pbst);		
	}
	
	
	/**
	 * Method to insert, update & delete Data to / from the database.
	 */
	void exeSAVE()
	{
		if (!vldDATA())
			return;	
		else 
			setMSG("",'E');
		try
		{
			String L_strSRLNO ="";
			int L_intSRLNO = 0;
			// only for exprimental grade has to uppend year digit
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{
				cl_dat.M_flgLCUPD_pbst = true;	
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				getCPRCD();
				L_strSRLNO = txtCPRCD.getText().substring(4,10);
				//System.out.println("L_strSRLNO : "+L_strSRLNO);
				M_strSQLQRY = "Insert into PR_PRMST (PR_OPRCD,PR_OPRDS,PR_CPRCD,PR_CGRDS,PR_CPRDS,PR_TPRCD,PR_TPRDS";//,PR_EXCCT
				M_strSQLQRY += ",PR_TRNFL,PR_STSFL,PR_LUSBY,PR_LUPDT) values ("; 
				M_strSQLQRY += "'"+ txtOPRCD.getText().trim() +"',";
				M_strSQLQRY += "'"+ txtOPRDS.getText().trim() +"',";
				M_strSQLQRY += "'"+ txtCPRCD.getText().trim() +"',";// //L_strSRLNO
				M_strSQLQRY += "'"+ txtCGRDS.getText().trim() +"',";
				M_strSQLQRY += "'"+ txtCPRDS.getText().trim() +"',";
				M_strSQLQRY += "'"+ txtTPRCD.getText().trim() +"',";
				M_strSQLQRY += "'"+ txtTPRDS.getText().trim() +"',";				
				M_strSQLQRY += "'0',";
				M_strSQLQRY += "'I',";
				M_strSQLQRY += "'" + cl_dat.M_strUSRCD_pbst+ "','";
				M_strSQLQRY += M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"')";
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst == true)
				{
					
					M_strSQLQRY = "Update CO_CDTRN set CMT_CCSVL ='"+ L_strSRLNO +"'";						
					M_strSQLQRY +=" where CMT_CGMTP = 'DOC'AND CMT_CGSTP ='PRXXPSL'";
					M_strSQLQRY +=" AND CMT_CODCD = '"+ txtTPRCD.getText().trim().substring(0,4) +"'";
					M_strSQLQRY +=" AND isnull(CMT_STSFL,'')<>'X'";				
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;	
				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG(" Data Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					setMSG(" Data Modified Successfully..",'N'); 
				clrCOMP_1();
				setENBL(true);
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				   setMSG("Error in Modification..",'E'); 
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"exeADDREC");
		}	
	}


	/** Restoring default Key Values after clearing components 
	 * on the entry screen
	 */
	private void clrCOMP_1()
	{
		try
		{
			String L_strOPRCD = txtOPRCD.getText();
			String L_strOPRDS = txtOPRDS.getText();
			String L_strCPRCD = txtCPRCD.getText();
			String L_strCGRDS = txtCGRDS.getText();
			String L_strCPRDS = txtCPRDS.getText();
			String L_strTPRCD = txtTPRCD.getText();
			String L_strTPRDS = txtTPRDS.getText();
			clrCOMP();
			txtOPRCD.setText(L_strOPRCD);
			txtOPRDS.setText(L_strOPRDS);
			txtCPRCD.setText(L_strCPRCD);
			txtCGRDS.setText(L_strCGRDS);
			txtCPRDS.setText(L_strCPRDS);
			txtTPRCD.setText(L_strTPRCD);
			txtTPRDS.setText(L_strTPRDS);
			lblPRMDS.setText("");
			lblPRSDS.setText("");
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP_1");}
	}
	
	
	
	
	
	/**
	 */
	private void getCPRCD()
	{
	try
	{
			if(txtTPRCD.getText().length()<4)
				return;

			String L_strSRLNO ="";
			int L_intSRLNO = 0;
			M_strSQLQRY = "Select CMT_CCSVL from CO_CDTRN where CMT_CGMTP = 'DOC'";
			M_strSQLQRY +=" AND CMT_CGSTP ='PRXXPSL' AND CMT_CODCD = '"+ txtTPRCD.getText().trim().substring(0,4) +"'";
			M_strSQLQRY +=" AND isnull(CMT_STSFL,'')<>'X'";				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{
				if(M_rstRSSET.next())					
					L_strSRLNO = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");					
				M_rstRSSET.close();
			}				
			L_intSRLNO = Integer.parseInt(L_strSRLNO);
			L_intSRLNO++;
			L_strSRLNO = String.valueOf(L_intSRLNO).toString();	
			//System.out.println(L_strSRLNO);
			// for Exprimental grade first char represents the Year Digit
			L_strSRLNO = "000000".substring(0,6 - L_strSRLNO.length())+L_strSRLNO;
			if(txtTPRCD.getText().length()>6)
				if(txtTPRCD.getText().trim().substring(4,6).equals("80"))
					L_strSRLNO = cl_dat.M_strFNNYR_pbst.substring(3,4)+"0000".substring(0,5 - L_strSRLNO.length())+L_strSRLNO;
			if(txtCPRCD.getText().length()==4)
			{
				txtCPRCD.setText(txtCPRCD.getText() + L_strSRLNO);
			}
	
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getCPRCD");
		}	
	}
	
	
	/** 
	 * Method to validate the Inputs before execuastion of the SQL Queries.
	 */
	boolean vldDATA()
	{
		try
		{		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(txtTPRDS.getText().trim().length() == 0)
				{
					setMSG("Please Enter Technical Code Description..",'E');
					txtTPRDS.requestFocus();
					return false;
				}					
				
				if(txtCGRDS.getText().trim().length() == 0)
				{
					setMSG("Please Enter Commercial Grade..",'E');
					txtCGRDS.requestFocus();
					return false;
				}					
				if(txtCPRDS.getText().trim().length() == 0)
				{
					setMSG("Please Enter Commercial code Description..",'E');
					txtCPRDS.requestFocus();
					return false;
				}					
				else if(txtTPRCD.getText().trim().length()!= intQFLCD)
				{
					int L_intCODLN = txtTPRCD.getText().trim().length();
					switch (L_intCODLN)
					{
						case 0:
							cmbMGRCD.requestFocus();							
							setMSG("Please Select "+strMGRCD,'E');
							break;
						case 2:
							cmbPRMCD.requestFocus();
							setMSG("Please Enter "+strPRMCD,'E');
							break;
						case 4:
							cmbPRSCD.requestFocus();
							setMSG("Please Enter "+strPRSCD,'E');
							break;
						case 6:
							cmbPMRCD.requestFocus();
							setMSG("Please Select "+strPMRCD,'E');
							break;
						case 8:
							cmbPC0CD.requestFocus();
							setMSG("Please Select "+strPC0CD,'E');
							break;
						case 10:
							cmbPM0CD.requestFocus();
							setMSG("Please Select "+strPM0CD,'E');
							break;
						case 12:
							cmbPA0CD.requestFocus();
							setMSG("Please Select "+strPA0CD,'E');
							break;
						case 14:
							cmbPC1CD.requestFocus();
							setMSG("Please Select "+strPC1CD,'E');
							break;
						case 16:
							cmbPM1CD.requestFocus();
							setMSG("Please Select "+strPM1CD,'E');
							break;
						case 18:
							cmbPA1CD.requestFocus();
							setMSG("Please Select "+strPA1CD,'E');
							break;
						case 20:
							cmbPC2CD.requestFocus();
							setMSG("Please Select "+strPC2CD,'E');
							break;
						case 22:
							cmbPM2CD.requestFocus();
							setMSG("Please Select "+strPM2CD,'E');
							break;
						case 24:
							cmbPA2CD.requestFocus();
							setMSG("Please Select "+strPA2CD,'E');
							break;
						case 26:
							txtBSPCD.requestFocus();
							setMSG("Please Enter "+strBSPCD,'E');
							break;
						case 28:
							txtPRPCD.requestFocus();
							setMSG("Please Enter "+strPRPCD,'E');
							break;
						case 30:
							txtSCPCD.requestFocus();
							setMSG("Please Enter "+strSCPCD,'E');
							break;
						case 32:
							cmbPCCCD.requestFocus();
							setMSG("Please select "+strPCCCD,'E');
							break;
						case 36:
							cmbPCSCD.requestFocus();
							setMSG("Please Select "+strPCSCD,'E');
							break;
						case 38:
							cmbQFLCD.requestFocus();
							setMSG("Please Select "+strQFLCD,'E');
							break;					
					}
					//setMSG("Please Select All Fields to generate the Code Of length 39..",'E');
					//txtTPRCD.requestFocus();
					return false;
				}
				else if(txtTPRDS.getText().trim().length()== 0)
				{
					setMSG("Please Enter Techcinal Code Description..",'E');
					txtTPRDS.requestFocus();
					return false;
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
	/**
	 * Method to fetch Tanker Details from the database & display it in the Table.
	 */
	private void getDATA()
	{
		try
		{ 
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strTEMP="";
			strTPRCD = txtTPRCD.getText().trim();
			L_strTEMP = strTPRCD.substring(intMGRCD-2,intMGRCD);
			M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'MST'";
			M_strSQLQRY += " AND CMT_CGSTP = 'PRXXPMG' AND isnull(CMT_STSFL,'')<> 'X'";
			M_strSQLQRY += " AND CMT_CODCD = '"+ L_strTEMP+ "'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);		
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtMGRCD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			
			L_strTEMP = strTPRCD.substring(intPMRCD-2,intPMRCD);
			M_strSQLQRY = "Select CMT_CODDS  from CO_CDTRN where CMT_CGMTP = 'MST'";
			M_strSQLQRY += " AND CMT_CGSTP = 'PRXXPMR' AND isnull(CMT_STSFL,'')<> 'X'";			
			M_strSQLQRY += " AND CMT_CODCD = '"+ L_strTEMP +"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);		
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPMRCD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			
			L_strTEMP = strTPRCD.substring(intPC0CD-2,intPC0CD);
			M_strSQLQRY = "Select CMT_CODDS,CMT_CGSTP from CO_CDTRN where CMT_CGMTP = 'MST'";
			M_strSQLQRY += " AND CMT_CGSTP ='PRXXPCT' AND isnull(CMT_STSFL,'')<> 'X'";
			M_strSQLQRY += " AND CMT_CODCD = '"+ L_strTEMP +"'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPC0CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
	
			L_strTEMP = strTPRCD.substring(intPM0CD-2,intPM0CD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
			M_strSQLQRY += " AND CD_HDRCD = '"+ strTPRCD.substring(intPC0CD-2,intPM0CD)+"0A'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPM0CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}	
			
			L_strTEMP = strTPRCD.substring(intPA0CD-2,intPA0CD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
			M_strSQLQRY += " AND CD_HDRCD = '"+ strTPRCD.substring(intPC0CD-2,intPA0CD)+"'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPA0CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}						
			L_strTEMP = strTPRCD.substring(intPC1CD-2,intPC1CD);
			M_strSQLQRY = "Select CMT_CODDS,CMT_CGSTP from CO_CDTRN where CMT_CGMTP = 'MST'";
			M_strSQLQRY += " AND CMT_CGSTP ='PRXXPCT' AND isnull(CMT_STSFL,'')<> 'X'";
			M_strSQLQRY += " AND CMT_CODCD = '"+ L_strTEMP +"'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPC1CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			
			L_strTEMP = strTPRCD.substring(intPM1CD-2,intPM1CD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
			M_strSQLQRY += " AND CD_HDRCD = '"+ strTPRCD.substring(intPC1CD-2,intPC1CD)+strTPRCD.substring(intPM1CD-2,intPM1CD)+"0A'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPM1CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}	
			
			L_strTEMP = strTPRCD.substring(intPA1CD-2,intPA1CD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
			M_strSQLQRY += " AND CD_HDRCD = '"+ strTPRCD.substring(intPC1CD-2,intPC1CD)+strTPRCD.substring(intPM1CD-2,intPA1CD)+"'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPA1CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}						
			L_strTEMP = strTPRCD.substring(intPC2CD-2,intPC2CD);
			M_strSQLQRY = "Select CMT_CODDS,CMT_CGSTP from CO_CDTRN where CMT_CGMTP = 'MST'";
			M_strSQLQRY += " AND CMT_CGSTP ='PRXXPCT' AND isnull(CMT_STSFL,'')<> 'X'";
			M_strSQLQRY += " AND CMT_CODCD = '"+ L_strTEMP +"'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPC2CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			
			L_strTEMP = strTPRCD.substring(intPM2CD-2,intPM2CD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
			M_strSQLQRY += " AND CD_HDRCD = '"+ strTPRCD.substring(intPC2CD-2,intPC2CD)+strTPRCD.substring(intPM2CD-2,intPM2CD)+"0A'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPM2CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}	
			
			L_strTEMP = strTPRCD.substring(intPA2CD-2,intPA2CD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='PC'";
			M_strSQLQRY += " AND CD_HDRCD = '"+ strTPRCD.substring(intPC2CD-2,intPC2CD)+strTPRCD.substring(intPM2CD-2,intPA2CD)+"'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPA2CD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}						
			
			txtBSPCD.setText(strTPRCD.substring(intBSPCD-2,intBSPCD));			
			txtPRPCD.setText(strTPRCD.substring(intPRPCD-2,intPRPCD));			
			txtSCPCD.setText(strTPRCD.substring(intSCPCD-2,intSCPCD));
			
			L_strTEMP = strTPRCD.substring(intPCCCD-2,intPCCCD);
			M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP ='MST'";
			M_strSQLQRY += " AND CMT_CGSTP ='PRXXCLR' AND isnull(CMT_STSFL,'')<>'X'";
			M_strSQLQRY += " AND CMT_CODCD = '"+ L_strTEMP +"'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPCCCD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			
			L_strTEMP = strTPRCD.substring(intPCSCD-4,intPCSCD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='CS'";
			M_strSQLQRY += " AND CD_HDRCD= '"+ strTPRCD.substring(intPCCCD-2,intPCSCD) +"'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPCSCD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}
			
			L_strTEMP = strTPRCD.substring(intPRMCD-2,intPRMCD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='RF'";
			M_strSQLQRY += " AND CD_HDRCD= '"+ strTPRCD.substring(intPMRCD-2,intPMRCD)+L_strTEMP+"0A'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPRMCD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}
						

			L_strTEMP = strTPRCD.substring(intPRSCD-2,intPRSCD);
			M_strSQLQRY = "Select CD_HDRDS from PR_CDWRK where CD_HDRCT ='RF'";
			M_strSQLQRY += " AND CD_HDRCD= '"+ strTPRCD.substring(intPMRCD-2,intPMRCD)+strTPRCD.substring(intPRMCD-2,intPRMCD)+L_strTEMP+"'  and CD_SUBCD = '00'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtPRSCD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CD_HDRDS"),""));
				M_rstRSSET.close();
			}
			
			//txtPRMCD.setText(strTPRCD.substring(intPRMCD-2,intPRMCD));
			//txtPRSCD.setText(strTPRCD.substring(intPRSCD-2,intPRSCD));
			
			L_strTEMP = strTPRCD.substring(intQFLCD-1,intQFLCD);
			M_strSQLQRY = "Select distinct CMT_CODDS from CO_CDTRN where CMT_CGMTP ='MST'";
			M_strSQLQRY += " AND CMT_CGSTP ='PRXXQFT' AND isnull(CMT_STSFL,'')<>'X'";
			M_strSQLQRY += " AND CMT_CODCD ='"+ L_strTEMP +"'";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					txtQFLCD.setText(L_strTEMP +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			setPRMDS(txtPMRCD.getText().substring(0,2),txtPRMCD.getText());
			setPRSDS(txtPMRCD.getText().substring(0,2),txtPRMCD.getText(),txtPRSCD.getText());
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getdata");
		}
	}
	/**
	 * Method to fetch Technical codes to display in the JTable.
	 * @param P_strTPRCD String argument to pass the Technical code.
	 */
	void getTBLDT(String P_strTPRCD)
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			M_strSQLQRY ="Select PR_TPRCD,PR_CGRDS from PR_PRMST where isnull(PR_STSFL,'')<>'X'";
			M_strSQLQRY +=" AND PR_TPRCD like '"+ P_strTPRCD +"%'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(L_rstRSSET!= null)
			{
				int i = 0;
				tblDATA.clrTABLE();
				while(L_rstRSSET.next())
				{
					tblDATA.setValueAt(new Boolean(false),i,0);
					tblDATA.setValueAt(nvlSTRVL(L_rstRSSET.getString("PR_CGRDS"),""),i,1);
					tblDATA.setValueAt(nvlSTRVL(L_rstRSSET.getString("PR_TPRCD"),""),i,2);					
					i++;
				}
				L_rstRSSET.close();
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTBLDT");
		}
	}
	
	

	
	/** Accepting new (Product) Reference Code and Description
	 */
	private void exeNEWRF()
	{
	}
	
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{				
				/*if((input == txtCODE) && (txtCODE.getText().trim().length() == 2))
				{													
					M_strSQLQRY = "Select distinct substring(CD_HDRCD,3,2) FROM PR_CDWRK Where CD_HDRCT = 'PC'";
					M_strSQLQRY += " AND CD_HDRCD like'"+ txtCODE.getText().trim() +"' AND ifnull(CD_STSFL,'') <>'X'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if (M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							M_rstRSSET.close();								
							return true;
						}							
						else
						{								
							M_rstRSSET.close();
							setMSG( "Code Already Exists, Please Try another ..",'E');
							return false;							
						}															
					}
				}*/
				/*if((input == txtCODE1) && (txtCODE1.getText().trim().length() == 4))
				{													
					M_strSQLQRY = "Select distinct substring(CD_HDRCD,3,4) FROM PR_CDWRK Where CD_HDRCT = 'CS'";
					M_strSQLQRY += " AND CD_HDRCD = '__"+txtCODE1.getText().trim() +"' AND ifnull(CD_STSFL,'') <>'X'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if (M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							M_rstRSSET.close();								
							return true;
						}							
						else
						{								
							M_rstRSSET.close();
							setMSG( "Code Already Exists, Please Try another ..",'E');
							return false;							
						}															
					}
				}*/
			}
			catch(Exception L_EX)
			{				
				setMSG(L_EX,"InputVerify");
				setCursor(cl_dat.M_curDFSTS_pbst);				
			}	
			return true;
		}		
	}

	
	
	
	/** Displaying Additive subcategories for maintenance / reference
	 */	
	private void exeADDDSP(String LP_HDRCT,String LP_HDRCD)
	{
		try
			{
				ResultSet L_rstRSSET = null;
				//System.out.println("00");
				if(pnlADDDTL==null)
				{
					pnlADDDTL=new JPanel(null);
					tblADDDTL=crtTBLPNL1(pnlADDDTL,new String[]{"FL","Code","Description"},20,0,1,6,6,new int[]{20,60,300},new int[]{0});
				}
				//System.out.println("01");
				String L_strHDRDS_00 = "";
				pnlADDDTL.setSize(400,100);
				pnlADDDTL.setPreferredSize(new Dimension(450,150));
				String L_strSQLQRY = "Select CD_SUBCD,CD_HDRDS from PR_CDWRK where CD_HDRCT = '"+LP_HDRCT+"' and CD_HDRCD = '"+LP_HDRCD+"' order by CD_SUBCD asc";
				L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
				//System.out.println(L_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					{setMSG("No Records Found",'E');}	
				int i=0;
				tblADDDTL.clrTABLE();
				while(true)
				{
					String L_strSUBCD = getRSTVAL(L_rstRSSET,"CD_SUBCD","C");
					String L_strHDRDS = getRSTVAL(L_rstRSSET,"CD_HDRDS","C");
					if(L_strSUBCD.equals("00"))
					{
						L_strHDRDS_00 = L_strHDRDS;
						if(!L_rstRSSET.next())
							break;
						continue;
					}
					tblADDDTL.setValueAt(L_strSUBCD,i,intTB3_SUBCD);
					tblADDDTL.setValueAt(L_strHDRDS,i,intTB3_HDRDS);
					i++;
					if(!L_rstRSSET.next())
						break;
				}
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlADDDTL,L_strHDRDS_00,JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN == 0)
				{
					for(i = 0;i<tblADDDTL.getRowCount();i++)
					{
						if(tblADDDTL.getValueAt(i,intTB3_SUBCD).toString().length() < 2)
							break;
						if(tblADDDTL.getValueAt(i,intTB3_CHKFL).toString().equals("false"))
							continue;
						String L_strWHRSTR = " CD_HDRCT = '"+LP_HDRCT+"' and CD_HDRCD = '"+LP_HDRCD+"' and CD_SUBCD = '"+tblADDDTL.getValueAt(i,intTB3_SUBCD).toString()+"'";
						flgCHK_EXIST =  chkEXIST("PR_CDWRK", L_strWHRSTR);
						L_strSQLQRY = "insert into PR_CDWRK (CD_HDRCT, CD_HDRCD, CD_SUBCD, CD_HDRDS,CD_TRNFL, CD_STSFL,  CD_LUPDT, CD_LUSBY) values ("
						+setINSSTR("CD_HDRCT",LP_HDRCT,"C")
						+setINSSTR("CD_HDRCD",LP_HDRCD,"C")
						+setINSSTR("CD_SUBCD",tblADDDTL.getValueAt(i,intTB3_SUBCD).toString(),"C")
						+setINSSTR("CD_HDRDS",tblADDDTL.getValueAt(i,intTB3_HDRDS).toString(),"C")
						+setINSSTR("CD_TRNFL","0","C")
						+setINSSTR("CD_STSFL","1","C")
						+setINSSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
						+"'"+cl_dat.M_strUSRCD_pbst+"')";
						if(flgCHK_EXIST)
						{
							L_strSQLQRY = " update PR_CDWRK set "
							+setUPDSTR("CD_HDRDS",tblADDDTL.getValueAt(i,intTB3_HDRDS).toString(),"C")
							+setUPDSTR("CD_TRNFL","0","C")
							+setUPDSTR("CD_STSFL","1","C")
							+setUPDSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
							+" CD_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+L_strWHRSTR;
						}
						//System.out.println(L_strSQLQRY);
						cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					}
					String L_strWHRSTR1 = " CD_HDRCT = '"+LP_HDRCT+"' and CD_HDRCD = '"+LP_HDRCD+"' and CD_HDRDS = '-'";
					L_strSQLQRY = " delete from PR_CDWRK where "+ L_strWHRSTR1;
					System.out.println(L_strSQLQRY);
					cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					if(cl_dat.exeDBCMT("exeSAVE"))
						setMSG("Saved Successfully",'N');
				}
			}
		catch(Exception e)
		{setMSG(e,"exeADDDSP");}
	}
	

	
	
	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
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
		{setMSG(L_EX,"getRSTVAL");}
	return " ";
	} 


	
/** Generating string for Insertion Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try 
{
	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
	if (LP_FLDTP.equals("C"))
		 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
 	else if (LP_FLDTP.equals("N"))
         return   nvlSTRVL(LP_FLDVL,"0") + ",";
 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else if (LP_FLDTP.equals("T"))
		 return   (LP_FLDVL.length()>10) ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else return " ";
        }
    catch (Exception L_EX) 
	{setMSG("Error in setINSSTR : "+L_EX,'E');}
return " ";
}
		



/** Generating string for Updation Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try 
{
	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
	if (LP_FLDTP.equals("C"))
		 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
 	else if (LP_FLDTP.equals("N"))
         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
	else if (LP_FLDTP.equals("T"))
		 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
	else return " ";
        }
    catch (Exception L_EX) 
	{setMSG("Error in setUPDSTR : "+L_EX,'E');}
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
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if (L_rstRSSET != null && L_rstRSSET.next())
		{
			L_flgCHKFL = true;
			L_rstRSSET.close();
		}
	}
	catch (Exception L_EX)	
	{setMSG("Error in chkEXIST : "+L_EX,'E');}
	return L_flgCHKFL;
}
	

	/** Maintaining subcategories of product code
	 */	
	private void exeADDCAT_PC(String LP_CATDS,String LP_HDRCT,String LP_HDRCD1, String LP_HDRCD2, String LP_HDRCD3, int LP_CATSQ)
	{
		try
			{
				ResultSet L_rstRSSET = null;
				if(pnlADDCAT==null)
				{
					pnlADDCAT=new JPanel(null);
					tblADDCAT=crtTBLPNL1(pnlADDCAT,new String[]{"FL","Code","Description"},20,0,1,6,6,new int[]{20,60,300},new int[]{0});
				}
				String L_strHDRCD1 = "";
				String L_whrHDRCD1 = "";
				if(LP_CATSQ == 1)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,1,2)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,3,2) = '"+LP_HDRCD2+"' and SUBSTRING(CD_HDRCD,5,2)='"+LP_HDRCD3+"'";}
				else if(LP_CATSQ == 2)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,3,2)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,5,2)='"+LP_HDRCD3+"'";}
				else if(LP_CATSQ == 3)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,5,2)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,3,2)='"+LP_HDRCD2+"' and SUBSTRING(CD_HDRCD,5,2) <> '0A'";}
				String L_strHDRDS_00 = "";
				pnlADDCAT.setSize(400,100);
				pnlADDCAT.setPreferredSize(new Dimension(450,150));
				String L_strSQLQRY = "Select "+L_strHDRCD1+" CD_HDRCD1,CD_HDRDS from PR_CDWRK where  CD_SUBCD = '00' and CD_HDRCT = '"+LP_HDRCT+"' "+L_whrHDRCD1+" order by CD_HDRCD1 asc";
				L_rstRSSET=cl_dat.exeSQLQRY3(L_strSQLQRY);
				//System.out.println(L_strSQLQRY);
				int i=0;
				tblADDCAT.clrTABLE();
				if(L_rstRSSET!=null && L_rstRSSET.next())
				{
					while(true)
					{
						tblADDCAT.setValueAt(getRSTVAL(L_rstRSSET,"CD_HDRCD1","C"),i,intTB2_HDRCD1);
						tblADDCAT.setValueAt(getRSTVAL(L_rstRSSET,"CD_HDRDS","C"),i,intTB2_HDRDS);
						i++;
						if(!L_rstRSSET.next())
							break;
					}
				}
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlADDCAT,LP_CATDS,JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN == 0)
				{
					for(i=0;i<tblADDCAT.getRowCount();i++)
					{
						if(tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString().length() < 2)
							break;
						if(tblADDCAT.getValueAt(i,intTB2_CHKFL).toString().equals("false"))
							continue;
						String L_strHDRCD2 = "";
						if(LP_CATSQ == 1)
							L_strHDRCD2 = tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString()+LP_HDRCD2+LP_HDRCD3;
						else if(LP_CATSQ == 2)
							L_strHDRCD2 = LP_HDRCD1+tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString()+LP_HDRCD3;
						else if(LP_CATSQ == 3)
							L_strHDRCD2 = LP_HDRCD1+LP_HDRCD2+tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString();
							
						String L_strWHRSTR = " CD_HDRCT = '"+LP_HDRCT+"' and CD_HDRCD = '"+L_strHDRCD2+"' and CD_SUBCD = '00'";
						
						flgCHK_EXIST =  chkEXIST("PR_CDWRK", L_strWHRSTR);
						L_strSQLQRY = "insert into PR_CDWRK (CD_HDRCT, CD_HDRCD, CD_SUBCD, CD_HDRDS,CD_TRNFL, CD_STSFL,  CD_LUPDT, CD_LUSBY) values ("
						+setINSSTR("CD_HDRCT",LP_HDRCT,"C")
						+setINSSTR("CD_HDRCD",L_strHDRCD2,"C")
						+setINSSTR("CD_SUBCD","00","C")
						+setINSSTR("CD_HDRDS",tblADDCAT.getValueAt(i,intTB2_HDRDS).toString(),"C")
						+setINSSTR("CD_TRNFL","0","C")
						+setINSSTR("CD_STSFL","1","C")
						+setINSSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
						+"'"+cl_dat.M_strUSRCD_pbst+"')";
						if(flgCHK_EXIST)
						{
							L_strSQLQRY = " update PR_CDWRK set "
							+setUPDSTR("CD_HDRDS",tblADDCAT.getValueAt(i,intTB2_HDRDS).toString(),"C")
							+setUPDSTR("CD_TRNFL","0","C")
							+setUPDSTR("CD_STSFL","1","C")
							+setUPDSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
							+" CD_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+L_strWHRSTR;
						}
						//System.out.println(L_strSQLQRY);
						cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					}
					String L_strCDCHK = "";
					if(LP_CATSQ == 1)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,3,2) = '"+LP_HDRCD2+"' and SUBSTRING(CD_HDRCD,5,2) = '"+LP_HDRCD3+"'";
					else if(LP_CATSQ == 2)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,5,2) = '"+LP_HDRCD3+"'";
					else if(LP_CATSQ == 3)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,3,2) = '"+LP_HDRCD2+"'";
					String L_strWHRSTR1 = " CD_HDRCT = '"+LP_HDRCT+"' and CD_SUBCD = '00' "+L_strCDCHK+" and CD_HDRDS = '-'";
					L_strSQLQRY = " delete from PR_CDWRK where "+ L_strWHRSTR1;
					System.out.println(L_strSQLQRY);
					cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					if(cl_dat.exeDBCMT("exeSAVE"))
						setMSG("Saved Successfully",'N');
				}
			}
		catch(Exception e)
		{setMSG(e,"exeADDCAT");}
	}


	
	/** Maintaining subcategories of product code
	 */	
	private void exeADDCAT_RF(String LP_CATDS,String LP_HDRCT,String LP_HDRCD1, String LP_HDRCD2, String LP_HDRCD3, int LP_CATSQ)
	{
		try
			{
				ResultSet L_rstRSSET = null;
				if(pnlADDCAT==null)
				{
					pnlADDCAT=new JPanel(null);
					tblADDCAT=crtTBLPNL1(pnlADDCAT,new String[]{"FL","Code","Description"},20,0,1,6,6,new int[]{20,60,300},new int[]{0});
				}
				String L_strHDRCD1 = "";
				String L_whrHDRCD1 = "";
				if(LP_CATSQ == 1)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,1,2)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,3,2) = '"+LP_HDRCD2+"' and SUBSTRING(CD_HDRCD,5,2)='"+LP_HDRCD3+"'";}
				else if(LP_CATSQ == 2)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,3,2)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,5,2)='"+LP_HDRCD3+"'";}
				else if(LP_CATSQ == 3)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,5,2)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,3,2)='"+LP_HDRCD2+"' and SUBSTRING(CD_HDRCD,5,2) <> '0A'";}
				String L_strHDRDS_00 = "";
				pnlADDCAT.setSize(400,100);
				pnlADDCAT.setPreferredSize(new Dimension(450,150));
				String L_strSQLQRY = "Select "+L_strHDRCD1+" CD_HDRCD1,CD_HDRDS from PR_CDWRK where  CD_SUBCD = '00' and CD_HDRCT = '"+LP_HDRCT+"' "+L_whrHDRCD1+" order by CD_HDRCD1 asc";
				L_rstRSSET=cl_dat.exeSQLQRY3(L_strSQLQRY);
				//System.out.println(L_strSQLQRY);
				int i=0;
				tblADDCAT.clrTABLE();
				if(L_rstRSSET!=null && L_rstRSSET.next())
				{
					while(true)
					{
						tblADDCAT.setValueAt(getRSTVAL(L_rstRSSET,"CD_HDRCD1","C"),i,intTB2_HDRCD1);
						tblADDCAT.setValueAt(getRSTVAL(L_rstRSSET,"CD_HDRDS","C"),i,intTB2_HDRDS);
						i++;
						if(!L_rstRSSET.next())
							break;
					}
				}
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlADDCAT,LP_CATDS,JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN == 0)
				{
					for(i=0;i<tblADDCAT.getRowCount();i++)
					{
						if(tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString().length() != 2)
							break;
						if(tblADDCAT.getValueAt(i,intTB2_CHKFL).toString().equals("false"))
							continue;
						String L_strHDRCD2 = "";
						if(LP_CATSQ == 1)
							L_strHDRCD2 = tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString()+LP_HDRCD2+LP_HDRCD3;
						else if(LP_CATSQ == 2)
							L_strHDRCD2 = LP_HDRCD1+tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString()+LP_HDRCD3;
						else if(LP_CATSQ == 3)
							L_strHDRCD2 = LP_HDRCD1+LP_HDRCD2+tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString();
							
						String L_strWHRSTR = " CD_HDRCT = '"+LP_HDRCT+"' and CD_HDRCD = '"+L_strHDRCD2+"' and CD_SUBCD = '00'";
						
						flgCHK_EXIST =  chkEXIST("PR_CDWRK", L_strWHRSTR);
						L_strSQLQRY = "insert into PR_CDWRK (CD_HDRCT, CD_HDRCD, CD_SUBCD, CD_HDRDS,CD_TRNFL, CD_STSFL,  CD_LUPDT, CD_LUSBY) values ("
						+setINSSTR("CD_HDRCT",LP_HDRCT,"C")
						+setINSSTR("CD_HDRCD",L_strHDRCD2,"C")
						+setINSSTR("CD_SUBCD","00","C")
						+setINSSTR("CD_HDRDS",tblADDCAT.getValueAt(i,intTB2_HDRDS).toString(),"C")
						+setINSSTR("CD_TRNFL","0","C")
						+setINSSTR("CD_STSFL","1","C")
						+setINSSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
						+"'"+cl_dat.M_strUSRCD_pbst+"')";
						if(flgCHK_EXIST)
						{
							L_strSQLQRY = " update PR_CDWRK set "
							+setUPDSTR("CD_HDRDS",tblADDCAT.getValueAt(i,intTB2_HDRDS).toString(),"C")
							+setUPDSTR("CD_TRNFL","0","C")
							+setUPDSTR("CD_STSFL","1","C")
							+setUPDSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
							+" CD_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+L_strWHRSTR;
						}
						//System.out.println(L_strSQLQRY);
						cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					}
					String L_strCDCHK = "";
					if(LP_CATSQ == 1)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,3,2) = '"+LP_HDRCD2+"' and SUBSTRING(CD_HDRCD,5,2) = '"+LP_HDRCD3+"'";
					else if(LP_CATSQ == 2)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,5,2) = '"+LP_HDRCD3+"'";
					else if(LP_CATSQ == 3)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,3,2) = '"+LP_HDRCD2+"'";
					String L_strWHRSTR1 = " CD_HDRCT = '"+LP_HDRCT+"' and CD_SUBCD = '00' "+L_strCDCHK+" and CD_HDRDS = '-'";
					L_strSQLQRY = " delete from PR_CDWRK where "+ L_strWHRSTR1;
					//System.out.println(L_strSQLQRY);
					cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					if(cl_dat.exeDBCMT("exeSAVE"))
						setMSG("Saved Successfully",'N');
				}
			}
		catch(Exception e)
		{setMSG(e,"exeADDCAT");}
	}
	
	
	/** Maintaining subcategories of product code for Colour Codes 
	 */	
	private void exeADDCAT1(String LP_CATDS,String LP_HDRCT,String LP_HDRCD1, String LP_HDRCD2,int LP_CATSQ)
	{
		try
			{
				ResultSet L_rstRSSET = null;
				if(pnlADDCAT==null)
				{
					pnlADDCAT=new JPanel(null);
					tblADDCAT=crtTBLPNL1(pnlADDCAT,new String[]{"FL","Code","Description"},20,0,1,6,6,new int[]{20,60,300},new int[]{0});
				}
				String L_strHDRCD1 = "";
				String L_whrHDRCD1 = "";
				if(LP_CATSQ == 1)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,1,2)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,3,4) = '"+LP_HDRCD2+"'";}
				else if(LP_CATSQ == 2)
					{L_strHDRCD1 = "SUBSTRING(CD_HDRCD,3,4)"; L_whrHDRCD1 = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"' and SUBSTRING(CD_HDRCD,3,4) <> '000A'";}
				String L_strHDRDS_00 = "";
				pnlADDCAT.setSize(400,100);
				pnlADDCAT.setPreferredSize(new Dimension(450,150));
				String L_strSQLQRY = "Select "+L_strHDRCD1+" CD_HDRCD1,CD_HDRDS from PR_CDWRK where  CD_SUBCD = '00' and CD_HDRCT = '"+LP_HDRCT+"' "+L_whrHDRCD1+" order by CD_HDRCD1 asc";
				L_rstRSSET=cl_dat.exeSQLQRY3(L_strSQLQRY);
				//System.out.println(L_strSQLQRY);
				int i=0;
				tblADDCAT.clrTABLE();
				if(L_rstRSSET!=null && L_rstRSSET.next())
				{
					while(true)
					{
						tblADDCAT.setValueAt(getRSTVAL(L_rstRSSET,"CD_HDRCD1","C"),i,intTB2_HDRCD1);
						tblADDCAT.setValueAt(getRSTVAL(L_rstRSSET,"CD_HDRDS","C"),i,intTB2_HDRDS);
						i++;
						if(!L_rstRSSET.next())
							break;
					}
				}
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlADDCAT,LP_CATDS,JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN == 0)
				{
					for(i=0;i<tblADDCAT.getRowCount();i++)
					{
						if(tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString().length() != 4)
							break;
						if(tblADDCAT.getValueAt(i,intTB2_CHKFL).toString().equals("false"))
							continue;
						String L_strHDRCD2 = "";
						if(LP_CATSQ == 1)
							L_strHDRCD2 = tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString()+LP_HDRCD2;
						else if(LP_CATSQ == 2)
							L_strHDRCD2 = LP_HDRCD1+tblADDCAT.getValueAt(i,intTB2_HDRCD1).toString();
							
						String L_strWHRSTR = " CD_HDRCT = '"+LP_HDRCT+"' and CD_HDRCD = '"+L_strHDRCD2+"' and CD_SUBCD = '00'";
						
						flgCHK_EXIST =  chkEXIST("PR_CDWRK", L_strWHRSTR);
						L_strSQLQRY = "insert into PR_CDWRK (CD_HDRCT, CD_HDRCD, CD_SUBCD, CD_HDRDS,CD_TRNFL, CD_STSFL,  CD_LUPDT, CD_LUSBY) values ("
						+setINSSTR("CD_HDRCT",LP_HDRCT,"C")
						+setINSSTR("CD_HDRCD",L_strHDRCD2,"C")
						+setINSSTR("CD_SUBCD","00","C")
						+setINSSTR("CD_HDRDS",tblADDCAT.getValueAt(i,intTB2_HDRDS).toString(),"C")
						+setINSSTR("CD_TRNFL","0","C")
						+setINSSTR("CD_STSFL","1","C")
						+setINSSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
						+"'"+cl_dat.M_strUSRCD_pbst+"')";
						if(flgCHK_EXIST)
						{
							L_strSQLQRY = " update PR_CDWRK set "
							+setUPDSTR("CD_HDRDS",tblADDCAT.getValueAt(i,intTB2_HDRDS).toString(),"C")
							+setUPDSTR("CD_TRNFL","0","C")
							+setUPDSTR("CD_STSFL","1","C")
							+setUPDSTR("CD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
							+" CD_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+L_strWHRSTR;
						}
						//System.out.println(L_strSQLQRY);
						cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					}
					String L_strCDCHK = "";
					if(LP_CATSQ == 1)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,3,4) = '"+LP_HDRCD2+"'";
					else if(LP_CATSQ == 2)
						L_strCDCHK = " and SUBSTRING(CD_HDRCD,1,2) = '"+LP_HDRCD1+"'";
					String L_strWHRSTR1 = " CD_HDRCT = '"+LP_HDRCT+"' and CD_SUBCD = '00' "+L_strCDCHK+" and CD_HDRDS = '-'";
					L_strSQLQRY = " delete from PR_CDWRK where "+ L_strWHRSTR1;
					System.out.println(L_strSQLQRY);
					cl_dat.exeSQLUPD(L_strSQLQRY ,"");
					if(cl_dat.exeDBCMT("exeSAVE"))
						setMSG("Saved Successfully",'N');
				}
			}
		catch(Exception e)
		{setMSG(e,"exeADDCAT1");}
	}
	
}