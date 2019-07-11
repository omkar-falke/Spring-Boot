/*
System Name   : Marketing Management System
Program Name  : Transporterwise Despatch Detail/Summary Report
Program Desc. : Transporterwise Despatch Detail/Summary Report (Required for Bill Passing,Insurance and Accounting) 

Author        : Mr S.R.Tawde
Date          : 03.10.2005
Version       : MARKSYS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;import javax.swing.JPanel;
import java.awt.Font;import java.awt.Color;import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;import javax.swing.InputVerifier;import javax.swing.JComponent;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
/**<pre>
<b>System Name :</b> Marketing Management System.
 
<b>Program Name :</b> Transporterwise Despatch Detail/Summary Report

<b>Purpose :</b> This module gives Detail/Summarised Report of Transporterwise Despatch between given dates.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
CO_CDTRN	
CO_PTMST
MR_VWTRN
MR_IVTRN
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name         Table Name         Type/Size    Description
--------------------------------------------------------------------------------
txtFRMDT    IN_BKGDT/IVT_INVDT  MR_VWTRN/MR_IVTRN  Date         From Date
txtTORDT    IN_BKGDT/IVT_INVDT  MR_VWTRN/MR_IVTRN  Date         To Date
txtDSPTP	IN_DSPTP/IVT_DSPTP  MR_VWTRN/MR_IVTRN  Varchar      Despatch Type
txtTRPCD    IN_TRPCD/IVT_TRPCD  MR_VWTRN/MR_IVTRN  Varchar		Transporter Code 
rdbDSPSP	-------------Select for Specific Despatch Type----------------------
rdbDSPAL    ----------------Select for All Despatch Type------------------------
rdbTRPSP    --------------Select for Specific Transporter-----------------------
rdbTRPAL    ----------------Select for All Transporters-------------------------
rdbRPTDT    ------------------Select for Detail Report--------------------------
rdbRPTSM    ------------------Select for Summary Report-------------------------
rdbTRNBK    -------------Select for Order Bookingwise Report--------------------
rdbTRNIV    ----------------Select for Despatchwise Report----------------------
--------------------------------------------------------------------------------

NOTE - 1) If user selects Specific Transporter option then in the Report Type the only option available is "Detail"
       and the option available in Report Purpose is "Bill Passing"
       2) If user selects "Summary" in report type option then the Transaction Type option panel will be displayed.

List of fields with help facility : 
Field Name	Display Description			      Display Columns		  	 Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtDSPTP    Despatch Type,Description         CMT_CODCD,CMT_CODDS        CO_CDTRN
txtTRPCD    Transporter Code,Name             PT_PRTTP,PT_PRTCD,PT_PRTNM CO_PTMST
-----------------------------------------------------------------------------------------------------------------------------------------------------
<I>

<B>Conditions Given in Query:</b>
			 FOR DETAIL REPORT (FOR INSURANCE PURPOSE AND FOR BILL PASSING PURPOSE)
             Data is taken from MR_IVTRN,CO_PTMST,CO_CDTRN between the given date range 
             and depending upon the selection further in Despatch Type, Transporter, Report Type and Transaction Type.
             Grouping is done by Invoice No.,Invoice date,Consignee,Buyer,transporter's code,Transporter's Name,
             LR NO., Vehicle No.,Despatch Type,Destination,Invoice Value.
             1)DATE(IVT_INVDT) BETWEEN FROM DATE AND TO DATE.
             2)CMT_CGMTP='SYS' AND CMT_CGSTP='MRXXDTP' AND CMT_CODCD=IVT_DTPCD
             3)IVT_STSFL!= 'X' AND IVT_SALTP IN ('01','03','04','05')
             4)IVT_DTPCD = INPUT DESPATCH TYPE
             5)IVT_TRPCD = INPUT TRANSPORTER'S CODE 
             6)IVT_INVNO,DATE(IVT_INVDT),A.PT_PRTNM ,B.PT_PRTNM,IVT_TRPCD,C.PT_PRTNM,IVT_LR_NO,IVT_LRYNO,
               CMT_CODDS,IVT_DSTDS,IVT_INVVL
             7)ORDER BY C.PT_PRTNM,IVT_INVNO "
	
             FOR SUMMARY REPORT (FOR ACCOUNTS PURPOSE BASED ON SALES INVOICE)
             Data is taken from MR_IVTRN,CO_PTMST,CO_CDTRN between the given date range 
             and depending upon the selection further in Despatch Type, Transaction Type.
             1)DATE(IVT_INVDT) BETWEEN FROM DATE AND TO DATE.
             2)IVT_SALTP IN ('01','03','04','05')
             3)A.CMT_CGMTP='SYS' AND A.CMT_CGSTP='MRXXZON' AND A.CMT_CODCD=IVT_ZONCD 
             4)B.CMT_CGMTP='SYS' AND B.CMT_CGSTP='MR00SAL' AND B.CMT_CODCD=IVT_SALTP
             5)IVT_DTPCD = INPUT DESPATCH TYPE
             6)ORDER BY IVT_ZONCD,IVT_CNSCD"

             FOR SUMMARY REPORT (FOR ACCOUNTS PURPOSE BASED ON ORDER BOOKING)
             Data is taken from VW_INTRN,CO_PTMST,CO_CDTRN between the given date range 
             and depending upon the selection further in Despatch Type, Transaction Type.
             1)DATE(IN_BKGDT) BETWEEN FROM DATE AND TO DATE.
             2)IN_SALTP IN ('01','03','04','05')
             3)A.CMT_CGMTP='SYS' AND A.CMT_CGSTP='MRXXZON' AND A.CMT_CODCD=IN_ZONCD 
             4)B.CMT_CGMTP='SYS' AND B.CMT_CGSTP='MR00SAL' AND B.CMT_CODCD=IN_SALTP
             5)IN_DTPCD = INPUT DESPATCH TYPE
             6)ORDER BY IN_ZONCD,IN_CNSCD"

</I>
Validations :
//'ISOFGXXRPT','MSTCOXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL			 
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
    3) Despatch Type validation from CO_CDTRN.('MST','MRXXDTP')
    4) Transporter's Code from CO_PTMST('PT_PRTTP = 'T' AND 'PT_PRTCD= INPUT CODE')
 */
public class mr_rpfds extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;		/** JTextField to accept Transporter Code*/
	private JTextField txtTRPCD;        /** JtextField to Display Transporter's Name*/
	private JTextField txtTRPNM;		/** JTextField to accept Despatch Type.*/
	private JTextField txtDSPTP;		/** JTextField to Display Despatch Type*/				
	private JTextField txtDSPDS;
	private JTextField txtCSACD;
	private JTextField txtCSANM;
	
	private int intRCTPK = 0;
	private JPanel pnlRPTTP,pnlDSPTP,pnlTRPTP,pnlTRNTP,pnlPURTP,pnlCSATP;
	private ButtonGroup bgrRPTTP,bgrTRPTP,bgrDSPTP,bgrTRNTP,bgrPURTP;
	private JRadioButton rdbDSPAL,rdbDSPSP,rdbRPTDT,rdbRPTSM,rdbTRNBK,rdbTRNIV,rdbTRPAL,rdbTRPSP,rdbPURBP,rdbPURIN;

										/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;/** Report Header String*/	
	private String strRPHDR,strRPHDR1,strTRNDS,strDSTDS;
	private String strFMDAT,strTODAT;
	
	private INPVF objINPVF = new INPVF();
	
	/**
	 *1.Screen Designing
	 *2.Hashtable is created using CO_CDTRN for maintaining various types of codes alongwith
	 *  their descriptions.
	 */
	mr_rpfds()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			bgrDSPTP=new ButtonGroup();
			bgrTRPTP=new ButtonGroup();
			bgrRPTTP=new ButtonGroup();
			
			add(new JLabel("From Date"),5,2,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,3,1,1,this,'L');			
			add(new JLabel("To Date"),5,4,1,1.5,this,'L');
			add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');
			
			pnlDSPTP = new JPanel();
			setMatrix(20,8);
			pnlDSPTP.setLayout(null);
			bgrDSPTP=new ButtonGroup();
			add(rdbDSPAL = new JRadioButton("All",true),1,2,1,1,pnlDSPTP,'L');
			add(rdbDSPSP = new JRadioButton("Specific",false),1,3,1,1,pnlDSPTP,'L');
			add(txtDSPTP = new TxtLimit(2),1,4,1,1,pnlDSPTP,'L');
			add(txtDSPDS = new TxtLimit(20),1,5,1,1,pnlDSPTP,'L');
			bgrDSPTP.add(rdbDSPSP);
			bgrDSPTP.add(rdbDSPAL);
			pnlDSPTP.setBorder(BorderFactory.createTitledBorder(null,"Despatch Type",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlDSPTP,6,2,2,6,this,'L');

			pnlTRPTP = new JPanel();
			setMatrix(20,8);
			pnlTRPTP.setLayout(null);
			bgrTRPTP=new ButtonGroup();
			add(rdbTRPAL = new JRadioButton("All",true),1,2,1,1,pnlTRPTP,'L');
			add(rdbTRPSP = new JRadioButton("Specific",false),1,3,1,1,pnlTRPTP,'L');
			add(txtTRPCD = new TxtLimit(5),1,4,1,0.5,pnlTRPTP,'L');
			add(txtTRPNM = new TxtLimit(40),1,5,1,2.5,pnlTRPTP,'L');
			bgrTRPTP.add(rdbTRPSP);
			bgrTRPTP.add(rdbTRPAL);
			pnlTRPTP.setBorder(BorderFactory.createTitledBorder(null,"Transporter",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlTRPTP,8,2,2,7,this,'L');
			
			
			pnlRPTTP = new JPanel();
			setMatrix(20,8);
			pnlRPTTP.setLayout(null);
			bgrRPTTP=new ButtonGroup();
			add(rdbRPTDT = new JRadioButton("Detail",true),1,2,1,1,pnlRPTTP,'L');
			add(rdbRPTSM = new JRadioButton("Summary",false),1,3,1,1,pnlRPTTP,'L');
			bgrRPTTP.add(rdbRPTDT);
			bgrRPTTP.add(rdbRPTSM);
			pnlRPTTP.setBorder(BorderFactory.createTitledBorder(null,"Report Type",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlRPTTP,10,2,2,5,this,'L');

			pnlTRNTP = new JPanel();
			setMatrix(20,8);
			pnlTRNTP.setLayout(null);
			bgrTRNTP=new ButtonGroup();
			add(rdbTRNBK = new JRadioButton("Bookingwise",true),1,2,1,1,pnlTRNTP,'L');
			add(rdbTRNIV = new JRadioButton("Invoicewise",false),1,3,1,1,pnlTRNTP,'L');
			bgrTRNTP.add(rdbTRNBK);
			bgrTRNTP.add(rdbTRNIV);
			pnlTRNTP.setBorder(BorderFactory.createTitledBorder(null,"Transaction Type",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlTRNTP,12,2,2,5,this,'L');
			pnlTRNTP.setVisible(false);

			pnlPURTP = new JPanel();
			setMatrix(20,8);
			pnlPURTP.setLayout(null);
			bgrPURTP=new ButtonGroup();
			add(rdbPURBP = new JRadioButton("Bill Passing",true),1,2,1,1,pnlPURTP,'L');
			add(rdbPURIN = new JRadioButton("Insurance",false),1,3,1,1,pnlPURTP,'L');
			bgrPURTP.add(rdbPURBP);
			bgrPURTP.add(rdbPURIN);
			pnlPURTP.setBorder(BorderFactory.createTitledBorder(null,"Report Purpose",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlPURTP,12,2,2,5,this,'L'); 
			pnlPURTP.setVisible(false);

			pnlCSATP = new JPanel();
			setMatrix(20,8);
			pnlCSATP.setLayout(null);
			//bgrCSATP=new ButtonGroup();
			//add(rdbCSAAL = new JRadioButton("All",true),1,2,1,1,pnlTRPTP,'L');
			//add(rdbCSASP = new JRadioButton("Specific",false),1,3,1,1,pnlTRPTP,'L');
			add(txtCSACD = new TxtLimit(5),1,4,1,0.5,pnlCSATP,'L');
			add(txtCSANM = new TxtLimit(40),1,5,1,2.5,pnlCSATP,'L');
			//bgrCSATP.add(rdbCSASP);
			//bgrCSATP.add(rdbCSAAL);
			pnlCSATP.setBorder(BorderFactory.createTitledBorder(null,"Cons.Stockist",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlCSATP,14,2,2,7,this,'L');
			pnlCSATP.setVisible(false);

			/** Registering the components with inputverifier */
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtDSPTP.setInputVerifier(objINPVF);
			txtTRPCD.setInputVerifier(objINPVF);
			txtCSACD.setInputVerifier(objINPVF);
			
			M_pnlRPFMT.setVisible(true);			
			setENBL(false);									
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
	}			
    /**
     *Super class method overide to enhance its funcationality 
     *For populating FROM DATE AND TO DATE with required default values 
     *We can use this for Enabling or Disabling other components on the screen as per our
     *requirement based on option selected ie. ADDITION,MODIFICATION,DELETION etc.
     * @param L_flgSTAT boolean argument to pass state of the component.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtTODAT.setEnabled(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);		
		txtTRPCD.setEnabled(false);
		txtTRPNM.setEnabled(false);
		txtDSPTP.setEnabled(false);
		txtDSPDS.setEnabled(false);
		txtCSACD.setEnabled(true);
		//txtCSANM.setEnabled(true);
		
		if (L_flgSTAT==true )
		{
			if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
			{					 
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));																					
       				txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));				            														
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" setENBL",'E');
				}
			}
			else 
			{
				if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				{
					txtTODAT.setText("");
					txtFMDAT.setText("");			
				}
			}
		}
	}		

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
		
		if(M_objSOURC == rdbDSPAL)
		{	
			txtDSPTP.setText("");
			txtDSPDS.setText("All");
			txtDSPTP.setEnabled(false);	
			rdbTRPAL.requestFocus();
		}	
		
		if(M_objSOURC == rdbDSPSP)
		{	
			txtDSPTP.setEnabled(true);
			txtDSPDS.setText(" ");
			txtDSPTP.requestFocus();
		}	

		if(M_objSOURC == rdbTRPAL)
		{	
			txtTRPCD.setText("");
			txtTRPNM.setText("All");
			rdbRPTSM.setVisible(true);		
			rdbPURIN.setVisible(true);
			rdbRPTDT.requestFocus();
		}	

		if(M_objSOURC == rdbTRPSP)
		{	
			txtTRPCD.setEnabled(true);	
			rdbRPTSM.setVisible(false);
			rdbPURIN.setVisible(false);
			txtTRPNM.setText("");
			txtTRPCD.requestFocus();
		}	

		if(M_objSOURC == rdbRPTDT)
		{	
			pnlTRNTP.setVisible(false);
			pnlPURTP.setVisible(true);
		}	

		if(M_objSOURC == rdbRPTSM)
		{	
			pnlPURTP.setVisible(false);
			pnlTRNTP.setVisible(true);
		}	
		
		if(M_objSOURC == rdbPURIN)
		{	
			pnlCSATP.setVisible(true);
			txtCSACD.setVisible(true);
			txtCSANM.setVisible(true);
		}	
			

		if(M_objSOURC == rdbTRNBK)
		strTRNDS = "Order Booking";

		if(M_objSOURC == rdbTRNIV)
		strTRNDS = "Sales Invoice";	
			
		if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(M_cmbDESTN.getItemCount() > 1)
				{
					M_cmbDESTN.setEnabled (true);
				}
				else if(M_cmbDESTN.getItemCount() == 1)
				{
					setMSG("No Printer Attached to the System ..",'E');
				}
			}
		}
	}	 
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)			
				txtTODAT.requestFocus();
			if(M_objSOURC == txtTODAT)			
			{	
				txtTODAT.requestFocus();
				try
				{
					strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
				}
			catch(Exception L_E)
			{
				//setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"Error in Date");
			}
			}	
		}
		
		/**Method to display list of Shift Codes and Main Product Codes in help window 
		when F1 is Pressed.*/
		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtDSPTP)
				{
					M_strHLPFLD = "txtDSPTP";
					M_strSQLQRY =  "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' ";
					M_strSQLQRY+=  "and CMT_CGSTP='MRXXDTP'";
					M_strSQLQRY+=  "and isnull(CMT_STSFL,'')<> 'X' order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Despatch Type","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtTRPCD)
				{
					M_strHLPFLD = "txtTRPCD";
					if(rdbTRPSP.isSelected())
					{
						M_strSQLQRY = " select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP='T'";
						if(txtTRPCD.getText().length()==0)
						{	
							setMSG("Type First letter and then press F1",'E'); 
							return;
						}	
						if(!(txtTRPCD.getText().length()==1))
						return;
						if(txtTRPCD.getText().trim().length() > 0)
						{	
							M_strSQLQRY+= " AND PT_PRTNM LIKE '"+txtTRPCD.getText().trim().toUpperCase()+"%'";
							M_strSQLQRY+= " ORDER BY PT_PRTNM ";
						}
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Buyer Code","Name"},2,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}

				if(M_objSOURC == txtCSACD)
				{
					M_strHLPFLD = "txtCSACD";
					//if(rdbTRPSP.isSelected())
					//{
						M_strSQLQRY = " select DISTINCT PT_PRTCD,PT_PRTNM from CO_PTMST,MR_IVTRN where PT_PRTTP='G' ";
						M_strSQLQRY+= " AND IVT_DSRTP='G' AND PT_PRTCD=IVT_DSRCD AND CONVERT(varchar,IVT_INVDT,101) BETWEEN '"+strFMDAT+"' AND '"+strTODAT+"' AND IVT_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP IN ('14')";
						//if(txtCSACD.getText().length()==0)
						//{	
						//	setMSG("Type First letter and then press F1",'E'); 
						//	return;
						//}	
						//if(!(txtCSACD.getText().length()==1))
						//return;
						//if(txtCSACD.getText().trim().length() > 0)
						//{	
						//	M_strSQLQRY+= " AND PT_PRTNM LIKE '"+txtCSACD.getText().trim().toUpperCase()+"%'";
						//	M_strSQLQRY+= " ORDER BY PT_PRTNM ";
						//}
						System.out.println("F1 : "+M_strSQLQRY);
						cl_hlp(M_strSQLQRY,2,1,new String[]{"C.stk.Code","Name"},2,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
					//}
				}
			}	
			catch(Exception L_E)
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"Key Pressed F1");
			}
		}		
	}
		public void exeHLPOK()
		{
		super.exeHLPOK();
			if(M_strHLPFLD == "txtTRPCD")
			{
				txtTRPCD.setText(cl_dat.M_strHLPSTR_pbst);
				System.out.println("set");
			}	
			if(M_strHLPFLD == "txtCSACD")
			{
				txtCSACD.setText(cl_dat.M_strHLPSTR_pbst);
				System.out.println("set");
			}	
			if(M_strHLPFLD == "txtDSPTP")
			{
				txtDSPTP.setText(cl_dat.M_strHLPSTR_pbst);
				System.out.println("set dsptp");
			}	
		}
	
	/**
	*Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
							
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpfds.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpfds.doc";	
			if(rdbRPTDT.isSelected())
				getDATA();				
			if(rdbRPTSM.isSelected())
				getDATA1();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(intRECCT >0)
				    if (M_rdbTEXT.isSelected())
				        doPRINT(strFILNM);
				    else 
                    {    
		                Runtime r = Runtime.getRuntime();
					    Process p = null;								
				        p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
				        setMSG("For Printing Select File Menu, then Print  ..",'N');
				    }    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
				 if(intRECCT >0)
				 {						
				     if(M_rdbHTML.isSelected())
				        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
				     else
    				    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
	    		  }
 				  else
					  setMSG("No data found, Please check Date Range OR Product Category OR Shift Code..",'E');				    
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
		    {
			    cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Gradewise Receipt Detail Report"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	/**
	*Method to validate data in the input components on the screen, before execution like
    *validation of Dates, availability of the printers etc.
    *Check for blank input
	*/
	boolean vldDATA()
	{
		try
		{	
			if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Specify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter valid To-Date To Specify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}					
			else if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtTODAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" vldDATA",'E');
				}	
			}	
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("Please Note that From-Date must be Greater than To-Date .. ",'E');								
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtFMDAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX + " vldData", 'E');
				}																
			}							
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{				
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{				
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}			
			return true;
		}
		catch(Exception L_EX)
		{
			return false;
		}
	}
	/**
	*Method to fetch Data from Database and start creation of the output file for Detail Report.
	*/
	private void getDATA()
	{ 	    		
		//String L_strFMDAT,L_strTODAT;
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblTIVQT,L_dblINVVL,L_dblTTRQT = 0,L_dblGTOQT=0,L_dblIVAMT=0,L_dblGIVAMT=0;
		
		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPTRPCD = new String();
		String L_strPTRPNM = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCTRPCD = new String();

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strTRPCD = new String();
		String L_strTRPNM = new String();
		String L_strCSACD = new String();
		String L_strCSANM = new String();
		String L_strCNSNM = new String();
		String L_strDSTDS = new String();
		String L_strBYRNM = new String();
		String L_strINVNO = new String();
		String L_strINVDT = new String();
		String L_strLRNO  = new String();
		String L_strLRYNO = new String();
		String L_strDSPDS = new String();


		try
	    {	
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Statement </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();
		//	L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
		//	L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			//strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			//strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			setMSG("Report Generation is in Progress.......",'N');		
			
/**			M_strSQLQRY = " SELECT DISTINCT IVT_INVNO,DATE(IVT_INVDT) IVT_INVDT,A.PT_PRTNM CNSNM,E.CMT_CODDS DSTDS,B.PT_PRTNM BYRNM,IVT_TRPCD,";
			M_strSQLQRY+= "	C.PT_PRTNM TRPNM,IVT_LR_NO,IVT_LRYNO,LEFT(D.CMT_CODDS,7) CMT_CODDS,IVT_INVVL,SUM(IVT_INVQT) TOT_INVQT";
			M_strSQLQRY+= " FROM SPLDATA.MR_IVTRN,SPLDATA.CO_PTMST A,SPLDATA.CO_PTMST B,SPLDATA.CO_PTMST C,";
			M_strSQLQRY+= " SPLDATA.CO_CDTRN D,SPLDATA.CO_CDTRN E WHERE DATE(IVT_INVDT) BETWEEN '"+L_strFMDAT+"' AND '"+L_strTODAT+"' AND A.PT_PRTTP ='C'";
			M_strSQLQRY+= " AND A.PT_PRTCD=IVT_CNSCD AND B.PT_PRTTP='C' AND B.PT_PRTCD=IVT_BYRCD AND C.PT_PRTTP='T'";
			M_strSQLQRY+= " AND C.PT_PRTCD=IVT_TRPCD AND D.CMT_CGMTP='SYS' AND D.CMT_CGSTP='MRXXDTP' AND D.CMT_CODCD=IVT_DTPCD ";
			M_strSQLQRY+= " AND IVT_STSFL!= 'X' AND IVT_SALTP IN ('01','03','04','05') AND A.PT_DSTCD=E.CMT_CODCD AND E.CMT_CGMTP='SYS' AND E.CMT_CGSTP='COXXDST'"; 
			if(rdbDSPSP.isSelected())
				M_strSQLQRY+= " AND IVT_DTPCD='"+txtDSPTP.getText().trim()+"'";
			if(rdbTRPSP.isSelected())
				M_strSQLQRY+= " AND IVT_TRPCD='"+txtTRPCD.getText().trim()+"'";
				
			M_strSQLQRY+= "GROUP BY IVT_INVNO,DATE(IVT_INVDT),A.PT_PRTNM ,E.CMT_CODDS,B.PT_PRTNM,IVT_TRPCD,C.PT_PRTNM,";
			M_strSQLQRY+= " IVT_LR_NO,IVT_LRYNO,D.CMT_CODDS,IVT_INVVL ";
			M_strSQLQRY+= " ORDER BY C.PT_PRTNM,IVT_INVNO ";*/
			
			M_strSQLQRY = " SELECT DISTINCT IVT_INVNO,CONVERT(varchar,IVT_INVDT,103) IVT_INVDT,A.PT_PRTNM CNSNM,E.CMT_CODDS DSTDS,B.PT_PRTNM BYRNM,IVT_TRPCD,";
			M_strSQLQRY+= "	C.PT_PRTNM TRPNM,IVT_LR_NO,IVT_LRYNO,LEFT(D.CMT_CODDS,7) CMT_CODDS,IVT_INVVL,SUM(IVT_INVQT) TOT_INVQT";
			M_strSQLQRY+= " FROM MR_IVTRN,CO_PTMST A,CO_PTMST B,CO_PTMST C,";
			M_strSQLQRY+= " CO_CDTRN D,CO_CDTRN E WHERE CONVERT(varchar,IVT_INVDT,101) BETWEEN '"+strFMDAT+"' AND '"+strTODAT+"' AND A.PT_PRTTP ='C'";
			M_strSQLQRY+= " AND A.PT_PRTCD=IVT_CNSCD AND B.PT_PRTTP='C' AND B.PT_PRTCD=IVT_BYRCD AND C.PT_PRTTP='T'";
			M_strSQLQRY+= " AND C.PT_PRTCD=IVT_TRPCD AND D.CMT_CGMTP='SYS' AND D.CMT_CGSTP='MRXXDTP' AND D.CMT_CODCD=IVT_DTPCD ";
			M_strSQLQRY+= " AND IVT_STSFL!= 'X' AND A.PT_DSTCD=E.CMT_CODCD AND E.CMT_CGMTP='SYS' AND E.CMT_CGSTP='COXXDST' AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "; 
			if(rdbDSPSP.isSelected())
				M_strSQLQRY+= " AND IVT_DTPCD='"+txtDSPTP.getText().trim()+"'";
			if(rdbTRPSP.isSelected())
				M_strSQLQRY+= " AND IVT_TRPCD='"+txtTRPCD.getText().trim()+"'";
			if(!txtCSACD.getText().equals(""))
				M_strSQLQRY+= " AND IVT_SALTP IN ('14') AND IVT_DSRTP='G' AND IVT_DSRCD='"+txtCSACD.getText().trim()+"' ";
			else
				M_strSQLQRY+= " AND IVT_SALTP IN ('01','03','04','05') ";
				
			M_strSQLQRY+= "GROUP BY IVT_INVNO,CONVERT(varchar,IVT_INVDT,101),A.PT_PRTNM ,E.CMT_CODDS,B.PT_PRTNM,IVT_TRPCD,C.PT_PRTNM,";
			M_strSQLQRY+= " IVT_LR_NO,IVT_LRYNO,D.CMT_CODDS,IVT_INVVL ";
			M_strSQLQRY+= " ORDER BY C.PT_PRTNM,IVT_INVNO ";
			

			System.out.println(M_strSQLQRY);			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					L_strTRPCD = nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),"");
					L_strTRPNM = nvlSTRVL(M_rstRSSET.getString("TRPNM"),"");
					L_strPTRPNM = L_strTRPNM;
					L_strCNSNM = nvlSTRVL(M_rstRSSET.getString("CNSNM"),"");
					L_strDSTDS = nvlSTRVL(M_rstRSSET.getString("DSTDS"),"");
					L_strBYRNM = nvlSTRVL(M_rstRSSET.getString("BYRNM"),"");
					L_strINVNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
					L_strINVDT = nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),"");
					L_strLRNO  = nvlSTRVL(M_rstRSSET.getString("IVT_LR_NO"),"");
					L_strLRYNO = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"");
					L_strDSPDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					//L_strDSTDS = nvlSTRVL(M_rstRSSET.getString("IVT_DSTDS"),"");
	   				L_dblTIVQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("TOT_INVQT"),"0"));
	   				L_dblINVVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
					
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
					}
					if(L_flg1STSFL){
						L_strPTRPCD = L_strTRPCD;
						L_strCTRPCD = L_strTRPCD;
						L_flg1STSFL = false;
					}

			while(!L_flgEOF)		
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(L_strTRPNM+"\n\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				cl_dat.M_intLINNO_pbst+= 2;
				L_strPTRPCD = L_strTRPCD;
				L_strPTRPNM = L_strTRPNM;
				while(L_strTRPCD.equals(L_strPTRPCD) && !L_flgEOF)
				{
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
						prnHEADER();
					}
//					dosREPORT.writeBytes("Consignee                                  Destination       Inv.No      LR.No.          Qty.   Rate     Desp.Type   Del.on      Bill No.   Bill Amt.  \n");		    			
//					dosREPORT.writeBytes("Buyer                                                        Date        Vehicle No.     (MT)  (Rs/MT)               Trans.Time  Date         (Rs.)                \n");		    			
					
					intRECCT++;
						dosREPORT.writeBytes(padSTRING('R',L_strCNSNM,43));
						if(rdbPURBP.isSelected())
						{	
							dosREPORT.writeBytes(padSTRING('R',L_strDSTDS,18));
						}	
						dosREPORT.writeBytes(padSTRING('R',L_strINVNO,12));
						dosREPORT.writeBytes(padSTRING('R',L_strLRNO,13));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTIVQT,3),10));
						dosREPORT.writeBytes(padSTRING('L',"",11));
						dosREPORT.writeBytes(padSTRING('R',L_strDSPDS,9));
					if(rdbPURIN.isSelected())
					{	
						dosREPORT.writeBytes(padSTRING('R',L_strDSTDS,17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblINVVL,2),12));
					}	
					dosREPORT.writeBytes("\n");
					if(L_strBYRNM.equals(L_strCNSNM))
						dosREPORT.writeBytes(padSTRING('L',"",43));
					else
					dosREPORT.writeBytes(padSTRING('R',L_strBYRNM,43));
					if(rdbPURBP.isSelected())
						dosREPORT.writeBytes(padSTRING('L',"",18));
					dosREPORT.writeBytes(padSTRING('R',L_strINVDT,12));
					dosREPORT.writeBytes(padSTRING('R',L_strLRYNO,15));
					dosREPORT.writeBytes("\n");
   					cl_dat.M_intLINNO_pbst+= 2;
					L_dblTTRQT+=  L_dblTIVQT;
					L_dblGTOQT+=  L_dblTIVQT;
					L_dblIVAMT+=  L_dblINVVL;
					L_dblGIVAMT+= L_dblINVVL;
						if(!M_rstRSSET.next())
						{
							L_flgEOF = true;
							break;
						}
					L_strTRPCD = nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),"");	
					L_strTRPNM = nvlSTRVL(M_rstRSSET.getString("TRPNM"),"");
					L_strCNSNM = nvlSTRVL(M_rstRSSET.getString("CNSNM"),"");
					L_strDSTDS = nvlSTRVL(M_rstRSSET.getString("DSTDS"),"");
					L_strBYRNM = nvlSTRVL(M_rstRSSET.getString("BYRNM"),"");
					L_strINVNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
					L_strINVDT = nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),"");
					L_strLRNO  = nvlSTRVL(M_rstRSSET.getString("IVT_LR_NO"),"");
					L_strLRYNO = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"");
					L_strDSPDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					//L_strDSTDS = nvlSTRVL(M_rstRSSET.getString("IVT_DSTDS"),"");
	   				L_dblTIVQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("TOT_INVQT"),"0"));
	   				L_dblINVVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
				}//	while((L_strTRPCD).equals(L_strPTRPCD) && !L_EOF)
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Total for "+L_strPTRPNM,68));
				if(rdbPURBP.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('L',"",18));
				}
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTTRQT,3),10));
				if(rdbPURIN.isSelected())
				{	
					dosREPORT.writeBytes(padSTRING('L',"",37));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblIVAMT,2),12));
				}	
				dosREPORT.writeBytes("\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				cl_dat.M_intLINNO_pbst+= 3;
				if(rdbPURBP.isSelected() && rdbTRPAL.isSelected()) 
				{	
					dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------");		
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
					prnHEADER();
				}	
				L_dblTTRQT = 0;
				L_dblIVAMT = 0;
			}//while(eof())
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Grand Total ",68));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOQT,3),10));
				if(rdbPURIN.isSelected())
				{	
					dosREPORT.writeBytes(padSTRING('L',"",37));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGIVAMT,2),12));
				}	
				dosREPORT.writeBytes("\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				cl_dat.M_intLINNO_pbst+= 3;
				L_dblGTOQT = 0;
				L_dblGIVAMT = 0;
		}		prnFOOTR();
									
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
		    if(M_rstRSSET != null)
				M_rstRSSET.close();						
			else
    		{							
				setMSG("No Data Found..",'E');
				return ;
			}
			if(intRECCT == 0)
			{
				setMSG("No Data Found for the given selection..",'E');
				return ;
			}
			}	
		}	
	
	 	catch(Exception L_EX)
		{		    
			setMSG(L_EX+" getDATA",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}
	
	/**
	*Method to fetch Data from Database and start creation of the output file for Summary Report.
	*/
	private void getDATA1()
	{ 	    		
		//String L_strFMDAT,L_strTODAT,L_strDSTDS;
		String L_strDSTDS;
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblINVQT=0,L_dblZONTOT=0,L_dblCNSTOT=0,L_dblGTOQT=0;
		double L_dblSCTOT=0,L_dblSHTOT=0,L_dblSPTOT=0,L_dblCRTOT=0,L_dblOTTOT=0,L_dblDETOT=0;
		double L_dblSCZTOT=0,L_dblSHZTOT=0,L_dblSPZTOT=0,L_dblCRZTOT=0,L_dblOTZTOT=0,L_dblDEZTOT=0;
		double L_dblSCGTOT=0,L_dblSHGTOT=0,L_dblSPGTOT=0,L_dblCRGTOT=0,L_dblOTGTOT=0,L_dblDEGTOT=0;
		
		
		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPZONCD = new String();
		String L_strPZONDS = new String();
		String L_strPCNSCD = new String();
		String L_strPSALTP = new String();
		String L_strPPRDCD = new String();	
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCZONCD = new String();
		String L_strCZONDS = new String();
		String L_strCCNSCD = new String();
		String L_strCSALTP = new String();
		String L_strCPRDCD = new String();	

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strZONCD = new String();
		String L_strZONDS = new String();
		String L_strCNSCD = new String();
		String L_strCNSNM = new String();
		String L_strSALTP = new String();
		String L_strPRDCD = new String();	
		
		try
	    {	
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Consigneewise Despatch Statement </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();
			//L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			//L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			//strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			//strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			setMSG("Report Generation is in Progress.......",'N');		

			if(rdbTRNIV.isSelected())
			{	
				M_strSQLQRY = "	SELECT IVT_ZONCD,A.CMT_CODDS ZONDS,IVT_SALTP,B.CMT_CODDS SALDS,IVT_CNSCD,PT_PRTNM,left(C.CMT_CODDS,15) DSTDS,left(IVT_PRDCD,4) IVT_PRDCD,IVT_INVQT ";
				M_strSQLQRY+= " FROM MR_IVTRN,CO_CDTRN A,CO_CDTRN B,CO_CDTRN C,CO_PTMST WHERE IVT_SALTP IN ('01','03','04','05') AND ";
				M_strSQLQRY+= " CONVERT(varchar,IVT_INVDT,101) between '"+strFMDAT+"' AND '"+strTODAT+"' AND IVT_STSFL != 'X'  AND ";
				M_strSQLQRY+= " A.CMT_CGMTP='SYS' AND A.CMT_CGSTP='MRXXZON' AND A.CMT_CODCD=IVT_ZONCD AND ";
				M_strSQLQRY+= " B.CMT_CGMTP='SYS' AND B.CMT_CGSTP='MR00SAL' AND B.CMT_CODCD=IVT_SALTP AND ";
				M_strSQLQRY+= " PT_PRTTP='C' AND PT_PRTCD=IVT_CNSCD AND C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='COXXDST' AND PT_DSTCD=C.CMT_CODCD AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
				if(rdbDSPSP.isSelected())
					M_strSQLQRY+= " AND IVT_DTPCD='"+txtDSPTP.getText().trim()+"' ";
				M_strSQLQRY+= " ORDER BY IVT_ZONCD,IVT_CNSCD ";
			}	
			if(rdbTRNBK.isSelected())
			{   
				M_strSQLQRY = "	SELECT IN_ZONCD IVT_ZONCD,A.CMT_CODDS ZONDS,IN_SALTP IVT_SALTP,B.CMT_CODDS SALDS,";
				M_strSQLQRY+= " IN_CNSCD IVT_CNSCD,PT_PRTNM,left(C.CMT_CODDS,15) DSTDS,left(INT_PRDCD,4) IVT_PRDCD,INT_INDQT IVT_INVQT";
				M_strSQLQRY+= " FROM VW_INTRN,CO_CDTRN A,CO_CDTRN B,CO_CDTRN C,CO_PTMST WHERE IN_SALTP IN ('01','04','03','05') AND ";
				M_strSQLQRY+= " IN_BKGDT between '"+strFMDAT+"' AND '"+strTODAT+"' AND INT_STSFL != 'X'  AND ";
				M_strSQLQRY+= " A.CMT_CGMTP='SYS' AND A.CMT_CGSTP='MRXXZON' AND A.CMT_CODCD=IN_ZONCD AND ";
				M_strSQLQRY+= " B.CMT_CGMTP='SYS' AND B.CMT_CGSTP='MR00SAL' AND B.CMT_CODCD=IN_SALTP AND ";
				M_strSQLQRY+= " PT_PRTTP='C' AND PT_PRTCD=IN_CNSCD  AND C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='COXXDST' AND PT_DSTCD=C.CMT_CODCD  AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  ";
				if(rdbDSPSP.isSelected())
					M_strSQLQRY+= " AND IN_DTPCD='"+txtDSPTP.getText().trim()+"' ";
				M_strSQLQRY+= " ORDER BY IN_ZONCD,IN_CNSCD ";
			}
			
			System.out.println(M_strSQLQRY);			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					L_strZONCD = nvlSTRVL(M_rstRSSET.getString("IVT_ZONCD"),"");
					L_strZONDS = nvlSTRVL(M_rstRSSET.getString("ZONDS"),"");
					L_strCNSCD = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
					L_strCNSNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					L_strDSTDS = nvlSTRVL(M_rstRSSET.getString("DSTDS"),"");
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),"");
					L_strSALTP = nvlSTRVL(M_rstRSSET.getString("IVT_SALTP"),"");
	   				L_dblINVQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
					
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
					}
					if(L_flg1STSFL){
						L_strPZONCD = L_strZONCD;
						L_strCZONCD = L_strZONCD;
						L_strPZONDS = L_strZONDS;
						L_strPCNSCD = L_strCNSCD;
						L_strCCNSCD = L_strCNSCD;
						L_flg1STSFL = false;
					}

			while(!L_flgEOF)		
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(L_strZONDS+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				cl_dat.M_intLINNO_pbst+= 1;
				L_strZONCD = L_strCZONCD;
				L_strPZONCD = L_strZONCD;
				L_strZONDS = L_strCZONDS;
				L_strPZONDS = L_strZONDS;
				while(L_strZONCD.equals(L_strPZONCD) && !L_flgEOF)
				{
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
						prnHEADER();
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes(padSTRING('L',"",8));
					dosREPORT.writeBytes(padSTRING('R',L_strCNSNM,43));
					dosREPORT.writeBytes(padSTRING('R',L_strDSTDS,15));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					L_strZONCD = L_strCZONCD;
					L_strPZONCD = L_strZONCD;
					L_strPZONDS = L_strZONDS;
					L_strCNSCD = L_strCCNSCD;
					L_strPCNSCD = L_strCNSCD;
				while((L_strZONCD+L_strCNSCD).equals(L_strPZONCD+L_strPCNSCD) && !L_flgEOF)
				{
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
						prnHEADER();
					}
					intRECCT++;
					if(L_strSALTP.equals("01") || L_strSALTP.equals("04") || L_strSALTP.equals("05"))
					{	
						if(L_strPRDCD.substring(0,4).equals("5111") && L_strPRDCD.substring(2,2)!= "95" && L_strPRDCD.substring(2,2)!= "97" && L_strPRDCD.substring(2,2)!= "98")
						{																										 
							L_dblSCTOT+= L_dblINVQT;
							L_dblSCZTOT+= L_dblINVQT;
							L_dblSCGTOT+= L_dblINVQT;
							L_dblCNSTOT+= L_dblINVQT;
							L_dblGTOQT+= L_dblINVQT;
						}	
						if(L_strPRDCD.substring(0,4).equals("5112") && L_strPRDCD.substring(2,2)!= "95" && L_strPRDCD.substring(2,2)!= "97" && L_strPRDCD.substring(2,2)!= "98")
						{
							L_dblSHTOT+= L_dblINVQT;
							L_dblSHZTOT+= L_dblINVQT;
							L_dblSHGTOT+= L_dblINVQT;
							L_dblCNSTOT+= L_dblINVQT;
							L_dblGTOQT+= L_dblINVQT;
						}	
						if(L_strPRDCD.substring(0,2).equals("52") && L_strPRDCD.substring(2,2)!= "95" && L_strPRDCD.substring(2,2)!= "97" && L_strPRDCD.substring(2,2)!= "98")
						{
							L_dblSPTOT+= L_dblINVQT;
							L_dblSPZTOT+= L_dblINVQT;
							L_dblSPGTOT+= L_dblINVQT;
							L_dblCNSTOT+= L_dblINVQT;
							L_dblGTOQT+= L_dblINVQT;
						}	
						if(L_strPRDCD.substring(0,2).equals("54") && L_strPRDCD.substring(2,2)!= "95" && L_strPRDCD.substring(2,2)!= "97" && L_strPRDCD.substring(2,2)!= "98")
						{
							L_dblCRTOT+= L_dblINVQT;
							L_dblCRZTOT+= L_dblINVQT;
							L_dblCRGTOT+= L_dblINVQT;
							L_dblCNSTOT+= L_dblINVQT;
							L_dblGTOQT+= L_dblINVQT;
						}	
						if(L_strPRDCD.substring(2,2).equals("95") || L_strPRDCD.substring(2,2).equals("97") || L_strPRDCD.substring(2,2).equals("98"))
						{
							L_dblOTTOT+= L_dblINVQT;
							L_dblOTZTOT+= L_dblINVQT;
							L_dblOTGTOT+= L_dblINVQT;
							L_dblCNSTOT+= L_dblINVQT;
							L_dblGTOQT+= L_dblINVQT;
						}	
					}
					if(L_strSALTP.equals("03"))
					{
						L_dblDETOT+= L_dblINVQT;
						L_dblDEZTOT+= L_dblINVQT;
						L_dblDEGTOT+= L_dblINVQT;
					}	
						if(!M_rstRSSET.next())
						{
							L_flgEOF = true;
							break;
						}
					L_strCZONCD = nvlSTRVL(M_rstRSSET.getString("IVT_ZONCD"),"");
					L_strCZONDS = nvlSTRVL(M_rstRSSET.getString("ZONDS"),"");
					L_strCCNSCD = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
					L_strCNSNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					L_strDSTDS = nvlSTRVL(M_rstRSSET.getString("DSTDS"),"");
					L_strCPRDCD = nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),"");
					L_strCSALTP = nvlSTRVL(M_rstRSSET.getString("IVT_SALTP"),"");
	   				L_dblINVQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
					
					L_strZONCD = L_strCZONCD;
					L_strZONDS = L_strCZONDS;
					L_strCNSCD = L_strCCNSCD;
					L_strPRDCD = L_strCPRDCD;
					L_strSALTP = L_strCSALTP;
				}//	while((L_strZONCD).equals(L_strPZONCD) && L_strCNSCD.equals(L_strPCNSCD) && !L_flgEOF)
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSCTOT,3),10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSHTOT,3),10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSPTOT,3),10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCRTOT,3),10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblOTTOT,3),10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCNSTOT,3),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDETOT,3),12));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst+= 1;
				L_dblSCTOT = 0;
				L_dblSHTOT = 0;
				L_dblSPTOT = 0;
				L_dblCRTOT = 0;
				L_dblOTTOT = 0;
				L_dblCNSTOT = 0;
				L_dblDETOT = 0;
				L_strPCNSCD = L_strCNSCD;
			}//while(L_strZONCD).equals(L_strPZONCD) && !flgEOF)
				L_dblZONTOT= L_dblSCZTOT+L_dblSHZTOT+L_dblSPZTOT+L_dblCRZTOT+L_dblOTZTOT;
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Total for "+L_strPZONDS,66));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSCZTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSHZTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSPZTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCRZTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblOTZTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblZONTOT,3),12));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDEZTOT,3),12)+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				cl_dat.M_intLINNO_pbst+= 3;
/**				if(rdbPURBP.isSelected())
				{	
					dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------");		
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
					prnHEADER();
				}*/	
				L_dblSCZTOT = 0;
				L_dblSHZTOT = 0;
				L_dblSPZTOT = 0;
				L_dblCRZTOT = 0;
				L_dblOTZTOT = 0;
				L_dblDEZTOT = 0;
				L_dblZONTOT = 0;
				L_strPZONCD = L_strZONCD;
			}//while(eof())
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Grand Total ",66));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSCGTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSHGTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSPGTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCRGTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblOTGTOT,3),10));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOQT,3),12));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDEGTOT,3),12)+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				cl_dat.M_intLINNO_pbst+= 3;
				L_dblSCGTOT = 0;
				L_dblSHGTOT = 0;
				L_dblSPGTOT = 0;
				L_dblCRGTOT = 0;
				L_dblOTGTOT = 0;
				L_dblGTOQT = 0;
				L_dblDEGTOT = 0;
		}		
			prnFOOTR();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
		    if(M_rstRSSET != null)
				M_rstRSSET.close();						
			else
    		{							
				setMSG("No Data Found..",'E');
				return ;
			}
			if(intRECCT == 0)
			{
				setMSG("No Data Found for the given selection..",'E');
				return ;
			}
			}	
		}	
	
	 	catch(Exception L_EX)
		{		    
			setMSG(L_EX+" getDATA",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}
	
	
	/**
	Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			if(rdbRPTSM.isSelected())
			strRPHDR = "Consigneewise Despatch Statement From "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim()+" for "+txtDSPDS.getText()+" type Despatch Based on "+strTRNDS;				
			if(rdbRPTDT.isSelected())
			{	
				if(rdbPURBP.isSelected())
				strRPHDR = "Transporterwise Despatch Statement from "+txtFMDAT.getText().trim()+" to "+txtTODAT.getText().trim()+" for "+txtDSPDS.getText()+" type Despatch ";	
				if(rdbPURIN.isSelected())
				strRPHDR = "Despatch Statement for Insurance from "+txtFMDAT.getText().trim()+" to "+txtTODAT.getText().trim()+" for "+txtDSPDS.getText()+" type Despatch ";
				strRPHDR1 = "Consignment Stockist : '"+txtCSACD.getText()+"'"+" NAME : '"+txtCSANM.getText().trim()+"'"+" DESTINATION : '"+strDSTDS+"'" ;
			}
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",120));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',strRPHDR,120));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			if(!txtCSACD.getText().trim().equals(""))
				dosREPORT.writeBytes(padSTRING('R',strRPHDR1,120)+"\n");			
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			if(rdbRPTDT.isSelected())
			{	
				if(rdbPURIN.isSelected())
				{	
					dosREPORT.writeBytes("Consignee                                  Inv.No      LR.No.           Quantity       Desp.Type   Destination       Inv.Amount                \n");		    			
					dosREPORT.writeBytes("Buyer                                      Date        Vehicle No.        (MT)                                          (Rs.)                  \n");		    			
				}
				if(rdbPURBP.isSelected())
				{
					dosREPORT.writeBytes("Consignee                                  Destination       Inv.No      LR.No.            Qty.   Rate     Desp.    Del.on      Bill No.  Amt.  \n");		    			
					dosREPORT.writeBytes("Buyer                                                        Date        Vehicle No.       (MT)  (Rs/MT)   Type     Trans.Time  Date      (Rs.) \n");		    			
				}	
			}
			if(rdbRPTSM.isSelected())
			{
				dosREPORT.writeBytes("Zone                                                                                                                                             \n");		    			
				dosREPORT.writeBytes("        Consignee                                  Destination          GPPS      HIPS      SPS      CRESIN    OTHERS     TOTAL     DEEMED EXPORT \n");		    			
			}	
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 7;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
	/**
  	*Method to Generate the Page Footer of the Report.
	 */
private void prnFOOTR()
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
	    {   
			prnFMTCHR(dosREPORT,M_strEJT);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
		}	
	}
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnFOOTR",'E');
		}	
}

class INPVF extends InputVerifier
{
String L_strTRPCD="",L_strDSPTP="",L_strCSACD="",L_strCSANM="";

	public boolean verify(JComponent Jcomp)
	{
		if(((JTextField)Jcomp).getText().length() == 0)
			return true;
		if(Jcomp == txtTRPCD)
		{
			L_strTRPCD = txtTRPCD.getText().toString().trim().toUpperCase();
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST where ";
			M_strSQLQRY +=" PT_PRTTP = 'T' AND PT_PRTCD ='"+txtTRPCD.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				try
				{
					if(!M_rstRSSET.next())
					{	
						setMSG("Invalid Buyer Code....Press F1 for Help..",'E');
						return false;
					}
					else
					{	
	                    txtTRPNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
						M_rstRSSET.close();
						return true;	
					}	
				}	
				catch(Exception L_EX)
				{
					setMSG(L_EX," Error Validating Transporter Code");
				}
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}	

		if(Jcomp == txtCSACD)
		{
			L_strCSACD = txtCSACD.getText().toString().trim().toUpperCase();
			M_strSQLQRY = "Select DISTINCT PT_PRTCD,PT_PRTNM,PT_DSTCD,CMT_CODDS from CO_PTMST,CO_CDTRN,MR_IVTRN where ";
			M_strSQLQRY +=" PT_PRTTP = 'G' AND PT_PRTCD ='"+txtCSACD.getText().trim()+"' AND PT_PRTCD=IVT_DSRCD";
			M_strSQLQRY+= " AND IVT_DSRTP='G' AND CONVERT(varchar,IVT_INVDT,101) BETWEEN '"+strFMDAT+"' AND '"+strTODAT+"' ";
			M_strSQLQRY+=" AND PT_DSTCD=CMT_CODCD AND CMT_CGSTP='COXXDST' AND IVT_DSRCD= '"+txtCSACD.getText().trim()+"'";
			M_strSQLQRY+=" AND IVT_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'  AND IVT_SALTP IN ('14')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				try
				{
					if(!M_rstRSSET.next())
					{	
						System.out.println("not found");
						setMSG("Invalid C.Stk. Code....Press F1 for Help..",'E');
						return false;
					}
					else
					{	
	                    txtCSANM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
						strDSTDS=nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						M_rstRSSET.close();
						return true;	
					}	
				}	
				catch(Exception L_EX)
				{
					setMSG(L_EX," Error Validating C.Stockist Code");
				}
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}	
		
		
		if(Jcomp == txtDSPTP)
		{
			L_strDSPTP = txtDSPTP.getText().toString().trim().toUpperCase();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where ";
			M_strSQLQRY +=" CMT_CGMTP='SYS' AND CMT_CGSTP = 'MRXXDTP' AND CMT_CODCD = '"+txtDSPTP.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				try
				{
					if(!M_rstRSSET.next())
					{	
						setMSG("Invalid Despatch Type....Press F1 for Help..",'E');
						return false;
					}
					else
					{	
	                    txtDSPDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						M_rstRSSET.close();
						return true;
					}	
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX," Error Validating Despatch Type");
				}
			}
		}
		return false;
	}
}	
}
