/*
	Name			: Grade Wise Customer Order Outstanding report
	System			: MKT
	Author			: AAP
	Version			: v2.0.0
	Last Modified	: 29/07/2004 //For FIMS processing date from CO_CDTRN
	Documented On	: 20/05/2004
*/
import java.io.File;import java.io.FileOutputStream;import java.io.DataOutputStream;
//import javax.swing.JPanel;
import javax.swing.*;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Vector;import java.util.Hashtable;import java.sql.ResultSet;
import java.awt.Color;import java.util.Calendar;

/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>  Grade&nbsp;Wise Customer Order Outstanding Summary Report.</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                 Gives&nbsp;summary report of Grade and Package type                  wise Booking(Region wise), stock,       target dispatch, Sales return and unclassified stock. </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Existing report      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mr_rpcoo.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_rpcoo.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>13/03/2004&nbsp; </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;&nbsp;        </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue borderColor=black>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>MR_INMST</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_INTRN</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_IVTRN</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_PTMST</TD>    <TD>&nbsp; </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P><P><FONT color=purple><STRONG>Features :</STRONG></FONT></P><UL>  <LI>Region wise outstanding totals up to the to date and day opening stock figures for the calendar date </LI></UL>*/
class mr_rpcoo extends cl_rbase
{	/**Contains booking details in format : KEY : PR_PRDDS|PKGWT VALUE : Vector<String> of REGION CODE|BOOKING QTY	 */
	private Hashtable<String,String> hstQLHQT;/**Contains Qualit Hold stock qty. : KEY : PR_PRDDS|PKGWT VALUE : ST_DOSQT;	 	 	 */
	private Hashtable hstBKGDT;/**Contains Stock details in format : KEY : PR_PRDDS|PKGWT VALUE : Vector<String> of OP_DOSQT|OP_TDSQT|OP_DXRQT|OP_DOUQT;	 	 	 */
	private Hashtable hstSTKDT;/**Contains Region code details in format : KEY : Region Code VALUE : Short Description from CO_CDTRN	 */
	private Hashtable<String,String> hstRGNCD;/**Contains Product Grade details in format : PR_PRDDS|PKGWT*/
	private Vector<String> vtrPRDDS;/**Contains product codes in serial order*/
	private Vector<String> vtrPRDCD;/**Contains Product Group Code*/
	private Vector<String> vtrPGRCD;/**Contains Prduct Group Description from CO_CDTRN, COXXPGR*/
	private Vector<String> vtrPGRDS;/**Column index in query from CO_CDTRN, COXXPGR*/
	private final int	intPRDDS_fn=1,
						intPKGWT_fn=2,
						intINDQT_fn=3,
						intRGNCD_fn=4;/**To write report to file "mr_rpcoo.doc"	 */
	private DataOutputStream dosREPORT;/**Table for report data*/
	//private final int[] inaCOLWD_fn=new int[]{15,15,15,15,15,15,15,15,15,15};
	private cl_JTable tblREPORT;/**Flag to indicate report date has been changed and requires firing query*/
	//private boolean flgOLDRP=false;/**Panel for report*/
	private JPanel pnlREPORT;/**Thread to collect details of grades and package types.*/
	private Thread thrPRDDS;
	private String strPRCDT;
	private JLabel lblSTKMSG;
	private JCheckBox chkXPS;	
	
	private int intTB1_CHKFL = 0;
	private int intTB1_PRDCT = 1;
	private int intTB1_PRDDS = 2;
	private int intTB1_PKGWT = 3;
	private int intTB1_WC_RG = 4;
	private int intTB1_NESRG = 5;
	private int intTB1_EXPRG = 6;
	private int intTB1_TOTBK = 7;
	private int intTB1_STKQT = 8;
	private int intTB1_QLHQT = 9;
	private int intTB1_TDSQT = 10;
	private int intTB1_SLRQT = 11;
	private int intTB1_UCLQT = 12;
	
	//private JCheckBox chkSELFML;
	/**
	 * Constructs the form
	 * 
	 * Constructs the form
	 * 
	 * <p>Retrieves region codes and puts in hstRGNCD<br>Retrieves details of product catagories and puts in vtrPGRCD & vtrPGRDS<br>Retrieves details of Package Types and puts in vtrPKGTP<br> Retrieves details of grades available and puts all combinations with PKGTP in vtrPRDDS & vtrPRDCD
	 */
	mr_rpcoo()
	{
		super(2);
		try
		{
//			thrPRDDS=new Thread(this);
//			thrPRDDS.start();
//RETRIEVING REGION CODE DETAILS
			lblSTKMSG = new JLabel();
			//chkSELFML = new JCheckBox("Self Mail");
			//add(chkSELFML,2,6,1,1,this,'L');
			add(lblSTKMSG,3,6,1,3,this,'L');
			chkXPS = new JCheckBox("XPS Report");
			add(chkXPS,3,2,1,2,this,'L');
			M_rstRSSET=cl_dat.exeSQLQRY("Select * from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN'");
			hstRGNCD=new Hashtable<String,String>(5,0.2f);
			while(M_rstRSSET.next())
				hstRGNCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_SHRDS"));
			M_rstRSSET.close();
			M_txtFMDAT.setVisible(false);M_lblFMDAT.setVisible(false);
			M_vtrSCCOMP.remove(M_txtFMDAT);M_vtrSCCOMP.remove(M_lblFMDAT);
	//RETRIEVING DETAILS OF PRODUCT CODE GROUPING AND PUTTING IN RESP Vector<String>S
			M_rstRSSET=cl_dat.exeSQLQRY0("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' order by CMT_CODCD");
			if(M_rstRSSET!=null)
			{
				vtrPGRDS=new Vector<String>(10,2);vtrPGRCD=new Vector<String>(10,2);
				while(M_rstRSSET.next())
				{
					vtrPGRDS.addElement(M_rstRSSET.getString("CMT_CODDS"));
					vtrPGRCD.addElement(M_rstRSSET.getString("CMT_CODCD"));
				}
				M_rstRSSET.close();
			}
	//RETRIEVING PACKAGE WT DETAILS
				M_rstRSSET=cl_dat.exeSQLQRY("Select distinct CMT_NCSVL from CO_CDTRN where CMT_CGSTP='FGXXPKG'");
				Vector<String> vtrPKGWT=new Vector<String>(10,5);
				while(M_rstRSSET.next())
					vtrPKGWT.addElement(M_rstRSSET.getString(1));
				M_rstRSSET.close();
	//RETRIEVING LIST OF AVAILABLE PRODUCT GRADES			
				M_rstRSSET=cl_dat.exeSQLQRY("Select PR_PRDDS,PR_PRDCD from CO_PRMST order by PR_PRDCD");
				vtrPRDDS=new Vector<String>(50,10);
				vtrPRDCD=new Vector<String>(50,10);
				while(M_rstRSSET.next())
					for(int i=0;i<vtrPKGWT.size();i++)
					{
						vtrPRDDS.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+vtrPKGWT.elementAt(i).toString());
						vtrPRDCD.addElement(M_rstRSSET.getString("PR_PRDCD"));
					}
					M_rstRSSET.close();
		//RETRIEVING PROCESSING DATE FROM FIMS
				M_strSQLQRY="Select * from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXREF' and CMT_CODCD='DOCDT'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(M_rstRSSET.getString("CMT_CCSVL")));
					M_calLOCAL.add(Calendar.DATE,1);
					strPRCDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
					M_rstRSSET.close();
					lblSTKMSG.setText("Stock as on : "+strPRCDT+" 7:00 hrs");
				}
			//chkSELFML.setSelected(false);
			M_pnlRPFMT.setVisible(true);

		}catch(Exception e)
		{setMSG(e,"Child.Constructor");}
	}
	
/*	public void run()
	{
		try
		{
			if(Thread.currentThread()==thrPRDDS)
			{
			}
			else
				super.run();
		}catch(Exception e){setMSG(e,"child.run");}
	}
*/	
	/**
	 * To Prepare the report
	 * <p>Validates user inputs,Fires query to DB and formats the data as per user selection
	 */
	@SuppressWarnings("unchecked") void exePRINT()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			M_intLINNO=0;
			M_intPAGNO=0;
			if(M_txtTODAT.getText().length()==0)
			{
				setMSG("Please Enter As On Date ..",'E');
				M_txtTODAT.requestFocus();
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst))
			{
				if(M_rdbTEXT.isSelected())
				if(M_cmbDESTN.getSelectedIndex()==0)
				{
					setMSG("Please Select Printer ..",'E');
					M_cmbDESTN.requestFocus();
					return;
				}
			}
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("",'N');
			//System.out.println("Date Diff : "+M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(strPRCDT)));
			if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(strPRCDT))!= 0)
			{
				JOptionPane.showMessageDialog(this,"Stock Closing Date : "+strPRCDT+"       Report Date : "+M_txtTODAT.getText()+"    please note ...");
			}
				
				
//COLLECTING BOOKING DETAILS 			
	/*			M_strSQLQRY="select INT_PRDDS,INT_PKGWT,INT_INDQT-isnull(SUM(isnull(IVT_INVQT,0)),0)"
					+" INT_INDQT,b.cmt_chp02"
					+"  from MR_INMST,MR_INTRN,CO_CDTRN b"
					+"  left outer join MR_IVTRN "
					+" on INT_INDNO=IVT_INDNO AND INT_PRDCD=IVT_PRDCD "
	//				+" left OUTER JOIN MR_DOTRN ON"
	//				+" DOT_INDNO=INT_INDNO AND DOT_PRDCD =INT_PRDCD and DOT_STSFL<>'X' "
	//				+" left outer join mr_dodel on dot_dorno=dod_dorno AND DOT_PRDCD=DOD_PRDCD "
					+" where in_mkttp='01' AND IN_MKTTP=INT_MKTTP and IN_INDNO=INT_INDNO and "
					+" IN_INDDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"'"
					+" and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON'"
					+" and in_stsfl<>'X' and int_stsfl<>'X'"
					+" and INT_INDQT>0"
					+" group by INT_PRDDS,INT_PKGWT,INT_INDQT,b.cmt_chp02 "
					+" having  INT_INDQT-isnull(SUM(isnull(IVT_INVQT,0)),0)>0";
				//System.out.println(M_strSQLQRY);
*/
//				M_strSQLQRY="select INT_PRDDS,INT_PKGWT,sum((INT_INDQT-isnull(INT_INVQT,0))) INT_INDQT,b.cmt_chp02  from MR_INMST,MR_INTRN,CO_CDTRN b   where  IN_MKTTP=INT_MKTTP and IN_INDNO=INT_INDNO and  IN_BKGDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON' and in_stsfl<>'X' and int_stsfl<>'X' and INT_INDQT-isnull(INT_INVQT,0)>0 and length(int_prdcd)=10 and in_mkttp='01' group by int_prdds, int_pkgwt,b.cmt_chp02";
				
				M_strSQLQRY="select INT_PRDDS,INT_PKGWT, (isnull(INT_INDQT,0)-isnull(INT_FCMQT,0))-isnull(SUM(isnull(IVT_INVQT,0)),0) INT_INDQT,"
					+" b.cmt_chp02,b.cmt_codds,IN_DSRCD,IN_MKTTP,IN_INDNO,a.CMT_CODDS,IN_BYRCD,IN_DTPCD "
					+" from CO_CDTRN a,CO_CDTRN b,VW_INTRN "
					+" left outer join MR_IVTRN  on INT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and  ivt_mkttp in "+(chkXPS.isSelected() ? "('07')" : "('01','04','05')")+" and INT_CMPCD = IVT_CMPCD and INT_MKTTP=IVT_MKTTP and INT_INDNO=IVT_INDNO AND INT_PRDCD=IVT_PRDCD and INT_PKGTP = IVT_PKGTP "
					+" and IVT_STSFL<>'X' and CONVERT(varchar,ivt_invdt,101)<= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' where INT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and  "
					+"  int_mkttp in "+(chkXPS.isSelected() ? "('07')" : "('01','04','05')")+" and  IN_BKGDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' "
					+" and IN_SALTP=a.CMT_CODCD and a.CMT_CGSTP='MR00SAL' and IN_ZONCD=b.CMT_CODCD "
					+" and b.CMT_CGSTP='MR00ZON' and in_stsfl<>'X' and int_stsfl<>'X' and (isnull(INT_INDQT,0)-isnull(INT_FCMQT,0))>0  and INT_SBSCD1 in "+M_strSBSLS
					+" group by IN_MKTTP ,IN_INDNO,a.CMT_CODDS,IN_BYRCD,IN_DSRCD,INT_PKGWT,INT_PRDDS,INT_INDQT,INT_FCMQT,b.cmt_codds,b.cmt_chp02,IN_DTPCD"
					+" having  (isnull(INT_INDQT,0)-isnull(INT_FCMQT,0))-isnull(SUM(isnull(IVT_INVQT,0)),0)>0 order by int_prdds";
//				M_strSQLQRY="select INT_PRDDS,INT_PKGWT,INT_INDQT-sum(isnull(IVT_INVQT,0)) INVQT,b.cmt_chp02  from MR_INMST,MR_INTRN,CO_CDTRN b left outer join  mr_ivtrn on int_indno=ivt_indno and date(ivt_invdt)< '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' where  IN_MKTTP=INT_MKTTP and IN_INDNO=INT_INDNO and  IN_BKGDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON' and in_stsfl<>'X' and int_stsfl<>'X' and INT_INDQT-isnull(INT_INVQT,0)>0 and length(int_prdcd)=10 and in_mkttp='01' group by int_prdds, int_pkgwt,b.cmt_chp02,INT_INDQT";				
				//M_strSQLQRY="select INT_PRDDS,int_pkgwt,sum(INT_INDQT) INDQT,sum(ivt_invqt) INVQT,cmt_chp02 from mr_intrn,mr_inmst,co_cdtrn left outer join mr_ivtrn on int_indno=ivt_indno and int_prdds=ivt_prdds where int_indqt>int_invqt and in_indno=int_indno and in_inddt<='03/22/2204' and in_mkttp='01' and cmt_cgstp='MR00ZON'and cmt_codcd=in_zoncd and int_stsfl<>'X' and in_stsfl<>'X' group by int_prdds,int_pkgwt,cmt_chp02 order by cmt_chp02";
				//System.out.println(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				hstBKGDT=new Hashtable(30,0.3f);
				hstQLHQT=new Hashtable<String,String>(30,0.3f);
				crtHSTQLHQT();
				String  L_strTEMP="";
				Vector<String> L_vtrTEMP=null;
				while(M_rstRSSET.next())
				{
					L_strTEMP="";
					L_strTEMP+=nvlSTRVL(M_rstRSSET.getString(intRGNCD_fn)," ");
					L_strTEMP+="|"+nvlSTRVL(M_rstRSSET.getString(intINDQT_fn)," ");
					if(!hstBKGDT.containsKey(M_rstRSSET.getString(intPRDDS_fn)+"|"+M_rstRSSET.getString(intPKGWT_fn)))
						L_vtrTEMP=new Vector(1,1);
					else
						L_vtrTEMP=(Vector)hstBKGDT.get(M_rstRSSET.getString(intPRDDS_fn)+"|"+M_rstRSSET.getString(intPKGWT_fn));
					L_vtrTEMP.addElement(L_strTEMP);
					hstBKGDT.put(M_rstRSSET.getString(intPRDDS_fn)+"|"+M_rstRSSET.getString(intPKGWT_fn),L_vtrTEMP);
				}
				M_rstRSSET.close();
				
	//COLLECTING STOCK DETAILS
//				if(M_txtTODAT.getText().equals(cl_dat.M_txtCLKDT_pbst.getText()))
			   {
					M_strSQLQRY="Select * from FG_OPSTK,CO_CDTRN,CO_PRMST where OP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"'  and SUBSTRING(OP_PRDCD,1,2)  "+(chkXPS.isSelected() ? " = 'SX'" : " <> 'SX' ")+" and (OP_SLRQT>0 or OP_TDSQT>0 or OP_STKQT>0 or OP_UCLQT>0) and CMT_CGSTP='FGXXPKG' and cmt_codcd=op_pkgtp and PR_PRDCD=OP_PRDCD";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						hstSTKDT=new Hashtable(30,0.3f);
						String L_strQLHQT = "";
						double L_dblDOSQT,L_dblQLHQT,L_dblTDSQT,L_dblSLRQT,L_dblDOUQT;
						StringTokenizer L_stkTEMP=null;
						L_strTEMP="";
						while(M_rstRSSET.next())
						{
							L_strQLHQT = "0.000";
							if(hstQLHQT.containsKey(M_rstRSSET.getString("PR_PRDDS")+"|"+M_rstRSSET.getString("CMT_NCSVL")))
							   L_strQLHQT = setNumberFormat(Double.parseDouble(hstQLHQT.get(M_rstRSSET.getString("PR_PRDDS")+"|"+M_rstRSSET.getString("CMT_NCSVL")).toString()),3);
							L_dblDOSQT=0.000;L_dblQLHQT=0.000;L_dblTDSQT=0.000;L_dblSLRQT=0.000;L_dblDOUQT=0.000;
							L_dblDOSQT = M_rstRSSET.getDouble("OP_DOSQT");
							L_dblQLHQT = Double.parseDouble(L_strQLHQT);
							L_dblTDSQT = M_rstRSSET.getDouble("OP_TDSQT");
							L_dblSLRQT = M_rstRSSET.getDouble("OP_SLRQT");
							L_dblDOUQT = M_rstRSSET.getDouble("OP_DOUQT");
							if(hstSTKDT.containsKey(M_rstRSSET.getString("PR_PRDDS")+"|"+M_rstRSSET.getString("CMT_NCSVL")))
							{
								L_stkTEMP=new StringTokenizer((String)hstSTKDT.get(M_rstRSSET.getString("PR_PRDDS")+"|"+M_rstRSSET.getString("CMT_NCSVL")),"|");
								if(L_stkTEMP.hasMoreTokens())
								{
									L_strTEMP=L_stkTEMP.nextToken();
									if(Double.parseDouble(L_strTEMP)>0)
										L_dblDOSQT +=  Double.parseDouble(L_strTEMP);
								}
								if(L_stkTEMP.hasMoreTokens())
								{
									L_strTEMP=L_stkTEMP.nextToken();
									if(Double.parseDouble(L_strTEMP)>0)
										L_dblQLHQT +=  Double.parseDouble(L_strTEMP);
								}
								if(L_stkTEMP.hasMoreTokens())
								{
									L_strTEMP=L_stkTEMP.nextToken();
									if(Double.parseDouble(L_strTEMP)>0)
										L_dblTDSQT +=  Double.parseDouble(L_strTEMP);
								}
								if(L_stkTEMP.hasMoreTokens())
								{
									L_strTEMP=L_stkTEMP.nextToken();
									if(Double.parseDouble(L_strTEMP)>0)
										L_dblSLRQT +=  Double.parseDouble(L_strTEMP);
								}
								if(L_stkTEMP.hasMoreTokens())
								{
									L_strTEMP=L_stkTEMP.nextToken();
									if(Double.parseDouble(L_strTEMP)>0)
										L_dblDOUQT +=  Double.parseDouble(L_strTEMP);
								}
							}
							//hstSTKDT.put(M_rstRSSET.getString("PR_PRDDS")+"|"+M_rstRSSET.getString("CMT_NCSVL"),M_rstRSSET.getString("OP_DOSQT")+"|"+L_strQLHQT+"|"+M_rstRSSET.getString("OP_TDSQT")+"|"+M_rstRSSET.getString("OP_SLRQT")+"|"+M_rstRSSET.getString("OP_DOUQT"));
							hstSTKDT.put(M_rstRSSET.getString("PR_PRDDS")+"|"+M_rstRSSET.getString("CMT_NCSVL"),setNumberFormat(L_dblDOSQT,3)+"|"+setNumberFormat(L_dblQLHQT,3)+"|"+setNumberFormat(L_dblTDSQT,3)+"|"+setNumberFormat(L_dblSLRQT,3)+"|"+setNumberFormat(L_dblDOUQT,3));
						}
						M_rstRSSET.close();
					}
				}
//				else
//				{
//					M_strSQLQRY="";
//				}
				String[] L_staCOLHD=new String[] {"FL","Catagory","Grade","Package",(String)hstRGNCD.get("01"),(String)hstRGNCD.get("02"),(String)hstRGNCD.get("03"),"Total","Stock","Q.Hold","T/Dsp","S/R","U/Cl"};
				int[] L_staCOLSZ=new int[]{20,70,80,60,70,70,70,70,70,70,70,70,70};
				pnlREPORT=new JPanel(null);
				tblREPORT = crtTBLPNL1(pnlREPORT,L_staCOLHD,vtrPRDDS.size(),1,1,11,8,L_staCOLSZ,new int[]{0});
				tblREPORT.setEnabled(false);
				add(pnlREPORT,5,1,12,8,this,'L');

	//ADDING DATA TO TABLE
				int L_intROWID=0;//TABLE ROW NUMBER
				float L_fltRGN1VL=0,L_fltRGN2VL=0,L_fltRGN3VL=0,//FOR REGION WISE TOTAL FIGURES OF INDIVIDUAL GRADE
				//FOR PRODUCT TYPE WISE TOTALS
					L_flaTOTCL[]=new float[9],
					L_flaGRTOT[]=new float[9],
					L_flaPSTOT[]=new float[9],
					L_flaGPTOT[]=new float[9];
				
				//TEMP. FOR VERCTOR ELEMENTS
				StringTokenizer L_stkTEMP=null;
				int L_intSRTRW=0;//STARTING ROW NO FOR THE CATAGORY
				boolean L_flgDATA=false;//TO INDICATE, WHETHER DATA WAS FOUND IN THE CATAGORY OR NOT
				boolean L_flgSPPS=false,L_flgARPS=false,L_flgOTHERS=false;//TO INDICATE WHETHER HEADING FOR THIS CATAGORY IS DISPLAYED
				int t = 0;
				
				tblREPORT.setRowColor(L_intROWID,Color.blue);//HEADING FOR PPOLYSTYRENE
				tblREPORT.setValueAt("PS",L_intROWID++,intTB1_PRDCT);
				for(int z=0;z<vtrPGRCD.size();z++)
				{
					//System.out.println("z :"+z+" "+vtrPGRCD.elementAt(z).toString());
					L_intSRTRW=L_intROWID;//STARTING ROW NUMBER, TO DISPLAY HDG IF DATA FOUND
					for(int i=0;i<vtrPRDDS.size();i++)
					{
						//System.out.println("i :"+i+" "+vtrPRDDS.elementAt(i).toString());
						if(!vtrPRDCD.elementAt(i).toString().substring(0,4).equals(vtrPGRCD.elementAt(z)))
							continue;//FILTER FOR PRODUCT GROUP
						if(vtrPGRDS.elementAt(z).equals("GPPS") || vtrPGRDS.elementAt(z).equals("HIPS"))
							if(vtrPRDCD.elementAt(i).toString().substring(6,7).equals("1"))//PRIME GRADE OF GPPS
								continue;
							if(vtrPRDCD.elementAt(i).toString().substring(0,2).equals("52") && !L_flgOTHERS)
							{
								//DISPLAY TOTAL OF OTHERS
								L_flgOTHERS=true;
								tblREPORT.setRowColor(L_intROWID,Color.blue);
								tblREPORT.setValueAt("OTHERS(PS) TOTAL",L_intROWID,intTB1_PRDCT);
								for(t=0;t<L_flaGPTOT.length;t++)
									tblREPORT.setValueAt(setNumberFormat(L_flaGPTOT[t],3),L_intROWID,t+4);
								//L_flaGPTOT=new float[8];
								L_intROWID+=2;
								
								L_flgDATA=false;
								tblREPORT.setRowColor(L_intROWID,Color.magenta);
								tblREPORT.setValueAt(" PS TOTAL",L_intROWID,intTB1_PRDCT);
								for (t=0;t<L_flaPSTOT.length;t++)
									tblREPORT.setValueAt(setNumberFormat(L_flaPSTOT[t],3),L_intROWID,t+4);
								L_flaGPTOT=new float[9];
								L_intROWID+= 2;
								//L_intROWID++;
								
								//DISPLAY HEADING FOR SPPS
								tblREPORT.setRowColor(L_intROWID,Color.magenta);
								tblREPORT.setValueAt("SP. POLYSTYRENE",L_intROWID,intTB1_PRDCT);
								L_intROWID+= 2;
								L_intSRTRW+=4;
							}

							if(vtrPRDCD.elementAt(i).toString().substring(0,2).equals("53")&&!L_flgSPPS)
							{
							//DISPLAY TOTAL FOR SPPS
								L_flgSPPS=true;
								tblREPORT.setRowColor(L_intROWID,Color.magenta);
								for(t=0;t<L_flaGPTOT.length;t++)
									tblREPORT.setValueAt(setNumberFormat(L_flaGPTOT[t],3),L_intROWID,t+4);
								tblREPORT.setValueAt("SP. POLYSTYRENE TOTAL",L_intROWID++,intTB1_PRDCT);
								L_flaGPTOT=new float[9];
								L_intROWID++;
							//DISPLAY HEADING FOR ARTICLES OF PS
								tblREPORT.setRowColor(L_intROWID,Color.blue);
								tblREPORT.setValueAt("ARTICLES OF PS",L_intROWID++,intTB1_PRDCT);
								L_intSRTRW+=4;
								L_intROWID++;
							}

							if(vtrPRDCD.elementAt(i).toString().substring(0,2).equals("54")&&!L_flgARPS)
							{
							//DISPLAY TOTAL FOR SPPS
								L_flgARPS=true;
								tblREPORT.setRowColor(L_intROWID,Color.magenta);
								for(t=0;t<L_flaGPTOT.length;t++)
									tblREPORT.setValueAt(setNumberFormat(L_flaGPTOT[t],3),L_intROWID,t+4);
								tblREPORT.setValueAt("ARTICLE OF PS TOTAL",L_intROWID++,intTB1_PRDCT);
								L_flaGPTOT=new float[9];
								L_intROWID++;
							//DISPLAY HEADING FOR ARTICLES OF PS
								tblREPORT.setRowColor(L_intROWID,Color.blue);
								tblREPORT.setValueAt("MASTER BATCH",L_intROWID++,intTB1_PRDCT);
								L_intSRTRW+=4;
								L_intROWID++;
							}
						if(hstBKGDT.containsKey(vtrPRDDS.elementAt(i))||hstSTKDT.containsKey(vtrPRDDS.elementAt(i)))
						{
							L_flgDATA=true;//DATA FOUND IN CURREN CATAGORY
							L_intROWID++;
							//System.out.println((String)vtrPRDDS.elementAt(i) + "/" + L_intROWID);
							L_stkTEMP=new StringTokenizer((String)vtrPRDDS.elementAt(i),"|");
							tblREPORT.setValueAt(L_stkTEMP.nextToken(),L_intROWID,intTB1_PRDDS);
							tblREPORT.setValueAt(L_stkTEMP.nextToken(),L_intROWID,intTB1_PKGWT);
							if(hstBKGDT.containsKey(vtrPRDDS.elementAt(i)))
							{
								L_fltRGN1VL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,intTB1_WC_RG).toString(),"0.0"));
								L_fltRGN2VL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,intTB1_NESRG).toString(),"0.0"));
								L_fltRGN3VL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,intTB1_EXPRG).toString(),"0.0"));
								L_vtrTEMP=(Vector<String>)hstBKGDT.get(vtrPRDDS.elementAt(i));
								for(int j=0;j<L_vtrTEMP.size();j++)
								{
									L_stkTEMP= new StringTokenizer((String)L_vtrTEMP.elementAt(j),"|");
									L_strTEMP=L_stkTEMP.nextToken();
									//CALCULATE REGIONWISE VALUES
									if(L_strTEMP.equals("01"))
									{
										L_fltRGN1VL+=Float.parseFloat(L_stkTEMP.nextToken());
										tblREPORT.setValueAt(setNumberFormat(L_fltRGN1VL,3),L_intROWID,intTB1_WC_RG);
									}
									if(L_strTEMP.equals("02"))
									{
										L_fltRGN2VL+=Float.parseFloat(L_stkTEMP.nextToken());
										tblREPORT.setValueAt(setNumberFormat(L_fltRGN2VL,3),L_intROWID,intTB1_NESRG);
									}
									if(L_strTEMP.equals("03"))
									{
										L_fltRGN3VL+=Float.parseFloat(L_stkTEMP.nextToken());
										tblREPORT.setValueAt(setNumberFormat(L_fltRGN3VL,3),L_intROWID,intTB1_EXPRG);
									}
								}
								tblREPORT.setValueAt(setNumberFormat(L_fltRGN3VL+L_fltRGN2VL+L_fltRGN1VL,3),L_intROWID,intTB1_TOTBK);
								L_flaTOTCL[0]+=L_fltRGN1VL;L_flaTOTCL[1]+=L_fltRGN2VL;L_flaTOTCL[2]+=L_fltRGN3VL;L_flaTOTCL[3]+=L_fltRGN1VL+L_fltRGN2VL+L_fltRGN3VL;
							}
							if(hstSTKDT.containsKey(vtrPRDDS.elementAt(i)))
							{
								L_stkTEMP=new StringTokenizer((String)hstSTKDT.get(vtrPRDDS.elementAt(i)),"|");
								int L_intCOLID=8;
								while(L_stkTEMP.hasMoreTokens())
								{
									L_strTEMP=L_stkTEMP.nextToken();
									if(Float.parseFloat(L_strTEMP)>0)
										tblREPORT.setValueAt(L_strTEMP,L_intROWID,L_intCOLID++);
									else
										L_intCOLID++;
								}
								for(t=4;t<L_flaTOTCL.length;t++)
									L_flaTOTCL[t]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,t+4).toString(),"0.0f"));
							}
						}
					}
					
					
					if(L_flgDATA)
					{
						//DISPLAY HDG FOR CURRENT CATAGORY
						tblREPORT.setRowColor(L_intSRTRW,Color.blue);
						if(vtrPGRDS.elementAt(z).equals("GPPS") || vtrPGRDS.elementAt(z).equals("HIPS"))//ATACH 'PRIME' FOR HI OR GP
							tblREPORT.setValueAt(vtrPGRDS.elementAt(z)+" PRIME",L_intSRTRW,intTB1_PRDCT);
						else
							tblREPORT.setValueAt(vtrPGRDS.elementAt(z),L_intSRTRW,intTB1_PRDCT);
						L_intROWID++;
						//DISPLAY TOTALS OF CURREN CATAGORY
						tblREPORT.setRowColor(L_intROWID,Color.blue);
						if(vtrPGRDS.elementAt(z).equals("GPPS") || vtrPGRDS.elementAt(z).equals("HIPS"))
							tblREPORT.setValueAt(vtrPGRDS.elementAt(z)+" PRIME TOTAL",L_intROWID,intTB1_PRDCT);
						else
							tblREPORT.setValueAt(vtrPGRDS.elementAt(z)+" TOTAL",L_intROWID,intTB1_PRDCT);
						for (t=0;t<L_flaTOTCL.length;t++)
						{//DISPLAY TOTAL AND CALCULATE GOURP AND GRAND TOTAL
							tblREPORT.setValueAt(setNumberFormat(L_flaTOTCL[t],3),L_intROWID,t+4);
							L_flaPSTOT[t]+=L_flaTOTCL[t];
							L_flaGPTOT[t]+=L_flaTOTCL[t];
							L_flaGRTOT[t]+=L_flaTOTCL[t];
						}
						L_flaTOTCL=new float[9];//ININTIALISE CATAGORY TOTAL
						L_intROWID++;L_intROWID++;
						L_flgDATA=false;
					}

					
					if(vtrPGRDS.elementAt(z).equals("GPPS") || vtrPGRDS.elementAt(z).equals("HIPS"))
					{//RUN THE ABOVE LOOP TWICE FOR GP & HI, TO GET DETAILS OF NON-PRIME GRADES
						L_intSRTRW=L_intROWID;
						for(int i=0;i<vtrPRDDS.size();i++)
						{
							if(!vtrPRDCD.elementAt(i).toString().substring(0,4).equals(vtrPGRCD.elementAt(z)))
								continue;
							if(!vtrPRDCD.elementAt(i).toString().substring(6,7).equals("1"))//PRIME GRADE OF GPPS
								continue;
							if(hstBKGDT.containsKey(vtrPRDDS.elementAt(i))||hstSTKDT.containsKey(vtrPRDDS.elementAt(i)))
							{
								L_flgDATA=true;
								L_intROWID++;
								L_stkTEMP=new StringTokenizer((String)vtrPRDDS.elementAt(i),"|");
								tblREPORT.setValueAt(L_stkTEMP.nextToken(),L_intROWID,intTB1_PRDDS);
								tblREPORT.setValueAt(L_stkTEMP.nextToken(),L_intROWID,intTB1_PKGWT);
								if(hstBKGDT.containsKey(vtrPRDDS.elementAt(i)))
								{
									L_fltRGN1VL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,intTB1_WC_RG).toString(),"0.0"));
									L_fltRGN2VL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,intTB1_NESRG).toString(),"0.0"));
									L_fltRGN3VL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,intTB1_EXPRG).toString(),"0.0"));
									L_vtrTEMP=(Vector<String>)hstBKGDT.get(vtrPRDDS.elementAt(i));
									for(int j=0;j<L_vtrTEMP.size();j++)
									{
										L_stkTEMP= new StringTokenizer((String)L_vtrTEMP.elementAt(j),"|");
										L_strTEMP=L_stkTEMP.nextToken();
										if(L_strTEMP.equals("01"))
										{
											L_fltRGN1VL+=Float.parseFloat(L_stkTEMP.nextToken());
											tblREPORT.setValueAt(setNumberFormat(L_fltRGN1VL,3),L_intROWID,intTB1_WC_RG);
										}
										if(L_strTEMP.equals("02"))
										{
											L_fltRGN2VL+=Float.parseFloat(L_stkTEMP.nextToken());
											tblREPORT.setValueAt(setNumberFormat(L_fltRGN2VL,3),L_intROWID,intTB1_NESRG);
										}
										if(L_strTEMP.equals("03"))
										{
											L_fltRGN3VL+=Float.parseFloat(L_stkTEMP.nextToken());
											tblREPORT.setValueAt(setNumberFormat(L_fltRGN3VL,3),L_intROWID,intTB1_EXPRG);
										}
									}
									tblREPORT.setValueAt(setNumberFormat(L_fltRGN3VL+L_fltRGN2VL+L_fltRGN1VL,3),L_intROWID,intTB1_TOTBK);
									L_flaTOTCL[0]+=L_fltRGN1VL;L_flaTOTCL[1]+=L_fltRGN2VL;L_flaTOTCL[2]+=L_fltRGN3VL;L_flaTOTCL[3]+=L_fltRGN1VL+L_fltRGN2VL+L_fltRGN3VL;
								}
								if(hstSTKDT.containsKey(vtrPRDDS.elementAt(i)))
								{
									L_stkTEMP=new StringTokenizer((String)hstSTKDT.get(vtrPRDDS.elementAt(i)),"|");
									int L_intCOLID=8;
									while(L_stkTEMP.hasMoreTokens())
									{
										L_strTEMP=L_stkTEMP.nextToken();
										if(Float.parseFloat(L_strTEMP)>0)
											tblREPORT.setValueAt(L_strTEMP,L_intROWID,L_intCOLID++);
										else
											L_intCOLID++;
									}
									for(t=4;t<L_flaTOTCL.length;t++)
										L_flaTOTCL[t]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID,t+4).toString(),"0.0f"));
								}
							}
						}
					}
					
					if(L_flgDATA)
					{
						//DISPLAY HEADING FOR CURRENT CATAGORY
						tblREPORT.setRowColor(L_intSRTRW,Color.blue);
						tblREPORT.setValueAt(vtrPGRDS.elementAt(z)+" NON-PRIME",L_intSRTRW,intTB1_PRDCT);
						L_intROWID++;
						//DISPLAY TOTALS
						tblREPORT.setRowColor(L_intROWID,Color.blue);
						tblREPORT.setValueAt(vtrPGRDS.elementAt(z)+" NON-PRIME TOTAL",L_intROWID,intTB1_PRDCT);
						for (t=0;t<L_flaTOTCL.length;t++)
						{//DISPLAY TOTAL FOR THE CATAGORY AND CALCULATE GROUP & GRAND TOTAL 
							tblREPORT.setValueAt(setNumberFormat(L_flaTOTCL[t],3),L_intROWID,t+4);
							L_flaPSTOT[t]+=L_flaTOTCL[t];
							L_flaGPTOT[t]+=L_flaTOTCL[t];
							L_flaGRTOT[t]+=L_flaTOTCL[t];
						}
						L_flaTOTCL=new float[9];
						L_intROWID++;L_intROWID++;
						//DISPLAY TOTAL FOR GPPS & HIPS
						tblREPORT.setRowColor(L_intROWID,Color.blue);
						tblREPORT.setValueAt(vtrPGRDS.elementAt(z)+" TOTAL",L_intROWID,intTB1_PRDCT);
						for (t=0;t<L_flaTOTCL.length;t++)
							tblREPORT.setValueAt(setNumberFormat(L_flaGPTOT[t],3),L_intROWID,t+4);
						L_flaGPTOT=new float[9];
						L_intROWID++;L_intROWID++;
						L_flgDATA=false;
						//if(vtrPGRDS.elementAt(z).equals("HIPS"))
						//{//DISPLAY POLYSTYRENE TOTAL
						//	tblREPORT.setRowColor(L_intROWID,Color.magenta);
						//	tblREPORT.setValueAt(" PS TOTAL",L_intROWID,1);
						//	for (t=0;t<L_flaPSTOT.length;t++)
						//		tblREPORT.setValueAt(setNumberFormat(L_flaPSTOT[t],3),L_intROWID,t+4);
						//	L_flaGPTOT=new float[8];
						//	L_intROWID++;L_intROWID++;
						//}
					}					
				}

			//L_intROWID+=2;
			//DISPLAY TOTAL OF LAST GROUP i.e. ARTICLES OF POLYSTYRENE
				tblREPORT.setRowColor(L_intROWID,Color.magenta);
				tblREPORT.setValueAt("MASTER BATCH",L_intROWID,intTB1_PRDCT);
				for (t=0;t<L_flaTOTCL.length;t++)
					tblREPORT.setValueAt(setNumberFormat(L_flaGPTOT[t],3),L_intROWID,t+4);
				L_intROWID++;L_intROWID++;
			//DISPLAY GRAND TOTAL
				tblREPORT.setRowColor(L_intROWID,Color.red);
				tblREPORT.setValueAt("ACCOUNTABLE TOTAL ",L_intROWID,intTB1_PRDCT);
				for (t=0;t<L_flaTOTCL.length;t++)
					tblREPORT.setValueAt(setNumberFormat(L_flaGRTOT[t],3),L_intROWID,t+4);
				//flgOLDRP=true;	
			//}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst))
			{
				genRPTFL();
			}
			//exeSELFML(cl_dat.M_strREPSTR_pbst+"mr_rpcoo.doc","Order Outstanding Summary");
		}catch(Exception e)
		{setMSG(e,"exePRINT");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	/**To print Report header	 */
	private void prnHEADR()
	{
		try
		{
			M_intPAGNO++;
			if(M_intPAGNO>1)
			{
				if (M_rdbTEXT.isSelected())
					prnFMTCHR(dosREPORT,M_strBOLD);
				else
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes("Remark : Stock figures are as on 07:00hrs of "+strPRCDT+"  Stock /Order qty. is in MT   Stock includes Target Despatch & Sales Return\n");
				if (M_rdbTEXT.isSelected())
					prnFMTCHR(dosREPORT,M_strEJT);
			}
			if (M_rdbTEXT.isSelected())
				prnFMTCHR(dosREPORT,M_strCPI17);
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LIMITED",144)+"Date :"+cl_dat.M_strLOGDT_pbst+"\n");
			dosREPORT.writeBytes(padSTRING('R',"POLYSTYRENE Customer Order Outstanding For Dispatch (Summary Report)",149)+"Page :   "+M_intPAGNO+"\n");
			dosREPORT.writeBytes("As On : "+cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText()+"hrs\n");
			dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes("    "+padSTRING('R',"GRADE",16));
			dosREPORT.writeBytes(padSTRING('L',"PKG. Wt.",14));
			dosREPORT.writeBytes(padSTRING('L',"W & C",14));
			dosREPORT.writeBytes(padSTRING('L',"N, E & S",14));
			dosREPORT.writeBytes(padSTRING('L',"EXPORT",14));
			dosREPORT.writeBytes(padSTRING('L',"TOTAL",14));
			dosREPORT.writeBytes(padSTRING('L',"Stock",14));
			dosREPORT.writeBytes(padSTRING('L',"Q.Hold",14));
			dosREPORT.writeBytes(padSTRING('L',"T/Dsp",14));
			dosREPORT.writeBytes(padSTRING('L',"S/R",14));
			dosREPORT.writeBytes(padSTRING('L',"U/Cl",14));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			if (M_rdbTEXT.isSelected())
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			else
				dosREPORT.writeBytes("</B>");
			M_intLINNO=7;
		}catch(Exception e)
		{setMSG(e,"prnHEADR");}
	}
	/**Sets flag to fire query to DB if report date is edited	 */
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
		//if(M_objSOURC==M_txtTODAT)
		//	flgOLDRP=false;
	}
	/**Set report date and focus nevigation	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC==M_txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				//M_calLOCAL.add(Calendar.DATE,-1);
				M_txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				M_txtTODAT.requestFocus();
			}
		}
		
		catch(Exception e)
		{setMSG(e,"actionPerformed");}
	}

	/** Self E-mailing the report file
	 //
	private void exeSELFML(String LP_DOCFL,String LP_MSGDS)
	{
		try
		{
			if(chkSELFML.isSelected())
			{
				String L_strEML = "";
				cl_eml ocl_eml = new cl_eml();
				M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+cl_dat.M_strUSRCD_pbst+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null && M_rstRSSET.next())
				{
					L_strEML = M_rstRSSET.getString("US_EMLRF");
					if(L_strEML.length() >0)
						ocl_eml.sendfile(L_strEML,LP_DOCFL,LP_MSGDS,LP_MSGDS);
					System.out.println("Sending mail to : "+L_strEML);
				}
			}
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeSELFML");}
	}
*/
	
void crtHSTQLHQT()
{
	try
	{
		hstQLHQT.clear();
		M_strSQLQRY = "select pr_prdds,cmt_ncsvl st_pkgwt,sum(isnull(st_dosqt,0))  st_dosqt from fg_stmst,co_prmst,co_cdtrn,pr_ltmst where SUBSTRING(PR_PRDCD,1,2)  "+(chkXPS.isSelected() ? " = 'SX'" : " <> 'SX' ")+"  and st_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(ST_PRDCD,1,2)  "+(chkXPS.isSelected() ? " = 'SX'" : " <> 'SX' ")+" and st_prdcd=pr_prdcd and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and cmt_codcd = st_pkgtp and isnull(st_dosqt,0)>0 and st_cmpcd=lt_cmpcd and st_prdtp=lt_prdtp and st_lotno=lt_lotno and st_rclno=lt_rclno and lt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"'  and SUBSTRING(LT_PRDCD,1,2)  "+(chkXPS.isSelected() ? " = 'SX'" : " <> 'SX' ")+" and isnull(lt_resfl,' ') in ('Q','H') group by pr_prdds,cmt_ncsvl order by pr_prdds,cmt_ncsvl";
		//System.out.println(M_strSQLQRY);
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if(L_rstRSSET == null || !L_rstRSSET.next())
			return;
		while(true)
		{
			hstQLHQT.put(L_rstRSSET.getString("PR_PRDDS").trim()+"|"+L_rstRSSET.getString("ST_PKGWT").trim(),L_rstRSSET.getString("ST_DOSQT"));
			if(!L_rstRSSET.next())
				break;
		}
		L_rstRSSET.close();
	}
		catch (Exception L_EX) {setMSG(L_EX,"crtHSTQLHQT");}
	}
	

void genRPTFL()
{
	try
	{
		File filREPORT=null;
		if(M_rdbTEXT.isSelected())
			filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_rpcoo.doc");
		else
			filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_rpcoo.html");
		FileOutputStream filOUT=new FileOutputStream(filREPORT);
		dosREPORT=new DataOutputStream(filOUT);
		if(M_rdbHTML.isSelected())
		{
		    dosREPORT.writeBytes("<HTML><HEAD><Title> Stock Statement </title> </HEAD> <BODY><P><PRE style =\" font-size : 7 pt \">");    
		    dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
		}

		prnHEADR();
		String L_strPADST="-";
				int i=0;
				String L_strBLANK="";//TO INDICATE STATRING GAP FOR PRINTING LINE, FOR BETTER INDENTING IN PRINT
				for(i=0;i<tblREPORT.getRowCount();i++)
				{
					if(tblREPORT.getValueAt(i,intTB1_PRDCT).toString().length()==0&&tblREPORT.getValueAt(i,intTB1_PRDDS).toString().length()==0)
						if(tblREPORT.getValueAt(i+1,intTB1_PRDCT).toString().length()==0&&tblREPORT.getValueAt(i+1,intTB1_PRDDS).toString().length()==0)
							if(tblREPORT.getValueAt(i+1,intTB1_PRDCT).toString().length()==0&&tblREPORT.getValueAt(i+2,intTB1_PRDDS).toString().length()==0)
								break;
					if(M_intLINNO>60)
						prnHEADR();
					L_strPADST="-";
					if(tblREPORT.getValueAt(i,intTB1_PRDDS)==null)
						L_strPADST=" ";
					else if(tblREPORT.getValueAt(i,intTB1_PRDDS).toString().length()==0)
						L_strPADST=" ";
			//REDUCE STARTING GAP AFTER DISPLAY OF TOTAL
					if(tblREPORT.getCellColor(i,intTB1_PRDCT).equals(Color.magenta) && L_strBLANK.length()>2)
					   L_strBLANK=L_strBLANK.substring(2,L_strBLANK.length());
			//MAKE TOTALS BOLD IN PRINT
					if(!tblREPORT.getCellColor(i,intTB1_PRDCT).equals(Color.black))
					{	
						if (M_rdbTEXT.isSelected())
							prnFMTCHR(dosREPORT,M_strBOLD);
						else dosREPORT.writeBytes("<B>");
					}
					if(tblREPORT.getValueAt(i,intTB1_PRDCT)!=null)
						if(tblREPORT.getValueAt(i,intTB1_PRDCT).toString().length()>0)
						{
							if (M_rdbTEXT.isSelected())
								dosREPORT.writeBytes(L_strBLANK+padSTRING('R',tblREPORT.getValueAt(i,intTB1_PRDCT).toString(),34-L_strBLANK.length()));
							else
								dosREPORT.writeBytes(L_strBLANK+padSTRING('R',tblREPORT.getValueAt(i,intTB1_PRDCT).toString(),34-L_strBLANK.length()));
						}
					//DETERMINE LENGTH OF STARTING GAP OF LINE
					if(!tblREPORT.getCellColor(i,intTB1_PRDCT).equals(Color.black))
					{
						if (M_rdbTEXT.isSelected())
							prnFMTCHR(dosREPORT,M_strBOLD);
						else
							dosREPORT.writeBytes("<B>");
						if(tblREPORT.getValueAt(i,intTB1_WC_RG).toString().length()==0)
							L_strBLANK+="  ";
						else if(L_strBLANK.length()>2)
							L_strBLANK=L_strBLANK.substring(2,L_strBLANK.length());
					}
			//WRITE TO FILE
					for(int j=(L_strPADST.equals(" ")==true ? 4 : 2);j<tblREPORT.getColumnCount();j++)
					{
						if(j==2)
						{
							dosREPORT.writeBytes(L_strBLANK+padSTRING('R',tblREPORT.getValueAt(i,intTB1_PRDDS).toString(),20-L_strBLANK.length()));
							continue;
						}
						try
						{
							Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"0"));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),L_strPADST),14));
						}catch(NumberFormatException e)
						{
							if(tblREPORT.getValueAt(i,j).equals(tblREPORT.getValueAt(i-1,j)))
								dosREPORT.writeBytes(padSTRING('R'," ",14));
							else
							{
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),L_strPADST),14));
								//System.out.println(nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"-"));
							}
						}
					}
				if (M_rdbTEXT.isSelected())
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				else
					dosREPORT.writeBytes("</B>");

					dosREPORT.writeBytes("\n");
					M_intLINNO++;
				}
				double L_dblSTKQT = Double.parseDouble(tblREPORT.getValueAt(i-1,intTB1_STKQT).toString())+Double.parseDouble(tblREPORT.getValueAt(i-1,intTB1_UCLQT).toString());
				if (M_rdbTEXT.isSelected())			
					prnFMTCHR(dosREPORT,M_strBOLD);
				else
					dosREPORT.writeBytes("<B>");

				dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes("Total Outstanding Quantity for despatch :               "+tblREPORT.getValueAt(i-1,intTB1_TOTBK).toString()+"\n\n");
				dosREPORT.writeBytes("Total Stock                             :               "+setNumberFormat(L_dblSTKQT,3)+"\n\n");
				dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				if(M_rdbHTML.isSelected())				
					dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
				dosREPORT.flush();
				dosREPORT.close();
				filOUT.close();
				if(M_rdbTEXT.isSelected())
					doPRINT(cl_dat.M_strREPSTR_pbst+"mr_rpcoo.doc");
				else 
		      	{    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+cl_dat.M_strREPSTR_pbst+"mr_rpcoo.html"); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"genRPTFL");
			}
			
}
}
