/*
System Name   : Marketing System
Program Name  : Payment Receipt Entry.
Author        : Mr. Zaheer A. Khan
Date          : 08/06/2006
System        : 
Version       : MMS v2.0.0
Modificaitons : 
Modified By   :
Modified Date :  12/07/2006 
Modified det. :
Version       :
*/

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;import java.awt.Color;
import java.sql.ResultSet;import javax.swing.JPanel;import javax.swing.JTabbedPane;
import java.sql.CallableStatement;import javax.swing.JComboBox;

/**
<P><PRE style = font-size : 10 pt >

<b>Purpose :</b> This Program is  used for  capturing and processing Payment Receipts.Here we canbe able add new Records, Modify existing Records and to Delete unwanted records.
			
List of tables used :
Table Name     Primary key                                          Operation done
                                                         Insert   Update   Query   Delete	
------------------------------------------------------------------------------------------
MR_IVTRN       IVT_CMPCD,IVT_INVNO,IVT_PRDCD,IVT_PKGTP                        #
MR_PRTRN       PR_CMPCD,PR_PRTTP,PR_PRTCD,PR_DOCTP,
               PR_DOCNO,PR_SRLNO                            #        #        #       #
CO_PTMST       PT_PRTTP,PT_PRTCD                                     #        #
MR_PATRN       PA_CMPCD,PA_PRTTP,PA_PRTCD,PA_CRDTP,
               PA_RCTNO,PA_DBTTP,PA_DBTNO                            #		  #
MR_PLTRN       PA_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO          #        #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                         #        #
--------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		 Type/Size       Description
----------------------------------------------------------------------------------------------------------
txtDSRCD    PR_PRTCD        CO_PTMST         varchar(5)      Distributer Code
txtPRTTP    PR_PRTTP        MR_PRTRN         varchar(1)      Party Type 
txtPRTCD    PR_PRTCD        CO_PTMST         varchar(5)      Party Code Code
txtDOCNO    PR_DOCNO        MR_PRTRN         varchar(8)      Document Number
txtDOCTP    PR_DOCTP        MR_PRTRN         varchar(2)      Document Type
txtDOCDT    PR_DOCDT        MR_DOCDT         Date            Document Date
cmbMKTTP    CMT_CODCD       CO_CDTRN         vatchar(2)      Market Type
lblPRTBL    CO_PTMST        PT_YTDVL         Decimal(12.2)   Party's Balance
lblTOTVL    Variable                                         Total Receipt Amount
lblADJVL    Variable                                         Total Adjusted Amount
----------------------------------------------------------------------------------------------------------
In this form there are two Tab.
	1) For Cheque wise
    2) For Invoice Wise

1) For Cheque wise Entry there may be one to many relation appear.
   means for one cheque amount more then one invoice can be Adjusted
2) For invoice wise Entry there are only one to one relation appear.
   
For distributer Code
     Table used  : 1) CO_PTMST                             
     Condition   : 1) PT_PRTTP='D' 
                   2) AND ifnull(PT_STSFL,'')<> 'X'
For Party Code :</b>
     Table used  : 1) CO_PTMST,MR_PLTRN                             
     Condition   : 1) PT_PRTTP='C' 
				   2)AND PL_PRTCD=PT_PRTCD 
                   3)AND (PL_DOCVL-PL_ADJVL)>0
                   4) AND ifnull(PT_STSFL,'')<> 'X'
                  
<B>For Payment Type:</B>
     Table used  : 1)CO_CDTRN   
     Conditation : 1) CMT_CGMTP='SYS' 
				 : 2) AND CMT_CGSTP = 'MRXXPTT' 
                 : 3) AND ifnull(CMT_STSFL,'') <> 'X'";
<B>For Document Number</B>
     Table used  : 1)MR_PRTRN
     Condition   : 1) PR_CMPCD =strCMPCD
				   2) AND PR_DOCTP= txtDOCTP.getText()
                   3) AND ifnull(PR_STSFL,'') <> 'X'";
<B>For Payment Mode </B>
     Table used  : 1)CO_CDTRN 
     Condition   : 1) CMT_CGMTP ='SYS'
                   2) AND MT_CGSTP  = 'MRXXPMM'
				   3) AND ifnull(CMT_STSFL,'') <> 'X'";
<B> for Document Number,Document Amount,Out-Stdg Amount,Consignee,Distributer,Buyer
    Table  used  : 1)MR_PLTRN
                   2)MR_IVTRN 
    Condition    : 1) PL_DOCNO = IVT_INVNO
                   2) And PL_DOCTP =strINVTP(Invoice Type
                   3) And PL_CMPCD = strCMPCD 
                   4) AND PL_PRTCD = given By User

<B> For Invoice details in Invoice Wise Tab
	Table  Used  : 1)MR_PLTRN,MR_IVTRN 
    Condition    : 1)PL_DOCNO = IVT_INVNO
                   2)AND PL_CMPCD=strCMPCD
                   3)AND PL_MKTTP=L_strMKTTP
                   4)AND (PL_DOCVL -PL_ADJVL)>0 
				   5)AND PL_DOCTP like strDOCTP%
				   6)AND PL_PRTCD =txtPRTCD.getText().trim()
 
For Addition :
	Value fron Receipt Entry Table will be Saved in Table MR_PRTRN
    And Value of Receipt Adjustment Table will bw Saved in Table MR_PATRN
    And Update Field PL_ADJVL of Table MR_PLTRN with Adjusted value.
     
For Modification :
	Value is Fetched from table MR_PRTRN and display on Receipt Entry table.
    And Linkup value will came from MR_PATRN and display on Payment Receipt Adjustment table.
    When we Modify the Receipt amount in Table Receipt Entry , then PR_RCTVL field of Table 
    MR_PRTRN will be  Modifed 
    When we Modify adjusted amount in Table receipt Adjusted table, the value will be modify
    in Table MR_PATRN, and MR_PLTRN.

For deletion  :
    When we delete any Row from table receipt Entry then only MR_PRTRN table will be updated.
    When we deletd any Row from table Receipt Adjustment table then MR_PATRN, And MR_PLTRN 
    table will be updated.


Note : Store Procedure  updPLTRN_PMR() Called For addition,Modification And Deletion

validation :

   Validation on Payment type.
   Validation on Document Number.
   Validation on Cheque No And bank Code.
   Validation on Cheque No, Cheque date,Bank Code with data base for duplicate cheque Entry
   Validation on Cheque No(Cheque number can't be less then 10 digit)
   Validation on Total Receipt Amount And sum of individual Cheque Amount.
   Validation on Multiple Entry of Document number in Receipt adjustment table 
   Validation on out stdg Amount and Adjusted Amount (adjusted Amount can not be Exceed from 
   Out-Stdg Amount.
   Adjusted amount in Receipt Adjustment table Can't be greater then Receipt Amount.
   In 
   
*/

public class mr_tepmt extends cl_pbase
{
	private JTextField txtPRTNM,txtPRTCD,txtDOCNO,txtTOTVL,txtDOCTP,txtRECAM,txtADJAM,txtBALAM,txtDOCDT;
	private JTextField txtMOPCD,txtCHQNO,txtCHQDT,txtBNKCD,txtBNKNM,txtCHQAM;
	
	private JTextField txtINVNO1,txtINVDT,txtPRTCD1,txtPRTNM1,txtINVAM,txtOTSAM,txtCHQNO1,txtCHQDT1;
	private JTextField txtBNKCD1,txtMOPCD1,txtBNKNM1,txtCHQAM1;
	private JTextField txtDOCTP1,txtDOCNO1,txtDOCDT1;
	
	private JTextField txtINVNO,txtADJVL,txtPLDOC;
	private JTextField txtDSRCD,txtDSRNM;
	private cl_JTable tblRECEN,tblRECAD;    
	private JLabel lblTOTVL,lblADJVL,lblPRTBL;
	private TBLINPVF objTBLVRF; 
	private JCheckBox chkCHKFL;
	
	private JTabbedPane tbpMAIN;
	private JPanel pnlINVWS; 
	private JPanel pnlCHQWS; 
	
	private JComboBox cmbMKTTP,cmbPMTTP;
	
	private ButtonGroup btgINCRE;    
	private JRadioButton rdbINVIC;	 /**JRadioButton to  Invoice*/
	private JRadioButton rdbDEBIT;	 /**JRadioButton to Credit Note */
	
	private final int TB1_CHKFL=0;
	private final int TB1_MOPCD=1;
	private final int TB1_CHQNO=2;
	private final int TB1_CHQDT=3;
	private final int TB1_BNKCD=4;
	private final int TB1_BNKNM=5;
	private final int TB1_RCTVL=6;
	
	private final int TB2_CHKFL=0;
	private final int TB2_DOCNO=1;
	private final int TB2_DOCDT=2;
	private final int TB2_INVVL=3;
	private final int TB2_AVLVL=4;
	private final int TB2_ADJVL=5;
	private final int TB2_CNSNM=6;
	private final int TB2_DSRNM=7;
	private final int TB2_BYRNM=8;
	private final int TB2_ADJSH=9;
	private final int TB2_DOCTP=10; 
		
	private int intRWCNT=0;       // variable for Row Count
	private int intRWADJ=0;       // Variable for Adjustment table row Cont
	private String strTEMP;
	
	private final String strCMPCD="01";   //Comany Code
	
	private String strSTSFL="0";
	private String strDOCTP;
	private double dblADJVL=0.0;
	private CallableStatement calSTAMT ;
	private Hashtable hstBNKCD = new Hashtable();	
	private Hashtable hstPARTY = new Hashtable();
	private Hashtable hstINVDL = new Hashtable();
	public mr_tepmt()
	{
		super(2);
		setMatrix(20,9);
		try
		{
			tbpMAIN  = new JTabbedPane();
			pnlINVWS = new JPanel(null);
			pnlCHQWS = new JPanel(null);
		
			lblTOTVL = new JLabel("");
			lblPRTBL = new JLabel("");
			//lblYTDFL = new JLabel("");
			
			cmbMKTTP=new JComboBox();
			cmbPMTTP=new JComboBox();
			
			btgINCRE=new ButtonGroup();
			String L_strCODCD="";
			
			lblTOTVL.setForeground(Color.blue);
			lblPRTBL.setForeground(Color.blue);
					
			calSTAMT =cl_dat.M_conSPDBA_pbst.prepareCall("{call updPLTRN_PMR(?,?,?)}");
			
			add(new JLabel("Distributer "),1,4,1,1,this,'L');
			add(txtDSRCD = new TxtLimit(5),1,5,1,1,this,'L');
			add(txtDSRNM = new TxtLimit(30),1,6,1,4,this,'L');
			
			add(new JLabel("Party "),2,4,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(8),2,5,1,1,this,'L');
			add(txtPRTNM = new TxtLimit(30),2,6,1,4,this,'L');
		
			add(new JLabel("Payment Receipt Entry "),1,1,1,3,pnlCHQWS,'L');
			//add(new JLabel("Pmt. Type"),2,1,1,1,pnlCHQWS,'L');
			//add(txtDOCTP = new TxtLimit(2),3,1,1,1,pnlCHQWS,'L');
			add(new JLabel("Doc. No"),2,1,1,1,pnlCHQWS,'L');
			add(txtDOCNO = new TxtNumLimit(8),3,1,1,1,pnlCHQWS,'L');
			add(new JLabel("Doc. Date"),2,2,1,1,pnlCHQWS,'L');
			add(txtDOCDT = new TxtDate(),3,2,1,1,pnlCHQWS,'L');		
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXMKT'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODCD =L_strCODCD+" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbMKTTP.addItem(L_strCODCD);
				}
			}
		 	if(M_rstRSSET != null)
				M_rstRSSET.close();
			add(new JLabel("Market Type"),1,1,1,1,this,'L');
	
			add(cmbMKTTP,1,2,1,2,this,'L');
			
			M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
				+ " AND CMT_CGSTP = 'MRXXPTT' AND CMT_CODCD Like '1%' AND ifnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					cmbPMTTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
			}
		 	if(M_rstRSSET != null)
				M_rstRSSET.close();
			add(new JLabel("Pmt. Type"),2,1,1,1,this,'L');
	
			add(cmbPMTTP,2,2,1,2,this,'L');
			
			add(rdbINVIC=new JRadioButton("Invoice",true),3,1,1,1,this,'L');
			add(rdbDEBIT=new JRadioButton("Debit Note"),3,2,1,1,this,'L');
			
			btgINCRE.add(rdbINVIC);
			btgINCRE.add(rdbDEBIT);			
			
			//add(new JLabel("Pmt. Type"),2,2,1,1,pnlINVWS,'L');
			//add(txtDOCTP1 = new TxtLimit(2),3,2,1,1,pnlINVWS,'L');
			add(new JLabel("Doc. No"),2,2,1,1,pnlINVWS,'L');
			add(txtDOCNO1 = new TxtNumLimit(8),3,2,1,1,pnlINVWS,'L');
			add(new JLabel("Doc. Date"),2,3,1,1,pnlINVWS,'L');
			add(txtDOCDT1 = new TxtDate(),3,3,1,1,pnlINVWS,'L');	
			
			add(new JLabel("Invoice No"),2,4,1,1,pnlINVWS,'L');
			add(txtINVNO1 = new TxtLimit(8),3,4,1,1,pnlINVWS,'L');
			
			add(new JLabel("Inv. date"),2,5,1,1,pnlINVWS,'L');
			add(txtINVDT = new TxtDate(),3,5,1,1,pnlINVWS,'L');
			
			add(new JLabel("Doc Type"),2,6,1,1,pnlINVWS,'L');
			add(txtPLDOC = new TxtLimit(5),3,6,1,1,pnlINVWS,'L');
			
			add(new JLabel("Inv Amount"),2,7,1,1,pnlINVWS,'L');
			add(txtINVAM = new TxtNumLimit(12.2),3,7,1,1,pnlINVWS,'L');
			
			add(new JLabel("Out st. Amount"),2,8,1,1.5,pnlINVWS,'L');
			add(txtOTSAM = new TxtNumLimit(12.2),3,8,1,1,pnlINVWS,'L');
			
			add(new JLabel("Pmt Mode"),5,2,1,1,pnlINVWS,'L');
			add(txtMOPCD1 = new TxtLimit(2),6,2,1,1,pnlINVWS,'L');
			
			add(new JLabel("Cheque No"),5,3,1,1,pnlINVWS,'L');
			add(txtCHQNO1 = new TxtNumLimit(10),6,3,1,1,pnlINVWS,'L');
			
			add(new JLabel("Cheque Date"),5,4,1,1,pnlINVWS,'L');
			add(txtCHQDT1 = new TxtDate(),6,4,1,1,pnlINVWS,'L');
			add(new JLabel("Bank Code"),5,5,1,1,pnlINVWS,'L');
			add(txtBNKCD1 = new TxtLimit(5),6,5,1,1,pnlINVWS,'L');
			add(new JLabel("Bank Name"),5,6,1,1,pnlINVWS,'L');
			add(txtBNKNM1 = new TxtLimit(30),6,6,1,2,pnlINVWS,'L');
			
			add(new JLabel("Cheque Amt."),5,8,1,1,pnlINVWS,'L');
			add(txtCHQAM1 = new TxtNumLimit(12.2),6,8,1,1,pnlINVWS,'L');
			
			add(new JLabel("Receipt Amount"),2,3,1,1.5,pnlCHQWS,'L');
			add(txtRECAM = new TxtNumLimit(12.2),3,3,1,1.5,pnlCHQWS,'L');
						
			add(new JLabel("Adjusted Amount"),2,5,1,1.5,pnlCHQWS,'L');
			add(txtADJAM = new TxtNumLimit(12.2),3,5,1,1.5,pnlCHQWS,'L');
					
			add(new JLabel("Balance Amount"),2,7,1,1.5,pnlCHQWS,'L');
			add(txtBALAM = new TxtNumLimit(12.2),3,7,1,1.5,pnlCHQWS,'L');
			
			add(new JLabel("Total "),8,8,1,1,pnlCHQWS,'L');
			add(lblTOTVL,8,9,1,1,pnlCHQWS,'L');
			
			String[] L_strTBLHD1 = {" ","Pmt. Mode","Cheque No","Cheque Date","Bank Code ","Bank Name","Amount"};
			int[] L_intCOLSZ1 = {15,90,90,90,100,255,100};
			tblRECEN = crtTBLPNL1(pnlCHQWS,L_strTBLHD1,50,4,1,4,8.9,L_intCOLSZ1,new int[]{0});
		
			add(new JLabel("Payment Receipt Adjustment "),9,1,1,3,pnlCHQWS,'L');
			String[] L_strTBLHD2 = {" ","Doc. No","Doc. Date","Document Amt","Out-stdg Amt ","Amt tobe Adjusted","Consignee","Distributer","Buyer","Adj Amount","Doc Type"};
			int[] L_intCOLSZ2 = {15,80,80,80,80,80,80,200,200,50,40};
			tblRECAD = crtTBLPNL1(pnlCHQWS,L_strTBLHD2,50,10,1,4,8.9,L_intCOLSZ2,new int[]{0});
		
			add(new JLabel("Balance "),3,4,1,1,this,'L');
			add(lblPRTBL,3,5,1,1.5,this,'L');
		
					
			txtDOCDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
			txtDOCDT1.setText(cl_dat.M_txtCLKDT_pbst.getText());
			txtADJAM.setText("0.0");
			txtBALAM.setText("0.0");
			
			txtMOPCD = new TxtLimit(2);
			txtCHQNO = new TxtNumLimit(10);
			txtCHQDT = new TxtDate();
			txtBNKCD = new TxtLimit(10);
			txtBNKNM = new TxtLimit(40);
			txtCHQAM = new TxtNumLimit(12.2);
			
			txtINVNO = new TxtNumLimit(8);
			txtADJVL  = new TxtNumLimit(12.2);
			
			txtMOPCD.addFocusListener(this);
			txtMOPCD.addKeyListener(this);
			
			txtCHQNO.addFocusListener(this);
			txtCHQNO.addKeyListener(this);
			
			txtCHQDT.addFocusListener(this);
			txtCHQDT.addKeyListener(this);
			
			txtBNKCD.addFocusListener(this);
			txtBNKCD.addKeyListener(this);
			
			txtBNKNM.addFocusListener(this);
			txtBNKNM.addKeyListener(this);
			
			txtCHQAM.addFocusListener(this);
			txtCHQAM.addKeyListener(this);
			
			txtINVNO.addFocusListener(this);
			txtINVNO.addKeyListener(this);
			
			txtADJVL.addFocusListener(this);
			txtADJVL.addKeyListener(this);
	
			M_strSQLQRY="Select PT_PRTTP,PT_PRTCD,PT_PRTNM from CO_PTMST where  ifnull(PT_STSFL,'')<> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET!=null)
			{
				String L_strPRTNM="";
				while(M_rstRSSET.next())
				{
					hstPARTY.put(nvlSTRVL(M_rstRSSET.getString("PT_PRTTP"),"  ") +""+nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"  "),nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  "));
				}
			}
			
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			tbpMAIN.addTab("Cheque Wise",pnlCHQWS);
			tbpMAIN.addTab("Invoice Wise",pnlINVWS);
			
			add(tbpMAIN,4,1,16,9,this,'L');
			
			tblRECEN.setCellEditor(TB1_MOPCD,txtMOPCD);
			tblRECEN.setCellEditor(TB1_CHQNO,txtCHQNO);
			tblRECEN.setCellEditor(TB1_CHQDT,txtCHQDT);
			tblRECEN.setCellEditor(TB1_BNKCD,txtBNKCD);
			tblRECEN.setCellEditor(TB1_BNKNM,txtBNKNM);
			tblRECAD.setCellEditor(TB2_DOCNO,txtINVNO);
			tblRECAD.setCellEditor(TB2_ADJVL,txtADJVL);
			tblRECEN.setCellEditor(TB1_CHKFL,chkCHKFL = new JCheckBox());
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new TBLINPVF();
			tblRECEN.setInputVerifier(objTBLVRF);
			tblRECAD.setInputVerifier(objTBLVRF);	
			vldINVER objINVER=new vldINVER();
			tblRECEN.addFocusListener(this);
			tblRECEN.addKeyListener(this);
			tblRECAD.addFocusListener(this);
			tblRECAD.addKeyListener(this);
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objINVER);
			}		
			setENBL(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"Constructor");
		}
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC==txtMOPCD) 
			{
				setMSG("Press F1 to select Mode of Payment & then press Enter.",'N');
				if(tblRECEN.getSelectedRow()<intRWCNT)
					((JTextField)tblRECEN.cmpEDITR[TB1_MOPCD]).setEditable(false);
				else
					((JTextField)tblRECEN.cmpEDITR[TB1_MOPCD]).setEditable(true);
			}
			if(M_objSOURC==txtCHQNO) 
			{
				setMSG("Cheque Number ",'N');
				if(tblRECEN.getSelectedRow()<intRWCNT)
					((JTextField)tblRECEN.cmpEDITR[TB1_CHQNO]).setEditable(false);
				else
					((JTextField)tblRECEN.cmpEDITR[TB1_CHQNO]).setEditable(true);
			}
			if(M_objSOURC==txtBNKCD) 
			{
				setMSG("Press F1 to select Bank Code & then press Enter.",'N');
				if(tblRECEN.getSelectedRow()<intRWCNT)
					((JTextField)tblRECEN.cmpEDITR[TB1_BNKCD]).setEditable(false);
				else
					((JTextField)tblRECEN.cmpEDITR[TB1_BNKCD]).setEditable(true);
			}
			if(M_objSOURC==txtINVNO)
			{
				setMSG("Press F1 to select Bank Code & then press Enter.",'N');
				if(tblRECAD.getSelectedRow()<intRWADJ)
					((JTextField)tblRECAD.cmpEDITR[TB2_DOCNO]).setEditable(false);
				else
					((JTextField)tblRECAD.cmpEDITR[TB2_DOCNO]).setEditable(true);
			}
		}
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}
		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			
			if(rdbINVIC.isSelected())
			{
				strDOCTP="2";
			}
			if(rdbDEBIT.isSelected())
			{
				strDOCTP="3";
			}
					
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				intRWCNT=0;
				intRWADJ=0;
				dblADJVL=0.0;
				lblTOTVL.setText("");
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
					setENBL(false);
				else
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						//txtDOCTP.setEnabled(true);
						cmbPMTTP.setEnabled(true);
					    txtDOCNO.setEnabled(true);
						//txtDOCTP1.setEnabled(false);
						txtINVNO1.setEnabled(false);
						txtPRTCD.setEnabled(false);
						txtDSRCD.setEnabled(false);
						//txtDOCTP.requestFocus();
						cmbPMTTP.requestFocus();
						//txtDOCNO.requestFocus();
					}
					else 
					{
						txtDOCDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
						txtDOCDT1.setText(cl_dat.M_txtCLKDT_pbst.getText());
						txtADJAM.setText("0.0");
						txtBALAM.setText("0.0");
						setENBL(true);
						cmbMKTTP.requestFocus();
						intRWCNT=0;
						intRWADJ=0;
					}
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ActionPerformed");
		}
	}
	/**
	 * Super Class metdhod overrided to inhance its funcationality, to enable disable 
	 * components according to requriement.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtDOCNO.setEnabled(false);
		txtPRTNM.setEnabled(false);
		txtADJAM.setEnabled(false);
		txtBALAM.setEnabled(false);
		
		txtINVDT.setEnabled(false);
		txtDOCNO1.setEnabled(false);
		
		txtINVAM.setEnabled(false);
		txtBNKNM1.setEnabled(false);
		//txtDOCDT1.setEnabled(false);
		txtOTSAM.setEnabled(false);
		txtPLDOC.setEnabled(false);
		txtDSRNM.setEnabled(false);
		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
		{
			txtDOCNO.setEnabled(true);
			//txtDOCTP.setEnabled(true);
		}
		tblRECEN.cmpEDITR[TB1_BNKNM].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_DOCDT].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_INVVL].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_AVLVL].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_CNSNM].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_DSRNM].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_BYRNM].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_ADJSH].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_DOCTP].setEnabled(false);
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			int L_inaCOLSZ[]={60,70,60,60,60,60,60,60,60};
			
			if(M_objSOURC==txtDSRCD)
			{
				M_strHLPFLD="txtDSRCD";
				M_strSQLQRY="Select distinct PT_PRTCD,PT_SHRNM,PT_PRTNM from CO_PTMST where  PT_PRTTP='D' ";
				if(txtDSRCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtDSRCD.getText().trim().toUpperCase()+"%'"; 
				}
					M_strSQLQRY +="Order by PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Dist Code", " Short Name","Dist Name"},3,"CT",new int[]{80,100,325});
			}
			if(M_objSOURC==txtPRTCD)
			{
				M_strHLPFLD="txtPRTCD";
				
				if((cmbPMTTP.getSelectedItem().toString()).substring(0,2).equals("13"))
				{
					M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM,PT_YTDVL,PT_YTDFL from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD AND (PL_DOCVL-PL_ADJVL)>0 ";
					M_strSQLQRY +=" AND PL_DOCTP LIKE '"+strDOCTP+"%'";
				}
				else
				{
					M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM,PT_YTDVL,PT_YTDFL from CO_PTMST  where  PT_PRTTP='C'  ";
				}
				
				if(txtDSRCD.getText().trim().length()==5)
				{
					if(!txtDSRCD.getText().trim().substring(1,5).equals("8888"))
					{
						M_strSQLQRY += " AND PT_DSRCD='"+txtDSRCD.getText().trim()+"'";					
					}
				}
				
				
				if(txtPRTCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtPRTCD.getText().trim().toUpperCase()+"%'"; 
				}
				//System.out.println(M_strSQLQRY);
					M_strSQLQRY +="Order by PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name","Party Balance","CR/DB"},4,"CT",new int[]{80,290,95,40});
			}
			/*if(M_objSOURC==txtDOCTP)
			{
				M_strHLPFLD="txtDOCTP";
				M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
				+ " AND CMT_CGSTP = 'MRXXPTT' AND CMT_CODCD Like '1%' AND ifnull(CMT_STSFL,'') <> 'X'";
				if(txtDOCTP.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtDOCTP.getText().trim()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Payment Code", "Description"},2,"CT");
			}
			
			if(M_objSOURC==txtDOCTP1)
			{
				M_strHLPFLD="txtDOCTP1";
				M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
				+ " AND CMT_CGSTP = 'MRXXPTT' AND CMT_CODCD Like '1%' AND ifnull(CMT_STSFL,'') <> 'X'";
				if(txtDOCTP1.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtDOCTP1.getText().trim()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Payment Code", "Description"},2,"CT");
			}
			*/
			if(M_objSOURC==txtBNKCD)
			{
				M_strHLPFLD="txtBNKCD";
				M_strSQLQRY="Select PT_PRTCD,PT_PRTNM from CO_PTMST where  PT_PRTTP='B' AND ifnull(PT_STSFL,'')<> 'X'";
				if(txtBNKCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtBNKCD.getText().trim().toUpperCase()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT");
			}
			if(M_objSOURC==txtBNKCD1)
			{
				M_strHLPFLD="txtBNKCD1";
				M_strSQLQRY="Select PT_PRTCD,PT_PRTNM from CO_PTMST where  PT_PRTTP='B' AND ifnull(PT_STSFL,'')<> 'X'";
				if(txtBNKCD1.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtBNKCD1.getText().trim().toUpperCase()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT");
			}
		
			if(M_objSOURC==txtMOPCD)
			{
				M_strHLPFLD="txtMOPCD";
				M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
				+ " AND CMT_CGSTP = 'MRXXPMM'  AND ifnull(CMT_STSFL,'') <> 'X'";
				if(txtMOPCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtMOPCD.getText().trim()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Payment Code", "Description"},2,"CT");
			}
			
			if(M_objSOURC==txtMOPCD1)
			{
				M_strHLPFLD="txtMOPCD1";
				M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
				+ " AND CMT_CGSTP = 'MRXXPMM'  AND ifnull(CMT_STSFL,'') <> 'X'";
				if(txtMOPCD1.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtMOPCD1.getText().trim()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Payment Code", "Description"},2,"CT");
			}
			
			if(M_objSOURC==txtDOCNO)
			{
				M_strHLPFLD="txtDOCNO";
				M_strSQLQRY="Select distinct PR_DOCNO from MR_PRTRN where PR_CMPCD ='"+strCMPCD+"' AND PR_DOCTP= '"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'AND ifnull(PR_STSFL,'') <> 'X'";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					M_strSQLQRY += "And PR_STSFL ='0' ";
				else
					M_strSQLQRY += "And PR_STSFL <>'X' ";
				if(txtDOCNO.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PR_DOCNO LIKE '"+txtDOCNO.getText().trim()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Document No"},1,"CT");
			}
			if(M_objSOURC==txtINVNO)
			{
				String L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
				L_strMKTTP=L_strMKTTP.substring(0,2);
				M_strHLPFLD="txtINVNO";
				M_strSQLQRY="SELECT DISTINCT PL_DOCNO,PL_DOCDT,PL_DOCVL,(PL_DOCVL -PL_ADJVL) PT_AVLVL,IVT_CNSCD,IVT_DSRCD,IVT_BYRCD ,PL_ADJVL,PL_DOCTP  ";
				M_strSQLQRY +=" FROM MR_PLTRN,MR_IVTRN WHERE PL_DOCNO = IVT_INVNO AND PL_CMPCD='"+ strCMPCD +"' ";
				M_strSQLQRY += " AND PL_MKTTP='"+L_strMKTTP+"' AND (PL_DOCVL -PL_ADJVL)>0 ";
				M_strSQLQRY += " AND PL_PRTCD ='"+txtPRTCD.getText().trim() +"' ";
				M_strSQLQRY += " AND PL_DOCTP like '"+strDOCTP+"%'";
				
				if(txtINVNO.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PL_DOCNO LIKE '"+txtINVNO.getText().trim()+"%'"; 
				}
			
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date","Doc. Value","Outstanding","Consignee","Distributer","Buyer","Adj. Value","Doc Type"},9,"CT",L_inaCOLSZ);
			}
			
			if(M_objSOURC==txtINVNO1)
			{
				M_strHLPFLD="txtINVNO1";
				String L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
				L_strMKTTP=L_strMKTTP.substring(0,2);
				M_strSQLQRY="SELECT DISTINCT PL_DOCNO,PL_DOCDT,PL_DOCVL,(PL_DOCVL -PL_ADJVL) PT_AVLVL,IVT_CNSCD,IVT_DSRCD,IVT_BYRCD ,PL_ADJVL,PL_DOCTP ";
				M_strSQLQRY +=" FROM MR_PLTRN,MR_IVTRN WHERE PL_DOCNO = IVT_INVNO AND PL_DOCTP like '"+strDOCTP+"%'";
				M_strSQLQRY +="	AND PL_CMPCD='"+ strCMPCD +"' AND PL_MKTTP='"+L_strMKTTP+"'  AND (PL_DOCVL -PL_ADJVL)>0 ";
				M_strSQLQRY += " AND PL_PRTCD ='"+txtPRTCD.getText().trim() +"' ";
				
				if(txtINVNO1.getText().trim().length()>0)
				{
					M_strSQLQRY += " AND PL_DOCNO LIKE '"+txtINVNO1.getText().trim()+"%'"; 
				}
			
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date","Doc. Value","Outstanding","Consignee","Distributer","Buyer","Adj. Value","Doc Type"},9,"CT",L_inaCOLSZ);
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC==cmbMKTTP)
				{				
					cmbPMTTP.requestFocus();
				}
				if(M_objSOURC==cmbPMTTP)
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						txtDOCNO.requestFocus();
					else
						txtDSRCD.requestFocus();
				}
				if(M_objSOURC==txtDSRCD)
				{				
					txtPRTCD.requestFocus();
				}
				if(M_objSOURC==txtPRTCD)
				{				
					
					if(tbpMAIN.getSelectedIndex()==0)
						txtDOCDT.requestFocus();
					else
						txtDOCDT1.requestFocus();
				}
				
				if(M_objSOURC==txtDOCNO)
				{	
					dspDATA();		
					txtDOCDT.requestFocus();
				}
				if(M_objSOURC==txtDOCDT)
				{		
					txtRECAM.requestFocus();
				}
				if(M_objSOURC==txtRECAM)
				{	
					tblRECEN.setRowSelectionInterval(tblRECEN.getSelectedRow(),tblRECEN.getSelectedRow());		
					tblRECEN.setColumnSelectionInterval(TB1_MOPCD,TB1_MOPCD);		
					tblRECEN.editCellAt(tblRECEN.getSelectedRow(),TB1_MOPCD);
					tblRECEN.cmpEDITR[TB1_MOPCD].requestFocus();
				}
								
			//	if(M_objSOURC==txtDOCTP1)
			//	{
			//		txtDOCDT1.requestFocus();
			//	}
				if(M_objSOURC==txtDOCDT1)
				{
					txtINVNO1.requestFocus();
				}
				if(M_objSOURC==txtINVNO1)
				{
					txtMOPCD1.requestFocus();
				}
				if(M_objSOURC==txtMOPCD1)
				{
					txtCHQNO1.requestFocus();
				}
				if(M_objSOURC==txtCHQNO1)
				{
					txtCHQDT1.requestFocus();
				}
				if(M_objSOURC==txtCHQDT1)
				{
					txtBNKCD1.requestFocus();
				}
				if(M_objSOURC==txtBNKCD1)
				{
					txtCHQAM1.requestFocus();
				}
				if(M_objSOURC==txtCHQAM1)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
			catch(Exception L_EK)
			{
				setMSG(L_EK,"Key Evelnt");
			}
		}
	}
	
	/**
	 * Method to execute F1 help for the selected field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtDSRCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				
				txtDSRCD.setText(L_STRTKN1.nextToken());
				L_STRTKN1.nextToken();
				txtDSRNM.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtPRTCD.setText(L_STRTKN1.nextToken());
				txtPRTNM.setText(L_STRTKN1.nextToken());
				lblPRTBL.setText(L_STRTKN1.nextToken()+"  "+L_STRTKN1.nextToken());
				//lblYTDFL.setText(L_STRTKN1.nextToken());
			}
			/*if(M_strHLPFLD == "txtDOCTP")
			{
				txtDOCTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtDOCTP1")
			{
				txtDOCTP1.setText(cl_dat.M_strHLPSTR_pbst);
			}*/
			if(M_strHLPFLD == "txtMOPCD")
			{
				txtMOPCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtMOPCD1")
			{
				txtMOPCD1.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtDOCNO")
			{
				txtDOCNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtDOCNO1")
			{
				txtDOCNO1.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtBNKCD")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtBNKCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblRECEN.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblRECEN.getSelectedRow(),TB1_BNKNM);
			}
			if(M_strHLPFLD == "txtBNKCD1")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtBNKCD1.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtBNKNM1.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());	
			}
			if(M_strHLPFLD == "txtINVNO")
			{
				
				double L_dblRECVL=Double.parseDouble(txtRECAM.getText().trim());
					//System.out.println("Value rect"+L_dblRECVL);
					
				L_dblRECVL=L_dblRECVL-dblADJVL;
				
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtINVNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblRECAD.getSelectedRow(),TB2_DOCDT);
				tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblRECAD.getSelectedRow(),TB2_INVVL);
				//tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),tblRECAD.getSelectedRow(),TB2_AVLVL);
				double L_dblAVLVL=Double.parseDouble(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());
				//System.out.println("Value"+L_dblAVLVL);
				if(L_dblAVLVL<=L_dblRECVL)
				{
					tblRECAD.setValueAt(setNumberFormat(L_dblAVLVL,2),tblRECAD.getSelectedRow(),TB2_ADJVL);
					dblADJVL=dblADJVL+L_dblAVLVL;
				}
				else
				{
					tblRECAD.setValueAt(setNumberFormat(L_dblRECVL,2),tblRECAD.getSelectedRow(),TB2_ADJVL);
				}
					
				
				
				tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),tblRECAD.getSelectedRow(),TB2_AVLVL);
				tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),7)).trim(),tblRECAD.getSelectedRow(),TB2_ADJSH);
				tblRECAD.setValueAt((String)hstPARTY.get("C"+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4))),tblRECAD.getSelectedRow(),TB2_CNSNM);
				String L_strCNCNM=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4));
				tblRECAD.setValueAt((String)hstPARTY.get("D"+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5))),tblRECAD.getSelectedRow(),TB2_DSRNM);
				tblRECAD.setValueAt((String)hstPARTY.get("C"+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),6))),tblRECAD.getSelectedRow(),TB2_BYRNM);
				tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),8)).trim(),tblRECAD.getSelectedRow(),TB2_DOCTP);
					//tblRECAD.setValueAt(M_rstRSSET.getString("PA_ADJVL"),i,TB2_ADJSH);
				tblRECAD.setValueAt( new Boolean(true),tblRECAD.getSelectedRow(),TB2_CHKFL);
				txtINVNO.requestFocus();
			}
			if(M_strHLPFLD == "txtINVNO1")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtINVNO1.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtINVDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());	
				
				txtINVAM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());	
				txtOTSAM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());
				txtPLDOC.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),8)).trim());	
			}
		}
		catch(Exception e)                  
		{
			setMSG(e,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	boolean vldDATA()
	{
		double L_dblTOTVL=0.000;
		if(txtPRTCD.getText().trim().length() ==0)
    	{
			txtPRTCD.requestFocus();
    		setMSG("Enter the Party Code",'E');
    		return false;
    	}
		if(tbpMAIN.getSelectedIndex()==1)
		{
			/*if(txtDOCTP1.getText().trim().length()!=2)
			{
				txtDOCTP1.requestFocus();
				setMSG("Please Enter Payment Type",'E');
    			return false;
			}*/
			if(txtDOCDT1.getText().trim().length()==0)
			{
				setMSG("Date can't be Blank",'E');
				return false;
			}
			if(txtINVNO1.getText().trim().length()!=8)
			{
				txtINVNO1.requestFocus();
				setMSG("Please Enter Proper Invoice Number",'E');
    			return false;
			}
			if(txtMOPCD1.getText().trim().length()!=2)
			{
				txtMOPCD1.requestFocus();
				setMSG("Please Enter payment Mode",'E');
    			return false;
			}
			if(txtCHQNO1.getText().trim().length()==0)
			{
				txtCHQNO1.requestFocus();
				setMSG("Cheque Number can't be Blank",'E');
    			return false;
			}
			if(txtBNKCD1.getText().trim().length()!=5)
			{
				txtBNKCD1.requestFocus();
				setMSG("Invalid Bank Code",'E');
    			return false;
			}
			if(txtCHQAM1.getText().trim().length()==0)
			{
				txtCHQAM1.requestFocus();
				setMSG("Cheque Amount Can't be Blank",'E');
    			return false;
			}
			
			if(txtCHQAM1.getText().trim().length()!=0)
			{
				double L_dblOTSAM=Double.parseDouble(txtOTSAM.getText().trim());
				double L_dblCHQAM=Double.parseDouble(txtCHQAM1.getText().trim());
				if((cmbPMTTP.getSelectedItem().toString()).substring(0,2).equals("13"))
				{
					if(L_dblCHQAM<=L_dblOTSAM)
						return true;
					else
					{
						txtCHQAM1.requestFocus();
						setMSG("Cheque Amount Can't be More then out satanding Amount",'E');
    					return false;
					}
				}
			}
		}
		else 
		{
			if(txtDOCDT.getText().trim().length()==0)
			{
				setMSG("Date can't be Blank",'E');
				return false;
			}
			
			/*if(txtDOCTP.getText().trim().length()!=2)
			{
				txtDOCTP.requestFocus();
				setMSG("Please Enter Payment Type",'E');
    			return false;
			}*/
			if(txtRECAM.getText().trim().length() ==0)
    		{
				txtRECAM.requestFocus();
    			setMSG("Please Enter Receipt Amount",'E');
    			return false;
    		}
			for(int i=0;i<tblRECEN.getRowCount();i++)
    		{
				if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
    			{
					strTEMP = nvlSTRVL(tblRECEN.getValueAt(i,TB1_MOPCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Payment Mode Can not be Blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblRECEN.getValueAt(i,TB1_CHQNO).toString(),"");
    				if(strTEMP.length() ==0)
    				{
    					setMSG("Cheque Number can't be Blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblRECEN.getValueAt(i,TB1_CHQDT).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Cheque Date Can not be Blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblRECEN.getValueAt(i,TB1_BNKCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Bank Code Can not be Blank..",'E');
						return false;
    				}
				}
				if(tblRECEN.isEditing())
    			{
    				if(tblRECEN.getValueAt(tblRECEN.getSelectedRow(),tblRECEN.getSelectedColumn()).toString().length()>0)
    				{
    					TBLINPVF obj=new TBLINPVF();
    					obj.setSource(tblRECEN);
    					if(obj.verify(tblRECEN.getSelectedRow(),tblRECEN.getSelectedColumn()))
    						tblRECEN.getCellEditor().stopCellEditing();
    					else
    						return false;
    				}
				}
			}
			for(int i=0;i<tblRECAD.getRowCount();i++)
    		{
				if(tblRECAD.getValueAt(i,TB2_CHKFL).toString().equals("true"))
    			{
					strTEMP = nvlSTRVL(tblRECAD.getValueAt(i,TB2_DOCNO).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Document Number Can not be Blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblRECAD.getValueAt(i,TB2_ADJVL).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Adjustment Value Can not be Blank..",'E');
						return false;
    				}
				}
				if(tblRECAD.isEditing())
    			{
    				if(tblRECAD.getValueAt(tblRECAD.getSelectedRow(),tblRECAD.getSelectedColumn()).toString().length()>0)
    				{
    					TBLINPVF obj=new TBLINPVF();
    					obj.setSource(tblRECAD);
    					if(obj.verify(tblRECAD.getSelectedRow(),tblRECAD.getSelectedColumn()))
    						tblRECAD.getCellEditor().stopCellEditing();
    					else
    						return false;
    				}
				}
			}
			double L_dblRECVL=Double.parseDouble(txtRECAM.getText().trim());
			for(int i=0 ;i<=tblRECEN.getRowCount();i++)
			{
				if(tblRECEN.getValueAt(i,TB1_RCTVL).toString().length()==0)
					if(tblRECEN.getValueAt(i+1,TB1_RCTVL).toString().length()==0)
						if(tblRECEN.getValueAt(i+2,TB1_RCTVL).toString().length()==0)
							break;
				L_dblTOTVL +=Double.parseDouble(tblRECEN.getValueAt(i,TB1_RCTVL).toString());
			}
			if(L_dblRECVL!=L_dblTOTVL)
			{
				txtRECAM.requestFocus();
				setMSG("Please Check The Amount",'E');
				return false;
			}
		}
		return true;
	}
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	*/
	void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			dblADJVL=0.0;
			if(!vldDATA())
			{
				return;
			}
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				
				delPAYMT();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				modPAYMT();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				double L_dblADJVL=0.0;
				//double L_dblCHQAM=0.0;
				//double L_dblOTSAM=0.0;
				
				String L_strADJVL="",L_strDOCNO="",L_strINVNO="",L_strMKTTP="",L_strGRPCD="";
				L_strDOCNO=genDOCNO();
				if(L_strDOCNO !=null)
				{
					L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
					L_strMKTTP=L_strMKTTP.substring(0,2);
					
					if(tbpMAIN.getSelectedIndex()==1)
					{
						//L_dblCHQAM =Double.parseDouble(txtCHQAM1.getText().trim());
						//L_dblOTSAM =Double.parseDouble(txtOTSAM.getText().trim());
						
						strSTSFL="1";
						
						String L_strSQLQRY="Select PT_GRPCD from CO_PTMST where  PT_PRTTP='C' AND PT_PRTCD='"+txtPRTCD.getText().trim()+"' AND ifnull(PT_STSFL,'')<> 'X'";
						M_rstRSSET=cl_dat.exeSQLQRY(L_strSQLQRY);
						if(M_rstRSSET !=null && M_rstRSSET.next())
						{
							L_strGRPCD=M_rstRSSET.getString("PT_GRPCD");
							M_rstRSSET.close();
						}
												
						M_strSQLQRY="Insert into MR_PRTRN(PR_CMPCD,PR_PRTTP,PR_MKTTP,PR_PRTCD,PR_GRPCD,PR_DOCTP,PR_DOCNO,PR_DOCDT,PR_BNKCD,PR_CHQNO,PR_CHQDT,PR_MOPCD,PR_RCTVL,PR_SBSCD,PR_TRNFL,PR_STSFL,PR_LUSBY,PR_LUPDT ) values ( ";
						M_strSQLQRY+="'"+strCMPCD+"',";
						M_strSQLQRY+="'C',";
						M_strSQLQRY+="'"+L_strMKTTP+"',";
						M_strSQLQRY+="'"+txtPRTCD.getText().trim() +"',";
						M_strSQLQRY+="'"+L_strGRPCD+"',";
						M_strSQLQRY+="'"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2) +"',";
						M_strSQLQRY+="'"+L_strDOCNO+"',";
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOCDT1.getText().trim())) +"',";
						
						M_strSQLQRY+="'"+txtBNKCD1.getText().trim()+"',";
						M_strSQLQRY+="'"+txtCHQNO1.getText().trim()+"',";
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHQDT1.getText().trim()))+ "',";
						M_strSQLQRY+="'"+txtMOPCD1.getText().trim()+"',";
						M_strSQLQRY+=""+txtCHQAM1.getText() +",";
						M_strSQLQRY+="'"+M_strSBSCD+"',";
							
						M_strSQLQRY += getUSGDTL("PR_",'I',strSTSFL)+")";
						
						//System.out.println("INSERT PRTRN ="+M_strSQLQRY);
						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
						M_strSQLQRY="insert into MR_PATRN(PA_CMPCD,PA_PRTTP,PA_MKTTP,PA_PRTCD,PA_GRPCD,PA_CRDTP,PA_CRDNO,PA_DBTTP,PA_DBTNO,PA_ADJVL,PA_SBSCD,PA_TRNFL,PA_STSFL,PA_LUSBY,PA_LUPDT) values ( ";
						M_strSQLQRY+= "'"+strCMPCD+"',";
						M_strSQLQRY+= "'C',";
						M_strSQLQRY+="'"+L_strMKTTP+"',";
						M_strSQLQRY+= "'"+txtPRTCD.getText().trim() +"',";
						M_strSQLQRY+="'"+L_strGRPCD+"',";
						M_strSQLQRY+= "'"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2) +"',";
						M_strSQLQRY+= "'"+L_strDOCNO+"',";
						M_strSQLQRY+= "'"+ txtPLDOC.getText().trim()+"',";
						M_strSQLQRY+= "'"+txtINVNO1.getText().trim()+"',";
						M_strSQLQRY+= ""+txtCHQAM1.getText().trim()+",";	
						M_strSQLQRY+= "'"+M_strSBSCD+"',";
						M_strSQLQRY += getUSGDTL("MR_",'I',strSTSFL)+")";
						
						//System.out.println("INSERT PATRN ="+M_strSQLQRY);
						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						L_dblADJVL =Double.parseDouble(txtCHQAM1.getText().trim());
		
						M_strSQLQRY="UPDATE MR_PLTRN SET PL_ADJVL= PL_ADJVL +"+L_dblADJVL+" , ";
						M_strSQLQRY +="PL_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						M_strSQLQRY +="PL_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
						M_strSQLQRY += " WHERE PL_CMPCD = '" +strCMPCD+ "'";
						M_strSQLQRY +=" AND PL_PRTTP='C'";
						M_strSQLQRY += " AND PL_PRTCD = '" +txtPRTCD.getText().trim() + "'";
						M_strSQLQRY += " AND PL_DOCTP = '" +txtPLDOC.getText().trim()+ "'";
						M_strSQLQRY += " AND PL_DOCNO = '" +txtINVNO1.getText().trim() +"'";
						
						//System.out.println("PUDATE PLTRN ="+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
					}
					else
					{
						for(int i=0;i<tblRECEN.getRowCount();i++)
						{
							if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
							{
								//System.out.println("INSERT i ="+i);
								insRCTDATA(i);
							}
						}
						for(int i=0;i<tblRECAD.getRowCount();i++)
						{
							if(tblRECAD.getValueAt(i,TB2_CHKFL).toString().equals("true"))
							{
								
								insADJDATA(i);
								
							}
						}
					}
					updDOCNO(L_strDOCNO);
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						calSTAMT.setString(1,strCMPCD);
						calSTAMT.setString(2,L_strMKTTP);
						calSTAMT.setString(3,L_strDOCNO);
						calSTAMT.executeUpdate();
						shoDATA();
						cl_dat.M_conSPDBA_pbst.commit();
						txtDOCNO1.setText(L_strDOCNO);
						txtDOCNO.setText(L_strDOCNO);
						setMSG("Data saved successfully",'N');
						strSTSFL="0";
					}
				}
				else
				{
					setMSG("Error in saving",'E');
				}
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	 	setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	private void insRCTDATA(int P_intROWNO)
	{
		try
		{
			double L_dblADJVL=0.0;
			//System.out.println("IN FUNCTION"+P_intROWNO);
			String L_strADJVL="",L_strDOCNO="",L_strINVNO="",L_strMKTTP="",L_strGRPCD="";
				
			L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
			L_strMKTTP=L_strMKTTP.substring(0,2);
							
			double L_dblRECVL = Double.parseDouble(txtRECAM.getText().trim());
			if(txtADJAM.getText().trim().length()>0)
			L_dblADJVL = Double.parseDouble(txtADJAM.getText().trim());
			if(L_dblRECVL==L_dblADJVL)
				strSTSFL="1";
			else
				strSTSFL="0";		
			String L_strSQLQRY="Select PT_GRPCD from CO_PTMST where  PT_PRTTP='C' AND PT_PRTCD='"+txtPRTCD.getText().trim()+"' AND ifnull(PT_STSFL,'')<> 'X'";
			M_rstRSSET=cl_dat.exeSQLQRY(L_strSQLQRY);
			if(M_rstRSSET !=null && M_rstRSSET.next())
			{
				L_strGRPCD=M_rstRSSET.getString("PT_GRPCD");
				M_rstRSSET.close();
			}
			
			M_strSQLQRY="Insert into MR_PRTRN(PR_CMPCD,PR_PRTTP,PR_MKTTP,PR_PRTCD,PR_GRPCD,PR_DOCTP,PR_DOCNO,PR_DOCDT,PR_BNKCD,PR_CHQNO,PR_CHQDT,PR_MOPCD,PR_RCTVL,PR_SBSCD,PR_TRNFL,PR_STSFL,PR_LUSBY,PR_LUPDT ) values ( ";
			M_strSQLQRY+="'"+strCMPCD+"',";
			M_strSQLQRY+="'C',";
			M_strSQLQRY+="'"+L_strMKTTP+"',";
			M_strSQLQRY+="'"+txtPRTCD.getText().trim() +"',";
			M_strSQLQRY+="'"+L_strGRPCD+"',";
			M_strSQLQRY+="'"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+"',";
			M_strSQLQRY+="'"+txtDOCNO.getText().trim() +"',";
			M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOCDT.getText().trim())) +"',";
			M_strSQLQRY+="'"+tblRECEN.getValueAt(P_intROWNO,TB1_BNKCD).toString() +"',";
			M_strSQLQRY+="'"+tblRECEN.getValueAt(P_intROWNO,TB1_CHQNO).toString() +"',";
			M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRECEN.getValueAt(P_intROWNO,TB1_CHQDT).toString()))+ "',";
			M_strSQLQRY+="'"+tblRECEN.getValueAt(P_intROWNO,TB1_MOPCD).toString() +"',";
			M_strSQLQRY+=""+tblRECEN.getValueAt(P_intROWNO,TB1_RCTVL).toString() +",";
			M_strSQLQRY+="'"+M_strSBSCD+"',";
								
			M_strSQLQRY += getUSGDTL("PR_",'I',strSTSFL)+")";
			
			//System.out.println("InsertMR_PRTRN = "+M_strSQLQRY);
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
		}
	}
	private void insADJDATA(int P_intROWNO)
	{
		try
		{
			double L_dblADJVL=0.0;
			//System.out.println("IN ADJUST DATA"+P_intROWNO);
			String L_strADJVL="",L_strDOCNO="",L_strINVNO="",L_strMKTTP="",L_strGRPCD="";
				
			L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
			L_strMKTTP=L_strMKTTP.substring(0,2);
			
			double L_dblRECVL = Double.parseDouble(txtRECAM.getText().trim());
			if(txtADJAM.getText().trim().length()>0)
			L_dblADJVL = Double.parseDouble(txtADJAM.getText().trim());
			if(L_dblRECVL==L_dblADJVL)
				strSTSFL="1";
			else
				strSTSFL="0";		
			
			String L_strSQLQRY="Select PT_GRPCD from CO_PTMST where  PT_PRTTP='C' AND PT_PRTCD='"+txtPRTCD.getText().trim()+"' AND ifnull(PT_STSFL,'')<> 'X'";
			M_rstRSSET=cl_dat.exeSQLQRY(L_strSQLQRY);
			if(M_rstRSSET !=null && M_rstRSSET.next())
			{
				L_strGRPCD=M_rstRSSET.getString("PT_GRPCD");
			}
			
			M_strSQLQRY="insert into MR_PATRN(PA_CMPCD,PA_PRTTP,PA_MKTTP,PA_PRTCD,PA_GRPCD,PA_CRDTP,PA_CRDNO,PA_DBTTP,PA_DBTNO,PA_ADJVL,PA_SBSCD,PA_TRNFL,PA_STSFL,PA_LUSBY,PA_LUPDT) values ( ";
			M_strSQLQRY+= "'"+strCMPCD+"',";
			M_strSQLQRY+= "'C',";
			M_strSQLQRY+="'"+L_strMKTTP+"',";
			M_strSQLQRY+= "'"+txtPRTCD.getText().trim() +"',";
			M_strSQLQRY+="'"+L_strGRPCD+"',";
			M_strSQLQRY+= "'"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+"',";
			M_strSQLQRY+= "'"+txtDOCNO.getText().trim() +"',";
			M_strSQLQRY+= "'"+ tblRECAD.getValueAt(P_intROWNO,TB2_DOCTP).toString() +"',";
			M_strSQLQRY+= "'"+tblRECAD.getValueAt(P_intROWNO,TB2_DOCNO).toString() +"',";
			M_strSQLQRY+= ""+tblRECAD.getValueAt(P_intROWNO,TB2_ADJVL).toString() +",";	
			M_strSQLQRY+= "'"+M_strSBSCD+"',";
			M_strSQLQRY += getUSGDTL("MR_",'I',strSTSFL)+")";
				
			//System.out.println("Insert = "+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
			L_dblADJVL =Double.parseDouble(tblRECAD.getValueAt(P_intROWNO,TB2_ADJVL).toString());
			L_strINVNO = nvlSTRVL(tblRECAD.getValueAt(P_intROWNO,TB2_DOCNO).toString(),"");
											
			M_strSQLQRY="UPDATE MR_PLTRN SET PL_ADJVL= PL_ADJVL +"+L_dblADJVL+" , ";
			M_strSQLQRY +="PL_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
			M_strSQLQRY +="PL_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
			M_strSQLQRY += " WHERE PL_CMPCD = '" +strCMPCD+ "'";
			M_strSQLQRY +=" AND PL_PRTTP='C'";
			M_strSQLQRY += " AND PL_PRTCD = '" +txtPRTCD.getText().trim() + "'";
			M_strSQLQRY += " AND PL_DOCTP = '" +tblRECAD.getValueAt(P_intROWNO,TB2_DOCTP).toString() + "'";
			M_strSQLQRY += " AND PL_DOCNO = '" +L_strINVNO +"'";
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");


		}	
	}
	
	private void shoDATA()
	{
		try
		{
			int intTBIND=tbpMAIN.getSelectedIndex();
			String L_strPRTNM=txtPRTNM.getText().trim();
			String L_strPRTCD=txtPRTCD.getText().trim();
			String L_strDSRCD=txtDSRCD.getText().trim();
			String L_strDSRNM=txtDSRNM.getText().trim();
			String L_strDOCDT1=txtDOCDT1.getText().trim();
			String L_strBNKCD=txtBNKCD1.getText().trim();
			String L_strMOPCD =txtMOPCD1.getText().trim();
					
			String L_strDOCDT=txtDOCDT.getText().trim();
			//String L_strDOCTP=txtDOCTP.getText().trim();
			//String L_strDOCTP1=txtDOCTP1.getText().trim();
			String L_strBNKNM1=txtBNKNM1.getText().trim();
						
			clrCOMP();
			tbpMAIN.setSelectedIndex(intTBIND);
			txtPRTCD.setText(L_strPRTCD);
			txtDSRCD.setText(L_strDSRCD);
			txtPRTNM.setText(L_strPRTNM);
			txtDSRNM.setText(L_strDSRNM);
			txtMOPCD1.setText(L_strMOPCD);
			txtDOCDT1.setText(L_strDOCDT1);
			txtBNKCD1.setText(L_strBNKCD);
			txtDOCDT.setText(L_strDOCDT);
			//txtDOCTP.setText(L_strDOCTP);
			//txtDOCTP1.setText(L_strDOCTP1);
			txtBNKNM1.setText(L_strBNKNM1);
						
			lblTOTVL.setText("");
			//lblPRTBL.setText("");
						
			M_strSQLQRY="Select distinct PT_YTDVL,PT_YTDFL from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C' And PT_PRTCD='"+ txtPRTCD.getText().trim()+"'";
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						
			if(L_rstRSSET!=null &&L_rstRSSET.next())
			{
			
				lblPRTBL.setText(nvlSTRVL(L_rstRSSET.getString("PT_YTDVL"),"")+"  "+nvlSTRVL(L_rstRSSET.getString("PT_YTDFL"),""));
				//System.out.println("Lable value = "+lblPRTBL.getText());
				L_rstRSSET.close();
			}	
			else
			{
				lblPRTBL.setText("");
			}
		}
		catch(Exception e)
		{}
	}
	
	
	/**
	 *  Function For display The Data On the Screen
	*/
	private void dspDATA()
	{
		intRWCNT=0;
		java.sql.Date L_datTMPDT;
		java.sql.Date L_datTMPDT1;
		int i=0;
		String L_strPRTCD="";
		String L_strBNKCD="";
		String L_strRCTVL="";
		String L_strSRLNO="";
		
		String L_strMKTTP="";
		String L_strPMTTP="";
		String L_strDSRCD="";
		double L_dblRCTVL=0.0;
		try
		{
			tblRECEN.clrTABLE();
			tblRECAD.clrTABLE();
			txtRECAM.setText(setNumberFormat(L_dblRCTVL,2));
			txtADJAM.setText(setNumberFormat(L_dblRCTVL,2));
			txtBALAM.setText(setNumberFormat(L_dblRCTVL,2));
			lblTOTVL.setText(setNumberFormat(L_dblRCTVL,2));
			if(!chkDOCNO())
				return;
			
			M_strSQLQRY = "Select PR_CMPCD,PR_PRTTP,PR_MKTTP,PR_PRTCD,PR_DOCTP,PR_DOCNO,PR_DOCDT,";
			M_strSQLQRY +="PR_BNKCD,PR_CHQNO,PR_CHQDT,PR_MOPCD,PR_RCTVL,PR_ADJVL,PT_DSRCD,PT_YTDVL,PT_YTDFL from MR_PRTRN,CO_PTMST ";
			M_strSQLQRY +="WHERE PR_CMPCD ='"+strCMPCD+"' and PR_PRTTP='C' and PR_DOCNO ='"+txtDOCNO.getText().trim()+ "' ";
			M_strSQLQRY +=" AND PR_PRTTP=PT_PRTTP AND PR_PRTCD= PT_PRTCD and  ifnull(PR_STSFL,'') <> 'X'";
			//System.out.println(M_strSQLQRY);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			{
				M_strSQLQRY +="	and PR_STSFL <> 'X'";
			}
            else
				M_strSQLQRY +="	and PR_STSFL not in('1','X')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			
			clrCOMP();
			if(tblRECEN.isEditing())
			tblRECEN.getCellEditor().stopCellEditing();
			tblRECEN.setRowSelectionInterval(0,0);
			tblRECEN.setColumnSelectionInterval(0,0);
			
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					txtDOCNO.setText(nvlSTRVL(M_rstRSSET.getString("PR_DOCNO"),""));
					lblPRTBL.setText(nvlSTRVL(M_rstRSSET.getString("PT_YTDVL"),"")+"  "+nvlSTRVL(M_rstRSSET.getString("PT_YTDFL"),""));
					//lblYTDFL.setText(nvlSTRVL(M_rstRSSET.getString("PT_YTDFL"),""));
					L_datTMPDT1 = M_rstRSSET.getDate("PR_DOCDT");
					
					if(L_datTMPDT1 !=null)
					{
						txtDOCDT.setText(M_fmtLCDAT.format(L_datTMPDT1));
					}
					else
						txtDOCDT.setText("");
					L_datTMPDT = M_rstRSSET.getDate("PR_CHQDT");
					if(L_datTMPDT !=null)
					{
						tblRECEN.setValueAt(M_fmtLCDAT.format(L_datTMPDT),i,TB1_CHQDT);						
					}
					else
						tblRECEN.setValueAt("",i,TB1_CHQDT);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				    {
						tblRECEN.cmpEDITR[TB1_CHKFL].setEnabled(true);
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				    {
						tblRECEN.setValueAt( new Boolean(true),i,TB1_CHKFL);
						tblRECEN.cmpEDITR[TB1_CHKFL].setEnabled(false);
					}
					L_strPRTCD=nvlSTRVL(M_rstRSSET.getString("PR_PRTCD"),"");
					txtPRTCD.setText(L_strPRTCD);
					txtPRTNM.setText(nvlSTRVL(hstPARTY.get("C"+L_strPRTCD).toString(),""));
					
					L_strDSRCD=nvlSTRVL(M_rstRSSET.getString("PT_DSRCD"),"");
					txtDSRCD.setText(L_strDSRCD);
					txtDSRNM.setText(nvlSTRVL(hstPARTY.get("D"+L_strDSRCD).toString(),""));
					
					
				//	L_strMKTTP=M_rstRSSET.getString("PR_MKTTP");
					L_strMKTTP=nvlSTRVL(M_rstRSSET.getString("PR_MKTTP"),"");
					
					L_strBNKCD=M_rstRSSET.getString("PR_BNKCD");
					tblRECEN.setValueAt(hstPARTY.get("B"+L_strBNKCD),i,TB1_BNKNM);
					tblRECEN.setValueAt(L_strBNKCD,i,TB1_BNKCD);
					//txtDOCTP.setText(nvlSTRVL(M_rstRSSET.getString("PR_DOCTP"),""));
					L_strPMTTP=nvlSTRVL(M_rstRSSET.getString("PR_DOCTP"),"");
					tblRECEN.setValueAt(M_rstRSSET.getString("PR_MOPCD"),i,TB1_MOPCD);
					tblRECEN.setValueAt(M_rstRSSET.getString("PR_CHQNO"),i,TB1_CHQNO);
					L_strRCTVL=M_rstRSSET.getString("PR_RCTVL");
					tblRECEN.setValueAt(L_strRCTVL,i,TB1_RCTVL);
					L_dblRCTVL +=Double.parseDouble(L_strRCTVL);
					i++;
					intRWCNT++;
					//System.out.println("DSPPAYADJ"+intRWCNT);
				}
				for(int j=0;j<cmbMKTTP.getItemCount();j++)
				{
					if(L_strMKTTP.equals((cmbMKTTP.getItemAt(j).toString()).substring(0,2)))
					{
						cmbMKTTP.setSelectedIndex(j);
					}
				}
				for(int j=0;j<cmbPMTTP.getItemCount();j++)
				{
					if(L_strPMTTP.equals((cmbPMTTP.getItemAt(j).toString()).substring(0,2)))
					{
						cmbPMTTP.setSelectedIndex(j);
					}
				}
			}
			txtRECAM.setText(setNumberFormat(L_dblRCTVL,2));
			lblTOTVL.setText(setNumberFormat(L_dblRCTVL,2));
			if(intRWCNT==0)
			{
				setMSG("Data has been Locked for Further Modification",'E');
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			//System.out.println("DSPPAYADJ");
			dspPAYADJ();
		}
		catch(Exception E)
		{
			setMSG(E ,"dspDATA");
		}
	}
	/**
	 * Function To display data in Receipt Adjustment table
	*/
	private void dspPAYADJ()
	{
		try
		{
			int i=0;
			intRWADJ=0;
			String L_strADJVL="";
			double L_dblADJVL=0.0;
			java.sql.Date L_datTMPDT;
			M_strSQLQRY = "Select distinct PA_DBTNO,PA_ADJVL,PA_DBTTP,PA_LUPDT,IVT_CNSCD,IVT_DSRCD,IVT_BYRCD,PL_DOCDT,PL_DOCVL,(PL_DOCVL-PL_ADJVL) PL_AVLVL ";
			M_strSQLQRY +="  from MR_PATRN,MR_IVTRN,MR_PLTRN WHERE PA_DBTNO = IVT_INVNO and PA_DBTNO=PL_DOCNO and PA_CMPCD = '" +strCMPCD + "' ";
			M_strSQLQRY +=" and PA_PRTTP='C' and PA_PRTCD=PL_PRTCD and PA_PRTCD='"+txtPRTCD.getText().trim()+"' and PA_CRDNO = '"+txtDOCNO.getText().trim()+ "' ";
			M_strSQLQRY +=" and PA_CRDTP= '"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'  and  ifnull(PA_STSFL,'') <> 'X'";
		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
				M_strSQLQRY +="	and PA_STSFL <> 'X'";
			else
				M_strSQLQRY +="	and PA_STSFL not in('1','X')";
			
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);	
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		    {
				tblRECAD.cmpEDITR[TB2_CHKFL].setEnabled(true);
			}
			if(tblRECAD.isEditing())
			tblRECAD.getCellEditor().stopCellEditing();
			tblRECAD.setRowSelectionInterval(0,0);
			tblRECAD.setColumnSelectionInterval(0,0);
		
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				    {
						tblRECAD.setValueAt( new Boolean(true),i,TB2_CHKFL);
					}
					tblRECAD.setValueAt(M_rstRSSET.getString("PA_DBTNO"),i,TB2_DOCNO);
					tblRECAD.setValueAt(M_rstRSSET.getString("PA_DBTTP"),i,TB2_DOCTP);
					L_datTMPDT = M_rstRSSET.getDate("PL_DOCDT");
					if(L_datTMPDT !=null)
					{
						tblRECAD.setValueAt(M_fmtLCDAT.format(L_datTMPDT),i,TB2_DOCDT);
					}
					else
						tblRECAD.setValueAt("",i,TB2_DOCDT);
					
					L_strADJVL=M_rstRSSET.getString("PA_ADJVL");
					tblRECAD.setValueAt(L_strADJVL,i,TB2_ADJVL);
					L_dblADJVL +=Double.parseDouble(L_strADJVL);
					
					tblRECAD.setValueAt(M_rstRSSET.getString("PL_DOCVL"),i,TB2_INVVL);
					tblRECAD.setValueAt(M_rstRSSET.getString("PL_AVLVL"),i,TB2_AVLVL);
					tblRECAD.setValueAt(M_rstRSSET.getString("PA_ADJVL"),i,TB2_ADJSH);
					
					tblRECAD.setValueAt((String)hstPARTY.get("C"+M_rstRSSET.getString("IVT_CNSCD")),i,TB2_CNSNM);
					tblRECAD.setValueAt((String)hstPARTY.get("D"+M_rstRSSET.getString("IVT_DSRCD")),i,TB2_DSRNM);
					tblRECAD.setValueAt((String)hstPARTY.get("C"+M_rstRSSET.getString("IVT_BYRCD")),i,TB2_BYRNM);
					i++;
					intRWADJ++;
				}
				txtADJAM.setText(setNumberFormat(L_dblADJVL,2));
				double L_dblRECVL = Double.parseDouble(txtRECAM.getText().trim());
				L_dblADJVL = Double.parseDouble(txtADJAM.getText().trim());
				txtBALAM.setText(setNumberFormat((L_dblRECVL-L_dblADJVL),2));
			}
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception E)
		{
			
		}
	}
	/**
	 *  Function For Modify Record
	 */		   
	private void modPAYMT()
	{
		try
		{
			String L_strMOPCD="",L_strBNKCD="",L_strCHQNO="",L_strCHQDT="",L_strRCTVL="",L_strSRLNO,L_strDOCNO,L_strADJVL;
			String L_strINVNO="",L_strSQLQRY="",L_strMKTTP="";
			double L_dblRECVL = Double.parseDouble(txtRECAM.getText().trim());
			double L_dblADJVL = Double.parseDouble(txtADJAM.getText().trim());
			if(L_dblRECVL==L_dblADJVL)
				strSTSFL="1";
			else
				strSTSFL="0";			
			L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
			L_strMKTTP=L_strMKTTP.substring(0,2);
			for(int i=0;i<tblRECEN.getRowCount();i++)
			{
				if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					L_strMOPCD = nvlSTRVL(tblRECEN.getValueAt(i,TB1_MOPCD).toString(),"");
					L_strCHQNO = nvlSTRVL(tblRECEN.getValueAt(i,TB1_CHQNO).toString(),"");
					L_strCHQDT = nvlSTRVL(tblRECEN.getValueAt(i,TB1_CHQDT).toString(),"");
					L_strBNKCD = nvlSTRVL(tblRECEN.getValueAt(i,TB1_BNKCD).toString(),"");
					L_strRCTVL = nvlSTRVL(tblRECEN.getValueAt(i,TB1_RCTVL).toString(),"");
					if(i<intRWCNT)
					{
						M_strSQLQRY="UPDATE MR_PRTRN SET PR_MOPCD= '"+L_strMOPCD+"' , ";
						M_strSQLQRY +="PR_RCTVL = "+L_strRCTVL+", ";
						M_strSQLQRY +="PR_STSFL = '"+strSTSFL+"', ";
						M_strSQLQRY +="PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						M_strSQLQRY +="PR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
						M_strSQLQRY += " WHERE PR_CMPCD = '" +strCMPCD + "'";
						M_strSQLQRY += " AND PR_PRTTP = 'C'";
						M_strSQLQRY += " AND PR_PRTCD = '" +txtPRTCD.getText().trim() + "'";
						M_strSQLQRY += " AND PR_DOCNO = '" +txtDOCNO.getText().trim() + "'";
						M_strSQLQRY += " AND PR_DOCTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2) + "'";
						M_strSQLQRY += " AND PR_MOPCD = '" + L_strMOPCD + "'";
						M_strSQLQRY += " AND PR_BNKCD = '" + L_strBNKCD + "' ";
						M_strSQLQRY += " AND PR_CHQNO = '" + L_strCHQNO + "' ";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else
					{
						insRCTDATA(i);
					}					
				}
			}
			for(int i=0;i<tblRECAD.getRowCount();i++)
			{
				if(tblRECAD.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					L_strDOCNO = nvlSTRVL(tblRECAD.getValueAt(i,TB2_DOCNO).toString(),"");
					L_strADJVL = nvlSTRVL(tblRECAD.getValueAt(i,TB2_ADJVL).toString(),"");
					
					if(i<intRWADJ)
					{
						M_strSQLQRY="UPDATE MR_PATRN SET PA_ADJVL= "+L_strADJVL+" , ";
						M_strSQLQRY +="PA_STSFL = '"+strSTSFL+"', ";
						M_strSQLQRY +="PA_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						M_strSQLQRY +="PA_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
						M_strSQLQRY += " WHERE PA_CMPCD = '" +strCMPCD+ "'";
						M_strSQLQRY += " AND PA_PRTTP='C'";
						M_strSQLQRY += " AND PA_PRTCD = '" +txtPRTCD.getText().trim() + "'";
						M_strSQLQRY += " AND PA_CRDNO = '" +txtDOCNO.getText().trim() + "'";
						M_strSQLQRY += " AND PA_CRDTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2) + "'";
						M_strSQLQRY += " AND PA_DBTTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
						M_strSQLQRY += " AND PA_DBTNO = '" +L_strDOCNO +"'";
						
						L_dblADJVL =Double.parseDouble(tblRECAD.getValueAt(i,TB2_ADJVL).toString())-Double.parseDouble(tblRECAD.getValueAt(i,TB2_ADJSH).toString());
						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
						L_strINVNO = nvlSTRVL(tblRECAD.getValueAt(i,TB2_DOCNO).toString(),"");
						L_strSQLQRY="UPDATE MR_PLTRN SET PL_ADJVL= PL_ADJVL + "+L_dblADJVL+" , ";
						L_strSQLQRY +="PL_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						L_strSQLQRY +="PL_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
						L_strSQLQRY += " WHERE PL_CMPCD = '" +strCMPCD+ "'";
						L_strSQLQRY +=" AND PL_PRTTP='C'";
						L_strSQLQRY += " AND PL_PRTCD = '" +txtPRTCD.getText().trim() + "'";
						L_strSQLQRY += " AND PL_DOCTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
						L_strSQLQRY += " AND PL_DOCNO = '" +L_strINVNO +"'";
					
						cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
						
					}
					else
					{
						L_strSQLQRY  = " Select PA_PRTCD,PA_DBTTP,PA_CRDNO,PA_DBTNO,pa_adjvl from MR_PATRN";
						L_strSQLQRY += " WHERE PA_CMPCD = '" +strCMPCD+ "'";
						L_strSQLQRY += " AND PA_PRTTP='C'";
						L_strSQLQRY += " AND PA_PRTCD = '" +txtPRTCD.getText().trim() + "'";
						L_strSQLQRY += " AND PA_CRDNO = '" +txtDOCNO.getText().trim() + "'";
						L_strSQLQRY += " AND PA_CRDTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";
						L_strSQLQRY += " AND PA_DBTTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
						L_strSQLQRY += " AND PA_DBTNO = '" +L_strDOCNO +"'";
						
						M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(M_rstRSSET !=null && M_rstRSSET.next())
						{
							M_strSQLQRY="UPDATE MR_PATRN SET PA_ADJVL= "+L_strADJVL+" , ";
							M_strSQLQRY +="PA_STSFL = '"+strSTSFL+"', ";
							M_strSQLQRY +="PA_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
							M_strSQLQRY +="PA_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
							M_strSQLQRY += " WHERE PA_CMPCD = '" +strCMPCD+ "'";
							M_strSQLQRY += " AND PA_PRTTP='C'";
							M_strSQLQRY += " AND PA_PRTCD = '" +txtPRTCD.getText().trim() + "'";
							M_strSQLQRY += " AND PA_CRDNO = '" +txtDOCNO.getText().trim() + "'";
							M_strSQLQRY += " AND PA_CRDTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";
							M_strSQLQRY += " AND PA_DBTTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
							M_strSQLQRY += " AND PA_DBTNO = '" +L_strDOCNO +"'";
							M_rstRSSET.close();
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							
						}
						else
						{
							insADJDATA(i);
						}							
					}
				}
			}
			
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeMOD"))
				{
					L_strDOCNO=txtDOCNO.getText().trim();
				//	calSTAMT =cl_dat.M_conSPDBA_pbst.prepareCall("call  updPLTRN_PMR(?,?,?)");
					calSTAMT.setString(1,strCMPCD);
					calSTAMT.setString(2,L_strMKTTP);
					calSTAMT.setString(3,L_strDOCNO);
					calSTAMT.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();
					//calSTAMT.close();
					strSTSFL="0";
					//rCOMP();
					shoDATA();
					lblTOTVL.setText("");
					setMSG("Data Update successfully",'N');
				}
			}
			else
			{
				setMSG("Error in Updating",'E');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"modPAYMT ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * Function For Deleting Record 
	*/
	private void delPAYMT()
	{
		int L_intSELROW =0;
		String L_strMOPCD="",L_strCHQNO="",L_strBNKCD="",L_strDOCNO="",L_strMKTTP="";
		double L_dblADJVL=0.0;
		String L_strSQLQRY="";
		if(!chkAMOUNT())
			return;
		try
		{
			L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
			L_strMKTTP=L_strMKTTP.substring(0,2);
			for(int i=0;i<tblRECEN.getRowCount();i++)
			{
				if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					L_strMOPCD = nvlSTRVL(tblRECEN.getValueAt(i,TB1_MOPCD).toString(),"");
					L_strCHQNO = nvlSTRVL(tblRECEN.getValueAt(i,TB1_CHQNO).toString(),"");
					L_strBNKCD = nvlSTRVL(tblRECEN.getValueAt(i,TB1_BNKCD).toString(),"");
									
					L_intSELROW++;
					M_strSQLQRY  ="update MR_PRTRN SET ";
					M_strSQLQRY +="PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="PR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', ";
					M_strSQLQRY +="PR_STSFL= 'X'";
					M_strSQLQRY += " WHERE PR_CMPCD = '" +strCMPCD+ "'";
					M_strSQLQRY +=" AND PR_PRTTP='C'";
					M_strSQLQRY += " AND PR_PRTCD = '" +txtPRTCD.getText().trim() + "'";
					M_strSQLQRY += " AND PR_DOCNO = '" +txtDOCNO.getText().trim() + "'";
					M_strSQLQRY += " AND PR_DOCTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";
					M_strSQLQRY += " AND PR_MOPCD = '" + L_strMOPCD + "'";
					M_strSQLQRY += " AND PR_BNKCD = '" + L_strBNKCD + "' ";
					M_strSQLQRY += " AND PR_CHQNO = '" + L_strCHQNO + "' ";

					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			
			for(int i=0;i<tblRECAD.getRowCount();i++)
			{
				if(tblRECAD.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					L_intSELROW++;
					L_strDOCNO = nvlSTRVL(tblRECAD.getValueAt(i,TB2_DOCNO).toString(),"");
					M_strSQLQRY  ="update MR_PATRN SET ";
					M_strSQLQRY +="PA_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="PA_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', ";
					M_strSQLQRY +="PA_STSFL= 'X'";
					M_strSQLQRY += " WHERE PA_CMPCD = '" +strCMPCD+ "'";
					M_strSQLQRY +=" AND PA_PRTTP='C'";
					M_strSQLQRY += " AND PA_PRTCD = '" +txtPRTCD.getText().trim() + "'";
					M_strSQLQRY += " AND PA_CRDNO = '" +txtDOCNO.getText().trim() + "'";
					M_strSQLQRY += " AND PA_CRDTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";
					M_strSQLQRY += " AND PA_DBTTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
					M_strSQLQRY += " AND PA_DBTNO = '" +L_strDOCNO +"'";
					
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					L_dblADJVL =Double.parseDouble(tblRECAD.getValueAt(i,TB2_ADJVL).toString());
																		
					L_strSQLQRY="UPDATE MR_PLTRN SET PL_ADJVL= PL_ADJVL -"+L_dblADJVL+" , ";
					L_strSQLQRY +="PL_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					L_strSQLQRY +="PL_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
					L_strSQLQRY += " WHERE PL_CMPCD = '" +strCMPCD+ "'";
					L_strSQLQRY +=" AND PL_PRTTP='C'";
					L_strSQLQRY += " AND PL_PRTCD = '" +txtPRTCD.getText().trim() + "'";
					L_strSQLQRY += " AND PL_DOCTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
					L_strSQLQRY += " AND PL_DOCNO = '" +L_strDOCNO +"'";
					
					cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("delPAYMT"))
				{
					//calSTAMT =cl_dat.M_conSPDBA_pbst.prepareCall("call  updPLTRN_PMR(?,?,?)");
					calSTAMT.setString(1,strCMPCD);
					calSTAMT.setString(2,L_strMKTTP);
					calSTAMT.setString(3,txtDOCNO.getText().trim());
					calSTAMT.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();
					//calSTAMT.close();
					clrCOMP();
					lblTOTVL.setText("");
					setMSG("Data Deleted successfully",'N');
				}
			}
			else
			{
				setMSG("Error In Deleting Data",'E');
			}
			if(L_intSELROW ==0)
			{
				setMSG("No rows selcted",'E');
				return;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"delPAYMT ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 *  Function for Validation of Amount 
	*/
	private boolean chkAMOUNT()
	{
		double L_dblRECVL=0.0;
		double L_dblADJVL=0.0;
		for(int i=0;i<tblRECEN.getRowCount();i++)
		{
			if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("false"))
			{
				if(tblRECEN.getValueAt(i,TB1_CHQNO).toString().length()==0)
					break;
				L_dblRECVL +=Double.parseDouble(tblRECEN.getValueAt(i,TB1_RCTVL).toString());
			}
		}
		for(int i=0;i<tblRECAD.getRowCount();i++)
		{
			if(tblRECAD.getValueAt(i,TB2_CHKFL).toString().equals("false"))
			{
				if(tblRECAD.getValueAt(i,TB2_DOCNO).toString().length()==0)
				{
					break;
				}
				L_dblADJVL +=Double.parseDouble(tblRECAD.getValueAt(i,TB2_ADJVL).toString());
			}
		}
		if(L_dblADJVL>L_dblRECVL)
		{
			setMSG("Available Receipt Amount is Less than Adjusted Amount",'E');
			return false;
		}
		else
		{
			setMSG("",'N');
			return true;
		}
	}
	
	/**
	 * Function For Generate  DOC Number for 
	*/
	private String genDOCNO()
	{
		String L_strDOCTP="",L_strDOCNO  = "",  L_strCODCD = "", L_strCCSVL = "0",L_CHP01="";// for DOC
		
		//if(tbpMAIN.getSelectedIndex()==1)
		//{
			L_strDOCTP=(cmbPMTTP.getSelectedItem().toString()).substring(0,2);
	//	}
		
	//	else
	
			//{
		//	L_strDOCTP=txtDOCTP.getText().trim() ;
		//}

		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MRXXPMT'  and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP +"'  and  ifnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP01.trim().length() >0)
					{
						setMSG("dataBase IN USE",'E');
						M_rstRSSET.close();
						return null;
					}
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
			M_strSQLQRY += " and CMT_CGSTP = 'MRXXPMT'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP+ "'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY," ");
			if(cl_dat.exeDBCMT("genDOCNO"))
			{
				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
				for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
					L_strDOCNO += "0";
				L_strCCSVL = L_strDOCNO + L_strCCSVL;
				L_strDOCNO = L_strCODCD + L_strCCSVL;
				txtDOCNO.setText(L_strDOCNO);
				
				
			}
			else 
				return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genDOCNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_strDOCNO;
	}	
	/**
	 *  Method to update the last Document No.in the CO_CDTRN
	*/
	private void updDOCNO(String P_strDOCNO)
	{
		try
		{
			String L_strDOCTP="";
		//	if(tbpMAIN.getSelectedIndex()==1)
		//	{
				L_strDOCTP=(cmbPMTTP.getSelectedItem().toString()).substring(0,2) ;
		//	}
			
			//else
			//{
				//L_strDOCTP=txtDOCTP.getText().trim() ;
			//}
			
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + P_strDOCNO.substring(3,8) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP ='MRXXPMT'";
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP+"'";			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeDOCNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 *  Function for Document number Validation
	*/
	private boolean chkDOCNO()
	{
		try
		{
			M_strSQLQRY="Select PR_DOCNO from MR_PRTRN  WHERE PR_CMPCD = '" +strCMPCD + "' and PR_PRTTP='C' and PR_DOCNO = '"+txtDOCNO.getText().trim()+ "'  and  ifnull(PR_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if (M_rstRSSET != null && M_rstRSSET.next())
			{
				M_rstRSSET.close();
				return true;
			}
			else
			{
				setMSG("Invalid Document Number",'E');
				return false;
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR," chkDOCNO ");		
		}
		return true;
	}
		
	/**
	 *  Input Verifier Class
	*/
	private class vldINVER extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				java.sql.Date L_datTMPDT;
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				
				if(input==txtDSRCD)
				{
					if(txtDSRCD.getText().trim().length()!=5)
						return true;
					else
					{
						
						M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTTP='D' AND  PT_PRTCD = '"+txtDSRCD.getText().trim().toUpperCase()+"'  "  ;
 						ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET!=null &&L_rstRSSET.next())
						{
							txtDSRNM.setText(L_rstRSSET.getString("PT_PRTNM"));
							L_rstRSSET.close();
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
				if(input==txtPRTCD)
				{
					if(txtPRTCD.getText().trim().length()!=5)
						return true	;
					else
					{
						if((cmbPMTTP.getSelectedItem().toString()).substring(0,2).equals("13"))
						{
							M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM,PT_YTDVL,PT_YTDFL from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD AND (PL_DOCVL-PL_ADJVL)>0 ";
							M_strSQLQRY +=" AND PL_DOCTP LIKE '"+strDOCTP+"%'";
						}
						else
						{
							M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM,PT_YTDVL,PT_YTDFL from CO_PTMST  where  PT_PRTTP='C'  ";
							
						}
						if(!txtDSRCD.getText().trim().substring(1,5).equals("8888"))
						{
							M_strSQLQRY += " AND PT_DSRCD='"+txtDSRCD.getText().trim()+"'";					
						}
						
				
						M_strSQLQRY += "AND PT_PRTCD ='"+txtPRTCD.getText().trim().toUpperCase()+"'"; 
												
						//M_strSQLQRY += " AND ifnull(PT_STSFL,'')<> 'X'";
						
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						
						if(L_rstRSSET!=null &&L_rstRSSET.next())
						{
							txtPRTNM.setText(L_rstRSSET.getString("PT_PRTNM"));
							lblPRTBL.setText(nvlSTRVL(L_rstRSSET.getString("PT_YTDVL"),"")+"  "+nvlSTRVL(L_rstRSSET.getString("PT_YTDFL"),""));
							//L_strCODCD = nvlSTRVL(L_rstRSSET.getString("PT_YTDFL"),"");
							//lblYTDFL.setText(L_rstRSSET.getString("PT_YTDFL"));
							L_rstRSSET.close();
								return true;
						}	
						else
						{
							txtPRTNM.setText("");
							lblPRTBL.setText("");
							//lblYTDFL.setText("");
							setMSG("Invalid Party Type",'E'); 
							return false;
						}
					}
				}
				
				/*if(input==txtDOCTP)
				{
					if(txtDOCTP.getText().trim().length()<2)
						return true;
					M_strSQLQRY ="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'";
					M_strSQLQRY  +="  AND CMT_CGSTP = 'MRXXPTT' AND CMT_CODCD Like '1%' AND CMT_CODCD= '"+txtDOCTP.getText().trim()+"' AND ifnull(CMT_STSFL,'') <> 'X'";
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null &&M_rstRSSET.next())
					{
						return true;
					}
					else
					{
						setMSG("Invalid Payment Type ",'E'); 
						return false;
					}
				}
				
				if(input==txtDOCTP1)
				{
					if(txtDOCTP1.getText().trim().length()<2)
						return true;
					M_strSQLQRY ="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'";
					M_strSQLQRY +="  AND CMT_CGSTP = 'MRXXPTT'  AND CMT_CODCD Like '1%' AND CMT_CODCD= '"+txtDOCTP1.getText().trim()+"' AND ifnull(CMT_STSFL,'') <> 'X'";
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null &&M_rstRSSET.next())
					{
						return true;
					}
					else
					{
						setMSG("Invalid Payment Type ",'E'); 
						return false;
					}
				}
				*/
				if(input==txtINVNO1)
				{
					if(txtINVNO1.getText().trim().length()<8)
						return true;
					
					M_strSQLQRY ="SELECT DISTINCT PL_DOCNO,PL_DOCDT,PL_DOCVL,(PL_DOCVL -PL_ADJVL) PT_AVLVL,IVT_CNSCD,IVT_DSRCD,IVT_BYRCD ,PL_ADJVL FROM MR_PLTRN,MR_IVTRN";
					M_strSQLQRY +=" WHERE PL_DOCNO = IVT_INVNO  AND PL_DOCTP like '"+strDOCTP+"%' AND  PL_CMPCD='"+ strCMPCD +"' AND (PL_DOCVL -PL_ADJVL)>0 ";
					M_strSQLQRY += " AND IVT_INVNO ='"+txtINVNO1.getText().trim() +"' ";
					M_strSQLQRY += " AND PL_PRTCD ='"+txtPRTCD.getText().trim() +"' ";
					
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null &&M_rstRSSET.next())
					{
						L_datTMPDT = M_rstRSSET.getDate("PL_DOCDT");
						if(L_datTMPDT !=null)
						{
							txtINVDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						}
						else
							txtINVDT.setText("");
						
						txtINVAM.setText(M_rstRSSET.getString("PL_DOCVL"));	
						txtOTSAM.setText(M_rstRSSET.getString("PT_AVLVL"));	
						
						M_rstRSSET.close();
						return true;
					}	
					else
					{
						txtINVDT.setText("");
						txtINVAM.setText("");
						txtOTSAM.setText("");
						setMSG("Invalid Invoice Number ",'E'); 
						return false;
					}
				}
				if(input==txtMOPCD1)
				{
					if(txtMOPCD1.getText().trim().length()<2)
						return true;
					M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'";
					M_strSQLQRY += " AND CMT_CGSTP = 'MRXXPMM'  AND CMT_CODCD='"+txtMOPCD1.getText().trim()+"' AND ifnull(CMT_STSFL,'') <> 'X'";
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);					
					if(M_rstRSSET!=null &&M_rstRSSET.next())
					{
						return true;
					}
					else
					{
						setMSG("Invalid Payment Mode ",'E'); 
						return false;
					}
				}
				if(input==txtBNKCD1)
				{
					if(txtBNKCD1.getText().trim().length()!=5)
						return true;
					M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTTP='B' AND  PT_PRTCD = '"+txtBNKCD1.getText().trim() +"'  AND ifnull(PT_STSFL,'')<> 'X' "  ;
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET!=null &&L_rstRSSET.next())
					{
						txtBNKNM1.setText(L_rstRSSET.getString("PT_PRTNM"));
						L_rstRSSET.close();
					}	
					else
					{
						txtBNKNM1.setText("");
						setMSG("Invalid Bank Code",'E'); 
						return false;
					}
					if(!chkCHDTL(txtBNKCD1.getText().trim(),txtCHQNO1.getText().trim(),txtCHQDT1.getText().trim(),txtDOCNO1.getText().trim()))
					{
						setMSG("Duplicate Entry ",'E');
						txtBNKNM1.setText("");
						return false;
					}
					else
						return true;
				}
				
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"Input Verifier");		
			}
			return true;
		}
	}
	private boolean chkCHDTL(String P_strBNKCD,String P_strCHQNO,String P_strCHQDT,String P_strDOCNO)
	{
		try
		{
			String L_strPRTCD=txtPRTCD.getText().trim();
			M_strSQLQRY="Select PR_CHQNO,PR_CHQDT,PR_BNKCD from MR_PRTRN where ";
			M_strSQLQRY +=" PR_BNKCD='"+P_strBNKCD+"' AND PR_CHQNO='"+P_strCHQNO+"' AND PR_PRTCD='"+L_strPRTCD+"' ";
			M_strSQLQRY +="	AND PR_CHQDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strCHQDT))+"' AND ifnull(PR_STSFL,'')<> 'X' "  ;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				M_strSQLQRY +="	AND PR_DOCNO <>'"+P_strDOCNO+"'";
			}
			//System.out.println("VALIDATION "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!=null &&M_rstRSSET.next())
			{
				M_rstRSSET.close();
				return false;
			}
			else
			{
				return true;
			}				
		}
		catch(Exception E)
		{
			setMSG(E,"Chq Details");
		}
		return true;
	}
		
	/**
	 *  Table Input Verifier Class for Validation
	*/
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				String L_strDOCNO="";
				java.sql.Date L_datTMPDT;
				if(getSource()==tblRECEN)
				{
					if(P_intCOLID>0)
						if(P_intCOLID!=TB1_CHKFL)
							if(((JTextField)tblRECEN.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
								return true;
					
					if(P_intCOLID==TB1_MOPCD)
					{ 
						strTEMP=((JTextField)tblRECEN.cmpEDITR[TB1_MOPCD]).getText();
						M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
						+ " AND CMT_CGSTP = 'MRXXPMM'AND CMT_CODCD='"+strTEMP+"' AND ifnull(CMT_STSFL,'') <> 'X'";
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET!=null &&L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}	
						else
						{
							
							setMSG("Invalid Payment Code",'E'); 
							return false;
						}
					}					
					
					if(P_intCOLID==TB1_RCTVL)			//Validates For Receipt Amount in JTable
					{
						double L_dblTOTVL=0.0;
						String L_strRCTVL="";
						double L_dblRECVL=0.0;
						if(txtRECAM.getText().trim().length()>0)
						{
							L_dblRECVL=Double.parseDouble(txtRECAM.getText().trim());
						}
						else
						{
							setMSG("Please Enter Receipt Amount First ",'E');
						}
						
						for(int i=0 ;i<=tblRECEN.getRowCount();i++)
						{
							if(tblRECEN.getValueAt(i,TB1_RCTVL).toString().length()==0)
								if(tblRECEN.getValueAt(i+1,TB1_RCTVL).toString().length()==0)
									if(tblRECEN.getValueAt(i+2,TB1_RCTVL).toString().length()==0)
										break;
							
							L_dblTOTVL +=Double.parseDouble(tblRECEN.getValueAt(i,TB1_RCTVL).toString());
						}
						if(L_dblRECVL<L_dblTOTVL)
						{
							setMSG("Please Check The Amount",'E');
							return false;
						}
						lblTOTVL.setText(setNumberFormat(L_dblTOTVL,2));
					}
					if(P_intCOLID==TB1_BNKCD)
					{
						hstBNKCD.clear();
						String L_strCHQNO="",L_strBNKCD="",L_strBNKCD1="",L_strCHQDT="";
						for(int i=0;i<tblRECEN.getRowCount();i++)
						{
							if(tblRECEN.getValueAt(i,TB1_CHQNO).toString().length()==0)
								if(tblRECEN.getValueAt(i+1,TB1_CHQNO).toString().length()==0)
									if(tblRECEN.getValueAt(i+2,TB1_CHQNO).toString().length()==0)
										break;
							L_strCHQNO=tblRECEN.getValueAt(i,TB1_CHQNO).toString();
							L_strBNKCD=tblRECEN.getValueAt(i,TB1_BNKCD).toString()+L_strCHQNO;
							if(i==P_intROWID)
								continue;
							else
								hstBNKCD.put(L_strBNKCD,"");
						}
						L_strCHQNO = tblRECEN.getValueAt(P_intROWID,TB1_CHQNO).toString();
						L_strCHQDT = tblRECEN.getValueAt(P_intROWID,TB1_CHQDT).toString();
						L_strBNKCD =((JTextField)tblRECEN.cmpEDITR[TB1_BNKCD]).getText();
						L_strBNKCD1=L_strBNKCD+L_strCHQNO;
						if(hstBNKCD.containsKey(L_strBNKCD1))
						{
							setMSG("Please Check The  Cheque Number, Bank Code",'E');
							setMSG("Invalid Bank Code",'E'); 
							return false;
						}
						M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTTP='B' AND  PT_PRTCD = '"+txtBNKCD.getText().trim() +"'  AND ifnull(PT_STSFL,'')<> 'X' "  ;
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET!=null &&L_rstRSSET.next())
						{
							tblRECEN.setValueAt(String.valueOf(L_rstRSSET.getString("PT_PRTNM")),tblRECEN.getSelectedRow(),TB1_BNKNM);
							L_rstRSSET.close();
						}	
						else
						{
							tblRECEN.setValueAt("",tblRECEN.getSelectedRow(),TB1_BNKNM);
							setMSG("Invalid Bank Code",'E'); 
							return false;
						}
						L_strDOCNO=txtDOCNO.getText().trim();
						if(!chkCHDTL(L_strBNKCD,L_strCHQNO,L_strCHQDT,L_strDOCNO))
						{
							setMSG("Duplicate Cheque Entry ",'E');
							tblRECEN.setValueAt("",tblRECEN.getSelectedRow(),TB1_BNKNM);
							return false;
							
						}
						else
						{
							return true;
						}						
					}
					//return true;
				}
				
				if(getSource()==tblRECAD)
				{
					if(P_intCOLID==TB2_DOCNO)
					{
						//hstBNKCD.clear();
					//	hstINVDL.clear();
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							if(P_intROWID<intRWADJ)
								return true;							
						}
						
						strTEMP=((JTextField)tblRECAD.cmpEDITR[TB2_DOCNO]).getText();
						
						for(int i=0;i<tblRECAD.getRowCount();i++)
						{
							if(tblRECAD.getValueAt(i,TB2_DOCNO).toString().length()==0)
								if(tblRECAD.getValueAt(i+1,TB2_DOCNO).toString().length()==0)
									if(tblRECAD.getValueAt(i+1,TB2_DOCNO).toString().length()==0)
									{
										break;
									}
							//	strTEMP=tblRECAD.getValueAt(i,TB2_DOCNO).toString();
							//System.out.println("P_intROWID ="+ P_intROWID +" I ="+i);
							if(i==P_intROWID)
							{
								//System.out.println("in COnt");
								continue;
							}
							else if(strTEMP.equals(tblRECAD.getValueAt(i,TB2_DOCNO).toString()))
							{
								//System.out.println(" ===  "+strTEMP);
								//System.out.println("IN IF P_intROWID ="+ P_intROWID +" I ="+i);
								
								setMSG("Duplicate Entry",'E');
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
								{
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_DOCDT);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_INVVL);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_AVLVL);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_ADJVL);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_ADJSH);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_CNSNM);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_DSRNM);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_BYRNM);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_DOCTP);
								}
									return false;
								
							}
							
						}
												
						M_strSQLQRY="SELECT DISTINCT PL_DOCNO,PL_DOCDT,PL_DOCVL,(PL_DOCVL -PL_ADJVL) PT_AVLVL,IVT_CNSCD,IVT_DSRCD,IVT_BYRCD ,PL_ADJVL,PL_DOCTP FROM MR_PLTRN,MR_IVTRN WHERE PL_DOCNO = IVT_INVNO  AND PL_CMPCD='"+ strCMPCD +"' ";
						M_strSQLQRY += " AND IVT_INVNO ='"+strTEMP+"' ";
						M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET!=null &&M_rstRSSET.next())
						{							
							L_datTMPDT = M_rstRSSET.getDate("PL_DOCDT");
							if(L_datTMPDT !=null)
								tblRECAD.setValueAt(M_fmtLCDAT.format(L_datTMPDT),tblRECAD.getSelectedRow(),TB2_DOCDT);
							else
								tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_DOCDT);
							
							tblRECAD.setValueAt(String.valueOf(M_rstRSSET.getString("PL_DOCVL")),tblRECAD.getSelectedRow(),TB2_INVVL);
							tblRECAD.setValueAt(String.valueOf(M_rstRSSET.getString("PT_AVLVL")),tblRECAD.getSelectedRow(),TB2_AVLVL);
							tblRECAD.setValueAt((String)hstPARTY.get("C"+M_rstRSSET.getString("IVT_CNSCD")),tblRECAD.getSelectedRow(),TB2_CNSNM);
							tblRECAD.setValueAt((String)hstPARTY.get("D"+M_rstRSSET.getString("IVT_DSRCD")),tblRECAD.getSelectedRow(),TB2_DSRNM);
							tblRECAD.setValueAt((String)hstPARTY.get("C"+M_rstRSSET.getString("IVT_BYRCD")),tblRECAD.getSelectedRow(),TB2_BYRNM);
							tblRECAD.setValueAt(String.valueOf(M_rstRSSET.getString("PL_ADJVL")),tblRECAD.getSelectedRow(),TB2_ADJSH);
							tblRECAD.setValueAt(String.valueOf(M_rstRSSET.getString("PL_DOCTP")),tblRECAD.getSelectedRow(),TB2_DOCTP);
							M_rstRSSET.close();
							return true;
						}	
						else
						{
							setMSG("Invalid Invoice Number",'E'); 
							return false;
						}												
					}
					if(P_intCOLID==TB2_ADJVL)
					{
						double L_dblTOTVL=0.0;
						double L_dblRECVL = Double.parseDouble(txtRECAM.getText().trim());
						double L_dblAVLVL = Double.parseDouble(tblRECAD.getValueAt(P_intROWID,TB2_AVLVL).toString());
						double L_dblADJVL = Double.parseDouble(((JTextField)tblRECAD.cmpEDITR[TB2_ADJVL]).getText());
						
						if(L_dblADJVL>L_dblAVLVL)						
						{
							setMSG("Adjusted Amount can't be Greater then Out-Stdg Amount",'E');
							return false;
						}
						for(int i=0;i<tblRECAD.getRowCount();i++)
						{
							if(tblRECAD.getValueAt(i,TB2_ADJVL).toString().length()==0)
								if(tblRECAD.getValueAt(i+1,TB2_ADJVL).toString().length()==0)
									if(tblRECAD.getValueAt(i+2,TB2_ADJVL).toString().length()==0)
										break;
							L_dblTOTVL +=Double.parseDouble(tblRECAD.getValueAt(i,TB2_ADJVL).toString());
						}
						if(L_dblTOTVL>L_dblRECVL)						
						{
							setMSG("Adjusted Amount can't be Greater then Receipt Amount",'E');
							return false;
						}
						txtADJAM.setText(setNumberFormat(L_dblTOTVL,2));
						txtBALAM.setText(setNumberFormat((L_dblRECVL-L_dblTOTVL),2));
					}
				}
			}
			catch(Exception E_TL)
			{
				setMSG(E_TL,"Table Input Verifier");				
			}
			return true;
		}
	}
	void clrCOMP()
	{
		super.clrCOMP();
		lblPRTBL.setText("");
	}
}
