/*
System Name		: Materials Management System
Program Name	: Gate pass reports
Author			: Mr.S.R.Mehesare
Modified Date	: 20/10/2005
Documented Date	: 
Version			: v2.0.0

Modified Date	: 
Details			: 
				: 
*/

import java.util.Hashtable;import java.sql.ResultSet;
import java.util.Enumeration;import java.util.StringTokenizer;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : Gate Pass Reports

Purpose : Program for Gate Pass Reports for various Gate Pass Type for given Date
          and Department Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN
MM_GPTRN       GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD                     #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    GP_MGPDT                MM_GPTRN           Date          Gate Pass Date
txtTODAT    GP_MGPDT                MM_GPTRN           Date          Gate Pass Date
txtFMDPT    GP_DPTCD                MM_GPTRN           VARCHAR(3)    Department Code
txtFDPTN    CMT_CODDS               CO_CDTRN           VARCHAR(20)   Dept. Name
txtTODPT    GP_DPTCD                MM_GPTRN           VARCHAR(3)    Department Code
txtTDPTN    CMT_CODDS               CO_CDTRN           VARCHAR(20)   Dept. Name
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>

Different Gate in Types are taken from CO_CDTRN for condiation :-
   1) CMT_CGMTP = 'DOC' 
   2) AND CMT_CGSTP = 'MMXXMGP' 
   3) AND CMT_STSFL<>'X'
Different  Department Code is taken from CO_CDTRN for condiations :- 
   1) CMT_CGMTP = 'SYS'
   2) AND CMT_CGSTP = 'COXXDPT'

Data for List Of Gate Passes is taken from MM_GPTRN and CO_CTMST for condiations :-
   1) CT_MATCD = GP_MATCD 
   2) AND GP_STRTP = Specified Store Type.
  If Not All Gate Pass Type Selected
   3) AND GP_MGPTP = Specified Gate Pass Type.
  If Department code Range is Specified 
   4) AND GP_DPTCD between specified Dept. Code Range. 
  If Date Range is Specified 
   5) AND GP_MGPDT between given date range.									

Data for OverDue Gate Pass is taken from MM_GPTRN and CO_CTMST for condiations :-
   1) CT_MATCD = GP_MATCD 
   2) AND GP_STRTP = selected Store Type.
   3) AND isnull(GP_ISSQT,0)-isnull(GP_RECQT,0) > 0 
  If Not All Gate Pass Type Selected
   3) AND GP_MGPTP = Specified Gate Pass Type.
  If Department code Range is Specified 
   4) AND GP_DPTCD between specified Dept. Code Range.
   5) AND (days(CURRENT_DATE) - days(GP_DUEDT)) > 0 
   6) AND isnull(GP_ISSQT,0) > isnull(GP_RECQT,0)
   7) AND GP_GOTDT IS NOT NULL 
   8) AND isnull(GP_STSFL,' ') NOT IN ('X','C')

Data for Pending Gate Pass is taken from MM_GPTRN and CO_CTMST for condiations :-
   1) CT_MATCD = GP_MATCD 
   2) AND GP_STRTP = Specified store Type.
  If Not All Gate Pass Type Selected
   3) AND GP_MGPTP = Specified Gate Pass Type.
  If Department code Range is Specified 
   4) AND GP_DPTCD between specified Dept. Code Range.
   5) AND isnull(GP_ISSQT,0)-isnull(GP_RECQT,0) > 0 ";				
   6) AND GP_GOTDT IS NOT NULL 
   7) AND isnull(GP_STSFL,' ') NOT IN ('X','C')

Data for Pending GRIN is taken from MM_GRMST and CO_CTMST for condiations :-
   1) CT_MATCD = GR_MATCD 
   2) AND GR_STRTP = Selected Store Type
   3) AND (isnull(GR_REJQT,0) - isnull(GR_GPQTY,0)) > 0 
   4) AND isnull(GR_STSFL,' ') <> 'X' 
   5) AND isnull(GR_GPTAG,'') <> 'C'

<B>Validations & Other Information:</B>    
    - Department code specifies must be valid.
    - To Date must be greater than From Date & smaller then current Date.
</I> */

class mm_rpgps extends cl_rbase
{									/** JComboBox to specify the Gate Pass Type.*/
	private JComboBox cmbGTPTP;		/** JComboBox to specify the report option*/
	private JComboBox cmbRPTOP;		/** JTextField to specify & display From Date.*/
	private JTextField txtFMDAT;	/** JTextField to specify & display To date.*/
	private JTextField txtTODAT;	/** JTextField to specify & display From Material Code*/
	private JTextField txtFMMCD;	/** JTextField to specify & display From Department Code.*/
	private JTextField txtFMDPT;	/** JTextField to display From Department Name.*/
	private JTextField txtFDPTN;	/** JTextField to specify & display To Material Code*/
	private JTextField txtTOMCD;	/** JTextField to specify & display To Departmetn Code*/
	private JTextField txtTODPT;	/** JTextField to display To department Name.*/
	private JTextField txtTDPTN;	/** JCheckBox to specify the summery report.*/
	private JCheckBox chbSUMRY;		
									/** String variable for department code.*/
	private String strDPTCD="";		/** String varaible for gate pass number.*/
	private String strMGPNO="";		/** Sting varaible for generated report File Name.*/
	private String strFILNM;		/** Hashtable to store gate code as key and pass name as value.*/
	private Hashtable<String,String> hstGTPTP;		/** Hashtable to store departmetn code as key and name as value.*/
	private Hashtable<String,String> hstDPTCD;		/** String variable for GRIN number.*/
	private String strGRNNO;		/** DataOutputStream Object to generate & hold Stream  of report data.*/	
	private DataOutputStream dosREPORT;/** FileoutputStream Object to generate the Report File.*/
	private FileOutputStream fosREPORT;/** Integer variable to count the Number of records fetched to block the report if no data found.*/
	private int intRECCT;									/** Final String to specify List of Gate Passes.*/	
	private final String strLSTGP = "List Of Gate Passes";	/** Final String to specify OverDue Gate Passes.*/
	private final String strOVDGP = "OverDue Gate Pass";	/** Final String to specify Pending Gate Passes.*/
	private final String strPNDGP = "Pending Gate Pass";	/** Final String to specify Pending GRIN.*/
	private final String strPNDGR = "Pending GRIN";			/** Final value for Grand Total*/
	private double dblTOTQT = 0.000;			/** String variable to print Dotted line in the Report.*/	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	mm_rpgps()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			add(new JLabel("Report Type"),3,3,1,1.2,this,'R');
			add(cmbRPTOP = new JComboBox(),3,4,1,2,this,'L');
			add(new JLabel("Gate Pass Type"),4,3,1,1.2,this,'R');
			add(cmbGTPTP = new JComboBox(),4,4,1,2,this,'L');
			add(new JLabel("From Department"),5,3,1,1.2,this,'R');
			add(txtFMDPT = new TxtNumLimit(3.0),5,4,1,1,this,'L');
			add(txtFDPTN = new JTextField(),5,5,1,2.5,this,'L');
			add(new JLabel("To Department"),6,3,1,1.2,this,'R');
			add(txtTODPT = new TxtNumLimit(3.0),6,4,1,1,this,'L');
			add(txtTDPTN = new JTextField(),6,5,1,2.5,this,'L');
			add(new JLabel("From Date"),7,3,1,1.2,this,'R');
			add(txtFMDAT = new TxtDate(),7,4,1,1,this,'L');
			add(new JLabel("To Date"),8,3,1,1.2,this,'R');
			add(txtTODAT = new TxtDate(),8,4,1,1,this,'L');
			add(new JLabel("From Mat.Code"),9,3,1,1.2,this,'R');
			add(txtFMMCD = new JTextField(),9,4,1,2,this,'L');
			add(new JLabel("To Mat.Code"),10,3,1,1.2,this,'R');
			add(txtTOMCD = new JTextField(),10,4,1,2,this,'L');
			add(chbSUMRY = new JCheckBox("Gate Pass Summary"),11,4,1,2,this,'L');
			
			cmbRPTOP.addItem(strLSTGP);
			cmbRPTOP.addItem(strOVDGP);
			cmbRPTOP.addItem(strPNDGP);
			cmbRPTOP.addItem(strPNDGR);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);

			cmbGTPTP.addItem("00 All");
			hstGTPTP = new Hashtable<String,String>(5,0.75f);
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN"
					+" where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MMXXMGP' and CMT_STSFL<>'X'";
			M_rstRSSET =cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{				
					cmbGTPTP.addItem(M_rstRSSET.getString("CMT_CODCD").substring(1,3)+" "+M_rstRSSET.getString("CMT_CODDS"));
					hstGTPTP.put(M_rstRSSET.getString("CMT_CODCD").substring(1,3),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}			
			hstDPTCD = new Hashtable<String,String>(5,0.75f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' "
				+"AND CMT_CGSTP = 'COXXDPT'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())			
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));			
				M_rstRSSET.close();
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super class method to enable & disable the components.
	 */
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		if(L_STAT == false)
			return;
		txtFDPTN.setEnabled(false);
		txtTDPTN.setEnabled(false);
		if(cmbRPTOP.getItemCount() >0)
		{
		    cmbGTPTP.removeActionListener(this);
		if(cmbRPTOP.getSelectedItem().equals(strLSTGP))
		{										
			cmbGTPTP.setSelectedItem("00 All");
			txtFMMCD.setEnabled(true);
			txtTOMCD.setEnabled(true);
			txtFMDPT.setEnabled(true);
			txtTODPT.setEnabled(true);
			txtFMDAT.setEnabled(true);
			txtTODAT.setEnabled(true);
			chbSUMRY.setEnabled(true);
		}
		else if(cmbRPTOP.getSelectedItem().equals(strOVDGP))
		{															
			cmbGTPTP.setSelectedIndex(1);
			cmbGTPTP.setEnabled(false);
			txtFMDPT.setEnabled(true);
			txtTODPT.setEnabled(true);
			txtFMMCD.setEnabled(true);
			txtTOMCD.setEnabled(true);
			txtFMDAT.setEnabled(false);
			txtTODAT.setEnabled(true);
			chbSUMRY.setEnabled(false);
		}
		else if(cmbRPTOP.getSelectedItem().equals(strPNDGP))
		{					
			cmbGTPTP.setSelectedIndex(1);										
			cmbGTPTP.setEnabled(false);
			txtFMDPT.setEnabled(true);
			txtTODPT.setEnabled(true);
			txtFMMCD.setEnabled(true);
			txtTOMCD.setEnabled(true);
			txtFMDAT.setEnabled(false);
			txtTODAT.setEnabled(true);
			chbSUMRY.setEnabled(false);
		}
		else if(cmbRPTOP.getSelectedItem().equals(strPNDGR))
		{					
			cmbGTPTP.setSelectedIndex(3);
			cmbGTPTP.setEnabled(false);
			txtFMDPT.setEnabled(false);
			txtTODPT.setEnabled(false);
			txtFMMCD.setEnabled(false);
			txtTOMCD.setEnabled(false);
			txtFMDAT.setEnabled(false);
			txtTODAT.setEnabled(false);
			chbSUMRY.setEnabled(false);
		}
		cmbGTPTP.addActionListener(this);
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{			
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)				
					setENBL(true);									
				else
					setENBL(false);
			}			
			else if(M_objSOURC == cmbRPTOP)
			{				
				txtFMMCD.setText("");
				txtFMDPT.setText("");
				txtFDPTN.setText("");
				txtTOMCD.setText("");
				txtTODPT.setText("");
				txtTDPTN.setText("");
				txtFMDAT.setText("");
				txtTODAT.setText("");
				chbSUMRY.setSelected(false);
				cmbGTPTP.setEnabled(true);	
				setENBL(true);		
			}
			if(M_objSOURC == cmbGTPTP)
			{
				txtFMDPT.requestFocus();
				txtFMDPT.setText("");
				txtFMMCD.setText("");
				txtFDPTN.setText("");
				txtTODPT.setText("");
				txtTOMCD.setText("");
				txtTDPTN.setText("");
				txtFMDAT.setText("");
				txtTODAT.setText("");				
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
		try
		{
			if(M_objSOURC == cmbRPTOP)
				setMSG("Select Report Type ..",'N');
			if(M_objSOURC == cmbGTPTP)	
				setMSG("Select Gate Pass Type..",'N');
			else if(M_objSOURC == txtFMMCD)
				setMSG("Enter Dept code if Report for Specific Mat.Code Requried, Press 'F1' For Help ..",'N');
			else if(M_objSOURC == txtFMDPT)
				setMSG("Enter Dept code if Report for Specific Dept Requried, Press 'F1' For Help ..",'N');
			else if(M_objSOURC == txtTODPT)			
				setMSG("Enter Dept code if Report for Specific Dept Requried, Press 'F1' For Help ..",'N');
			else if(M_objSOURC == txtFMDAT)
				setMSG("Enter From Date..",'N');
			else if(M_objSOURC == txtTODAT)			
				setMSG("Enter To Date..",'N');			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"FocusGained");
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode()== L_KE.VK_F1)
			{
				if(M_objSOURC == txtFMMCD)
				{
					M_strHLPFLD = "txtFMMCD";
					M_strSQLQRY = "SELECT GP_MATCD, CT_MATDS FROM MM_GPTRN, CO_CTMST WHERE GP_MATCD = CT_MATCD and GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
					if(txtFMDPT.getText().trim().length() != 0 && txtTODPT.getText().trim().length() != 0)	//If From Department Is Blank
						M_strSQLQRY +=" AND GP_DPTCD BETWEEN  '"+txtFMDPT.getText().trim()+"' AND '"+txtTODPT.getText().trim()+"'";
					if(txtFMMCD.getText().length()>0)
						M_strSQLQRY += " AND GP_MATCD LIKE '"+txtFMMCD.getText()+"%' ";
					M_strSQLQRY += "  order by GP_MATCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Description"},2,"CT");
				}
				if(M_objSOURC == txtTOMCD)
				{
					M_strHLPFLD = "txtTOMCD";
					M_strSQLQRY = "SELECT GP_MATCD, CT_MATDS FROM MM_GPTRN, CO_CTMST WHERE GP_MATCD = CT_MATCD and GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
					if(txtFMDPT.getText().trim().length() != 0 && txtTODPT.getText().trim().length() != 0)	//If From Department Is Blank
						M_strSQLQRY +=" AND GP_DPTCD BETWEEN  '"+txtFMDPT.getText().trim()+"' AND '"+txtTODPT.getText().trim()+"'";
					if(txtTOMCD.getText().length()>0)
						M_strSQLQRY += " AND GP_MATCD LIKE '"+txtTOMCD.getText()+"%' ";
					M_strSQLQRY += "  order by GP_MATCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Description"},2,"CT");
				}
				if(M_objSOURC == txtFMDPT)
				{
					M_strHLPFLD = "txtFMDPT";
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
						+"CMT_CGSTP = 'COXXDPT' ";	
					if(txtFMDPT.getText().length()>0)
						M_strSQLQRY += " AND CMT_CODCD LIKE '"+txtFMDPT.getText()+"%' ";
					M_strSQLQRY += " ORDER BY CMT_CODDS";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Department"},2,"CT");
				}
				if(M_objSOURC == txtTODPT)
				{
					M_strHLPFLD = "txtTODPT";
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
						+"CMT_CGSTP = 'COXXDPT' ";
					if(txtTODPT.getText().length()>0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtTODPT.getText()+"%' ";
					M_strSQLQRY += " ORDER BY CMT_CODDS";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Department"},2,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtFMMCD)
				{
					if(txtFMMCD.getText().length() > 0)
					{
						M_strSQLQRY = "SELECT CT_MATDS FROM CO_CTMST WHERE CT_MATCD = '"+txtFMMCD.getText()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET == null || !M_rstRSSET.next())
								setMSG("Invalid Mat.Code Press F1 to select from List..",'E');
					}
				}
				if(M_objSOURC == txtTOMCD)
				{
					if(txtTOMCD.getText().length() > 0)
					{
						M_strSQLQRY = "SELECT CT_MATDS FROM CO_CTMST WHERE CT_MATCD = '"+txtTOMCD.getText()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET == null || !M_rstRSSET.next())
								setMSG("Invalid Mat.Code Press F1 to select from List..",'E');
					}
				}
				if(M_objSOURC == txtFMDPT )
				{
					if(txtFMDPT.getText().length() ==3)
					{
						M_strSQLQRY = "SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
							+"CMT_CGSTP = 'COXXDPT' AND CMT_CODCD ='"+txtFMDPT.getText()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								txtFDPTN.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
								txtTODPT.requestFocus();
							}
							else
								setMSG("Invalid Department Code Press F1 to select from List..",'E');
						}
					}
					else
						setMSG("From Department Code Cannot be blank..",'E');
				}
				if(M_objSOURC == txtTODPT)
				{
					if(txtTODPT.getText().length() == 3)
					{
						M_strSQLQRY = "SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
							+"CMT_CGSTP = 'COXXDPT' AND CMT_CODCD ='"+txtTODPT.getText()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								txtTDPTN.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
								if(cmbRPTOP.getSelectedItem().equals(strLSTGP))
								{
									txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
									M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
									M_calLOCAL.add(java.util.Calendar.DATE,-1);
       								txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
									txtFMDAT.requestFocus();
								}
								else
								{
									txtTODAT.requestFocus();
									txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
								}
							}
							else
								setMSG("Invalid Department Code Press F1 to select from List..",'E');
						}						
					}
					else
						setMSG("To Department Code Cannot be blank..",'E');
				}
				if(M_objSOURC == txtFMDAT)
				{
					if(txtFMDAT.getText().trim().length()==10)
						txtTODAT.requestFocus();
					else
						setMSG("From Date Cannot be blank..",'E');
				}
				if(M_objSOURC == txtTODAT)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else if(M_objSOURC == cmbRPTOP)
				{
					if(cmbRPTOP.getSelectedIndex() == 1 ||cmbRPTOP.getSelectedIndex() == 2)
						txtFMDPT.requestFocus();
					else if(cmbRPTOP.getSelectedIndex() == 3 )
						cl_dat.M_btnSAVE_pbst.requestFocus();
					else
						cmbGTPTP.requestFocus();
				}				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	/**
	 * Method to execuate the F1 Help for selected component.
	 */
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();			
			if(M_strHLPFLD.equals("txtFMMCD"))
			{				
				txtFMMCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtTOMCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtTOMCD"))
			{				
				txtTOMCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtFMDPT"))
			{				
				txtFMDPT.setText(cl_dat.M_strHLPSTR_pbst);
				txtFDPTN.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtTODPT"))
			{
				txtTODPT.setText(cl_dat.M_strHLPSTR_pbst);
				txtTDPTN.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHPOK");
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
				strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgps.html";
			if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgps.doc";
			
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Gate Pass Reports"," ");
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
	 * Method to fetch Data from the database & to club it with Header & Footer.
	 */
	public void getDATA()
	{
		if(!vldDATA())
			return;		
		try
		{
			intRECCT = 0;
			ResultSet L_rstRSSET;
			strMGPNO = "";
			strGRNNO = "";			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Gate Pass Reports</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			if(cmbRPTOP.getSelectedItem().equals(strLSTGP))
			{
				if(chbSUMRY.isSelected())
				{
					M_strSQLQRY = "SELECT GP_MATCD,sum(GP_ISSQT) GP_ISSQT,CT_MATDS,CT_UOMCD FROM MM_GPTRN,CO_CTMST "
							+"WHERE CT_MATCD = GP_MATCD AND GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
					if(cmbGTPTP.getSelectedIndex() > 0)		//Except All Gate Pass Type Selected
						M_strSQLQRY +=" AND GP_MGPTP = '"+cmbGTPTP.getSelectedItem().toString().substring(0,2)+"'";
					if(txtFMDPT.getText().trim().length() != 0 && txtTODPT.getText().trim().length() != 0)	//If From Department Is Blank
						M_strSQLQRY +=" AND GP_DPTCD BETWEEN  '"+txtFMDPT.getText().trim()+"' AND '"+txtTODPT.getText().trim()+"'";
					if(txtFMMCD.getText().trim().length() != 0 && txtTOMCD.getText().trim().length() != 0)	//If From Mat.Code Is Blank
						M_strSQLQRY +=" AND GP_MATCD BETWEEN  '"+txtFMMCD.getText().trim()+"' AND '"+txtTOMCD.getText().trim()+"'";
					M_strSQLQRY +=" AND GP_MGPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' GROUP BY GP_MATCD,CT_MATDS,CT_UOMCD ORDER BY GP_MATCD ";
				}
				else
				{
					M_strSQLQRY = "SELECT GP_MGPNO,GP_MGPDT,GP_MATCD,GP_VENCD,GP_VENNM,GP_ISSQT,GP_DPTCD,"
						+"GP_RECQT,CT_MATDS,CT_UOMCD,day(getdate()) - day(GP_MGPDT) PDGDT,"
						+"GP_DUEDT,GP_GRNNO FROM MM_GPTRN,CO_CTMST WHERE CT_MATCD = GP_MATCD AND "
						+"GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
					if(!cmbGTPTP.getSelectedItem().toString().equals("00 All"))		//Except All Gate Pass Type Selected						
						M_strSQLQRY +=" AND GP_MGPTP = '"+cmbGTPTP.getSelectedItem().toString().substring(0,2)+"'";
					if(txtFMDPT.getText().trim().length() != 0 && txtTODPT.getText().trim().length() != 0)	//If From Department Is Blank
						M_strSQLQRY +=" AND GP_DPTCD BETWEEN  '"+txtFMDPT.getText().trim()+"' AND '"+txtTODPT.getText().trim()+"'";
					if(txtFMMCD.getText().trim().length() != 0 && txtTOMCD.getText().trim().length() != 0)	//If From Mat.Code Is Blank
						M_strSQLQRY +=" AND GP_MATCD BETWEEN  '"+txtFMMCD.getText().trim()+"' AND '"+txtTOMCD.getText().trim()+"'";
					M_strSQLQRY +=" AND GP_MGPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'"
							+" AND isnull(GP_STSFL,' ') <> 'X' ORDER BY GP_DPTCD,GP_MGPNO,GP_MATCD ";
				}
			}
			if(cmbRPTOP.getSelectedItem().equals(strOVDGP))
			{
				M_strSQLQRY = "SELECT GP_MGPNO,GP_MGPDT,GP_MATCD,GP_VENCD,GP_VENNM,GP_ISSQT,GP_DPTCD,"
					+"GP_RECQT,CT_MATDS,CT_UOMCD,day(getdate()) - day(GP_DUEDT) PDGDT,"
					+"GP_DUEDT,GP_GRNNO FROM MM_GPTRN,CO_CTMST WHERE CT_MATCD = GP_MATCD AND "
					+"GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(!cmbGTPTP.getSelectedItem().toString().equals("00 All"))		//Except All Gate Pass Type Selected
					M_strSQLQRY +=" AND GP_MGPTP = '"+cmbGTPTP.getSelectedItem().toString().substring(0,2)+"'";
				M_strSQLQRY +=" AND isnull(GP_ISSQT,0)-isnull(GP_RECQT,0) > 0 ";
				if(txtFMDPT.getText().trim().length() != 0 && txtTODPT.getText().trim().length() != 0)	//If From Department Is Blank
					M_strSQLQRY +=" AND GP_DPTCD BETWEEN  '"+txtFMDPT.getText().trim()+"' AND '"+txtTODPT.getText().trim()+"'";
				if(txtFMMCD.getText().trim().length() != 0 && txtTOMCD.getText().trim().length() != 0)	//If From Department Is Blank
					M_strSQLQRY +=" AND GP_MATCD BETWEEN  '"+txtFMMCD.getText().trim()+"' AND '"+txtTOMCD.getText().trim()+"'";
				M_strSQLQRY += " AND (day(getdate()) - day(GP_DUEDT)) > 0 AND isnull(GP_ISSQT,0) > isnull(GP_RECQT,0) "
					+" AND GP_GOTDT IS NOT NULL AND isnull(GP_STSFL,' ') NOT IN ('X','C') ORDER BY GP_DPTCD,GP_MGPNO,GP_MATCD ";
				System.out.println(M_strSQLQRY);
					
			}
			if(cmbRPTOP.getSelectedItem().equals(strPNDGP))
			{
				
				M_strSQLQRY = "SELECT GP_MGPNO,GP_MGPDT,GP_MATCD,GP_VENCD,GP_VENNM,GP_ISSQT,GP_DPTCD,"
					+"GP_RECQT,CT_MATDS,CT_UOMCD,day(getdate()) - day(GP_MGPDT) PDGDT,"
					+"GP_DUEDT,GP_GRNNO FROM MM_GPTRN,CO_CTMST WHERE CT_MATCD = GP_MATCD AND "
					+"GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(!cmbGTPTP.getSelectedItem().toString().equals("00 All"))		//Except All Gate Pass Type Selected
					M_strSQLQRY +=" AND GP_MGPTP = '"+cmbGTPTP.getSelectedItem().toString().substring(0,2)+"'";
				M_strSQLQRY +=" AND isnull(GP_ISSQT,0)-isnull(GP_RECQT,0) > 0 ";
				if(txtFMDPT.getText().trim().length() != 0 && txtTODPT.getText().trim().length() != 0)	//If From Department Is Blank
					M_strSQLQRY +=" AND GP_DPTCD BETWEEN  '"+txtFMDPT.getText().trim()+"' AND '"+txtTODPT.getText().trim()+"'";
				if(txtFMMCD.getText().trim().length() != 0 && txtTOMCD.getText().trim().length() != 0)	//If From Department Is Blank
					M_strSQLQRY +=" AND GP_MATCD BETWEEN  '"+txtFMMCD.getText().trim()+"' AND '"+txtTOMCD.getText().trim()+"'";
				M_strSQLQRY +=" and GP_GOTDT IS NOT NULL AND isnull(GP_STSFL,' ') NOT IN ('X','C') ORDER BY GP_DPTCD,GP_MGPNO,GP_MATCD ";
				//System.out.println(strPNDGP+" : "+M_strSQLQRY);
			}
			if(cmbRPTOP.getSelectedItem().equals(strPNDGR))
			{
				M_strSQLQRY = "SELECT GR_GRNNO,GR_ACPDT,GR_VENCD,GR_VENNM,GR_MATCD,GR_REJQT,GR_RECQT,GR_GPQTY,GR_PORNO,"
					+"(day(getdate()) - day(GR_ACPDT)) P_DAY,CT_MATDS,CT_UOMCD FROM MM_GRMST,CO_CTMST "
					+"WHERE CT_MATCD = GR_MATCD AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
					+"AND (isnull(GR_REJQT,0) - isnull(GR_GPQTY,0)) > 0 AND "
					+"isnull(GR_STSFL,' ') <> 'X' AND isnull(GR_GPTAG,'') <> 'C' ORDER BY GR_GRNNO,GR_MATCD ";
			}
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				prnHEADER();
				if(cmbRPTOP.getSelectedItem().equals(strLSTGP))
				{
					if(chbSUMRY.isSelected())
						prnSMGPS();
					else
						prnLIGPS();
				}
				if(cmbRPTOP.getSelectedItem().equals(strOVDGP))
					prnOVRGP();
				if(cmbRPTOP.getSelectedItem().equals(strPNDGP))
					prnPENGP();
				if(cmbRPTOP.getSelectedItem().equals(strPNDGR))
					prnPENGR();
								
				dosREPORT.writeBytes("\n"+strDOTLN+"\n");
				if(txtFMMCD.getText().trim().length()==10 && txtTOMCD.getText().trim().length()==10)
					dosREPORT.writeBytes(padSTRING('R',"Total Qty : ",80)+padSTRING('L',setNumberFormat(dblTOTQT,3),16));
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
	}
	
	/**
	 * Method to validate the components before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		try
		{	
			cl_dat.M_PAGENO = 0;
			if(cmbRPTOP.getSelectedIndex() != 3) 
			{
				if(txtFMMCD.getText().trim().length() > 0)
				{
					if(txtTOMCD.getText().trim().length() == 0)
					{
						setMSG("Enter To Material Code..",'E');
						txtTOMCD.requestFocus();
						return false;
					}
				}
				if(txtFMDPT.getText().trim().length() > 0)
				{
					if(txtTODPT.getText().trim().length() == 0)
					{
						setMSG("Enter To Department Code..",'E');
						txtTODPT.requestFocus();
						return false;
					}
				}
				if(cmbRPTOP.getSelectedIndex() == 0)
				{
					if(txtFMDAT.getText().trim().length() == 0)
					{
						setMSG("Enter From Date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
					else if(txtTODAT.getText().trim().length() == 0)
					{
						setMSG("Enter To Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
					{
						setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
				}
				if(txtTODAT.getText().trim().length() == 0)
				{
					setMSG("Enter To Date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("To Date Should Not Be Grater Than Todays Date..",'E');
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
	 * Method to generate the Summry of Gate Pass List.
	 */
	public void prnSMGPS()		//0
	{
		try
		{
			dblTOTQT = 0.000;
			while(M_rstRSSET.next())
			{
				intRECCT = 1;
				if(cl_dat.M_intLINNO_pbst >58)
				{
					dosREPORT.writeBytes("\n"+strDOTLN);					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),13));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),61));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),""),18));
				dblTOTQT += Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0.000"));
				cl_dat.M_intLINNO_pbst++;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnSMGPS");
		}
	}
	/**
	 * Method to generate the List Of Gate Pass.
	 */
	public void prnLIGPS()		//0
	{
		try
		{
			dblTOTQT = 0.000;
			while(M_rstRSSET.next())
			{
				intRECCT = 1;
				if(cl_dat.M_intLINNO_pbst >58)
				{
					dosREPORT.writeBytes("\n"+strDOTLN);					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				if(!strMGPNO.equals(nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"")))
				{
					String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE "
						+"RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = 'GP' "
						+"AND RM_DOCTP = 'GP' AND RM_DOCNO = '"+strMGPNO+"' AND isnull(RM_STSFL,'') <> 'X'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET.next())
					{
						dosREPORT.writeBytes("\n"); 
						cl_dat.M_intLINNO_pbst++;
						String L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
						dosREPORT.writeBytes("Remark : ");
						if(L_strREMDS.length() > 87)
						{
							dosREPORT.writeBytes(L_strREMDS.substring(0,87));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(L_strREMDS.substring(87,L_strREMDS.length()));
						}
						else
							dosREPORT.writeBytes(L_strREMDS);	
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
					L_rstRSSET.close();
				}
				if(!(strDPTCD).equals(nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"")))
				{
					dosREPORT.writeBytes("\n");
					strDPTCD = nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"");
					dosREPORT.writeBytes(padSTRING('R',"Department : "+hstDPTCD.get(strDPTCD),96));
					dosREPORT.writeBytes("\n");
				}
				//Check Whether Curretn Gate Pass No Is Equal TO previous Number
				if(!(strMGPNO).equals(nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"")))
				{
					strMGPNO = nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),""),10));
					
					if(M_rstRSSET.getDate("GP_MGPDT") != null)
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("GP_MGPDT")),11));
					else
						dosREPORT.writeBytes(padSTRING('R',"",11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),""),7));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),""),43));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_GRNNO"),""),10));
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst += 3;
				}
				dosREPORT.writeBytes(padSTRING('R',"",3));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),11));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),63));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),""),15));
				dblTOTQT += Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0.000"));
				dosREPORT.writeBytes("\n");									
				cl_dat.M_intLINNO_pbst += 2;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnLIGPS");
		}
	}
	/**
	 * Method to generate the List of Overdue Gate Pass
	 */
	public void prnOVRGP()		//1
	{
		try
		{
			dblTOTQT = 0.000;
			while(M_rstRSSET.next())
			{
				intRECCT = 1;
				if(cl_dat.M_intLINNO_pbst >58)
				{
					dosREPORT.writeBytes("\n"+strDOTLN);					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				if(!strMGPNO.equals(nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"")))
				{
					String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE "
						+"RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = 'GP' AND "
						+"RM_DOCTP = 'GP' AND RM_DOCNO = '"+strMGPNO+"' AND isnull(RM_STSFL,'') <> 'X'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET.next())
					{
						dosREPORT.writeBytes("\n"); 
						cl_dat.M_intLINNO_pbst++;
						String L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
						dosREPORT.writeBytes("Remark : ");
						if(L_strREMDS.length() > 87)
						{
							dosREPORT.writeBytes(L_strREMDS.substring(0,87));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(L_strREMDS.substring(87,L_strREMDS.length()));
						}
						else
							dosREPORT.writeBytes(L_strREMDS);	
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
					L_rstRSSET.close();
				}
				if(!strDPTCD.equals(nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"")))
				{
					dosREPORT.writeBytes("\n");
					strDPTCD = nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"");
					dosREPORT.writeBytes("Department : "+hstDPTCD.get(strDPTCD));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 2;
				}
				//Print Gate pass No If not Equal to Previous one
				if(!strMGPNO.equals(M_rstRSSET.getString("GP_MGPNO")))
				{
					strMGPNO = nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"");
					dosREPORT.writeBytes("\n");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
					    dosREPORT.writeBytes("<b>");    
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),""),10));
					if(M_rstRSSET.getDate("GP_MGPDT") != null)
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("GP_MGPDT")),11));
					else
						dosREPORT.writeBytes(padSTRING('R',"",11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),""),6));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),""),38));
					if(M_rstRSSET.getDate("GP_DUEDT") != null)
						dosREPORT.writeBytes(padSTRING('L',M_fmtLCDAT.format(M_rstRSSET.getDate("GP_DUEDT")),15));
					else
						dosREPORT.writeBytes(padSTRING('L',"",15));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PDGDT"),""),15));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
					    dosREPORT.writeBytes("</b>");    
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst += 3;
				}
				dosREPORT.writeBytes(padSTRING('R',"",1));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),11));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),55));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0"),13));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0")),3),15));
				dblTOTQT += Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0.000"));
				dosREPORT.writeBytes("\n");										
				cl_dat.M_intLINNO_pbst += 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnOVRGP");
		}
	}
	/**
	 * Method to generate the List Of Pending Gate Pass
	 */
	public void prnPENGP()		//2
	{
		try
		{
			dblTOTQT = 0.000;
			while(M_rstRSSET.next())
			{
				intRECCT = 1;
				if(cl_dat.M_intLINNO_pbst >58)
				{
					dosREPORT.writeBytes("\n"+strDOTLN);					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				if(!strMGPNO.equals(nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"")))
				{
					String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE "
						+"RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = 'GP' "
						+"AND RM_DOCTP = 'GP' AND RM_DOCNO = '"+strMGPNO+"' AND isnull(RM_STSFL,'') <> 'X'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET.next())
					{
						dosREPORT.writeBytes("\n"); 
						cl_dat.M_intLINNO_pbst++;
						String L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
						dosREPORT.writeBytes("Remark : ");
						if(L_strREMDS.length() > 87)
						{
							dosREPORT.writeBytes(L_strREMDS.substring(0,87));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(L_strREMDS.substring(87,L_strREMDS.length()));
						}
						else
							dosREPORT.writeBytes(L_strREMDS);	
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
					L_rstRSSET.close();
				}
				if(!strDPTCD.equals(nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"")))
				{
					dosREPORT.writeBytes("\n");
					strDPTCD = nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"");
					dosREPORT.writeBytes("Department : "+hstDPTCD.get(strDPTCD));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 2;
				}
				//Print Gate pass No If not Equal to Previous one
				if(!strMGPNO.equals(nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"")))
				{
					strMGPNO = nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),""),10));
					if(M_rstRSSET.getDate("GP_MGPDT") != null)
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("GP_MGPDT")),11));
					else
						dosREPORT.writeBytes(padSTRING('R',"",11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),""),6));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),""),38));
					if(M_rstRSSET.getDate("GP_DUEDT") != null)
						dosREPORT.writeBytes(padSTRING('L',M_fmtLCDAT.format(M_rstRSSET.getDate("GP_DUEDT")),15));
					else
						dosREPORT.writeBytes(padSTRING('L',"",15));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PDGDT"),""),15));
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst += 3;
				}
				dosREPORT.writeBytes(padSTRING('R',"",1));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),11));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),55));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0")),3),13));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0"),15));
				dblTOTQT += Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0.000"));
				dosREPORT.writeBytes("\n");										
				cl_dat.M_intLINNO_pbst += 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnPENGP");
		}
	}
	/**
	 * Method to generate the List of Pending GRIN  
	 */
	public void prnPENGR()		//3
	{
		try
		{
			strGRNNO = "";
			while(M_rstRSSET.next())
			{
				intRECCT = 1;
				if(cl_dat.M_intLINNO_pbst >58)
				{
					dosREPORT.writeBytes("\n"+strDOTLN);					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				if(!strGRNNO.equals(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")))
				{
					String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE "
						+"RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = 'GR' "
						+"AND RM_DOCTP = 'GR' AND RM_DOCNO = '"+strGRNNO+"' AND "
						+"RM_REMTP = 'ACP' AND isnull(RM_STSFL,' ') <> 'X'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET.next())
					{
						dosREPORT.writeBytes("\n"); 
						cl_dat.M_intLINNO_pbst++;
						String L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
						dosREPORT.writeBytes("Remark : ");
						if(L_strREMDS.length() > 87)
						{
							dosREPORT.writeBytes(L_strREMDS.substring(0,87));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(L_strREMDS.substring(87,L_strREMDS.length()));
						}
						else
							dosREPORT.writeBytes(L_strREMDS);	
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
					L_rstRSSET.close();
					strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),9));
							
				if(M_rstRSSET.getDate("GR_ACPDT") != null)
					dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT")),11));
				else
					dosREPORT.writeBytes(padSTRING('R',"",11));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),9));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""),6));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),34));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0"),16));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0"))-Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("GR_GPQTY"),"0")),3),11));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),11));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),60));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),3));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),""),11));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("P_DAY"),""),11));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 3;				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnPENGR");
		}
	}
	/**
	 * Method to generate the Header part of the Report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO ++;
			dosREPORT.writeBytes("\n"+cl_dat.M_strCMPNM_pbst+"\n");			
			cl_dat.M_intLINNO_pbst ++;
			
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				if(chbSUMRY.isSelected())
					dosREPORT.writeBytes(padSTRING('R',"Material Wise Summary For "+cmbGTPTP.getSelectedItem().toString().substring(3)+" Gate Passes ",strDOTLN.length()-21));
				else
					dosREPORT.writeBytes(padSTRING('R',"List Of "+cmbGTPTP.getSelectedItem().toString().substring(3)+" Gate Passes",strDOTLN.length()-21));					
			}
			else if(cmbRPTOP.getSelectedIndex() == 1)
				dosREPORT.writeBytes(padSTRING('R',"LIST OF OVERDUE RETURNABLE GATE PASSES AS ON "+txtTODAT.getText().trim().toString(),strDOTLN.length()-21));
			else if(cmbRPTOP.getSelectedIndex() == 2)
				dosREPORT.writeBytes(padSTRING('R',"LIST OF PENDING RETURNABLE GATE PASSES AS ON "+txtTODAT.getText().trim().toString(),strDOTLN.length()-21));
			else if(cmbRPTOP.getSelectedIndex() == 3)
				dosREPORT.writeBytes(padSTRING('R',"From : Material Handeling Division  To : Purchase Department ",strDOTLN.length()-21));
			
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			cl_dat.M_intLINNO_pbst ++;
			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");			
			cl_dat.M_intLINNO_pbst ++;
			
			if(cmbRPTOP.getSelectedIndex() == 0 )
			{
				dosREPORT.writeBytes(padSTRING('R',"From : "+txtFMDAT.getText()+" To : "+txtTODAT.getText(),40));
				dosREPORT.writeBytes("\n");	cl_dat.M_intLINNO_pbst ++;
			}
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
				dosREPORT.writeBytes(padSTRING('R',"Sub : Rejected Material To Be Collected By The Supplier",strDOTLN.length()-21));
				dosREPORT.writeBytes("\n");	cl_dat.M_intLINNO_pbst ++;
			}
			dosREPORT.writeBytes(strDOTLN+"\n");			
			cl_dat.M_intLINNO_pbst++;
			
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				if(chbSUMRY.isSelected())				
					dosREPORT.writeBytes("Material Code And Description                                             UOM   Quantity Issued");				
				else
				{
					dosREPORT.writeBytes("Gate Pass No. And Date   Vendor Code And Name                          ");
					if(cmbGTPTP.getSelectedItem().toString().equalsIgnoreCase("53 Rejection"))
						dosREPORT.writeBytes("GRIN No.");
				}
			}
			if(cmbRPTOP.getSelectedIndex() == 1)
				dosREPORT.writeBytes("GP No     Date       Vendor Code And Name                          Expected Date   Overdue Days");
			if(cmbRPTOP.getSelectedIndex() == 2)
				dosREPORT.writeBytes("GP No     Date       Vendor Code And Name                          Expected Date   Pending Days");
			if(cmbRPTOP.getSelectedIndex() == 3)
				dosREPORT.writeBytes("GRIN No. GRIN Date  PO No.   Vendor Code And Name                            Rej.Qty.    Pen.Qty");
			
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				if(!chbSUMRY.isSelected())
				{
					dosREPORT.writeBytes("\n");	
					cl_dat.M_intLINNO_pbst ++;
					dosREPORT.writeBytes("Material Code & Description                                                 UOM       Issue Qty.");
				}
			}
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				dosREPORT.writeBytes("\n");	
				cl_dat.M_intLINNO_pbst ++;
				dosREPORT.writeBytes("Material Code & Description                                        Gate Pass Qty.   Pending Qty");
			}
			if(cmbRPTOP.getSelectedIndex() == 2)
			{
				dosREPORT.writeBytes("\n");	
				cl_dat.M_intLINNO_pbst ++;
				dosREPORT.writeBytes("Material Code & Description                                       Qty. To Receive  Received Qty");
			}
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst ++;
				dosREPORT.writeBytes("Material Code And Description                                          UOM   Rec.Qty.    Pen. Dy");
			}
			dosREPORT.writeBytes("\n");	
			cl_dat.M_intLINNO_pbst ++;
			dosREPORT.writeBytes(strDOTLN+"\n");			
			cl_dat.M_intLINNO_pbst ++;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}
