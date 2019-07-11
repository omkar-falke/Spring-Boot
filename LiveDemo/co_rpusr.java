/*
System Name   : 
Program Name  : List of User Rights
Program Desc. : 
Author        : A.T.Chaudhari
Date          : 21/09/2004
Version       : MMS 2.0
*/

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.Hashtable;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>   </TD></TR> <TR><TD>Program Name</TD><TD> User Rights</TD></TR> <TR><TD>Program Desc</TD><TD>Report                                              of User Rights to       Program in User Wise and Program Wise </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\co_rpusr.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\co_rpusr.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>22/09/2004 </TD></TR> <TR><TD>Version </TD><TD> </TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD>							<TD><P align=center>Mod</P>								  </TD>	<TD><P align=center>Del</P></TD>			<TD><P align=center>Enq</P></TD></TR> <TR><TD>SA_PPRTR</TD><TD>PPR_SYSCD,PPR_PRGCD,PPR_SBSCD,PPR_USRTP					</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>SA_PPMST</TD><TD>PP_PRGCD			</TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>SA_USMST</TD><TD>US_USRCD	</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>SA_USTRN</TD><TD>UST_USRCD,UST_SYSCD,UST_USRTP								</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>SA_SPMST</TD><TD>												SP_SYSCD												</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class co_rpusr extends cl_rbase
{										/**Text Field for System Code*/
	private JTextField txtSYSCD;		/**Text Field for system name*/
	private JTextField txtSYSNM;		/**Radio Button for user Wise*/
	private JRadioButton rdbUSRCD;		/**Radio Button for Program wise*/
	private JRadioButton rdbPRGCD;		/**Button Group for user wise and program wise*/
	private ButtonGroup btgORDER;		/**Hash Table for user type and description*/
	private Hashtable<String,String> hstUSRCD;			/**Constant string variable for file name*/
	private final String strFILNM = "C:\\Reports\\co_rpusr.doc";	/**file out put stream object*/
	private FileOutputStream fosREPORT;		/**data out put stream object */
	private DataOutputStream dosREPORT;
	/**Constructor for report */
	public co_rpusr()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			rdbUSRCD = new JRadioButton("User Wise",true);
			rdbPRGCD = new JRadioButton("Module Wise",false);
			btgORDER = new ButtonGroup();
			btgORDER.add(rdbUSRCD);
			btgORDER.add(rdbPRGCD);
			
			
			hstUSRCD = new Hashtable<String,String>(10,0.8f); 
			//Method to put user code as key and user name as value in hstUSRCD hash tabel
			M_strSQLQRY = "SELECT US_USRCD,US_USRNM FROM SA_USMST";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstUSRCD.put(nvlSTRVL(M_rstRSSET.getString("US_USRCD"),""),nvlSTRVL(M_rstRSSET.getString("US_USRNM"),""));
				}
				M_rstRSSET.close();
			}
			
			/*
			hstPRGCD = new Hashtable(10,0.8f);
			//method ot put program code and program description in hstPRGCD hash table
			M_strSQLQRY = "SELECT DISTINCT PP_PRGCD,PP_PRGDS FROM SA_PPMST ORDER BY PP_PRGCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstPRGCD.put(nvlSTRVL(M_rstRSSET.getString("PP_PRGCD"),""),nvlSTRVL(M_rstRSSET.getString("PP_PRGDS"),""));
				}
				M_rstRSSET.close();
			}
			*/
			setMatrix(20,8);
			add(new JLabel("System Code"),2,3,1,1,this,'L');
			add(txtSYSCD = new TxtLimit(2),2,4,1,0.35,this,'L');
			add(txtSYSNM = new JTextField(),2,6,1,2.78,this,'R');
			add(rdbUSRCD,3,4,1,1,this,'L');
			add(rdbPRGCD,3,5,1,1.5,this,'L');
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**Method on action performed*/
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				setENBL(true);
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);
				txtSYSCD.requestFocus();
			}
		}
		if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
				txtSYSCD.requestFocus();
			else
				M_cmbDESTN.requestFocus();
		}
	}
	/**Event on key pressed */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtSYSCD)
			{
				M_strHLPFLD = "txtSYSCD";
				M_strSQLQRY = "SELECT SP_SYSCD,SP_SYSNM FROM SA_SPMST";
				//Mpdified date 05/08/2006
				if(txtSYSCD.getText().trim().length()>0)
					M_strSQLQRY += " Where SP_SYSCD like '"+txtSYSCD.getText().trim().toUpperCase()+"%'";
				M_strSQLQRY += " ORDER BY SP_SYSCD ";
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,new String[]{"System Code","System Description"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == rdbPRGCD)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	/**event on pressing ok or enter button on help window*/
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtSYSCD")
		{
			txtSYSCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtSYSNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			rdbUSRCD.requestFocus();
		}
	}
	/**Method to set enabled component */
	public void setENBL(boolean L_ACTION)
	{
		super.setENBL(L_ACTION);
		txtSYSNM.setEnabled(false);
	}
	/**method to print or display report on screen*/
	public void exePRINT()
	{
		try
		{
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			
			M_strSQLQRY = "SELECT DISTINCT PPR_SYSCD,PPR_PRGCD,PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,"
				+"UST_USRCD,PP_PRGDS FROM SA_PPRTR,SA_USTRN,SA_PPMST WHERE UST_SYSCD = PPR_SYSCD "
				+"AND UST_USRTP = PPR_USRTP and UST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PP_SYSCD = PPR_SYSCD AND PP_PRGCD = PPR_PRGCD ";
			if(txtSYSCD.getText().trim().length() > 0)
				M_strSQLQRY += " AND PPR_SYSCD = '"+txtSYSCD.getText().trim()+"' ";
			if(rdbUSRCD.isSelected())
				M_strSQLQRY += " ORDER BY UST_USRCD,PPR_SYSCD,PPR_PRGCD ";
			if(rdbPRGCD.isSelected())
				M_strSQLQRY += " ORDER BY PPR_SYSCD,PPR_PRGCD,UST_USRCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				String L_strPRGCD = "";
				String L_strUSRCD = "";
				M_intPAGNO = 1;
				M_intLINNO = 1;
				
				while(M_rstRSSET.next())
				{
					if(M_intPAGNO == 1 || M_intLINNO > 55)
					{
						if(M_intPAGNO > 1)
						{
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						dosREPORT.writeBytes("\n");		M_intLINNO += 1;
						dosREPORT.writeBytes("SUPREME PERTOCHEM LIMITED");
						dosREPORT.writeBytes("\n");		M_intLINNO += 1;
						if(rdbPRGCD.isSelected())
							dosREPORT.writeBytes("Module wise List of User Rights ");
						if(rdbUSRCD.isSelected())
							dosREPORT.writeBytes("User wise List of Module Rights ");
						if(txtSYSCD.getText().trim().length() > 0)
							dosREPORT.writeBytes(padSTRING('R',"for "+txtSYSNM.getText().trim(),44));
						else
							dosREPORT.writeBytes(padSTRING('R',"",44));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");		M_intLINNO += 1;
						dosREPORT.writeBytes(padSTRING('R'," ",76));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");		M_intLINNO += 1;
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");		M_intLINNO += 1;
						if(rdbUSRCD.isSelected())
							dosREPORT.writeBytes("User                               Program Name                       Add.    Mod.    Del.   Enq.");
						if(rdbPRGCD.isSelected())
							dosREPORT.writeBytes("Program Name                       User                               Add.    Mod.    Del.   Enq.");
						dosREPORT.writeBytes("\n");		M_intLINNO += 1;
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
						M_intLINNO = 1;
						M_intPAGNO++;
						L_strPRGCD = "";
						L_strUSRCD = "";
					}
					dosREPORT.writeBytes("\n");		M_intLINNO += 1;
					if(rdbUSRCD.isSelected())
					{
						if(!L_strUSRCD.equals(nvlSTRVL(M_rstRSSET.getString("UST_USRCD"),"")))
						{
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							//Date  05/08/2006 
							if(hstUSRCD.containsKey(M_rstRSSET.getString("UST_USRCD")))
								dosREPORT.writeBytes(padSTRING('R',hstUSRCD.get(nvlSTRVL(M_rstRSSET.getString("UST_USRCD"),"")).toString(),35));
							else
								dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("UST_USRCD")+"*",35));
							L_strUSRCD = nvlSTRVL(M_rstRSSET.getString("UST_USRCD"),"");
						}
						else
							dosREPORT.writeBytes(padSTRING('R',"",35));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PP_PRGDS"),""),36));
					}
					if(rdbPRGCD.isSelected())
					{
						if(!L_strPRGCD.equals(nvlSTRVL(M_rstRSSET.getString("PPR_PRGCD"),"")))
						{
							dosREPORT.writeBytes("\n");
							M_intLINNO += 1;
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PP_PRGDS"),""),35));
							L_strPRGCD = nvlSTRVL(M_rstRSSET.getString("PPR_PRGCD"),"");
						}
						else
							dosREPORT.writeBytes(padSTRING('R',"",35));
						//Date  05/08/2006 
						if(hstUSRCD.containsKey(M_rstRSSET.getString("UST_USRCD")))
							dosREPORT.writeBytes(padSTRING('R',hstUSRCD.get(nvlSTRVL(M_rstRSSET.getString("UST_USRCD"),"")).toString(),36));
						else 
							dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("UST_USRCD")+"*",36));
					}
					if(M_rstRSSET.getString("PPR_ADDFL").equals("Y"))
						dosREPORT.writeBytes(padSTRING('R',"Y",6));
					else
						dosREPORT.writeBytes(padSTRING('R',"-",6));
					if(M_rstRSSET.getString("PPR_MODFL").equals("Y"))
						dosREPORT.writeBytes(padSTRING('R',"Y",8));
					else
						dosREPORT.writeBytes(padSTRING('R',"-",8));
					
					if(M_rstRSSET.getString("PPR_DELFL").equals("Y"))
						dosREPORT.writeBytes(padSTRING('R',"Y",8));
					else
						dosREPORT.writeBytes(padSTRING('R',"-",8));
					
					if(M_rstRSSET.getString("PPR_ENQFL").equals("Y"))
						dosREPORT.writeBytes(padSTRING('R',"Y",7));
					else
						dosREPORT.writeBytes(padSTRING('R',"-",7));
				}
				dosREPORT.writeBytes("\n");		M_intLINNO += 1;
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
				prnFMTCHR(dosREPORT,M_strEJT);
				M_rstRSSET.close();
				M_intPAGNO = 1;
				M_intLINNO = 1;
				dosREPORT.close();
				fosREPORT.close();
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				doPRINT(strFILNM);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
}