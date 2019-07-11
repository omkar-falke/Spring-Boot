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
	private static SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	
	private static Calendar M_calLOCAL=Calendar.getInstance();
	private static String M_strSBSCD ="010100";
	private static String M_strCMPCD ="01";
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
		strDATE = "17/11/2008";
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
			System.out.println("inside constructor : "+E.toString());
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
				//strDATE = "11/09/2008";
                txtDATE.setText(strDATE);
            }
        }
        catch(SQLException L_E)
        {
			System.out.println("inside run() : "+L_E.toString());
        }
    }
 public void actionPerformed(ActionEvent L_AE)
{
	 try
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
    catch(Exception L_E)
    {
		System.out.println("inside actionPerformed() : "+L_E.toString());
    }
}
    public static void main(String args[])
    {
        try
        {
        hr_teapr ohr_teapr = new hr_teapr();
        if(thrCONNECT!=null)
			thrCONNECT.join();
        hr_teapr_thread thr1 = new hr_teapr_thread("TNTTerminal102AXIS1",strDATE,conSPDBA);
		hr_teapr_thread thr2 = new hr_teapr_thread("TNTTerminal102AXIS2",strDATE,conSPDBA);
        hr_teapr_thread thr3 = new hr_teapr_thread("TNTTerminal102AXIS3",strDATE,conSPDBA);
        hr_teapr_thread thr4 = new hr_teapr_thread("TNTTerminal102AXIS4",strDATE,conSPDBA);
        thr1.start();
        thr2.start();
        thr3.start();
		thr4.start();
        thr1.join();
		thr2.join();
		thr3.join();
		thr4.join();
       /* System.out.println("hst1 size "+hstAXIS1.size());
        System.out.println("hst3 size "+hstAXIS3.size());
        System.out.println("hst4 size "+hstAXIS4.size());
        hstMAIN = new Hashtable();
        hstMAIN.putAll((Map)hstAXIS1);
        hstMAIN.putAll((Map)hstAXIS3);
        hstMAIN.putAll((Map)hstAXIS4);*/
        if(conSPDBA !=null)
        {
            String L_STRTMP ="UPDATE CO_CDTRN SET CMT_CHP01 = current_date WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='HRXXDTR'";
            stmSPDBA.executeUpdate(L_STRTMP);
            conSPDBA.commit();
            System.out.println("commit complte");
            System.out.println("calling stored procedure");
            cstEMPCT.executeUpdate();
            System.out.println("stored procedure called");
           // conSPDBA.close();
        }

        //System.out.println("hstMAin size "+hstMAIN.size());
	prcLOG();
        System.exit(0);
        //ohr_teapr.show();
        }
        catch(Exception L_E)
        {
			System.out.println("inside main() : "+L_E.toString());
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
			System.out.println("inside setCONDTB() : "+L_EX);
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
			System.out.println("inside setMSG() : "+e);
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
			strSQLQRY = "Select SLT_EMPNO,SLT_PNCTM,DATE(SLT_PNCTM)L_WRKDT,SLT_INOCD,SLT_STSFL from HR_SLTRN WHERE SLT_CMPCD='"+M_strCMPCD+"' AND SLT_STSFL ='1'";
			//strSQLQRY += "and slt_empno in('5004')";
			strSQLQRY += " ORDER BY SLT_EMPNO,SLT_PNCTM,SLT_INOCD ";//and slt_inocd ='0' and slt_empno in('5003','5004') 
			
			if (conSPDBA != null)
			 stmt = conSPDBA.createStatement(
                                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                                      ResultSet.CONCUR_UPDATABLE);
			ResultSet rstRSSET = stmt.executeQuery(strSQLQRY);
			//System.out.println("strSQLQRY>>"+strSQLQRY); 
			int L_CNT =0;
			int L_SAVECNT =0;
			 if(rstRSSET!=null)
			 while(rstRSSET.next())
			 {
				System.out.println("Fetched "+ L_CNT);	
				 L_CNT++;
				 strEMPNO = rstRSSET.getString("SLT_EMPNO");
				 strINOST = rstRSSET.getString("SLT_INOCD");
				 datWRKDT = rstRSSET.getDate("L_WRKDT");
				 tmsPNCTM = rstRSSET.getTimestamp("SLT_PNCTM");
				 //System.out.println("tmsPNCTM>>>>"+tmsPNCTM);
				 if(strWRKDT !=null)
				 {
					 strWRKDT = M_fmtLCDAT.format(datWRKDT);
				 }
				 if(strPNCTM !=null)
				 {
					 strPNCTM = M_fmtLCDTM.format(tmsPNCTM);
					 if(strINOST.equals("0")) // IN Time
					 {
						 if((Time.valueOf(strPNCTM.substring(11,16)+":00").compareTo(Time.valueOf("00:01:00"))>0)&&(Time.valueOf(strPNCTM.substring(11,16)+":00").compareTo(Time.valueOf("05:00:00"))<0))
						 {
							// System.out.println("found  IN "+strPNCTM + "date " +strWRKDT);
							 M_calLOCAL.setTime(datWRKDT);
							 M_calLOCAL.add(Calendar.DATE,- 1); 
							 strWRKDT =  M_fmtLCDAT.format(M_calLOCAL.getTime());
							 System.out.println("time / worked out date for IN "+strPNCTM + "date " +strWRKDT);
						 }
					 }
					 else if(strINOST.equals("1")) // Out Time
					 {
						 if((Time.valueOf(strPNCTM.substring(11,16)+":00").compareTo(Time.valueOf("00:01:00"))>0)&&(Time.valueOf(strPNCTM.substring(11,16)+":00").compareTo(Time.valueOf("08:00:00"))<0))
						 {
							 M_calLOCAL.setTime(datWRKDT);
							 M_calLOCAL.add(Calendar.DATE,- 1); 
							 strWRKDT =  M_fmtLCDAT.format(M_calLOCAL.getTime());
							 System.out.println("time / worked out date for OUT "+strPNCTM + "date " +strWRKDT);
						 }
					 }
				 }
				 strINOST = rstRSSET.getString("SLT_INOCD");
				 //System.out.println("strINOST>>>>>>"+strINOST);
				if(strINOST.equals("0")) // In Punch
				{
					if(!strPEMPNO.equals(strEMPNO))// emp no changes
					{
						strSRLNO ="1";	
						strPEMPNO = strEMPNO;
						strPWRKDT = strWRKDT;
					}
					else
					{
						 // emp no. same, work date different, check the max srl no. with/without empty slot
						 if(!strPWRKDT.equals(strWRKDT))
						{
							//strSRLNO ="1";	 ///////
							 strSQLQRY = "SELECT ifnull(MAX(SWT_SRLNO),0)L_SRLNO FROM HR_SWTRN WHERE SWT_CMPCD='"+M_strCMPCD+"' AND SWT_EMPNO ='"+strEMPNO +"' AND SWT_WRKDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"' " ;
										L_rstRSSET = stmSPDBA.executeQuery(strSQLQRY);
										if(L_rstRSSET !=null)
										{
										if(L_rstRSSET.next())
										{
											L_strSRLNO = L_rstRSSET.getString("L_SRLNO");
										}
										  L_rstRSSET.close();
										}
										strSRLNO = Integer.parseInt(L_strSRLNO) + 1+"";	 
								////System.out.println("xxxxxx");	
							strPWRKDT = strWRKDT;
						}
						else 
						{
							strSRLNO = Integer.parseInt(strSRLNO) + 1+"";	 
						}
					}
					try
					 {
							strSQLQRY = "INSERT INTO HR_SWTRN(SWT_CMPCD,SWT_SBSCD,SWT_EMPNO,SWT_WRKDT,SWT_SRLNO,SWT_INCTM,SWT_OUTTM,SWT_TRNFL,SWT_STSFL,SWT_LUSBY,SWT_LUPDT) values("
							+"'"+M_strCMPCD+"'," 
							+"'"+M_strSBSCD+"'," 
							+"'"+strEMPNO+"'," 
							+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"'," 	  
							+"'"+strSRLNO+"'," 	  				  
							+"'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strPNCTM))+"'," 	
							+"null," 	  
							+ "'0','0','SYS',CURRENT_DATE)"	;	
							if (conSPDBA != null)
									if(stmSPDBA.executeUpdate(strSQLQRY)>0)
									{
										conSPDBA.commit();
										L_SAVECNT++;
									}
							//System.out.println("strSQLQRY>>>"+strSQLQRY);
					 }
					 catch(Exception L_SE)
					 {
						  L_flgERR = true;
						  System.out.println("error 1" + L_SE.toString()+strSQLQRY);
						  conSPDBA.rollback();
					 }
				}
				else // if it is out punch
				{
					// check for the empty slot
					strSQLQRY = "SELECT ifnull(MIN(SWT_SRLNO),0)L_SRLNO FROM HR_SWTRN WHERE SWT_CMPCD='"+M_strCMPCD+"' AND SWT_EMPNO ='"+strEMPNO +"' AND SWT_WRKDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"' AND SWT_STSFL ='0'" ;
					strSQLQRY +="having ifnull(MIN(SWT_SRLNO),0) >0";
					L_rstRSSET = stmSPDBA.executeQuery(strSQLQRY);
					if(L_rstRSSET !=null)
					{
						if(L_rstRSSET.next())
						{
						   //System.out.println("inside empty slot");
						   L_strSRLNO = L_rstRSSET.getString("L_SRLNO");
						   //System.out.println("L_strSRLNO>>"+L_strSRLNO);
						   if(L_rstRSSET.getInt("L_SRLNO") != 0)
						   {
								if(!strPEMPNO.equals(strEMPNO))// emp no changes
								{
									strPEMPNO = strEMPNO;
								}
								else
								{
									// emp no. same
									if(!strPWRKDT.equals(strWRKDT))
									{
										strPWRKDT = strWRKDT;
									}
									else 
									{
										//	strSRLNO = Integer.parseInt(strSRLNO) + 1+"";	 
									}
								}
								strSQLQRY = "UPDATE HR_SWTRN SET SWT_OUTTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strPNCTM))+"',SWT_STSFL ='1' " ;	
								strSQLQRY += " where SWT_CMPCD='"+M_strCMPCD+"' AND SWT_EMPNO ='"+strEMPNO +"' AND swT_wrkdt ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"'" ;
								strSQLQRY += " AND SWT_SRLNO = '"+ L_strSRLNO +"'";	
								
								if(stmSPDBA.executeUpdate(strSQLQRY)>0)
								{
									L_SAVECNT++;
									conSPDBA.commit();
								}
							}
							/*else
							{
								 if(!strPEMPNO.equals(strEMPNO))// emp no changes
								 {
									strSRLNO ="1";	
									strPEMPNO = strEMPNO;
								 }
								else // 
								{
									// emp no. same
									 if(!strPWRKDT.equals(strWRKDT))
									{
										strSRLNO ="1";	 
										strPWRKDT = strWRKDT;
									}
									else 
									{
										//strSQLQRY = "SELECT ifnull(MAX(SWT_SRLNO),0)L_SRLNO FROM HR_SWTRN WHERE SWT_EMPNO ='"+strEMPNO +"' AND SWT_WRKDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"' " ;
										//strSQLQRY +="having ifnull(MIN(SWT_SRLNO),0) >0";
										L_rstRSSET = stmSPDBA.executeQuery(strSQLQRY);
										if(L_rstRSSET !=null)
										if(L_rstRSSET.next())
										{
											L_strSRLNO = L_rstRSSET.getString("L_SRLNO");
										}
										strSRLNO = Integer.parseInt(L_strSRLNO) + 1+"";	 
									}
								}
								 try
								 {
								// insert a new record 
								 /*strSQLQRY = "INSERT INTO HR_SWTRN(SWT_SBSCD,SWT_EMPNO,SWT_WRKDT,SWT_SRLNO,SWT_INCTM,SWT_OUTTM,SWT_TRNFL,SWT_STSFL,SWT_LUSBY,SWT_LUPDT) values("
								  +"'"+M_strSBSCD+"'," 
								  +"'"+strEMPNO+"'," 
								  +"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"'," 	  
								  +"'"+strSRLNO+"'," 	  				  
								  +"null," 	  
								  +"'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strPNCTM))+"'," 	
								  + "'0','0','SYS',CURRENT_DATE)"	;	
								
								  if (conSPDBA != null)
									if(stmSPDBA.executeUpdate(strSQLQRY)>0)
									{
										//rstRSSET.updateString("SLT_STSFL","2");
										//rstRSSET.updateRow();
										conSPDBA.commit();
										L_SAVECNT++;
									}
								   L_rstRSSET.close();
								 
								}
								catch(Exception L_SE)
								{	
										L_flgERR = true;
									   System.out.println("error 2" + L_SE.toString()+strSQLQRY);
									  conSPDBA.commit();
								}
							}*/
							 L_rstRSSET.close();
						}// empty slot not found for updating the out time
						else
						{
							 if(!strPEMPNO.equals(strEMPNO))// emp no changes
							 {
								strSRLNO ="1";	
								strPEMPNO = strEMPNO;
								strPWRKDT = strWRKDT;
							 }
							else
							{
								//System.out.println("in1");
								if(!strPWRKDT.equals(strWRKDT))
								{
									//System.out.println("in2");
									////strSRLNO ="1";	///// 
									strPWRKDT = strWRKDT;
								}
								else
								{	
									//System.out.println("in3");
								strSQLQRY = "SELECT ifnull(MAX(SWT_SRLNO),0)L_SRLNO FROM HR_SWTRN WHERE SWT_CMPCD='"+M_strCMPCD+"' AND SWT_EMPNO ='"+strEMPNO +"' AND SWT_WRKDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"' " ;
										//strSQLQRY +="having ifnull(MIN(SWT_SRLNO),0) >0";
										L_rstRSSET = stmSPDBA.executeQuery(strSQLQRY);
										if(L_rstRSSET !=null)
										{
										if(L_rstRSSET.next())
										{
											L_strSRLNO = L_rstRSSET.getString("L_SRLNO");
										}
										  L_rstRSSET.close();
										}
										//System.out.println("strSRLNO1>>"+strSRLNO);
										strSRLNO = Integer.parseInt(L_strSRLNO) + 1+"";	 
										//System.out.println("strSRLNO2>>"+strSRLNO);
								//System.out.println("aaaaa");	
								// emp no. same
								}
								
							}
							// insert a new record 
							 try
							 {
								 //System.out.println("M_fmtDBDTM.format(M_fmtLCDTM.parse(strPNCTM))>>>>"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strPNCTM)));
							 strSQLQRY = "INSERT INTO HR_SWTRN(SWT_CMPCD,SWT_SBSCD,SWT_EMPNO,SWT_WRKDT,SWT_SRLNO,SWT_INCTM,SWT_OUTTM,SWT_TRNFL,SWT_STSFL,SWT_LUSBY,SWT_LUPDT) values("
							  +"'"+M_strCMPCD+"'," 
							  +"'"+M_strSBSCD+"'," 
							  +"'"+strEMPNO+"'," 
							  +"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"'," 	  
							  +"'"+strSRLNO+"'," 	  				  
							  +"null," 	  
							  +"'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strPNCTM))+"'," 	
							  + "'0','1','SYS',CURRENT_DATE)"	;	
							
							  if (conSPDBA != null)
									if(stmSPDBA.executeUpdate(strSQLQRY)>0)
									{
										//rstRSSET.updateString("SLT_STSFL","2");
										//rstRSSET.updateRow();
										conSPDBA.commit();
										L_SAVECNT++;
									}
							 }
							 catch(SQLException L_SE)
							 {
								   L_flgERR = true;
								   System.out.println("error 3" + L_SE.toString()+strSQLQRY);
							 }
						}
					}
					
				}
			 }
			 //System.out.println(L_flgERR);
			 //if(!L_flgERR)
			 {
				System.out.println("delete ");	 
				strSQLQRY = "DELETE FROM HR_SLTRN A WHERE A.SLT_CMPCD='"+M_strCMPCD+"' AND A.SLT_EMPNO||CHAR(A.SLT_PNCTM) NOT IN (SELECT B.SLT_EMPNO||MAX(CHAR(B.SLT_PNCTM)) FROM HR_SLTRN B WHERE B.SLT_CMPCD='"+M_strCMPCD+"' GROUP BY B.SLT_EMPNO)";
				System.out.println(strSQLQRY);	 				if (conSPDBA != null)
					if(stmSPDBA.executeUpdate(strSQLQRY)>0)
					{
						conSPDBA.commit();
						strSQLQRY = "UPDATE HR_SLTRN SET SLT_STSFL ='2'";
						System.out.println(strSQLQRY);	 						if (conSPDBA != null)
						if(stmSPDBA.executeUpdate(strSQLQRY)>0)
							conSPDBA.commit();
						else
							conSPDBA.rollback();
					}
					else
						conSPDBA.rollback();
				
			 }
			 System.out.println("Fetched / saved "+ L_CNT +" : " +L_SAVECNT);
			 conSPDBA.commit();
		}
		catch(SQLException L_SEX)
	    {
			//L_flgERR = true;
			System.out.println("inside prcLOG1() :"+L_SEX.toString());
	    }
	    catch(Exception L_EX)
	    {
			System.out.println("inside prcLOG2() :"+L_EX.toString());
	    }
	}
}
class hr_teapr_thread extends Thread
{
   // Hashtable hstData;
    String strTERNM="",strDate="",M_strCMPCD="01";
    Connection conAXDBA = null;
    Statement stmAXDBA = null;
    Connection conSPDBA = null;
    Statement stmSPDBA = null;
    hr_teapr_thread(String P_strTERNM,String strDate,Connection P_conSPDBA)
    {
        this.strTERNM = P_strTERNM;
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
				System.out.println("run() : Error in connecting to "+strTERNM);
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
                  //  hstData.put(L_strPNCTM+"|"+L_strEMPNO,L_strINOCD+"|"+L_strTERID);
                  // L_STRSQL = "insert into HR_EPALG(EPA_EMPNO,EPA_PNCTM,EPA_INOCD,EPA_TRMID,EPA_STSFL) values ("
                    //          + "'"+L_strEMPNO+"','"+L_strPNCTM+"','"+L_strINOCD+"','"+L_strTERID+"','0')";
					L_STRSQL = "insert into HR_SLTRN(SLT_CMPCD,SLT_EMPNO,SLT_PNCTM,SLT_INOCD,SLT_TRMID,SLT_STSFL) values ("
                              + "'"+M_strCMPCD+"','"+L_strEMPNO+"','"+L_strPNCTM+"','"+L_strINOCD+"','"+L_strTERID+"','1')";
                    try
                    {
                        if(stmSPDBA.executeUpdate(L_STRSQL) >0)
                		{
							//stmSPDBA.executeUpdate(L_STRSQL1);
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
						System.out.println("run() : "+strTERNM +" : "+intREC + " "+L_E.toString());
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
			 System.out.println("run() : "+L_E.toString()+strTERNM);
         }
     }

}
