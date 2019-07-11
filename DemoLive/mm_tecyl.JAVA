import javax.swing.*;
import java.awt.event.*;import java.awt.*;
import java.util.*;import javax.swing.undo.*;
import java.awt.Component;import java.awt.Dimension;import java.sql.ResultSet;
import javax.swing.event.*;import javax.swing.event.ChangeEvent;import javax.swing.event.ChangeListener;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Materials Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>    Cylinder Transactions</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                          Form       for&nbsp;Cylinder out entry and gate pass generation </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>         Existing system      in Maveric      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mm_tecyl.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mm_tecyl.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>10/08/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD> Series Used : COXXDPT, MMXXMGP </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_CLTRN</TD>    <TD>CLT_MATCD,CLT_GRNNO,CLT_SRLNO</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>   <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_GPTRN</TD>    <TD>GP_MGPTP,GP_MGPNO</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>CO_PTMST</TD>    <TD>PT_PRTTP,PT_PRTCD</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P><UL>  <LI>At present, only Non-returnable gate pass can be prepared for empty   cylinders, However, provision if kept for rejection gate pass of full   cylnders. In case of rejection gate pass, GRIN modification is pending.</LI>  <LI>All gate passes are prepared against single material code '8399999999',   with discription "Empty Cylinder". Cylinder number and material description is   stored in MM_RMMST as remark. And then printed in gatepass.</LI>  <LI>By default, previous rent rate is displayed to user for rent billing.</LI></UL><P>&nbsp;</P>*/
class mm_tecyl extends cl_pbase 
{
	/**	Cylinder out date (Date of gatepass) */
	private TxtDate txtOUTDT;/**	Order of cylinder details display */
	private TxtDate txtDUEDT;/**	Order of cylinder details display */
	private JLabel lblDUEDT;/**	Order of cylinder details display */
	private cl_Combo cmbORDER;/**	For Gate Pass Type */
	private cl_Combo cmbMGPTP;/**	To print gatepass */
	private JButton btnPRINT;/**	For Cylinder details */
	private cl_JTable tblCLDTL;/**	Department code */
	///**	To indicate that, empty cylinder is being sent out */
//	private JRadioButton rdbEMPTY;/**	To indicate that, clinders are rejected and hence beng sent full. (Kept disabled at present as rejected cylinders are sent by rejection gatepass) */
//	private JRadioButton rdbREJCT;/**	Department code */
	private TxtNumLimit txtDPTCD;/**	Gate pass number */
	private TxtNumLimit txtMGPNO;/**	Department name */
	private JTextField txtDPTNM;/**	Vendor code */
	private TxtLimit txtVENCD;/**	Vehicle number */
	private TxtLimit txtVEHNO;/**	Vendor name */
	private JTextField txtVENNM;/**	Name of the person carring out the cylinders */
	private TxtLimit txtVEHDS;/**	Count of old records in case of gate pss modification */
	private int intOLDID;/**	Index of column in table tblCLDTL */
	private final int TB_MATCD=1,
		TB_MATDS=2,
		TB_SRLNO=3,
		TB_GRNNO=4,
		TB_GRNDT=5,
		TB_RNTDY=6,
		TB_RNTRT=7,
		TB_RNTAM=8,
		TB_BILRF=9;/**	Hash table for recent rent rates in the format : KEY - Material Code; VALUE - Recent Rent Rate */
	private Hashtable<String,String> hstBILRT;/**	To disable input verifier when 'F1' is pressed */
	private boolean flgHELP=false;
	
	/**To construct the form	 
	 * To construct the form
	 * 
	 * <p>Lays the compoentns<br>
	 * Retrieves details of recent rent rates material code wise anf stores in hstBILRT<br>
	 * Registers input verifier<br>
	 * Order of display of cylinder details is hard coded
	 * */
	mm_tecyl()
	{
		super(2);
		try
		{
			setMatrix(20,6);
//			JPanel L_pnlTEMP=new JPanel(null);
//			add(rdbEMPTY=new JRadioButton("Empty"),1,1,1,1,L_pnlTEMP,'L');
//			add(rdbREJCT=new JRadioButton("Rejected"),1,2,1,1,L_pnlTEMP,'L');
//			ButtonGroup L_btgTEMP=new ButtonGroup();
//			L_btgTEMP.add(rdbEMPTY);L_btgTEMP.add(rdbREJCT);
//			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder("Cylinder Type"));
//			add(L_pnlTEMP,1,1,2,2.1,this,'L');
			add(new JLabel("Gate Pass Type"),1,1,1,1,this,'L');
			add(cmbMGPTP=new cl_Combo(),1,2,1,1,this,'L');
			add(new JLabel("Department"),1,3,1,0.8,this,'R');
			add(txtDPTCD=new TxtNumLimit(3.0),1,4,1,1,this,'L');
			add(txtDPTNM=new JTextField(),1,5,1,2,this,'L');
			
			add(new JLabel("Out Date"),2,1,1,1,this,'R');
			add(txtOUTDT=new TxtDate(),2,2,1,1,this,'L');
			add(new JLabel("Vendor"),2,3,1,0.8,this,'R');
			add(txtVENCD=new TxtLimit(5),2,4,1,1,this,'L');
			add(txtVENNM=new JTextField(),2,5,1,2,this,'L');
			
			add(lblDUEDT = new JLabel("Exp. Return Date"),3,1,1,1,this,'R');
			add(txtDUEDT = new TxtDate(),3,2,1,1,this,'L');
			txtDUEDT.setVisible(false);
			lblDUEDT.setVisible(false);
			add(new JLabel("Vehile No"),3,3,1,0.8,this,'R');
			add(txtVEHNO=new TxtLimit(10),3,4,1,1,this,'L');
			add(new JLabel("Carrier Name"),3,5,1,1,this,'R');
			add(txtVEHDS=new TxtLimit(10),3,6,1,1,this,'L');
			
			add(new JLabel("Sort Order"),5,1,1,1,this,'L');
			add(cmbORDER=new cl_Combo(),5,2,1,1,this,'L');
			add(new JLabel("Gate Pass No."),5,3,1,1,this,'L');
			add(txtMGPNO=new TxtNumLimit(8.0),5,4,1,1,this,'L');
			
			String[] L_staCOLHD=new String[]{"FL","Mat.Code","Description","Cylinder No.","GRIN No.","Cylinder In","Days","Rate","Amt","Bill No"};
			int[] L_inaCOLSZ=new int[]{20,80,200,80,80,70,40,60,60,70};
			tblCLDTL=crtTBLPNL1(this,L_staCOLHD,100,6,1,13,6,L_inaCOLSZ,new int[]{0});
			cmbORDER.addItem("Select","CLT_GRNDT");
			cmbORDER.addItem("By Date","CLT_GRNDT");
			cmbORDER.addItem("By Vendor","CLT_VENCD");
			cmbORDER.addItem("By Material","CLT_MATCD");
			cmbORDER.addActionListener(this);
			cmbMGPTP.addItem("Non-Returnable","52");
			cmbMGPTP.addItem("Returnable","51");
			cmbMGPTP.addActionListener(this);
			
			tblCLDTL.setCellEditor(TB_RNTRT,new TxtNumLimit(6.2));
			tblCLDTL.cmpEDITR[TB_RNTRT].addFocusListener(this);
			tblCLDTL.cmpEDITR[TB_RNTRT].addKeyListener(this);
//RETRIEVING DETAILS OF RECENT RENT RATE & PUTTING IN hstBILRF
			hstBILRT=new Hashtable<String,String>(10,0.75f);
			M_strSQLQRY="Select CLT_MATCD, isnull(CLT_RNTRT,0) CLT_BILRT from MM_CLTRN A where "
				+"CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_MATCD in (Select clt_matcd from MM_CLTRN B where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STSFL in ('7','8')) "
				+"and CLT_MGPDT=(Select  isnull(max(CLT_MGPDT),'01/01/1995') from MM_CLTRN C where C.CLT_MATCD=A.CLT_MATCD and C.CLT_CMPCD=A.CLT_CMPCD) ";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
					hstBILRT.put(M_rstRSSET.getString("CLT_MATCD"),M_rstRSSET.getString("CLT_BILRT"));
				M_rstRSSET.close();
			}
//			rdbEMPTY.setSelected(true);
//Registering INPUR VERIFIER
			INPVF oINPVF =new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
						((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(oINPVF);
				else if((M_vtrSCCOMP.elementAt(i) instanceof JLabel))
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			setMatrix(20,6);
		}catch(Exception e)
		{
			setMSG(e,"Child.Constructor");
		}
	}
	/**<b>TASKS : </b><br>
	 * &nbsp&nbsp&nbsp&nbspcmbOPTN : Focus nevigation<br>
	 * &nbsp&nbsp&nbsp&nbspcmbORDER : Display cuylinder details as per selection
	 */
	public void actionPerformed(ActionEvent P_AE)
	{
		super.actionPerformed(P_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
				{
					txtMGPNO.setEnabled(true);
					txtMGPNO.requestFocus();
				}
//				else
//					rdbREJCT.requestFocus();
			}
			else if(M_objSOURC == cmbMGPTP )
			{
				if(cmbMGPTP.getITMCD().equals("51"))
				{
					txtDUEDT.setVisible(true);
					lblDUEDT.setVisible(true);
				}
				else
				{
					txtDUEDT.setVisible(false);
					lblDUEDT.setVisible(false);
				}
			}
			else if(M_objSOURC == cmbORDER )
			{
				tblCLDTL.clrTABLE();
				if(cmbORDER.getSelectedIndex()==0)
					return;
				getCLDTL(0);
			}
		}catch(Exception e)
		{
			setMSG(e,"Child.ActionPerofrmed");
		}
	}
	
	/**<b>TASKS : </b><br>
	 * &nbsp&nbsp&nbsp&nbsptxtVENCD : Help on 'F1'<br>
	 * &nbsp&nbsp&nbsp&nbsptxtDPTCD : Help on 'F1'
	 * &nbsp&nbsp&nbsp&nbsptxtOUTDT & Key = Enter : If out date is entered, enable cmbORDER
	 * &nbsp&nbsp&nbsp&nbsptxtMGPNO & Key = Enter : Get details of the gate pass
	 * &nbsp&nbsp&nbsp&nbspKEy = Enter : Focus transfer
	 */
	public void keyPressed(KeyEvent P_KE)
	{
		super.keyPressed(P_KE);
		if(P_KE.getKeyCode() == P_KE.VK_F1)
		{
			if(M_objSOURC == txtVENCD)
			{
				if(txtVENCD.getText().length()==0)
					M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03, PT_TEL01, PT_TEL02 from CO_PTMST where PT_PRTTP='S'  ORDER BY PT_PRTNM";
				else
					M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD from CO_PTMST where PT_PRTTP='S' and PT_PRTCD like '"+txtVENCD.getText().toUpperCase()+"%' ORDER BY PT_PRTNM";
				M_strHLPFLD = "txtVENCD";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Vendor Name","Vendor Code"},2,"CT");
			}
			if(M_objSOURC == txtDPTCD)
			{
				if(txtDPTCD.getText().length()==0)
					M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT' order by CMT_CODDS";
				else
					M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT' and CMT_CODCD like '"+txtDPTCD.getText()+"% order by CMT_CODDS";
				M_strHLPFLD = "txtDPTCD";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Department"},2,"CT");
			}
		}
		else if(P_KE.getKeyCode() == P_KE.VK_ENTER)
		{
			if(M_objSOURC == txtOUTDT && txtOUTDT.getText().length()>0)
				cmbORDER.setEnabled(true);
			if(M_objSOURC == txtMGPNO && txtMGPNO.getText().length()>0)
				getDATA();
			else	 
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	
	void exeHLPOK()
	{
		cl_dat.M_flgHELPFL_pbst = false;
		super.exeHLPOK();
		cl_dat.M_wndHLP_pbst=null;
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD.equals("txtDPTCD"))
		{
			txtDPTCD.setText(L_STRTKN.nextToken());
			txtDPTNM.setText(L_STRTKN.nextToken());
		}
		if(M_strHLPFLD.equals("txtVENCD"))
		{
			txtVENNM.setText(L_STRTKN.nextToken());
			txtVENCD.setText(L_STRTKN.nextToken());
		}
	}
	
	/**<b>Rent Rate Column of Table : </B> Display rent rate of latest Transaction from hstBILRT	 */
	public void focusGained(FocusEvent P_FE)
	{
		super.focusGained(P_FE);
		if(M_objSOURC == txtOUTDT && txtOUTDT.getText().length()==0)
		{
			txtOUTDT.setText(cl_dat.M_strLOGDT_pbst);
			txtOUTDT.select(0,10);
		}
		else if(M_objSOURC == txtDPTCD && txtDPTCD.getText().length()==0)
		{
			txtDPTCD.setText("302");
			txtDPTCD.select(0,3);
		}
		else if(M_objSOURC == tblCLDTL.cmpEDITR[TB_RNTRT])
			if(((JTextField)tblCLDTL.cmpEDITR[TB_RNTRT]).getText().length() == 0)
				((JTextField)tblCLDTL.cmpEDITR[TB_RNTRT]).setText((String)hstBILRT.get(tblCLDTL.getValueAt(tblCLDTL.getSelectedRow(),TB_MATCD)));
	}
	
	/**To validate data	 */
	boolean vldDATA()
	{
		setMSG("",'N');
		if(txtOUTDT.getText().length()==0)
		{
			setMSG("Enter Out Date ..",'E');
			txtOUTDT.requestFocus();
		}
		else if(txtDPTCD.getText().length()==0)
		{
			setMSG("Enter Department ..",'E');
			txtDPTCD.requestFocus();
		}
		else if(txtVENCD.getText().length()==0)
		{
			setMSG("Enter Vendor ..",'E');
			txtVENCD.requestFocus();
		}
		else if(txtVEHNO.getText().length()==0)
		{
			setMSG("Enter Vehicle No ..",'E');
			txtVEHNO.requestFocus();
		}
		else if(txtVEHDS.getText().length()==0)
		{
			setMSG("Enter Carrier Name ..",'E');
			txtVEHDS.requestFocus();
		}
		boolean L_flgNODATA=true;
		for(int i=0;i<tblCLDTL.getRowCount()&&tblCLDTL.getValueAt(i,TB_MATCD).toString().length()>0;i++)
		{
			if(tblCLDTL.getValueAt(i,0).equals(Boolean.TRUE))
			{
				L_flgNODATA=false;
				if(tblCLDTL.getValueAt(i,TB_RNTRT).toString().length() == 0)
				{
					setMSG("Enter Rent Rate ..",'E');
					tblCLDTL.editCellAt(i,TB_RNTRT);
				}
				if(tblCLDTL.getValueAt(i,TB_RNTAM).toString().length() == 0)
				{
					setMSG("Enter Rent Amount ..",'E');
					tblCLDTL.editCellAt(i,TB_RNTAM);
				}
			}
		}
		if(L_flgNODATA)
			setMSG("No Cylinders selected for gate pass ..",'E');
		if(M_flgERROR)
			return false;
		return true;
	}
	/**To save the data/changes
	 * 
	 * To save the data/changes
	 * 
	 * <p>Sets Gate pass type as per selection of rdbEMPTY/rdbREJCT (Presently, not in use. Only possible gate pass is '53' i.e. non-returnable)<br>
	 * ADDITION  : 
	 *	&nbsp&nbsp&nbspGenerate gate pass number and update in CDTRN<br>
	 * &nbsp&nbsp&nbspConstruct remark mensioning cylinder numbers and put in MM_RMMST<br>
	 * &nbsp&nbsp&nbspInsert gate pass details in MM_GPTRN and update status flags of corressponding cylinders in MM_CLTRN<br>
	 * MODIFICATION : <br>
	 * &nbsp&nbsp&nbspUpdate/delete MM_GPTRN & MM_RMMST
	 * &nbsp&nbsp&nbspUpdate status flags in MM_CLTRN accordingly.
	 */
	void exeSAVE()
	{
		if(!vldDATA())
			return;
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
			cl_dat.M_flgLCUPD_pbst = true;
			String L_strMGPTP="53";
//			if(rdbEMPTY.isSelected())
			L_strMGPTP=cmbMGPTP.getITMCD().toString();
			int L_intCYLCT=0;
			String L_strMGPNO="";
			String L_strYRDGT="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MMXXMGP' and substring(CMT_CODCD,2,2)='"+L_strMGPTP+"'");
				if(M_rstRSSET.next())
				{
					L_strYRDGT=M_rstRSSET.getString("CMT_CODCD");
					int L_intMGPNO=M_rstRSSET.getInt("CMT_CCSVL")+1;
					L_strMGPNO=Integer.toString(L_intMGPNO);
					while(L_strMGPNO.length()<5)
						L_strMGPNO="0"+L_strMGPNO;
					M_rstRSSET.close();
				}
				cl_dat.exeSQLUPD("Update CO_CDTRN set CMT_CCSVL='"+L_strMGPNO+"' where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MMXXMGP' and substring(CMT_CODCD,2,2)='"+L_strMGPTP+"'","setLCLUPD");
				L_strMGPNO=L_strYRDGT+L_strMGPNO;
			}
			else
			{
				L_strMGPNO=txtMGPNO.getText();
				cl_dat.exeSQLUPD("Delete from MM_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_DOCTP='GP' and RM_TRNTP='GP' and RM_DOCNO='"+L_strMGPNO+"'","se");
			}
			//CONSTRUCTYING REMARK :
			Hashtable<Object,Object> L_hstREMRK=new Hashtable<Object,Object>(5,0.75f);
			for(int i=0;i<tblCLDTL.getRowCount()&&tblCLDTL.getValueAt(i,TB_MATCD).toString().length()>0;i++)
			{
				if(tblCLDTL.getValueAt(i,0).equals(Boolean.TRUE))
				{
					L_intCYLCT++;//Totalise No. of cylinders
					if(L_hstREMRK.containsKey(tblCLDTL.getValueAt(i,TB_MATDS)))//Put remarks in hash table
						L_hstREMRK.put(tblCLDTL.getValueAt(i,TB_MATDS),L_hstREMRK.get(tblCLDTL.getValueAt(i,TB_MATDS)).toString()+", "+tblCLDTL.getValueAt(i,TB_SRLNO));
					else
						L_hstREMRK.put(tblCLDTL.getValueAt(i,TB_MATDS),tblCLDTL.getValueAt(i,TB_SRLNO));
				//Update MM_CLTRN
					M_strSQLQRY="Update MM_CLTRN set CLT_MGPNO='"+L_strMGPNO+"', CLT_BILRF='"+tblCLDTL.getValueAt(i,TB_BILRF)+"',"
						+" CLT_RNTRT = "+tblCLDTL.getValueAt(i,TB_RNTRT)+", CLT_RNTAM="+tblCLDTL.getValueAt(i,TB_RNTAM)
						+",CLT_MGPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtOUTDT.getText()))+"',"+getUSGDTL("CLT",'U',cmbMGPTP.getITMCD().equals("51") ? "8":"7")
						+" where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_MATCD='"+tblCLDTL.getValueAt(i,TB_MATCD)//CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and
						+"' and CLT_SRLNO='"+tblCLDTL.getValueAt(i,TB_SRLNO)+"' and CLT_GRNNO='"+tblCLDTL.getValueAt(i,TB_GRNNO)+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else if(i<intOLDID)
				{
					M_strSQLQRY="Update MM_CLTRN set CLT_MGPNO='', CLT_BILRF='',"
						+" CLT_RNTRT = 0, CLT_RNTAM=0"
						+",CLT_MGPDT=null,"+getUSGDTL("CLT",'U',cmbMGPTP.getITMCD().equals("51") ? "8":"7")
						+" where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_MATCD='"+tblCLDTL.getValueAt(i,TB_MATCD)//CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and
						+"' and CLT_SRLNO='"+tblCLDTL.getValueAt(i,TB_SRLNO)+"' and CLT_GRNNO='"+tblCLDTL.getValueAt(i,TB_GRNNO)+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}

			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY="Update MM_GPTRN set "
					+"GP_VENCD='"+txtVENCD.getText()+"',"//GP_VENCD ,
					+"GP_VENNM='"+txtVENNM.getText()+"',"//GP_VENNM ,
					+"GP_ISSQT="+Integer.toString(L_intCYLCT)+","//GP_ISSQT ,
					+"GP_DPTCD='"+txtDPTCD.getText()+"',"//GP_DPTCD,
					+"GP_VEHNO='"+txtVEHNO.getText()+"',"//GP_VEHNO,
					+"GP_VEHDS='"+txtVEHDS.getText()+"',"//GP_VEHDS,
					+"GP_DUEDT="+(L_strMGPTP.equals("51") ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDUEDT.getText()))+"'" : "null")+","//GP_VEHDS,
					+getUSGDTL("GP",'U',null)
					+" where GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP='"+M_strSBSCD.substring(2,4)+"'"
					+" and GP_MGPTP='"+L_strMGPTP+"'"//GP_MGPTP,
					+" and GP_MGPNO='"+L_strMGPNO+"'"//GP_MGPNO,
					+" and GP_MATCD='8399999999'";//GP_MATCD ,
			}
			else
			{
	//GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD ,GP_MGPDT ,GP_VENCD ,GP_VENNM ,GP_ISSQT ,GP_RECQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,GP_GRNNO,GP_GINNO ,GP_STSFL ,GP_TRNFL,GP_LUSBY,GP_LUPDT,GP_DUEDT,GP_RCPDT//RM_TRNFL,RM_STSFL,RM_LUSBY,RM_LUPDT
				M_strSQLQRY="Insert into MM_GPTRN (GP_CMPCD,GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD ,GP_MGPDT ,GP_VENCD ,GP_VENNM ,GP_ISSQT ,GP_RECQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,GP_GRNNO,GP_GINNO ,GP_TRNFL,GP_STSFL ,GP_LUSBY,GP_LUPDT,GP_DUEDT,GP_RCPDT) values ("
					+"'"+cl_dat.M_strCMPCD_pbst+"',"
					+"'"+M_strSBSCD.substring(2,4)+"',"//GP_STRTP,
					+"'"+L_strMGPTP+"',"//GP_MGPTP,
					+"'"+L_strMGPNO+"',"//GP_MGPNO,
					+"'8399999999',"//GP_MATCD ,
					+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//GP_MGPDT ,
					+"'"+txtVENCD.getText()+"',"//GP_VENCD ,
					+"'"+txtVENNM.getText()+"',"//GP_VENNM ,
					+Integer.toString(L_intCYLCT)+","//GP_ISSQT ,
					+"0,"//GP_RECQT,
					+"'"+txtDPTCD.getText()+"',"//GP_DPTCD,
					+"'"+txtVEHNO.getText()+"',"//GP_VEHNO,
					+"'"+txtVEHDS.getText()+"',"//GP_VEHDS,
					+"'',"//GP_GRNNO,
					+"'',"//GP_GINNO ,
					+getUSGDTL("GP",'I',"3")+","  // GP STATUS FLAG to be shown as 3, will be authorised by stores
					+(L_strMGPTP.equals("51") ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDUEDT.getText()))+"'" : "null")+","//GP_DUEDT,
					+"null)";//GP_RCPDT
			}
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			Enumeration L_enmREMRK=L_hstREMRK.keys();
			String L_strMATDS=null;
			int L_intREMTP=0;
			while(L_enmREMRK.hasMoreElements() && cl_dat.M_flgLCUPD_pbst)
			{
				L_strMATDS=(String)L_enmREMRK.nextElement();
				M_strSQLQRY = "INSERT INTO MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,RM_DOCTP,RM_REMTP,RM_DOCNO,RM_REMDS," 
					+"RM_TRNFL,RM_STSFL,RM_LUSBY,RM_LUPDT) VALUES ('"
					+cl_dat.M_strCMPCD_pbst+"','"+M_strSBSCD.substring(2,4)+"','GP','GP','"+Integer.toString(++L_intREMTP)+"','"+L_strMGPNO+"','"
					+"Cylender No. For "+L_strMATDS+" : "+L_hstREMRK.get(L_strMATDS)+"',"+getUSGDTL("RM",'I',"0")+")";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				txtMGPNO.setText(L_strMGPNO);
				setMSG("Gate Pass prepared .. ",'N');
				JOptionPane.showMessageDialog(this, "Please Note Down Gate Pass Number : \n                     "+ L_strMGPNO, "Material Gate Pass Number ..", JOptionPane.INFORMATION_MESSAGE);
			}
			else
				setMSG("Error occured during saving ..",'E');
		}catch(Exception e)
		{setMSG(e,"Child.exeSAVE");}
	}
	/**To display details of gate pass.<br>Previous data is displayed with forecolor blue.	 */
	void getDATA()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
			M_strSQLQRY = "Select * from MM_CLTRN where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_MGPNO='"+txtMGPNO.getText()+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			int i=0;
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					tblCLDTL.setRowColor(i,Color.blue);
					if(i==0)
						txtOUTDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_MGPDT")));
					tblCLDTL.setValueAt(Boolean.TRUE,i,0);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_MATCD"),i,TB_MATCD);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_MATCD"),i,TB_MATDS);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_SRLNO"),i,TB_SRLNO);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_GRNNO"),i,TB_GRNNO);
					tblCLDTL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_GRNDT")),i,TB_GRNDT);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_MATCD"),i,TB_RNTDY);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_RNTRT"),i,TB_RNTRT);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_RNTAM"),i,TB_RNTAM);
					tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_BILRF"),i++,TB_BILRF);
					intOLDID=i;
				}
			}
			M_strSQLQRY = "Select * from MM_GPTRN where GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_MGPNO='"+txtMGPNO.getText()+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					txtDPTCD.setText(M_rstRSSET.getString("GP_DPTCD"));
					txtVENCD.setText(M_rstRSSET.getString("GP_VENCD"));
					txtVEHNO.setText(M_rstRSSET.getString("GP_VEHNO"));
					txtVEHDS.setText(M_rstRSSET.getString("GP_VEHDS"));
					if(M_rstRSSET.getDate("GP_DUEDT")!=null)
						txtDUEDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("GP_DUEDT")));
				}
			}
			if(i==0)
				setMSG("Invalid Gate Pass Number ..",'E');
			else
				getCLDTL(i);
		}catch(Exception e)
		{setMSG(e,"Child.getDATA");}
	}
	/**Over ridden to disable cylinder no,material code, description, due days in tblCLDTL and verndor, deptt. name, MGPNO	 */
	void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(P_flgSTATE);
		if(tblCLDTL.cmpEDITR[1].isEnabled())
		{
			tblCLDTL.setEnabled(false);
			tblCLDTL.cmpEDITR[0].setEnabled(true);
			tblCLDTL.cmpEDITR[7].setEnabled(true);
			tblCLDTL.cmpEDITR[8].setEnabled(true);tblCLDTL.cmpEDITR[9].setEnabled(true);
		}
		txtVENNM.setEnabled(false);txtDPTNM.setEnabled(false);
		cmbORDER.setEnabled(false);txtMGPNO.setEnabled(false);
	}
	/**To get Cylinder Details from MM_CLTRN
	 * 
	 * To get Cylinder Details from MM_CLTRN
	 * 
	 * <p>Collects details of empty culinders with stores from MM_CLTRN and displays in tblCLDTL.<br>
	 * Displays with black  forecolor. <br>
	 * </p>
	 * @param P_intROWID : Line number from which data is to be displayed.
	 */
	private void getCLDTL(int P_intROWID) throws Exception
	{
		if(tblCLDTL.isEditing())
			tblCLDTL.getCellEditor().stopCellEditing();
		tblCLDTL.setRowSelectionInterval(0,0);
		tblCLDTL.setColumnSelectionInterval(0,0);
		M_strSQLQRY = "Select CLT_MATCD,CLT_SRLNO,CLT_GRNDT,CLT_RFDAY,CLT_GRNNO,CT_MATDS from MM_CLTRN,CO_CTMST where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STSFL in ('2','3','4','5','6') and CT_MATCD=CLT_MATCD "+(txtVENCD.getText().length()>0 ? " and CLT_VENCD='"+txtVENCD.getText().toUpperCase()+"' " : "")+"order by "+cmbORDER.getITMCD();
		//System.out.println(M_strSQLQRY);
		M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
		boolean L_flgNODATA=true;
		if(M_rstRSSET!=null)
		{
			int i=P_intROWID;
			while(M_rstRSSET.next())
			{
				L_flgNODATA=false;
				tblCLDTL.setRowColor(i,Color.black);
				tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_MATCD"),i,TB_MATCD);
				tblCLDTL.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,TB_MATDS);
				tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_SRLNO"),i,TB_SRLNO);
				tblCLDTL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_GRNDT")),i,TB_GRNDT);
						
				String L_strTEMP=calTIME(txtOUTDT.getText(),(String)tblCLDTL.getValueAt(i,TB_GRNDT));
				int L_intRFDAY=Integer.parseInt(new StringTokenizer(L_strTEMP,":").nextToken())-M_rstRSSET.getInt("CLT_RFDAY");
				tblCLDTL.setValueAt(String.valueOf(L_intRFDAY),i,TB_RNTDY);
				tblCLDTL.setValueAt(M_rstRSSET.getString("CLT_GRNNO"),i++,TB_GRNNO);
			}
			M_rstRSSET.close();
			tblCLDTL.setRowSelectionInterval(10,10);
			tblCLDTL.setColumnSelectionInterval(0,0);
		}
		if(L_flgNODATA)
			setMSG("No Cylinders Available ..",'N');
	}
	/**To verify the input	 */
	private class INPVF extends InputVerifier
	{
		/**<b>TASKS</b><br>
		 * &nbsp&nbsp&nbsp&nbsptxtDPTCD : Validate Deptt. code and display Name
		 * &nbsp&nbsp&nbsp&nbsptxtVENCD : Validate Vendor code and display name.
		 * &nbsp&nbsp&nbsp&nbsptxtOUTDT : Should not be less than today.
		 * 
		 */
		public boolean verify(JComponent input)
		{
			try
			{
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length() == 0)
						return true;
				setMSG("",'N');
				ResultSet L_rstRSSET=null;
				if(input == txtDPTCD)
				{
					L_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT' and CMT_CODCD='"+txtDPTCD.getText()+"'");
					if(L_rstRSSET!=null)
						if(L_rstRSSET.next())
						{
							txtDPTNM.setText(L_rstRSSET.getString("CMT_CODDS"));
							return true;
						}
					setMSG("Invalid Department ..",'E');
					return false;
				}
				else if(input == txtVENCD)
				{
					L_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_PTMST where PT_PRTTP='S' and PT_PRTCD='"+txtVENCD.getText().toUpperCase()+"'");
					if(L_rstRSSET!=null)
						if(L_rstRSSET.next())
						{
							txtVENNM.setText(L_rstRSSET.getString("PT_PRTNM"));
							txtVENCD.setText(L_rstRSSET.getString("PT_PRTCD"));
							return true;
						}
					setMSG("Invalid Vendor ..",'E');
					return false;
				}
				else if(input == txtOUTDT)
				{
					if(txtOUTDT.vldDATE() == null)
					{
						if(M_fmtLCDAT.parse(txtOUTDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
						{
							setMSG("Out date cannot be less than today ..",'E');
							return false;
						}
					}
				}
			}catch(Exception e)
			{
				setMSG("Invalid Data ..",'E');
				return false;
			}
			return true;
		}
	}
}