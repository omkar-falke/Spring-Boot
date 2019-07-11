//import spl.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.plaf.TableUI;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.sql.ResultSetMetaData;import java.sql.ResultSet;

/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>Customer Order Booking Report cum Query</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                  Gives daily order booking       report, Customised report for a given period </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Existing reports      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\mr_qrbrn.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_qrbrn.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>DNM </TD></TR>  <TR>    <TD>Date</TD>    <TD>08/11/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>For Base classes revision and Subsystem implementation</TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>AAP</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>20/09/2003</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>2.0.0.</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>VW_CRFWD</TD>    <TD> </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class mr_qrbrn extends cl_rbase
{
	private double L_dblGRDTOT=0.0,L_dblBASRT_GRD=0.0,L_dblCDCVL_GRD=0.0,L_dblDDCVL_GRD=0.0,L_dblTDCVL_GRD=0.0,L_dblCPTVL_GRD=0.0;
	private String L_strGRDDS="AAA";private int L_intGRDCL=0;
	/** Data output stream for report to file*/
	private DataOutputStream L_doutREPORT;/** Current heading / catagory in report*/
	private String strHEADNG;/** Hashtables for toatlised figures in report*/
	private Hashtable<String,String> hstDLYTOT,hstMONTOT,hstCRFTOT;/** Report order set by user*/
	private int L_intORDR1=-1,L_intORDR2=-1,L_intORDR3=-1,L_intORDR4=-1;/** For table column header*/
	private String[] L_COLHD;/** Table to hold report data*/
	private cl_JTBL tblREPORT;	/**Panel for Report table	 */
	private JPanel pnlREPORT=new JPanel(null);/** Index of old selection in List. For drag and drop provision*/
	private int intOLDSL;/** String Array for table header*/
	// IN_MKTTP,IN_INDNO,IN_SALTP,IN_BYRCD,IN_DSRCD,INT_PRDDS,INT_INDQT,INT_INDDT
	private final String[] staCOLHD_fn=new String[]{"Market Type","Indent No","Sale Type",
                                            	    "Buyer","Distributor","Grade",
                                            	    "Indent Qty","Zone","Base Rate",
                                            	    "C.Dsc.","D.Dsc.","T.Dsc.",
                                            	    "Term"};/** Column width in report*/
	private final int[] inaCOLWD_fn=new int[]{0,9,9,30,30,20,12,10,10,7,7,7,4};/** column index in result set*/
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
						 intAPTVL_fn=12;/** Display total zone-wise*/
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
	private JRadioButton rdbDETAIL;/** summery report */
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
	private JTextField txtGRDDS;
	Object[] obaRPDAT;
	/**Construct the screen and populates Market Type combo from CO_CDTRN	 */
	mr_qrbrn()
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
		//ADDING REPORT ON PANEL
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
			add(new JScrollPane(lstODRBY,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),1,1,3.9,1,L_pnlTEMP,'R');
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
				{
					cmbMKTTP.addItem(M_rstRSSET.getString("CMT_CODDS")+" ("+M_rstRSSET.getString("CMT_CODCD")+")");
				}
				M_rstRSSET.close();
			}
			vtrODRBY=new Vector<String>(3,1);
					
		}catch(Exception e)
		{setMSG(e,"child.constructor");}
	}
	/**<B>TASKS : .</B><br>
	 * &nbsp&nbsp&nbsp&nbsprdbDSTAL : Update list data, Hide Sp. DST name txtfield. If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsprdbGRDAL : Update list data, Hide Sp. GRD discription txtfield. If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsprdbZONAL : Update list data, If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsprdbSTPAL : Update list data, If Summary is selected, set total check to true<br>
	 * &nbsp&nbsp&nbsp&nbsprdbDSTSP : Update list data, set total check to false, show txtfield for sp. DST<br>
	 * &nbsp&nbsp&nbsp&nbsprdbGRDSP : Update list data, set total check to false, show txtfield for sp. DST<br>
	 * &nbsp&nbsp&nbsp&nbsprdbZONSP : Update list data, set total check to false, <br>
	 * &nbsp&nbsp&nbsp&nbsprdbSTPSP : Update list data, set total check to false, <br>
	 */
	public void actionPerformed(ActionEvent LP_AE)
	{
		try
		{
			super.actionPerformed(LP_AE);
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst&&cl_dat.M_cmbSBSL1_pbst.getItemCount()>0)
				chbRGNWS.setVisible(true);
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
				M_calLOCAL.add(Calendar.DATE,-1);
				M_txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				M_txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				rdbZONAL.setSelected(true);rdbDSTAL.setSelected(true);rdbGRDAL.setSelected(true);
				rdbSTPAL.setSelected(true);rdbRPTAL.setSelected(true);
				chbZONTOT.setSelected(true);chbDSTTOT.setSelected(true);chbGRDTOT.setSelected(false);
				chbSTPTOT.setSelected(true);
				vtrODRBY.removeAllElements();
				vtrODRBY.addElement("Sale Type");
				vtrODRBY.addElement("Zone");vtrODRBY.addElement("Distributor");vtrODRBY.addElement("Grade");
				lstODRBY.setListData(vtrODRBY);
				lstODRBY.setEnabled(false);
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
				M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD FROM CO_PTMST WHERE PT_PRTTP='D' ORDER BY PT_PRTNM";
				M_strHLPFLD = "txtDSTNM";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Distributor Name","Distributor Code"},2,"CT");
			}
			else if(M_objSOURC==txtGRDDS&&L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strSQLQRY="SELECT DISTINCT INT_PRDDS FROM VW_INTRN WHERE INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_INDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and INT_SBSCD1 in "+M_strSBSLS+" ORDER BY INT_PRDDS";
				M_strHLPFLD = "txtGRDDS";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {staCOLHD_fn[intPRDDS_fn-1]},1,"CT");
			}

		}catch(Exception e)
		{
			setMSG(e,"child.keypressed");
		}
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
		{
			System.out.println("Exception in exeHLPOK : "+L_EX);
		}
	}
	private void setGRDTOT(int i)
	{
			tblREPORT.setRowColor(i,Color.blue);
						if(L_strGRDDS.substring(0,2).equals("SC"))
							tblREPORT.setValueAt("Total GPPS",i,L_intGRDCL);
						else if(L_strGRDDS.substring(0,2).equals("SH"))
							tblREPORT.setValueAt("Total HIPS",i,L_intGRDCL);
						else if(L_strGRDDS.substring(0,2).equals("SP"))
							tblREPORT.setValueAt("Total SPS",i,L_intGRDCL);
						
						tblREPORT.setValueAt(setNumberFormat(L_dblGRDTOT,3),i,6);
						tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_GRD/L_dblGRDTOT,2),i,7);
						tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_GRD/L_dblGRDTOT,2),i,11);
						tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_GRD/L_dblGRDTOT,2),i,12);
						tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_GRD/L_dblGRDTOT,2),i,13);
						tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_GRD/L_dblGRDTOT,0),i,10);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
						+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
						+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
													
						L_dblGRDTOT=0.0;L_dblBASRT_GRD=0.0;L_dblCDCVL_GRD=0.0;L_dblDDCVL_GRD=0.0;L_dblTDCVL_GRD=0.0;L_dblCPTVL_GRD=0.0;
	}
	/**Method to fire a qrery, if required<br>filter the data<br>Display it in tabular format
	 */
	void exePRINT()
	{
		if(vldDATA())
		try
		{
			setMSG("",'N');
			String[] L_staTEMP=null;
			if(vtrQRDAT==null)
			{//FIRING QUERY TO DB DURING FIRST FORMATTING
				setMSG("Collecting data for current month ..",'N');
				M_strSQLQRY="select IN_MKTTP , IN_INDNO,a.CMT_CODDS,IN_BYRCD,c.PT_PRTNM CMT_BYRNM,IN_DSRCD,d.PT_PRTNM,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,b.cmt_codds,INT_BASRT,INT_CDCVL ,INT_DDCVL ,INT_TDCVL, IN_CPTVL from MR_INMST,MR_INTRN,CO_CDTRN a,CO_CDTRN b,cO_PTMST c,CO_PTMST d where IN_CMPCD=INT_CMPCD and IN_MKTTP=INT_MKTTP and IN_INDNO=INT_INDNO and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_INDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"'"
					+" and IN_SALTP=a.CMT_CODCD and a.CMT_CGSTP='MR00SAL'"
					+" and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON' "
					+" and c.PT_PRTTP='C' and IN_BYRCD=c.PT_PRTCD"
					+" and d.PT_PRTTP='D' and IN_DSRCD=d.PT_PRTCD"
					+" and in_stsfl<>'X' and int_stsfl<>'X'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				vtrQRDAT=new Vector<String[]>(100,10);
				ResultSetMetaData L_mtdRSSET=M_rstRSSET.getMetaData();
				int L_intCOLCT=L_mtdRSSET.getColumnCount();
				L_staTEMP=new String[L_intCOLCT];
				while(M_rstRSSET.next())
				{
					L_staTEMP=new String[L_intCOLCT-2];
					L_staTEMP[0]=nvlSTRVL(M_rstRSSET.getString("IN_MKTTP")," ");
					L_staTEMP[1]=nvlSTRVL(M_rstRSSET.getString("IN_INDNO")," ");
					L_staTEMP[2]=nvlSTRVL(M_rstRSSET.getString(3)," ");
					L_staTEMP[3]=nvlSTRVL(M_rstRSSET.getString(5)+"("+M_rstRSSET.getString(4)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
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
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("export"))
						L_staTEMP[7]="6"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					L_staTEMP[8]=nvlSTRVL(M_rstRSSET.getString("INT_BASRT")," ");
					L_staTEMP[9]=nvlSTRVL(M_rstRSSET.getString("INT_CDCVL")," ");
					L_staTEMP[10]=nvlSTRVL(M_rstRSSET.getString("INT_DDCVL")," ");
					L_staTEMP[11]=nvlSTRVL(M_rstRSSET.getString("INT_TDCVL")," ");
					L_staTEMP[12]=nvlSTRVL(M_rstRSSET.getString("IN_CPTVL")," ");
					vtrQRDAT.addElement(L_staTEMP);//Vector to hold total, unsorted data
				}
				M_rstRSSET.close();
			
			/////////////
			//FIRING QUERY TO DB DURING FIRST FORMATTING
				setMSG("Collecting data for previous months ..",'N');
				M_strSQLQRY="select IN_MKTTP , INT_INDNO,CMT_SALTP,IN_BYRCD,CMT_BYRNM,IN_DSRCD,PT_DSRNM,INT_PRDDS,CF_QTY,CMT_ZONDS,INT_BASRT,INT_CDCVL ,INT_DDCVL ,INT_TDCVL, IN_APTVL from VW_CRFWD where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'   AND INT_SBSCD1 in "+M_strSBSLS;
				M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			//	vtrQRDAT=new Vector(100,10);
				 L_mtdRSSET=M_rstRSSET.getMetaData();
				 L_intCOLCT=L_mtdRSSET.getColumnCount();
				L_staTEMP=new String[L_intCOLCT];
				while(M_rstRSSET.next())
				{
					L_staTEMP=new String[L_intCOLCT-2];
					L_staTEMP[0]=nvlSTRVL(M_rstRSSET.getString("IN_MKTTP")," ");
					L_staTEMP[1]=nvlSTRVL(M_rstRSSET.getString("INT_INDNO")," ");
					L_staTEMP[2]=nvlSTRVL(M_rstRSSET.getString(3)," ");
					L_staTEMP[3]=nvlSTRVL(M_rstRSSET.getString(5)+"("+M_rstRSSET.getString(4)+")"," ");//HARD CODED FOR DISPLAY IN description(code) FORMAT
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
					if(nvlSTRVL(M_rstRSSET.getString(10)," ").equalsIgnoreCase("export"))
						L_staTEMP[7]="6"+nvlSTRVL(M_rstRSSET.getString(10)," ");
					L_staTEMP[8]=nvlSTRVL(M_rstRSSET.getString("INT_BASRT")," ");
					L_staTEMP[9]=nvlSTRVL(M_rstRSSET.getString("INT_CDCVL")," ");
					L_staTEMP[10]=nvlSTRVL(M_rstRSSET.getString("INT_DDCVL")," ");
					L_staTEMP[11]=nvlSTRVL(M_rstRSSET.getString("INT_TDCVL")," ");
					L_staTEMP[12]=nvlSTRVL(M_rstRSSET.getString("IN_APTVL")," ");
					vtrQRDAT.addElement(L_staTEMP);//Vector to hold total, unsorted data
				}
				M_rstRSSET.close();
			}
			setMSG("Formatting data ..",'N');
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
							L_vtrORDR1.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]);
						else
							L_vtrORDR1.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]);
					}
				}
			}
			//SORTING DATA IN USER DEFINED ORDER
			obaRPDAT=L_vtrORDR1.toArray();
			Arrays.sort(obaRPDAT);
			//CREATING TABLE HEADER AS PER USER DEFINED SORT ORDER
			if(rdbDAILY.isSelected())
				L_COLHD = new String[]{"FL","Sale Type","Zone","Distributor","Buyer","Grade","Indent Qty","Base Rate","W.Avg. Credit","Net Price","Term","C.Dsc.","D.Dsc.","T.Dsc.","Term"};
			else
				L_COLHD = new String[]{"FL",staCOLHD_fn[L_intORDR1-1],staCOLHD_fn[L_intORDR2-1],staCOLHD_fn[L_intORDR3-1],staCOLHD_fn[L_intORDR4-1],"Buyer","Indent Qty","Base Rate","W.Avg. Credit","Net Price","Term","C.Dsc.","D.Dsc.","T.Dsc."};
			int[] L_COLSZ = {20,100,100,100,100,100,100,100,100,50,50,50,50,50,50};
			pnlREPORT.removeAll();
			//100 ROWs ADDITIONAL FOR VAUES OF SUBTOTALS TO BE DISPLAYED
			System.out.println(getROWCNT());
			tblREPORT = crtTBLPNL(pnlREPORT,L_COLHD,obaRPDAT.length+getROWCNT(),1,1,7,5.8,L_COLSZ,new int[]{0});
			tblREPORT.setEnabled(false);
			StringTokenizer L_stkTEMP=null;
			//STRINGS TO HOLD VALUES OF COLUMNS IN PRESENT RECORD.
			//FOR SUB-CATARORYWISE TOTALS
			double L_dblTOT1=0.0,L_dblTOT2=0.0,L_dblTOT3=0.0,L_dblTOT4=0.0,L_dblTOTRGN=0.0;
			double L_dblBASRT_RGN=0.0,L_dblCDCVL_RGN=0.0,L_dblDDCVL_RGN=0.0,L_dblTDCVL_RGN=0.0,L_dblCPTVL_RGN=0.0,
				L_dblBASRT_1=0.0,L_dblCDCVL_1=0.0,L_dblDDCVL_1=0.0,L_dblTDCVL_1=0.0,L_dblCPTVL_1=0.0,
				L_dblBASRT_2=0.0,L_dblCDCVL_2=0.0,L_dblDDCVL_2=0.0,L_dblTDCVL_2=0.0,L_dblCPTVL_2=0.0,
				L_dblBASRT_3=0.0,L_dblCDCVL_3=0.0,L_dblDDCVL_3=0.0,L_dblTDCVL_3=0.0,L_dblCPTVL_3=0.0,
			//	L_dblBASRT_GRD=0.0,L_dblCDCVL_GRD=0.0,L_dblDDCVL_GRD=0.0,L_dblTDCVL_GRD=0.0,L_dblCPTVL_GRD=0.0,
				L_dblBASRT_4=0.0,L_dblCDCVL_4=0.0,L_dblDDCVL_4=0.0,L_dblTDCVL_4=0.0,L_dblCPTVL_4=0.0,
			L_dblBASRT_GR=0.0,L_dblCDCVL_GR=0.0,L_dblDDCVL_GR=0.0,L_dblTDCVL_GR=0.0,L_dblCPTVL_GR=0.0;
			int j=-1;
			//OLD VALUES OF COLUMNS.
			String L_strCOL1VL="",L_strCOL2VL="",L_strCOL3VL="",L_strCOL4VL="";
			//FLAGS TO INDICATE WHETHER TOTAL FOR RESP. COL. IS TO BE DISPLAYED
			boolean L_flgTOTAL=false,L_flgTOTCL1=false,L_flgTOTCL2=false,L_flgTOTCL3=false,L_flgTOTCL4=false;
			//INITIALIZING ABOVE FLAGS
			if(L_COLHD[1].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[1].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[1].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[1].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL1=true;
			if(L_COLHD[2].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[2].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[2].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[2].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL2=true;
			if(L_COLHD[3].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[3].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[3].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[3].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL3=true;
			if(L_COLHD[4].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[4].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[4].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[4].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL4=true;
			int L_intFLPRN=0;//FLAG TO INDICATE TOTALS OF HOW MANY COLUMNS ARE TO BE DISPLAYED
			double L_dblGRTOT=0.0;//L_dblGRDTOT=0.0;//GRAND TOTAL
			double L_dblBYRQTY=0.0,
				L_dblBASRT=0.0,
				L_dblCDCVL=0.0,
				L_dblDDCVL=0.0,
				L_dblTDCVL=0.0,
				L_dblCPTVL=0.0;
			String L_strBYRNM="";
			int i=0,//LOOP COUNTER
				L_intROWID=0,//TO REMEMBER WHERE TOTAL REALISATION VALUES ARE TO BE PUT
				L_intGRDCL1=0;//To IDENTIFY GRADE DESCRIPTION COL
			for(int a=0;a<L_COLHD.length;a++)
				if(L_COLHD[a].equalsIgnoreCase("GRADE"))
				{
					L_intGRDCL=a;
					break;
				}System.out.println(L_intGRDCL); 
			String L_strHDGST="";
			if(!rdbSUMRY.isSelected())
				L_strHDGST="Total ";
			String[] L_staCOLVL=new String[12];
			String L_strZONVL="";
			for(i=0;i<tblREPORT.getRowCount();i++)
			{
				L_intFLPRN=0;L_intROWID=1;
				
				if(j==obaRPDAT.length-1)//IF END OF REPORT IS ACHEIVED PRINT SUMMERY
				{L_intROWID=1;
	/*					tblREPORT.setValueAt(L_strCOL4VL,i,4);
						tblREPORT.setValueAt(L_staCOLVL[4],i,5);
						L_dblBYRQTY+=Double.parseDouble(L_staCOLVL[6]);
						L_dblBASRT=L_dblBASRT+Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
						L_dblCDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
						L_dblDDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
						L_dblTDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
						L_dblCPTVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
						
						tblREPORT.setValueAt( setNumberFormat(L_dblBYRQTY,3),i-L_intROWID+1,6);
						tblREPORT.setValueAt(setNumberFormat(L_dblBASRT/L_dblBYRQTY,2),i-L_intROWID+1,7);
						tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL/L_dblBYRQTY,2),i-L_intROWID+1,11);
						tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL/L_dblBYRQTY,2),i-L_intROWID+1,12);
						tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL/L_dblBYRQTY,2),i-L_intROWID+1,13);
						tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL/L_dblBYRQTY,0),i-L_intROWID+1,10);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,7).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,11).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,12).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,13).toString(),"0.0")),2),i-L_intROWID+1,9);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,11).toString(),"0.0"))
															+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,12).toString(),"0.0"))
															+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,13).toString(),"0.0")),2),i-L_intROWID+1,8);
	*/			 
				 if(!rdbSUMRY.isSelected()){
//					tblREPORT.setValueAt(L_staCOLVL[3],i,4);
//					tblREPORT.setValueAt(L_staCOLVL[4],i,5);
					tblREPORT.setValueAt(setNumberFormat(L_dblBYRQTY,3),i-L_intROWID,6);
					tblREPORT.setValueAt(setNumberFormat(L_dblBASRT/L_dblBYRQTY,2),i-L_intROWID,7);
					tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL/L_dblBYRQTY,2),i-L_intROWID,11);
					tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL/L_dblBYRQTY,2),i-L_intROWID,12);
					tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL/L_dblBYRQTY,2),i-L_intROWID,13);
					tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL/L_dblBYRQTY,0),i-L_intROWID,10);
					tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,7).toString(),"0.0"))
														-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,11).toString(),"0.0"))
														-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,12).toString(),"0.0"))
														-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,13).toString(),"0.0")),2),i-L_intROWID,9);
					tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,11).toString(),"0.0"))
														+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,12).toString(),"0.0"))
														+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,13).toString(),"0.0")),2),i-L_intROWID,8);
				 }
//						tblREPORT.setRowColor(i,Color.blue);
/*						if(L_strGRDDS.substring(0,2).equals("SC"))
							tblREPORT.setValueAt(L_strHDGST+" GPPS",i,L_intGRDCL);
						else if(L_strGRDDS.substring(0,2).equals("SH"))
							tblREPORT.setValueAt(L_strHDGST+" HIPS",i,L_intGRDCL);
						else if(L_strGRDDS.substring(0,2).equals("SP"))
							tblREPORT.setValueAt(L_strHDGST+" SPS",i,L_intGRDCL);
						
						tblREPORT.setValueAt(setNumberFormat(L_dblGRDTOT,3),i,6);
						tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_GRD/L_dblGRDTOT,2),i,7);
						tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_GRD/L_dblGRDTOT,2),i,11);
						tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_GRD/L_dblGRDTOT,2),i,12);
						tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_GRD/L_dblGRDTOT,2),i,13);
						tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_GRD/L_dblGRDTOT,0),i,10);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
						+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
						+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
													
						L_intROWID++;i++;
						L_dblGRDTOT=0.0;L_dblBASRT_GRD=0.0;L_dblCDCVL_GRD=0.0;L_dblDDCVL_GRD=0.0;L_dblTDCVL_GRD=0.0;L_dblCPTVL_GRD=0.0;
*/					if(L_flgTOTCL4)
					{//Diplay total of fourth col
						if(!rdbSUMRY.isSelected())
							tblREPORT.setRowColor(i,Color.blue);
						else
							tblREPORT.setRowColor(i,Color.darkGray);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_4/L_dblTOT4,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_4/L_dblTOT4,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_4/L_dblTOT4,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_4/L_dblTOT4,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_4/L_dblTOT4,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
//							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
						
						tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
						if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
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
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								i++;//tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,6);
						}
						else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
						{
							setGRDTOT(i);
							i++;L_intROWID++;
						}
					}
					if(L_flgTOTCL3)
					{//Diplay total of third col
						tblREPORT.setRowColor(i,Color.blue);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_3/L_dblTOT3,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_3/L_dblTOT3,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_3/L_dblTOT3,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_3/L_dblTOT3,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_3/L_dblTOT3,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
						
						tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
						
						if(L_COLHD[3].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
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
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								
							i++;//tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i++,6);
						}
						else if(L_intGRDCL==3)//!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
						{
							setGRDTOT(i);
							i++;L_intROWID++;
						}
					}
					if(L_flgTOTCL2)
					{//Diplay total of second col
						if(!rdbSUMRY.isSelected())
							tblREPORT.setRowColor(i,Color.blue);
						else
							tblREPORT.setRowColor(i,Color.magenta);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT2,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT2,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_2/L_dblTOT2,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_2/L_dblTOT2,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_2/L_dblTOT2,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_2/L_dblTOT2,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_2/L_dblTOT2,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
//							tblREPORT.setValueAt(L_strHDGST+L_strCOL2VL,i++,2);
								
						tblREPORT.setValueAt(L_strHDGST+L_strCOL2VL,i++,2);
						if(L_COLHD[2].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
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
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								i++;
						}
						else if(L_intGRDCL==2)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
						{
							setGRDTOT(i);
							i++;L_intROWID++;
						}
					}
					if(L_flgTOTCL1)
					{//Diplay total of first col
						if(!rdbSUMRY.isSelected())
							tblREPORT.setRowColor(i,Color.blue);
						else
							tblREPORT.setRowColor(i,Color.red);
						tblREPORT.setValueAt(setNumberFormat(L_dblTOT1,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT1,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_1/L_dblTOT1,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_1/L_dblTOT1,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_1/L_dblTOT1,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_1/L_dblTOT1,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_1/L_dblTOT1,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
							tblREPORT.setValueAt(L_strHDGST+L_strCOL1VL,i++,1);////////////////////////
						
//						tblREPORT.setValueAt(L_strHDGST+L_strCOL1VL,i++,1);
						if(L_COLHD[1].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
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
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								
							i++;
						}
						else if(L_intGRDCL==1)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
						{
							setGRDTOT(i);
							i++;L_intROWID++;
						}
					}

					break;
				}
				
				L_stkTEMP=new StringTokenizer(obaRPDAT[++j].toString(),"|");
				for(int z=0;z<L_staCOLVL.length;z++)
				{//putting values for this row in a string array
					L_staCOLVL[z]=L_stkTEMP.nextToken().replace('~',' ');
					if(z<L_COLHD.length-1)
					if(L_COLHD[z+1].equals(staCOLHD_fn[7]))
					{//removing digit attached b4 zone
						L_staCOLVL[z]=L_staCOLVL[z].substring(1);
					}
				}
				if(i==0)
				{
					L_strCOL1VL=L_staCOLVL[0];L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];
				}
				else
				{
								//DISPLAYING GP/HI/SPS TOTAL
					if(!L_strCOL1VL.equalsIgnoreCase(L_staCOLVL[0]))
					{//if value for first col is NOT as same as previous row
						if(L_flgTOTCL4)
						{//display total for fourth col
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.darkGray);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_4/L_dblTOT4,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_4/L_dblTOT4,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_4/L_dblTOT4,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_4/L_dblTOT4,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_4/L_dblTOT4,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							L_intROWID++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								L_intROWID++;i++;
								L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
							}
							else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						if(L_flgTOTCL3)
						{//display total of second col
							tblREPORT.setRowColor(i,Color.blue);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_3/L_dblTOT3,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_3/L_dblTOT3,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_3/L_dblTOT3,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_3/L_dblTOT3,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_3/L_dblTOT3,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
							L_intROWID++;
							if(L_COLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,3);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,3);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,3);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								L_intROWID++;i++;
								L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
							}
							else if(L_intGRDCL==3)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						if(L_flgTOTCL2)
						{//displa total of second col
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.magenta);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT2,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_2/L_dblTOT2,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_2/L_dblTOT2,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_2/L_dblTOT2,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_2/L_dblTOT2,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_2/L_dblTOT2,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
							tblREPORT.setValueAt(L_strHDGST+L_strCOL2VL,i++,2);
							L_intROWID++;
							if(L_COLHD[2].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,2);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,2);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,2);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								L_intROWID++;i++;
								L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;							
							}
							else if(L_intGRDCL==2)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						if(L_flgTOTCL1)
						{//display total of fist col
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.red);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT1,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_1/L_dblTOT1,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_1/L_dblTOT1,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_1/L_dblTOT1,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_1/L_dblTOT1,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_1/L_dblTOT1,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
							tblREPORT.setValueAt(L_strHDGST+L_strCOL1VL,i++,1);////////////////////////
							L_intROWID++;
							if(L_COLHD[1].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[0].equalsIgnoreCase("West")&&!L_staCOLVL[0].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,1);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[0].equalsIgnoreCase("North")&&!L_staCOLVL[0].equalsIgnoreCase("South")&&!L_staCOLVL[0].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,1);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,1);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
							}
							else if(L_intGRDCL==1&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						L_dblTOT1=0.0;L_dblTOT2=0.0;L_dblTOT3=0.0;L_dblTOT4=0.0;//intialise total variables
						L_dblBASRT_1=0.0;L_dblCDCVL_1=0.0;L_dblDDCVL_1=0.0;L_dblTDCVL_1=0.0;L_dblCPTVL_1=0.0;
						L_dblBASRT_2=0.0;L_dblCDCVL_2=0.0;L_dblDDCVL_2=0.0;L_dblTDCVL_2=0.0;L_dblCPTVL_2=0.0;
						L_dblBASRT_3=0.0;L_dblCDCVL_3=0.0;L_dblDDCVL_3=0.0;L_dblTDCVL_3=0.0;L_dblCPTVL_3=0.0;
						L_dblBASRT_4=0.0;L_dblCDCVL_4=0.0;L_dblDDCVL_4=0.0;L_dblTDCVL_4=0.0;L_dblCPTVL_4=0.0;
						L_strCOL1VL=L_staCOLVL[0];L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];//copy current values as previous values for next row
						int z=0;
						for(z=0;z<L_staCOLVL.length;z++)//retrieve colindex for zone
							if(L_COLHD[z].equalsIgnoreCase(staCOLHD_fn[7]))
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
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_4/L_dblTOT4,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_4/L_dblTOT4,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_4/L_dblTOT4,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_4/L_dblTOT4,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_4/L_dblTOT4,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							L_intROWID++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
								L_intROWID++;i++;
								L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
							}
							else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						if(L_flgTOTCL3)
						{
							tblREPORT.setRowColor(i,Color.blue);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_3/L_dblTOT3,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_3/L_dblTOT3,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_3/L_dblTOT3,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_3/L_dblTOT3,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_3/L_dblTOT3,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
							L_intROWID++;
							if(L_COLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,3);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,3);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,3);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
								L_intROWID++;i++;
								L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
							}
							else if(L_intGRDCL==3)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						if(L_flgTOTCL2)
						{
							if(!rdbSUMRY.isSelected())
								tblREPORT.setRowColor(i,Color.blue);
							else
								tblREPORT.setRowColor(i,Color.magenta);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT2,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_2/L_dblTOT2,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_2/L_dblTOT2,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_2/L_dblTOT2,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_2/L_dblTOT2,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_2/L_dblTOT2,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL2VL,i++,2);
							L_intROWID++;
							if(L_COLHD[2].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[1].equalsIgnoreCase("West")&&!L_staCOLVL[1].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,2);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(L_staCOLVL[1].equalsIgnoreCase("North")&&!L_staCOLVL[1].equalsIgnoreCase("South")&&!L_staCOLVL[1].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,2);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,2);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
							}
							else if(L_intGRDCL==2&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						L_dblTOT2=0.0;L_dblBASRT_2=0.0;L_dblCDCVL_2=0.0;L_dblDDCVL_2=0.0;L_dblTDCVL_2=0.0;L_dblCPTVL_2=0.0;
						L_dblTOT3=0.0;L_dblBASRT_3=0.0;L_dblCDCVL_3=0.0;L_dblDDCVL_3=0.0;L_dblTDCVL_3=0.0;L_dblCPTVL_3=0.0;
						L_dblTOT4=0.0;L_dblBASRT_4=0.0;L_dblCDCVL_4=0.0;L_dblDDCVL_4=0.0;L_dblTDCVL_4=0.0;L_dblCPTVL_4=0.0;
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
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_4/L_dblTOT4,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_4/L_dblTOT4,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_4/L_dblTOT4,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_4/L_dblTOT4,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_4/L_dblTOT4,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							L_intROWID++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if(L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
								else if(L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
								else
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
								tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
								tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
								tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
								tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
								tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
								tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
								tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																	+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
								L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
							}
							else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						if(L_flgTOTCL3)
						{
							tblREPORT.setRowColor(i,Color.blue);
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT3,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_3/L_dblTOT3,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_3/L_dblTOT3,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_3/L_dblTOT3,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_3/L_dblTOT3,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_3/L_dblTOT3,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL3VL,i++,3);
							L_intROWID++;
							if(L_COLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[2].equalsIgnoreCase("West")&&!L_staCOLVL[2].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,3);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[2].equalsIgnoreCase("North")&&!L_staCOLVL[2].equalsIgnoreCase("South")&&!L_staCOLVL[2].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,3);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,3);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
							}
							else if(L_intGRDCL==3&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						L_dblTOT3=0.0;L_dblBASRT_3=0.0;L_dblCDCVL_3=0.0;L_dblDDCVL_3=0.0;L_dblTDCVL_3=0.0;L_dblCPTVL_3=0.0;
						L_dblTOT4=0.0;L_dblBASRT_4=0.0;L_dblCDCVL_4=0.0;L_dblDDCVL_4=0.0;L_dblTDCVL_4=0.0;L_dblCPTVL_4=0.0;
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
							tblREPORT.setValueAt(setNumberFormat(L_dblTOT4,3),i,6);
							tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_4/L_dblTOT4,2),i,7);
							tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_4/L_dblTOT4,2),i,11);
							tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_4/L_dblTOT4,2),i,12);
							tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_4/L_dblTOT4,2),i,13);
							tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_4/L_dblTOT4,0),i,10);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
							tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							tblREPORT.setValueAt(L_strHDGST+L_strCOL4VL,i++,4);
							L_intROWID++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								tblREPORT.setRowColor(i,Color.red);
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[3].equalsIgnoreCase("West")&&!L_staCOLVL[3].equalsIgnoreCase("Central")))
								{
									tblREPORT.setValueAt(L_strHDGST+" West & Central",i,4);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
							
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[3].equalsIgnoreCase("North")&&!L_staCOLVL[3].equalsIgnoreCase("South")&&!L_staCOLVL[3].equalsIgnoreCase("East")))
								{
									tblREPORT.setValueAt(L_strHDGST+" North, South & East ",i,4);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
								else if(L_strZONVL.equalsIgnoreCase("Export"))
								{
									tblREPORT.setValueAt(L_strHDGST+" Export",i,4);
									tblREPORT.setValueAt(setNumberFormat(L_dblTOTRGN,3),i,6);
									tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_RGN/L_dblTOTRGN,2),i,7);
									tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_RGN/L_dblTOTRGN,2),i,11);
									tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_RGN/L_dblTOTRGN,2),i,12);
									tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_RGN/L_dblTOTRGN,2),i,13);
									tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_RGN/L_dblTOTRGN,0),i,10);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
									tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
																		+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
									L_intROWID++;i++;
									L_dblTOTRGN=0.0;L_dblBASRT_RGN=0.0;L_dblCDCVL_RGN=0.0;L_dblDDCVL_RGN=0.0;L_dblTDCVL_RGN=0.0;L_dblCPTVL_RGN=0.0;
								}
							}
							else if(L_intGRDCL==4&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							{
								setGRDTOT(i);
								i++;L_intROWID++;
							}
						}
						L_dblTOT4=0.0;L_dblBASRT_4=0.0;L_dblCDCVL_4=0.0;L_dblDDCVL_4=0.0;L_dblTDCVL_4=0.0;L_dblCPTVL_4=0.0;
						L_strCOL4VL=L_staCOLVL[3];
						L_intFLPRN=1;
					}
//END OF FOURTH COL TOTAL
				}
				L_dblGRDTOT+=Double.parseDouble(L_staCOLVL[6]);
				L_dblBASRT_GRD+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
				L_dblCDCVL_GRD+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
				L_dblDDCVL_GRD+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
				L_dblTDCVL_GRD+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
				L_dblCPTVL_GRD+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				L_dblTOTRGN+=Double.parseDouble(L_staCOLVL[6]);
				L_dblBASRT_RGN+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
				L_dblCDCVL_RGN+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
				L_dblDDCVL_RGN+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
				L_dblTDCVL_RGN+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
				L_dblCPTVL_RGN+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				L_dblTOT1+=Double.parseDouble(L_staCOLVL[6]);
				L_dblBASRT_1+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
				L_dblCDCVL_1+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
				L_dblDDCVL_1+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
				L_dblTDCVL_1+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
				L_dblCPTVL_1+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				L_dblTOT2+=Double.parseDouble(L_staCOLVL[6]);
				L_dblBASRT_2+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
				L_dblCDCVL_2+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
				L_dblDDCVL_2+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
				L_dblTDCVL_2+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
				L_dblCPTVL_2+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				L_dblTOT3+=Double.parseDouble(L_staCOLVL[6]);
				L_dblBASRT_3+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
				L_dblCDCVL_3+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
				L_dblDDCVL_3+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
				L_dblTDCVL_3+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
				L_dblCPTVL_3+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				L_dblTOT4+=Double.parseDouble(L_staCOLVL[6]);
				L_dblBASRT_4+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
				L_dblCDCVL_4+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
				L_dblDDCVL_4+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
				L_dblTDCVL_4+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
				L_dblCPTVL_4+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				L_dblGRTOT+=Double.parseDouble(L_staCOLVL[6]);
				L_dblBASRT_GR+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
				L_dblCDCVL_GR+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
				L_dblDDCVL_GR+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
				L_dblTDCVL_GR+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
				L_dblCPTVL_GR+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				
				if(L_strBYRNM.equals(L_staCOLVL[4])&&L_strGRDDS.equals(L_staCOLVL[L_intGRDCL-1]))
				{
					L_dblBYRQTY+=Double.parseDouble(L_staCOLVL[6]);
					L_dblBASRT=L_dblBASRT+Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
					L_dblCDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
					L_dblDDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
					L_dblTDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
					L_dblCPTVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				}
				int z=0;
				for(z=0;z<L_staCOLVL.length;z++)
					if(L_COLHD[z].equalsIgnoreCase(staCOLHD_fn[7]))
					   break;
				L_strZONVL=L_staCOLVL[z-1];
				if((!rdbSUMRY.isSelected())
					&&((!L_strBYRNM.equals(L_staCOLVL[4]))||(!L_strGRDDS.equals(L_staCOLVL[L_intGRDCL-1]))))//SHOW DETIALS IF DETILS ARE SELECTED
				{
					tblREPORT.setRowColor(i,Color.black);
					if((L_intFLPRN>3||i==0))
						tblREPORT.setValueAt(L_staCOLVL[0],i,1);
					if((L_intFLPRN>2||i==0))
						tblREPORT.setValueAt(L_staCOLVL[1],i,2);
					if(L_intFLPRN>1||i==0)
						tblREPORT.setValueAt(L_staCOLVL[2],i,3);
					if(i>0)
					{
						tblREPORT.setValueAt(L_staCOLVL[3],i,4);
						tblREPORT.setValueAt(L_staCOLVL[4],i,5);
						tblREPORT.setValueAt(setNumberFormat(L_dblBYRQTY,3),i-L_intROWID,6);
						tblREPORT.setValueAt(setNumberFormat(L_dblBASRT/L_dblBYRQTY,2),i-L_intROWID,7);
						tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL/L_dblBYRQTY,2),i-L_intROWID,11);
						tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL/L_dblBYRQTY,2),i-L_intROWID,12);
						tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL/L_dblBYRQTY,2),i-L_intROWID,13);
						tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL/L_dblBYRQTY,0),i-L_intROWID,10);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,7).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,11).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,12).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,13).toString(),"0.0")),2),i-L_intROWID,9);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,11).toString(),"0.0"))
															+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,12).toString(),"0.0"))
															+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID,13).toString(),"0.0")),2),i-L_intROWID,8);
					}
					else 
					{
						tblREPORT.setValueAt(L_strCOL4VL,i,4);
						tblREPORT.setValueAt(L_staCOLVL[4],i,5);
						L_dblBYRQTY+=Double.parseDouble(L_staCOLVL[6]);
						L_dblBASRT=L_dblBASRT+Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
						L_dblCDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
						L_dblDDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
						L_dblTDCVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
						L_dblCPTVL+=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
						
						tblREPORT.setValueAt( setNumberFormat(L_dblBYRQTY,3),i-L_intROWID+1,6);
						tblREPORT.setValueAt(setNumberFormat(L_dblBASRT/L_dblBYRQTY,2),i-L_intROWID+1,7);
						tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL/L_dblBYRQTY,2),i-L_intROWID+1,11);
						tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL/L_dblBYRQTY,2),i-L_intROWID+1,12);
						tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL/L_dblBYRQTY,2),i-L_intROWID+1,13);
						tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL/L_dblBYRQTY,0),i-L_intROWID+1,10);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,7).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,11).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,12).toString(),"0.0"))
															-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,13).toString(),"0.0")),2),i-L_intROWID+1,9);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,11).toString(),"0.0"))
															+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,12).toString(),"0.0"))
															+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i-L_intROWID+1,13).toString(),"0.0")),2),i-L_intROWID+1,8);
					}
					L_strBYRNM=L_staCOLVL[4];L_strGRDDS=L_staCOLVL[L_intGRDCL-1];
					L_dblBYRQTY=Double.parseDouble(L_staCOLVL[6]);
					L_dblBASRT=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[7]);
					L_dblCDCVL=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[8]);
					L_dblDDCVL=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[9]);
					L_dblTDCVL=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[10]);
					L_dblCPTVL=Double.parseDouble(L_staCOLVL[6])*Double.parseDouble(L_staCOLVL[11]);
				}
				else if(i>0)
					i--;
				L_strGRDDS=L_staCOLVL[L_intGRDCL-1];
			}
			tblREPORT.setRowColor(i,Color.red);
			tblREPORT.setValueAt("Grand Total",i,1);
						tblREPORT.setValueAt(setNumberFormat(L_dblGRTOT,3),i,6);
						tblREPORT.setValueAt(setNumberFormat(L_dblBASRT_GR/L_dblGRTOT,2),i,7);
						tblREPORT.setValueAt(setNumberFormat(L_dblCDCVL_GR/L_dblGRTOT,2),i,11);
						tblREPORT.setValueAt(setNumberFormat(L_dblDDCVL_GR/L_dblGRTOT,2),i,12);
						tblREPORT.setValueAt(setNumberFormat(L_dblTDCVL_GR/L_dblGRTOT,2),i,13);
						tblREPORT.setValueAt(setNumberFormat(L_dblCPTVL_GR/L_dblGRTOT,0),i,10);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,7).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
						-Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,9);
						tblREPORT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,11).toString(),"0.0"))
						+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,12).toString(),"0.0"))
						+Double.parseDouble(nvlSTRVL(tblREPORT.getValueAt(i,13).toString(),"0.0")),2),i,8);
			
			tblREPORT.setValueAt(setNumberFormat(L_dblGRTOT,3),i,6);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equalsIgnoreCase("Screen"))
			add(pnlREPORT,6,1,8,6,this,'L');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equalsIgnoreCase("Print"))
				exePRNRPT();
			setMSG("",'N');
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
		int [] L_inaCOLWD=new int[inaCOLWD_fn.length];
		prnFMTCHR(L_doutREPORT,M_strBOLD);
		prnFMTCHR(L_doutREPORT,M_strCPI10);
		
		L_doutREPORT.writeBytes("SUPREME PETROCHEM LIMITED\n");
		prnFMTCHR(L_doutREPORT,M_strNOBOLD);
		L_doutREPORT.writeBytes("POLYSTYRENE ORDERS BOOKED ON "+M_txtFMDAT.getText()+" \n");
		prnFMTCHR(L_doutREPORT,M_strBOLD);
		prnFMTCHR(L_doutREPORT,M_strCPI17);
		L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
		for(int i=0;i<L_inaCOLWD.length;i++)
		{
			for(int j=0;j<inaCOLWD_fn.length;j++)
			{
				if(L_COLHD[i].equals(staCOLHD_fn[j]))
				{
					L_inaCOLWD[i]=inaCOLWD_fn[j];
					L_doutREPORT.writeBytes(padSTRING('R',L_COLHD[i],L_inaCOLWD[i]));
					break;
				}
			}
		}
		prnFMTCHR(L_doutREPORT,M_strNOBOLD);
		L_doutREPORT.writeBytes("\n");
		L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
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
					if(L_COLHD[i].equals(staCOLHD_fn[j]))
					{
						L_inaCOLWD[i]=inaCOLWD_fn[j];
						break;
					}
				}
			}
			String L_strTEMP="",L_strREPORT="";
			boolean L_flgPRNLN=false;
			File filREPORT=new File(cl_dat.M_strREPSTR_pbst+"mr_qrbrn.doc");
			FileOutputStream filOUT=new FileOutputStream(filREPORT);
			L_doutREPORT=new DataOutputStream(filOUT);
			boolean L_flgTOTAL=false;
			PRNDLYHD();
			for(int i=0;i<tblREPORT.getRowCount();i++)
			{
				for(int j=1;j<tblREPORT.getColumnCount();j++)
				{
					if(tblREPORT.getValueAt(i,j).toString().length()>0)
					{
						L_flgPRNLN=true;
						break;
					}
				}
				for(int j=1;j<tblREPORT.getColumnCount()&&L_flgPRNLN;j++)
				{
					if(tblREPORT.getValueAt(i,j).toString().length()>5)
					{
						if(tblREPORT.getValueAt(i,j).toString().substring(0,5).equalsIgnoreCase("total")
						   ||tblREPORT.getValueAt(i,j).toString().substring(0,5).equalsIgnoreCase("grand"))
						{
							L_strTEMP=tblREPORT.getValueAt(i,j).toString();
								L_strTEMP=padSTRING('R',L_strTEMP,L_inaCOLWD[j]+L_inaCOLWD[j+1]+L_inaCOLWD[j+2]);	
								j++;j++;
								L_flgTOTAL=true;
						}
						else 
						{
							if(j>8)
								L_strTEMP=nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"  -");
							else
								L_strTEMP=nvlSTRVL(tblREPORT.getValueAt(i,j).toString(),"");
							if(L_strTEMP.length()>28)
								L_strTEMP=L_strTEMP.substring(0,27);
							try
							{
								Double.parseDouble(L_strTEMP);
								L_strTEMP=padSTRING('L',L_strTEMP,L_inaCOLWD[j]);	
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
						if(L_strTEMP.length()>28)
							L_strTEMP=L_strTEMP.substring(0,27);
						try
						{//to check whether data is nuemeric
							Double.parseDouble(L_strTEMP);
							L_strTEMP=padSTRING('L',L_strTEMP,L_inaCOLWD[j]);	
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
						prnFMTCHR(L_doutREPORT,M_strBOLD);
					L_doutREPORT.writeBytes(L_strREPORT);
					if(L_flgTOTAL)
					{
						prnFMTCHR(L_doutREPORT,M_strNOBOLD);
						L_doutREPORT.writeBytes("\n");M_intLINNO++;
					}
					L_flgTOTAL=false;
					L_doutREPORT.writeBytes("\n");
					M_intLINNO++;
					if(M_intLINNO>60)
					{
						M_intLINNO=0;
						prnFMTCHR(L_doutREPORT,M_strEJT);
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
				M_strSQLQRY="select IN_MKTTP ,IN_INDNO,a.CMT_CODDS,IN_BYRCD,c.PT_PRTNM CMT_BYRNM,IN_DSRCD,d.PT_PRTNM,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,b.cmt_codds,INT_BASRT,INT_CDCVL/1000 INT_CDCVL,INT_DDCVL/1000 INT_DDCVL,INT_TDCVL/1000 INT_TDCVL, IN_APTVL from VW_INTRN,CO_CDTRN a,CO_CDTRN b,cO_PTMST c,CO_PTMST d where  INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and IN_INDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse("01"+M_txtFMDAT.getText().substring(2)))+"'"
					+" and IN_SALTP=a.CMT_CODCD and a.CMT_CGSTP='MR00SAL'"
					+" and IN_ZONCD=b.CMT_CODCD and b.CMT_CGSTP='MR00ZON'"
					+" and c.PT_PRTTP='C' and IN_BYRCD=c.PT_PRTCD"
					+" and d.PT_PRTTP='D' and IN_DSRCD=d.PT_PRTCD"
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
					if (chkSPCOL(L_staTEMP))
						vtrQRDAT.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]);
				}
				hstMONTOT=new Hashtable<String,String>(20,0.2f);
				getDAILY(hstMONTOT,vtrQRDAT.toArray());//TO TOTALISE AND PUT IN HASH TABLE
					////////////////////////////////////
				//RETRIEVING CARRY FORWARD TOTALS
				M_strSQLQRY="select in_mkttp,int_indno,cmt_saltp,in_byrcd,cmt_byrnm,in_dsrcd,pt_dsrnm,int_prdds,cf_qty,cmt_zonds,int_basrt,int_cdcvl,int_ddcvl,int_tdcvl,in_aptvl from vw_crfwd where  INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_SBSCD1 in "+M_strSBSLS;      //TAKING DATA FROM VIEW
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
					if (chkSPCOL(L_staTEMP))//CHECKING IF THE RECORD FITS IN FILTER CRITERIA
						vtrQRDAT.addElement(L_staTEMP[L_intORDR1-1]+"|"+L_staTEMP[L_intORDR2-1]+"|"+L_staTEMP[L_intORDR3-1]+"|"+L_staTEMP[intBYRCD_fn-1]+"|"+L_staTEMP[L_intORDR4-1]+"|"+L_staTEMP[intINDNO_fn-1]+"|"+L_staTEMP[intINDQT_fn-1]+"|"+L_staTEMP[intBASRT_fn]+"|"+L_staTEMP[intCDCVL_fn]+"|"+L_staTEMP[intDDCVL_fn]+"|"+L_staTEMP[intTDCVL_fn]+"|"+L_staTEMP[intAPTVL_fn]);
				
				}
				L_rstRSSET.close();
				strHEADNG="";
				hstCRFTOT=new Hashtable<String,String>(20,0.2f);
				getDAILY(hstCRFTOT,vtrQRDAT.toArray());//TO TOTALISE AND PUT IN HASH TABLE
				M_intLINNO=0;//To start summary on new oage
				//COPING DATA FROM ALL 3 HASHTABLES TO FILE.
				PRNLIN("ALL INDIA",2);
				L_doutREPORT.writeBytes("\n");
				M_intLINNO++;
				PRNLIN("EXPORT",2);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("NON ACCOUNTABLE",2);
				L_doutREPORT.writeBytes("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
				PRNLIN("ALL INDIA|WEST & CENTRAL",0);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("ALL INDIA|GPPS",0);
				PRNLIN("ALL INDIA|HIPS",0);
				PRNLIN("ALL INDIA|SPECIALITY PS",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("ALL INDIA|NORTH",0);
				PRNLIN("ALL INDIA|EAST",0);
				PRNLIN("ALL INDIA|SOUTH",0);
				PRNLIN("ALL INDIA|WEST",0);
				PRNLIN("ALL INDIA|CENTRAL",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("EXPORT|GPPS",0);
				PRNLIN("EXPORT|HIPS",0);
				PRNLIN("EXPORT|SPECIALITY PS",0);
				L_doutREPORT.writeBytes("\n");M_intLINNO++;
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL",0);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH",0);
				PRNLIN("NON ACCOUNTABLE|GPPS",0);
				PRNLIN("NON ACCOUNTABLE|HIPS",0);
				PRNLIN("NON ACCOUNTABLE|SPECIALITY PS",0);
				PRNLIN("NON ACCOUNTABLE|NORTH",0);
				PRNLIN("NON ACCOUNTABLE|EAST",0);
				PRNLIN("NON ACCOUNTABLE|SOUTH",0);
				PRNLIN("NON ACCOUNTABLE|WEST",0);
				PRNLIN("NON ACCOUNTABLE|CENTRAL",0);
				L_doutREPORT.writeBytes("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - -\n");
				L_doutREPORT.writeBytes("ALL INDIA : \n");M_intLINNO++;M_intLINNO++;
				PRNLIN("ALL INDIA|WEST & CENTRAL|GPPS",1);
				PRNLIN("ALL INDIA|WEST & CENTRAL|HIPS",1);
				PRNLIN("ALL INDIA|WEST & CENTRAL|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|GPPS",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|HIPS",1);
				PRNLIN("ALL INDIA|NORTH, EAST & SOUTH|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|NORTH|GPPS",1);
				PRNLIN("ALL INDIA|NORTH|HIPS",1);
				PRNLIN("ALL INDIA|NORTH|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|EAST|GPPS",1);
				PRNLIN("ALL INDIA|EAST|HIPS",1);
				PRNLIN("ALL INDIA|EAST|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|SOUTH|GPPS",1);
				PRNLIN("ALL INDIA|SOUTH|HIPS",1);
				PRNLIN("ALL INDIA|SOUTH|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|WEST|GPPS",1);
				PRNLIN("ALL INDIA|WEST|HIPS",1);
				PRNLIN("ALL INDIA|WEST|SPECIALITY PS",1);
				PRNLIN("ALL INDIA|CENTRAL|GPPS",1);
				PRNLIN("ALL INDIA|CENTRAL|HIPS",1);
				PRNLIN("ALL INDIA|CENTRAL|SPECIALITY PS",1);
	//NONACOUNTABLE
				L_doutREPORT.writeBytes("NON ACCOUNTABLE : \n");M_intLINNO++;
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST & CENTRAL|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH, EAST & SOUTH|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|NORTH|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|EAST|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|EAST|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|EAST|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|SOUTH|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|WEST|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|WEST|SPECIALITY PS",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|GPPS",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|HIPS",1);
				PRNLIN("NON ACCOUNTABLE|CENTRAL|SPECIALITY PS",1);
			}
			L_doutREPORT.flush();
			L_doutREPORT.close();
			doPRINT(cl_dat.M_strREPSTR_pbst+"mr_qrbrn.doc");
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
					L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
					L_doutREPORT.writeBytes(padSTRING('R',"Catagory",50)+padSTRING('R',"Qty. Booked(Day)",23)+padSTRING('R',"Qty. Booked (Month)",25)+padSTRING('R',"Carry Forward",25)+padSTRING('R',"Cummulative Total",25)+"\n");
					L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
					prnFMTCHR(L_doutREPORT,M_strNOBOLD);
					M_intLINNO=3;
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
				L_doutREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(hstDLYTOT.get(P_strLINKEY).toString(),"0.0"))+Double.parseDouble(nvlSTRVL(hstMONTOT.get(P_strLINKEY).toString(),"0.0"))+Double.parseDouble(nvlSTRVL(hstCRFTOT.get(P_strLINKEY).toString(),"0.0")),3),25)+"\n");
			}
		}catch (Exception e)
		{setMSG(e,"Printing Line"+P_strLINKEY);}
	}
	private void getDAILY(Hashtable<String,String> P_hstRPDAT,Object[] P_obaRPDAT)
	{
		try
		{
			double L_dblTOTAL1=0.0,L_dblTOTAL2=0.0,L_dblTOTAL3=0.0,L_dblTOTWC=0.0,L_dblTOTNES=0.0,
				L_dblINDGP=0.0,L_dblINDHI=0.0,L_dblINDSP=0.0,L_dblNORTH=0.0,L_dblSOUTH=0.0,L_dblEAST=0.0,L_dblCENTRAL=0.0,
				L_dblWEST=0.0,L_dblXPTGP=0.0,L_dblXPTHI=0.0,L_dblXPTSP=0.0,
				L_dblWESTGP=0.0,L_dblWESTHI=0.0,L_dblWESTSP=0.0,
				L_dblEASTGP=0.0,L_dblEASTHI=0.0,L_dblEASTSP=0.0,
				L_dblSOUTHGP=0.0,L_dblSOUTHHI=0.0,L_dblSOUTHSP=0.0,
				L_dblNORTHGP=0.0,L_dblNORTHHI=0.0,L_dblNORTHSP=0.0,
				L_dblCENTRALGP=0.0,L_dblCENTRALHI=0.0,L_dblCENTRALSP=0.0,
				L_dblWCGP=0.0,L_dblWCHI=0.0,L_dblWCSP=0.0,
				L_dblNESGP=0.0,L_dblNESHI=0.0,L_dblNESSP=0.0,L_dblODRQT=0.0;
			double L_dblNACGP=0.0,L_dblNACHI=0.0,L_dblNACSP=0.0,L_dblNORTHNAC=0.0,L_dblSOUTHNAC=0.0,L_dblTOTWCNAC=0.0,L_dblTOTNESNAC=0.0,L_dblEASTNAC=0.0,L_dblCENTRALNAC=0.0,
				L_dblWESTNAC=0.0,L_dblXPTGPNAC=0.0,L_dblXPTHINAC=0.0,L_dblXPTSPNAC=0.0,
				L_dblWESTGPNAC=0.0,L_dblWESTHINAC=0.0,L_dblWESTSPNAC=0.0,
				L_dblEASTGPNAC=0.0,L_dblEASTHINAC=0.0,L_dblEASTSPNAC=0.0,
				L_dblSOUTHGPNAC=0.0,L_dblSOUTHHINAC=0.0,L_dblSOUTHSPNAC=0.0,
				L_dblNORTHGPNAC=0.0,L_dblNORTHHINAC=0.0,L_dblNORTHSPNAC=0.0,
				L_dblCENTRALGPNAC=0.0,L_dblCENTRALHINAC=0.0,L_dblCENTRALSPNAC=0.0,
				L_dblWCGPNAC=0.0,L_dblWCHINAC=0.0,L_dblWCSPNAC=0.0,
				L_dblNESGPNAC=0.0,L_dblNESHINAC=0.0,L_dblNESSPNAC=0.0,L_dblODRQTNAC=0.0;
				StringTokenizer L_stkTEMP=null;
				String[][] L_staDATA=new String[P_obaRPDAT.length][];
				for(int i=0;i<P_obaRPDAT.length;i++)//ALL INDIA DOMESTIC TOTAL
				{
					L_stkTEMP=new StringTokenizer(P_obaRPDAT[i].toString(),"|");
					L_staDATA[i]=new String[L_stkTEMP.countTokens()];
					for(int j=0;j<L_staDATA[i].length;j++)
						L_staDATA[i][j]=L_stkTEMP.nextToken();
				}
				for(int i=0;i<L_staDATA.length;i++)
				{
					L_dblODRQT=Double.parseDouble(L_staDATA[i][6].toString());
					if(L_staDATA[i][1].substring(1).equalsIgnoreCase("Export"))
					{
						L_dblTOTAL2+=L_dblODRQT;
						if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							L_dblXPTGP+=L_dblODRQT;
						else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							L_dblXPTHI+=L_dblODRQT;
						else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							L_dblXPTSP+=L_dblODRQT;
					}
					else if(L_staDATA[i][0].toString().equalsIgnoreCase("Captive Consumption")||L_staDATA[i][0].toString().equalsIgnoreCase("Free Test Sample"))
					{
						L_dblTOTAL3+=L_dblODRQT;
						if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							L_dblNACGP+=L_dblODRQT;
						else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							L_dblNACHI+=L_dblODRQT;
						else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							L_dblNACSP+=L_dblODRQT;
						
						if(L_staDATA[i][1].substring(1).equalsIgnoreCase("North"))
						{
							L_dblNORTH+=L_dblODRQT;
							L_dblTOTNESNAC+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblNORTHSPNAC+=L_dblODRQT;
								L_dblNESSPNAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblNORTHHINAC+=L_dblODRQT;
								L_dblNESHINAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblNORTHGPNAC+=L_dblODRQT;
								L_dblNESGPNAC+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("West"))
						{
							L_dblWESTNAC+=L_dblODRQT;
							L_dblTOTWCNAC+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblWESTSPNAC+=L_dblODRQT;
								L_dblWCSPNAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblWESTHINAC+=L_dblODRQT;
								L_dblWCHINAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblWESTGPNAC+=L_dblODRQT;
								L_dblWCGPNAC+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("South"))
						{
							L_dblSOUTHNAC+=L_dblODRQT;
							L_dblTOTNESNAC+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblSOUTHSPNAC+=L_dblODRQT;
								L_dblNESSPNAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblSOUTHHINAC+=L_dblODRQT;
								L_dblNESHINAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblSOUTHGPNAC+=L_dblODRQT;
								L_dblNESGPNAC+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("EAST"))
						{
							L_dblTOTNESNAC+=L_dblODRQT;
							L_dblEASTNAC+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblEASTSPNAC+=L_dblODRQT;
								L_dblNESSPNAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblEASTHINAC+=L_dblODRQT;
								L_dblNESHINAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblEASTGPNAC+=L_dblODRQT;
								L_dblNESGPNAC+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("CENTRAL"))
						{
							L_dblTOTWCNAC+=L_dblODRQT;
							L_dblCENTRALNAC+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblCENTRALSPNAC+=L_dblODRQT;
								L_dblWCSPNAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblCENTRALHINAC+=L_dblODRQT;
								L_dblWCHINAC+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblCENTRALGPNAC+=L_dblODRQT;
								L_dblWCGPNAC+=L_dblODRQT;
							}
						}
					}
					else
					{
						L_dblTOTAL1+=L_dblODRQT;
						if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							L_dblINDGP+=L_dblODRQT;
						else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							L_dblINDHI+=L_dblODRQT;
						else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							L_dblINDSP+=L_dblODRQT;
						
						if(L_staDATA[i][1].substring(1).equalsIgnoreCase("North"))
						{
							L_dblTOTNES+=L_dblODRQT;
							L_dblNORTH+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblNORTHSP+=L_dblODRQT;
								L_dblNESSP+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblNORTHHI+=L_dblODRQT;
								L_dblNESHI+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblNORTHGP+=L_dblODRQT;
								L_dblNESGP+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("West"))
						{
							L_dblTOTWC+=L_dblODRQT;
							L_dblWEST+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblWESTSP+=L_dblODRQT;
								L_dblWCSP+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblWESTHI+=L_dblODRQT;
								L_dblWCHI+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblWESTGP+=L_dblODRQT;
								L_dblWCGP+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("South"))
						{
							L_dblTOTNES+=L_dblODRQT;
							L_dblSOUTH+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblSOUTHSP+=L_dblODRQT;
								L_dblNESSP+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblSOUTHHI+=L_dblODRQT;
								L_dblNESHI+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblSOUTHGP+=L_dblODRQT;
								L_dblNESGP+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("EAST"))
						{
							L_dblTOTNES+=L_dblODRQT;
							L_dblEAST+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblEASTSP+=L_dblODRQT;
								L_dblNESSP+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblEASTHI+=L_dblODRQT;
								L_dblNESHI+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblEASTGP+=L_dblODRQT;
								L_dblNESGP+=L_dblODRQT;
							}
						}
						else if(L_staDATA[i][1].substring(1).equalsIgnoreCase("CENTRAL"))
						{
							L_dblTOTWC+=L_dblODRQT;
							L_dblCENTRAL+=L_dblODRQT;
							if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SP"))
							{
								L_dblCENTRALSP+=L_dblODRQT;
								L_dblWCSP+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SH"))
							{
								L_dblCENTRALHI+=L_dblODRQT;
								L_dblWCHI+=L_dblODRQT;
							}
							else if(L_staDATA[i][4].substring(0,2).equalsIgnoreCase("SC"))
							{
								L_dblCENTRALGP+=L_dblODRQT;
								L_dblWCGP+=L_dblODRQT;
							}
						}
					}
				}
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
				
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|GPPS",setNumberFormat(L_dblWCGP,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|HIPS",setNumberFormat(L_dblWCHI,3));
				P_hstRPDAT.put("ALL INDIA|WEST & CENTRAL|SPECIALITY PS",setNumberFormat(L_dblWCSP,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|GPPS",setNumberFormat(L_dblNESGP,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|HIPS",setNumberFormat(L_dblNESHI,3));
				P_hstRPDAT.put("ALL INDIA|NORTH, EAST & SOUTH|SPECIALITY PS",setNumberFormat(L_dblNESSP,3));
				
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
				
				//PUTTIN NON ACCOUNTABLE QTY
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH",setNumberFormat(L_dblNORTHNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SOUTH",setNumberFormat(L_dblSOUTHNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST",setNumberFormat(L_dblWESTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|EAST",setNumberFormat(L_dblEASTNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|CENTRAL",setNumberFormat(L_dblCENTRALNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|GPPS",setNumberFormat(L_dblNACGP,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|HIPS",setNumberFormat(L_dblNACHI,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|SPECIALITY PS",setNumberFormat(L_dblNACSP,3));
				
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|GPPS",setNumberFormat(L_dblWCGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|HIPS",setNumberFormat(L_dblWCHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|WEST & CENTRAL|SPECIALITY PS",setNumberFormat(L_dblWCSPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|GPPS",setNumberFormat(L_dblNESGPNAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|HIPS",setNumberFormat(L_dblNESHINAC,3));
				P_hstRPDAT.put("NON ACCOUNTABLE|NORTH, EAST & SOUTH|SPECIALITY PS",setNumberFormat(L_dblNESSPNAC,3));
				
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
//END OF NON ACCOUNTABLE			
				P_hstRPDAT.put("EXPORT|GPPS",setNumberFormat(L_dblXPTGP,3));
				 P_hstRPDAT.put("EXPORT|HIPS",setNumberFormat(L_dblXPTHI,3));
				 P_hstRPDAT.put("EXPORT|SPECIALITY PS",setNumberFormat(L_dblXPTSP,3));
				
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
			StringTokenizer L_stkTEMP=new StringTokenizer(cmbMKTTP.getSelectedItem().toString(),"(");
			L_stkTEMP.nextToken();
			if(!L_stkTEMP.nextToken().substring(0,2).equals(nvlSTRVL(P_staDATVL[intMKTTP_fn-1],"")))
				return false;
			if(rdbRPTSP.isSelected())
			{
				if((nvlSTRVL(P_staDATVL[intSALTP_fn-1],"").equalsIgnoreCase("Stock Transfer"))
				   ||(nvlSTRVL(P_staDATVL[intSALTP_fn-1],"").equalsIgnoreCase("Free Test Sample")))
					return false;
			}       
			if(rdbZONSP.isSelected())
				if(!(nvlSTRVL(P_staDATVL[intZONCD_fn-1],"  ").substring(1).equalsIgnoreCase(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString())))
					return false;
			if(rdbSTPSP.isSelected())
				if(!(nvlSTRVL(P_staDATVL[intSALTP_fn-1],"").equalsIgnoreCase(cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString())))
					return false;
			if(rdbDSTSP.isSelected())
				if(!(new StringTokenizer(nvlSTRVL(P_staDATVL[intDSRCD_fn-1],""),"~").nextToken()).equals(txtDSTNM.getText()))
					return false;
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
	void setENBL(boolean L_STAT)
	{System.out.println("in child");
		super.setENBL(L_STAT);
		if(tblREPORT!=null)
			tblREPORT.setEnabled(false);
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
			return true;
		}catch(Exception e)
		{setMSG(e,"while validating data");return false;}
	}

		private int getROWCNT()
		{
			try
			{
				for(int a=0;a<L_COLHD.length;a++)
				if(L_COLHD[a].equalsIgnoreCase("GRADE"))
				{
					L_intGRDCL=a;
					break;
				}
				boolean L_flgTOTAL=false,L_flgTOTCL1=false,L_flgTOTCL2=false,L_flgTOTCL3=false,L_flgTOTCL4=false;
			//INITIALIZING ABOVE FLAGS
			if(L_COLHD[1].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[1].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[1].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[1].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL1=true;
			if(L_COLHD[2].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[2].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[2].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[2].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL2=true;
			if(L_COLHD[3].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[3].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[3].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[3].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL3=true;
			if(L_COLHD[4].equals(staCOLHD_fn[intZONCD_fn-1])&&chbZONTOT.isSelected()
			   ||L_COLHD[4].equals(staCOLHD_fn[intSALTP_fn-1])&&chbSTPTOT.isSelected()
			   ||L_COLHD[4].equals(staCOLHD_fn[intDSRCD_fn-1])&&chbDSTTOT.isSelected()
			   ||L_COLHD[4].equals(staCOLHD_fn[intPRDDS_fn-1])&&chbGRDTOT.isSelected())
				L_flgTOTCL4=true;
			int L_intFLPRN=0;//FLAG TO INDICATE TOTALS OF HOW MANY COLUMNS ARE TO BE DISPLAYED
					System.out.println("1");	
			String[] L_staCOLVL=new String[12];
			String L_strBYRNM="",L_strZONVL="";
			String L_strCOL1VL="",L_strCOL2VL="",L_strCOL3VL="",L_strCOL4VL="";
			int L_intROWCNT=0,i=0;
			StringTokenizer L_stkTEMP=null;
			for(i=0;i<obaRPDAT.length;i++)
			{
				L_intFLPRN=0;
				
				if(i==obaRPDAT.length-1)//IF END OF REPORT IS ACHEIVED PRINT SUMMERY
				{
					if(!rdbSUMRY.isSelected())
						L_intROWCNT++;
					if(L_flgTOTCL4)
					{//Diplay total of fourth col
						L_intROWCNT++;
						if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
								L_intROWCNT++;
						else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
					}
					if(L_flgTOTCL3)
					{//Diplay total of third col
						L_intROWCNT++;
						if(L_COLHD[3].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							L_intROWCNT++;
						else if(L_intGRDCL==3)//!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							L_intROWCNT++;
					}
					if(L_flgTOTCL2)
					{//Diplay total of second col
						L_intROWCNT++;
						if(L_COLHD[2].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							L_intROWCNT++;
						else if(L_intGRDCL==2)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							L_intROWCNT++;
					}
					if(L_flgTOTCL1)
					{//Diplay total of first col
						L_intROWCNT++;
						if(L_COLHD[1].equals(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							L_intROWCNT++;
						else if(L_intGRDCL==1)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
							L_intROWCNT++;
					}
					break;
				}
				L_stkTEMP=new StringTokenizer(obaRPDAT[++i].toString(),"|");
				for(int z=0;z<L_staCOLVL.length;z++)
				{//putting values for this row in a string array
					L_staCOLVL[z]=L_stkTEMP.nextToken().replace('~',' ');
					if(z<L_COLHD.length-1)
					if(L_COLHD[z+1].equals(staCOLHD_fn[7]))
					{//removing digit attached b4 zone
						L_staCOLVL[z]=L_staCOLVL[z].substring(1);
					}
				}
				if(i==0)
				{
					L_strCOL1VL=L_staCOLVL[0];L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];
				}
				else
				{
								//DISPLAYING GP/HI/SPS TOTAL
					if(!L_strCOL1VL.equalsIgnoreCase(L_staCOLVL[0]))
					{//if value for first col is NOT as same as previous row
						if(L_flgTOTCL4)
						{//display total for fourth col
							L_intROWCNT++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
								L_intROWCNT++;
							else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						if(L_flgTOTCL3)
						{//display total of second col
							L_intROWCNT++;
							if(L_COLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
								L_intROWCNT++;
							else if(L_intGRDCL==3)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						if(L_flgTOTCL2)
						{//displa total of second col
							L_intROWCNT++;
							if(L_COLHD[2].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
								L_intROWCNT++;
							else if(L_intGRDCL==2)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						if(L_flgTOTCL1)
						{//display total of fist col
							L_intROWCNT++;
							if(L_COLHD[1].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[0].equalsIgnoreCase("West")&&!L_staCOLVL[0].equalsIgnoreCase("Central")))
									L_intROWCNT++;
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[0].equalsIgnoreCase("North")&&!L_staCOLVL[0].equalsIgnoreCase("South")&&!L_staCOLVL[0].equalsIgnoreCase("East")))
									L_intROWCNT++;
								else if(L_strZONVL.equalsIgnoreCase("Export"))
									L_intROWCNT++;
							}
							else if(L_intGRDCL==1&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						L_strCOL1VL=L_staCOLVL[0];L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];//copy current values as previous values for next row
						int z=0;
						for(z=0;z<L_staCOLVL.length;z++)//retrieve colindex for zone
							if(L_COLHD[z].equalsIgnoreCase(staCOLHD_fn[7]))
							   break;
						L_strZONVL=L_staCOLVL[z-1];
						L_intFLPRN=4;
					}
			//TOTALISING SECOND COL
					else if(!L_strCOL2VL.equalsIgnoreCase(L_staCOLVL[1]))
					{
						if(L_flgTOTCL4)
						{
							L_intROWCNT++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
								L_intROWCNT++;
							else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						if(L_flgTOTCL3)
						{
							L_intROWCNT++;
							if(L_COLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
								L_intROWCNT++;
							else if(L_intGRDCL==3)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						if(L_flgTOTCL2)
						{
							L_intROWCNT++;
							if(L_COLHD[2].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[1].equalsIgnoreCase("West")&&!L_staCOLVL[1].equalsIgnoreCase("Central")))
									L_intROWCNT++;
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(L_staCOLVL[1].equalsIgnoreCase("North")&&!L_staCOLVL[1].equalsIgnoreCase("South")&&!L_staCOLVL[1].equalsIgnoreCase("East")))
									L_intROWCNT++;
								else if(L_strZONVL.equalsIgnoreCase("Export"))
									L_intROWCNT++;
							}
							else if(L_intGRDCL==2&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						L_strCOL2VL=L_staCOLVL[1];L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];
						L_intFLPRN=3;	
					}
//END OF SECOND COL TOTAL
//TOTALISING THIRD COL
					else if(!L_strCOL3VL.equalsIgnoreCase(L_staCOLVL[2]))
					{
						if(L_flgTOTCL4)
						{
							L_intROWCNT++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
								L_intROWCNT++;
							else if(L_intGRDCL==4)//&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						if(L_flgTOTCL3)
						{
							L_intROWCNT++;
							if(L_COLHD[3].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[2].equalsIgnoreCase("West")&&!L_staCOLVL[2].equalsIgnoreCase("Central")))
									L_intROWCNT++;
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[2].equalsIgnoreCase("North")&&!L_staCOLVL[2].equalsIgnoreCase("South")&&!L_staCOLVL[2].equalsIgnoreCase("East")))
									L_intROWCNT++;
								else if(L_strZONVL.equalsIgnoreCase("Export"))
									L_intROWCNT++;
							}
							else if(L_intGRDCL==3&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						L_strCOL3VL=L_staCOLVL[2];L_strCOL4VL=L_staCOLVL[3];
						L_intFLPRN=2;
					}
//END OF THIRD COL TOTAL
//TOTALISING FOURTH COL
					else if(!L_strCOL4VL.equalsIgnoreCase(L_staCOLVL[3]))
					{
						if(L_flgTOTCL4)
						{
							L_intROWCNT++;
							if(L_COLHD[4].equalsIgnoreCase(staCOLHD_fn[intZONCD_fn-1])&&chbRGNWS.isSelected())
							{
								if((L_strZONVL.equalsIgnoreCase("west")||L_strZONVL.equalsIgnoreCase("central"))
								   &&(!L_staCOLVL[3].equalsIgnoreCase("West")&&!L_staCOLVL[3].equalsIgnoreCase("Central")))
									L_intROWCNT++;
								else if((L_strZONVL.equalsIgnoreCase("North")||L_strZONVL.equalsIgnoreCase("South")||L_strZONVL.equalsIgnoreCase("East"))
										&&(!L_staCOLVL[3].equalsIgnoreCase("North")&&!L_staCOLVL[3].equalsIgnoreCase("South")&&!L_staCOLVL[3].equalsIgnoreCase("East")))
									L_intROWCNT++;
								else if(L_strZONVL.equalsIgnoreCase("Export"))
									L_intROWCNT++;
							}
							else if(L_intGRDCL==4&&!L_strGRDDS.substring(0,2).equals(L_staCOLVL[L_intGRDCL-1].substring(0,2)))
								L_intROWCNT++;
						}
						L_strCOL4VL=L_staCOLVL[3];
						L_intFLPRN=1;
					}
//END OF FOURTH COL TOTAL
				}
				if(L_strBYRNM.equals(L_staCOLVL[4])&&L_strGRDDS.equals(L_staCOLVL[L_intGRDCL-1]))
				{
					
				}
				int z=0;
				for(z=0;z<L_staCOLVL.length;z++)
					if(L_COLHD[z].equalsIgnoreCase(staCOLHD_fn[7]))
					   break;
				L_strZONVL=L_staCOLVL[z-1];
				if((!rdbSUMRY.isSelected())
					&&((!L_strBYRNM.equals(L_staCOLVL[4]))||(!L_strGRDDS.equals(L_staCOLVL[L_intGRDCL-1]))))//SHOW DETIALS IF DETILS ARE SELECTED
				{
					if(i>0)
					{
//						L_intROWCNT++;
					}
					else 
					{
//						L_intROWCNT++;
					}
					L_strBYRNM=L_staCOLVL[4];L_strGRDDS=L_staCOLVL[L_intGRDCL-1];
				}
//				else if(i>0)
//					i--;
				L_strGRDDS=L_staCOLVL[L_intGRDCL-1];
			}
			System.out.println("retutrning");
			return L_intROWCNT;			
		}catch (Exception e)
			{setMSG(e,"Totalising count");
			return 0;}
			
		}
		
}