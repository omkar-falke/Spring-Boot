/*
System Name   : Management Information System.
Program Name  : Software Licences Master. 
Program Desc. : Entry Screen for Software Licences Master.
Author        : Mrs.Dipti S.Shinde.
Date          : 27th June 2005
System        : System admin. 
Version       : MMS v2.0.0
Modificaitons : 
Modified By   :  
Modified Date : 
Modified det. :  
Version       : 
*/
import java.sql.ResultSet;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.InputVerifier;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.JButton;
import java.util.Date;
import java.awt.event.*;

/**
 * <P>
 * <PRE style = font-size : 10 pt > <b>System Name :</b> Management Information
 * System.
 * 
 * <b>Program Name :</b> Software Master..
 * 
 * <b>Purpose :</b> This module captures Software Licences & location wise
 * Software Details such as Software code,Licences Type ,no of user, Location
 * etc for the insertion & updation of data in the Software Master table. Here
 * we canbe able add new Records, Modify existing Records and to Delete unwanted
 * records.
 * 
 * List of tables used : Table Name Primary key Operation done Insert Update
 * Query Delete
 * ----------------------------------------------------------------------------------------------------------
 * CO_SWMST SW_SFTCD,SW_SRLNO # # # #
 * 
 * ----------------------------------------------------------------------------------------------------------
 * 
 * List of fields accepted/displayed on screen : Field Name Column Name Table
 * name Type/Size Description
 * ----------------------------------------------------------------------------------------------------------
 * cmbFLDNM SW_LOCDS, CO_SWMST Varchar(15) Software Location SW_LICTP
 * Varchar(10) Software Licences type SW_SFTNM varchar(40) Software Name
 * ----------------------------------------------------------------------------------------------------------
 * 
 * List of fields with help facility : Field Name Display Description Display
 * Columns Table Name
 * ----------------------------------------------------------------------------------------------------------
 * txtFLDVL SW_LOCDS, CO_SWMST Varchar(15) Software Location SW_LICTP
 * Varchar(10) Software Licences type SW_SFTNM varchar(40) Software Name
 * ----------------------------------------------------------------------------------------------------------
 * 
 * Validations : 1)Date should be in date format. 2)Last use by Date should not
 * be gretter than today date. 3)When Data is entering that time field should
 * not be blank.
 * 
 */
class sa_teswm extends cl_pbase implements MouseListener, FocusListener, KeyListener 
{
    private JTextField txtFLDVL;/**TextField for f1 help*/
	private JComboBox cmbFLDNM;/** Combo box for f1 selection*/
    private String strHLPFLD="";/**String for Join String to exeHLPOK*/
	private String strHLPFLD1="";/**String for Join String to exeHLPOK*/
	private String strHLPFLD2="";/**String for Join String to exeHLPOK*/
	private int  intROWCNT =200;/** Integer for counting a Row no*/
    private String strARRHDR[] = {"Field Value"}; /**Array for String of f1 menu*/     
	private	JTabbedPane jtpMANTAB;/** Tab for SWDTL table and ADDTL table */
	private JPanel pnlSWDTL,pnlADDTL;/** panel for table*/
	private cl_JTable tblSWDTL;/** table for Software details*/
	private cl_JTable tblADDTL;/** table for Additional details about software*/
	private	JLabel lblFLDNM,lblFLDVL;/**label for combobox,textfield  */ 

	private	final int TB1_CHKFL =0;	/** for specifis columns of SWDTL */
	private	final int TB1_SFTCD =1;	
	private	final int TB1_SRLNO =2;	
	private	final int TB1_SFTNM =3;	
	private	final int TB1_LICTP =4;
	private	final int TB1_PRCFR =5;
	private	final int TB1_VERNO =6;
	private	final int TB1_USRNO =7;
	private	final int TB1_LOCDS =8;
	
	private	final int TB2_CHKFL =0;/** for specifis columns of ADDTL */
	private	final int TB2_SFTCD =1;
	private	final int TB2_SRLNO =2;
	private	final int TB2_PRCDT =3;
	private	final int TB2_LICNO =4;
	private	final int TB2_UPGDT =5;
	private	final int TB2_EXPDT =6;
	private	final int TB2_CUSID =7;
	private	final int TB2_REMDS =8;
	private JTextField txtSFTCD;/** Textfield for Software code */
	private JTextField txtLOCDS;/** Textfield for location */
	private JTextField txtSFTCD1;/** Textfield for Software code of TB2 */
	private JTextField txtEXPDT;/** Textfield for Exp date of software */
	private JTextField txtUPGDT;/** Textfield for update date */
	private JTextField txtPRCDT;/** Textfield for parchase date of software details  */
	private JTextField txtSRLNO;/** Textfield for Software srl no */
	private JTextField txtLICTP;/** Textfield for licence type of software details*/
	private JTextField txtSRLNO1;/** Textfield for software srl no of ADDTL tabel */
	private String strSRLNO1="";/** forsrlno in String format */
	private int intSRLNO1=0;;/** for srl no in integet format */
	sa_teswm()    
	{
		super(2);
		try
		{
			setMatrix(20,8);				
			add(lblFLDNM = new JLabel("Query on "),2,2,1,1,this,'L');
			add(cmbFLDNM = new JComboBox(),2,3,1,1,this,'R');
			
			cmbFLDNM.addItem("ALL");
			cmbFLDNM.addItem("SW_LICTP");
			cmbFLDNM.addItem("SW_LOCDS");
			cmbFLDNM.addItem("SW_SFTCD");
						
			add(lblFLDVL = new JLabel("Query For"),2,5,1,1,this,'R');
			add(txtFLDVL = new JTextField(),2,6,1,1,this,'L');
			
		    pnlADDTL = new JPanel();
			pnlSWDTL = new JPanel();
			jtpMANTAB = new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
	        
			pnlADDTL.setLayout(null);
			String[] L_strTBLHD2 = {"Select","Code","SRL.","Procurement Date","Licence No.","Upgraded on","Expiry date","Customer Id","Remark"};// Table Header
			int[] L_intCOLSZ1 = {40,60,50,80,180,80,80,100,150}; // Columnm Size in Table  
			tblADDTL = crtTBLPNL1(pnlADDTL,L_strTBLHD2, intROWCNT,1,1,12,7.9,L_intCOLSZ1,new int[]{0});
			add(pnlADDTL,1,1,10,9,jtpMANTAB,'L');
			tblADDTL.setCellEditor(TB2_SFTCD, txtSFTCD1 = new TxtLimit(15));	
			tblADDTL.setCellEditor(TB2_EXPDT, txtEXPDT = new TxtDate());
			tblADDTL.setCellEditor(TB2_UPGDT, txtUPGDT = new TxtDate());
			tblADDTL.setCellEditor(TB2_PRCDT, txtPRCDT = new TxtDate());
			tblADDTL.setCellEditor(TB2_SRLNO, txtSRLNO1 = new TxtNumLimit(3));
			tblADDTL.addMouseListener(this);
			
			pnlSWDTL.setLayout(null);
			String[] L_strTBLHD1 = {"Select","Code","SRL.","Software Name ","Lic.Type","Supplier","Service Pack.","No.Of users","Location"};// Table Header
			int[]L_intCOLSZ = {40,60,50,170,80,100,100,80,100}; // Columnm Size in Table  
			tblSWDTL = crtTBLPNL1(pnlSWDTL,L_strTBLHD1, intROWCNT,1,1,12,7.9,L_intCOLSZ,new int[]{0});
		    add(pnlSWDTL,1,1,10,9,jtpMANTAB,'L');
			tblSWDTL.setCellEditor(TB1_SFTCD, txtSFTCD = new TxtLimit(15));
			tblSWDTL.setCellEditor(TB1_LOCDS, txtLOCDS = new TxtLimit(15));
			tblSWDTL.setCellEditor(TB1_SRLNO, txtSRLNO = new TxtLimit(10));
			tblSWDTL.setCellEditor(TB1_LICTP, txtLICTP = new TxtLimit(15));
			tblSWDTL.addMouseListener(this);
			
			jtpMANTAB.addTab("Licence details",pnlSWDTL);	
			jtpMANTAB.addTab("Additional details",pnlADDTL);	
			add(jtpMANTAB,4,1,14,8,this,'L');	
			txtSFTCD.addFocusListener(this);
			txtSFTCD.addKeyListener(this);
			
			txtLOCDS.addKeyListener(this);
			txtLOCDS.addFocusListener(this);
			
			txtSFTCD1.addFocusListener(this);
			txtSFTCD1.addKeyListener(this);
			
			txtEXPDT.addFocusListener(this);
			txtEXPDT.addKeyListener(this);
			
			txtUPGDT.addFocusListener(this);
			txtUPGDT.addKeyListener(this);
			
			txtPRCDT.addFocusListener(this);
			txtPRCDT.addKeyListener(this); 
			
			txtSRLNO.addFocusListener(this);
			txtSRLNO.addKeyListener(this);
			
			txtLICTP.addFocusListener(this);
			txtLICTP.addKeyListener(this);
			
			txtSRLNO1.addFocusListener(this);
			txtSRLNO1.addKeyListener(this);
			
			
			setENBL(false);
		}   
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}   
	}       
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		    
			cmbFLDNM.setEnabled(true);
			txtFLDVL.setEnabled(true);
			cmbFLDNM.setVisible(true);
			txtFLDVL.setVisible(true);
			lblFLDNM.setVisible(true);
			lblFLDVL.setVisible(true);
			tblSWDTL.clrTABLE();
			tblADDTL.clrTABLE();
				
			tblSWDTL.cmpEDITR[TB1_CHKFL].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_SFTCD].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_SRLNO].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_SFTNM].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_LICTP].setEnabled(false);
			tblADDTL.cmpEDITR[TB2_LICNO].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_VERNO].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_USRNO].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_LOCDS].setEnabled(false);		
			tblADDTL.cmpEDITR[TB2_REMDS].setEnabled(false);	
		        
			tblADDTL.cmpEDITR[TB2_CHKFL].setEnabled(false);
			tblADDTL.cmpEDITR[TB2_SFTCD].setEnabled(false);
			tblADDTL.cmpEDITR[TB2_SRLNO].setEnabled(false);
			tblADDTL.cmpEDITR[TB2_PRCDT].setEnabled(false);
			tblSWDTL.cmpEDITR[TB1_PRCFR].setEnabled(false);
			tblADDTL.cmpEDITR[TB2_UPGDT].setEnabled(false);
			tblADDTL.cmpEDITR[TB2_EXPDT].setEnabled(false);
			tblADDTL.cmpEDITR[TB2_CUSID].setEnabled(false);	
				
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{
				cmbFLDNM.setEnabled(false);
				txtFLDVL.setEnabled(false);
				cmbFLDNM.setVisible(false);
				txtFLDVL.setVisible(false);
				lblFLDNM.setVisible(false);
				lblFLDVL.setVisible(false);
				tblSWDTL.requestFocus();
				
			   	tblSWDTL.cmpEDITR[TB1_SFTCD].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_SRLNO].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_SFTNM].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_LICTP].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_LICNO].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_VERNO].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_USRNO].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_LOCDS].setEnabled(true);		
				tblSWDTL.cmpEDITR[TB1_CHKFL].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_PRCFR].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_CHKFL].requestFocus();
				
				tblADDTL.cmpEDITR[TB2_SFTCD].setEnabled(false);
				tblADDTL.cmpEDITR[TB2_SRLNO].setEnabled(false);
				tblADDTL.cmpEDITR[TB2_PRCDT].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_REMDS].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_UPGDT].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_EXPDT].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_CUSID].setEnabled(true);	
				tblADDTL.cmpEDITR[TB2_CHKFL].setEnabled(false);
				tblADDTL.cmpEDITR[TB2_CHKFL].requestFocus();		
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				cmbFLDNM.setEnabled(true);
				txtFLDVL.setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_SFTCD].setEnabled(false);
				tblSWDTL.cmpEDITR[TB1_SRLNO].setEnabled(false);
				tblSWDTL.cmpEDITR[TB1_SFTNM].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_LICTP].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_LICNO].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_VERNO].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_USRNO].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_LOCDS].setEnabled(true);		
				tblADDTL.cmpEDITR[TB2_REMDS].setEnabled(true);	
		    
				tblADDTL.cmpEDITR[TB2_SFTCD].setEnabled(false);
				tblADDTL.cmpEDITR[TB2_SRLNO].setEnabled(false);
				tblADDTL.cmpEDITR[TB2_PRCDT].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_PRCFR].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_UPGDT].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_EXPDT].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_CUSID].setEnabled(true);
				tblSWDTL.cmpEDITR[TB1_CHKFL].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_CHKFL].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_CHKFL].setEnabled(false);
				tblSWDTL.cmpEDITR[TB1_CHKFL].setEnabled(true);
						
				tblSWDTL.cmpEDITR[TB1_CHKFL].requestFocus();
				tblADDTL.cmpEDITR[TB2_CHKFL].requestFocus();		
			}	
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{		
				tblSWDTL.cmpEDITR[TB1_CHKFL].setEnabled(true);
				tblADDTL.cmpEDITR[TB2_CHKFL].setEnabled(false);
				txtFLDVL.requestFocus();
			}
		}       
	public void actionPerformed(ActionEvent L_AE)
	{       
		super.actionPerformed(L_AE);    
		try 
		{   
 		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{   
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
			{
				cmbFLDNM.requestFocus();
				cmbFLDNM.setEnabled(true);
				txtFLDVL.setEnabled(true);
				setMSG("Please Select Option..",'N');					
				setENBL(true);
				tblSWDTL.clrTABLE();
				tblADDTL.clrTABLE();
			}
			if(M_objSOURC==cl_dat.M_OPADD_pbst)
			{
				tblSWDTL.requestFocus();
				tblSWDTL.clrTABLE();
				tblADDTL.clrTABLE();
			}
			else
			{					
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				setMSG("select option..",'N');
				//setMSG("Addition & Deletation is not allowed here..",'N');
				setENBL(false);
			}	
		}   
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			setENBL(false);	
		if(M_objSOURC==txtFLDVL)
		{
		    tblSWDTL.clrTABLE();
			tblADDTL.clrTABLE();
			if(tblSWDTL.isEditing())
				tblSWDTL.getCellEditor().stopCellEditing();
			tblSWDTL.setRowSelectionInterval(0,0);
			tblSWDTL.setColumnSelectionInterval(0,0);
			if(tblADDTL.isEditing())
				tblADDTL.getCellEditor().stopCellEditing();
			tblADDTL.setRowSelectionInterval(0,0);
			tblADDTL.setColumnSelectionInterval(0,0);
			
			getDATA(cmbFLDNM.getSelectedItem().toString().trim(),txtFLDVL.getText().trim());				
		}
			if(M_objSOURC==cmbFLDNM)
			{
				 if(cmbFLDNM.getSelectedItem().toString().trim().equals("ALL"))
				 {
					  tblSWDTL.clrTABLE();
					  tblADDTL.clrTABLE();
					  if(tblSWDTL.isEditing())
						tblSWDTL.getCellEditor().stopCellEditing();
					  tblSWDTL.setRowSelectionInterval(0,0);
					  tblSWDTL.setColumnSelectionInterval(0,0);
					  if(tblADDTL.isEditing())
						tblADDTL.getCellEditor().stopCellEditing();
					  tblADDTL.setRowSelectionInterval(0,0);
					  tblADDTL.setColumnSelectionInterval(0,0);
			
					  txtFLDVL.setText("");
					  getDATA("ALL","");
				 }
				 else
				 {
					 tblSWDTL.clrTABLE();
					 tblADDTL.clrTABLE();
					 txtFLDVL.setText("");
					 txtFLDVL.requestFocus();
				}
				 jtpMANTAB.setSelectedIndex(0);
			}		
			/*if(M_objSOURC==jtpMANTAB)
			{
				if(jtpMANTAB.getSelectedIndex() == 1)
				{
					for(int i=0;i<tblSWDTL.getRowCount();i++)
					{
						if(tblSWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
		   				{
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_CHKFL),i,TB2_CHKFL);
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SFTCD),i,TB2_SFTCD);
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SRLNO),i,TB2_SRLNO);
						}
					}
				}
			}*/
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}
	}
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			for(int i=0;i<tblSWDTL.getRowCount();i++)
				{
					if(tblSWDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
					  /*if(tblADDTL.isEditing())
							tblADDTL.getCellEditor().stopCellEditing();
						tblADDTL.setRowSelectionInterval(0,0);
						tblADDTL.setColumnSelectionInterval(0,0);
						tblADDTL.clrTABLE();*/
			            
						tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SRLNO),i,TB2_SRLNO);
						tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_CHKFL),i,TB2_CHKFL);
						tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SFTCD),i,TB2_SFTCD);
					}
				}
			if(M_objSOURC==jtpMANTAB)
			{
				if(jtpMANTAB.getSelectedIndex() == 1)
				{
					for(int i=0;i<tblSWDTL.getRowCount();i++)
					{
						if(tblSWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
		   				{
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_CHKFL),i,TB2_CHKFL);
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SFTCD),i,TB2_SFTCD);
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SRLNO),i,TB2_SRLNO);
						}
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"MouseClick");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		if(M_objSOURC==txtSFTCD)
		{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			setMSG("press F1 to select the value..",'N');
		else	
				setMSG(" select a option..",'N');
		}	
		if(M_objSOURC==txtLOCDS)
		{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			setMSG("press F1 to select the value..",'N');
		else	
				setMSG(" select a option..",'N');
		}	
		if(M_objSOURC==cmbFLDNM)
		{	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			setMSG("Please select the field for enquiry, select 'ALL' for all records.. ",'N');
			else	
				setMSG(" ",'N');
		}
	}		
	public void focusLost(FocusEvent L_FE)
	{	
		super.focusLost(L_FE);
	}		
	public void keyTyped(KeyEvent L_KE)
	{	
		super.keyTyped(L_KE);
	}
	public void keyPressed(KeyEvent L_KE)
	{ 
		super.keyPressed(L_KE);
		try
		 {
			if ((L_KE.getKeyCode()== L_KE.VK_F1))
			{		
			 txtFLDVL.setText(txtFLDVL.getText().trim().toUpperCase());	
			 txtSFTCD.setText(txtSFTCD.getText().trim().toUpperCase());
			 txtLOCDS.setText(txtLOCDS.getText().trim().toUpperCase());
			 setCursor(cl_dat.M_curWTSTS_pbst);
		
			 if(M_objSOURC==txtSFTCD)
				{						
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					 {
						M_strHLPFLD = "txtSFTCD";
						M_strSQLQRY=" SELECT distinct SW_SFTCD from CO_SWMST where isnull(SW_STSFL,' ') <> 'X'";
						if(txtSFTCD.getText().length() >0)
							M_strSQLQRY += " where SW_SFTCD like '"+txtSFTCD.getText().trim()+"%'";
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Software code"},1,"CT");
					}
				}
				if(M_objSOURC==txtLOCDS)
				{	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						strHLPFLD1 = "txtLOCDS";
						M_strSQLQRY = " SELECT distinct SW_LOCDS from CO_SWMST where isnull(SW_STSFL,' ') <> 'X'";					
						if(txtLOCDS.getText().length()>0)
							M_strSQLQRY += " where SW_LOCDS like '"+ txtLOCDS.getText().trim()+"%'"; 
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Location"},1,"CT");
					}
				}
				if(M_objSOURC==txtLICTP)
				{
					strHLPFLD2 = "txtLICTP";
					M_strSQLQRY ="SELECT distinct SW_LICTP from CO_SWMST where isnull(SW_STSFL,' ') <> 'X'";
					if(txtLICTP.getText().length()>0)
							M_strSQLQRY += " where SW_LICTP like '"+ txtLICTP.getText().trim()+"%'"; 
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Licence Type"},1,"CT");
				 }
				if(M_objSOURC==txtFLDVL)
				{
					if(cmbFLDNM.getSelectedItem().toString().trim().equals("ALL"))
					{
						 setMSG("query on value is not applicable for the Option 'ALL'",'E');
					 }
					else 
					{
						M_strSQLQRY = "SELECT distinct "+ cmbFLDNM.getSelectedItem().toString().trim() +" from CO_SWMST";
						M_strSQLQRY  +=" where isnull(SW_STSFL,' ') <> 'X'";
						cl_hlp(M_strSQLQRY,1,1,strARRHDR,1,"CT");
					}
				}
					strHLPFLD = "txtFLDVL";
					setCursor(cl_dat.M_curDFSTS_pbst);
			}		
		else if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		 {
			if(M_objSOURC==txtSFTCD)
			{
				try
				{
					String L_strSFTCD="";
					String L_strPSFTCD ="";	
					String L_strTEMPS="";
					int L_intSRLN0=0;
					if(tblSWDTL.getSelectedRow()==0)
							getSRLNO();
					//else if(tblSWDTL.getSelectedRow()<=tblSWDTL.getRowCount())
					if(tblSWDTL.getSelectedRow()>=1)
						{
							L_strPSFTCD=tblSWDTL.getValueAt(tblSWDTL.getSelectedRow()-1,TB1_SFTCD).toString().trim();
							L_strSFTCD=tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_SFTCD).toString().trim();
							if(L_strSFTCD.equals(L_strPSFTCD))
							{
								L_intSRLN0=Integer.parseInt(tblSWDTL.getValueAt(tblSWDTL.getSelectedRow()-1,TB1_SRLNO).toString().trim());
								L_intSRLN0=L_intSRLN0+1;
								String ss="0";
								tblSWDTL.setValueAt(ss+String.valueOf(L_intSRLN0),tblSWDTL.getSelectedRow(),TB1_SRLNO);								
							}						
							else
							{
								int L_TSRLNO=0;
								int L_intMAX=0;
							    L_strTEMPS=tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_SFTCD).toString().trim();
								for(int i=0;i<=tblSWDTL.getSelectedRow()-1;i++)
									{
										if(L_strTEMPS.equals(tblSWDTL.getValueAt(i,TB1_SFTCD)))
										{
											L_TSRLNO=Integer.parseInt(tblSWDTL.getValueAt(i,TB1_SRLNO).toString().trim());
											L_intMAX=L_TSRLNO;
											L_TSRLNO=L_intMAX;
										}
									}
										if(L_TSRLNO>=1)
										{
											String ss="0";
											tblSWDTL.setValueAt(ss+String.valueOf(L_TSRLNO+1),tblSWDTL.getSelectedRow(),TB1_SRLNO);
										}
										if(L_TSRLNO==0)
										getSRLNO();	
							}
					}
				}
				catch(Exception L_EX)
				{
				setMSG(L_EX,"SRLNO1");
				}
			}	            
			if(M_objSOURC==txtFLDVL)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			if(!cmbFLDNM.getSelectedItem().equals("ALL"))
			{
				txtFLDVL.requestFocus();
				setMSG("please select the options.",'N');
			}
			if(M_objSOURC==tblSWDTL)
			{
				if(tblSWDTL.getSelectedColumn() == TB1_SFTCD)
				{
					tblSWDTL.setColumnSelectionInterval(TB1_SFTCD,TB1_SRLNO);
					tblADDTL.setValueAt(tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_SFTCD),tblSWDTL.getSelectedRow(),TB2_SFTCD);
				}
				else if(tblSWDTL.getSelectedColumn() == TB1_SRLNO)
				{
					tblSWDTL.setColumnSelectionInterval(TB1_SRLNO,TB1_LICTP);
					tblADDTL.setValueAt(tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_SRLNO),tblSWDTL.getSelectedRow(),TB2_SRLNO);
				}
				else if(tblSWDTL.getSelectedColumn() == TB1_LICTP)
					tblADDTL.setColumnSelectionInterval(TB1_LICTP,TB2_LICNO);
				else if(tblADDTL.getSelectedColumn() == TB2_LICNO)
					tblADDTL.setColumnSelectionInterval(TB2_LICNO,TB1_VERNO);
				else if(tblSWDTL.getSelectedColumn() == TB1_VERNO)
					tblSWDTL.setColumnSelectionInterval(TB1_VERNO,TB1_USRNO);
				else if(tblSWDTL.getSelectedColumn() == TB1_USRNO)
					tblSWDTL.setColumnSelectionInterval(TB1_USRNO,TB1_LOCDS);
				else if(tblSWDTL.getSelectedColumn() == TB1_LOCDS)
				{
					tblSWDTL.setColumnSelectionInterval(TB1_LOCDS,TB1_SFTCD);//
				}
			}
	   		else if(M_objSOURC==tblADDTL)
			{
				if(tblADDTL.getSelectedColumn() == TB2_SFTCD)
				{
					tblADDTL.setColumnSelectionInterval(TB2_SFTCD,TB2_SRLNO);
				}
				else if(tblADDTL.getSelectedColumn() == TB2_SRLNO)
				{
					tblADDTL.setColumnSelectionInterval(TB2_SRLNO,TB2_PRCDT);
				}
				else if(tblADDTL.getSelectedColumn() == TB2_PRCDT)
					tblSWDTL.setColumnSelectionInterval(TB2_PRCDT,TB1_PRCFR);
				else if(tblSWDTL.getSelectedColumn() == TB1_PRCFR)
					tblSWDTL.setColumnSelectionInterval(TB1_PRCFR,TB2_UPGDT);
				else if(tblADDTL.getSelectedColumn() == TB2_UPGDT)
					tblADDTL.setColumnSelectionInterval(TB2_UPGDT,TB2_EXPDT);
				else if(tblADDTL.getSelectedColumn() == TB2_EXPDT)
					tblADDTL.setColumnSelectionInterval(TB2_EXPDT,TB2_CUSID);
				else if(tblADDTL.getSelectedColumn() == TB2_CUSID)
				{
					tblADDTL.setColumnSelectionInterval(TB2_CUSID,TB2_SFTCD);
				}
			}
			/*if((M_objSOURC==txtSFTCD)&&(M_objSOURC==txtSRLNO))
			{               
			for(int i=0;i<tblSWDTL.getRowCount();i++)
					{        
						if(tblSWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
		   				{
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_CHKFL),i,TB2_CHKFL);
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SFTCD),i,TB2_SFTCD);
							tblADDTL.setValueAt(tblSWDTL.getValueAt(i,TB1_SRLNO),i,TB2_SRLNO);
						}	
					}
			}*/
		}
	}
		catch(Exception L_EX)
		{
    	    setMSG(L_EX ," F1 help..");    	
			setCursor(cl_dat.M_curDFSTS_pbst);		
		}    	    
	}
	public void getSRLNO()
	{
		try
		{
			ResultSet M_rstRSSET1;
			String M_strSQLQRY1;
			M_strSQLQRY1="select max(SW_SRLNO)L_SRLNO from CO_SWMST where SW_SFTCD='"+txtSFTCD.getText()+"' and isnull(SW_STSFL,' ') <> 'X' ";
			M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY1);
			if(M_rstRSSET1 !=null)
			{
				strSRLNO1="";
				intSRLNO1=0;
				String ss="0";
			if(M_rstRSSET1.next())
			{	 
				intSRLNO1=M_rstRSSET1.getInt("L_SRLNO");
				intSRLNO1=intSRLNO1+1;
				strSRLNO1=String.valueOf(intSRLNO1);
				if(strSRLNO1.toString().length()<=1)
				{
					strSRLNO1=ss+strSRLNO1;
				 }
			tblSWDTL.setValueAt(strSRLNO1,tblSWDTL.getSelectedRow(),TB1_SRLNO);
			}     
				M_rstRSSET1.close();
			}
		}
		catch(Exception L_EX)
		{
    		setMSG(L_EX ," F1 SRLNO..");    	
			setCursor(cl_dat.M_curDFSTS_pbst);		
		}  
	}
		public void exeHLPOK()
		{
			try
			{
				super.exeHLPOK();
				if(strHLPFLD == "txtFLDVL")
				{
					txtFLDVL.setText(cl_dat.M_strHLPSTR_pbst);	
					txtFLDVL.setEnabled(true);
					txtFLDVL.requestFocus();
					if (txtFLDVL.getText().length()<=8)			
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				 if(M_strHLPFLD== "txtSFTCD")
				{
					txtSFTCD.setText(cl_dat.M_strHLPSTR_pbst);
					txtSFTCD.requestFocus();
				}
				if(strHLPFLD2== "txtLICTP")
				{
					txtLICTP.setText(cl_dat.M_strHLPSTR_pbst);
					txtLICTP.requestFocus();
				}
				if(strHLPFLD1== "txtLOCDS")
				{
					txtLOCDS.setText(cl_dat.M_strHLPSTR_pbst);
					txtLOCDS.requestFocus();
				}
				cl_dat.M_flgHELPFL_pbst = false;
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"in Child.exeHLPOK");
			}
		}
	void exeSAVE()
	{
		if (!vldDATA())
			return;	
		else 
			setMSG("",'E');
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;	
				for(int i=0;i<tblSWDTL.getRowCount();i++)
				{
					if(tblSWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
					{
						M_strSQLQRY ="UPDATE CO_SWMST SET ";
						M_strSQLQRY += "SW_SFTNM = '" + tblSWDTL.getValueAt(i,TB1_SFTNM).toString().trim()+"',";
						M_strSQLQRY += "SW_LICTP = '" + tblSWDTL.getValueAt(i,TB1_LICTP).toString().trim()+"',";	
						M_strSQLQRY += "SW_LICNO = '" + tblADDTL.getValueAt(i,TB2_LICNO).toString().trim()+"',";
						M_strSQLQRY += "SW_VERNO = '" +tblSWDTL.getValueAt(i,TB1_VERNO).toString().trim() +"',";
						M_strSQLQRY += "SW_USRNO = '" +tblSWDTL.getValueAt(i,TB1_USRNO).toString().trim()+"',";
						M_strSQLQRY += "SW_LOCDS = '" + tblSWDTL.getValueAt(i,TB1_LOCDS).toString().trim()+"',";
			
						if(tblADDTL.getValueAt(i,TB2_PRCDT).toString().trim().length() >0)
						{
							M_strSQLQRY +="SW_PRCDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_PRCDT).toString().trim()))+"',";
						}
						M_strSQLQRY +="SW_PRCFR = '" + tblSWDTL.getValueAt(i,TB1_PRCFR).toString().trim()+"',";
				
						if(tblADDTL.getValueAt(i,TB2_UPGDT).toString().trim().length() >0)
						{
							M_strSQLQRY +="SW_UPGDT ='" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_UPGDT).toString().trim()))+"',";
						}
			
						if(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim().length() >0)
						{
							M_strSQLQRY += "SW_EXPDT ="+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim()))+"',";
						}
						M_strSQLQRY +="SW_CUSID = '"+tblADDTL.getValueAt(i,TB2_CUSID).toString().trim()+"',";
						M_strSQLQRY +="SW_REMDS = '"+tblADDTL.getValueAt(i,TB2_REMDS).toString().trim()+"',";
						M_strSQLQRY +="SW_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				
						M_strSQLQRY +="SW_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						//M_strSQLQRY +=getUSGDTL("SW",'U',"");
						M_strSQLQRY += " WHERE SW_SFTCD = '"+tblSWDTL.getValueAt(i,TB1_SFTCD).toString().trim()+"'";
						M_strSQLQRY += " AND SW_SRLNO = '"+tblSWDTL.getValueAt(i,TB1_SRLNO).toString().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						setMSG("Data has been Changed",'N');
						tblSWDTL.clrTABLE();
						tblADDTL.clrTABLE();
						txtFLDVL.setText("");
					}
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);				
			 //cl_dat.M_flgLCUPD_pbst = true;
		        for(int i=0;i<tblSWDTL.getRowCount();i++)
				{
					if(tblSWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
					{
						int L_intROWNO =0;
						//M_strSQLQRY  ="Delete from CO_SWMST ";
						M_strSQLQRY  ="update CO_SWMST SET ";
						M_strSQLQRY +="SW_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY +="SW_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY +="SW_STSFL= 'X'";
						//M_strSQLQRY +=  getUSGDTL("SW",'U',"X");
						M_strSQLQRY  += " WHERE SW_SFTCD ='" +tblSWDTL.getValueAt(i,TB1_SFTCD).toString().trim() +"'";
						M_strSQLQRY  += " AND SW_SRLNO ='" +tblSWDTL.getValueAt(i,TB1_SRLNO).toString().trim() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}		
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			 else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{			//cl_dat.M_flgLCUPD_pbst = true;
						this.setCursor(cl_dat.M_curWTSTS_pbst);
				for(int i=0;i<tblSWDTL.getRowCount();i++)
				{
					if(tblSWDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
					{
						M_strSQLQRY ="INSERT INTO CO_SWMST(SW_SFTCD,SW_SRLNO,SW_SFTNM,SW_LICTP,SW_LICNO,SW_VERNO,SW_USRNO,SW_LOCDS,SW_PRCDT,SW_PRCFR,SW_EXPDT,SW_UPGDT,SW_CUSID,SW_REMDS,SW_LUSBY,SW_LUPDT) VALUES(";
						M_strSQLQRY += "'" + tblSWDTL.getValueAt(i,TB1_SFTCD).toString().trim()+"',";
						M_strSQLQRY += "'" + tblSWDTL.getValueAt(i,TB1_SRLNO).toString().trim()+"',";
						M_strSQLQRY += "'" + tblSWDTL.getValueAt(i,TB1_SFTNM).toString().trim()+"',";
						M_strSQLQRY += "'" + tblSWDTL.getValueAt(i,TB1_LICTP).toString().trim()+"',";
						M_strSQLQRY += "'" + tblADDTL.getValueAt(i,TB2_LICNO).toString().trim()+"',";
						M_strSQLQRY += "'" + tblSWDTL.getValueAt(i,TB1_VERNO).toString().trim()+"',";
						M_strSQLQRY += "'" + tblSWDTL.getValueAt(i,TB1_USRNO).toString().trim()+"',";
						M_strSQLQRY += "'" + tblSWDTL.getValueAt(i,TB1_LOCDS).toString().trim()+"',";
					
						if(tblADDTL.getValueAt(i,TB2_PRCDT).toString().trim().length() >0)
							M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_PRCDT).toString().trim()))+"',";
						else
							M_strSQLQRY += null +",";
						M_strSQLQRY += "'" +tblSWDTL.getValueAt(i,TB1_PRCFR).toString().trim()+"',";
					
						if(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim().length() >0)		
							M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_EXPDT).toString().trim()))+"',";
						else
							M_strSQLQRY += null +",";
						if(tblADDTL.getValueAt(i,TB2_UPGDT).toString().trim().length() >0)
							M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(tblADDTL.getValueAt(i,TB2_UPGDT).toString().trim()))+"',";
						else
							M_strSQLQRY += null +",";
							M_strSQLQRY += "'" +tblADDTL.getValueAt(i,TB2_CUSID).toString().trim()+"',";
							M_strSQLQRY += "'" +tblADDTL.getValueAt(i,TB2_REMDS).toString().trim()+"',";
							M_strSQLQRY += "'" +cl_dat.M_strUSRCD_pbst+"',";
					
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						//M_strSQLQRY +=getUSGDTL("SW",'I',"");
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(cl_dat.M_flgLCUPD_pbst)
						{	
							setMSG("Data has been added..",'N');
						}
						setMSG("Data has been Saved",'N');
						tblSWDTL.clrTABLE();
						tblADDTL.clrTABLE();
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
			setMSG(L_SE,"exeADDREC");
		}	
	}
	private void getDATA(String L_strFLDNM,String L_strFLDVL)
	{
		try
		{ 
			int L_intROWNO =0;
			java.sql.Date L_tmpDATE;
			String L_strTEMP="";
			System.out.println("query");
			M_strSQLQRY  =" Select * from CO_SWMST ";
			M_strSQLQRY+= " where isnull(SW_STSFL,' ') <> 'X'  ";
			if(!L_strFLDNM.trim().equals("ALL"))
			{
				if(L_strFLDVL.trim().length() >0)
				{
					M_strSQLQRY  += " AND "+ L_strFLDNM.trim() +"  = '"+L_strFLDVL.trim() +"'" ;					
				}
			}
			M_rstRSSET =  cl_dat.exeSQLQRY(M_strSQLQRY);
			int L_intSRLNO=0;
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_SFTCD"),""),L_intROWNO,TB1_SFTCD);
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_SRLNO"),""),L_intROWNO,TB1_SRLNO);
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_SFTNM"),""),L_intROWNO,TB1_SFTNM);
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_LICTP"),""),L_intROWNO,TB1_LICTP);
					tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_LICNO"),""),L_intROWNO,TB2_LICNO);
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_VERNO"),""),L_intROWNO,TB1_VERNO);
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_USRNO"),""),L_intROWNO,TB1_USRNO);
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_LOCDS"),""),L_intROWNO,TB1_LOCDS);
					tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_REMDS"),""),L_intROWNO,TB2_REMDS);

					tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_SFTCD"),""),L_intROWNO,TB2_SFTCD);
					tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_SRLNO"),""),L_intROWNO,TB2_SRLNO);
					tblSWDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_PRCFR"),""),L_intROWNO,TB1_PRCFR);
					
					L_tmpDATE = M_rstRSSET.getDate("SW_PRCDT");
					
					L_strTEMP="";
					if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
					tblADDTL.setValueAt(L_strTEMP,L_intROWNO,TB2_PRCDT);
					
					L_strTEMP="";
					L_tmpDATE =M_rstRSSET.getDate("SW_UPGDT");
					if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
					tblADDTL.setValueAt(L_strTEMP,L_intROWNO,TB2_UPGDT);
					
					L_strTEMP="";
					L_tmpDATE =M_rstRSSET.getDate("SW_EXPDT");
					if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
					tblADDTL.setValueAt(L_strTEMP,L_intROWNO,TB2_EXPDT);
					tblADDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_CUSID"),""),L_intROWNO,TB2_CUSID);
					L_intROWNO ++;
				}
			M_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getdata");
		}
	}
	boolean vldDATA()
	{
		try
		{	if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
					if(tblSWDTL.isEditing())
						tblSWDTL.getCellEditor().stopCellEditing();
					tblSWDTL.setRowSelectionInterval(0,0);
					tblSWDTL.setColumnSelectionInterval(0,0);
					if(tblADDTL.isEditing())
						tblADDTL.getCellEditor().stopCellEditing();
					tblADDTL.setRowSelectionInterval(0,0);
					tblADDTL.setColumnSelectionInterval(0,0);
			}
			if((tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_SFTNM).toString().length()<1)&&(tblADDTL.getValueAt(tblADDTL.getSelectedRow(),TB2_LICNO).toString().length()<1)&&(tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_USRNO).toString().length()<1))
			{
				setMSG("software name &userno& License no should not be null",'E');
				return false;
			}
			
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
		{
			boolean L_flgCHKFL= false;
			for(int i=0; i<tblSWDTL.getRowCount(); i++)
			{				
				if (tblSWDTL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
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
			if (L_flgCHKFL== false)
			{
				setMSG("No row Selected..",'E');				
				return false;
			}			
		}	
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			if(txtSFTCD.getText().toString().length()<1)
			{
				setMSG("code should be there select from f1 or create new",'E');
				return false;
			}
			else if(txtSFTCD.getText().toString().length()>1)
			{
				if(txtSRLNO.getText().toString().length()<1)
				{
					setMSG("Srlno should be vaild  ",'E');
					return false;
				}
				else
				{
					return true;
				}
			}
		}
		if(txtSRLNO.getText().toString().length()<=intSRLNO1)
		{
			setMSG("SRLNO is primary key not again in same code",'E');
			return false;
		}
			if(!cmbFLDNM.getSelectedItem().equals("ALL"))
			if(txtFLDVL.getText().toString().length()<1)
			{
				setMSG("Wrong data",'E');
				return false;
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vlddata");
			return true;
		}
	}	
	/*class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{				
				if(input == txtFLDVL)
				{					
					if (txtFLDVL.getText().trim().length() == 5)
					{
						try
						{
							/*M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST" +
								" where PT_PRTCD = '" + strPRTCD + "' and PT_PRTTP = 'T'" +
								" and isnull(PT_STSFL,'' ) <> 'X'";			
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if (M_rstRSSET !=null)
							{
								if(M_rstRSSET.next())
								{
									//txtFLDVL.setText(M_rstRSSET.getString("PT_PRTNM"));
									M_rstRSSET.close();						
								}
							}
							else
							{
								setMSG("No Data found ..",'E');
								return false;				
							}
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"txtPRTCD");
						}
					}	
					else if(txtFLDVL.getText().trim().length() != 5)
					{
						if(txtFLDVL.getText().trim().length() == 0)
							setMSG("Enter SOftware code or Press F1 for Help..",'N');
						else
							setMSG("Invalid Software code,Press F1 for Help..",'E');
						return false;
					}
				}		
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;	
		}
	}
	/*private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try  
			{
				String strTEMP =""
				if(P_intCOLID==TB1_SFTCD)
				{
					strTEMP = tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_SFTCD).toString();
					if(strTEMP.length() == 0)
						return true;
					if((strTEMP.indexOf("'") >=0)||(strTEMP.indexOf("\"") >=0)||(strTEMP.indexOf(" ") >=0)||(strTEMP.indexOf(",") >=0)||(strTEMP.indexOf("\\") >=0)||(strTEMP.indexOf("/") >=0))
					{
						setMSG("Characters like \', \", \\, /, blank & , are not allowed in Lorry number field",'E');
						return false;
					}
				}
				if(P_intCOLID==TB1_SFTCD)
				{
					strTEMP = tblSWDTL.getValueAt(tblSWDTL.getSelectedRow(),TB1_SFTCD).toString();
					if(strTEMP.length() == 0)
						return true;
					if((strTEMP.indexOf("'") >=0)||(strTEMP.indexOf("\"") >=0)||(strTEMP.indexOf(",") >=0)||(strTEMP.indexOf("\\") >=0)||(strTEMP.indexOf("/") >=0))
					{
						setMSG("Characters like \', \", \\, / are not allowed in ",'E');
						return false;
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"table verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}*/
}
	
	


