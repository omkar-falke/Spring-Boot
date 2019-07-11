/**
System Name   : Laboratoty Information Management System
Program Name  : Competitor Sample Analysis.
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 30 September 2005
Version       : LIMS V2.0.0

Modificaitons 
Modified By   : 
Modified Date : 
Modified det. : 
Version       : 
*/

import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
import javax.swing.JTable;import javax.swing.JCheckBox;

/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Competitor Sample Analysis.

Purpose : To take the Report of Competitors Sample quality parameters.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
QC_RMMST       RM_QCATP,RM_TSTTP, RM_TSTNO                             #
QC_PSMST       PS_QCATP, PS-TSTTP,PS_LOTNO,
               PS_RCLNO,PS_TSTNO,PS_TSTDT                              #
QC_SPMST       SP_SBSCD,SP_QCATP,SP_SMPTP,SP_SEFNO                     #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    PS_TSTDT       QC_PSMST      DATE          Test Date
txtTODAT    PS_TSTDT       QC_PSMST      DATE          Test Date
txtZONCD    SP_ZONCD       QC_SPMST      VARCHAR(10)   Zone Description
txtMFGBY    SP_MFGBY       QC_SPMST      VARCHAR(20)   Manufacturers Name
txtGRDDS    SP_GRDDS       QC_SPMST      VARCHAR(20)   Grade Description
--------------------------------------------------------------------------------------
<B>
Logic</B>
     Quality parameters are fetched for Test Type 0901.

<I><B>Conditions Give in Query:</b>
    Data is taken from QC_PSMST & QC_SPMST for given condiations
       1) PS_QCATP = '"+M_strSBSCD.substring(0,2)+"'";
       2) AND PS_SBSCD = Sub system Code
       3) AND PS_TSTTP = SP_SMPTP 
       4) AND PS_TSTNO = SP_TSTNO";
       5) AND PS_SBSCD = SP_SBSCD 
       6) AND SP_SMPTP = '0901'";
   if Zone Code is Specified
       7) AND SP_ZONCD = given Zone Code
   if Manufacturer is Specified
       8) AND SP_MFGBY = Specified Manufacturer.
   if Grade Decription is Specified
       9) AND SP_GRDDS = given Grade Description.
   if From Date & todate is specified
      10) AND PS_TSTDT between From date & To date.
   if Only From Date is specified 
      11) AND PS_TSTDT > From date.
   if Only To Date is specified 
      12) AND PS_TSTDT < To date.
		
<B>Validations :</B>
    - From date & To date must be valid. i.e. To date must be samller 
    then current date & greater than From Date.
</I> */

public class qc_rpcsa extends cl_rbase
{									/** JTextField to enter & display Zone Code.*/
	private JTextField txtZONCD;	/** JTextField to enter & display From Date.*/
	private JTextField txtFMDAT;	/** JTextField to enter & display To Date.*/
	private JTextField txtTODAT;	/** JTextField to enter & display Manufacturer Name.*/
	private JTextField txtMFGBY;	/** JTextField to enter & display Grade Description.*/
	private JTextField txtGRDDS;	/** JComboBox to display & select Status flag.*/
	private JComboBox cmbSTSFL;		/** JTable to display Quality parameters & Coresponding values.*/
	private cl_JTable tblQPRLS; 	/** JCheckBox to specify the Zone Code in the ordering clause while fetching data.*/
	private JCheckBox chkZONE;		/** JCheckBox to specify the Grade Description in the ordering clause while fetching data.*/
	private JCheckBox chkGRADE; 	/** JCheckBox to specify the Manufacturer Name in the ordering clause while fetching data.*/
	private JCheckBox chkMFGBY;		/** JCheckBox to specify, to include Remark in the Report.*/
	private JCheckBox chkREMRK;
	private JCheckBox chkSCLFR;
	private JCheckBox chkSPLRM;
											/** FileOutputStream for the generated Report File Handeling.*/		
	private FileOutputStream fosREPORT ;    /** DataOutputStream object to store the data as a Stream */
    private DataOutputStream dosREPORT ;	/** String Buffer Object to hold column description to generate the Report.*/
	private StringBuffer stbHEADR;			/** String Buffer Object to hold Unit of Measurement to generate the Report.*/
	private StringBuffer stbUOMCD;			/** StringBuffer object to generate the Dotted line Dynamically.*/		
	private StringBuffer stbDOTLN;			/** Integer variable to specify the number of rows in the table.*/
	private int intROWCT = 50;				/** Integer variable to count the number of Records fetched.*/				
	private int intRECCT = 0;
										/** String variable for generated Report File Name.*/
	private String strFILNM;			/** String variable to hold from date.*/
	private String strFMDAT;			/** String variable to hold To date.*/
	private String strTODAT;			/** Final Integer to represent the Check Flag Column.*/
	private final int TB1_CHKFL = 0;	/** Final Integer to represent the Quality Parameter Column.*/
	private final int TB1_QPRCD = 1;	/** Final Integer to represent the Quaility Parameter Description Column.*/
	private final int TB1_QPRDS = 2;	/** Final Integer to represent the Unit of Measurement Column.*/
	private final int TB1_UOMCD = 3;
	private final String strSPLRM ="CSSR";
	private String strCLSNM;
	private String strDTFLD;
	//private String strISODOC1="",strISODOC2="",strISODOC3="";
	qc_rpcsa()
	{
		super(2);
		try
		{
			strCLSNM = getClass().getName();			
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("From Date"),2,2,1,.8,this,'R');
			add(txtFMDAT = new TxtDate(),2,3,1,1.5,this,'L');
			add(new JLabel("TO Date"),2,5,1,.8,this,'R');
			add(txtTODAT = new TxtDate(),2,6,1,1.5,this,'L');
			
		    add(new JLabel("Status"),3,2,1,.8,this,'R');
			add(cmbSTSFL = new JComboBox(),3,3,1,2,this,'L');			
			add(new JLabel("Zone Code"),3,5,1,.8,this,'R');
			add(txtZONCD = new JTextField(),3,6,1,1.5,this,'L');									
						
			add(new JLabel("MFG By"),4,2,1,.8,this,'R');
			add(txtMFGBY = new JTextField(),4,3,1,3,this,'L');			
			add(new JLabel("Grade Desc."),5,2,1,.8,this,'R');
			add(txtGRDDS = new JTextField(),5,3,1,3,this,'L');
			
			add(new JLabel("Select Parameter for Report"),6,3,1,2.8,this,'L');
			String[] L_COLHD = {"Select","Para.Code","Description","UOM"};
      		int[] L_COLSZ = {20,50,155,50};
			tblQPRLS = crtTBLPNL1(this,L_COLHD,intROWCT,7,3,11,2.6,L_COLSZ,new int[]{0});			
			add(new JLabel("Order By"),7,6,1,1,this,'L');
			add(chkZONE = new JCheckBox("Zone"),8,6,1,1,this,'L');
			add(chkGRADE = new JCheckBox("Grade"),9,6,1,1,this,'L');
			add(chkMFGBY = new JCheckBox("MFG By"),10,6,1,1,this,'L');			
			
			add(new JLabel("Additional Fields"),12,6,1,1.5,this,'L');
			add(chkREMRK = new JCheckBox("Remarks"),13,6,1,2,this,'L');
			add(chkSCLFR = new JCheckBox("Collected From"),14,6,1,2,this,'L');
			chkSPLRM = new JCheckBox("QCA Special Remarks");
			if(strCLSNM.equals("qc_rpcs1"))//QCA login
			{
				strDTFLD = "CONVERT(varchar,PS_TSTDT,103)";
				add(chkSPLRM ,15,6,1,2,this,'L');				
			}
			else if(strCLSNM.equals("qc_rpcsa"))
				strDTFLD = "SP_SEFDT";
			
			stbHEADR = new StringBuffer();
			stbUOMCD = new StringBuffer();			
		//	cmbSTSFL.addItem("Select");
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP ='STSQCXXCSA'";
			M_strSQLQRY += " AND isnull(CMT_STSFL,'') <> 'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET != null)    			        		
			{
				while(M_rstRSSET.next())				
					cmbSTSFL.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));				
				M_rstRSSET.close();
			}			
			M_pnlRPFMT.setVisible(true);
			stbDOTLN = new StringBuffer("-");
			setENBL(false);				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Method super class method overrided to enable & disable components
	 * according to the requriement.
	 * @param P_flgSTAT boolean argument to pass state of the Component.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);		
		chkZONE.setSelected(true);
		tblQPRLS.cmpEDITR[TB1_CHKFL].setEnabled(true);
		tblQPRLS.cmpEDITR[TB1_QPRCD].setEnabled(false);
		tblQPRLS.cmpEDITR[TB1_QPRDS].setEnabled(false);
		tblQPRLS.cmpEDITR[TB1_UOMCD].setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			try
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{
					setMSG("Please Enter the Date..",'N');				
					setENBL(true);					
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
					if(tblQPRLS.getValueAt(1,TB1_QPRCD).toString().length()>0)
						return;
					String L_strSQLQRY = "Select CMT_CODCD,CMT_CHP01,CMT_CHP02,QS_QPRDS,QS_UOMCD,QS_TSMCD from CO_QSMST,CO_CDTRN";
					L_strSQLQRY += " where isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD = QS_QPRCD ";
					L_strSQLQRY += " AND CMT_CGMTP ='SYS' AND CMT_CGSTP = 'QCXXCSA' ";
					if(strCLSNM.equals("qc_rpcsa"))
						L_strSQLQRY += " AND CMT_CHP01 = 'Y'";					
					L_strSQLQRY += " order by CMT_CHP02";
					
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(M_rstRSSET != null)
					{										
						int L_intQPRCT = 0;
						tblQPRLS.clrTABLE();
						String L_strTEMP= "";
						while (M_rstRSSET.next())
						{													
							L_strTEMP = M_rstRSSET.getString("CMT_CODCD");
							tblQPRLS.setValueAt(L_strTEMP,L_intQPRCT,TB1_QPRCD);
							tblQPRLS.setValueAt(M_rstRSSET.getString("QS_QPRDS"),L_intQPRCT,TB1_QPRDS);
						    tblQPRLS.setValueAt(M_rstRSSET.getString("QS_UOMCD"),L_intQPRCT,TB1_UOMCD);
						    L_intQPRCT ++;									
						}
						M_rstRSSET.close();
					}		
				}								
				else
				{
					setMSG("Please Select an Option..",'N');
					setENBL(false);	
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"");
			}
		}					
		if(M_objSOURC == cmbSTSFL) 
		{
			txtZONCD.requestFocus();
			setMSG("Please Enter Zone Code to generate Report",'N');
		}		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);       
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{      
				String L_strDATE = "";
				if((M_objSOURC == txtZONCD) ||(M_objSOURC == txtMFGBY)||(M_objSOURC == txtGRDDS))
				{
					if(txtFMDAT.getText().trim().length()>0)
						strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					else
						strFMDAT ="";
					if(txtTODAT.getText().trim().length()>0)
						strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
					else
						strTODAT="";
					if((strFMDAT.length()>0) &&(strTODAT.length()>0))
						L_strDATE = " AND "+ strDTFLD +" between '"+ strFMDAT+ "' AND '"+ strTODAT+"'";
					else if(strFMDAT.length()>0)
						L_strDATE = " AND "+ strDTFLD +" >= '"+ strFMDAT +"'";
					else if(strTODAT.length()>0)
						L_strDATE = " AND "+ strDTFLD +" <= '"+ strTODAT+"'";
				}
				if(M_objSOURC == txtZONCD)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtZONCD";
					String[] L_arrTBHDR = {"Zone Code"};
					M_strSQLQRY =  " Select distinct SP_ZONCD from QC_SPMST,QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '"+M_strSBSCD.substring(0,2)+"'";
					M_strSQLQRY += " AND PS_SBSCD ='"+ M_strSBSCD+"' AND PS_CMPCD = SP_CMPCD AND PS_TSTTP = SP_SMPTP AND PS_TSTNO = SP_TSTNO";					
					if(cmbSTSFL.getSelectedIndex() != 0)
						M_strSQLQRY += " AND SP_STSFL ='"+ cmbSTSFL.getSelectedItem().toString().substring(0,1)+"'";
					if(txtZONCD.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_ZONCD like '"+txtZONCD.getText().trim().toUpperCase()+"%'";
					M_strSQLQRY += L_strDATE;
					
					if(txtGRDDS.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_GRDDS ='"+ txtGRDDS.getText().trim() +"'";
					if(txtMFGBY.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_MFGBY ='"+ txtMFGBY.getText().trim()+"'";
					M_strSQLQRY += " Order by SP_ZONCD";					
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,1,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtMFGBY)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMFGBY";				
					String[] L_arrTBHDR = {"Manufacturer Name"};
					M_strSQLQRY =  "Select distinct SP_MFGBY from QC_SPMST,QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '"+M_strSBSCD.substring(0,2)+"'";
					M_strSQLQRY += " AND PS_SBSCD ='"+ M_strSBSCD+"' AND PS_CMPCD = SP_CMPCD and PS_TSTTP = SP_SMPTP AND PS_TSTNO = SP_TSTNO";					
					if(cmbSTSFL.getSelectedIndex() != 0)
						M_strSQLQRY += " AND SP_STSFL ='"+ cmbSTSFL.getSelectedItem().toString().substring(0,1)+"'";
					if(txtZONCD.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_ZONCD ='"+txtZONCD.getText().trim()+"'";
					M_strSQLQRY += L_strDATE;
					
					if(txtGRDDS.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_GRDDS ='"+ txtGRDDS.getText().trim() +"'";
					if(txtMFGBY.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_MFGBY like '"+ txtMFGBY.getText().trim().toUpperCase() +"%'";
					M_strSQLQRY += " Order by SP_MFGBY";					
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,1,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtGRDDS)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtGRDDS";					
					String[] L_arrTBHDR = {"Grade Descrition"};
					M_strSQLQRY =  "Select distinct SP_GRDDS from QC_SPMST,QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '"+M_strSBSCD.substring(0,2)+"'";
					M_strSQLQRY += " AND PS_SBSCD ='"+ M_strSBSCD+"' AND PS_CMPCD = SP_CMPCD and PS_TSTTP = SP_SMPTP AND PS_TSTNO = SP_TSTNO";
					if(cmbSTSFL.getSelectedIndex() != 0)
						M_strSQLQRY += " AND SP_STSFL ='"+ cmbSTSFL.getSelectedItem().toString().substring(0,1)+"'";
					M_strSQLQRY += L_strDATE;
					if(txtMFGBY.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_MFGBY = '"+ txtMFGBY.getText().trim() +"'";
					if(txtGRDDS.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_GRDDS like '"+ txtGRDDS.getText().trim().toUpperCase() +"%'";					
					M_strSQLQRY += " Order by SP_GRDDS";					
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,1,"CT");					
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"F1 Help..");                            
			}
		}
		if (L_KE.getKeyCode()== L_KE.VK_ENTER)
        {	
			try
			{
				if(M_objSOURC == txtZONCD)
				{						
					txtMFGBY.requestFocus();
					/*if(txtFMDAT.getText().trim().length()==0)
					{
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);																
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
					}*/
					setMSG("Please Enter Manufacturer Name, to generate report..",'N');
				}						
				else if(M_objSOURC == txtFMDAT)
				{
					txtTODAT.requestFocus();
					if(txtTODAT.getText().trim().length()==0)					
		   				txtTODAT.setText(cl_dat.M_strLOGDT_pbst.trim());					
					setMSG("Please Enter Date to specify date Range..",'N');
				}
				else if(M_objSOURC == txtTODAT)
				{
					//txtMFGBY.requestFocus();
					txtZONCD.requestFocus();
					setMSG("Please Enter Zone code to generate the report..",'N');
				}
				else if(M_objSOURC == txtMFGBY)
				{
					txtGRDDS.requestFocus();
					setMSG("Please Enter Grade to generate report..",'N');
				}
				else if(M_objSOURC == txtGRDDS)				
					cl_dat.M_btnSAVE_pbst.requestFocus();				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == txtZONCD");
			}
		}
	}
	/**
	 * Super class method to execute the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();		
		if(M_strHLPFLD.equals("txtZONCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtZONCD.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD.equals("txtMFGBY"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtMFGBY.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD.equals("txtGRDDS"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtGRDDS.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
	/**
	* Method to print, display report as per selection
	*/
	void exePRINT()
	{		
		try
		{
			if(!vldDATA())
				return;							
			if(M_rdbHTML.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpcsa.html";
			else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpcsa.doc";							
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Competitors Sample Analysis Report"," ");
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
    *Method to fetch data from Qc_SPMST & QC_PSMST tables & club it with Header &
    *footer in DataOutputStream.
	*/
	private void getDATA()
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
		{			
			String L_strZONCD="",L_strOZONCD="",L_strMFGBY="",L_strQPRCD="",L_strQPRDS="",L_strREMDS="",L_strTSTNO="";
			String L_strGRDDS= "",L_strDATE="";
			String L_strSQLQRY="";
			String L_strDATA="";
			ResultSet L_rstRSSET;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Finished product Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}						
			prnHEADER();

			if(txtFMDAT.getText().trim().length() > 0)
				//strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim()+" 07:00"));
				strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			else
				strFMDAT ="";
			if(txtTODAT.getText().trim().length() > 0)
				//strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" 07:00"));
				strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			else
				strTODAT="";						
			if((strFMDAT.length()>0) && (strTODAT.length()>0))
				L_strDATE = " AND "+ strDTFLD +" between '"+ strFMDAT+ "' AND '"+ strTODAT+"'";
			else if(strFMDAT.length()>0)
				L_strDATE = " AND "+ strDTFLD +" >= '"+ strFMDAT +"'";
			else if(strTODAT.length()>0)
				L_strDATE = " AND "+ strDTFLD +" <= '"+ strTODAT+"'";			
			M_strSQLQRY = "Select ";
			for(int i=0;i<intROWCT;i++)
			{						
				L_strDATA="";
				if(tblQPRLS.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY +="PS_"+tblQPRLS.getValueAt(i,TB1_QPRCD).toString()+"VL,";
				}
			}
			M_strSQLQRY += "SP_ZONCD,SP_MFGBY,SP_GRDDS,SP_TSTNO,SP_SCLFR From QC_SPMST,QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '"+M_strSBSCD.substring(0,2)+"'";
			M_strSQLQRY += " AND PS_SBSCD ='"+ M_strSBSCD+"' AND PS_CMPCD = SP_CMPCD and PS_TSTTP = SP_SMPTP AND PS_TSTNO = SP_TSTNO";
			M_strSQLQRY += L_strDATE;
			M_strSQLQRY += " AND PS_SBSCD = SP_SBSCD AND SP_SMPTP = '0901'";
			if(txtZONCD.getText().trim().length()>0)
				M_strSQLQRY += " AND SP_ZONCD = '"+ txtZONCD.getText().trim() +"'";
			if(txtMFGBY.getText().trim().length()>0)
				M_strSQLQRY += " AND SP_MFGBY = '"+ txtMFGBY.getText().trim() +"'";
			if(txtGRDDS.getText().trim().length()>0)
				M_strSQLQRY += " AND SP_GRDDS = '"+ txtGRDDS.getText().trim() +"'";
			if(cmbSTSFL.getSelectedIndex() != 0)
				M_strSQLQRY += " AND SP_STSFL ='"+ cmbSTSFL.getSelectedItem().toString().substring(0,1)+"'";
			String L_strTEMP = "";
			if((chkZONE.isSelected())||(chkGRADE.isSelected()) ||(chkMFGBY.isSelected()))
			{						
					L_strTEMP = " Order by";
				if(chkZONE.isSelected())
				    L_strTEMP += " SP_ZONCD";
				if(chkGRADE.isSelected())
				{
					if(L_strTEMP.length()>17)
						L_strTEMP += ",";
					L_strTEMP += " SP_GRDDS";
				}
				if(chkMFGBY.isSelected())
				{
					if((L_strTEMP.length()>26) ||(L_strTEMP.equals(" Order by SP_ZONCD")))
						L_strTEMP += ",";
					L_strTEMP += " SP_MFGBY";
				}					
			}
			M_strSQLQRY += L_strTEMP;
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				StringBuffer stbDATA = new StringBuffer();				
				while(M_rstRSSET.next())
				{
					stbDATA.delete(0,stbDATA.length());						
					L_strZONCD = nvlSTRVL(M_rstRSSET.getString("SP_ZONCD"),"");
					L_strMFGBY = nvlSTRVL(M_rstRSSET.getString("SP_MFGBY"),"");					
					L_strGRDDS = nvlSTRVL(M_rstRSSET.getString("SP_GRDDS"),"");
					if(L_strMFGBY.trim().length() >= 20)
						L_strMFGBY = L_strMFGBY.substring(0,18);
					if(!L_strOZONCD.equals(L_strZONCD))
					{
						stbDATA.append(padSTRING('R',L_strZONCD,10)+"\n");
						cl_dat.M_intLINNO_pbst++;
						stbDATA.append(padSTRING('R',"",3));
						L_strOZONCD = L_strZONCD;
					}
					else
						stbDATA.append(padSTRING('R',"",3));
					stbDATA.append(padSTRING('R',L_strGRDDS,20));
					stbDATA.append(padSTRING('R',L_strMFGBY,20));					
					if(chkSCLFR.isSelected())
					{
						L_strMFGBY = nvlSTRVL(M_rstRSSET.getString("SP_SCLFR"),"");
						if(L_strMFGBY.length() >= 20)
							L_strMFGBY = L_strMFGBY.substring(0,20);
						stbDATA.append(padSTRING('R',L_strMFGBY,22));
					}
					for(int i=0;i<intROWCT;i++)
					{						
						L_strDATA="";
						if(tblQPRLS.getValueAt(i,TB1_CHKFL).toString().equals("true"))
						{															
							L_strQPRCD = "PS_"+tblQPRLS.getValueAt(i,TB1_QPRCD).toString()+"VL";							
							L_strDATA = nvlSTRVL(M_rstRSSET.getString(L_strQPRCD),"");							
							stbDATA.append(padSTRING('L',L_strDATA,10));
						}							
					}
					L_strTSTNO = nvlSTRVL(M_rstRSSET.getString("SP_TSTNO"),"");	
					
					if(chkREMRK.isSelected())
					{						
						L_strSQLQRY = "Select RM_REMDS from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"+ M_strSBSCD.substring(0,2) +"'";
						L_strSQLQRY +=" AND isnull(RM_STSFL,'')<>'X' AND RM_TSTTP = '0901' AND RM_TSTNO = '"+ L_strTSTNO+"'";
						L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
						L_strREMDS = "";
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							   L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
							L_rstRSSET.close();					
						}
						if(L_strREMDS.length()> stbDOTLN.length()-22)
						{
							L_strREMDS = L_strREMDS.substring(0,stbDOTLN.length()-22)+"\n"
										 +"                   -"+L_strREMDS.substring(stbDOTLN.length()-22);
							cl_dat.M_intLINNO_pbst++;
						}
						stbDATA.append("\n"+"     Remark         : " + L_strREMDS);
						cl_dat.M_intLINNO_pbst++;
					}
					if((chkSPLRM.isSelected() && (strCLSNM.equals("qc_rpcs1"))))
					{						
						L_strSQLQRY = "Select RM_REMDS from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"+ M_strSBSCD.substring(0,2) +"'";
						L_strSQLQRY +=" AND RM_TSTTP = '"+ strSPLRM +"' AND RM_TSTNO = '"+ L_strTSTNO+"'";						
						L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
						L_strREMDS = "";
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							   L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
							L_rstRSSET.close();					
						}						
						if(L_strREMDS.length()> stbDOTLN.length()-22)
						{
							L_strREMDS = L_strREMDS.substring(0,stbDOTLN.length()-22)+"\n"
										 +"                   -"+L_strREMDS.substring(stbDOTLN.length()-22);
							cl_dat.M_intLINNO_pbst++;
						}
						stbDATA.append("\n"+"     Special Remark : " + L_strREMDS);
						cl_dat.M_intLINNO_pbst++;
					}					
					
					stbDATA.append("\n");
					dosREPORT.writeBytes(stbDATA.toString());
					cl_dat.M_intLINNO_pbst++;
					if(cl_dat.M_intLINNO_pbst > 66)
					{				
						dosREPORT.writeBytes(stbDOTLN.toString());
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}					
					intRECCT = 1;
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			
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
		}
		catch(Exception L_EX)
		{			
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{		
		try
		{
			cl_dat.M_intLINNO_pbst = 13;
			cl_dat.M_PAGENO ++;
			String L_strTEMP = "";
			if(cl_dat.M_PAGENO == 1)
			{
				stbHEADR.delete(0,stbHEADR.length());
				stbUOMCD.delete(0,stbUOMCD.length());
				stbHEADR.append("Zone" +"\n");
				stbHEADR.append("   Grade               MFG By              ");
				if(chkSCLFR.isSelected())
				{
					stbHEADR.append("Collected From        ");
					stbUOMCD.append(padSTRING('R',"",22));
				}
				stbUOMCD.append(padSTRING('R',"",43));
				for(int i=0;i<intROWCT;i++)
				{
					if(tblQPRLS.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						L_strTEMP = tblQPRLS.getValueAt(i,TB1_QPRCD).toString();
						if(L_strTEMP.endsWith("_"))
							L_strTEMP = L_strTEMP.substring(0,2);
						stbHEADR.append(padSTRING('L',L_strTEMP,10));
						stbUOMCD.append(padSTRING('L',tblQPRLS.getValueAt(i,TB1_UOMCD).toString(),10));
					}
				}
				int L_intROWLN = 0;
				if(stbHEADR.length() < 90)
				   L_intROWLN = 90;
				else
					L_intROWLN =stbHEADR.length()-4;
				stbDOTLN.delete(0,stbDOTLN.length());
				for(int i =0; i<L_intROWLN; i++)
					stbDOTLN.append("-");
			}			
			dosREPORT.writeBytes("\n\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,stbDOTLN.length() - 21));
			dosREPORT.writeBytes("Date    : " + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Competitors Sample Analysis Report",stbDOTLN.length() - 21));
			dosREPORT.writeBytes("Page No.: " + cl_dat.M_PAGENO + "\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			dosREPORT.writeBytes(stbHEADR.toString()+"\n");			
			dosREPORT.writeBytes(stbUOMCD.toString()+"\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	Method to validate Inputs before Execuation of SQL Query.
	*/
	boolean vldDATA()
	{
		try
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;
			if(txtFMDAT.getText().length() == 10)
			{
				if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Date must be smaller than current Date.. ",'E');
					txtFMDAT.requestFocus();
					return false;
				}
			}
			if(txtTODAT.getText().length() == 10)
			{
				if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Date must be smaller than current Date.. ",'E');
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
			//if no row selected.			
			boolean L_flgCHKFL= false;
			int L_intCOLCT = 0;
			for(int i=0; i<tblQPRLS.getRowCount(); i++)
			{			
				if(tblQPRLS.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{					
					L_flgCHKFL= true;
					break;
				}
			}
			if(L_flgCHKFL== false)
			{
				setMSG("No Quality parameter is Selected..",'E');
				return false;			
			}
			int L_intCOLWD = 0;
			for(int i=0; i<tblQPRLS.getRowCount(); i++)
			{		
				if(tblQPRLS.getValueAt(i,TB1_CHKFL).toString().equals("true"))				
					L_intCOLCT ++;
				if(M_rdbTEXT.isSelected())				
					L_intCOLWD = 13;
				if(M_rdbHTML.isSelected())				
					L_intCOLWD = 10;
				if(L_intCOLCT >L_intCOLWD)
				{
					setMSG("To many Quality Parameters selected..",'E');
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
}