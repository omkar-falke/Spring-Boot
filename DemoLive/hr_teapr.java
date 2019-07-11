import javax.swing.*;
import java.io.IOException;import java.io.File;import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Vector;import java.util.StringTokenizer;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.Color;import java.awt.Container;import java.awt.Toolkit;
import java.awt.Cursor;import java.awt.Component;
import java.sql.*;
import java.sql.CallableStatement;
import java.util.Map;import java.util.Calendar;import java.text.SimpleDateFormat;
class hr_teapr extends JFrame implements ActionListener,Runnable
{
   	private File file;
	private RandomAccessFile file_io;
	private JButton btnPROCS,btnSTOP,btnEXIT;
	private JLabel lblMESG;
	private static JTextField txtDATE;
	private boolean FLAG = true;
	private static CallableStatement cstEMPCT;
	private static CallableStatement cstSWTRN;
	private cl_pbase ocl_pbase;
	private static Connection conSPDBA;
	private static Statement stmSPDBA;
	private static Statement stmt;
	private PreparedStatement pstmSPDBA;
	//private static Hashtable hstAXIS1,hstAXIS2,hstAXIS3,hstAXIS4,hstAXIS5,hstMAIN;
	private static String strDATE;
	private static Thread thrCONNECT;
	private static SimpleDateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");	/**	For Timestamp in DB format "yyyy-MM-dd-HH.mm" */
	private static SimpleDateFormat M_fmtDBDTM=new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	private static SimpleDateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");	/**	For Timestamp in Locale format "dd/MM/yyyy HH:mm" */
	private static SimpleDateFormat M_fmtLCTIM=new SimpleDateFormat("HH:mm");	/**	For Timestamp in Locale format "dd/MM/yyyy HH:mm" */
	private static SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	private static Calendar M_calLOCAL=Calendar.getInstance();
	private static String M_strSBSCD ="";
	private static String M_strCMPCD;
    hr_teapr()
    {
        super("Head count updating");
        try
        {
            thrCONNECT = new Thread(this);
		    thrCONNECT.start();
            cl_dat.M_dimSCRN_pbst = Toolkit.getDefaultToolkit().getScreenSize();
			cl_dat.M_dblWIDTH=(cl_dat.M_dimSCRN_pbst.width/800.00);
			cl_dat.M_dblHIGHT=((cl_dat.M_dimSCRN_pbst.height)/600.00);
            ocl_pbase = new cl_pbase();
            ocl_pbase.setMatrix(20,8);
            ocl_pbase.add(new JLabel("Transfer Date"),2,1,1,1,ocl_pbase,'L');
            ocl_pbase.add(txtDATE = new TxtLimit(10),2,2,1,1,ocl_pbase,'L');
            ocl_pbase.add(btnPROCS = new JButton("Process"),2,3,1,1,ocl_pbase,'L');
            ocl_pbase.add(btnSTOP = new JButton("STOP"),2,4,1,1,ocl_pbase,'L');
            ocl_pbase.add(btnEXIT = new JButton("EXIT"),2,5,1,1,ocl_pbase,'L');
            ocl_pbase.add(lblMESG = new JLabel(" "),4,1,1,8,ocl_pbase,'L');
            Container L_ctrTHIS=getContentPane();
			M_fmtDBDAT.setLenient(false);
			M_fmtDBDTM.setLenient(false);
			M_fmtLCDAT.setLenient(false);
			M_fmtLCDTM.setLenient(false);
			M_calLOCAL.setLenient(false);
			L_ctrTHIS.add(ocl_pbase);
		    setSize(800,200);
			//show();
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
		strDATE = "29/04/2009";
		txtDATE.setText(strDATE);
        /*hstAXIS1 = new Hashtable();
        hstAXIS2 = new Hashtable();
        hstAXIS3 = new Hashtable();
        hstAXIS4 = new Hashtable();
        hstAXIS5 = new Hashtable();
        hstMAIN = new Hashtable();*/
      	}
	    catch(Exception E)
		{
            System.out.println(E.toString());
        }

    }

	/**
	 */
	public void run()
    {
        try
        {
            conSPDBA=setCONDTB("01","spldata","FIMS","FIMS");
            if(conSPDBA != null)
            {
                setMSG("Connected to Database..",'N');
            }
            if(conSPDBA == null)
            {
                setMSG("Failed to Connect to spldata..",'E');
                return; 
            }
            stmSPDBA = conSPDBA.createStatement();
            stmt = conSPDBA.createStatement();
            cstEMPCT = conSPDBA.prepareCall("{call hr_ephct(?)}");
			cstEMPCT.setString(1,M_strCMPCD);
            String L_strTEMP = "SELECT CMT_CHP01 from CO_CDTRN WHERE CMT_CGMTP ='S"+M_strCMPCD+"' AND CMT_CGSTP ='HRXXDTR' AND CMT_CODCD ='01'";
            String L_strDATE;
            ResultSet L_rstRSSET = stmSPDBA.executeQuery(L_strTEMP);
            if(L_rstRSSET !=null)
            if(L_rstRSSET.next())
            {
                L_strTEMP = L_rstRSSET.getString("CMT_CHP01");
				strDATE = L_strTEMP.substring(0,10);
                //strDATE = L_strTEMP.substring(8,10)+"/"+L_strTEMP.substring(5,7)+"/"+L_strTEMP.substring(0,4);
				//strDATE = "11/09/2008";
                txtDATE.setText(strDATE);
            }
        }
        catch(SQLException L_E)
        {
            System.out.println(L_E.toString());
        }
    }

/**
 */
public void actionPerformed(ActionEvent L_AE)
{
    if(L_AE.getSource().equals(btnPROCS))
	{
	   strDATE = txtDATE.getText();
	}
	if(L_AE.getSource().equals(btnSTOP))
	{
	}
	if(L_AE.getSource().equals(btnEXIT))
	{
	    System.exit(0);
	}
}
    public static void main(String args[])
    {
        try
        {
			M_strCMPCD = args[0];
			hr_teapr ohr_teapr = new hr_teapr();
	        if(thrCONNECT!=null)
				thrCONNECT.join();
			if(args[0].equals("01"))
			{
		        hr_teapr_thrData thr1 = new hr_teapr_thrData("TNTTerminal102AXIS1",strDATE,conSPDBA,args[0]);
				hr_teapr_thrData thr2 = new hr_teapr_thrData("TNTTerminal102AXIS2",strDATE,conSPDBA,args[0]);
		        hr_teapr_thrData thr3 = new hr_teapr_thrData("TNTTerminal102AXIS3",strDATE,conSPDBA,args[0]);
		        hr_teapr_thrData thr4 = new hr_teapr_thrData("TNTTerminal102AXIS4",strDATE,conSPDBA,args[0]);
		        thr1.start();
		        thr2.start();
		        thr3.start();
				thr4.start();
				thr1.join();thr2.join();thr3.join();thr4.join();
			}	
			if(args[0].equals("11"))
			{
	        	hr_teapr_thrData thr5 = new hr_teapr_thrData("TNTTerminal102SPLCHENNAI1",strDATE,conSPDBA,args[0]);
				thr5.start();
		        thr5.join();
	        }
	        if(conSPDBA !=null)
	        {
				String L_strCURDT="";
				String L_strCURTM="";
				try{
				String L_strTEMP =" Select current_date DATE,current_time TIME from CO_CDTRN";
				ResultSet L_rstRSSET = stmSPDBA.executeQuery(L_strTEMP);
				if(L_rstRSSET!=null && L_rstRSSET.next())
				{
					L_strCURDT = M_fmtLCDAT.format(L_rstRSSET.getDate("DATE"));
					L_strCURTM = M_fmtLCTIM.format(L_rstRSSET.getTime("TIME"));
				}
				L_rstRSSET.close();
				L_strTEMP ="UPDATE CO_CDTRN SET CMT_CHP01 = '"+L_strCURDT+" "+L_strCURTM+"' WHERE CMT_CGMTP ='S"+M_strCMPCD+"' AND CMT_CGSTP ='HRXXDTR' and CMT_CODCD = '01'";
				//System.out.println("L_STRTMP>>"+L_strTEMP);
	            stmSPDBA.executeUpdate(L_strTEMP);
	            conSPDBA.commit();
				}catch(Exception E){System.out.println("date time :"+E);}
	            System.out.println("commit complte");
	            System.out.println("calling stored procedure");
	            cstEMPCT.executeUpdate();
	            System.out.println("stored procedure called");
	            conSPDBA.commit();
	        }
	
	 		prcLOG();
	        System.exit(0);
        }
        catch(Exception L_E)
        {
            System.out.println(L_E.toString());
        }
        
    }
    private  Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
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
   private static void prcLOG()
	{
		try
	    {
			String strEMPNO ="";
			String strWRKDT ="";
			String strPNCTM ="";
		    String strSQLQRY ="";
			String strSRLNO ="";
			String strINOST ="";
			String L_strSRLNO ="";
			String strPEMPNO ="",strPWRKDT=""; 
			java.sql.Date datWRKDT;
			java.sql.Timestamp tmsPNCTM;
			ResultSet L_rstRSSET =null;
			boolean L_flgERR = false;
			int L_intSRLNO =0;
            conSPDBA.commit();
			System.out.println("calling updSWTRN_SLT");
            cstSWTRN = conSPDBA.prepareCall("{call updSWTRN_SLT(?)}");
			cstSWTRN.setString(1,M_strCMPCD);
            cstSWTRN.executeUpdate();
			System.out.println("updSWTRN_SLT called");
            conSPDBA.commit();

			//exeEXITMSG();			
			
			//if (conSPDBA != null)
			//		if(stmSPDBA.executeUpdate(strSQLQRY)>0)
			//		{
						conSPDBA.commit();
						strSQLQRY = "UPDATE HR_SLTRN SET SLT_STSFL ='2' where slt_cmpcd = '"+M_strCMPCD+"'";
						//System.out.println(strSQLQRY);	 						if (conSPDBA != null)
						if(stmSPDBA.executeUpdate(strSQLQRY)>0)
							conSPDBA.commit();
						else
							conSPDBA.rollback();
			//		}
			 conSPDBA.commit();
			 
				strSQLQRY = "DELETE FROM HR_SLTRN A WHERE A.SLT_CMPCD='"+M_strCMPCD+"' AND A.SLT_EMPNO||CHAR(A.SLT_PNCTM) NOT IN (SELECT B.SLT_EMPNO||MAX(CHAR(B.SLT_PNCTM)) FROM HR_SLTRN B WHERE B.SLT_CMPCD='"+M_strCMPCD+"' GROUP BY B.SLT_EMPNO)";
				System.out.println(strSQLQRY);	 			
				stmSPDBA.executeUpdate(strSQLQRY);
				conSPDBA.commit();
				//if (conSPDBA != null)
				//	if(stmSPDBA.executeUpdate(strSQLQRY)>0)
				//	{
				//		conSPDBA.commit();
				//		strSQLQRY = "UPDATE HR_SLTRN SET SLT_STSFL ='2' where slt_cmpcd = '"+M_strCMPCD+"'";
				//		//System.out.println(strSQLQRY);	 					
				//	if (conSPDBA != null)
				//		if(stmSPDBA.executeUpdate(strSQLQRY)>0)
				//			conSPDBA.commit();
				//		else
				//			conSPDBA.rollback();
				//	}
			 //conSPDBA.commit();
		}
		catch(SQLException L_SEX)
	    {
			//L_flgERR = true;
			System.out.println(" SQL "+L_SEX.toString());
	    }
	    catch(Exception L_EX)
	    {
			System.out.println(L_EX.toString());
	    }
	}

	
	public  static void exeEXITMSG()
    {
		try
        {

			String M_strSQLQRY = "select SWT_EMPNO,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.'  ep_empnm,min(SWT_OUTTM) SWT_OUTTM,min(ex_docno) EX_DOCNO,ex_remds, substr(cmt_codcd,6,4),b.ep_emlrf EP_EMLRF from HR_SWTRN,hr_extrn,hr_epmst a left outer join co_cdtrn on cmt_cgstp='HR"+M_strCMPCD+"LSN' and substr(cmt_codcd,1,4)=swt_empno left outer join hr_epmst b on b.ep_cmpcd='"+M_strCMPCD+"' and b.ep_empno=substr(cmt_codcd,6,4) where  swt_cmpcd='"+M_strCMPCD+"' and swt_wrkdt = current_date and swt_outtm is not null and ex_cmpcd=swt_cmpcd and ex_empno = swt_empno and ex_docdt = swt_wrkdt  and ex_outtm is null  and  swt_outtm >= ex_gentm and swt_cmpcd=a.ep_cmpcd and swt_empno=a.ep_empno  group by swt_empno,a.ep_lstnm||' '||substr(a.ep_fstnm,1,1)||'.'||substr(a.ep_mdlnm,1,1)||'.',substr(cmt_codcd,6,4),ex_remds,b.ep_emlrf order by SWT_EMPNO";
			System.out.println(M_strSQLQRY);
			ResultSet rstRSSET = stmt.executeQuery(M_strSQLQRY);
			if(rstRSSET!=null)
			{
				while(rstRSSET.next())
				{
					if(rstRSSET.getString("EP_EMLRF").length()>0)
					{
						String strEXITMSG = rstRSSET.getString("EP_EMPNM")+" punched out at "+M_fmtLCDTM.format(rstRSSET.getTimestamp("SWT_OUTTM"))+" Hrs.  for Reason : "+rstRSSET.getString("EX_REMDS");
						//JOptionPane.showMessageDialog(null,strBODY, "Verify", JOptionPane.INFORMATION_MESSAGE); 
						cl_eml ocl_eml = new cl_eml();
						//ocl_eml.sendfile(rstRSSET.getString("EP_EMLRF"),null,"Exit Pass Approval",strBODY);
						System.out.println(strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")");
						//ocl_eml.setFRADR("systems_works@spl.co.in");
						ocl_eml.sendfile_ext(rstRSSET.getString("EP_EMLRF"),null,strEXITMSG,"");
						//ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")","");
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
						String strEXITMSG = rstRSSET.getString("EP_EMPNM")+" returned back at "+M_fmtLCDTM.format(rstRSSET.getTimestamp("SWT_INCTM"))+" Hrs. after Exit at "+M_fmtLCDTM.format(rstRSSET.getTimestamp("EX_OUTTM"))+" Hrs.";
						//JOptionPane.showMessageDialog(null,strBODY, "Verify", JOptionPane.INFORMATION_MESSAGE); 
						cl_eml ocl_eml = new cl_eml();
						//ocl_eml.sendfile(rstRSSET.getString("EP_EMLRF"),null,"Exit Pass Approval",strBODY);
						System.out.println(strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")");
						ocl_eml.setFRADR("systems_works@spl.co.in");
						ocl_eml.sendfile_ext(rstRSSET.getString("EP_EMLRF"),null,strEXITMSG,"");
						//ocl_eml.sendfile_ext("sr_deshpande@spl.co.in",null,strEXITMSG+"("+rstRSSET.getString("EP_EMLRF")+")","");
						
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
   
   
}
class hr_teapr_thrData extends Thread
{
   // Hashtable hstData;
    String strTERNM="",strDate="",strCMPCD= "";
    Connection conAXDBA = null;
    Statement stmAXDBA = null;
    Connection conSPDBA = null;
    Statement stmSPDBA = null;
    hr_teapr_thrData(String P_strTERNM,String strDate,Connection P_conSPDBA, String P_strCMPCD)
    {
        this.strTERNM = P_strTERNM;
       // this.hstData = hstData;
        this.strDate = strDate;
        this.conSPDBA = P_conSPDBA;
        this.strCMPCD = P_strCMPCD;
    }
    
    public void run()
    {
         java.sql.Timestamp L_tmsTEMP;
         String L_STRTIM ="",L_strTEMP ="";
         int intSAVED =0,intERR =0,intREC =0;
         String L_STRSQL="";
         String L_STRSQL1="";
         String L_strEMPNO="",L_strEMPCD="",L_strTERID="",L_strPNCTM="",L_strINOCD="",L_strTSLOGID="";
         boolean L_flgRUN = true;
         try
         {
         	try
			{
			   Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	           String  L_strURL = "jdbc:odbc:"+strTERNM;   // TNTTerminal102AXIS1
	           conAXDBA = DriverManager.getConnection(L_strURL, "monty", "some_pass");
				if(conAXDBA == null)
					return ;
				conAXDBA.setAutoCommit(false);
				stmSPDBA = conSPDBA.createStatement();
			}
			catch(java.lang.Exception L_EX)
			{
				System.out.println("Error in connecting to "+strTERNM);
			}
            if(conAXDBA == null)
            {
                System.out.println("Failed to Connect to Terminal.."+strTERNM);
                return; 
            }
            System.out.println(this.getName());
            System.out.println("Connected to Terminal.."+strTERNM);
            stmAXDBA = conAXDBA.createStatement();
            //String L_strTMP = "Select EmployeeCode,EmployeeNo,TSLogId,TerminalId,DateTimeStamp,a.userid usrid,ReasonCode,specialReason from TimeStampLog a,Employee b where a.UserId = b.userId and a.datetimestamp >= '"+ strDate +"'";
            //String L_strTMP = "Select EmployeeCode,EmployeeNo,TSLogId,TerminalId,DateTimeStamp,a.userid usrid,ReasonCode,specialReason from TimeStampLog a,Employee b where a.UserId = b.userId and a.datetimestamp >= '06/21/2006'";
            String L_strTMP = "Select EmployeeCode,EmployeeNo,TSLogId,TerminalId,DateTimeStamp,a.userid usrid,ReasonCode,specialReason from TimeStampLog a,Employee b where a.UserId = b.userId  order by a.datetimestamp desc";  // and a.datetimestamp >= '"+ strDate +"'
            System.out.println(L_strTMP);
            ResultSet L_rstRSSET = stmAXDBA.executeQuery(L_strTMP);
         //   ResultSet L_rstRSSET = stmAXDBA.executeQuery("Select EmployeeCode,EmployeeNo,TSLogId,TerminalId,DateTimeStamp,a.userid usrid,ReasonCode,specialReason from TimeStampLog a,Employee b where a.UserId = b.userId");
            if(L_rstRSSET !=null)
            {
                intREC=0;
                System.out.println("rs not null");
                while((L_rstRSSET.next())&& L_flgRUN)
                {
                    //System.out.println("data>>"+L_rstRSSET.getString("EmployeeCode")+" : "+L_rstRSSET.getString("EmployeeNo")+" : "+L_rstRSSET.getString("TSLogId")+" : "+L_rstRSSET.getString("TerminalId")+" : "+L_rstRSSET.getString("DateTimeStamp")+" : "+L_rstRSSET.getString("usrid")+" : "+L_rstRSSET.getString("ReasonCode")+" : "+L_rstRSSET.getString("specialReason"));
                    L_strTSLOGID = L_rstRSSET.getString("TSLogID");
                    L_strTEMP = L_rstRSSET.getString("DateTimeStamp");
                    L_strPNCTM = L_strTEMP.substring(0,10)+"-"+L_strTEMP.substring(11,13)+"."+L_strTEMP.substring(14,16)+"."+L_strTEMP.substring(17,19);
                    L_strEMPNO = L_rstRSSET.getString("EmployeeNo");
                    L_strEMPCD = L_rstRSSET.getString("EmployeeCode");
                    L_strTERID = L_rstRSSET.getString("terminalId");
                    L_strINOCD = L_rstRSSET.getString("ReasonCode");
                  //  hstData.put(L_strPNCTM+"|"+L_strEMPNO,L_strINOCD+"|"+L_strTERID);
                  // L_STRSQL = "insert into HR_EPALG(EPA_EMPNO,EPA_PNCTM,EPA_INOCD,EPA_TRMID,EPA_STSFL) values ("
                    //          + "'"+L_strEMPNO+"','"+L_strPNCTM+"','"+L_strINOCD+"','"+L_strTERID+"','0')";
					 L_STRSQL = "insert into HR_SLTRN(SLT_CMPCD,SLT_EMPNO,SLT_EMPCD,SLT_PNCTM,SLT_INOCD,SLT_TRMID,SLT_STSFL) values ("
                              + "'"+strCMPCD+"','"+L_strEMPNO+"','"+L_strEMPCD+"','"+L_strPNCTM+"','"+L_strINOCD+"','"+L_strTERID+"','1')";
					 L_STRSQL1 = "insert into HR_SLHST(SLT_CMPCD,SLT_EMPNO,SLT_EMPCD,SLT_PNCTM,SLT_INOCD,SLT_TRMID,SLT_STSFL) values ("
                              + "'"+strCMPCD+"','"+L_strEMPNO+"','"+L_strEMPCD+"','"+L_strPNCTM+"','"+L_strINOCD+"','"+L_strTERID+"','1')";
                    try
                    {
                        if(stmSPDBA.executeUpdate(L_STRSQL) >0)
                		{
							stmSPDBA.executeUpdate(L_STRSQL1);
                	    	conSPDBA.commit();
                			intSAVED++;
                			System.out.println(" Downloading from " +strTERNM+ " : Saved "+ intSAVED);
                		}
                		else
                		{
                		    conSPDBA.rollback();
                			System.out.println("Error");
                			intERR++;
                		}
                    }
                  	catch(SQLException L_E)
                	{
                	    conSPDBA.rollback();
                		System.out.println(strTERNM +" : "+intREC + " "+L_E.toString());
                        System.out.println("Error Code :"+L_E.getErrorCode());    
                        if(L_E.getErrorCode()== -803)
                            L_flgRUN = false;
                        System.out.println("Exiting "+strTERNM);    
                		intERR++;
                	}
                	intREC++;
                	
                }
            }
         }
         catch(Exception L_E)
         {
            System.out.println("error "+L_E.toString()+strTERNM);
         }
     }

}
