
/**
System Name   : System Administration
Program Name  : User Status Updation
Program Desc. : Form for Enabling user and setting default password
Author        : AAP
Date          : 03/09/2003
Version       :  2.0.0

Modificaitons 

Modified By    : AAP
Modified Date  : 27/04/2004
Modified det.  : For better functioning and bugs removal
Version        : 2.0.1
*/

import javax.swing.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
/** <BODY><P>Program Description :</P>			<P><FONT color=purple><STRONG>Program Details :</STRONG></FONT> 			<TABLE border=1 borderColorDark=darkslateblue borderColorLight=white cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%">			  <TR><TD>System Name</TD><TD>System Administration</TD></TR><TR><TD>Program Name</TD><TD>User Status</TD></TR><TR><TD>Program Desc</TD><TD>      <P>To&nbsp;enable user, </P>      <P>To set password to default,</P>      <P>To check disabling details </P>      <P>To delete the user</P>            </TD></TR><TR><TD>Basis Document</TD><TD>&nbsp;</TD></TR><TR><TD>Executable path</TD><TD>f:\exec\splerp2\sa_usrst.class</TD></TR><TR><TD>Source path</TD><TD>f:\source\splerp2\sa_usrst.java</TD></TR><TR><TD>Author </TD><TD>AAP </TD></TR><TR><TD>Date</TD><TD>&nbsp; </TD></TR><TR><TD>Version </TD><TD>2.0.0</TD></TR><TR><TD><STRONG>Modification&nbsp;: 1 </STRONG></TD><TD></TD></TR><TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD>AAP</TD></TR><TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Date</P></TD><TD><P align=left>27/04/2004</P></TD></TR><TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD>2.0.1</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE background="" border=1 borderColorDark=white borderColorLight=darkslateblue cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width="100%">			  <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD><TD><P align=center>Mod</P></TD><TD><P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR><TR><TD>SA_USMST</TD><TD>US_USRCD</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P></BODY> */
class sa_usrst extends cl_pbase
{
	/** User Code	 */
	private TxtNonNumLimit	txtUSRCD;/** Enable User Option*/
	private JCheckBox	chbENUSR;/** Set default passwordOption*/
	private JCheckBox	chbDFPWD;/** User Name*/
	private TxtLimit	txtUSRNM;/** User Email ID */
	private TxtLimit	txtEMAIL;/** User Status flag */
	private TxtLimit	txtSTSFL;/** Disabling login name */
	private TxtLimit	txtDSBUS;/** Disabling IP address */
	private TxtLimit	txtDSBIP;/** Disabling PC name */
	private TxtLimit	txtDSBPC;/** Disabling time */
	private TxtLimit	txtDSBTM;/** Disalbing Date */
	private TxtLimit	txtDSBDT;
	/**To construct the form and lay the components	 */
	public sa_usrst()
	{
		super(2);
		txtUSRCD=new TxtNonNumLimit(3);
		txtUSRNM=new TxtLimit(25);txtEMAIL=new TxtLimit(30);txtSTSFL=new TxtLimit(10);
		txtDSBUS=new TxtLimit(20);txtDSBPC=new TxtLimit(20);txtDSBIP=new TxtLimit(20);
		txtDSBDT=new TxtLimit(20);txtDSBTM=new TxtLimit(20);
		chbENUSR=new JCheckBox("Enable User");chbDFPWD=new JCheckBox("Set Default Password");
		
		setMatrix(20,6);
		add(new JLabel("User Code : "),1,1,1,1,this,'L');
		add(txtUSRCD,1,2,1,1,this,'L');
		add(new JLabel("User Name : "),1,3,1,1,this,'L');
		add(txtUSRNM,1,4,1,1,this,'L');
		add(new JLabel("E-mail ID : "),1,5,1,1,this,'L');
		add(txtEMAIL,1,6,1,1,this,'L');
		
		JPanel pnlTEMP=new JPanel(null);
		add(new JLabel(" User Status : "),1,1,1,1,pnlTEMP,'L');
		add(txtSTSFL,1,2,1,1,pnlTEMP,'L');
		add(new JLabel("Disable Date : "),1,3,1,1,pnlTEMP,'L');
		add(txtDSBDT,1,4,1,1,pnlTEMP,'L');
		add(new JLabel("Disable Time : "),1,5,1,1,pnlTEMP,'L');
		add(txtDSBTM,1,6,1,1,pnlTEMP,'L');
		add(new JLabel(" Disabled from : "),2,1,1,1,pnlTEMP,'L');
		add(txtDSBPC,2,2,1,1,pnlTEMP,'L');
		add(new JLabel("IP Address : "),2,3,1,1,pnlTEMP,'L');
		add(txtDSBIP,2,4,1,1,pnlTEMP,'L');
		add(new JLabel("User : "),2,5,1,1,pnlTEMP,'L');
		add(txtDSBUS,2,6,1,1,pnlTEMP,'L');
		pnlTEMP.setBorder(BorderFactory.createTitledBorder("  Disable Data  "));
		add(pnlTEMP,2,1,4,6,this,'L');
		
		add(chbDFPWD,7,1,1,2,this,'L');
		add(chbENUSR,8,1,1,1,this,'L');
		chbENUSR.setVisible(false);chbENUSR.setSelected(false);
		
		txtUSRCD.setInputVerifier(new INPVF());
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
	}
	/**<b>TASKS : </b>
	 * <br>&nbsp&nbsp&nbsp&nbsp cmbOPTN : Focus transfer
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				txtUSRCD.requestFocus();
				txtUSRCD.setEnabled(true);
			}
		}
	}
	/**<b>TASKS : </b>
	 * <br>&nbsp&nbsp&nbsp&nbsp txtUSRCD : HELP - User codes and other details
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(M_objSOURC==txtUSRCD&&L_KE.getKeyCode()==L_KE.VK_F1)
		{
			M_strSQLQRY="SELECT US_USRCD,US_USRNM,US_EMLRF,US_STSFL,US_DSBID FROM SA_USMST ORDER BY US_USRCD";
			M_strHLPFLD="txtUSRCD";
			cl_hlp(M_strSQLQRY,1,1,new String[] {"User Code","User Code","1","2","3"},5,"CT");
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			((JComponent)M_objSOURC).transferFocus();
	}
	/**
	 * To save data
	 * 
	 * To save data
	 * 
	 * <p>Changes password to default (i.e. to user code) if chbDFPWD is checked<br>Enables user if chbENUSR is checked<br>In delete option, deletes the user
	 */
	void exeSAVE()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(chbENUSR.isSelected())
				{
					cl_dat.M_flgLCUPD_pbst = true;
					M_strSQLQRY="UPDATE SA_USMST SET US_STSFL='',US_DSBID='' WHERE US_USRCD= '"+txtUSRCD.getText().toUpperCase()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY, "setLCLUPD");
					if(cl_dat.exeDBCMT( "setLCLUPD"))
						setMSG("User Enabled ..",'N');
					else
						setMSG("Error in Enabling User ..",'E');
				}
				if(chbDFPWD.isSelected())
				{
					cl_dat.M_flgLCUPD_pbst = true;
					M_strSQLQRY="UPDATE SA_USMST SET US_USRPW='"+cl_ProcessPassword.encode(txtUSRCD.getText().toUpperCase())+"' WHERE US_USRCD= '"+txtUSRCD.getText().toUpperCase()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY, "setLCLUPD");
					if(cl_dat.exeDBCMT( "setLCLUPD"))
						setMSG("Default password set ..",'N');
					else
						setMSG("Error in Setting Default Password ..",'E');
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				int i=JOptionPane.showConfirmDialog(this,"Do you want to delete user : "+txtUSRCD.getText().toUpperCase());
				if(i==0)
				{
					cl_dat.M_flgLCUPD_pbst = true;
					M_strSQLQRY="DELETE FROM SA_USMST WHERE US_USRCD= '"+txtUSRCD.getText().toUpperCase()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY, "setLCLUPD");
					M_strSQLQRY="SELECT COUNT(*) FROM SA_USTRN WHERE UST_USRCD= '"+txtUSRCD.getText().toUpperCase()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null)
						if(M_rstRSSET.next())
							if(!M_rstRSSET.getString(1).equals("0"))
							{
								M_strSQLQRY="DELETE FROM SA_USTRN WHERE UST_USRCD= '"+txtUSRCD.getText().toUpperCase()+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY, "setLCLUPD");
							}
					if(cl_dat.exeDBCMT( "setLCLUPD"))
						JOptionPane.showMessageDialog(this,"User Deleted ..");
					else
						setMSG("Error in Deleting User ..",'E');
				}
			}
		}catch(Exception E)
		{setMSG(E,"exeSAVE : ");}
	}
	
	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtUSRCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtUSRCD.setText(L_STRTKN.nextToken());
				txtUSRNM.setText(L_STRTKN.nextToken());
				txtEMAIL.setText(L_STRTKN.nextToken());
				if(L_STRTKN.hasMoreTokens())
				{
					txtSTSFL.setText((L_STRTKN.nextToken().equalsIgnoreCase("D")==true ? "Disabled" : "Enabled"));
					chbENUSR.setVisible(true);
					if(L_STRTKN.hasMoreTokens())
					{
						String L_STRTMP=L_STRTKN.nextToken();
						StringTokenizer L_DSBTKN=new StringTokenizer(L_STRTMP,";");
						
						StringTokenizer L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
						L_IDTKN.nextToken();
						txtDSBUS.setText(L_IDTKN.nextToken());
						L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
						L_IDTKN.nextToken();
						txtDSBIP.setText(L_IDTKN.nextToken());
						L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
						L_IDTKN.nextToken();
						txtDSBPC.setText(L_IDTKN.nextToken());
						L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
						L_IDTKN.nextToken();
						txtDSBDT.setText(L_IDTKN.nextToken());
						L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
						L_IDTKN.nextToken();
						txtDSBTM.setText(L_IDTKN.nextToken()+":"+L_IDTKN.nextToken());
					}
				}
				else
				{
					txtSTSFL.setText("Enabled");
					chbENUSR.setSelected(false);
					chbENUSR.setVisible(false);
				}
				
			}
		}catch (Exception e)
		{setMSG(e,"child HLPOK : ");}
	}
	/**Class for input verification	 */
	private class INPVF extends InputVerifier
	{
		/**if input == txtUSRCD : Get status details and display		 */
		public boolean verify(JComponent input)
		{
			try
			{
				if(input == txtUSRCD)
				{
					if(txtUSRCD.getText().length()==0)
						return true;
					txtUSRCD.setText(txtUSRCD.getText().toUpperCase());
					M_rstRSSET=cl_dat.exeSQLQRY0("SELECT US_USRCD,US_USRNM,US_EMLRF,US_STSFL,US_DSBID FROM SA_USMST where US_USRCD='"+txtUSRCD.getText()+"'");
					if(M_rstRSSET!=null)
					{
						if(M_rstRSSET.next())
						{
							txtUSRNM.setText(M_rstRSSET.getString("US_USRNM"));
							txtEMAIL.setText(M_rstRSSET.getString("US_EMLRF"));
							txtSTSFL.setText((nvlSTRVL(M_rstRSSET.getString("US_STSFL"),"").equalsIgnoreCase("D")==true ? "Disabled" : "Enabled"));
							chbENUSR.setVisible(true);
							if(nvlSTRVL(M_rstRSSET.getString("US_DSBID"),"").length()>0)
							{
								StringTokenizer L_DSBTKN=new StringTokenizer(M_rstRSSET.getString("US_DSBID"),";");
								StringTokenizer L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
								L_IDTKN.nextToken();
								txtDSBUS.setText(L_IDTKN.nextToken());
								L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
								L_IDTKN.nextToken();
								txtDSBIP.setText(L_IDTKN.nextToken());
								L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
								L_IDTKN.nextToken();
								txtDSBPC.setText(L_IDTKN.nextToken());
								L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
								L_IDTKN.nextToken();
								txtDSBDT.setText(L_IDTKN.nextToken());
								L_IDTKN=new StringTokenizer(L_DSBTKN.nextToken(),":");
								L_IDTKN.nextToken();
								txtDSBTM.setText(L_IDTKN.nextToken()+":"+L_IDTKN.nextToken());
							}
							else
							{
								txtSTSFL.setText("Enabled");
								chbENUSR.setSelected(false);
								chbENUSR.setVisible(false);
							}
							return true;
						}
						else
						{
							setMSG("Invalid User Code .. ",'E');
							return false;
						}
					}
					setMSG("Invalid User Code .. ",'E');
					return false;
				}
			}catch (Exception e)
			{
				setMSG(e,"SA_USRST.INPVF");
				setMSG("Invalid data .. ",'E');
				return false;
			}
			return true;
		}
	}
}
