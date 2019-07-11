import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.text.*;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Process&nbsp;Resource&nbsp;Planning System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>Run Master</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>Form for adding&nbsp;and retrieving&nbsp; details       of Grade Run (Modification option not to be given to any       user API APT. But, code is tested.) </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\pr_mernm.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\pr_mernm.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>15/05/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>For Base classes revision and Subsystem implementation</TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>AAP</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>20/09/2003</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>2.0.0.</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>PR_RNMST</TD>    <TD> RN_RUNNO,RN_GRDCD</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>PR_BTTRN</TD>    <TD>BTT_RUNNO,BTT_GRDCD,BTT_BATNO,BTT_MTLCD</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class pr_mernm extends cl_pbase implements ActionListener
{
	/**EDITOR FOR ACTUAL CONSUMPTION QTY OF PS*/
	private TxtNumLimit txtPSDTL;/**RUN NUMBER*/		
	private TxtNumLimit	txtRUNNO;	/**RUN START TIME*/
	private TxtNumLimit	txtRNSTM;	/**NO. OF TRIALS*/
	private TxtNumLimit	txtTRLCT;	/**WASTE GENERATED QTY*/
	private TxtNumLimit	txtWSTQT;	/**RE PROCESSED QUANTITY*/
	private TxtNumLimit	txtRPRQT;	/**RECYCLED QTY*/
	private TxtNumLimit	txtRCLQT;	/**SAMPLING QUANTITY*/
	private TxtNumLimit	txtSMPQT;	/**OTHER LOSSES*/
	private TxtNumLimit	txtLOSQT;	/**TOTAL PRODUCTION*/
	private TxtNumLimit	txtTOTPR;	/**TOTAL PRIME PRODUCATION*/
	private TxtNumLimit	txtTOTPM;	/**FIRST TIME PRIME*/
	private TxtNumLimit	txtFTMPM;	/**PRODUCTION FROM WIP*/
	private TxtNumLimit	txtWIPQT;	/**DECLARED PRODUCTION*/
	private TxtNumLimit	txtDCLPM;	/**FIRST PASS YIELD*/
	private TxtNumLimit	txtFPSYD;	/**TOTAL YIELD*/
	private TxtNumLimit	txtTOTYD;	/**SP RAW MATERIAL COST*/
	private TxtNumLimit	txtRMSPC;	/**SP. UTILITY COST*/
	private TxtNumLimit	txtUTSPC;	/**SP. TRIAL COST*/
	private TxtNumLimit	txtTLSPC;	/**SP. SAMPLE COST*/
	private TxtNumLimit	txtSMSPC;	/**SP. REPROCESSING COST*/
	private TxtNumLimit	txtRPSPC;	/**SP. RECYLING COST*/
	private TxtNumLimit	txtRCSPC;	/**SP. RECYCLING GAIN*/
	private TxtNumLimit	txtRCSPG;	/**SP. REPROCESSING GAIN*/
	private TxtNumLimit	txtRPSPG;	/**SP. PRODUCTION COST FOR THE RUN*/
	private TxtNumLimit	txtRNSPC;	/**MINIMUM POSSIBLE SP. PRODUCATION COST*/
	private TxtNumLimit	txtMNSPC;	/**RUN START DATE*/
	private TxtDate		txtRNSDT;		/**RUN END DATE*/
	private TxtDate		txtRNEDT;	/**GRADE DESCRIPTION*/
	private TxtLimit	txtGRDDS;	/**CMR NO. FOR THE RUN*/
	private TxtLimit	txtCMRNO;	/**CMR REVISION NO.*/
	private TxtLimit	txtCMRRV;	/**CMR TYPE : CMR/DMR/OTHER*/
	private TxtLimit	txtCMRTP;	/**GRADE PRODUCED*/
	private TxtLimit	txtGRDCD;	/**RUN END TIME*/
	private TxtTime		txtRNETM;	/**TO SELECT LINE NO. FOR NEW TRIAL*/
	private JComboBox	cmbLINNO;	/**COMBOBOX FOR UNIT OF MEASSUREMENT*/
	private JComboBox	cmbUOMCD;	/**TABLE FOR BASE MATERIAL CONSUMTION DETAILS*/
	private cl_JTBL		tblPSDTL;	/**TABLE FOR RECYCLE QTY AVAILABLE AND USED DETAILS (ON F1)*/
	private cl_JTBL		tblRCLQT;	/**QUALITY DETAILS*/
	private cl_JTBL		tblQLDTL;	/**FINAL RECIPE DETAILS*/
	private cl_JTBL		tblRECIP;	/** Dialog window for recycle material available in internal stores */
	private JDialog		dlgRCLQT;	/** Dialog window for WIP material available in internal stores */
	private JButton		btnWIPQTY;	/** WIP details Window  - Calnel button */
	private JButton		btnWIPQTN;	/** Recycle details Window  - OK button */
	private JButton		btnRCLQTY;	/** Recycle details Window  - Calnel button */
	private JButton		btnRCLQTN;	/** Hash table to store details of production from WIP for current Run */
	private Hashtable<String,String>	htbWIPQT;	/** Hash table to store details of Recycled material for current Run */
	private Hashtable<String,String>	htbRCLQT;	/**TABBED PANE USED TO RESTRICT ACCESS DEPENDING ON USER TYPE.*/
	private JTabbedPane	tbpMAIN;
	private JPanel		pnlTEMP;	/**JPANEL FOR tblPSDTL*/
	private JPanel		pnlPSDTL;	/**JPANEL FOR tblRCLQT*/
	private JPanel		pnlRCLQT;	/**JPANEL FOR tblRECIP*/
	private JPanel		pnlRECIP;	/**COST DETAILS*/
	private JPanel		pnlCTDTL;	/**QUALITY DETAILS*/
	private JPanel		pnlQLDTL;	/**PRIME DETAILS*/
	private JPanel		pnlPMDTL;
	private DefaultCellEditor	dceTBLUOM;	/**ARRAY TO MARK OLD DATA OF RECYCLE DURING MODIFICATION*/
	private boolean []	flaFLGRC,flaFLWIP;	/**ARRAY FOR RUN CONSUMPTION QTY; RAW MATERIAL WISE*/
	private double[]	dbaARQTY;	/**ARRAY FOR RECYCLED WTY; RUN WISE*/
	private double[]	dbaARRRC;	/**ARRAY FOR WIP QTY; RUN WISE*/
	private double[]	dbaARWIP;	/**TOTAL WIP QUANTITY	*/
	private double		dblWIPQT=0.0;	/**TOTAL RECYCLE QUANTITY*/
	private double		dblRCLQT=0.0;	/**CONVERSION FACTORS FOR UOM*/
	private static final double dblKGTGM_st=1000.0,dblKGTMT_st=0.001;/**TO STORE CURRENT GRADE CODE. WILL BE COMPARED ON ACP OF txtGRDCD.*/
	private String		strGRDCD="";
	private double		dblTOTPR;
	
	/** Constructs form with one subsystem combo	 */
	pr_mernm()
	{
		super(2);
		cmbUOMCD=new JComboBox();cmbUOMCD.addItem("MT");cmbUOMCD.addItem("Kg");cmbUOMCD.addItem("gm");
		cmbUOMCD.addActionListener(this);
		htbRCLQT=new Hashtable<String,String>(5,1);htbWIPQT=new Hashtable<String,String>(5,1);
		pnlCTDTL=new JPanel(null);pnlPMDTL=new JPanel(null);pnlQLDTL=new JPanel(null);pnlPSDTL=new JPanel(null);
		tbpMAIN=new JTabbedPane();
		dbaARQTY=new double[20];
		dbaARRRC=new double[20];dbaARWIP=new double[20];
//CREATING JTABLE FOR RECIPE DETAILS & RM QTY CONSUMED IN THE RUN.
		setMatrix(19,4);
		String[] L_staNAMES=new String[]{"FL","Material Code","Description","Manufacturer","Lot No.","%age","Total Consumption","UOM","Base Mtl"};
		int[] L_inaWID=new int[]{20,100,150,75,75,75,75,40,75};
		int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
		int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		tblRECIP=crtTBLPNL(pnlRECIP=new JPanel(null),L_staNAMES,10,1,1,5,4,L_inaWID,new int[]{0,8});
		tblRECIP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		((cl_JTBL)tblRECIP).setCellEditor(7,cmbUOMCD);
		tblRECIP.clrTABLE();
		tblRECIP.addKeyListener(this);
		flaFLGRC=new boolean[20];flaFLWIP=new boolean[20];
//CREATING TABLE FOR PS CONSUMPTION DETAILS
		L_staNAMES=new String[]{"FL","Grade Code","Description","Manufacturer","Lot No.","Total Consumption","Actual Consumption","UOM"};
		L_inaWID=new int[]{20,100,150,75,75,100,100,75};
		tblPSDTL=(cl_JTBL)crtTBLPNL(pnlPSDTL=new JPanel(null),L_staNAMES,10,1,1,6,3.95,L_inaWID,new int[]{0});
		tblPSDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblPSDTL.setCellEditor(6,txtPSDTL=new TxtNumLimit(7.6));
		txtPSDTL.addFocusListener(this);
		for(int i=0;i<tblPSDTL.getColumnCount();i++)
		{
			tblPSDTL.cmpEDITR[i].addKeyListener(this);
			tblPSDTL.cmpEDITR[i].addFocusListener(this);
		}
//ADDING BASIC DETAILS OF RUN 
		add(new JLabel("Run No. : "),1,1,1,1,this,'L');
		add(txtRUNNO=new TxtNumLimit(8),1,2,1,1,this,'L');
		add(new JLabel("Run Start Date/Time : "),1,3,1,1,this,'L');
		add(txtRNSDT=new TxtDate(),1,4,1,0.5,this,'L');
		add(txtRNSTM=new TxtNumLimit(5),1,4,1,0.5,this,'R');
		add(new JLabel("Grade Code/Description : "),2,1,1,1,this,'L');
		add(txtGRDCD=new TxtLimit(10),2,2,1,0.5,this,'L');
		add(txtGRDDS=new TxtLimit(11),2,2,1,0.5,this,'R');
		add(new JLabel("Run End Date/Time : "),2,3,1,1,this,'L');
		add(txtRNEDT=new TxtDate(),2,4,1,0.5,this,'L');
		add(txtRNETM=new TxtTime(),2,4,1,0.5,this,'R');
		add(pnlRECIP,4,1,6,4,this,'L');//ADDING JTABLE
		add(new JLabel("Production Request Details : "),3,1,1,1,this,'L');
		setMatrix(19,12);
		setHGAP(1);
		add(txtCMRTP=new TxtLimit(10),3,4,1,1,this,'L');
		add(txtCMRNO=new TxtLimit(10),3,5,1,1,this,'L');
		add(txtCMRRV=new TxtLimit(10),3,6,1,0.88,this,'L');
		setMatrix(19,4);
//ADDING POLYSTYRENE COMPOSITION DETAILS TAB
		tbpMAIN.add(pnlPSDTL,"PS Details");
//ADDING PRODUCT ANALYSIS DETAILS TAB.
		add(new JLabel("Production from WIP : "),1,1,1,1,pnlPMDTL,'L');
		add(txtWIPQT=new TxtNumLimit(7.3),1,2,1,1,pnlPMDTL,'L');
		add(new JLabel("Waste Generated : "),2,1,1,1,pnlPMDTL,'L');
		add(txtWSTQT=new TxtNumLimit(7.3),2,2,1,1,pnlPMDTL,'L');
		add(new JLabel("Reprocess Quantity : "),3,1,1,1,pnlPMDTL,'L');
		add(txtRPRQT=new TxtNumLimit(7.3),3,2,1,1,pnlPMDTL,'L');
		add(new JLabel("Recycled Quantity : "),4,1,1,1,pnlPMDTL,'L');
		add(txtRCLQT=new TxtNumLimit(7.3),4,2,1,1,pnlPMDTL,'L');
		add(new JLabel("Sample Quantity : "),5,1,1,1,pnlPMDTL,'L');
		add(txtSMPQT=new TxtNumLimit(7.3),5,2,1,1,pnlPMDTL,'L');
		add(new JLabel("Other Losses : "),6,1,1,1,pnlPMDTL,'L');
		add(txtLOSQT=new TxtNumLimit(7.3),6,2,1,1,pnlPMDTL,'L');
		add(new JLabel("Total Production : "),1,3,1,1,pnlPMDTL,'L');
		add(txtTOTPR=new TxtNumLimit(7.3),1,4,1,0.95,pnlPMDTL,'L');
		add(new JLabel("Total Prime : "),2,3,1,1,pnlPMDTL,'L');
		add(txtTOTPM=new TxtNumLimit(7.3),2,4,1,0.95,pnlPMDTL,'L');
		add(new JLabel("Declared Prime : "),3,3,1,1,pnlPMDTL,'L');
		add(txtDCLPM=new TxtNumLimit(7.3),3,4,1,0.95,pnlPMDTL,'L');
		add(new JLabel("First Time Prime : "),4,3,1,1,pnlPMDTL,'L');
		add(txtFTMPM=new TxtNumLimit(7.3),4,4,1,0.95,pnlPMDTL,'L');
		add(new JLabel("First Pass Yield : "),5,3,1,1,pnlPMDTL,'L');
		add(txtFPSYD=new TxtNumLimit(4.2),5,4,1,0.95,pnlPMDTL,'L');
		add(new JLabel("Total Yield : "),6,3,1,1,pnlPMDTL,'L');
		add(txtTOTYD=new TxtNumLimit(4.2),6,4,1,0.95,pnlPMDTL,'L');
		tbpMAIN.add(pnlPMDTL,"Prime Details");
		add(tbpMAIN,10,1,8,4,this,'L');
//ADDING PRODUCT COSTING DETAILS TAB.
		add(new JLabel("Sp. Raw Material cost : "),1,1,1,1,pnlCTDTL,'L');
		add(txtRMSPC=new TxtNumLimit(7.2),1,2,1,1,pnlCTDTL,'L');
		add(new JLabel("Sp. Utility Cost : "),2,1,1,1,pnlCTDTL,'L');
		add(txtUTSPC=new TxtNumLimit(7.2),2,2,1,1,pnlCTDTL,'L');
		add(new JLabel("Sp. Trial Cost : "),3,1,1,1,pnlCTDTL,'L');
		add(txtTLSPC=new TxtNumLimit(7.2),3,2,1,1,pnlCTDTL,'L');
		add(new JLabel("Sp. Sampling Cost : "),4,1,1,1,pnlCTDTL,'L');
		add(txtSMSPC=new TxtNumLimit(7.2),4,2,1,1,pnlCTDTL,'L');
		add(new JLabel("Sp. Reprocessing Cost : "),5,1,1,1,pnlCTDTL,'L');
		add(txtRPSPC=new TxtNumLimit(7.2),5,2,1,1,pnlCTDTL,'L');
		add(new JLabel("Sp. Recycling Cost : "),6,1,1,1,pnlCTDTL,'L');
		add(txtRCSPC=new TxtNumLimit(7.2),6,2,1,1,pnlCTDTL,'L');
		add(new JLabel("Sp. Reprocessing Gain : "),1,3,1,1,pnlCTDTL,'L');
		add(txtRPSPG=new TxtNumLimit(7.2),1,4,1,1,pnlCTDTL,'L');
		add(new JLabel("Sp. Recycling Gain : "),2,3,1,1,pnlCTDTL,'L');
		add(txtRCSPG=new TxtNumLimit(7.2),2,4,1,1,pnlCTDTL,'L');
		add(new JLabel("Minimum Sp. Production cost : "),3,3,1,1,pnlCTDTL,'L');
		add(txtMNSPC=new TxtNumLimit(7.2),3,4,1,1,pnlCTDTL,'L');
		tbpMAIN.add(pnlCTDTL,"Costing Details");
//ADDING QUALITY DETAILS TAB
		add(new JLabel("Test Results for the Run : "),1,1,1,1,pnlQLDTL,'L');
		L_staNAMES=new String[]{"FL","Material Code","Description","Manufacturer","Lot No.","%age","Total Consumption","UOM","Base Mtl"};
		L_inaWID=new int[]{20,100,150,75,75,75,75,40,75};
		tblQLDTL=crtTBLPNL(pnlQLDTL,L_staNAMES,10,2,1,5,4,L_inaWID,new int[]{0}) ;
		tblQLDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblQLDTL.clrTABLE();
		tbpMAIN.add(pnlQLDTL,"Quality Details");
	}
	/**<b>TASKS</b>
	 * <br>&nbsp&nbsp&nbsp&nbsptxtWIPQT : SetEditable(true)
	 * <br>&nbsp&nbsp&nbsp&nbsptxtRCLQT : SetEditable(true)
	 * <br>Disables direct entry from user. Entry allowed only thro' details windows.	 */
	public void keyReleased(KeyEvent L_KE)
	{
		txtWIPQT.setEditable(true);
		txtRCLQT.setEditable(true);
	}
	/**<b> TASKS : </b>
	 * <br>&nbsp&nbsp&nbsp&nbsptxtRUNNO : HELP - Run numbers and run details ordered by runno
	  * <br>&nbsp&nbsp&nbsp&nbsptxtRCLQT||txtWIPQT : HELP - Details of resp material available in internal stores.
	  * (br> shown in dialog window
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		int key=L_KE.getKeyCode();
		if(M_objSOURC==txtWIPQT&&key!=L_KE.VK_F1)
		{
			txtWIPQT.setEditable(false);
		}
		else if(M_objSOURC==txtRCLQT&&key!=L_KE.VK_F1)
		{
			txtRCLQT.setEditable(false);
		}
		try{
		if(M_objSOURC==txtRUNNO&&L_KE.getKeyCode()==L_KE.VK_F1)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Addition"))
			{
				M_strSQLQRY="SELECT RN_RUNNO,PR_PRDDS,RN_RUNST,PR_PRDCD FROM pr_RNMST,CO_PRMST WHERE RN_RUNED is NULL and ltrim(str(RN_GRDCD,20,0))=PR_PRDCD ORDER BY RN_RUNNO";
			}
			else
			{
				M_strSQLQRY="SELECT RN_RUNNO,PR_PRDDS,RN_RUNST,PR_PRDCD FROM pr_RNMST,CO_PRMST WHERE ltrim(str(RN_GRDCD,20,0))=PR_PRDCD ORDER BY RN_RUNNO";
			}
			M_strHLPFLD="txtRUNNO";
			cl_hlp(M_strSQLQRY ,1,1,new String[] {"Run No.","Grade","Run Start","Grade Code"},4,"CT");
		}
		else if(M_objSOURC==txtRCLQT&&key==L_KE.VK_F1)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Addition"))
			{
				String[] L_staNAMES=new String[]{"FL","Run Number","Grade","Available Quantity","Recycle Quantity","Grade Code"};
				int[] L_inaWID=new int[]{20,100,150,100,75,75};
				JPanel pnlTEMP=new JPanel();
				pnlRCLQT=new JPanel(null);
				pnlRCLQT.setBounds(10,30,575,200);
				int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
				int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
				tblRCLQT=(cl_JTBL)crtTBLPNL(pnlRCLQT,L_staNAMES,20,1,1,4,2.9,L_inaWID,new int[]{0,9}) ;
				tblRCLQT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	//FETCHING DETAILS OF RUNWISE WASTE MATERIAL 			
				M_strSQLQRY="SELECT RN_RUNNO,RN_GRDDS,RN_WAVQT,RN_GRDCD FROM pr_RNMST where RN_WAVQT>0 ";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET!=null)
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
					pnlTEMP.add(btnRCLQTY=new JButton("Ok"));
					pnlTEMP.add(btnRCLQTN=new JButton("Cancel"));
					dlgRCLQT.getContentPane().setLayout(null);
					dlgRCLQT.getContentPane().add(pnlRCLQT);
					dlgRCLQT.getContentPane().add(pnlTEMP);
					pnlTEMP.setBounds(50,250,500,100);
					dlgRCLQT.setBounds(100,100,600,400);
					//dlgRCLQT.show();	deprecated in 1.6
					dlgRCLQT.setVisible(true);	
					dlgRCLQT.toFront();
					btnRCLQTY.addActionListener(this);
					btnRCLQTN.addActionListener(this);
					tblRCLQT.setEnabled(false);
					tblRCLQT.setCellEditor(4,new TxtNumLimit(6.3));
					tblRCLQT.cmpEDITR[4].setEnabled(true);
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Modification"))
			{
				String[] L_staNAMES=new String[]{"FL","Run Number","Grade","Available Quantity","Recycle Quantity","Grade Code"};
				int[] L_inaWID=new int[]{20,100,150,100,75,75};
				JPanel pnlTEMP=new JPanel();
				pnlRCLQT=new JPanel(null);
				pnlRCLQT.setBounds(10,30,575,200);
				int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
				int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
				tblRCLQT=(cl_JTBL)crtTBLPNL(pnlRCLQT,L_staNAMES,20,1,1,4,2.9,L_inaWID,new int[]{0,9}) ;
				tblRCLQT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	//FETCHING DETAILS OF MATERIAL RECYCLED
				
	//FETCHING DETAILS OF RUNWISE WASTE MATERIAL 			
				M_strSQLQRY="SELECT RCT_PRRUN,RCT_PRGRD,RCT_RCLQT,RN_WAVQT,RN_GRDCD FROM pr_RCTRN,pr_RNMST "
					+" where RCT_RCRUN='"+txtRUNNO.getText()
					+"' and RCT_RCGRD='"+txtGRDCD.getText()
					+"' and RCT_MTLTP='R"
					+"' and RCT_PRRUN=RN_RUNNO and RCT_PRGRD=ltrim(str(RN_GRDCD,20,0))";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET!=null)
				{
					int i=0;
					while(M_rstRSSET.next())
					{
						tblRCLQT.setValueAt(M_rstRSSET.getString("RCT_PRRUN"),i,1);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RCT_PRGRD"),i,2);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_WAVQT"),i,3);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RCT_RCLQT"),i,4);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDCD"),i,5);
						tblRCLQT.setValueAt(new Boolean(true),i,0);
						flaFLGRC[i]=true;
						dbaARRRC[i]=M_rstRSSET.getDouble("RCT_RCLQT");
						i++;
					}
					M_strSQLQRY="SELECT RN_RUNNO,RN_GRDDS,RN_GRDCD,RN_WAVQT FROM pr_RNMST "
						+" where RN_WAVQT>0";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET!=null)
					{
						i=0;
						while(M_rstRSSET.next())
						{
							int j=0;
							boolean flag=true;
							for(j=0;j<tblRCLQT.getRowCount();j++)
							{
								if((tblRCLQT.getValueAt(j,1).toString().equals(M_rstRSSET.getString("RN_RUNNO"))&&tblRCLQT.getValueAt(j,2).toString().equals(M_rstRSSET.getString("RN_GRDCD"))))
								{
									flag=false;
									break;
								}
								if((tblRCLQT.getValueAt(j,1).toString().equals("")&&tblRCLQT.getValueAt(j,2).toString().equals("")))
									break;
							}
							if(flag)
							{
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_RUNNO"),j,1);
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDDS"),j,2);
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_WAVQT"),j,3);
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDCD"),j,5);
							}
							i++;
						}
					}		
					dlgRCLQT=new JDialog();
					pnlTEMP.add(btnRCLQTY=new JButton("Ok"));
					pnlTEMP.add(btnRCLQTN=new JButton("Cancel"));
					dlgRCLQT.getContentPane().setLayout(null);
					dlgRCLQT.getContentPane().add(pnlRCLQT);
					dlgRCLQT.getContentPane().add(pnlTEMP);
					pnlTEMP.setBounds(50,250,500,100);
					dlgRCLQT.setBounds(100,100,600,400);
					//dlgRCLQT.show();	deprecated in 1.6
					dlgRCLQT.setVisible(true);
					dlgRCLQT.toFront();
					btnRCLQTY.addActionListener(this);
					btnRCLQTN.addActionListener(this);
					tblRCLQT.setEnabled(false);
					tblRCLQT.setCellEditor(4,new TxtNumLimit(6.3));
					tblRCLQT.cmpEDITR[4].setEnabled(true);
				}
			}
		}
		else if(M_objSOURC==txtWIPQT&&key==L_KE.VK_F1)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Addition"))
			{
				String[] L_staNAMES=new String[]{"FL","Run Number","Grade","Available Quantity","Recycle Quantity","Grade Code"};
				int[] L_inaWID=new int[]{20,100,150,100,75,75};
				JPanel pnlTEMP=new JPanel();
				pnlRCLQT=new JPanel(null);
				pnlRCLQT.setBounds(10,30,575,200);
				int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
				int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
				tblRCLQT=(cl_JTBL)crtTBLPNL(pnlRCLQT,L_staNAMES,20,1,1,4,2.9,L_inaWID,new int[]{0,9}) ;
				tblRCLQT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	//FETCHING DETAILS OF RUNWISE WASTE MATERIAL 			
				M_strSQLQRY="SELECT RN_RUNNO,RN_GRDDS,RN_WIPAV,RN_GRDCD FROM pr_RNMST where RN_WIPAV>0 ";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET!=null)
				{
					int i=0;
					while(M_rstRSSET.next())
					{
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_RUNNO"),i,1);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDDS"),i,2);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_WIPAV"),i,3);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDCD"),i,5);
						i++;
					}
					dlgRCLQT=new JDialog();
					pnlTEMP.add(btnWIPQTY=new JButton("Ok"));
					pnlTEMP.add(btnWIPQTN=new JButton("Cancel"));
					dlgRCLQT.getContentPane().setLayout(null);
					dlgRCLQT.getContentPane().add(pnlRCLQT);
					dlgRCLQT.getContentPane().add(pnlTEMP);
					pnlTEMP.setBounds(50,250,500,100);
					dlgRCLQT.setBounds(100,100,600,400);
					//dlgRCLQT.show();	deprecated in 1.6
					dlgRCLQT.setVisible(true);
					dlgRCLQT.toFront();
					btnWIPQTY.addActionListener(this);
					btnWIPQTN.addActionListener(this);
					tblRCLQT.setEnabled(false);
					tblRCLQT.setCellEditor(4,new TxtNumLimit(6.3));
					tblRCLQT.cmpEDITR[4].setEnabled(true);
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Modification"))
			{
				String[] L_staNAMES=new String[]{"FL","Run Number","Grade","Available Quantity","Recycle Quantity","Grade Code"};
				int[] L_inaWID=new int[]{20,100,150,100,75,75};
				JPanel pnlTEMP=new JPanel();
				pnlRCLQT=new JPanel(null);
				pnlRCLQT.setBounds(10,30,575,200);
				int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
				int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
				tblRCLQT=(cl_JTBL)crtTBLPNL(pnlRCLQT,L_staNAMES,20,1,1,4,2.9,L_inaWID,new int[]{0,9}) ;
				tblRCLQT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	//FETCHING DETAILS OF MATERIAL RECYCLED
				
	//FETCHING DETAILS OF RUNWISE WASTE MATERIAL 			
				M_strSQLQRY="SELECT RCT_PRRUN,RCT_PRGRD,RCT_RCLQT,RN_WIPAV,RN_GRDCD FROM pr_RCTRN,pr_RNMST "
					+" where RCT_RCRUN='"+txtRUNNO.getText()
					+"' and RCT_RCGRD='"+txtGRDCD.getText()
					+"' and RCT_MTLTP='W"
					+"' and RCT_PRRUN=RN_RUNNO and RCT_PRGRD=ltrim(str(RN_GRDCD,20,0))";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET!=null)
				{
					int i=0;
					while(M_rstRSSET.next())
					{
						tblRCLQT.setValueAt(M_rstRSSET.getString("RCT_PRRUN"),i,1);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RCT_PRGRD"),i,2);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_WIPAV"),i,3);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RCT_RCLQT"),i,4);
						tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDCD"),i,5);
						tblRCLQT.setValueAt(new Boolean(true),i,0);
						flaFLWIP[i]=true;
						dbaARWIP[i]=M_rstRSSET.getDouble("RCT_RCLQT");
						i++;
					}
					M_strSQLQRY="SELECT RN_RUNNO,RN_GRDDS,RN_GRDCD,RN_WIPAV FROM pr_RNMST "
						+" where RN_WIPAV>0";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET!=null)
					{
						i=0;
						while(M_rstRSSET.next())
						{
							int j=0;
							boolean flag=true;
							for(j=0;j<tblRCLQT.getRowCount();j++)
							{
								if((tblRCLQT.getValueAt(j,1).toString().equals(M_rstRSSET.getString("RN_RUNNO"))&&tblRCLQT.getValueAt(j,2).toString().equals(M_rstRSSET.getString("RN_GRDCD"))))
								{
									flag=false;
									break;
								}
								if((tblRCLQT.getValueAt(j,1).toString().equals("")&&tblRCLQT.getValueAt(j,2).toString().equals("")))
									break;
							}
							if(flag)
							{
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_RUNNO"),j,1);
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDDS"),j,2);
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_WIPAV"),j,3);
								tblRCLQT.setValueAt(M_rstRSSET.getString("RN_GRDCD"),j,5);
							}
							i++;
						}
					}		
					dlgRCLQT=new JDialog();
					pnlTEMP.add(btnWIPQTY=new JButton("Ok"));
					pnlTEMP.add(btnWIPQTN=new JButton("Cancel"));
					dlgRCLQT.getContentPane().setLayout(null);
					dlgRCLQT.getContentPane().add(pnlRCLQT);
					dlgRCLQT.getContentPane().add(pnlTEMP);
					pnlTEMP.setBounds(50,250,500,100);
					dlgRCLQT.setBounds(100,100,600,400);
					//dlgRCLQT.show();	deprecated in 1.6
					dlgRCLQT.setVisible(true);
					dlgRCLQT.toFront();
					btnWIPQTY.addActionListener(this);
					btnWIPQTN.addActionListener(this);
					tblRCLQT.setEnabled(false);
					tblRCLQT.setCellEditor(4,new TxtNumLimit(6.3));
					tblRCLQT.cmpEDITR[4].setEnabled(true);
				}
			}
		}
		}catch (Exception E)
		{System.out.println(E);}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}

	/**<b>TASKS</b>
	 * <br>&nbsp&nbsp&nbsp&nbsptxtRUNNO : Fetch details of the run.
	 * <br>&nbsp&nbsp&nbsp&nbsptxtWSTQT||txtRCLQT||txtSMPQT||txtRPRQT||txtTOTPM : Calculate run staistics
	 * <br>&nbsp&nbsp&nbsp&nbspcmbUOMCD : Change display unit of quantities.
	 * <br>&nbsp&nbsp&nbsp&nbspbtnRCLQTY||WIPQTY : Calculate total, display in textfields and store details in hashtable.
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{
					txtRUNNO.requestFocus();
					txtRUNNO.setText("");
				}
			}
			else if(M_objSOURC==txtRUNNO)
			{
				((cl_JTBL)tblRECIP).clrTABLE();txtRNEDT.setText("");txtRNETM.setText("");
				((cl_JTBL)tblPSDTL).clrTABLE();
				txtLOSQT.setText("0.0");txtFPSYD.setText("0.0");txtTOTYD.setText("0.0");
				M_strSQLQRY="select BTT_runno,BTT_mtlcd,BTT_mtlds,BTT_mtlmf,BTT_mtlbt,BTT_bsmfl,sum(BTT_mtlqt) BTT_MTLQT,rp_pctqt from"
							+" pr_bttrn left outer join pr_rpmst on BTT_runno=rp_runno and BTT_mtlcd=rp_mtlcd"
							+" where BTT_runno='"+txtRUNNO.getText()
							+" ' and BTT_grdcd ='"+txtGRDCD.getText()
							+" ' and BTT_batno <>0 "
							+" group by BTT_runno,BTT_mtlcd,BTT_mtlds,BTT_mtlmf,BTT_mtlbt,BTT_bsmfl,rp_pctqt "; 
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				int i=0,z=0;
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{
						tblRECIP.setValueAt(M_rstRSSET.getString("BTT_MTLCD"),i,1);
						tblRECIP.setValueAt(M_rstRSSET.getString("BTT_MTLDS"),i,2);
						tblRECIP.setValueAt(nvlSTRVL(M_rstRSSET.getString("RP_PCTQT"),"    -"),i,5);
						tblRECIP.setValueAt(nvlSTRVL(M_rstRSSET.getString("BTT_MTLQT"),"    -"),i,6);
						tblRECIP.setValueAt("Kg",i,7);
						tblRECIP.setValueAt(nvlSTRVL(M_rstRSSET.getString("BTT_MTLMF"),"    -"),i,3);
						tblRECIP.setValueAt(nvlSTRVL(M_rstRSSET.getString("BTT_MTLBT"),"    -"),i,4);
						tblRECIP.setValueAt((M_rstRSSET.getString("BTT_BSMFL").equals("Y") ? new Boolean(true) : new Boolean(false)),i,8);
						dbaARQTY[i]=M_rstRSSET.getDouble("BTT_mtlqt");
						if(M_rstRSSET.getString("BTT_BSMFL").equals("Y"))
						{
							tblPSDTL.setValueAt(M_rstRSSET.getString("BTT_MTLCD"),z,1);
							tblPSDTL.setValueAt(M_rstRSSET.getString("BTT_MTLDS"),z,2);
							tblPSDTL.setValueAt(setNumberFormat((M_rstRSSET.getDouble("BTT_MTLQT")/1000.0),3),z,5);
							if(setNumberFormat((M_rstRSSET.getDouble("BTT_MTLQT")/1000.0),3).equals("0.000"))
								tblPSDTL.setValueAt(setNumberFormat((M_rstRSSET.getDouble("BTT_MTLQT")/1000.0),6),z,6);
							else
								tblPSDTL.setValueAt(setNumberFormat((M_rstRSSET.getDouble("BTT_MTLQT")/1000.0),3),z,6);
							tblPSDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BTT_MTLMF"),"    -"),z,3);
							tblPSDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BTT_MTLBT"),"    -"),z,4);
							tblPSDTL.setValueAt("MT",z,7);
							z++;
						}
						i++;
					}
					M_strSQLQRY="SELECT * FROM pr_BTTRN WHERE BTT_RUNNO='"+txtRUNNO.getText()+"' AND BTT_GRDCD='"+txtGRDCD.getText()+"' AND BTT_BATNO=0 ";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET!=null)
					{
						String L_strSTRTMP="";	
						boolean flag=false;
						while(M_rstRSSET.next())
						{
							L_strSTRTMP=M_rstRSSET.getString("BTT_MTLCD");
							flag=false;
							for(i=0;i<tblPSDTL.getRowCount();i++)
							{
								if(tblPSDTL.getValueAt(i,1).toString().equals(L_strSTRTMP))
								{
									tblPSDTL.setValueAt(Double.toString(M_rstRSSET.getDouble("BTT_MTLQT")/1000.0),i,6);
									flag=true;
									break;
								}
							}
							if(!flag)
							{
								for(i=0;i<tblPSDTL.getRowCount();i++)
								{
									if(tblPSDTL.getValueAt(i,1).toString().length()==0)
									{
										tblPSDTL.setValueAt(M_rstRSSET.getString("BTT_MTLCD"),i,1);
										tblPSDTL.setValueAt(M_rstRSSET.getString("BTT_MTLDS"),i,2);
										tblPSDTL.setValueAt(M_rstRSSET.getString("BTT_MTLMF"),i,3);
										tblPSDTL.setValueAt(M_rstRSSET.getString("BTT_MTLBT"),i,4);
										tblPSDTL.setValueAt(Double.toString(M_rstRSSET.getDouble("BTT_MTLQT")/1000.0),i,6);
										tblPSDTL.setValueAt("MT",i,7);
										break;
									}
								}
							}
						}
					}
					M_strSQLQRY="SELECT * FROM pr_RNMST WHERE RN_RUNNO='"+txtRUNNO.getText()+"' AND RN_GRDCD = '"+txtGRDCD.getText()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET!=null)
					{
						while(M_rstRSSET.next())
						{	
							txtWSTQT.setText(nvlSTRVL(M_rstRSSET.getString("RN_WSTQT"),""));
							txtRCLQT.setText(nvlSTRVL(M_rstRSSET.getString("RN_RCLQT"),""));
							txtRPRQT.setText(nvlSTRVL(M_rstRSSET.getString("RN_RPRQT"),""));
							txtSMPQT.setText(nvlSTRVL(M_rstRSSET.getString("RN_SMPQT"),""));
							txtLOSQT.setText(nvlSTRVL(M_rstRSSET.getString("RN_LOSQT"),""));
							txtDCLPM.setText(nvlSTRVL(M_rstRSSET.getString("RN_DCLPM"),""));
							txtWIPQT.setText(setNumberFormat(M_rstRSSET.getDouble("RN_DCLPM")-M_rstRSSET.getDouble("RN_TOTPM")+M_rstRSSET.getDouble("RN_WIPQT"),3));
							dblTOTPR=M_rstRSSET.getDouble("RN_TOTPM")+M_rstRSSET.getDouble("RN_WSTQT")+M_rstRSSET.getDouble("RN_SMPQT")+M_rstRSSET.getDouble("RN_LOSQT");
							txtTOTPR.setText(Double.toString(dblTOTPR));
							txtTOTPM.setText(nvlSTRVL(M_rstRSSET.getString("RN_TOTPM"),""));
							String L_strSTRTMP="";
							if(M_rstRSSET.getTimestamp("RN_RUNED")!=null)
							{
								L_strSTRTMP=M_fmtLCDTM.format(M_rstRSSET.getTimestamp("RN_RUNED"));//cc_dattm.setDTMFMT(M_rstRSSET.getString("RN_RUNED"));
								if(L_strSTRTMP.length()>5){
								String date=L_strSTRTMP.substring(0,10);
								String time=L_strSTRTMP.substring(10,16).trim();
								txtRNEDT.setText(date);
								txtRNETM.setText(time);
								}
							}
							L_strSTRTMP=M_fmtLCDTM.format(M_rstRSSET.getTimestamp("RN_RUNST"));
							if(L_strSTRTMP.length()>5)
							{
								String date=L_strSTRTMP.substring(0,10);
								String time=L_strSTRTMP.substring(10,16).trim();
								txtRNSDT.setText(date);
								txtRNSTM.setText(time);
								txtRNSDT.setEnabled(false);
								txtRNSTM.setEnabled(false);
							}
							txtCMRNO.setText(nvlSTRVL(M_rstRSSET.getString("RN_CMRNO"),""));
							txtCMRRV.setText(nvlSTRVL(M_rstRSSET.getString("RN_CMRRV"),""));
							L_strSTRTMP=nvlSTRVL(M_rstRSSET.getString("RN_CMRTP"),"");
							if(L_strSTRTMP.equals("C"))
								txtCMRTP.setText("CMR");
							else if(L_strSTRTMP.equals("D"))
								txtCMRTP.setText("DWR");
							else if(L_strSTRTMP.equals("O"))
								txtCMRTP.setText("Other");
						}
						setTOTPM();
					}
				}
				
			}
			else if(M_objSOURC==txtWSTQT||M_objSOURC==txtRCLQT||M_objSOURC==txtSMPQT)
			{
				setTOTPM();
			}
			else if(M_objSOURC==txtRPRQT)
			{
				txtFTMPM.setText(setNumberFormat((Double.parseDouble(nvlSTRVL(txtTOTPM.getText(),"0.0"))-Double.parseDouble(nvlSTRVL(txtRPRQT.getText(),"0.0"))),3));
				txtFPSYD.setText(setNumberFormat((Double.parseDouble(nvlSTRVL(txtFTMPM.getText(),"0.0"))/Double.parseDouble(nvlSTRVL(txtTOTPM.getText(),"0.0"))*100.0),2));
			}
			else if(M_objSOURC==txtTOTPM)
			{
				txtLOSQT.setText(setNumberFormat((Double.parseDouble(nvlSTRVL(txtTOTPR.getText(),"0.0"))-(Double.parseDouble(nvlSTRVL(txtWSTQT.getText(),"0.0"))+Double.parseDouble(nvlSTRVL(txtSMPQT.getText(),"0.0"))+Double.parseDouble(nvlSTRVL(txtTOTPM.getText(),"0.0")))),3));
				setTOTPM();
			}
			else if(M_objSOURC==cmbUOMCD)
			{
				int row=tblRECIP.getSelectedRow();
				if(row!=-1)
				{
					if(tblRECIP.getValueAt(row,7).toString().equals("MT"))
					{
						Double d=new Double(dbaARQTY[row]*dblKGTMT_st);
						tblRECIP.setValueAt(d.toString(),row,6);
					}
					else if(tblRECIP.getValueAt(row,7).toString().equals("Kg"))
					{
						Double d=new Double(dbaARQTY[row]);
						tblRECIP.setValueAt(d.toString(),row,6);
					}
					else if(tblRECIP.getValueAt(row,7).toString().equals("gm"))
					{
						Double d=new Double(dbaARQTY[row]*dblKGTGM_st);
						tblRECIP.setValueAt(d.toString(),row,6);
					}
				}
			}
			else if(M_objSOURC==btnRCLQTY)
			{
//CODE TO PUT TOTAL RECYCLE QUANTITY IN txtRCLQT AND TO PUT RELATED DETAILS IN htbRCLQT				
				Boolean TRUE=new Boolean(true);
				dblRCLQT=0.0;
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
							dblRCLQT=0.0;
							htbRCLQT.clear();
							break;
						}
						else
						{
							dblRCLQT+=REQQT;
							htbRCLQT.put(Integer.toString(i),tblRCLQT.getValueAt(i,1).toString()+"|"+tblRCLQT.getValueAt(i,4).toString()+"|"+tblRCLQT.getValueAt(i,2).toString()+"|"+tblRCLQT.getValueAt(i,5).toString());
						}
					}
				}
				txtRCLQT.setText(Double.toString(dblRCLQT));
				tblRCLQT=null;
			}
			else if(M_objSOURC==btnRCLQTN)
			{
				dlgRCLQT.dispose();
				tblRCLQT=null;
			}
			else if(M_objSOURC==btnWIPQTY)
			{
//CODE TO PUT TOTAL WIP QUANTITY IN txtRCLQT AND TO PUT RELATED DETAILS IN htbWIPQT				
				Boolean TRUE=new Boolean(true);
				dblWIPQT=0.0;
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
							dblWIPQT=0.0;
							htbWIPQT.clear();
							break;
						}
						else
						{
							dblWIPQT+=REQQT;
							htbWIPQT.put(Integer.toString(i),tblRCLQT.getValueAt(i,1).toString()+"|"+tblRCLQT.getValueAt(i,4).toString()+"|"+tblRCLQT.getValueAt(i,2).toString()+"|"+tblRCLQT.getValueAt(i,5).toString());
						}
					}
				}
				txtWIPQT.setText(Double.toString(dblWIPQT));
				tblRCLQT=null;
			}
			else if(M_objSOURC==btnRCLQTN)
			{
				dlgRCLQT.dispose();
				tblRCLQT=null;
			}
			if(M_objSOURC!=tblRECIP && M_objSOURC!=txtRUNNO && M_objSOURC!=cl_dat.M_cmbOPTN_pbst)
			{
				((JComponent)M_objSOURC).transferFocus();
			}
			
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}catch(Exception E)
		{
			System.out.println(E);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**User friendly messages
	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(!M_flgERROR)
		{
			if(M_objSOURC==txtRUNNO)
				setMSG("Enter Run Number, Press 'F1' for help.. ",'N');
			else if(M_objSOURC==txtRNEDT)
				setMSG("Enter Run End Date 'dd/mm/yyyy'.. ",'N');
			else if(M_objSOURC==txtRNETM)
				setMSG("Enter Run End Time 'hh:mm'.. ",'N');
			else if(M_objSOURC==txtPSDTL)
				setMSG("Enter Actual Consumption Quantity in MT.. ",'N');
			else if(M_objSOURC==tblPSDTL.cmpEDITR[1])
				setMSG("Enter Grade Code, Press 'F1' for help.. ",'N');
			else if(M_objSOURC==tblPSDTL.cmpEDITR[3])
				setMSG("Enter Material Manufacturer.. ",'N');
			else if(M_objSOURC==tblPSDTL.cmpEDITR[4])
				setMSG("Enter Lot Number.. ",'N');
			else if(M_objSOURC==txtWIPQT)
				setMSG("Enter Production from WIP Quantity in MT.. ",'N');
			else if(M_objSOURC==txtWSTQT)
				setMSG("Enter Waste Generated Quantity in MT.. ",'N');
			else if(M_objSOURC==txtRPRQT)
				setMSG("Enter Reprocessed Quantity in MT.. ",'N');
			else if(M_objSOURC==txtRCLQT)
				setMSG("Enter Recycled Quantity in MT.. ",'N');
			else if(M_objSOURC==txtSMPQT)
				setMSG("Enter Sample Quantity in MT.. ",'N');
			else if(M_objSOURC==txtTOTPM)
				setMSG("Enter Total Prime Quantity in MT.. ",'N');
			else if(M_objSOURC==txtDCLPM)
				setMSG("Enter Declared Production Quantity in MT.. ",'N');
		}
	}
	/** txtPSDTL : Recalculate run staticstics with revised PS consumption	 */
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
		if(M_objSOURC==txtPSDTL)
		{
		if(tblPSDTL.isEditing())
			tblPSDTL.getCellEditor().stopCellEditing();
			double L_dblTGTCN=0.0,L_dblACTCN=0.0;
			for (int i=0;i<tblPSDTL.getRowCount();i++)
			{
				if(tblPSDTL.getValueAt(i,1).toString().length()!=0)
				{
					L_dblTGTCN+=Double.parseDouble(nvlSTRVL(tblPSDTL.getValueAt(i,5).toString(),"0"));
					L_dblACTCN+=Double.parseDouble(nvlSTRVL(tblPSDTL.getValueAt(i,6).toString(),"0"));
				}
			}
			txtTOTPR.setText(Double.toString(dblTOTPR-(L_dblTGTCN)+(L_dblACTCN)));
			setTOTPM();
		}
	}
	
	void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtRUNNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtRUNNO.setText(L_STRTKN.nextToken());
				txtGRDDS.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				txtGRDCD.setText(L_STRTKN.nextToken());
			}
		}catch(Exception E)
		{
			System.out.println("Error in mernm : exeHLP \n"+E);
		}
	}
	/** Calculate  & display run statistics
	 */
	private void setTOTPM()
	{
		txtTOTPM.setText(setNumberFormat((Double.parseDouble(nvlSTRVL(txtTOTPR.getText(),"0.0"))-(Double.parseDouble(nvlSTRVL(txtWSTQT.getText(),"0.0"))+Double.parseDouble(nvlSTRVL(txtSMPQT.getText(),"0.0"))+Double.parseDouble(nvlSTRVL(txtLOSQT.getText(),"0.0")))),3));
		txtTOTYD.setText(setNumberFormat((Double.parseDouble(nvlSTRVL(txtTOTPM.getText(),"0.0"))/Double.parseDouble(nvlSTRVL(txtTOTPR.getText(),"0.0"))*100.0),2));
		txtFTMPM.setText(setNumberFormat((Double.parseDouble(nvlSTRVL(txtTOTPM.getText(),"0.0"))-Double.parseDouble(nvlSTRVL(txtRPRQT.getText(),"0.0"))),3));
		txtFPSYD.setText(setNumberFormat((Double.parseDouble(nvlSTRVL(txtFTMPM.getText(),"0.0"))/Double.parseDouble(nvlSTRVL(txtTOTPM.getText(),"0.0"))*100.0),2));
	}
	
	boolean vldDATA()
	{
		if(txtRNEDT.getText().length()==0||txtRNETM.getText().length()==0)
		{
			setMSG("Enter Run End Date & Time ..",'E');
			txtRNEDT.requestFocus();
			return false;
		}
		else if(txtDCLPM.getText().length()==0)
		{
			setMSG("Enter Declared Production tqy. ..",'E');
			txtDCLPM.requestFocus();
		}
		else if((Double.parseDouble(txtWIPQT.getText())+Double.parseDouble(txtTOTPM.getText()))<Double.parseDouble(txtDCLPM.getText()))
		{
			setMSG("Declared production cannot be greater than actual ..",'E');
			txtDCLPM.requestFocus();
		}
		
		return true;
	}
	void exeSAVE()
	{
		try
		{
			if(vldDATA()){
				double L_dblWIPQT=Double.parseDouble(nvlSTRVL(txtTOTPM.getText(),"0.0"))+Double.parseDouble(nvlSTRVL(txtWIPQT.getText(),"0.0"))-Double.parseDouble(nvlSTRVL(txtDCLPM.getText(),"0.0"));
			M_strSQLQRY="UPDATE pr_RNMST SET "
				//+"RN_RUNED= "+cc_dattm.setDBSTM(txtRNEDT.getText()+" "+txtRNETM.getText())+","//RN_RUNED
				+"RN_RUNED= '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtRNEDT.getText()+" "+txtRNETM.getText()))+"',"//RN_RUNST,		
				+"RN_RCLQT= "+nvlSTRVL(txtRCLQT.getText(),"0")+","//RN_RCLQT,
				+"RN_TOTPR= "+nvlSTRVL(txtTOTPR.getText(),"0")+","//RN_TOTPR,
				+"RN_TOTPM= "+nvlSTRVL(txtTOTPM.getText(),"0")+" ,"//RN_TOTPM
				+"RN_DCLPM= "+nvlSTRVL(txtDCLPM.getText(),"0")+" ,"//RN_TOTPM
				+"RN_WSTQT= "+nvlSTRVL(txtWSTQT.getText(),"0")+" ,"//RN_WSTQT
				+"RN_WAVQT= "+nvlSTRVL(txtWSTQT.getText(),"0")+" ,"//RN_WAVQT
				+"RN_WIPQT= "+Double.toString(L_dblWIPQT)+" ,"//RN_WIPQT
				+"RN_WIPAV= "+Double.toString(L_dblWIPQT)+" ,"//RN_WIPAV
				+"RN_SMPQT= "+nvlSTRVL(txtSMPQT.getText(),"0")+" ,"//RN_WSTQT
				+"RN_RPRQT= "+nvlSTRVL(txtRPRQT.getText(),"0")+" ,"//RN_RPRQT
				+"RN_LOSQT= "+nvlSTRVL(txtLOSQT.getText(),"0")+" ,"//RN_LOSQT
				+"RN_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//RN_LUSBY,
				+"RN_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"//RN_LUPDT
				+" where rn_runno='"+txtRUNNO.getText()+"' and rn_grdcd='"+txtGRDCD.getText()+"'";
			cl_dat.M_flgLCUPD_pbst=true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	//UPDATING RUNMASTER FOR WASTE QUANTITY CONSUMPTION
			if(Double.parseDouble(nvlSTRVL(txtRCLQT.getText(),"0.0"))>0.0)
			{
				Enumeration keys=htbRCLQT.keys();
				String L_strGRDDS="",L_strCONQT="",L_strSTRTMP="",L_strGRDCD="";	
				StringTokenizer L_STRTKN=new StringTokenizer(L_strSTRTMP,"|");
				int i=0;
				while(keys.hasMoreElements()&&cl_dat.M_flgLCUPD_pbst)
				{
					L_strSTRTMP=keys.nextElement().toString();
					L_STRTKN=new StringTokenizer(htbRCLQT.get(L_strSTRTMP).toString(),"|");
					L_strSTRTMP=L_STRTKN.nextToken();
					L_strCONQT=L_STRTKN.nextToken();
					L_strGRDDS=L_STRTKN.nextToken();
					L_strGRDCD=L_STRTKN.nextToken();
					if(flaFLGRC[i])
					{
						M_strSQLQRY="UPDATE pr_RNMST SET RN_WAVQT=RN_WAVQT+"+dbaARRRC[i]+"-"+L_strCONQT+" WHERE RN_RUNNO = '"+L_strSTRTMP+"' AND RN_GRDCD='"+L_strGRDCD+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						M_strSQLQRY="UPDATE pr_RCTRN set RCT_RCLQT="+L_strCONQT+" where "
							+"RCT_PRRUN='"+L_strSTRTMP+"' and RCT_PRGRD='"+L_strGRDCD+"' and RCT_RCRUN='"
							+txtRUNNO.getText()+"' and RCT_RCGRD='"+txtGRDCD.getText()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else
					{
						M_strSQLQRY="UPDATE pr_RNMST SET RN_WAVQT=RN_WAVQT-"+L_strCONQT+" WHERE RN_RUNNO = '"+L_strSTRTMP+"' AND RN_GRDCD='"+L_strGRDCD+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						M_strSQLQRY="INSERT INTO pr_RCTRN (RCT_PRRUN,RCT_PRGRD,RCT_RCRUN,RCT_RCGRD,RCT_RCLQT,RCT_MTLTP,RCT_TRNFL,RCT_STSFL,RCT_LUSBY,RCT_LUPDT) VALUES ("
							+"'"+L_strSTRTMP+"',"//RCT_PRRUN
							+"'"+L_strGRDCD+"',"//RCT_PRGCD
							+"'"+txtRUNNO.getText()+"',"//RCT_RCRUN
							+"'"+txtGRDCD.getText()+"',"//RCT_RCGRD
							+""+L_strCONQT+","//RCT_RCLQT
							+"'R',"//RCT_MTLTP
							+getUSGDTL("RCT",'i',"")+")";//RCT_TRNFL,RCT_STSFL,RCT_LUSBY,RCT_LUPDT
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					i++;
				}
				htbRCLQT.clear();
			}
	//UPDATING RNMST FOR WIP QTY
			if(Double.parseDouble(nvlSTRVL(txtWIPQT.getText(),"0.0"))>0.0)
			{
				Enumeration keys=htbWIPQT.keys();
				String L_strGRDDS="",L_strCONQT="",L_strSTRTMP="",L_strGRDCD="";
				StringTokenizer L_STRTKN=new StringTokenizer(L_strSTRTMP,"|");
				int i=0;
				while(keys.hasMoreElements()&&cl_dat.M_flgLCUPD_pbst)
				{
					L_strSTRTMP=keys.nextElement().toString();
					L_STRTKN=new StringTokenizer(htbWIPQT.get(L_strSTRTMP).toString(),"|");
					L_strSTRTMP=L_STRTKN.nextToken();
					L_strCONQT=L_STRTKN.nextToken();
					L_strGRDDS=L_STRTKN.nextToken();
					L_strGRDCD=L_STRTKN.nextToken();
					if(flaFLGRC[i])
					{
						M_strSQLQRY="UPDATE pr_RNMST SET RN_WIPAV=RN_WIPAV+"+dbaARWIP[i]+"-"+L_strCONQT+" WHERE RN_RUNNO = '"+L_strSTRTMP+"' AND RN_GRDCD='"+L_strGRDCD+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						M_strSQLQRY="UPDATE pr_RCTRN set RCT_RCLQT="+L_strCONQT+" where "
							+"RCT_PRRUN='"+L_strSTRTMP+"' and RCT_PRGRD='"+L_strGRDCD+"' and RCT_RCRUN='"
							+txtRUNNO.getText()+"' and RCT_RCGRD='"+txtGRDCD.getText()+"' and RCT_MTLTP='W'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else
					{
						M_strSQLQRY="UPDATE pr_RNMST SET RN_WIPAV=RN_WIPAV-"+L_strCONQT+" WHERE RN_RUNNO = '"+L_strSTRTMP+"' AND RN_GRDCD='"+L_strGRDCD+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						M_strSQLQRY="INSERT INTO pr_RCTRN (RCT_PRRUN,RCT_PRGRD,RCT_RCRUN,RCT_RCGRD,RCT_RCLQT,RCT_MTLTP,RCT_TRNFL,RCT_STSFL,RCT_LUSBY,RCT_LUPDT) VALUES ("
							+"'"+L_strSTRTMP+"',"//RCT_PRRUN
							+"'"+L_strGRDCD+"',"//RCT_PRGRD
							+"'"+txtRUNNO.getText()+"',"//RCT_RCRUN
							+"'"+txtGRDCD.getText()+"',"//RCT_RCGRD
							+""+L_strCONQT+","//RCT_RCLQT							
							+"'W',"//RCT_MTLTP
							+getUSGDTL("RCT",'i',"")+")";//RCT_TRNFL,RCT_STSFL,RCT_LUSBY,RCT_LUPDT
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					i++;
				}
				htbWIPQT.clear();
			}
			int i=0;
			while(cl_dat.M_flgLCUPD_pbst&&tblPSDTL.getValueAt(i,2).toString().length()>0&&(!tblPSDTL.getValueAt(i,5).toString().equals(tblPSDTL.getValueAt(i,6).toString())))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Addition"))
				{
					M_strSQLQRY="INSERT INTO pr_BTTRN (BTT_RUNNO,BTT_GRDCD,BTT_BATNO,BTT_MTLCD,BTT_MTLDS,BTT_MTLMF,BTT_MTLBT,BTT_MTLQT,BTT_BSMFL,BTT_TRNFL,BTT_STSFL,BTT_LUSBY,BTT_LUPDT) VALUES ("
						+"'"+txtRUNNO.getText()+"',"//BTT_RUNNO
						+"'"+txtGRDCD.getText()+"',"//BTT_GRDCD
						+" 0,"//BTT_BATNO
						+"'"+tblPSDTL.getValueAt(i,1).toString()+"',"//BTT_MTLCD
						+"'"+tblPSDTL.getValueAt(i,2).toString()+"',"//BTT_MTLDS
						+"'"+tblPSDTL.getValueAt(i,3).toString()+"',"//BTT_MTLMF
						+"'"+tblPSDTL.getValueAt(i,4).toString()+"',"//BTT_MTLBT
						+""+Double.toString(Double.parseDouble(tblPSDTL.getValueAt(i,6).toString())*1000.0)+","//BTT_MTLQT
						+"'Y',"//BTT_BSMFL
						+getUSGDTL("BTT",'i',"")+")";//BTT_TRNFL,BTT_STSFL,BTT_LUSBY,BTT_LUPDT
				}
				else
				{
					M_strSQLQRY="UPDATE PR_BTTRN SET "
						+" BTT_MTLQT="+Double.toString(Double.parseDouble(tblPSDTL.getValueAt(i,6).toString())*1000.0)+","//BTT_MTLQT
						+"BTT_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//BTT_LUSBY,
						+"BTT_LUPDT="+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+", WHERE "//BTT_LUPDT
						+"BTT_RUNNO="+"'"+txtRUNNO.getText()+"'"//BTT_RUNNO
						+" AND BTT_GRDCD='"+txtGRDCD.getText()+"'"//BTT_GRDCD
						+" AND BTT_BATNO=0"//BTT_BATNO
						+" AND BTT_MTLCD='"+tblPSDTL.getValueAt(i,1).toString()+"'"//BTT_MTLCD
						+" AND BTT_MTLBT='"+tblPSDTL.getValueAt(i,4).toString()+"'";//BTT_MTLBT					
				}
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				i++;
			}
				if(cl_dat.exeDBCMT("exeSAVE"))
					setMSG("Data Saved Successfully ..",'N');
				else
					setMSG("Error occured during saving ..",'E');
		}
			}catch(Exception e)
				{setMSG(e,"Child.exeSAVE");}
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtGRDCD.setEnabled(false);txtGRDDS.setEnabled(false);txtRNSTM.setEnabled(false);
		txtRNSDT.setEnabled(false);txtGRDCD.setEnabled(false);txtLOSQT.setEnabled(false);
		txtGRDCD.setEnabled(false);txtFTMPM.setEnabled(false);txtFPSYD.setEnabled(false);
		txtCMRNO.setEnabled(false);txtCMRRV.setEnabled(false);txtCMRTP.setEnabled(false);
		txtTOTPR.setEnabled(false);txtTOTYD.setEnabled(false);tblPSDTL.setEnabled(L_STAT);
		((cl_JTBL)tblRECIP).setEnabled(false);((cl_JTBL)tblRECIP).cmpEDITR[7].setEnabled(true);
		tblPSDTL.cmpEDITR[5].setEnabled(false);tblPSDTL.cmpEDITR[2].setEnabled(false);tblPSDTL.cmpEDITR[3].setEnabled(false);
		tblPSDTL.cmpEDITR[0].setEnabled(false);
	}
	void clrCOMP()
	{
		super.clrCOMP();
		((cl_JTBL)tblRECIP).clrTABLE();
	}
}