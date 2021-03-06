/*
System Name   : Material Management System
Program Name  : Analysis of Internal And External Lead Time
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
import java.sql.*;
import java.util.Calendar;	
import java.util.Hashtable;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR>  <TR><TD>Program Name</TD><TD>Internal ,External Lead Time Analysis&nbsp;</TD></TR> <TR><TD>Program Desc</TD><TD>Display deviation in P.O. Date GRIN Date on                                              Indent Authorisation date with       respect to internal and external lead time. </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_rplta.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\mm_rplta.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>21/09/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD>							<TD><P align=center>Mod</P>								  </TD>	<TD><P align=center>Del</P></TD>			<TD><P align=center>Enq</P></TD></TR> <TR><TD>CO_CDTRN</TD><TD>CMT_CGMTP,CMT_CGSTP,CMT_CODCD					</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>CO_CTMST</TD><TD>CT_GRPCD,CT_CODTP,CT_MATCD			</TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_GRMST</TD><TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO	</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_INMST</TD><TD>IN_SBSCD,IN_STRTP,IN_INDNO,IN_MATCD								</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_POMST</TD><TD>												PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD												</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class mm_rplta extends cl_rbase
{									/**Text Field For Department Code*/
	private JTextField txtDPTCD;	/**Text Field for Departmetn Name*/
	private JTextField txtDPTNM;	/**Text Field for form date*/
	private JTextField txtFMDAT;	/**Text Field for to date*/
	private JTextField txtTODAT;	/**Integer for count in time p.o*/
	private int intINTPO;			/**Integer for count deviation in p.o.*/
	private int intDEVPO;			/**integer for count in time GRIN*/
	private int intINTGR;			/**Integer for count deviation in GRIN*/
	private int intDEVGR;			/**Integer for count total number of item in report*/
	private int intTOTAL;			/**String for department name	*/
	private String strDPTCD;		/**String for report name	*/
	private  String strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rplta.doc";	/**file out put stream object*/
	private FileOutputStream fosREPORT;		/**data out put stream object */
	private DataOutputStream dosREPORT;
	private ButtonGroup bgrRPTOP = new ButtonGroup();
	private JRadioButton rdoOPTDP,rdoOPTMG,rdoOPTVE,rdoOPTMC;
	private Hashtable<String,String> hstDPTCD;
	private String M_PORBYDT;
	private String M_PORBYDT1;
	private String M_GRNBYDT;
	private String M_GRNBYDT1;
	private int M_PODEVIA;
	private String M_PORDT;
	/**Constructor of report */
	public mm_rplta()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			hstDPTCD = new Hashtable<String,String>(10,0.8f);
			
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'COXXDPT' AND isnull(CMT_STSFL,' ') <>'X' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}
			setMatrix(20,8);
			//add(rdoOPTDP = new JRadioButton("Department Wise",true),3,3,1,1.5,this,'L'); // Option Indent wise
			//add(rdoOPTMG = new JRadioButton("Main Group Wise",false),3,5,1,2,this,'L');	// Option Dept.wise
			add(rdoOPTDP = new JRadioButton("Departmentwise",true),3,1,1,1.5,this,'L'); // Option Main Groupwise
			add(rdoOPTMG = new JRadioButton("Main Groupwise",true),3,3,1,1.5,this,'L'); // Option Vendor wise
			add(rdoOPTVE = new JRadioButton("Vendorwise",true),3,5,1,1,this,'L'); // Option Material Code wise
			add(rdoOPTMC = new JRadioButton("Material Codewise",true),3,6,1,1.5,this,'L'); // 
			
			bgrRPTOP.add(rdoOPTDP);
			bgrRPTOP.add(rdoOPTMG);
			bgrRPTOP.add(rdoOPTVE);
			bgrRPTOP.add(rdoOPTMC);
		
			add(new JLabel("Dept./Main Grp/Vendor/Mat.Code"),4,3,1,1,this,'L');
			add(txtDPTCD = new TxtLimit(10),4,4,1,1,this,'L');
			add(txtDPTNM = new JTextField(),4,5,1,3,this,'L');
			add(new JLabel("From Date"),5,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),5,4,1,1,this,'L');
			add(new JLabel("To Date"),6,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),6,4,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**Action Event method	 */
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
				txtDPTCD.requestFocus();
			}
		}
		if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
				txtDPTCD.requestFocus();
			else
				M_cmbDESTN.requestFocus();
		}
		if(M_objSOURC == txtDPTCD)
		{
			txtDPTNM.setText(hstDPTCD.get(txtDPTCD.getText().trim()).toString());
		}
	}
	/**Event On Key Pressed */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
		/**Key F1 used for display help window */
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			/**Help for department code */
			if(M_objSOURC == txtDPTCD)
			{
				M_strHLPFLD = "txtDPTCD";
			   if(rdoOPTDP.isSelected())
			   {
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
						+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' AND ";
					if(txtDPTCD.getText().trim().length() > 0)
						M_strSQLQRY += "CMT_CODCD LIKE '"+txtDPTCD.getText().trim()+"%' AND ";
					M_strSQLQRY += "isnull(CMT_STSFL,' ') <>'X' ORDER BY CMT_CODDS";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code","Name"},2,"CT");
				}
			   else if(rdoOPTMG.isSelected())
			   {
				   M_strSQLQRY = "SELECT CT_GRPCD,CT_MATDS FROM CO_CTMST WHERE "
						+"CT_CODTP  = 'MG' AND isnull(CT_STSFL,'') <>'X'";
					if(txtDPTCD.getText().trim().length() > 0)
						M_strSQLQRY += " AND CT_GRPCD LIKE '"+txtDPTCD.getText().trim()+"%'";
					M_strSQLQRY += "ORDER BY CT_MATCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Main Group","Description"},2,"CT");
			   }
			   else if(rdoOPTVE.isSelected())
			   {
				   M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE "
						+"PT_PRTTP  = 'S' AND isnull(PT_STSFL,'') <>'X'";
					if(txtDPTCD.getText().trim().length() > 0)
						M_strSQLQRY += " AND PT_PRTNM LIKE '"+txtDPTCD.getText().trim()+"%'";
					M_strSQLQRY += "ORDER BY PT_PRTNM";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Vendor Code","Name"},2,"CT");
			   }
			   else if(rdoOPTMC.isSelected())
			   {
				   M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST WHERE "
						+"CT_CODTP  = 'CD' AND isnull(CT_STSFL,'') <>'X'";
					if(txtDPTCD.getText().trim().length() > 0)
						M_strSQLQRY += " AND CT_MATCD LIKE '"+txtDPTCD.getText().trim()+"%'";
					M_strSQLQRY += "ORDER BY CT_MATCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Material Code","Description"},2,"CT");
			   }
				   
			}
		}
	}
	/**Event on click on ok or enter button on help window */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtDPTCD")
		{
			txtDPTCD.setText((cl_dat.M_strHLPSTR_pbst).trim());
			txtDPTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtFMDAT.requestFocus();
		}
	}
	/**Event when focus is on a perticular component */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == txtDPTCD)
				setMSG("Eneter Departmetnt Code Or Press 'F1' For Help Or Blank for all .. ",'N');
			if(M_objSOURC == txtFMDAT)
				setMSG("Enter From Date ..",'N');
			if(M_objSOURC == txtTODAT)
			{
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				txtTODAT.select(0,txtTODAT.getText().length());
				setMSG("Enter To Date ..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"focusGained");
		}
	}
	/**Method to set enabled component on frame */
	public void setENBL(boolean ACTION)
	{
		super.setEnabled(ACTION);
		txtDPTNM.setEnabled(false);
	}
	/**method to validate data before processing  */
	public boolean vldDATA()
	{
		try
		{
		    if(M_rdbHTML.isSelected())
		         strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rplta.html";
		    if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rplta.doc";
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Be Grater Than Today..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if(M_cmbDESTN.getItemCount() ==0)
				{
					setMSG("Please select E-mail Id by using the F1 list ..",'E');
					return false;
				}
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/**Method to display of print report  */
	public void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				fosREPORT = new FileOutputStream(strFILNM);
				dosREPORT = new DataOutputStream(fosREPORT);
				if(M_rdbHTML.isSelected())
    			{
    			    dosREPORT.writeBytes("<HTML><HEAD><Title>Lead Time Analysis </title> ");
    				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
    				dosREPORT.writeBytes("</STYLE></HEAD>"); 
    				dosREPORT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
    			}
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&& M_rdbTEXT.isSelected())
					prnFMTCHR(dosREPORT,M_strCPI17);
				
				
				//OLD QUERY								
				
				//M_strSQLQRY = "SELECT IN_INDNO,IN_MATCD,DATE(IN_AUTDT) IN_AUTDT,IN_PORBY,IN_EXPDT,IN_DPTCD,CT_MATDS,"
				//	+"CT_UOMCD,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,(isnull(CT_ILDTM,0)+isnull(CT_ELDTM,0)) AS CT_TOTAL,(isnull(CT_IILTM,0)+isnull(CT_IELTM,0)) AS CT_ITOTA,"
				//	+"PO_PORNO,PO_PORDT,GR_GRNNO,GR_GRNDT,DATE(WB_GINDT)L_GINDT, (DAYS(isnull(PO_PORDT,CURRENT_DATE)) - DAYS(IN_PORBY)) AS PO_DEVIA,"
				//	+"(DAYS(isnull(DATE(WB_GINDT),CURRENT_DATE)) - DAYS(IN_EXPDT)) AS GR_DEVIA FROM CO_CTMST,MM_INMST LEFT OUTER JOIN "
				//	+"MM_POMST ON IN_STRTP = PO_STRTP AND IN_INDNO = PO_INDNO AND IN_MATCD = PO_MATCD AND "
				//	+"isnull(PO_STSFL,'') <> 'X' LEFT OUTER JOIN MM_GRMST ON PO_STRTP = GR_STRTP AND "
				//	+"PO_PORNO = GR_PORNO AND PO_MATCD = GR_MATCD LEFT OUTER JOIN MM_WBTRN ON GR_GINNO = WB_DOCNO AND GR_CMPCD=WB_CMPCD AND WB_DOCTP ='02' WHERE  "
				//	+"IN_MATCD = CT_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') <> 'X' AND isnull(IN_INDTP,'') = '01' and isnull(IN_AUTQT,0)-isnull(IN_FCCQT,0) >0 AND IN_STRTP = '"
				//	+M_strSBSCD.substring(2,4)+"' ";
				
				//NEW QUERY (Modified by SRT on 17/12/2008 as per request By MDP)
				
//				M_strSQLQRY = "SELECT IN_INDNO,IN_MATCD,DATE(IN_AUTDT) IN_AUTDT,IN_PORBY,IN_EXPDT,IN_DPTCD,CT_MATDS,";
//				M_strSQLQRY+= "CT_UOMCD,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,(isnull(CT_ILDTM,0)+isnull(CT_ELDTM,0)) ";
//				M_strSQLQRY+= "AS CT_TOTAL,(isnull(CT_IILTM,0)+isnull(CT_IELTM,0)) AS CT_ITOTA,PO_PORNO,PT_PRTNM,";
//				M_strSQLQRY+= "PO_PORDT,PO_PORQT,PO_FRCQT,GR_RECQT,GR_ACPQT,GR_REJQT,GR_GRNNO,GR_GRNDT,DATE(WB_GINDT)L_GINDT,";
//				M_strSQLQRY+= "(DAYS(isnull(PO_PORDT,CURRENT_DATE)) - DAYS(IN_PORBY)) AS PO_DEVIA,(DAYS(isnull(DATE(WB_GINDT),";
//				M_strSQLQRY+= "CURRENT_DATE)) - DAYS(IN_EXPDT)) AS GR_DEVIA FROM CO_CTMST,MM_INMST LEFT OUTER JOIN ";
//				M_strSQLQRY+= "MM_POMST ON IN_STRTP = PO_STRTP AND IN_INDNO = PO_INDNO AND IN_MATCD = PO_MATCD AND ";
//				M_strSQLQRY+= "isnull(PO_STSFL,'') <> 'X'  LEFT OUTER JOIN CO_PTMST ON PO_VENCD=PT_PRTCD AND ";
//				M_strSQLQRY+= "PT_PRTTP ='S' LEFT OUTER JOIN MM_GRMST ON PO_STRTP = GR_STRTP AND PO_PORNO = GR_PORNO AND ";
//				M_strSQLQRY+= "PO_MATCD = GR_MATCD LEFT OUTER JOIN MM_WBTRN ON GR_GINNO = WB_DOCNO AND ";
//				M_strSQLQRY+= "GR_CMPCD=WB_CMPCD AND WB_DOCTP ='02' WHERE IN_MATCD = CT_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
//				M_strSQLQRY+= "isnull(IN_STSFL,'') <> 'X' AND isnull(IN_INDTP,'') = '01' and isnull(IN_AUTQT,0)-isnull(IN_FCCQT,0) >0 ";
//				M_strSQLQRY+= "AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"'";

				M_strSQLQRY = "SELECT IN_INDNO,IN_MATCD,CONVERT(varchar,IN_AUTDT,103) IN_AUTDT,IN_PORBY,IN_EXPDT,IN_DPTCD,CT_MATDS,";
				M_strSQLQRY+= "CT_UOMCD,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,(isnull(CT_ILDTM,0)+isnull(CT_ELDTM,0)) ";
				M_strSQLQRY+= "AS CT_TOTAL,(isnull(CT_IILTM,0)+isnull(CT_IELTM,0)) AS CT_ITOTA,PO_PORNO,PT_PRTNM,";
				M_strSQLQRY+= "PO_PORDT,PO_PORQT,PO_FRCQT,GR_RECQT,GR_ACPQT,GR_REJQT,GR_GRNNO,GR_GRNDT,CONVERT(varchar,WB_GINDT,103)L_GINDT,";
				M_strSQLQRY+= "(DAY(isnull(PO_PORDT,getdate())) - DAY(IN_PORBY)) AS PO_DEVIA,(DAY(isnull(CONVERT(varchar,WB_GINDT,103),";
				M_strSQLQRY+= "CONVERT(varchar,getdate(),103))) - DAY(IN_EXPDT)) AS GR_DEVIA FROM CO_CTMST,MM_INMST LEFT OUTER JOIN ";
				M_strSQLQRY+= "MM_POMST ON IN_STRTP = PO_STRTP AND IN_INDNO = PO_INDNO AND IN_MATCD = PO_MATCD AND ";
				M_strSQLQRY+= "isnull(PO_STSFL,'') <> 'X'  LEFT OUTER JOIN CO_PTMST ON PO_VENCD=PT_PRTCD AND PT_PRTTP ='S' ";
				M_strSQLQRY+= "LEFT OUTER JOIN MM_GRMST ON PO_STRTP = GR_STRTP AND PO_PORNO = GR_PORNO AND ";
				M_strSQLQRY+= "PO_MATCD = GR_MATCD LEFT OUTER JOIN MM_WBTRN ON GR_GINNO = WB_DOCNO AND ";
				M_strSQLQRY+= "GR_CMPCD=WB_CMPCD AND WB_DOCTP ='02' WHERE IN_MATCD = CT_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
				M_strSQLQRY+= "isnull(IN_STSFL,'') <> 'X' AND isnull(IN_INDTP,'') = '01' and isnull(IN_AUTQT,0)-isnull(IN_FCCQT,0) >0 ";
				M_strSQLQRY+= "AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"'";

				if(txtDPTCD.getText().trim().length() > 0)
				{	
					if(rdoOPTMG.isSelected())
					{
						//M_strSQLQRY +=" AND CT_GRPCD ='"+txtDPTCD.getText().trim()+"'";
						M_strSQLQRY +=" AND SUBSTRING(IN_MATCD,1,2) ='"+txtDPTCD.getText().trim()+"'";
					}
					else if(rdoOPTDP.isSelected())
						M_strSQLQRY += " AND IN_DPTCD = '"+txtDPTCD.getText().trim()+"' ";
					else if(rdoOPTVE.isSelected())
						M_strSQLQRY+= " AND PO_VENCD='"+txtDPTCD.getText().trim()+"' ";
					else if(rdoOPTMC.isSelected())
						M_strSQLQRY += " AND IN_MATCD = '"+txtDPTCD.getText().trim()+"' ";
				}
				M_strSQLQRY +=" AND CONVERT(varchar,IN_AUTDT,101) BETWEEN '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' ";
				if(rdoOPTMG.isSelected())
					M_strSQLQRY += "ORDER BY IN_MATCD";
				else
					M_strSQLQRY += "ORDER BY IN_DPTCD,IN_INDNO,IN_MATCD";
				
					System.out.println(M_strSQLQRY);
				
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
//				if(M_rstRSSET != null)
//				{	
//					if(!M_rstRSSET.next())
//					{	
//						System.out.println("Data Not Found");
//						M_rstRSSET.close();
//						return;
//					}	
//				}	   
			//	System.out.println("query executed");
				if(M_rstRSSET != null)
				{
					M_intPAGNO = 1;
					M_intLINNO = 1;
					//initialise values of counter for intime p.o,deviation in p.o., intime grin, deviation in grin
					intINTPO = 0;
					intDEVPO = 0;
					intINTGR = 0;
					intDEVGR = 0;
					intTOTAL = 0;
					strDPTCD = "";
				//if(M_rstRSSET.next())
				//{	
					while(M_rstRSSET.next())
					{
						/**method to print header if line no is exceeds 58 or page is first */
						if(M_intPAGNO == 1 ||M_intLINNO > 58)
						{
							if(M_intPAGNO > 1)
							{
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&& M_rdbTEXT.isSelected())
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
		                    	   dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
			
							}
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							dosREPORT.writeBytes("SUPREME PERTOCHEM LIMITED");
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							dosREPORT.writeBytes(padSTRING('R',"Report for internal and external lead time Analysis for period "+txtFMDAT.getText()+" To "+txtTODAT.getText(),90));							
							dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),90));
							dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							if(txtDPTNM.getText().trim().length() > 0)
							{
								if(rdoOPTDP.isSelected())
									dosREPORT.writeBytes("Department  : "+txtDPTNM.getText());
								else if(rdoOPTMG.isSelected())
									dosREPORT.writeBytes("Main Group : ("+txtDPTCD.getText()+")"+" "+txtDPTNM.getText());
								else if(rdoOPTVE.isSelected())
									dosREPORT.writeBytes("Vendor : ("+txtDPTCD.getText()+")"+" "+txtDPTNM.getText());
								else if(rdoOPTMC.isSelected())
									dosREPORT.writeBytes("Material Code : ("+txtDPTCD.getText()+")"+" "+txtDPTNM.getText());
									
								dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							}
							dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							dosREPORT.writeBytes("Ind. No    Indent      Material Code And Description                  UOM  Lead Time  Total  P.O. By     GRIN By     P.O. No.   P.O. Dt.    Supp.Name        GRIN No.   Gate-in Dt. P.O.Qty.   Rec.Qty.   Acp.Qty.   Rej.Qty.  Deviation");
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							dosREPORT.writeBytes("           Aut Dt                                                     In   Ex     tm                                                                                                                                           P.O.  Rec");
							dosREPORT.writeBytes("\n");		M_intLINNO += 1;
							dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							M_intPAGNO += 1;
						}
						dosREPORT.writeBytes("\n");		M_intLINNO += 1;
						if(txtDPTCD.getText().trim().length() == 0)
						{
							if(!strDPTCD.equals(nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"")))
							{
								dosREPORT.writeBytes("\n");		M_intLINNO += 1;
								dosREPORT.writeBytes("Department : "+hstDPTCD.get(nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"")).toString());
								dosREPORT.writeBytes("\n");		M_intLINNO += 1;
								dosREPORT.writeBytes("\n");		M_intLINNO += 1;
								strDPTCD = nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"");
							}
						}
						//IN_INDNO,IN_AUTDT,IN_MATCD,CT_MATDS,CT_UOMCD,CT_ILDTM,CT_ELDTM,CT_TOTAL,IN_PORBY,IN_EXPDT,
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),11));
						if(M_rstRSSET.getDate("IN_AUTDT") != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("IN_AUTDT")),12));
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),34));
						dosREPORT.writeBytes(padSTRING('R',"",2));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4));
						if(nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),"").substring(9).equals("2")||nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),"").substring(9).equals("6"))
						{	
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),""),5));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),""),5));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_ITOTA"),""),7));
							java.util.Date M_PORBYDT=M_fmtLCDAT.parse(nvlSTRVL(M_rstRSSET.getString("IN_AUTDT"),""));
							M_calLOCAL.setTime(M_PORBYDT);
							M_calLOCAL.add(java.util.Calendar.DATE,M_rstRSSET.getInt("CT_IILTM"));																
							M_PORBYDT1=M_fmtLCDAT.format(M_calLOCAL.getTime());
							M_calLOCAL.setTime(M_fmtLCDAT.parse(M_PORBYDT1));
							M_calLOCAL.add(java.util.Calendar.DATE,M_rstRSSET.getInt("CT_IELTM"));																
							M_GRNBYDT1=M_fmtLCDAT.format(M_calLOCAL.getTime());
							//System.out.println(M_GRNBYDT1);
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),""),5));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),""),5));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_TOTAL"),""),7));
							java.util.Date M_PORBYDT=M_fmtLCDAT.parse(nvlSTRVL(M_rstRSSET.getString("IN_AUTDT"),""));
							M_calLOCAL.setTime(M_PORBYDT);
							M_calLOCAL.add(java.util.Calendar.DATE,M_rstRSSET.getInt("CT_ILDTM"));																
							M_PORBYDT1=M_fmtLCDAT.format(M_calLOCAL.getTime());
							//java.util.Date M_GRNBYDT=M_fmtLCDAT.parse(nvlSTRVL(M_rstRSSET.getString("IN_AUTDT"),""));
							M_calLOCAL.setTime(M_fmtLCDAT.parse(M_PORBYDT1));
							M_calLOCAL.add(java.util.Calendar.DATE,M_rstRSSET.getInt("CT_ELDTM"));																
							M_GRNBYDT1=M_fmtLCDAT.format(M_calLOCAL.getTime());
						}
							
						dosREPORT.writeBytes(padSTRING('L',"",2));
						if(M_rstRSSET.getDate("IN_PORBY") != null)
							//dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("IN_PORBY")),12));
							dosREPORT.writeBytes(padSTRING('R',M_PORBYDT1,12));
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));
						
						if(M_rstRSSET.getDate("IN_EXPDT") != null)
							//dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("IN_EXPDT")),12));
							dosREPORT.writeBytes(padSTRING('R',M_GRNBYDT1,12));
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));
						//PO_PORNO,PO_PORDT,GR_GRNNO,GR_GRNDT,PO_DEVIA,GR_DEVIA
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),11));
						
						if(M_rstRSSET.getDate("PO_PORDT") != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PO_PORDT")),12));
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));
						
						//if(M_rstRSSET.getDate("PT_PRTNM") != null)
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),15));
						//else
							dosREPORT.writeBytes(padSTRING('R',"",2));
						
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),11));
						
//						if(M_rstRSSET.getDate("GR_GRNDT") != null)
//							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("GR_GRNDT")),12));
						if(M_rstRSSET.getDate("L_GINDT") != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("L_GINDT")),12));
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));

						//if(M_rstRSSET.getDate("PO_PORQT") != null)
							//dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PO_PORDT")),12));
						    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0")),3),9));
						    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0")),3),11));
						    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")),3),11));
						    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0")),3),11));
						
						//else
							//dosREPORT.writeBytes(padSTRING('R',"",6));
						
						
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_DEVIA"),"0"),5));
						//check for intime p.o. if yes then increment counter of intime p.o by one else increment deviation p.o. by one
						if(M_rstRSSET.getInt("PO_DEVIA") <= 0)
							intINTPO++;
						else
							intDEVPO++;
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_DEVIA"),"0"),5));
						//check for in time grin if yes then increment counter of intime grin by one else increment deviation grin by one
						if(M_rstRSSET.getInt("GR_DEVIA") <= 0)
							intINTGR++;
						else
							intDEVGR++;
						//increase counter for item count by one
						intTOTAL++;
					}
					dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					dosREPORT.writeBytes("\n");
					//display count of intime p.o., deviation in p.o., intime grin and deviation in grin with persentage to over all no of items
					dosREPORT.writeBytes(padSTRING('R',"In Time P.O.      : "+intINTPO,30));
					dosREPORT.writeBytes((intINTPO * 100) / intTOTAL+"%");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"Deviation In P.O. : "+intDEVPO,30));
					dosREPORT.writeBytes((intDEVPO * 100) / intTOTAL+"%");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"In Time GRIN      : "+intINTGR,30));
					dosREPORT.writeBytes((intINTGR * 100) / intTOTAL+"%");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"Deviation in GRIN : "+intDEVGR,30));
					dosREPORT.writeBytes((intDEVGR * 100) / intTOTAL+"%");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"Total No Items    : "+intTOTAL,30));
					M_intPAGNO = 1;
					M_intLINNO = 1;
					strDPTCD = "";
					M_rstRSSET.close();
					Process p;
					Runtime r = Runtime.getRuntime();
					if(M_rdbHTML.isSelected())
        			{
        			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
        			}
	                dosREPORT.close();
					fosREPORT.close();
				
					//if print option is called then print the report by callind doPRINT method 
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					    if(M_rdbHTML.isSelected())
					       p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					    else 
						    doPRINT(strFILNM);
					//if screen is selected then display it on screen
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					{
						if(M_rdbHTML.isSelected())
					        p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					    else
					        p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
    				{
    				   	cl_eml ocl_eml = new cl_eml();				    
    				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
    				    {
    					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Lead Time Analysis"," ");
    					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
    					}				    	    	
    			    }
				}
				else
				{	
					setMSG("No Data Found... ",'N');
					return;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
}