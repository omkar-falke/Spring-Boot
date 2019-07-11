/*
System Name		: Materials Management System
Program Name	: Receipt Summary
Author			: Mr. S.R.Mehesare
Modified Date	: 18/10/2005
Documented Date	: 
Version			: MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.KeyEvent;import java.sql.ResultSet;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;
import java.util.StringTokenizer;import java.util.Hashtable;
/**<pre>
System Name : 
 
Program Name : 

Purpose : 

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from MM_GRMST and CO_CTMST for condiations :-
  
<B>Validations & Other Information:</B>    
    - 
</I> */

class mr_rpexd extends cl_rbase
{								/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtFMDAT;/** JTextField to display to enter To date to specify date range.*/
	private JTextField txtTODAT;
	private JTextField txtLCDAT;
	private JTextField txtLCNUM;
	private cl_JTable tblDATA;
	private cl_JTable tblQPRCD;
	
	/** String variable for Store Type.*/
	private String strSTRTP;	/** String variable for Store Type Description.*/
	private String strSTRNM;	/** String variable for generated Report File Name.*/
	private String strFILNM;		
	/** Integer Variable to count the Number of records fetched to block the Report if No data Found.*/
	private int intRECCT;		/** DataOutputStream to generate hld the stream of data.*/
	private DataOutputStream dosREPORT;/** FileOutputStream to generate the Report file form stream of data.*/
	private FileOutputStream fosREPORT;/** String variable to print Dotted Line in the Report.*/	
	private StringBuffer stbDOTLN = new StringBuffer();
	
	
	private int intROWCT = 300;
	private int intROWCT1 = 15;
	
	private final int TB1_CHKFL = 0;
	private final int TB1_QPRCD = 1;	
	private final int TB1_QPRDS = 2;	
	private final int TB1_TSMCD = 3;
	private final int TB1_UOMCD = 4;
		
	private final int TB2_CHKFL = 0;
	private final int TB2_INDNO = 1;
	private final int TB2_INVNO = 2;
	private final int TB2_BYRCD = 3;
	private final int TB2_CNTNO = 4;
	private final int TB2_LCNUM = 5;
	private final int TB2_LCDAT = 6;
	private final int TB2_CRTOR = 7; //Certificate of Origin 
	private final int TB2_CRTAN = 8; //Certificate of Analysis.
	private final int TB2_PRDDS = 9;
	private final int TB2_INVQT = 10;
	private final int TB2_LADNO = 11;
	private final int TB2_SALTP = 12;
	private final int TB2_MKTTP = 13;
	
	private int intROWNO;
	private int intCOLNO = 0;
	
	String arrQPRCD[] = new String[10];
	Hashtable<String,String> hstPRDCD = new  Hashtable<String,String>();
	Hashtable<String,String> hstPRDDS = new  Hashtable<String,String>();
	//Hashtable hstTSMCD = new  Hashtable();
	//Hashtable hstUOMCD = new  Hashtable();
	String arrQPRDS[];
	String arrTSMCD[];
	String arrUOMCD[];
	int intLOTCT;
	int intQPRCT;
	private StringTokenizer stkQPRLS;	
	
	private int intSLROW;
	public mr_rpexd()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		
			add(new JLabel("From Date"),2,2,1,.7,this,'R');
			add(txtFMDAT = new TxtDate(),2,3,1,1,this,'L');
			add(new JLabel("To Date"),2,4,1,.7,this,'R');
			add(txtTODAT = new TxtDate(),2,5,1,1,this,'L');
		
			String[] L_COLHD = {"Select","Para. Code","Description","Test Method","UOM"};
      		int[] L_COLSZ = {50,70,170,150,115};
			tblQPRCD = crtTBLPNL1(this,L_COLHD,intROWCT1,3,2,5.8,6,L_COLSZ,new int[]{0});	
		
			String[] L_COLHD1 = {"Update","Indent No","Invoice No","Buyer","Container No.","LC Number","LC Date","Origin","Analysis","Grade","Inv Quantity","Loading adv. No","Sale Type","Market Type"};
      		int[] L_COLSZ1 = {20,75,75,140,110,100,75,29,29,30,30,30,30,30};
			tblDATA = crtTBLPNL1(this,L_COLHD1,intROWCT,10,2,8.4,7,L_COLSZ1,new int[]{TB2_CRTOR,TB2_CRTAN,TB2_CHKFL});
		
			M_pnlRPFMT.setVisible(true);
			setENBL(false);	
			tblDATA.setCellEditor(TB2_LCDAT, txtLCDAT = new TxtDate());		txtLCDAT.addFocusListener(this);	txtLCDAT.addKeyListener(this);
			tblDATA.setCellEditor(TB2_LCNUM, txtLCNUM = new TxtLimit(20));	txtLCNUM.addFocusListener(this);	txtLCNUM.addKeyListener(this);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	/**
	 * Super Class method overrided to enable & disable the Components.
	 * @param L_flgSTAT boolean argument to pass State of the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);		
			
		//TB1_QPRCD TB1_QPRDS TB1_UOMCD TB1_TSMCD
		tblQPRCD.cmpEDITR[TB1_QPRCD].setEnabled(false);
		tblQPRCD.cmpEDITR[TB1_QPRDS].setEnabled(false);
		tblQPRCD.cmpEDITR[TB1_UOMCD].setEnabled(false);
		tblQPRCD.cmpEDITR[TB1_TSMCD].setEnabled(false);
					
		tblDATA.cmpEDITR[TB2_INDNO].setEnabled(false);
		tblDATA.cmpEDITR[TB2_INVNO].setEnabled(false);
		tblDATA.cmpEDITR[TB2_BYRCD].setEnabled(false);
		tblDATA.cmpEDITR[TB2_CNTNO].setEnabled(false);
		tblDATA.cmpEDITR[TB2_PRDDS].setEnabled(false);
		tblDATA.cmpEDITR[TB2_INVQT].setEnabled(false);
		tblDATA.cmpEDITR[TB2_LADNO].setEnabled(false);
		tblDATA.cmpEDITR[TB2_SALTP].setEnabled(false);
		tblDATA.cmpEDITR[TB2_MKTTP].setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					String L_strQPRCD="",L_strDATA="";
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
						
						if(tblQPRCD.isEditing())
							tblQPRCD.getCellEditor().stopCellEditing();
						tblQPRCD.setRowSelectionInterval(0,0);
						tblQPRCD.setColumnSelectionInterval(0,0);
			
						M_strSQLQRY = "SELECT QS_QPRCD,QS_QPRDS,QS_UOMCD,QS_TSMCD FROM CO_CDTRN,CO_QSMST WHERE CMT_CGMTP = 'RPT'"
						+" AND CMT_CGSTP = 'QCXXEXC' AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD = QS_QPRCD AND QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
						M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							int i = 0;							
							while((M_rstRSSET.next()) && (i < intROWCT))
							{
								tblQPRCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),""),i,TB1_QPRCD);
								tblQPRCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_QPRDS"),""),i,TB1_QPRDS);
								tblQPRCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""),i,TB1_UOMCD);
								tblQPRCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_TSMCD"),""),i,TB1_TSMCD);
								if(i == 299)								
									setMSG("More than ",'E');								
								i++;							
							}
							intSLROW = i;
							M_rstRSSET.close();
						}
					}					
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == txtTODAT)
			{
				String L_strFMDAT="",L_strTODAT="";
				if(txtFMDAT.getText().trim().length() != 10)
				{
					setMSG("Please Enter From Date as Data Range is Compulsory..",'E');
					txtFMDAT.requestFocus();
					return;
				}
				else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("From Date should not be grater than current Date..",'E');
					txtFMDAT.requestFocus();
					return;
				}					
				if(txtTODAT.getText().trim().length() != 10)
				{
					setMSG("Please Enter From Date as Data Range is Compulsory..",'E');
					txtTODAT.requestFocus();
					return;
				}							
				else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("To Date should not be grater than current Date Date..",'E');
					txtTODAT.requestFocus();
					return;
				}
			
				L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
				L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY = "Select IVT_INVNO,IVT_INDNO,IVT_INVQT,IVT_CNTDS,IVT_LCNUM,IVT_LCDAT,IVT_LADNO,IVT_SALTP,IVT_MKTTP,IVT_PRDCD,PR_PRDDS,PT_PRTNM from MR_IVTRN,CO_PTMST,CO_PRMST"
					+" where isnull(IVT_STSFL,'')<>'X' AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "
					+" AND CONVERT(varchar,IVT_LODDT,101) between '"+ L_strFMDAT +"' AND '"+ L_strTODAT +"'"
					+" AND IVT_SALTP = '"+ M_strSBSCD.substring(2,4) +"'"
					+" AND PT_PRTTP = 'C'"
					+" AND isnull(PR_STSFL,'')<>'X'"
					+" AND IVT_BYRCD = PT_PRTCD"
					+" AND IVT_PRDCD = PR_PRDCD";					
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET!= null)
				{
					//TB2_INDNO TB2_INVNO TB2_BYRCD TB2_CNTNO TB2_LCNUM TB2_LCDAT TB2_CRTOR 
					int i = 0;
					String L_strTEMP="";
					java.sql.Date L_tmpDATE;
					tblDATA.clrTABLE();
					hstPRDDS.clear();
					while(M_rstRSSET.next())
					{
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),i,TB2_INDNO);
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),i,TB2_INVNO);
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),i,TB2_BYRCD);
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),""),i,TB2_CNTNO);
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_LCNUM"),"");
						tblDATA.setValueAt(L_strTEMP,i,TB2_LCNUM);
												
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");		
						tblDATA.setValueAt(L_strTEMP,i,TB2_PRDDS);
						
						hstPRDDS.put(nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),""),L_strTEMP);
						
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),i,TB2_INVQT);
																		
						L_tmpDATE = M_rstRSSET.getDate("IVT_LCDAT");
						if(L_tmpDATE != null)
							L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
						else 
							L_strTEMP = "";
						tblDATA.setValueAt(L_strTEMP,i,TB2_LCDAT);
						
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),""),i,TB2_LADNO);
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_SALTP"),""),i,TB2_SALTP);
						tblDATA.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_MKTTP"),""),i,TB2_MKTTP);
						i++;
					}
					M_rstRSSET.close();
				}
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"ActionPerformed");
		}
	}	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == txtFMDAT)			
			setMSG("Enter From Date..",'N');			
		else if(M_objSOURC == txtTODAT)			
			setMSG("Enter To Date..",'N');		
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{					
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					txtTODAT.requestFocus();
			}
			else if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
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
				strFILNM = cl_dat.M_strREPSTR_pbst+"mr_rpexd.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mr_rpexd.doc";

			getDATA();
			
		/*	if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
				return;
			}*/
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Export Certificate"," ");
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
	 * Method to validate the data before execuation of the SQL Quary.
	 */
	boolean vldDATA()
	{
		try
		{
			if(tblDATA.isEditing())
				tblDATA.getCellEditor().stopCellEditing();
			tblDATA.setRowSelectionInterval(0,0);
			tblDATA.setColumnSelectionInterval(0,0);
			
			setMSG("",'N');
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("From Date should not be grater than current Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("To Date should not be grater than current Date Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
				return false;
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
			for(int i=0;i<intROWCT-1;i++)
			{
				if((tblDATA.getValueAt(i,TB2_CRTOR).toString().trim().toString().equals("true")) && (tblDATA.getValueAt(i,TB2_LCNUM).toString().trim().length() == 0))
				{
					setMSG("Please Enter LC Number to generate the Certificate of Origin..",'E');
					return false;
				}
				if(tblDATA.getValueAt(i,TB2_CHKFL).toString().trim().toString().equals("true"))
				{
					if(tblDATA.getValueAt(i,TB2_LCNUM).toString().trim().toString().equals(""))
					{
						setMSG("Please Enter LC Numbar to update..",'E');
						tblDATA.editCellAt(tblDATA.getSelectedRow(),TB2_LCNUM);
						tblDATA.cmpEDITR[TB2_LCNUM].requestFocus();
						return false;
					}
					if(tblDATA.getValueAt(i,TB2_LCDAT).toString().trim().toString().equals(""))
					{
						setMSG("Please Enter LC Date to update..",'E');
						tblDATA.editCellAt(tblDATA.getSelectedRow(),TB2_LCDAT);
						tblDATA.cmpEDITR[TB2_LCDAT].requestFocus();
						return false;
					}
				}
			}			
			int L_intTEMP = 3;
			for(int j=0;j<intROWCT1;j++)
			{				
				if(tblQPRCD.getValueAt(j,TB1_CHKFL).toString().trim().toString().equals("true"))
					L_intTEMP++;
			}
			arrQPRDS = new String[L_intTEMP];
			arrTSMCD = new String[L_intTEMP];
			arrUOMCD = new String[L_intTEMP];			
			arrQPRDS[0] = "ITEMS";
			arrUOMCD[0] = "UNITS";
			arrTSMCD[0] = "TEST METHOD";										
			
			int k = 0;
			boolean flgQPRCD = false;
			for(int j=0;j<intROWCT1;j++)
			{
				if(tblQPRCD.getValueAt(j,TB1_CHKFL).toString().trim().toString().equals("true"))
				{
					flgQPRCD = true;
					arrQPRDS[k+3] = tblQPRCD.getValueAt(j,TB1_QPRDS).toString().trim().toString();
					
					arrUOMCD[k+3] = tblQPRCD.getValueAt(j,TB1_UOMCD).toString().trim().toString();
					
					arrTSMCD[k+3] = tblQPRCD.getValueAt(j,TB1_TSMCD).toString().trim().toString();
					k++;
				}				
			}
			if(flgQPRCD == false)
			{
				setMSG("No Quality Parameter is selected..",'E');
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA()
	{
		if(!vldDATA())
			return;		
		try
		{
			// updating LCDATE& LCnulber if entered
			//TB2_INDNO TB2_INVNO TB2_BYRCD TB2_CNTNO TB2_LCNUM TB2_LCDAT TB2_CRTOR 			
			String L_strLCDAT="",L_strLCNUM="";
			cl_dat.M_flgLCUPD_pbst = true;	
			for(int i=0;i<intROWCT;i++)
			{
				if(tblDATA.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
		   		{
					L_strLCDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDATA.getValueAt(i,TB2_LCDAT).toString().trim()));
					L_strLCNUM = tblDATA.getValueAt(i,TB2_LCNUM).toString().trim();
					
					M_strSQLQRY = "Update MR_IVTRN set IVT_LCDAT = '"+ L_strLCDAT +"',"
					+"IVT_LCNUM = '"+ L_strLCNUM +"'"
					+" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INDNO = '"+ tblDATA.getValueAt(i,TB2_INDNO).toString().trim() +"'"
					+" AND IVT_INVNO = '"+ tblDATA.getValueAt(i,TB2_INVNO).toString().trim() +"'"
					+" AND IVT_CNTDS = '"+ tblDATA.getValueAt(i,TB2_CNTNO).toString().trim() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					if(cl_dat.exeDBCMT("exeSAVE"))					
						setMSG(" Data Modified Successfully..",'N'); 
					else
						setMSG("Error in Modifing LC Number & LC Date..",'E'); 					
				}
			}
			
			intRECCT = 0; 
			String L_strINDNO="";
			String L_strINVNO="";
			String L_strCNTDS="";
			double L_dblQTY = 0;
			setCursor(cl_dat.M_curWTSTS_pbst);
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Progress.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Export Certificate</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			setCursor(cl_dat.M_curWTSTS_pbst);
			//certificate of Origin				
			for(int i=0;i<intROWCT;i++)//
			{
				L_strINDNO = tblDATA.getValueAt(i,TB2_INDNO).toString().trim();
				L_strINVNO = tblDATA.getValueAt(i,TB2_INVNO).toString().trim();
				L_strCNTDS = tblDATA.getValueAt(i,TB2_CNTNO).toString().trim();
				//if(tblDATA.getValueAt(i,TB2_LCNUM).toString().trim().length()>0)
				if(tblDATA.getValueAt(i,TB2_CRTOR).toString().equals("true"))
		   		{
					// Report 1
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</STYLE><PRE style =\" font-size : 12 pt \">");
					dosREPORT.writeBytes("\n\n\n\n\n\n\n"+padSTRING('L',"Date : "+cl_dat.M_strLOGDT_pbst,75)+"\n\n");					
					if(M_rdbHTML.isSelected())
					{			
						dosREPORT.writeBytes("</STYLE>");
						dosREPORT.writeBytes("<PRE style =\" font-size : 15 pt \">");
						dosREPORT.writeBytes("\n\n<B>"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",45)+"\n\n");
						dosREPORT.writeBytes("</STYLE>");
						dosREPORT.writeBytes("<PRE style =\" font-size : 13 pt \">");
						dosREPORT.writeBytes(padSTRING('L',"CERTIFICATE OF ORIGIN",47)+"\n\n");
						dosREPORT.writeBytes("</B></STYLE>");
						dosREPORT.writeBytes("<PRE style =\" font-size : 12 pt \">");
					}
					else 
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						{
							prnFMTCHR(dosREPORT,M_strENH);					
							dosREPORT.writeBytes("\n\n"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",34)+"\n\n");
							prnFMTCHR(dosREPORT,M_strNOENH);
							prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes(padSTRING('L',"CERTIFICATE OF ORIGIN",50)+"\n");
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						}
						else
						{
							dosREPORT.writeBytes("\n\n"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",54)+"\n\n");
							dosREPORT.writeBytes(padSTRING('L',"CERTIFICATE OF ORIGIN",50)+"\n");
						}
					}					
					dosREPORT.writeBytes("\n\n\n"+"    This is to certify that the goods of following particulars are being ");
					dosREPORT.writeBytes("\n\n"+"    supplied to M/S "+tblDATA.getValueAt(i,TB2_BYRCD).toString().trim()+".");
					dosREPORT.writeBytes("\n\n"+"    They are manufactured by SUPREME PETROCHEM LTD. and are of indian origin."+"\n\n\n");
					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))			
					    prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("\n\n"+"    LC No.       : "+ tblDATA.getValueAt(i,TB2_LCNUM).toString().trim());
					dosREPORT.writeBytes("\n\n"+"    DTD          : "+tblDATA.getValueAt(i,TB2_LCDAT).toString().trim());					
					dosREPORT.writeBytes("\n\n"+"    GRADE        : "+tblDATA.getValueAt(i,TB2_PRDDS).toString().trim());
					dosREPORT.writeBytes("\n\n"+"    CONTAINER NO : "+tblDATA.getValueAt(i,TB2_CNTNO).toString().trim());
					dosREPORT.writeBytes("\n\n"+"    QUANTITY     : "+tblDATA.getValueAt(i,TB2_INVQT).toString().trim()+" MT");
										
					//dosREPORT.writeBytes("\n\n\n\n"+padSTRING('L',"For SUPREME PETROCHEM LTD.",75));
					//dosREPORT.writeBytes("\n\n\n\n"+padSTRING('L',"AUTHORIZED SIGNATORY",70));
					dosREPORT.writeBytes("\n\n\n\n\n\n"+"    For SUPREME PETROCHEM LTD.");
					dosREPORT.writeBytes("\n\n\n\n\n"+"    AUTHORIZED SIGNATORY");
					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))			
					    prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strEJT);				
					}			
					if(M_rdbHTML.isSelected())			
						dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");  
				}
				if(tblDATA.getValueAt(i,TB2_CRTAN).toString().equals("true"))					
				{
					M_strSQLQRY = "Select distinct IST_LOTNO,IST_PRDCD,IST_RCLNO from MR_IVTRN,FG_ISTRN "//count(IST_LOTNO)LOTNO
					+" where IVT_CMPCD=IST_CMPCD and IVT_MKTTP = IST_MKTTP"
					+" AND IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP"
					+" AND IVT_LADNO = IST_ISSNO AND IVT_INDNO = '"+ L_strINDNO +"'"
					+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO = '"+ L_strINVNO +"'"
					+" AND IVT_CNTDS = '"+ L_strCNTDS +"'";
					M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					intLOTCT = 0;
					hstPRDCD.clear();
					if(M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
						{
							hstPRDCD.put(nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),"")+nvlSTRVL(M_rstRSSET.getString("IST_RCLNO"),""),nvlSTRVL(M_rstRSSET.getString("IST_PRDCD"),""));
							intLOTCT++;
						}
						M_rstRSSET.close();
					}
					if(intLOTCT >=5)
					{
						dosREPORT.close();
						fosREPORT.close();		
						setMSG("For Invoice No "+ L_strINVNO +"  Lot Numbers are more than 4..",'E');
						setCursor(cl_dat.M_curDFSTS_pbst);
						return;
					}
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</STYLE><PRE style =\" font-size : 12 pt \">");
					dosREPORT.writeBytes("\n\n\n\n\n\n\n"+padSTRING('L',"Date : "+cl_dat.M_strLOGDT_pbst,75)+"\n\n");					
					if(M_rdbHTML.isSelected())
					{			
						dosREPORT.writeBytes("</STYLE>");
						dosREPORT.writeBytes("<PRE style =\" font-size : 15 pt \">");
						dosREPORT.writeBytes("\n\n<B>"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",45)+"\n\n");
						dosREPORT.writeBytes("</STYLE>");
						dosREPORT.writeBytes("<PRE style =\" font-size : 13 pt \">");
						dosREPORT.writeBytes(padSTRING('L',"CERTIFICATE OF ANALYSIS",47)+"\n\n");
						dosREPORT.writeBytes("</B></STYLE>");
						dosREPORT.writeBytes("<PRE style =\" font-size : 12 pt \">");
					}
					else 
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						{
							prnFMTCHR(dosREPORT,M_strENH);					
							dosREPORT.writeBytes("\n\n"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",34)+"\n\n");
							prnFMTCHR(dosREPORT,M_strNOENH);
							prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes(padSTRING('L',"CERTIFICATE OF ANALYSIS",50)+"\n");
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						}
						else
						{
							dosREPORT.writeBytes("\n\n"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",54)+"\n\n");
							dosREPORT.writeBytes(padSTRING('L',"CERTIFICATE OF ANALYSIS",50)+"\n");
						}
					}					
					dosREPORT.writeBytes("\n\n\n"+"      We hereby certify that the undermentioned Lot was duly inspected");
					dosREPORT.writeBytes("\n\n"+"      and passed by our Quality Control Department.");
					
					dosREPORT.writeBytes("\n\n\n"+"      Container No : "+tblDATA.getValueAt(i,TB2_CNTNO).toString().trim()+"\n");
					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					    prnFMTCHR(dosREPORT,M_strCPI12);
					if(M_rdbHTML.isSelected())
					    dosREPORT.writeBytes("</PRE><PRE style =\" font-size : 9 pt \">");
					
					//to generate the arrays dynamically.
					String L_strCOLST = "PS_LOTNO,PS_RCLNO";
					intQPRCT  = 0;
					for(int j=0;j<intROWCT1;j++)
					{
						if(tblQPRCD.getValueAt(j,TB1_CHKFL).toString().trim().toString().equals("true"))		   				
						{
							L_strCOLST += ",PS_"+tblQPRCD.getValueAt(j,TB1_QPRCD).toString().trim()+"VL";						
							intQPRCT ++;
						}
					}										   
					stbDOTLN.delete(0,stbDOTLN.length());
					stbDOTLN.append("      ");
					for(int l=0;l<(10*intLOTCT)+61;l++)
						stbDOTLN.append("-");					
					intQPRCT += 3;
					String arrTEMP[][] = new String[intLOTCT][intQPRCT];//j+2 total Quality Parameters Selected a = number of Lots Found 																			
						
					M_strSQLQRY = "Select "+ L_strCOLST +" from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PS_STSFL ,'')<>'X'"
					+" AND PS_TSTTP = '0103'"
					+" AND PS_PRDTP + PS_LOTNO  + PS_RCLNO in(Select IST_PRDTP + IST_LOTNO + IST_RCLNO from MR_IVTRN,FG_ISTRN "
					+" where IVT_CMPCD=IST_CMPCD AND IVT_MKTTP = IST_MKTTP AND IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP"
					+" AND IVT_LADNO = IST_ISSNO AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INDNO = '"+ L_strINDNO +"'"
					+" AND IVT_INVNO = '"+ L_strINVNO +"'"
					+" AND IVT_CNTDS = '"+ L_strCNTDS +"')";
					M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);					
										
					String L_strDATA = "";
					String str1="";
					intCOLNO = 0;
					if(M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
						{
							stkQPRLS = new StringTokenizer(L_strCOLST,",");
							intROWNO = 0;
							arrTEMP[intCOLNO][intROWNO] = "LOT NO.";
							intROWNO++;
							while(stkQPRLS.hasMoreTokens())
							{
								str1 = stkQPRLS.nextToken();
								if(str1.equals("PS_RCLNO"))
								{
									L_strDATA += nvlSTRVL(M_rstRSSET.getString(str1),"-");
									if(hstPRDCD.containsKey(L_strDATA))
									{
										L_strDATA = hstPRDCD.get(L_strDATA).toString();										
										if(hstPRDDS.containsKey(L_strDATA))
											L_strDATA = hstPRDDS.get(L_strDATA).toString();
										else 
											L_strDATA ="X";
									}
									else 
										L_strDATA = "-";
								}
								else
									L_strDATA = nvlSTRVL(M_rstRSSET.getString(str1),"-");																																	
								arrTEMP[intCOLNO][intROWNO] = L_strDATA;
								intROWNO++;
							}
							intCOLNO++;
						}						
					}					
					dosREPORT.writeBytes("\n\n"+stbDOTLN.toString());
					for(int j=0;j<intQPRCT;j++)
					{
						str1 = arrQPRDS[j];
						if(str1 == null)
							str1="";
						if(j>3)
							dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("\n      "+ padSTRING('R',str1,32));						
						
						str1 = arrTSMCD[j];
						if(str1 == null)
							str1="";
						dosREPORT.writeBytes(padSTRING('R',str1,15));
												
						str1 = arrUOMCD[j];
						if(str1 == null)
							str1="";
						dosREPORT.writeBytes(padSTRING('R',str1,12));
						
						for(int k=0;k<intLOTCT;k++)
						{
							str1 = arrTEMP[k][j];
							if(str1 == null)
								str1="";
							dosREPORT.writeBytes(padSTRING('R',str1,10));
						}
						if(j == 2)
							dosREPORT.writeBytes("\n"+stbDOTLN.toString()+"\n");
					}
					dosREPORT.writeBytes("\n\n"+stbDOTLN.toString());
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strNOCPI17);
					    prnFMTCHR(dosREPORT,M_strCPI10);
					}
					if(M_rdbHTML.isSelected())
					    dosREPORT.writeBytes("</PRE><PRE style =\" font-size : 10 pt \">");
										
					dosREPORT.writeBytes("\n\n\n\n\n\n"+"    For SUPREME PETROCHEM LTD.");
					dosREPORT.writeBytes("\n\n\n\n\n"+"    AUTHORIZED SIGNATORY");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strEJT);
					}			
					if(M_rdbHTML.isSelected())			
						dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");  
				}
			}
			setMSG("Report completed.. ",'N');
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
	}
}