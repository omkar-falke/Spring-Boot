/*
System Name   : Material Management System
Program Name  : mm_temnp
Program Desc. : Monthly Stores Leadure Processing
Author        : A. A. Patil
Date          : 28/07/2004
Version       : MMS 2.0

Last Modified	: 29/07/2004 
Documented On	: 10/08/2004

*/

import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.util.StringTokenizer;
/**<P>Program Description :</P><P align=center><FONT color=blue size=4><STRONG>CHANGES IN THIS CLASS ARE TO BE AUTHORISED BY H.O.D. - SYSTEMS</STRONG></FONT></P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Materials Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>    Monthly    Stores    Ledger Processing</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                                  Form                                                  for&nbsp;Month end processing of Stores Ledger </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>                   Existing system      in Maveric&nbsp;      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mm_mnprc.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mm_mnprc.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>10/08/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD>  Series Used : MMXXPRC </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_STPRC</TD>    <TD>STP_STRTP,STP_MATCD</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">&nbsp;</FONT></P></TD>   <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE><P>&nbsp;</P>*/
class mm_temnp extends cl_pbase
{
	/**	Last date of the month upto which processing is to be carried out */
	private TxtDate txtTODAT;
	
	/** To construct the screen	 */
	mm_temnp()
	{
		super(2);
		try
		{
			add(new JLabel("Process upto"),1,2,1,1,this,'L');
			add(txtTODAT=new TxtDate(),1,3,1,1,this,'L');
		}catch(Exception e)
		{setMSG(e,"Child.Constructor");}
	}
	/**Focus Nevigation	 */
	public void actionPerformed(ActionEvent P_AE)
	{
		super.actionPerformed(P_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				txtTODAT.requestFocus();
			else if(M_objSOURC == txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			
		}catch(Exception e)
		{setMSG(e,"Child.actionPerformed");}
	}
	/**To validate data. <br>Validates processing date from CO_CDTRN. If processing for the month is already carried out, restricts user from processing	 */
	boolean vldDATA()
	{
		try
		{
			if(txtTODAT.getText().length()==0)
			{
				setMSG("Please enter Month End Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			/*if(Integer.parseInt(cl_dat.M_txtCLKDT_pbst.getText().substring(3,5))<Integer.parseInt(txtTODAT.getText().substring(3,5)))
			{
				setMSG("Processing for next month cannot be carried out ..",'E');
				txtTODAT.requestFocus();
				return false;
			}*/
			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MMXXPRC' and CMT_CODCD='MONTH'");
			if(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("CMT_CCSVL").substring(3,5).equals(txtTODAT.getText().substring(3,5)))
				{
					setMSG("Processing for the specified month is already carried out ..",'E');
					txtTODAT.requestFocus();
					M_rstRSSET.close();
					return false;
				}
			}
			else
			{
				setMSG("Invalid Data ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
		}catch(Exception e)
		{
			setMSG(e,"Child.vldDATA");
			setMSG("Invalid Data ..",'E');
			txtTODAT.requestFocus();
			return false;
		}
		return true;
	}
	/** To carry out monthly processing after taking back up of MM_STPRC
	 * 
	 * To carry out monthly processing after taking back up of MM_STPRC
	 * 
	 * <p>Creates backup copy of MM_STPRC as: MM_STP+Month 2 digits+Year 2 digits.<br>
	 * Copies last months closing values as month opening values.<br>
	 * Copies Year Opening values as current month closing values.<br>
	 * Updates last processing data in CO_CDTRN.
	 */
	void exeSAVE()
	{
		try
		{
			if(!vldDATA())
				return;
			cl_dat.M_flgLCUPD_pbst=true;
			StringTokenizer L_stkTEMP=new StringTokenizer(cl_dat.M_txtCLKDT_pbst.getText(),"/");
			L_stkTEMP.nextToken();
		//Generate BACKUP table name as : STP+MM+YY			
			String L_strTBLNM=L_stkTEMP.nextToken()+L_stkTEMP.nextToken().substring(2);
		//Create BACKUP copy of MM_STPRC  as : STP+MM+YY			
			M_strSQLQRY="Create table MM_STP"+L_strTBLNM+" LIKE  MM_STPRC";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
		//Copy data from live table to backup table
			cl_dat.M_flgLCUPD_pbst=true;
			M_strSQLQRY="INSERT INTO MM_STP"+L_strTBLNM+"  SELECT * FROM MM_STPRC";
			if(cl_dat.M_flgLCUPD_pbst)
			{
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				setMSG("Back-up created ..",'N');
				System.out.println("Back-up created ..");
			}
		//Copy Previous month closing figures as opening figures of current month
			cl_dat.M_flgLCUPD_pbst=true;
			M_strSQLQRY="Update MM_STPRC set STP_MORCQ=isnull(STP_MCRCQ,0),STP_MORCV=isnull(STP_MCRCV,0),"
				+"STP_MOISQ=isnull(STP_MCISQ,0),STP_MOISV=isnull(STP_MCISV,0),STP_MOMRQ=isnull(STP_MCMRQ,0),STP_MOMRV=isnull(STP_MCMRV,0),"
				+"STP_MOSAQ=isnull(STP_MCSAQ,0),STP_MOSAV=isnull(STP_MCSAV,0),STP_MOSTQ=isnull(STP_MCSTQ,0),STP_MOSTV=isnull(STP_MCSTV,0),STP_MOSQT=isnull(STP_MCSQT,0),STP_MOSVL=isnull(STP_MCSVL,0) where stp_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"'";
			setMSG("Month Opening Values Copied ..",'N');
			if(cl_dat.M_flgLCUPD_pbst)
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		//Copy Yearly cummulative figures as closing figures of current month
			M_strSQLQRY="Update MM_STPRC set STP_MCRCQ=isnull(STP_CRCQT,0),STP_MCRCV=isnull(STP_CRCVL,0),"
				+"STP_MCISQ=isnull(STP_CISQT,0),STP_MCISV=isnull(STP_CISVL,0),"
				+"STP_MCMRQ=isnull(STP_CMRQT,0),STP_MCMRV=isnull(STP_CMRVL,0),"
				+"STP_MCSAQ=isnull(STP_CSAQT,0),STP_MCSAV=isnull(STP_CSAVL,0),"
				+"STP_MCSTQ=isnull(STP_CSTQT,0),STP_MCSTV=isnull(STP_CSTVL,0),"
				+"STP_MCSQT=isnull(STP_YCSQT,0),STP_MCSVL=isnull(STP_YCSVL,0)  where stp_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"'";
			setMSG("Month Closing Values Copied ..",'N');
			if(cl_dat.M_flgLCUPD_pbst)
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			M_strSQLQRY = "Update CO_CDTRN SET CMT_CCSVL ='"+txtTODAT.getText().trim()+"'";
			M_strSQLQRY += " WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP ='MMXXPRC' AND CMT_CODCD ='MONTH'";
			if(cl_dat.M_flgLCUPD_pbst)
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst)
			if(cl_dat.exeDBCMT("exeSAVE"))
				setMSG("Processing Completed ..",'N');
			else
				setMSG("Error occured during processing ..",'E');
		}catch(Exception e)
		{setMSG(e,"Child.exeSAVE");}
	}
}