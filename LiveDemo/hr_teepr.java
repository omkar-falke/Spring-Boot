import javax.swing.*;
import java.io.IOException;import java.io.File;import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Vector;import java.util.StringTokenizer;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.Color;import java.awt.Container;import java.awt.Toolkit;
import java.awt.Cursor;import java.awt.Component;
import java.sql.*;
import java.sql.CallableStatement;
import java.util.Map;import java.util.Date;import java.util.Calendar;import java.text.SimpleDateFormat;

class hr_teepr extends JFrame implements ActionListener
{
   	private File file;
	private RandomAccessFile file_io;
	private JButton btnPROCS,btnSTOP,btnEXIT;
	private JLabel lblMESG;
	private static JTextField txtSTRDT,txtENDDT;
	private String strSTRDT;
	private String strENDDT;
	private boolean FLAG = true;
	private static CallableStatement cstEMPCT;
	private cl_pbase ocl_pbase;
	private static Connection conSPDBA;
	private static Statement stmSPDBA;
	private static String strDATE;
	private static SimpleDateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");	/**	For Timestamp in DB format "yyyy-MM-dd-HH.mm" */
	private static SimpleDateFormat M_fmtDBDTM=new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	private static SimpleDateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");	/**	For Timestamp in Locale format "dd/MM/yyyy HH:mm" */
	private static SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	private static SimpleDateFormat M_fmtHHMM=new SimpleDateFormat("HH:mm");
	private static Calendar M_calLOCAL=Calendar.getInstance();
	private static String M_strSBSCD ="";
	public static Hashtable<String,String> hstSTSDS;
	//public static Hashtable<String,Vector<String>> hstLVETD;
	//public static Hashtable<String,Vector<String>> hstLVETM;
	//public static Hashtable<String,Vector<String>> hstEXTPS;
	//public static Hashtable<String,Vector<String>> hstVSTDT;
	//public static Vector<String> vtrMAILID;
	private static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	
	//public static String strLINE = "---------------------------------------------------------------------------------------------------------------------";
	private static Statement stmt;
	private JCheckBox chkENTDT;
	private static String M_strCMPCD ="01";
	private static String strPRGTP = "";
    hr_teepr()
    {
        super("Head count updating");
        try
        {
            cl_dat.M_dimSCRN_pbst = Toolkit.getDefaultToolkit().getScreenSize();
			cl_dat.M_dblWIDTH=(cl_dat.M_dimSCRN_pbst.width/800.00);
			cl_dat.M_dblHIGHT=((cl_dat.M_dimSCRN_pbst.height)/600.00);
            ocl_pbase = new cl_pbase();
            ocl_pbase.setMatrix(20,8);
            ocl_pbase.add(new JLabel("Start Date"),3,1,1,1,ocl_pbase,'L');
            ocl_pbase.add(txtSTRDT = new TxtDate(),3,2,1,1,ocl_pbase,'L');
            
			ocl_pbase.add(new JLabel("End Date"),3,3,1,1,ocl_pbase,'L');
            ocl_pbase.add(txtENDDT = new TxtDate(),3,4,1,1,ocl_pbase,'L');
			
			ocl_pbase.add(chkENTDT= new JCheckBox("Process by Entering Date",false),1,1,1,4,this,'L');
			ocl_pbase.add(btnPROCS = new JButton("Process"),4,1,1,1,ocl_pbase,'L');
            ocl_pbase.add(btnSTOP = new JButton("STOP"),4,2,1,1,ocl_pbase,'L');
            ocl_pbase.add(btnEXIT = new JButton("EXIT"),4,3,1,1,ocl_pbase,'L');
            ocl_pbase.add(lblMESG = new JLabel(" "),5,1,1,8,ocl_pbase,'L');
						
            Container L_ctrTHIS=getContentPane();
			
			hstSTSDS = new Hashtable<String,String>();
			//hstLVETD = new Hashtable<String,Vector<String>>();
			//hstLVETM = new Hashtable<String,Vector<String>>();
			//hstEXTPS = new Hashtable<String,Vector<String>>();
			//hstVSTDT = new Hashtable<String,Vector<String>>();
			//vtrMAILID = new Vector<String>();
			
			M_fmtDBDAT.setLenient(false);
			M_fmtDBDTM.setLenient(false);
			M_fmtLCDAT.setLenient(false);
			M_fmtLCDTM.setLenient(false);
			M_calLOCAL.setLenient(false);
			L_ctrTHIS.add(ocl_pbase);
		    setSize(800,200);
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Component L_cmpTEMP=null;
			//Unregister listners of cl_pbase and register local listeners
			for(int i=0;i<ocl_pbase.M_vtrSCCOMP.size();i++)
			{
				L_cmpTEMP=(Component)ocl_pbase.M_vtrSCCOMP.elementAt(i);
				L_cmpTEMP.removeKeyListener(ocl_pbase);
				if(L_cmpTEMP instanceof JTextField)
				{
					((JTextField)L_cmpTEMP).removeActionListener(ocl_pbase);
					((JTextField)L_cmpTEMP).removeFocusListener(ocl_pbase);
				}
				else if(L_cmpTEMP instanceof AbstractButton)
				{
					((AbstractButton)L_cmpTEMP).removeActionListener(ocl_pbase);
					((AbstractButton)L_cmpTEMP).removeFocusListener(ocl_pbase);
				}
				else if(L_cmpTEMP instanceof JComboBox)
				{
					((JComboBox)L_cmpTEMP).removeActionListener(ocl_pbase);
					L_cmpTEMP.removeFocusListener(ocl_pbase);
				}
			}
			btnPROCS.addActionListener(this);btnSTOP.addActionListener(this);btnEXIT.addActionListener(this);
			L_cmpTEMP=null;
			
			conSPDBA=setCONDTB("01","SPLDATA","FIMS","FIMS");
            if(conSPDBA != null)
            {
                setMSG("Connected to Database..",'N');
            }
            if(conSPDBA == null)
            {
                setMSG("Failed to Connect to Database..",'E');
                return; 
            }
			
			
			Date datTEMP = new Date();
			Calendar M_calLOCAL = Calendar.getInstance();
			M_calLOCAL.setTime(datTEMP);      
			M_calLOCAL.add(Calendar.DATE,-1);    
			datTEMP = M_calLOCAL.getTime();
			//L_strTEMP = L_rstRSSET.getString("CMT_CHP01");
			strSTRDT = M_fmtLCDAT.format(datTEMP);
			strENDDT = M_fmtLCDAT.format(datTEMP);
			txtSTRDT.setText(strSTRDT);
			txtENDDT.setText(strENDDT);

			stmt = conSPDBA.createStatement();
			stmSPDBA = 	conSPDBA.createStatement();
			if(!chkENTDT.isSelected())
			{
				if(strPRGTP.equals("H"))
					exeEXITMSG();
				else if(strPRGTP.equals("D"))
				{
					hr_teexr ohr_teexr=new hr_teexr(conSPDBA,strSTRDT);
					System.out.println("calling hr_teepr");
					ohr_teexr.prcDATA(txtSTRDT.getText(),txtENDDT.getText(),"","","");
					System.out.println("called hr_teepr");
					exeOVRAL_MSG();
				}
			}
		}
	    catch(Exception E)
		{
            System.out.println("inside constructor()"+E);
        }

    }

	public  static void exeEXITMSG()
    {
		try
        {
			String M_strSQLQRY = "select SWT_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,min(SWT_OUTTM) SWT_OUTTM,min(ex_docno) EX_DOCNO,ex_remds, substr(cmt_codcd,6,4),b.ep_emlrf EP_EMLRF from hr_extrn,hr_epmst a,HR_SWTRN left outer join co_cdtrn on cmt_cgstp='HR"+M_strCMPCD+"LSN' and substr(cmt_codcd,1,4)=swt_empno left outer join hr_epmst b on b.ep_cmpcd='"+M_strCMPCD+"' and b.ep_empno=substr(cmt_codcd,6,4) where  swt_cmpcd='"+M_strCMPCD+"' and swt_wrkdt = current_date and swt_outtm is not null and ex_cmpcd=swt_cmpcd and ex_empno = swt_empno and ex_docdt = swt_wrkdt  and ex_outtm is null  and  swt_outtm >= ex_gentm and swt_cmpcd=a.ep_cmpcd and swt_empno=a.ep_empno  group by swt_empno,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.',substr(cmt_codcd,6,4),ex_remds,b.ep_emlrf order by SWT_EMPNO";
			//System.out.println(M_strSQLQRY);
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
						//String strEXITMSG = rstRSSET.getString("EP_EMPNM")+" punched out at "+M_fmtLCDTM.format(rstRSSET.getTimestamp("SWT_OUTTM"))+" Hrs.  for Reason : "+rstRSSET.getString("EX_REMDS");
						cl_eml ocl_eml = new cl_eml();
						//System.out.println(strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")");
						ocl_eml.sendfile_ext(rstRSSET.getString("EP_EMLRF"),null,strEXITMSG,"");
						//ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")","");
						String M_strSQLQRY1 = "update hr_extrn set ex_outtm='"+M_fmtDBDTM.format(rstRSSET.getTimestamp("SWT_OUTTM"))+"' where ex_cmpcd = '"+M_strCMPCD+"' and ex_empno='"+rstRSSET.getString("SWT_EMPNO")+"' and ex_docno = '"+rstRSSET.getString("EX_DOCNO")+"'";
						//System.out.println(M_strSQLQRY1);
                        if(stmSPDBA.executeUpdate(M_strSQLQRY1) >0)
                		{
                	    	conSPDBA.commit();
						}
					}
				}
				rstRSSET.close();
			}
			M_strSQLQRY = "select SWT_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,min(SWT_INCTM) SWT_INCTM,min(ex_docno) EX_DOCNO,ex_outtm, substr(cmt_codcd,6,4),b.ep_emlrf EP_EMLRF from hr_extrn,hr_epmst a,HR_SWTRN left outer join co_cdtrn on cmt_cgstp='HR"+M_strCMPCD+"LSN' and substr(cmt_codcd,1,4)=swt_empno left outer join hr_epmst b on b.ep_cmpcd='"+M_strCMPCD+"' and b.ep_empno=substr(cmt_codcd,6,4) where  swt_cmpcd='"+M_strCMPCD+"' and swt_wrkdt = current_date and swt_inctm is not null and  ex_cmpcd=swt_cmpcd and ex_empno = swt_empno and ex_docdt = swt_wrkdt  and ex_outtm is not null and ex_inctm is null and swt_inctm > ex_outtm and swt_cmpcd=a.ep_cmpcd and swt_empno=a.ep_empno  group by swt_empno,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.',substr(cmt_codcd,6,4),ex_outtm,b.ep_emlrf order by SWT_EMPNO";
			//System.out.println(M_strSQLQRY);
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
						//String strEXITMSG = rstRSSET.getString("EP_EMPNM")+" returned back at "+M_fmtLCDTM.format(rstRSSET.getTimestamp("SWT_INCTM"))+" Hrs. after Exit at "+M_fmtLCDTM.format(rstRSSET.getTimestamp("EX_OUTTM"))+" Hrs.";
						cl_eml ocl_eml = new cl_eml();
						//System.out.println(strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")");
						ocl_eml.setFRADR("systems_works@spl.co.in");
						ocl_eml.sendfile_ext(rstRSSET.getString("EP_EMLRF"),null,strEXITMSG,"");
						//ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")","");
						
						String M_strSQLQRY1 = "update hr_extrn set ex_inctm='"+M_fmtDBDTM.format(rstRSSET.getTimestamp("SWT_INCTM"))+"' where ex_cmpcd = '"+M_strCMPCD+"' and ex_empno='"+rstRSSET.getString("SWT_EMPNO")+"' and ex_docno = '"+rstRSSET.getString("EX_DOCNO")+"'";
						//System.out.println(M_strSQLQRY1);
                        if(stmSPDBA.executeUpdate(M_strSQLQRY1) >0)
                		{
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
	
	public static void exeOVRAL_MSG()
    {
		try
        {
			//hstLVETD.clear();
			//hstLVETM.clear();
			//hstEXTPS.clear();
			hstSTSDS.clear();
			//hstVSTDT.clear();
			//vtrMAILID.clear();
			ResultSet rstRSSET;
			String M_strSQLQRY = "";
			String strMSG="";
			int L_intSRLNO = 0;

			Date datCURDT = new Date();
			java.sql.Timestamp tmsCURDT = null;
			Vector<String> vtrEMPNO = new Vector<String>();
			int intCNT = 0;
			String L_strSQLQRY1="";
			
			L_strSQLQRY1 = " delete from co_intrn where in_msgdt <= date(days(current_date)-1)";
			//System.out.println(M_strSQLQRY1);
            if(stmSPDBA.executeUpdate(L_strSQLQRY1) >0)
            {
            	conSPDBA.commit();
			}

			M_strSQLQRY = "select current_timestamp TIMESTAMP,cmt_codcd,cmt_codds,cmt_cgstp from co_cdtrn where cmt_cgmtp='STS' and cmt_cgstp in ('HRXXLVT','MMXXIND','MMXXPOR')";
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					datCURDT = rstRSSET.getDate("TIMESTAMP");
					tmsCURDT = rstRSSET.getTimestamp("TIMESTAMP");
					if(!hstSTSDS.containsKey(rstRSSET.getString("cmt_codcd")+rstRSSET.getString("cmt_cgstp").substring(4,7)))
						hstSTSDS.put(rstRSSET.getString("cmt_cgstp").substring(4,7)+rstRSSET.getString("cmt_codcd"),rstRSSET.getString("cmt_codds"));
				}
				rstRSSET.close();
			}
			//System.out.println(datCURDT+">>"+tmsCURDT);
			L_strSQLQRY1 = " UPDATE CO_CDTRN SET CMT_CHP01 = '"+M_fmtLCDTM.format(tmsCURDT)+"' WHERE CMT_CGMTP ='S"+M_strCMPCD+"' AND CMT_CGSTP ='HRXXDTR' and cmt_codcd='02'";
			//System.out.println(M_strSQLQRY1);
            if(stmSPDBA.executeUpdate(L_strSQLQRY1) >0)
            {
            	conSPDBA.commit();
			}

			///Employees On leave Today
			M_strSQLQRY = " select lvt_empno,ifnull(a.EP_FSTNM,' ')||' '||substr(ifnull(a.EP_MDLNM,' '),1,1)||'. '||ifnull(a.EP_LSTNM,' ') EP_EMPNM,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_stsfl,lvt_rsnds,min(lvt_lvedt) min_lvedt,max(lvt_lvedt) max_lvedt,days(max(lvt_lvedt))-days(min(lvt_lvedt)) TOTAL,b.ep_empno EMPNO,b.ep_emlrf EP_EMLRF";
			M_strSQLQRY+= " from hr_epmst a,hr_lvtrn left outer join co_cdtrn on cmt_cgstp in ('HR"+M_strCMPCD+"LRC','HR"+M_strCMPCD+"LSN') and substr(cmt_codcd,1,4)=lvt_empno and substr(cmt_codcd,6,4)<>lvt_empno left outer join hr_epmst b on b.ep_empno=substr(cmt_codcd,6,4)";
			M_strSQLQRY+= " where lvt_cmpcd='"+M_strCMPCD+"' and lvt_cmpcd= a.ep_cmpcd and lvt_empno=a.ep_empno and  a.ep_lftdt is null and lvt_empno || char(lvt_refdt) in (select lvt_empno || char(lvt_refdt) from hr_lvtrn where lvt_cmpcd='"+M_strCMPCD+"'  and LVT_LVECD <> 'PE' and date(lvt_lvedt)= date(days(current_date)+0))";
			M_strSQLQRY+= " group by lvt_empno,a.ep_fstnm,a.ep_mdlnm,a.ep_lstnm,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_rsnds,b.ep_empno,b.ep_emlrf";
			M_strSQLQRY+= " order by EP_DPTNM,lvt_EMPNO";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String L_strLVETD = "";
			if(rstRSSET!=null)
			{
				L_intSRLNO=0;
				while(rstRSSET.next())
				{
					L_intSRLNO++;
					String L_strTMP = setSTRING(rstRSSET.getString("EP_EMPNM"),30)+"  "+rstRSSET.getString("LVT_LVECD")+"  "+(rstRSSET.getInt("TOTAL")+1)+" day(s)  "+M_fmtLCDAT.format(rstRSSET.getDate("MIN_LVEDT"))+(rstRSSET.getString("MIN_LVEDT").equals(rstRSSET.getString("MAX_LVEDT"))?"           ":"-"+M_fmtLCDTM.format(rstRSSET.getDate("MAX_LVEDT")))+"  "+rstRSSET.getString("LVT_RSNDS")+" ("+hstSTSDS.get("LVT"+rstRSSET.getString("LVT_STSFL"))+")";
					insINTRN(rstRSSET.getString("EMPNO"),L_intSRLNO,L_strTMP,"LV","TD",datCURDT);
				}
				rstRSSET.close();
			}
			
			///Employees On leave Tomorrow
			M_strSQLQRY = " select lvt_empno,ifnull(a.EP_FSTNM,' ')||' '||substr(ifnull(a.EP_MDLNM,' '),1,1)||'. '||ifnull(a.EP_LSTNM,' ') EP_EMPNM,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_stsfl,lvt_rsnds,min(lvt_lvedt) min_lvedt,max(lvt_lvedt) max_lvedt,days(max(lvt_lvedt))-days(min(lvt_lvedt)) TOTAL,b.ep_empno EMPNO,b.ep_emlrf EP_EMLRF";
			M_strSQLQRY+= " from hr_epmst a,hr_lvtrn  left outer join co_cdtrn on cmt_cgstp in ('HR"+M_strCMPCD+"LRC','HR"+M_strCMPCD+"LSN') and substr(cmt_codcd,1,4)=lvt_empno and substr(cmt_codcd,6,4)<>lvt_empno left outer join hr_epmst b on b.ep_empno=substr(cmt_codcd,6,4)";
			M_strSQLQRY+= " where lvt_cmpcd='"+M_strCMPCD+"' and lvt_cmpcd= a.ep_cmpcd and lvt_empno=a.ep_empno and  a.ep_lftdt is null and lvt_empno || char(lvt_refdt) in (select lvt_empno || char(lvt_refdt) from hr_lvtrn where lvt_cmpcd='"+M_strCMPCD+"'  and LVT_LVECD <> 'PE' and date(lvt_lvedt)= date(days(current_date)+1))";
			M_strSQLQRY+= " group by lvt_empno,a.ep_fstnm,a.ep_mdlnm,a.ep_lstnm,a.ep_dptnm,lvt_stsfl,lvt_lvecd,lvt_rsnds,b.ep_empno,b.ep_emlrf";
			M_strSQLQRY+= " order by EP_DPTNM,lvt_EMPNO";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String L_strLVETM = "";
			
			if(rstRSSET!=null)
			{
				L_intSRLNO=0;
				while(rstRSSET.next())
				{
					L_intSRLNO++;
					String L_strTMP = setSTRING(rstRSSET.getString("EP_EMPNM"),30)+"  "+rstRSSET.getString("LVT_LVECD")+"  "+(rstRSSET.getInt("TOTAL")+1)+" day(s)  "+M_fmtLCDAT.format(rstRSSET.getDate("MIN_LVEDT"))+(rstRSSET.getString("MIN_LVEDT").equals(rstRSSET.getString("MAX_LVEDT"))?"           ":"-"+M_fmtLCDTM.format(rstRSSET.getDate("MAX_LVEDT")))+"  "+rstRSSET.getString("LVT_RSNDS")+" ("+hstSTSDS.get("LVT"+rstRSSET.getString("LVT_STSFL"))+")";
					insINTRN(rstRSSET.getString("EMPNO"),L_intSRLNO,L_strTMP,"LV","TM",datCURDT);
				}
				rstRSSET.close();
			}
			
			///Yesterdays Exit Passes
			M_strSQLQRY = " select distinct EX_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,EX_OUTTM,EX_INCTM, EX_DOCNO,ex_remds,b.ep_empno EMPNO,b.ep_emlrf EP_EMLRF";
			M_strSQLQRY+= " from hr_epmst a,hr_extrn left outer join co_cdtrn on cmt_cgstp in ('HR"+M_strCMPCD+"LSN','HR"+M_strCMPCD+"LRC') and substr(cmt_codcd,1,4)=ex_empno and substr(cmt_codcd,6,4)<>ex_empno left outer join hr_epmst b on b.ep_cmpcd='"+M_strCMPCD+"' and b.ep_empno=substr(cmt_codcd,6,4)";
			M_strSQLQRY+= " where  ex_cmpcd='"+M_strCMPCD+"' and ex_docdt = date(days(current_date)-1) and ex_cmpcd=a.ep_cmpcd and ex_empno=a.ep_empno";
			M_strSQLQRY+= " order by b.ep_emlrf,ex_EMPNO";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String L_strEXTPS = "";
			if(rstRSSET!=null)
			{
				L_intSRLNO=0;
				while(rstRSSET.next())
				{
					L_intSRLNO++;
					String L_strTMP = setSTRING(rstRSSET.getString("EP_EMPNM"),30)+"  "+(rstRSSET.getTimestamp("EX_OUTTM")==null?"-               ":M_fmtLCDTM.format(rstRSSET.getTimestamp("EX_OUTTM")))+"  "+(rstRSSET.getTimestamp("EX_INCTM")==null?"-               ":M_fmtLCDTM.format(rstRSSET.getTimestamp("EX_INCTM")))+"  "+rstRSSET.getString("EX_REMDS");
					insINTRN(rstRSSET.getString("EMPNO"),L_intSRLNO,L_strTMP,"EP","YD",datCURDT);
				}
				rstRSSET.close();
			}
			
			M_strSQLQRY = " select distinct VS_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,VS_VSTNM,VS_VSORG,VS_PURPS,VS_VSITM,VS_VSOTM,ifnull(VS_VSICT,0) VS_VSICT,b.ep_empno EMPNO,b.ep_emlrf EP_EMLRF";
			M_strSQLQRY+= " from hr_epmst a,hr_vstrn left outer join co_cdtrn on cmt_cgstp in ('HR"+M_strCMPCD+"LSN','HR"+M_strCMPCD+"LRC') and substr(cmt_codcd,1,4)=vs_empno  left outer join hr_epmst b on  b.ep_empno=substr(cmt_codcd,6,4)";
			M_strSQLQRY+= " where  vs_cmpcd='"+M_strCMPCD+"' and vs_vsttp='01' and ifnull(VS_VSICT,0)>0 and vs_vstdt = date(days(current_date)-1) and vs_cmpcd=a.ep_cmpcd and vs_empno=a.ep_empno";
			M_strSQLQRY+= " order by b.ep_emlrf,vs_EMPNO";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			if(rstRSSET!=null)
			{
				L_intSRLNO=0;
				while(rstRSSET.next())
				{
					L_intSRLNO++;
					String L_strTMP = setSTRING(rstRSSET.getString("VS_VSTNM"),20)+"  "+setSTRING(rstRSSET.getString("VS_VSORG"),15)+"  "+setSTRING(rstRSSET.getString("VS_PURPS"),30)+"  "+setSTRING(rstRSSET.getString("EP_EMPNM"),20)+"  "+M_fmtHHMM.format(rstRSSET.getTime("VS_VSITM"))+"  "+(rstRSSET.getTime("VS_VSOTM")!=null?M_fmtHHMM.format(rstRSSET.getTime("VS_VSOTM")):"     ")+"  "+rstRSSET.getInt("VS_VSICT");
					insINTRN(rstRSSET.getString("EMPNO"),L_intSRLNO,L_strTMP,"VS","YD",datCURDT);
				}
				rstRSSET.close();
			}
			
			
			vtrEMPNO.clear();
			intCNT = 0;

			////Material indented pending for Authorisation
			M_strSQLQRY = " select in_indno,in_inddt,in_matcd,ct_matds,in_indqt,ct_uomcd,in_preby,in_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_inmst,co_ctmst,co_cdtrn where  in_cmpcd='"+M_strCMPCD+"' and in_strtp in ('01','06','07','08') and cmt_cgmtp='S01' and in_stsfl <> 'X' and cmt_cgstp='COXXICT' and cmt_codcd=IN_DPTCD and in_matcd=ct_matcd and in_stsfl in ('0','1','2','3') and in_inddt > date(days(current_date)-90)";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_PA = new String[5000][9];
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_PA[intCNT][0] = L_staEMPNO[i];
						staTEMP_PA[intCNT][1] = rstRSSET.getString("IN_INDNO");
						staTEMP_PA[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("IN_INDDT"));
						staTEMP_PA[intCNT][3] = rstRSSET.getString("IN_MATCD");
						staTEMP_PA[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_PA[intCNT][5] = rstRSSET.getString("IN_INDQT");
						staTEMP_PA[intCNT][6] = rstRSSET.getString("CT_UOMCD");
						staTEMP_PA[intCNT][7] = setSTRING(rstRSSET.getString("IN_PREBY"),3);
						staTEMP_PA[intCNT][8] = hstSTSDS.get("IND"+rstRSSET.getString("IN_STSFL"));
						
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_PA[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_PA[j][1]+"  "+staTEMP_PA[j][2]+"  "+staTEMP_PA[j][3]+"  "+staTEMP_PA[j][4]+"  "+staTEMP_PA[j][5]+"  "+staTEMP_PA[j][6]+"  "+staTEMP_PA[j][7]+"  "+staTEMP_PA[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"IN","PA",datCURDT);
						L_intSRLNO++;
					}
				}
			}
			
			vtrEMPNO.clear();
			intCNT = 0;
			
			////Indent authorised Order to be generated
			M_strSQLQRY = " select in_indno,in_inddt,in_matcd,ct_matds,in_autqt,ct_uomcd,in_preby,in_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_inmst,co_ctmst,co_cdtrn where in_cmpcd='"+M_strCMPCD+"' and in_strtp  in ('01','06','07','08') and in_stsfl <> 'X' and cmt_cgmtp='S01' and cmt_cgstp='COXXICT' and cmt_codcd=IN_DPTCD and in_matcd=ct_matcd and in_stsfl ='4' and (ifnull(in_autqt,0)-ifnull(in_fccqt,0))>ifnull(in_ordqt,0) and in_inddt > date(days(current_date)-90)";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_PO = new String[5000][9];
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_PO[intCNT][0] = L_staEMPNO[i];
						staTEMP_PO[intCNT][1] = rstRSSET.getString("IN_INDNO");
						staTEMP_PO[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("IN_INDDT"));
						staTEMP_PO[intCNT][3] = rstRSSET.getString("IN_MATCD");
						staTEMP_PO[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_PO[intCNT][5] = rstRSSET.getString("IN_AUTQT");
						staTEMP_PO[intCNT][6] = rstRSSET.getString("CT_UOMCD");
						staTEMP_PO[intCNT][7] = setSTRING(rstRSSET.getString("IN_PREBY"),3);
						staTEMP_PO[intCNT][8] = hstSTSDS.get("IND"+rstRSSET.getString("IN_STSFL"));
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_PO[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_PO[j][1]+"  "+staTEMP_PO[j][2]+"  "+staTEMP_PO[j][3]+"  "+staTEMP_PO[j][4]+"  "+staTEMP_PO[j][5]+"  "+staTEMP_PO[j][6]+"  "+staTEMP_PO[j][7]+"  "+staTEMP_PO[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"IN","PO",datCURDT);
						L_intSRLNO++;
					}
				}
			}
			
			vtrEMPNO.clear();
			intCNT = 0;
			
			////Order generated material to be received.
			M_strSQLQRY = " select po_porno,po_pordt,po_matcd,ct_matds,po_porqt,ct_uomcd,po_indno,po_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_pomst,co_ctmst,co_cdtrn where po_cmpcd='"+M_strCMPCD+"' and po_strtp  in ('01','06','07','08') and po_stsfl<>'X' and cmt_cgmtp='S01' and cmt_cgstp='COXXICT' and cmt_codcd=PO_DPTCD and po_matcd=ct_matcd and po_stsfl ='A' and (ifnull(po_porqt,0)-ifnull(po_frcqt,0))>ifnull(po_acpqt,0) and po_pordt > date(days(current_date)-90)";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_PR = new String[5000][9];
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_PR[intCNT][0] = L_staEMPNO[i];
						staTEMP_PR[intCNT][1] = rstRSSET.getString("PO_PORNO");
						staTEMP_PR[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("PO_PORDT"));
						staTEMP_PR[intCNT][3] = rstRSSET.getString("PO_MATCD");
						staTEMP_PR[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_PR[intCNT][5] = rstRSSET.getString("PO_PORQT");
						staTEMP_PR[intCNT][6] = rstRSSET.getString("CT_UOMCD");
						staTEMP_PR[intCNT][7] = rstRSSET.getString("po_indno");
						staTEMP_PR[intCNT][8] = hstSTSDS.get("POR"+rstRSSET.getString("PO_STSFL"));
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_PR[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_PR[j][1]+"  "+staTEMP_PR[j][2]+"  "+staTEMP_PR[j][3]+"  "+staTEMP_PR[j][4]+"  "+staTEMP_PR[j][5]+"  "+staTEMP_PR[j][6]+"  "+staTEMP_PR[j][7]+"  "+staTEMP_PR[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"IN","PR",datCURDT);
						L_intSRLNO++;
					}
				}
			}
			
			
			///////////for old records//////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////
			
			////Material indented pending for Authorisation
			M_strSQLQRY = " select in_indno,in_inddt,in_matcd,ct_matds,in_indqt,ct_uomcd,in_preby,in_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_inmst,co_ctmst,co_cdtrn where  in_cmpcd='"+M_strCMPCD+"' and in_strtp in ('01','06','07','08') and cmt_cgmtp='S01' and in_stsfl <> 'X' and cmt_cgstp='COXXICT' and cmt_codcd=IN_DPTCD and in_matcd=ct_matcd and in_stsfl in ('0','1','2','3') and in_inddt <= date(days(current_date)-90)";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_OA = new String[5000][9];
			//Vector<String> vtrEMPNO = new Vector<String>();
			vtrEMPNO.clear();
			intCNT = 0;
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_OA[intCNT][0] = L_staEMPNO[i];
						staTEMP_OA[intCNT][1] = rstRSSET.getString("IN_INDNO");
						staTEMP_OA[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("IN_INDDT"));
						staTEMP_OA[intCNT][3] = rstRSSET.getString("IN_MATCD");
						staTEMP_OA[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_OA[intCNT][5] = rstRSSET.getString("IN_INDQT");
						staTEMP_OA[intCNT][6] = rstRSSET.getString("CT_UOMCD");
						staTEMP_OA[intCNT][7] = setSTRING(rstRSSET.getString("IN_PREBY"),3);
						staTEMP_OA[intCNT][8] = hstSTSDS.get("IND"+rstRSSET.getString("IN_STSFL"));
						
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_OA[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_OA[j][1]+"  "+staTEMP_OA[j][2]+"  "+staTEMP_OA[j][3]+"  "+staTEMP_OA[j][4]+"  "+staTEMP_OA[j][5]+"  "+staTEMP_OA[j][6]+"  "+staTEMP_OA[j][7]+"  "+staTEMP_OA[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"IN","OA",datCURDT);
						L_intSRLNO++;
					}
				}
			}
			
			vtrEMPNO.clear();
			intCNT = 0;
			
			////Indent authorised Order to be generated
			M_strSQLQRY = " select in_indno,in_inddt,in_matcd,ct_matds,in_autqt,ct_uomcd,in_preby,in_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_inmst,co_ctmst,co_cdtrn where in_cmpcd='"+M_strCMPCD+"' and in_strtp  in ('01','06','07','08') and in_stsfl <> 'X' and cmt_cgmtp='S01' and cmt_cgstp='COXXICT' and cmt_codcd=IN_DPTCD and in_matcd=ct_matcd and in_stsfl ='4' and (ifnull(in_autqt,0)-ifnull(in_fccqt,0))>ifnull(in_ordqt,0) and in_inddt <= date(days(current_date)-90)";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_OO = new String[5000][9];
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_OO[intCNT][0] = L_staEMPNO[i];
						staTEMP_OO[intCNT][1] = rstRSSET.getString("IN_INDNO");
						staTEMP_OO[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("IN_INDDT"));
						staTEMP_OO[intCNT][3] = rstRSSET.getString("IN_MATCD");
						staTEMP_OO[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_OO[intCNT][5] = rstRSSET.getString("IN_AUTQT");
						staTEMP_OO[intCNT][6] = rstRSSET.getString("CT_UOMCD");
						staTEMP_OO[intCNT][7] = setSTRING(rstRSSET.getString("IN_PREBY"),3);
						staTEMP_OO[intCNT][8] = hstSTSDS.get("IND"+rstRSSET.getString("IN_STSFL"));
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_OO[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_OO[j][1]+"  "+staTEMP_OO[j][2]+"  "+staTEMP_OO[j][3]+"  "+staTEMP_OO[j][4]+"  "+staTEMP_OO[j][5]+"  "+staTEMP_OO[j][6]+"  "+staTEMP_OO[j][7]+"  "+staTEMP_OO[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"IN","OO",datCURDT);
						L_intSRLNO++;
					}
				}
			}
			
			vtrEMPNO.clear();
			intCNT = 0;
			
			////Order generated material to be received.
			M_strSQLQRY = " select po_porno,po_pordt,po_matcd,ct_matds,po_porqt,ct_uomcd,po_indno,po_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_pomst,co_ctmst,co_cdtrn where po_cmpcd='"+M_strCMPCD+"' and po_strtp  in ('01','06','07','08') and po_stsfl<>'X' and cmt_cgmtp='S01' and cmt_cgstp='COXXICT' and cmt_codcd=PO_DPTCD and po_matcd=ct_matcd and po_stsfl ='A' and (ifnull(po_porqt,0)-ifnull(po_frcqt,0))>ifnull(po_acpqt,0) and po_pordt <= date(days(current_date)-90)";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_OR = new String[5000][9];
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_OR[intCNT][0] = L_staEMPNO[i];
						staTEMP_OR[intCNT][1] = rstRSSET.getString("PO_PORNO");
						staTEMP_OR[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("PO_PORDT"));
						staTEMP_OR[intCNT][3] = rstRSSET.getString("PO_MATCD");
						staTEMP_OR[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_OR[intCNT][5] = rstRSSET.getString("PO_PORQT");
						staTEMP_OR[intCNT][6] = rstRSSET.getString("CT_UOMCD");
						staTEMP_OR[intCNT][7] = rstRSSET.getString("po_indno");
						staTEMP_OR[intCNT][8] = hstSTSDS.get("POR"+rstRSSET.getString("PO_STSFL"));
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_OR[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_OR[j][1]+"  "+staTEMP_OR[j][2]+"  "+staTEMP_OR[j][3]+"  "+staTEMP_OR[j][4]+"  "+staTEMP_OR[j][5]+"  "+staTEMP_OR[j][6]+"  "+staTEMP_OR[j][7]+"  "+staTEMP_OR[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"IN","OR",datCURDT);
						L_intSRLNO++;
					}
				}
			}
			
			
			vtrEMPNO.clear();
			intCNT = 0;
			
			///////Old records for indent queries over////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////
			
			////Material pending for inspection
			M_strSQLQRY = " select distinct gr_grnno,gr_grndt,gr_matcd,ct_matds,sum(gr_recqt) gr_recqt,ct_uomcd,gr_porno,gr_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_grmst,co_ctmst,co_cdtrn,mm_pomst";
			M_strSQLQRY+= " where gr_cmpcd='01' and gr_strtp  in ('01','06','07','08') and gr_stsfl<>'X' and gr_cmpcd=po_cmpcd and gr_strtp=po_strtp and gr_porno=po_porno and gr_matcd=po_matcd and cmt_cgmtp='S01' and cmt_cgstp='COXXICT' and cmt_codcd=PO_DPTCD and po_matcd=ct_matcd and po_stsfl ='A'  and gr_grndt > date(days(current_date)-90)";
			M_strSQLQRY+= " group by gr_grnno,gr_grndt,gr_matcd,ct_matds,ct_uomcd,gr_porno,gr_stsfl,cmt_modls";
			M_strSQLQRY+= " having  (sum(ifnull(gr_recqt,0))-sum(ifnull(gr_rejqt,0)))>sum(ifnull(gr_acpqt,0))";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_GI = new String[5000][9];
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_GI[intCNT][0] = L_staEMPNO[i];
						staTEMP_GI[intCNT][1] = rstRSSET.getString("gr_grnno");
						staTEMP_GI[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("gr_grndt"));
						staTEMP_GI[intCNT][3] = rstRSSET.getString("GR_MATCD");
						staTEMP_GI[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_GI[intCNT][5] = rstRSSET.getString("gr_recqt");
						staTEMP_GI[intCNT][6] = rstRSSET.getString("ct_uomcd");
						staTEMP_GI[intCNT][7] = rstRSSET.getString("gr_porno");
						//staTEMP_GI[intCNT][8] = hstSTSDS.get("POR"+rstRSSET.getString("gr_stsfl"));
						staTEMP_GI[intCNT][8] = rstRSSET.getString("gr_stsfl");
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_GI[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_GI[j][1]+"  "+staTEMP_GI[j][2]+"  "+staTEMP_GI[j][3]+"  "+staTEMP_GI[j][4]+"  "+staTEMP_GI[j][5]+"  "+staTEMP_GI[j][6]+"  "+staTEMP_GI[j][7]+"  "+staTEMP_GI[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"GP","GI",datCURDT);
						L_intSRLNO++;
					}
				}
			}
			
			vtrEMPNO.clear();
			intCNT = 0;
			
			////Material to be received against Returnable Gate Pass
			M_strSQLQRY = " select distinct gp_mgpno,gp_mgpdt,gp_matcd,ct_matds,gp_issqt,ct_uomcd,gp_duedt,gp_stsfl,cmt_modls";
			M_strSQLQRY+= " from mm_gptrn,co_ctmst,co_cdtrn";
			M_strSQLQRY+= " where gp_cmpcd='01' and gp_strtp  in ('01','06','07','08') and gp_mgptp='51' and gp_stsfl not in ('X','C') and ifnull(gp_issqt,0)>ifnull(gp_recqt,0) and gp_matcd=ct_matcd and  cmt_cgmtp='S01' and cmt_cgstp='COXXICT' and cmt_codcd=GP_DPTCD and gp_mgpdt > date(days(current_date)-90)";
			M_strSQLQRY+= " order by cmt_modls";
			//System.out.println(M_strSQLQRY);
			rstRSSET = stmt.executeQuery(M_strSQLQRY);
			String[][] staTEMP_GR = new String[5000][9];
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					String[] L_staEMPNO = rstRSSET.getString("CMT_MODLS").split("_");
					for(int i=0;i<L_staEMPNO.length;i++)
					{
						if(!vtrEMPNO.contains(L_staEMPNO[i]))
							vtrEMPNO.add(L_staEMPNO[i]);
						staTEMP_GR[intCNT][0] = L_staEMPNO[i];
						staTEMP_GR[intCNT][1] = rstRSSET.getString("gp_mgpno");
						staTEMP_GR[intCNT][2] = M_fmtLCDAT.format(rstRSSET.getDate("gp_mgpdt"));
						staTEMP_GR[intCNT][3] = rstRSSET.getString("gp_matcd");
						staTEMP_GR[intCNT][4] = setSTRING(rstRSSET.getString("CT_MATDS"),30);
						staTEMP_GR[intCNT][5] = rstRSSET.getString("gp_issqt");
						staTEMP_GR[intCNT][6] = rstRSSET.getString("ct_uomcd");
						staTEMP_GR[intCNT][7] = M_fmtLCDAT.format(rstRSSET.getDate("gp_duedt"));
						//staTEMP_GR[intCNT][8] = hstSTSDS.get("POR"+rstRSSET.getString("gr_stsfl"));
						staTEMP_GR[intCNT][8] = rstRSSET.getString("gp_stsfl");
						intCNT++;
					}
				}
			}
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				L_intSRLNO=0;
				for(int j=0;j<intCNT;j++)
				{
					if(staTEMP_GR[j][0].equals(vtrEMPNO.get(i)))
					{	
						String L_strTMP = staTEMP_GR[j][1]+"  "+staTEMP_GR[j][2]+"  "+staTEMP_GR[j][3]+"  "+staTEMP_GR[j][4]+"  "+staTEMP_GR[j][5]+"  "+staTEMP_GR[j][6]+"  "+staTEMP_GR[j][7]+"  "+staTEMP_GR[j][8];
						insINTRN(vtrEMPNO.get(i),L_intSRLNO,L_strTMP,"GP","GR",datCURDT);
						L_intSRLNO++;
					}
				}
			}

			/*
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
				if(hstVSTDT.containsKey(vtrMAILID.get(i)))
				{
					strMSG += "\n\nVisitors detail (Yesterday) : \n";
					strMSG += setSTRING("VISITOR NAME",20)+"  "+setSTRING("ORGANIZATION",15)+"  "+setSTRING("PURPOSE",30)+"  "+setSTRING("VISITING PERSON",20)+"  "+setSTRING("IN",5)+"  "+setSTRING("OUT",5)+"  "+"No.Person\n";
					strMSG = setSTRMSG(strMSG,vtrMAILID.get(i),hstVSTDT);
				}
				//JOptionPane.showMessageDialog(null,strMSG,"Time Descripnacy...",JOptionPane.INFORMATION_MESSAGE);
				ocl_eml.setFRADR("systems_works@spl.co.in");
				ocl_eml.sendfile_ext(vtrMAILID.get(i),null,"Attendance Intimation",strMSG);
				ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,"Attendance Intimation to "+vtrMAILID.get(i),strMSG);
				ocl_eml.sendfile_ext("systems_works@spl.co.in",null,"Attendance Intimation to "+vtrMAILID.get(i),strMSG);
				strMSG="";
			}
			*/
			if(rstRSSET != null)
				rstRSSET.close();	
		}
        catch(Exception L_EX)
        {
			System.out.println("Error in exeOVRAL_MSG() : "+L_EX);
		}
	}
	
	public static void insINTRN(String LP_EMPNO,int LP_SRLNO,String LP_MSGDS,String LP_MSMTP,String LP_MSSTP,Date LP_MSGDT)
	{
		String L_strSQLQRY ="";
		try
		{
			L_strSQLQRY =" Insert into co_intrn(IN_CMPCD,IN_EMPNO,IN_SRLNO,IN_MSGDT,IN_MSMTP,IN_MSSTP,IN_MSGDS) values(";
			L_strSQLQRY+= "'"+M_strCMPCD+"',";
			L_strSQLQRY+= "'"+LP_EMPNO+"',";
			L_strSQLQRY+= "'"+(String.valueOf(LP_SRLNO).length()==3 ? String.valueOf(LP_SRLNO):(String.valueOf(LP_SRLNO).length()==2 ? "0":"00")+String.valueOf(LP_SRLNO))+"',";
			L_strSQLQRY+= "'"+M_fmtDBDAT.format(LP_MSGDT)+"',";
			L_strSQLQRY+= "'"+LP_MSMTP+"',";
			L_strSQLQRY+= "'"+LP_MSSTP+"',";
			L_strSQLQRY+= "'"+LP_MSGDS+"')";
			//System.out.println("L_strSQLQRY>>"+L_strSQLQRY);
			if(stmSPDBA.executeUpdate(L_strSQLQRY) >0)
            {
            	conSPDBA.commit();
			}
		}
		catch(Exception E)
		{
			System.out.println("L_strSQLQRY>>"+L_strSQLQRY);
			System.out.println("error in insINTRN() : "+E);
		}
	}
	
	/*public static String setSTRMSG(String LP_STR,String LP_MAILID,Hashtable<String,Vector<String>> LP_HSTBL)
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
	*/
	public static String setSTRING(String LP_STR,int LP_LEN)
	{
		if(LP_STR.length()>LP_LEN)
			LP_STR = LP_STR.substring(0,LP_LEN-1);
		else if(LP_STR.length()<LP_LEN)
		{
			for(int i=LP_STR.length();i<LP_LEN-1;i++)
				LP_STR+=" ";
		}
		return LP_STR;
	}
	
public void actionPerformed(ActionEvent L_AE)
{
 try{
		if(L_AE.getSource().equals(btnPROCS))
		{
			if(chkENTDT.isSelected())
			{
				if(txtSTRDT.getText().length()==0)
				{	
					setMSG("Please Enter Start Date..",'N');
					txtSTRDT.requestFocus();
					return;
				}	
				else if(txtENDDT.getText().length()==0)
				{	
					setMSG("Please Enter End Date..",'N');
					txtENDDT.requestFocus();
					return;
				}
				hr_teexr ohr_teexr=new hr_teexr(conSPDBA,strSTRDT);
				System.out.println("calling hr_teepr");
				ohr_teexr.prcDATA(txtSTRDT.getText(),txtENDDT.getText(),"","","");
				System.out.println("called hr_teepr");
			}	
			//System.out.println("calling hr_teepr");
			//ohr_teexr.prcDATA(txtSTRDT.getText(),txtENDDT.getText());	
			//System.out.println("called hr_teepr");			
		}
		if(L_AE.getSource().equals(btnSTOP))
		{
		}
		if(L_AE.getSource().equals(btnEXIT))
		{
		    System.exit(0);
		}
	}
	catch(Exception E)
	{
        System.out.println("inside constructor()"+E);
    }
}

    public static void main(String args[])
    {
        try
        {
			if(args.length == 0 || !(args[0].equals("H") || args[0].equals("D")))
			{
				System.out.println("Please Enter Parameter as H(hourly) or D(Daily)");
				System.exit(0);
			}
			strPRGTP = args[0];
	        hr_teepr ohr_teepr = new hr_teepr();
		    System.exit(0);
        }
        catch(Exception L_E)
        {
            System.out.println(L_E.toString());
        }
        
    }
    private Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
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
			setMSG("Error while connecting to DB : "+L_EX,'E');
			return null;
		}
	}
   private void setMSG(String P_strMESG,char P_chrMSGTP)
	{
		try
		{
			if(P_chrMSGTP == 'N')
			{
				lblMESG.setForeground(Color.blue);
				lblMESG.setText(P_strMESG);
			}
			else
			{
				lblMESG.setForeground(Color.red);
				lblMESG.setText(P_strMESG);
			}
			P_strMESG=null;
			System.gc();
		}
		catch (Exception e)
		{
			setMSG(e.toString(),'E');
		}
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
