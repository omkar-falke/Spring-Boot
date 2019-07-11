/*
System Name		: 
Program Name	: 
Author			: Mr. S.R.Mehesare
Modified Date	: 30/12/2005
Documented Date	: 
Version			: MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.awt.event.KeyEvent;import java.sql.ResultSet;import javax.swing.JPanel;
import java.io.DataOutputStream;import java.io.FileOutputStream;import javax.swing.JTable;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;
import java.util.Hashtable;import java.util.StringTokenizer;
import javax.swing.DefaultCellEditor;
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

<B>Validations & Other Information:</B>    

</I> */

class co_rpcst extends cl_rbase
{									/** JConboBox to select & to display Complaint Type. */
	private JComboBox cmbCMPTP;		/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtFMDAT;	/** JTextField to display to enter To date to specify date range.*/
	private JTextField txtTODAT;	/** JTextField to display to enter Status Flag.*/
	private JTextField txtSTSFL;	/** JTextField to display to enter Complaint Category.*/
	private JTextField txtCMPCT;	/** JTextField to display to enter Complaint Status Description.*/
	private JTextField txtSTSDS;	/** JTextField to display to enter Complaint Category Description.*/
	private JTextField txtCMPDS;
	private JTextField txtZONCD;
	private JTextField txtZONDS;
	private JTextField txtPRDCD;
	private JTextField txtPRDDS;
	private JTextField txtPRTCD;
	private JTextField txtPRTDS;
	private JPanel pnlTABLE;
	private JComboBox cmbFNTSZ;
	private JComboBox cmbPAGSZ;
	private cl_JTable tblDATA;
	
	/** String variable for Store Type.*/
	private String strSTRTP;		/** String variable for Store Type Description.*/
	private String strSTRNM;		/** String variable for generated Report File Name.*/
	private String strFILNM;		/** Integer Variable to count the Number of records fetched to block the Report if No data Found.*/
	private int intRECCT;			/** DataOutputStream to generate hld the stream of data.*/
	private DataOutputStream dosREPORT;/** FileOutputStream to generate the Report file form stream of data.*/
	private FileOutputStream fosREPORT;/** String variable to print Dotted Line in the Report.*/	
	//private String stbDOTLN.toString() = "------------------------------------------------------------------------------------------------";
	
	private String strFMDAT;
	private String strTODAT;
	//private Hashtable hstCMPTP;
	//private Hashtable hstSTSDS;
	//private Hashtable hstCMPDS;
	
	private Hashtable<String,String> hstCODCD;
	private Hashtable<String,String> hstSHRDS;
	private Hashtable<String,String> hstWIDTH;
	private Hashtable<String,String> hstZONCD;
	private Hashtable<String,String> hstPRDDS;
	private Hashtable<String,String> hstTPRNM;
	
	private final int TBL_CHKFL = 0;
	private final int TBL_COLNM = 1;
	//private final int TBL_COLWD = 2;
	//private final int TBL_COLCD = 3;
	//private final int TBL_SHRDS = 4;
	private int intNOLIN;
	private int intNOCHR;
	private String strFNTSZ = "Size 8";
	
	int intROWLN;
	String strCOLHD = "";
	String strCOLST = "";  //column list to fetch data
	private StringBuffer stbDOTLN = new StringBuffer();
	private int arrCOLWD[];	
	private String strCMPTP = "01";
	public co_rpcst()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Complaint Type"),2,3,1,1,this,'L');
			add(cmbCMPTP = new JComboBox(),2,4,1,1.2,this,'L');
		
			add(new JLabel("From Date"),3,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),3,4,1,1,this,'L');
			add(new JLabel("To Date"),3,5,1,.6,this,'R');
			add(txtTODAT = new TxtDate(),3,6,1,1,this,'L');
		
			add(new JLabel("Comp. Status"),4,3,1,1,this,'L');
			add(txtSTSFL = new TxtNumLimit(1),4,4,1,1,this,'L');
			add(txtSTSDS = new JTextField(),4,5,1,2,this,'L');
		
			add(new JLabel("Category"),5,3,1,1,this,'L');
			add(txtCMPCT = new TxtNumLimit(2),5,4,1,1,this,'L');
			add(txtCMPDS = new JTextField(),5,5,1,2,this,'L');
			
			add(new JLabel("Zone"),6,3,1,1,this,'L');
			add(txtZONCD = new TxtNumLimit(2),6,4,1,1,this,'L');
			add(txtZONDS = new JTextField(),6,5,1,2,this,'L');
			
			add(new JLabel("Product"),7,3,1,1,this,'L');
			add(txtPRDCD = new TxtNumLimit(10),7,4,1,1,this,'L');
			add(txtPRDDS = new JTextField(),7,5,1,2,this,'L');
			
			add(new JLabel("Customer"),8,3,1,1,this,'L');
			add(txtPRTCD = new TxtNumLimit(5),8,4,1,1,this,'L');
			add(txtPRTDS = new JTextField(),8,5,1,2,this,'L');
			
			add(new JLabel("Page Size"),9,3,1,1,this,'L');
			add(cmbPAGSZ = new JComboBox(),9,4,1,1.5,this,'L');			
			add(new JLabel("Font"),9,5,1,.4,this,'R');
			add(cmbFNTSZ = new JComboBox(),9,6,1,1,this,'L');						
														
			add(new JLabel("Select Collumns"),10,3,1,2,this,'L');
			
			String[] L_COLHD = {"Select","Column Name"};
      		int[] L_COLSZ = {50,215};	    				
			tblDATA = crtTBLPNL1(this,L_COLHD,30,10,4,8.4,3,L_COLSZ,new int[]{0});				
			
			cmbPAGSZ.addItem("Plain A4");
			cmbPAGSZ.addItem("Fanfold 210mm - 12in");
			//cmbPAGSZ.addItem("Fanfold 358mm - 12in");
			
			hstCODCD = new Hashtable<String,String>(); // from Description we get column name
			hstSHRDS = new Hashtable<String,String>(); // from Collumn name shrds
			hstWIDTH = new Hashtable<String,String>(); // from collumn name width
			hstZONCD = new Hashtable<String,String>(); 
			M_strSQLQRY =" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
				+" CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MR00ZON' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstZONCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}							
			tblDATA.cmpEDITR[TBL_COLNM].setEnabled(false);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
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
		if(L_flgSTAT == false)	
			return;			
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
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("CPI10");
					cmbFNTSZ.addItem("CPI12");
					cmbFNTSZ.addItem("CPI15");
					cmbFNTSZ.addItem("CPI17");
					
					setENBL(true);
					txtSTSDS.setEnabled(false);
					txtCMPDS.setEnabled(false);
					txtZONDS.setEnabled(false);
					txtPRDDS.setEnabled(false);
					txtPRTDS.setEnabled(false);
					
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
					if(tblDATA.getValueAt(1,1).toString().equals(""))
					{						
						String L_strCODDS="",L_strCODCD="";
						M_strSQLQRY = "Select CMT_NMP01,CMT_CODCD,CMT_CODDS,CMT_SHRDS,CMT_CHP01 from CO_CDTRN where"
							+" isnull(CMT_STSFL,'')<>'X'"
							+" AND CMT_CGMTP = 'RPT'"
							+" AND CMT_CGSTP = 'COXXCTR' order by CMT_NMP01";						
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							int i = 0;
							while(M_rstRSSET.next())
							{
								L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
								tblDATA.setValueAt(L_strCODDS,i++,TBL_COLNM);
								L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
								hstCODCD.put(L_strCODDS,L_strCODCD);
								hstWIDTH.put(L_strCODCD,nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),""));
								hstSHRDS.put(L_strCODCD,nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));					
							}
							M_rstRSSET.close();
						}
					}
					if(cmbCMPTP.getItemCount() == 0)
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXCMT'";
						M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);						
						String L_strCODDS="",L_strCODCD="";
						if(M_rstRSSET != null)
						{								
//								hstCMPTP = new Hashtable<String,String>();
							while(M_rstRSSET.next())
							{
								L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
								L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
//									hstCMPTP.put(L_strCODCD,L_strCODDS);
								cmbCMPTP.addItem(L_strCODCD+" "+L_strCODDS);
							}
							M_rstRSSET.close();							
						}
					}
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC ==  cl_dat.M_btnSAVE_pbst)
			{
				cl_dat.M_PAGENO = 0;
				cl_dat.M_intLINNO_pbst = 0;
			}
			else if((M_objSOURC ==  M_rdbHTML) ||(M_objSOURC ==  M_rdbTEXT))
			{
				if(M_rdbHTML.isSelected())
				{
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("Size 8");
					cmbFNTSZ.addItem("Size 9");
					cmbFNTSZ.addItem("Size 10");
				}
				else
				{
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("CPI10");
					cmbFNTSZ.addItem("CPI12");
					cmbFNTSZ.addItem("CPI15");
					cmbFNTSZ.addItem("CPI17");
				}
			}
			else  if(M_objSOURC == cmbFNTSZ)
				strFNTSZ = cmbFNTSZ.getSelectedItem().toString().trim();
			else  if(M_objSOURC == cmbCMPTP)
				strCMPTP = cmbCMPTP.getSelectedItem().toString().trim().substring(0,2);
			
			else if(M_objSOURC == txtSTSFL)
			{
				if(txtSTSFL.getText().trim().length() == 1)
				{
					M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP ='STS'"
						+" AND CMT_CGSTP = 'COXXCST'"
						+" AND CMT_CODCD = '"+ txtSTSFL.getText().trim() +"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							txtSTSDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						M_rstRSSET.close();
					}
				}
			}
			else if(M_objSOURC == txtCMPCT)
			{
				if(txtCMPCT.getText().trim().length() == 2)
				{
					M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXCCT'"
						+" AND CMT_CODCD = '"+ txtCMPCT.getText() +"'";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							txtCMPDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						M_rstRSSET.close();
					}
				}
			}
			else if(M_objSOURC == txtZONCD)
			{
				if(txtZONCD.getText().trim().length() == 2)
				{
					M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'MR00ZON'"
						+" AND CMT_CODCD = '"+ txtZONCD.getText().trim() +"'";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							txtZONDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						M_rstRSSET.close();
					}
				}
			}
			else if(M_objSOURC == txtPRDCD)
			{
				if(txtPRDCD.getText().trim().length() == 10)
				{
					if(!strCMPTP.equals("03"))
					{
						M_strSQLQRY =" SELECT PR_PRDDS PRDDS from CO_PRMST where"
							+" isnull(PR_STSFL,'')<>'X'"
							+" AND PR_PRDCD = '"+txtPRDCD.getText().trim()+"'";						
					}
					else 
					{
						M_strSQLQRY =" SELECT CT_MATDS PRDDS from CO_CTMST where"
							+" isnull(CT_STSFL,'')<>'X'"
							+" AND CT_MATCD = '"+txtPRDCD.getText().trim()+"'";						
					}
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							txtPRDDS.setText(nvlSTRVL(M_rstRSSET.getString("PRDDS"),""));
						M_rstRSSET.close();
					}
				}
			}
			else if(M_objSOURC == txtPRTCD)
			{
				if(txtPRDCD.getText().trim().length() == 5)
				{
					M_strSQLQRY =" SELECT PR_PRDDS from CO_PTMST where isnull(PT_STSFL,'')<>'X'"
						+ " AND PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'";
					if(strCMPTP.equals("03"))
						M_strSQLQRY += " AND PT_PRTTP ='S'";
					else
						M_strSQLQRY += " AND PT_PRTTP ='C'";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							txtPRTDS.setText(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
						M_rstRSSET.close();
					}
				}
			}
		}
		catch(Exception L_EX)
		{
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
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				if(txtFMDAT.getText().trim().length() == 10)
					strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
				else 
					strFMDAT = "";
				if(txtTODAT.getText().trim().length() == 10)
					strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
				else 
					strTODAT = "";
				if(M_objSOURC == txtSTSFL)
				{
					M_strHLPFLD = "txtSTSFL";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY = "Select distinct CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP ='STS'"
						+" AND CMT_CGSTP = 'COXXCST' order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Status Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtCMPCT)
				{
					M_strHLPFLD = "txtCMPCT";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXCCT'"
						+" order by CMT_CODCD";
						/*+" AND CMT_CODCD = CM_CMPCT";
					if((txtTODAT.getText().trim().length() == 10) && (txtFMDAT.getText().trim().length() == 10))
						M_strSQLQRY += " AND CM_REPDT between '"+ strFMDAT +"' AND '"+ strTODAT +"'";
					else if(txtFMDAT.getText().trim().length() == 10)
						M_strSQLQRY += " AND CM_REPDT >='"+ strFMDAT +"'";
					else if(txtTODAT.getText().trim().length() == 10)
						M_strSQLQRY += " AND CM_REPDT <='"+ strTODAT +"'";
					if(txtSTSFL.getText().trim().length() == 1)
						M_strSQLQRY += " AND CM_STSFL ='"+ txtSTSFL.getText().trim()+"'";
					if(txtCMPCT.getText().trim().length() > 0)
						M_strSQLQRY += " AND CM_CMPCT like '"+ txtCMPCT.getText().trim()+"%'";
					*/
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Category Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtZONCD)
				{
					M_strHLPFLD = "txtZONCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY = "Select distinct CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'MR00ZON' order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Zone Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtPRDCD)
				{
					M_strHLPFLD = "txtPRDCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					if(!strCMPTP.equals("03"))
					{
						M_strSQLQRY =" SELECT PR_PRDCD,PR_PRDDS from CO_PRMST where"
						+" isnull(PR_STSFL,'')<>'X'";
						if(txtPRDCD.getText().length() > 0)
							M_strSQLQRY += " AND PR_PRDCD like '"+txtPRDCD.getText().trim()+"%'";
						M_strSQLQRY += " order by PR_PRDCD";
					}
					else // Supplier Complaint
					{
						M_strSQLQRY =" SELECT distinct CT_MATCD,CT_MATDS from CO_CTMST where"
							+" isnull(CT_STSFL,'')<>'X'";							
						if(txtPRDCD.getText().length() > 0)
							M_strSQLQRY += " AND CT_MATCD like '"+txtPRDCD.getText().trim()+"%'";
						M_strSQLQRY += " order by CT_MATCD";
					}
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtPRTCD)
				{
					M_strHLPFLD = "txtPRTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT distinct PT_PRTCD,PT_PRTNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
					if(strCMPTP.equals("03"))
						M_strSQLQRY += " AND PT_PRTTP ='S'";
					else
						M_strSQLQRY += " AND PT_PRTTP ='C'";
					if(txtPRTCD.getText().length() > 0)
						M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText().trim().toUpperCase()+"%'";
					M_strSQLQRY += " order by PT_PRTCD ";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
		}
		else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbCMPTP)
			{
				txtFMDAT.requestFocus();
				setMSG("Please Enter From Date to generate the Report..",'N');
			}
			if(M_objSOURC == txtFMDAT)
			{			
				txtTODAT.requestFocus();
				setMSG("Please Enter To Date to generate the Report..",'N');
			}
			else if(M_objSOURC == txtTODAT)
			{			
				txtSTSFL.requestFocus();
				setMSG("Please Enter Status Flag or Press F1 to select from List..",'N');
			}
			else if(M_objSOURC == txtSTSFL)
			{			
				txtCMPCT.requestFocus();
				setMSG("Please Enter Complaint Category or Press F1 to select from List..",'N');
			}
			else if(M_objSOURC == txtCMPCT)
			{
				txtZONCD.requestFocus();
				setMSG("Please Enter Zone Code or Press F1 to select from List..",'N');
			}
			else if(M_objSOURC == txtZONCD)
			{
				txtPRDCD.requestFocus();
				setMSG("Please Enter Product Code or Press F1 to select from List..",'N');
			}
			else if(M_objSOURC == txtPRDCD)
			{
				setMSG("Please Enter Party Code or Press F1 to select from List..",'N');
				txtPRTCD.requestFocus();
			}
			else if(M_objSOURC == txtPRTCD)
				cl_dat.M_btnSAVE_pbst.requestFocus();				
		}	
	}
	/**
	 * Super class Method overrrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{
		try
		{
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtSTSFL"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtSTSFL.setText(cl_dat.M_strHLPSTR_pbst);
				txtSTSDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
			else if(M_strHLPFLD.equals("txtCMPCT"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtCMPCT.setText(cl_dat.M_strHLPSTR_pbst);
				txtCMPDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
			else if(M_strHLPFLD.equals("txtZONCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtZONCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtZONDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
			else if(M_strHLPFLD.equals("txtPRDCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
			else if(M_strHLPFLD.equals("txtPRTCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
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
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcst.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcst.doc";
			
			getDATA();
			
			/*if(intRECCT == 0)
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Receipt Summary"," ");
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
			if(cmbPAGSZ.getSelectedItem().equals("Plain A4"))
			{					
				if(strFNTSZ.equals("CPI10"))
				{					
					intNOLIN = 65;
					intNOCHR = 70;
				}	
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 85;
				}				
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 65;
					intNOCHR = 110;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 65;
					intNOCHR = 125;
				}	
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 110;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 100;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 100;
				}
			}				
			if(cmbPAGSZ.getSelectedItem().equals("Fanfold 210mm - 12in"))
			{
				if(strFNTSZ.equals("CPI10"))
				{
					intNOLIN = 65;
					intNOCHR = 85;
				}	
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 105;
				}				
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 70;
					intNOCHR = 140;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 70;
					intNOCHR = 160;
				}	
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 130;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 120;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 110;
				}
			}						
			int L_intCOUNT = 0;
			if(stbDOTLN != null)
				stbDOTLN.delete(0,stbDOTLN.length());								
			intROWLN = 0;
			String L_strSHRDS = "";
			String L_strCODDS = "";
			String L_strCODCD = "";
			int L_intCOLWD = 0;
			strCOLST = "";
			arrCOLWD = new int[30];
			strCOLHD = "";
			char L_strALN = 'R';
			for(int i=0; i< tblDATA.getRowCount();i++)
			{
				if(tblDATA.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{				
					L_strCODDS = tblDATA.getValueAt(i,TBL_COLNM).toString();					
					if(hstCODCD.containsKey(L_strCODDS))
						L_strCODCD = hstCODCD.get(L_strCODDS).toString();
					if(hstWIDTH.containsKey(L_strCODCD))
						L_intCOLWD = Integer.valueOf(hstWIDTH.get(L_strCODCD).toString()).intValue();
					if(hstSHRDS.containsKey(L_strCODCD))
						L_strSHRDS = hstSHRDS.get(L_strCODCD).toString();
										
					arrCOLWD[L_intCOUNT] = L_intCOLWD;		// array of integers for column width
					if(!strCOLST.equals(""))
						strCOLST += ",";
					strCOLST += "CM_"+L_strCODCD;  // list of collumn for query 
					intROWLN += L_intCOLWD + 1;
					char L_chrALIGN = 'R';
					if(L_strCODCD.substring(3).equals("QT"))
						L_chrALIGN = 'L';
					else 
						L_chrALIGN = 'R';				
				
					if((L_strALN == 'L') && (L_chrALIGN == 'R'))					
						strCOLHD +="  ";
					if(L_chrALIGN == 'L')
						L_strALN = 'L';
					else 
						L_strALN = 'R';					
						
					strCOLHD += padSTRING(L_chrALIGN,L_strSHRDS,L_intCOLWD);// Column heading for Report					
					L_intCOUNT++;
				}
			}		
			if(intROWLN < 65)
				intROWLN = 65;
			for(int i=0;i<intROWLN;i++)
				stbDOTLN.append("-");
			if(L_intCOUNT==0)
			{
				setMSG("No column is selected, Please select column/s..",'E');
				return false;
			}
						
			if(intNOCHR <intROWLN)
			{
				setMSG("Report cannot be fit into "+cmbPAGSZ.getSelectedItem().toString() +" page Please Select Larger Size Paper..",'E');
				return false;
			}			
			if(txtFMDAT.getText().trim().length() == 10)
			{
				if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("From Date should not be grater than Current Date..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
			}
			if(txtTODAT.getText().trim().length() == 10)
			{
				if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("To Date should not be grater than Current Date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
			}
			if((txtFMDAT.getText().trim().length() == 10) && (txtTODAT.getText().trim().length() == 10))
			{
				if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
				{
					setMSG("To Date should be grater than or equal to From Date..",'E');
					txtTODAT.requestFocus();
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
		try
		{
			intRECCT = 0; 
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);												
				if(strFNTSZ.equals("CPI10"))				
					prnFMTCHR(dosREPORT,M_strCPI10);																
				else if(strFNTSZ.equals("CPI12"))
					prnFMTCHR(dosREPORT,M_strCPI12);					
			    else if(strFNTSZ.equals("CPI15"))
				{
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI17);
				}
				else
				{
					prnFMTCHR(dosREPORT,M_strCPI12);
					prnFMTCHR(dosREPORT,M_strCPI17);
				}																
			}
			if(M_rdbHTML.isSelected())
			{
				strFNTSZ = strFNTSZ.substring(strFNTSZ.length()-2).trim();									
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Complaint Analysis</title> </HEAD> <BODY><P><PRE style =\" font-size :"+ strFNTSZ +"pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			if(txtFMDAT.getText().trim().length() == 10)
				strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			if(txtTODAT.getText().trim().length() == 10)
				strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
				
			prnHEADER();
			hstTPRNM = new Hashtable<String,String>();
			hstPRDDS = new Hashtable<String,String>();
				
			if((strCOLST.indexOf("CM_TPRCD")>=0) || (strCOLST.indexOf("CM_PRDCD")>=0))
			{	
				String L_strSQLQRY = "";
				String L_strSQLQRY1 = "";
				if((txtTODAT.getText().trim().length() == 10) && (txtFMDAT.getText().trim().length() == 10))
					L_strSQLQRY1 += " AND CM_REPDT between '"+ strFMDAT +"' AND '"+ strTODAT +"'";
				else if(txtFMDAT.getText().trim().length() == 10)
					L_strSQLQRY1 += " AND CM_REPDT >='"+ strFMDAT +"'";
				else if(txtTODAT.getText().trim().length() == 10)
					L_strSQLQRY1 += " AND CM_REPDT <='"+ strTODAT +"'";
				if(txtSTSFL.getText().trim().length() == 1)
					L_strSQLQRY1 += " AND CM_STSFL ='"+ txtSTSFL.getText().trim()+"'";
				if(txtCMPCT.getText().trim().length() == 2)
					L_strSQLQRY1 += " AND CM_CMPCT ='"+ txtCMPCT.getText().trim()+"'";			
				if(txtZONCD.getText().trim().length() == 2)
					L_strSQLQRY1 += " AND CM_ZONCD ='"+ txtZONCD.getText().trim()+"'";
				if(txtPRDCD.getText().trim().length() == 10)
					L_strSQLQRY1 += " AND CM_PRDCD ='"+ txtPRDCD.getText().trim()+"'";
				if(txtPRTCD.getText().trim().length() == 5)
					L_strSQLQRY1 += " AND CM_PRTCD ='"+ txtPRTCD.getText().trim()+"'";
				
				
				if(strCOLST.indexOf("CM_TPRCD") >= 0)
				{
					L_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,CO_CMMST where isnull(PT_STSFL,'')<>'X'"
					+ L_strSQLQRY1
					+ " AND PT_PRTCD = CM_TPRCD AND PT_PRTTP = 'T'";					
//					System.out.println(L_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
							hstTPRNM.put(nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),""),nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
						M_rstRSSET.close();
					}					
				}
				if(strCOLST.indexOf("CM_PRDCD")>=0)
				{
					if(!strCMPTP.equals("03"))
					{
						L_strSQLQRY = "select distinct PR_PRDCD,PR_PRDDS from CO_PRMST,CO_CMMST where isnull(PR_STSFL,'')<>'X'"
						+ L_strSQLQRY1
						+" AND PR_PRDCD = CM_PRDCD";
	//					System.out.println(L_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(M_rstRSSET != null)
						{
							while(M_rstRSSET.next())
								hstPRDDS.put(nvlSTRVL(M_rstRSSET.getString("PR_PRDCD"),""),nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
							M_rstRSSET.close();
						}
					}
					else
					{
						M_strSQLQRY =" SELECT CT_MATCD,CT_MATDS from CO_CTMST,CO_CMMST where"
							+" isnull(CT_STSFL,'')<>'X'"
							+ L_strSQLQRY1
							+" AND CT_MATCD = CM_PRDCD";	
						M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(M_rstRSSET != null)
						{
							while(M_rstRSSET.next())
								hstPRDDS.put(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
							M_rstRSSET.close();
						}
					
					}
				}								
			}			
			M_strSQLQRY = "SELECT "+ strCOLST +",CM_PRTNM from CO_CMMST where isnull(CM_STSFL,'')<>'X'";
			if((txtTODAT.getText().trim().length() == 10) && (txtFMDAT.getText().trim().length() == 10))
				M_strSQLQRY += " AND CM_REPDT between '"+ strFMDAT +"' AND '"+ strTODAT +"'";
			else if(txtFMDAT.getText().trim().length() == 10)
				M_strSQLQRY += " AND CM_REPDT >='"+ strFMDAT +"'";
			else if(txtTODAT.getText().trim().length() == 10)
				M_strSQLQRY += " AND CM_REPDT <='"+ strTODAT +"'";
			if(txtSTSFL.getText().trim().length() == 1)
				M_strSQLQRY += " AND CM_STSFL ='"+ txtSTSFL.getText().trim()+"'";
			if(txtCMPCT.getText().trim().length() == 2)
				M_strSQLQRY += " AND CM_CMPCT ='"+ txtCMPCT.getText().trim()+"'";			
			if(txtZONCD.getText().trim().length() == 2)
				M_strSQLQRY += " AND CM_ZONCD ='"+ txtZONCD.getText().trim()+"'";
			if(txtPRDCD.getText().trim().length() == 10)
				M_strSQLQRY += " AND CM_PRDCD ='"+ txtPRDCD.getText().trim()+"'";
			if(txtPRTCD.getText().trim().length() == 5)
				M_strSQLQRY += " AND CM_PRTCD ='"+ txtPRTCD.getText().trim()+"'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET != null)
			{
				String L_strTEMP ="";
				dosREPORT.writeBytes("\n");
				StringTokenizer L_stkTEMP = new StringTokenizer(strCOLST,",");				
				int i = 0;
				while(M_rstRSSET.next())
				{
					L_stkTEMP = new StringTokenizer(strCOLST,",");
					intRECCT = 1; 
					i = 0;
					String str1="";
					char L_chrALIGN = 'R';
					char L_strALN = 'R';
					ResultSet L_rstRSSET;
					while(L_stkTEMP.hasMoreTokens())
					{
						str1 = L_stkTEMP.nextToken();
						if(str1.substring(6).equals("QT"))
							L_chrALIGN = 'L';
						else 
							L_chrALIGN = 'R';
						
						// if 'R' after 'L' the add two blank chars						
						if((L_strALN == 'L') && (L_chrALIGN == 'R'))
							dosREPORT.writeBytes("  ");
						if(L_chrALIGN == 'L')
							L_strALN = 'L';
						else 
							L_strALN = 'R';
												
						if(str1.equals("CM_ZONCD"))
						{
							L_strTEMP = M_rstRSSET.getString(str1);
							if(hstZONCD.containsKey(L_strTEMP))
								dosREPORT.writeBytes(padSTRING('R',hstZONCD.get(L_strTEMP).toString(),arrCOLWD[i]));
							else
								dosREPORT.writeBytes(padSTRING('R',"",arrCOLWD[i]));
						}
						else if(str1.equals("CM_PRDCD"))
						{
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CM_PRDCD"),"");
							if(hstPRDDS.containsKey(L_strTEMP))
								dosREPORT.writeBytes(padSTRING(L_chrALIGN,hstPRDDS.get(L_strTEMP).toString(),arrCOLWD[i]));	
							else 
								dosREPORT.writeBytes(padSTRING(L_chrALIGN,"",arrCOLWD[i]));							
						}
						else if(str1.equals("CM_TPRCD"))
						{
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CM_TPRCD"),"");
							if(hstTPRNM.containsKey(L_strTEMP))
								dosREPORT.writeBytes(padSTRING(L_chrALIGN,hstTPRNM.get(L_strTEMP).toString(),arrCOLWD[i]));
							else 
								dosREPORT.writeBytes(padSTRING(L_chrALIGN,"",arrCOLWD[i]));
						}
						else
							dosREPORT.writeBytes(padSTRING(L_chrALIGN,nvlSTRVL(M_rstRSSET.getString( str1 ),""),arrCOLWD[i]));
						i++;
					}
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+stbDOTLN.toString());					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					dosREPORT.writeBytes("\n");					
					cl_dat.M_intLINNO_pbst++;
				}
			}
			dosREPORT.writeBytes(stbDOTLN.toString());
			
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
		finally
		{
			
		}
	}
	/**
	 * Method to generate Header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{
			int L_intTEMP = 0;
			cl_dat.M_PAGENO++;
			int L_intPADLN = 0;
			dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,stbDOTLN.toString().length()-25));
			dosREPORT.writeBytes("Date      : "+cl_dat.M_strLOGDT_pbst+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Complaint Type : "+cmbCMPTP.getSelectedItem().toString().substring(3),stbDOTLN.toString().length()-25));
			dosREPORT.writeBytes("Page No   : "+cl_dat.M_PAGENO+"\n");
			cl_dat.M_intLINNO_pbst = 3;
			L_intPADLN = stbDOTLN.length() - 25;
			if((txtTODAT.getText().trim().length()==10) && (txtFMDAT.getText().trim().length()==10))
			{
				dosREPORT.writeBytes(padSTRING('R',"Date      : "+txtFMDAT.getText()+" To "+txtTODAT.getText(),L_intPADLN));
				L_intTEMP ++;				
				if(L_intPADLN == 25)
					L_intPADLN = stbDOTLN.length() - 25;
				else 
					L_intPADLN = 25;
			}
									
			if(txtSTSFL.getText().trim().length() == 1)
			{
				dosREPORT.writeBytes(padSTRING('R',"Status    : "+ txtSTSDS.getText().trim(),L_intPADLN));
				L_intTEMP ++;
				if(L_intPADLN == 25)
					L_intPADLN = stbDOTLN.length() - 25;
				else 
					L_intPADLN = 25;
			}
									
			if(txtCMPCT.getText().trim().length() == 2)
			{
				if(L_intTEMP == 2)
				{
					dosREPORT.writeBytes("\n");
					L_intTEMP = 0;
					cl_dat.M_intLINNO_pbst ++;
				}
				dosREPORT.writeBytes(padSTRING('R',"Category  : "+ txtCMPDS.getText().trim(),L_intPADLN));
				L_intTEMP ++;
				if(L_intPADLN == 25)
					L_intPADLN = stbDOTLN.length() - 25;
				else 
					L_intPADLN = 25;
			}						
			
			if(txtZONCD.getText().trim().length() == 2)
			{
				if(L_intTEMP == 2)
				{
					dosREPORT.writeBytes("\n");
					L_intTEMP = 0;
					cl_dat.M_intLINNO_pbst ++;
				}
				dosREPORT.writeBytes(padSTRING('R',"Zone Code : "+ txtZONDS.getText().trim(),L_intPADLN));
				L_intTEMP ++;
				if(L_intPADLN == 25)
					L_intPADLN = stbDOTLN.length() - 25;
				else 
					L_intPADLN = 25;
			}
												
			if(txtPRDCD.getText().trim().length() == 10)
			{
				if(L_intTEMP == 2)
				{
					dosREPORT.writeBytes("\n");
					L_intTEMP = 0;
					cl_dat.M_intLINNO_pbst ++;
				}
				dosREPORT.writeBytes(padSTRING('R',"Grade Desc: "+ txtPRDDS.getText().trim(),L_intPADLN));
				L_intTEMP ++;
			}
						
			if(txtPRTCD.getText().trim().length() == 5)
			{								
				cl_dat.M_intLINNO_pbst ++;
				dosREPORT.writeBytes("\n"+padSTRING('R',"Party Desc: "+ txtPRTDS.getText().trim(),stbDOTLN.length() - 25));
			}			
			dosREPORT.writeBytes("\n"+stbDOTLN.toString());
			dosREPORT.writeBytes("\n"+strCOLHD);			
			dosREPORT.writeBytes("\n"+stbDOTLN.toString());
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}


/*			hstCMPDS = new Hashtable();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where"
				+" isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_CGMTP ='SYS'"
				+" AND CMT_CGSTP = 'COXXCCT'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstCMPDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}			*/
		/*	hstSTSDS = new Hashtable();
			M_strSQLQRY = "Select distinct CMT_CODCD,CMT_CODDS from CO_CDTRN where"
				+" isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_CGMTP ='STS'"
				+" AND CMT_CGSTP = 'COXXCST'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstSTSDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}*/