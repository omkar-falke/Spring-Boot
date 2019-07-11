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
class hr_hkapr extends cl_pbase implements ActionListener,Runnable
{
   	private File file;
	private RandomAccessFile file_io;
	private JButton btnPROCS,btnSTOP,btnEXIT;
	private JLabel lblMESG;
	private hr_teexr objTEEXR;
	private static JTextField txtDATE;
	private boolean FLAG = true;
	private static CallableStatement cstEMPCT;
	//private cl_pbase ocl_pbase;
	private static Connection conSPDBA;
	private static Statement stmSPDBA;
	private PreparedStatement pstmSPDBA;
	//private static Hashtable hstAXIS1,hstAXIS2,hstAXIS3,hstAXIS4,hstAXIS5,hstMAIN;
	private static String strDATE;
	private static Thread thrCONNECT;
	private static SimpleDateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");	/**	For Timestamp in DB format "yyyy-MM-dd-HH.mm" */
	private static SimpleDateFormat M_fmtDBDTM=new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	private static SimpleDateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");	/**	For Timestamp in Locale format "dd/MM/yyyy HH:mm" */
	private static SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	private static Calendar M_calLOCAL=Calendar.getInstance();
	private String M_strSBSCD = "";

    private static String	strEXEDRV_st = "",
							strSYSLC_st  = "01",
							strSYSLC0_st = "01",
							strCONTP_st = "";
							/** For getting the printer list	 */
	private String	M_strSPLBA_pb_st = "",
					M_strSPUSA_pb_st = "",
					M_strSPPWA_pb_st = "",
					M_strSPDNA_pb_st = "",
					M_strSPDPA_pb_st = "";/** Global Miscellaneous String variable */
	private String strCURLC;
	
	hr_hkapr()
    {
        //super("Attendance Processing");
        super();
        try
        {
            thrCONNECT = new Thread(this);
		    thrCONNECT.start();
            cl_dat.M_dimSCRN_pbst = Toolkit.getDefaultToolkit().getScreenSize();
			cl_dat.M_dblWIDTH=(cl_dat.M_dimSCRN_pbst.width/800.00);
			cl_dat.M_dblHIGHT=((cl_dat.M_dimSCRN_pbst.height)/600.00);
            //ocl_pbase = new cl_pbase();
            this.setMatrix(20,8);
            this.add(new JLabel("Transfer Date"),2,1,1,1,this,'L');
            this.add(txtDATE = new TxtLimit(10),2,2,1,1,this,'L');
            this.add(btnPROCS = new JButton("Process"),2,3,1,1,this,'L');
            this.add(btnSTOP = new JButton("STOP"),2,4,1,1,this,'L');
            this.add(btnEXIT = new JButton("EXIT"),2,5,1,1,this,'L');
            this.add(lblMESG = new JLabel(" "),4,1,1,8,this,'L');
            //Container L_ctrTHIS=this.getContentPane();
			M_fmtDBDAT.setLenient(false);
			M_fmtDBDTM.setLenient(false);
			M_fmtLCDAT.setLenient(false);
			M_fmtLCDTM.setLenient(false);
			M_calLOCAL.setLenient(false);
			//L_ctrTHIS.add(this);
		    setSize(800,200);
			this.setVisible(true);
			//show();
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Component L_cmpTEMP=null;
			//Unregister listners of cl_pbase and register local listeners
			for(int i=0;i<this.M_vtrSCCOMP.size();i++)
			{
				L_cmpTEMP=(Component)this.M_vtrSCCOMP.elementAt(i);
				L_cmpTEMP.removeKeyListener(this);
				if(L_cmpTEMP instanceof JTextField)
				{
					((JTextField)L_cmpTEMP).removeActionListener(this);
					((JTextField)L_cmpTEMP).removeFocusListener(this);
				}
				else if(L_cmpTEMP instanceof AbstractButton)
				{
					((AbstractButton)L_cmpTEMP).removeActionListener(this);
					((AbstractButton)L_cmpTEMP).removeFocusListener(this);
				}
				else if(L_cmpTEMP instanceof JComboBox)
				{
					((JComboBox)L_cmpTEMP).removeActionListener(this);
					L_cmpTEMP.removeFocusListener(this);
				}
			}
			btnPROCS.addActionListener(this);btnSTOP.addActionListener(this);btnEXIT.addActionListener(this);
			L_cmpTEMP=null;
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
			cl_dat.M_flgTSTDT_pbst = false;
			M_strSPUSA_pb_st = "FIMS";
			M_strSPPWA_pb_st = "FIMS";
			M_strSPLBA_pb_st = "SPLDATA";
			M_strSPDNA_pb_st = "SPLDATA";
			if(strCONTP_st.equalsIgnoreCase("TST"))
			{
				cl_dat.M_flgTSTDT_pbst = true;
				System.out.println("You are entering into Test database zone");
				M_strSPLBA_pb_st = "SPLTEST";
				M_strSPDNA_pb_st = "SPLTEST";
			}
			setCONACT(strSYSLC0_st,M_strSPLBA_pb_st,M_strSPUSA_pb_st,M_strSPPWA_pb_st);
			strCURLC=strSYSLC0_st;
			cl_dat.M_strCMPCD_pbst ="01";
			cl_dat.M_strCMPNM_pbst = "Supreme Petrochem Ltd.";
            String L_strTEMP = "SELECT current_date CMT_CURDT from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='HRXXDTR' AND CMT_CODCD ='01'";
            String L_strDATE;
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strTEMP);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
				    L_strDATE = M_fmtLCDAT.format(L_rstRSSET.getDate("CMT_CURDT"));
					M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strDATE));      
					M_calLOCAL.add(Calendar.DATE,-1);    
					txtDATE.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				}
			}
			System.out.println(txtDATE.getText());
			hr_teexr objTEEXR = new hr_teexr("XYZ");
			//M_strSBSCD="010000";
			//objTEEXR.cl_dat.M_strUSRCD_pbst = "HBP";
			prcDATA(txtDATE.getText(),txtDATE.getText());
        }
        catch(Exception L_E)
        {
            System.out.println(L_E.toString());
        }
    }

	
	
	void prcDATA(String LP_STRDT, String LP_ENDDT)
	{
		System.out.println("LP_STRDT>>>>>"+LP_STRDT);
		System.out.println("LP_ENDDT>>>>>"+LP_ENDDT);
		System.out.println(M_strSBSCD);
		System.out.println(objTEEXR.hstLVDTL);
		String M_strSQLQRY1="",M_strSQLQRY2="";
		ResultSet rstRSSET1,rstRSSET2;
		boolean L_flgUPD;
		int L_intCNT=0;					
		int L_intCNT1=0;					
		try
		{

			String L_strWHRSTR_MST = " substr(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U'";
			String L_strWHRSTR_SS = " ss_wrkdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and substr(SS_SBSCD,1,2) = '"+M_strSBSCD.substring(0,2)+"' and SS_STSFL <> 'X' ";
			String L_strWHRSTR_SW = " sw_wrkdt  between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  and substr(SW_SBSCD,1,2) = '"+M_strSBSCD.substring(0,2)+"'";
			String L_strWHRSTR_AL1 = " ifnull(swt_sbscd,' ') = '"+ M_strSBSCD+"' and swt_inctm is not null and  swt_wrkdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  and swt_stsfl not in ('2','X')";
			String L_strWHRSTR_AL2 = " ifnull(swt_sbscd,' ') = '"+ M_strSBSCD+"' and swt_outtm is not null and  swt_wrkdt  between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  and swt_stsfl not in ('2','X')";

			objTEEXR.hstLVDTL.clear();
			M_strSQLQRY=" select SW_EMPNO,SW_WRKDT,SW_LVECD";
			M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW+" and SW_STSFL not in ('X')";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
            if(M_rstRSSET != null)
            {	
				while(M_rstRSSET.next())
				{	
					if(M_rstRSSET.getString("SW_LVECD") != null)
						objTEEXR.hstLVDTL.put(M_rstRSSET.getString("sw_empno")+M_rstRSSET.getString("sw_wrkdt"),nvlSTRVL(M_rstRSSET.getString("sw_lvecd"),""));
				}
			}	

			M_strSQLQRY=" select SS_SBSCD,SS_EMPNO, SS_WRKDT,SS_ORGSH, SS_CURSH, SS_LVECD, CMT_CHP01, CMT_CHP02 ";
			M_strSQLQRY+= " from HR_SSTRN,CO_CDTRN where CMT_CGMTP = 'M"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'COXXSHF' and CMT_CODCD=SS_CURSH and "+L_strWHRSTR_SS+" and SS_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") order by ss_empno";
			System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET_SS = cl_dat.exeSQLQRY2(M_strSQLQRY);
			
            objTEEXR.exeSWMUPD_SS(L_rstRSSET_SS);

			M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,swt_srlno,min(swt_inctm) swt_PNCTM ";
			M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL1+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt,swt_srlno order by swt_empno,swt_wrkdt,swt_srlno";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			System.out.println(">>>>exeSWMUPD_AL>>>>"+M_strSQLQRY);
            objTEEXR.exeSWMUPD_AL(M_rstRSSET, "IN");

			M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,swt_srlno,max(swt_outtm) swt_PNCTM ";
			M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL2+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt,swt_srlno order by swt_empno,swt_wrkdt,swt_srlno";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
            objTEEXR.exeSWMUPD_AL(M_rstRSSET, "OUT");

			String L_strWHRSTR_LV = " lvt_lvedt  between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  and substr(LVT_SBSCD,1,2) = '"+M_strSBSCD.substring(0,2)+"'";
			M_strSQLQRY=  " select LVT_LVECD,LVT_EMPNO,LVT_STSFL,LVT_LVEDT";
			M_strSQLQRY+= " from HR_LVTRN where "+L_strWHRSTR_LV+" and LVT_LVECD <> 'PE' and LVT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") ";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
            objTEEXR.exeSWMUPD_LV(M_rstRSSET);
			
			M_strSQLQRY=" select SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_ORGSH,SW_CURSH,SW_WRKSH,SW_ACTWK, SW_SHRWK, SW_EXTWK, SW_OVTWK, SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT,trim(ifnull(ep_lstnm,' '))||' '  ||left(ifnull(ep_fstnm,' '),1)||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
			M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW;
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			
			objTEEXR.updWRKHR(M_rstRSSET);
		}
		catch(Exception L_EX){
			setMSG(L_EX,"prcDATA()");
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

/**
 */
    public static void main(String args[])
    {
        try
        {
        hr_hkapr ohr_hkapr = new hr_hkapr();
        if(thrCONNECT!=null)
			thrCONNECT.join();
        System.exit(0);
        //ohr_hkapr.show();
        }
        catch(Exception L_E)
        {
            System.out.println(L_E.toString());
        }
        
    }
	
	/**
	 */
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
			//setMSG("Error while connecting to DB : "+L_EX,'E');
			System.out.println("Error while connecting to DB : ");
			return null;
		}
	}

	/**
	 * Establishees connection with DB and creates statement objects
	 *
	 * @param LP_SYSLC String of length 2 for System Location
	 * @param LP_XXLBX String for library name
	 * @param LP_XXUSX String for User name
	 * @param LP_XXPWX String for Password */
	public  void setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX)
    {
		try
        {
          cl_dat.M_conSPDBA_pbst = this.setCONDTB(LP_SYSLC,LP_XXLBX, LP_XXUSX, LP_XXPWX);
          if(cl_dat.M_conSPDBA_pbst != null)
          {
              cl_dat.M_conSPDBA_pbst.rollback();
		  	  cl_dat.M_stmSPDBA_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst1 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst2 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst3 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
		  }
	    }
        catch(Exception L_EX)
        {
			System.out.println("Error in setCONTACT"+L_EX);
		}
	}
	/**
	 */
   /*private void setMSG(String P_strMESG,char P_chrMSGTP)
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
*/
}
