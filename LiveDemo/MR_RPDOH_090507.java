import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.lang.*;
import java.util.Properties;
import java.util.Date; 
import java.util.Hashtable;
import java.io.*; 

class mr_rpdoh extends cl_rbase
{
	public JTextField txtFMDOR;
	//public JTextField txtTODOR;
	public JTextField txtPRDTP;

	private final String strFILNM = cl_dat.M_strREPSTR_pbst+"mr_rpdoh.htm"; 
	
	private FileOutputStream fosREPORT;
      private DataOutputStream dosREPORT;
	private Hashtable hstPKGTP;
	public boolean flgOUTPRT = true;
	public boolean flgEOFFL = false;
	private Hashtable hstCDTRN;			// Details of all codes used in program
	private Hashtable hstTXDOC;			// Doc.wise Tax Details
	
	private int intGRDCT =0;			// Line counter for Grade Record

	private String strISON1;
	private String strISON2;
	private String strISON3;
	private String strREMDS;
      private String strMODDS;
	private String strAUTBY;
	private String strAUTDT;
	public String strFAXNO;
	public String strTRPNM;
	public String strTREML;
	private String strINDNO;
	private String strDORNO;
	private String strPRDCD;
	private String strPKGTP;
	private String strDORNO_OLD;
	private String strPRDCD_OLD;
	private String strPKGTP_OLD;
	
	private String strEXCNO;
	private String strRNGDS;
	private String strDIVDS;
	private String strCLLDS;
	private String strITPNO;
	private String strTSTFL;
	private String strTINNO;
	
	/** Array elements for records picked up from Code Transactoion */
	private int intCDTRN_TOT = 9;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
	
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	
	private String strTX_SYSCD;
	private String strTX_SBSTP;
	private String strTX_DOCTP;
	private String strTX_DOCNO;
	private String strTX_PRDCD;
	
	boolean flgCOTAX = true;
	boolean flgTAXPR = false;
	boolean flgDODEL = true;
	int intTXLIN = 1, intTXFLD = 0;
	int intTXFLD_TOT = 8;
	String[] arrTXFLD = new String[] {"EXC","EDC","EHC","TOT","CST","STX","SCH","VAT","HIC","OTH"};  // Array of taxes applicable for Marketing System
//	String[] arrTXFLD = new String[] {"EXC","EDC","SCH","TOT","CST","STX"};
	
	/** Array elements for Doc.wise Tax detail */
	private int intTXDOC_TOT = 16;
    private int intAE_TX_EXCVL = 0;
    private int intAE_TX_EXCFL = 1;
    private int intAE_TX_EDCVL = 2;
    private int intAE_TX_EDCFL = 3;
    private int intAE_TX_SCHVL = 4;
    private int intAE_TX_SCHFL = 5;
    private int intAE_TX_TOTVL = 6;
    private int intAE_TX_TOTFL = 7;
    private int intAE_TX_CSTVL = 8;
    private int intAE_TX_CSTFL = 9;
    private int intAE_TX_STXVL = 10;
    private int intAE_TX_STXFL = 11;
    private int intAE_TX_VATVL = 12;
    private int intAE_TX_VATFL = 13;
    private int intAE_TX_EHCVL = 14;
    private int intAE_TX_EHCFL = 15;

  //  private String strADRLN1 = "Bldg. No. 11, 5th Floor,Solitair Corporate Park, Chakala Andheri(E) Mumbai-400093 ";
  //  private String strADRLN2 = "Tel : 91-22-67091900 - 04   Fax 67091927 / 26 / 25 /28   E-mail cms@spl.co.in";
	private String strADRLN1 =     "Ammullavoyil Village,Andarkuppam P.O. Manali New Town,Chennai 600103";
	private String strADRLN2  =	  "91-44-25943457/25940468 Fax 914425941442 email shinho_giasmd02@dataone.in";
	mr_rpdoh()
	{
		super(2);
		M_txtFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		M_lblFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_txtTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_lblTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblTODAT);
		setMatrix(20,8);
		
		hstTXDOC = new Hashtable();
		hstCDTRN = new Hashtable();

		hstCDTRN.clear();
		crtCDTRN("'SYSCOXXTAX'","",hstCDTRN);
		add(new JLabel("Product Catagory"),2,5,1,2.3,this,'R');
		add(txtPRDTP=new JTextField(),2,5,1,1,this,'L');
		add(new JLabel("D.O. Number"),3,5,1,2.3,this,'R');
		add(txtFMDOR=new JTextField(),3,5,1,1,this,'L');
		
		//add(new JLabel("To D.O. Number"),4,5,1,2.3,this,'R');
		//add(txtTODOR=new JTextField(),4,5,1,1,this,'L');
		
		try
		{
			hstPKGTP = new Hashtable(4,0.5f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'FGXXPKG' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Pkgtp In Constructor");
		}
	
		setENBL(false);
		}
		
	mr_rpdoh(String LP_SBSLS)
	{
		super(2);
		M_strSBSLS = LP_SBSLS;	
		M_txtFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		M_lblFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_txtTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_lblTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblTODAT);

		hstTXDOC = new Hashtable();
		hstCDTRN = new Hashtable();

		hstCDTRN.clear();
		crtCDTRN("'SYSCOXXTAX'","",hstCDTRN);
		
		setMatrix(20,8);
		add(new JLabel("Product Catagory"),2,5,1,2.3,this,'R');
		add(txtPRDTP=new JTextField(),2,5,1,1,this,'L');
		add(new JLabel("D.O. Number"),3,5,1,2.3,this,'R');
		add(txtFMDOR=new JTextField(),3,5,1,1,this,'L');
		
		//add(new JLabel("To D.O. Number"),4,5,1,2.3,this,'R');
		//add(txtTODOR=new JTextField(),4,5,1,1,this,'L');
		
		try
		{
			hstPKGTP = new Hashtable(4,0.5f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'FGXXPKG' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Pkgtp In Constructor");
		}

		
		setENBL(false);
	}
	
	/**
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst))
			{
				txtPRDTP.requestFocus();
			}
		}
	}
	
	/**
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(L_KE.getSource().equals(txtPRDTP))
				{
					String L_ARRHDR[] = {"Type.","DESC"};
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
						+"CMT_CGMTP = 'MST' AND CMT_CGSTP = 'COXXMKT' ORDER BY CMT_CODCD";
					M_strHLPFLD = "txtPRDTP";
					cl_hlp(M_strSQLQRY, 2,1,new String []{"Type" ,"Description "},2,"CT");
				}
				else if(L_KE.getSource().equals(txtFMDOR))
				{
					M_strHLPFLD = "txtFMDOR";
					M_strSQLQRY = "SELECT DISTINCT DOT_DORNO,DOT_DORDT,PT_PRTNM,DOT_AMDNO,"
						+"DOT_AMDDT,DOT_STSFL,DOT_LUPDT FROM MR_DOTRN,MR_INMST,CO_PTMST WHERE "
						+"DOT_MKTTP = '"+txtPRDTP.getText()+"' and DOT_MKTTP = IN_MKTTP AND DOT_INDNO = IN_INDNO AND IN_BYRCD = PT_PRTCD "
						+"AND PT_PRTTP = 'C' AND IN_STSFL IN ('A','U','1','2','3','4','X') and IN_SBSCD in "+M_strSBSLS+" and DOT_SBSCD in "+M_strSBSLS;
					if(txtFMDOR.getText().trim().length() > 0)
						M_strSQLQRY += " AND DOT_DORNO LIKE '"+txtFMDOR.getText().trim()+"%' ";
					M_strSQLQRY += " AND DOT_STSFL IN ('0','1','X') ORDER BY DOT_DORDT DESC";
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY, 1,1,new String [] {"D.O. No.","D.O. Date","Buyer","Amd. No.","Amd. Date","Status","Last Updated By"},7,"CT");
				}
				//else if(L_KE.getSource().equals(txtTODOR))
				//{
				//	M_strHLPFLD = "txtTODOR";
				//	M_strSQLQRY = "SELECT DISTINCT DOT_DORNO,DOT_DORDT,PT_PRTNM,DOT_AMDNO,"
				//		+"DOT_AMDDT,DOT_STSFL,DOT_LUPDT FROM MR_DOTRN,MR_INMST,CO_PTMST WHERE "
				//		+"DOT_MKTTP = IN_MKTTP AND DOT_INDNO = IN_INDNO AND IN_BYRCD = PT_PRTCD "
				//		+"AND PT_PRTTP = 'C' AND IN_STSFL IN 'A','U','1','2','3','X') and IN_SBSCD in "+M_strSBSLS+" and DOT_SBSCD in "+M_strSBSLS;
				//	if(txtFMDOR.getText().trim().length() > 0)
				//		M_strSQLQRY += "AND DOT_DORNO LIKE '"+txtTODOR.getText().trim()+"%' ";
				//	M_strSQLQRY += " AND DOT_STSFL IN ('1','2','X') AND DOT_DORNO >= '"+txtFMDOR.getText().trim()+"'"
				//		+"ORDER BY DOT_DORDT DESC";
				//	//System.out.println(M_strSQLQRY);
				//	cl_hlp(M_strSQLQRY, 1,1,new String[] {"D.O. No.","D.O. Date","Buyer","Amd. No.","Amd. Date","Status","Last Updated By"},7,"CT");
				//}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtFMDOR)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					((JComponent)M_objSOURC).transferFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	/**
	 * 
	*/
	void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtPRDTP"))
			{
				txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst);
				txtFMDOR.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtFMDOR"))
			{
				txtFMDOR.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			//else if(M_strHLPFLD.equals("txtTODOR"))
			//{
			//	txtTODOR.setText(cl_dat.M_strHLPSTR_pbst);
			//	cl_dat.M_btnSAVE_pbst.requestFocus();
			//}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	
	/**
	 */
	void exePRINT()
	{
		try
		{
			M_intPAGNO = 1;
			M_intLINNO = 1;
			
			strREMDS = "";
            strMODDS = "";
			strEXCNO = "";
			strRNGDS = "";
			strDIVDS = "";
			strTINNO = "";
			strCLLDS = "";
			strITPNO = "";
			strDORNO = "";
			
			
			boolean blnNDONO = false;
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			fosREPORT.flush();
			dosREPORT.flush();
			setCursor(cl_dat.M_curWTSTS_pbst);
			dosREPORT.writeBytes("<HTML>");
						
			//strISON1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR01");
		    //strISON2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR02");
		    //strISON3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR03");

			M_strSQLQRY  = "SELECT IN_BYRCD,IN_CNSCD,IN_INDNO,IN_INDDT,IN_AMDDT,IN_PORNO,"
				+"IN_PORDT,IN_SALTP,IN_DTPCD,IN_DSRCD,IN_CPTVL,IN_AMDNO,DOT_MKTTP,DOT_DORNO,"
				+"DOT_DORDT,DOT_STSFL,DOT_AMDNO,DOT_AMDDT,DOT_TMOCD,DOT_LRYNO,DOT_MKTTP,"
				+"DOT_TRPCD,DOT_PRDDS,DOT_LOTRF,DOT_DORPK,DOT_DORQT,DOT_DELTP,DOT_AUTDT,"
				+"DOT_AUTBY,INT_SRLNO,DOT_PRDCD,DOT_PKGTP,INT_BASRT,INT_EXCRT,DOD_DSPDT,DOD_DORQT FROM VW_INTRN,"
				+"MR_DOTRN LEFT OUTER JOIN MR_DODEL ON DOT_DORNO = DOD_DORNO AND DOT_MKTTP = "
				+"DOD_MKTTP AND DOT_PRDCD = DOD_PRDCD WHERE DOT_MKTTP = '"+txtPRDTP.getText().trim()+"' "
				+"AND DOT_MKTTP = IN_MKTTP AND DOT_INDNO = IN_INDNO AND DOT_PRDCD = INT_PRDCD "+" and IN_SBSCD in "+M_strSBSLS+" and DOT_SBSCD in "+M_strSBSLS
				+"AND DOT_PKGTP = INT_PKGTP AND DOT_DORNO = '"+txtFMDOR.getText().trim()+"' "
				+" ORDER BY DOT_DORNO ASC, INT_SRLNO ASC";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET == null || !M_rstRSSET.next())
			{
				setMSG("No Record Found",'E');
				return;
			}
				//System.out.println("000A");
				strDORNO_OLD = nvlSTRVL(M_rstRSSET.getString("DOT_DORNO"),"");
				strPRDCD_OLD = nvlSTRVL(M_rstRSSET.getString("DOT_PRDCD"),"");
				strPKGTP_OLD = nvlSTRVL(M_rstRSSET.getString("DOT_PKGTP"),"");
				//System.out.println("000B");
				dosREPORT.writeBytes("<HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD>");
				dosREPORT.writeBytes("<BODY bgColor=ghostwhite><P>");
				flgEOFFL = false;
				M_intPAGNO = 1;
				while(!flgEOFFL)
				{
					//System.out.println("0001");
					strINDNO = nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),"");
					strDORNO = nvlSTRVL(M_rstRSSET.getString("DOT_DORNO"),"");
					strAUTBY = nvlSTRVL(M_rstRSSET.getString("DOT_AUTBY"),"");
					strAUTDT = (M_rstRSSET.getDate("DOT_AUTDT") == null) ? "" : M_fmtLCDAT.format(M_rstRSSET.getDate("DOT_AUTDT"));
					//System.out.println("0002");
					crtTXDOC(txtPRDTP.getText(),nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""));
					exeHDRPRN(M_rstRSSET);
					//System.out.println("0003");
					exeGRDPRN(M_rstRSSET);
						
					exeFTRPRN();
				}
				dosREPORT.writeBytes("</BODY></HTML>");
                if(dosREPORT !=null)
                    dosREPORT.close();
                if(fosREPORT !=null)
                    fosREPORT.close();
           /*     //if((strAUTBY.length() >0) && (strTREML.length() >0))  
                if((strAUTBY.length() >0))// && (strTREML.length() >0))  
                {
        			int L_intOPTN = JOptionPane.showConfirmDialog( this,"E-mail D.O. to "+strTREML+" ? \n ("+strTRPNM+")","Message",JOptionPane.OK_CANCEL_OPTION);
        			if(L_intOPTN == 0)
        			{
        				cl_eml ocl_eml = new cl_eml();
        		    	ocl_eml.setFRADR("cms@spl.co.in"); 
        		    	System.out.println("E-mail address found");
        		    	ocl_eml.sendfile("systems@spl.co.in",cl_dat.M_strREPSTR_pbst+"mr_rpdoh.html","Delivery Order ","Delivery Order");
                		//ocl_eml.sendfile("EXT",strTREML,cl_dat.M_strREPSTR_pbst+"mr_rpdoh.html","Delivery Order ","Delivery Order");
        				ocl_eml.setORADR();                              
        				setMSG("Report has been mailed succesfully",'N');																
        			}		
                }*/
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst))
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					if((strAUTBY.length() >0) && (strTREML.length() >0))  
                    {
            			int L_intOPTN = JOptionPane.showConfirmDialog( this,"E-mail D.O. to "+strTREML+" ? \n ("+strTRPNM+")","Message",JOptionPane.OK_CANCEL_OPTION);
            			if(L_intOPTN == 0)
            			{
            				cl_eml ocl_eml = new cl_eml();
            		    	ocl_eml.setFRADR("cms@spl.co.in"); 
            		    	System.out.println("E-mail address found");
            		    //	ocl_eml.sendfile("systems@spl.co.in",cl_dat.M_strREPSTR_pbst+"mr_rpdoh.html","Delivery Order ","Delivery Order");
                    		ocl_eml.sendfile("EXT",strTREML,cl_dat.M_strREPSTR_pbst+"mr_rpdoh.htm","Delivery Order ","Delivery Order");
            				ocl_eml.setORADR();                              
            				setMSG("Report has been mailed succesfully",'N');																
            			}		
                    }
					setMSG("For Printing Select File Menu, then Print  ..",'N');				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
					cl_eml ocl_eml = new cl_eml();		
					//System.out.println(strFILNM);
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{					
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Delivery Order"," ");
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
						//System.out.println("After Send "+M_cmbDESTN.getSelectedItem());
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPFAX_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) || flgOUTPRT==true)
				{
					//String L_EMLID = cl_dat.M_strSYSEML_pbst;
					Process prcREPORT;
					prcREPORT  = Runtime.getRuntime().exec("c:\\program files\\internet explorer\\iexplore.exe "+strFILNM);
					//System.out.println("strFAXNO 2 : "+strFAXNO);
				if(M_txtDESTN.getText().length()>1)
				{
					int L_intOPTN = JOptionPane.showConfirmDialog( this,"Fax D.O. to "+M_txtDESTN.getText()+" ? \n ("+strTRPNM+")","Message",JOptionPane.OK_CANCEL_OPTION);
					if(L_intOPTN==0)
					{
						cl_dat.M_strSYSEML_pbst = "cms@spl.co.in";
						cl_eml ocl_eml = new cl_eml();
						setMSG("Sending FAX mail to Party.",'N');
						//System.out.println(strFILNM);
						//System.out.println("Sending Fax to : "+"cms@"+M_txtDESTN.getText()+"@zfax");
						ocl_eml.sendfile("cms@"+M_txtDESTN.getText()+"@zfax",strFILNM,"Delivery Order","Delivery Order");
					}
				}
				
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}

/**
 */
	private void exeFTRPRN()
	{
		try
		{
			ResultSet L_rstRSSET;
		
			//System.out.println("FTPRN  0001");
			M_strSQLQRY = "SELECT RM_REMDS FROM MR_RMMST WHERE RM_DOCNO ='"+strDORNO_OLD+"' "
				+"AND RM_MKTTP = '"+txtPRDTP.getText()+"' AND RM_TRNTP = 'DO'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(L_rstRSSET.next())
				strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
			L_rstRSSET.close();
						
			/*for(int i = intGRDCT; i < 15; i++)
			{
				dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("</TR>");	
				M_intLINNO += 1;
			}*/
			//System.out.println("FTPRN  0002");
			dosREPORT.writeBytes("<TABLE cellSpacing = 0 cellPadding = 0 width=\"100%\" border=1>");
			// Added
			if(strTSTFL.equals("Y"))
			{
				dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD colSpan=7> <P style=\"LINE-HEIGHT: 0.9\" align=center> *** TEST CERTIFICATE REQUIRED *** </TD>");
				dosREPORT.writeBytes("</TR>");
				M_intLINNO += 1;

			}
			///// for monsoon alarm
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD colSpan=7><P><FONT SIZE = 1.5 ><STRONG>");
			dosREPORT.writeBytes("MONSOON ALARM -     TRANSPORTER SHOULD ENSURE WET-FREE TRANSPORTATION OF MATERIAL.THEY SHOULD PROVIDE 3 SETS OF TARPAULIN IN GOOD");
			dosREPORT.writeBytes(" CONDITION WITH EACH TRUCK.ANY COMPENSATION CLAIMED BY THE BUYER DUE TO WETNESS OR ANY SORT ");
			dosREPORT.writeBytes("OF CONTAMINATION OR SPOILAGE ETC. WILL BE ON TRANSPORTER'S ACCOUNT.</P>");
			M_intLINNO += 3;
			//dosREPORT.writeBytes("</STRONG></P>");
			dosREPORT.writeBytes("</FONT>");
			dosREPORT.writeBytes("</TD></TR>");
	
			/////

			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD colSpan=7> <P style=\"LINE-HEIGHT: 1\" align=left> Validity of this D.O. is for 10 days from the date of issue </TD>");
			dosREPORT.writeBytes("</TR>");
			//For Sepecial Instructions 
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD colSpan=7> <P style=\"LINE-HEIGHT: 1\" align=left>Special Instruction <STRONG>"+strREMDS+"</STRONG></P>");
			dosREPORT.writeBytes("<P "+strREMDS+"></P></TD>");
			dosREPORT.writeBytes("</TR>");
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD colSpan=7> <P style=\"LINE-HEIGHT: 1\" align=left>Particulars of MODVAT :  <STRONG>"+strMODDS+"</STRONG></P>");
			//dosREPORT.writeBytes("<P "+strREMDS+"></P></TD>");
			dosREPORT.writeBytes("</TR>");
			
			M_intLINNO += 3;
			////End Instruction
		
			////
			//dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD colSpan=4> <P style=\"LINE-HEIGHT: 0.9\" align=left>Tin No.<STRONG>"+strTINNO+"</STRONG>");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=left>Range <STRONG>"+strRNGDS+"</STRONG></P>");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=left>Division <STRONG>"+strDIVDS+"</STRONG></P>");
//			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=left>Tin No. <STRONG>"+strTINNO+"</STRONG></P>");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=left>Collectorate <STRONG>"+strCLLDS+"</STRONG></P>");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=left>Income Tax PAN No <STRONG>"+strITPNO+"</STRONG></P>");
			dosREPORT.writeBytes("</TD>");
			dosREPORT.writeBytes("<TD colSpan=4> <P style=\"LINE-HEIGHT: 0.9\" align=center> For CMS Dept.");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=center> &nbsp </P>");
//			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=center> &nbsp </P>");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=center> &nbsp </P>");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=center>"+strAUTBY+" "+strAUTDT+"</P>");
			dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.9\" align=center> Authorised Signatory </P>");
			dosREPORT.writeBytes("</TD>");
			dosREPORT.writeBytes("</TR>");
			dosREPORT.writeBytes("</TABLE>");
	//		dosREPORT.writeBytes("<P FONT SIZE = \"1.5\" style=\"LINE-HEIGHT: 0.6\" align=center><FONT size=1><STRONG>REGD. OFFICE :</STRONG> 612, REHEJA CHAMBERS, NARIMAN POINT, MUMBAI-400021</FONT> </P>");
	//		dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.6\" align=center><FONT size=1><STRONG>WORKS :</STRONG> VILLAGE : AMDOSHI / WANGANI, WAKAN-ROHA ROAD, TALUKA : ROHA DIST : RAIGAD, MAHARASHTRA - 402106</FONT> </P>");
	//		dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.6\" align=center><FONT size=1><STRONG>TEL :</STRONG> (02194)-222228,222540-48 FAX : (02194) 222337,222537,222617 </FONT></P></FONT>");
	
	//		dosREPORT.writeBytes("<P FONT SIZE = \"1.5\" style=\"LINE-HEIGHT: 0.6\" align=center><FONT size=1><STRONG>REGD. OFFICE :</STRONG> 612, REHEJA CHAMBERS, NARIMAN POINT, MUMBAI-400021</FONT> </P>");
	//		dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.6\" align=center><FONT size=1><STRONG>WORKS :</STRONG> VILLAGE : AMDOSHI / WANGANI, WAKAN-ROHA ROAD, TALUKA : ROHA DIST : RAIGAD, MAHARASHTRA - 402106</FONT> </P>");
	//		dosREPORT.writeBytes("<P style=\"LINE-HEIGHT: 0.6\" align=center><FONT size=1><STRONG>TEL :</STRONG> (02194)-222228,222540-48 FAX : (02194) 222337,222537,222617 </FONT></P></FONT>");
	
			M_intLINNO += 5;
			//System.out.println("FTPRN  0004");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeFTRPRN");
		}
	}


					
/**
 */					
	private void exeGRDPRN(ResultSet LP_RSSET)
	{
		try
		{
			intGRDCT = 0;
			flgEOFFL = false;
			String L_strPKGTP ="";
			dosREPORT.writeBytes("<BR>");
		//	dosREPORT.writeBytes("<TABLE cellSpacing = 0 cellPadding = 0 width=\"100%\" border=0>");
			while(!flgEOFFL)
			{
				/*dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD><P align=left style= \"LINE-HEIGHT: 1\">"+nvlSTRVL(LP_RSSET.getString("DOT_PRDDS"),"")+"</TD>");
				dosREPORT.writeBytes("<TD><P align=left style= \"LINE-HEIGHT: 1\">"+nvlSTRVL(LP_RSSET.getString("DOT_DORQT"),"0.0")+"</TD>");
				dosREPORT.writeBytes("<TD><P align=left style= \"LINE-HEIGHT: 1\">"+nvlSTRVL(LP_RSSET.getString("DOT_PKGTP"),"0")+"</TD>");
				dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 1\">"+nvlSTRVL(LP_RSSET.getString("DOT_DORPK"),"0")+"</TD>");
				dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 1\">"+nvlSTRVL(LP_RSSET.getString("INT_BASRT"),"0.0")+"</TD>");
			*/	
			    L_strPKGTP = nvlSTRVL(LP_RSSET.getString("DOT_PKGTP"),"0");
			   if(hstPKGTP.containsKey(L_strPKGTP))
			        L_strPKGTP = hstPKGTP.get(L_strPKGTP).toString();
				dosREPORT.writeBytes("<P><PRE><STRONG>");
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(LP_RSSET.getString("DOT_PRDDS"),""),20));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(LP_RSSET.getString("DOT_DORQT"),"0.0"),8));
				dosREPORT.writeBytes(padSTRING('L',L_strPKGTP,12));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(LP_RSSET.getString("DOT_DORPK"),"0"),8));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(LP_RSSET.getString("INT_BASRT"),"0.0"),10));
				dosREPORT.writeBytes(padSTRING('L'," ",2));
				//dosREPORT.writeBytes("</P></PRE>");
				flgDODEL = true; flgCOTAX = true;
				intTXLIN=1; intTXFLD=0;
				while(true)
				{
					chkTAXPRN(txtPRDTP.getText(),strINDNO);
					chkDODEL(LP_RSSET);
					M_intLINNO++;
					intGRDCT++;
					//dosREPORT.writeBytes("</TR>");
					if(flgDODEL)
					{
						if(!LP_RSSET.next())
							{flgEOFFL = true;flgDODEL = false;}
						else
						{
							strPRDCD = nvlSTRVL(LP_RSSET.getString("DOT_PRDCD"),"");
							strPKGTP = nvlSTRVL(LP_RSSET.getString("DOT_PKGTP"),"");
						}
					}
						
					if(!(strPRDCD_OLD+strPKGTP_OLD).equals(strPRDCD+strPKGTP))
						{flgDODEL = false; strPRDCD_OLD = strPRDCD; strPKGTP_OLD = strPKGTP;}
					if(!flgDODEL && !flgCOTAX)
						break;
				}
			    
				//System.out.println(strPRDCD_OLD+"/"+strPKGTP_OLD);
			}
			dosREPORT.writeBytes("</STRONG><HR>");
			//	endTABLE(dosREPORT);
				dosREPORT.writeBytes("<TABLE cellSpacing = 0 cellPadding = 0 width=\"100%\" border=1>");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeGRDPRN");
		}
	}
					
		
	private void exeHDRPRN(ResultSet LP_RSSET)
	{
		try
		{
			//System.out.println("HDRPRN  0001");
			ResultSet L_rstRSSET;
			//dosREPORT.writeBytes("<P align=right style=\"LINE-HEIGHT: 0.1\"><FONT size=2>"+strISON1+"</FONT></P>");
			//dosREPORT.writeBytes("<P align=right style=\"LINE-HEIGHT: 0.1\"><FONT size=2>"+strISON2+"</FONT></P>");
			//dosREPORT.writeBytes("<P align=right style=\"LINE-HEIGHT: 0.1\"><FONT size=2>"+strISON3+"</FONT></P>");
			//M_intLINNO += 3;
			//dosREPORT.writeBytes("<HR>");
			//M_intLINNO += 1;
			// dosREPORT.writeBytes("<HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD><BODY bgColor=ghostwhite><P><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\">  <TBODY> <TR><TD><IMG src=\" "+imageURL.toString() +"\" style=\"HEIGHT: 90px; LEFT: 0px; TOP: 0px; WIDTH: 86px\"></TD><TD style=\"LINE-HEIGHT: 1\"><P align=left><FONT color=black><FONT face=Arial size=5><STRONG>"+cl_dat.M_strCMPNM_pbst+"</STRONG>  </FONT></FONT></P>      <P align=left style=\"LINE-HEIGHT: 0.1\"><FONT                     face=Arial size=1>"+strADRLN1+"</FONT></P>      <P align=left style=\"LINE-HEIGHT: 0; MARGIN-TOP: 0px\"      ><FONT size=3><FONT face=Arial><FONT       face=Wingdings size=4><STRONG>)</STRONG></FONT><FONT size=1><FONT face=Webdings size=2>       </FONT>"+strADRLN2+"</FONT></FONT></FONT></P><FONT size=+0><FONT       face=Arial></FONT><FONT size=5><p align=left><STRONG style=\"LINE-HEIGHT: 1.8\"       >Customer       Order</STRONG></FONT></FONT></P></TD><TD>      <P><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</P><p><FONT face=Arial size=2>Page No. : 1</p></FONT></FONT><TD></TD></TR></TBODY></TABLE><HR><FONT face=\"Comic Sans MS\"></STRONG><p></p>");
			dosREPORT.writeBytes("<TABLE cellSpacing=0 cellPadding=0 width=\"100%\" border=1>");
		    dosREPORT.writeBytes("<TR><TD><IMG src=\"file://C:\\windows\\"+cl_dat.M_strCMPLG_pbst +"\" style= \"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></TD>");
            dosREPORT.writeBytes("<TD><P align=center ><FONT size=4><STRONG>"+cl_dat.M_strCMPNM_pbst.toUpperCase()+"</STRONG></FONT></P>");
			dosREPORT.writeBytes("<P align=center style = \"LINE-HEIGHT: 0.1\"><FONT size=2>"+strADRLN1+"</FONT> </P>");
			dosREPORT.writeBytes("<P align=center style = \"LINE-HEIGHT: 0.1\"><FONT size=2>"+strADRLN2+"</FONT></A></P>");
			dosREPORT.writeBytes("<P align=center style = \"LINE-HEIGHT: 0.8\"><STRONG><FONT size=4>Delivery Order</FONT> </STRONG></P></TD>");
			dosREPORT.writeBytes("<TD><P>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</P><P>Page No : "+M_intPAGNO+"</P></TD>");
			dosREPORT.writeBytes("</TR></TABLE></P>");
			M_intLINNO += 3;
			//dosREPORT.writeBytes("<HR>");
		//	M_intLINNO += 1;
			//Query for get Byers Name and Address						
			//System.out.println("HDRPRN  0002");
			M_strSQLQRY = "SELECT PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,"
				+"PT_PINCD,PT_CSTNO,PT_STXNO,PT_ECCNO,PT_EXCNO,PT_RNGDS,PT_DIVDS,"
				+"PT_CLLDS,PT_ITPNO,PT_TSTFL,PT_TINNO FROM CO_PTMST WHERE PT_PRTCD ='"
				+LP_RSSET.getString("IN_BYRCD")+"' AND PT_PRTTP = 'C'";
			L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
							
			crtTBL(dosREPORT,true,66,'L',false);
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD><font aling = left>Buyer's Name & Address");
			M_intLINNO += 1;
			//System.out.println("HDRPRN  0003");
			if(L_rstRSSET.next())
			{	
				dosREPORT.writeBytes("<p align=left style= \"LINE-HEIGHT: 0.1\"><FONT size=1><STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"")+" ("+LP_RSSET.getString("IN_BYRCD")+")"+"</STRONG></p>");
				dosREPORT.writeBytes("<p align=left style= \"LINE-HEIGHT: 0.1\"><FONT size=1.2><STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_ADR01"),"")+"</STRONG></p>");
				dosREPORT.writeBytes("<p align=left style= \"LINE-HEIGHT: 0.1\"><FONT size=1.2><STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_ADR02"),"")+"</STRONG></p>");
				dosREPORT.writeBytes("<p align=left style= \"LINE-HEIGHT: 0.1\"><FONT size=1.2><STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_ADR03"),"")+"</STRONG></p>");
				dosREPORT.writeBytes("<p align=left ><FONT size=1.2><STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_ADR04"),"")+" "+nvlSTRVL(L_rstRSSET.getString("PT_PINCD"),"-")+"</STRONG></p>");
				strTSTFL = nvlSTRVL(L_rstRSSET.getString("PT_TSTFL"),"");
				strTINNO = nvlSTRVL(L_rstRSSET.getString("PT_TINNO"),"");
				M_intLINNO += 5;
				dosREPORT.writeBytes("</TD>");
			}
			else
			{
				for(int i = 0;i < 5;i++)
					dosREPORT.writeBytes("<p align=left style= \"LINE-HEIGHT: 0.1\">&nbsp</p>");
				dosREPORT.writeBytes("</TD>");
			}
			//System.out.println("HDRPRN  0004");
			dosREPORT.writeBytes("<TD><font aling = left>Consignee Name & Address");
			for(int i = 0;i < 5;i++)
				dosREPORT.writeBytes("<p align=left style= \"LINE-HEIGHT: 0.1\">&nbsp</p>");
			dosREPORT.writeBytes("</TD>");
			dosREPORT.writeBytes("</TR>");
							
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD align=left style= \"LINE-HEIGHT: 0.8\">C.S.T. No : <STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_CSTNO"),"")+" ("+LP_RSSET.getString("IN_CNSCD")+")"+"</STRONG>");
			dosREPORT.writeBytes("<p  align=left style= \"LINE-HEIGHT: 0.8\">S.T. No : <STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_STXNO"),"")+"</STRONG></p>");
			dosREPORT.writeBytes("<p  align=left style= \"LINE-HEIGHT: 0.8\">ECC No : <STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_ECCNO"),"")+"</STRONG></p>");
			dosREPORT.writeBytes("</TD>");
			dosREPORT.writeBytes("<TD  align=left style= \"LINE-HEIGHT: 0.8\">C.S.T. No : <STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_CSTNO"),"")+"</STRONG>");
			dosREPORT.writeBytes("<p  align=left style= \"LINE-HEIGHT: 0.8\">S.T. No : <STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_STXNO"),"")+"</STRONG></p>");
			dosREPORT.writeBytes("<p  align=left style= \"LINE-HEIGHT: 0.8\">ECC No : <STRONG>"+nvlSTRVL(L_rstRSSET.getString("PT_ECCNO"),"")+"</STRONG></p>");
			dosREPORT.writeBytes("</TD>");
			dosREPORT.writeBytes("</TR>");
			//System.out.println("HDRPRN  0005");
			M_intLINNO += 3;	
			strEXCNO = nvlSTRVL(L_rstRSSET.getString("PT_EXCNO"),"");
			strRNGDS = nvlSTRVL(L_rstRSSET.getString("PT_RNGDS"),"");
			strDIVDS = nvlSTRVL(L_rstRSSET.getString("PT_DIVDS"),"");
			strTINNO = nvlSTRVL(L_rstRSSET.getString("PT_TINNO"),"");
			strCLLDS = nvlSTRVL(L_rstRSSET.getString("PT_CLLDS"),"");
			strITPNO = nvlSTRVL(L_rstRSSET.getString("PT_ITPNO"),"");
			L_rstRSSET.close();
			endTABLE(dosREPORT);
													
			crtTBL(dosREPORT,true,34,'L',false);
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD align=left style= \"LINE-HEIGHT: 0.7\">D.O.No : <STRONG>"+nvlSTRVL(LP_RSSET.getString("DOT_DORNO"),"")+"</STRONG><BR>");
			if(LP_RSSET.getDate("DOT_DORDT") != null)
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">D.O.Dt : <STRONG>"+M_fmtLCDAT.format(LP_RSSET.getDate("DOT_DORDT"))+"</STRONG><BR>");
			else
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">D.O.Dt : <BR>");
			if(LP_RSSET.getDate("DOT_AMDDT") != null)
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Amd. Dt : <STRONG>"+M_fmtLCDAT.format(LP_RSSET.getDate("DOT_AMDDT"))+"</STRONG></TD>");
			else
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Amd. Dt : </TD>");
			dosREPORT.writeBytes("</TR>");
			//System.out.println("HDRPRN  0006");
							
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD align=left style= \"LINE-HEIGHT: 0.7\">Indent No : <STRONG>"+nvlSTRVL(LP_RSSET.getString("IN_INDNO"),"")+"</STRONG><BR>");
			if(LP_RSSET.getDate("IN_INDDT") != null)
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Indent Dt : <STRONG>"+M_fmtLCDAT.format(LP_RSSET.getDate("IN_INDDT"))+"</STRONG><BR>");
			else
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Indent Dt : <BR>");
			if(LP_RSSET.getDate("IN_AMDDT") != null)
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Amd. Dt : <STRONG>"+M_fmtLCDAT.format(LP_RSSET.getDate("IN_AMDDT"))+"</STRONG></TD>");
			else
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Amd. Dt : </TD>");
			
			dosREPORT.writeBytes("</TR>");
							
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD align=left style= \"LINE-HEIGHT: 0.7\">P. O. No : <STRONG>"+nvlSTRVL(LP_RSSET.getString("IN_PORNO"),"")+"</STRONG><BR>");
			if(LP_RSSET.getDate("IN_PORDT") != null)
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">P. O. Dt. : <STRONG>"+M_fmtLCDAT.format(LP_RSSET.getDate("IN_PORDT"))+"</STRONG></TD>");
			else
				dosREPORT.writeBytes("<BR align = left style = \"LINE-HEIGHT: 0.7\">P. O. Dt. : </TD>");
			dosREPORT.writeBytes("</TR>");
							
			//System.out.println("HDRPRN  0007");
			dosREPORT.writeBytes("<TR>");
			M_strSQLQRY = "SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS'"
				+" AND CMT_CGSTP = 'MR00SAL' AND CMT_CODCD = '"+nvlSTRVL(LP_RSSET.getString("IN_SALTP"),"")+"'";
			L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstRSSET.next())
				dosREPORT.writeBytes("<TD align=left style= \"LINE-HEIGHT: 0.7\">Type Of Sale : <STRONG>"+L_rstRSSET.getString("CMT_CODDS")+"</STRONG><BR>");
			else
				dosREPORT.writeBytes("<TD align=left style= \"LINE-HEIGHT: 0.7\">Type Of Sale : <BR>");
			L_rstRSSET.close();
			dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Del Type : <BR>");
			//dosREPORT.writeBytes("<TD align=left style= \"LINE-HEIGHT: 0.7\">Del Type : <STRONG>"+" "+"</STRONG><BR>");				
			M_strSQLQRY = "SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS'"
				+" AND CMT_CGSTP = 'MRXXMOT' AND CMT_CODCD = '"+nvlSTRVL(LP_RSSET.getString("dot_tmocd"),"")+"'";
			L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstRSSET.next())
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Mode Of Trans.: <STRONG>"+nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"")+"</STRONG><BR>");
			else
				dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Mode Of Trans.: <BR>");
			L_rstRSSET.close();
							
			//System.out.println("HDRPRN  0008");
			dosREPORT.writeBytes("<BR align=left style= \"LINE-HEIGHT: 0.7\">Vehicle No : <STRONG>"+nvlSTRVL(LP_RSSET.getString("DOT_LRYNO"),"")+"</STRONG></TD>");
			dosREPORT.writeBytes("</TR>");
			endTABLE(dosREPORT);
			dosREPORT.writeBytes("&nbsp");	
			M_intLINNO += 1;		
			crtTBL(dosREPORT,true,100,'C',true);
			dosREPORT.writeBytes("<TR>");
			M_strSQLQRY = "SELECT PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP = 'D' AND "
				+"PT_PRTCD = '"+LP_RSSET.getString("IN_DSRCD")+"'";
			L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstRSSET.next())
				dosREPORT.writeBytes("<TD><font aling = right>Distributor : <STRONG><FONT size=2>"+L_rstRSSET.getString("PT_PRTNM")+"</STRONG></TD>");
			else
				dosREPORT.writeBytes("<TD><font align = right>Distributor : </TD>");
			M_intLINNO += 1;
			L_rstRSSET.close();
			dosREPORT.writeBytes("<TD><font aling = right>Credit Period : <STRONG>"+nvlSTRVL(LP_RSSET.getString("IN_CPTVL"),"")+" Days  </STRONG>"+"Veh. No. "+nvlSTRVL(LP_RSSET.getString("DOT_LRYNO"),"")+"</TD>");
			dosREPORT.writeBytes("</TR>");
							
			//System.out.println("HDRPRN  0009");
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD><font aling = right>Product : <STRONG>Polystyrene</STRONG></TD>");
							
			M_strSQLQRY = "SELECT PT_PRTNM, PT_FAXNO,PT_EMLMR FROM CO_PTMST WHERE PT_PRTTP = 'T' AND "
				+"PT_PRTCD = '"+LP_RSSET.getString("DOT_TRPCD")+"'";
			L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstRSSET.next())
			{
				strFAXNO = nvlSTRVL(L_rstRSSET.getString("PT_FAXNO"),"");
				strTRPNM = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");
				strTREML = nvlSTRVL(L_rstRSSET.getString("PT_EMLMR"),"");
				//System.out.println("strFAXNO 1 : "+strFAXNO);
				if(M_txtDESTN.getText().length()==0)
					M_txtDESTN.setText(strFAXNO);
				if(!strFAXNO.equals(M_txtDESTN.getText()))
					JOptionPane.showMessageDialog( this,"Fax No.Mismatch   Entered: "+M_txtDESTN.getText()+"     File: "+strFAXNO,"Warning",JOptionPane.WARNING_MESSAGE);
					
				dosREPORT.writeBytes("<TD><font aling = right>Transporter : <STRONG><FONT size=2>"+strTRPNM+"</FONT></STRONG></TD>");
				dosREPORT.writeBytes("</TR>");
				dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD> - </TD>");
				dosREPORT.writeBytes("<TD><font aling = right> Fax No :"+strFAXNO+"</TD>");		
				dosREPORT.writeBytes("</TR>");
				M_intLINNO += 1;
			}
			else
			{
				dosREPORT.writeBytes("<TD><font aling = right>Transporter : </TD>");
				dosREPORT.writeBytes("</TR>");
				dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD> - </TD>");
				dosREPORT.writeBytes("<TD><font aling = right> Fax No :"+strFAXNO+"</TD>");		
				dosREPORT.writeBytes("</TR>");
				M_intLINNO += 1;
			}
			L_rstRSSET.close();
			dosREPORT.writeBytes("</TR>");
			endTABLE(dosREPORT);
			//M_intLINNO += 1;
							
			//System.out.println("HDRPRN  0010");
		/*	dosREPORT.writeBytes("<TABLE cellSpacing = 0 cellPadding = 0 width=\"100%\" border=1>");
			dosREPORT.writeBytes("<TR>");
			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\"><STRONG>Grade &nbsp&nbsp&nbsp&nbsp&nbsp  </STRONG></P></TD>");
			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\"><STRONG>Qty.</STRONG></P><P align=center style= \"LINE-HEIGHT: 0.8\"><STRONG>MT</STRONG></P></TD>");
			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\"><STRONG>Pkg.</STRONG></P><P align=center style= \"LINE-HEIGHT: 0.8\"><STRONG>Type</STRONG></P></TD>");
			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\"><STRONG>No Of</STRONG> </P><P align=center style= \"LINE-HEIGHT: 0.8\"><STRONG>Pkgs.</STRONG></P></TD>");
			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\"><STRONG>Basic Price</STRONG> </P><P align=center style= \"LINE-HEIGHT: 0.8\"><STRONG>Rs / MT</STRONG>  </P></TD>");
			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\"><STRONG>Rate Of</STRONG> </P><P align=center style= \"LINE-HEIGHT: 0.8\"><STRONG>Excise</STRONG></P></TD>");
			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\"><STRONG>Schedule</STRONG></P><P align=center style= \"LINE-HEIGHT: 0.8\"><STRONG>Date   Qty</STRONG></P></TD>");
			dosREPORT.writeBytes("</TR>");
			endTABLE(dosREPORT);*/
			dosREPORT.writeBytes("<HR><P><PRE><STRONG>");
			dosREPORT.writeBytes(padSTRING('R',"GRADE",16));
			dosREPORT.writeBytes(padSTRING('L',"QTY.",8));
			dosREPORT.writeBytes(padSTRING('L',"PKG.",15));
			dosREPORT.writeBytes(padSTRING('L',"NO. OF",11));
			dosREPORT.writeBytes(padSTRING('L',"BASIC RATE",12));
			dosREPORT.writeBytes(padSTRING('C',"RATE  OF",15));
			dosREPORT.writeBytes(padSTRING('R',"DELIVERY SCHEDULE",20));
			dosREPORT.writeBytes(padSTRING('R'," ",10));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",16));
			dosREPORT.writeBytes(padSTRING('L',"(MT)",8));
			dosREPORT.writeBytes(padSTRING('L',"TYPE",15));
			dosREPORT.writeBytes(padSTRING('L',"PKGS.",11));
			dosREPORT.writeBytes(padSTRING('C',"RS/MT  ",12));
			dosREPORT.writeBytes(padSTRING('C',"EXCISE",15));
			dosREPORT.writeBytes(padSTRING('C'," DATE",10));
			dosREPORT.writeBytes(padSTRING('C',"  QTY",10));
			dosREPORT.writeBytes("</STRONG></P></PRE><HR>");
			M_intLINNO += 2;
			strDORNO = nvlSTRVL(LP_RSSET.getString("DOT_DORNO"),"");
			//System.out.println("HDRPRN  0011");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHDRPRN");
		}
	}

		
	
	/**
	 */
	private void chkDODEL(ResultSet LP_RSSET)
	{
		try
		{
			if(!flgDODEL)
				return;
			//{dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 0.9\">&nbsp</TD></TR>");return;}
			if(intTXLIN>1 && !flgTAXPR)
	 		{
				/*dosREPORT.writeBytes("<TR>");
	 			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 1\">&nbsp</TD>");
	 			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 1\">&nbsp</TD>");
	 			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 1\">&nbsp</TD>");
	 			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 1\">&nbsp</TD>");
	 			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 1\">&nbsp</TD>");
	 			dosREPORT.writeBytes("<TD><P align=center style= \"LINE-HEIGHT: 1\">&nbsp</TD>");
	 			*/
	 			dosREPORT.writeBytes(padSTRING('L',"",60));
	 		}
			if(nvlSTRVL(LP_RSSET.getString("DOT_DELTP"),"").equalsIgnoreCase("I"))
				//dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 1\">Immediate</TD>");
				dosREPORT.writeBytes("Immediate");
			else
		 	{
				String L_strDODDT = "";
				if(LP_RSSET.getString("DOD_DSPDT")!=null)
					L_strDODDT = M_fmtLCDAT.format(LP_RSSET.getDate("DOD_DSPDT"));
				String L_strDORQT = nvlSTRVL(LP_RSSET.getString("dod_dorqt"),"0.0");
				//dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">"+L_strDODDT+" "+L_strDORQT+"</TD>");
				dosREPORT.writeBytes(padSTRING('R',L_strDODDT,10)+padSTRING('L',L_strDORQT,8));
			}
			//dosREPORT.writeBytes("</TR>");
			dosREPORT.writeBytes("\n");
			intTXLIN++;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkDODEL");
		}
	}
	
	
	/**
	 * Method to display data in HTML table format
	 * <p>Does nothing if text format is seleted. <br>In HTML format, writes the table tag with format as specified by aurguments
	 * @param L_DOUT Stream to which data is to be written
	 * @param P_flgBORDER Flag to paint borders of the table
	 * @param P_intWIDTH % width the table should occupy on the screen
	 * @param P_chrALIGN Character for table alignment on screen. 'C' : Center, 'L' : Left, 'R' : Right
	 * @param P_flgNWLIN Flag to indicate whether the table is to be placed on the current line or to the next line. This parameter is importent when earlier table is having width <100 or when two tables are to be place beside each other
	 **/
	private void crtTBL(DataOutputStream L_DOUT,boolean P_flgBORDR,int P_intWIDTH,char P_chrALIGN,boolean P_flgNWLIN) throws Exception
	{
		if(P_flgNWLIN)
		{
			for(int i=0;i<7;i++)
				L_DOUT.writeBytes("<p style= \"LINE-HEIGHT: 0.656\">&nbsp</p>");
		}
		String L_strALIGN=" Center ";
		if(P_chrALIGN == 'l' || P_chrALIGN == 'L')
			L_strALIGN=" Left ";
		else if(P_chrALIGN == 'r' || P_chrALIGN == 'R')
			L_strALIGN=" right ";
		if(P_flgBORDR)
			L_DOUT.writeBytes("<TABLE border = 1 borderColor=white borderColorDark=white borderColorLight=gray cellPadding=0 cellSpacing=0  width=\""+Integer.toString(P_intWIDTH)+"%\" align="+L_strALIGN+">");
		else
			L_DOUT.writeBytes("<TABLE border = 0 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gostwhite cellPadding=0 cellSpacing=0 width=\""+Integer.toString(P_intWIDTH)+"%\"  align="+L_strALIGN+">");
	}
	/**Method to end table in HTML format	 */
	private void endTABLE(DataOutputStream L_DOUT) throws Exception
	{
		//if(M_rdbHTML.isSelected())
		L_DOUT.writeBytes("</TABLE>");
	}


	/** Checking for common tax applicable, and printing the Tax Details 
	 * in predefined order
	 */
	private void chkTAXPRN(String LP_PRDTP, String LP_INDNO)
	{
		try
		{
			if(!flgCOTAX)
				return;
			String L_strTXVAL = "0.00";
			String L_strTXFLG = "X";
			if(intTXFLD>intTXFLD_TOT)
				{flgTAXPR = false; flgCOTAX = false;  return;}
			while(intTXFLD<=intTXFLD_TOT)
			{
				L_strTXVAL = getTXDOC("MR",LP_PRDTP,"IND",LP_INDNO,"XXXXXXXXXX",arrTXFLD[intTXFLD],"VL");
				L_strTXFLG = getTXDOC("MR",LP_PRDTP,"IND",LP_INDNO,"XXXXXXXXXX",arrTXFLD[intTXFLD],"FL").equalsIgnoreCase("P") ? "%" : "";
				if(Double.parseDouble(L_strTXVAL)==0.00)
				{
					intTXFLD++;
					continue;
				}
				else
					break;
			}
			if(Double.parseDouble(L_strTXVAL)==0.00)
			{
				flgTAXPR = false;
				if(intTXLIN==1)
					{//dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">"+" "+"</TD>");
					    dosREPORT.writeBytes(" ");
					}
					    
				return;
			}
			if(intTXLIN>1)
			{
				/*dosREPORT.writeBytes("<TR>");
				dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">&nbsp</TD>");
				*/
				dosREPORT.writeBytes(padSTRING('L',"",60));
				/*dosREPORT.writeBytes(" ");
				dosREPORT.writeBytes(" ");
				dosREPORT.writeBytes(" ");
				dosREPORT.writeBytes(" ");
				dosREPORT.writeBytes(" ");*/
			}
			//dosREPORT.writeBytes("<TD><P align=right style= \"LINE-HEIGHT: 0.9\">"+getCDTRN("SYSCOXXTAX"+arrTXFLD[intTXFLD],"CMT_SHRDS",hstCDTRN)+":"+L_strTXVAL+L_strTXFLG+"</TD>");
			dosREPORT.writeBytes(padSTRING('R',(getCDTRN("SYSCOXXTAX"+arrTXFLD[intTXFLD],"CMT_SHRDS",hstCDTRN)+":"+L_strTXVAL+L_strTXFLG),15));
			flgTAXPR = true;

			M_intLINNO += 1;
			intTXFLD++;
			if(intTXLIN>1 && !flgDODEL)
				//dosREPORT.writeBytes("</TR>");
				dosREPORT.writeBytes("\n");
			intTXLIN++;
		}
		catch(Exception L_EX)
			{setMSG(L_EX,"chkTAXPRN");}
		return;
	}
	
	
	
		/** One time data capturing for Doc.Tax 
		*	into the Hash Table
		*/
	private void crtTXDOC(String LP_MKTTP,String LP_INDNO)
	{
		String L_strSQLQRY = "";
		try
		{
			hstTXDOC.clear();
				//System.out.println("crtTXDOC 001");
		    M_strSQLQRY = "select * from co_txdoc where tx_syscd='MR' and tx_sbstp='"+LP_MKTTP+"' and tx_doctp='IND' and tx_docno='"+LP_INDNO+"' and tx_prdcd='XXXXXXXXXX'";
			//System.out.println(M_strSQLQRY);
		    ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//System.out.println("crtTXDOC 002");
		    if(L_rstRSSET == null || !L_rstRSSET.next())
		    {
				setMSG("Tax Records not found in CO_TXDOC",'E');
		        return;
		    }
		    while(true)
			{
		        strTX_SYSCD = getRSTVAL(L_rstRSSET,"TX_SYSCD","C");
		        strTX_SBSTP = getRSTVAL(L_rstRSSET,"TX_SBSTP","C");
		        strTX_DOCTP = getRSTVAL(L_rstRSSET,"TX_DOCTP","C");
		        strTX_DOCNO = getRSTVAL(L_rstRSSET,"TX_DOCNO","C");
		        strTX_PRDCD = getRSTVAL(L_rstRSSET,"TX_PRDCD","C");
				//System.out.println("crtTXDOC 003");
		        String[] staTXDOC = new String[intTXDOC_TOT];
		        staTXDOC[intAE_TX_EXCVL] = getRSTVAL(L_rstRSSET,"TX_EXCVL","N");
		        staTXDOC[intAE_TX_EXCFL] = getRSTVAL(L_rstRSSET,"TX_EXCFL","C");
		        staTXDOC[intAE_TX_EDCVL] = getRSTVAL(L_rstRSSET,"TX_EDCVL","N");
		        staTXDOC[intAE_TX_EDCFL] = getRSTVAL(L_rstRSSET,"TX_EDCFL","C");
		        staTXDOC[intAE_TX_EHCVL] = getRSTVAL(L_rstRSSET,"TX_EHCVL","N");
		        staTXDOC[intAE_TX_EHCFL] = getRSTVAL(L_rstRSSET,"TX_EHCFL","C");
		        staTXDOC[intAE_TX_SCHVL] = getRSTVAL(L_rstRSSET,"TX_SCHVL","N");
		        staTXDOC[intAE_TX_SCHFL] = getRSTVAL(L_rstRSSET,"TX_SCHFL","C");
		        staTXDOC[intAE_TX_TOTVL] = getRSTVAL(L_rstRSSET,"TX_TOTVL","N");
		        staTXDOC[intAE_TX_TOTFL] = getRSTVAL(L_rstRSSET,"TX_TOTFL","C");
				//System.out.println("crtTXDOC 004");
		        staTXDOC[intAE_TX_CSTVL] = getRSTVAL(L_rstRSSET,"TX_CSTVL","N");
		        staTXDOC[intAE_TX_CSTFL] = getRSTVAL(L_rstRSSET,"TX_CSTFL","C");
		        staTXDOC[intAE_TX_STXVL] = getRSTVAL(L_rstRSSET,"TX_STXVL","N");
		        staTXDOC[intAE_TX_STXFL] = getRSTVAL(L_rstRSSET,"TX_STXFL","C");
                staTXDOC[intAE_TX_VATVL] = getRSTVAL(L_rstRSSET,"TX_VATVL","N");
		        staTXDOC[intAE_TX_VATFL] = getRSTVAL(L_rstRSSET,"TX_VATFL","C");
				//System.out.println("crtTXDOC 005");
		        hstTXDOC.put(strTX_SYSCD+strTX_SBSTP+strTX_DOCTP+strTX_DOCNO+strTX_PRDCD,staTXDOC);
		        if (!L_rstRSSET.next())
		                break;
		    }
		    L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtTXDOC");
		}
		return;
	}
	 /** Picking up Doc.Tax Details
	 */
	private String getTXDOC(String LP_SYSCD, String LP_SBSTP, String LP_DOCTP, String LP_DOCNO, String LP_PRDCD, String LP_FLDNM, String LP_FLGVL)
	{
		//System.out.println("Received : "+LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD+LP_FLDNM);
		String L_RETSTR = LP_FLGVL.equals("VL") ? "0.00" : "";
		try
		{
			if(!hstTXDOC.containsKey(LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD))
				return L_RETSTR;
	        String[] staTXDOC = (String[])hstTXDOC.get(LP_SYSCD+LP_SBSTP+LP_DOCTP+LP_DOCNO+LP_PRDCD);
			if(LP_FLGVL.equals("VL"))
		    {
				if (LP_FLDNM.equals("EXC"))
			        L_RETSTR = staTXDOC[intAE_TX_EXCVL];
				else if (LP_FLDNM.equals("EDC"))
			        L_RETSTR = staTXDOC[intAE_TX_EDCVL];
				else if (LP_FLDNM.equals("EHC"))
			        L_RETSTR = staTXDOC[intAE_TX_EHCVL];
				else if (LP_FLDNM.equals("SCH"))
			        L_RETSTR = staTXDOC[intAE_TX_SCHVL];
				else if (LP_FLDNM.equals("TOT"))
			        L_RETSTR = staTXDOC[intAE_TX_TOTVL];
				else if (LP_FLDNM.equals("CST"))
			        L_RETSTR = staTXDOC[intAE_TX_CSTVL];
				else if (LP_FLDNM.equals("STX"))
			        L_RETSTR = staTXDOC[intAE_TX_STXVL];
			    else if (LP_FLDNM.equals("VAT"))
			        L_RETSTR = staTXDOC[intAE_TX_VATVL];
			}
			if(LP_FLGVL.equals("FL"))
		    {
				if (LP_FLDNM.equals("EXC"))
			       L_RETSTR = staTXDOC[intAE_TX_EXCFL];
				else if (LP_FLDNM.equals("EDC"))
			       L_RETSTR = staTXDOC[intAE_TX_EDCFL];
				else if (LP_FLDNM.equals("EHC"))
			       L_RETSTR = staTXDOC[intAE_TX_EHCFL];
	
			  else if (LP_FLDNM.equals("SCH"))
			       L_RETSTR = staTXDOC[intAE_TX_SCHFL];
				else if (LP_FLDNM.equals("TOT"))
			       L_RETSTR = staTXDOC[intAE_TX_TOTFL];
				else if (LP_FLDNM.equals("CST"))
				   L_RETSTR = staTXDOC[intAE_TX_CSTFL];
				else if (LP_FLDNM.equals("STX"))
				   L_RETSTR = staTXDOC[intAE_TX_STXFL];
				else if (LP_FLDNM.equals("VAT"))
			        L_RETSTR = staTXDOC[intAE_TX_VATFL];
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getTXDOC");
		}
		return L_RETSTR;
	}


		
		/** One time data capturing for specified codes from CO_CDTRN
		 * into the Hash Table
		 */
    private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable LP_HSTNM)
    {
		String L_strSQLQRY = "";
        try
		{
			L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp||cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
			//System.out.println(L_strSQLQRY);
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
				//setMSG("Records not found in CO_CDTRN",'E');
                return;
            }
            while(true)
            {
				strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                String[] staCDTRN = new String[intCDTRN_TOT];
                staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
                staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
                staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
                staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
                staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
                staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
                staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
                LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
						//hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
                if (!L_rstRSSET.next())
					break;
            }
            L_rstRSSET.close();
		}
        catch(Exception L_EX)
        {
             setMSG(L_EX,"crtCDTRN");
        }
	}
	
	/** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	*/
    private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
    {
		//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
        try
        {
			if (LP_FLDNM.equals("CMT_CODCD"))
				return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
            else if (LP_FLDNM.equals("CMT_CODDS"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
            else if (LP_FLDNM.equals("CMT_SHRDS"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
            else if (LP_FLDNM.equals("CMT_CHP01"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
            else if (LP_FLDNM.equals("CMT_CHP02"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
            else if (LP_FLDNM.equals("CMT_NMP01"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
            else if (LP_FLDNM.equals("CMT_NMP02"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
            else if (LP_FLDNM.equals("CMT_NCSVL"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
            else if (LP_FLDNM.equals("CMT_CCSVL"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return "";
    }


	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSSET		Result set name
	 * @param   LP_FLDNM        Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
			if (LP_FLDTP.equals("C"))
				return LP_RSSET.getString(LP_FLDNM) != null ? LP_RSSET.getString(LP_FLDNM).toString() : "";
				//return LP_RSSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSSET.getString(LP_FLDNM).toString()," ")) : "";
			else if (LP_FLDTP.equals("N"))
				return LP_RSSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSSET.getString(LP_FLDNM).toString(),"0") : "0";
			else if (LP_FLDTP.equals("D"))
				return LP_RSSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSSET.getDate(LP_FLDNM)) : "";
			else if (LP_FLDTP.equals("T"))
			    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSSET.getString(LP_FLDNM)));
			else 
				return " ";
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 
}