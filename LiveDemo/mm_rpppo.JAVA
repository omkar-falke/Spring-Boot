/*
System Name   : Material Management System
Program Name  : Pending Purches Order.
Program Desc. : 
Author        : Mr S.R.Mehesare
Date          : 04/10/2005
Version       : MMS 2.0
*/


import javax.swing.JComboBox;import java.sql.Date;
import java.sql.ResultSet;import java.util.Hashtable;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.KeyEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
/**<pre>
System Name : Material Management System.
 
Program Name : Peinding Purches Order.

Purpose : Program for Peinding Purches Order Report for Specified Criteria.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD            #
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name    Column Name     Table name      Type/Size     Description
--------------------------------------------------------------------------------------
cmbRPTOP      To Specify Report option as Department Wise,Vendor Wise and P.O. Wise.
txtFMDPT      PO_DPTCD         MM_POMST       VARCHAR(3)    Department group Code
txtTODPT      PO_DPTCD         MM_POMST       VARCHAR(3)    Department group Code
txtVENCD      PO_VENCD         MM_POMST       VARCHAR(5)    Vendor Code
txtPORNO      PO_PORNO         MM_POMST       VARCHAR(8)    Purchase Order Number
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from MM_POMST,CO_CTMST and CO_PTMST for condiations :-
    1) CT_MATCD = PO_MATCD 
    2) AND PT_PRTCD = PO_VENCD 
    3) AND PT_PRTTP = 'S' 
    4) AND isnull(CT_STSFL,' ') <> 'X'
    5) AND PO_PORTP <> '03' 
    6) AND PO_STRTP = Secified Store Type
    7) AND isnull(PO_PORQT,0) - isnull(PO_ACPQT,0) - isnull(PO_FRCQT,0) > 0 
    8) AND PO_DPTCD between given Department Range
  if Vendor code is specified
    9) AND PO_VENCD = Specified Vendor Code.
  if purchase Order Number is given
   10) AND PO_PORNO = Specified urchase Order Number.
   11) AND isnull(PO_STSFL,' ') NOT IN ('X','O','C')

<B>Validations & Other Information:</B>    
    - Department Code specified must be valid.
    - Vendor Code entered must be valid.
    - Purchase order Number entered must be valid.
</I> */
class mm_rpppo extends cl_rbase
{										/** JCombobox for Report Option Type.*/
	private JComboBox cmbRPTOP;			/** Text Field For From Department Code*/
	private JTextField txtFMDPT;		/** Text Field For To Department code*/
	private JTextField txtTODPT;		/**	Text Field for vendor code */
	private JTextField txtVENCD;		/**	Text Field for p.o. number */
	private JTextField txtPORNO;		/** Flag To Check If Data Is get Or Not On Condition */
	private char chrDATA;				/** String For Department Name */
	private String strDPTNM;			/** String variable for generate Report File Name */
	private String strFILNM;	  		/** FileOutputStream object to generate the Report from Stream of Data.*/
	private FileOutputStream fosREPORT ;/** DataOutputStream object to generate the Stream of data.*/
    private DataOutputStream dosREPORT ;/** HashTable to hold the Department Code with description.*/
	private Hashtable<String,String> hstDPTCD;			/** String variable to print Dotted line in the Report.*/
	private String strDOTLN = "------------------------------------------------------------------------------------------------";
	private JLabel lblFRVAL,lblTOVAL;
	private JTextField txtFMDAT;
	private JTextField txtTODAT;
	public mm_rpppo()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			
			setMatrix(20,8);
			add(new JLabel("Report Option"),2,4,1,1,this,'L');
			add(cmbRPTOP = new JComboBox(),2,5,1,1.5,this,'L');
			add(lblFRVAL = new JLabel("From Dept."),3,4,1,1,this,'L');
			add(txtFMDPT = new TxtNumLimit(3.0),3,5,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),3,5,1,1,this,'L');
			add(lblTOVAL = new JLabel("To Dept."),4,4,1,1,this,'L');
			add(txtTODPT = new TxtNumLimit(3.0),4,5,1,1,this,'L');
			add(txtTODAT = new TxtDate(),4,5,1,1,this,'L');
			add(new JLabel("Ven. Code"),5,4,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),5,5,1,1,this,'L');
			add(new JLabel("P.O. No."),6,4,1,1,this,'L');
			add(txtPORNO = new TxtNumLimit(8.0),6,5,1,1,this,'L');			
			
			cmbRPTOP.addItem("Department Wise");
			cmbRPTOP.addItem("Vendor Wise");
			cmbRPTOP.addItem("P.O. Wise");
			cmbRPTOP.addItem("Date Wise");
			M_pnlRPFMT.setVisible(true);
			
			hstDPTCD = new Hashtable<String,String>(10,0.8f);			
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' ORDER BY CMT_CODCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())				
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				M_rstRSSET.close();
			}			
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Cconstructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter Department Code OR Press F1 to Select from List..",'N');				
				setENBL(true);				
				txtFMDPT.requestFocus();
				txtVENCD.setEnabled(false);
				txtPORNO.setEnabled(false);
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}			
		}
		if(M_objSOURC == cmbRPTOP)
		{
			txtFMDPT.setText("");
			txtTODPT.setText("");
            txtFMDAT.setText("");
			txtTODAT.setText("");
			txtVENCD.setText("");
			txtPORNO.setText("");
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				txtFMDPT.setVisible(true);
				txtTODPT.setVisible(true);
				txtFMDAT.setVisible(false);
				txtTODAT.setVisible(false);
				txtFMDPT.setEnabled(true);
				txtTODPT.setEnabled(true);
			
				txtFMDAT.setEnabled(false);
				txtTODAT.setEnabled(false);
				txtVENCD.setEnabled(false);
				txtPORNO.setEnabled(false);
				txtFMDPT.requestFocus();
			}
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				txtFMDPT.setEnabled(false);
				txtTODPT.setEnabled(false);
				
				txtFMDAT.setEnabled(false);
				txtTODAT.setEnabled(false);
				txtVENCD.setEnabled(true);
				txtPORNO.setEnabled(false);
				lblFRVAL.setText("From Dept.");
				lblTOVAL.setText("To Dept.");
				txtVENCD.requestFocus();
			}
			if(cmbRPTOP.getSelectedIndex() == 2)
			{
				txtFMDPT.setEnabled(false);
				txtTODPT.setEnabled(false);
				txtFMDAT.setEnabled(false);
				txtTODAT.setEnabled(false);
				txtVENCD.setEnabled(false);
				txtPORNO.setEnabled(true);
				txtPORNO.requestFocus();
			}
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
			    txtFMDAT.setVisible(true);
				txtTODAT.setVisible(true);
				txtFMDAT.setEnabled(true);
				txtTODAT.setEnabled(true);
			    
			    txtFMDPT.setVisible(false);
				txtTODPT.setVisible(false);
			  
				txtFMDPT.setEnabled(false);
				txtTODPT.setEnabled(false);
				txtVENCD.setEnabled(false);
				txtPORNO.setEnabled(false);
				lblFRVAL.setText("From Date");
				lblTOVAL.setText("To Date");
				txtFMDPT.requestFocus();
			}
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;			
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDPT)				
			{
				setMSG("Enter Department Code to generate the Report..",'N');
				txtTODPT.requestFocus();
			}
			else if(M_objSOURC == txtFMDAT)				
			{
				setMSG("Enter From Date ..",'N');
				txtTODAT.requestFocus();
			}
			else if(M_objSOURC == txtTODPT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC == txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC == txtVENCD)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC == txtPORNO)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);			
			if(M_objSOURC == txtFMDPT)
			{
				M_strHLPFLD = "txtFMDPT";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
					+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' AND ";
				if(txtFMDPT.getText().trim().length() > 0)
					M_strSQLQRY += "CMT_CODCD LIKE '"+txtFMDPT.getText().trim()+"%' AND ";
				M_strSQLQRY += "isnull(CMT_STSFL,' ') <>'X' ORDER BY CMT_CODCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code","Name"},2,"CT");
			}
			if(M_objSOURC == txtTODPT)
			{
				M_strHLPFLD = "txtTODPT";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
					+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' AND ";
				if(txtTODPT.getText().trim().length() > 0)
					M_strSQLQRY += "CMT_CODCD LIKE '"+txtTODPT.getText().trim()+"%' AND ";
				if(txtFMDPT.getText().trim().length() > 0)
					M_strSQLQRY += "CMT_CODCD >= '"+txtFMDPT.getText().trim()+"' AND ";
				M_strSQLQRY += "isnull(CMT_STSFL,' ') <>'X' ORDER BY CMT_CODCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code","Name"},2,"CT");
			}
			if(M_objSOURC == txtVENCD)
			{
				M_strHLPFLD = "txtVENCD";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT DISTINCT PO_VENCD,PT_PRTNM FROM MM_POMST,CO_PTMST WHERE "
					+"PT_PRTCD = PO_VENCD AND PT_PRTTP = 'S' AND isnull(PO_STSFL,'') <> 'X' AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "
					+"AND isnull(PT_STSFL,' ')<> 'X' ";
				if(txtVENCD.getText().trim().length() > 0)
					M_strSQLQRY += " AND PO_VENCD LIKE '"+txtVENCD.getText().trim()+"%' ";
				M_strSQLQRY += " ORDER BY PO_VENCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Vendor Code","Vendor Name"},2,"CT");
					
			}
			if(M_objSOURC == txtPORNO)
			{
				M_strHLPFLD = "txtPORNO";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT DISTINCT PO_PORNO,PO_PORDT FROM MM_POMST WHERE "
					+"PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_PORQT,0) - isnull(PO_ACPQT,0) > 0 AND "
					+"isnull(PO_STSFL,' ') NOT IN ('X','O','C') AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtPORNO.getText().trim().length() > 0)
					M_strSQLQRY += " AND PO_PORNO LIKE '"+txtPORNO.getText()+"%' ";
				M_strSQLQRY += "ORDER BY PO_PORNO ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"P.O. Number","P.O. Date"},2,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * Super class method overrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMDPT")
		{
			txtFMDPT.setText(cl_dat.M_strHLPSTR_pbst);			
			cl_dat.M_flgHELPFL_pbst = false;
		}
		if(M_strHLPFLD == "txtTODPT")
		{
			txtTODPT.setText(cl_dat.M_strHLPSTR_pbst);			
			cl_dat.M_flgHELPFL_pbst = false;
		}
		if(M_strHLPFLD == "txtVENCD")
		{
			txtVENCD.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_flgHELPFL_pbst = false;
		}
		if(M_strHLPFLD == "txtPORNO")
		{
			txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_flgHELPFL_pbst = false;
		}
	}
	/**
	 * Method to validate data before execuation of the SQL Query.
	 */
	public boolean vldDATA()
	{
	    try
	    {
    		if(cmbRPTOP .getSelectedIndex() == 0)
    		{
    			if(txtFMDPT.getText().trim().length() < 3)
    			{
    				setMSG("Enter From Departmetn Code Or Press F1 For Help..",'E');
    				txtFMDPT.requestFocus();
    				return false;
    			}
    			if(txtTODPT.getText().trim().length() < 3)
    			{
    				setMSG("Enter To Departmetn Code Or Press F1 For Help..",'E');
    				txtTODPT.requestFocus();
    				return false;
    			}
    			if(Integer.parseInt(txtTODPT.getText().trim()) < Integer.parseInt(txtFMDPT.getText().trim()))
    			{
    				setMSG("To Department Should Be Grater Than Or Equal To From Department Code..",'E');
    				txtFMDPT.requestFocus();
    				return false;
    			}
    		}
    		if(cmbRPTOP .getSelectedIndex() == 1)
    		{
    			if(txtVENCD.getText().trim().length() < 5)
    			{
    				setMSG("Enter Vendor Code Or Press F1 For Help",'E');
    				txtVENCD.requestFocus();
    				return false;
    			}
    		}
    		if(cmbRPTOP .getSelectedIndex() == 2)
    		{
    			if(txtPORNO.getText().trim().length() < 8)
    			{
    				setMSG("Enter P.O. Number Or Press F1 For Help",'E');
    				txtPORNO.requestFocus();
    				return false;
    			}
    		}
    		if(cmbRPTOP .getSelectedIndex() == 3)
    		{
    			if(txtFMDAT.getText().trim().length() < 10)
    			{
    				setMSG("Enter From Date..",'E');
    				txtFMDAT.requestFocus();
    				return false;
    			}
    			else if(txtTODAT.getText().trim().length() < 10)
    			{
    				setMSG("Enter To Date..",'E');
    				txtTODAT.requestFocus();
    				return false;
    			}
    			else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
    			{
    				setMSG("From Date can not be greater than today's date..",'E');
    				return false;
    			}
    			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
    			{
    				setMSG("To Date can not be greater than today's date..",'E');
    				return false;
    			}
    			else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))> 0)
    			{
    				setMSG("To Date can not be smaller than From date..",'E');
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
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"vldDATA");
	    }
		return true;
	}	
	/**
	* Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpppo.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpppo.doc";
			
			getDATA();
						
			if(chrDATA == 'N')
			{
				setMSG("Data not found for the given Inputs..",'E');				
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
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM," List Of Non Moved Items "," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}	
	/**
	 * Method to fetch data from database & to club it with header & footer.
	 */
	public void getDATA()
	{		
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			chrDATA = 'N';
			String L_strDPTCD = "";	//Local String for department name
			String L_strPORNO = "";	//local string for p.o. number			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Peinding Purches Order</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			prnHEADER();
			
			M_strSQLQRY = "SELECT PO_PORNO,PO_MATCD,PO_PORDT,PO_CMPDT,PO_VENCD,PO_DPTCD,PO_UOMCD,"
				+"PO_PORQT,PO_ACPQT,(day(getdate()) - day(PO_CMPDT)) PO_PENDY,CT_MATDS,"
				+"PT_PRTNM FROM MM_POMST,CO_CTMST,CO_PTMST WHERE CT_MATCD = PO_MATCD AND "
				+"PT_PRTCD = PO_VENCD AND PT_PRTTP = 'S' AND isnull(CT_STSFL,' ') <> 'X' "
				+" AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_PORTP <> '03' AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' and "
			// Commented on 21/09/2004 API HBP
				//	+" (days(CURRENT_DATE) - days(PO_CMPDT)) > 0 AND "
				+"isnull(PO_PORQT,0) - isnull(PO_ACPQT,0) - isnull(PO_FRCQT,0) > 0 ";
			if(txtFMDPT.getText().trim().length() > 0 && txtTODPT.getText().trim().length() > 0)
				M_strSQLQRY += " and PO_DPTCD BETWEEN '"+txtFMDPT.getText().trim()+"' AND "
					+"'"+txtTODPT.getText().trim()+"' ";
			if(txtFMDAT.getText().trim().length() > 0 && txtTODAT.getText().trim().length() > 0)
				M_strSQLQRY += " and PO_PORDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND "
					+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' ";		
			if(txtVENCD.getText().trim().length() > 0)
				M_strSQLQRY += " and PO_VENCD = '"+txtVENCD.getText().trim()+"' ";
			if(txtPORNO.getText().trim().length() > 0)
				M_strSQLQRY += " and PO_PORNO = '"+txtPORNO.getText().trim()+"' ";
			M_strSQLQRY += " and isnull(PO_STSFL,' ') NOT IN ('X','O','C') ORDER BY PO_DPTCD,PO_PORNO,PO_MATCD";
		    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
				{					
					chrDATA = 'Y';
					/**Check If Line Number Exceeds 60	 */
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);
						prnFMTCHR(dosREPORT,M_strEJT);
						cl_dat.M_intLINNO_pbst = 0;
					//	cl_dat.M_PAGENO += 1;
						L_strDPTCD = "";
						L_strPORNO = "";
						prnHEADER();
					}
					/**Check If Depatrtment Code is Equal to Previous Departmetn Code */
					if(!(nvlSTRVL(M_rstRSSET.getString("PO_DPTCD"),"")).equals(L_strDPTCD))
					{
						dosREPORT.writeBytes("\n");
						L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("PO_DPTCD"),"");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes("Department Code And Name : "+nvlSTRVL(M_rstRSSET.getString("PO_DPTCD"),"")+"  ");
						if(hstDPTCD.containsKey(M_rstRSSET.getString("PO_DPTCD")))
						    dosREPORT.writeBytes(hstDPTCD.get(M_rstRSSET.getString("PO_DPTCD")).toString());
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 2;
					}
					/**Check If P.O Number is Equal to Previous P.O Number	 */
					if(!(nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),"")).equals(L_strPORNO))
					{
						dosREPORT.writeBytes("\n");
						L_strPORNO = nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),"");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),11));
						if(M_rstRSSET.getDate("PO_PORDT") != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PO_PORDT")),13));
						else
							dosREPORT.writeBytes(padSTRING('R',"",13));
						if(M_rstRSSET.getDate("PO_CMPDT") != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PO_CMPDT")),16));
						else
							dosREPORT.writeBytes(padSTRING('R',"",16));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),""),7));
						dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 2;
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),46));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_UOMCD"),""),4));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0"),12));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_ACPQT"),"0")) , 3),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_PENDY"),"0"),11));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;					
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN);
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);												
			}			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
				setMSG("For HTML Report Printing, Please insert 120 column Page..",'N');
			}	
			dosREPORT.close();
			fosREPORT.close();			
			setCursor(cl_dat.M_curDFSTS_pbst);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	/**
	 * Method to genrate the header part of the Report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 8;
			cl_dat.M_PAGENO ++;			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);			
			dosREPORT.writeBytes("\n"+ cl_dat.M_strCMPNM_pbst +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Department Wise Pending Purchase Order List ",strDOTLN.length()-22));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-22));
			//System.out.println("Page "+cl_dat.M_PAGENO);
            dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO);			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);			
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			dosREPORT.writeBytes("P.O. No.   P.O. Date    P.O. Comp.Date  Vendor Code And Name         Order    Balance    Pending"+"\n");			
			dosREPORT.writeBytes("Material Code & Description                               UOM        Qty.       Qty.       Days"+"\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");			
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
		}
	}
}
