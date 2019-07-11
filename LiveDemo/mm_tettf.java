/*
System Name   : Material Management System.
Program Name  : Tank to Tank Transfer.
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
//import javax.swing.table.DefaultTableColumnModel;
import javax.swing.InputVerifier;
import java.sql.*;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.JOptionPane;import javax.swing.JPanel;
import javax.swing.table.*;import javax.swing.JComponent;
import java.awt.event.*;import java.awt.Color;
import javax.swing.border.*;
import javax.swing.InputVerifier;
/**
* <pre>
<B>System Name :</B> Material Management System.
 
<B>Program Name :</B> Tank to Tank Transfer

<B>Purpose :</B> Transfer of Material is done from one tank to other.This module is used for Transfer 
entry .
<B>Business Logic   :</B> Transfer of Styrene for plant operations. For plant operations Material is
taken from Day Tank .When material is transferred from one Tank to the other, Initial and 
Final Dip and temprature entries are recorded.Initial and final quantity is calculated for 
source and destination Tank. During the transfer material may be used for plant operations.
Mass balancing is done to calculate the quantity issued to plant during transfer.There may also be 
some receipt to the source tank during transfer.
 
Material Transferred from Source Tank = Material Transferred to Destination Tank
Transfer quantity from Source Tank(X) = Final quantity - Initial Quantity
Transfer Qty. to Destination Tank (Y) = Final quantity - Initial Quantity
X + Receipt to source Tank = Y + Issue
 
 

List of tables used :
Table Name     Primary key                          Operation done
                                             Insert   Update   Query   Delete	
-------------------------------------------------------------------------------
MM_TFTRN       TF_DOCTP,TF_DOCNO                #        #       #       #
MM_DPTRN       DP_MEMTP,DP_MEMNO,DP_TNKNO                #
MM_TKMST       TK_TNKNO                                          #
MM_TKCTR       TKC_TNKNO,TKC_DEPCT                               #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD             #
-------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name    Column Name   Table name    Type/Size      Description
-------------------------------------------------------------------------------
cmbDOCTP      TF_DOCTP      MM_TFTRN      Varchar(2)     Document Type
txtDOCNO      TF_DOCNO      MM_TFTRN      Varchar(8)     Document No.
txtDOCDT      TF_DOCDT      MM_TFTRN      Date           Document Date
txtTN1NO      TF_TN1NO      MM_TFTRN      Varchar(10)    Source Tank
txtTN1ID      TF_TN1ID      MM_TFTRN      Decimal(6,1)   Source Initial Dip
TxtTN1IT      TF_TN1IT      MM_TFTRN      Decimal(6,2)   Source Initial Tempreture
TxtTN1IQ      TF_TN1IQ      MM_TFTRN      Decimal(16,3)  Source Initial Quantity
TxtTN1FD      TF_TN1FD      MM_TFTRN      Deciaml(6,1)   Source Final Dip
TxtTN1FT      TF_TN1FT      MM_TFTRN      Deciaml(6,2)   Source Final Tempreture
TxtTN1FQ      TF_TN1FQ      MM_TFTRN      Decimal(16,3)  Source Final Quantity
TxtTN1NQ      Calculated                                 (txtTN1FQ -txtTN1IQ)
TxtMATCD      TF_MATCD      MM_TFTRN      Varchar(10)    Material Code
TxtMATDS      CT_MATDS      CO_CTMST      Varchar(45)    Material Description
TxtUOMCD      CT_UOMCD      CO_CTMST      Varchar(3)     Unit of Measurement
txtTN2NO      TF_TN2NO      MM_TFTRN      Varchar(10)    Target Tank
txtTN2ID      TF_TN2ID      MM_TFTRN      Decimal(6,1)   Target Initial Dip
TxtTN2IT      TF_TN2IT      MM_TFTRN      Decimal(6,2)   Target Initial Tempreture
TxtTN2IQ      TF_TN2IQ      MM_TFTRN      Decimal(16,3)  Target Initial Quantity
TxtTN2FD      TF_TN2FD      MM_TFTRN      Deciaml(6,1)   Target Final Dip
TxtTN2FT      TF_TN2FT      MM_TFTRN      Deciaml(6,2)   Target Final Tempreture
TxtTN2FQ      TF_TN2FQ      MM_TFTRN      Decimal(16,3)  Target Final Quantity
TxtTN2NQ      Calculated                                 (txtTN2FQ -txtTN2IQ)
TxtMF_QT      TF_MF_QT      MM_TFTRN      Decimal(16,3)  MF Quantity
TxtDIFQT      Calculated
-------------------------------------------------------------------------------

<B>List of fields with help facility : </B>
Field Name  Display Description         Display Columns        Table Name
-------------------------------------------------------------------------------
txtDOCNO    Transfer No,Date,           TF_DOCNO,TF_DOCDT,     MM_TFTRN
            Source Tank,Target Tank     TF_TN1NO,TF_TN2NO
TxtTN1NO    Tank No, Material Code,     TK_TNKNO,TK_MATCD,     MM_TKMST
            Material Description        TK_MATDS
TxtTN2NO    Tank No, Material Code,     TK_TNKNO,TK_MATCD,     MM_TKMST
            Material Description        TK_MATDS
-------------------------------------------------------------------------------
<B>Queries:</b>
<I><B>To insert New Record :</B>
       i) To insert Data in the above table, Document number is generated from Table CO_CDTRN
'D"+cl_dat.M_strCMPCD_pbst+"' where CMT_CGSTP = 'MMXXTTF' and CMT_CODCD = cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP 
       ii) Data is inserted in the Table, MM_TFTRN
 for fields TF_DOCTP, TF_DOCNO, TF_DOCDT, TF_MATCD, TF_TN1NO, TF_TN1ID, TF_TN1IT, TF_TN1IQ,
 TF_TN1FD, TF_TN1FT, TF_TN1FQ, TF_TN2NO, TF_TN2ID, TF_TN2IT, TF_TN2IQ, TF_TN2FD, TF_TN2FT,
 TF_TN2FQ,TF_TN1AQ,TF_TN2AQ,TF_TRFQT
	   iii)Then In the CO_CDTRN table,CMT_CCSVL (Doc No.) is updated for The Record having
	CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXTTF' and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP + "'";			
	<B>To Update Records : </B>
		iv) If this updation is Successful, MM_DPTRN Table Records are Updated. Only Transfer Quantity 
is updated. if it is blank then zero is updated. for matching DP_MEMTP, DP_MEMDT & DP_TNKNO
       i) First Table (MM_TFTRN) Data is updated .
Except Document Number & Document Type all fields in this table are updateable & by matching 
these two Field (TF_DOCTP, TF_DOCNO) Data is Updated.  
       ii) If this updation is Successful, MM_DPTRN Table Records are Updated. Only Transfer Quantity 
is updated. if it is blank then zero is updated. for matching DP_MEMTP, DP_MEMDT & DP_TNKNO
  
<B>To Delete Records : </B>      
       i) Record from MM_TFTRN Table are marked as 'X' for the Effect of delettion. only those 
records having corresponding TF_DOCTP(Doc.Type) and TF_DOCNO( Doc.Number) 
</I>

<B>Validations :</B>
	- Transfer Type is tanken from CO_CDTRN (SYS/MMXXTTF). Following transfer type are valid
		11 - Exbonded to Exbonded
		12 - Exbonded to Bonded
		13 - Exbonded to Day
		21 - Bonded to Exbonded
	- Transfer No. should exists in Tank to Tank Transfer table(except Addition mode)	
	- Transfer No. is eight digit code with first digit is Financial Year,next two 
         digits are Transfer Type and remaining five digits are serial number within 
         that Transfer Type.
	- Source tank & Target tank are selected on the basis of Transfer Type.
	- Transfer quantity in Dip register is updated as follows
         Negation of Source net qunatity is stored against Source Tank
         Target net quantity is stored against Target Tank	
    - Transaction is not allowed if Issue is updated for the transfer Date.
    */

class mm_tettf extends cl_pbase
{									/** JComboBox to Show & Select any one available Transfer Type.*/
	private JComboBox cmbDOCTP;		/** JTextField to display & accept Transfer Number.*/
	private JTextField txtDOCNO;	/** JTextField to display & accept Transfer Date.*/
	private JTextField txtDOCDT;	/** JPanel to display Source Tank Details.*/
	private	JPanel pnlSOURCE;		/** Jpanel to display Target Tank Details.*/
	private	JPanel pnlTARGET;		/** JTextField to display Material Code.*/
	private	JTextField txtMATCD;	/** JTextField to display Material Description.*/
	private	JTextField txtMATDS;	/** JTextField to display Unit of Measurement.*/
	private	JTextField txtUOMCD;	/** JTextField to display & accept Source Tank Number.*/
	private	JTextField txtTN1NO;	/** JTextField to display & accept Source Tank Initial Dip.*/
	private	JTextField txtTN1ID;	/** JTextField to display & accept Source Tank Initial Temprature.*/
	private	JTextField txtTN1IT;	/** JTextField to display Source Tank Initial Quantity.*/
	private	JTextField txtTN1IQ;	/** JTextField to display & accept Source Tank Final Dip.*/
	private	JTextField txtTN1FD;	/** JTextField to display & accept Source Tank Final Temprature.*/	
	private	JTextField txtTN1FT;	/** JTextField to display Source Tank Final Quantity.*/
	private	JTextField txtTN1FQ;	/** JTextField to display Source Tank Net Quantity.*/
	private	JTextField txtTN1NQ;	/** JTextField to display Source Tank Transfer Quantity.*/
	private	JTextField txtTR1QT;	/** JTextField to display & accept Reciept Quantity.*/
	private	JTextField txtRCTQT;	/** JTextField to display & accept Target Tank Number.*/
	private	JTextField txtTN2NO;	/** JTextField to display & accept Target Tank Initial Dip.*/
	private	JTextField txtTN2ID;	/** JTextField to display & accept Target Tank Initial Temprature.*/
	private	JTextField txtTN2IT;	/** JTextField to display & accept Target Tank Initial Quantity.*/
	private	JTextField txtTN2IQ;	/** JTextField to display & accept Target Tank Final Dip.*/
	private	JTextField txtTN2FD;	/** JTextField to display & accept Target Tank Final Temprature.*/
	private	JTextField txtTN2FT;	/** JTextField to display Target Tank Final Quantity.*/
	private	JTextField txtTN2FQ;	/** JTextField to display Target Tank Net Quantity.*/
	private	JTextField txtTN2NQ;	/** JTextField to display Target Tank Transfer Quantity.*/
	private	JTextField txtTR2QT;	/** JTextField to display & accept Issue Quantity*/
	private	JTextField txtISSQT;	/** String variable for Document Type.*/	
	private String strDOCTP;		/** String variable for Document Number.*/
	private String strDOCNO;		/** String variable for document Type.*/
	private String strDOCDT;		/** String variable for Material Code.*/
	private String strMATCD;		/** String variable for Unit Of Measurement.*/
	private String strUOMCD;		/** String variable for Transfer Quantity.*/	
	private String strTRAQT;		/** String variable for Source Tank Number*/
	private String strTN1NO;		/** String variable for Source Tank Initial Dip.*/
	private String strTN1ID;		/** String variable for Source Tank Initial Temprature.*/	
	private String strTN1IT;		/** String variable for Source Tank Initail Quantity.*/
	private String strTN1IQ;		/** String variable for Source Tank Final Dip.*/
	private String strTN1FD;		/** String variable for Source Tank Final Temprature.*/	
	private String strTN1FT;		/** String variable for Source Tank Final Quantity.*/	
	private String strTN1FQ;		/** String variable for Source Tank Net Quantity.*/
	private String strTN1NQ;		/** String variable for Source Tank Actual Quantity.*/
	private String strTN1AQ;		/** String variable for Source Tank Transfer Quantity.*/
	private String strTR1QT;		/** String variable for Target Tank Number.*/
	private String strTN2NO;		/** String variable for Target Tank Initial Dip.*/
	private String strTN2ID;		/** String variable for Target Tank Initial Temprature.*/
	private String strTN2IT;		/** String variable for Target Tank Quantity.*/
	private String strTN2IQ;		/** String variable for Target Tank Final Dip.*/
	private String strTN2FD;		/** String variable for Target Tank Final Temprature.*/
	private String strTN2FT;		/** String variable for Target Tank Final Quantity.*/
	private String strTN2FQ;		/** String variable for Target Tank Net Quantity.*/
	private String strTN2NQ;		/** String variable for Target Tank Actual Quantity.*/
	private String strTN2AQ;		/** String variable for Target Tank Transfer Quantity.*/
	private String strTR2QT;		/** String variable for Source Tank Transfer Code.*/
	private String strFRTTP;		/** String variable for Target Tank Transfer Code.*/	
	private String strTOTTP;		/** String variable for Help Field.*/
	private String M_strHLPFLD;		/** Result Set Object for Data Base Manipulation.*/
	private ResultSet M_rstRSSET1;	/** String variable for Regular Dip.*/
	final String strREGDP = "81";	/** String Variable for maintaing old Transfer Qty in TAnk1 */
	String strOTR1QT ="";           /** String Variable for maintaing old Transfer Qty in Tank2 */
	String strOTR2QT ="";	
	mm_tettf()
	{	
		super(2);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMatrix(20,8);
			add(new JLabel("Transfer Type"),2,1,1,.9,this,'R');
			add(cmbDOCTP = new JComboBox(),2,3,1,2,this,'R');
			add(new JLabel("Transfer No."),2,4,1,.8,this,'R');
			add(txtDOCNO = new TxtLimit(8),2,5,1,1,this,'R');						
			add(new JLabel("Transfer Date"),2,6,1,.9,this,'R');
			add(txtDOCDT = new TxtDate(),2,7,1,1,this,'L');
		
			add(new JLabel("Dip"),4,5,1,.6,this,'R');
			add(new JLabel("Temprature"),4,6,1,.9,this,'R');
			add(new JLabel("Quantity"),4,7,1,.8,this,'R');

			pnlSOURCE = new JPanel();
			pnlSOURCE.setLayout(null);
			pnlSOURCE.setBorder(new TitledBorder(new LineBorder(Color.darkGray),"Source"));					

			add(new JLabel("Tank No."),1,1,1,.7,pnlSOURCE,'R');
			add(txtTN1NO = new TxtLimit(10),1,2,1,1,pnlSOURCE,'L');
			add(new JLabel("Initial"),1,3,1,.6,pnlSOURCE,'R');
			add(txtTN1ID = new TxtNumLimit(6.1),1,4,1,1,pnlSOURCE,'R');									
			add(txtTN1IT = new TxtNumLimit(6.2),1,5,1,1,pnlSOURCE,'R');
			add(txtTN1IQ = new TxtNumLimit(6.3),1,6,1,1,pnlSOURCE,'R');

			add(new JLabel("Final"),2,3,1,.6,pnlSOURCE,'R');
			add(txtTN1FD = new TxtNumLimit(6.1),2,4,1,1,pnlSOURCE,'R');
			add(txtTN1FT = new TxtNumLimit(6.2),2,5,1,1,pnlSOURCE,'R');
			add(txtTN1FQ = new TxtNumLimit(6.3),2,6,1,1,pnlSOURCE,'R');

			add(new JLabel("Net Qty."),3,1,1,.7,pnlSOURCE,'R');
			add(txtTN1NQ = new TxtLimit(12),3,2,1,1,pnlSOURCE,'L');
			add(new JLabel("Reciept"),3,3,1,.6,pnlSOURCE,'R');
			add(txtRCTQT = new TxtNumLimit(6.3),3,4,1,1,pnlSOURCE,'R');									
			add(new JLabel("Transfer Qty."),3,5,1,1,pnlSOURCE,'R');
			add(txtTR1QT = new TxtNumLimit(6.3),3,6,1,1,pnlSOURCE,'R');
			add(pnlSOURCE,5,2,4.4,6.2,this,'L');

			add(new JLabel("Material"),10,2,1,.7,this,'R');
			add(txtMATCD = new TxtLimit(10),10,3,1,1,this,'L');			
			add(txtMATDS = new JTextField(30),10,4,1,3,this,'L');
			add(txtUOMCD = new JTextField(5),10,7,1,.9,this,'R');
			
			pnlTARGET = new JPanel();
			pnlTARGET.setLayout(null);
			pnlTARGET.setBorder(new TitledBorder(new LineBorder(Color.darkGray),"Target"));			
	
			add(new JLabel("Tank No."),1,1,1,.7,pnlTARGET,'R');
			add(txtTN2NO = new TxtLimit(10),1,2,1,1,pnlTARGET,'L');
			add(new JLabel("Initial"),1,3,1,.8,pnlTARGET,'R');
			add(txtTN2ID = new TxtNumLimit(6.1),1,4,1,1,pnlTARGET,'R');									
			add(txtTN2IT = new TxtNumLimit(6.2),1,5,1,1,pnlTARGET,'R');
			add(txtTN2IQ = new TxtNumLimit(6.3),1,6,1,1,pnlTARGET,'R');
		
			add(new JLabel("Final"),2,3,1,.8,pnlTARGET,'R');
			add(txtTN2FD = new TxtNumLimit(6.1),2,4,1,1,pnlTARGET,'R');
			add(txtTN2FT = new TxtNumLimit(6.2),2,5,1,1,pnlTARGET,'R');
			add(txtTN2FQ = new TxtNumLimit(6.3),2,6,1,1,pnlTARGET,'R');

			add(new JLabel("Net Qty."),3,1,1,.7,pnlTARGET,'R');
			add(txtTN2NQ = new TxtLimit(12),3,2,1,1,pnlTARGET,'L');
			add(new JLabel("Issue Qty."),3,3,1,.8,pnlTARGET,'R');
			add(txtISSQT = new TxtNumLimit(6.3),3,4,1,1,pnlTARGET,'R');									
			add(new JLabel("Transfer Qty."),3,5,1,1,pnlTARGET,'R');
			add(txtTR2QT = new TxtNumLimit(6.3),3,6,1,1,pnlTARGET,'R');
	
			add(pnlTARGET,11,2,4.4,6.2,this,'L');					
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXTTF' order by CMT_CODCD";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);						
			if(M_rstRSSET!= null)
			{
				while(M_rstRSSET.next())
				{
					cmbDOCTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			
			}									
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;									
			strFRTTP = cmbDOCTP.getSelectedItem().toString().substring(0,1);
			setENBL(false);
			this.setCursor(cl_dat.M_curDFSTS_pbst);			
		}
		catch(Exception L_Ex)
		{
			setMSG(L_Ex,"Constructor");
		}
	}	

	/**
	 * Super class method override to inhance its functionality according to Requriment.
	 */
	void setENBL(boolean P_flgSTAT)
	{		
		super.setENBL(P_flgSTAT);
		txtDOCDT.setEnabled(false);		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
		{		
		cmbDOCTP.setEnabled(true);
		txtDOCNO.setEnabled(true);
		}		
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{								
			try
			{				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);
					txtDOCDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
					txtDOCNO.setEnabled(false);
					txtDOCDT.setEnabled(true);
					cmbDOCTP.requestFocus();
					setMSG("Select Transfer Type to add new Record..",'N');
				}
				else
				{	
					txtDOCDT.setEnabled (false);
					txtDOCNO.requestFocus();
					setMSG("Enter Transfer Number for " + cl_dat.M_cmbOPTN_pbst.getSelectedItem()+"..",'N');
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"setENBL");
			}			
			txtTN1NO.setEnabled(true);
			txtTN1ID.setEnabled(true);
			txtTN1IT.setEnabled(true);
			txtTN1FD.setEnabled(true);
			txtTN1FT.setEnabled(true);
			txtRCTQT.setEnabled(true);
			txtTR1QT.setEnabled(true);			
			txtTN2NO.setEnabled(true);
			txtTN2ID.setEnabled(true);
			txtTN2IT.setEnabled(true);
			txtTN2FD.setEnabled(true);
			txtTN2FT.setEnabled(true);
			txtISSQT.setEnabled(true);	
			txtTR2QT.setEnabled(true);					
		}
	}	
	/** 
	 * Method to Clear all the fields.
	 */
	void clrCOMP()
	{	
		//if((cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0) ||(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==1))
		txtDOCNO.setText("");
		txtDOCDT.setText("");
		txtTN1NO.setText("");
		txtTN1ID.setText("");
		txtTN1IT.setText("");		
		txtTN1IQ.setText("");
		txtTN1FD.setText("");
		txtTN1FT.setText("");
		txtTN1FQ.setText("");
		txtTN1NQ.setText("");
		txtTN2NO.setText("");
		txtTN2ID.setText("");
		txtTN2IT.setText("");
		txtTN2IQ.setText("");
		txtTN2FD.setText("");
		txtTN2FT.setText("");
		txtTN2FQ.setText("");
		txtTN2NQ.setText("");
		txtMATCD.setText("");		
		txtMATDS.setText("");
		txtTR1QT.setText("");		
		txtTR2QT.setText("");
		txtRCTQT.setText("");
		txtISSQT.setText("");
		txtUOMCD.setText("");
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				setENBL(false);
				clrCOMP();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)
				{					
					cmbDOCTP.requestFocus();
					setMSG("Select Transfer Type to add new Record..",'N');
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						try
						{
							M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
							M_calLOCAL.add(java.util.Calendar.DATE,-1);
							txtDOCDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));					
							setMSG("Select Transfer Type..",'N');
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"actionPerformed");
						}
					}
					else// for MOD,DEL & ENQ
					{
						txtDOCNO.setEnabled(true);						
						txtDOCNO.requestFocus();
						setMSG("Enter Transfer Number for " + cl_dat.M_cmbOPTN_pbst.getSelectedItem()+"..",'N');
					}
				}				
				else
				{
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					setMSG("Select an Option..",'N');
				}
			}	
			else if (M_objSOURC == cmbDOCTP)
			{
				clrCOMP();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtTN1NO.setEnabled(true);
						txtTN2NO.setEnabled(true);
						//txtTN1NO.requestFocus();
						txtDOCDT.requestFocus();
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
						txtDOCDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
					}
					else
					{
						txtDOCNO.requestFocus();
					}
				}					
			}
			else if(M_objSOURC == txtDOCNO)				// Tank Transfer No.
			{	
				if((txtDOCNO.getText().trim()).length()<8)
				{
					setMSG("Invalid Transfer Number, Press F1 for Help..",'E');
					txtDOCNO.requestFocus();
					return;
				}				
				this.setCursor(cl_dat.M_curWTSTS_pbst);				
				strDOCTP = cmbDOCTP.getSelectedItem().toString().substring(0,2);
				strDOCNO = txtDOCNO.getText().trim();				
				try
				{
					M_strSQLQRY = "Select TF_DOCNO,TF_TN1NO from MM_TFTRN";
					M_strSQLQRY += " where TF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TF_DOCTP = '" + strDOCTP + "'";
					M_strSQLQRY += " and TF_DOCNO = '" + strDOCNO + "'";
					M_strSQLQRY += " and isnull(TF_STSFL,'') <> 'X'" ;					    
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);					
					if (M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							M_rstRSSET.close();							
						}
						else
						{
							clrCOMP();							
							setMSG("Invalid Transfer No., Press F1 for help..",'E');							
							this.setCursor(cl_dat.M_curDFSTS_pbst);	
							txtDOCNO.requestFocus();
							return;
						}
					}					
					else
					{
						clrCOMP();
						setMSG("Invalid Transfer No. Press F1 for help..",'E');						
						this.setCursor(cl_dat.M_curDFSTS_pbst);	
						txtDOCNO.requestFocus();
						return;															
					}
					if(getALLREC(strDOCTP,strDOCNO))
					{					
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						{
							cmbDOCTP.setEnabled(false);	
						}	
					}					
					else
					{
						clrCOMP();
						setMSG("Invalid Transfer Number, Try again..",'E');											
					}
						
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"actionPerformed");
				}								
			}
			else if(M_objSOURC ==txtDOCDT) // Source Tank No. 
			{
				txtTN1NO.requestFocus();
			}
			else if(M_objSOURC ==txtTN1NO) // Source Tank No. 
			{
				strTN1NO = txtTN1NO.getText().trim();
				strTN1NO = strTN1NO.toUpperCase();
				txtTN1NO.setText(strTN1NO);
				strFRTTP = cmbDOCTP.getSelectedItem().toString().substring(0,1);
				if(vldTNKNO(strFRTTP,strTN1NO,""))
				{
					txtMATCD.setText(strMATCD);
					getMATDS(strMATCD);
					txtTN1ID.requestFocus();
				}
				else
					txtTN1NO.requestFocus();
			}
			else if(M_objSOURC ==txtTN2NO)	// Target Tank No.
			{
				strTN2NO = txtTN2NO.getText().trim();
				strTN2NO = strTN2NO.toUpperCase();
				txtTN2NO.setText(strTN2NO);
				strMATCD = txtMATCD.getText().trim();
				strTOTTP = cmbDOCTP.getSelectedItem().toString().substring(1,2);
				if(vldTNKNO(strTOTTP,strTN2NO,strMATCD))
					txtTN2ID.requestFocus();
				else
					txtTN2NO.requestFocus();
			}
			else if(M_objSOURC ==txtTN1IT)		// Source Initial Temp
			{
				strTN1NO = txtTN1NO.getText().trim();
				strTN1ID = txtTN1ID.getText().trim();
				strTN1IT = txtTN1IT.getText().trim();
				strMATCD = txtMATCD.getText().trim();
				strTN1IQ = calMATQT(strTN1NO,strTN1ID,strTN1IT,strMATCD);
				txtTN1IQ.setText(strTN1IQ);				
			}
			else if(M_objSOURC ==txtTN2IT)		// Target Initial Temp
			{
				strTN2NO= txtTN2NO.getText().trim();
				strTN2ID= txtTN2ID.getText().trim();
				strTN2IT= txtTN2IT.getText().trim();
				strMATCD = txtMATCD.getText().trim();
				strTN2IQ = calMATQT(strTN2NO,strTN2ID,strTN2IT,strMATCD);
				txtTN2IQ.setText(strTN2IQ);				
			}
			else if(M_objSOURC ==txtTN1FT)		// Source Final Temp
			{
				strTN1NO = txtTN1NO.getText().trim();
				strTN1FD = txtTN1FD.getText().trim();
				strTN1FT = txtTN1FT.getText().trim();
				strMATCD = txtMATCD.getText().trim();
				strTN1FQ = calMATQT(strTN1NO,strTN1FD,strTN1FT,strMATCD);
				txtTN1FQ.setText(strTN1FQ);
				txtTN1NQ.setText(setNumberFormat(Float.valueOf(strTN1FQ).floatValue() - Float.valueOf(strTN1IQ).floatValue(),3));
				strTN1NQ = txtTN1NQ.getText().trim();
				txtRCTQT.setText("0");				
			}
			else if(M_objSOURC ==txtTN2FT)		// Target Final Temp
			{
				strTN2NO = txtTN2NO.getText().trim();
				strTN2FD = txtTN2FD.getText().trim();
				strTN2FT = txtTN2FT.getText().trim();
				strMATCD = txtMATCD.getText().trim();
				strTN2FQ = calMATQT(strTN2NO,strTN2FD,strTN2FT,strMATCD);
				txtTN2FQ.setText(strTN2FQ);
				txtTN2NQ.setText(setNumberFormat(Float.valueOf(strTN2FQ).floatValue() - Float.valueOf(strTN2IQ).floatValue(),3));
				strTN2NQ = txtTN2NQ.getText().trim();					
				txtISSQT.setText(setNumberFormat((Math.abs(Float.valueOf(txtTR1QT.getText().trim()).floatValue()) - Float.valueOf(strTN2NQ).floatValue()),3));
			}						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			setMSG("",'N');			
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{	
				if(M_objSOURC ==txtDOCNO)				// Tank Transfer No.
				{
					strDOCTP = cmbDOCTP.getSelectedItem().toString().substring(0,2);
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDOCNO";
					String L_ARRHDR[] = {"Transfer No","Date","Source Tank","Target Tank"};
					M_strSQLQRY = "Select TF_DOCNO,TF_DOCDT,TF_TN1NO,TF_TN2NO from MM_TFTRN";
					M_strSQLQRY +=" where TF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TF_DOCTP = '" + strDOCTP + "'" ;
					if((txtDOCNO.getText().trim()).length()>0) 
						M_strSQLQRY +=" and TF_DOCNO like '" + txtDOCNO.getText().trim()+"%'" ;
					M_strSQLQRY += " and TF_STSFL <> 'X' order by TF_DOCDT desc" ;
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				}
				// Source Tank No. OR Target Tank.
				else if((M_objSOURC == txtTN1NO) || (M_objSOURC ==txtTN2NO))
				{					
					String P_strTNKTP="";
					try
					{
						if(M_objSOURC == txtTN1NO)
						{ 
							strFRTTP = cmbDOCTP.getSelectedItem().toString().substring(0,1);
							M_strHLPFLD = "txtTN1NO";
							P_strTNKTP = "0" + strFRTTP;							
						}
						else 
						{
							if((txtTN1NO.getText().trim()).length()==0)
							{
								setMSG("Enter Source Tank No. first..",'E');
								return;
							}
							strTOTTP = cmbDOCTP.getSelectedItem().toString().substring(1,2);
							strMATCD = txtMATCD.getText().trim();
							M_strHLPFLD = "txtTN2NO";
							P_strTNKTP = "0" + strTOTTP;							
						}						
						strDOCTP = cmbDOCTP.getSelectedItem().toString().substring(0,2);
						cl_dat.M_flgHELPFL_pbst = true;
						String L_ARRHDR[] = {"Tank No","Material Code","Material description"};						
						
						M_strSQLQRY = "Select TK_TNKNO,TK_MATCD,TK_MATDS from MM_TKMST";
						M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_STSFL <> 'X'" ;
						M_strSQLQRY += " and TK_TNKTP = '" + P_strTNKTP + "'" ;
						M_strSQLQRY += " and TK_MATCD in (Select TK_MATCD from MM_TKMST where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"'" ;
						M_strSQLQRY += " group by TK_MATCD having count(TK_MATCD) > 1)" ;						
						if(M_strHLPFLD.equals("txtTN2NO"))
						{	
							M_strSQLQRY += " and TK_MATCD = '" + strMATCD + "'" ;
							M_strSQLQRY += " and TK_TNKNO <> '" + txtTN1NO.getText().trim() + "'" ;
						}						
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");						
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"VK_F1");
					}					
				}
			}				
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{									
				if (M_objSOURC ==cmbDOCTP)		// Tank Transfer Type
				{
					strFRTTP = cmbDOCTP.getSelectedItem().toString().substring(0,1);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 1)//for add setfocus at doc type.
					{						
						//txtTN1NO.requestFocus();
						cmbDOCTP.requestFocus();
						setMSG("Enter Tank Number OR Press F1 for Help..",'N');					
					}																	
					else
					{
						cl_dat.M_cmbOPTN_pbst.requestFocus();
						setMSG("Select an Option..",'N');
					}
				}				
				if(M_objSOURC == txtDOCNO)				// Tank Transfer No.
				{						
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						setENBL(false);
						txtTN1NO.requestFocus();
						setMSG("",'N');
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{						
						setENBL(false);
						cl_dat.M_btnSAVE_pbst.requestFocus();
						setMSG("Press Delete button to delete Record..",'N');
					}							
				}				
				else if(M_objSOURC ==txtTN1NO)				// Source Tank No.
				{					
					if((txtTN1NO.getText().trim()).length() == 0)
					{
						txtTN1NO.requestFocus();
						setMSG("Enter Source Tank Number OR Press F1 Key for Help..",'E');
					}
					else
					{						
						setMSG("Enter Source Tank Initial Dip & press Enter Key..",'N');				
					}
				}				
				else if(M_objSOURC ==txtTN1ID)		// Source Initial Dip
				{
					if((txtTN1ID.getText().trim()).length() == 0)
					{
						txtTN1ID.requestFocus();
						setMSG("Enter Source Tank Initial Dip & press Enter Key..",'E');
					}
					else
					{
						txtTR1QT.setText("");
						txtTR2QT.setText("");
						txtTN1IT.requestFocus();
						setMSG("Enter Souce Tank Initial Temprature & Press Enter key..",'N');
					}
				}
				else if(M_objSOURC ==txtTN1IT)		// Source Initial Temp
				{	
					if((txtTN1IT.getText().trim()).length() == 0)
					{
						txtTN1IT.requestFocus();
						setMSG("Enter Souce Tank Initial Temprature & Press Enter key..",'E');
					}
					else
					{
						txtTR1QT.setText("");
						txtTR2QT.setText("");
						txtTN1FD.requestFocus();
						setMSG("Enter Souce Tank Final Dip & press Enter Key..",'N');
					}
				}
				else if(M_objSOURC ==txtTN1FD)		// Source Final Dip
				{
					if((txtTN1FD.getText().trim()).length() == 0)
					{
						txtTN1FD.requestFocus();
						setMSG("Enter Souce Tank Final Dip & press Enter Key..",'E');
					}					
					else
					{
						txtTR1QT.setText("");
						txtTR2QT.setText("");
						txtTN1FT.requestFocus();
						setMSG("Enter Souce Tank Final Temprature & Press Enter Key..",'N');
					}
				}
				else if(M_objSOURC ==txtTN1FT)		// Source Final Temp
				{				
					if((txtTN1FT.getText().trim()).length() == 0)
					{
						txtTN1FT.requestFocus();
						setMSG("Enter Souce Tank Final Temprature & Press Enter Key..",'E');
					}
					else
					{
						txtTR1QT.setText("");
						txtTR2QT.setText("");
						txtRCTQT.requestFocus();										
						setMSG("Enter Reciept Quantity & Press Enter Key..",'N');
					}
				}								
				else if(M_objSOURC ==txtTN2NO)	// Target Tank No.
				{					
					if((txtTN2NO.getText().trim()).length() == 0)
					{
						txtTN2NO.requestFocus();				
						setMSG("Enter Target Tank Number Or Press F1 Key for Help..",'E');
					}
					else
					{
						txtTN2ID.requestFocus();
						setMSG("Enter Target Tank Initial Dip & press Enter Key..",'N');
					}
				}
				else if(M_objSOURC ==txtTN2ID)		// Target Initial Dip
				{
					if((txtTN2ID.getText().trim()).length() == 0)
					{
						txtTN2ID.requestFocus();
						setMSG("Enter Target Tank Initial Dip & press Enter Key..",'E');
					}
					else
					{
						txtTR2QT.setText("");
						txtTN2IT.requestFocus();
						setMSG("Enter Target Tank Initial Temprature & press Enter Key..",'N');
					}
				}
				else if(M_objSOURC ==txtTN2IT)		// Target Initial Temp
				{					
					if((txtTN2IT.getText().trim()).length() == 0)
					{
						txtTN2IT.requestFocus();
						setMSG("Enter Target Tank Initial Temprature & press Enter Key..",'E');
					}
					else
					{
						txtTR2QT.setText("");
						txtTN2FD.requestFocus();
						setMSG("Enter Target Tank Final Dip..",'N');
					}
				}
				else if(M_objSOURC ==txtTN2FD)		// Target Final Dip
				{
					if((txtTN2FD.getText().trim()).length() == 0)
					{
						txtTN2FD.requestFocus();
						setMSG("Enter Target Tank Final Dip & press Enter Key..",'E');
					}
					else
					{
						txtTR2QT.setText("");
						txtTN2FT.requestFocus();
						setMSG("Enter Target Tank Initial Temprature & Press Enter Key..",'N');
					}
				}
				else if(M_objSOURC ==txtTN2FT)		// Target Final Temp
				{					
					if((txtTN2FT.getText().trim()).length() == 0)
					{
						txtTN2FT.requestFocus();
						setMSG("Enter Target Tank Initial Temprature & Press Enter Key..",'E');
					}
					else
					{
						txtTR2QT.setText("");
						txtISSQT.requestFocus();
						setMSG("Enter Target Tank Issue Quantity & press Enter Key..",'N');
					}
				}				
				else if(M_objSOURC ==txtISSQT)
				{
					txtTR2QT.setText(setNumberFormat((Float.valueOf(strTN2NQ).floatValue()) + (Float.valueOf(txtISSQT.getText().trim()).floatValue()),3));
				}
				else if(M_objSOURC ==txtRCTQT)
				{
					if((txtTN1FD.getText().trim()).length() == 0)
					{
						txtRCTQT.requestFocus();										
						setMSG("Enter Reciept Quantity & Press Enter Key..",'E');
					}
					else
					{
						txtTR1QT.setText(setNumberFormat(Float.valueOf(strTN1NQ).floatValue() + Float.valueOf(txtRCTQT.getText().trim()).floatValue(),3));
						strTRAQT = setNumberFormat(Math.abs(Float.valueOf(txtTR1QT.getText().trim()).floatValue()),3);
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							strTN2NQ = txtTN2NQ.getText().trim();
							txtISSQT.setText(setNumberFormat((Math.abs(Float.valueOf(txtTR1QT.getText().trim()).floatValue())) - Float.valueOf(strTN2NQ).floatValue(),3));
							txtTR2QT.setText(setNumberFormat(Float.valueOf(strTN2NQ).floatValue() + Float.valueOf(txtISSQT.getText().trim()).floatValue(),3));	
						}
						txtTN2NO.requestFocus();
					}
				}
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
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
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if (!vldDATA())
				return;	
			cl_dat.M_flgLCUPD_pbst = true;
			strDOCTP = cmbDOCTP.getSelectedItem().toString().substring(0,2);			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{ 
				String L_strTTFNO  = "",  L_strCODCD = "", L_strCCSVL = "";			
				M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXTTF' and ";
				M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP + "'";			
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						L_strCODCD = M_rstRSSET.getString("CMT_CODCD");
						L_strCCSVL = M_rstRSSET.getString("CMT_CCSVL");
						M_rstRSSET.close();
					}
				}				
				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);			
				for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
					L_strTTFNO += "0";			
				L_strCCSVL = L_strTTFNO + L_strCCSVL;
				L_strTTFNO = L_strCODCD + L_strCCSVL;
				txtDOCNO.setText(L_strTTFNO);
			}			
			strDOCNO = txtDOCNO.getText().trim();
			strDOCDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOCDT.getText().trim()));			
			//strTN2NO = "";			
			strTN1NO = txtTN1NO.getText().trim();
			strTN1ID = txtTN1ID.getText().trim();
			strTN1IT = txtTN1IT.getText().trim(); 
			strTN1IQ = txtTN1IQ.getText().trim(); 
			strTN1FD = txtTN1FD.getText().trim(); 
			strTN1FT = txtTN1FT.getText().trim(); 
			strTN1FQ = txtTN1FQ.getText().trim();
			strTN1NQ = txtTN1NQ.getText().trim(); 

			strTN2NO = txtTN2NO.getText().trim();
			strTN2ID = nvlSTRVL(txtTN2ID.getText().trim(),"0");
			strTN2IT = nvlSTRVL(txtTN2IT.getText().trim(),"0");
			strTN2IQ = nvlSTRVL(txtTN2IQ.getText().trim(),"0");
			strTN2FD = nvlSTRVL(txtTN2FD.getText().trim(),"0");
			strTN2FT = nvlSTRVL(txtTN2FT.getText().trim(),"0");
			strTN2FQ = nvlSTRVL(txtTN2FQ.getText().trim(),"0");
			strTN2NQ = nvlSTRVL(txtTN2NQ.getText().trim(),"0");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))					// Addition
			{
				M_strSQLQRY = "Insert into MM_TFTRN(TF_CMPCD,TF_DOCTP,TF_DOCNO,TF_DOCDT,TF_MATCD,";
				M_strSQLQRY += "TF_TN1NO,TF_TN1ID,TF_TN1IT,TF_TN1IQ,TF_TN1FD,TF_TN1FT,";
				M_strSQLQRY += "TF_TN1FQ,TF_TN2NO,TF_TN2ID,TF_TN2IT,TF_TN2IQ,TF_TN2FD,TF_TN2FT,";
				M_strSQLQRY += "TF_TN2FQ,TF_TN1AQ,TF_TN2AQ,TF_TRFQT,TF_TRNFL,TF_STSFL,TF_LUSBY,TF_LUPDT)values(";				
				M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
				M_strSQLQRY += "'" + strDOCTP + "',";
				M_strSQLQRY += "'" + strDOCNO + "','";
				M_strSQLQRY += strDOCDT + "',";
				M_strSQLQRY += "'" + strMATCD + "',";				
				M_strSQLQRY += "'" + strTN1NO + "',";
				M_strSQLQRY += strTN1ID + ",";
				M_strSQLQRY += strTN1IT + ",";
				M_strSQLQRY += strTN1IQ + ",";
				M_strSQLQRY += strTN1FD + ",";
				M_strSQLQRY += strTN1FT + ",";
				M_strSQLQRY += strTN1FQ + ",";
				M_strSQLQRY += "'" + strTN2NO + "',";
				M_strSQLQRY += strTN2ID + ",";
				M_strSQLQRY += strTN2IT + ",";
				M_strSQLQRY += strTN2IQ + ",";
				M_strSQLQRY += strTN2FD + ",";
				M_strSQLQRY += strTN2FT + ",";
				M_strSQLQRY += strTN2FQ + ",";				
				M_strSQLQRY += txtRCTQT.getText().trim() + ",";
				M_strSQLQRY += txtISSQT.getText().trim() + ",";
				M_strSQLQRY += strTRAQT + ",";
				M_strSQLQRY += getUSGDTL("TF",'I',"") + ")";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					// To save the Last Memo no. in CO_CDTRN table				
				if(cl_dat.M_flgLCUPD_pbst)
				{
					M_strSQLQRY = "Update CO_CDTRN set " +
								"CMT_CCSVL = '" + txtDOCNO.getText().trim().substring(3) + "'," +												
								getUSGDTL("CMT",'U',"") + 
								" where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXTTF' and " +
								"CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP + "'";			
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
				if(cl_dat.M_flgLCUPD_pbst) 
				{
					M_strSQLQRY = "Update MM_DPTRN set " +
								"DP_TRNQT = isnull(DP_TRNQT,0) + " + txtTR1QT.getText().trim() + "," +
								getUSGDTL("DP",'U',"") + 
								" where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP + "'" +						
								" and CONVERT(varchar,DP_MEMDT,103) = '" + strDOCDT + "' and DP_TNKNO = '" + strTN1NO + "' and isnull(DP_ISSQT,0) =0";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");							
					M_strSQLQRY = "Update MM_DPTRN set " +
							"DP_TRNQT = isnull(DP_TRNQT,0) + " + strTRAQT + "," +
							getUSGDTL("DP",'U',"") + 
							" where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP + "'" +						
							" and CONVERT(varchar,DP_MEMDT,103) = '" + strDOCDT + "' and DP_TNKNO = '" + strTN2NO + "' and isnull(DP_ISSQT,0) =0";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
					if(!cl_dat.M_flgLCUPD_pbst)
					{
						setMSG("Issue is updated for the day,Transfer entry is not allowed..",'E');
					}
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))			// Modification
			{
				M_strSQLQRY = "Update MM_TFTRN set ";
			//	M_strSQLQRY += "TF_DOCDT = '" + strDOCDT + "',";
			//	M_strSQLQRY += "TF_MATCD = '" + strMATCD + "',";				
			//	M_strSQLQRY += "TF_TN1NO = '" + strTN1NO + "',";
				M_strSQLQRY += "TF_TN1ID = " + strTN1ID + ",";
				M_strSQLQRY += "TF_TN1IT = " + strTN1IT + ",";
				M_strSQLQRY += "TF_TN1IQ = " + strTN1IQ + ",";
				M_strSQLQRY += "TF_TN1FD = " + strTN1FD + ",";
				M_strSQLQRY += "TF_TN1FT = " + strTN1FT + ",";
				M_strSQLQRY += "TF_TN1FQ = " + strTN1FQ + ",";
			//	M_strSQLQRY += "TF_TN2NO = '" + strTN2NO + "',";
				M_strSQLQRY += "TF_TN2ID = " + strTN2ID + ",";
				M_strSQLQRY += "TF_TN2IT = " + strTN2IT + ",";
				M_strSQLQRY += "TF_TN2IQ = " + strTN2IQ + ",";
				M_strSQLQRY += "TF_TN2FD = " + strTN2FD + ",";
				M_strSQLQRY += "TF_TN2FT = " + strTN2FT + ",";
				M_strSQLQRY += "TF_TN2FQ = " + strTN2FQ + ",";				
				M_strSQLQRY += "TF_TN1AQ = " + txtRCTQT.getText().trim() + ",";
				M_strSQLQRY += "TF_TN2AQ = " + txtISSQT.getText().trim() + ",";
				M_strSQLQRY += "TF_TRFQT = " + strTRAQT + ",";				
				M_strSQLQRY += getUSGDTL("TF",'U',"");
				M_strSQLQRY += " where TF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TF_DOCTP = '" + strDOCTP + "'";
				M_strSQLQRY += " and TF_DOCNO = '" + strDOCNO + "'";	
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
				System.out.println("strOTR1QT"+strOTR1QT);
				System.out.println("strOTR2QT"+strOTR2QT);
				if(cl_dat.M_flgLCUPD_pbst) 
				{
					M_strSQLQRY = "Update MM_DPTRN set " +
								"DP_TRNQT = isnull(DP_TRNQT,0) + " + txtTR1QT.getText().trim()+"- "+strOTR1QT + "," +
								getUSGDTL("DP",'U',"") + 
								" where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP + "'" +						
								" and CONVERT(varchar,DP_MEMDT,103) = '" + strDOCDT + "' and DP_TNKNO = '" + strTN1NO + "' and isnull(DP_ISSQT,0) =0";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");							
					M_strSQLQRY = "Update MM_DPTRN set " +
							"DP_TRNQT = isnull(DP_TRNQT,0) + " + strTRAQT+"- "+strOTR2QT + "," +
							getUSGDTL("DP",'U',"") + 
							" where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP + "'" +						
							" and CONVERT(varchar,DP_MEMDT,103) = '" + strDOCDT + "' and DP_TNKNO = '" + strTN2NO + "' and isnull(DP_ISSQT,0) =0";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
					if(!cl_dat.M_flgLCUPD_pbst)
					{
						setMSG("Issue is updated,Transfer entry modification is not allowed..",'E');
					}
				}			
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))			// Deletion
			{
				M_strSQLQRY = "Update MM_TFTRN set ";
				M_strSQLQRY += getUSGDTL("TF",'U',"X");
				M_strSQLQRY += " where TF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TF_DOCTP = '" + strDOCTP + "'";
				M_strSQLQRY += " and TF_DOCNO = '" + strDOCNO + "'";				
				strTN1NQ = strTN2NQ = "0.000";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
				if(cl_dat.M_flgLCUPD_pbst) 
				{
					M_strSQLQRY = "Update MM_DPTRN set " +
								"DP_TRNQT = isnull(DP_TRNQT,0) - " + txtTR1QT.getText().trim() + "," +
								getUSGDTL("DP",'U',"") + 
								" where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP + "'" +						
								" and CONVERT(varchar,DP_MEMDT,103) = '" + strDOCDT + "' and DP_TNKNO = '" + strTN1NO + "' and isnull(DP_ISSQT,0) =0";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");							
					M_strSQLQRY = "Update MM_DPTRN set " +
							"DP_TRNQT = isnull(DP_TRNQT,0) - " + strTRAQT + "," +
							getUSGDTL("DP",'U',"") + 
							" where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP + "'" +						
							" and CONVERT(varchar,DP_MEMDT,103) = '" + strDOCDT + "' and DP_TNKNO = '" + strTN2NO + "' and isnull(DP_ISSQT,0) =0";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
					if(!cl_dat.M_flgLCUPD_pbst)
					{
						setMSG("Issue is updated for the day,Cancellation is not allowed..",'E');
					}
				}
			}						
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					JOptionPane.showMessageDialog(this,"Please note down Tank Transfer No. " + strDOCNO,"Tank Transfer No.",JOptionPane.INFORMATION_MESSAGE); 			
				clrCOMP();				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))				
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');								 
				setENBL(false);
				strDOCTP = cmbDOCTP.getSelectedItem().toString().substring(0,2);
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
		return;
	}
		
	/**
	 *  Method to fetch the records from MM_TFTRN 
	 * @param P_strDOCTP Transfer Type.
	 * @param P_strDOCNO Transfer Number
	 */
	private boolean getALLREC(String P_strDOCTP,String P_strDOCNO)
	{		
		java.sql.Date L_datDOCDT;
		//clrCOMP();
		try
		{
			M_strSQLQRY = "Select TF_DOCTP,TF_DOCNO,TF_DOCDT,TF_MATCD,TF_TRFFL,TF_TN1NO,";
			M_strSQLQRY += "TF_TN1ID,TF_TN1IT,TF_TN1IQ,TF_TN1FD,TF_TN1FT,TF_TN1FQ,TF_TN2NO,";
			M_strSQLQRY += "TF_TN2ID,TF_TN2IT,TF_TN2IQ,TF_TN2FD,TF_TN2FT,TF_TN2FQ,TF_TRFQT,TF_TN1AQ,TF_TN2AQ,CT_MATDS,CT_UOMCD";
			M_strSQLQRY += " from MM_TFTRN,CO_CTMST ";
			M_strSQLQRY += " where TF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TF_MATCD = CT_MATCD AND TF_DOCTP = '" + P_strDOCTP + "'";
			M_strSQLQRY += " and TF_DOCNO = '" + P_strDOCNO + "'";			
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(M_rstRSSET1!= null)
			{
				if(M_rstRSSET1.next())
				{
					L_datDOCDT = M_rstRSSET1.getDate("TF_DOCDT");
					if(L_datDOCDT != null)
						txtDOCDT.setText((M_fmtLCDAT.format(L_datDOCDT)));
					
					strTN1NO = M_rstRSSET1.getString("TF_TN1NO").trim();
					txtTN1NO.setText(strTN1NO);
					strFRTTP = cmbDOCTP.getSelectedItem().toString().substring(0,1);
					//??vldTNKNO(strFRTTP,strTN1NO,"");
					strMATCD = M_rstRSSET1.getString("TF_MATCD").trim();
					txtMATCD.setText(strMATCD);
					//getMATDS(strMATCD);
					txtMATDS.setText(M_rstRSSET1.getString("CT_MATDS").trim());
					txtUOMCD.setText(M_rstRSSET1.getString("CT_UOMCD").trim());
					txtTN1ID.setText(M_rstRSSET1.getString("TF_TN1ID").trim());
					txtTN1IT.setText(M_rstRSSET1.getString("TF_TN1IT").trim());
					strTN1IQ = M_rstRSSET1.getString("TF_TN1IQ").trim();
					txtTN1IQ.setText(strTN1IQ);
					txtTN1FD.setText(M_rstRSSET1.getString("TF_TN1FD").trim());
					txtTN1FT.setText(M_rstRSSET1.getString("TF_TN1FT").trim());
					strTN1FQ = M_rstRSSET1.getString("TF_TN1FQ").trim();
					strTN1AQ = nvlSTRVL(M_rstRSSET1.getString("TF_TN1AQ").trim(),"0");
					txtRCTQT.setText(strTN1AQ);
					txtTN1FQ.setText(strTN1FQ);
					strTR1QT = nvlSTRVL(M_rstRSSET1.getString("TF_TRFQT").trim(),"0");
					txtTR1QT.setText("-"+strTR1QT);	
					strOTR1QT = txtTR1QT.getText().trim();
					strTN1NQ = setNumberFormat((Float.valueOf(strTN1FQ).floatValue() - Float.valueOf(strTN1IQ).floatValue()),3);
					txtTN1NQ.setText(strTN1NQ);				
					txtTN2NO.setText(M_rstRSSET1.getString("TF_TN2NO").trim());
					txtTN2ID.setText(M_rstRSSET1.getString("TF_TN2ID").trim());
					txtTN2IT.setText(M_rstRSSET1.getString("TF_TN2IT").trim());
					strTN2IQ = nvlSTRVL(M_rstRSSET1.getString("TF_TN2IQ").trim(),"0");
					txtTN2IQ.setText(strTN2IQ);
					txtTN2FD.setText(M_rstRSSET1.getString("TF_TN2FD").trim());
					txtTN2FT.setText(M_rstRSSET1.getString("TF_TN2FT").trim());
					strTN2FQ = nvlSTRVL(M_rstRSSET1.getString("TF_TN2FQ").trim(),"0");
					strTR2QT = nvlSTRVL(M_rstRSSET1.getString("TF_TRFQT").trim(),"0");
					strTN2AQ = nvlSTRVL(M_rstRSSET1.getString("TF_TN2AQ").trim(),"0");
					txtISSQT.setText(strTN2AQ);
					txtTN2FQ.setText(strTN2FQ);
					txtTR2QT.setText(strTR2QT);
					strOTR2QT = txtTR2QT.getText().trim();
					strTN2NQ = setNumberFormat((Float.valueOf(strTN2FQ).floatValue() - Float.valueOf(strTN2IQ).floatValue()),3);
					txtTN2NQ.setText(strTN2NQ);
					M_rstRSSET1.close();										
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
					{						
						setENBL(false);						
						txtDOCNO.setEnabled(false);
						cmbDOCTP.setEnabled(false);
						txtTN1NO.setEnabled(false);
						txtTN2NO.setEnabled(false);
						txtTN1NO.requestFocus();						
					}					
					return true;
				}
			}			
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
		return false;
	}	
	
	/**
	 * Method to Execute the F1 Help for selected field.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtDOCNO"))					// Tank Transfer No.
			{
				strDOCTP = cmbDOCTP.getSelectedItem().toString().substring(0,2);				
				strDOCNO = cl_dat.M_strHLPSTR_pbst;					
				txtDOCNO.setText(strDOCNO);
				setMSG("Press Enter Key to display Details..",'N');
			}
			else if(M_strHLPFLD.equals("txtTN1NO"))				// From Tank No.
			{				
				txtTN1NO.setText(cl_dat.M_strHLPSTR_pbst);
				strMATCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
				txtMATCD.setText(strMATCD);
				getMATDS(strMATCD);
				setMSG("Please Enter Initial Dip..",'N');
				txtTN1ID.requestFocus();				
				txtTN1NO.setEnabled(false);								
			}
			else if(M_strHLPFLD.equals("txtTN2NO"))			// To Tank No.
			{				
				txtTN2NO.setText(cl_dat.M_strHLPSTR_pbst);
				txtTN2ID.requestFocus();
				setMSG("Please Enter Initial Dip..",'N');
				txtTN2NO.setEnabled(false);				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
/**
 * Method to Check given Tank No. with the Tank number in the Data Base.
 * @param P_strTNKTP String argument for Transfer Type.
 * @param P_strTNKNO String argument for Tank Number.
 * @param P_strMATCD String argument for Material Code.
 */	
	public boolean vldTNKNO(String P_strTNKTP,String P_strTNKNO,String P_strMATCD)
	{	
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			P_strTNKTP = "0" + P_strTNKTP;
			strDOCTP = cmbDOCTP.getSelectedItem().toString().substring(0,2);
			M_strSQLQRY = "Select TK_TNKNO,TK_MATCD from MM_TKMST";
			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_TNKNO = '" + P_strTNKNO + "'";
			M_strSQLQRY += " and TK_STSFL <> 'X'";
			M_strSQLQRY += " and TK_TNKTP = '" + P_strTNKTP + "'" ;			
			if(!P_strMATCD.equals(""))	
				M_strSQLQRY += " and TK_MATCD = '" + P_strMATCD + "'" ;			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET1 != null)
			{
				if(M_rstRSSET.next())
				{
					strMATCD = M_rstRSSET.getString("TK_MATCD");
					M_rstRSSET.close();
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return true;
				}
			}			
			setMSG("Invalid Tank No. Press F1 for help..",'E');			
			this.setCursor(cl_dat.M_curDFSTS_pbst);				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTNKNO");
		}
		return false;
	}	
	/**
	 *  Method to get the Material Description & UOM Code for the available Material Code.
	 * @param String argument for Material Code.
	 */
	private void getMATDS(String P_strMATCD)
	{
		try
		{
			M_strSQLQRY = "Select CT_MATDS,CT_UOMCD from CO_CTMST ";
			M_strSQLQRY += " where CT_MATCD = '" + P_strMATCD + "'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if (M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));
					txtUOMCD.setText(M_rstRSSET.getString("CT_UOMCD"));					
				}	
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMATDS");
		}
	}	
	/**
	 * Method to calculate the Quantity of the Material
	 * @param P_strTNKNO String argument for Tank Number.
	 * @param P_strDIPVL String argument for Dip Value.
	 * @param P_strTMPVL String argument for Temprature Value.
	 * @param P_strMATCD String argument for Material Code.
	 * For detailed calculation logic see document of Dip Entry
	 */
	private String calMATQT(String P_strTNKNO,String P_strDIPVL,String P_strTMPVL,String P_strMATCD)
	{			
		float L_fltDEPCT = 0,L_fltDEPVL = 0,L_fltINCVL = 0,L_fltMATGR = 0;
		String L_strTNKQT = "";
		float L_fltMAXTP = 0,L_fltMINTP = 0,L_fltCALTP, L_fltTOTQT = 0;
		float L_fltGRVVL = 0,L_fltTMPVL;
		L_fltTMPVL = Float.valueOf(P_strTMPVL).floatValue();
		float L_fltITMQT = 0;				
		try
		{
			//L_fltMATGR = getQTY(P_strTNKNO,P_strMATCD,P_strTMPVL);		// Item Specific Gravity			
			// To get the temp. greater than the given temp.
			M_strSQLQRY = "Select min(QP_NPFVL) LM_MAXTP from CO_QPMST where ";
			M_strSQLQRY += " QP_PRDCD = '" + P_strMATCD + "' and ";
			M_strSQLQRY += " QP_QCATP = '11' and QP_TSTTP = '1101' and ";
			M_strSQLQRY += " QP_QPRCD = 'SPG' and ";
			M_strSQLQRY += " QP_NPFVL >= " + P_strTMPVL;		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{
				if(M_rstRSSET.next())
				{
					L_fltMAXTP = M_rstRSSET.getFloat("LM_MAXTP");
					M_rstRSSET.close();
				}
			}		
			// To get the temp. less than the given temp.
			M_strSQLQRY = "Select max(QP_NPFVL) LM_MINTP from CO_QPMST where ";
			M_strSQLQRY += " QP_PRDCD = '" + P_strMATCD + "' and ";
			M_strSQLQRY += " QP_QCATP = '11' and QP_TSTTP = '1101' and ";
			M_strSQLQRY += " QP_QPRCD = 'SPG' and ";
			M_strSQLQRY += " QP_NPFVL <= " + P_strTMPVL;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!= null)	
			{
				if(M_rstRSSET.next())
				{
					L_fltMINTP = M_rstRSSET.getFloat("LM_MINTP");
					M_rstRSSET.close();
				}
			}
			// Now the uppper and lower temp values are in L_fltMAXTP & L_fltMINTP		
			if(L_fltMAXTP == 0 || L_fltMINTP == 0)
				L_fltCALTP = L_fltMAXTP + L_fltMINTP;
			else if((L_fltMAXTP - L_fltTMPVL) < (L_fltTMPVL - L_fltMINTP))
				L_fltCALTP = L_fltMAXTP;
			else
				L_fltCALTP = L_fltMINTP;
			// To get the specifis gravity corresponding to the given item, at given tempreture
			M_strSQLQRY = "Select QP_STDVL from CO_QPMST where ";
			M_strSQLQRY += " QP_PRDCD = '" + P_strMATCD + "' and ";
			M_strSQLQRY += " QP_QCATP = '11' and QP_TSTTP = '1101' and ";
			M_strSQLQRY += " QP_QPRCD = 'SPG' and ";
			M_strSQLQRY += " QP_NPFVL = " + L_fltCALTP;		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if (M_rstRSSET!= null)
			{
				if(M_rstRSSET.next())
				{
					//L_fltGRVVL = M_rstRSSET.getFloat("QP_STDVL");
					L_fltMATGR= M_rstRSSET.getFloat("QP_STDVL");
					M_rstRSSET.close();
				}
			}						
						
			// To get the depth at dip less than or equal to the given dip.
			M_strSQLQRY = "Select TKC_DEPCT,TKC_DEPVL,TKC_INCVL from";
			M_strSQLQRY += " MM_TKCTR where TKC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TKC_TNKNO = '" + P_strTNKNO + "' and ";
			M_strSQLQRY += "  TKC_DEPCT <= " + P_strDIPVL + " order by TKC_DEPCT desc";		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{
				if(M_rstRSSET.next())
				{
					L_fltDEPCT = M_rstRSSET.getFloat("TKC_DEPCT");
					L_fltDEPVL = M_rstRSSET.getFloat("TKC_DEPVL");
					L_fltINCVL = M_rstRSSET.getFloat("TKC_INCVL");
					M_rstRSSET.close();
				}
				else
				{
					M_strSQLQRY = "Select TKC_DEPCT,TKC_DEPVL,TKC_INCVL from MM_TKCTR where ";
					M_strSQLQRY += " TKC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TKC_TNKNO = '" + P_strTNKNO + "' and ";
					M_strSQLQRY += "  TKC_DEPCT > " + P_strDIPVL + " order by TKC_DEPCT asc";		
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							L_fltDEPCT = M_rstRSSET.getFloat("TKC_DEPCT");
							L_fltDEPVL = M_rstRSSET.getFloat("TKC_DEPVL");
							L_fltINCVL = M_rstRSSET.getFloat("TKC_INCVL");
						}
					}
					M_rstRSSET.close();
				}
			}
			// Material Quantity in MT
			L_strTNKQT = String.valueOf(((L_fltDEPVL + Math.abs(L_fltDEPCT - (Float.valueOf(P_strDIPVL).floatValue()))*L_fltINCVL)/1000) * L_fltMATGR);
			strUOMCD = txtUOMCD.getText();			
			// Getting Quantity in UOM
			if(strUOMCD.equals("LT"))
				L_fltITMQT = ((Float.valueOf(L_strTNKQT).floatValue()) / L_fltMATGR) * 1000;
			else if(strUOMCD.equals("KL"))
				L_fltITMQT = (Float.valueOf(L_strTNKQT).floatValue())/L_fltMATGR;
			else if(strUOMCD.equals("KG"))
				L_fltITMQT = (Float.valueOf(L_strTNKQT).floatValue()) * 1000;
			else if(strUOMCD.equals("MT"))
				L_fltITMQT = (Float.valueOf(L_strTNKQT).floatValue());
			
			L_strTNKQT = setNumberFormat(Float.valueOf(L_strTNKQT).floatValue(),3);
			return L_strTNKQT;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calMATQT");
			return L_strTNKQT;
		}		
	}			
	/**
	* Method to check the validy the given Data before inserting into the DataBase.
	*/
	boolean vldDATA()
	{
		strDOCDT = txtDOCDT.getText().trim(); 
		strTN1NO = txtTN1NO.getText().trim();
		strTN1ID = txtTN1ID.getText().trim();
		strTN1IT = txtTN1IT.getText().trim(); 
		strTN1FD = txtTN1FD.getText().trim(); 
		strTN1FT = txtTN1FT.getText().trim(); 
		strTN2NO = txtTN2NO.getText().trim();
		strTN2ID = txtTN2ID.getText().trim();
		strTN2IT = txtTN2IT.getText().trim(); 
		strTN2FD = txtTN2FD.getText().trim(); 
		strTN2FT = txtTN2FT.getText().trim(); 		
		strFRTTP = cmbDOCTP.getSelectedItem().toString().substring(0,1);
		strTOTTP = cmbDOCTP.getSelectedItem().toString().substring(1,2);		
		if(txtTR1QT.getText().trim().length() >0)
			strTRAQT = setNumberFormat(Math.abs(Float.valueOf(txtTR1QT.getText().trim()).floatValue()),3);		
		if((txtDOCDT.getText().trim()).length()==0)
		 {
			txtDOCDT.requestFocus();
			setMSG("Please Enter Transfer Date..",'E');
			return false;
		 }		 
		try
		{
			if (M_fmtLCDAT.parse(txtDOCDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>=0)			 
			{					
				txtDOCDT.requestFocus();
				setMSG("Transfer Date Must be smaller than current Date..",'E');					
				return false;
			}				
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		if((txtTN1NO.getText().trim()).length() == 0)				// Source Tank No
		{
			txtTN1NO.requestFocus();
			setMSG("Please Enter the Source Tank No. Press F1 for Help",'E');
			return false;			
		}								
		if((txtTN1ID.getText().trim()).length() == 0)
		{
			txtTN1ID.requestFocus();
			setMSG("Please Enter the Source Tank Initial Dip..",'E');
			return false;
		}
		if((txtTN1IT.getText().trim()).length() == 0)
		{
			txtTN1IT.requestFocus();
			setMSG("Please Enter the Source Tank Initial Temprature..",'E');
			return false;
		}
		if((txtMATCD.getText().trim()).length() == 0)
		{
			txtMATCD.requestFocus();
			setMSG("Please Enter the Material Code..",'E');
			return false;
		}
		if((txtTN1IQ.getText().trim()).length() == 0)
		{
			txtTN1IT.requestFocus();
			setMSG("Please Enter to Calculate Initial Quantity..",'E');
			return false;
		}
		strTN1IQ = calMATQT(strTN1NO,strTN1ID,strTN1IT,strMATCD);
		txtTN1IQ.setText(strTN1IQ);
		/*if( ! strTN1IQ.equals(txtTN1IQ.getText().trim()))
		{
			JOptionPane.showMessageDialog(this,"Initial Quantity is changed as Initial Dip & Temprature are modified..","Data Validation",JOptionPane.INFORMATION_MESSAGE); 
			txtTN1IQ.setText(strTN1IQ);					
			txtTR1QT.setText("");
			txtTR2QT.setText("");
			txtTN1NQ.setText("");
			txtTN2NQ.setText("");
			txtTN1FT.requestFocus();
			setMSG("Press Enter key to calculate & display Initial Quantity of Source Tank..",'N');
			return false;
		}*/
		if((txtTN1FD.getText().trim()).length() == 0)
		{
			txtTN1FD.requestFocus();
			setMSG("Please Enter the Source Tank Final Dip..",'E');
			return false;
		}
		if((txtTN1FT.getText().trim()).length() == 0)
		{
			txtTN1FT.requestFocus();
			setMSG("Please Enter the Source Tank Final Temprature..",'E');
			return false;
		}
		if((txtTN1FQ.getText().trim()).length() == 0)
		{
			txtTN1FT.requestFocus();
			setMSG("Please Enter to Calculate Final Quantity..",'E');
			return false;
		}
		if((txtTN1FQ.getText().trim()).length() == 0)
		{
			txtTN1FT.requestFocus();
			setMSG("Please Enter to Calculate Final Quantity..",'E');
			return false;
		}
		if((txtTN1NQ.getText().trim()).length() == 0)
		{
			txtTN1FT.requestFocus();
			setMSG("Please Enter to Calculate Net Quantity..",'E');
			return false;
		}
		strTN1FQ = calMATQT(strTN1NO,strTN1FD,strTN1FT,strMATCD);
		txtTN1FQ.setText(strTN1FQ);
		txtTN1NQ.setText(setNumberFormat(Float.valueOf(strTN1FQ).floatValue() - Float.valueOf(strTN1IQ).floatValue(),3));
		/*if( ! strTN1FQ.equals(txtTN1FQ.getText().trim()))
		{
			JOptionPane.showMessageDialog(this,"Final Quantity is changed as Final Dip & Temprature are modified..","Data Validation",JOptionPane.INFORMATION_MESSAGE);
			txtTN1FQ.setText(strTN1FQ);					
			txtTR1QT.setText("");
			txtTR2QT.setText("");
			txtTN1NQ.setText("");
			txtTN2NQ.setText("");
			txtTN1FT.requestFocus();					
			setMSG("Press Enter key to calculate & display Final Quantity of Source Tank..",'N');
			return false;
		}*/
		////////
		/*if((txtRCTQT.getText().trim()).length() == 0)
		{
			txtRCTQT.requestFocus();
			setMSG("Please Enter Reciept Quantity & press Enter key..",'E');
			return false;			
		}*/
		if((txtTR1QT.getText().trim()).length() == 0)
		{
			txtRCTQT.requestFocus();
			setMSG("Please Enter Reciept Quantity & press Enter key to display Transfer Quantity..",'E');
			return false;			
		}					
		if((txtTN2NO.getText().trim()).length() == 0)				// Target Tank No
		{
			txtTN2NO.requestFocus();
			setMSG("Please Enter the Target Tank No. Press F1 for Help",'E');
			return false;
		}		
		if((txtTN2ID.getText().trim()).length() == 0)
		{
			txtTN2ID.requestFocus();
			setMSG("Please Enter the Target Tank Initial Dip..",'E');
			return false;
		}
		if((txtTN2IT.getText().trim()).length() == 0)
		{
			txtTN2IT.requestFocus();
			setMSG("Please Enter the Target Tank Initial Temprature..",'E');
			return false;
		}
		if((txtTN2IQ.getText().trim()).length() == 0)
		{
			txtTN2IT.requestFocus();
			setMSG("Press Enter key to calculate & display Initial Quantity..",'N');
			return false;			
		}
		strTN2IQ = calMATQT(strTN2NO,strTN2ID,strTN2IT,strMATCD);
		txtTN2IQ.setText(strTN2IQ);
		/*if( ! strTN2IQ.equals(txtTN2IQ.getText().trim()))
		{
			JOptionPane.showMessageDialog(this,"Initial Quantity is changed as Initial Dip & Temprature are modified..","Data Validation",JOptionPane.INFORMATION_MESSAGE);
			txtTN2IQ.setText(strTN2IQ);										
			txtTR2QT.setText("");					
			txtTN2NQ.setText("");
			txtTN2IT.requestFocus();
			setMSG("Press Enter key to calculate & display Inital Quantity of Target Tank..",'N');
			return false;
		}*/
		if((txtTN2FD.getText().trim()).length() == 0)
		{
			txtTN2FD.requestFocus();
			setMSG("Please Enter the Target Tank Final Dip..",'E');
			return false;
		}
		if((txtTN2FT.getText().trim()).length() == 0)
		{
			txtTN2FD.requestFocus();
			setMSG("Please Enter the Target Tank Final Temprature..",'E');
			return false;
		}
		if((txtTN2FQ.getText().trim()).length() == 0)
		{
			txtTN2FT.requestFocus();
			setMSG("Press Enter key to calculate & display Final Quantity..",'N');
			return false;			
		}
		if((txtTN2NQ.getText().trim()).length() == 0)
		{
			txtTN2FT.requestFocus();
			setMSG("Press Enter key to calculate & display Net Quantity..",'N');
			return false;			
		}
		strTN2FQ = calMATQT(strTN2NO,strTN2FD,strTN2FT,strMATCD);
		/*if( ! strTN2FQ.equals(txtTN2FQ.getText().trim()))
		{
			JOptionPane.showMessageDialog(this,"Final Quantity is changed as Final Dip & Temprature are modified..","Data Validation",JOptionPane.INFORMATION_MESSAGE);
			txtTN2IQ.setText(strTN2IQ);										
			txtTR2QT.setText("");
			txtTN2NQ.setText("");
			txtTN2FT.requestFocus();
			setMSG("Press Enter key to calculate & display Final Quantity of Target Tank..",'E');
			return false;
		}*/
		txtTN2FQ.setText(strTN2FQ);	
		txtTN2NQ.setText(setNumberFormat(Float.valueOf(strTN2FQ).floatValue() - Float.valueOf(strTN2IQ).floatValue(),3));
		/*if((txtISSQT.getText().trim()).length() == 0)
		{
			txtISSQT.requestFocus();
			setMSG("Please Enter Issue Quantity & press Enter key..",'N');
			return false;			
		}*/
		if((txtTR2QT.getText().trim()).length() == 0)
		{
			txtISSQT.requestFocus();
			setMSG("Please Enter Issue Quantity & press Enter key to display Transfer Quantity..",'E');
			return false;			
		}
		if((Math.abs(Float.valueOf(txtTR2QT.getText().trim()).floatValue()) - Math.abs(Float.valueOf(txtTR1QT.getText().trim()).floatValue())) != 0)
		{  
			setMSG("Transfer Quantity must be equal, Please Enter complete Data once again..",'E');
			return false;
		}
		return true; 
	}	
	/*class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{					
				if(input ==txtTN1NO)					
				{									
					if(!vldTNKNO(strFRTTP,txtTN1NO.getText().trim(),""))
					{
						txtTN1NO.requestFocus();
						setMSG("Invalid Source Tank No.Press F1 for Help",'E');
						return false;
					}								
				}
				if(input ==txtTN2NO)					
				{					
					if(!vldTNKNO(strFRTTP,txtTN1NO.getText().trim(),""))
					{
						txtTN1NO.requestFocus();
						setMSG("Invalid Source Tank No.Press F1 for Help",'E');
						return false;
					}								
				}				
				return true;
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"INPVF");
				return false;
			}
		}
	}*/

}