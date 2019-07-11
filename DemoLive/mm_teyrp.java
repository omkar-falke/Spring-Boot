/*
System Name   : Material Management System
Program Name  : mm_prcsl
Program Desc. : Yearly Stores Leadure Processing
Author        : A. A. Patil
Date          : 30/06/2004
Version       : MMS 2.0

Last Modified	: 02/07/2004 
Documented On	: 11/08/2004

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;import java.text.DateFormat;
import javax.swing.BorderFactory;import javax.swing.ButtonGroup;import javax.swing.JPanel;import javax.swing.JRadioButton;
/**<P>Program Description :</P><P align=center><FONT color=blue size=4><STRONG>CHANGES IN THIS CLASS ARE TO BE AUTHORISED BY H.O.D. - SYSTEMS</STRONG></FONT></P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Materials Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>    Yeary    Stores    Ledger Processing</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                                  Form                                                  for&nbsp;Year end processing of Stores Ledger </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>                   Existing system      in Maveric&nbsp;      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\mm_prcsl.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mm_prcsl.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>10/08/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>CO_CDTRN</TD>    <TD>  Series Used : MMXXPRC </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>mm_stprc</TD>    <TD>STP_STRTP,STP_MATCD</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">&nbsp;</FONT></P></TD>   <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE><P>&nbsp;</P>*/
class mm_teyrp extends cl_pbase
{
	/** TextField for year start date	 */
	private JTextField txtFMDAT;/** TextField for year end date	 */
	private JTextField txtTODAT;/** Modvat Qty.	During data transfer from GRIN table */
	
	private double fltMODQT;/** GRIN Value	During data transfer from GRIN table */
	private double fltVALUE;/** GRIN Rate	During data transfer from GRIN table */
	private double fltRATE;/**Statement to insert GRIN data in MM_PSLYR */
	private PreparedStatement pstPSLYR;/**Statement to update Values in MM_YRPRC */
	private PreparedStatement pstQUERY;/**Statement to update Values of MIN in MM_PSLYR after processing */
	private PreparedStatement pstPSLYR1;/**Statement to update Transactions other than MIN in MM_PSLYR after processing*/
	private PreparedStatement pstPSLYR2;/**Statement to generate san for adjustment values in MM_PSLYR */
	private PreparedStatement pstPSLYR3;/**Statement to generate san for adjustment values in MM_PSLYR */
	private PreparedStatement pstADJMT;/** Year Opening Stock from STP_YOSQT*/
	private double fltYOSQT;/** Year Opening Stock Value from STP_YOSQT*/
	private double fltYOSVL;/** Year Closing Stock Qty. from STP_YOSQT*/
	private double fltYCSQT;/** Year Closing Stock Value from STP_YOSQT*/
	private double fltYCSVL;/** Year Cummulative Receipt Qty. from STP_YOSQT*/
	private double fltCRCQT;/** Year Cummulative Receipt Value from STP_YOSQT*/	
	private double fltCRCVL;/** Year Cummulative Issue Qty. from STP_YOSQT*/
								
	private double fltCISQT;/** Year Cummulative Issue Value from STP_YOSQT*/
	private double fltCISVL;/** Year Cummulative MRN Qty. from STP_YOSQT*/
	private double fltCMRQT;/** Year Cummulative MRN Value from STP_YOSQT*/
	private double fltCMRVL;/** Year Cummulative SAN Qty. from STP_YOSQT*/
	private double fltCSAQT;/** Year Cummulative SAN Value from STP_YOSQT*/
	private double fltCSAVL;/** To Date Closing Weighted Avg. Rate from STP_YOSQT*/
	private double fltWAVRT;/** Year Closing Weighted Avg. Rate from STP_YOSQT*/
	private double fltCWVRT;/** Issue Value after processing MIN To PS_DOCVL */
	private double fltISSVL;/** Issue Qty. after processing MIN To PS_DOCQT */
	private double fltISSQT;/** Issue Rate after processing MIN To PS_DOCRT */
	private double fltISSRT;/** Cummulative Modvat Value for the year*/
	private double fltCMDVL;/** Cummulative Modvat Quantity for the year*/
	private double fltCMDQT;/** Year Opening Modvat Quantity */
	private double fltYOMQT;/** Year Opening Modvat Value */
	private double fltYOMVL;/** Year Closing Modvat quanitity */
	private double fltYCMQT;/** Year Closing Modvat Value */
	private double fltYCMVL;/** Modvat Quantity for current issue*/
	private double fltISMQT;/** Modvat Value for current issue */
	private double fltISMVL;
	private double fltCMIVL ;
	private double fltCMIQT ;	
	private long lngCOUNT;
	private double fltVATVL;
	private double fltCSTQT;/** Year Cummulative STN Value from STP_YOSQT*/
	private double fltCSTVL;/** Year Cummulative STN Value from STP_YOSQT*/
	private String strDOCNO;
	private String strMATCD;
	private String strSTRTP;
	private String strMODFL;
	private java.sql.Date sdtLOGDT	;
	private JRadioButton rdbAPRYR;/** Yearly processing report option*/
	private JRadioButton rdbJULYR;/**  Report Type options*/
	private JRadioButton rdbDELETE;/**  Report Type options*/
	private JRadioButton rdbNODELETE;/**  Report Type options*/
	
	public mm_teyrp()
	{
		super(2);
		fltMODQT = 0;	fltVALUE = 0;	fltRATE = 0;fltVATVL = 0;
		fltISSQT = 0;		fltISSVL = 0;	fltISSRT = 0.0f;
		
		fltYOSQT = 0;	fltYOSVL = 0;	fltYCSQT = 0;	fltYCSVL = 0;
		fltCRCQT = 0;	fltCRCVL = 0;	fltCISQT = 0;	fltCISVL = 0;
		fltCMRQT = 0;	fltCMRVL = 0;	fltCSAQT = 0;	fltCSAVL = 0;
		fltWAVRT = 0;	fltCWVRT = 0;   fltCSTQT = 0;	fltCSTVL = 0;
		
		lngCOUNT = 0;	//Counter For Processing
			
		strMATCD = "";	strMODFL = "";	strDOCNO = "";
		
		setMatrix(20,8);
		JPanel L_pnlTEMP=new JPanel(null);
		add(rdbDELETE=new JRadioButton("Copy Data"),1,1,1,1,L_pnlTEMP,'L');
		add(rdbNODELETE=new JRadioButton("Continue Existing"),1,2,1,1,L_pnlTEMP,'L');
		rdbNODELETE.setSelected(true);
		ButtonGroup L_btgTEMP=new ButtonGroup();
		L_btgTEMP.add(rdbDELETE);L_btgTEMP.add(rdbNODELETE);
		add(L_pnlTEMP,1,1,2,2.1,this,'L');
		L_pnlTEMP.setBorder(BorderFactory.createTitledBorder("Processing Type"));

		L_pnlTEMP=new JPanel(null);
	
		add(rdbJULYR=new JRadioButton("July"),1,1,1,1,L_pnlTEMP,'L');
		add(rdbAPRYR=new JRadioButton("April"),1,2,1,1,L_pnlTEMP,'L');
		rdbJULYR.setSelected(true);
		L_btgTEMP=new ButtonGroup();
		L_btgTEMP.add(rdbJULYR);L_btgTEMP.add(rdbAPRYR);
		add(L_pnlTEMP,3,1,2,2.1,this,'L');
		L_pnlTEMP.setBorder(BorderFactory.createTitledBorder("Processing Type"));

		add(new JLabel("From Date"),3,4,1,1,this,'L');
		add(txtFMDAT = new TxtDate(),3,5,1,1,this,'L');
		add(new JLabel("To Date"),4,4,1,1,this,'L');
		add(txtTODAT = new TxtDate(),4,5,1,1,this,'L');
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					txtFMDAT.requestFocus();
					txtFMDAT.setText("01/07/");
				}
				else
				{
					setMSG("Select Modification..",'N');
					setENBL(false);
				}
			}
		}
		catch(Exception e){setMSG(e,"actionPerformed");}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().length() == 0)
					txtFMDAT.requestFocus();
				else
				{
					txtTODAT.requestFocus();
					txtTODAT.setText("30/06/"+(Integer.parseInt(txtFMDAT.getText().substring(6,10))+1));
				}
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().length() == 0)
					txtTODAT.requestFocus();
				else
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	public void exeSAVE()
	{
		try
		{
			strMATCD = "";
			strMODFL = "";
			cl_dat.M_flgLCUPD_pbst = true;
			DateFormat L_fmtLOCAL=new SimpleDateFormat("yyyy-mm-dd");
			/*Delete All Records From MM_YRPRC
			 */
			if(rdbDELETE.isSelected())
			{
			setMSG("Deleting Record From Table MM_YRPRC ..",'N');
			M_rstRSSET=cl_dat.exeSQLQRY0("Select count(*) from mm_yrprc");
			if(M_rstRSSET.next())
				if(M_rstRSSET.getInt(1)>0)
				{
					cl_dat.exeSQLUPD("DELETE FROM MM_YRPRC ","setLCLUPD");
					if(cl_dat.exeDBCMT("exeSAVE"))
						setMSG("Delete MM_YRPRC Successful..",'N');
					else
						setMSG("Delete MM_YRPRC Fail..",'N');
				}

			/* Insert Recort In MM_YRPRC From mm_stprc_010708
			 */
			if(cl_dat.M_flgLCUPD_pbst)
			{
				String L_strTBLNM ="";
				if(rdbJULYR.isSelected())
					L_strTBLNM = "mm_stprc";
					//L_strTBLNM = "BK_STP010705";
					//L_strTBLNM = "MM_STP300604";
				else if(rdbAPRYR.isSelected())
					L_strTBLNM = "MM_STPAP";
				setMSG("Inserting In To MM_YRPRC From "+L_strTBLNM +"..",'N');//MM_stp300604
				M_strSQLQRY = "INSERT INTO MM_YRPRC (YR_CMPCD,YR_MMSBS, YR_STRTP, YR_MATCD, YR_MATDS, YR_UOMCD, YR_YOSQT, YR_YOSVL, YR_YOMQT, YR_YOMVL, YR_WAVRT )"
							   +" SELECT STP_CMPCD,STP_MMSBS,STP_STRTP,STP_MATCD,STP_MATDS,STP_UOMCD,STP_YOSQT,STP_YOSVL,STP_YOMQT,STP_YOMVL, STP_WAVRT FROM "+L_strTBLNM +" WHERE STP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and STP_STRTP in ('01','06','07','08')";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					M_strSQLQRY = "UPDATE MM_YRPRC SET YR_YCSQT = 0,YR_YCSVL = 0,"
						+"YR_YCMQT = YR_YOMQT,YR_YCMVL = YR_YOMVL,YR_CRCQT = 0,YR_CRCVL = 0,"//
						+"YR_CISQT = 0,YR_CISVL = 0,YR_CMRQT = 0,YR_CMRVL = 0,"
						+"YR_CSAQT = 0,YR_CSAVL = 0,YR_CSTQT = 0,YR_CSTVL = 0,"
						+"YR_CMDQT = 0,YR_CMDVL = 0,YR_CMIQT = 0,"//YR_WAVRT = 0,
						+"YR_CMIVL = 0 where YR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
				setMSG("Values Inserted In To Table MM_YRPRC..",'N');
			else
				setMSG("Fail To Insert In To  MM_YRPRC",'N');
			
			/*	Delete All Record From MM_PSLYR
			 */
			M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_PSLYR");
			if(M_rstRSSET.next())
			{
				if(M_rstRSSET.getInt(1) > 0)
				{
					setMSG("Deleting Previous Record From MM_PSLYR..",'N');
					cl_dat.exeSQLUPD("DELETE FROM MM_PSLYR ","setLCLUPD");
					if(cl_dat.exeDBCMT("exeSAVE"))
						setMSG("Deleted From MM_PSLYR..",'N');
					else
						setMSG("Fail To Deleted From MM_PSLYR..",'N');
				}
			}
			
			M_rstRSSET.close();
			//Processin From GRIN Table
			if(cl_dat.M_flgLCUPD_pbst)
			{
				M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_GRMST WHERE GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(GR_ACPQT,0) > 0 AND "
					+"isnull(GR_STSFL,' ') = '2' AND isnull(GR_STRTP,' ') in  ('01','06','07','08') "
					+" AND GR_ACPDT BETWEEN '"// AND substr(GR_MATCD,1,3) = '680'
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ");
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt(1) > 0)
					{
						setMSG("Get Data From GRIN Table..",'N');
						M_strSQLQRY = "SELECT GR_STRTP,GR_MATCD,GR_ACPDT,GR_GRNNO,GR_BATNO,GR_ACPQT,"
							+"GR_MODFL,GR_BILVL,GR_PORVL,GR_MODVL,isnull(GR_VATBL,GR_VATVL) L_VATVL FROM MM_GRMST WHERE GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(GR_ACPQT,0) > 0 AND "
							+"isnull(GR_STSFL,' ') = '2' AND isnull(GR_STRTP,' ') in  ('01','06','07','08') AND GR_ACPDT BETWEEN '"// AND substr(GR_MATCD,1,3) = '680'
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
						//	+ " AND GR_MATCD like '40%'"						  
							+"ORDER BY GR_GRNNO,GR_MATCD";
					//System.out.println(M_strSQLQRY);
						
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							double fltMODVL=0;
							pstPSLYR=cl_dat.M_conSPDBA_pbst.prepareStatement("INSERT INTO MM_PSLYR ("
								+" PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,"
								+"PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL,PS_MODQT,"
								+"PS_MODVL,PS_PREDT) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
							while(M_rstRSSET.next())
							{
								strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
								strMODFL = nvlSTRVL(M_rstRSSET.getString("GR_MODFL"),"");
		//						if(strMATCD.equals("0141300091"))
		//							System.out.println(M_rstRSSET.getString("GR_ACPDT"));
								if(strMODFL.equalsIgnoreCase("Y"))
								   fltMODQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.0"));
								else
									fltMODQT = 0;
								if(strMODFL.equalsIgnoreCase("Y"))
								   fltMODVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0.0"));
								else
									fltMODVL = 0;
								
								
								if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0.0")) == 0)
								{
									fltVALUE = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_PORVL"),"0.0"));
									// Added on 27/12/2005
								//	fltVATVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_VATVL"),"0.0"));
								}
								else
								{
									fltVALUE = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0.0"));
									// Added on 27/12/2005
								//	fltVATVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_VATBL"),"0.0"));
								}
								//fltVALUE = fltVALUE - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0.0"))- fltVATVL ;
								fltVALUE = fltVALUE - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0.0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_VATVL"),"0.0")) ;
								/*if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_VATVL"),"0.0")) >0)
								{
								    System.out.println(strMATCD + " "+fltVALUE);
								}*/
								
								if(!(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.0"))==0.0))
								fltRATE = fltVALUE / Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.0"));
								pstPSLYR.setString(1,cl_dat.M_strCMPCD_pbst);
								pstPSLYR.setString(2,nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),""));
								pstPSLYR.setString(3,nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""));
								pstPSLYR.setString(4,"1");
								pstPSLYR.setString(5,nvlSTRVL(M_rstRSSET.getString("GR_BATNO"),""));
								pstPSLYR.setDate(6,M_rstRSSET.getDate("GR_ACPDT"));
								pstPSLYR.setString(7,nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""));
								pstPSLYR.setString(8,nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.0"));
								pstPSLYR.setDouble(9,fltRATE);
								pstPSLYR.setDouble(10,fltVALUE);
								pstPSLYR.setDouble(11,fltMODQT);
								pstPSLYR.setDouble(12,fltMODVL);
								pstPSLYR.setDate(13,M_rstRSSET.getDate("GR_ACPDT"));
								pstPSLYR.addBatch();
								int v=pstPSLYR.executeUpdate();
								cl_dat.exeDBCMT("exeSAVE()");
		//						if(strMATCD.equals("0141300091"))
		//					   System.out.println(M_rstRSSET.getDate("GR_ACPDT"));
							}
		//					int v=pstPSLYR.executeUpdate();
		//					if(strMATCD.equals("0141300091"))
		//					   System.out.println(v);
							if(cl_dat.M_flgLCUPD_pbst)
								setMSG("Insertion For GRIN Successful..",'N');
							else
								setMSG("Insertion For GRIN Fail..",'N');
						}
					}
				}
			}
			//Process For MRN
			setMSG("Processing For MRN",'N');
			if(cl_dat.M_flgLCUPD_pbst)
			{
				M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_MRMST WHERE MR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(MR_RETQT,0) > 0 AND "
					+"isnull(MR_STSFL,' ') = '5' AND isnull(MR_STRTP,' ') in ('01','06','07','08') AND MR_AUTDT BETWEEN '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'");
				//	+ " AND MR_MATCD like '40%'");
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt(1) > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_PSLYR (PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL) " 
							+"(SELECT MR_CMPCD,MR_STRTP,MR_MATCD,'2',MR_MRNNO,MR_MRNDT,MR_MRNNO,MR_RETQT,MR_MRNRT,MR_MRNVL"
							+" FROM MM_MRMST WHERE MR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(MR_RETQT,0) > 0 AND "
							+"isnull(MR_STSFL,' ') = '5' AND isnull(MR_STRTP,' ') in ('01','06','07','08') AND MR_AUTDT BETWEEN '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
					//		+ " AND MR_MATCD like '40%'"		
							+")" 
							;//ORDER BY MR_STRTP,MR_MRNNO,MR_MATCD
		//				System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			//Process For SAN
			setMSG("Processing For SAN",'N');
			if(cl_dat.M_flgLCUPD_pbst)
			{
				M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_SAMST WHERE SA_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(SA_STSFL,' ') <> 'X' AND "
						+"isnull(SA_STRTP,' ') in ( '01','06','07','08') AND SA_SANDT BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and SA_SANQT>=0 ");
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt(1) > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_PSLYR (PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL)"
								+"(SELECT SA_CMPCD,SA_STRTP,SA_MATCD,'3',SA_SANNO,SA_SANDT,SA_SANNO,SA_SANQT,SA_SANRT,SA_SANVL"
								+" FROM MM_SAMST WHERE SA_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(SA_STSFL,' ') <> 'X' AND "
								+"isnull(SA_STRTP,' ') in ( '01','06','07','08') AND SA_SANDT BETWEEN '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and SA_SANQT>=0 "
							 //   +" AND SA_MATCD like '40%'"
								+" )";
			//					+"ORDER BY SA_STRTP,SA_SANNO,SA_MATCD";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
				// -san previously 5 is now 7
				M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_SAMST WHERE SA_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(SA_STSFL,' ') <> 'X' AND "
						+"isnull(SA_STRTP,' ') in  ('01','06','07','08') AND SA_SANDT BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and SA_SANQT<0 ");
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt(1) > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_PSLYR (PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL)"
								+"(SELECT SA_CMPCD,SA_STRTP,SA_MATCD,'7',SA_SANNO,SA_SANDT,SA_SANNO,SA_SANQT,SA_SANRT,SA_SANVL"
								+" FROM MM_SAMST WHERE SA_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(SA_STSFL,' ') <> 'X' AND "
								+"isnull(SA_STRTP,' ') in  ('01','06','07','08') AND SA_SANDT BETWEEN '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and SA_SANQT<0 "
							//	+" AND SA_MATCD like '40%'"
								+")";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			//Process For STN
			setMSG("Processing For STN",'N');
			if(cl_dat.M_flgLCUPD_pbst)
			{
				M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_STNTR WHERE STN_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(STN_STSFL,' ') = '2' AND "
						+"isnull(STN_TOSTR,' ') in ( '01','06','07','08') AND STN_STNDT BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and STN_STNTP IN('01','02') ");
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt(1) > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_PSLYR (PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL)"
								+"(SELECT STN_CMPCD,STN_TOSTR,STN_TOMAT,'4',STN_STNNO,STN_STNDT,STN_STNNO,STN_TRNQT,STN_STNRT,STN_TRNVL"
								+" FROM MM_STNTR WHERE STN_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(STN_STSFL,' ') = '2' AND "
								+"isnull(STN_TOSTR,' ') in ( '01','06','07','08') AND STN_STNDT BETWEEN '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and STN_STNTP IN('01','02') "
							 //   +" AND SA_MATCD like '40%'"
								+" )";
			//					+"ORDER BY SA_STRTP,SA_SANNO,SA_MATCD";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
				// -san previously 5 is now 7
				M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_STNTR WHERE STN_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(STN_STSFL,' ') = '2' AND "
						+"isnull(STN_FRSTR,' ') in  ('01','06','07','08') AND STN_STNDT BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and STN_STNTP IN('01','03')");
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt(1) > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_PSLYR (PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL)"
								+"(SELECT STN_CMPCD,STN_FRSTR,STN_FRMAT,'8',STN_STNNO,STN_STNDT,STN_STNNO,(- STN_TRNQT)STN_TRNQT,STN_STNRT,(- STN_TRNVL) STN_TRNVL"
								+" FROM MM_STNTR WHERE STN_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(STN_STSFL,' ') = '2' AND "
								+"isnull(STN_FRSTR,' ') in  ('01','06','07','08') AND STN_STNDT BETWEEN '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
								+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and STN_STNTP IN('01','03') "
							//	+" AND SA_MATCD like '40%'"
								+")";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}

			//Process For ISSUE // issue previously 4 is now 6
			if(cl_dat.M_flgLCUPD_pbst)
			{
				M_rstRSSET = cl_dat.exeSQLQRY("SELECT count(*) FROM MM_ISMST "
							+"WHERE IS_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(IS_ISSQT,0) > 0  AND "	//AND substr(IS_MATCD,1,3) = '680'
							+"isnull(IS_STSFL,' ') = '2' AND isnull(IS_STRTP,' ') in  ('01','06','07','08') AND CONVERT(varchar,IS_AUTDT,103) BETWEEN '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ");
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt(1) > 0)
					{
						M_strSQLQRY = "INSERT INTO MM_PSLYR (PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL ) ("
							+"SELECT IS_CMPCD,IS_STRTP,IS_MATCD,'6',IS_TAGNO + '|' + IS_BATNO + '|' + IS_GRNNO IS_TAGNO,CONVERT(varchar,IS_AUTDT,103),IS_ISSNO,IS_ISSQT,IS_ISSRT,IS_ISSVL FROM MM_ISMST "
							+"WHERE IS_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(IS_ISSQT,0) > 0  AND "	//AND substr(IS_MATCD,1,3) = '680'
							+"isnull(IS_STSFL,' ') = '2' AND isnull(IS_STRTP,' ') in  ('01','06','07','08') AND CONVERT(varchar,IS_AUTDT,103) BETWEEN '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'"
					//		+" AND IS_MATCD like '40%'"
							+")";
					//	System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			
			if(cl_dat.exeDBCMT("exeSAVE"))
				setMSG("Insertin From GRIN,MRN,SAN & ISSUE Completed..",'N');
			else
				setMSG("Insertin From GRIN,MRN,SAN & ISSUE Fail..",'N');
			}	
			lngCOUNT = 0;	//loop counter
			fltYOSQT = 0;	fltYOSVL = 0;		//year opening stock quantity and value
			fltYCSQT = 0;	fltYCSVL = 0;		//year closing quantity and value
			fltCRCQT = 0;	fltCRCVL = 0;		//cum rec quantity and value
			fltCISQT = 0;	fltCISVL = 0;		//cum iss quantity and value
			fltCMRQT = 0;	fltCMRVL = 0;		//cum mrn quantity and value
			fltCSAQT = 0;	fltCSAVL = 0;		//cum san quantity and value
			fltCSTQT = 0;	fltCSTVL = 0;		//cum san quantity and value
			fltWAVRT = 0;	fltCWVRT = 0;		//Wated Avg Rate And Closing Wated Avg Rate
			fltISSVL = 0;	fltISSQT = 0;	fltISSVL = 0;	//Issue Val, Qty, Rate
			fltCMDVL = 0;	fltCMDQT = 0;
			fltYOMQT = 0;/** Year Opening Modvat Value */
			fltYOMVL = 0;/** Year Closing Modvat quanitity */
			fltYCMQT = 0;/** Year Closing Modvat Value */
			fltYCMVL = 0;/** Modvat Quantity for current issue*/
			fltISMQT = 0;/** Modvat Value for current issue */
			fltISMVL = 0;

			String L_strPRMAT = "",L_strPRSTR ="";	// Variable To Store Temparory Material Code
			strMATCD = "";	strDOCNO = "";
			cl_dat.exeDBCMT("exeSAVE()");
//			if(true)
//					return;
			
			
	//		cl_dat.exeSQLUPD("delete from MM_PSLYR where PS_MATCD not in ('6805082016','6805350175','6805404925')","setLCLUPD");
	//		cl_dat.exeDBCMT("exeSAVE");
			/*Main Yearly Store Leager Processing 
			 */
			M_strSQLQRY = "SELECT PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,"
				+"PS_DOCQT,PS_DOCRT,PS_DOCVL,PS_MODQT,PS_MODVL,YR_YOSQT,YR_YOSVL,YR_YOMQT,YR_YOMVL FROM "
				+"MM_PSLYR,MM_YRPRC WHERE YR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and YR_STRTP = PS_STRTP AND YR_MATCD = PS_MATCD and YR_PRCDT is null "
				//+"AND PS_MATCD BETWEEN '0000000000' AND '4999999999' "
			//	+" AND PS_MATCD like '95%'"//42040175','9542040325')"		 
				+"ORDER BY PS_STRTP,PS_MATCD,PS_DOCDT,PS_DOCTP,PS_DOCNO,PS_DOCRF ";
//			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst=true;
			if(M_rstRSSET != null)
			{
				
				sdtLOGDT=java.sql.Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)));
				
				pstQUERY=cl_dat.M_conSPDBA_pbst.prepareStatement("UPDATE MM_YRPRC SET "
									+"YR_YCSQT = ?,YR_YCSVL = ?,"
									+"YR_CRCQT = ?,YR_CRCVL = ?,"
									+"YR_CISQT = ?,YR_CISVL = ?,"
									+"YR_CMRQT = ?,YR_CMRVL = ?,"
									+"YR_CSAQT = ?,YR_CSAVL = ?,"
									+"YR_CMDQT = ?,YR_CMDVL = ?,YR_PRCDT = ?,"
									+" YR_ADJVL = ?,YR_YCLRT = ?, "
									+"YR_YCMQT = ?,YR_YCMVL = ?, "
									+"YR_CMIQT = ?,YR_CMIVL = ?, "
									+"YR_CSTQT = ?,YR_CSTVL = ? "
									+"WHERE "
									+" YR_CMPCD = ? and YR_STRTP = ? AND YR_MATCD = ?");
				pstPSLYR1=cl_dat.M_conSPDBA_pbst.prepareStatement("UPDATE MM_PSLYR SET "
								+"PS_PREDT = ?, "
								+"PS_DOCRT = ?,PS_DOCVL = ? ,PS_MODQT = ? ,PS_MODVL = ? WHERE PS_CMPCD = ? and PS_DOCNO = ? AND "
								+"PS_DOCTP = ? AND "
								+"PS_MATCD = ? AND PS_DOCRF = ?");
				pstPSLYR2=cl_dat.M_conSPDBA_pbst.prepareStatement("UPDATE MM_PSLYR SET "
								+"PS_PREDT = ? "
								+"WHERE PS_CMPCD = ? and PS_DOCNO = ? and PS_DOCTP=? "
								+"AND PS_MATCD=? ");
				pstPSLYR3=cl_dat.M_conSPDBA_pbst.prepareStatement("UPDATE MM_PSLYR SET "
								+"PS_PREDT = ? "
								+"WHERE PS_CMPCD = ? and PS_DOCNO = ? and PS_DOCTP=? "
								+"AND PS_MATCD=? and PS_DOCRF=?");
				pstADJMT=cl_dat.M_conSPDBA_pbst.prepareStatement("INSERT INTO MM_PSLYR ( "
								+" PS_CMPCD,PS_STRTP,PS_MATCD,PS_DOCTP,PS_DOCRF,PS_DOCDT,PS_DOCNO,PS_DOCQT,PS_DOCRT,PS_DOCVL,PS_MODQT,PS_MODVL,PS_PREDT) values ("
								+" ?,?,?,?,?,?,?,?,?,?,?,?,?)");
		//		boolean L_flgPSLYR2=false,L_flgPSLYR1=false;
				while(M_rstRSSET.next())
				{
					//Take The Current Material Code 
					strMATCD = M_rstRSSET.getString("PS_MATCD");
					strDOCNO = nvlSTRVL(M_rstRSSET.getString("PS_DOCNO"),"");
				//	System.out.println(strMATCD +" "+strDOCNO+" "+L_strPRMAT);
					if((lngCOUNT==1000)||(lngCOUNT==2000)||(lngCOUNT==3000)||(lngCOUNT==4000)||(lngCOUNT==5000)||(lngCOUNT==6000)||(lngCOUNT==7000))
					   System.out.println(lngCOUNT +" "+strMATCD);
					if(!L_strPRMAT.equals(strMATCD))	//Check Current Material Cd. With Previous
					{
						if(lngCOUNT > 0)	//Other Than First Record 
						{	// Update For Material Code L_strPRMAT i.e. For previous Material Code
							if(cl_dat.M_flgLCUPD_pbst)
							{
						// CHECK FOR SAN QTY & IF REQUIRED< GENERATE SAN
								
							//	System.out.println("san generation "+fltCWVRT);
							//	System.out.println("YOsVL "+fltYOSVL);
							//	System.out.println("CRCVL "+fltCRCVL);
							//	System.out.println("CSAVL "+fltCSAVL);
							//	System.out.println("CWVRT "+fltCWVRT);
							//	System.out.println("YCSQT "+fltYCSQT);
							//	System.out.println("CISVL "+fltCISVL);
							//	System.out.println("CMRVL "+fltCMRVL);
								
								double L_fltDIFVL=-((fltYOSVL+fltCRCVL+fltCSAVL) - (fltCWVRT * fltYCSQT) - (fltCISVL-fltCMRVL));
							//	System.out.println("DIFVL "+L_fltDIFVL);
								if(L_fltDIFVL<-0.01f || L_fltDIFVL>0.01f)
								{
							//		System.out.println("DIFVL in range");
									fltYCSVL+=L_fltDIFVL;
									if(fltYCSQT>0.00001)
									{
										fltCWVRT=fltYCSVL/fltYCSQT;
										//System.out.println(fltCWVRT +" from 1");
									}
									pstADJMT.setString(1,cl_dat.M_strCMPCD_pbst);
									pstADJMT.setString(2,L_strPRSTR);
									pstADJMT.setString(3,L_strPRMAT);
									if(L_fltDIFVL<0.0f)
										pstADJMT.setString(4,"7");  // -VE SAN IS NOW 7 INSTEAD OF 5
									else
										pstADJMT.setString(4,"3");
									pstADJMT.setString(5,"9999999999");
									pstADJMT.setDate(6,sdtLOGDT);
									pstADJMT.setString(7,"99999999");
									pstADJMT.setDouble(8,0.0f);
									pstADJMT.setDouble(9,0.0f);
									pstADJMT.setDouble(10,L_fltDIFVL);
									pstADJMT.setDouble(11,0.0f);
									pstADJMT.setDouble(12,0.0f);
									pstADJMT.setDate(13,sdtLOGDT);
									pstADJMT.addBatch();
									pstADJMT.executeUpdate();
								}
								pstQUERY.setDouble(1,Double.parseDouble(setNumberFormat(fltYCSQT,3)));
								pstQUERY.setDouble(2,Double.parseDouble(setNumberFormat(fltYCSVL,2)));
								pstQUERY.setDouble(3,Double.parseDouble(setNumberFormat(fltCRCQT,3)));
								pstQUERY.setDouble(4,Double.parseDouble(setNumberFormat(fltCRCVL,2)));
								pstQUERY.setDouble(5,Double.parseDouble(setNumberFormat(fltCISQT,3)));
								pstQUERY.setDouble(6,Double.parseDouble(setNumberFormat(fltCISVL,2)));
								pstQUERY.setDouble(7,Double.parseDouble(setNumberFormat(fltCMRQT,3)));
								pstQUERY.setDouble(8,Double.parseDouble(setNumberFormat(fltCMRVL,2)));
								pstQUERY.setDouble(9,Double.parseDouble(setNumberFormat(fltCSAQT,3)));
								pstQUERY.setDouble(10,Double.parseDouble(setNumberFormat(fltCSAVL,2)));
							    ////////// FOR STN 
								pstQUERY.setDouble(11,Double.parseDouble(setNumberFormat(fltCMDQT,3)));
								pstQUERY.setDouble(12,Double.parseDouble(setNumberFormat(fltCMDVL,2)));
								pstQUERY.setDate(13,sdtLOGDT);
								pstQUERY.setDouble(14,L_fltDIFVL);
								pstQUERY.setDouble(15,Double.parseDouble(setNumberFormat(fltCWVRT,2)));
								pstQUERY.setDouble(16,Double.parseDouble(setNumberFormat(fltYCMQT,3)));
								pstQUERY.setDouble(17,Double.parseDouble(setNumberFormat(fltYCMVL,2)));
								pstQUERY.setDouble(18,Double.parseDouble(setNumberFormat(fltCMIQT,3)));
								pstQUERY.setDouble(19,Double.parseDouble(setNumberFormat(fltCMIVL,2)));
								pstQUERY.setDouble(20,Double.parseDouble(setNumberFormat(fltCSTQT,3)));
								if(fltCSTQT >0.0)
								    System.out.println("stock transfer qty " + fltCSTQT);
								pstQUERY.setDouble(21,Double.parseDouble(setNumberFormat(fltCSTVL,2)));
							//	pstQUERY.setString(20,strSTRTP);
								pstQUERY.setString(22,cl_dat.M_strCMPCD_pbst);
								pstQUERY.setString(23,L_strPRSTR);
								pstQUERY.setString(24,L_strPRMAT);
								pstQUERY.executeUpdate();
								cl_dat.exeDBCMT("exeSAVE");
						//		L_flgPSLYR2=false;L_flgPSLYR1=false;
								setMSG("No. of records processed : "+String.valueOf(++lngCOUNT),'N');
								cl_dat.exeDBCMT("exeSAVE");
								//Variables initialise To Zero For New Material Code
						fltCRCQT = 0;	fltCRCVL = 0;		//cum receipt qty and value
						fltCISQT = 0;	fltCISVL = 0;		//cum iss qty and value
						fltCMRQT = 0;	fltCMRVL = 0;		//cum mrn qty and value
						fltCSAQT = 0;	fltCSAVL = 0;		//cum san qty and value
						fltCSTQT = 0;	fltCSTVL = 0;		//cum san qty and value
						fltWAVRT = 0;
						fltCMDVL = 0;	fltCMDQT = 0;
						fltCMIVL = 0;	fltCMIQT = 0;
						fltYOMQT = 0;/** Year Opening Modvat Value */
						fltYOMVL = 0;/** Year Closing Modvat quanitity */
						fltYCMQT = 0;/** Year Closing Modvat Value */
						fltYCMVL = 0;/** Modvat Quantity for current issue*/
							}
							else
							{
								System.out.println("Error while Updating DB "+M_strSQLQRY);
								break;
							}
						}
						else
							lngCOUNT++;
						//System.out.println("before modvat calculation");
				//For modvat claculations
						fltISMQT = 0;/** Modvat Value for current issue */
						fltISMVL = 0;
						fltYOMQT= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("YR_YOMQT"),"0.0"));
						fltYOMVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("YR_YOMVL"),"0.0"));
						fltYCMQT = fltYOMQT;
					//	if(strMATCD.equals("3530820301"))
					//		System.out.println(fltYCMQT);
						fltYCMVL = fltYOMVL;
				//////
						// Opening Stock Qty, And Opening Stock Value For New Materail Code
						fltYOSQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("YR_YOSQT"),"0.0"));
						fltYOSVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("YR_YOSVL"),"0.0"));
						fltYCSQT = fltYOSQT;
						fltYCSVL = fltYOSVL;
						if(fltYCSQT>0.00001)
						{
							fltCWVRT = fltYCSVL / fltYCSQT ; //Closing Wated Avg Rate
						}
					}
			//		if(!(M_rstRSSET.getString("PS_MATCD").equals("6805082016")||M_rstRSSET.getString("PS_MATCD").equals("6805350175")||M_rstRSSET.getString("PS_MATCD").equals("6805404925")))
			//			continue;
					if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("1"))//GRIN
					{
						fltCRCQT = fltCRCQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltCRCVL = fltCRCVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						fltYCSQT = fltYCSQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltYCSVL = fltYCSVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						if(fltCRCQT>0.0)
						{
//							if(strMATCD.equals("3820350185"))
//								System.out.println("for wt avg "+fltCRCVL+" / "+fltCRCQT);
							fltWAVRT = fltCRCVL / fltCRCQT;		//Wated Avg Rate
						}
						if(fltYCSQT>0.00001)
						{
						fltCWVRT = fltYCSVL / fltYCSQT;		//Closing Wated Avg Rate
						}
			//FOr modvat calculations
						fltYCMQT+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_MODQT"),"0.0"));
						fltYCMVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_MODVL"),"0.0"));
						fltCMDQT+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_MODQT"),"0.0"));
						fltCMDVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_MODVL"),"0.0"));
			///////
					//	if(strMATCD.equals("6810010211"))
					//	System.out.println("processing"+M_rstRSSET.getString("PS_DOCNO")+" "+M_rstRSSET.getString("PS_DOCQT")+" "+fltCRCQT);
					//	System.out.println("grin out");
					}
					else if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("2")) //MRN
					{
						fltYCSQT = fltYCSQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltYCSVL = fltYCSVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						fltCMRQT = fltCMRQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltCMRVL = fltCMRVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						if(fltYCSQT>0.00001)
						{
						fltCWVRT = fltYCSVL / fltYCSQT;
						}
					}
					else if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("3"))//SAN +ve
					{
						fltCSAQT = fltCSAQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltCSAVL = fltCSAVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						fltYCSQT = fltYCSQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltYCSVL = fltYCSVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						if(fltYCSQT>0.00001)
						{
						fltCWVRT = fltYCSVL / fltYCSQT;
						}
					}
					else if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("4"))//STN +ve
					{
						fltCSTQT = fltCSTQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltCSTVL = fltCSTVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						fltYCSQT = fltYCSQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltYCSVL = fltYCSVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						if(fltYCSQT>0.00001)
						{
						fltCWVRT = fltYCSVL / fltYCSQT;
						}
					}
					else if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("6"))//ISSUE
					{
					 	fltISSQT=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltYCSQT = fltYCSQT - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltISSVL = (fltYOSVL + fltCRCVL+fltCSAVL) - (fltCWVRT * fltYCSQT) - (fltCISVL-fltCMRVL);	//Issue Value
						if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"))>0.0)
							fltISSRT = fltISSVL / Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));	//Issue Rate
						if(fltISSRT<=0.009f)
							fltISSRT = 0.01f;
						fltCISQT = fltCISQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltCISVL = fltCISVL + fltISSVL;
						fltYCSVL = fltYCSVL - fltISSVL;
				//For modvat calculations
						if(fltYCMQT>0)
						{
							fltISMQT=(fltYCMQT>fltISSQT ? fltISSQT : fltYCMQT);
							fltISMVL=fltISMQT*(fltYCMVL/fltYCMQT);
							fltCMIVL += fltISMVL;
							fltCMIQT += fltISMQT;
							fltYCMVL -= fltISMVL;
							fltYCMQT -= fltISMQT;
						}
				///////\
					}
					else if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("7"))//SAN -ve
					{
						fltCSAQT = fltCSAQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltCSAVL = fltCSAVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						fltYCSQT = fltYCSQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltYCSVL = fltYCSVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						if(fltYCSQT>0.00001)
						{
						fltCWVRT = fltYCSVL / fltYCSQT;
						}
						else
							fltCWVRT=0.0f;
						
					}
					else if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("8"))//STN -ve
					{
						fltCSTQT = fltCSTQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltCSTVL = fltCSTVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						fltYCSQT = fltYCSQT + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0.0"));
						fltYCSVL = fltYCSVL + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0.0"));
						if(fltYCSQT>0.00001)
						{
						fltCWVRT = fltYCSVL / fltYCSQT;
						}
						else
							fltCWVRT=0.0f;
						
					}
					//Set Current Material Code/Stores Type In Previous Material Code For next Loop
					L_strPRMAT = nvlSTRVL(M_rstRSSET.getString("PS_MATCD"),"");
					L_strPRSTR = nvlSTRVL(M_rstRSSET.getString("PS_STRTP"),"");
					strSTRTP = nvlSTRVL(M_rstRSSET.getString("PS_STRTP"),"");
					if(cl_dat.M_flgLCUPD_pbst)
					{
						if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("6"))//ISSUE
						{
							/*M_strSQLQRY = "UPDATE MM_PSLYR SET "
								+"PS_DOCDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', "
								+"PS_DOCRT = "+setNumberFormat(fltISSRT,2)+",PS_DOCVL = "+setNumberFormat(fltISSVL,2)+
							"  WHERE PS_DOCNO = '"+strDOCNO+"' AND "
								+"PS_DOCTP = '"+M_rstRSSET.getString("PS_DOCTP")+"' AND "
								+"PS_MATCD = '"+strMATCD+"'";*/
							
						//	System.out.println(sdtLOGDT+"||"+fltISSRT+"||"+fltISSVL+"||"+fltISMQT+"||"+fltISMVL+"||"+strDOCNO+"||"+strMATCD);
							pstPSLYR1.setDate(1,sdtLOGDT);
							pstPSLYR1.setDouble(2,fltISSRT);
							pstPSLYR1.setDouble(3,fltISSVL);
							pstPSLYR1.setDouble(4,fltISMQT);
							pstPSLYR1.setDouble(5,fltISMVL);
							pstPSLYR1.setString(6,cl_dat.M_strCMPCD_pbst);
							pstPSLYR1.setString(7,strDOCNO);
							pstPSLYR1.setString(8,M_rstRSSET.getString("PS_DOCTP"));
							pstPSLYR1.setString(9,strMATCD);
							pstPSLYR1.setString(10,M_rstRSSET.getString("PS_DOCRF"));
							pstPSLYR1.addBatch();
							pstPSLYR1.executeUpdate();
						//	System.out.println(" issue procesed");
						//	L_flgPSLYR1=true;
						}
						else if(nvlSTRVL(M_rstRSSET.getString("PS_DOCTP"),"").equals("1"))//GRIN
						{
							/*M_strSQLQRY = "UPDATE MM_PSLYR SET "
								+"PS_DOCDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
								+"WHERE PS_DOCNO = '"+strDOCNO+"' and PS_DOCTP='"+M_rstRSSET.getString("PS_DOCTP")+"' "
								+"AND PS_MATCD='"+strMATCD+"'";*/
							
							pstPSLYR3.setDate(1,sdtLOGDT);
							pstPSLYR3.setString(2,cl_dat.M_strCMPCD_pbst);
							pstPSLYR3.setString(3,strDOCNO);
							pstPSLYR3.setString(4,M_rstRSSET.getString("PS_DOCTP"));
							pstPSLYR3.setString(5,strMATCD);
							pstPSLYR3.setString(6,M_rstRSSET.getString("PS_DOCRF"));
							pstPSLYR3.addBatch();
							pstPSLYR3.executeUpdate();
						//	L_flgPSLYR2=true;
						}
						else
						{
							/*M_strSQLQRY = "UPDATE MM_PSLYR SET "
								+"PS_DOCDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
								+"WHERE PS_DOCNO = '"+strDOCNO+"' and PS_DOCTP='"+M_rstRSSET.getString("PS_DOCTP")+"' "
								+"AND PS_MATCD='"+strMATCD+"'";*/
							
							pstPSLYR2.setDate(1,sdtLOGDT);
							pstPSLYR2.setString(2,cl_dat.M_strCMPCD_pbst);
							pstPSLYR2.setString(3,strDOCNO);
							pstPSLYR2.setString(4,M_rstRSSET.getString("PS_DOCTP"));
							pstPSLYR2.setString(5,strMATCD);
							pstPSLYR2.addBatch();
							pstPSLYR2.executeUpdate();
						//	L_flgPSLYR2=true;
						}
		
						
				//		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else
					{
						System.out.println(M_strSQLQRY);
						break;
					}
					//lngCOUNT++;			//Increment In Loop counter
				}//While Loop
				
			//	System.out.println(strMATCD);
				if(cl_dat.exeDBCMT("exeSAVE"))
					setMSG("First Half Update In MM_PSLYR AND MM_YRPRC Complete..",'N');
				else
					setMSG("First Half Update In MM_PSLYR AND MM_YRPRC Fail..",'N');
			}
		//updating for last record
			//********
			double L_fltDIFVL=-((fltYOSVL + fltCRCVL+fltCSAVL) - (fltCWVRT * fltYCSQT) - (fltCISVL-fltCMRVL));
								
								if(L_fltDIFVL<-0.01f || L_fltDIFVL>0.01f)
								{
									fltYCSVL+=L_fltDIFVL;
									if(fltYCSQT>0.00001)
									{
										fltCWVRT=fltYCSVL/fltYCSQT;
									}
								//	pstADJMT.setString(1,"01");
									pstADJMT.setString(1,cl_dat.M_strCMPCD_pbst);
									pstADJMT.setString(2,L_strPRSTR);
									pstADJMT.setString(3,L_strPRMAT);
									if(L_fltDIFVL<0.0f)
										pstADJMT.setString(4,"7");
									else
										pstADJMT.setString(4,"3");
									pstADJMT.setString(5,"9999999999");
									pstADJMT.setDate(6,sdtLOGDT);
									pstADJMT.setString(7,"99999999");
									pstADJMT.setDouble(8,0.0f);
									pstADJMT.setDouble(9,0.0f);
									pstADJMT.setDouble(10,L_fltDIFVL);
									pstADJMT.setDouble(11,0.0f);
									pstADJMT.setDouble(12,0.0f);
									pstADJMT.setDate(13,sdtLOGDT);
									pstADJMT.addBatch();
									pstADJMT.executeUpdate();
								}
								pstQUERY.setDouble(1,Double.parseDouble(setNumberFormat(fltYCSQT,3)));
								pstQUERY.setDouble(2,Double.parseDouble(setNumberFormat(fltYCSVL,2)));
								pstQUERY.setDouble(3,Double.parseDouble(setNumberFormat(fltCRCQT,3)));
								pstQUERY.setDouble(4,Double.parseDouble(setNumberFormat(fltCRCVL,2)));
								pstQUERY.setDouble(5,Double.parseDouble(setNumberFormat(fltCISQT,3)));
								pstQUERY.setDouble(6,Double.parseDouble(setNumberFormat(fltCISVL,2)));
								pstQUERY.setDouble(7,Double.parseDouble(setNumberFormat(fltCMRQT,3)));
								pstQUERY.setDouble(8,Double.parseDouble(setNumberFormat(fltCMRVL,2)));
								pstQUERY.setDouble(9,Double.parseDouble(setNumberFormat(fltCSAQT,3)));
								pstQUERY.setDouble(10,Double.parseDouble(setNumberFormat(fltCSAVL,2)));
								///////// FOR STN
								pstQUERY.setDouble(11,Double.parseDouble(setNumberFormat(fltCMDQT,3)));
								pstQUERY.setDouble(12,Double.parseDouble(setNumberFormat(fltCMDVL,2)));
								pstQUERY.setDate(13,sdtLOGDT);
								pstQUERY.setDouble(14,L_fltDIFVL);
								pstQUERY.setDouble(15,Double.parseDouble(setNumberFormat(fltCWVRT,2)));
								pstQUERY.setDouble(16,Double.parseDouble(setNumberFormat(fltYCMQT,3)));
								pstQUERY.setDouble(17,Double.parseDouble(setNumberFormat(fltYCMVL,2)));
								pstQUERY.setDouble(18,Double.parseDouble(setNumberFormat(fltCMIQT,3)));
								pstQUERY.setDouble(19,Double.parseDouble(setNumberFormat(fltCMIVL,2)));
							//	pstQUERY.setString(20,strSTRTP);
							    pstQUERY.setDouble(20,Double.parseDouble(setNumberFormat(fltCSTQT,3)));
								pstQUERY.setDouble(21,Double.parseDouble(setNumberFormat(fltCSTVL,2)));
							    if(fltCSTQT >0.0)
								    System.out.println("stock transfer qty " + fltCSTQT);
								pstQUERY.setString(22,cl_dat.M_strCMPCD_pbst);
								pstQUERY.setString(23,L_strPRSTR);
								pstQUERY.setString(24,L_strPRMAT);
								pstQUERY.executeUpdate();
								cl_dat.exeDBCMT("exeSAVE");
							//	L_flgPSLYR2=false;L_flgPSLYR1=false;
								setMSG("No. of records processed : "+String.valueOf(++lngCOUNT),'N');
								cl_dat.exeDBCMT("exeSAVE");
								//Variables initialise To Zero For New Material Code
						fltCRCQT = 0;	fltCRCVL = 0;		//cum receipt qty and value
						fltCISQT = 0;	fltCISVL = 0;		//cum iss qty and value
						fltCMRQT = 0;	fltCMRVL = 0;		//cum mrn qty and value
						fltCSAQT = 0;	fltCSAVL = 0;		//cum san qty and value
						fltCSTQT = 0;	fltCSTVL = 0;		//cum san qty and value
						fltWAVRT = 0;
						fltCMDVL = 0;	fltCMDQT = 0;
						fltCMIVL = 0;	fltCMIQT = 0;
						fltYOMQT = 0;/** Year Opening Modvat Value */
						fltYOMVL = 0;/** Year Closing Modvat quanitity */
						fltYCMQT = 0;/** Year Closing Modvat Value */
						fltYCMVL = 0;/** Modvat Quantity for current issue*/
				//*************		
			//Update Record By Seting Year Opening Value As Year Closing Value And 
			//Year Opening Quantity As Year Closing Quantity Where Processing Date Is null
			setMSG("Updating For Null Date In MM_YRPRC ",'N');
			
			M_strSQLQRY = "UPDATE MM_YRPRC SET YR_YCSQT = YR_YOSQT,YR_YCSVL = YR_YOSVL "
//				+"YR_PRCDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
				+"WHERE YR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and YR_PRCDT IS NULL ";
			
			if(cl_dat.M_flgLCUPD_pbst)
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCUPD");
			
			M_strSQLQRY = "UPDATE MM_YRPRC SET YR_YCLRT=round(YR_YOSVL/YR_YOSQT,2), "
				+"YR_PRCDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
				+"WHERE YR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and YR_PRCDT IS NULL and isnull(YR_YOSQT,0)>0";
			if(cl_dat.M_flgLCUPD_pbst)
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCUPD");
			
			M_strSQLQRY = "UPDATE MM_YRPRC SET YR_YCLRT=0, "
				+"YR_PRCDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "
				+"WHERE YR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and YR_PRCDT IS NULL and isnull(YR_YOSQT,0)=0";
			if(cl_dat.M_flgLCUPD_pbst)
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCUPD");

			//			if(cl_dat.M_flgLCUPD_pbst)
//				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCUPD");
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Process Completed Successful..",'N');
				System.out.println("Processing completed");
			}
			else
				setMSG("Process Fail..",'N');
			
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
			System.out.println("Mat code "+strMATCD);
			System.out.println(M_strSQLQRY);
		}
	}
}