/*
System Name		: Materials Management System
Program Name	: Discrepancy Report order stock quatity, indent stock quantity, inspection stock quantity
Author			: Abhijit T. Chaudhari
Modified Date	: 08/09/2004
Version			: v2.0.0
*/

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR> <TR><TD>Program Name</TD><TD>P.O.&nbsp;Query</TD></TR> <TR><TD>Program Desc</TD><TD>Display                                        All the information i.e. P.O. Details,GRIN Detail and Indent Detail       for given P.O. Number And Vendor Code. </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_qrpor.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\mm_qrpor.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>20/08/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR> <TR><TD>MM_STMST</TD><TD>ST_MMSBS,ST_STRTP,ST_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_POMST</TD><TD>PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_INMST</TD><TD>IN_MMSBS,IN_STRTP,IN_INDNO,IN_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_GRMST</TD><TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO&nbsp;</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class mm_rpdes extends cl_rbase
{						/**String Value for stores tyep	 */
	String strSTRTP;	/**int value for report type	 */
	int intRPTTP;		/**date out put stream	 */
	private DataOutputStream dosREPORT;	/**file out put stream	 */
	private FileOutputStream fosREPORT;	/**constant string for report file name	 */
	private final String strFILNM="C:\\Reports\\mm_rpdes.doc";
	/**constructor set the value for string stor type, store description and report type 	 */
	public mm_rpdes(String strtp,int i)
	{
		super(2);
		M_intLINNO = 0;
		M_intPAGNO = 1;
		strSTRTP = strtp;
		intRPTTP = i;
	}
	/**Method to Generate and print report 
	 */
	public void exePRINT()
	{
		M_intLINNO = 0;
		M_intPAGNO = 1;
		try
		{
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
				
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI12);
			/**SQL Query for the the different report option 	 */
			if(intRPTTP == 0)
				M_strSQLQRY = "SELECT ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKOR,SUM(isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)- isnull(PO_FRCQT,0)) "
					+"AS ACTQT FROM MM_STMST,MM_POMST WHERE PO_STRTP = ST_STRTP AND PO_MATCD = ST_MATCD AND "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND isnull(PO_STSFL,'') NOT IN ('X','O') AND isnull(ST_STSFL,'') <> 'X' "
					+"AND isnull(PO_PORTP,'') = '01' GROUP BY ST_MATCD,ST_STKFL,ST_STKOR,ST_MATDS,ST_UOMCD "
					+"HAVING SUM(isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0)) <> ST_STKOR "
					+"ORDER BY ST_MATCD ";
			if(intRPTTP == 1)
				M_strSQLQRY = "SELECT ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKIN,SUM(isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)- isnull(IN_FCCQT,0)) "
					+"AS ACTQT FROM MM_STMST,MM_INMST WHERE IN_CMPCD = ST_CMPCD and IN_STRTP = ST_STRTP AND IN_MATCD = ST_MATCD AND "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') = '4' AND isnull(ST_STSFL,'') <> 'X' "
					+"AND isnull(IN_INDTP,'') = '01' GROUP BY ST_MATCD,ST_STKFL,ST_STKIN,ST_MATDS,ST_UOMCD "
					+"HAVING SUM(isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)) <> ST_STKIN "
					+"ORDER BY ST_MATCD ";
			if(intRPTTP == 2)
				M_strSQLQRY = "SELECT ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKIP,SUM(isnull(GR_RECQT,0) - isnull(GR_ACPQT,0)- isnull(GR_REJQT,0)) "
					+"AS ACTQT FROM MM_STMST,MM_GRMST WHERE GR_CMPCD = ST_CMPCD and GR_STRTP = ST_STRTP AND GR_MATCD = ST_MATCD AND "
					+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <> 'X' AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' AND "
					+"ST_STRTP = '"+strSTRTP+"' GROUP BY ST_MATCD,ST_STKFL,ST_STKIP,ST_MATDS,ST_UOMCD "
					+"HAVING SUM(isnull(GR_RECQT,0) - isnull(GR_ACPQT,0)- isnull(GR_REJQT,0)) <> isnull(ST_STKIP,0) "
					+"ORDER BY ST_MATCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{
				int i = 0;
				prnRPHDR();
				while(M_rstRSSET.next())
				{
					if(M_intLINNO > 60)	// Check If Line No is grater than 56 if yes then page break
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
						M_intLINNO = 0;
						M_intPAGNO += 1;
						prnFMTCHR(dosREPORT,M_strEJT);
						prnRPHDR();	//Call methhod to print report header
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),20));
					if(nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"").equals("Y"))
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),""),5));
					else
						dosREPORT.writeBytes(padSTRING('L',"",5));
					if(intRPTTP == 0)
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),""),28));
					if(intRPTTP == 1)
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),""),28));
					if(intRPTTP == 2)
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),""),28));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ACTQT"),""),28));
					M_intLINNO += 1;
					i++;
				}
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("Total Number Of Items "+i);
				prnFMTCHR(dosREPORT,M_strEJT);
				M_rstRSSET.close();
				dosREPORT.close();
				fosREPORT.close();
				doPRINT(strFILNM);
				//Process prcREPORT;
				//Runtime r = Runtime.getRuntime();
				//prcREPORT= r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
	/**Method to Print Report Header
	 */
	public void prnRPHDR()
	{
		try
		{
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,96));
			dosREPORT.writeBytes("\n");
			if(intRPTTP == 0)
				dosREPORT.writeBytes(padSTRING('R',"Descripency For Stock Order Quantity ",70));
			if(intRPTTP == 1)
				dosREPORT.writeBytes(padSTRING('R',"Descripency For Stock Indent Quantity ",70));
			if(intRPTTP == 2)
				dosREPORT.writeBytes(padSTRING('R',"Descripency For Stock Inspection Quantity",70));
			dosREPORT.writeBytes(padSTRING('R',"Date      : "+cl_dat.M_strLOGDT_pbst,26));
			dosREPORT.writeBytes("\n");
			if(strSTRTP.equals("01"))
				dosREPORT.writeBytes(padSTRING('R',"Stores Type : Engg. Stores",70));
			if(strSTRTP.equals("04"))
				dosREPORT.writeBytes(padSTRING('R',"Stores Type : Tankform",70));
			if(strSTRTP.equals("07"))
				dosREPORT.writeBytes(padSTRING('R',"Stores Type : R. M. PS",70));
			if(strSTRTP.equals("08"))
				dosREPORT.writeBytes(padSTRING('R',"Stores Type : R. M. SPS",70));
			dosREPORT.writeBytes(padSTRING('R',"Page No   : "+M_intPAGNO,26));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			if(intRPTTP == 0)
				dosREPORT.writeBytes("Material Code       STKFL             Stock Order Qty.      Actual Caluclated Qty");
			if(intRPTTP == 1)
				dosREPORT.writeBytes("Material Code       STKFL            Stock Indent Qty.      Actual Caluclated Qty");
			if(intRPTTP == 2)
				dosREPORT.writeBytes("Material Code       STKFL        Stock Inspection Qty.      Actual Caluclated Qty");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			M_intLINNO += 5;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnRPHDR");
		}
	}
}