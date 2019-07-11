/*
	Name			: Order COnfrmation Printing
	System			: EXPORT MKT
	Author			: SRT
	Version			: v2.0.0
	Last Modified	: 02/07/2007
	Documented On	: 02/07/2007
*/

import javax.swing.*;
import java.io.File;import java.io.FileOutputStream;import java.io.DataOutputStream;import java.io.IOException;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.Color;import java.awt.Component;
import java.util.Vector;import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
import java.sql.ResultSet;import java.util.TreeMap;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>   Customer Order Printing</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                        Form for Customer       Order&nbsp;Printing (To be extended for HTML Format) </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Marketing System Enhancement Proposal by      Mr. SRD      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mr_rpocn.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mr_teind.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>15/04/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>&nbsp;MR_INMST</TD>    <TD>OC_OCFNO,IN_MKTTP&nbsp; </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_OCTRN</TD>    <TD>MKTTP,OCFNO,PRDCD</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>&nbsp;MR_DOTRN</TD>    <TD>DOT_INDNO,DOT_DORNO&nbsp;</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P><P>&nbsp;</P><P><FONT color=purple><STRONG>Features :</STRONG></FONT></P><UL>  <LI><FONT color=black>Report available in text as well as HTML   format.</FONT>  <LI>Available at present only for a perticular indent no.</LI></UL>*/
class mr_rpocn extends cl_rbase 
{
	private String strDSTNM;/**	Flag to indicate that, data is being written in table format in HTML */
	private boolean flgTBLDT;/**	Global string for default text color in HTML */
	private String strTXCLR="<font Color=black>";/**Global string for Text Format in HTML	 */
	private String strTXFMT="";/** String for Distributer Name */
	private String strDSRNM="";/** String for Agent Name    */
	//private String strAGTNM="";/** String for Report Title */
	private String strREPNM="";/** Process to display the report */
	private Process prcREPORT;/** Line counter */
	private int intLINCT;/**Global string for page break in HTML	 */
	private String strPGBRK="<P CLASS = \"breakhere\"></PRE>";/**DATA OUTPUT STEARM TO WRITE TO FILE	 */
	private DataOutputStream dosREPORT;/** String to store Booking by Initials (IN_BKGBY) */
	private String strBKGBY;/**String to store Registered by Initials (IN_REGBY) */
	private String strREGBY;/** String to store Reviewed  by Initials (DOT_LUSBY)*/
	private String strRVWBY="";/** String for D.O. date (DOT_DORDT)*/
	private String strDORDT="";/** String for D.O. Number (DOT_DORNO)*/
	private String strDORNO="";/** Text field for indent no entry*/
	//private String strRPTFL = cl_dat.M_strREPSTR_pbst+txtOCFNO.getText();
	private String strRPTFL = "";
	public JTextField txtMKTTP;
	public JTextField txtOCFNO;
	public boolean flgOUTPRN; /**Hash table for base details of indent */
	private Hashtable<String,String> hstBASDT;/**Hshtable for grade details of indent */
	private Hashtable<String,String> hstGRDDT;/**Hash table for tax details of indent */
	private Hashtable<String,String> hstTAXDT;/**Hash table for tax catagories */
	private Hashtable<String,String> hstTXCAT;/**Hash table for tax details of indent */
	private Hashtable<String,String[]> hstCDTRN;			// Details of all codes used in program
	private Hashtable<String,String[]> hstTXDOC;			// Hash table for capturing Common Tax (Indent Level)
	private Hashtable<String,String[]> hstTXSPC;			// Hash table for capturing Specific Tax (Indent Level)
	private Hashtable<String,String> hstDLSCH;
	//private TreeMap hstDLSCH;
	private Hashtable<String,String[]> hstTXITM;
	private final String strISO1_fn="ISO DOC.NO. : SPL/MKT/QR/014",strISO2_fn="ISSUE NO./DATE : 01/01-11-99",strISO3_fn="REV.NO./DATE : 00"; 
	private String strSALTP_EXP = "12";		// Export Sale Type
	private String strSALTP_DEX = "03";		// Deemed Export Sale Type
	private String strSALTP_STF = "04";		// Stock Transfer Sale Type
	private String strSALTP     = "  ";		// Sale Type
	private String strSPLRF     = "  ";	
	private String strDELAD     = "  ";		
	private int intCDTRN_TOT = 9;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
	private String strORDDTL = "";	
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	private String strDSRCD;
	
	private String strTX_SYSCD;
	private String strTX_SBSTP;
	private String strTX_DOCTP;
	private String strTX_DOCNO;
	private String strTX_PRDCD;
	
	private String strTXT_SYSCD;
	private String strTXT_SBSTP;
	private String strTXT_DOCTP;
	private String strTXT_DOCNO;
	private String strTXT_PRDCD;
	private String strTXT_CODCD;

	
	
	boolean flgCOTAX = true;
	boolean flgSPTAX = true;
	String[] arrTXFLD = new String[] {"EXC","EDC","EHC","SCH","TOT","CST","STX","VAT","HIC","OTH"};  // Array of taxes applicable for Marketing System
	int intTXLIN = 1;						// Line No. for Tax Detail printing
	private int intTXFLD = 0;               // Running array element getting printed from Tax Detail Array.
	private int intTXFLD_TOT = 8;			// Total No. of Taxes defined in Tax Detail Array

	private int intTXDOC_TOT = 22;			// Total No. of elements in Tax Detail Array
											/** Array elements for Doc.wise Tax detail */
    private int intAE_TX_EXCVL = 0;
    private int intAE_TX_EXCFL = 1;
    private int intAE_TX_EDCVL = 2;
    private int intAE_TX_EDCFL = 3;
    private int intAE_TX_EHCVL = 4;
    private int intAE_TX_EHCFL = 5;
    private int intAE_TX_SCHVL = 6;
    private int intAE_TX_SCHFL = 7;
    private int intAE_TX_TOTVL = 8;
    private int intAE_TX_TOTFL = 9;
    private int intAE_TX_CSTVL = 10;
    private int intAE_TX_CSTFL = 11;
    private int intAE_TX_VATVL = 12;
    private int intAE_TX_VATFL = 13;
    private int intAE_TX_HICVL = 14;
    private int intAE_TX_HICFL = 15;
    private int intAE_TX_STXVL = 16;
    private int intAE_TX_STXFL = 17;
    private int intAE_TX_STXDS = 18;
    private int intAE_TX_OTHVL = 19;
    private int intAE_TX_OTHFL = 20;
    private int intAE_TX_OTHDS = 21;
	

	private int intTXSPC_TOT = 2;			// Total No. of elements in Specific Tax Detail Array
											/** Array elements for Doc.wise Specificv Tax detail */
    private int intAE_TXT_CODVL = 0;
    private int intAE_TXT_CODFL = 1;
	
	
	/**Constructor for printing from indent entry screen */
	mr_rpocn(int P_intDESTN)
	{
		super();
		try
		{
			//System.out.println("i m in");
			add(new JLabel("Order Conf.No."),3,1,1,1,this,'L');
			add(txtOCFNO=new JTextField(),3,2,1,1,this,'L');
			add(new JLabel("Mkt.Type."),2,1,1,1,this,'L');
			add(txtMKTTP=new JTextField(),2,2,1,0.5,this,'L');
			M_pnlRPFMT.setVisible(true);
			M_rdbTEXT.setSelected(true);
			hstTXDOC = new Hashtable<String,String[]>();
			hstTXSPC = new Hashtable<String,String[]>();
			hstCDTRN = new Hashtable<String,String[]>();
			hstTXITM = new Hashtable<String,String[]>();
			hstCDTRN.clear();
			crtCDTRN("'SYSCOXXTAX','MSTCOXXMKT'","",hstCDTRN);
			//System.out.println("M_strSBSLS in MR_RPOCN : "+M_strSBSLS);
			M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from  CO_CDTRN where CMT_CGSTP='COXXTAX' and CMT_STSFL<>'X'";
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(L_rstRSSET!=null)
			{
				hstTXCAT=new Hashtable<String,String>(10,0.2f);
				while(L_rstRSSET.next())
					hstTXCAT.put(L_rstRSSET.getString("CMT_CODCD"),L_rstRSSET.getString("CMT_CODDS"));
				L_rstRSSET.close();
			}
		}
		catch(Exception e)
		{setMSG(e,"Child.constructor 1");}
	}
	/**Constructor for printing from report menu */
	mr_rpocn()
	{
		super(2);
		try
		{
			add(new JLabel("Order Conf.No."),3,1,1,1,this,'L');
			add(txtOCFNO=new JTextField(),3,2,1,1,this,'L');
			add(new JLabel("Mkt.Type."),2,1,1,1,this,'L');
			add(txtMKTTP=new JTextField(),2,2,1,0.5,this,'L');
			txtMKTTP.setText("01");
			M_pnlRPFMT.setVisible(true);
			M_rdbTEXT.setSelected(true);
			flgOUTPRN = false;
			hstTXDOC = new Hashtable<String,String[]>();
			hstTXSPC = new Hashtable<String,String[]>();
			hstCDTRN = new Hashtable<String,String[]>();
			hstTXITM = new Hashtable<String,String[]>();
			hstCDTRN.clear();
			crtCDTRN("'SYSCOXXTAX','MSTCOXXMKT'","",hstCDTRN);
			M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from  CO_CDTRN where CMT_CGSTP='COXXTAX' and CMT_STSFL<>'X'";
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(L_rstRSSET!=null)
			{
				hstTXCAT=new Hashtable<String,String>(10,0.2f);
				while(L_rstRSSET.next())
					hstTXCAT.put(L_rstRSSET.getString("CMT_CODCD"),L_rstRSSET.getString("CMT_CODDS"));
				L_rstRSSET.close();
			}
		}catch(Exception e)
		{setMSG(e,"Child.constructor 2");}
	}
	
	/**<b> TASKS</b><br>
	 * Source = txtOCFNO : HELP of available indent no
	 * Source = cmbOPTN : Transfer focus to txtOCFNO */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(M_objSOURC==txtMKTTP && L_KE.getKeyCode()==L_KE.VK_ENTER)
			txtOCFNO.requestFocus();
		if(M_objSOURC==txtOCFNO && L_KE.getKeyCode()==L_KE.VK_F1)
		{
			M_strHLPFLD = "txtOCFNO";
			cl_hlp("Select distinct OC_OCFNO,OC_OCFDT from VW_OCTRN where OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP = '"+txtMKTTP.getText()+"' and OCT_SBSCD1 in "+M_strSBSLS+" order by OC_OCFDT desc, oc_ocfno desc",1,1,new String[] {"Order Conf.No.","Date    "},2 ,"CT");
		}
		else if(M_objSOURC==txtMKTTP && L_KE.getKeyCode()==L_KE.VK_F1)
		{
			M_strHLPFLD = "txtMKTTP";
			M_strSQLQRY="Select CMT_CODCD, CMT_CODDS from CO_CDTRN where  CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXMKT' order by CMT_CODCD";
			//System.out.println(M_strSQLQRY);
			cl_hlp(M_strSQLQRY,1,1,new String[] {"Market Type"},1 ,"CT");
			//cl_hlp(M_strSQLQRY,1,2,new String[] {"Code","Description"},5,"CT");
		}
		else if(M_objSOURC==cl_dat.M_cmbOPTN_pbst&&(L_KE.getKeyCode()==L_KE.VK_P ||L_KE.getKeyCode()==L_KE.VK_S || L_KE.getKeyCode()==L_KE.VK_F || L_KE.getKeyCode()==L_KE.VK_E))
			txtOCFNO.requestFocus();
	}
	
	/**<b> TASKS</b><br>
	 * Source = cmbOPTN : Transfer focus to txtOCFNO */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			txtMKTTP.requestFocus();
		//else if(M_objSOURC==txtMKTTP)
		//	strMKTTP = txtMKTTP.getText();
		//if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
		//	txtOCFNO.requestFocus();
	}
	public void exeHLPOK()
	{
		cl_dat.M_flgHELPFL_pbst = false;
		super.exeHLPOK();
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD.equals("txtOCFNO"))
		{	
			txtOCFNO.setText(L_STRTKN.nextToken());
		}	
		else if(M_strHLPFLD.equals("txtMKTTP"))
		{
			txtMKTTP.setText(L_STRTKN.nextToken());
			//strMKTTP = txtMKTTP.getText();
		}
	}
	/**To format and create the report	 */
	void exePRINT()

	{
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		//System.out.println(M_strSBSLS);
		try
		{
			//System.out.println("001"+txtMKTTP.getText()+"/"+txtOCFNO.getText());
			
			strREPNM="ORDER CONFIRMATION";
			String L_strCGSTP = M_strSBSCD.substring(0,2).equals("12") ? "MRXXPOD" : "COXXDST";
			M_strSQLQRY = "Select oc_mkttp,oc_ocfno,oc_amdno,oc_amddt,oc_ocfdt,oc_saltp,oc_porno,oc_pordt,oc_byrcd,oc_byrnm,";
			M_strSQLQRY+= "oc_cnscd,oc_dsrtp,oc_dsrcd,oc_trpnm,oc_pmtrf,oc_pshfl,oc_tshfl,oc_filrf,oc_insrf,oc_forrf,";
			M_strSQLQRY+= "oc_regby,oc_regdt,oc_bkgby,oc_bkgdt,oc_cnscd,oc_cnsnm,oc_aptvl,oc_cptvl,oc_delad,oc_saltp,oc_dtpcd,oc_motcd,";
			M_strSQLQRY+= "oc_lcedt,oc_ashdt,oc_lshdt, ";
			M_strSQLQRY+= "oc_pmtcd,oc_dstcd,oct_reqqt,oct_ocfqt,oct_pkgtp,C.CMT_CODDS OCT_PKGDS,oct_pkgwt,oct_cc1vl,oct_cc2vl,oct_cc3vl,pt_prtnm,";
			M_strSQLQRY+= "oct_prdcd,oct_prdds,oct_basrt,B.CMT_CODDS ORDDTL,A.CMT_CODDS DSTDS ";
			M_strSQLQRY+= "from CO_CDTRN A,CO_CDTRN B,CO_CDTRN C,vw_octrn ";
			M_strSQLQRY+= "left outer join co_ptmst on PT_PRTCD=substring(OCT_CC3RF,2,5) ";
			M_strSQLQRY+= " and PT_PRTTP=substring(OCT_CC3RF,1,1) where OCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCT_OCFNO='"+txtOCFNO.getText()+"'";
			M_strSQLQRY+= " and OCT_MKTTP='"+txtMKTTP.getText()+"' ";
			M_strSQLQRY+= " and OCT_SBSCD1 in "+M_strSBSLS+"  and A.CMT_CODCD=OC_DSTCD ";
			M_strSQLQRY+= " and A.CMT_CGSTP='"+L_strCGSTP+"'  AND B.CMT_CGSTP='COXXPGR' AND LEFT(OCT_PRDCD,4)=LEFT(B.CMT_CODCD,4) ";
			M_strSQLQRY+= " and C.CMT_CGSTP='FGXXPKG' and C.CMT_CODCD=OCT_PKGTP ";
			M_strSQLQRY+= "ORDER BY OCT_PRDCD,OCT_PKGTP";
			System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println("200");
			if(M_rstRSSET==null || !M_rstRSSET.next())
				{setMSG("Order Confirmation Not found",'E'); return;}
				strDSRCD = nvlSTRVL(M_rstRSSET.getString("OC_DSRCD"),"");
				crtTXDOC(txtMKTTP.getText(),txtOCFNO.getText());
			//System.out.println("201");
				crtTXSPC(txtMKTTP.getText(),txtOCFNO.getText());
			//System.out.println("202");
				boolean flgFIRST=true;
				hstBASDT=new Hashtable<String,String>(10,0.2f);hstGRDDT=new Hashtable<String,String>(5,0.2f);hstTAXDT=new Hashtable<String,String>(5,0.2f);hstDLSCH=new Hashtable<String,String>(5,0.2f);//hstDLSCH=new TreeMap();
				int L_intPRCNT = 0;
				while(true)
				{
					//System.out.println("003");
					if(flgFIRST)
					{
						flgFIRST=false;
//System.out.println("203");						
						strDSTNM=nvlSTRVL(M_rstRSSET.getString("DSTDS")," ");
//System.out.println("204");						
						
				//PUTTING BASE DETAILS IN HASH TABLE
						hstBASDT.put("OC_BYRCD",nvlSTRVL(M_rstRSSET.getString("OC_BYRCD")," "));
						hstBASDT.put("OC_DSRTP",nvlSTRVL(M_rstRSSET.getString("OC_DSRTP")," "));
						hstBASDT.put("OC_DSRCD",nvlSTRVL(M_rstRSSET.getString("OC_DSRCD")," "));
						hstBASDT.put("OC_CNSCD",nvlSTRVL(M_rstRSSET.getString("OC_CNSCD")," "));
						hstBASDT.put("OC_APTVL",nvlSTRVL(M_rstRSSET.getString("OC_APTVL")," "));
						hstBASDT.put("OC_CPTVL",nvlSTRVL(M_rstRSSET.getString("OC_CPTVL")," "));
						//hstBASDT.put("OC_CFTAG",nvlSTRVL(M_rstRSSET.getString("OC_CFTAG")," "));   // added
						hstBASDT.put("OC_DELAD",nvlSTRVL(M_rstRSSET.getString("OC_DELAD")," "));   // added
						//System.out.println("100");
						//hstBASDT.put("PT_SPLRF",nvlSTRVL(M_rstRSSET.getString("PT_SPLRF")," "));   // added

						//System.out.println("004");
						if(M_rstRSSET.getString("OC_OCFDT")!=null)
						{	
							hstBASDT.put("OC_OCFDT",M_fmtLCDAT.format(M_rstRSSET.getDate("OC_OCFDT")));
						//System.out.println("101");
						}
						else
						{	
							hstBASDT.put("OC_OCFDT"," ");
						//System.out.println("102");
						}
						hstBASDT.put("OC_PORNO",nvlSTRVL(M_rstRSSET.getString("OC_PORNO")," "));
						if(M_rstRSSET.getString("OC_PORDT")!=null)
						{	hstBASDT.put("OC_PORDT",M_fmtLCDAT.format(M_rstRSSET.getDate("OC_PORDT")));
						}
						else
							{	hstBASDT.put("OC_PORDT"," ");
							}	//System.out.println("104");}
						hstBASDT.put("OC_PSHFL",nvlSTRVL(M_rstRSSET.getString("OC_PSHFL")," "));
						hstBASDT.put("OC_TSHFL",nvlSTRVL(M_rstRSSET.getString("OC_TSHFL")," "));
						hstBASDT.put("OC_FILRF",nvlSTRVL(M_rstRSSET.getString("OC_FILRF")," "));
						hstBASDT.put("OC_INSRF",nvlSTRVL(M_rstRSSET.getString("OC_INSRF")," "));
						hstBASDT.put("OC_FORRF",nvlSTRVL(M_rstRSSET.getString("OC_FORRF")," "));
						hstBASDT.put("OC_REGBY",nvlSTRVL(M_rstRSSET.getString("OC_REGBY")," "));
						hstBASDT.put("OC_REGDT",nvlSTRVL(M_rstRSSET.getString("OC_REGDT")," "));
						hstBASDT.put("OC_BKGBY",nvlSTRVL(M_rstRSSET.getString("OC_BKGBY")," "));
						hstBASDT.put("OC_BKGDT",nvlSTRVL(M_rstRSSET.getString("OC_BKGDT")," "));
						hstBASDT.put("OC_AMDNO",nvlSTRVL(M_rstRSSET.getString("OC_AMDNO")," "));
						hstBASDT.put("OC_TRPNM",nvlSTRVL(M_rstRSSET.getString("OC_TRPNM")," "));
						hstBASDT.put("OC_PMTRF",nvlSTRVL(M_rstRSSET.getString("OC_PMTRF")," "));
						hstBASDT.put("OC_LCEDT",M_fmtLCDAT.format(M_rstRSSET.getDate("OC_LCEDT")));						
						hstBASDT.put("OC_LSHDT",M_fmtLCDAT.format(M_rstRSSET.getDate("OC_LSHDT")));						
						hstBASDT.put("OC_ASHDT",M_fmtLCDAT.format(M_rstRSSET.getDate("OC_ASHDT")));						
						//System.out.println("105");
						//System.out.println("005");
						if(M_rstRSSET.getString("OC_AMDDT")!=null)
						{	hstBASDT.put("OC_AMDDT",M_fmtLCDAT.format(M_rstRSSET.getDate("OC_AMDDT")));
						}						
						else
						{	hstBASDT.put("OC_AMDDT"," ");
						}						
				//RETRIEVING DETAILS OF CONSIGNEE AND BUYER
						//ResultSet L_rstRSSET=cl_dat.exeSQLQRY1("Select * from co_ptmst where pt_prtcd in ('"+M_rstRSSET.getString("OC_BYRCD")+"','"+M_rstRSSET.getString("OC_DSRCD")+"','"+M_rstRSSET.getString("OC_CNSCD")+"','"+M_rstRSSET.getString("OC_AGTCD")+"') and PT_PRTTP in ('C','D','G','A')");
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY1("Select * from co_ptmst where pt_prtcd in ('"+M_rstRSSET.getString("OC_BYRCD")+"','"+M_rstRSSET.getString("OC_DSRCD")+"','"+M_rstRSSET.getString("OC_CNSCD")+"','"+M_rstRSSET.getString("OC_DSRCD")+"') and PT_PRTTP in ('C','D','G','A')");
						if(L_rstRSSET!=null)
						{
							while(L_rstRSSET.next())
							{
								//System.out.println("006");
								if(L_rstRSSET.getString("PT_PRTCD").equals(M_rstRSSET.getString("OC_BYRCD")) && L_rstRSSET.getString("PT_PRTTP").equals("C") )
								   hstBASDT.put("OC_BYRNM",L_rstRSSET.getString("PT_PRTNM")+"|"
															+nvlSTRVL(L_rstRSSET.getString("PT_ADR01")," ")+"|"
															+nvlSTRVL(L_rstRSSET.getString("PT_ADR02")," ")+"|"
															+nvlSTRVL(L_rstRSSET.getString("PT_ADR03")," "));
								else if(L_rstRSSET.getString("PT_PRTCD").equals(M_rstRSSET.getString("OC_CNSCD")) && L_rstRSSET.getString("PT_PRTTP").equals("C"))
								   hstBASDT.put("OC_CNSNM",L_rstRSSET.getString("PT_PRTNM")+"|"
															+nvlSTRVL(L_rstRSSET.getString("PT_ADR01")," ")+"|"
															+nvlSTRVL(L_rstRSSET.getString("PT_ADR02")," ")+"|"
															+nvlSTRVL(L_rstRSSET.getString("PT_ADR03")," "));
								else if(L_rstRSSET.getString("PT_PRTCD").equals(M_rstRSSET.getString("OC_DSRCD")) && L_rstRSSET.getString("PT_PRTTP").equals(M_rstRSSET.getString("OC_DSRTP")))
									strDSRNM=L_rstRSSET.getString("PT_PRTNM");
								//else if(L_rstRSSET.getString("PT_PRTCD").equals(M_rstRSSET.getString("OC_AGTCD")) && L_rstRSSET.getString("PT_PRTTP").equals("A"))
								//	strAGTNM=L_rstRSSET.getString("PT_PRTNM");
							}
							L_rstRSSET.close();
						}
				//RETRIEVING DETAILS OF MODE OF TRANSPORT, DELIVERY TYPE, SALE TYPE, DESTINATION DESCIPTION	and PAYMENT TYPE	
						L_rstRSSET=cl_dat.exeSQLQRY1("Select CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN where (CMT_CGMTP ='SYS' and "
																+"(CMT_CGSTP='MR01MOT' or CMT_CGSTP='MRXXDTP' or CMT_CGSTP='MRXXPMT' or CMT_CGSTP='MR00SAL' or CMT_CGSTP='COXXDST'))"
																+"order by CMT_CGSTP,CMT_CODCD");
						if(L_rstRSSET!=null)
						{
							while(L_rstRSSET.next())
							{
								//System.out.println("007");
								strSALTP = M_rstRSSET.getString("OC_SALTP");
								if(L_rstRSSET.getString("CMT_CGSTP").equals("MR01MOT"))
								{
									if(L_rstRSSET.getString("CMT_CODCD").equals(M_rstRSSET.getString("OC_MOTCD")))
										hstBASDT.put("OC_MOTDS",L_rstRSSET.getString("CMT_CODDS"));
								}
								else if(L_rstRSSET.getString("CMT_CGSTP").equals("MRXXDTP"))
								{
									if(L_rstRSSET.getString("CMT_CODCD").equals(M_rstRSSET.getString("OC_DTPCD")))
										hstBASDT.put("OC_DTPDS",L_rstRSSET.getString("CMT_CODDS"));
								}
								else if(L_rstRSSET.getString("CMT_CGSTP").equals("MRXXPMT"))
								{
									if(L_rstRSSET.getString("CMT_CODCD").equals(M_rstRSSET.getString("OC_PMTCD")))
										hstBASDT.put("OC_PMTDS",L_rstRSSET.getString("CMT_CODDS"));
								}
								else if(L_rstRSSET.getString("CMT_CGSTP").equals("COXXDST"))
								{
									if(L_rstRSSET.getString("CMT_CODCD").equals(M_rstRSSET.getString("OC_DSTCD")))
										hstBASDT.put("OC_DSTDS",L_rstRSSET.getString("CMT_CODDS"));
								}
								else if(L_rstRSSET.getString("CMT_CGSTP").equals("MR00SAL"))
								{
									if(L_rstRSSET.getString("CMT_CODCD").equals(M_rstRSSET.getString("OC_SALTP")))
										hstBASDT.put("OC_SALDS",L_rstRSSET.getString("CMT_CODDS"));
								}
							}
							L_rstRSSET.close();
}
						//System.out.println("008");
						strREGBY=nvlSTRVL(M_rstRSSET.getString("OC_REGBY"),"");
						strBKGBY=nvlSTRVL(M_rstRSSET.getString("OC_BKGBY"),"");
						//strDORNO=nvlSTRVL(M_rstRSSET.getString("OC_DORNO"),"");
				//RETIEVING DETAILS OF D.O.
						/**if(strDORNO.length()>0)
						{
							L_rstRSSET=cl_dat.exeSQLQRY1("Select * from MR_DOTRN where DOT_DORNO='"+strDORNO+"' and DOT_SBSCD1 in "+M_strSBSLS);
							if(L_rstRSSET!=null)
							{
								while(L_rstRSSET.next())
								{
									strDORDT=M_fmtLCDAT.format(L_rstRSSET.getDate("DOT_DORDT"));
									strRVWBY=nvlSTRVL(L_rstRSSET.getString("DOT_LUSBY"),"");
								}						
							}
						}*/
					}
					//System.out.println("010");
					String L_strOCFQT="";
					if(M_rstRSSET.getFloat("OCT_OCFQT")==0.0f)
						//System.out.println("exeprint3");
					{
						L_strOCFQT=M_rstRSSET.getString("OCT_REQQT");
						//strREPNM="PROVISIONAL CUSTOMER ORDER"
					}
					else
						L_strOCFQT=M_rstRSSET.getString("OCT_OCFQT");
					//System.out.println("011");
					hstGRDDT.put(M_rstRSSET.getString("OCT_PRDDS")+M_rstRSSET.getString("OCT_PKGTP"),
								 L_strOCFQT+"|"
								 +nvlSTRVL(M_rstRSSET.getString("OCT_PKGDS"),"-")+"|"
								 +M_rstRSSET.getString("OCT_PKGWT")+"|"
								 +setNumberFormat((Float.parseFloat(L_strOCFQT))/M_rstRSSET.getFloat("OCT_PKGWT"),0)+"|"
								 +nvlSTRVL(M_rstRSSET.getString("OCT_BASRT"),"-")+"|"
								 +nvlSTRVL(M_rstRSSET.getString("OCT_PRDCD"),"-")+"|"
								 +nvlSTRVL(M_rstRSSET.getString("OCT_CC1VL"),"-")+"|"
								 +nvlSTRVL(M_rstRSSET.getString("OCT_CC2VL"),"-")+"|"
								 +nvlSTRVL(M_rstRSSET.getString("OCT_CC3VL"),"-")+"|"
								 +nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"-")+"|");
								 //+nvlSTRVL(M_rstRSSET.getString("OCT_PRDRF"),"-")+"|");
					if(L_intPRCNT==0)
						strORDDTL = nvlSTRVL(M_rstRSSET.getString("ORDDTL"),"-");
					else
						strORDDTL = strORDDTL+","+nvlSTRVL(M_rstRSSET.getString("ORDDTL"),"-");
					//System.out.println(strORDDTL);
					L_intPRCNT=L_intPRCNT+1;
					if(!M_rstRSSET.next())
						break;
				}
				//oct_reqqt,oct_ocfqt,oct_pkgwt,oct_cc1vl,oct_cc2vl,oct_cc3vl,pt_prtnm,oct_prdcd,oct_basrt
				M_strSQLQRY = "SELECT * from MR_OCDEL WHERE OCD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCD_OCFNO='"+txtOCFNO.getText()+"' and OCD_MKTTP='"+txtMKTTP.getText()+"' and OCD_SBSCD1 in " +M_strSBSLS +"order by ocd_prdcd,ocd_pkgtp,ocd_srlno";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
                String L_strDATE =" ";
                if(M_rstRSSET !=null)
                while(M_rstRSSET.next())
                {
                    if(M_rstRSSET.getDate("OCD_DSPDT") !=null)
                        L_strDATE = M_fmtLCDAT.format(M_rstRSSET.getDate("OCD_DSPDT"));
                    else L_strDATE =" ";    
                  hstDLSCH.put(M_rstRSSET.getString("OCD_PRDCD")+"|"+M_rstRSSET.getString("OCD_SRLNO"),
								 M_rstRSSET.getString("OCD_DELTP")+"|"+M_rstRSSET.getString("OCD_OCFQT")+"|"
								 +L_strDATE+"|");
                }
 	//RETIEVING DOCUMENT LEVEL TAX DETAILS AND PUTTING IN HASHTABLE
/*			M_rstRSSET=cl_dat.exeSQLQRY("Select * from co_txdoc where tx_SYSCD='MR' and TX_DOCTP='OCF' and TX_SBSCD='"+M_strSBSCD+"' and TX_DOCNO='"+txtOCFNO.getText()+"' ");
			if(M_rstRSSET!=null)
			{
				System.out.println("012");
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getFloat("TX_DSBVL")!=0.0f)//,TX_DSBVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"DSB",M_rstRSSET.getString("TX_DSBVL")+"|"+(M_rstRSSET.getString("TX_DSBFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_EXCVL")!=0.0f)//,TX_EXCVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"EXC", M_rstRSSET.getString("TX_EXCVL")+"|"+(M_rstRSSET.getString("TX_EXCFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_PNFVL")!=0.0f)//,TX_PNFVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"PNF", M_rstRSSET.getString("TX_PNFVL")+"|"+(M_rstRSSET.getString("TX_PNFFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_CSTVL")!=0.0f)//,TX_CSTVL,////////////////////
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"CST",M_rstRSSET.getString("TX_CSTVL")+"|"+(M_rstRSSET.getString("TX_CSTFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_STXVL")!=0.0f)//,TX_STXVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"STX",M_rstRSSET.getString("TX_STXVL")+"|"+(M_rstRSSET.getString("TX_STXFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_OCTVL")!=0.0f)//,TX_OCTVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"OCT",M_rstRSSET.getString("TX_OCTVL")+"|"+(M_rstRSSET.getString("TX_OCTFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_FRTVL")!=0.0f)//,TX_FRTVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"FRT",M_rstRSSET.getString("TX_FRTVL")+"|"+M_rstRSSET.getString("TX_FRTFL"));
					if(M_rstRSSET.getFloat("TX_INSVL")!=0.0f)//,TX_INSVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"INS",M_rstRSSET.getString("TX_INSVL")+"|"+(M_rstRSSET.getString("TX_INSFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_CDSVL")!=0.0f)//,TX_CDSVL,
				hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"CDS",M_rstRSSET.getString("TX_CDSVL")+"|"+(M_rstRSSET.getString("TX_CDSFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_INCVL")!=0.0f)//,TX_INCVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"INC",M_rstRSSET.getString("TX_INCVL")+"|"+(M_rstRSSET.getString("TX_INCFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_ENCVL")!=0.0f)//,TX_ENCVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"ENC",M_rstRSSET.getString("TX_ENCVL")+"|"+(M_rstRSSET.getString("TX_ENCFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_FNIVL")!=0.0f)//,TX_FNIVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"FNI",M_rstRSSET.getString("TX_FNIVL")+"|"+(M_rstRSSET.getString("TX_FNIFL").equals("R")==true ? "%" : "RS. "));
					if(M_rstRSSET.getFloat("TX_CDUVL")!=0.0f)//,TX_CDUVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"CDU",M_rstRSSET.getString("TX_CDUVL")+"| N.A.");
					if(M_rstRSSET.getFloat("TX_CLRVL")!=0.0f)//,TX_CLRVL,
						hstTAXDT.put(M_rstRSSET.getString("TX_PRDCD")+"|"+"CLR",M_rstRSSET.getString("TX_CLRVL")+"|"+(M_rstRSSET.getString("TX_CLRFL").equals("R")==true ? "%" : "RS. "));
				}
			}
	//RETIEVING SPECIFIC TAX DETAILS AND PUTTING IN HASH TABLE
			M_rstRSSET=cl_dat.exeSQLQRY("Select * from co_txspc where txt_SYSCD='MR' and TXt_DOCTP='OCF' and TXt_SBSCD='"+M_strSBSCD+"' and TXt_DOCNO='"+txtOCFNO.getText()+"' ");
			System.out.println("013");
			if(M_rstRSSET!=null)
				while(M_rstRSSET.next())
					hstTAXDT.put(M_rstRSSET.getString("TXT_PRDCD")+"|"+M_rstRSSET.getString("TXT_CODDS"),(M_rstRSSET.getString("TXT_CODFL")).equals("%")==true ? "" : "Rs. "+ M_rstRSSET.getString("TXT_CODVL")+"|"+(M_rstRSSET.getString("TXT_CODFL").equals("R")==true ? "%" : "RS. "));
			*/
	// FETCHING REMARK DETAILS AND PUTTING IN BASE DETAILS HASH TABLE
			
			//strEXCRT = getTXDOC("MR",txtMKTTP.getText(),"OCF",txtOCFNO.getText(),"XXXXXXXXXX","EXCVL");
			//strEDCRT = getTXDOC("MR",txtMKTTP.getText(),"OCF",txtOCFNO.getText(),"XXXXXXXXXX","EDCVL");
			//M_rstRSSET=cl_dat.exeSQLQRY("Select * from MR_RMMST where RM_TRNTP IN ('IN','IR','OR') and RM_DOCNO='"+txtOCFNO.getText().toUpperCase()+"' and RM_STSFL<>'X'");
			M_rstRSSET=cl_dat.exeSQLQRY("Select * from MR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP IN ('OR') and RM_DOCNO='"+txtOCFNO.getText().toUpperCase()+"' and RM_STSFL<>'X'");
			//System.out.println("014");
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					M_rstRSSET.getString("RM_TRNTP").equals("OR");
						hstBASDT.put("OC_BKGRM",M_rstRSSET.getString("RM_REMDS"));
					//if(M_rstRSSET.getString("RM_TRNTP").equals("IR"))
					//	hstBASDT.put("OC_REGRM",M_rstRSSET.getString("RM_REMDS"));
					//else if(M_rstRSSET.getString("RM_TRNTP").equals("OR"))
					//	hstBASDT.put("OC_BKGRM",M_rstRSSET.getString("RM_REMDS"));
					//else
					//	hstBASDT.put("OC_AUTRM",M_rstRSSET.getString("RM_REMDS"));
				}
			}
			
	//WRITING TO FILE
			File filREPORT=null;
			strRPTFL = cl_dat.M_strREPSTR_pbst+txtOCFNO.getText();
			if(M_rdbTEXT.isSelected())
				filREPORT=new File(strRPTFL+".doc");
			else
				filREPORT=new File(strRPTFL+".html");
			FileOutputStream filOUT=new FileOutputStream(filREPORT);
			dosREPORT=new DataOutputStream(filOUT);
		//	if(M_rdbHTML.isSelected())
		//		dosREPORT.writeBytes("<pre>");
			M_intPAGNO=0;
			String L_strPAD="";
			filOUT.flush();
			dosREPORT.flush();
			dosREPORT.writeBytes("<HTML>");
			prnHEADR();
//********************************************************************
// BLOCK WHICH PRINTS GRADE DETAILS STARTS HERE
//********************************************************************
			//crtTBL(dosREPORT,false,100,'C',true);
			prnFMTCHR(dosREPORT,M_strBOLD);
			//dosREPORT.writeBytes(padSTRING('R',"Grade",15)+padSTRING('R',"Qty[MT]",13)+padSTRING('R',"Package Type",20)+padSTRING('L',"Pkg.Wt.",8)+padSTRING('L',"Packs",7)+padSTRING('L',"Rate/MT",10));
			dosREPORT.writeBytes(padSTRING('R',"Grade",15)+padSTRING('R',"Qty[MT]",13)+padSTRING('R',"Package Type",20)+padSTRING('R',"Rate/MT",10)+padSTRING('R',"Commission(USD/PMT)",20));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
//			if(M_rdbTEXT.isS
			//endTABLE(dosREPORT);
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
			//crtHRLIN(dosREPORT,"-",90);
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
		//WRITING GRADE DETAILS
			//System.out.println("015");
			String L_strTEMP=null;
			String L_strTEMP1=null;
			String L_strCOMRT="";
			StringTokenizer L_stkTEMP=null;
			Enumeration L_enmTEMP=hstGRDDT.keys();			
			while(L_enmTEMP.hasMoreElements())
			{
				L_strTEMP=L_enmTEMP.nextElement().toString();
				int i=L_strTEMP.length();
				L_strTEMP1 = L_strTEMP.substring(0,i-2);
				L_stkTEMP=new StringTokenizer(hstGRDDT.get(L_strTEMP).toString(),"|");
				dosREPORT.writeBytes(padSTRING('R',L_strTEMP1,15));//5 GRADE
				dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),8));//2 QTY
				dosREPORT.writeBytes("     ");
				dosREPORT.writeBytes(padSTRING('R',L_stkTEMP.nextToken(),20));//PACKAGE TYPE DESCRIPTION
				//dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),8));//2 PKGWT
				//dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),7));//PACKS
				L_stkTEMP.nextToken();
				L_stkTEMP.nextToken();
				//System.out.println(L_stkTEMP.nextToken());
				dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),10));// RATE
				//dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),8));// CDSC
				//dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),8));//DDSC
				String L_strPRDCD = L_stkTEMP.nextToken();
				System.out.println(L_strPRDCD.substring(0,2));
				if((L_strPRDCD).substring(0,2).equals("51"))
				{
					
					String L_strSQLQRY =  "select CMT_NCSVL from CO_CDTRN WHERE CMT_CGMTP='SYS'  AND CMT_CGSTP='MRXXDCM' AND CMT_CODCD='D"+strDSRCD+"_XXXXX_07' ";
					System.out.println("select CMT_NCSVL from CO_CDTRN WHERE CMT_CGMTP='SYS'  AND CMT_CGSTP='MRXXDCM' AND CMT_CODCD='D"+strDSRCD+"_XXXXX_07' ");
					
						//AND CMT_CODCD='D+'"+M_rstRSSET.getString("OC_DSRCD")+"'+'_XXXXX_07'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
					if(L_rstRSSET != null && L_rstRSSET.next())
						{L_strCOMRT = L_rstRSSET.getString("CMT_NCSVL");      L_rstRSSET.close();}
					else
						JOptionPane.showMessageDialog(this,"Commission Category not found for MRXXDCM / D"+strDSRCD+"_XXXXX_07  Contact Systems");
					System.out.println(L_strCOMRT);
				}
				if((L_strPRDCD).substring(0,2).equals("52"))
				{
					String L_strSQLQRY =  "select CMT_NCSVL from CO_CDTRN WHERE CMT_CGMTP='SYS'  AND CMT_CGSTP='MRXXDCM' AND CMT_CODCD='D"+strDSRCD+"_XXXXX_08' ";
					//String L_strSQLQRY = "select CMT_NCSVL from CO_CDTRN WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='MRXXDCM' AND CMT_CODCD='DC0004_XXXXX_08' ";
						//AND CMT_CODCD='D+'"+M_rstRSSET.getString("OC_DSRCD")+"'+'_XXXXX_07'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
					if(L_rstRSSET != null && L_rstRSSET.next())
						{L_strCOMRT = L_rstRSSET.getString("CMT_NCSVL");      L_rstRSSET.close();}
					else
						JOptionPane.showMessageDialog(this,"Commission Category not found for MRXXDCM / D"+strDSRCD+"_XXXXX_08  Contact Systems");
					//if(L_rstRSSET == null || !L_rstRSSET.next())
					//{
					//	setMSG("Commission Category not found in CO_CDTRN",'E');
					//}
					//L_strCOMRT = L_rstRSSET.getString("CMT_NCSVL");
		            //L_rstRSSET.close();
				}
				if((L_strPRDCD).substring(0,2).equals("54"))
				{
					String L_strSQLQRY =  "select CMT_NCSVL from CO_CDTRN WHERE CMT_CGMTP='SYS'  AND CMT_CGSTP='MRXXDCM' AND CMT_CODCD='D"+strDSRCD+"_XXXXX_09' ";
					//String L_strSQLQRY = "select CMT_NCSVL from CO_CDTRN WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='MRXXDCM' AND CMT_CODCD='DC0004_XXXXX_09' ";
						//AND CMT_CODCD='D+'"+M_rstRSSET.getString("OC_DSRCD")+"'+'_XXXXX_07'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
					if(L_rstRSSET != null && L_rstRSSET.next())
						{L_strCOMRT = L_rstRSSET.getString("CMT_NCSVL");      L_rstRSSET.close();}
					else
						JOptionPane.showMessageDialog(this,"Commission Category not found for MRXXDCM / D"+strDSRCD+"_XXXXX_09  Contact Systems");
					//if(L_rstRSSET == null || !L_rstRSSET.next())
					//{
					//	setMSG("Commission Category not found in CO_CDTRN",'E');
					//}
					//L_strCOMRT = L_rstRSSET.getString("CMT_NCSVL");
		            //L_rstRSSET.close();
				}
				dosREPORT.writeBytes(padSTRING('L',L_strCOMRT,10));// COMMISSION RATE
				dosREPORT.writeBytes("\n");
				String L_strDELTP ="";
				/**Enumeration L_enmDLSCH=hstDLSCH.keys();
				String L_strSRLNO="";
				Hashtable hstDSHORD = new Hashtable(5);
				while(L_enmDLSCH.hasMoreElements())
				{
				    String L_strDLSCH=(String)L_enmDLSCH.nextElement();
				    if(L_strDLSCH.substring(0,10).equals(L_strPRDCD))
				    {
				        hstDSHORD.put(L_strDLSCH.substring(11),(String)hstDLSCH.get(L_strDLSCH));
				    }
				}
				for(int i=1;i<=hstDSHORD.size();i++)
				{
				    if(hstDSHORD.containsKey("0"+i))
				    {
    				    L_stkTEMP=new StringTokenizer((String)hstDSHORD.get("0"+i),"|");
					  dosREPORT.writeBytes(padSTRING('R'," ",15));
    				    dosREPORT.writeBytes(padSTRING('R'," ",8));
    				    dosREPORT.writeBytes(padSTRING('R'," ",8));
    				    dosREPORT.writeBytes(padSTRING('R'," ",7));
    				    dosREPORT.writeBytes(padSTRING('R'," ",10));
    				    dosREPORT.writeBytes(padSTRING('R'," ",8));
    				    dosREPORT.writeBytes(padSTRING('R'," ",12));
    				    L_strDELTP =L_stkTEMP.nextToken();
    				
    				    dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),8));
    				    dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),13));
						dosREPORT.writeBytes("\n");
				    }
				}*/
				// Old logic of DLSCH printing , order was not getting maintained
				// changed on 23/09/2006
				/*while(L_enmDLSCH.hasMoreElements())
				{
				    String L_strDLSCH=(String)L_enmDLSCH.nextElement();
				    if(L_strDLSCH.substring(0,10).equals(L_strPRDCD))
				    {
				        L_stkTEMP=new StringTokenizer((String)hstDLSCH.get(L_strDLSCH),"|");
				        dosREPORT.writeBytes(padSTRING('R'," ",15));
				        dosREPORT.writeBytes(padSTRING('R'," ",8));
				        dosREPORT.writeBytes(padSTRING('R'," ",8));
				        dosREPORT.writeBytes(padSTRING('R'," ",7));
				        dosREPORT.writeBytes(padSTRING('R'," ",10));
				        dosREPORT.writeBytes(padSTRING('R'," ",8));
				        dosREPORT.writeBytes(padSTRING('R'," ",12));
				        L_strDELTP =L_stkTEMP.nextToken();
				
				        dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),8));
				        dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),13));

				
				        prnNWLIN(dosREPORT);
				    }
				}*/
				Enumeration L_enmTAXDT=hstTAXDT.keys();
			//WRITING GRADE WISE TAX DETAILS JUST AFTER THE CORRESPONDING GRADE DETAILS
				//System.out.println("016");
				while(L_enmTAXDT.hasMoreElements())
				{
					String L_strTAXDT=(String)L_enmTAXDT.nextElement();
					L_strTEMP="  ";
					if(M_rdbHTML.isSelected())
						L_strTEMP="-";
					if(L_strTAXDT.substring(0,10).equals(L_strPRDCD))
					{
						setTXCLR(dosREPORT,Color.green);
						L_stkTEMP=new StringTokenizer((String)hstTAXDT.get(L_strTAXDT),"|");
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,3));
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,3));
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,4));
						
						dosREPORT.writeBytes(padSTRING('L',hstTXCAT.get(L_strTAXDT.substring(11)).toString(),30));
						dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),20));
						dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),5));
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes(padSTRING('L',"-",5));
						dosREPORT.writeBytes("\n");
						//prnNWLIN(dosREPORT);
						hstTAXDT.remove(L_strTAXDT);
					}
				}
				setTXCLR(dosREPORT,Color.black);
			}
			
			//endTABLE(dosREPORT);
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
			//crtHRLIN(dosREPORT,"-",90);
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
//********************************************************************
// BLOCK WHICH PRINTS GRADE DETAILS ENDS HERE
//********************************************************************
//*****************************************************************************
// BLOCK WHICH PRINTS DELIVERY TYPE,PAYMENT TERMS ,TRANSPORTER ETC. STARTS HERE
//*****************************************************************************
			//crtTBL(dosREPORT,false,100,'C',true);
prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Sale Type ",14)+": ");
			//else
			//	dosREPORT.writeBytes(padSTRING('R',"Sale Type",13));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(hstBASDT.containsKey("OC_SALDS"))
				dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_SALDS").toString(),33));
			prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"P.O. No. ",19)+": ");
			//else
			//{
				/*L_strPAD="<p Align = left>"+strTXFMT+strTXCLR+"P.O. No."+"</font>"+"</P>";
				if(flgTBLDT)
					L_strPAD="<td width = \"153\">"+strTXFMT+strTXCLR+L_strPAD+"</font>"+"</td>";*/
				//dosREPORT.writeBytes(padSTRING('R',"P.O. No.",18));
			//}
			
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			strTXFMT = "<FONT Size = 4>";
			if(hstBASDT.containsKey("OC_PORNO"))
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(hstBASDT.get("OC_PORNO").toString(),""),9));
				//dosREPORT.writeBytes(nvlSTRVL(hstBASDT.get("IN_PORNO").toString(),""));
			strTXFMT = "<FONT Size = 4>";
			if(hstBASDT.containsKey("OC_PORDT"))
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(hstBASDT.get("OC_PORDT").toString(),""),10));
			strTXFMT = "<FONT Size = 6>";
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Payment Terms ",14)+": ");
			//else
			//	dosREPORT.writeBytes(padSTRING('R',"Payment Terms",13));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(hstBASDT.containsKey("OC_PMTDS"))
				dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_PMTDS").toString(),33));
			prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Cr.Days (Inv/Acct) ",19)+": ");
			//else
			//	dosREPORT.writeBytes(padSTRING('R',"Cr.Days (Inv/Acct)",18));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			//System.out.println("strSALTP : "+strSALTP);
			//System.out.println("strSALTP_STF : "+strSALTP_STF);
			if(hstBASDT.containsKey("OC_CPTVL"))
				dosREPORT.writeBytes(padSTRING('R',(strSALTP.trim().equals(strSALTP_STF) ? "- " : nvlSTRVL(hstBASDT.get("OC_CPTVL").toString(),"-")+"/"+nvlSTRVL(hstBASDT.get("OC_CPTVL").toString(),"-")),7));
				//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(hstBASDT.get("IN_CPTVL").toString(),"-")+"/"+nvlSTRVL(hstBASDT.get("IN_CPTVL").toString(),"-"),7));
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Delivery Type ",14)+": ");
			//else
			//	dosREPORT.writeBytes(padSTRING('R',"Delivery Type",13));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(hstBASDT.containsKey("OC_DTPDS"))
			{
				//if(txtOCFNO.getText().substring(0,3).equalsIgnoreCase("XPT"))
					dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_DTPDS").toString()+" ("+strDSTNM+")",33));
				//else
				//	dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_DTPDS").toString(),33));
			}
			prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
				//dosREPORT.writeBytes(padSTRING('R',"Transport Type ",19)+": ");
			//else
			//{
			    if(hstBASDT.containsKey("OC_MOTDS"))
					dosREPORT.writeBytes(padSTRING('R',"Transport Type     :",23));
			//}
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(hstBASDT.containsKey("OC_MOTDS"))
			dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_MOTDS").toString(),23));
			////
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
			//dosREPORT.writeBytes(padSTRING('R',"Transporter   :",14)+": ");
			//else
			//{	
			    if(hstBASDT.containsKey("OC_DTPDS"))
			    {
			        //System.out.println(hstBASDT.get("IN_FORRF").toString());
			        if(hstBASDT.get("OC_DTPDS").toString().equals("F.O.R. DESTN."))
			        {
			            dosREPORT.writeBytes(padSTRING('R',"FOR Reference",13));
			            prnFMTCHR(dosREPORT,M_strNOBOLD);
   			            if(hstBASDT.containsKey("OC_FORRF"))
            			{
            				dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_FORRF").toString(),33));
            			}

			        }
			        else
			        {
						dosREPORT.writeBytes(padSTRING('R',"Transporter   : ",17));
			            prnFMTCHR(dosREPORT,M_strNOBOLD);
			            if(hstBASDT.containsKey("OC_TRPNM"))
            			{
            				dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_TRPNM").toString(),32));
            			}
			        }
			    }
    		
    			
			//}
			prnFMTCHR(dosREPORT,M_strBOLD);
			//if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Doc. Reference ",18)+" : ");
			//else
			//	dosREPORT.writeBytes(padSTRING('R',"Doc. Reference",18));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_PMTRF").toString(),25));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Shipment Date : ",16));
			if(hstBASDT.containsKey("OC_ASHDT"))
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(hstBASDT.get("OC_ASHDT").toString(),""),33));
			dosREPORT.writeBytes(padSTRING('R',"Last Dt.of Shipment: ",21));
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(hstBASDT.get("OC_LSHDT").toString(),""),10));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"LC To be Estb.: ",16));
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(hstBASDT.get("OC_LCEDT").toString(),""),10));

			////
			dosREPORT.writeBytes("\n");
			
			//prnNWLIN(dosREPORT);
			//dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			//endTABLE(dosREPORT);
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
			//crtHRLIN(dosREPORT,"-",90);
			//dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
//***************************************************************************
// BLOCK WHICH PRINTS DELIVERY TYPE,PAYMENT TERMS ,TRANSPORTER ETC. ENDS HERE
//***************************************************************************
			
//********************************************************************
// BLOCK WHICH PRINTS TAXES, REMARKS  ETC. STARTS HERE
//********************************************************************
			Enumeration L_enmTAXDT=hstTAXDT.keys();
			//crtTBL(dosREPORT,false,100,'C',true);
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"Taxes Applicable :",19));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			//endTABLE(dosREPORT);
			if(M_rdbTEXT.isSelected())
				prnFMTCHR(dosREPORT,M_strCPI10);
			//crtTBL(dosREPORT,false,50,'C',true);
			//dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
		//WRITING DOCUMENT LEVEL TAX DETAILS
			//System.out.println("017");
			//System.out.println("017a");
			flgCOTAX = true; intTXFLD = 0;
			flgSPTAX = true;
			while(flgCOTAX)
				chkTAXPRN_CO(txtMKTTP.getText(),txtOCFNO.getText());
			chkTAXPRN_SP(txtMKTTP.getText(),txtOCFNO.getText(),"EX1");
			chkTAXPRN_SP(txtMKTTP.getText(),txtOCFNO.getText(),"EX2");
			chkTAXPRN_SP(txtMKTTP.getText(),txtOCFNO.getText(),"EX3");
			chkTAXPRN_SP(txtMKTTP.getText(),txtOCFNO.getText(),"EX4");
			chkTAXPRN_SP(txtMKTTP.getText(),txtOCFNO.getText(),"EX5");
			chkTAXPRN_SP(txtMKTTP.getText(),txtOCFNO.getText(),"EX6");
			chkTAXPRN_SP(txtMKTTP.getText(),txtOCFNO.getText(),"EX7");
			//dosREPORT.writeBytes(padSTRING('R',Float.parseFloat(strEXCRT)>0 ? "Excise Duty : "+strEXCRT+"%      " : "",30));
			//dosREPORT.writeBytes(padSTRING('R',Float.parseFloat(strEDCRT)>0 ? "Ed. Cess    : "+strEDCRT+"%      " : "",30));
			//prnNWLIN(dosREPORT);
			/*while(L_enmTAXDT.hasMoreElements())
			{
				System.out.println("017a_1");
				String L_strTAXDT=(String)L_enmTAXDT.nextElement();
				System.out.println("017a_2");
				L_stkTEMP=new StringTokenizer((String)hstTAXDT.get(L_strTAXDT),"|");
				//System.out.println("017a_3");
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(hstTXCAT.get(L_strTAXDT.substring(11)).toString(),""),30));
				//System.out.println("017a_4");
				dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),20));
				//System.out.println("017a_5");
				dosREPORT.writeBytes(padSTRING('L',L_stkTEMP.nextToken(),5));
				//System.out.println("017a_6");
				prnNWLIN(dosREPORT);
				//System.out.println("017a_7");
				hstTAXDT.remove(L_strTAXDT);
				//System.out.println("017a_8");
				//System.out.println("017b");
			}
			*/
			//endTABLE(dosREPORT);
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
			//crtHRLIN(dosREPORT,"-",90);
			//dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			//crtTBL(dosREPORT,false,100,'C',true);
			prnFMTCHR(dosREPORT,M_strCPI10);
			//System.out.println("017c");
			if(hstBASDT.containsKey("OC_REGRM"))
			//System.out.println("1008");						
			{//PRINTING REGISTRATION REMARK
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("Registration Remark : ");
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				L_strTEMP=(String)hstBASDT.get("OC_REGRM");
				dosREPORT.writeBytes(padSTRING('R',L_strTEMP,52));
				//System.out.println("017d");
				dosREPORT.writeBytes("\n");
				//prnNWLIN(dosREPORT);
				if(L_strTEMP.length()>52)
				{
					L_strTEMP=L_strTEMP.substring(52);
					dosREPORT.writeBytes(padSTRING('R',L_strTEMP,74));
					dosREPORT.writeBytes("\n");
					//prnNWLIN(dosREPORT);
					//System.out.println("017e");
					if(L_strTEMP.length()>74)
					{
						L_strTEMP=L_strTEMP.substring(74);
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,74));
						dosREPORT.writeBytes("\n");
						//prnNWLIN(dosREPORT);
					}
				}
			}	
			//System.out.println("018");
			if(hstBASDT.containsKey("OC_BKGRM"))
			{//PRINTING BOOKING REMARK
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("Booking Remark : ");
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				L_strTEMP=(String)hstBASDT.get("OC_BKGRM");
				System.out.println(L_strTEMP);
				dosREPORT.writeBytes(padSTRING('R',L_strTEMP,52));
				dosREPORT.writeBytes("\n");
				//prnNWLIN(dosREPORT);
				if(L_strTEMP.length()>52)
				{
					L_strTEMP=L_strTEMP.substring(52);
					dosREPORT.writeBytes(padSTRING('R',L_strTEMP,74));
					dosREPORT.writeBytes("\n");
					//prnNWLIN(dosREPORT);
					if(L_strTEMP.length()>74)
					{
						L_strTEMP=L_strTEMP.substring(74);
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,74));
						dosREPORT.writeBytes("\n");
						//prnNWLIN(dosREPORT);
					}
				}
			}
			
			if(hstBASDT.containsKey("OC_AUTRM"))
			{//PRINTING SP. AUTHORISATION REMARK
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("Booking Remark : ");
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				L_strTEMP=(String)hstBASDT.get("OC_REGRM");
				dosREPORT.writeBytes(padSTRING('R',L_strTEMP,50));
				if(L_strTEMP.length()>50)
					L_strTEMP=L_strTEMP.substring(50);
				dosREPORT.writeBytes(padSTRING('R',L_strTEMP,70));
			}
			//if(M_rdbTEXT.isSelected())
			//{
			//	for(int i=M_intLINNO;M_intLINNO<35;i++)
			//		dosREPORT.writeBytes("\n");
					//prnNWLIN(dosREPORT);
			//}
			//else
			//{
			//	//endTABLE(dosREPORT);

			//	dosREPORT.writeBytes("\n");
			//	//prnNWLIN();prnNWLIN();
			//}
			//System.out.println("019");
			prnFOOTR();
			prnFMTCHR(dosREPORT,M_strEJT);
			filOUT.flush();
			dosREPORT.flush();
			dosREPORT.close();
			//System.out.println("020");
			strRPTFL = cl_dat.M_strREPSTR_pbst+txtOCFNO.getText();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) || flgOUTPRN)
			{
				//System.out.println("021");
				if(M_rdbTEXT.isSelected())
					{doPRINT(strRPTFL+".doc");}
				else if(M_rdbHTML.isSelected())
					{prcREPORT  = Runtime.getRuntime().exec("c:\\program files\\internet explorer\\iexplore.exe "+strRPTFL+".html");}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst))
			{
				if(M_rdbTEXT.isSelected())
					prcREPORT  = Runtime.getRuntime().exec("c:\\windows\\wordpad.exe "+cl_dat.M_strREPSTR_pbst+txtOCFNO.getText()+".doc");
				else if(M_rdbHTML.isSelected())
					prcREPORT  = Runtime.getRuntime().exec("c:\\program files\\internet explorer\\iexplore.exe "+strRPTFL+".html");
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst))
			{
			    cl_eml ocl_eml = new cl_eml();	
				System.out.println("ocl_eml1");
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPTFL+".html","Order Confirmation No."," ");
					System.out.println(M_cmbDESTN.getItemAt(i).toString().trim()+" "+strRPTFL);
					System.out.println("ocl_eml2");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					System.out.println("ocl_eml3");
				}
			}
		}catch(Exception e){setMSG(e,"Child.exePRINT");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
//********************************************************************
// BLOCK WHICH PRINTS TAXES,REMARKS ETC. ENDS HERE
//********************************************************************
	
	/**To print new line character<br>Increments line no, checks for end of page and if end of page has occured, prints footer and header	 */
	private void prnNWLIN() throws Exception
	{
		dosREPORT.writeBytes("\n");
		if(intLINCT!=-1)
			intLINCT++;
		M_intLINNO++;
		if(M_intLINNO>52)
		{
			prnFOOTR();
			prnFMTCHR(dosREPORT,M_strEJT);
			prnHEADR();
		}
	}
	/**To print footer	 */
	private void prnFOOTR() throws Exception
	{
		prnFMTCHR(dosREPORT,M_strCPI10);
		prnFMTCHR(dosREPORT,M_strBOLD);
		//dosREPORT.writeBytes(padSTRING('R',"For SPL use Only",50));
		//endTABLE(dosREPORT);
		//prnNWLIN(dosREPORT);
		dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
		//crtHRLIN(dosREPORT,"-",90);
		//prnNWLIN(dosREPORT);
		dosREPORT.writeBytes("\n");
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		//crtTBL(dosREPORT,false,100,'C',true);
		//if(M_rdbTEXT.isSelected())
		//{
			dosREPORT.writeBytes(padSTRING('R',"Registered By ",14)+": ");
			dosREPORT.writeBytes(padSTRING('R',strREGBY,34));
			dosREPORT.writeBytes(padSTRING('R',"Booked By ",14)+": ");
			dosREPORT.writeBytes(padSTRING('R',strBKGBY,34));
			//dosREPORT.writeBytes(padSTRING('R',padSTRING('R',"Booked By ",18)+":"+strBKGBY,40));
			//prnNWLIN(dosREPORT);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			dosREPORT.writeBytes(padSTRING('R',"Authorised By ",14)+": ");
			dosREPORT.writeBytes(padSTRING('R',strRVWBY,34));
			//dosREPORT.writeBytes(padSTRING('R',padSTRING('R',"AuthorisedReviewed By",16)+":"+strRVWBY,50));
			//dosREPORT.writeBytes(padSTRING('R',padSTRING('R',"D.O. No. / Date",20)+":"+strDORNO+"  "+strDORDT,40));
			//prnNWLIN(dosREPORT);
			//prnNWLIN(dosREPORT);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
		//}
		//else
		//{
		//	dosREPORT.writeBytes("<td><font aling = right>Registered By");
		//	dosREPORT.writeBytes(padSTRING('R',strREGBY,40));
		//	dosREPORT.writeBytes("<td><font aling = right>Booked By");
		//	dosREPORT.writeBytes(padSTRING('R',strBKGBY,40));
			//prnNWLIN(dosREPORT);
			//prnNWLIN(dosREPORT);
		//	dosREPORT.writeBytes("\n");
		//	dosREPORT.writeBytes("\n");
		//	dosREPORT.writeBytes("<td><font aling = right>Reviewed By");
		//	dosREPORT.writeBytes(padSTRING('R',strRVWBY,40));
		//	dosREPORT.writeBytes("<td><font aling = right>D.O. No. / Date");
			//dosREPORT.writeBytes(padSTRING('R',strDORNO,40));
		//	dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
		//}
	}
	/**To print header	 */
	private void prnHEADR() throws Exception
	{
		if(M_rdbTEXT.isSelected())
		{
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			dosREPORT.writeBytes(padSTRING('C',strREPNM,80)+"\n\n\n");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			prnFMTCHR(dosREPORT,M_strCPI17);
			//dosREPORT.writeBytes(padSTRING('L',strISO1_fn,130)+"\n");
			//dosREPORT.writeBytes(padSTRING('L',strISO2_fn,130)+"\n");
			//dosREPORT.writeBytes(padSTRING('L',strISO3_fn,131)+"\n");
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Andheri (E), Mumbai - 400 093.",77)+"Page No. : "+Integer.toString(++M_intPAGNO)+"\n");
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes("\n");
			//crtHRLIN(dosREPORT,"-",90);
			//prnNWLIN(dosREPORT);
		}
		else
		{
				//dosREPORT.writeBytes("<HTML><HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD>");
			java.net.URL imageURL = mr_rpocn.class.getResource("spllogo_old.gif");
			System.out.println("image URL "+imageURL);
			if(imageURL !=null)
		       // dosREPORT.writeBytes("<HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD><BODY bgColor=ghostwhite><P><UL>  <LI>  <DIV align=right><FONT size=1>"+strISO1_fn+"</FONT></DIV>  <LI>  <DIV align=right><FONT size=1>"+strISO2_fn+"&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>  <LI>  <DIV align=right><FONT size=1>"+strISO3_fn+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>    </UL><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\">  <TBODY> <TR><TD><IMG src=\" "+imageURL.toString() +"\" style=\"HEIGHT: 90px; LEFT: 0px; TOP: 0px; WIDTH: 86px\"></TD><TD style=\"LINE-HEIGHT: 1\"><P align=left><FONT color=black><FONT face=Arial size=5><STRONG>SUPREME PETROCHEM LTD.</STRONG>  </FONT></FONT></P>      <P align=left style=\"LINE-HEIGHT: 0.1\"><FONT                     face=Arial size=1>17 / 18, Shah Industrial&nbsp;Estate, Veera Desai Road,       Andheri (West), Mumbai - 400 053</FONT></P>      <P align=left style=\"LINE-HEIGHT: 0; MARGIN-TOP: 0px\"      ><FONT size=3><FONT face=Arial><FONT       face=Wingdings size=4><STRONG>)</STRONG></FONT><FONT size=1><FONT face=Webdings size=2>       </FONT>91 - 22 - 26734789/26736196-99; Fax : 91 - 22 - 26734788</FONT></FONT></FONT></P><FONT size=+0><FONT       face=Arial></FONT><FONT size=5><p align=left><STRONG style=\"LINE-HEIGHT: 1.8\"       >Customer       Order</STRONG></FONT></FONT></P></TD><TD>      <P><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</P><p><FONT face=Arial size=2>Page No. : 1</p></FONT></FONT><TD></TD></TR></TBODY></TABLE><HR><FONT face=\"Comic Sans MS\"></STRONG><p></p>");
			{	
                //dosREPORT.writeBytes("<HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD><BODY bgColor=ghostwhite><P><UL>  <LI>  <DIV align=right><FONT size=1>"+strISO1_fn+"</FONT></DIV>  <LI>  <DIV align=right><FONT size=1>"+strISO2_fn+"&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>  <LI>  <DIV align=right><FONT size=1>"+strISO3_fn+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>    </UL><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\">  <TBODY> <TR><TD><IMG src=\" "+imageURL.toString() +"\" style=\"HEIGHT: 90px; LEFT: 0px; TOP: 0px; WIDTH: 86px\"></TD><TD style=\"LINE-HEIGHT: 1\"><P align=left><FONT color=black><FONT face=Arial size=5><STRONG>SUPREME PETROCHEM LTD.</STRONG>  </FONT></FONT></P>      <P align=left style=\"LINE-HEIGHT: 0.1\"><FONT                     face=Arial size=1>Bldg. No. 11 5th Floor Solitair Corporate Park, Chakala Andheri(East), Mumbai - 400 093</FONT></P>      <P align=left style=\"LINE-HEIGHT: 0; MARGIN-TOP: 0px\"      ><FONT size=3><FONT face=Arial><FONT       face=Wingdings size=4><STRONG>)</STRONG></FONT><FONT size=1><FONT face=Webdings size=2>       </FONT>91 - 22 - 67091900-04 Fax : 67091927/26/25/28</FONT></FONT></FONT></P><FONT size=+0><FONT       face=Arial></FONT><FONT size=5><p align=left><STRONG style=\"LINE-HEIGHT: 1.8\"       >Order     Confirmation</STRONG></FONT></FONT></P></TD><TD>      <P><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</P><p><FONT face=Arial size=2>Page No. : 1</p></FONT></FONT><TD></TD></TR></TBODY></TABLE><HR><FONT face=\"Comic Sans MS\"></STRONG><p></p>");
                dosREPORT.writeBytes("<HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD><BODY bgColor=ghostwhite><P><UL>  <DIV align=right><FONT size=1>"+"</FONT></DIV>  <DIV align=right><FONT size=1>"+"&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>  <DIV align=right><FONT size=1>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>    </UL><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\">  <TBODY> <TR><TD><IMG src=\" "+imageURL.toString() +"\" style=\"HEIGHT: 90px; LEFT: 0px; TOP: 0px; WIDTH: 86px\"></TD><TD style=\"LINE-HEIGHT: 1\"><P align=left><FONT color=black><FONT face=Arial size=5><STRONG>"+cl_dat.M_strCMPNM_pbst+"</STRONG>  </FONT></FONT></P>      <P align=left style=\"LINE-HEIGHT: 0.1\"><FONT                     face=Arial size=1>Bldg. No. 11 5th Floor Solitair Corporate Park, Chakala Andheri(East), Mumbai - 400 093</FONT></P>      <P align=left style=\"LINE-HEIGHT: 0; MARGIN-TOP: 0px\"      ><FONT size=3><FONT face=Arial><FONT       face=Wingdings size=4><STRONG>)</STRONG></FONT><FONT size=1><FONT face=Webdings size=2>       </FONT>91 - 22 - 67091900-04 Fax : 67091927/26/25/28</FONT></FONT></FONT></P><FONT size=+0><FONT       face=Arial></FONT><FONT size=5><p align=left><STRONG style=\"LINE-HEIGHT: 1.8\"       >Order     Confirmation</STRONG></FONT></FONT></P></TD><TD>      <P><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</P><p><FONT face=Arial size=2>Page No. : 1</p></FONT></FONT><TD></TD></TR></TBODY></TABLE><HR><FONT face=\"Comic Sans MS\"></STRONG><p></p>");
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Order Confirmation Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}	
		    else
			{	
                dosREPORT.writeBytes("<HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD><BODY bgColor=ghostwhite><P><UL>   <DIV align=right><FONT size=1>"+"</FONT></DIV>   <DIV align=right><FONT size=1>"+"&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>   <DIV align=right><FONT size=1>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>    </UL><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\">  <TBODY> <TR><TD><IMG src=\"file://d://splerp2//spllogo_old.gif\" style=\"HEIGHT: 90px; LEFT: 0px; TOP: 0px; WIDTH: 86px\"></TD><TD style=\"LINE-HEIGHT: 1\"><P align=left><FONT color=black><FONT face=Arial size=5><STRONG>"+cl_dat.M_strCMPNM_pbst+"</STRONG>  </FONT></FONT></P>      <P align=left style=\"LINE-HEIGHT: 0.1\"><FONT                     face=Arial size=1>Bldg. No. 11 5th Floor Solitair Corporate Park, Chakala Andheri(East), Mumbai - 400 093</FONT></P>      <P align=left style=\"LINE-HEIGHT: 0; MARGIN-TOP: 0px\"      ><FONT size=3><FONT face=Arial><FONT       face=Wingdings size=4><STRONG>)</STRONG></FONT><FONT size=1><FONT face=Webdings size=2>       </FONT>91 - 22 - 67091900-04 Fax : 67091927/26/25/28</FONT></FONT></FONT></P><FONT size=+0><FONT       face=Arial></FONT><FONT size=5><p align=left><STRONG style=\"LINE-HEIGHT: 1.8\"       >Order     Confirmation</STRONG></FONT></FONT></P></TD><TD>      <P><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</P><p><FONT face=Arial size=2>Page No. : 1</p></FONT></FONT><TD></TD></TR></TBODY></TABLE><HR><FONT face=\"Comic Sans MS\"></STRONG><p></p>");
		        //dosREPORT.writeBytes("<HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD><BODY bgColor=ghostwhite><P><UL>  <LI>  <DIV align=right><FONT size=1>"+strISO1_fn+"</FONT></DIV>  <LI>  <DIV align=right><FONT size=1>"+strISO2_fn+"&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>  <LI>  <DIV align=right><FONT size=1>"+strISO3_fn+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</FONT></DIV>    </UL><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\">  <TBODY> <TR><TD><IMG src=\"file://d://splerp2//spllogo_old.gif\" style=\"HEIGHT: 90px; LEFT: 0px; TOP: 0px; WIDTH: 86px\"></TD><TD style=\"LINE-HEIGHT: 1\"><P align=left><FONT color=black><FONT face=Arial size=5><STRONG>SUPREME PETROCHEM LTD.</STRONG>  </FONT></FONT></P>      <P align=left style=\"LINE-HEIGHT: 0.1\"><FONT                     face=Arial size=1>17 / 18, Shah Industrial&nbsp;Estate, Veera Desai Road,       Andheri (West), Mumbai - 400 053</FONT></P>      <P align=left style=\"LINE-HEIGHT: 0; MARGIN-TOP: 0px\"      ><FONT size=3><FONT face=Arial><FONT       face=Wingdings size=4><STRONG>)</STRONG></FONT><FONT size=1><FONT face=Webdings size=2>       </FONT>91 - 22 - 26734789/26736196-99; Fax : 91 - 22 - 26734788</FONT></FONT></FONT></P><FONT size=+0><FONT       face=Arial></FONT><FONT size=5><p align=left><STRONG style=\"LINE-HEIGHT: 1.8\"       >Customer       Order</STRONG></FONT></FONT></P></TD><TD>      <P><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</P><p><FONT face=Arial size=2>Page No. : 1</p></FONT></FONT><TD></TD></TR></TBODY></TABLE><HR><FONT face=\"Comic Sans MS\"></STRONG><p></p>");
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Gradewise Price Difference Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}	
		//		dosREPORT.writeBytes("<BODY bgColor=ghostwhite><P><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\"> <TR><TD><IMG src=\"file://f:\\exec\\splerp2\\spllogo_old.gif\" style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></TD><TD><P align=left><STRONG><FONT face=Arial size=5>SUPREME PETROCHEM LTD.</FONT></STRONG></P><STRONG><FONT face=Arial size=4><p align=left> "+"Customer Order"+"</font><STRONG><FONT face=Arial size=3> </TD><TD><p><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</p><p><FONT face=Arial size=2>Page No. : 1</P><TD> </TR></TABLE><HR><FONT face=\"Comic Sans MS\">");
		}
		if(M_strSBSCD.equals("111200"))
		{
			//crtTBL(dosREPORT,false,100,'C',true);
			//dosREPORT.writeBytes(padSTRING('L',"",4));			
			dosREPORT.writeBytes(padSTRING('R',"To",50));
			dosREPORT.writeBytes(padSTRING('R',"From",50));
			//dosREPORT.writeBytes(padSTRING('R',"From",50));
			//prnNWLIN(dosREPORT);
			dosREPORT.writeBytes("\n");
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"Attn.: Mr. C.DAS / Manoj",50));
			dosREPORT.writeBytes(padSTRING('R',"Mr. Umakant D.P.",50));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			dosREPORT.writeBytes(padSTRING('R',"Div. : Export",50));
			dosREPORT.writeBytes(padSTRING('R',"Div. : Export Marketing",50));
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			prnFMTCHR(dosREPORT,M_strBOLD);
			//dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",50));
			//dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",50));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			//prnNWLIN(dosREPORT);
			//dosREPORT.writeBytes(padSTRING('R',"Andheri",50));
			//dosREPORT.writeBytes(padSTRING('R',"Andheri",50));
			//prnNWLIN(dosREPORT);
			//endTABLE(dosREPORT);
			//crtHRLIN(dosREPORT,"-",90);
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			//crtHRLIN(dosREPORT,"-",90);
			//dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
		}
		dosREPORT.writeBytes(padSTRING('R',"Please arrange to issue Proforma Invoice as per following :-",80));
		//prnNWLIN(dosREPORT);
		dosREPORT.writeBytes("\n");
		dosREPORT.writeBytes("\n");
		//prnNWLIN(dosREPORT);
		
		prnFMTCHR(dosREPORT,M_strCPI10);
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		M_intLINNO=7;
		//crtTBL(dosREPORT,false,70,'L',true);
		prnFMTCHR(dosREPORT,M_strCPI10);
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"Market Type :",15));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		dosREPORT.writeBytes(padSTRING('R',getCDTRN("MSTCOXXMKT"+txtMKTTP.getText(),"CMT_CODDS",hstCDTRN),30));
		dosREPORT.writeBytes("\n");
		dosREPORT.writeBytes("\n");
		//prnNWLIN(dosREPORT);
		//prnNWLIN(dosREPORT);
		//dosREPORT.writeBytes("\n");
		prnFMTCHR(dosREPORT,M_strCPI10);
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"Ord.Conf.No.:",15));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		dosREPORT.writeBytes(padSTRING('R',txtMKTTP.getText()+" / "+txtOCFNO.getText(),15));
		dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_OCFDT").toString(),12));
		//prnNWLIN(dosREPORT);
		//dosREPORT.writeBytes("\n");
		dosREPORT.writeBytes("\n");
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"Amdmt No.   :",15));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_AMDNO").toString(),12));
		dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_AMDDT").toString(),12));
		//prnNWLIN(dosREPORT);
		dosREPORT.writeBytes("\n");
		dosREPORT.writeBytes("\n");
		//strSPLRF = hstBASDT.get("PT_SPLRF").toString();	
		/**if(strSPLRF.trim().length() >0)
		{
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"SPL Ref.   :",15)); // Added 
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst+" ( "+strSPLRF+" )",50)); // Added 
			prnNWLIN(dosREPORT);													  // added	
		}*/
		//endTABLE(dosREPORT);
		if(M_rdbTEXT.isSelected())
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
		//crtTBL(dosREPORT,false,100,'C',true);
		prnFMTCHR(dosREPORT,M_strCPI10);
		StringTokenizer L_stkBYRNM=new StringTokenizer(hstBASDT.get("OC_BYRNM").toString(),"|");
		StringTokenizer L_stkCNSNM=(hstBASDT.containsKey("OC_CNSNM")==true ? new StringTokenizer(hstBASDT.get("OC_CNSNM").toString(),"|") : null);
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"Buyer Code  :",15));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_BYRCD").toString(),35));
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"Consignee Code :",20));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		dosREPORT.writeBytes(padSTRING('R',hstBASDT.get("OC_CNSCD").toString(),10));
		dosREPORT.writeBytes("\n");
		//prnNWLIN(dosREPORT);
	//	String L_strSPC=padSTRING('R',"",6);
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"",6));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		dosREPORT.writeBytes(padSTRING('R',L_stkBYRNM.nextToken(),44));
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"",6));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		dosREPORT.writeBytes(padSTRING('R',L_stkCNSNM==null ? "Same as Buyer" : L_stkCNSNM.nextToken(),30));
		dosREPORT.writeBytes("\n");
		//prnNWLIN(dosREPORT);
		dosREPORT.writeBytes(padSTRING('R',"",6));
		dosREPORT.writeBytes(padSTRING('R',L_stkBYRNM.nextToken(),44));
		dosREPORT.writeBytes(padSTRING('R',"",6));
		dosREPORT.writeBytes(padSTRING('R',L_stkCNSNM==null ? "" : L_stkCNSNM.nextToken(),30));
		dosREPORT.writeBytes("\n");
		//prnNWLIN(dosREPORT);
		if(L_stkBYRNM.hasMoreTokens())
		{
			dosREPORT.writeBytes(padSTRING('R',"",6));
			dosREPORT.writeBytes(padSTRING('R',L_stkBYRNM.nextToken(),44));
			dosREPORT.writeBytes(padSTRING('R',"",6));
			if(L_stkCNSNM!=null)
				if(L_stkCNSNM.hasMoreTokens())
					dosREPORT.writeBytes(padSTRING('R',L_stkCNSNM.nextToken(),30));
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
		}
		if(L_stkBYRNM.hasMoreTokens())
		{
			dosREPORT.writeBytes(padSTRING('R',"",6));
			dosREPORT.writeBytes(padSTRING('R',L_stkBYRNM.nextToken(),44));
			dosREPORT.writeBytes(padSTRING('R',"",6));
			if(L_stkCNSNM!=null)
				if(L_stkCNSNM.hasMoreTokens())
					dosREPORT.writeBytes(padSTRING('R',L_stkCNSNM.nextToken(),30));
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
		}
		//prnNWLIN(dosREPORT);
		//endTABLE(dosREPORT);
//		System.out.println(" len "+strDELAD);
		prnFMTCHR(dosREPORT,M_strCPI10);
		prnFMTCHR(dosREPORT,M_strBOLD);
		dosREPORT.writeBytes(padSTRING('R',"Distributor : ",15));
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		//dosREPORT.writeBytes(padSTRING('R',strAGTNM,30));
		dosREPORT.writeBytes(padSTRING('R',strDSRNM,30));
		//dosREPORT.writeBytes(padSTRING('R',"",4));
		//dosREPORT.writeBytes(padSTRING('L',"Commission : ",13));
		
		dosREPORT.writeBytes("\n");
		dosREPORT.writeBytes("\n");
		//prnNWLIN(dosREPORT);
		
		dosREPORT.writeBytes(padSTRING('R',"Order Details : ",16));  // added
		dosREPORT.writeBytes(padSTRING('R',strORDDTL.toString().trim(),60));
		dosREPORT.writeBytes("\n");
		dosREPORT.writeBytes("\n");
		//dosREPORT.writeBytes("\n");
		//prnNWLIN(dosREPORT);
		//prnNWLIN(dosREPORT);
		//prnNWLIN(dosREPORT);
	
		strDELAD = hstBASDT.get("OC_DELAD").toString();		
		// new block added
		if(strDELAD.trim().length() > 0)
		{
			dosREPORT.writeBytes(padSTRING('R',"Delivery Address  : ",20));  // added
			if(strDELAD.length() > 100)
				dosREPORT.writeBytes(strDELAD.substring(0,100));
			else	
				dosREPORT.writeBytes(strDELAD);
		//	System.out.println(" len "+strDELAD.length());
			//prnNWLIN(dosREPORT);								//added
			dosREPORT.writeBytes("\n");
			if(strDELAD.length() > 100)
			{
				dosREPORT.writeBytes(strDELAD.substring(100));
				//prnNWLIN(dosREPORT);								//added
				dosREPORT.writeBytes("\n");
			}
		}
		// end new block added

			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------\n");		
		//crtHRLIN(dosREPORT,"-",90);
		//prnNWLIN(dosREPORT);
		//prnNWLIN(dosREPORT);
		
	}
	/**Empty definition to facilitate print from entry program */
	void exeSAVE(){}
	
	void clrCOMP()
	{
		super.clrCOMP();
		if(prcREPORT!=null)
			prcREPORT.destroy();
		if(txtMKTTP.getText().length()==0)
			txtMKTTP.setText("01");
	}
	/**Overridden to facilitate HTML format	 */
protected  void prnFMTCHR(DataOutputStream L_DOUT,String L_FMTSTR){
		try{
			if(L_FMTSTR.equals(M_strCPI10))
			{
				if(M_rdbHTML.isSelected())
					strTXFMT= "<FONT Size = 6>";
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("P");
				}
			}
			if(L_FMTSTR.equals(M_strCPI12))
			{
				intCOLCT=90;
				if(M_rdbHTML.isSelected())
					strTXFMT="<FONT  Size = 5>";
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("M");
				}
			}
			if(L_FMTSTR.equals(M_strCPI17))
			{
				intCOLCT=145;
				if(M_rdbHTML.isSelected())
					strTXFMT="<FONT Size = 4>";
				else
					L_DOUT.writeChar(15);
			}
			if(L_FMTSTR.equals(M_strNOCPI17))
			{
				if(M_rdbHTML.isSelected())
					strTXFMT="</FONT>";
				else
					L_DOUT.writeChar(18);
			}
			if(L_FMTSTR.equals(M_strBOLD))
			{
				if(M_rdbHTML.isSelected())
					strTXFMT="<STRONG>";
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("G");
				}
			}
			if(L_FMTSTR.equals(M_strNOBOLD))
			{
				if(M_rdbHTML.isSelected())
					strTXFMT="</STRONG>";
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("H");
				}
			}
			if(L_FMTSTR.equals(M_strENH))
			{
				if(M_rdbHTML.isSelected())
					strTXFMT="<FONT Size = 5><STRONG>";
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("W1");
				}
			}
			if(L_FMTSTR.equals(M_strNOENH))
			{
				if(M_rdbHTML.isSelected())
					strTXFMT="</STRONG></FONT >";
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("W0");
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("F");
				}
			}
			if(L_FMTSTR.equals(M_strEJT))
			{
				if(M_rdbHTML.isSelected())
				{
					
				}
				else
					L_DOUT.writeChar(12);
			}
		}catch(IOException L_EX){
			setMSG(L_EX,"prnFMTCHR");
		}
	}
/**
 * Method to display data in HTML table format
 * 
 * Method to display data in HTML table format
 * 
 * <p>Does nothing if text format is seleted. <br>In HTML format, writes the table tag with format as specified by aurguments
 * 
 * @param L_DOUT Stream to which data is to be written
 * @param P_flgBORDER Flag to paint borders of the table
 * @param P_intWIDTH % width the table should occupy on the screen
 * @param P_chrALIGN Character for table alignment on screen. 'C' : Center, 'L' : Left, 'R' : Right
 * @param P_flgNWLIN Flag to indicate whether the table is to be placed on the current line or to the next line. This parameter is importent when earlier table is having width <100 or when two tables are to be place beside each other
 * 
 */
	private void crtTBL(DataOutputStream L_DOUT,boolean P_flgBORDR,int P_intWIDTH,char P_chrALIGN,boolean P_flgNWLIN) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(P_flgNWLIN)
			{
				for(int i=0;i<intLINCT;i++)
					L_DOUT.writeBytes("<p>&nbsp</p>");
			}
			String L_strALIGN=" Center ";
			if(P_chrALIGN == 'l' || P_chrALIGN == 'L')
				L_strALIGN=" Left ";
			else if(P_chrALIGN == 'r' || P_chrALIGN == 'R')
				L_strALIGN=" right ";
			if(P_flgBORDR)
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=white borderColorDark=white borderColorLight=gray cellPadding=0 cellSpacing=0  width=\""+Integer.toString(P_intWIDTH)+"%\" align="+L_strALIGN+">");
			else
				L_DOUT.writeBytes("<p><TABLE border=0 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gostwhite cellPadding=0 cellSpacing=0 width=\""+Integer.toString(P_intWIDTH)+"%\"  align="+L_strALIGN+">");
			flgTBLDT=true;
			if(P_intWIDTH<100 && P_chrALIGN!='C' )
				intLINCT=0;
			else
				intLINCT=-1;
		}
	}
/**
 * Method to print new line character
 * 
 * <p>In text format, simply writes "\n" in HTML format, closes TR tag
 */	
	void prnNWLIN(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbTEXT.isSelected())
		   L_DOUT.writeBytes("\n");
		else
		{
			if(intLINCT!=-1)
			intLINCT++;
			//if(flgTBLDT)
			//	L_DOUT.writeBytes("</TD></TR><TR>");
			//else
			//	L_DOUT.writeBytes("</P><P>");
		}
		M_intLINNO++;
		if(M_intLINNO>23)
		{
			
//			M_intLINNO=1;
			//prnRPFTR();
		}
	}
	/**Method to end table in HTML format	 */
	private void endTABLE(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbHTML.isSelected())
			L_DOUT.writeBytes("</TR></TABLE></P>");
		flgTBLDT=false;
	}

 	
	/**Over ridden to facilitate HTML format	 */
	protected  String padSTRING1(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		try
		{
			String L_STRSP = " ";
			if(!P_strSTRVL.equals(" "))
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			if(P_intPADLN <= L_STRLN)
			{
				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			if(M_rdbHTML.isSelected())
			{
				if(P_chrPADTP=='C')
					P_strTRNVL="<p Align = center>"+strTXFMT+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				else if(P_chrPADTP=='R')
					P_strTRNVL="<p Align = left>"+strTXFMT+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				else if(P_chrPADTP=='L')
					P_strTRNVL="<p Align = right>"+strTXFMT+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				if(flgTBLDT)
					P_strTRNVL="<td>"+strTXFMT+strTXCLR+P_strTRNVL+"</font>"+"</td>";
				return P_strTRNVL;
			}
			int L_STRDF = P_intPADLN - L_STRLN;
			StringBuffer L_STRBUF;
			switch(P_chrPADTP)
			{
				case 'C':
						L_STRDF = L_STRDF / 2;
						L_STRBUF = new StringBuffer(L_STRDF);
						for(int j = 0;j < L_STRBUF.capacity();j++)
							L_STRBUF.insert(j,' ');
						P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
					break;
				case 'R':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  P_strSTRVL+L_STRBUF ;
					break;
				case 'L':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL ;
					break;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"padSTRING");
		}
		return P_strTRNVL;
	}
	/**Method to set Text color in HTML file<br>Has no effect in text format	 */
	void setTXCLR(DataOutputStream L_DOUT,Color P_clrCOLOR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(P_clrCOLOR.equals(Color.red))
				strTXCLR="<font Color=red>";
			else if(P_clrCOLOR.equals(Color.black))
				strTXCLR="<font Color=black>";
			else if(P_clrCOLOR.equals(Color.blue))
				strTXCLR="<font Color=blue>";
			else if(P_clrCOLOR.equals(Color.magenta))
				strTXCLR="<font Color=purple>";
			else if(P_clrCOLOR.equals(Color.green))
				strTXCLR="<font Color=green>";
		}
		else if(M_rdbTEXT.isSelected())
		{
			if(P_clrCOLOR.equals(Color.red))
				prnFMTCHR(L_DOUT,M_strBOLD);
			else if(P_clrCOLOR.equals(Color.black))
				prnFMTCHR(L_DOUT,M_strNOBOLD);
			else if(P_clrCOLOR.equals(Color.blue))
				prnFMTCHR(L_DOUT,M_strBOLD);
			else if(P_clrCOLOR.equals(Color.magenta))
				prnFMTCHR(L_DOUT,M_strBOLD);
			else if(P_clrCOLOR.equals(Color.green))
				prnFMTCHR(L_DOUT,M_strBOLD);
		}
	}
	/**
	 * To create Horizintal line
	 * 
	 * To create Horizintal line
	 * 
	 * <p>Uses <HR> tag to create line in HTML. In text, uses carater specified in P_strLINCHR repeatedly upto P_intCHRCT.
	 * 
	 * @param L_DOUT Stream to write to
	 * @param P_strLNCHR Character to be used to create the line
	 * @param P_intCHRCT No. of times the string to be repeated to form a line
	 */
	void crtHRLIN(DataOutputStream L_DOUT,String P_strLNCHR,int P_intCHRCT) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(!flgTBLDT)
				//if(flgTBLDT)
				L_DOUT.writeBytes("<HR>");
		}
		else
			for(int i=0;i<P_intCHRCT;i++)
				L_DOUT.writeBytes(P_strLNCHR);
		prnNWLIN(L_DOUT);
	}

		

		 

		/** One time data capturing for Common Doc.Tax 
		*	into the Hash Table
		*/
		 private void crtTXDOC(String LP_MKTTP,String LP_OCFNO)
		{
			String L_strSQLQRY = "";
		    try
		    {
		        hstTXDOC.clear();
		        hstTXITM.clear();
				System.out.println("299");
				System.out.println("LP MKTTP : " +LP_MKTTP);
		        M_strSQLQRY = "select * from co_txdoc where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and tx_syscd='MR' and tx_sbstp='"+LP_MKTTP+"' and tx_doctp='OCF' and tx_docno='"+LP_OCFNO+"' ";//and tx_prdcd='XXXXXXXXXX'";
				System.out.println("300");
				//System.out.println(M_strSQLQRY);
		        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		        if(L_rstRSSET == null || !L_rstRSSET.next())
		        {
					System.out.println("301");
			         setMSG("Tax Records not found in CO_TXDOC",'E');
		              return;
		        }
		        while(true)
		        {
		                strTX_SYSCD = getRSTVAL(L_rstRSSET,"TX_SYSCD","C");
		                strTX_SBSTP = getRSTVAL(L_rstRSSET,"TX_SBSTP","C");
		                strTX_DOCTP = getRSTVAL(L_rstRSSET,"TX_DOCTP","C");
		                strTX_DOCNO = getRSTVAL(L_rstRSSET,"TX_DOCNO","C");
		                strTX_PRDCD = getRSTVAL(L_rstRSSET,"TX_PRDCD","C");
		                String[] staTXDOC = new String[intTXDOC_TOT];
		                staTXDOC[intAE_TX_EXCVL] = getRSTVAL(L_rstRSSET,"TX_EXCVL","N");
		                staTXDOC[intAE_TX_EXCFL] = getRSTVAL(L_rstRSSET,"TX_EXCFL","C");
		                staTXDOC[intAE_TX_EDCVL] = getRSTVAL(L_rstRSSET,"TX_EDCVL","N");
		                staTXDOC[intAE_TX_EDCFL] = getRSTVAL(L_rstRSSET,"TX_EDCFL","C");
		                staTXDOC[intAE_TX_EHCVL] = getRSTVAL(L_rstRSSET,"TX_EHCVL","N");
		                staTXDOC[intAE_TX_EHCFL] = getRSTVAL(L_rstRSSET,"TX_EHCFL","C");
		                staTXDOC[intAE_TX_SCHVL] = getRSTVAL(L_rstRSSET,"TX_SCHVL","N");
		                staTXDOC[intAE_TX_SCHFL] = getRSTVAL(L_rstRSSET,"TX_SCHFL","C");
		                staTXDOC[intAE_TX_VATVL] = getRSTVAL(L_rstRSSET,"TX_VATVL","N");
		                staTXDOC[intAE_TX_VATFL] = getRSTVAL(L_rstRSSET,"TX_VATFL","C");
		                staTXDOC[intAE_TX_HICVL] = getRSTVAL(L_rstRSSET,"TX_HICVL","N");
		                staTXDOC[intAE_TX_HICFL] = getRSTVAL(L_rstRSSET,"TX_HICFL","C");
						System.out.println("00001");
		                staTXDOC[intAE_TX_TOTVL] = getRSTVAL(L_rstRSSET,"TX_TOTVL","N");
		                staTXDOC[intAE_TX_TOTFL] = getRSTVAL(L_rstRSSET,"TX_TOTFL","C");
						//System.out.println("00002");
		                staTXDOC[intAE_TX_CSTVL] = getRSTVAL(L_rstRSSET,"TX_CSTVL","N");
		                staTXDOC[intAE_TX_CSTFL] = getRSTVAL(L_rstRSSET,"TX_CSTFL","C");
		                staTXDOC[intAE_TX_STXVL] = getRSTVAL(L_rstRSSET,"TX_STXVL","N");
		                staTXDOC[intAE_TX_STXFL] = getRSTVAL(L_rstRSSET,"TX_STXFL","C");
		                staTXDOC[intAE_TX_STXDS] = getRSTVAL(L_rstRSSET,"TX_STXDS","N");
		                staTXDOC[intAE_TX_OTHVL] = getRSTVAL(L_rstRSSET,"TX_OTHVL","N");
		                staTXDOC[intAE_TX_OTHFL] = getRSTVAL(L_rstRSSET,"TX_OTHFL","C");
		                staTXDOC[intAE_TX_OTHDS] = getRSTVAL(L_rstRSSET,"TX_OTHDS","N");
		              if(strTX_PRDCD.equals("XXXXXXXXXX"))
                            hstTXDOC.put(strTX_SYSCD+strTX_SBSTP+strTX_DOCTP+strTX_DOCNO+strTX_PRDCD,staTXDOC);
                      else
                            hstTXITM.put(strTX_SYSCD+strTX_SBSTP+strTX_DOCTP+strTX_DOCNO+strTX_PRDCD,staTXDOC);
		                if (!L_rstRSSET.next())
		                        break;
		        }
		        if(L_rstRSSET !=null)
		            L_rstRSSET.close();
		    }
		    catch(Exception L_EX)
		    {
		           setMSG(L_EX,"crtTXDOC");
		    }
		return;
		}
		


		/** One time data capturing for Specific Doc.Tax 
		*	into the Hash Table
		*/
		 private void crtTXSPC(String LP_MKTTP,String LP_OCFNO)
		{
			String L_strSQLQRY = "";
		    try
		    {
		        hstTXSPC.clear();
		        M_strSQLQRY = "select * from co_txspc where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and txt_syscd='MR' and txt_sbstp='"+LP_MKTTP+"' and txt_doctp='OCF' and txt_docno='"+LP_OCFNO+"' and txt_prdcd='XXXXXXXXXX'";
				//System.out.println(M_strSQLQRY);
		        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		        if(L_rstRSSET == null || !L_rstRSSET.next())
		        {
				     setMSG("Tax Records not found in CO_TXSPC",'N');
		              return;
		        }
		        while(true)
		        {
		                strTXT_SYSCD = getRSTVAL(L_rstRSSET,"TXT_SYSCD","C");
		                strTXT_SBSTP = getRSTVAL(L_rstRSSET,"TXT_SBSTP","C");
		                strTXT_DOCTP = getRSTVAL(L_rstRSSET,"TXT_DOCTP","C");
		                strTXT_DOCNO = getRSTVAL(L_rstRSSET,"TXT_DOCNO","C");
		                strTXT_PRDCD = getRSTVAL(L_rstRSSET,"TXT_PRDCD","C");
		                strTXT_CODCD = getRSTVAL(L_rstRSSET,"TXT_CODCD","C");
		                String[] staTXSPC = new String[intTXSPC_TOT];
		                staTXSPC[intAE_TXT_CODVL] = getRSTVAL(L_rstRSSET,"TXT_CODVL","N");
		                staTXSPC[intAE_TXT_CODFL] = getRSTVAL(L_rstRSSET,"TXT_CODFL","C");
		                hstTXSPC.put(strTXT_SYSCD+strTXT_SBSTP+strTXT_DOCTP+strTXT_DOCNO+strTXT_PRDCD+strTXT_CODCD,staTXSPC);
						//System.out.println(strTXT_SYSCD+strTXT_SBSTP+strTXT_DOCTP+strTXT_DOCNO+strTXT_PRDCD+strTXT_CODCD);
		                if (!L_rstRSSET.next())
		                        break;
		        }
		        L_rstRSSET.close();
		    }
		    catch(Exception L_EX)
		    {
		           setMSG(L_EX,"crtTXSPC");
		    }
		return;
		}
		 
		 
		 
		 
		 
		 
		 /** Picking up Doc.Tax Details (Common Taxes)
		 */
		private String getTXDOC(String LP_SYSCD, String LP_SBSTP, String LP_DOCTP, String LP_DOCNO, String LP_PRDCD, String LP_FLDNM, String LP_FLGVL)
		{
			//System.out.println("Received : "+LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD+LP_FLDNM);
			String L_RETSTR = LP_FLGVL.equals("VL") ? "0.00" : "";
			try
			{
					if(!hstTXDOC.containsKey(LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD))
						return L_RETSTR;
			        String[] staTXDOC = (String[])hstTXDOC.get(LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD);
					if(LP_FLGVL.equals("VL"))
				    {
						if (LP_FLDNM.equals("EXC"))
						        L_RETSTR = staTXDOC[intAE_TX_EXCVL];
						else if (LP_FLDNM.equals("EDC"))
						        L_RETSTR = staTXDOC[intAE_TX_EDCVL];
						else if (LP_FLDNM.equals("EHC"))
						        L_RETSTR = staTXDOC[intAE_TX_EHCVL];
						else if (LP_FLDNM.equals("SCH"))
						        L_RETSTR = staTXDOC[intAE_TX_SCHVL];
						else if (LP_FLDNM.equals("TOT"))
						        L_RETSTR = staTXDOC[intAE_TX_TOTVL];
						else if (LP_FLDNM.equals("CST"))
						        L_RETSTR = staTXDOC[intAE_TX_CSTVL];
						else if (LP_FLDNM.equals("STX"))
						        L_RETSTR = staTXDOC[intAE_TX_STXVL];
						else if (LP_FLDNM.equals("VAT"))
						        L_RETSTR = staTXDOC[intAE_TX_VATVL];
						else if (LP_FLDNM.equals("HIC"))
						        L_RETSTR = staTXDOC[intAE_TX_HICVL];
						else if (LP_FLDNM.equals("OTH"))
						        L_RETSTR = staTXDOC[intAE_TX_OTHVL];
					}
					if(LP_FLGVL.equals("FL"))
				    {
						if (LP_FLDNM.equals("EXC"))
						        L_RETSTR = staTXDOC[intAE_TX_EXCFL];
						else if (LP_FLDNM.equals("EDC"))
						        L_RETSTR = staTXDOC[intAE_TX_EDCFL];
						else if (LP_FLDNM.equals("EHC"))
						        L_RETSTR = staTXDOC[intAE_TX_EHCFL];
						else if (LP_FLDNM.equals("SCH"))
						        L_RETSTR = staTXDOC[intAE_TX_SCHFL];
						else if (LP_FLDNM.equals("TOT"))
						        L_RETSTR = staTXDOC[intAE_TX_TOTFL];
						else if (LP_FLDNM.equals("CST"))
						        L_RETSTR = staTXDOC[intAE_TX_CSTFL];
						else if (LP_FLDNM.equals("STX"))
						        L_RETSTR = staTXDOC[intAE_TX_STXFL];
						else if (LP_FLDNM.equals("VAT"))
						        L_RETSTR = staTXDOC[intAE_TX_VATFL];
						else if (LP_FLDNM.equals("HIC"))
						        L_RETSTR = staTXDOC[intAE_TX_HICFL];
						else if (LP_FLDNM.equals("OTH"))
						        L_RETSTR = staTXDOC[intAE_TX_OTHFL];
					}
					if(LP_FLGVL.equals("DS"))
				    {
						if (LP_FLDNM.equals("STX"))
						        L_RETSTR = staTXDOC[intAE_TX_STXDS];
						else if (LP_FLDNM.equals("OTH"))
						        L_RETSTR = staTXDOC[intAE_TX_OTHDS];
					}
			}
			catch (Exception L_EX)
			{
				setMSG(L_EX,"getTXDOC");
			}
			return L_RETSTR;
		}


		// Specific taxes are those taxes which are not predefined at Doc.Level tax table (CO_TXDOC)
		 /** Picking up Doc.Tax Details (Specific Taxes)
		 */
		private String getTXSPC(String LP_SYSCD, String LP_SBSTP, String LP_DOCTP, String LP_DOCNO, String LP_PRDCD, String LP_FLDNM, String LP_FLGVL)
		{
			//System.out.println("Received : "+LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD+LP_FLDNM);
			String L_RETSTR = LP_FLGVL.equals("VL") ? "0.00" : "";
			try
			{
					if(!hstTXSPC.containsKey(LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD+LP_FLDNM))
						return L_RETSTR;
			        String[] staTXSPC = (String[])hstTXSPC.get(LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD+LP_FLDNM);
					if(LP_FLGVL.equals("VL"))
						L_RETSTR = staTXSPC[intAE_TXT_CODVL];
					if(LP_FLGVL.equals("FL"))
						L_RETSTR = staTXSPC[intAE_TXT_CODFL];
					//if(LP_FLGVL.equals("DS"))
				    //{
					//	if (LP_FLDNM.equals("STX"))
					//	        L_RETSTR = staTXSPC[intAE_TX_STXDS];
					//	else if (LP_FLDNM.equals("OTH"))
					//	        L_RETSTR = staTXSPC[intAE_TX_OTHDS];
					//}
			}
			catch (Exception L_EX)
			{
				setMSG(L_EX,"getTXSPC");
			}
			return L_RETSTR;
		}
		
		
		
		
		/** One time data capturing for specified codes from CO_CDTRN
		 * into the Hash Table
		 */
         private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
        {
			String L_strSQLQRY = "";
            try
            {
                L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
                ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
                if(L_rstRSSET == null || !L_rstRSSET.next())
                {
					 //System.out.println(L_strSQLQRY);
                     setMSG("Records not found in CO_CDTRN",'E');
                      return;
                }
                while(true)
                {
                        strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                        strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                        strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                        String[] staCDTRN = new String[intCDTRN_TOT];
                        staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                        staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                        staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
                        staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
                        staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
                        staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
                        staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
                        staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
                        staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
                        LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
						//hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
                        if (!L_rstRSSET.next())
                                break;
                }
                L_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtCDTRN");
            }
		}
		 
		
		

		/** Picking up Specified Codes Transaction related details from Hash Table
		 * <B> for Specified Code Transaction key
		 * @param LP_CDTRN_KEY	Code Transaction key
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable<String,String[]> LP_HSTNM)
        {
		//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
        try
        {
				if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
					return "";
                if (LP_FLDNM.equals("CMT_CODCD"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
                else if (LP_FLDNM.equals("CMT_CODDS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
                else if (LP_FLDNM.equals("CMT_SHRDS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
                else if (LP_FLDNM.equals("CMT_CHP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
                else if (LP_FLDNM.equals("CMT_CHP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
                else if (LP_FLDNM.equals("CMT_NMP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
                else if (LP_FLDNM.equals("CMT_NMP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
                else if (LP_FLDNM.equals("CMT_NCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
                else if (LP_FLDNM.equals("CMT_CCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return "";
        }
		 
	/** Checking for common tax applicable, and printing the Tax Details 
	 * in predefined order
	 */
	private void chkTAXPRN_CO(String LP_PRDTP, String LP_OCFNO)
	{
		try
		{
			//System.out.println(LP_PRDTP+"/"+LP_OCFNO);
			if(intTXFLD>intTXFLD_TOT)
				{flgCOTAX = false;  return;}
			String L_strTXVAL = getTXDOC("MR",LP_PRDTP,"OCF",LP_OCFNO,"XXXXXXXXXX",arrTXFLD[intTXFLD],"VL");
			String L_strTXFLG = getTXDOC("MR",LP_PRDTP,"OCF",LP_OCFNO,"XXXXXXXXXX",arrTXFLD[intTXFLD],"FL");
			String L_strTXFLG1 = L_strTXFLG.equals("P")?"%":"";
			if(Double.parseDouble(nvlSTRVL(L_strTXVAL,"0.00"))==0.00 && !L_strTXFLG.equalsIgnoreCase("X"))
					{intTXFLD++; return;}
			dosREPORT.writeBytes(padSTRING('R',getCDTRN("SYSCOXXTAX"+arrTXFLD[intTXFLD],"CMT_CODDS",hstCDTRN),15));
			dosREPORT.writeBytes(padSTRING('L'," : "+L_strTXVAL+L_strTXFLG1,10));
			if(arrTXFLD[intTXFLD].equals("STX") || arrTXFLD[intTXFLD].equals("OTH"))
				dosREPORT.writeBytes(padSTRING('R',"  ("+getTXDOC("MR",LP_PRDTP,"OCF",LP_OCFNO,"XXXXXXXXXX",arrTXFLD[intTXFLD],"DS")+")",30));
			dosREPORT.writeBytes("\n");
			//prnNWLIN(dosREPORT);
			//O_DOUT.writeBytes(padSTR1('L',getCDTRN("SYSCOXXTAX"+arrTXFLD[intTXFLD],"CMT_TAXDS",hstCDTRN)+":"+L_strTXVAL+L_strTXFLG,12,"171",'N'));
			intTXFLD++;
		}
		catch(Exception L_EX)
			{setMSG(L_EX,"chkTAXPRN_CO");}
		return;
	}
		 


	/** Checking for specific tax applicable, and printing the Tax Details 
	 * in predefined order
	 */
	private void chkTAXPRN_SP(String LP_PRDTP, String LP_OCFNO, String LP_TAXCD)
	{
		try
		{
			//System.out.println(LP_PRDTP+"/"+LP_OCFNO);
			String L_strTXVAL = getTXSPC("MR",LP_PRDTP,"OCF",LP_OCFNO,"XXXXXXXXXX",LP_TAXCD,"VL");
			String L_strTXFLG = getTXSPC("MR",LP_PRDTP,"OCF",LP_OCFNO,"XXXXXXXXXX",LP_TAXCD,"FL");
			String L_strTXFLG1 = L_strTXFLG.equals("P")?"%":"";
			if(Double.parseDouble(nvlSTRVL(L_strTXVAL,"0.00"))==0.00 && !L_strTXFLG.equalsIgnoreCase("X"))
					return;
			if(Double.parseDouble(nvlSTRVL(L_strTXVAL,"0.00"))==0.00 && L_strTXFLG.equalsIgnoreCase("X"))
				dosREPORT.writeBytes(padSTRING('R',getCDTRN("SYSCOXXTAX"+LP_TAXCD,"CMT_CODDS",hstCDTRN),30));
			if(Double.parseDouble(nvlSTRVL(L_strTXVAL,"0.00"))>0.00 && !L_strTXFLG.equalsIgnoreCase("X"))
			{
				dosREPORT.writeBytes(padSTRING('R',getCDTRN("SYSCOXXTAX"+arrTXFLD[intTXFLD],"CMT_CODDS",hstCDTRN),15));
				dosREPORT.writeBytes(padSTRING('L'," : "+L_strTXVAL+L_strTXFLG1,10));
			}
			//prnNWLIN(dosREPORT);
			dosREPORT.writeBytes("\n");
		}
		catch(Exception L_EX)
			{setMSG(L_EX,"chkTAXPRN_SP");}
		return;
	}
	
	
	
	
		
/** Method for returning values from Result Set
 * <br> with respective verifications against various data types
 * @param	LP_RSLSET		Result set name
 * @param       LP_FLDNM                Name of the field for which data is to be extracted
 * @param	LP_FLDTP		Data Type of the field
 */
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
	{setMSG(L_EX,"getRSTVAL");}
return " ";
} 
		
}

