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
class ho_teapr extends JFrame implements ActionListener,Runnable
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
	private PreparedStatement pstmSPDBA;
	//private static Hashtable hstAXIS1,hstAXIS2,hstAXIS3,hstAXIS4,hstAXIS5,hstMAIN;
	private static String strDATE;
	private static Thread thrCONNECT;
	private static SimpleDateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");	/**	For Timestamp in DB format "yyyy-MM-dd-HH.mm" */
	private static SimpleDateFormat M_fmtDBDTM=new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	private static SimpleDateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");	/**	For Timestamp in Locale format "dd/MM/yyyy HH:mm" */
	private static SimpleDateFormat M_fmtLCTIM=new SimpleDateFormat("HH:mm");	/**	For Time in Locale format "HH:mm" */
	private static SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	private static Calendar M_calLOCAL=Calendar.getInstance();
	private static String M_strSBSCD ="";
	private static String M_strCMPCD ="02";
    ho_teapr()
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
		txtDATE.setText(strDATE);
      	}
	    catch(Exception E)
		{
            System.out.println("Inside Constructor"+E.toString());
        }

    }
    public void run()
    {
        try
        {
            conSPDBA=setCONDTB("01","SPLDATA","FIMS","FIMS");
            if(conSPDBA != null)
            {
                setMSG("Connected to Database..",'N');
            }
            if(conSPDBA == null)
            {
                setMSG("Failed to Connect to spltest..",'E');
                return; 
            }
            stmSPDBA = conSPDBA.createStatement();
            cstEMPCT = conSPDBA.prepareCall("{call hr_ephct_ho(?)}");
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
			L_rstRSSET.close();
        }
        catch(SQLException L_E)
        {
            System.out.println("Inside Run"+L_E.toString());
        }
    }
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
        ho_teapr oho_teapr = new ho_teapr();
        if(thrCONNECT!=null)
			thrCONNECT.join();
        ho_teapr_thrData thr1 = new ho_teapr_thrData(strDATE,conSPDBA);
        thr1.start();
        thr1.join();
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
			L_strTEMP ="UPDATE CO_CDTRN SET CMT_CHP01 = '"+L_strCURDT+" "+L_strCURTM+"' WHERE CMT_CGMTP ='S"+M_strCMPCD+"' AND CMT_CGSTP ='HRXXDTR' and cmt_codcd='01'";
            stmSPDBA.executeUpdate(L_strTEMP);
            conSPDBA.commit();
			}catch(Exception E){System.out.println("date time :"+E);}
            System.out.println("commit complte");
            System.out.println("calling stored procedure");
            cstEMPCT.executeUpdate();
            System.out.println("stored procedure called");
           // conSPDBA.close();
        }

        //System.out.println("hstMAin size "+hstMAIN.size());
		prcLOG();
        System.exit(0);
        //oho_teapr.show();
        }
        catch(Exception L_E)
        {
            System.out.println("Inside Main"+L_E.toString());
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
			System.out.println("Inside setCONDB"+"Error");
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
			System.out.println("Inside setMSG");
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
			Statement stmt = null;
			boolean L_flgERR = false;
			int L_intSRLNO =0;
            cstSWTRN = conSPDBA.prepareCall("{call updSWTRN_SLT_ho(?)}");
			cstSWTRN.setString(1,M_strCMPCD);
            cstSWTRN.executeUpdate();
			
				strSQLQRY = "DELETE FROM HO_SLTRN A WHERE A.SLT_CMPCD='"+M_strCMPCD+"' AND A.SLT_EMPNO||CHAR(A.SLT_PNCTM) NOT IN (SELECT B.SLT_EMPNO||MAX(CHAR(B.SLT_PNCTM)) FROM HO_SLTRN B WHERE B.SLT_CMPCD='"+M_strCMPCD+"' GROUP BY B.SLT_EMPNO)";				//System.out.println(strSQLQRY);	 				if (conSPDBA != null)
					if(stmSPDBA.executeUpdate(strSQLQRY)>0)
					{
						conSPDBA.commit();
						strSQLQRY = "UPDATE HO_SLTRN SET SLT_STSFL ='2'";
						if (conSPDBA != null)
						   if(stmSPDBA.executeUpdate(strSQLQRY)>0)
							  conSPDBA.commit();
						   else
							  conSPDBA.rollback();
					}
			 conSPDBA.commit();
		}
		catch(SQLException L_SEX)
	    {
			//L_flgERR = true;
			System.out.println("Inside prcLOG");
			System.out.println(" SQL "+L_SEX.toString());
	    }
	    catch(Exception L_EX)
	    {
			System.out.println(L_EX.toString());
	    }
	}
}
class ho_teapr_thrData extends Thread
{
   // Hashtable hstData;
    String strDate="",M_strCMPCD="02";
    Connection conAXDBA = null;
    Statement stmAXDBA = null;
    Connection conSPDBA = null;
    Statement stmSPDBA = null;
    ho_teapr_thrData(String strDate,Connection P_conSPDBA)
    {
        //this.strTERNM = P_strTERNM;
       // this.hstData = hstData;
        this.strDate = strDate;
        this.conSPDBA = P_conSPDBA;
    }
    public void run()
    {
         java.sql.Timestamp L_tmsTEMP;
         String L_STRTIM ="",L_strTEMP ="";
         int intSAVED =0,intERR =0,intREC =0;
         String L_STRSQL="";
         String L_STRSQL1="";
         String L_strTERID="",L_strTSLOGID="";
		 String L_strEMPNO="",L_strACCRF="",L_strPNCDT="",L_strPNCDTM="",L_strPNCDT1="",L_strPNCTM="",L_strPNCTM1="",L_strINOCD="";		 
		SimpleDateFormat fmtDBDATTM = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss"); 
		SimpleDateFormat fmtDBDATTM_YMD = new SimpleDateFormat("yyyy-MM-dd"); 
		SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("yyyyMMdd"); 
		SimpleDateFormat fmtHHMMSS = new SimpleDateFormat("HHmmss"); 
		SimpleDateFormat fmtHH_MM_SS = new SimpleDateFormat("HH.mm.ss"); 
         boolean L_flgRUN = true;
         try
         {
         	try
			{
			   Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			   conAXDBA = DriverManager.getConnection("jdbc:odbc:ho_teapr","","");
				if(conAXDBA == null)
					return ;
				conAXDBA.setAutoCommit(false);
				stmSPDBA = conSPDBA.createStatement();
			}
			catch(java.lang.Exception L_EX)
			{
				System.out.println("Error in connecting to Access Data");
			}
            if(conAXDBA == null)
            {
                System.out.println("Failed to Connect to Access Database");
                return; 
            }
            System.out.println(this.getName());
            System.out.println("Connected to Access Database..");
            stmAXDBA = conAXDBA.createStatement();
            //String L_strTMP = "Select EmployeeCode,EmployeeNo,TSLogId,TerminalId,DateTimeStamp,a.userid usrid,ReasonCode,specialReason from TimeStampLog a,Employee b where a.UserId = b.userId order by a.datetimestamp desc ";//and a.datetimestamp >= '"+ strDate +"'";
            //String L_strTMP = "Select distinct esabun ,edate ,etime ,einout  from history where edate > '20090110' order by edate desc, etime desc";//and a.datetimestamp >= '"+ strDate +"'";
			String L_strTMP = "Select distinct esabun ,edate ,etime ,einout  from history where edate > '20090101' order by edate desc, etime desc";//and a.datetimestamp >= '"+ strDate +"'";
            //System.out.println(L_strTMP);
            ResultSet L_rstRSSET = stmAXDBA.executeQuery(L_strTMP);
            if(L_rstRSSET !=null)
            {
                intREC=0;
                //System.out.println("rs not null");
                while((L_rstRSSET.next())&& L_flgRUN)
                {
                    //L_strTSLOGID = L_rstRSSET.getString("TSLogID");
                    L_strACCRF = L_rstRSSET.getString("esabun").toUpperCase();
                    L_strPNCDT = L_rstRSSET.getString("edate");
                    L_strPNCTM = L_rstRSSET.getString("etime");
					L_strPNCDT1 = fmtDBDATTM_YMD.format(fmtYYYYMMDD.parse(L_strPNCDT));
					L_strPNCTM1 = fmtHH_MM_SS.format(fmtHHMMSS.parse(L_strPNCTM));
					L_strPNCDTM = L_strPNCDT1+"-"+L_strPNCTM1;
                    //L_strPNCTM = L_strTEMP.substring(0,10)+"-"+L_strTEMP.substring(11,13)+"."+L_strTEMP.substring(14,16)+"."+L_strTEMP.substring(17,19);
                    L_strINOCD = L_rstRSSET.getString("einout");
					if(L_strACCRF.trim().length() != 6)
						continue;
					//System.out.println(L_strEMPNO+"  "+L_strPNCDT+"   "+L_strPNCTM+"   "+L_strPNCDTM+"  "+L_strINOCD);
                    try
                    {
					   L_STRSQL = "insert into HO_SLTRN(SLT_CMPCD,SLT_EMPNO,SLT_ACCRF,SLT_PNCTM,SLT_INOCD,SLT_TRMID,SLT_STSFL) values ("
								 + "'"+M_strCMPCD+"','XXXX','"+L_strACCRF+"','"+L_strPNCDTM+"','"+(L_strINOCD.trim().equalsIgnoreCase("IN") ? "0" : "1")+"','X','1')";
					    System.out.println(L_STRSQL);
                        if(stmSPDBA.executeUpdate(L_STRSQL) >0)
                		{
                	    	conSPDBA.commit();
                			intSAVED++;
                			//System.out.println(" Downloading from  Access Database : Saved "+ intSAVED);
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
                		System.out.println(" : "+intREC + " "+L_E.toString());
                        System.out.println("Error Code :"+L_E.getErrorCode());    
                        if(L_E.getErrorCode()== -803)
                            L_flgRUN = false;
                        System.out.println("Exiting Access Database");    
                		intERR++;
                	}
                	intREC++;
                	
                }
				L_STRSQL = "update ho_sltrn a set slt_empno = (select b.ep_empno from hr_epmst b where a.slt_cmpcd=b.ep_cmpcd and a.slt_accrf = b.ep_accrf) where a.slt_empno='XXXX' and a.slt_accrf in (select ifnull(c.ep_accrf,'') from hr_epmst c where c.ep_cmpcd='"+M_strCMPCD+"')";
				 //System.out.println(L_STRSQL);
                 if(stmSPDBA.executeUpdate(L_STRSQL) >0);
                 	conSPDBA.commit();
            }
         }
         catch(Exception L_E)
         {
			System.out.println("Inside run");
            System.out.println("error "+L_E.toString());
         }
     }

}
