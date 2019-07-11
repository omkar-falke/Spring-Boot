/*
	Name			: Indent Query
	System			: MKT
	Author			: AAP
	Version			: v2.0.0
	Last Modified	: 20/05/2004
	Documented On	: 20/05/2004
*/
import javax.swing.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent; import java.awt.Color;
import java.util.Hashtable;import java.util.Vector;import java.util.StringTokenizer;
import java.awt.Component;import java.awt.Dimension;import java.sql.ResultSet;
import javax.swing.event.ChangeEvent;import javax.swing.event.ChangeListener;
/**<BODY><P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>    Indent Query</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                  Form to retirieve details of                  orders booked with filter                  criteria defined by user&nbsp;&amp;       see D.O. and invoice&nbsp;details of an indent </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>                New concept&nbsp;      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mr_qrind.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mr_qrind.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>13/05/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><P><FONT color=purple><STRONG>   </STRONG></FONT>&nbsp;</P><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD> Series used : COXXMKT,MR00SAL </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_PTMST</TD>    <TD>Party type : Customer</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_PRMST</TD>    <TD>For grade description</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_INMST</TD>    <TD>IN_SALTP,IN_INDNO</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_INTRN</TD>    <TD>INT_SALTP,INT_INDNO,INT_PRDCD</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_DOTRN</TD>    <TD>DOT_INDNO,DO_DORNO</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_IVTRN</TD>    <TD>IVT_INDNO</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE><P><FONT color=purple><STRONG>Features : </STRONG></FONT></P><UL>  <LI>Indent can be searched on :</LI>  <UL>    <LI>    <DIV align=left>Market Type</DIV></LI>    <LI>   <DIV align=left>Sale Type (one or many</DIV></LI>    <LI>    <DIV align=left>Distributor (one or many)</DIV></LI>    <LI>    <DIV align=left>Grade (one or many)</DIV></LI>    <LI>    <DIV align=left>Buyer ( one or many)</DIV></LI>    <LI>    <DIV align=left>Perticular indent no.</DIV></LI>    <LI>    <DIV align=left>Perticular customer group</DIV></LI>    <LI>    <DIV align=left>Indent date from to</DIV></LI></UL>  <LI>  <DIV align=left>After selecting the criteria to search and clicking on "Run",   details will be displayed in the first tab with indent and grade wise   totalised fig. of INDQT, INVQT, DORQT and pending qty. Overall totals are   displayed at the end.</DIV></LI>  <LI>  <DIV align=left>To see details of&nbsp;D.O. angainst perticular indent, select   that indent and go to D.O. details tab.</DIV></LI>  <LI>  <DIV align=left>To see details of invoices against an indent or D.O., select   that indent or D.O. and click on Invoice details tab. This will give lorry   no.and L.A. No. also.</DIV></LI>  <LI>  <DIV align=left>Dynamic creation of tab panes is used in this form.</DIV></LI></UL></BODY>*/
class mr_qrind extends cl_rbase implements ChangeListener
{
	/** Combo for market type  */
	private cl_Combo cmbMKTTP;/**Combo for Buyer Name */
	private cl_Combo cmbBYRNM;/** Combo for Distributor Name*/
	private cl_Combo cmbDSRNM;/** Combo for Grade Description*/
	private cl_Combo cmbGRDDS;/** From Indent No*/
	private JTextField txtINDNO;/** Group Code*/
	private JTextField txtGRPCD;/** From Indent Date*/
	private JTextField txtFMDAT;/** To Indent Date*/
	private JTextField txtTODAT;/** List of Sale Types*/
	private JList lstSALDS;/** Panel for Indent Details*/
	private JPanel pnlINDTL;/**Panel for D.O. Details */
	private JPanel pnlDODTL;/**Panel for Invoice Details */
	private JPanel pnlIVDTL;/** Table for Indent Details*/
	private JPanel pnlPRDTL;/** Table for Indent Details*/
	private cl_JTable tblINDTL;/** Table for D.O. Details*/
	private cl_JTable tblDODTL;/** Table for Invoice Details*/
	private cl_JTable tblIVDTL;/** Vector for sale Type Description*/
//	private cl_JTable tblPRDTL;/** Vector for sale Type Description*/
	private Vector<String> vtrSALDS;/** Vector for sale Type Codes*/
	private Vector<String> vtrSALCD;/** Vector for group code*/
	private Vector<String> vtrGRPCD;/** Tabbed pane*/
	private JTabbedPane tbpMAIN;/**Grade Description */
	private String strGRDDS;/**Flag to indicate whether to show realisation values. At present, hard coded for systems user */
	private boolean flgSHWRN;/**Column indexes in tables */
	private final int intINDNO_fn=1,intINDDT_fn=2,intBYRNM_fn=3,intGRDDS_fn=4,intINDQT_fn=5,intDORQT_fn=6,intINVQT_fn=7,intPDGQT_fn=8,intBASRT_fn=9;
	/**
	 * To construct the form
	 * 
	 * To construct the form
	 * 
	 * <p>Lays the compoenets. Registers input verifiers. <br>Populates cmbMKTTP and Sale type list
	 */
	mr_qrind()
	{
		super(2);
		try
		{
			pnlINDTL=new JPanel(null);
			pnlDODTL=new JPanel(null);
			pnlIVDTL=new JPanel(null);
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			M_cmbDESTN.setVisible(false);M_lblDESTN.setVisible(false);M_txtFMDAT.setVisible(false);
			M_lblFMDAT.setVisible(false);M_txtTODAT.setVisible(false);M_lblTODAT.setVisible(false);
			M_vtrSCCOMP.remove(M_cmbDESTN);M_vtrSCCOMP.remove(M_lblDESTN);M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_lblFMDAT);M_vtrSCCOMP.remove(M_txtTODAT);M_vtrSCCOMP.remove(M_lblTODAT);
			setMatrix(20,9);
			add(new JLabel("Market Type"),1,1,1,1,this,'L');
			add(cmbMKTTP=new cl_Combo(),1,2,1,2,this,'L');
			add(new JLabel("Distributor"),1,4,1,1,this,'L');
			add(cmbDSRNM=new cl_Combo(),1,5,1,2,this,'L');
			add(new JLabel("Sale Type"),1,7,1,1,this,'L');
//			add(new JScrollPane(),1,8,4,2,this,'L');
			
			add(new JLabel("Grade"),2,1,1,1,this,'L');
			add(cmbGRDDS=new cl_Combo(),2,2,1,2,this,'L');
			add(new JLabel("Buyer"),2,4,1,1,this,'L');
			add(cmbBYRNM=new cl_Combo(),2,5,1,2,this,'L');
			
			add(new JLabel("Indent No."),3,1,1,1,this,'L');
			add(txtINDNO=new JTextField(),3,2,1,2,this,'L');

			add(new JLabel("Group Code"),3,4,1,1,this,'L');
			add(txtGRPCD=new JTextField(),3,5,1,2,this,'L');
			
			add(new JLabel("Ind.Date From"),4,1,1,1,this,'L');
			add(txtFMDAT=new TxtDate(),4,2,1,2,this,'L');
			add(new JLabel("Ind.Date To"),4,4,1,1,this,'L');
			add(txtTODAT=new TxtDate(),4,5,1,2,this,'L');
						
			add(tbpMAIN=new JTabbedPane(),6,1,13,9,this,'L');
			tbpMAIN.addChangeListener(this);
			tbpMAIN.add("Indent Details", pnlINDTL);
			tbpMAIN.add("D.O. Details", pnlDODTL);
			tbpMAIN.add("Invoice Details", pnlIVDTL);
//			tbpMAIN.add("Dispatch Details", pnlPRDTL=new JPanel(null));
			vtrSALDS=new Vector<String>(10,2);vtrSALCD=new Vector<String>(10,2);
//RETRIEVING MARKET TYPE AND SALE TYPE DETAILS.			
			M_strSQLQRY = "Select CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN where (CMT_CGMTP='MST' and"
				+ " CMT_CGSTP = 'COXXMKT' ) or(CMT_CGMTP ='SYS' and "
				+" CMT_CGSTP='MR00SAL' )"
				+"order by CMT_CGSTP,CMT_CODCD";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("CMT_CGSTP").equalsIgnoreCase("COXXMKT"))
						cmbMKTTP.addItem(M_rstRSSET.getString("CMT_CODDS"),M_rstRSSET.getString("CMT_CODCD"));
					else if(M_rstRSSET.getString("CMT_CGSTP").equalsIgnoreCase("MR00SAL"))
					{
						vtrSALCD.addElement(M_rstRSSET.getString("CMT_CODCD"));
						vtrSALDS.addElement(M_rstRSSET.getString("CMT_CODDS"));
					}
				}
				M_rstRSSET.close();
				lstSALDS=new JList(vtrSALDS);
				add(new JScrollPane(lstSALDS),1,8,4,2,this,'L');
			}
		//REGISTERING INPUT VERIFIER
			INPVF oINPVF=new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(oINPVF);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
					{
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
					}
				}
				else
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		

			
			if(cl_dat.M_strUSRCD_pbst.equals("SYS"))
				flgSHWRN=true;
			else
				flgSHWRN=false;

			
			
			if(flgSHWRN)//IF REALISATION VALUE IS TO BE SHOWN
				tblINDTL=crtTBLPNL1(pnlINDTL,new String[]{"DTL","Indent No","Ind. Date","Buyer","Grade","Ind. Qty.","D.O. Qty.","Inv. Qty.","Pdg. Qty.","Rate/MT","Rsl. Rate"},500,1,1,11,8.9,new int[]{50,75,75,150,80,75,75,75,75,75,75},new int[]{0});
			else
				tblINDTL=crtTBLPNL1(pnlINDTL,new String[]{"DTL","Indent No","Ind. Date","Buyer","Grade","Ind. Qty.","D.O. Qty.","Inv. Qty.","Pdg. Qty.","Rate/MT"},5000,1,1,11,8.9,new int[]{50,75,75,150,80,75,75,75,75,75},new int[]{0});
			tblINDTL.setInputVerifier(new TINPVF());
			
			tblDODTL=crtTBLPNL1(pnlDODTL,new String[]{"DTL","D.O. No","D.O. Date","Transporter","D.O. Qty.","L.A. Qty.","Inv. Qty.","Fright Rate/MT","Status"},100,1,1,5,8.9,new int[]{50,75,75,150,80,75,75,75,75},new int[]{0});
			tblDODTL.setInputVerifier(new TINPVF());

			tblIVDTL=crtTBLPNL1(pnlIVDTL,new String[]{"DTL","Inv. No","Inv. Date","Inv. Qty.","L.A. Qty.","Lorry No.","L.A. No."},100,1,1,11,8.9,new int[]{50,75,75,100,80,75,75},new int[]{0});
			tblIVDTL.setInputVerifier(new TINPVF());
			
		}catch(Exception e)
		{setMSG(e,"Child.Constructor");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**
	 * <b>TASKS : </b><br>
	 * cmbOPTN : if selected index >0, transfer focus to cmbMKTTP.
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				cmbMKTTP.requestFocus();
		}catch(Exception e)
		{setMSG(e,"Child.actionPerformed");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	/**
	 * <b>TASKS</b><BR>
	 * cmbBYRNM : Help on Buyer names CO_PTMST where PT_PRTTP='C'
	 * cmbDSRNM : Help on Distributor names from CO_PTMST where PT_PRTTP='D'
	 * cmbGRDDS : Help on Grade descriptions from CO_PRMST
	 * cmbGRPCD : Help on Group Codes from CO_PTMST where PT_PRTTP='C'
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				if(M_objSOURC==cmbBYRNM)
				{
					M_strHLPFLD = "cmbBYRNM";
					cl_hlp("Select PT_PRTNM,PT_PRTCD from CO_PTMST where PT_PRTTP='C' order by PT_PRTNM" ,1,1,new String[] {"Buyer","Code"},2,"CT");
				}
				else if(M_objSOURC==cmbDSRNM)
				{
					M_strHLPFLD = "cmbDSRNM";
					cl_hlp("Select PT_PRTNM,PT_PRTCD from CO_PTMST where PT_PRTTP='D' order by PT_PRTNM" ,1,1,new String[] {"Buyer","Code"},2,"CT");
				}
				else if(M_objSOURC==cmbGRDDS)
				{
					M_strHLPFLD = "cmbGRDDS";
					cl_hlp("Select PR_PRDDS,PR_PRDCD from CO_PRMST order by PR_PRDDS" ,1,1,new String[] {"Buyer","Code"},2,"CT");
				}
				else if(M_objSOURC==txtGRPCD)
				{
					M_strHLPFLD = "txtGRPCD";
					cl_hlp("Select distinct PT_GRPCD from CO_PTMST order by PT_GRPCD" ,1,1,new String[] {"Group Code"},1,"CT");
				}
			}
			else if(L_KE.getKeyCode()==L_KE.VK_ENTER)
			{
				if(M_objSOURC==cmbDSRNM)
					cmbGRDDS.requestFocus();
				else
					((JComponent)M_objSOURC).transferFocus();
			}
		}catch(Exception e)
		{setMSG(e,"Child.KeyPressed");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	void exeHLPOK()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			cl_dat.M_wndHLP_pbst=null;
			StringTokenizer L_stkTEMP=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("cmbBYRNM"))
			{
				cmbBYRNM.addItem(L_stkTEMP.nextToken(),L_stkTEMP.nextToken());
				cmbBYRNM.requestFocus();
			}
			else if(M_strHLPFLD.equals("cmbDSRNM"))
			{
				cmbDSRNM.addItem(L_stkTEMP.nextToken(),L_stkTEMP.nextToken());
				cmbDSRNM.requestFocus();
			}
			
			else if(M_strHLPFLD.equals("cmbGRDDS"))
			{
				cmbGRDDS.addItem(L_stkTEMP.nextToken(),L_stkTEMP.nextToken());
				cmbGRDDS.requestFocus();
			}
			
			else if(M_strHLPFLD.equals("txtGRPCD"))
				txtGRPCD.setText(L_stkTEMP.nextToken());
		}catch(Exception e)
		{setMSG(e,"Child.KeyPressed");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**
	 * To clear contents of the screen
	 */
	void clrCOMP()
	{
		super.clrCOMP();
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			cmbBYRNM.removeAllItems();
			cmbDSRNM.removeAllItems();
			cmbGRDDS.removeAllItems();
			lstSALDS.clearSelection();
		}catch(Exception e)
		{setMSG(e,"Child.clrCOMP");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/*
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
		}catch(Exception e)
		{setMSG(e,"Child.KeyPressed");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	*/
	/**To validate data
	 * <p>Group Code and buyer name cannot be searched at a time
	 */
	boolean vldDATA()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(txtGRPCD.getText().length()>0 && cmbBYRNM.getItemCount()>0)
			{
				setMSG("Group Code and Buyer Name cannot be searched commonly ..",'E');
				return false;
			}
			return true;
		}catch(Exception e)
		{setMSG(e,"Child.vldDATA");
		 return false;}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**
	 * To construct where part of the query. 
	 * 
	 * Checksfor search criteria adde by user, converts it into SQL format
	 */
	private String crtQURY()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strRTNVL=null;
			L_strRTNVL=" where IN_MKTTP='"+cmbMKTTP.getITMCD().toString()+"' ";
			int[] L_inaSALDS=lstSALDS.getSelectedIndices();
			if(L_inaSALDS.length>0)
			{
				if(L_inaSALDS.length==1)
					L_strRTNVL+=" and IN_SALTP='"+vtrSALCD.elementAt(L_inaSALDS[0]).toString()+"' ";
				else
				{
					L_strRTNVL+=" and IN_SALTP in(";
					for(int i=0;i<L_inaSALDS.length;i++)
						L_strRTNVL+="'"+vtrSALCD.elementAt(L_inaSALDS[i]).toString()+"',";
					L_strRTNVL=L_strRTNVL.substring(0,L_strRTNVL.length()-1)+") ";
				}
			}
			if(cmbDSRNM.getItemCount()>0)
			{
				if(cmbDSRNM.getItemCount()==1)
					L_strRTNVL+=" and IN_DSRCD = '"+cmbDSRNM.getITMCD().toString()+"' ";
				else
				{
					L_strRTNVL+=" and IN_DSRCD in(";
					for(int i=0;i<cmbDSRNM.getItemCount();i++)
						L_strRTNVL+="'"+cmbDSRNM.getITMCDAt(i).toString()+"',";
					L_strRTNVL=L_strRTNVL.substring(0,L_strRTNVL.length()-1)+") ";
				}
			}
			if(cmbBYRNM.getItemCount()>0)
			{
				if(cmbBYRNM.getItemCount()==1)
					L_strRTNVL+=" and IN_BYRCD = '"+cmbBYRNM.getITMCD().toString()+"' ";
				else
				{
					L_strRTNVL+=" and IN_BYRCD in(";
					for(int i=0;i<cmbBYRNM.getItemCount();i++)
						L_strRTNVL+="'"+cmbBYRNM.getITMCDAt(i).toString()+"',";
					L_strRTNVL=L_strRTNVL.substring(0,L_strRTNVL.length()-1)+") ";
				}
			}
			if(cmbGRDDS.getItemCount()>0)
			{
				if(cmbGRDDS.getItemCount()==1)
					L_strRTNVL+=" and INT_PRDCD = '"+cmbGRDDS.getITMCD().toString()+"' ";
				else
				{
					L_strRTNVL+=" and INT_PRDCD in(";
					for(int i=0;i<cmbGRDDS.getItemCount();i++)
						L_strRTNVL+="'"+cmbGRDDS.getITMCDAt(i).toString()+"',";
					L_strRTNVL=L_strRTNVL.substring(0,L_strRTNVL.length()-1)+") ";
				}
			}
			if(txtGRPCD.getText().length()>0)
			{
				if(vtrGRPCD.size()==1)
					L_strRTNVL+=" and IN_BYRCD='"+vtrGRPCD.elementAt(0).toString()+"' ";
				else
				{
					L_strRTNVL+=" and IN_BYRCD in(";
					for(int i=0;i<vtrGRPCD.size();i++)
						L_strRTNVL+="'"+vtrGRPCD.elementAt(i).toString()+"',";
					L_strRTNVL=L_strRTNVL.substring(0,L_strRTNVL.length()-1)+") ";
				}
			}
			if(txtINDNO.getText().length()>0)
				L_strRTNVL+=" and IN_INDNO='"+txtINDNO.getText()+"' ";
			if(txtFMDAT.getText().length()>0)
				L_strRTNVL+=" and IN_INDDT between '"+
							M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and  '"+
							M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
			return L_strRTNVL;
		}catch(Exception e)
		{setMSG(e,"Child.crtQURY");
		 return null;}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**
	 * To generate the report
	 */
	void exePRINT()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			tblINDTL.clrTABLE();
			tblDODTL.clrTABLE();
			tblIVDTL.clrTABLE();
			if(vldDATA())
			{
				M_strSQLQRY = "Select * from MR_INMST,MR_INTRN,CO_PTMST "+crtQURY()+"  and IN_CMPCD=INT_CMPCD and IN_MKTTP = INT_MKTTP and IN_INDNO=INT_INDNO and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_SBSCD1 in "+M_strSBSLS+" and PT_PRTCD=IN_BYRCD and PT_PRTTP='C'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				Hashtable<String,String> L_hstTEMP=new Hashtable<String,String>(50,0.5f);
				int i=0;
				while(M_rstRSSET.next())
				{//PUT THE ROW DATA IN HASHTABLE
					L_hstTEMP.put(Integer.toString(i++),
									nvlSTRVL(M_rstRSSET.getString("IN_INDNO")," ")+"|"+
									M_fmtLCDAT.format(M_rstRSSET.getDate("IN_INDDT"))+"|"+
									nvlSTRVL(M_rstRSSET.getString("PT_PRTNM")," ")+"|"+
									nvlSTRVL(M_rstRSSET.getString("INT_PRDDS")," ")+"|"+
									nvlSTRVL(M_rstRSSET.getString("INT_INDQT")," ")+"|"+
									nvlSTRVL(M_rstRSSET.getString("INT_DORQT")," ")+"|"+
									nvlSTRVL(M_rstRSSET.getString("INT_INVQT")," ")+"|"+
									setNumberFormat(M_rstRSSET.getFloat("INT_INDQT")-Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("INT_FCMQT"),"0"))-M_rstRSSET.getFloat("INT_DORQT"),3)+"|"+
									nvlSTRVL(M_rstRSSET.getString("INT_BASRT")," ")+"|"+
									((flgSHWRN&&M_rstRSSET.getFloat("INT_RSNVL")>0)==true ? setNumberFormat(M_rstRSSET.getFloat("INT_RSNVL")/(M_rstRSSET.getFloat("INT_INDQT")-Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("INT_FCMQT"),"0"))),2)+"|" : " |")
									);
				}
				StringTokenizer L_stkTEMP=null;
				String L_strTEMP=null;//TO STORE VALUE TO BE DISPLAYED
				boolean L_flgPRINT=false;//FLAG TO INDICATE WHETHER VALUE IS TO BE DISPALYED IN TABLE
				float L_fltINDQT=0.0f,L_fltDORQT=0.0f,L_fltINVQT=0.0f,L_fltPDGQT=0.0f,L_fltBASRT=0.0f;//COLUMN WISE TOTALS
				for(i=0;i<L_hstTEMP.size();i++)
				{//TO DISPLAY VALUES FROM HASH TABLE INTO THE TABLE
					L_stkTEMP=new StringTokenizer((String)L_hstTEMP.get(Integer.toString(i)),"|");
					for(int z=1;z<tblINDTL.getColumnCount();z++)
					{
						L_strTEMP=L_stkTEMP.nextToken();
						L_flgPRINT=true;
			/*			if(z==1 || z==4)
						for(int p=z-1;p>=0;p--)
						{
//							if(tblINDTL.getValueAt(p,z)!=null)
							if(tblINDTL.getValueAt(p,z).toString().length()>0)
							{
								if(tblINDTL.getValueAt(p,z).equals(L_strTEMP))
								{
									L_flgPRINT=false;
									break;
								}
								else
									break;
							}
						}
			*/			if(i==0 || L_flgPRINT)
							tblINDTL.setValueAt(L_strTEMP,i,z);
					}
					//CALCULATING TOTALS
					L_fltINDQT+=Float.parseFloat(nvlSTRVL(tblINDTL.getValueAt(i,intINDQT_fn).toString(),"0.0"));
					L_fltDORQT+=Float.parseFloat(nvlSTRVL(tblINDTL.getValueAt(i,intDORQT_fn).toString(),"0.0"));
					L_fltINVQT+=Float.parseFloat(nvlSTRVL(tblINDTL.getValueAt(i,intINVQT_fn).toString(),"0.0"));
					L_fltBASRT+=(Float.parseFloat(nvlSTRVL(tblINDTL.getValueAt(i,intBASRT_fn).toString(),"0.0"))*Float.parseFloat(nvlSTRVL(tblINDTL.getValueAt(i,intINDQT_fn).toString(),"0.0")));
				}
//				tbpMAIN.add("Indent Details", pnlINDTL);
				tblINDTL.setRowColor(L_hstTEMP.size(),Color.red);
				tblINDTL.setValueAt(setNumberFormat(L_fltINDQT,3),L_hstTEMP.size(),intINDQT_fn);
				tblINDTL.setValueAt(setNumberFormat(L_fltDORQT,3),L_hstTEMP.size(),intDORQT_fn);
				tblINDTL.setValueAt(setNumberFormat(L_fltINVQT,3),L_hstTEMP.size(),intINVQT_fn);
				tblINDTL.setValueAt(setNumberFormat(L_fltBASRT/L_fltINDQT,3),L_hstTEMP.size(),intBASRT_fn);
				updateUI();
			}
		}catch(Exception e)
		{setMSG(e,"Child.ExePRINT");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(input instanceof JTextField && ((JTextField)input).getText().length()==0)
					return true;
				if(input==txtGRPCD)
				{
					txtGRPCD.setText(txtGRPCD.getText().toUpperCase());
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY3("Select Distinct PT_PRTCD from CO_PTMST where PT_GRPCD='"+txtGRPCD.getText()+"'");
					if(L_rstRSSET!=null)
					{//PUT PARTIES CORRESPONDING TO THE GROUP CODE INTO VECTOR
						vtrGRPCD=new Vector<String>(10,2);
						while(L_rstRSSET.next())
							vtrGRPCD.addElement(L_rstRSSET.getString("PT_PRTCD"));
					}
					else
					{
						setMSG("Invalid Group Code",'E');
						return false;
					}
					if(vtrGRPCD.size()==0)
					{
						setMSG("Invalid Group Code",'E');
						return false;
					}
				}
				else if(input==txtINDNO)
				{
					txtINDNO.setText(txtINDNO.getText().toUpperCase());
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY3("select * from VW_INTRN WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP = '"+cmbMKTTP.getITMCD().toString()+"' and IN_INDNO='"+txtINDNO.getText()+"' and INT_SBSCD1 in "+M_strSBSLS);
					if(L_rstRSSET!=null)
					{
						if(!L_rstRSSET.next())
						{
							setMSG("Invalid Order Number ..",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Order Number ..",'E');
						return false;
					}
				}
				return true;
			}catch(Exception e)
			{setMSG(e,"Child.INPVF");
			 return false;}
		}
	}
	
	private class TINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID==0)
				{
					if(tbpMAIN.getSelectedIndex()==0)
					{//TO ENSURE THAT,ONLY ONE ROW ISSELECTED AT A TIME
						for(int i=0;i<tblINDTL.getRowCount();i++)
							if(i!=P_intROWID)
								tblINDTL.setValueAt(Boolean.FALSE,i,0);
					}
					else if(tbpMAIN.getSelectedIndex()==1)
					{//TO ENSURE THAT,ONLY ONE ROW ISSELECTED AT A TIME
						for(int i=0;i<tblDODTL.getRowCount();i++)
							if(i!=P_intROWID)
								tblDODTL.setValueAt(Boolean.FALSE,i,0);
					}
					else if(tbpMAIN.getSelectedIndex()==2)
					{//TO ENSURE THAT,ONLY ONE ROW ISSELECTED AT A TIME
						for(int i=0;i<tblIVDTL.getRowCount();i++)
							if(i!=P_intROWID)
								tblIVDTL.setValueAt(Boolean.FALSE,i,0);
					}
				}
				return true;
			}catch(Exception e)
			{setMSG(e,"Child.TableInputVerifier");
			return false;}
		}
	}
	/**To Populate the tables on tabbed pane selection change	 */
	public void stateChanged(ChangeEvent L_CE)
	{
		try
		{
			setMSG("",'N');
			ResultSet L_rstRSSET=null;
			String strSQLQRY="";
			if(tbpMAIN.getSelectedIndex()==1)
			{
				setMSG("",'N');
				tblDODTL.clrTABLE();
				tblIVDTL.clrTABLE();
				int L_intROWID=0;
				for(L_intROWID=0;L_intROWID<=tblINDTL.getRowCount();L_intROWID++)
					if(tblINDTL.getValueAt(L_intROWID,0).equals(Boolean.TRUE))
						break;
				if(L_intROWID==tblINDTL.getRowCount())
				{//NO INDENT IS SELECTED TO SEE DETAILS
					setMSG("Please Select Indent to see details ..",'E');
					tbpMAIN.setSelectedIndex(0);
					return;
				}
				strGRDDS=tblINDTL.getValueAt(L_intROWID,intGRDDS_fn).toString();
				strSQLQRY="Select * from mr_dotrn,CO_PTMST where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"+cmbMKTTP.getITMCD().toString()+"' and DOT_INDNO='"+tblINDTL.getValueAt(L_intROWID,intINDNO_fn).toString()+"' and DOT_PRDDS='"+tblINDTL.getValueAt(L_intROWID,intGRDDS_fn).toString()+"' and  DOT_SBSCD1 in "+M_strSBSLS+" and PT_PRTTP='T' and PT_prtcd=DOT_TRPCD";
				L_rstRSSET=cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(strSQLQRY);
				if(L_rstRSSET!=null)
				{//CONSTRUCT TABLE WITH D.O. DETAILS
					Vector<String> L_vtrTEMP=new Vector<String>(20,5);
					String L_strTEMP=null;
					StringTokenizer L_stkTEMP=null;
					while(L_rstRSSET.next())
					{
						L_strTEMP=nvlSTRVL(L_rstRSSET.getString("DOT_DORNO")," ")+"|";
						L_strTEMP+=M_fmtLCDAT.format(L_rstRSSET.getDate("DOT_DORDT"))+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("PT_PRTNM")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("DOT_DORQT")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("DOT_LADQT")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("DOT_INVQT")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("DOT_FRTRT")," ")+"|";
						if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("0"))
							L_strTEMP+="Raised|";
						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("1"))
							L_strTEMP+="Authorized|";
						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("X"))
							L_strTEMP+="Cancelled|";
						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("D"))
							L_strTEMP+="Dispatched|";
						L_vtrTEMP.addElement(L_strTEMP);
					}
					for(int i=0;i<L_vtrTEMP.size();i++)
					{
						L_stkTEMP=new StringTokenizer((String)L_vtrTEMP.elementAt(i),"|");
						for(int L_intCOLID=1;L_intCOLID<tblDODTL.getColumnCount();L_intCOLID++)
						{
							tblDODTL.setValueAt(L_stkTEMP.nextToken(),i,L_intCOLID);
						}
					}
					L_rstRSSET.close();
				}
			}
			else if(tbpMAIN.getSelectedIndex()==2)
			{
				String L_strSQLQRY="";
				setMSG("",'N');
				tblIVDTL.clrTABLE();			
				boolean L_flgINDFL=false;//INVOICE DATA TO BE RETRIEVED USING D.O. NO
				int L_intROWID=0;
//				String L_strGRDDS=tblINDTL.getValueAt(L_intROWID,intGRDDS_fn).toString();
				if(tblDODTL!=null)
				{
					for(L_intROWID=0;L_intROWID<tblDODTL.getRowCount();L_intROWID++)
						if(tblDODTL.getValueAt(L_intROWID,0).equals(Boolean.TRUE))
							break;
					if(L_intROWID==tblDODTL.getRowCount())
						L_flgINDFL=true;//INVOICE DATA TO BE RETRIEVED USING INDENT NO
				}
				else
					L_flgINDFL=true;//INVOICE DATA TO BE RETRIEVED USING INDENT NO
				if(L_flgINDFL)
				{
					for(L_intROWID=0;L_intROWID<tblINDTL.getRowCount();L_intROWID++)
						if(tblINDTL.getValueAt(L_intROWID,0).equals(Boolean.TRUE))
							break;//NITHER INDENT NOR D.O. IS SELECTED
					//String L_strGRDDS=tblINDTL.getValueAt(L_intROWID,intGRDDS_fn).toString();
					if(L_intROWID==tblINDTL.getRowCount())
					{
						setMSG("Please Select D.O. or Indent to see details ..",'E');
						tbpMAIN.setSelectedIndex(1);
						return;
					}
//					L_flgINDFL=true;
				}
				if(!L_flgINDFL)
				{//SEARCH BY D.O. No
					L_strSQLQRY = "Select * from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+cmbMKTTP.getITMCD().toString()+"' and IVT_DORNO='"+tblDODTL.getValueAt(L_intROWID,intINDNO_fn).toString()+"' and IVT_INVQT > 0 and IVT_STSFL <> 'X' and IVT_SBSCD1 in "+M_strSBSLS+" and IVT_PRDDS='"+strGRDDS+"'";
					L_rstRSSET=cl_dat.exeSQLQRY(L_strSQLQRY);
					//System.out.println(L_strSQLQRY);
				}
				else
				{//SEARCH BY INDENT No
					L_strSQLQRY = "Select * from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+cmbMKTTP.getITMCD().toString()+"' and IVT_INDNO='"+tblINDTL.getValueAt(L_intROWID,intINDNO_fn).toString()+"' and IVT_INVQT > 0  and IVT_STSFL <> 'X' and IVT_SBSCD1 in "+M_strSBSLS+" and IVT_PRDDS='"+strGRDDS+"'";
					L_rstRSSET=cl_dat.exeSQLQRY(L_strSQLQRY);
					//System.out.println(L_strSQLQRY);
				}
				System.out.println("L_strSQLQRY>>"+L_strSQLQRY);
				if(L_rstRSSET!=null)
				{//DISPLAY THE DATA IN A TABLE
					Vector<String> L_vtrTEMP=new Vector<String>(20,5);
					String L_strTEMP=null;
					StringTokenizer L_stkTEMP=null;
					while(L_rstRSSET.next())
					{
						L_strTEMP=nvlSTRVL(L_rstRSSET.getString("IVT_INVNO")," ")+"|";
						L_strTEMP+=M_fmtLCDAT.format(L_rstRSSET.getDate("IVT_INVDT"))+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_INVQT")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_LADQT")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_LRYNO")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_LADNO")," ")+"|";
//						if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("0"))
//							L_strTEMP+="Raised|";
//						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("1"))
//							L_strTEMP+="Authorized|";
//						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("X"))
//							L_strTEMP+="Cancelled|";
//						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("D"))
//							L_strTEMP+="Dispatched|";
						L_vtrTEMP.addElement(L_strTEMP);
					}
					tbpMAIN.removeChangeListener(this);
					tbpMAIN.setSelectedIndex(2);
					tbpMAIN.addChangeListener(this);
					for(int i=0;i<L_vtrTEMP.size() && i<tblIVDTL.getRowCount();i++)
					{
						L_stkTEMP=new StringTokenizer((String)L_vtrTEMP.elementAt(i),"|");
						for(int L_intCOLID=1;L_intCOLID<tblIVDTL.getColumnCount();L_intCOLID++)
						{
							if(L_stkTEMP.hasMoreTokens())
								tblIVDTL.setValueAt(L_stkTEMP.nextToken(),i,L_intCOLID);
						}
					}
					L_rstRSSET.close();
				}
			}
/*			else if(tbpMAIN.getSelectedIndex()==3)
			{
				boolean L_flgINDFL=false;
				int L_intROWID=0;
//				String L_strGRDDS=tblINDTL.getValueAt(L_intROWID,intGRDDS_fn).toString();
				if(tblIVDTL!=null)
				{
					for(L_intROWID=0;L_intROWID<tblIVDTL.getRowCount();L_intROWID++)
						if(tblIVDTL.getValueAt(L_intROWID,0).equals(Boolean.TRUE))
							break;
					if(L_intROWID==tblDODTL.getRowCount())
					{
						setMSG("Please Select D.O. or Indent to see details ..",'E');
						tbpMAIN.setSelectedIndex(1);
						return;
					}
				}
				else
				{
					setMSG("Please Select  Invoice to see details ..",'E');
					tbpMAIN.setSelectedIndex(2);
					return;
				}
				}
					L_rstRSSET=cl_dat.exeSQLQRY("Select * from mr_ivtrn where IVT_DORNO='"+tblDODTL.getValueAt(L_intROWID,intINDNO_fn).toString()+"' and IVT_PRDDS='"+strGRDDS+"'");
					System.out.println("Select * from mr_ivtrn where IVT_DORNO='"+tblDODTL.getValueAt(L_intROWID,intINDNO_fn).toString()+"' and IVT_PRDDS='"+strGRDDS+"'");
				
				if(L_rstRSSET!=null)
				{
					Vector L_vtrTEMP=new Vector(20,5);
					String L_strTEMP=null;
					StringTokenizer L_stkTEMP=null;
					while(L_rstRSSET.next())
					{
						L_strTEMP=nvlSTRVL(L_rstRSSET.getString("IVT_INVNO")," ")+"|";
						L_strTEMP+=M_fmtLCDAT.format(L_rstRSSET.getDate("IVT_INVDT"))+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_INVQT")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_LADQT")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_LRYNO")," ")+"|";
						L_strTEMP+=nvlSTRVL(L_rstRSSET.getString("IVT_LADNO")," ")+"|";
//						if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("0"))
//							L_strTEMP+="Raised|";
//						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("1"))
//							L_strTEMP+="Authorized|";
//						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("X"))
//							L_strTEMP+="Cancelled|";
//						else if(L_rstRSSET.getString("DOT_STSFL").equalsIgnoreCase("D"))
//							L_strTEMP+="Dispatched|";
						L_vtrTEMP.addElement(L_strTEMP);
					}
					tblIVDTL=crtTBLPNL1(pnlIVDTL=new JPanel(null),new String[]{"DTL","Inv. No","Inv. Date","Lorry No.","L.A. No.","Inv. Qty.","Status"},L_vtrTEMP.size()+5,1,1,11,8.9,new int[]{50,75,75,100,80,75,75},new int[]{0});
					tblIVDTL.setInputVerifier(new TINPVF());
					tbpMAIN.remove(2);
					tbpMAIN.insertTab("Invoice Details", null, pnlIVDTL, null, 2);
					tbpMAIN.removeChangeListener(this);
					tbpMAIN.setSelectedIndex(2);
					tbpMAIN.addChangeListener(this);
					for(int i=0;i<L_vtrTEMP.size() && i<tblIVDTL.getRowCount();i++)
					{
						L_stkTEMP=new StringTokenizer((String)L_vtrTEMP.elementAt(i),"|");
						for(int L_intCOLID=1;L_intCOLID<tblIVDTL.getColumnCount();L_intCOLID++)
						{
							if(L_stkTEMP.hasMoreTokens())
								tblIVDTL.setValueAt(L_stkTEMP.nextToken(),i,L_intCOLID);
						}
					}
					L_rstRSSET.close();
				}
			}*/
		}catch(Exception e)
		{setMSG(e,"Child.StateChanged");	}
	}
}