/*
System Name   : Material Management System
Program Name  : Gate Pass Entry
Program Desc. : Enter,Modify,Delete and enquary of Gate Pass
Author        : A. T. Chaudhari
Date          : 29/10/2004
Version       : MMS 2.0
*/
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.awt.Color;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR> <TR><TD>Program Name</TD><TD>Gate Pass Entry</TD></TR>   <TR><TD>Program Desc</TD><TD>Gate Pass Entry Program to Create Gate Pass</TD></TR>   <TR><TD>Basis Document</TD><TD>  </TD></TR>   <TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_tegpt.class</TD></TR>   <TR><TD>Source path</TD><TD>f:\source\splerp2\mm_tegpt.java</TD></TR>   <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR>   <TR><TD>Date</TD><TD>29/10/2004 </TD></TR>   <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR>   <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR>   <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR>   <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR>   <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR>   </TABLE></P>   <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P>   <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>   <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD>	<TD><P align=center>Mod</P>	 </TD>	<TD><P align=center>Del</P></TD>	<TD><P align=center>Enq</P></TD></TR>   <TR><TD>CO_CDTRN</TD><TD>DOC,MMXXMGP,STS,MMXXGPS,SYS,COXXDPT,AUT,MMXXGPF	</TD><TD><P align=center>&nbsp;</P></TD>	<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>	<TD><P align=center>&nbsp;</P></TD>	<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR><TD>MM_GPTRN</TD><TD>GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD	</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>	<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>   <TR><TD>MM_GRMST</TD><TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO	</TD><TD><P align=center>&nbsp;</P></TD>	<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>   <TR><TD>CO_PTMST</TD><TD>PT_PRTTP,PT_PRTCD	</TD><TD><P align=center>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>		<TD><P align=center>&nbsp;</P></TD>	<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>   <TR><TD>MM_WBTRN</TD><TD>	</TD><TD><P align=center>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>		<TD><P align=center>&nbsp;</P></TD>		<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>   <TR><TD>CO_CTMST</TD><TD>CT_GRPCD,CT_CODTP,CT_MATCD	</TD><TD><P align=center>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>		<TD><P align=center>&nbsp;</P></TD>		<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>   <TR><TD>MM_RMMST</TD><TD>RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO	</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>	<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>   </TABLE></P><P><FONT color=purple><STRONG>Remarks : </STRONG></FONT></P><UL dir=ltr>  <LI>  <DIV><STRONG>Addition of New Gate Pass :</STRONG> </DIV>  <UL style="MARGIN-RIGHT: 0px">    <LI>                                                 Gate Pass Number is generated and updated by&nbsp;getMGPNO(),updMGPNO().     <LI>                                       For Returnable Gate Pass Due Date is     compulsary.&nbsp;     <LI>                                                                               For Rejection Gate     Pass all the detail of rejected material&nbsp;displayed&nbsp;by getGRNDTL().     <LI>    Remark for the gate pass is added in the     MM_RMMST w.r.t the gate pass number if present.    <LI>                   Gate Pass Status 1 for Fresh Gate pass, 2 for forwrd for approval, 3     approved, 4 authorised by stores.    <LI>Insurence Items in the gate pass is marked by     insurence check box in the table.     <LI>In case of returnable gate pass gate pass quantity is modified in MM_GRMST     tabek for GR_GPQTY.</LI></UL>  <LI dir=ltr><STRONG>Modification :</STRONG> </LI>   <UL dir=ltr>    <LI dir=ltr>      Modification&nbsp;is allowed before the gate pass     is&nbsp;gate out that is check by the gate pass status is not above 4.     <LI dir=ltr>In case of Returnable gate pass only due date is allowd to     modify though the gate pass is gate out.    <LI dir=ltr>In modification you may modifiy remark of gate pass, issue     quantity, due date, insurance and forwared to fields.    <LI dir=ltr>Gate pass quantity is&nbsp;updated by (     GP_GPQTY -&nbsp;vtrGPQTY + new issue Quantity).&nbsp;</LI></UL>  <LI><STRONG>Deletion :</STRONG>   <UL>    <LI>                                         Gate pass Status Flag is&nbsp;marked as 'X'&nbsp;for checked items.</LI></UL>  <LI><STRONG>Enquery :</STRONG>   <UL>    <LI>                                    Displays all the detail of gate pass&nbsp;getGPDTL() .</LI></UL>  <LI><STRONG>Autorrisation :</STRONG> </LI>   <UL>    <LI>                This option is allowed to only authorities. On&nbsp;F1 display the list of only     those gate pass which is forward&nbsp;for authorization. All detail are     displayed by&nbsp;getGPDTL().&nbsp;&nbsp;</LI></UL></UL>   **/
class mm_tegpt extends cl_pbase
{									
	private JButton btnPRNT;								/**TextField For Gate pass Number	*/
	private JTextField txtMGPNO;	/**TextField For Gate Pass Date		*/
	private JTextField txtMGPDT;	/**TextField For Department Code	*/
	private JTextField txtDPTCD;	/**TextField For Department Name	*/
	private JTextField txtDPTNM;	/**TextField For Vehicle Number		*/
	private JTextField txtVEHNO;	/**TextField For Carrier Name		*/
	private JTextField txtAPRVL;	/**TextField For Approximate Value  */
	private JTextField txtVEHDS;	/**TextField For Vendor Code		*/
	private JTextField txtVENCD;	/**TextField For Vendor Name		*/
	private JTextField txtVENNM;	/**TextField For GRIN Number		*/
	private JTextField txtGRNNO;	/**TextField For Due Date			*/
//	private JTextField txtDUEDT;	/**TextField For Remark				*/
	private JTextArea txtREMDS;	 /**	TextField For Remark			*/
	private JTextField txtPKGCT;  /**	No. of packages			*/
	private JTextField txtCOMNT;
	private JTextField txtEDITR;	/**TextEditor for tabel				*/
	private JTextField txtEDITR1;	/**TextField For Gate In Number		*/
	private JTextField txtGINNO;	/**TextField For Material Code In Table*/
	private JTextField txtMATCD;	/**TextField For Due Date In Table	*/	
	private JTextField txtDUEDT1;	
	private JTextField txtRECEDTR;	/**Text Field for status of Gate Pass */
	private JTextField txtSTSFL;	
	private JCheckBox chbINSFL;	    /**Check Box For Insurance applied or not */ 
	private JComboBox cmbMGPTP;		/**Combo box for gate pass type		*/
									/**Combo for forward to */
	private JComboBox cmbFRWTO;	/**Table for gate pass details */
	private cl_JTable tblGPDTL;		/**Double value for Issue quantity	*/
	private double dblISSQT;		/**Double value for Received quantity*/
	private double dblRECQT;		/**Double value for Temp Issue quantity */
	private double dblTISSQT;		/**Double value for Temp Received quantity */
	private double dblTRECQT;		/**Double value for Gate Pass Quantity */
	private double dblGPQTY;		/**int for row count with intial value 30*/
	private int intROWCNT = 30;		/**int for tabel row number		*/
	private int intTBLROW;			/**int for tabel column number	*/
	private int intTBLCOL;			/**String for status flage		*/
	private String strSTSFL;		/**String for GRN Number	*/
	private String strGRNNO;		/**String for Forward To	*/
	private String strFRWTO;		/**String for document type		*/
	private String strCLSNM;		/**Class Name for checking at runtime	*/
	private final String strDOCTP = "GP";	/**String for transfer flage with initial value 0*/
    private final String strTRNFL = "0";    /**String for transction type with intial value GP*/        
	private final String strTRNTP = "GP";	
	private String strPREBY = "",strAPRBY ="";	
	private String strMGPNO,strMGPTP,strMGPDS,strSTRTP;
									/**ResultSet for dispying data	*/
	private ResultSet rstRSSET;		/**Resultset for validateing data*/
	private ResultSet rstVLDRS;		/**Hashtable for gate pass type */
	private Hashtable<String,String> hstMGPTP;		/**Vector for gate pass quantity used in modification */
	private Vector<String> vtrGPQTY;		/**Vector for maximum issue quantity for gate pass	*/
	private Vector<String> vtrMAXQT;		
	private boolean flgINPVF=true;	/**Flag to check remark for modification */
	private boolean flgMODIF= false;
	private boolean flgCMMOD= false;
	private Hashtable<String,String> hstSTSFL;
	private JScrollPane jsp;
									/**Constant int variabel for check flag column */
	final int TBL_CHKFL = 0;		/**final int variabel for material code column	*/
	final int TBL_MATCD = 1;		/**final int variabel for material description column	*/
	final int TBL_MATDS = 2;		/**final int variable for unit of measurment column	*/
	final int TBL_UOMCD = 3;		/**final int variable for issue quantity column	*/
	final int TBL_ISSQT = 4;		/**final int variable for received quantity column	*/
    final int TBL_RECQT = 5;		/**final int variabel for due date column	*/
	final int TBL_DUEDT = 6;

	//	final int TBL_INSFL = 7;
	/**Constructor for the Gate Pass Entry Program which insert gate pass type in cmbMGPTP
	 * and gate pass tyep as key and number as value in hstMGPTP*/
	mm_tegpt()
	{
		super(2);
		try
		{
			strCLSNM = getClass().getName();
			txtMATCD = new TxtNumLimit(10.0);
			hstMGPTP = new Hashtable<String,String>(4,0.5f);
			cmbMGPTP = new JComboBox();
			vtrGPQTY = new Vector<String>(10,2);
			vtrMAXQT = new Vector<String>(10,2);
			strSTSFL = "";
			strGRNNO = "";
			strFRWTO = "";
			
			//Method To Add Gate Pass Name to Gate Pass Type ComboBox and add To 
			//hstMGPTP Gate Pass Name as key and Gate Pass Number as value
			
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS,CMT_CHP02,CMT_CGMTP FROM CO_CDTRN"
				+" WHERE CMT_CGMTP + CMT_CGSTP in ('D"+cl_dat.M_strCMPCD_pbst+"MMXXMGP','A"+cl_dat.M_strCMPCD_pbst+"MMXXGPT') AND isnull(CMT_STSFL,'')<>'X' ORDER BY CMT_CGMTP";

			M_rstRSSET =cl_dat.exeSQLQRY(M_strSQLQRY);
			Vector<String> L_vtrAUTNM = new Vector<String>();
			while(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("CMT_CGMTP").equals("AUT"))
				{
				//	strAUTNM ="";
					L_vtrAUTNM.add(M_rstRSSET.getString("CMT_CODCD"));
				//	if(M_rstRSSET.getString("CMT_CHP02").equals("AUT"))
				//		strAUTNM = M_rstRSSET.getString("CMT_CODCD");
				}
				if(M_rstRSSET.getString("CMT_CGMTP").equals("D"+cl_dat.M_strCMPCD_pbst))
				{
					if(strCLSNM.equals("mm_tegp1"))
						cmbMGPTP.addItem(M_rstRSSET.getString("CMT_CODDS"));
					else
					{
						if(nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"").equals("Y"))
							cmbMGPTP.addItem(M_rstRSSET.getString("CMT_CODDS"));
						else
						{
							if(L_vtrAUTNM.contains(cl_dat.M_strUSRCD_pbst))
								cmbMGPTP.addItem(M_rstRSSET.getString("CMT_CODDS"));
						}
					}
					//Store Gate Pass Name and Number In HashTable 
					hstMGPTP.put(M_rstRSSET.getString("CMT_CODDS"),M_rstRSSET.getString("CMT_CODCD").substring(1,3));
				}
				
			}
			M_rstRSSET.close();
			
			hstSTSFL = new Hashtable<String,String>(4,2.0f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'STS' AND CMT_CGSTP = 'MMXXGPS' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				hstSTSFL.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
			}
			M_rstRSSET.close();
		
			setMatrix(20,6);
			add(btnPRNT = new JButton("Print"),1,6,1,1,this,'L');
			add(new JLabel("Gate Pass Type"),2,1,1,1,this,'L');
			add(cmbMGPTP,2,2,1,1,this,'L');
			add(new JLabel("Gate Pass No"),2,3,1,1,this,'L'); 
			add(txtMGPNO = new TxtNumLimit(8.0),2,4,1,1,this,'L');
			add(new JLabel("Gate Pass Date"),2,5,1,1,this,'L'); 
			add(txtMGPDT = new TxtDate(),2,6,1,1,this,'L');
			add(new JLabel("Dept.code"),3,1,1,1,this,'L');
			add(txtDPTCD = new TxtNumLimit(3.0),3,2,1,1,this,'L');
			add(new JLabel("Dept.Name"),3,3,1,1,this,'L');
			add(txtDPTNM = new JTextField(),3,4,1,1,this,'L');
			txtDPTNM.setDisabledTextColor(Color.gray);
			add(new JLabel("Gate-In Ref"),3,5,1,1,this,'L');
			add(txtGINNO = new JTextField(),3,6,1,1,this,'L');
			txtGINNO.setDisabledTextColor(Color.gray);
			add(new JLabel("Vehicle No"),4,1,1,1,this,'L');
			add(txtVEHNO = new TxtLimit(15),4,2,1,1,this,'L');
		
			add(new JLabel("Approx. Value"),4,3,1,1,this,'L');
			add(txtAPRVL = new TxtNumLimit(12.2),4,4,1,1,this,'L');
		
			add(new JLabel("No.of Packages"),4,5,1,1,this,'L');
			add(txtPKGCT = new TxtNumLimit(2.0),4,6,1,1,this,'L');
		
			add(new JLabel("Carrier Name"),5,1,1,1,this,'L');
			add(txtVEHDS = new TxtLimit(40),5,2,1,3,this,'L');
			add(chbINSFL = new JCheckBox("Insurance Flag"),5,6,1,2,this,'L');
		//	add(new JLabel("Due Date"),5,5,1,1,this,'L');
		//	add(txtDUEDT = new TxtDate(),5,6,1,1,this,'L');
		
			add(new JLabel("Vendor"),6,1,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),6,2,1,1,this,'L');
			add(txtVENNM = new TxtLimit(40),6,3,1,2,this,'L');
			
			add(new JLabel("GRIN No"),6,5,1,1,this,'L'); 
			add(txtGRNNO = new TxtNumLimit(8.0),6,6,1,1,this,'L');
		
			txtREMDS = new JTextArea();
			txtREMDS.setLineWrap(true);
		//	jsp = new JScrollPane(txtREMDS,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	 
			add(new JLabel("Remarks"),7,1,1,1,this,'L'); 
		//	add(jsp,7,2,1.5,5,this,'L');
			add(txtREMDS,7,2,1.5,5,this,'L');
			txtREMDS.setRows(2);
			txtREMDS.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			add(new JLabel("Comments"),9,1,1,1,this,'L'); 
			add(txtCOMNT = new TxtLimit(200),9,2,1,5,this,'L');
		
			tblGPDTL = crtTBLPNL1(this,new String[]{"","Material Code","Material Description","UOM","Qty.Issued","Qty.Received","Due Date"},30,10,1,7.76,6,new int[]{20,85,350,45,80,80,85},new int[]{0});
			add(new JLabel("Forward To"),18,1,1,1,this,'L');
			add(cmbFRWTO = new JComboBox(),18,2,1,1,this,'R');
			add(new JLabel("Gate Pass Status"),18,4,1,1,this,'L');
			add(txtSTSFL = new JTextField(),18,5,1,2,this,'L');
			txtSTSFL.setDisabledTextColor(Color.gray);
			//add(chbINSFL = new JCheckBox("Insurence"),18,3,1,1,this,'L');
			txtEDITR1 = (JTextField)(tblGPDTL.cmpEDITR[TBL_MATCD]);
			txtRECEDTR = new JTextField();
			txtRECEDTR.setDisabledTextColor(Color.gray);
			txtRECEDTR.setEnabled(false);
		
			tblGPDTL.setCellEditor(TBL_ISSQT,new TxtNumLimit(12.3));
			tblGPDTL.cmpEDITR[TBL_ISSQT].addKeyListener(this);
			tblGPDTL.setCellEditor(TBL_MATCD,txtMATCD);
			tblGPDTL.setCellEditor(TBL_DUEDT,txtDUEDT1 = new TxtDate());
		
			txtMATCD.addKeyListener(this);
			txtEDITR1.addKeyListener(this);
			tblGPDTL.addKeyListener(this);
			txtMGPNO.setInputVerifier(new INPVF());
			txtDPTCD.setInputVerifier(new INPVF());
			txtVENCD.setInputVerifier(new INPVF());
			txtGRNNO.setInputVerifier(new INPVF());
			//txtDUEDT.setInputVerifier(new INPVF());
			cmbFRWTO.setInputVerifier(new INPVF());
			txtDUEDT1.setInputVerifier(new INPVF());
			tblGPDTL.setInputVerifier(new TBLINPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		//POPULATING FORWARD TO USERS COMBO
			cmbFRWTO.addItem("Select");
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP "
							+" = 'A"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXGPF' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				cmbFRWTO.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""));
			}
			M_rstRSSET.close();
			
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"In Constructor");
		}
	}
	/** Method on action performed 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			
			if(M_objSOURC == btnPRNT)
			{
				if(txtMGPNO.getText().trim().length() ==8)
					strMGPNO = txtMGPNO.getText().trim();
				if(strMGPNO.length() ==0)
				{
					setMSG("Gate Pass number is not specified..",'E');
					return;
				}
				// In case of Addition, data is already maintained in varibles befor the screen is cleared
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					strMGPTP = hstMGPTP.get(cmbMGPTP.getSelectedItem()).toString();
					strMGPDS = cmbMGPTP.getSelectedItem().toString();
					strSTRTP = cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString();
				}
				mm_rpgpp objGPPRP = new mm_rpgpp(M_strSBSCD);
				objGPPRP.genRPFIL(strMGPNO,strMGPNO,strMGPTP,strMGPDS,strSTRTP);
			//	objGPPRP.genRPFIL(strMGPNO,strMGPNO,"51","xxx",cl_dat.M_strSBSL2_pbst);
				//objINDRP.getALLREC(strINDNO,strINDNO,'I',"PI");
				JComboBox L_cmbLOCAL = objGPPRP.getPRNLS();
				objGPPRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpgpp.doc",L_cmbLOCAL.getSelectedIndex());
					
			}
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				txtREMDS.setText("");
				setENBL(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					txtVENCD.setEnabled(true);
				if(tblGPDTL.isEditing())
					tblGPDTL.getCellEditor().stopCellEditing();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					txtMGPDT.setText(cl_dat.M_strLOGDT_pbst);
				cmbMGPTP.requestFocus();
			}
			if(M_objSOURC == cmbMGPTP)
			{
				int L_cmbINDEX = cmbMGPTP.getSelectedIndex();
				clrCOMP();
				txtREMDS.setText("");
				tblGPDTL.clrTABLE();
				if(tblGPDTL.isEditing())
					tblGPDTL.getCellEditor().stopCellEditing();
				cmbMGPTP.setSelectedIndex(L_cmbINDEX);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					setENBL(false);
					txtMGPDT.setText(cl_dat.M_strLOGDT_pbst);
					// If NRGP enable gate in number else disable
					if(cmbMGPTP.getSelectedItem().toString().equals("Non-Returnable"))
					{
						if(strCLSNM.equals("mm_tegp1"))
							txtGINNO.setEnabled(true);
						else
							txtGINNO.setEnabled(false);
					}
					else
						txtGINNO.setEnabled(false);
				}
				
			}
			if(M_objSOURC == txtGINNO)
			{
				if(cmbMGPTP.getSelectedItem().toString().equals("Non-Returnable"))
				{
					M_strSQLQRY ="Select WB_PRTCD,WB_PRTDS from MM_WBTRN WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(wb_docrf,'') ='' and isnull(wb_stsfl,'') <>'X' and WB_DOCNO ='"+txtGINNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						if(M_rstRSSET.next())
						{
							txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("WB_PRTCD"),""));
							txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),""));
						}
						tblGPDTL.clrTABLE();
						tblGPDTL.setValueAt("99999999",0,TBL_MATCD);
						tblGPDTL.setValueAt("Dummy Material for NRGP against gate in Ref",0,TBL_MATDS);
						M_rstRSSET.close();
					}
					else
					{
						setMSG("Invalid Gate in number ..",'E');
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	/**Event on key pressed on F1 for displaying help and Enter 
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				flgINPVF=false;
				setCursor(cl_dat.M_curWTSTS_pbst);
				//display help for gate pass number 
				if(M_objSOURC == txtMGPNO)			// Material Gate Pass
				{	
					M_strHLPFLD = "txtMGPNO";
					M_strSQLQRY = "SELECT DISTINCT GP_MGPNO,GP_MGPDT,GP_DPTCD FROM MM_GPTRN "
						+"WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"
						+hstMGPTP.get(cmbMGPTP.getSelectedItem()).toString()+"' ";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						if(strCLSNM.equals("mm_tegpt")) // For users
							M_strSQLQRY += "AND isnull(GP_STSFL,'') IN ('0','1','2') ";
						else
						{
							if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Returnable"))
								M_strSQLQRY += "AND isnull(GP_STSFL,'') NOT IN ('6','9','C','X') ";
							else
								M_strSQLQRY += "AND isnull(GP_STSFL,'') IN ('0','1','2','3') ";
						}
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						if(strCLSNM.equals("mm_tegpt")) // For users
							M_strSQLQRY += "AND isnull(GP_STSFL,'') IN ('0','1','2') ";
						else
							M_strSQLQRY += "AND isnull(GP_STSFL,'') IN ('0','1','2','3','4') ";
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))	//new added on 29/09/2004
						M_strSQLQRY += "AND isnull(GP_STSFL,'') <> 'X' ";
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						if(strCLSNM.equals("mm_tegpt")) // Regular module
						{
							/*if(strAUTNM.equals(cl_dat.M_strUSRCD_pbst))
						    {
								M_strSQLQRY += " AND isnull(GP_STSFL,'') = '2' "
								+"AND GP_FRWTO = '"+cl_dat.M_strUSRCD_pbst+"'";
							}
							else
							{*/
								//M_strSQLQRY += " AND isnull(GP_STSFL,'') = '2' "
                                M_strSQLQRY += " AND isnull(GP_STSFL,'') = '1' "
								+"AND GP_FRWTO = '"+cl_dat.M_strUSRCD_pbst+"'";
							//}
						}
						else if(strCLSNM.equals("mm_tegp1")) // Auth. stores prog
						{
							M_strSQLQRY += "AND isnull(GP_STSFL,'') = '3' ";
						}
					}
					if(txtMGPNO.getText().trim().length() > 0)
						M_strSQLQRY += "AND GP_MGPNO LIKE '"+txtMGPNO.getText().trim()+"%' ";
					M_strSQLQRY += " ORDER BY GP_MGPNO DESC";
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Gate Pass","Date","Department"},3,"CT");
				}
				//Display help window for department code and name
				else if(M_objSOURC == txtDPTCD)		
				{
					M_strHLPFLD = "txtDPTCD";
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN "
						+"WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' ";
					if(txtDPTCD.getText().trim().length() > 0)
						M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtDPTCD.getText().trim()+"%' ";
					M_strSQLQRY += "ORDER BY CMT_CODDS";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Department"},2,"CT");
				}
				//display help window for GRN number 
				else if(M_objSOURC == txtGRNNO)		
				{
					M_strHLPFLD = "txtGRNNO";
					M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_MATCD,GR_REJQT,GR_ISSQT FROM MM_GRMST "
						+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(GR_REJQT,0) > 0 AND "
						+"isnull(GR_STSFL,'') NOT IN ('C','X') AND isnull(GR_GPTAG,'') <> 'C' ";
					if(txtGRNNO.getText().trim().length() > 0)
						M_strSQLQRY += "AND GR_GRNNO LIKE '"+txtGRNNO.getText().trim()+"%' ";
					M_strSQLQRY += "ORDER BY GR_GRNNO DESC";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN No","Mat. Code","Rej. Qty.","Issue Qty."},4,"CT");
				}
				//Display help window for vendor code
				else if(M_objSOURC == txtVENCD)		
				{
					M_strHLPFLD = "txtVENCD";
					M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04 FROM CO_PTMST WHERE PT_PRTTP = 'S' ";
					if(txtVENCD.getText().trim().length() > 0)
						M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtVENCD.getText().trim().toUpperCase()+"%' ";
					M_strSQLQRY += "ORDER BY PT_PRTNM";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Name","Address1","Address2","Address3","Address4"},6,"CT",new int[]{100,300,80,80,80,80});
				}
				//Display help window for gate in number
				else if(M_objSOURC == txtGINNO)		
				{	
					M_strHLPFLD = "txtGINNO";
					M_strSQLQRY = "SELECT WB_DOCNO,WB_LRYNO,WB_TPRDS FROM MM_WBTRN "
						+"WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '02' AND isnull(WB_STSFL,'') <> 'X'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Gate-In No","Lorry","Transporter"},3,"CT");
				}
				//Display help windoiw for material code
				else if(M_objSOURC == txtMATCD)		
				{		
					intTBLCOL = tblGPDTL.getSelectedColumn();
					if(intTBLCOL == TBL_MATCD)
					{
						M_strHLPFLD = "txtMATCD";
						M_strSQLQRY = "SELECT DISTINCT CT_MATCD,CT_MATDS,CT_UOMCD FROM CO_CTMST "
								+"WHERE isnull(CT_STSFL,'') <> 'X' AND CT_CODTP = 'CD' ";
						if(txtMATCD.getText().trim().length() > 0)
							M_strSQLQRY += "AND CT_MATCD LIKE '"+txtMATCD.getText().trim()+"%' ";
						M_strSQLQRY += "ORDER BY CT_MATDS ";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"CODE","DESCRIPTION","UOM"},3,"CT");
					}
				}
				//display help window for gate pass forwart to 
/*				else if(M_objSOURC == txtFRWTO)
				{
					M_strHLPFLD = "txtFRWTO";
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP "
							+" = 'A"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXGPF' ";
					if(txtFRWTO.getText().trim().length() > 0)
						M_strSQLQRY += " AND CMT_CODCD LIKE '"+txtFRWTO.getText().trim()+"%' ";
					M_strSQLQRY += " ORDER BY CMT_CODCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"User Code","User Name"},2,"CT");
				}
*/				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			
			if(L_KE.getKeyCode() == L_KE.VK_F9)
			{
				if(M_objSOURC == txtVENCD)
				{
					M_strHLPFLD = "txtVENCD";
					M_strSQLQRY = " SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04 FROM CO_PTMST WHERE PT_PRTTP = 'S' ";
					if(txtVENCD.getText().trim().length() > 0)
						M_strSQLQRY += " AND PT_PRTCD ='"+txtVENCD.getText().trim().toUpperCase()+"' ";
					M_strSQLQRY += " ORDER BY PT_PRTNM";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					dspADDR(M_rstRSSET);
				}	
			}
			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtMGPNO)
				{
					if(txtMGPNO.getText().trim().length() > 0)
					{
						getGPDTL();		//call method to get gate pass details
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							txtDPTCD.requestFocus();
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
							txtVENCD.setEnabled(true);
					}
				}
				else if(M_objSOURC == txtVEHNO)
				{
					txtVEHNO.setText(txtVEHNO.getText().toUpperCase());
					txtAPRVL.requestFocus();
				}
				else if(M_objSOURC == txtAPRVL)
				{
					txtPKGCT.requestFocus();
				}
				else if(M_objSOURC == txtPKGCT)
				{
					txtVEHDS.requestFocus();
				}
				else if(M_objSOURC == txtVEHDS)
				{
					txtVEHDS.setText(txtVEHDS.getText().toUpperCase());
					((JComponent)M_objSOURC).transferFocus();
				}
				else if(M_objSOURC == txtGRNNO)
				{
					if(txtGRNNO.getText().trim().length() > 0)
					{
						getGRNDTL();	//call method to get GRN Details
						txtREMDS.requestFocus();
					}
				}
				else if(M_objSOURC == txtREMDS)
				{
					tblGPDTL.requestFocus();
					tblGPDTL.setRowSelectionInterval(0,0);
					if(cmbMGPTP.getSelectedItem().toString().equals("Rejection"))
					{
						tblGPDTL.setColumnSelectionInterval(TBL_ISSQT,TBL_ISSQT);
						tblGPDTL.editCellAt(0,TBL_ISSQT);
					}
					else
					{
						tblGPDTL.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);
						tblGPDTL.editCellAt(0,TBL_MATCD);
					}
				}
				else if(M_objSOURC == cmbFRWTO)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					((JComponent)M_objSOURC).transferFocus();	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	

	private void dspADDR(ResultSet L_rstRSSET)
	{
		try
		{
			if(L_rstRSSET!=null && L_rstRSSET.next())
			{
				JOptionPane.showMessageDialog(null,getRSTVAL(L_rstRSSET,"PT_PRTCD","C")+"\n"
															+getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
															+getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
															+getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
															+getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n"
															+getRSTVAL(L_rstRSSET,"PT_ADR04","C")+"\n");
			}
			else
				JOptionPane.showMessageDialog(null,"Please Enter Vendor Code");
			if(L_rstRSSET==null)
				L_rstRSSET.close();
		}
		catch(Exception e){setMSG(e,"dspADDR");}
	}
	
private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
{
	//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
    try
    {
	if (LP_FLDTP.equals("C"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
		//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
	else if (LP_FLDTP.equals("N"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
	else if (LP_FLDTP.equals("D"))
		return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
	else if (LP_FLDTP.equals("T"))
	    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
	else 
		return " ";
	}
	catch (Exception L_EX)
	{setMSG(L_EX,"getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);}
return " ";
} 
	
	/** method to insert the selected field form help window in to the text field 
	 */
	public void exeHLPOK()
	{
		flgINPVF=true;
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtMGPNO"))				
			{
				txtMGPNO.setText(cl_dat.M_strHLPSTR_pbst);
				getGPDTL();	//call method for gate pass detail for given gate pass number
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					txtDPTCD.requestFocus();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					txtVENCD.setEnabled(true);
			}
			else if(M_strHLPFLD.equals("txtDPTCD"))	
			{
				txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtDPTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				txtVEHNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtGRNNO"))	
			{
				txtGRNNO.setText(cl_dat.M_strHLPSTR_pbst);
				getGRNDTL();	//call method to get GRIN Detail for given GRIN Number 
				txtGRNNO.setEnabled(false);
				txtVENCD.setEnabled(false);
			}
			//insert selected field in vendor code and name for help window and if that vendor code
			//is Z0999 ro Z9999 then allowed to edit vendor name
			else if(M_strHLPFLD.equals("txtVENCD"))	
			{
				txtVENCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtVENNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				if(txtVENCD.getText().equals("Z0999") || txtVENCD.getText().equals("Z9999"))
				{
					txtVENNM.setEnabled(true);
					txtVENNM.requestFocus();
				}
				else if((cmbMGPTP.getSelectedItem().toString().equals("Rejection"))||(cmbMGPTP.getSelectedItem().toString().equals("Scrap")))
				{
					txtGRNNO.requestFocus();
				}
				else
					txtREMDS.requestFocus();    
			}
			else if(M_strHLPFLD.equals("txtGINNO"))	
			{
				txtGINNO.setText(cl_dat.M_strHLPSTR_pbst);
			//	tblGPDTL.requestFocus();
			//	tblGPDTL.setRowSelectionInterval(0,0);
			//	tblGPDTL.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);
			//	tblGPDTL.editCellAt(0,TBL_MATCD);
			}
			else if(M_strHLPFLD.equals("txtMATCD"))	
			{
				intTBLROW = tblGPDTL.getSelectedRow();
				txtMATCD.setText(cl_dat.M_strHLPSTR_pbst);
				tblGPDTL.setValueAt(cl_dat.M_strHLPSTR_pbst,intTBLROW,TBL_MATCD);
				tblGPDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),intTBLROW,TBL_MATDS);
				tblGPDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intTBLROW,TBL_UOMCD);
				tblGPDTL.setColumnSelectionInterval(TBL_UOMCD,TBL_ISSQT);
				tblGPDTL.editCellAt(intTBLROW,TBL_ISSQT);
			}
/*			else if(M_strHLPFLD.equals("txtFRWTO"))
			{
				txtFRWTO.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
*/		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
		}
	}
	/** Method to display message for focusing on the component 
	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == cmbMGPTP)
				setMSG("Select Gate Pass Type ..",'N');
			else if(M_objSOURC == txtMGPNO)
				setMSG("Enter Enter Gate Pass Number. 'F1' For Help ..",'N');
			else if(M_objSOURC == txtDPTCD)
				setMSG("Enter Dept. 'F1' For Help ..",'N');
			else if(M_objSOURC == txtVEHNO)
				setMSG("Enter Vehicle Number..",'N');
			else if(M_objSOURC == txtVEHDS)
				setMSG("Enter Carrier Name..",'N');
		//	else if(M_objSOURC == txtDUEDT)
		//		setMSG("Enter Due Date..",'N');
			else if(M_objSOURC == txtVENCD)
				setMSG("Enter Vendor Code. 'F1' For Help Or 'F9' for Vendor Description..",'N');
			else if(M_objSOURC == txtVENNM)
				setMSG("Enter Vendore Name ..",'N');
			else if(M_objSOURC == txtGRNNO)
				setMSG("Enter GRIN Number. 'F1' For Help..",'N');
			else if(M_objSOURC == txtREMDS)
				setMSG("Enter Remark...",'N');
			else if(M_objSOURC == txtMATCD)
				setMSG("Enter Material Code For Gate Pass ..",'N');
	//		else if(M_objSOURC == cmbFRWTO)
	//			setMSG("Select Forward To User ..",'N');
		}
		catch(Exception e)
		{
			setMSG(e,"Child.FocusGained");
		}
	}
	/** Method to set enabled the component for the given condition
	 */
	public void setENBL(boolean ACTION)
	{
		super.setENBL(ACTION);
		btnPRNT.setEnabled(true);
		tblGPDTL.setEnabled(false);
		cmbMGPTP.setEnabled(true);
		btnPRNT.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			txtMGPNO.setEnabled(false);
		else
			txtMGPNO.setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			btnPRNT.setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			tblGPDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
		if(cmbMGPTP.getSelectedItem().toString().equals("Returnable"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				//txtDUEDT.setEnabled(true);
				tblGPDTL.cmpEDITR[TBL_DUEDT].setEnabled(true);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					tblGPDTL.cmpEDITR[TBL_MATCD].setEnabled(true);
			}
		}
		else
		{
			tblGPDTL.cmpEDITR[TBL_DUEDT].setEnabled(false);
		//	txtDUEDT.setEnabled(false);
		}
		if(cmbMGPTP.getSelectedItem().toString().equals("Rejection"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				txtGRNNO.setEnabled(true);
		}
		else if(cmbMGPTP.getSelectedItem().toString().equals("Scrap"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				txtGRNNO.setEnabled(true);
		}
		else
			txtGRNNO.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{
			//chbINSFL.setEnabled(true);
			if(strSTSFL.equals("") || strSTSFL.equals("0")|| strSTSFL.equals("1") || strSTSFL.equals("2") || strSTSFL.equals("3") || strSTSFL.equals("4"))
			{
				txtDPTCD.setEnabled(true);
				txtVEHNO.setEnabled(true);
				txtAPRVL.setEnabled(true);
				txtPKGCT.setEnabled(true);
				chbINSFL.setEnabled(true);
				txtVEHDS.setEnabled(true);
				txtVENCD.setEnabled(true);
				txtREMDS.setEnabled(true);
				txtCOMNT.setEnabled(true);
				tblGPDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
				tblGPDTL.cmpEDITR[TBL_MATCD].setEnabled(true);
				// IN STATUS 3,4 THIS SHOULD BE DISABLED
				tblGPDTL.cmpEDITR[TBL_ISSQT].setEnabled(true);
			//	tblGPDTL.cmpEDITR[TBL_INSFL].setEnabled(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) )
				cmbFRWTO.setEnabled(false);
			else
				cmbFRWTO.setEnabled(true);
		}
		txtMGPDT.setEnabled(false);
		txtDPTNM.setEnabled(false);
		txtVENNM.setEnabled(false);
		txtGINNO.setEnabled(false);
	//	txtREMDS.setEnabled(false);
		/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) )
		{
			txtREMDS.setEnabled(true);
			txtREMDS.setEditable(false);
		}*/
	}
	/** method to get Gate Pass Detail and display in the tabel and screeen for the given gate pass number
	 */
	//public void getGPDTL(String LP_STRTP,String LP_MGPTP,String LP_MGPNO)
	public void getGPDTL()
	{
		boolean L_FIRST = true;
        int L_RECCNT1 = 0;
		if(vtrMAXQT !=null)
			vtrMAXQT.clear();	//clear vector hold max issue quantity
		if(vtrGPQTY !=null)
			vtrGPQTY.clear();	//clear vector hold gate pass quantity
		flgMODIF = false;
		flgCMMOD = false;
		try
		{
			if(tblGPDTL.isEditing())
				tblGPDTL.getCellEditor().stopCellEditing();
			tblGPDTL.clrTABLE();
			if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))
			{
				M_strSQLQRY = "SELECT GP_MGPNO,GP_MATCD,GP_MGPDT,GP_VENCD,GP_VENNM,GP_DUEDT,GP_STSFL,"
					+"GP_FRWTO,GP_ISSQT,GP_RECQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,GP_GRNNO,GP_GINNO,GP_INSFL,GP_STSFL,GP_APRVL,"
					+"CT_MATDS,CT_UOMCD,GR_REJQT,GR_GPQTY,GP_PREBY,GP_APRBY,GP_PKGCT FROM MM_GPTRN,CO_CTMST,MM_GRMST "
					+"WHERE CT_MATCD = GP_MATCD AND GR_STRTP = GP_STRTP AND GR_GRNNO = GP_GRNNO "
					+"AND GR_MATCD = GP_MATCD AND GR_CMPCD=GP_CMPCD AND GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
					+"AND GP_MGPTP = '"+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND "
					+"GP_MGPNO = '"+txtMGPNO.getText().trim()+"' AND ";
				//Allow Those Gate Pass Which are not gate out for modification and deletion
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					M_strSQLQRY += "isnull(GP_STSFL,'') IN ('0','1','2','3','4') AND isnull(GR_STSFL,'') <> 'X' AND isnull(CT_STSFL,'') <> 'X'";
				//Allow all gate pass excepted delted for enquery
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					M_strSQLQRY += "isnull(GP_STSFL,'') <> 'X' AND isnull(GR_STSFL,'') <> 'X' AND isnull(CT_STSFL,'') <> 'X'";
				//allow gate pass who is not authorised i.e status flag is 2 i.e forwared to authorization
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					if(strCLSNM.equals("mm_tegpt")) // Regular module
					{
						M_strSQLQRY += "isnull(GP_stsfl,'') = '1' and isnull(GR_STSFL,'') <> 'X' "
						+"AND isnull(CT_STSFL,'') <> 'X' AND GP_FRWTO = '"+cl_dat.M_strUSRCD_pbst+"'";
	
					}
					else if(strCLSNM.equals("mm_tegp1")) // Auth. stores prog
					{
						M_strSQLQRY += " isnull(GP_STSFL,'') = '3' AND isnull(GR_STSFL,'') <> 'X' AND isnull(CT_STSFL,'') <> 'X'";
					}
				}		
			}
			else
			{
				M_strSQLQRY = "SELECT GP_MGPNO,GP_MATCD,GP_MGPDT,GP_VENCD,GP_VENNM,GP_DUEDT,GP_STSFL,GP_FRWTO," 
					+"GP_ISSQT,GP_RECQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,GP_GRNNO,GP_GINNO,GP_INSFL,GP_STSFL,GP_PREBY,GP_APRBY,GP_APRVL,GP_PKGCT,CT_MATDS,CT_UOMCD "
					+"FROM MM_GPTRN,CO_CTMST WHERE CT_MATCD = GP_MATCD AND GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"GP_MGPTP = '"+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND GP_MGPNO = '"+txtMGPNO.getText().trim()+"' AND ";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(strCLSNM.equals("mm_tegpt")) // For users
						M_strSQLQRY += "isnull(GP_STSFL,'') IN ('0','1','2') ";
					else
					{
						// For Stores
						if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Returnable"))
						{	
                            M_strSQLQRY += "isnull(GP_STSFL,'') NOT IN ('6','9','C','X') ";
                            M_strSQLQRY += " and GP_RCPDT IS NULL";
                        }
                    	else
							//M_strSQLQRY += "isnull(GP_STSFL,'') IN ('1','2','3','4') ";
							M_strSQLQRY += "isnull(GP_STSFL,'') IN ('0','1','2','3') ";
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					if(strCLSNM.equals("mm_tegpt")) // For users
						M_strSQLQRY += "isnull(GP_STSFL,'') IN ('0','1','2') ";
					else
					{
						// For stores, If gate out is not done, item can be deleted
						M_strSQLQRY += "isnull(GP_STSFL,'') IN ('0','1','2','3','4') ";
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					M_strSQLQRY += "isnull(GP_STSFL,'') <> 'X' AND isnull(CT_STSFL,'') <> 'X' ";
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					if(strCLSNM.equals("mm_tegpt")) // Regular module
					{
						M_strSQLQRY += " isnull(GP_STSFL,'') = '1' AND isnull(CT_STSFL,'') <> 'X' "
						+"AND GP_FRWTO = '"+cl_dat.M_strUSRCD_pbst+"'";
					}
					else if(strCLSNM.equals("mm_tegp1")) // Auth. stores prog
					{
						M_strSQLQRY += " isnull(GP_STSFL,'') = '3' AND isnull(CT_STSFL,'') <> 'X' ";
					}
				}
			}
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				vtrGPQTY = new Vector<String>(10,2);
				while(M_rstRSSET.next())
				{
					strSTSFL = nvlSTRVL(M_rstRSSET.getString("GP_STSFL"),"");
					if(L_FIRST)
					{
                        L_FIRST = false;
                      	if(M_rstRSSET.getDate("GP_MGPDT") != null)
							txtMGPDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("GP_MGPDT")));
						txtMGPNO.setText(nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),""));
			            txtDPTCD.setText(nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),""));
                        txtGRNNO.setText(nvlSTRVL(M_rstRSSET.getString("GP_GRNNO"),""));
                        txtVEHNO.setText(nvlSTRVL(M_rstRSSET.getString("GP_VEHNO"),""));
			            txtVEHDS.setText(nvlSTRVL(M_rstRSSET.getString("GP_VEHDS"),""));
                        txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),""));
			            txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),""));
						txtAPRVL.setText(nvlSTRVL(M_rstRSSET.getString("GP_APRVL"),""));
						txtPKGCT.setText(nvlSTRVL(M_rstRSSET.getString("GP_PKGCT"),""));
						strPREBY = nvlSTRVL(M_rstRSSET.getString("GP_PREBY"),"");
						strAPRBY = nvlSTRVL(M_rstRSSET.getString("GP_APRBY"),"");
						//if(M_rstRSSET.getDate("GP_DUEDT") != null)
						//	txtDUEDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("GP_DUEDT")));
				        txtGINNO.setText(nvlSTRVL(M_rstRSSET.getString("GP_GINNO"),""));
						cmbFRWTO.setSelectedItem(nvlSTRVL(M_rstRSSET.getString("GP_FRWTO"),""));
						if(hstSTSFL.containsKey(strSTSFL))
							txtSTSFL.setText(nvlSTRVL(hstSTSFL.get(strSTSFL).toString(),""));
						// To get the remark from Remark Master
					    txtREMDS.setText("");
						txtCOMNT.setText("");
						String L_strQUERY = "SELECT RM_DOCTP,RM_REMDS FROM MM_RMMST WHERE "
							+"RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = '"+strTRNTP+"' "
							+"AND RM_DOCTP IN('GP','RQ') AND RM_DOCNO = '"+txtMGPNO.getText().trim()+"'"
							+" AND isnull(rm_remtp,'1') ='1'";
							/// lAST LINE ADDED IN QUERY AS IN CYLINDERS MULTIPLE REMARKS ARE THERE
							// IN MODI MODE LAST REMARK WAS GETTING SAVED IN ALL SERIALS
						rstRSSET = cl_dat.exeSQLQRY3(L_strQUERY);
						if(rstRSSET != null)
						{
							while(rstRSSET.next())
							{
								if(nvlSTRVL(rstRSSET.getString("RM_DOCTP"),"").equals("GP"))
								{
									String L_strREMDS = nvlSTRVL(rstRSSET.getString("RM_REMDS"),"");
									txtREMDS.setText(L_strREMDS);
									flgMODIF = true;  //set Flag true if Remark is present to indicate for modification of remark
								}
								else if(nvlSTRVL(rstRSSET.getString("RM_DOCTP"),"").equals("RQ"))
								{
									txtCOMNT.setText(nvlSTRVL(rstRSSET.getString("RM_REMDS"),""));
									flgCMMOD = true; // Flag Comment modification is set to true
								}
							}
							rstRSSET.close();
						}
					}
					dblISSQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0"));
                    dblRECQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0"));
					tblGPDTL.setValueAt(Boolean.TRUE,L_RECCNT1,TBL_CHKFL);
                    tblGPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),L_RECCNT1,TBL_MATCD);
					//tblGPDTL.getCellEditor(L_RECCNT1,TBL_MATCD).setEnabled(false);
					tblGPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_RECCNT1,TBL_MATDS);
					tblGPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),L_RECCNT1,TBL_UOMCD);
                    tblGPDTL.setValueAt(setNumberFormat(dblISSQT,3),L_RECCNT1,TBL_ISSQT);
                    tblGPDTL.setValueAt(setNumberFormat(dblRECQT,3),L_RECCNT1,TBL_RECQT);
					//If Gate Pass Type Is Rejection then Maximum Gate Pass Quantity is
					//GRIN Rejected Quantity - GRIN Gate Pass Qty. + GP Issue Quantity is store 
					//in verMAXQT to chenk the maximum quanntity for issue quantity of material for the gate pass
					if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))
						vtrMAXQT.addElement(String.valueOf(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_GPQTY"),"0")) + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0"))));
					//store Gate Pass Issue Quantity Of Curretnt Gate Pass Number In Vector
					vtrGPQTY.addElement(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0.0"));
					if(M_rstRSSET.getDate("GP_DUEDT") != null)
						tblGPDTL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("GP_DUEDT")),L_RECCNT1,TBL_DUEDT);
					else
						tblGPDTL.setValueAt("",L_RECCNT1,TBL_DUEDT);
					/*if(nvlSTRVL(M_rstRSSET.getString("GP_INSFL"),"").equals("Y"))
						tblGPDTL.setValueAt(Boolean.TRUE,L_RECCNT1,TBL_INSFL);
					else
						tblGPDTL.setValueAt(Boolean.FALSE,L_RECCNT1,TBL_INSFL);*/
					if(nvlSTRVL(M_rstRSSET.getString("GP_INSFL"),"").equals("Y"))
						chbINSFL.setSelected(true);
					else
						chbINSFL.setSelected(false);
					txtDPTNM.setText(cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXDPT",txtDPTCD.getText().trim()));
			        L_RECCNT1++;
                }
				M_rstRSSET.close();
				setENBL(false);
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					setMSG("Record Not Found..",'E');	
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Gate pass is not available for Authorisation..",'E');	
				else	
					setMSG("Record Not Found,or is not valid for this operation",'E');	
			}
			if(L_RECCNT1 ==0)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					setMSG("Record Not Found..",'E');	
				else
					setMSG("Record Not Found,or is not valid for this operation",'E');	
			}
		}
		catch(Exception e)
		{
			setMSG(e,"getGPDTL");
		}
    }
	/** Method to get GRIN Detail for a given GRN Number 
	 */
	public void getGRNDTL()
	{
		int L_RECCNT2 = 0;
		vtrMAXQT.clear();	//Clear Vector For Storing Maximium Value for Value Check For GP Qty
		boolean L_FIRST = true;
		try
		{
			M_strSQLQRY = "SELECT  GR_VENCD,GR_VENNM,GR_MATCD,GR_REJQT,GR_ISSQT,GR_GPQTY,"
				+"CT_MATDS,CT_UOMCD FROM MM_GRMST,CO_CTMST WHERE CT_MATCD = GR_MATCD AND "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(GR_STSFL,'') <> 'X' AND "
				+"isnull(CT_STSFL,'') <> 'X' AND GR_GRNNO = '"+txtGRNNO.getText()+"' AND "
				+"isnull(GR_REJQT,0) - isnull(GR_GPQTY,0) > 0 ";
			
			tblGPDTL.clrTABLE();
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(L_FIRST)
					{
						L_FIRST = false;
						txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""));
						txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""));
					}
					dblISSQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_ISSQT"),"0"));
                    dblRECQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0"));
					dblISSQT = dblRECQT - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_GPQTY"),"0"));
					tblGPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),L_RECCNT2,TBL_MATCD);
					tblGPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_RECCNT2,TBL_MATDS);
					tblGPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),L_RECCNT2,TBL_UOMCD);
                    tblGPDTL.setValueAt(setNumberFormat(dblISSQT,3),L_RECCNT2,TBL_ISSQT);
					vtrMAXQT.addElement(String.valueOf(dblISSQT));	//Mximum Quantity as Rejected Quantity - Gate Pass Quantity for material code
                    L_RECCNT2++;
                }
                M_rstRSSET.close();
			}
			txtREMDS.requestFocus();
			tblGPDTL.cmpEDITR[TBL_MATCD].setEnabled(false);
		}
		catch(SQLException L_SQL)
		{
			setMSG(L_SQL,"getGRNDTL");
		}
	}
	/** class to check input for the given input component 
	 */
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(!flgINPVF)
				{
					flgINPVF=true;
					return true;
				}
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length() == 0)
						return true;
				if(input == txtMGPNO)
				{
					M_strSQLQRY = "SELECT count(*) FROM MM_GPTRN WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND isnull(GP_STSFL,'') <> 'X' AND GP_MGPNO = '"+txtMGPNO.getText().trim()+"'";
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3(M_strSQLQRY);
					if(L_rstTEMP.next())
					{
						if(L_rstTEMP.getInt(1)<= 0)
						{
							setMSG("Invalid Gate Pass Number.. ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Gate Pass Number.. ",'E');
						return false;
					}
				}
				if(input == txtDPTCD)
				{
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select CMT_CODDS FROM CO_CDTRN WHERE  CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' AND CMT_CODCD = '"+txtDPTCD.getText()+"'");
					if(L_rstTEMP.next())
						txtDPTNM.setText(nvlSTRVL(L_rstTEMP.getString("CMT_CODDS"),""));
					else
					{
						setMSG("Invalid Department Code ",'E');
						return false;
					}
				}
				if(input == txtGRNNO)
				{
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("SELECT count(*) FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GR_REJQT > 0 AND isnull(GR_STSFL,'') NOT IN ('C','X') AND GR_GRNNO = '"+((JTextField)input).getText()+"'");
					if(L_rstTEMP.next())
					{
						if(L_rstTEMP.getInt(1)<= 0)
						{
							setMSG("Invalid GRIN Number.. ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid GRIN Number.. ",'E');
						return false;
					}
				}
				if(input == txtVENCD)
				{
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("SELECT PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP = 'S' AND isnull(PT_STSFL,'') <> 'X' AND PT_PRTCD = '"+((JTextField)input).getText().toUpperCase()+"'");
					if(L_rstTEMP.next())
					{
						txtVENCD.setText(txtVENCD.getText().toUpperCase());
						txtVENNM.setText(nvlSTRVL(L_rstTEMP.getString("PT_PRTNM"),""));
						if(txtVENCD.getText().equals("Z0999") || txtVENCD.getText().equals("Z9999"))
						{
							txtVENNM.setEnabled(true);
							txtVENNM.requestFocus();
						}
					}
					else
					{
						setMSG("Invalid Vendor Code ",'E');
						return false;
					}
				}
			/*	if(input == txtDUEDT)
				{
					if(M_fmtLCDAT.parse(txtDUEDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) < 0)
					{
						setMSG("Due Date Should Be Greater Than Todays Date",'E');
						return false;
					}
				}*/
/*				if(input == txtFRWTO)
				{	
					txtFRWTO.setText(txtFRWTO.getText().trim().toUpperCase());
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("SELECT COUNT(*) FROM CO_CDTRN WHERE CMT_CODCD = '"+txtFRWTO.getText().trim()+"' AND CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXGPF'");
					if(L_rstTEMP.next())
					{
						if(L_rstTEMP.getInt(1)<= 0)
						{
							setMSG("Invalid Forward For Authorisation Code Press 'F1' For Help .. ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Forward For Authorisation Code Press 'F1' For Help .. ",'E');
						return false;
					}
				}
*/			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"InputVerifier");
			}
			return true;
		}
	}
	/** class to check the input for the tabel
	 */
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			if(getSource() == tblGPDTL)
			{
				if(P_intCOLID == TBL_MATCD)	//Check For Valid Material Code
				{
					try
					{
						if(tblGPDTL.getValueAt(tblGPDTL.getSelectedRow(),TBL_MATCD).toString().equals("99999999"))
						{
							tblGPDTL.setValueAt("Dummy Material Code",tblGPDTL.getSelectedRow(),TBL_MATDS);
							return true;
						}
						M_strSQLQRY = "SELECT CT_MATDS,CT_UOMCD FROM CO_CTMST "
							+"WHERE isnull(CT_STSFL,'') <> 'X' AND CT_CODTP = 'CD' "
							+"AND CT_MATCD = '"+tblGPDTL.getValueAt(tblGPDTL.getSelectedRow(),TBL_MATCD).toString()+"'";
    					rstVLDRS =cl_dat.exeSQLQRY(M_strSQLQRY);
						if(rstVLDRS.next())
						{
							tblGPDTL.setValueAt(rstVLDRS.getString("CT_MATDS"),tblGPDTL.getSelectedRow(),TBL_MATDS);
							tblGPDTL.setValueAt(rstVLDRS.getString("CT_UOMCD"),tblGPDTL.getSelectedRow(),TBL_UOMCD);
							return true;
						}
						tblGPDTL.setValueAt("",tblGPDTL.getSelectedRow(),TBL_MATDS);
						tblGPDTL.setValueAt("",tblGPDTL.getSelectedRow(),TBL_UOMCD);
						setMSG("Invalid Material Code. Press F1 for help",'E');
					    if(rstVLDRS != null)
					        rstVLDRS.close();               
					}
					catch(Exception e)
					{
						setMSG(e,"vldMATCD");
						return false;
					}	
					return false;
				}
				if(P_intCOLID == TBL_ISSQT)	//Check For Issue Quantity
				{
					//If Issue Quantity is null or zero return false
					if(((JTextField)tblGPDTL.cmpEDITR[4]).getText().length()== 0 || Float.parseFloat(((JTextField)tblGPDTL.cmpEDITR[4]).getText()) == 0.0)
					{
						setMSG("Enter Issue Quantity",'E');
						return false;
					}
					//For The Case Of Rejection 
					if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))	//Rejection
					{
						//if Table Issue Quantity is Grater Than Vector Max Quantity Return false
						if(Float.parseFloat(tblGPDTL.getValueAt(tblGPDTL.getSelectedRow(),TBL_ISSQT).toString()) > Float.parseFloat(vtrMAXQT.elementAt(tblGPDTL.getSelectedRow()).toString()))
						{
							setMSG("Gate Pass Quantity Not More Than "+vtrMAXQT.elementAt(tblGPDTL.getSelectedRow()),'E');
							return false;
						}
					}
					if((strCLSNM.equals("mm_tegp1"))&&(strSTSFL.equals("3")))
					{
						// Qty. modi. by stores after Approval, increasing blocked.
						if(Float.parseFloat(tblGPDTL.getValueAt(tblGPDTL.getSelectedRow(),TBL_ISSQT).toString()) != Float.parseFloat(vtrGPQTY.elementAt(tblGPDTL.getSelectedRow()).toString()))
						{
							setMSG("Changing G.P.Qty Not allowed at this stage.. ",'E');
							return false;
						}
						
					}
													  
				}
				if(P_intCOLID == TBL_DUEDT)
				{
					try
					{
						//If Gate Pass Is Returnable Check For Due Date
						if((cmbMGPTP.getSelectedItem().toString().equals("Returnable"))&&(txtDUEDT1.getText().trim().length() > 0))
						if(M_fmtLCDAT.parse(txtDUEDT1.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) < 0)
						{
							setMSG("Due Date Should Be Greater Than Todays Date",'E');
							return false;
						}
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"Table Input Verifier Due Date");
						return false;
					}
				}
			}
			return true;
		}
	}
	/** Method to validate data of the input component 
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtMGPDT.getText().trim().length() == 0)
			{
				setMSG("Enter Gate Pass Date..",'E');
				txtMGPDT.requestFocus();
				return false;
			}
			if(txtDPTCD.getText().trim().length() < 3)
			{
				setMSG("Enter Department Or Press 'F1' For Help..",'E');
				txtDPTCD.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(txtAPRVL.getText().trim().length() == 0)
				{
					setMSG("Enter Approximate value of Gate Pass ..",'E');
					txtAPRVL.requestFocus();
					return false;
				}
				if(txtPKGCT.getText().trim().length() == 0)
					{
						setMSG("Enter Total number of packages ..",'E');
						txtPKGCT.requestFocus();
						return false;
					}
				if(Integer.parseInt(txtPKGCT.getText().trim()) == 0)
				{
					setMSG("Total number of packages can not be zero..",'E');
					txtPKGCT.requestFocus();
					return false;
				}
			}
			if((txtREMDS.getText().indexOf("'")>=0)||(txtREMDS.getText().indexOf("\"")>=0)||(txtREMDS.getText().indexOf("\\")>=0))
				{
					setMSG("Special characters LIKE ' are not allowed in Remarks ",'E');
					return false;
				}
			if((txtCOMNT.getText().indexOf("'")>=0)||(txtCOMNT.getText().indexOf("\"")>=0)||(txtCOMNT.getText().indexOf("\\")>=0))
				{
					setMSG("Special characters LIKE ' are not allowed in Comments.. ",'E');
					return false;
				}
			if(strCLSNM.equals("mm_tegp1"))
			{
				if(txtVEHNO.getText().trim().length() == 0)
				{
					setMSG("Enter Vehicle Number..",'E');
					txtVEHNO.requestFocus();
					return false;
				}
				if(txtVEHDS.getText().trim().length() == 0)
				{
					setMSG("Enter Carieer Name..",'E');
					txtVEHDS.requestFocus();
					return false;
				}
			}
			if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Returnable"))
			{
				String L_strTEMP ="";
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				for(int i = 0;i<intROWCNT;i++)
				{
					if(tblGPDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_strTEMP = tblGPDTL.getValueAt(i,TBL_DUEDT).toString();
						if(L_strTEMP.length() != 10)
						{
							setMSG("Enter Due Date in dd/mm/yyyy format..",'E');
							//txtDUEDT.requestFocus();
							return false;
						}
						else
						{
							if(M_fmtLCDAT.parse(L_strTEMP).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) < 0)
							{
								setMSG("Due Date Should Be Greater Than Todays Date",'E');
								return false;
							}
						}
					}
				}
			}
			if(txtVENCD.getText().trim().length() < 5)
			{
				setMSG("Enter Vendor Code Or Press 'F1' For Help Or Press 'F9' For Vendor Description,..",'E');
				txtVENCD.requestFocus();
				return false;
			}
			if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))	//Rejection
			{
				if(txtGRNNO.getText().trim().length() < 8)
				{
					setMSG("Enter GRIN Number Or Press 'F1' For Help..",'E');
					txtGRNNO.requestFocus();
					return false;
				}
			}
			if(txtREMDS.getText().length() >200)
			{
				setMSG("Remark can not be more than 200 characters ..",'E');
				return false;
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/** Method on clicking on the save button on frame 
	 */
	public void exeSAVE()
	{
		cl_dat.M_flgLCUPD_pbst = true;
		if(vldDATA())
		{
			try
			{
				if(tblGPDTL.isEditing())
					tblGPDTL.getCellEditor().stopCellEditing();
				
				if(txtGRNNO.getText().trim().length() > 0)	
					strGRNNO = txtGRNNO.getText().trim();
				else
					strGRNNO = "";
				String  L_strMGPNO ="";
				//if add recored is selected then gernerate new gate pass number by calling getMGPNO() method
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && !cmbFRWTO.isEnabled())
				{
					L_strMGPNO = getMGPNO();
					if(L_strMGPNO.length() !=8)
					{
						setMSG("Error in generating gate pass number ..",'E');
						return;
					}
					txtMGPNO.setText(L_strMGPNO);	//get Next Gate Pass Number
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && !cmbFRWTO.isEnabled())
				if(L_strMGPNO.length() !=8)
				{
					setMSG("Error in generating gate pass number ..",'E');
					return;
				}
				//If Deletion Option Is Selected
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					strSTSFL = "X";
					int L_intCOUNT = 0;		//Counter to check noumber of loop of deletion used to check with total size of vector vtrGPQTY
					for(int i = 0;i<intROWCNT;i++)
					{
						if(tblGPDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(cl_dat.M_flgLCUPD_pbst)
							{
								M_strSQLQRY = "UPDATE MM_GPTRN SET GP_STSFL = '"+strSTSFL+"',"
									+"GP_TRNFL = '"+strTRNFL+"',GP_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',"
									+"GP_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
									+"WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"
									+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND "
									+"GP_MGPNO = '"+txtMGPNO.getText().trim()+"' AND GP_MATCD = '"
									+tblGPDTL.getValueAt(i,TBL_MATCD)+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								L_intCOUNT++;
							}
						}
					}
				//	System.out.println("count : "+L_intCOUNT+" vtrGPQTY size : "+vtrGPQTY.size());
					if(L_intCOUNT == vtrGPQTY.size())
					{
						if(cl_dat.M_flgLCUPD_pbst)
						{
							if(txtREMDS.getText().trim().length() > 0)
							{
								M_strSQLQRY = "UPDATE MM_RMMST SET RM_STSFL = '"+strSTSFL+"',RM_TRNFL = '"+strTRNFL+"',"
									+"RM_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',RM_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
									+"WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = '"+strTRNTP+"' AND "
									+"RM_DOCTP = '"+strDOCTP+"' AND RM_DOCNO = '"+txtMGPNO.getText().trim()+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
							if(txtCOMNT.getText().trim().length() > 0)
							{
								M_strSQLQRY = "UPDATE MM_RMMST SET RM_REMDS = '"+txtCOMNT.getText().trim() +"',RM_STSFL = '"+strSTSFL+"',RM_TRNFL = '"+strTRNFL+"',"
									+"RM_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',RM_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
									+"WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = '"+strTRNTP+"' AND "
									+"RM_DOCTP = 'RQ' AND RM_DOCNO = '"+txtMGPNO.getText().trim()+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
						}
					}
				}
				//If Addtion Of Record Is Selected
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					//if for new gate pass forward to is available then set status flag to "1" 
					
					if(cmbFRWTO.isEnabled())
					{
						if(cmbFRWTO.getSelectedIndex() ==0)
							return;
						String L_strEML ="";
						strSTSFL = "1";
						strFRWTO = (String)cmbFRWTO.getSelectedItem();
						for(int i=0;i<tblGPDTL.getRowCount();i++)
						{
							if(tblGPDTL.getValueAt(i,0).equals(Boolean.FALSE))
								continue;
							M_strSQLQRY="Update MM_GPTRN set GP_FRWTO='"+cmbFRWTO.getSelectedItem()+"', GP_STSFL='1' where GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND "
										+"GP_MGPNO = '"+txtMGPNO.getText().trim()+"' AND GP_MATCD = '"+tblGPDTL.getValueAt(i,TBL_MATCD)+"' and isnull(GP_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.exeDBCMT("exeSAVE"))
							{
								setMSG("Gate pass Forwarded to "+cmbFRWTO.getSelectedItem()+" ..",'N');
								
							}
							else
							{
								setMSG("Error in forwarding the gatepass ..",'E');
								return;
							}
						}
						M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+cmbFRWTO.getSelectedItem()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						L_strMGPNO = txtMGPNO.getText().trim();
						clrCOMP();
						txtREMDS.setText("");
						cl_eml ocl_eml = new cl_eml();
						if(M_rstRSSET != null)
						if(M_rstRSSET.next())
						{
							L_strEML = M_rstRSSET.getString("US_EMLRF");
							if(L_strEML.length() >0)
								ocl_eml.sendfile(L_strEML,null,"Pending Gate Pass for Approval","Gate Pass No."+L_strMGPNO + " is pending for Approval. ");
						}
						if(M_rstRSSET != null)
							M_rstRSSET.close();
						return;
					}
					else
					{
						strSTSFL = "0";		 
						strFRWTO = "";
					}
					//insert remark in the remark table
					cl_dat.M_flgLCUPD_pbst = true;
					if(txtREMDS.getText().trim().length() > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS," 
							+"RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT) VALUES ('"
							+cl_dat.M_strCMPCD_pbst+"','"+M_strSBSCD.substring(2,4)+"','"+strTRNTP+"','"+strDOCTP+"','"+txtMGPNO.getText().trim()+"','"
							+txtREMDS.getText().trim()+"','','"+strTRNFL+"','"+cl_dat.M_strUSRCD_pbst+"','"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					if(txtCOMNT.getText().trim().length() > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS," 
							+"RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT) VALUES ('"
							+cl_dat.M_strCMPCD_pbst+"','"+M_strSBSCD.substring(2,4)+"','"+strTRNTP+"','RQ','"+txtMGPNO.getText().trim()+"','"
							+txtCOMNT.getText().trim()+"','','"+strTRNFL+"','"+cl_dat.M_strUSRCD_pbst+"','"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					for(int i = 0;i<intROWCNT;i++)
					{
						if(tblGPDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							dblISSQT = Double.parseDouble(nvlSTRVL(tblGPDTL.getValueAt(i,TBL_ISSQT).toString(),"0"));
							if(tblGPDTL.getValueAt(i,TBL_RECQT) == null)
								dblRECQT = 0;
							else
								dblRECQT = Double.parseDouble(nvlSTRVL(tblGPDTL.getValueAt(i,TBL_RECQT).toString(),"0"));
							
							if(cl_dat.M_flgLCUPD_pbst)
							{
								M_strSQLQRY = "INSERT INTO MM_GPTRN(GP_CMPCD,GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD,GP_MGPDT,"
									+"GP_VENCD,GP_VENNM,GP_DUEDT,GP_ISSQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,"
									+"GP_GRNNO,GP_GINNO,GP_TRNFL,GP_STSFL,GP_LUSBY,GP_LUPDT,GP_FRWTO,"
									+"GP_PREBY,GP_INSFL,GP_APRVL,GP_PKGCT)values ('"+cl_dat.M_strCMPCD_pbst+"','"+M_strSBSCD.substring(2,4)+"','"
									+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"','"+txtMGPNO.getText().trim()+"','"+tblGPDTL.getValueAt(i,TBL_MATCD)+"','"
									+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtMGPDT.getText().trim()))+"','"
									+txtVENCD.getText().trim()+"','"+txtVENNM.getText().trim()+"',";
								if(tblGPDTL.getValueAt(i,TBL_DUEDT).toString().length() > 0)
									M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblGPDTL.getValueAt(i,TBL_DUEDT).toString()))+"',";
								else 
									M_strSQLQRY += "null,";
								M_strSQLQRY += +dblISSQT+",'"+txtDPTCD.getText().trim()+"','"+txtVEHNO.getText().trim()+"','"+txtVEHDS.getText().trim()+"','"
									+strGRNNO+"','"+txtGINNO.getText().trim()+"','"+strTRNFL+"','"+strSTSFL+"','"+cl_dat.M_strUSRCD_pbst+"','"
									+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"','"+strFRWTO+"','"+cl_dat.M_strUSRCD_pbst+"',";
								if(chbINSFL.isSelected())
								//if(tblGPDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
									M_strSQLQRY += "'Y'";
								else
									M_strSQLQRY += "'N'";
								M_strSQLQRY += ","+txtAPRVL.getText().trim();
								M_strSQLQRY += ","+txtPKGCT.getText().trim()+")";
								System.out.println(M_strSQLQRY);
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								//if the new gate pass is rejection then update GRMST table
								if((cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))||(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Scrap")))
								{
									if(cl_dat.M_flgLCUPD_pbst)		//Update Gate Pass Quatity In GRIN Table
									{
										//System.out.println("maxqt "+vtrMAXQT.elementAt(tblGPDTL.getSelectedRow()).toString());
										if(txtGRNNO.getText().trim().length() >0)
										{
											M_strSQLQRY = "UPDATE MM_GRMST SET GR_GPQTY = isnull(GR_GPQTY,0)+"+dblISSQT;
											if(Float.parseFloat(tblGPDTL.getValueAt(tblGPDTL.getSelectedRow(),TBL_ISSQT).toString()) == Float.parseFloat(vtrMAXQT.elementAt(tblGPDTL.getSelectedRow()).toString()))
												M_strSQLQRY += ",GR_GPTAG = 'C'";
											M_strSQLQRY +=",GR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', GR_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
												+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GR_GRNNO = '"+strGRNNO+"' AND "
												+"GR_MATCD = '"+tblGPDTL.getValueAt(i,TBL_MATCD)+"'";
											System.out.println(M_strSQLQRY);
											cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
										}
									}
								}
							}
							// iF NON RETURNABLE AND GATE IN REF IS GIVEN, update mm_wbtrn
							if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Non-Returnable"))
							{
								// For Access only to stores, not to other users
								if((txtGINNO.getText().trim().length() >0)&&(strCLSNM.equals("mm_tegp1")))
								{
									M_strSQLQRY = "UPDATE MM_WBTRN SET WB_DOCRF ='"+ txtMGPNO.getText().trim()+"'"+
												  " WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP ='02' AND WB_DOCNO ='"+txtGINNO.getText().trim()+"'";  
									if(cl_dat.M_flgLCUPD_pbst)
										cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
									txtGINNO.setEnabled(false);
								}
							}
						}
					}
					if(cl_dat.M_flgLCUPD_pbst)
						updMGPNO();
					cmbFRWTO.setEnabled(true);
					setMSG("You can forward the Gate Pass now ..",'N');
					cmbFRWTO.requestFocus();
				}
				/**If Modification Of Record Is Selected
				 */
				String L_strEML ="";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(flgMODIF)	//If Modification Flag Is True that was set in getRECRD()
					{
						M_strSQLQRY = "UPDATE MM_RMMST SET RM_REMDS = '"+txtREMDS.getText().trim()+"',"
							+"RM_TRNFL = '"+strTRNFL+"',RM_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',"
							+"RM_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
							+"WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = '"+strTRNTP+"' "
							+"AND RM_DOCTP = '"+strDOCTP+"' AND RM_DOCNO = '"+txtMGPNO.getText().trim()+"'"
							+" AND isnull(rm_remtp,'1') ='1'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else if(txtREMDS.getText().trim().length() > 0)
					{
						M_strSQLQRY = "Insert into MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS," 
							+"RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT) values ('"
							+cl_dat.M_strCMPCD_pbst+"','"+M_strSBSCD.substring(2,4)+"','"+strTRNTP+"','"+strDOCTP+"','"+txtMGPNO.getText().trim()+"','"
							+txtREMDS.getText().trim()+"','','"+strTRNFL+"','"+cl_dat.M_strUSRCD_pbst+"','"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					if(flgCMMOD)	//If Modification Flag Is True that was set in getRECRD()
					{
						M_strSQLQRY = "UPDATE MM_RMMST SET RM_REMDS = '"+txtCOMNT.getText().trim()+"',"
							+"RM_TRNFL = '"+strTRNFL+"',RM_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',"
							+"RM_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
							+"WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND RM_TRNTP = '"+strTRNTP+"' "
							+"AND RM_DOCTP = 'RQ' AND RM_DOCNO = '"+txtMGPNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else if(txtCOMNT.getText().trim().length() > 0)
					{
						M_strSQLQRY = "Insert into MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS," 
							+"RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT) values ('"
							+cl_dat.M_strCMPCD_pbst+"','"+M_strSBSCD.substring(2,4)+"','"+strTRNTP+"','RQ','"+txtMGPNO.getText().trim()+"','"
							+txtCOMNT.getText().trim()+"','','"+strTRNFL+"','"+cl_dat.M_strUSRCD_pbst+"','"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}

					//check the status flag set in gerGPDTL() method
					if(strSTSFL.equals("0"))
					{
						if(cmbFRWTO.getSelectedIndex() > 0)
							strSTSFL = "1";
					}
					if(cmbFRWTO.getSelectedIndex() > 0)
						strFRWTO = (String)cmbFRWTO.getSelectedItem();
					else
						strFRWTO = "";
					for(int i = 0;i<intROWCNT;i++)
					{
						if(tblGPDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							dblISSQT = Double.parseDouble(nvlSTRVL(tblGPDTL.getValueAt(i,TBL_ISSQT).toString(),"0"));
							
							if(tblGPDTL.getValueAt(i,TBL_RECQT) == null)
								dblRECQT = 0;
							else
								dblRECQT = Double.parseDouble(nvlSTRVL(tblGPDTL.getValueAt(i,TBL_RECQT).toString(),"0"));
							if(cl_dat.M_flgLCUPD_pbst)
							{
								//Check whether the record is previously present if yes the update
								//by checking vector size with current updation vector value is added in display recored for modificaion 
								if(vtrGPQTY.size() > i)
								{
									M_strSQLQRY ="Update MM_GPTRN set GP_MGPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtMGPDT.getText()))+"',"
										+"GP_VENCD = '"+txtVENCD.getText().trim()+"',GP_VENNM = '"+txtVENNM.getText().trim()+"',";
									if(tblGPDTL.getValueAt(i,TBL_DUEDT).toString().length() > 0)
										M_strSQLQRY += "GP_DUEDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblGPDTL.getValueAt(i,TBL_DUEDT).toString()))+"',";
									M_strSQLQRY += "GP_ISSQT = isnull(GP_ISSQT,0) - "+vtrGPQTY.elementAt(i).toString()+"+"+setNumberFormat(dblISSQT,3)+",GP_RECQT = "+dblRECQT+",GP_DPTCD = '"+txtDPTCD.getText().trim()+"',"
										+"GP_VEHNO = '"+txtVEHNO.getText().trim()+"',GP_VEHDS = '"+txtVEHDS.getText().trim()+"',GP_GRNNO = '"+strGRNNO+"',"
										+"GP_GINNO = '"+txtGINNO.getText().trim()+"',GP_TRNFL = '"+strTRNFL+"',GP_STSFL = '"+strSTSFL+"',"
										+"GP_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',GP_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
									if(cmbFRWTO.getSelectedIndex() >0)		  
										M_strSQLQRY +=	"GP_FRWTO = '"+cmbFRWTO.getSelectedItem()+"',";
									if(chbINSFL.isSelected())
									//if(tblGPDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
										M_strSQLQRY += "GP_INSFL = 'Y' ";
									else
										M_strSQLQRY += "GP_INSFL = 'N' ";
									if(txtAPRVL.getText().trim().length() >0)
										M_strSQLQRY += ",GP_APRVL = "+txtAPRVL.getText().trim();
									if(txtPKGCT.getText().trim().length() >0)
										M_strSQLQRY += ",GP_PKGCT = "+txtPKGCT.getText().trim();
									M_strSQLQRY += " WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND "
										+"GP_MGPNO = '"+txtMGPNO.getText().trim()+"' AND GP_MATCD = '"+tblGPDTL.getValueAt(i,TBL_MATCD)+"' and isnull(GP_STSFL,'') <>'X'";
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								}
								else	//if current record no is grater than vector size then insert record 
								{
									if((strCLSNM.equals("mm_tegp1"))&& strSTSFL.equals("3"))
									{
										// Adding of new item by stores in an approved gate pass
										// is not allowed.
										continue;
									}
									M_strSQLQRY = "INSERT INTO MM_GPTRN(GP_CMPCD,GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD,GP_MGPDT,"
										+"GP_VENCD,GP_VENNM,GP_DUEDT,GP_ISSQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,"
										+"GP_GRNNO,GP_GINNO,GP_TRNFL,GP_STSFL,GP_LUSBY,GP_LUPDT,GP_FRWTO,GP_INSFL,GP_APRVL,GP_PKGCT)"
										+"values ('"+cl_dat.M_strCMPCD_pbst+"','"+M_strSBSCD.substring(2,4)+"','"+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"','"+txtMGPNO.getText().trim()+"','"+tblGPDTL.getValueAt(i,TBL_MATCD)+"','"
										+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtMGPDT.getText().trim()))+"','"
										+txtVENCD.getText().trim()+"','"+txtVENNM.getText().trim()+"',";
									if(tblGPDTL.getValueAt(i,TBL_DUEDT).toString().length() > 0)
										M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblGPDTL.getValueAt(i,TBL_DUEDT).toString()))+"',";
									else 
										M_strSQLQRY += "null,";
									M_strSQLQRY += +dblISSQT+",'"+txtDPTCD.getText().trim()+"','"+txtVEHNO.getText().trim()+"','"+txtVEHDS.getText().trim()+"','"
										+strGRNNO+"','"+txtGINNO.getText().trim()+"','"+strTRNFL+"','"+strSTSFL+"','"+cl_dat.M_strUSRCD_pbst+"','"
										+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"','"+strFRWTO+"',";
									if(chbINSFL.isSelected())
									//if(tblGPDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
										M_strSQLQRY += "'Y',";
									else
										M_strSQLQRY += "'N',";
									M_strSQLQRY += txtAPRVL.getText().trim()+",";
									M_strSQLQRY += txtPKGCT.getText().trim()+")";
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								}
								if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))
								{
									if(cl_dat.M_flgLCUPD_pbst)		//Update Gate Pass Quatity In GRIN Table
									{
										M_strSQLQRY = "UPDATE MM_GRMST SET GR_GPQTY = isnull(GR_GPQTY,0) - "+vtrGPQTY.elementAt(i)+"+"+setNumberFormat(dblISSQT,3);
										//Check If ISSQT Is equals To Rejected Quantity Stored in vtrMAXQT
										if(Float.parseFloat(tblGPDTL.getValueAt(tblGPDTL.getSelectedRow(),TBL_ISSQT).toString()) == Float.parseFloat(vtrMAXQT.elementAt(tblGPDTL.getSelectedRow()).toString()))
											M_strSQLQRY += ",GR_GPTAG = 'C'";
										M_strSQLQRY += ",GR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',GR_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
											+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GR_GRNNO = '"+strGRNNO+"' AND "
											+"GR_MATCD = '"+tblGPDTL.getValueAt(i,TBL_MATCD)+"'";
										cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
									}
								}
							}
						}
					}
					if((strSTSFL.equals("2"))&& (strFRWTO.length() == 3))
					{
						M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+strFRWTO+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						cl_eml ocl_eml = new cl_eml();
						if(M_rstRSSET != null)
						if(M_rstRSSET.next())
						{
							L_strEML = M_rstRSSET.getString("US_EMLRF");
							if(L_strEML.length() >0)
								ocl_eml.sendfile(L_strEML,null,"Pending Gate Pass for Approval","Gate Pass No."+txtMGPNO.getText().trim() + " is pending for Approval. ");
						}
						if(M_rstRSSET != null)
							M_rstRSSET.close();
					}
				}
			
				//if authorisation of gate pass is selected
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					if(strCLSNM.equals("mm_tegpt"))
					{
						strSTSFL = "3";
						M_strSQLQRY ="UPDATE MM_GPTRN SET GP_STSFL = '"+strSTSFL+"',GP_LUSBY = '"
						+cl_dat.M_strUSRCD_pbst+"',GP_LUPDT = '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
						+"GP_APRBY = '"+cl_dat.M_strUSRCD_pbst+"',GP_APRDT = current_timestamp "
						+"WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"
						+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND GP_MGPNO = '"
						+txtMGPNO.getText().trim()+"' AND isnull(GP_STSFL,'') <>'X'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						// Generate email to user saying GP no. is approved.
						// keep two variables preby and aprby, frwto 
						M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+strPREBY+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						cl_eml ocl_eml = new cl_eml();
						if(M_rstRSSET != null)
						if(M_rstRSSET.next())
						{
							L_strEML = M_rstRSSET.getString("US_EMLRF");
							if(L_strEML.length() >0)
								ocl_eml.sendfile(L_strEML,null,"Intimation of Gate Pass Approval","Gate Pass No."+txtMGPNO.getText().trim() + " is Approved by "+cl_dat.M_strUSRCD_pbst);
						}
						if(M_rstRSSET != null)
						M_rstRSSET.close();
					}
					else
					{
						strSTSFL = "4";
						M_strSQLQRY ="UPDATE MM_GPTRN SET GP_STSFL = '"+strSTSFL+"',GP_LUSBY = '"
						+cl_dat.M_strUSRCD_pbst+"',GP_LUPDT = '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
						+"GP_AUTBY = '"+cl_dat.M_strUSRCD_pbst+"',GP_AUTDT = current_timestamp "
						+"WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"
						+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"' AND GP_MGPNO = '"
						+txtMGPNO.getText().trim()+"' AND isnull(GP_STSFL,'') <>'X'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Returnable"))
						{
							M_strSQLQRY = "update mm_gptrn set gp_duedt = date(days(gp_duedt)+days(CURRENT_DATE)-days(GP_MGPDT)) "
									+"WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '51'"
									+" AND GP_MGPNO = '"
									+txtMGPNO.getText().trim()+"' AND isnull(GP_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
						// Generate email to user saying GP no. is authorised.
						M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD IN('"+strPREBY+"','"+strAPRBY+"')";
						
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						cl_eml ocl_eml = new cl_eml();
						if(M_rstRSSET != null)
						while(M_rstRSSET.next())
						{
							L_strEML = M_rstRSSET.getString("US_EMLRF");
							if(L_strEML.length() >0)
								ocl_eml.sendfile(L_strEML,null,"Intimation of Gate Pass Authorisation","Gate Pass No."+ txtMGPNO.getText().trim() + " is Authorised for Gate out. ");
						}
						if(M_rstRSSET != null)
						M_rstRSSET.close();
					}
				
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"exeSAVE");
			}
			//check if all recored is added successfully the save it by commit if addition the dispaly 
			//new gate pass number in option pane 
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Data Saved Successfully..",'N');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					JOptionPane.showMessageDialog(this,"Please Note Gate Pass Number : "+"\n"+M_strSBSCD.substring(2,4)+"/"+txtMGPNO.getText());
				strMGPNO = txtMGPNO.getText().trim();
				strMGPTP = hstMGPTP.get(cmbMGPTP.getSelectedItem()).toString();
				strMGPDS = cmbMGPTP.getSelectedItem().toString();
				strSTRTP = cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString();
				btnPRNT.setEnabled(true);
	//			clrCOMP();  // Why clrCOMP was commented , check if prob.
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					clrCOMP();
					txtREMDS.setText("");
				}
				cmbFRWTO.requestFocus();
				vtrGPQTY.clear();
				vtrMAXQT.clear();
				if(tblGPDTL.isEditing())
					tblGPDTL.getCellEditor().stopCellEditing();
			}
			else
			{
				setMSG("Error in saving the data..",'E');
			}
		}
	}
	/** Method to generate material gate pass number 
	 */
	private String getMGPNO()
	{
		String L_MGPNO  = "",  L_CODCD = "", L_CCSVL = "",L_strLKSTS ="";
		try
		{
			L_strLKSTS = cl_dat.getPRMCOD("CMT_CHP01","D"+cl_dat.M_strCMPCD_pbst,"MMXXMGP",cl_dat.M_strFNNYR_pbst.substring(3,4)+(String)hstMGPTP.get(cmbMGPTP.getSelectedItem()));
			if(L_strLKSTS.length() >0)
			{
				setMSG("In Use, Please try after some time..",'E');
				return "";
			}
			else
			{
				M_strSQLQRY = "UPDATE CO_CDTRN SET CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"' "
				+"WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXMGP' AND "
				+"SUBSTRING(CMT_CODCD,2,3) = '"+(String)hstMGPTP.get(cmbMGPTP.getSelectedItem())+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(!cl_dat.M_flgLCUPD_pbst)
				{
					setMSG("Error in Locking the record ..",'E');
					return "";
				}
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CCSVL FROM CO_CDTRN "
				+"WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXMGP' AND "
				+"SUBSTRING(CMT_CODCD,2,3) = '"+(String)hstMGPTP.get(cmbMGPTP.getSelectedItem())+"'";
			
			M_rstRSSET =cl_dat.exeSQLQRY(M_strSQLQRY);
			//cl_dat.M_strFNNYR_pbst.substring(3,4); -- 5
			/**get the curretnt ccsvl value 		 */
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"").substring(1,3);	//Get Gate Pass Type last Two Charactor
					L_CODCD = cl_dat.M_strFNNYR_pbst.substring(3,4)+L_CODCD;	//append Fin Year 
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");	//Get Gate Pass Counter
					M_rstRSSET.close();
				}
			}
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);	//Increment Counter By 1
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_MGPNO += "0";
			
            L_CCSVL = L_MGPNO + L_CCSVL;
            L_MGPNO = L_CODCD + L_CCSVL;
			
			M_strSQLQRY = "UPDATE CO_CDTRN SET CMT_CHP01 =''"
				+"WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXMGP' AND "
				+"SUBSTRING(CMT_CODCD,2,3) = '"+(String)hstMGPTP.get(cmbMGPTP.getSelectedItem())+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(!cl_dat.M_flgLCUPD_pbst)
				{
					setMSG("Error in Releasing the Lock ..",'E');
					return "";
				}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genMGPNO");
		}
		return L_MGPNO;
	}
	/** Updater teh ccsvl value for the gate pas for next gate pass
	 */
	private void updMGPNO()
	{
		try
		{
			M_strSQLQRY = "UPDATE CO_CDTRN SET CMT_CCSVL = '"+txtMGPNO.getText().toString().substring(3)+"', "
				+"CMT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',CMT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
				+" WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXMGP' "
				+"AND CMT_CODCD = '"+cl_dat.M_strFNNYR_pbst.substring(3,4)+hstMGPTP.get(cmbMGPTP.getSelectedItem())+"'";                
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeMEMNO");
		}
	}
	//mm_rpgpp objGPPRP = new mm_rpgpp(M_strSBSCD.substring(0,2)+hstCODCD.get(tblGPAPR.getValueAt(i,TBL_STRTP).toString()).toString()+"00");
	//objGPPRP.genRPFIL(L_strGPPNO,L_strGPPNO,hstCODCD.get(tblGPAPR.getValueAt(i,TBL_DOCTP).toString()).toString(),tblGPAPR.getValueAt(i,TBL_DOCTP).toString(),tblGPAPR.getValueAt(i,TBL_STRTP).toString());
}