import javax.swing.*;
import java.io.IOException;import java.io.File;import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Vector;import java.util.StringTokenizer;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.Color;import java.awt.Container;import java.awt.Toolkit;
import java.awt.Cursor;import java.awt.Component;
import java.sql.*;
import java.sql.CallableStatement;
import java.util.Map;import java.util.Calendar;import java.util.Date;import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
class hr_exmsg
{
   	
	private static Connection conSPDBA;
	private static Statement stmSPDBA;
	private static Statement stmt;
	
	private static SimpleDateFormat M_fmtDBDTM=new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	private static SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	private static SimpleDateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");	
	private static SimpleDateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");	
	private static Calendar M_calLOCAL=Calendar.getInstance();
	private static String M_strSBSCD ="";
	private static String M_strCMPCD ="01";
	public static Hashtable<String,String> hstLVEDS;
	public static Hashtable<String,Vector<String>> hstLVETD;
	public static Hashtable<String,Vector<String>> hstLVETM;
	public static Hashtable<String,Vector<String>> hstEXTPS;
	public static Vector<String> vtrMAILID;
	private static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	
	public static String strLINE = "---------------------------------------------------------------------------------------------------------------------";
	
    public static void main(String args[])
    {
        try
        {
			hstLVEDS = new Hashtable<String,String>();
			hstLVETD = new Hashtable<String,Vector<String>>();
			hstLVETM = new Hashtable<String,Vector<String>>();
			hstEXTPS = new Hashtable<String,Vector<String>>();
			vtrMAILID = new Vector<String>();
            conSPDBA=setCONDTB("01","SPLDATA","FIMS","FIMS");
            if(conSPDBA != null)
            {
                System.out.println("Connected to Database..");
            }
            if(conSPDBA == null)
            {
                System.out.println("Failed to Connect to spltest..");
                return; 
            }
            stmSPDBA = conSPDBA.createStatement();
            stmt = conSPDBA.createStatement();
			//Unregister listners of cl_pbase and register local listeners
			
			if(args.length > 0)
			{
				if(args[0].equals("H")) // Hourly Basis
					exeEXITMSG();
				else if(args[0].equals("D")) // Daily Basis
					exeOVRAL_MSG();
			}
	        System.exit(0);
        }
        catch(Exception L_E)
        {
            System.out.println(L_E.toString());
        }
        
    }
    private static Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
    {
		Connection LM_CONDTB=null;
		try
		{
			String L_STRURL = "", L_STRDRV = "";
			if(LP_SYSLC.equals("01"))
			{
				L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
				L_STRURL = "jdbc:as400://SPLWS01/";
				Class.forName(L_STRDRV);
			}
			L_STRURL = L_STRURL + LP_DTBLB;
			LM_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);
            System.out.println("connected "+LM_CONDTB);
			if(LM_CONDTB == null)
				return null;
			LM_CONDTB.setAutoCommit(false);

			SQLWarning L_STRWRN = LM_CONDTB.getWarnings();
			return LM_CONDTB;
		}
		catch(java.lang.Exception L_EX)
		{
			System.out.println("Error");
			System.out.println("Error while connecting to DB : "+L_EX);
			return null;
		}
	}
  
	
	
	public  static void exeEXITMSG()
    {
		try
        {
			String M_strSQLQRY = "select SWT_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,min(SWT_OUTTM) SWT_OUTTM,min(ex_docno) EX_DOCNO,ex_remds, substr(cmt_codcd,6,4),b.ep_emlrf EP_EMLRF from HR_SWTRN,hr_extrn,hr_epmst a left outer join co_cdtrn on cmt_cgstp='HR"+M_strCMPCD+"LSN' and substr(cmt_codcd,1,4)=swt_empno left outer join hr_epmst b on b.ep_cmpcd='"+M_strCMPCD+"' and b.ep_empno=substr(cmt_codcd,6,4) where  swt_cmpcd='"+M_strCMPCD+"' and swt_wrkdt = current_date and swt_outtm is not null and ex_cmpcd=swt_cmpcd and ex_empno = swt_empno and ex_docdt = swt_wrkdt  and ex_outtm is null  and  swt_outtm >= ex_gentm and swt_cmpcd=a.ep_cmpcd and swt_empno=a.ep_empno  group by swt_empno,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.',substr(cmt_codcd,6,4),ex_remds,b.ep_emlrf order by SWT_EMPNO";
			System.out.println(M_strSQLQRY);
			ResultSet rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String L_strOUTTM="", L_strINCTM="", L_strEXTTM="";
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					if(rstRSSET.getString("EP_EMLRF").length()>0)
					{
						L_strOUTTM = M_fmtLCDTM.format(rstRSSET.getTimestamp("SWT_OUTTM"));
						String strEXITMSG = rstRSSET.getString("EP_EMPNM")+" punched out at "+L_strOUTTM.substring(11)+" Hrs. for Reason : "+rstRSSET.getString("EX_REMDS");
						cl_eml ocl_eml = new cl_eml();
						System.out.println("Sending Message to "+rstRSSET.getString("EP_EMLRF")+strEXITMSG);
						ocl_eml.sendfile_ext(rstRSSET.getString("EP_EMLRF"),null,strEXITMSG,"");
						//ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,"Sending Message to "+rstRSSET.getString("EP_EMLRF")+strEXITMSG,"");
						String M_strSQLQRY1 = "update hr_extrn set ex_outtm='"+M_fmtDBDTM.format(rstRSSET.getTimestamp("SWT_OUTTM"))+"' where ex_cmpcd = '"+M_strCMPCD+"' and ex_empno='"+rstRSSET.getString("SWT_EMPNO")+"' and ex_docno = '"+rstRSSET.getString("EX_DOCNO")+"'";
						System.out.println(M_strSQLQRY1);
                        if(stmSPDBA.executeUpdate(M_strSQLQRY1) >0)
                		{
							stmSPDBA.executeUpdate(M_strSQLQRY1);
                	    	conSPDBA.commit();
						}
					}
				}
				rstRSSET.close();
			}
			M_strSQLQRY = "select SWT_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,min(SWT_INCTM) SWT_INCTM,min(ex_docno) EX_DOCNO,ex_outtm, substr(cmt_codcd,6,4),b.ep_emlrf EP_EMLRF from HR_SWTRN,hr_extrn,hr_epmst a left outer join co_cdtrn on cmt_cgstp='HR"+M_strCMPCD+"LSN' and substr(cmt_codcd,1,4)=swt_empno left outer join hr_epmst b on b.ep_cmpcd='"+M_strCMPCD+"' and b.ep_empno=substr(cmt_codcd,6,4) where  swt_cmpcd='"+M_strCMPCD+"' and swt_wrkdt = current_date and swt_inctm is not null and  ex_cmpcd=swt_cmpcd and ex_empno = swt_empno and ex_docdt = swt_wrkdt  and ex_outtm is not null and ex_inctm is null and swt_inctm > ex_outtm and swt_cmpcd=a.ep_cmpcd and swt_empno=a.ep_empno  group by swt_empno,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.',substr(cmt_codcd,6,4),ex_outtm,b.ep_emlrf order by SWT_EMPNO";
			System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			if(rstRSSET!=null)
			{
				 while(rstRSSET.next())
				 {
					if(rstRSSET.getString("EP_EMLRF").length()>0)
					{
						L_strINCTM = M_fmtLCDTM.format(rstRSSET.getTimestamp("SWT_INCTM"));
						L_strOUTTM = M_fmtLCDTM.format(rstRSSET.getTimestamp("EX_OUTTM"));
						L_strEXTTM = calTIME(L_strINCTM,L_strOUTTM);
						String strEXITMSG = rstRSSET.getString("EP_EMPNM")+" Returned at: "+L_strINCTM.substring(11)+" Hrs. ... Exit period: "+L_strEXTTM+" Hrs.";
						cl_eml ocl_eml = new cl_eml();
						System.out.println("Sending Message to "+rstRSSET.getString("EP_EMLRF")+strEXITMSG);
						ocl_eml.setFRADR("systems_works@spl.co.in");
						ocl_eml.sendfile_ext(rstRSSET.getString("EP_EMLRF"),null,strEXITMSG,"");
						//ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,"Sending Message to "+rstRSSET.getString("EP_EMLRF")+strEXITMSG,"");
						
						String M_strSQLQRY1 = "update hr_extrn set ex_inctm='"+M_fmtDBDTM.format(rstRSSET.getTimestamp("SWT_INCTM"))+"' where ex_cmpcd = '"+M_strCMPCD+"' and ex_empno='"+rstRSSET.getString("SWT_EMPNO")+"' and ex_docno = '"+rstRSSET.getString("EX_DOCNO")+"'";
						System.out.println(M_strSQLQRY1);
                        if(stmSPDBA.executeUpdate(M_strSQLQRY1) >0)
                		{
							stmSPDBA.executeUpdate(M_strSQLQRY1);
                	    	conSPDBA.commit();
						}
					}
				 }
				rstRSSET.close();
			}
	    }
        catch(Exception L_EX)
        {
			System.out.println("Error in setEXITMSG"+L_EX);
		}
	}
   
	
	public  static void exeOVRAL_MSG()
    {
		try
        {
			/*hr_teexr ohr_teexr = new hr_teexr("a");
			Date datTEMP = new Date();
			Calendar M_calLOCAL = Calendar.getInstance();
			M_calLOCAL.setTime(datTEMP);      
			M_calLOCAL.add(Calendar.DATE,-1);    
			datTEMP = M_calLOCAL.getTime();
			ohr_teexr.prcOVTDT1(M_fmtLCDAT.format(datTEMP),M_fmtLCDAT.format(datTEMP),"01");
			boolean flg=true;
			if(flg)
				return;
			*/
			
			hstLVETD.clear();
			hstLVETM.clear();
			hstEXTPS.clear();
			hstLVEDS.clear();
			vtrMAILID.clear();
			String M_strSQLQRY = "";
			String strMSG="";
			ResultSet rstRSSET;
			
			M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='STS' and cmt_cgstp = 'HRXXLVT'";
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					if(!hstLVEDS.containsKey(rstRSSET.getString("cmt_codcd")))
						hstLVEDS.put(rstRSSET.getString("cmt_codcd"),rstRSSET.getString("cmt_codds"));
				}
				rstRSSET.close();
			}
			
			///Employees On leave Today
			M_strSQLQRY = " select lvt_empno,ifnull(a.EP_FSTNM,' ')||' '||substr(ifnull(a.EP_MDLNM,' '),1,1)||'. '||ifnull(a.EP_LSTNM,' ') EP_EMPNM,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_stsfl,lvt_rsnds,min(lvt_lvedt) min_lvedt,max(lvt_lvedt) max_lvedt,days(max(lvt_lvedt))-days(min(lvt_lvedt)) TOTAL,b.ep_emlrf EP_EMLRF";
			M_strSQLQRY+= " from hr_lvtrn,hr_epmst a  left outer join co_cdtrn on cmt_cgstp in ('HR01LRC','HR01LSN') and substr(cmt_codcd,1,4)=lvt_empno and substr(cmt_codcd,6,4)<>lvt_empno left outer join hr_epmst b on b.ep_empno=substr(cmt_codcd,6,4)";
			M_strSQLQRY+= " where lvt_cmpcd='01' and lvt_cmpcd= a.ep_cmpcd and lvt_empno=a.ep_empno and  a.ep_lftdt is null and lvt_empno || char(lvt_refdt) in (select lvt_empno || char(lvt_refdt) from hr_lvtrn where lvt_cmpcd='01'  and LVT_LVECD <> 'PE' and date(lvt_lvedt)= date(days(current_date)+0))";
			M_strSQLQRY+= " group by lvt_empno,a.ep_fstnm,a.ep_mdlnm,a.ep_lstnm,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_rsnds,b.ep_emlrf";
			M_strSQLQRY+= " order by EP_DPTNM,lvt_EMPNO";
			System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String L_strLVETD = "";
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					if(rstRSSET.getString("EP_EMLRF") != null && rstRSSET.getString("EP_EMLRF").length()>0)
					{
						Vector<String> L_vtrLVETD = new Vector<String>();
						L_strLVETD = setSTRING(rstRSSET.getString("EP_EMPNM"),30)+"  "+rstRSSET.getString("LVT_LVECD")+"  "+(rstRSSET.getInt("TOTAL")+1)+" day(s)  "+M_fmtLCDAT.format(rstRSSET.getDate("MIN_LVEDT"))+(rstRSSET.getString("MIN_LVEDT").equals(rstRSSET.getString("MAX_LVEDT"))?"           ":"-"+M_fmtLCDTM.format(rstRSSET.getDate("MAX_LVEDT")))+"  "+rstRSSET.getString("LVT_RSNDS")+" ("+hstLVEDS.get(rstRSSET.getString("LVT_STSFL"))+")";
						//L_strLVETD = setSTRING(rstRSSET.getString("EP_EMPNM"),30)+"  "+rstRSSET.getString("LVT_LVECD")+"  "+(rstRSSET.getInt("TOTAL")+1)+" day(s)  "+M_fmtLCDAT.format(rstRSSET.getDate("MIN_LVEDT"))+(rstRSSET.getString("MIN_LVEDT").equals(rstRSSET.getString("MAX_LVEDT"))?"           ":"-"+M_fmtLCDTM.format(rstRSSET.getDate("MAX_LVEDT")))+"  "+setSTRING(hstLVEDS.get(rstRSSET.getString("LVT_STSFL")),12)+"  "+rstRSSET.getString("LVT_RSNDS");
						if(hstLVETD.containsKey(rstRSSET.getString("EP_EMLRF")))
							L_vtrLVETD = hstLVETD.get(rstRSSET.getString("EP_EMLRF"));
						L_vtrLVETD.add(L_strLVETD);
						
						hstLVETD.put(rstRSSET.getString("EP_EMLRF"),L_vtrLVETD);
						if(!vtrMAILID.contains(rstRSSET.getString("EP_EMLRF")))
							vtrMAILID.add(rstRSSET.getString("EP_EMLRF"));
					}
				}
				rstRSSET.close();
			}
			
			///Employees On leave Tomorrow
			M_strSQLQRY = " select lvt_empno,ifnull(a.EP_FSTNM,' ')||' '||substr(ifnull(a.EP_MDLNM,' '),1,1)||'. '||ifnull(a.EP_LSTNM,' ') EP_EMPNM,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_stsfl,lvt_rsnds,min(lvt_lvedt) min_lvedt,max(lvt_lvedt) max_lvedt,days(max(lvt_lvedt))-days(min(lvt_lvedt)) TOTAL,b.ep_emlrf EP_EMLRF";
			M_strSQLQRY+= " from hr_lvtrn,hr_epmst a  left outer join co_cdtrn on cmt_cgstp in ('HR01LRC','HR01LSN') and substr(cmt_codcd,1,4)=lvt_empno and substr(cmt_codcd,6,4)<>lvt_empno left outer join hr_epmst b on b.ep_empno=substr(cmt_codcd,6,4)";
			M_strSQLQRY+= " where lvt_cmpcd='01' and lvt_cmpcd= a.ep_cmpcd and lvt_empno=a.ep_empno and  a.ep_lftdt is null and lvt_empno || char(lvt_refdt) in (select lvt_empno || char(lvt_refdt) from hr_lvtrn where lvt_cmpcd='01'  and LVT_LVECD <> 'PE' and date(lvt_lvedt)= date(days(current_date)+1))";
			M_strSQLQRY+= " group by lvt_empno,a.ep_fstnm,a.ep_mdlnm,a.ep_lstnm,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_rsnds,b.ep_emlrf";
			M_strSQLQRY+= " order by EP_DPTNM,lvt_EMPNO";
			System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String L_strLVETM = "";
			
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					if(rstRSSET.getString("EP_EMLRF") != null && rstRSSET.getString("EP_EMLRF").length()>0)
					{
						Vector<String> L_vtrLVETM = new Vector<String>();
						L_strLVETM = setSTRING(rstRSSET.getString("EP_EMPNM"),30)+"  "+rstRSSET.getString("LVT_LVECD")+"  "+(rstRSSET.getInt("TOTAL")+1)+" day(s)  "+M_fmtLCDAT.format(rstRSSET.getDate("MIN_LVEDT"))+(rstRSSET.getString("MIN_LVEDT").equals(rstRSSET.getString("MAX_LVEDT"))?"           ":"-"+M_fmtLCDTM.format(rstRSSET.getDate("MAX_LVEDT")))+"  "+rstRSSET.getString("LVT_RSNDS")+" ("+hstLVEDS.get(rstRSSET.getString("LVT_STSFL"))+")";
						if(hstLVETM.containsKey(rstRSSET.getString("EP_EMLRF")))
							L_vtrLVETM = hstLVETM.get(rstRSSET.getString("EP_EMLRF"));
						L_vtrLVETM.add(L_strLVETM);
						hstLVETM.put(rstRSSET.getString("EP_EMLRF"),L_vtrLVETM);

						if(!vtrMAILID.contains(rstRSSET.getString("EP_EMLRF")))
							vtrMAILID.add(rstRSSET.getString("EP_EMLRF"));
					}
				}
				rstRSSET.close();
			}
			
			///Yesterdays Exit Passes
			M_strSQLQRY = " select distinct EX_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,EX_OUTTM,EX_INCTM, EX_DOCNO,ex_remds, b.ep_emlrf EP_EMLRF";
			M_strSQLQRY+= " from hr_extrn,hr_epmst a left outer join co_cdtrn on cmt_cgstp in ('HR01LSN','HR01LRC') and substr(cmt_codcd,1,4)=ex_empno and substr(cmt_codcd,6,4)<>ex_empno left outer join hr_epmst b on b.ep_cmpcd='01' and b.ep_empno=substr(cmt_codcd,6,4)";
			M_strSQLQRY+= " where  ex_cmpcd='01' and ex_docdt = date(days(current_date)-1) and ex_cmpcd=a.ep_cmpcd and ex_empno=a.ep_empno";
			M_strSQLQRY+= " order by b.ep_emlrf,ex_EMPNO";
			System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String L_strEXTPS = "";
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					if(rstRSSET.getString("EP_EMLRF") != null && rstRSSET.getString("EP_EMLRF").length()>0)
					{
						Vector<String> L_vtrEXTPS = new Vector<String>();
						L_strEXTPS = setSTRING(rstRSSET.getString("EP_EMPNM"),30)+"  "+(rstRSSET.getTimestamp("EX_OUTTM")==null?"-               ":M_fmtLCDTM.format(rstRSSET.getTimestamp("EX_OUTTM")))+"  "+(rstRSSET.getTimestamp("EX_INCTM")==null?"-               ":M_fmtLCDTM.format(rstRSSET.getTimestamp("EX_INCTM")))+"  "+rstRSSET.getString("EX_REMDS");
						if(hstEXTPS.containsKey(rstRSSET.getString("EP_EMLRF")))
							L_vtrEXTPS = hstEXTPS.get(rstRSSET.getString("EP_EMLRF"));
						L_vtrEXTPS.add(L_strEXTPS);
						hstEXTPS.put(rstRSSET.getString("EP_EMLRF"),L_vtrEXTPS);
						
						if(!vtrMAILID.contains(rstRSSET.getString("EP_EMLRF")))
							vtrMAILID.add(rstRSSET.getString("EP_EMLRF"));
					}
				}
				rstRSSET.close();
			}
			
			cl_eml ocl_eml = new cl_eml();
			for(int i=0;i<vtrMAILID.size();i++)
			{
				//strMSG += vtrMAILID.get(i)+"\n";
				if(hstLVETD.containsKey(vtrMAILID.get(i)))
				{
					strMSG += "Today's Leave Status : \n";
					strMSG = setSTRMSG(strMSG,vtrMAILID.get(i),hstLVETD);
				}

				if(hstLVETM.containsKey(vtrMAILID.get(i)))
				{
					strMSG += "\n\nTommorow's Leave Status : \n";
					strMSG = setSTRMSG(strMSG,vtrMAILID.get(i),hstLVETM);
				}
				
				if(hstEXTPS.containsKey(vtrMAILID.get(i)))
				{
					strMSG += "\n\nExit Pass detail (Yesterday) : \n";
					strMSG = setSTRMSG(strMSG,vtrMAILID.get(i),hstEXTPS);
				}
				//JOptionPane.showMessageDialog(null,strMSG,"Time Descripnacy...",JOptionPane.INFORMATION_MESSAGE);
				ocl_eml.setFRADR("systems_works@spl.co.in");
				ocl_eml.sendfile_ext(vtrMAILID.get(i),null,"Attendance Intimation",strMSG);
				//ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,"Attendance Intimation to "+vtrMAILID.get(i),strMSG);
				strMSG="";
			}
	    }
        catch(Exception L_EX)
        {
			System.out.println("Error in exeOVRAL_MSG() : "+L_EX);
		}
	}
   
	public static String setSTRMSG(String LP_STR,String LP_MAILID,Hashtable<String,Vector<String>> LP_HSTBL)
	{
		Vector<String> L_vtrTEMP = LP_HSTBL.get(LP_MAILID); 
		LP_STR += strLINE+"\n";
		for(int j=0;j<L_vtrTEMP.size();j++)
		{
			LP_STR += L_vtrTEMP.get(j)+"\n";
		}
		LP_STR += strLINE+"\n";
		return LP_STR;
	}
	
	public static String setSTRING(String LP_STR,int LP_LEN)
	{
		if(LP_STR.length()>LP_LEN)
			LP_STR.substring(0,LP_LEN-1);
		else if(LP_STR.length()<LP_LEN)
		{
			for(int i=LP_STR.length();i<LP_LEN+1;i++)
				LP_STR+=" ";
		}
		return LP_STR;
	}


	/**
    Method to Calculate the differance two Date & Time.
	@param P_strFINTM String argument to Final Time.
	@param P_strINITM String argument to Initial Time.
    */
	private static String calTIME(String P_strFINTM,String P_strINITM)
	{
		String L_strDIFTM = "";
		try
		{
			int L_intYRS,L_intMTH,L_intDAY,L_intHRS,L_intMIN;
			int L_intYRS1,L_intMTH1,L_intDAY1,L_intHRS1,L_intMIN1;
			int L_intYRS2,L_intMTH2,L_intDAY2,L_intHRS2,L_intMIN2;
			String L_strHOUR,L_strMINT;			
			if(P_strFINTM.equals("") || P_strINITM.equals(""))
				return L_strDIFTM;			
			// Seperating year,month,day,hour & minute from Final time
			L_intYRS1 = Integer.parseInt(P_strFINTM.substring(6,10));
			L_intMTH1 = Integer.parseInt(P_strFINTM.substring(3,5));
			L_intDAY1 = Integer.parseInt(P_strFINTM.substring(0,2));
			L_intHRS1 = Integer.parseInt(P_strFINTM.substring(11,13));
			L_intMIN1 = Integer.parseInt(P_strFINTM.substring(14));			
			// Seperating year,month,day,hour & minute from Initial time
			L_intYRS2 = Integer.parseInt(P_strINITM.substring(6,10));
			L_intMTH2 = Integer.parseInt(P_strINITM.substring(3,5));
			L_intDAY2 = Integer.parseInt(P_strINITM.substring(0,2));
			L_intHRS2 = Integer.parseInt(P_strINITM.substring(11,13));
			L_intMIN2 = Integer.parseInt(P_strINITM.substring(14));			
			L_intMIN = L_intMIN1 - L_intMIN2;
			L_intHRS = L_intHRS1 - L_intHRS2;			
			// Checking for leap year
		if(L_intYRS1%4 == 0)
				arrDAYS[1] = "29";
			else
				arrDAYS[1] = "28";			
			// Final date is of next month
			if(L_intMTH1 > L_intMTH2)
			{
				for(int i = L_intMTH2;i < L_intMTH1;i++)
					L_intDAY1 += Integer.parseInt(arrDAYS[i-1]);
			}			
			L_intDAY = L_intDAY1 - L_intDAY2;			
			if(L_intMIN < 0)
			{
				L_intMIN += 60;
				L_intHRS--;
			}
			if(L_intHRS < 0)
			{
				L_intHRS += 24;
				L_intDAY--;
			}
			if(L_intDAY > 0)
				L_intHRS += L_intDAY*24;			
			L_strHOUR = String.valueOf(L_intHRS);
			L_strMINT = String.valueOf(L_intMIN);
			if(L_strHOUR.length() < 2)
				L_strHOUR = "0" + L_strHOUR;
			if(L_strMINT.length() < 2)
				L_strMINT = "0" + L_strMINT;
			L_strDIFTM = L_strHOUR + ":" + L_strMINT;
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX+" calTIME");
		}	
		return L_strDIFTM;
	}	


}

