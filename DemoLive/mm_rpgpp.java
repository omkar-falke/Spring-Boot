/*
System Name		: Materials Management System
Program Name	: Gate Pass Printing
Author			: ATC

Modified Date	: 04/06/2004
Documented Date	: 04/06/2004
Version			: v2.0.0
*/
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.sql.ResultSet;

/**<pre>
System Name : Material Management System.
 
Program Name : Gate Pass Printing

Purpose : Program for Gate Pass Printing for given Range of Gate Pass Number.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
MM_GPTRN       GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD                     #
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
cmbMGPTP    GP_MGPTP                MM_GPTRN           VARCHAR(2)    Gate Pass Type
txtFMGPS    GP_MGPNO                MM_GPTRN           VARCHAR(8)    Gate Pass Number
txtTOGPS    GP_MGPNO                MM_GPTRN           VARCHAR(8)    Gate Pass Number
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Gate pass details are taken from MM_GPTRN,CO_CTMST and CO_PTMST for condiations :- 
    1) GP_MATCD = CT_MATCD 
    2) AND PT_PRTCD = GP_VENCD 
    3) AND PT_PRTTP = 'S' 
    4) AND GP_STRTP = Specified Store Type.
    5) AND GP_MGPTP = Specified Gate Pass Type.
    6) AND GP_MGPNO in the given range of gate pass number.
    7) AND isnull(GP_STSFL,' ') <> 'X'
GRIN Date is taken from MM_GRMST for
    1) GR_STRTP = Specified Store Type 
    2) AND GR_GRNNO = Given GRIN number.
      
<B>Validations & Other Information:</B>    
    - Gate Pass Number entered must be valid.
    - If Gate Pass status is 1,2 or 3 then Gate Pass request is printed, else Gate pass is printed.
</I> */

public class mm_rpgpp extends cl_rbase
{									/**Combo box for gate pass type	 */				
	private JComboBox cmbMGPTP;		/**Text Field for from gate pass number	 */
	private JTextField txtFMGPS;	/**Text Field for to gate pass number	 */
	private JTextField txtTOGPS;	/**Hashtable for gate pass description	and code */
	private Hashtable<String,String> hstMGPTP;		/**Hashtabel for department code and department namd */
	private Hashtable<String,String> hstDPTCD;		/**String for gate pass type */
	private String strMGPTP;		/**String for gate pass number */
	private String strMGPNO;		/**String for gate pass date */
	private String strMGPDT;		/**String for vendor code */
	private String strVENCD;		/**String for vendor name */
	private String strVENNM;		/**String for due date */
	private String strDUEDT;		/**String for received quantity */
	private String strRECQT;		/**String for department code */
	private String strDPTCD;		/**String for vehicle number */
	private String strVEHNO;		/**String for vehicle description */
	private String strVEHDS;		/**String for address 1 */
	private String strADR01;		/**String for address 2 */
	private String strADR02;		/**String for city name */
	private String strCTYNM;		/**String for pin code */
	private String strPINCD;		/**String for phone number */
	private String strTEL01;		/**String for remark */
	private String strREMDS;		/**String for status flag */
	private String strSTSFL;		/**String for prepared by */
	private String strPREBY;		/**String for approved by */
	private String strAPRBY;		/**String for authorised by */	
	private String strAUTBY;		/**String for GRIN number */
	private String strGRNNO;		/**String for GRIN Date */
	private String strGRNDT;		/**String for Temp Gate Pass number */
	private String strAPRVL;		/**String for Approx value given by user */
	private String strTGPNO = "";	/**Integer for Serial number */
	private String strCOMNT = "";	/**Comments for Gate pass request */
	private int intSRLNO;			/**Final String for report file name */
	private String strMGPDS = "";	/**Material Gate Pass description */
	private String strSTRDS = "";	/**Stores type description */
	private String strMATCD = "";
	private String strMDVFL = "";
	//private final String strFILNM = "C:\\Reports\\mm_rpgpp.doc";
	private String strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpgpp.doc";
	private final String strREJTP_fn ="53";
	private final String strRTNGP_fn ="51";
	private FileOutputStream fosREPORT ;   
    private DataOutputStream dosREPORT ;
	private String strPRVVL;		
	private String strPRSTS;		
	mm_rpgpp()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			hstMGPTP = new Hashtable<String,String>(4,2.0f);
			hstDPTCD = new Hashtable<String,String>(8,0.5f);
			cmbMGPTP = new JComboBox();
		
			M_intLINNO = 0;
			M_intPAGNO = 1;
			intSRLNO = 1;
			//Get list of gate pass and add to combo box and description as key and code as value to hash table hstMGPTP			
			//M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' "
			//	+"AND CMT_CGSTP = 'MMXXMGP' AND isnull(CMT_STSFL,'') <>'X' ORDER BY CMT_CODCD";
			
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS,CMT_CGSTP FROM CO_CDTRN WHERE CMT_CGMTP + CMT_CGSTP IN('D"+cl_dat.M_strCMPCD_pbst+"MMXXMGP','SYSCOXXDPT') "
				+" AND isnull(CMT_STSFL,'') <>'X' ORDER BY CMT_CGSTP,CMT_CODCD";
			
			M_rstRSSET =cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXMGP"))
				{
					cmbMGPTP.addItem(M_rstRSSET.getString("CMT_CODDS"));
					hstMGPTP.put(M_rstRSSET.getString("CMT_CODDS"),M_rstRSSET.getString("CMT_CODCD").substring(1,3));
				}
				else if(M_rstRSSET.getString("CMT_CGSTP").equals("COXXDPT"))
				{
					hstDPTCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
			}
			M_rstRSSET.close();
			setMatrix(20,6);
			add(new JLabel("Gate Pass Type"),3,3,1,1,this,'L');
			add(cmbMGPTP,3,4,1,1,this,'L');
			add(new JLabel("From Gate Pass"),4,3,1,1,this,'L');
			add(txtFMGPS = new TxtNumLimit(8.0),4,4,1,1,this,'L');
			add(new JLabel("To Gate Pass"),5,3,1,1,this,'L');
			add(txtTOGPS = new TxtNumLimit(8.0),5,4,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);	
			txtFMGPS.setInputVerifier(new INPVF());
			txtTOGPS.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	mm_rpgpp(String P_strSBSCD) throws Exception
	{
		M_strSBSCD = P_strSBSCD;
		if(hstDPTCD ==null)
		{
			hstDPTCD = new Hashtable<String,String>(8,0.5f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' "
				+"AND CMT_CGSTP = 'COXXDPT' AND isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					hstDPTCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				setENBL(true);
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);
				cmbMGPTP.requestFocus();
			}
			
		}
		if(M_objSOURC == cmbMGPTP)
		{
			txtFMGPS.setText("");
			txtTOGPS.setText("");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbMGPTP)
				txtFMGPS.requestFocus();
			if(M_objSOURC == txtFMGPS)
			{
				if(txtFMGPS.getText().trim().toString().length() != 0)
				{
					txtTOGPS.setText(txtFMGPS.getText());
					txtTOGPS.requestFocus();
				}
			}
			if(M_objSOURC == txtTOGPS)
			{
				if(txtTOGPS.getText().trim().toString().length() != 0)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			strMGPTP = hstMGPTP.get(cmbMGPTP.getSelectedItem()).toString();
			if(M_objSOURC == txtFMGPS)
			{
				M_strHLPFLD = "txtFMGPS";
				M_strSQLQRY = "SELECT DISTINCT GP_MGPNO,GP_MGPDT,GP_DPTCD,CONVERT(varchar,GP_GOTDT,101) FROM MM_GPTRN "
					+"WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"+strMGPTP+"' AND ";
				if(txtFMGPS.getText().trim().length() > 0)
					M_strSQLQRY += "GP_MGPNO LIKE '"+txtFMGPS.getText().trim()+"%' AND ";
				M_strSQLQRY +="isnull(GP_STSFL,' ') <> 'X' ORDER BY GP_MGPNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Gate Pass No.","Gate Pass Date","Dept. Code","Gate Out"},4,"CT");
			}
			if(M_objSOURC == txtTOGPS)
			{
				M_strHLPFLD = "txtTOGPS";
				M_strSQLQRY = "SELECT DISTINCT GP_MGPNO,GP_MGPDT,GP_DPTCD FROM MM_GPTRN "
					+"WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GP_MGPTP = '"+strMGPTP+"' AND ";
				if(txtTOGPS.getText().trim().length() > 0)
					M_strSQLQRY += "GP_MGPNO LIKE '"+txtTOGPS.getText().trim()+"%' AND ";
				M_strSQLQRY += "isnull(GP_STSFL,' ') <> 'X' ORDER BY GP_MGPNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Gate Pass No.","Gate Pass Date","Dept. Code"},3,"CT");
			}
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMGPS")
		{
			txtFMGPS.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMGPS.requestFocus();
		}
		if(M_strHLPFLD == "txtTOGPS")
		{
			txtTOGPS.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOGPS.requestFocus();
		}
	}
	/** Method to generate Report File
	 */
	public void genRPFIL(String P_strFMGPS,String P_strTOGPS,String P_strMGPTP,String P_strMGPDS,String P_strSTRDS)throws Exception
	{
		try
		{
			strMGPTP = P_strMGPTP;
			strMGPDS = P_strMGPDS;
			strSTRDS = P_strSTRDS;
			setCursor(cl_dat.M_curWTSTS_pbst);
			ResultSet L_rstRSSET;
				fosREPORT = new FileOutputStream(strFILNM);
				dosREPORT = new DataOutputStream(fosREPORT);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Gate Pass </title> ");
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE></HEAD>"); 
				dosREPORT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			M_strSQLQRY = "SELECT GP_MGPNO,GP_MATCD,CT_MATDS,CT_UOMCD,CT_PGRNO,CT_PGRDT,GP_MGPDT,GP_VENCD,"
				+"GP_VENNM,GP_DUEDT,GP_ISSQT,GP_RECQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,GP_GRNNO,"
				+"GP_INSFL,GP_PREBY,GP_APRBY,GP_AUTBY,GP_STSFL,GP_APRVL,PT_ADR01,PT_ADR02,PT_CTYNM,PT_PINCD,"
				+"PT_TEL01 FROM MM_GPTRN,CO_CTMST,CO_PTMST WHERE  GP_MATCD = CT_MATCD AND "
				+"PT_PRTCD = GP_VENCD AND PT_PRTTP = 'S' AND GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
				+"AND GP_MGPTP = '"+strMGPTP+"' AND GP_MGPNO "
				+"BETWEEN '"+P_strFMGPS+"' AND '"+P_strTOGPS+"' "
				+"AND isnull(GP_STSFL,' ') <> 'X' order by GP_MGPNO";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println("M_strSQLQRY"+M_strSQLQRY);
			// strPRVVL maintained on 22/11/2005 as value of one GP was coming in other (mixup)
			strPRVVL ="0.00";strAPRVL ="0.00";
			boolean L_FIRST = true;
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strGRNNO = "";
					strGRNDT = "";
					strMGPNO = nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"");
					strVENCD = nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),"");
					strVENNM = nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),"");
					strRECQT = nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"");
					strDPTCD = nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"");
					strVEHNO = nvlSTRVL(M_rstRSSET.getString("GP_VEHNO"),"");
					strVEHDS = nvlSTRVL(M_rstRSSET.getString("GP_VEHDS"),"");
					strADR01 = nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),"");
					strADR02 = nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),"");
					strCTYNM = nvlSTRVL(M_rstRSSET.getString("PT_CTYNM"),"");
					strPINCD = nvlSTRVL(M_rstRSSET.getString("PT_PINCD"),"");
					strTEL01 = nvlSTRVL(M_rstRSSET.getString("PT_TEL01"),"");
					if(!L_FIRST)
    				{
    				    strPRVVL = strAPRVL;
    				    strPRSTS = strSTSFL;	    
    				}
    				strAPRVL = nvlSTRVL(M_rstRSSET.getString("GP_APRVL"),"0.00");
					strSTSFL = nvlSTRVL(M_rstRSSET.getString("GP_STSFL"),"");
					if(L_FIRST)
					{
					    strPRVVL = strAPRVL;	    
					    strPRSTS = strSTSFL;	
					    L_FIRST = false;    
					}
					if(M_rstRSSET.getDate("GP_MGPDT") != null)
						strMGPDT = M_fmtLCDAT.format(M_rstRSSET.getDate("GP_MGPDT"));
					if(M_rstRSSET.getDate("GP_DUEDT") != null)
						strDUEDT = M_fmtLCDAT.format(M_rstRSSET.getDate("GP_DUEDT"));
					//check temperory gate pass sting with recent gate pass number
					
					if(strPRSTS.equals("0") ||strPRSTS.equals("1") || strPRSTS.equals("2") || strPRSTS.equals("3"))
					{
						if(!strMGPNO.equals(strTGPNO))
						{
							if(strTGPNO != "")
								getRPFTR();  //call method to print report footer
							strTGPNO = strMGPNO;
							M_intLINNO = 0;
							M_intPAGNO = 1;
							intSRLNO = 1;
							getRPHDR('P',strMGPDS,strSTRDS);  //call method to print report header
						}
						if(M_intLINNO >50)  //check line is exceed 50
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							M_intPAGNO += 1;
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(dosREPORT,M_strEJT);  //eject page
							getRPHDR('P',strMGPDS,strSTRDS);	//print report header
						}
						dosREPORT.writeBytes("\n");
						strMATCD = nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),"");
						dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
						dosREPORT.writeBytes(padSTRING('R',strMATCD,20));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),""),18));
						dosREPORT.writeBytes(padSTRING('R',"",3));
						//For Getting GRIN Number And Date For the material other than rejected
						//if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))
						if(strMGPTP.equals(strREJTP_fn))
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_GRNNO"),""),16));
						else
						{
							/*M_strSQLQRY = "SELECT CT_PGRNO,CT_PGRDT FROM CO_CTMST WHERE "
								+" CT_MATCD = '"+nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),"")+"'";
							L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
							if(L_rstRSSET.next())
							{*/
								strGRNNO = nvlSTRVL(M_rstRSSET.getString("CT_PGRNO"),"");
								if(M_rstRSSET.getDate("CT_PGRDT") != null)
									strGRNDT = M_fmtLCDAT.format(M_rstRSSET.getDate("CT_PGRDT"));
							//}
							if(strGRNNO.length() ==0)
							{
								strGRNNO ="?";
								strMDVFL ="?";
							}
							else
							{
								M_strSQLQRY = "SELECT GR_MODFL FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"
									+M_strSBSCD.substring(2,4)+"' AND GR_GRNNO = '"+strGRNNO+"'"
								    +" AND GR_MATCD = '"+strMATCD+"'";
								L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
								if(L_rstRSSET.next())
								{
									strMDVFL = nvlSTRVL(L_rstRSSET.getString("GR_MODFL"),"-");
									if(strMDVFL.length() ==0)
										strMDVFL ="?";
								}	
							}
							dosREPORT.writeBytes(padSTRING('R',strGRNNO,16));
							dosREPORT.writeBytes(strMDVFL);
						}
						dosREPORT.writeBytes(padSTRING('R',"",9));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),71));
						if(strMGPTP.equals(strREJTP_fn))
						//if(cmbMGPTP.getSelectedItem().toString().equalsIgnoreCase("Rejection"))
						{
							M_strSQLQRY = "SELECT GR_GRNDT FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"
								+M_strSBSCD.substring(2,4)+"' AND GR_GRNNO = '"+nvlSTRVL(M_rstRSSET.getString("GP_GRNNO"),"")+"'";
							L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
							if(L_rstRSSET.next())
							{
								if(L_rstRSSET.getDate("GR_GRNDT") != null)
									strGRNDT = M_fmtLCDAT.format(L_rstRSSET.getDate("GR_GRNDT"));
							}
						}
						dosREPORT.writeBytes(padSTRING('R',strGRNDT,16));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_INSFL"),""),9));
						intSRLNO += 1;
						M_intLINNO += 1;
						strPREBY = nvlSTRVL(M_rstRSSET.getString("GP_PREBY"),"");
						strAPRBY = nvlSTRVL(M_rstRSSET.getString("GP_APRBY"),"");
						strAUTBY = nvlSTRVL(M_rstRSSET.getString("GP_AUTBY"),"");
					}
					else
					{
						if(!strMGPNO.equals(strTGPNO))
						{
							if(strTGPNO != "")
								getRPFTR();  //call method to print report footer
							strTGPNO = strMGPNO;
							M_intLINNO = 0;
							M_intPAGNO = 1;
							intSRLNO = 1;
							getRPHDR('F',strMGPDS,strSTRDS);  //call method to print report header
						}
						if(M_intLINNO >50)  //check line is exceed 50
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							M_intPAGNO += 1;
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(dosREPORT,M_strEJT);  //eject page
							getRPHDR('F',strMGPDS,strSTRDS);	//print report header
						}
						dosREPORT.writeBytes("\n");
						strMATCD = nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),"");
						dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
					    dosREPORT.writeBytes(padSTRING('R',strMATCD,16));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),62));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),2));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),""),11));
					//	dosREPORT.writeBytes(padSTRING('R',"",3));
						//For Getting GRIN Number And Date For the material other than rejected
						dosREPORT.writeBytes(padSTRING('R',"",9));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",4));
						intSRLNO += 1;
						M_intLINNO += 1;
						strPREBY = nvlSTRVL(M_rstRSSET.getString("GP_PREBY"),"");
						strAPRBY = nvlSTRVL(M_rstRSSET.getString("GP_APRBY"),"");
						strAUTBY = nvlSTRVL(M_rstRSSET.getString("GP_AUTBY"),"");
					}
              
				}
			}
			strPRVVL = strAPRVL;
			strPRSTS = strSTSFL;
			getRPFTR();  //print report footer
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			dosREPORT.close();
			fosREPORT.close();
			strMGPNO="";
			M_intLINNO = 0;
			M_intPAGNO = 1;
			strTGPNO = "";
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genRPFIL");
		}
		setMSG("",'N');
	}
	/** method to print report header
	 */
	void getRPHDR(char P_chrGPSTS,String P_strMGPDS,String P_strSTRDS) // 'P'or 'F' provision or final gate pass
	{
		try
		{
			dosREPORT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strBOLD);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			   
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,96));
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strCPI12);
			if(P_chrGPSTS =='P')
				//dosREPORT.writeBytes(padSTRING('R',cmbMGPTP.getSelectedItem().toString()+" Gate Pass Request",76));
				dosREPORT.writeBytes(padSTRING('R',P_strMGPDS+" Gate Pass Request",76));
			else
				dosREPORT.writeBytes(padSTRING('R',P_strMGPDS+" Gate Pass",76));
			dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
			dosREPORT.writeBytes("\n");
			//cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString()
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+P_strSTRDS ,76));
			dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
			dosREPORT.writeBytes("\n");
			M_intLINNO += 4;
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"M/S."+strVENNM,60));
			dosREPORT.writeBytes(padSTRING('R',"Gate Pass No. : "+M_strSBSCD.substring(2,4)+"/"+strMGPNO,36));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',strADR01,60));
			dosREPORT.writeBytes(padSTRING('R',"Gate Pass Dt. : "+strMGPDT,36));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',strADR02,60));
			dosREPORT.writeBytes(padSTRING('R',"Vehicle No.   : "+strVEHNO,36));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',strCTYNM,60));
			dosREPORT.writeBytes(padSTRING('R',"Carrier Name  : "+strVEHDS,36));
			dosREPORT.writeBytes("\n");						   
			dosREPORT.writeBytes(padSTRING('R',strPINCD,60));
			dosREPORT.writeBytes(padSTRING('R',"Carrier Sign  : ",26));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Tel : "+strTEL01,60));
			dosREPORT.writeBytes(padSTRING('R',"Department    : "+hstDPTCD.get(strDPTCD).toString(),46));
			dosREPORT.writeBytes("\n");
			M_intLINNO += 7;
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n"); 
			if(P_chrGPSTS =='P')
				dosREPORT.writeBytes("Sr. Material Code       UOM                                     Quantity   GRIN No        Cenvat");   
			else
				dosREPORT.writeBytes("Sr. Material Code   Description                                                  UOM    Quantity");   
			
			dosREPORT.writeBytes("\n");
			M_intLINNO += 2;
			if(P_chrGPSTS =='P')
			{
				dosREPORT.writeBytes("No. Description                                                            Date        Insurance");   
				dosREPORT.writeBytes("\n"); 
				M_intLINNO += 1;
			}
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPHDR");
		}
	}
	/** method to print report footer
	 */
	public void getRPFTR()
	{
		try
		{
			////Added by AMJ to Print total Approx value. on 14/05/2008

			M_strSQLQRY = " Select gp_aprvl from mm_gptrn where GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND gp_mgpno = '"+txtTOGPS.getText().trim()+"'";
			ResultSet rstRSSET1 = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(rstRSSET1 !=null && rstRSSET1.next())
			{
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("Total Approx. Value   :  Rs "+rstRSSET1.getString("gp_aprvl")+"\n");
			}	
			
			M_strSQLQRY = "SELECT RM_DOCTP,RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"RM_TRNTP = 'GP' AND RM_DOCTP IN('GP','RQ') AND RM_DOCNO = '"+strTGPNO+"' and isnull(RM_STSFL,'') <>'X'";
			strREMDS="";
			strCOMNT="";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			strREMDS ="";
			while(L_rstRSSET.next())
			{
				if(nvlSTRVL(L_rstRSSET.getString("RM_DOCTP"),"").equals("GP"))
					strREMDS += (nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"")+"|");
				else
					strCOMNT = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
			}
		//	else
		//		strREMDS = "";
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			StringTokenizer L_stkTEMP=new StringTokenizer(strREMDS,"|");
			String L_strTITLE="Remarks  : ";
			while (L_stkTEMP.hasMoreTokens())
			{
				L_strTITLE+=L_stkTEMP.nextToken();
				if(L_strTITLE.length()>95)
				{
					dosREPORT.writeBytes(L_strTITLE.substring(0,95)+"\n");
					if(L_strTITLE.length()>165)
					{
						dosREPORT.writeBytes("           "+L_strTITLE.substring(95,165)+"\n");
						dosREPORT.writeBytes("           "+L_strTITLE.substring(165)+"\n");
					}
					else
						dosREPORT.writeBytes("           "+L_strTITLE.substring(95)+"\n");
				}
				else
					dosREPORT.writeBytes(L_strTITLE+"\n");
				L_strTITLE="          ";
			}
			if(strPRSTS.equals("0")||strPRSTS.equals("1") || strPRSTS.equals("2") || strPRSTS.equals("3"))
			{
				if(strCOMNT.length() >0)
					dosREPORT.writeBytes("Comments : "+strCOMNT);
			}	
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			if(strMGPTP.equals(strRTNGP_fn))
			//   cmbMGPTP.getSelectedIndex() == 0)
			{
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("                                                            Expected Date of Return : "+strDUEDT);
			}
			dosREPORT.writeBytes("\n\n");
			if(strPRSTS.equals("0") ||strPRSTS.equals("1") || strPRSTS.equals("2") || strPRSTS.equals("3"))
			{
				dosREPORT.writeBytes("\n");				
				dosREPORT.writeBytes(padSTRING('R',"",4));
				dosREPORT.writeBytes(padSTRING('R',strPREBY,84));
				dosREPORT.writeBytes(padSTRING('R',strAPRBY,11));
				dosREPORT.writeBytes("\n");				
				dosREPORT.writeBytes("Requested By                                                                       Approved By");
			}
			else if(strPRSTS.equals("4") ||strPRSTS.equals("5") ||strPRSTS.equals("6")||strPRSTS.equals("9"))
			{
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',"",4));
			//	dosREPORT.writeBytes(padSTRING('R',strPREBY,44));
				dosREPORT.writeBytes(padSTRING('R',strAUTBY,11));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("Authorised By                                                                       Received By");
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			if(strPRSTS.equals("0") ||strPRSTS.equals("1") || strPRSTS.equals("2") || strPRSTS.equals("3"))
			{
				dosREPORT.writeBytes("\n Approx Value : Rs. "+strPRVVL);
				dosREPORT.writeBytes("\n G.P. Request sent to Commercial department on    : ");
				dosREPORT.writeBytes("\n Material can be sent out. Excise documents for despatch are ready / not required.");
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------");
				dosREPORT.writeBytes("\n Signature    : ");
				dosREPORT.writeBytes("\n\n");
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------");
				dosREPORT.writeBytes("\n G.P. Request received from Commercial dept. on      :");
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------");
			}
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPFTR");
		}
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			prnFMTCHR(dosREPORT,M_strEJT);
	}
	/** method to print or display report
	 */
	public void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				strMGPDS = cmbMGPTP.getSelectedItem().toString();
				strSTRDS = cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString();
				strMGPTP = hstMGPTP.get(cmbMGPTP.getSelectedItem()).toString();
				genRPFIL(txtFMGPS.getText().trim(),txtTOGPS.getText().trim(),strMGPTP,strMGPDS,strSTRDS);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if (M_rdbTEXT.isSelected())
					    doPRINT(strFILNM);
					else 
			        {    
						Runtime r = Runtime.getRuntime();
						Process p = null;					
						p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
						setMSG("For Printing Select File Menu, then Print  ..",'N');
					}    
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				    Runtime r = Runtime.getRuntime();
					Process p = null;
					if(M_rdbHTML.isSelected())
					    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					else
					    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
				   	cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Gate Pass Printing "," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
			    }
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
	/** method to check valid data before processing
	 */
	boolean vldDATA()
	{
		if(M_rdbHTML.isSelected())
		     strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgpp.html";
		if(M_rdbTEXT.isSelected())
		     strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgpp.doc";
		
		if(txtFMGPS.getText().trim().length() < 8)
		{
			setMSG("Enter From Gate Pass No Or Press 'F1' For Help",'E');
			txtFMGPS.requestFocus();
			return false;
		}
		if(txtTOGPS.getText().trim().length() < 8)
		{
			setMSG("Enter From To Pass No Or Press 'F1' For Help",'E');
			txtTOGPS.requestFocus();
			return false;
		}
		if(Integer.parseInt(txtFMGPS.getText().trim()) > Integer.parseInt(txtTOGPS.getText().trim()))
		{
			setMSG("To Gate Pass No Should Be Greater Than  From Gate Pass No",'E');
			txtTOGPS.requestFocus();
			return false;
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if(M_cmbDESTN.getItemCount() ==0)
			{
				setMSG("Please select E-mail Id by using the F1 list ..",'E');
				return false;
			}
		}
		setMSG("",'N');
		return true;
	}
	
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(input == txtFMGPS || input == txtTOGPS )
				{
					if(((JTextField)input).getText().length() < 8)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("SELECT COUNT(*) FROM MM_GPTRN WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_MGPNO = '"+((JTextField)input).getText()+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(GP_STSFL,' ') <> 'X'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								setMSG("Invalid Gate Pass No ",'E');
								L_rstTEMP.close();
								return false;
							}
						}
						else
						{
							setMSG("Invalid Gate Pass No ",'E');
							L_rstTEMP.close();
							return false;
						}
					}
					else
					{
						setMSG("Invalid Gate Pass No ",'E');
						L_rstTEMP.close();
						return false;
					}
					L_rstTEMP.close();
				}
				return true;
			}
			catch(SQLException L_SQL)
			{
				setMSG(L_SQL,"verify");
				return false;
			}
		}
	}
}