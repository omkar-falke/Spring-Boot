 /*
System Name   : Laboratory Information Management System
Program Name  : Product Test Report
Program Desc. : Product Test Report
Author        : Mr.S.R.Mehesare
Date          : 24 May 2005
Version       : LIMS V2.0.0
Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        : 
*/

import java.sql.ResultSet;import java.util.Hashtable;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComboBox;import javax.swing.InputVerifier;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Product Test Report

Purpose : Report for Product Quality tests details.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
QC_PSMST       PS_QCATP,PS_TSTTP,PS_LOTNO,PS_RCLNO,PS_TSTNO,PS_TSTDT   #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name   Column Name  Table name           Type/Size     Description
--------------------------------------------------------------------------------------
txtPRDGR     CMT_CODDS    CO_CDTRN             VARCHAR(30)   Code Description
txtFMLOT     LT_LOTNO     PR_LTMST,CO_PRMST    VARCHAR(8)    Lot Number
txtTOLOT     LT_LOTNO     PR_LTMST,CO_PRMST    VARCHAR(8)    Lot Number
txtRKMDS     To append the Remark at the end of the Report.
--------------------------------------------------------------------------------------
<B>
Logic</B>
Each grade (finshed product) has some proparties. & these proparties changes as per 
new standards adopted. 
These proparties are varing from grade to grade.
System manages proparties of each grade & makes Latest propatries available.
According to the new standards, some new properties are added and some unwanted
are removed for specific grade.
Hence to generate report every time list of proparties are generated dynamically as :-
   1) Latest property list is fetched from the database associated with perticular grade .
   2) These Properties are maintained in the array.
   3) List of Columns to fetch from the database is generated dynamically as 
    "SMT_"+ element in the array +"VL" i.e SMT_COLVL, SMT_TOLVL
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	
<I>
<B>Conditions given in Query for F1 help:</b> 
    For Product Code & Description Data is taken From CO_CDTRN
      1) CMT_CGMTP ='MST' 
      2) AND CMT_CGSTP ='COXXPGR'
      3) AND CMT_CCSVL = 'SG' 
      4) AND CMT_CHP01 =given Rpoduct Type here it is "01"
    To Specify the Lot Number Range
      1) LT_CLSFL = '9' 
      2) AND LT_CPRCD = PR_PRDCD // for classified lots
      3) AND SUBSTRING(LT_PRDCD,1,4) = given Product Grade
      4) AND LT_PRDCD = PR_PRDCD       
      5) Condidtation to check that From-Lot number must be smaller than To-Lot number

<B>Conditions given in Query:</b>
    Data is taken from PR_LTMST,CO_PRMST and QC_PSMST  for given Data as
       1) LT_LOTNO = PS_LOTNO 
       2) AND LT_RCLNO = PS_RCLNO 
       3) AND PS_QCATP = given QCA Type here it is "01"
       4) AND PS_TSTTP = Composite certification Test here it is "0103"
       5) AND SUBSTRING(LT_PRDCD,1,4) = given Product Grade
       6) AND PS_LOTNO BETWEEN give Lot Range.		       
       
<B>Validations :</B>
    - Product Grade must be valid.
    - Lot Range must be valid.
    - Test Properties are shown for finally classified lots only.
    - Test parameters list is fecthed from
         SUBSTRING(cmt_codcd,6,3) RPT/QCXXPTR SUBSTRING(cmt_codcd,0,4) = LM_PRDGR
</I> */

public class qc_rpptr extends cl_rbase
{				
	private JComboBox cmbPRDTP;			/** JTextField to enter & display Product Code.*/
	private JTextField txtPRDGR;		/** JTextField to display Product Description.*/
	private JTextField txtPRDDS;		/** JTextField to enter & display Lot Number to specify Lot range.*/
	private JTextField txtFMLOT;		/** JTextField to enter & display Lot Number to specify Lot range.*/
	private JTextField txtTOLOT;		/** JTextField to enter & display Remarks.*/
	private JTextField txtRMKDS;		/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	/** String variable for generated report file name.*/    
	private String strFILNM;			/** String Variable to specify Product Type.*/						
	private String strPRDTP="";		/** String Variable for dynamically generated header.*/	
	private String strTHEAD="";			/** String Variable for Test Method Code.*/	
	private String strTSMCD;			/** String Variable for Test Method Description.*/	
	private String strTSMDS;			/** String Variable for Product Group.*/
	private String strPRDGR = "";		/** String Variable to specify QCA Type.*/ 
	private String strQCATP="";		/** String Variable for composite certification Test.*/
	private String strTSTTP = "0103";	/** Array of String to hold Quality proparties of a Material.*/
	private String arrQPRCD[];			/**	StringBuffer to hold dynamically generated dotted line.*/
	private StringBuffer stbDOTLN;		/** Integer variable to hold the dynamicalyy generated Column width.*/
	private int intCOLWD;				/** Integer variable for the dynamicalyy generated Column width.*/
	private int intROWLN;				/** Integer variable to hold the dynamicalyy generated Column width.*/
	private int intROWCT;				/** Integer variable to count the number of records fetched.*/
	private int intRECCT;				/** Array of Integers to hold Column width of each Column.*/
	private int[] arrCOLWID;			/** Array of Integers to Specify the number of variables after decimal Point*/
	private int[] arrCOLDEC;			/** Resultset boject for database functionality.*/
	private ResultSet M_rstRSSET1;
	private Hashtable<String,String> hstUOMCD;
	private Hashtable<String,String> hstSHRDS;
	private Hashtable<String,String> hstTSMDS;	
	qc_rpptr()
	{
		super(2);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Product Type"),4,3,1,1,this,'R');
			add(cmbPRDTP = new JComboBox(),4,4,1,2,this,'L');				
		    add(new JLabel("Product"),5,3,1,1,this,'R');
			add(txtPRDGR = new JTextField(),5,4,1,1,this,'L');
			add(txtPRDDS = new JTextField(),5,5,1,2.5,this,'L');			
			add(new JLabel("From Lot No"),6,3,1,1,this,'R');
			add(txtFMLOT = new TxtNumLimit(8),6,4,1,1,this,'L');			
			add(new JLabel("To Lot No"),7,3,1,1,this,'R');
			add(txtTOLOT = new TxtNumLimit(8),7,4,1,1,this,'L');			
			add(new JLabel("Remark"),8,3,1,1,this,'R');
			add(txtRMKDS = new TxtLimit(200),8,4,1,4,this,'L');			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
						
			hstUOMCD = new Hashtable<String,String>();
			hstSHRDS = new Hashtable<String,String>();
			hstTSMDS = new Hashtable<String,String>();
			
			M_strSQLQRY = "Select QS_QPRCD,QS_SHRDS,QS_UOMCD,QS_TSMCD from CO_QSMST where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
					{
						hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));
						hstSHRDS.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_SHRDS"),""));
						hstTSMDS.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_TSMCD"),""));
					}
				}				
				M_rstRSSET.close();
			}	
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD''";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD + " "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{				
				setENBL(true);
				txtPRDDS.setEnabled(false);				
				setMSG("Please Enter Product Code OR Press F1 to select from list..",'N');
				cmbPRDTP.requestFocus();					
			}
			else
			{
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == cmbPRDTP)
		{
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);			
			txtPRDGR.requestFocus();
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;	
		}
		else if(M_objSOURC == txtPRDGR)
		{
			txtPRDDS.setText("");
			txtFMLOT.setText("");
			txtTOLOT.setText("");
			txtRMKDS.setText("");
			if (txtPRDGR.getText().length() != 4)
			{
				setMSG("Please Enter complete product code or press F1 to select from list..",'E');
				return;
			}
			else
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{									
					M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN";					
					M_strSQLQRY +=	" where CMT_CODCD ='" +txtPRDGR.getText().trim() + "'";
					M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET1 !=null)
					{
						if(M_rstRSSET1.next())
							txtPRDDS.setText(M_rstRSSET1.getString("CMT_CODDS"));
						else
						{
							setMSG("Invalid Product Group, Press F1 to select from List..",'E');
							txtPRDGR.requestFocus();
						}
						M_rstRSSET.close();
					}					
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"ActionPerformed");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
		}		
		else if(M_objSOURC == txtFMLOT)
		{
			try
			{
				M_strSQLQRY = "select LT_LOTNO from PR_LTMST";
				M_strSQLQRY += " Where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL = '9'";
				M_strSQLQRY += " AND SUBSTRING(LT_PRDCD,1,4) = "+"'"+txtPRDGR.getText().trim()+"'"; 
				M_strSQLQRY += " AND LT_STSFL <> 'X'";	 				
				M_strSQLQRY +=	"AND LT_LOTNO = '" +txtFMLOT.getText().trim() + "'";
				if(txtTOLOT.getText().length() == 8)
					M_strSQLQRY +=	"AND LT_LOTNO <'" +txtTOLOT.getText().trim() + "'";
				else if(txtTOLOT.getText().length() != 0)
				{
					setMSG("Invalid Lot Number..",'E');
					txtTOLOT.requestFocus();
				}
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(!M_rstRSSET.next())					
					{
						setMSG("Invalid Lot Number, Press F1 to select from List..",'E');
						txtFMLOT.requestFocus();
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == txtFMLOT");
			}
		}
		else if(M_objSOURC == txtTOLOT)
		{
			try
			{
				M_strSQLQRY = "select LT_LOTNO from PR_LTMST";
				M_strSQLQRY += " Where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL = '9'";
				M_strSQLQRY += " AND SUBSTRING(LT_PRDCD,1,4) = "+"'"+txtPRDGR.getText().trim()+"'"; 
				M_strSQLQRY += " AND LT_STSFL <> 'X'";	 				
				M_strSQLQRY +=	"AND LT_LOTNO = '" +txtTOLOT.getText().trim() + "'";
				if(txtFMLOT.getText().length()== 8)
					M_strSQLQRY +=	"AND LT_LOTNO >'" +txtFMLOT.getText().trim() + "'";
				else if(txtFMLOT.getText().length() != 0)
				{
					setMSG("Invalid Lot Number..",'E');
					txtFMLOT.requestFocus();
				}
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(!M_rstRSSET.next())					
					{
						setMSG("Invalid Lot Number, Press F1 to select from List..",'E');
						txtTOLOT.requestFocus();
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == txtFMLOT");
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
			if(M_objSOURC == txtPRDGR)
			{
				txtPRDDS.setText("");
				txtFMLOT.setText("");
				txtTOLOT.setText("");
				txtRMKDS.setText("");
 				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{						
					M_strHLPFLD = "txtPRDGR";          						
					M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,4)L_CODE,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP ='MST' AND CMT_CGSTP ='COXXPGR'";
					M_strSQLQRY += " AND CMT_CCSVL = 'SG' AND CMT_CHP01 ='"+strPRDTP +"'";
					if (txtPRDGR.getText().length()!=0)
						M_strSQLQRY +=	"AND CMT_CODCD LIKE '" +txtPRDGR.getText().trim() + "%'";
					M_strSQLQRY += "order by CMT_CODCD ";				    									
    				cl_hlp(M_strSQLQRY,2,1,new String[]{"Type.  ","Description."},2,"CT");    																					   			  			  
    			}
    			catch(Exception L_EX)				
    			{
    			    setMSG(L_EX ," F1 help..");    		    
    			}
		  	    setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(M_objSOURC == txtFMLOT)
			{			 			   
				M_strHLPFLD = "txtFMLOT";          						
				M_strSQLQRY = "select LT_LOTNO,LT_PSTDT,PR_PRDDS,LT_CLSFL from PR_LTMST,CO_PRMST";
				M_strSQLQRY += " Where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL = '9' and LT_CPRCD = PR_PRDCD"; // for classified lots
				M_strSQLQRY += " AND SUBSTRING(LT_PRDCD,1,4) = "+"'"+txtPRDGR.getText().trim()+"'"; 
				M_strSQLQRY += " AND LT_PRDCD = PR_PRDCD AND LT_STSFL <> 'X'";	 
			   	if(txtFMLOT.getText().length()!=0)
					M_strSQLQRY +=	"AND LT_LOTNO LIKE '" +txtFMLOT.getText().trim() + "%'";
				if(txtTOLOT.getText().length()!=0)
					M_strSQLQRY +=	"AND LT_LOTNO <'" +txtTOLOT.getText().trim() + "'";
				M_strSQLQRY += " order by LT_LOTNO ";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No. ","Lot Start Date","Clasfn. Grade ","Clasfn. Flag"},4,"CT");
			}
			if(M_objSOURC == txtTOLOT)
			{
				M_strHLPFLD = "txtTOLOT";          						
				M_strSQLQRY = "select LT_LOTNO,LT_PSTDT,PR_PRDDS,LT_CLSFL from PR_LTMST,CO_PRMST";
				M_strSQLQRY += " Where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL = '9' and LT_CPRCD = PR_PRDCD"; // for classified lots
				M_strSQLQRY += " AND SUBSTRING(LT_PRDCD,1,4) = "+"'"+txtPRDGR.getText().trim()+"'"; 
				M_strSQLQRY += " AND LT_PRDCD = PR_PRDCD AND LT_STSFL <> 'X'";	 				
				if(txtTOLOT.getText().length()!=0)
					M_strSQLQRY +=	"AND LT_LOTNO like'" +txtTOLOT.getText().trim() + "%'";
			   	if(txtFMLOT.getText().length()!=0)
					M_strSQLQRY +=	"AND LT_LOTNO >'" +txtFMLOT.getText().trim() + "'";				
				M_strSQLQRY += " order by LT_LOTNO ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No.   ","Lot Start Date","Clasfn. Grade ","Clasfn. Flag"},4,"CT");
			}
 	    }
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{							
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{
					setMSG("Please Enter Product Code OR Press F1 to select from list..",'N');
					txtPRDGR.requestFocus();	
				}
				else
				{					
					setMSG("Please Select an option..",'N');
					cl_dat.M_cmbOPTN_pbst.requestFocus();
				}				
			}			
			if(M_objSOURC == txtPRDGR)
			{
				if (txtPRDGR.getText().trim().length()>0)
				{										
					txtFMLOT.requestFocus();
					setMSG("Enter Lot number to specify lot..",'N');
				}
				else
				{
					txtPRDGR.requestFocus();
					setMSG("Enter Product Code OR Press F1 to select form List..",'N');
				}	
			}
			if(M_objSOURC == txtFMLOT)
			{				
				if (txtFMLOT.getText().trim().length()>0)
				{
					txtTOLOT.requestFocus();
					setMSG("Enter Lot number to specify lot..",'N');
				}
				else
				{
					txtFMLOT.requestFocus();
					setMSG("Enter Lot number to specify lot..",'E');
				}
			}
			if(M_objSOURC == txtTOLOT)
			{
				if (txtTOLOT.getText().trim().length()>0)
				{
					txtRMKDS.requestFocus();
					setMSG("Enter Remarks if any..",'N');
				}
				else
				{
					txtTOLOT.requestFocus();
					setMSG("Enter Lot number to specify lot..",'E');
				}
			}
			if(M_objSOURC == txtRMKDS)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("",'N');
			}
		}
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPRDGR")
		{
			txtPRDGR.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMLOT.requestFocus();
			txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}	
		if(M_strHLPFLD == "txtFMLOT")
		{
			txtFMLOT.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOLOT.requestFocus();
		}		
		if(M_strHLPFLD == "txtTOLOT")
		{
			txtTOLOT.setText(cl_dat.M_strHLPSTR_pbst);
			txtRMKDS.requestFocus();
		}	
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpptr.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpptr.doc";				
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			if(intRECCT == 0)
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Product Test Report"," ");
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
    *Method to fetch data from Database & club it with Header & footer in Data Output Stream.
	*/
	private void getDATA()
	{
		String L_strPRDCD = "",L_strPRVCD = "";
		String L_strLOTNO = "",L_strPRDDS = "", L_strSTRFLD = "",L_strSTRLIN = "";
		String L_srQPRVL = "",L_strRCLNO = "",L_strPRVLOT ="";
		int i=0;
	    strQCATP = M_strSBSCD.substring(2,4);
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Product Test Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}						
			setCursor(cl_dat.M_curWTSTS_pbst);
			getTSTDET();
			setCursor(cl_dat.M_curDFSTS_pbst);			
			prnHEADER();			
			
			M_strSQLQRY = "Select LT_PRDCD,LT_LOTNO,LT_RCLNO,PR_PRDDS";									
			//-------------------------------------
			for(int k=0;k<arrQPRCD.length;k++)
			{
				M_strSQLQRY += ","+"PS_"+arrQPRCD[k]+"VL";
			}
			//-----------------------------------error in array initialization in case of off gade Material
			M_strSQLQRY += " FROM PR_LTMST,CO_PRMST,QC_PSMST where LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO AND LT_CMPCD=PS_CMPCD";
			M_strSQLQRY += " AND PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = " + "'"+strQCATP + "'" + " AND PS_TSTTP = " + "'"+ strTSTTP + "'";
			M_strSQLQRY += " AND SUBSTRING(LT_PRDCD,1,4) = "+"'"+txtPRDGR.getText().trim()+"'";
			M_strSQLQRY += " AND PS_LOTNO BETWEEN " +"'" +txtFMLOT.getText().trim() + "'" + " AND "+"'"+txtTOLOT.getText().trim() + "'";
			M_strSQLQRY += " AND PS_STSFL <> 'X' AND LT_PRDCD = PR_PRDCD ORDER by LT_LOTNO,LT_RCLNO";						
			
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
					intRECCT = 1;
					L_strSTRLIN = "";
					L_strPRDCD = M_rstRSSET.getString("LT_PRDCD"); 									
					if(L_strPRDCD !=null)
					{						
						if(!L_strPRVCD.equals(L_strPRDCD.substring(0,4)))//getPRDCAT(L_strPRDCD.substring(0,4));						
						{						
							try
							{
								M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CODCD= ";
								M_strSQLQRY += "'"+L_strPRDCD.trim().substring(0,4) + "00000A"+"'" ;
								M_strSQLQRY += " AND CMT_CCSVL = 'SG'";								
						    	M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
								if(M_rstRSSET1 !=null)								
								{
									if(M_rstRSSET1.next())
									{										
										strPRDGR = M_rstRSSET1.getString("CMT_CODDS");									
										dosREPORT.writeBytes("\n");
										cl_dat.M_intLINNO_pbst += 1;																		
									}																
									M_rstRSSET1.close();
								}
							}
							catch(Exception L_EX)
							{
								setMSG(L_EX,"getPRDCAT");
							}												
						}
						L_strPRVCD = L_strPRDCD.substring(0,4);						
					}				
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("LT_LOTNO"),"-"); 
					L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("LT_RCLNO"),""); 			
					
					if(!L_strLOTNO.trim().equals(L_strPRVLOT.trim()))
					{							
						L_strSTRLIN = padSTRING('R',L_strLOTNO.trim(),9)+padSTRING('R',L_strRCLNO.trim(),4);
						L_strPRVLOT = L_strLOTNO.trim();
					}
					else
						L_strSTRLIN = padSTRING('R'," ",9)+padSTRING('R',L_strRCLNO.trim(),4);										
					
					L_strPRDDS = M_rstRSSET.getString("PR_PRDDS"); 
					if(L_strPRDDS !=null)
						L_strSTRLIN += padSTRING('R',L_strPRDDS.trim(),7);				
					else
						L_strSTRLIN += padSTRING('R',"-",7);				
					i =i+1;					
					for(int l=0;l<arrQPRCD.length;l++)
					{
						L_strSTRFLD = "PS_"+arrQPRCD[l]+"VL";
						L_srQPRVL = nvlSTRVL(M_rstRSSET.getString(L_strSTRFLD),"0"); 						
						L_strSTRLIN += padSTRING('L',setNumberFormat(Double.valueOf(L_srQPRVL.trim()).doubleValue(),arrCOLDEC[l]),intCOLWD);												
					}										
					dosREPORT.writeBytes(L_strSTRLIN);					
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					
					if(cl_dat.M_intLINNO_pbst> 60)
					{																		
						dosREPORT.writeBytes(stbDOTLN.toString());
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();					
					}
				}							
				dosREPORT.writeBytes(stbDOTLN.toString());				
				if (intROWLN >15)					
				{	
					dosREPORT.writeBytes("\n\nRemark : "+ txtRMKDS.getText().trim());
					dosREPORT.writeBytes("\n\n" + padSTRING('L',"H.O.D.(QCA)",intROWLN-15));
				}				
				M_rstRSSET.close();
			}						
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    			
			dosREPORT.close();
			fosREPORT.close();
		}
		catch(Exception L_EX)
		{			
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	Method to validate Consignment number, before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		if(txtPRDGR.getText().length()==0)			
		{
			setMSG("Please Enter Product Code OR Press Enter Key.. ",'E');
			txtPRDGR.requestFocus();			
			return false;
		}
		if( txtFMLOT.getText().trim().length() == 0)
		{
			setMSG("Lot number cannot be blank..",'E');
			txtFMLOT.requestFocus();
			return false;
		}
		if( txtTOLOT.getText().trim().length() == 0)
		{
			setMSG("Lot number cannot be blank..",'E');
			txtTOLOT.requestFocus();
			return false;
		}
		if(Integer.valueOf(txtTOLOT.getText().trim()).intValue() < Integer.valueOf(txtFMLOT.getText().trim()).intValue())
		{
			setMSG("TO Lot must be greater than From Lot Number..",'E');
			txtTOLOT.requestFocus();
			return false;
		}
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getSelectedIndex() == 0)
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
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			if(intROWLN <= 24)
			{
				setMSG("No Data Found..",'E');
				return ;
			}
			String L_strSRLNO = "";			
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;		
			dosREPORT.writeBytes("\n\n\n\n\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWLN)+"\n");													
			dosREPORT.writeBytes(padSTRING('R',"PRODUCT TEST REPORT",intROWLN-24));									
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst+ "\n");					
			dosREPORT.writeBytes(padSTRING('R',txtPRDDS.getText().trim()+" Lots from "+txtFMLOT.getText().trim()+" To "+txtTOLOT.getText().trim(),intROWLN-24));			
			dosREPORT.writeBytes("Page No.    : " + cl_dat.M_PAGENO + "\n");				
			dosREPORT.writeBytes(strTHEAD);	
			cl_dat.M_intLINNO_pbst = 13;
		}		
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	 * Method to generate header of the Report dynamically  from the Test Parameters fetched.
	 */
	private void getTSTDET()
	{		
		StringBuffer L_stbTHEAD=new StringBuffer("\n");		
		try
		{
			L_stbTHEAD.append(padSTRING('R',"LOT NO. RCL",13));
			L_stbTHEAD.append(padSTRING('R',"Grade",7));
			intROWLN = 30; //Total number of charectors in a row
			M_strSQLQRY = "select SUBSTRING(CMT_CODCD,6,3)arrQPRCD,CMT_NCSVL,CMT_NMP01,CMT_NMP02 from CO_CDTRN where CMT_CGMTP ='RPT'";
			M_strSQLQRY += " AND CMT_CGSTP ='QCXXPTR' AND SUBSTRING(CMT_CODCD,1,4) = " + "'"+txtPRDGR.getText().trim() + "'";				
			M_strSQLQRY += " ORDER BY CMT_NCSVL";						
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,   ResultSet.CONCUR_UPDATABLE);		
			M_rstRSSET = L_stat.executeQuery(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{	
				intROWCT=0;
				while(M_rstRSSET.next())
					intROWCT++;					
				if(intROWCT >0)
				{															
					arrQPRCD = new String[intROWCT]; 
					arrCOLWID = new int[intROWCT]; 
					arrCOLDEC = new int[intROWCT]; 					
					if(intROWCT < 9)
						intCOLWD = 13;
					else
						intCOLWD = 11;
				}				
				intROWLN += (intCOLWD * (intROWCT-1)) + 3;								
				M_rstRSSET.beforeFirst();
				int i=0;				
				while(M_rstRSSET.next())
				{
					arrQPRCD[i] = M_rstRSSET.getString("arrQPRCD"); //Code					
					if(arrQPRCD[i]!=null)
					{
						arrCOLWID[i] = M_rstRSSET.getInt("CMT_NMP01"); // col width
						arrCOLDEC[i] = M_rstRSSET.getInt("CMT_NMP02"); // decimal
					}
					i=i+1;
				}
			    M_rstRSSET.close();					
				for(int j=0;j<intROWCT;j++)    // Column Heading
				{										
					String L_strDESC = hstSHRDS.get(arrQPRCD[j]).toString();
					if(L_strDESC !=null)				
						L_stbTHEAD.append(padSTRING('L',L_strDESC.trim(),intCOLWD));		             					
				}				
				L_stbTHEAD.append("\n" + padSTRING('R'," ",20));				   // Unit of measurement.				
				String L_strUOM ="";
				for(int j=0;j<intROWCT;j++)  
				{					
					L_strUOM = hstUOMCD.get(arrQPRCD[j]).toString();
					if(L_strUOM !=null)		               
						L_stbTHEAD.append(padSTRING('L',L_strUOM.trim(),intCOLWD));
				}																				
				L_stbTHEAD.append("\n" + padSTRING('R'," ",20));   // For Test Method.				
				for(int j=0;j<intROWCT;j++) 
				{	
					try
					{
						strTSMCD = arrQPRCD[j];						
						if(strTSMCD !=null)
						{														
							strTSMDS = hstTSMDS.get(strTSMCD).toString();							
							if(strTSMDS.length() >0)
								L_stbTHEAD.append(padSTRING('L',strTSMDS.trim(),intCOLWD));
							else
								L_stbTHEAD.append(padSTRING('L',"-",intCOLWD));							
						}											
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"getTSMCD");
					}																								
				}				
			}
			stbDOTLN = new StringBuffer("-");			
			for(int i=0;i<intROWLN;i++)
				stbDOTLN.append("-");						
			L_stbTHEAD.append("\n");								
			L_stbTHEAD.append(stbDOTLN.toString());															
			strTHEAD = stbDOTLN.toString() + L_stbTHEAD.toString();							
		}
		catch(Exception L_EX)
		{
			 setMSG(L_EX,"getTSTDET");			 
		}		
		return;
	}	
}			