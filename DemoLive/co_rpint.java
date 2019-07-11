import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;

class co_rpint extends cl_rbase 
{
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	//private String strRPFNM = strRPLOC + "co_rpint.html";
	private String strRPFNM = strRPLOC + "co_rpint.doc";
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	int intCNT = 0;
	private String strLINE = "---------------------------------------------------------------------------------------------------------------------";
	private String[][] staINTRN;
		
	co_rpint(String LP_EMPNO,String LP_LOGDT)		/*  Constructor   */
	{
		super(2);
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			genRPTFL(LP_EMPNO,LP_LOGDT);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	void genRPTFL(String LP_EMPNO,String LP_LOGDT)
	{
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			staINTRN = new String[5000][3];
			
			M_strSQLQRY =" select cmt_chp01 from co_cdtrn";
			M_strSQLQRY+=" WHERE CMT_CGMTP ='S"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='HRXXDTR' AND CMT_CODCD ='02'";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET!=null && M_rstRSSET.next())
				D_OUT.writeBytes("REPORT AS ON : "+M_rstRSSET.getString("CMT_CHP01")+"\n\n");

			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			M_strSQLQRY =" select * from co_intrn";
			M_strSQLQRY+=" where in_empno='"+LP_EMPNO+"' and in_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and in_msgdt='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_LOGDT))+"'";
			M_strSQLQRY+=" order by in_msmtp,in_msstp,in_msgds";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					staINTRN[intCNT][0]=M_rstRSSET.getString("IN_MSMTP");
					staINTRN[intCNT][1]=M_rstRSSET.getString("IN_MSSTP");
					staINTRN[intCNT][2]=M_rstRSSET.getString("IN_MSGDS");
					intCNT++;
				}
				M_rstRSSET.close();
			}
			
			if(chkDATA("LV","TD"))
			{
				D_OUT.writeBytes("Today's Leave Status : \n");
				dspDATA("LV","TD");
			}
			if(chkDATA("LV","TM"))
			{
				D_OUT.writeBytes("\n\nTommorow's Leave Status : \n");
				dspDATA("LV","TM");
			}
			if(chkDATA("VS","YD"))
			{
				D_OUT.writeBytes("\n\nVisitors detail (Yesterday) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("VISITOR NAME         ORGANIZATION    PURPOSE                        VISITING PERSON      IN    OUT   No.Person\n");
				dspDATA("VS","YD");
			}
			if(chkDATA("EP","YD"))
			{
				D_OUT.writeBytes("\n\nExit Pass detail (Yesterday) : \n");
				dspDATA("EP","YD");
			}
			if(chkDATA("GP","GI"))
			{
				D_OUT.writeBytes("\n\nMaterial pending for inspection (for last quarter) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("GRN No.   GRN date    Mat. code   Mat. Desc                      Rec.Qty UOM PO Ref    Status\n");
				dspDATA("GP","GI");
			}
			if(chkDATA("GP","GR"))
			{
				D_OUT.writeBytes("\n\nMaterial to be received against Returnable Gate Pass (for last quarter) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("MGP No.   MGP date    Mat. code   Mat. Desc                    Iss.Qty  UOM Due Date    Status\n");
				dspDATA("GP","GR");
			}
			if(chkDATA("IN","PR"))
			{
				D_OUT.writeBytes("\n\nOrder generated material to be received (for last quarter) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("Order No.  Date       Mat.Code    Mat. Description               Qty.   UOM Indent Ref.   Status\n");
				dspDATA("IN","PR");
			}
			if(chkDATA("IN","PO"))
			{
				D_OUT.writeBytes("\n\nIndent authorised Order to be generated (for last quarter) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("Indent No. Ind. Date  Mat.Code    Mat. Description               Qty.   UOM By   Status\n");
				dspDATA("IN","PO");
			}
			if(chkDATA("IN","PA"))
			{
				D_OUT.writeBytes("\n\nMaterial indented pending for Authorisation (for last quarter) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("Indent No. Ind. Date  Mat.Code    Mat. Description               Qty.   UOM By   Status\n");
				dspDATA("IN","PA");
			}
			D_OUT.writeBytes("\n\n================================================================================================\n");
			
			if(chkDATA("IN","OR"))
			{
				D_OUT.writeBytes("\n\nOrder generated material to be received (Old Records) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("Order No.  Date       Mat.Code    Mat. Description               Qty.   UOM Indent Ref.   Status\n");
				dspDATA("IN","OR");
			}
			if(chkDATA("IN","OO"))
			{
				D_OUT.writeBytes("\n\nIndent authorised Order to be generated (Old Records) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("Indent No. Ind. Date  Mat.Code    Mat. Description               Qty.   UOM By   Status\n");
				dspDATA("IN","OO");
			}
			if(chkDATA("IN","OA"))
			{
				D_OUT.writeBytes("\n\nMaterial indented pending for Authorisation (Old Records) : \n");
				D_OUT.writeBytes(strLINE+"\n");
				D_OUT.writeBytes("Indent No. Ind. Date  Mat.Code    Mat. Description               Qty.   UOM By   Status\n");
				dspDATA("IN","OA");
			}

			D_OUT.close();
			F_OUT.close();

    		Runtime r = Runtime.getRuntime();
		 	Process p = null;
		    //p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"co_rpint : genRPTFL()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	void dspDATA(String LP_MSMTP,String LP_MSSTP)
	{
		try
		{
			D_OUT.writeBytes(strLINE+"\n");
			for(int i=0;i<intCNT;i++)
			{
				if(staINTRN[i][0].equals(LP_MSMTP) && staINTRN[i][1].equals(LP_MSSTP))
				{
					D_OUT.writeBytes(staINTRN[i][2]+"\n");
				}
			}
			D_OUT.writeBytes(strLINE+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"co_rpint : dspDATA()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}
	
	public boolean chkDATA(String LP_MSMTP,String LP_MSSTP)
	{
		try
		{
			for(int i=0;i<intCNT;i++)
			{
				if(staINTRN[i][0].equals(LP_MSMTP) && staINTRN[i][1].equals(LP_MSSTP))
				{
					return true;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"co_rpint : dspDATA()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
		return false;
	}
}



