import java.io.IOException;import java.io.File;import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JPanel;import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.BorderFactory;import javax.swing.ButtonGroup;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.util.Enumeration;import java.util.StringTokenizer;import java.util.Vector;import java.util.Hashtable;import java.util.Arrays;
import java.awt.Color; import java.sql.ResultSet;

/**<BODY><P><FONT size=4><STRONG>Program Description :</STRONG></FONT>  </P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>    Grade&nbsp;Wise Stock Status Report.</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                 Gives&nbsp;summary report of Grade and                                 Package type wise Booking(Region wise), stock, target                  dispatch, Sales return and       unclassified stock. Calculates shortage/available stock on hand. </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      123 report generate by      Mr. PDP      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mr_rpgrd.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_rpgrd.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>27/04/2004&nbsp; </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;&nbsp;        </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><P><FONT color=purple><STRONG>   </STRONG></FONT>&nbsp;</P><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="LEFT: 11px; TOP:  373px; WIDTH: 100%" width="100%" background="" borderColorDark=white borderColorLight=darkslateblue borderColor=black>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center> Columns</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>MR_INMST</TD>    <TD>&nbsp;IN_INDDT</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_INTRN</TD>    <TD>&nbsp;INT_INDNO,INT_PRDCD,INT_INDQT</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_IVTRN</TD>    <TD>&nbsp;IVT_INVQT</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>FG_OPSTK</TD>    <TD>OP_SLRQT,OP_TDSQT, OP_STKQT,OP_UCLQT</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>PR_LTMST</TD>    <TD>      <P>LT_LOTNO,LT_LINNO,LT_PENDT,LT_PESDT,LT_TGRCD,LT_PRDQT </P>      <P>(For current grade and rate of production)</P></TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_PRMST</TD>    <TD>&nbsp; PR_PRDCD,PR_PRDDS </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD>&nbsp;CMT_CGSTP='FGXXPKG'</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE><P>&nbsp;</P><P><FONT color=purple><STRONG>Report is Available in following Catagories : </STRONG></FONT></P><UL>  <LI>PS - Prime and Non Prime   <LI>SPS - GPPS &amp; HIPS   <LI>Articles of PS</LI></UL><P>This Report makes use of HTML format. Methods in cl_rbase are overridden for the same</P></BODY> */
class mr_rpgrd extends cl_rbase
{
	/**	Flag to indicate that, data is being written in table format in HTML */
	private boolean flgTBLDT;/** Data output stream for report to file*/
	private DataOutputStream dosREPORT;/**	Global string for default text color in HTML */
	private String strTXCLR="<font Color=black>";/**Global string for page break in HTML	 */
	private String strPGBRK="<P CLASS = \"breakhere\"></PRE>";/**	Hash table for Package type details<br>KEY : CMT_CODCD<br>VALUE : CMT_NCSVL */
	private Hashtable hstPKGTP;/**	Hash table for Booking details<br>KEY : INT_PRDDS|INT_PKGWT<br>VALUE : Vector in format IN_SALTP|INT_INDQT*/
	private Hashtable hstBKGDT;/**	Hash table for Stock details<br>KEY : PR_PRDDS|CMT_NCSVL of package type code<br>VALUE : OP_STKQT")|OP_TDSQT|OP_UCLQT*/
	private Hashtable hstSTKDT;/** Object Array for grade codes of GPPS Prime grades in FORMAT : PR_PRDDS|PKGWT*/
	private Object[] obaGPPS;/** Object Array for grade codes of HIPS Prime grades in FORMAT : PR_PRDDS|PKGWT*/
	private Object[] obaHIPS;/** Object Array for grade codes of GPPS Non - Prime grades in FORMAT : PR_PRDDS|PKGWT*/
	private Object[] obaGPNP;/** Object Array for grade codes of HIPS Non - Prime grades in FORMAT : PR_PRDDS|PKGWT*/
	private Object[] obaHINP;/** Object Array for grade codes of SPS GPPS  grades in FORMAT : PR_PRDDS|PKGWT*/
	private Object[] obaSPGP;/** Object Array for grade codes of SPS HIPS Prime grades in FORMAT : PR_PRDDS|PKGWT*/
	private Object[] obaSPHI;/** Panel for report data table*/
	private JPanel pnlREPORT;/**Panel for PS pirme/Nonprime catagories	                          */
	private JPanel pnlPSCAT;/**Radio button for Product type : PS*/
	private JRadioButton rdbPRDPS;/**Radio button for Product type : SPS*/
	private JRadioButton rdbPRDSP;/**Radio button for Product type : Aricles of Polystyrene*/
	private JRadioButton rdbPRDWP;/**Radio button for Product type : PS - PRIME*/
	private JRadioButton rdbPSPRM;/**Radio button for Product type : PS - NONPRIME*/
	private JRadioButton rdbPSNPR;/**Radio button for Report type : CURRENT*/
	private JRadioButton rdbCURNT;/**Radio button for report type : DAY OPENING*/
	private JRadioButton rdbDAYOP;/**Table for report data*/
	private cl_JTable tblREPORT;/**Global string for Sale type : EXPORT*/
	private final String strEXSTP_fn="12";/**Global string for Sale type : DOMESTIC*/
	private final String strDMSTP_fn="01";/**Global string for Sale type : DEEMED EXPORT*/
	private final String strDESTP_fn="03";/** Index variable for tblREPORT columns*/
	private final int intPRDDS_fn=1,intPKGWT_fn=2,intOPSTK_fn=3,intTDSQT_fn=4,intUCLQT_fn=5,intSTKAV_fn=6,intDOMBK_fn=7,intAFTDM_fn=8,intEXPBK_fn=9,intBALQT_fn=10;
	private Vector vtrPGRDS;
	private Vector vtrPGRCD;
	private Vector vtrPSDSC;
	private Vector vtrPSPRM;
	private Vector vtrNPDSC;
	private Vector vtrNPPS;
	private Vector vtrSPDSC;
	private Vector vtrSPPS;
	
	/**
	 * Constructs the form
	 * 
	 * Constructs the form
	 * 
	 * <p>Retrieves details of package types and stores in hspPKGTP<br>
	 * Retrieves details of Grade codes , generates combinations with package types for all product types and stores in respective object arrays<br>
	 */
	@SuppressWarnings("unchecked") mr_rpgrd()
	{
		super(2);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
		//RETRIEVING PAKAGE TYPE DETAILS FROM CO_CDTRN
			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXPKG'");
			if(M_rstRSSET!=null)
			{
				hstPKGTP=new Hashtable(10,0.2f);
				while(M_rstRSSET.next())
					hstPKGTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_NCSVL"));
				M_rstRSSET.close();
			}
		//RETRIEVING PRODUCT CODE DETAILS FROM CO_PRMST AND RETRIVING ALL POSSIBLE COMBINATIONS OF GRADE AND PKGTP
/*			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_PRMST order by PR_PRDCD");
			if(M_rstRSSET!=null)
			{
				Vector L_vtrGPPS=new Vector(20,5),L_vtrHIPS=new Vector(20,5),L_vtrGPNP=new Vector(20,5),L_vtrHINP=new Vector(20,5),L_vtrSPGP=new Vector(20,5),L_vtrSPHI=new Vector(20,5);
				Enumeration L_enmPKGWT=hstPKGTP.elements();
				while(M_rstRSSET.next())
				{
					L_enmPKGWT=hstPKGTP.elements();
					if(M_rstRSSET.getString("PR_PRDCD").substring(0,4).equals("5111"))
					{
						if(!M_rstRSSET.getString("PR_PRDCD").substring(6,7).equals("1"))
							while(L_enmPKGWT.hasMoreElements())
								L_vtrGPPS.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
						else
							while(L_enmPKGWT.hasMoreElements())
								L_vtrGPNP.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
					}
					else if(M_rstRSSET.getString("PR_PRDCD").substring(0,4).equals("5112"))
					{
						if(!M_rstRSSET.getString("PR_PRDCD").substring(6,7).equals("1"))
							while(L_enmPKGWT.hasMoreElements())
								L_vtrHIPS.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
						else
							while(L_enmPKGWT.hasMoreElements())
								L_vtrHINP.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
					}
					else if(M_rstRSSET.getString("PR_PRDCD").substring(0,4).equals("5211"))
						while(L_enmPKGWT.hasMoreElements())
							L_vtrSPGP.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
					else if(M_rstRSSET.getString("PR_PRDCD").substring(0,4).equals("5212"))
						while(L_enmPKGWT.hasMoreElements())
							L_vtrSPHI.addElement(M_rstRSSET.getString("PR_PRDDS")+"|"+L_enmPKGWT.nextElement().toString());
				}
				M_rstRSSET.close();
				obaGPPS=L_vtrGPPS.toArray();
				obaHIPS=L_vtrHIPS.toArray();
				obaGPNP=L_vtrGPNP.toArray();
				obaHINP=L_vtrHINP.toArray();
				obaSPGP=L_vtrSPGP.toArray();
				obaSPHI=L_vtrSPHI.toArray();
			}
*/
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
			setMatrix(20,4);
			JPanel L_pnlPRDCT=new JPanel(null);
			add(rdbPRDPS=new JRadioButton("Polystyrene"),1,1,1,1,L_pnlPRDCT,'L');
			add(rdbPRDSP=new JRadioButton("Sp. Polystyrene"),1,2,1,1,L_pnlPRDCT,'L');
			add(rdbPRDWP=new JRadioButton("Article of PS"),1,3,1,0.95,L_pnlPRDCT,'L');
			ButtonGroup L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbPRDPS);L_btgTEMP.add(rdbPRDSP);L_btgTEMP.add(rdbPRDWP);
			L_pnlPRDCT.setBorder(BorderFactory.createTitledBorder("Product Catagory"));
			add(L_pnlPRDCT,2,1,2,3,this,'L');
			pnlPSCAT=new JPanel(null);
			add(rdbPSPRM=new JRadioButton("Prime"),1,1,1,1,pnlPSCAT,'L');
			add(rdbPSNPR=new JRadioButton("Non-Prime"),1,2,1,0.95,pnlPSCAT,'L');
			L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbPSPRM);L_btgTEMP.add(rdbPSNPR);
			pnlPSCAT.setBorder(BorderFactory.createTitledBorder("PS Quality"));
			add(pnlPSCAT,4,1,2,2,this,'L');
			
			JPanel L_pnlRPTYP=new JPanel(null);
			add(rdbCURNT=new JRadioButton("Current"),1,1,1,1,L_pnlRPTYP,'L');
			add(rdbDAYOP=new JRadioButton("Day Opening"),1,2,1,0.95,L_pnlRPTYP,'L');
			L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbCURNT);L_btgTEMP.add(rdbDAYOP);
			L_pnlRPTYP.setBorder(BorderFactory.createTitledBorder("Report Type"));
			add(L_pnlRPTYP,4,3,2,2,this,'L');
			
			M_txtFMDAT.setVisible(false);M_vtrSCCOMP.removeElement(M_txtFMDAT);
			M_lblFMDAT.setVisible(false);M_vtrSCCOMP.removeElement(M_lblFMDAT);
			M_txtTODAT.setVisible(false);M_vtrSCCOMP.removeElement(M_txtTODAT);
			M_lblTODAT.setVisible(false);M_vtrSCCOMP.removeElement(M_lblTODAT);
			M_pnlRPFMT.setVisible(true);
			M_rdbHTML.setSelected(true);
			rdbPRDPS.setSelected(true);
			rdbPSPRM.setSelected(true);
			rdbDAYOP.setSelected(true);
			setMatrix(20,6);
		}catch(Exception e)
		{setMSG(e,"Child.Constructor");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**<b>TASKS</B><BR>
	 * Source != btnSAVE : Clear contents of tblREPORT<br>
	 * Focus nevigation and component state setting
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			super.actionPerformed(L_AE);
			if(tblREPORT!=null && M_objSOURC != cl_dat.M_btnSAVE_pbst)
				tblREPORT.clrTABLE();
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
					M_rdbTEXT.requestFocus();
			}
			else if(M_objSOURC==M_rdbTEXT || M_objSOURC==M_rdbHTML)
				rdbPRDPS.requestFocus();
			else if(M_objSOURC==rdbPRDPS)
			{
				pnlPSCAT.setVisible(true);
				rdbPSPRM.requestFocus();
			}
			else if(M_objSOURC==rdbPRDSP || M_objSOURC==rdbPRDWP )
			{
				pnlPSCAT.setVisible(false);
				rdbCURNT.requestFocus();
			}
			else if( M_objSOURC==rdbPSPRM || M_objSOURC==rdbPSNPR)
				rdbCURNT.requestFocus();
			else if(M_objSOURC==rdbCURNT || M_objSOURC==rdbDAYOP )
				cl_dat.M_btnSAVE_pbst.requestFocus();
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
		if(M_rdbTEXT.isSelected() && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) && M_cmbDESTN.getSelectedIndex()==0)
		{
			setMSG("Select Printer ..",'E');
			M_cmbDESTN.requestFocus();
			return false;
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst) && M_cmbDESTN.getItemCount() == 0)
		{
			setMSG("Please enter Receipents ..",'E');
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
	 * <p>For Day Opening : Booking upto previous date is considered<br>
	 * For As on : Booking upto Report time is considered<br>
	 * <b>In both cases, stock figures are of day opening</B>
	 * 	 
	 */
	@SuppressWarnings("unchecked") void exePRINT()
	{
		try
		{
			if(!vldDATA())
				return;
			if(rdbCURNT.isSelected())
			{
				M_strSQLQRY = "update fg_opstk a set op_stkqt = (select sum(b.st_stkqt) from fg_stmst b where a.op_cmpcd=b.st_cmpcd and a.op_prdtp=b.st_prdtp and a.op_prdcd = b.st_prdcd and a.op_pkgtp = b.st_pkgtp and a.op_CMPCD='"+cl_dat.M_strCMPCD_pbst+"')";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				M_strSQLQRY = "update fg_opstk a set op_uclqt = (select sum(b.st_uclqt) from fg_stmst b where a.op_cmpcd=b.st_cmpcd and a.op_prdtp=b.st_prdtp and a.op_prdcd = b.st_prdcd and a.op_pkgtp = b.st_pkgtp and a.op_CMPCD='"+cl_dat.M_strCMPCD_pbst+"')";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				if(cl_dat.exeDBCMT("exePRINT"))
					setMSG("Current Stock updated",'N');
			}

			M_intLINNO=1;M_intPAGNO=1;
			M_strSQLQRY="select INT_PRDDS,INT_PKGWT,(isnull(int_indqt,0)-isnull(int_fcmqt,0))-isnull(SUM(isnull(IVT_INVQT,0)),0) INT_INDQT, "
					+" IN_MKTTP,IN_SALTP "
					+" from VW_INTRN "
					+" left outer join MR_IVTRN  on INT_CMPCD=IVT_CMPCD and INT_INDNO=IVT_INDNO AND INT_PRDCD=IVT_PRDCD "
					+" and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL<>'X' and CONVERT(varchar,ivt_invdt,101)<"+(rdbCURNT.isSelected()==true ? "=" : " ")+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where int_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' "
					+" and in_mkttp='01' and  IN_BKGDT <"+(rdbCURNT.isSelected()==true ? "=" : " ")+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
					+" and in_stsfl<>'X' and INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND int_stsfl<>'X' and (isnull(int_indqt,0)-isnull(int_fcmqt,0))>0 and IN_SALTP in ('"+strEXSTP_fn+"','"+strDMSTP_fn+"','"+strDESTP_fn+"')  and INT_SBSCD1 in "+M_strSBSLS
					+" group by IN_MKTTP ,IN_SALTP,IN_INDNO,INT_PKGWT,INT_PRDDS,INT_INDQT,INT_FCMQT"
					+" having  (isnull(int_indqt,0)-isnull(int_fcmqt,0))-isnull(SUM(isnull(IVT_INVQT,0)),0)>0 order by int_prdds";
			System.out.println("1. :"+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			hstBKGDT=new Hashtable(30,0.3f);
			String  L_strTEMP="";
			Vector L_vtrTEMP=null;//System.out.println(-1);
			while(M_rstRSSET.next())
			{//System.out.println(-2);
				L_strTEMP="";
				L_strTEMP+=nvlSTRVL(M_rstRSSET.getString("IN_SALTP")," ");
				L_strTEMP+="|"+nvlSTRVL(M_rstRSSET.getString("INT_INDQT")," ");
				if(!hstBKGDT.containsKey(M_rstRSSET.getString("INT_PRDDS")+"|"+M_rstRSSET.getString("INT_PKGWT")))
					L_vtrTEMP=new Vector(1,1);
				else
					L_vtrTEMP=(Vector)hstBKGDT.get(M_rstRSSET.getString("INT_PRDDS")+"|"+M_rstRSSET.getString("INT_PKGWT"));
				L_vtrTEMP.addElement(L_strTEMP);
				hstBKGDT.put(M_rstRSSET.getString("INT_PRDDS")+"|"+M_rstRSSET.getString("INT_PKGWT"),L_vtrTEMP);
			}
			M_rstRSSET.close();
		//COLLECTING SSTOCK DETAILS
//			System.out.println(0);
			M_strSQLQRY="Select * from FG_OPSTK,CO_PRMST where (OP_SLRQT>0 or OP_TDSQT>0 or OP_DOSQT>0 or OP_STKQT>0 or OP_DOUQT>0 or OP_UCLQT>0) and PR_PRDCD=OP_PRDCD and OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				hstSTKDT=new Hashtable(30,0.3f);
				while(M_rstRSSET.next())
				{//	System.out.print(M_rstRSSET.getString("PR_PRDDS"));
					hstSTKDT.put(M_rstRSSET.getString("PR_PRDDS")+"|"+hstPKGTP.get(M_rstRSSET.getString("OP_PKGTP")),
								 (Float.parseFloat(M_rstRSSET.getString(rdbCURNT.isSelected() ? "OP_STKQT" : "OP_DOSQT"))>0 == true ? M_rstRSSET.getString(rdbCURNT.isSelected() ? "OP_STKQT" : "OP_DOSQT") : " " )
								+(Float.parseFloat(M_rstRSSET.getString("OP_TDSQT"))>0 == true ? "|"+M_rstRSSET.getString("OP_TDSQT") : "| ")
								+(Float.parseFloat(M_rstRSSET.getString(rdbCURNT.isSelected() ? "OP_UCLQT" : "OP_DOUQT"))>0 == true ? "|"+M_rstRSSET.getString(rdbCURNT.isSelected() ? "OP_UCLQT" : "OP_DOUQT") : "| "));
				}
			}
			
			String[] L_staCOLHD=new String[] {"FL","Grade","Package","Class. Stk.","Target","Unclassified","Stk. Available","Domst. Bkg.","After Domst","Exp. Bkg.","Balance"};
			int[] L_staCOLSZ=new int[]{20,70,80,70,70,70,70,70,70,70,70};
			if(pnlREPORT!=null)
				pnlREPORT.removeAll();
			if(pnlREPORT==null)
				pnlREPORT=new JPanel(null);
			tblREPORT = crtTBLPNL1(pnlREPORT,L_staCOLHD,1000,1,1,11,6,L_staCOLSZ,new int[]{0});
			tblREPORT.setEnabled(false);
			add(pnlREPORT,6,1,12,6,this,'L');
			pnlREPORT.setVisible(false);
			updateUI();
			int L_intROWID=0;
			float[] L_flaTOTAL=new float[8];
			float[] L_flaCTTOT=new float[8];
			int L_intTEMP=0;
			tblREPORT.setRowColor(0,Color.blue);
			if(rdbPSPRM.isSelected())
			{
				boolean L_flgDATA=false;
				for(int i=0;i<vtrPSPRM.size();i++)
				{
					L_intTEMP=L_intROWID++;
					if(((Object[])vtrPSPRM.elementAt(i)).length>0)
						L_intROWID=setTBLDATA(L_intROWID,(Object[])vtrPSPRM.elementAt(i));
					if(L_intROWID==L_intTEMP+1)//IF NO DATA ADDED, SET ROW POINTER TO ORIGINAL
						L_intROWID-=2;
					else //ELSE DISPLAY HEADING AT THE TOP & CALCULATE GRAND TOTAL
					{
						tblREPORT.setRowColor(L_intTEMP,Color.blue);
						tblREPORT.setValueAt(vtrPSDSC.elementAt(i),L_intTEMP,1);
						for(int j=0;j<L_flaTOTAL.length;j++)
							L_flaTOTAL[j]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID-1,j+3).toString(),"0.0"));
						L_flgDATA=true;
					}
					
					L_intROWID++;
					
				}
			}
			else if(rdbPSNPR.isSelected())
			{
				boolean L_flgDATA=false;
				for(int i=0;i<vtrNPPS.size();i++)
				{
					L_intTEMP=L_intROWID++;
					if(((Object[])vtrNPPS.elementAt(i)).length>0)
					{
						L_intROWID=setTBLDATA(L_intROWID,(Object[])vtrNPPS.elementAt(i));
					}
					if(L_intROWID==L_intTEMP+1)//IF NO DATA ADDED, SET ROW POINTER TO ORIGINAL
						L_intROWID-=2;
					else//ELSE DISPLAY HEADING AT THE TOP & CALCULATE GRAND TOTAL
					{
						tblREPORT.setRowColor(L_intTEMP,Color.blue);
						tblREPORT.setValueAt(vtrNPDSC.elementAt(i).toString()+ (((vtrNPDSC.elementAt(i).equals("GPPS"))||(vtrNPDSC.elementAt(i).equals("HIPS")))==true ? " Non Prime " : "" ),L_intTEMP,1);
						L_flgDATA=true;
						for(int j=0;j<L_flaTOTAL.length;j++)
							L_flaTOTAL[j]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID-1,j+3).toString(),"0.0"));
					}
					L_intROWID++;
				}
			}
			else
			{
				boolean L_flgDATA=false;
				for(int i=0;i<vtrSPPS.size();i++)
				{
					L_intTEMP=L_intROWID++;
					if(((Object[])vtrSPPS.elementAt(i)).length>0)
						L_intROWID=setTBLDATA(L_intROWID,(Object[])vtrSPPS.elementAt(i));
					if(L_intROWID==L_intTEMP+1)//IF NO DATA ADDED, SET ROW POINTER TO ORIGINAL
						L_intROWID-=2;
					else//ELSE DISPLAY HEADING AT THE TOP & CALCULATE GRAND TOTAL
					{
						tblREPORT.setRowColor(L_intTEMP,Color.blue);
						tblREPORT.setValueAt(vtrSPDSC.elementAt(i),L_intTEMP,1);
						for(int j=0;j<L_flaTOTAL.length;j++)
							L_flaTOTAL[j]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID-1,j+3).toString(),"0.0"));
						L_flgDATA=true;
					}
					L_intROWID++;
				}
			//DISPLAY SP. GPPS VALUES
//		if(L_intROWID!=-1)
//				{
//				}
/*			//DISPLAY SPS_GP TOTALS
				for(int j=0;j<L_flaTOTAL.length;j++)
					L_flaTOTAL[j]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID-1,j+3).toString(),"0.0"));
			//DISPLAY SPS_HI DATA
				if(L_intROWID!=-1)
				{
					tblREPORT.setRowColor(++L_intROWID,Color.blue);
					tblREPORT.setValueAt("Sp. HIPS",L_intROWID++,1);
					L_intROWID=setTBLDATA(L_intROWID,obaSPHI);
				}
			//DISPLAY SPS_HI TOTALS
				for(int j=0;j<L_flaTOTAL.length;j++)
					L_flaTOTAL[j]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(L_intROWID-1,j+3).toString(),"0.0"));
			//DISPLAY SPS TOTALS
				L_intROWID++;
*/			}
				tblREPORT.setRowColor(L_intROWID,Color.magenta);
				tblREPORT.setValueAt("GRAND TOTAL",L_intROWID,intPRDDS_fn);
				for(int j=0;j<L_flaTOTAL.length;j++)
				{
					if(L_flaTOTAL[j]<0.0f)
						tblREPORT.setCellColor(L_intROWID,j+3,Color.red);
					tblREPORT.setValueAt(setNumberFormat(L_flaTOTAL[j],3),L_intROWID,j+3);
				}
			//WRITE TO FILE
			exePRNRPT();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst) || (M_rdbHTML.isSelected() && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst)))
			{//DISPLAY HTML REPORT IN BROWSER
					Runtime r = Runtime.getRuntime();
					Process p = null;
					if(M_rdbTEXT.isSelected())
						p  = r.exec("c:\\windows\\wordpad.exe "+ cl_dat.M_strREPSTR_pbst+"mr_rpgrd.doc"); 
					else
						p  = r.exec("c:\\program files\\internet explorer\\iexplore.exe "+cl_dat.M_strREPSTR_pbst+"mr_rpgrd.htm");
			}
			//PRINT TO PRINTER IN PRINT OPTION IN TEXT FORMAT
			else if(M_rdbTEXT.isSelected() && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst))
				doPRINT(cl_dat.M_strREPSTR_pbst+"mr_rpgrd.doc");
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst))
			//E_MAIL THE RESPECTIVE FILE TO RECEIPENTS, ONE BY ONE
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					((cl_eml)Class.forName("cl_eml").newInstance()).sendfile(M_cmbDESTN.getItemAt(i).toString(),cl_dat.M_strREPSTR_pbst+"mr_rpgrd."+(M_rdbTEXT.isSelected() ? "doc" : "htm"),"Grade wise stock status on "+cl_dat.M_strLOGDT_pbst,"");
		}catch(Exception e)
		{setMSG(e,"exePRINT");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
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
	private int setTBLDATA(int P_intROWID,Object[] P_obaPSCAT)
	{
		try
		{
			String  L_strTEMP="";
			Vector L_vtrTEMP=null;
			float[] L_flaTOTAL=new float[8];
			for(int i=0; i<P_obaPSCAT.length;i++)
			{
				if(hstBKGDT.containsKey(P_obaPSCAT[i]) || hstSTKDT.containsKey(P_obaPSCAT[i]) )
				{
					StringTokenizer L_stkTEMP=null;
					if(hstBKGDT.containsKey(P_obaPSCAT[i]))
					{
						L_vtrTEMP=(Vector)hstBKGDT.get(P_obaPSCAT[i]);
						for(int j=0;j<L_vtrTEMP.size();j++)
						{
							L_stkTEMP=new StringTokenizer((String)L_vtrTEMP.elementAt(j),"|");
							L_strTEMP=L_stkTEMP.nextToken();
							if(L_strTEMP.equals(strDMSTP_fn))
							{
								float L_fltOLDVL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intDOMBK_fn).toString(),"0.0"));
								tblREPORT.setValueAt(setNumberFormat(L_fltOLDVL+Float.parseFloat(L_stkTEMP.nextToken()),3),P_intROWID,intDOMBK_fn);
							}
							else if(L_strTEMP.equals(strDESTP_fn))
							{
								float L_fltOLDVL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intDOMBK_fn).toString(),"0.0"));
								tblREPORT.setValueAt(setNumberFormat(L_fltOLDVL+Float.parseFloat(L_stkTEMP.nextToken()),3),P_intROWID,intDOMBK_fn);
							}
							else if(L_strTEMP.equals(strEXSTP_fn))
							{
								float L_fltOLDVL=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intEXPBK_fn).toString(),"0.0"));
								tblREPORT.setValueAt(setNumberFormat(L_fltOLDVL+Float.parseFloat(L_stkTEMP.nextToken()),3),P_intROWID,intEXPBK_fn);
							}
						}
					}
					if(hstSTKDT.containsKey(P_obaPSCAT[i]))
					{//OP_PKGTP")),("OP_STKQT")+"|"+("OP_TDSQT")+"|"+("OP_SLRQT")+"|"+("OP_UCLQT"));
						L_stkTEMP=new StringTokenizer((String)hstSTKDT.get(P_obaPSCAT[i]),"|");
						tblREPORT.setValueAt(L_stkTEMP.nextToken(),P_intROWID,intOPSTK_fn);
						tblREPORT.setValueAt(L_stkTEMP.nextToken(),P_intROWID,intTDSQT_fn);
						tblREPORT.setValueAt(L_stkTEMP.nextToken(),P_intROWID,intUCLQT_fn);
						
					}
					float L_fltSTKAV=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intOPSTK_fn).toString(),"0.0"))
									+Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intUCLQT_fn).toString(),"0.0"))
									-Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intTDSQT_fn).toString(),"0.0"));
					if(L_fltSTKAV<0.0f)
						tblREPORT.setCellColor(P_intROWID,intSTKAV_fn,Color.red);
					tblREPORT.setValueAt(setNumberFormat(L_fltSTKAV,3),P_intROWID,intSTKAV_fn);
					float L_fltAFTDM=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intSTKAV_fn).toString(),"0.0"))
								  -Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intDOMBK_fn).toString(),"0.0"));
					if(L_fltAFTDM<0.0f)
						tblREPORT.setCellColor(P_intROWID,intAFTDM_fn,Color.red);
					tblREPORT.setValueAt(setNumberFormat(L_fltAFTDM,3),P_intROWID,intAFTDM_fn);
					float L_fltBALQT=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intAFTDM_fn).toString(),"0.0"))
									-Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,intEXPBK_fn).toString(),"0.0"));
					if(L_fltBALQT<0.0f)
						tblREPORT.setCellColor(P_intROWID,intBALQT_fn,Color.red);
					tblREPORT.setValueAt(setNumberFormat(L_fltBALQT,3),P_intROWID,intBALQT_fn);
					
					L_stkTEMP=new StringTokenizer((String) P_obaPSCAT[i],"|");
					L_strTEMP=L_stkTEMP.nextToken();
					if(!tblREPORT.getValueAt(P_intROWID-1,intPRDDS_fn).equals(L_strTEMP))
						tblREPORT.setValueAt(L_strTEMP,P_intROWID,intPRDDS_fn);
					tblREPORT.setValueAt(L_stkTEMP.nextToken(),P_intROWID,intPKGWT_fn);
					for(int j=0;j<L_flaTOTAL.length;j++)
						L_flaTOTAL[j]+=Float.parseFloat(nvlSTRVL(tblREPORT.getValueAt(P_intROWID,j+3).toString(),"0.0"));
					P_intROWID++;
				}
				
			}
			tblREPORT.setRowColor(P_intROWID,Color.blue);
				tblREPORT.setValueAt("TOTAL",P_intROWID,1);
			for(int j=0;j<L_flaTOTAL.length;j++)
			{
				if(L_flaTOTAL[j]<0.0f)
					tblREPORT.setCellColor(P_intROWID,j+3,Color.red);
				tblREPORT.setValueAt(setNumberFormat(L_flaTOTAL[j],3),P_intROWID,j+3);
			}
			P_intROWID++;
			return P_intROWID;
		}catch(Exception e)
		{setMSG(e,"setTBLDATA");
		return -1;}
	}
	/**Method to write report to file, text or html	 */
	private void exePRNRPT()
	{
		try
		{
			File filREPORT=null;
			
			if(M_rdbHTML.isSelected())
				filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_rpgrd.htm");
			else
				filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_rpgrd.doc");
			FileOutputStream filOUT=new FileOutputStream(filREPORT);
			dosREPORT=new DataOutputStream(filOUT);

			String L_strREPNM="GRADEWISE STOCK STATUS ";
			if(rdbPRDPS.isSelected())
			{
				L_strREPNM+="( PS - ";
				if(rdbPSPRM.isSelected())
					L_strREPNM+="PRIME )";
				else
					L_strREPNM+="NON-PRIME )";
			}
			else if(rdbPRDSP.isSelected())
				L_strREPNM+="( SPS )";
			else if(rdbPRDWP.isSelected())
				L_strREPNM+="( Art. of PS )";
		
			if(M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("<HTML><HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD>");
				dosREPORT.writeBytes("<BODY bgColor=ghostwhite><P><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\"> <TR><TD><IMG src=\"file://f:\\exec\\splerp2\\spllogo_old.gif\" style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></TD><TD><P align=left><STRONG><FONT face=Arial size=5>"+cl_dat.M_strCMPNM_pbst+"</FONT></STRONG></P><STRONG><FONT face=Arial size=4><p align=left> "+L_strREPNM+"</font></><p><font face=Arial size=3>"+(rdbDAYOP.isSelected()==true ? " (Day Opening Of " : " (As On ")+cl_dat.M_strLOGDT_pbst+") </P> </TD><TD><p><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</p><p><FONT face=Arial size=2>Page No. : 1</P><TD> </TR></TABLE><HR><FONT face=\"Comic Sans MS\">");
			}
			else
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				dosREPORT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst,80));
				dosREPORT.writeBytes(padSTRING('R',"Page No. : 1",20));
				crtNWLIN(dosREPORT);
				dosREPORT.writeBytes(padSTRING('C',L_strREPNM +" "+(rdbDAYOP.isSelected()==true ? " (Day Opening Of " : " (As On ")+cl_dat.M_strLOGDT_pbst+")" ,80));
				crtNWLIN(dosREPORT);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			prnHEADR();
			//System.out.println("select LT_LOTNO from PR_LTMST where LT_LINNO in ('10','11','12') and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PENDT is null order by LT_LINNO ");
			M_rstRSSET=cl_dat.exeSQLQRY0("select LT_LOTNO from PR_LTMST where LT_LINNO in ('10','11','12') and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PENDT is null order by LT_LINNO ");
			String L_strLIN10=null,L_strLIN11=null,L_strLIN12=null;
			String L_strGRD10="",L_strGRD11="",L_strGRD12="";
			if(M_rstRSSET!=null)
			{
				while (M_rstRSSET.next())
				{
					//System.out.println("Select LT_LINNO,LT_LOTNO,LT_PRDQT,LT_PENDT-LT_PSTDT LT_DURN,PR_PRDDS from PR_LTMST,CO_PRMST where PR_PRDCD=LT_TPRCD and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO='"+String.valueOf(M_rstRSSET.getInt("LT_LOTNO")-1)+"'");
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY0("Select LT_LINNO,LT_LOTNO,LT_PRDQT,LT_PENDT-LT_PSTDT LT_DURN,PR_PRDDS from PR_LTMST,CO_PRMST where PR_PRDCD=ltrim(str(LT_TPRCD,20,0)) and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO='"+String.valueOf(M_rstRSSET.getInt("LT_LOTNO")-1)+"'");
					if(L_rstRSSET!=null)
					{
						while (L_rstRSSET.next())
						{
							if(L_rstRSSET.getString("LT_LINNO").equals("10"))
							{
								L_strLIN10="Grade Under Production in Line : "+L_rstRSSET.getString("LT_LINNO")+" is "+L_rstRSSET.getString("PR_PRDDS")+" @ "+setNumberFormat((L_rstRSSET.getFloat("LT_PRDQT")/L_rstRSSET.getFloat("LT_DURN"))*10000.0f,3)+" MT/hr";
								L_strGRD10=L_rstRSSET.getString("PR_PRDDS");
							}
							else if(L_rstRSSET.getString("LT_LINNO").equals("11"))
							{
								L_strLIN11="Grade Under Production in Line : "+L_rstRSSET.getString("LT_LINNO")+" is "+L_rstRSSET.getString("PR_PRDDS")+" @ "+setNumberFormat((L_rstRSSET.getFloat("LT_PRDQT")/L_rstRSSET.getFloat("LT_DURN"))*10000.0f,3)+" MT/hr";
								L_strGRD11=L_rstRSSET.getString("PR_PRDDS");
							}
							else if(L_rstRSSET.getString("LT_LINNO").equals("12"))
							{
								L_strLIN12="Grade Under Production in Line : "+L_rstRSSET.getString("LT_LINNO")+" is "+L_rstRSSET.getString("PR_PRDDS")+" @ "+setNumberFormat((L_rstRSSET.getFloat("LT_PRDQT")/L_rstRSSET.getFloat("LT_DURN"))*10000.0f,3)+" MT/hr";
								L_strGRD12=L_rstRSSET.getString("PR_PRDDS");
							}
						}
					}
				}
			}
			prnFMTCHR(dosREPORT,M_strCPI17);
			for(int i=0;i<tblREPORT.getRowCount();i++)
			{
				if(i<tblREPORT.getRowCount()-2)
					if(tblREPORT.getValueAt(i+1,1).toString().length()==0 && tblREPORT.getValueAt(i,1).toString().length()==0
					   && tblREPORT.getValueAt(i+1,2).toString().length()==0 && tblREPORT.getValueAt(i,2).toString().length()==0)
						break;
				
				if(tblREPORT.getValueAt(i,1).toString().equals(L_strGRD10) || tblREPORT.getValueAt(i,1).toString().equals(L_strGRD11) || tblREPORT.getValueAt(i,1).toString().equals(L_strGRD12))
				{
					setTXCLR(dosREPORT,Color.green);
					prnFMTCHR(dosREPORT,M_strBOLD);
				}
				else 
					setTXCLR(dosREPORT,tblREPORT.getCellColor(i,1));
				int L_intCOLWD=12;
				if(M_rdbHTML.isSelected())
					L_intCOLWD=30;
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(tblREPORT.getValueAt(i,1).toString(),"-"),L_intCOLWD));
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,2));
				dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(tblREPORT.getValueAt(i,2).toString(),"-"),L_intCOLWD));
				String L_strREPLC="-";
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,3));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,3).toString(),L_strREPLC),L_intCOLWD));
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,4));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,4).toString(),L_strREPLC),L_intCOLWD));
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,5));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,5).toString(),L_strREPLC),L_intCOLWD));
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,6));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,6).toString(),L_strREPLC),L_intCOLWD));
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,7));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),L_strREPLC),L_intCOLWD));
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,8));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,8).toString(),L_strREPLC),L_intCOLWD));
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,9));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,9).toString(),L_strREPLC),L_intCOLWD));
				setTXCLR(dosREPORT,tblREPORT.getCellColor(i,10));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(tblREPORT.getValueAt(i,10).toString(),L_strREPLC),L_intCOLWD));
				crtNWLIN(dosREPORT);
			}
			endTABLE(dosREPORT);
			setTXCLR(dosREPORT,Color.black);
			if(L_strLIN10==null)
				dosREPORT.writeBytes(padSTRING('R',"Line 10 under shut down",80));
			else
				dosREPORT.writeBytes(padSTRING('R',L_strLIN10,80));
			crtNWLIN(dosREPORT);
			if(L_strLIN11==null)
				dosREPORT.writeBytes(padSTRING('R',"Line 11 under shut down",80));
			else
				dosREPORT.writeBytes(padSTRING('R',L_strLIN11,80));
			crtNWLIN(dosREPORT);
			if(L_strLIN12==null)
				dosREPORT.writeBytes(padSTRING('R',"Line 12 under shut down",80));
			else
				dosREPORT.writeBytes(padSTRING('R',L_strLIN12,80));
			crtNWLIN(dosREPORT);
			dosREPORT.flush();
			dosREPORT.close();
		}catch(Exception e)
		{setMSG(e,"exePRNRPT");}
	}
	/**Method to print header of report 	 */
	private void prnHEADR() throws Exception
	{
		crtTBL(dosREPORT,true);
		prnFMTCHR(dosREPORT,M_strCPI17);
		prnFMTCHR(dosREPORT,M_strBOLD);
		crtHRLIN(dosREPORT,"-",150);
		setTXCLR(dosREPORT,Color.red);
		dosREPORT.writeBytes(padSTRING('R',"Grade",12));
		dosREPORT.writeBytes(padSTRING('C',"Pkg. Wt.",12));
		dosREPORT.writeBytes(padSTRING('L',"Clsf. Stk.",12));
		dosREPORT.writeBytes(padSTRING('L',"Target",12));
		dosREPORT.writeBytes(padSTRING('L',"Un-clsf. Stk.",12));
		dosREPORT.writeBytes(padSTRING('L',"Avl. Stk.",12));
		dosREPORT.writeBytes(padSTRING('L',"Domst. Bkg.",12));
		dosREPORT.writeBytes(padSTRING('L',"After Domst.",12));
		dosREPORT.writeBytes(padSTRING('L',"Exp. Bkg.",12));
		dosREPORT.writeBytes(padSTRING('L',"Balance",12));
		crtNWLIN(dosREPORT);
		crtHRLIN(dosREPORT,"-",150);
		prnFMTCHR(dosREPORT,M_strNOBOLD);
		crtNWLIN(dosREPORT);
	}
	/**Method to print footer of report	 */
	private void prnRPFTR() throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			endTABLE(dosREPORT);
			dosREPORT.writeBytes(strPGBRK);
			crtNWLIN(dosREPORT);
			prnHEADR();
		}
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
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=black cellPadding=0 cellSpacing=0 width=\"100%\" align=center>");
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
		if(M_intLINNO>35)
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