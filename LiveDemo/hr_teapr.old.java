import javax.swing.*;
import java.io.IOException;import java.io.File;import java.io.RandomAccessFile;
import java.util.Hashtable;
import java.util.Vector;import java.util.StringTokenizer;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.Color;import java.awt.Container;import java.awt.Toolkit;
import java.awt.Cursor;import java.awt.Component;
import java.sql.*;
import java.sql.CallableStatement;
import java.util.Map;
class hr_teapr extends JFrame implements ActionListener,Runnable
{
   	private File file;
	private RandomAccessFile file_io;
	private JButton btnPROCS,btnSTOP,btnEXIT;
	private JLabel lblMESG;
	private static JTextField txtDATE;
	private boolean FLAG = true;
	private static CallableStatement cstEMPCT;
	private cl_pbase ocl_pbase;
	private static Connection conSPDBA;
	private static Statement stmSPDBA;
	private PreparedStatement pstmSPDBA;
	private static Hashtable hstAXIS1,hstAXIS2,hstAXIS3,hstAXIS4,hstAXIS5,hstMAIN;
	private static String strDATE;
	private static Thread thrCONNECT;
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
			L_ctrTHIS.add(ocl_pbase);
		    setSize(800,200);
			show();
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
        hstAXIS1 = new Hashtable();
        hstAXIS2 = new Hashtable();
        hstAXIS3 = new Hashtable();
        hstAXIS4 = new Hashtable();
        hstAXIS5 = new Hashtable();
        hstMAIN = new Hashtable();
      	}
	    catch(Exception E)
		{
            System.out.println(E.toString());
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
            cstEMPCT = conSPDBA.prepareCall("{call hr_ephct()}");
            String L_strTEMP = "SELECT CMT_CHP01 from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='HRXXDTR' AND CMT_CODCD ='01'";
            String L_strDATE;
            ResultSet L_rstRSSET = stmSPDBA.executeQuery(L_strTEMP);
            if(L_rstRSSET !=null)
            if(L_rstRSSET.next())
            {
                L_strTEMP = L_rstRSSET.getString("CMT_CHP01");
                strDATE = L_strTEMP.substring(3,6)+L_strTEMP.substring(0,3)+"20"+L_strTEMP.substring(6);
                txtDATE.setText(strDATE);
            }
        }
        catch(SQLException L_E)
        {
            System.out.println(L_E.toString());
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
        hr_teapr ohr_teapr = new hr_teapr();
        if(thrCONNECT!=null)
			thrCONNECT.join();
        thrData thr1 = new thrData("TNTTerminal102AXIS1",hstAXIS1,strDATE,conSPDBA);
        thrData thr2 = new thrData("TNTTerminal102AXIS3",hstAXIS3,strDATE,conSPDBA);
        thrData thr3 = new thrData("TNTTerminal102AXIS4",hstAXIS4,strDATE,conSPDBA);
        thr1.start();
        thr2.start();
        thr3.start();
        thr1.join();thr2.join();thr3.join();
        System.out.println("hst1 size "+hstAXIS1.size());
        System.out.println("hst3 size "+hstAXIS3.size());
        System.out.println("hst4 size "+hstAXIS4.size());
        hstMAIN = new Hashtable();
        hstMAIN.putAll((Map)hstAXIS1);
        hstMAIN.putAll((Map)hstAXIS3);
        hstMAIN.putAll((Map)hstAXIS4);
        if(conSPDBA !=null)
        {
            String L_STRTMP ="UPDATE CO_CDTRN SET CMT_CHP01 = current_date WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='HRXXDTR'";
            stmSPDBA.executeUpdate(L_STRTMP);
            conSPDBA.commit();
            System.out.println("commit complte");
            System.out.println("calling stored procedure");
            cstEMPCT.executeUpdate();
            System.out.println("stored procedure called");
            conSPDBA.close();
        }

        System.out.println("hstMAin size "+hstMAIN.size());
        System.exit(0);
        //ohr_teapr.show();
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

}
class thrData extends Thread
{
    Hashtable hstData;
    String strTERNM="",strDate="";
    Connection conAXDBA = null;
    Statement stmAXDBA = null;
    Connection conSPDBA = null;
    Statement stmSPDBA = null;
    thrData(String P_strTERNM,Hashtable hstData,String strDate,Connection P_conSPDBA)
    {
        this.strTERNM = P_strTERNM;
        this.hstData = hstData;
        this.strDate = strDate;
        this.conSPDBA = P_conSPDBA;
    }
    public void run()
    {
         java.sql.Timestamp L_tmsTEMP;
         String L_STRTIM ="",L_strTEMP ="";
         int intSAVED =0,intERR =0,intREC =0;
         String L_STRSQL="";
         String L_strEMPNO="",L_strTERID="",L_strPNCTM="",L_strINOCD="",L_strTSLOGID="";
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
            String L_strTMP = "Select EmployeeCode,EmployeeNo,TSLogId,TerminalId,DateTimeStamp,a.userid usrid,ReasonCode,specialReason from TimeStampLog a,Employee b where a.UserId = b.userId order by a.datetimestamp desc ";//and a.datetimestamp >= '"+ strDate +"'";
            System.out.println(L_strTMP);
            ResultSet L_rstRSSET = stmAXDBA.executeQuery(L_strTMP);
         //   ResultSet L_rstRSSET = stmAXDBA.executeQuery("Select EmployeeCode,EmployeeNo,TSLogId,TerminalId,DateTimeStamp,a.userid usrid,ReasonCode,specialReason from TimeStampLog a,Employee b where a.UserId = b.userId");
            if(L_rstRSSET !=null)
            {
                intREC=0;
                System.out.println("rs not null");
                while((L_rstRSSET.next())&& L_flgRUN)
                {
                    L_strTSLOGID = L_rstRSSET.getString("TSLogID");
                    L_strTEMP = L_rstRSSET.getString("DateTimeStamp");
                    L_strPNCTM = L_strTEMP.substring(0,10)+"-"+L_strTEMP.substring(11,13)+"."+L_strTEMP.substring(14,16)+"."+L_strTEMP.substring(17,19);
                    L_strEMPNO = L_rstRSSET.getString("EmployeeNo");
                    L_strTERID = L_rstRSSET.getString("terminalId");
                    L_strINOCD = L_rstRSSET.getString("ReasonCode");
                    hstData.put(L_strPNCTM+"|"+L_strEMPNO,L_strINOCD+"|"+L_strTERID);
                    L_STRSQL = "insert into HR_EPALG(EPA_EMPNO,EPA_PNCTM,EPA_INOCD,EPA_TRMID,EPA_STSFL) values ("
                              + "'"+L_strEMPNO+"','"+L_strPNCTM+"','"+L_strINOCD+"','"+L_strTERID+"','0')";
                    try
                    {
                        if(stmSPDBA.executeUpdate(L_STRSQL) >0)
                		{
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
