import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.math.*;
import java.text.*;
/**
<P>Program Description :</P>
<P>
<FONT color=purple><STRONG>Program Details 
:</STRONG></FONT>

<TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" 
width="75%" borderColorDark=darkslateblue borderColorLight=white>
  
  <TR>
    <TD>System Name</TD>
    <TD>Process&nbsp;Resource&nbsp;Planning System </TD></TR>
  <TR>
    <TD>Program Name</TD>
    <TD>Recipe Master</TD></TR>
  <TR>
    <TD>Program Desc</TD>
    <TD>Form for adding, modifying, retrieving and 
      printing details of Grade Run &amp; recipe </TD></TR>
  <TR>
    <TD>Basis Document</TD>
    <TD> Recipe sheet given by Mr. APT</TD></TR>
  <TR>
    <TD>Executable path</TD>
    <TD>f:\dada\asoft\exec\splerp2\pr_merpm.class</TD></TR>
  <TR>
    <TD>Source path</TD>
    <TD>g:\splerp2\pr_merpm.java</TD></TR>
  <TR>
    <TD>Author </TD>
    <TD>AAP </TD></TR>
  <TR>
    <TD>Date</TD>
    <TD>15/05/2003 </TD></TR>
  <TR>
    <TD>Version </TD>
    <TD>1.0.0</TD></TR>
  <TR>
    <TD><STRONG>Revision : 1 </STRONG></TD>
    <TD>For Base classes revision and Subsystem implementation</TD></TR>
  <TR>
    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
      By</TD>
    <TD>AAP</TD></TR>
  <TR>
    <TD>
      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
      Date</P> 
      </TD>
    <TD>
      <P align=left>20/09/2003</P>  </TD></TR>
  <TR>
    <TD>
      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P> 
        </TD>
    <TD>2.0.0.</TD></TR></TABLE>
<FONT color=purple><STRONG>Tables Used : </STRONG></FONT>

<TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  
="100%" background="" borderColorDark=white borderColorLight=darkslateblue>
  
  <TR>
    <TD>
      <P align=center>Table Name</P></TD>
    <TD>
      <P align=center>Primary Key</P></TD>
    <TD>
      <P align=center>Add</P></TD>
    <TD>
      <P align=center>Mod</P></TD>
    <TD>
      <P align=center>Del</P></TD>
    <TD>
      <P align=center>Enq</P></TD></TR>
  <TR>
    <TD>PR_RPMST</TD>
    <TD>RP_RUNNO,RP_GRDCD,RP_MTLCD</TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
  <TR>
    <TD>PR_RNMST</TD>
    <TD> RN_RUNNO,RN_GRDCD</TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
  <TR>
    <TD>PR_BTMST</TD>
    <TD>BT_RUNNO,BT_GRDCD,BT_BATNO</TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
  <TR>
    <TD>PR_BTTRN</TD>
    <TD>BTT_RUNNO,BTT_GRDCD,BTT_BATNO,BTT_MTLCD</TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center><FONT 
face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>
*/
class pr_merpm extends cl_pbase
{
	private TxtLimit	txtGRDCD;/**GRADE CODE VARCHAR 10*/
	private TxtLimit	txtGRDDS;/**GRADE DESCRIPTION VARCHAR 11*/
	private TxtLimit	txtRNETM;/**RUN END TIME HH:MM*/
	private TxtLimit	txtLOTNO;/**LOT NO FOR CURRENT BATCH*/
	private TxtLimit	txtCMRNO;
	private TxtLimit	txtCMRRV;
	private TxtLimit	txtRNSTM;/**RUN START TIME HH:MM*/
	private TxtNumLimit txtFDRQT;
	private TxtNumLimit	txtPWDFD;
	private TxtNumLimit	txtRUNNO;/**RUN NUMBER*/
	private TxtDate		txtRNEDT;/**RUN END DATE DD*/
	private TxtDate		txtRNSDT;/**RUN START DATE*/
	private cl_JTBL		tblPSDTL;/**DETAILS OF POLYSTYRENE TO BE ADDED TO THE BATCH*/
	private cl_JTBL			tblSTFNL;/**RECIPES OF TRIALS CARRIED OUT*/
	private cl_JTBL			tblRECIP;/**RECIPE TABLE*/
	private cl_JTBL		tblFDRQT;
//				tblRCLQT;/**DETAILS OF RECYCLE MATERIAL AVAILABLE*/
	private cl_JTBL			tblRFRCP;/**JTable for ref. recipies */
	private TxtNumLimit txtBATSZ;/**BATCH SIZE*/
	private TxtNumLimit			txtPRAQT;/**ACTUAL PRODUCTION*/
	private TxtNumLimit			txtPRTQT;/**QUANTITY TO BE PRODUCED*/
//				txtPSRCL;/**PS TO BE RECYCLED IN CURRENT BATCH*/
	private TxtNumLimit			txtPSPWD;/**PS POWDER TO BE USED*/
	private TxtNumLimit			txtTRLCT;/**TRIAL COUNT*/
	private TxtNumLimit			txtBTTCT;/**NO. OF BATCHES TO BE FORMULATED*/
	private TxtNumLimit			txtBTACT;/**NO OF BATCHES FORMULATED*/
	private TxtNumLimit			txtBATNO;/**UNIQUE SERIAL NO FOR THE BATCH*/
				
	private JButton		btnPRINT;
	private JButton			btnRFRCPY;/**SELECT REF. RECIPE - YES*/
	private JButton			btnRFRCPN;/**SELECT REF. RECIPE - NO*/
//	private JButton			btnRCLQTY;/**SELECT Recyle qty - YES*/
//	private JButton			btnRCLQTN;/**SELECT Recycle qty - NO*/
	private JButton			btnSTFNLY;/**SET FINAL RECIPE - YES*/
	private JButton			btnSTFNLN;/**SET FINAL RECIPE - YES*/
	private JButton			btnSTFNL;/**BUTTON TO FINALISE RECIPE FROM TRIAL RECIPES.*/
	private DefaultCellEditor dceTBLTXT;
	private DefaultCellEditor  dcePSDTL;
	private JCheckBox chbRFRCP;/**TICK REFERNCE RECIPE*/
	private JPanel pnlRECIP;
//			pnlRCLQT;*/
	private JDialog /**dlgRCLQT;*/
			dlgRFRCP;
	private Hashtable	htbRCLQT;/**HASHTABLE FOR DETAILS OF RECYCLE MATERIAL*/
	private double RCLQT=0.0;/**TOTAL RECYCLE QUANTITY*/
	private int row;
	private int LM_RCPROW=20;/**global varialble for no. of rows in recipe table*/
	private JComboBox	cmbUOMCD;/**for uom */
	private JComboBox			cmbPSPWD;
	private JComboBox			cmbLINNO;/**TO SELECT LINE NO FOR NEW RUN*/
	private JComboBox			cmbCMRTP;
	private JComboBox			cmbPSDTL;/**LOCATION OF PS STORAGE*/
	private String LM_GRDCD="";/**to store current grade code. Will be compared on ACP of txtGRDCD. AND IN exeSAVE*/
	private double[] LM_ARQTY;/**ARRAY FOR ALL QUANTITIES CALCULATED in KG*/
	private static double LM_KGTGM=1000.0;
	private static double  LM_KGTMT=0.001;/**FACTORS FOR UNIT CONVERSION*/
	private NumberFormat nf;
	private FileOutputStream	outFILE;
	private File				FILE;
	private DataOutputStream	outDTSTR;
	private boolean LM_FLERR;
	
	pr_merpm()
	{
		super(1);
		txtGRDCD=new TxtLimit(10);txtRNETM=new TxtLimit(5);txtRUNNO=new TxtNumLimit(8);
		txtRNEDT=new TxtDate();txtLOTNO=new TxtLimit(10);txtPWDFD=new TxtNumLimit(1.0);txtCMRNO=new TxtLimit(6);txtCMRRV=new TxtLimit(2);
		txtRNSTM=new TxtLimit(5);txtRNSDT=new TxtDate();txtBATSZ=new TxtNumLimit(7.3);txtPRTQT=new TxtNumLimit(8.3);
		txtBTTCT=new TxtNumLimit(2);txtBTACT=new TxtNumLimit(2);txtPSPWD=new TxtNumLimit(6.3);
		//txtPSRCL=new TxtNumLimit(6.3);txtRCLFD=new TxtNumLimit(1.0);
		txtGRDDS=new TxtLimit(12);txtPRAQT=new TxtNumLimit(7.3);txtBATNO=new TxtNumLimit(2);txtTRLCT=new TxtNumLimit(2);
		cmbUOMCD=new JComboBox();cmbUOMCD.addItem("MT");cmbUOMCD.addItem("Kg");cmbUOMCD.addItem("gm");
		cmbUOMCD.addActionListener(this);cmbUOMCD.addFocusListener(this);
		btnSTFNL=new JButton("Set Final Recipe");btnPRINT=new JButton("Print Recipe");
		btnSTFNL.setVisible(false);btnSTFNL.setEnabled(false);
		cmbPSDTL=new JComboBox();cmbPSDTL.addItem("Select");cmbPSDTL.addItem("Select1");cmbPSDTL.addItem("Select2");
		cmbCMRTP=new JComboBox();cmbCMRTP.addItem("Select");cmbCMRTP.addItem("CMR");cmbCMRTP.addItem("DWR");
		cmbCMRTP.addItem("Other");
		cmbLINNO=new JComboBox(new String[]{"Select Line No.","Line : 50","Line : 51","Line : 52","Line : 53"});
		cmbPSPWD=new JComboBox();cmbPSPWD.addItem("Select");
		LM_ARQTY=new double[20];
		String[] names=new String[]{"FL","Material Code","Description","Manufacturer","Lot No.","PHR","%age","Qty in batch","UOM","Feeder","Base Material"};
		int[] wid=new int[]{20,75,150,100,75,75,70,75,40,30,50};
//ADDING TITLED BORDER TO THE RECIPE TABLE		
		JPanel ptemp=new JPanel(new BorderLayout());
		setMatrix(1,1);
//CREATING TABLE FOR RECIPE
		names=new String[]{"FL","Material Code","Description","Manufacturer","Lot No.","PHR","%age","Qty in batch","UOM","Feeder","Base Material"};
		wid=new int[]{20,75,150,100,75,75,70,75,40,30,50};
		int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
		int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		tblRECIP=crtTBLPNL(ptemp,names,LM_RCPROW,1,1,1,1,wid,new int[]{0,10}) ;
		tblRECIP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		((cl_JTBL)tblRECIP).clrTABLE();
		((cl_JTBL)tblRECIP).setCellEditor(1,new TxtLimit(10));
		TxtLimit t2=new TxtLimit(45);t2.setEnabled(false);
		((cl_JTBL)tblRECIP).setCellEditor(2,t2);
		((cl_JTBL)tblRECIP).setCellEditor(3,new TxtLimit(40));
		((cl_JTBL)tblRECIP).setCellEditor(4,new TxtLimit(10));
		((cl_JTBL)tblRECIP).setCellEditor(5,new TxtNumLimit(8.5));
		((cl_JTBL)tblRECIP).setCellEditor(9,new TxtNumLimit(1.0));
		TxtNumLimit t1=new TxtNumLimit(9.6);t1.setEnabled(false);
		((cl_JTBL)tblRECIP).setCellEditor(6,t1);
//ADDING KEY LISTENER TO EDITOR COMPONENTS OF JTBL		
		for (int i=0;i<((cl_JTBL)tblRECIP).cmpEDITR.length;i++)
		{
			((JComponent)((cl_JTBL)tblRECIP).cmpEDITR[i]).addKeyListener(this);
			((JComponent)((cl_JTBL)tblRECIP).cmpEDITR[i]).addFocusListener(this);	
		}
		((cl_JTBL)tblRECIP).setCellEditor(8,cmbUOMCD);
		ptemp.setBorder(BorderFactory.createTitledBorder(" Recipe "));
//ADDING ALL COMPONENTS
		setMatrix(18,7);
		add(new JLabel("Select Line No : "),1,1,1,1,this,'L');//LINE 1
		add(cmbLINNO,1,2,1,1,this,'L');
		add(btnSTFNL,1,3,1,2,this,'L');
		add(new JLabel("Start Date/Time : "),1,5,1,1,this,'L');
		add(txtRNSDT,1,6,1,1,this,'L');
		add(txtRNSTM,1,7,1,1,this,'L');
		add(new JLabel("Grade : "),2,1,1,1,this,'L');//LINE 2
		add(txtGRDCD,2,2,1,1,this,'L');
		add(new JLabel("Code : "),2,3,1,1,this,'L');
		add(txtGRDDS,2,4,1,1,this,'L');
		add(new JLabel("End Date/Time"),2,5,1,1,this,'L');
		add(txtRNEDT,2,6,1,1,this,'L');
		add(txtRNETM,2,7,1,1,this,'R');
		add(new JLabel("Run No. : "),3,1,1,1,this,'L');//LINE 3
		add(txtRUNNO,3,2,1,1,this,'L');
		add(new JLabel("Trial Count : "),3,3,1,1,this,'L');
		add(txtTRLCT,3,4,1,1,this,'L');
		add(new JLabel("Prod Tgt/Actual : "),3,5,1,1,this,'L');
		add(txtPRTQT,3,6,1,1,this,'L');
		add(txtPRAQT,3,7,1,1,this,'L');
		add(new JLabel("Batch Size : "),4,1,1,1,this,'L');//LINE 4
		add(txtBATSZ,4,2,1,1,this,'L');
		add(new JLabel("Batch Number : "),4,3,1,1,this,'L');
		add(txtBATNO,4,4,1,1,this,'L');
		add(new JLabel("Batch Tgt/Actual : "),4,5,1,1,this,'L');
		add(txtBTTCT,4,6,1,1,this,'L');
		add(txtBTACT,4,7,1,1,this,'L');
		add(new JLabel("Lot No. : "),5,1,1,1,this,'L');
		add(txtLOTNO,5,2,1,1,this,'L');
		add(new JLabel("Request Type : "),5,3,1,1,this,'L');
		add(cmbCMRTP,5,4,1,1,this,'L');
		add(new JLabel("Request No./Rev. : "),5,5,1,1,this,'L');
		add(txtCMRNO,5,6,1,1,this,'L');
		add(txtCMRRV,5,7,1,1,this,'L');
		setMatrix(18,4);//LINE 6
		add(ptemp,6,1,6,4,this,'L');
		setMatrix(18,7);//LINE 13
		add(new JLabel("PS Powder qty/fdr : "),12,1,1,1,this,'L');
		add(txtPSPWD,12,2,1,0.75,this,'L');
		add(txtPWDFD,12,2,1,0.25,this,'R');
		add(new JLabel("Powder in : "),12,3,1,1,this,'L');
		add(cmbPSPWD,12,4,1,1,this,'L');
		add(btnPRINT,12,6,1,2,this,'L');
		ptemp=new  JPanel(new BorderLayout());
		setMatrix(1,1);

		names=new String[]{"FL","Grade","Lot No","Quantity","Location"};
		wid=new int[]{20,80,100,100,100};
		tblPSDTL=crtTBLPNL(ptemp,names,5,1,1,1,1,wid,new int[]{0}) ;
		tblPSDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dcePSDTL=new DefaultCellEditor(cmbPSDTL);
		dcePSDTL.setClickCountToStart(0);
		tblPSDTL.getColumn(tblPSDTL.getColumnName(2)).setCellEditor(dcePSDTL);
		tblPSDTL.clrTABLE();
		ptemp.setBorder(BorderFactory.createTitledBorder(" PS Details "));
		setMatrix(18,7);//LINE 13
		add(ptemp,13,1,4,4,this,'L');
		ptemp=new JPanel(new BorderLayout());
		setMatrix(1,1);
//CREATING TABLE FOR FEEDER QTY DETAILS
		names=new String[]{"FL","Feeder No.","Quantity","Percentage"};
		wid=new int[]{20,95,100,100};
		tblFDRQT=(cl_JTBL)crtTBLPNL(ptemp,names,4,1,1,1,1,wid,new int[]{0}) ;
		tblFDRQT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		txtFDRQT=new TxtNumLimit(7.3);txtFDRQT.addActionListener(this);
		tblFDRQT.setCellEditor(2,txtFDRQT);
		ptemp.setBorder(BorderFactory.createTitledBorder(" Feeder wise Quantity "));
		setMatrix(18,7);//LINE 13
		add(ptemp,13,5,4,3,this,'L');
		setENBL(false);
	}
	/**<b>TASKS : </B>
	 * <br>&nbsp&nbsp&nbsp&nbspcmbLINNO : exeENQRY() - Displays details of recent batch in the line in current run	 * <br>&nbsp&nbsp&nbsp&nbspcmbbtnPRINT : Print receipe for the batch displayed	 * <br>&nbsp&nbsp&nbsp&nbspcmbtxtPWDFD : Calculate batch composition	 * <br>&nbsp&nbsp&nbsp&nbspcmbtxtFDRQT : Claculate Feeder Quantiies and ratios	 * <br>&nbsp&nbsp&nbsp&nbspcmbbtnSTFNL : exeSTFNL()	 * <br>&nbsp&nbsp&nbsp&nbspcmbbtnRFRCPN : Cancel new run generation	 * <br>&nbsp&nbsp&nbsp&nbspcmbbtnRFRCPY : Use selected recipe as starting of new run and display it in the table 	 * <br>&nbsp&nbsp&nbsp&nbspcmbbtnSTFNLN : Calcel finalisation of recipe	 * <br>&nbsp&nbsp&nbsp&nbspcmb btnSTFNLY : Delete all toher trial recipes, Change production flag of run, Change production type of successful trial to P and add it to prime production, Display final recipe.	 * <br>&nbsp&nbsp&nbsp&nbspcmb chbRFRCP : Ensure that, only one recipe is selected	 * <br>&nbsp&nbsp&nbsp&nbspcmb cmbUOMCD : change display unit. Swaps in round on each click	 * <br>&nbsp&nbsp&nbsp&nbspcmb txtGRDCD : Check, if grdcd is edited, conrirm and then exeNWRUN()	 * <br>&nbsp&nbsp&nbsp&nbspcmb txtBATSZ||txtPSPWD||cmbPSPWD : Calculate batch	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Enquiry"))
				{
					txtBATNO.setEnabled(true);
					txtRUNNO.setEnabled(true);cmbPSPWD.setEnabled(true);
					txtRUNNO.requestFocus();txtPWDFD.setEnabled(true);
					txtLOTNO.setEnabled(true);txtPSPWD.setEnabled(true);
					btnPRINT.setEnabled(true);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
					cmbLINNO.requestFocus();
			}
			else if(M_objSOURC==cmbLINNO)
			{//DISPLAYS DETAILS OF RECENT RUN IN SELECTED LINE
				if(cmbLINNO.getSelectedIndex()!=0)
				{
					txtGRDCD.requestFocus();
					int i=cmbLINNO.getSelectedIndex();
					clrCOMP();
					cmbLINNO.setSelectedIndex(i);
					exeENQRY();//DIPLAYS DETAILS OF RECENT RUN IN SELECTED LINE
					txtBATSZ.requestFocus();
				}
				else
				{
					clrCOMP();
				}
			}
			else if(M_objSOURC==btnPRINT)
			{
				exePRNT();
			}
			else if(M_objSOURC==txtPWDFD)
			{
				calBTCH();
				cmbPSPWD.requestFocus();
			}
			else if(M_objSOURC==txtBATNO)
			{
				if(!btnSTFNL.isVisible())
				{
					if(Integer.parseInt(txtBATNO.getText())<=Integer.parseInt(txtTRLCT.getText()))
						setMSG("Recipe of deleted trials cannot be retrieved ..",'E');
					else
					{
						M_strSQLQRY="SELECT * FROM PR_RPMST WHERE RP_RUNNO='"+txtRUNNO.getText()+"' AND RP_GRDCD='"+txtGRDCD.getText()+"'";
						System.out.println(M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
						row=0;
						if(M_rstRSSET!=null)
						{
							double L_BSMQT=0.0;
							while(M_rstRSSET.next())
							{//DISPLAYING DETAILS OF RECIPE FOR REFERENCE RUN SELECTED
								System.out.println("got data");
								tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLCD"),row,1);
								tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLDS"),row,2);
								tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLMF"),row,3);
								tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLBT"),row,4);
								tblRECIP.setValueAt(M_rstRSSET.getString("RP_FDRNO"),row,9);
								if(M_rstRSSET.getString("RP_BSMFL").equals("Y"))
								{
									tblRECIP.setValueAt(new Boolean(true),row,10);
									L_BSMQT+=M_rstRSSET.getDouble("RP_PCTQT");
								}
								else
									tblRECIP.setValueAt(new Boolean(false),row,10);
								tblRECIP.setValueAt(M_rstRSSET.getString("RP_PCTQT"),row++,6);
								
							}
							int i=0;
							while((!tblRECIP.getValueAt(i,1).equals(""))&&i<LM_RCPROW)
							{
								if(tblRECIP.getValueAt(i,10).equals(new Boolean(true)))
								{
									tblRECIP.setValueAt(setNumberFormat(((Double.parseDouble(tblRECIP.getValueAt(i,6).toString())/L_BSMQT)*100.0),5),i,5);
								}
								else
								{
									tblRECIP.setValueAt(setNumberFormat((Double.parseDouble(tblRECIP.getValueAt(i,6).toString())/L_BSMQT*100.0),5),i,5);
								}
								i++;
							}
							M_strSQLQRY="SELECT * FROM PR_BTMST WHERE BT_BATNO="+txtBATNO.getText()+" and BT_GRDCD='"+txtGRDCD.getText()+"' and BT_RUNNO='"+txtRUNNO.getText()+"'";
							M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
							if(M_rstRSSET!=null)
								if(M_rstRSSET.next())
									txtBATSZ.setText(String.valueOf( M_rstRSSET.getDouble("BT_BATSZ")*1000));
							calBTCH();

						}
					}
				}					
			}
			else if(M_objSOURC==txtFDRQT)
			{
				double L_FDR1QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(0,2).toString(),"0.0")),
					L_FDR2QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(1,2).toString(),"0.0")),
					L_FDR3QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(2,2).toString(),"0.0")),
					L_FDR4QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(3,2).toString(),"0.0"));
				tblFDRQT.setValueAt(setNumberFormat(L_FDR1QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),0,3);
				tblFDRQT.setValueAt(setNumberFormat(L_FDR2QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),1,3);
				tblFDRQT.setValueAt(setNumberFormat(L_FDR3QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),2,3);
				tblFDRQT.setValueAt(setNumberFormat(L_FDR4QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),3,3);
			}
/*			else if(M_objSOURC==btnRCLQTY)
			{
//CODE TO PUT TOTAL RECYCLE QUANTITY IN txtPSRCL AND TO PUT RELATED DETAILS IN htbRCLQT				
				Boolean TRUE=new Boolean(true);
				htbRCLQT=new Hashtable(5,1);
				RCLQT=0.0;
				tblRCLQT.setRowSelectionInterval(0,1);
				tblRCLQT.setColumnSelectionInterval(0,1);
				tblRCLQT.editCellAt(0,0);
				dlgRCLQT.dispose();
				
				for(int i=0;i<tblRCLQT.getRowCount();i++)
				{
					if(tblRCLQT.getValueAt(i,0).equals(TRUE))
					{
						double REQQT=Double.parseDouble(nvlSTRVL(tblRCLQT.getValueAt(i,4).toString(),"0.0"));
						double AVLQT=Double.parseDouble(tblRCLQT.getValueAt(i,3).toString());
						if(REQQT>AVLQT)
						{
							JOptionPane.showConfirmDialog(dlgRCLQT,"Value of material to be recycled should not be greater thatn available quantity","Wrong Quantity",0,JOptionPane.ERROR_MESSAGE);
							RCLQT=0.0;
							htbRCLQT.clear();
							break;
						}
						else
						{
							RCLQT+=REQQT;
							htbRCLQT.put(Integer.toString(i),tblRCLQT.getValueAt(i,1).toString()+"|"+tblRCLQT.getValueAt(i,4).toString()+"|"+tblRCLQT.getValueAt(i,2).toString()+"|"+tblRCLQT.getValueAt(i,5).toString());
						}
					}
				}
				txtPSRCL.setText(Double.toString(RCLQT));
				tblRCLQT=null;
				srpRCLQT=null;
			}
			else if(M_objSOURC==btnRCLQTN)
			{
				dlgRCLQT.dispose();
				tblRCLQT=null;
				srpRCLQT=null;
			}
*/			else if(M_objSOURC==btnSTFNL)
			{
				exeSTFNL();//SET FINAL RECIPE FOR CURRENT RUN
			}
//CODE RELATED TO SELECTION OF BASE RECIPE FOR NEW RUN ******
			else if(M_objSOURC==btnRFRCPN)
			{
				dlgRFRCP.dispose();
				tblRFRCP=null;
				txtBATSZ.requestFocus();
			}
			else if(M_objSOURC==btnRFRCPY)
			{
				String L_STRTMP="";
				Boolean b=new Boolean(true);
				for(int i=0;i<tblRFRCP.getRowCount();i++)
				{
					if(tblRFRCP.getValueAt(i,0).equals((Object)(b)))
					{
						L_STRTMP=(String)tblRFRCP.getValueAt(i,2);
						break;
					}
				}
				dlgRFRCP.dispose();
//				tblRFRCP=null;
				M_rstRSSET.close();
				M_strSQLQRY="SELECT * FROM pr_RPMST WHERE RP_RUNNO='"+L_STRTMP+"' AND RP_GRDCD='"
					+txtGRDCD.getText()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				txtBTACT.setText("");
				row=0;
				((cl_JTBL)tblRECIP).clrTABLE();
				((cl_JTBL)tblRECIP).setEnabled(true);
				((cl_JTBL)tblRECIP).cmpEDITR[2].setEnabled(false);
				((cl_JTBL)tblRECIP).cmpEDITR[6].setEnabled(false);
				((cl_JTBL)tblRECIP).cmpEDITR[7].setEnabled(false);
				if(M_rstRSSET!=null)
				{
					double L_BSMQT=0.0;
					while(M_rstRSSET.next())
					{//DISPLAYING DETAILS OF RECIPE FOR REFERENCE RUN SELECTED
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLCD"),row,1);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLDS"),row,2);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLMF"),row,3);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLBT"),row,4);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_FDRNO"),row,9);
						if(M_rstRSSET.getString("RP_BSMFL").equals("Y"))
						{
							tblRECIP.setValueAt(new Boolean(true),row,10);
							L_BSMQT+=M_rstRSSET.getDouble("RP_PCTQT");
						}
						else
							tblRECIP.setValueAt(new Boolean(false),row,10);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_PCTQT"),row++,6);
						
					}
					int i=0;
					while((!tblRECIP.getValueAt(i,1).equals(""))&&i<LM_RCPROW)
					{
						if(tblRECIP.getValueAt(i,10).equals(new Boolean(true)))
						{
							tblRECIP.setValueAt(setNumberFormat(((Double.parseDouble(tblRECIP.getValueAt(i,6).toString())/L_BSMQT)*100.0),8),i,5);
						}
						else
						{
							tblRECIP.setValueAt(setNumberFormat((Double.parseDouble(tblRECIP.getValueAt(i,6).toString())/L_BSMQT*100.0),8),i,5);
						}
						i++;
					}
				}
			}
			else if(M_objSOURC==btnSTFNLN)
			{
				dlgRFRCP.dispose();
				tblRFRCP=null;
				txtBATSZ.requestFocus();
			}
			else if(M_objSOURC==btnSTFNLY)
			{//SET FINAL RECIPE FOR THE RUN
				String L_STRTMP="";
				Boolean b=new Boolean(true);
				for(int i=0;i<tblRFRCP.getRowCount();i++)
				{
					if(tblRFRCP.getValueAt(i,0).equals((Object)(b)))
					{
						L_STRTMP=(String)tblRFRCP.getValueAt(i,2);
						break;
					}
				}
				dlgRFRCP.dispose();
				tblRFRCP=null;
				M_rstRSSET.close();
				M_strSQLQRY="DELETE FROM pr_RPMST WHERE RP_RUNNO='"+txtRUNNO.getText()
					+"' and rp_batno<>"+L_STRTMP;//RECIPE OF FAILED TRIAL DELETED
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				txtBTACT.setText("1");
				((cl_JTBL)tblRECIP).setEnabled(true);
				txtTRLCT.setText(Integer.toString(Integer.parseInt(nvlSTRVL(txtTRLCT.getText(),"0"))-1));
				M_strSQLQRY="UPDATE pr_RNMST SET RN_TOTPM= (SELECT BT_BATSZ FROM pr_BTMST WHERE BT_RUNNO='"+txtRUNNO.getText()+"' AND BT_GRDCD='"
					+txtGRDCD.getText()+"' AND BT_BATNO= "+L_STRTMP+"),RN_DCLPM=RN_TOTPM WHERE RN_RUNNO='"+txtRUNNO.getText()
					+"' and RN_GRDCD='"+txtGRDCD.getText()+"'";//DECLARE PRODUCTION UNDER SUCCESSFUL TRIAL AS PRIME PRODUCTION
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				M_strSQLQRY="UPDATE pr_RNMST SET rn_trlfl='P',RN_WSTQT= (SELECT sum(BT_BATSZ) FROM pr_BTMST WHERE BT_RUNNO='"+txtRUNNO.getText()+"' AND BT_GRDCD='"
					+txtGRDCD.getText()+"' AND BT_BATNO!= "+L_STRTMP+") WHERE RN_RUNNO='"+txtRUNNO.getText()
					+"' and RN_GRDCD='"+txtGRDCD.getText()+"'";//DECLARE PRODUCTION UNDER FAILED TRIAL AS WASTE GENERATED
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				//M_strSQLQRY="update pr_rnmst set  where rn_runno='"+txtRUNNO.getText()+"' AND RN_GRDCD='"+txtGRDCD.getText()+"'";
				//cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");//SET  FLAG
				((cl_JTBL)tblRECIP).setEnabled(false);
				cl_dat.exeDBCMT("set Final Recipe");
				row=0;
				M_strSQLQRY="SELECT * FROM pr_RPMST WHERE RP_RUNNO='"+txtRUNNO.getText()
					+"'AND RP_GRDCD='"+txtGRDCD.getText()+"'";//SELECT RECIPE OF SUCCESSFUL TRIAL
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				((cl_JTBL)tblRECIP).clrTABLE();
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{//DISPLAYING FINALISED RECIPE
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLCD"),row,1);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLDS"),row,2);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLMF"),row,3);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLBT"),row,4);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_FDRNO"),row,9);
						if(M_rstRSSET.getString("RP_BSMFL").equals("Y"))
							tblRECIP.setValueAt(new Boolean(true),row,10);
						else
							tblRECIP.setValueAt(new Boolean(false),row,10);
						tblRECIP.setValueAt(M_rstRSSET.getString("RP_PCTQT"),row++,6);
					}
				}
				btnSTFNL.setVisible(false);
			}
			else if(M_objSOURC==chbRFRCP)
			{
				row=tblRFRCP.getSelectedRow();
				Boolean b=new Boolean(false);
				for(int i=0;i<tblRFRCP.getRowCount();i++)
				{
					if(row!=i||tblRFRCP.getValueAt(i,2).equals((Object)""))
						tblRFRCP.setValueAt(b,i,0);
				}
			}
			//*********************************TO CHANGE DISPLAY QTY UNIT
			else if(M_objSOURC==cmbUOMCD)
			{
				row=tblRECIP.getSelectedRow();
				if(row!=-1)
				{
					//if(cmbUOMCD.getSelectedItem().toString().equals("MT"))
					if(tblRECIP.getValueAt(row,8).toString().equals("MT"))
					{
						//Double d=new Double(LM_ARQTY[row]*LM_KGTMT);
						NumberFormat nf1=NumberFormat.getNumberInstance();
						nf1.setMaximumFractionDigits(8);
						tblRECIP.setValueAt(setNumberFormat(LM_ARQTY[row]*LM_KGTMT,8),row,7);
					}
					else if(tblRECIP.getValueAt(row,8).toString().equals("Kg"))
					{
						Double d=new Double(LM_ARQTY[row]);
						NumberFormat nf1=NumberFormat.getNumberInstance();
						nf1.setMaximumFractionDigits(8);
						tblRECIP.setValueAt(setNumberFormat(LM_ARQTY[row],8),row,7);
					}
					else if(tblRECIP.getValueAt(row,8).toString().equals("gm"))
					{
						Double d=new Double(LM_ARQTY[row]*LM_KGTGM);
						NumberFormat nf1=NumberFormat.getNumberInstance();
						nf1.setMaximumFractionDigits(8);
						tblRECIP.setValueAt(setNumberFormat(LM_ARQTY[row]*LM_KGTGM,8),row,7);
					}
				}
			}
			else if(M_objSOURC==txtGRDCD)
			{//CHECKS WHETHER GRADE CODE HAS BEEN EDITED. IF YES, CONFIRMS AND THEN PROCEEDS FOR NEW RUN.
				if(!txtGRDCD.getText().equals(LM_GRDCD))
				{
					int option=JOptionPane.showConfirmDialog(null,"Do you want to schedule new Run ?","Confirm ..",JOptionPane.YES_NO_OPTION);
					requestFocus();
					if(option!=1)
					{
						exeNWRUN();//STARTING NEW RUN
					}
				}
			}
			else if(M_objSOURC==txtBATSZ)//CALCULATE BATCH QUANTITIES
			{
				calBTCH();
			}
			else if(M_objSOURC==txtPSPWD)//CALCULATE FEEDER WISE QUANTITIES
			{
				calFDRQT();
				txtPWDFD.requestFocus();
			}
			
			else if(M_objSOURC==cmbPSPWD)
			{
				calBTCH();
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else
			{
				setFOCUS(M_objSOURC);//FOR FOCUS NEVIGATION
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}catch(Exception e)
		{
			System.out.println("Error in PR_MERPM ActionPerformed " +e);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/** Calculates feeder wise quantity	 */
	private void calFDRQT()
	{
		String L_STRTMP="";
		for(int i=0;i<tblFDRQT.getRowCount();i++)
			tblFDRQT.setValueAt("0.0",i,2);
		int L_FDRNO=0;
		for(int i=0;i<LM_RCPROW;i++)
		{
			if(!tblRECIP.getValueAt(i,1).toString().equals(""))
			{
				L_FDRNO=Integer.parseInt(nvlSTRVL(tblRECIP.getValueAt(i,9).toString(),"0"));
				if(L_FDRNO==0) L_FDRNO=1;
				tblFDRQT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(L_FDRNO-1,2).toString(),"0.0"))+LM_ARQTY[i],3),L_FDRNO-1,2);
			}
		}
	//ADDING RECYCLE QTY TO FEEDER QTY
	/*	L_FDRNO=Integer.parseInt(nvlSTRVL(txtRCLFD.getText(),"0"));
		if(L_FDRNO==0) L_FDRNO=1;
		tblFDRQT.setValueAt(Double.toString(Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(L_FDRNO-1,2).toString(),"0.0"))+Double.parseDouble(nvlSTRVL(txtPSRCL.getText(),"0.0"))),L_FDRNO-1,2);
	*///ADDING RECYCLE QTY TO FEEDER QTY
		L_FDRNO=Integer.parseInt(nvlSTRVL(txtPWDFD.getText(),"0"));
		if(L_FDRNO==0) L_FDRNO=1;
		tblFDRQT.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(L_FDRNO-1,2).toString(),"0.0"))+Double.parseDouble(nvlSTRVL(txtPSPWD.getText(),"0.0")),3),L_FDRNO-1,2);
	//CONVERTING QTY TO % RATIOS
		double L_FDR1QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(0,2).toString(),"0.0")),
			L_FDR2QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(1,2).toString(),"0.0")),
			L_FDR3QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(2,2).toString(),"0.0")),
			L_FDR4QT=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(3,2).toString(),"0.0"));
		tblFDRQT.setValueAt(setNumberFormat(L_FDR1QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),0,3);
		tblFDRQT.setValueAt(setNumberFormat(L_FDR2QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),1,3);
		tblFDRQT.setValueAt(setNumberFormat(L_FDR3QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),2,3);
		tblFDRQT.setValueAt(setNumberFormat(L_FDR4QT/(L_FDR1QT+L_FDR2QT+L_FDR3QT+L_FDR4QT)*100.0,2),3,3);
	}
	/** To schedule new run <br>shows reference receipes and selects one 	 */
	private void exeNWRUN()
	{
		btnSTFNL.setVisible(true);
		txtRNSTM.setText("");txtRNSDT.setText("");txtPRTQT.setText("");txtPSPWD.setText("");txtTRLCT.setText("1");
		txtBTACT.setText("1");txtPRAQT.setText("");txtBATNO.setText("1");txtRNETM.setText("");txtRNEDT.setText("");
		M_rstRSSET=null;
//GET OLD RUNS OF SAME GRADE, IRRESPECTIVE OF LINE NO.
		M_strSQLQRY="SELECT * FROM pr_RPMST WHERE RP_GRDCD='"+txtGRDCD.getText()+"' order by RP_RUNNO";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
		try
		{
			if(M_rstRSSET.next())
			{
				setMatrix(18,4);
				ResultSet L_RSSET;
//GET LIST OF RAW MATERIALS USED IN PREVIOUS RUNS				
				M_strSQLQRY="SELECT DISTINCT RP_MTLCD FROM pr_RPMST WHERE "
					+"RP_GRDCD='"+txtGRDCD.getText()+"'";
				L_RSSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
				Vector vtrMTLCD=new Vector(10,2);
				if(L_RSSET!=null)
				{
					while(L_RSSET.next())
					{
						vtrMTLCD.addElement(L_RSSET.getString(1));
					}
				}
				String[] names=new String[vtrMTLCD.size()+3];
				int[] wid=new int[vtrMTLCD.size()+3];
				chbRFRCP=new JCheckBox();
				chbRFRCP.addKeyListener(this);
				chbRFRCP.addActionListener(this);
				names[1]="Date";
				names[0]="FL";
				names[2]="Run No.";
				wid[0]=30;
				wid[1]=75;
				wid[2]=30;
//SET COL HDR TO RAW MATERIALS NAME USING VCTOR				
				for(int i=0;i<vtrMTLCD.size();i++)
				{
					names[i+3]=((String)vtrMTLCD.elementAt(i));
					wid[i+3]=50;
				}
				int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
				int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
				tblRFRCP=((cl_JTBL)crtTBLPNL(pnlRECIP=new JPanel(null),names,10,1,1,4,4,wid,new int[]{0}));
				tblRFRCP.setCellEditor(0,chbRFRCP);
				tblRFRCP.addKeyListener(this);
				String L_STRTMP="";
				int L_RPPOS=-1,L_LINNO=0;
				boolean L_EOF=true;
//PUT PERCENTAGE RECIPE FOR EACH RUN				
				while(L_EOF)
				{
					L_STRTMP=nvlSTRVL(M_rstRSSET.getString("RP_RUNNO"),"");
					if(!L_STRTMP.equals(""))
						for(int i=0;i<5;i++)
						{
							if(((String)tblRFRCP.getValueAt(i,2)).equals(L_STRTMP))
							{
								L_RPPOS=i;//POSSITION OF THE LINE IF RUN NO ALREAD ADDED TO TBL.
								break;
							}
						}
					if(L_RPPOS==-1)
					{
						tblRFRCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("RP_RUNNO"),""),L_LINNO,2);
					}
//FIND OUT COL TO ADD THE VALUE					
					for(int i=0;i<names.length;i++)
					{
						if(nvlSTRVL(M_rstRSSET.getString("RP_MTLCD"),"").equals(names[i]))
						{
							if(L_RPPOS==-1)
								tblRFRCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("RP_PCTQT"),""),L_LINNO++,i);
							else
								tblRFRCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("RP_PCTQT"),""),L_RPPOS,i);
							L_RPPOS=-1;
							break;
						}
					}
					if(!M_rstRSSET.next())
						L_EOF=false;
				}
				if(!L_EOF)
				{
					tblRFRCP.setEnabled(false);
					chbRFRCP.setEnabled(true);
					dlgRFRCP=new JDialog();
					btnRFRCPY=new JButton("Ok");btnRFRCPN=new JButton("Cancel");
					btnRFRCPY.addActionListener(this);btnRFRCPN.addActionListener(this);
					dlgRFRCP.getContentPane().setLayout(null);
					dlgRFRCP.getContentPane().add(pnlRECIP);
					JPanel p_temp=new JPanel();
					p_temp.add(btnRFRCPY);
					p_temp.add(btnRFRCPN);
					dlgRFRCP.getContentPane().add(p_temp);//btnRFRCPY);
					dlgRFRCP.setBounds(0,100,800,400);
					pnlRECIP.setBounds(10,10,800,300);
					p_temp.setBounds(10,310,800,100);
					dlgRFRCP.toFront();
					dlgRFRCP.show();
					dlgRFRCP.setTitle("Select Recipe");
				}
			}
			else
			{
				((cl_JTBL)tblRECIP).clrTABLE();
			}
			if(M_rstRSSET != null)
					M_rstRSSET.close();	
//GENERATING RUN NUMBER
	//		if(tblRFRCP!=null)
	//		{
				String L_STRTMP=cmbLINNO.getSelectedItem().toString().substring(7)+cl_dat.M_strLOGDT_pbst.substring(9);
				System.out.println(L_STRTMP);
				M_strSQLQRY="SELECT MAX(RN_RUNNO),count(rn_runno) FROM pr_RNMST WHERE RN_GRDCD='"+txtGRDCD.getText()+"' AND RN_RUNNO LIKE '"+L_STRTMP+"%'";
				//SELECT MAX(RN_RUNNO) FROM spltest/pr_RNMST WHERE RN_GRDCD='5212157690' AND RN_RUNNO LIKE '523%'"
				M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString(1)!=null)
						txtRUNNO.setText(L_STRTMP+(padSTRING('L',Integer.toString((Integer.parseInt(M_rstRSSET.getString(1).substring(3))+1)),5)).replace(' ','0'));
					else
					{
						txtRUNNO.setText(L_STRTMP+"00001");
						setMSG("First Run for the Grade ..",'N');
						txtTRLCT.setText("1");
					}
				}
				else
				{
					System.out.println("2-2");
					txtRUNNO.setText(L_STRTMP+"00001");
					setMSG("First Run for the Grade ..",'N');
					txtTRLCT.setText("1");
				}
	/*		}
			else
			{
				txtRUNNO.setText(cmbLINNO.getSelectedItem().toString().substring(7)+cl_dat.M_strLOGDT_pbst.substring(9)+"00001");
				setMSG("First Run for the Grade ..",'N');
				txtTRLCT.setText("1");
			}
	*/		((cl_JTBL)tblRECIP).setEnabled(true);
			((cl_JTBL)tblRECIP).cmpEDITR[0].setEnabled(false);
			((cl_JTBL)tblRECIP).cmpEDITR[2].setEnabled(false);
			((cl_JTBL)tblRECIP).cmpEDITR[6].setEnabled(false);
			((cl_JTBL)tblRECIP).cmpEDITR[7].setEnabled(false);
			txtRNSDT.requestFocus();
		}catch(Exception e)
		{
			setMSG(e,"EXENWRUN");
		}
	}
	/** To set final receipe<br>Displays receipies of trials carried out in the run. 
	 * <br>Marks selected as final and delettes other from backend permenantly
	 * <br>Changes production flag for the run to 'P' from 'T'	 */
	private void exeSTFNL()
	{
		M_rstRSSET=null;
//GET DETAILS OF ALL TRIALS IN THE RUN
		M_strSQLQRY="SELECT * FROM pr_RPMST WHERE RP_RUNNO='"+txtRUNNO.getText()+"' AND RP_GRDCD='"+txtGRDCD.getText()+"' order by RP_BATNO";
		//SELECT * FROM spltest/pr_RPMST WHERE RP_RUNNO='51300001' AND RP_GRDCD='6711020051' order by RP_BATNO
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
		try
		{
			if(M_rstRSSET.next())
			{
				setMatrix(18,4);
				ResultSet L_RSSET;
//GET LIST OF RAW MATERIALS USED IN PREVIOUS TRIALS
				M_strSQLQRY="SELECT DISTINCT RP_MTLCD FROM pr_RPMST WHERE "
					+"RP_RUNNO='"+txtRUNNO.getText()+"' and RP_GRDCD='"+txtGRDCD.getText()+"'";
				L_RSSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
				Vector vtrMTLCD=new Vector(10,2);
				if(L_RSSET!=null)
				{
					while(L_RSSET.next())
					{
						vtrMTLCD.addElement(L_RSSET.getString(1));
					}
				}
				String[] names=new String[vtrMTLCD.size()+3];
				int[] wid=new int[vtrMTLCD.size()+3];
				chbRFRCP=new JCheckBox();
				chbRFRCP.addKeyListener(this);
				chbRFRCP.addActionListener(this);
				names[1]="Date";
				names[0]="FL";
				names[2]="Trial No.";
				wid[0]=30;
				wid[1]=75;
				wid[2]=30;
//SET COL HDR TO RAW MATERIALS NAME USING VECTOR				
				for(int i=0;i<vtrMTLCD.size();i++)
				{
					names[i+3]=((String)vtrMTLCD.elementAt(i));
					wid[i+3]=50;
				}
				int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
				int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
				tblRFRCP=((cl_JTBL)crtTBLPNL(pnlRECIP=new JPanel(null),names,15,1,1,8,4,wid,new int[]{0}));
				tblRFRCP.setCellEditor(0,chbRFRCP);
				tblRFRCP.addKeyListener(this);
				String L_STRTMP="";
				int L_RPPOS=-1,L_LINNO=0;
				boolean L_EOF=true;
//PUT PERCENTAGE RECIPE FOR EACH RUN				
				while(L_EOF)
				{
					L_STRTMP=nvlSTRVL(M_rstRSSET.getString("RP_BATNO"),"");
					if(!L_STRTMP.equals(""))
						for(int i=0;i<10;i++)
						{
							if(((String)tblRFRCP.getValueAt(i,2)).equals(L_STRTMP))
							{
								L_RPPOS=i;//POSSITION OF THE LINE IF RUN NO ALREAD ADDED TO TBL.
								break;
							}
						}
					if(L_RPPOS==-1)
					{
						tblRFRCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("RP_BATNO"),""),L_LINNO,2);
					}
//FIND OUT COL TO ADD THE VALUE					
					for(int i=0;i<names.length;i++)
					{
						if(nvlSTRVL(M_rstRSSET.getString("RP_MTLCD"),"").equals(names[i]))
						{
							if(L_RPPOS==-1)
								tblRFRCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("RP_PCTQT"),""),L_LINNO++,i);
							else
								tblRFRCP.setValueAt(nvlSTRVL(M_rstRSSET.getString("RP_PCTQT"),""),L_RPPOS,i);
							L_RPPOS=-1;
							break;
						}
					}
					if(!M_rstRSSET.next())
						L_EOF=false;
				}
				if(!L_EOF)
				{
					tblRFRCP.setEnabled(false);
					chbRFRCP.setEnabled(true);
					dlgRFRCP=new JDialog();
					btnSTFNLY=new JButton("Ok");btnSTFNLN=new JButton("Cancel");
					btnSTFNLY.addActionListener(this);btnSTFNLN.addActionListener(this);
					dlgRFRCP.getContentPane().setLayout(null);
					dlgRFRCP.getContentPane().add(pnlRECIP);
					JPanel p_temp=new JPanel();
					p_temp.add(btnSTFNLY);
					p_temp.add(btnSTFNLN);
					dlgRFRCP.getContentPane().add(p_temp);//btnRFRCPY);
					dlgRFRCP.setBounds(0,100,800,400);
					pnlRECIP.setBounds(10,10,800,300);
					p_temp.setBounds(10,310,800,100);
					dlgRFRCP.setTitle("Select Recipe");
					dlgRFRCP.show();
					dlgRFRCP.toFront();
					
				}
			}
			if(M_rstRSSET != null)
					M_rstRSSET.close();	
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	/** calculates batch composition	 */
	private void calBTCH()//TO CALCULATE BATCH QUANTITIES
	{
		((cl_JTBL)tblRECIP).dceEDITR[tblRECIP.getSelectedColumn()].stopCellEditing();
		tblRECIP.setRowSelectionInterval(0,0);
		tblRECIP.setColumnSelectionInterval(0,0);
		boolean flag=true;
//VALIDATING FEEDER NO.s
		for(int i=0;i<LM_RCPROW;i++)
		{
			if(!tblRECIP.getValueAt(i,1).toString().equals(""))
			{
				if(tblRECIP.getValueAt(i,9).toString().equals(""))
				{
					setMSG("Enter Feeder no.s ..",'E');
					 
					flag=false;
				}
				else if(Integer.parseInt(tblRECIP.getValueAt(i,9).toString())>4)
				{
					setMSG("Feeder no. should be less than 5 ..",'E');
					 
					flag=false;
				}
			}
		}
		if(flag)
		{
			if(!(Double.parseDouble(nvlSTRVL(txtBATSZ.getText(),"0.0"))>0.0))
			{
				setMSG("Batch Size cannot be zero or less ..",'E');
				 
				txtBATSZ.requestFocus();
				flag=false;
			}
			else
			{
				for(int i=0;i<4;i++)
					tblFDRQT.setValueAt("FEEDER No. : "+Integer.toString(i+1),i,1);
				double [][] L_MTLQT=new double[LM_RCPROW][2];
				double total=0.0;
				Boolean TRUE=new Boolean(true);
				for(int i=0;i<LM_RCPROW;i++)
				{
					if((!tblRECIP.getValueAt(i,1).toString().equals(""))&&tblRECIP.getValueAt(i,10).equals(TRUE))
					{
						total+=Double.parseDouble(tblRECIP.getValueAt(i,5).toString());
					}
				}
				if(total>100.00001||total<99.99999)
				{
					setMSG("Base material PHR should be 100 ..",'E');
					 
				}
				else
				{//ALL DATA VALIDATED. NOW CALCUATING COMPOSITION
					total=0.0;
					for(int i=0;i<LM_RCPROW;i++)
					{
						if(!tblRECIP.getValueAt(i,1).toString().equals(""))
						{
							total+=Double.parseDouble(tblRECIP.getValueAt(i,5).toString());
						}
					}
					String L_STRTMP="";
					for(int i=0;i<LM_RCPROW;i++)
					{
						if(!tblRECIP.getValueAt(i,1).toString().equals(""))
						{
							double pct=Double.parseDouble(tblRECIP.getValueAt(i,5).toString())/total*100.0;
							NumberFormat nf1=NumberFormat.getNumberInstance();
							nf1.setMaximumFractionDigits(8);
							tblRECIP.setValueAt(setNumberFormat(pct,8),i,6);
							L_MTLQT[i][0]=(Double.parseDouble(tblRECIP.getValueAt(i,5).toString())/total)*100.0;
							double f=pct;
						}
					}
					total=0.0;
					for(int i=0;i<LM_RCPROW;i++)
					{
						if(!tblRECIP.getValueAt(i,1).toString().equals(""))
						{
//							L_MTLQT[i][0]=Double.parseDouble(tblRECIP.getValueAt(i,6).toString());
							L_MTLQT[i][1]=Double.parseDouble(tblRECIP.getValueAt(i,1).toString());
							total+=L_MTLQT[i][0];
						}
					}
					total=(Double.parseDouble(nvlSTRVL(txtBATSZ.getText().trim(),"0")))/1000.0;
					for(int i=0;i<LM_RCPROW;i++)
					{
						if(!tblRECIP.getValueAt(i,1).toString().equals(""))
						{
							double d1=((total/100.0)*L_MTLQT[i][0]*1000.0);
							double l1=(Math.round(d1*100000000.0))/100000.0;
							l1=l1/1000.0;
							/*if(d1<0.0001)
							{
								tblRECIP.setValueAt(new Double(d1*1000.0).toString(),i,7);
								tblRECIP.setValueAt("gm",i,8);
							}
							else*/
							{
								NumberFormat nf1=NumberFormat.getNumberInstance();
							nf1.setMaximumFractionDigits(8);
							
								tblRECIP.setValueAt(setNumberFormat(l1,8),i,7);
								tblRECIP.setValueAt("kg",i,8);
							}
							LM_ARQTY[i]=l1;
						}
						else
						{
							tblRECIP.setValueAt("",i,5);
							tblRECIP.setValueAt("",i,6);
							tblRECIP.setValueAt("",i,7);
							tblRECIP.setValueAt("",i,8);
						}
					}
					String s1=Double.toString((Double.parseDouble(nvlSTRVL(txtPRTQT.getText(),"0"))-Double.parseDouble(nvlSTRVL(txtPRAQT.getText(),"0")))/(Double.parseDouble(nvlSTRVL(txtBATSZ.getText(),"0"))/1000.0));
					if(s1.length()>5)
						s1=s1.substring(0,5);
					txtBTTCT.setText(s1);
					//***********
				if(cmbPSPWD.getSelectedIndex()!=0)
				{
				
					for(int i=0;i<LM_RCPROW;i++)
					{
						if(tblRECIP.getValueAt(i,1).toString().length()>0)
						{
							if(tblRECIP.getValueAt(i,2).toString().equals(cmbPSPWD.getSelectedItem().toString()))
							{
								double d=Double.parseDouble(tblRECIP.getValueAt(i,7).toString())-Double.parseDouble(nvlSTRVL(txtPSPWD.getText(),"0.0"));
								if(d<0.0)
								{
									setMSG("PS Powder qty. cannot be greater than base material qty ..",'E');
									 
								}
								else
								{
									tblRECIP.setValueAt(Double.toString(d),i,7);
									LM_ARQTY[i]=d;
								}
								break;
							}
						}
					}
					calFDRQT();
				}
				
				//****************
//CALCULATING FEEDER WISE QTY.
					calFDRQT();
				}
			}
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int key=L_KE.getKeyCode();
		if(M_objSOURC!=cmbLINNO)
			setMSG("",'N');
		try
		{
		if(M_objSOURC==txtGRDCD&&key==L_KE.VK_F1)
		{
			M_strSQLQRY="SELECT PR_PRDDS,PR_PRDCD FROM CO_PRMST ORDER BY PR_PRDDS";
			M_strHLPFLD="txtGRDCD";
			cl_hlp(M_strSQLQRY ,1,1,new String[] {"Grade Name","Grade Code"},2,"CT");
		}
		else if(M_objSOURC==((cl_JTBL)tblRECIP).cmpEDITR[1]&&key==L_KE.VK_F1)
		{
			M_strSQLQRY="SELECT CT_MATCD,CT_MATDS FROM CO_CTMST where ct_grpcd='68' or ct_grpcd='51' ORDER BY CT_MATCD";
			M_strHLPFLD="cmpEDITR[1]";
			cl_hlp(M_strSQLQRY ,2,1,new String[] {"Material Code","Material Description"},2,"CT");
		}
		else if(M_objSOURC==txtRUNNO&&key==L_KE.VK_F1)
		{
			M_strSQLQRY="SELECT RN_RUNNO,RN_GRDDS,RN_RUNST,RN_TRLFL,RN_TRLCT,RN_BATCT,RN_GRDCD FROM PR_RNMST ORDER BY RN_RUNST DESC";
			M_strHLPFLD="txtRUNNO";
			cl_hlp(M_strSQLQRY,2,1,new String[] {"Run no","Grade Name","Run Start","Production Type","Trial Count","Total Batches","Grade Code"},7,"CT");
		}
//FETCHING DETAILS OF SCRAP AVAILABLE //************CODE COMMENTED AS PER USER REQUEST.
/*		else if(M_objSOURC==txtPSRCL&&key==L_KE.VK_F1)
		{
			//exeRCLQT();
			String[] names=new String[]{"FL","Run Number","Grade","Available Quantity","Recycle Quantity","Grade Code"};
			int[] wid=new int[]{20,100,150,100,75,75};
			JPanel pnlTEMP=new JPanel();
			pnlRCLQT=new JPanel(null);
			int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			tblRCLQT=(cl_JTBL)crtTBLPNL(pnlRCLQT,names,LM_RCPROW,0,25,M_intCOLWD*4,150,wid,new int[]{0,9}) ;
			tblRCLQT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			srpRCLQT=new JScrollPane(tblRCLQT,v1,h1);
//FETCHING DETAILS OF RUNWISE WASTE MATERIAL 			
			M_strSQLQRY="SELECT RN_RUNNO,RN_GRDDS,RN_WAVQT,RN_GRDCD FROM pr_RNMST where RN_WAVQT>0 ";
			M_objSOURC = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_objSOURC!=null)
			{
				int i=0;
				while(M_rstRSSET.next())
				{
					tblRCLQT.setValueAt(M_rstRSSET.getString("RN_RUNNO"),i,1);
					tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDDS"),i,2);
					tblRCLQT.setValueAt(M_rstRSSET.getString("RN_WAVQT"),i,3);
					tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDCD"),i,5);
					i++;
				}
				dlgRCLQT=new JDialog();
				pnlTEMP.add(btnRCLQTY=new JButton("Yes"));
				pnlTEMP.add(btnRCLQTN=new JButton("Cancel"));
				dlgRCLQT.getContentPane().setLayout(null);
				dlgRCLQT.getContentPane().add(srpRCLQT);
				dlgRCLQT.getContentPane().add(pnlTEMP);
				srpRCLQT.setBounds(50,30,500,200);
				pnlTEMP.setBounds(50,250,500,100);
				dlgRCLQT.setBounds(100,100,600,400);
				dlgRCLQT.show();
				dlgRCLQT.toFront();
				btnRCLQTY.addActionListener(this);
				btnRCLQTN.addActionListener(this);
				tblRCLQT.setEnabled(false);
				tblRCLQT.setCellEditor(4,new TxtNumLimit(6.3));
				tblRCLQT.cmpEDITR[4].setEnabled(true);
			}
		}
*/		else if(M_objSOURC==chbRFRCP&&L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			String L_STRTMP="";
			Boolean b=new Boolean(true);
			for(int i=0;i<tblRFRCP.getRowCount();i++)
			{
				if(tblRFRCP.getValueAt(i,0).equals((Object)(b)))
				{
					L_STRTMP=(String)tblRFRCP.getValueAt(i,2);
					break;
				}
			}
			dlgRFRCP.dispose();
//			tblRFRCP=null;
			M_rstRSSET.close();
			M_strSQLQRY="SELECT * FROM pr_RPMST WHERE RP_RUNNO='"+L_STRTMP+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			row=0;
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLCD"),row,1);
					tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLDS"),row,2);
					tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLMF"),row,3);
					tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLBT"),row,4);
					tblRECIP.setValueAt(M_rstRSSET.getString("RP_FDRNO"),row,9);
					if(M_rstRSSET.getString("RP_BSMFL").equals("Y"))
						tblRECIP.setValueAt(new Boolean(true),row,10);
					else
						tblRECIP.setValueAt(new Boolean(false),row,10);
					tblRECIP.setValueAt(M_rstRSSET.getString("RP_PCTQT"),row++,6);
				}
			}
		}
		}catch (Exception e)
		{System.out.println(e);}
	}

	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(!cl_dat.M_txtSTAT_pbst.getForeground().equals(Color.red))
		{
			if(M_objSOURC==cmbUOMCD)
			{
				int i=cmbUOMCD.getSelectedIndex();
				if(i>1) i=-1;
				cmbUOMCD.setSelectedIndex(++i);
			}
			else if(M_objSOURC==txtGRDCD)
				setMSG("Enter Grade to be produced. Press 'F1' for list ..",'N');
			else if(M_objSOURC==txtRNSDT)
			{
				if(!cl_dat.M_txtSTAT_pbst.getText().equals("First Run for the Grade .."))
					setMSG("Enter Run Start Date 'dd/mm/yyyy' ..",'N');
				else
					setMSG("First Run for the Grade, Enter Run Start Date 'dd/mm/yyyy' ..",'N');
			}
			else if(M_objSOURC==txtRNSTM)
				setMSG("Enter Run Start Time 'hh:mm' ..",'N');
			else if(M_objSOURC==txtBATSZ)
				setMSG("Enter Batch Size in KG ..",'N');
			else if(M_objSOURC==txtPRTQT)
				setMSG("Enter Target Pirme Production in MT ..",'N');
			else if(M_objSOURC==((cl_JTBL)tblRECIP).cmpEDITR[1])
				setMSG("Enter Material Code, Press 'F1' for list ..",'N');
			else if(M_objSOURC==((cl_JTBL)tblRECIP).cmpEDITR[5])
				setMSG("Enter PHR quantity 'ddd.ddddd' ..",'N');
			else if(M_objSOURC==((cl_JTBL)tblRECIP).cmpEDITR[8])
				setMSG("Select Unit for Display ..",'N');
			else if(M_objSOURC==txtPSPWD)
				setMSG("Enter PS powder qty. in kg ..",'N');
			else if(M_objSOURC==txtLOTNO)
				setMSG("Enter Lot No. ..",'N');
			else if(M_objSOURC==txtCMRNO)
				setMSG("Enter CMR/DWR No. ..",'N');
			else if(M_objSOURC==txtCMRRV)
				setMSG("Enter CMR/DWR Revision No. ..",'N');
			else if(M_objSOURC==cmbCMRTP)
				setMSG("Select Production Request Type ..",'N');
			else if(M_objSOURC==cmbPSPWD)
				setMSG("Select Base Material to be adjusted against PS Powder ..",'N');
			else if(M_objSOURC==cmbLINNO)
				setMSG("Select Line No. ..",'N');
			else if(M_objSOURC==txtPWDFD)
			{
				setMSG("Enter feeder for PS Powder (<5) ..",'N');
				//ADDING BASE MATERIAL GRADES INTO COMBO BOX
				if(cmbPSPWD.getItemCount()==1)
				{
					for(int i=0;i<LM_RCPROW;i++)
					{
						if(tblRECIP.getValueAt(i,1).toString().length()>0)
						{
							if(tblRECIP.getValueAt(i,10).toString().equals("true"))
							{
								cmbPSPWD.addItem(tblRECIP.getValueAt(i,2).toString());
							}
						}
						else
							break;
					}
				}
			}
		}
		else  if(M_objSOURC==txtPWDFD)
		{
			//ADDING BASE MATERIAL GRADES INTO COMBO BOX
			if(cmbPSPWD.getItemCount()==1)
			{
				for(int i=0;i<LM_RCPROW;i++)
				{
					if(tblRECIP.getValueAt(i,1).toString().length()>0)
					{
						if(tblRECIP.getValueAt(i,10).toString().equals("true"))
						{
							cmbPSPWD.addItem(tblRECIP.getValueAt(i,2).toString());
						}
					}
					else
						break;
				}
			}
		}
	}
	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtGRDCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtGRDDS.setText(L_STRTKN.nextToken());
				txtGRDCD.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("cmpEDITR[1]"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				((JTextField)((cl_JTBL)tblRECIP).cmpEDITR[1]).setText(L_STRTKN.nextToken());
				tblRECIP.setValueAt(L_STRTKN.nextToken(),tblRECIP.getSelectedRow(),2);
			}
			else if(M_strHLPFLD.equals("txtRUNNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtRUNNO.setText(L_STRTKN.nextToken());
				txtGRDDS.setText(L_STRTKN.nextToken());
				String L_STRTMP=L_STRTKN.nextToken();
				txtRNSDT.setText(L_STRTMP.substring(0,10));
				txtRNSTM.setText(L_STRTMP.substring(10,16));//RN_PRDTP,RN_TRLCT,RN_BATCT
				if(L_STRTKN.nextToken().equalsIgnoreCase("p"))
					btnSTFNL.setVisible(false);
				else
					btnSTFNL.setVisible(true);
				txtTRLCT.setText(L_STRTKN.nextToken());
				txtBTACT.setText(L_STRTKN.nextToken());
				txtGRDCD.setText(L_STRTKN.nextToken());
			}
		}catch(Exception L_EX)
		{
			System.out.println("Exception in Recipe Master exeHLPOK : "+L_EX);
		}
	}
	boolean vldDATA()
	{
		if(txtRNEDT.getText().length()>0)
		{
			setMSG("Batches cannot be added to completed run ..",'E');
			return false;
		}
		if(txtGRDCD.getText().length()<8)
		{
			setMSG("8 digit Grade Code is Compulsory ..",'E');
			txtGRDCD.requestFocus();
			return false;
		}
		else if(txtRUNNO.getText().length()<8)
		{
			setMSG("8 digit Run Number is Compulsory ..",'E');
			txtRUNNO.requestFocus();
			return false;
		}
		else if(txtBATSZ.getText().length()==0)
		{
			setMSG("Please enter Batch Size ..",'E');
			txtBATSZ.requestFocus();
			return false;
		}
		else if(txtRNSTM.getText().length()<5)
		{
			setMSG("Please enter Run Start Time ..",'E');
			txtRNSTM.requestFocus();
			return false;
		}
		else if(txtRNSDT.vldDATE()!=null)
		{
			setMSG(txtRNSDT.vldDATE(),'E');
			txtRNSDT.requestFocus();
			return false;
		}
		else if(txtPRTQT.getText().length()==0)
		{
			setMSG("Please enter Production Target ..",'E');
			txtPRTQT.requestFocus();
			return false;
		}
		else if(txtLOTNO.getText().length()==0)
		{
			setMSG("Please enter Lot Number ..",'E');
			txtLOTNO.requestFocus();
			return false;
		}
		else if(txtPSPWD.getText().length()>0&&txtPWDFD.getText().length()==0)
		{
			setMSG("Please enter Feeder for PS Powder ..",'E');
			txtPWDFD.requestFocus();
			return false;
		}
		else if(txtPSPWD.getText().length()>0&&txtPWDFD.getText().length()>0)
		{
			if(Integer.parseInt(txtPWDFD.getText())>4)
			{
				setMSG("Feeder no. for PS Powder should be less than 5..",'E');
				txtPWDFD.requestFocus();
				return false;
			}
		}
		else
		{
//VALIDATING FEEDER NO.s
			for(int i=0;i<LM_RCPROW;i++)
			{
				if(!tblRECIP.getValueAt(i,1).toString().equals(""))
				{
					if(tblRECIP.getValueAt(i,9).toString().equals(""))
					{
						setMSG("Enter Feeder no.s ..",'E');
						return false;
					}
					else if(Integer.parseInt(tblRECIP.getValueAt(i,9).toString())>4)
					{
						setMSG("Feeder no. should be less than 5 ..",'E');
						return false;
					}
				}
			}
		}
		calBTCH();
		String L_STRTMP="",L_SPRTR="'";
		double [][] L_MTLQT=new double[LM_RCPROW][2];
		double total=0.0;
		for(int i=0;i<LM_RCPROW;i++)
		{
			if(L_MTLQT[i][1]!=0.0)
			{
				L_STRTMP+=L_SPRTR+Double.toString(L_MTLQT[1][0]);
				L_SPRTR="','";
			}			
		}
		return true;
	}

	private void exeENQRY() throws Exception
	{
		setMSG(cmbLINNO.getSelectedItem().toString().substring(7),'N');
		M_strSQLQRY="SELECT * FROM pr_RNMST WHERE RN_RUNNO LIKE '"+cmbLINNO.getSelectedItem().toString().substring(7)+"%' AND RN_RUNST=(SELECT MAX(RN_RUNST) FROM pr_RNMST WHERE RN_RUNNO LIKE('"+cmbLINNO.getSelectedItem().toString().substring(7)+"%' ))";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
		if(M_rstRSSET.next())
		{
			tblRECIP.setRowSelectionInterval(0,0);
			tblRECIP.setColumnSelectionInterval(0,0);
			txtRUNNO.setText(M_rstRSSET.getString("RN_RUNNO"));
			txtPRTQT.setText(M_rstRSSET.getString("RN_TOTPR"));
			txtPRAQT.setText(M_rstRSSET.getString("RN_TOTPM"));
			txtGRDCD.setText(M_rstRSSET.getString("RN_GRDCD"));
			txtCMRNO.setText(nvlSTRVL(M_rstRSSET.getString("RN_CMRNO"),""));
			txtCMRRV.setText(nvlSTRVL(M_rstRSSET.getString("RN_CMRRV"),""));
			String L_STRTMP=nvlSTRVL(M_rstRSSET.getString("RN_CMRTP"),"");
			if(L_STRTMP.equals("C"))
				cmbCMRTP.setSelectedIndex(1);
			else if(L_STRTMP.equals("D"))
				cmbCMRTP.setSelectedIndex(2);
			else if(L_STRTMP.equals("C"))
				cmbCMRTP.setSelectedIndex(3);
			else
				cmbCMRTP.setSelectedIndex(0);
			L_STRTMP=M_fmtLCDTM.format(M_rstRSSET.getTimestamp("RN_RUNST"));
			String date=L_STRTMP.substring(0,10);
			String time=L_STRTMP.substring(10,16).trim();
			txtRNSDT.setText(date);
			txtRNSTM.setText(time);
			if(M_rstRSSET.getString("RN_RUNED")!=null)
			{
				setMSG("Batches cannot be added to completed run ..",'E');
				L_STRTMP=M_fmtLCDTM.format(M_rstRSSET.getTimestamp("RN_RUNED"));
				date=L_STRTMP.substring(0,10);
				time=L_STRTMP.substring(10,16).trim();
				txtRNEDT.setText(date);
				txtRNETM.setText(time);
			}
			if(M_rstRSSET.getString("RN_TRLFL").equals("T"))
			{
				btnSTFNL.setVisible(true);
				txtTRLCT.setText(Integer.toString(Integer.parseInt(M_rstRSSET.getString("RN_TRLCT"))+1));
			}
			else
			{
				btnSTFNL.setVisible(false);
				txtBTACT.setText(Integer.toString(Integer.parseInt(M_rstRSSET.getString("RN_BATCT"))+1));
				txtTRLCT.setText(Integer.toString(Integer.parseInt(M_rstRSSET.getString("RN_TRLCT"))));
				((cl_JTBL)tblRECIP).setEnabled(false);
				((cl_JTBL)tblRECIP).cmpEDITR[8].setEnabled(true);((cl_JTBL)tblRECIP).cmpEDITR[9].setEnabled(true);
			}
			txtGRDCD.setText(M_rstRSSET.getString("RN_GRDCD"));
			txtGRDDS.setText(M_rstRSSET.getString("RN_GRDDS"));
			M_strSQLQRY="SELECT * FROM pr_RPMST WHERE RP_RUNNO='"+txtRUNNO.getText()+"' AND RP_BATNO=(SELECT MAX(RP_BATNO) FROM pr_RPMST WHERE RP_RUNNO='"+txtRUNNO.getText()+"' AND RP_GRDCD='"+txtGRDCD.getText()+"') AND RP_GRDCD='"+txtGRDCD.getText()+"'";
			M_rstRSSET=null;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			int i=0;
			double L_BSMQT=0.0;
			while(M_rstRSSET.next())
			{
				tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLCD"),i,1);
				tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLDS"),i,2);
				tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLMF"),i,3);
				tblRECIP.setValueAt(M_rstRSSET.getString("RP_MTLBT"),i,4);
				tblRECIP.setValueAt(M_rstRSSET.getString("RP_PCTQT"),i,6);
				tblRECIP.setValueAt(M_rstRSSET.getString("RP_FDRNO"),i,9);
				if(M_rstRSSET.getString("RP_BSMFL").equals("Y"))
				{
					tblRECIP.setValueAt(new Boolean(true),i,10);
					L_BSMQT+=M_rstRSSET.getDouble("RP_PCTQT");
				}
				else
					tblRECIP.setValueAt(new Boolean(false),i,10);
				i++;
			}
			i=0;
			while((!tblRECIP.getValueAt(i,1).equals(""))&&i<LM_RCPROW)
			{
				if(tblRECIP.getValueAt(i,10).equals(new Boolean(true)))
					tblRECIP.setValueAt(setNumberFormat((Double.parseDouble(tblRECIP.getValueAt(i,6).toString())/L_BSMQT)*100.0,5),i,5);
				else
					tblRECIP.setValueAt(setNumberFormat(Double.parseDouble(tblRECIP.getValueAt(i,6).toString())/L_BSMQT*100.0,5),i,5);
				i++;
			}
		}
		else
			txtTRLCT.setText("1");
		txtBATNO.setText(Integer.toString((Integer.parseInt(nvlSTRVL(txtTRLCT.getText(),"0"))+Integer.parseInt(nvlSTRVL(txtBTACT.getText(),"0")))));
		if(btnSTFNL.isVisible())
		{
			((cl_JTBL)tblRECIP).setEnabled(true);
			((cl_JTBL)tblRECIP).cmpEDITR[7].setEnabled(false);
			((cl_JTBL)tblRECIP).cmpEDITR[2].setEnabled(false);
		}
		LM_GRDCD=txtGRDCD.getText();
		if(txtRNEDT.getText().length()>2)
			setMSG("Batches cannot be added to completed run ..",'E');
	}
	void exeSAVE()
	{
		try{
		cl_dat.M_flgLCUPD_pbst=true;
		if(vldDATA())
		{
			((cl_JTBL)tblRECIP).dceEDITR[tblRECIP.getSelectedColumn()].stopCellEditing();
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
			{
				if(LM_GRDCD.equals(txtGRDCD.getText()))
				{
					M_strSQLQRY="UPDATE pr_RNMST SET "
						+"RN_RCLQT=RN_RCLQT,"//RN_RCLQT,
						+"RN_TOTPM="+(btnSTFNL.isVisible()==true ? "0" : (" RN_TOTPM +"+Double.toString(Double.parseDouble(txtBATSZ.getText())/1000.0)+" "))+" ,"//RN_TOTPM
						+"RN_TRLFL='"+(btnSTFNL.isVisible()==true ? "T" : "P")+"',"//RN_TRLFL
						+"RN_CMRNO='"+txtCMRNO.getText()+"',"//RN_CMRNO
						+"RN_CMRRV='"+txtCMRRV.getText()+"',"//RN_CMRRV
						+"RN_CMRTP='"+cmbCMRTP.getSelectedItem().toString().substring(0,1)+"',"//RN_CMRTP
						+"RN_BATCT= "+nvlSTRVL(txtBTACT.getText(),"0")+","//RN_BATCT,
						+"RN_TRLCT= "+nvlSTRVL(txtTRLCT.getText(),"0")+","//RN_TRLCT,
						+" RN_DCLPM=RN_TOTPM,"
						+getUSGDTL("RN",'u',null) +" where rn_runno='"+txtRUNNO.getText()+"' and rn_grdcd='"+txtGRDCD.getText()+"'";//RN_LUPDT
				}
				else
				{
					M_strSQLQRY="INSERT INTO pr_RNMST (RN_RUNNO,RN_RUNST,RN_RUNED,RN_GRDCD,RN_GRDDS,RN_CMRNO,RN_CMRRV,RN_CMRTP,RN_WSTQT,RN_RCLQT,RN_RPRQT,RN_SMPQT,RN_LOSQT,RN_TOTPR,RN_TOTPM,RN_TRLCT,RN_BATCT,RN_TRLFL,RN_TRNFL,RN_STSFL,RN_LUSBY,RN_LUPDT) VALUES ("
						+"'"+txtRUNNO.getText()+"',"//	+RN_RUNNO,
						+"'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtRNSDT.getText()+" "+txtRNSTM.getText()))+"',"//RN_RUNST,
						+""+null+","//RN_RUNED,
						+"'"+txtGRDCD.getText()+"',"//RN_GRDCD,
						+"'"+txtGRDDS.getText()+"',"//RN_GRDCD,
						+"'"+txtCMRNO.getText()+"',"//RN_CMRNO
						+"'"+txtCMRRV.getText()+"',"//RN_CMRRV
						+"'"+cmbCMRTP.getSelectedItem().toString().substring(0,1)+"',"//RN_CMRTP
						+""+0+","//RN_WSTQT,
						+""+0+","//RN_RCLQT,
						+""+0+","//RN_RPRQT,
						+""+0+","//RN_SMPQT,
						+""+0+","//RN_LOSQT,
						+""+txtPRTQT.getText()+","//RN_TOTPR,
						+""+0+","//RN_TOTPM,
						+""+txtTRLCT.getText()+","//RN_TRLCT,
						+""+nvlSTRVL(txtBTACT.getText(),"0")+","//RN_BATCT,
						+"'T',"//RN_TRLFL
						+getUSGDTL("RN",'i',"")+")";//RN_TRNFL,RN_STSFL,RN_LUSBY,RN_LUPDT
				}
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
//INSERTING INTO pr_BTMST (BATCH MASTER)
					M_strSQLQRY="INSERT INTO pr_BTMST (BT_BATNO,BT_GRDCD,BT_RUNNO,BT_BATDT,BT_BATSZ,BT_TRLFL,BT_TRNFL,BT_STSFL,BT_LUSBY,BT_LUPDT) VALUES ("
						+""+txtBATNO.getText()+","//RP_BATNO,
						+"'"+txtGRDCD.getText()+"',"//RP_GRDCD,
						+"'"+txtRUNNO.getText()+"',"//RP_RUNNO,
						+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//BT_BATDT
						+""+Double.toString(Double.parseDouble(nvlSTRVL(txtBATSZ.getText(),"0"))/1000.0)+","//BT_BATSZ
						+"'"+(btnSTFNL.isVisible()==true ? "T" : "P")+"',"//BT_TRLFL
						+getUSGDTL("BT",'i',"")+")";//BT_TRNFL,BT_STSFL,BT_LUSBY,BT_LUPDT
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
//ADDING DATA TO RECIPE MASTER TABLE							
				for(int i=0;i<LM_RCPROW&&cl_dat.M_flgLCUPD_pbst;i++)
				{
					if(tblRECIP.getValueAt(i,1).toString().length()>0)
					{
						if(btnSTFNL.isVisible())
						{
							M_strSQLQRY="INSERT INTO pr_RPMST (RP_RUNNO,RP_BATNO,RP_GRDCD,RP_MTLCD,RP_MTLMF,RP_MTLDS,RP_MTLBT,RP_PCTQT,RP_FDRNO,RP_BSMFL,RP_TRNFL,RP_STSFL,RP_LUSBY,RP_LUPDT) VALUES ("
								+"'"+txtRUNNO.getText()+"',"//RP_RUNNO,
								+""+txtTRLCT.getText()+","//RP_TRLNO,
								+"'"+txtGRDCD.getText()+"',"//RP_GRDCD,
								+"'"+tblRECIP.getValueAt(i,1).toString()+"',"//RP_MTLCD,
								+"'"+tblRECIP.getValueAt(i,3).toString()+"',"//RP_MTLMF,
								+"'"+tblRECIP.getValueAt(i,2).toString()+"',"//RP_MTLDS,
								+"'"+tblRECIP.getValueAt(i,4).toString()+"',"//RP_MTLBT,
								+""+Double.toString(Double.parseDouble(tblRECIP.getValueAt(i,6).toString()))+","//RP_PCTQT,
								+""+tblRECIP.getValueAt(i,9).toString()+","//RP_FDRNO
								+"'"+(((Boolean)tblRECIP.getValueAt(i,10)).equals(new Boolean(true))?"Y" : "N")+"',"//RP_BSMFL
								+getUSGDTL("RP",'i',"")+")";//RP_TRNFL,RP_STSFL,RP_LUSBY,RP_LUPDT
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						}
//ADDING DATA TO BATCH TRANSACTION TABLE 							
						M_strSQLQRY="INSERT INTO pr_BTTRN (BTT_BATNO,BTT_GRDCD,BTT_RUNNO,BTT_MTLDS,BTT_MTLMF,BTT_MTLCD,BTT_MTLBT,BTT_MTLQT,BTT_BSMFL,BTT_TRNFL,BTT_STSFL,BTT_LUSBY,BTT_LUPDT) VALUES ("
							+""+nvlSTRVL(txtBATNO.getText(),"0")+","//BTT_BATNO
							+"'"+nvlSTRVL(txtGRDCD.getText(),"0")+"',"//BTT_GRDCD
							+"'"+nvlSTRVL(txtRUNNO.getText(),"0")+"',"//BTT_RUNNO
							+"'"+tblRECIP.getValueAt(i,2).toString()+"',"//BTT_MTLDS
							+"'"+tblRECIP.getValueAt(i,3).toString()+"',"//BTT_MTLMF
							+"'"+tblRECIP.getValueAt(i,1).toString()+"',"//BTT_MTLCD
							+"'"+tblRECIP.getValueAt(i,4).toString()+"',"//BTT_MTLBT
							+""+LM_ARQTY[i]+","//BTT_MTLQT
							+"'"+(((Boolean)tblRECIP.getValueAt(i,10)).equals(new Boolean(true))?"Y" : "N")+"',"//BTT_BSMFL
							+getUSGDTL("BTT",'i',"")+")";//BTT_TRNFL,BTT_STSFL,BTT_LUSBY,BTT_LUPDT
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
//UPDATING RUNMASTER FOR WASTE QUANTITY CONSUMPTION
/*				if(Double.parseDouble(nvlSTRVL(txtPSRCL.getText(),"0.0"))>0.0)
				{
					Enumeration keys=htbRCLQT.keys();
					String L_GRDDS="",L_CONQT="",L_STRTMP="",L_GRDCD="";
					StringTokenizer L_STRTKN=new StringTokenizer(L_STRTMP,"|");
					while(keys.hasMoreElements())
					{
						L_STRTMP=keys.nextElement().toString();
						L_STRTKN=new StringTokenizer(htbRCLQT.get(L_STRTMP).toString(),"|");
						L_STRTMP=L_STRTKN.nextToken();
						L_CONQT=L_STRTKN.nextToken();
						L_GRDDS=L_STRTKN.nextToken();
						L_GRDCD=L_STRTKN.nextToken();
						System.out.println("con"+L_CONQT+" grdcd"+L_GRDCD+" RUN"+L_STRTMP);
						M_strSQLQRY="UPDATE pr_RNMST SET RN_WAVQT=RN_WAVQT-"+L_CONQT+" WHERE RN_RUNNO = '"+L_STRTMP+"' AND RN_GRDCD='"+L_GRDCD+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						M_strSQLQRY="INSERT INTO pr_RCTRN (RCT_PRRUN,RCT_PRGRD,RCT_RCRUN,RCT_RCGRD,RCT_RCLQT,RCT_TRNFL,RCT_STSFL,RCT_LUSBY,RCT_LUPDT) VALUES ("
							+"'"+L_STRTMP+"',"
							+"'"+L_GRDCD+"',"
							+"'"+txtRUNNO.getText()+"',"
							+"'"+txtGRDCD.getText()+"',"
							+""+L_CONQT+","
							+"'0',"//RCT_trnfl,
							+"'',"//RCT_STSFL,
							+"'"+cl_dat.M_strUSRCD_pbst+"',"//RCT_LUSBY,
							+cc_dattm.setDBSDT(cl_dat.M_strLOGDT_pbst)+")";//RCT_LUPDT
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					htbRCLQT.clear();
				}
*/				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setENBL(false);
					btnPRINT.setEnabled(true);
					btnPRINT.requestFocus();
					setMSG("Data Saved Successfully ..",'N');
				}
				else
				{
					setMSG("Error occured during saving ..",'E');
				}
			}
		}
		}catch(Exception e)
		{setMSG(e,"Child.exeSAVE");}
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtRUNNO.setEnabled(false);txtTRLCT.setEnabled(false);txtRNETM.setEnabled(false);
		txtRNEDT.setEnabled(false);txtBTTCT.setEnabled(false);txtPRAQT.setEnabled(false);
		txtBTACT.setEnabled(false);txtBATNO.setEnabled(false);txtGRDDS.setEnabled(false);
		tblRECIP.setEnabled(L_STAT);((cl_JTBL)tblRECIP).cmpEDITR[6].setEnabled(false);btnPRINT.setEnabled(false);
		((cl_JTBL)tblRECIP).cmpEDITR[7].setEnabled(false);cmbUOMCD.setEnabled(true);
		((cl_JTBL)tblRECIP).cmpEDITR[2].setEnabled(false);((cl_JTBL)tblRECIP).cmpEDITR[8].setEnabled(true);
	}
	void clrCOMP()
	{
		RCLQT=0.0;
		super.clrCOMP();
	}
	private void setFOCUS(Object L_OSRC)
	{
		if(L_OSRC==txtRNSDT)
			txtRNSTM.requestFocus();
		else if(L_OSRC==txtRNSTM)
			txtPRTQT.requestFocus();
		else if(L_OSRC==txtPRTQT)
			txtBATSZ.requestFocus();
		else if(L_OSRC==txtLOTNO)
			txtPSPWD.requestFocus();
//		else if(L_OSRC==txtPSRCL)
//			txtPSPWD.requestFocus();
	}
	/** To print the receipe sheet	 */
	private void exePRNT()
	{
	/*	try
		{
			FILE=new File("c:\\reports\\pr_rprpp.doc");
			outFILE=new FileOutputStream(FILE);
			outDTSTR=new DataOutputStream(outFILE);
	//	PRINTING HEADER INFORMATION
			outDTSTR.writeBytes(padSTRING('L',"|----------------------------------------------|\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"    SUPREME PETROCHEM LIMITED    |              SPL/PRD_SPS/QR/005              |\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"|----------------------------------------------|\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"  SPECIALITY POLYSTYRENE PLANT   | ISSUE NO.  |    0     | REV. NO.  |    1     |\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"   RECIPE AUTHORISATION SHEET    | ISSUE DATE |15/01/2002| REV. DATE |01/10/2002|\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"|----------------------------------------------|\n",89));
//			outDTSTR.writeBytes("\n");
//			outDTSTR.writeBytes(padSTRING('C',"SPECIALITY POLYSTYRENE PLANT\n",80));
//			outDTSTR.writeBytes("\n");
//			outDTSTR.writeBytes(padSTRING('C',"RECIPE AUTHORISATION SHEET\n",80));
			outDTSTR.writeBytes("\n_________________________________________________________________________________________\n");
			outDTSTR.writeBytes("|Production Type : "+padSTRING('R',(btnSTFNL.isVisible()==true ? "Trial" : "Production"),25)+"Lot No. : "+padSTRING('L',txtLOTNO.getText(),10)+"   Grade : "+padSTRING('L',txtGRDDS.getText(),13)+"|\n");
			outDTSTR.writeBytes("|Batch No.       : "+padSTRING('R',(btnSTFNL.isVisible()==true ? txtTRLCT.getText() : txtBATNO.getText()),25)+"Date    : "+padSTRING('L',cl_dat.M_strLOGDT_pbst,10)+"   Line : "+padSTRING('L',txtRUNNO.getText().substring(0,2),14)+"|\n");
			outDTSTR.writeBytes("|Batch Size      : "+padSTRING('R',txtBATSZ.getText(),45)+"   Run :"+padSTRING('L',txtRUNNO.getText(),16)+"|\n");
			outDTSTR.writeBytes("|_______________________________________________________________________________________|\n");
	// PRINTING RECIPE
			outDTSTR.writeBytes("RECIPE :\n");
			outDTSTR.writeBytes("|_______________________________________________________________________________________|\n");
			outDTSTR.writeBytes("|                    Item                     |   Grade  |  PHR   |   %    |Batch Qt|UOM|\n");
			outDTSTR.writeBytes("|---------------------------------------------|----------|--------|--------|--------|---|\n");
			String L_STRTMP="";
			for(int i=0;i<LM_RCPROW;i++)
			{
				if(tblRECIP.getValueAt(i,1).toString().length()<1)
					break;
				L_STRTMP="|"+padSTRING('R',tblRECIP.getValueAt(i,2).toString(),45)+"|"+padSTRING('L',tblRECIP.getValueAt(i,4).toString(),10)
					+"|"+padSTRING('L',tblRECIP.getValueAt(i,5).toString(),8)+"|"+padSTRING('L',tblRECIP.getValueAt(i,6).toString(),8)
					+"|"+padSTRING('L',tblRECIP.getValueAt(i,7).toString(),8)+"|"+padSTRING('L',tblRECIP.getValueAt(i,8).toString(),3)+"|";
				outDTSTR.writeBytes(L_STRTMP);
				outDTSTR.writeBytes("\n");
				outDTSTR.writeBytes("|---------------------------------------------|----------|--------|--------|--------|---|\n");
			}
			outDTSTR.writeBytes("|_______________________________________________________________________________________|\n");
			outDTSTR.writeBytes("FEEDER RATIOS :                        PS/SPS LOT DETAILS :             PARAMETERS :   \n");
			outDTSTR.writeBytes("|------------------------------------| |-----------------------------| |----------------|\n");
			outDTSTR.writeBytes("|Feeder No.|    Qty     |    Ratio   | | PS GRADE  |   QTY  |UOM|FDR | |     |Zone|Temp.|\n");
			outDTSTR.writeBytes("|Feeder : 1|"+padSTRING('L',tblFDRQT.getValueAt(0,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(0,3).toString(),12)+"| |PS POWDER  |"+padSTRING('L',txtPSPWD.getText(),8)+"| KG| "+padSTRING('L',nvlSTRVL(txtPWDFD.getText(),""),2)+" | |     |  2 |     |\n");
			outDTSTR.writeBytes("|Feeder : 2|"+padSTRING('L',tblFDRQT.getValueAt(1,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(1,3).toString(),12)+"| |           |        |   |    | |     |  3 |     |\n");
			outDTSTR.writeBytes("|Feeder : 3|"+padSTRING('L',tblFDRQT.getValueAt(2,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(2,3).toString(),12)+"| |           |        |   |    | | Tol.|  4 |     |\n");
			outDTSTR.writeBytes("|Feeder : 4|"+padSTRING('L',tblFDRQT.getValueAt(3,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(3,3).toString(),12)+"| |           |        |   |    | |+/-10|  5 |     |\n");
			outDTSTR.writeBytes("|------------------------------------| |-----------|--------|---|----| |     |  6 |     |\n"); 
			double fdrqt=0.0;
			for(int i=0;i<tblFDRQT.getRowCount();i++)
			{
				fdrqt+=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(i,2).toString(),"0.0"));
			}
			outDTSTR.writeBytes("|TOTAL     |"+padSTRING('L',Double.toString(fdrqt),12)+"|   100.00   | |           |        |   |    | |     |  7 |     |\n");
			outDTSTR.writeBytes("|------------------------------------| |-----------------------------| |     |  8 |     |\n");
			outDTSTR.writeBytes(padSTRING('L',"  |     |  9 |     |",89)+"\n");
/*			outDTSTR.writeBytes("OPERATING CONDITIONS                                                    |     | 10 |      | \n");
			outDTSTR.writeBytes(" _______________________________________________________________________________________\n");
			outDTSTR.writeBytes("| Zone :|   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |  10   |\n");
			outDTSTR.writeBytes("|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|\n");
			outDTSTR.writeBytes("|       |       |       |       |       |       |       |       |       |       |       |\n");
			outDTSTR.writeBytes("|°C(±10)|       |       |       |       |       |       |       |       |       |       |\n");
			outDTSTR.writeBytes("|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|\n");
			outDTSTR.writeBytes("|               |               |               |               |Screen |               |\n");
			outDTSTR.writeBytes("|  Torque (%)   |               |Plant O/P(kgh) |               |Pack   |               |\n");
			outDTSTR.writeBytes("|_______________|_______________|_______________|_______________|_______|_______________|\n");
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes("REMARKS :                                                              |     | 10 |     |  \n");
			outDTSTR.writeBytes("______________________________________________________________________ |_____|____|_____|\n");
			outDTSTR.writeBytes("| PR No. :                                    "+padSTRING('L',(cmbCMRTP.getSelectedItem().toString()+" : "+txtCMRNO.getText()+" Rev. : "+nvlSTRVL(txtCMRRV.getText(),"0")),24)+"||Plant O/P |     |\n");
			//outDTSTR.writeBytes("| ");
			//outDTSTR.writeBytes(padSTRING('L',(cmbCMRTP.getSelectedItem().toString()+" : "+txtCMRNO.getText()+" Rev. : "+nvlSTRVL(txtCMRRV.getText(),"0")),40)+"|\n");
			outDTSTR.writeBytes("|                                                                     ||__________|_____|\n");
			outDTSTR.writeBytes("|                                                                     ||SCRN pack |     |\n");
			outDTSTR.writeBytes("|_____________________________________________________________________||__________|_____|\n");
			outDTSTR.writeBytes("------------------------------------------------------------\n");
			outDTSTR.writeBytes("|         |Prepared By|Checked By|Authorised By|Assigned To|  MIXING TIME                \n");
			outDTSTR.writeBytes("|---------|-----------|----------|-------------|-----------| |---------|-----------|----|\n");
			outDTSTR.writeBytes("|         |           |          |             |           | |1st speed|           |    |\n");
			outDTSTR.writeBytes("|---------|-----------|----------|-------------|-----------| |---------|-----------|----|\n");
			outDTSTR.writeBytes("|         |           |          |             |           | |2nd speed|           |    |\n");
			outDTSTR.writeBytes("|---------|-----------|----------|-------------|-----------| |---------|-----------|----|\n");
			outDTSTR.close();
			outFILE.close();

			Runtime r = Runtime.getRuntime();
			Process p = null;
//			cl_dat.ocl_dat.M_DELFL = 'T' ; 
			p  = r.exec("co_rpprn.bat "+"c:\\reports\\pr_rprpp.doc"); 
		}catch(Exception e)
		{System.out.println(e);}*/
		try
		{
			FILE=new File("c:\\reports\\pr_rprpp.doc");
			outFILE=new FileOutputStream(FILE);
			outDTSTR=new DataOutputStream(outFILE);
	//	PRINTING HEADER INFORMATION
			outDTSTR.writeBytes(padSTRING('L',"|----------------------------------------------|\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"    SUPREME PETROCHEM LIMITED    |              SPL/PRD_SPS/QR/005              |\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"|----------------------------------------------|\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"  SPECIALITY POLYSTYRENE PLANT   | ISSUE NO.  |    0     | REV. NO.  |    1     |\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"   RECIPE AUTHORISATION SHEET    | ISSUE DATE |15/01/2002| REV. DATE |01/10/2002|\n",89));
			outDTSTR.writeBytes("\n");
			outDTSTR.writeBytes(padSTRING('L',"|----------------------------------------------|\n",89));
//			outDTSTR.writeBytes("\n");
//			outDTSTR.writeBytes(padSTRING('C',"SPECIALITY POLYSTYRENE PLANT\n",80));
//			outDTSTR.writeBytes("\n");
//			outDTSTR.writeBytes(padSTRING('C',"RECIPE AUTHORISATION SHEET\n",80));
			outDTSTR.writeBytes("\n_________________________________________________________________________________________\n");
			outDTSTR.writeBytes("|Production Type : "+padSTRING('R',(btnSTFNL.isVisible()==true ? "Trial" : "Production"),25)+"Lot No. : "+padSTRING('L',txtLOTNO.getText(),10)+"   Grade : "+padSTRING('L',txtGRDDS.getText(),13)+"|\n");
			outDTSTR.writeBytes("|Batch No.       : "+padSTRING('R',(btnSTFNL.isVisible()==true ? txtTRLCT.getText() : txtBATNO.getText()),25)+"Date    : "+padSTRING('L',cl_dat.M_strLOGDT_pbst,10)+"   Line : "+padSTRING('L',txtRUNNO.getText().substring(0,2),14)+"|\n");
			outDTSTR.writeBytes("|Batch Size      : "+padSTRING('R',txtBATSZ.getText(),45)+"   Run :"+padSTRING('L',txtRUNNO.getText(),16)+"|\n");
			outDTSTR.writeBytes("|_______________________________________________________________________________________|\n");
	// PRINTING RECIPE
			outDTSTR.writeBytes("RECIPE :\n");
			outDTSTR.writeBytes("|_______________________________________________________________________________________|\n");
			outDTSTR.writeBytes("|                    Item                  |FD|  Lot no. |  PHR   |   %    |Batch Qt|UOM|\n");
//			outDTSTR.writeBytes("|---------------------------------------------|----------|--------|--------|--------|---|\n");
			String L_STRTMP="";
			for(int i=0;i<LM_RCPROW;i++)
			{
				if(tblRECIP.getValueAt(i,1).toString().length()<1)
					break;
				outDTSTR.writeBytes("|------------------------------------------|--|----------|--------|--------|--------|---|\n");
				L_STRTMP="|"+padSTRING('R',tblRECIP.getValueAt(i,2).toString(),45).substring(0,42)+"|"+padSTRING('L',tblRECIP.getValueAt(i,9).toString(),2)+"|"+padSTRING('L',tblRECIP.getValueAt(i,4).toString(),10)
					+"|"+padSTRING('L',tblRECIP.getValueAt(i,5).toString(),8)+"|"+padSTRING('L',tblRECIP.getValueAt(i,6).toString(),8)
					+"|"+padSTRING('L',tblRECIP.getValueAt(i,7).toString(),8)+"|"+padSTRING('L',tblRECIP.getValueAt(i,8).toString(),3)+"|";
				outDTSTR.writeBytes(L_STRTMP);
				outDTSTR.writeBytes("\n");
			}
			outDTSTR.writeBytes("|_______________________________________________________________________________________|\n");
			outDTSTR.writeBytes("FEEDER RATIOS :                        PS/SPS LOT DETAILS :             PARAMETERS :   \n");
			outDTSTR.writeBytes("|------------------------------------| |-----------------------------| |----------------|\n");
			outDTSTR.writeBytes("|Feeder No.|    Qty     |    Ratio   | | PS GRADE  |   QTY  |UOM|FDR | |     |Zone|Temp.|\n");
			outDTSTR.writeBytes("|Feeder : 1|"+padSTRING('L',tblFDRQT.getValueAt(0,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(0,3).toString(),12)+"| |PS POWDER  |"+padSTRING('L',txtPSPWD.getText(),8)+"| KG| "+padSTRING('L',nvlSTRVL(txtPWDFD.getText(),""),2)+" | |     |  2 |     |\n");
			outDTSTR.writeBytes("|Feeder : 2|"+padSTRING('L',tblFDRQT.getValueAt(1,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(1,3).toString(),12)+"| |           |        |   |    | |     |  3 |     |\n");
			outDTSTR.writeBytes("|Feeder : 3|"+padSTRING('L',tblFDRQT.getValueAt(2,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(2,3).toString(),12)+"| |           |        |   |    | | Tol.|  4 |     |\n");
			outDTSTR.writeBytes("|Feeder : 4|"+padSTRING('L',tblFDRQT.getValueAt(3,2).toString(),12)+"|"+padSTRING('L',tblFDRQT.getValueAt(3,3).toString(),12)+"| |           |        |   |    | |+/-10|  5 |     |\n");
			outDTSTR.writeBytes("|------------------------------------| |-----------|--------|---|----| |     |  6 |     |\n"); 
			double fdrqt=0.0;
			for(int i=0;i<tblFDRQT.getRowCount();i++)
			{
				fdrqt+=Double.parseDouble(nvlSTRVL(tblFDRQT.getValueAt(i,2).toString(),"0.0"));
			}
			outDTSTR.writeBytes("|TOTAL     |"+padSTRING('L',Double.toString(fdrqt),12)+"|   100.00   | |           |        |   |    | |     |  7 |     |\n");
			outDTSTR.writeBytes("|------------------------------------| |-----------------------------| |     |  8 |     |\n");
			outDTSTR.writeBytes(padSTRING('L',"  |     |  9 |     |",89)+"\n");
/*			outDTSTR.writeBytes("OPERATING CONDITIONS                                                    |     | 10 |      | \n");
			outDTSTR.writeBytes(" _______________________________________________________________________________________\n");
			outDTSTR.writeBytes("| Zone :|   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |  10   |\n");
			outDTSTR.writeBytes("|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|\n");
			outDTSTR.writeBytes("|       |       |       |       |       |       |       |       |       |       |       |\n");
			outDTSTR.writeBytes("|°C(±10)|       |       |       |       |       |       |       |       |       |       |\n");
			outDTSTR.writeBytes("|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|_______|\n");
			outDTSTR.writeBytes("|               |               |               |               |Screen |               |\n");
			outDTSTR.writeBytes("|  Torque (%)   |               |Plant O/P(kgh) |               |Pack   |               |\n");
			outDTSTR.writeBytes("|_______________|_______________|_______________|_______________|_______|_______________|\n");
			outDTSTR.writeBytes("\n");
		*/	outDTSTR.writeBytes("REMARKS :                                                              |     | 10 |     |  \n");
			outDTSTR.writeBytes("______________________________________________________________________ |_____|____|_____|\n");
			outDTSTR.writeBytes("| PR No. :                                    "+padSTRING('L',(cmbCMRTP.getSelectedItem().toString()+" : "+txtCMRNO.getText()+" Rev. : "+nvlSTRVL(txtCMRRV.getText(),"0")),24)+"||Plant O/P |     |\n");
			//outDTSTR.writeBytes("| ");
			//outDTSTR.writeBytes(padSTRING('L',(cmbCMRTP.getSelectedItem().toString()+" : "+txtCMRNO.getText()+" Rev. : "+nvlSTRVL(txtCMRRV.getText(),"0")),40)+"|\n");
			outDTSTR.writeBytes("|                                                                     ||__________|_____|\n");
			outDTSTR.writeBytes("|                                                                     ||SCRN pack |     |\n");
			outDTSTR.writeBytes("|_____________________________________________________________________||__________|_____|\n");
			outDTSTR.writeBytes("------------------------------------------------------------\n");
			outDTSTR.writeBytes("|         |Prepared By|Checked By|Authorised By|Assigned To|  MIXING TIME                \n");
			outDTSTR.writeBytes("|---------|-----------|----------|-------------|-----------| |---------|-----------|----|\n");
			outDTSTR.writeBytes("|         |           |          |             |           | |1st speed|           |SEC |\n");
			outDTSTR.writeBytes("|---------|-----------|----------|-------------|-----------| |---------|-----------|----|\n");
			outDTSTR.writeBytes("|         |           |          |             |           | |2nd speed|           |SEC |\n");
			outDTSTR.writeBytes("|---------|-----------|----------|-------------|-----------| |---------|-----------|----|\n");
			outDTSTR.close();
			outFILE.close();

			Runtime r = Runtime.getRuntime();
			Process p = null;
			p  = r.exec("co_rpprn.bat "+"c:\\reports\\pr_rprpp.doc"); 
		}catch(Exception e)
		{System.out.println(e);}
	}
}