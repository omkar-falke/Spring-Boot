import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class co_datrep extends JFrame implements ActionListener,Runnable
{
  JButton btnSTR,btnSTP,btnEXT, btnBKP,btnDTR;
  JTextField txtMSG;
  JPanel pnlMAIN;
  Connection conSRCDB;
  Connection conDESDB;
  Statement stmSRCQ1;
  Statement stmSRCQ2;
  Statement stmSRCUP;
  Statement stmDESUP;
  ResultSet LM_RSLTPM,LM_RSLSET,LM_RSLSET1,LM_RSLSRC;
  ResultSetMetaData LM_RSLMDT;
  boolean LM_UPDFL = false;
  boolean LM_RUNFL = false; // thread not running
  Cursor curWTSTS = new Cursor(Cursor.WAIT_CURSOR);
  Cursor curDFSTS = new Cursor(Cursor.DEFAULT_CURSOR);
  String LM_STRSQL = ""; 
  String LM_SRCLC ="";
  String LM_DESLC ="";
  static String LM_REPOPT; // replication option
  String LM_TBLNM,LM_ORGCD,LM_TFLNM;
  String LM_INSSQL ="";
  String LM_UPDSQL ="";
  String LM_TRNUPD ="";
  String LM_WHRCND ="";
  final String LM_SVRWR   ="01";
  final String LM_SVRHO   ="02";

  String LM_QCATP,LM_TSTTP,LM_TSTNO,LM_CGMTP,LM_CGSTP,LM_CODCD; 	  
  String LM_PRDTP,LM_LOTNO,LM_RCLNO,LM_TSTDT,LM_WTRTP,LM_MORTP,LM_TSTRF;
  String LM_RCTWRHTP,LM_RCTRCTTP,LM_RCTRCTNO,LM_RCTPRDTP,LM_RCTLOTNO,LM_RCTRCLNO,LM_RCTPKGTP,LM_RCTMNLCD;
  String LM_LWWRHTP,LM_LWRCTTP,LM_LWRCTNO,LM_LWPRDTP,LM_LWLOTNO,LM_LWRCLNO;
  String LM_RMWRHTP,LM_RMTRNTP,LM_RMDOCTP,LM_RMDOCNO;
  String LM_LCWRHTP,LM_LCMNLCD;
  String LM_PTFPTFNO,LM_PTFPRDTP,LM_PTFLOTNO,LM_PTFRCLNO,LM_PTFPKGTP;
  String LM_ISTWRHTP,LM_ISTISSTP,LM_ISTISSNO,LM_ISTPRDTP,LM_ISTPRDCD,LM_ISTLOTNO,LM_ISTRCLNO,LM_ISTPKGTP,LM_ISTMNLCD;
  String LM_STLSQL="",LM_TMPSTR="";
  StringBuffer LM_UPDQRY = new StringBuffer();
  StringBuffer LM_WHCND= new StringBuffer();
  StringBuffer LM_INSSTR= new StringBuffer();
  StringBuffer LM_UPDSTR= new StringBuffer();
  Timestamp LM_TSTTM,LM_LWSTRTM;	
  Thread th ;	  
  Hashtable LM_HSCOLNM = new Hashtable();
  Hashtable LM_HSCOLTP = new Hashtable();
  Hashtable LM_HSKEYNM = new Hashtable();
  Hashtable LM_HSKEYTP = new Hashtable();
  int LM_KEYCNT =0;
  co_datrep(String LP_SRCLC,String LP_DESLC)
  {
	super("Data Replication");
	LM_SRCLC = LP_SRCLC; 
	LM_DESLC = LP_DESLC; 
	pnlMAIN = new JPanel();
	pnlMAIN.setLayout(null);
	btnSTR = new JButton("Start");
	btnSTP = new JButton("Stop");
	btnBKP = new JButton("Backup");
	btnDTR = new JButton("Data Tfr.");
	btnEXT = new JButton("Exit");
	txtMSG = new JTextField();
	pnlMAIN.add(btnSTR);
	pnlMAIN.add(btnSTP);
	pnlMAIN.add(btnDTR);
	pnlMAIN.add(btnBKP);
	pnlMAIN.add(btnEXT);
	pnlMAIN.add(txtMSG);
	btnSTR.setBounds(50,30,80,20);
	btnSTP.setBounds(150,30,80,20);
	btnBKP.setBounds(250,30,80,20);
	btnDTR.setBounds(350,30,80,20);
	btnEXT.setBounds(450,30,80,20);
	txtMSG.setBounds(10,70,580,20);
	
	btnSTR.addActionListener(this);
	btnSTP.addActionListener(this);
	btnBKP.addActionListener(this);
	btnDTR.addActionListener(this);
	btnEXT.addActionListener(this);
	
	getContentPane().add(pnlMAIN);
	setSize(600,150);
	validate();
	setVisible(true);
	th = new Thread(this);
	th.start();
  }
  public static void main(String args[])
	{
		if(args.length < 3)
		{
			System.out.println("Give the Transfer Source and Destination location code and Database as parameters");
			System.exit(0);
		}
		else
		{
			LM_REPOPT = args[2];
			if((args[0].equals("02"))&&(args[1].equals("01")))
			{
				System.out.println("Option not available for HO to Works data updating");
				System.exit(0);
			}
			else if((args[0].trim().equals(args[1].trim())))
			{
		
				if(!LM_REPOPT.equals("TST"))
			    {
					System.out.println("Source and destination parameters can not be same");
					System.exit(0);
				}
			}
		}
		co_datrep oco_datrep = new co_datrep(args[0],args[1]);
   }
  public void actionPerformed(ActionEvent L_AE)
  {
		if(L_AE.getSource().equals(btnEXT))
		{
			this.dispose();
			System.exit(0);
		}
		if(L_AE.getSource().equals(btnSTR))
		{
			strREP();
		}
  }
  public void run()
  {
	  
	  try
	  {
		  for(;;)
		  {
			if(!LM_RUNFL)  
			{
				LM_RUNFL = true;
				strREP();
				th.sleep(30*60*1000); // sleep for half an hour
				LM_RUNFL = false;
			}
		  }
	  }
	  catch(Exception L_E)
	  {
	  }
  }
 private boolean strREP()
{
	 int L_CNT =0;
	 String L_KEYNM ="";
	 StringTokenizer L_STRTKN;
	 String L_SRCDB = "";
	 String L_DESDB = "";
	try
	{
		setCursor(curWTSTS);
		if(LM_REPOPT.equals("ACT"))
		{
			L_SRCDB = "SPLDATA";
			L_DESDB = "SPLDATA";
			
		}
		else if(LM_REPOPT.equals("HST"))
		{
			L_SRCDB = "SPLHIST";
			L_DESDB = "SPLHIST";
		}
		if(LM_REPOPT.equals("TST"))
		{
			L_SRCDB = "SPLDATA";
			L_DESDB = "SPLTEST";
		}
		conSRCDB = setCONDTB(LM_SRCLC,L_SRCDB);
		setMSG("Connecting to the source database :"+LM_SRCLC + " / "+L_SRCDB +". Please wait",'N');
		conDESDB = setCONDTB(LM_DESLC,L_DESDB);
		setMSG("Connecting to the destination database :"+LM_DESLC + " / "+L_DESDB +". Please wait",'N');
		
		if(conSRCDB == null)
		{
			setMSG("Can not establish connection to the source database",'E');
			return false;
		}
		else
		{
			setMSG("Connection established to source DB",'N');
		}
		if(conDESDB == null)
		{
			setMSG("Can not establish connection to the destination database",'E');
			return false;
		}
		else
		{
			setMSG("Connection established to Destination DB",'N');
		}
		setSTMDTB();
		LM_STRSQL ="Select * from CO_TPMST order by TP_ORDBY";
	//	LM_STRSQL ="Select * from CO_TPMST where tp_tblnm like 'MM%' order by TP_ORDBY";
	//	LM_STRSQL ="Select * from CO_TPMST where tp_tblnm = 'MM_LRMST'";
		LM_RSLTPM = exeSQLQRY(LM_STRSQL,LM_SRCLC);
		setMSG("Picking up the table information",'N'); 
		if(LM_RSLTPM != null)
		while(LM_RSLTPM.next())
		{
			L_CNT =1;
			LM_TBLNM = nvlSTRVL(LM_RSLTPM.getString("TP_TBLNM"),"");
			LM_ORGCD = nvlSTRVL(LM_RSLTPM.getString("TP_ORGCD"),"");
			L_KEYNM  = nvlSTRVL(LM_RSLTPM.getString("TP_KEYFL"),"");
		//	System.out.println(L_KEYNM);
			LM_TFLNM  = nvlSTRVL(LM_RSLTPM.getString("TP_TFLNM"),"");
			L_STRTKN = new StringTokenizer(L_KEYNM,",");
			if(LM_HSKEYNM !=null)
				LM_HSKEYNM.clear();
			while(L_STRTKN.hasMoreTokens())
			{
				LM_HSKEYNM.put(new Integer(L_CNT),L_STRTKN.nextToken());
				L_CNT++;
			}
		
		/*	System.out.println(L_CNT+ " "+LM_HSKEYNM.size());
			for(int i=1;i<=LM_HSKEYNM.size();i++)
			{
				System.out.println(LM_HSKEYNM.get(new Integer(L_CNT)));
			}*/
			if(LM_TBLNM.substring(0,2).equals("MM"))
			{
				setMSG("Picking up "+LM_TBLNM,'N');
				LM_STRSQL ="Select * from "+ LM_TBLNM +" WHERE "+LM_TFLNM +"='0'";
				chkTBLNM(LM_STRSQL,LM_ORGCD,LM_TBLNM,LM_TFLNM);
			}
			else if(LM_TBLNM.substring(0,2).equals("HR"))
			{
				setMSG("Picking up "+LM_TBLNM,'N');
				LM_STRSQL ="Select * from "+ LM_TBLNM +" WHERE "+LM_TFLNM +"='0'";
				chkTBLNM(LM_STRSQL,LM_ORGCD,LM_TBLNM,LM_TFLNM);
			}
			else if(LM_TBLNM.substring(0,2).equals("PR"))
			{
				setMSG("Picking up "+LM_TBLNM,'N');
				LM_STRSQL ="Select * from "+ LM_TBLNM +" WHERE "+LM_TFLNM +"='0'";
				chkTBLNM(LM_STRSQL,LM_ORGCD,LM_TBLNM,LM_TFLNM);
			}
			else if(LM_TBLNM.substring(0,2).equals("SA"))
			{
				setMSG("Picking up "+LM_TBLNM,'N');
				LM_STRSQL ="Select * from "+ LM_TBLNM +" WHERE "+LM_TFLNM +"='0'";
				chkTBLNM(LM_STRSQL,LM_ORGCD,LM_TBLNM,LM_TFLNM);
			}
			if(LM_TBLNM.equals("CO_CPMST"))
			{
				setMSG("Picking up CO_CPMST",'N');
				LM_STRSQL ="Select * from CO_CPMST WHERE CP_TRNFL ='0'";
				chkCPMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_SPMST"))
			{
				setMSG("Picking up CO_SPMST",'N');
				LM_STRSQL ="Select * from CO_SPMST where SP_TRNFL  ='0'";
				chkSPMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_SPTRN"))
			{
				setMSG("Picking up CO_SPTRN",'N');
				LM_STRSQL ="Select * from CO_SPTRN WHERE SPT_TRNFL ='0'";
				chkSPTRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_USMST"))
			{
				setMSG("Picking up CO_USMST",'N');
				LM_STRSQL ="Select * from CO_USMST WHERE US_TRNFL ='0'";
				chkUSMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_PPMST"))
			{
				setMSG("Picking up CO_PPMST",'N');
				LM_STRSQL ="Select * from CO_PPMST WHERE PP_TRNFL ='0'";
				chkPPMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_PPRTR"))
			{
				setMSG("Picking up CO_PPRTR",'N');
				LM_STRSQL ="Select * from CO_PPRTR WHERE PPR_TRNFL ='0'";
				chkPPRTR(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_PRMST"))
			{
				setMSG("Picking up CO_PRMST",'N');
				LM_STRSQL ="Select * from CO_PRMST WHERE PR_TRNFL ='0'";
				chkPRMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_CDMST"))
			{
				setMSG("Picking up CO_CDMST",'N');
				LM_STRSQL ="Select * from CO_CDMST WHERE CM_TRNFL ='0'";
				chkCDMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_CDTRN"))
			{
				setMSG("Picking up CO_CDTRN",'N');
				LM_STRSQL ="Select * from CO_CDTRN WHERE CMT_TRNFL ='0'";
				chkCDTRN(LM_STRSQL,LM_ORGCD);
			}
		
			else if(LM_TBLNM.equals("CO_PTMST"))
			{
				setMSG("Picking up CO_PTMST",'N');
				LM_STRSQL ="Select * from CO_PTMST WHERE PT_TRNFL ='0'";
				chkPTMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("CO_QPMST"))
			{
				setMSG("Picking up CO_QPMST",'N');
				LM_STRSQL ="Select * from CO_QPMST WHERE QP_TRNFL ='0'";
				chkQPMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("QC_RSMST"))
			{
				setMSG("Picking up QC_RSMST",'N');
				LM_STRSQL ="Select * from QC_RSMST WHERE RS_TRNFL ='0'";
				chkRSMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("QC_RMMST"))
			{
				setMSG("Picking up QC_RMMST",'N');
				LM_STRSQL ="Select * from QC_RMMST WHERE RM_TRNFL ='0'";
				chkRMMST(LM_STRSQL,LM_ORGCD);
			}
			
			else if(LM_TBLNM.equals("QC_PSMST"))
			{
				setMSG("Picking up QC_PSMST",'N');
				LM_STRSQL ="Select * from QC_PSMST WHERE PS_TRNFL ='0'";
				chkPSMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("QC_SMTRN"))
			{
				setMSG("Picking up QC_SMTRN",'N');
				LM_STRSQL ="Select * from QC_SMTRN WHERE SMT_TRNFL ='0'";
				chkSMTRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("QC_WTTRN"))
			{
				setMSG("Picking up QC_WTTRN",'N');
				LM_STRSQL ="Select * from QC_WTTRN WHERE WTT_TRNFL ='0'";
				chkWTTRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("FG_LCMST"))
			{
				setMSG("Picking up FG_LCMST",'N');
				LM_STRSQL ="Select * from FG_LCMST WHERE LC_TRNFL ='0'";
				chkLCMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("FG_RMMST"))
			{
				setMSG("Picking up FG_RMMST",'N');
				LM_STRSQL ="Select * from FG_RMMST WHERE RM_TRNFL ='0'";
				chkFGRMM(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("FG_RCTRN"))
			{
				setMSG("Picking up FG_RCTRN",'N');
				LM_STRSQL ="Select * from FG_RCTRN WHERE RCT_TRNFL ='0'";
				chkRCTRN(LM_STRSQL,LM_ORGCD);
			}
			
			else if(LM_TBLNM.equals("FG_LWMST"))
			{
				setMSG("Picking up FG_LWMST",'N');
				LM_STRSQL ="Select * from FG_LWMST WHERE LW_TRNFL ='0'";
				chkLWMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("FG_RSTRN"))
			{
				setMSG("Picking up FG_RSTRN",'N');
				LM_STRSQL ="Select * from FG_RSTRN WHERE RS_TRNFL ='0'";
				chkRSTRN(LM_STRSQL,LM_ORGCD);
			}
			
			else if(LM_TBLNM.equals("FG_STMST"))
			{
				setMSG("Picking up FG_STMST",'N');
				LM_STRSQL ="Select * from FG_STMST WHERE ST_TRNFL ='0'";
				chkSTMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("FG_PTFRF"))
			{
				setMSG("Picking up FG_PTFRF",'N');
				LM_STRSQL ="Select * from FG_PTFRF WHERE PTF_TRNFL ='0'";
				chkPTFRF(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("FG_ISTRN"))
			{
				setMSG("Picking up FG_ISTRN",'N');
				LM_STRSQL ="Select * from FG_ISTRN WHERE IST_TRNFL ='0'";
				chkISTRN(LM_STRSQL,LM_ORGCD);
			}
		
			else if(LM_TBLNM.equals("FG_OPSTK"))
			{
				setMSG("Picking up FG_OPSTK",'N');
				LM_STRSQL ="Select * from FG_OPSTK WHERE OP_TRNFL ='0'";
				chkOPSTK(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MR_IVTRN"))
			{
				setMSG("Picking up MR_IVTRN",'N');
				LM_STRSQL ="Select * from MR_IVTRN WHERE IVT_TRNFL ='0'";
				chkIVTRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MR_INMST"))
			{
				setMSG("Picking up MR_INMST",'N');
				LM_STRSQL ="Select * from MR_INMST WHERE IN_TRNFL ='0'";
				chkINMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MR_INTRN"))
			{
				setMSG("Picking up MR_INTRN",'N');
				LM_STRSQL ="Select * from MR_INTRN WHERE INT_TRNFL ='0'";
				chkINTRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MR_DOTRN"))
			{
				setMSG("Picking up MR_DOTRN",'N');
				LM_STRSQL ="Select * from MR_DOTRN WHERE DOT_TRNFL ='0'";
				chkDOTRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MR_DODEL"))
			{
				setMSG("Picking up "+LM_TBLNM,'N');
				LM_STRSQL ="Select * from "+ LM_TBLNM +" WHERE "+LM_TFLNM +"='0'";
				chkTBLNM(LM_STRSQL,LM_ORGCD,LM_TBLNM,LM_TFLNM);
			}
			else if(LM_TBLNM.equals("MR_RMMST"))
			{
				setMSG("Picking up MR_RMMST",'N');
				LM_STRSQL ="Select * from MR_RMMST WHERE RM_TRNFL ='0'";
				chkMRRMM(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_DVMST"))
			{
				setMSG("Picking up MM_DVMST",'N');
				LM_STRSQL ="Select * from MM_DVMST WHERE DV_TRNFL ='0'";
				chkDVMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_LRMST"))
			{
				setMSG("Picking up MM_LRMST",'N');
				LM_STRSQL ="Select * from MM_LRMST WHERE LR_TRNFL ='0'";
				chkLRMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_TKMST"))
			{
				setMSG("Picking up MM_TKMST",'N');
				LM_STRSQL ="Select * from MM_TKMST WHERE TK_TRNFL ='0'";
				chkTKMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_TKCTR"))
			{
				setMSG("Picking up MM_TKCTR",'N');
				LM_STRSQL ="Select * from MM_TKCTR WHERE TKC_TRNFL ='0'";
				chkTKCTR(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_BETRN"))
			{
				setMSG("Picking up MM_BETRN",'N');
				LM_STRSQL ="Select * from MM_BETRN WHERE BE_TRNFL ='0'";
				chkBETRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_POMST"))
			{
				setMSG("Picking up MM_POMST",'N');
				LM_STRSQL ="Select * from MM_POMST WHERE PO_TRNFL ='0'";
				chkPOMST(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_POTRN"))
			{
				setMSG("Picking up MM_POTRN",'N');
				LM_STRSQL ="Select * from MM_POTRN WHERE POT_TRNFL ='0'";
				chkPOTRN(LM_STRSQL,LM_ORGCD);
			}
			else if(LM_TBLNM.equals("MM_WBTRN"))
			{
				setMSG("Picking up MM_WBTRN",'N');
				LM_STRSQL ="Select * from MM_WBTRN WHERE WB_TRNFL ='0'";
				chkWBTRN(LM_STRSQL,LM_ORGCD);
			}
		}
		if(conSRCDB !=null)
			conSRCDB.close();
		Thread.sleep(10000);
			if(conDESDB !=null)
			{
				conDESDB.rollback();
				conDESDB.close();
			}
	
		setCursor(curDFSTS);					
		
	}
	catch(Exception L_E)
	{
		showEXMSG(L_E,"strREP","");
		return false;
	}
    setMSG("Data Replication Completed..",'N');
	return true;
}
private boolean chkCPMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_CMPCD ="",L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
		{
			return false;	
		}
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_CMPCD =  LM_RSLSRC.getString("CP_CMPCD");
			setMSG("CO_CPMST : "+L_CMPCD,'N');
	    	LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update co_cpmst set cp_trnfl ='1'");
			LM_UPDQRY.append(" WHERE CP_CMPCD ='"+L_CMPCD +"'");
			LM_WHCND.append(" WHERE CP_CMPCD ='"+L_CMPCD +"'");
			if(!insCPMST(LM_RSLSRC,"SP"))
			{
				L_UPDSTR ="UPDATE CO_CPMST SET ";
				L_UPDSTR += "CP_CMPNM = '"+nvlSTRVL(LM_RSLSRC.getString("CP_CMPNM"),"") + "',";
				L_UPDSTR += "CP_ADR01 = '"+nvlSTRVL(LM_RSLSRC.getString("CP_ADR01"),"") + "',";
				L_UPDSTR += "CP_ADR02 = '"+LM_RSLSRC.getString("CP_ADR02") + "',";
				L_UPDSTR += "CP_ADR03 = '"+LM_RSLSRC.getString("CP_ADR03") + "',";
				L_UPDSTR += "CP_ADR04 = '"+LM_RSLSRC.getString("CP_ADR04") + "',";
				L_UPDSTR += "CP_PINCD = '"+LM_RSLSRC.getString("CP_PINCD") + "',";
				L_UPDSTR += "CP_CTYNM = '"+LM_RSLSRC.getString("CP_CTYNM") + "',";
				L_UPDSTR += "CP_STACD = '"+LM_RSLSRC.getString("CP_STACD") + "',";
				L_UPDSTR += "CP_CNTCD = '"+LM_RSLSRC.getString("CP_CNTCD") + "',";
				L_UPDSTR += "CP_CONNM = '"+LM_RSLSRC.getString("CP_CONNM") + "',";
				L_UPDSTR += "CP_TEL01 = '"+LM_RSLSRC.getString("CP_TEL01") + "',";
				L_UPDSTR += "CP_TEL02 = '"+LM_RSLSRC.getString("CP_TEL02") + "',";
				L_UPDSTR += "CP_FAXNO = '"+LM_RSLSRC.getString("CP_FAXNO") + "',";
				L_UPDSTR += "CP_SSTNO = '"+LM_RSLSRC.getString("CP_SSTNO") + "',";
				L_UPDSTR += "CP_LSTNO = '"+LM_RSLSRC.getString("CP_LSTNO") + "',";
				L_UPDSTR += "CP_CSTNO = '"+LM_RSLSRC.getString("CP_CSTNO") + "',";
				L_UPDSTR += "CP_EMLRF = '"+LM_RSLSRC.getString("CP_EMLRF") + "',";
				L_UPDSTR += "CP_ECCNO = '"+LM_RSLSRC.getString("CP_ECCNO") + "',";
				L_UPDSTR += "CP_TRNFL = '"+LM_RSLSRC.getString("CP_TRNFL") + "',";
				L_UPDSTR += "CP_LUSBY = '"+LM_RSLSRC.getString("CP_LUSBY") + "',";
				L_UPDSTR += "CP_SSTDT ="+ setDATE("MDY",LM_RSLSRC.getDate("CP_SSTDT")) + ",";
				L_UPDSTR += "CP_LSTDT ="+setDATE("MDY",LM_RSLSRC.getDate("CP_LSTDT")) + ",";
				L_UPDSTR += "CP_CSTDT ="+setDATE("MDY",LM_RSLSRC.getDate("CP_CSTDT")) + ",";
				L_UPDSTR += "CP_LUPDT ="+setDATE("MDY",LM_RSLSRC.getDate("CP_LUPDT"));
                                L_UPDSTR += " "+LM_WHCND.toString().trim();
				if(chkUPDCT(L_UPDSTR))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
			LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for CO_CPMST ",'N');
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkCPMST "+L_EX.toString());
		showEXMSG(L_EX,"chkCPMST","");
		return false;
	}
	return true;
}
private boolean chkSPMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_SYSCD =  nvlSTRVL(LM_RSLSRC.getString("SP_SYSCD"),"");
			setMSG("CO_SPMST : "+L_SYSCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update co_spmst set sp_trnfl ='1'");
			LM_UPDQRY.append(" WHERE SP_SYSCD ='"+L_SYSCD +"'");
			LM_WHCND.append(" WHERE SP_SYSCD ='"+L_SYSCD +"'");
			if(!insSPMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE CO_SPMST SET ";
					L_UPDSTR += "SP_SYSNM = '"+nvlSTRVL(LM_RSLSRC.getString("SP_SYSNM"),"") + "',";
					L_UPDSTR += "SP_VERNO = '"+LM_RSLSRC.getString("SP_VERNO") + "',";
					L_UPDSTR += "SP_SYSRM = '"+LM_RSLSRC.getString("SP_SYSRM") + "',";
					L_UPDSTR += "SP_TRNFL = '"+LM_RSLSRC.getString("SP_TRNFL") + "',";
					L_UPDSTR += "SP_LUSBY = '"+LM_RSLSRC.getString("SP_LUSBY") + "',";
					L_UPDSTR += "SP_IMPDT = "+setDATE("MDY",LM_RSLSRC.getDate("SP_IMPDT")) + ",";
					L_UPDSTR += "SP_LMDDT = "+setDATE("MDY",LM_RSLSRC.getDate("SP_LMDDT")) + ",";
					L_UPDSTR += "SP_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("SP_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
						setMSG("Could not update the record for "+ L_SYSCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updSPMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_SPMST ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkSPMST "+L_EX.toString());
		showEXMSG(L_EX,"chkSPMST","");
		return false;
	}
	return true;
}
private boolean chkSPTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD,L_SYSLC,L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_SYSCD =  nvlSTRVL(LM_RSLSRC.getString("SPT_SYSCD"),"");
			L_SYSLC =  nvlSTRVL(LM_RSLSRC.getString("SPT_SYSLC"),"");
			setMSG("CO_SPTRN : "+L_SYSCD+" / "+L_SYSLC,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE SPT_SYSCD ='"+L_SYSCD +"' AND SPT_SYSLC ='"+L_SYSLC+"'");
			LM_UPDQRY.append("UPDATE CO_SPTRN SET SPT_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insSPTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE CO_SPTRN SET ";
					L_UPDSTR += "SPT_DTBLB = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_DTBLB"),"") + "',";
					L_UPDSTR += "SPT_DTBUS = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_DTBUS"),"") + "',";
					L_UPDSTR += "SPT_DTBPW = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_DTBPW"),"") + "',";
					L_UPDSTR += "SPT_DBSNM = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_DBSNM"),"") + "',";
					L_UPDSTR += "SPT_DBSLC = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_DBSLC"),"") + "',";
					L_UPDSTR += "SPT_SRCLB = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_SRCLB"),"") + "',";
					L_UPDSTR += "SPT_EXELB = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_EXELB"),"") + "',";
					L_UPDSTR += "SPT_DOCLB = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_DOCLB"),"") + "',";
					L_UPDSTR += "SPT_REPLB = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_REPLB"),"") + "',";
					L_UPDSTR += "SPT_SNDLC = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_SNDLC"),"") + "',";
					L_UPDSTR += "SPT_RECLC = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_RECLC"),"") + "',";
					L_UPDSTR += "SPT_BKPLC = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_BKPLC"),"") + "',";
					L_UPDSTR += "SPT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_TRNFL"),"") + "',";
					L_UPDSTR += "SPT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("SPT_LUSBY"),"") + "',";
					L_UPDSTR += "SPT_YSTDT = "+setDATE("MDY",LM_RSLSRC.getDate("SPT_YSTDT")) + ",";
					L_UPDSTR += "SPT_YENDT = "+setDATE("MDY",LM_RSLSRC.getDate("SPT_YENDT")) + ",";
					L_UPDSTR += "SPT_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("SPT_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
				}
				catch(SQLException L_SE)
				{
					System.out.println("Error from chkSPTRN "+L_SE.toString());
					showEXMSG(L_SE,"updSPTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_SPTRN ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkSPTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkSPTRN","");
		return false;
	}
	return true;
}
private boolean chkUSMST(String LP_STRSQL,String LP_ORGCD)
{
	
	ResultSet L_RSLSET;
	String L_USRCD ="",L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_USRCD =  nvlSTRVL(LM_RSLSRC.getString("US_USRCD"),"");
			setMSG("CO_USMST : "+L_USRCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE US_USRCD ='"+L_USRCD +"'");
			LM_UPDQRY.append("UPDATE CO_USMST SET US_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insUSMST(LM_RSLSRC,"SP"))
			{
				try
				{	
					L_UPDSTR = "Update CO_USMST SET ";
					L_UPDSTR += "US_USRNM = '"+LM_RSLSRC.getString("US_USRNM") + "',";
					L_UPDSTR += "US_USRPW = '"+LM_RSLSRC.getString("US_USRPW") + "',";
					L_UPDSTR += "US_USRTP = '"+LM_RSLSRC.getString("US_USRTP") + "',";
					L_UPDSTR += "US_PWMFL = '"+LM_RSLSRC.getString("US_PWMFL") + "',";
					L_UPDSTR += "US_EMLRF = '"+LM_RSLSRC.getString("US_EMLRF") + "',";
					L_UPDSTR += "US_APPLS = '"+LM_RSLSRC.getString("US_APPLS") + "',";
					L_UPDSTR += "US_TRNFL = '"+LM_RSLSRC.getString("US_TRNFL") + "',";
					L_UPDSTR += "US_LUSBY = '"+LM_RSLSRC.getString("US_LUSBY") + "',";
					L_UPDSTR += "US_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("US_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"insUSMST","");
					return false;
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_USMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkUSMST "+L_EX.toString());
		showEXMSG(L_EX,"chkUSMST","");
		return false;
	}
	return true;
}
private boolean chkPPMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD,L_PRGCD,L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_SYSCD =  nvlSTRVL(LM_RSLSRC.getString("PP_SYSCD"),"");
			L_PRGCD =  nvlSTRVL(LM_RSLSRC.getString("PP_PRGCD"),"");
			setMSG("CO_PPMST : "+L_SYSCD+" / "+L_PRGCD,'N');
				LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE PP_SYSCD ='"+L_SYSCD +"' AND PP_PRGCD ='"+L_PRGCD+"'");
			LM_UPDQRY.append("UPDATE CO_PPMST SET PP_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPPMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE CO_PPMST SET ";
					L_UPDSTR += "PP_PRGDS = '"+LM_RSLSRC.getString("PP_PRGDS") + "',";
					L_UPDSTR += "PP_PRGTP = '"+LM_RSLSRC.getString("PP_PRGTP") + "',";
					L_UPDSTR += "PP_DEVBY = '"+LM_RSLSRC.getString("PP_DEVBY") + "',";
					L_UPDSTR += "PP_LMDBY = '"+LM_RSLSRC.getString("PP_LMDBY") + "',";
					L_UPDSTR += "PP_VERNO = '"+LM_RSLSRC.getString("PP_VERNO") + "',";
					L_UPDSTR += "PP_HITCT = "+LM_RSLSRC.getString("PP_HITCT") + ",";
					L_UPDSTR += "PP_LHTBY = '"+LM_RSLSRC.getString("PP_LHTBY") + "',";
					L_UPDSTR += "PP_PRGRM = '"+LM_RSLSRC.getString("PP_PRGRM") + "',";
					L_UPDSTR += "PP_TRNFL = '"+LM_RSLSRC.getString("PP_TRNFL") + "',";
					L_UPDSTR += "PP_LUSBY = '"+LM_RSLSRC.getString("PP_LUSBY") + "',";
					L_UPDSTR += "PP_PRGHD = '"+LM_RSLSRC.getString("PP_PRGHD") + "',";
					L_UPDSTR += "PP_IMPDT = "+setDATE("MDY",LM_RSLSRC.getDate("PP_IMPDT")) + ",";
					L_UPDSTR += "PP_LMDDT = "+setDATE("MDY",LM_RSLSRC.getDate("PP_LMDDT")) + ",";
					L_UPDSTR += "PP_LHTDT = "+setDATE("MDY",LM_RSLSRC.getDate("PP_LHTDT")) + ",";
					L_UPDSTR += "PP_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("PP_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim() ;
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updPPMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_PPMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkPPMST "+L_EX.toString());
		showEXMSG(L_EX,"chkPPMST","");
		return false;
	}
	return true;
}
private boolean chkPPRTR(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD,L_PRGCD,L_USRTP,L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_SYSCD=  nvlSTRVL(LM_RSLSRC.getString("PPR_SYSCD"),"");
			L_PRGCD =  nvlSTRVL(LM_RSLSRC.getString("PPR_PRGCD"),"");
			L_USRTP =  nvlSTRVL(LM_RSLSRC.getString("PPR_USRTP"),"");
			setMSG("CO_PPRTR : "+L_SYSCD+" / "+L_PRGCD +" / "+L_USRTP ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE PPR_SYSCD ='"+L_SYSCD +"' AND PPR_PRGCD ='"+L_PRGCD+"' AND PPR_USRTP ='"+L_USRTP+"'");
			LM_UPDQRY.append("UPDATE CO_PPRTR SET PPR_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPPRTR(LM_RSLSRC,"SP"))
			{
			try
			{
				L_UPDSTR = "UPDATE CO_PPRTR SET ";
				L_UPDSTR += "PPR_ADDFL = '"+LM_RSLSRC.getString("PPR_ADDFL") + "',";
				L_UPDSTR += "PPR_MODFL = '"+LM_RSLSRC.getString("PPR_MODFL") + "',";
				L_UPDSTR += "PPR_DELFL = '"+LM_RSLSRC.getString("PPR_DELFL") + "',";
				L_UPDSTR += "PPR_ENQFL = '"+LM_RSLSRC.getString("PPR_ENQFL") + "',";
				L_UPDSTR += "PPR_REPFL = '"+LM_RSLSRC.getString("PPR_REPFL") + "',";
				L_UPDSTR += "PPR_PROFL = '"+LM_RSLSRC.getString("PPR_PROFL") + "',";
				L_UPDSTR += "PPR_TRNFL = '"+LM_RSLSRC.getString("PPR_TRNFL") + "',";
				L_UPDSTR += "PPR_LUSBY = '"+LM_RSLSRC.getString("PPR_LUSBY") + "',";
				L_UPDSTR += "PPR_LUPDT ="+setDATE("MDY",LM_RSLSRC.getDate("PPR_LUPDT"));
                                L_UPDSTR += " "+LM_WHCND.toString().trim();
				if(chkUPDCT(L_UPDSTR))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
				
			}catch(SQLException L_SE)
			{
				showEXMSG(L_SE,"insPPRTR","");
			}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_PPRTR ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkPPRTR "+L_EX.toString());
		showEXMSG(L_EX,"chkPPRTR","");
		return false;
	}
	return true;
}
private boolean chkCDMST(String LP_STRSQL,String LP_ORGCD)
{
	String L_CGMTP="";
	String L_CGSTP="",L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_CGMTP=  nvlSTRVL(LM_RSLSRC.getString("CM_CGMTP"),"");
			L_CGSTP =  nvlSTRVL(LM_RSLSRC.getString("CM_CGSTP"),"");
			setMSG("CO_CDMST : "+L_CGMTP+" / "+L_CGSTP ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE CM_CGMTP ='"+L_CGMTP +"' AND CM_CGSTP ='"+L_CGSTP+"'");
			LM_UPDQRY.append("UPDATE CO_CDMST SET CM_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insCDMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR  = "UPDATE CO_CDMST SET ";
					L_UPDSTR += "CM_CGRDS ='"+nvlSTRVL(LM_RSLSRC.getString("CM_CGRDS"),"") + "',";
					L_UPDSTR +=	"CM_CODLN = "+nvlSTRVL(LM_RSLSRC.getString("CM_CODLN"),"0") + ",";
					L_UPDSTR += "CM_MODFL = '"+nvlSTRVL(LM_RSLSRC.getString("CM_MODFL"),"") + "',";
					L_UPDSTR += "CM_APPLS = '"+nvlSTRVL(LM_RSLSRC.getString("CM_APPLS"),"") + "',";
					L_UPDSTR += "CM_CODRM = '"+nvlSTRVL(LM_RSLSRC.getString("CM_CODRM"),"") + "',";
					L_UPDSTR += "CM_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("CM_TRNFL"),"") + "',";
					L_UPDSTR += "CM_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("CM_LUSBY"),"") + "',";
					L_UPDSTR += "CM_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("CM_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						setMSG("updated record "+LM_CGMTP+" / "+LM_CGSTP,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
						setMSG("could not update record "+LM_CGMTP+" / "+LM_CGSTP,'E');
					}
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updCDMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_CDMST ",'N');
		LM_RSLSRC.close();
	}
	catch(Exception L_EX)
	{
		
		showEXMSG(L_EX,"chkCDMST","");
		return false;
	}
	return true;
}
private boolean chkCDTRN(String LP_STRSQL,String LP_ORGCD)
{
	String L_CGMTP="";
	String L_CGSTP="";
	String L_CODCD="",L_UPDSTR ="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_CGMTP=  nvlSTRVL(LM_RSLSRC.getString("CMT_CGMTP"),"");
			L_CGSTP =  nvlSTRVL(LM_RSLSRC.getString("CMT_CGSTP"),"");
			L_CODCD =  nvlSTRVL(LM_RSLSRC.getString("CMT_CODCD"),"");
			setMSG("CO_CDTRN : "+L_CGMTP+" / "+L_CGSTP+" / "+L_CODCD ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE CMT_CGMTP ='"+L_CGMTP +"' AND CMT_CGSTP ='"+L_CGSTP+"' AND CMT_CODCD ='"+L_CODCD+"'");
			LM_UPDQRY.append("UPDATE CO_CDTRN SET CMT_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insCDTRN(LM_RSLSRC,"SP"))
			{
				L_UPDSTR ="UPDATE CO_CDTRN SET ";
				L_UPDSTR +="CMT_CODDS = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_CODDS"),"")+"',";
				L_UPDSTR +="CMT_SHRDS = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_SHRDS"),"")+"',";
				L_UPDSTR +="CMT_CHP01 = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_CHP01"),"")+"',";
				L_UPDSTR +="CMT_CHP02 = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_CHP02"),"")+"',";
				L_UPDSTR +="CMT_NMP01 = "+nvlSTRVL(LM_RSLSRC.getString("CMT_NMP01"),"")+",";
				L_UPDSTR +="CMT_NMP02 = "+nvlSTRVL(LM_RSLSRC.getString("CMT_NMP02"),"")+",";
				L_UPDSTR +="CMT_NCSVL = "+nvlSTRVL(LM_RSLSRC.getString("CMT_NCSVL"),"")+",";
				L_UPDSTR +="CMT_CCSVL = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_CCSVL"),"")+"',";
				L_UPDSTR +="CMT_MODLS = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_MODLS"),"")+"',";
				L_UPDSTR +="CMT_TRNFL = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_TRNFL"),"")+"',";
				L_UPDSTR +="CMT_LUSBY = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_LUSBY"),"")+"',";
				L_UPDSTR +="CMT_STSFL = "+"'"+nvlSTRVL(LM_RSLSRC.getString("CMT_STSFL"),"")+"',";
				L_UPDSTR +="CMT_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("CMT_LUPDT"));
                                L_UPDSTR += " "+LM_WHCND.toString().trim();
				if(chkUPDCT(L_UPDSTR))
				{
					conDESDB.commit();
					setMSG("updated record "+LM_CGMTP+" / "+LM_CGSTP,'N');
					LM_UPDFL = true;
				}
				else
				{
					conDESDB.rollback();
					setMSG("could not update record "+LM_CGMTP+" / "+LM_CGSTP,'E');
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_CDTRN ",'N');
		LM_RSLSRC.close();
	}
	catch(Exception L_EX)
	{
		
		showEXMSG(L_EX,"chkCDTRN","");
		return false;
	}
	return true;
}
private boolean chkPRMST(String LP_STRSQL,String LP_ORGCD)
{
	String L_PRDCD="",L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_PRDCD=  nvlSTRVL(LM_RSLSRC.getString("PR_PRDCD"),"");
			setMSG("CO_PRMST : "+L_PRDCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE PR_PRDCD ='"+L_PRDCD +"'");
			LM_UPDQRY.append("UPDATE CO_PRMST SET PR_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPRMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE CO_PRMST SET ";
					L_UPDSTR += "PR_PRDDS = '"+nvlSTRVL(LM_RSLSRC.getString("PR_PRDDS"),"") + "',";
					L_UPDSTR += "PR_UOMCD = '"+nvlSTRVL(LM_RSLSRC.getString("PR_UOMCD"),"") + "',";
					L_UPDSTR += "PR_CSTQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_CSTQT"),"0") + ",";
					L_UPDSTR += "PR_USTQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_USTQT"),"0") + ",";
					L_UPDSTR += "PR_RSTQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_RSTQT"),"0") + ",";
					L_UPDSTR += "PR_MOSQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_MOSQT"),"0") + ",";
					L_UPDSTR += "PR_YOSQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_YOSQT"),"0") + ",";
					L_UPDSTR += "PR_TBGQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_TBGQT"),"0") + ",";
					L_UPDSTR += "PR_MBGQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_MBGQT"),"0") + ",";
					L_UPDSTR += "PR_YBGQT ="+nvlSTRVL(LM_RSLSRC.getString("PR_YBGQT"),"0") + ",";
					L_UPDSTR += "PR_PINNO = '"+nvlSTRVL(LM_RSLSRC.getString("PR_PINNO"),"") + "',";
					L_UPDSTR += "PR_PINRT = "+nvlSTRVL(LM_RSLSRC.getString("PR_PINRT"),"0") + ",";
					L_UPDSTR += "PR_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("PR_TRNFL"),"") + "',";
					L_UPDSTR += "PR_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("PR_STSFL"),"") + "',";
					L_UPDSTR += "PR_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("PR_LUSBY"),"") + "',";
					L_UPDSTR += "PR_PRDTP = '"+nvlSTRVL(LM_RSLSRC.getString("PR_PRDTP"),"") + "',";
					L_UPDSTR += "PR_GRDDS = '"+nvlSTRVL(LM_RSLSRC.getString("PR_GRDDS"),"") + "',";
					L_UPDSTR += "PR_PINDT = "+setDATE("MDY",LM_RSLSRC.getDate("PR_PINDT")) + ",";
					L_UPDSTR += "PR_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("PR_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updPRMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_PRMST ",'N');
		LM_RSLSRC.close();
	}
	catch(Exception L_EX)
	{
		
		showEXMSG(L_EX,"chkPRMST","");
		return false;
	}
	return true;
}
private boolean chkPTMST(String LP_STRSQL,String LP_ORGCD)
{
	String L_PRTTP="";
	String L_PRTCD="",L_UPDSTR="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_PRTTP=  nvlSTRVL(LM_RSLSRC.getString("PT_PRTTP"),"");
			L_PRTCD =  nvlSTRVL(LM_RSLSRC.getString("PT_PRTCD"),"");
			setMSG("CO_PTMST : "+L_PRTTP+" / "+L_PRTCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE PT_PRTTP ='"+L_PRTTP +"' AND PT_PRTCD ='"+L_PRTCD+"'");
			LM_UPDQRY.append("UPDATE CO_PTMST SET PT_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPTMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE CO_PTMST SET ";
					L_UPDSTR += "PT_PRTNM = '"+nvlSTRVL(LM_RSLSRC.getString("PT_PRTNM"),"") + "',";
					L_UPDSTR += "PT_SHRNM = '"+nvlSTRVL(LM_RSLSRC.getString("PT_SHRNM"),"") + "',";
					L_UPDSTR += "PT_GRPCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_GRPCD"),"") + "',";
					L_UPDSTR += "PT_ADR01 = '"+nvlSTRVL(LM_RSLSRC.getString("PT_ADR01"),"") + "',";
					L_UPDSTR += "PT_ADR02 = '"+nvlSTRVL(LM_RSLSRC.getString("PT_ADR02"),"") + "',";
					L_UPDSTR += "PT_ADR03 = '"+nvlSTRVL(LM_RSLSRC.getString("PT_ADR03"),"") + "',";
					L_UPDSTR += "PT_ADR04 = '"+nvlSTRVL(LM_RSLSRC.getString("PT_ADR04"),"") + "',";
					L_UPDSTR += "PT_PINCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_PINCD"),"") + "',";
					L_UPDSTR += "PT_CTYNM = '"+nvlSTRVL(LM_RSLSRC.getString("PT_CTYNM"),"") + "',";
					L_UPDSTR += "PT_STACD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_STACD"),"") + "',";
					L_UPDSTR += "PT_CNTCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_CNTCD"),"") + "',";
					L_UPDSTR += "PT_CONNM = '"+nvlSTRVL(LM_RSLSRC.getString("PT_CONNM"),"") + "',";
					L_UPDSTR += "PT_TEL01 = '"+nvlSTRVL(LM_RSLSRC.getString("PT_TEL01"),"") + "',";
					L_UPDSTR += "PT_TEL02 = '"+nvlSTRVL(LM_RSLSRC.getString("PT_TEL02"),"") + "',";
					L_UPDSTR += "PT_EMLRF = '"+nvlSTRVL(LM_RSLSRC.getString("PT_EMLRF"),"") + "',";
					L_UPDSTR += "PT_FAXNO = '"+nvlSTRVL(LM_RSLSRC.getString("PT_FAXNO"),"") + "',";
					L_UPDSTR += "PT_INFFL = '"+nvlSTRVL(LM_RSLSRC.getString("PT_INFFL"),"") + "',";
					L_UPDSTR += "PT_STXNO = '"+nvlSTRVL(LM_RSLSRC.getString("PT_STXNO"),"") + "',";
					L_UPDSTR += "PT_CLSCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_CLSCD"),"") + "',";
					L_UPDSTR += "PT_SCRCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_SCRCD"),"") + "',";
					L_UPDSTR += "PT_CSTNO = '"+nvlSTRVL(LM_RSLSRC.getString("PT_CSTNO"),"") + "',";
					L_UPDSTR += "PT_ECCNO = '"+nvlSTRVL(LM_RSLSRC.getString("PT_ECCNO"),"") + "',";
					L_UPDSTR += "PT_ITPNO = '"+nvlSTRVL(LM_RSLSRC.getString("PT_ITPNO"),"") + "',";
					L_UPDSTR += "PT_EXCNO = '"+nvlSTRVL(LM_RSLSRC.getString("PT_EXCNO"),"") + "',";
					L_UPDSTR += "PT_RNGDS = '"+nvlSTRVL(LM_RSLSRC.getString("PT_RNGDS"),"") + "',";
					L_UPDSTR += "PT_DIVDS = '"+nvlSTRVL(LM_RSLSRC.getString("PT_DIVDS"),"") + "',";
					L_UPDSTR += "PT_CLLDS = '"+nvlSTRVL(LM_RSLSRC.getString("PT_CLLDS"),"") + "',";
					L_UPDSTR += "PT_ZONCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_ZONCD"),"") + "',";
					L_UPDSTR += "PT_SALVL ="+nvlSTRVL(LM_RSLSRC.getString("PT_SALVL"),"0") + ",";
					L_UPDSTR += "PT_TRNCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_TRNCD"),"") + "',";
					L_UPDSTR += "PT_DSTCD = '"+nvlSTRVL(LM_RSLSRC.getString("PT_DSTCD"),"") + "',";
					L_UPDSTR += "PT_TSTFL = '"+nvlSTRVL(LM_RSLSRC.getString("PT_TSTFL"),"") + "',";
					L_UPDSTR += "PT_YOPCR = "+nvlSTRVL(LM_RSLSRC.getString("PT_YOPCR"),"0") + ",";
					L_UPDSTR += "PT_YOPDB = "+nvlSTRVL(LM_RSLSRC.getString("PT_YOPDB"),"0") + ",";
					L_UPDSTR += "PT_YTDCR = "+nvlSTRVL(LM_RSLSRC.getString("PT_YTDCR"),"0") + ",";
					L_UPDSTR += "PT_YTDDB = "+nvlSTRVL(LM_RSLSRC.getString("PT_YTDDB"),"0") + ",";
					L_UPDSTR += "PT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("PT_STSFL"),"") + "',";
					L_UPDSTR += "PT_TRNFL ='"+nvlSTRVL(LM_RSLSRC.getString("PT_TRNFL"),"") + "',";
					L_UPDSTR += "PT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("PT_LUSBY"),"") + "',";
					L_UPDSTR += "PT_MOBNO = '"+nvlSTRVL(LM_RSLSRC.getString("PT_MOBNO"),"") + "',";
					L_UPDSTR += "PT_COWEB = '"+nvlSTRVL(LM_RSLSRC.getString("PT_COWEB"),"") + "',";
					L_UPDSTR += "PT_EMLPR = '"+nvlSTRVL(LM_RSLSRC.getString("PT_EMLPR"),"") + "',";
					L_UPDSTR += "PT_EMLMR = '"+nvlSTRVL(LM_RSLSRC.getString("PT_EMLMR"),"") + "',";
					L_UPDSTR += "PT_EMLAC = '"+nvlSTRVL(LM_RSLSRC.getString("PT_EMLAC"),"") + "',";
					L_UPDSTR += "PT_STXDT = "+setDATE("MDY",LM_RSLSRC.getDate("PT_STXDT")) + ",";
					L_UPDSTR += "PT_CSTDT = "+setDATE("MDY",LM_RSLSRC.getDate("PT_CSTDT")) + ",";
					L_UPDSTR += "PT_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("PT_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
				
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
				}
				catch(SQLException L_SE)
				{
					showEXMSG(L_SE,"updPTMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_PTMST ",'N');
		LM_RSLSRC.close();
	}
	catch(Exception L_EX)
	{
		
		showEXMSG(L_EX,"chkPTMST","");
		return false;
	}
	return true;
}
private boolean chkQPMST(String LP_STRSQL,String LP_ORGCD)
{
	String L_UPDSTR="";
	String L_QCATP,L_TSTTP,L_PRDCD,L_QPRCD,L_STRDT,L_SRLNO;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_QCATP =  nvlSTRVL(LM_RSLSRC.getString("QP_QCATP"),"");
			L_TSTTP =  nvlSTRVL(LM_RSLSRC.getString("QP_TSTTP"),"");
			L_PRDCD =  nvlSTRVL(LM_RSLSRC.getString("QP_PRDCD"),"");
			L_QPRCD =  nvlSTRVL(LM_RSLSRC.getString("QP_QPRCD"),"");
			L_STRDT =  setDATE("MDY",LM_RSLSRC.getDate("QP_STRDT"));
			L_SRLNO =  nvlSTRVL(LM_RSLSRC.getString("QP_SRLNO"),"");
			setMSG("CO_QPMST : "+L_PRDCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE QP_QCATP ='"+L_QCATP +"' and QP_TSTTP ='"+L_TSTTP +"' AND QP_PRDCD ='"+L_PRDCD +"' AND QP_QPRCD ='"+L_QPRCD +"' AND QP_STRDT = "+L_STRDT +" AND QP_SRLNO ='"+L_SRLNO +"'");
			LM_UPDQRY.append("UPDATE CO_QPMST SET QP_TRNFL ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insQPMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR =" UPDATE CO_QPMST SET ";
					L_UPDSTR += "QP_ENDDT =" +setDATE("MDY",LM_RSLSRC.getDate("QP_ENDDT")) + ",";
					L_UPDSTR += "QP_NPFVL ="+nvlSTRVL(LM_RSLSRC.getString("QP_NPFVL"),"0")+",";
					L_UPDSTR += "QP_NPTVL ="+nvlSTRVL(LM_RSLSRC.getString("QP_NPTVL"),"0")+",";
					L_UPDSTR += "QP_STDVL ="+nvlSTRVL(LM_RSLSRC.getString("QP_STDVL"),"0")+",";
					L_UPDSTR += "QP_TXTVL ='"+nvlSTRVL(LM_RSLSRC.getString("QP_TXTVL"),"")+"',";
					L_UPDSTR += "QP_CMPFL = '"+nvlSTRVL(LM_RSLSRC.getString("QP_CMPFL"),"")+"',";
					L_UPDSTR += "QP_TSMCD = '"+nvlSTRVL(LM_RSLSRC.getString("QP_TSMCD"),"")+"',";
					L_UPDSTR += "QP_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("QP_TRNFL"),"")+"',";
					L_UPDSTR += "QP_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("QP_LUSBY"),"")+"',";
					L_UPDSTR += "QP_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("QP_LUPDT")) + ",";
					L_UPDSTR += "QP_QPRDS = '"+nvlSTRVL(LM_RSLSRC.getString("QP_QPRDS"),"")+"',";
					L_UPDSTR += "QP_UOMDS = '"+nvlSTRVL(LM_RSLSRC.getString("QP_UOMDS"),"")+"',";
					L_UPDSTR += "QP_TSMDS = '"+nvlSTRVL(LM_RSLSRC.getString("QP_TSMDS"),"")+"',";
					L_UPDSTR += "QP_PRGFL = '"+nvlSTRVL(LM_RSLSRC.getString("QP_PRGFL"),"")+"',";
					L_UPDSTR += " QP_ORDBY = '"+nvlSTRVL(LM_RSLSRC.getString("QP_ORDBY"),"")+"'";
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updQPMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}

		}
		setMSG("checking completed for CO_QPMST ",'N');
		LM_RSLSRC.close();
	}
	catch(Exception L_EX)
	{
		
		showEXMSG(L_EX,"chkPRMST","");
		return false;
	}
	return true;
}
private boolean insQPMST(ResultSet LP_RSLSET,String  LP_SYSCD)
{
	try
	{
		LM_TMPSTR = "Insert Into CO_QPMST(QP_QCATP,QP_TSTTP,QP_PRDCD,QP_QPRCD,QP_STRDT,QP_SRLNO,QP_ENDDT,QP_NPFVL,QP_NPTVL,QP_STDVL,QP_TXTVL,QP_CMPFL,QP_TSMCD,QP_TRNFL,QP_LUSBY,QP_LUPDT,QP_QPRDS,QP_UOMDS,QP_TSMDS,QP_PRGFL,QP_ORDBY)values(";
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_QCATP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_TSTTP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_PRDCD"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_QPRCD"),"")+"',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("QP_STRDT")) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_SRLNO"),"")+"',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("QP_ENDDT")) + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("QP_NPFVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("QP_NPTVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("QP_STDVL"),"0")+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_TXTVL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_CMPFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_TSMCD"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_TRNFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_LUSBY"),"")+"',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("QP_LUPDT")) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_QPRDS"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_UOMDS"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_TSMDS"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("QP_PRGFL"),"")+"',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("QP_ORDBY"),"0")+")";
		if(chkUPDCT(LM_STLSQL))
			return true;
		else
			return false;
	}
	catch(SQLException L_SE)
	{
		showEXMSG(L_SE,"crtQPMST","");
		return false;
	}
	}	

private boolean insCPMST(ResultSet LP_RSLSET,String LP_SYSCD){
try
{
	LM_TMPSTR  = "Insert Into CO_CPMST(CP_CMPCD,CP_CMPNM,CP_ADR01,CP_ADR02,CP_ADR03,CP_ADR04,CP_PINCD,CP_CTYNM,CP_STACD,CP_CNTCD,CP_CONNM,CP_TEL01,CP_TEL02,CP_FAXNO,CP_SSTNO,CP_LSTNO,";
	LM_TMPSTR  += "CP_CSTNO,CP_EMLRF,CP_ECCNO,CP_TRNFL,CP_LUSBY,CP_SSTDT,CP_LSTDT,CP_CSTDT,CP_LUPDT)";
	LM_TMPSTR  += " values ("; 
	LM_STLSQL = LM_TMPSTR;
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_CMPCD").trim() + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CP_CMPNM"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CP_ADR01"),"") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_ADR02") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_ADR03") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_ADR04") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_PINCD") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_CTYNM") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_STACD") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_CNTCD") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_CONNM") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_TEL01") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_TEL02") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_FAXNO") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_SSTNO") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_LSTNO") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_CSTNO") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_EMLRF") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_ECCNO") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_TRNFL") + "',";
	LM_STLSQL += "'"+LP_RSLSET.getString("CP_LUSBY") + "',";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("CP_SSTDT")) + ",";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("CP_LSTDT")) + ",";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("CP_CSTDT")) + ",";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("CP_LUPDT")) + ")";

	return chkUPDCT(LM_STLSQL);
			
}
catch(SQLException L_SE)
{
	showEXMSG(L_SE,"insCPMST","");
	return false;
		
}
}
private boolean insSPMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into CO_SPMST(SP_SYSCD,SP_SYSNM,SP_VERNO,SP_SYSRM,";
		LM_TMPSTR  += "SP_TRNFL,SP_LUSBY,SP_IMPDT,SP_LMDDT,SP_LUPDT) values (";
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SP_SYSCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SP_SYSNM"),"") + "',";
		LM_STLSQL += "'"+LP_RSLSET.getString("SP_VERNO") + "',";
		LM_STLSQL += "'"+LP_RSLSET.getString("SP_SYSRM") + "',";
		LM_STLSQL += "'"+LP_RSLSET.getString("SP_TRNFL") + "',";
		LM_STLSQL += "'"+LP_RSLSET.getString("SP_LUSBY") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("SP_IMPDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("SP_LMDDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("SP_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insSPMST","");
		return false;
	}
}
private boolean insSPTRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into CO_SPTRN(SPT_SYSCD,SPT_SYSLC,SPT_DTBLB,SPT_DTBUS,SPT_DTBPW,SPT_DBSNM,";
		LM_TMPSTR  += "SPT_DBSLC,SPT_SRCLB,SPT_EXELB,SPT_DOCLB,SPT_REPLB,SPT_SNDLC,SPT_RECLC,SPT_BKPLC,";
		LM_TMPSTR  += "SPT_TRNFL,SPT_LUSBY,SPT_YSTDT,SPT_YENDT,SPT_LUPDT) values (";
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_SYSCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_SYSLC"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_DTBLB"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_DTBUS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_DTBPW"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_DBSNM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_DBSLC"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_SRCLB"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_EXELB"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_DOCLB"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_REPLB"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_SNDLC"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_RECLC"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_BKPLC"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SPT_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("SPT_YSTDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("SPT_YENDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("SPT_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insSPTRN","");
		return false;
	}
}
	
private boolean insPPMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into CO_PPMST(PP_SYSCD,PP_PRGCD,PP_PRGDS,PP_PRGTP,PP_DEVBY,PP_LMDBY,";
		LM_TMPSTR  += "PP_VERNO,PP_HITCT,PP_LHTBY,PP_PRGRM,PP_TRNFL,PP_LUSBY,PP_PRGHD,PP_IMPDT,PP_LMDDT,PP_LHTDT,PP_LUPDT)";
		LM_TMPSTR  += " values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_SYSCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_PRGCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_PRGDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_PRGTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_DEVBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_LMDBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_VERNO"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PP_HITCT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_LHTBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_PRGRM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_LUSBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PP_PRGHD"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PP_IMPDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PP_LMDDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PP_LHTDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PP_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insPPMST","");
		return false;
	}
}
	
private boolean insUSMST(ResultSet LP_RSLSET,String LP_SYSCD){
	try
	{
		LM_TMPSTR  = "Insert Into CO_USMST(US_USRCD,US_USRNM,US_USRPW,US_USRTP,US_PWMFL,US_EMLRF,";
		LM_TMPSTR  += "US_APPLS,US_TRNFL,US_LUSBY,US_LUPDT) values (";
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_USRCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_USRNM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_USRPW"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_USRTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_PWMFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_EMLRF"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_APPLS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("US_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("US_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insUSMST","");
		return false;
	}
}
	
private boolean insPPRTR(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into CO_PPRTR(PPR_SYSCD,PPR_PRGCD,PPR_USRTP,PPR_ADDFL,PPR_MODFL,";
		LM_TMPSTR  += "PPR_DELFL,PPR_ENQFL,PPR_REPFL,PPR_PROFL,PPR_TRNFL,PPR_LUSBY,PPR_LUPDT)";
		LM_TMPSTR  += " values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_SYSCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_PRGCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_USRTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_ADDFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_MODFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_DELFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_ENQFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_REPFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_PROFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PPR_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PPR_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
	}catch(SQLException L_SE)
	{
		showEXMSG(L_SE,"insPPRTR","");
		return false;
	}
}
private boolean insCDMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
		try
		{
			LM_TMPSTR  = "Insert Into CO_CDMST(CM_CGMTP,CM_CGSTP,CM_CGRDS,CM_CODLN,CM_MODFL,CM_APPLS,";
			LM_TMPSTR  += "CM_CODRM,CM_TRNFL,CM_LUSBY,CM_LUPDT) values (";
			LM_STLSQL = LM_TMPSTR;
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_CGMTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_CGSTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_CGRDS"),"") + "',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("CM_CODLN"),"0") + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_MODFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_APPLS"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_CODRM"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_TRNFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CM_LUSBY"),"") + "',";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("CM_LUPDT")) + ")";
			return chkUPDCT(LM_STLSQL);
			
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtCDMST","");
			return false;
		}
	}
	private boolean insCDTRN(ResultSet LP_RSLSET,String LP_SYSCD)
	{
		try
		{
			LM_TMPSTR  = "Insert Into CO_CDTRN(CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_SHRDS,";
			LM_TMPSTR  += "CMT_CHP01,CMT_CHP02,CMT_NMP01,CMT_NMP02,CMT_NCSVL,CMT_CCSVL,CMT_MODLS,";
			LM_TMPSTR  += "CMT_TRNFL,CMT_LUSBY,CMT_STSFL,CMT_LUPDT) values (";
			LM_STLSQL = LM_TMPSTR; 
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_CGMTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_CGSTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_CODCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_CODDS"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_SHRDS"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_CHP01"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_CHP02"),"") + "',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("CMT_NMP01"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("CMT_NMP02"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("CMT_NCSVL"),"0") + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_CCSVL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_MODLS"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_TRNFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_LUSBY"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("CMT_STSFL"),"") + "',";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("CMT_LUPDT")) + ")";
			return chkUPDCT(LM_STLSQL);
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"insCDTRN","");
			return false;
		}
	}
private boolean insPRMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into CO_PRMST(PR_PRDCD,PR_PRDDS,PR_UOMCD,PR_CSTQT,PR_USTQT,PR_RSTQT,PR_MOSQT,PR_YOSQT,PR_TBGQT,PR_MBGQT,PR_YBGQT,PR_PINNO,";
		LM_TMPSTR  += "PR_PINRT,PR_TRNFL,PR_STSFL,PR_LUSBY,PR_PRDTP,PR_GRDDS,PR_PINDT,PR_LUPDT)";
		LM_TMPSTR  += " values("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_PRDCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_PRDDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_UOMCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_CSTQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_USTQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_RSTQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_MOSQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_YOSQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_TBGQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_MBGQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_YBGQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_PINNO"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PR_PINRT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_LUSBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_PRDTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PR_GRDDS"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PR_PINDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PR_LUPDT"))+")";
		return chkUPDCT(LM_STLSQL);
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insPRMST","");
		return false;
	}
}
private boolean insPTMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into CO_PTMST(PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_SHRNM,PT_GRPCD,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_PINCD,PT_CTYNM,PT_STACD,PT_CNTCD,";
		LM_TMPSTR  += "PT_CONNM,PT_TEL01,PT_TEL02,PT_EMLRF,PT_FAXNO,PT_INFFL,PT_STXNO,PT_CLSCD,PT_SCRCD,PT_CSTNO,PT_ECCNO,PT_ITPNO,PT_EXCNO,PT_RNGDS,PT_DIVDS,";
		LM_TMPSTR  += "PT_CLLDS,PT_ZONCD,PT_SALVL,PT_TRNCD,PT_DSTCD,PT_TSTFL,PT_YOPCR,PT_YOPDB,PT_YTDCR,PT_YTDDB,PT_STSFL,PT_TRNFL,PT_LUSBY,PT_MOBNO,";
		LM_TMPSTR  += "PT_COWEB,PT_EMLPR,PT_EMLMR,PT_EMLAC,PT_STXDT,PT_CSTDT,PT_LUPDT)";
		LM_TMPSTR  += " values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_PRTTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_PRTCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_PRTNM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_SHRNM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_GRPCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_ADR01"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_ADR02"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_ADR03"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_ADR04"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_PINCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_CTYNM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_STACD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_CNTCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_CONNM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_TEL01"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_TEL02"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_EMLRF"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_FAXNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_INFFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_STXNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_CLSCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_SCRCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_CSTNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_ECCNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_ITPNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_EXCNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_RNGDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_DIVDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_CLLDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_ZONCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PT_SALVL"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_TRNCD"),"0") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_DSTCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_TSTFL"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PT_YOPCR"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PT_YOPDB"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PT_YTDCR"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PT_YTDDB"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_LUSBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_MOBNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_COWEB"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_EMLPR"),"") + "',";
		LM_STLSQL+= "'"+nvlSTRVL(LP_RSLSET.getString("PT_EMLMR"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PT_EMLAC"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PT_STXDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PT_CSTDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PT_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"crtPTMST","");
		return false;
	}
}
private boolean chkRSMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_QCATP,L_TSTTP,L_TSTNO,L_UPDSTR;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_QCATP =  nvlSTRVL(LM_RSLSRC.getString("RS_QCATP"),"");
			L_TSTTP =  nvlSTRVL(LM_RSLSRC.getString("RS_TSTTP"),"");
			L_TSTNO =  nvlSTRVL(LM_RSLSRC.getString("RS_TSTNO"),"");
			setMSG("QC_RSMST : "+L_QCATP +" / "+L_TSTTP +" / "+L_TSTNO,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update qc_rsmst set rs_trnfl ='1'");
			LM_WHCND.append(" WHERE RS_QCATP ='"+L_QCATP +"' AND RS_TSTTP ='"+L_TSTTP +"' AND RS_TSTNO ='"+L_TSTNO+"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insRSMST(LM_RSLSRC,"SP"))
			{
				L_UPDSTR = "UPDATE QC_RSMST SET "; 	
				L_UPDSTR += "RS_TSTDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("RS_TSTDT")))+",";
				L_UPDSTR += "RS_10101 = "+nvlSTRVL(LM_RSLSRC.getString("RS_10101"),"0")+",";
				L_UPDSTR += "RS_10102 = "+nvlSTRVL(LM_RSLSRC.getString("RS_10102"),"0")+",";
				L_UPDSTR += "RS_10103 = "+nvlSTRVL(LM_RSLSRC.getString("RS_10103"),"0")+",";
				L_UPDSTR += "RS_10104 = "+nvlSTRVL(LM_RSLSRC.getString("RS_10104"),"0")+",";
				L_UPDSTR += "RS_11101 = "+nvlSTRVL(LM_RSLSRC.getString("RS_11101"),"0")+",";
				L_UPDSTR += "RS_11102 = "+nvlSTRVL(LM_RSLSRC.getString("RS_11102"),"0")+",";
				L_UPDSTR += "RS_11103 = "+nvlSTRVL(LM_RSLSRC.getString("RS_11103"),"0")+",";
				L_UPDSTR += "RS_12101 = "+nvlSTRVL(LM_RSLSRC.getString("RS_12101"),"0")+",";
				L_UPDSTR += "RS_12102 = "+nvlSTRVL(LM_RSLSRC.getString("RS_12102"),"0")+",";
				L_UPDSTR += "RS_12103 = "+nvlSTRVL(LM_RSLSRC.getString("RS_12103"),"0")+",";
				L_UPDSTR += "RS_12104 = "+nvlSTRVL(LM_RSLSRC.getString("RS_12104"),"0")+",";
				L_UPDSTR += "RS_TSTBY = '"+nvlSTRVL(LM_RSLSRC.getString("RS_TSTBY"),"")+"',";
				L_UPDSTR += "RS_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("RS_STSFL"),"")+"',";
				L_UPDSTR += "RS_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("RS_TRNFL"),"")+"',";
				L_UPDSTR += "RS_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("RS_LUSBY"),"")+"',";
				L_UPDSTR += "RS_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("RS_LUPDT"));
                                L_UPDSTR += " "+LM_WHCND.toString().trim();
				if(chkUPDCT(L_UPDSTR))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for QC_RSMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkRSMST "+L_EX.toString());
		showEXMSG(L_EX,"chkRSMST","");
		return false;
	}
	return true;
}
private boolean insRSMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into QC_RSMST(RS_QCATP,RS_TSTTP,RS_TSTNO,";
		LM_TMPSTR  += "RS_TSTDT,RS_10101,RS_10102,RS_10103,RS_10104,RS_11101,RS_11102,RS_11103,RS_12101,RS_12102,RS_12103,RS_12104,RS_TSTBY,RS_STSFL,RS_TRNFL,RS_LUSBY,RS_LUPDT) values (";
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_QCATP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_TSTTP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_TSTNO"),"")+"',";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("RS_TSTDT")))+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_10101"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_10102"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_10103"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_10104"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_11101"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_11102"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_11103"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_12101"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_12102"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_12103"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_12104"),"0")+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_TSTBY"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_STSFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_TRNFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_LUSBY"),"")+"',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RS_LUPDT"))+")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE)
	{
		showEXMSG(L_SE,"insRSMST","");
		System.out.println(L_SE.getErrorCode());
		return false;
	}
}
private boolean insRMMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR  = "Insert Into QC_RMMST(RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)values(";
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_QCATP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_TSTTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_TSTNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_REMDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RM_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insRMMST","");
		return false;
	}
}
private boolean chkRMMST(String LP_STRSQL,String LP_ORGCD)
{
	String L_QCATP,L_TSTTP,L_TSTNO,L_UPDSTR;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_QCATP =  nvlSTRVL(LM_RSLSRC.getString("RM_QCATP"),"");
			L_TSTTP =  nvlSTRVL(LM_RSLSRC.getString("RM_TSTTP"),"");
			L_TSTNO =  nvlSTRVL(LM_RSLSRC.getString("RM_TSTNO"),"");
			setMSG("QC_RMMST : "+L_QCATP +" / "+L_TSTTP +" / "+L_TSTNO,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update qc_rmmst set rm_trnfl ='1'");
			LM_WHCND.append("WHERE RM_QCATP ='"+L_QCATP +"' AND RM_TSTTP ='"+L_TSTTP +"' AND RM_TSTNO ='"+L_TSTNO+"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insRMMST(LM_RSLSRC,"SP"))
			{
				L_UPDSTR = "UPDATE QC_RMMST SET "; 	
				L_UPDSTR += "RM_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("RM_REMDS"),"")+"',";
				L_UPDSTR += "RM_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_STSFL"),"")+"',";
				L_UPDSTR += "RM_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_TRNFL"),"")+"',";
				L_UPDSTR += "RM_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("RM_LUSBY"),"")+"',";
				L_UPDSTR += "RM_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("RM_LUPDT"));
                                L_UPDSTR += " "+LM_WHCND.toString().trim();
				if(chkUPDCT(L_UPDSTR))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for QC_RMMST ",'N');
		LM_RSLSRC.close();
	}
	catch(Exception L_EX)
	{
		
		showEXMSG(L_EX,"chkRMMST","");
		return false;
	}
	return true;
}
private boolean chkLTMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="",L_UPDSTR;
	String L_PRDTP,L_LOTNO,L_RCLNO;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_PRDTP =  nvlSTRVL(LM_RSLSRC.getString("LT_PRDTP"),"");
			L_LOTNO =  nvlSTRVL(LM_RSLSRC.getString("LT_LOTNO"),"");
			L_RCLNO =  nvlSTRVL(LM_RSLSRC.getString("LT_RCLNO"),"");
			setMSG("PR_LTMST : "+L_PRDTP +" / "+L_LOTNO +" / "+L_RCLNO,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update pr_ltmst set lt_trnfl ='1'");
			LM_WHCND.append(" WHERE LT_PRDTP ='"+L_PRDTP +"' AND LT_LOTNO ='"+L_LOTNO +"' AND LT_RCLNO ='"+L_RCLNO+"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insLTMST(LM_RSLSRC,"SP"))
			{
				L_UPDSTR = "UPDATE PR_LTMST SET ";
				L_UPDSTR += "LT_PRDCD = '"+nvlSTRVL(LM_RSLSRC.getString("LT_PRDCD"),"")+"',";
				L_UPDSTR += "LT_RUNNO = '"+nvlSTRVL(LM_RSLSRC.getString("LT_RUNNO"),"")+"',";
				L_UPDSTR += "LT_TPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("LT_TPRCD"),"")+"',";
				L_UPDSTR += "LT_IPRDS = '"+nvlSTRVL(LM_RSLSRC.getString("LT_IPRDS"),"")+"',";
				L_UPDSTR += "LT_LINNO = '"+nvlSTRVL(LM_RSLSRC.getString("LT_LINNO"),"")+"',";
				L_UPDSTR += "LT_CYLNO = '"+nvlSTRVL(LM_RSLSRC.getString("LT_CYLNO"),"")+"',";
				L_UPDSTR += "LT_PSTDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_PSTDT"))) + ",";
				L_UPDSTR += "LT_PENDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_PENDT"))) + ",";
				L_UPDSTR += "LT_BSTDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_BSTDT"))) + ",";
				L_UPDSTR += "LT_BENDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_BENDT"))) + ",";
				L_UPDSTR += "LT_PRDQT = "+nvlSTRVL(LM_RSLSRC.getString("LT_PRDQT"),"0")+",";
				L_UPDSTR += "LT_BAGQT = "+nvlSTRVL(LM_RSLSRC.getString("LT_BAGQT"),"0")+",";
				L_UPDSTR += "LT_DSPQT = "+nvlSTRVL(LM_RSLSRC.getString("LT_DSPQT"),"0")+",";
				L_UPDSTR += "LT_CLSFL = '"+nvlSTRVL(LM_RSLSRC.getString("LT_CLSFL"),"")+"',";
				L_UPDSTR += "LT_PPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("LT_PPRCD"),"")+"',";
				L_UPDSTR += "LT_PCLBY = '"+nvlSTRVL(LM_RSLSRC.getString("LT_PCLBY"),"")+"',";
				L_UPDSTR += "LT_PCLTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_PCLTM"))) + ",";
				L_UPDSTR += "LT_CPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("LT_CPRCD"),"")+"',";
				L_UPDSTR += "LT_CLSBY = '"+nvlSTRVL(LM_RSLSRC.getString("LT_CLSBY"),"")+"',";
				L_UPDSTR += "LT_CLSTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_CLSTM"))) + ",";
				L_UPDSTR += "LT_CLSRF = '"+nvlSTRVL(LM_RSLSRC.getString("LT_CLSRF"),"")+"',";
				L_UPDSTR += "LT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("LT_STSFL"),"")+"',";
				L_UPDSTR += "LT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("LT_TRNFL"),"")+"',";
				L_UPDSTR += "LT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("LT_LUSBY"),"")+"',";
				L_UPDSTR += "LT_LUPDT ="+ setDATE("MDY",LM_RSLSRC.getDate("LT_LUPDT")) + ",";
				L_UPDSTR += "LT_ADDTM = "+ setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_ADDTM"))) + ",";
				L_UPDSTR += "LT_ADDBY = '"+nvlSTRVL(LM_RSLSRC.getString("LT_ADDBY"),"")+"',";
				L_UPDSTR += "LT_ENDTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LT_ENDTM"))) + ",";
				L_UPDSTR += "LT_ENDBY = '"+nvlSTRVL(LM_RSLSRC.getString("LT_ENDBY"),"")+"',";
				L_UPDSTR += "LT_RETQT = "+nvlSTRVL(LM_RSLSRC.getString("LT_RETQT"),"0")+",";
				L_UPDSTR += "LT_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("LT_REMDS"),"")+"'";
                                L_UPDSTR += " "+LM_WHCND.toString();
				if(chkUPDCT(L_UPDSTR))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for PR_LTMST ",'N');
		LM_RSLSRC.close();
	}
	catch(SQLException L_EX)
	{
		System.out.println(L_EX.getErrorCode());
		showEXMSG(L_EX,"chkLTMST","");
		return false;
	}
	return true;
}
private boolean insLTMST(ResultSet LP_RSLSET,String  LP_SYSCD)
{

	try
	{
		LM_TMPSTR = "Insert Into PR_LTMST(LT_PRDTP,LT_LOTNO,LT_PRDCD,LT_RUNNO,LT_TPRCD,LT_IPRDS,LT_LINNO,LT_CYLNO,LT_PSTDT,LT_PENDT,";
		LM_TMPSTR += "LT_BSTDT,LT_BENDT,LT_PRDQT,LT_BAGQT,LT_DSPQT,LT_CLSFL,LT_PPRCD,LT_PCLBY,LT_PCLTM,LT_CPRCD,LT_CLSBY,LT_CLSTM,LT_CLSRF,LT_STSFL,LT_TRNFL,";
		LM_TMPSTR +="LT_LUSBY,LT_LUPDT,LT_ADDTM,LT_ADDBY,LT_ENDTM,LT_ENDBY,LT_RETQT,LT_REMDS,LT_RCLNO)";
		LM_TMPSTR +="values("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_PRDTP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_LOTNO"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_PRDCD"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_RUNNO"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_TPRCD"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_IPRDS"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_LINNO"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_CYLNO"),"")+"',";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_PSTDT"))) + ",";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_PENDT"))) + ",";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_BSTDT"))) + ",";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_BENDT"))) + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LT_PRDQT"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LT_BAGQT"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LT_DSPQT"),"0")+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_CLSFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_PPRCD"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_PCLBY"),"")+"',";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_PCLTM"))) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_CPRCD"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_CLSBY"),"")+"',";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_CLSTM"))) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_CLSRF"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_STSFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_TRNFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_LUSBY"),"")+"',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("LT_LUPDT")) + ",";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_ADDTM"))) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_ADDBY"),"")+"',";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LT_ENDTM"))) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_ENDBY"),"")+"',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LT_RETQT"),"0")+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_REMDS"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LT_RCLNO"),"")+"')";
		return chkUPDCT(LM_STLSQL);
	}
	catch(SQLException L_SE)
	{
		showEXMSG(L_SE,"crtLTMST","");
		return false;
	}
}
private boolean insPSMST(ResultSet LP_RSLSET,String  LP_SYSCD)
{
	try
	{
		LM_TMPSTR = "Insert Into QC_PSMST(PS_QCATP,PS_TSTTP,PS_TSTNO,PS_TSTDT,PS_MORTP,PS_PRDTP,PS_LOTNO,PS_LINNO,";
		LM_TMPSTR +="PS_MFIVL,PS_DSPVL,PS_VICVL,PS_TS_VL,PS_EL_VL,PS_RSMVL,PS_A__VL,PS_B__VL,PS_Y1_VL,PS_MW_VL,PS_MN_VL,";
		LM_TMPSTR +="PS_MZ_VL,PS_PD_VL,PS_HDTVL,PS_FS_VL,PS_FM_VL,PS_MA_VL,PS_IZOVL,PS_RP1VL,PS_RP2vl,PS_RP3VL,PS_RP4VL,PS_WI_VL,";
		LM_TMPSTR +="PS_RBRVL,PS_GL1Vl,PS_GL2VL,PS_GL3VL,PS_GELVL,PS_SWLVL,PS_ESCVL,PS_TSTBY,PS_RCLNO,PS_STSFL,PS_TRNFL,PS_LUSBY,PS_ADDBY,PS_ADDTM,PS_LUPDT,PS_MOIVL,";
		LM_TMPSTR +="PS_ESNVL,PS_ESBVL,PS_ESOVL,PS_ESIVL,PS_FR1VL,PS_FR2Vl,PS_FR3VL,PS_L__VL,PS_dL_VL,PS_da_VL,PS_db_VL,PS_dE_VL)";
		LM_TMPSTR +="values("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_QCATP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_TSTTP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_TSTNO"),"")+"',";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("PS_TSTDT"))) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_MORTP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_PRDTP"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_LOTNO"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_LINNO"),"")+"',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_MFIVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_DSPVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_VICVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_TS_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_EL_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_RSMVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_A__VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_B__VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_Y1_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_MW_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_MN_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_MZ_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_PD_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_HDTVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_FS_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_FM_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_MA_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_IZOVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_RP1VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_RP2VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_RP3VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_RP4VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_WI_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_RBRVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_GL1VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_GL2VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_GL3VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_GELVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_SWLVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_ESCVL"),"0")+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_tstby"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_RCLNO"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_STSFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_TRNFL"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_LUSBY"),"")+"',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PS_ADDBY"),"")+"',";
		LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("PS_ADDTM"))) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PS_LUPDT")) + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_MOIVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_ESNVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_ESBVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_ESOVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_ESIVL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_FR1VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_FR2VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_FR3VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_L__VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_dL_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_da_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_db_VL"),"0")+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PS_dE_VL"),"0")+")";
		return chkUPDCT(LM_STLSQL);
	}
	catch(SQLException L_SE)
	{
		showEXMSG(L_SE,"crtPSMST","");
		return false;
	}
		
}
private boolean chkPSMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="",L_UPDSTR;
	String L_QCATP,L_TSTTP,L_LOTNO,L_RCLNO,L_TSTNO,L_TSTDT;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_QCATP =  nvlSTRVL(LM_RSLSRC.getString("PS_QCATP"),"");
			L_TSTTP =  nvlSTRVL(LM_RSLSRC.getString("PS_TSTTP"),"");
			L_LOTNO =  nvlSTRVL(LM_RSLSRC.getString("PS_LOTNO"),"");
			L_RCLNO =  nvlSTRVL(LM_RSLSRC.getString("PS_RCLNO"),"");
			L_TSTNO =  nvlSTRVL(LM_RSLSRC.getString("PS_TSTNO"),"");
			L_TSTDT =  nvlSTRVL(LM_RSLSRC.getString("PS_TSTDT"),"");
			setMSG("QC_PSMST : "+L_TSTTP +" / "+L_LOTNO +" / "+L_RCLNO+" / "+L_TSTNO +" / "+L_TSTDT,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update qc_psmst set ps_trnfl ='1'");
			LM_WHCND.append(" WHERE PS_QCATP ='"+L_QCATP +"' AND PS_TSTTP ='"+L_TSTTP +"' AND PS_LOTNO ='"+L_LOTNO+"' AND PS_RCLNO ='"+L_RCLNO+"' AND PS_TSTNO ='"+L_TSTNO +"' AND PS_TSTDT = "+setDTMDB2(setDTMFMT(L_TSTDT)));
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPSMST(LM_RSLSRC,"SP"))
			{
				L_UPDSTR = "UPDATE QC_PSMST SET ";
				L_UPDSTR += "PS_MORTP = '"+nvlSTRVL(LM_RSLSRC.getString("PS_MORTP"),"")+"',";
				L_UPDSTR += "PS_PRDTP = '"+nvlSTRVL(LM_RSLSRC.getString("PS_PRDTP"),"")+"',";
				L_UPDSTR += "PS_LOTNO = '"+nvlSTRVL(LM_RSLSRC.getString("PS_LOTNO"),"")+"',";
				L_UPDSTR += "PS_LINNO = '"+nvlSTRVL(LM_RSLSRC.getString("PS_LINNO"),"")+"',";
				L_UPDSTR += "PS_MFIVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_MFIVL"),"0")+",";
				L_UPDSTR += "PS_DSPVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_DSPVL"),"0")+",";
				L_UPDSTR += "PS_VICVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_VICVL"),"0")+",";
				L_UPDSTR += "PS_TS_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_TS_VL"),"0")+",";
				L_UPDSTR += "PS_EL_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_EL_VL"),"0")+",";
				L_UPDSTR += "PS_RSMVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_RSMVL"),"0")+",";
				L_UPDSTR += "PS_A__VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_A__VL"),"0")+",";
				L_UPDSTR += "PS_B__VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_B__VL"),"0")+",";
				L_UPDSTR += "PS_Y1_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_Y1_VL"),"0")+",";
				L_UPDSTR += "PS_MW_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_MW_VL"),"0")+",";
				L_UPDSTR += "PS_MN_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_MN_VL"),"0")+",";
				L_UPDSTR += "PS_MZ_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_MZ_VL"),"0")+",";
				L_UPDSTR += "PS_PD_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_PD_VL"),"0")+",";
				L_UPDSTR += "PS_HDTVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_HDTVL"),"0")+",";
				L_UPDSTR += "PS_FS_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_FS_VL"),"0")+",";
				L_UPDSTR += "PS_FM_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_FM_VL"),"0")+",";
				L_UPDSTR += "PS_MA_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_MA_VL"),"0")+",";
				L_UPDSTR += "PS_IZOVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_IZOVL"),"0")+",";
				L_UPDSTR += "PS_RP1VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_RP1VL"),"0")+",";
				L_UPDSTR += "PS_RP2VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_RP2VL"),"0")+",";
				L_UPDSTR += "PS_RP3VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_RP3VL"),"0")+",";
				L_UPDSTR += "PS_RP4VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_RP4VL"),"0")+",";
				L_UPDSTR += "PS_WI_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_WI_VL"),"0")+",";
				L_UPDSTR += "PS_RBRVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_RBRVL"),"0")+",";
				L_UPDSTR += "PS_GL1VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_GL1VL"),"0")+",";
				L_UPDSTR += "PS_GL2VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_GL2VL"),"0")+",";
				L_UPDSTR += "PS_GL3VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_GL3VL"),"0")+",";
				L_UPDSTR += "PS_GELVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_GELVL"),"0")+",";
				L_UPDSTR += "PS_SWLVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_SWLVL"),"0")+",";
				L_UPDSTR += "PS_ESCVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_ESCVL"),"0")+",";
				L_UPDSTR += "PS_TSTBY = '"+nvlSTRVL(LM_RSLSRC.getString("PS_TSTBY"),"")+"',";
				L_UPDSTR += "PS_RCLNO = '"+nvlSTRVL(LM_RSLSRC.getString("PS_RCLNO"),"")+"',";
				L_UPDSTR += "PS_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("PS_STSFL"),"")+"',";
				L_UPDSTR += "PS_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("PS_TRNFL"),"")+"',";
				L_UPDSTR += "PS_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("PS_LUSBY"),"")+"',";
				L_UPDSTR += "PS_ADDBY = '"+nvlSTRVL(LM_RSLSRC.getString("PS_ADDBY"),"")+"',";
				L_UPDSTR += "PS_ADDTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("PS_ADDTM"))) + ",";
				L_UPDSTR += "PS_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("PS_LUPDT")) + ",";
				L_UPDSTR += "PS_MOIVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_MOIVL"),"0")+",";
				L_UPDSTR += "PS_ESNVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_ESNVL"),"0")+",";
				L_UPDSTR += "PS_ESBVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_ESBVL"),"0")+",";
				L_UPDSTR += "PS_ESOVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_ESOVL"),"0")+",";
				L_UPDSTR += "PS_ESIVL = "+nvlSTRVL(LM_RSLSRC.getString("PS_ESIVL"),"0")+",";
				L_UPDSTR += "PS_FR1VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_FR1VL"),"0")+",";
				L_UPDSTR += "PS_FR2VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_FR2VL"),"0")+",";
				L_UPDSTR += "PS_FR3VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_FR3VL"),"0")+",";
				L_UPDSTR += "PS_L__VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_L__VL"),"0")+",";
				L_UPDSTR += "PS_dL_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_dL_VL"),"0")+",";
				L_UPDSTR += "PS_da_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_da_VL"),"0")+",";
				L_UPDSTR += "PS_db_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_db_VL"),"0")+",";
				L_UPDSTR += "PS_dE_VL = "+nvlSTRVL(LM_RSLSRC.getString("PS_dE_VL"),"0");
                                L_UPDSTR += " "+LM_WHCND.toString();
				if(chkUPDCT(L_UPDSTR))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for QC_PSMST ",'N');
		LM_RSLSRC.close();
	}
	catch(SQLException L_EX)
	{
		System.out.println(L_EX.getErrorCode());
		showEXMSG(L_EX,"chkPSMST","");
		return false;
	}
	return true;
}
private boolean chkSMTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_QCATP ="",L_TSTTP ="",L_TSTNO ="",L_MORTP ="",L_TSTTM ="",L_TSTRF ="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_QCATP =  nvlSTRVL(LM_RSLSRC.getString("SMT_QCATP"),"");
			L_TSTTP =  nvlSTRVL(LM_RSLSRC.getString("SMT_TSTTP"),"");
			L_TSTNO =  nvlSTRVL(LM_RSLSRC.getString("SMT_TSTNO"),"");
			L_MORTP =  nvlSTRVL(LM_RSLSRC.getString("SMT_MORTP"),"");
			L_TSTTM =  nvlSTRVL(LM_RSLSRC.getString("SMT_TSTDT"),"");
			L_TSTRF =  nvlSTRVL(LM_RSLSRC.getString("SMT_TSTRF"),"");
			setMSG("Updating SMTRN "+LM_TSTTP+" / "+LM_TSTNO +" / "+LM_TSTRF,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE SMT_QCATP ='"+L_QCATP +"'");
			LM_WHCND.append(" AND SMT_TSTTP ='"+L_TSTTP +"'");
			LM_WHCND.append(" AND SMT_TSTNO ='"+L_TSTNO +"'");
			LM_WHCND.append(" AND SMT_MORTP ='"+L_MORTP +"'");
			LM_WHCND.append(" AND SMT_TSTDT ="+setDTMDB2(setDTMFMT(L_TSTTM)));
			LM_WHCND.append(" AND SMT_TSTRF ='"+L_TSTRF +"'");
			LM_UPDQRY.append("update qc_smtrn set smt_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insSMTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE QC_SMTRN SET ";
					L_UPDSTR += "SMT_COLVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_COLVL"),"0")+",";
					L_UPDSTR += "SMT_RI_VL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_RI_VL"),"0")+",";
					L_UPDSTR += "SMT_TBCVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_TBCVL"),"0")+",";
					L_UPDSTR += "SMT_POLVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_POLVL"),"0")+",";
					L_UPDSTR += "SMT_BZ_VL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_BZ_VL"),"0")+",";
					L_UPDSTR += "SMT_TOLVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_TOLVL"),"0")+",";
					L_UPDSTR += "SMT_EBSVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_EBSVL"),"0")+",";
					L_UPDSTR += "SMT_XYSVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_XYSVL"),"0")+",";
					L_UPDSTR += "SMT_CUMVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_CUMVL"),"0")+",";
					L_UPDSTR += "SMT_STYVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_STYVL"),"0")+",";
					L_UPDSTR += "SMT_BHDVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_BHDVL"),"0")+",";
					L_UPDSTR += "SMT_ULBVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_ULBVL"),"0")+",";
					L_UPDSTR += "SMT_UHBVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_UHBVL"),"0")+",";
					L_UPDSTR += "SMT_MOIVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_MOIVL"),"0")+",";
					L_UPDSTR += "SMT_TMPVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_TMPVL"),"0")+",";
					L_UPDSTR += "SMT_SPGVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_SPGVL"),"0")+",";
					L_UPDSTR += "SMT_EB_VL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_EB_VL"),"0")+",";
					L_UPDSTR += "SMT_XYLVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_XYLVL"),"0")+",";
					L_UPDSTR += "SMT_PMRVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_PMRVL"),"0")+",";
					L_UPDSTR += "SMT_QLTVL = "+nvlSTRVL(LM_RSLSRC.getString("SMT_QLTVL"),"0")+",";
					L_UPDSTR += "SMT_TNKCT = "+nvlSTRVL(LM_RSLSRC.getString("SMT_TNKCT"),"0")+",";
					L_UPDSTR += "SMT_TKLCD = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_TKLCD"),"")+"',";
					L_UPDSTR += "SMT_PRDCD = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_PRDCD"),"")+"',";
					L_UPDSTR += "SMT_QLTFL = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_QLTFL"),"")+"',";
					L_UPDSTR += "SMT_ADDBY = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_ADDBY"),"")+"',";
					L_UPDSTR += "SMT_ADDTM ="+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("SMT_ADDTM"))) + ",";
					L_UPDSTR += "SMT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_STSFL"),"")+"',";
					L_UPDSTR += "SMT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_TRNFL"),"")+"',";
					L_UPDSTR += "SMT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_LUSBY"),"")+"',";
					L_UPDSTR += "SMT_LUPDT ="+setDATE("MDY",LM_RSLSRC.getDate("SMT_LUPDT")) + ",";
					L_UPDSTR += "SMT_TSTBY = '"+nvlSTRVL(LM_RSLSRC.getString("SMT_TSTBY"),"")+"'";
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						setMSG("Updated the record for "+ L_TSTTP+" / "+L_TSTNO +" / "+L_TSTRF,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
						setMSG("Could not update the record for "+ L_TSTTP+" / "+L_TSTNO +" / "+L_TSTRF,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updSMTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for QC_SMTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkSMTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkSMTRN","");
		return false;
	}
	return true;
}
private boolean chkWTTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_QCATP ="",L_TSTTP ="",L_TSTNO ="",L_TSTTM ="",L_WTRTP ="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_QCATP =  nvlSTRVL(LM_RSLSRC.getString("WTT_QCATP"),"");
			L_TSTTP =  nvlSTRVL(LM_RSLSRC.getString("WTT_TSTTP"),"");
			L_TSTNO =  nvlSTRVL(LM_RSLSRC.getString("WTT_TSTNO"),"");
			L_TSTTM =  nvlSTRVL(LM_RSLSRC.getString("WTT_TSTDT"),"");
			L_WTRTP =  nvlSTRVL(LM_RSLSRC.getString("WTT_WTRTP"),"");
			
			setMSG("QC_WTTRN : "+L_TSTTP+" / "+L_TSTNO +" / "+L_TSTTM+" / "+L_WTRTP,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE WTT_QCATP ='"+L_QCATP +"'");
			LM_WHCND.append(" AND WTT_TSTTP ='"+L_TSTTP +"'");
			LM_WHCND.append(" AND WTT_TSTNO ='"+L_TSTNO +"'");
			LM_WHCND.append(" AND WTT_TSTDT ="+setDTMDB2(setDTMFMT(L_TSTTM)));
			LM_WHCND.append(" AND WTT_WTRTP ='"+L_WTRTP +"'");
			LM_UPDQRY.append("update qc_wttrn set wtt_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insWTTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE QC_WTTRN SET ";
					L_UPDSTR += "WTT_PH_VL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_PH_VL"),"0")+",";
					L_UPDSTR += "WTT_CONVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_CONVL"),"0")+",";
					L_UPDSTR += "WTT_TBDVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_TBDVL"),"0")+",";
					L_UPDSTR += "WTT_HARVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_HARVL"),"0")+",";
					L_UPDSTR += "WTT_PALVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_PALVL"),"0")+",";
					L_UPDSTR += "WTT_MALVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_MALVL"),"0")+",";
					L_UPDSTR += "WTT_CHLVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_CHLVL"),"0")+",";
					L_UPDSTR += "WTT_IROVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_IROVL"),"0")+",";
					L_UPDSTR += "WTT_PO4VL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_PO4VL"),"0")+",";
					L_UPDSTR += "WTT_SILVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_SILVL"),"0")+",";
					L_UPDSTR += "WTT_SPTVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_SPTVL"),"0")+",";
					L_UPDSTR += "WTT_TDSVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_TDSVL"),"0")+",";
					L_UPDSTR += "WTT_TSSVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_TSSVL"),"0")+",";
					L_UPDSTR += "WTT_DO_VL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_DO_VL"),"0")+",";
					L_UPDSTR += "WTT_BODVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_BODVL"),"0")+",";
					L_UPDSTR += "WTT_CODVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_CODVL"),"0")+",";
					L_UPDSTR += "WTT_ONGVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_ONGVL"),"0")+",";
					L_UPDSTR += "WTT_BASVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_BASVL"),"0")+",";
					L_UPDSTR += "WTT_SPDVL = "+nvlSTRVL(LM_RSLSRC.getString("WTT_SPDVL"),"0")+",";
					L_UPDSTR += "WTT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("WTT_STSFL"),"")+"',";
					L_UPDSTR += "WTT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("WTT_TRNFL"),"")+"',";
					L_UPDSTR += "WTT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("WTT_LUSBY"),"")+"',";
					L_UPDSTR += "WTT_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("WTT_LUPDT")) + ",";
					L_UPDSTR += "WTT_TSTBY = '"+nvlSTRVL(LM_RSLSRC.getString("WTT_TSTBY"),"")+"'";
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updWTTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for QC_WTTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkSMTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkSMTRN","");
		return false;
	}
	return true;
}
	public  void showEXMSG(Exception LP_EX, String LP_MTHNM, String LP_SETFL)
	{
		System.out.println(LP_MTHNM+" : "+LP_EX.toString());
		setMSG(LP_MTHNM+" : "+LP_EX.toString(),'E');
	}
	public void setMSG(String LP_STRMSG,char LP_MSGTP){
     	if(LP_MSGTP == 'N')
			txtMSG.setForeground(Color.blue);
		else if(LP_MSGTP == 'E')
			txtMSG.setForeground(Color.red);
		txtMSG.setText(LP_STRMSG);
		txtMSG.paintImmediately(txtMSG.getVisibleRect());
	}
	public String nvlSTRVL(String LP_VARVL, String LP_DEFVL)
	{
		try
		{
            if (LP_VARVL != null) 
				LP_VARVL = LP_VARVL.trim();
		    else 
				return LP_DEFVL;
	    }
		catch (Exception L_EX)
		{
			showEXMSG(L_EX,"nvl","");
		}
		return LP_VARVL;
	}

private Connection setCONDTB(String LP_SYSLC,String LP_DTBLB)
{
	String L_DTBUS="";
	String L_DTBPW="";
	Connection L_CONDTB;
	try
	{
		String L_STRURL = "", L_STRDRV = "";
        if(LP_SYSLC.equals(LM_SVRWR))
        {
			L_DTBUS = "FIMS";
            L_DTBPW = "FIMS";
            L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
            //L_STRURL = "jdbc:as400://SPLWS02/";
			L_STRURL = "jdbc:as400://SPLWS01/";
            Class.forName(L_STRDRV);
        }
        else if(LP_SYSLC.equals(LM_SVRHO))
         {
			int port = 50000;
			if(LM_REPOPT.equals("ACT"))
			{
				L_DTBUS = "SPLDATA";
				L_DTBPW = "SPLDATA";
			}
			else if(LM_REPOPT.equals("HST"))
			{
				L_DTBUS = "SPLHIST";
				L_DTBPW = "SPLHIST";
			}
			else if(LM_REPOPT.equals("TST"))
			{
				System.out.println("Test option not available at H.O");
			}
			L_STRURL = "jdbc:db2://" + "splhos01" + ":" + 50000 + "/" ;
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
         }
         L_STRURL = L_STRURL + LP_DTBLB;
         L_CONDTB = DriverManager.getConnection(L_STRURL,L_DTBUS,L_DTBPW);
		 if(L_CONDTB == null)
			return null;
         L_CONDTB.setAutoCommit(false);
         SQLWarning L_STRWRN = L_CONDTB.getWarnings();
		 if ( L_STRWRN != null )
			setMSG("Warning in setCONDTB : "+L_STRWRN,'N');
         return L_CONDTB;
	}
    catch(java.lang.Exception L_EX)
    {
		showEXMSG(L_EX,"setCONDTB","");
	}
		return null;
  }
private void setSTMDTB()
 {
	 try
	 {
		if(conSRCDB !=null)
		{
			 stmSRCQ1 = conSRCDB.createStatement();
			 stmSRCQ2 = conSRCDB.createStatement();
			 stmSRCUP = conSRCDB.createStatement();
			 
		}
		if(conDESDB !=null)
		{
			stmDESUP = conDESDB.createStatement();
		}
	 }
	 catch(SQLException L_SE)
	 {
		showEXMSG(L_SE,"setSTMDTB","");
	 }
 }
private ResultSet exeSQLQRY(String LP_SQLVAL,String LP_CONTP)
{
	try
	{
		if (LP_CONTP.equals(LM_SRCLC) && conSRCDB != null)
			LM_RSLSET = stmSRCQ1.executeQuery(LP_SQLVAL);
	//	else if(LP_CONTP.equals(LM_DESLC) && conDESDB != null)
	//		LM_RSLSET = LM_STMDS1.executeQuery(LP_SQLVAL);
	}
	catch(Exception L_SE)
	{
		showEXMSG(L_SE,"exeSQLQRY : "+LP_SQLVAL ,"");
	}
	return LM_RSLSET;
}
private ResultSet exeSQLQRY1(String LP_SQLVAL,String LP_CONTP)
{
	try
	{
		if (LP_CONTP.equals(LM_SRCLC) && conSRCDB != null)
			LM_RSLSET1 = stmSRCQ2.executeQuery(LP_SQLVAL);
	//	else if(LP_CONTP.equals(LM_DESLC) && conDESDB != null)
	//		LM_RSLSET = LM_STMDS2.executeQuery(LP_SQLVAL);
	}
	catch(Exception L_SE)
	{
		showEXMSG(L_SE,"exeSQLQRY1","");
	}
	return LM_RSLSET1;
}
private  boolean exeSQLUPD(String LP_SQLVAL,String LP_CONTP)
{
	int L_UPDCTR = 0;
	try
	{
		if (LP_CONTP.equals(LM_SRCLC) && conSRCDB != null)
			L_UPDCTR = stmSRCUP.executeUpdate(LP_SQLVAL);
		if(L_UPDCTR < 1)
		{
			setMSG("Error in exeSQLUPD",'E');
			return false;
		}
		else
			return true;
	}
	catch(Exception L_SE)
	{
		System.out.println("error from exeSQLUPD "+L_SE.getMessage()+LP_SQLVAL);
		//showEXMSG(L_SE,"exeSQLUPD","");
		return false;
	}
}
private String setDATFMT(String LP_FMTTP,String LP_DATSTR){
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
private String setDTMDB2(String LP_DTMSTR){
		String L_RTNSTR = "";
		try{
			if(LP_DTMSTR != null && LP_DTMSTR.length() > 0){
				if(LP_DTMSTR.trim().length() == 16){
					if((LP_DTMSTR.substring(11,13).equals("24")) && (LP_DTMSTR.substring(14,16).equals("00"))){
						LP_DTMSTR = LP_DTMSTR.substring(0,11) + "23" + ":" + "59";
					}else if(LP_DTMSTR.substring(11,13).equals("00")){
						if(LP_DTMSTR.substring(14,16).equals("00"))// minutes
							LP_DTMSTR = LP_DTMSTR.substring(0,11) + "00" + ":" + "01";
					}
					L_RTNSTR = LP_DTMSTR.substring(6,10) + "-" + LP_DTMSTR.substring(3,5) + "-" + LP_DTMSTR.substring(0,2);
					L_RTNSTR += "-" + LP_DTMSTR.substring(11,13) + "." + LP_DTMSTR.substring(14,16) + "." + "00";
					L_RTNSTR = "'"+L_RTNSTR+"'";
				}
			}else
				L_RTNSTR = "null ";
		}catch(Exception L_EX){
			showEXMSG(L_EX,"setDTMDB2","");
		}
		return L_RTNSTR;
	}
/////////
private boolean chkFGRMM(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	String L_WRHTP="",L_TRNTP="",L_DOCTP ="",L_DOCNO ="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_WRHTP =  nvlSTRVL(LM_RSLSRC.getString("RM_WRHTP"),"");
			L_TRNTP =  nvlSTRVL(LM_RSLSRC.getString("RM_TRNTP"),"");
			L_DOCTP =  nvlSTRVL(LM_RSLSRC.getString("RM_DOCTP"),"");
			L_DOCNO =  nvlSTRVL(LM_RSLSRC.getString("RM_DOCNO"),"");
			setMSG("FG_RMMST : "+L_WRHTP +" / "+L_TRNTP +" / "+L_DOCTP +" / "+L_DOCNO,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update fg_rmmst set rm_trnfl ='1'");
			LM_UPDQRY.append("WHERE RM_WRHTP ='"+L_WRHTP +"' AND RM_TRNTP = '"+L_TRNTP +"' AND RM_DOCTP ='"+L_DOCTP +"' AND RM_DOCNO ='"+L_DOCNO +"'");
			LM_WHCND.append("WHERE RM_WRHTP ='"+L_WRHTP +"' AND RM_TRNTP = '"+L_TRNTP +"' AND RM_DOCTP ='"+L_DOCTP +"' AND RM_DOCNO ='"+L_DOCNO +"'");
			if(!insFGRMM(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_RMMST SET ";
					L_UPDSTR += "RM_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("RM_REMDS"),"") + "',";
					L_UPDSTR += "RM_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_STSFL"),"") + "',";
					L_UPDSTR += "RM_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_TRNFL"),"") + "',";
					L_UPDSTR += "RM_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("RM_LUSBY"),"") + "',";
					L_UPDSTR += "RM_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("RM_LUPDT"));

                    L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						setMSG("Updated the record for "+ L_DOCNO,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
						setMSG("Could not update the record for "+ L_DOCNO,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updFGRMM","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_RMMST ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkFGRMM "+L_EX.toString());
		showEXMSG(L_EX,"chkFGRMM","");
		return false;
	}
	return true;
}
private boolean insFGRMM(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR = "Insert Into FG_RMMST(RM_WRHTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS,";
		LM_TMPSTR += "RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)";
		LM_TMPSTR += " values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_WRHTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_TRNTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_DOCTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_DOCNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_REMDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RM_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"crtRMMST","");
		return false;
	}
}
private boolean chkLCMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	String L_WRHTP="",L_MNLCD="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_WRHTP =  nvlSTRVL(LM_RSLSRC.getString("LC_WRHTP"),"");
			L_MNLCD =  nvlSTRVL(LM_RSLSRC.getString("LC_MNLCD"),"");
			setMSG("FG_LCMST : "+L_WRHTP +" / "+L_MNLCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update fg_lcmst set lc_trnfl ='1'");
			LM_UPDQRY.append(" WHERE LC_WRHTP ='"+L_WRHTP +"' AND LC_MNLCD = '"+L_MNLCD +"'");
			LM_WHCND.append(" WHERE LC_WRHTP ='"+L_WRHTP +"' AND LC_MNLCD = '"+L_MNLCD +"'");
			if(!insLCMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_LCMST SET ";
					L_UPDSTR += "LC_LOCDS = '"+nvlSTRVL(LM_RSLSRC.getString("LC_LOCDS"),"") + "',";
					L_UPDSTR += "LC_MAXQT = "+nvlSTRVL(LM_RSLSRC.getString("LC_MAXQT"),"0") + ",";
					L_UPDSTR += "LC_STKQT ="+nvlSTRVL(LM_RSLSRC.getString("LC_STKQT"),"0") + ",";
					L_UPDSTR += "LC_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("LC_STSFL"),"") + "',";
					L_UPDSTR += "LC_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("LC_TRNFL"),"") + "',";
					L_UPDSTR += "LC_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("LC_LUSBY"),"") + "',";
					L_UPDSTR += "LC_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("LC_LUPDT"));

                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						setMSG("Updated the record for "+ L_WRHTP + " / "+L_MNLCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
						setMSG("Could not update the record for "+ L_WRHTP + " / "+L_MNLCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updLCMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_LCMST ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkLCMST "+L_EX.toString());
		showEXMSG(L_EX,"chkLCMST","");
		return false;
	}
	return true;
}
private boolean insLCMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR = "Insert Into FG_LCMST(LC_WRHTP,LC_MNLCD,LC_LOCDS,LC_MAXQT,";
		LM_TMPSTR += "LC_STKQT,LC_STSFL,LC_TRNFL,LC_LUSBY,LC_LUPDT)";
		LM_TMPSTR += " values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LC_WRHTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LC_MNLCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LC_LOCDS"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LC_MAXQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LC_STKQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LC_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LC_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LC_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("LC_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
	}
	catch(SQLException L_SE){
		showEXMSG(L_SE,"crtLCMST","");
		return false;
	}
}
private boolean chkRCTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	String L_WRHTP="",L_RCTTP="",L_RCTNO ="",L_PRDTP ="",L_LOTNO ="",L_RCLNO ="",L_PKGTP ="",L_MNLCD="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_WRHTP =  nvlSTRVL(LM_RSLSRC.getString("RCT_WRHTP"),"");
			L_RCTTP =  nvlSTRVL(LM_RSLSRC.getString("RCT_RCTTP"),"");
			L_RCTNO =  nvlSTRVL(LM_RSLSRC.getString("RCT_RCTNO"),"");
			L_PRDTP =  nvlSTRVL(LM_RSLSRC.getString("RCT_PRDTP"),"");
			L_LOTNO =  nvlSTRVL(LM_RSLSRC.getString("RCT_LOTNO"),"");
			L_RCLNO =  nvlSTRVL(LM_RSLSRC.getString("RCT_RCLNO"),"");
			L_PKGTP =  nvlSTRVL(LM_RSLSRC.getString("RCT_PKGTP"),"");
			L_MNLCD =  nvlSTRVL(LM_RSLSRC.getString("RCT_MNLCD"),"");
			setMSG("FG_RCTRN : "+L_WRHTP +" / "+L_RCTTP + " / "+L_RCTNO +" / "+L_PRDTP +" /"+ L_LOTNO + " / "+L_RCLNO ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			
			LM_WHCND.append(" WHERE RCT_WRHTP ='"+L_WRHTP +"'");
			LM_WHCND.append(" AND RCT_RCTTP ='"+L_RCTTP +"'");
			LM_WHCND.append(" AND RCT_RCTNO ='"+L_RCTNO +"'");
			LM_WHCND.append(" AND RCT_PRDTP ='"+L_PRDTP +"'");
			LM_WHCND.append(" AND RCT_LOTNO ='"+L_LOTNO +"'");
			LM_WHCND.append(" AND RCT_RCLNO ='"+L_RCLNO +"'");
			LM_WHCND.append(" AND RCT_PKGTP ='"+L_PKGTP +"'");
			LM_WHCND.append(" AND RCT_MNLCD ='"+L_MNLCD +"'");
			LM_UPDQRY.append("update fg_rctrn set rct_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insRCTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_RCTRN SET ";
					L_UPDSTR += "RCT_ISSRF = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_ISSRF"),"") + "',";
					L_UPDSTR += "RCT_STKTP = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_STKTP"),"") + "',";
					L_UPDSTR += "RCT_RCTQT = "+nvlSTRVL(LM_RSLSRC.getString("RCT_RCTQT"),"0") + ",";
					L_UPDSTR += "RCT_RCTPK = "+nvlSTRVL(LM_RSLSRC.getString("RCT_RCTPK"),"0") + ",";
					L_UPDSTR += "RCT_PKGCT = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_PKGCT"),"") + "',";
					L_UPDSTR += "RCT_PTFRF = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_PTFRF"),"") + "',";
					L_UPDSTR += "RCT_AUTBY = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_AUTBY"),"") + "',";
					L_UPDSTR += "RCT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_STSFL"),"") + "',";
					L_UPDSTR += "RCT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_TRNFL"),"") + "',";
					L_UPDSTR += "RCT_PKGTP = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_PKGTP"),"") + "',";
					L_UPDSTR += "RCT_SHFCD = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_SHFCD"),"") + "',";
					L_UPDSTR += "RCT_PRDCD = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_PRDCD"),"") + "',";
					L_UPDSTR += "RCT_RCLNO = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_RCLNO"),"") + "',";
					L_UPDSTR += "RCT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("RCT_LUSBY"),"") + "',";
					L_UPDSTR += "RCT_RCTDT ="+setDATE("MDY",LM_RSLSRC.getDate("RCT_RCTDT")) + ",";
					L_UPDSTR += "RCT_AUTDT ="+setDATE("MDY",LM_RSLSRC.getDate("RCT_AUTDT")) + ",";
					L_UPDSTR += "RCT_PTFDT ="+setDATE("MDY",LM_RSLSRC.getDate("RCT_PTFDT")) + ",";
                                        L_UPDSTR += "RCT_LUPDT ="+setDATE("MDY",LM_RSLSRC.getDate("RCT_LUPDT"));

                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_WRHTP + " / "+L_MNLCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_WRHTP + " / "+L_MNLCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updRCTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_RCTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkRCTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkRCTRN","");
		return false;
	}
	return true;
}
private boolean insRCTRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
try
{
	LM_TMPSTR = "Insert Into FG_RCTRN(RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_MNLCD,RCT_ISSRF,";
	LM_TMPSTR += "RCT_STKTP,RCT_RCTQT,RCT_RCTPK,RCT_PKGCT,RCT_PTFRF,RCT_AUTBY,RCT_STSFL,RCT_TRNFL,RCT_PKGTP,";
	LM_TMPSTR += "RCT_SHFCD,RCT_PRDCD,RCT_RCLNO,RCT_LUSBY,RCT_RCTDT,RCT_AUTDT,RCT_PTFDT,RCT_LUPDT)";
	LM_TMPSTR += " values ("; 
	LM_STLSQL = LM_TMPSTR;
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_WRHTP"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_RCTTP"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_RCTNO"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_PRDTP"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_LOTNO"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_MNLCD"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_ISSRF"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_STKTP"),"") + "',";
	LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RCT_RCTQT"),"0") + ",";
	LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RCT_RCTPK"),"0") + ",";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_PKGCT"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_PTFRF"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_AUTBY"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_STSFL"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_TRNFL"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_PKGTP"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_SHFCD"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_PRDCD"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_RCLNO"),"") + "',";
	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RCT_LUSBY"),"") + "',";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RCT_RCTDT")) + ",";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RCT_AUTDT")) + ",";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RCT_PTFDT")) + ",";
	LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RCT_LUPDT")) + ")";
	return chkUPDCT(LM_STLSQL);
}
catch(SQLException L_SE){
	showEXMSG(L_SE,"crtRCTRN","");
	return false;
}
}
private boolean chkLWMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	
	String L_WRHTP,L_RCTTP,L_RCTNO,L_PRDTP,L_LOTNO,L_RCLNO,L_STRTM;	
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_WRHTP =  nvlSTRVL(LM_RSLSRC.getString("LW_WRHTP"),"");
			L_RCTTP =  nvlSTRVL(LM_RSLSRC.getString("LW_RCTTP"),"");
			L_RCTNO =  nvlSTRVL(LM_RSLSRC.getString("LW_RCTNO"),"");
			L_PRDTP =  nvlSTRVL(LM_RSLSRC.getString("LW_PRDTP"),"");
			L_LOTNO =  nvlSTRVL(LM_RSLSRC.getString("LW_LOTNO"),"");
			L_RCLNO =  nvlSTRVL(LM_RSLSRC.getString("LW_RCLNO"),"");
			L_STRTM =  nvlSTRVL(LM_RSLSRC.getString("LW_STRTM"),"");
			setMSG("FG_LWMST : "+L_WRHTP +" / "+L_RCTTP+" / "+L_RCTNO +" / "+L_PRDTP +" / "+L_LOTNO +" / "+L_RCLNO +" / "+L_STRTM,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			
			LM_WHCND.append(" WHERE LW_WRHTP ='"+L_WRHTP +"'");
			LM_WHCND.append(" AND LW_RCTTP ='"+L_RCTTP +"'");
			LM_WHCND.append(" AND LW_RCTNO ='"+L_RCTNO +"'");
			LM_WHCND.append(" AND LW_PRDTP ='"+L_PRDTP +"'");
			LM_WHCND.append(" AND LW_LOTNO ='"+L_LOTNO +"'");
			LM_WHCND.append(" AND LW_RCLNO ='"+L_RCLNO +"'");
			LM_WHCND.append(" AND LW_STRTM ="+setDTMDB2(setDTMFMT(L_STRTM)));
			LM_UPDQRY.append("update fg_lwmst set lw_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insLWMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_LWMST SET ";
					L_UPDSTR += "LW_STRCT ="+nvlSTRVL(LM_RSLSRC.getString("LW_STRCT"),"0") + ",";
					L_UPDSTR += "LW_ENDCT ="+nvlSTRVL(LM_RSLSRC.getString("LW_ENDCT"),"0") + ",";
					L_UPDSTR += "LW_MISCT ="+nvlSTRVL(LM_RSLSRC.getString("LW_MISCT"),"0") + ",";
					L_UPDSTR += "LW_BAGQT ="+nvlSTRVL(LM_RSLSRC.getString("LW_BAGQT"),"0") + ",";
					L_UPDSTR += "LW_BAGPK ="+nvlSTRVL(LM_RSLSRC.getString("LW_BAGPK"),"0") + ",";
					L_UPDSTR += "LW_MCHNO = '"+nvlSTRVL(LM_RSLSRC.getString("LW_MCHNO"),"") + "',";
					L_UPDSTR += "LW_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("LW_STSFL"),"") + "',";
					L_UPDSTR += "LW_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("LW_TRNFL"),"") + "',";
					L_UPDSTR += "LW_SHFCD = '"+nvlSTRVL(LM_RSLSRC.getString("LW_SHFCD"),"") + "',";
					L_UPDSTR += "LW_MNLCD = '"+nvlSTRVL(LM_RSLSRC.getString("LW_MNLCD"),"") + "',";
					L_UPDSTR += "LW_PKGTP = '"+nvlSTRVL(LM_RSLSRC.getString("LW_PKGTP"),"") + "',";
					L_UPDSTR += "LW_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("LW_REMDS"),"") + "',";
					L_UPDSTR += "LW_UCLTG = '"+nvlSTRVL(LM_RSLSRC.getString("LW_UCLTG"),"") + "',";
					L_UPDSTR += "LW_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("LW_LUSBY"),"") + "',";
					L_UPDSTR += "LW_ENDTM ="+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("LW_ENDTM"))) + ",";
					L_UPDSTR += "LW_LUPDT ="+setDATE("MDY",LM_RSLSRC.getDate("LW_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_WRHTP + " / "+L_MNLCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_WRHTP + " / "+L_MNLCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updLWMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_LWMST ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkLWMST "+L_EX.toString());
		showEXMSG(L_EX,"chkLWMST","");
		return false;
	}
	return true;
}
private boolean insLWMST(ResultSet LP_RSLSET,String LP_SYSCD)
	{
		try
		{
			LM_TMPSTR = "Insert Into FG_LWMST(LW_WRHTP,LW_RCTTP,LW_RCTNO,LW_PRDTP,LW_LOTNO,LW_RCLNO,";
			LM_TMPSTR += "LW_STRCT,LW_ENDCT,LW_MISCT,LW_BAGQT,LW_BAGPK,LW_MCHNO,LW_STSFL,LW_TRNFL,";
			LM_TMPSTR += "LW_SHFCD,LW_MNLCD,LW_PKGTP,LW_REMDS,LW_UCLTG,LW_LUSBY,LW_STRTM,LW_ENDTM,LW_RCTDT,LW_LUPDT)";
			LM_TMPSTR += " values ("; 
			LM_STLSQL = LM_TMPSTR;
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_WRHTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_RCTTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_RCTNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_PRDTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_LOTNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_RCLNO"),"") + "',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LW_STRCT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LW_ENDCT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LW_MISCT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LW_BAGQT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("LW_BAGPK"),"0") + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_MCHNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_STSFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_TRNFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_SHFCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_MNLCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_PKGTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_REMDS"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_UCLTG"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("LW_LUSBY"),"") + "',";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LW_STRTM"))) + ",";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("LW_ENDTM"))) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("LW_RCTDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("LW_LUPDT")) + ")";
			return chkUPDCT(LM_STLSQL);
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtLWMST","");
			return false;
		}
	}
private boolean chkPTFRF(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	String L_PTFNO,L_PRDTP,L_LOTNO,L_RCLNO,L_PKGTP;	
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_PTFNO =  nvlSTRVL(LM_RSLSRC.getString("PTF_PTFNO"),"");
			L_PRDTP =  nvlSTRVL(LM_RSLSRC.getString("PTF_PRDTP"),"");
			L_LOTNO =  nvlSTRVL(LM_RSLSRC.getString("PTF_LOTNO"),"");
			L_RCLNO =  nvlSTRVL(LM_RSLSRC.getString("PTF_RCLNO"),"");
			L_PKGTP =  nvlSTRVL(LM_RSLSRC.getString("PTF_PKGTP"),"");
			setMSG("FG_PTFRF : "+L_PTFNO +" / "+L_PRDTP+" / "+L_LOTNO+" / "+L_RCLNO+" / "+L_PKGTP,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE PTF_PTFNO ='"+L_PTFNO +"'");
			LM_WHCND.append(" AND PTF_PRDTP ='"+L_PRDTP +"'");
			LM_WHCND.append(" AND PTF_LOTNO ='"+L_LOTNO +"'");
			LM_WHCND.append(" AND PTF_RCLNO ='"+L_RCLNO +"'");
			LM_WHCND.append(" AND PTF_PKGTP ='"+L_PKGTP +"'");
			LM_UPDQRY.append("update fg_ptfrf set ptf_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPTFRF(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_PTFRF SET ";
					L_UPDSTR += "PTF_OPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("PTF_OPRCD"),"").trim() + "',";
					L_UPDSTR += "PTF_PRDCD = '"+nvlSTRVL(LM_RSLSRC.getString("PTF_PRDCD"),"").trim() + "',";
					L_UPDSTR += "PTF_PTFCT = '"+nvlSTRVL(LM_RSLSRC.getString("PTF_PTFCT"),"").trim() + "',";
					L_UPDSTR += "PTF_PTFDT = "+setDATE("MDY",LM_RSLSRC.getDate("PTF_PTFDT")) + ",";
					L_UPDSTR += "PTF_PTFQT = " +nvlSTRVL(LM_RSLSRC.getString("PTF_PTFQT"),"0") + ",";
					L_UPDSTR += "PTF_PTFRF = '"+nvlSTRVL(LM_RSLSRC.getString("PTF_PTFRF"),"") + "',";
					L_UPDSTR += "PTF_PTFBY = '"+nvlSTRVL(LM_RSLSRC.getString("PTF_PTFBY"),"") + "')";
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_WRHTP + " / "+L_MNLCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_WRHTP + " / "+L_MNLCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updPTFRF","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_PTFRF ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkPTFRF "+L_EX.toString());
		showEXMSG(L_EX,"chkLWMST","");
		return false;
	}
	return true;
}
private boolean insPTFRF(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR = "Insert Into FG_PTFRF(PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_RCLNO,PTF_PKGTP,";
		LM_TMPSTR += "PTF_OPRCD,PTF_PRDCD,PTF_PTFCT,PTF_PTFDT,PTF_PTFQT,PTF_PTFRF,PTF_PTFBY)";
		LM_TMPSTR += " values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_PTFNO"),"").trim() + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_PRDTP"),"").trim() + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_LOTNO"),"").trim() + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_RCLNO"),"").trim() + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_PKGTP"),"").trim() + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_OPRCD"),"").trim() + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_PRDCD"),"").trim() + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_PTFCT"),"").trim() + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("PTF_PTFDT")) + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("PTF_PTFQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_PTFRF"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("PTF_PTFBY"),"") + "')";
		return chkUPDCT(LM_STLSQL);
	}
	catch(SQLException L_EX)
	{
		showEXMSG(L_EX,"crtPTFRF","");
		System.out.println(L_EX.toString());
		return false;
	}
}
private boolean chkISTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	String L_WRHTP,L_ISSTP,L_ISSNO,L_PRDCD,L_PRDTP,L_LOTNO,L_RCLNO,L_MNLCD;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_WRHTP =  nvlSTRVL(LM_RSLSRC.getString("IST_WRHTP"),"");
			L_ISSTP =  nvlSTRVL(LM_RSLSRC.getString("IST_ISSTP"),"");
			L_ISSNO =  nvlSTRVL(LM_RSLSRC.getString("IST_ISSNO"),"");
			L_PRDCD =  nvlSTRVL(LM_RSLSRC.getString("IST_PRDCD"),"");
			L_PRDTP =  nvlSTRVL(LM_RSLSRC.getString("IST_PRDTP"),"");
			L_LOTNO =  nvlSTRVL(LM_RSLSRC.getString("IST_LOTNO"),"");
			L_RCLNO =  nvlSTRVL(LM_RSLSRC.getString("IST_RCLNO"),"");
			L_MNLCD =  nvlSTRVL(LM_RSLSRC.getString("IST_MNLCD"),"");
			setMSG("FG_ISTRN : "+L_WRHTP+" / "+L_ISSTP+" / "+L_ISSNO+" / "+L_PRDCD+" / "+L_PRDTP+" / "+L_LOTNO+" / "+L_RCLNO+" / "+L_MNLCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			
			LM_WHCND.append(" WHERE IST_WRHTP ='"+L_WRHTP +"'");
			LM_WHCND.append(" and IST_ISSTP ='"+L_ISSTP.trim()+"'");
			LM_WHCND.append(" and IST_ISSNO ='"+L_ISSNO.trim()+"'");
			LM_WHCND.append(" and IST_PRDCD ='"+L_PRDCD.trim()+"'");
			LM_WHCND.append(" and IST_PRDTP ='"+L_PRDTP.trim()+"'");
			LM_WHCND.append(" and IST_LOTNO ='"+L_LOTNO.trim()+"'");
			LM_WHCND.append(" and IST_RCLNO ='"+L_RCLNO.trim()+"'");
			LM_WHCND.append(" and IST_MNLCD ='"+L_MNLCD.trim()+"'");
			LM_UPDQRY.append("update fg_istrn set ist_trnfl ='1' ");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insISTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_ISTRN SET ";
					L_UPDSTR += "IST_ISSQT = "+nvlSTRVL(LM_RSLSRC.getString("IST_ISSQT"),"0") + ",";
					L_UPDSTR += "IST_ISSPK = "+nvlSTRVL(LM_RSLSRC.getString("IST_ISSPK"),"0") + ",";
					L_UPDSTR += "IST_STKTP = '"+nvlSTRVL(LM_RSLSRC.getString("IST_STKTP"),"") + "',";
					L_UPDSTR += "IST_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("IST_STSFL"),"") + "',";
					L_UPDSTR += "IST_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("IST_TRNFL"),"") + "',";
					L_UPDSTR += "IST_TDSFL = '"+nvlSTRVL(LM_RSLSRC.getString("IST_TDSFL"),"") + "',";
					L_UPDSTR += "IST_PKGCT = '"+nvlSTRVL(LM_RSLSRC.getString("IST_PKGCT"),"") + "',";
					L_UPDSTR += "IST_SALTP = '"+nvlSTRVL(LM_RSLSRC.getString("IST_SALTP"),"") + "',";
					L_UPDSTR += "IST_MKTTP = '"+nvlSTRVL(LM_RSLSRC.getString("IST_MKTTP"),"") + "',";
					L_UPDSTR += "IST_RCLNO = '"+nvlSTRVL(LM_RSLSRC.getString("IST_RCLNO"),"") + "',";
					L_UPDSTR += "IST_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("IST_LUSBY"),"") + "',";
					L_UPDSTR += "IST_ISSDT = "+setDATE("MDY",LM_RSLSRC.getDate("IST_ISSDT")) + ",";
					L_UPDSTR += "IST_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("IST_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_WRHTP + " / "+L_MNLCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_WRHTP + " / "+L_MNLCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"upISTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_ISTRN",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkISTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkISTRN","");
		return false;
	}
	return true;
}

	private boolean insISTRN(ResultSet LP_RSLSET,String LP_SYSCD)
	{
	try
	{
			LM_TMPSTR = "Insert Into FG_ISTRN(IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDCD,IST_PRDTP,";
			LM_TMPSTR += "IST_LOTNO,IST_PKGTP,IST_MNLCD,IST_ISSQT,IST_ISSPK,IST_STKTP,IST_STSFL,";
			LM_TMPSTR += "IST_TRNFL,IST_TDSFL,IST_PKGCT,IST_SALTP,IST_MKTTP,IST_RCLNO,IST_LUSBY,IST_ISSDT,IST_LUPDT)";
			LM_TMPSTR += " values ("; 
			LM_STLSQL = LM_TMPSTR;
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_WRHTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_ISSTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_ISSNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_PRDCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_PRDTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_LOTNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_PKGTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_MNLCD"),"") + "',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IST_ISSQT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IST_ISSPK"),"0") + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_STKTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_STSFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_TRNFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_TDSFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_PKGCT"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_SALTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_MKTTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_RCLNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IST_LUSBY"),"") + "',";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IST_ISSDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IST_LUPDT")) + ")";
			return chkUPDCT(LM_STLSQL);
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtISTRN","");
			return false;
		}
	}
private boolean chkRSTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	String L_WRHTP="",L_RESTP="",L_RESNO="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_WRHTP =  nvlSTRVL(LM_RSLSRC.getString("RS_WRHTP"),"");
			L_RESTP =  nvlSTRVL(LM_RSLSRC.getString("RS_RESTP"),"");
			L_RESNO =  nvlSTRVL(LM_RSLSRC.getString("RS_RESNO"),"");
			setMSG("FG_RSTRN : "+L_WRHTP +" / "+L_RESTP + " / "+L_RESNO,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update fg_rstrn set rs_trnfl ='1'");
			LM_UPDQRY.append("WHERE RS_WRHTP ='"+L_WRHTP +"' AND RS_RESTP = '"+L_RESTP +"' AND RS_RESNO ='"+L_RESNO +"'");
			LM_WHCND.append("WHERE RS_WRHTP ='"+L_WRHTP +"' AND RS_RESTP = '"+L_RESTP +"' AND RS_RESNO ='"+L_RESNO +"'");
			if(!insRSTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_RSTRN SET ";
					L_UPDSTR += "RS_PRDCD = '"+nvlSTRVL(LM_RSLSRC.getString("RS_PRDCD"),"") + "',";
					L_UPDSTR += "RS_REQBY = '"+nvlSTRVL(LM_RSLSRC.getString("RS_REQBY"),"") + "',";
					L_UPDSTR += "RS_AUTBY = '"+nvlSTRVL(LM_RSLSRC.getString("RS_AUTBY"),"") + "',";
					L_UPDSTR += "RS_RESCD = '"+nvlSTRVL(LM_RSLSRC.getString("RS_RESCD"),"") + "',";
					L_UPDSTR += "RS_RESDS = '"+nvlSTRVL(LM_RSLSRC.getString("RS_RESDS"),"") + "',";
					L_UPDSTR += "RS_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("RS_REMDS"),"") + "',";
					L_UPDSTR += "RS_RESQT = "+ nvlSTRVL(LM_RSLSRC.getString("RS_RESQT"),"0") + ",";
					L_UPDSTR += "RS_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("RS_LUSBY"),"") + "',";
					L_UPDSTR += "RS_RESDT ="+setDATE("MDY",LM_RSLSRC.getDate("RS_RESDT")) + ",";
					L_UPDSTR += "RS_REXDT ="+setDATE("MDY",LM_RSLSRC.getDate("RS_REXDT")) + ",";
					L_UPDSTR += "RS_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("RS_LUPDT")) + ")";
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						setMSG("Updated the record for "+ L_WRHTP + " / "+L_RESTP+ " / "+L_RESNO,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
						setMSG("Could not Update the record for "+ L_WRHTP + " / "+L_RESTP+ " / "+L_RESNO,'N');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updRSTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_RSTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkRSTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkRSTRN","");
		return false;
	}
	return true;
}	
private boolean insRSTRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR = "Insert Into FG_RSTRN(RS_WRHTP,RS_RESTP,RS_RESNO,RS_PRDCD,";
		LM_TMPSTR += "RS_REQBY,RS_AUTBY,RS_RESCD,RS_RESDS,RS_REMDS,RS_RESQT,";
		LM_TMPSTR += "RS_LUSBY,RS_RESDT,RS_REXDT,RS_LUPDT) values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_WRHTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_RESTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_RESNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_PRDCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_REQBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_AUTBY"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_RESCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_RESDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_REMDS"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("RS_RESQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RS_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RS_RESDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RS_REXDT")) + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RS_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
	}
	catch(SQLException L_EX){
		showEXMSG(L_EX,"crtRSTRN","");
		return false;
	}
}
private boolean chkSTMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_SYSCD ="",L_UPDSTR="";
	String L_PRDTP,L_LOTNO,L_RCLNO,L_PKGTP,L_MNLCD;	
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_PRDTP =  nvlSTRVL(LM_RSLSRC.getString("ST_PRDTP"),"");
			L_LOTNO =  nvlSTRVL(LM_RSLSRC.getString("ST_LOTNO"),"");
			L_RCLNO =  nvlSTRVL(LM_RSLSRC.getString("ST_RCLNO"),"");
			L_PKGTP =  nvlSTRVL(LM_RSLSRC.getString("ST_PKGTP"),"");
			L_MNLCD =  nvlSTRVL(LM_RSLSRC.getString("ST_MNLCD"),"");
			setMSG("FG_STMST : "+L_PRDTP +" / "+L_LOTNO+" / "+L_RCLNO +" / "+L_PKGTP +" / "+L_MNLCD,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE ST_PRDTP ='"+L_PRDTP +"'");
			LM_WHCND.append(" AND ST_LOTNO ='"+L_LOTNO +"'");
			LM_WHCND.append(" AND ST_RCLNO ='"+L_RCLNO +"'");
			LM_WHCND.append(" AND ST_PKGTP ='"+L_PKGTP +"'");
			LM_WHCND.append(" AND ST_MNLCD ='"+L_MNLCD +"'");
			LM_UPDQRY.append("update fg_stmst set st_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insSTMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_STMST SET ";
					L_UPDSTR += "ST_STKQT = "+nvlSTRVL(LM_RSLSRC.getString("ST_STKQT"),"0") + ",";
					L_UPDSTR += "ST_TPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("ST_TPRCD"),"") + "',";
					L_UPDSTR += "ST_CPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("ST_CPRCD"),"") + "',";
					L_UPDSTR += "ST_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("ST_TRNFL"),"") + "',";
					L_UPDSTR += "ST_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("ST_STSFL"),"") + "',";
					L_UPDSTR += "ST_ALOQT = "+nvlSTRVL(LM_RSLSRC.getString("ST_ALOQT"),"0") + ",";
					L_UPDSTR += "ST_UCLQT = "+nvlSTRVL(LM_RSLSRC.getString("ST_UCLQT"),"0") + ",";
					L_UPDSTR += "ST_DOUQT = "+nvlSTRVL(LM_RSLSRC.getString("ST_DOUQT"),"0") + ",";
					L_UPDSTR += "ST_DOSQT = "+nvlSTRVL(LM_RSLSRC.getString("ST_DOSQT"),"0") + ",";
					L_UPDSTR += "ST_MAXQT = "+nvlSTRVL(LM_RSLSRC.getString("ST_MAXQT"),"0") + ",";
					L_UPDSTR += "ST_PRDCD = '"+nvlSTRVL(LM_RSLSRC.getString("ST_PRDCD"),"") + "',";
					L_UPDSTR += "ST_RESNO = '"+nvlSTRVL(LM_RSLSRC.getString("ST_RESNO"),"") + "',";
					L_UPDSTR += "ST_RESCD = '"+nvlSTRVL(LM_RSLSRC.getString("ST_RESCD"),"") + "',";
					L_UPDSTR += "ST_PKGCT = '"+nvlSTRVL(LM_RSLSRC.getString("ST_PKGCT"),"") + "',";
					L_UPDSTR += "ST_PKGTP = '"+nvlSTRVL(LM_RSLSRC.getString("ST_PKGTP"),"") + "',";
					L_UPDSTR += "ST_PKGWT = " +nvlSTRVL(LM_RSLSRC.getString("ST_PKGWT"),"0") + ",";
					L_UPDSTR += "ST_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("ST_REMDS"),"") + "',";
					L_UPDSTR += "ST_RESFL = '"+nvlSTRVL(LM_RSLSRC.getString("ST_RESFL"),"") + "',";
					L_UPDSTR += "ST_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("ST_LUSBY"),"") + "',";
					L_UPDSTR += "ST_RCTDT = "+setDATE("MDY",LM_RSLSRC.getDate("ST_RCTDT")) + ",";
					L_UPDSTR += "ST_RESDT = " +setDATE("MDY",LM_RSLSRC.getDate("ST_RESDT")) + ",";
					L_UPDSTR += "ST_REXDT = "+setDATE("MDY",LM_RSLSRC.getDate("ST_REXDT")) + ",";
					L_UPDSTR += "ST_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("ST_LUPDT"));

                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updSTMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_STMST ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkLWMST "+L_EX.toString());
		showEXMSG(L_EX,"chkLWMST","");
		return false;
	}
	return true;
}
	private boolean insSTMST(ResultSet LP_RSLSET,String LP_SYSCD){
		try
		{
			LM_TMPSTR = "Insert Into FG_STMST(ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_MNLCD,ST_STKQT,ST_TPRCD,ST_CPRCD,";
			LM_TMPSTR += "ST_TRNFL,ST_STSFL,ST_ALOQT,ST_UCLQT,ST_DOUQT,ST_DOSQT,ST_MAXQT,ST_PRDCD,ST_RESNO,";
			LM_TMPSTR += "ST_RESCD,ST_PKGCT,ST_PKGTP,ST_PKGWT,ST_RCLNO,ST_REMDS,ST_RESFL,ST_LUSBY,ST_RCTDT,ST_RESDT,ST_REXDT,ST_LUPDT)";
			LM_TMPSTR += " values ("; 
			LM_STLSQL = LM_TMPSTR;
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_WRHTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_PRDTP"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_LOTNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_MNLCD"),"") + "',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("ST_STKQT"),"0") + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_TPRCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_CPRCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_TRNFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_STSFL"),"") + "',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("ST_ALOQT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("ST_UCLQT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("ST_DOUQT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("ST_DOSQT"),"0") + ",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("ST_MAXQT"),"0") + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_PRDCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_RESNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_RESCD"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_PKGCT"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_PKGTP"),"") + "',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("ST_PKGWT"),"0") + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_RCLNO"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_REMDS"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_RESFL"),"") + "',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("ST_LUSBY"),"") + "',";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("ST_RCTDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("ST_RESDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("ST_REXDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("ST_LUPDT")) + ")";
			return chkUPDCT(LM_STLSQL);
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtSTMST","");
			return false;
		}
	}
	//
private boolean chkOPSTK(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_WRHTP ="",L_PRDTP ="",L_PRDCD ="",L_PKGCT ="",L_PKGTP ="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_WRHTP = nvlSTRVL(LM_RSLSRC.getString("OP_WRHTP"),"");
			L_PRDTP = nvlSTRVL(LM_RSLSRC.getString("OP_PRDTP"),"");
			L_PRDCD = nvlSTRVL(LM_RSLSRC.getString("OP_PRDCD"),"");
			L_PKGCT = nvlSTRVL(LM_RSLSRC.getString("OP_PKGCT"),"");
			L_PKGTP = nvlSTRVL(LM_RSLSRC.getString("OP_PKGTP"),"");
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE OP_WRHTP ='"+L_WRHTP +"'");
			LM_WHCND.append(" and OP_PRDTP ='"+L_PRDTP.trim()+"'");
			LM_WHCND.append(" and OP_PRDCD ='"+L_PRDCD.trim()+"'");
			LM_WHCND.append(" and OP_PKGCT ='"+L_PKGCT.trim()+"'");
			LM_WHCND.append(" and OP_PKGTP ='"+L_PKGTP.trim()+"'");
			LM_UPDQRY.append("update fg_opstk set op_trnfl ='1' ");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insOPSTK(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR = "UPDATE FG_OPSTK SET ";
					L_UPDSTR += "OP_YOSQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_YOSQT"),"0") + ",";
					L_UPDSTR += "OP_SLRQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_SLRQT"),"0") + ",";
					L_UPDSTR += "OP_YXRQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_YXRQT"),"0") + ",";
					L_UPDSTR += "OP_MXRQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_MXRQT"),"0") + ",";
					L_UPDSTR += "OP_DXRQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_DXRQT"),"0") + ",";
					L_UPDSTR += "OP_YRCQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_YRCQT"),"0") + ",";
					L_UPDSTR += "OP_MRCQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_MRCQT"),"0") + ",";
					L_UPDSTR += "OP_DRCQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_DRCQT"),"0") + ",";
					L_UPDSTR += "OP_YXDQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_YXDQT"),"0") + ",";
					L_UPDSTR += "OP_MXDQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_MXDQT"),"0") + ",";
					L_UPDSTR += "OP_DXDQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_DXDQT"),"0") + ",";
					L_UPDSTR += "OP_Y1DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_Y1DQT"),"0") + ",";
					L_UPDSTR += "OP_M1DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_M1DQT"),"0") + ",";
					L_UPDSTR += "OP_D1DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_D1DQT"),"0") + ",";
					L_UPDSTR += "OP_Y2DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_Y2DQT"),"0") + ",";
					L_UPDSTR += "OP_M2DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_M2DQT"),"0") + ",";
					L_UPDSTR += "OP_D2DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_D2DQT"),"0") + ",";
					L_UPDSTR += "OP_Y3DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_Y3DQT"),"0") + ",";
					L_UPDSTR += "OP_M3DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_M3DQT"),"0") + ",";
					L_UPDSTR += "OP_D3DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_D3DQT"),"0") + ",";
					L_UPDSTR += "OP_Y6DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_Y6DQT"),"0") + ",";
					L_UPDSTR += "OP_M6DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_M6DQT"),"0") + ",";
					L_UPDSTR += "OP_D6DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_D6DQT"),"0") + ",";
					L_UPDSTR += "OP_Y4DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_Y4DQT"),"0") + ",";
					L_UPDSTR += "OP_M4DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_M4DQT"),"0") + ",";
					L_UPDSTR += "OP_D4DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_D4DQT"),"0") + ",";
					L_UPDSTR += "OP_Y5DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_Y5DQT"),"0") + ",";
					L_UPDSTR += "OP_M5DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_M5DQT"),"0") + ",";
					L_UPDSTR += "OP_D5DQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_D5DQT"),"0") + ",";
					L_UPDSTR += "OP_UCLQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_UCLQT"),"0") + ",";
					L_UPDSTR += "OP_TDSQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_TDSQT"),"0") + ",";
					L_UPDSTR += "OP_STKQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_STKQT"),"0") + ",";
					L_UPDSTR += "OP_DOSQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_DOSQT"),"0") + ",";
					L_UPDSTR += "OP_DOUQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_DOUQT"),"0") + ",";
					L_UPDSTR += "OP_YAPQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_YAPQT"),"0") + ",";
					L_UPDSTR += "OP_YAMQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_YAMQT"),"0") + ",";
					L_UPDSTR += "OP_YOXQT = "+nvlSTRVL(LM_RSLSRC.getString("OP_YOXQT"),"0") + ",";
					L_UPDSTR += "OP_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("OP_LUPDT")) ;
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_WRHTP + " / "+L_MNLCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_WRHTP + " / "+L_MNLCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updOPSTK","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for FG_OPSTK",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkOPSTK "+L_EX.toString());
		showEXMSG(L_EX,"chkOPSTK","");
		return false;
	}
	return true;
}

private boolean insOPSTK(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_TMPSTR = "Insert Into FG_OPSTK(OP_WRHTP,OP_PRDTP,OP_PKGCT,OP_YOSQT,OP_PRDCD,OP_SLRQT,OP_PKGTP,";
		LM_TMPSTR += "OP_YXRQT,OP_MXRQT,OP_DXRQT,OP_YRCQT,OP_MRCQT,OP_DRCQT,OP_YXDQT,OP_MXDQT,OP_DXDQT,";
		LM_TMPSTR += "OP_Y1DQT,OP_M1DQT,OP_D1DQT,OP_Y2DQT,OP_M2DQT,OP_D2DQT,OP_Y3DQT,OP_M3DQT,OP_D3DQT,";
		LM_TMPSTR += "OP_Y6DQT,OP_M6DQT,OP_D6DQT,OP_Y4DQT,OP_M4DQT,OP_D4DQT,OP_Y5DQT,OP_M5DQT,OP_D5DQT,";
		LM_TMPSTR += "OP_UCLQT,OP_TDSQT,OP_STKQT,OP_DOSQT,OP_DOUQT,OP_YAPQT,OP_YAMQT,OP_YOXQT,OP_LUPDT) values ("; 
		LM_STLSQL = LM_TMPSTR;
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("OP_WRHTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("OP_PRDTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("OP_PKGCT"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_YOSQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("OP_PRDCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_SLRQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("OP_PKGTP"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_YXRQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_MXRQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_DXRQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_YRCQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_MRCQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_DRCQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_YXDQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_MXDQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_DXDQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_Y1DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_M1DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_D1DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_Y2DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_M2DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_D2DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_Y3DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_M3DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_D3DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_Y6DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_M6DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_D6DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_Y4DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_M4DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_D4DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_Y5DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_M5DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_D5DQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_UCLQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_TDSQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_STKQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_DOSQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_DOUQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_YAPQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_YAMQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("OP_YOXQT"),"0") + ",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("OP_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
}
catch(SQLException L_EX)
{
	showEXMSG(L_EX,"crtOPSTK","");
	return false;
}
}
	public String setDATE(String LP_DATFMT,java.util.Date oDT){
		String strCURDT = "";
        SimpleDateFormat dtFORM;
        if (LP_DATFMT.equals("DMY"))
           dtFORM = new SimpleDateFormat("dd/MM/yyyy");
        else 
           dtFORM = new SimpleDateFormat("MM/dd/yyyy");
		if(oDT != null)
			strCURDT = dtFORM.format(oDT);
		if(strCURDT.trim().length() >0)
			return "'"+strCURDT+"'";
		else
			return null;
		
	}
	
	public  String setDTMFMT(String LP_DTMSTR){
		String L_RTNSTR = "";
		try{
			if(LP_DTMSTR != null){
				if(LP_DTMSTR.length() > 0){
					int L_LEN = LP_DTMSTR.trim().length();
					if(L_LEN >=16){
						L_RTNSTR = LP_DTMSTR.substring(8,10)+"/"+ LP_DTMSTR.substring(5,7)+"/"+LP_DTMSTR.substring(0,4);
					    L_RTNSTR = L_RTNSTR + " "+ LP_DTMSTR.substring(11,13)+ ":" + LP_DTMSTR.substring(14,16);
					}
					if(L_RTNSTR.trim().equals("30/12/1899 00:00"))
					   L_RTNSTR = "";
				}
			}
		}catch(Exception L_EX){
			System.out.println("setDTMFMT: "+L_EX);
		}
		return L_RTNSTR;
	}
private boolean chkUPDCT(String LP_STRSQL)
	{
		try
		{
			if(stmDESUP.executeUpdate(LP_STRSQL)== 1)
			{
				conDESDB.commit();
				return true;
			}
			else
			{
				conDESDB.rollback();
				return false;
			}
		}
		catch(SQLException L_EX)
		{
			if(L_EX.getErrorCode() != -803) // error code for duplicate key value specified on AS/400
			{
				showEXMSG(L_EX,"chkUPDCT","");
				System.out.println(L_EX.toString() +LP_STRSQL);
			}
			return false;
		}
		catch(Exception L_EX)
		{
			showEXMSG(L_EX,"chkUPDCT","");
			System.out.println(L_EX.toString() +LP_STRSQL);
			return false;
		}
	}
private boolean insSMTRN(ResultSet LP_RSLSET,String  LP_SYSCD)
	{
		try
		{
			LM_STLSQL = "Insert Into QC_SMTRN(SMT_QCATP,SMT_TSTTP,SMT_TSTNO,SMT_MORTP,SMT_TSTDT,SMT_TSTRF,";
			LM_STLSQL +="SMT_COLVL,SMT_RI_VL,SMT_TBCVL,SMT_POLVL,SMT_BZ_VL,SMT_TOLVL,SMT_EBSVL,SMT_XYSVL,SMT_CUMVL,";
			LM_STLSQL +="SMT_STYVL,SMT_BHDVL,SMT_ULBVL,SMT_UHBVL,SMT_MOIVL,SMT_TMPVL,SMT_SPGVL,";
			LM_STLSQL +="SMT_EB_VL,SMT_XYLVL,SMT_PMRVL,SMT_QLTVL,SMT_TNKCT,SMT_TKLCD,SMT_PRDCD,SMT_QLTFL,";
			LM_STLSQL +="SMT_ADDBY,SMT_ADDTM,SMT_STSFL,SMT_TRNFL,SMT_LUSBY,SMT_LUPDT,SMT_TSTBY)";
			LM_STLSQL +="values("; 
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_QCATP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_TSTTP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_TSTNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_MORTP"),"")+"',";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("SMT_TSTDT"))) + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_TSTRF"),"")+"',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_COLVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_RI_VL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_TBCVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_POLVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_BZ_VL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_TOLVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_EBSVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_XYSVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_CUMVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_STYVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_BHDVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_ULBVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_UHBVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_MOIVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_TMPVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_SPGVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_EB_VL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_XYLVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_PMRVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_QLTVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("SMT_TNKCT"),"0")+",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_TKLCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_PRDCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_QLTFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_ADDBY"),"")+"',";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("SMT_ADDTM"))) + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_STSFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_TRNFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_LUSBY"),"")+"',";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("SMT_LUPDT")) + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("SMT_TSTBY"),"")+"')";
			return chkUPDCT(LM_STLSQL);
		}
		catch(SQLException L_SE)
		{
			showEXMSG(L_SE,"crtSMTRN","");
			return false;
		}
}
	private boolean insWTTRN(ResultSet LP_RSLSET,String  LP_SYSCD)
	{
		try
		{
			LM_STLSQL = "Insert Into QC_WTTRN(WTT_QCATP,WTT_TSTTP,WTT_TSTNO,WTT_TSTDT,WTT_WTRTP,";
			LM_STLSQL +="WTT_PH_VL,WTT_CONVL,WTT_TBDVL,WTT_HARVL,WTT_PALVL,WTT_MALVL,WTT_CHLVL,WTT_IROVL,WTT_PO4VL,";
			LM_STLSQL +="WTT_SILVL,WTT_SPTVL,WTT_TDSVL,WTT_TSSVL,WTT_DO_VL,WTT_BODVL,WTT_CODVL,WTT_ONGVL,WTT_BASVL,WTT_SPDVL,WTT_STSFL,WTT_TRNFL,WTT_LUSBY,WTT_LUPDT,WTT_TSTBY)";
			LM_STLSQL +="values("; 
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_QCATP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_TSTTP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_TSTNO"),"")+"',";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("WTT_TSTDT"))) + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_WTRTP"),"")+"',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_PH_VL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_CONVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_TBDVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_HARVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_PALVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_MALVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_CHLVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_IROVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_PO4VL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_SILVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_SPTVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_TDSVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_TSSVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_DO_VL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_BODVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_CODVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_ONGVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_BASVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("WTT_SPDVL"),"0")+",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_STSFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_TRNFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_LUSBY"),"")+"',";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("WTT_LUPDT")) + ",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("WTT_TSTBY"),"")+"')";
			return chkUPDCT(LM_STLSQL);
		}
		catch(SQLException L_SE)
		{
			showEXMSG(L_SE,"crtWTTRN","");
			return false;
		}
	
	}
private boolean chkIVTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_MKTTP,L_LADNO,L_PRDCD,L_PKGTP;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_MKTTP = nvlSTRVL(LM_RSLSRC.getString("IVT_MKTTP"),"");
			L_LADNO = nvlSTRVL(LM_RSLSRC.getString("IVT_LADNO"),"");
			L_PRDCD = nvlSTRVL(LM_RSLSRC.getString("IVT_PRDCD"),"");
			L_PKGTP = nvlSTRVL(LM_RSLSRC.getString("IVT_PKGTP"),"");
			setMSG("MR_IVTRn : "+L_MKTTP +" / "+L_LADNO +" / "+L_PRDCD +" / "+L_PKGTP ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update mr_ivtrn set ivt_trnfl ='1' ");
			LM_WHCND.append(" WHERE IVT_MKTTP ='"+L_MKTTP +"'");
			LM_WHCND.append(" AND IVT_LADNO ='"+L_LADNO +"'");
			LM_WHCND.append(" AND IVT_PRDCD ='"+L_PRDCD +"'");
			LM_WHCND.append(" AND IVT_PKGTP ='"+L_PKGTP +"'");
			LM_UPDQRY.append(LM_WHCND);
			if(!insIVTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE MR_IVTRN SET ";
					L_UPDSTR += "IVT_DORNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_DORNO"),"")+"',";
					L_UPDSTR += "IVT_INDNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_INDNO"),"")+"',";
					L_UPDSTR += "IVT_INVNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_INVNO"),"")+"',";
					L_UPDSTR += "IVT_GINNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_GINNO"),"")+"',";
					L_UPDSTR += "IVT_SALTP = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_SALTP"),"")+"',";
					L_UPDSTR += "IVT_CNSCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_CNSCD"),"")+"',";
					L_UPDSTR += "IVT_BYRCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_BYRCD"),"")+"',";
					L_UPDSTR += "IVT_DSRCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_DSRCD"),"")+"',";
				//	L_UPDSTR += "IVT_TRPCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_TRPCD"),"")+"',";
					L_UPDSTR += "IVT_TMOCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_TMOCD"),"")+"',";
					L_UPDSTR += "IVT_CURCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_CURCD"),"")+"',";
					L_UPDSTR += "IVT_LRYNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_LRYNO"),"")+"',";
					L_UPDSTR += "IVT_CNTDS = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_CNTDS"),"")+"',";
					L_UPDSTR += "IVT_LR_NO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_LR_NO"),"")+"',";
					L_UPDSTR += "IVT_CPTVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_CPTVL"),"0")+",";
					L_UPDSTR += "IVT_ASSVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_ASSVL"),"0")+",";
					L_UPDSTR += "IVT_DSCVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_DSCVL"),"0")+",";
					L_UPDSTR += "IVT_PKFVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_PKFVL"),"0")+",";
					L_UPDSTR += "IVT_INSVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_INSVL"),"0")+",";
					L_UPDSTR += "IVT_STXVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_STXVL"),"0")+",";
					L_UPDSTR += "IVT_EXCVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_EXCVL"),"0")+",";
					L_UPDSTR += "IVT_INVVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_INVVL"),"0")+",";
					L_UPDSTR += "IVT_NETVL = "+nvlSTRVL(LM_RSLSRC.getString("IVT_NETVL"),"0")+",";
					L_UPDSTR += "IVT_ECHRT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_ECHRT"),"0")+",";
					L_UPDSTR += "IVT_LADRT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_LADRT"),"0")+",";
					L_UPDSTR += "IVT_EXCRT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_EXCRT"),"0")+",";
					L_UPDSTR += "IVT_INVRT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_INVRT"),"0")+",";
					L_UPDSTR += "IVT_PKGWT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_PKGWT"),"0")+",";
					L_UPDSTR += "IVT_LADQT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_LADQT"),"0")+",";
					L_UPDSTR += "IVT_REQQT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_REQQT"),"0")+",";
					L_UPDSTR += "IVT_INVQT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_INVQT"),"0")+",";
					L_UPDSTR += "IVT_INVPK = "+nvlSTRVL(LM_RSLSRC.getString("IVT_INVPK"),"0")+",";
					L_UPDSTR += "IVT_AUTBY = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_AUTBY"),"")+"',";
					L_UPDSTR += "IVT_DSTDS = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_DSTDS"),"")+"',";
					L_UPDSTR += "IVT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_STSFL"),"")+"',";
					L_UPDSTR += "IVT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_LUSBY"),"")+"',";
					L_UPDSTR += "IVT_DTPCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_DTPCD"),"")+"',";
					L_UPDSTR += "IVT_TSTFL = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_TSTFL"),"")+"',";
					L_UPDSTR += "IVT_SLRFL = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_SLRFL"),"")+"',";
					L_UPDSTR += "IVT_TSLNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_TSLNO"),"")+"',";
					L_UPDSTR += "IVT_RSLNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_RSLNO"),"")+"',";
					L_UPDSTR += "IVT_ADLNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_ADLNO"),"")+"',";
					L_UPDSTR += "IVT_PRDDS = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_PRDDS"),"")+"',";
					L_UPDSTR += "IVT_PRDTP = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_PRDTP"),"")+"',";
					L_UPDSTR += "IVT_LOTRF = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_LOTRF"),"")+"',";
					L_UPDSTR += "IVT_UOMCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_UOMCD"),"")+"',";
					L_UPDSTR += "IVT_PORNO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_PORNO"),"")+"',";
					L_UPDSTR += "IVT_ZONCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_ZONCD"),"")+"',";
					L_UPDSTR += "IVT_PMTTP = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_PMTTP"),"")+"',";
					L_UPDSTR += "IVT_EXCCO = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_EXCCO"),"")+"',";
					L_UPDSTR += "IVT_STXCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_STXCD"),"")+"',";
					L_UPDSTR += "IVT_STXRT = "+nvlSTRVL(LM_RSLSRC.getString("IVT_STXRT"),"0")+",";
					L_UPDSTR += "IVT_OCTCD = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_OCTCD"),"")+"',";
					L_UPDSTR += "IVT_OCTRT = "+ nvlSTRVL(LM_RSLSRC.getString("IVT_OCTRT"),"0")+",";
					/*L_UPDSTR += "'"+LM_RSLSRC.getString("IVT_SVCCD")+"',";
					System.out.println("IVT_SVCCD: "+i++);
					L_UPDSTR += LM_RSLSRC.getString("IVT_SVCRT")+",";
					System.out.println("IVT_SVCRT: "+i++);*/
					L_UPDSTR += "IVT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("IVT_TRNFL"),"")+"',";
					L_UPDSTR += "IVT_LADDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("IVT_LADDT"))) + ",";
					L_UPDSTR += "IVT_LODDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("IVT_LODDT"))) + ",";
					L_UPDSTR += "IVT_ALODT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("IVT_ALODT"))) + ",";
					L_UPDSTR += "IVT_INVDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("IVT_INVDT"))) + ",";
					L_UPDSTR += "IVT_LR_DT = "+setDATE("MDY",LM_RSLSRC.getDate("IVT_LR_DT")) + ",";
					L_UPDSTR += "IVT_PMDDT = "+setDATE("MDY",LM_RSLSRC.getDate("IVT_PMDDT")) + ",";
					L_UPDSTR += "IVT_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("IVT_LUPDT")) + ",";
					L_UPDSTR += "IVT_ADLDT = "+setDATE("MDY",LM_RSLSRC.getDate("IVT_ADLDT")) + ",";
					L_UPDSTR += "IVT_PORDT = "+setDATE("MDY",LM_RSLSRC.getDate("IVT_PORDT")) + ",";
					L_UPDSTR += "IVT_DORDT = "+setDATE("MDY",LM_RSLSRC.getDate("IVT_DORDT"));
					L_UPDSTR += " " + LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_SYSCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updIVTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for CO_IVTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkIVTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkIVTRN","");
		return false;
	}
	return true;
}
	public boolean insIVTRN(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_STLSQL = "Insert Into MR_IVTRN(IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP,IVT_DORNO,IVT_INDNO,";
				LM_STLSQL += "IVT_INVNO,IVT_GINNO,IVT_SALTP,IVT_CNSCD,IVT_BYRCD,IVT_DSRCD,IVT_TMOCD,";
		//	LM_STLSQL += "IVT_INVNO,IVT_GINNO,IVT_SALTP,IVT_CNSCD,IVT_BYRCD,IVT_DSRCD,IVT_TRPCD,IVT_TMOCD,";
			LM_STLSQL += "IVT_CURCD,IVT_LRYNO,IVT_CNTDS,IVT_LR_NO,IVT_CPTVL,IVT_ASSVL,IVT_DSCVL,IVT_PKFVL,";
			LM_STLSQL += "IVT_INSVL,IVT_STXVL,IVT_EXCVL,IVT_INVVL,IVT_NETVL,IVT_ECHRT,IVT_LADRT,IVT_EXCRT,";
			LM_STLSQL += "IVT_INVRT,IVT_PKGWT,IVT_LADQT,IVT_REQQT,IVT_INVQT,IVT_INVPK,IVT_AUTBY,IVT_DSTDS,";
			LM_STLSQL += "IVT_STSFL,IVT_LUSBY,IVT_DTPCD,IVT_TSTFL,IVT_SLRFL,IVT_TSLNO,IVT_RSLNO,IVT_ADLNO,";
			LM_STLSQL += "IVT_PRDDS,IVT_PRDTP,IVT_LOTRF,IVT_UOMCD,IVT_PORNO,IVT_ZONCD,IVT_PMTTP,IVT_EXCCO,";
			LM_STLSQL += "IVT_STXCD,IVT_STXRT,IVT_OCTCD,IVT_OCTRT,IVT_TRNFL,IVT_LADDT,IVT_LODDT,IVT_ALODT,";
			LM_STLSQL += "IVT_INVDT,IVT_LR_DT,IVT_PMDDT,IVT_LUPDT,IVT_ADLDT,IVT_PORDT,IVT_DORDT)";
			LM_STLSQL += " values ("; 
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_MKTTP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_LADNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_PRDCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_PKGTP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_DORNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_INDNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_INVNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_GINNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_SALTP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_CNSCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_BYRCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_DSRCD"),"")+"',";
		//	LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_TRPCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_TMOCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_CURCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_LRYNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_CNTDS"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_LR_NO"),"")+"',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_CPTVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_ASSVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_DSCVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_PKFVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_INSVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_STXVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_EXCVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_INVVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_NETVL"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_ECHRT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_LADRT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_EXCRT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_INVRT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_PKGWT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_LADQT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_REQQT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_INVQT"),"0")+",";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_INVPK"),"0")+",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_AUTBY"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_DSTDS"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_STSFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_LUSBY"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_DTPCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_TSTFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_SLRFL"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_TSLNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_RSLNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_ADLNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_PRDDS"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_PRDTP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_LOTRF"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_UOMCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_PORNO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_ZONCD"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_PMTTP"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_EXCCO"),"")+"',";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_STXCD"),"")+"',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_STXRT"),"0")+",";
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_OCTCD"),"")+"',";
			LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IVT_OCTRT"),"0")+",";
			/*LM_STLSQL += "'"+LP_RSLSET.getString("IVT_SVCCD")+"',";
			System.out.println("IVT_SVCCD: "+i++);
			LM_STLSQL += LP_RSLSET.getString("IVT_SVCRT")+",";
			System.out.println("IVT_SVCRT: "+i++);*/
			LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IVT_TRNFL"),"")+"',";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("IVT_LADDT"))) + ",";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("IVT_LODDT"))) + ",";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("IVT_ALODT"))) + ",";
			LM_STLSQL += setDTMDB2(setDTMFMT(LP_RSLSET.getString("IVT_INVDT"))) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IVT_LR_DT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IVT_PMDDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IVT_LUPDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IVT_ADLDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IVT_PORDT")) + ",";
			LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IVT_DORDT")) + ")";
			//System.out.println(cl_dat.ocl_dat.M_STRSQL);
			return chkUPDCT(LM_STLSQL);
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtIVTRN","");
			return false;
		}

	}
private boolean chkDODEL(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_MKTTP,L_DORNO,L_PRDCD,L_PKGTP,L_DSPDT;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_MKTTP = nvlSTRVL(LM_RSLSRC.getString("DOD_MKTTP"),"");
			L_DORNO = nvlSTRVL(LM_RSLSRC.getString("DOD_DORNO"),"");
			L_PRDCD = nvlSTRVL(LM_RSLSRC.getString("DOD_PRDCD"),"");
			L_PKGTP = nvlSTRVL(LM_RSLSRC.getString("DOD_PKGTP"),"");
			L_DSPDT =  setDATE("MDY",LM_RSLSRC.getDate("DOD_DSPDT"));	
			setMSG("MR_DODEL : "+L_MKTTP +" / "+L_DORNO +" / "+L_PRDCD +" / "+L_PKGTP+" / "+L_DSPDT ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update mr_dodel set dod_trnfl ='1'");
			LM_WHCND.append(" WHERE DOD_MKTTP ='"+L_MKTTP +"'");
			LM_WHCND.append(" AND DOD_DORNO ='"+L_DORNO +"'");
			LM_WHCND.append(" AND DOD_PRDCD ='"+L_PRDCD +"'");
			LM_WHCND.append(" AND DOD_PKGTP ='"+L_PKGTP +"'");
			LM_WHCND.append(" AND DOD_DSPDT ="+L_DSPDT);
			LM_UPDQRY.append(LM_WHCND);
			if(!insDODEL(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE MR_DODEL SET ";
					LM_STLSQL += "DOD_DSPDT = "+setDATE("MDY",LM_RSLSRC.getDate("DOD_DSPDT"))+",";
					LM_STLSQL += "DOD_LADDT = "+setDATE("MDY",LM_RSLSRC.getDate("DOD_LADDT"))+",";
					LM_STLSQL += "DOD_LADQT = "+nvlSTRVL(LM_RSLSRC.getString("DOD_LADQT"),"0")+ ",";
					LM_STLSQL += "DOD_DORQT = "+nvlSTRVL(LM_RSLSRC.getString("DOD_DORQT"),"0")+ ",";
					LM_STLSQL += "DOD_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("DOD_STSFL"),"") + "',";
					LM_STLSQL += "DOD_DELTP = '"+nvlSTRVL(LM_RSLSRC.getString("DOD_DELTP"),"") + "',";
					LM_STLSQL += "DOD_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("DOD_LUSBY"),"") + "',";
					LM_STLSQL += "DOD_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("DOD_LUPDT")) + ",";
					LM_STLSQL += "DOD_LADNO = '"+nvlSTRVL(LM_RSLSRC.getString("DOD_LADNO"),"") + "',";
					LM_STLSQL += "DOD_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("DOD_TRNFL"),"") + "'";
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_SYSCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updDODEL","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MR_DODEL ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkDODEL "+L_EX.toString());
		showEXMSG(L_EX,"chkDODEL","");
		return false;
	}
	return true;
}
private boolean insDODEL(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_STLSQL  = "Insert Into MR_DODEL(DOD_MKTTP,DOD_DORNO,DOD_PRDCD,DOD_DSPDT,DOD_LADDT,DOD_LADQT,DOD_DORQT,DOD_STSFL,DOD_DELTP,DOD_LUSBY,DOD_LUPDT,DOD_LADNO,DOD_PKGTP,DOD_TRNFL)values(";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_MKTTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_DORNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_PRDCD"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("DOD_DSPDT"))+",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("DOD_LADDT"))+",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOD_LADQT"),"0")+ ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOD_DORQT"),"0")+ ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_DELTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("DOD_LUPDT")) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_LADNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_PKGTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOD_TRNFL"),"") + "')";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insRMMST","");
		return false;
	}
}

private boolean chkDOTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_MKTTP,L_DORNO,L_PRDCD,L_PKGTP,L_DSPDT;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_MKTTP = nvlSTRVL(LM_RSLSRC.getString("DOT_MKTTP"),"");
			L_DORNO = nvlSTRVL(LM_RSLSRC.getString("DOT_DORNO"),"");
			L_PRDCD = nvlSTRVL(LM_RSLSRC.getString("DOT_PRDCD"),"");
			L_PKGTP = nvlSTRVL(LM_RSLSRC.getString("DOT_PKGTP"),"");
			setMSG("MR_DOTRN : "+L_MKTTP +" / "+L_DORNO +" / "+L_PRDCD +" / "+L_PKGTP ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update mr_dotrn set dot_trnfl ='1'");
			LM_WHCND.append("   WHERE DOT_MKTTP ='"+L_MKTTP +"'");
			LM_WHCND.append(" AND DOT_DORNO ='"+L_DORNO +"'");
			LM_WHCND.append(" AND DOT_PRDCD ='"+L_PRDCD +"'");
			LM_WHCND.append(" AND DOT_PKGTP ='"+L_PKGTP +"'");
			LM_UPDQRY.append(LM_WHCND);
			if(!insDOTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE MR_DOTRN SET ";
					L_UPDSTR += "DOT_DORDT = "+setDATE("MDY",LM_RSLSRC.getDate("DOT_DORDT"))+",";
					L_UPDSTR += "DOT_AMDNO = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_AMDNO"),"") + "',";
					L_UPDSTR += "DOT_AMDDT = "+setDATE("MDY",LM_RSLSRC.getDate("DOT_AMDDT"))+",";
					L_UPDSTR += "DOT_INDNO = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_INDNO"),"") + "',";
					L_UPDSTR += "DOT_TRSCD = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_TRSCD"),"") + "',";
					L_UPDSTR += "DOT_TMOCD = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_TMOCD"),"") + "',";
					L_UPDSTR += "DOT_DLCCD = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_DLCCD"),"") + "',";
					L_UPDSTR += "DOT_LRYNO = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_LRYNO"),"") + "',";
					L_UPDSTR += "DOT_AUTBY = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_AUTBY"),"") + "',";
					L_UPDSTR += "DOT_AUTDT = " + setDATE("MDY",LM_RSLSRC.getDate("DOT_AUTDT"))+",";
					L_UPDSTR += "DOT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_TRNFL"),"") + "',";
					L_UPDSTR += "DOT_PRDDS = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_PRDDS"),"") + "',";
					L_UPDSTR += "DOT_DORPK = "+nvlSTRVL(LM_RSLSRC.getString("DOT_DORPK"),"0") + ",";
					L_UPDSTR += "DOT_PKGWT = "+nvlSTRVL(LM_RSLSRC.getString("DOT_PKGWT"),"0") + ",";
					L_UPDSTR += "DOT_DORQT = "+nvlSTRVL(LM_RSLSRC.getString("DOT_DORQT"),"0") + ",";
					L_UPDSTR += "DOT_ORDUM = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_ORDUM"),"") + "',";
					L_UPDSTR += "DOT_LOTRF = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_LOTRF"),"") + "',";
					L_UPDSTR += "DOT_LADQT = "+nvlSTRVL(LM_RSLSRC.getString("DOT_LADQT"),"0") + ",";
					L_UPDSTR += "DOT_INVQT = "+nvlSTRVL(LM_RSLSRC.getString("DOT_INVQT"),"0") + ",";
					L_UPDSTR += "DOT_FCMQT = "+nvlSTRVL(LM_RSLSRC.getString("DOT_FCMQT"),"0") + ",";
					L_UPDSTR += "DOT_DELTP = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_DELTP"),"") + "',";
					L_UPDSTR += "DOT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_STSFL"),"") + "',";
					L_UPDSTR += "DOT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_LUSBY"),"") + "',";
					L_UPDSTR += "DOT_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("DOT_LUPDT")) + ",";
					L_UPDSTR += "DOT_TRPCD = '"+nvlSTRVL(LM_RSLSRC.getString("DOT_TRPCD"),"") + "',";
					L_UPDSTR += "DOT_FRTRT = "+nvlSTRVL(LM_RSLSRC.getString("DOT_FRTRT"),"0");
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_SYSCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updDOTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MR_DOTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkDOTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkDOTRN","");
		return false;
	}
	return true;
}
private boolean insDOTRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_STLSQL  = "Insert Into MR_DOTRN(DOT_MKTTP,DOT_DORNO,DOT_DORDT,DOT_AMDNO,DOT_AMDDT,DOT_INDNO,DOT_TRSCD,DOT_TMOCD,DOT_DLCCD,DOT_LRYNO,DOT_AUTBY,DOT_AUTDT,DOT_TRNFL,DOT_PRDCD,DOT_PRDDS,DOT_PKGTP,DOT_DORPK,DOT_PKGWT,DOT_DORQT,DOT_ORDUM,DOT_LOTRF,DOT_LADQT,DOT_INVQT,DOT_FCMQT,DOT_DELTP,DOT_STSFL,DOT_LUSBY,DOT_LUPDT,DOT_TRPCD,DOT_FRTRT)values(";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_MKTTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_DORNO"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("DOT_DORDT"))+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_AMDNO"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("DOT_AMDDT"))+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_INDNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_TRSCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_TMOCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_DLCCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_LRYNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_AUTBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("DOT_AUTDT"))+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_PRDCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_PRDDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_PKGTP"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOT_DORPK"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOT_PKGWT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOT_DORQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_ORDUM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_LOTRF"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOT_LADQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOT_INVQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOT_FCMQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_DELTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("DOT_LUPDT")) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("DOT_TRPCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("DOT_FRTRT"),"0") + ")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insDOTRN","");
		return false;
	}
}
private boolean chkINMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_MKTTP,L_INDNO;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_MKTTP = nvlSTRVL(LM_RSLSRC.getString("IN_MKTTP"),"");
			L_INDNO = nvlSTRVL(LM_RSLSRC.getString("IN_INDNO"),"");
			setMSG("MR_INMST : "+L_MKTTP +" / "+L_INDNO,'N'); 
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE IN_MKTTP ='"+L_MKTTP +"'");
			LM_WHCND.append(" AND IN_INDNO ='"+L_INDNO +"'");
			LM_UPDQRY.append("update mr_inmst set in_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND);
			if(!insINMST(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE MR_INMST SET ";
					L_UPDSTR += "IN_INDDT = "+setDATE("MDY",LM_RSLSRC.getDate("IN_INDDT"))+",";
					L_UPDSTR += "IN_DORNO = '"+nvlSTRVL(LM_RSLSRC.getString("IN_DORNO"),"") + "',";
					L_UPDSTR += "IN_AMDNO = '"+nvlSTRVL(LM_RSLSRC.getString("IN_AMDNO"),"") + "',";
					L_UPDSTR += "IN_AMDDT = " +setDATE("MDY",LM_RSLSRC.getDate("IN_AMDDT"))+",";
					L_UPDSTR += "IN_BKGDT = " +setDATE("MDY",LM_RSLSRC.getDate("IN_BKGDT"))+",";
					L_UPDSTR += "IN_SALTP = '"+nvlSTRVL(LM_RSLSRC.getString("IN_SALTP"),"") + "',";
					L_UPDSTR += "IN_DTPCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_DTPCD"),"") + "',";
					L_UPDSTR += "IN_PORNO = '"+nvlSTRVL(LM_RSLSRC.getString("IN_PORNO"),"") + "',";
					L_UPDSTR += "IN_PORDT = "+setDATE("MDY",LM_RSLSRC.getDate("IN_PORDT"))+",";
					L_UPDSTR += "IN_ZONCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_ZONCD"),"") + "',";
					L_UPDSTR += "IN_CNSCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_CNSCD"),"") + "',";
					L_UPDSTR += "IN_BYRCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_BYRCD"),"") + "',";
					L_UPDSTR += "IN_CURCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_CURCD"),"") + "',";
					L_UPDSTR += "IN_ECHRT = "+nvlSTRVL(LM_RSLSRC.getString("IN_ECHRT"),"") + ",";
					L_UPDSTR += "IN_DSRCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_DSRCD"),"") + "',";
					L_UPDSTR += "IN_FAXNO = '"+nvlSTRVL(LM_RSLSRC.getString("IN_FAXNO"),"") + "',";
					L_UPDSTR += "IN_FAXLC = '"+nvlSTRVL(LM_RSLSRC.getString("IN_FAXLC"),"") + "',";
					L_UPDSTR += "IN_APTVL = "+nvlSTRVL(LM_RSLSRC.getString("IN_APTVL"),"") + ",";
					L_UPDSTR += "IN_CPTVL = "+nvlSTRVL(LM_RSLSRC.getString("IN_CPTVL"),"") + ",";
					L_UPDSTR += "IN_PMTCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_PMTCD"),"") + "',";
					L_UPDSTR += "IN_STXCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_STXCD"),"") + "',";
					L_UPDSTR += "IN_STXRT = "+nvlSTRVL(LM_RSLSRC.getString("IN_STXRT"),"") + ",";
					L_UPDSTR += "IN_OCTCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_OCTCD"),"") + "',";
					L_UPDSTR += "IN_OCTRT = "+nvlSTRVL(LM_RSLSRC.getString("IN_OCTRT"),"") + ",";
					L_UPDSTR += "IN_SVCCD = '"+nvlSTRVL(LM_RSLSRC.getString("IN_SVCCD"),"") + "',";
					L_UPDSTR += "IN_SVCRT = "+nvlSTRVL(LM_RSLSRC.getString("IN_SVCRT"),"") + ",";
					L_UPDSTR += "IN_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("IN_STSFL"),"") + "',";
					L_UPDSTR += "IN_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("IN_LUSBY"),"") + "',";
					L_UPDSTR += "IN_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("IN_LUPDT"))+",";
					L_UPDSTR += "IN_DTPDS = '"+nvlSTRVL(LM_RSLSRC.getString("IN_DTPDS"),"") + "',";
					L_UPDSTR += "IN_PSHFL = '"+nvlSTRVL(LM_RSLSRC.getString("IN_PSHFL"),"") + "',";
					L_UPDSTR += "IN_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("IN_TRNFL"),"") + "'";
					
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
						setMSG("Could not update the record for "+ L_MKTTP + " / "+L_INDNO,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updINMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MR_INMST ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkINMST "+L_EX.toString());
		showEXMSG(L_EX,"chkINMST","");
		return false;
	}
	return true;
}
private boolean insINMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_STLSQL  = "Insert Into MR_INMST(IN_MKTTP,IN_INDNO,IN_INDDT,IN_DORNO,IN_AMDNO,IN_AMDDT,IN_BKGDT,IN_SALTP,IN_DTPCD,IN_PORNO,IN_PORDT,IN_ZONCD,IN_CNSCD,IN_BYRCD,IN_CURCD,IN_ECHRT,IN_DSRCD,IN_FAXNO,IN_FAXLC,IN_APTVL,IN_CPTVL,IN_PMTCD,IN_STXCD,IN_STXRT,IN_OCTCD,IN_OCTRT,IN_SVCCD,IN_SVCRT,IN_STSFL,IN_LUSBY,IN_LUPDT,IN_DTPDS,IN_PSHFL,IN_TRNFL)values(";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_MKTTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_INDNO"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IN_INDDT"))+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_DORNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_AMDNO"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IN_AMDDT"))+",";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IN_BKGDT"))+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_SALTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_DTPCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_PORNO"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IN_PORDT"))+",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_ZONCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_CNSCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_BYRCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_CURCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IN_ECHRT"),"") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_DSRCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_FAXNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_FAXLC"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IN_APTVL"),"") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IN_CPTVL"),"") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_PMTCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_STXCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IN_STXRT"),"") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_OCTCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IN_OCTRT"),"") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_SVCCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("IN_SVCRT"),"") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("IN_LUPDT")) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_DTPDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_PSHFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("IN_TRNFL"),"") + "')";
		
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insINMST","");
		return false;
	}
}

private boolean chkMRRMM(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_MKTTP,L_TRNTP,L_DOCNO;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_MKTTP = nvlSTRVL(LM_RSLSRC.getString("RM_MKTTP"),"");
			L_TRNTP = nvlSTRVL(LM_RSLSRC.getString("RM_TRNTP"),"");
			L_DOCNO = nvlSTRVL(LM_RSLSRC.getString("RM_DOCNO"),"");
			setMSG("MR_RMMST : "+L_MKTTP +" / "+L_TRNTP+" / "+L_DOCNO,'N'); 
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE RM_MKTTP ='"+L_MKTTP +"'");
			LM_WHCND.append(" AND RM_TRNTP ='"+L_TRNTP +"'");
			LM_WHCND.append(" AND RM_DOCNO ='"+L_DOCNO +"'");
			LM_UPDQRY.append("update mr_rmmst set rm_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND);
			if(!insMRRMM(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE MR_RMMST SET ";
					L_UPDSTR += "RM_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("RM_REMDS"),"") + "',";
					L_UPDSTR += "RM_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_STSFL"),"") + "',";
					L_UPDSTR += "RM_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_TRNFL"),"") + "',";
					L_UPDSTR += "RM_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("RM_LUSBY"),"") + "',";
					L_UPDSTR += "RM_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("RM_LUPDT"));
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_SYSCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updRMMST","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MR_RMMST ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkRMMST "+L_EX.toString());
		showEXMSG(L_EX,"chkRMMST","");
		return false;
	}
	return true;
}
private boolean insMRRMM(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_STLSQL  = "Insert Into MR_RMMST(RM_MKTTP,RM_TRNTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)values(";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_MKTTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_TRNTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_DOCNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_REMDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_TRNFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("RM_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("RM_LUPDT")) + ")";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insMRRMM","");
		return false;
	}
}
private boolean chkINTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_MKTTP,L_INDNO,L_PRDCD,L_PKGTP;
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_MKTTP = nvlSTRVL(LM_RSLSRC.getString("INT_MKTTP"),"");
			L_INDNO = nvlSTRVL(LM_RSLSRC.getString("INT_INDNO"),"");
			L_PRDCD = nvlSTRVL(LM_RSLSRC.getString("INT_PRDCD"),"");
			L_PKGTP = nvlSTRVL(LM_RSLSRC.getString("INT_PKGTP"),"");
			setMSG("MR_INTRN : "+L_MKTTP +" / "+L_INDNO+ " / "+L_PRDCD +" / "+L_PKGTP,'N'); 
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE INT_MKTTP ='"+L_MKTTP +"'");
			LM_WHCND.append(" AND INT_INDNO ='"+L_INDNO +"'");
			LM_WHCND.append(" AND INT_PRDCD ='"+L_PRDCD +"'");
			LM_WHCND.append(" AND INT_PKGTP ='"+L_PKGTP +"'");
			LM_UPDQRY.append("update mr_intrn set int_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND);
			if(!insINTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE MR_INTRN SET ";
					L_UPDSTR += "INT_PRDGR = '"+nvlSTRVL(LM_RSLSRC.getString("INT_PRDGR"),"") + "',";
					L_UPDSTR += "INT_PRDDS = '"+nvlSTRVL(LM_RSLSRC.getString("INT_PRDDS"),"") + "',";
					L_UPDSTR += "INT_AMDNO = '"+nvlSTRVL(LM_RSLSRC.getString("INT_AMDNO"),"") + "',";
					L_UPDSTR += "INT_INDPK = "+nvlSTRVL(LM_RSLSRC.getString("INT_INDPK"),"0") + ",";
					L_UPDSTR += "INT_PKGWT = "+nvlSTRVL(LM_RSLSRC.getString("INT_PKGWT"),"0") + ",";
					L_UPDSTR += "INT_REQQT = "+nvlSTRVL(LM_RSLSRC.getString("INT_REQQT"),"0") + ",";
					L_UPDSTR += "INT_ORDUM = '"+nvlSTRVL(LM_RSLSRC.getString("INT_ORDUM"),"") + "',";
					L_UPDSTR += "INT_EUSCD = '"+nvlSTRVL(LM_RSLSRC.getString("INT_EUSCD"),"") + "',";
					L_UPDSTR += "INT_CNVFT = "+nvlSTRVL(LM_RSLSRC.getString("INT_CNVFT"),"0") + ",";
					L_UPDSTR += "INT_INDQT = "+nvlSTRVL(LM_RSLSRC.getString("INT_INDQT"),"0") + ",";
					L_UPDSTR += "INT_BASRT = "+nvlSTRVL(LM_RSLSRC.getString("INT_BASRT"),"0") + ",";
					L_UPDSTR += "INT_RTPVL = "+nvlSTRVL(LM_RSLSRC.getString("INT_RTPVL"),"0") + ",";
					L_UPDSTR += "INT_LOTRF = '"+nvlSTRVL(LM_RSLSRC.getString("INT_LOTRF"),"") + "',";
					L_UPDSTR += "INT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("INT_STSFL"),"") + "',";
					L_UPDSTR += "INT_AUTBY = '"+nvlSTRVL(LM_RSLSRC.getString("INT_AUTBY"),"") + "',";
					L_UPDSTR += "INT_DORQT = " + nvlSTRVL(LM_RSLSRC.getString("INT_DORQT"),"0") + ",";
					L_UPDSTR += "INT_LADQT = "+nvlSTRVL(LM_RSLSRC.getString("INT_LADQT"),"0") + ",";
					L_UPDSTR += "INT_INVQT = "+nvlSTRVL(LM_RSLSRC.getString("INT_INVQT"),"0") + ",";
					L_UPDSTR += "INT_FCMQT = "+nvlSTRVL(LM_RSLSRC.getString("INT_FCMQT"),"0") + ",";
					L_UPDSTR += "INT_EXCRT = "+nvlSTRVL(LM_RSLSRC.getString("INT_EXCRT"),"0") + ",";
					L_UPDSTR += "INT_CDCVL = "+nvlSTRVL(LM_RSLSRC.getString("INT_CDCVL"),"0") + ",";
					L_UPDSTR += "INT_DDCVL = "+nvlSTRVL(LM_RSLSRC.getString("INT_DDCVL"),"0") + ",";
					L_UPDSTR += "INT_TDCVL = "+nvlSTRVL(LM_RSLSRC.getString("INT_TDCVL"),"0") + ",";
					L_UPDSTR += "INT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("INT_LUSBY"),"") + "',";
					L_UPDSTR += "INT_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("INT_LUPDT")) + ",";
					L_UPDSTR += "INT_STDUM = '"+nvlSTRVL(LM_RSLSRC.getString("INT_STDUM"),"") + "',";
					L_UPDSTR += "INT_TDCRF = '"+nvlSTRVL(LM_RSLSRC.getString("INT_TDCRF"),"") + "',";
					L_UPDSTR += "INT_INDDT = "+setDATE("MDY",LM_RSLSRC.getDate("INT_INDDT")) + ",";
					L_UPDSTR += "INT_PINNO = '"+nvlSTRVL(LM_RSLSRC.getString("INT_PINNO"),"") + "',";
					L_UPDSTR += "INT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("INT_TRNFL"),"") + "'";
                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_SYSCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updINTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MR_INTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkINTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkINTRN","");
		return false;
	}
	return true;

}
private boolean insINTRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_STLSQL  = "Insert Into MR_INTRN(INT_MKTTP,INT_INDNO,INT_PRDGR,INT_PRDDS,INT_AMDNO,INT_PKGTP,INT_INDPK,INT_PKGWT,INT_REQQT,INT_ORDUM,INT_EUSCD,INT_CNVFT,INT_INDQT,INT_BASRT,INT_RTPVL,INT_LOTRF,INT_STSFL,INT_AUTBY,INT_DORQT,INT_LADQT,INT_INVQT,INT_FCMQT,INT_EXCRT,INT_CDCVL,INT_DDCVL,INT_TDCVL,INT_LUSBY,INT_LUPDT,INT_PRDCD,INT_STDUM,INT_TDCRF,INT_INDDT,INT_PINNO,INT_TRNFL)values(";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_MKTTP"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_INDNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_PRDGR"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_PRDDS"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_AMDNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_PKGTP"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_INDPK"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_PKGWT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_REQQT"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_ORDUM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_EUSCD"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_CNVFT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_INDQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_BASRT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_RTPVL"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_LOTRF"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_STSFL"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_AUTBY"),"") + "',";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_DORQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_LADQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_INVQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_FCMQT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_EXCRT"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_CDCVL"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_DDCVL"),"0") + ",";
		LM_STLSQL += nvlSTRVL(LP_RSLSET.getString("INT_TDCVL"),"0") + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_LUSBY"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("INT_LUPDT")) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_PRDCD"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_STDUM"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_TDCRF"),"") + "',";
		LM_STLSQL += setDATE("MDY",LP_RSLSET.getDate("INT_INDDT")) + ",";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_PINNO"),"") + "',";
		LM_STLSQL += "'"+nvlSTRVL(LP_RSLSET.getString("INT_TRNFL"),"") + "')";
		return chkUPDCT(LM_STLSQL);
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insINTRN","");
		return false;
	}
}

private boolean chkWBTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDSTR="";
	String L_DOCTP="",L_DOCNO="",L_SRLNO="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_DOCTP = nvlSTRVL(LM_RSLSRC.getString("WB_DOCTP"),"");
			L_DOCNO = nvlSTRVL(LM_RSLSRC.getString("WB_DOCNO"),"");
			L_SRLNO = nvlSTRVL(LM_RSLSRC.getString("WB_SRLNO"),"");
			setMSG("MM_WBTRN : "+L_DOCTP +" / "+L_DOCNO+ " / "+L_SRLNO,'N'); 
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_WHCND.append(" WHERE WB_DOCTP ='"+L_DOCTP +"'");
			LM_WHCND.append(" AND WB_DOCNO ='"+L_DOCNO +"'");
			LM_WHCND.append(" AND WB_SRLNO ='"+L_SRLNO +"'");
			LM_UPDQRY.append("update mm_wbtrn set wb_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND);
			if(!insWBTRN(LM_RSLSRC,"SP"))
			{
				try
				{
					L_UPDSTR ="UPDATE MM_WBTRN SET ";
					L_UPDSTR += "WB_WBRNO = '" + nvlSTRVL(LM_RSLSRC.getString("WB_WBRNO"),"") + "'," +
				 "WB_LRYNO = '" + nvlSTRVL(LM_RSLSRC.getString("WB_LRYNO"),"") + "'," +
				 "WB_TPRCD = '" + nvlSTRVL(LM_RSLSRC.getString("WB_TPRCD"),"") + "'," +
				 "WB_TPRDS = '" + nvlSTRVL(LM_RSLSRC.getString("WB_TPRDS"),"") + "'," +
				 "WB_DRVCD = '" + nvlSTRVL(LM_RSLSRC.getString("WB_DRVCD"),"") + "'," +
				 "WB_DRVNM = '" + nvlSTRVL(LM_RSLSRC.getString("WB_DRVNM"),"") + "'," +
				 "WB_MATCD = '" + nvlSTRVL(LM_RSLSRC.getString("WB_MATCD"),"") + "'," +
				 "WB_BOENO = '" + nvlSTRVL(LM_RSLSRC.getString("WB_BOENO"),"") + "'," +
				 "WB_CHLNO = '" + nvlSTRVL(LM_RSLSRC.getString("WB_CHLNO"),"") + "'," +
				 "WB_CHLDT = "+setDATE("MDY",LM_RSLSRC.getDate("WB_CHLDT")) + "," +
				 "WB_CHLQT = "+nvlSTRVL(LM_RSLSRC.getString("WB_CHLQT"),"0") + "," +
				 "WB_GRSWT = "+nvlSTRVL(LM_RSLSRC.getString("WB_GRSWT"),"0") + "," +
				 "WB_TARWT = "+nvlSTRVL(LM_RSLSRC.getString("WB_TARWT"),"0") + "," +
				 "WB_NETWT = "+nvlSTRVL(LM_RSLSRC.getString("WB_NETWT"),"0") + "," +
				 "WB_INCTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("WB_INCTM"))) + "," +
				 "WB_OUTTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("WB_OUTTM"))) + "," +
				 "WB_STRTP = '" + nvlSTRVL(LM_RSLSRC.getString("WB_STRTP"),"") + "'," +
				 "WB_DOCRF = '" + nvlSTRVL(LM_RSLSRC.getString("WB_DOCRF"),"") + "'," +
				 "WB_STSFL = '" + nvlSTRVL(LM_RSLSRC.getString("WB_STSFL"),"") + "'," +
				 "WB_ORDRF = '" + nvlSTRVL(LM_RSLSRC.getString("WB_ORDRF"),"") + "'," +
				 "WB_QLLRF = '" + nvlSTRVL(LM_RSLSRC.getString("WB_QLLRF"),"") + "'," +
				 "WB_QNLRF = '" + nvlSTRVL(LM_RSLSRC.getString("WB_QNLRF"),"") + "'," +
				 "WB_DEFFL = '" + nvlSTRVL(LM_RSLSRC.getString("WB_DEFFL"),"") + "'," +
				 "WB_DEFDS = '" + nvlSTRVL(LM_RSLSRC.getString("WB_DEFDS"),"") + "'," +
				 "WB_GINDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("WB_GINDT"))) + "," +
				 "WB_LOCCD = '" + nvlSTRVL(LM_RSLSRC.getString("WB_LOCCD"),"") + "'," +
				 "WB_LUSBY = '" + nvlSTRVL(LM_RSLSRC.getString("WB_LUSBY"),"") + "'," +
				 "WB_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("WB_LUPDT")) + "," +
				 "WB_GINBY = '" + nvlSTRVL(LM_RSLSRC.getString("WB_GINBY"),"") + "'," +
				 "WB_GOTBY = '" + nvlSTRVL(LM_RSLSRC.getString("WB_GOTBY"),"") + "'," +
				 "WB_GOTDT = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("WB_GOTDT"))) + "," +
				 "WB_REMGT = '" + nvlSTRVL(LM_RSLSRC.getString("WB_REMGT"),"") + "'," +
				 "WB_MATTP = '" + nvlSTRVL(LM_RSLSRC.getString("WB_MATTP"),"") + "'," +
				 "WB_WINBY = '" + nvlSTRVL(LM_RSLSRC.getString("WB_WINBY"),"") + "'," +
				 "WB_WOTBY = '" + nvlSTRVL(LM_RSLSRC.getString("WB_WOTBY"),"") + "'," +
				 "WB_REMWB = '" + nvlSTRVL(LM_RSLSRC.getString("WB_REMWB"),"") + "'," +
				 "WB_PRTCD = '" + nvlSTRVL(LM_RSLSRC.getString("WB_PRTCD"),"") + "'," +
				 "WB_PRTDS = '" + nvlSTRVL(LM_RSLSRC.getString("WB_PRTDS"),"") + "'," +
				 "WB_MATDS = '" + nvlSTRVL(LM_RSLSRC.getString("WB_MATDS"),"") + "'," +
				 "WB_ORDDT = "+setDATE("MDY",LM_RSLSRC.getDate("WB_ORDDT")) + "," +
				 "WB_TNKNO = '" + nvlSTRVL(LM_RSLSRC.getString("WB_TNKNO"),"") + "'," +
				 "WB_ACPTG = '" + nvlSTRVL(LM_RSLSRC.getString("WB_ACPTG"),"") + "'," +
				 "WB_ACPDT = "+ setDTMDB2(setDTMFMT(LM_RSLSRC.getString("WB_ACPDT"))) + "," +   
				 "WB_CONNO = '" + nvlSTRVL(LM_RSLSRC.getString("WB_CONNO"),"") + "'," +
				 "WB_PORNO = '" + nvlSTRVL(LM_RSLSRC.getString("WB_PORNO"),"") + "'," +
				 "WB_SMPTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("WB_SMPTM"))) + "," +
				 "WB_TSTTM = "+setDTMDB2(setDTMFMT(LM_RSLSRC.getString("WB_TSTTM")));     
				chkUPDCT(L_UPDSTR);

                                        L_UPDSTR += " "+LM_WHCND.toString().trim();
					if(chkUPDCT(L_UPDSTR))
					{
						conDESDB.commit();
					//	setMSG("Updated the record for "+ L_SYSCD,'N');
						LM_UPDFL = true;
					}
					else
					{
						conDESDB.rollback();
					//	setMSG("Could not update the record for "+ L_SYSCD,'E');
					}
						
				}catch(SQLException L_SE){
					showEXMSG(L_SE,"updWBTRN","");
				}
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_WBTRN ",'N');

	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkWBTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkWBTRN","");
		return false;
	}
	return true;

}

private boolean insWBTRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_STLSQL = "Insert into MM_WBTRN(WB_DOCTP,WB_WBRNO,WB_LRYNO,WB_TPRCD," + 
				"WB_TPRDS,WB_DRVCD,WB_DRVNM,WB_MATCD,WB_BOENO,WB_CHLNO,WB_CHLDT," +
				"WB_CHLQT,WB_GRSWT,WB_TARWT,WB_NETWT,WB_INCTM,WB_OUTTM,WB_STRTP," +
				"WB_DOCRF,WB_STSFL,WB_ORDRF,WB_QLLRF,WB_QNLRF,WB_DEFFL,WB_DEFDS," +
				"WB_GINDT,WB_LOCCD,WB_LUSBY,WB_LUPDT,WB_GINBY,WB_GOTBY,WB_GOTDT," +
				"WB_REMGT,WB_MATTP,WB_WINBY,WB_WOTBY,WB_REMWB,WB_PRTCD,WB_PRTDS," +
				"WB_DOCNO,WB_MATDS,WB_ORDDT,WB_SRLNO,WB_TNKNO,WB_ACPTG,WB_ACPDT," +
				"WB_CONNO,WB_PORNO,WB_SMPTM,WB_TSTTM) values(";

		LM_STLSQL += "'" + nvlSTRVL(LP_RSLSET.getString("WB_DOCTP"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_WBRNO"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_LRYNO"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_TPRCD"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_TPRDS"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_DRVCD"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_DRVNM"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_MATCD"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_BOENO"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_CHLNO"),"") + "'," +
				 setDATE("MDY",LP_RSLSET.getDate("WB_CHLDT")) + "," +
				 nvlSTRVL(LP_RSLSET.getString("WB_CHLQT"),"0") + "," +
				 nvlSTRVL(LP_RSLSET.getString("WB_GRSWT"),"0") + "," +
				 nvlSTRVL(LP_RSLSET.getString("WB_TARWT"),"0") + "," +
				 nvlSTRVL(LP_RSLSET.getString("WB_NETWT"),"0") + "," +
				 setDTMDB2(setDTMFMT(LP_RSLSET.getString("WB_INCTM"))) + "," +
				 setDTMDB2(setDTMFMT(LP_RSLSET.getString("WB_OUTTM"))) + "," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_STRTP"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_DOCRF"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_STSFL"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_ORDRF"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_QLLRF"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_QNLRF"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_DEFFL"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_DEFDS"),"") + "'," +
				 setDTMDB2(setDTMFMT(LP_RSLSET.getString("WB_GINDT"))) + "," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_LOCCD"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_LUSBY"),"") + "'," +
				 setDATE("MDY",LP_RSLSET.getDate("WB_LUPDT")) + "," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_GINBY"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_GOTBY"),"") + "'," +
				 setDTMDB2(setDTMFMT(LP_RSLSET.getString("WB_GOTDT"))) + "," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_REMGT"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_MATTP"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_WINBY"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_WOTBY"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_REMWB"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_PRTCD"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_PRTDS"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_PRTDS"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_MATDS"),"") + "'," +
				 setDATE("MDY",LP_RSLSET.getDate("WB_ORDDT")) + "," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_SRLNO"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_TNKNO"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_ACPTG"),"") + "'," +
				  setDTMDB2(setDTMFMT(LP_RSLSET.getString("WB_ACPDT"))) + "," +   
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_CONNO"),"") + "'," +
				 "'" + nvlSTRVL(LP_RSLSET.getString("WB_PORNO"),"") + "'," +
				 setDTMDB2(setDTMFMT(LP_RSLSET.getString("WB_SMPTM"))) + "," +
				 setDTMDB2(setDTMFMT(LP_RSLSET.getString("WB_TSTTM")))+")";     
				return chkUPDCT(LM_STLSQL);
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtWBTRN","");
			return false;
		}
	}
private boolean chkDVMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_DRVCD="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_DRVCD =  nvlSTRVL(LM_RSLSRC.getString("DV_DRVCD"),"");
			setMSG("MM_DVMST : "+L_DRVCD ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update mm_dvmst set dv_trnfl ='1'");
			LM_WHCND.append(" WHERE DV_DRVCD ='"+L_DRVCD +"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insDVMST(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_DVMST SET "); 	
				LM_UPDSTR.append("DV_DRVNM = '"+nvlSTRVL(LM_RSLSRC.getString("DV_DRVNM"),"") + "',");
				LM_UPDSTR.append("DV_TPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("DV_TPRCD"),"") + "',");
				LM_UPDSTR.append("DV_LICNO = '"+nvlSTRVL(LM_RSLSRC.getString("DV_LICNO"),"") + "',");
				LM_UPDSTR.append("DV_LICBY = '"+nvlSTRVL(LM_RSLSRC.getString("DV_LICBY"),"") + "',");
				LM_UPDSTR.append("DV_LVLDT = "+setDATE("MDY",LM_RSLSRC.getDate("DV_LVLDT")) + ",");
				LM_UPDSTR.append("DV_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("DV_STSFL"),"") + "',");
				LM_UPDSTR.append("DV_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("DV_LUSBY"),"") + "',");
				LM_UPDSTR.append("DV_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("DV_LUPDT")) + ",");
				LM_UPDSTR.append("DV_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("DV_TRNFL"),"") + "'");
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_DVMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkDVMST "+L_EX.toString());
		showEXMSG(L_EX,"chkDVMST","");
		return false;
	}
	return true;
}
private boolean insDVMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_DVMST(DV_DRVCD,DV_DRVNM,DV_TPRCD,DV_LICNO,DV_LICBY,DV_LVLDT,DV_STSFL,DV_LUSBY,DV_LUPDT,DV_TRNFL) values(");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_DRVCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_DRVNM"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_TPRCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_LICNO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_LICBY"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("DV_LVLDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_STSFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_LUSBY"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("DV_LUPDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("DV_TRNFL"),"") + "')");
		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insDVMST","");
		return false;
	}
}
private boolean chkLRMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_LRYNO="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_LRYNO =  nvlSTRVL(LM_RSLSRC.getString("LR_LRYNO"),"");
			setMSG("MM_LRMST : "+L_LRYNO ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_UPDSTR.delete(0,LM_UPDSTR.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDQRY.append("update MM_LRMST set LR_TRNFL ='1'");
			LM_WHCND.append(" WHERE LR_LRYNO ='"+L_LRYNO +"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insLRMST(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_LRMST SET "); 	
				LM_UPDSTR.append("LR_TPRCD = '"+nvlSTRVL(LM_RSLSRC.getString("LR_TPRCD"),"") + "',");
				LM_UPDSTR.append("LR_LRYDS = '"+nvlSTRVL(LM_RSLSRC.getString("LR_LRYDS"),"") + "',");
				LM_UPDSTR.append("LR_TRPCT = "+nvlSTRVL(LM_RSLSRC.getString("LR_TRPCT"),"0") + ",");
				LM_UPDSTR.append("LR_DEFCT = "+nvlSTRVL(LM_RSLSRC.getString("LR_DEFCT"),"0") + ",");
				LM_UPDSTR.append("LR_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("LR_STSFL"),"") + "',");
				LM_UPDSTR.append("LR_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("LR_LUSBY"),"") + "',");
				LM_UPDSTR.append("LR_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("LR_LUPDT")) + ",");
				LM_UPDSTR.append("LR_DRVCD = '"+nvlSTRVL(LM_RSLSRC.getString("LR_DRVCD"),"") + "',");
				LM_UPDSTR.append("LR_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("LR_TRNFL"),"") + "'");		
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_LRMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkLRMST "+L_EX.toString());
		showEXMSG(L_EX,"chkLRMST","");
		return false;
	}
	return true;
}
private boolean insLRMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_LRMST(LR_LRYNO,LR_TPRCD,LR_LRYDS,LR_TRPCT,LR_DEFCT,LR_STSFL,LR_LUSBY,LR_LUPDT,LR_DRVCD,LR_TRNFL)values (");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("LR_LRYNO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("LR_TPRCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("LR_LRYDS"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("LR_TRPCT"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("LR_DEFCT"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("LR_STSFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("LR_LUSBY"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("LR_LUPDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("LR_DRVCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("LR_TRNFL"),"") + "')");		
		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insTKCTR","");
		return false;
	}
}

private boolean chkTKMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_TNKNO="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_TNKNO =  nvlSTRVL(LM_RSLSRC.getString("TK_TNKNO"),"");
			setMSG("MM_TKMST : "+L_TNKNO ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDSTR.delete(0,LM_UPDSTR.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_UPDQRY.append("update mm_tkmst set tk_trnfl ='1'");
			LM_WHCND.append(" WHERE TK_TNKNO ='"+L_TNKNO +"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insTKMST(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_TKMST SET ");
				LM_UPDSTR.append("TK_TMPVL = "+nvlSTRVL(LM_RSLSRC.getString("TK_TMPVL"),"0")+ ",");
				LM_UPDSTR.append("TK_MATCD = '"+nvlSTRVL(LM_RSLSRC.getString("TK_MATCD"),"") + "',");
				LM_UPDSTR.append("TK_STKQT =" +nvlSTRVL(LM_RSLSRC.getString("TK_STKQT"),"0")+ ",");
				LM_UPDSTR.append("TK_TNKTP = '"+nvlSTRVL(LM_RSLSRC.getString("TK_TNKTP"),"") + "',");
				LM_UPDSTR.append("TK_DEPCT = "+nvlSTRVL(LM_RSLSRC.getString("TK_DEPCT"),"0")+ ",");
				LM_UPDSTR.append("TK_DEPVL = "+nvlSTRVL(LM_RSLSRC.getString("TK_DEPVL"),"0")+ ",");
				LM_UPDSTR.append("TK_DEPQT = "+nvlSTRVL(LM_RSLSRC.getString("TK_DEPQT"),"0")+ ",");
				LM_UPDSTR.append("TK_MNDVL = "+nvlSTRVL(LM_RSLSRC.getString("TK_MNDVL"),"0")+ ",");
				LM_UPDSTR.append("TK_MXDVL = "+nvlSTRVL(LM_RSLSRC.getString("TK_MXDVL"),"0")+ ",");
				LM_UPDSTR.append("TK_CLBDT = "+setDATE("MDY",LM_RSLSRC.getDate("TK_CLBDT")) + ",");
				LM_UPDSTR.append("TK_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("TK_STSFL"),"") + "',");
				LM_UPDSTR.append("TK_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("TK_LUSBY"),"") + "',");
				LM_UPDSTR.append("TK_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("TK_LUPDT")) + ",");
				LM_UPDSTR.append("TK_MATDS = '"+nvlSTRVL(LM_RSLSRC.getString("TK_MATDS"),"") + "',");
				LM_UPDSTR.append("TK_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("TK_TRNFL"),"") + "'");
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_TKMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkTKMST "+L_EX.toString());
		showEXMSG(L_EX,"chkTKMST","");
		return false;
	}
	return true;
}
private boolean insTKMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_TKMST(TK_TNKNO,TK_TMPVL,TK_MATCD,TK_STKQT,TK_TNKTP,TK_DEPCT,TK_DEPVL,TK_DEPQT,TK_MNDVL,TK_MXDVL,TK_CLBDT,TK_STSFL,TK_LUSBY,TK_LUPDT,TK_MATDS,TK_TRNFL) values (");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TK_TNKNO"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TK_TMPVL"),"0")+ ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TK_MATCD"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TK_STKQT"),"0")+ ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TK_TNKTP"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TK_DEPCT"),"0")+ ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TK_DEPVL"),"0")+ ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TK_DEPQT"),"0")+ ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TK_MNDVL"),"0")+ ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TK_MXDVL"),"0")+ ",");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("TK_CLBDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TK_STSFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TK_LUSBY"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("TK_LUPDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TK_MATDS"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TK_TRNFL"),"") + "')");

		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insTKCTR","");
		return false;
	}
}

private boolean chkTKCTR(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_TNKNO="",L_DEPCT="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_TNKNO =  nvlSTRVL(LM_RSLSRC.getString("TKC_TNKNO"),"");
			L_DEPCT =  nvlSTRVL(LM_RSLSRC.getString("TKC_DEPCT"),"");
			setMSG("MM_TKCTR : "+L_TNKNO +" / "+L_DEPCT ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDSTR.delete(0,LM_UPDSTR.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_WHCND.append(" WHERE TKC_TNKNO ='"+L_TNKNO +"'");
			LM_WHCND.append(" AND TKC_DEPCT ="+L_DEPCT );
			LM_UPDQRY.append("update mm_tkctr set tkc_trnfl ='1'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insTKCTR(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_TKCTR SET "); 	
				LM_UPDSTR.append("TKC_DEPVL = "+nvlSTRVL(LM_RSLSRC.getString("TKC_DEPVL"),"0") + ",");
				LM_UPDSTR.append("TKC_INCVL = "+nvlSTRVL(LM_RSLSRC.getString("TKC_INCVL"),"0") + ",");
				LM_UPDSTR.append("TKC_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("TKC_LUSBY"),"") + "',");
				LM_UPDSTR.append("TKC_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("TKC_LUPDT")) + ",");
				LM_UPDSTR.append("TKC_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("TKC_STSFL"),"") + "',");
				LM_UPDSTR.append("TKC_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("TKC_TRNFL"),"") + "'");
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_TKCTR ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkTKMST "+L_EX.toString());
		showEXMSG(L_EX,"chkTKMST","");
		return false;
	}
	return true;
}
private boolean insTKCTR(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_TKCTR(TKC_TNKNO,TKC_DEPCT,TKC_DEPVL,TKC_INCVL,TKC_LUSBY,TKC_LUPDT,TKC_STSFL,TKC_TRNFL) values (");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TKC_TNKNO"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TKC_DEPCT"),"0")+ ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TKC_DEPVL"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("TKC_INCVL"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TKC_LUSBY"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("TKC_LUPDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TKC_STSFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("TKC_TRNFL"),"") + "')");

		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insTKCTR","");
		return false;
	}
}
private boolean chkMMRMM(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_STRTP="",L_DOCTP="",L_TRNTP="",L_DOCNO="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_STRTP =  nvlSTRVL(LM_RSLSRC.getString("RM_STRTP"),"");
			L_TRNTP =  nvlSTRVL(LM_RSLSRC.getString("RM_TRNTP"),"");
			L_DOCTP =  nvlSTRVL(LM_RSLSRC.getString("RM_DOCTP"),"");
			L_DOCNO =  nvlSTRVL(LM_RSLSRC.getString("RM_DOCNO"),"");
			setMSG("MM_RMMST : "+L_STRTP +" / "+L_TRNTP +" / "+L_DOCTP +" / "+L_DOCNO +" / " ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDSTR.delete(0,LM_UPDSTR.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_UPDQRY.append("update mm_rmmst set rm_trnfl ='1'");
			LM_WHCND.append(" WHERE RM_STRTP ='"+L_STRTP +"'");
			LM_WHCND.append(" AND RM_TRNTP ='"+L_TRNTP +"'");
			LM_WHCND.append(" AND RM_DOCTP ='"+L_DOCTP +"'");
			LM_WHCND.append(" AND RM_DOCNO ='"+L_DOCNO +"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insMMRMM(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_RMMST SET "); 	
				LM_UPDSTR.append("RM_REMDS = '"+nvlSTRVL(LM_RSLSRC.getString("RM_REMDS"),"") + "',");
				LM_UPDSTR.append("RM_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_STSFL"),"") + "',");
				LM_UPDSTR.append("RM_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("RM_TRNFL"),"") + "',");
				LM_UPDSTR.append("RM_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("RM_LUSBY"),"") + "',");
				LM_UPDSTR.append("RM_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("RM_LUPDT")));
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_RMMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkMMRMM "+L_EX.toString());
		showEXMSG(L_EX,"chkMMRMM","");
		return false;
	}
	return true;
}
private boolean insMMRMM(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_RMMST(RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT) values (");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_STRTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_TRNTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_DOCTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_DOCNO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_REMDS"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_STSFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_TRNFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("RM_lusby"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("RM_LUPDT")) + ")");
	
		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insRMMST","");
		return false;
	}
}
private boolean chkBETRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_STRTP="",L_PORNO="",L_CONNO="",L_BOENO="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_STRTP =  nvlSTRVL(LM_RSLSRC.getString("BE_STRTP"),"");
			L_PORNO =  nvlSTRVL(LM_RSLSRC.getString("BE_PORNO"),"");
			L_CONNO =  nvlSTRVL(LM_RSLSRC.getString("BE_CONNO"),"");
			L_BOENO =  nvlSTRVL(LM_RSLSRC.getString("BE_BOENO"),"");
			setMSG("MM_BETRN : "+L_STRTP +" / "+L_PORNO +" / "+L_CONNO +" / "+L_BOENO +" / " ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDSTR.delete(0,LM_UPDSTR.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_UPDQRY.append("update MM_BETRN set BE_TRNFL ='1'");
			LM_WHCND.append(" WHERE BE_STRTP ='"+L_STRTP +"'");
			LM_WHCND.append(" AND BE_PORNO ='"+L_PORNO +"'");
			LM_WHCND.append(" AND BE_CONNO ='"+L_CONNO +"'");
			LM_WHCND.append(" AND BE_BOENO ='"+L_BOENO +"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insBETRN(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_BETRN SET "); 	
				LM_UPDSTR.append("BE_MATCD = '"+nvlSTRVL(LM_RSLSRC.getString("BE_MATCD"),"") + "',");
				LM_UPDSTR.append("BE_UOMCD = '"+nvlSTRVL(LM_RSLSRC.getString("BE_UOMCD"),"") + "',");
				LM_UPDSTR.append("BE_CONDT = "+setDATE("MDY",LM_RSLSRC.getDate("BE_CONDT")) + ",");
				LM_UPDSTR.append("BE_CONQT = "+nvlSTRVL(LM_RSLSRC.getString("BE_CONQT"),"0") + ",");
				LM_UPDSTR.append("BE_CONDS = '"+nvlSTRVL(LM_RSLSRC.getString("BE_CONDS"),"") + "',");
				LM_UPDSTR.append("BE_BOEDT = "+setDATE("MDY",LM_RSLSRC.getDate("BE_BOEDT")) + ",");
				LM_UPDSTR.append("BE_LOCCD = '"+nvlSTRVL(LM_RSLSRC.getString("BE_LOCCD"),"") + "',");
				LM_UPDSTR.append("BE_BOEQT = "+nvlSTRVL(LM_RSLSRC.getString("BE_BOEQT"),"0") + ",");
				LM_UPDSTR.append("BE_ACCVL = "+nvlSTRVL(LM_RSLSRC.getString("BE_ACCVL"),"0") + ",");
				LM_UPDSTR.append("BE_DUTVL = "+nvlSTRVL(LM_RSLSRC.getString("BE_DUTVL"),"0") + ",");
				LM_UPDSTR.append("BE_DUTPR = "+nvlSTRVL(LM_RSLSRC.getString("BE_DUTPR"),"0") + ",");
				LM_UPDSTR.append("BE_BOETP = '"+nvlSTRVL(LM_RSLSRC.getString("BE_BOETP"),"") + "',");
				LM_UPDSTR.append("BE_MATTP = '"+nvlSTRVL(LM_RSLSRC.getString("BE_MATTP"),"") + "',");
				LM_UPDSTR.append("BE_CHLQT = "+nvlSTRVL(LM_RSLSRC.getString("BE_CHLQT"),"0") + ",");
				LM_UPDSTR.append("BE_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("BE_STSFL"),"") + "',");
				LM_UPDSTR.append("BE_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("BE_LUSBY"),"") + "',");
				LM_UPDSTR.append("BE_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("BE_LUPDT")) + ",");
				LM_UPDSTR.append("BE_NETQT = "+nvlSTRVL(LM_RSLSRC.getString("BE_NETQT"),"0") + ",");
				LM_UPDSTR.append("BE_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("BE_TRNFL"),"") + "'");
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_BETRN ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkBETRN "+L_EX.toString());
		showEXMSG(L_EX,"chkBETRN","");
		return false;
	}
	return true;
}
private boolean insBETRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_BETRN(BE_STRTP,BE_PORNO,BE_CONNO,BE_BOENO,BE_MATCD,BE_UOMCD," +
					 "BE_CONDT,BE_CONQT,BE_CONDS,BE_BOEDT,BE_LOCCD,BE_BOEQT,BE_ACCVL,BE_DUTVL,BE_DUTPR," +
					 "BE_BOETP,BE_MATTP,BE_CHLQT,BE_STSFL,BE_LUSBY,BE_LUPDT,BE_NETQT,BE_TRNFL) values (");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_STRTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_PORNO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_CONNO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_BOENO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_MATCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_UOMCD"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("BE_CONDT")) + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("BE_CONQT"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_CONDS"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("BE_BOEDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_LOCCD"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("BE_BOEQT"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("BE_ACCVL"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("BE_DUTVL"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("BE_DUTPR"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_BOETP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_MATTP"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("BE_CHLQT"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_STSFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_LUSBY"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("BE_LUPDT")) + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("BE_NETQT"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("BE_TRNFL"),"") + "')");
	
		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insBETRN","");
		return false;
	}
	
}
private boolean chkPOMST(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_STRTP="",L_PORTP="",L_PORNO="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_STRTP =  nvlSTRVL(LM_RSLSRC.getString("PO_STRTP"),"");
			L_PORTP =  nvlSTRVL(LM_RSLSRC.getString("PO_PORTP"),"");
			L_PORNO =  nvlSTRVL(LM_RSLSRC.getString("PO_PORNO"),"");
			setMSG("MM_POMST : "+L_STRTP +" / "+L_PORTP +" / "+L_PORNO ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDSTR.delete(0,LM_UPDSTR.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_UPDQRY.append("update mm_pomst set po_trnfl ='1'");
			LM_WHCND.append(" WHERE PO_STRTP ='"+L_STRTP +"'");
			LM_WHCND.append(" AND PO_PORTP ='"+L_PORTP +"'");
			LM_WHCND.append(" AND PO_PORNO ='"+L_PORNO +"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPOMST(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_POMST SET "); 	
				LM_UPDSTR.append("PO_PORDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_PORDT")) + ",");
				LM_UPDSTR.append("PO_AMDNO = '"+nvlSTRVL(LM_RSLSRC.getString("PO_AMDNO"),"") + "',");
				LM_UPDSTR.append("PO_AMDDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_AMDDT")) + ",");
				LM_UPDSTR.append("PO_BUYCD = '"+nvlSTRVL(LM_RSLSRC.getString("PO_BUYCD"),"") + "',");
				LM_UPDSTR.append("PO_QTNTP = '"+nvlSTRVL(LM_RSLSRC.getString("PO_QTNTP"),"") + "',");	
				LM_UPDSTR.append("PO_QTNNO = '"+nvlSTRVL(LM_RSLSRC.getString("PO_QTNNO"),"") + "',");	
				LM_UPDSTR.append("PO_EFFDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_EFFDT")) + ",");
				LM_UPDSTR.append("PO_CMPDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_CMPDT")) + ",");
				LM_UPDSTR.append("PO_VENTP = '"+nvlSTRVL(LM_RSLSRC.getString("PO_VENTP"),"") + "',");	
				LM_UPDSTR.append("PO_VENCD = '"+nvlSTRVL(LM_RSLSRC.getString("PO_VENCD"),"") + "',");	
				LM_UPDSTR.append("PO_CURCD = '"+nvlSTRVL(LM_RSLSRC.getString("PO_CURCD"),"") + "',");	
				LM_UPDSTR.append("PO_EXGRT = "+nvlSTRVL(LM_RSLSRC.getString("PO_EXGRT"),"0") + ",");	
				LM_UPDSTR.append("PO_PORVL = "+nvlSTRVL(LM_RSLSRC.getString("PO_PORVL"),"0") + ",");	
				LM_UPDSTR.append("PO_SHRDS = '"+nvlSTRVL(LM_RSLSRC.getString("PO_SHRDS"),"") + "',");	
				LM_UPDSTR.append("PO_PREBY = '"+nvlSTRVL(LM_RSLSRC.getString("PO_PREBY"),"") + "',");	
				LM_UPDSTR.append("PO_PREDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_PREDT")) + ",");
				LM_UPDSTR.append("PO_CHKBY = '"+nvlSTRVL(LM_RSLSRC.getString("PO_CHKBY"),"") + "',");	
				LM_UPDSTR.append("PO_CHKDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_CHKDT")) + ",");
				LM_UPDSTR.append("PO_AUTBY = '"+nvlSTRVL(LM_RSLSRC.getString("PO_AUTBY"),"") + "',");	
				LM_UPDSTR.append("PO_AUTDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_AUTDT")) + ",");
				LM_UPDSTR.append("PO_INSBY = '+"+nvlSTRVL(LM_RSLSRC.getString("PO_INSBY"),"") + "',");	
				LM_UPDSTR.append("PO_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("PO_TRNFL"),"") + "',");	
				LM_UPDSTR.append("PO_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("PO_STSFL"),"") + "',");	
				LM_UPDSTR.append("PO_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("PO_LUSBY"),"") + "',");	
				LM_UPDSTR.append("PO_LUPDT = "+setDATE("MDY",LM_RSLSRC.getDate("PO_LUPDT")));
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_POMST ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkPOMST "+L_EX.toString());
		showEXMSG(L_EX,"chkPOMST","");
		return false;
	}
	return true;
}
private boolean insPOMST(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_POMST(PO_STRTP,PO_PORTP,PO_PORNO,PO_PORDT,PO_AMDNO,PO_AMDDT,PO_BUYCD,PO_QTNTP,PO_QTNNO,"+
					 "PO_EFFDT,PO_CMPDT,PO_VENTP,PO_VENCD,PO_CURCD,PO_EXGRT,PO_PORVL,PO_SHRDS,PO_PREBY,PO_PREDT,PO_CHKBY,"+
					 "PO_CHKDT,PO_AUTBY,PO_AUTDT,PO_INSBY,PO_TRNFL,PO_STSFL,PO_LUSBY,PO_LUPDT) values (");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_STRTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_PORTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_PORNO"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_PORDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_AMDNO"),"") + "',");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_AMDDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_BUYCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_QTNTP"),"") + "',");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_QTNNO"),"") + "',");	
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_EFFDT")) + ",");
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_CMPDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_VENTP"),"") + "',");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_VENCD"),"") + "',");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_CURCD"),"") + "',");	
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("PO_EXGRT"),"0") + ",");	
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("PO_PORVL"),"0") + ",");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_SHRDS"),"") + "',");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_PREBY"),"") + "',");	
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_PREDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_CHKBY"),"") + "',");	
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_CHKDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_AUTBY"),"") + "',");	
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_AUTDT")) + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_INSBY"),"") + "',");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_TRNFL"),"") + "',");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_STSFL"),"") + "',");	
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("PO_LUSBY"),"") + "',");	
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("PO_LUPDT")) + ")");
		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insRMMST","");
		return false;
	}
}
private boolean chkPOTRN(String LP_STRSQL,String LP_ORGCD)
{
	ResultSet L_RSLSET;
	String L_UPDQRY ="";
	String L_STRTP="",L_PORTP="",L_PORNO="",L_MATCD="";
	try
	{
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		while(LM_RSLSRC.next())
		{
			LM_UPDFL = false;
			L_STRTP =  nvlSTRVL(LM_RSLSRC.getString("POT_STRTP"),"");
			L_PORTP =  nvlSTRVL(LM_RSLSRC.getString("POT_PORTP"),"");
			L_PORNO =  nvlSTRVL(LM_RSLSRC.getString("POT_PORNO"),"");
			L_MATCD =  nvlSTRVL(LM_RSLSRC.getString("POT_MATCD"),"");
			setMSG("MM_POTRN : "+L_STRTP +" / "+L_PORTP +" / "+L_PORNO +" / "+L_MATCD ,'N');
			LM_UPDQRY.delete(0,LM_UPDQRY.length());
			LM_WHCND.delete(0,LM_WHCND.length());
			LM_UPDSTR.delete(0,LM_UPDSTR.length());
			LM_INSSTR.delete(0,LM_INSSTR.length());
			LM_UPDQRY.append("update MM_POTRN set poT_trnfl ='1'");
			LM_WHCND.append(" WHERE POT_STRTP ='"+L_STRTP +"'");
			LM_WHCND.append(" AND POT_PORTP ='"+L_PORTP +"'");
			LM_WHCND.append(" AND POT_PORNO ='"+L_PORNO +"'");
			LM_WHCND.append(" AND POT_MATCD ='"+L_MATCD +"'");
			LM_UPDQRY.append(LM_WHCND.toString());
			if(!insPOTRN(LM_RSLSRC,"SP"))
			{
				LM_UPDSTR.append("UPDATE MM_POTRN SET "); 	
				LM_UPDSTR.append("POT_AMDNO = '"+nvlSTRVL(LM_RSLSRC.getString("POT_AMDNO"),"") + "',");
				LM_UPDSTR.append("POT_RMSRL = '"+nvlSTRVL(LM_RSLSRC.getString("POT_RMSRL"),"") + "',");
				LM_UPDSTR.append("POT_INDTP = '"+nvlSTRVL(LM_RSLSRC.getString("POT_INDTP"),"") + "',");
				LM_UPDSTR.append("POT_INDNO = '"+nvlSTRVL(LM_RSLSRC.getString("POT_INDNO"),"") + "',");
				LM_UPDSTR.append("POT_DPTCD = '"+nvlSTRVL(LM_RSLSRC.getString("POT_DPTCD"),"") + "',");
				LM_UPDSTR.append("POT_UOMCD = '"+nvlSTRVL(LM_RSLSRC.getString("POT_UOMCD"),"") + "',");
				LM_UPDSTR.append("POT_UCNVL = "+nvlSTRVL(LM_RSLSRC.getString("POT_UCNVL"),"0") + ",");
				LM_UPDSTR.append("POT_PORRT = "+nvlSTRVL(LM_RSLSRC.getString("POT_PORRT"),"0") + ",");
				LM_UPDSTR.append("POT_PERRT = "+nvlSTRVL(LM_RSLSRC.getString("POT_PERRT"),"0") + ",");
				LM_UPDSTR.append("POT_PORQT = "+nvlSTRVL(LM_RSLSRC.getString("POT_PORQT"),"0") + ",");
				LM_UPDSTR.append("POT_PORVL = "+nvlSTRVL(LM_RSLSRC.getString("POT_PORVL"),"0") + ",");
				LM_UPDSTR.append("POT_ACPQT = "+nvlSTRVL(LM_RSLSRC.getString("POT_ACPQT"),"0") + ",");
				LM_UPDSTR.append("POT_FRCQT = "+nvlSTRVL(LM_RSLSRC.getString("POT_FRCQT"),"0") + ",");
				LM_UPDSTR.append("POT_DELCT = "+nvlSTRVL(LM_RSLSRC.getString("POT_DELCT"),"0") + ",");
				LM_UPDSTR.append("POT_TRNFL = '"+nvlSTRVL(LM_RSLSRC.getString("POT_TRNFL"),"") + "',");
				LM_UPDSTR.append("POT_STSFL = '"+nvlSTRVL(LM_RSLSRC.getString("POT_STSFL"),"") + "',");
				LM_UPDSTR.append("POT_LUSBY = '"+nvlSTRVL(LM_RSLSRC.getString("POT_LUSBY"),"") + "',");	
				LM_UPDSTR.append("POT_LUPDT ="+setDATE("MDY",LM_RSLSRC.getDate("POT_LUPDT")));
				LM_UPDSTR.append(LM_WHCND.toString().trim());
				if(chkUPDCT(LM_UPDSTR.toString()))
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				else
					conDESDB.rollback();
			}
			else
				LM_UPDFL = true;
			if(LM_UPDFL)
			{
				if(exeSQLUPD(LM_UPDQRY.toString(),"01"))
					conSRCDB.commit();
				else
					conSRCDB.rollback();
			}
		}
		setMSG("checking completed for MM_POTRN ",'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkPOTRN "+L_EX.toString());
		showEXMSG(L_EX,"chkPOTRN","");
		return false;
	}
	return true;
}
private boolean insPOTRN(ResultSet LP_RSLSET,String LP_SYSCD)
{
	try
	{
		LM_INSSTR.append("Insert Into MM_POTRN(POT_STRTP,POT_PORTP,POT_PORNO,POT_MATCD,POT_AMDNO,POT_RMSRL,"+
					 "POT_INDTP,POT_INDNO,POT_DPTCD,POT_UOMCD,POT_UCNVL,POT_PORRT,POT_PERRT,POT_PORQT,"+
					 "POT_PORVL,POT_ACPQT,POT_FRCQT,POT_DELCT,POT_TRNFL,POT_STSFL,POT_LUSBY,POT_LUPDT) VALUES(");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_STRTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_PORTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_PORNO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_MATCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_AMDNO"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_RMSRL"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_INDTP"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_INDNO"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_DPTCD"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_UOMCD"),"") + "',");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_UCNVL"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_PORRT"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_PERRT"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_PORQT"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_PORVL"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_ACPQT"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_FRCQT"),"0") + ",");
		LM_INSSTR.append(nvlSTRVL(LP_RSLSET.getString("POT_DELCT"),"0") + ",");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_TRNFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_STSFL"),"") + "',");
		LM_INSSTR.append("'"+nvlSTRVL(LP_RSLSET.getString("POT_LUSBY"),"") + "',");	
		LM_INSSTR.append(setDATE("MDY",LP_RSLSET.getDate("POT_LUPDT")) + ")");
		return chkUPDCT(LM_INSSTR.toString());
			
	}catch(SQLException L_SE){
		showEXMSG(L_SE,"insPOTRN","");
		return false;
	}
}
public void crtINSQRY(ResultSet LP_RSLSRC,ResultSetMetaData LP_RSLMDT,String LP_INSSQL,int LP_COLCNT)
{
	String L_COLTYPE ="";
	String L_MSG ="";
	String L_COLNAME ="";
	try
	{

		LM_INSSQL =LP_INSSQL;
		LM_WHRCND = "";
		for(int j=1;j<=LP_COLCNT;j++)
		{
			if(j !=LP_COLCNT)
				LM_INSSQL += LM_HSCOLNM.get(new Integer(j))+",";
			else
				LM_INSSQL += LM_HSCOLNM.get(new Integer(j))+")values(";
		}
		for(int z=1;z<=LP_COLCNT;z++)
		{
			L_COLTYPE = LM_HSCOLTP.get(new Integer(z)).toString();
			L_COLNAME = LM_HSCOLNM.get(new Integer(z)).toString();
			if(L_COLTYPE.trim().equals("VARCHAR"))
			{
				if(z !=LP_COLCNT)
					LM_INSSQL += "'"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"',";	
				else 
					LM_INSSQL += "'"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"')";	
				if(LM_HSKEYNM.contains(L_COLNAME))
				{	
					if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME +"='"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"' AND ";	
					else
					{
						LM_WHRCND += L_COLNAME +"='"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"'";	
					}
					LM_KEYCNT++;
				}
			}
            else if((L_COLTYPE.trim().equals("DECIMAL"))||(L_COLTYPE.trim().equals("NUMERIC")))
			{
				if(z !=LP_COLCNT)
					LM_INSSQL += nvlSTRVL(LP_RSLSRC.getString(z),"0")+",";	
				else 
					LM_INSSQL += nvlSTRVL(LP_RSLSRC.getString(z),"0")+")";	
				if(LM_HSKEYNM.contains(L_COLNAME))
				{	
					if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME +"="+nvlSTRVL(LP_RSLSRC.getString(z),"0")+" AND ";	
					else
						LM_WHRCND += L_COLNAME +"="+nvlSTRVL(LP_RSLSRC.getString(z),"0");	
					LM_KEYCNT++;
				}
			}
			else if(L_COLTYPE.trim().equals("DATE"))
			{
				if(z !=LP_COLCNT)
					LM_INSSQL += setDATE("MDY",LP_RSLSRC.getDate(z))+",";	
				else 
					LM_INSSQL += setDATE("MDY",LP_RSLSRC.getDate(z))+")";	
				if(LM_HSKEYNM.contains(L_COLNAME))
				{	
						if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME + "="+setDATE("MDY",LP_RSLSRC.getDate(z))+ " AND ";	
					else
						LM_WHRCND += L_COLNAME + "="+setDATE("MDY",LP_RSLSRC.getDate(z));	
					LM_KEYCNT++;
				}
			}
			else if(L_COLTYPE.trim().equals("TIMESTAMP"))
			{
				if(z !=LP_COLCNT)
					LM_INSSQL += setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z)))+",";
				else 
					LM_INSSQL += setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z)))+")";
				if(LM_HSKEYNM.contains(L_COLNAME))
				{	
					if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME + "="+setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z))) +" AND ";
					else
						LM_WHRCND += L_COLNAME + "="+setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z)));
						
					LM_KEYCNT++;
				}
	
			}
		}
		for(int i=1;i<=LM_HSKEYNM.size();i++)
		{
			if(i !=LM_HSKEYNM.size())
				L_MSG += LP_RSLSRC.getString(LM_HSKEYNM.get(new Integer(i)).toString())+" / ";
			else
				L_MSG += LP_RSLSRC.getString(LM_HSKEYNM.get(new Integer(i)).toString());
		}
		setMSG(LM_TBLNM + " : "+ L_MSG,'N');
	}
	catch(Exception L_E)
	{
		showEXMSG(L_E,"crtINSSQL","");
	}
}
public void crtUPDQRY(ResultSet LP_RSLSRC,ResultSetMetaData LP_RSLMDT,String LP_UPDSQL,int LP_COLCNT)
{
	String L_COLTYPE ="";
	String L_COLNAME ="";
	try
	{
		LM_UPDSQL = LP_UPDSQL;
		LM_WHRCND = "";
		for(int z=1;z<=LP_COLCNT;z++)
		{
			L_COLTYPE = LM_HSCOLTP.get(new Integer(z)).toString();
			L_COLNAME = LM_HSCOLNM.get(new Integer(z)).toString();
			if(!LM_HSKEYNM.contains(L_COLNAME))
		    {
				if(L_COLTYPE.trim().equals("VARCHAR"))
				{
					if(z !=LP_COLCNT)
					{
						// if added on 29/09/2003 in all cases
						if((LP_COLCNT-z) == (LM_HSKEYNM.size()-(LM_KEYCNT-1))) // since keycnt is initialised at 1., fix for case where primary key is last column 
							LM_UPDSQL += L_COLNAME +"='"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"'";	
						else
							LM_UPDSQL += L_COLNAME +"='"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"',";	
					}
					else 
						LM_UPDSQL += L_COLNAME +"='"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"'";	
				}
				else if((L_COLTYPE.trim().equals("DECIMAL"))||(L_COLTYPE.trim().equals("NUMERIC")))
				{
					if(z !=LP_COLCNT)
					{
						if((LP_COLCNT-z) == (LM_HSKEYNM.size()-(LM_KEYCNT-1)))
							LM_UPDSQL += L_COLNAME+"="+nvlSTRVL(LP_RSLSRC.getString(z),"0");
						else
							LM_UPDSQL += L_COLNAME+"="+nvlSTRVL(LP_RSLSRC.getString(z),"0")+",";
					}
					else 
						LM_UPDSQL += L_COLNAME+"="+nvlSTRVL(LP_RSLSRC.getString(z),"0");	
				}
				else if(L_COLTYPE.trim().equals("DATE"))
				{
					if(z !=LP_COLCNT)
					{
						if((LP_COLCNT-z) == (LM_HSKEYNM.size()-(LM_KEYCNT-1)))
							LM_UPDSQL += L_COLNAME + "="+setDATE("MDY",LP_RSLSRC.getDate(z));
						else
							LM_UPDSQL += L_COLNAME + "="+setDATE("MDY",LP_RSLSRC.getDate(z))+",";
					}
					else 
						LM_UPDSQL += L_COLNAME + "="+setDATE("MDY",LP_RSLSRC.getDate(z));	
				}
				else if(L_COLTYPE.trim().equals("TIMESTAMP"))
				{
					if(z !=LP_COLCNT)
					{
						if((LP_COLCNT-z) == (LM_HSKEYNM.size()-(LM_KEYCNT-1)))
							LM_UPDSQL += L_COLNAME + "="+setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z)));
						else
							LM_UPDSQL += L_COLNAME + "="+setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z)))+",";
					}
					else 
						LM_UPDSQL += L_COLNAME + "="+setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z)));
	
				}
			}
			else
			{
				if(L_COLTYPE.trim().equals("VARCHAR"))
				{
					if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME +"='"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"' AND ";	
					else
						LM_WHRCND += L_COLNAME +"='"+nvlSTRVL(LP_RSLSRC.getString(z),"")+"'";	
				}
				else if((L_COLTYPE.trim().equals("DECIMAL"))||(L_COLTYPE.trim().equals("NUMERIC")))
				//else if(L_COLTYPE.trim().equals("DECIMAL"))
				{
					if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME+"="+nvlSTRVL(LP_RSLSRC.getString(z),"0")+ " AND ";	
					else
						LM_WHRCND += L_COLNAME+"="+nvlSTRVL(LP_RSLSRC.getString(z),"0");	
				}
				else if(L_COLTYPE.trim().equals("DATE"))
				{
					if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME + "="+setDATE("MDY",LP_RSLSRC.getDate(z))+ " AND ";	
					else
						LM_WHRCND += L_COLNAME + "="+setDATE("MDY",LP_RSLSRC.getDate(z));	
				}
				else if(L_COLTYPE.trim().equals("TIMESTAMP"))
				{
					if(LM_KEYCNT != LM_HSKEYNM.size())
						LM_WHRCND += L_COLNAME + "="+setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z))) +" AND ";
					else
						LM_WHRCND += L_COLNAME + "="+setDTMDB2(setDTMFMT(LP_RSLSRC.getString(z)));
	
				}
				LM_KEYCNT++;
			}
		}
	}
	catch(Exception L_E)
	{
		showEXMSG(L_E,"crtUPDSQL","");
	}
}
//private boolean chkDPTRN(String LP_STRSQL,String LP_ORGCD)
private boolean chkTBLNM(String LP_STRSQL,String LP_ORGCD,String LP_TBLNM,String LP_TRNFLD)
{
	int L_COLCNT =0;
	LM_KEYCNT =1;
	LM_UPDFL = false;
	try
	{
	//	LM_TRNUPD ="UPDATE "+LP_TBLNM.trim()+" SET "+LP_TRNFLD.trim() +" ='1' ";
	//	LM_UPDQRY += // where condition
		if(LP_TRNFLD.trim().length() == 0)
		{
			System.out.println("Transfer Field name not specified in CO_TPMST for table "+LP_TBLNM+" , could not replicate");
			return false;
		}
		if(LM_HSKEYNM.size() == 0)
		{
			System.out.println("Primary key not specified in CO_TPMST for table "+LP_TBLNM+" , could not replicate");
			return false;
		}
		LM_INSSQL = "INSERT INTO "+LP_TBLNM.trim()+"(";
		LM_UPDSQL = "UPDATE "+LP_TBLNM.trim()+" SET ";
		
		if(LM_HSCOLNM !=null)
			LM_HSCOLNM.clear();
		if(LM_HSCOLTP !=null)
			LM_HSCOLTP.clear();
		if(LM_HSKEYTP !=null)
			LM_HSKEYTP.clear();
		
		LM_RSLSRC = exeSQLQRY1(LP_STRSQL,LP_ORGCD);
		if(LM_RSLSRC == null)
			return false;	
		else
		{
			if(LM_RSLSRC.next())
			{
				LM_UPDFL = false;
				LM_KEYCNT =1;
				LM_TRNUPD ="UPDATE "+LP_TBLNM.trim()+" SET "+LP_TRNFLD.trim() +" ='1' ";
				LM_RSLMDT = LM_RSLSRC.getMetaData();
				L_COLCNT = LM_RSLMDT.getColumnCount();
				for(int i=1;i<=L_COLCNT;i++)
				{
						LM_HSCOLNM.put(new Integer(i),LM_RSLMDT.getColumnName(i));
						LM_HSCOLTP.put(new Integer(i),LM_RSLMDT.getColumnTypeName(i));
				}
				crtINSQRY(LM_RSLSRC,LM_RSLMDT,LM_INSSQL,L_COLCNT);	
				if(!chkUPDCT(LM_INSSQL))
				{
					//
					LM_KEYCNT =1;
					//
					crtUPDQRY(LM_RSLSRC,LM_RSLMDT,LM_UPDSQL,L_COLCNT);
					LM_UPDSQL += " WHERE "+LM_WHRCND;
					if(chkUPDCT(LM_UPDSQL))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
				}
				else
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				if(LM_UPDFL)
				{
					LM_TRNUPD += " WHERE "+LM_WHRCND;
					if(exeSQLUPD(LM_TRNUPD.toString(),"01"))
						conSRCDB.commit();
					else
						conSRCDB.rollback();
				}
			}
			while(LM_RSLSRC.next())
			{
				LM_UPDFL = false;
				LM_KEYCNT =1;
				LM_TRNUPD ="UPDATE "+LP_TBLNM.trim()+" SET "+LP_TRNFLD.trim() +" ='1' ";
				LM_INSSQL = "INSERT INTO "+LP_TBLNM.trim()+"(";
				LM_UPDSQL = "UPDATE "+LP_TBLNM.trim()+" SET ";
				crtINSQRY(LM_RSLSRC,LM_RSLMDT,LM_INSSQL,L_COLCNT);	
				if(!chkUPDCT(LM_INSSQL))
				{
					LM_KEYCNT =1;
					crtUPDQRY(LM_RSLSRC,LM_RSLMDT,LM_UPDSQL,L_COLCNT);
					LM_UPDSQL += " WHERE "+LM_WHRCND;
					if(chkUPDCT(LM_UPDSQL))
					{
						conDESDB.commit();
						LM_UPDFL = true;
					}
					else
						conDESDB.rollback();
					
				}
				else
				{
					conDESDB.commit();
					LM_UPDFL = true;
				}
				if(LM_UPDFL)
				{
					LM_TRNUPD += " WHERE "+LM_WHRCND;
					if(exeSQLUPD(LM_TRNUPD.toString(),"01"))
						conSRCDB.commit();
					else
						conSRCDB.rollback();
				}
			}
			
		}
		setMSG("checking completed for "+LP_TBLNM.trim(),'N');
		LM_RSLSRC.close();
		
	}
	catch(Exception L_EX)
	{
		System.out.println("Error from chkTBLNM "+LP_TBLNM.toString()+L_EX.toString());
		showEXMSG(L_EX,"chkTBLNM","");
		return false;
	}
	return true;
}

}
