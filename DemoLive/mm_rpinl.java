/*
System Name	  : Material Management System
Program Name  : Indent Status
Program Desc. : Print diffent type of indent reports 
Author        : Mr S.R.Mehesare
Date          : 22/10/2005
Version       : MMS v2.0.0
*/

import javax.swing.JPanel;import javax.swing.JComponent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;import java.awt.event.MouseEvent;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.Color;import java.sql.Time;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Vector;import java.util.Hashtable;
import javax.swing.JComboBox;import javax.swing.JTextField;import javax.swing.JLabel;
/**<pre>
System Name : Material Management System.
 
Program Name : Indent Status

Purpose : Program Print diffent type of indent reports.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_INMST       IN_MMSBS,IN_STRTP,IN_INDNO,IN_MATCD                     #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD            #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name      Table name      Type/Size       Description
--------------------------------------------------------------------------------------
cmbRPTOP                                                     Report Option
cmbINDTP                                                     Indent Type
txtDPTCD    IN_DPTCD         MM_INMST        VARCHAR(3)      Department Code
txtDPTNM    CMT_CODDS        CO_CDTRN        VARCHAR(30)     Department Name
txtUOMCD    ST_UOMCD         MM_STMST        VARCHAR(3)      Unit of Measurement
txtINDQT    ST_STKIN         MM_STMST        DECIMAL(12,3)   Indent Quantity.
txtORDQT    ST_STKOR         MM_STMST        DECIMAL(12,3)   Ordered Quantity.
txtSTKQT    ST_STKQT         MM_STMST        DECIMAL(12,3)   Stock Quantity.
txtPPONO    PO_PORNO         MM_POMST        VARCHAR(8)      Purchase Order Number
txtPPODT    PO_PORDT         MM_POMST        DATE            Purchase Order Date
txtPPOQT    PO_PORQT         MM_POMST        DECIMAL(12,3)   Purchase Order Quantity.
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
For different Specified Report options Data is fetched for given condiations as :-
<b> 1) For Indents not Authorised Data is taken from CO_CTMST,MM_INMST and MM_STMST for :-</b>
      1) LEFT OUTER JOIN MM_STMST ON IN_STRTP = ST_STRTP 
      2) AND IN_MATCD = ST_MATCD 
      3) WHERE CT_MATCD = IN_MATCD 
      4) AND IN_STRTP= Specified Store Type 
      5) AND isnull(IN_STSFL,'') IN('0','2','3')
    if Department Code is specified.
      6) AND IN_DPTCD = Specified Department code.
      7) AND IN_INDTP = Specified Indent Type.

 <B>2) For List of indents not converted to P.O., Data is taken from MM_INMST,MM_STMST & 
   CO_CTMST for condiations as :-</B>
      1) IN_STRTP = ST_STRTP 
      2) AND IN_MATCD = ST_MATCD 
      3) AND CT_MATCD = IN_MATCD 
      4) AND IN_STRTP = Specified Store Type 
      5) AND isnull(IN_STSFL,'') = '4' "
      6) AND (isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 ";
    if Department Code is specified 
      7) AND IN_DPTCD = Specified Department Code
      8) AND IN_INDTP = Specified indent Type.				
 <B>3) For Department wise list of indents overdue data is Taken from MM_INMST,MM_STMST & CO_CTMST </B>
      1) IN_STRTP = ST_STRTP 
      2) AND IN_MATCD = ST_MATCD 
      3) AND CT_MATCD = IN_MATCD 
      4) AND IN_STRTP = Specified Store Type
      5) AND isnull(IN_STSFL,'') = '4' 
      6) AND (isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 
      7) AND (CURRENT_DATE - IN_EXPDT) > 0 
    if Department Code is specified
      8) AND IN_DPTCD = Department Code.
      9) AND IN_INDTP = Specified Indent Type
     
<B>4) For Material wise List of Indents Overdue,Data is taken from MM_INMST,MM_STMST & CO_CTMST </B>
      1) IN_STRTP = ST_STRTP 
      2) AND IN_MATCD = ST_MATCD 
      3) AND CT_MATCD = IN_MATCD 
      4) AND IN_STRTP = Specified store type
      5) AND isnull(IN_STSFL,'') = '4' 
      6) AND (isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 
      7) AND (CURRENT_DATE - IN_EXPDT) > 0 
    if Department Code is specified 
      8) AND IN_DPTCD = Specified Department Code.
      9) AND IN_INDTP = Specified Indent Type
				
<B>5)For List of Authorised Indents Data is Taken from MM_INMST,MM_STMST & CO_CDTRN </B>
  for condiation:-
      1) IN_STRTP = ST_STRTP 
      2) AND IN_MATCD = ST_MATCD 
      3) AND IN_STRTP = Specified Store Type 
      4) AND isnull(IN_STSFL,'') = '4' 
      5) AND CMT_CGMTP = 'STS' 
      6) AND CMT_CGSTP = 'MMXXIND' 
      7) AND CMT_CODCD = IN_STSFL 
      8) AND (isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 
    if Department Code is Specified 				
      9) AND IN_DPTCD = Specified Department code.
     10) AND IN_INDTP = specified Indent Type.

<B>To Display Additionl Information For selected indent with respect to that material 
code on mouse click</B>
If Indents Material Wise Selected, then Data is taken from MM_STMST & MM_POMST 
   for condiations :-    
     1) ST_MATCD = Material Code selected From the JTable
     2) AND ST_STRTP = Specified Store Type
     3) AND PO_MATCD = Selected Material code From the JTable
     4) AND PO_STRTP = Specified store Type
     5) AND PO_INDNO = selected indent Number from The Table
		
If Indents Departmetn Wise Selected, 
1) For List of authorised Indents, Pending Indents(for Printing) 
& Material Wise List of indents Overdues, Data is taken from 
MM_STMST & MM_POMST for condiations.
     1) ST_MATCD = Material Code selected From the JTable.
     2) AND ST_STRTP = Specified store type
     3) AND PO_MATCD = Material Code Selected From the JTable.
     4) AND PO_STRTP = Specified Store Type.
     5) AND PO_INDNO = Selected Indent number from The JTable.
2) For List of indents Not Authorised, Not converted to P.O. 
   & For Departmentwise List of indents Overdues.Data is taken from 
   MM_STMST for condiations :-
     1) ST_MATCD = Selected Material code From the Table 
     2) AND ST_STRTP = Specified Store Type.

<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then current Date.
</I> */

class mm_rpinl extends cl_rbase
{										/**Combobox for Report Option 	 */
	private JComboBox cmbRPTOP;			/**Text Field For Department Code */
	private JComboBox cmbINDTP;			/**Text Field For Indent Type */
	private JTextField txtDPTCD;		/**Text Field For Department Name */
	private JTextField txtDPTNM;		/**Text Field for Unit Of Measurment Code */
	private JTextField txtUOMCD;		/**Text Field For Indent Quantity	 */
	private JTextField txtINDQT;		/**Text Field For Order Quantity	 */
	private JTextField txtORDQT;		/**Text Field For Quantity on Hand	 */
	private JTextField txtSTKQT;		/**Text Field For Prv P. O. Number	 */
	private JTextField txtPPONO;		/**Text Field For P. O. Date		 */
	private JTextField txtPPODT;		/**Text Field For P.O. Qty.			 */
	private JTextField txtPPOQT;		/**JTable For List Of Indents Departmet Wise defalut True */
	private cl_JTable tblINDDP;			/**JTable For List of Indents Material Wise Default False		 */
	private cl_JTable tblINDMT;			/**panel For Table List of indents Dept Wise	 */
	private JPanel pnlTBLDP;			/**Panel For Table List Of Indents Material Wise */
	private JPanel pnlTBLMT;			/**Hash Table For Department Name		 */
	private Hashtable<String,String> hstDPTCD;			/**Hash Table for status of indents	 */	
	private Hashtable<String,String> hstSTSFL;			/**Vector to Store Indent Details  */
	private Vector<String> vtrINDDT;			/**flag to disply field  */
	private boolean flgPRINT;			/**String for report name	*/
	private String strRPTNM = "";
	private String strFILNM;			/**String for department code		*/
	private String strDPTCD="";			/**String for indent number	*/
	private String strINDNO;			/**Integer Variable for Row Count		*/
	private int intRWCNT;				/**Integer Variable for serial number	*/
	private int intSRLNO;		
										/**Object For File OutPut	 */	
	private FileOutputStream fosREPORT;	/**Object For Data Out put   */
    private DataOutputStream dosREPORT;			
										/**Final int To Represent Table Column Number	 */
	final int TBL_DPTNM = 1;			
	final int TBL_INDNO = 2;
	final int TBL_AMDNO = 3;
	final int TBL_INDDT = 4;
	final int TBL_MATCD = 5;
	final int TBL_MATDS = 6;
	final int TBL_INDQT = 7;
	final int TBL_AUTQT = 8;
	final int TBL_DOVRD = 9;
		
	mm_rpinl()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			cmbRPTOP = new JComboBox();
			cmbRPTOP.addItem("SELECT REPORT OPTION");
			cmbRPTOP.addItem("LIST OF INDENTS NOT AUTHORISED");
			cmbRPTOP.addItem("LIST OF INDENTS NOT CONVERTED TO P.O.");
			cmbRPTOP.addItem("DEPATMENTWISE LIST OF INDENTS OVERDUES");
			cmbRPTOP.addItem("MATERIALWISE LIST OF INDENTS OVERDUES");
			cmbRPTOP.addItem("LIST OF AUTHORISED INDENTS");
			cmbRPTOP.addItem("LIST OF PENDING INDENTS (FOR RECEIPT)");
			cmbINDTP = new JComboBox();
			cmbINDTP.addItem("01 REGULAR INDENTS");
			cmbINDTP.addItem("02 RATE CONTRACT REQUEST");
			
			pnlTBLDP = new JPanel(null);
			pnlTBLMT = new JPanel(null);
			
			hstDPTCD = new Hashtable<String,String>();		//Hashtable for departmetn code and name
			hstSTSFL = new Hashtable<String,String>();
			
			setMatrix(20,8);
		
			tblINDDP = crtTBLPNL1(pnlTBLDP, new String[]{"","Department Name","Ind No.","Amd.No.","Ind. Dt.","Mat. Code","Material Description","Indent Qty.","Auth. Qty."},6000,1,1,10,7.9,new int[]{20,110,70,55,75,85,190,70,65},new int[]{0});
			tblINDMT = crtTBLPNL1(pnlTBLMT, new String[]{"","Mat. Code","Material Description","Department Name","Ind No.","Amd.No.","Ind. Dt.","Indent Qty.","Auth. Qty."},6000,1,1,10,7.9,new int[]{20,75,200,110,70,55,75,70,65},new int[]{0}); 
		
			add(new JLabel("Report Name :"),2,1,1,1,this,'L');
			add(cmbRPTOP,2,2,1,2.5,this,'L');
			add(new JLabel("Indent Type :"),3,1,1,1,this,'L');
			add(cmbINDTP,3,2,1,2.5,this,'L');
			
			add(new JLabel("Department "),4,1,1,1,this,'L');
			add(txtDPTCD = new TxtNumLimit(3.0),4,2,1,1,this,'L');
			add(txtDPTNM = new JTextField(),4,3,1,3,this,'L');
			
			add(new JLabel("UOM"),17,1,1,1,this,'L');
			add(txtUOMCD = new JTextField(),17,2,1,1,this,'L');
			add(new JLabel("Qty. On Indent"),17,3,1,1,this,'L');
			add(txtINDQT = new JTextField(),17,4,1,1,this,'L');
			add(new JLabel("Qty.Ordered"),17,5,1,1,this,'L');
			add(txtORDQT = new JTextField(),17,6,1,1,this,'L');
			add(new JLabel("Qty. On Hand"),17,7,1,1,this,'L');
			add(txtSTKQT = new JTextField(),17,8,1,0.9,this,'L');
			add(new JLabel("P.O. No."),18,1,1,1,this,'L');
			add(txtPPONO = new JTextField(),18,2,1,1,this,'L');
			add(new JLabel("P.O. Date"),18,3,1,1,this,'L');
			add(txtPPODT = new JTextField(),18,4,1,1,this,'L');
			add(new JLabel("P.O. Qty."),18,5,1,1,this,'L');
			add(txtPPOQT = new JTextField(),18,6,1,1,this,'L');
		
			add(pnlTBLMT,5,1,11,8,this,'L');
			add(pnlTBLDP,5,1,11,8,this,'L');
							
			tblINDDP.addMouseListener(this);
			tblINDMT.addMouseListener(this);
		
			pnlTBLDP.setVisible(true);
			pnlTBLMT.setVisible(false);
			M_pnlRPFMT.setVisible(true);			
			
			//Method to add department code and name to combo box and add to hashtabel
			//hstDPTCD which store department code as key and department name as value
			M_strSQLQRY="SELECT  DISTINCT CMT_CODDS,CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='COXXDPT'ORDER BY CMT_CODCD";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				M_rstRSSET.close();
			}
			//Add status code and status description to hstSTSFL 
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
				 +"CMT_CGMTP = 'STS' AND CMT_CGSTP = 'MMXXIND' ORDER BY CMT_CODCD";	
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstSTSFL.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/** 
	 * Super class method overrided to enable & disable the compoonent on screen
	 */
	public void setENBL(boolean ACTION)
	{
		super.setENBL(ACTION);
		if( ACTION == true)
		{
			cmbRPTOP.setEnabled(true);
			cmbINDTP.setEnabled(true);
			txtDPTCD.setEnabled(true);
			tblINDDP.cmpEDITR[0].setEnabled(true);
			tblINDMT.cmpEDITR[0].setEnabled(true);
			M_pnlRPFMT.setEnabled(true);
			M_rdbTEXT.setEnabled(true);
			M_rdbHTML.setEnabled(true);
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			tblINDDP.clrTABLE();
			tblINDMT.clrTABLE();
			clrTEXTFLD();
			txtDPTNM.setText("");
			txtDPTCD.setText("");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() >0)
			{
				txtDPTCD.requestFocus();
				setENBL(true);
			}
			else
				setENBL(false);
		}
		//Action On Selection of Report Option 
		if(M_objSOURC == cmbRPTOP)
		{
			tblINDDP.clrTABLE();
			tblINDMT.clrTABLE();
			clrTEXTFLD();
			txtDPTNM.setText("");
			txtDPTCD.setText("");
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				strRPTNM = "LIST OF INDENTS NOT AUTHORISED";
				pnlTBLDP.setVisible(true);
				pnlTBLMT.setVisible(false);
			}
			if(cmbRPTOP.getSelectedIndex() == 2)
			{
				strRPTNM = "LIST OF INDENTS NOT CONVERTED TO P.O.";
				pnlTBLDP.setVisible(true);
				pnlTBLMT.setVisible(false);
			}
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
				strRPTNM = "DEPATMENTWISE LIST OF INDENTS OVERDUES ";
				pnlTBLDP.setVisible(true);
				pnlTBLMT.setVisible(false);
			}
			if(cmbRPTOP.getSelectedIndex() == 4)
			{
				strRPTNM = "MATERIALWISE LIST OF INDENTS OVERDUES ";
				pnlTBLDP.setVisible(false);
				pnlTBLMT.setVisible(true);
			}
			if(cmbRPTOP.getSelectedIndex() == 5)
			{
				strRPTNM = "LIST OF AUTHORISED INDENTS";
				pnlTBLDP.setVisible(true);
				pnlTBLMT.setVisible(false);
			}
			if(cmbRPTOP.getSelectedIndex() == 6)
			{
				strRPTNM = "LIST OF PENDING INDENTS (FOR RECEIPT)";
				pnlTBLDP.setVisible(false);
				pnlTBLMT.setVisible(false);
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == cmbRPTOP)
				setMSG("Select Report Option ..",'N');
			if(M_objSOURC == txtDPTCD)
				setMSG("Enter Department Code Or Press 'F1' For Help Or Blank For All Department ..",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"FocusGained");
		}
	}			
	public void keyPressed(KeyEvent L_KE)
	{		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtDPTCD)
			{
				M_strHLPFLD = "txtDPTCD";
				M_strSQLQRY="SELECT  DISTINCT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
					+"CMT_CGMTP='SYS' AND CMT_CGSTP='COXXDPT' ";
				if(txtDPTCD.getText().trim().length() > 0 )
					M_strSQLQRY += "AND DMT_CODCD LIKE '"+txtDPTCD.getText().trim()+"%' ";
				M_strSQLQRY += " AND isnull(CMT_STSFL,'') <> 'X' ORDER BY CMT_CODCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code","Name"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtDPTCD)
			{
				getRECRD();		//call method to display record in table 
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	//To Display Additionl Info For selected indent w.r.t that material code on mouse click
	public void mouseClicked(MouseEvent L_ME)
	{
		super.mouseClicked(L_ME);
		try
		{
			if(L_ME.getSource() == tblINDMT)	//If Indents Material Wise Selected
			{	
				clrTEXTFLD();
				if(tblINDMT.getSelectedColumn() == 0)
				{
					int L_intSELROW = tblINDMT.getSelectedRow();
					for(int i=0;i<tblINDMT.getRowCount();i++)
					{
						if(i != L_intSELROW)
							tblINDMT.setValueAt(new Boolean(false),i,0);
					}
					M_strSQLQRY="SELECT ST_UOMCD,ST_STKIN,ST_STKOR,ST_STKQT,PO_PORNO,PO_PORDT,PO_PORQT "
						+"FROM MM_STMST,MM_POMST WHERE "
						+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = '"+tblINDMT.getValueAt(L_intSELROW,1)+"' AND "
						+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
						+"PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MATCD = '"+tblINDMT.getValueAt(L_intSELROW,1)+"' AND  "
						+"PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
						+"PO_INDNO = '"+tblINDMT.getValueAt(L_intSELROW,4)+"'";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							txtUOMCD.setText(M_rstRSSET.getString("ST_UOMCD"));
							txtINDQT.setText(M_rstRSSET.getString("ST_STKIN"));
							txtORDQT.setText(M_rstRSSET.getString("ST_STKOR"));
							txtSTKQT.setText(M_rstRSSET.getString("ST_STKQT"));
							txtPPONO.setText(M_rstRSSET.getString("PO_PORNO"));
							txtPPODT.setText(M_rstRSSET.getString("PO_PORDT"));
							txtPPOQT.setText(M_rstRSSET.getString("PO_PORQT"));
						}
						M_rstRSSET.close();
					}
				}
			}
			if(L_ME.getSource() == tblINDDP)	//If Indents Departmetn Wise Selected
			{	
				clrTEXTFLD();
				if(tblINDDP.getSelectedColumn() == 0)
				{
					int L_intSELROW = tblINDDP.getSelectedRow();
					for(int i=0;i<tblINDDP.getRowCount();i++)
					{
						if(i != L_intSELROW)
							tblINDDP.setValueAt(new Boolean(false),i,0);
					}
					if(cmbRPTOP.getSelectedIndex() == 3 || cmbRPTOP.getSelectedIndex() == 4 || cmbRPTOP.getSelectedIndex() == 5)
						M_strSQLQRY="SELECT ST_UOMCD,ST_STKIN,ST_STKOR,ST_STKQT,PO_PORNO,PO_PORDT,PO_PORQT "
							+"FROM MM_STMST,MM_POMST WHERE "
							+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = '"+tblINDDP.getValueAt(L_intSELROW,TBL_MATCD)+"' AND "
							+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
							+"PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MATCD = '"+tblINDDP.getValueAt(L_intSELROW,TBL_MATCD)+"' AND  "
							+"PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
							+"PO_INDNO = '"+tblINDDP.getValueAt(L_intSELROW,TBL_INDNO)+"'";
					else
						M_strSQLQRY="SELECT ST_UOMCD,ST_STKIN,ST_STKOR,ST_STKQT "
							+"FROM MM_STMST WHERE "
							+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = '"+tblINDDP.getValueAt(L_intSELROW,TBL_MATCD)+"' AND "
							+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							txtUOMCD.setText(M_rstRSSET.getString("ST_UOMCD"));
							txtINDQT.setText(M_rstRSSET.getString("ST_STKIN"));
							txtORDQT.setText(M_rstRSSET.getString("ST_STKOR"));
							txtSTKQT.setText(M_rstRSSET.getString("ST_STKQT"));
							if(cmbRPTOP.getSelectedIndex() == 3 || cmbRPTOP.getSelectedIndex() == 4 || cmbRPTOP.getSelectedIndex() == 5)
							{
								txtPPONO.setText(M_rstRSSET.getString("PO_PORNO"));
								txtPPODT.setText(M_rstRSSET.getString("PO_PORDT"));
								txtPPOQT.setText(M_rstRSSET.getString("PO_PORQT"));
							}
						}
						M_rstRSSET.close();
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseClicked,for Department Wise Table");
		}
	}
	/**
	 *  Super class method overrided to execuate the F1 help.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtDPTCD")
		{
			txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtDPTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			getRECRD();
		}
	}
	/**
	 * Method to fetch data from the database & to Club it with Header & Footer.
	 */
	void getRECRD()
	{
		try
		{
			String strTEMP = txtDPTCD.getText().trim();
			tblINDDP.clrTABLE();
			tblINDMT.clrTABLE();
			clrTEXTFLD();
			txtDPTCD.setText(strTEMP);
			if(txtDPTCD.getText().trim().length() > 0)
			{
				M_strSQLQRY="SELECT  CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP='SYS' AND "
					+"CMT_CGSTP='COXXDPT' AND CMT_CODCD = '"+txtDPTCD.getText().trim()+"' "
					+"AND isnull(CMT_STSFL,'') <> 'X'";
					System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next())
					txtDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
				M_rstRSSET.close();
			}
			setCursor(cl_dat.M_curWTSTS_pbst);
			//Vector to use store additional detail as string and seperated by stringtokenizer on report printing
			vtrINDDT = new Vector<String>(10,5); 			
			if(cmbRPTOP.getSelectedIndex() == 1) // Query for - Indents not Authorised
			{ 
				M_strSQLQRY = "SELECT IN_DPTCD,IN_INDNO,IN_INDDT,IN_MATCD,IN_INDQT,isnull(IN_ORDQT,0) IN_ORDQT,"
					+"(day(getdate()) - day(IN_INDDT))AS PENDAY,IN_REQDT,IN_STSFL,IN_AMDNO,"
					+"IN_INDQT,IN_AUTQT,CT_MATDS,CT_UOMCD,ST_STKIN,ST_STKQT,ST_STKOR FROM "
					+"CO_CTMST,MM_INMST LEFT OUTER JOIN MM_STMST ON IN_STRTP = ST_STRTP AND "
					+"IN_MATCD = ST_MATCD AND IN_CMPCD=ST_CMPCD WHERE CT_MATCD = IN_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP='"
					+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,'') IN('0','2','3') ";
				if(txtDPTCD.getText().trim().length() > 0)
					M_strSQLQRY += " AND IN_DPTCD = '"+txtDPTCD.getText().trim()+"'";
				M_strSQLQRY += " AND IN_INDTP = '"+cmbINDTP.getSelectedItem().toString().substring(0,2)+"' ";
				M_strSQLQRY += " ORDER BY IN_DPTCD,IN_INDNO,IN_MATCD";
			}
			if(cmbRPTOP.getSelectedIndex() == 2 ) // Query for - List of indents not converted to P.O.
			{ 
				M_strSQLQRY = "SELECT IN_DPTCD,IN_INDNO,IN_AMDNO,IN_INDDT,IN_MATCD,IN_INDQT,IN_AUTQT,"
					+"isnull(IN_ORDQT,0) IN_ORDQT,IN_AUTDT,IN_REQDT,IN_EXPDT,IN_PORBY,(day(getdate()) - days(IN_PORBY)) AS PORBY,"
					+"(day(getdate()) - day(IN_EXPDT)) AS RECDAY,ST_MATDS,ST_UOMCD,CT_ILDTM,"
					+"CT_ELDTM FROM MM_INMST,MM_STMST,CO_CTMST WHERE IN_CMPCD=ST_CMPCD and IN_STRTP = ST_STRTP AND "
					+"IN_MATCD = ST_MATCD AND CT_MATCD = IN_MATCD AND "
					+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,'') = '4' "
					+"AND (isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 ";
				if(txtDPTCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND IN_DPTCD = '"+txtDPTCD.getText().trim()+"' ";
				M_strSQLQRY += " AND IN_INDTP = '"+cmbINDTP.getSelectedItem().toString().substring(0,2)+"' ";
				M_strSQLQRY += " ORDER BY IN_DPTCD,IN_INDDT DESC,IN_INDNO,IN_MATCD DESC";
			}
			if(cmbRPTOP.getSelectedIndex() == 3 )// Query for - Department wise list of indents overdue.
			{
				M_strSQLQRY = "SELECT IN_DPTCD,IN_INDNO,IN_AMDNO,IN_INDDT,IN_MATCD,IN_INDQT,IN_AUTQT,"
					+"isnull(IN_ORDQT,0) IN_ORDQT,IN_AUTDT,IN_REQDT,IN_EXPDT,(day(getdate()) - day(IN_PORBY)) AS PORBY,"
					+"(day(getdate()) - day(IN_EXPDT)) AS RECDAY,ST_MATDS,ST_UOMCD,CT_ILDTM,"
					+"CT_ELDTM FROM MM_INMST,MM_STMST,CO_CTMST WHERE IN_CMPCD=ST_CMPCD and IN_STRTP = ST_STRTP AND "
					+"IN_MATCD = ST_MATCD AND CT_MATCD = IN_MATCD AND "
					+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,'') = '4' AND " 
					+"(isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 AND "
					+"(getdate() - IN_EXPDT) > 0 ";
				if(txtDPTCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND IN_DPTCD = '"+txtDPTCD.getText().trim()+"' ";
				M_strSQLQRY += " AND IN_INDTP = '"+cmbINDTP.getSelectedItem().toString().substring(0,2)+"' ";
				// ARC INdents blocked on 17/11/04
				//M_strSQLQRY += " AND IN_INDTP <> '02' ORDER BY IN_DPTCD,IN_INDNO,IN_MATCD";
				M_strSQLQRY += " ORDER BY IN_DPTCD,IN_INDNO,IN_MATCD";
			}
			if(cmbRPTOP.getSelectedIndex() == 4)	//Query for - MATERIAL WISE LIST OF INDENTS OVERDUE
			{ 
				M_strSQLQRY = "SELECT IN_DPTCD,IN_INDNO,IN_AMDNO,IN_INDDT,IN_MATCD,IN_INDQT,IN_AUTQT,"
					+"isnull(IN_ORDQT,0) IN_ORDQT,IN_AUTDT,IN_REQDT,(day(getdate()) - day(IN_PORBY)) AS PORBY,"
					+"IN_EXPDT,(day(getdate()) - day(IN_EXPDT)) AS RECDAY,ST_MATDS,ST_UOMCD,"
					+"CT_ILDTM,CT_ELDTM FROM MM_INMST,MM_STMST,CO_CTMST WHERE "
					+"IN_CMPCD = ST_CMPCD and IN_STRTP = ST_STRTP AND IN_MATCD = ST_MATCD AND CT_MATCD = IN_MATCD AND "
					+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,'') = '4' AND "
					+"(isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 AND "
					+"(getdate() - IN_EXPDT) > 0 ";
				if(txtDPTCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND IN_DPTCD = '"+txtDPTCD.getText().trim()+"' ";
				M_strSQLQRY += " AND IN_INDTP = '"+cmbINDTP.getSelectedItem().toString().substring(0,2)+"' ";
				M_strSQLQRY += " ORDER BY IN_DPTCD,IN_INDNO,IN_MATCD";
				// ARC INdents blocked on 17/11/04
				//M_strSQLQRY += " AND IN_INDTP <> '02' ORDER BY IN_MATCD,IN_INDNO,IN_DPTCD";
			}
			if(cmbRPTOP.getSelectedIndex() == 5)	//Query for - LIST OF AUTHORISED INDENTS
			{
				M_strSQLQRY = "SELECT IN_DPTCD,IN_INDNO,IN_AMDNO,IN_INDDT,IN_MATCD,IN_INDQT,IN_AUTQT,"
				    +"isnull(IN_ORDQT,0) IN_ORDQT,IN_STSFL,IN_AUTDT,IN_REQDT,IN_AUTBY,IN_EXPDT,ST_MATDS,ST_UOMCD,"
					+"ST_STKIN,ST_STKQT,ST_STKOR,CMT_CODDS FROM MM_INMST,MM_STMST,CO_CDTRN WHERE "
					+"IN_CMPCD=ST_CMPCD and IN_STRTP = ST_STRTP AND IN_MATCD=ST_MATCD AND "
					+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP='"+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,'') = '4' AND "
					+"CMT_CGMTP = 'STS' AND CMT_CGSTP = 'MMXXIND' AND CMT_CODCD = IN_STSFL AND "
					+" (isnull(IN_AUTQT,0) - isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 ";
				if(txtDPTCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND IN_DPTCD = '"+txtDPTCD.getText().trim()+"' ";
				M_strSQLQRY += " AND IN_INDTP = '"+cmbINDTP.getSelectedItem().toString().substring(0,2)+"' ";
				M_strSQLQRY += " ORDER BY IN_DPTCD,IN_INDNO,IN_MATCD";	
			}
			if(cmbRPTOP.getSelectedIndex() == 6)	//Query for - LIST OF AUTHORISED INDENTS
			   return;
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);		
			intRWCNT = 0;
			//display the result of query in table and store remaining detaisl in hashtable in the form of string 
			//which then seperated by using string tokenizer 
			if(M_rstRSSET != null)
			{
				int i=0;
				if(cmbRPTOP.getSelectedIndex() == 4)	//MATERIALWISE LIST OF INDENTS OVERDUES
				{		
					while(M_rstRSSET.next())
					{
						tblINDMT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),""),i,1);
						tblINDMT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),i,2);
						tblINDMT.setValueAt(hstDPTCD.get(M_rstRSSET.getString("IN_DPTCD")),i,3);
						tblINDMT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),i,4);
						tblINDMT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_AMDNO"),""),i,5);
						if(M_rstRSSET.getDate("IN_INDDT") != null)
							tblINDMT.setValueAt(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_INDDT")),""),i,6);
						tblINDMT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDQT"),"0.0"),i,7);
						tblINDMT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.0"),i,8);
						
						//Srote Other Details Of Record In Hash Table w.r.t. Material Code
						//ST_UOMCD,PORBY,IN_AUTDT,CT_ILDTM,CT_ELDTM,IN_REQDT,IN_AUTQT,IN_ORDQT
						//sequence in string L_strMATDL used in printing report
					
						String L_strMATDL = M_rstRSSET.getString("ST_UOMCD")+"|";
						L_strMATDL += nvlSTRVL(M_rstRSSET.getString("PORBY"),"0")+"|";
						
						if(M_rstRSSET.getDate("IN_AUTDT") != null)				   
							L_strMATDL +=M_fmtLCDAT.format(M_rstRSSET.getDate("IN_AUTDT"))+"|";
						else
							L_strMATDL +=" "+"|";
						
						L_strMATDL += nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0")+"|";
						L_strMATDL += nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0")+"|";

						if(M_rstRSSET.getDate("IN_REQDT") != null)
							L_strMATDL +=M_fmtLCDAT.format(M_rstRSSET.getDate("IN_REQDT"))+"|";
						else
							L_strMATDL +=" "+"|";
						
						L_strMATDL += nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.0")+"|";
						L_strMATDL += nvlSTRVL(M_rstRSSET.getString("IN_ORDQT"),"0.0")+"|";
						if(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("RECDAY"),"0")) > 0)
							L_strMATDL += nvlSTRVL(M_rstRSSET.getString("RECDAY"),"0")+"|";		//Pendign day for receipt
						else
							L_strMATDL += " "+"|";
						//Strote in vector for report printing use
						vtrINDDT.addElement(L_strMATDL);
						intRWCNT++;
						i++;
					}
				}
				else
				{	
					while(M_rstRSSET.next())
					{
						tblINDDP.setValueAt(hstDPTCD.get(M_rstRSSET.getString("IN_DPTCD")),i,1);
						tblINDDP.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),i,2);
						tblINDDP.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_AMDNO"),""),i,3);
						if(M_rstRSSET.getDate("IN_INDDT") != null)
							tblINDDP.setValueAt(nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_INDDT")),""),i,4);
						tblINDDP.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),""),i,5);
						if(cmbRPTOP.getSelectedIndex() == 1)
							tblINDDP.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),i,6);
						else
							tblINDDP.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),i,6);
						tblINDDP.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDQT"),"0.0"),i,7);
						tblINDDP.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.0"),i,8);
						//Method to Store details in hashtable hstRPTDT
						if (cmbRPTOP.getSelectedIndex() == 1)	//LIST OF INDENTS NOT AUTHORISED
						{	
							//Store Record Detail In Hash Table W.R.T. Materoal Code
							//CT_UOMCD,ST_STKIN,ST_STKIN,ST_STKQT,PENDAY,CMT_CODDS,ST_STKOR,IN_REQDT
							//sequence in string L_strMATDL used in printing report
							String L_strMATDL = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"")+"|"	//Unit Of Mesurment Code
								   +nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0.0")+"|"			//Qty on Indent
								   +nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0.0")+"|"			//Qty on Hand
								   +nvlSTRVL(M_rstRSSET.getString("PENDAY"),"0")+"|"				//Pending Days
								   +hstSTSFL.get(M_rstRSSET.getString("IN_STSFL")).toString()+"|"
								   +nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0.0")+"|";			//Qty on Order For Stock
							if(M_rstRSSET.getDate("IN_REQDT") !=null)
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_REQDT")),"")+"|";	//Required Date
							else		   
								L_strMATDL +=" "+"|";	//Required Date
							//Store Report one detail in vector w.r.t. row id
							vtrINDDT.addElement(L_strMATDL);
						}
						if(cmbRPTOP.getSelectedIndex() == 2)	//LIST OF INDENTS NOT CONVERTED TO P.O.
						{
							//ST_UOMCD,IN_ORDQT,IN_REQDT,IN_PORBY,IN_AUTDT,CT_ILDTM,CT_ELDTM,IN_EXPDT,RECDAY
							//sequence in string L_strMATDL used in printing report
							//Add extra details to string and add this string to the hastable w.r.t material code
							String L_strMATDL = M_rstRSSET.getString("ST_UOMCD")+"|"		//Unit Of Measurment
								+nvlSTRVL(M_rstRSSET.getString("IN_ORDQT"),"0.0")+"|";		//Order Quantity
							if(M_rstRSSET.getDate("IN_REQDT") != null)
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_REQDT")),"")+"|";	//Required Date
							else
								L_strMATDL +=" "+"|";	//Required Date
							if(M_rstRSSET.getDate("IN_PORBY") != null)
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_PORBY")),"")+"|";	//P.O. By Date
							else
								L_strMATDL +=" "+"|";	//P.O. By Date
					// Commented on 14/12/04 to display overdue days (+ / -) both as per purchase requirement 
					//		if(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("PORBY"),"0")) > 0)
								L_strMATDL += nvlSTRVL(M_rstRSSET.getString("PORBY"),"0")+"|";	//P.O. Overdue
					//		else
					//			L_strMATDL += " "+"|";
							if(M_rstRSSET.getDate("IN_AUTDT") != null)
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_AUTDT")),"")+"|";	//Auth Date
							else
								L_strMATDL +=" "+"|";	//Autorised Date
							L_strMATDL += nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0")+"|"	//Internal Lead Time
										 +nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0")+"|";	//External Lead Time
							if(M_rstRSSET.getDate("IN_EXPDT") != null)
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_EXPDT")),"")+"|";	//Expected Date
							else
								L_strMATDL +=" "+"|";	//Required Date
							if(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("RECDAY"),"0")) > 0)
								L_strMATDL += nvlSTRVL(M_rstRSSET.getString("RECDAY"),"0")+"|";		//Pendign day for receipt
							else
								L_strMATDL += " "+"|";
							
							
							//Store Report detail in vector w.r.t. row id
							vtrINDDT.addElement(L_strMATDL);
						}
						if (cmbRPTOP.getSelectedIndex() == 3)	//DEPATMENTWISE LIST OF INDENTS OVERDUES
						{
							//ST_UOMD,IN_AUTQT,IN_ORDQT,IN_REQDT,PORBY,CT_ILDTM,CT_ELDTM,IN_EXPDT,RECDAY
							//sequence in string L_strMATDL used in printing report
							String L_strMATDL = M_rstRSSET.getString("ST_UOMCD")+"|"		//Unit Of Measurment
								+nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.0")+"|"		//Authorised Quantity
								+nvlSTRVL(M_rstRSSET.getString("IN_ORDQT"),"0.0")+"|";		//Order Quanitity
							if(M_rstRSSET.getDate("IN_REQDT") !=null)	//Required Date
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_REQDT"))," ")+"|";	
							else
								L_strMATDL +=" "+"|";	
							L_strMATDL += nvlSTRVL(M_rstRSSET.getString("PORBY"),"0")+"|";		//P.O. Overdue	
							L_strMATDL += nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0.0")+"|";	//Internal Lead Time
							L_strMATDL += nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0.0")+"|";	//External Lead Time
							if(M_rstRSSET.getDate("IN_EXPDT") !=null)
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_EXPDT"))," ")+"|";	
							else
								L_strMATDL +=" "+"|";	
							if(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("RECDAY"),"0")) > 0)
								L_strMATDL += nvlSTRVL(M_rstRSSET.getString("RECDAY")," ")+"|";		//Pendign day for receipt
							else
								L_strMATDL += " "+"|";
							//Store Report detail in vector w.r.t. row id
							vtrINDDT.addElement(L_strMATDL);
						}
						if (cmbRPTOP.getSelectedIndex() == 5)	//LIST OF AUTHORISED INDENTS
						{
							//ST_UOMCD,IN_AUTBY,IN_REQDT,IN_AUTDT
							//sequence in string L_strMATDL used in printing report
							String L_strMATDL = M_rstRSSET.getString("ST_UOMCD")+"|";			//Unit Of Measurment 
							L_strMATDL += nvlSTRVL(M_rstRSSET.getString("IN_AUTBY"),"")+"|";	//Authorised By
							if(M_rstRSSET.getDate("IN_REQDT") != null)		//Required Date
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_REQDT")),"")+"|";	
							else
								L_strMATDL +=" "+"|";	
							if(M_rstRSSET.getDate("IN_AUTDT") != null)		//Authorised Date
								L_strMATDL +=nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_AUTDT")),"")+"|";	
							else
								L_strMATDL +=" "+"|";	
							//Store Report detail in vector w.r.t. row id
							vtrINDDT.addElement(L_strMATDL);
						}
						
						
						intRWCNT++;
						i++;
					}
				}
				M_rstRSSET.close();
			}	
			else
				setMSG("Record Not Found ",'E');			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRECRD, Display Data In Table");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
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
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpinl.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpinl.doc";
			
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Indent Status"," ");
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
	 * Method to fetch data from the Hashtable & vector & to club it with Header & footer. 
	 */
	private void getDATA()
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
		try
		{
			if(fosREPORT !=null)
				fosREPORT.close();
			if(dosREPORT !=null)
				dosREPORT.close();
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{			 						
				if(cmbRPTOP.getSelectedIndex() == 1)
				{
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI17);
				}
				else
				{
					prnFMTCHR(dosREPORT,M_strNOCPI17);
					prnFMTCHR(dosREPORT,M_strCPI12);
				}
			}
			else if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Indent Status</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}			
			prnHEADER();		//call method to print heading
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				strDPTCD="";
				String L_strINDNO = "";
				String L_strTEMP = "";
				char L_chrINDNO = 'N';
				for(int i=0;i<intRWCNT;i++)
				{
					//Get String from vector and seperate by string tokenizer
					String s = vtrINDDT.elementAt(i).toString();
					StringTokenizer stkSTRZR = new StringTokenizer(s,"|");
					if(cl_dat.M_intLINNO_pbst > 65)
					{
						dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------------------------------------------------------------\n");					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					if(tblINDDP.getValueAt(i,TBL_DPTNM) != null)
						L_strTEMP = tblINDDP.getValueAt(i,TBL_DPTNM).toString();		//Departmetn Name
					if(L_strTEMP != strDPTCD)	//Check with Pervious Departmetn Name
					{  
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes("DEPARTMENT :"+L_strTEMP);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n\n");
						strDPTCD = L_strTEMP;
						cl_dat.M_intLINNO_pbst +=2;
					}
					//First Row
					if(!L_strINDNO.equals(tblINDDP.getValueAt(i,TBL_INDNO).toString())){  //Check with Previous Indent No
						dosREPORT.writeBytes(padSTRING('R',"\n"+tblINDDP.getValueAt(i,TBL_INDNO).toString(),13));		//Indent No  2
						L_strINDNO = tblINDDP.getValueAt(i,TBL_INDNO).toString();
						L_chrINDNO = 'Y';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"\n",13));		//Indent No  2
					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_MATCD).toString(),30));	//Material code	5
					dosREPORT.writeBytes(padSTRING('R',stkSTRZR.nextToken(),30));		//Unit Of Mesurement
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),13));	//Qty Intent
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),14));	//Qty On Hand
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),17));	//Pending Days
					dosREPORT.writeBytes(padSTRING('L',"",4));
					dosREPORT.writeBytes(padSTRING('R',stkSTRZR.nextToken(),30));	//Indent Status
					//Second Row
					dosREPORT.writeBytes("\n");
					if(L_chrINDNO == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_INDDT).toString(),13));	//Ind Date
						L_chrINDNO = 'N';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",13));	//Ind Date

					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,6).toString(),60));	//Material Description
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),13));	//Qty On Order
					dosREPORT.writeBytes(padSTRING('L',tblINDDP.getValueAt(i,TBL_INDQT).toString(),14));	//Qty Indented	7
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),17)+"\n \n");	//INDENT STATUS
					cl_dat.M_intLINNO_pbst +=4;
				}
				L_strINDNO="";
				L_chrINDNO = 'N';
			}
			if(cmbRPTOP.getSelectedIndex() == 2)
			{
				strDPTCD="";
				String L_strINDNO="";
				char L_chrINDNO = 'N';
				for(int i=0;i<intRWCNT;i++)
				{
					//Get String for vector w.r.t. row id and seperate it by string tokenizer
					String s = vtrINDDT.elementAt(i).toString();
					StringTokenizer stkSTRZR = new StringTokenizer(s,"|");
					if(cl_dat.M_intLINNO_pbst > 65)
					{						
						dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					String L_strTEMP;
					if(tblINDDP.getValueAt(i,1) != null)
						L_strTEMP = tblINDDP.getValueAt(i,1).toString();
					else 
						L_strTEMP = "";
					if(L_strTEMP != strDPTCD)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes("DEPARTMENT :"+L_strTEMP);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n\n");
						strDPTCD = L_strTEMP;
						cl_dat.M_intLINNO_pbst +=2;
					}
					//ST_UOMCD,IN_ORDQT,IN_REQDT,IN_PORBY,IN_AUTDT,CT_ILDTM,CT_ELDTM,IN_EXPDT,RECDAY
					//First Row
					if(!L_strINDNO.equals(tblINDDP.getValueAt(i,TBL_INDNO).toString()))
					{
						dosREPORT.writeBytes(padSTRING('R',"\n"+tblINDDP.getValueAt(i,TBL_INDNO).toString(),13));		//Indent No
						L_strINDNO = tblINDDP.getValueAt(i,TBL_INDNO).toString();
						L_chrINDNO = 'Y';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"\n",13));		//Indent No
					
					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_MATCD).toString(),25));			//Material code
					dosREPORT.writeBytes(padSTRING('R',stkSTRZR.nextToken(),17));			//Unit Of Mesurement
					float fltAUTQT = Float.parseFloat(tblINDDP.getValueAt(i,TBL_AUTQT).toString());		//Authorised Qty
					float fltORDQT = Float.parseFloat(stkSTRZR.nextToken());			//Order Qty
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(fltAUTQT - fltORDQT),8));		//Pending Qty.
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),15));			//Required Date
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),12));			//P.O.By Date
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),8));			//P.O. Overdue
					stkSTRZR.nextToken();
					int intILDTM = Integer.parseInt(stkSTRZR.nextToken());
					int intELDTM = Integer.parseInt(stkSTRZR.nextToken());
					dosREPORT.writeBytes("\n");
					if(L_chrINDNO == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_INDDT).toString(),13));	//Ind Date
						L_chrINDNO = 'N';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",13));	//Ind Date
					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_MATDS).toString(),55));	//Material Desc
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),10));	//Expected Date
					dosREPORT.writeBytes(padSTRING('L',"",12));	//Blank space
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),8));	//Days OverDue For Receipt
					
					dosREPORT.writeBytes("\n \n");
					cl_dat.M_intLINNO_pbst += 4;
				}
				L_strINDNO="";
				L_chrINDNO = 'N';
				strDPTCD = "";
			}
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
				strDPTCD="";
				String L_strINDNO="";
				char L_chrINDNO = 'N';
				for(int i=0;i<intRWCNT;i++)
				{
					//Get String for vector w.r.t. row id and seperate it by string tokenizer
					String s = vtrINDDT.elementAt(i).toString();
					StringTokenizer stkSTRZR = new StringTokenizer(s,"|");
					if(cl_dat.M_intLINNO_pbst > 65)
					{						
						dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					String L_strTEMP;
					if(tblINDDP.getValueAt(i,1) != null)
						L_strTEMP = tblINDDP.getValueAt(i,1).toString();
					else
						L_strTEMP = "";
					
					if(L_strTEMP != strDPTCD)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes("DEPARTMENT :"+L_strTEMP);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n\n");
						strDPTCD = L_strTEMP;
						cl_dat.M_intLINNO_pbst +=2;
					}
					//First Row
					if(!L_strINDNO.equals(tblINDDP.getValueAt(i,TBL_INDNO).toString()))
					{
						dosREPORT.writeBytes(padSTRING('R',"\n"+tblINDDP.getValueAt(i,TBL_INDNO).toString(),12));		//Indent No  2
						L_strINDNO = tblINDDP.getValueAt(i,TBL_INDNO).toString();
						L_chrINDNO = 'Y';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"\n",12));		//Indent No  2
					//ST_UOMD,IN_AUTQT,IN_ORDQT,IN_REQDT,PORBY,CT_ILDTM,CT_ELDTM,IN_EXPDT,RECDAY
					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_MATCD).toString(),25));	//Material code
					dosREPORT.writeBytes(padSTRING('R',stkSTRZR.nextToken(),17));		//Unit Of Mesurement
					dosREPORT.writeBytes(padSTRING('L',tblINDDP.getValueAt(i,TBL_AUTQT).toString(),9));	//Authorised Qty
					float fltAUTQT = Float.parseFloat(stkSTRZR.nextToken());	//Authorised Qty
					float fltORDQT = Float.parseFloat(stkSTRZR.nextToken());	//Order Qty
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),15));	//Required Date
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),15));	//P.O. Overdue
					//stkSTRZR.nextToken();
					dosREPORT.writeBytes("\n");
					if(L_chrINDNO == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_INDDT).toString(),12));	//Ind Date
						L_chrINDNO = 'N';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",12));	//Ind Date
					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_MATDS).toString(),42));	//Material Desc
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(fltAUTQT-fltORDQT),9));	//Pending Qty
					int intILDTM = Integer.parseInt(stkSTRZR.nextToken());
					int intELDTM = Integer.parseInt(stkSTRZR.nextToken());
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),15));	//Expected Date
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),15));	//receipt overdue
					dosREPORT.writeBytes("\n \n");
					cl_dat.M_intLINNO_pbst += 4;
				}
				L_strINDNO="";
				L_chrINDNO = 'N';
			}
			if(cmbRPTOP.getSelectedIndex() == 4)
			{
				strDPTCD="";
				for(int i=0;i<intRWCNT;i++)
				{
					//Get String for vector w.r.t. row id and seperate it by string tokenizer
					String s = vtrINDDT.elementAt(i).toString();
					StringTokenizer stkSTRZR = new StringTokenizer(s,"|");
					if(cl_dat.M_intLINNO_pbst > 65)
					{						
						dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					dosREPORT.writeBytes(padSTRING('R',"\n"+tblINDMT.getValueAt(i,1).toString(),13));		//Material Code
					dosREPORT.writeBytes(padSTRING('R',tblINDMT.getValueAt(i,2).toString(),61));	//Material code
					dosREPORT.writeBytes(padSTRING('R',stkSTRZR.nextToken(),5));		//Unit Of Mesurement
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),15));		//P.O. Overdue
					String strAUTDT = stkSTRZR.nextToken().toString();
					int intILDTM = Integer.parseInt(stkSTRZR.nextToken());
					int intELDTM = Integer.parseInt(stkSTRZR.nextToken());
					dosREPORT.writeBytes(padSTRING('L',"",17));	//Days OverDue for P. O.
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',tblINDMT.getValueAt(i,4).toString(),13));	//Indent No
					dosREPORT.writeBytes(padSTRING('R',tblINDMT.getValueAt(i,6).toString(),13));	//Indent Date
					dosREPORT.writeBytes(padSTRING('L',tblINDMT.getValueAt(i,8).toString(),10));	//Authorised Qty
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('R',stkSTRZR.nextToken(),13));	//Required Date
					float fltAUTQT = Float.parseFloat(stkSTRZR.nextToken());
					float fltORDQT = Float.parseFloat(stkSTRZR.nextToken());
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(fltAUTQT - fltORDQT),10));		//Pending Qty.
					dosREPORT.writeBytes(padSTRING('L',"",3));		//Blank
					dosREPORT.writeBytes(padSTRING('R',tblINDMT.getValueAt(i,3).toString(),14));		//Department
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),15));		//Days OverDue for Receipt
					dosREPORT.writeBytes("\n \n");
					cl_dat.M_intLINNO_pbst += 4;
				}
			}
			if(cmbRPTOP.getSelectedIndex() == 5)
			{
				strDPTCD="";
				String L_strINDNO="";
				char L_chrINDNO = 'N';
				intSRLNO = 1;
				for(int i=0;i<intRWCNT;i++)
				{
					//Get String for vector w.r.t. row id and seperate it by string tokenizer
					String s = vtrINDDT.elementAt(i).toString();
					StringTokenizer stkSTRZR = new StringTokenizer(s,"|");
					if(cl_dat.M_intLINNO_pbst > 65)
					{						
						dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					String L_strTEMP;
					if(tblINDDP.getValueAt(i,1) != null)
						L_strTEMP = tblINDDP.getValueAt(i,1).toString();
					else 
						L_strTEMP = "";
					if(L_strTEMP != strDPTCD)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes("DEPARTMENT :"+L_strTEMP);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n\n");
						strDPTCD = L_strTEMP;
						cl_dat.M_intLINNO_pbst +=2;
					}
					//First Row		//ST_UOMCD,IN_AUTBY,IN_REQDT,IN_AUTDT
					if(!L_strINDNO.equals(tblINDDP.getValueAt(i,TBL_INDNO).toString()))
					{
						intSRLNO = 1;
						dosREPORT.writeBytes(padSTRING('R',"\n"+tblINDDP.getValueAt(i,TBL_INDNO).toString(),12));		//Indent No
						L_strINDNO = tblINDDP.getValueAt(i,TBL_INDNO).toString();
						L_chrINDNO = 'Y';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"\n",12));		//Indent No
					
					dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),5));
					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_MATCD).toString(),25));			//Material code
					dosREPORT.writeBytes(padSTRING('R',stkSTRZR.nextToken(),24));			//Unit Of Mesurement
					dosREPORT.writeBytes(padSTRING('L',tblINDDP.getValueAt(i,TBL_AUTQT).toString(),15));		//Authorised Qty.
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),5));			//Authorised By
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),11));	//Required Date
					dosREPORT.writeBytes("\n");
					if(L_chrINDNO == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_INDDT).toString(),17));	//Ind Date
						L_chrINDNO = 'N';
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",17));	//Ind Date
					dosREPORT.writeBytes(padSTRING('R',tblINDDP.getValueAt(i,TBL_MATDS).toString(),50));	//Material Desc
					dosREPORT.writeBytes(padSTRING('L',stkSTRZR.nextToken(),14));	//Authorised Date
					dosREPORT.writeBytes("\n \n");
					cl_dat.M_intLINNO_pbst += 4;
					intSRLNO++;
				}
				L_strINDNO="";
				L_chrINDNO = 'N';
				strDPTCD = "";
			}
			if(cmbRPTOP.getSelectedIndex() == 6)	//LIST OF PENDING INDENTS (FOR RECEIPT)
			{
				ResultSet L_rstRSSET;
				strINDNO = "";
				strDPTCD = "";
				flgPRINT = false;
				java.sql.Date jdtTEMP;	
				M_strSQLQRY = "SELECT IN_INDNO,IN_INDDT,IN_MATCD,IN_REQDT,IN_AUTQT,isnull(IN_ORDQT,0) IN_ORDQT,"
					+"isnull(IN_ACPQT,0) IN_ACPQT,IN_DPTCD,IN_INDTP,CT_UOMCD,CT_MATDS FROM MM_INMST,CO_CTMST "
					+"WHERE CT_MATCD = IN_MATCD AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_AUTQT,0) - isnull(IN_ACPQT,0)- isnull(IN_FCCQT,0) > 0 AND "
					+"isnull(IN_STSFL,' ') = '4' AND isnull(CT_STSFL,' ') <> 'X' AND "
					+"(isnull(IN_ORDQT,0) - isnull(IN_FCCQT,0)) > 0 ";
				if(txtDPTCD.getText().trim().length() > 0)
					M_strSQLQRY += " AND IN_DPTCD = '"+txtDPTCD.getText()+"' ";
				// ARC INdents blocked on 17/11/04
				M_strSQLQRY += " AND IN_INDTP <>'02' ORDER BY IN_DPTCD,IN_INDNO,IN_MATCD";
					
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						if(cl_dat.M_intLINNO_pbst > 60)
						{							
							dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
						}
						//Check If Departmetn is Equal To previous Department 
						if(!strDPTCD.equals(nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"")))
						{
							if(M_rstRSSET.getString("IN_DPTCD") != null)
							{
								strDPTCD = nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"");
								dosREPORT.writeBytes("Department : "+hstDPTCD.get(strDPTCD));
							}
							else
								dosREPORT.writeBytes("Department : ");
							dosREPORT.writeBytes("\n\n");
						}
						//Check For Previous Indent Number
						if(!strINDNO.equals(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),"")))
						{
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),12));
							strINDNO = nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),"");
							flgPRINT = true;
						}
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));
						
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),""),20));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),7));
						jdtTEMP = M_rstRSSET.getDate("IN_REQDT");
						if(jdtTEMP != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(jdtTEMP),11));
						else
							dosREPORT.writeBytes(padSTRING('R',"",11));
						
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IN_ORDQT"),"0.0"),15));										
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IN_ACPQT"),"0.0"),14));
						dosREPORT.writeBytes("\n");
						if(flgPRINT)
						{
							jdtTEMP = M_rstRSSET.getDate("IN_INDDT");
							if(jdtTEMP != null)
								dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(jdtTEMP),12));
							else
								dosREPORT.writeBytes(padSTRING('R',"",12));
							flgPRINT = false;
						}
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),71));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IN_ACPQT"),"0.0")),3),13));
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
				}
			}
			if(cmbRPTOP.getSelectedIndex() == 1)
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			else
				dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");
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
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/** 
	 * Method to generate the Header part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			cl_dat.M_intLINNO_pbst = 0;			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strENH);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("SUPREME PETROCHEM LTD.");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOENH);
			dosREPORT.writeBytes("\n \n");
			cl_dat.M_intLINNO_pbst += 2;
			if(cmbRPTOP.getSelectedIndex() == 1) 
				dosREPORT.writeBytes(padSTRING('R',strRPTNM,130));			
			if(cmbRPTOP.getSelectedIndex() == 2 || cmbRPTOP.getSelectedIndex() == 5 || cmbRPTOP.getSelectedIndex() == 6)
				dosREPORT.writeBytes(padSTRING('R',strRPTNM,75));
			if(cmbRPTOP.getSelectedIndex() == 3 || cmbRPTOP.getSelectedIndex() == 4)
				dosREPORT.writeBytes(padSTRING('R',strRPTNM +" AS ON : "+cl_dat.M_strLOGDT_pbst,75));
			dosREPORT.writeBytes(padSTRING('L',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
			dosREPORT.writeBytes("\n");		cl_dat.M_intLINNO_pbst += 1;
			if(cmbRPTOP.getSelectedIndex() == 1) 
				dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),130));
			else
				dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),75));				
			dosREPORT.writeBytes(padSTRING('R',"Page No : "+cl_dat.M_PAGENO,20));
			if(cmbRPTOP.getSelectedIndex() == 1)
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			else
				dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst += 2;
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				dosREPORT.writeBytes("Ind No.      Material Code                 UOM                           Qty.On Indent  Qty.On Hand     Pending Days    Indent Status                   \n");
				dosREPORT.writeBytes("Ind Dt.      Description                                                 Qty.On Order.  Qty.Indented    Required Date   ");
			}
			if(cmbRPTOP.getSelectedIndex() == 2)
			{
				dosREPORT.writeBytes("Ind No.      Mat. Code                UOM               Pending       Req. Dt. PO Exp Dt Overdue PO\n");
				dosREPORT.writeBytes("Ind Dt.      Description                                  Qty.        Exp. Dt.           For Receipt");
				
			//	dosREPORT.writeBytes("Ind No.      Mat. Code                UOM               Pending       Requ.Dt.   Days OverDue PO\n");
			//	dosREPORT.writeBytes("Ind Dt.      Description                                  Qty.        Exp. Dt.   For Receipt");
			}
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
				dosREPORT.writeBytes("Ind No.     Mat. Code                UOM               Auth.Qty.    Requ.Dt.   Days OverDue P.O.\n");
				dosREPORT.writeBytes("Ind Dt.     Description                                Pend.Qty.    Exp.Date   Days OverDue Rcpt");
			}
			if(cmbRPTOP.getSelectedIndex() == 4)
			{
				dosREPORT.writeBytes("Mat.Code     Description                                                 UOM   Days OverDue P.O.\n");
				dosREPORT.writeBytes("Ind No.      Ind. Date    Auth. Qty.   Requ. Dt.    Pend. Qty.   Department    Days OverDue Rcpt");
			}
			if(cmbRPTOP.getSelectedIndex() == 5)
			{
				dosREPORT.writeBytes("Ind No.     Sr.  Mat. Code                UOM                          Auth. Qty. Auth   Required\n");
				dosREPORT.writeBytes("Ind Dt.     No.  Description                                           Auth. Date   By       Date");
			}
			if(cmbRPTOP.getSelectedIndex() == 6)
			{
				dosREPORT.writeBytes("Indent No.  Material Code       UOM    Req. Date     Authorised Qty.     Order Qty.    Accp. Qty\n");
				dosREPORT.writeBytes("Indent Dt.  Material Description                                                     Balance Qty");
			}
			if(cmbRPTOP.getSelectedIndex() == 1)
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			else
				dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst += 2;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Exception in prnHEADER");
		}
		setMSG("",'N');
	}
	/**
	 * Method to clear text field diaplys additional information of selected recored 	 
	 */
	void clrTEXTFLD()
	{
		txtUOMCD.setText("");
		txtINDQT.setText("");
		txtORDQT.setText("");
		txtSTKQT.setText("");
		txtPPONO.setText("");
		txtPPODT.setText("");
		txtPPOQT.setText("");
	}	
	/**
	 *  Method to validate input before prossing retur boolean depaending on input
	 */
	public boolean vldDATA()
	{
		strDPTCD = "";
		strINDNO = ""; 
		cl_dat.M_PAGENO = 0;
		intSRLNO = 1;
		if(cmbRPTOP.getSelectedIndex() == 0)
		{
			cmbRPTOP.requestFocus();
			setMSG("Select Report Option..",'E');
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
}