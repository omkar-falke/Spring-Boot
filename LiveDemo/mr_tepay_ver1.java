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
import javax.swing.JOptionPane;

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
     Conditation : 1) CMT_CGMTP='DOC' 
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

public class mr_tepay extends cl_pbase
{
	private JTextField txtDOCNO,txtTOTVL,txtDOCTP,txtRECAM,txtADJAM,txtBALAM,txtDOCDT;
	private JTextField txtMOPCD,txtCHQNO,txtCHQDT,txtBNKCD,txtBNKNM,txtCHQAM;
//	txtPRTNM,txtPRTCD,
//	private JTextField txtINVNO1,txtINVDT,txtPRTCD1,txtPRTNM1,txtINVAM,txtOTSAM,txtCHQNO1,txtCHQDT1;
	private JTextField txtBNKCD1,txtMOPCD1,txtBNKNM1,txtCHQAM1;
//	private JTextField txtDOCTP1,txtDOCNO1,txtDOCDT1,txtCNSCD,txtCNSNM;
	
	private JTextField txtINVNO,txtADJVL,txtPLDOC;
	private JTextField txtDSRCD,txtDSRNM;
	private cl_JTable tblRECEN,tblRECAD;    
	private JLabel lblTOTVL,lblADJVL,lblPRTBL;
	private TBLINPVF objTBLVRF; 
	private JCheckBox chkCHKFL, chkREVFL;
	private JLabel lblCHQNO_REV;
	private JTextField txtCHQNO_REV;
	
	private JTabbedPane tbpMAIN;
	private JPanel pnlCHQWS; 
	
	private JComboBox cmbMKTTP,cmbPMTTP;
	
	private ButtonGroup btgINCRE;
//	private JRadioButton rdbINVIC;	 /**JRadioButton to  Invoice*/
//	private JRadioButton rdbDEBIT;	 /**JRadioButton to Credit Note */
	
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
	private final int TB2_DSRNM=6;
	private final int TB2_ADJSH=7;
	private final int TB2_DOCTP=8; 
		
	private int intRWCNT=0;       // variable for Row Count
	private int intRWADJ=0;       // Variable for Adjustment table row Cont
	private String strTEMP;
	
	private final String strCMPCD="01";   //Comany Code
	
	private String strSTSFL="0";
	private String strDOCTP;
	private String strDOCNO_ORG;
	private String strDOCNO_REF;
	private String strCNSCD;
	private double dblADJVL=0.0;
	private CallableStatement cstPLTRN_PMP ;
	private CallableStatement cstPATRN_REV ;
	private CallableStatement cstrwkPLTRN_TRN ;
	private Hashtable hstBNKCD = new Hashtable();	
	private Hashtable hstPARTY = new Hashtable();
	private Hashtable hstINVDL = new Hashtable();
	private boolean flgPAYADJ = true;
	private String strYREND = "31/03/2007";
      private String strYRDGT ;
	public mr_tepay()
	{
		super(2);
		setMatrix(20,9);
		try
		{
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "8" : "7";
			//System.out.println("strYRDGT "+strYRDGT);
			tbpMAIN  = new JTabbedPane();
			pnlCHQWS = new JPanel(null);
		
			lblTOTVL = new JLabel("");
			lblPRTBL = new JLabel("");
						
			cmbMKTTP=new JComboBox();
			cmbPMTTP=new JComboBox();
			
			btgINCRE=new ButtonGroup();
			String L_strCODCD="";
			
			lblTOTVL.setForeground(Color.blue);
			lblPRTBL.setForeground(Color.blue);
					
			cstPLTRN_PMP =cl_dat.M_conSPDBA_pbst.prepareCall("{call updPLTRN_PMP(?,?,?)}");
			cstPATRN_REV =cl_dat.M_conSPDBA_pbst.prepareCall("{call updPATRN_REV(?,?,?,?,?,?,?)}");
			cstrwkPLTRN_TRN = cl_dat.M_conSPDBA_pbst.prepareCall("{call rwkPLTRN_TRN(?,?,?,?,?,?)}");

			add(new JLabel("Distributer "),1,4,1,1,this,'L');
			add(txtDSRCD = new TxtLimit(5),1,5,1,1,this,'L');
			add(txtDSRNM = new TxtLimit(30),1,6,1,4,this,'L');
			
	/*		add(new JLabel("Party "),2,4,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(8),2,5,1,1,this,'L');
			add(txtPRTNM = new TxtLimit(30),2,6,1,4,this,'L');
	*/	
			add(new JLabel("Payment Details "),1,1,1,3,pnlCHQWS,'L');
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
			//System.out.println("strYRDGT "+strYRDGT);
			M_strSQLQRY="Select substr(CMT_CODCD,2,2) CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='DOC'"
			+ " AND CMT_CGSTP = 'MRXXPTT' AND CMT_CODCD like '"+strYRDGT+"4%' AND ifnull(CMT_STSFL,'') <> 'X'";

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
			
			//add(rdbINVIC=new JRadioButton("Invoice",true),3,1,1,1,this,'L');
			//add(rdbDEBIT=new JRadioButton("Debit Note"),3,2,1,1,this,'L');
			
			//btgINCRE.add(rdbINVIC);
			//btgINCRE.add(rdbDEBIT);			
			
			add(new JLabel("Paid Amount"),2,3,1,1.5,pnlCHQWS,'L');
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
			add(chkREVFL = new JCheckBox("Reverse Entry"),8,4,1,2,pnlCHQWS,'L');
			add(lblCHQNO_REV = new JLabel("Chq.No."),8,6,1,1,pnlCHQWS,'L');
			add(txtCHQNO_REV = new TxtLimit(10),8,7,1,1,pnlCHQWS,'L');
		
			add(new JLabel("Adjustment Details"),9,1,1,3,pnlCHQWS,'L');
			String[] L_strTBLHD2 = {" ","Doc. No","Doc. Date","Document Amt","Out-stdg Amt ","Amt tobe Adjusted","Distributer","Adj Amount","Doc Type"};
			int[] L_intCOLSZ2 = {15,80,80,80,80,80,200,50,40};
			tblRECAD = crtTBLPNL1(pnlCHQWS,L_strTBLHD2,50,10,1,4,8.9,L_intCOLSZ2,new int[]{0});
		
			add(new JLabel("Balance "),3,4,1,1,this,'L');
			add(lblPRTBL,3,5,1,1.5,this,'L');
		

			txtDOCDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
			txtADJAM.setText("0.0");
			txtBALAM.setText("0.0");
			
			txtMOPCD = new TxtLimit(2);
			txtCHQNO = new TxtLimit(10);
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
			tbpMAIN.addTab("Payments",pnlCHQWS);
			
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
			chkREVFL.setVisible(false);
			lblCHQNO_REV.setVisible(false);
			txtCHQNO_REV.setVisible(false);
			System.out.println("1 before set enbl");
			setENBL(false);
			System.out.println("1 after set enbl");

			
			//System.out.println(cl_dat.M_strFNNYR1_pbst.substring(3));
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
			
			if(M_objSOURC==txtDSRCD)
			{
				if(tbpMAIN.getSelectedIndex()==0)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtDSRCD.setEnabled(true);
					}
					else
					{
						txtDSRCD.setEnabled(false);
					}
				}
			}
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
			
			strDOCTP="4";
			txtDSRCD.setEnabled(true);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP_1();
				intRWCNT=0;
				intRWADJ=0;
				dblADJVL=0.0;
				lblTOTVL.setText("");
				//cmbPMTTP.setSelectedIndex(2);
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
					setENBL(false);
				else
				{
					chkREVFL.setVisible(false);
					lblCHQNO_REV.setVisible(false);
					txtCHQNO_REV.setVisible(false);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						cmbPMTTP.setEnabled(true);
					    txtDOCNO.setEnabled(true);
						//txtINVNO1.setEnabled(false);
						txtDSRCD.setEnabled(false);
						cmbPMTTP.requestFocus();
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						{
							chkREVFL.setVisible(true); 
							chkREVFL.setEnabled(true);
							lblCHQNO_REV.setVisible(true);
							txtCHQNO_REV.setVisible(true);
							txtCHQNO_REV.setEnabled(true);
						}
					}
					else
					{
						txtDOCDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
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
		//txtPRTNM.setEnabled(false);
		txtADJAM.setEnabled(false);
		txtBALAM.setEnabled(false);
		//txtINVDT.setEnabled(false);
		
//		txtINVAM.setEnabled(false);
		txtDSRNM.setEnabled(false);
		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
		{
			txtDOCNO.setEnabled(true);
		}

		tblRECEN.cmpEDITR[TB1_BNKNM].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_DOCDT].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_INVVL].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_AVLVL].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_DSRNM].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_ADJSH].setEnabled(false);
		tblRECAD.cmpEDITR[TB2_DOCTP].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
		{
			txtRECAM.setEnabled(false);
		}
		lblCHQNO_REV.setVisible(false);
		txtCHQNO_REV.setVisible(false);

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
				M_strSQLQRY +="Order by PT_PRTNM";
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
				
				//System.out.println(M_strSQLQRY);
				
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
				M_strSQLQRY="Select distinct PR_DOCNO,PR_DOCDT,PT_PRTNM from MR_PRTRN,CO_PTMST where PR_PRTTP=PT_PRTTP and PR_PRTCD = PT_PRTCD and PR_CMPCD ='"+strCMPCD+"' AND PR_DOCTP= '"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'  And ifnull(PR_STSFL,'') <>'X' and ifnull(PR_REFNO,'') = '' ";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					M_strSQLQRY += "And PR_STSFL ='0' ";
				//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					if(chkREVFL.isSelected() && txtCHQNO_REV.getText().length()>2)
						M_strSQLQRY += " and  PR_CHQNO = '"+txtCHQNO_REV.getText()+"'";
					else
						M_strSQLQRY += " And PR_STSFL ='0' ";
				}
				if(txtDOCNO.getText().trim().length()>0)
				{
					M_strSQLQRY += " AND PR_DOCNO LIKE '"+txtDOCNO.getText().trim()+"%' "; 
				}
				
				M_strSQLQRY +=" Order by PR_DOCNO desc";
				//System.out.println(M_strSQLQRY);
				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc.No","Date","Party"},3,"CT");
			}
			if(M_objSOURC==txtINVNO)
			{
				String L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
				L_strMKTTP=L_strMKTTP.substring(0,2);
				M_strHLPFLD="txtINVNO";
				M_strSQLQRY="SELECT DISTINCT PL_DOCNO,PL_DOCDT,PL_DOCVL,(ifnull(PL_DOCVL,0) -ifnull(PL_ADJVL,0)) PT_AVLVL,ifnull(PL_ADJVL,0) PL_ADJVL,PL_DOCTP,PL_CNSCD ";
				M_strSQLQRY +=" FROM MR_PLTRN where PL_CMPCD='"+ strCMPCD +"' ";
				M_strSQLQRY += " AND PL_MKTTP='"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' AND (ifnull(PL_DOCVL,0)-ifnull(PL_ADJVL,0))>0 ";
				M_strSQLQRY += " AND PL_PRTTP ='D' AND PL_PRTCD ='"+txtDSRCD.getText().trim() +"' ";
				M_strSQLQRY += " AND PL_DOCTP like '0%'";
				if(txtINVNO.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PL_DOCNO LIKE '"+txtINVNO.getText().trim()+"%'"; 
				}
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date","Doc. Value","Outstanding","Adj. Value","Doc Type","CNSCD"},7,"CT");
				
				//cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date","Doc. Value","Outstanding","Consignee","Distributer","Buyer","Adj. Value","Doc Type"},9,"CT",L_inaCOLSZ);

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
					txtDOCDT.requestFocus();

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
					txtMOPCD.setText("01");
					tblRECEN.cmpEDITR[TB1_MOPCD].requestFocus();
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
			if(M_strHLPFLD == "txtMOPCD")
			{
				txtMOPCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtDOCNO")
			{
				txtDOCNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtBNKCD")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtBNKCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblRECEN.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblRECEN.getSelectedRow(),TB1_BNKNM);
				tblRECEN.setValueAt(txtRECAM.getText().trim(),tblRECEN.getSelectedRow(),TB1_RCTVL);
			}
			if(M_strHLPFLD == "txtINVNO")
			{
				double L_dblRECVL=0.0;
				double L_dblAVLVL=0.0;
				if(txtRECAM.getText().trim().length()>0)
					L_dblRECVL=Double.parseDouble(txtRECAM.getText().trim());
													
				L_dblRECVL=L_dblRECVL-dblADJVL;
					StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
					L_STRTKN.nextToken();
					txtINVNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
					tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblRECAD.getSelectedRow(),TB2_DOCDT);
					tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblRECAD.getSelectedRow(),TB2_INVVL);
		
					L_dblAVLVL=Double.parseDouble(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());
				
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
					tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)).trim(),tblRECAD.getSelectedRow(),TB2_ADJSH);
					String L_strCNCNM=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4));
//					tblRECAD.setValueAt((String)hstPARTY.get("D"+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5))),tblRECAD.getSelectedRow(),TB2_DSRNM);
					tblRECAD.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),6)).trim(),tblRECAD.getSelectedRow(),TB2_DOCTP);
	
					tblRECAD.setValueAt( new Boolean(true),tblRECAD.getSelectedRow(),TB2_CHKFL);
					txtINVNO.requestFocus();
			}
		}
		catch(Exception e)                  
		{
			setMSG(e,"exeHLPOK ");
		}
	}
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	boolean vldDATA()
	{
		double L_dblTOTVL=0.000;
		if(txtDOCDT.getText().trim().length()==0)
		{
			setMSG("Date can't be Blank",'E');
			return false;
		}
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
			i=0;
			for(i=0;i<tblRECAD.getRowCount();i++)
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
			i=0;
			/*for(i=0 ;i<=tblRECEN.getRowCount();i++)
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
			}*/
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
			setCursor(cl_dat.M_curWTSTS_pbst);
			System.out.println(" IN SAVE ");
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			dblADJVL=0.0;
			/*if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}*/
			
			//setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				if(flgPAYADJ) // payment is adjusted
				{
					delPAYMT();
				}
				else	
					delPAYMT_NEW(); // payment is not adjusted
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
				System.out.println("1");
				String L_strADJVL="",L_strDOCNO="",L_strINVNO="",L_strMKTTP="",L_strGRPCD="";
				L_strDOCNO=genDOCNO();
			
				System.out.println("Doc NO "+L_strDOCNO);
				strDOCNO_ORG=L_strDOCNO;				// New Document Number When cheque wise entry will entered
				
				if(L_strDOCNO !=null)
				{
					L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
					L_strMKTTP=L_strMKTTP.substring(0,2);
					System.out.println("2");
						for(int i=0;i<tblRECEN.getRowCount();i++)
						{
							if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
							{
								insPMTDATA(i);
							}
						}
						for(int i=0;i<tblRECAD.getRowCount();i++)
						{
							if(tblRECAD.getValueAt(i,TB2_CHKFL).toString().equals("true"))
							{
								insADJDATA(i);
							}
						}
				///	}
					updDOCNO(L_strDOCNO);
				}
				System.out.println(1);
				if(cl_dat.M_flgLCUPD_pbst)
				{
					System.out.println("saved");
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						System.out.println("COMMIT");

						System.out.println("3 "+strCMPCD+ " "+L_strMKTTP+" "+L_strDOCNO);
						cstPLTRN_PMP.setString(1,strCMPCD);
						cstPLTRN_PMP.setString(2,L_strMKTTP);
						cstPLTRN_PMP.setString(3,txtDOCNO.getText().trim());
						//System.out.println(4);
						cstPLTRN_PMP.executeUpdate();
						System.out.println("PROC CALLED");
						JOptionPane.showMessageDialog(this,"Please Note down Document No :  "+txtDOCNO.getText().trim(),"Message",JOptionPane.INFORMATION_MESSAGE);
						System.out.println("commit CALLED");
						cl_dat.M_conSPDBA_pbst.commit();
						System.out.println("commit completed");
						//System.out.println(6);
						//shoDATA();
						//txtDOCNO1.setText(L_strDOCNO);
						txtDOCNO.setText(L_strDOCNO); 
						setMSG("Data saved successfully",'N');
						clrCOMP();
						strSTSFL="0";
					}
					else
					{
                       		 JOptionPane.showMessageDialog(this,"Error in saving data to Party Ledger ","Error",JOptionPane.INFORMATION_MESSAGE);
					    setMSG("Error in saving data to Party Ledger",'E');
                      		  cl_dat.M_btnSAVE_pbst.setEnabled(false);
					}
				}
				else
				{
					M_strSQLQRY = "Update CO_CDTRN set ";
					M_strSQLQRY += " CMT_CHP02 =''";
					M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
					M_strSQLQRY += " and CMT_CGSTP = 'MRXXPTT'";	
				///	M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + (cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";
					M_strSQLQRY += " and CMT_CODCD = '" + strYRDGT+ (cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";

					cl_dat.M_flgLCUPD_pbst = true;
					//System.out.println("M_strSQLQRY"+M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY," ");
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
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
	}
	private void insPMTDATA(int P_intROWNO)
	{
	   try
	   {
		System.out.println("insert pmt ");
		double L_dblADJVL=0.0;
		String L_strADJVL="",L_strINVNO="",L_strMKTTP="",L_strGRPCD="";
		String L_strREFNO = "";

		L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
		L_strMKTTP=L_strMKTTP.substring(0,2);
										
		double L_dblRECVL = Double.parseDouble(txtRECAM.getText().trim());
		if(txtADJAM.getText().trim().length()>0)
		L_dblADJVL = Double.parseDouble(txtADJAM.getText().trim());
		if(L_dblRECVL==L_dblADJVL)
			strSTSFL="1";
		else
			strSTSFL="0";	
		String L_strSQLQRY="Select PT_GRPCD from CO_PTMST where  PT_PRTTP='D' AND PT_PRTCD='"+txtDSRCD.getText().trim()+"' AND ifnull(PT_STSFL,'')<> 'X'";
		M_rstRSSET=cl_dat.exeSQLQRY(L_strSQLQRY);
		if(M_rstRSSET !=null && M_rstRSSET.next())
		{
			L_strGRPCD=nvlSTRVL(M_rstRSSET.getString("PT_GRPCD"),"");
			M_rstRSSET.close();
		}

		String L_strDOCDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOCDT.getText().trim()));
		double dblCHQAMT1 = Double.parseDouble(tblRECEN.getValueAt(P_intROWNO,TB1_RCTVL).toString());
			
		M_strSQLQRY="Insert into MR_PRTRN(PR_CMPCD,PR_PRTTP,PR_MKTTP,PR_PRTCD,PR_GRPCD,PR_DOCTP,PR_DOCNO,PR_DOCDT,PR_BNKCD,PR_CHQNO,PR_CHQDT,PR_MOPCD,PR_RCTVL,PR_REFNO,PR_SBSCD,PR_TRNFL,PR_STSFL,PR_LUSBY,PR_LUPDT) values ( ";
		M_strSQLQRY+="'"+strCMPCD+"',";
		M_strSQLQRY+="'D',";
		M_strSQLQRY+="'"+L_strMKTTP+"',";
		M_strSQLQRY+="'"+txtDSRCD.getText().trim()+"',";
		M_strSQLQRY+="'"+L_strGRPCD+"',";
		M_strSQLQRY+="'"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+"',";
		M_strSQLQRY+="'"+ txtDOCNO.getText().trim() +"',";
		M_strSQLQRY+="'"+L_strDOCDT +"',";
		M_strSQLQRY+="'"+tblRECEN.getValueAt(P_intROWNO,TB1_BNKCD).toString() +"',";
		M_strSQLQRY+="'"+tblRECEN.getValueAt(P_intROWNO,TB1_CHQNO).toString() +"',";
		M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblRECEN.getValueAt(P_intROWNO,TB1_CHQDT).toString()))+ "',";
		M_strSQLQRY+="'"+tblRECEN.getValueAt(P_intROWNO,TB1_MOPCD).toString() +"',";
		M_strSQLQRY+=""+setNumberFormat(dblCHQAMT1,2) +",";
		M_strSQLQRY+="'"+L_strREFNO+"',";
		M_strSQLQRY+="'"+M_strSBSCD+"',";
		M_strSQLQRY += getUSGDTL("PR_",'I',strSTSFL)+")";
		System.out.println("InsertMR_PRTRN = "+M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		System.out.println("InsertMR_PRTRN ");

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
			
			String L_strSQLQRY="Select PT_GRPCD from CO_PTMST where  PT_PRTTP='D' AND PT_PRTCD='"+txtDSRCD.getText().trim()+"' AND ifnull(PT_STSFL,'')<> 'X'";
			M_rstRSSET=cl_dat.exeSQLQRY(L_strSQLQRY);
			if(M_rstRSSET !=null && M_rstRSSET.next())
			{
				//L_strGRPCD=M_rstRSSET.getString("PT_GRPCD");
				L_strGRPCD=nvlSTRVL(M_rstRSSET.getString("PT_GRPCD"),"");
				M_rstRSSET.close();
			}
			System.out.println("adj pmt");
			M_strSQLQRY="insert into MR_PATRN(PA_CMPCD,PA_PRTTP,PA_MKTTP,PA_PRTCD,PA_GRPCD,PA_CRDTP,PA_CRDNO,PA_DBTTP,PA_DBTNO,PA_ADJVL,PA_SBSCD,PA_TRNFL,PA_STSFL,PA_LUSBY,PA_LUPDT) values ( ";
			M_strSQLQRY+= "'"+strCMPCD+"',";
			M_strSQLQRY+= "'D',";
			M_strSQLQRY+="'"+L_strMKTTP+"',";
			M_strSQLQRY+= "'"+txtDSRCD.getText().trim() +"',";
			M_strSQLQRY+="'"+L_strGRPCD+"',";
			M_strSQLQRY+= "'"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+"',";
			M_strSQLQRY+= "'"+txtDOCNO.getText().trim() +"',";
			M_strSQLQRY+= "'"+ tblRECAD.getValueAt(P_intROWNO,TB2_DOCTP).toString() +"',";
			M_strSQLQRY+= "'"+tblRECAD.getValueAt(P_intROWNO,TB2_DOCNO).toString() +"',";
			M_strSQLQRY+= ""+tblRECAD.getValueAt(P_intROWNO,TB2_ADJVL).toString() +",";	
			M_strSQLQRY+= "'"+M_strSBSCD+"',";
			M_strSQLQRY += getUSGDTL("MR_",'I',strSTSFL)+")";
				
			System.out.println("Insert = "+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
			L_dblADJVL =Double.parseDouble(tblRECAD.getValueAt(P_intROWNO,TB2_ADJVL).toString());
			L_strINVNO = nvlSTRVL(tblRECAD.getValueAt(P_intROWNO,TB2_DOCNO).toString(),"");
											
			M_strSQLQRY="UPDATE MR_PLTRN SET PL_ADJVL= ifnull(PL_ADJVL,0) +"+L_dblADJVL+" , ";
			M_strSQLQRY +="PL_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
			M_strSQLQRY +="PL_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
			M_strSQLQRY += " WHERE PL_CMPCD = '" +strCMPCD+ "'";
			M_strSQLQRY +=" AND PL_PRTTP='D'";
			M_strSQLQRY += " AND PL_PRTCD = '" +txtDSRCD.getText().trim() + "'";
			M_strSQLQRY += " AND PL_DOCTP = '" +tblRECAD.getValueAt(P_intROWNO,TB2_DOCTP).toString() + "'";
			M_strSQLQRY += " AND PL_DOCNO = '" +L_strINVNO +"'";
			System.out.println("PUDATE PLTRN  NNN ="+M_strSQLQRY);
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
			//String L_strPRTNM=.getText().trim();
			String L_strDSRCD=txtDSRCD.getText().trim();
			String L_strDSRNM=txtDSRNM.getText().trim();
			String L_strBNKCD=txtBNKCD1.getText().trim();
			String L_strMOPCD =txtMOPCD1.getText().trim();
					
			String L_strDOCDT=txtDOCDT.getText().trim();
			
			String L_strPMTTP=(cmbPMTTP.getSelectedItem().toString()).substring(0,2);
					
			clrCOMP_1();
			txtDSRCD.setText(L_strDSRCD);
			//txtPRTNM.setText(L_strPRTNM);
			txtDSRNM.setText(L_strDSRNM);
			txtDOCDT.setText(L_strDOCDT);
			
			for(int j=0;j<cmbPMTTP.getItemCount();j++)
			{
				if(L_strPMTTP.equals((cmbPMTTP.getItemAt(j).toString()).substring(0,2)))
				{
					cmbPMTTP.setSelectedIndex(j);
				}
			}
						
			lblTOTVL.setText("");
									
			 M_strSQLQRY="Select distinct PT_YTDVL,PT_YTDFL from CO_PTMST  where  PT_PRTTP='D' And PT_PRTCD='"+ txtDSRCD.getText().trim()+"'";
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						
			if(L_rstRSSET!=null &&L_rstRSSET.next())
			{
			
				lblPRTBL.setText(nvlSTRVL(L_rstRSSET.getString("PT_YTDVL"),"")+"  "+nvlSTRVL(L_rstRSSET.getString("PT_YTDFL"),""));
				L_rstRSSET.close();
			}	
			else
			{
				lblPRTBL.setText("");
			}
		}
		catch(Exception e)
		{
		    setMSG(e,"showDATA");    
		}
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
			M_strSQLQRY +="WHERE PR_CMPCD ='"+strCMPCD+"' and PR_PRTTP='D' and PR_DOCNO ='"+txtDOCNO.getText().trim()+ "' ";
			M_strSQLQRY +=" AND PR_PRTTP=PT_PRTTP AND PR_PRTCD= PT_PRTCD and  ifnull(PR_STSFL,'') <> 'X'";
			//System.out.println(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)))
			{
				M_strSQLQRY +="	and PR_STSFL <> 'X'";
			}
           		else
				M_strSQLQRY +="	and PR_STSFL not in('1','X')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			
			clrCOMP_1();
			if(tblRECEN.isEditing())
			tblRECEN.getCellEditor().stopCellEditing();
			tblRECEN.setRowSelectionInterval(0,0);
			tblRECEN.setColumnSelectionInterval(0,0);
			
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					txtDOCNO.setText(nvlSTRVL(M_rstRSSET.getString("PR_DOCNO"),""));
					strDOCNO_ORG = nvlSTRVL(M_rstRSSET.getString("PR_DOCNO"),"");
					lblPRTBL.setText(nvlSTRVL(M_rstRSSET.getString("PT_YTDVL"),"")+"  "+nvlSTRVL(M_rstRSSET.getString("PT_YTDFL"),""));
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
					
					L_strDSRCD=nvlSTRVL(M_rstRSSET.getString("PT_DSRCD"),"");
					txtDSRCD.setText(L_strDSRCD);
					txtDSRNM.setText(nvlSTRVL(hstPARTY.get("D"+L_strDSRCD).toString(),""));
					
					L_strMKTTP=nvlSTRVL(M_rstRSSET.getString("PR_MKTTP"),"");
					
					L_strBNKCD=M_rstRSSET.getString("PR_BNKCD");
					tblRECEN.setValueAt(hstPARTY.get("B"+L_strBNKCD),i,TB1_BNKNM);
					tblRECEN.setValueAt(L_strBNKCD,i,TB1_BNKCD);
					
					L_strPMTTP=nvlSTRVL(M_rstRSSET.getString("PR_DOCTP"),"");
					tblRECEN.setValueAt(M_rstRSSET.getString("PR_MOPCD"),i,TB1_MOPCD);
					tblRECEN.setValueAt(M_rstRSSET.getString("PR_CHQNO"),i,TB1_CHQNO);
					L_strRCTVL=M_rstRSSET.getString("PR_RCTVL");
					tblRECEN.setValueAt(L_strRCTVL,i,TB1_RCTVL);
					L_dblRCTVL +=Double.parseDouble(L_strRCTVL);
					i++;
					intRWCNT++;
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
			M_strSQLQRY = "Select distinct PA_DBTNO,PA_ADJVL,PA_DBTTP,PA_LUPDT,IVT_CNSCD,IVT_DSRCD,IVT_BYRCD,PL_DOCDT,PL_DOCVL,(ifnull(PL_DOCVL,0)-ifnull(PL_ADJVL,0)) PL_AVLVL ";
			M_strSQLQRY +="  from MR_PATRN,MR_PLTRN left outer join MR_IVTRN ON PA_DBTNO = IVT_INVNO WHERE  PA_DBTNO=PL_DOCNO and PA_CMPCD = '" +strCMPCD + "' ";
			M_strSQLQRY +=" and PA_PRTTP='D' and PA_PRTCD=PL_PRTCD and PA_PRTCD='"+txtDSRCD.getText().trim()+"' and PA_CRDNO = '"+txtDOCNO.getText().trim()+ "' ";
			M_strSQLQRY +=" and PA_CRDTP= '"+(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'  and  ifnull(PA_STSFL,'') <> 'X'";
		
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)))

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
					
					tblRECAD.setValueAt((String)hstPARTY.get("D"+M_rstRSSET.getString("IVT_DSRCD")),i,TB2_DSRNM);
					i++;
					intRWADJ++;
				}
				txtADJAM.setText(setNumberFormat(L_dblADJVL,2));
				double L_dblRECVL = Double.parseDouble(txtRECAM.getText().trim());
				L_dblADJVL = Double.parseDouble(txtADJAM.getText().trim());
				txtBALAM.setText(setNumberFormat((L_dblRECVL-L_dblADJVL),2));
			}
			if(intRWADJ ==0)
				flgPAYADJ = false;
			else
				flgPAYADJ = true;
 
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception E)
		{
			setMSG(E,"dspPAYADJ ");
			//setCursor(cl_dat.M_curDFSTS_pbst);
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
						M_strSQLQRY += " AND PR_PRTTP = 'D'";
						M_strSQLQRY += " AND PR_PRTCD = '" +txtDSRCD.getText().trim() + "'";
						M_strSQLQRY += " AND PR_DOCNO = '" +txtDOCNO.getText().trim() + "'";
						M_strSQLQRY += " AND PR_DOCTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2) + "'";
						M_strSQLQRY += " AND PR_MOPCD = '" + L_strMOPCD + "'";
						M_strSQLQRY += " AND PR_BNKCD = '" + L_strBNKCD + "' ";
						M_strSQLQRY += " AND PR_CHQNO = '" + L_strCHQNO + "' ";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else
					{
						insPMTDATA(i);
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
						M_strSQLQRY += " AND PA_PRTTP='D'";
						M_strSQLQRY += " AND PA_PRTCD = '" +txtDSRCD.getText().trim() + "'";
						M_strSQLQRY += " AND PA_CRDNO = '" +txtDOCNO.getText().trim() + "'";
						M_strSQLQRY += " AND PA_CRDTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2) + "'";
						M_strSQLQRY += " AND PA_DBTTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
						M_strSQLQRY += " AND PA_DBTNO = '" +L_strDOCNO +"'";
						
						L_dblADJVL =Double.parseDouble(tblRECAD.getValueAt(i,TB2_ADJVL).toString())-Double.parseDouble(tblRECAD.getValueAt(i,TB2_ADJSH).toString());
						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
						L_strINVNO = nvlSTRVL(tblRECAD.getValueAt(i,TB2_DOCNO).toString(),"");
						L_strSQLQRY="UPDATE MR_PLTRN SET PL_ADJVL= ifnull(PL_ADJVL,0) + "+L_dblADJVL+" , ";
						L_strSQLQRY +="PL_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						L_strSQLQRY +="PL_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
						L_strSQLQRY += " WHERE PL_CMPCD = '" +strCMPCD+ "'";
						L_strSQLQRY +=" AND PL_PRTTP='D'";
						L_strSQLQRY += " AND PL_PRTCD = '" +txtDSRCD.getText().trim() + "'";
						L_strSQLQRY += " AND PL_DOCTP = '" +tblRECAD.getValueAt(i,TB2_DOCTP).toString() + "'";
						L_strSQLQRY += " AND PL_DOCNO = '" +L_strINVNO +"'";
					
						cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
						
					}
					else
					{
						L_strSQLQRY  = " Select PA_PRTCD,PA_DBTTP,PA_CRDNO,PA_DBTNO,ifnull(pa_adjvl,0) from MR_PATRN";
						L_strSQLQRY += " WHERE PA_CMPCD = '" +strCMPCD+ "'";
						L_strSQLQRY += " AND PA_PRTTP='D'";
						L_strSQLQRY += " AND PA_PRTCD = '" +txtDSRCD.getText().trim() + "'";
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
							M_strSQLQRY += " AND PA_PRTTP='D'";
							M_strSQLQRY += " AND PA_PRTCD = '" +txtDSRCD.getText().trim() + "'";
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
			//System.out.println("mod 1");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeMOD"))
				{
					//System.out.println("mod 2");

					L_strDOCNO=txtDOCNO.getText().trim();
					//System.out.println("mod 3 "+strCMPCD +" "+L_strMKTTP +" "+L_strDOCNO);
					cstPLTRN_PMP.setString(1,strCMPCD);
					cstPLTRN_PMP.setString(2,L_strMKTTP);
					cstPLTRN_PMP.setString(3,L_strDOCNO);
					//System.out.println("mod 4");
					cstPLTRN_PMP.executeUpdate();
					//System.out.println("mod 5");
					cl_dat.M_conSPDBA_pbst.commit();
					//System.out.println("mod 6");

					strSTSFL="0";
				
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
			//setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	private void delPAYMT_NEW()
	{
	   try
	   {
		int L_intROWCT =0;
		int L_intSEQNO =0;
		int L_intSELROW =0;
		if(!chkAMOUNT())
			return;
		String L_strMOPCD="",L_strCHQNO="",L_strBNKCD="",L_strDOCNO="",L_strMKTTP="";
		if(flgPAYADJ)// payment is adjusted
		{
			//setMSG("Payment is adjusted, cannot cancel..",'E');
			//return;
			// Do cancellation without reversal entry (working)
			// put a reversal entry
			delPAYMT();
		}
		if(!flgPAYADJ)// payment is not adjusted
		{
			if(!chkREVFL.isSelected())
			{
				M_strSQLQRY = "SELECT PL_SEQNO FROM MR_PLTRN WHERE pl_prtcd ='"+txtDSRCD.getText().trim() +"' AND PL_DOCNO ='"+txtDOCNO.getText().trim() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
						L_intSEQNO = M_rstRSSET.getInt("PL_SEQNO");
					M_rstRSSET.close();
				} 
				M_strSQLQRY  ="update MR_PRTRN SET ";
				M_strSQLQRY +="PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
				M_strSQLQRY +="PR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', ";
				M_strSQLQRY +="PR_RCTVL= 0, ";
				M_strSQLQRY +="PR_STSFL= 'X'";
				M_strSQLQRY += " WHERE PR_CMPCD = '" +strCMPCD+ "'";
				M_strSQLQRY += " AND PR_PRTTP='D'";
				M_strSQLQRY += " AND PR_PRTCD = '" +txtDSRCD.getText().trim() + "'";
				M_strSQLQRY += " AND PR_DOCNO = '" +txtDOCNO.getText().trim() + "'";
				M_strSQLQRY += " AND PR_DOCTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";	cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");

				M_strSQLQRY  ="update MR_PLTRN SET ";
				M_strSQLQRY +="PL_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
				M_strSQLQRY +="PL_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', ";
				M_strSQLQRY +="PL_DOCVL= 0 ";
				M_strSQLQRY += " WHERE PL_CMPCD = '" +strCMPCD+ "'";
				M_strSQLQRY += " AND PL_PRTTP='D'";
				M_strSQLQRY += " AND PL_PRTCD = '" +txtDSRCD.getText().trim() + "'";
				M_strSQLQRY += " AND PL_DOCNO = '" +txtDOCNO.getText().trim() + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");

			}
			if(cl_dat.exeDBCMT("delPAYMT"))
			{
				// Rework the ledger here
				System.out.println("Rework ");
				cstrwkPLTRN_TRN.setString(1,strCMPCD);
				cstrwkPLTRN_TRN.setString(2,"D");
				cstrwkPLTRN_TRN.setString(3,txtDSRCD.getText().trim());
				cstrwkPLTRN_TRN.setString(4,(cmbPMTTP.getSelectedItem().toString()).substring(0,2));
				cstrwkPLTRN_TRN.setString(5,txtDOCNO.getText().trim());
				cstrwkPLTRN_TRN.setInt(6,L_intSEQNO);
				cstrwkPLTRN_TRN.executeUpdate();
				//System.out.println("Rework end");
				cl_dat.M_conSPDBA_pbst.commit();
				setMSG("Data saved successfully",'N');
				clrCOMP();
			}
		}
	    }
	    catch(Exception L_E)
	    {
		setMSG(L_E,"delPAY_NEW");		
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
			// New document for reverse entry is generated (during cheque bouncing)
			if(chkREVFL.isSelected() &&  txtCHQNO_REV.getText().length() > 2)
			{
				strDOCNO_REF = genDOCNO();
				//System.out.println("reversal selected "+strDOCNO_REF);
				for(int i=0;i<tblRECEN.getRowCount();i++)
				{
					if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						//System.out.println("insert rect start");
						insPMTDATA(i);
						//System.out.println("insert rect end");

					}
				}
				updDOCNO(strDOCNO_REF);
				//System.out.println("Doc no. updated "+strDOCNO_REF);			
				////
				cstPLTRN_PMP.setString(1,strCMPCD);
				cstPLTRN_PMP.setString(2,cmbMKTTP.getSelectedItem().toString().substring(0,2));
				cstPLTRN_PMP.setString(3,strDOCNO_REF);
				cstPLTRN_PMP.executeUpdate();
				///System.out.println("LEDGER UPDATED"	);
			}
			// For cancellation of payment (without reversal entry)
			if(!chkREVFL.isSelected())
			{
				//System.out.println("Reversal not selected");
				for(int i=0;i<tblRECEN.getRowCount();i++)
				{
					if(tblRECEN.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						L_strMOPCD = nvlSTRVL(tblRECEN.getValueAt(i,TB1_MOPCD).toString(),"");
						L_strCHQNO = nvlSTRVL(tblRECEN.getValueAt(i,TB1_CHQNO).toString(),"");
						L_strBNKCD = nvlSTRVL(tblRECEN.getValueAt(i,TB1_BNKCD).toString(),"");
										
						L_intSELROW++;
						cl_dat.M_conSPDBA_pbst.commit();
						M_strSQLQRY  ="update MR_PRTRN SET ";
						M_strSQLQRY +="PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						M_strSQLQRY +="PR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', ";
						M_strSQLQRY +="PR_RCTVL= 0, ";
						M_strSQLQRY +="PR_STSFL= 'X'";
						M_strSQLQRY += " WHERE PR_CMPCD = '" +strCMPCD+ "'";
						M_strSQLQRY += " AND PR_PRTTP='D'";
						M_strSQLQRY += " AND PR_PRTCD = '" +txtDSRCD.getText().trim() + "'";
						M_strSQLQRY += " AND PR_DOCNO = '" +txtDOCNO.getText().trim() + "'";
						M_strSQLQRY += " AND PR_DOCTP = '" +(cmbPMTTP.getSelectedItem().toString()).substring(0,2)+ "'";
						M_strSQLQRY += " AND PR_MOPCD = '" + L_strMOPCD + "'";
						M_strSQLQRY += " AND PR_BNKCD = '" + L_strBNKCD + "' ";
						M_strSQLQRY += " AND PR_CHQNO = '" + L_strCHQNO + "' ";
						//System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(cl_dat.exeDBCMT("delPAYMT"))
						{	
							//System.out.println("calling PATRN reverse");
							//System.out.println(strCMPCD);
							//System.out.println(cmbPMTTP.getSelectedItem().toString().substring(0,2));
							//System.out.println(strDOCNO_ORG);
							//System.out.println(L_strBNKCD);
							//System.out.println(L_strCHQNO);
							cstPATRN_REV.setString(1,strCMPCD);
							cstPATRN_REV.setString(2,"D");
							cstPATRN_REV.setString(3,txtDSRCD.getText().trim());
							cstPATRN_REV.setString(4,(cmbPMTTP.getSelectedItem().toString()).substring(0,2));
							cstPATRN_REV.setString(5,strDOCNO_ORG);
							cstPATRN_REV.setString(6,L_strBNKCD);
							cstPATRN_REV.setString(7,L_strCHQNO);
							cstPATRN_REV.executeUpdate();
							//System.out.println("callED PATRN reverse");
							cl_dat.M_conSPDBA_pbst.commit();
						}
					}
				}
			}	
				if(cl_dat.exeDBCMT("delPAYMT"))
				{
					//System.out.println("callpltrn ");
					//System.out.println("org doc "+strDOCNO_ORG);
					//System.out.println(strCMPCD);
					//System.out.println(cmbMKTTP.getSelectedItem().toString().substring(0,2));
					cstPLTRN_PMP.setString(1,strCMPCD);
					cstPLTRN_PMP.setString(2,cmbMKTTP.getSelectedItem().toString().substring(0,2));
					cstPLTRN_PMP.setString(3,strDOCNO_ORG);
					cstPLTRN_PMP.executeUpdate();
					//System.out.println("callpltrn ended");
					cl_dat.M_conSPDBA_pbst.commit();
				int L_intSEQNO =0;
				M_strSQLQRY = "SELECT PL_SEQNO FROM MR_PLTRN WHERE PL_PRTTP ='D' AND pl_prtcd ='"+txtDSRCD.getText().trim() +"' AND PL_DOCNO ='"+txtDOCNO.getText().trim() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
						L_intSEQNO = M_rstRSSET.getInt("PL_SEQNO");
					M_rstRSSET.close();
				} 
				// Rework the ledger here
				//System.out.println("Rework ");
				cstrwkPLTRN_TRN.setString(1,strCMPCD);
				cstrwkPLTRN_TRN.setString(2,"D");
				cstrwkPLTRN_TRN.setString(3,txtDSRCD.getText().trim());
				cstrwkPLTRN_TRN.setString(4,(cmbPMTTP.getSelectedItem().toString()).substring(0,2));
				cstrwkPLTRN_TRN.setString(5,txtDOCNO.getText().trim());
				cstrwkPLTRN_TRN.setInt(6,L_intSEQNO);
				cstrwkPLTRN_TRN.executeUpdate();
				//System.out.println("Rework end");
				cl_dat.M_conSPDBA_pbst.commit();
				clrCOMP_1();
				lblTOTVL.setText("");
				setMSG("Data Deleted successfully",'N');
				}
				else
				{
					setMSG("Error In Deleting Data",'E');
				}
			/*if(L_intSELROW ==0)
			{
				setMSG("No rows selcted",'E');
				return;
			}*/
		}
		catch(Exception e)
			{
			setMSG(e,"delPAYMT ");
			//setCursor(cl_dat.M_curDFSTS_pbst);
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
				{
					//setMSG("Cheque number can not be blank..",'E');
					break;
				}
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
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			return true;
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
		String L_strDOCTP="",L_strDOCNO  = "",  L_strCODCD = "", L_strCCSVL = "0",L_CHP02="";// for DOC
		L_strDOCTP=(cmbPMTTP.getSelectedItem().toString()).substring(0,2);
	
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,ifnull(CMT_CHP02,'') CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MRXXPTT'  and ";
			M_strSQLQRY += " CMT_CODCD = '" + strYRDGT + L_strDOCTP +"'  and  ifnull(CMT_STSFL,'') <> 'X'";
			//M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP +"'  and  ifnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP02.trim().length() >0)
					{
						setMSG("dataBase IN USE",'E');
						M_rstRSSET.close();
						return null;
					}
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP02 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
			M_strSQLQRY += " and CMT_CGSTP = 'MRXXPTT'";	
			M_strSQLQRY += " and CMT_CODCD = '" + strYRDGT + L_strDOCTP+ "'";

			//M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP+ "'";
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
			//setCursor(cl_dat.M_curDFSTS_pbst);
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
		
			L_strDOCTP=(cmbPMTTP.getSelectedItem().toString()).substring(0,2) ;
			
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP02 ='',CMT_CCSVL = '" + P_strDOCNO.substring(3,8) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP ='MRXXPTT'";
			M_strSQLQRY += " and CMT_CODCD = '" + strYRDGT + L_strDOCTP+"'";			

			//M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP+"'";			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeDOCNO ");
			//setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 *  Function for Document number Validation
	*/
	private boolean chkDOCNO()
	{
		try
		{
			M_strSQLQRY="Select PR_DOCNO from MR_PRTRN  WHERE PR_CMPCD = '" +strCMPCD + "' and PR_DOCNO = '"+txtDOCNO.getText().trim()+ "'  and  ifnull(PR_STSFL,'') <> 'X'";
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
					{
						txtDSRNM.setText("");
						return true;
					}
					else
					{
						txtDSRCD.setText(txtDSRCD.getText().trim().toUpperCase());
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
			String L_strPRTCD=txtDSRCD.getText().trim();
			M_strSQLQRY="Select PR_CHQNO,PR_CHQDT,PR_BNKCD from MR_PRTRN where ";
			M_strSQLQRY +=" PR_BNKCD='"+P_strBNKCD+"' AND PR_CHQNO='"+P_strCHQNO+"' AND PR_PRTCD='"+L_strPRTCD+"' ";
			M_strSQLQRY +="	AND PR_CHQDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strCHQDT))+"' AND ifnull(PR_STSFL,'')<> 'X' "  ;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				M_strSQLQRY +="	AND PR_DOCNO <>'"+P_strDOCNO+"'";
			}
			
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
						tblRECEN.setValueAt(String.valueOf(L_strBNKCD.toUpperCase()),tblRECEN.getSelectedRow(),TB1_BNKCD);
						M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTTP='B' AND  PT_PRTCD = '"+txtBNKCD.getText().trim().toUpperCase()+"'  AND ifnull(PT_STSFL,'')<> 'X' "  ;
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET!=null &&L_rstRSSET.next())
						{
							tblRECEN.setValueAt(String.valueOf(L_rstRSSET.getString("PT_PRTNM")),tblRECEN.getSelectedRow(),TB1_BNKNM);
							tblRECEN.setValueAt(String.valueOf(txtRECAM.getText().trim()),tblRECEN.getSelectedRow(),TB1_RCTVL);
							
							L_rstRSSET.close();
						}	
						else
						{
							tblRECEN.setValueAt("",tblRECEN.getSelectedRow(),TB1_BNKNM);
							tblRECEN.setValueAt("",tblRECEN.getSelectedRow(),TB1_RCTVL);
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
					
				}
				if(getSource()==tblRECAD)
				{
					if(P_intCOLID==TB2_DOCNO)
					{
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
							
							if(i==P_intROWID)
							{
								continue;
							}
							else if(strTEMP.equals(tblRECAD.getValueAt(i,TB2_DOCNO).toString()))
							{
								setMSG("Duplicate Entry",'E');
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
								{
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_DOCDT);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_INVVL);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_AVLVL);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_ADJVL);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_ADJSH);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_DSRNM);
									tblRECAD.setValueAt("",tblRECAD.getSelectedRow(),TB2_DOCTP);
								}
									return false;
							}
							
						}
						M_strSQLQRY="SELECT DISTINCT PL_DOCNO,PL_DOCDT,PL_DOCVL,(ifnull(PL_DOCVL,0) -ifnull(PL_ADJVL,0)) PT_AVLVL,ifnull(PL_ADJVL,0) PL_ADJVL,PL_DOCTP,PL_CNSCD ";
				M_strSQLQRY +=" FROM MR_PLTRN where PL_CMPCD='"+ strCMPCD +"' ";
				M_strSQLQRY += " AND PL_MKTTP='"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' AND (ifnull(PL_DOCVL,0)-ifnull(PL_ADJVL,0))>0 ";
				M_strSQLQRY += " AND PL_PRTTP ='D' AND PL_PRTCD ='"+txtDSRCD.getText().trim() +"' ";
				M_strSQLQRY += " AND PL_DOCTP like '0%'";
				if(txtINVNO.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PL_DOCNO LIKE '"+txtINVNO.getText().trim()+"%'"; 
				}
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
					//System.out.println("IN TABLE INPUT");
					tblRECAD.setValueAt((String)hstPARTY.get("D"+txtDSRCD.getText().trim()),tblRECAD.getSelectedRow(),TB2_DSRNM);
					//System.out.println("IN TABLE INPUT111");
					tblRECAD.setValueAt(String.valueOf(M_rstRSSET.getString("PL_ADJVL")),tblRECAD.getSelectedRow(),TB2_ADJSH);
					tblRECAD.setValueAt(String.valueOf(M_rstRSSET.getString("PL_DOCTP")),tblRECAD.getSelectedRow(),TB2_DOCTP);
								//System.out.println("IN TABLE INPUT2222");
					M_rstRSSET.close();
					return true;
				}	
				else
				{
					setMSG("Invalid Credit Note Number",'E'); 
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
		if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
		{
			inlTBLEDIT(tblRECEN);
			inlTBLEDIT(tblRECAD);
		}
		lblPRTBL.setText("");
	}


	/** Restoring default Key Values after clearing components 
	 * on the entry screen
	 */
	private void clrCOMP_1()
	{
		try
		{
			String L_strDOCTP = cmbPMTTP.getSelectedItem().toString();
			String L_strCHQNO_REV = txtCHQNO_REV.getText();
			boolean L_flgREVFL = chkREVFL.isSelected() ? true : false;
			clrCOMP();
			setCMBPMT(L_strDOCTP);
			txtCHQNO_REV.setText(L_strCHQNO_REV);
			chkREVFL.setSelected(L_flgREVFL);
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP_1");}
	}

	
/** Initializing table editing before poulating/capturing data
 */
private void inlTBLEDIT(JTable P_tblTBLNM)
{
	if(!P_tblTBLNM.isEditing())
		return;
	P_tblTBLNM.getCellEditor().stopCellEditing();
	P_tblTBLNM.setRowSelectionInterval(0,0);
	P_tblTBLNM.setColumnSelectionInterval(0,0);
			
}

/** Setting initial/default values in Combo Box (taking code as a parameter)
 */
	private void setCMBPMT(String LP_CMBVL)
	{
		try
		{
			int L_intCMBITMS = cmbPMTTP.getItemCount();
			for(int i=0;i<cmbPMTTP.getItemCount();i++)
			{
				if((cmbPMTTP.getItemAt(i).toString()).equalsIgnoreCase(LP_CMBVL))
				{
					cmbPMTTP.setSelectedItem(cmbPMTTP.getItemAt(i).toString());
					cmbPMTTP.setSelectedIndex(i);
					break;
				}
			}
		}
	catch(Exception e){setMSG(e,"setCMBDFT");}
	}
}

