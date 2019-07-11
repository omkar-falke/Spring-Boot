import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.ResultSet;import java.util.Calendar;
import java.text.SimpleDateFormat;import javax.swing.*;import javax.swing.border.*;	

/*

System Name :  Finished Goods Information Management System
Program Name : Bagging Despatch & Stock
Source Directory : f:\source\splerp2\fg_rpdbs	        Executable Directory : f:\exec\splerp2\fg_rpdbs

Purpose :  To give overall summary about daily and monthly transactions.

List of tables used :
Table Name		Primary key							Operation done
																Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - -
FG_DSWRK		DS_GRPCD											/		 /	    /	    /		
CO_CDTRN		CMT_CGMTP,CMT_CGSTP,CMT_CODCD						                /
FG_OPSTK		OP_WRHTP,OP_PRDTP,OP_PRDCD,OP_PKGTP						            /
FG_STMST		ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_MNLCD				/
FG_RCTRN		RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,
				RCT_LOTNO,RCT_RCLNO,RCT_PKGTP,RCT_MNLCD								/
PR_LTMST		LT_PRDTP,LT_LOTNO,LT_RCLNO											/
MR_IVTRN		IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP								/
FG_ISTRN		IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDCD,			    
				IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PKGTP,IST_MNLCD					/

-----------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name			Table name			Type/Size	Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - 
rdbDAILY										 printing report for a day
rdbMONTH										 printing report for a month

-----------------------------------------------------------------------------------------------------

List of Columns  displayed in the Report : 
Column Name	 						Display Columns			Table Name     
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - - - - - - - - - - - - 
Category							DS_GRPCD				FG_DSWRK
(A)Opening Stock					DS_MNOQT								
(B)Bagging During The Period		DS_MNRQT									
(C)Captive To PS					DS_PSCQT									
(D)Captive to SPS					DS_SPCQT									
(E)Actual Bagging					B-(C+D)									
(F)Sales Return						DS_SLRQT									
(G)Job Works						DS_JBRQT									
   Despatches															
		Domestic					DS_DMDQT									
		Export						DS_EXDQT									
		Deemed						DS_DEDQT									
   Despatches From Sales Return													
		Domestic					DS_SRDQT									
		Export						DS_SREQT									
(H)Total Despatch														
(I)Transfer To SPS					DS_SPDQT								
() Captive to Job Work				DS_CJBQT
(J)SCP Consumption					DS_SCPQT									
(K)Closing Stock					DS_MNCQT									
-----------------------------------------------------------------------------------------------------
Other  Requirements :

->If rdbDAILY is Selected then report for the day will be displayed.
->If rdbMONTH is Selected then report for the month will be displayed.
->Takes All the required Data from respective tables.
->Stores the data in to the FG_DSWRK table.
->Then Data is Fetched From FG_DSWRK For Display.
->Actual Bagging is calculated by B-(C+D)
->If Closing Stock <> Calculated Closing Stock(A+E+F+G-H-I-J) then special marking (*) is given.


*/

class fg_rpbds extends cl_rbase 
{
									/**Radio Button for report for day*/
	private JRadioButton rdbDAILY;	/**Radio Button for report for month*/	
	private JRadioButton rdbMONTH;		
	private ButtonGroup btgRTYPE;	/**label to display report date*/
	private JLabel lblDSPDT;
	private JPanel pnlMAIN;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"fg_rpbds.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;		/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;			/** DataOutputStream to hold Stream of report data.*/   
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
	private	String strPRTCD_JB = "S7776";
			
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
	
	/** Variables used to calculate grand total
	*/
	private double dblMNOQT=0;
	private double dblMNRQT=0;
	private double dblPSCQT=0;
	private double dblSPCQT=0;
	private double dblACTBG=0;
	private double dblSLRQT=0;
	private double dblJBRQT=0;
	private double dblDMDQT=0;
	private double dblEXDQT=0;
	private double dblDEDQT=0;
	private double dblSRDQT=0;
	private double dblSREQT=0;
	private double dblSPDQT=0;
	private double dblCJBQT=0;
	private double dblSCPQT=0;
	private double dblMNCQT=0;
	private double dblTTDSP=0;
	
	fg_rpbds()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,20);
			pnlMAIN= new JPanel(null);
			pnlMAIN.setBorder(new EtchedBorder(Color.black,Color.lightGray));
			add(rdbDAILY=new JRadioButton("Daily"),1,2,1,2,pnlMAIN,'L');
			add(rdbMONTH=new JRadioButton("Monthly"),2,2,1,2,pnlMAIN,'L');
			add(lblDSPDT=new JLabel(),3,2,1,5,pnlMAIN,'L');
			btgRTYPE=new ButtonGroup();
			add(pnlMAIN,5,7,5,6,this,'L');
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
						
			// get Locking date from CO_CDTRN
			M_strSQLQRY =" Select CMT_CCSVL from CO_CDTRN where";
			M_strSQLQRY+=" CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXREF' and CMT_CODCD='DOCDT' ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>>chkLOCKINGDATE>>>>"+M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET !=null)
				datLOCDT=M_fmtLCDAT.parse(M_rstRSSET.getString("CMT_CCSVL"));
			else
				setMSG("LOCKING date does not exist ",'E');
			
			strENDDT=fmtYYYYMMDD.format(datLOCDT);
			strSTRDT="01"+strENDDT.substring(2,10);
			
			M_calLOCAL.setTime(datLOCDT);      
			M_calLOCAL.add(Calendar.DATE,+1);    
			datLOCDT = M_calLOCAL.getTime();
			lblDSPDT.setText("As On "+fmtYYYYMMDD.format(datLOCDT)+" 7:00 hrs");
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
		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			dblMNOQT=0;dblMNRQT=0;dblPSCQT=0;dblSPCQT=0;dblACTBG=0;dblSLRQT=0;dblJBRQT=0;dblDMDQT=0;
			dblEXDQT=0;dblDEDQT=0;dblSRDQT=0;dblSREQT=0;dblSPDQT=0;dblCJBQT=0;dblSCPQT=0;dblMNCQT=0;dblTTDSP=0;
		}	
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
	
	/** returns short descreption for corresponding GRPCD
	 */
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
			double dblACTBG=0;    //Stores actual bagging as B-(C+D)
			double dblTTDSP1=0;	 //total despatch
			double dblCNTCL=0;	 //Count for Closing Sock A+E+F+G-H-I-J 
			double dblDIFF=0;	 //Diff of actual closing stock and calculated closing stock 
			String L_strCATDS;
			
			M_strSQLQRY =" Select DS_GRPCD,isnull(DS_MNOQT,0) DS_MNOQT,isnull(DS_MNRQT,0) DS_MNRQT,isnull(DS_PSCQT,0) DS_PSCQT,isnull(DS_SPCQT,0) DS_SPCQT,";
			M_strSQLQRY+=" isnull(DS_SLRQT,0) DS_SLRQT,isnull(DS_JBRQT,0) DS_JBRQT,isnull(DS_DMDQT,0) DS_DMDQT,isnull(DS_EXDQT,0) DS_EXDQT,isnull(DS_DEDQT,0) DS_DEDQT,";
			M_strSQLQRY+=" isnull(DS_SRDQT,0) DS_SRDQT,isnull(DS_SREQT,0) DS_SREQT,isnull(DS_SPDQT,0) DS_SPDQT,isnull(DS_CJBQT,0) DS_CJBQT,isnull(DS_SCPQT,0) DS_SCPQT,isnull(DS_MNCQT,0) DS_MNCQT";
			M_strSQLQRY+=" from FG_DSWRK";
			M_strSQLQRY+=" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(DS_GRPCD,3,4) <> '00'";
			M_strSQLQRY+=" order by DS_GRPCD";			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					L_strNEWGRPCD=M_rstRSSET.getString("DS_GRPCD").substring(0,2);
					//System.out.println(">>L_strNEWGRPCD>>"+L_strNEWGRPCD+">>L_strNEWGRPCD>>"+L_strNEWGRPCD);
					if(!L_strNEWGRPCD.equals(L_strOLDGRPCD) && !L_strOLDGRPCD.equals(""))
					{
						crtNWLIN();
						prnTOTAL(L_strOLDGRPCD);
						crtNWLIN();
					}
					L_strOLDGRPCD=M_rstRSSET.getString("DS_GRPCD").substring(0,2);
					L_strCATDS=getCATDS(M_rstRSSET.getString("DS_GRPCD"));
					dblACTBG=M_rstRSSET.getDouble("DS_MNRQT")-(M_rstRSSET.getDouble("DS_PSCQT")+M_rstRSSET.getDouble("DS_SPCQT"));
					dblTTDSP1=M_rstRSSET.getDouble("DS_DMDQT")+M_rstRSSET.getDouble("DS_EXDQT")+M_rstRSSET.getDouble("DS_DEDQT")+M_rstRSSET.getDouble("DS_SRDQT")+M_rstRSSET.getDouble("DS_SREQT");
					D_OUT.writeBytes(padSTRING('R',L_strCATDS,12));
					//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("DS_GRPCD"),15));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_MNOQT")),11));			
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_MNRQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_PSCQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SPCQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblACTBG,3).toString()),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SLRQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_JBRQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_DMDQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_EXDQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_DEDQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SRDQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SREQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblTTDSP1,3).toString()),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SPDQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_CJBQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_SCPQT")),11));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("DS_MNCQT")),11));
					
					/**if Closing stock != A+D+E-F-G-H display '*'
					 */
					dblCNTCL=M_rstRSSET.getDouble("DS_MNOQT")+dblACTBG+M_rstRSSET.getDouble("DS_SLRQT")+M_rstRSSET.getDouble("DS_JBRQT")-dblTTDSP1-M_rstRSSET.getDouble("DS_SPDQT")-M_rstRSSET.getDouble("DS_CJBQT")-M_rstRSSET.getDouble("DS_SCPQT");
					dblDIFF=Double.parseDouble(setNumberFormat(M_rstRSSET.getDouble("DS_MNCQT"),3)) - Double.parseDouble(setNumberFormat(dblCNTCL,3));
					// if error > 0.001 or error is less than 0.001 then display that error 
					if(dblDIFF != 0)
					{	
						D_OUT.writeBytes(padSTRING('L'," ["+(dblDIFF>0 ? "+" : "")+setNumberFormat(dblDIFF,3),10)+"]");	
					}
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
	
	/** Method Prints Total when substring(GRPCD,0,2) changes from "51" to"52" and same for "52" to "53" and "53" to "54"
	 */
	private void prnTOTAL(String LP_SSGRPCD)
	{
		try{
			LP_SSGRPCD=LP_SSGRPCD+"00";
			double dblACTBG1=0;		//Stores actual bagging as B-(C+D)
			double dblTTDSP1=0;		//total despatch
			double dblCNTCL=0;		//Count for Closing Sock A+E+F+G-H-I-J 
			double dblDIFF=0;	 //Diff of actual closing stock and calculated closing stock 			
			String L_strCATDS1;		
			String strSQLQRY;
			ResultSet rstRSSET;
			strSQLQRY =" Select DS_GRPCD,isnull(DS_MNOQT,0) DS_MNOQT,isnull(DS_MNRQT,0) DS_MNRQT,isnull(DS_PSCQT,0) DS_PSCQT,isnull(DS_SPCQT,0) DS_SPCQT,";
			strSQLQRY+=" isnull(DS_SLRQT,0) DS_SLRQT,isnull(DS_JBRQT,0) DS_JBRQT,isnull(DS_DMDQT,0) DS_DMDQT,isnull(DS_EXDQT,0) DS_EXDQT,isnull(DS_DEDQT,0) DS_DEDQT,";
			strSQLQRY+=" isnull(DS_SRDQT,0) DS_SRDQT,isnull(DS_SREQT,0) DS_SREQT,isnull(DS_SPDQT,0) DS_SPDQT,isnull(DS_CJBQT,0) DS_CJBQT,isnull(DS_SCPQT,0) DS_SCPQT,isnull(DS_MNCQT,0) DS_MNCQT";
			strSQLQRY+=" from FG_DSWRK";
			strSQLQRY+=" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+LP_SSGRPCD+"'";
			strSQLQRY+=" order by DS_GRPCD";			
			rstRSSET=cl_dat.exeSQLQRY1(strSQLQRY);
			//System.out.println(">>>prnTOTAL>>>>>>>>>>>>>>>>>>>>"+strSQLQRY);		
    		prnFMTCHR(D_OUT,M_strBOLD);			
			if(rstRSSET.next() && rstRSSET!=null)		
			{
				L_strCATDS1=getCATDS(rstRSSET.getString("DS_GRPCD"));
				dblACTBG1=rstRSSET.getDouble("DS_MNRQT")-(rstRSSET.getDouble("DS_PSCQT")+rstRSSET.getDouble("DS_SPCQT"));
				dblTTDSP1=rstRSSET.getDouble("DS_DMDQT")+rstRSSET.getDouble("DS_EXDQT")+rstRSSET.getDouble("DS_DEDQT")+rstRSSET.getDouble("DS_SRDQT")+rstRSSET.getDouble("DS_SREQT");
				System.out.println(">>>>>>>>>>>>>>"+rstRSSET.getDouble("DS_DMDQT"));
				System.out.println(">>>>>>>>>>>>>>"+rstRSSET.getDouble("DS_EXDQT"));
				System.out.println(">>>>>>>>>>>>>>"+rstRSSET.getDouble("DS_DEDQT"));
				System.out.println(">>>>>>>>>>>>>>"+rstRSSET.getDouble("DS_SRDQT"));
				System.out.println(">>>>>>>>>>>>>>"+rstRSSET.getDouble("DS_SREQT"));
				System.out.println(">>>>>>>>>>>>>>"+rstRSSET.getDouble("DS_DMDQT")+rstRSSET.getDouble("DS_EXDQT")+rstRSSET.getDouble("DS_DEDQT")+rstRSSET.getDouble("DS_SRDQT")+rstRSSET.getDouble("DS_SREQT"));
				System.out.println(">>>>>>>>>>>>>>"+dblTTDSP1);
				D_OUT.writeBytes(padSTRING('R',"Total "+L_strCATDS1,12));
				//D_OUT.writeBytes(padSTRING('R',rstRSSET.getString("DS_GRPCD"),15));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_MNOQT")),11));			
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_MNRQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_PSCQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SPCQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblACTBG1,3).toString()),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SLRQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_JBRQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_DMDQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_EXDQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_DEDQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SRDQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SREQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblTTDSP1,3).toString()),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SPDQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_CJBQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_SCPQT")),11));
				D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET.getString("DS_MNCQT")),11));

				/**if Closing stock != A+D+E-F-G-H display '*'
				 */ 
				dblCNTCL=rstRSSET.getDouble("DS_MNOQT")+dblACTBG1+rstRSSET.getDouble("DS_SLRQT")+rstRSSET.getDouble("DS_JBRQT")-dblTTDSP1-rstRSSET.getDouble("DS_SPDQT")-rstRSSET.getDouble("DS_CJBQT")-rstRSSET.getDouble("DS_SCPQT");
				dblDIFF=Double.parseDouble(setNumberFormat(dblCNTCL,3))-Double.parseDouble(setNumberFormat(rstRSSET.getDouble("DS_MNCQT"),3));
				// if error > 0.001 or error is less than 0.001 then display that error 
					
				if(dblDIFF != 0)
				{	
					D_OUT.writeBytes(padSTRING('L'," ["+(dblDIFF>0 ? "+" : "")+setNumberFormat(dblDIFF,3),10)+"]");	
				}
		
				/*if(!(dblDIFF < 0.001 && dblDIFF > (-0.001)))
				{	
					//System.out.println(Double.parseDouble(setNumberFormat(dblCNTCL,3))-Double.parseDouble(setNumberFormat(rstRSSET.getDouble("DS_MNCQT"),3)));
					D_OUT.writeBytes(padSTRING('L',"*"+setNumberFormat(dblCNTCL,3),10));	
				}*/

				crtNWLIN();
				/**variables are used for grand total printing
				 */
				dblMNOQT+=rstRSSET.getDouble("DS_MNOQT");
				dblMNRQT+=rstRSSET.getDouble("DS_MNRQT");
				dblPSCQT+=rstRSSET.getDouble("DS_PSCQT");
				dblSPCQT+=rstRSSET.getDouble("DS_SPCQT");
				dblACTBG+=dblACTBG1;
				dblSLRQT+=rstRSSET.getDouble("DS_SLRQT");
				dblJBRQT+=rstRSSET.getDouble("DS_JBRQT");
				dblDMDQT+=rstRSSET.getDouble("DS_DMDQT");
				dblEXDQT+=rstRSSET.getDouble("DS_EXDQT");
				dblDEDQT+=rstRSSET.getDouble("DS_DEDQT");
				dblSRDQT+=rstRSSET.getDouble("DS_SRDQT");
				dblSREQT+=rstRSSET.getDouble("DS_SREQT");
				dblSPDQT+=rstRSSET.getDouble("DS_SPDQT");
				dblCJBQT+=rstRSSET.getDouble("DS_CJBQT");
				dblSCPQT+=rstRSSET.getDouble("DS_SCPQT");
				dblMNCQT+=rstRSSET.getDouble("DS_MNCQT");
				dblTTDSP+=dblTTDSP1;
			}
    		prnFMTCHR(D_OUT,M_strNOBOLD);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnTOTAL()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}	
	
	/** method for printing grand total
	 */
	private void prnGRNDTT()
	{
		try
		{
			double dblCNTCL=0;		//Count for Closing Sock A+E+F+G-H-I-J 
			double dblDIFF=0;	 //Diff of actual closing stock and calculated closing stock 			
    		prnFMTCHR(D_OUT,M_strBOLD);			
			D_OUT.writeBytes(padSTRING('R',"Grand Total ",12));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblMNOQT,3).toString()),11));			
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblMNRQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblPSCQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblSPCQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblACTBG,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblSLRQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblJBRQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblDMDQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblEXDQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblDEDQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblSRDQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblSREQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblTTDSP,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblSPDQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblCJBQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblSCPQT,3).toString()),11));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(dblMNCQT,3).toString()),11));

			/**if Closing stock != A+D+E-F-G-H display '*'
			 */
			dblCNTCL=dblMNOQT+dblACTBG+dblSLRQT+dblJBRQT-dblTTDSP-dblSPDQT-dblCJBQT-dblSCPQT;
			dblDIFF=Double.parseDouble(setNumberFormat(dblCNTCL,3))-Double.parseDouble(setNumberFormat(dblMNCQT,3));
			// if error > 0.001 or error is less than 0.001 then display that error 
			
			if(!(dblDIFF < 0.001 && dblDIFF > (-0.001)))
			{	
				D_OUT.writeBytes(padSTRING('L'," ["+(dblDIFF>0 ? "+" : "")+setNumberFormat(dblDIFF,3),10)+"]");	
			}

			/*if(!(dblDIFF < 0.001 && dblDIFF > (-0.001)))
			{	
				D_OUT.writeBytes(padSTRING('L',"*"+setNumberFormat(dblCNTCL,3),10));	
					
			}*/
			
    		prnFMTCHR(D_OUT,M_strNOBOLD);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRNDTT()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

	/** if argument string ="0.000" reurns "-"
	 */
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
				D_OUT.writeBytes("<HTML><HEAD><Title>Overall Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 7 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,74));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",115));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
			if(rdbDAILY.isSelected())
				D_OUT.writeBytes(padSTRING('R',"Bagging, Despatch & Stock For the day as on "+fmtYYYYMMDD.format(datLOCDT)+" 7:00 hrs",74));
			else 
				D_OUT.writeBytes(padSTRING('R',"Bagging, Despatch & Stock For the Month as on "+fmtYYYYMMDD.format(datLOCDT)+" 7:00 hrs",74));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",115));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		crtNWLIN();
			
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			//											 (A)		(B)			(C)		(D)			(E)			(F)		   (G)																(H)			(I)		   (J)			  (K)							
			D_OUT.writeBytes("Category        Opening    Bagging    Captive    Captive     Actual      Sales        SCP   <---------Despatches--------->   Despatch   Despatch      TOTAL   Transfer    Captive        SCP    Closing"); crtNWLIN();
			D_OUT.writeBytes("                  Stock During the      to PS     to SPS    Bagging     Return   Receipts   Domestic     Export     Deemed   Domestic     Export DESPATCHES     to SPS         To   Consumpn      Stock"); crtNWLIN();
			D_OUT.writeBytes("                            Period                                                                                           from SLR   from SLR                          JobWork                      ");
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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
	
	/** generates report footer
	 */
	void genRPFTR()
	{
		try
		{
			//initialising variables for next print
			cl_dat.M_intLINNO_pbst = 0;	
			dblMNOQT=0;dblMNRQT=0;dblPSCQT=0;dblSPCQT=0;dblACTBG=0;dblSLRQT=0;dblJBRQT=0;dblDMDQT=0;
			dblEXDQT=0;dblDEDQT=0;dblSRDQT=0;dblSREQT=0;dblSPDQT=0;dblCJBQT=0;dblSCPQT=0;dblMNCQT=0;dblTTDSP=0;
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
			updRPAVAIL("Yes");  // make available report for other users
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
			//if report is already in use then others cant use the report till that user releases that lock.
			M_strSQLQRY =" Select CMT_CHP01 from CO_CDTRN where";
			M_strSQLQRY+=" CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXREF' and CMT_CODCD='RPBDS'";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET!= null)
			{
				if(M_rstRSSET.getString("CMT_CHP01").equalsIgnoreCase("No"))
					{JOptionPane.showMessageDialog(null, "Report in use.. Please try later ..." , "Message",JOptionPane.INFORMATION_MESSAGE);cl_dat.M_btnSAVE_pbst.setEnabled(true); return;}
			}	
			updRPAVAIL("No");  // User Locks the report from others to use
			cl_dat.M_flgLCUPD_pbst = true;			
			M_strSQLQRY = "delete from fg_dswrk";
			//System.out.println(">>>M_strSQLQRY>>>"+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			if(cl_dat.exeDBCMT("exeUPDTBLS"))
				setMSG("Generating Report ... ",'N');
			exeUPDTBLS(4);
			exeUPDTBLS(2);
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "fg_rpbds.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "fg_rpbds.doc";
				
			genRPTFL();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				}	
				else 
			    {    
					Runtime r = Runtime.getRuntime();
					Process p = null;					    
					p  = r.exec("doPRINT(strRPFNM)"); 
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
			/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}*/
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		

	
	}

	/** Locks Or Unlocks the report for other users
	 */
	private void updRPAVAIL(String LP_LOCK)
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			M_strSQLQRY =" Update CO_CDTRN set CMT_CHP01 = '"+LP_LOCK+"'";
			M_strSQLQRY+=" where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXREF' and CMT_CODCD='RPBDS'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");				
			cl_dat.exeDBCMT("exeUPDTBLS");
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"updRPAVAIL");
		}
	}	
	
	/** checks for DS_GRPCD present in the table or not
	 *	if present Update record otherwise insert record
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
	

	/**	MNRQT Down grading (to be transferred to PSFS -  from '5111' to '5197',  '5112' to '5297')
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
	 *   inserting or updating of records in the FG_DSWRK table
	 *	 if LP_GRPLN=4 ==> 4 characters of code are taken
	 *	 if LP_GRPLN=2 ==> first 2 characters of code are taken
	 *		for total of "51##" LP_GRPLN=2 and total stored as "5100"	
	 */
	public void exeUPDTBLS(int LP_GRPLN)
	{
		try
		{
			intGRPLN = LP_GRPLN;
			
			//MNOQT
			int L_intDSPQRY = 1;
			setMSG("Fetching Opening Stock",'N');
			M_strSQLQRY =" ";
			if(rdbMONTH.isSelected())
				M_strSQLQRY="Select SUBSTRING(op_prdcd,1,"+LP_GRPLN+") ds_grpcd , sum(isnull(op_mosqt,0)+isnull(op_mouqt,0)) ds_xxxqt from fg_opstk where OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(op_mosqt,0)+isnull(op_mouqt,0))>0 group by SUBSTRING(op_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(op_prdcd,1,"+LP_GRPLN+")";
			else
				M_strSQLQRY="Select SUBSTRING(st_prdcd,1,"+LP_GRPLN+") ds_grpcd , sum(isnull(st_dosqt,0)+isnull(st_douqt,0)) ds_xxxqt from fg_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  (isnull(st_dosqt,0)+isnull(st_douqt,0))>0 group by SUBSTRING(st_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(st_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_MNOQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_MNOQT");
			
			//MNRQT
			setMSG("Fetching Bagging During The Period",'N');
			M_strSQLQRY =" select SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(rct_rctqt) ds_xxxqt ";
			M_strSQLQRY+=" from fg_rctrn,pr_ltmst";
			M_strSQLQRY+=" where  isnull(rct_rctqt,0)>0 and rct_cmpcd = lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_rclno = lt_rclno and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp in ('"+strRCTTP_FRESH+"','"+strRCTTP_REBAGG+"') and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and rct_stsfl='2' and rct_rctqt>0";
			M_strSQLQRY+=" group by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_MNRQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_MNRQT");
			
			//Captive Receipt  PS
			setMSG("Fetching Captive to PS",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  isnull(ivt_invqt,0)>0 and  ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_PS+"'  and SUBSTRING(ivt_prdcd,1,2) = '"+strPRDTP_PS+"' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0  group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_strSQLQRY+=" order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_PSCQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_PSCQT");

			//Captive Receipt  SPS
			setMSG("Fetching Captive Receipt  SPS",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_SPS+"'  and SUBSTRING(ivt_prdcd,1,2) in ('"+strPRDTP_SPS+"','"+strPRDTP_MB+"')  and IVT_PRDCD <> '5211951680' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_SPCQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SPCQT");

			//SLRQT			
			setMSG("Fetching Captive Sales Return",'N');
			M_strSQLQRY =" select SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(rct_rctqt) ds_xxxqt";
			M_strSQLQRY+=" from fg_rctrn, pr_ltmst";
			M_strSQLQRY+=" where isnull(rct_rctqt,0)>0 and  rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_rclno = lt_rclno and rct_cmpcd = lt_cmpcd and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp in ('"+strRCTTP_SRTN+"') and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and rct_stsfl='2' and rct_rctqt>0";
			M_strSQLQRY+=" group by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_SLRQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SLRQT");
			
			//DMDQT      Domestic despatch
			setMSG("Fetching Domestic Despatch",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp in ('"+strSALTP_DOM+"', '"+strSALTP_STKTR+"','"+strSALTP_FTS+"') and ivt_slrfl='1' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_DMDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_DMDQT");
			
			//EXDQT    Export Despatch
			setMSG("Fetching Export Despatch",'N');
  			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt ";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and ivt_saltp='"+strSALTP_EXP+"'  and ivt_slrfl='1'  and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_EXDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_EXDQT");

			
		    //DEDQT   Deemed Export Despatch
			setMSG("Fetching Deemed Export Despatch",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt"; 
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and  ivt_saltp='"+strSALTP_DEXP+"'   and ivt_slrfl='1'  and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";   
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";  
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_DEDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_DEDQT");
			
			
			//SRDQT   Domestic Despatches from Sales Return
			setMSG("Fetching Domestic Despatches from Sales Return",'N');
  			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp ='"+strSALTP_DOM+"'  and IVT_SLRFL = '2' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_SRDQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SRDQT");

			//SREQT
			setMSG("Fetching Export Despatches from Sales Return",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  isnull(ivt_invqt,0)>0 and  ivt_saltp = '"+strSALTP_EXP+"'  and IVT_SLRFL = '2' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_SREQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SREQT");

			//Job Work Receipt;
			setMSG("Fetching Job Work Receipt",'N');
			M_strSQLQRY =" select SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(rct_rctqt) ds_xxxqt"; 
			M_strSQLQRY+=" from fg_rctrn,pr_ltmst";
			M_strSQLQRY+=" where isnull(rct_rctqt,0)>0 and   rct_prdtp=lt_prdtp and rct_lotno = lt_lotno and rct_cmpcd = lt_cmpcd and rct_rclno = lt_rclno and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp = '"+strRCTTP_JBWRK+"' and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and rct_stsfl='2' and rct_rctqt>0";   
			M_strSQLQRY+=" group by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(lt_prdcd,1,"+LP_GRPLN+")";  
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>>DS_JBRQT>>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_JBRQT");
			
		    //SCP used by SPS	
			setMSG("SCP used by SPS",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt "; 
			M_strSQLQRY+=" from mr_ivtrn ";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_SPS+"'    and IVT_PRDCD = '5211951680' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' ";   
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";  
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>>DS_SCPQT>>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SCPQT");
						
		    //PS to SPS transfer
			setMSG("PS to SPS transfer",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt"; 
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_SPS+"'  and SUBSTRING(ivt_prdcd,1,2) = '"+strPRDTP_PS+"' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";   
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";  
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>>DS_SPDQT>>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_SPDQT");
			
		    //Captive to Job Work
			setMSG("Captive to Job Work",'N');
			M_strSQLQRY =" select SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ivt_invqt) ds_xxxqt";
			M_strSQLQRY+=" from mr_ivtrn";
			M_strSQLQRY+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ivt_invqt,0)>0 and   ivt_saltp='"+strSALTP_CAPT+"'   and ivt_byrcd = '"+strPRTCD_JB+"'  and SUBSTRING(ivt_prdcd,1,2) in ('"+strPRDTP_PS+"','"+strPRDTP_SPS+"','"+strPRDTP_MB+"')  and IVT_PRDCD <> '5211951680' and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ivt_invqt>0";
			M_strSQLQRY+=" group by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ivt_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>>DS_CJBQT>>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_CJBQT");
			//MNCQT
			setMSG("Fetching Closing Stock",'N');
			M_strSQLQRY =" Select SUBSTRING(st_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(isnull(st_dosqt,0)+isnull(st_douqt,0)) ds_xxxqt ";
			M_strSQLQRY+=" from fg_stmst";
			M_strSQLQRY+=" where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(st_dosqt,0)+isnull(st_douqt,0))>0 ";
			M_strSQLQRY+=" group by SUBSTRING(st_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(st_prdcd,1,"+LP_GRPLN+")";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(LP_GRPLN == L_intDSPQRY)
				System.out.println(">>>> DS_MNCQT >>>>"+M_strSQLQRY);
			putFGDSWRK(M_rstRSSET,"DS_MNCQT");

			
			//MNRQT Down grading (to be transferred to PSFS -  from '5111' to '5197',  '5112' to '5297')
			if(LP_GRPLN==4)
			{
				M_strSQLQRY =" select SUBSTRING(ist_prdcd,1,"+LP_GRPLN+") ds_grpcd ,sum(ist_issqt) ds_xxxqt"; 
				M_strSQLQRY+=" from fg_istrn";  
				M_strSQLQRY+=" where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND   ist_isstp = '"+strISSTP_DNGRDG+"' and ist_autdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strENDDT))+"' and ist_stsfl='2' and ist_issqt>0";
				M_strSQLQRY+=" group by SUBSTRING(ist_prdcd,1,"+LP_GRPLN+") order by SUBSTRING(ist_prdcd,1,"+LP_GRPLN+")";   
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				if(LP_GRPLN == L_intDSPQRY)
					System.out.println(">>>> DS_MNRQT (DGR) >>>>"+M_strSQLQRY);
				putFGDSWRK_DGR(M_rstRSSET);
			}
		
			cl_dat.exeDBCMT("exeSAVE");
		}	
		catch(Exception L_E)
		{
			setMSG(L_E,"exeUPDTBLS()");
		}
	}	
	
	/** returns count of the lp_grpcd in to the FG_DSWRK
	 */
	public int getCOUNT(String lp_grpcd)
	{
		try
		{
			String strSQLQRY;
			ResultSet rstRSSET;
		
			strSQLQRY =" Select count(*) CNT";
			strSQLQRY+=" from FG_DSWRK";strSQLQRY =" Select count(*) CNT";
			strSQLQRY+=" from FG_DSWRK";
			strSQLQRY+=" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+lp_grpcd+(intGRPLN==2?"00":"")+"'";
			rstRSSET=cl_dat.exeSQLQRY1(strSQLQRY);
			//System.out.println(">>>>strSQLQRY>>>>"+strSQLQRY);
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
	
	/**	insert records in to the FG_DSWRK
	 *	if intGRPLN==2 appends "00" to lp_grpcd coz 5100 is total for all 51** and so on.
	 */
	public void insSTRING(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			
			strSQLQRY1 =" Insert into FG_DSWRK(DS_GRPCD,"+lp_fldnm+") values(";
			strSQLQRY1+="'"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"',"; 
			strSQLQRY1+= lp_valqt+")";
			//System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");							
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"insSTRING()");
		}
	}	


	
	/**	insert records in to the FG_DSWRK for MNRQT Down grading
	 *	if intGRPLN==2 appends "00" to lp_grpcd coz 5100 is total for all 51** and so on.
	 */
	public void insSTRING_DGR(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			
			strSQLQRY1 =" Insert into FG_DSWRK(DS_GRPCD,"+lp_fldnm+") values(";
			strSQLQRY1+="'"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"',"; 
			strSQLQRY1+= lp_valqt+"*(-1))";
			//System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");							
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"insSTRING()");
		}
	}	
	
	/**	updates records in to the FG_DSWRK
	 *	if intGRPLN==2 appends "00" to lp_grpcd coz 5100 is total for all 51** and so on.
	 */
	public void updSTRING(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			strSQLQRY1 =" Update FG_DSWRK set  "+lp_fldnm+"=isnull("+lp_fldnm+",0)+"+lp_valqt+" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"'";	
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");				
			//System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"updSTRING()");
		}
	}	

	/**	update records in FG_DSWRK for MNRQT Down grading
	 *	if intGRPLN==2  it appends "00" to lp_grpcd coz 5100 is total for all 51** and so on.
	 */
	public void updSTRING_DGR(String lp_grpcd,String lp_valqt,String lp_fldnm)
	{
		try
		{
			String strSQLQRY1;
			strSQLQRY1 =" Update FG_DSWRK set  "+lp_fldnm+"=isnull("+lp_fldnm+",0)-"+lp_valqt+" where DS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DS_GRPCD='"+lp_grpcd+(intGRPLN==2 ? "00" : "")+"'";	
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");				
			//System.out.println(">>>>strSQLQRY1>>>>"+strSQLQRY1);
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



