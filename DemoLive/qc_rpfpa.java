/**
System Name   : Laboratoty Information Management System
Program Name  : Finished product Analysis
Program Desc. : Report for taking the Grab Details				
Author        : Mr. S.R.Mehesare
Date          : 28 MAY 2005
Version       : LIMS V2.0.0

Modificaitons 
Modified By   : 
Modified Date : 
Modified det. : 
Version       : 
*/

import java.sql.ResultSet;import java.sql.Timestamp;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
//import java.util.Date; 
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Finished product Analysis		

Purpose : Report for taking the Grab Details.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
QC_RMMST       RM_QCATP,RM_TSTTP, RM_TSTNO            #        #       #       #
QC_PSMST       PS_QCATP, PS-TSTTP,PS_LOTNO,
               PS_RCLNO,PS_TSTNO,PS_TSTDT                              #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtDATE     PS_TSTDT       QC_PSMST      DATE          Test Date 
txtRMRK1    RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 1
txtRMRK2    RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 2
txtRMRK3    RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 3
txtRMRK4    RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 4
--------------------------------------------------------------------------------------
<B>
Logic</B>
Each grade (finshed product) has some proparties. & these proparties changes as per 
new standards adopted. 
These proparties are varing from grade to grade.
System maintains Quality Paramters of each grade & makes Latest paramaters available.
According to the new standards, some new parameters are added and some unwanted
are removed for specific grade.
Hence to generate report every time list of proparties are generated dynamically as :-
   1) Latest property list is fetched from the database associated with perticular grade .
   2) These Properties are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    "PS_"+ Vector.elementAt(i)+"VL" i.e SMT_COLVL, SMT_TOLVL
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

For Remark1,2,3 & 4 are taken from QC_RMMST for conditations
    1) RM_QCATP = given QCA Type here it is "01"
    2) AND RM_TSTTP like last three charectores of the report file name.
    3) AND RM_TSTNO = report date & time 	

<I><B>Conditions Give in Query:</b>
    Data is taken from QC_PSMST and PR_LTMST for given Test Date
       1) PS_LOTNO = LT_LOTNO 
       2) AND PS_RCLNO = LT_RCLNO 
       3) AND PS_TSTDT Between a day before login date & login date time
       4) AND PS_RCLNO = "00"
       5) AND PS_TSTTP ='0101' 
       6) AND PS_QCATP =M_strSBSCD.susbtring(2,4)             
<B>Validations :</B>
    - Date Time must be valid. i.e.smaller than current date & Time.       
</I> */

public class qc_rpfpa extends cl_rbase
{											/** JTextField to enter & display Date.*/
	private JTextField txtDATE;				/** JTextField to enter & display Remark1.*/
	private JTextField txtRMRK1;			/** JTextField to enter & display Remark2.*/
	private JTextField txtRMRK2;			/** JTextField to enter & display Remark3.*/
	private JTextField txtRMRK3;			/** JTextField to enter & display Remark4.*/
	private JTextField txtRMRK4;			/** FileOutputStream for the generated Report File Handeling.*/		
	private FileOutputStream fosREPORT ;    /** DataOutputStream object to store the data as a Stream */
    private DataOutputStream dosREPORT ;	/** String variable for generated Report File Name.*/
	private String strFILNM;				
											/** String variable for Remark 1.*/
	private String strRMRK1="";				/** String variable for Remark 2.*/
	private String strRMRK2="";				/** String variable for Remark 3.*/
	private String strRMRK3="";				/** String variable for Remark 4.*/
	private String strRMRK4="";				/** String variable to Represent the Time.*/
	private String strTIME = "07:00";		/** String variable for dynamically generated Header of the Report.*/
	private String strHEADR;				/** String variable for Test Vales for the grade under production.*/
	private String strTSTVL;				/** String variable to Represent the Time.*/
    private String []arrCCSVL;				/** Array of Strings Charector constatent.*/
	private StringBuffer stbDOTLN;			/** Integer variable to count the number of records fetched.*/
	private int intRECCT;					/** Integer variable for dynamically generated Row Length. */
	private int intROWLN = 0;				/** Integer variable for Report width.*/
	private int intRPSWD;					/** Integer variable for column width.*/
	private int intGLWID;					/** Array of Integers for Column width.*/
	private int arrCOLWD[];					/** Array of Integer for column Description.*/
	private int arrCOLDC[];					/** Boolean variable to manage the selection of query.*/
	private boolean flgRMSTS = false;		
	private final String strINRCL_fn ="00";	/** Vector to store the Quality Propary Description.*/
	private Vector<String> vtrQPRDS = new Vector<String>(); /** Vector to store Quality proparty releated values.*/
	private Vector<String> vtrQPRLS = new Vector<String>(); /** Vector to stroe the unit of measurement of the quality proparty.*/
	private Vector<String> vtrQPRSH = new Vector<String>();	/** Hash table for temporary storage of the remarks & associated code.*/
	private Hashtable<String,String> hstUOMCD = new Hashtable<String,String>();
	String strISOD1="",strISOD2="",strISOD3="";
	qc_rpfpa()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Date"),4,3,1,.7,this,'R');
			add(txtDATE = new TxtDate(),4,4,1,1,this,'L');			
			add(new JLabel("Remark1"),5,3,1,.7,this,'R');
			add(txtRMRK1 = new TxtLimit(200),5,4,1,3.5,this,'L');						
			add(new JLabel("Remark2"),6,3,1,.7,this,'R');
			add(txtRMRK2 = new TxtLimit(200),6,4,1,3.5,this,'L');			
			add(new JLabel("Remark3"),7,3,1,.7,this,'R');
			add(txtRMRK3 = new TxtLimit(200),7,4,1,3.5,this,'L');			
			add(new JLabel("Remark4"),8,3,1,.7,this,'R');
			add(txtRMRK4 = new TxtLimit(200),8,4,1,3.5,this,'L');
			
			vtrQPRDS = new Vector<String>();
			vtrQPRLS = new Vector<String>();
			vtrQPRSH = new Vector<String>();			
			M_pnlRPFMT.setVisible(true);
						
			M_strSQLQRY = "Select QS_QPRCD,QS_UOMCD from CO_QSMST where isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="",L_strUOMCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
						hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));						
				}
			}			
			setENBL(false);						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Method to enable & disable the Components according to requriements
	 * @param P_flgSTAT boolean argument to pass the state of the variable.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)		
			cl_dat.M_btnSAVE_pbst.setEnabled(false);					
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter the Date..",'N');				
				setENBL(true);
				if(txtDATE.getText().trim().length() == 0)
				{
					txtDATE.setText(cl_dat.M_strLOGDT_pbst);
					txtRMRK1.setText("");
					txtRMRK2.setText("");
					txtRMRK3.setText("");
					txtRMRK4.setText("");
				}
				txtDATE.requestFocus();
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			txtDATE.setEnabled(true);
			txtDATE.setText(cl_dat.M_strLOGDT_pbst);
			txtDATE.requestFocus();
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;			
		}
		else if(M_objSOURC == txtDATE)
		{
			try
			{
				if (txtDATE.getText().trim().length()>0)
				{
					String L_strDATE = txtDATE.getText().trim();
					this.setCursor(cl_dat.M_curWTSTS_pbst);					
					txtRMRK1.setText("");
					txtRMRK2.setText("");
					txtRMRK3.setText("");
					txtRMRK4.setText("");									
					strRMRK1="";
					strRMRK2="";
					strRMRK3="";
					strRMRK4="";
					
					M_strSQLQRY ="Select RM_REMDS, RM_TSTTP from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = "
					+ "'"+ M_strSBSCD.substring(2,4).trim()+ "'"
					+ " AND RM_TSTTP like " + "'FPA%'"
					+ " AND RM_TSTNO = " + "'"+L_strDATE.substring(0,2).trim()+L_strDATE.substring(3,5).trim()+L_strDATE.substring(6).trim()+"'";													
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						String L_strTSTTP="";
						while(M_rstRSSET.next())
						{
							L_strTSTTP = nvlSTRVL(M_rstRSSET.getString("RM_TSTTP"),"");
							if(!L_strTSTTP.equals(""))
							{	
								if(L_strTSTTP.equals("FPA1"))
								{
									strRMRK1 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK1.setText(strRMRK1);
								}
								else if(L_strTSTTP.equals("FPA2"))
								{
									strRMRK2 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK2.setText(strRMRK2);
								}
								else if(L_strTSTTP.equals("FPA3"))
								{
									strRMRK3 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK3.setText(strRMRK3);
								}
								else if(L_strTSTTP.equals("FPA4"))
								{
									strRMRK4 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK4.setText(strRMRK4);
								}
							}
						}						
					}															
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					setMSG("Enter Remark4 & Prass Enter Key..",'N');
					txtRMRK1.requestFocus();
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					txtDATE.setEnabled(false);
				}
				else			
					setMSG("Please Enter Date to generate the Report..",'N');
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == txtDATE");
			}
		}
		else if(M_objSOURC == txtRMRK1)
		{
			setMSG("Enter Remark 2 & Prass Enter Key..",'N');
			txtRMRK2.requestFocus();
		}
		else if(M_objSOURC == txtRMRK2)
		{
			setMSG("Enter Remark 3 & Prass Enter Key..",'N');
			txtRMRK3.requestFocus();
		}
		else if(M_objSOURC == txtRMRK3)
		{
			setMSG("Enter Remark 4 & Prass Enter Key..",'N');
			txtRMRK4.requestFocus();
		}
		else if(M_objSOURC == txtRMRK4)
		{
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}		
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		cl_dat.M_flgLCUPD_pbst = true;
		if(!vldDATA())
			return;					
		try
		{
			if(M_rdbHTML.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rpfpa.html";
			else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpfpa.doc";							
			getDATA();			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Finished product Analysis"," ");
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
    *Method to fetch data from MM_GRMST,CO_CTMST & MM_BETRN tables & club it with Header &
    *footer in DataOutputStream.
	*/
	private void getDATA()
	{ 							
		String L_strTSTDT="",L_strOLDLN="",L_strNEWLN="";
		String L_strFMDAT="",L_strTODAT="",L_strQPFLD ="",L_strQPRVL ="";						
		Timestamp L_tmsTMPDT;				
		
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
				setMSG("For HTML Report Printing Please insert 120 column Page..",'N');
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Finished product Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}						
			prnHEADER();			
			L_strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim()+" 07:00"));
			try
			{				
				M_calLOCAL.setTime(M_fmtLCDAT.parse(txtDATE.getText().trim()));
				M_calLOCAL.add(java.util.Calendar.DATE,-1);																       			
				L_strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(M_fmtLCDAT.format(M_calLOCAL.getTime()) +" 07:00"));
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX, "calRQDAT");
				return;
			}	
			
			StringBuffer L_stbTEMP= new StringBuffer();
			for(int i=0;i<vtrQPRLS.size();i++)
			{						
				L_strQPFLD = "PS_" + (String)(vtrQPRLS.elementAt(i))+ "VL";				
				L_stbTEMP.append(L_strQPFLD);
				L_stbTEMP.append(",");
			}									
			
			StringBuffer L_stbSQLQRY = new StringBuffer("Select PS_TSTDT," +L_stbTEMP.toString()+"LT_LINNO");
			L_stbSQLQRY.append(" From QC_PSMST,PR_LTMST where PS_CMPCD = LT_CMPCD and PS_LOTNO = LT_LOTNO AND PS_RCLNO = LT_RCLNO AND PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_TSTDT");
			L_stbSQLQRY.append(" Between '" + L_strFMDAT + "' AND '"+ L_strTODAT + "' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(LT_STSFL,'') <> 'X'");
			L_stbSQLQRY.append(" AND isnull(PS_STSFL,'') <> 'X' AND PS_RCLNO = '"+strINRCL_fn.trim()+ "'");
			L_stbSQLQRY.append(" AND PS_TSTTP ='0101' AND PS_QCATP ='"+M_strSBSCD.substring(2,4)+"'");
			L_stbSQLQRY.append(" Order by LT_LINNO,PS_TSTDT");						
			
			M_rstRSSET = cl_dat.exeSQLQRY(L_stbSQLQRY.toString());					
			if(M_rstRSSET != null)
			{							
				StringBuffer L_stbTSTVL = new StringBuffer();
				while(M_rstRSSET.next())
				{									
					L_strNEWLN = M_rstRSSET.getString("LT_LINNO");
					if(!L_strNEWLN.equals(L_strOLDLN))
					{
						dosREPORT.writeBytes("\n"+padSTRING('R',"Line No:",10));
						dosREPORT.writeBytes(padSTRING('R',L_strNEWLN,5));							
						dosREPORT.writeBytes("\n" + padSTRING('R',"----------------",15)+"\n");							
				        cl_dat.M_intLINNO_pbst += 2;					            
						L_strOLDLN = L_strNEWLN;						
					}
					
					L_stbTSTVL.delete(0,L_stbTSTVL.toString().length());					
					L_strTSTDT="";
					L_tmsTMPDT = M_rstRSSET.getTimestamp("PS_TSTDT");
					if(L_tmsTMPDT!= null)
						L_strTSTDT = M_fmtLCDTM.format(L_tmsTMPDT);					
					L_stbTSTVL.append((padSTRING('R',L_strTSTDT.substring(11,16),5)));
					for(int i=0;i<vtrQPRLS.size();i++)
					{						
						L_strQPFLD = "PS_" + (String)(vtrQPRLS.elementAt(i))+ "VL";
						L_strQPRVL = M_rstRSSET.getString(L_strQPFLD);
						if(L_strQPRVL !=null && !L_strQPRVL.trim().equals(""))
						{
							if(Double.valueOf(L_strQPRVL).doubleValue()!=0.0)							
								L_stbTSTVL.append(padSTRING('L',setNumberFormat(Double.valueOf(L_strQPRVL).doubleValue() ,arrCOLDC[i]),arrCOLWD[i]));							
							else
								L_stbTSTVL.append(padSTRING('L',"-",arrCOLWD[i]));							
						}
						else					
							L_stbTSTVL.append(padSTRING('L',"-",arrCOLWD[i]));					
					}				
					dosREPORT.writeBytes(L_stbTSTVL.toString()+"\n");
					cl_dat.M_intLINNO_pbst += 1;
					
					if(cl_dat.M_intLINNO_pbst>58)
					{						
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();											
					}												
				}	
				M_rstRSSET.close();
			}			
			dosREPORT.writeBytes("\n" + stbDOTLN.toString() + "\n");						
			dosREPORT.writeBytes("Remarks :-\n");			
			cl_dat.M_intLINNO_pbst += 3;			
			if(txtRMRK1.getText().length()> 0)
			{
				dosREPORT.writeBytes(padSTRING('R'," ",10));	
				dosREPORT.writeBytes(txtRMRK1.getText().trim());
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}			
			if(txtRMRK2.getText().length()> 0)
			{
				dosREPORT.writeBytes(padSTRING('R'," ",10));	
				dosREPORT.writeBytes(txtRMRK2.getText().trim());
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;				
			}			
			if(txtRMRK3.getText().length()> 0)
			{
				dosREPORT.writeBytes(padSTRING('R'," ",10));	
				dosREPORT.writeBytes(txtRMRK3.getText().trim());
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;				
			}
			if(txtRMRK4.getText().length()> 0)
			{
				dosREPORT.writeBytes(padSTRING('R'," ",10));	
				dosREPORT.writeBytes(txtRMRK4.getText().trim());
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			if(!strRMRK1.equals(txtRMRK1.getText().trim()))
			{			
				if(strRMRK1.equals(""))
					insRPTRM("QC_RPFPA",txtDATE.getText().trim(),"1",txtRMRK1.getText().trim());
				else
					updRPTRM("QC_RPFPA",txtDATE.getText().trim(),"1",txtRMRK1.getText().trim());
			}
			if(!strRMRK2.equals(txtRMRK2.getText().trim()))
			{	 				
				if(strRMRK2.equals(""))
					insRPTRM("QC_RPFPA",txtDATE.getText().trim(),"2",txtRMRK2.getText().trim());
				else
					updRPTRM("QC_RPFPA",txtDATE.getText().trim(),"2",txtRMRK2.getText().trim());
			}
			if(!strRMRK3.equals(txtRMRK3.getText().trim()))
			{				
				if(strRMRK3.equals(""))
					insRPTRM("QC_RPFPA",txtDATE.getText().trim(),"3",txtRMRK3.getText().trim());
				else
					updRPTRM("QC_RPFPA",txtDATE.getText().trim(),"3",txtRMRK3.getText().trim());					
			}
			if(!strRMRK4.equals(txtRMRK4.getText().trim()))
			{				
				if(strRMRK4.equals(""))
					insRPTRM("QC_RPFPA",txtDATE.getText().trim(),"4",txtRMRK4.getText().trim());
				else
					updRPTRM("QC_RPFPA",txtDATE.getText().trim(),"4",txtRMRK4.getText().trim());					
			}			
			if(cl_dat.M_flgLCUPD_pbst)
			{
				cl_dat.exeDBCMT("updRPTRM");
			}			
			dosREPORT.writeBytes("\n"+padSTRING('L',"HOD(QCA)",intROWLN-15));										
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);												
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
				setMSG("For HTML Report Printing, Please insert 120 column Page..",'N');
			}			
			dosREPORT.close();
			fosREPORT.close();	
			setMSG("Report generated Successfully..",'N');
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
		try
		{
			if(txtDATE.getText().length()==0)			
			{
				setMSG("Please Enter Date to generate the Report.. ",'E');
				txtDATE.requestFocus();			
				return false;
			}
			if (M_fmtLCDAT.parse(txtDATE.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Date can not be greater than current Date.. ",'E');								
				txtDATE.requestFocus();
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
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/**
	 * Method to Generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{		
		try
		{
			cl_dat.M_intLINNO_pbst = 13;
			cl_dat.M_PAGENO += 1;					
			boolean L_flgRPSCT = true,L_flgGLCNT = true;			
			String L_strTHEAD = "",L_strQPRCD ="",L_strCHP02="",L_strSHRDS="";
			int L_intCOLDEC =0,L_intCOLWID=0,i=0;									
			if(cl_dat.M_PAGENO ==1)
			{
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
					+ " AND CMT_CGSTP ='QCXXFPA' AND isnull(CMT_STSFL,'') <> 'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
				if(M_rstRSSET != null)
				{
					String L_strTEMP="";
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
						if(!L_strTEMP.equals(""))
						{
							if(L_strTEMP.equals("DOC1"))
								strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals("DOC2"))
								strISOD2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals("DOC3"))
								strISOD3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						}					
					}
					M_rstRSSET.close();
				}				
				//getTSTDET();
				intRECCT = 0;
				vtrQPRLS.removeAllElements();
				vtrQPRDS.removeAllElements();
				vtrQPRSH.removeAllElements();
								
				M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_NMP01,CMT_NMP02,CMT_SHRDS,CMT_CHP02"
					+ " from CO_CDTRN where CMT_CGMTP ='RPT' AND CMT_CGSTP = 'QCXXFPA'"
					+ " AND isnull(CMT_STSFL,'') <> 'X' order by CMT_NCSVL";
				
				java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				M_rstRSSET = L_stat.executeQuery(M_strSQLQRY);				
				if(M_rstRSSET != null)
				{					
					while(M_rstRSSET.next())
						intRECCT ++;										
					if(intRECCT >0)
					{	
						arrCOLWD = new int[intRECCT]; 
						arrCOLDC = new int[intRECCT]; 
						arrCCSVL  = new String[intRECCT];
					}
					
					M_rstRSSET.beforeFirst();								
					while (M_rstRSSET.next())
					{						
						L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
						L_intCOLDEC = M_rstRSSET.getInt("CMT_NMP02");
						L_intCOLWID = M_rstRSSET.getInt("CMT_NMP01");
						L_strCHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"");
						L_strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"");
						arrCOLDC[i]= L_intCOLDEC;
						arrCOLWD[i]= L_intCOLWID;
						vtrQPRLS.addElement(L_strQPRCD);
						if(L_strCHP02.trim().length()>0)
							vtrQPRSH.addElement(L_strCHP02.trim());
						vtrQPRDS.addElement(L_strSHRDS);
						if(L_strSHRDS.trim().equals("RPS"))
							intRPSWD = L_intCOLWID;
						if(L_strSHRDS.equals("GL"))
							intGLWID = L_intCOLWID;
						
						//arrCCSVL[i] = nvlSTRVL(cl_dat.getPRMCOD("CMT_CCSVL","SYS","QCXXQPR",L_strQPRCD),"");						
						if(hstUOMCD.containsKey(L_strQPRCD))
							arrCCSVL[i] = hstUOMCD.get(L_strQPRCD).toString();						
						i+=1;
					}
				}
				M_rstRSSET.close();	
			}							
			StringBuffer L_stbSUBTT = new StringBuffer(padSTRING('R',"TIME",5));
			StringBuffer L_stbSUBHD = new StringBuffer(padSTRING('R'," ",5));
			StringBuffer L_stbUOMCD = new StringBuffer(padSTRING('R',"HRS ",4));
			for(i=0;i<vtrQPRDS.size();i++)
			{
				if(((String)(vtrQPRDS.elementAt(i))).equals("RPS")&& L_flgRPSCT)
				{
					L_stbSUBTT.append(padSTRING('L',(String)(vtrQPRDS.elementAt(i)),arrCOLWD[i]));
					for(int j=0;j<vtrQPRSH.size();j++)
					{
						if(j<4)
							L_stbSUBHD.append(padSTRING('L',(String)(vtrQPRSH.elementAt(j)),intRPSWD));
						else
							if(j==4)
							{
								L_stbSUBHD.append(padSTRING('L',"",50));
								L_stbSUBHD.append(padSTRING('L',(String)(vtrQPRSH.elementAt(j)),intRPSWD));
							}
							else
								L_stbSUBHD.append(padSTRING('L',(String)(vtrQPRSH.elementAt(j)),intRPSWD));
					}
					L_flgRPSCT = false;
				}
				else if(((String)(vtrQPRDS.elementAt(i))).equals("GL")&& L_flgGLCNT)
				{
					L_stbSUBTT.append(padSTRING('L',(String)(vtrQPRDS.elementAt(i)),arrCOLWD[i]));
					for(int k=4;k<vtrQPRSH.size();k++)
					{
						L_stbSUBHD.append(padSTRING('L',(String)(vtrQPRSH.elementAt(k)),intGLWID));
					}
					L_flgGLCNT = false;
				}
				else
				{
					L_stbSUBTT.append(padSTRING('L',(String)(vtrQPRDS.elementAt(i)),arrCOLWD[i]));
					if(!((String)(vtrQPRDS.elementAt(i))).equals("RPS"))
						L_stbSUBHD.append(padSTRING('L'," ",arrCOLWD[i]));
				}					
			}
			for(i=0;i<arrCCSVL.length;i++)
			{
				if(((String)(vtrQPRDS.elementAt(i))).equals("MFI"))
				{
					L_stbUOMCD.append(padSTRING('L'," ",1));
					L_stbUOMCD.append(padSTRING('C',arrCCSVL[i],arrCOLWD[i]));
					L_stbUOMCD.append(padSTRING('L'," ",2));
				}
				else if(((String)(vtrQPRDS.elementAt(i))).equals("DSP"))				
					 L_stbUOMCD.append(padSTRING('C',arrCCSVL[i],arrCOLWD[i]));				
				else if(((String)(vtrQPRDS.elementAt(i))).equals("IZO"))
				{
					L_stbUOMCD.append(padSTRING('C',arrCCSVL[i],arrCOLWD[i]));
					L_stbUOMCD.append(padSTRING('L'," ",1));
				 }
				 else if(((String)(vtrQPRDS.elementAt(i))).equals("TS"))				 
					 L_stbUOMCD.append(padSTRING('C',arrCCSVL[i],arrCOLWD[i]));				 
				 else if(((String)(vtrQPRDS.elementAt(i))).equals("EL"))				 
					 L_stbUOMCD.append(padSTRING('L',arrCCSVL[i],arrCOLWD[i]-3));				 
				 else
					L_stbUOMCD.append(padSTRING('L',arrCCSVL[i],arrCOLWD[i]));
			}	
			strHEADR = L_stbSUBTT.toString()+"\n"+L_stbSUBHD.toString()+"\n"+L_stbUOMCD.toString()+"\n";
			L_strTHEAD = L_stbSUBTT.toString();			
			intROWLN = L_strTHEAD.length(); 			
			stbDOTLN = new StringBuffer("-");
			for (i=0; i<intROWLN; i++)
				stbDOTLN.append("-");						
							
			dosREPORT.writeBytes(padSTRING('L',"-----------------------------------",intROWLN)+"\n");						
			dosREPORT.writeBytes(padSTRING('L',strISOD1,intROWLN) + "\n");			
			dosREPORT.writeBytes(padSTRING('L',strISOD2,intROWLN) + "\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD3,intROWLN) + "\n");
			dosREPORT.writeBytes(padSTRING('L',"-----------------------------------",intROWLN)+"\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("SUPREME PETROCHEM LIMITED\n");
			dosREPORT.writeBytes(padSTRING('R',"FINISHED PRODUCT ANALYSIS",intROWLN - 24)+ "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			
			//dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			dosREPORT.writeBytes(padSTRING('R',"Day ending at " + strTIME +" hrs on Date: "+txtDATE.getText().trim(),intROWLN - 24)+ "\n");
			//dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");														
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			dosREPORT.writeBytes(strHEADR);			
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}	
	/**
	 * Method to insert Remarks in the database if not already entered for the current date 
	 * @param P_strRPTNM String argument to pass Report Name.
	 * @param P_strRPTDT String argument to pass Report Date & Time.
	 * @param P_strRMSRL String argument to pass Remark Serial Number.
	 * @param P_strREMDS String	argument to pass Remark Description.
	 */
	public void insRPTRM(String P_strRPTNM,String P_strRPTDT,String P_strRMSRL,String P_strREMDS)
	{				
		try
		{
			M_strSQLQRY = "Insert into QC_RMMST(RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)values(";
			M_strSQLQRY += "'" + M_strSBSCD.substring(2,4)+"',";
			M_strSQLQRY += "'" + P_strRPTNM.substring(5).trim()+P_strRMSRL.trim()+"',";
			M_strSQLQRY += "'" + P_strRPTDT.substring(0,2)+P_strRPTDT.substring(3,5)+P_strRPTDT.substring(6)+"',";
			M_strSQLQRY += "'" + P_strREMDS.trim()+"',";
			M_strSQLQRY += getUSGDTL("LP",'I',"") + ")";									
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"insRPTRM");
		}
	}
	/**
	 * Method to update the Remarks already entered Remarks are modified  
	 * @param P_strRPTNM String argument to pass Report Name.
	 * @param P_strRPTDT String argument to pass Report Date & Time.
	 * @param P_strRMSRL String argument to pass Remark Serial Number.
	 * @param P_strREMDS String	argument to pass Remark Description.
	 */
	public void updRPTRM(String P_strRPTNM,String P_strRPTDT,String P_strRMSRL,String P_strREMDS)
	{		
		try
		{				
			if(P_strREMDS.length() > 0)
			{
				M_strSQLQRY = "Update QC_RMMST set ";
				M_strSQLQRY += " RM_REMDS = '" + P_strREMDS.trim()+"',";				
				M_strSQLQRY += " RM_LUSBY = '" + cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += " RM_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "'";
			}
			else			
				M_strSQLQRY = "Delete from QC_RMMST";
			
			M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+M_strSBSCD.substring(2,4)+"'";
			M_strSQLQRY += " AND RM_TSTTP = "+ "'"+P_strRPTNM.substring(5).trim()+P_strRMSRL.trim()+"'";
			M_strSQLQRY += " AND RM_TSTNO = "+ "'"+P_strRPTDT.substring(0,2)+P_strRPTDT.substring(3,5)+P_strRPTDT.substring(6)+"'";							
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");										
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"updRPTRM");
		}
	}
}
	