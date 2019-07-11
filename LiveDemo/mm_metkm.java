/*
System Name   : Material Management System
Program Name  : Entry form for Tank Master and Tank Calibration
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : Mar 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

import javax.swing.InputVerifier;
import java.sql.*;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.table.*;import javax.swing.JComponent;
import java.awt.event.*;import java.util.Hashtable;

/**
<P><PRE style = font-size : 10 pt >
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Tank Master  & Calibration

<b>Purpose :</b> Tank & it's calibration details are captured in this module. This information
is used for calculation of quantity stored in Tanks.

List of tables used :
Table Name     Primary key                         Operation done
                                             Insert  Update  Query  Delete	
-----------------------------------------------------------------------------
MM_TKMST       TK_TNKNO                         #      #      #       #
MM_TKCTR       TKC_TNKNO,TKC_DEPCT              #      #      #       #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                  #
CO_CTMST       CT_MATCD                                       #
-----------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name    Column Name   Table name   Type/Size       Description
-----------------------------------------------------------------------------
txtTNKNO      TK_TNKNO      MM_TKMST     Varchar(20)     Tank No.	
cmbTNKTP      TK_TNKTP      MM_TKMST     Varchar(2)      Tank Type
txtTMPVL      TK_TMPVL      MM_TKMST     Decimal(6,2)    Tank Tempreture
txtMATCD      TK_MATCD      MM_TKMST     Varchar(10)     Material Code
txtMATDS      TK_MATDS      MM_TKMST     Varchar(45)     Material Description
txtSTKQT      TK_STKQT      MM_TKMST     Decimal(12,3)   Stock Quantity
txtDEPCT      TK_DEPCT      MM_TKMST     Decimal(6,1)    Tank Depth
txtDEPVL      TK_DEPVL      MM_TKMST     Decimal(15,4)   Tank Volume
txtDEPQT      TK_DEPQT      MM_TKMST     Decimal(12,3)   Tank Quantity
txtMNDVL      TK_MNDVL      MM_TKMST     Decimal(6,1)    Minimum Dip
txtMXDVL      TK_MXDVL      MM_TKMST     Decimal(6,1)    Maximum Dip
txtCLBDT      TK_CLBDT      MM_TKMST     Date            Calibration Date
TBL_DEPTH     TKC_DEPCT     MM_TKCTR     Decimal(6,1)    Dip Value
TBL_VDPTH     TKC_DEPVL     MM_TKCTR     Decimal(15,4)   Volume Depth
TBL_INCLV     TKC_INCVL     MM_TKCTR     Decimal(11,4)   Incremental Volume 
-----------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description      Display Columns        Table Name
-----------------------------------------------------------------------------
txtTNKNO    Tank No,Tank Type        TK_TNKNO,TK_TNKTP      MM_TKMST
txtMATCD    Item Code,Description    CT_MATCD,CT_MATDS      CO_CTMST
-----------------------------------------------------------------------------

Tables Used For Query :</b>1)CO_CTMST             
                       2)MM_TKMST
                       3)MM_TKCTR
                       4)CO_CDTRN
<B>Queries:</b>
<I><B>To insert New Record :</B> 
       i) First Data is inserted in the Master Table, MM_TKMST
 for fields TK_TNKNO, TK_TMPVL, TK_MATCD, TK_STKQT, TK_TNKTP, TK_DEPCT, TK_DEPVL, TK_DEPQT,
 TK_MNDVL, TK_MXDVL, TK_CLBDT, TK_TRNFL, TK_STSFL, TK_LUSBY, TK_LUPDT
       ii) If Master Entry is Successful then data is inserted in Table, MM_TKCTR
for fields TKC_TNKNO, TKC_DEPCT, TKC_DEPVL, TKC_INCVL, TKC_TRNFL, TKC_STSFL, TKC_LUSBY, TKC_LUPDT

<B>To Update Records : </B>
       i) First Master Table ( MM_TKMST ) Data is updated.
Except Tank Number & Tank Type all fields in Master table are updateable. 
       ii) If Master updation is Successful, Transaction Records are Updated. Except tank No. & Depth
all fields are updateable.

<B>To Delete Records : </B>
       i) For Deletation Status flag is updated to 'X'.
       ii) If all Records from Transaction Table are marked as 'X' & Successful, then Corresponding 
Master Record is also marked as 'X' for Deletion.
</I>
Validations : 
   - Dip Value can not be greater than Tank Depth.   
   - To indicate Deletion TKC_STSFL is marked as 'X'
   - For addition Tank Number & Depth must be unique.
   - Sum of the Volume Depth entered in the Table canot be greater than Tank volume.
   - If modification is Selected, Tank number, Tank type & Depth are not alowed to modify. but new 
      Depth value insertion is allowed.
   - Existing & Deleted Tank numbers (marked 'X') are not allowed to reenter to add new Record.
   - Tank Type is Taken from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXTNK'
*/

class mm_metkm extends cl_pbase implements KeyListener, FocusListener
{									/** Final Integer for Check Flag Column Count.*/	
	final int TBL_CHKFL = 0;		/** Final Integer for Serial Number Column Count .*/	
	final int TBL_SRLNO = 1;		/** Final Integer for Depth Column Count.*/	
	final int TBL_DEPTH = 2;		/** Final integer for Depth Value Column Count.*/	
	final int TBL_VDPTH = 3;		/** Final Integer for Incremental Volume Column Count.*/	
	final int TBL_INCLV = 4;		/** JTextField to accept & display the Tank Number.*/
	private JTextField txtTNKNO;	/** JTextField to accept & display the Temprature Value.*/	
	private JTextField txtTMPVL;	/** JTextField to accept & display the Matrial Code.*/	
	private JTextField txtMATCD;	/** JTestField to display Material Description.*/	
	private JTextField txtMATDS;	/** JTextField to accept & display the Available Stock.*/	
	private JTextField txtSTKQT;	/** JTextField to accept & display the Tank Depth.*/	
	private JTextField txtDEPCT;	/** JTextField to accept & display the Tank Volume.*/	
	private JTextField txtDEPVL;	/** JTextField to accept & display the Tank Storage Quantity.*/	
	private JTextField txtDEPQT;	/** JTextField to accept & display the Minimum Dip Volume.*/	
	private JTextField txtMNDVL;	/** JTextField to accept & display the Maximum Dip Value.*/	
	private JTextField txtMXDVL;	/** JTextFiled to accept & display Calibration Date.*/	
	private JTextField txtCLBDT;	/** JComboBox to display & select Tank Type.*/		
	private JComboBox cmbTNKTP;		/** JTable to accept & display details about Tank as Depth, Volume Depth and Incremental Depth.*/	
	private cl_JTable tblITMDT;		/** Integer variable to count Records Fetched.*/			
	private int intRECCT;			/** Integer variable to count the Row Displayed in the Details Table.*/		
	private int intSRLNO =0;		/** Float variable for Tank Depth.*/	
	private float fltTNKDP;			/** Float variable for Tank Volume.*/	
	private float fltTNKVL;			/** Float varialble for Tank Quantity.*/	
	private float fltTNKQTY;		/** Float variable for Minumum Dip Quantity.*/	
	private float fltMINDIP;		/** Float variable for Maximum Dip Quantity.*/	
	private float fltMAXDIP;		/** String variable for Tanker Number.*/			
	private String strTNKNO;		/** String variable for Temprature.*/	
	private String strTMPVL;		/** String varaible for Material code.*/	
	private String strMATCD;		/** String variable for Material Description.*/	
	private String strSTKQT;		/** String variable for Tank Type.*/	
	private String strTNKTP;		/** String variable for Tank Depth.*/	
	private String strDEPCT;		/** String varaible for Tank Depth Volume.*/
	private String strDEPVL;		/** String varaible for Tank Storage Quantity.*/
	private String strDEPQT;		/** String variable for Minimum Dip Value.*/	
	private String strMNDVL;		/** String variable for Maximum Dip Value.*/	
	private String strMXDVL;		/** String variable  for Temporary Data.*/
	private	String strTEMP1;		/** String variable for Calibration Date.*/
	private String strCLBDT;		/** Innear class for Table Input Data Verification.*/
	private TableInputVerifier TBLINPVF;/** Innear class object for Table Input Data Verification.*/
	private TBLINPVF objTBLVRF;			/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();	/** JTextField to attach with Depth Column of the Table to accept Input.*/	
	private JTextField txtDEPTH;	/** JTextFiled to attach with Volume depth Column of the Table to accept Input.*/
	private JTextField txtDPTHV;	/** JTextField to attached with Incremental Volume Column of the Table to accept the Input.*/		
	private JTextField txtINCLV;	/** Date Variable for Calibration Date.*/
	private Date datCLBDT;					/** Result Set object to Fetch Data from Data Base.*/	
	private ResultSet M_rstRSSET1;			/** String variable for Help Field.*/
	private String strHLPFLD;				
	private int i;
	private Hashtable<String,String> hstDELIT;
	mm_metkm()
	{	
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Tank No."),2,2,1,.9,this,'R');
			add(txtTNKNO = new TxtLimit(10),2,3,1,1,this,'L');
			add(new JLabel("Tank Type"),2,4,1,.9,this,'R');
			add(cmbTNKTP = new JComboBox(),2,5,1,1.2,this,'R');						
			add(new JLabel("Tank Tempt."),2,6,1,1.5,this,'L');
			add(txtTMPVL = new TxtNumLimit(6.2),2,7,1,1,this,'L');
			
			add(new JLabel("Mat. Code"),3,2,1,.9,this,'R');
			add(txtMATCD = new TxtLimit(10),3,3,1,1,this,'L');			
			add(txtMATDS = new JTextField(),3,4,1,2,this,'L');						
			add(new JLabel("Available Stock"),3,6,1,1.5,this,'L');
			add(txtSTKQT = new TxtNumLimit(12.3),3,7,1,1,this,'L');
			
			add(new JLabel("Tank Depth"),4,2,1,.9,this,'R');
			add(txtDEPCT = new TxtNumLimit(6.1),4,3,1,1,this,'R');			
			add(new JLabel("Tank Volume"),4,4,1,.9,this,'R');
			add(txtDEPVL = new TxtNumLimit(10.4),4,5,1,1,this,'L');						
			add(new JLabel("Tank Quantity"),4,6,1,1.5,this,'L');
			add(txtDEPQT = new TxtNumLimit(12.3),4,7,1,1,this,'L');
			
			add(new JLabel("Minimum Dip"),5,2,1,.9,this,'R');
			add(txtMNDVL = new TxtNumLimit(6.1),5,3,1,1,this,'L');			
			add(new JLabel("Maximum Dip"),5,4,1,.9,this,'R');
			add(txtMXDVL = new TxtNumLimit(6.1),5,5,1,1,this,'L');						
			add(new JLabel("Last Calib Date"),5,6,1,1.5,this,'L');
			add(txtCLBDT = new TxtDate(),5,7,1,1,this,'L');
			
			setCursor(cl_dat.M_curWTSTS_pbst);			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXTNK'";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
				{
					cmbTNKTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));				
				}								
				M_rstRSSET.close();
			}			
			String[] L_COLHD = {"Select","Sr.No.","Depth","Volume Depth","Incremental Volume"};
      		int[] L_COLSZ = {50,75,125,150,150};	    				
			tblITMDT = crtTBLPNL1(this,L_COLHD,600,7,2,10,6,L_COLSZ,new int[]{0});				
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);						
			objTBLVRF = new TBLINPVF();
			tblITMDT.setInputVerifier(objTBLVRF);		
			txtTNKNO.setInputVerifier(objINPVR);			
			tblITMDT.setCellEditor(TBL_DEPTH, txtDEPTH = new TxtLimit(6));
			tblITMDT.setCellEditor(TBL_VDPTH, txtDPTHV = new TxtLimit(15));
			tblITMDT.setCellEditor(TBL_VDPTH, txtINCLV = new TxtLimit(15));			
			txtDEPTH.addKeyListener(this);txtDEPTH.addFocusListener(this);
			txtDPTHV.addKeyListener(this);txtDPTHV.addFocusListener(this);
			txtINCLV.addKeyListener(this);txtINCLV.addFocusListener(this);
			setENBL(false);			
			setCursor(cl_dat.M_curDFSTS_pbst);
			hstDELIT = new Hashtable<String,String>();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"const");			
		}
	}
	/**
	 * Super class method override to inhance its functionality according to Requriment.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		clrCOMP();
		txtTNKNO.setEnabled(true);
		txtMATDS.setEnabled(false);
		tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblITMDT.cmpEDITR[TBL_SRLNO].setEnabled(false);	
		tblITMDT.cmpEDITR[TBL_DEPTH].setEnabled(false);
		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) 
		{
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(false);
		}			
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{		
			cmbTNKTP.setEnabled(true);
			txtTNKNO.setEnabled(true);
			txtTMPVL.setEnabled(true);
			txtMATCD.setEnabled(true);			
			txtSTKQT.setEnabled(true);
			txtDEPCT.setEnabled(true);
			txtDEPVL.setEnabled(true);
			txtDEPQT.setEnabled(true);
			txtMNDVL.setEnabled(true);
			txtMXDVL.setEnabled(true);
			txtCLBDT.setEnabled(true);	
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);									
			tblITMDT.cmpEDITR[TBL_DEPTH].setEnabled(true);
			tblITMDT.cmpEDITR[TBL_VDPTH].setEnabled(true);									
			tblITMDT.cmpEDITR[TBL_INCLV].setEnabled(true);									
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{			
			txtTMPVL.setEnabled(true);
			txtMATCD.setEnabled(true);			
			txtSTKQT.setEnabled(true);
			txtDEPCT.setEnabled(true);
			txtDEPVL.setEnabled(true);
			txtDEPQT.setEnabled(true);
			txtMNDVL.setEnabled(true);
			txtMXDVL.setEnabled(true);
			txtCLBDT.setEnabled(true);	
			tblITMDT.cmpEDITR[TBL_DEPTH].setEnabled(true);
			tblITMDT.cmpEDITR[TBL_VDPTH].setEnabled(true);									
			tblITMDT.cmpEDITR[TBL_INCLV].setEnabled(true);				
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);			
			if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				setENBL(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)					
				{
					txtTNKNO.requestFocus();					
					setMSG("Enter Tank Number OR Press F1 for Help..",'N');					
				}
				else
				{					
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					setMSG("Select an option to perform operation..",'N');
				}				
			}
			else if(M_objSOURC == txtTNKNO)			
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					String L_strTNKNO = txtTNKNO.getText().trim();
					clrCOMP();
					txtTNKNO.setText(L_strTNKNO);
					exeGETREC(L_strTNKNO);										
				}
			}	
			else if(M_objSOURC == txtMATCD)			// Item Code
			{	
				txtMATDS.setText("");
				if((txtMATCD.getText().trim()).length() != 0)
				{					
					try
					{
						M_strSQLQRY = "select CT_MATCD,CT_MATDS from CO_CTMST";
						M_strSQLQRY += " where CT_MATCD like '68%' and CT_MATCD ='" + txtMATCD.getText().trim()+"'";	
						M_strSQLQRY += " AND isnull(CT_MATCD,'') not in('X','9') ";
						M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET1!= null)
						{
							if(M_rstRSSET1.next())
							{
								txtMATDS.setText(M_rstRSSET1.getString("CT_MATDS"));
								M_rstRSSET1.close();																	
							}
							else 
							{
								setMSG("Invalid Material Code, Press F1 for Help..",'E');									
								txtMATCD.setText("");
							}
						}
						else
						{
							setMSG("Invalid Material Code, Press F1 for help..",'E');
							txtMATDS.setText("");
							txtMATCD.requestFocus();								
						}
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"vldSTACD");
					}												
				}
			}			
			else if(M_objSOURC == txtDEPCT)			// Tank Depth
			{										
				try
				{		
					fltTNKDP = Float.valueOf(txtDEPCT.getText().trim()).floatValue();					
					if(txtMXDVL.getText().trim().length() != 0)
					{						
						fltMAXDIP = Float.valueOf(txtMXDVL.getText().trim()).floatValue();
						if(fltTNKDP < fltMAXDIP)
						{							
							setMSG("Tank depth can not be less than Max Dip",'E');
							txtDEPCT.requestFocus();	
							return;
						}						
					}
					if(txtMNDVL.getText().trim().length() != 0)
					{
						fltMINDIP = Float.valueOf(txtMNDVL.getText().trim()).floatValue();
						if(fltTNKDP < fltMINDIP)
						{
							setMSG("Tank depth can not be less than Min Dip",'E');
							txtDEPCT.requestFocus();	
							return;
						}						
					}					
				}				
				catch(Exception NumberFormatException)
				{
					setMSG("Tank Depth cannot be blank, Enter Tank Depth..",'N');					
				}
			}
			else if(M_objSOURC == txtDEPVL)			// Tank Volume
			{	
				try
				{
					if(txtDEPVL.getText().trim().length() != 0)
					{
						//txtDEPVL.requestFocus();	
						//setMSG("",'N');
						//return;
					//}
					fltTNKVL = Float.valueOf(txtDEPVL.getText().trim()).floatValue();
					if(txtDEPQT.getText().trim().length() != 0)
					{
						fltTNKQTY = Float.valueOf(txtDEPQT.getText().trim()).floatValue();
						if(fltTNKVL < fltTNKQTY)
						{
							setMSG("Tank volume can not be less than Tank Quantity",'E');
							txtDEPVL.requestFocus();	
						}
					}						
				/*
					if(fltTNKVL < totVOLDPT())
					{
						setMSG("Tank volume should not be less than total volume depth (" + totVOLDPT() + ")",'E');
						txtDEPVL.requestFocus();	
					}*/
					}
				}
				catch(Exception NumberFormatException)
				{
					setMSG("Tank Volume must be Numeric",'E');
					txtDEPVL.requestFocus();
				}
			}
			else if(M_objSOURC == txtDEPQT)		// Tank Quantity
			{
				try
				{
					if(txtDEPQT.getText().trim().length() == 0)
					{
						/*txtMNDVL.requestFocus();	
						setMSG("Please Enter Tank Quantity..",'N');
						return;
					}*/
					fltTNKQTY = Float.valueOf(txtDEPQT.getText().trim()).floatValue();
					if(txtDEPVL.getText().trim().length() != 0)
					{
						fltTNKVL = Float.valueOf(txtDEPVL.getText().trim()).floatValue();
						if(fltTNKQTY > fltTNKVL)
						{
							setMSG("Tank Quantity can not be greater than Tank Volume",'E');
							txtDEPQT.requestFocus();	
							return;
						}					
					}
					}
				}
				catch(Exception NumberFormatException)
				{
					setMSG("Tank Quantity cannot be blank, Enter Tank quantity..",'N');
					txtDEPQT.requestFocus();
				}
			}
			else if(M_objSOURC == txtMNDVL)		// Min. Dip
			{
				try
				{
					if(txtMNDVL.getText().toString().trim().length() != 0)
					{
						fltMINDIP = Float.valueOf(txtMNDVL.getText().toString().trim()).floatValue();
						if(txtMNDVL.getText().toString().trim().length() > 8)
						{
							txtMNDVL.requestFocus();
							setMSG("Min Dip value should not exceed 7 digits ",'E');
							return;
						}
					}
					if(txtMXDVL.getText().toString().trim().length() != 0)
					{
						fltMAXDIP = Float.parseFloat(txtMXDVL.getText().trim());
						if(fltMINDIP > fltMAXDIP)
						{
							txtMNDVL.requestFocus();
							setMSG("Min Dip can not have value greater than Max Dip ",'E');							
							return;
						}						
					}						
					if(txtDEPCT.getText().toString().trim().length() != 0)
					{
						fltTNKDP = Float.valueOf(txtDEPCT.getText().toString().trim()).floatValue();
						if(fltMINDIP > fltTNKDP)
						{
							setMSG("Min Dip can not have value greater than Tank Depth..",'E');
							txtMNDVL.requestFocus();
							return;
						}					
					}							
				}				
				catch(Exception NumberFormatException)
				{
					setMSG("Min Dip value must be Numeric..",'E');
					txtMNDVL.requestFocus();
				}
			}
			else if(M_objSOURC == txtMXDVL)		// Max. Dip
			{
				try
				{
					if(txtMXDVL.getText().toString().trim().length() != 0)
					{						
						fltMAXDIP = Float.valueOf(txtMXDVL.getText().toString().trim()).floatValue();							
						if(txtMXDVL.getText().toString().trim().length() > 8)
						{
							setMSG("Max Dip value should not exceed 7 digits ",'E');
							txtMXDVL.requestFocus();
							return;
						}
					}
					if(txtMNDVL.getText().toString().trim().length() != 0)
					{
						fltMAXDIP = Float.valueOf(txtMXDVL.getText().trim()).floatValue();
						if(fltMINDIP > fltMAXDIP)
						{
							setMSG("Min Dip can not have value greater than Max Dip..",'E');
							txtMNDVL.requestFocus();
							return;
						}
					}
					if(txtDEPCT.getText().toString().trim().length() != 0)
					{						
						fltTNKDP = Float.valueOf(txtDEPCT.getText().toString().trim()).floatValue();										
						if(fltMAXDIP > fltTNKDP)
						{
							setMSG("Max Dip can not have value greater than Tank Depth ",'E');
							txtMXDVL.requestFocus();
							return;
						}							
					}					
				}
				catch(Exception NumberFormatException)
				{
					setMSG("Max Dip value must be Numeric",'E');
					txtMXDVL.requestFocus();
				}
			}
			else if((M_objSOURC == txtCLBDT) && ((txtCLBDT.getText().trim()).length()!=0))			// LCalib Date
			{				
				if (M_fmtLCDAT.parse(txtCLBDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			 
				{					
					txtCLBDT.requestFocus();
					setMSG("Calibration Date can not be greater than Todays Date..",'E');					
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
		if(M_objSOURC == tblITMDT.cmpEDITR[TBL_DEPTH])
	    {			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{									
				if(tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_SRLNO).toString().length() >0)
					 ((JTextField)tblITMDT.cmpEDITR[TBL_DEPTH]).setEditable(false);
				else				
					((JTextField)tblITMDT.cmpEDITR[TBL_DEPTH]).setEditable(true);
			}
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);			
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{			
			if (M_objSOURC ==txtTNKNO)
			{
				try
				{
					cl_dat.M_flgHELPFL_pbst = true;
					strHLPFLD = "txtTNKNO";
					String L_arrHDR[] = {"Tank No","Tank Type"};
					M_strSQLQRY = "select TK_TNKNO,TK_TNKTP from MM_TKMST where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TK_STSFL <> 'X'" ;            			
					//if (txtTNKNO.getText().length()>0)
					//M_strSQLQRY +=	"AND TK_TNKNO like "+txtTNKNO.getText().trim()+"%";
					clrCOMP();										
					cl_hlp(M_strSQLQRY,1,1,L_arrHDR,2,"CT");					
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"hlpTNKNO");
				}
			}								
			if (M_objSOURC ==txtMATCD)
			{
				try
				{
					cl_dat.M_flgHELPFL_pbst = true;
					strHLPFLD = "txtMATCD";
					String L_ARRHDR[] = {"Item Code","Description"};
					M_strSQLQRY = "select CT_MATCD,CT_MATDS from CO_CTMST ";
				    M_strSQLQRY += " where CT_MATCD like '68%' and CT_CODTP='CD' and CT_STSFL not in ('X','9')";													
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");					
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"hlpMATCD");
				}
			}						
		}	
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)	// Detail Table  //for tab key Pressed.
		{
			if(M_objSOURC ==txtTNKNO)
			{	
				if ((txtTNKNO.getText().trim()).length()==0)
				{
					//txtTNKNO.requestFocus();
					setMSG("Enter Tank Number OR Press F1 for Help..",'N');
				}
				else
				{
					txtTNKNO.setText((txtTNKNO.getText().trim()).toUpperCase());
					cmbTNKTP.requestFocus();
					setMSG("Select Tank Type..",'N');
				}
			}				
			else if(M_objSOURC == cmbTNKTP)
			{
				if(cmbTNKTP.getSelectedItem().toString().substring(0,2).equals("03"))
				   setMSG("Day tank can not be Added",'N');								   
				txtTMPVL.requestFocus();
				setMSG("Enter Tank Temprature..",'N');				
			}			
			else if(M_objSOURC == txtTMPVL)		// Tank Temp
			{
				if ((txtTMPVL.getText().trim()).length()== 0)
				{
					txtTMPVL.requestFocus();
					setMSG("Enter Tank Temprature..",'N');
				}
				else
				{
					txtMATCD.requestFocus();
					setMSG("Enter Material code OR Press F1 Key for help..",'N');
				}
			}
			if(M_objSOURC == txtMATCD)			// Item Code
			{
				if ((txtMATCD.getText().trim()).length()== 0)
				{
					txtMATCD.requestFocus();
					setMSG("Enter Material code OR Press F1 Key for help..",'N');
				}
				else
				{
					txtSTKQT.requestFocus();	
					setMSG("Enter Available Stock..",'N');
				}
			}
			else if(M_objSOURC == txtSTKQT)           // Available Srock
			{
				if ((txtSTKQT.getText().trim()).length()== 0)
				{
					txtSTKQT.requestFocus();
					setMSG("Enter Available Stock..",'N');
				}
				else
				{
					txtDEPCT.requestFocus();
					setMSG("Enter Tank Depth..",'N');
				}
			}
			else if(M_objSOURC == txtDEPCT)			// Tank Depth
			{
				if ((txtDEPCT.getText().trim()).length()== 0)
				{
					txtDEPCT.requestFocus();
					setMSG("Enter Tank Depth..",'N');
				}
				else
				{
					txtDEPVL.requestFocus();
					setMSG("Enter Tank volume..",'N');
				}
			}
			else if(M_objSOURC == txtDEPVL)			// Tank Volume
			{
				if ((txtDEPVL.getText().trim()).length()== 0)
				{
					txtDEPVL.requestFocus();
					setMSG("Enter Tank volume..",'N');
				}
				else
				{
					txtDEPQT.requestFocus();	
					setMSG("Enter Tank Quantity..",'N');
				}
			}	
			else if(M_objSOURC == txtDEPQT) //Tank quantity
			{
				if ((txtDEPQT.getText().trim()).length()== 0)
				{
					txtDEPQT.requestFocus();	
					setMSG("Enter Tank Quantity..",'N');
				}
				else
				{
					txtMNDVL.requestFocus();
					setMSG("Enter Minimum Dip value..",'N');
				}
			}
			else  if (M_objSOURC == txtMNDVL)
			{
				if ((txtMNDVL.getText().trim()).length()== 0)
				{
					txtMNDVL.requestFocus();
					setMSG("Enter Minimum Dip value..",'N');
				}
				else
				{
					txtMXDVL.requestFocus();
					setMSG("Enter Maximum Dip value..",'N');
				}
			}
			else  if (M_objSOURC == txtMXDVL)
			{
				if ((txtMXDVL.getText().trim()).length()== 0)
				{
					txtMXDVL.requestFocus();
					setMSG("Enter Maximum Dip value..",'N');
				}
				else
				{
					txtCLBDT.requestFocus();
					setMSG("Enter Last Calibration Date..",'N');
				}
			}
			else if (M_objSOURC == txtCLBDT)
			{
				if ((txtCLBDT.getText().trim()).length()== 0)
				{
					txtCLBDT.requestFocus();
					setMSG("Enter Last Calibration Date..",'N');
				}
				else
				{
					tblITMDT.requestFocus();
					setMSG("Please Enter Depth..",'N');
				}
			}
			if(M_objSOURC == tblITMDT)
			{
				//tblITMDT.setRowSelectionInterval(0,0);
				//tblITMDT.setColumnSelectionInterval(1,1);				
				/*if(tblITMDT.getSelectedColumn() == TBL_DEPTH)
				{
					if((tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_VDPTH).toString().trim()).length()==0)
					{
						tblITMDT.cmpEDITR[TBL_DEPTH].requestFocus();											
						setMSG("Depth cannot be blank, Please Enter Depth..",'N');
					}
					else
					{					
						tblITMDT.editCellAt(tblITMDT.getSelectedRow(),TBL_VDPTH);
						tblITMDT.cmpEDITR[TBL_VDPTH].requestFocus();											
						setMSG("Enter volume Depth..",'N');
					}
				}				
				if(tblITMDT.getSelectedColumn() == TBL_VDPTH)
				{
					tblITMDT.editCellAt(tblITMDT.getSelectedRow(),TBL_INCLV);
					tblITMDT.cmpEDITR[TBL_INCLV].requestFocus();					
					setMSG("Enter Incremental Volume..",'N');
				}				
				if(tblITMDT.getSelectedColumn() == TBL_INCLV)
				{
					tblITMDT.editCellAt(tblITMDT.getSelectedRow()+1, TBL_INCLV);
					tblITMDT.cmpEDITR[TBL_DEPTH].requestFocus();					
					setMSG("Please Enter Depth..",'N');
				}*/				
			}
		}			
	}
				
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	 */
	void exeSAVE()
	{
		try
		{
			String L_strDEPCT;
			String L_strDEPVL;
			String L_strINCVL;
			cl_dat.M_flgLCUPD_pbst = true;
			this.setCursor(cl_dat.M_curWTSTS_pbst);			
			if(!vldDATA())
				return ;
			else
				setMSG("",'E');									
			strTNKNO = txtTNKNO.getText().trim();
			strTMPVL = txtTMPVL.getText().trim();
			strMATCD = txtMATCD.getText().trim();
			strSTKQT = txtSTKQT.getText().trim();			
			strTNKTP = cmbTNKTP.getSelectedItem().toString().substring(0,2);
			strDEPCT = txtDEPCT.getText().trim();
			strDEPVL = txtDEPVL.getText().trim();
			strDEPQT = txtDEPQT.getText().trim();
			strMNDVL = txtMNDVL.getText().trim();
			strMXDVL = txtMXDVL.getText().trim();
			try
			{
				strCLBDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCLBDT.getText().trim()));	
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"error");
			}
			 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY = "Insert into MM_TKMST(TK_CMPCD,TK_TNKNO,TK_TMPVL,TK_MATCD,";
				M_strSQLQRY += "TK_STKQT,TK_TNKTP,TK_DEPCT,TK_DEPVL,TK_DEPQT,";
				M_strSQLQRY += "TK_MNDVL,TK_MXDVL,TK_CLBDT,TK_TRNFL,TK_STSFL,TK_LUSBY,TK_LUPDT)";
				M_strSQLQRY += " values( ";
				M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
				M_strSQLQRY += "'" + strTNKNO.toUpperCase() + "',";
				M_strSQLQRY += strTMPVL + ",";
				M_strSQLQRY += "'" + strMATCD + "',";
				M_strSQLQRY += strSTKQT + ",";
				M_strSQLQRY += "'" + strTNKTP + "',";
				M_strSQLQRY += strDEPCT + ",";
				M_strSQLQRY += strDEPVL + ",";
				M_strSQLQRY += strDEPQT + ",";
				M_strSQLQRY += strMNDVL + ",";
				M_strSQLQRY += strMXDVL + ",'";
				M_strSQLQRY += strCLBDT + "',";
				M_strSQLQRY += getUSGDTL("TK",'I',"")+")";		
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");									
				if(cl_dat.M_flgLCUPD_pbst)					
				{					
					for(i = 0;i <= (tblITMDT.getRowCount() - 1);i++)
					{	//if not present then insert new.																
						if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))
						{
							L_strDEPCT = tblITMDT.getValueAt(i,2).toString().trim();								
							L_strDEPVL = tblITMDT.getValueAt(i,3).toString().trim();								
							L_strINCVL = tblITMDT.getValueAt(i,4).toString().trim();
							M_strSQLQRY = "Insert into MM_TKCTR(TKC_TNKNO,TKC_DEPCT,TKC_DEPVL,";
							M_strSQLQRY += "TKC_INCVL,TKC_TRNFL,TKC_STSFL,TKC_LUSBY,TKC_LUPDT) values (";
							M_strSQLQRY += "'" + strTNKNO + "',";
							M_strSQLQRY += L_strDEPCT + ",";
							M_strSQLQRY += L_strDEPVL + ",";
							M_strSQLQRY += L_strINCVL + ",";
							M_strSQLQRY += getUSGDTL("TK",'I',"")+")";			
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");									
						}
					}
				}
			}							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "Update MM_TKMST set ";
				M_strSQLQRY += "TK_TMPVL = " + strTMPVL + ",";
				M_strSQLQRY += "TK_MATCD = '" + strMATCD + "',";
				M_strSQLQRY += "TK_STKQT = " + strSTKQT + ",";
				M_strSQLQRY += "TK_TNKTP = '" + strTNKTP + "',";
				M_strSQLQRY += "TK_DEPCT = " + strDEPCT + ",";
				M_strSQLQRY += "TK_DEPVL = " + strDEPVL + ",";
				M_strSQLQRY += "TK_DEPQT = " + strDEPQT + ",";
				M_strSQLQRY += "TK_MNDVL = " + strMNDVL + ",";
				M_strSQLQRY += "TK_MXDVL = " + strMXDVL + ",";
				M_strSQLQRY += "TK_CLBDT = '" + strCLBDT + "',";
				M_strSQLQRY += getUSGDTL("TK",'U',"");
				M_strSQLQRY += " where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' andTK_TNKNO = '" + strTNKNO + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)					
				{					
					for(i = 0;i <= (tblITMDT.getRowCount() - 1);i++)
					{  
						if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))
						{
							strTNKNO = txtTNKNO.getText().trim();								
							L_strDEPCT = tblITMDT.getValueAt(i,2).toString().trim();								
							L_strDEPVL = tblITMDT.getValueAt(i,3).toString().trim();								
							L_strINCVL = tblITMDT.getValueAt(i,4).toString().trim();																
							if (i<intSRLNO)
							{  //if present then modify.
								M_strSQLQRY = "Update MM_TKCTR set ";			
								M_strSQLQRY += "TKC_DEPVL = " + L_strDEPVL + ",";										
								M_strSQLQRY += "TKC_INCVL = " + L_strINCVL + ",";										
								M_strSQLQRY += getUSGDTL("TKC",'U',"");
								M_strSQLQRY += " where TKC_TNKNO = '" + strTNKNO + "'";
								M_strSQLQRY += " and TKC_DEPCT = " + L_strDEPCT;			
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");										
							}																
							else
							{	
								if(hstDELIT.containsKey((String)L_strDEPCT))
							    {
									M_strSQLQRY = "Update MM_TKCTR set ";			
									M_strSQLQRY += "TKC_DEPVL = " + L_strDEPVL + ",";										
									M_strSQLQRY += "TKC_INCVL = " + L_strINCVL + ",";										
									M_strSQLQRY += getUSGDTL("TKC",'U',"");
									M_strSQLQRY += " where TKC_TNKNO = '" + strTNKNO + "'";
									M_strSQLQRY += " and TKC_DEPCT = " + L_strDEPCT;			
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
								}
								else
								{
									//if not present then insert new.																
									M_strSQLQRY = "Insert into MM_TKCTR(TKC_TNKNO,TKC_DEPCT,TKC_DEPVL,";
									M_strSQLQRY += "TKC_INCVL,TKC_TRNFL,TKC_STSFL,TKC_LUSBY,TKC_LUPDT) values (";
									M_strSQLQRY += "'" + strTNKNO + "',";
									M_strSQLQRY += L_strDEPCT + ",";
									M_strSQLQRY += L_strDEPVL + ",";
									M_strSQLQRY += L_strINCVL + ",";
									M_strSQLQRY += getUSGDTL("TK",'I',"")+")";	
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");									
								}
								
							}
						}
					}
				}			
			}								
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{									
				intRECCT = 0;
				for(i = 0; i<=(intSRLNO - 1); i++)
				{  
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))
					{
						M_strSQLQRY = "Update MM_TKCTR set ";				
						M_strSQLQRY += getUSGDTL("TKC",'U',"X");
						M_strSQLQRY += " where TKC_TNKNO = '" + txtTNKNO.getText().trim() + "'";
						M_strSQLQRY += " and TKC_DEPCT = " + tblITMDT.getValueAt(i,2).toString().trim();										
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						intRECCT++;						
					}
				}		
				if(cl_dat.M_flgLCUPD_pbst)	
				{
					// If all detail records are deleted, delete the master record					
					if(intRECCT == intSRLNO)
					{
						M_strSQLQRY = "Update MM_TKMST set ";
						M_strSQLQRY += getUSGDTL("TK",'U',"X");
						M_strSQLQRY += " where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TK_TNKNO = '" +  strTNKNO + "'";						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}					
				}
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);				
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				clrCOMP();	
				if(hstDELIT !=null)
					hstDELIT.clear();
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))				
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');				
				setENBL(false);
			}
			else
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");			
		}
	}	
	/**
	 * Method to perform validity check of the Data entered, Before inserting 
	 *new data in the data base.
	 */
	boolean vldDATA()
	{	
		if((txtTNKNO.getText().trim()).length() == 0)			// Tank No
		{
			setMSG("Enter Tank No..",'E');
			txtTNKNO.requestFocus();			
			return false;
		}		
		if((txtTMPVL.getText().trim()).length() == 0)	// Tank Temp	
		{
			setMSG("Enter Tank Temprature..",'E');
			txtTMPVL.requestFocus();			
			return false;
		}		
		if((txtMATCD.getText().trim()).length() == 0)		// Item Code
		{
			setMSG("Enter Item Code..",'E');
			txtMATCD.requestFocus();		
			return false;
		}		
		if((txtDEPCT.getText().trim()).length() == 0)		// Tank Depth
		{	
			setMSG("Enter Tank Depth..",'E');
			txtDEPCT.requestFocus();	
			return false;
		}		
		if((txtDEPVL.getText().trim()).length() == 0)		// Tank Volume
		{
			txtDEPVL.requestFocus();
			setMSG("Enter Tank Volume..",'E');
			return false;
		}		
		if((txtDEPQT.getText().trim()).length() == 0)	// Tank Quantity
		{
			txtDEPQT.requestFocus();
			setMSG("Enter Tank Quantity..",'E');
			return false;
		}		
		if((txtMNDVL.getText().trim()).length() == 0)	// Min Dip
		{
			txtMNDVL.requestFocus();
			setMSG("Enter Minimum Dip..",'E');
			return false;
		}		
		if((txtMXDVL.getText().trim()).length() == 0)	// Max Dip
		{
			txtMXDVL.requestFocus();
			setMSG("Enter Maximum Dip..",'E');
			return false;
		}		
		if((txtCLBDT.getText().trim()).length() == 0)		// Lcalib Date
		{
			txtCLBDT.requestFocus();
			setMSG("Enter Calibration Date..",'E');
			return false;
		}
		try
		{
			if (M_fmtLCDAT.parse(txtCLBDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			 
			{					
				txtCLBDT.requestFocus();
				setMSG("Calibration Date Must be smaller than Todays Date..",'E');					
				return false;
			}
			else 			
				tblITMDT.requestFocus();
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"Date Comparison in vldDATA");
		}
		if(tblITMDT.isEditing())
			tblITMDT.getCellEditor().stopCellEditing();
		tblITMDT.setRowSelectionInterval(0,0);
		tblITMDT.setColumnSelectionInterval(0,0);			
		//if no row selected.
		//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{				
			boolean L_flgCHKFL= false;
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{				
				if (tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true")) 
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
		//if tryed to delete blank Row
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{			
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{								
				if (tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{			
					if(i>= intSRLNO)
					{
						setMSG("Blank Row cannot be Deleted..",'E');
						return false;
					}
				}
			}
		}		
		//To Check the data entered in the table Row for not to be Blank.//TBL_DEPTH, TBL_VDPTH, TBL_INCLV 
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{						
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{	
				if (tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if((tblITMDT.getValueAt(i,TBL_DEPTH).toString()).length() == 0)
					{
						setMSG("Depth can not be Blank..",'E');					
						return false;					
					}					
					else if ((tblITMDT.getValueAt(i,TBL_VDPTH).toString()).length() == 0)
					{
						setMSG("Volume Depth can not be Blank..",'E');						
						return false;					
					}				
					else if ((tblITMDT.getValueAt(i,TBL_INCLV).toString()).length() == 0)
					{
						setMSG("Incremental Volume can not be Blank..",'E');					
						return false;					
					}
				}	
			}
			/*if(totVOLDPT() > Float.valueOf(txtDEPVL.getText()).floatValue())
			{
				setMSG("Total Volume depth can not be greater than total volume..",'E');
				return false;
			}*/
				
		}
		
		return true;
	}
	/**
	 * Method To execute the F1 Help.
	 */
	void exeHLPOK()
	{
		String L_strTNKNO;
		try
		{
			super.exeHLPOK();							
			if(strHLPFLD.equals("txtTNKNO"))
			{
				txtTNKNO.setText(cl_dat.M_strHLPSTR_pbst);
				L_strTNKNO = txtTNKNO.getText();
				//for F1 help complete Information is only displayed for MOD,ENQ & DEL.
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					L_strTNKNO = txtTNKNO.getText().trim();
					clrCOMP();
					txtTNKNO.setText(L_strTNKNO);
					exeGETREC(L_strTNKNO);										
				}
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
				{					
					cmbTNKTP.setEnabled(false);
					txtTNKNO.setEnabled(false);
					txtTMPVL.requestFocus();
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
				{
					txtTNKNO.setEnabled(false);
				}
			}
			else if(strHLPFLD.equals("txtMATCD"))
			{
				txtMATDS.setText("");
				txtMATDS.setText(cl_dat.M_strHLPSTR_pbst);
				txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtSTKQT.requestFocus();
				setMSG("Enter Tank Temprature..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}					
	/**
	 * Method to fetch the respective records from the table co_ptmst
	 * @param P_strTNKNO String parameter for Tank Number.
	 */
		
	private void exeGETREC(String P_strTNKNO) throws SQLException
	{
		try 
		{	
			setCursor(cl_dat.M_curWTSTS_pbst);
			intSRLNO = 0;
			M_strSQLQRY = "Select TK_TMPVL,TK_MATCD,TK_MATDS,TK_STKQT,TK_TNKTP,TK_DEPCT,TK_DEPVL,";
			M_strSQLQRY += "TK_DEPQT,TK_MNDVL,TK_MXDVL,TK_CLBDT from MM_TKMST ";
			M_strSQLQRY += " where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TK_TNKNO='" + P_strTNKNO + "' and isnull(TK_STSFL,'') <> 'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);						
			if (M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					strTMPVL = M_rstRSSET.getString("TK_TMPVL");
					strMATCD = M_rstRSSET.getString("TK_MATCD");
					strSTKQT = M_rstRSSET.getString("TK_STKQT");					
					strTNKTP = M_rstRSSET.getString("TK_TNKTP");
					strDEPCT = M_rstRSSET.getString("TK_DEPCT");					
					strDEPVL = M_rstRSSET.getString("TK_DEPVL");
					strDEPQT = M_rstRSSET.getString("TK_DEPQT");					
					strMNDVL = M_rstRSSET.getString("TK_MNDVL");
					strMXDVL = M_rstRSSET.getString("TK_MXDVL");					
					datCLBDT = M_rstRSSET.getDate("TK_CLBDT");										
					txtTMPVL.setText(strTMPVL);
					txtMATCD.setText(strMATCD);	
					txtMATDS.setText(nvlSTRVL(M_rstRSSET.getString("TK_MATDS"),""));
					M_rstRSSET.close();
					txtSTKQT.setText(strSTKQT);				
					cmbTNKTP.setSelectedIndex(Integer.parseInt(strTNKTP)- 1);				
					txtDEPCT.setText(strDEPCT);
					txtDEPVL.setText(strDEPVL);
					txtDEPQT.setText(strDEPQT);
					txtMNDVL.setText(strMNDVL);				
					txtMXDVL.setText(strMXDVL);					
					txtCLBDT.setText(M_fmtLCDAT.format(datCLBDT));							
					// Get the data from MM_TKCTR
					M_strSQLQRY = "Select TKC_DEPCT,TKC_DEPVL,TKC_INCVL,TKC_STSFL";
					M_strSQLQRY += " from MM_TKCTR where TKC_TNKNO ='" + P_strTNKNO + "'";
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						M_strSQLQRY += " and isnull(TKC_STSFL,'') <> 'X'";		
	
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
					i = 0;
					if (M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
						{
							if(nvlSTRVL(M_rstRSSET.getString("TKC_STSFL"),"").equals("X"))
							{
								hstDELIT.put(M_rstRSSET.getString("TKC_DEPCT"),"");
							}
							else
							{
								intSRLNO = intSRLNO + 1;
								tblITMDT.setValueAt(String.valueOf(intSRLNO).toString(),i,1);
								tblITMDT.setValueAt(M_rstRSSET.getString("TKC_DEPCT"),i,2);
								tblITMDT.setValueAt(M_rstRSSET.getString("TKC_DEPVL"),i,3);
								tblITMDT.setValueAt(M_rstRSSET.getString("TKC_INCVL"),i,4);							
								i++;
							}
						}
						M_rstRSSET.close();
					}
					intRECCT = i;									
					if (M_rstRSSET != null)
						M_rstRSSET.close();
				}
				else
					setMSG("Could not find matching record",'E');									
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeGETREC");
		}
	}		
	/**
	 *Method to validate the Depth, for uniqueness, and Must be in the range of Max & Min Depth.
	 */	
	private boolean vldDEPTH()
	{
		String L_strDEPTH = tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_DEPTH).toString().trim();
		float L_fltMINDIP, L_fltMAXDIP, L_fltDEPTH = Float.parseFloat(L_strDEPTH);				
		int intCOUNT = 0;		
		try
		{
			if(txtMNDVL.getText().trim().length() == 0)
			{
				setMSG("Please Enter Minimum Dip before table entry..",'E');
				return false;
			}	
			if (txtMXDVL.getText().trim().length() == 0)
			{
				setMSG("Please Enter Maximum Dip before table entry..",'E');
				return false;
			}								
			if(L_strDEPTH.trim().length() != 0)
			{															
				L_fltMINDIP = Float.parseFloat(txtMNDVL.getText().trim());
				L_fltMAXDIP = Float.parseFloat(txtMXDVL.getText().trim());			
				if(L_fltDEPTH < L_fltMINDIP)
				{
					setMSG("Depth can not be less than Minimum Dip..",'E');
					return false;
				}
				else if(L_fltDEPTH  > L_fltMAXDIP)
				{
					setMSG("Depth can not be greater than Maximum Dip...",'E');
					return false;
				}
				for(int i=0;i<tblITMDT.getRowCount()- 1;i++)
				{
					if(((String)tblITMDT.getValueAt(i,TBL_DEPTH)).trim().equals(L_strDEPTH))
						intCOUNT++;
				}
				if(intCOUNT > 1)
				{	
					setMSG("Depth value must be Unique..",'E');
					return false;
				}																										
			}
			setMSG("Enter Volume Depth..",'N');
			return true;
		}
		catch(Exception L_EX)
		{			
			setMSG(L_EX, "vldDEPTH");
			return false;
		}
	}
	/**
	 * Method to Calculate Total ( sum of ) Volume Depth entered in the Depth Column.	  
	 */
	private float totVOLDPT()
	{
		float DPT_TOTAL = 0;		
		for(int i=0; i<tblITMDT.getRowCount()-1;i++)
		{
			//if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))//|| tblITMDT.getValueAt(i,TBL_NEWFLG).toString().trim().equals("N"))
			//{
				if(tblITMDT.getValueAt(i,TBL_VDPTH).toString().trim().length() != 0)
				{
					DPT_TOTAL += Float.parseFloat(tblITMDT.getValueAt(i,TBL_VDPTH).toString().trim());
				}
			//}
		}
		return DPT_TOTAL;
	}	
	/**
	 * Method to check validy of Volume Depth entered in the Volume Depth Column of the Table.
	 * The Sum of Volume Depth entered in Table Column cannot be Greater than Tank Volume.
	*/
	private boolean vldVOLDPT()
	{
		try
		{
			String L_strVDPTH = tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_VDPTH).toString().trim();			
			float L_fltVDPTH = Float.parseFloat(L_strVDPTH);
			if(L_strVDPTH.trim().length() != 0)
			{
				L_fltVDPTH = Float.parseFloat(L_strVDPTH);				
				tblITMDT.setValueAt(new Boolean(true),tblITMDT.getSelectedRow(),TBL_CHKFL);					
				if(txtDEPVL.getText().trim().length() != 0)
				{
					fltTNKVL = Float.parseFloat(txtDEPVL.getText().trim());
					/*if(totVOLDPT() > fltTNKVL)
					{
						tblITMDT.setColumnSelectionInterval(TBL_INCLV,TBL_VDPTH);
						setMSG("Total Volume Depth should not exceed Tank Volume",'E');
						return false;
					}*/
				}					
				tblITMDT.setRowSelectionInterval(tblITMDT.getSelectedRow(),tblITMDT.getSelectedRow());
				tblITMDT.setColumnSelectionInterval(TBL_VDPTH,TBL_INCLV);
				setMSG("Enter Incremental Depth",'N');
				return true;
			}
			else
			{
				tblITMDT.setRowSelectionInterval(tblITMDT.getSelectedRow(),tblITMDT.getSelectedRow());
				tblITMDT.setColumnSelectionInterval(TBL_VDPTH,TBL_INCLV);
				setMSG("Enter Incremental Depth",'N');
				return true;
			}
		}
		catch(Exception NumberFormatException)
		{
			tblITMDT.setColumnSelectionInterval(TBL_INCLV,TBL_VDPTH);
			setMSG("Volume Depth cannot be blank, Enter Volume Depth..",'E');
			return false;
		}
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if(input == txtTNKNO)
				{		
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						return true;
					if (txtTNKNO.getText().trim().length() <= 10)
					{										
						M_strSQLQRY = "select TK_TNKNO from MM_TKMST where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TK_TNKNO ='" + txtTNKNO.getText().trim()+ "'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{
								// for Add avoid Duplicate Tank no.
								setMSG( "Tank No. already exist, Duplicate is not allowed, Please Enter another..",'E');
								M_rstRSSET.close();
								return false;
							}						
						}								
					}
				}
				if(input == txtMATCD)
				{
					txtMATDS.setText("");
					if (txtMATCD.getText().trim().length() == 10)
					{										
						M_strSQLQRY = " select CT_MATCD,CT_MATDS from CO_CTMST";
						M_strSQLQRY += " where CT_MATCD like '68%' and CT_MATCD ='" + txtMATCD.getText().trim()+"'";			
						M_strSQLQRY += " AND isnull(CT_STSFL,'') NOT IN('X','9')";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{
								txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));								
								M_rstRSSET.close();
								return true;
							}
							else 
							{
								setMSG("Invalid Material Code..",'E');
								return false;				
							}
						}
						else
						{
							setMSG("Invalid Material Code..",'E');
							return false;				
						}									
					}
				}
				if(input ==txtDEPCT)		// Tank Depth
				{
					fltTNKDP = Float.parseFloat(txtDEPCT.getText().trim());
					if((txtMXDVL.getText().trim()).length()!= 0)
					{
						fltMAXDIP = Float.parseFloat(txtMXDVL.getText().trim());
						if(fltTNKDP < fltMAXDIP)
						{
							setMSG("Tank Depth can not be less than Max Dip",'E');
							txtDEPCT.requestFocus();
							return false;
						}							
					}						
					if((txtMNDVL.getText().trim()).length()!= 0)
					{
						fltMINDIP = Float.parseFloat(txtMNDVL.getText().trim());			
						if(fltTNKDP < fltMINDIP)
						{
							setMSG("Tank Depth can not be less than Min Dip",'E');
							txtDEPCT.requestFocus();
							return false;
						}
					}											
				}				
				if(input == txtDEPVL) 	// Tank Volume
				{					
					fltTNKVL = Float.parseFloat(txtDEPVL.getText().trim());			
					if((txtDEPQT.getText().trim()).length()!=0)
					{
						fltTNKQTY = Float.parseFloat(txtDEPQT.getText().trim());
						if(fltTNKVL < fltTNKQTY)
						{
							setMSG("Tank Volume can not be less than Tank Quantity",'E');
							txtDEPVL.requestFocus();
							return false;
						}
					}				
				}
				if(input ==txtMNDVL) 	// Min Dip
				{
					fltMINDIP = Float.parseFloat(txtMNDVL.getText().trim());
					if((txtDEPCT.getText().trim()).length()!= 0)
					{
						fltTNKDP = Float.parseFloat(txtDEPCT.getText().trim());
						if(fltMINDIP > fltTNKDP)
						{
							setMSG("Min Dip can not be greater than Tank Depth",'E');
							txtMNDVL.requestFocus();
							return false;
						}
					}
					if((txtMXDVL.getText().trim()).length()!= 0)
					{
						fltMAXDIP = Float.parseFloat(txtMXDVL.getText().trim());
						if(fltMINDIP > fltMAXDIP)
						{
							setMSG("Min Dip can not be greater than Max Dip",'E');
							txtMNDVL.requestFocus();
							return false;
						}
					}		
				}
				if(input == txtMXDVL)		// Max Dip
				{						
					fltMAXDIP = Float.parseFloat(txtMXDVL.getText().trim());						
					if((txtDEPCT.getText().trim()).length()!= 0)
					{
						fltTNKDP = Float.parseFloat(txtDEPCT.getText().trim());
						if(fltMAXDIP > fltTNKDP)
						{
							setMSG("Max Dip can not be greater than Tank Depth",'E');
							txtMXDVL.requestFocus();
							return false;
						}
					}
					if((txtMNDVL.getText().trim()).length()!= 0)
					{
						fltMINDIP = Float.parseFloat(txtMNDVL.getText().trim());					
						if(fltMAXDIP < fltMINDIP)
						{
							setMSG("Max Dip can not be less than Min Dip",'E');
							txtMXDVL.requestFocus();
							return false;
						}
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
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try  
			{
				if(P_intCOLID==TBL_DEPTH)
				{
					strTEMP1 = tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_DEPTH).toString();
					if(!vldDEPTH())
					{
						return false;
					}
				}
				if(P_intCOLID==TBL_VDPTH)
				{
					strTEMP1 = tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_VDPTH).toString();
					if(strTEMP1.length() == 0)
						return true;
					if(!vldVOLDPT()) // Validation for volume depth
					{
						return false;
					}						
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Table Verifier");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}	
}
			