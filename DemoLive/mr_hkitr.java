/* Program for Indent data transfer
*               D.O.  data transfer
*               Party master data transfer
*               Product master data transfer from DB2 to Foxpro System
*/
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
//import cl_cust;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.util.*;


class mr_hkitr extends cl_pbase
{
										
        private JTextField txtMKTTP;    // Market Type
										/**	(txtDOCTP) Indent Number */
        private JTextField txtDOCDT;    /** (txtDOCTP) Doc.Type*/
        private JTextField txtDOCTP;  	/** (txtDOCNO) Del.Order Number*/
		private JTextField txtDOCNO;	/**(strIN_INDNO) Indent Number*/
        private String  strIN_INDNO;	/**(strDOT_DORNO) Del.Order Number */
		private String	strDOT_DORNO;	/**(strFLSUFX) File Name suffix (wdXXXXXX)*/
        private String  strFLSUFX;		/**(strKEYVL) Key value*/
        private String  strKEYVL;		/**(strFLD01) */
		private String	strFLD01;		/**(strFLD02) */
		private String	strFLD02;		/**(strFXPFL) Foxpro file name, (in which data is to be updated)*/
		private String  strFXPFL;		/**(strFLDTP) */
		private String	strFLDTP;		/**(strINSSTR1) Inserion query string, Table columns*/
        private String  strINSSTR1;		/**(strINSSTR2) Insertion query string, value part*/
		private String	strINSSTR2;		/**(strUPDSTR)  Updation query string*/
		private String	strUPDSTR;		/**(strWHRSTR)  Where string for Updation query*/
		private String	strWHRSTR;		/**(strCNDSTR) Where Condition*/
        private String  strCNDSTR;              /**(strDOCDT)   Document Date.*/
        private String  strDOCDT;               /**(strDOCTP)   Transaction type*/
        private String  strDOCTP;		/**(strDOCNO)   Document No.*/
		private String	strDOCNO;		/**(strREMDS)  Remark */
		private String  strREMDS;		/**(strSTSFL)  Status*/
		private String	strSTSFL;		/**(strLUSBY)  Last user*/
		private String	strLUSBY;		/**(strLUPDT)  Last updation date*/
		private String	strLUPDT;		/**(filWDFILE) WD file Name*/
        private File	filWDFILE;		/**(intKEYSIZE) No.of array elements for Key Field*/
        private int		intKEYSIZE;		/**(intVALSIZE) No. of elements for data fields*/
		private int		intVALSIZE;		/**(intUPDCTR) Updation counter*/
		private int		intUPDCTR;		/**(intRUNCTR) Running counter*/
		private int		intRUNCTR;		/**(strSPDPA)  Path for DataBase directory*/
		private boolean flgSPLHO = true;

		private FileOutputStream O_FOUT;
		private DataOutputStream O_DOUT;
		private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_hkitr.doc"; 
		
		
		private String strSPDPA = flgSPLHO ? "w:\\foxdat\\0304\\marksys" : "f:\\foxdat\\0102\\marksys";  /**(strSPDNA) Database Name */
		private String strSPDNA = "MARKMST";		/**(strEXEDIR)*/
        private ResultSet rstTEMP = null;
        private Statement stmTEMP = null;
        private ResultSet rstTMPRST = null;
        private Vector<String> vtrMKTTP;
        private Vector<String> vtrDOCNO;

		
        private String strMKTTP;		/**(strINDNO) Indent No.*/
		private String strINDNO;		/**(strSBSCD) Sub-system code*/
		private String strSBSCD;		/**(strSALTP) Sale type*/
		private String strSALTP;		/**(strZONCD) Zone*/
		private String strZONCD;		/**(strSALTP1) Sale type New version*/
		private String strSALTP1;		/**(strDSRCD) Distributor code*/
		private String strDSRCD;		/**(strCNSCD) Consignee code*/
		private String strCNSCD;		/**(strZONCD1) Zone code New version*/
		private String strZONCD1;		/**(strCGMTP) Code main group*/
        private String strCGMTP;		/**(strCGSTP) Code sub-group*/
		private String strCGSTP;		/**(strCODCD) Code*/
		private String strCODCD;		/**(strCODDS) Code Description*/
		private String strCODDS;		/**(strSHRDS) Short Description*/
		private String strSHRDS;		/**(strCHP01) Char.para 01*/
		private String strCHP01;		/**(strCHP02) Char.para 02*/
		private String strCHP02;		/**(strNMP01) Numeric para 01*/
		private String strNMP01;		/**(strNMP02) Numeric para 02*/
		private String strNMP02;		/**(strCCSVL) Char.constant*/
		private String strCCSVL;		/**(strNCSVL) Numeric constant*/
		private String strNCSVL;		/**(strPRDCD) Product code*/
        private String strPRDCD;		/**(strPRDDS) Product descr.*/
		private String strPRDDS;		/**(strPKGTP) Package Type*/
		private String strPKGTP;		/**(strCDCVL) Consinee credit discount*/
		private String strCDCVL;		/**(strDDCVL) Distributor credit discount*/
		private String strDDCVL;		/**(strTDCVL) Third party credit discount*/
		private String strTDCVL;		/**(strTDCRF) Third party code*/
		private String strTDCRF;		/**(strEXCRT) Excise rate*/
		private String strEXCRT;		/**(strDORNO) D.O. No.*/
        private String strDORNO;		/**(intPRMST_TOT) Total No. of elements in Prod.Master array*/
		private int intPRMST_TOT = 1;			/**(intAE_PR_PRDCD) Product code element*/
        private int intAE_PR_PRDCD = 0;	/**(intPRMST_TOT) Total No. of elements in Prod.Master array*/

		private int intPRMST1_TOT = 1;			/**(intAE_PR_PRDDS) Product description element*/
        private int intAE_PR_PRDDS = 0;			/**(intDOMST_TOT) No.of elements in DO Master array*/

		private int intDOMST_TOT = 1;			/**(intAE_DO_INDNO) Ind.No. element*/
        private int intAE_DO_INDNO = 0;			/**(intDOTRN_TOT) No. of elements in DOTRN */

		private int intDOTRN_TOT = 1;			/**(intAE_DOT_PKGTP) Pkg.type element*/
        private int intAE_DOT_PKGTP = 0;

		private boolean	flgNUMFLD = false;
		private boolean	flgSETDATA = false;		// This flag is used only for inserting records in MR_DTITR
		private boolean flgDOSTS_DEL;
		private String strDOSTS_NODEL;
		private String strDOCNO_PRV = "";		

        String strDO_PRTYPE;
        String strDO_INDNO;
        String strDO_NO;
        String strDO_SPLINST;
        String strDOT_PRDCD;
        String strDOT_GRADECD;
        String strDOT_PKGTYPE;
		
        String strIN_INDDT;
        String strIN_AMNDNO;
        String strIN_AMNDDT;
        String strIN_PONO;
        String strIN_PODATE;
        String strIN_SALTYPE;
        String strIN_DTPCD;
        String strIN_ZONE;
        String strIN_DISTRIBTR;
        String strIN_CONSIGNEE;
        String strIN_BUYER;
        String strIN_CURRENCY;
        String strIN_EXCHRATE;
        String strIN_PMTTYPE;
        String strIN_PMTCUST;
        String strIN_PMTACCT;
        String strIN_STXCD;
        String strIN_STXRT;
        String strIN_OCTCD;
        String strIN_OCTRT;
        String strIN_SVCCD;
        String strIN_SVCRT;
		
        String strINT_AMNDNO;
        String strINT_CNVFACT;
        String strINT_BASRATE;
        String strINT_RATEPER;
        String strINT_EXCDUTY;
												/**(intCDTRN_TOT) Code Transaction Table Elements*/
		private int intCDTRN_TOT = 8;			/**(intAE_CMT_CODDS) Code Description element*/
        private int intAE_CMT_CODDS = 0;		/**(intAE_CMT_SHRDS) Short Description element*/
        private int intAE_CMT_SHRDS = 1;		/**(intAE_CMT_CHP01) Char.para 01 element*/
        private int intAE_CMT_CHP01 = 2;		/**(intAE_CMT_CHP02) Char.para 02 Element*/
        private int intAE_CMT_CHP02 = 3;		/**(intAE_CMT_NMP01) Num.para 01 element*/
        private int intAE_CMT_NMP01 = 4;		/**(intAE_CMT_NMP02) Num.para 02 element*/
        private int intAE_CMT_NMP02 = 5;		/**(intAE_CMT_CCSVL) Char.const. element*/
        private int intAE_CMT_CCSVL = 6;		/**(intAE_CMT_NCSVL) Num.const. element*/
        private int intAE_CMT_NCSVL = 7;		/**(btnREWRK) Rework Total */

		private JButton btnREWRK;				/**(btnCHUPD) Updation Summary Display*/
		private JButton btnCHUPD;				/**(btnRUN1) Run button */
		private JButton btnRUN1;				/**(rstL1RSLSET) additional result set*/
        private ResultSet rstL1RSLSET;			/**(rstL3RSLSET) additional result set*/
		private ResultSet rstL3RSLSET;			/**(stmSTBKA) Statement for Foxpro connection*/
		private ResultSet L3_RSLSET;
        private Statement stmSTBKA;				/**(stmSPBKQ) Statement for query*/
		private Statement stmSPBKQ;				/**(stmSPBKQ1)Additional statement provision*/
		private Statement stmSPBKQ1;			/**(conSPBKA) Connection for Foxpro*/
        private Connection conSPBKA;			/**(vtrKEY01) Key field vector for Target Table*/

		private Vector<String> vtrKEY01;		/**(vtrKEY02) Key field vector for Source Table*/
		private Vector<String> vtrKEY02;		/**(vtrKEYTP) Data type vector for Key fields*/
		private Vector<String> vtrKEYTP;		/**(vtrVAL01) Data fields vector for Target Table*/
        private Vector<String> vtrVAL01;		/**(vtrVAL02) Data fields vector for Source Table*/
		private Vector<String> vtrVAL02;		/**(vtrVALTP) Data type vector for Data fields*/
		private Vector<String> vtrVALTP;		/**(htbPRMST)Prduct master Hash Table (Descriptionwise)*/

		private Hashtable<String,String[]> htbPRMST;		/**(htbPRMST1)Prduct master Hash Table(Codewise)*/
		private Hashtable<String,String[]> htbPRMST1;	/**(htbKEYTBL) Hash table for key-fields (not in use)*/
		private Hashtable<String,String> htbKEYTBL;	/**(htbCDTRN) Hash table for Code Transaction details*/
		private Hashtable<String,String[]> htbCDTRN;		/**(htbDOMST) Hash table for D.O.master details*/
		private Hashtable htbDOMST;		/**(htbDOTRN) Hash table for D.O.transaction details*/
		private Hashtable<String,String[]> htbDOTRN;		/**(strSTRSQL_1)SQL query string additional provision 1*/

		private String strSTRSQL_1;		/**(strSTRSQL_2)SQL query string additional provision 2*/
		private String strSTRSQL_2;

		mr_hkitr()
        {
           super(2);
           try
	       {
				strSTRSQL_1 = "";
				cl_dat.M_flgLCUPD_pbst = true;
				//setLBLMOD();
				vtrKEY01  = new Vector<String>();
				vtrKEY02  = new Vector<String>();
				vtrKEYTP  = new Vector<String>();
				vtrVAL01  = new Vector<String>();
				vtrVAL02  = new Vector<String>();
				vtrVALTP  = new Vector<String>();
				htbPRMST  = new Hashtable<String,String[]>();
				htbPRMST1  = new Hashtable<String,String[]>();
				htbKEYTBL = new Hashtable<String,String>();
				htbCDTRN  = new Hashtable<String,String[]>();
				htbDOMST  = new Hashtable();
				htbDOTRN  = new Hashtable<String,String[]>();

				setMatrix(15,8);
				setVGAP(13);
				//add(new JLabel(""),				3,3,1,1,this,'L');
				//add(new JLabel("Doc.Type"),       4,2,1,1,this,'L');
				//add(new JLabel("Doc.No."),      5,2,1,1,this,'L');


				add(new JLabel("MarketType : "), 2,2,1,1,this,'L');
                add(txtMKTTP=new JTextField(),   2,3,1,0.5,this,'L');
				add(new JLabel("Doc.Date :"),    3,2,1,1,this,'L');
                add(txtDOCDT=new JTextField(),   3,3,1,1,this,'L');
				add(new JLabel("Doc.Type : "),  4,2,1,1,this,'L');
				add(txtDOCTP=new JTextField(),  4,3,1,1,this,'L');
				add(new JLabel("Doc.No.  : "),  5,2,1,1,this,'L');
				add(txtDOCNO=new JTextField(),  5,3,1,1,this,'L');
				add(btnRUN1 =new JButton("RUN"),7,3,1,1,this,'L');
				add(btnCHUPD =new JButton("VERIFY"),10,6,1,1,this,'L');
				add(btnREWRK =new JButton("REWORK"),10,7,1,1,this,'L');
				this.validate();
				txtMKTTP.setText("01");
				txtMKTTP.setEnabled(false);
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));
				M_calLOCAL.add(Calendar.DATE,-1);
				txtDOCDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				txtDOCTP.setText("IN");
				
			}
            catch(Exception L_EX)
            {
               setMSG(L_EX,"in Child.constructor");
			}	
        }

/*		mr_hkitr(int P_intTEMP)
        {
           super();
           try
	       {
				strSTRSQL_1 = "";
				cl_dat.M_flgLCUPD_pbst = true;
				//setLBLMOD();
				vtrKEY01  = new Vector();
				vtrKEY02  = new Vector();
				vtrKEYTP  = new Vector();
				vtrVAL01  = new Vector();
				vtrVAL02  = new Vector();
				vtrVALTP  = new Vector();
				htbPRMST  = new Hashtable();
				htbPRMST1  = new Hashtable();
				htbKEYTBL = new Hashtable();
				htbCDTRN  = new Hashtable();
				htbDOMST  = new Hashtable();
				htbDOTRN  = new Hashtable();

				setMatrix(15,8);
				setVGAP(13);
				//add(new JLabel(""),				3,3,1,1,this,'L');
				//add(new JLabel("Doc.Type"),       4,2,1,1,this,'L');
				//add(new JLabel("Doc.No."),      5,2,1,1,this,'L');


				add(new JLabel("MarketType : "), 2,2,1,1,this,'L');
                add(txtMKTTP=new JTextField(),   2,3,1,0.5,this,'L');
				add(new JLabel("Doc.Date :"),    3,2,1,1,this,'L');
                add(txtDOCDT=new JTextField(),   3,3,1,1,this,'L');
				add(new JLabel("Doc.Type : "),  4,2,1,1,this,'L');
				add(txtDOCTP=new JTextField(),  4,3,1,1,this,'L');
				add(new JLabel("Doc.No.  : "),  5,2,1,1,this,'L');
				add(txtDOCNO=new JTextField(),  5,3,1,1,this,'L');
				add(btnRUN1 =new JButton("RUN"),7,3,1,1,this,'L');
				txtMKTTP.setText("01");


				this.validate();
			}
            catch(Exception L_EX)
            {
               setMSG(L_EX,"in Child.constructor");
			}	
        }

*/		
		
        private void exeINTVAR()
        {
                strINDNO = "";
                strDORNO = "";
        }



/** Deleting Single Quotes(') from a specified string  (for Saving to Database)
*/
      private String delQuote(String LP_STRVL)
        {
        String L_STRVL = LP_STRVL;
        String L_RETSTR="";
        StringTokenizer L_STRTKN;
        try
          {
            if(LP_STRVL==null)
               return L_STRVL;
            else if (LP_STRVL.length()==0)
               return L_STRVL;
            int L_STRLEN = LP_STRVL.length();
            int L_QOTLCN = 0;
            L_RETSTR = "";
            L_STRTKN = new StringTokenizer(L_STRVL,"'");
            while(L_STRTKN.hasMoreTokens())
            {
                 L_RETSTR +=  L_STRTKN.nextToken();
            }         
          }
          catch(Exception ex)
          {
			  setMSG(ex,"in delQuote");
          }
          //System.out.println("Original : "+L_STRVL);
          //System.out.println("Modified : "+L_RETSTR);
          return(L_RETSTR);
        }



	/** Action performed event
	 */
	public void actionPerformed(ActionEvent L_AE)
    {
        super.actionPerformed(L_AE);
		try
		{
			strDOCDT = txtDOCDT.getText();
			strDOCTP = txtDOCTP.getText();
			strDOCNO = txtDOCNO.getText();
			if(L_AE.getSource().equals(btnRUN1))
			{
					if(flgSETDATA)
			            insREFTBL();
					else
					{
						O_FOUT = crtFILE(strRESSTR);
						O_DOUT = crtDTOUTSTR(O_FOUT);	
						setFOXDATA();

						exeINTVAR();

						crtPRMST();
						crtPRMST1();
						crtCDTRN();
						if (strDOCTP.equalsIgnoreCase("IN") || strDOCTP.equalsIgnoreCase("DO"))
						{
							strMKTTP = "04";
							updDATAFL0(strDOCDT,strDOCTP,strMKTTP,strDOCNO);
							strMKTTP = "05";
							updDATAFL0(strDOCDT,strDOCTP,strMKTTP,strDOCNO);
						}
						strMKTTP = "01";
						updDATAFL0(strDOCDT,strDOCTP,strMKTTP,strDOCNO);
						if(strDOCTP.equalsIgnoreCase("IN"))
						{
							strDOCTP = "PR"; txtDOCTP.setText("PR");
							updDATAFL0(strDOCDT,strDOCTP,"01",strDOCNO);
							strDOCTP = "PT"; txtDOCTP.setText("PT");
							setFOXDATA();
							updDATAFL0(strDOCDT,strDOCTP,"01",strDOCNO);
							strDOCTP = "IN"; txtDOCTP.setText("IN");
							setFOXDATA();
						}
					}
					O_DOUT.close();
					O_FOUT.close();
					
					
			    //JOptionPane.showMessageDialog(this," Updation Completed ...","Data Transfer",JOptionPane.INFORMATION_MESSAGE);
			    setMSG(" Updation Completed ...",'N');
			}
			if(L_AE.getSource().equals(btnREWRK))
				{setFOXDATA();setDORQT(); setINVQT();JOptionPane.showMessageDialog(this," Reworking Completed ...","Message",JOptionPane.INFORMATION_MESSAGE);}
			if(L_AE.getSource().equals(btnCHUPD))
			{
				setFOXDATA();
				if(txtDOCTP.getText().equals("DO"))
				{
					chkDOUPD("01");
					chkDOUPD("04");
					chkDOUPD("05");
					setMSG("",'N');
				}
				if(txtDOCTP.getText().equals("IN"))
				{
					chkIVIND();
					chkIVDOR();
					setMSG("",'N');
				}
			}
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"actionPerformed");
        }
    }

	
/** setting connection document for Foxpro Database
 */
	void setFOXDATA()
	{
		try
		{
			strSPDPA = flgSPLHO ? "w:\\foxdat\\0304\\marksys" : "f:\\foxdat\\0102\\marksys";
			strSPDNA = "MARKMST";
			if (strDOCTP.equalsIgnoreCase("PT"))
			{
					strSPDPA = flgSPLHO ? "w:\\foxdat\\0304\\masters" : "f:\\foxdat\\0102\\masters";
			        strSPDNA = "SPLMAST";
			}
            setCONFTB1(strSPDNA, strSPDPA);
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"setFOXDATA");
        }
	}
	
	
	
	/** Updating DOT_DORQT (D.O. qty from D.O. transaction) to INT_DORQT (Indent Transaction)
	 *  Wherever mismatches are found.
	 */
	void setDORQT()
	{
			String L_strSQLQRY = "";
			String L_strUPDQRY = "";
			String L_strPRTYPE = "01";
			String L_strINDNO   = "";
			String L_strGRADECD = "";
			String L_strAUTHQTY = "0.00";
			String L_strDOQTY   = "0.00";
			String L_strDORQT   = "0.00";
			
            try
            {
				//System.out.println("001");
				stmSTBKA.executeUpdate("update mr_intrn set int_invqty=0 where isnull(int_invqty)");
				//System.out.println("002");
				stmSTBKA.executeUpdate("update mr_intrn set int_ladqty=0 where isnull(int_ladqty)");
				//System.out.println("003");
				stmSTBKA.executeUpdate("update mr_dotrn set dot_invqty=0 where isnull(dot_invqty)");
				//System.out.println("004");
				stmSTBKA.executeUpdate("update mr_dotrn set dot_ladqty=0 where isnull(dot_ladqty)");
				//System.out.println("005");
				conSPBKA.commit();
                L_strSQLQRY = "SELECT INT_INDNO,INT_GRADECD,INT_AUTHQTY,INT_DOQTY,SUM(DOT_QTY) DOT_QTY FROM MR_INTRN,MR_DOMST,MR_DOTRN "
							 +" WHERE INT_PRTYPE = '"+L_strPRTYPE+"' and INT_PRTYPE = DO_PRTYPE AND INT_INDNO = DO_INDNO and  DO_PRTYPE=DOT_PRTYPE AND "
							 +" DO_NO = DOT_DONO and INT_GRADECD=DOT_GRADECD and INT_AUTHQTY>0  and DOT_QTY>0 "
							 +" GROUP BY INT_INDNO,INT_GRADECD,INT_AUTHQTY,INT_DOQTY HAVING INT_DOQTY <> SUM(DOT_QTY)";
                //System.out.println(L_strSQLQRY);
                ResultSet L_rstRSSET = stmSPBKQ.executeQuery(L_strSQLQRY);
	            if(L_rstRSSET==null)
					return;
	            if(!L_rstRSSET.next())
					return;
				while(true)
				{
					L_strINDNO   = getRSTVAL1(L_rstRSSET,"INT_INDNO","C");
					L_strGRADECD = getRSTVAL1(L_rstRSSET,"INT_GRADECD","C");
					L_strAUTHQTY = getRSTVAL1(L_rstRSSET,"INT_AUTHQTY","N");
					L_strDOQTY   = getRSTVAL1(L_rstRSSET,"DOT_QTY","N");
					L_strDORQT   = getDORQT(L_strPRTYPE,L_strINDNO,L_strGRADECD);

					L_strUPDQRY = "update mr_intrn set int_doqty = "+L_strDORQT+" where int_prtype = '"+L_strPRTYPE+"' and "
					             +" int_indno = '"+L_strINDNO+"' and int_gradecd = '"+L_strGRADECD+"'  and "
					             +" int_authqty = "+L_strAUTHQTY+"  and int_authqty >= "+L_strDORQT;
					//System.out.println(L_strUPDQRY);
					stmSTBKA.executeUpdate(L_strUPDQRY);
					conSPBKA.commit();
					if(!L_rstRSSET.next())
						break;
				}
				L_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
				   System.out.println(L_strUPDQRY);
                   setMSG(L_EX,"setDORQT");
            }
	}

	
	/** Fetching sum of DOT_DORQT from MR_DOTRN
	 * for specified record of Indent Transaction
	 */
	private String getDORQT(String LP_PRTYPE,String LP_INDNO,String LP_GRADECD)
	{
		String L_RETSTR = "0.00";
		try
		{
			String LP_PRDCD = getPRMST(LP_GRADECD);
			String L_strSQLQRY = "select sum(dot_dorqt) dot_dorqt from mr_dotrn where dot_mkttp= '"+LP_PRTYPE+"' and dot_indno='"+LP_INDNO+"' and dot_prdds='"+LP_GRADECD+"' and dot_stsfl<>'X' and dot_dorqt>0";
			//System.out.println(LP_PRDCD);
			//System.out.println(L_strSQLQRY);
			ResultSet L_rstTEMP = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstTEMP.next() && L_rstTEMP != null)
			{
				L_RETSTR = getRSTVAL(L_rstTEMP,"DOT_DORQT","N");
				L_rstTEMP.close();
			}
		}
        catch(Exception L_EX)
        {
               setMSG(L_EX,"getDORQT");
        }
		return L_RETSTR;
	}
	

	
	/** Updating IVT_INVQT (Inv.Qty from Invoice transaction) to INT_INVQT (Indent Transaction)
	 *  Whereever mismatches are found.
	 */
	void setINVQT()
	{
			String L_strSQLQRY = "";
			String L_strUPDQRY = "";
			String L_strPRTYPE = "01";
			String L_strINDNO   = "";
			String L_strGRADECD = "";
			String L_strAUTHQTY = "0.00";
			String L_strINVQTY   = "0.00";
			String L_strINVQT   = "0.00";
			
            try
            {
				//System.out.println("001");
				stmSTBKA.executeUpdate("update mr_intrn set int_invqty=0 where isnull(int_invqty)");
				//System.out.println("002");
				stmSTBKA.executeUpdate("update mr_intrn set int_ladqty=0 where isnull(int_ladqty)");
				//System.out.println("003");
				stmSTBKA.executeUpdate("update mr_dotrn set dot_invqty=0 where isnull(dot_invqty)");
				//System.out.println("004");
				stmSTBKA.executeUpdate("update mr_dotrn set dot_ladqty=0 where isnull(dot_ladqty)");
				//System.out.println("005");
				conSPBKA.commit();
                L_strSQLQRY = "SELECT INT_INDNO,INT_GRADECD,INT_AUTHQTY,INT_INVQTY,SUM(IVT_QTY) IVT_QTY FROM MR_INTRN,MR_IVMST,MR_IVTRN "
							 +" WHERE INT_PRTYPE = '"+L_strPRTYPE+"' and INT_PRTYPE = IV_PRTYPE AND INT_INDNO = IV_INDNO and  IV_PRTYPE=IVT_PRTYPE AND "
							 +" IV_INVNO = IVT_INVNO and INT_GRADECD=IVT_GRADECD and INT_AUTHQTY>0  and IVT_QTY>0 "
							 +" GROUP BY INT_INDNO,INT_GRADECD,INT_AUTHQTY,INT_INVQTY HAVING INT_INVQTY <> SUM(IVT_QTY)";
                //System.out.println(L_strSQLQRY);
                ResultSet L_rstRSSET = stmSPBKQ.executeQuery(L_strSQLQRY);
	            if(L_rstRSSET==null)
					return;
	            if(!L_rstRSSET.next())
					return;
				while(true)
				{
					L_strINDNO   = getRSTVAL1(L_rstRSSET,"INT_INDNO","C");
					L_strGRADECD = getRSTVAL1(L_rstRSSET,"INT_GRADECD","C");
					L_strAUTHQTY = getRSTVAL1(L_rstRSSET,"INT_AUTHQTY","N");
					L_strINVQTY  = getRSTVAL1(L_rstRSSET,"IVT_QTY","N");
					L_strINVQT   = getINVQT(L_strPRTYPE,L_strINDNO,L_strGRADECD);

					L_strUPDQRY = "update mr_intrn set int_invqty = "+L_strINVQT+" where int_prtype = '"+L_strPRTYPE+"' and "
					             +" int_indno = '"+L_strINDNO+"' and int_gradecd = '"+L_strGRADECD+"'  and "
					             +" int_authqty = "+L_strAUTHQTY+"  and int_authqty >= "+L_strINVQT;
					//System.out.println(L_strUPDQRY);
					stmSTBKA.executeUpdate(L_strUPDQRY);
					conSPBKA.commit();
					if(!L_rstRSSET.next())
						break;
				}
				L_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
				   System.out.println(L_strUPDQRY);
                   setMSG(L_EX,"setINVQT");
            }
	}
	
	/** Fetching sum of DOT_DORQT from MR_DOTRN
	 * for specified record of Indent Transaction
	 */
	private String getINVQT(String LP_PRTYPE,String LP_INDNO,String LP_GRADECD)
	{
		String L_RETSTR = "0.00";
		try
		{
			String LP_PRDCD = getPRMST(LP_GRADECD);
			String L_strSQLQRY = "select sum(ivt_invqt) ivt_invqt from mr_ivtrn where ivt_mkttp= '"+LP_PRTYPE+"' and ivt_indno='"+LP_INDNO+"' and ivt_prdds='"+LP_GRADECD+"' and ivt_stsfl<>'X' and ivt_invqt>0";
			//System.out.println(LP_PRDCD);
			//System.out.println(L_strSQLQRY);
			ResultSet L_rstTEMP = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstTEMP.next() && L_rstTEMP != null)
			{
				L_RETSTR = getRSTVAL(L_rstTEMP,"IVT_INVQT","N");
				L_rstTEMP.close();
			}
		}
        catch(Exception L_EX)
        {
               setMSG(L_EX,"getINVQT");
        }
		return L_RETSTR;
	}
	
        /** Data file updation, Main Method
        */
        void updDATAFL0(String LP_DOCDT, String LP_DOCTP, String LP_MKTTP, String LP_DOCNO)
        {
                try
                {
                        exeGC();
						txtMKTTP.setText(LP_MKTTP);
                        String L_STRQRY = "";
                        vtrMKTTP = new Vector<String>();
                        vtrDOCNO = new Vector<String>();
                        vtrMKTTP.clear();
                        vtrDOCNO.clear();

                        //System.out.println(LP_DOCDT+"/"+LP_DOCTP+"/"+LP_MKTTP+"/"+LP_DOCNO);
                        if (!LP_DOCDT.equals(""))
                        {
                            if(LP_DOCTP.toUpperCase().equals("IN"))
                            {
							   strWHRSTR = "in_mkttp = '"+LP_MKTTP+"' and (in_bkgdt='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' or IN_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' or IN_AMDDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' or IN_INDNO IN (SELECT INT_INDNO from MR_INTRN WHERE INT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"') or  IN_INDNO IN (SELECT IVT_INDNO from MR_IVTRN WHERE  date(IVT_INVDT) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"'))";
                               L_STRQRY = "select count(*) from mr_inmst where "+strWHRSTR;
							   //O_DOUT.writeBytes(L_STRQRY+"\n");
                               //System.out.println(L_STRQRY);
							   rstTMPRST = cl_dat.exeSQLQRY(L_STRQRY);
                               if (rstTMPRST== null || !rstTMPRST.next())
								   return;
							   rstTMPRST.close();
								   
                               L_STRQRY = "select distinct IN_MKTTP,IN_INDNO from mr_inmst where "+strWHRSTR+" order by IN_MKTTP,IN_INDNO";
							   O_DOUT.writeBytes(L_STRQRY+"\n");
                               System.out.println(L_STRQRY);
							   rstTMPRST = cl_dat.exeSQLQRY(L_STRQRY);
                               while(rstTMPRST.next())
                               {
                                  vtrMKTTP.addElement(getRSTVAL(rstTMPRST,"IN_MKTTP","C"));
                                  vtrDOCNO.addElement(getRSTVAL(rstTMPRST,"IN_INDNO","C"));
                               }
                               rstTMPRST.close();

                            }
                            else if(LP_DOCTP.toUpperCase().equals("DO"))
                            {
							   strWHRSTR = " dot_mkttp = '"+LP_MKTTP+"' and (dot_dordt='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' or DOT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' or DOT_AMDDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"'  or  DOT_DORNO IN (SELECT IVT_DORNO from MR_IVTRN WHERE  date(IVT_INVDT) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"'))";
                               L_STRQRY = "select count(*) from mr_dotrn where "+strWHRSTR;
							   O_DOUT.writeBytes(L_STRQRY+"\n");
                               System.out.println(L_STRQRY);
							   rstTMPRST = cl_dat.exeSQLQRY(L_STRQRY);
                               if (rstTMPRST== null || !rstTMPRST.next())
								   return;
							   rstTMPRST.close();

							   
							   L_STRQRY = "select distinct DOT_MKTTP,DOT_DORNO from mr_dotrn where "+strWHRSTR+" order by DOT_MKTTP,DOT_DORNO";
                               rstTMPRST = cl_dat.exeSQLQRY(L_STRQRY);
                               while(rstTMPRST.next())
                               {
                                  vtrMKTTP.addElement(getRSTVAL(rstTMPRST,"DOT_MKTTP","C"));
                                  vtrDOCNO.addElement(getRSTVAL(rstTMPRST,"DOT_DORNO","C"));
                                  //System.out.println(getRSTVAL(rstTMPRST,"DOT_MKTTP","C")+"/"+getRSTVAL(rstTMPRST,"DOT_DORNO","C"));
                               }
                               rstTMPRST.close();
                            }
                            else if(LP_DOCTP.toUpperCase().equals("PT"))
                            {
                               L_STRQRY = "select distinct PT_PRTTP,PT_PRTCD from co_ptmst where  PT_LUPDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' order by PT_PRTTP,PT_PRTCD";
                               rstTMPRST = cl_dat.exeSQLQRY(L_STRQRY);
                               while(rstTMPRST.next())
                               {
                                  vtrMKTTP.addElement(getRSTVAL(rstTMPRST,"PT_PRTTP","C"));
                                  vtrDOCNO.addElement(getRSTVAL(rstTMPRST,"PT_PRTCD","C"));
                               }
                               rstTMPRST.close();
                            }
                            else if(LP_DOCTP.toUpperCase().equals("PR"))
                            {
                               //L_STRQRY = "select distinct PR_PRDTP,PR_PRDDS from co_prmst where  PR_LUPDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' order by PR_PRDTP,PR_PRDCD";
                               //L_STRQRY = "select distinct PR_PRDTP,PR_PRDDS from co_prmst where  PR_LUPDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' order by PR_PRDTP,PR_PRDCD";
                               L_STRQRY = "select distinct PR_PRDTP,PR_PRDDS from co_prmst order by PR_PRDTP,PR_PRDDS";
                               rstTMPRST = cl_dat.exeSQLQRY(L_STRQRY);
                               while(rstTMPRST.next())
                               {
                                  vtrMKTTP.addElement(getRSTVAL(rstTMPRST,"PR_PRDTP","C"));
                                  vtrDOCNO.addElement(getRSTVAL(rstTMPRST,"PR_PRDDS","C"));
                               }
                               rstTMPRST.close();
                            }
                            for(int i=0 ; i<vtrDOCNO.size(); i++)
                            {
                                updDATAFL(LP_DOCTP, vtrMKTTP.get(i).toString(), vtrDOCNO.get(i).toString());
                            }
                        }
                        if(!LP_DOCNO.equals(""))
                        {
                            updDATAFL(LP_DOCTP, LP_MKTTP, LP_DOCNO);
                        }
						

				
				}
                //catch(SQLException L_EX)
                catch(Exception L_EX)
                {
                        setMSG(L_EX,"updDATAFL");
                }
        }



		/**
		 */
		private void chkDOUPD(String LP_MKTTP)
		{
			try
			{
				String L_strSQLQRY = "select count(distinct dot_dorno) dot_dorct, sum(dot_dorqt) dot_dorqt from mr_dotrn where dot_dordt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDOCDT))+"' and dot_mkttp = '"+LP_MKTTP+"' and dot_stsfl<>'X'";
				//System.out.println(L_strSQLQRY);
				String L_strDORCT1=" 0 ", L_strDORQT1=" 0 ", L_strDORCT2=" 0 ", L_strDORQT2=" 0 ";
				ResultSet L_rstTEMP = cl_dat.exeSQLQRY(L_strSQLQRY);
				boolean L_flgUPDFL = false;
				if(L_rstTEMP != null)
					if(L_rstTEMP.next())
					{
						L_strDORCT1 = getRSTVAL(L_rstTEMP,"DOT_DORCT","N");
						L_strDORQT1 = getRSTVAL(L_rstTEMP,"DOT_DORQT","N");
						L_rstTEMP.close();
						if(Double.parseDouble(L_strDORCT1)>0)
							L_flgUPDFL = true;
					}
				L_strSQLQRY = "select count(distinct dot_dono) dot_dorct, sum(dot_qty) dot_dorqt from mr_dotrn where dot_dono in (select do_no from mr_domst where do_date = ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDOCDT))+"') and do_prtype='"+LP_MKTTP+"' and do_status<>'X')";
				//System.out.println(L_strSQLQRY);
				L_rstTEMP = stmSPBKQ.executeQuery(L_strSQLQRY);
				if(L_rstTEMP != null)
					if(L_rstTEMP.next())
					{
						L_strDORCT2 = getRSTVAL1(L_rstTEMP,"DOT_DORCT","N");
						L_strDORQT2 = getRSTVAL1(L_rstTEMP,"DOT_DORQT","N");
						L_rstTEMP.close();
						if(Double.parseDouble(L_strDORCT2)>0)
							L_flgUPDFL = true;
					}
				if(L_flgUPDFL==true)
				{
					JOptionPane.showMessageDialog(this," Transaction Summary for "+strDOCDT+" :  Market Type : "+LP_MKTTP+"\n"
													  +"                  No.of D.O.          Qty \n"
													  +"  DB2           "+L_strDORCT1+ "       "+L_strDORQT1+"\n"
													  +"  FoxPro        "+L_strDORCT2+ "       "+L_strDORQT2+"\n"
													   ,"Data Transfer",JOptionPane.INFORMATION_MESSAGE);
				}
					else
						{setMSG("Data not found for Market Type : "+LP_MKTTP+" on "+strDOCDT,'E');}
			}
            catch(Exception L_EX)
            {
                    setMSG(L_EX,"chkDOUPD");
            }
		}
		

		
		/**
		 */
		private void chkIVIND()
		{
			try
			{
				String L_strSQLQRY = "select ivt_mkttp,ivt_indno,ivt_prdds from mr_ivtrn where date(ivt_invdt) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDOCDT))+"' and ivt_mkttp in ('01','04','05') and ivt_stsfl<>'X' and ivt_invqt>0";
				System.out.println(L_strSQLQRY);
				ResultSet L_rstTEMP = cl_dat.exeSQLQRY(L_strSQLQRY);
				Hashtable<String,String>  L_hstINDB2 = new  Hashtable<String,String>();
				Hashtable<String,String>  L_hstINFOX = new  Hashtable<String,String>();
				L_hstINDB2.clear();
				if(L_rstTEMP != null && L_rstTEMP.next())
				{
					while(true)
					{
						L_hstINDB2.put(getRSTVAL(L_rstTEMP,"IVT_MKTTP","C")+"  "+getRSTVAL(L_rstTEMP,"IVT_INDNO","C")+"  "+getRSTVAL(L_rstTEMP,"IVT_PRDDS","C"),"");
						if(!L_rstTEMP.next())
							break;
					}
					L_rstTEMP.close();
				}

				L_strSQLQRY = "select iv_prtype,iv_indno,ivt_gradecd from mr_ivmst,mr_ivtrn where iv_prtype=ivt_prtype and iv_invno=ivt_invno and iv_invdt = ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDOCDT))+"') and iv_prtype in ('01','04','05') and iv_status<>'X'";
				System.out.println(L_strSQLQRY);
				L_rstTEMP = stmSPBKQ.executeQuery(L_strSQLQRY);
				L_hstINFOX.clear();
				if(L_rstTEMP != null && L_rstTEMP.next())
				{
					while(true)
					{
						L_hstINFOX.put(getRSTVAL1(L_rstTEMP,"IV_PRTYPE","C")+"  "+getRSTVAL1(L_rstTEMP,"IV_INDNO","C")+"  "+getRSTVAL1(L_rstTEMP,"IVT_GRADECD","C"),"");
						if(!L_rstTEMP.next())
							break;
					}
				}

				
		Enumeration enmDB2KEYS=L_hstINDB2.keys();
		String L_strINLIST = ""; 
		String L_strINKEY = ""; 
		int k=0;
		while(enmDB2KEYS.hasMoreElements())
		{
			L_strINKEY = (String)enmDB2KEYS.nextElement();
			if(!L_hstINFOX.containsKey(L_strINKEY))
				{L_strINLIST += (L_strINKEY + " / "); k++; if(k>5){L_strINLIST += "\n"; k=0;}}
		}

		//System.out.println(" L_strINLIST : "+L_strINLIST);				
		if(L_strINLIST.length()>0)
			JOptionPane.showMessageDialog(this," Missing Indent List for "+strDOCDT+" :  \n"
											  +L_strINLIST,"Data Transfer",JOptionPane.INFORMATION_MESSAGE);
			else
			JOptionPane.showMessageDialog(this," No Missing Indents found for "+strDOCDT,"Data Transfer",JOptionPane.INFORMATION_MESSAGE);
	
			}
            catch(Exception L_EX)
            {
                    setMSG(L_EX,"chkINUPD");
            }
		}
		

		
		
		/**
		 */
		private void chkIVDOR()
		{
			try
			{
				String L_strSQLQRY = "select ivt_mkttp,ivt_dorno,ivt_prdds from mr_ivtrn where date(ivt_invdt) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDOCDT))+"' and ivt_mkttp in ('01','04','05') and ivt_stsfl<>'X' and ivt_invqt>0";
				System.out.println(L_strSQLQRY);
				ResultSet L_rstTEMP = cl_dat.exeSQLQRY(L_strSQLQRY);
				Hashtable<String,String>  L_hstINDB2 = new  Hashtable<String,String>();
				Hashtable<String,String>  L_hstINFOX = new  Hashtable<String,String>();
				L_hstINDB2.clear();
				if(L_rstTEMP != null && L_rstTEMP.next())
				{
					while(true)
					{
						L_hstINDB2.put(getRSTVAL(L_rstTEMP,"IVT_MKTTP","C")+"  "+getRSTVAL(L_rstTEMP,"IVT_DORNO","C")+"  "+getRSTVAL(L_rstTEMP,"IVT_PRDDS","C"),"");
						if(!L_rstTEMP.next())
							break;
					}
					L_rstTEMP.close();
				}

				L_strSQLQRY = "select iv_prtype,iv_dono,ivt_gradecd from mr_ivmst,mr_ivtrn where iv_prtype=ivt_prtype and iv_invno=ivt_invno and iv_invdt = ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDOCDT))+"') and iv_prtype in ('01','04','05') and iv_status<>'X'";
				System.out.println(L_strSQLQRY);
				L_rstTEMP = stmSPBKQ.executeQuery(L_strSQLQRY);
				L_hstINFOX.clear();
				if(L_rstTEMP != null && L_rstTEMP.next())
				{
					while(true)
					{
						L_hstINFOX.put(getRSTVAL1(L_rstTEMP,"IV_PRTYPE","C")+"  "+getRSTVAL1(L_rstTEMP,"IV_DONO","C")+"  "+getRSTVAL1(L_rstTEMP,"IVT_GRADECD","C"),"");
						if(!L_rstTEMP.next())
							break;
					}
				}

		Enumeration enmDB2KEYS=L_hstINDB2.keys();
		String L_strDOLIST = ""; 
		String L_strDOKEY = ""; 
		int k=0;
		while(enmDB2KEYS.hasMoreElements())
		{
			L_strDOKEY = (String)enmDB2KEYS.nextElement();
			if(!L_hstINFOX.containsKey(L_strDOKEY))
				{L_strDOLIST += (L_strDOKEY + " / "); k++; if(k>5){L_strDOLIST += "\n"; k=0;}}
		}

		//System.out.println(" L_strDOLIST : "+L_strDOLIST);				
		if(L_strDOLIST.length()>0)
			JOptionPane.showMessageDialog(this," Missing D.O. List for "+strDOCDT+" :  \n"
											  +L_strDOLIST,"Data Transfer",JOptionPane.INFORMATION_MESSAGE);
			else
			JOptionPane.showMessageDialog(this," No Missing D.O.s found for "+strDOCDT,"Data Transfer",JOptionPane.INFORMATION_MESSAGE);
	
			}
            catch(Exception L_EX)
            {
                    setMSG(L_EX,"chkINUPD");
            }
		}
		
		
		
		/**
		 */
        void updDATAFL(String LP_DOCTP, String LP_MKTTP, String LP_DOCNO)
        {
                try
                {
                        exeGC();
						if(!strDOCNO_PRV.equals(LP_DOCNO) && LP_DOCTP.equals("DO"))
							{strDOCNO_PRV = LP_DOCNO; flgDOSTS_DEL = true;}
                        if (LP_DOCTP.toUpperCase().equals("IN"))
                        {
                              updDATAFL1("01","MR_INMST","MR_INMST","IN_INDNO"," IN_MKTTP = '"+LP_MKTTP+"' and IN_INDNO  = '"+LP_DOCNO+"'");
                              updDATAFL1("02","MR_INMST","MR_INMST","IN_INDNO"," IN_MKTTP = '"+LP_MKTTP+"' and IN_INDNO  = '"+LP_DOCNO+"'");
                              updDATAFL1("03","MR_INTRN","MR_INTRN","INT_INDNO"," INT_MKTTP = '"+LP_MKTTP+"' and INT_INDNO  = '"+LP_DOCNO+"'");
                              updDATAFL1("04","MR_INTRN","MR_INTRN","INT_INDNO"," INT_MKTTP = '"+LP_MKTTP+"' and INT_INDNO  = '"+LP_DOCNO+"'");
                        }
						
                        if (LP_DOCTP.toUpperCase().equals("DO"))
                        {
                            updDATAFL1("05","MR_DOTRN","MR_DOTRN","DOT_MKTTP,DOT_DORNO"," DOT_MKTTP = '"+LP_MKTTP+"' and DOT_DORNO = '"+LP_DOCNO+"'");
                            updDATAFL1("06","MR_DOMST","MR_DOTRN","DOT_MKTTP,DOT_DORNO"," DOT_MKTTP = '"+LP_MKTTP+"' and DOT_DORNO = '"+LP_DOCNO+"'");
                            updDATAFL1("07","MR_DODEL","MR_DODEL","DOD_MKTTP,DOD_DORNO"," DOD_MKTTP = '"+LP_MKTTP+"' and DOD_DORNO = '"+LP_DOCNO+"'");
							if (getINMST(strDO_PRTYPE, strDO_INDNO))
								setDOMST(strDO_PRTYPE, strDO_NO);
							if (getINTRN(strDO_PRTYPE, strDO_INDNO, strDOT_PRDCD))
								setDOTRN(strDO_PRTYPE, strDO_NO, strDOT_GRADECD);
                        }

                        if (LP_DOCTP.toUpperCase().equals("PT"))
                        {
                              updDATAFL1("08","SP_CTMST","CO_PTMST","PT_PRTTP,PT_PRTCD"," PT_PRTCD = '"+LP_DOCNO+"'");
                              updDATAFL1("09","SP_CTMST","CO_PTMST","PT_PRTTP,PT_PRTCD"," PT_PRTCD = '"+LP_DOCNO+"'");
                        }
                        if (LP_DOCTP.toUpperCase().equals("PR"))
                        {
                              updDATAFL1("10","MR_GRMST","CO_PRMST","PR_PRDDS"," PR_PRDDS = '"+LP_DOCNO+"'");
                        }
                }
                //catch(SQLException L_EX)
                catch(Exception L_EX)
                {
                        setMSG(L_EX,"updDATAFL");
                }
        }


		/** Data file updation sub-method
		 * @param LP_FLPRFX  Data file prefix (IM for Indent Master, IT for Indent Transaction etc.)
		 * @param LP_TRFNO   Data transfer serial number (predefined in mr_dtitr table) of the corresponding data file
		 * @param LP_CURFILE	Corresponding main data file getting updated on Server
		 * @param LP_ORDFL		Order (of columns) according to which data will be selected (picked-up) for updation
		 * @param LP_CHKFL		Filtering condition to be applied while selecting data for updation
		 * 
		 */
        private void updDATAFL1(String LP_TRFNO, String LP_FXPFL, String LP_DB2FL, String LP_ORDFL, String LP_CHKFL)
        {
				strFXPFL = LP_FXPFL;
                
                //setMSG("Updating "+strFXPFL+" ...",'N');
                
                try
                {
                        //System.out.println("File prefix : "+LP_FLPRFX);
                        M_strSQLQRY = "select * from mr_dtitr where DT_TRFNO = '"+LP_TRFNO+"'";
                        M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);

                        if(M_rstRSSET == null || !M_rstRSSET.next())
                        {
                                setMSG("Records not found in mr_dtitr",'E');
								//System.out.println(M_strSQLQRY);
                                return;
                        }
                        setREFVTR();
                        //crtKEYTBL();
                        strSTRSQL_1 = "select * from "+LP_DB2FL+" where "+LP_CHKFL+" order by "+LP_ORDFL;
                        rstL1RSLSET = cl_dat.exeSQLQRY2(strSTRSQL_1);
                        if(rstL1RSLSET == null || !rstL1RSLSET.next())
                        {
							   //System.out.println(strSTRSQL_1);
                               //setMSG("Records not found in "+LP_DB2FL,'E');
                                return;
                        }

                        while(true)
                        {
                                dspFLDVAL();
                                if(!rstL1RSLSET.next())
                                        break;
                        }

                        rstL1RSLSET.close();
                        
                exeGC();                        
                }
                catch(Exception L_EX)
                {
                       setMSG(L_EX,"updDATAFL1");
                }
                
        }


		/** Garbage collection
		 */
        private void exeGC()
        {
                Runtime r = Runtime.getRuntime();
                r.gc();
        }


		
		/** Capturing details from control file (mr_dtitr) into vectors
		 * for reference, during data updation
		 */
        private void setREFVTR()
        {
        try
        {
                vtrKEY01.clear();
                vtrKEY02.clear();
                vtrKEYTP.clear();

                vtrVAL01.clear();
                vtrVAL02.clear();
                vtrVALTP.clear();

                while(true)
                {
                        strKEYVL = getRSTVAL(M_rstRSSET,"DT_KEYVL","C");
                        strFLD01 = getRSTVAL(M_rstRSSET,"DT_FLD01","C");
                        strFLD02 = getRSTVAL(M_rstRSSET,"DT_FLD02","C");
                        strFLDTP = getRSTVAL(M_rstRSSET,"DT_FLDTP","C");
                        
                        if (!strKEYVL.toUpperCase().equals("X"))
                        {
                           vtrKEY01.add(strFLD01);
                           vtrKEY02.add(strFLD02);
                           vtrKEYTP.add(strFLDTP);
                        }
                        else
                        {
                           vtrVAL01.add(strFLD01);
                           vtrVAL02.add(strFLD02);
                           vtrVALTP.add(strFLDTP);
                        }

                        if (!M_rstRSSET.next())
                                break;
                }
                M_rstRSSET.close();
        }
        catch(SQLException L_EX)
        {
                 setMSG(L_EX,"setREFVTR");
        }
     }





		/** One time data capturing for specified codes from CO_CDTRN
		 * into the Hash Table
		 */
         private void crtCDTRN()
        {
			String L_strSQLQRY = "";
            try
            {
                htbCDTRN.clear();
                L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp||cmt_cgstp in ('SYSMR00SAL', 'STSMRXXIND','STSMRXXDOR','SYSMR00ZON', 'SYSMRXXSAL', 'SYSMRXXZON')";
                M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
                if(M_rstRSSET == null || !M_rstRSSET.next())
                {
                     setMSG("Records not found in CO_CDTRN",'E');
                      return;
                }
                while(true)
                {
                        strCGMTP = getRSTVAL(M_rstRSSET,"CMT_CGMTP","C");
                        strCGSTP = getRSTVAL(M_rstRSSET,"CMT_CGSTP","C");
                        strCODCD = getRSTVAL(M_rstRSSET,"CMT_CODCD","C");
                        String[] staCDTRN = new String[intCDTRN_TOT];
                        staCDTRN[intAE_CMT_CODDS] = getRSTVAL(M_rstRSSET,"CMT_CODDS","C");
                        staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(M_rstRSSET,"CMT_SHRDS","C");
                        staCDTRN[intAE_CMT_CHP01] = getRSTVAL(M_rstRSSET,"CMT_CHP01","C");
                        staCDTRN[intAE_CMT_CHP02] = getRSTVAL(M_rstRSSET,"CMT_CHP02","C");
                        staCDTRN[intAE_CMT_NMP01] = getRSTVAL(M_rstRSSET,"CMT_NMP01","C");
                        staCDTRN[intAE_CMT_NMP02] = getRSTVAL(M_rstRSSET,"CMT_NMP02","C");
                        staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(M_rstRSSET,"CMT_CCSVL","C");
                        staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(M_rstRSSET,"CMT_NCSVL","C");
                        htbCDTRN.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
                        if (!M_rstRSSET.next())
                                break;
                }
                M_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtCDTRN");
            }
        }


		/** Creating hash table with required details from Product Master
		 */
        private void crtPRMST()
        {
			String L_strSQLQRY="";
            try
            {
                htbPRMST.clear();
                L_strSQLQRY = "select PR_PRDCD, PR_PRDDS from co_prmst order by pr_prdds";
                M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
                if(M_rstRSSET == null || !M_rstRSSET.next())
                {
                     setMSG("Records not found in CO_PRMST",'E');
                      return;
                }
                while(true)
                {
                        strPRDDS = getRSTVAL(M_rstRSSET,"PR_PRDDS","C");
                        String[] staPRMST = new String[intPRMST_TOT];
                        staPRMST[intAE_PR_PRDCD] = getRSTVAL(M_rstRSSET,"PR_PRDCD","C");

                        htbPRMST.put(strPRDDS,staPRMST);
                        if (!M_rstRSSET.next())
                                break;
                }
                M_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtPRMST");
            }
        }


		
		/** Creating hash table with required details from Product Master (codewise)
		 */
        private void crtPRMST1()
        {
			String L_strSQLQRY="";
            try
            {
                htbPRMST.clear();
                L_strSQLQRY = "select pr_prdcd, pr_prdds from co_prmst order by pr_prdcd";
                M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
                if(M_rstRSSET == null || !M_rstRSSET.next())
                {
                     setMSG("Records not found in CO_PRMST",'E');
                      return;
                }
                while(true)
                {
                        strPRDCD = getRSTVAL(M_rstRSSET,"PR_PRDCD","C");
                        String[] staPRMST1 = new String[intPRMST1_TOT];
                        staPRMST1[intAE_PR_PRDDS] = getRSTVAL(M_rstRSSET,"PR_PRDDS","C");

                        htbPRMST1.put(strPRDCD,staPRMST1);
                        if (!M_rstRSSET.next())
                                break;
                }
                M_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtPRMST1");
            }
        }
		



		/** Creating hash table with required details from Indent Master 
		 */
        private boolean getINMST(String LP_MKTTP, String LP_INDNO)
        {
			String L_strSQLQRY="";
            try
            {
                L_strSQLQRY = "select * from MR_INMST where IN_MKTTP = '"+LP_MKTTP+"' and IN_INDNO = '"+LP_INDNO+"'";
				
                L3_RSLSET = cl_dat.exeSQLQRY3(L_strSQLQRY);

                if(L3_RSLSET == null || !L3_RSLSET.next())
                {
                     //setMSG("Indent Records not found for  "+LP_INDNO,'E');
                      return false;
                }
                        strIN_INDDT = getRSTVAL(L3_RSLSET,"IN_INDDT","D");
                        strIN_AMNDNO = getRSTVAL(L3_RSLSET,"IN_AMDNO","C");
                        strIN_AMNDDT = getRSTVAL(L3_RSLSET,"IN_AMDDT","D");
                        strIN_PONO = getRSTVAL(L3_RSLSET,"IN_PORNO","C");
                        strIN_PODATE = getRSTVAL(L3_RSLSET,"IN_PORDT","D");
                        strIN_SALTYPE = getCDTRN("SYSMR00SAL"+getRSTVAL(L3_RSLSET,"IN_SALTP","C"),"CMT_CCSVL");
                        strIN_DTPCD = getRSTVAL(L3_RSLSET,"IN_DTPCD","C");
                        strIN_ZONE = getCDTRN("SYSMR00ZON"+getRSTVAL(L3_RSLSET,"IN_ZONCD","C"),"CMT_CCSVL");;
                        strIN_DISTRIBTR = getRSTVAL(L3_RSLSET,"IN_DSRCD","C");
                        strIN_CONSIGNEE = getRSTVAL(L3_RSLSET,"IN_CNSCD","C");
                        strIN_BUYER = getRSTVAL(L3_RSLSET,"IN_BYRCD","C");
                        strIN_CURRENCY = getRSTVAL(L3_RSLSET,"IN_CURCD","C");
                        strIN_EXCHRATE = getRSTVAL(L3_RSLSET,"IN_ECHRT","N");
                        strIN_PMTTYPE = getRSTVAL(L3_RSLSET,"IN_PMTCD","C");
                        strIN_PMTCUST = getRSTVAL(L3_RSLSET,"IN_CPTVL","N");
                        strIN_PMTACCT = getRSTVAL(L3_RSLSET,"IN_APTVL","N");
                        strIN_STXCD = getRSTVAL(L3_RSLSET,"IN_STXCD","C");
                        strIN_STXRT = getRSTVAL(L3_RSLSET,"IN_STXRT","N");
                        strIN_OCTCD = getRSTVAL(L3_RSLSET,"IN_OCTCD","C");
                        strIN_OCTRT = getRSTVAL(L3_RSLSET,"IN_OCTRT","N");
                        strIN_SVCCD = getRSTVAL(L3_RSLSET,"IN_SVCCD","C");
                        strIN_SVCRT = getRSTVAL(L3_RSLSET,"IN_SVCRT","N");

						L3_RSLSET.close();
						L_strSQLQRY = "select * from MR_RMMST where RM_MKTTP = '"+LP_MKTTP+"' and RM_TRNTP = 'DO' and RM_DOCNO = '"+strDO_NO+"'";
				
						L3_RSLSET = cl_dat.exeSQLQRY3(L_strSQLQRY);
						strDO_SPLINST = "";
						if(L3_RSLSET == null || !L3_RSLSET.next())
						{
						     setMSG("Remark not found for  D.O. "+strDO_NO,'E');
						      return true;
						}
                        strDO_SPLINST = getRSTVAL(L3_RSLSET,"RM_REMDS","N");
            }
            catch(Exception L_EXgetINMST)
            {
                   setMSG(L_EXgetINMST,"getINMST");
            }
			return true;
        }





		/** Creating hash table with required details from DO Transaction table
		 */
        private void crtDOTRN(String LP_DATAFL)
        {
			String L_strSQLQRY = "";
            try
            {
                htbDOTRN.clear();
                ResultSet L2_RSLSET;
                L_strSQLQRY = "select * from "+LP_DATAFL;
                L2_RSLSET = exeBKPQRY1(L_strSQLQRY);

                if(L2_RSLSET == null || !L2_RSLSET.next())
                {
					 //System.out.println(L_strSQLQRY);
                     //setMSG("Records not found in "+LP_DATAFL,'E');
                      return;
                }
                while(true)
                {
                        strMKTTP = getRSTVAL(L2_RSLSET,"DOT_PRTYPE","C");
                        strDORNO = getRSTVAL(L2_RSLSET,"DOT_DONO","C");
                        strPRDDS = getRSTVAL(L2_RSLSET,"DOT_GRADEC","C");
                        strPRDCD = getPRMST(strPRDDS);
                        String[] staDOTRN = new String[intDOTRN_TOT];
                        staDOTRN[intAE_DOT_PKGTP] = getRSTVAL(L2_RSLSET,"DOT_PKGTYP","C");

                        htbDOTRN.put(strMKTTP+strDORNO+strPRDCD,staDOTRN);
                        if (!L2_RSLSET.next())
                                break;
                }
                L2_RSLSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtDOTRN");
            }
        }

		/** Picking up required details from MR_INTRN
		 */
        private boolean getINTRN(String LP_MKTTP, String LP_INDNO, String LP_PRDCD)
        {
			String L_strSQLQRY = "";
            try
            {
                L_strSQLQRY = "select * from MR_INTRN where INT_MKTTP ='"+LP_MKTTP+"' and INT_INDNO = '"+LP_INDNO+"' and INT_PRDCD = '"+LP_PRDCD+"'";
				//System.out.println("getINTRN : "+L_strSQLQRY);
                L3_RSLSET = cl_dat.exeSQLQRY3(L_strSQLQRY);
                if(L3_RSLSET == null || !L3_RSLSET.next())
                {
                      //setMSG("Records not found in MR_INTRN for "+LP_INDNO,'E');
                      return false;
                }

				strINT_AMNDNO = getRSTVAL(L3_RSLSET,"INT_AMDNO","C");
				strINT_CNVFACT = getRSTVAL(L3_RSLSET,"INT_CNVFT","C");
				strINT_BASRATE = getRSTVAL(L3_RSLSET,"INT_BASRT","N");
				strINT_RATEPER = getRSTVAL(L3_RSLSET,"INT_RTPVL","N");
				strINT_EXCDUTY = getRSTVAL(L3_RSLSET,"INT_EXCRT","N");
                L3_RSLSET.close();
            }
            catch(Exception L_EXgetINTRN)
            {
                   setMSG(L_EXgetINTRN,"getINTRN");
            }
			return true;
        }


		/** Replacing Additional details from MR_INTRN
		 */
        private void setDOTRN(String LP_MKTTP, String LP_DORNO, String LP_PRDDS)
        {
			String L_strSQLQRY="";
            try
            {
                L_strSQLQRY = "update MR_DOTRN set "+
				setUPDSTR("DOT_AMNDNO",strINT_AMNDNO,"C","U")+
				setUPDSTR("DOT_BASRATE",strINT_BASRATE,"N","U")+
				setUPDSTR("DOT_RATEPER",strINT_RATEPER,"N","U")+
				setUPDSTR_L("DOT_EXCDUTY",strINT_EXCDUTY,"N","U")+
			    " where DOT_PRTYPE = '"+LP_MKTTP+"' and DOT_DONO = '"+LP_DORNO+"' and DOT_GRADECD = '"+LP_PRDDS+"'";
                //System.out.println(L_strSQLQRY);
				//O_DOUT.writeBytes(L_strSQLQRY+"\n");
				stmSTBKA.executeUpdate(L_strSQLQRY);
                conSPBKA.commit();
            }
            catch(Exception L_EXsetDOTRN)
            {
                   System.out.println(L_strSQLQRY);
                   setMSG(L_EXsetDOTRN,"DOTRN");
            }
        }
		
		

		/** Replacing Additional details from MR_INMST
		 */
        private void setDOMST(String LP_MKTTP, String LP_DORNO)
        {
			String L_strSQLQRY="";
            try
            {
                L_strSQLQRY = "update MR_DOMST set "+
						setUPDSTR("DO_INDDATE",strIN_INDDT,"D","U")+
						setUPDSTR("DO_INAMDNO",strIN_AMNDNO,"C","U")+
						setUPDSTR("DO_INAMDDT",strIN_AMNDDT,"D","U")+
						setUPDSTR("DO_PONO",strIN_PONO,"C","U")+
						setUPDSTR("DO_PODATE",strIN_PODATE,"D","U")+
						setUPDSTR("DO_SALTYPE",strIN_SALTYPE,"C","U")+
						setUPDSTR("DO_DTPCD",strIN_DTPCD,"C","U")+
						setUPDSTR("DO_ZONE",strIN_ZONE,"C","U")+
						setUPDSTR("DO_DISTRIBTR",strIN_DISTRIBTR,"C","U")+
						setUPDSTR("DO_CONSIGNEE",strIN_CONSIGNEE,"C","U")+
						setUPDSTR("DO_BUYER",strIN_BUYER,"C","U")+
						setUPDSTR("DO_CURRENCY",strIN_CURRENCY,"C","U")+
						setUPDSTR("DO_EXCHRATE",strIN_EXCHRATE,"N","U")+
						setUPDSTR("DO_PMTTYPE",strIN_PMTTYPE,"C","U")+
						setUPDSTR("DO_PMTCUST",strIN_PMTCUST,"N","U")+
						setUPDSTR("DO_PMTACCT",strIN_PMTACCT,"N","U")+
						setUPDSTR("DO_STAXCD",strIN_STXCD,"C","U")+
						setUPDSTR("DO_STAXRT",strIN_STXRT,"N","U")+
						setUPDSTR("DO_SPLINST",strDO_SPLINST,"C","U")+
						setUPDSTR("DO_OCTCD",strIN_OCTCD,"C","U")+
						setUPDSTR("DO_OCTRT",strIN_OCTRT,"N","U")+
						setUPDSTR("DO_SVCCD",strIN_SVCCD,"C","U")+
						setUPDSTR_L("DO_SVCRT",strIN_SVCRT,"N","U")+
			    " where DO_PRTYPE = '"+LP_MKTTP+"' and DO_NO = '"+LP_DORNO+"'";
                //System.out.println(L_strSQLQRY);
				//O_DOUT.writeBytes(L_strSQLQRY+"\n");
				stmSTBKA.executeUpdate(L_strSQLQRY);
                conSPBKA.commit();
            }
            catch(Exception L_EXsetDOMST)
            {
				   System.out.println(L_strSQLQRY);
                   setMSG(L_EXsetDOMST,"setDOMST");
            }
        }
		
		
        /** Hash Table of Key values of main table is created
        *  <br> This is used to decide whether to Update / Insert record
        *  <br> depending on Key Value availability in Hash Table
        */
        private void crtKEYTBL()
        {
			String L_strSQLQRY = "";
            try
            {
                intKEYSIZE = vtrKEY02.size();
                String L_NEWNM, L_KEYSTR, L_KEYTP;
                String L_KPRFX = "", L_KSUFX = "";
                L_KEYSTR = "";

                for(int i=0 ; i<intKEYSIZE ; i++)
                {
                        L_KEYTP = vtrKEYTP.get(i).toString();
                        if(L_KEYTP.equals("D"))
                        {
                              L_KPRFX = "char(";
                              L_KSUFX = ")";
                        }
                        L_NEWNM = vtrKEY01.get(i).toString();
                        L_KEYSTR += L_KPRFX+L_NEWNM+L_KSUFX;
                        if(i<intKEYSIZE-1)
                            L_KEYSTR += "||";
                }
                L_strSQLQRY = "select "+L_KEYSTR+" KEYSTR from "+strFXPFL;
                M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);


                if(M_rstRSSET == null || !M_rstRSSET.next())
                {
                        setMSG("Records not found in mr_dtitr",'E');
                         return;
                }
                String L_KEYVL;
                while(true)
                {
                                L_KEYVL = getRSTVAL(M_rstRSSET,"KEYSTR","C");
                                htbKEYTBL.put(L_KEYVL,"1");
                                //setMSG(strCURFILE+" : "+L_KEYVL,'N');
                                if (!M_rstRSSET.next())
                                        break;
                }
                M_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtKEYTBL");
            }
        }



        /** Method for actual execution of updation / insertion query
        */
        private void dspFLDVAL()
        {
        String L_KEYSTR = "";
        try
        {
                //JOptionPane.showMessageDialog(this,"Size of vtrKEY02 : "+vtrKEY02.size(),"Message",JOptionPane.INFORMATION_MESSAGE);
                intKEYSIZE = vtrKEY02.size();
                intVALSIZE = vtrVAL02.size();
                String L_NEWNM, L_OLDNM, L_FLDVL, L_FLDVL1, L_FLDTP, L_FLDTP1;
                strWHRSTR  = "";
                strINSSTR1 = "";
                strINSSTR2 = "";
                strUPDSTR  = "";
                L_KEYSTR   = "";

                for(int i=0 ; i<intKEYSIZE ; i++)
                {
                        L_NEWNM = vtrKEY01.get(i).toString();
                        L_OLDNM = vtrKEY02.get(i).toString();
                        L_FLDTP = vtrKEYTP.get(i).toString();
                        L_FLDTP1 = L_FLDTP;
						//System.out.println(L_NEWNM+"/"+L_OLDNM+"/"+L_FLDTP);
                        if (L_FLDTP.toUpperCase().equals("X"))
                        {
                                L_FLDVL = getFLDVL(L_OLDNM);
                                L_FLDVL = L_FLDVL.toUpperCase().trim().equals("NULL") ? null : L_FLDVL;
		                        L_FLDTP1 = "C";
								if(flgNUMFLD)
		                           L_FLDTP1 = "N";
                        }
                        else
                                L_FLDVL = getRSTVAL(rstL1RSLSET,L_OLDNM,L_FLDTP1);


                        L_FLDVL1 = L_FLDVL;
                        if(L_FLDTP1.equals("D"))
                                L_FLDVL1 = L_FLDVL.substring(0,6)+L_FLDVL.substring(8,10);

                        setINSSTR(L_NEWNM,L_FLDVL,L_FLDTP1);

                        //System.out.println("L_KEYSTR : "+L_KEYSTR + "    L_FLDVL : "+L_FLDVL);
                        L_KEYSTR +=  L_FLDVL1;
                        

                        if (i == (intKEYSIZE-1))
                                strWHRSTR += setUPDSTR_L(L_NEWNM,L_FLDVL,L_FLDTP1,"W");
                        else
                                strWHRSTR += setUPDSTR(L_NEWNM,L_FLDVL,L_FLDTP1,"W");
                }
                setMSG(strFXPFL+" : "+L_KEYSTR,'N');


                for(int i=0 ; i<intVALSIZE ; i++)
                {
                        L_NEWNM = vtrVAL01.get(i).toString();
                        L_OLDNM = vtrVAL02.get(i).toString();
                        L_FLDTP = vtrVALTP.get(i).toString();
                        L_FLDTP1 = L_FLDTP;
						//System.out.println(L_NEWNM+"/"+L_OLDNM+"/"+L_FLDTP);
                        if (L_FLDTP.toUpperCase().equals("X"))
                        {
                                L_FLDVL = getFLDVL(L_OLDNM);
                                if(L_FLDVL.toUpperCase().trim().equals("NULL"))
                                        L_FLDVL = "null";
		                        L_FLDTP1 = "C";
								if(flgNUMFLD)
		                           L_FLDTP1 = "N";
                        }
                        else
                                L_FLDVL = getRSTVAL(rstL1RSLSET,L_OLDNM,L_FLDTP1);

                        if (i == (intVALSIZE-1))
                        {
                                strUPDSTR += setUPDSTR_L(L_NEWNM,L_FLDVL,L_FLDTP1,"U");
                                             setINSSTR_L(L_NEWNM,L_FLDVL,L_FLDTP1);
                        }
                        else
                        {
                                strUPDSTR += setUPDSTR(L_NEWNM,L_FLDVL,L_FLDTP1,"U");
                                             setINSSTR(L_NEWNM,L_FLDVL,L_FLDTP1);
                        }


                }
				
                strSTRSQL_1 = "insert into "+strFXPFL+" ("+strINSSTR1+") values ("+strINSSTR2+")";
                strSTRSQL_2 = "update "+strFXPFL+" set "+strUPDSTR;
                //System.out.println("1.  "+strSTRSQL_1);
                //System.out.println("2.  "+strSTRSQL_2);
                if (!strWHRSTR.equals(""))
                        strSTRSQL_2+= " where "+ strWHRSTR;
                String L0_STRSQL = "select count(*) from "+strFXPFL+" where "+strWHRSTR;
                //System.out.println(L0_STRSQL);
				
                int L_RECCNT = getRECCNT1(L0_STRSQL);

                M_strSQLQRY = strSTRSQL_1;
                if(L_RECCNT > 0)
                        M_strSQLQRY = strSTRSQL_2;

				if(strDOCTP.toUpperCase().equals("PR") && L_RECCNT > 0)
					{cl_dat.M_flgLCUPD_pbst = true; return;}
					
                //System.out.println(M_strSQLQRY);
				//O_DOUT.writeBytes(M_strSQLQRY+"\n");
                stmSTBKA.executeUpdate(M_strSQLQRY);
                conSPBKA.commit();
                }
                //catch (SQLException L_SE)
                catch (Exception L_SE)
                {
					   System.out.println(M_strSQLQRY);
                       setMSG(L_SE,"DSPFLDVAL");
                }

                //M_strSQLQRY = strSTRSQL_1;
                //if(htbKEYTBL.containsKey(L_KEYSTR))
                //   M_strSQLQRY = strSTRSQL_2;
                //                cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
                //if(cl_dat.M_flgLCUPD_pbst)
                //{
                //        cl_dat.exeDBCMT("dspFLDVAL");
                //}
                cl_dat.M_flgLCUPD_pbst = true;
                //System.out.println(M_strSQLQRY);
                //JOptionPane.showMessageDialog(this,"Next Record","Message",JOptionPane.INFORMATION_MESSAGE);

        }

		/** Picking up / arriving at field details exclusivelly 
		 * @param LP_FLDNM Input field Name A method with same name (condition) is defined for 
		 * <br> processing corresponding source code.
		 */

        private String getFLDVL(String LP_FLDNM)
        {
           String L_RETSTR = "";
           String L_TMPSTR = "";
           try
           {
				flgNUMFLD = false;
                if(LP_FLDNM.equals("XX_TRNFL"))
                {
                        L_RETSTR = "0";
                }
                if(LP_FLDNM.equals("XX_LUPDT"))
                {
                          L_RETSTR = "date()";
                }
                
                else if(LP_FLDNM.equals("CC_PRDCD"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"CC_GRADECD","C");
                        L_RETSTR = getPRMST(L_TMPSTR);
                }
                else if(LP_FLDNM.equals("INT_PRODCD"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"INT_PRDCD","C");
                        L_RETSTR = "";
						if (L_TMPSTR.substring(0,4).equals("5111"))
							L_RETSTR = "SC";
						else if (L_TMPSTR.substring(0,4).equals("5112"))
							L_RETSTR = "SH";
						else if (L_TMPSTR.substring(0,2).equals("52"))
							L_RETSTR = "SP";
						else if (L_TMPSTR.substring(0,2).equals("53"))
							L_RETSTR = "AP";
						else if (L_TMPSTR.substring(0,2).equals("54"))
							L_RETSTR = "MB";
						else
							L_RETSTR = "PS";
                }
                else if(LP_FLDNM.equals("PR_PRODCD"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"PR_PRDCD","C");
                        L_RETSTR = "";
						if (L_TMPSTR.substring(0,4).equals("5111"))
							L_RETSTR = "SC";
						else if (L_TMPSTR.substring(0,4).equals("5112"))
							L_RETSTR = "SH";
						else if (L_TMPSTR.substring(0,2).equals("52"))
							L_RETSTR = "SP";
						else if (L_TMPSTR.substring(0,2).equals("53"))
							L_RETSTR = "AP";
						else if (L_TMPSTR.substring(0,2).equals("54"))
							L_RETSTR = "MB";
						else
							L_RETSTR = "PS";
                }
                else if(LP_FLDNM.equals("PR_PRDTP"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"PR_PRDCD","C");
                        L_RETSTR = "";
						if (L_TMPSTR.substring(0,2).equals("51"))
							L_RETSTR = "01";
						else if (L_TMPSTR.substring(0,2).equals("53"))
							L_RETSTR = "04";
						else if (L_TMPSTR.substring(0,2).equals("54"))
							L_RETSTR = "05";
                }
                else if(LP_FLDNM.equals("DOT_PRODCD"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"DOT_PRDCD","C");
                        L_RETSTR = "";
						if (L_TMPSTR.substring(0,4).equals("5111"))
							L_RETSTR = "SC";
						else if (L_TMPSTR.substring(0,4).equals("5112"))
							L_RETSTR = "SH";
						else if (L_TMPSTR.substring(0,2).equals("52"))
							L_RETSTR = "SP";
						else if (L_TMPSTR.substring(0,2).equals("53"))
							L_RETSTR = "AP";
						else if (L_TMPSTR.substring(0,2).equals("54"))
							L_RETSTR = "MB";
						else
							L_RETSTR = "PS";
                }
                else if(LP_FLDNM.equals("PR_PRTYPE"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"DOT_PRDCD","C");
                        L_RETSTR = "";
						if (L_TMPSTR.substring(0,4).equals("5111"))
							L_RETSTR = "SC";
						else if (L_TMPSTR.substring(0,4).equals("5112"))
							L_RETSTR = "SH";
						else if (L_TMPSTR.substring(0,2).equals("52"))
							L_RETSTR = "SP";
						else if (L_TMPSTR.substring(0,2).equals("53"))
							L_RETSTR = "AP";
						else if (L_TMPSTR.substring(0,2).equals("54"))
							L_RETSTR = "MB";
						else
							L_RETSTR = "PS";
                }
                else if(LP_FLDNM.equals("PR_QLTYTAG"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"PR_PRDCD","C").substring(2,4);
						L_RETSTR = "P";
						if (L_TMPSTR.equals("98"))
							L_RETSTR = "S";
                }
                else if(LP_FLDNM.equals("PR_OBSTAG"))
                {
					L_RETSTR = "N";
                }
                else if(LP_FLDNM.equals("PT_GRPCD"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"PT_GRPCD","C");
						L_RETSTR = getRSTVAL(rstL1RSLSET,"PT_GRPCD","C");
						if (L_TMPSTR.equalsIgnoreCase("XXXXX"))
						{
							L_RETSTR = getRSTVAL(rstL1RSLSET,"PT_PRTCD","C");
						}
                }
                else if(LP_FLDNM.equals("PR_PKGWT"))
                {
					flgNUMFLD = true;
					L_RETSTR = "0.025";
                }
                else if(LP_FLDNM.equals("PR_EXCDUTY"))
                {
					flgNUMFLD = true;
					L_RETSTR = "0.16";
                }
                else if(LP_FLDNM.equals("PR_AVGRT"))
                {
					flgNUMFLD = true;
					L_RETSTR = "0.00";
                }
                else if(LP_FLDNM.equals("DOT_INDNO"))
                {
                        strDO_PRTYPE = getRSTVAL(rstL1RSLSET,"DOT_MKTTP","C");
                        strDO_INDNO  = getRSTVAL(rstL1RSLSET,"DOT_INDNO","C");
                        strDO_NO     = getRSTVAL(rstL1RSLSET,"DOT_DORNO","C");
						//if (getINMST(strDO_PRTYPE, strDO_INDNO))
						//	setDOMST(strDO_PRTYPE, strDO_NO);
						L_RETSTR = strDO_INDNO;
                }
                else if(LP_FLDNM.equals("DOT_ORDUM"))
                {
                        L_RETSTR  = getRSTVAL(rstL1RSLSET,"DOT_ORDUM","C");
                        strDO_PRTYPE = getRSTVAL(rstL1RSLSET,"DOT_MKTTP","C");
                        strDO_INDNO  = getRSTVAL(rstL1RSLSET,"DOT_INDNO","C");
                        strDO_NO     = getRSTVAL(rstL1RSLSET,"DOT_DORNO","C");
                        strDOT_PRDCD   = getRSTVAL(rstL1RSLSET,"DOT_PRDCD","C");
                        strDOT_GRADECD = getRSTVAL(rstL1RSLSET,"DOT_PRDDS","C");
                        strDOT_PKGTYPE = getRSTVAL(rstL1RSLSET,"DOT_PKGTP","C");
						//if (getINTRN(strDO_PRTYPE, strDO_INDNO, strDOT_PRDCD))
						//	setDOTRN(strDO_PRTYPE, strDO_NO, strDOT_GRADECD);
                }
                else if(LP_FLDNM.equals("DOD_PRODCD"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"DOD_PRDCD","C");
                        L_RETSTR = "";
						if (L_TMPSTR.substring(0,4).equals("5111"))
							L_RETSTR = "SC";
						else if (L_TMPSTR.substring(0,4).equals("5112"))
							L_RETSTR = "SH";
						else if (L_TMPSTR.substring(0,2).equals("52"))
							L_RETSTR = "SP";
						else if (L_TMPSTR.substring(0,2).equals("53"))
							L_RETSTR = "AP";
						else if (L_TMPSTR.substring(0,2).equals("54"))
							L_RETSTR = "MB";
						else
							L_RETSTR = "PS";
                }
				else if(LP_FLDNM.equals("INT_CC3RF"))
                {
						//System.out.println("005");
                        String L_CC3VL = getRSTVAL(rstL1RSLSET,"INT_CC3RF","C");
						L_RETSTR = "";
						if(L_CC3VL.length()>5)
							L_RETSTR = L_CC3VL.substring(1,6);
                }
                else if(LP_FLDNM.equals("DOD_PRDDS"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"DOD_PRDCD","C");
                        L_RETSTR = getPRMST1(L_TMPSTR);
                }
                else if(LP_FLDNM.equals("IN_SALTP1"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"IN_SALTP","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"IN_SALTP","C");
                        L_RETSTR = getCDTRN("SYSMR00SAL"+L_TMPSTR,"CMT_CCSVL");
                }
                else if(LP_FLDNM.equals("IN_ZONCD1"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"IN_ZONCD","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"IN_ZONCD","C");
                        L_RETSTR = getCDTRN("SYSMR00ZON"+L_TMPSTR,"CMT_CCSVL");
                }
                else if(LP_FLDNM.equals("INT_SALTP1"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"INT_SALTP","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"INT_SALTP","C");
                        L_RETSTR = getCDTRN("SYSMR00SAL"+L_TMPSTR,"CMT_CCSVL");
                }
                else if(LP_FLDNM.equals("IN_STSFL1"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"IN_STSFL","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"IN_STSFL","C");
                        L_RETSTR = getCDTRN("STSMRXXIND"+L_TMPSTR,"CMT_CCSVL");
                }
                else if(LP_FLDNM.equals("INT_STSFL1"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"INT_STSFL","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"INT_STSFL","C");
                        L_RETSTR = getCDTRN("STSMRXXIND"+L_TMPSTR,"CMT_CCSVL");
                }
                else if(LP_FLDNM.equals("DOT_STSFL1"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"DOT_STSFL","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"DOT_STSFL","C");
                        L_RETSTR = getCDTRN("STSMRXXDOR"+L_TMPSTR,"CMT_CCSVL");
                }
                else if(LP_FLDNM.equals("PR_PRDTP"))
                {
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"PR_PRDTP","C");
						if(L_TMPSTR.equalsIgnoreCase("01"))
							L_RETSTR = "01";
						else if(L_TMPSTR.equalsIgnoreCase("02"))
							L_RETSTR = "01";
						else if(L_TMPSTR.equalsIgnoreCase("04"))
							L_RETSTR = "04";
						else if(L_TMPSTR.equalsIgnoreCase("05"))
							L_RETSTR = "05";
                }
                else if(LP_FLDNM.equals("DOT_STSFL2"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"DOT_STSFL","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"DOT_STSFL","C");
						if(!L_TMPSTR.equalsIgnoreCase("X"))
							{flgDOSTS_DEL = false; strDOSTS_NODEL = L_TMPSTR;}
						L_RETSTR = getCDTRN("STSMRXXDOR"+((!flgDOSTS_DEL && L_TMPSTR.equalsIgnoreCase("X")) ? strDOSTS_NODEL : L_TMPSTR),"CMT_CCSVL");
                }
                else if(LP_FLDNM.equals("DOD_STSFL1"))
                {
                        //L_RETSTR = getRSTVAL(rstL1RSLSET,"DOD_STSFL","C");
                        L_TMPSTR = getRSTVAL(rstL1RSLSET,"DOD_STSFL","C");
                        L_RETSTR = getCDTRN("STSMRXXDOR"+L_TMPSTR,"CMT_CCSVL");
                }
           }
           catch (Exception L_EX)
           {
                 setMSG(L_EX,"getFLDVL");
           }
           //System.out.println("getFLDVL : "+LP_FLDNM+"/"+L_RETSTR);
           //JOptionPane.showMessageDialog(this,"getFLDVL : "+LP_FLDNM+"/"+L_RETSTR,"Message",JOptionPane.INFORMATION_MESSAGE);
           return L_RETSTR;
        }



        /** Method for returning values from Result Set
         * <br> with respective verifications against various data types
         * @param	LP_RSLSET		Result set name
         * @param       LP_FLDNM                Name of the field for which data is to be extracted
         * @param	LP_FLDTP		Data Type of the field
         */
		private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
		{
            String L_RETVL = "";
			//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
            try
            {
		if (LP_FLDTP.equals("C"))
                {
			L_RETVL = LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
                }
		else if (LP_FLDTP.equals("N"))
                {
			L_RETVL = LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
                }
		else if (LP_FLDTP.equals("D"))
                {
						L_RETVL = LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
                }
		else if (LP_FLDTP.equals("T"))
                {
                        L_RETVL = M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
                }
                //System.out.println(" : "+nvlSTRVL(L_RETVL," "));
           }
           catch (Exception L_EX)
           {
				System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
				setMSG(L_EX,"getRSTVAL");
           }
        return L_RETVL;
        } 
	

		/** Method for exclusive updations of tax transactions
		 * through getFLDVL
		 */
        private void updTXDOC()
        {
          try
          {
                String L_SYSCD, L_SBSTP, L_DOCTP, L_DOCNO, L_PRDCD, L_TRNTP, L_SBSCD, L_CCL01, L_CCL02, L_CCL03, L_CC1RF, L_CC2RF, L_CC3RF, L_EXCVL, L_EXCFL, L_TRNFL, L_STSFL, L_LUSBY, L_LUPDT, L_PRTTP, L_PRTCD, L_CODVL;
                L_CCL01="0.00";
                L_CCL02="0.00";
                L_CCL03="0.00";
                L_CC1RF="";
                L_CC2RF="";
                L_CC3RF="";

                // Default field values setting
                L_SYSCD = "MR";
                L_SBSTP = strMKTTP;
                L_DOCTP = "COR";
                L_DOCNO = strINDNO;
                L_PRDCD = strPRDCD;
                L_TRNTP = "T";                  // Transaction level tax
                L_SBSCD = strZONCD1+strSALTP1+"00";
                String[] staCCLVL = new String[3];
                String[] staCCLRF = new String[3];
                staCCLVL[0] = strCDCVL;
                staCCLVL[1] = strDDCVL;
                staCCLVL[2] = strTDCVL;
                staCCLRF[0] = "C"+nvlSTRVL(strCNSCD,"");
                staCCLRF[1] = "D"+nvlSTRVL(strDSRCD,"");
                staCCLRF[2] = "C"+nvlSTRVL(strTDCRF,"");
                int L_CCLCT = 1;
                for (int i=0; i<3 ; i++)
                {
                        if (Double.parseDouble(staCCLVL[i])>0)
                        {
                                if(L_CCLCT==1)
                                {
                                        L_CCL01 = staCCLVL[i];
                                        L_CC1RF = staCCLRF[i];
                                }
                                else if(L_CCLCT==2)
                                {
                                        L_CCL02 = staCCLVL[i];
                                        L_CC2RF = staCCLRF[i];
                                }
                                else if(L_CCLCT==3)
                                {
                                        L_CCL03 = staCCLVL[i];
                                        L_CC3RF = staCCLRF[i];
                                }
                                L_CCLCT++; 
                        }
                }


                L_EXCVL = strEXCRT;
                L_EXCFL = "P";
                L_TRNFL = "0";
                L_STSFL = "";
                L_LUSBY = strLUSBY;
                L_LUPDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(strLUPDT));
                

                        String J1    = "','";
                        String J2    = " , ";
                        String R1_STRSQL  = "insert into CO_TXDOC (TX_SYSCD,    TX_SBSTP,  TX_DOCTP,  TX_DOCNO,  TX_PRDCD,  TX_SBSCD,  TX_TRNTP,  TX_EXCFL,  TX_TRNFL,  TX_STSFL,  TX_LUSBY,    TX_LUPDT)";
                               R1_STRSQL +=           " values ('"+L_SYSCD+J1+  L_SBSTP+J1+L_DOCTP+J1+L_DOCNO+J1+L_PRDCD+J1+L_SBSCD+J1+L_TRNTP+J1+L_EXCFL+J1+L_TRNFL+J1+L_STSFL+J1+L_LUSBY+"','"+L_LUPDT+"')";

                        String R2_STRSQL  = "update  CO_TXDOC set ";
                        R2_STRSQL += " TX_SBSCD = '"+L_SBSCD+"',";
                        R2_STRSQL += " TX_TRNTP = '"+L_TRNTP+"',";
                        R2_STRSQL += " TX_EXCVL =  "+L_EXCVL+" ,";
                        R2_STRSQL += " TX_EXCFL = '"+L_EXCFL+"',";
                        R2_STRSQL += " TX_TRNFL = '"+L_TRNFL+"',";
                        R2_STRSQL += " TX_STSFL = '"+L_STSFL+"',";
                        R2_STRSQL += " TX_LUSBY = '"+L_LUSBY+"',";
                        R2_STRSQL += " TX_LUPDT = '"+L_LUPDT+"'  where ";
                        R2_STRSQL += " TX_SYSCD = '"+L_SYSCD+"' and ";
                        R2_STRSQL += " TX_SBSTP = '"+L_SBSTP+"' and ";
                        R2_STRSQL += " TX_DOCTP = '"+L_DOCTP+"' and ";
                        R2_STRSQL += " TX_DOCNO = '"+L_DOCNO+"' and ";
                        R2_STRSQL += " TX_PRDCD = '"+L_PRDCD+"'";
                        String R0_STRSQL = "select count(*) from CO_TXDOC where ";
                        R0_STRSQL += " TX_SYSCD = '"+L_SYSCD+"' and ";
                        R0_STRSQL += " TX_SBSTP = '"+L_SBSTP+"' and ";
                        R0_STRSQL += " TX_DOCTP = '"+L_DOCTP+"' and ";
                        R0_STRSQL += " TX_DOCNO = '"+L_DOCNO+"' and ";
                        R0_STRSQL += " TX_PRDCD = '"+L_PRDCD+"'";
                        int L_RECCNT = getRECCNT(R0_STRSQL);

                        String RR_STRSQL = R1_STRSQL;
                        if(L_RECCNT > 0)
                                RR_STRSQL = R2_STRSQL;
                        //System.out.println(RR_STRSQL);
                        cl_dat.exeSQLUPD(RR_STRSQL,"");
                        cl_dat.exeDBCMT("updTXDOC");
               }
		  catch (Exception L_EX) 
		  {
			  setMSG(L_EX,"updTXDOC");
		  }
        }




        /** In the beginning, Credit claim, Distributor discount, Excise duty were saved into CO_TXSPC
        *   <br> Currently these details are recorded in CO_TXDOC, Howevere this method could be used as guideline for
        *   <br> recording tax details in CO_TXSPC
        */
        private void updTXSPC()
        {
           try
           {
                String L_SYSCD, L_SBSTP, L_DOCTP, L_DOCNO, L_PRDCD, L_TRNTP, L_SBSCD, L_CODCD, L_CODDS, L_CODFL, L_PRCSQ, L_TRNFL, L_STSFL, L_LUSBY, L_LUPDT, L_PRTTP, L_PRTCD, L_CODVL;

                // Default field values setting
                L_SYSCD = "MR";
                L_SBSTP = strMKTTP;
                L_DOCTP = "COR";
                L_DOCNO = strINDNO;
                L_PRDCD = strPRDCD;
                L_TRNTP = "T";                  // Transaction level tax
                L_SBSCD = strZONCD1+strSALTP1+"00";
                L_CODCD = "007";                // code for credit claim
                L_CODDS = "Credit Discount";
                L_CODFL = "A";                  // Amount
                L_PRCSQ = "00";
                L_TRNFL = "0";
                L_STSFL = "";
                L_LUSBY = strLUSBY;
                L_LUPDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(strLUPDT));
                
                // Credit discount for consignee
                if (Double.parseDouble(strCDCVL)>0.00)
                {
                        L_PRTTP = "C";                  // Consignee
                        L_PRTCD = strCNSCD;
                        L_CODVL = strCDCVL;
                        updTXSPC1(L_SYSCD, L_SBSTP, L_DOCTP, L_DOCNO, L_PRDCD, L_TRNTP, L_SBSCD, L_CODCD, L_CODDS, L_CODFL, L_PRCSQ, L_TRNFL, L_STSFL, L_LUSBY, L_LUPDT, L_PRTTP, L_PRTCD, L_CODVL);
                }

                // Credit discount for distributor
                if (Double.parseDouble(strDDCVL)>0.00)
                {
                        L_PRTTP = "D";                  // Distributor
                        L_PRTCD = strDSRCD;
                        L_CODVL = strDDCVL;
                        updTXSPC1(L_SYSCD, L_SBSTP, L_DOCTP, L_DOCNO, L_PRDCD, L_TRNTP, L_SBSCD, L_CODCD, L_CODDS, L_CODFL, L_PRCSQ, L_TRNFL, L_STSFL, L_LUSBY, L_LUPDT, L_PRTTP, L_PRTCD, L_CODVL);
                }

                // Credit discount for third party
                if (Double.parseDouble(strTDCVL)>0.00)
                {
                        L_PRTTP = "C";                  // Third party
                        L_PRTCD = strTDCRF;
                        L_CODVL = strTDCVL;
                        updTXSPC1(L_SYSCD, L_SBSTP, L_DOCTP, L_DOCNO, L_PRDCD, L_TRNTP, L_SBSCD, L_CODCD, L_CODDS, L_CODFL, L_PRCSQ, L_TRNFL, L_STSFL, L_LUSBY, L_LUPDT, L_PRTTP, L_PRTCD, L_CODVL);
                }

                L_PRDCD = "XXXXXXXXXX";
                L_TRNTP = "M";                  // Master level Tax
                L_CODCD = "010";                // code for Excise Duty
                L_CODDS = "Excise Duty";
                L_CODFL = "P";                  // Percentage
                L_PRCSQ = "10";
                // Credit discount for third party
                if (Double.parseDouble(strEXCRT)>0.00)
                {
                        L_PRTTP = "X";                  // Third party
                        L_PRTCD = "XXXXX";
                        L_CODVL = strEXCRT;
                        updTXSPC1(L_SYSCD, L_SBSTP, L_DOCTP, L_DOCNO, L_PRDCD, L_TRNTP, L_SBSCD, L_CODCD, L_CODDS, L_CODFL, L_PRCSQ, L_TRNFL, L_STSFL, L_LUSBY, L_LUPDT, L_PRTTP, L_PRTCD, L_CODVL);
                }
            }
		   catch (Exception L_EX) 
		   {
			   setMSG(L_EX,"updTXSPC");
		   }

        }


        /** In the beginning, Credit claim, Distributor discount, Excise duty were saved into CO_TXSPC
        *   <br> Currently these details are recorded in CO_TXDOC, Howevere this method could be used as guideline for
        *   <br> recording tax details in CO_TXSPC
        */
        private void updTXSPC1(String L_SYSCD, String L_SBSTP, String L_DOCTP, String L_DOCNO, String L_PRDCD, String L_TRNTP, String L_SBSCD, String L_CODCD, String L_CODDS, String L_CODFL, String L_PRCSQ, String L_TRNFL, String L_STSFL, String L_LUSBY, String L_LUPDT, String L_PRTTP, String L_PRTCD, String L_CODVL)
        {
                        String J1    = "','";
                        String J2    = " , ";
                        String R1_STRSQL  = "insert into CO_TXSPC (TXT_SYSCD, TXT_SBSTP, TXT_DOCTP, TXT_DOCNO, TXT_PRDCD, TXT_CODCD, TXT_PRTTP, TXT_PRTCD, TXT_SBSCD, TXT_TRNTP, TXT_CODDS,   TXT_CODVL, TXT_CODFL, TXT_PRCSQ, TXT_TRNFL, TXT_STSFL, TXT_LUSBY, TXT_LUPDT)";
                               R1_STRSQL +=           " values ('"+strMKTTP+J1+ L_SBSTP+J1+L_DOCTP+J1+L_DOCNO+J1+L_PRDCD+J1+L_CODCD+J1+L_PRTTP+J1+L_PRTCD+J1+L_SBSCD+J1+L_TRNTP+J1+L_CODDS+"',"+L_CODVL+",'"+L_CODFL+J1+L_PRCSQ+J1+L_TRNFL+J1+L_STSFL+J1+L_LUSBY+"','"+L_LUPDT+"')";

                        String R2_STRSQL  = "update  CO_TXSPC set ";
                        R2_STRSQL += " TXT_SBSCD = '"+L_SBSCD+"',";
                        R2_STRSQL += " TXT_TRNTP = '"+L_TRNTP+"',";
                        R2_STRSQL += " TXT_CODDS = '"+L_CODDS+"',";
                        R2_STRSQL += " TXT_CODVL =  "+L_CODVL+" ,";
                        R2_STRSQL += " TXT_CODFL = '"+L_CODFL+"',";
                        R2_STRSQL += " TXT_PRCSQ = '"+L_PRCSQ+"',";
                        R2_STRSQL += " TXT_TRNFL = '"+L_TRNFL+"',";
                        R2_STRSQL += " TXT_STSFL = '"+L_STSFL+"',";
                        R2_STRSQL += " TXT_LUSBY = '"+L_LUSBY+"',";
                        R2_STRSQL += " TXT_LUPDT = '"+L_LUPDT+"'  where ";
                        R2_STRSQL += " TXT_SYSCD = '"+L_SYSCD+"' and ";
                        R2_STRSQL += " TXT_SBSTP = '"+L_SBSTP+"' and ";
                        R2_STRSQL += " TXT_DOCTP = '"+L_DOCTP+"' and ";
                        R2_STRSQL += " TXT_DOCNO = '"+L_DOCNO+"' and ";
                        R2_STRSQL += " TXT_PRDCD = '"+L_PRDCD+"' and ";
                        R2_STRSQL += " TXT_CODCD = '"+L_CODCD+"' and ";
                        R2_STRSQL += " TXT_PRTTP = '"+L_PRTTP+"' and ";
                        R2_STRSQL += " TXT_PRTCD = '"+L_PRTCD+"'";
                        String R0_STRSQL = "select count(*) from CO_TXSPC where ";
                        R0_STRSQL += " TXT_SYSCD = '"+L_SYSCD+"' and ";
                        R0_STRSQL += " TXT_SBSTP = '"+L_SBSTP+"' and ";
                        R0_STRSQL += " TXT_DOCTP = '"+L_DOCTP+"' and ";
                        R0_STRSQL += " TXT_DOCNO = '"+L_DOCNO+"' and ";
                        R0_STRSQL += " TXT_PRDCD = '"+L_PRDCD+"' and ";
                        R0_STRSQL += " TXT_CODCD = '"+L_CODCD+"' and ";
                        R0_STRSQL += " TXT_PRTTP = '"+L_PRTTP+"' and ";
                        R0_STRSQL += " TXT_PRTCD = '"+L_PRTCD+"'";
                        int L_RECCNT = getRECCNT(R0_STRSQL);

                        String RR_STRSQL = R1_STRSQL;
                        if(L_RECCNT > 0)
                                RR_STRSQL = R2_STRSQL;
                        //System.out.println(RR_STRSQL);
                        cl_dat.exeSQLUPD(RR_STRSQL,"");
                        cl_dat.exeDBCMT("updTXSPC1"); 
        }


		/** Method to return prodct code for Specified Product Description
		 * @param	LP_PRMST_KEY	Product Description
		 */
        private String getPRMST(String LP_PRMST_KEY)
        {
        String L_RETSTR = "";
        try
        {
                if(!htbPRMST.containsKey(LP_PRMST_KEY))
                        return L_RETSTR;
                String[] staPRMST = (String[])htbPRMST.get(LP_PRMST_KEY);
                L_RETSTR = staPRMST[intAE_PR_PRDCD];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPRMST");
		}
        return L_RETSTR;
        }


		/** Method to return prodct code for Specified Product Description
		 * @param	LP_PRMST_KEY	Product Description
		 */
        private String getPRMST1(String LP_PRMST_KEY)
        {
        String L_RETSTR = "";
        try
        {
                if(!htbPRMST1.containsKey(LP_PRMST_KEY))
                        return L_RETSTR;
                String[] staPRMST1 = (String[])htbPRMST1.get(LP_PRMST_KEY);
                L_RETSTR = staPRMST1[intAE_PR_PRDDS];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPRMST1");
		}
        return L_RETSTR;
        }
		

		/** Picking up D.O. Master related details from Hash Table
		 * <B> for Specified D.O. Master key
		 * @param LP_DOMST_KEY	D.O. master key
		 */
        private void getDOMST(String LP_DOMST_KEY)
        {
        try
        {
                String[] staDOMST = (String[])htbDOMST.get(LP_DOMST_KEY);
                strINDNO = staDOMST[intAE_DO_INDNO];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getDOMST");
		}
        }


		/** Picking up D.O.Transaction related details from Hash Table
		 * <B> for Specified D.O.Transaction key
		 * @param LP_DOTRN_KEY	D.O.Transaction key
		 */
        private void getDOTRN(String LP_DOTRN_KEY)
        {
        try
        {
                String[] staDOTRN = (String[])htbDOTRN.get(LP_DOTRN_KEY);
                strPKGTP = staDOTRN[intAE_DOT_PKGTP];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getDOTRN");
		}
        }



		/** Picking up Specified Codes Transaction related details from Hash Table
		 * <B> for Specified Code Transaction key
		 * @param LP_CDTRN_KEY	Code Transaction key
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM)
        {
        String L_RETSTR = "";
        //System.out.println("getCDTRN:"+LP_CDTRN_KEY+"/"+LP_FLDNM);
        try
        {
				if(!htbCDTRN.containsKey(LP_CDTRN_KEY))
					return L_RETSTR;
                String[] staCDTRN = (String[])htbCDTRN.get(LP_CDTRN_KEY);
                if (LP_FLDNM.equals("CMT_CODDS"))
                        L_RETSTR = staCDTRN[intAE_CMT_CODDS];
                else if (LP_FLDNM.equals("CMT_SHRDS"))
                        L_RETSTR = staCDTRN[intAE_CMT_SHRDS];
                else if (LP_FLDNM.equals("CMT_CHP01"))
                        L_RETSTR = staCDTRN[intAE_CMT_CHP01];
                else if (LP_FLDNM.equals("CMT_CHP02"))
                        L_RETSTR = staCDTRN[intAE_CMT_CHP02];
                else if (LP_FLDNM.equals("CMT_NMP01"))
                        L_RETSTR = staCDTRN[intAE_CMT_NMP01];
                else if (LP_FLDNM.equals("CMT_NMP02"))
                        L_RETSTR = staCDTRN[intAE_CMT_NMP02];
                else if (LP_FLDNM.equals("CMT_NCSVL"))
                        L_RETSTR = staCDTRN[intAE_CMT_NCSVL];
                else if (LP_FLDNM.equals("CMT_CCSVL"))
                        L_RETSTR = staCDTRN[intAE_CMT_CCSVL];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return L_RETSTR;
        }



        /** Procedure to pickup resultset values from VFP Resultset
         * @param	Resultset Name
         * @param	Field name
         * @param	Dtata type of the field
        */
        private String getRSTVAL1(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP)
        {
            //System.out.println("parameters in getRSTVAL1  : "+LP_FLDNM+"   "+LP_FLDTP);
            String L_RETVL = "";
            try
            {
                if (LP_FLDTP.equals("C")||LP_FLDTP.equals("N"))
                {
                        L_RETVL = delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),""));
                }
		else if (LP_FLDTP.equals("D"))
                {
                        //System.out.println("parameters for Date Field  : "+LP_FLDNM+"   "+LP_FLDTP);
                        L_RETVL = M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM));
                        //System.out.println("Value of L_RETVL  : "+L_RETVL);
                        if(L_RETVL.length()==10)
                        {
                        if(L_RETVL.substring(6,10).equals("1899"))
                                L_RETVL="";
                        }
                }
                else if (LP_FLDTP.equals("T"))
                {
                        L_RETVL = M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
                }
            }
            catch (Exception L_EX)
            {
                 setMSG(L_EX,"getRSTVAL1");
            }
        //setMSG(LP_FLDNM+" : "+L_RETVL,'N');
        //System.out.println(LP_FLDNM+" : "+L_RETVL);
        return L_RETVL;
        } 


	



//***************************************************************************
		/** Inserting data into the Control Table (mr_dtitr)
		 */
		
		private void insREFTBL()
        {
               insREFTBL1("01","1","IN_PRTYPE","IN_MKTTP" ,"C");
                insREFTBL1("01","2","IN_INDNO","IN_INDNO" ,"C");
                insREFTBL1("01","x","IN_INDDT","IN_INDDT" ,"D");
                insREFTBL1("01","x","IN_DONO","IN_DORNO" ,"C");
                insREFTBL1("01","x","IN_AMNDNO","IN_AMDNO" ,"C");
                insREFTBL1("01","x","IN_AMNDDT","IN_AMDDT" ,"D");
                insREFTBL1("01","x","IN_BOOKDT","IN_BKGDT" ,"D");
                insREFTBL1("01","x","IN_SALTYPE","IN_SALTP1" ,"X");
                insREFTBL1("01","x","IN_DTPCD","IN_DTPCD" ,"C");
                insREFTBL1("01","x","IN_DTPDS","IN_DTPDS" ,"C");
                insREFTBL1("01","x","IN_PONO","IN_PORNO" ,"C");
                insREFTBL1("01","x","IN_PODATE","IN_PORDT" ,"D");
                insREFTBL1("01","x","IN_ZONE","IN_ZONCD1" ,"X");
                insREFTBL1("01","x","IN_PARTSH","IN_PSHFL" ,"C");
                insREFTBL1("01","x","IN_CONSIGNEE","IN_CNSCD" ,"C");
                insREFTBL1("01","x","IN_BUYER","IN_BYRCD" ,"C");
                insREFTBL1("01","x","IN_CURRENCY","IN_CURCD" ,"C");
                insREFTBL1("01","x","IN_EXCHRATE","IN_ECHRT" ,"N");
                insREFTBL1("01","x","IN_DISTRIBTR","IN_DSRCD" ,"C");

                insREFTBL1("02","1","IN_PRTYPE","IN_MKTTP" ,"C");
                insREFTBL1("02","2","IN_INDNO","IN_INDNO" ,"C");
                insREFTBL1("02","x","IN_FAXNO","IN_FAXNO" ,"C");
                insREFTBL1("02","x","IN_FAXPLACE","IN_FAXLC" ,"C");
                insREFTBL1("02","x","IN_PMTACCT","IN_APTVL" ,"N");
                insREFTBL1("02","x","IN_PMTCUST","IN_CPTVL" ,"N");
                insREFTBL1("02","x","IN_PMTTYPE","IN_PMTCD" ,"C");
                insREFTBL1("02","x","IN_STXCD","IN_STXCD" ,"C");
                insREFTBL1("02","x","IN_STXRT","IN_STXRT" ,"N");
                insREFTBL1("02","x","IN_OCTCD","IN_OCTCD" ,"C");
                insREFTBL1("02","x","IN_OCTRT","IN_OCTRT" ,"N");
                insREFTBL1("02","x","IN_SVCCD","IN_SVCCD" ,"C");
                insREFTBL1("02","x","IN_SVCRT","IN_SVCRT" ,"N");
                insREFTBL1("02","x","IN_STATUS","IN_STSFL1" ,"X");
                insREFTBL1("02","x","IN_LUID","IN_LUSBY" ,"C");
                insREFTBL1("02","x","IN_LUPD" ,"XX_LUPDT","X");

 
                insREFTBL1("03","1","INT_PRTYPE","INT_MKTTP" ,"C");
                insREFTBL1("03","2","INT_INDNO","INT_INDNO" ,"C");
                insREFTBL1("03","3","INT_GRADECD","INT_PRDDS" ,"C");
                //insREFTBL1("03","x","INT_PRDCD","INT_PRDCD" ,"X");
                insREFTBL1("03","x","INT_PRODCD","INT_PRODCD" ,"X");
                insREFTBL1("03","x","INT_PKGTYPE","INT_PKGTP" ,"C");
                insREFTBL1("03","x","INT_AMNDNO","INT_AMDNO" ,"C");
                insREFTBL1("03","x","INT_PKGS","INT_INDPK" ,"N");
                insREFTBL1("03","x","INT_PKGWT","INT_PKGWT" ,"N");
                insREFTBL1("03","x","INT_INDQTY","INT_REQQT" ,"N");
                insREFTBL1("03","x","INT_SUOM","INT_STDUM" ,"C");
                insREFTBL1("03","x","INT_OUOM","INT_ORDUM" ,"C");
                insREFTBL1("03","x","INT_EUSCD","INT_EUSCD" ,"C");
                insREFTBL1("03","x","INT_CNVFACT","INT_CNVFT" ,"N");
                insREFTBL1("03","x","INT_AUTHQTY","INT_INDQT" ,"N");
                insREFTBL1("03","x","INT_BASRATE","INT_BASRT" ,"N");
                insREFTBL1("03","x","INT_RATEPER","INT_RTPVL" ,"N");
                insREFTBL1("03","x","INT_STATUS","INT_STSFL1" ,"X");
				
                insREFTBL1("04","1","INT_PRTYPE","INT_MKTTP" ,"C");
                insREFTBL1("04","2","INT_INDNO","INT_INDNO" ,"C");
                insREFTBL1("04","3","INT_GRADECD","substr(INT_PRDDS,1,11)" ,"C");
                insREFTBL1("04","x","INT_AUTHBY","INT_AUTBY" ,"C");
                insREFTBL1("04","x","INT_DOQTY","INT_DORQT" ,"N");
            //  insREFTBL1("04","x","INT_LADQT" ,"INT_LADQTY","N");
                insREFTBL1("04","x","INT_INVQTY" ,"INT_INVQT","N");
                insREFTBL1("04","x","INT_FCQTY","INT_FCMQT" ,"N");
                insREFTBL1("04","x","INT_EXCDUTY","INT_EXCRT" ,"N");
                insREFTBL1("04","x","INT_CDCNT","INT_CC1VL" ,"N");
                insREFTBL1("04","x","INT_DDCNT","INT_CC2VL" ,"N");
                insREFTBL1("04","x","INT_TDCNT","INT_CC3VL" ,"N");
                insREFTBL1("04","x","INT_TDCONS","INT_CC3RF" ,"X");
                insREFTBL1("04","x","INT_LUID","INT_LUSBY" ,"C");
                insREFTBL1("04","x","INT_LUPD","XX_LUPDT" ,"X");
              //insREFTBL1("04","x","INT_SBSCD","INT_SBSCD" ,"X");
              //insREFTBL1("04","x","INT_RESRF","INT_RESRF" ,"X");
                
                insREFTBL1("05","1","DOT_PRTYPE","DOT_MKTTP" ,"C");
                insREFTBL1("05","2","DOT_DONO","DOT_DORNO" ,"C");
                insREFTBL1("05","3","DOT_GRADECD","DOT_PRDDS" ,"C");
                insREFTBL1("05","x","DOT_PRODCD","DOT_PRODCD" ,"X");
                insREFTBL1("05","x","DOT_PKGTYPE","DOT_PKGTP" ,"C");
                insREFTBL1("05","x","DOT_PKGS","DOT_DORPK" ,"N");
                insREFTBL1("05","x","DOT_PKGWT","DOT_PKGWT" ,"N");
                insREFTBL1("05","x","DOT_QTY","DOT_DORQT" ,"N");
                insREFTBL1("05","x","DOT_OUOM","DOT_ORDUM" ,"X");
                insREFTBL1("05","x","DOT_LOTREF","DOT_LOTRF" ,"C");
                //insREFTBL1("05","x","DOT_LADQT" ,"DOT_LADQTY","N");
                //insREFTBL1("05","x","DOT_INVQT" ,"DOT_INVQTY","N");
                insREFTBL1("05","x","DOT_FCQTY","DOT_FCMQT" ,"N");
                insREFTBL1("05","x","DOT_DELTYPE","DOT_DELTP" ,"C");
              //insREFTBL1("05","x","XX_TRNFL","DOT_TRNFL" ,"X");
                insREFTBL1("05","x","DOT_STATUS","DOT_STSFL1" ,"X");
                insREFTBL1("05","x","DOT_LUID","DOT_LUSBY" ,"C");
                insREFTBL1("05","x","DOT_LUPD" ,"XX_LUPDT","X");

                insREFTBL1("06","1","DO_PRTYPE","DOT_MKTTP" ,"C");
                insREFTBL1("06","2","DO_NO","DOT_DORNO" ,"C");
                insREFTBL1("06","x","DO_DATE","DOT_DORDT" ,"D");
                insREFTBL1("06","x","DO_AMNDNO","DOT_AMDNO" ,"C");
                insREFTBL1("06","x","DO_AMNDDT","DOT_AMDDT" ,"D");
                insREFTBL1("06","x","DO_INDNO","DOT_INDNO" ,"X");
                insREFTBL1("06","x","DO_TRANSPTR","DOT_TRPCD" ,"C");
                insREFTBL1("06","x","DO_TRANSMODE","DOT_TMOCD" ,"C");
                insREFTBL1("06","x","DO_DESPFROM","DOT_DLCCD" ,"C");
                insREFTBL1("06","x","DO_FRTRT","DOT_FRTRT" ,"N");
                insREFTBL1("06","x","DO_LORRYNO","DOT_LRYNO" ,"C");
                insREFTBL1("06","x","DO_AUTHBY","DOT_AUTBY" ,"C");
                insREFTBL1("06","x","DO_AUTHDT","DOT_AUTDT" ,"D");
                insREFTBL1("06","x","DO_STATUS","DOT_STSFL2" ,"X");
                insREFTBL1("06","x","DO_LUID","DOT_LUSBY" ,"C");
                insREFTBL1("06","x","DO_LUPD" ,"XX_LUPDT","X");
                //insREFTBL1("06","x","DOT_SBSCD","DOT_SBSCD" ,"X");
                
                insREFTBL1("07","1","DOD_PRTYPE","DOD_MKTTP" ,"C");
                insREFTBL1("07","2","DOD_DONO","DOD_DORNO" ,"C");
                insREFTBL1("07","3","DOD_GRADECD","DOD_PRDDS" ,"X");
                insREFTBL1("07","4","DOD_SRLNO","DOD_SRLNO" ,"C");
                insREFTBL1("07","x","DOD_PRDCD","DOD_PRDCD" ,"C");
                insREFTBL1("07","x","DOD_PRODCD","DOD_PRODCD" ,"X");
                insREFTBL1("07","x","DOD_DSPDT","DOD_DSPDT" ,"D");
                insREFTBL1("07","x","DOD_PKGTP","DOD_PKGTP" ,"C");
                //insREFTBL1("07","x","DOD_LADNO","DOD_LADNO" ,"C");
                //insREFTBL1("07","x","DOD_LADDT","DOD_LADDT" ,"D");
                //insREFTBL1("07","x","DOD_LAQTY","DOD_LADQT" ,"N");
                insREFTBL1("07","x","DOD_QTY","DOD_DORQT" ,"N");
                insREFTBL1("07","x","DOD_STATUS","DOD_STSFL1" ,"X");
                insREFTBL1("07","x","DOD_DELTYPE","DOD_DELTP" ,"C");
                //insREFTBL1("07","x","DOD_TRNFL" ,"XX_TRNFL","X");
                insREFTBL1("07","x","DOD_LUID","DOD_LUSBY" ,"C");
                insREFTBL1("07","x","DOD_LUPD" ,"XX_LUPDT","X");
                //insREFTBL1("07","x","DOD_SBSCD","DOD_SBSCD" ,"X");

                insREFTBL1("08","1","CT_TYPE","PT_PRTTP" ,"C");
                insREFTBL1("08","2","CT_CODE","PT_PRTCD" ,"C");
                insREFTBL1("08","x","CT_NAME","PT_PRTNM" ,"C");
                insREFTBL1("08","x","CT_SHNAME","PT_SHRNM" ,"C");
                insREFTBL1("08","x","CT_GROUP","PT_GRPCD" ,"X");
                insREFTBL1("08","x","CT_ADDR1","PT_ADR01" ,"C");
                insREFTBL1("08","x","CT_ADDR2","PT_ADR02" ,"C");
                insREFTBL1("08","x","CT_ADDR3","PT_ADR03" ,"C");
                insREFTBL1("08","x","CT_ADDR4","PT_ADR04" ,"C");
                insREFTBL1("08","x","CT_CITY","PT_CTYNM" ,"C");
                insREFTBL1("08","x","CT_PINCODE","PT_PINCD" ,"C");
                insREFTBL1("08","x","CT_STATCD","PT_STACD" ,"C");
                insREFTBL1("08","x","CT_CNTRYCD","PT_CNTCD" ,"C");
                insREFTBL1("08","x","CT_CONTACT","PT_CONNM" ,"C");
                insREFTBL1("08","x","CT_TEL_NO1","PT_TEL01" ,"C");
                insREFTBL1("08","x","CT_TEL_NO2","PT_TEL02" ,"C");
                insREFTBL1("08","x","CT_FAX","PT_FAXNO" ,"C");
                insREFTBL1("08","x","CT_EMAILID","PT_EMLRF" ,"C");
                insREFTBL1("08","x","CT_IORF","PT_INFFL" ,"C");
                insREFTBL1("08","x","CT_ST_NO","PT_STXNO" ,"C");
                insREFTBL1("08","x","CT_ST_WEF","PT_STXDT" ,"D");
                insREFTBL1("08","x","CT_CLASS","PT_CLSCD" ,"C");
                insREFTBL1("08","x","CT_SCORE","PT_SCRCD" ,"C");

                insREFTBL1("09","1","CT_TYPE","PT_PRTTP" ,"C");
                insREFTBL1("09","2","CT_CODE","PT_PRTCD" ,"C");
                insREFTBL1("09","x","CT_CST_NO","PT_CSTNO" ,"C");
                insREFTBL1("09","x","CT_CST_WEF","PT_CSTDT" ,"D");
                insREFTBL1("09","x","CT_TINNO","PT_TINNO" ,"C");
                insREFTBL1("09","x","CT_TINDT","PT_TINDT" ,"D");
                insREFTBL1("09","x","CT_ECC","PT_ECCNO" ,"C");
                insREFTBL1("09","x","CT_ITPANNO","PT_ITPNO" ,"C");
                insREFTBL1("09","x","CT_EXCNO","PT_EXCNO" ,"C");
                insREFTBL1("09","x","CT_RANGE","PT_RNGDS" ,"C");
                insREFTBL1("09","x","CT_DIVISION","PT_DIVDS" ,"C");
                insREFTBL1("09","x","CT_CLLECTR","PT_CLLDS" ,"C");
                insREFTBL1("09","x","CT_ZONE","PT_ZONCD" ,"C");
                insREFTBL1("09","x","CT_YROPCR","PT_YOPCR" ,"N");
                insREFTBL1("09","x","CT_SALAMT","PT_SALVL" ,"N");
                insREFTBL1("09","x","CT_DISTREF","PT_DSRCD" ,"C");
                insREFTBL1("09","x","CT_TRANREF","PT_TRNCD" ,"C");
                insREFTBL1("09","x","CT_OBTAG","PT_STSFL" ,"C");
                insREFTBL1("09","x","CT_TSTTAG","PT_TSTFL" ,"C");
                insREFTBL1("09","x","CT_LUPD","XX_LUPDT" ,"X");
                insREFTBL1("09","x","CT_REGDT","PT_APRDT" ,"D");
                insREFTBL1("09","x","CT_LUID","PT_LUSBY" ,"C");

		
                insREFTBL1("10","1","GR_PRODCD","PR_PRODCD" ,"X");
                insREFTBL1("10","2","GR_GRADECD","PR_PRDDS" ,"C");
                insREFTBL1("10","x","GR_PRTYPE","PR_PRDTP" ,"X");
                insREFTBL1("10","x","GR_PRDCD","PR_PRDCD" ,"C");
                insREFTBL1("10","x","GR_RATE","PR_AVGRT" ,"N");
                insREFTBL1("10","x","GR_DESC","PR_PRDDS" ,"C");
                insREFTBL1("10","x","GR_UOM","PR_UOMCD" ,"C");
                insREFTBL1("10","x","GR_EXCDUTY","PR_EXCDUTY" ,"X");
                insREFTBL1("10","x","GR_AVGRT","PR_AVGRT" ,"X");
                insREFTBL1("10","x","GR_QLTYTAG","PR_QLTYTAG" ,"X");
                insREFTBL1("10","x","GR_OBSTAG","PR_OBSTAG" ,"X");
                insREFTBL1("10","x","GR_PKGWT","PR_PKGWT" ,"X");
                insREFTBL1("10","x","GR_LUPD","XX_LUPDT" ,"X");
                insREFTBL1("10","x","GR_LUID","PR_LUSBY" ,"C");
		}

	/** Sub-method for inserting data into control table (mr_dtitr)
	*/
   public void insREFTBL1(String LP_TRFNO, String LP_KEYVL, String LP_FLD01, String LP_FLD02, String LP_FLDTP)
   {
        strINSSTR1 = "";
        strINSSTR2 = "";
	try {
        setINSSTR("DT_TRFNO",LP_TRFNO,"C");
        setINSSTR("DT_KEYVL",LP_KEYVL,"C");
        setINSSTR("DT_FLD01",LP_FLD01,"C");
        setINSSTR("DT_FLD02",LP_FLD02,"C");
        setINSSTR_L("DT_FLDTP",LP_FLDTP,"C");
        M_strSQLQRY = "insert into mr_dtitr ("+strINSSTR1+") values ("+strINSSTR2+")";
        setMSG(LP_FLD01+"/"+LP_FLD02,'N');
        cl_dat.exeSQLUPD(M_strSQLQRY,"");
        cl_dat.exeDBCMT("insREFTBL1");
	}
	catch (Exception L_EX) {
                setMSG(L_EX,"exeREFTBL1");
        }
   }


/** Generating string for Insertion Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private void setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 
{
try 
{
        //System.out.println("LP_FLDNM / LP_FLDVL / LP_FLDTP :"+LP_FLDNM+"/"+LP_FLDVL+"/"+LP_FLDTP);
        strINSSTR1 += LP_FLDNM + ",";
	if (LP_FLDTP.equals("C"))
        {
                if (LP_FLDVL.toUpperCase().equals("NULL"))
                        strINSSTR2 += "null,";
                else if (LP_FLDVL.toUpperCase().equals("DATE()"))
                        strINSSTR2 += "date(),";
                else
                        strINSSTR2 += "'"+nvlSTRVL(LP_FLDVL,"")+"',";
                    
        }
	else if (LP_FLDTP.equals("N")) 
	{
		strINSSTR2 += nvlSTRVL(LP_FLDVL,"0") + ",";
    }
	else if (LP_FLDTP.equals("D"))
           {

                //System.out.println("LP_FLDNM / LP_FLDVL / LP_FLDTP (for Date) :"+LP_FLDNM+"/"+LP_FLDVL+"/"+LP_FLDTP);
                String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL));

                //System.out.println("L_CHKDT : "+L_CHKDT+"  "+L_CHKDT.length());
                if(L_CHKDT.length() >= 10)
                        strINSSTR2 +=  "ctod('"+L_CHKDT+"'),";
                else
                        strINSSTR2 += " ctod('  /  /    '),";
           }
	else if (LP_FLDTP.equals("T"))
           {
                String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL));
                if(L_CHKDT.length() > 10)
                        strINSSTR2 +=  "ctot('"+L_CHKDT+"'),";
                else
                        strINSSTR2 += "ctot('  /  /    '),";
           }
    }
    catch (Exception L_EX) 
	{
                setMSG(L_EX,"setINSSTR/"+LP_FLDNM);
    }
    //System.out.println("strINSSTR1 : "+strINSSTR1);
    //System.out.println("strINSSTR2 : "+strINSSTR2);
}



/** Generating last part of the string for Insertion Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private void setINSSTR_L(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try
    {
        strINSSTR1 += LP_FLDNM ;
	if (LP_FLDTP.equals("C"))
        {
                if (LP_FLDVL.toUpperCase().equals("NULL"))
                        strINSSTR2 += "null";
                else if (LP_FLDVL.toUpperCase().equals("DATE()"))
                        strINSSTR2 += "date()";
                else
                        strINSSTR2 += "'"+nvlSTRVL(LP_FLDVL,"")+"'";
        }
	else if (LP_FLDTP.equals("N")) 
	{
            strINSSTR2 += nvlSTRVL(LP_FLDVL,"0") ;
    }
	else if (LP_FLDTP.equals("D"))
           {
                String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL));
                if(L_CHKDT.length() > 10)
                        strINSSTR2 += "ctod('"+L_CHKDT+"'),";
                else
                        strINSSTR2 += "ctod('  /  /    '),";
                strINSSTR2 += L_CHKDT;
           }
	else if (LP_FLDTP.equals("T")) 
	{
                String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL));
                if(L_CHKDT.length() > 10)
                        strINSSTR2 += "ctot('"+L_CHKDT+"'),";
                else
                        strINSSTR2 += "ctot('  /  /    '),";
    }
    //System.out.println("strINSSTR1 : "+strINSSTR1);
    //System.out.println("strINSSTR2 : "+strINSSTR2);
    }
    catch (Exception L_EX)
    {
                setMSG(L_EX,"setINSSTR/"+LP_FLDNM);
    }
}
	

/** Generating string for Updation Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 * @param	LP_UPDTP	Type of updation,  For where condition / for Upsdation string
 */
private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP, String LP_UPDTP)
{
      String L_JOINCHR = " , ";
      String L_RETSTR = "";
      try
      {
        if (LP_UPDTP.toUpperCase().equals("W"))
               L_JOINCHR = " and ";
	if (LP_FLDTP.equals("C"))
        {
                
                if (LP_FLDVL.toUpperCase().equals("NULL"))
                        L_RETSTR += LP_FLDNM + " = null" + L_JOINCHR;
                else if (LP_FLDVL.toUpperCase().equals("DATE()"))
                        L_RETSTR += LP_FLDNM + " = date() " + L_JOINCHR;
                else
                        L_RETSTR += LP_FLDNM + " = '" + nvlSTRVL(LP_FLDVL,"") + "'" + L_JOINCHR;
        } 
	else if (LP_FLDTP.equals("N"))
        {
                L_RETSTR += LP_FLDNM + " = " + nvlSTRVL(LP_FLDVL,"0")  + L_JOINCHR;
        } 
	else if (LP_FLDTP.equals("D"))
        {
                String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL));
                if(L_CHKDT.length() >= 10)
                        L_RETSTR += LP_FLDNM + " = ctod('" + L_CHKDT + "')"+  L_JOINCHR;
                else
                        L_RETSTR += LP_FLDNM + " = ctod('  /  /    ') " + L_JOINCHR;
        } 
	else if (LP_FLDTP.equals("T"))
        {
                String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL));
                if(L_CHKDT.length() > 10)
                        L_RETSTR += LP_FLDNM + " = ctod('" + L_CHKDT + "')"+  L_JOINCHR;
                else
                        L_RETSTR += LP_FLDNM + " = ctod('  /  /    ') " + L_JOINCHR;
        } 
      } 
      catch (Exception L_EX)
      {
                setMSG(L_EX,"exeUPDSTR");
      } 
	  //System.out.println("setUPDSTR : "+LP_FLDNM+"/"+L_RETSTR+"/"+LP_UPDTP);
      return L_RETSTR;
}

        public int getRECCNT(String LP_SQLSTR){
		int L_RETVAL = -1;
		try{
                        M_rstRSSET = cl_dat.exeSQLQRY1(LP_SQLSTR);
                        if(M_rstRSSET.next())
                          L_RETVAL = M_rstRSSET.getInt(1);
                        M_rstRSSET.close();
	    }catch (Exception L_EX){
			setMSG(L_EX,"getRECCNT");				
	        L_RETVAL = -1;
	       }
		return L_RETVAL;
	}


		
        public int getRECCNT1(String LP_SQLSTR){
		int L_RETVAL = -1;
		try{
                        ResultSet L_rstRSSET = stmSPBKQ.executeQuery(LP_SQLSTR);
	                        if(L_rstRSSET.next())
							{
								L_RETVAL = L_rstRSSET.getInt(1);
								conSPBKA.commit();
								L_rstRSSET.close();
							}
	    }catch (Exception L_EX){
			setMSG(L_EX,"getRECCNT1");				
	        L_RETVAL = -1;
	       }
		return L_RETVAL;
	}
		
		
		
/** Executing query on foxpro table
 * @param	LP_SQLVAL	Query to be executed
 */
public ResultSet exeBKPQRY(String LP_SQLVAL)
{
        M_rstRSSET = null;
	try
	{
          if(conSPBKA != null)
			M_rstRSSET = stmSPBKQ.executeQuery(LP_SQLVAL);
	}
	catch(Exception L_SE)
	{
		  setMSG(L_SE,"exeBKPQRY");
	}
          return M_rstRSSET;
}


/** Executing query on foxpro table, additional provision
 * @param	LP_SQLVAL	Query to be executed
 */
public ResultSet exeBKPQRY1(String LP_SQLVAL){
        ResultSet L_rstRSSET = null;
	try{
                if(conSPBKA != null)
                        M_rstRSSET = stmSPBKQ1.executeQuery(LP_SQLVAL);
		}catch(Exception L_SE){
		  setMSG(L_SE,"exeBKPQRY1");
		}
                return M_rstRSSET;
	}


/** Establishing connection to Foxpro Table
 * @param	LP_PTHWD	Path for foxpro table
 */
   public Connection setCONFTB(String LP_PTHWD)
   {
		String L_URLSTR ="";
                L_URLSTR = "jdbc:odbc:Visual FoxPro Tables;SourceDB = " + LP_PTHWD;
		try
                {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                        conSPBKA = DriverManager.getConnection(L_URLSTR,"","");
                        stmSTBKA = chkCONSTM1(conSPBKA);
                        stmSPBKQ = chkCONSTM1(conSPBKA);
                        stmSPBKQ1 = chkCONSTM1(conSPBKA);
                }
                catch(ClassNotFoundException L_CNFE)
                {
			setMSG(L_CNFE,"setCONFTB");
                }
                catch(SQLException L_SQLE)
                {
                       setMSG(LP_PTHWD+": Database not found"+L_SQLE.toString(),'E');
                }
                return conSPBKA;
   }


   
/** Establishing connection to Foxpro Database
 * @param	LP_DBSNM	Database Name
 * @param	LP_DBSLC	Location / path for Database
 */
        public   Connection setCONFTB1(String LP_DBSNM,String LP_DBSLC)
        {
		String L_URLSTR ="";
                L_URLSTR = "jdbc:odbc:Visual FoxPro Database;SourceDB = " + LP_DBSLC+"\\"+LP_DBSNM+".DBC";
		try
                {
                        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                        conSPBKA = DriverManager.getConnection(L_URLSTR,"","");
                        stmSTBKA  = chkCONSTM1(conSPBKA);
                        stmSPBKQ  = chkCONSTM1(conSPBKA);
                        stmSPBKQ1  = chkCONSTM1(conSPBKA);
                          
                }
                catch(ClassNotFoundException L_CNFE)
                {
                       setMSG("Error in setCONFTB : "+L_CNFE.toString(),'E');
                }
                catch(SQLException L_SQLE)
                {
					setMSG("Database not found"+L_SQLE.toString(),'E');
                }
				setMSG("URL "+L_URLSTR+"  Connection : "+conSPBKA,'N');
                return conSPBKA;
   }
   
   
   
/** Verifying connection and creating statement for Foxpro Table
 * @param	LP_CONVAL	Connection Object
 */
        public Statement chkCONSTM1(Connection LP_CONVAL)
        {
                Statement L_stmSPXXA = null;
		try
                {
			if (LP_CONVAL != null)
                        {
                           LP_CONVAL.setAutoCommit(false);
                           L_stmSPXXA = LP_CONVAL.createStatement();
			}
		}
                catch(Exception L_EX){}
                return L_stmSPXXA;             
	}




/** Generating last part of string for Updation Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 * @param	LP_UPDTP	Type of updation,  For where condition / for Upsdation string
 */
private String setUPDSTR_L(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP, String LP_UPDTP)
{
	String L_RETSTR = "";
	try
	{
		if (LP_FLDTP.equals("C"))
		{
		                
			if (LP_FLDVL.toUpperCase().equals("NULL"))
				L_RETSTR += LP_FLDNM + " = null";
			else if (LP_FLDVL.toUpperCase().equals("DATE()"))
				L_RETSTR += LP_FLDNM + " = date()";
			else
				L_RETSTR += LP_FLDNM + " = '" + nvlSTRVL(LP_FLDVL,"") + "' ";
		}
		else if (LP_FLDTP.equals("N"))
		{
			L_RETSTR += LP_FLDNM + " = " + nvlSTRVL(LP_FLDVL,"0") +" ";
		}
		else if (LP_FLDTP.equals("D"))
		{
			String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL));
			if(L_CHKDT.length() >= 10)
				L_RETSTR += LP_FLDNM + " = '" + L_CHKDT + "'";
			else
				L_RETSTR += LP_FLDNM + " = null " ;
		}
		else if (LP_FLDTP.equals("T"))
		{
			String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtLCDTM.format(LP_FLDVL);
			if(L_CHKDT.length() > 10)
				L_RETSTR += LP_FLDNM + " = '" + L_CHKDT + "'";
			else
				L_RETSTR += LP_FLDNM + " = null " ;
		}
	}
	catch (Exception L_EX)
	{
	//showEXMSG(L_EX,"setUPDSTR_L","");
	}
// setMSG("strUPDSTR : "+strUPDSTR,'E');
return L_RETSTR;
}

public ResultSet exeSQLQRYTMP(String LP_SQLVAL)
{
	try
	{
                if (cl_dat.M_conSPDBA_pbst != null)
                        rstTEMP = stmTEMP.executeQuery(LP_SQLVAL);
	}
        catch(Exception L_SE)
        {
                System.out.println("Error in exeSQLQRYTMP : "+L_SE.toString());
		System.out.println("QUERY Failed: "+LP_SQLVAL);
        }
        return rstTEMP;
}


	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		try
		{
			super.clrCOMP();
			txtMKTTP.setText("01");
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));
			M_calLOCAL.add(Calendar.DATE,-1);
			txtDOCDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
			txtDOCTP.setText("IN");
		}
	catch (Exception L_EX)
	{
	  setMSG(L_EX,"clrCOMP");
	}
	}


void exeSAVE()
{}

	public static DataOutputStream crtDTOUTSTR(FileOutputStream outfile)
	{
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}
	public static FileOutputStream crtFILE(String strFILE)
	{
		FileOutputStream outFILE = null;
		try
		{
			File file = new File(strFILE);
			outFILE = new FileOutputStream(file);
		   	return outFILE;
		}
		catch(IOException L_IO)
		{
			System.out.println("L_IO FOS...........:"+L_IO);
			return outFILE;		
		}
	}




}

