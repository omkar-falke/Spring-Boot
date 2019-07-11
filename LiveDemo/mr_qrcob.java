/*
	Name			: Customer Order Outstanding Report cum Query
	System			: MKT
	Author			: AAP
	Version			: v2.0.0
	Last Modified	: 21/05/2004 SUBSYSTEM FILTER ADDED.
	Documented On	: 20/05/2004
*/
import java.io.File;import java.io.DataOutputStream;import java.io.FileOutputStream;
import javax.swing.*;
import java.awt.event.*;
import java.util.Hashtable;import java.util.Vector;import java.util.StringTokenizer;import java.util.Arrays;import java.util.Calendar;
import java.awt.Color;
import java.sql.ResultSetMetaData;import java.sql.ResultSet;

/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>Customer Order Booking Report cum Query</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                  Gives daily order booking       report, Customised report for a given period </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Existing reports      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\mr_qrcob.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_qrcob.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>DNM </TD></TR>  <TR>    <TD>Date</TD>    <TD>08/11/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>For Base classes revision and Subsystem implementation</TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>AAP</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>20/09/2003</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>2.0.0.</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>VW_CRFWD</TD>    <TD>&nbsp; </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P><P>&nbsp;</P><P><FONT color=purple><STRONG>Features : </STRONG></FONT></P><UL>  <LI><FONT color=black>Sort order available for : Zone, Sale Type, Distributor,   grade </FONT>  <LI><FONT color=black>Report for Sp. or all Zone, Sale Type, Distributor,   Grade along wiht totals </FONT>  <LI><FONT color=black>Report on Accountaable / All, region wise Daily Report   of Obders booked Detailed report or summary report of totals only. </FONT>  <LI><FONT color=black>For market type Polystyrene, Styrene, Captive   Consumption, Wood profile </FONT>  <LI><FONT color=black>Daily report includes summary of bookings for day, month   and carry forward</FONT></LI></UL>*/
class mr_qrcob extends cl_rbase
{
	/** Data output stream for report to file*/
	private DataOutputStream L_doutREPORT;/** Current heading / catagory in report*/
	private String strHEADNG;/** Hashtables for toatlised figures in report*/
	private Hashtable<String,String> hstDLYTOT,hstMONTOT,hstCRFTOT;/** Report order set by user*/
	private int L_intORDR1=-1,L_intORDR2=-1,L_intORDR3=-1,L_intORDR4=-1;/** For table column header Description*/
	private String[] staCOLHD;/** Table to hold report data*/
	private cl_JTBL tblREPORT;	/**Panel for Report table	 */
	private JPanel pnlREPORT=new JPanel(null);/** Index of old selection in List. For drag and drop provision*/
	private int intOLDSL;/** String Array for table header*/
	private final String[] staCOLHD_fn=new String[]{"Market Type","Ind. No","Sale Type",
                                            	    "Buyer","Distributor","Grade",
                                            	    "Indent Qty","Zone","Base Rate",
                                            	    "C.Dsc.","D.Dsc.","T.Dsc.",
                                            	    "Term","Consignee","Del. Tp."};/** Column width in report*/
	private final int[] inaCOLWD_fn=new int[]{0,9,15,20,20,20,12,10,10,7,7,7,8,0,4};/** column index in result set*/
	private final int	intMKTTP_fn=1,
						intINDNO_fn=2,
						intSALTP_fn=3,
						intZONCD_fn=8,
						intBYRCD_fn=4,
						intDSRCD_fn=5,
						intPRDDS_fn=6,
						intINDQT_fn=7,
						intBASRT_fn=8,
						intCDCVL_fn=9,
						intDDCVL_fn=10,
						intTDCVL_fn=11,
						intAPTVL_fn=12,
						intCNSNM_fn=13,
						intDTPCD_fn=14,/** Display total zone-wise*/
						intPRDCD_fn=15;
	private JCheckBox chbZONTOT;/** Display total Region-wise*/
	private JCheckBox chbRGNWS;/** Display total Sale type-wise*/
	private JCheckBox chbSTPTOT;/** Display total Distributor-wise*/
	private JCheckBox chbDSTTOT;/** Display total Grade-wise*/
	private JCheckBox chbGRDTOT;/** Marcket type for which report is to be generated. Populated dynamatically from CDTRN*/
	private JComboBox cmbMKTTP;/** Report of all products*/
	private JRadioButton rdbRPTAL;/** Report of accountable products*/
	private JRadioButton rdbRPTSP;/** Report of all zones*/
	private JRadioButton rdbZONAL;/** Report of specific zone*/
	private JRadioButton rdbZONSP;/** Report of all sale types*/
	private JRadioButton rdbSTPAL;/** Report of specific sale type*/
	private JRadioButton rdbSTPSP;/** Report of all distributors*/
	private JRadioButton rdbDSTAL;/** Report of specific distributor*/
	private JRadioButton rdbDSTSP;/** Report of all grades*/
	private JRadioButton rdbGRDAL;/** Report of Specific distributor*/
	private JRadioButton rdbGRDSP;/** Details report*/
	private JRadioButton rdbDETAIL;/** Summary report */
	private JRadioButton rdbSUMRY;/** Daily report*/
	private JRadioButton rdbDAILY;/** Button groups for respective radiobuttons*/
	private ButtonGroup  btgRPCAT,
						btgRPT,
						btgZON,
						btgSTP,
						btgDST,
						btgGRD;/**Panel for distributor criteria selection*/
	private JPanel pnlDSTSL;/**Panel for Grade criteria selection*/
	private JPanel pnlGRDSL;/** for Total report data*/
	private Vector<String[]> vtrQRDAT;/**for sort order list items*/
	private Vector<String> vtrODRBY;/**for sort order*/
	private JList lstODRBY;/**for distributor name in distributor specific report. Help available*/
	private JTextField txtDSTNM;/**for Grade description in grade specific report. Help available*/
	private JTextField txtGRDDS;/**Object array for report data storage, filteration and sorting*/
	private Object[] obaRPDAT;
	//private String strSBSCD;
	private String strMKTTP_LST = "('01','04','05')";    // Polystyrene Wood Profile & Master Batch
	/**Construct the screen and populates Market Type combo from CO_CDTRN	 */
	mr_qrcob()
	{
		super(2);
		try
		{
			setMatrix(15,6);
			setVGAP(13);
			JPanel L_pnlTEMP=new JPanel(null);
			add(cmbMKTTP=new JComboBox(),1,1,1,1,L_pnlTEMP,'R');
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder(" Market Type "));
			add(L_pnlTEMP,5,1,1.5,1,this,'L');
			L_pnlTEMP=new JPanel(null);
			add(rdbDETAIL=new JRadioButton("Detail"),1,1,1,1,L_pnlTEMP,'L');
			add(rdbSUMRY=new JRadioButton("Summary"),1,2,1,1,L_pnlTEMP,'L');
			add(rdbDAILY=new JRadioButton("Daily Report"),1,3,1,.91,L_pnlTEMP,'L');
			btgRPCAT=new ButtonGroup();btgRPCAT.add(rdbDETAIL);btgRPCAT.add(rdbSUMRY);btgRPCAT.add(rdbDAILY);
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder(" Report Type "));
			add(L_pnlTEMP,5,2,1.5,3,this,'L');
			setMatrix(15,6);
			setVGAP(13);
		//ADDING REPORT CRITERIA PANEL
			L_pnlTEMP=new JPanel(null);
			setDefaultGap();setMatrix(20,7);
			add(rdbRPTAL=new JRadioButton("All"),1,1,1,0.75,L_pnlTEMP,'L');
			add(rdbRPTSP=new JRadioButton("Accountable"),2,1,1,0.951,L_pnlTEMP,'L');
			add(chbRGNWS=new JCheckBox("Region Wise"),3,1,1,0.951,L_pnlTEMP,'L');
			chbRGNWS.setVisible(false);
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder(" Report On "));
			btgRPT=new ButtonGroup();
			btgRPT.add(rdbRPTAL);btgRPT.add(rdbRPTSP);
			setMatrix(15,6);
			setVGAP(13);
			add(L_pnlTEMP,2,1,2.8,1,this,'L');
		//ADDING ZONE SELECION PANEL	
			L_pnlTEMP=new JPanel(null);
			setDefaultGap();setMatrix(20,7);
			add(rdbZONAL=new JRadioButton("All"),1,1,1,0.91,L_pnlTEMP,'L');
			add(rdbZONSP=new JRadioButton("Specific"),2,1,1,0.91,L_pnlTEMP,'L');
			add(chbZONTOT=new JCheckBox("Total"),3,1,1,0.91,L_pnlTEMP,'L');
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder(" Zone "));
			btgZON=new ButtonGroup();
			btgZON.add(rdbZONAL);btgZON.add(rdbZONSP);
			setMatrix(15,6);
			setVGAP(13);
			add(L_pnlTEMP,2,2,2.8,1,this,'L');
		//ADDING SALE TYPE SELECTION PANEL
			L_pnlTEMP=new JPanel(null);
			setDefaultGap();setMatrix(20,7);
			add(rdbSTPAL=new JRadioButton("All"),1,1,1,0.91,L_pnlTEMP,'L');
			add(rdbSTPSP=new JRadioButton("Specific"),2,1,1,0.91,L_pnlTEMP,'L');
			add(chbSTPTOT=new JCheckBox("Total"),3,1,1,0.91,L_pnlTEMP,'L');
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder(" Sale Type "));
			btgSTP=new ButtonGroup();
			btgSTP.add(rdbSTPAL);btgSTP.add(rdbSTPSP);
			setMatrix(15,6);
			setVGAP(13);
			add(L_pnlTEMP,2,3,2.8,1,this,'L');
		//ADDING DISTRIBUTOR SELECTION PANEL
			pnlDSTSL=new JPanel(null);
			setDefaultGap();setMatrix(20,7);
			add(rdbDSTAL=new JRadioButton("All"),1,1,1,0.91,pnlDSTSL,'L');
			add(rdbDSTSP=new JRadioButton("Specific"),2,1,1,0.91,pnlDSTSL,'L');
			add(chbDSTTOT=new JCheckBox("Total"),3,1,1,0.91,pnlDSTSL,'L');
			add(txtDSTNM=new JTextField(),3,1,1,0.91,pnlDSTSL,'L');
			pnlDSTSL.setBorder(BorderFactory.createTitledBorder(" Distributor "));
			btgDST=new ButtonGroup();
			btgDST.add(rdbDSTAL);btgDST.add(rdbDSTSP);
			setMatrix(15,6);
			setVGAP(13);
			add(pnlDSTSL,2,4,2.8,1,this,'L');
		//ADDING GRADE SELECTION
			pnlGRDSL=new JPanel(null);
			setDefaultGap();setMatrix(20,7);
			add(rdbGRDAL=new JRadioButton("All"),1,1,1,0.91,pnlGRDSL,'L');
			add(rdbGRDSP=new JRadioButton("Specific"),2,1,1,0.91,pnlGRDSL,'L');
			add(chbGRDTOT=new JCheckBox("Total"),3,1,1,0.91,pnlGRDSL,'L');
			add(txtGRDDS=new JTextField(),3,1,1,0.91,pnlGRDSL,'L');
			pnlGRDSL.setBorder(BorderFactory.createTitledBorder(" Grade "));
			btgGRD=new ButtonGroup();
			btgGRD.add(rdbGRDAL);btgGRD.add(rdbGRDSP);
			setMatrix(15,6);
			setVGAP(13);
			add(pnlGRDSL,2,5,2.8,1,this,'L');
			
			lstODRBY=new JList();lstODRBY.addMouseListener(this);
			L_pnlTEMP=new JPanel(null);
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder(" Sort Order "));
			setDefaultGap();setMatrix(20,7);
			add(new JScrollPane(lstODRBY,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),1,1,3.8,0.83,L_pnlTEMP,'R');
			setMatrix(15,6);
			setVGAP(13);
			add(L_pnlTEMP,2,6,3.7,1,this,'L');
			
		//PUTTING MARKET TYPES IN COMBO
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and"
			  + " CMT_CGSTP = 'COXXMKT' ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
					cmbMKTTP.addItem(M_rstRSSET.getString("CMT_CODDS")+" ("+M_rstRSSET.getString("CMT_CODCD")+")");
				M_rstRSSET.close();
			}
			vtrODRBY=new Vector<String>(3,1);
		}catch(Exception e)
		{setMSG(e,"child.constructor");}
	}
	/**<B>TASKS : .</B><br>
	 * &nbsp&nbsp&nbsp&nbsp cmbOPTN  : Enable @see chbRGNWS if user has access in more than one sone, toherwise, disable
	 * &nbsp&nbsp&nbsp&nbsp rdbDSTAL : Update list data, Hide Sp. DST name txtfield. If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsp rdbGRDAL : Update list data, Hide Sp. GRD discription txtfield. If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsp rdbZONAL : Update list data, If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsp rdbSTPAL : Update list data, If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsp rdbDSTSP : Update list data, set total check to false, show txtfield for sp. DST<br>
	 * &nbsp&nbsp&nbsp&nbsp rdbGRDSP : Update list data, set total check to false, show txtfield for sp. DST<br>
	 * &nbsp&nbsp&nbsp&nbsp rdbZONSP : Update list data, set total check to false, <br>
	 * &nbsp&nbsp&nbsp&nbsp rdbSTPSP : Update list data, set total check to false, <br>
	 */
	public void actionPerformed(ActionEvent LP_AE)
	{
		try
		{
			super.actionPerformed(LP_AE);
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				if(cl_dat.M_cmbSBSL1_pbst.getItemCount()>0)
					chbRGNWS.setVisible(true);
				rdbDAILY.requestFocus();
			}
			else if(M_objSOURC==M_txtFMDAT)
			{
				if(M_txtFMDAT.vldDATE()==null)
					M_txtTODAT.requestFocus();
			}
			else if(M_objSOURC==rdbDSTAL)
			{
				if(!vtrODRBY.contains(staCOLHD_fn[intDSRCD_fn-1]))
					vtrODRBY.addElement(staCOLHD_fn[intDSRCD_fn-1]);
				lstODRBY.setListData(vtrODRBY);
				txtDSTNM.setVisible(false);
				chbDSTTOT.setVisible(true);
				if(rdbSUMRY.isSelected())
					chbDSTTOT.setSelected(true);
			}
			else if(M_objSOURC==rdbGRDAL)
			{
				if(!vtrODRBY.contains(staCOLHD_fn[intPRDDS_fn-1]))
					vtrODRBY.addElement(staCOLHD_fn[intPRDDS_fn-1]);
				lstODRBY.setListData(vtrODRBY);
				txtGRDDS.setVisible(false);
				chbGRDTOT.setVisible(true);
				if(rdbSUMRY.isSelected())
					chbGRDTOT.setSelected(true);
			}
			else if(M_objSOURC==rdbZONAL)
			{
				if(!vtrODRBY.contains(staCOLHD_fn[intZONCD_fn-1]))
					vtrODRBY.addElement(staCOLHD_fn[intZONCD_fn-1]);
				lstODRBY.setListData(vtrODRBY);
				if(rdbSUMRY.isSelected())
				chbZONTOT.setSelected(true);
			}
			else if(M_objSOURC==rdbSTPAL)
			{
				if(!vtrODRBY.contains(staCOLHD_fn[intSALTP_fn-1]))
					vtrODRBY.addElement(staCOLHD_fn[intSALTP_fn-1]);
				lstODRBY.setListData(vtrODRBY);
				if(rdbSUMRY.isSelected())
				chbSTPTOT.setSelected(true);
			}
			else if(M_objSOURC==rdbDSTSP)
			{
				if(vtrODRBY.contains(staCOLHD_fn[intDSRCD_fn-1]))
					vtrODRBY.removeElement(staCOLHD_fn[intDSRCD_fn-1]);
				lstODRBY.setListData(vtrODRBY);
				chbDSTTOT.setSelected(false);
				txtDSTNM.setVisible(true);
				chbDSTTOT.setVisible(false);
				chbDSTTOT.setSelected(false);
			}
			else if(M_objSOURC==rdbGRDSP)
			{
				if(vtrODRBY.contains(staCOLHD_fn[intPRDDS_fn-1]))
					vtrODRBY.removeElement(staCOLHD_fn[intPRDDS_fn-1]);
				lstODRBY.setListData(vtrODRBY);
				chbGRDTOT.setSelected(false);
				txtGRDDS.setVisible(true);
				chbGRDTOT.setVisible(false);
				chbGRDTOT.setSelected(false);
			}
			else if(M_objSOURC==rdbZONSP&&vtrODRBY.contains(staCOLHD_fn[intZONCD_fn-1]))
			{
				chbZONTOT.setSelected(false);
				vtrODRBY.removeElement(staCOLHD_fn[intZONCD_fn-1]);
				lstODRBY.setListData(vtrODRBY);
			}
			else if(M_objSOURC==rdbSTPSP&&vtrODRBY.contains(staCOLHD_fn[intSALTP_fn-1]))
			{
				chbSTPTOT.setSelected(false);
				vtrODRBY.removeElement(staCOLHD_fn[intSALTP_fn-1]);
				lstODRBY.setListData(vtrODRBY);
			}
			else if(M_objSOURC==rdbDAILY)
			{
				chbRGNWS.setSelected(true);
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
//				M_calLOCAL.add(Calendar.DATE,-1);//22/05/04 API HBP : DEFAULT DATE DISPLAYED AS OF TODAY
				M_txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				M_txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				rdbZONAL.setSelected(true);rdbDSTAL.setSelected(true);rdbGRDAL.setSelected(true);
				rdbSTPAL.setSelected(true);rdbRPTAL.setSelected(true);
				chbZONTOT.setSelected(true);chbDSTTOT.setSelected(true);chbGRDTOT.setSelected(false);
				chbSTPTOT.setSelected(true);
				vtrODRBY.removeAllElements();
				vtrODRBY.addElement("Zone");vtrODRBY.addElement("Sale Type");
				vtrODRBY.addElement("Distributor");vtrODRBY.addElement("Grade");
				lstODRBY.setListData(vtrODRBY);
				lstODRBY.setEnabled(false);
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if(M_objSOURC==rdbSUMRY)
				lstODRBY.setEnabled(true);
			else if(M_objSOURC==rdbDETAIL)
				lstODRBY.setEnabled(true);
			
		}catch(Exception LP_EX)
		{
			setMSG(LP_EX,"Child.actionPerformed");
		}
	}
	/**<b>TASK </B>
	 * &nbsp&nbsp&nbsp&nbsp M_txtFMDAT or M_txtTODAT : set report data vector to null
	 */
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
		if(M_objSOURC==M_txtFMDAT||M_objSOURC==M_txtTODAT)
			vtrQRDAT=null;
	}
	/**<b>TASK </B>
	 * &nbsp&nbsp&nbsp&nbsp txtDSTNM : HELP All distributor names from CO_CDTRN
	 * &nbsp&nbsp&nbsp&nbsp txtGRDDS : HELP Grade descrition for which order is booked during report period. From MR_INTRN
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(M_objSOURC==txtDSTNM&&L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strSQLQRY="SELECT PT_SHRNM,PT_PRTCD FROM CO_PTMST WHERE PT_PRTTP='D' ORDER BY PT_PRTNM";
				M_strHLPFLD = "txtDSTNM";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Distributor Name","Distributor Code"},2,"CT");
			}
			else if(M_objSOURC==txtGRDDS&&L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strSQLQRY="SELECT DISTINCT INT_PRDDS FROM VW_INTRN WHERE INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_BKGDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"'  AND INT_SBSCD1 in "+M_strSBSLS+" ORDER BY INT_PRDDS";
				M_strHLPFLD = "txtGRDDS";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {staCOLHD_fn[intPRDDS_fn-1]},1,"CT");
			}

		}catch(Exception e)
		{			setMSG(e,"child.keypressed");		}
	}
	
	void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtDSTNM"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				txtDSTNM.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtGRDDS"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				txtGRDDS.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}catch(Exception L_EX)
		{			setMSG(L_EX,"Chlid.exeHLPOK");		}
	}
/*	public void run()
	{
		try{	
/*		M_strSQLQRY="select IN_MKTTP ,IN_INDNO,a.CMT_CODDS,IN_BYRCD,c.PT_PRTNM CMT_BYRNM,IN_DSRCD,d.PT_SHRNM,INT_PRDDS,INT_INDQT,b.cmt_codds,INT_BASRT,INT_CDCVL/1000 INT_CDCVL,INT_DDCVL/1000 INT_DDCVL,INT_TDCVL/1000 INT_TDCVL, IN_APTVL ,e.PT_PRTNM PT_CNSNM, IN_CPTVL,IN_DTPCD from MR_INMST,MR_INTRN,CO_CDTRN a,CO_CDTRN b,cO_PTMST c,CO_PTMST d, CO_PTMST e where IN_MKTTP=INT_MKTTP and IN_INDNO=INT_INDNO and IN_INDDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_INDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"'"
					+" and IN_SALTP=a.CMT_CODCD and a.CMT_CGSTP='MR00SAL'"
					+" and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON'"
					+" and c.PT_PRTTP='C' and IN_BYRCD=c.PT_PRTCD"
					+" and d.PT_PRTTP='D' and IN_DSRCD=d.PT_PRTCD"
					+" and e.PT_PRTTP='C' and IN_CNSCD=e.PT_PRTCD"
					+" and in_stsfl<>'X' and int_stsfl<>'X'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		}catch(Exception e)
		{setMSG(e,"retrieving details");}
	}
*/
	/**Method to fire a query, if required<br>filter the data<br>Display it in tabular format<br>Print if 'Print' option is selected	 */
	void exePRINT()
	{
		if(vldDATA())
		try
		{
			M_intPAGNO=0;
			setMSG("Fetching Data ..",'N');
			String[] L_staTEMP=null;
			//String L_strSBSCD=""; //STRING FOR SUBSYSTEM FILTER
			//for(int i=0;i<M_staUSRRT.length;i++)
			//	L_strSBSCD+="'"+sub_str(M_staUSRRT[i][0],2)+"',";
			//L_strSBSCD=sub_str(L_strSBSCD,0,L_strSBSCD.length()-1);
			if(vtrQRDAT==null)
			{//FIRING QUERY TO DB DURING FIRST FORMATTING
				M_strSQLQRY="select IN_MKTTP ,IN_INDNO,a.CMT_CODDS,IN_BYRCD,c.PT_PRTNM CMT_BYRNM,IN_DSRCD,d.PT_SHRNM,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,b.cmt_codds,INT_BASRT,INT_CDCVL/1000 INT_CDCVL,INT_DDCVL/1000 INT_DDCVL,INT_TDCVL/1000 INT_TDCVL, IN_APTVL ,e.PT_PRTNM PT_CNSNM,IN_CPTVL,IN_DTPCD,INT_PRDCD from VW_INTRN,CO_CDTRN a,CO_CDTRN b,cO_PTMST c,CO_PTMST d, CO_PTMST e where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and IN_BKGDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_BKGDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"'"
					+" and IN_SALTP=a.CMT_CODCD and a.CMT_CGSTP='MR00SAL'"
					+" and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON'"
					+" and c.PT_PRTTP='C' and IN_BYRCD=c.PT_PRTCD"
					+" and d.PT_PRTTP=IN_DSRTP and IN_DSRCD=d.PT_PRTCD"
					+" and e.PT_PRTTP='C' and IN_CNSCD=e.PT_PRTCD"
					+" and in_stsfl<>'X' and int_stsfl<>'X' and INT_SBSCD1 in "+M_strSBSLS+" and IN_MKTTP in "+strMKTTP_LST;
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				vtrQRDAT=new Vector<String[]>(100,10);
				ResultSetMetaData L_mtdRSSET=M_rstRSSET.getMetaData();
				int L_intCOLCT=L_mtdRSSET.getColumnCount();
				L_staTEMP=new String[L_intCOLCT];
				String L_strAPTVL=null,L_strCPTVL=null;
				while(M_rstRSSET.next())
				{
					//L_staTEMP=new String[L_intCOLCT-2];
					L_staTEMP=new String[L_intCOLCT];
					L_staTEMP[0]=nvlSTRVL(M_rstRSSET.getString("IN_MKTTP")," ");
					L_staTEMP[1]=nvlSTRVL(M_rstRSSET.getString("IN_INDNO")," ");
					L_staTEMP[2]=nvlSTRVL(M_rstRSSET.getString(3)," ");
					//L_staTEMP[3]=nvlSTRVL(M_rstRSSET.getString(5)+"("+M_rstRSSET.getString(4)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
					L_staTEMP[3]=nvlSTRVL(M_rstRSSET.getString(5)," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
					L_staTEMP[4]=nvlSTRVL(M_rstRSSET.getString(7)+"~("+M_rstRSSET.getString(6)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
					L_staTEMP[5]=nvlSTRVL(M_rstRSSET.getString(8)," ");
					L_staTEMP[6]=nvlSTRVL(M_rstRSSET.getString(9)," ");
			//Digits are attached b4 zone to set order in desired manner. (i.e. region wise)
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("west"))
						L_staTEMP[7]="1"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("central"))
						L_staTEMP[7]="2"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("north"))
						L_staTEMP[7]="3"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("south"))
						L_staTEMP[7]="4"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("east"))
						L_staTEMP[7]="5"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("overall"))
						L_staTEMP[7]="6"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					L_staTEMP[intBASRT_fn]=nvlSTRVL(M_rstRSSET.getString("INT_BASRT")," ");
					L_staTEMP[intCDCVL_fn]=nvlSTRVL(M_rstRSSET.getString("INT_CDCVL")," ");
					L_staTEMP[intDDCVL_fn]=nvlSTRVL(M_rstRSSET.getString("INT_DDCVL")," ");
					L_staTEMP[intTDCVL_fn]=nvlSTRVL(M_rstRSSET.getString("INT_TDCVL")," ");
					L_strAPTVL=nvlSTRVL(M_rstRSSET.getString("IN_APTVL")," ");
					if(L_strAPTVL.length()==1)
						L_strAPTVL="0"+L_strAPTVL;
					L_strCPTVL=nvlSTRVL(M_rstRSSET.getString("IN_CPTVL")," ");
					if(L_strCPTVL.length()==1)
						L_strCPTVL="0"+L_strCPTVL;
					L_staTEMP[intAPTVL_fn]=L_strCPTVL+"  "+L_strAPTVL+" ";
			//		L_staTEMP[12]=nvlSTRVL(M_rstRSSET.getString("IN_CPTVL")," ")+"/"+nvlSTRVL(M_rstRSSET.getString("IN_APTVL")," ")+" ";
					L_staTEMP[intCNSNM_fn]=nvlSTRVL(M_rstRSSET.getString("PT_CNSNM")," ");
					L_staTEMP[intDTPCD_fn]=nvlSTRVL(M_rstRSSET.getString("IN_DTPCD")," ");
					L_staTEMP[intPRDCD_fn]=nvlSTRVL(M_rstRSSET.getString("INT_PRDCD")," ");
					
					vtrQRDAT.addElement(L_staTEMP);//Vector to hold total, unsorted data
				}
				M_rstRSSET.close();
			}
			Vector<String> L_vtrORDR1=new Vector<String>(10,5);//Vector to hold filtered data to be sorted
			L_intORDR1=-1;L_intORDR2=-1;L_intORDR3=-1;L_intORDR4=-1;//Variables for column no. in sort order list
			if(vtrODRBY.size()>0)//if sort order contains only one element
			{
				if(vtrODRBY.elementAt(0).equals(staCOLHD_fn[intZONCD_fn-1]))
					L_intORDR1=intZONCD_fn;
				else if(vtrODRBY.elementAt(0).equals(staCOLHD_fn[intSALTP_fn-1]))
					L_intORDR1=intSALTP_fn;
				else if(vtrODRBY.elementAt(0).equals(staCOLHD_fn[intDSRCD_fn-1]))
					L_intORDR1=intDSRCD_fn;
				else if(vtrODRBY.elementAt(0).equals(staCOLHD_fn[intPRDDS_fn-1]))
					L_intORDR1=intPRDDS_fn;
			}
			if(vtrODRBY.size()>1)//if sort order contains two elements
			{
				if(vtrODRBY.elementAt(1).equals(staCOLHD_fn[intZONCD_fn-1]))
					L_intORDR2=intZONCD_fn;
				else if(vtrODRBY.elementAt(1).equals(staCOLHD_fn[intSALTP_fn-1]))
					L_intORDR2=intSALTP_fn;
				else if(vtrODRBY.elementAt(1).equals(staCOLHD_fn[intDSRCD_fn-1]))
					L_intORDR2=intDSRCD_fn;
				else if(vtrODRBY.elementAt(1).equals(staCOLHD_fn[intPRDDS_fn-1]))
					L_intORDR2=intPRDDS_fn;
			}
			if(vtrODRBY.size()>2)//if sort order contains three elements
			{
				if(vtrODRBY.elementAt(2).equals(staCOLHD_fn[intZONCD_fn-1]))
					L_intORDR3=intZONCD_fn;
				else if(vtrODRBY.elementAt(2).equals(staCOLHD_fn[intSALTP_fn-1]))
					L_intORDR3=intSALTP_fn;
				else if(vtrODRBY.elementAt(2).equals(staCOLHD_fn[intDSRCD_fn-1]))
					L_intORDR3=intDSRCD_fn;
				else if(vtrODRBY.elementAt(2).equals(staCOLHD_fn[intPRDDS_fn-1]))
					L_intORDR3=intPRDDS_fn;
			}
			if(vtrODRBY.size()>3)//if sort order contains four elements
			{
				if(vtrODRBY.elementAt(3).equals(staCOLHD_fn[intZONCD_fn-1]))
					L_intORDR4=intZONCD_fn;
				else if(vtrODRBY.elementAt(3).equals(staCOLHD_fn[intSALTP_fn-1]))
					L_intORDR4=intSALTP_fn;
				else if(vtrODRBY.elementAt(3).equals(staCOLHD_fn[intDSRCD_fn-1]))
					L_intORDR4=intDSRCD_fn;
				else if(vtrODRBY.elementAt(3).equals(staCOLHD_fn[intPRDDS_fn-1]))
					L_intORDR4=intPRDDS_fn;
			}
			if(L_intORDR4==-1)//Assigning default values if sort order not selected.
			{
				if(!vtrODRBY.contains(staCOLHD_fn[intPRDDS_fn-1]))
					L_intORDR4=intPRDDS_fn;
				else if(!vtrODRBY.contains(staCOLHD_fn[intDSRCD_fn-1]))
					L_intORDR4=intDSRCD_fn;
				else if(!vtrODRBY.contains(staCOLHD_fn[intSALTP_fn-1]))
					L_intORDR4=intSALTP_fn;
				else
					L_intORDR4=intZONCD_fn;
				if(L_intORDR3==-1)
				{
					if(!vtrODRBY.contains(staCOLHD_fn[intDSRCD_fn-1])&&L_intORDR4!=intDSRCD_fn)
						L_intORDR3=intDSRCD_fn;
					else if(!vtrODRBY.contains(staCOLHD_fn[intSALTP_fn-1])&&L_intORDR4!=intSALTP_fn)
						L_intORDR3=intSALTP_fn;
					else
						L_intORDR3=intZONCD_fn;
					if(L_intORDR2==-1)
					{
						if(!vtrODRBY.contains(staCOLHD_fn[intSALTP_fn-1])&&L_intORDR4!=intSALTP_fn&&L_intORDR3!=intSALTP_fn)
							L_intORDR2=intSALTP_fn;
						else
							L_intORDR2=intZONCD_fn;
						if(L_intORDR1==-1&&L_intORDR4!=intZONCD_fn&&L_intORDR3!=intZONCD_fn&&L_intORDR2!=intZONCD_fn)
						{
							L_intORDR1=intZONCD_fn;
						}
					}				
				}	
			}
			//TO FILTER DATA AS PER USER DEFINED CRITERIA
			for(int i=0;i<vtrQRDAT.size();i++)
			{
				L_staTEMP=(String[])vtrQRDAT.elementAt(i);
				if(chkSPCOL(L_staTEMP))//Validates filteration rules applied by user
				{
					if(L_intORDR1!=-1)
					{
						if(rdbDAILY.isSelected())
							L_vtrORDR1.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]+"|"+L_staTEMP[intCNSNM_fn]+"|"+L_staTEMP[intDTPCD_fn]+"|"+L_staTEMP[intPRDCD_fn]);
						else
							L_vtrORDR1.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]+"|"+L_staTEMP[intCNSNM_fn]+"|"+L_staTEMP[intDTPCD_fn]+"|"+L_staTEMP[intPRDCD_fn]);
					}
				}
			}
			//SORTING DATA IN USER DEFINED ORDER
			obaRPDAT=L_vtrORDR1.toArray();
			Arrays.sort(obaRPDAT);
			//CREATING TABLE HEADER AS PER USER DEFINED SORT ORDER
			if(rdbDAILY.isSelected())
				staCOLHD = new String[]{"FL","Zone","Sale Type","Distributor","Buyer","Grade","Ind. No","Indent Qty","Base Rate","C.Dsc.","D.Dsc.","T.Dsc.","Term","Consignee","Del. Tp."};
			else
				staCOLHD = new String[]{"FL",staCOLHD_fn[L_intORDR1-1],staCOLHD_fn[L_intORDR2-1],staCOLHD_fn[L_intORDR3-1],staCOLHD_fn[L_intORDR4-1],"Buyer","Ind. No","Indent Qty","Base Rate","C.Dsc.","D.Dsc.","T.Dsc.","Term","Consignee","Del. Tp."};
			int[] L_COLSZ = {20,100,100,100,100,100,100,100,100,50,50,50,50,75,50};
			pnlREPORT.removeAll();
			//100 ROWs ADDITIONAL FOR VAUES OF SUBTOTALS TO BE DISPLAYED
			tblREPORT = crtTBLPNL(pnlREPORT,staCOLHD,obaRPDAT.length+300,1,1,7,5.8,L_COLSZ,new int[]{0});
			tblREPORT.setEnabled(false);
			StringTokenizer L_stkTEMP=null;
			//STRINGS TO HOLD VALUES OF COLUMNS IN PRESENT RECORD.
			String L_strCOL1="",L_strCOL2="",L_strCOL3="",L_strCOL4="",L_strCOL5="",L_strCOL6="",L_strCOL7="",L_strCOL8="",L_strCOL9="",L_strCOL10="",L_strCOL11="",L_strCOL12="";
			//FOR SUB-CATARORYWISE TOTALS
			double L_dblTOT1=0.0,L_dblTOT2=0.0,L_dblTOT3=0.0,L_dblTOT4=0.0,L_dblTOTRGN=0.0;
			int j=-1;
			//OLD VALUES OF COLUMNS.
			String L_strCOL1VL="",L_strCOL2VL="",L_strCOL3VL="",L_strCOL4VL="";
			//FLAGS TO INDICATE WHETHER TOTAL FOR RESP. COL. IS TO BE DISPLAYED
			boolean L_flgTOTAL=false,L_flgTOTCL1=false,L_flgTOTCL2=false,L_flgTOTCL3=false,L_flgTOTCL4=false;
			//INITIALIZING ABOVE FLAGS
			if(staCOLHD[1].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||staCOLHD[1].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||staCOLHD[1].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||staCOLHD[1].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL1=true;
			if(staCOLHD[2].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||staCOLHD[2].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||staCOLHD[2].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||staCOLHD[2].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL2=true;
			if(staCOLHD[3].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||staCOLHD[3].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||staCOLHD[3].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||staCOLHD[3].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL3=true;
			if(staCOLHD[4].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||staCOLHD[4].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||staCOLHD[4].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||staCOLHD[4].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL4=true;
			int L_intFLPRN=0;//FLAG TO INDICATE TOTALS OF HOW MANY COLUMNS ARE TO BE DISPLAYED
			double L_dblGRTOT=0.0;//GRAND TOTAL
			int i=0;//LOOP COUNTER
			String L_strHDGST="";
			if(!rdbSUMRY.isSelected())
				L_strHDGST="Total ";
			String[] L_staCOLVL=new String[staCOLHD_fn.length-1];
			String L_strZONVL="";
			for(i=0;i<tblREPORT.getRowCount();i++)
			{
				L_intFLPRN=0;
				if(j==obaRPDAT.length-1)//IF END OF REPORT IS ACHEIVED PRINT Summary
				{
					if(L_flgTOTCL4)
					{//Diplay total of fourth col
						if(!rdbSUMRY.isSelected())
							tblREPORT.setRowColor(i,Color.blue);
						else
							tblREPORT.setRowColor(i,Color.darkGray);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,7);
						tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
						if(staCOLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
						{
							String L_strTEMP="";
							if(L_strZONVL.equalsIgnoreCase("West")||L_strZONVL.equalsIgnoreCase("Central"))
								L_strTEMP="West & Central";
							else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
								L_strTEMP="North, South & East";
							else
								L_strTEMP="Export";
							tblREPORT.setRowColor(i,Color.red);
							tblREPORT.setValueAt(L_strHDGST+L_strTEMP,i,4);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
						}
					}
					if(L_flgTOTCL3)
					{//Diplay total of third col
						tblREPORT.setRowColor(i,Color.blue);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,7);
						tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
						if(staCOLHD[3].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
						{
							String L_strTEMP="";
							if(L_strZONVL.equalsIgnoreCase("West")||L_strZONVL.equalsIgnoreCase("Central"))
								L_strTEMP="West & Central";
							else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
								L_strTEMP="North, South & East";
							else
								L_strTEMP="Export";
							tblREPORT.setRowColor(i,Color.red);
							tblREPORT.setValueAt(L_strHDGST+L_strTEMP,i,3);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
						}
					}
					if(L_flgTOTCL2)
					{//Diplay total of second col
						if(!rdbSUMRY.isSelected())
							tblREPORT.setRowColor(i,Color.blue);
						else
							tblREPORT.setRowColor(i,Color.magenta);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT2,3),i,7);
						tblREPORT.setValueAt(L_strHDGST+L_strCOL2VL,i++,2);
						if(staCOLHD[2].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
						{
							String L_strTEMP="";
							if(L_strZONVL.equalsIgnoreCase("West")||L_strZONVL.equalsIgnoreCase("Central"))
								L_strTEMP="West & Central";
							else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
								L_strTEMP="North, South & East";
							else
								L_strTEMP="Export";
							tblREPORT.setRowColor(i,Color.red);
							tblREPORT.setValueAt(L_strHDGST+L_strTEMP,i,2);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
						}
					}
					if(L_flgTOTCL1)
					{//Diplay total of first col
						if(!rdbSUMRY.isSelected())
							tblREPORT.setRowColor(i,Color.blue);
						else
							tblREPORT.setRowColor(i,Color.red);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT1,3),i,7);
						tblREPORT.setValueAt(L_strHDGST+L_strCOL1VL,i++,1);
						if(staCOLHD[1].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
						{
							String L_strTEMP="";
							if(L_strZONVL.equalsIgnoreCase("West")||L_strZONVL.equalsIgnoreCase("Central"))
								L_strTEMP="West & Central";
							else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
								L_strTEMP="North, South & East";
							else
								L_strTEMP="Export";
							tblREPORT.setRowColor(i,Color.red);
							tblREPORT.setValueAt(L_strHDGST+L_strTEMP,i,1);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
						}
					}
					break;
				}
				L_stkTEMP=new StringTokenizer(obaRPDAT[++j].toString(),"|");
				for(int z=0;z<L_staCOLVL.length;z++)
				{//putting values for this row in a string array
					L_staCOLVL[z]=L_stkTEMP.nextToken().replace('~',' ');
					if(staCOLHD[z+1].equals(staCOLHD_fn[7]))
					{//removing digit attached b4 zone
						L_staCOLVL[z]=sub_str(L_staCOLVL[z],1);
					}
				}
				if(i==0)
				{
					L_strCOL1VL=L_staCOLVL[0];L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];
					int z=0;
					for(z=0;z<staCOLHD.length;z++)
						if(staCOLHD[z].equalsIgnoreCase(staCOLHD_fn[7]))
						   break;
				}
				if(i>0)
				{
					if(!L_strCOL1VL.equalsIgnoreCase(L_staCOLVL[0]))
					{//if value for first col is NOT as same as previous row
						if(L_flgTOTCL4)
						{//display total for fourth col
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.darkGray);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							if(staCOLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
							}
						}
						if(L_flgTOTCL3)
						{//display total of second col
							tblREPORT.setRowColor(i,Color.blue);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
							if(staCOLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,3);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,3);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,3);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
							}
						}
						if(L_flgTOTCL2)
						{//displa total of second col
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.magenta);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT2,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL2VL,i++,2);
							if(staCOLHD[2].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,2);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,2);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,2);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
								L_dblTOTRGN=0.0;
							}
						}
						if(L_flgTOTCL1)
						{//display total of fist col
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.red);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT1,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL1VL,i++,1);
							if(staCOLHD[1].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[0].equalsIgnoreCase("West")&&!L_staCOLVL[0].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,1);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[0].equalsIgnoreCase("North")&&!L_staCOLVL[0].equalsIgnoreCase("South")&&!L_staCOLVL[0].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,1);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,1);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
							}
						}
						L_dblTOT1=0.0;L_dblTOT2=0.0;L_dblTOT3=0.0;L_dblTOT4=0.0;//intialise total variables
						L_strCOL1VL=L_staCOLVL[0];L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];//copy current values as previous values for next row
						int z=0;
						for(z=0;z<L_staCOLVL.length;z++)//retrieve colindex for zone
							if(staCOLHD[z].equalsIgnoreCase(staCOLHD_fn[7]))
							   break;
						L_strZONVL=L_staCOLVL[z-1];
						L_intFLPRN=4;
					}
			//TOTALISING SECOND COL
					else if(!L_strCOL2VL.equalsIgnoreCase(L_staCOLVL[1]))
					{
						if(L_flgTOTCL4)
						{
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.darkGray);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							if(staCOLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
								L_dblTOTRGN=0.0;
							}
						}
						if(L_flgTOTCL3)
						{
							tblREPORT.setRowColor(i,Color.blue);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
							if(staCOLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,3);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,3);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,3);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
								L_dblTOTRGN=0.0;
							}
						}
						if(L_flgTOTCL2)
						{
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.magenta);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT2,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL2VL,i++,2);
							if(staCOLHD[2].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[1].equalsIgnoreCase("West")&&!L_staCOLVL[1].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,2);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(L_staCOLVL[1].equalsIgnoreCase("North")&&!L_staCOLVL[1].equalsIgnoreCase("South")&&!L_staCOLVL[1].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,2);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,2);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
							}
						}
						L_dblTOT2=0.0;L_dblTOT3=0.0;L_dblTOT4=0.0;
						L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];
						L_intFLPRN=3;	
					}
//END OF SECOND COL TOTAL
//TOTALISING THIRD COL
					else if(!L_strCOL3VL.equalsIgnoreCase(L_staCOLVL[2]))
					{
						if(L_flgTOTCL4)
						{
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.darkGray);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							if(staCOLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
							}
						}
						if(L_flgTOTCL3)
						{
							tblREPORT.setRowColor(i,Color.blue);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
							if(staCOLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[2].equalsIgnoreCase("West")&&!L_staCOLVL[2].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,3);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[2].equalsIgnoreCase("North")&&!L_staCOLVL[2].equalsIgnoreCase("South")&&!L_staCOLVL[2].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,3);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,3);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
							}
						}
						L_dblTOT3=0.0;L_dblTOT4=0.0;
						L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];
						L_intFLPRN=2;
					}
//END OF THIRD COL TOTAL
//TOTALISING FOURTH COL
					else if(!L_strCOL4VL.equalsIgnoreCase(L_staCOLVL[3]))
					{
						if(L_flgTOTCL4)
						{
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.darkGray);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,7);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							if(staCOLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[3].equalsIgnoreCase("West")&&!L_staCOLVL[3].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[3].equalsIgnoreCase("North")&&!L_staCOLVL[3].equalsIgnoreCase("South")&&!L_staCOLVL[3].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,7);
									L_dblTOTRGN=0.0;
								}
							}
						}
						L_dblTOT4=0.0;
						L_strCOL4VL=L_staCOLVL[3];
						L_intFLPRN=1;
					}
//END OF FOURTH COL TOTAL
				}
				L_dblTOTRGN+=Double.parseDouble(L_staCOLVL[6]);
				L_dblTOT1+=Double.parseDouble(L_staCOLVL[6]);
				L_dblTOT2+=Double.parseDouble(L_staCOLVL[6]);
				L_dblTOT3+=Double.parseDouble(L_staCOLVL[6]);
				L_dblTOT4+=Double.parseDouble(L_staCOLVL[6]);
				L_dblGRTOT+=Double.parseDouble(L_staCOLVL[6]);
				int z=0;
				for(z=0;z<L_staCOLVL.length;z++)
					if(staCOLHD[z].equalsIgnoreCase(staCOLHD_fn[7]))
					   break;
				L_strZONVL=L_staCOLVL[z-1];
				if(!rdbSUMRY.isSelected())//SHOW DETIALS IF DETILS ARE SELECTED
				{
					tblREPORT.setRowColor(i,Color.black);
					if((L_intFLPRN>3||i==0))
					{
						tblREPORT.setValueAt(L_staCOLVL[0],i,1);
					}
					if((L_intFLPRN>2||i==0))
						tblREPORT.setValueAt(L_staCOLVL[1],i,2);
					if(L_intFLPRN>1||i==0)
					{
					///	System.out.println("from 2 "+L_staCOLVL[2]);
						tblREPORT.setValueAt(L_staCOLVL[2],i,3);
					}
					if(L_intFLPRN>0||i==0)
						tblREPORT.setValueAt(L_staCOLVL[3],i,4);
					///	System.out.println("from a");
                        tblREPORT.setValueAt(L_staCOLVL[4],i,5);
	               		tblREPORT.setValueAt(L_staCOLVL[5],i,6);
						tblREPORT.setValueAt(L_staCOLVL[6],i,7);
						tblREPORT.setValueAt(L_staCOLVL[7],i,8);
						if(Double.parseDouble(L_staCOLVL[8])>0.0)
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(L_staCOLVL[8]),2),i,9);
						if(Double.parseDouble(L_staCOLVL[9])>0.0)
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(L_staCOLVL[9]),2),i,10);
						if(Double.parseDouble(L_staCOLVL[10])>0.0)
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(L_staCOLVL[10]),2),i,11);
			//			if(Double.parseDouble(L_staCOLVL[11])>0.0)
						tblREPORT.setValueAt(L_staCOLVL[11],i,12);
//							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(L_staCOLVL[11]),0),i,12);
						int a=0;
						for(a=0;a<staCOLHD.length;a++)
							if(staCOLHD[a].equalsIgnoreCase("Buyer"))
								break;
						if(!L_staCOLVL[a-1].equalsIgnoreCase(L_staCOLVL[12]))
						{
							tblREPORT.setValueAt(L_staCOLVL[12],i,13);
							tblREPORT.setCellColor(i,a,Color.blue);
							tblREPORT.setValueAt(L_staCOLVL[a-1],i,a);
						}
						if(L_staCOLVL[13].equalsIgnoreCase("01"))
							tblREPORT.setValueAt("Ex.F",i,14);
						else
							tblREPORT.setValueAt("FOR.",i,14);
				}
				else if(i>0)
					i--;
			}
			tblREPORT.setRowColor(i,Color.red);
			tblREPORT.setValueAt("Grand Total",i,1);
			tblREPORT.setValueAt(setNumberFormat(L_dblGRTOT,3),i,7);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equalsIgnoreCase("Screen"))
			add(pnlREPORT,6,1,8,6,this,'L');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equalsIgnoreCase("Print"))
				exePRNRPT();
			updateUI();
		}catch(ArrayIndexOutOfBoundsException e)
		{
			setMSG(e,"exePRINT");
			setMSG("Report length is too large to fit in table ..",'E');
		}catch(Exception e)
		 {
			 setMSG(e,"exePRINT");
		}
	}
	private void PRNDLYHD() throws Exception
	{
		if(M_intPAGNO>0)
		{
			L_doutREPORT.writeBytes("\n\n---------------------------------------------------------------------------------------------------------------------------------------------\n REPORT CRITERIA : \n");
			L_doutREPORT.writeBytes(getRPFTR());
			prnFMTCHR(L_doutREPORT,M_strEJT);
		}
		int [] L_inaCOLWD=new int[inaCOLWD_fn.length];
		prnFMTCHR(L_doutREPORT,M_strBOLD);
		prnFMTCHR(L_doutREPORT,M_strNOCPI17);
		prnFMTCHR(L_doutREPORT,M_strCPI10);
		M_intPAGNO++;
		L_doutREPORT.writeBytes("SUPREME PETROCHEM     LIMITED                                            Page : "+M_intPAGNO+"\n");
		prnFMTCHR(L_doutREPORT,M_strNOBOLD);
		L_doutREPORT.writeBytes("POLYSTYRENE ORDERS BOOKED ON "+M_txtFMDAT.getText()+"                          Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"\n");
		prnFMTCHR(L_doutREPORT,M_strBOLD);
		prnFMTCHR(L_doutREPORT,M_strCPI17);
		L_doutREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------\n   ");
		for(int i=2;i<L_inaCOLWD.length;i++)
		{
			for(int j=0;j<inaCOLWD_fn.length;j++)
			{
				if(staCOLHD[i].equals(staCOLHD_fn[j]))
				{
					L_inaCOLWD[i]=inaCOLWD_fn[j];
					L_doutREPORT.writeBytes(padSTRING('R',staCOLHD[i],L_inaCOLWD[i]));
					break;
				}
			}
		}
		prnFMTCHR(L_doutREPORT,M_strNOBOLD);
		L_doutREPORT.writeBytes("\n");
		L_doutREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------\n");
		M_intLINNO=5;
	}
	/**To print report to file
	 */
	private void exePRNRPT()
	{
		try
		{
			int [] L_inaCOLWD=new int[inaCOLWD_fn.length];
			for(int i=0;i<L_inaCOLWD.length;i++)
			{
				for(int j=0;j<inaCOLWD_fn.length;j++)
				{
					if(staCOLHD[i].equals(staCOLHD_fn[j]))
					{
						L_inaCOLWD[i]=inaCOLWD_fn[j];
						break;
					}
				}
			}
			String L_strTEMP="",L_strREPORT="";
			boolean L_flgPRNLN=false;
			File filREPORT=null;
			if(M_rdbTEXT.isSelected())
				filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_qrcob.doc");
			else
			{
				filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_qrcob.html");
			}
			FileOutputStream filOUT=new FileOutputStream(filREPORT);
			L_doutREPORT=new DataOutputStream(filOUT);
			if(M_rdbHTML.isSelected())
				L_doutREPORT.writeBytes("<pre>");
			boolean L_flgTOTAL=false;
			String L_strCNSNM="";
			PRNDLYHD();
			String L_strSEPRTR=null;
			for(int i=0;i<tblREPORT.getRowCount();i++)
			{
				if(!L_strCNSNM.equals(""))
				{
					if(L_strCNSNM.length()>15)
						L_strCNSNM=sub_str(L_strCNSNM,0,14);
					L_doutREPORT.writeBytes(padSTRING('L',"CON:"+L_strCNSNM,57)+"\n");
					L_strCNSNM="";
					M_intLINNO++;
				}
				for(int j=1;j<tblREPORT.getColumnCount();j++)
				{
					if(tblREPORT.getValueAt(i,j).toString().length()>0)
					{
						L_flgPRNLN=true;
						break;
					}
				}
				if(tblREPORT.getValueAt(i,13).toString().length()>0)
					L_strCNSNM=tblREPORT.getValueAt(i,13).toString();
				else
					L_strCNSNM="";
				if(tblREPORT.getValueAt(i,1).toString().length()>0)
				{//DISPLAY COL - 1 VALUE IN SEPARATE LINE
					if(tblREPORT.getValueAt(i,1).toString().length()>5)
					{//CHECK IF IT IS NOT TOTALISING ROW
						if(!(sub_str(tblREPORT.getValueAt(i,1).toString(),0,5).equalsIgnoreCase("TOTAL")||sub_str(tblREPORT.getValueAt(i,1).toString(),0,5).equalsIgnoreCase("GRAND")))//||tblREPORT.getValueAt(i,1).toString().length()<5)
						{
							L_doutREPORT.writeBytes(staCOLHD[1].toUpperCase()+":"+tblREPORT.getValueAt(i,1).toString().toUpperCase()+"\n");
							M_intLINNO++;
						}
					}
					else
					{
						L_doutREPORT.writeBytes(staCOLHD[1].toUpperCase()+":"+tblREPORT.getValueAt(i,1).toString().toUpperCase()+"\n");
						M_intLINNO++;
					}
				}
				L_strSEPRTR=null;
				for(int j=1;j<tblREPORT.getColumnCount()&&L_flgPRNLN;j++)
				{
					if(j==1)
					{
						if( tblREPORT.getValueAt(i,1).toString().length()>5)
						{//DISPLAY COL - 1 ONLY IF TOTAL IS TO BE PRINTED
							if(!(sub_str(tblREPORT.getValueAt(i,1).toString(),0,5).equalsIgnoreCase("TOTAL")||sub_str(tblREPORT.getValueAt(i,1).toString(),0,5).equalsIgnoreCase("GRAND")))
								continue;
						}
						else
							continue;
					}
					if(tblREPORT.getValueAt(i,j).toString().length()>5)
					{
						if(sub_str(tblREPORT.getValueAt(i,j).toString(),0,5).equalsIgnoreCase("total")
						   ||sub_str(tblREPORT.getValueAt(i,j).toString(),0,5).equalsIgnoreCase("grand"))
						{
							L_strTEMP=tblREPORT.getValueAt(i,j).toString();
							if(j>2)
							{
								L_strTEMP="TOTAL : ";
/*								if(staCOLHD[j].equalsIgnoreCase(staCOLHD_fn[intDSRCD_fn-1]))
								{
									StringTokenizer L_stkTEMP=new StringTokenizer(tblREPORT.getValueAt(i,j).toString().toUpperCase(),"(");
									L_strTEMP=L_stkTEMP.nextToken()+" : ";
								}
								else
									L_strTEMP=tblREPORT.getValueAt(i,j).toString().toUpperCase()+" : ";
*/								int L_intPADLN=0;
								for(int z=j;z<=6;z++)
										L_intPADLN+=L_inaCOLWD[z];
								L_strTEMP=padSTRING('L',L_strTEMP,L_intPADLN+3);	
								if(j<4)
								{
									L_strSEPRTR="\n";
									M_intLINNO++;
								}
							}
							else
							{
								L_strTEMP=padSTRING('R',L_strTEMP,L_inaCOLWD[2]+L_inaCOLWD[3]+L_inaCOLWD[4]+L_inaCOLWD[5]+L_inaCOLWD[6]+3);	
								if(j==1)
									L_strSEPRTR="\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";
								else
								{
									L_strTEMP="   "+sub_str(L_strTEMP,0,L_strTEMP.length()-3);
									L_strSEPRTR="\n";
								}
								M_intLINNO++;
							}
							j=6;
							L_flgTOTAL=true;
						}
						else 
						{
							if(j>8)
								L_strTEMP=nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"  -");
							else
								L_strTEMP=nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"");
							if(L_strTEMP.length()>19)
								L_strTEMP=sub_str(L_strTEMP,0,18);
							try
							{
								if(L_strTEMP.equals("  -"))
									L_strTEMP=padSTRING('C',L_strTEMP,L_inaCOLWD[j]);	
								else
								{
									Double.parseDouble(L_strTEMP);
									L_strTEMP=padSTRING('L',L_strTEMP,L_inaCOLWD[j]);	
								}
							}
							catch(Exception e)
							{
								L_strTEMP=padSTRING('R',L_strTEMP,L_inaCOLWD[j]);
							}
						}
					}
					else
					{
						if(j>8)
							L_strTEMP=nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"  -");
						else
							L_strTEMP=nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"");
						if(L_strTEMP.length()>19)
							L_strTEMP=sub_str(L_strTEMP,0,18);
						try
						{//to check whether data is nuemeric
							if(L_strTEMP.equals("  -"))
								L_strTEMP=padSTRING('C',L_strTEMP,L_inaCOLWD[j]);	
							else
							{
								Double.parseDouble(L_strTEMP);
								L_strTEMP=padSTRING('L',L_strTEMP,L_inaCOLWD[j]);	
							}
						}
						catch(Exception e)
						{
							L_strTEMP=padSTRING('R',L_strTEMP,L_inaCOLWD[j]);
						}
					}
					L_strREPORT+=L_strTEMP;
				}
				if(L_strREPORT.length()>1)
				{
					if(L_flgTOTAL)
					{
						prnFMTCHR(L_doutREPORT,M_strBOLD);
						L_doutREPORT.writeBytes(L_strREPORT);
					}
					else
						L_doutREPORT.writeBytes("   "+L_strREPORT);
					if(L_flgTOTAL)
					{
						prnFMTCHR(L_doutREPORT,M_strNOBOLD);
						if(L_strSEPRTR!=null)
						L_doutREPORT.writeBytes(L_strSEPRTR);
//						L_doutREPORT.writeBytes("\n");
//						M_intLINNO++;
					}
					L_flgTOTAL=false;
					L_doutREPORT.writeBytes("\n");
					M_intLINNO++;
					if(M_intLINNO>55)
					{
						M_intLINNO=0;
						PRNDLYHD();
					}
					L_doutREPORT.flush();
				}
				L_strREPORT="";
				L_flgPRNLN=false;
			}
			L_doutREPORT.flush();
			if(rdbDAILY.isSelected())
			{
				hstDLYTOT=new Hashtable<String,String>(20,0.2f);//FOR DAILY TOTAL VALUES
				getDAILY(hstDLYTOT,obaRPDAT);
				//RETRIEVING MONTHLY TOTALS
				M_strSQLQRY="select IN_MKTTP ,IN_INDNO,a.CMT_CODDS,IN_BYRCD,c.PT_PRTNM CMT_BYRNM,IN_DSRCD,d.PT_PRTNM,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,b.cmt_codds,INT_BASRT,INT_CDCVL/1000 INT_CDCVL,INT_DDCVL/1000 INT_DDCVL,INT_TDCVL/1000 INT_TDCVL, IN_APTVL, e.PT_PRTNM PT_CNSNM, IN_DTPCD, INT_PRDCD from VW_INTRN,CO_CDTRN a,CO_CDTRN b,cO_PTMST c,CO_PTMST d,CO_PTMST e where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_BKGDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse("01"+sub_str(M_txtFMDAT.getText(),2)))+"'"
					+" and IN_SALTP=a.CMT_CODCD and a.CMT_CGSTP='MR00SAL'"
					+" and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON'"
					+" and c.PT_PRTTP='C' and IN_BYRCD=c.PT_PRTCD"
					+" and d.PT_PRTTP=IN_DSRTP and IN_DSRCD=d.PT_PRTCD"
					+" and e.PT_PRTTP='C' and IN_CNSCD=e.PT_PRTCD"
					+" and in_stsfl<>'X' and int_stsfl<>'X' and INT_SBSCD1 in "+M_strSBSLS;
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
				Vector<String> vtrQRDAT=new Vector<String>(100,10);
				ResultSetMetaData L_mtdRSSET=L_rstRSSET.getMetaData();
				int L_intCOLCT=L_mtdRSSET.getColumnCount();
				String[] L_staTEMP=new String[L_intCOLCT];
			
				while(L_rstRSSET.next())
				{
					L_staTEMP=new String[L_intCOLCT-2];
					L_staTEMP[0]=nvlSTRVL(L_rstRSSET.getString("IN_MKTTP")," ");
					L_staTEMP[1]=nvlSTRVL(L_rstRSSET.getString("IN_INDNO")," ");
					L_staTEMP[2]=nvlSTRVL(L_rstRSSET.getString(3)," ");
					L_staTEMP[3]=nvlSTRVL(L_rstRSSET.getString(5)+"("+L_rstRSSET.getString(4)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
					L_staTEMP[4]=nvlSTRVL(L_rstRSSET.getString(7)+"~("+L_rstRSSET.getString(6)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
					L_staTEMP[5]=nvlSTRVL(L_rstRSSET.getString(8)," ");
					L_staTEMP[6]=nvlSTRVL(L_rstRSSET.getString(9)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("west"))
						L_staTEMP[7]="1"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("central"))
						L_staTEMP[7]="2"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("north"))
						L_staTEMP[7]="3"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("south"))
						L_staTEMP[7]="4"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("east"))
						L_staTEMP[7]="5"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("export"))
						L_staTEMP[7]="6"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					L_staTEMP[8]=nvlSTRVL(L_rstRSSET.getString("INT_BASRT")," ");
					L_staTEMP[9]=nvlSTRVL(L_rstRSSET.getString("INT_CDCVL")," ");
					L_staTEMP[10]=nvlSTRVL(L_rstRSSET.getString("INT_DDCVL")," ");
					L_staTEMP[11]=nvlSTRVL(L_rstRSSET.getString("INT_TDCVL")," ");
					L_staTEMP[12]=nvlSTRVL(L_rstRSSET.getString("IN_APTVL")," ");
					L_staTEMP[13]=nvlSTRVL(L_rstRSSET.getString("PT_CNSNM")," ");
					L_staTEMP[14]=nvlSTRVL(L_rstRSSET.getString("IN_DTPCD")," ");
					L_staTEMP[15]=nvlSTRVL(L_rstRSSET.getString("INT_PRDCD")," ");
					if (chkSPCOL(L_staTEMP))
						vtrQRDAT.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]+"|"+L_staTEMP[intCNSNM_fn]+"|"+L_staTEMP[intDTPCD_fn]+"|"+L_staTEMP[intPRDCD_fn]);
				}
				hstMONTOT=new Hashtable<String,String>(20,0.2f);
				getDAILY(hstMONTOT,vtrQRDAT.toArray());//TO TOTALISE AND PUT IN HASH TABLE
					////////////////////////////////////
				//RETRIEVING CARRY FORWARD TOTALS FROM INDENT TRANSACTION
				M_strSQLQRY="select int_mkttp,int_indno,a.cmt_codds,in_byrcd,c.pt_prtnm,in_dsrcd,d.pt_prtnm,int_prdds,int_cfwqt,b.cmt_codds,int_basrt,int_cdcvl,int_ddcvl,int_tdcvl,in_aptvl,in_cnscd pt_cnsnm, in_dtpcd, int_prdcd from vw_intrn,co_cdtrn a,co_cdtrn b,co_ptmst c,co_ptmst d where "
					+"  INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND int_cfwqt>0 and INT_SBSCD1 in "+M_strSBSLS+" and a.cmt_cgstp='MR00SAL' and a.cmt_codcd=in_saltp "
					+"  and b.cmt_cgstp='MR00ZON' and b.cmt_codcd=in_zoncd and c.pt_prttp='C' and c.pt_prtcd=in_byrcd and d.pt_prttp=in_dsrtp and d.pt_prtcd=in_dsrcd ";
				L_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
				vtrQRDAT=new Vector<String>(100,10);
				L_mtdRSSET=L_rstRSSET.getMetaData();
				L_intCOLCT=L_mtdRSSET.getColumnCount();
				L_staTEMP=new String[L_intCOLCT];
				while(L_rstRSSET.next())
				{//PUTTING DATA IN A STRING ARRAY
					L_staTEMP=new String[L_intCOLCT-2];
					L_staTEMP[0]=nvlSTRVL(L_rstRSSET.getString(1)," ");
					L_staTEMP[1]=nvlSTRVL(L_rstRSSET.getString(2)," ");
					L_staTEMP[2]=nvlSTRVL(L_rstRSSET.getString(3)," ");
					L_staTEMP[3]=nvlSTRVL(L_rstRSSET.getString(5)+"("+L_rstRSSET.getString(4)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
					L_staTEMP[4]=nvlSTRVL(L_rstRSSET.getString(7)+"~("+L_rstRSSET.getString(6)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
					L_staTEMP[5]=nvlSTRVL(L_rstRSSET.getString(8)," ");
					L_staTEMP[6]=nvlSTRVL(L_rstRSSET.getString(9)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("west"))
						L_staTEMP[7]="1"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("central"))
						L_staTEMP[7]="2"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("north"))
						L_staTEMP[7]="3"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("south"))
						L_staTEMP[7]="4"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("east"))
						L_staTEMP[7]="5"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					if(nvlSTRVL(L_rstRSSET.getString(10)," ").equalsIgnoreCase("export"))
						L_staTEMP[7]="6"+nvlSTRVL(L_rstRSSET.getString(10)," ");
					L_staTEMP[8]=nvlSTRVL(L_rstRSSET.getString("INT_BASRT")," ");
					L_staTEMP[9]=nvlSTRVL(L_rstRSSET.getString("INT_CDCVL")," ");
					L_staTEMP[10]=nvlSTRVL(L_rstRSSET.getString("INT_DDCVL")," ");
					L_staTEMP[11]=nvlSTRVL(L_rstRSSET.getString("INT_TDCVL")," ");
					L_staTEMP[12]=nvlSTRVL(L_rstRSSET.getString("IN_APTVL")," ");
					L_staTEMP[13]=nvlSTRVL(L_rstRSSET.getString("PT_CNSNM")," ");
					L_staTEMP[14]=nvlSTRVL(L_rstRSSET.getString("IN_DTPCD")," ");
					L_staTEMP[15]=nvlSTRVL(L_rstRSSET.getString("INT_PRDCD")," ");
					if (chkSPCOL(L_staTEMP))//CHECKING IF THE RECORD FITS IN FILTER CRITERIA
						vtrQRDAT.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]+"|"+L_staTEMP[intCNSNM_fn]+"|"+L_staTEMP[intDTPCD_fn]+"|"+L_staTEMP[intPRDCD_fn]);
				
				}
				L_rstRSSET.close();
				strHEADNG="";
				hstCRFTOT=new Hashtable<String,String>(20,0.2f);
				getDAILY(hstCRFTOT,vtrQRDAT.toArray());//TO TOTALISE AND PUT IN HASH TABLE
				M_intLINNO=0;//To start summary on new oage
				//COPING DATA FROM ALL 3 HASHTABLES TO FILE.
				PRNLIN("ALL INDIA",2);
				//L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("EXPORT",2);
				//L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("NON ACCOUNTABLE",2);
				L_doutREPORT.writeBytes("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");M_intLINNO++;
				PRNLIN("ALL INDIA|WEST & CENTRAL",0);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("ALL INDIA|OTHERS",0);
				PRNLIN("ALL INDIA|GPPS",0);
				PRNLIN("ALL INDIA|HIPS",0);
				PRNLIN("ALL INDIA|SPECIALITY PS",0);
				PRNLIN("ALL INDIA|WOOD PROFILE",0);
				PRNLIN("ALL INDIA|MASTER BATCH",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("ALL INDIA|NORTH",0);
				PRNLIN("ALL INDIA|EAST",0);
				PRNLIN("ALL INDIA|SOUTH",0);
				PRNLIN("ALL INDIA|WEST",0);
				PRNLIN("ALL INDIA|CENTRAL",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("EXPORT|OTHERS",0);
				PRNLIN("EXPORT|GPPS",0);
				PRNLIN("EXPORT|HIPS",0);
				PRNLIN("EXPORT|SPECIALITY PS",0);
				PRNLIN("EXPORT|WOOD PROFILE",0);
				PRNLIN("EXPORT|MASTER BATCH",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL",0);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH",0);
				PRNLIN("NON ACCOUNTABLE|OTHERS",0);
				PRNLIN("NON ACCOUNTABLE|GPPS",0);
				PRNLIN("NON ACCOUNTABLE|HIPS",0);
				PRNLIN("NON ACCOUNTABLE|SPECIALITY PS",0);
				PRNLIN("NON ACCOUNTABLE|WOOD PROFILE",0);
				PRNLIN("NON ACCOUNTABLE|MASTER BATCH",0);
				PRNLIN("NON ACCOUNTABLE|NORTH",0);
				PRNLIN("NON ACCOUNTABLE|EAST",0);
				PRNLIN("NON ACCOUNTABLE|SOUTH",0);
				PRNLIN("NON ACCOUNTABLE|WEST",0);
				PRNLIN("NON ACCOUNTABLE|CENTRAL",0);
				L_doutREPORT.writeBytes("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - -\n");M_intLINNO++;
				L_doutREPORT.writeBytes("ALL INDIA : \n");M_intLINNO++;
				PRNLIN("ALL INDIA|WEST & CENTRAL|OTHERS",1);
				PRNLIN("ALL INDIA|WEST & CENTRAL|GPPS",1);
				PRNLIN("ALL INDIA|WEST & CENTRAL|HIPS",1);
				PRNLIN("ALL INDIA|WEST & CENTRAL|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|WEST & CENTRAL|WOOD PROFILE",1);
				PRNLIN("ALL INDIA|WEST & CENTRAL|MASTER BATCH",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|OTHERS",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|GPPS",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|HIPS",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|WOOD PROFILE",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|MASTER BATCH",1);
				PRNLIN("ALL INDIA|NORTH|OTHERS",1);
				PRNLIN("ALL INDIA|NORTH|GPPS",1);
				PRNLIN("ALL INDIA|NORTH|HIPS",1);
				PRNLIN("ALL INDIA|NORTH|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|NORTH|WOOD PROFILE",1);
				PRNLIN("ALL INDIA|NORTH|MASTER BATCH",1);
				PRNLIN("ALL INDIA|EAST|OTHERS",1);
				PRNLIN("ALL INDIA|EAST|GPPS",1);
				PRNLIN("ALL INDIA|EAST|HIPS",1);
				PRNLIN("ALL INDIA|EAST|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|EAST|WOOD PROFILE",1);
				PRNLIN("ALL INDIA|EAST|MASTER BATCH",1);
				PRNLIN("ALL INDIA|SOUTH|OTHERS",1);
				PRNLIN("ALL INDIA|SOUTH|GPPS",1);
				PRNLIN("ALL INDIA|SOUTH|HIPS",1);
				PRNLIN("ALL INDIA|SOUTH|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|SOUTH|WOOD PROFILE",1);
				PRNLIN("ALL INDIA|SOUTH|MASTER BATCH",1);
				PRNLIN("ALL INDIA|WEST|OTHERS",1);
				PRNLIN("ALL INDIA|WEST|GPPS",1);
				PRNLIN("ALL INDIA|WEST|HIPS",1);
				PRNLIN("ALL INDIA|WEST|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|WEST|WOOD PROFILE",1);
				PRNLIN("ALL INDIA|WEST|MASTER BATCH",1);
				PRNLIN("ALL INDIA|CENTRAL|OTHERS",1);
				PRNLIN("ALL INDIA|CENTRAL|GPPS",1);
				PRNLIN("ALL INDIA|CENTRAL|HIPS",1);
				PRNLIN("ALL INDIA|CENTRAL|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|CENTRAL|WOOD PROFILE",1);
				PRNLIN("ALL INDIA|CENTRAL|MASTER BATCH",1);
	//NONACOUNTABLE
				L_doutREPORT.writeBytes("NON ACCOUNTABLE : \n");M_intLINNO++;
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|OTHERS",1);
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|WOOD PROFILE",1);
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|MASTER BATCH",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|OTHERS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|WOOD PROFILE",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|MASTER BATCH",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|OTHERS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|WOOD PROFILE",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|MASTER BATCH",1);
				PRNLIN("NON ACCOUNTABLE|EAST|OTHERS",1);
				PRNLIN("NON ACCOUNTABLE|EAST|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|EAST|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|EAST|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|EAST|WOOD PROFILE",1);
				PRNLIN("NON ACCOUNTABLE|EAST|MASTER BATCH",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|OTHERS",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|WOOD PROFILE",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|MASTER BATCH",1);
				PRNLIN("NON ACCOUNTABLE|WEST|OTHERS",1);
				PRNLIN("NON ACCOUNTABLE|WEST|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|WEST|WOOD PROFILE",1);
				PRNLIN("NON ACCOUNTABLE|WEST|MASTER BATCH",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|OTHERS",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|WOOD PROFILE",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|MASTER BATCH",1);
			}
			L_doutREPORT.flush();
			L_doutREPORT.close();
			doPRINT(cl_dat.M_strREPSTR_pbst+"mr_qrcob.doc");
		}catch(Exception e)
		{setMSG(e,"Printing report ");}
	}
/** Method to print line andd header line, if required.
 */
	private void PRNLIN(String P_strLINKEY,int P_intHDIDX)
	{
		try
		{
			if(hstDLYTOT.containsKey(P_strLINKEY)
			   ||hstMONTOT.containsKey(P_strLINKEY)
			   ||hstCRFTOT.containsKey(P_strLINKEY))
			{
				if(hstDLYTOT.get(P_strLINKEY)==null)
					hstDLYTOT.put(P_strLINKEY,"0.0");
				if(hstMONTOT.get(P_strLINKEY)==null)
					hstMONTOT.put(P_strLINKEY,"0.0");
				if(!hstCRFTOT.containsKey(P_strLINKEY))
				{
					hstCRFTOT.put(P_strLINKEY,"0.0");
				}
				if(hstDLYTOT.get(P_strLINKEY).toString().equals("0.000")
				   &&hstMONTOT.get(P_strLINKEY).toString().equals("0.000")
				   &&hstCRFTOT.get(P_strLINKEY).toString().equals("0.000"))
					return;
				if(M_intLINNO>=60||M_intLINNO==0)
				{
					prnFMTCHR(L_doutREPORT,M_strEJT);
					prnFMTCHR(L_doutREPORT,M_strBOLD);
					//		prnFMTCHR(L_doutREPORT,M_strBOLD);
					prnFMTCHR(L_doutREPORT,M_strCPI10);
					M_intPAGNO++;
					L_doutREPORT.writeBytes("SUPREME PETROCHEM LIMITED                                                                                                Page : "+M_intPAGNO+"\n");
					prnFMTCHR(L_doutREPORT,M_strNOBOLD);
					L_doutREPORT.writeBytes("POLYSTYRENE ORDERS BOOKING SUMMARY AS ON "+M_txtFMDAT.getText()+"                                                                 Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"\n");
					prnFMTCHR(L_doutREPORT,M_strBOLD);
					prnFMTCHR(L_doutREPORT,M_strCPI17);
					L_doutREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------\n");
					L_doutREPORT.writeBytes(padSTRING('R',"Catagory",50)+padSTRING('R',"Qty. Booked(Day)",23)+padSTRING('R',"Qty. Booked (Month)",25)+padSTRING('R',"Carry Forward",25)+padSTRING('R',"Cummulative Total",25)+"\n");
					L_doutREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------\n");
					prnFMTCHR(L_doutREPORT,M_strNOBOLD);
					M_intLINNO=5;
				}
				StringTokenizer L_stkTEMP=new StringTokenizer(P_strLINKEY,"|");
				String L_strTEMP="";
				int i=0;
				while(L_stkTEMP.hasMoreTokens())
				{
					L_strTEMP=L_stkTEMP.nextToken();
					if(i==P_intHDIDX)
					{
						if(!strHEADNG.equals(L_strTEMP))
						{
							if(P_intHDIDX==0)
								L_doutREPORT.writeBytes("\n"+L_strTEMP+" : \n");
							else
								L_doutREPORT.writeBytes("\n   "+L_strTEMP+" : \n");
							strHEADNG=L_strTEMP;
							M_intLINNO++;
						}
					}
					i++;
				}
				L_doutREPORT.writeBytes("      "+padSTRING('R',L_strTEMP,25)
					+padSTRING('L',hstDLYTOT.get(P_strLINKEY).toString().equals("0.000") ? "-  " : hstDLYTOT.get(P_strLINKEY).toString(),25)
					+padSTRING('L',hstMONTOT.get(P_strLINKEY).toString().equals("0.000") ? "-  " : hstMONTOT.get(P_strLINKEY).toString(),25)
					+padSTRING('L',hstCRFTOT.get(P_strLINKEY).toString().equals("0.000") ? "-  " : hstCRFTOT.get(P_strLINKEY).toString(),25));
				M_intLINNO++;
				L_doutREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(hstMONTOT.get(P_strLINKEY).toString(),"0.0"))+Double.parseDouble(nvlSTRVL(hstCRFTOT.get(P_strLINKEY).toString(),"0.0")),3),25)+"\n");
			}
		}catch (Exception e)
		{setMSG(e,"Printing Line"+P_strLINKEY);}
	}
	private void getDAILY(Hashtable<String,String> P_hstRPDAT,Object[] P_obaRPDAT)
	{
		try
		{
			//System.out.println("001");
			double L_dblTOTAL1=0.0,L_dblTOTAL2=0.0,L_dblTOTAL3=0.0,
				L_dblTOTWC=0.0,L_dblTOTNES=0.0,
				L_dblINDGP=0.0,L_dblINDHI=0.0,L_dblINDSP=0.0,L_dblINDOT=0.0,L_dblINDWP=0.0,L_dblINDMB=0.0,
				L_dblNORTH=0.0,L_dblSOUTH=0.0,L_dblEAST=0.0,L_dblCENTRAL=0.0,L_dblWEST=0.0,
				L_dblXPTGP=0.0,L_dblXPTHI=0.0,L_dblXPTSP=0.0,L_dblXPTOT=0.0,L_dblXPTWP=0.0,L_dblXPTMB=0.0,
				L_dblWESTGP=0.0,L_dblWESTHI=0.0,L_dblWESTSP=0.0,L_dblWESTOT=0.0,L_dblWESTWP=0.0,L_dblWESTMB=0.0,
				L_dblEASTGP=0.0,L_dblEASTHI=0.0,L_dblEASTSP=0.0,L_dblEASTOT=0.0,L_dblEASTWP=0.0,L_dblEASTMB=0.0,
				L_dblSOUTHGP=0.0,L_dblSOUTHHI=0.0,L_dblSOUTHSP=0.0,L_dblSOUTHOT=0.0,L_dblSOUTHWP=0.0,L_dblSOUTHMB=0.0,
				L_dblNORTHGP=0.0,L_dblNORTHHI=0.0,L_dblNORTHSP=0.0,L_dblNORTHOT=0.0,L_dblNORTHWP=0.0,L_dblNORTHMB=0.0,
				L_dblCENTRALGP=0.0,L_dblCENTRALHI=0.0,L_dblCENTRALSP=0.0,L_dblCENTRALOT=0.0,L_dblCENTRALWP=0.0,L_dblCENTRALMB=0.0,
				L_dblWCGP=0.0,L_dblWCHI=0.0,L_dblWCSP=0.0,L_dblWCOT=0.0,L_dblWCWP=0.0,L_dblWCMB=0.0,
				L_dblNESGP=0.0,L_dblNESHI=0.0,L_dblNESSP=0.0,L_dblNESOT=0.0,L_dblNESWP=0.0,L_dblNESMB=0.0,
				L_dblODRQT=0.0;
			//System.out.println("002");
			double L_dblNACGP=0.0,L_dblNACHI=0.0,L_dblNACSP=0.0,L_dblNACOT=0.0,L_dblNACWP=0.0,L_dblNACMB=0.0,
				L_dblNORTHNAC=0.0,L_dblSOUTHNAC=0.0,L_dblEASTNAC=0.0,L_dblCENTRALNAC=0.0,L_dblWESTNAC=0.0,
				L_dblTOTWCNAC=0.0,L_dblTOTNESNAC=0.0,
				L_dblXPTGPNAC=0.0,L_dblXPTHINAC=0.0,L_dblXPTSPNAC=0.0,L_dblXPTOTNAC=0.0,L_dblXPTWPNAC=0.0,L_dblXPTMBNAC=0.0,
				L_dblWESTGPNAC=0.0,L_dblWESTHINAC=0.0,L_dblWESTSPNAC=0.0,L_dblWESTOTNAC=0.0,L_dblWESTWPNAC=0.0,L_dblWESTMBNAC=0.0,
				L_dblEASTGPNAC=0.0,L_dblEASTHINAC=0.0,L_dblEASTSPNAC=0.0,L_dblEASTOTNAC=0.0,L_dblEASTWPNAC=0.0,L_dblEASTMBNAC=0.0,
				L_dblSOUTHGPNAC=0.0,L_dblSOUTHHINAC=0.0,L_dblSOUTHSPNAC=0.0,L_dblSOUTHOTNAC=0.0,L_dblSOUTHWPNAC=0.0,L_dblSOUTHMBNAC=0.0,
				L_dblNORTHGPNAC=0.0,L_dblNORTHHINAC=0.0,L_dblNORTHSPNAC=0.0,L_dblNORTHOTNAC=0.0,L_dblNORTHWPNAC=0.0,L_dblNORTHMBNAC=0.0,
				L_dblCENTRALGPNAC=0.0,L_dblCENTRALHINAC=0.0,L_dblCENTRALSPNAC=0.0,L_dblCENTRALOTNAC=0.0,L_dblCENTRALWPNAC=0.0,L_dblCENTRALMBNAC=0.0,
				L_dblWCGPNAC=0.0,L_dblWCHINAC=0.0,L_dblWCSPNAC=0.0,L_dblWCOTNAC=0.0,L_dblWCWPNAC=0.0,L_dblWCMBNAC=0.0,
				L_dblNESGPNAC=0.0,L_dblNESHINAC=0.0,L_dblNESSPNAC=0.0,L_dblNESOTNAC=0.0,L_dblNESWPNAC=0.0,L_dblNESMBNAC=0.0,
				L_dblODRQTNAC=0.0;
				StringTokenizer L_stkTEMP=null;
				String[][] L_staDATA=new String[P_obaRPDAT.length][];
			//System.out.println("003");
				for(int i=0;i<P_obaRPDAT.length;i++)//ALL INDIA DOMESTIC TOTAL
				{
					L_stkTEMP=new StringTokenizer(P_obaRPDAT[i].toString(),"|");
					L_staDATA[i]=new String[L_stkTEMP.countTokens()];
					for(int j=0;j<L_staDATA[i].length;j++)
					{
						String L_strTEMP01 = L_stkTEMP.nextToken();
						L_staDATA[i][j]=L_strTEMP01;
					}
				}
			//System.out.println("004");
			
				for(int i=0;i<L_staDATA.length;i++)
				{
					System.out.println("L_staDATA[i][intPRDCD_fn-1] : "+L_staDATA[i][intPRDCD_fn-1]);
					//System.out.println("004a");
					L_dblODRQT=Double.parseDouble(L_staDATA[i][6].toString());
					if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("Export"))
					{
						L_dblTOTAL2+=L_dblODRQT;
						if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							L_dblXPTGP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							L_dblXPTHI+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							L_dblXPTOT+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							L_dblXPTSP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							L_dblXPTWP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							L_dblXPTMB+=L_dblODRQT;
					}
					else if(L_staDATA[i][1].toString().equalsIgnoreCase("Captive Consumption")||L_staDATA[i][0].toString().equalsIgnoreCase("Free Test Sample"))
					{
						L_dblTOTAL3+=L_dblODRQT;
						if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							L_dblNACGP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							L_dblNACHI+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							L_dblNACOT+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							L_dblNACSP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							L_dblNACWP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							L_dblNACMB+=L_dblODRQT;
						
						if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("North"))
						{
							L_dblNORTH+=L_dblODRQT;
							L_dblTOTNESNAC+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblNORTHSPNAC += L_dblODRQT;
								L_dblNESSPNAC   += L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblNORTHWPNAC += L_dblODRQT;
								L_dblNESWPNAC   += L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblNORTHMBNAC += L_dblODRQT;
								L_dblNESMBNAC   += L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblNORTHWPNAC += L_dblODRQT;
								L_dblNESWPNAC   += L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblNORTHMBNAC += L_dblODRQT;
								L_dblNESMBNAC   += L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblNORTHHINAC+=L_dblODRQT;
								L_dblNESHINAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblNORTHGPNAC+=L_dblODRQT;
								L_dblNESGPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblNORTHOTNAC+=L_dblODRQT;
								L_dblNESOTNAC+=L_dblODRQT;
							}	
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("West"))
						{
							L_dblWESTNAC+=L_dblODRQT;
							L_dblTOTWCNAC+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblWESTSPNAC+=L_dblODRQT;
								L_dblWCSPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblWESTWPNAC+=L_dblODRQT;
								L_dblWCWPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblWESTMBNAC+=L_dblODRQT;
								L_dblWCMBNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblWESTHINAC+=L_dblODRQT;
								L_dblWCHINAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblWESTGPNAC+=L_dblODRQT;
								L_dblWCGPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblWESTOTNAC+=L_dblODRQT;
								L_dblWCOTNAC+=L_dblODRQT;
							}
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("South"))
						{
							L_dblSOUTHNAC+=L_dblODRQT;
							L_dblTOTNESNAC+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblSOUTHSPNAC+=L_dblODRQT;
								L_dblNESSPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblSOUTHWPNAC+=L_dblODRQT;
								L_dblNESWPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblSOUTHMBNAC+=L_dblODRQT;
								L_dblNESMBNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblSOUTHHINAC+=L_dblODRQT;
								L_dblNESHINAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblSOUTHGPNAC+=L_dblODRQT;
								L_dblNESGPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblSOUTHOTNAC+=L_dblODRQT;
								L_dblNESOTNAC+=L_dblODRQT;
							}
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("EAST"))
						{
							L_dblTOTNESNAC+=L_dblODRQT;
							L_dblEASTNAC+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblEASTSPNAC+=L_dblODRQT;
								L_dblNESSPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblEASTWPNAC+=L_dblODRQT;
								L_dblNESWPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblEASTMBNAC+=L_dblODRQT;
								L_dblNESMBNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblEASTHINAC+=L_dblODRQT;
								L_dblNESHINAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblEASTGPNAC+=L_dblODRQT;
								L_dblNESGPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblEASTOTNAC+=L_dblODRQT;
								L_dblNESOTNAC+=L_dblODRQT;
							}
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("CENTRAL"))
						{
							L_dblTOTWCNAC+=L_dblODRQT;
							L_dblCENTRALNAC+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblCENTRALSPNAC+=L_dblODRQT;
								L_dblWCSPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblCENTRALWPNAC+=L_dblODRQT;
								L_dblWCWPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblCENTRALMBNAC+=L_dblODRQT;
								L_dblWCMBNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblCENTRALHINAC+=L_dblODRQT;
								L_dblWCHINAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblCENTRALGPNAC+=L_dblODRQT;
								L_dblWCGPNAC+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblCENTRALOTNAC+=L_dblODRQT;
								L_dblWCOTNAC+=L_dblODRQT;
							}
						}
					}
					else
					{
						L_dblTOTAL1+=L_dblODRQT;
						if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							L_dblINDGP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							L_dblINDHI+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							L_dblINDSP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							L_dblINDWP+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							L_dblINDMB+=L_dblODRQT;
						else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							L_dblINDOT+=L_dblODRQT;
						
						if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("North"))
						{
							L_dblTOTNES+=L_dblODRQT;
							L_dblNORTH+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblNORTHSP+=L_dblODRQT;
								L_dblNESSP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblNORTHWP+=L_dblODRQT;
								L_dblNESWP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblNORTHMB+=L_dblODRQT;
								L_dblNESMB+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblNORTHHI+=L_dblODRQT;
								L_dblNESHI+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblNORTHGP+=L_dblODRQT;
								L_dblNESGP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblNORTHOT+=L_dblODRQT;
								L_dblNESOT+=L_dblODRQT;
							}
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("West"))
						{
							L_dblTOTWC+=L_dblODRQT;
							L_dblWEST+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblWESTSP+=L_dblODRQT;
								L_dblWCSP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblWESTWP +=L_dblODRQT;
								L_dblWCWP +=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblWESTMB+=L_dblODRQT;
								L_dblWCMB+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblWESTHI+=L_dblODRQT;
								L_dblWCHI+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblWESTGP+=L_dblODRQT;
								L_dblWCGP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblWESTOT+=L_dblODRQT;
								L_dblWCOT+=L_dblODRQT;
							}
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("South"))
						{
							L_dblTOTNES+=L_dblODRQT;
							L_dblSOUTH+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblSOUTHSP+=L_dblODRQT;
								L_dblNESSP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblSOUTHWP+=L_dblODRQT;
								L_dblNESWP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblSOUTHMB+=L_dblODRQT;
								L_dblNESMB+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblSOUTHHI+=L_dblODRQT;
								L_dblNESHI+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblSOUTHGP+=L_dblODRQT;
								L_dblNESGP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblSOUTHOT+=L_dblODRQT;
								L_dblNESOT+=L_dblODRQT;
							}
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("EAST"))
						{
							L_dblTOTNES+=L_dblODRQT;
							L_dblEAST+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblEASTSP+=L_dblODRQT;
								L_dblNESSP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblEASTWP+=L_dblODRQT;
								L_dblNESWP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblEASTMB+=L_dblODRQT;
								L_dblNESMB+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblEASTHI+=L_dblODRQT;
								L_dblNESHI+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblEASTGP+=L_dblODRQT;
								L_dblNESGP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblEASTOT+=L_dblODRQT;
								L_dblNESOT+=L_dblODRQT;
							}
						}
						else if(sub_str(L_staDATA[i][0],1).equalsIgnoreCase("CENTRAL"))
						{
							L_dblTOTWC+=L_dblODRQT;
							L_dblCENTRAL+=L_dblODRQT;
						   if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("52"))
							{
								L_dblCENTRALSP+=L_dblODRQT;
								L_dblWCSP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("53"))
							{
								L_dblCENTRALWP+=L_dblODRQT;
								L_dblWCWP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("54"))
							{
								L_dblCENTRALMB+=L_dblODRQT;
								L_dblWCMB+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5112"))
							{
								L_dblCENTRALHI+=L_dblODRQT;
								L_dblWCHI+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,4).equalsIgnoreCase("5111"))
							{
								L_dblCENTRALGP+=L_dblODRQT;
								L_dblWCGP+=L_dblODRQT;
							}
						   else if(sub_str(L_staDATA[i][intPRDCD_fn-1],0,2).equalsIgnoreCase("51"))
							{
								L_dblCENTRALOT+=L_dblODRQT;
								L_dblWCOT+=L_dblODRQT;
							}
						}
					}
				}
			//System.out.println("005");
				P_hstRPDAT.put("ALL INDIA",setNumberFormat(L_dblTOTAL1,3));
				P_hstRPDAT.put("EXPORT",setNumberFormat(L_dblTOTAL2,3));
				P_hstRPDAT.put("NON ACCOUNTABLE",setNumberFormat(L_dblTOTAL3,3));
				
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH",setNumberFormat(L_dblTOTNES,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL",setNumberFormat(L_dblTOTWC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH",setNumberFormat(L_dblTOTNESNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL",setNumberFormat(L_dblTOTWCNAC,3));
				P_hstRPDAT.put("ALL INDIA|NORTH",setNumberFormat(L_dblNORTH,3));
				P_hstRPDAT.put("ALL INDIA|SOUTH",setNumberFormat(L_dblSOUTH,3));
				P_hstRPDAT.put("ALL INDIA|WEST",setNumberFormat(L_dblWEST,3));
				P_hstRPDAT.put("ALL INDIA|EAST",setNumberFormat(L_dblEAST,3));
				P_hstRPDAT.put("ALL INDIA|CENTRAL",setNumberFormat(L_dblCENTRAL,3));
				P_hstRPDAT.put("ALL INDIA|GPPS",setNumberFormat(L_dblINDGP,3));
				P_hstRPDAT.put("ALL INDIA|HIPS",setNumberFormat(L_dblINDHI,3));
				P_hstRPDAT.put("ALL INDIA|SPECIALITY PS",setNumberFormat(L_dblINDSP,3));
				P_hstRPDAT.put("ALL INDIA|OTHERS",setNumberFormat(L_dblINDOT,3));
				P_hstRPDAT.put("ALL INDIA|WOOD PROFILE",setNumberFormat(L_dblINDWP,3));
				P_hstRPDAT.put("ALL INDIA|MASTER BATCH",setNumberFormat(L_dblINDMB,3));
				
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|GPPS",setNumberFormat(L_dblWCGP,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|HIPS",setNumberFormat(L_dblWCHI,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|SPECIALITY PS",setNumberFormat(L_dblWCSP,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|OTHERS",setNumberFormat(L_dblWCOT,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|WOOD PROFILE",setNumberFormat(L_dblWCWP,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|MASTER BATCH",setNumberFormat(L_dblWCMB,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|GPPS",setNumberFormat(L_dblNESGP,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|HIPS",setNumberFormat(L_dblNESHI,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|SPECIALITY PS",setNumberFormat(L_dblNESSP,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|OTHERS",setNumberFormat(L_dblNESOT,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|WOOD PROFILE",setNumberFormat(L_dblNESWP,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|MASTER BATCH",setNumberFormat(L_dblNESMB,3));
				
				P_hstRPDAT.put("ALL INDIA|NORTH|GPPS",setNumberFormat(L_dblNORTHGP,3));
				P_hstRPDAT.put("ALL INDIA|SOUTH|GPPS",setNumberFormat(L_dblSOUTHGP,3));
				P_hstRPDAT.put("ALL INDIA|WEST|GPPS",setNumberFormat(L_dblWESTGP,3));
				P_hstRPDAT.put("ALL INDIA|EAST|GPPS",setNumberFormat(L_dblEASTGP,3));
				P_hstRPDAT.put("ALL INDIA|CENTRAL|GPPS",setNumberFormat(L_dblCENTRALGP,3));
				P_hstRPDAT.put("ALL INDIA|NORTH|HIPS",setNumberFormat(L_dblNORTHHI,3));
				P_hstRPDAT.put("ALL INDIA|SOUTH|HIPS",setNumberFormat(L_dblSOUTHHI,3));
				P_hstRPDAT.put("ALL INDIA|WEST|HIPS",setNumberFormat(L_dblWESTHI,3));
				P_hstRPDAT.put("ALL INDIA|EAST|HIPS",setNumberFormat(L_dblEASTHI,3));
				P_hstRPDAT.put("ALL INDIA|CENTRAL|HIPS",setNumberFormat(L_dblCENTRALHI,3));
				P_hstRPDAT.put("ALL INDIA|NORTH|SPECIALITY PS",setNumberFormat(L_dblNORTHSP,3));
				P_hstRPDAT.put("ALL INDIA|SOUTH|SPECIALITY PS",setNumberFormat(L_dblSOUTHSP,3));
				P_hstRPDAT.put("ALL INDIA|WEST|SPECIALITY PS",setNumberFormat(L_dblWESTSP,3));
				P_hstRPDAT.put("ALL INDIA|EAST|SPECIALITY PS",setNumberFormat(L_dblEASTSP,3));
				P_hstRPDAT.put("ALL INDIA|CENTRAL|SPECIALITY PS",setNumberFormat(L_dblCENTRALSP,3));
				P_hstRPDAT.put("ALL INDIA|NORTH|OTHERS",setNumberFormat(L_dblNORTHOT,3));
				P_hstRPDAT.put("ALL INDIA|SOUTH|OTHERS",setNumberFormat(L_dblSOUTHOT,3));
				P_hstRPDAT.put("ALL INDIA|WEST|OTHERS",setNumberFormat(L_dblWESTOT,3));
				P_hstRPDAT.put("ALL INDIA|EAST|OTHERS",setNumberFormat(L_dblEASTOT,3));
				P_hstRPDAT.put("ALL INDIA|CENTRAL|OTHERS",setNumberFormat(L_dblCENTRALOT,3));

				P_hstRPDAT.put("ALL INDIA|NORTH|WOOD PROFILE",setNumberFormat(L_dblNORTHWP,3));
				P_hstRPDAT.put("ALL INDIA|SOUTH|WOOD PROFILE",setNumberFormat(L_dblSOUTHWP,3));
				P_hstRPDAT.put("ALL INDIA|WEST|WOOD PROFILE",setNumberFormat(L_dblWESTWP,3));
				P_hstRPDAT.put("ALL INDIA|EAST|WOOD PROFILE",setNumberFormat(L_dblEASTWP,3));
				P_hstRPDAT.put("ALL INDIA|CENTRAL|WOOD PROFILE",setNumberFormat(L_dblCENTRALWP,3));
				
				P_hstRPDAT.put("ALL INDIA|NORTH|MASTER BATCH",setNumberFormat(L_dblNORTHMB,3));
				P_hstRPDAT.put("ALL INDIA|SOUTH|MASTER BATCH",setNumberFormat(L_dblSOUTHMB,3));
				P_hstRPDAT.put("ALL INDIA|WEST|MASTER BATCH",setNumberFormat(L_dblWESTMB,3));
				P_hstRPDAT.put("ALL INDIA|EAST|MASTER BATCH",setNumberFormat(L_dblEASTMB,3));
				P_hstRPDAT.put("ALL INDIA|CENTRAL|MASTER BATCH",setNumberFormat(L_dblCENTRALMB,3));
				//PUTTIN NON ACCOUNTABLE QTY
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH",setNumberFormat(L_dblNORTHNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH",setNumberFormat(L_dblSOUTHNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST",setNumberFormat(L_dblWESTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST",setNumberFormat(L_dblEASTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL",setNumberFormat(L_dblCENTRALNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|GPPS",setNumberFormat(L_dblNACGP,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|HIPS",setNumberFormat(L_dblNACHI,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SPECIALITY PS",setNumberFormat(L_dblNACSP,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|OTHERS",setNumberFormat(L_dblNACOT,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WOOD PROFILE",setNumberFormat(L_dblNACWP,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|MASTER BATCH",setNumberFormat(L_dblNACMB,3));
				
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|GPPS",setNumberFormat(L_dblWCGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|HIPS",setNumberFormat(L_dblWCHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|SPECIALITY PS",setNumberFormat(L_dblWCSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|OTHERS",setNumberFormat(L_dblWCOTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|WOOD PROFILE",setNumberFormat(L_dblWCWPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|MASTER BATCH",setNumberFormat(L_dblWCMBNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|GPPS",setNumberFormat(L_dblNESGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|HIPS",setNumberFormat(L_dblNESHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|SPECIALITY PS",setNumberFormat(L_dblNESSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|OTHERS",setNumberFormat(L_dblNESOTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|WOOD PROFILE",setNumberFormat(L_dblNESWPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|MASTER BATCH",setNumberFormat(L_dblNESMBNAC,3));
				
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH|GPPS",setNumberFormat(L_dblNORTHGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH|GPPS",setNumberFormat(L_dblSOUTHGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST|GPPS",setNumberFormat(L_dblWESTGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST|GPPS",setNumberFormat(L_dblEASTGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL|GPPS",setNumberFormat(L_dblCENTRALGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH|HIPS",setNumberFormat(L_dblNORTHHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH|HIPS",setNumberFormat(L_dblSOUTHHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST|HIPS",setNumberFormat(L_dblWESTHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST|HIPS",setNumberFormat(L_dblEASTHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL|HIPS",setNumberFormat(L_dblCENTRALHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH|SPECIALITY PS",setNumberFormat(L_dblNORTHSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH|SPECIALITY PS",setNumberFormat(L_dblSOUTHSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST|SPECIALITY PS",setNumberFormat(L_dblWESTSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST|SPECIALITY PS",setNumberFormat(L_dblEASTSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL|SPECIALITY PS",setNumberFormat(L_dblCENTRALSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH|OTHERS",setNumberFormat(L_dblNORTHOTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH|OTHERS",setNumberFormat(L_dblSOUTHOTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST|OTHERS",setNumberFormat(L_dblWESTOTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST|OTHERS",setNumberFormat(L_dblEASTOTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL|OTHERS",setNumberFormat(L_dblCENTRALOTNAC,3));

				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH|WOOD PROFILE",setNumberFormat(L_dblNORTHWPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH|WOOD PROFILE",setNumberFormat(L_dblSOUTHWPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST|WOOD PROFILE",setNumberFormat(L_dblWESTWPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST|WOOD PROFILE",setNumberFormat(L_dblEASTWPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL|WOOD PROFILE",setNumberFormat(L_dblCENTRALWPNAC,3));

				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH|MASTER BATCH",setNumberFormat(L_dblNORTHMBNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH|MASTER BATCH",setNumberFormat(L_dblSOUTHMBNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST|MASTER BATCH",setNumberFormat(L_dblWESTMBNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST|MASTER BATCH",setNumberFormat(L_dblEASTMBNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL|MASTER BATCH",setNumberFormat(L_dblCENTRALMBNAC,3));
				//END OF NON ACCOUNTABLE			
				P_hstRPDAT.put("EXPORT|GPPS",setNumberFormat(L_dblXPTGP,3));
				 P_hstRPDAT.put("EXPORT|HIPS",setNumberFormat(L_dblXPTHI,3));
				 P_hstRPDAT.put("EXPORT|SPECIALITY PS",setNumberFormat(L_dblXPTSP,3));
				 P_hstRPDAT.put("EXPORT|OTHERS",setNumberFormat(L_dblXPTOT,3));
				 P_hstRPDAT.put("EXPORT|WOOD PROFILE",setNumberFormat(L_dblXPTWP,3));
				 P_hstRPDAT.put("EXPORT|MASTER BATCH",setNumberFormat(L_dblXPTMB,3));
			//System.out.println("006");
				
		}catch (Exception e)
		{setMSG(e,"Getting Monthly Details");}
	}

	
	
	/**To check if a perticular row fits in filter criteria set by user as : <br>
	 * Market Type : As selected in combo<br>
	 * Accountable report : Report should exclude data from Sale types stock transfer,Free test sample if only accountable bookings are to be displayed.<br>
	 * Zone : If the record is for distributor specified in case of distributor specific record. (SBSCD level -1)<br>
	 * Grade : If the record is for Grade specified in case of Grade specific record.<br>
	 * Sale Type : If the record is for Sale Type specified in case of sale Type specific record.(SBSCD- level-2)<br>*/
	private boolean chkSPCOL(String []P_staDATVL)
	{
		try
		{
			boolean L_flgRTNVL=false;
			//StringTokenizer L_stkTEMP=new StringTokenizer(cmbMKTTP.getSelectedItem().toString(),"(");
			//L_stkTEMP.nextToken();
			//if(!L_stkTEMP.nextToken(),0,2).equals(nvlSTRVL(P_staDATVL[intMKTTP_fn-1],"")))
			//	return false;
			if(rdbRPTSP.isSelected())
			{
				if((nvlSTRVL(P_staDATVL[intSALTP_fn-1],"").equalsIgnoreCase("Stock Transfer"))
				   ||(nvlSTRVL(P_staDATVL[intSALTP_fn-1],"").equalsIgnoreCase("Free Test Sample")))
					return false;
			}       
			if(rdbZONSP.isSelected())
				if(!(sub_str(nvlSTRVL(P_staDATVL[intZONCD_fn-1],"  "),1).equalsIgnoreCase(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString())))
					return false;
			if(rdbSTPSP.isSelected())
				if(!(nvlSTRVL(P_staDATVL[intSALTP_fn-1],"").equalsIgnoreCase(cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString())))
					return false;
			if(rdbDSTSP.isSelected())
			{
			  	if(!(new StringTokenizer(nvlSTRVL(P_staDATVL[intDSRCD_fn-1],""),"~").nextToken()).equals(txtDSTNM.getText()))
					return false;
			}
			if(rdbGRDSP.isSelected())
				if(!nvlSTRVL(P_staDATVL[intPRDDS_fn-1],"").equals(txtGRDDS.getText()))
					return false;
		}catch(Exception e)
		{
			setMSG(e,"chkSPCOL");
			return false;
		}
		return true;
	}
	/**For Drag & drop of list	 */
	public void mousePressed(MouseEvent LP_ME)
	{
		super.mousePressed(LP_ME);
		if(M_objSOURC==lstODRBY)
			intOLDSL=lstODRBY.getSelectedIndex();
	}
	/**For Drag & drop of list	 */
	public void mouseReleased(MouseEvent LP_ME)
	{
		super.mouseReleased(LP_ME);
		if(M_objSOURC==lstODRBY&&intOLDSL!=-1)
		{
			int L_intNEWSL=lstODRBY.getSelectedIndex();
			if(L_intNEWSL!=intOLDSL)
			{
				vtrODRBY.insertElementAt(vtrODRBY.remove(intOLDSL),L_intNEWSL);
				lstODRBY.setListData(vtrODRBY);
			}
			intOLDSL=-1;
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC==txtDSTNM)
		{
			setMSG("Enter Distributor Name, Press 'F1' for help..",'N');
		}
		else if(M_objSOURC==txtGRDDS)
		{
			setMSG("Enter Grade Description, Press 'F1' for help..",'N');
		}
	}
	boolean vldDATA()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(M_cmbDESTN.getSelectedIndex()==0)
				{
					setMSG("Please select Printer ..",'E');
					M_cmbDESTN.requestFocus();
					return false;
				}
			}
			if(M_txtFMDAT.getText().length()==0)
			{
				setMSG("Please Enter From Date ..",'E');
				M_txtFMDAT.requestFocus();
				return false;
			}
			if(M_txtTODAT.getText().length()==0)
			{
				setMSG("Please Enter To Date ..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(M_txtFMDAT.getText()).compareTo(M_fmtLCDAT.parse(M_txtTODAT.getText()))>0)
			{
				setMSG("From Date cannot be greater than To Date ..",'E');
				M_txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(M_txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
			{
				setMSG("Report date cannot be greater than Today ..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			return true;
		}catch(Exception e)
		{setMSG(e,"while validating data");return false;}
	}
	
	private String getRPFTR()
	{
		String L_strRPTTP=null;
		String L_strRGNWS=null,L_strREPOF=null,L_strMKTTP=null,L_strZON=null,L_strSTP=null,
			L_strDST=null,L_strGRD=null;
		if(rdbDAILY.isSelected())
			L_strRPTTP="Daily ";
		else if(rdbDETAIL.isSelected())
			L_strRPTTP="Detailed ";
		else
			L_strRPTTP="Summary ";
		if(chbRGNWS.isSelected())
			L_strRGNWS="Region wise  ";
		if(rdbRPTSP.isSelected())
			L_strREPOF="Accountable ";
		else
			L_strREPOF="All ";
		if(rdbZONAL.isSelected())
			L_strZON="All \t";
		else
			L_strZON=cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString()+" ";
		if(rdbDSTAL.isSelected())
			L_strDST="All \t";
		else
			L_strDST=txtDSTNM.getText()+" \t";
		if(rdbSTPAL.isSelected())
			L_strSTP="All \t";
		else
			L_strSTP=cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString()+" ";
		if(rdbGRDAL.isSelected())
			L_strGRD="All \t";
		else
			L_strGRD=txtGRDDS.getText()+" \t";
		String L_strRTNVL=nvlSTRVL(L_strRGNWS,"")+" "+L_strRPTTP+"Report of "+L_strREPOF+(String)cmbMKTTP.getSelectedItem()+" Orders Booked for\n ZONE : "+L_strZON+"SALE TYPE : "+L_strSTP+"DISTRIBUTOR : "+L_strDST+"GRADE : "+L_strGRD;
		return L_strRTNVL;
	}
	
	private String sub_str(String LP_STRVL, int LP_STRCT, int LP_ENDCT)
	{
		try
		{
	
			//System.out.println(LP_STRVL+" :  "+LP_STRCT+" /  "+LP_ENDCT);
		}
		catch(Exception e)	{setMSG(e,"sub_str");}
		return LP_STRVL.substring(LP_STRCT,LP_ENDCT);
	}

	
	private String sub_str(String LP_STRVL, int LP_STRCT)
	{
		try
		{
	
			//System.out.println(LP_STRVL+" :  "+LP_STRCT);
		}
		catch(Exception e)	{setMSG(e,"sub_str");}
		return LP_STRVL.substring(LP_STRCT);
	}
	
}