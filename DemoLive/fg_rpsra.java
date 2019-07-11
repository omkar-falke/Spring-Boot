import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.sql.ResultSet;
import java.io.*; 
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>   Customer Order Printing</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                        Form for Customer       Order&nbsp;Printing (To be extended for HTML Format) </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Marketing System Enhancement Proposal by      Mr. SRD      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mr_teind.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mr_teind.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>15/04/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>&nbsp;MR_INMST</TD>    <TD>IN_INDNO,IN_MKTTP&nbsp; </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MR_INTRN</TD>    <TD>INT_MKTTP,INT_INDNO,INT_PRDCD</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>&nbsp;</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>&nbsp;MR_DOTRN</TD>    <TD>DOT_INDNO,DOT_DORNO&nbsp;</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P><P>&nbsp;</P><P><FONT color=purple><STRONG>Features :</STRONG></FONT></P><UL>  <LI><FONT color=black>Report available in text as well as HTML   format.</FONT>  <LI>Available at present only for a perticular indent no.</LI></UL>*/
class fg_rpsra extends cl_rbase 
{
	public JTextField  txtRESNO;
	private final String strFILNM = "C:\\REPORTS\\fg_rpsra.html";
	FileOutputStream fosREPORT;
    DataOutputStream dosREPORT;
	private Process prcREPORT;
	private String strREQBY;
	private String strAUTBY;
	private String strCONBY;
	/**Constructor for printing from indent entry screen */
	public fg_rpsra(int P_intDESTN)
	{
		super();
		try
		{
			add(new JLabel("Res. No."),2,2,1,1,this,'L');
			add(txtRESNO=new JTextField(),2,2,1,1,this,'L');
		}
		catch(Exception e)
		{
			setMSG(e,"Child.constructor 1");
		}
	}
	/**Constructor for printing from report menu */
	fg_rpsra()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Res. No."),2,4,1,1,this,'L');
			add(txtRESNO=new JTextField(),2,5,1,1,this,'L');
		}
		catch(Exception e)
		{
			setMSG(e,"Child.constructor 2");
		}
	}
	/**<b> TASKS</b><br>
	 * Source = cmbOPTN : Transfer focus to txtRESNO */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			txtRESNO.requestFocus();
	}
	/**<b> TASKS</b><br>
	 * Source = txtRESNO : HELP of available indent no
	 * Source = cmbOPTN : Transfer focus to txtRESNO */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(M_objSOURC==txtRESNO && L_KE.getKeyCode() == L_KE.VK_F1)
		{
			M_strHLPFLD = "txtRESNO";
			M_strSQLQRY = "SELECT RST_RESNO,RST_RESDS FROM MR_RSTRN ";
			cl_hlp(M_strSQLQRY,1,1,new String[] {"Reservation No.","Remark"},2 ,"CT");
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtRESNO)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	public void exeHLPOK()
	{
		cl_dat.M_flgHELPFL_pbst = false;
		super.exeHLPOK();
		StringTokenizer L_STRTKN = new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD.equals("txtRESNO"))
			txtRESNO.setText(L_STRTKN.nextToken());
		cl_dat.M_btnSAVE_pbst.requestFocus();
	}	
	void exePRINT()
	{
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		try
		{
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			M_intPAGNO = 1;
			M_intLINNO = 0;
			double L_dblRESQT = 0.0;
			//dosREPORT.writeBytes("<HTML><HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD>");
			//select PR_PRDCD, PR_PRDDS from CO_PRMST where RDER BY PR_PRDCD"
			/*M_strSQLQRY = "SELECT RST_RESNO,RST_RESDT,RST_REXDT,RST_RESDS,RST_PRDCD,RST_LOTNO,"
				+"RST_REQBY,RST_AUTBY,RST_LUSBY,RST_RESQT FROM MR_RSTRN WHERE RST_RESNO = '"
				+txtRESNO.getText().trim()+"' ORDER BY RST_RESDT,RST_RESNO";
			*/
			M_strSQLQRY = "SELECT RST_RESNO,RST_RESDT,RST_REXDT,RST_RESDS,RST_PRDCD,RST_LOTNO,"
				+"RST_REQBY,RST_AUTBY,RST_LUSBY,RST_RESQT,PR_PRDDS FROM MR_RSTRN,CO_PRMST "
				+"WHERE PR_PRDCD = RST_PRDCD AND RST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RST_RESNO = '"
				+txtRESNO.getText().trim()+"' ORDER BY RST_RESDT,RST_RESNO";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_intPAGNO == 1 || M_intLINNO > 55)
					{
						M_intPAGNO = 1;
						dosREPORT.writeBytes("<HTML><HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD>");
						dosREPORT.writeBytes("<BODY bgColor=ghostwhite><P><HR>");
						M_intLINNO++;
						dosREPORT.writeBytes("<TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\">");
						dosREPORT.writeBytes("<TR>");
						dosREPORT.writeBytes("<TD><IMG src=\"file://f:\\exec\\splerp2\\spllogo_old.gif\" style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></TD>");
						dosREPORT.writeBytes("<TD><P align=left><STRONG><FONT face=Arial size=5>SUPREME PETROCHEM LIMITED</FONT></STRONG></P><STRONG><FONT face=Arial size=4><p align=left>Stock Reservation Confirmation</font></TD>");
						M_intLINNO++;
						dosREPORT.writeBytes("<TD><p><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</p><p><FONT face=Arial size=2>Page No. : 1</P><TD>");
						M_intLINNO++;
						dosREPORT.writeBytes("</TR>");
						dosREPORT.writeBytes("</TABLE>");
						
						dosREPORT.writeBytes("<HR>");
						M_intPAGNO += 1;
												
						dosREPORT.writeBytes("<TABLE border = 0 cellSpacing=0 cellPadding=0 width=\"100%\">");
						
						dosREPORT.writeBytes("<TR>");
						dosREPORT.writeBytes("<TD><P><STRONG>Res Number  : </STRONG>"+txtRESNO.getText().trim()+"</TD>");
						M_intLINNO++;
						if(M_rstRSSET.getDate("RST_RESDT") != null)
							dosREPORT.writeBytes("<TD><STRONG>Res Date    : </STRONG>"+M_fmtLCDAT.format(M_rstRSSET.getDate("RST_RESDT"))+"</TD>");
						else
							dosREPORT.writeBytes("<TD><STRONG>Res Date    : </STRONG></TD>");
						if(M_rstRSSET.getDate("RST_REXDT") != null)
							dosREPORT.writeBytes("<TD><STRONG>Expiry Date : </STRONG>"+M_fmtLCDAT.format(M_rstRSSET.getDate("RST_REXDT"))+"</TD>");
						else
							dosREPORT.writeBytes("<TD><STRONG>Expiry Date : </STRONG></TD>");
						dosREPORT.writeBytes("</TR>");
						
						//dosREPORT.writeBytes("</TABLE>");
						//dosREPORT.writeBytes("<TABLE border = 0 cellSpacing=0 cellPadding=0 width=\"100%\">");
						//System.out.println(nvlSTRVL(M_rstRSSET.getString("RST_RESDS"),""));
						dosREPORT.writeBytes("<TR><TD colSpan=3><P>&nbsp;</P><STRONG>Description : </STRONG>"+nvlSTRVL(M_rstRSSET.getString("RST_RESDS"),"")+"</TD>");
						M_intLINNO++;
						dosREPORT.writeBytes("</TR>");
						dosREPORT.writeBytes("<TR>");
						//dosREPORT.writeBytes("<TD colSpan=3 ><P>&nbsp;</P><STRONG>Grade       : </STRONG>"+M_rstRSSET.getString("RST_PRDCD")+"  "+nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"")+"</TD>");
						dosREPORT.writeBytes("<TD colSpan=3 ><P>&nbsp;</P><STRONG>Grade       : </STRONG>"+nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"")+"</TD>");
						M_intLINNO++;
						dosREPORT.writeBytes("</TR>");
						
						//dosREPORT.writeBytes("</TABLE>");
						
						dosREPORT.writeBytes("<TR>");
						dosREPORT.writeBytes("<TD><P align=right></P> <P align=right><STRONG>Lot Number</STRONG></P> </TD>");
						M_intLINNO += 2;
						dosREPORT.writeBytes("<TD><P align=right></P> <P align=right><STRONG>Quantity</STRONG></P> </TD>");
						M_intLINNO += 2;
						dosREPORT.writeBytes("<TD><P>&nbsp;</P></TD>");
						M_intLINNO += 2;
						dosREPORT.writeBytes("</TR>");
						
					}
					//RST_RESQT
					dosREPORT.writeBytes("<TR>");
					dosREPORT.writeBytes("<TD align=right >"+M_rstRSSET.getString("RST_LOTNO")+"</TD>");
					dosREPORT.writeBytes("<TD align=right >"+M_rstRSSET.getString("RST_RESQT")+"</TD>");
					dosREPORT.writeBytes("<TD></TD>");
					dosREPORT.writeBytes("</TR>");
					L_dblRESQT += M_rstRSSET.getDouble("RST_RESQT");
					
					strREQBY = nvlSTRVL(M_rstRSSET.getString("RST_REQBY"),"");
					strAUTBY = nvlSTRVL(M_rstRSSET.getString("RST_AUTBY"),"");
					strCONBY = nvlSTRVL(M_rstRSSET.getString("RST_LUSBY"),"");
				}
				dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD align=right ><P>&nbsp;</P><STRONG>Total Reservation Quantity</STRONG> </TD>");
				dosREPORT.writeBytes("<TD align=right ><P>&nbsp;</P><STRONG>"+setNumberFormat(L_dblRESQT,3)+"</STRONG></TD>");
				dosREPORT.writeBytes("<TD></TD>");
				dosREPORT.writeBytes("</TR>");
				dosREPORT.writeBytes("</TABLE>");
				
				dosREPORT.writeBytes("<HR>");
				dosREPORT.writeBytes("<TABLE border = 0 cellSpacing=0 cellPadding=0 width=\"100%\">");
				dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD><STRONG>Requested By</STRONG></TD>");
				dosREPORT.writeBytes("<TD><STRONG>Approved By</STRONG></TD>");
				dosREPORT.writeBytes("<TD><STRONG>Confirmed By</STRONG></TD>");
				dosREPORT.writeBytes("</TR>");
				dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD><P>&nbsp;</P><STRONG>"+strREQBY+"</STRONG></TD>");
				dosREPORT.writeBytes("<TD><P>&nbsp;</P><STRONG>"+strAUTBY+"</STRONG></TD>");
				dosREPORT.writeBytes("<TD><P>&nbsp;</P><STRONG>"+strCONBY+"</STRONG></TD>");
				dosREPORT.writeBytes("</TR>");
				dosREPORT.writeBytes("</TABLE>");
				dosREPORT.writeBytes("<HR>");
				dosREPORT.writeBytes("<STRONG>CC:\\MHD Incharge\\"+strREQBY+"\\"+strAUTBY+"</STRONG>");
			}
			L_dblRESQT = 0.0;
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst))
				prcREPORT  = Runtime.getRuntime().exec("c:\\program files\\internet explorer\\iexplore.exe "+strFILNM);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
}