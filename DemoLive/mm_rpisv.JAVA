/*
System Name		: Materials Management System
Program Name	: Issue Valuation Reports
Author			: Mr.S.R.Mehesare
Modified Date	: 8/11/2005
Documented Date	: 8/11/2005
Version			: v2.0.0
*/

import java.io.FileOutputStream;import java.io.DataOutputStream;import java.awt.Color;
import javax.swing.JLabel;import javax.swing.JPanel;import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import javax.swing.JComponent;import javax.swing.InputVerifier;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.util.Enumeration;import java.util.StringTokenizer;import java.util.Vector;
import java.util.Hashtable;import java.util.Arrays;import java.sql.ResultSet;

/**<pre>
System Name : Material Management System.
 
Program Name : Issue Valuation Reports 

Purpose : A common report program for : 
          1) Department wise Consumption.
          2) Tagwise Consumption.
          3) Usage wise consuption.

List of tables used :
Table Name   Primary key                                        Operation done
                                                    Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST     CT_GRPCD,CT_CODTP,CT_MATCD                                 #
CO_CDTRN     CMT_CGMTP,CMT_CGSTP,CMT_CODCD                              #
MM_EQMST                                                                #
MM_ISMST     IS_STRTP,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_TAGNO,IS_BATNO      #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name    Column Name      Table Name    Type/Size       Description
--------------------------------------------------------------------------------------
cmbISSTP      IS_ISSTP         MM_ISMST      VARCHAR(2)      Issue Type
cmbRPTTP                                                     Specified Report Type
txtPRTCD      IS_PRTCD         MM_ISMST      VARCHAR(3)      Party Code
txtPRTNM      PT_PRTNM         CO_PTMST      VARCHAR(40)     Party Name
txtFMDPT1     CMT_CODCD        CO_CDTRN      VARCHAR(15)     Department Code
txtFMDNM1     CMT_CODDS        CO_CDTRN      VARCHAR(30)     Department Name
txtTODPT1     CMT_CODCD        CO_CDTRN      VARCHAR(15)     Department Code
txtTODNM1     CMT_CODDS        CO_CDTRN      VARCHAR(30)     Department Name
txtFMDAT1     IS_ISSDT         MM_ISMST      Date            Issue Date
txtTODAT1     IS_ISSDT         MM_ISMST      Date            Issue Date
txtFMDPT2     CMT_CODCD        CO_CDTRN      VARCHAR(15)     Department Code
txtFMDNM2     CMT_CODDS        CO_CDTRN      VARCHAR(30)     Department Name
txtTODPT2     CMT_CODCD        CO_CDTRN      VARCHAR(15)     Department Code
txtTODNM2     CMT_CODDS        CO_CDTRN      VARCHAR(30)     Department Name
txtFMDAT2     IS_ISSDT         MM_ISMST      Date            Issue Date
txtTODAT2     IS_ISSDT         MM_ISMST      Date            Issue Date
txtFMTAG2     EQ_TAGNO         MM_EQMST      VARCHAR(12)     Tag Number
txtFMTNM2     EQ_TAGDS         MM_EQMST      VARCHAR(40)     Tag Description
txtTOTAG2     EQ_TAGNO         MM_EQMST      VARCHAR(12)     Tag Number
txtTOTNM2     EQ_TAGDS         MM_EQMST      VARCHAR(40)     Tag Description
txtFMDAT3     IS_ISSDT         MM_ISMST      Date            Date
txtTODAT3     IS_ISSDT         MM_ISMST      Date            Date
--------------------------------------------------------------------------------------

Report Criterias Available :  
   1) Department Wise Consuption :
        1) For All Departments (If Department field are blank)
        2) For a Specific department (If from and to department is same)
        3) For selected Departments between from and to
        4) For a given Period
   2) Tag wise Consumption
        1) For selected Tags between from and to
        2) For All Departments (If Department field are blank)
        3) For a Specific department (If from and to department is same)
        4) For selected Departments between from and to
        5) For a given Period
   3) Usage wise Consuption
        1) For a given Period
        2) Buyer ( one or many)
        3) Perticular indent no.
        4) Perticular customer group
        5) Indent date from to
These reports are available in dos format only. Separate panels are used for input components for separate reports
<I>
<B>Query : </B>
1)For Department Wise Consuption Report, Data is taken from MM_ISMST & CO_CTMST for :-
    1) IS_MMSBS = Selected subsystem code. 
   if Department codes are provide for range selection 
    2) AND IS_DPTCD between given range of Department Code.
    3) IS_ISSTP Specified Issue type  
   if Party Name is provided
    4) AND IS_PRTCD = Specifed Party code.
    5) AND IS_ISSDT between given Date Range.
    6) AND CT_MATCD=IS_MATCD 
    group by IS_MATCD, CT_MATDS,CT_UOMCD,IS_DPTCD 

2) For Tag wise Consumption Report, Data is taken from MM_ISMST, MM_EQMST & CO_CTMST for :-
    1) IS_MMSBS = Selected subsystem code. 
   if Department codes are provide for range selection 
    2) AND IS_DPTCD between given range of Department Code.
   if Tag Number Range is Secified 
    3) IS_TAGNO between specifed range of Tage numbers.				
    4) IS_ISSDT between specified date range
    5) AND CT_MATCD=IS_MATCD 
    6) AND EQ_TAGNO=IS_TAGNO 
    group by IS_TAGNO, IS_MATCD, CT_MATDS,CT_UOMCD,IS_DPTCD, EQ_TAGDS					

   3) For Usage wise Consuption Report, Data is taken from MM_ISMST for :-
    1) IS_ISSDT between Specifed date renge.
    group by IS_DPTCD, IS_USGTP "
<B>Validations & Other Information:</B>    
    - Date Range specifed must be valid i.e. To date must be greater than from date & smallar than current date.
    - Party Code specified must be valid.
    - Department codes specified must be valid. 
</I> */

class mm_rpisv extends cl_rbase
{										/** JComboBox to specify & to display Issue Type.*/
	private JComboBox cmbISSTP;			/** JTextField to display to enter Product Code.*/
	private JTextField txtPRTCD;		/** JTextField to display Product Name.*/
	private JTextField txtPRTNM;		/** JComboBox to select & to display Report Types.*/
	private cl_Combo cmbRPTTP;			/** JTextField to enter & to display From Department Code in Department wise consumption report panel */	
	private JTextField txtFMDPT1;		/** JTextField to enter & to display From Department Name in Department wise consumption report panel */
	private JTextField txtFMDNM1;		/** JTextField to enter & to display To Department Code in Department wise consumption report panel */
	private JTextField txtTODPT1;		/** JTextField to enter & to display To Department Name in Department wise consumption report panel */
	private JTextField txtTODNM1;		/** JTextField to enter & to display From Date in Department wise consumption report panel */
	private JTextField txtFMDAT1;		/** JTextField to enter & to display To date in Department wise consumption report panel */
	private JTextField txtTODAT1;		/** JPanel for Department wise consumption report  */
	private JPanel pnlDPTWS;			/** JTextField to enter & to display From Department Code in TAG wise consumption report panel */	
	private JTextField txtFMDPT2;		/** JTextField to enter & to display From Department Name in TAG wise consumption report panel */
	private JTextField txtFMDNM2;		/** JTextField to enter & to display To Department Code in TAG wise consumption report panel */
	private JTextField txtTODPT2;		/** JTextField to enter & to display To Department Name in TAG wise consumption report panel */
	private JTextField txtTODNM2;		/** JTextField to enter & to display From Date in TAG wise consumption report panel */
	private JTextField txtFMDAT2;		/** JTextField to enter & to display To Date in TAG wise consumption report panel */
	private JTextField txtTODAT2;		/** JTextField to enter & to display From Tag in TAG wise consumption report panel */
	private JTextField txtFMTAG2;		/** JTextField to enter & to display From Tag Name in TAG wise consumption report panel */
	private JTextField txtFMTNM2;		/** JTextField to enter & to display To Tag in TAG wise consumption report panel */
	private JTextField txtTOTAG2;		/** JTextField to enter & to display To Tag Name in TAG wise consumption report panel */
	private JTextField txtTOTNM2;		/** JTextField to enter & to display Panel for Tag wise consumption report  */
	private JPanel pnlTAGWS;			/** From Date in USAGE wise consumption report panel */	
	private JTextField txtFMDAT3;		/** JTextField to enter & to display To Date in USAGE wise consumption report panel */
	private JTextField txtTODAT3;		/** JPanel for Usage wise consumption report  */
	private JPanel pnlUSGWS;			/** Final String Array for report types available */
	
	private final String[] staRPTTP_fn=new String []{"Department Wise Consumption",
													 "Tag Wise Comsumption",
													 "Usage wise Consumption"};
										/** Final int variable for Index of DEPARTMENT wise Consupmtion Report in cmbRPTTP */
	private final int intDPTWS_fn=1;	/** Final int variable for Index of TAG wise Consupmtion Report in cmbRPTTP */
	private final int intTAGWS_fn=2;	/** Final int variable for Index of USAGE wise Consupmtion Report in cmbRPTTP */
	private final int intUSGWS_fn=3;	/** DataOutputStream to hold Stream of data to generate the Report.*/
	private DataOutputStream dosREPORT;	/** FileOutputStream to generate the Report from the Stream of Data.*/
	private FileOutputStream fosREPORT;	/** Hashtable for Department Name. KEY : DPTCD; VALUE : DPTNM.*/
	private Hashtable<String,String> hstDPTNM;			/** Hashtable for help data on Department KEY : DPTCD ; VALUE : String[]{DPTCD, DPTNM}.*/
	private Hashtable<String,String[]> hstHLPDT;			/** Vector for usage codes */
	private Vector<String> vtrUSGCD;			/** Vector for usage code descriptions */
	private Vector<String> vtrUSGDS;			/** String Variable to append the dotted Line in the Report.*/		
	private String strDOTLN;			/** String Varaible for generated report File Name.*/
	private String strFILNM;			/** Integer variable to count the number of records fetched to block the Report if no data found.*/
	private int intRECCT;
	
	mm_rpisv()
	{
		super(2);
		try
		{
			setMatrix(20,6);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			
			add(new JLabel("Report Type"),2,2,1,1,this,'L');
			add(cmbRPTTP=new cl_Combo(),2,3,1,2,this,'L');
			cmbRPTTP.addItem("Select Report Type");
			for (int i=0;i<staRPTTP_fn.length;i++)
				cmbRPTTP.addItem(staRPTTP_fn[i],"");
			cmbRPTTP.addActionListener(this);
		//Create PAnel for Department wise consumption & hide it.
			pnlDPTWS=new JPanel(null);
			add(new JLabel("Issue Type"),1,1,1,1,pnlDPTWS,'L');
			add(cmbISSTP=new JComboBox(),1,2,1,2,pnlDPTWS,'L');
            add(new JLabel("From Deptt."),2,1,1,1,pnlDPTWS,'L');
			add(txtFMDPT1=new TxtNumLimit(3.0),2,2,1,1,pnlDPTWS,'L');
			add(txtFMDNM1=new JTextField(),2,3,1,2,pnlDPTWS,'L');
			add(new JLabel("To Deptt."),3,1,1,1,pnlDPTWS,'L');
			add(txtTODPT1=new TxtNumLimit(3.0),3,2,1,1,pnlDPTWS,'L');
			add(txtTODNM1=new JTextField(),3,3,1,2,pnlDPTWS,'L');
			add(new JLabel("From Date"),4,1,1,1,pnlDPTWS,'L');
			add(txtFMDAT1=new TxtDate(),4,2,1,1,pnlDPTWS,'L');
			add(new JLabel("To Date"),5,1,1,1,pnlDPTWS,'L');
			add(txtTODAT1=new TxtDate(),5,2,1,1,pnlDPTWS,'L');
			add(new JLabel("Contractor"),6,1,1,1,pnlDPTWS,'L');
			add(txtPRTCD=new TxtLimit(5),6,2,1,1,pnlDPTWS,'L');
			add(txtPRTNM=new TxtLimit(40),6,3,1,2,pnlDPTWS,'L');
			add(pnlDPTWS,4,2,8,5.1,this,'L');
			pnlDPTWS.setVisible(false);

		//Create PAnel for Tag wise consumption & hide it.
			pnlTAGWS=new JPanel(null);
			add(new JLabel("From Tag No."),1,1,1,1,pnlTAGWS,'L');
			add(txtFMTAG2=new JTextField(),1,2,1,1,pnlTAGWS,'L');
			add(txtFMTNM2=new JTextField(),1,3,1,2,pnlTAGWS,'L');
			add(new JLabel("To Tag No."),2,1,1,1,pnlTAGWS,'L');
			add(txtTOTAG2=new JTextField(),2,2,1,1,pnlTAGWS,'L');
			add(txtTOTNM2=new JTextField(),2,3,1,2,pnlTAGWS,'L');

			add(new JLabel("From Deptt."),3,1,1,1,pnlTAGWS,'L');
			add(txtFMDPT2=new TxtNumLimit(3.0),3,2,1,1,pnlTAGWS,'L');
			add(txtFMDNM2=new JTextField(),3,3,1,2,pnlTAGWS,'L');
			add(new JLabel("To Deptt."),4,1,1,1,pnlTAGWS,'L');
			add(txtTODPT2=new TxtNumLimit(3.0),4,2,1,1,pnlTAGWS,'L');
			add(txtTODNM2=new JTextField(),4,3,1,2,pnlTAGWS,'L');
			add(new JLabel("From Date"),5,1,1,1,pnlTAGWS,'L');
			add(txtFMDAT2=new TxtDate(),5,2,1,1,pnlTAGWS,'L');
			add(new JLabel("To Date"),6,1,1,1,pnlTAGWS,'L');
			add(txtTODAT2=new TxtDate(),6,2,1,1,pnlTAGWS,'L');
			add(pnlTAGWS,3,2,8,5.1,this,'L');
			pnlTAGWS.setVisible(false);
			
		//Create PAnel for Department wise consumption & hide it.
			pnlUSGWS=new JPanel(null);
			add(new JLabel("From Date"),1,1,1,1,pnlUSGWS,'L');
			add(txtFMDAT3=new TxtDate(),1,2,1,1,pnlUSGWS,'L');
			add(new JLabel("To Date"),2,1,1,1,pnlUSGWS,'L');
			add(txtTODAT3=new TxtDate(),2,2,1,1,pnlUSGWS,'L');
			add(pnlUSGWS,3,2,6,5.1,this,'L');
			pnlUSGWS.setVisible(false);
	
			
			
		//Retrive Department Name and Code and put in hashtable
			hstDPTNM=new Hashtable<String,String>(10,0.8f);
			hstHLPDT=new Hashtable<String,String[]>(10,0.8f);
			vtrUSGCD=new Vector<String>(5,2);vtrUSGDS=new Vector<String>(5,2);
			//M_strSQLQRY = "Select CMT_CGSTP, CMT_CODCD,CMT_CODDS, CMT_SHRDS from CO_CDTRN where CMT_CGMTP='SYS' and (CMT_CGSTP='COXXDPT' or CMT_CGSTP='MMXXUSG'or CMT_CGSTP='MMXXISS') order by CMT_CODCD";
			M_strSQLQRY = "Select CMT_CGSTP, CMT_CODCD,CMT_CODDS, CMT_SHRDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP in('COXXDPT','MMXXUSG','MMXXISS') order by CMT_CODCD";
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("CMT_CGSTP").equals("COXXDPT"))
				{
					hstDPTNM.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					hstHLPDT.put(M_rstRSSET.getString("CMT_CODCD"),new String[]{ M_rstRSSET.getString("CMT_CODCD"), M_rstRSSET.getString("CMT_CODDS")});
				}
				else if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXISS"))
					cmbISSTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
				else
				{
					vtrUSGCD.addElement(M_rstRSSET.getString("CMT_CODCD"));
					vtrUSGDS.addElement(M_rstRSSET.getString("CMT_CODDS"));
				}
			}
			M_rstRSSET.close();
			
			INPVF oINPVF =new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(oINPVF);
				}
				else
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Supper Class method overrided to enable & disable the Components.
	 * @param P_flgSTATE boolean to pass the state of the component.
	 */
	void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(P_flgSTATE);
		txtFMDNM1.setEnabled(false);
		txtTODNM1.setEnabled(false);
		txtFMDNM2.setEnabled(false);
		txtTODNM2.setEnabled(false);
		txtFMTNM2.setEnabled(false);
		txtTOTNM2.setEnabled(false);
		txtPRTNM.setEnabled(false);
	}
	public void actionPerformed(ActionEvent P_AE)
	{
		super.actionPerformed(P_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if( cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					cmbRPTTP.requestFocus();
					setENBL(true);
				}
				else
					setENBL(false);
			}			
			else if(M_objSOURC == cmbRPTTP)
			{
				int L_intRPTTP=cmbRPTTP.getSelectedIndex();
				switch (L_intRPTTP)
				{
					case 0://Select Report Type
						pnlDPTWS.setVisible(false);
						pnlTAGWS.setVisible(false);
						pnlUSGWS.setVisible(false);
						break;
					case 1://Department wise
						pnlDPTWS.setVisible(true);
						pnlTAGWS.setVisible(false);
						pnlUSGWS.setVisible(false);
						strDOTLN = "---------------------------------------------------------------------------------------------------------";
						txtFMDPT1.requestFocus();
						break;
					case 2://Tag wise
						pnlDPTWS.setVisible(false);
						pnlTAGWS.setVisible(true);
						pnlUSGWS.setVisible(false);
						strDOTLN = "---------------------------------------------------------------------------------------------------------";
						txtFMTAG2.requestFocus();
						break;
					case 3://Usage wise
						pnlDPTWS.setVisible(false);
						pnlTAGWS.setVisible(false);
						pnlUSGWS.setVisible(true);
						txtFMDAT3.requestFocus();
						strDOTLN = "-------------------------------------------------------------------------------------------------------------------------------------------------------------";
						break;
				}				
			}
			else if(M_objSOURC == txtTODAT1)
			{
				if(txtTODAT1.getText().trim().length()>0)
					txtPRTCD.requestFocus();
			}
			else if(M_objSOURC == txtPRTCD)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if(M_objSOURC == txtTODAT2)
			{
				if(txtTODAT2.getText().trim().length()>0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if(M_objSOURC == txtTODAT3)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	public void focusGained(FocusEvent P_FE)
	{
		super.focusGained(P_FE);
		if(!M_flgERROR)
		{
			if(M_objSOURC == txtFMDPT1 || M_objSOURC == txtFMDPT2)
				setMSG("Please enter From Department, 'F1' for help ..",'N');
			else if(M_objSOURC == txtTODPT1 || M_objSOURC == txtTODPT2)
				setMSG("Please enter To Department, 'F1' for help ..",'N');
			else if(M_objSOURC == txtFMTAG2)
				setMSG("Please enter From Tag No., 'F1' for help ..",'N');
			else if(M_objSOURC == txtTOTAG2)
				setMSG("Please enter To Tag No., 'F1' for help ..",'N');
			else if(M_objSOURC == txtFMDAT1 || M_objSOURC == txtFMDAT2 || M_objSOURC == txtFMDAT3)
				setMSG("Please enter From Date ..",'N');
			else if(M_objSOURC == txtTODAT1 || M_objSOURC == txtTODAT2 || M_objSOURC == txtTODAT3)
				setMSG("Please enter To Date ..",'N');
			else if(M_objSOURC == cmbRPTTP)
				setMSG("Select Report Type ..",'N');
		}
	}
	public void keyPressed(KeyEvent P_KE)
	{
		super.keyPressed(P_KE);
		try
		{
			if(P_KE.VK_F1==P_KE.getKeyCode())
			{
				if(M_objSOURC==txtFMDPT1 ||M_objSOURC==txtTODPT1 ||M_objSOURC==txtFMDPT2 ||M_objSOURC==txtTODPT2 )
				{
					if(M_objSOURC == txtFMDPT1)
						M_strHLPFLD="txtFMDPT1";
					else if(M_objSOURC == txtFMDPT2)
						M_strHLPFLD="txtFMDPT2";
					else if(M_objSOURC == txtTODPT1)
						M_strHLPFLD="txtTODPT1";
					else if(M_objSOURC == txtTODPT2)
						M_strHLPFLD="txtTODPT2";
					cl_hlp(hstHLPDT,2,1,new String[] {"Code","Department"},1 ,"CT");
				}
				else if(M_objSOURC==txtFMTAG2 ||M_objSOURC==txtTOTAG2)
				{
					M_strSQLQRY="Select EQ_TAGNO, EQ_TAGDS from MM_EQMST";
					if(M_objSOURC == txtFMTAG2)
					{
						txtFMTAG2.setText(txtFMTAG2.getText().toUpperCase());
						M_strHLPFLD="txtFMTAG2";
						if(txtFMTAG2.getText().length()>0)
							M_strSQLQRY+=" where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EQ_TAGNO like '"+txtFMTAG2.getText()+"%' ";
						M_strSQLQRY+=" order by EQ_TAGNO";
                         cl_hlp(M_strSQLQRY,1,1,new String[] {"Tag No.","Department"},2 ,"CT");
                	
							
					}
					else if(M_objSOURC == txtTOTAG2)
					{
						txtTOTAG2.setText(txtTOTAG2.getText().toUpperCase());
					    M_strHLPFLD="txtTOTAG2";
						if(txtTOTAG2.getText().length()>0)
							M_strSQLQRY+=" where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EQ_TAGNO like '"+txtTOTAG2.getText()+"%' ";
				    	M_strSQLQRY+=" order by EQ_TAGNO";
                        cl_hlp(M_strSQLQRY,1,1,new String[] {"Tag No.","Department"},2 ,"CT");
                
					}
				}
                if(M_objSOURC == txtPRTCD)
				{
					txtPRTCD.setText(txtPRTCD.getText().toUpperCase());
				    M_strHLPFLD="txtPRTCD";
					M_strSQLQRY = " SELECT DISTINCT IS_PRTCD,PT_PRTNM FROM MM_ISMST,CO_PTMST WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IS_PRTCD,'') <>'' AND IS_PRTTP = PT_PRTTP AND"
                                + " IS_PRTCD = PT_PRTCD AND IS_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
                    if(txtPRTCD.getText().length()>0)
						M_strSQLQRY+=" where PT_PRTCD like '"+txtPRTCD.getText()+"%' ";					
                    cl_hlp(M_strSQLQRY,1,1,new String[] {"Party Code","Description"},2 ,"CT");
				}
				
			}
			else if(P_KE.getKeyCode() == P_KE.VK_ENTER)
				((JComponent)M_objSOURC).transferFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Chlid.keyPressed");
		}
	}
	/**
	 * Super class method overrided to execuate the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
		if(M_strHLPFLD == "txtFMDPT1")
		{
			txtFMDPT1.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMDNM1.setText((String)hstDPTNM.get(cl_dat.M_strHLPSTR_pbst));
		}
		else if(M_strHLPFLD == "txtFMDPT2")
		{
			txtFMDPT2.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMDNM2.setText((String)hstDPTNM.get(cl_dat.M_strHLPSTR_pbst));
		}
		else if(M_strHLPFLD == "txtTODPT1")
		{
			txtTODPT1.setText(cl_dat.M_strHLPSTR_pbst);
			txtTODNM1.setText((String)hstDPTNM.get(cl_dat.M_strHLPSTR_pbst));
		}
		else if(M_strHLPFLD == "txtTODPT2")
		{
			txtTODPT2.setText(cl_dat.M_strHLPSTR_pbst);
			txtTODNM2.setText((String)hstDPTNM.get(cl_dat.M_strHLPSTR_pbst));
		}
		else if(M_strHLPFLD == "txtFMTAG2")
		{
			StringTokenizer L_stkTEMP=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtFMTAG2.setText(L_stkTEMP.nextToken());
		}
		else if(M_strHLPFLD == "txtTOTAG2")
		{
			StringTokenizer L_stkTEMP=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtTOTAG2.setText(L_stkTEMP.nextToken());
		}
		else if(M_strHLPFLD == "txtPRTCD")
		{
		    txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			StringTokenizer L_stkTEMP=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_stkTEMP.nextToken();
			txtPRTNM.setText(L_stkTEMP.nextToken());
		}
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpisv.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpisv.doc";
			
			getDATA();
			
			if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Issue Valuation Reports"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	void getDATA()
	{
		try
		{
			intRECCT = 0; 
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Issue Valuation Reports</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			prnHEADER();
			
			Vector<String> L_vtrDPTCD=new Vector<String>(10,2);//Vector for department code ; in USAGE wise report
			String [][] L_staRPDAT=new String[hstDPTNM.size()][vtrUSGCD.size()];//Vector for report data in USAGE wise report
			boolean L_flgNODAT=true;//Flga to indicate, no data found
			if(cmbRPTTP.getSelectedIndex() == intDPTWS_fn) //Query for DEPARTMENT WISE report
			{				
				M_strSQLQRY="Select IS_DPTCD, IS_MATCD, CT_MATDS, CT_UOMCD, sum(IS_ISSQT) IS_ISSQT, sum(IS_ISSVL) IS_ISSVL from "
					+" MM_ISMST, CO_CTMST where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MMSBS = '"+M_strSBSCD+"' and ";
				if(txtFMDPT1.getText().length()>0)
					M_strSQLQRY+=" IS_DPTCD between '"+txtFMDPT1.getText()+"' and '"+txtTODPT1.getText()+"' and ";
				M_strSQLQRY+= " IS_ISSTP ='"+cmbISSTP.getSelectedItem().toString().substring(0,2)+"' AND ";
				if(txtPRTCD.getText().length()>0)
				M_strSQLQRY+= " IS_PRTCD ='"+txtPRTCD.getText().trim()+"' AND";	
				M_strSQLQRY+=" IS_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT1.getText()))+"' and "
					+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT1.getText())) +"' and CT_MATCD=IS_MATCD group by IS_MATCD, CT_MATDS,CT_UOMCD,IS_DPTCD "
					+" order by IS_DPTCD,IS_MATCD";
			System.out.println(M_strSQLQRY);
			}
			else if (cmbRPTTP.getSelectedIndex() == intTAGWS_fn) //Query for TAG WISE report
			{				
				M_strSQLQRY="Select IS_TAGNO, EQ_TAGDS, IS_DPTCD, IS_MATCD, CT_MATDS, CT_UOMCD, sum(IS_ISSQT) IS_ISSQT, sum(IS_ISSVL) IS_ISSVL from "
					+" MM_ISMST, CO_CTMST, MM_EQMST where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MMSBS = '"+M_strSBSCD+"' and ";
				if(txtFMTAG2.getText().length()>0)
					M_strSQLQRY+=" IS_TAGNO between '"+txtFMTAG2.getText()+"' and '"+txtTOTAG2.getText()+"' and ";
				if(txtFMDPT2.getText().length()>0)
					M_strSQLQRY+= " IS_DPTCD between '"+txtFMDPT2.getText()+"' and '"+txtTODPT2.getText()+"' and ";
				M_strSQLQRY+=" IS_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT2.getText()))+"' and "
					+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT2.getText())) +"' and CT_MATCD=IS_MATCD and EQ_TAGNO=IS_TAGNO group by IS_TAGNO, IS_MATCD, CT_MATDS,CT_UOMCD,IS_DPTCD, EQ_TAGDS "
					+" order by IS_TAGNO, IS_DPTCD,IS_MATCD";
			}
			else if (cmbRPTTP.getSelectedIndex() == intUSGWS_fn) //Query for USAGE TYPE WISE COMSUPTION. Omite SBSCD in this
			{				
				cl_dat.M_intLINNO_pbst++;
				M_strSQLQRY="Select IS_DPTCD, IS_USGTP, sum(IS_ISSQT) IS_ISSQT, sum(IS_ISSVL) IS_ISSVL from "
					+" MM_ISMST where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT3.getText()))+"' and "
					+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT3.getText())) +"' group by IS_DPTCD, IS_USGTP "
					+" order by  IS_DPTCD,IS_USGTP";
				String [] L_staDPTCD=new String[hstDPTNM.size()];//String array to sort department Codes
				Enumeration L_enmTEMP=hstDPTNM.keys();
				int j=0;//Counter for enumerration
				while(L_enmTEMP.hasMoreElements())
					L_staDPTCD[j++]=(String)L_enmTEMP.nextElement();
				Arrays.sort(L_staDPTCD);//Department codes sorted
				for(int i=0;i<L_staDPTCD.length;i++)//Array copied to Vector
					L_vtrDPTCD.addElement(L_staDPTCD[i]);
			}			
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			System.out.println(M_strSQLQRY);
			String L_strDPTCD="",L_strTAGNO="";//For values in previous record
			float L_fltTOTAL=0.0f;//For subtotal
			String L_strMNGRP="";// For Previous Main Group
			String L_strPMNGRP="";
			float L_fltGRPTOT=0.0f;
			boolean flgFIRST=true;
			if(M_rstRSSET != null)
			{														
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst>50)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						cl_dat.M_intLINNO_pbst = 0;
						prnHEADER();					
					}					
					if(cmbRPTTP.getSelectedIndex() == intDPTWS_fn)//DEPARTMENT WISE CONSUMPTION
					{
						if(!L_strDPTCD.equals(nvlSTRVL(M_rstRSSET.getString("IS_DPTCD"),"")))
						{//If department code is changed, display it on separate line after total of previous department
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							if(!L_flgNODAT)
							{
								dosREPORT.writeBytes(strDOTLN+"\n");
								dosREPORT.writeBytes(padSTRING('L',"Total "+hstDPTNM.get(L_strDPTCD)+" : ",12+55+5+15));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTOTAL,2),15)+"\n");
								cl_dat.M_intLINNO_pbst+=2;
							}
							L_fltTOTAL=0;//Initialise subtotal
							dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst++;
							//prnFMTCHR(dosREPORT,M_strCPI10);
							dosREPORT.writeBytes("Department : "+hstDPTNM.get(nvlSTRVL(M_rstRSSET.getString("IS_DPTCD"),"")));
							dosREPORT.writeBytes("\n\n");
							cl_dat.M_intLINNO_pbst += 2;
							//prnFMTCHR(dosREPORT,M_strCPI12);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
						}
						//System.out.print((nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")).substring(0,2));
						//L_fltGRPTOT+= M_rstRSSET.getFloat("IS_ISSVL");
						if(!L_strMNGRP.equals(nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"").substring(0,2)) && !flgFIRST) //nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")).substring(0,2))
						{
							dosREPORT.writeBytes(strDOTLN+"\n");
							dosREPORT.writeBytes(padSTRING('R',"Group Total ",87));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltGRPTOT,2),15)+"\n");
							dosREPORT.writeBytes(strDOTLN+"\n");
							L_fltGRPTOT= M_rstRSSET.getFloat("IS_ISSVL");
							flgFIRST= true;
						}	
						else
						{	
							L_fltGRPTOT+= M_rstRSSET.getFloat("IS_ISSVL");
							flgFIRST = false;
						}
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),55));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),5));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),""),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IS_ISSVL"),""),15)+"\n");
						cl_dat.M_intLINNO_pbst++;
						L_strDPTCD=M_rstRSSET.getString("IS_DPTCD");//Remember current Department code
						L_fltTOTAL+=M_rstRSSET.getFloat("IS_ISSVL");//Calculate subtotal
						L_strMNGRP=nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"").substring(0,2);
					}
					else if(cmbRPTTP.getSelectedIndex() == intTAGWS_fn) //TAG WISE COINSUMPTIUON
					{
						if(L_flgNODAT && !L_strTAGNO.equals(nvlSTRVL(M_rstRSSET.getString("IS_TAGNO"),"")))
						{//If tag no. is changed, display total and new tag no. on separate lines
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;						
							if(!L_strTAGNO.equals(""))
								dosREPORT.writeBytes(padSTRING('L',"Total "+L_strTAGNO+" : "+setNumberFormat(L_fltTOTAL,2),100));
							L_fltTOTAL=0;
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							//prnFMTCHR(dosREPORT,M_strCPI10);
							dosREPORT.writeBytes("Tag No. : "+nvlSTRVL(M_rstRSSET.getString("IS_TAGNO"),"")+" : "+nvlSTRVL(M_rstRSSET.getString("EQ_TAGDS"),""));
							dosREPORT.writeBytes("\n\n");
							cl_dat.M_intLINNO_pbst+=2;
							//prnFMTCHR(dosREPORT,M_strCPI12);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
						}
						if((!L_strDPTCD.equals(nvlSTRVL(M_rstRSSET.getString("IS_DPTCD"),""))) || (!L_strTAGNO.equals(nvlSTRVL(M_rstRSSET.getString("IS_TAGNO"),""))))
						{//If tag no or department is changed, display department on separate line
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes("     Department : "+hstDPTNM.get(nvlSTRVL(M_rstRSSET.getString("IS_DPTCD"),"")));
							dosREPORT.writeBytes("\n\n");
							cl_dat.M_intLINNO_pbst += 2;		
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
						}
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),55));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),5));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),""),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IS_ISSVL"),""),15));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst ++;
						L_strDPTCD=M_rstRSSET.getString("IS_DPTCD");//Remember current department code
						L_strTAGNO=M_rstRSSET.getString("IS_TAGNO");//Remember current tag no.
						L_fltTOTAL+=M_rstRSSET.getFloat("IS_ISSVL");//Calculate subtotal
					}
					else if(cmbRPTTP.getSelectedIndex() == intUSGWS_fn)
					{//USAGE TYPE WISE COMSUPTION : Put data in string array
						if(L_vtrDPTCD.contains(M_rstRSSET.getString("IS_DPTCD"))&&vtrUSGCD.contains(M_rstRSSET.getString("IS_USGTP")))
						L_staRPDAT[L_vtrDPTCD.indexOf(M_rstRSSET.getString("IS_DPTCD"))][vtrUSGCD.indexOf(M_rstRSSET.getString("IS_USGTP"))]=M_rstRSSET.getString("IS_ISSVL");
					}
					L_flgNODAT = false;//Data found.
				}
			}
			if(cmbRPTTP.getSelectedIndex() == intDPTWS_fn)//Display total of last Department in DEPARTMENT wise report
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('L',"Total "+hstDPTNM.get(L_strDPTCD)+" : ",12+55+5+15));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTOTAL,2),15));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst ++;
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
			}
			else if(cmbRPTTP.getSelectedIndex() == intTAGWS_fn)//Display total of last TAG in TAG wise report
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst ++;
				if(!L_strTAGNO.equals(""))
					dosREPORT.writeBytes(padSTRING('L',"Total "+L_strTAGNO+" : "+setNumberFormat(L_fltTOTAL,2),100));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst ++;
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
			}
			else if(cmbRPTTP.getSelectedIndex() == intUSGWS_fn)//USAGE TYPE WISE COMSUPTION Write report data from string array to file
			{
				boolean L_flgPRINT=false;
				for(int i=0;i<L_staRPDAT.length;i++)
				{
					L_flgPRINT=false;
					for(int j=0;j<L_staRPDAT[i].length;j++)
						if(L_staRPDAT[i][j]!=null)
							L_flgPRINT=true;
					if(L_flgPRINT)
					{
						dosREPORT.writeBytes(padSTRING('R',L_vtrDPTCD.elementAt(i).toString(),5));
						dosREPORT.writeBytes(padSTRING('R',hstDPTNM.get(L_vtrDPTCD.elementAt(i)).toString(),30));
						for(int j=0;j<L_staRPDAT[i].length;j++)
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_staRPDAT[i][j],""),20));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst ++;
					}
				}				
				dosREPORT.writeBytes(strDOTLN+"\n");
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes(padSTRING('R',"Usage Wise Total ",5+30));
				for(int i=0;i<L_staRPDAT[0].length;i++)
				{//Display Usage type wise totals
					L_fltTOTAL=0.0f;
					for(int j=0;j<L_staRPDAT.length;j++)
						L_fltTOTAL+=Float.parseFloat(nvlSTRVL(L_staRPDAT[j][i],"0.0"));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTOTAL,2),20));
				}
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst ++;
			}
			dosREPORT.writeBytes(strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst++;
			
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}

	/**
	 * Method to validate the data before execuation of the SQL Query.
	 */
	boolean vldDATA() 
	{
		try
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;
			java.util.Date datTODAY=M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText());
			if(cmbRPTTP.getSelectedIndex() ==0)
			{
				setMSG("Please Select Report Type ..",'E');
				cmbRPTTP.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) && M_cmbDESTN.getSelectedIndex()==0)
			{
				setMSG("Please Select Printer ..",'E');
				M_cmbDESTN.requestFocus();
				return false;
			}
			if(cmbRPTTP.getSelectedIndex() == intDPTWS_fn)
			{
				if(txtFMDAT1.getText().length() ==0)
				{
					setMSG("Please Enter from date ..",'E');
					txtFMDAT1.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtFMDAT1.getText()).compareTo(datTODAY)>0)
				{
					setMSG("From date cannot be grater thatn today ..",'E');
					txtFMDAT1.requestFocus();
					return false;
				}
					
				if(txtTODAT1.getText().length() ==0)
				{
					setMSG("Please Enter to date ..",'E');
					txtTODAT1.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtTODAT1.getText()).compareTo(datTODAY)>0)
				{
					setMSG("To date cannot be grater thatn today ..",'E');
					txtTODAT1.requestFocus();
					return false;
				}
					
				if(txtFMDPT1.getText().length()>0 && txtTODPT1.getText().length()==0)
				{
					setMSG("Please Enter to Department ..",'E');
					txtTODPT1.requestFocus();
					return false;
				}
			}
			else if(cmbRPTTP.getSelectedIndex() == intTAGWS_fn)
			{
				if(txtFMDAT2.getText().length() ==0)
				{
					setMSG("Please Enter from date ..",'E');
					txtFMDAT2.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtFMDAT2.getText()).compareTo(datTODAY)>0)
				{
					setMSG("From date cannot be grater than today ..",'E');
					txtFMDAT2.requestFocus();
					return false;
				}
					
				if(txtTODAT2.getText().length() ==0)
				{
					setMSG("Please Enter to date ..",'E');
					txtTODAT2.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtTODAT2.getText()).compareTo(datTODAY)>0)
				{
					setMSG("From date cannot be grater than today ..",'E');
					txtTODAT2.requestFocus();
					return false;
				}
					
				if(txtFMDPT2.getText().length()>0 && txtTODPT2.getText().length()==0)
				{
					setMSG("Please Enter to Department ..",'E');
					txtTODPT2.requestFocus();
					return false;
				}
				if(txtFMTAG2.getText().length()>0 && txtTOTAG2.getText().length()==0)
				{
					setMSG("Please Enter to Department ..",'E');
					txtTOTAG2.requestFocus();
					return false;
				}
			}
			else if(cmbRPTTP.getSelectedIndex() == intUSGWS_fn)
			{
				if(txtFMDAT3.getText().length() ==0)
				{
					setMSG("Please Enter from date ..",'E');
					txtFMDAT3.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtFMDAT3.getText()).compareTo(datTODAY)>0)
				{
					setMSG("From date cannot be grater than today ..",'E');
					txtFMDAT3.requestFocus();
					return false;
				}
					
				if(txtTODAT3.getText().length() ==0)
				{
					setMSG("Please Enter to date ..",'E');
					txtTODAT3.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtTODAT3.getText()).compareTo(datTODAY)>0)
				{
					setMSG("From date cannot be grater than today ..",'E');
					txtTODAT3.requestFocus();
					return false;
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
			if(txtPRTCD.getText().length() ==0)
			    txtPRTNM.setText("");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}			
	/**
	 * Method to generate the header part of the Report.
	 */
	void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			int L_intRPTTP = cmbRPTTP.getSelectedIndex();			
			dosREPORT.writeBytes("\n"+padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-35));
			dosREPORT.writeBytes("Date   : "+cl_dat.M_strLOGDT_pbst +"\n");
			cl_dat.M_intLINNO_pbst++;
		
			dosREPORT.writeBytes(padSTRING('R',staRPTTP_fn[L_intRPTTP-1],strDOTLN.length()-35));
			dosREPORT.writeBytes("Page   : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");
			cl_dat.M_intLINNO_pbst++;
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),strDOTLN.length()-35));
			if(L_intRPTTP ==1)
				dosREPORT.writeBytes("Period : "+txtFMDAT1.getText()+" to "+txtTODAT1.getText());
			else if(L_intRPTTP ==2)
				dosREPORT.writeBytes("Period : "+txtFMDAT2.getText()+" to "+txtTODAT2.getText());
			else if(L_intRPTTP ==3)
				dosREPORT.writeBytes("Period : "+txtFMDAT3.getText()+" to "+txtTODAT3.getText());
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;		
		
			switch(L_intRPTTP)
			{
				case 1 :
				case 2 :
					dosREPORT.writeBytes(padSTRING('R',"Issue Type  : "+cmbISSTP.getSelectedItem().toString().substring(2),strDOTLN.length()-35));
					dosREPORT.writeBytes("Contractor : "+txtPRTCD.getText()+" "+txtPRTNM.getText()+"\n");
					cl_dat.M_intLINNO_pbst++;				
					dosREPORT.writeBytes(strDOTLN+"\n");
					cl_dat.M_intLINNO_pbst++;
					dosREPORT.writeBytes(padSTRING('R',"Material Code & Material Description",12+55));
					dosREPORT.writeBytes(padSTRING('R',"UOM",5));
					dosREPORT.writeBytes(padSTRING('L',"Issued Qty.",15));
					dosREPORT.writeBytes(padSTRING('L',"Value",15)+"\n");
					cl_dat.M_intLINNO_pbst++;
					dosREPORT.writeBytes(strDOTLN+"\n");
					cl_dat.M_intLINNO_pbst++;
					break;
				case 3 :
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strCPI17);
					dosREPORT.writeBytes(strDOTLN+"\n");
					cl_dat.M_intLINNO_pbst++;
					dosREPORT.writeBytes(padSTRING('R',"Dept. Code and Description",5+30));
					for(int i=0;i<vtrUSGDS.size();i++)
						dosREPORT.writeBytes(padSTRING('L',vtrUSGDS.elementAt(i).toString(),20));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst++;
					dosREPORT.writeBytes(strDOTLN+"\n");
					cl_dat.M_intLINNO_pbst++;
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}		
	private class INPVF extends InputVerifier
	{		
		public boolean verify(JComponent input)
		{
			try
			{
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length()==0)
						return true;
			
				if(input == txtFMDPT1 ||input == txtTODPT1 ||input == txtFMDPT2 ||input == txtTODPT2 )
				{
					if(!hstDPTNM.containsKey(((JTextField)input).getText()))
					{
						setMSG("Invalid Department ..",'E');
						return false;
					}
					else
					{
						if(input == txtFMDPT1)
							txtFMDNM1.setText((String)hstDPTNM.get(txtFMDPT1.getText()));
						else if(input == txtFMDPT2)
							txtFMDNM2.setText((String)hstDPTNM.get(txtFMDPT2.getText()));
						else if(input == txtTODPT1)
							txtTODNM1.setText((String)hstDPTNM.get(txtTODPT1.getText()));
						else if(input == txtTODPT2)
							txtTODNM2.setText((String)hstDPTNM.get(txtTODPT2.getText()));
					}
				}
				else if(input == txtFMTAG2 || input == txtTOTAG2)
				{
					txtFMTAG2.setText(txtFMTAG2.getText().toUpperCase());
					txtTOTAG2.setText(txtTOTAG2.getText().toUpperCase());
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY0("Select * from mm_eqmst where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND eq_tagno='"
							+ (input == txtFMTAG2 ? txtFMTAG2.getText() : txtTOTAG2.getText()) +"'");
					if(L_rstRSSET == null)
					{
						setMSG("Invalid Tag No. ..",'E');
						return false;
					}
					if(!L_rstRSSET.next())
					{
						setMSG("Invalid Tag No. ..",'E');
						return false;
					}
					if(input == txtFMTAG2)
					{
						txtFMTAG2.setText(L_rstRSSET.getString("EQ_TAGNO"));
						txtFMTNM2.setText(L_rstRSSET.getString("EQ_TAGDS"));
					}
					else if(input == txtTOTAG2)
					{
						txtTOTAG2.setText(L_rstRSSET.getString("EQ_TAGNO"));
						txtTOTNM2.setText(L_rstRSSET.getString("EQ_TAGDS"));
					}
				}
				else if(input == txtPRTCD)
				{
				    M_strSQLQRY = "SELECT PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP ='S' AND "
				                + "PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
				    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				    if(M_rstRSSET !=null)
					{
				        if(M_rstRSSET.next())			       
				            txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));				        
				        else
				        {
				            setMSG("Invalid Party Name ..",'E');
				            return false;
				        }
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Input Verifier");				
				return false;
			}
			return true;
		}
	}
}