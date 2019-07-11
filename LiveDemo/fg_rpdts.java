import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.ResultSet;import java.util.Calendar;
import java.text.SimpleDateFormat;

class fg_rpdts extends cl_rbase 
{
	private JRadioButton rdbDAILY;		
	private JRadioButton rdbMONTH;		
	private ButtonGroup btgRTYPE;
	private JLabel lblDSPDT;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"fg_rpdts.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	
	private	String strRCTTP_FRESH = "10";
	private	String strRCTTP_REBAGG = "15";
	private	String strRCTTP_SRTN = "30";	
	private	String strRCTTP_JBWRK = "21";	
			
	private	String strISSTP_DNGRDG = "61";	
			
	private	String strPRDTP_PS = "51";
	private	String strPRDTP_SPS = "52";
	private	String strPRDTP_MB = "54";
			
	private	String strPRTCD_PS = "S7773";
	private	String strPRTCD_SPS = "S7771";
			
	private	String strSALTP_DOM = "01";
	private	String strSALTP_STKTR = "04";
	private	String strSALTP_FTS = "05";
	private	String strSALTP_CAPT = "16";
	private	String strSALTP_EXP = "12";
	private	String strSALTP_DEXP = "03";
	private int intGRPLN;
    private String strSTRDT;
	private String strENDDT;
	private java.util.Date datLOCDT;
	private SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("dd/MM/yyyy"); 
	
	/** for grand total
	*/
	private float fltMNOQT=0;
	private float fltMNRQT=0;
	private float fltPSCQT=0;
	private float fltSPCQT=0;
	private float fltACTBG=0;
	private float fltSLRQT=0;
	private float fltJBRQT=0;
	private float fltDMDQT=0;
	private float fltEXDQT=0;
	private float fltDEDQT=0;
	private float fltSRDQT=0;
	private float fltSREQT=0;
	private float fltSPDQT=0;
	private float fltSCPQT=0;
	private float fltMNCQT=0;
	
	fg_rpdts()		/*  Constructor   */
	{
		super(2);
		try
		{
			//System.out.println("hiiiiiiiiii");
			add(rdbDAILY=new JRadioButton("Daily"),5,3,1,1,this,'L');
			add(rdbMONTH=new JRadioButton("Monthly"),6,3,1,1,this,'L');
			add(lblDSPDT=new JLabel(),7,3,1,3,this,'L');
			btgRTYPE=new ButtonGroup();
			
			btgRTYPE.add(rdbMONTH);btgRTYPE.add(rdbDAILY);
			rdbMONTH.setSelected(true);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			M_pnlRPFMT.setVisible(true);			
						
			M_strSQLQRY =" Select CMT_CCSVL from CO_CDTRN where";
			M_strSQLQRY+=" CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXREF' and CMT_CODCD='DOCDT' ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>>chkLOCKINGDATE>>>>"+M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET !=null)
				datLOCDT=M_fmtLCDAT.parse(M_rstRSSET.getString("CMT_CCSVL"));
			else
				setMSG("LOCKING date does not exist ",'E');
			
			//M_calLOCAL.setTime(datLOCDT);      
			//M_calLOCAL.add(Calendar.DATE,+1);    
			//datLOCDT = M_calLOCAL.getTime();

			strENDDT=fmtYYYYMMDD.format(datLOCDT);
			strSTRDT="01"+strENDDT.substring(2,10);
			lblDSPDT.setText("As On "+strENDDT+" 7:00 hrs");
			lblDSPDT.setForeground(Color.BLUE);    
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(rdbDAILY.isSelected())
		{
			strSTRDT=strENDDT;
		}	
		else if(rdbMONTH.isSelected())
		{
			strSTRDT="01"+strENDDT.substring(2,10);
		}	
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is KeyPressed");
		}	
	
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{

		}
		catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}
	
	private String getCATDS(String LP_CODCD)
	{
		try
		{
			ResultSet rstRSSET1;
			String strSQLQRY1;
			strSQLQRY1 =" Select CMT_SHRDS from CO_CDTRN ";
			strSQLQRY1+=" where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPGR' and SUBSTRING(CMT_CODCD,1,4)='"+LP_CODCD+"'";
			rstRSSET1=cl_dat.exeSQLQRY2(strSQLQRY1);
			if(rstRSSET1.next() && rstRSSET1!=null)
			{
				return rstRSSET1.getString("CMT_SHRDS");
			}	
				
		}
		catch(Exception L_EX)
		{
		 setMSG(L_EX,"getCATDS()"); 
		}
		return "";
	}	
	
	void genRPTFL()
	{
		try
		{
			String L_strOLDGRPCD="";
			String L_strNEWGRPCD="";
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			genRPHDR();
			float fltACTBG=0;
			String L_strCATDS;
			M_strSQLQRY =" Select DS_GRPCD,isnull(DS_MNOQT,0) DS_MNOQT,isnull(DS_MNRQT,0) DS_MNRQT,isnull(DS_PSCQT,0) DS_PSCQT,isnull(DS_SPCQT,0) DS_SPCQT,";
			M_strSQLQRY+=" isnull(DS_SLRQT,0) DS_SLRQT,isnull(DS_JBRQT,0) DS_JBRQT,isnull(DS_DMDQT,0) DS_DMDQT,isnull(DS_EXDQT,0) DS_EXDQT,isnull(DS_DEDQT,0) DS_DEDQT,";
			M_strSQLQRY+=" isnull(DS_SRDQT,0) DS_SRDQT,isnull(DS_SREQT,0) DS_SREQT,isnull(DS_SPDQT,0) DS_SPDQT,isnull(DS_SCPQT,0) DS_SCPQT,isnull(DS_MNCQT,0) DS_MNCQT";
			M_strSQLQRY+=" from FG_DSWRK";
			M_strSQLQRY+=" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(DS_GRPCD,3,4) <> '00'";
			M_strSQLQRY+=" order by DS_GRPCD";			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					L_strNEWGRPCD=M_rstRSSET.getString("DS_GRPCD").substring(0,2);
					System.out.println(">>L_strNEWGRPCD>>"+L_strNEWGRPCD+">>L_strNEWGRPCD>>"+L_strNEWGRPCD);
					if(!L_strNEWGRPCD.equals(L_strOLDGRPCD) && !L_strOLDGRPCD.equals(""))
					{
						crtNWLIN();
						prnTOTAL(L_strOLDGRPCD);
						crtNWLIN();
					}
					L_strOLDGRPCD=M_rstRSSET.getString("DS_GRPCD").substring(0,2);
					L_strCATDS=getCATDS(M_rstRSSET.getString("DS_GRPCD"));
					fltACTBG=M_rstRSSET.getFloat("DS_MNRQT")-(M_rstRSSET.getFloat("DS_PSCQT")+M_rstRSSET.getFloat("DS_SPCQT"));
					D_OUT.writeBytes(padSTRING('R',L_strCATDS,15));
					//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("DS_GRPCD"),15));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_MNOQT")),15));			
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_MNRQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_PSCQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SPCQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltACTBG,3).toString()),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SLRQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_JBRQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_DMDQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_EXDQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_DEDQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SRDQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SREQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SPDQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SCPQT")),12));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_MNCQT")),15));
					crtNWLIN();
				}
				crtNWLIN();
				prnTOTAL("54");
				crtNWLIN();
				prnGRNDTT();
				crtNWLIN();
			}	
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			if(M_rstRSSET==null)
			{
				M_rstRSSET.close();
			}	
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genRPTFL");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	private void prnTOTAL(String LP_SSGRPCD)
	{
		try{
			LP_SSGRPCD=LP_SSGRPCD+"00";
			float fltACTBG1=0;
			String L_strCATDS1;
			String strSQLQRY;
			ResultSet rstRSSET;
			strSQLQRY =" Select DS_GRPCD,isnull(DS_MNOQT,0) DS_MNOQT,isnull(DS_MNRQT,0) DS_MNRQT,isnull(DS_PSCQT,0) DS_PSCQT,isnull(DS_SPCQT,0) DS_SPCQT,";
			strSQLQRY+=" isnull(DS_SLRQT,0) DS_SLRQT,isnull(DS_JBRQT,0) DS_JBRQT,isnull(DS_DMDQT,0) DS_DMDQT,isnull(DS_EXDQT,0) DS_EXDQT,isnull(DS_DEDQT,0) DS_DEDQT,";
			strSQLQRY+=" isnull(DS_SRDQT,0) DS_SRDQT,isnull(DS_SREQT,0) DS_SREQT,isnull(DS_SPDQT,0) DS_SPDQT,isnull(DS_SCPQT,0) DS_SCPQT,isnull(DS_MNCQT,0) DS_MNCQT";
			strSQLQRY+=" from FG_DSWRK";
			strSQLQRY+=" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+LP_SSGRPCD+"'";
			strSQLQRY+=" order by DS_GRPCD";			
			rstRSSET=cl_dat.exeSQLQRY1(strSQLQRY);
			System.out.println(">>>prnTOTAL>>>>>>"+strSQLQRY);		
    		prnFMTCHR(D_OUT,M_strBOLD);			
			if(rstRSSET.next() && rstRSSET!=null)		
			{
				L_strCATDS1=getCATDS(rstRSSET.getString("DS_GRPCD"));
				fltACTBG1=rstRSSET.getFloat("DS_MNRQT")-(rstRSSET.getFloat("DS_PSCQT")+rstRSSET.getFloat("DS_SPCQT"));
				D_OUT.writeBytes(padSTRING('R',"Total "+L_strCATDS1,15));
				//D_OUT.writeBytes(padSTRING('R',rstRSSET.getString("DS_GRPCD"),15));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_MNOQT")),15));			
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_MNRQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_PSCQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SPCQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltACTBG1,3).toString()),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SLRQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_JBRQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_DMDQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_EXDQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_DEDQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SRDQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SREQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SPDQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SCPQT")),12));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_MNCQT")),15));
				crtNWLIN();
				fltMNOQT+=rstRSSET.getFloat("DS_MNOQT");
				fltMNRQT+=rstRSSET.getFloat("DS_MNRQT");
				fltPSCQT+=rstRSSET.getFloat("DS_PSCQT");
				fltSPCQT+=rstRSSET.getFloat("DS_SPCQT");
				fltACTBG+=fltACTBG1;
				fltSLRQT+=rstRSSET.getFloat("DS_SLRQT");
				fltJBRQT+=rstRSSET.getFloat("DS_JBRQT");
				fltDMDQT+=rstRSSET.getFloat("DS_DMDQT");
				fltEXDQT+=rstRSSET.getFloat("DS_EXDQT");
				fltDEDQT+=rstRSSET.getFloat("DS_DEDQT");
				fltSRDQT+=rstRSSET.getFloat("DS_SRDQT");
				fltSREQT+=rstRSSET.getFloat("DS_SREQT");
				fltSPDQT+=rstRSSET.getFloat("DS_SPDQT");
				fltSCPQT+=rstRSSET.getFloat("DS_SCPQT");
				fltMNCQT+=rstRSSET.getFloat("DS_MNCQT");
			}
    		prnFMTCHR(D_OUT,M_strNOBOLD);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnTOTAL()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}	
	
	
	private void prnGRNDTT()
	{
		try
		{
    		prnFMTCHR(D_OUT,M_strBOLD);			
			D_OUT.writeBytes(padSTRING('R',"Grand Total ",15));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltMNOQT,3).toString()),15));			
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltMNRQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltPSCQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltSPCQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltACTBG,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltSLRQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltJBRQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltDMDQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltEXDQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltDEDQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltSRDQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltSREQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltSPDQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltSPCQT,3).toString()),12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(fltMNCQT,3).toString()),15));
    		prnFMTCHR(D_OUT,M_strNOBOLD);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRNDTT()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

	
	private String chkZERO(String LP_STR)
	{
		try
		{
			if(LP_STR.equals("0.000"))
				return "-";
			else
				return LP_STR;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkZERO()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return "";
	}
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >70)
				{
					genRPFTR();
					genRPHDR();
				}
			}	
			else if(cl_dat.M_intLINNO_pbst >60)
			{		
				genRPFTR();
				genRPHDR();			
			}			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

	void genRPHDR()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
			
				
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title>Overall Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		D_OUT.writeBytes(padSTRING('R',"Overall Summary Extracts For The",33));
			if(M_fmtLCDAT.parse(strSTRDT).compareTo(M_fmtLCDAT.parse(strENDDT))==0)
				D_OUT.writeBytes(padSTRING('R',strSTRDT,50));
			else
				D_OUT.writeBytes(padSTRING('R',"Period "+strSTRDT+" to "+strENDDT,50));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		crtNWLIN();
			
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Category               Opening     Bagging     Captive     Captive      Actual       Sales         Job    <----------Despatches---------->    Despatch    Despatch    Transfer         SCP        Closing");
			crtNWLIN();
			D_OUT.writeBytes("                         Stock  During the       to PS      to SPS     Bagging      Return       Works    Domestic      Export      Deemed    Domestic      Export      to SPS    Consumpn          Stock");
			crtNWLIN();
			D_OUT.writeBytes("                                    Period	                                                                                                from SLR    from SLR                                       ");
			crtNWLIN();
			D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			crtNWLIN();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}	

	boolean vldDATA()
	{
		try
		{

		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA()");
		}
		return true;
	}
	
	void exePRINT()
	{
		try
		{
			M_strSQLQRY = "delete from fg_dswrk";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			cl_dat.exeDBCMT("exeUPDTBLS");
			exeUPDTBLS(4);
			exeUPDTBLS(2);
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "fg_rpdts.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "fg_rpdts.doc";
				
			genRPTFL();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				*/	
				}	
				else 
			    {    
					Runtime r = Runtime.getRuntime();
					Process p = null;					    
					p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
			    if(M_rdbHTML.isSelected())
			        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			    else
			        p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	
	/**
	 */
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		

	
	}

	/**
	 */
	private void putFGDSWRK(ResultSet LP_RSSET, String LP_FLDNM)
	{
		try
		{
			while(LP_RSSET.next() && LP_RSSET !=null)
			{
				if(getCOUNT(LP_RSSET.getString("ds_grpcd"))==0)
					insSTRING(LP_RSSET.getString("ds_grpcd"),LP_RSSET.getString("ds_xxxqt"),LP_FLDNM);
				else
					updSTRING(LP_RSSET.getString("ds_grpcd"),LP_RSSET.getString("ds_xxxqt"),LP_FLDNM);
			}
	
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"putFGDSWRK");
		}
	}
	

		//MNRQT Down grading (to be transferred to PSFS -  from '5111' to '5197',  '5112' to '5297')
	/**
	 */
	private void putFGDSWRK_DGR(ResultSet LP_RSSET)
	{
		try
		{
			while(LP_RSSET.next() && LP_RSSET !=null)
			{
				String L_strFRGRP = LP_RSSET.getString("ds_grpcd"), L_strTOGRP = "";
				if(L_strFRGRP.substring(0,2).equals("51"))
				{
					L_strTOGRP = "5197";
					
					if(getCOUNT(L_strFRGRP)==0)
						insSTRING_DGR(L_strFRGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");
					else
						updSTRING_DGR(L_strFRGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");

					if(getCOUNT(L_strTOGRP)==0)
						insSTRING(L_strTOGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");
					else
						updSTRING(L_strTOGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");
				}
				if(L_strFRGRP.substring(0,2).equals("52"))
				{
					L_strTOGRP = "5297";
					
					if(getCOUNT(L_strFRGRP)==0)
						insSTRING_DGR(L_strFRGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");
					else
						updSTRING_DGR(L_strFRGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");

					if(getCOUNT(L_strTOGRP)==0)
						insSTRING(L_strTOGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");
					else
						updSTRING(L_strTOGRP,LP_RSSET.getString("ds_xxxqt"),"DS_MNRQT");
				}
			}
	
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"putFGDSWRK");
		}
	}
	
	
	/**
	 */
	public void exeUPDTBLS(int LP_GRPLN)
	{
		try
		{
			intGRPLN = LP_GRPLN;
			
			//MNOQT
			setMSG("Fetching Opening Stock",'N');
			M_strSQLQRY =" Select SUBSTRING(st_prdcd,1,"+LP_GRPLN+") ds_grpcd ,";
			if(rdbMONTH.isSelected())
				M_strSQLQRY+=" sum(op_mosqt) ds_xxxqt from fg_opstk,";
			else
				M_strSQLQRY+=" sum(st_posqt) ds_xxxqt from";
			M_strSQLQRY+=" fg_stmst";
			M_strSQLQRY+=" where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_dosqt,0)>0";
			M_strSQLQRY+=" group by SUBSTRING(st_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(st_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_MNOQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_MNOQT");
			
			//MNRQT
			setMSG("Fetching Bagging During The Period",'N');
			M_strSQLQRY =" select SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(rct_rctqt) ds_xxxqt ";
			M_strSQLQRY+=" from fg_rctrn,pr_ltmst";
			M_strSQLQRY+=" where isnull(rct_rctqt,0)>0 and rct_cmpcd = lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_rclno = lt_rclno and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp in ('"+strRCTTP_FRESH+"','"+strRCTTP_REBAGG+"') and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and rct_stsfl='2' and rct_rctqt>0";
			M_strSQLQRY+=" group by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_MNRQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_MNRQT");
			
			//Captive Receipt  PS
			setMSG("Fetching Captive to PS",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  isnull(ivt_invqt,0)>0 and  ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_PS+"'  and SUBSTRING(ivt_prdcd,1,2) = '"+strPRDTP_PS+"' and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0  group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_strSQLQRY+=" order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_PSCQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_PSCQT");

			//Captive Receipt  SPS
			setMSG("Fetching Captive Receipt  SPS",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_SPS+"'  and SUBSTRING(ivt_prdcd,1,2) in ('"+strPRDTP_SPS+"','"+strPRDTP_MB+"')  and IVT_PRDCD <> '5211951680' and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_SPCQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SPCQT");

			//SLRQT			
			setMSG("Fetching Captive Sales Return",'N');
			M_strSQLQRY =" select SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(rct_rctqt) ds_xxxqt";
			M_strSQLQRY+=" from fg_rctrn, pr_ltmst";
			M_strSQLQRY+=" where isnull(rct_rctqt,0)>0 and rct_cmpcd = lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_rclno = lt_rclno and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp in ('"+strRCTTP_SRTN+"') and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and rct_stsfl='2' and rct_rctqt>0";
			M_strSQLQRY+=" group by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_SLRQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SLRQT");
			
			//DMDQT      Domestic despatch
			setMSG("Fetching Domestic Despatch",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp in ('"+strSALTP_DOM+"', '"+strSALTP_STKTR+"','"+strSALTP_FTS+"') and ivt_slrfl='1' and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_DMDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_DMDQT");
			
			//EXDQT    Export Despatch
			setMSG("Fetching Export Despatch",'N');
  			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt ";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and ivt_saltp='"+strSALTP_EXP+"'  and ivt_slrfl='1'  and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_EXDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_EXDQT");

			
		    //DEDQT   Deemed Export Despatch
			setMSG("Fetching Deemed Export Despatch",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt"; 
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and  ivt_saltp='"+strSALTP_DEXP+"'   and ivt_slrfl='1'  and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";   
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";  
			System.out.println(">>>> DS_DEDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_DEDQT");
			
			
			//SRDQT   Domestic Despatches from Sales Return
			setMSG("Fetching Domestic Despatches from Sales Return",'N');
  			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp ='"+strSALTP_DOM+"'  and IVT_SLRFL = '2' and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_SRDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SRDQT");

			//SREQT
			setMSG("Fetching Export Despatches from Sales Return",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  isnull(ivt_invqt,0)>0 and  ivt_saltp = '"+strSALTP_EXP+"'  and IVT_SLRFL = '2' and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_SREQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SREQT");

			//Job Work Receipt;
			setMSG("Fetching Job Work Receipt",'N');
			M_strSQLQRY =" select SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(rct_rctqt) ds_xxxqt"; 
			M_strSQLQRY+=" from fg_rctrn,pr_ltmst";
			M_strSQLQRY+=" where isnull(rct_rctqt,0)>0 and  rct_cmpcd = lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_rclno = lt_rclno and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp = '"+strRCTTP_JBWRK+"' and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and rct_stsfl='2' and rct_rctqt>0";   
			M_strSQLQRY+=" group by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+")";  
			System.out.println(">>>>DS_JBRQT>>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_JBRQT");
			
		    //SCP used by SPS	
			setMSG("SCP used by SPS",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt"; 
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_SPS+"'  and SUBSTRING(ivt_prdcd,1,2) in ('"+strPRDTP_SPS+"','"+strPRDTP_MB+"')  and IVT_PRDCD = '5211951680' and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";   
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";  
			System.out.println(">>>>DS_SCPQT>>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SCPQT");
			
		    //PS to SPS transfer
			setMSG("PS to SPS transfer",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt"; 
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_SPS+"'  and SUBSTRING(ivt_prdcd,1,2) = '"+strPRDTP_PS+"' and date(ivt_invdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";   
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";  
			System.out.println(">>>>DS_SPDQT>>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SPDQT");
			
			//MNCQT
			setMSG("Fetching Closing Stock",'N');
			M_strSQLQRY =" Select SUBSTRING(st_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(st_dosqt) ds_xxxqt ";
			M_strSQLQRY+=" from fg_stmst";
			M_strSQLQRY+=" where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_dosqt,0)>0 ";
			M_strSQLQRY+=" group by SUBSTRING(st_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(st_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>> DS_MNCQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_MNCQT");

			
			//MNRQT Down grading (to be transferred to PSFS -  from '5111' to '5197',  '5112' to '5297')
			if(LP_GRPLN==4)
			{
				M_strSQLQRY =" select SUBSTRING(ist_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ist_issqt) ds_xxxqt"; 
				M_strSQLQRY+=" from fg_istrn";  
				M_strSQLQRY+=" where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_isstp = '"+strISSTP_DNGRDG+"' and ist_autdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ist_stsfl='2' and ist_issqt>0";
				M_strSQLQRY+=" group by SUBSTRING(ist_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ist_prdcd,1,"+LP_GRPLN+")";   
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				System.out.println(">>>> DS_MNRQT (DGR) >>>>"+M_strSQLQRY);
				putFGDSWRK_DGR(M_rstRSSET);
			}
			

			
		//MNRQT Down grading (to be transferred to PSFS -  from '5111' to '5197',  '5112' to '5297')
		//	M_strSQLQRY =" select SUBSTRING(ist_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ist_issqt) ds_xxxqt"; 
		//	M_strSQLQRY+=" from fg_istrn";  
		//	M_strSQLQRY+=" where   ist_isstp = '"+strISSTP_DNGRDG+"' and ist_autdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ist_stsfl='2' and ist_issqt>0";
		//	M_strSQLQRY+=" group by SUBSTRING(ist_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ist_prdcd,1,"+LP_GRPLN+")";   
		
		
		cl_dat.exeDBCMT("exeSAVE");
		}	
		catch(Exception L_E)
		{
			setMSG(L_E,"exeUPDTBLS()");
		}
	}	
	
	public int getCOUNT(String ds_grpcd)
	{
		try
		{
			String strSQLQRY;
			ResultSet rstRSSET;
		
			strSQLQRY =" Select count(*) CNT";
			strSQLQRY+=" from FG_DSWRK";strSQLQRY =" Select count(*) CNT";
			strSQLQRY+=" from FG_DSWRK";
			strSQLQRY+=" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+ds_grpcd+(intGRPLN==2?"00":"")+"'";
			rstRSSET=cl_dat.exeSQLQRY1(strSQLQRY);
			System.out.println(">>>>strSQLQRY>>>>"+strSQLQRY);
			if(rstRSSET.next() && rstRSSET !=null)
			{
				return rstRSSET.getInt("CNT");
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getCOUNT()");
		}
		return -1;
	}	
	
	/**
	 */
	public void insSTRING(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			
			strSQLQRY1 =" Insert into FG_DSWRK(DS_GRPCD,"+lp_fldnm+") values(";
			strSQLQRY1+="'"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"',"; 
			strSQLQRY1+= lp_valqt+")";
			System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");							
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"insSTRING()");
		}
	}	


	
	/**
	 */
	public void insSTRING_DGR(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			
			strSQLQRY1 =" Insert into FG_DSWRK(DS_GRPCD,"+lp_fldnm+") values(";
			strSQLQRY1+="'"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"',"; 
			strSQLQRY1+= lp_valqt+"*(-1))";
			System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");							
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"insSTRING()");
		}
	}	
	
	/**
	 */
	public void updSTRING(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			strSQLQRY1 =" Update FG_DSWRK set  "+lp_fldnm+"=isnull("+lp_fldnm+",0)+"+lp_valqt+" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"'";	
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");				
			System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"updSTRING()");
		}
	}	

	/**
	 */
	public void updSTRING_DGR(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			strSQLQRY1 =" Update FG_DSWRK set  "+lp_fldnm+"=isnull("+lp_fldnm+",0)-"+lp_valqt+" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"'";	
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");				
			System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"updSTRING()");
		}
	}	
	
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{

		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
}



