/*
System Name   : Marketing Management System
Program Name  : Price Difference Report
Program Desc. : Price Difference Report

Author        : Mr S.R.Tawde
Date          : 06.09.2005
Version       : Marksys v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;import javax.swing.JPanel;
import java.awt.Font;import java.awt.Color;import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;import javax.swing.InputVerifier;import javax.swing.JComponent;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
/**<pre>
<b>System Name :</b> Marketing Management System.
 
<b>Program Name :</b> Price Difference Report

<b>Purpose :</b> This module gives Price Difference Report between given dates.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
MR_IVTRN	IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP					#
CO_PTMST	PT_PRTTP,PT_PRTCD										#
CO_CDTRN	CMT_CGMTP,CMT_CGSTP,CMT_CODCD							#
CO_PRMST	PR_PRDCD												#
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table Name      Type/Size     Description
--------------------------------------------------------------------------------
txtFRMDT    date(IVT_INVDT) MR_IVTRN       Date          From Date
txtTORDT    date(IVT_INVDT) MR_IVTRN       Date          From Date
txtPRDCD    PR_PRDCD        CO_PRMST       Varchar       Product Code
txtBYRCD    IVT_BYRCD       MR_IVTRN       Varchar       Buyer Code
rdbPRDSP    ------------------ Select for specific product----------------------
rdbPRDAL    ---------------- Select for all products ---------------------------
--------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description			      Display Columns			Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtPRDCD    Product Code,                     PR_PRDCD,PR_PRDDS         CO_PRMST
            Description 
txtBYRCD    Buyer Code,Name			          PT_PRTCD,PT_PRTNM		    CO_PTMST
-----------------------------------------------------------------------------------------------------------------------------------------------------
<I>
<B>Conditions Give in Query:</b>
             Data is taken from MR_IVTRN,CO_PTMST,CO_CDTRN between the given date range 
             and depending upon the selection of Product Code.

             1)date(IVT_INVDT) between From Date and To Date
			 1)and IVT_BYRCD = Accepted Buyer Code
             2)and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MRXXDTP' and CMT_CODCD=IVT_DTPCD
 			 order by IVT_PRDDS,IVT_INVNO	
</I>
Validations :
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
    3) Product Code validation from CO_PRMST ('PR_PRDCD')
    4) Buyer Code validation from CO_PTMST ('PT_PRTTP','PT_PRTCD')
 */
public class mr_rppdr extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;		/** JTextField to accept Product Code.*/
	private JTextField txtPRDCD;		/** JTextField to display Grade. */
	private JTextField txtPRDDS;		/** JTextField to accept Buyer Code*/
	private JTextField txtBYRCD;		/** JTextField to Display Buyer Name*/                          			
	private JTextField txtBYRNM;
	
	private JRadioButton rdbPRDSP,rdbPRDAL;
	private ButtonGroup bgrPRDCD;

	private String strHLPFLD,strFMDAT,strTODAT;									
		
											/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;	

	private INPVF objINPVF = new INPVF();
	
	/**
	 *1.Screen Designing
	 *2.Hashtable is created using CO_CDTRN for maintaining various types of codes alongwith
	 *  their descriptions.
	 */
	mr_rppdr()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			bgrPRDCD=new ButtonGroup();			
			add(new JLabel("From Date"),5,2,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,3,1,1,this,'L');			
			add(new JLabel("To Date"),5,4,1,1.5,this,'L');
			add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');
			
			JPanel pnlPRDCD = new JPanel();
			setMatrix(20,8);
			pnlPRDCD.setLayout(null);
			bgrPRDCD=new ButtonGroup();
			add(rdbPRDAL = new JRadioButton("All",true),1,2,1,1,pnlPRDCD,'L');
			add(rdbPRDSP = new JRadioButton("Specific",false),1,3,1,1,pnlPRDCD,'L');
			add(txtPRDCD = new TxtLimit(10),1,4,1,1,pnlPRDCD,'L');
			add(txtPRDDS = new TxtLimit(15),1,5,1,1,pnlPRDCD,'L');
			bgrPRDCD.add(rdbPRDAL);
			bgrPRDCD.add(rdbPRDSP);
			pnlPRDCD.setBorder(BorderFactory.createTitledBorder(null,"Product",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlPRDCD,6,2,2,6,this,'L');
			
			JPanel pnlBYRCD = new JPanel();
			setMatrix(20,8);
			pnlBYRCD.setLayout(null);
			add(txtBYRCD = new TxtLimit(5),1,2,1,1,pnlBYRCD,'L');
			add(txtBYRNM = new TxtLimit(45),1,3,1,3.5,pnlBYRCD,'L');
			pnlBYRCD.setBorder(BorderFactory.createTitledBorder(null,"Buyer",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlBYRCD,8,2,2,6,this,'L');
			
			/** Registering the components with inputverifier */
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtBYRCD.setInputVerifier(objINPVF);
			txtPRDCD.setInputVerifier(objINPVF);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);

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
		txtPRDCD.setEnabled(false);
		txtPRDDS.setEnabled(false);
		txtBYRNM.setEnabled(false);

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
			if(rdbPRDSP.isSelected())
			{
				txtPRDCD.setEnabled(L_flgSTAT);
			} 
		}
	}		

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
		if(M_objSOURC == rdbPRDSP)
		{	
			txtPRDCD.setEnabled(true);
			txtPRDCD.requestFocus();
			txtPRDDS.setText("");
		}	
		if(M_objSOURC == rdbPRDAL)
		{	
			txtPRDCD.setEnabled(false);
			txtPRDCD.setText("");
			txtPRDDS.setText("ALL");
		}	
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
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtBYRCD)
		{	
			try
			{
				strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
				strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Error in Date Conversion");
			}	
		}	
	}	
		
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)
			{	
				try
				{
					strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					txtTODAT.requestFocus();
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"FROM DATE CONVERSION");
				}	
			}	
			else if(M_objSOURC == txtTODAT)			
			{	
				try
				{	
					strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
					rdbPRDAL.requestFocus();
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"TO DATE CONVERSION");
				}	
			}	
			else if(M_objSOURC == txtPRDCD)
			{	
				M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where ";
				M_strSQLQRY +=" PR_PRDCD ='"+txtPRDCD.getText().trim()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				System.out.println(M_strSQLQRY);
				if(M_rstRSSET != null)
				try
				{
					if(!M_rstRSSET.next())
					{	
						setMSG("Invalid Product Code....Press F1 for Help..",'E');
						return;
					}
					else
					System.out.println("in else");
					txtPRDDS.setText(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
					System.out.println("settext");
					txtBYRCD.requestFocus();
					System.out.println("Requestfocus");
					//M_rstRSSET.close();
				}	
				catch(Exception L_EX)
				{
					setMSG(L_EX," Fetching Product Details");
				}
			}	
			else if(M_objSOURC == txtBYRCD)
			{	
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST where ";
				M_strSQLQRY +=" PT_PRTTP = 'C' AND PT_PRTCD ='"+txtBYRCD.getText().trim()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				System.out.println(M_strSQLQRY);
				if(M_rstRSSET != null)
				try
				{
					if(!M_rstRSSET.next())
					{	
						setMSG("Invalid Buyer Code....Press F1 for Help..",'E');
						return;
					}
					else
					txtBYRNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
				}	
				catch(Exception L_EX)
				{
					setMSG(L_EX," Fetching Buyer Details");
				}
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}	
		}
		
		/**Method to display list of Product Codes and Buyer Codes in help window 
		when F1 is Pressed.*/
		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtPRDCD)
				{
					M_strHLPFLD = "txtPRDCD";
					M_strSQLQRY =  " Select PR_PRDCD,PR_PRDDS from CO_PRMST ";
					if(txtPRDCD.getText().trim().length() > 0)
						M_strSQLQRY += " WHERE PR_PRDCD LIKE '"+txtPRDCD.getText().trim()+"%' ";
					M_strSQLQRY+= " ORDER BY PR_PRDDS ";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"PR");
				}
				if(M_objSOURC == txtBYRCD)
				{
					M_strHLPFLD = "txtBYRCD";
					if(rdbPRDSP.isSelected())
					{	
						M_strSQLQRY = " select distinct IVT_BYRCD,PT_PRTNM from MR_IVTRN,CO_PTMST where ";
						M_strSQLQRY+= " PT_PRTTP='C' and IVT_BYRCD=PT_PRTCD and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,IVT_INVDT,101) BETWEEN ";
						M_strSQLQRY+= " '"+strFMDAT+"' AND '"+strTODAT+"' and IVT_PRDDS= '"+txtPRDDS.getText()+"'";
						M_strSQLQRY+= " order by PT_PRTNM ";
					}
					if(rdbPRDAL.isSelected())
					{	
						M_strSQLQRY = " select distinct IVT_BYRCD,PT_PRTNM from MR_IVTRN,CO_PTMST where ";
						M_strSQLQRY+= " PT_PRTTP='C' and IVT_BYRCD=PT_PRTCD and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,IVT_INVDT,101) BETWEEN ";
						M_strSQLQRY+= " '"+strFMDAT+"' AND '"+strTODAT+"'";
						//M_strSQLQRY+= " order by PT_PRTNM ";
						if(txtBYRCD.getText().length()==0)
						{	
							setMSG("Type First letter and then press F1",'E'); 
							return;
						}	
						if(!(txtBYRCD.getText().length()==1))
						return;
					if(txtBYRCD.getText().trim().length() > 0)
						M_strSQLQRY+= " AND PT_PRTNM LIKE '"+txtBYRCD.getText().trim().toUpperCase()+"%'";
					M_strSQLQRY+= " ORDER BY PT_PRTNM ";
					}
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Buyer Code","Name"},2,"CT");
				}	
				setCursor(cl_dat.M_curDFSTS_pbst);
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
			if(M_strHLPFLD == "txtPRDCD")
			{
				txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
				if(M_rstRSSET != null)
				try				
				{
					M_rstRSSET.close();
				}
				catch(Exception L_E)
				{
					setMSG(L_E,"Closing Resultset");
				}	
			}
			if(M_strHLPFLD == "txtBYRCD")
			{
				txtBYRCD.setText(cl_dat.M_strHLPSTR_pbst);
				if(M_rstRSSET != null)
				try				
				{
					M_rstRSSET.close();
				}
				catch(Exception L_E)
				{
					setMSG(L_E,"Closing Resultset");
				}	
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rppdr.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rppdr.doc";				
			getDATA();				
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
	*Method to fetch Data from Database and start creation of the output file.
	*/
	private void getDATA()
	{ 	    		
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblINVQT,L_dblPRDQT = 0,L_dblTOTQT=0,L_dblINVRT,L_dblCC1VL,L_dblCC2VL,L_dblCC3VL,L_dblCC4VL,L_dblOCRVL;
		
		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPPRDDS = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCPRDDS = new String();

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strPRDDS = new String();
		String L_strINVNO = new String();
		String L_strINVDT = new String();
		String L_strDSPTP = new String();
		String L_strINDNO = new String();
			
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
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Gradewise Price Difference Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();			
			setMSG("Report Generation is in Progress.......",'N');		
			System.out.println("1");

			M_strSQLQRY = "SELECT IVT_INVNO,CONVERT(varchar,IVT_INVDT,101) IVT_INVDT,IVT_PRDDS,CMT_CODDS DSPTP,IVT_INVQT,IVT_INDNO,";
			M_strSQLQRY+= "IVT_INVRT,IVT_CC1VL/IVT_INVQT IVT_CC1VL,IVT_CC2VL/IVT_INVQT IVT_CC2VL,";
			M_strSQLQRY+= "IVT_CC3VL/IVT_INVQT IVT_CC3VL,PT_INVQT*PT_TRNRT IVT_OCRVL FROM ";
			M_strSQLQRY+= "CO_CDTRN,MR_IVTRN  LEFT OUTER JOIN MR_PTTRN ON IVT_INVNO=PT_INVNO AND ";
			M_strSQLQRY+= "PT_CRDTP IN ('0A','0B','0C','0Z')  AND IVT_PRDCD=PT_PRDCD WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,IVT_INVDT,101)";
			M_strSQLQRY+= "BETWEEN '"+strFMDAT+"' AND '"+strTODAT+"'  AND IVT_BYRCD='"+txtBYRCD.getText()+"' AND ";
			M_strSQLQRY+= "CMT_CGMTP='SYS' AND CMT_CGSTP='MRXXDTP' AND CMT_CODCD=IVT_DTPCD";

			
			// EARLIER QUERY (WITHOUT THE VALUE OF OTHER CREDIT NOTE FROM MR_PTTRN)
			
			//M_strSQLQRY = " SELECT IVT_INVNO,date(IVT_INVDT) IVT_INVDT,IVT_PRDDS,CMT_CODDS DSPTP,IVT_INVQT,IVT_INDNO,IVT_INVRT,IVT_CC1VL/IVT_INVQT IVT_CC1VL,";
			//M_strSQLQRY+= " IVT_CC2VL/IVT_INVQT IVT_CC2VL,IVT_CC3VL/IVT_INVQT IVT_CC3VL FROM SPLDATA.MR_IVTRN,SPLDATA.CO_CDTRN WHERE date(IVT_INVDT) BETWEEN ";
			//M_strSQLQRY+= " '"+strFMDAT+"' AND '"+strTODAT+"' AND IVT_BYRCD='"+txtBYRCD.getText()+"'";
			//M_strSQLQRY+= " AND CMT_CGMTP='SYS' AND CMT_CGSTP='MRXXDTP' AND CMT_CODCD=IVT_DTPCD";

			if(rdbPRDSP.isSelected())
				M_strSQLQRY+= " AND IVT_PRDDS= '"+txtPRDDS.getText()+"' ";	
			M_strSQLQRY+= " ORDER BY IVT_PRDDS,IVT_INVNO";
			System.out.println(M_strSQLQRY);			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
                    L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"");
					L_strINVNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
					L_strINVDT = nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),"");
					L_strDSPTP = nvlSTRVL(M_rstRSSET.getString("DSPTP"),"");
					L_strINDNO = nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),"");
	   				L_dblINVQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
	   				L_dblINVRT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVRT"),"0"));
	   				L_dblCC1VL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_CC1VL"),"0"));
	   				L_dblCC2VL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_CC2VL"),"0"));
	   				//L_dblCC3VL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_CC3VL"),"0"));
					L_dblOCRVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_OCRVL"),"0"));
					
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
						L_strPPRDDS = L_strPRDDS;
						L_strCPRDDS = L_strPRDDS;
						L_flg1STSFL = false;
					}

			while(!L_flgEOF)		
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(L_strPRDDS+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				cl_dat.M_intLINNO_pbst+= 1;
				L_strPPRDDS = L_strPRDDS;
				while(L_strPRDDS.equals(L_strPPRDDS) && !L_flgEOF)
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
					dosREPORT.writeBytes(padSTRING('L',"",4));
					dosREPORT.writeBytes(padSTRING('R',L_strINVNO,13));
					dosREPORT.writeBytes(padSTRING('R',L_strINVDT,12));
					dosREPORT.writeBytes(padSTRING('R',L_strDSPTP.substring(0,5),5));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblINVQT,3),10));
					dosREPORT.writeBytes(padSTRING('L',"",2));
					dosREPORT.writeBytes(padSTRING('R',L_strINDNO,10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblINVRT,2),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCC1VL,2),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCC2VL,2),10));
					//dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCC3VL,2),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblOCRVL,2),10));
					dosREPORT.writeBytes("\n");
   					cl_dat.M_intLINNO_pbst+= 1;
					L_dblPRDQT+=  L_dblINVQT;
					L_dblTOTQT+=  L_dblINVQT;
						if(!M_rstRSSET.next())
						{
							L_flgEOF = true;
							break;
						}
                    L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"");
					L_strINVNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
					L_strINVDT = nvlSTRVL(M_rstRSSET.getString("IVT_INVDT").substring(0,10),"");
					L_strDSPTP = nvlSTRVL(M_rstRSSET.getString("DSPTP"),"");
					L_strINDNO = nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),"");
	   				L_dblINVQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
	   				L_dblINVRT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVRT"),"0"));
	   				L_dblCC1VL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_CC1VL"),"0"));
	   				L_dblCC2VL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_CC2VL"),"0"));
	   				//L_dblCC3VL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_CC3VL"),"0"));
	   				L_dblOCRVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_OCRVL"),"0"));
				}//	while((L_strPRDDS).equals(L_strPPRDDS) && !L_EOF)
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Total for "+L_strPPRDDS,34));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRDQT,3),10)+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				cl_dat.M_intLINNO_pbst+= 3;
				L_dblPRDQT = 0;
			}//while(eof())
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				//cl_dat.M_intLINNO_pbst+= 1;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Grand Total ",34));
			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,3),10)+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------\n");		
				cl_dat.M_intLINNO_pbst+= 3;
				L_dblTOTQT = 0;
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
	Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,120));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Price Difference Report between " +txtFMDAT.getText().trim()+ " and " + txtTODAT.getText().trim()+" for "+txtBYRNM.getText().trim()+" "+txtBYRCD.getText().trim(),120));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("For Grade : "+txtPRDDS.getText()+"\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("Grade                                                                                                                                             \n");		    			
			dosREPORT.writeBytes("    Invoice No.  Date        Desp.  Quantity  Indent    Basic Rate                  Credit Note                 Agreed       Difference  Amount   \n");
			dosREPORT.writeBytes("                             Type    (MT)       No.     Rs./MT        Buyer    Distrib.   Third Party  Other    Price(Rs.)    (Rs./MT)   (Rs.)    \n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 9;
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
String L_strBYRCD="",L_strPRDCD="";

	public boolean verify(JComponent Jcomp)
	{
		if(((JTextField)Jcomp).getText().length() == 0)
			return true;
		if(Jcomp == txtBYRCD)
		{
			L_strBYRCD = txtBYRCD.getText().toString().trim().toUpperCase();
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST where ";
			M_strSQLQRY +=" PT_PRTTP = 'C' AND PT_PRTCD ='"+txtBYRCD.getText().trim()+"'";
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
						M_rstRSSET.close();
						return true;	
					}	
				}	
				catch(Exception L_EX)
				{
					setMSG(L_EX," Error Validating Buyer Code");
				}
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}	

		if(Jcomp == txtPRDCD)
		{
			L_strPRDCD = txtPRDCD.getText().toString().trim().toUpperCase();
			M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where ";
			M_strSQLQRY +=" PR_PRDCD ='"+txtPRDCD.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				try
				{
					if(!M_rstRSSET.next())
					{	
						setMSG("Invalid Product Code....Press F1 for Help..",'E');
						return false;
					}
					else
					{	
						M_rstRSSET.close();
						return true;
					}	
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX," Error Validating Product Code");
				}
			}
		}
		return false;
	}
}	
}
