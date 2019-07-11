/*
System Name			: Marketing Management System
Program Name		: Grade Wise Booking status
Author				: AAP

Modified Date		: 
Documentation Date  : 
Version				: v2.0.0
*/

import java.io.IOException;import java.io.File;import java.io.FileOutputStream;
import java.io.DataOutputStream;
import javax.swing.JPanel;import javax.swing.JComponent;import javax.swing.JRadioButton;
import javax.swing.BorderFactory;import javax.swing.ButtonGroup;import javax.swing.JComboBox;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.util.Enumeration;import java.util.StringTokenizer;import java.util.Vector;
import java.util.Hashtable;import java.util.Calendar;import java.util.Arrays;
import java.awt.Color; import java.sql.ResultSet;
import java.text.SimpleDateFormat;

/**<BODY><P><FONT size=4><STRONG>Program Description :</STRONG></FONT>  </P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>    Sales Realisation Report.</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                                                         Gives&nbsp;Grade and                                                                         Package type wise&nbsp;Sales realisation                  based on invoices for       give period, sale type and product type </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>               Existing Report&nbsp;      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mr_rpsrn.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_rpsrn.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>15/06/2004&nbsp; </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;&nbsp;        </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><P><FONT color=purple><STRONG>   </STRONG></FONT>&nbsp;</P><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="LEFT: 11px; TOP:  373px; WIDTH: 100%" width="100%" background="" borderColorDark=white borderColorLight=darkslateblue borderColor=black>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center> Columns</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>MR_IVTRN</TD>    <TD>IVT_MKTTP, IVT_LADNO, IVT_PRDCD, IVT_PKGTP</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_INTRN</TD>    <TD>&nbsp;INT_INDNO,INT_PRDDS,INT_PKGWT,INT_INDQT</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD>      <P>CMT_CGMTP='SYS' and (CMT_CGSTP='FGXXPKG' or CMT_CGSTP='MR00ZON' or&nbsp;       CMT_CGSTP='MRXXRGN' or CMT_CGSTP='MR00SAL')&nbsp;</P>      <P>&nbsp;CMT_CGMTP='DOC' and CMT_CGSTP='MR01IND'</P>      <P>CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR'</P>        </TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_PRMST</TD>    <TD>&nbsp; PR_PRDCD,PR_PRDDS </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE><P>&nbsp;</P><P><FONT color=purple><STRONG>Report is Available in following Catagories : </STRONG></FONT></P><UL>  <LI>PS - Prime&nbsp;  <LI>  PS - Non Prime   <LI> SPS - All catagories   <LI>  All</LI></UL><UL>  <LI>For given duration</LI></UL><UL>  <LI>For Domestic  <LI>For Deemed Export  <LI>For Domestic &amp; Deemed Export  <LI>For Export  <LI>For all sale types</LI></UL><P>This Report makes use of HTML format. Methods in cl_rbase are overridden for the same. However, text format report is also available</P><P>Product catagory wise seggregation is done dynamically using 'COXXPGR' series in CO_CDTRN.</P></BODY> */
class mr_rpsrn extends cl_rbase
{
	/**	Flag to indicate that, data is being written in table format in HTML */
	private boolean flgTBLDT;/**	Global string for default text color in HTML */
	private String strTXCLR="<font Color=black>";/**Global string for page break in HTML	 */
	private String strPGBRK="<P CLASS = \"breakhere\"></PRE>";
	private Hashtable hstSALDT;
	private Hashtable hstZONDT;
	private Hashtable hstRGNDT;
	private Hashtable hstPKGTP;
	private Hashtable hstDDCVL;
	private Hashtable hstRPDAT;
	private Hashtable hstDEDAT;
	private Vector vtrPGRDS;
	private Vector vtrPGRCD;
	private Vector vtrPSDSC;
	private Vector vtrPSPRM;
	private Vector vtrNPDSC;
	private Vector vtrNPPS;
	private Vector vtrSPDSC;
	private Vector vtrSPPS;
	private int intINDQT=0,intBASVL=1,intDSCVL=2,intTPTVL=2;
	private boolean flgNSE=false,flgWC=false,flgEXP=false;
	private float[] flaGRTOT;
	private float[] flaCTTOT;
	private float[] flaDETOT;
	private int intCOLST=3;
/** To wirte report to a file */
	private DataOutputStream dosREPORT;/** String array for report column headers */
	private String[] staHEADR;/** Panel for report data table*/
	private JPanel pnlREPORT;/** Table for report data */
	private cl_JTable tblREPORT;/** Report of all product types */
	private JRadioButton rdbPSALL;/** Report of PS-All */
	private JRadioButton rdbPSPRM;/** Report of PS-non prime */
	private JRadioButton rdbPSNPR;/** Report of SPS-All */
	private JRadioButton rdbSPSAL;/** Radio buttons for all sale types. derived dynamically from CO_CDTRN */
	private JRadioButton rdbSALTP[];/** For all sale types */
	private JRadioButton rdbSTALL;/** For Domestic + Deemed export sale types */
	private JRadioButton rdbDOMDE;

	private JRadioButton rdbBASE_IND;
	private JRadioButton rdbBASE_INV;
	private JRadioButton rdbBASE_INDINV;
	
	
	/**
	 * Constructs the form
	 * 
	 * Constructs the form
	 * 
	 * <p>Retrieves details of package types and stores in hspPKGTP
	 * <p>Retrieves details of Product Catgories('COXXPGR') and stores in vtrPGRDS (Description) and vtrPGRCD (First 4 digits of code)
	 * <p>Retrieves product code and description from CO_PRMST, serggregates Product catagory wise using vtrPGRCD and stores in respective vectors in the form of set of object array for each subtype
	 */
	@SuppressWarnings("unchecked") mr_rpsrn()
	{
		super(2);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
		//RETRIEVING PAKAGE TYPE, ZONE, SALE TYPE DETAILS FROM CO_CDTRN and PUTTING IN RESPCTIVE HASHTABLES
			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CDTRN where (CMT_CGMTP='SYS' and (CMT_CGSTP='FGXXPKG' or CMT_CGSTP='MR00ZON' or CMT_CGSTP='MRXXRGN' or CMT_CGSTP='MR00SAL')) or (CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MR01IND') order by CMT_CODDS");
			if(M_rstRSSET!=null)
			{
				hstPKGTP=new Hashtable(10,0.75f);hstRGNDT=new Hashtable(10,0.75f);
				hstDDCVL=new Hashtable(10,0.75f);
				hstZONDT=new Hashtable(10,0.75f);hstSALDT=new Hashtable(10,0.75f);
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("CMT_CGSTP").equals("FGXXPKG"))
						hstPKGTP.put(M_rstRSSET.getString("CMT_CODCD"),nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),""));
					else if(M_rstRSSET.getString("CMT_CGSTP").equals("MRXXRGN"))
						hstRGNDT.put(M_rstRSSET.getString("CMT_CODCD"),new String[]{ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"")});
					else if(M_rstRSSET.getString("CMT_CGSTP").equals("MR00ZON"))
						hstZONDT.put(M_rstRSSET.getString("CMT_CODCD"),new  String[]{ nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"")} );
					else if(M_rstRSSET.getString("CMT_CGSTP").equals("MR00SAL"))
					{
						if(M_rstRSSET.getString("CMT_CODDS").equalsIgnoreCase("DOMESTIC") ||
						   M_rstRSSET.getString("CMT_CODDS").equalsIgnoreCase("DEEMED EXPORT") ||
						   M_rstRSSET.getString("CMT_CODDS").equalsIgnoreCase("EXPORT"))
						hstSALDT.put(M_rstRSSET.getString("CMT_CODDS"),nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""));
					}
					else if(M_rstRSSET.getString("CMT_CGSTP").equals("MR01IND"))
						hstDDCVL.put(M_rstRSSET.getString("CMT_CODCD"),nvlSTRVL(M_rstRSSET.getString("CMT_NMP01"),""));
				}
				rdbSALTP=new JRadioButton[hstSALDT.size()];
				Enumeration L_enmTEMP=hstSALDT.keys();
				int c=0;
				while(L_enmTEMP.hasMoreElements())
					rdbSALTP[c++]=new JRadioButton((String)L_enmTEMP.nextElement());
				M_rstRSSET.close();
			}
		//RETRIEVING DETAILS OF PRODUCT CODE GROUPING AND PUTTING IN RESP VECTORS
			M_rstRSSET=cl_dat.exeSQLQRY0("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' order by CMT_CODCD");
			if(M_rstRSSET!=null)
			{
				vtrPGRDS=new Vector(10,2);vtrPGRCD=new Vector(10,2);
				while(M_rstRSSET.next())
				{
					vtrPGRDS.addElement(M_rstRSSET.getString("CMT_CODDS"));
					vtrPGRCD.addElement(M_rstRSSET.getString("CMT_CODCD"));
				}
			}
		//RETRIEVING PRODUCT CODE DETAILS FROM CO_PRMST AND RETRIVING ALL POSSIBLE COMBINATIONS OF GRADE AND PKGTP
			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_PRMST order by PR_PRDCD");
			if(M_rstRSSET!=null)
			{
				vtrPSDSC=new Vector(20,5);vtrPSPRM=new Vector(10,5);vtrNPDSC=new Vector(20,5);
				vtrNPPS=new Vector(10,5);vtrSPDSC=new Vector(20,5);vtrSPPS=new Vector(10,5);
				Vector L_vtrTEMP=null;//Vector for list of PRDCD|PKGWT
				Enumeration L_enmPKGWT=null;//Enumeration for all PKGWT
				String L_strPGRCD=null;//Substring of first 4 digits of PRDCD
				while(M_rstRSSET.next())
				{
					L_enmPKGWT=hstPKGTP.elements();
					L_strPGRCD=M_rstRSSET.getString("PR_PRDCD").substring(0,4);
					for(int i=0;i<vtrPGRCD.size();i++)//SCAN TOTAL LIST OF PRODUCT CATAGORIES
					{
						if(L_strPGRCD.equals(vtrPGRCD.elementAt(i)))//FIND MATCHING PRODUCT CATAGORY
						{
							if(M_rstRSSET.getString("PR_PRDCD").substring(0,2).equals("51"))//GRADE IS OF PS
							{
								if(!M_rstRSSET.getString("PR_PRDCD").substring(6,7).equals("1"))//PRIME GRADE OF PS
								{
									if(vtrPSDSC.contains(vtrPGRDS.elementAt(i)))//PRODUCT CATAGORY ALREADY ADDED, THEN MODIFY EXISTING VECTOR, OTHERWISE, USE NEW
										L_vtrTEMP=(Vector)vtrPSPRM.elementAt(vtrPSDSC.indexOf(vtrPGRDS.elementAt(i)));
									else
										L_vtrTEMP=new Vector(10,2);
									while(L_enmPKGWT.hasMoreElements())//MAKE COMBINATION WITH ALL PKGWT AND OUT IN THE VECTOR
										L_vtrTEMP.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
									if(vtrPSDSC.contains(vtrPGRDS.elementAt(i)))//IF CATAGORY ALREADY EXISTS, REPLACE THE VECTOR OR ADD IT AND MAKE ENTRY TO DESRIPTION VECTOR
										vtrPSPRM.setElementAt(L_vtrTEMP,vtrPSDSC.indexOf(vtrPGRDS.elementAt(i)));
									else
									{
										vtrPSPRM.addElement(L_vtrTEMP);
										vtrPSDSC.addElement(vtrPGRDS.elementAt(i));
									}
								}
								else//PS NON - PRIME
								{
									if(vtrNPDSC.contains(vtrPGRDS.elementAt(i)))//PRODUCT CATAGORY ALREADY ADDED, THEN MODIFY EXISTING VECTOR, OTHERWISE, USE NEW
										L_vtrTEMP=(Vector)vtrNPPS.elementAt(vtrNPDSC.indexOf(vtrPGRDS.elementAt(i)));
									else
										L_vtrTEMP=new Vector(10,2);
									while(L_enmPKGWT.hasMoreElements())//MAKE COMBINATION WITH ALL PKGWT AND OUT IN THE VECTOR
										L_vtrTEMP.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
									if(vtrNPDSC.contains(vtrPGRDS.elementAt(i)))//IF CATAGORY ALREADY EXISTS, REPLACE THE VECTOR OR ADD IT AND MAKE ENTRY TO DESRIPTION VECTOR
										vtrNPPS.setElementAt(L_vtrTEMP,vtrNPDSC.indexOf(vtrPGRDS.elementAt(i)));
									else
									{
										vtrNPPS.addElement(L_vtrTEMP);
										vtrNPDSC.addElement(vtrPGRDS.elementAt(i));
									}
								}
							}
							if(M_rstRSSET.getString("PR_PRDCD").substring(0,2).equals("52"))
							{
								if(vtrSPDSC.contains(vtrPGRDS.elementAt(i)))//PRODUCT CATAGORY ALREADY ADDED, THEN MODIFY EXISTING VECTOR, OTHERWISE, USE NEW
									L_vtrTEMP=(Vector)vtrSPPS.elementAt(vtrSPDSC.indexOf(vtrPGRDS.elementAt(i)));
								else
									L_vtrTEMP=new Vector(10,2);
								while(L_enmPKGWT.hasMoreElements())//MAKE COMBINATION WITH ALL PKGWT AND OUT IN THE VECTOR
									L_vtrTEMP.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
								if(vtrSPDSC.contains(vtrPGRDS.elementAt(i)))//IF CATAGORY ALREADY EXISTS, REPLACE THE VECTOR OR ADD IT AND MAKE ENTRY TO DESRIPTION VECTOR
									vtrSPPS.setElementAt(L_vtrTEMP,vtrSPDSC.indexOf(vtrPGRDS.elementAt(i)));
								else
								{
									vtrSPPS.addElement(L_vtrTEMP);
									vtrSPDSC.addElement(vtrPGRDS.elementAt(i));
								}
							}
						}
					}
				}
				M_rstRSSET.close();
				L_vtrTEMP=null;
			//CONVERTING ALL VECTORS IN vtrPSPRM to OBJECT ARRAYS if any element is added
				for(int i=0;i<vtrPSPRM.size();i++)
				{
					L_vtrTEMP=(Vector)vtrPSPRM.elementAt(i);
					vtrPSPRM.removeElementAt(i);
					if(L_vtrTEMP.size()>0)
						vtrPSPRM.insertElementAt(L_vtrTEMP.toArray(),i);
					else
						vtrPSDSC.removeElementAt(i);
				}
			//CONVERTING ALL VECTORS IN vtrSPPS to OBJECT ARRAYS if any element is added
				for(int i=0;i<vtrSPPS.size();i++)
				{
					L_vtrTEMP=(Vector)vtrSPPS.elementAt(i);
					vtrSPPS.removeElementAt(i);
					if(L_vtrTEMP.size()>0)
						vtrSPPS.insertElementAt(L_vtrTEMP.toArray(),i);
					else
						vtrSPDSC.removeElementAt(i);
				}
			//CONVERTING ALL VECTORS IN vtrNPPS to OBJECT ARRAYS if any element is added
				for(int i=0;i<vtrNPPS.size();i++)
				{
					L_vtrTEMP=(Vector)vtrNPPS.elementAt(i);
					vtrNPPS.removeElementAt(i);
					if(L_vtrTEMP.size()>0)
						vtrNPPS.insertElementAt(L_vtrTEMP.toArray(),i);
					else
						vtrNPDSC.removeElementAt(i);
				}
			}
			setMatrix(20,8);
			M_pnlRPFMT.setVisible (true);M_rdbHTML.setSelected(true);
			
			JPanel pnlPSCAT=new JPanel(null);
			add(rdbPSPRM=new JRadioButton("PS - All"),1,1,1,1.5,pnlPSCAT,'L');
			add(rdbPSNPR=new JRadioButton("PS - NonPrime"),1,3,1,1.5,pnlPSCAT,'R');
			add(rdbSPSAL=new JRadioButton("Spciality PS"),1,4,1,1.5,pnlPSCAT,'L');
			add(rdbPSALL=new JRadioButton("All"),1,6,1,1.5,pnlPSCAT,'R');
			ButtonGroup L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbPSPRM);L_btgTEMP.add(rdbPSNPR);L_btgTEMP.add(rdbSPSAL);
			L_btgTEMP.add(rdbPSALL);
			pnlPSCAT.setBorder(BorderFactory.createTitledBorder("Product Type"));
			add(pnlPSCAT,2,1,2,6.05,this,'L');

			
			
			JPanel L_pnlSALTP=new JPanel(null);
			L_btgTEMP=new ButtonGroup();
			for(int i=0;i<rdbSALTP.length;i++)
			{
				add(rdbSALTP[i],1,i*2+1,1,1.5,L_pnlSALTP,'L');
				L_btgTEMP.add(rdbSALTP[i]);
			}
			add(rdbDOMDE=new JRadioButton("Domestic/D.Export"),1,rdbSALTP.length*2+1,1,1.5,L_pnlSALTP,'R');
			add(rdbSTALL=new JRadioButton("All"),1,rdbSALTP.length*2+2,1,1,L_pnlSALTP,'L');
			L_btgTEMP.add(rdbSTALL);L_btgTEMP.add(rdbDOMDE);
			L_pnlSALTP.setBorder(BorderFactory.createTitledBorder("Sale Type"));
			add(L_pnlSALTP,4,1,2,rdbSALTP.length*2+2.1,this,'L');
			rdbPSPRM.setSelected(true);
			M_rdbTEXT.setSelected(true);

			JPanel L_pnlBASCT=new JPanel(null);
			add(rdbBASE_IND=new JRadioButton("Booking"),1,1,1,1.5,L_pnlBASCT,'L');
			add(rdbBASE_INV=new JRadioButton("Sale"),1,3,1,1.5,L_pnlBASCT,'R');
			add(rdbBASE_INDINV=new JRadioButton("Sale-Bkg"),1,4,1,1.5,L_pnlBASCT,'L');

			L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbBASE_IND);
			L_btgTEMP.add(rdbBASE_INV);
			L_btgTEMP.add(rdbBASE_INDINV);
			L_pnlBASCT.setBorder(BorderFactory.createTitledBorder("Based On"));
			add(L_pnlBASCT,6,1,2,6.05,this,'L');
			rdbBASE_INV.setSelected(true);
			
			setMatrix(20,6);
		}catch(Exception e)
		{setMSG(e,"Child.Constructor");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**<b>TASKS</B><BR>
	 * Focus nevigation, component state setting and cmbZONDS & cmbRGNDS population
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			super.actionPerformed(L_AE);
		}catch(Exception e)
		{setMSG(e,"Child.actionPerformed");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**Focus transfer on Enter	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
			((JComponent)M_objSOURC).transferFocus();
	}
	
	boolean vldDATA()
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst)
		   && M_rdbTEXT.isSelected()
		   && M_cmbDESTN.getSelectedIndex() == 0)
		{
			setMSG("Please select Printer ..",'E');
			M_cmbDESTN.requestFocus();
			return false;
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst)
		   && M_cmbDESTN.getItemCount() == 0)
		{
			setMSG("Please Enter Receipents ..",'E');
			M_cmbDESTN.requestFocus();
			return false;
		}
	return true;
	}
	/** 
	 * Retrieve data from database and put in tblREPORT after Formatting
	 * 
	 * Retrieve data from database and put in tblREPORT after Formatting
	 * 
	 * <p>For the Day  : Booking upto report date is considered<br>
	 * For the Month : Booking in Report Month is considered<br>
	 * 	 
	 */
	@SuppressWarnings("unchecked") void exePRINT()
	{
		this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);
		try
		{
			if(!vldDATA())
				return;
			M_intLINNO=1;M_intPAGNO=1;
			setMSG("Fetching Report Data ..",'N');
		//FORMING SBSCD FILTER
			String L_strSBSCD="";
			for(int i=0;i<M_staUSRRT.length;i++)
				L_strSBSCD+="'"+M_staUSRRT[i][0].substring(2,6)+"',";
			L_strSBSCD=L_strSBSCD.substring(0,L_strSBSCD.length()-1);
			String L_strSALTP_IND = "and IN_SALTP in (";
			String L_strSALTP_INV = "and IVT_SALTP in (";
			if(rdbSTALL.isSelected())
			{
				Enumeration L_enmSTPSL=hstSALDT.elements();
				while(L_enmSTPSL.hasMoreElements())
				{
					L_strSALTP_IND+="'"+L_enmSTPSL.nextElement()+"',";
					L_strSALTP_INV+="'"+L_enmSTPSL.nextElement()+"',";
				}
				L_strSALTP_IND=L_strSALTP_IND.substring(0,L_strSALTP_IND.length()-1)+")";
				L_strSALTP_INV=L_strSALTP_INV.substring(0,L_strSALTP_INV.length()-1)+")";
			}
			else if(rdbDOMDE.isSelected())
			{
				L_strSALTP_IND +="'"+hstSALDT.get("DOMESTIC").toString()+"','"+hstSALDT.get("DEEMED EXPORT").toString()+"')";
				L_strSALTP_INV +="'"+hstSALDT.get("DOMESTIC").toString()+"','"+hstSALDT.get("DEEMED EXPORT").toString()+"')";
			}
			else
			{	
				int flag = 0;//shubham
				for(int i=0;i<rdbSALTP.length;i++)
					if(rdbSALTP[i].isSelected())
					{
						flag=1;//shubham
						L_strSALTP_IND = " and IN_SALTP='"+hstSALDT.get( rdbSALTP[i].getText())+"'";
						L_strSALTP_INV = " and IVT_SALTP='"+hstSALDT.get( rdbSALTP[i].getText())+"'";
					}
				
					if (flag == 0)//shubham
					L_strSALTP_INV = " ";//shubham
			}
			//M_strSQLQRY="Select IN_INDNO,IN_ZONCD,IN_SALTP,INT_PRDCD,INT_PRDDS, INT_PKGWT, INT_INDQT, INT_BASRT, INT_INDQT*INT_BASRT INT_BASVL,(isnull(INT_CDCVL,0)+isnull(INT_TDCVL,0)+isnull(INT_DDCVL,0))*INT_INDQT INT_TDCVL,isnull(IN_APTVL,0)*INT_INDQT*INT_BASRT INT_TPTVL from MR_INMST,MR_INTRN where IN_INDNO=INT_INDNO and IN_BKGDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_SBSCD in ("+L_strSBSCD+") and IN_STSFL not in ('0','X') and INT_STSFL not in ('0','X')"+L_strSALTP;
			if(rdbBASE_IND.isSelected())
				M_strSQLQRY="Select IN_INDNO IVT_INDNO, IN_ZONCD IVT_ZONCD,IN_SALTP IVT_SALTP,INT_PRDCD IVT_PRDCD,INT_PRDDS IVT_PRDDS, INT_PKGWT IVT_PKGWT,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) IVT_INVQT, isnull(IN_CPTVL,0)*INT_RSNVL IVT_TPTVL,INT_RSNVL IVT_RSNVL from VW_INTRN  where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and INT_SBSCD1 in "+M_strSBSLS+"  AND INT_RSNVL>0 and INT_STSFL not in ('X') "+L_strSALTP_IND;
			else if(rdbBASE_INV.isSelected())
				M_strSQLQRY="Select IVT_INDNO,IVT_ZONCD,IVT_SALTP,IVT_PRDCD,IVT_PRDDS, IVT_PKGWT, IVT_INVQT, isnull(IVT_CPTVL,0)*IVT_RSNVL IVT_TPTVL,IVT_RSNVL from MR_IVTRN where  IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,IVT_INVDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IVT_SBSCD1 in "+M_strSBSLS+" and IVT_RSNVL>0 and IVT_STSFL not in ('X') "+L_strSALTP_INV;
			else if(rdbBASE_INDINV.isSelected())
				M_strSQLQRY="Select IVT_INDNO,IVT_ZONCD,IVT_SALTP,IVT_PRDCD,IVT_PRDDS, IVT_PKGWT, IVT_INVQT, isnull(IVT_CPTVL,0)*IVT_RSNVL IVT_TPTVL,IVT_RSNVL from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SBSCD1 in "+M_strSBSLS+" and IVT_RSNVL>0 and IVT_STSFL not in ('X') "+L_strSALTP_INV+" and IVT_INDNO in (Select IN_INDNO  from VW_INTRN where  INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and INT_SBSCD1 in "+M_strSBSLS+L_strSALTP_IND+")";

			System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			setMSG("Formatting Report Data, Please wait ..",'N');
			Hashtable L_hstTEMP=null;
			hstRPDAT=new Hashtable(50,0.75f);
			hstDEDAT=new Hashtable(50,0.75f);
			float[] L_flaTEMP=null;
			String L_strRGNCD=null;
			flgNSE=false;flgWC=false;flgEXP=false;
			while(M_rstRSSET.next())
			{
				L_strRGNCD=((String[])hstZONDT.get(M_rstRSSET.getString("IVT_ZONCD")))[0];
				if(L_strRGNCD.equals("01"))
					flgNSE=true;
				else if(L_strRGNCD.equals("02"))
					flgWC=true;
				if(L_strRGNCD.equals("03"))
					flgEXP=true;
				if((rdbDOMDE.isSelected()&&M_rstRSSET.getString("IVT_SALTP").equals(hstSALDT.get("DOMESTIC"))) || !rdbDOMDE.isSelected())
				{
					if(hstRPDAT.containsKey(M_rstRSSET.getString("IVT_PRDDS")+"|"+M_rstRSSET.getString("IVT_PKGWT")))
					{
						L_hstTEMP=(Hashtable)hstRPDAT.get(M_rstRSSET.getString("IVT_PRDDS")+"|"+M_rstRSSET.getString("IVT_PKGWT"));
						if(L_hstTEMP.containsKey(L_strRGNCD))
							L_flaTEMP=(float[])L_hstTEMP.get(L_strRGNCD);
						else
							L_flaTEMP=new float[]{0.0f,0.0f,0.0f};
					}
					else
					{
						L_hstTEMP=new Hashtable(5,0.75f);
						L_flaTEMP=new float[]{0.0f,0.0f,0.0f};
					}
					L_flaTEMP[intINDQT]+=M_rstRSSET.getFloat("IVT_INVQT");
					L_flaTEMP[intBASVL]+=M_rstRSSET.getFloat("IVT_RSNVL");
					L_flaTEMP[2]+=M_rstRSSET.getFloat("IVT_TPTVL");
					L_hstTEMP.put(L_strRGNCD,L_flaTEMP);
					hstRPDAT.put(M_rstRSSET.getString("IVT_PRDDS")+"|"+M_rstRSSET.getString("IVT_PKGWT"),L_hstTEMP);
				}
				if(rdbDOMDE.isSelected() && M_rstRSSET.getString("IVT_SALTP").equals(hstSALDT.get("DEEMED EXPORT")))
				{
					if(hstDEDAT.containsKey(M_rstRSSET.getString("IVT_PRDDS")+"|"+M_rstRSSET.getString("IVT_PKGWT")))
					{
						L_hstTEMP=(Hashtable)hstDEDAT.get(M_rstRSSET.getString("IVT_PRDDS")+"|"+M_rstRSSET.getString("IVT_PKGWT"));
						if(L_hstTEMP.containsKey(L_strRGNCD))
							L_flaTEMP=(float[])L_hstTEMP.get(L_strRGNCD);
						else
							L_flaTEMP=new float[]{0.0f,0.0f,0.0f};
					}
					else
					{
						L_hstTEMP=new Hashtable(5,0.75f);
						L_flaTEMP=new float[]{0.0f,0.0f,0.0f};
					}
					L_flaTEMP[intINDQT]+=M_rstRSSET.getFloat("IVT_INVQT");
					L_flaTEMP[intBASVL]+=M_rstRSSET.getFloat("IVT_RSNVL");
					L_flaTEMP[2]+=M_rstRSSET.getFloat("IVT_TPTVL");
					L_hstTEMP.put(L_strRGNCD,L_flaTEMP);
					hstDEDAT.put(M_rstRSSET.getString("IVT_PRDDS")+"|"+M_rstRSSET.getString("IVT_PKGWT"),L_hstTEMP);
				}
			}
			int L_intCOLCT=3;
			if(flgWC)
				L_intCOLCT+=intCOLST;
			if(flgNSE)
				L_intCOLCT+=intCOLST;
			if(flgWC && flgNSE)
				L_intCOLCT+=intCOLST;
			if(flgEXP)
				L_intCOLCT+=intCOLST;
			String[] L_staHEADR=new String[L_intCOLCT];
			L_staHEADR[0]="FL";
			L_staHEADR[1]="Grade";
			L_staHEADR[2]="Pkg.Wt.";
			for(int i=3;i<L_staHEADR.length;i+=intCOLST)
			{
					L_staHEADR[i]="Total Qty [MT]"+Integer.toString(i);
					L_staHEADR[i+1]="W. Avg. Net Price"+Integer.toString(i);
					L_staHEADR[i+2]="W.Avg. Credit Period"+Integer.toString(i);
			}
			if(L_staHEADR.length==3)
			{
				setMSG("No Data Found ..",'E');
				return;
			}
			int[] L_inaCOLSZ=new int[L_intCOLCT];
			L_inaCOLSZ[0]=20;
			for(int i=1;i<L_inaCOLSZ.length;i++)
				L_inaCOLSZ[i]=60;
			
			if(pnlREPORT!=null)
				pnlREPORT.removeAll();
			if(pnlREPORT==null)
				pnlREPORT=new JPanel(null);
			tblREPORT=crtTBLPNL1(pnlREPORT,L_staHEADR,1000,1,1,11,6,L_inaCOLSZ,new int[]{0});
			tblREPORT.setEnabled(false);
			add(pnlREPORT,7,1,12,6,this,'L');
			
			flaGRTOT=new float[tblREPORT.getColumnCount()-3];
			flaCTTOT=new float[tblREPORT.getColumnCount()-3];
			flaDETOT=new float[tblREPORT.getColumnCount()-3];
		//PUT VALUES TO TABLE
			int L_intTEMP=0,L_intROWID=0;
			if(rdbPSPRM.isSelected() || rdbPSALL.isSelected())//PS PRIME VALUES
				L_intROWID=dspPSCAT(vtrPSPRM,vtrPSDSC,L_intROWID);
			flaCTTOT=new float[tblREPORT.getColumnCount()-3];
			if(rdbPSNPR.isSelected() || rdbPSALL.isSelected() || rdbPSPRM.isSelected())//PS - NON PRIME VALUES
				L_intROWID=dspPSCAT(vtrNPPS,vtrNPDSC,L_intROWID);
			flaCTTOT=new float[tblREPORT.getColumnCount()-3];
			if(rdbSPSAL.isSelected() || rdbPSALL.isSelected())//SPPS VALUES
				L_intROWID=dspPSCAT(vtrSPPS,vtrSPDSC,L_intROWID);
			if(rdbPSALL.isSelected())
			{
				dspTOTAL(L_intROWID,flaGRTOT,"GRAND TOTAL");
				L_intROWID++;
			}
		//WRITE TO FILE
			exePRNRPT();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst) || (M_rdbHTML.isSelected() && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst)))
			{//DISPLAY HTML REPORT IN BROWSER
					Runtime r = Runtime.getRuntime();
					Process p = null;
					if(M_rdbTEXT.isSelected())
						p  = r.exec("c:\\windows\\wordpad.exe "+cl_dat.M_strREPSTR_pbst+"mr_rpsrn.doc"); 
					else
						p  = r.exec("c:\\program files\\internet explorer\\iexplore.exe "+cl_dat.M_strREPSTR_pbst+"mr_rpsrn.htm");
			}
		//PRINT TO PRINTER IN PRINT OPTION IN TEXT FORMAT
			else if(M_rdbTEXT.isSelected() && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst))
				doPRINT(cl_dat.M_strREPSTR_pbst+"mr_rpsrn.doc");
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst))
			//E_MAIL THE RESPECTIVE FILE TO RECEIPENTS, ONE BY ONE
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					((cl_eml)Class.forName("cl_eml").newInstance()).sendfile(M_cmbDESTN.getItemAt(i).toString(),cl_dat.M_strREPSTR_pbst+"mr_rpsrn."+(M_rdbTEXT.isSelected() ? "doc" : "htm"),"Grade wise stock status on "+M_txtTODAT.getText(),"");
			setMSG("Report Completed ..",'N');
		}catch(Exception e)
		{
			setMSG(e,"exePRINT");
			setMSG("Error in Report Generation ..",'E');
		}
		finally{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	private int dspPSCAT(Vector P_vtrPSCOD,Vector P_vtrPSDSC,int P_intROWID) throws Exception
	{
		boolean L_flgDATA=false,L_flgCTDAT=false;
		int L_intTEMP=0;
		for(int i=0;i<P_vtrPSCOD.size();i++)
		{
			L_intTEMP=P_intROWID++;P_intROWID++;
			if(((Object[])P_vtrPSCOD.elementAt(i)).length>0)
				P_intROWID=setTBLDATA(P_intROWID,(Object[])P_vtrPSCOD.elementAt(i),hstRPDAT);
			if(P_intROWID==L_intTEMP+2)//IF NO DATA ADDED, SET ROW POINTER TO ORIGINAL
				P_intROWID-=3;
			else //ELSE DISPLAY HEADING AT THE TOP & CALCULATE GRAND TOTAL
			{
				tblREPORT.setRowColor(L_intTEMP,Color.blue);
				tblREPORT.setValueAt(P_vtrPSDSC.elementAt(i)+(P_vtrPSDSC==vtrNPDSC ? " NON PRIME" : ""),L_intTEMP,1);
				if(rdbDOMDE.isSelected())
				{
					tblREPORT.setRowColor(L_intTEMP+1,Color.blue);
					tblREPORT.setValueAt("DOMESTIC",L_intTEMP+1,1);
					L_flgCTDAT=true;
				}
				L_flgDATA=true;
			}
			L_intTEMP=P_intROWID++;P_intROWID++;
			if(((Object[])P_vtrPSCOD.elementAt(i)).length>0)
				P_intROWID=setTBLDATA(P_intROWID,(Object[])P_vtrPSCOD.elementAt(i),hstDEDAT);
			if(P_intROWID==L_intTEMP+2)//IF NO DATA ADDED, SET ROW POINTER TO ORIGINAL
				P_intROWID-=2;
			else //ELSE DISPLAY HEADING AT THE TOP & CALCULATE GRAND TOTAL
			{
				if(rdbDOMDE.isSelected())
				{
					tblREPORT.setRowColor(L_intTEMP+1,Color.blue);
					tblREPORT.setValueAt("DEMMED EXPORT",L_intTEMP+1,1);
					L_flgCTDAT=true;
				}
				L_flgDATA=true;
			}
			P_intROWID++;
			if(L_flgCTDAT)
			{
				dspTOTAL(P_intROWID++,flaDETOT,P_vtrPSDSC.elementAt(i)+(P_vtrPSDSC==vtrNPDSC ? " NON PRIME" : "")+" TOTAL");
				P_intROWID++;
				flaDETOT=new float[tblREPORT.getColumnCount()-3];
				L_flgCTDAT=false;
			}
		}
		if(L_flgDATA)
		{
			String L_strHEADG="";
			if(P_vtrPSCOD==vtrPSPRM)
				L_strHEADG="PS-PRIME ";
			else if(P_vtrPSCOD==vtrNPPS)
				L_strHEADG="PS-NONPRIME ";
			if(P_vtrPSCOD==vtrSPPS)
				L_strHEADG="SPS ";
			dspTOTAL(P_intROWID++,flaCTTOT,L_strHEADG+"TOTAL");
			P_intROWID++;
		}
		return P_intROWID;
	}
	
	/**
	 * Method to put data in tblREPORT
	 * 
	 * Method to put data in tblREPORT
	 * 
	 * <p>Formats the data and displays in tblREPORT. Calculates Totals side by side and displays at the end
	 * 
	 * @param P_intROWID Row no. from which data is to be displayed in tblREPORT
	 * @param P_obaPSCAT Object array giving details of the Product catagory to be displayed. Should be one of obaGPPS,obaHIP,obaGPNP,obaHINP,obaSPGP,obaSPHI
	 * @return Last row updated in tblREPORT. -1 if error occurres
	 */
	private int setTBLDATA(int P_intROWID,Object[] P_obaPSCAT,Hashtable P_hstRPDAT)
	{
		try
		{
			boolean L_flgTOTAL=false;
			float[] L_flaTOTAL=new float[tblREPORT.getColumnCount()-3];
			Hashtable L_hstTEMP=null;
			float [] L_flaTEMP=null;
			int L_intROWID=P_intROWID;
			for(int i=0; i<P_obaPSCAT.length;i++)
			{
				if(P_hstRPDAT.containsKey(P_obaPSCAT[i]) )
				{
					L_hstTEMP=(Hashtable)P_hstRPDAT.get(P_obaPSCAT[i]);
					Enumeration L_enmTEMP=L_hstTEMP.keys();
					Object L_objRGNCD=null;
					int L_intCOLID=0;
					StringTokenizer L_stkTEMP=new StringTokenizer((String)P_obaPSCAT[i],"|");
					tblREPORT.setValueAt(L_stkTEMP.nextToken(),P_intROWID,1);
					tblREPORT.setValueAt(L_stkTEMP.nextToken(),P_intROWID,2);
					while(L_enmTEMP.hasMoreElements())
					{
						L_objRGNCD=L_enmTEMP.nextElement();
						L_flaTEMP=(float[])L_hstTEMP.get(L_objRGNCD);
						L_intCOLID=3;
						if(L_objRGNCD.equals("02"))
						{
							if(flgNSE)
								L_intCOLID=3+intCOLST;
						}
						else if(L_objRGNCD.equals("03"))
						{
							if(flgNSE)
								L_intCOLID=3+intCOLST;
							if(flgWC)
								L_intCOLID+=intCOLST;
							if(flgWC && flgNSE)
								L_intCOLID+=intCOLST;
						}
						tblREPORT.setValueAt(setNumberFormat(L_flaTEMP[intINDQT],3),P_intROWID,0+L_intCOLID);
						tblREPORT.setValueAt(setNumberFormat(L_flaTEMP[intBASVL]/L_flaTEMP[intINDQT],2),P_intROWID,1+L_intCOLID);
						tblREPORT.setValueAt(setNumberFormat(L_flaTEMP[intTPTVL]/L_flaTEMP[intBASVL],0),P_intROWID,2+L_intCOLID);
						L_flaTOTAL[L_intCOLID-3]+=L_flaTEMP[intINDQT];
						L_flaTOTAL[L_intCOLID-2]+=L_flaTEMP[intBASVL];
						L_flaTOTAL[L_intCOLID-1]+=L_flaTEMP[intTPTVL];
					}
					if(flgWC && flgNSE)
					{//CALCUALTE TOTAL OF NSE & WC
						float L_fltQTY1=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,3).toString(),"0.0")),
							L_fltQTY2=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,3+intCOLST).toString(),"0.0"));
						if(L_fltQTY1+L_fltQTY2>0.0f)
						{
							tblREPORT.setValueAt(setNumberFormat(L_fltQTY1+L_fltQTY2,3),P_intROWID,3+intCOLST+intCOLST);
							tblREPORT.setValueAt(setNumberFormat(((Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4).toString(),"0.0"))*L_fltQTY1)
																 +(Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4+intCOLST).toString(),"0.0"))*L_fltQTY2))/(L_fltQTY1+L_fltQTY2),2),P_intROWID,4+intCOLST+intCOLST);
							tblREPORT.setValueAt(setNumberFormat(((Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,5).toString(),"0.0"))*L_fltQTY1*Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4).toString(),"0.0")))
																 +(Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,5+intCOLST).toString(),"0.0"))*L_fltQTY2*Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4+intCOLST).toString(),"0.0"))))/(L_fltQTY1*Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,3+1).toString(),"0.0"))+L_fltQTY2*Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,3+intCOLST+1).toString(),"0.0"))),0),P_intROWID,5+intCOLST+intCOLST);

					//ADDING VALUES TO TOTALISED VALUES		
							L_flaTOTAL[intCOLST+intCOLST]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,3+intCOLST+intCOLST).toString(),"0.0"));
							L_flaTOTAL[1+intCOLST+intCOLST]+=((Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4).toString(),"0.0"))*L_fltQTY1)
																 +(Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4+intCOLST).toString(),"0.0"))*L_fltQTY2));
							L_flaTOTAL[2+intCOLST+intCOLST]+=((Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,5).toString(),"0.0"))*L_fltQTY1*Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4).toString(),"0.0")))
																 +(Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,5+intCOLST).toString(),"0.0"))*L_fltQTY2*Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,4+intCOLST).toString(),"0.0"))));
						}
					}
					P_intROWID++;
				}
			}
			for(int i=0;i<L_flaTOTAL.length;i++)
			{
				flaGRTOT[i]+=L_flaTOTAL[i];
				flaCTTOT[i]+=L_flaTOTAL[i];
				flaDETOT[i]+=L_flaTOTAL[i];
			}
			if(L_intROWID!=P_intROWID)
			{
				String L_strHEADG="TOTAL";
				if(rdbDOMDE.isSelected())
				{
					if(P_hstRPDAT==hstRPDAT)
						L_strHEADG="DOMESTIC "+L_strHEADG;
					else
						L_strHEADG="DEEMED EXPORT "+L_strHEADG;
				}
				dspTOTAL(P_intROWID,L_flaTOTAL,L_strHEADG);
				P_intROWID++;
			}
			return P_intROWID;
		}catch(Exception e)
		{setMSG(e,"setTBLDATA");
		return -1;}
	}
	
	private void dspTOTAL(int P_intROWID,float[] P_flaTOTAL,String P_strHEADR)
	{
		tblREPORT.setRowColor(P_intROWID,Color.blue);
		tblREPORT.setValueAt(P_strHEADR,P_intROWID,1);
		for(int j=0;j<P_flaTOTAL.length;j++)
			tblREPORT.setValueAt(setNumberFormat(P_flaTOTAL[j],3),P_intROWID,j+3);
		for(int j=4;j<tblREPORT.getColumnCount();j+=intCOLST)
		{
			if(Float.parseFloat(tblREPORT.getValueAt(P_intROWID,j-1).toString())>0.0f)
			{
				tblREPORT.setValueAt(setNumberFormat(Float.parseFloat(tblREPORT.getValueAt(P_intROWID,j).toString())/Float.parseFloat(tblREPORT.getValueAt(P_intROWID,j-1).toString()),2),P_intROWID,j);
				tblREPORT.setValueAt(setNumberFormat(Float.parseFloat(tblREPORT.getValueAt(P_intROWID,j+1).toString())/(Float.parseFloat(tblREPORT.getValueAt(P_intROWID,j-1).toString())*Float.parseFloat(tblREPORT.getValueAt(P_intROWID,j).toString())),0),P_intROWID,j+1);
			}
			else
			{
				tblREPORT.setValueAt("0.0",P_intROWID,j);
				tblREPORT.setValueAt("0.0",P_intROWID,j+1);
			}
		}
	}
	/**Method to write report to file, text or html	 */
	private void exePRNRPT()
	{
		try
		{
			File filREPORT=null;
			if(M_rdbHTML.isSelected())
				filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_rpsrn.htm");
			else
				filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_rpsrn.doc");
			FileOutputStream filOUT=new FileOutputStream(filREPORT);
			dosREPORT=new DataOutputStream(filOUT);

			String L_strREPNM="Grade wise Sales Realisation (Based On Invoice)";//+(rdbDAILY.isSelected() ? "Day" : "Month");
			String L_strREPTP="For The Period : From "+M_txtFMDAT.getText()+" To "+M_txtTODAT.getText();
			String L_strREPCT="Product Type : ";
			if(rdbPSPRM.isSelected())
				L_strREPCT+=rdbPSPRM.getText();
			else if(rdbPSNPR.isSelected())
				L_strREPCT+=rdbPSNPR.getText();
			else if(rdbSPSAL.isSelected())
				L_strREPCT+=rdbSPSAL.getText();
			else if(rdbPSALL.isSelected())
				L_strREPCT+=rdbPSALL.getText();
			L_strREPCT+="; Sale Type : ";
			for(int i=0;i<rdbSALTP.length;i++)
				if(rdbSALTP[i].isSelected())
					L_strREPCT+=rdbSALTP[i].getText();
			if(rdbDOMDE.isSelected())
				L_strREPCT+=rdbDOMDE.getText();
			else if(rdbSTALL.isSelected())
				L_strREPCT+=rdbSTALL.getText();
			int L_intROWSZ=(tblREPORT.getColumnCount()-1)*10-30;
			if(M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("<HTML><HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD>");
				dosREPORT.writeBytes("<BODY bgColor=ghostwhite><P><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\"> <TR><TD><IMG src=\"file://f:\\exec\\splerp2\\spllogo_old.gif\" style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></TD><TD><P align=left><STRONG><FONT face=Arial size=5>"+cl_dat.M_strCMPNM_pbst+"</FONT></STRONG></P><STRONG><FONT face=Arial size=4><p align=left> "+L_strREPNM+"</font><STRONG><FONT face=Arial size=3><p align=left> "+L_strREPTP+"</font><STRONG><FONT face=Arial size=3><p align=left> "+L_strREPCT+"</font> </TD><TD><p><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</p><p><FONT face=Arial size=2>Page No. : 1</P><TD> </TR></TABLE><HR><FONT face=\"Comic Sans MS\">");
			}
			else
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);prnFMTCHR(dosREPORT,M_strCPI10);prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,80));
				crtNWLIN(dosREPORT);
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes(padSTRING('R',L_strREPNM,(L_intROWSZ>70 ? L_intROWSZ:70)));
				dosREPORT.writeBytes(padSTRING('R',"DATE : "+cl_dat.M_txtCLKDT_pbst.getText(),30));
				crtNWLIN(dosREPORT);
				dosREPORT.writeBytes(padSTRING('R',L_strREPTP,(L_intROWSZ>70 ? L_intROWSZ:70)));
				dosREPORT.writeBytes(padSTRING('R',"PAGE : 1",30));
				crtNWLIN(dosREPORT);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			prnHEADR();
			for(int i=0;i<tblREPORT.getRowCount();i++)
			{
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,1));
					if(	   tblREPORT.getValueAt(i,1).toString().length()==0 && tblREPORT.getValueAt(i,2).toString().length()==0
						&& tblREPORT.getValueAt(i+1,1).toString().length()==0 && tblREPORT.getValueAt(i+1,2).toString().length()==0)
					{
						crtHRLIN(dosREPORT,"-",L_intROWSZ+30);
						endTABLE(dosREPORT);
						dosREPORT.writeBytes("Formula : Total Qty = Simple Addition;\t Wt. Avg. Basic Price = Sum(Ind.Qty * Bas.Price)/Sum(Ind.Qty.);\t Wt. Avg. Cr. Prd. = Sum((Inv.Qty*Bas.Price)*Cr.Prd)/Sum(Inv.Qty*Bas.Price).\nNote : Wt.Avg formula for Credit Amount & Net Price is similar to Wt.Avg formula for Basic Price");///////////////
						prnFMTCHR(dosREPORT,M_strEJT);
						break;
					}
					if(tblREPORT.getValueAt(i,1).toString().length()==0 && tblREPORT.getValueAt(i,2).toString().length()==0)
						continue;
				for(int j=1;j<tblREPORT.getColumnCount();j++)
				{
					if(j==6 || j==11 || j==16 || j==21)
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(j>2)
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"-"),10));
					else if(j == 1)
					{
						if(tblREPORT.getValueAt(i,2).toString().length() == 0)
						{
							if(M_rdbTEXT.isSelected())
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(tblREPORT.getValueAt(i,j++).toString(),"-"),24));
							else
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(tblREPORT.getValueAt(i,j++).toString(),"-"),14)+padSTRING('R',"-",2));
						}
						else
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"-"),14));
					}
					else
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"-"),10));
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				}
				crtNWLIN(dosREPORT);
				if(tblREPORT.getCellColor(i,1).equals(Color.blue))
				{
					if(   tblREPORT.getValueAt(i,1).equals("PS-PRIME TOTAL") 
					   || tblREPORT.getValueAt(i,1).equals("PS-NONPRIME TOTAL") 
					   || tblREPORT.getValueAt(i,1).equals("SPS TOTAL") )
						crtHRLIN(dosREPORT,"- ",L_intROWSZ/2+15);
					crtNWLIN(dosREPORT);
				}
			}
			dosREPORT.flush();
			dosREPORT.close();
		}catch(Exception e)
		{setMSG(e,"exePRNRPT");}
	}
	/**Method to print header of report 	 */
	private void prnHEADR() throws Exception
	{
		crtTBL(dosREPORT,true);
		setTXCLR(dosREPORT,Color.red);
		prnFMTCHR(dosREPORT,M_strCPI17);
		crtHRLIN(dosREPORT,"-",220);
		prnFMTCHR(dosREPORT,M_strBOLD);
		if(M_rdbTEXT.isSelected())
			prnFMTCHR(dosREPORT,M_strCPI17);
		if(M_rdbTEXT.isSelected())
			dosREPORT.writeBytes(padSTRING('L'," ",24));
		else
			dosREPORT.writeBytes(padSTRING('L',"-",2)+padSTRING('L',"-",2));
		if(flgNSE)
		{
			if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes("    "+padSTRING('C',"----- West & Central -----",26));
			else
				dosREPORT.writeBytes("<td colspan = "+Integer.toString(intCOLST)+" align = center><font color=red>West & Central</td>");
		}
		if(flgWC)
		{
			if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes("    "+padSTRING('C',"--- North, East & South ---",26));
			else
				dosREPORT.writeBytes("<td colspan = "+Integer.toString(intCOLST)+" align = center><font color=red>North, East & South</td>");
		}
		if(flgNSE && flgWC)
		{
			if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes("    "+padSTRING('C',"------ TOTAL ------",26));
			else
				dosREPORT.writeBytes("<td colspan = "+Integer.toString(intCOLST)+" align = center><font color=red>TOTAL</td>");
		}
		if(flgEXP)
		{
			if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes("    "+padSTRING('C',"----- Export -----",26));
			else
				dosREPORT.writeBytes("<td colspan = "+Integer.toString(intCOLST)+" align = center><font color=red>Export</td>");
		}
		crtNWLIN(dosREPORT);
		dosREPORT.writeBytes(padSTRING('R',"GRADE",14));
		dosREPORT.writeBytes(padSTRING('L',"PKG.Wt.",10));
		for (int i=3;i<tblREPORT.getColumnCount();i+=intCOLST)
		{
			dosREPORT.writeBytes(padSTRING('L',"TOTAL",10));
			dosREPORT.writeBytes(padSTRING('L',"W.AVG.",10));
			dosREPORT.writeBytes(padSTRING('L',"W.AVG.",10));
//			dosREPORT.writeBytes(padSTRING('L',"W.AVG.",10));
//			dosREPORT.writeBytes(padSTRING('L',"W.AVG.",10));
		}
		crtNWLIN(dosREPORT);
		if(M_rdbTEXT.isSelected())
			dosREPORT.writeBytes(padSTRING('L'," ",24));
		else
			dosREPORT.writeBytes(padSTRING('L',"-",2)+padSTRING('L',"-",2));
		for (int i=3;i<tblREPORT.getColumnCount();i+=intCOLST)
		{
			dosREPORT.writeBytes(padSTRING('L',"QTY",10));
//			dosREPORT.writeBytes(padSTRING('L',"BASIC",10));
//			dosREPORT.writeBytes(padSTRING('L',"CREDIT",10));
			dosREPORT.writeBytes(padSTRING('L',"NET",10));
			dosREPORT.writeBytes(padSTRING('L',"CREDIT",10));
		}
		crtNWLIN(dosREPORT);
		if(M_rdbTEXT.isSelected())
			dosREPORT.writeBytes(padSTRING('L'," ",24));
		else
			dosREPORT.writeBytes(padSTRING('L',"-",2)+padSTRING('L',"-",2));
		for (int i=3;i<tblREPORT.getColumnCount();i+=intCOLST)
		{
			dosREPORT.writeBytes(padSTRING('L',"(MT)",10));
//			dosREPORT.writeBytes(padSTRING('L',"PRICE",10));
//			dosREPORT.writeBytes(padSTRING('L',"AMOUNT",10));
			dosREPORT.writeBytes(padSTRING('L',"PRICE",10));
			dosREPORT.writeBytes(padSTRING('L',"PERIOD",10));
		}
		crtNWLIN(dosREPORT);
		crtHRLIN(dosREPORT,"-",224);
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		
	}
	/**Method to print footer of report	 */
	private void prnRPFTR() throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			endTABLE(dosREPORT);
			dosREPORT.writeBytes(strPGBRK);
		}
		else 
			prnFMTCHR(dosREPORT,M_strEJT);
		crtNWLIN(dosREPORT);
		prnHEADR();
		crtNWLIN(dosREPORT);
		
	}
	

	void clrCOMP()
	{
		super.clrCOMP();
		try
		{
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			M_calLOCAL.add(Calendar.DATE,-1);
			M_txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
			M_txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
		}catch(Exception e)
		{setMSG(e,"Child.clrCOMP");}
	}

	protected  void prnFMTCHR(DataOutputStream L_DOUT,String L_FMTSTR){
		try{
			if(L_FMTSTR.equals(M_strCPI10))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<FONT Size = 6>");
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
					L_DOUT.writeBytes("<FONT  Size = 5>");
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
					L_DOUT.writeBytes("<FONT Size = 4>");
				else
					L_DOUT.writeChar(15);
			}
			if(L_FMTSTR.equals(M_strNOCPI17))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("</FONT>");
				else
					L_DOUT.writeChar(18);
			}
			if(L_FMTSTR.equals(M_strBOLD))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<STRONG>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("G");
				}
			}
			if(L_FMTSTR.equals(M_strNOBOLD))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("</STRONG>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("H");
				}
			}
			if(L_FMTSTR.equals(M_strENH))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<FONT Size = 5><STRONG>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("W1");
				}
			}
			if(L_FMTSTR.equals(M_strNOENH))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("</STRONG></FONT >");
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
	
	private void crtTBL(DataOutputStream L_DOUT,boolean P_flgBORDR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(P_flgBORDR)
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=white borderColorDark=white borderColorLight=gray cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
			else
				L_DOUT.writeBytes("<p><TABLE border=0 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gostwhite cellPadding=0 cellSpacing=0 width=\"100%\"  align=center>");
			flgTBLDT=true;
		}
	}
	
	void crtNWLIN(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbTEXT.isSelected())
		   L_DOUT.writeBytes("\n");
		else
		{
			if(flgTBLDT)
				L_DOUT.writeBytes("</TD></TR><TR>");
			else
				L_DOUT.writeBytes("</P><P>");
		}
		M_intLINNO++;
		if(M_intLINNO>65)
		{
			M_intLINNO=1;
			prnRPFTR();
		}
	}
	
	private void endTABLE(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbHTML.isSelected())
			L_DOUT.writeBytes("</TR></TABLE></P>");
		flgTBLDT=false;
	}
	
	protected  String padSTRING(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		try
		{
			String L_STRSP = " ";
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
					P_strTRNVL="<p Align = center>"+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				else if(P_chrPADTP=='R')
					P_strTRNVL="<p Align = left>"+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				else if(P_chrPADTP=='L')
					P_strTRNVL="<p Align = right>"+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				if(flgTBLDT)
					P_strTRNVL="<td>"+strTXCLR+P_strTRNVL+"</font>"+"</td>";
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
	
	void crtHRLIN(DataOutputStream L_DOUT,String P_strLNCHR,int P_intCHRCT) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(!flgTBLDT)
				L_DOUT.writeBytes("<HR>");
		}
		else
			for(int i=0;i<P_intCHRCT;i++)
				L_DOUT.writeBytes(P_strLNCHR);
		crtNWLIN(L_DOUT);
	}
}