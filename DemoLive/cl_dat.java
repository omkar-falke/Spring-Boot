import java.sql.ResultSet;import java.sql.SQLException;import java.sql.Connection;
import java.sql.ResultSetMetaData;import java.sql.Statement;
import javax.swing.*;
import java.awt.Cursor;import java.awt.Dimension;import java.awt.CardLayout;
import java.util.Hashtable;import java.util.Vector;
import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;

	
/**Defines Global variables and methods. All varialbes and method in this class should be static as they are used by fr_log as well as cl_pbase and its child classes
 */
public class cl_dat
{
	/**
	 * Timer for System Idle Time	 
	 * 
	 * Timer for System Idle Time	 
	 * 
	 * This timer is restarted at keypress or mouseclick event generated by user. If the timer completes one time cycle of 30min, system is supposed to be idle and application is terminated after breaking the connection to the database
	 */
    public static javax.swing.Timer M_tmrIDLTM_pbst;
	/**
	 * Thread for usage update
	 * 
	 * Thread for usage update	 	 
	 * 
	 * <p>This thread updates usage data at the backend.<br>
	 * This thread is started when user comes out of any screen<br>
	 * The SQL query will modify the corresponding row in SA_PPUTR for logout time and operations carried out by the user<br>
	 * The affected row will be the row inserte by trigger while updating hit count in SA_PPMST, when user enters the screen
	 */
	static Thread M_tmrUPUSG_st;
	/** 
	 * DB connection handle
	 * 
	 * DB connection handle
	 * 
	 * <p>This is single connection available throu out the application and is valid for DB2 only
	 */
	public  static Connection M_conSPDBA_pbst = null;
	/** 
	 * Statement object	
	 * 
	 * Statement object	
	 */
	static Statement M_stmSPDBA_pbst = null,
	 M_stmSPDBQ_pbst = null,
	 M_stmSPDBQ_pbst1 = null,			
	 M_stmSPDBQ_pbst2 = null,			
	 M_stmSPDBQ_pbst3 = null;
    static Hashtable<String,String> hst1to100;
	//TO BE REVIEWD    /////////////////********************
	/** 
	 * String for System Location
	 * 
	 * String for System Location
	 * 
	 * This string will contain 
	 */
	public static String M_strSYSLC_pbst = "";
	//TO BE REIVIEWED    //////////////////////*********************
	/** String for System Location as commandline aurgument	  */
    public static String M_strSYSLC_pbst0 = "";
	/** 
	 * String for Finantial year for the system
	 * 
	 * String for Finantial year for the system
	 * 
	 * This string will be initialised when user enters into a screen. This will facilitate to have different finantial year endings for different systems. 
	 * 
	 */
    public   static String M_strFNNYR_pbst = "";/** Financial year for Marketing	  */
    public   static String M_strFNNYR1_pbst = "";/** String for SQL QUERY	  */
	//TO BE REVIEWD /////////////////***********************
    private   static String strSQLQRY_st = ""; //String for SQL QUERY
    public   static String M_strSRLNUM_pbst;//TO BE WATCHED FOR USAGE ????????
	 /** String for Login Date	  */
	public   static String M_strLOGDT_pbst = "";/**Description of Select option in cbmOPTN	 */
	public   static String M_OPSEL_pbst = "Select an Option";/**Description of Add option in cbmOPTN	 */
	public   static String M_OPADD_pbst = "Addition";/**Description of Modify option in cbmOPTN	 */
	public   static String M_OPMOD_pbst = "Modification";/**Description of Authorisation option in cbmOPTN	 */
	public   static String M_OPAUT_pbst = "Authorization";// ADDED ON 02/04/2004 BY AAP FOR AUTHORISATION OPTION
	/**Description of Delete option in cbmOPTN	 */
	public   static String M_OPDEL_pbst = "Deletion";/**Description of Enquiry option in cbmOPTN	 */
	public   static String M_OPENQ_pbst = "Enquiry";/** Description of File option in report*/
	public   static String M_OPFIL_pbst = "File";/** Description of E-mail option in report*/
	public   static String M_OPEML_pbst = "E-mail";/** Description of Select option in report*/
	public   static String M_OPRSL_pbst = "Select an Option";/** Description of To screen option in report*/
	public   static String M_OPSCN_pbst = "Screen";/** Description of Print option in report*/
	public   static String M_OPPRN_pbst = "Print";
	public   static String M_OPFAX_pbst = "Fax";
	public   static boolean M_flgTRNFL_pbst = true;//TO BE WATCHED FOR USAGE ????????????? .......
	public   static boolean M_strDIRDSN_pbst = false;//TO BE WATCHED FOR USAGE ????????????? .......
	protected static Hashtable<String,String[]> M_hstMKTCD_pbst; 
	
	protected final static int M_intCODDS_pbst = 0;
    protected final static int M_intSHRDS_pbst = 1;
    protected final static int M_intCHP01_pbst = 2;
    protected final static int M_intCHP02_pbst = 3;
    protected final static int M_intCCSVL_pbst = 4;
    protected final static int M_intNMP01_pbst = 5;
    protected final static int M_intNMP02_pbst = 6;
    protected final static int M_intNCSVL_pbst = 7;

    public   static String M_strCMPCD_pbst = "";
	public   static String M_strCMPCD_EXT_pbst = "";
    public   static String M_strCMPLG_pbst = "";

	/**
	 *  Flag to check whether query was successful 
	 * 
	 *  Flag to check whether query was successful 
	 * 
	 * <p>This flag is set in @see #exeSQLUPD(String LP_SQLVAL, String LP_UPDVLD)
	 *  only if LP_UPDVLD="setLCLUPD"<br>
	 * User must set and check this flag while firing a sequence of queries
	 */
	public   static boolean M_flgLCUPD_pbst = true;
	public   static boolean M_flgHELPFL_pbst = false;
	public   static boolean M_flgCHKTB_pbst = true;
	public   static boolean M_flgTSTDT_pbst = false; // test data module	
	public   static int M_intLINNO_pbst, M_PAGENO;////TO BE WATCHED FOR USAGE ????????????? .......
	/**
	 * E-mail ID of the user logged in
	 * 
	 * E-mail ID of the user logged in
	 * 
	 * <p>This E-mail ID is retrieved from SA_USMST during login validation and is used as from address while sending e-mails
	 */
	public   static String M_strSYSEML_pbst = "";
	/** 
	 * String for System specific year start date
	 * 
	 * String for System specific year start date	 	 
	 * 
	 * This string will be initialised when user enters into a screen. This will facilitate to have different finantial year dates for different systems. 
	 */
	public   static String M_strYSTDT_pbst = "";
	/** 
	 * String for System specific year end date
	 * 
	 * String for System specific year end date
	 * 
	 * This string will be initialised when user enters into a screen. This will facilitate to have different finantial year dates for different systems. 
	 */
	public   static String M_strYEDDT_pbst = "";
    /** 
     * String for Company Name
     * 
     * String for Company Name
     * 
     * <p>This string is used to set title of the application window and is hardcoded
     */
	public   static String M_strCMPNM_pbst = "";
    /** 
     * String for Company Locn
     * 
     * String for Company Locn
     * 
     * <p>This string is used to set Locn of the company.
     */
	public   static String M_strCMPLC_pbst = "";
	/**
	 * String for user code, Initialised during login
	 */
	public   static String M_strEINFL_pbst = "";
    public   static String M_strUSRCD_pbst = "";
    public   static String M_strEMPNO_pbst = "";
    public   static String M_strUSRCT_pbst = "";
	public   static String M_strUSRTP_pbst = "";
	public   static String M_strUSRPW_pbst = "";
	public   static String M_strHLPSTR_pbst = "", M_strMESSG_pbst = "",M_strREPSTR_pbst = "";
	/**Strings for internal use in cl_dat	 */
	private static String strTEMP_st, M_strSQLQRY;
	/**Hash table to hold data of help window	 */
	public static Hashtable<String,String> M_hstHELP_pbst;
	/**Cursor in wait status	 */
	public static Cursor M_curWTSTS_pbst = new Cursor(Cursor.WAIT_CURSOR);/**Cursor in default status	 */
	public static Cursor M_curDFSTS_pbst = new Cursor(Cursor.DEFAULT_CURSOR);/**JPanel for help window */
    public static  JPanel M_pnlHELP_pbst = new JPanel();/**JPanel for help window button	 */
	public static  JPanel M_pnlPOSBTN_pbst = new JPanel();/**JPanel for dynamic table creation*/
	public static  JPanel M_pnlDYNTBL_pbst = new JPanel();/**JTable for help data*/
	public static JTable M_tblHELP_pbst;
	public static String M_strURL_pbst ="";
	/**
	 * 	Combo for selecting Options
	 * 
	 * 	Combo for selecting Options
	 * 
	 * <p>This ComboBox contains options available to user in the form he has entered<br>
	 * The options are added dynamically according to subsystem selection for entry forms<br>
	 * for reports, all four options are displayed by default
	 */
	public static JComboBox  M_cmbCMPCD_pbst = new JComboBox();
	public static JComboBox  M_cmbOPTN_pbst = new JComboBox();
	public static JComboBox  M_cmbDESTN_pbst = new JComboBox();
	/**	Combo for SubSystem Code Level - 1 */
	public static JComboBox  M_cmbSBSL1_pbst;
	/** Combo for SubSystem Code Level - 2	 */
	public static JComboBox  M_cmbSBSL2_pbst;
	/** Combo for SubSystem Code Level - 3	 */
	public static JComboBox  M_cmbSBSL3_pbst;
	/**
	 * JButton for Data saving / retrieval
	 * 
	 * JButton for Data saving / retrieval
	 * 
	 * <p>This button is multifunctional as per the program type and option selected by the user<br>
	 * For entry programs, this will work as data saving button, i.e. exeSAVE() will be called<br>
	 * For report/query programs, this will work as data retrieval button, i.e. exePRINT will be called<br>
	 * Caption of the button is changed dynamically as per the selection made by the user<br>
	 * Developer has to decide the action depending on cation of the button or item selection of cmbOPTN
	 */
	public static JButton M_btnSAVE_pbst;
	/**
	 * JButton to undo any data entry made by the user
	 * 
	 * JButton to undo any data entry made by the user
	 * 
	 * <p>This wil simply give call to @link {cl_pbase#clrCOMP()} and clear the contents of the screen<br><b>
	 * Please note that, pressing this button sill not revert back any commands fired to back end</b>
	 */
	public static JButton M_btnUNDO_pbst;
	/**
	 * Displays HTML help page for current form
	 * 
	 * Displays HTML help page for current form
	 * 
	 * Starts internel explorer and opens htm file with same name as that of PRGCD, if available
	 */
	public static JButton M_btnHELP_pbst;
	/**
	 * Button to exit from a form
	 * 
	 * Button to exit from a form
	 * 
	 * <p>Removes current form, hides button pallets, Displays login screen and menu bar
	 */
	public static JButton M_btnEXIT_pbst;
	/**Button to exit from application. Closes connection to database and terminates system	 */
	public static JButton M_btnEXT_pbst;
	public static JButton M_btnHLP_pbst,M_btnRUN_pbst,M_btnCLR_pbst,M_btnPRN_pbst;        
	/** Text field to display messages using setMSG()	 */
    public static JTextField M_txtSTAT_pbst;
	/** Text field to display UserCode	 */
	public static JTextField M_txtUSER_pbst;
	/** Text field to display login date */
	public static JTextField M_txtDATE_pbst;
	/** Text field to display Descriptions in status bar	 */
	public static JTextField M_txtDESC_pbst;
	/** Text field for label "Description" for txtDESC in status bar		 */
	public static JTextField M_txtDMSG_pbst;
	/**Text field for smart seach in help panel	 */
	public static JTextField M_txtHLPPOS_pbst;
	public static JTextField M_txtDESTN_pbst;
	/** User Code in login screen		 */
	public static JTextField M_txtUSRCD_pbst;
	/** User password in login screen		 */
	public static JPasswordField M_txtPASWD_pbst;
	/**OK button in help window	 */
	public static JButton    M_btnHLPOK_pbst;
	/**used in help module	 */
    public static JLabel     M_lblHELP_pbst,M_lblHLPHDG_pbst;/**used in help module	 */
	public static JWindow	  M_wndPOS_pbst;/**used in help module	 */
	public static JDialog		M_wndHLP_pbst;/**used in help module	 */
    public static ResultSet         M_rstHELP_pbst;/**used in help module	 */
    public static ResultSetMetaData M_rmdRSMHLP_pbst;
    public static ResultSet M_rstRSLCOD_pbst,M_rstRSSET_pbst;/**used in help module	 */
	public static JList M_lstHELP_pbst;/**used in help module	 */
	public static int M_intCTITM_pbst,M_SPLSCH_pbst, M_intSPLRTN_pbst,M_intCNTCHR_pbst; /**used in Dynamic table module	 */
    public static String[] M_staTBDAT_pbst;/**used in help module	 */
	public static int M_intXPOS_pbst,M_intYPOS_pbst,M_intCOUNT_pbst=0;/**used in help module	 */
	public static   String M_strRTNVL_pbst;/**used in help module	 */
	public static String M_strHELP_pbst ;
	/**JPnale for top portion of application window	 */
	public static JPanel M_pnlFRTOP_pbst;/**JPnale for subsystem combos of application window	 */
	public static JPanel M_pnlSBSCD_pbst;/**Card layout for display of forms in application window	 */
	public static CardLayout M_crdCENTR_pbst;/**JButton for changing password*/
	public static JButton M_btnSETPW_pbst;	/**Text field for login date in login screen*/
	public static TxtDate M_txtCURDT_pbst;/**JPanel for login screen*/
	public static JPanel M_pnlLOGIN_pbst;/**JPanel for status bar at the bottom of application window*/
	public static JPanel M_pnlFRBTM_pbst;/**Dimension of the screen, depends on resolution selected by user */
	public static Dimension M_dimSCRN_pbst;/** Ratio of curren screen dimension to standard dimension*/
	public static double M_dblWIDTH=1,M_dblHIGHT=1; /** Text field to display current calendar date and time in status bar*/
	public static JTextField M_txtCLKTM_pbst,M_txtCLKDT_pbst;
	public static Vector<String> M_vtrPRNTR_pbst = new Vector<String>();
	public static DocFlavor flavor;
	public static PrintRequestAttributeSet aset;
	public static PrintService[] pservices;	
	public static Thread M_thrPRNLS_pbst;
	public static int M_intPRIND_pbst=2;
	/**
	 * Method to get Current clock time in HH:mm:ss format
	 * 
	 * Method to get Current clock time in HH:mm:ss format 
	 * 
	 * <p>This method gets current time from @see M_txtCLKTM_pbst, appends ":00" to it and returns the value<br>
	 * Time displayed in txtCLKTM is server time, which gets refreshed by timer at every second and once in an hr from database server
	 */
	public static String getCURTIM()
	{
		return M_txtCLKTM_pbst.getText()+":00";
	}
	
	/**
	 * Method to get Current date time in dd/mm/yyyy HH:mm:ss format
	 * 
	 * Method to get Current date time in dd/mm/yyyy HH:mm:ss format
	 * 
	 * <p>This method gets current date and time from @see M_txtCLKDT_pbst & @see M_txtCLKTM_pbst, appends ":00" to it and returns the value<br>
	 * Time & date displayed in txtCLKTM & txtCLKDT resp. are same as server, which gets refreshed by timer at every second and once in an hr from database server
	 */
	public static String getCURDTM()
	{
		return M_txtCLKDT_pbst.getText()+" "+M_txtCLKTM_pbst.getText()+":00";
	}
//METHOD IS USED ONLY IN FG_MNU & MR_MNU> CAN BE DEFINED LOCALLY
	//09/09/2003 HBP
	/*
	public static  boolean chkUPDVLD(){//CAN BE REMOVED AFTER REVIEW BY SRD****??????
		 boolean L_UPDVLD = false ;
		 try{
			 M_strSQLQRY  = "Select * from CO_CDTRN where CMT_CGMTP = 'DTR' and";
			 M_strSQLQRY += " CMT_CGSTP='FGXXTOH' and CMT_CODCD='0001' and CMT_CHP02='VALID'" ;
			 M_rstRSSET_pbst = exeSQLQRY(M_strSQLQRY);
			 if(M_rstRSSET_pbst.next())
				L_UPDVLD = true;
		}catch(SQLException L_SE){
			System.out.println("Error in chkUSRVLD : "+L_SE.toString());
		}catch(NullPointerException L_SE){
			System.out.println("null pointer exception");
		}
		return L_UPDVLD;
	}
	
*/
	
/**
 * Method to fire a query to database, dynamically
 * 
 * Method to fire a query to database, dynamically
 * 
 * <p> This method is termed as dynamic because, it uses locally created resultset and statment objects<br>
 * This will be useful when a number of qreries are to be fired in parallel (in case of threads) or nessted format
 */
public static  ResultSet exeSQLQRY0(String LP_SQLVAL){
	ResultSet L_rstRSSET = null;
	try
	{
		if (M_conSPDBA_pbst != null)
		{
			Statement L_stmLOCAL=cl_dat.M_conSPDBA_pbst.createStatement();
			L_rstRSSET = L_stmLOCAL.executeQuery(LP_SQLVAL);
		}
	}catch(Exception L_SE){
		System.out.println("Error in exeSQLQRY : "+L_SE.toString());
		System.out.println("QUERY Failed: "+LP_SQLVAL);
	  }
	return L_rstRSSET;
}
	
/**
 * Method to fire select query to database
 * 
 * Method to fire select query to database
 * 
 * This method makes use of pre-defined set of statment and resultset object
 */	
public static  ResultSet exeSQLQRY(String LP_SQLVAL){
	M_rstRSSET_pbst = null;
	try
	{
		if (M_conSPDBA_pbst != null)
			M_rstRSSET_pbst = M_stmSPDBQ_pbst.executeQuery(LP_SQLVAL);
	}catch(Exception L_SE){
		System.out.println("Error in exeSQLQRY : "+L_SE.toString());
		System.out.println("QUERY Failed: "+LP_SQLVAL);
	  }
	return M_rstRSSET_pbst;
}
  
/**
 * Method to fire select query to database
 * 
 * Method to fire select query to database
 * 
 * This method makes use of pre-defined set of statment and resultset object
 */	
public static  ResultSet exeSQLQRY1(String LP_SQLVAL){
	M_rstRSSET_pbst = null;
	try
	{
		if (M_conSPDBA_pbst != null)
			M_rstRSSET_pbst = M_stmSPDBQ_pbst1.executeQuery(LP_SQLVAL);
   }catch(Exception L_SE){
		   System.out.println(" Sql Exception "+ L_SE.toString());
		   System.out.println("QUERY Failed: "+LP_SQLVAL);
	  }
	return M_rstRSSET_pbst;
	}

/**
 * Method to fire select query to database
 * 
 * Method to fire select query to database
 * 
 * This method makes use of pre-defined set of statment and resultset object
 */	
public static  ResultSet exeSQLQRY2(String LP_SQLVAL){
	M_rstRSSET_pbst = null;
	try{
		if (M_conSPDBA_pbst != null)
			M_rstRSSET_pbst = M_stmSPDBQ_pbst2.executeQuery(LP_SQLVAL);
   }catch(Exception L_SE){
		   System.out.println(" Sql Exception "+ L_SE.toString());
		   System.out.println("QUERY Failed: "+LP_SQLVAL);
	  }
	return M_rstRSSET_pbst;
	}
  
/**
 * Method to fire select query to database
 * 
 * Method to fire select query to database
 * 
 * This method makes use of pre-defined set of statment and resultset object
 */	
public static  ResultSet exeSQLQRY3(String LP_SQLVAL){
	M_rstRSSET_pbst = null;
	try{
		if (M_conSPDBA_pbst != null)
			M_rstRSSET_pbst = M_stmSPDBQ_pbst3.executeQuery(LP_SQLVAL);
   }catch(Exception L_SE){
		   System.out.println(" Sql Exception "+ L_SE.toString());
		   System.out.println("QUERY Failed: "+LP_SQLVAL);
	  }
	return M_rstRSSET_pbst;
	}
/**
 * Method to fire queries other than select to database
 * 
 * Method to fire queries other than select to database
 * 
 * <p>This method makes use of pre-defined set of statment and resultset object<br>
 * It is neccessary to have flgLCUPD true before call to this method. otherwise, error is genrated as "Query failed"
 * 
 * @param LP_SQLVAL String containing query to be fired
 * @param LP_UPDVLD String to indicate whether flagfor updation is to be set or not
 * 
 * <hr>
 * <b>Usage :</b><br>
 * exeSQLUPD("MyQuery","setLCLUPD"):<br>
 * Executes query, if query suceeds, M_flgLCUPD_pbst is set to true, otherwise false<br>
 * <b>Restrictions</b><br>
 * To set flgLCUPD, LP_UPDVLD must be "setLCLUPD" (case sensitive)<br>
 * If the query is an update or delete query and "NoRowFound" exception occures at back end, i.e. if executeUpdate() returns zero, error is thrown as "Query failed"
 */	
public static  void exeSQLUPD(String LP_SQLVAL, String LP_UPDVLD){
	if (M_flgLCUPD_pbst){
		int L_UPDCTR = 0;
		try{
			if (M_conSPDBA_pbst != null)
				L_UPDCTR = M_stmSPDBA_pbst.executeUpdate(LP_SQLVAL);
			if(L_UPDCTR < 1){
				if(LP_UPDVLD.equals("setLCLUPD")){
					System.out.println("QUERY Failed: ("+L_UPDCTR+")"+LP_SQLVAL);
					M_flgLCUPD_pbst = false;
				}
			}
		}catch(Exception L_SE){
			M_flgLCUPD_pbst = false;
			System.out.println("Error in exeSQLUPD : " + L_SE.toString());
			System.out.println("Error Query : " + LP_SQLVAL);
		}
	}
}
////09/09/03 HBP
//THIS METHOD IS REMOVED AS THE SAME HAS TO BE INCORPORATED IN A TABLE MODULE WITH SCROLLABLE RESULTSET ATTACHED
	public static int getRECCNT(String LP_SQLSTR){
		int L_RETVAL = -1;
		try{
			M_rstRSSET_pbst = exeSQLQRY(LP_SQLSTR);
			if(M_rstRSSET_pbst.next())
			  L_RETVAL = M_rstRSSET_pbst.getInt(1);
			M_rstRSSET_pbst.close();
	    }catch (Exception L_EX){
			System.out.println("getRECCNT: "+L_EX);				
	        L_RETVAL = -1;
	       }
		return L_RETVAL;
	}

//09/09/03 HBP
//METHOD REMOVED AS WE ARE USING A SINGLE CONNECTION, THIS METHOD GETS REDUCED TO A STATEMENT :
////////M_conSPDBA_pbst.rollback();//////////
/*public static  void exeDBRBK(String LP_SYSCD, String LP_CONTP){
		try{
			if (LP_CONTP.equals("ACT")){
				if(M_conSPDBA_pbst != null)
					M_conSPDBA_pbst.rollback();
			}
/*			else if (LP_CONTP.equals("REM")){
				if(conSPDBR != null)
					conSPDBR.rollback();
				}
		}catch(Exception L_EX){
			System.out.println("exeDBRBK: "+L_EX);				
			System.out.println("exeDBRBK: "+M_flgLCUPD_pbst);
		}
	}*/
	
	/**
	 * Method to commit changes done at database
	 * 
	 * Method to commit changes done at database
	 * 
	 * <p>This method Checks for flgLCUPD = true. If yes, changes are commited and true is returned. Otherwise, changes arre rollback and false if returned.
	 * 
	 * @param LP_MTDNM String containing Name of the method from which changes are being committed. This name is used for output of userfriendly erroe message if any.
	 */
	public static  boolean exeDBCMT(String LP_MTDNM){
		boolean L_CMTFL = true;
		try{
					if(M_conSPDBA_pbst != null)
					{
						if (M_flgLCUPD_pbst == true)
							M_conSPDBA_pbst.commit();
						else{
							M_conSPDBA_pbst.rollback();
							L_CMTFL = false;
						}
					}
					else
						L_CMTFL = false;
		}catch(Exception L_EX){
			System.out.println(LP_MTDNM+": "+L_EX);
			L_CMTFL = false;
//			System.out.println("Exception: "+strSQLQRY_st);
		}
		return L_CMTFL;
	}

//METHOD TO GET A PARAMETER FROM CODE TRANSACTION TABLE.
public static  String getPRMCOD(String LP_FLDRTN, String LP_STRMGP, String LP_STRSGP, String LP_STRCOD){
	strTEMP_st = "";
	M_strSQLQRY ="select " + LP_FLDRTN ;
	M_strSQLQRY = M_strSQLQRY + " from CO_CDTRN where CMT_CGMTP = "+ "'" + LP_STRMGP.trim() +"'" ;
	M_strSQLQRY = M_strSQLQRY + " AND CMT_CGSTP = " + "'" + LP_STRSGP.trim() + "'";
	M_strSQLQRY = M_strSQLQRY + " AND CMT_CODCD = " + "'" + LP_STRCOD.trim() + "'";
	try{
		M_rstRSSET_pbst = exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET_pbst.next())
		    strTEMP_st = M_rstRSSET_pbst.getString(1);
		M_rstRSSET_pbst.close();
	}catch(SQLException L_SE){
		System.out.println("Error in getPRMCOD : "+L_SE.toString()); 
	}
	return strTEMP_st;
}
//METHOD TO GET DETAIL OF PERTICULAR FIELD IN PARTY MASTER
public static  String getPRMPRT(String LP_FLDRTN, String LP_STRPTP, String LP_STRPCD){
   strTEMP_st = "";
   M_strSQLQRY = "Select "  + LP_FLDRTN ;
   M_strSQLQRY += " from CO_PTMST where PT_PRTTP = "+ "'" + LP_STRPTP +"'" ;
   M_strSQLQRY += " and PT_PRTCD = " + "'" + LP_STRPCD + "'";
   try{
	   M_rstRSSET_pbst = exeSQLQRY(M_strSQLQRY);
	   if(M_rstRSSET_pbst.next())
			strTEMP_st = M_rstRSSET_pbst.getString(1);
	   M_rstRSSET_pbst.close();
   }catch(SQLException L_SE){
       System.out.println("Error in getPRMPRT : "+L_SE.toString()); 
   }
   return strTEMP_st;
}
   
//TO BE DECIDED AFTER STUDY OF JAVA PACKAGE METHODS

	public static String setDATFMT(String LP_FMTTP,String LP_DATSTR){
		String L_RTNSTR = "";
		try{
			if(LP_FMTTP.equals("MDY")){
				if(LP_DATSTR != null){
					if(LP_DATSTR.trim().length() == 10)
						L_RTNSTR = LP_DATSTR.substring(3,5)+ "/"+ LP_DATSTR.substring(0,2)+"/"+LP_DATSTR.substring(6,10);
				}
			}
			else if(LP_FMTTP.equals("DMY")){
				if(LP_DATSTR != null){
					if(LP_DATSTR.trim().length() == 8)
						L_RTNSTR = LP_DATSTR.substring(0,2)+ "/"+LP_DATSTR.substring(3,5)+"/"+"20"+LP_DATSTR.substring(6,8);
				}
			}
		}catch(Exception L_EX){
			System.out.println("setDATFMT: "+L_EX);
		}
		return L_RTNSTR;
	}

   
	public static void exeSRLSET(String LP_CGMTP, String LP_CGSTP, String LP_CODCD, String LP_CCSVL){
		try{
			String L_STRQRY = "";
			L_STRQRY = "update CO_CDTRN set CMT_CCSVL = '"+LP_CCSVL+"' where CMT_CGMTP = ";
			L_STRQRY += "'" + LP_CGMTP + "' and CMT_CGSTP = '" + LP_CGSTP + "'";
			L_STRQRY += " AND CMT_CODCD = '" + LP_CODCD + "'";
			exeSQLUPD(L_STRQRY,"setLCLUPD");
			} // try
		catch (Exception L_EX){
			System.out.println("Error "+L_EX.toString());
		} // catch (SQLException L_EX)
	} // exeSRLSET()

	
	
	
	public static String exeSRLGET(String LP_CGMTP, String LP_CGSTP, String LP_CODCD){
		ResultSet M_rstRSLCOD_pbst;
		String L_STRRET = "XXXXX";
		String L_STRQRY = " ";
		L_STRQRY = "select CMT_CODCD,CMT_CCSVL from co_cdtrn where CMT_CGMTP = ";
		L_STRQRY += "'" + LP_CGMTP + "' and CMT_CGSTP = '" + LP_CGSTP + "'";
		L_STRQRY += " AND CMT_CODCD = '" + LP_CODCD + "'";
		System.out.println("L_STRQRY : "+L_STRQRY);
		try{
			M_rstRSLCOD_pbst = cl_dat.exeSQLQRY(L_STRQRY);
    		String L_SRLNO = " ";
			if((M_rstRSLCOD_pbst !=null) && (M_rstRSLCOD_pbst.next())) {
				cl_dat.M_strSRLNUM_pbst = M_rstRSLCOD_pbst.getString("CMT_CCSVL").trim();
	            int i = Integer.valueOf(cl_dat.M_strSRLNUM_pbst).intValue();
                i++;
                L_SRLNO = i + " ";
                int L_STRLEN = 5 - L_SRLNO.trim().length();
                for (int j=0;j < L_STRLEN;j++)
					L_SRLNO = "0" + L_SRLNO;
				cl_dat.M_strSRLNUM_pbst = L_SRLNO.trim();
				System.out.println("M_strSRLNUM_pbst : "+cl_dat.M_strSRLNUM_pbst);
                //setMSG(" ",'N');
				int L_IDXNUM = LP_CODCD.lastIndexOf('_');
                L_STRRET = (LP_CODCD.substring(L_IDXNUM + 1) + L_SRLNO.trim());
				System.out.println("L_STRRET : "+L_STRRET);
				M_rstRSLCOD_pbst.close();
			} // if((M_rstRSLCOD_pbst !=null) && (M_rstRSLCOD_pbst.next()))
		} // try
		catch (SQLException L_EX){
			System.out.println("SQL Exception in exeSTRLGET : "+L_EX);
			//showEXMSG(L_EX,"exeSRLGET","");				
		}
		return(L_STRRET);
	}
	public static void getPRSRV()
	{
		flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		aset = new HashPrintRequestAttributeSet();
	//	System.out.println("start "+cl_dat.getCURDTM());
		pservices =PrintServiceLookup.lookupPrintServices(flavor, aset);
	//	System.out.println("out "+cl_dat.getCURDTM());
		System.out.println("pservices.length "+pservices.length);
		for(int i=0;i<pservices.length;i++)
		{
			//M_cmbDESTN.addItem(pservices[i].getName());
			M_vtrPRNTR_pbst.addElement(pservices[i].getName());
		}
	}
public static String getCURWRD(String LP_CURVL,String LP_DENOM)
{
      	//String to be printed
		String L_PRNSTR = "";
		try{
			//Remainder of divison
			int L_REMVL = 0;
			//Quotient of divison
			int L_QUOVL = 0;
			//Getting rupees
			String L_RPSVL = LP_CURVL.substring(0,LP_CURVL.indexOf(".")).trim();
			//Converting rupees in string to integer
			int L_RUPVL = Integer.parseInt(L_RPSVL);
			//Getting paise
			String L_PAIVL = LP_CURVL.substring(LP_CURVL.lastIndexOf(".")+1,LP_CURVL.length()).trim();
			//Taking length of rupees value
			int L_ABSVL = L_RPSVL.length();
           if(L_ABSVL > 0){
				//Ones
				if(L_ABSVL == 1){
                                        L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Tens
				else if(L_ABSVL == 2){
                                        L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Hundreds
				else if(L_ABSVL == 3){
					L_QUOVL = L_RUPVL/100;
					L_REMVL = L_RUPVL%100;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
				//Thousands
				else if(L_ABSVL == 4 || L_ABSVL == 5){
					L_QUOVL = L_RUPVL/1000;
					L_REMVL = L_RUPVL%1000;
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
                 	L_QUOVL = L_REMVL/100;
					L_REMVL = L_REMVL%100;
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
                               
				}
				//Lacs
				else if(L_ABSVL == 6 || L_ABSVL == 7){
					L_QUOVL = L_RUPVL/100000; //for getting lac value
					L_REMVL = L_RUPVL%100000;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Lac",'B');
					L_QUOVL = L_REMVL/1000;  //for getting thousand value
					L_REMVL = L_REMVL%1000;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
					L_QUOVL = L_REMVL/100;  //for getting hundred value
					L_REMVL = L_REMVL%100;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
				//Crores
				else if(L_ABSVL == 8 || L_ABSVL == 9){
					L_QUOVL = L_RUPVL/10000000; //for getting crore value
					L_REMVL = L_RUPVL%10000000;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Crore",'B');
					L_QUOVL = L_REMVL/100000;  //for getting lac value
					L_REMVL = L_REMVL%100000;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Lac",'B');
					L_QUOVL = L_REMVL/1000;  //for getting thousand value
					L_REMVL = L_REMVL%1000;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
					L_QUOVL = L_REMVL/100;  //for getting hundred value
					L_REMVL = L_REMVL%100;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
			}
			if(L_PAIVL.length() > 0 && !L_PAIVL.equals("00")){
				if(L_PAIVL.substring(0,1).equals("0"))
					L_PAIVL =L_PAIVL.substring(1);
                                L_PRNSTR += chkZERO(hst1to100.get(L_PAIVL).toString(),"and "+LP_DENOM,'F');
			}
			L_PRNSTR += "Only";
		}catch(Exception L_EX){
                       // setMSG(L_EX,"getCURWRD");
                   System.out.println("Error in getCURWD "+L_EX.toString());    
		}
		return L_PRNSTR;
	}
	
        private static String chkZERO(String LP_STRVL,String LP_RPSVL,char LP_PLCSTR){
		String L_RTNSTR = "";
		try{
			if(String.valueOf(LP_STRVL).equals(null) || LP_STRVL.equalsIgnoreCase("zero")){
				return "";
			}else{
				if(LP_PLCSTR == 'F'){
					L_RTNSTR = LP_RPSVL + " " +  LP_STRVL + " ";
				}
				else if(LP_PLCSTR == 'B'){
					L_RTNSTR = LP_STRVL + " " + LP_RPSVL + " ";
				}
			}
		}catch(Exception L_EX){
          	//setMSG(L_EX,"chkZERO");
          	System.out.println(L_EX.toString());
		}
		return L_RTNSTR;
	}
	public static void crtHST()
	{
		hst1to100 = new Hashtable<String,String>();
		hst1to100.put("0","zero");hst1to100.put("1","One");hst1to100.put("2","Two");hst1to100.put("3","Three");hst1to100.put("4","Four");
		hst1to100.put("5","Five");hst1to100.put("6","Six");hst1to100.put("7","Seven");hst1to100.put("8","Eight");
		hst1to100.put("9","Nine");hst1to100.put("10","Ten");hst1to100.put("11","Eleven");hst1to100.put("12","Twelve");
		hst1to100.put("13","Thirteen");hst1to100.put("14","Fourteen");hst1to100.put("15","Fifteen");hst1to100.put("16","Sixteen");
		hst1to100.put("17","Seventeen");hst1to100.put("18","Eighteen");hst1to100.put("19","Nineteen");hst1to100.put("20","Twenty");
		hst1to100.put("21","Twenty One");hst1to100.put("22","Twenty Two");hst1to100.put("23","Twenty Three");hst1to100.put("24","Twenty Four");
		hst1to100.put("25","Twenty Five");hst1to100.put("26","Twenty Six");hst1to100.put("27","Twenty Seven");hst1to100.put("28","Twenty Eight");
		hst1to100.put("29","Twenty Nine");hst1to100.put("30","Thirty");hst1to100.put("31","Thirty One");hst1to100.put("32","Thirty Two");
		hst1to100.put("33","Thirty Three");hst1to100.put("34","Thirty Four");hst1to100.put("35","Thirty Five");hst1to100.put("36","Thirty Six");
		hst1to100.put("37","Thirty Seven");hst1to100.put("38","Thirty Eight");hst1to100.put("39","Thirty Nine");hst1to100.put("40","Fourty");
		hst1to100.put("41","Fourty One");hst1to100.put("42","Fourty Two");hst1to100.put("43","Fourty Three");hst1to100.put("44","Fourty Four");
		hst1to100.put("45","Fourty Five");hst1to100.put("46","Fourty Six");hst1to100.put("47","Fourty Seven");hst1to100.put("48","Fourty Eight");
		hst1to100.put("49","Fourty Nine");hst1to100.put("50","Fity");hst1to100.put("51","Fifty One");hst1to100.put("52","Fifty Two");
		hst1to100.put("53","Fifty Three");hst1to100.put("54","Fifty Four");hst1to100.put("55","Fifty Five");hst1to100.put("56","Fifty Six");
		hst1to100.put("57","Fifty Seven");hst1to100.put("58","Fifty Eight");hst1to100.put("59","Fifty Nine");hst1to100.put("60","Sixty");
		hst1to100.put("61","Sixty One");hst1to100.put("62","Sixty Two");hst1to100.put("63","Sixty Three");hst1to100.put("64","Sixty Four");
		hst1to100.put("65","Sixty Five");hst1to100.put("66","Sixty Six");hst1to100.put("67","Sixty Seven");hst1to100.put("68","Sixty Eight");
        hst1to100.put("69","Sixty Nine");hst1to100.put("70","Seventy");hst1to100.put("71","Seventy One");hst1to100.put("72","Seventy Two");hst1to100.put("73","Seventy Three");
		hst1to100.put("74","Seventy Four");hst1to100.put("75","Seventy Five");hst1to100.put("76","Seventy Six");hst1to100.put("77","Seventy Seven");
		hst1to100.put("78","Seventy Eight");hst1to100.put("79","Seventy Nine");hst1to100.put("80","Eighty");hst1to100.put("81","Eighty One");
		hst1to100.put("82","Eighty Two");hst1to100.put("83","Eighty Three");hst1to100.put("84","Eighty Four");hst1to100.put("85","Eighty Five");		
		hst1to100.put("86","Eighty Six");hst1to100.put("87","Eighty Seven");hst1to100.put("88","Eighty Eight");hst1to100.put("89","Eighty Nine");
		hst1to100.put("90","Ninety");hst1to100.put("91","Ninety One");hst1to100.put("92","Ninety Two");hst1to100.put("93","Ninety Three");
		hst1to100.put("94","Ninety Four");hst1to100.put("95","Ninety Five");hst1to100.put("96","Ninety Six");hst1to100.put("97","Ninety Seven");
		hst1to100.put("98","Ninety Eight");hst1to100.put("99","Ninety Nine");

	}

////09/09/03 HBP
//THIS METHOD IS REMOVED AS THE SAME HAS TO BE INCORPORATED IN A TABLE MODULE WITH SCROLLABLE RESULTSET ATTACHED
/*
	public static int getROWCNT(ResultSet LP_RSLSET){
		int L_CNT = 0;
		try{
			while(LP_RSLSET.next())
				L_CNT++;
			LP_RSLSET.close();
		}catch(Exception L_EX){
			System.out.println("getROWCNT: "+L_EX);
		}
		return L_CNT;
	}*/
}
