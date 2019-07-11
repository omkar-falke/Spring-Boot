 /*
    System Name   : Marketing System
    Program Name  : Account Reference Posting Form
    Author        : Mr.V. M. Bhaurkar

    Purpose : Entry for Acc Ref no and Dt within Masters is done through this form.
 	
    List of Table used :
    -----------------------------------------------------------------------------------------------------------
    Table Name      Primary Key														Operation done
    																				Insert  Upd  Query  Del
    -----------------------------------------------------------------------------------------------------------
    MR_IVTRN		IVT_MKTTP,IVT_INVNO,IVT_PRDCD,IVT_PKGTP							  -      -     #     -
    MR_PTTRN		PT_MKTTP,PT_INVNO,PT_PRDCD,PT_PKGTP,PT_CRDTP,PT_PRTTP,PT_PRTCD    -      -     #     -    
	CO_PTMST		PT_PRTTP,PT_PRTCD												  -      -     #     -
	-----------------------------------------------------------------------------------------------------------

    List of  fields accepted/displayed on screen :
	Field Name Column Name   Table name         Type/Size		Description
	-----------------------------------------------------------------------------
	TB_DOCRF	PT_DOCRF	   MR_PTTRN			VARCHAR(8)		Credit note no
	TB_INVNO	PT_INVNO	   MR_PTTRN			VARCHAR(8)		Invoice no
	TB_INVDT	IVT_INVDT	   MR_IVTRN			TIMESTAMP		Invoice date
	TB_PRDDS	IVT_PRDDS	   MR_IVTRN			VARCHAR(10)		Grade
	TB_INVQT	PT_INVQT	   MR_PTTRN			DECIMAL(10,3)	Invoice Qty
	TB_INDNO	IVT_INDNO	   MR_IVTRN			VARCHAR(9)		Indent No
	TB_CRDRT	PT_CRDRT	   MR_PTTRN			DECIMAL(6,2)	Credit rate
	TB_CRDVL	PT_CRDVL	   MR_PTTRN			DECIMAL(12,2)	Credit amount
	TB_ACCRF	PT_ACCRF	   MR_PTTRN			VARCHAR(10)		Acc Ref No
	TB_ACCDT	PT_ACCDT	   MR_PTTRN			DATE			Acc Ref Dt
	-----------------------------------------------------------------------------
	<I>
	<B>Query : </B>
	for getting the record:
		M_strSQLQRY = "select PT_DOCRF,PT_INVNO,IVT_INVDT,IVT_PRDDS,PT_INVQT,IVT_INDNO,PT_TRNRT,PT_PGRVL,PT_ACCRF,PT_ACCDT ";";
			 +=" from MR_PTTRN,MR_IVTRN where ";
			1) PT_INVNO = IVT_INVNO "
			2) and PT_PRDCD = IVT_PRDCD and PT_PKGTP = IVT_PKGTP 
			3) and PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"' ";
			4) and PT_STSFL <> 'X' and PT_CRDTP ='"+strCRDTP+"' 
			5) and isnull(PT_ACCRF,'') = '' 
		for Bookin Discount
			6) and IVT_BYRCD = '"+strBYRCD+"' 
		for Distributor Commission
			7) and IVT_DSRCD = '"+strDSRCD+"' 

	Data is  updated in MR_CNTRN iff it is successful then transactiion data 
	is  updated in the table MR_CNTRN.
 
	for updating :
	M_strSQLQRY = "Update MR_PTTRN set";
		1) PT_ACCRF = '"+strACCRF1+"'
		2) PT_ACCDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strACCDT1))+ "'						
		3) PT_TRNFL = '0'
		4) PT_LUSBY ='" + cl_dat.M_strUSRCD_pbst+ "'
		5) PT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
		6) where PT_DOCRF = '"+strDOCRF1+"'";


<B>Validations & Other Information:</B>    

	</I>

*/

import javax.swing.*;import javax.swing.table.*;import java.awt.event.*;
import javax.swing.JComboBox;import javax.swing.JLabel;
import javax.swing.JTextField;import java.sql.*;import java.sql.Date;
import java.util.*;import java.awt.Cursor;

class mr_tearu extends cl_pbase
{
	private JComboBox cmbRPTOP;		/** JComboBox to display & select Report.*/
	
	//private JLabel lblFMDAT;       /** JLabel to display from date on the Screen.*/
	//private JLabel lblTODAT;       /**JLabel to display to date on the Screen.*/
	//private JLabel lblBUYER;		/**JLabel to display Buyer on the Screen.*/
	//private JLabel lblDISTR;		/**JLabel to display Destributor on the Screen.*/
	
	private JTextField txtFMDAT;   /** JTextField to display & to enter the From Date.*/
	private JTextField txtTODAT;   /** JTextField to display & to enter the To date.*/
	private JTextField txtBYRCD;   /** JTextField to display & to enter the Buyer code.*/
	private JTextField txtBYRNM;	/** JTextField to display & to enter the Buyer description.*/
	private JTextField txtDSRCD;	/** JTextField to display & to enter the Destributor code.*/
	private JTextField txtDSRNM;	/** JTextField to display & to enter the Destributor description.*/
	private JTextField txtACCRF;	/** JTextField to display & to enter the Acc Ref No.*/
	private JTextField txtACCDT;	/** JTextField to display & to enter the Acc Ref Dt.*/
	private JTextField txtCATGR;
	
	private String strBYRCD;		/** String variable for Buyer Code.*/	
	private String strDSRCD;		/** String variable for Destributor Code.*/	
	
	private String strDOCRF;		/** String variable for C.N.No.*/	
	private String strINVNO;		/** String variable for Inv.No.*/	
	private String strINVDT;		/** String variable for Inv.Dt.*/	
	private String strPRDDS;		/** String variable for Grade.*/	
	private String strINVQT;		/** String variable for Inv.Qt.*/	
	private String strINDNO;		/** String variable for Ind.No.*/
	private String strCRDRT;		/** String variable for Credit Rate.*/
	private String strCRDVL;		/** String variable for Credit Amount.*/
	private String strACCRF;		/** String variable for Acc.Ref.No*/
	private String strACCDT;		/** String variable for Acc.Ref.Dt*/
	private String strPRTCD;
	private String strPRTTP;
	
	private cl_JTable tblENTTB;
	private String strDOCRF1;		/** String variable for previous C.N.No.*/	
	private String strACCRF1;		/** String variable for previous Acc Ref No.*/	
	private String strACCDT1;       /** String variable for previous Acc Ref Dt.*/	
	
	private int TB_CHKFL = 0;		/** Integer variable to represent the check flag Column.*/
	private int TB_DOCRF = 1;		/** Integer variable to represent the C.N.No Column.*/
	private int TB_INVNO = 2;		/** Integer variable to represent the Inv no Column.*/
	private int TB_INVDT = 3;		/** Integer variable to represent the Inv Dt Column.*/
	private int TB_PRDDS = 4;		/** Integer variable to represent the Grade Column.*/
	private int TB_INVQT = 5;		/** Integer variable to represent the Inv qty Column.*/
	private int TB_INDNO = 6;		/** Integer variable to represent the Ind no Column.*/
	private int TB_CRDRT = 7;		/** Integer variable to represent the Credit rate Column.*/
	private int TB_CRDVL = 8;		/** Integer variable to represent the Credit Amount Column.*/
	private int TB_ACCRF = 9;		/** Integer variable to represent the Acc Ref No Column.*/
	private int TB_ACCDT = 10;		/** Integer variable to represent the Acc Ref Dt Column.*/
	private int TB_PRTTP = 11;      /**           */
	private int TB_PRTCD = 12;      /**           */
	
	private String strPDCRF = "";	/** String variable for previous C.N.No.*/	
	private String strPIVDT = "";	/** String variable for previous Inv.Dt.*/
	private String strPDCRF1 = "";	/** String variable for previous C.N.No.*/
	private String strNINNO="";
	private String strPINNO="";	
	private final String strDSTCM="03";
	private final String strBDSCM="02";
	private final String strBYRCM="01";
		
	private TableInputVerifier TBLINPVF;/** Inner class object for Table Input Data Verification.*/
	private TBLINPVF objTBLVRF;			/** Input varifier for master data validity Check.*/
	
	private String L_strCRDTP;		/** String variable for geting credit type*/
	
	mr_tearu()
	{
		super(2);
		cmbRPTOP = new JComboBox();
		try
		{
			String L_strCODCD="";
			String L_strCODDS="";
			ResultSet L_rstRSSET;
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT' and CMT_CODCD like '0%'";
			
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET!=null)
			{	 
				while(L_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODDS = nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"");
					cmbRPTOP.addItem(L_strCODCD+" "+L_strCODDS);
				}
			}
			if(L_rstRSSET != null)
				L_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"GetSAL");
		}
		setMatrix(20,8);
		add(new JLabel("Credit Note Type"),2,2,1,2,this,'L');
		add(cmbRPTOP,2,3,1,1.7,this,'L');
		
		add(new JLabel("From Date "),3,2,1,2,this,'L');
		add(txtFMDAT = new TxtDate(),3,3,1,1.8,this,'L');
		add(new JLabel("Category "),2,5,1,1,this,'L');
		add(txtCATGR = new TxtLimit(2),2,6,1,1,this,'L');
		add(new JLabel("To Date "),4,2,1,2,this,'L');
		add(txtTODAT = new TxtDate(),4,3,1,1.8,this,'L');
				
		add(new JLabel("Buyer "),5,2,1,2,this,'L');
		add(txtBYRCD = new TxtLimit(5),5,3,1,1.8,this,'L');
		
		
		add(txtBYRNM = new TxtLimit(30),5,5,1,2.5,this,'L');
		add(new JLabel("Distributor "),6,2,1,2,this,'L');
		add(txtDSRCD = new TxtLimit(5),6,3,1,1.8,this,'L');
		add(txtDSRNM = new TxtLimit(30),6,5,1,2.5,this,'L');
		
		tblENTTB  = crtTBLPNL1(this,new String[]{"Select","C.N.No","Inv No","Inv Dt","Grade","Inv Qty","Ind No","Credit Rs/MT","Amount","Acc Ref No","Acc Ref Dt","Party Type","Party Code" },200,8,1,9,8,new int[]{20,65,65,70,80,50,80,70,80,80,80,30,80},new int[]{0});
		objTBLVRF = new TBLINPVF();
		tblENTTB.setInputVerifier(objTBLVRF);	
		tblENTTB.addKeyListener(this);
		tblENTTB.setCellEditor(TB_ACCRF,txtACCRF = new TxtLimit(10));
		tblENTTB.setCellEditor(TB_ACCDT,txtACCDT = new TxtDate());
		txtACCRF.addKeyListener(this);
		txtACCDT.addKeyListener(this);
		cl_dat.M_flgHELPFL_pbst = false;
		vldINVER objINVER=new vldINVER();
		for(int i=0;i<M_vtrSCCOMP.size();i++)
		{
			if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objINVER);
		}		
		
		setENBL(false);	
	}
	
	/** Function for setting enabled or disabled 
	 * 
	*/
	void setENBL(boolean L_STAT)
	{
		try
		{
			clrCOMP();	
			super.setENBL(L_STAT);// default false
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			{
				txtBYRNM.setEnabled(false);
				txtDSRNM.setEnabled(false);
			   	tblENTTB.cmpEDITR[TB_CHKFL].setEnabled(true);
				tblENTTB.cmpEDITR[TB_DOCRF].setEnabled(false);						
				tblENTTB.cmpEDITR[TB_INVNO].setEnabled(false);	
				tblENTTB.cmpEDITR[TB_INVDT].setEnabled(false);	
				tblENTTB.cmpEDITR[TB_PRDDS].setEnabled(false);	
				tblENTTB.cmpEDITR[TB_INVQT].setEnabled(false);	
				tblENTTB.cmpEDITR[TB_INDNO].setEnabled(false);	
				tblENTTB.cmpEDITR[TB_CRDRT].setEnabled(false);	
				tblENTTB.cmpEDITR[TB_CRDVL].setEnabled(false);
				tblENTTB.cmpEDITR[TB_PRTTP].setEnabled(false);
				tblENTTB.cmpEDITR[TB_PRTCD].setEnabled(false);				
				
				tblENTTB.cmpEDITR[TB_ACCRF].setEnabled(true);
				tblENTTB.cmpEDITR[TB_ACCDT].setEnabled(true);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setENBL");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				setENBL(false);
		
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		if(M_objSOURC==cmbRPTOP)
		{
			txtBYRCD.setText("");
			txtBYRNM.setText("");
			txtDSRCD.setText("");
			txtDSRNM.setText("");
		}
			
		if((cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals(strDSTCM)||(cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals(strBDSCM))
		{
			txtDSRCD.setEnabled(true);
			txtBYRCD.setEnabled(false);
			txtCATGR.setEnabled(false);
		}
		else
		{
			txtDSRCD.setEnabled(false);
			txtBYRCD.setEnabled(true);
			if((cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals("09"))
			{
				
				txtCATGR.setEnabled(true);
				//txtCATGR.requestFocus();
			}
			else
				txtCATGR.setEnabled(false);
		}
	
		if(M_objSOURC == txtBYRCD)
		{
			if((txtBYRCD.getText().trim().length()>0)&& (txtBYRCD.getText().trim().length()<5))
			{
				setMSG("Please Enter Proper Party Code or Press f1 for help ..",'E');
				txtBYRCD.requestFocus();
				//return false;
			}
			else
			{
				getDATA();
			}
		}
		if(M_objSOURC == txtDSRCD)
		{
			if((txtBYRCD.getText().trim().length()>0)&& (txtBYRCD.getText().trim().length()<5))
			{
				setMSG("Please Enter Proper Party Code or Press f1 for help ..",'E');
				txtBYRCD.requestFocus();
				//return false;
			}
			else
			{
				getDATA();
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				cmbRPTOP.requestFocus();
			}
			if(M_objSOURC == cmbRPTOP)
			{
				if((cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals("09"))
				{
					txtCATGR.requestFocus();
					//txtBYRCD.setEnabled(true);
				}
				else
					txtFMDAT.requestFocus();
			}
			if(M_objSOURC == txtCATGR)
			{
				txtFMDAT.requestFocus();
			}
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
				{
					txtTODAT.requestFocus();
				}
				else
				{
					txtFMDAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
				{
					if((cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals(strDSTCM)||(cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals(strBDSCM))
					{
						txtDSRCD.requestFocus();
						txtBYRCD.setEnabled(false);
					}
					else
					{
						txtBYRCD.requestFocus();
						txtDSRCD.setEnabled(false);
					}
				}
				else
				{
					txtTODAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			if(M_objSOURC == txtBYRCD)
			{
				txtBYRCD.setText(txtBYRCD.getText().trim().toUpperCase());
				tblENTTB.setRowSelectionInterval(tblENTTB.getSelectedRow(),tblENTTB.getSelectedRow());		
				tblENTTB.setColumnSelectionInterval(TB_DOCRF,TB_DOCRF);		
				tblENTTB.editCellAt(tblENTTB.getSelectedRow(),TB_ACCRF);
				tblENTTB.cmpEDITR[TB_ACCRF].requestFocus();
			}
			if(M_objSOURC == txtDSRCD)
			{
				txtDSRCD.setText(txtDSRCD.getText().trim().toUpperCase());
				tblENTTB.setRowSelectionInterval(tblENTTB.getSelectedRow(),tblENTTB.getSelectedRow());		
				tblENTTB.setColumnSelectionInterval(TB_ACCRF,TB_ACCRF);		
				tblENTTB.editCellAt(tblENTTB.getSelectedRow(),TB_ACCRF);
				tblENTTB.cmpEDITR[TB_ACCRF].requestFocus();
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				if(M_objSOURC == txtBYRCD)
				{
					M_strHLPFLD = "txtBYRCD";
					M_strSQLQRY = "select distinct A.PT_PRTCD,A.PT_PRTNM from MR_PTTRN B ,CO_PTMST A ,MR_PLTRN where ";
					M_strSQLQRY +=" B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD ";
					M_strSQLQRY +=" and B.PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.PT_PRTTP = 'C' ";
					if(!(cmbRPTOP.getSelectedItem().toString()).substring(0,2).equals("09"))
					{
						M_strSQLQRY += " and PT_CRDTP='"+(cmbRPTOP.getSelectedItem().toString()).substring(0,2)+"'";
					}
					else
					{
						M_strSQLQRY += " and PT_CRDTP='"+txtCATGR.getText().trim()+"' ";
					}
					M_strSQLQRY += " AND isnull(PT_ACCRF,'') = '' ";
					M_strSQLQRY += " and PT_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
					M_strSQLQRY += " AND PL_DOCNO = B.PT_DOCRF ";
					if(txtBYRCD.getText().trim().length()>0)
					{
						M_strSQLQRY += " AND  B.PT_PRTCD like '"+txtBYRCD.getText().trim().toUpperCase()+"%'";
					}
					M_strSQLQRY += "Order by A.PT_PRTNM ";
					//System.out.println(" BUYER = "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,2,1,new String[] {"Party Code","Party Name"},2,"CT");
				}
				if(M_objSOURC == txtDSRCD)
				{
					M_strHLPFLD = "txtDSRCD";
					M_strSQLQRY =  "select distinct A.PT_PRTCD,A.PT_PRTNM from MR_PTTRN B ,CO_PTMST A,MR_PLTRN  where ";
					M_strSQLQRY += " B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD ";
					M_strSQLQRY += " and A.PT_PRTTP = 'D' ";
					M_strSQLQRY += " AND isnull(PT_ACCRF,'') = '' ";
					M_strSQLQRY += " and PT_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
					M_strSQLQRY += " AND PL_DOCNO = B.PT_DOCRF AND PL_CMPCD=B.PT_CMPCD AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
					if(txtDSRCD.getText().trim().length()>0)
					{
						M_strSQLQRY += " AND  B.PT_PRTCD like '"+txtDSRCD.getText().trim().toUpperCase()+"%'";
					}
					M_strSQLQRY += "Order by A.PT_PRTNM ";
					//System.out.println(" Distributer  = "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,2,1,new String[] {"Dist Code","Dist Name"},2,"CT");
				}
				if(M_objSOURC == txtCATGR)
				{
					M_strHLPFLD = "txtCATGR";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXOCN' And CMT_CODCD like '0%'";					
    				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
    			
				}
				setMSG("",'N');
			}
			catch(Exception E_KE)
			{
				setMSG(E_KE,"Key Pressed");
			}
		}
	}
	
	/**
	 *  Function for getting value from F1 after clicking OK 
	*/
	public void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtBYRCD"))
			{
				txtBYRCD.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());
				txtBYRNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());		
			}
			if(M_strHLPFLD.equals("txtDSRCD"))
			{
				txtDSRCD.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());
				txtDSRNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
			}							
			if(M_strHLPFLD.equals("txtCATGR"))
			{
				txtCATGR.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	
	/**User friendly messagees	 */
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		String L_strTEMP="";
		try
		{
			if(L_FE.getSource().equals(txtFMDAT))
			{
                setMSG("Enter Date in format dd/mm/yyyy",'N');
			}
			if(L_FE.getSource().equals(txtTODAT))
			{
                setMSG("Enter Date in format dd/mm/yyyy",'N');
			}
			if(L_FE.getSource().equals(txtBYRCD))
			{
				 setMSG("Press F1 to select the Buyer code",'N');
			}
			if(L_FE.getSource().equals(txtDSRCD))
			{
				 setMSG("Press F1 to select the Destributor code",'N');
			}
			if(L_FE.getSource().equals(tblENTTB))
			{
				if(tblENTTB.getSelectedColumn() == TB_DOCRF)
				{
					setMSG("Credit note no..",'N');
				}
				if(tblENTTB.getSelectedColumn() == TB_INVNO)
				{
					setMSG("Invoice no..",'N');
				}
				if(tblENTTB.getSelectedColumn() == TB_PRDDS)
				{
					setMSG("Grade..",'N');
				}
				if(tblENTTB.getSelectedColumn() == TB_INVQT)
				{
					setMSG("Invoice qty..",'N');
				}
				if(tblENTTB.getSelectedColumn() == TB_INDNO)
				{
					setMSG("Indent no..",'N');
				}
				if(tblENTTB.getSelectedColumn() == TB_CRDRT)
				{
					setMSG("Credit Rs/MT..",'N');
				}
				if(tblENTTB.getSelectedColumn() == TB_CRDVL)
				{
					setMSG("Credit Amount..",'N');
				}
		/*		if(tblENTTB.getSelectedColumn() == TB_ACCRF)
				{
					setMSG("Enter Acc Ref No..",'N');
					L_strTEMP = tblENTTB.getValueAt(tblENTTB.getSelectedRow(),TB_DOCRF).toString().trim();
					if(L_strTEMP.length()==0)
						((JTextField)tblENTTB.cmpEDITR[TB_ACCRF]).setEditable(false);
					else
						((JTextField)tblENTTB.cmpEDITR[TB_ACCRF]).setEditable(true);
				}
				if(tblENTTB.getSelectedColumn() == TB_ACCDT)
				{
					L_strTEMP = tblENTTB.getValueAt(tblENTTB.getSelectedRow(),TB_DOCRF).toString().trim();
					if(L_strTEMP.length()==0)
						((JTextField)tblENTTB.cmpEDITR[TB_ACCDT]).setEditable(false);
					else
						((JTextField)tblENTTB.cmpEDITR[TB_ACCDT]).setEditable(true);
					setMSG("Enter Acc Ref DT in DD/MM/YYYY ",'N');
				}
				*/
			}
		}
		catch(Exception e)
		{
			setMSG(e,"TEIND.FocusGained"+M_objSOURC);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
		
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
			String L_strTEMP="";
			if(txtFMDAT.getText().trim().length() != 10)
			{
				setMSG("Enter the proper Date..",'E');
				return false;
			}
			if(txtTODAT.getText().trim().length() != 10)
			{
				setMSG("Enter the proper Date..",'E');
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("To Date can not be Less than From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}	
			for(int i=0;i<tblENTTB.getRowCount();i++)
    		{
				if(tblENTTB.getValueAt(i,TB_CHKFL).toString().equals("true"))
    			{
					L_strTEMP = nvlSTRVL(tblENTTB.getValueAt(i,TB_ACCDT).toString(),"");
    				if(L_strTEMP.length() == 0)
    				{
    					setMSG("Reference Date Can not be Blank..",'E');
						return false;
    				}
					L_strTEMP = nvlSTRVL(tblENTTB.getValueAt(i,TB_ACCRF).toString(),"");
    				if(L_strTEMP.length()==0)
    				{
						setMSG("Reference Number Can not be Blank..",'E');
						return false;
    				}
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	/**
	 * Method to fetch the data from the table 
	*/
	private void getDATA()
	{
		try
		{
			java.sql.Date L_datTMPDT=null;
			if(tblENTTB.isEditing())
				tblENTTB.getCellEditor().stopCellEditing();
			tblENTTB.setRowSelectionInterval(0,0);
			tblENTTB.setColumnSelectionInterval(0,0);
			tblENTTB.clrTABLE();
			String L_strFMDAT = txtFMDAT.getText().trim();
			String L_strTODAT = txtTODAT.getText().trim();
			L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
			String strCRDTP = L_strCRDTP.substring(0,2);
			if(strCRDTP.equals("09"))
				strCRDTP=txtCATGR.getText().trim();				
			strBYRCD = txtBYRCD.getText().toString().trim().toUpperCase();
			strDSRCD = txtDSRCD.getText().toString().trim().toUpperCase();
			M_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_DOCRF,PT_INVNO,IVT_INVDT,IVT_PRDDS,PT_INVQT,IVT_INDNO,PT_TRNRT,PT_PGRVL,PT_ACCRF,PT_ACCDT ";
	 		M_strSQLQRY +=" from MR_PTTRN,MR_IVTRN where ";
			M_strSQLQRY +=" PT_INVNO = IVT_INVNO";
			M_strSQLQRY +=" and PT_PRDCD = IVT_PRDCD and PT_PKGTP = IVT_PKGTP and PT_CMPCD=IVT_CMPCD";
			M_strSQLQRY +=" and PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"' ";
			M_strSQLQRY +=" and PT_STSFL <> 'X' and PT_CRDTP ='"+strCRDTP+"' ";
			if(strCRDTP.equals("0Z"))
			{
				M_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_DOCRF,PT_INVNO,IVT_INVDT,IVT_PRDDS,PT_INVQT,IVT_INDNO,PT_TRNRT,PT_PGRVL,PT_ACCRF,PT_ACCDT ";
	 			M_strSQLQRY +=" from MR_PTTRN left outer join MR_IVTRN on PT_INVNO = IVT_INVNO and PT_PRDCD = IVT_PRDCD and PT_PKGTP = IVT_PKGTP and PT_CMPCD = IVT_CMPCD where ";
				M_strSQLQRY +="  PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"' ";
				M_strSQLQRY +=" and PT_STSFL <> 'X' and PT_CRDTP ='"+strCRDTP+"' ";
				
			}
			else
			{
				M_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_DOCRF,PT_INVNO,IVT_INVDT,IVT_PRDDS,PT_INVQT,IVT_INDNO,PT_TRNRT,PT_PGRVL,PT_ACCRF,PT_ACCDT ";
	 			M_strSQLQRY +=" from MR_PTTRN,MR_IVTRN where ";
				M_strSQLQRY +=" PT_INVNO = IVT_INVNO ";
				M_strSQLQRY +=" and PT_PRDCD = IVT_PRDCD and PT_PKGTP = IVT_PKGTP and PT_CMPCD = IVT_CMPCD";
				M_strSQLQRY +=" and PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"' ";
				M_strSQLQRY +=" and PT_STSFL <> 'X' and PT_CRDTP ='"+strCRDTP+"' ";
			}
			if(strCRDTP.equals(strBYRCM))
			{
				if(txtBYRCD.getText().trim().length()==5)
				{
					M_strSQLQRY +=" and IVT_BYRCD = '"+strBYRCD+"' ";
				}
			}
			
			else if(strCRDTP.equals(strDSTCM)||strCRDTP.equals(strBDSCM))
			{
				if(txtDSRCD.getText().trim().length()==5)
				{
					M_strSQLQRY +=" and IVT_DSRCD = '"+strDSRCD+"' ";
				}
				M_strSQLQRY += " AND PT_DOCRF <>'00000000' ";
			}
			
			else if(txtBYRCD.getText().trim().length()>0)
			{
				if(strCRDTP.equals("0Z"))
				{
					M_strSQLQRY +=" and PT_PRTCD = '"+strBYRCD+"' ";
				}
				else
					M_strSQLQRY +=" and IVT_BYRCD = '"+strBYRCD+"' ";
			}
			M_strSQLQRY +=" and isnull(PT_ACCRF,'') = '' ";
			M_strSQLQRY +=" order by  PT_DOCRF ";
			//System.out.println("Getdata = " +M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int i = 0;
			strPDCRF = "";
			strPIVDT = "";
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strDOCRF = nvlSTRVL(M_rstRSSET.getString("PT_DOCRF"),"");
					strINVNO = nvlSTRVL(M_rstRSSET.getString("PT_INVNO"),"");
					L_datTMPDT =M_rstRSSET.getDate("IVT_INVDT");
					if(L_datTMPDT !=null)
					{
						strINVDT=M_fmtLCDAT.format(L_datTMPDT).toString();
					}
					else
						strINVDT="";
						
					//strINVDT = nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IVT_INVDT")),"");
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"");
					strINVQT = nvlSTRVL(M_rstRSSET.getString("PT_INVQT"),"");
					strINDNO = nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),"");
					strCRDRT = nvlSTRVL(M_rstRSSET.getString("PT_TRNRT"),"");
					strCRDVL = nvlSTRVL(M_rstRSSET.getString("PT_PGRVL"),"");
					strACCRF = nvlSTRVL(M_rstRSSET.getString("PT_ACCRF"),"");
					
					strPRTTP = nvlSTRVL(M_rstRSSET.getString("PT_PRTTP"),"");
					strPRTCD = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
															
					if(!strPDCRF.equals(strDOCRF) || !strPIVDT.equals(strINVDT)||!strPINNO.equals(strNINNO))
					{
						tblENTTB.setValueAt(strDOCRF.trim(),i,TB_DOCRF);
						tblENTTB.setValueAt(strINVNO.trim(),i,TB_INVNO);
						tblENTTB.setValueAt(strINVDT.trim(),i,TB_INVDT);
						tblENTTB.setValueAt(strPRDDS.trim(),i,TB_PRDDS);
						tblENTTB.setValueAt(strINVQT.trim(),i,TB_INVQT);
						tblENTTB.setValueAt(strINDNO.trim(),i,TB_INDNO);
						tblENTTB.setValueAt(strCRDRT.trim(),i,TB_CRDRT);
						tblENTTB.setValueAt(strCRDVL.trim(),i,TB_CRDVL);
						tblENTTB.setValueAt(strACCRF.trim(),i,TB_ACCRF);
						tblENTTB.setValueAt(strPRTTP.trim(),i,TB_PRTTP);
						tblENTTB.setValueAt(strPRTCD.trim(),i,TB_PRTCD);
						
					}
					else
					{
						//tblENTTB.setValueAt(strINVNO.trim(),i,TB_INVNO);
						tblENTTB.setValueAt(strPRDDS.trim(),i,TB_PRDDS);
						tblENTTB.setValueAt(strINVQT.trim(),i,TB_INVQT);
						tblENTTB.setValueAt(strINDNO.trim(),i,TB_INDNO);
						tblENTTB.setValueAt(strCRDRT.trim(),i,TB_CRDRT);
						tblENTTB.setValueAt(strCRDVL.trim(),i,TB_CRDVL);
						tblENTTB.setValueAt(strPRTTP.trim(),i,TB_PRTTP);
						tblENTTB.setValueAt(strPRTCD.trim(),i,TB_PRTCD);

					}
					strPDCRF = strDOCRF;
					strPIVDT = strINVDT;
					i++;					
				}
			}
			if(i==0)
				setMSG("No Record Found ",'E');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	
	/**
	 * super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	*/
	void exeSAVE()
	{
		try
		{	
			strPDCRF1 = "";
			int L_intSELROW=0;
			String L_strTODAT="";
			String L_strFMDAt="";
			cl_dat.M_flgLCUPD_pbst = true;
			
			if(tblENTTB.isEditing())
				tblENTTB.getCellEditor().stopCellEditing();
			tblENTTB.setRowSelectionInterval(0,0);
			tblENTTB.setColumnSelectionInterval(0,0);
			
			if(!vldDATA())
				return;
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				for(int i=0;i<tblENTTB.getRowCount();i++)
				{
					String L_strCRDTP = (cmbRPTOP.getSelectedItem().toString()).substring(0,2);
					if(L_strCRDTP.equals("09"))
						L_strCRDTP=txtCATGR.getText().trim();		
					if(tblENTTB.getValueAt(i,TB_CHKFL).toString().trim().equals("true"))
					{
						L_intSELROW++;
						strDOCRF1 = tblENTTB.getValueAt(i,TB_DOCRF).toString().trim();
					
						strACCRF1 = tblENTTB.getValueAt(i,TB_ACCRF).toString().trim();
						strACCDT1 = tblENTTB.getValueAt(i,TB_ACCDT).toString().trim();
						strPRTTP  = tblENTTB.getValueAt(i,TB_PRTTP).toString().trim();
						strPRTCD  = tblENTTB.getValueAt(i,TB_PRTCD).toString().trim();
						if(strDOCRF1.length()>0)
						{
							M_strSQLQRY = "Update MR_PTTRN set";
							M_strSQLQRY += " PT_ACCRF = '"+strACCRF1+"',";
							M_strSQLQRY += " PT_ACCDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strACCDT1))+ "',";						
							M_strSQLQRY += " PT_TRNFL = '0',";
							M_strSQLQRY += " PT_LUSBY ='" + cl_dat.M_strUSRCD_pbst+ "',";
							M_strSQLQRY += " PT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
							M_strSQLQRY += " where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_DOCRF = '"+strDOCRF1+"'";
							//System.out.println("PTTRN  = "+M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
				
						if(strDOCRF1.length()>0)
						{
							M_strSQLQRY = "Update MR_PLTRN set";
							M_strSQLQRY += " PL_ACCRF = '"+strACCRF1+"',";
							M_strSQLQRY += " PL_ACCDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strACCDT1))+ "',";						
							M_strSQLQRY += " PL_TRNFL = '0',";
							M_strSQLQRY += " PL_LUSBY ='" + cl_dat.M_strUSRCD_pbst+ "',";
							M_strSQLQRY += " PL_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
							M_strSQLQRY += " where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_DOCNO = '"+strDOCRF1+"'";
							M_strSQLQRY += " AND PL_PRTTP = '"+strPRTTP+"'";
							M_strSQLQRY += " AND PL_PRTCD = '"+strPRTCD+"'";
							M_strSQLQRY += " AND  PL_DOCTP ='"+ L_strCRDTP+"'";
							
							//System.out.println("PLTRN  = "+M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
				}
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					L_strTODAT=txtTODAT.getText().trim();
					L_strFMDAt=txtFMDAT.getText().trim();
					clrCOMP();	
					txtTODAT.setText(L_strTODAT);
					txtFMDAT.setText(L_strFMDAt);
					setMSG("Saved Successfully ",'N');
				}
				else
					setMSG("Error In Saving",'E');	
				
				if(L_intSELROW ==0)
				{
					setMSG("No rows selcted",'E');
					return;
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
			cl_dat.M_flgLCUPD_pbst=false;
		}
	}
	
	private class vldINVER extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtFMDAT)
				{
					if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Date can not be greater than today's date..",'E');
						return false;
					}
				}
				if(input==txtTODAT)
				{
					if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
					{
						setMSG("To Date can not be Less than From Date ..",'E');
						
						return false;
					}
					if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Date can not be greater than today's date..",'E');
						
						return false;
					}
				}
				
				if(input==txtBYRCD)
				{
					if(txtBYRCD.getText().trim().length()!=5)
					{
						txtBYRNM.setText("");
						return true;
					}
						
					else
					{
						M_strSQLQRY = "select distinct A.PT_PRTCD,A.PT_PRTNM  PT_PRTNM from MR_PTTRN B ,CO_PTMST A, MR_PLTRN where ";
						M_strSQLQRY +=" B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD ";
						M_strSQLQRY +=" and A.PT_PRTTP = 'C' ";
						if(!(cmbRPTOP.getSelectedItem().toString()).substring(0,2).equals("09"))
						{
							M_strSQLQRY += " and PT_CRDTP='"+(cmbRPTOP.getSelectedItem().toString()).substring(0,2)+"'";
						}
						else
						{
							M_strSQLQRY += " and PT_CRDTP='"+txtCATGR.getText().trim()+"' ";
						}
						M_strSQLQRY += " AND B.PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(B.PT_ACCRF,'') = '' ";
						M_strSQLQRY += " and PT_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
						M_strSQLQRY += " AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_DOCNO = B.PT_DOCRF AND PL_CMPCD=B.PT_CMPCD";
						M_strSQLQRY += " AND  B.PT_PRTCD ='"+txtBYRCD.getText().trim().toUpperCase()+"'";
						M_strSQLQRY += "Order by A.PT_PRTNM ";
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET!=null &&M_rstRSSET.next())
						{
							txtBYRNM.setText(M_rstRSSET.getString("PT_PRTNM"));
							M_rstRSSET.close();
								return true;
						}	
						else
						{
							txtBYRNM.setText("");
							setMSG("Invalid Party Type",'E'); 
							return false;
						}
					}
					
					
				}
				if(input==txtDSRCD)
				{
					M_strSQLQRY = "select distinct A.PT_PRTCD,A.PT_PRTNM PT_PRTNM  from MR_PTTRN B ,CO_PTMST A , MR_PLTRN where ";
					M_strSQLQRY +=" B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD ";
					M_strSQLQRY +=" and A.PT_PRTTP = 'D' ";
					M_strSQLQRY += " AND B.PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(B.PT_ACCRF,'') = '' ";
					M_strSQLQRY += " and PT_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
					M_strSQLQRY += " AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_DOCNO = B.PT_DOCRF AND PL_CMPCD=B.PT_CMPCD";
					M_strSQLQRY += " AND  B.PT_PRTCD ='"+txtDSRCD.getText().trim().toUpperCase()+"'";					
					M_strSQLQRY += "Order by A.PT_PRTNM ";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null &&M_rstRSSET.next())
					{
						txtDSRNM.setText(M_rstRSSET.getString("PT_PRTNM"));
						M_rstRSSET.close();
							return true;
					}	
					else
					{
						txtDSRNM.setText("");
						setMSG("Invalid Distributer Type",'E'); 
						return false;
					}
					
				}
			}
		
			catch(Exception E_VR)
			{
				setMSG(E_VR,"Input Verifier");		
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
				if(getSource()==tblENTTB)
				{
					if(P_intCOLID>0)
						if(((JTextField)tblENTTB.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
							return true;
					String L_strACCRF = "";
					String L_strTEMP1="";
					if(P_intCOLID==TB_ACCRF)
					{
						L_strTEMP1 = tblENTTB.getValueAt(tblENTTB.getSelectedRow(),TB_ACCRF).toString().trim();
					
						if(L_strTEMP1.length()>0)
						{
							
							
							tblENTTB.setValueAt(String.valueOf(cl_dat.M_txtCLKDT_pbst.getText()),tblENTTB.getSelectedRow(),TB_ACCDT);
							for(int i=0; i<tblENTTB.getSelectedRow(); i++)
							{							
								L_strACCRF = (tblENTTB.getValueAt(i,TB_ACCRF).toString());
								if(L_strACCRF.equals(L_strTEMP1))
								{
									setMSG("Duplicate values of code are not allowed..",'E');					
									tblENTTB.setValueAt("",tblENTTB.getSelectedRow(),TB_ACCDT);
									return false;													
								}
							}
						}
					}	
					if(P_intCOLID==TB_ACCDT)
					{					
						if(M_fmtLCDAT.parse(txtACCDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
						{
							setMSG("Date can not be greater than today's date..",'E');
							return false;
						}
					}
				}
				return true;
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"TableInputVerify");
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
		}
	}
}