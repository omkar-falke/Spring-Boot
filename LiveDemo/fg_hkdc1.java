/*
System Name   : Finished Goods Inventory Management System
Program Name  : Day End Closing Confirmation.
Program Desc. : 1)Gives details regarding Opening Stock,Receipts,Issue & Closing Stock as per
				  Categorywise i.e Polystyrene & Speciality Polystyrene.
				2)The above details are differentiated as per Sub-Category wise i.e GPPS,HIPS 
				  for PS & PS Blend/Alloys,Compounded PS for SPS. 
				3)The above details are further differentiated as per Classified Qty. & 
				  UnClassified Qty. as per the Package Type.
				4)Again the above details are differentiated as per the Grade,Lot No., Location
				  & Qty.
Author        : 
Date          : 15th November 2001
Version       : FIMS 2.0
Modificaitons :
Modified By   :
Modified Date :18.10.2006
Modified det. :
Version       :

*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

class fg_hkdc1 extends cl_pbase
{
	JButton btnRUN,btnEXIT,btnLOCK;
	JTextField txtDATE,txtLCKDT,txtMAXDT;
	JOptionPane LM_OPTNPN;
	JTable LM_DTLTBL,LM_CHKTBL;
	TableColumn TCL_STKQT,TCL_ALOQT,TCL_LOTNO,TCL_VERQT;  
	Hashtable<String,String> hstERSTQT,hstERALQT,hstDIFFQT;
	int intCATEG = 1;
	int intOPNNG = 2;
	int intRECPT = 3;
	int intMISAD = 4;
	int intISSUE = 5;
	int intMISMI = 6;
    int intCLSNG = 7;
	int intVERQT = 8;
	
	int TB2_intLOTNO = 1;
	int TB2_intRCLNO = 2;
    int TB2_intMNLCD = 3;
	int TB2_intPKGTP = 4;
	int TB2_intDOSQT = 5;
	int TB2_intDOUQT = 6;
	int TB2_intRECPT = 7;
    int TB2_intISSUE = 8;
	int TB2_intSTKQT = 9;
	int TB2_intUCLQT = 10;
	int TB2_intALOQT = 11;
	
	int LM_LMRGN=0;
	int i = 0;
	int LM_COUNT = 0;
	int LM_CNT = 0;
	int LM_UPDCTR = 0;
	
	double dblDSTQT = 0;
	double dblDUCQT = 0;
	double dblDOSQT = 0;
	double dblDOUQT = 0;
	double dblDRCQT = 0;
	double dblDISQT = 0;
	double dblDALQT = 0;
	//double dblDOAQT = 0;
	//double LM_CURQT = 0;
	double dblDIFFQT= 0;
	
	String strSTKQT,strUCLQT,strDATE,strQUERY,strMNPRD,strDOPQT,strLOTNO,strRCLNO,strMNLCD,strPKGTP,strTRNTP,strPRDCD,strSTSFL,strMISAD,strCALQT,strDAY,strMXFNM;
	String strSBPRD,strSUBPD,strADDSTR,strPRDTP,strISSQT,strIPKGDS,strTOTQT,strWRHTP,strCONDN,strTRNQT,strSALTP,strDOSTK,strDOUCL,strMAXDT,strMISMI,strRECPT,strRESNO,strMONTH,strGAFNM;
	
	String strPRVPTP = "";
	String strPRVSTP = "";
	String strPKGCT = "";
	String strRTNSTR = "";
	
	//String strTDSVAR = "1"; //Target Despatch Variable i.e st_resfl = '1'
	String strSLRVAR = "2"; //Sales Return Variable i.e st_stsfl = '2'
	
	String LM_RESFIN = cl_dat.M_strREPSTR_pbst;
    String LM_RESSTR = LM_RESFIN.concat("\\LOCKFILE.doc"); 
	
    String LM_RESSTR1 = cl_dat.M_strREPSTR_pbst.concat("\\DE_RESRV.doc"); 
	String LM_OPTPMSG = "";
	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;
	CallableStatement cstCRFWD;	// Stored procedure for Booking Carry-forward reworking at the Month Starting
	CallableStatement cstHKDCC; // Stored procedure for Day-opening Stock replacement and for shifting un-invoiced despatches to next day.
	boolean flgLOCKFL = true;   //flag used to enable Lock button
	boolean flgINTFL = true;    //Flag used for initializing Monthly figures
	boolean flgRCLFL = true;    //Flag used for checking Reclassification PTF
	boolean flgINVFL = true;    //Flag used for checking LAs pending for Inv.preparation
	boolean flgRCTFL = true;    //Flag used for checking Receipts pending for authorisation
	boolean flgDGRFL = true;    //Flag used for checking DownGrading PTF
	boolean flgMAXFL = false;   //Flag used for initializing Stock Max. figures with current	
							    // stock during month end	
	
	/// New Variable
	String strSTLSQL="";
	String strSTRSQL="";
	String M_STLSQL="";
	String strCURTM="";
	String strUSRCD="";
	String L_strPKGTP="";
	//cl_eml ocl_eml = new cl_eml();
	String M_RCTQT_cldat="";
	String L_strPRDCD="";
	String strFRSRCT = "10"; //Fresh Receipt type
	String strRPKRCT = "15"; //Rebagging Receipt type
	String strSLRRCT = "30"; //Sales Return Receipt type
	
	String strSLRDSP = "30"; //Sales Return Despatch type
	String strDIRDSP = "10"; //Direct Despatch type
    String strDOMDSP = "01"; //Domestic Despatch Sale Type
    String strEXPDSP = "12"; //Export Despatch sale Type
    String strDEXDSP = "03"; //Deemed Export Despatch Sale Type
    String strSTFDSP = "04"; //Stock Transfer Despatch sale Type
    String strFTSDSP = "05"; //Free test Sample Despatch Sale Type
    String strCPCDSP = "16"; //Captive Consumption Despatch sale Type
	PreparedStatement pstmCHKDATFL,pstmADDDATFL;
	String strREFDT="";
	String strREFTM="";
	String strSTRTMP="";
	
	TableColumn LM_TBLCOL;
	DefaultTableCellRenderer numREN	;
	
	fg_hkdc1()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			//cstCRFWD = cl_dat.ocl_dat.conSPDBA.prepareCall("call setCRFQT(?,?)");
			//cl_dat.ocl_dat.M_PRGVER = " (v1.01)";
			//setLBLMOD();
			getREFDT();
			getMAXDT();
			//crtNUMREN();
			hstERSTQT = new Hashtable<String,String>();
			hstERALQT = new Hashtable<String,String>();
			hstDIFFQT = new Hashtable<String,String>();
			LM_OPTNPN = new JOptionPane();
			
			add(new JLabel("Date"),3,1,1,1,this,'L');
			add(txtDATE = new TxtDate(),3,2,1,1,this,'C');
			add(btnRUN = new JButton("Run"),3,3,1,1,this,'L');
			add(btnLOCK = new JButton("Lock"),3,4,1,1,this,'L');
			
			add(new JLabel("Previous Lock Date"),2,5,1,2,this,'L');
			add(txtLCKDT = new TxtLimit(18),3,5,1,2,this,'L');
				
			add(new JLabel("Max. Stock Date"),2,7,1,2,this,'L');
			add(txtMAXDT = new TxtLimit(20),3,7,1,2,this,'L');
			
		//	add(btnEXIT = new JButton("Exit"),4,4,1,1,this,'L');

			txtDATE.setText(getABDATE(strREFDT,1,'A'));
			txtLCKDT.setText(strREFDT+" "+strREFTM);
			//System.out.println("LLLLLLLL");
			txtMAXDT.setText(strMAXDT);
			txtLCKDT.setEnabled(false);
			txtMAXDT.setEnabled(false);
			btnRUN.setEnabled(false);
            btnLOCK.setEnabled(false);
			setENBL(false);
			//strDATE = txtLCKDT.getText();
			//strDATE = "06/05/2005";
			//System.out.println(strDATE);
			//System.out.println(cc_dattm.occ_dattm.setDBSDT(strDATE)+" / "+cc_dattm.occ_dattm.setDBSTM(cc_dattm.occ_dattm.getABDATE(strDATE,1,'A')+" "+cc_dattm.occ_dattm.getCURTIM()));
			
			INPVF objFGIPV=new INPVF();
		
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objFGIPV);
					//if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
					//	((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"fg_hkdc1");
		}
	}
	/**
	 * Function For Data OutPut Stream
	 */
	public static DataOutputStream crtDTOUTSTR(FileOutputStream outfile)
	{
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}
	/**
	 * Function For File OutPut Stream
	*/
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
			//System.out.println("L_IO FOS...........:"+L_IO);
			return outFILE;		
		}
	}
	/**
	 * Getting Maximum Locking Date And Time
	 */
	private void getMAXDT()
	{
		try
		{
			M_strSQLQRY = "Select cmt_ccsvl,cmt_ncsvl from co_cdtrn where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and cmt_cgstp='FGXXREF' and cmt_codcd='MAXDT'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				strMAXDT = nvlSTRVL(M_rstRSSET.getString("cmt_ccsvl").trim(),"")+" "+nvlSTRVL(M_rstRSSET.getString("cmt_ncsvl").trim(),"");
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMAXDT");
		}
	}
	
		
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if (L_KE.getSource().equals(txtDATE))
			{
				if(!vldDATE())
					return ;				
			}
		}
	}

	
	
	/**
	 * Validates LAs pending for Invoice preparation
	 */
	
	private boolean vldINVQT()
	{ 
		try
		{
			M_strSQLQRY = "Select IVT_LADNO, IVT_PRDDS, IVT_LADQT  from mr_ivtrn where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and date(ivt_loddt)= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"' and isnull(ivt_ladqt,0)>isnull(ivt_invqt,0)";
			//System.out.println("vldINVQT : "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//boolean L_IVCHK = false;
			LM_OPTPMSG = "";
			if(M_rstRSSET.next() && M_rstRSSET != null)
			{
				LM_OPTPMSG += "List of LAs Pending for Invoice Preparation \n\n";
				while (true)
				{
					LM_OPTPMSG += M_rstRSSET.getString("IVT_LADNO")+"    "+  M_rstRSSET.getString("IVT_PRDDS") +"    "+ M_rstRSSET.getString("IVT_LADQT")+" MT"+"\n";
					if (!M_rstRSSET.next())
						break;
				}
				//LM_OPTPMSG += "\n Locking Disabled \n";
				LM_OPTPMSG += "\n Please Confirm Before Locking \n";
				M_rstRSSET.close();
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldINVQT");
		}
		return true;
	}
	

	/**
	 */
	private boolean vldRCTAUT()
	{ //Validates Receipts pending for authorisation
		try
		{
			M_strSQLQRY = "Select RCT_RCTTP, RCT_RCTNO, sum(isnull(RCT_RCTQT,0)) RCT_RCTQT  from fg_rctrn where RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_STSFL = '1' and isnull(RCT_RCTQT,0)>0 and rct_rctdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"'  group by RCT_RCTTP, RCT_RCTNO ";
			M_strSQLQRY += " union Select LW_RCTTP RCT_RCTTP, LW_RCTNO RCT_RCTNO, sum(isnull(LW_BAGQT,0)) RCT_RCTQT  from fg_lwmst where LW_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and LW_STSFL = '1'  and isnull(LW_BAGQT,0)>0  and lw_rctdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"'  group by LW_RCTTP, LW_RCTNO order by RCT_RCTTP,RCT_RCTNO";
			//System.out.println("vldRCTAUT : "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//boolean L_RCTCHK = false;
			LM_OPTPMSG = "";
			if(M_rstRSSET.next() && M_rstRSSET != null)
			{
				LM_OPTPMSG += "RECEIPTS PENDING FOR AUTHORISATION \n\n";
				while (true)
				{
					LM_OPTPMSG += M_rstRSSET.getString("RCT_RCTTP")+"    "+  M_rstRSSET.getString("RCT_RCTNO") +"     "+ M_rstRSSET.getString("RCT_RCTQT")+" MT"+"\n";
					if (!M_rstRSSET.next())
						break;
				}
				LM_OPTPMSG += "\n Locking Disabled \n";
				LM_OPTPMSG += "\n PLEASE AUTHORISE PENDING RECEIPTS \n";
				M_rstRSSET.close();
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldRCTAUT");
		}
		return true;
	}
	
	/**
	 * 
	*/
	private boolean vldDATE()    //Validates Entered From Date
	{
		try
		{
			strDATE = txtDATE.getText().toString().trim();
			
			//System.out.println("strREFDT"+strREFDT);
			//System.out.println("strDATE"+strDATE);
			if(strDATE.length()==0)
			{
				setMSG("To Date can not be Blank ..",'E');
				txtDATE.requestFocus();
				return false;
			}		
			if(M_fmtLCDAT.parse(strDATE).compareTo(M_fmtLCDAT.parse(getABDATE(strREFDT,1,'A')))==0)
			{
				setMSG("Valid Date Format",'N');
				btnRUN.setEnabled(true);
				btnRUN.requestFocus();
				return true;
			}	
			else
			{
				if((M_fmtLCDAT.parse(strDATE).compareTo(M_fmtLCDAT.parse(getABDATE(strREFDT,1,'A')))<0))
				{
					setMSG("Invalid Date ...",'E');
					JOptionPane.showMessageDialog(this,"Locking of data till "+strREFDT+" has been performed.","Date Valid Status",JOptionPane.INFORMATION_MESSAGE);				
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Entered  date is greater then  Locking Date","Date Valid Status",JOptionPane.INFORMATION_MESSAGE);				
				}
					
					btnRUN.setEnabled(false);
					btnLOCK.setEnabled(false);
				txtDATE.requestFocus();
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATE");
		}
		return true;
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			//System.out.println("LLLLLLLL ACTIONP");
			strDATE=txtDATE.getText().toString().trim();
			if(M_objSOURC==btnRUN)
			{
				//this.setCursor(curWTSTS);
				setCursor(cl_dat.M_curWTSTS_pbst);
				//exeDSRITM();
				if(chkCDTRN())
				{
                   /// exeDERESV(); No need to call at shin HO presently
					flgLOCKFL = true;
					flgRCLFL = true;
					flgDGRFL = true;
					cl_dat.M_flgLCUPD_pbst = true;
					strDATE = txtDATE.getText().toString().trim();
					strCURTM = cl_dat.M_txtCLKTM_pbst.getText().trim();
					strUSRCD = cl_dat.M_strUSRCD_pbst;
					
					//cl_cust.ocl_cust.updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
					updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
					//cl_dat.ocl_dat.M_CHKTBL = false;
					String[] L_DTLHD = {"S.NO","Category","Opening","Receipts","Misc.+","Issues","Misc.-","Closing","Difference"};
					int[] L_DTLCSZ = {30,90,90,90,90,90,90,90,90};
					LM_CNT = 25;
					//LM_DTLTBL = crtDFLTBL(L_DTLHD,LM_CNT,0,0,750,247,10,120,750,249,L_DTLCSZ,0);
					LM_DTLTBL = crtTBLPNL1(this,L_DTLHD,100,5,1,8,8,L_DTLCSZ,new int[]{0});
                    TCL_VERQT = LM_DTLTBL.getColumn(LM_DTLTBL.getColumnName(intVERQT));
					setNUMREN(LM_DTLTBL,intOPNNG);
					setNUMREN(LM_DTLTBL,intRECPT);
					setNUMREN(LM_DTLTBL,intMISAD);
					setNUMREN(LM_DTLTBL,intISSUE);
					setNUMREN(LM_DTLTBL,intMISMI);
					setNUMREN(LM_DTLTBL,intCLSNG);
					TCL_VERQT.setCellRenderer(new RowRenderer3());
					//LM_DTLTBL.addMouseListener(this);

					getALLREC(LM_DTLTBL);
					//System.out.println("11111111111");
					setMSG("Deleting Lock Table record.",'N');
					delLCKTBL("");
					//System.out.println("222222222222");
					setMSG("Copying Stock Table into Lock Table.",'N');
					cpyLCKTBL();
					//System.out.println("333333333333");
					setMSG("Updating Receipt Qty. into Lock Table.",'N');
					updRCTQT();
					//System.out.println("4444444444");
					setMSG("Updating Issue Qty. into Lock Table.",'N');
					updISSQT();
					//System.out.println("5555555555");
					strCONDN = "where lk__CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and (lk_dosqt+lk_douqt+lk_rctqt)-lk_issqt=(lk_stkqt+lk_uclqt)";  
					strCONDN += " and lk_aloqt=0 and (lk_dosqt >= 0 and lk_douqt >= 0 and";    
					strCONDN += " lk_stkqt >= 0 and lk_uclqt >= 0)";                           
 					delLCKTBL(strCONDN);
					//System.out.println("6666666666666");
					setMSG("Updating Loading Advice details into Lock Table.",'N');
					chkINVTRN();
					//System.out.println("77777777777");
					dspERRTRN();

					//System.out.println("8888888888");
					M_strSQLQRY = "Select count(*) from FG_STMST,PR_LTMST where ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ST_PRDTP=LT_PRDTP";
					M_strSQLQRY += " and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and ST_RCLNO > '00' and ST_UCLQT > 0";
					M_strSQLQRY += " and ((ST_PRDTP='01' and LT_CLSFL='9') or (ST_PRDTP='02' and LT_CLSFL in ('0','9')))";

					//if(cl_dat.ocl_dat.getRECCNT("SP","ACT",M_strSQLQRY) > 0)
					if(getRECCNT(M_strSQLQRY) > 0)
						flgRCLFL = false;
					else
					{ 
						M_strSQLQRY = "Select count(*) from FG_STMST,PR_LTMST where ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ST_CMPCD = LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO";
						M_strSQLQRY += " and ST_RCLNO=LT_RCLNO and ST_RCLNO='00' and ST_UCLQT > 0 and LT_STSFL='7'";
						M_strSQLQRY += " and ((ST_PRDTP='01' and LT_CLSFL='9') or (ST_PRDTP='02' and LT_CLSFL in ('0','9')))";
						//if(cl_dat.ocl_dat.getRECCNT("SP","ACT",M_strSQLQRY) > 0)
						if(getRECCNT(M_strSQLQRY) > 0)
							flgDGRFL = false;
					}

					flgRCTFL = true;
					if(!vldRCTAUT())
						flgRCTFL = false;
					if(flgRCTFL == false)
						LM_OPTNPN.showMessageDialog(this,LM_OPTPMSG,"PENDING AUTHORISATION",JOptionPane.INFORMATION_MESSAGE);

					flgINVFL = true;
					if(!vldINVQT())
						flgINVFL = false;

					if(flgINVFL == false)
						LM_OPTNPN.showMessageDialog(this,LM_OPTPMSG,"Error Message",JOptionPane.INFORMATION_MESSAGE);
					
					if(flgLOCKFL && flgRCLFL && flgDGRFL && flgRCTFL)
					{
						btnLOCK.setEnabled(true);
						btnLOCK.requestFocus();
						setMSG("Press Lock Button to Lock Yesterday's data",'N');
					}
					else
					{
						if(flgRCLFL == false)
							setMSG("ReClassification PTF is pending.",'E');
						if(flgDGRFL == false)
							setMSG("DownGrading PTF is pending.",'E');
						if(flgLOCKFL == false)
							setMSG("InValid Transactions monitored.",'E');
					}
					strCURTM = "";
					strUSRCD = "";
					//cl_cust.ocl_cust.updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
					updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
				}
				else
					setMSG("Please wait in Use",'N');
				//this.setCursor(curDFSTS);
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==btnLOCK)
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
                btnLOCK.setEnabled(false);
                cl_dat.M_flgLCUPD_pbst = true;
                M_STLSQL = "Update FG_STMST set ";
    				M_STLSQL += "ST_PDOST = ST_DOSQT";
				
				//cl_dat.ocl_dat.M_STRSQL = M_STLSQL;
				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
				if(cl_dat.exeDBCMT("Setting Previous day Opening Stock"))
				{
					setMSG("Setting Previous day Opening Stock",'N');
				}
				else
				{
					setMSG("Error Setting Previous day Opening Stock ",'E');
					return;
				}
			    ////	exeDSRITM(); // commented temporarily
				//ocl_eml.setFRADR("ptd@spl.co.in");  
				flgINTFL = true;
				flgMAXFL = false;
				cl_dat.M_flgLCUPD_pbst = true;
				strDATE = txtDATE.getText().toString().trim();
				//String L_PRVDT = cc_dattm.occ_dattm.getABDATE(strDATE,1,'B').toString().trim();
				//String L_NEXDT = cc_dattm.occ_dattm.getABDATE(strDATE,1,'A').toString().trim();
			
				String L_strPRVDT = getABDATE(strDATE,1,'B');
				String L_strNEXDT = getABDATE(strDATE,1,'A');

				//System.out.println("1100000000000");
				String L_strCURMTH = strDATE.substring(3,5).trim();
				String L_strPRVMTH = L_strPRVDT.substring(3,5).trim();
				String L_strNEXMTH = L_strNEXDT.substring(3,5).trim();
				if(!L_strCURMTH.equals(L_strPRVMTH))
				{
					int L_SELOPT = LM_OPTNPN.showConfirmDialog(null,"Do You Want to Reset Monthly figures to zero?","Locking Status",JOptionPane.YES_NO_OPTION);
					if(L_SELOPT == 0)
					{
						setMSG("Resetting monthly figures of opening stock table.",'N');
						rstMTHSTK(); //resetting Monthly figures to 0 once the month changes
						flgMAXFL = true;
					}
					else
					{
						setMSG("Monthly figures not initialized. Locking Terminated",'E');					
						flgINTFL = false;
					}
				}
				//System.out.println("2200000000000");
				if(flgINTFL)
				{
				    
					if(!cl_dat.M_flgLCUPD_pbst)
						return;
					setMSG("Updating Locking Date.",'N');
					lckREFDT();
					
				
					setMSG("Resetting stock related figures of opening stock table.",'N');
					rstOPSTK(); //resetting daily & stock figures to 0
					setMSG("Updating Receipt Qty. of Opening Stock table",'N');
					//cl_cust.ocl_cust.modRCTQT(strDATE,strDATE,false,false,true,true); //update op_yxrqt,op_mxrqt,op_yrcqt,op_mrcqt in fg_opstk table
					modRCTQT(strDATE,strDATE,false,false,true,true); //update op_yxrqt,op_mxrqt,op_yrcqt,op_mrcqt in fg_opstk table 
					setMSG("Updating Despatch Qty. of Opening Stock table",'N');
					//cl_cust.ocl_cust.modISSQT(strDATE,strDATE,false,false,true,true); //update despatch qty of all saletype in fg_opstk table
					modISSQT(strDATE,strDATE,false,false,true,true); //update despatch qty of all saletype in fg_opstk table 
					setMSG("Updating Maximum Stock for the current month.",'N');
					updMAXQT(); //Updating Maximum Stock for the current month.
					setMSG("Updating Opening Stock.",'N');
					updSTKQT(); //updates day opening stock qty as stk qty and day opening UnClsfd qty as Unclsfd qty. 
					setMSG("Updating Excise Data Files.",'N');
					setMSG("Updating Stock related Qty. of Opening Stock table",'N');
					modSTKQT(); //updates stock qty in fg_opstk table 
				//	System.out.println("before EX_DATFL");
					EX_DATFL("RC",strDATE);
					EX_DATFL("C1",strDATE);
					EX_DATFL("C3",strDATE);
					EX_DATFL("RB",strDATE);
					EX_DATFL("JR",strDATE);
					EX_DATFL("CJ",strDATE);
					EX_DATFL("CS",strDATE);
				//	System.out.println("end EX_DATFL");
					/*setMSG("Sending Data availability message to Excise Dept.",'N');
					O_FOUT = crtFILE(LM_RESSTR);
					O_DOUT = crtDTOUTSTR(O_FOUT);	
					O_DOUT.writeBytes("Transaction Data for the Day "+strDATE+" are available.");
					O_DOUT.close();
					O_FOUT.close();
					//System.out.println("3300000000000");
					ocl_eml.setFRADR("ptd@spl.co.in"); 
                    ocl_eml.sendfile("excise@spl.co.in",LM_RESSTR,"Transaction Data for the Day "+strDATE+" are available.","");
                    ocl_eml.sendfile("excise@spl.co.in",LM_RESSTR,"Day closing for "+strDATE+" is Over at "+strCURTM+" Hrs","");
                    ocl_eml.sendfile("cms@spl.co.in",LM_RESSTR,"Day closing for "+strDATE+" is Over at "+strCURTM+" Hrs","");
					*/
                    //System.out.println("4400000000000");
				
					if(cl_dat.M_flgLCUPD_pbst)
					{
						//Current Locking month is compared with next month, if the month
						//has changed i.e next date is 1st, then Maximum Stock Summary &		
						//Gradewise Aging Report (based on day opening stock) is generated &
						//mailed to concerned persons.
						setMSG("Locking Completed.",'N');
                        // Prsently commented for shinho
                        if(!L_strCURMTH.equals(L_strNEXMTH))
						{
						    
							setMSG("Generating Maximum Stock.",'N');
							fg_rpmax ofg_rpmax = new fg_rpmax();
							ofg_rpmax.getALLREC();
							setMSG("Generating Aging Report.",'N');
							fg_qrgra ofg_qrgra = new fg_qrgra();
							ofg_qrgra.getALLREC(strDATE,false);
							
							setMSG("Mailing Maximum Stock & Aging Report.",'N');
							strDAY =  cl_dat.M_strLOGDT_pbst.substring(0,2).toString().trim();
							strMONTH =  cl_dat.M_strLOGDT_pbst.substring(3,5).toString().trim();
							strMXFNM = "fgmx"+strDAY+strMONTH+".doc";
							strDAY = strDATE.substring(0,2).toString().trim();
							strMONTH = strDATE.substring(3,5).toString().trim();
							strGAFNM = "fgga"+strDAY+strMONTH+".doc";
							
							/*ocl_eml.sendfile("dilip_deole@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strMXFNM),"Max. Stock Summary","Max. Stock Summary");
                            ocl_eml.sendfile("dilip_deole@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strGAFNM),"Gradewise Aging Summary","Gradewise Aging Summary");
                            
							ocl_eml.sendfile("n_gopal@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strMXFNM),"Max. Stock Summary","Max. Stock Summary");
                            ocl_eml.sendfile("n_gopal@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strGAFNM),"Gradewise Aging Summary","Gradewise Aging Summary");
							
                            ocl_eml.sendfile("rakesh_nayyar@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strMXFNM),"Max. Stock Summary","Max. Stock Summary");
                            ocl_eml.sendfile("rakesh_nayyar@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strGAFNM),"Gradewise Aging Summary","Gradewise Aging Summary");
							
                            ocl_eml.sendfile("rp_shinde@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strMXFNM),"Max. Stock Summary","Max. Stock Summary");
                            ocl_eml.sendfile("rp_shinde@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strGAFNM),"Gradewise Aging Summary","Gradewise Aging Summary");
							
                            ocl_eml.sendfile("kv_mujumdar@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strMXFNM),"Max. Stock Summary","Max. Stock Summary");
                            ocl_eml.sendfile("kv_mujumdar@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strGAFNM),"Gradewise Aging Summary","Gradewise Aging Summary");
							
                            ocl_eml.sendfile("hb_pandit@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strMXFNM),"Max. Stock Summary","Max. Stock Summary");
                            ocl_eml.sendfile("hb_pandit@spl.co.in",cl_dat.M_strREPSTR_pbst.concat("\\"+strGAFNM),"Gradewise Aging Summary","Gradewise Aging Summary");
                            */
						}
						setMSG("Locking Completed.",'N');
					}
					else
					setMSG("Locking Failed.",'E');
				}
				else
				{
					setMSG("Monthly figures not initialized.Locking Terminated.Contact Systems Department",'E');
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
				//ocl_eml.setFRADR("ptd@spl.co.in");  
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"btnLOCK");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		//txtDATE.setText(getABDATE(strREFDT,1,'A'));
		btnRUN.setEnabled(false);
		btnLOCK.setEnabled(false);
		txtMAXDT.setEnabled(false);
		txtLCKDT.setEnabled(false);
	}
	class RowRenderer3 extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isselected,boolean hasFocus,int row,int col)
		{
			if(hstDIFFQT.get(String.valueOf(row))  != null)
				setForeground(Color.blue);
			else
				setForeground(Color.black);
			int cellValue = (value instanceof Number) ? ((Number)value).intValue() : 0;
			setText((value == null) ? "" : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);
			return super.getTableCellRendererComponent(table,value,isselected,hasFocus,row,col);
		}
	}
	
	private void exeDERESV()
	{
		try
		{
			boolean L_EMLFL = false;
			//System.out.println("strDATE"+strDATE);
			String L_strWRHTP,L_strPRDTP,L_strLOTNO,L_strPKGTP,L_strMNLCD,L_strRESNO,L_strREXDT,L_STKQT,L_strPRDCD,L_REMDS; 
			M_rstRSSET = null;
			M_strSQLQRY = "Select ST_WRHTP, ST_PRDTP, ST_LOTNO, ST_RCLNO, ST_PKGTP, ST_MNLCD, ST_REMDS, ST_RESNO, ST_REXDT, ST_STKQT, ST_PRDCD from FG_STMST";
			M_strSQLQRY += " where ST__CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and (ST_RESNO <> '' and ST_RESNO is not null)";
			M_strSQLQRY += " and (ST_REXDT is not null and ST_REXDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"')";
			//System.out.println("M_strSQLQRY");
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			setMSG("de-reservation of Stock is in Progress ...   Please wait ...",'N');
			if(M_rstRSSET != null)
			{
				O_FOUT = crtFILE(LM_RESSTR1);
				O_DOUT = crtDTOUTSTR(O_FOUT); 
				O_DOUT.writeBytes(padSTRING('R',"Res.No.",15)+padSTRING('R',"Exp.Date",15)+padSTRING('R',"Lot No.",15)+padSTRING('R',"Grade",20)+padSTRING('L',"Stock",8)+padSTRING('R',"Remarks",30));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes("\n");
				while (M_rstRSSET.next())
				{
					L_strWRHTP = nvlSTRVL(M_rstRSSET.getString("ST_WRHTP"),"");
					L_strPRDTP = nvlSTRVL(M_rstRSSET.getString("ST_PRDTP"),"");
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("ST_LOTNO"),"");
					L_strPKGTP = nvlSTRVL(M_rstRSSET.getString("ST_PKGTP"),"");
					L_strMNLCD = nvlSTRVL(M_rstRSSET.getString("ST_MNLCD"),"");
					L_strRESNO = nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"");
					L_REMDS = nvlSTRVL(M_rstRSSET.getString("ST_REMDS"),"");
					//L_strREXDT = cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("ST_REXDT"));
					L_strREXDT = M_rstRSSET.getDate("ST_REXDT") != null ?M_fmtLCDAT.format(M_rstRSSET.getDate("ST_REXDT")):"";
					//L_strREXDT = cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("ST_REXDT"));
					L_STKQT = nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"");
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("ST_PRDCD"),"");
					strSTRSQL = "update FG_STMST set ST_RESCD = ' ',";
					strSTRSQL += "ST_RESDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(nvlSTRVL("","null")))+"',";
					strSTRSQL += "ST_REXDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(nvlSTRVL("","null")))+"',";
					strSTRSQL += "ST_RESNO = ' ',";
					strSTRSQL += "ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
					strSTRSQL += "ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(nvlSTRVL( cl_dat.M_strLOGDT_pbst,"null")))+"' ";
					strSTRSQL += " where ";
					strSTRSQL += "ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ST_WRHTP = '"+L_strWRHTP+"' and ";
					strSTRSQL += "ST_PRDTP = '"+L_strPRDTP+"' and ";
					strSTRSQL += "ST_LOTNO = '"+L_strLOTNO+"' and ";
					strSTRSQL += "ST_PKGTP = '"+L_strPKGTP+"' and ";
					strSTRSQL += "ST_MNLCD = '"+L_strMNLCD+"'";
					if (Double.parseDouble(L_STKQT)>0)
					{
					   //O_DOUT.writeBytes(padSTRING('R',L_strRESNO,15)+padSTRING('R',L_strREXDT,15)+padSTRING('R',L_strLOTNO,15)+padSTRING('R',cl_cust.ocl_cust.getPRDDS(L_strPRDCD),20)+padSTRING('L',L_STKQT,8)+padSTRING('R',L_REMDS,30));
					   O_DOUT.writeBytes(padSTRING('R',L_strRESNO,15)+padSTRING('R',L_strREXDT,15)+padSTRING('R',L_strLOTNO,15)+padSTRING('R',getPRDDS(L_strPRDCD),20)+padSTRING('L',L_STKQT,8)+padSTRING('R',L_REMDS,30));
					   O_DOUT.writeBytes("\n");
					   L_EMLFL = true;
					} 
					cl_dat.exeSQLUPD(strSTRSQL,"");
					//cl_dat.ocl_dat.exeDBCMT("SP","ACT","CMT");
					if(cl_dat.M_flgLCUPD_pbst)
					{
						if(cl_dat.exeDBCMT("save"))
						{
							setMSG("SAVE ",'N');
						}
					}
				}
				if(M_rstRSSET.next())
					M_rstRSSET.close();
				O_DOUT.close();
				O_FOUT.close();
				/*if(L_EMLFL)
				{
					//cl_eml ocl_eml1 = new cl_eml();
					 ocl_eml.sendfile("hb_pandit@spl.co.in",LM_RESSTR1,"deReservation ","deReservation");
					 ocl_eml.sendfile("sr_deshpande@spl.co.in",LM_RESSTR1,"deReservation ","deReservation");
					 ocl_eml.sendfile("kv_mujumdar@spl.co.in",LM_RESSTR1,"deReservation ","deReservation");
					 ocl_eml.sendfile("rp_shinde@spl.co.in",LM_RESSTR1,"deReservation ","deReservation");
					 ocl_eml.sendfile("systems@spl.co.in",LM_RESSTR1,"deReservation ","deReservation");
				}*/
			}
			
		 }
		 // try
		catch (Exception L_EX) 
		{
			setMSG(L_EX,"exeDERESV");
        } // catch (Exception L_EX)
	}

 	private void getALLREC(JTable LP_DTLTBL)
	{
		try
		{
			String L_strPRDTP = "";
			String L_SUBPD = "";
			strPRVPTP = "";
			strPRVSTP = "";
			M_strSQLQRY = "Select SUBSTRING(ST_PRDCD,1,2) L_PRDTP,SUBSTRING(ST_PRDCD,1,4) L_SUBPD from FG_STMST";
			M_strSQLQRY += " where _CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and (ST_STKQT+ST_UCLQT+ST_DOSQT+ST_DOUQT) > 0 group by   SUBSTRING(ST_PRDCD,1,2),SUBSTRING(ST_PRDCD,1,4) order by SUBSTRING(ST_PRDCD,1,2),SUBSTRING(ST_PRDCD,1,4)";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i = 0;
			while(M_rstRSSET.next())
			{
				strPRDTP = M_rstRSSET.getString("L_PRDTP");
				strSUBPD = M_rstRSSET.getString("L_SUBPD");
				strADDSTR = "";
				if(!(strPRDTP.equals(strPRVPTP)))
				{
					//strMNPRD = cl_cust.ocl_cust.getMNPRD(strPRDTP); // gets the Main product Description
					strMNPRD = getMNPRD(strPRDTP); // gets the Main product Description
					setMSG("Updating: "+strMNPRD,'N');
					strADDSTR = " ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(ST_PRDCD,1,2)='"+strPRDTP+"'"; 
					calOPCLQT(); // To calculate sum of Main Product
					strADDSTR = " LT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(LT_PRDCD,1,2)='"+strPRDTP+"' and RCT_RCTTP in ('10','15','30','21','22','23')"; 
					//cl_cust.ocl_cust.exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					strRECPT = M_RCTQT_cldat;
					LP_DTLTBL.setValueAt(strRECPT,i,intRECPT);
					strADDSTR = " LT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(LT_PRDCD,1,2)='"+strPRDTP+"' and RCT_RCTTP in ('40','16','50', '51','61')"; 
					//cl_cust.ocl_cust.exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					LP_DTLTBL.setValueAt(M_RCTQT_cldat,i,intMISAD);
					strMISAD = M_RCTQT_cldat;
					strADDSTR = " IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(IST_PRDCD,1,2)='"+strPRDTP+"'"; 
                    strADDSTR += " and IST_ISSTP in ('10','30') and IST_SALTP IN ('01','12','03','04','05','16')";
					strISSQT = calISSQT();
					strADDSTR = " IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(IST_PRDCD,1,2)='"+strPRDTP+"'"; 
					strADDSTR += " and IST_ISSTP in ('40','16','50','51','61')";
					strMISMI = calISSQT();
					LP_DTLTBL.setValueAt(strMNPRD.trim(),i,intCATEG);
					LP_DTLTBL.setValueAt(strDOPQT,i,intOPNNG);
					LP_DTLTBL.setValueAt(strISSQT,i,intISSUE);
					LP_DTLTBL.setValueAt(strMISMI,i,intMISMI);
					LP_DTLTBL.setValueAt(strTOTQT,i,intCLSNG);
					//strCALQT = setFMT("RND",String.valueOf((Double.parseDouble(strDOPQT)+Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD))-(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI))),3);
					strCALQT = setNumberFormat(((Double.parseDouble(strDOPQT)+Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD))-(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI))),3);
					dblDIFFQT = Double.parseDouble(strCALQT) - Double.parseDouble(strTOTQT);
					if(dblDIFFQT != 0)
					{
						hstDIFFQT.put(String.valueOf(i),"X");
						//System.out.println("Opening Stock: "+Double.parseDouble(strDOPQT));
						//System.out.println("Total Receipt: "+(Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD)));
						//System.out.println("Total Issue: "+(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI)));
						//System.out.println("Total Stock: "+((Double.parseDouble(strDOPQT)+Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD))-(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI))));
						//System.out.println("Closing Stock: "+Double.parseDouble(strTOTQT));
					}
					//LP_DTLTBL.setValueAt(setFMT("RND",String.valueOf(dblDIFFQT),3),i,intVERQT);
					LP_DTLTBL.setValueAt(setNumberFormat(dblDIFFQT,3),i,intVERQT);
					strPRVPTP = strPRDTP;
					i++;
				}
				if(!strSUBPD.equals(strPRVSTP))
				{
					//strSBPRD = cl_cust.ocl_cust.getSBPRD(strSUBPD); //To get Sub Product Code i.e HIPS,GPPS
					strSBPRD = getSBPRD(strSUBPD); //To get Sub Product Code i.e HIPS,GPPS
					setMSG("Updating: "+strSBPRD,'N');
					strADDSTR = " ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(ST_PRDCD,1,2)='"+strPRDTP+"' and SUBSTRING(ST_PRDCD,1,4)='"+strSUBPD+"'";
					calOPCLQT(); // To calculate sum of Sub Product
					strADDSTR = " LT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(LT_PRDCD,1,2)='"+strPRDTP+"' and SUBSTRING(LT_PRDCD,1,4)='"+strSUBPD+"' and RCT_RCTTP in ('10','15','30','21','22','23')"; 
					//cl_cust.ocl_cust.exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					strRECPT = M_RCTQT_cldat;
					LP_DTLTBL.setValueAt(strRECPT,i,intRECPT);
					strADDSTR = " LT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(LT_PRDCD,1,2)='"+strPRDTP+"' and SUBSTRING(LT_PRDCD,1,4)='"+strSUBPD+"' and RCT_RCTTP in ('40','16','50','51','61')"; 
					//cl_cust.ocl_cust.exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					exeRCTQRY(M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)),strADDSTR);
					LP_DTLTBL.setValueAt(M_RCTQT_cldat,i,intMISAD);
					strMISAD = M_RCTQT_cldat;
					strADDSTR = " IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(IST_PRDCD,1,2)='"+strPRDTP+"' and SUBSTRING(IST_PRDCD,1,4)='"+strSUBPD+"'";
                    strADDSTR += " and IST_ISSTP in ('10','30') and IST_SALTP IN ('01','12','03','04','05','16')";
					strISSQT = calISSQT();
					strADDSTR = " IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(IST_PRDCD,1,2)='"+strPRDTP+"' and SUBSTRING(IST_PRDCD,1,4)='"+strSUBPD+"'";
					strADDSTR += " and IST_ISSTP in ('40','16','50','51','61')";
					strMISMI = calISSQT();
					LP_DTLTBL.setValueAt(strSBPRD.trim(),i,intCATEG);
					LP_DTLTBL.setValueAt(strDOPQT,i,intOPNNG);
					LP_DTLTBL.setValueAt(strISSQT,i,intISSUE);
					LP_DTLTBL.setValueAt(strMISMI,i,intMISMI);
					LP_DTLTBL.setValueAt(strTOTQT,i,intCLSNG);
					//strCALQT = setFMT("RND",String.valueOf((Double.parseDouble(strDOPQT)+Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD))-(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI))),3);
					strCALQT = setNumberFormat((Double.parseDouble(strDOPQT)+Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD))-(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI)),3);
					dblDIFFQT = Double.parseDouble(strCALQT) - Double.parseDouble(strTOTQT);
					if(dblDIFFQT != 0)
					{
						hstDIFFQT.put(String.valueOf(i),"X");
						//System.out.println("Opening Stock: "+Double.parseDouble(strDOPQT));
						//System.out.println("Total Receipt: "+(Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD)));
						//System.out.println("Total Issue: "+(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI)));
						//System.out.println("Total Stock: "+((Double.parseDouble(strDOPQT)+Double.parseDouble(strRECPT)+Double.parseDouble(strMISAD))-(Double.parseDouble(strISSQT)+Double.parseDouble(strMISMI))));
						//System.out.println("Closing Stock: "+Double.parseDouble(strTOTQT));
					}
					//LP_DTLTBL.setValueAt(setFMT("RND",String.valueOf(dblDIFFQT),3),i,intVERQT);
					LP_DTLTBL.setValueAt(setNumberFormat(dblDIFFQT,3),i,intVERQT);
					strPRVSTP = strSUBPD;
					i++;
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	
	private void calOPCLQT()
	{
		try
		{
			strDOPQT = "";
			strTOTQT = "";
			ResultSet L_RSLSET1;
			strQUERY = "Select sum(ST_DOSQT+ST_DOUQT) L_DOPQT,sum(ST_STKQT+ST_UCLQT) L_TOTQT";
			strQUERY += " from FG_STMST where" + strADDSTR;
			L_RSLSET1 = cl_dat.exeSQLQRY2(strQUERY);
			if(L_RSLSET1.next())
			{
				strDOPQT = nvlSTRVL(L_RSLSET1.getString("L_DOPQT"),"").trim();
				strTOTQT = nvlSTRVL(L_RSLSET1.getString("L_TOTQT"),"").trim();
			}
			L_RSLSET1.close();
			if(strDOPQT.length() == 0)
				strDOPQT = "0.000";
			else
				strDOPQT = setNumberFormat(Double.parseDouble(strDOPQT),3);
			if(strTOTQT.length() == 0)
				strTOTQT = "0.000";
			else
				strTOTQT = setNumberFormat(Double.parseDouble(strTOTQT),3);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calOPCLQT");
		}
	}
	
	private String calISSQT()
	{
		strRTNSTR = "";
		try
		{
			ResultSet L_RSLSET1;
			strQUERY = "Select sum(IST_ISSQT) L_ISSQT from FG_ISTRN where ";
			strQUERY += strADDSTR; 
			strQUERY += " and IST_AUTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"' and IST_STSFL='2'";
			L_RSLSET1 = cl_dat.exeSQLQRY2(strQUERY);
			if(L_RSLSET1.next())
			{
				strRTNSTR = nvlSTRVL(L_RSLSET1.getString("L_ISSQT"),"").trim();
			}
			L_RSLSET1.close();
			if(strRTNSTR.length() == 0)
				strRTNSTR = "0.000";
			else
				strRTNSTR = setNumberFormat(Double.parseDouble(strRTNSTR),3);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calISSQT");
		}
		return strRTNSTR;
	}
	
	private void delLCKTBL(String LP_CONDN)
	{
		try
		{
			strSTLSQL = "Delete from FG_LKTRN " + LP_CONDN;
			cl_dat.exeSQLUPD(strSTLSQL," ");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("save"))
				{
					setMSG("SAVE ",'N');
				}
					
			}
			else
				setMSG("Error In Deleting ",'E');
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"delLCKTBL");
		}
	}
	
	private void cpyLCKTBL()
	{
		try
		{
			strSTLSQL = "Insert into fg_lktrn select '"+cl_dat.M_strCMPCD_pbst+"',st_wrhtp,st_prdtp,st_lotno,st_mnlcd,st_rctdt,";  
			strSTLSQL += "st_stkqt,st_tprcd,st_cprcd,st_trnfl,st_stsfl,st_lusby,st_lupdt,";
			strSTLSQL += "st_aloqt,st_prdcd,st_pkgct,st_uclqt,st_dosqt,st_pkgtp,st_rclno,";  
			strSTLSQL += "0,0,'','',st_douqt,st_pkgwt,st_resfl,st_remds,'' from fg_stmst";
			strSTLSQL += " where ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and (st_dosqt+st_douqt+st_stkqt+st_uclqt) > 0";
			cl_dat.exeSQLUPD(strSTLSQL,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("save"))
				{
					setMSG("SAVE ",'N');
				}
			}
			else
			{
				setMSG("Error insert cpyLCKTBL ",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"cpyLCKTBL");
		}
	}
	
	private void updRCTQT()
	{
	    //System.out.println(cl_dat.M_flgLCUPD_pbst);
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				String L_RCTQT = "";
				String L_SUMQT = "";
				
				strSTLSQL = "Select RCT_WRHTP,RCT_PRDTP,RCT_LOTNO,RCT_RCLNO,RCT_PKGTP,RCT_MNLCD,sum(RCT_RCTQT) L_SUMQT";
				strSTLSQL += " from FG_RCTRN where RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_RCTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"' and RCT_STSFL='2'";
				strSTLSQL += " group by RCT_WRHTP,RCT_PRDTP,RCT_LOTNO,RCT_RCLNO,RCT_PKGTP,RCT_MNLCD";
				strSTLSQL += " order by RCT_WRHTP,RCT_PRDTP,RCT_LOTNO,RCT_RCLNO,RCT_PKGTP,RCT_MNLCD";
				//System.out.println(strSTLSQL);
				M_rstRSSET = cl_dat.exeSQLQRY1(strSTLSQL);
				
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						strWRHTP = M_rstRSSET.getString("RCT_WRHTP");
						strPRDTP = M_rstRSSET.getString("RCT_PRDTP");
						strLOTNO = M_rstRSSET.getString("RCT_LOTNO");
						strRCLNO = M_rstRSSET.getString("RCT_RCLNO");
						strPKGTP = M_rstRSSET.getString("RCT_PKGTP");
						strMNLCD = M_rstRSSET.getString("RCT_MNLCD");
						L_RCTQT = M_rstRSSET.getString("L_SUMQT");
						setMSG("Updating Lot No. "+strLOTNO+" Receipt Qty.",'N');
						if(L_RCTQT != null)
							addLCKTBL(L_RCTQT,"FG_RCTRN");
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"updRCTQT");
			}
		}
	}
	
	private void updISSQT()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				String L_ISSQT = "";
				String L_SUMQT = "";
				
				strSTLSQL = "Select IST_WRHTP,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PKGTP,IST_MNLCD,sum(IST_ISSQT) L_SUMQT";
				strSTLSQL += " from FG_ISTRN where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_AUTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"' and IST_STSFL='2'";
				strSTLSQL += " group by IST_WRHTP,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PKGTP,IST_MNLCD";
				strSTLSQL += " order by IST_WRHTP,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PKGTP,IST_MNLCD";
				
				M_rstRSSET = cl_dat.exeSQLQRY1(strSTLSQL);
				
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						strWRHTP = M_rstRSSET.getString("IST_WRHTP");
						strPRDTP = M_rstRSSET.getString("IST_PRDTP");
						strLOTNO = M_rstRSSET.getString("IST_LOTNO");
						strRCLNO = M_rstRSSET.getString("IST_RCLNO");
						strPKGTP = M_rstRSSET.getString("IST_PKGTP");
						strMNLCD = M_rstRSSET.getString("IST_MNLCD");
						L_ISSQT = M_rstRSSET.getString("L_SUMQT");
						setMSG("Updating Lot No. "+strLOTNO+" Issue Qty.",'N');
						if(L_ISSQT != null)
							addLCKTBL(L_ISSQT,"FG_ISTRN");
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"updISSQT");
			}
		}
	}
	
	private void addLCKTBL(String LP_UPDQT,String LP_TBLNM)
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				strSTLSQL = "Select count(*) from fg_lktrn";
				strSTLSQL += " where lk_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and lk_wrhtp = '"+strWRHTP+"'";
				strSTLSQL += " and lk_prdtp = '"+strPRDTP+"'";
				strSTLSQL += " and lk_lotno = '"+strLOTNO+"'";
				strSTLSQL += " and lk_rclno = '"+strRCLNO+"'";
				strSTLSQL += " and lk_pkgtp = '"+strPKGTP+"'";
				strSTLSQL += " and lk_mnlcd = '"+strMNLCD+"'";
				//if(cl_dat.ocl_dat.getRECCNT("SP","ACT",strSTLSQL) > 0)
				if(getRECCNT(strSTLSQL) > 0)
					updLCKTBL(LP_UPDQT,LP_TBLNM);
				else
					insLCKTBL(LP_UPDQT,LP_TBLNM);
				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("save"))
					{
						setMSG("SAVE addLCKTBL ",'N');
					}
				}
				else
				{
					setMSG("Error insert addLCKTBL ",'E');
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"addLCKTBL");
			}
		}
	}
	
	private void updLCKTBL(String LP_UPDQT,String LP_TBLNM)
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				M_STLSQL = "Update FG_LKTRN set ";
				if(LP_TBLNM.equals("FG_RCTRN"))
					M_STLSQL += "LK_RCTQT = LK_RCTQT + "+LP_UPDQT+"";
				if(LP_TBLNM.equals("FG_ISTRN"))
					M_STLSQL += "LK_ISSQT = LK_ISSQT + "+LP_UPDQT+"";
				M_STLSQL += " where lk_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and lk_wrhtp = '"+strWRHTP+"'";
				M_STLSQL += " and lk_prdtp = '"+strPRDTP+"'";
				M_STLSQL += " and lk_lotno = '"+strLOTNO+"'";
				M_STLSQL += " and lk_rclno = '"+strRCLNO+"'";
				M_STLSQL += " and lk_pkgtp = '"+strPKGTP+"'";
				M_STLSQL += " and lk_mnlcd = '"+strMNLCD+"'";
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"updLCKTBL");
				setMSG("Record not found for Lot No "+strLOTNO+" of "+LP_TBLNM+" in fg_stmst",'E');
			}
		}
	}
	
	private void insLCKTBL(String LP_UPDQT,String LP_TBLNM)
	{
		try
		{
			M_STLSQL = "Insert into FG_LKTRN(LK_CMPCD,LK_WRHTP,LK_PRDTP,LK_LOTNO,LK_RCLNO,LK_PKGTP,LK_MNLCD,";
			M_STLSQL += "LK_STKQT,LK_UCLQT,LK_CPRCD,LK_STSFL,LK_ALOQT,LK_RCTQT,LK_ISSQT,LK_RCTDT,";
			M_STLSQL += "LK_PKGCT,LK_PKGWT,LK_TPRCD,LK_PRDCD,LK_LUSBY,LK_TRNFL,LK_LUPDT) values (";
			M_STLSQL += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_STLSQL += "'"+strWRHTP+"',";
			M_STLSQL += "'"+strPRDTP+"',";
			M_STLSQL += "'"+strLOTNO+"',";
			M_STLSQL += "'"+strRCLNO+"',";
			M_STLSQL += "'"+strPKGTP+"',";
			M_STLSQL += "'"+strMNLCD+"',";
			M_STLSQL += "0.000,";
			M_STLSQL += "0.000,";
			M_STLSQL += "' ',";
			M_STLSQL += "'0',";
			M_STLSQL += "0.000,";
			if(LP_TBLNM.equals("FG_RCTRN"))
			{
				M_STLSQL += LP_UPDQT+",";
				M_STLSQL += "0.000,";
			}
			else if(LP_TBLNM.equals("FG_ISTRN"))
			{
				M_STLSQL += "0.000,";
				M_STLSQL += LP_UPDQT+",";
			}
			M_STLSQL +=" '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"',";
			M_STLSQL += "'00',";
			M_STLSQL += "0.025,";
			M_STLSQL += "' ',";
			M_STLSQL += "' ',";
			M_STLSQL += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_STLSQL += "'0',";
			M_STLSQL += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"')";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insLCKTBL");
		}
		
	}
	
	private void dspERRTRN()
	{
		try
		{
			int L_CNT = 0;
			M_strSQLQRY = "Select count(*) from FG_LKTRN";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				L_CNT = M_rstRSSET.getInt(1);
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			if(L_CNT > 0)
			{
				//cl_dat.ocl_dat.M_CHKTBL = false;
				String[] L_CHKHD = {"S.NO","LOT/LA No.","RCL. No.","MAIN LOC.","PKG. TYPE","D.O.CLQTY","D.O.UCLQTY","RCPT. QTY.","ISS. QTY.","CLSF. QTY.","UNCLSF. QTY.","ALCTD. QTY."};
				int[] L_CHKCSZ = {40,70,40,70,70,70,70,70,70,70,70,70};
				//LM_CHKTBL = crtDFLTBL(L_CHKHD,L_CNT,0,0,750,145,10,375,750,147,L_CHKCSZ,0);
				LM_CHKTBL = crtTBLPNL1(this,L_CHKHD,100,14,1,5,8,L_CHKCSZ,new int[]{0});
				//TCL_LOTNO = LM_CHKTBL.getColumn(LM_CHKTBL.getColumnName(TB2_intLOTNO));
				TCL_STKQT = LM_CHKTBL.getColumn(LM_CHKTBL.getColumnName(TB2_intSTKQT));
				TCL_ALOQT = LM_CHKTBL.getColumn(LM_CHKTBL.getColumnName(TB2_intALOQT));
				LM_CHKTBL.addMouseListener(this);
				if(chkTRNREC(LM_CHKTBL))
				{
					//TCL_LOTNO.setCellRenderer(new RowRenderer());
					TCL_STKQT.setCellRenderer(new RowRenderer1());
					TCL_ALOQT.setCellRenderer(new RowRenderer2());
					setNUMREN(LM_CHKTBL,TB2_intDOSQT);
					setNUMREN(LM_CHKTBL,TB2_intDOUQT);
					setNUMREN(LM_CHKTBL,TB2_intRECPT);
					setNUMREN(LM_CHKTBL,TB2_intISSUE);
					setNUMREN(LM_CHKTBL,TB2_intUCLQT);
				}
			}
		}
		catch(Exception L_EX)
		{
			strCURTM = "";
			strUSRCD = "";
			//cl_cust.ocl_cust.updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
			updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
			setMSG(L_EX,"dspERRTRN");
		}
	}
	
	class RowRenderer1 extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col)
		{
			if(hstERSTQT.get(String.valueOf(row)) != null)
				setForeground(Color.red);
			else
				setForeground(Color.black);
			int cellValue = (value instanceof Number)? ((Number)value).intValue() : 0;
			setText((value == null)? "":value.toString());
			setHorizontalAlignment(JLabel.RIGHT);	
			return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
		}
	}
	
	class RowRenderer2 extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col)
		{
			if(hstERALQT.get(String.valueOf(row)) != null)
				setForeground(Color.red);
			else
				setForeground(Color.black);
			int cellValue = (value instanceof Number)? ((Number)value).intValue() : 0;
			setText((value == null)?"":value.toString());
			setHorizontalAlignment(JLabel.RIGHT);	
			return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
		}
	}
	
	private boolean chkTRNREC(JTable LP_CHKTBL)
	{
		boolean L_RETFL = false;
		try
		{
			setMSG("Checking for Invalid Transactions... ",'N');
			hstERSTQT.clear();
			hstERALQT.clear();
			M_strSQLQRY = "Select lk_lotno,lk_rclno,lk_mnlcd,lk_pkgtp,lk_dosqt,lk_douqt,lk_rctqt,";
			M_strSQLQRY += "lk_issqt,lk_stkqt,lk_uclqt,lk_aloqt from fg_lktrn";
			//System.out.println(" M_strSQLQRY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			i = 0;
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strLOTNO = nvlSTRVL(M_rstRSSET.getString("LK_LOTNO"),"");
					//System.out.println("strLOTNO = "+strLOTNO);
					strRCLNO = nvlSTRVL(M_rstRSSET.getString("LK_RCLNO"),"");
					//System.out.println("strRCLNO = "+strRCLNO);
					strPKGTP = nvlSTRVL(M_rstRSSET.getString("LK_PKGTP"),"");
					//System.out.println("strPKGTP = "+strPKGTP);
					strMNLCD = nvlSTRVL(M_rstRSSET.getString("LK_MNLCD"),"");
					//System.out.println("strMNLCD = "+strMNLCD);
					setMSG("Checking Invalid Transaction for "+strLOTNO+" at "+strMNLCD,'N');
					dblDOSQT = M_rstRSSET.getDouble("LK_DOSQT");
					dblDOUQT = M_rstRSSET.getDouble("LK_DOUQT");
					dblDRCQT = M_rstRSSET.getDouble("LK_RCTQT");
					dblDISQT = M_rstRSSET.getDouble("LK_ISSQT");
					dblDSTQT = M_rstRSSET.getDouble("LK_STKQT");
					dblDUCQT = M_rstRSSET.getDouble("LK_UCLQT");
					dblDALQT = M_rstRSSET.getDouble("LK_ALOQT");
					
					//if(!(setFMT("RND",String.valueOf(((dblDOSQT+dblDOUQT+dblDRCQT)-dblDISQT)),3).equals(setFMT("RND",String.valueOf(dblDSTQT+dblDUCQT),3)))){	
					if(!(setNumberFormat(((dblDOSQT+dblDOUQT+dblDRCQT)-dblDISQT),3).equals(setNumberFormat((dblDSTQT+dblDUCQT),3))))
					{
					
						hstERSTQT.put(String.valueOf(i),"x");
					}
					if(dblDALQT > 0)
						hstERALQT.put(String.valueOf(i),"x");
					LP_CHKTBL.setValueAt(strLOTNO,i,TB2_intLOTNO);
					LP_CHKTBL.setValueAt(strRCLNO,i,TB2_intRCLNO);
					LP_CHKTBL.setValueAt(strMNLCD,i,TB2_intMNLCD);
					//strIPKGDS = cl_dat.ocl_dat.getPRMCOD("CMT_SHRDS","SYS","FGXXPKG",strPKGTP);
					strIPKGDS = getPRMCOD("CMT_SHRDS","SYS","FGXXPKG",strPKGTP);
					//System.out.println("strIPKGDS = "+strIPKGDS);
					LP_CHKTBL.setValueAt(strIPKGDS,i,TB2_intPKGTP);
					
					//LP_CHKTBL.setValueAt(setFMT("RND",String.valueOf(dblDOSQT),3),i,TB2_intDOSQT);
					LP_CHKTBL.setValueAt(setNumberFormat(dblDOSQT,3),i,TB2_intDOSQT);
					//LP_CHKTBL.setValueAt(setFMT("RND",String.valueOf(dblDOUQT),3),i,TB2_intDOUQT);
					LP_CHKTBL.setValueAt(setNumberFormat(dblDOUQT,3),i,TB2_intDOUQT);
					//LP_CHKTBL.setValueAt(setFMT("RND",String.valueOf(dblDRCQT),3),i,TB2_intRECPT);
					LP_CHKTBL.setValueAt(setNumberFormat(dblDRCQT,3),i,TB2_intRECPT);
					//LP_CHKTBL.setValueAt(setFMT("RND",String.valueOf(dblDISQT),3),i,TB2_intISSUE);
					LP_CHKTBL.setValueAt(setNumberFormat(dblDISQT,3),i,TB2_intISSUE);
					//LP_CHKTBL.setValueAt(setFMT("RND",String.valueOf(dblDSTQT),3),i,TB2_intSTKQT);
					LP_CHKTBL.setValueAt(setNumberFormat(dblDSTQT,3),i,TB2_intSTKQT);
					//LP_CHKTBL.setValueAt(setFMT("RND",String.valueOf(dblDUCQT),3),i,TB2_intUCLQT);
					LP_CHKTBL.setValueAt(setNumberFormat(dblDUCQT,3),i,TB2_intUCLQT);
					//LP_CHKTBL.setValueAt(setFMT("RND",String.valueOf(dblDALQT),3),i,TB2_intALOQT);
					LP_CHKTBL.setValueAt(setNumberFormat(dblDALQT,3),i,TB2_intALOQT);
					i++;
					//System.out.println("I = "+i);
					flgLOCKFL = false;
					L_RETFL = true;
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			strCURTM = "";
			strUSRCD = "";
			//cl_cust.ocl_cust.updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
			updCDTRN("DTR","FGXXTOH","0002",strCURTM,strUSRCD);
			setMSG(L_EX,"chkTRNREC");
		}
		return L_RETFL;
	}
	
	private void chkINVTRN()
	{
		try
		{
			strSTLSQL = "Insert into fg_lktrn select '"+cl_dat.M_strCMPCD_pbst+"','','',ivt_ladno,'',date(ivt_loddt),ivt_invqt,";  
			strSTLSQL += "'','','','','',date(ivt_loddt),0,'','',0,0,'','',0,ivt_ladqt,'','',0,";
			strSTLSQL += "0.025,'','','' from mr_ivtrn where ivt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_mkttp='01'";
			strSTLSQL += " and ((ivt_invno = '' or ivt_invno is null)";
			strSTLSQL += " or (ivt_invqt <> ivt_ladqt)) and date(ivt_loddt) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"' ";
			strSTLSQL += " and ivt_stsfl='D'";
			cl_dat.exeSQLUPD(strSTLSQL,"");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("save"))
				{
					setMSG("SAVE chkINVTRN ",'N');
				}
			}
			else
			{
				setMSG("Error insert chkINVTRN ",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkINVTRN");
		}
	}
	
	class RowRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isselected,boolean hasFocus,int row,int col)
		{
			setForeground(Color.red);
			return super.getTableCellRendererComponent(table,value,isselected,hasFocus,row,col);
		}
	}
	
	private void lckREFDT()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				strCURTM = cl_dat.M_txtCLKTM_pbst.getText();
				M_STLSQL = "Update CO_CDTRN set ";
				M_STLSQL += "CMT_CCSVL = '"+strDATE+"',";
				M_STLSQL += "CMT_CHP01 = 'YES',";
				M_STLSQL += "CMT_CHP02 = '"+strCURTM+"'";
				M_STLSQL += " where CMT_CGMTP = 'S"+cl_dat.M_strCMPCD_pbst+"'";
				M_STLSQL += " and CMT_CGSTP = 'FGXXREF'";
				M_STLSQL += " and CMT_CODCD = 'DOCDT'";
				//cl_dat.ocl_dat.M_STRSQL = M_STLSQL;
				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
				//cl_dat.ocl_dat.exeSQLUPD(cl_dat.ocl_dat.M_STRSQL,"SP","REM","");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("save"))
					{
						setMSG("SAVE lckREFDT",'N');
					}
				}
				else
				{
					setMSG("Error insert lckREFDT ",'E');
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"lckREFDT");
			}
		}
	}
	
	private boolean chkREPDT(String LP_PRVDT)
	{
		try
		{
			String L_REPDT = getPRMCOD("CMT_CHP01","S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","MAXDT");
			String L_LCKDT = getPRMCOD("CMT_CCSVL","S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","DOCDT");
			if(L_REPDT.equals(LP_PRVDT) && L_LCKDT.equals(LP_PRVDT))
				return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkREPDT");
		}
		return false;
	}
	
	/**
	 * @return void
	 * This method updates monthly qty. to 0
	 */
	private void rstMTHSTK()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				setMSG("Working-out Carry Forward figures",'N');
				//cstCRFWD.setString(1,"01");
				//cstCRFWD.setString(2,cc_dattm.occ_dattm.setDBSDT(strDATE).substring(1,4)+"01/"+cc_dattm.occ_dattm.setDBSDT(strDATE).substring(7,11));
				//System.out.println("01/"+cc_dattm.occ_dattm.setDBSDT(strDATE).substring(1,4)+"01/"+cc_dattm.occ_dattm.setDBSDT(strDATE).substring(7,11));
				//cstCRFWD.executeUpdate();
			
			/*
				PART OF THE CODE FOR RESETTING CARRY FORWARD BOOKING during strting of the month
				The code is BLOCKED TEMPORARILY FOR OPTIMIZATION
				M_STLSQL = "update co_cdtrn set cmt_ccsvl = '"+cc_dattm.occ_dattm.setDBSDT(strDATE).substring(1,4)+"01/"+cc_dattm.occ_dattm.setDBSDT(strDATE).substring(7,11)+"' where cmt_cgmtp='SYS' and cmt_cgstp='MRXXCRF'";
				if(cl_dat.M_flgLCUPD_pbst){cl_dat.ocl_dat.exeDBCMT("SP","ACT","");}
				M_STLSQL = "update mr_intrn set int_cfwqt=0 where int_cfwqt<>0";
				if(cl_dat.M_flgLCUPD_pbst){cl_dat.ocl_dat.exeDBCMT("SP","ACT","");}
				M_STLSQL = "update mr_intrn set int_stsfl = '1' where int_stsfl='1' and int_indno in (select in_indno from spldata.mr_inmst where in_bkgdt between date('04/01/2005')  and  date('"+cc_dattm.occ_dattm.setDBSDT(strDATE).substring(1,4)+"01/"+cc_dattm.occ_dattm.setDBSDT(strDATE).substring(7,11)+"'))";
				if(cl_dat.M_flgLCUPD_pbst){cl_dat.ocl_dat.exeDBCMT("SP","ACT","");}
				if(!cl_dat.M_flgLCUPD_pbst){cl_dat.ocl_dat.exeDBRBK("SP","ACT");}
			*/
			
			//if(getRECCNT("SELECT COUNT(*) FROM FG_OPSTK") > 0)
			//{
				M_STLSQL = "Update FG_OPSTK set ";
				M_STLSQL += "OP_MXRQT = 0,";	
				M_STLSQL += "OP_MRCQT = 0,";
				M_STLSQL += "OP_MXDQT = 0,";
				M_STLSQL += "OP_M1DQT = 0,";
				M_STLSQL += "OP_M2DQT = 0,";
				M_STLSQL += "OP_M3DQT = 0,";
				M_STLSQL += "OP_M4DQT = 0,";
				M_STLSQL += "OP_M5DQT = 0,";
				M_STLSQL += "OP_M6DQT = 0";
				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
				if(cl_dat.exeDBCMT("save"))
					{
						setMSG("SAVE ",'N');
					}
				}
				else
				{
					setMSG("Error insert addLCKTBL ",'E');
				}
			//}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"rstMTHSTK");
			}
		}
	}
	
	/**
	 * @return void
	 * This method updates st_maxqt with max(st_stkqt+st_uclqt,st_dosqt+st_douqt).
	 */
	private void updMAXQT()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				double L_CURQT = 0;
				double L_MAXQT = 0;
				M_strSQLQRY = "Select sum(ST_STKQT+ST_UCLQT) L_CURQT,sum(ST_MAXQT) L_MAXQT";
				M_strSQLQRY += " from fg_stmst";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					L_CURQT = M_rstRSSET.getDouble("L_CURQT");
					L_MAXQT = M_rstRSSET.getDouble("L_MAXQT");
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				if(L_CURQT > L_MAXQT || flgMAXFL)
				{
					LM_OPTNPN.showMessageDialog(this,"Maximum Stock is getting updated: Previous Max. stock: "+L_MAXQT+" Todays stock: "+L_CURQT,"Max. Stock Updation Status",JOptionPane.INFORMATION_MESSAGE);
					M_STLSQL = "Update FG_STMST set ";
					M_STLSQL += "ST_MAXQT = (ST_STKQT+ST_UCLQT)";
					//cl_dat.ocl_dat.M_STRSQL = M_STLSQL;
					cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
					//cl_dat.ocl_dat.exeSQLUPD(cl_dat.ocl_dat.M_STRSQL,"SP","REM","");
					if(cl_dat.M_flgLCUPD_pbst)
					{
						if(cl_dat.exeDBCMT("save"))
						{
							setMSG("SAVE ",'N');
						}
					}
					else
					{
						setMSG("Error insert addLCKTBL ",'E');
					}
					if(cl_dat.M_flgLCUPD_pbst)
					{
						M_STLSQL = "Update CO_CDTRN set ";
						//M_STLSQL += "CMT_NCSVL = "+setFMT("RND",String.valueOf(L_CURQT),3)+",";
						M_STLSQL += "CMT_NCSVL = "+setNumberFormat((L_CURQT),3)+",";
						M_STLSQL += "CMT_CCSVL = '"+getABDATE(strDATE,1,'A')+"'";
						M_STLSQL += " where CMT_CGMTP = 'S"+cl_dat.M_strCMPCD_pbst+"'";
						M_STLSQL += " and CMT_CGSTP = 'FGXXREF'";
						M_STLSQL += " and CMT_CODCD = 'MAXDT'";
						//cl_dat.ocl_dat.M_STRSQL = M_STLSQL;
						cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
						//cl_dat.ocl_dat.exeSQLUPD(cl_dat.ocl_dat.M_STRSQL,"SP","REM","");
						if(cl_dat.M_flgLCUPD_pbst)
						{
							if(cl_dat.exeDBCMT("save"))
							{
								setMSG("SAVE ",'N');
							}
						}
						else
						{
							setMSG("Error insert addLCKTBL ",'E');
						}
					}
					else
						LM_OPTNPN.showMessageDialog(this,"Maximum Stock Date not saved. Contact Systems Department","Date Updation Status",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"updMAXQT");
			}
		}
	}
	
	/**
	 * @return void
	 * This method updates st_dosqt,st_douqt with st_stkqt,st_uclqt.
	 * A stored procedure setHKDCC is introduced which looks for authorised LA records
	 * for which Invoice is yet to be prepared.
	 * This procedure transfers the outstanding LA & Issue records to next date
	 * and it increases day opening stock for corresponding Lot & Locations
	 */
	private void updSTKQT()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				M_STLSQL = "Update FG_STMST set ";
				M_STLSQL += "ST_DOSQT = ST_STKQT,";
				M_STLSQL += "ST_DOUQT = ST_UCLQT";
				//cl_dat.ocl_dat.M_STRSQL = M_STLSQL;
				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("save"))
					{
						setMSG("SAVE ",'N');
					}
				}
				else
				{
					setMSG("Error insert addLCKTBL ",'E');
				}
				//cl_dat.ocl_dat.exeSQLUPD(cl_dat.ocl_dat.M_STRSQL,"SP","REM","");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					//System.out.println("call spltest.setHKDCC("+setDBSDT_DCC(strDATE)+","+setDBSTM_DCC(cc_dattm.occ_dattm.getABDATE(strDATE,1,'A')+" "+cc_dattm.occ_dattm.getCURTIM())+")");
					//System.out.println("call spltest.setHKDCC("+setDBSDT_DCC(cc_dattm.occ_dattm.getABDATE(strDATE,1,'B'))+","+setDBSTM_DCC(cc_dattm.occ_dattm.getABDATE(strDATE,1,'A')+" "+cc_dattm.occ_dattm.getCURTIM())+")");
								   
					//cl_dat.ocl_dat.exeDBCMT("SP","ACT","");
					//cstHKDCC = cl_dat.ocl_dat.conSPDBA.prepareCall("call setHKDCC(?,?)");
					cstHKDCC = cl_dat.M_conSPDBA_pbst.prepareCall("{ call setHKDCC(?,?,?)}");
					//cstHKDCC = cl_dat.M_conSPDBA_pbst.prepareStatement
					//M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDAT)
					//cstHKDCC.setString(1,setDBSDT_DCC(strDATE));
					cstHKDCC.setString(1,cl_dat.M_strCMPCD_pbst);
					cstHKDCC.setString(2,M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE)));
					cstHKDCC.setString(3,getABDATE(strDATE,1,'A')+" "+cl_dat.M_txtCLKTM_pbst.getText());
					cstHKDCC.executeUpdate();
					//cstHKDCC.setString(1,setDBSDT_DCC(cc_dattm.occ_dattm.getABDATE(strDATE,1,'B')));
					//cstHKDCC.setString(2,setDBSTM_DCC(cc_dattm.occ_dattm.getABDATE(strDATE,1,'A')+" "+cc_dattm.occ_dattm.getCURTIM()));
					//cstHKDCC.executeUpdate();
				}
				else
				{
					//cl_dat.ocl_dat.exeDBRBK("SP","ACT");
					//cl_dat.ocl_dat.exeDBRBK("SP","REM");
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"updSTKQT");
			}
		}
	}
	
	/**
	 * @return void
	 * This method updates Stock related qty. to 0
	 */
	private void rstOPSTK()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
			    //if(getRECCNT("SELECT COUNT(*) FROM FG_OPSTK") > 0)
			    //{
    				M_STLSQL = "Update FG_OPSTK set ";
    				M_STLSQL += "OP_STKQT = 0,";
    				M_STLSQL += "OP_UCLQT = 0,";
    				M_STLSQL += "OP_TDSQT = 0,";
    				M_STLSQL += "OP_SLRQT = 0,";
    				M_STLSQL += "OP_DOSQT = 0,";
    				M_STLSQL += "OP_DOUQT = 0,";
    				M_STLSQL += "OP_DRCQT = 0,";
    				M_STLSQL += "OP_DXRQT = 0,";
    				M_STLSQL += "OP_DXDQT = 0,";
    				M_STLSQL += "OP_D1DQT = 0,";
    				M_STLSQL += "OP_D2DQT = 0,";
    				M_STLSQL += "OP_D3DQT = 0,";
    				M_STLSQL += "OP_D4DQT = 0,";
    				M_STLSQL += "OP_D5DQT = 0,";
    				M_STLSQL += "OP_D6DQT = 0";
    				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
    				if(cl_dat.M_flgLCUPD_pbst)
    				{
    					if(cl_dat.exeDBCMT("save"))
    					{
    						setMSG("SAVE ",'N');
    					}
    				}
    				else
    				{
    					setMSG("Error insert addLCKTBL ",'E');
    				}
			    //}
			    
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"rstOPSTK");
			}
		}
	}
	
	private void modSTKQT()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				String L_STKQT = "";
				String L_UCLQT = "";
				String L_DOSQT = "";
				String L_DOUQT = "";
				boolean L_TDSFL = false;
				boolean L_SLRFL = false;
				strSTLSQL = "Select ST_WRHTP,ST_PRDTP,ST_PRDCD,ST_PKGCT,ST_PKGTP,ST_RESNO,ST_STSFL,";
				strSTLSQL += "sum(ST_DOSQT) L_DOSQT,sum(ST_DOUQT) L_DOUQT,sum(ST_STKQT) L_STKQT,";
				strSTLSQL += "sum(ST_UCLQT) L_UCLQT from FG_STMST where ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and (isnull(ST_STKQT,0)+isnull(ST_UCLQT,0)+isnull(ST_DOSQT,0)+isnull(ST_DOUQT,0)) > 0 ";
				strSTLSQL += " group by ST_WRHTP,ST_PRDTP,ST_PRDCD,ST_PKGCT,ST_PKGTP,ST_RESNO,ST_STSFL";
				strSTLSQL += " order by ST_WRHTP,ST_PRDTP,ST_PRDCD,ST_PKGCT,ST_PKGTP,ST_RESNO,ST_STSFL";
				//System.out.println(strSTLSQL);
				M_rstRSSET = cl_dat.exeSQLQRY1(strSTLSQL);
				while(M_rstRSSET.next())
				{
					strWRHTP = M_rstRSSET.getString("ST_WRHTP");
					strPRDTP = M_rstRSSET.getString("ST_PRDTP");
					strPRDCD = M_rstRSSET.getString("ST_PRDCD");
					strPKGTP = M_rstRSSET.getString("ST_PKGTP");
					if(strPKGTP.equals("01"))
						strPKGCT = "01";
					else
						strPKGCT = "02";
					//strPKGCT = M_rstRSSET.getString("ST_PKGCT");
					strRESNO = nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"").trim();
					strSTSFL = M_rstRSSET.getString("ST_STSFL");
					strSTKQT = M_rstRSSET.getString("L_STKQT");
					strUCLQT = M_rstRSSET.getString("L_UCLQT");
					strDOSTK = M_rstRSSET.getString("L_DOSQT");
					strDOUCL = M_rstRSSET.getString("L_DOUQT");
					L_TDSFL = false;
					L_SLRFL = false;
					if(strRESNO.length() > 0)
					{
						L_TDSFL = true;
					}
					if(strSTSFL.equals(strSLRVAR))
					{
						L_SLRFL = true;
					}
					addOPSTK(L_TDSFL,L_SLRFL);
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"modSTKQT");
			}
		}
	}
	
	/**
	 * 
	*/
	private void addOPSTK(boolean LP_TDSFL,boolean LP_SLRFL)
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				strSTLSQL = "Select count(*) from fg_opstk";
				strSTLSQL += " where op_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and op_wrhtp = '"+strWRHTP+"'";
				strSTLSQL += " and op_prdtp = '"+strPRDTP+"'";
				strSTLSQL += " and op_prdcd = '"+strPRDCD+"'";
				strSTLSQL += " and op_pkgct = '"+strPKGCT+"'";
				strSTLSQL += " and op_pkgtp = '"+strPKGTP+"'";
				//System.out.println(strSTLSQL);
				//if(cl_dat.ocl_dat.getRECCNT("SP","ACT",strSTLSQL) > 0)
				if(getRECCNT(strSTLSQL) > 0)
					updOPSTK(LP_TDSFL,LP_SLRFL);
				else
					insOPSTK(LP_TDSFL,LP_SLRFL);
				//System.out.println(M_STLSQL);
				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("save"))
					{
						setMSG("SAVE ",'N');
					}
				}
				else
				{
					setMSG("Error insert addLCKTBL ",'E');
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"addOPSTK");
			}
		}
	}
	
	/**
	 */
	private void updOPSTK(boolean LP_TDSFL,boolean LP_SLRFL)
	{
		try
		{
			M_STLSQL = "Update FG_OPSTK set ";
			M_STLSQL += "OP_UCLQT = OP_UCLQT + "+strUCLQT+",";
			M_STLSQL += "OP_STKQT = OP_STKQT + "+strSTKQT+",";
			M_STLSQL += "OP_DOSQT = OP_DOSQT + "+strDOSTK+",";
			M_STLSQL += "OP_DOUQT = OP_DOUQT + "+strDOUCL+",";
			if(LP_TDSFL)
				M_STLSQL += "OP_TDSQT = OP_TDSQT + "+strSTKQT+",";
			if(LP_SLRFL)
				M_STLSQL += "OP_SLRQT = OP_SLRQT + "+strSTKQT+",";
			M_STLSQL += "OP_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse( cl_dat.M_strLOGDT_pbst))+"'";
			M_STLSQL += " where op_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and op_wrhtp = '"+strWRHTP+"'";
			M_STLSQL += " and op_prdtp = '"+strPRDTP+"'";
			M_STLSQL += " and op_prdcd = '"+strPRDCD+"'";
			M_STLSQL += " and op_pkgct = '"+strPKGCT+"'";
			M_STLSQL += " and op_pkgtp = '"+strPKGTP+"'";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updOPSTK");
		}
	}
	
	/**
	 */
	private void insOPSTK(boolean LP_TDSFL,boolean LP_SLRFL)
	{
		try
		{
			M_STLSQL = "Insert into FG_OPSTK(OP_CMPCD,OP_WRHTP,OP_PRDTP,OP_PRDCD,OP_PKGCT,";
			M_STLSQL += "OP_PKGTP,OP_UCLQT,OP_STKQT,OP_DOSQT,OP_DOUQT,OP_TDSQT,";
			M_STLSQL += "OP_SLRQT,OP_LUPDT) values (";
			M_STLSQL += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_STLSQL += "'"+strWRHTP+"',";
			M_STLSQL += "'"+strPRDTP+"',";
			M_STLSQL += "'"+strPRDCD+"',";
			M_STLSQL += "'"+strPKGCT+"',";
			M_STLSQL += "'"+strPKGTP+"',";
			M_STLSQL += strUCLQT+",";
			M_STLSQL += strSTKQT+",";
			M_STLSQL += dblDOSQT+",";
			M_STLSQL += strDOUCL+",";
			if(LP_TDSFL)
				M_STLSQL += strSTKQT+",";
			else
				M_STLSQL += "0.000,";
			if(LP_SLRFL)
				M_STLSQL += strSTKQT+",";
			else
				M_STLSQL += "0.000,";
			M_STLSQL +="'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse( cl_dat.M_strLOGDT_pbst))+"')";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insOPSTK");
		}
	}
	
 
	
	
	/**************************CL_CUST.JAVA**********************/
	/**
	 *  Check whether Locking is going on 
	 */
	public boolean chkCDTRN()
	{
		try
		{
			String L_CHP01 = "";
			String L_CHP02 = "";
			strSTLSQL = "Select CMT_CHP01,CMT_CHP02 FROM CO_CDTRN WHERE CMT_CGMTP = 'DTR'";
			strSTLSQL += " AND CMT_CGSTP = 'FGXXTOH'";
			strSTLSQL += " AND CMT_CODCD = '0002'";
			//System.out.println("strSTLSQL"+strSTLSQL);
			M_rstRSSET = cl_dat.exeSQLQRY(strSTLSQL);
			if(M_rstRSSET.next())
			{
				L_CHP01 = M_rstRSSET.getString("CMT_CHP01").trim();
				L_CHP02 = M_rstRSSET.getString("CMT_CHP02").trim();
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			if(L_CHP01 == null || L_CHP01.equals(""))
				return true;
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
		return true;
	}
	/**
	 *  Update CMT_CHP01 with flag that's no one can update at same time.
	 */
	public  void updCDTRN(String LP_CGMTP,String LP_CGSTP,String LP_CODCD,String LP_CURTM,String LP_USRCD)
	{
		//cl_dat.ocl_dat.M_LCLUPD = true;
		try
		{
			M_STLSQL = "Update CO_CDTRN set ";
			M_STLSQL += "CMT_CHP01 = '"+LP_USRCD+"',";
			M_STLSQL += "CMT_CHP02 = '"+LP_CURTM+"'";
			M_STLSQL += " where CMT_CGMTP = '"+LP_CGMTP+"'";
			M_STLSQL += " and CMT_CGSTP = '"+LP_CGSTP+"'";
			M_STLSQL += " and CMT_CODCD = '"+LP_CODCD+"'";
			//cl_dat.ocl_dat.M_STRSQL = M_STLSQL;
			cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");                   
			//cl_dat.ocl_dat.exeSQLUPD(cl_dat.ocl_dat.M_STRSQL,"SP","REM","");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Update Successfully",'N');
				}
				
			}
			
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
	}


	
	
	public  void exeRCTQRY(String LP_FRMDT,String LP_TODAT,String LP_CONDN)   // get System Sub Type.
	{
		try
		{
			M_RCTQT_cldat = "";
			String M_RCTPK = "";
      		strQUERY = " Select sum(RCT_RCTQT),sum(RCT_RCTPK) from FG_RCTRN,PR_LTMST,CO_PRMST where";
			strQUERY += " RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_CMPCD = LT_CMPCD and CT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and RCT_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD and";
			strQUERY += LP_CONDN + " and RCT_RCTDT between '"+LP_FRMDT+"' and '"+LP_TODAT+"' and RCT_STSFL='2'";
			ResultSet LM_CTRLST = cl_dat.exeSQLQRY(strQUERY);
			if(LM_CTRLST.next())
			{
				M_RCTQT_cldat = LM_CTRLST.getString(1);
				M_RCTPK = LM_CTRLST.getString(2);
			}
			LM_CTRLST.close();
			if(M_RCTQT_cldat == null || M_RCTQT_cldat.length() == 0)
				M_RCTQT_cldat = "0.000";
			if(M_RCTPK == null || M_RCTPK.length() == 0)
				M_RCTPK = "0";
			//System.out.println(L_RCTQT);
		}
		catch(SQLException L_EX)
		{
			setMSG(L_EX,"exeRCTQRY");
		}
    }
	public  void delLCKTBL()
	{
		try
		{
			strSTLSQL = "Delete from FG_LKTRN";
			cl_dat.exeSQLUPD(strSTLSQL," ");
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"delLCKTBL");
		}
	}
	
	
	public  String getPRDDS(String L_strPRDCD) //To get Product Grade i.e SH3001
	{
		String L_PRDDS = "";
		try
		{
			strQUERY = "Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+L_strPRDCD+"'";
			ResultSet LM_CTRLST = cl_dat.exeSQLQRY(strQUERY);
			while(LM_CTRLST.next())
			{
				L_PRDDS = LM_CTRLST.getString("PR_PRDDS");
			}
			LM_CTRLST.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDDS");
		}
		if(L_PRDDS == null)
			L_PRDDS = "";
		return L_PRDDS;
	}
	
	public void modRCTQT(String LP_FRMDT,String LP_TODAT,boolean LP_YRFLG,boolean LP_MNFLG,boolean LP_DYFLG,boolean LP_HKDCF)
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				String L_XXRCT = "";
				String L_MNXXX = "";
				String L_YRXXX = "";
				String L_DYXXX = "";
					
				strSTLSQL = "Select RCT_RCTTP,RCT_WRHTP,RCT_PRDTP,LT_PRDCD,RCT_PKGCT,RCT_PKGTP,";
				strSTLSQL += "sum(RCT_RCTQT) L_RCTQT from FG_RCTRN,PR_LTMST where RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_CMPCD = LT_CMPCD and RCT_PRDTP = LT_PRDTP";
				strSTLSQL += " and RCT_LOTNO = LT_LOTNO and RCT_RCLNO = LT_RCLNO and ";
				strSTLSQL += " RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+"' " ;
				strSTLSQL += " and RCT_STSFL='2' and RCT_RCTTP in ('10','15','30')";
				strSTLSQL += " group by RCT_RCTTP,RCT_WRHTP,RCT_PRDTP,LT_PRDCD,RCT_PKGCT,RCT_PKGTP";
				strSTLSQL += " order by RCT_RCTTP,RCT_WRHTP,RCT_PRDTP,LT_PRDCD,RCT_PKGCT,RCT_PKGTP";
					
				M_rstRSSET = cl_dat.exeSQLQRY1(strSTLSQL);
					
				while(M_rstRSSET.next())
				{
					strTRNTP = M_rstRSSET.getString("RCT_RCTTP");
					strWRHTP = M_rstRSSET.getString("RCT_WRHTP");
					strPRDTP = M_rstRSSET.getString("RCT_PRDTP");
					L_strPRDCD = M_rstRSSET.getString("LT_PRDCD");
					L_strPKGTP = M_rstRSSET.getString("RCT_PKGTP");
					if(L_strPKGTP.equals("01"))
						strPKGCT = "01";
					else
						strPKGCT = "02";
					//strPKGCT = M_rstRSSET.getString("RCT_PKGCT");
					L_strPKGTP = M_rstRSSET.getString("RCT_PKGTP");
					strTRNQT = M_rstRSSET.getString("L_RCTQT");
					if(strTRNTP.equals(strFRSRCT) || strTRNTP.equals(strRPKRCT))
					{
						if(LP_HKDCF)
						{
							L_YRXXX = "op_yrcqt";
							L_MNXXX = "op_mrcqt";
							L_DYXXX = "op_drcqt";
						}
						else
						{
							if(LP_YRFLG)
								L_XXRCT = "op_yrcqt";
							else if(LP_MNFLG)
								L_XXRCT = "op_mrcqt";
							else if(LP_DYFLG)
								L_XXRCT = "op_drcqt";
						}
					}
					else if(strTRNTP.equals(strSLRRCT))
					{
						if(LP_HKDCF)
						{
							L_YRXXX = "op_yxrqt";
							L_MNXXX = "op_mxrqt";
							L_DYXXX = "op_dxrqt";
						}
						else
						{
							if(LP_YRFLG)
								L_XXRCT = "op_yxrqt";
							else if(LP_MNFLG)
								L_XXRCT = "op_mxrqt";
							else if(LP_DYFLG)
								L_XXRCT = "op_dxrqt";
						}
					}
					if(strTRNQT != null)
						addOPSTK(L_XXRCT,L_YRXXX,L_MNXXX,L_DYXXX,LP_HKDCF);							
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"modRCTQT");
			}
		}
	}
		
	public  void addOPSTK(String LP_XXYYY,String LP_YRXXX,String LP_MNXXX,String LP_DYXXX,boolean LP_HKDCF)
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				strSTLSQL = "Select count(*) from fg_opstk";
				strSTLSQL += " where op_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and op_wrhtp = '"+strWRHTP+"'";
				strSTLSQL += " and op_prdtp = '"+strPRDTP+"'";
				strSTLSQL += " and op_prdcd = '"+L_strPRDCD+"'";
				strSTLSQL += " and op_pkgct = '"+strPKGCT+"'";
				strSTLSQL += " and op_pkgtp = '"+L_strPKGTP+"'";
				if(getRECCNT(strSTLSQL) > 0)
					updOPSTK(LP_XXYYY,LP_YRXXX,LP_MNXXX,LP_DYXXX,LP_HKDCF);
				else
					insOPSTK(LP_XXYYY,LP_YRXXX,LP_MNXXX,LP_DYXXX,LP_HKDCF);
				//cl_dat.ocl_dat.M_STRSQL = M_STLSQL;
				cl_dat.exeSQLUPD(M_STLSQL,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("save"))
					{
						setMSG("SAVE ",'N');
					}
				}
				else
				{
					setMSG("Error insert addLCKTBL ",'E');
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"addOPSTK");
			}
		}
	}
	
	public  void updOPSTK(String LP_XXYYY,String LP_YRXXX,String LP_MNXXX,String LP_DYXXX,boolean LP_HKDCF)
	{
		try
		{
			M_STLSQL = "Update FG_OPSTK set ";
			if(LP_HKDCF)
			{
				M_STLSQL += LP_YRXXX +" = "+ LP_YRXXX + "+" + strTRNQT+",";
				M_STLSQL += LP_MNXXX +" = "+ LP_MNXXX + "+" + strTRNQT+",";
				M_STLSQL += LP_DYXXX +" = "+ LP_DYXXX + "+" + strTRNQT+",";
			}
			else
			{
				M_STLSQL += LP_XXYYY +" = "+ LP_XXYYY + "+" + strTRNQT+",";
			}
			M_STLSQL += "OP_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
			M_STLSQL += " where op_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and op_wrhtp = '"+strWRHTP+"'";
			M_STLSQL += " and op_prdtp = '"+strPRDTP+"'";
			M_STLSQL += " and op_prdcd = '"+L_strPRDCD+"'";
			M_STLSQL += " and op_pkgct = '"+strPKGCT+"'";
			M_STLSQL += " and op_pkgtp = '"+L_strPKGTP+"'";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updOPSTK");
		}
	}
	
	public void insOPSTK(String LP_XXYYY,String LP_YRXXX,String LP_MNXXX,String LP_DYXXX,boolean LP_HKDCF)
	{
		try
		{
			M_STLSQL = "Insert into FG_OPSTK(OP_CMPCD,OP_WRHTP,OP_PRDTP,OP_PRDCD,OP_PKGCT,";
			if(LP_HKDCF)
				M_STLSQL += "OP_PKGTP,"+LP_YRXXX+","+LP_MNXXX+","+LP_DYXXX+",OP_LUPDT) values (";	
			else
				M_STLSQL += "OP_PKGTP,"+LP_XXYYY+",OP_LUPDT) values (";
			M_STLSQL += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_STLSQL += "'"+strWRHTP+"',";
			M_STLSQL += "'"+strPRDTP+"',";
			M_STLSQL += "'"+L_strPRDCD+"',";
			M_STLSQL += "'"+strPKGCT+"',";
			M_STLSQL += "'"+L_strPKGTP+"',";
			M_STLSQL += strTRNQT+",";
			if(LP_HKDCF)
			{
				M_STLSQL += strTRNQT+",";	
				M_STLSQL += strTRNQT+",";	
			}
			M_STLSQL +="'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insOPSTK");
		}
	}
		
	public  void modISSQT(String LP_FRMDT,String LP_TODAT,boolean LP_YRFLG,boolean LP_MNFLG,boolean LP_DYFLG,boolean LP_HKDCF)
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				String L_XXISS = "";
				String L_MNXXX = "";
				String L_YRXXX = "";
				String L_DYXXX = "";
				
				strSTLSQL = "Select IST_ISSTP,IST_SALTP,IST_WRHTP,IST_PRDTP,IST_PRDCD,IST_PKGCT,";
				strSTLSQL += "IST_PKGTP,sum(IST_ISSQT) L_ISSQT from FG_ISTRN where";
				strSTLSQL += " IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+"' ";	
				strSTLSQL += " and IST_STSFL='2' and IST_ISSTP in ('10','30')";
				strSTLSQL += " group by IST_ISSTP,IST_SALTP,IST_WRHTP,IST_PRDTP,IST_PRDCD,IST_PKGCT,IST_PKGTP";
				strSTLSQL += " order by IST_ISSTP,IST_SALTP,IST_WRHTP,IST_PRDTP,IST_PRDCD,IST_PKGCT,IST_PKGTP";
				
				M_rstRSSET = cl_dat.exeSQLQRY1(strSTLSQL);
				
				while(M_rstRSSET.next())
				{
					strTRNTP = M_rstRSSET.getString("IST_ISSTP");
					strSALTP = M_rstRSSET.getString("IST_SALTP");
					strWRHTP = M_rstRSSET.getString("IST_WRHTP");
					strPRDTP = M_rstRSSET.getString("IST_PRDTP");
					L_strPRDCD = M_rstRSSET.getString("IST_PRDCD");
					strPKGCT = M_rstRSSET.getString("IST_PKGCT");
					L_strPKGTP = M_rstRSSET.getString("IST_PKGTP");
					strTRNQT = M_rstRSSET.getString("L_ISSQT");
					if(strTRNTP.equals(strDIRDSP))
					{
						if(strSALTP.equals(strDOMDSP))
						{
							if(LP_HKDCF)
							{
								L_YRXXX = "op_y1dqt";
								L_MNXXX = "op_m1dqt";
								L_DYXXX = "op_d1dqt";
							}
							else
							{
								if(LP_YRFLG)
									L_XXISS = "op_y1dqt";
								else if(LP_MNFLG)
									L_XXISS = "op_m1dqt";
								else if(LP_DYFLG)
									L_XXISS = "op_d1dqt";
							}
						}
						else if(strSALTP.equals(strEXPDSP))
						{
							if(LP_HKDCF)
							{
								L_YRXXX = "op_y2dqt";
								L_MNXXX = "op_m2dqt";
								L_DYXXX = "op_d2dqt";
							}
							else
							{
								if(LP_YRFLG)
									L_XXISS = "op_y2dqt";
								else if(LP_MNFLG)
									L_XXISS = "op_m2dqt";
								else if(LP_DYFLG)
									L_XXISS = "op_d2dqt";
							}
						}
						else if(strSALTP.equals(strDEXDSP))
						{
							if(LP_HKDCF)
							{
								L_YRXXX = "op_y3dqt";
								L_MNXXX = "op_m3dqt";
								L_DYXXX = "op_d3dqt";
							}
							else
							{
								if(LP_YRFLG)
									L_XXISS = "op_y3dqt";
								else if(LP_MNFLG)
									L_XXISS = "op_m3dqt";
								else if(LP_DYFLG)
									L_XXISS = "op_d3dqt";
							}
						}
						else if(strSALTP.equals(strSTFDSP))
						{
							if(LP_HKDCF)
							{
								L_YRXXX = "op_y5dqt";
								L_MNXXX = "op_m5dqt";
								L_DYXXX = "op_d5dqt";
							}
							else
							{
								if(LP_YRFLG)
									L_XXISS = "op_y5dqt";
								else if(LP_MNFLG)
									L_XXISS = "op_m5dqt";
								else if(LP_DYFLG)
									L_XXISS = "op_d5dqt";
							}
						}
						else if(strSALTP.equals(strFTSDSP))
						{
							if(LP_HKDCF)
							{
								L_YRXXX = "op_y4dqt";
								L_MNXXX = "op_m4dqt";
								L_DYXXX = "op_d4dqt";
							}
							else
							{
								if(LP_YRFLG)
									L_XXISS = "op_y4dqt";
								else if(LP_MNFLG)
									L_XXISS = "op_m4dqt";
								else if(LP_DYFLG)
									L_XXISS = "op_d4dqt";
							}
						}
						else if(strSALTP.equals(strCPCDSP))
						{
							if(LP_HKDCF)
							{
								L_YRXXX = "op_y6dqt";
								L_MNXXX = "op_m6dqt";
								L_DYXXX = "op_d6dqt";
							}
							else
							{
								if(LP_YRFLG)
									L_XXISS = "op_y6dqt";
								else if(LP_MNFLG)
									L_XXISS = "op_m6dqt";
								else if(LP_DYFLG)
									L_XXISS = "op_d6dqt";
							}
						}
					}
					else if(strTRNTP.equals(strSLRDSP))
					{
						if(LP_HKDCF)
						{
							L_YRXXX = "op_yxdqt";
							L_MNXXX = "op_mxdqt";
							L_DYXXX = "op_dxdqt";
						}
						else
						{
							if(LP_YRFLG)
								L_XXISS = "op_yxdqt";
							else if(LP_MNFLG)
								L_XXISS = "op_mxdqt";
							else if(LP_DYFLG)
								L_XXISS = "op_dxdqt";
						}
					}
					if(strTRNQT != null)
						addOPSTK(L_XXISS,L_YRXXX,L_MNXXX,L_DYXXX,LP_HKDCF);							
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"modISSQT");
			}
		}
	}

	public String getMNPRD(String LP_MNPRD)     //To get Main Product Code i.e Polystyrene
	{
		String L_MNPRD = "";
		try
		{
			strQUERY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and SUBSTRING(CMT_CODCD,1,2)='"+LP_MNPRD+"' and CMT_CCSVL='MG'";
			ResultSet LM_CTRLST = cl_dat.exeSQLQRY(strQUERY);
			while(LM_CTRLST.next())
			{
				L_MNPRD = LM_CTRLST.getString("CMT_CODDS");
			}
			LM_CTRLST.close();
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
		if(L_MNPRD == null)
			L_MNPRD = "";
		return L_MNPRD;
	}
	
	public String getSBPRD(String LP_SBPRD)    //To get Sub Product Code i.e HIPS,GPPS
	{
		String L_SBPRD = "";
		try
		{
			strQUERY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and SUBSTRING(CMT_CODCD,1,4)='"+LP_SBPRD+"' and CMT_CCSVL='SG'";
			ResultSet LM_CTRLST = cl_dat.exeSQLQRY(strQUERY);
			while(LM_CTRLST.next())
			{
				L_SBPRD = LM_CTRLST.getString("CMT_CODDS");
			}
			LM_CTRLST.close();
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
		if(L_SBPRD == null)
			L_SBPRD = "";
		return L_SBPRD;
	}


	
	
	
	///////////////////////////CL_DAT.JAVA/////////////
	
	
	
	
	public int getRECCNT(String LP_SQLSTR)
	{
		int L_RETVAL = -1;
		try
		{
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(LP_SQLSTR);
			if(L_rstRSSET.next())
			  L_RETVAL = L_rstRSSET.getInt(1);
			L_rstRSSET.close();
	    }
		catch (Exception L_EX)
		{
			System.out.println("getRECCNT: "+L_EX);				
	        L_RETVAL = -1;
	    }
		return L_RETVAL;
	}
	
	
	
	
	

	
	
	
	//////////////////////////////////
	// Method to insert the data into EX_DATFL (24.07.2002  by Santosh)
	
	public  void EX_DATFL(String LP_DOCTP,String LP_DOCDT)
	{
		try
		{
			//ResultSet M_rstRSSET;
			crtPRESTM();		// Method to create the prepared Statements
			
			String L_DOCNO,L_strPRDTP,L_strLOTNO,L_strPKGTP,L_FPKTP;
			String L_FPRCD,L_strPRDCD,L_DOCQT,L_DOCDT;
			//cl_dat.ocl_dat.M_LCLUPD = true;
			cl_dat.M_flgLCUPD_pbst = true;
			
			// Get the Lock Date	// Modified by Santosh on 04/02/2003
			getREFDT();		// cl_dat.ocl_dat.strREFDT
			String L_LOCDT =  strREFDT;
							
			if(LP_DOCTP.equals("RC"))   			// Receipt(bagging) data(FG_RCTRN)
			{
				strSTRSQL = "Select RCT_RCTNO LM_DOCNO,RCT_PRDTP strPRDTP,RCT_LOTNO strLOTNO,";
				strSTRSQL += "RCT_PKGTP strPKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD strPRDCD,";
				strSTRSQL += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT ";
				strSTRSQL += " from FG_RCTRN,PR_LTMST";
				strSTRSQL += " where RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_CMPCD = LT_CMPCD and RCT_RCTDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"'";
				strSTRSQL += " and RCT_RCTTP in ('10','15') and RCT_STSFL = '2'";
				strSTRSQL += " and RCT_PRDTP = LT_PRDTP ";
				strSTRSQL += " and RCT_LOTNO = LT_LOTNO ";
				strSTRSQL += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,LT_PRDCD,";
				strSTRSQL += " RCT_RCTDT";
				strSTRSQL += " order by LM_DOCNO,strPRDTP,strLOTNO,strPKGTP,strPRDCD,LM_DOCDT";
			}
			else if(LP_DOCTP.equals("JR"))		// Jobwork Receipt Data (FG_RCTRN)
			{
				strSTRSQL = "Select RCT_RCTNO LM_DOCNO,RCT_PRDTP strPRDTP,RCT_LOTNO strLOTNO,";
				strSTRSQL += "RCT_PKGTP strPKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD strPRDCD,";
				strSTRSQL += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT ";
				strSTRSQL += " from FG_RCTRN,PR_LTMST";
				strSTRSQL += " where RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_CMPCD = LT_CMPCD and RCT_RCTDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"'";
				strSTRSQL += " and RCT_LOTNO like '999%'";
				strSTRSQL += " and RCT_RCTTP in ('21','22','23') and RCT_STSFL = '2'";
				strSTRSQL += " and RCT_PRDTP = LT_PRDTP ";
				strSTRSQL += " and RCT_LOTNO = LT_LOTNO ";
				strSTRSQL += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,LT_PRDCD,";
				strSTRSQL += " RCT_RCTDT";
				strSTRSQL += " order by LM_DOCNO,strPRDTP,strLOTNO,strPKGTP,strPRDCD,LM_DOCDT";
			}
			else if(LP_DOCTP.equals("C1") || LP_DOCTP.equals("C2"))	// From FG_PTFRF
			{
				strSTRSQL = "Select PTF_PTFNO LM_DOCNO,PTF_PRDTP strPRDTP,PTF_LOTNO strLOTNO,";
				strSTRSQL += "PTF_PKGTP strPKGTP,'XX' LM_FPKTP,PTF_OPRCD LM_FPRCD,PTF_PRDCD strPRDCD,";
				strSTRSQL += "PTF_PTFDT LM_DOCDT,PTF_PTFCT,sum(PTF_PTFQT) LM_DOCQT";
				strSTRSQL += " from FG_PTFRF";
				strSTRSQL += " where PTF_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PTF_PTFDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"' " ;
				strSTRSQL += " and PTF_PTFCT in ('01','02')";
				strSTRSQL += " and SUBSTRING(PTF_LOTNO,1,3) <> '999'";
				strSTRSQL += " group by PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_PKGTP,PTF_OPRCD,";
				strSTRSQL += " PTF_PRDCD,PTF_PTFDT,PTF_PTFCT";
				strSTRSQL += " order by LM_DOCNO,strPRDTP,strLOTNO,strPKGTP,LM_FPRCD,";
				strSTRSQL += " strPRDCD,LM_DOCDT,PTF_PTFCT";
			}
			else if(LP_DOCTP.equals("C3"))					// From FG_PTFRF & FG_ISTRN
			{
				strSTRSQL = "select PTF_PTFNO LM_DOCNO,IST_PRDTP strPRDTP,IST_LOTNO strLOTNO,";          
				strSTRSQL += "IST_PKGTP strPKGTP,'XX' LM_FPKTP,IST_PRDCD LM_FPRCD,'' strPRDCD,";
				strSTRSQL += "PTF_PTFDT LM_DOCDT,sum(IST_ISSQT) LM_DOCQT";
				strSTRSQL += " from FG_PTFRF,FG_ISTRN";         
				strSTRSQL += " where PTF_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PTF_CMPCD = IST_CMPCD and PTF_PTFRF = IST_ISSNO";                
				strSTRSQL += " and PTF_PTFDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"' ";
				strSTRSQL += " and PTF_PTFCT = '03'";                               
				strSTRSQL += " group by PTF_PTFNO,IST_PRDTP,IST_LOTNO,IST_PKGTP,";
				strSTRSQL += " IST_PRDCD,PTF_PTFDT";
					
				strSTRSQL += " union select PTF_PTFNO LM_DOCNO,PTF_PRDTP strPRDTP,PTF_LOTNO strLOTNO,";          
				strSTRSQL += "PTF_PKGTP strPKGTP,'XX' LM_FPKTP,'' LM_FPRCD,PTF_PRDCD strPRDCD,";
				strSTRSQL += "PTF_PTFDT LM_DOCDT,PTF_PTFQT LM_DOCQT";
				strSTRSQL += " from FG_PTFRF ";
				strSTRSQL += " where PTF_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PTF_PTFDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"' ";
				strSTRSQL += " and PTF_PTFCT = '03'";                               
				strSTRSQL += " order by LM_DOCNO,strPRDTP,strLOTNO,strPKGTP,LM_FPRCD,strPRDCD,LM_DOCDT";
				//System.out.println(strSTRSQL);
			}
			else if(LP_DOCTP.equals("RB"))   			// Rebagging From FG_RCTRN & FG_ISTRN
			{
				strSTRSQL = "select IST_ISSNO LM_DOCNO,IST_PRDTP strPRDTP,IST_LOTNO strLOTNO,";          
				strSTRSQL += "'XX' strPKGTP,IST_PKGTP LM_FPKTP,'' LM_FPRCD,LT_PRDCD strPRDCD,";
				strSTRSQL += "IST_AUTDT LM_DOCDT,sum(IST_ISSQT) LM_DOCQT";
				strSTRSQL += " from FG_ISTRN,PR_LTMST";         
				strSTRSQL += " where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_CMPCD = LT_CMPCD and IST_PRDTP = LT_PRDTP ";       
				strSTRSQL += " and IST_LOTNO = LT_LOTNO ";
				strSTRSQL += " and IST_AUTDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"' ";
				strSTRSQL += " and IST_ISSTP = '16' ";
				strSTRSQL += " and IST_STSFL = '2' ";             
                strSTRSQL += " and (IST_MKTTP <> 'SR' or IST_MKTTP is null) ";             
				strSTRSQL += " group by IST_ISSNO,IST_PRDTP,IST_LOTNO,IST_PKGTP,";
				strSTRSQL += " LT_PRDCD,IST_AUTDT";
				
				strSTRSQL += " union select RCT_RCTNO LM_DOCNO,RCT_PRDTP strPRDTP,RCT_LOTNO strLOTNO,";          
				strSTRSQL += "RCT_PKGTP strPKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD strPRDCD,";
				strSTRSQL += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT";
				strSTRSQL += " from FG_RCTRN,PR_LTMST";         
				strSTRSQL += " where RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_CMPCD = LT_CMPCD and RCT_PRDTP = LT_PRDTP ";       
				strSTRSQL += " and RCT_LOTNO = LT_LOTNO ";
				strSTRSQL += " and RCT_RCTDT between '" +M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"'";
				strSTRSQL += " and RCT_RCTTP = '16' ";
				strSTRSQL += " and RCT_STSFL = '2' ";             
                strSTRSQL += " and (RCT_ISSRF <> 'SR' or RCT_ISSRF is null) ";             
				strSTRSQL += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,";
				strSTRSQL += " LT_PRDCD,RCT_RCTDT";
				
				strSTRSQL += " order by LM_DOCNO,strPRDTP,strLOTNO,strPKGTP,LM_FPKTP,";
				strSTRSQL += " LM_FPRCD,strPRDCD,LM_DOCDT,LM_DOCQT";
			}
			else if(LP_DOCTP.equals("CJ"))  		// Jobwork Classification
			{
				strSTRSQL = "Select PTF_PTFNO LM_DOCNO,PTF_PRDTP strPRDTP,PTF_LOTNO strLOTNO,";
				strSTRSQL += "PTF_PKGTP strPKGTP,'XX' LM_FPKTP,PTF_OPRCD LM_FPRCD,PTF_PRDCD strPRDCD,";
				strSTRSQL += "PTF_PTFDT LM_DOCDT,PTF_PTFCT,sum(PTF_PTFQT) LM_DOCQT";
				strSTRSQL += " from FG_PTFRF";
				strSTRSQL += " where PTF_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PTF_PTFDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"'";
				strSTRSQL += " and PTF_PTFCT in ('01')";
				strSTRSQL += " and SUBSTRING(PTF_LOTNO,1,3) = '999'";
				strSTRSQL += " group by PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_PKGTP,PTF_OPRCD,";
				strSTRSQL += " PTF_PRDCD,PTF_PTFDT,PTF_PTFCT";
				strSTRSQL += " order by LM_DOCNO,strPRDTP,strLOTNO,strPKGTP,LM_FPRCD,";
				strSTRSQL += " strPRDCD,LM_DOCDT,PTF_PTFCT";
			}
			else if(LP_DOCTP.equals("CS"))   		// Sales return (Classified from Receipts)
			{
				strSTRSQL = "Select RCT_RCTNO LM_DOCNO,RCT_PRDTP strPRDTP,RCT_LOTNO strLOTNO,";          
				strSTRSQL += "RCT_PKGTP strPKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD strPRDCD,";
				strSTRSQL += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT";
				strSTRSQL += " from FG_RCTRN,PR_LTMST";         
				strSTRSQL += " where RCT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RCT_CMPCD = LT_CMPCD and RCT_PRDTP = LT_PRDTP ";
				strSTRSQL += " and RCT_LOTNO = LT_LOTNO ";       
				strSTRSQL += " and RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_LOCDT))+"' ";
				strSTRSQL += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT ))+"'";
				strSTRSQL += " and RCT_RCTTP in ('30') ";
				strSTRSQL += " and RCT_STSFL = '2' ";             
				strSTRSQL += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,";
				strSTRSQL += " LT_PRDCD,RCT_RCTDT";
				strSTRSQL += " order by LM_DOCNO,strPRDTP,strLOTNO,strPKGTP,LM_FPKTP,";
				strSTRSQL += " LM_FPRCD,strPRDCD,LM_DOCDT,LM_DOCQT";
			}
//          System.out.println(strSTRSQL);                 
			M_rstRSSET = cl_dat.exeSQLQRY(strSTRSQL);			
			
			int L_ROW = 0;
			
			while(M_rstRSSET.next())
			{
				if(LP_DOCTP.equals("C1") || LP_DOCTP.equals("C2"))
				{
					if(M_rstRSSET.getString("PTF_PTFCT").equals("01"))	
						LP_DOCTP = "C1";
					else
						LP_DOCTP = "C2";
				}
					
				L_DOCNO = M_rstRSSET.getString("LM_DOCNO").trim();
				L_strPRDTP = M_rstRSSET.getString("strPRDTP").trim(); 
				L_strLOTNO = M_rstRSSET.getString("strLOTNO").trim();
				L_strPKGTP = M_rstRSSET.getString("strPKGTP").trim();
				L_FPKTP = M_rstRSSET.getString("LM_FPKTP").trim();
				
				if(isUNIQUE(LP_DOCTP,L_DOCNO,L_strPRDTP,L_strLOTNO,L_strPKGTP,L_FPKTP))
				{
					L_FPRCD = M_rstRSSET.getString("LM_FPRCD");
					L_strPRDCD = M_rstRSSET.getString("strPRDCD");
					L_DOCQT = M_rstRSSET.getString("LM_DOCQT");
					//L_DOCDT = cc_dattm.setDATE("DMY",M_rstRSSET.getDate("LM_DOCDT"));
					
					L_DOCDT = M_rstRSSET.getDate("LM_DOCDT") != null ?M_fmtLCDAT.format(M_rstRSSET.getDate("LM_DOCDT")):"";
					L_DOCDT = L_DOCDT.substring(6) + "-" + L_DOCDT.substring(3,5) + "-" + L_DOCDT.substring(0,2);
					
					pstmADDDATFL.setString(1,cl_dat.M_strCMPCD_pbst);
					pstmADDDATFL.setString(2,LP_DOCTP);
					pstmADDDATFL.setString(3,L_DOCNO);
					pstmADDDATFL.setString(4,L_strPRDTP);
					pstmADDDATFL.setString(5,L_strLOTNO);
					pstmADDDATFL.setString(6,L_strPKGTP);
					pstmADDDATFL.setString(7,L_FPKTP);
					pstmADDDATFL.setString(8,L_FPRCD);
					pstmADDDATFL.setString(9,L_strPRDCD);
					pstmADDDATFL.setString(10,L_DOCQT);
					pstmADDDATFL.setDate(11,java.sql.Date.valueOf(L_DOCDT));
					pstmADDDATFL.setString(12,"");
					pstmADDDATFL.addBatch();
					L_ROW++;
				}
			}
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			if(L_ROW > 0)
			{
				int rows[] = pstmADDDATFL.executeBatch();
				if(rows.length == L_ROW)
					if(cl_dat.exeDBCMT("EX_DATFL"))
					{
						setMSG("EX_DATFL Saved",'N');
					}
				
			}
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX + "EX_DATFL");
		}
	}
	
	public  boolean isUNIQUE(String LP_DOCTP,String LP_DOCNO,String LP_PRDTP,String LP_LOTNO,String LP_PKGTP,String LP_FPKTP)
	{	
		ResultSet M_rstRSSET1;
		String M_strSQLQRY;
		
		try
		{
			pstmCHKDATFL.setString(1,cl_dat.M_strCMPCD_pbst);
			pstmCHKDATFL.setString(2,LP_DOCTP);
			pstmCHKDATFL.setString(3,LP_DOCNO);
			pstmCHKDATFL.setString(4,LP_PRDTP);
			pstmCHKDATFL.setString(5,LP_LOTNO);
			pstmCHKDATFL.setString(6,LP_PKGTP);
			pstmCHKDATFL.setString(7,LP_FPKTP);
			
			M_rstRSSET1 = pstmCHKDATFL.executeQuery();
			if(M_rstRSSET1.next())
			{
				M_rstRSSET1.close();
				return false;
			}
		}
		catch(Exception L_EX)
		{
			System.out.println("Error in isUNIQUE : " + L_EX.toString());
		}
		return true;
	}
	
	
	// Method to create the prepared Statements
	private  void crtPRESTM()
	{
		try
		{
			// Prepared Statement to check whether record already exists in EX_DATFL or not 
			//pstmCHKDATFL = cl_dat.conSPDBA.prepareStatement(
			pstmCHKDATFL = 	cl_dat.M_conSPDBA_pbst.prepareStatement(															
							"Select * from EX_DATFL where DT_CMPCD = ? DT_DOCTP = ? " +
							" and DT_DOCNO = ? and DT_PRDTP = ? and DT_LOTNO = ?" +
							" and DT_PKGTP = ? and DT_FPKTP = ?"
							);
			
			// Prepared statement to insert record into EX_DATFL
			//pstmADDDATFL = cl_dat.conSPDBA.prepareStatement(
			pstmADDDATFL =	cl_dat.M_conSPDBA_pbst.prepareStatement(
							"Insert into EX_DATFL (DT_CMPCD,DT_DOCTP,DT_DOCNO,DT_PRDTP,DT_LOTNO," +
							"DT_PKGTP,DT_FPKTP,DT_FPRCD,DT_PRDCD,DT_DOCQT,DT_DOCDT," +					
							"DT_UPDFL) values (?,?,?,?,?,?,?,?,?,?,?,?)"		 
							);
		}
		catch(Exception e)
		{
			System.out.println("Error in crtPRESTM : " + e.toString());
		}
	}
	public  String getPRMCOD(String LP_FLDRTN, String LP_STRMGP, String LP_STRSGP, String LP_STRCOD)
	{
		strSTRTMP = "";
		
		strSTRSQL ="select " + LP_FLDRTN ;
		strSTRSQL = strSTRSQL + " from CO_CDTRN where CMT_CGMTP = "+ "'" + LP_STRMGP.trim() +"'" ;
		strSTRSQL = strSTRSQL + " AND CMT_CGSTP = " + "'" + LP_STRSGP.trim() + "'";
		strSTRSQL = strSTRSQL + " AND CMT_CODCD = " + "'" + LP_STRCOD.trim() + "'";
		try
		{
			ResultSet M_rstRSSET1 = cl_dat.exeSQLQRY(strSTRSQL);
			if(M_rstRSSET1.next())
		    strSTRTMP = M_rstRSSET1.getString(1);
		M_rstRSSET1.close();
		}
		catch(SQLException L_SE)
		{
			System.out.println("Error in getPRMCOD : "+L_SE.toString()); 
		}
		return strSTRTMP;
	}

	
	public void setNUMREN(JTable LP_TBLNM,int LP_COLIDX)
	{
		LM_TBLCOL = LP_TBLNM.getColumn(LP_TBLNM.getColumnName(LP_COLIDX)); 
		LM_TBLCOL.setCellRenderer(numREN);
	}
	////////////////////////New Function///////////////////
	public void getREFDT()//get reference date
	{
		try
		{
			Date L_strTEMP=null;
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				strREFTM  = L_rstRSSET.getString("CMT_CHP02").trim();
				//M_REFFL  = L_rstRSSET.getString("CMT_CHP01").trim();
				L_rstRSSET.close();
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+0);                     // Increase Date from +1 with Locked Date
				strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				//System.out.println("REFDT = "+strREFDT);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
		}
	}
	
	public  String getABDATE(String strREFDT,int LP_DDCNT,char LP_ABFLG)
	{
		String L_strRETRN="";
		try
		{
		
			if(LP_ABFLG == 'A')
			{
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
				L_strRETRN = M_fmtLCDAT.format(M_calLOCAL.getTime());   
			}
			else if(LP_ABFLG == 'B')
			{
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,-1);                     // Increase Date from +1 with Locked Date
				L_strRETRN = M_fmtLCDAT.format(M_calLOCAL.getTime()); 
			}
		}
		catch(Exception E_LX)
		{
			setMSG(E_LX,"getABDATE");
			
		}
		return L_strRETRN;
		
	}
	/**
	 * 
	*/
	private void exeDSRITM()
	{
		try
		{
		    String L_strSQLQRY = "SELECT DISTINCT IVT_DSRCD,PT_PRTNM,isnull(PT_EMLRF,'systems@spl.co.in') PT_EMLRF from MR_IVTRN,CO_PTMST where PT_PRTTP='D' and PT_PRTCD = IVT_DSRCD and IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP in ('01','04','05') and IVT_SALTP not in ('04','05','16','21') and date(ivt_invdt) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"' order by ivt_dsrcd";
			//System.out.println(L_strSQLQRY);
		    ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			//System.out.println(L_strSQLQRY);
			if(L_rstRSSET ==null || !L_rstRSSET.next())
				return;
			while (true)
			{
				String L_strEMLID = L_rstRSSET.getString("PT_EMLRF").trim();
				String L_strMSGDS = "Despatch Intimation for "+strDATE;
				if (L_strEMLID.length()<2)
				{
					L_strEMLID = "cms@spl.co.in";
					L_strMSGDS = "E-mail address not specified for "+L_rstRSSET.getString("IVT_DSRCD").trim()+"/"+L_rstRSSET.getString("PT_PRTNM").trim();
				}
					
				exeDSRITM1(L_rstRSSET.getString("IVT_DSRCD").trim(),L_rstRSSET.getString("PT_PRTNM").trim(),L_strEMLID,L_strMSGDS);
				System.out.println(L_rstRSSET.getString("IVT_DSRCD").trim()+" / "+L_rstRSSET.getString("PT_PRTNM").trim()+" / "+L_rstRSSET.getString("PT_EMLRF").trim());
				if(!L_rstRSSET.next())
					break;
		    }
			L_rstRSSET.close();
		}catch(Exception e){
			setMSG("Error in exeDSRITM"+e,'E');
			return;
		}		
	}
	/**
	 * 
	*/
	private void exeDSRITM1(String LP_DSRCD, String LP_DSRNM, String LP_EMLRF, String LP_MSGDS)
	{
		try
		{
			FileOutputStream O_FOUT1;
			DataOutputStream O_DOUT1;

			String L_strFILNM = cl_dat.M_strREPSTR_pbst.concat(""+LP_DSRCD+".html");
			String L_strPRTNM = "";
			O_FOUT1 = crtFILE(L_strFILNM);
			O_DOUT1 = crtDTOUTSTR(O_FOUT1);	
			// e-mail functionality start
		    String L_strSQLQRY = "SELECT SUBSTRING(IVT_INVNO,2,7) IVT_INVNO, IVT_PRDDS, IVT_INVQT, IVT_PKGTP, IVT_LRYNO, a.PT_PRTNM IVT_BYRNM,b.PT_SHRNM IVT_TRPNM from MR_IVTRN, CO_PTMST a, CO_PTMST b  where a.PT_PRTTP = 'C' and a.PT_PRTCD = IVT_BYRCD and b.PT_PRTTP = 'T' and b.PT_PRTCD = IVT_TRPCD and IVT_DSRCD = '"+LP_DSRCD+"' and IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP in ('01','04','05') and IVT_SALTP not in ('04','05','16','21') and date(ivt_invdt) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDATE))+"'  order by ivt_byrcd,ivt_invno";
			//System.out.println(L_strSQLQRY);
		    ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			//System.out.println(L_strSQLQRY);
			if(L_rstRSSET ==null || !L_rstRSSET.next())
				return;
			O_DOUT1.writeBytes("<HTML><HEAD><Title> Despatch Intimation </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");
			O_DOUT1.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");
			O_DOUT1.writeBytes("<H1><CENTRE>");
			O_DOUT1.writeBytes("Despatch Intimation \n\n");
			O_DOUT1.writeBytes("</H1></CENTRE>");
			O_DOUT1.writeBytes("Distributor : "+LP_DSRNM+"\n\n");
			O_DOUT1.writeBytes("Materials with following detail have been despatched from SPL premises on "+strDATE+"\n\n");
			O_DOUT1.writeBytes(padSTRING('R', "Buyer",35) 
							 + padSTRING('R', "Inv.No.",10) 
							 + padSTRING('R', "Grade",15) 
							 + padSTRING('L', "Qty.",10) + "  "
							 + padSTRING('R', "Lorry No.",12)
							 + padSTRING('R', "Transpr.",10)+"\n");
			O_DOUT1.writeBytes("<hr>");
			while (true)
			{
			O_DOUT1.writeBytes(padSTRING('R', L_rstRSSET.getString("IVT_BYRNM").trim(),35) 
							 + padSTRING('R', L_rstRSSET.getString("IVT_INVNO").trim(),10)
							 + padSTRING('R', L_rstRSSET.getString("IVT_PRDDS").trim(),15)
							 + padSTRING('L', L_rstRSSET.getString("IVT_INVQT").trim(),10)+"  "
							 + padSTRING('R', L_rstRSSET.getString("IVT_LRYNO").trim(),12)
							 + padSTRING('R', L_rstRSSET.getString("IVT_TRPNM").trim(),10)+"\n");
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
				
			O_DOUT1.writeBytes("<hr>");
			O_DOUT1.writeBytes("\n\n\nFor Supreme Petrochem Ltd.\n\n");
			O_DOUT1.writeBytes("Central Marketing Services\n");
			O_DOUT1.writeBytes("cms@spl.co.in \n");
			O_DOUT1.close();
			O_FOUT1.close();
			O_DOUT1 = null;
			O_FOUT1 = null;

			
			
			//int L_SELOPT = LM_OPTNPN.showConfirmDialog(this,"Do you want to mail the report?","E-Mail Status",JOptionPane.YES_NO_OPTION);
			//if(L_SELOPT == 0){
			/*	cl_eml ocl_eml = new cl_eml();
                ocl_eml.setFRADR("cms@spl.co.in");
				System.out.println("Sendin mail to : "+LP_EMLRF+"...");
				//ocl_eml.sendfile("EXT",L_strEMLID,L_strFILNM,"Delivery Initimation"," ");
				ocl_eml.sendfile("EXT",LP_EMLRF.trim(),L_strFILNM,LP_MSGDS," ");
				ocl_eml.sendfile("cms@spl.co.in",L_strFILNM,LP_MSGDS," ");
				//ocl_eml.sendfile("sr_deshpande@spl.co.in",L_strFILNM,LP_MSGDS," ");
				setMSG("Report has been mailed succesfully",'N');*/
			//	}
			
		}catch(Exception e){
			setMSG("Error in exeDSRITM1"+e,'E');
			return;
		}		
	}
	
	private class INPVF extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtDATE)
				{
					btnLOCK.setEnabled(false);
					if(txtDATE.getText().trim().length()!=10)
					{
						txtDATE.setText("");
						return true;
					}
					else
					{
						strDATE=txtDATE.getText().trim();
						//System.out.println("KKKKKKK"+strDATE);
						if(M_fmtLCDAT.parse(strDATE).compareTo(M_fmtLCDAT.parse(getABDATE(strREFDT,1,'A')))==0)
						{
							setMSG("Valid Date Format",'N');
							return true;
						}	
						else
						{
							
							if((M_fmtLCDAT.parse(strDATE).compareTo(M_fmtLCDAT.parse(getABDATE(strREFDT,1,'A')))<0))
							{
									setMSG("Invalid Date ...",'E');
									setMSG("Locking of data till "+strREFDT+" has been performed.",'E');
									//JOptionPane.showMessageDialog(this,"Locking of data till "+strREFDT+" has been performed.","Date Valid Status",JOptionPane.INFORMATION_MESSAGE);				
							}
							else
							{
								setMSG("Entered  date is greater then  Locking Date",'E');				
							}
							
							//btnRUN.setEnabled(false);
							//btnLOCK.setEnabled(false);
							//txtDATE.requestFocus();
							return false;
						}
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"INPVF");
			}
			return true;
		}
	}
}
