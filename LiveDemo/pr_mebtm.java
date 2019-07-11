 	
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.StringTokenizer;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Process&nbsp;Resource&nbsp;Planning System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>Location Master</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                    Form for modifying                    and retrieving       details of Batches already added into system. </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\pr_melcm.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\pr_melcm.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>01/07/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>For Base classes revision and Subsystem implementation</TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>AAP</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>25/09/2003</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>2.0.0.</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR> */
class pr_mebtm extends cl_pbase 
{
	/**Table for Batch details	 */
	private cl_JTBL			tblBTMST;/** Run number*/
	private TxtNumLimit		txtRUNNO;/** Batch number*/
	private TxtNumLimit		txtBATNO;/** Batch size*/
	private TxtNumLimit		txtBATSZ;/** Recycle Quantity*/
	private TxtNumLimit		txtRCLQT;/** Grade Description*/
	private JTextField		txtGRDDS;/** Line Number*/
	private JComboBox		cmbLINNO;/** Production type - Trial/Prime*/
	private JComboBox		cmbPRDTP;/** Trial count*/
	private int				intTRLCT;/** Grade Code*/
	private String			strGRDCD;/** Trial flag*/
	private String			strTRLFL;/** Batch Date*/
	private TxtDate			txtBATDT;

	pr_mebtm()
	{
		super(2);
		txtRUNNO=new TxtNumLimit(10.0);txtBATNO=new TxtNumLimit(3.0);txtRCLQT=new TxtNumLimit(7.3);
		txtBATSZ=new TxtNumLimit(7.3);txtGRDDS=new JTextField();txtBATDT=new TxtDate();
		cmbPRDTP=new JComboBox();cmbPRDTP.addItem("Trial");cmbPRDTP.addItem("Production");
		cmbPRDTP.addItem("W.I.P.");cmbLINNO=new JComboBox();cmbLINNO.addItem("Select Line No.");
		cmbLINNO.addItem("Line : 51");cmbLINNO.addItem("Line : 52");cmbLINNO.addItem("Line : 53");
		setMatrix(18,4);
		String[] names=new String[]{"FL","Material Code","Description","Manufacturer","Batch No.","Quantity","UOM"};
		int[] wid=new int[]{20,100,150,75,75,75,75,};
		add(new JLabel("Line No. : "),1,1,1,1,this,'L');
		add(cmbLINNO,1,2,1,1,this,'L');
		add(new JLabel("Run Number : "),2,1,1,1,this,'L');
		add(txtRUNNO,2,2,1,1,this,'L');
		add(new JLabel("Batch No. : "),2,3,1,1,this,'L');
		add(txtBATNO,2,4,1,1,this,'L');
		add(new JLabel("Grade : "),3,1,1,1,this,'L');
		add(txtGRDDS,3,2,1,1,this,'L');
		add(new JLabel("Batch Size. : "),3,3,1,1,this,'L');
		add(txtBATSZ,3,4,1,1,this,'L');
		add(new JLabel("Production Type : "),4,1,1,1,this,'L');
		add(cmbPRDTP,4,2,1,1,this,'L');
		add(new JLabel("Batch Date : "),4,3,1,1,this,'L');
		add(txtBATDT,4,4,1,1,this,'L');
		tblBTMST=((cl_JTBL)crtTBLPNL(this,names,25,5,1,10,3.9,wid,new int[]{0}));
		add(new JLabel("Recycle Quantity : "),15,1,1,1,this,'L');
		add(txtRCLQT,15,2,1,1,this,'L');
		tblBTMST.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblBTMST.addKeyListener(this);
		for(int i=0;i<tblBTMST.cmpEDITR.length;i++)
			tblBTMST.cmpEDITR[i].addKeyListener(this);
		tblBTMST.clrTABLE();
		tblBTMST.addFocusListener(this);
	}
	/**
	 * <b>TASKS :</b>
	 * <br>&nbsp&nbsp&nbsp&nbsptxtRUNNO : HELP Runnumbers and grades date wise
	 * <br>&nbsp&nbsp&nbsp&nbsptxtBATNO : HELP Batch numbers in selected run
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int key=L_KE.getKeyCode();
		if(M_objSOURC==txtRUNNO&&key==L_KE.VK_F1)
		{
			M_strSQLQRY="SELECT RN_RUNNO,RN_GRDDS,RN_TRLCT,RN_GRDCD,RN_RUNST FROM pr_RNMST WHERE SUBSTRING(ltrim(str(RN_RUNNO,20,0)),1,2)='"+cmbLINNO.getSelectedItem().toString().substring(7,9)+"' ORDER BY RN_RUNST";
			M_strHLPFLD="txtRUNNO";
			cl_hlp(M_strSQLQRY,1,1,new String[] {"Run No.","Grade Name"},5,"CT");
		}
		else if(M_objSOURC==txtBATNO&&key==L_KE.VK_F1)
		{
			M_strSQLQRY="SELECT BT_BATNO,BT_BATDT,BT_BATSZ,BT_TRLFL FROM pr_BTMST WHERE BT_RUNNO='"+txtRUNNO.getText()+"' AND BT_GRDCD='"+strGRDCD+"' ORDER BY BT_BATDT";
			M_strHLPFLD="txtBATNO";
			cl_hlp(M_strSQLQRY,1,1,new String[] {"Batch No.","Batch Date","Batch Size"},4,"CT");
		}
	}
	/**
	 * <b>TASKS :</b>
	 * <br>&nbsp&nbsp&nbsp&nbsptxtBATNO : getDATA()
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try{
		if(M_objSOURC==txtBATNO)
		{
			getDATA();
		}
		}catch(Exception e)
		{System.out.println("Error in child ACP :"+e);}
	}
	public void exeHLPOK()
	{
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		if(M_strHLPFLD.equals("txtRUNNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtRUNNO.setText(L_STRTKN.nextToken());
			txtGRDDS.setText(L_STRTKN.nextToken());
			intTRLCT=Integer.parseInt( L_STRTKN.nextToken());
			strGRDCD=L_STRTKN.nextToken();
			txtBATNO.requestFocus();
		}
		else if(M_strHLPFLD.equals("txtBATNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtBATNO.setText(L_STRTKN.nextToken());
			txtBATDT.setText(L_STRTKN.nextToken());
			txtBATSZ.setText(L_STRTKN.nextToken());
	//		txtRCLQT.setText(L_STRTKN.nextToken());
			strTRLFL=L_STRTKN.nextToken();
			if(strTRLFL.equals("P"))
				cmbPRDTP.setSelectedIndex(1);
			else if(strTRLFL.equals("T"))
				cmbPRDTP.setSelectedIndex(0);
			else
				cmbPRDTP.setSelectedIndex(2);
			getDATA();
			cmbPRDTP.requestFocus();
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	boolean vldDATA()
	{
		return true;
	}
	void exeSAVE()
	{
		int i=0;
		String L_strSTRTMP="";	
//GETTTING VALUE FOR BT_TRLFL		
		if(cmbPRDTP.getSelectedIndex()==0)
			L_strSTRTMP="T";
		else if(cmbPRDTP.getSelectedIndex()==1)
			L_strSTRTMP="P";
		else if(cmbPRDTP.getSelectedIndex()==2)
			L_strSTRTMP="W";
//UPDATING BTMST		
		M_strSQLQRY="UPDATE pr_BTMST SET "
			+"BT_TRLFL='"+L_strSTRTMP+"'"
			+" WHERE BT_RUNNO='"+txtRUNNO.getText()+"' AND BT_GRDCD='"+strGRDCD+"' AND BT_BATNO="+txtBATNO.getText();
		cl_dat.M_flgLCUPD_pbst=true;
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
//FOR BATCHES TRANSFERED FROM TRIAL TO PRODCTION.		
		if(strTRLFL.equals("T")&&L_strSTRTMP.equals("P"))
		{
			M_strSQLQRY="UPDATE pr_RNMST SET RN_WSTQT=RN_WSTQT-"+txtBATSZ.getText()
				+",RN_TOTPM=RN_TOTPM+"+txtBATSZ.getText()
				+" WHERE RN_RUNNO='"+txtRUNNO.getText()+"' AND RN_GRDCD='"+strGRDCD+"'";
		}
		else if((!strTRLFL.equals(L_strSTRTMP))&&L_strSTRTMP.equals("W"))
		{
			if(strTRLFL.equals("P"))
			{
//FOR BATCHES TRANSFERED TO WIP FROM PRIME PRODUCTION		
				M_strSQLQRY="UPDATE pr_RNMST SET RN_TOTPR=RN_TOTPR-"+txtBATSZ.getText()
					+",RN_RCLQT=RN_RCLQT-"+txtRCLQT.getText()
					+",RN_TOTPM=RN_TOTPM-"+txtBATSZ.getText()
					+" WHERE RN_RUNNO='"+txtRUNNO.getText()+"' AND RN_GRDCD='"+strGRDCD+"'";
			}
			else if(strTRLFL.equals("T"))
			{
//FOR BATCHES TRANSFERED TO WIP FROM TRIAL PRODUCTION		
				M_strSQLQRY="UPDATE pr_RNMST SET RN_TOTPR=RN_TOTPR-"+txtBATSZ.getText()
					+",RN_RCLQT=RN_RCLQT-"+txtRCLQT.getText()
					+",RN_WSTQT=RN_WSTQT-"+txtBATSZ.getText()
					+" WHERE RN_RUNNO='"+txtRUNNO.getText()+"' AND RN_GRDCD='"+strGRDCD+"'";
			}
		}
		else if((!strTRLFL.equals(L_strSTRTMP))&&strTRLFL.equals("W"))
		{
			if(L_strSTRTMP.equals("P"))
			{
//FOR BATCHES TRANSFERED TO WIP FROM PRIME PRODUCTION		
				M_strSQLQRY="UPDATE pr_RNMST SET RN_TOTPR=RN_TOTPR+"+txtBATSZ.getText()
					+",RN_RCLQT=RN_RCLQT+"+txtRCLQT.getText()
					+",RN_TOTPM=RN_TOTPM+"+txtBATSZ.getText()
					+" WHERE RN_RUNNO='"+txtRUNNO.getText()+"' AND RN_GRDCD='"+strGRDCD+"'";
			}
			else if(L_strSTRTMP.equals("T"))
			{
//FOR BATCHES TRANSFERED TO WIP FROM TRIAL PRODUCTION		
				M_strSQLQRY="UPDATE pr_RNMST SET RN_TOTPR=RN_TOTPR+"+txtBATSZ.getText()
					+",RN_RCLQT=RN_RCLQT+"+txtRCLQT.getText()
					+",RN_WSTQT=RN_WSTQT+"+txtBATSZ.getText()
					+" WHERE RN_RUNNO='"+txtRUNNO.getText()+"' AND RN_GRDCD='"+strGRDCD+"'";
			}
		}
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
		if(cl_dat.exeDBCMT("exeSAVE"))
		{
			strTRLFL=L_strSTRTMP;
		}
	}
	
	void clrCOMP()
	{
		super.clrCOMP();
		tblBTMST.clrTABLE();
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		tblBTMST.setEnabled(false);txtBATSZ.setEnabled(false);txtGRDDS.setEnabled(false);txtRCLQT.setEnabled(false);
	}
	
	void getDATA()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Modification"))
			{
				txtRUNNO.setEnabled(false);
			}
			M_strSQLQRY="SELECT BTT_MTLCD,BTT_MTLMF,BTT_MTLBT,BTT_MTLQT,CT_MATDS FROM pr_BTTRN,CO_CTMST WHERE BTT_MTLCD=CT_MATCD AND BTT_BATNO="+txtBATNO.getText()+" AND BTT_RUNNO='"+txtRUNNO.getText()+"' AND BTT_GRDCD='"+strGRDCD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET==null)
			{
				setMSG("Invalid Batch No. ..",'E');
				tblBTMST.clrTABLE();
				txtBATSZ.setText("");txtRCLQT.setText("");txtBATDT.setText("");
			}
			else
			{
				int i=0;
				while(M_rstRSSET.next())
				{
					tblBTMST.setValueAt(M_rstRSSET.getString("BTT_MTLCD"),i,1);
					tblBTMST.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,2);
					tblBTMST.setValueAt(M_rstRSSET.getString("BTT_MTLMF"),i,3);
					tblBTMST.setValueAt(M_rstRSSET.getString("BTT_MTLBT"),i,4);
					tblBTMST.setValueAt(M_rstRSSET.getString("BTT_MTLQT"),i,5);
					tblBTMST.setValueAt("Kg",i,6);
					i++;
				}
			}			
		}catch(Exception E)
		{System.out.println(E);}
	}
}