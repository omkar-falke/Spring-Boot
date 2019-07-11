/*
System Name		: Materials Management System
Program Name	: Processing Reports
Author			: AAP

Modified Date	: 26/07/2004
Documented Date	: 
Version			: v2.0.0
*/

import java.io.IOException;import java.io.File;import java.io.FileOutputStream;
import java.io.DataOutputStream;
import javax.swing.*;
import javax.swing.BorderFactory;import javax.swing.ButtonGroup;import javax.swing.JComboBox;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.util.Enumeration;import java.util.StringTokenizer;import java.util.Vector;
import java.util.Hashtable;import java.util.Calendar;import java.util.Arrays;
import java.awt.Color; import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

/**<BODY><P><FONT size=4><STRONG>Program Description :</STRONG></FONT>  </P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Materials Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>     Processing Reports.</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                                                                                Gives&nbsp;all reports       for monthly as well as yearly processing. </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>               existing reports&nbsp;      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mm_rpprc.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mm_rpprc.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>29/07/2004&nbsp; </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;&nbsp;        </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><P><FONT color=purple><STRONG>   </STRONG></FONT>&nbsp;</P><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="LEFT: 11px; TOP:  373px; WIDTH: 100%" width="100%" background="" borderColorDark=white borderColorLight=darkslateblue borderColor=black>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center> Columns</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>MM_YRPRC</TD>    <TD>&nbsp;IN_INDDT,IN_ZONCD,IN_MKTTP,IN_SALTP</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_PSLYR</TD>    <TD>&nbsp;INT_INDNO,INT_PRDDS,INT_PKGWT,INT_INDQT</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_GRMST</TD>    <TD>GR_STRTP,GR_GRNNO,GR_MATCD</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_MRMST</TD>    <TD>MR_STRTP,MR_GRNNO,MR_MATCD</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_SAMST</TD>    <TD>SA_STRTP,SA_GRNNO,SA_MATCD</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_ISMST</TD>    <TD >        IS_STRTP,IS_GRNNO,IS_MATCD,IS_TAGNO</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_CTMST</TD>    <TD>CT_STRTP,CT_CODTP,CT_MATCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE><P>&nbsp;</P><P><FONT color=purple><STRONG>Report is Available in following Catagories : </STRONG></FONT></P><UL>  <LI>Store Type : Engg. Stores <STRONG>only</STRONG>  &nbsp;  <LI>  With or without Raw, Pkg. &amp; Misc. Material (i.e.   Material groups&nbsp;68,69,85,86)&nbsp;</LI></UL><P><FONT color=purple><STRONG>Following Reports&nbsp;are Available in&nbsp;this screen : </STRONG></FONT></P><UL>       <LI>Stores Ledger : Includes date and transaction type   wise details of trasactions for all material codes  <LI>Transaction Summary (Group-Wise) : Material Group wise Opening, Receipt,   Issue, Closing values  <LI>Transaction Summary Details (Group-Wise) : Above report including qty.  <LI>Stock / Value Detail (Material-Wise) : &nbsp;Material code wise Opening,   GRIN, MRN, Issue, SAN and Closing values along with group wise totals</LI></UL><P>Separate methods are defined for each type of report. In individual method, Only query is constructed with common allias for yearly or monthly report and data processing and report generation is done in common code.</P></BODY>*/
class mm_rpprc extends cl_rbase
{
	private DataOutputStream dosREPORT ;/** Monthly processing report option*/
	private JRadioButton rdbMONTH;/** Yearly processing report option*/
	private JRadioButton rdbYEAR;/**  Report Type options*/
	private cl_Combo cmbRPTOP;/**  Check box to include Raw, Pkg. & Misc. Material*/
	private JCheckBox chbGRPAL;/** Title of the report*/
	private JCheckBox chbIMPIT;/** Imported Items*/
	private String strRPTTL;/** String for current material group code in Stock value summary (exeRPSVS())*/
	private String strMGPCD;/** For material group Descriptions in Stock value summary (exeRPSVS()) Key : Group Code; Value : Description*/
	private Hashtable hstMGPDS;/** Process to display report*/
	private Process prcREPORT;
	private Statement stmSTBK1;
	private Connection conSPBKA;
	private DataOutputStream dosDIFF ;/** separate file for diff checking*/
	/**Constructss the form 
	 * 
	 * Constructss the form 
	 * 
	 * <p>Populates report type combo. Report types and realted codes are hard coded .<br>Populates hstMGPDS (Material group code & description for RPSVS) from CO_CTMST
	 */
	mm_rpprc()
	{
		super(2);
		try
		{
			JPanel L_pnlTEMP=new JPanel(null);
			add(rdbMONTH=new JRadioButton("Monthly"),1,1,1,1,L_pnlTEMP,'L');
			add(rdbYEAR=new JRadioButton("Yearly"),1,2,1,1,L_pnlTEMP,'L');
			ButtonGroup L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbMONTH);L_btgTEMP.add(rdbYEAR);
			add(L_pnlTEMP,3,1,2,2.1,this,'L');
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder("Duration"));
			add(new JLabel("Select Report Type"),4,4,1,1.5,this,'R');
			add(cmbRPTOP=new cl_Combo(),4,5,1,3,this,'L');
			cmbRPTOP.addItem("Stores Ledger","0");
			cmbRPTOP.addItem("Transaction Summary (Group-Wise)","1");
			cmbRPTOP.addItem("Transaction Summary Details (Group-Wise)","2");
			cmbRPTOP.addItem("Stock / Value Detail (Material-Wise)","3");
			cmbRPTOP.addItem("Create DBF Files","4");
			cmbRPTOP.addItem("Transaction Summary (Category-Wise)","5");
			cmbRPTOP.addItem("Transaction Summary (Ownership-Wise)","6");
			cmbRPTOP.addItem("Closing Stock / Value Detail (Material-Wise)","7");
			add(chbGRPAL=new JCheckBox("Include Raw, Pkg. & Misc. Material"),6,5,1,3,this,'L');
			add(chbIMPIT=new JCheckBox("Imported Items"),7,5,1,3,this,'L');
			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CTMST where CT_CODTP='MG'");
			hstMGPDS=new Hashtable(50,0.75f);
			while (M_rstRSSET.next())
				hstMGPDS.put(M_rstRSSET.getString("CT_MATCD").substring(0,2),M_rstRSSET.getString("CT_MATDS"));
			M_rstRSSET.close();
		}catch(Exception e)
		{setMSG(e,"Child.Constructor");}
	}
	/**Focus nevigation	 */
	public void keyPressed(KeyEvent P_KE)
	{
		super.keyPressed(P_KE);
		if(P_KE.getKeyCode() == P_KE.VK_ENTER)
		{
			if(M_objSOURC == chbGRPAL)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC == rdbYEAR)
				cmbRPTOP.requestFocus();
			else if(M_objSOURC == M_cmbDESTN)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	/**Focus Nevigation	 */
	public void actionPerformed(ActionEvent P_AE)
	{
		super.actionPerformed(P_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				rdbMONTH.requestFocus();
			else if(M_objSOURC == rdbMONTH)
				M_txtFMDAT.requestFocus();
			else if(M_objSOURC == rdbYEAR)
				M_txtFMDAT.requestFocus();
			else if(M_objSOURC == M_txtFMDAT)
				M_txtTODAT.requestFocus();
			else if(M_objSOURC == chbGRPAL)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst))
					M_cmbDESTN.requestFocus();
				else
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if	(M_objSOURC == M_txtTODAT)
				cmbRPTOP.requestFocus();
		}catch(Exception e)
		{setMSG(e,"Child.actionPerformed");}
	}
	/**To generate the report as per selection of cmbRPTOP.
	 * 
	 * To generate the report as per selection of cmbRPTOP.
	 * 
	 * <p>Generates report title using selected item description of cmbRPTOP and selection of rdbMONTH<br>
	 * Generates string of file name using selected item of cmbRPTOP<br>
	 * Gives call to respective method of report generation<br>
	 * Processes report as per cmbOPTN selection
	 */
	void exePRINT()
	{
		try
		{
			this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);
			M_intLINNO=0;
			M_intPAGNO=0;
			File L_filREPORT=null;
			String L_strFILNM=null;

		//SETTING REPORT TITLE
			strRPTTL=(String)cmbRPTOP.getSelectedItem();
			if(rdbMONTH.isSelected())
			{
				SimpleDateFormat L_fmtMONTH=new SimpleDateFormat("MMMMM,yyyy");
				strRPTTL+=" For the Month of "+L_fmtMONTH.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()));
			}
			else if(rdbYEAR.isSelected())
			{
				strRPTTL+=" For the Year from "+M_txtFMDAT.getText()+" to " +M_txtTODAT.getText();
			}
			
			if(!chbGRPAL.isSelected())
				strRPTTL+=" (Acct.)";
			
			if(chbIMPIT.isSelected())
				strRPTTL+=" (Imported Items)";
			
		//GENERATING REPORT FILE NAME
			if(cmbRPTOP.getITMCD().equals("0"))
				L_strFILNM="C:\\reports\\MM_RPLGR.DOC";
			else if(cmbRPTOP.getITMCD().equals("1"))
				L_strFILNM="C:\\reports\\MM_RPgts.DOC";
			else if(cmbRPTOP.getITMCD().equals("5"))
				L_strFILNM="C:\\reports\\MM_RPgts1.DOC";
			else if(cmbRPTOP.getITMCD().equals("6"))
				L_strFILNM="C:\\reports\\MM_RPgts2.DOC";
			else if(cmbRPTOP.getITMCD().equals("7"))
				L_strFILNM="C:\\reports\\MM_RPSVS1.DOC";
			
			else if(cmbRPTOP.getITMCD().equals("2"))
				L_strFILNM="C:\\reports\\MM_RPGTD.DOC";
			else if(cmbRPTOP.getITMCD().equals("3"))
				L_strFILNM="C:\\reports\\MM_RPSVS.DOC";
			if(!cmbRPTOP.getITMCD().equals("4"))	
			{
				L_filREPORT=new File(L_strFILNM);
				FileOutputStream L_fosREPORT=new FileOutputStream(L_filREPORT);
				dosREPORT=new DataOutputStream(L_fosREPORT);
			}	
			if(cmbRPTOP.getITMCD().equals("1"))
			{	
			    File L_filTEMP=new File("C:\\reports\\MM_PRDIFF.DOC");
				FileOutputStream L_fosTEMP=new FileOutputStream(L_filTEMP);
				dosDIFF=new DataOutputStream(L_fosTEMP);
			}
			boolean L_flgREPORT=true;
			if(cmbRPTOP.getITMCD().equals("0"))
				L_flgREPORT=exeRPLGR();
			else if(cmbRPTOP.getITMCD().equals("1"))
				L_flgREPORT=exeRPGTS();
			else if(cmbRPTOP.getITMCD().equals("2"))
				L_flgREPORT=exeRPGTD();
			else if(cmbRPTOP.getITMCD().equals("3"))
				L_flgREPORT=exeRPSVS();
			else if(cmbRPTOP.getITMCD().equals("5"))
				L_flgREPORT=exeRPGTS1();
			else if(cmbRPTOP.getITMCD().equals("6"))
				L_flgREPORT=exeRPGTS2();
			else if(cmbRPTOP.getITMCD().equals("7"))
				L_flgREPORT=exeRPSVS1();
			
			else if(cmbRPTOP.getITMCD().equals("4"))
			{
				setCONFTB("C:\\reports");
			// xcopy Not working thus copy the files externally 	
			System.out.println("Before xopy");	
			prcREPORT = Runtime.getRuntime().exec("xcopy.exe F:\\FOXDAT\\0102\\datatfr\\mmsdata\\*.dbf c:\\reports\\*.dbf");
			System.out.println("after xcopy");
				if(rdbMONTH.isSelected())
					L_flgREPORT=crtMNDBF();
					
				else
					L_flgREPORT=crtYRDBF();
			}
			if(!cmbRPTOP.getITMCD().equals("4"))
			dosREPORT.close();
			else
				L_flgREPORT = false;
			if(L_flgREPORT)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst))
					doPRINT(L_strFILNM);
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst))
					prcREPORT = Runtime.getRuntime().exec("c:\\windows\\wordpad.exe "+L_strFILNM);
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst))
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
						((cl_eml)Class.forName("cl_eml").newInstance()).sendfile(M_cmbDESTN.getItemAt(i).toString(),L_strFILNM,strRPTTL,"");
			}
		}catch(Exception e)
		{setMSG(e,"Child.exePRINT");}
		 finally{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	/**
	 * Method to generate STORES LEDGER REPORT
	 * 
	 * Method to generate STORES LEDGER REPORT
	 * 
	 * <p> if rdbMONTH is selected, generates Monthly report as : <br>
	 * Create temporary table named as : USRCD+HH+MM e.g. SYS1710 is table created with SYS login at 1710hrs.<br>
	 * Populates temporary table with data for given period from MM_GRMST, MM_ISMST, MM_SAMST, MM_MRMST and assigns document type to the transaction.<br>
	 * Collects data from MM_STPRC for month opening and closing values<br>
	 * Collects data from Temporary table for transaction details for the month <br>
	 * Drop the temporary table after report is generated<br><br>
	 * if rdbYEAR is selected, generates Yearly report as : <br>
	 * Collects data from MM_YRPRC for year opening and closing values<br>
	 * Collects data from MM_PSLYR for for transaction details for the year <br><br>
	 * 
	 * COMMON PROCESSING :
	 * Disply Material code wise transaction detail sorted by transaction date in sequence of : GRIN, MRN, +SAN, -SAN, MIN<br>
	 * Display opening and closing stock for the material code
	 */
	private boolean exeRPLGR()
	{
		this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);
		String L_strTBLNM=null;
		ResultSet L_rstRSSET=null;
		try
		{
			if(rdbYEAR.isSelected())
			{
				setMSG("Collecting report data ..",'N');
				L_rstRSSET=cl_dat.exeSQLQRY0("Select YR_MATCD MATCD, YR_MATDS MATDS, YR_YOSQT OPSQT, YR_YOSVL OPSVL, YR_YCSQT CLSQT, YR_YCSVL CLSVL from MM_YRPRC  where  "+(chbGRPAL.isSelected() ? "" : "  substr(yr_MATCD,1,2) not in('68','69','85','86') and ")+"  YR_STRTP='"+M_strSBSCD.substring(2,4)+"' order by YR_MATCD");
				M_rstRSSET=cl_dat.exeSQLQRY0("Select PS_DOCNO DOCNO, PS_DOCDT DOCDT, PS_DOCTP DOCTP, PS_MATCD MATCD, PS_DOCQT DOCQT, PS_DOCVL DOCVL from MM_PSLYR where  "+(chbGRPAL.isSelected() ? "" : " substr(Ps_MATCD,1,2) not in('68','69','85','86') and ")+"  PS_STRTP='"+M_strSBSCD.substring(2,4)+"' order by PS_MATCD,PS_DOCTP");
				setMSG("Formatting data. Please wait ..",'N');
			}
			else
			{//REPORT FOR MONTHLY PROCESSING
			//GENERATING TEMP. TABLE NAME AS : USRCD+HH+MM
			//	L_strTBLNM="TT_"+cl_dat.M_strUSRCD_pbst+cl_dat.M_txtCLKTM_pbst.getText().trim().substring(0,2)+cl_dat.M_txtCLKTM_pbst.getText().trim().substring(3,5);
				L_strTBLNM="TT_RPLGR";
			//CREATING TEMP. TABLE
				M_strSQLQRY="Create table "+L_strTBLNM
					+"  (	TT_STRTP varchar(2),"
					+"		TT_MATCD varchar(10),"
					+"		TT_DOCTP varchar(1),"
					+"		TT_DOCNO varchar(10),"
					+"		TT_DOCRF varchar(20),"
					+"		TT_DOCQT Decimal(12,3),"
					+"		TT_DOCVL Decimal(12,2),"
					+"		TT_DOCDT Date,"
					+"		constraint pk_"+L_strTBLNM+" primary key (TT_STRTP,TT_MATCD,TT_DOCTP,TT_DOCNO,TT_DOCRF))";
				cl_dat.M_flgLCUPD_pbst=true;
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				cl_dat.exeDBCMT("trfDATA");
			//TRANSFER DATA FROM LIVE TABLES TO TEMP. TABLE
				cl_dat.M_flgLCUPD_pbst=true;
				setMSG("Collecting GRIN data ..",'N');
				cl_dat.exeSQLUPD("Insert into "+L_strTBLNM+" (Select GR_STRTP,GR_MATCD,'1',GR_GRNNO,GR_BATNO,gr_ACPQT,gr_BILVL,GR_ACPDT from MM_GRMST where GR_ACPDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"'  and ifnull(GR_STSFL,'')='2')","");
				setMSG("Collecting MIN data ..",'N');
				cl_dat.exeSQLUPD("Insert into "+L_strTBLNM+" (Select IS_STRTP,IS_MATCD,'4',IS_ISSNO,IS_TAGNO,IS_ISSQT,IS_ISSVL,date(IS_AUTDT) from MM_ISMST where date(IS_AUTDT) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and ifnull(IS_STSFL,'')='2')","");
				setMSG("Collecting MRN data ..",'N');
				cl_dat.exeSQLUPD("Insert into "+L_strTBLNM+" (Select MR_STRTP,MR_MATCD,'2',MR_MRNNO,MR_MRNNO,MR_RETQT,MR_MRNVL,MR_MRNDT from MM_MRMST where MR_MRNDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and ifnull(MR_STSFL,'')='2')","");
				setMSG("Collecting SAN+ data ..",'N');
				cl_dat.exeSQLUPD("Insert into "+L_strTBLNM+" (Select SA_STRTP,SA_MATCD,'3',SA_SANNO,SA_SANNO,SA_SANQT,SA_SANVL,SA_SANDT  from MM_SAMST where SA_SANDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and SA_SANQT>0 and ifnull(SA_STSFL,'')<>'X')","");
				cl_dat.exeSQLUPD("Insert into "+L_strTBLNM+" (Select SA_STRTP,SA_MATCD,'5',SA_SANNO,SA_SANNO,SA_SANQT,SA_SANVL,SA_SANDT  from MM_SAMST where SA_SANDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and SA_SANQT<0 and ifnull(SA_STSFL,'')<>'X')","");
				setMSG("Formatting data. Please wait ..",'N');
				cl_dat.exeDBCMT("trfDATA");
			//COLLECTING DATA IN RESULT SETS
				L_rstRSSET=cl_dat.exeSQLQRY0("Select STP_MATCD MATCD, STP_MATDS MATDS, STP_MOSQT OPSQT, STP_MOSVL OPSVL, STP_MCSQT CLSQT, STP_MCSVL CLSVL from MM_STPRC where STP_strtp='01' "+(chbGRPAL.isSelected() ? "" : " and substr(STP_MATCD,1,2) not in('68','69','85','86') ")+" and STP_STRTP='"+M_strSBSCD.substring(2,4)+"' order by STP_MATCD");
				M_rstRSSET=cl_dat.exeSQLQRY0("Select TT_DOCNO DOCNO, TT_DOCDT DOCDT, TT_DOCTP DOCTP, TT_MATCD MATCD, TT_DOCQT DOCQT, TT_DOCVL DOCVL from "+L_strTBLNM+" where TT_strtp='01' "+(chbGRPAL.isSelected() ? "" : " and substr(TT_MATCD,1,2) not in('68','69','85','86') ")+" and TT_STRTP='"+M_strSBSCD.substring(2,4)+"' order by TT_MATCD,TT_DOCTP");
			}
				if(M_rstRSSET==null)
				{
					setMSG("No data found ..",'E');
					return false;
				}
				if(L_rstRSSET==null)
				{
					setMSG("No data found ..",'E');
					return false;
				}
				String[] L_staTRNTP=new String[]{"GRIN","MRN","+SAN","MIN","SAN"};//TO PRINT TRANSACTION TYPE WRT DOCTP
				String L_strBLANK="  ";
				prnHEADR();
				L_rstRSSET.next();
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATCD"),"")+"  :   ");
				dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATDS"),""));
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				crtNWLIN();
				dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Opening Balance : ",24));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSQT"),""),10));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSVL"),""),10));
				prnFMTCHR(dosREPORT,M_strCPI12);
				crtNWLIN();
				double L_dblYCSQT=0,L_dblYCSVL=0;//FOR YEAR CLOSING STOCK AND VALUE OF LAST MATERIAL CODE
				while(M_rstRSSET.next())
				{
					L_strBLANK="  ";
					if(M_intLINNO>50)
						prnHEADR();
					while( !M_rstRSSET.getString("MATCD").equals(L_rstRSSET.getString("MATCD")))
					{//DISPLAY CLOSING BALANCE FOR CURRENT AND OPENING BALANCE FOR NEXT MATCD
						prnFMTCHR(dosREPORT,M_strCPI10);
						dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Closing Balance : ",24));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("CLSQT"),""),20));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("CLSVL"),""),20));
						crtNWLIN();
						crtHRLIN(50,"- ");
						if(!L_rstRSSET.next())
							break;
						prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATCD"),"")+"  :   ");
						dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATDS"),""));
						crtNWLIN();
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Opening Balance : ",24));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSQT"),""),20));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSVL"),""),20));
						crtNWLIN();
						prnFMTCHR(dosREPORT,M_strCPI12);
						L_dblYCSQT=L_rstRSSET.getDouble("CLSQT");
						L_dblYCSVL=L_rstRSSET.getDouble("CLSVL");
					}
				//DISPLAY TRANSACTION DETAILS
					L_strBLANK="     ";
					dosREPORT.writeBytes(L_strBLANK+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("DOCDT"),""),10));
					dosREPORT.writeBytes(padSTRING('R',L_staTRNTP[M_rstRSSET.getInt("DOCTP")-1],6));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCNO"),""),10));
					if(M_rstRSSET.getFloat("DOCVL")>0.0f&&!(L_staTRNTP[M_rstRSSET.getInt("DOCTP")-1].equals("MIN")))
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCQT"),""),15));
					else
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCQT"),""),40));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCVL"),""),15));
					crtNWLIN();
				}
			//TOTAL FOR LAST MATERIAL CODE
				prnFMTCHR(dosREPORT,M_strCPI10);
				dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Closing Balance : ",24));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSQT,3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSVL,2),20));
				crtNWLIN();
				crtHRLIN(50,"- ");
				if(rdbYEAR.isSelected())
					setMSG("Report Completed ..",'N');
		}catch(Exception e)
		{setMSG(e,"Child.exePRINT");
		return false;}
		finally
		{
			if(rdbMONTH.isSelected() && L_strTBLNM != null)
			{
				try
				{//CLOSE RESULT SETS AND DELETE TEMP. TABLE IF CREATED
					M_rstRSSET.close();
					L_rstRSSET.close();
					cl_dat.exeSQLUPD("Drop table "+L_strTBLNM+" cascade","");
					setMSG("Report Completed ..",'N');
					cl_dat.exeDBCMT("");
				}catch(Exception e)
				{setMSG("Table "+L_strTBLNM+" not deleted ..",'E');}
				this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		return true;
	}
	/**
	 * Method to generate GROUP WISE TRANSACTION SUMMARY REPORT
	 * 
	 * Method to generate GROUP WISE TRANSACTION SUMMARY REPORT
	 * 
	 * <p>Construct qruery with same allias for monthly or yearly report and generate the report.
	 * 
	 */
	private boolean exeRPGTS()
	{
		try
		{
			double L_dblCLVAL=0.0,L_dblOPVAL=0.0,L_dblRCVAL=0.0,L_dblISVAL=0.0,L_dblCALVL =0.0,L_dblDIFVL =0.0;
			if(rdbYEAR.isSelected())
			{
				M_strSQLQRY="Select substring(YR_MATCD,1,2) YR_GRPCD,CT_MATDS,sum(YR_YOSVL),sum(YR_CRCVL)+sum(YR_CSAVL),sum(YR_CISVL)-sum(YR_CMRVL),sum(YR_ADJVL),sum(YR_YCSVL) from MM_YRPRC , CO_CTMST where  "
						+" substr(CT_MATCD,1,2)=substr(YR_MATCD,1,2) ";
					
			//  For imported items figure			
				if(chbIMPIT.isSelected())
					M_strSQLQRY +=" and substr(YR_MATCD,10,1) IN('2','6') ";
						
				M_strSQLQRY += (chbGRPAL.isSelected()? "" : " and SUBSTR(YR_MATCD,1,2) not in('68','69','85','86') " )
						+"and YR_STRTP ='01'"	
						+"and CT_CODTP='MG'  group by substring(yr_matcd,1,2),CT_MATDS order by substring(yr_matcd,1,2) ";//and yr_ycsqt<>st_stkqt
			}
			else
			{
				//M_strSQLQRY="Select substring(STP_MATCD,1,2),CT_MATDS,sum(STP_MOSVL),sum(STP_MCRCV-STP_MORCV+STP_MCSAV-STP_MOSAV),sum(STP_MCISV-STP_MOISV-(STP_MCMRV-STP_MOMRV)),0,sum(STP_MCSVL) from MM_STPRC, CO_CTMST where substring(CT_MATCD,1,2) = substring(STP_MATCD,1,2) and CT_CODTP='MG' group by substring(STP_MATCD,1,2),CT_MATDS order by substring(STP_MATCD,1,2)";
				M_strSQLQRY="Select substring(STP_MATCD,1,2),CT_MATDS,sum(ifnull(STP_MOSVL,0)),sum(ifnull(STP_MCRCV,0)-ifnull(STP_MORCV,0)+ifnull(STP_MCSAV,0)-ifnull(STP_MOSAV,0)),sum(ifnull(STP_MCISV,0)-ifnull(STP_MOISV,0)-(ifnull(STP_MCMRV,0)-ifnull(STP_MOMRV,0))),0,sum(ifnull(STP_MCSVL,0)) from MM_STPRC, CO_CTMST where substring(CT_MATCD,1,2) = substring(STP_MATCD,1,2) and CT_CODTP='MG' "+(chbGRPAL.isSelected() ? "" : " and substr(STP_MATCD,1,2) not in('68','69','85','86') ")+" and STP_STRTP='"+M_strSBSCD.substring(2,4)+"' group by substring(STP_MATCD,1,2),CT_MATDS order by substring(STP_MATCD,1,2)";
			}
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET==null)
			{
				setMSG("No data found ..",'E');
				return false;
			}
			double L_dblTOT1=0.0,L_dblTOT2=0.0,L_dblTOT3=0.0,L_dblTOT4=0.0,L_dblTOT5=0.0;
			prnHEADR();
			prnDIFFHD();
			while(M_rstRSSET.next())
			{
				if(M_intLINNO>66)
				{
					prnHEADR();
					prnDIFFHD();
				}
			 	dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString(1),"")+" "+nvlSTRVL(M_rstRSSET.getString(2),""),35));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(3),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(4)+M_rstRSSET.getDouble(6),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(5),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(7),0),17));
				
				//// for diff printing report mm_prdiff.doc
				L_dblOPVAL = Double.parseDouble(setNumberFormat(M_rstRSSET.getDouble(3),0));
                L_dblRCVAL = Double.parseDouble(setNumberFormat(M_rstRSSET.getDouble(4),0))+Double.parseDouble(setNumberFormat(M_rstRSSET.getDouble(6),0));
                L_dblISVAL = Double.parseDouble(setNumberFormat(M_rstRSSET.getDouble(5),0));
                L_dblCLVAL = Double.parseDouble(setNumberFormat(M_rstRSSET.getDouble(7),0));
				L_dblCALVL = L_dblOPVAL + L_dblRCVAL - L_dblISVAL;
				L_dblDIFVL = L_dblCALVL - L_dblCLVAL;
				dosDIFF.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString(1),"")+" "+nvlSTRVL(M_rstRSSET.getString(2),""),35));
				dosDIFF.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(3),0),15));
				dosDIFF.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(4)+M_rstRSSET.getDouble(6),0),15));
				dosDIFF.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(5),0),15));
				dosDIFF.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(7),0),15));

				if(L_dblDIFVL !=0)
				    dosDIFF.writeBytes(padSTRING('L',setNumberFormat(L_dblDIFVL,0),12));
				else    
				    dosDIFF.writeBytes(padSTRING('L',"-",12));
				dosDIFF.writeBytes("\n");
				if(M_intLINNO>66)
					prnDIFFHD();
				//// end diff printing report
				
				crtNWLIN();
				crtNWLIN();
				L_dblTOT1+=M_rstRSSET.getDouble(3);
				L_dblTOT2+=M_rstRSSET.getDouble(4)+M_rstRSSET.getDouble(6);
				L_dblTOT3+=M_rstRSSET.getDouble(5);
//				L_dblTOT4+=M_rstRSSET.getDouble(6);
				L_dblTOT5+=M_rstRSSET.getDouble(7);
				
				
			}
			crtNWLIN();
			crtHRLIN(106,"-");
			dosREPORT.writeBytes(padSTRING('L',"",35));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT1,0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT2,0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT3,0),"0.0"),17));
	//		dosREPORT.writeBytes(padSTRING('L',"",12));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT5,0),"0.0"),17));
			crtNWLIN();
			crtHRLIN(106,"-");
		}catch(Exception e)
		{setMSG(e,"Child.exeRPGTS");
		 setMSG("Error in report generation ..",'E');
		return false;}
		return true;
	}
	
	private boolean exeRPGTS1() // Method for displaying the groupwise trans summary category wise 
	{
		try
		{
			M_strSQLQRY = "Select CT_GRPCD,CT_MATDS from CO_CTMST WHERE CT_CODTP ='MG'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			Hashtable hstMATDS = new Hashtable();
			if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
					hstMATDS.put(M_rstRSSET.getString("CT_GRPCD"),nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
			if(rdbYEAR.isSelected())
			{
				M_strSQLQRY="Select substring(YR_MATCD,1,2) YR_GRPCD,YR_CATFL,sum(YR_YOSVL),sum(YR_CRCVL)+sum(YR_CSAVL),sum(YR_CISVL)-sum(YR_CMRVL),sum(YR_ADJVL),sum(YR_YCSVL) from MM_YRPRC where  YR_STRTP ='01'";
					//	+" substr(CT_MATCD,1,2)=substr(YR_MATCD,1,2) ";
					
			//  For imported items figure			
				if(chbIMPIT.isSelected())
					M_strSQLQRY +=" and substr(YR_MATCD,10,1) IN('2','6') ";
						
				M_strSQLQRY += (chbGRPAL.isSelected()? "" : " and SUBSTR(YR_MATCD,1,2) not in('68','69','85','86') " );
				M_strSQLQRY += " group by substring(yr_matcd,1,2),YR_CATFL order by substring(yr_matcd,1,2),YR_CATFL";
				//		+"and YR_STRTP ='01'"	
				//		+"and CT_CODTP='MG'  group by substring(yr_matcd,1,2),CT_MATDS order by substring(yr_matcd,1,2) ";//and yr_ycsqt<>st_stkqt
			}
			else
			{
				//M_strSQLQRY="Select substring(STP_MATCD,1,2),CT_MATDS,sum(STP_MOSVL),sum(STP_MCRCV-STP_MORCV+STP_MCSAV-STP_MOSAV),sum(STP_MCISV-STP_MOISV-(STP_MCMRV-STP_MOMRV)),0,sum(STP_MCSVL) from MM_STPRC, CO_CTMST where substring(CT_MATCD,1,2) = substring(STP_MATCD,1,2) and CT_CODTP='MG' group by substring(STP_MATCD,1,2),CT_MATDS order by substring(STP_MATCD,1,2)";
				M_strSQLQRY="Select substring(STP_MATCD,1,2),STP_CATFL,sum(ifnull(STP_MOSVL,0)),sum(ifnull(STP_MCRCV,0)-ifnull(STP_MORCV,0)+ifnull(STP_MCSAV,0)-ifnull(STP_MOSAV,0)),sum(ifnull(STP_MCISV,0)-ifnull(STP_MOISV,0)-(ifnull(STP_MCMRV,0)-ifnull(STP_MOMRV,0))),0,sum(ifnull(STP_MCSVL,0)) from MM_STPRC where "+" STP_STRTP='"+M_strSBSCD.substring(2,4)+"'"+(chbGRPAL.isSelected() ? "" : " and substr(STP_MATCD,1,2) not in('68','69','85','86') ")+" group by substring(STP_MATCD,1,2),STP_CATFL order by substring(STP_MATCD,1,2),STP_CATFL";
			}
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET==null)
			{
				setMSG("No data found ..",'E');
				return false;
			}
			double L_dblTOT1=0.0,L_dblTOT2=0.0,L_dblTOT3=0.0,L_dblTOT4=0.0,L_dblTOT5=0.0;
			double L_dblGTOT1=0.0,L_dblGTOT2=0.0,L_dblGTOT3=0.0,L_dblGTOT4=0.0,L_dblGTOT5=0.0;
			double L_dblCTOT[][] = new double[4][4];
			prnHEADR();
			String L_strGRPCD = "";
			String L_strCATCD = "";
			String L_strCATDS = "";
			String L_strPRGRP = "";
			while(M_rstRSSET.next())
			{
				if(M_intLINNO>66)
					prnHEADR();
				L_strGRPCD = M_rstRSSET.getString(1);
				L_strCATCD = M_rstRSSET.getString(2);
				if(L_strCATCD.equals("1"))
					L_strCATDS = "Stock Controlled";
				else if(L_strCATCD.equals("2"))
					L_strCATDS = "Insurance";
				else if(L_strCATCD.equals("3"))
					L_strCATDS = "Obsolete";
				else if(L_strCATCD.equals("4"))
					L_strCATDS = "Others";
				
				//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString(1),"")+" "+nvlSTRVL(M_rstRSSET.getString(2),""),35));
				if(!L_strGRPCD.equals(L_strPRGRP))
				{
					if(L_strPRGRP.length() >0)
					{
						dosREPORT.writeBytes(padSTRING('R',L_strPRGRP,4)+padSTRING('R',"Total",30));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT1,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT2,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT3,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT5,0),17));
						crtNWLIN();
						//crtNWLIN();
					}
					crtNWLIN();
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString(1),"")+" "+hstMATDS.get(nvlSTRVL(M_rstRSSET.getString(1),"")).toString(),35));
					crtNWLIN();
					L_strPRGRP = L_strGRPCD;
					L_dblGTOT1 =0;
					L_dblGTOT2 =0;
					L_dblGTOT3 =0;
					L_dblGTOT4 =0;
					L_dblGTOT5 =0;
				}
				dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',L_strCATDS,30));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(3),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(4)+M_rstRSSET.getDouble(6),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(5),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(7),0),17));
				crtNWLIN();
				L_dblTOT1+=M_rstRSSET.getDouble(3);
				L_dblTOT2+=M_rstRSSET.getDouble(4)+M_rstRSSET.getDouble(6);
				L_dblTOT3+=M_rstRSSET.getDouble(5);
				L_dblTOT5+=M_rstRSSET.getDouble(7);
				
				L_dblGTOT1+=M_rstRSSET.getDouble(3);
				L_dblGTOT2+=M_rstRSSET.getDouble(4)+M_rstRSSET.getDouble(6);
				L_dblGTOT3+=M_rstRSSET.getDouble(5);
				L_dblGTOT5+=M_rstRSSET.getDouble(7);
				
				// Categorywise  total
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][0]+=M_rstRSSET.getDouble(3);
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][1]+=M_rstRSSET.getDouble(4)+M_rstRSSET.getDouble(6);
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][2]+=M_rstRSSET.getDouble(5);
//				L_dblTOT4+=M_rstRSSET.getDouble(6);
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][3]+=M_rstRSSET.getDouble(7);
				
			}
			crtNWLIN();
			crtHRLIN(106,"-");
			dosREPORT.writeBytes(padSTRING('R',"Grand Total",35));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT1,0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT2,0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT3,0),"0.0"),17));
	//		dosREPORT.writeBytes(padSTRING('L',"",12));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT5,0),"0.0"),17));
			
			crtNWLIN();
			
			crtHRLIN(106,"-");
		//	dosREPORT.writeBytes(padSTRING('L',L_strCATCD,35));
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Stock Controlled",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][3],0),"0.0"),17));
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Insurance",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][3],0),"0.0"),17));
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Obsolete",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][3],0),"0.0"),17));
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Others",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][3],0),"0.0"),17));
			crtNWLIN();
			
			crtHRLIN(106,"-");
		}catch(Exception e)
		{setMSG(e,"Child.exeRPGTS");
		 setMSG("Error in report generation ..",'E');
		return false;}
		return true;
	}
	private boolean exeRPGTS2() // Method for displaying the groupwise trans summary category wise 
	{
		try
		{
			M_strSQLQRY = "Select CT_GRPCD,CT_MATDS from CO_CTMST WHERE CT_CODTP ='MG'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			Hashtable hstMATDS = new Hashtable();
			Hashtable hstDPTDS = new Hashtable();
			if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
					hstMATDS.put(M_rstRSSET.getString("CT_GRPCD"),nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' and ifnull(cmt_stsfl,'') <>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
					hstDPTDS.put(M_rstRSSET.getString("CMT_CODCD"),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
			hstDPTDS.put("X","Owner not known");
			if(rdbYEAR.isSelected())
			{
				M_strSQLQRY="Select YR_OWNBY,substring(YR_MATCD,1,2) YR_GRPCD,YR_CATFL,sum(YR_YOSVL),sum(YR_CRCVL)+sum(YR_CSAVL),sum(YR_CISVL)-sum(YR_CMRVL),sum(YR_ADJVL),sum(YR_YCSVL) from MM_YRPRC where  YR_STRTP ='01'";
				if(chbIMPIT.isSelected())	//  For imported items figure			
					M_strSQLQRY +=" and substr(YR_MATCD,10,1) IN('2','6') ";
				M_strSQLQRY += (chbGRPAL.isSelected()? "" : " and SUBSTR(YR_MATCD,1,2) not in('68','69','85','86') " );
				M_strSQLQRY += " group by YR_OWNBY,substring(yr_matcd,1,2),YR_CATFL order by YR_OWNBY,substring(yr_matcd,1,2),YR_CATFL";
			}
			else
			{
				M_strSQLQRY="Select STP_OWNBY,substring(STP_MATCD,1,2),STP_CATFL,sum(ifnull(STP_MOSVL,0)),sum(ifnull(STP_MCRCV,0)-ifnull(STP_MORCV,0)+ifnull(STP_MCSAV,0)-ifnull(STP_MOSAV,0)),sum(ifnull(STP_MCISV,0)-ifnull(STP_MOISV,0)-(ifnull(STP_MCMRV,0)-ifnull(STP_MOMRV,0))),0,sum(ifnull(STP_MCSVL,0)) from MM_STPRC where "+" STP_STRTP='"+M_strSBSCD.substring(2,4)+"'"+(chbGRPAL.isSelected() ? "" : " and substr(STP_MATCD,1,2) not in('68','69','85','86') ")+" group by STP_OWNBY,substring(STP_MATCD,1,2),STP_CATFL order by STP_OWNBY,substring(STP_MATCD,1,2),STP_CATFL";
			}
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET==null)
			{
				setMSG("No data found ..",'E');
				return false;
			}
			double L_dblTOT1=0.0,L_dblTOT2=0.0,L_dblTOT3=0.0,L_dblTOT4=0.0,L_dblTOT5=0.0;
			double L_dblGTOT1=0.0,L_dblGTOT2=0.0,L_dblGTOT3=0.0,L_dblGTOT4=0.0,L_dblGTOT5=0.0;
			double L_dblDTOT1=0.0,L_dblDTOT2=0.0,L_dblDTOT3=0.0,L_dblDTOT4=0.0,L_dblDTOT5=0.0;
			double L_dblCTOT[][] = new double[4][4]; // Grand total category wise
			double L_dblOCTOT[][] = new double[4][4]; // Owner total category wise
			prnHEADR();
			String L_strGRPCD = "";
			String L_strCATCD = "";
			String L_strCATDS = "";
			String L_strPRGRP = "";
			String L_strPRDPT = "";
			String L_strDPTCD = "";
			int L_intRECCT =0;
			while(M_rstRSSET.next())
			{
				if(M_intLINNO>66)
					prnHEADR();
				L_strDPTCD = M_rstRSSET.getString(1);
				L_strGRPCD = M_rstRSSET.getString(2);
				L_strCATCD = M_rstRSSET.getString(3);
				if(L_strCATCD.equals("1"))
					L_strCATDS = "Stock Controlled";
				else if(L_strCATCD.equals("2"))
					L_strCATDS = "Insurance";
				else if(L_strCATCD.equals("3"))
					L_strCATDS = "Obsolete";
				else if(L_strCATCD.equals("4"))
					L_strCATDS = "Others";
				if(!L_strDPTCD.equals(L_strPRDPT))
				{
					if(!L_strGRPCD.equals(L_strPRGRP))
					{
						if(L_strPRGRP.length() >0)
						{
							dosREPORT.writeBytes("  "+padSTRING('R',"Total",32));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT1,0),17));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT2,0),17));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT3,0),17));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT5,0),17));
							crtNWLIN();
						}
						crtNWLIN();
						L_strPRGRP = L_strGRPCD;
						L_dblGTOT1 =0;
						L_dblGTOT2 =0;
						L_dblGTOT3 =0;
						L_dblGTOT4 =0;
						L_dblGTOT5 =0;
					}
					if(L_strPRDPT.length() >0)
					{
						if(hstDPTDS.containsKey((String)L_strPRDPT))
							dosREPORT.writeBytes(padSTRING('R',"Total : "+hstDPTDS.get(L_strPRDPT).toString(),34));
						else
							dosREPORT.writeBytes(padSTRING('R',"Total : "+L_strPRDPT,34));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT1,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT2,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT3,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT5,0),17));
						crtNWLIN();
					// For ownership wise category total	
						crtHRLIN(106,"-");
						dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Stock Controlled",30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][0],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][1],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][2],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][3],0),"0.0"),17));
						crtNWLIN();
						dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Insurance",30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][0],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][1],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][2],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][3],0),"0.0"),17));
						crtNWLIN();
						dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Obsolete",30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][0],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][1],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][2],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][3],0),"0.0"),17));
						crtNWLIN();
						dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Others",30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][0],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][1],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][2],0),"0.0"),17));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][3],0),"0.0"),17));
						crtNWLIN();
						
						crtHRLIN(106,"-");
						if(L_intRECCT > 0)
							prnHEADR();
						L_dblOCTOT[0][0] =0;L_dblOCTOT[0][1] =0;L_dblOCTOT[0][2] =0;L_dblOCTOT[0][3] =0;
						L_dblOCTOT[1][0] =0;L_dblOCTOT[1][1] =0;L_dblOCTOT[1][2] =0;L_dblOCTOT[1][3] =0;
						L_dblOCTOT[2][0] =0;L_dblOCTOT[2][1] =0;L_dblOCTOT[2][2] =0;L_dblOCTOT[2][3] =0;
						L_dblOCTOT[3][0] =0;L_dblOCTOT[3][1] =0;L_dblOCTOT[3][2] =0;L_dblOCTOT[3][3] =0;
						//crtNWLIN();
					}
				//	crtNWLIN();
				//	dosREPORT.writeBytes("OWNER : "+nvlSTRVL(M_rstRSSET.getString(1),""));
					if(hstDPTDS.containsKey((String)L_strDPTCD))
						dosREPORT.writeBytes("OWNER : "+hstDPTDS.get(L_strDPTCD).toString());
					else
						dosREPORT.writeBytes("OWNER : "+nvlSTRVL(M_rstRSSET.getString(1),""));
					crtNWLIN();
					crtNWLIN();
					dosREPORT.writeBytes("  "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString(2),"")+" "+hstMATDS.get(nvlSTRVL(M_rstRSSET.getString(2),"")).toString(),35));
					crtNWLIN();
					L_strPRDPT = L_strDPTCD;
					L_dblDTOT1 =0;
					L_dblDTOT2 =0;
					L_dblDTOT3 =0;
					L_dblDTOT4 =0;
					L_dblDTOT5 =0;
				}
				if(!L_strGRPCD.equals(L_strPRGRP))
				{
					if(L_strPRGRP.length() >0)
					{
						dosREPORT.writeBytes("  "+padSTRING('R',"Total",32));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT1,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT2,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT3,0),17));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT5,0),17));
						crtNWLIN();
						//crtNWLIN();
					}
					crtNWLIN();
					dosREPORT.writeBytes("  "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString(2),"")+" "+hstMATDS.get(nvlSTRVL(M_rstRSSET.getString(2),"")).toString(),35));
					crtNWLIN();
					L_strPRGRP = L_strGRPCD;
					L_dblGTOT1 =0;
					L_dblGTOT2 =0;
					L_dblGTOT3 =0;
					L_dblGTOT4 =0;
					L_dblGTOT5 =0;
				}
				dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',L_strCATDS,30));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(4),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(5)+M_rstRSSET.getDouble(7),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(6),0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble(8),0),17));
				crtNWLIN();
				L_dblTOT1+=M_rstRSSET.getDouble(4);
				L_dblTOT2+=M_rstRSSET.getDouble(5)+M_rstRSSET.getDouble(7);
				L_dblTOT3+=M_rstRSSET.getDouble(6);
				L_dblTOT5+=M_rstRSSET.getDouble(8);
				
				L_dblDTOT1+=M_rstRSSET.getDouble(4);
				L_dblDTOT2+=M_rstRSSET.getDouble(5)+M_rstRSSET.getDouble(7);
				L_dblDTOT3+=M_rstRSSET.getDouble(6);
				L_dblDTOT5+=M_rstRSSET.getDouble(8);
				
				L_dblGTOT1+=M_rstRSSET.getDouble(4);
				L_dblGTOT2+=M_rstRSSET.getDouble(5)+M_rstRSSET.getDouble(7);
				L_dblGTOT3+=M_rstRSSET.getDouble(6);
				L_dblGTOT5+=M_rstRSSET.getDouble(8);
				
				// Categorywise  total
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][0]+=M_rstRSSET.getDouble(4);
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][1]+=M_rstRSSET.getDouble(5)+M_rstRSSET.getDouble(7);
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][2]+=M_rstRSSET.getDouble(6);
				L_dblCTOT[Integer.parseInt(L_strCATCD)-1][3]+=M_rstRSSET.getDouble(8);

				// Owner - Categorywise  total
				L_dblOCTOT[Integer.parseInt(L_strCATCD)-1][0]+=M_rstRSSET.getDouble(4);
				L_dblOCTOT[Integer.parseInt(L_strCATCD)-1][1]+=M_rstRSSET.getDouble(5)+M_rstRSSET.getDouble(7);
				L_dblOCTOT[Integer.parseInt(L_strCATCD)-1][2]+=M_rstRSSET.getDouble(6);
				L_dblOCTOT[Integer.parseInt(L_strCATCD)-1][3]+=M_rstRSSET.getDouble(8);

				L_intRECCT++;	
			}
			if(L_strPRGRP.length() >0)
			{
				dosREPORT.writeBytes("  "+padSTRING('R',"Total",32));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT1,0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT2,0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT3,0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOT5,0),17));
				crtNWLIN();
			}
			if(L_strPRDPT.length() >0)
			{
				if(hstDPTDS.containsKey((String)L_strPRDPT))
					dosREPORT.writeBytes(padSTRING('R',"Total : "+hstDPTDS.get(L_strPRDPT).toString(),34));
				else
					dosREPORT.writeBytes(padSTRING('R',"Total : "+L_strPRDPT,34));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT1,0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT2,0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT3,0),17));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTOT5,0),17));
				crtNWLIN();
			// For ownership wise category total	
				crtHRLIN(106,"-");
				dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Stock Controlled",30));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][0],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][1],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][2],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[0][3],0),"0.0"),17));
				crtNWLIN();
				dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Insurance",30));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][0],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][1],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][2],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[1][3],0),"0.0"),17));
				crtNWLIN();
				dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Obsolete",30));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][0],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][1],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][2],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[2][3],0),"0.0"),17));
				crtNWLIN();
				dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Others",30));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][0],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][1],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][2],0),"0.0"),17));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblOCTOT[3][3],0),"0.0"),17));
				crtNWLIN();
						
				crtHRLIN(106,"-");
				if(L_intRECCT > 0)
					prnHEADR();
				L_dblOCTOT[0][0] =0;L_dblOCTOT[0][1] =0;L_dblOCTOT[0][2] =0;L_dblOCTOT[0][3] =0;
				L_dblOCTOT[1][0] =0;L_dblOCTOT[1][1] =0;L_dblOCTOT[1][2] =0;L_dblOCTOT[1][3] =0;
				L_dblOCTOT[2][0] =0;L_dblOCTOT[2][1] =0;L_dblOCTOT[2][2] =0;L_dblOCTOT[2][3] =0;
				L_dblOCTOT[3][0] =0;L_dblOCTOT[3][1] =0;L_dblOCTOT[3][2] =0;L_dblOCTOT[3][3] =0;
				//crtNWLIN();
			}
            crtNWLIN();
			crtHRLIN(106,"-");
			dosREPORT.writeBytes(padSTRING('R',"Grand Total",35));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT1,0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT2,0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT3,0),"0.0"),17));
	//		dosREPORT.writeBytes(padSTRING('L',"",12));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblTOT5,0),"0.0"),17));
			
			crtNWLIN();
			crtHRLIN(106,"-");
		//	dosREPORT.writeBytes(padSTRING('L',L_strCATCD,35));
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Stock Controlled",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[0][3],0),"0.0"),17));
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Insurance",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[1][3],0),"0.0"),17));
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Obsolete",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[2][3],0),"0.0"),17));
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"Others",30));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][0],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][1],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][2],0),"0.0"),17));
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(L_dblCTOT[3][3],0),"0.0"),17));
			crtNWLIN();
			
			crtHRLIN(106,"-");
		}catch(Exception e)
		{setMSG(e,"Child.exeRPGTS");
		 setMSG("Error in report generation ..",'E');
		return false;}
		return true;
	}
	/**
	 * Method to generate GROUP WISE TRANSACTION DETAILS REPORT
	 * 
	 * Method to generate GROUP WISE TRANSACTION DETAILS REPORT
	 * 
	 * <p>Construct qruery with same allias for monthly or yearly report and generate the report along with totalised figures.
	 */
	private boolean exeRPGTD()
	{
		try
		{
			double	fltYOSVL = 0,
					fltCMRVL = 0,
					fltADJVL = 0,
					fltYCSVL = 0,
					fltCRCVL = 0,
					fltCSAVL = 0,
					fltCISVL = 0;
			if(rdbYEAR.isSelected())
			{
				M_strSQLQRY = "SELECT YR_STRTP,SUBSTR(YR_MATCD,1,2)MATCD,CT_MATDS,SUM(IFNULL(YR_YOSVL,0)) YOSVL,"
					+"SUM(IFNULL(YR_CRCVL,0)) CRCVL ,SUM(IFNULL(YR_CMRVL,0)) CMRVL ,"
					+"SUM(IFNULL(YR_CSAVL,0)) CSAVL ,SUM(IFNULL(YR_ADJVL,0)) ADJVL ,"
					+"SUM(IFNULL(YR_CISVL,0)) CISVL ,SUM(IFNULL(YR_YCSVL,0)) YCSVL "
					+"FROM MM_YRPRC,CO_CTMST WHERE CT_GRPCD = SUBSTR(YR_MATCD,1,2) "
					+(chbGRPAL.isSelected() ? "" : " and SUBSTR(YR_MATCD,1,2) not in('68','69','85','86') " )
					+" AND CT_CODTP = 'MG' "
					+"AND YR_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
					+"GROUP BY YR_STRTP,SUBSTR(YR_MATCD,1,2),CT_MATDS ORDER BY SUBSTR(YR_MATCD,1,2) ";
			}
			else
			{
				M_strSQLQRY="Select STP_STRTP,substring(STP_MATCD,1,2) MATCD,CT_MATDS,sum(ifnull(STP_MOSVL,0)) YOSVL,"
							+" sum(ifnull(STP_MCRCV,0)-ifnull(STP_MORCV,0)) CRCVL,sum(ifnull(STP_MCMRV,0)-ifnull(STP_MOMRV,0)) CMRVL,"
							+" sum(ifnull(STP_MCSAV,0)-ifnull(STP_MOSAV,0)) CSAVL,0 ADJVL,"
							+" sum(ifnull(STP_MCISV,0)-ifnull(STP_MOISV,0)) CISVL,sum(ifnull(STP_MCSVL,0)) YCSVL from MM_STPRC, CO_CTMST where substring(CT_MATCD,1,2) = substring(STP_MATCD,1,2) and CT_CODTP='MG'  "+(chbGRPAL.isSelected() ? "" : " and substr(STP_MATCD,1,2) not in('68','69','85','86') ")+" and STP_STRTP='"+M_strSBSCD.substring(2,4)+"' group by substring(STP_MATCD,1,2),CT_MATDS,STP_STRTP order by substring(STP_MATCD,1,2)";
			}
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET==null)
			{
				setMSG("No data found ..",'E');
				return false;
			}
				prnHEADR();
				while(M_rstRSSET.next())
				{
					if(M_intLINNO >56)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
						M_intLINNO = 0;
//						M_intPAGNO += 1;
//						prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADR();
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MATCD"),""),5));
					dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YOSVL"),0),31));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CMRVL"),0),22));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CISVL"),0),22));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YCSVL"),0),22));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CRCVL"),0),31));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CSAVL")+M_rstRSSET.getDouble("ADJVL"),0),22));
//					dosREPORT.writeBytes(padSTRING('L',"",22));
					dosREPORT.writeBytes("\n");
					M_intLINNO += 4;
					
					fltYOSVL = fltYOSVL + M_rstRSSET.getDouble("YOSVL");
					fltCMRVL = fltCMRVL + M_rstRSSET.getDouble("CMRVL");
					fltADJVL = fltADJVL + M_rstRSSET.getDouble("ADJVL");
					fltYCSVL = fltYCSVL + M_rstRSSET.getDouble("YCSVL");
					fltCRCVL = fltCRCVL + M_rstRSSET.getDouble("CRCVL");
					fltCSAVL = fltCSAVL + M_rstRSSET.getDouble("CSAVL") + M_rstRSSET.getDouble("ADJVL");
					fltCISVL = fltCISVL + M_rstRSSET.getDouble("CISVL");
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(fltYOSVL,0),31));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(fltCMRVL,0),22));
				dosREPORT.writeBytes(padSTRING('L',"",22));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(fltYCSVL,0),22));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(fltCRCVL,0),31));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(fltCSAVL,0),22));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(fltCISVL,0),22));
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------");
		}catch(Exception e)
		{setMSG(e,"Child.exeRPDTD");
		setMSG("Error in report generation .. ",'E');
		return false;}
		return true;
	}
	
	/**
	 * Method to generate STOCK / VALUE DETAILS REPORT
	 * 
	 * Method to generate STOCK / VALUE DETAILS REPORT
	 * 
	 * <p>Construct qruery with same allias for material code wise monthly or yearly report and generate the report along with totalised figures for groupwise values.
	 */
	private boolean exeRPSVS()
	{
		try
		{
			
			if(rdbYEAR.isSelected())
			{
				M_strSQLQRY="Select YR_MATCD MATCD,CT_MATDS MATDS,YR_UOMCD UOMCD,YR_YOSVL YOSVL,YR_YOSQT YOSQT,YR_CRCQT+YR_CSAQT CRCQT,YR_CRCVL+YR_CSAVL CRCVL,YR_CISVL-YR_CMRVL CISVL,YR_CISQT-YR_CMRQT CISQT,YR_YCSVL YCSVL, YR_YCSQT YCSQT, YR_ADJVL ADJVL from MM_YRPRC , CO_CTMST where  YR_STRTP='"+M_strSBSCD.substring(2,4)+"' and CT_MATCD=YR_MATCD "+(chbGRPAL.isSelected() ? "" : " and substr(YR_MATCD,1,2) not in('68','69','85','86') ")+" and CT_CODTP='CD'  order by yr_matcd ";//and yr_ycsqt<>st_stkqt
			}
			else
			{
				M_strSQLQRY="Select STP_MATCD MATCD,STP_MATDS MATDS,STP_UOMCD UOMCD,STP_MOSVL YOSVL,STP_MOSQT YOSQT,(STP_MCRCQ-STP_MORCQ)+(STP_MCSAQ-STP_MOSAQ) CRCQT,(STP_MCRCV-STP_MORCV)+(STP_MCSAV-STP_MOSAV) CRCVL,(STP_MCISV-STP_MOISV)-(STP_MCMRV-STP_MOMRV) CISVL,(STP_MCISQ-STP_MOISQ)-(STP_MCMRQ-STP_MOMRQ) CISQT,STP_MCSVL YCSVL, STP_MCSQT YCSQT, 0 ADJVL from MM_STPRC  where  STP_STRTP='"+M_strSBSCD.substring(2,4)+"' "+(chbGRPAL.isSelected() ? "" : " and substr(STP_MATCD,1,2) not in('68','69','85','86') ")+" order by STP_matcd ";//and yr_ycsqt<>st_stkqt
			}
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET==null)
			{
				setMSG("No data found ..",'E');
				return false;
			}
			double L_dblYOSVL=0.0,L_dblYCSVL=0.0,L_dblCRCVL=0.0,L_dblCISVL=0.0,L_dblADJVL=0.0;
			while(M_rstRSSET.next())
			{
				if(strMGPCD==null)
				{
					strMGPCD=M_rstRSSET.getString("MATCD").substring(0,2);
					prnHEADR();
				}
				if(M_intLINNO>66)
					prnHEADR();
				if(!strMGPCD.equals(M_rstRSSET.getString("MATCD").substring(0,2)))
				{
					dosREPORT.writeBytes(padSTRING('R',"Group wise Total Value : ",25));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYOSVL,3),20));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCRCVL+L_dblADJVL,3),20));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCISVL,3),20));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSVL,3),20));
					L_dblYOSVL=0.0;L_dblYCSVL=0.0;L_dblCRCVL=0.0;L_dblCISVL=0.0;
					crtNWLIN();
					strMGPCD=M_rstRSSET.getString("MATCD").substring(0,2);
					prnHEADR();
					dosREPORT.flush();
				}
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MATCD"),""),20));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("UOMCD"),""),5));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YOSQT"),3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CRCQT"),3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CISQT"),3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YCSQT"),3),20));
				crtNWLIN();
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MATDS"),""),25));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YOSVL"),3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CRCVL")+M_rstRSSET.getDouble("ADJVL"),3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("CISVL"),3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YCSVL"),3),20));
				crtNWLIN();
				crtNWLIN();
				L_dblYOSVL+=M_rstRSSET.getDouble("YOSVL");
				L_dblCRCVL+=M_rstRSSET.getDouble("CRCVL")+M_rstRSSET.getDouble("ADJVL");
				L_dblCISVL+=M_rstRSSET.getDouble("CISVL");
				L_dblYCSVL+=M_rstRSSET.getDouble("YCSVL");
				strMGPCD=M_rstRSSET.getString("MATCD").substring(0,2);
			}
			
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"Group wise Total Value : ",25));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYOSVL,3),20));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCRCVL+L_dblADJVL,3),20));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCISVL,3),20));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSVL,3),20));
			crtNWLIN();
			crtHRLIN(106,"-");
		}catch(Exception e)
		{setMSG(e,"Child.exeRPGTS");
		setMSG("Error in report generation .. ",'E');
		return false;}
		return true;
	}
/**
 Required by stores on 13/06/2005 for audit purpose
*/
	private boolean exeRPSVS1()
	{
		try
		{
			
			if(rdbYEAR.isSelected())
			{
				M_strSQLQRY="Select YR_MATCD MATCD,CT_MATDS MATDS,YR_UOMCD UOMCD,YR_YCSVL YCSVL, YR_YCSQT YCSQT,(stp_ycsvl/stp_ycsqt) YCLRT  from MM_YRPRC , CO_CTMST where  YR_STRTP='"+M_strSBSCD.substring(2,4)+"' and CT_MATCD=YR_MATCD "+(chbGRPAL.isSelected() ? "" : " and substr(YR_MATCD,1,2) not in('68','69','85','86') ")+" and CT_CODTP='CD'  order by yr_matcd ";//and yr_ycsqt<>st_stkqt
			}
			else
			{
				M_strSQLQRY="Select STP_MATCD MATCD,STP_MATDS MATDS,STP_UOMCD UOMCD,STP_MCSVL YCSVL, STP_MCSQT YCSQT,(stp_mcsvl/stp_mcsqt) YCLRT from MM_STPRC  where  STP_STRTP='"+M_strSBSCD.substring(2,4)+"' "+(chbGRPAL.isSelected() ? "" : " and substr(STP_MATCD,1,2) not in('68','69','85','86') ")+"and ifnull(stp_mcsqt,0) > 0  order by STP_matcd ";//and yr_ycsqt<>st_stkqt
			}
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET==null)
			{
				setMSG("No data found ..",'E');
				return false;
			}
			double L_dblYCSVL=0.0;
			while(M_rstRSSET.next())
			{
				if(strMGPCD==null)
				{
					strMGPCD=M_rstRSSET.getString("MATCD").substring(0,2);
					prnHEADR();
				}
				if(M_intLINNO>66)
					prnHEADR();
				if(!strMGPCD.equals(M_rstRSSET.getString("MATCD").substring(0,2)))
				{
					dosREPORT.writeBytes(padSTRING('R',"Group wise Total Value : ",77));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSVL,2),15));
					L_dblYCSVL=0.0;
					crtNWLIN();
					strMGPCD=M_rstRSSET.getString("MATCD").substring(0,2);
					prnHEADR();
					dosREPORT.flush();
				}
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MATCD"),""),12));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("UOMCD"),""),5));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MATDS"),""),45));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YCSQT"),3),15));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YCSVL"),2),15));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("YCLRT"),2),15));
				crtNWLIN();
				L_dblYCSVL+=M_rstRSSET.getDouble("YCSVL");
				strMGPCD=M_rstRSSET.getString("MATCD").substring(0,2);
			}
			
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"Group wise Total Value : ",77));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSVL,2),15));
			crtNWLIN();
			crtHRLIN(106,"-");
		}catch(Exception e)
		{setMSG(e,"Child.exeRPGTS");
		setMSG("Error in report generation .. ",'E');
		return false;}
		return true;
	}
	/** To print new line character and increament line counter	 */
	private void crtNWLIN() throws Exception
	{
		dosREPORT.writeBytes("\n");
		M_intLINNO++;
	}
	/** To print horizontal line	 */
	private void crtHRLIN(int P_intLINCT,String P_strLINCH) throws Exception
	{
		for(int i=0;i<P_intLINCT;i++)
			dosREPORT.writeBytes(P_strLINCH);
		crtNWLIN();
	}
	/** To print reportpage header depending on cmbRPTOP selection and strRPTTL	 */
	private void prnHEADR() throws Exception
	{
		if(M_intPAGNO>0)
			prnFMTCHR(dosREPORT,M_strEJT);
		M_intLINNO=0;
		prnFMTCHR(dosREPORT,M_strCPI10);
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LIMITED.",50)+padSTRING('L',"Date : "+cl_dat.M_txtCLKDT_pbst.getText(),38));
		crtNWLIN();
		dosREPORT.writeBytes(strRPTTL);
		crtNWLIN();
		dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),50)+padSTRING('L',"Page No. : "+Integer.toString(++M_intPAGNO),38));
		crtNWLIN();
		crtHRLIN(106,"-");
		prnFMTCHR(dosREPORT,M_strCPI12);
		if(cmbRPTOP.getITMCD().equals("0"))
		{
			dosREPORT.writeBytes(padSTRING('R',"Trn. Date",10));
			dosREPORT.writeBytes(padSTRING('R',"Type",6));
			dosREPORT.writeBytes(padSTRING('R',"Ref. No.",10));
			dosREPORT.writeBytes(padSTRING('R',"Qty.",15));
			dosREPORT.writeBytes(padSTRING('R',"Value",15));
			dosREPORT.writeBytes(padSTRING('R',"Qty.",15));
			dosREPORT.writeBytes(padSTRING('R',"Value",15));
			crtNWLIN();
			crtHRLIN(106,"-");
		}
		else if(cmbRPTOP.getITMCD().equals("1"))
		{
			dosREPORT.writeBytes(padSTRING('R',"Group Code/Description",35));
			dosREPORT.writeBytes(padSTRING('R',"Opening Value",17));
			dosREPORT.writeBytes(padSTRING('R',"Receipt Value",17));
			dosREPORT.writeBytes(padSTRING('R',"Issue Value",17));
			dosREPORT.writeBytes(padSTRING('R',"Closing Value",17));
			crtNWLIN();
			crtHRLIN(106,"-");
		}
		else if(cmbRPTOP.getITMCD().equals("2"))
		{
			dosREPORT.writeBytes("Mat. Group        Opening Value             MRN Value           Issue Value         Closing Value");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("                  Receipt Value             SAN Value           ");
			crtNWLIN();
			crtHRLIN(106,"-");
			crtNWLIN();
		}
		else if(cmbRPTOP.getITMCD().equals("3"))
		{
			dosREPORT.writeBytes(padSTRING('R',"Material Code",20));
			dosREPORT.writeBytes(padSTRING('R',"UOM",5));
			dosREPORT.writeBytes(padSTRING('L',"Opening Stock",20));
			dosREPORT.writeBytes(padSTRING('L',"Receipt Qty",20));
			dosREPORT.writeBytes(padSTRING('L',"Issue Qty",20));
			dosREPORT.writeBytes(padSTRING('L',"Closing Stock",20));
			crtNWLIN();
			dosREPORT.writeBytes(padSTRING('R',"Description",25));
			dosREPORT.writeBytes(padSTRING('L',"Value",20));
			dosREPORT.writeBytes(padSTRING('L',"Value",20));
			dosREPORT.writeBytes(padSTRING('L',"Value",20));
			dosREPORT.writeBytes(padSTRING('L',"Value",20));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			crtNWLIN();
			crtHRLIN(106,"-");
			dosREPORT.writeBytes("Material Group : "+strMGPCD+" "+hstMGPDS.get(strMGPCD));
			crtNWLIN();crtNWLIN();
		}
		else if((cmbRPTOP.getITMCD().equals("5"))||(cmbRPTOP.getITMCD().equals("6")))
		{
			dosREPORT.writeBytes(padSTRING('R',"Group Code/Description",35));
			dosREPORT.writeBytes(padSTRING('R',"Opening Value",17));
			dosREPORT.writeBytes(padSTRING('R',"Receipt Value",17));
			dosREPORT.writeBytes(padSTRING('R',"Issue Value",17));
			dosREPORT.writeBytes(padSTRING('R',"Closing Value",17));
			crtNWLIN();
			crtHRLIN(106,"-");
		}
		else if(cmbRPTOP.getITMCD().equals("7"))
		{
		    dosREPORT.writeBytes(padSTRING('R',"Item Code",12));
			dosREPORT.writeBytes(padSTRING('R',"UOM",5));
			dosREPORT.writeBytes(padSTRING('R',"Material Description",45));
			dosREPORT.writeBytes(padSTRING('L',"Closing Stock",15));
			dosREPORT.writeBytes(padSTRING('L',"Closing Value",15));
			dosREPORT.writeBytes(padSTRING('L',"Closing Rate",15));
			crtNWLIN();
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			crtNWLIN();
			crtHRLIN(106,"-");
			dosREPORT.writeBytes("Material Group : "+strMGPCD+" "+hstMGPDS.get(strMGPCD));
			crtNWLIN();crtNWLIN();
		}
	}
private void prnDIFFHD() throws Exception
	{
		if(M_intPAGNO>0)
			prnFMTCHR(dosDIFF,M_strEJT);
		//M_intLINNO=0;
		prnFMTCHR(dosDIFF,M_strCPI10);
		prnFMTCHR(dosDIFF,M_strBOLD);
		dosDIFF.writeBytes(padSTRING('R',"SUPREME PETROCHEM LIMITED.",50)+padSTRING('L',"Date : "+cl_dat.M_txtCLKDT_pbst.getText(),38));
		dosDIFF.writeBytes("\n");
		dosDIFF.writeBytes(strRPTTL);
		dosDIFF.writeBytes("\n");
		dosDIFF.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),50)+padSTRING('L',"Page No. : "+Integer.toString(++M_intPAGNO),38));
		dosDIFF.writeBytes("\n");
	    for(int i=0;i<106;i++)
			dosDIFF.writeBytes("-");
		dosDIFF.writeBytes("\n");
		prnFMTCHR(dosREPORT,M_strCPI12);
		dosDIFF.writeBytes(padSTRING('R',"Group Code/Description",35));
		dosDIFF.writeBytes(padSTRING('R',"Opening Value",15));
		dosDIFF.writeBytes(padSTRING('R',"Receipt Value",15));
		dosDIFF.writeBytes(padSTRING('R',"Issue Value",15));
		dosDIFF.writeBytes(padSTRING('R',"Closing Value",15));
		dosDIFF.writeBytes(padSTRING('R',"Difference",12));
		dosDIFF.writeBytes("\n");
	    for(int i=0;i<106;i++)
			dosDIFF.writeBytes("-");
		dosDIFF.writeBytes("\n");
	}
	void clrCOMP()
	{
		super.clrCOMP();
		if(prcREPORT!=null)
			prcREPORT.destroy();
	}
	private boolean crtYRDBF()
	{
		try
		{
			int i=0,j=0;
			java.sql.ResultSet L_rstRSSET ;
			M_strSQLQRY ="SELECT PS_STRTP,PS_DOCNO,PS_DOCDT,PS_MATCD,PS_DOCQT,PS_DOCRT,PS_DOCVL,PS_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_PSLYR,MM_GRMST,MM_STMST WHERE PS_DOCTP ='1' "+
					//	" AND PS_DOCDT BETWEEN '07/01/2003' AND '06/30/2004' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						 " AND PS_DOCDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						" AND PS_STRTP = GR_STRTP AND PS_DOCNO = GR_GRNNO AND PS_MATCD = GR_MATCD AND PS_STRTP ='01' and substr(ps_matcd,1,2) not in('68','69','85','86') ";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null){
				
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into yr_grdt values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_DOCNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("PS_DOCDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_MODVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}
				
			i=0;
			j=0;
			
			M_strSQLQRY ="SELECT GR_STRTP,GR_GRNNO,GR_ACPDT,GR_MATCD,GR_ACPQT,po_porRT,GR_PORVL,GR_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_GRMST,mm_pomst,MM_STMST WHERE gr_acpqt > 0 "+
					//	" AND gr_ACPDT BETWEEN '07/01/2003' AND '06/30/2004' AND gr_strtp = po_strtp and gr_porno = po_porno and gr_matcd = po_matcd and ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						" AND gr_ACPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND gr_strtp = po_strtp and gr_porno = po_porno and gr_matcd = po_matcd and ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						 " AND substr(gr_matcd,1,2) not in('68','69','85','86') and ifnull(gr_bilvl,0) =0";
						
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null){
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into po_grdt values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_grnNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("po_porRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_PORVL"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}
		M_strSQLQRY ="SELECT PS_STRTP,PS_DOCNO,PS_DOCDT,PS_MATCD,PS_DOCQT,PS_DOCRT,PS_DOCVL,PS_MODVL,"+
				        "ST_MATDS FROM MM_PSLYR,MM_STMST WHERE PS_DOCTP in('3','5') "+
					//	" AND PS_DOCDT BETWEEN '07/01/2003' AND '06/30/2004' AND ST_STRTP ='01'"+
					 	" AND PS_DOCDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND ST_STRTP ='01'"+
						" AND PS_STRTP = st_STRTP AND pS_MATCD = st_MATCD AND PS_STRTP ='01' and substr(ps_matcd,1,2) not in('68','69','85','86') ";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null){
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into yr_smdt values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_DOCNO"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_MATCD"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("PS_DOCDT"))))+"'),";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCRT"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					try
					{
						if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
						{
							i++;
						}
						else
						{
							j++;
							System.out.println("insert failed" +M_strSQLQRY);
						}
					}
					catch(Exception L_E)
					{
						setMSG(L_E,"inner catch");
						System.out.println("inner catch");
						System.out.println(M_strSQLQRY);
					}
						setMSG("saved "+i+" not saved "+j,'N');
						conSPBKA.commit();
				}
				
			}
			cl_dat.M_flgLCUPD_pbst = true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
			return false;
		}
		return true;
	}
	private boolean crtMNDBF()
	{
		try
		{
			int i=0,j=0;
			java.sql.ResultSet L_rstRSSET ;
			M_strSQLQRY ="SELECT GR_STRTP,GR_GRNNO,GR_ACPDT,GR_MATCD,GR_ACPQT,GR_GRNRT,GR_BILVL,GR_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_GRMST,MM_STMST WHERE "+
						" GR_ACPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
					//	" and substr(GR_matcd,1,2) not in('68','69','85','86') AND GR_ACPDT IS NOT NULL";
						" AND GR_ACPDT IS NOT NULL";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			setMSG("Genearating dbf psl_gr01",'N');
			if (M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_gr01 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_GRNRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0")+",";
				//	M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
				M_rstRSSET.close();
			}
			if(i==0)
				setMSG("No data is inserted in psl_gr01",'E');
			i=0;
			j=0;
			M_strSQLQRY ="SELECT GR_STRTP,GR_GRNNO,GR_ACPDT,GR_MATCD,GR_ACPQT,GR_GRNRT,GR_BILVL,GR_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_GRMST,MM_STMST WHERE "+
						" GR_ACPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD "+
						" and substr(GR_matcd,1,2) in('68','69') AND GR_ACPDT IS NOT NULL";
			setMSG("Genearating dbf psl_gr07",'N');
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_gr07 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_GRNRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					if(i==0)
						setMSG("No data is inserted in psl_gr07",'E');
					conSPBKA.commit();
				}
				M_rstRSSET.close();
			}
			i=0;
			j=0;
			M_strSQLQRY ="SELECT IS_STRTP,IS_ISSNO,DATE(IS_AUTDT)L_ISSDT,IS_MATCD,ST_MATDS,ST_UOMCD,IS_ISSQT FROM MM_ISMST,MM_STMST WHERE "+
						" DATE(IS_AUTDT) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND ST_STRTP = IS_STRTP AND ST_MATCD = IS_MATCD AND ST_STRTP ='01'"+
					//	" and substr(IS_matcd,1,2) not in('68','69','85','86') AND IS_AUTDT IS NOT NULL";
						 	"  AND IS_AUTDT IS NOT NULL";
			setMSG("Genearating dbf psl_is01",'N');
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_IS01 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("L_ISSDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_uomcd"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),"0")+")";
					
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					if(i==0)
						setMSG("No data is inserted in psl_is01",'E');
					conSPBKA.commit();
				}
				M_rstRSSET.close();	
			}
			cl_dat.M_flgLCUPD_pbst = true;
			i=0;
			j=0;
			M_strSQLQRY ="SELECT IS_STRTP,IS_ISSNO,DATE(IS_AUTDT)L_ISSDT,IS_MATCD,ST_MATDS,ST_UOMCD,IS_ISSQT FROM MM_ISMST,MM_STMST WHERE "+
						" DATE(IS_AUTDT) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND ST_STRTP = IS_STRTP AND ST_MATCD = IS_MATCD "+
						" and substr(IS_matcd,1,2) in('68','69') AND IS_AUTDT IS NOT NULL";
			setMSG("Genearating dbf psl_is07",'N');
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_IS07 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("L_ISSDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_uomcd"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),"0")+")";
					
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					if(i==0)
						setMSG("No data is inserted in psl_gr07",'E');
					conSPBKA.commit();
				}
				M_rstRSSET.close();
			}
////    TANKFARM DBF's
			cl_dat.M_flgLCUPD_pbst = true;
			i=0;
			j=0;
			M_strSQLQRY =	" SELECT IS_STRTP,IS_ISSTP,IS_ISSNO,IS_ISSDT,IS_MATCD,CT_MATDS,IS_ISSQT "
						+" FROM MM_ISMST,CO_CTMST where CT_MATCD = IS_MATCD AND IS_STRTP ='04'"
						+" and IS_ISSDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText())) + "' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"'" 
						+" AND ifnull(IS_STSFL,'') <>'X'";
			setMSG("Genearating dbf mm_issdt",'N');
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into mm_issdt(IS_STRTP,IS_ISSTP,IS_ISSNO,IS_ISSDT,IS_MATCD,IS_MATDS,IS_ISSQT)"
								   +"values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_ISSTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("IS_ISSDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("ct_matds"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),"0")+")";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					if(i==0)
						setMSG("No data is inserted in psl_issdt",'E');
					conSPBKA.commit();
				}
				M_rstRSSET.close();
			}
			i=0;
			j=0;
			M_strSQLQRY =  " SELECT GR_STRTP,GR_GRNTP,GR_GRNNO,GR_GRNDT,GR_MATCD,CT_MATDS,"
						   +"GR_TRNCD,GR_TRNNM,GR_VENCD,GR_VENNM,GR_ACPQT "
						   +"FROM MM_GRMST,CO_CTMSt where CT_MATCD = GR_MATCD "
						   +" AND GR_STRTP ='04' and GR_GRNDT BETWEEN '"
						   +M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText())) + "' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"'" 
						   +" AND ifnull(GR_STSFL,'')<>'X'";
			setMSG("Genearating dbf mm_grndt",'N');
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into mm_grndt(gr_strtp,gr_grntp,gr_grnno,gr_grndt,gr_matcd,gr_matds,"
								  +"gr_tprcd,gr_tprds,gr_vencd,gr_vends,gr_netqt)values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNTP"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_GRNDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("ct_matds"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_TRNCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_TRNNM"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD")," ")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM")," ")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+")";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					if(i==0)
						setMSG("No data is inserted in psl_grndt",'E');
					conSPBKA.commit();
				}
				M_rstRSSET.close();
			}
		///
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
			return false;
		}
		return true;
	}
	public Connection setCONFTB(String LP_PTHWD){
		String L_URLSTR ="";
        L_URLSTR = "jdbc:odbc:Visual FoxPro Tables;SourceDB = " + LP_PTHWD;
		try{
			System.out.println("conftb");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            conSPBKA = DriverManager.getConnection(L_URLSTR,"","");
			System.out.println(" connection "+conSPBKA);
            stmSTBK1 = conSPBKA.createStatement();
			System.out.println(" Statement created..."+stmSTBK1);
		}
		catch(ClassNotFoundException L_CNFE){
			System.out.println("Connectiob not found");
		}
		catch(SQLException L_SQLE){
			System.out.print("Database not found "+M_strSQLQRY);
		}
		return conSPBKA;
	}

private boolean crtTFDBF() // For B/E/ wise advance licence receipt
{
	try
	{
		int i=0,j=0;
		java.sql.ResultSet L_rstRSSET ;
		M_strSQLQRY = "SELECT GR_GRNTP,GR_BOENO,BE_BOEDT,BE_BOEQT,GR_GRNNO,GR_GRNDT,GR_MATCD,'',"
					 +"GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_CHLQT,GR_acpQT,(GR_ACPQT-GR_CHLQT) DIFQT "
					 +" FROM MM_GRMST,MM_BETRN WHERE GR_BOENO = BE_BOENO AND BE_MATTP ='03' "
			         +" AND GR_GRNDT BETWEEN '07/01/2004' AND '06/30/2005' ORDER BY GR_BOENO,GR_MATCD";
		/*M_strSQLQRY ="SELECT GR_GRNTP,GR_BOENO,BE_BOEDT,BE_BOEQT,GR_GRNNO,GR_GRNDT,GR_MATCD,"
					 +"GR_MATDS,GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_CHLQT,GR_NETQT,(GR_netqt-GR_CHLQT)l_difqt"+
			        " FROM MM_GRMST,MM_BETRN WHERE "+
					" GR_ACPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
					" and substr(GR_matcd,1,2) not in('68','69','85','86') AND GR_ACPDT IS NOT NULL";*/
		System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		cl_dat.M_flgLCUPD_pbst = true;
		if (M_rstRSSET != null){
				
			System.out.println("not null");
			while(M_rstRSSET.next())
			{
				//System.out.println("while");
				M_strSQLQRY =  "Insert into adv_rct values(";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNTP"),"")+"',";					
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_BOENO"),"")+"',";
				M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("BE_BOEDT"))))+"'),";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("BE_BOEQT"),"0")+",";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")+"',";
				M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_GRNDT"))))+"'),";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
				M_strSQLQRY += "' ',";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_LRYNO"),"")+"',";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"")+"',";
				M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_CHLDT"))))+"'),";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0")+",";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("DIFQT"),"0")+")";
				//System.out.println(M_strSQLQRY);
				if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
				{
					i++;
				}
				else
				{
					j++;
					System.out.println("insert failed");
				}
				setMSG("saved "+i+" not saved "+j,'N');
			//	System.out.println("i="+i);
				conSPBKA.commit();
			}
		}
	}
	catch(Exception L_E)
	{
		System.out.println(L_E.toString());
		return false;
	}
	return true;
}
}
/*
OLD METHOD WITH PARALLEL RESULTSET, DATA NOT ORDERED AS PER DATE	
private void exeRPLGR()
	{
		try
		{
			if(rdbYEAR.isSelected())
				{
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY0("Select * from MM_YRPRC a where a.yr_strtp='01'  order by a.YR_MATCD");
	//			ResultSet L_rstRSSET=cl_dat.exeSQLQRY0("Select * from MM_YRPRC a where a.yr_strtp='08' and a.yr_matcd in ('6805082016','6805350175','6805404925') order by a.YR_MATCD");
				//System.out.println("Select * from MM_YRPRC a where a.yr_strtp='01' and a.yr_matcd in ('6905200055','6905201055' ) order by a.YR_MATCD");
				M_rstRSSET=cl_dat.exeSQLQRY0("Select * from MM_PSLYR where ps_strtp='01'  order by PS_MATCD,PS_DOCTP");
	//			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from MM_PSLYR where ps_strtp='08' and ps_matcd in('6805082016','6805350175','6805404925' ) order by PS_MATCD,PS_DOCTP");
				//System.out.println("Select * from MM_PSLYR where ps_strtp='01' and ps_matcd in(select yr_matcd from mm_stmst, mm_yrprc where yr_strtp='01' and yr_matcd=st_matcd   and yr_strtp=st_strtp and yr_ycsqt<>st_stkqt ) order by PS_MATCD,PS_DOCTP");
				if(M_rstRSSET==null)
				{
					setMSG("No data found ..",'E');
					return;
				}
				String[] L_staTRNTP=new String[]{"GRIN","MRN","+SAN","MIN","SAN"};
				String L_strBLANK="  ";
				prnHEADR();
				L_rstRSSET.next();
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("YR_MATCD"),"")+"  :   ");
				dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("YR_MATDS"),""));
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				crtNWLIN();
				dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Opening Balance : ",24));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("YR_YOSQT"),""),10));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("YR_YOSVL"),""),10));
				prnFMTCHR(dosREPORT,M_strCPI12);
				crtNWLIN();
					double L_dblYCSQT=0,L_dblYCSVL=0;
				while(M_rstRSSET.next())
				{
					L_strBLANK="  ";
					if(M_intLINNO>50)
						prnHEADR();
					while( !M_rstRSSET.getString("PS_MATCD").equals(L_rstRSSET.getString("YR_MATCD")))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Closing Balance : ",24));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("YR_YCSQT"),""),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("YR_YCSVL"),""),10));
						crtNWLIN();
						crtHRLIN(50,"- ");
						if(!L_rstRSSET.next())
							break;
						prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("YR_MATCD"),"")+"  :   ");
						dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("YR_MATDS"),""));
						crtNWLIN();
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Opening Balance : ",24));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("YR_YOSQT"),""),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("YR_YOSVL"),""),10));
						crtNWLIN();
						prnFMTCHR(dosREPORT,M_strCPI12);
						L_dblYCSQT=L_rstRSSET.getDouble("YR_YCSQT");
						L_dblYCSVL=L_rstRSSET.getDouble("YR_YCSVL");
					}
					L_strBLANK="     ";
					dosREPORT.writeBytes(L_strBLANK+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PS_DOCDT"),""),10));
					dosREPORT.writeBytes(padSTRING('R',L_staTRNTP[M_rstRSSET.getInt("PS_DOCTP")-1],6));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PS_DOCNO"),""),10));
					if(M_rstRSSET.getFloat("PS_DOCVL")>0.0f&&!(L_staTRNTP[M_rstRSSET.getInt("PS_DOCTP")-1].equals("MIN")))
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),""),10));
					else
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),""),35));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),""),15));
					crtNWLIN();
				}
			//TOTAL FOR LAST MATERIAL CODE
				prnFMTCHR(dosREPORT,M_strCPI10);
				dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Closing Balance : ",24));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSQT,3),10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSVL,2),10));
				crtNWLIN();
				crtHRLIN(50,"- ");
			}
			else
			{//REPORT FOR MONTHLY PROCESSING
				ResultSet L_rstGRDAT=null,L_rstISDAT=null,L_rstPSADAT=null,L_rstNSADAT=null,L_rstMRDAT=null;//L_rstGRDAT=null;
				M_strSQLQRY="Select * from MM_STPRC order by STP_MATCD";
				M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
//				System.out.println(M_rstRSSET);
				L_rstGRDAT=cl_dat.exeSQLQRY0("Select * from MM_GRMST where GR_ACPDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' order by GR_MATCD, GR_ACPDT");
//				System.out.println(L_rstGRDAT);
				L_rstISDAT=cl_dat.exeSQLQRY0("Select * from MM_ISMST where date(IS_AUTDT) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' order by IS_MATCD, IS_AUTDT");
				//System.out.println(L_rstISDAT);
				L_rstMRDAT=cl_dat.exeSQLQRY0("Select * from MM_MRMST where MR_MRNDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' order by MR_MATCD, MR_MRNDT");
				//System.out.println(L_rstMRDAT);
				L_rstPSADAT=cl_dat.exeSQLQRY0("Select * from MM_SAMST where SA_SANDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and SA_SANQT>0 order by SA_MATCD, SA_SANDT");
				//System.out.println(L_rstPSADAT);
				L_rstNSADAT=cl_dat.exeSQLQRY0("Select * from MM_SAMST where SA_SANDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and SA_SANQT<0 order by SA_MATCD, SA_SANDT");
				if(M_rstRSSET == null && L_rstGRDAT==null && L_rstISDAT==null && L_rstPSADAT==null && L_rstMRDAT==null)
				{
					setMSG("No data found ..",'E');
					return;
				}
				String L_strMATCD=null,L_strMATCD1=null,L_strBLANK="\t";
				boolean L_flgGRDAT=true,L_flgISDAT=true,L_flgNSADAT=true,L_flgSADAT=true,L_flgMRDAT=true;
//				System.out.println(L_rstGRDAT);
				L_rstGRDAT.next();
				L_rstISDAT.next();
				L_rstPSADAT.next();
				L_rstNSADAT.next();
				L_rstMRDAT.next();
				String L_strDOCDT=null,L_strDOCNO=null,L_strDOCQT=null,L_strDOCVL=null;
				while(M_rstRSSET.next())
				{
//					System.out.println("in ourter while");
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),"")+"  :   ");
					dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("STP_MATDS"),""));
//					System.out.println("in ourter while 1");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					crtNWLIN();
					dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"  Opening Balance : ",24));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_MOSQT"),""),15));
//					System.out.println("in ourter while 2");
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_MOSVL"),""),15));
					prnFMTCHR(dosREPORT,M_strCPI12);
					crtNWLIN();
//					System.out.println("in ourter while 3");
					L_strMATCD=M_rstRSSET.getString("STP_MATCD");
//					System.out.println("in ourter while 4");
					
					if(L_flgGRDAT)
					L_strMATCD1=L_rstGRDAT.getString("GR_MATCD");
//					System.out.println("in ourter while 5  "+ L_strMATCD +" "+L_strMATCD1);
					while(L_strMATCD.equals(L_strMATCD1) && L_flgGRDAT)
					{
						dosREPORT.writeBytes(L_strBLANK+L_strBLANK);
//						System.out.println("in while"+L_flgGRDAT);
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_rstGRDAT.getDate("GR_ACPDT")),12));
						dosREPORT.writeBytes(padSTRING('R',"GRIN",5));
						dosREPORT.writeBytes(padSTRING('R',L_rstGRDAT.getString("GR_GRNNO"),12));
						dosREPORT.writeBytes(padSTRING('L',L_rstGRDAT.getString("GR_ACPQT"),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstGRDAT.getString("GR_BILVL"),"N.A."),15));
						crtNWLIN();
						if(L_strMATCD.equals(L_strMATCD1))
						{
							if(!L_rstGRDAT.next())
							{
								L_flgGRDAT=false;
								break;
							}
						}
						else 
							break;
						
						if(L_rstGRDAT.isLast())
						{
							L_flgGRDAT=false;
								break;
						}
//						System.out.println("MATCD");
						dosREPORT.close();
						L_strMATCD1=L_rstGRDAT.getString("GR_MATCD");
					}
					
				//DISPLAY +ve SAN DETAILS
					if(L_flgSADAT)
					L_strMATCD1=L_rstPSADAT.getString("SA_MATCD");
//					System.out.println("in ourter while 5  "+ L_strMATCD +" "+L_strMATCD1);
					while(L_strMATCD.equals(L_strMATCD1) && L_flgSADAT)
					{
						dosREPORT.writeBytes(L_strBLANK+L_strBLANK);
//						System.out.println("in while"+L_flgSADAT);
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_rstPSADAT.getDate("SA_SANDT")),12));
						dosREPORT.writeBytes(padSTRING('R',"SAN",5));
						dosREPORT.writeBytes(padSTRING('R',L_rstPSADAT.getString("SA_SANNO"),12));
						dosREPORT.writeBytes(padSTRING('L',L_rstPSADAT.getString("SA_SANQT"),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstPSADAT.getString("SA_SANVL"),"N.A."),15));
						crtNWLIN();
						if(L_strMATCD.equals(L_strMATCD1))
						{
							if(!L_rstPSADAT.next())
							{
								L_flgSADAT=false;
								break;
							}
						}
						else 
							break;
						
						if(L_rstPSADAT.isLast())
						{
							L_flgSADAT=false;
								break;
						}
//						System.out.println("MATCD");
						L_strMATCD1=L_rstPSADAT.getString("SA_MATCD");
					}
					setMSG("SAN details processed ..",'N');
				//DISPLAY MRN DETAILS
					if(L_flgMRDAT)
					L_strMATCD1=L_rstMRDAT.getString("MR_MATCD");
//					System.out.println("in ourter while 5  "+ L_strMATCD +" "+L_strMATCD1);
					while(L_strMATCD.equals(L_strMATCD1) && L_flgMRDAT)
					{
						dosREPORT.writeBytes(L_strBLANK+L_strBLANK);
//						System.out.println("in while"+L_flgMRDAT);
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_rstMRDAT.getDate("MR_MRNDT")),12));
						dosREPORT.writeBytes(padSTRING('R',"MRN",5));
						dosREPORT.writeBytes(padSTRING('R',L_rstMRDAT.getString("MR_MRNNO"),12));
						dosREPORT.writeBytes(padSTRING('L',L_rstMRDAT.getString("MR_RETQT"),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstMRDAT.getString("MR_MRNVL"),"N.A."),15));
						crtNWLIN();
						if(L_strMATCD.equals(L_strMATCD1))
						{
							if(!L_rstMRDAT.next())
							{
								L_flgMRDAT=false;
								break;
							}
						}
						else 
							break;
						
						if(L_rstMRDAT.isLast())
						{
							L_flgMRDAT=false;
								break;
						}
		//				System.out.println("MATCD");
						L_strMATCD1=L_rstMRDAT.getString("MR_MATCD");
					}
					setMSG("MRN details Processed ..",'N');
				//DISPLAY MIN DETAILS
					if(L_flgISDAT)
					L_strMATCD1=L_rstISDAT.getString("IS_MATCD");
		//			System.out.println("in ourter while 5  "+ L_strMATCD +" "+L_strMATCD1);
					while(L_strMATCD.equals(L_strMATCD1) && L_flgISDAT)
					{
						dosREPORT.writeBytes(L_strBLANK+L_strBLANK);
		//				System.out.println("in while"+L_flgISDAT);
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_rstISDAT.getDate("IS_AUTDT")),12));
						dosREPORT.writeBytes(padSTRING('R',"MIN",5));
						dosREPORT.writeBytes(padSTRING('R',L_rstISDAT.getString("IS_ISSNO"),12));
						dosREPORT.writeBytes(padSTRING('L',L_rstISDAT.getString("IS_ISSQT"),15+30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstISDAT.getString("IS_ISSVL"),"N.A."),15));
						crtNWLIN();
						if(L_strMATCD.equals(L_strMATCD1))
						{
							if(!L_rstISDAT.next())
							{
								L_flgISDAT=false;
								break;
							}
						}
						else 
							break;
						
						if(L_rstISDAT.isLast())
						{
							L_flgISDAT=false;
								break;
						}
//						System.out.println("MATCD");
						L_strMATCD1=L_rstISDAT.getString("IS_MATCD");
					}
				//DISPLAY -ve SAN DETAILS
					if(L_flgNSADAT)
					L_strMATCD1=L_rstNSADAT.getString("SA_MATCD");
//					System.out.println("in ourter while 5  "+ L_strMATCD +" "+L_strMATCD1);
					while(L_strMATCD.equals(L_strMATCD1) && L_flgNSADAT)
					{
						
//						System.out.println("in while"+L_flgNSADAT);
						dosREPORT.writeBytes(L_strBLANK+L_strBLANK);
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_rstNSADAT.getDate("SA_SANDT")),12));
						dosREPORT.writeBytes(padSTRING('R',"SAN",5));
						dosREPORT.writeBytes(padSTRING('R',L_rstNSADAT.getString("SA_SANNO"),12));
						dosREPORT.writeBytes(padSTRING('L',L_rstNSADAT.getString("SA_SANQT"),15+30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstNSADAT.getString("SA_SANVL"),"N.A."),15));
						crtNWLIN();
						if(L_strMATCD.equals(L_strMATCD1))
						{
							if(!L_rstNSADAT.next())
							{
								L_flgNSADAT=false;
								break;
							}
						}
						else 
							break;
						
						if(L_rstNSADAT.isLast())
						{
							L_flgNSADAT=false;
								break;
						}
//						System.out.println("MATCD");
						L_strMATCD1=L_rstNSADAT.getString("SA_MATCD");
					}
					prnFMTCHR(dosREPORT,M_strCPI10);
					dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"  Closing Balance : ",24));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YOSQT"),""),15));
//					System.out.println("in ourter while 2");
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCSVL"),""),15));
					prnFMTCHR(dosREPORT,M_strCPI12);
					crtNWLIN();
					crtHRLIN(52,"- ");
				}
					dosREPORT.flush();
					dosREPORT.close();
					M_rstRSSET.close();
					L_rstGRDAT.close();L_rstISDAT.close();L_rstMRDAT.close();L_rstPSADAT.close();
			}
			
		}catch(Exception e)
		{setMSG(e,"Child.exePRINT");}
	}

	private void dspDOCDT(String P_strDOCTP,String P_strDOCDT,String P_strDOCNO,String P_strDOCQT,String P_strDOCVL) throws  Exception
	{
//		dosREPORT.writeBytes(L_strBLANK+L_strBLANK);
		dosREPORT.writeBytes(padSTRING('R',P_strDOCDT,12));
		dosREPORT.writeBytes(padSTRING('R',P_strDOCTP,5));
		dosREPORT.writeBytes(padSTRING('R',P_strDOCNO,12));
		dosREPORT.writeBytes(padSTRING('L',P_strDOCQT,15));
		dosREPORT.writeBytes(padSTRING('L',P_strDOCVL,15));
		crtNWLIN();

	}
*/
