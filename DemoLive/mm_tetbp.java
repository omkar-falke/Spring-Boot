/*
System Name   : Material Management System
Program Name  : Transporters Bill Passing.
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
import java.sql.*;
import java.awt.event.*;
import javax.swing.InputVerifier;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import javax.swing.JComboBox;import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JButton;import javax.swing.JOptionPane;import javax.swing.JComponent;

/**<pre>
System Name : Material Management System.
 
Program Name : Bill Clearence Entry Program.

Purpose : This program is used to enter the bill clearance details for given Transporter & 
Location code from which the goods are loaded for SPL Works.

List of tables used :
Table Name Primary key                                   Operation done
                                                      Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN   CMT_CGMTP,CMT_CGSTP,CMT_CODCD                          #       #   
MM_BLMST   BL_BILTP, BL_DOCNO                            #                #       #
MM_BILTR   BIL_BILTP,BIL_DOCNO,BIL_DOCRF,BIL_MATCD       #                #       #
MM_GRMST   GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO           #       #      
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name         Table name         Type/Size       Description
--------------------------------------------------------------------------------------
txtDOCNO    BL_DOCNO            MM_BLMST           VARCHAR(8)      Document Number
cmbBILTP    BL_BILTP            MM_BLMST           VARCHAR(2)      Bill Type
txtPRTCD    BL_PRTCD            MM_TPRRT           VARCHAR(5)      Party Code  
txtTRNNM    GR_TRNNM            MM_GRMST           VARCHAR(40)     Transporter Name
txtLOCCD    BIL_LOCCD           MM_BILTR           VARCHAR(3)      Location Code
txtBILNO    BL_BILNO            MM_BLMST           VARCHAR(10)     Bill Number
txtBILDT    BL_BILDT            MM_BLMST           DATE            Bill Date
txtBILAM    BL_BILAM            MM_BLMST           DECIMAL(12,2)   Billed Amount
txtRTPMT    TPR_RTPMT           MM_RTPMT           DECIMAL(12,2)   Rate per MT  
txtFMDAT    GR_CHLDT            MM_GRMST           DATE            Challan Date
txtTODAT    GR_CHLDT            MM_GRMST           DATE            Challan Date
txtCALAM    (dblCALAM+dblOTHPA-dblOTHDA-dblTDDAM)
txtDRTPK    BL_DRPKG            MM_BLMST           DECIMAL(12,2)   Deducation Rate
txtOTHPA    BL_OTPMT            MM_BLMST           DECIMAL(12,2)   Other Payment
txtOTHDA    BL_OTHDA            MM_BLMST           DECIMAL(12,2)   Other Deducation
txtREMDS    BL_REMDS            MM_BLMST           VARCHAR(100)    Remark
--------------------------------------------------------------------------------------
<B>
For Bill Type:</b>
     Table used  : 1)CO_CDTRN                             
     Conditation : 1) CMT_CGMTP = 'SYS' 
                   2) AND CMT_CGSTP = 'MMXXBTP' 
                   3) AND CMT_CODCD ='03'"
<B>For Document Number:</B>
     Table used  : 1)MM_BLMST                             
     Conditation : 1) BL_BILTP = Selected Bill Type
                   2) And isnull(BL_STSFL,'') <> 'X'";
<b>For Transportation Trip details:</b>(Table Displayed on Screen)
     Tables used : 1)MM_GRMST
                   2)MM_WBTRN                                                
     Conditation : 1) GR_STRTP = M_strSBSCD.substring(2,4) (Sub-System Code)
                   2) AND GR_GRNNO = WB_DOCRF
                   3) AND WB_DOCTP = Document Type (for Tanker "01")
                   4) AND WB_LOCCD = Given Location Code
                   5) AND isnull(GR_BILQT,0) < GR_RECQT 
                   6) AND GR_CHLDT BETWEEN given Date range
                   7) AND GR_TRNCD = Given Transporter Code
                   8) AND GR_BATNO = Default Batch No. ("COMMON")
                   9) AND isnull(GR_STSFL,' ') <> 'X'					
                  10) AND isnull(GR_STSFL,' ') <> 'X'
<I><B>Validations :</B>
For Modification:
      Modification is not allowed here.
For addition:
    - Bill number cannoy be already passed
    - Bill Type must be "03 Incomming Transportation"
    - Transportation Trip must be exists for given date range.
    - After Sucessful Insertion Of Data, Report Printing for given Doc. 
      number details is allowed.
for Deletion:
    - Document number must be valid.
for Enquiry:
    - Document number must be valid.
    - Report Printing for given Doc. number details is allowed.
</I> */
class mm_tetbp extends cl_pbase
{									/** JComboBox to display & select Bill Type.*/
	private JComboBox cmbBILTP;		/** JTextField to display & accept Bill Number.*/
	private JTextField txtBILNO;	/** JTextField to display & accept Bill Date.*/
	private JTextField txtBILDT;	/** JTextField to display & accept From Date.*/
	private JTextField txtFMDAT;	/** JTextField to display & accept To Date.*/
	private JTextField txtTODAT;	/** JTextField to display & accept Transporter Code.*/
	private JTextField txtTRNCD;	/** JTextField to display & accept Location Code.*/
	private JTextField txtLOCCD;	/** JTextField to display & accept Deducation Rate per Kg.*/
	private JTextField txtDRTPK;	/** JTextField to display Transporter Name.*/	
	private JTextField txtTRNNM;	/** JTextField to display & accept Bill Amount*/
	private JTextField txtRTPMT;	/** JTextField to display & accept Deducation Rate per Metric Ton.*/
	private JTextField txtBILAM;	/** JTextField to display Calculated Amount.*/
	private JTextField txtCALAM;	/** JTextField to display & accept Document Number.*/
	private JTextField txtDOCNO;	/** JTextField to display & accept Other Payments.*/
	private JTextField txtOTHPA;	/** JTextField to display & accept Other Deducations.*/
	private JTextField txtOTHDA;	/** JTextField to display & accept Remarks.*/
	private JTextField txtREMDS;	/** JTextField to display & accept Total Challan Quantity.*/
	private JTextField txtTCHQT;	/** JTextField to display & accept Total Receipt Quantity.*/
	private JTextField txtTRCQT;	/** JTextField to display & accept Total Shortage*/
	private JTextField txtTSHRT;	/** JButton to print the Report.*/		
	private JButton btnPRINT;			/** JTable to display the bill Details.*/
	private cl_JTable tblBILDT;	
	private JTextField txtPMDDT;
	/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();	/** String variable for Bill Date.*/
		private String strBILDT;			/** String variable for Document Number.*/
		private String strDOCNO;		/** String variable for Bill Type.*/	
		private String strBILTP;		/** String variable for Challan Quantity.*/
		private String strCHLQT;		/** String variable for Receipt Quantity.*/
		private String strRECQT;		/** String variable for Differance Quantity.*/
		private String strDIFQT;		/** String variable for Deducation Rate rer Kg.*/
		private String strDRTPK;		/** String variable for Location Description.*/
		private String strLOCDS;		/** String variable for Vendor Type, here it is "T".*/
		private String strVENTP="T";	/** String variable for Rate per Metric Ton.*/
		private String strRTPMT;		/** String variable for location Code.*/
		private String strLOCCD;		/** Charector variable for Deducation Type.*/
	private char chrDEDTP = 'V';		/** Integer variable for Row Count.*/	
	private int intROWCT;				/** Double Varaible for Deducation Amount.*/	
	private double dblTDDAM =0;			/** Double Varaible for Total Challan Quantity.*/
	private double dblTCHQT =0;			/** Double Varaible for total Transfer Quanity.*/
	private double dblTRCQT =0;			/** FileOutputStream object for generated File Hendling.*/				
	private FileOutputStream fosREPORT;		/** DataOutputStream object to generate the Stream of Report Data.*/	
	private DataOutputStream dosREPORT;		/** Final Integer to represent Check-Flag column of the JTable.*/
	CallableStatement cstPLTRN_BIL; // Stored procedure for updating bill amount into party ledger (MM_PLTRN)
	
		final int TBL_CHKFL = 0;			/** Final Integer to represent GRIN Date column.*/
		final int TBL_GRNDT = 1;			/** Final Integer to represent GRIN number column.*/	
		final int TBL_GRNNO = 2;			/** Final Integer to represent Material Code column.*/
		//final int TBL_PORNO = 3;
		final int TBL_MATCD = 3;			/** Final Integer to represent Challan Number column.*/
		final int TBL_CHLNO = 4;			/** Final Integer to represent Challan Quantity column.*/
		final int TBL_CHLQT = 5;			/** Final Integer to represent Receipt Quantity column.*/
		final int TBL_RECQT = 6;			/** Final Integer to represent Differance in Challan Quantity & Receipt Quantity.*/
		final int TBL_DIFQT = 7;			/** Final Integer to represent Shortage Quantity column.*/
		final int TBL_SHRPR = 8;			/** Final Integer to represent Ducation amount for Shortages > 0.2% & < 0.5%.*/
		final int TBL_SHRP1 = 9;			/** Final Integer to represent Ducation amount for Shortages > 0.5%.*/
		final int TBL_SHRP2 = 10;			/** Final Integer to represent Total Amount column.*/
		final int TBL_TOTAM = 11;			/** Final String for Incoming transportation.*/
	final String strINCTP_fn = "03";		/** Final String for Document Type - Tanker.*/
	final String strTNKTP_fn = "01";		/** Final String for Default Bach No.*/
	final String strDFBAT_fn ="COMMON";		
	mm_tetbp()
	{				
		super(2);
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMatrix(20,8);
			
			add(btnPRINT = new JButton("Print"),1,7,1,1,this,'L');
			
			add(new JLabel("Bill Type"),2,1,1,.7,this,'R');
			add(cmbBILTP = new JComboBox(),2,2,1,1.8,this,'L');									
			add(new JLabel("SPL/DOC.No."),2,4,1,1,this,'R');
			add(txtDOCNO = new TxtNumLimit(8),2,5,1,1.2,this,'R');
		
			add(new JLabel("Party Code"),3,1,1,.7,this,'R');
			add(txtTRNCD = new TxtLimit(5),3,2,1,1,this,'L');
			add(txtTRNNM = new TxtLimit(40),3,3,1,4,this,'L');							
			add(new JLabel("Location"),3,7,1,.9,this,'R');
			add(txtLOCCD = new TxtLimit(3),3,8,1,1.2,this,'R');
			
			add(new JLabel("Bill No."),4,1,1,.7,this,'R');
			add(txtBILNO = new TxtLimit(10),4,2,1,1,this,'L');
			add(new JLabel("Bill Date"),4,3,1,.9,this,'R');
			add(txtBILDT = new TxtDate(),4,4,1,1.2,this,'R');							
			add(new JLabel("Bill Amount"),4,5,1,.8,this,'R');
			add(txtBILAM = new TxtNumLimit(12.2),4,6,1,1,this,'R');
			add(new JLabel("Rate/MT"),4,7,1,.9,this,'R');
			add(txtRTPMT = new TxtNumLimit(12.2),4,8,1,1.2,this,'R');
		
			add(new JLabel("From Date"),5,1,1,.7,this,'R');
			add(txtFMDAT = new TxtDate(),5,2,1,1,this,'L');
			add(new JLabel("To Date"),5,3,1,.9,this,'R');
			add(txtTODAT = new TxtDate(),5,4,1,1.2,this,'R');							
			add(new JLabel("Cal.Amount"),5,5,1,.8,this,'R');
			add(txtCALAM = new TxtNumLimit(12.2),5,6,1,1,this,'R');
			add(new JLabel("Ded.Rate/Kg"),5,7,1,.9,this,'R');
			add(txtDRTPK = new TxtNumLimit(10.2),5,8,1,1.2,this,'R');
		
			add(new JLabel("Other Pay."),6,1,1,.7,this,'R');
			add(txtOTHPA = new TxtNumLimit(10.2),6,2,1,1,this,'L');
			add(new JLabel("Other Ded."),6,3,1,.9,this,'R');
			add(txtOTHDA = new TxtNumLimit(10.2),6,4,1,1.2,this,'R');							
			add(new JLabel("Remarks"),6,5,1,.8,this,'R');
			add(txtREMDS = new JTextField(),6,6,1,3,this,'L');
			add(new JLabel("Pay.Due Date"),7,5,1,1,this,'L');
			add(txtPMDDT=new TxtDate(),7,6,1,1,this,'L');
		
			String[] L_COLHD = {"","Challan Date","GRIN No.","Material","Challan No.","Chalan Qty.","Received Qty.","Shortage","Shortage %",">0.3 <=0.5",">0.5","Amount"};
			int[] L_COLSZ = {10,80,70,80,80,80,80,80,80,60,60,80};			
			tblBILDT = crtTBLPNL1(this,L_COLHD,500,8,1,7.8,8,L_COLSZ,new int[]{0});			
		
			add(new JLabel("Chalan Qty."),16,4,1,1,this,'L');
			add(new JLabel("Reciept Qty."),16,5,1,1,this,'L');
			add(new JLabel("Shortage Qty."),16,6,1,1,this,'L');		
		
			add(new JLabel("Total"),17,3,1,.5,this,'R');
			add(txtTCHQT = new TxtNumLimit(12.3),17,4,1,1,this,'L');
			add(txtTRCQT = new TxtNumLimit(12.3),17,5,1,1,this,'L');							
			add(txtTSHRT = new TxtNumLimit(12.3),17,6,1,1,this,'L');
		
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXBTP' and CMT_CODCD ='03'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);						
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{						
					cmbBILTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}				
			
			setENBL(false);					
			txtTRNNM.setEnabled(false);					
			txtCALAM.setEnabled(false);
			txtTCHQT.setEnabled(false);
			txtTRCQT.setEnabled(false);
			txtTSHRT.setEnabled(false);
			btnPRINT.setEnabled(false);
			setMSG("Select an Option..",'N');
		
			txtTRNCD.setInputVerifier(objINPVR);
			txtLOCCD.setInputVerifier(objINPVR);
			txtBILDT.setInputVerifier(objINPVR);
			txtFMDAT.setInputVerifier(objINPVR);
			txtTODAT.setInputVerifier(objINPVR);
			
			cl_dat.M_flgHELPFL_pbst = false;			
			setCursor(cl_dat.M_curDFSTS_pbst);			
		}
		catch(Exception L_E)
		{			
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_E,"const");			
		}				
	}	
	/**
	 * Super Class metdhod overrided to inhance its funcationality, to enable disable 
	 * components according to requriement.
	 */
	void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(false);
		cmbBILTP.setEnabled(false);
		txtDOCNO.setEnabled(false);
		txtBILNO.setEnabled(false);
		txtBILDT.setEnabled(false);
		txtFMDAT.setEnabled(false);
		txtBILAM.setEnabled(false);
		txtLOCCD.setEnabled(false);
		txtDRTPK.setEnabled(false);
		txtOTHPA.setEnabled(false);
		txtOTHDA.setEnabled(false);
		txtREMDS.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
		{
			setMSG("Select an Option..",'N');
			return;
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			cmbBILTP.setEnabled(true);
			txtTRNCD.setEnabled(true);
			txtLOCCD.setEnabled(true);
			txtBILNO.setEnabled(true);
			txtBILDT.setEnabled(true);			
			txtBILAM.setEnabled(true);
			txtFMDAT.setEnabled(true);
			txtTODAT.setEnabled(true);
			txtDRTPK.setEnabled(true);
			txtOTHPA.setEnabled(true);
			txtOTHDA.setEnabled(true);
			txtREMDS.setEnabled(true);
			txtPMDDT.setEnabled(true);
			txtTRNCD.requestFocus();
		}
		else
		{
			txtDOCNO.setEnabled(true);			
			txtDOCNO.requestFocus();
		}		
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);											
			if(M_objSOURC == btnPRINT)
			{				
				mm_rptbp objRPTBP  = new mm_rptbp(M_strSBSCD);
				objRPTBP.getALLREC(strDOCNO,(String.valueOf(cmbBILTP.getSelectedItem()).toString()).trim().substring(0,2));
				JComboBox L_cmbLOCAL = objRPTBP.getPRNLS();
				objRPTBP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rptbp.doc",L_cmbLOCAL.getSelectedIndex());
				btnPRINT.setEnabled(false);								
			}						
			else if(M_objSOURC == txtDOCNO)
			{
				if((txtDOCNO.getText().trim()).length()<8)
				{
					setMSG("Insert complete Document Number & then Press Enter key..",'E');
					return;
				}
				this.setCursor(cl_dat.M_curWTSTS_pbst);				
				strBILTP = String.valueOf(cmbBILTP.getSelectedItem()).trim().substring(0,2);
				strDOCNO = txtDOCNO.getText().trim();
				if(getDATA(txtTRNCD.getText().trim(),txtLOCCD.getText().trim()))
				{
					setENBL(false);					
					cmbBILTP.setEnabled(false);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						btnPRINT.setEnabled(true);
				}
				else
				{
						setMSG("Record could not be found..",'E');
						btnPRINT.setEnabled(false);
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if((M_objSOURC == txtTRNCD)&& ((txtTRNCD.getText().trim()).length()>0))
			{					
				txtTRNCD.setText((txtTRNCD.getText().trim()).toUpperCase());
				ResultSet L_rstRSSET;
				M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
				M_strSQLQRY += " where PT_PRTTP = '"+"T"+"'";
				M_strSQLQRY += " and PT_PRTCD = '" + txtTRNCD.getText().trim() + "'";
				L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
				if(L_rstRSSET !=null)
				if(L_rstRSSET.next())
				{
					txtTRNNM.setText(L_rstRSSET.getString("PT_PRTNM"));
					setMSG("",'N');
					L_rstRSSET.close();								
					return;
				}
				txtTRNNM.setText("");
				txtTRNCD.requestFocus();
				setMSG("Invalid Supplier, Press F1 for Help..",'E');				
			}
			else if(M_objSOURC == cmbBILTP)
			{
				strBILTP = String.valueOf(cmbBILTP.getSelectedItem()).trim().substring(0,2);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					txtTRNCD.requestFocus();
				else
					txtDOCNO.requestFocus();
			}			
			else if(M_objSOURC == txtLOCCD)
			{
				ResultSet L_rstRSSET;
				if((txtTRNCD.getText().trim()).length()==0)
				{
					setMSG("Please Enter Transportor code first..",'E');
					txtTRNCD.requestFocus();
					return;
				}
				if((txtLOCCD.getText().trim()).length()>0)
				{
					M_strSQLQRY = "Select TPR_FRMLC,CMT_CODDS,TPR_RTPMT,TPR_DEDTP from"
						+" MM_TPRRT,CO_CDTRN where CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP ='QC11TKL' AND CMT_CODCD = TPR_FRMLC"
						+" AND TPR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TPR_PRTTP ='T' AND TPR_PRTCD = '"+(txtTRNCD.getText().trim()).toUpperCase() +"'"
						+" AND TPR_TPRTP ='TNK'";
					if((txtLOCCD.getText().trim()).length()>0)
						M_strSQLQRY +=" AND TPR_FRMLC ='" + (txtLOCCD.getText().trim()).toUpperCase()+"'";									
					L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET!= null)
					{						
						if(L_rstRSSET.next())
						{							
							txtRTPMT.setText("");
							txtRTPMT.setText(L_rstRSSET.getString("TPR_RTPMT"));						
							L_rstRSSET.close();									
						}
						tblBILDT.clrTABLE();
					}
				}
			}						
			else if(M_objSOURC == txtREMDS)
			{			
				tblBILDT.clrTABLE();
				setMSG("Data Fetching in progress..",'N');					
				getDATA(txtTRNCD.getText().trim(),txtLOCCD.getText().trim());					
				setMSG(" ",'N');			
			}
			else if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))//Mod selected
				{
					JOptionPane.showMessageDialog(this,"Modification is not allowed, Delete the record & Enter new record..","Operation Validation",JOptionPane.INFORMATION_MESSAGE);
					cl_dat.M_cmbOPTN_pbst.setSelectedItem(cl_dat.M_cmbOPTN_pbst.getItemAt(0));
					setENBL(false);
					cl_dat.M_cmbOPTN_pbst.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					clrCOMP();
					setENBL(true);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{		
		super.keyPressed(L_KE);
		setMSG("",'N');
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{							
			tblBILDT.clrTABLE();
			btnPRINT.setEnabled(false);
			if(M_objSOURC == txtDOCNO)		// GRIN No //hlpDOCNO()
			{				
				try
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDOCNO";												
					String L_ARRHDR[] = {"SPL Doc. No.","Bill No.","Bill Date","Transporter Code"};
					M_strSQLQRY = "Select BL_DOCNO,BL_BILNO,BL_BILDT,BL_PRTCD";
					M_strSQLQRY += " from MM_BLMST";
					M_strSQLQRY += " where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '" + strBILTP + "'";
					if((txtDOCNO.getText().trim()).length()>0)
						M_strSQLQRY += " AND BL_DOCNO LIKE '" +txtDOCNO.getText().trim()+ "%'";
					M_strSQLQRY += " AND isnull(BL_STSFL,'') <> 'X'";
					M_strSQLQRY += " order by BL_DOCNO Desc";																
					clrCOMP();
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"hlpDOCNO");
				}	
			}
			else if(M_objSOURC == txtTRNCD)	// Supplier Code//	hlpTRNCD();
			{
				try
				{
					txtTRNNM.setText("");
					txtTRNCD.setText((txtTRNCD.getText().trim()).toUpperCase());
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTRNCD";						
					String L_ARRHDR[] = {"Code","Name"};
					M_strSQLQRY = "Select distinct GR_TRNCD,GR_TRNNM from MM_GRMST ";
					M_strSQLQRY += "where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <>'X' AND isnull(GR_BILQT,0) = 0 ";
					if((txtTRNCD.getText().trim()).length()>0)
						M_strSQLQRY += " AND GR_TRNCD LIKE '" +(txtTRNCD.getText().trim()).toUpperCase() + "%'";
					M_strSQLQRY += "AND GR_STRTP ='"+ M_strSBSCD.substring(2,4)+"' ORDER BY GR_TRNNM";																				
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"hlpTRNCD");
				}	
			}				
			else if(M_objSOURC == txtLOCCD)	// Location Code //hlpLOCCD();
			{
				if((txtTRNCD.getText().trim()).length()==0)
				{
					setMSG("Please Enter Transportor code first..",'E');
					txtTRNCD.requestFocus();
					return;
				}
				try
				{ 
					txtLOCCD.setText((txtLOCCD.getText().trim()).toUpperCase());
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtLOCCD";			
					String L_ARRHDR[] = {"Code","Location","Rate/MT","Deduction Type"};													
					M_strSQLQRY = "Select TPR_FRMLC,CMT_CODDS,TPR_RTPMT,TPR_DEDTP from MM_TPRRT,CO_CDTRN";
					M_strSQLQRY +=" where CMT_CGMTP ='SYS' AND CMT_CGSTP ='QC11TKL' AND CMT_CODCD = TPR_FRMLC";
					M_strSQLQRY +=" and TPR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TPR_PRTTP ='T' AND TPR_PRTCD = '"+txtTRNCD.getText().trim() +"'";
					if((txtLOCCD.getText().trim()).length()>0)
						M_strSQLQRY +=" AND TPR_FRMLC LIKE '" +(txtLOCCD.getText().trim()).toUpperCase() +"%'";
					M_strSQLQRY +=" AND TPR_TPRTP ='TNK'";															
					cl_hlp(M_strSQLQRY,1,3,L_ARRHDR,4,"CT");
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"hlpLOCCD");
				}	
			}								
		}
		else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{		
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();		
				cmbBILTP.setEnabled(true);				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{											
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						setENBL(false);				
						cmbBILTP.setEnabled(true);
						txtTRNCD.requestFocus();
					}
					else //mod, enq, del
					{					
						setENBL(false);
						txtDOCNO.requestFocus();
					}		
				}
				else
				{
					setMSG("Select an option..",'N');
					setENBL(false);			
					cmbBILTP.setEnabled(false);
					txtBILNO.setEnabled(false);		
				}
			}			   
			if(M_objSOURC == txtBILNO)
			{
				if((txtBILNO.getText().trim()).length() != 0)
				{
					setMSG("Please Insert the Bill Date & Press Enter key..",'N');
					txtBILDT.requestFocus();
				}
				else
				{
					setMSG("Bill number cannot be blank..",'E');
					txtBILNO.requestFocus();
				}
			}			
			else if(M_objSOURC == txtTRNCD)
			{
				if((txtTRNCD.getText().trim()).length()>0)
				{
					setMSG("Please select the Transporter from F1 list & Press Enter key..",'N');
					txtLOCCD.requestFocus();
				}
				else
				{
					setMSG("Location cannot be Blank, Please select Transporter from F1 list..",'E');					
					txtTRNCD.requestFocus();
				}					
			}
			else if(M_objSOURC == txtLOCCD)
			{
				txtLOCCD.setText((txtLOCCD.getText().trim()).toUpperCase());
				if((txtLOCCD.getText().trim()).length()>0)
				{					
					setMSG("Please enter the bill No. & Press Enter key..",'N');
					txtBILNO.requestFocus();
				}
				else
				{
					setMSG("Location cannot be blank,Please select the location from F1 list..",'E');
					txtLOCCD.requestFocus();
				}
			}
			else if(M_objSOURC == txtBILNO)
			{
				if((txtBILNO.getText().trim()).length()==0)
				{
					setMSG("Please enter the bill date & Press Enter key..",'N');
					txtBILDT.requestFocus();
				}
				else
				{
					setMSG("Bill No. cannot be Blank,Please enter the bill no..",'E');
					txtBILNO.requestFocus();
				}
			}
			else if(M_objSOURC == txtBILDT)
			{
				if((txtBILDT.getText().trim()).length()>0)
				{					
					setMSG("Please enter the bill amount & Press Enter key..",'N');					
					txtBILAM.requestFocus();
				}
				else			
					setMSG("Bill Date Can not be empty..",'E');				
			}
			else if(M_objSOURC == txtBILAM)
			{
				if((txtBILAM.getText().trim()).length()>0)
				{
					setMSG("Please enter the from date value & Press Enter key..",'N');
					txtFMDAT.requestFocus();
				}
				else
				{
					txtBILAM.setText("0.00");					
				}				
			}			
			else if(M_objSOURC == txtFMDAT)
			{				
				if((txtFMDAT.getText().trim()).length() > 0)
				{
					txtTODAT.requestFocus();
					setMSG("Please insert Todate & Press Enter key..",'N');
				}	
				else
					setMSG("From Date Can not be empty..",'E');					
			}
			else if(M_objSOURC == txtTODAT)
			{				
				if((txtTODAT.getText().trim()).length() > 0)
				{
					setMSG("Please specify the Deduction Rate/Kg & Press Enter key..",'N');
					txtDRTPK.requestFocus();
				}
				else
					setMSG("To Date Can not be empty..",'E');										
			}
			else if(M_objSOURC == txtDRTPK)
			{				
				setMSG("please insert deducation rate per Kg & Press Enter key..",'N');
				if((txtDRTPK.getText().trim()).length()>0)				
					txtOTHPA.requestFocus();					
				else
					txtDRTPK.setText("0.00");					
			}
			else if(M_objSOURC == txtOTHPA)
			{
				setMSG("Please specify the other amount to be deducted & Press Enter key..",'N');
				if((txtOTHPA.getText().trim()).length()>0)				
					txtOTHDA.requestFocus();
				else
					txtOTHPA.setText("0.00");									
			}
			else if(M_objSOURC == txtOTHDA)
			{		
				setMSG("Please Specify the Remarks if any & Press Enter key..",'N');
				if((txtOTHDA.getText().trim()).length()>0)				
					txtREMDS.requestFocus();												
				else				
					txtOTHDA.setText("0.00");
			}	
			else if(M_objSOURC == txtREMDS)
			{	txtPMDDT.requestFocus();	
				setMSG("Please enter bill pymt date",'N');
				
			}
			else if(M_objSOURC == txtPMDDT)
			{	cl_dat.M_btnSAVE_pbst.requestFocus();	
				setMSG("Save the record",'N');
				
			}
			
			else if(M_objSOURC == txtRTPMT)
			{
				if((txtFMDAT.getText().trim().length() >0)&&(txtTODAT.getText().trim().length() >0)&&(txtDRTPK.getText().trim().length() >0))
				{
					setMSG("Data Fetching in progress..",'N');					
					tblBILDT.clrTABLE();
					getDATA(txtTRNCD.getText().trim(),txtLOCCD.getText().trim());
					setMSG(" ",'N');
				}
			}	
		/*	else if(L_KE.getKeyCode() == 106)// * pressed
			{
				clrCOMP();
				cl_dat.M_cmbOPTN_pbst.setSelectedIndex(0);				
				setENBL(false);
				txtDOCNO.setEnabled(false);
			}*/
		}
	}	
	/**
	 * Method to perform validity check of the Data entered, Before inserting 
	 *new data in the data base.
	 */
	boolean vldDATA()
	{	
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))			
			{						
				strBILTP = String.valueOf(cmbBILTP.getSelectedItem()).trim().substring(0,2);
				strDOCNO = txtDOCNO.getText().trim();				
				strBILDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBILDT.getText().trim()));							
				strRTPMT = txtRTPMT.getText().trim();
				strLOCCD = txtLOCCD.getText().trim();				
				strDRTPK = txtDRTPK.getText().trim();	
				if((txtBILNO.getText().trim()).length()==0)
				{
					setMSG("Bill number cannot be blank, Please Insert the Bill number..",'E');
					txtBILNO.requestFocus();
					return false;
				}				
				else if((txtTRNCD.getText().trim()).length()==0)
				{					
					setMSG("Location cannot be Blank, Please select Transporter from F1 list..",'E');					
					txtTRNCD.requestFocus();
					return false;
				}											
				else if((txtLOCCD.getText().trim()).length()==0)
				{					
					setMSG("Location cannot be blank,Please select the location from F1 list..",'E');
					txtLOCCD.requestFocus();
					return false;
				}				
				else if((txtBILDT.getText().trim()).length()==0)
				{					
					setMSG("Bill Date Can not be empty..",'E');					
					txtBILDT.requestFocus();
					return false;
				}							
				else if((txtBILAM.getText().trim()).length()==0)
				{
					setMSG("Bill amount Can not be empty..",'E');
					txtBILAM.setText("0.00");
					txtBILAM.requestFocus();
					return false;
				}									
				else if((txtFMDAT.getText().trim()).length() == 0)
				{
					setMSG("From Date Can not be empty..",'E');
					txtFMDAT.requestFocus();
					return false;
				}							
				else if((txtTODAT.getText().trim()).length() == 0)
				{
					setMSG("To Date Can not be empty..",'E');
					txtTODAT.requestFocus();
					return false;
				}								
				else if((txtDRTPK.getText().trim()).length()==0)				
				{
					setMSG("Deducation Rate/Kg Can not be blank..",'E');
					txtDRTPK.setText("0.00");
					return false;
				}
				else if((txtRTPMT.getText().trim()).length()==0)				
				{
					setMSG("Rate/MT Can not be blank..",'E');
					return false;
				}
				else if(Double.parseDouble(txtBILAM.getText())==0)
				{
					setMSG("Bill amount Can not be zero..",'E');
					txtBILAM.requestFocus();
					return false;
				}	
				else if(Double.parseDouble(txtDRTPK.getText())==0)
				{
					setMSG("Deduction Rate Can not be zero..",'E');
					txtBILAM.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtPMDDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
				{
					setMSG("Bil PMT Date should be equal or more than current date..",'E');
					return false;
				}
				if((txtOTHPA.getText().trim()).length()==0)									
				{
					setMSG("Other payment Can not be blank..",'E');
					txtOTHPA.setText("0.00");
					//txtOTHPA.requestFocus();
					//return false;
				}
				if((txtOTHDA.getText().trim()).length()==0)									
				{
					setMSG(" Other Deducation cannot be blank..",'E');
					txtOTHDA.setText("0.00");
					//txtOTHDA.requestFocus();
					//return false;
				}
				if((txtREMDS.getText().trim()).length()==0)
				{
					txtREMDS.setText(" ");			
				}
				
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}	
	/**
	 * Method to execute F1 help for the selected field.
	 */
	void exeHLPOK()
	{
		try
		{
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtDOCNO"))
			{				
				this.setCursor(cl_dat.M_curWTSTS_pbst);				
				strBILTP = String.valueOf(cmbBILTP.getSelectedItem()).trim().substring(0,2);
				txtDOCNO.setText(cl_dat.M_strHLPSTR_pbst);
				strDOCNO = cl_dat.M_strHLPSTR_pbst;
				if(getDATA(txtTRNCD.getText().trim(),txtLOCCD.getText().trim()))
				{
					setENBL(false);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						btnPRINT.setEnabled(true);
				}
				else
				{
					btnPRINT.setEnabled(false);
					setMSG("Record could not be found..",'E');
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			else if(M_strHLPFLD.equals("txtTRNCD"))			// Supplier Code
			{
				txtTRNCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtTRNNM.setText(cl_dat.M_strHLPSTR_pbst);				
				txtTRNCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtLOCCD"))			// Location code
			{
				txtLOCCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				chrDEDTP = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim().charAt(0);
				strLOCDS = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();				
				strRTPMT = cl_dat.M_strHLPSTR_pbst;
				txtRTPMT.setText(strRTPMT);
				tblBILDT.clrTABLE();
				txtRTPMT.setEnabled(true);
				txtLOCCD.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	 */
	void exeSAVE()  //exeADDREC
	{		
		try
		{		
			String L_strMATCD,L_strGRNNO;
			this.setCursor(cl_dat.M_curWTSTS_pbst);			
			if(!vldDATA())
				return ;
			else
				setMSG("",'E');
			cl_dat.M_flgLCUPD_pbst = true;	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{				
				String L_CALAM ;
				double L_dblOTHAM =0;
				double L_dblFRTAM =0;
				cl_dat.M_flgLCUPD_pbst = true;			
				L_CALAM = txtCALAM.getText().trim();

				L_dblOTHAM = (Double.parseDouble(txtOTHPA.getText().trim())- Double.parseDouble(txtOTHDA.getText().trim()))/intROWCT;
				strDOCNO = genDOCNO(strBILTP);
				txtDOCNO.setText(strDOCNO);
				M_strSQLQRY = "Insert into MM_BLMST(BL_CMPCD,BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,BL_BLPDT,";
				M_strSQLQRY += "BL_PRTTP,BL_PRTCD,BL_PRTNM,BL_BILAM,BL_CALAM,BL_DRPKG,BL_DEDTP,BL_OTPMT,BL_OTDED,BL_CURCD,BL_EXGRT,BL_PMDDT,BL_REMDS,BL_STSFL,BL_TRNFL,BL_LUSBY,BL_LUPDT) values(";			
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst +"',";
				M_strSQLQRY += "'"+strBILTP +"',";
				M_strSQLQRY += "'"+strDOCNO +"',";
				M_strSQLQRY += "'"+txtBILNO.getText().trim() +"','";
				M_strSQLQRY += strBILDT +"',";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"',";
				M_strSQLQRY += "'T',";
				M_strSQLQRY += "'"+txtTRNCD.getText().trim() +"',";
				M_strSQLQRY += "'"+txtTRNNM.getText().trim() +"',";
				M_strSQLQRY += txtBILAM.getText().trim() +",";
				M_strSQLQRY += L_CALAM +",";
				M_strSQLQRY += txtDRTPK.getText().trim() +",";
				M_strSQLQRY += "'"+chrDEDTP +"',";
				M_strSQLQRY += txtOTHPA.getText().trim() +",";
				M_strSQLQRY += txtOTHDA.getText().trim() +",";
				M_strSQLQRY += "'01',";
				M_strSQLQRY += 1.0+","; 
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPMDDT.getText().trim())) +"',";
				M_strSQLQRY += "'"+txtREMDS.getText().trim() +"',";
				M_strSQLQRY += getUSGDTL("BL",'I',"")+")";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");										
				for(int i=0;i<intROWCT;i++)
				{
					if(tblBILDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{					
						L_strMATCD = String.valueOf(tblBILDT.getValueAt(i,TBL_MATCD));
						L_strGRNNO = String.valueOf(tblBILDT.getValueAt(i,TBL_GRNNO));
						//L_strPORNO = String.valueOf(tblBILDT.getValueAt(i,TBL_PORNO));
						strCHLQT = String.valueOf(tblBILDT.getValueAt(i,TBL_CHLQT));	
						strRECQT = String.valueOf(tblBILDT.getValueAt(i,TBL_RECQT));						
						//M_strSQLQRY = "Insert into MM_BILTR(BIL_CMPCD,BIL_BILTP,BIL_DOCNO,BIL_DOCRF,isnull(BIL_PORRF,'') BIL_PORNO,BIL_MATCD,BIL_STRTP,BIL_CHLQT,BIL_RECQT,";
						M_strSQLQRY = "Insert into MM_BILTR(BIL_CMPCD,BIL_BILTP,BIL_DOCNO,BIL_DOCRF,BIL_MATCD,BIL_STRTP,BIL_CHLQT,BIL_RECQT,";
						M_strSQLQRY += "BIL_RTPMT,BIL_LOCCD,BIL_STSFL,BIL_TRNFL,BIL_LUSBY,BIL_LUPDT) values(";						
						M_strSQLQRY += "'"+ cl_dat.M_strCMPCD_pbst +"',";
						M_strSQLQRY += "'"+ strBILTP +"',";
						M_strSQLQRY += "'"+ strDOCNO +"',";
						M_strSQLQRY += "'"+ L_strGRNNO +"',";
						//M_strSQLQRY += "'"+ L_strPORNO +"',";
						M_strSQLQRY += "'"+ L_strMATCD +"',";
						M_strSQLQRY += "'"+ M_strSBSCD.substring(2,4) +"',";
						M_strSQLQRY += strCHLQT +",";
						M_strSQLQRY += strRECQT +",";
						M_strSQLQRY += txtRTPMT.getText() +",";
						M_strSQLQRY += "'"+ strLOCCD +"',";							
						M_strSQLQRY += getUSGDTL("BIL",'I',"")+")";						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");								
						if(chrDEDTP =='V')
						{
							L_dblFRTAM = Double.parseDouble(tblBILDT.getValueAt(i,TBL_TOTAM).toString())+L_dblOTHAM;
						}
						else if(chrDEDTP =='T')
						{
							L_dblFRTAM = Double.parseDouble(tblBILDT.getValueAt(i,TBL_TOTAM).toString())+L_dblOTHAM;
						}
						else if(chrDEDTP =='B')
						{
							L_dblFRTAM = Double.parseDouble(tblBILDT.getValueAt(i,TBL_TOTAM).toString())+L_dblOTHAM-(dblTDDAM/intROWCT);
						}
						M_strSQLQRY = "Update MM_GRMST set";
						M_strSQLQRY += " GR_BILRF = '" + strDOCNO + "',";
						M_strSQLQRY += " GR_BILQT = " + strRECQT + ",";
				        M_strSQLQRY += " GR_FRTAM = " + setNumberFormat(L_dblFRTAM,2)+",";
						M_strSQLQRY += getUSGDTL("GR",'U',"");
						M_strSQLQRY += " WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP ='"+M_strSBSCD.substring(2,4) +"' AND isnull(GR_STSFL,'') <>'X'" ;
						M_strSQLQRY += " AND GR_GRNNO = '" + L_strGRNNO + "'";							
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");														
					}
				}
				cstPLTRN_BIL = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_BIL(?,?,?,?)");
				cstPLTRN_BIL.setString(1,cl_dat.M_strCMPCD_pbst);
				cstPLTRN_BIL.setString(2,strBILTP);
				cstPLTRN_BIL.setString(3,txtDOCNO.getText().trim());
				cstPLTRN_BIL.setString(4,txtTRNNM.getText().trim());
				cstPLTRN_BIL.executeUpdate();
				cl_dat.M_conSPDBA_pbst.commit();
				
				if(cl_dat.M_flgLCUPD_pbst)
					exeDOCNO(strDOCNO,strBILTP);
			}	
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{				
				String L_BILTP = String.valueOf(cmbBILTP.getSelectedItem()).trim().substring(0,2);				
				cl_dat.M_flgLCUPD_pbst = true;				
				M_strSQLQRY = "UPDATE MM_BILTR SET ";
				M_strSQLQRY += getUSGDTL("BIL",'U',"X");				
				M_strSQLQRY += " WHERE BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BIL_BILTP = '"+L_BILTP+"'";
				M_strSQLQRY += " and BIL_DOCNO = '"+txtDOCNO.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
						
				if(cl_dat.M_flgLCUPD_pbst == true)
				{
					M_strSQLQRY = "UPDATE MM_BLMST SET ";				
					M_strSQLQRY += getUSGDTL("BL",'U',"X");					
					M_strSQLQRY += " WHERE BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '"+L_BILTP+"'";
					M_strSQLQRY += " and BL_DOCNO = '"+txtDOCNO.getText().trim()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");						
				}
								
				if(cl_dat.M_flgLCUPD_pbst == true)
				{
					M_strSQLQRY = "UPDATE MM_GRMST SET GR_BILQT =0,GR_BILRF ='',";
					M_strSQLQRY += "GR_FRTAM =0,";
					M_strSQLQRY += getUSGDTL("GR",'U',"");					
					M_strSQLQRY += " WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_BILRF = '"+txtDOCNO.getText().trim()+"' AND isnull(GR_STSFL,'') <>'X'";					
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
				}																			
			}	
			if(cl_dat.exeDBCMT("exeSAVE"))
			{									
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))				
				{
					setMSG("Saved Successfully..",'N'); 
					strDOCNO = txtDOCNO.getText().trim();
					JOptionPane.showMessageDialog(this,"SPL Document No. is -  " + strDOCNO,"SPL DOC No.",JOptionPane.INFORMATION_MESSAGE);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');									 
				strDOCNO = txtDOCNO.getText().trim();
				clrCOMP();	
				
				setENBL(false);
				btnPRINT.setEnabled(true);
			}
			else
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'N');
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");			
		}
	}
		
	/**
	 *Method to retrieve the data from mm_blmst & mm_biltr table
	 * @param LP_TRNCD String argument to pass Transport Code.	
	 * @param LP_LOCCD String argument to pass Location Code
	 */
	private boolean getDATA(String LP_TRNCD,String LP_LOCCD)
	{				
		boolean L_FIRST = true;	
		try
		{					
			String L_TRNCD,L_CHLQT,L_RECQT,L_SHRPR,L_CALAM;
			double L_dblDEDAM=0,L_dblCALVL=0,L_dblDCHQT=0;
			double L_dblTDFQT =0,L_dblTSHRT =0, dblTDDAM =dblTCHQT=dblTRCQT=0;
			//dblTDED1=dblTDED2=
			int i = 0;
			double L_dblCALAM =0;
			intROWCT =0;			
			java.sql.Date L_datTEMP;
			//LM_CELRND.setHorizontalAlignment(JLabel.RIGHT);
			txtCALAM.setText("0");				
			ResultSet L_rstRSSET;
			strRTPMT = txtRTPMT.getText().trim();
			strLOCCD = txtLOCCD.getText().trim();				
			strDRTPK = txtDRTPK.getText().trim();	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		    {
				String L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
				String L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
				
				//M_strSQLQRY = "Select GR_STRTP,GR_GRNNO,GR_CNSNO,GR_PORNO BIL_PORNO,GR_VENCD,GR_VENNM,";
				M_strSQLQRY = "Select GR_STRTP,GR_GRNNO,GR_CNSNO,GR_VENCD,GR_VENNM,";
				M_strSQLQRY += " GR_TRNCD,GR_TRNNM,GR_MATCD,WB_LOCCD,";
				M_strSQLQRY += " GR_CHLDT,GR_CHLNO,GR_CHLQT,GR_RECQT from MM_GRMST,MM_WBTRN";
				M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += " and GR_GRNNO = WB_DOCRF AND GR_CMPCD=WB_CMPCD ";
				M_strSQLQRY += " and WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strTNKTP_fn + "'";
				M_strSQLQRY += " and WB_LOCCD = '" + LP_LOCCD + "'";
				M_strSQLQRY += " and isnull(GR_BILQT,0) < GR_RECQT AND GR_CHLDT BETWEEN '" +L_strFMDAT+"' AND '"+L_strTODAT;
				M_strSQLQRY += "' and GR_TRNCD = '" + LP_TRNCD + "'";
			//	M_strSQLQRY += " and GR_BATNO = '" + strDFBAT_fn + "'";
				M_strSQLQRY += " and isnull(GR_STSFL,' ') <> 'X'";
				M_strSQLQRY += " and isnull(WB_STSFL,' ') <> 'X'";
				M_strSQLQRY += " order by GR_CHLDT,GR_CHLNO";
			//	System.out.println(M_strSQLQRY);
				if(txtOTHPA.getText().length() ==0)
					txtOTHPA.setText("0.00");
				if(txtOTHDA.getText().length() ==0)
					txtOTHDA.setText("0.00");
				
			}
			else //if(cl_dat.M_FLGOPT =='E')
		    {				
				strDOCNO = txtDOCNO.getText().trim();
				//M_strSQLQRY = "Select BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,BL_BLPDT,BL_PRTTP,BL_PRTCD,BL_BILAM,BL_CALAM,BL_DRPKG,BL_DEDTP,BL_OTPMT,BL_OTDED,BL_REMDS,BIL_DOCRF,isnull(BIL_PORRF,'') BIL_PORNO,BIL_MATCD,BIL_STRTP,";
				M_strSQLQRY = "Select BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,BL_BLPDT,BL_PRTTP,BL_PRTCD,BL_BILAM,BL_CALAM,BL_DRPKG,BL_DEDTP,BL_OTPMT,BL_OTDED,BL_REMDS,BIL_DOCRF,BIL_MATCD,BIL_STRTP,";
				M_strSQLQRY += " BIL_CHLQT,BIL_RECQT,BIL_RTPMT,BIL_LOCCD,GR_CHLDT,GR_CHLNO,GR_MATCD";
				M_strSQLQRY += " from MM_BLMST,MM_BILTR,MM_GRMST";
				M_strSQLQRY += " where BL_BILTP = BIL_BILTP and BL_DOCNO = BIL_DOCNO AND BL_CMPCD=BIL_CMPCD AND BIL_CMPCD + BIL_STRTP + BIL_DOCRF =GR_CMPCD + GR_STRTP + GR_GRNNO and BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '" + strBILTP + "'";
				M_strSQLQRY += " and BL_DOCNO = '"+strDOCNO +"'";
				M_strSQLQRY += " and isnull(BL_STSFL,'') <> 'X' and BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(BIL_STSFL,'') <> 'X'";
				M_strSQLQRY += " order by GR_CHLDT,GR_CHLNO";	
			}
			System.out.println(M_strSQLQRY);
			this.setCursor(cl_dat.M_curWTSTS_pbst);			
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,   ResultSet.CONCUR_UPDATABLE);
			M_rstRSSET = L_stat.executeQuery(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				intROWCT=0;
				while(M_rstRSSET.next())
					intROWCT++;				
				M_rstRSSET.beforeFirst();
				while(M_rstRSSET.next())										
				{			
					tblBILDT.setValueAt(new Boolean(true),i,TBL_CHKFL);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						L_datTEMP = M_rstRSSET.getDate("GR_CHLDT");
						if(L_datTEMP!= null)									
						tblBILDT.setValueAt(M_fmtLCDAT.format(L_datTEMP), i, TBL_GRNDT);
						tblBILDT.setValueAt(M_rstRSSET.getString("GR_GRNNO"),i,TBL_GRNNO);
						//tblBILDT.setValueAt(M_rstRSSET.getString("BIL_PORNO"),i,TBL_PORNO);
						tblBILDT.setValueAt(M_rstRSSET.getString("GR_MATCD"),i,TBL_MATCD);
						tblBILDT.setValueAt(M_rstRSSET.getString("GR_CHLNO"),i,TBL_CHLNO);
						strCHLQT = M_rstRSSET.getString("GR_CHLQT");
						strRECQT = M_rstRSSET.getString("GR_RECQT");
					}
					else //if(cl_dat.M_FLGOPT =='E')
					{					
						if(i==0)
						{						
							txtBILNO.setText(nvlSTRVL(M_rstRSSET.getString("BL_BILNO"),""));						
							L_datTEMP = M_rstRSSET.getDate("BL_BILDT");
							if(L_datTEMP!= null)
								txtBILDT.setText(M_fmtLCDAT.format(L_datTEMP));		
							L_TRNCD = nvlSTRVL(M_rstRSSET.getString("BL_PRTCD"),"");
							txtTRNCD.setText(L_TRNCD);						
							//vldVENCD(L_TRNCD);
							try
							{							
								M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
								M_strSQLQRY += " where PT_PRTTP = '"+strVENTP+"'";
								M_strSQLQRY += " and PT_PRTCD = '" + L_TRNCD + "'";						    	
								L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
								if(L_rstRSSET!= null)
								if(L_rstRSSET.next())
								{
									txtTRNNM.setText(L_rstRSSET.getString("PT_PRTNM"));
									setMSG("",'N');
									L_rstRSSET.close();											
								}							
								if(L_rstRSSET != null)
									L_rstRSSET.close();			
							}
							catch(Exception L_EX)
							{
								setMSG(L_EX,"vldVENCD");							
								return false;							
							}	
							
							strLOCCD = nvlSTRVL(M_rstRSSET.getString("BIL_LOCCD"),"");
							strLOCDS = getLOCDS(strLOCCD);						
							strRTPMT = nvlSTRVL(M_rstRSSET.getString("BIL_RTPMT"),"0");
							strDRTPK = nvlSTRVL(M_rstRSSET.getString("BL_DRPKG"),"0");						
							txtBILAM.setText(nvlSTRVL(M_rstRSSET.getString("BL_BILAM"),"0"));
							
							txtRTPMT.setText(strRTPMT);
							txtLOCCD.setText(strLOCCD);
							txtDRTPK.setText(strDRTPK);
							txtOTHPA.setText(nvlSTRVL(M_rstRSSET.getString("BL_OTPMT"),"0"));
							txtOTHDA.setText(nvlSTRVL(M_rstRSSET.getString("BL_OTDED"),"0"));
							txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("BL_REMDS"),""));						
							chrDEDTP = nvlSTRVL(M_rstRSSET.getString("BL_DEDTP"),"").charAt(0);											
						}
						L_datTEMP = M_rstRSSET.getDate("GR_CHLDT");
						if(L_datTEMP!= null)					
						tblBILDT.setValueAt(M_fmtLCDAT.format(L_datTEMP),i,TBL_GRNDT);
						
						tblBILDT.setValueAt(M_rstRSSET.getString("BIL_DOCRF"),i,TBL_GRNNO);
						//tblBILDT.setValueAt(M_rstRSSET.getString("BIL_PORNO"),i,TBL_PORNO);
						tblBILDT.setValueAt(M_rstRSSET.getString("BIL_MATCD"),i,TBL_MATCD);
						tblBILDT.setValueAt(M_rstRSSET.getString("GR_CHLNO"),i,TBL_CHLNO);
						strCHLQT = M_rstRSSET.getString("BIL_CHLQT");
						strRECQT = M_rstRSSET.getString("BIL_RECQT");
					}
					tblBILDT.setValueAt(strCHLQT,i,TBL_CHLQT);
					tblBILDT.setValueAt(strRECQT,i,TBL_RECQT);
					strDIFQT = setNumberFormat(Double.parseDouble(strCHLQT) - Double.parseDouble(strRECQT),3);
					L_SHRPR = setNumberFormat(((Double.parseDouble(strDIFQT)/Double.parseDouble(strCHLQT))*100),2);
					if(chrDEDTP == 'B')
					{
						L_dblDEDAM =0;
					}
					else if(chrDEDTP == 'T') // Per trip for Pooja roadways,deduction above 1% shortage
					{
						L_dblDEDAM =0;
						if(Double.parseDouble(L_SHRPR) >1.0)
						{
							L_dblDEDAM = Double.parseDouble(strDIFQT)*Double.parseDouble(strDRTPK)*1000;
							//dblTDED2 +=L_dblDEDAM;
							tblBILDT.setValueAt(setNumberFormat(L_dblDEDAM,2),i,TBL_SHRP2);
						}
					}
					else if(chrDEDTP == 'V')  // Vehicle wise
					{
						L_dblDEDAM =0;
						if(Double.parseDouble(L_SHRPR) >0.5)
						{
							L_dblDEDAM = Double.parseDouble(strDIFQT)*Double.parseDouble(strDRTPK)*1000;
							//dblTDED2 +=L_dblDEDAM;
							tblBILDT.setValueAt(setNumberFormat(L_dblDEDAM,2),i,TBL_SHRP1);
						}
						else
						{
							if(Double.parseDouble(L_SHRPR) >0.3)
							{
								L_dblDCHQT = Double.parseDouble(strCHLQT);
								L_dblDEDAM = ((L_dblDCHQT-(L_dblDCHQT*0.3/100))-Double.parseDouble(strRECQT))*Double.parseDouble(strDRTPK)*1000;
								//dblTDED1 +=L_dblDEDAM;
								tblBILDT.setValueAt(setNumberFormat(L_dblDEDAM,2),i,TBL_SHRP1);
							}
							else
								L_dblDEDAM =0;
						}
					}
					L_dblCALVL= Double.parseDouble(strCHLQT)*Double.parseDouble(nvlSTRVL(txtRTPMT.getText(),"0"))-L_dblDEDAM;
					L_dblCALAM += L_dblCALVL; 
					dblTRCQT += Double.parseDouble(strRECQT);
					dblTCHQT += Double.parseDouble(strCHLQT);		
					if(Double.parseDouble(strDIFQT)>0)
						L_dblTDFQT += Double.parseDouble(strDIFQT);
					L_CALAM = setNumberFormat(L_dblCALVL,2);
					tblBILDT.setValueAt(strDIFQT,i,TBL_DIFQT);
					tblBILDT.setValueAt(L_SHRPR,i,TBL_SHRPR);
					tblBILDT.setValueAt(L_CALAM,i,TBL_TOTAM);
					i++;
			}
			}
			L_dblTSHRT = (L_dblTDFQT/dblTCHQT)*100;
			if(chrDEDTP == 'V')
			{				
				dblTDDAM = 0;
			}
			else if(chrDEDTP == 'T')
			{				
				dblTDDAM = 0;
			}
			else
			{
				if(L_dblTSHRT >0.2)
					dblTDDAM = ((dblTCHQT-(dblTCHQT*0.2/100))-dblTRCQT)*Double.parseDouble(strDRTPK)*1000;
			}
			txtCALAM.setText(setNumberFormat(L_dblCALAM-dblTDDAM+Double.parseDouble(txtOTHPA.getText().trim())-Double.parseDouble(txtOTHDA.getText().trim()),2));	
			txtTCHQT.setText(setNumberFormat(dblTCHQT,3));
			txtTRCQT.setText(setNumberFormat(dblTRCQT,3));
			txtTSHRT.setText(setNumberFormat(L_dblTDFQT,3));
			/*if(i>0)
				btnPRN.setEnabled(true);
			else
				btnPRN.setEnabled(false);*/
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			//}
		}
		catch(Exception L_EX)
		{
			this.setCursor(cl_dat.M_curDFSTS_pbst);				
			setMSG(L_EX,"getDATA");
		}
		return true;
	}
	
	/**
	 *Method to generate the new Document Number.
	 * @param P_strBILTP String argument to pass bill Type.
	 */
	private String genDOCNO(String P_strBILTP)
	{
		String L_DOCNO  = "",  L_CODCD = "", L_CCSVL = "";		
		try
		{			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXBLP' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strBILTP + "'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = M_rstRSSET.getString("CMT_CODCD");
					L_CCSVL = M_rstRSSET.getString("CMT_CCSVL");
					M_rstRSSET.close();
				}
			}
			
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);			
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_DOCNO += "0";
			
			L_CCSVL = L_DOCNO + L_CCSVL;
			L_DOCNO = L_CODCD + L_CCSVL;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genGRNNO");
		}
		return L_DOCNO;
	}
	
	/**
	 * Method to update the last GRIN No.in the CO_CDTRN
	 * @param P_strDOCNO String argument to Pass the Document Number.
	 * @param P_strBILTP String argument to pass the Bill Type.
	 */
	private void exeDOCNO(String P_strDOCNO,String P_strBILTP)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CCSVL = '" + P_strDOCNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");					
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MMXXBLP'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strBILTP + "'";			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeDOCNO");
		}
	}		
	/**
	 * Method to get location description for given Location Code.
	 * @param LP_LOCCD String argument to pass Location Code.
	 */
	private String getLOCDS(String LP_LOCCD)
	{
		String L_LOCDS = "";		
		ResultSet M_rstRSSET1;
		try
		{
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'QC11TKL' and ";
			M_strSQLQRY += " CMT_CODCD = '" + LP_LOCCD.trim() + "'";
            	
			M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET1.next())
				L_LOCDS = M_rstRSSET1.getString("CMT_CODDS");
			
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLOCDS");
			return L_LOCDS;
		}	
		return L_LOCDS;
	}	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{				
			try
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					return true;
				if(input == txtTRNCD)
				{	
					if((txtTRNCD.getText().trim()).length()!=0)
					{
						ResultSet L_rstRSSET;
						M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
						M_strSQLQRY += " where PT_PRTTP = 'T'";
						M_strSQLQRY += " and PT_PRTCD = '" + txtTRNCD.getText().trim() + "'";
						
						L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{		
								txtTRNNM.setText(L_rstRSSET.getString("PT_PRTNM"));								
								L_rstRSSET.close();
								return true	;
							}													
						}
						else
						{
							txtTRNNM.setText("");
							setMSG("Invalid Supplier. Press F1 for help..",'E');
							if(L_rstRSSET != null)
								L_rstRSSET.close();
							return false;
						}
					}
				}					
				if(input == txtLOCCD)
				{	
					if((txtLOCCD.getText().trim()).length()!=0)
					{												
						M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
						M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
						M_strSQLQRY += " and CMT_CGSTP = 'QC11TKL' and ";
						M_strSQLQRY += " CMT_CODCD = '" + txtLOCCD.getText().trim() + "'";		            	
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
								setMSG("Invalid Location Name..",'E');									
								return false;				
							}
						}
						if(M_rstRSSET != null)
							M_rstRSSET.close();
					}
				}
				if(input == txtBILDT)
				{					
					if((txtBILDT.getText().trim()).length()!=0)
					{						
						strBILTP = String.valueOf(cmbBILTP.getSelectedItem()).trim().substring(0,2);
						M_strSQLQRY = "Select count(*) from MM_BLMST where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILNO = '"+txtBILNO.getText().trim() +"'";
						M_strSQLQRY += " AND BL_BILDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBILDT.getText().trim()))+"'";
						if(strBILTP.equals(strINCTP_fn))
							M_strSQLQRY += " and bl_prttp = 'T'";
						M_strSQLQRY += " and bl_prtcd = '"+txtTRNCD.getText().trim() +"'";
						M_strSQLQRY += " and bl_stsfl <>'X' ";						
						if(cl_dat.getRECCNT(M_strSQLQRY)>0)
						{							
							setMSG("Bill passing has been done..",'E'); 												
							return false;
						}
					}
				}										
				if((input == txtFMDAT) && ((txtFMDAT.getText().trim()).length()>0))
				{
					if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
					{			    
						setMSG("From-Date must be smaller than current Date..",'E');																
						return false;
					}				
					if((txtTODAT.getText().trim()).length()>0)
					{
						if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
						{			    
							setMSG("From-Date must be smaller than To-Date..",'E');																											
							return false;
						}
					}					
				}																						
				if((input == txtTODAT) &&((txtTODAT.getText().trim()).length()>0))
				{
					if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
					{			    
						setMSG("To-Date must be smaller than OR equal to current Date..",'E');																
						return false;
					}									
					if((txtFMDAT.getText().trim()).length()>0)
					{
						if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
						{			    
							setMSG("From-Date must be smaller than To-Date..",'E');																											
							return false;
						}						
					}					
				}					
				return true;
			}																																																														
			catch(Exception L_EX)
			{
				setMSG(L_EX,"INPVF");
				return true;
			}
		}
	}	
}
