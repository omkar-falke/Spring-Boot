/*
	Name			: Indent Printing
	System			: MKT
	Author			: AAP
	Version			: v2.0.0
	Last Modified	: 20/05/2004
	Documented On	: 20/05/2004
*/

import javax.swing.*;
import java.io.File;import java.io.FileOutputStream;import java.io.DataOutputStream;import java.io.IOException;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.Color;import java.awt.Component;
import java.util.Vector;import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
import java.sql.ResultSet;
/***/
class fg_rpvhd extends cl_rbase 
{
	private String strDSTNM;/**	Flag to indicate that, data is being written in table format in HTML */
	private boolean flgTBLDT;/**	Global string for default text color in HTML */
	private Process prcREPORT;/** Line counter */
	private int intLINCT;/**Global string for page break in HTML	 */
	private String strPGBRK="<P CLASS = \"breakhere\"></PRE>";/**DATA OUTPUT STEARM TO WRITE TO FILE	 */
	//private String strRPTFL = cl_dat.M_strREPSTR_pbst+txtINDNO.getText();
	private String strRPTFL = "";
    private String strRESSTR = cl_dat.M_strREPSTR_pbst+"fg_rpvhd.doc"; 
	private FileOutputStream fosREPORT;
    private DataOutputStream dosREPORT;
	
	private final String strISO1_fn="ISO DOC.NO. : SPL/MKT/QR/014",strISO2_fn="ISSUE NO./DATE : 01/01-11-99",strISO3_fn="REV.NO./DATE : 00"; 

	private JRadioButton rdbDEPT_MHD;
	private JRadioButton rdbDEPT_COM;	
	private JRadioButton rdbDEPT_ALL;	
	
	private JRadioButton rdbTRGT_WTH;
	private JRadioButton rdbTRGT_BND;	
	private JRadioButton rdbTRGT_ALL;	

	
	private JRadioButton rdbSAL_DOM;	
	private JRadioButton rdbSAL_EXP;	
	private JRadioButton rdbSAL_ALL;	

	private JRadioButton rdbSUMRPT;
	private JRadioButton rdbDETRPT;	
	
	
	private boolean flgRPTCHK = true;
	
	int intTOTCT0 = 0;
	int intTOTCT1 = 0;
	String strRPTLINE = "";
	String strHDRDPT = "";
	String strWHRDPT = "";
	String strHDRSAL = "";
	String strWHRSAL = "";
	String strHDRTRGT = "";
	String strWHRTRGT = "";

	String strWHRSCOP = "";
	String strWHRSTR0 = "";	
	String strWHRSTR1 = "";
											/** Array elements for Doc.wise Specificv Tax detail */
	/**Constructor for printing from report menu */
	fg_rpvhd()
	{
		super(2);
		try
		{
			M_pnlRPFMT.setVisible(true);
			M_rdbTEXT.setSelected(true);

			add(new JLabel("DEPARTMENT :"),4,2,1,2,this,'L');
			add(rdbDEPT_MHD=new JRadioButton("MHD"),5,2,1,1,this,'L');
			add(rdbDEPT_COM=new JRadioButton("Comm."),6,2,1,1,this,'L');
			add(rdbDEPT_ALL=new JRadioButton("Overall"),7,2,1,1,this,'L');
			ButtonGroup btgDEPT=new ButtonGroup();
			btgDEPT.add(rdbDEPT_MHD); btgDEPT.add(rdbDEPT_COM); btgDEPT.add(rdbDEPT_ALL);

			add(new JLabel("SALE TYPE :"),4,4,1,2,this,'L');
			add(rdbSAL_DOM=new JRadioButton("Domestic"),5,4,1,1,this,'L');
			add(rdbSAL_EXP=new JRadioButton("Export"),6,4,1,1,this,'L');
			add(rdbSAL_ALL=new JRadioButton("Overall"),7,4,1,1,this,'L');
			ButtonGroup btgSAL=new ButtonGroup();
			btgSAL.add(rdbSAL_DOM); btgSAL.add(rdbSAL_EXP); btgSAL.add(rdbSAL_ALL);

			
			add(new JLabel("TARGET SCOPE :"),4,6,1,2,this,'L');
			add(rdbTRGT_WTH=new JRadioButton("Within"),5,6,1,1,this,'L');
			add(rdbTRGT_BND=new JRadioButton("Beyond"),6,6,1,1,this,'L');
			add(rdbTRGT_ALL=new JRadioButton("Overall"),7,6,1,1,this,'L');
			ButtonGroup btgTRGT=new ButtonGroup();
			btgTRGT.add(rdbTRGT_WTH); btgTRGT.add(rdbTRGT_BND); btgTRGT.add(rdbTRGT_ALL);

			
			add(new JLabel("REPORT TYPE :"),4,8,1,2,this,'L');
			add(rdbSUMRPT=new JRadioButton("Summary"),5,8,1,1,this,'L');
			add(rdbDETRPT=new JRadioButton("Detail"),6,8,1,1,this,'L');
			ButtonGroup btgRPT=new ButtonGroup();
			btgRPT.add(rdbSUMRPT); btgRPT.add(rdbDETRPT);
			
			
		}catch(Exception e)
		{setMSG(e,"Child.constructor 2");}
	}
	
	/**<b> TASKS</b><br>
	 * Source = txtINDNO : HELP of available indent no
	 * Source = cmbOPTN : Transfer focus to txtINDNO */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
	}
	
	/**<b> TASKS</b><br>
	 * Source = cmbOPTN : Transfer focus to txtINDNO */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
	}
	public void exeHLPOK()
	{
		cl_dat.M_flgHELPFL_pbst = false;
		super.exeHLPOK();
	}
	/**To format and create the report	 */
	void exePRINT()
	{
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		try
		{
		    if(M_rdbHTML.isSelected())
		    {
		       
		    
		        strRESSTR = cl_dat.M_strREPSTR_pbst +"fg_rpvhd.html";
		    }  
		    else if(M_rdbTEXT.isSelected())
				strRESSTR = cl_dat.M_strREPSTR_pbst + "fg_rpvhd.doc";	 
			getDATA();
			if(!flgRPTCHK)
				return;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strRESSTR);
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strRESSTR); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strRESSTR); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strRESSTR);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRESSTR,"Gate Pass Reports"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
						
			
		}catch(Exception e){setMSG(e,"Child.exePRINT");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}


	/**
	 */
	private void getDATA()
	{
		try
		{
			fosREPORT = new FileOutputStream(strRESSTR);
			dosREPORT = new DataOutputStream(fosREPORT);
			 if(M_rdbHTML.isSelected())
			    {
			        
			        dosREPORT.writeBytes("<HTML><HEAD><Title>Vehicle Detention Summary</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
			        dosREPORT.writeBytes("</STYLE>");
				 }
				 else
					prnFMTCHR(dosREPORT,M_strCPI17);
			 
			String L_strSTRTM = "IVT_LADDT";
			String L_strENDTM = "IVT_INVDT";
			String L_strTRGTM = "3.00";
			
			String L_strTRGTMHD_D = "2.30";
			String L_strTRGTMHD_E = "1.45";
			String L_strTRGTMHD = "";
				
			String L_strTRGTCOM_D = "0.30";
			String L_strTRGTCOM_E = "0.30";
			String L_strTRGTCOM = "";

			strWHRSAL = "";
			if(rdbSAL_DOM.isSelected())
				{strWHRSAL = " ivt_saltp not in ('04','16','21','12')"; L_strTRGTMHD = L_strTRGTMHD_D; L_strTRGTCOM = L_strTRGTCOM_D; strHDRSAL = "Domestic";}
			else if(rdbSAL_EXP.isSelected())
				{strWHRSAL = " ivt_saltp = '12'"; L_strTRGTMHD = L_strTRGTMHD_E; L_strTRGTCOM = L_strTRGTCOM_E; strHDRSAL = "Export";}
			else if(rdbSAL_ALL.isSelected())
				{strWHRSAL = " ivt_saltp not in ('04','16','21')"; strHDRSAL = "Overall";}
			

			strWHRTRGT = "";
			if(rdbDEPT_MHD.isSelected())
				{strWHRDPT = " and iv1_detcd < '70'"; L_strSTRTM = "IVT_LADDT"; L_strTRGTM = L_strTRGTMHD; L_strENDTM = "IVT_LODDT"; strHDRDPT = "M.H.D.";}
			else if(rdbDEPT_COM.isSelected())
				{strWHRDPT = " and iv1_detcd > '70'";  L_strSTRTM = "IVT_LODDT"; L_strTRGTM = L_strTRGTCOM; L_strENDTM = "IVT_INVDT"; strHDRDPT = "Commercial";}
			else if(rdbDEPT_ALL.isSelected())
				{strWHRDPT = ""; strHDRDPT = "Overall";}


			strWHRTRGT = "";
			if(rdbTRGT_WTH.isSelected())
				{strWHRTRGT = "("+L_strENDTM+" - "+ L_strSTRTM + ")/10000 <= " + L_strTRGTM; strHDRTRGT = "Within Taget";}
			else if(rdbTRGT_BND.isSelected())
				{strWHRTRGT = "("+L_strENDTM+" - "+ L_strSTRTM + ")/10000 > " + L_strTRGTM; strHDRTRGT = "Beyond Target";}
			else if(rdbTRGT_ALL.isSelected())
				{strWHRTRGT = ""; strHDRTRGT = "Overall";}

			if(strWHRSAL.length()>2 || strWHRTRGT.length()>2)
				strWHRSCOP = " and wb_docno in (select ivt_ginno from mr_ivtrn where "+strWHRSAL+((strWHRSAL.length()>2 && strWHRTRGT.length()>2) ? " and " : "")+strWHRTRGT+"  and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_stsfl<>'X' and length(isnull(ivt_invno,''))=8)";

			System.out.println("L_strTRGTCOM :"+L_strTRGTCOM);
			System.out.println("L_strTRGTMHD :"+L_strTRGTMHD);
			System.out.println("L_strTRGTM :"+L_strTRGTM);
			
			
			strWHRSTR0 = " CONVERT(varchar,wb_gindt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' and wb_doctp='03' and wb_stsfl<>'X'"+strWHRSCOP;
			strWHRSTR1 = " iv1_ginno in (select wb_docno from mm_wbtrn where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR0+")"+strWHRDPT;

			if(rdbSUMRPT.isSelected())		
				getDATA_SUM();
			else if(rdbDETRPT.isSelected())
				getDATA_DET();
			
			dosREPORT.close();
			fosREPORT.close();
		}
		catch(Exception e){setMSG(e,"getDATA");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}

	/** Summary Report
	 */
	private void getDATA_SUM()
	{
		try
		{
			flgRPTCHK = true;
			M_strSQLQRY = "select count(distinct wb_docno) wb_docct from mm_wbtrn where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR0;
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			intTOTCT0 = 0;
			if(M_rstRSSET.next())
				intTOTCT0 = Integer.parseInt(getRSTVAL(M_rstRSSET,"WB_DOCCT","N"));
			if(intTOTCT0 == 0)
				{flgRPTCHK = false; JOptionPane.showMessageDialog(this,"No Records Found");return;}

			
			M_strSQLQRY = "select count(distinct iv1_ginno) iv1_totct from fg_ivtr1 where IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR1;
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			intTOTCT1 = 0;
			if(M_rstRSSET.next())
				intTOTCT1 = Integer.parseInt(getRSTVAL(M_rstRSSET,"IV1_TOTCT","N"));
			if(intTOTCT1 == 0)
				{flgRPTCHK = false; JOptionPane.showMessageDialog(this,"No Reasons Defined");return;}

			M_strSQLQRY = "select iv1_detcd,cmt_codds,count(iv1_ginno) iv1_ginct from fg_ivtr1,co_cdtrn  where IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+strWHRSTR1+" and cmt_cgmtp='SYS' and cmt_cgstp='FGXXVHD' and cmt_codcd = iv1_detcd group by iv1_detcd,cmt_codds";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
				{flgRPTCHK = false; JOptionPane.showMessageDialog(this,"No Data found");return;}


			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,40)+padSTRING('L',"Date : "+cl_dat.M_txtCLKDT_pbst.getText(),40)+"\n\n");
			dosREPORT.writeBytes(padSTRING('C',"V E H I C L E   D E T E N T I O N   S U M M A R Y",80)+"\n\n");
			
			dosREPORT.writeBytes(padSTRING('C',"Period :  From : "+M_txtFMDAT.getText()+" To : "+M_txtTODAT.getText(),80)+"\n\n");
			dosREPORT.writeBytes(padSTRING('R',"Department :"+strHDRDPT+ "   Sale Type :"+strHDRSAL+"    Target Scope :"+strHDRTRGT,80)+"\n\n");
			
			dosREPORT.writeBytes(padSTRING('R',"Overall No. of Vehicles Despatched : "+String.valueOf(intTOTCT0),80)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Total No. of Vehicles under Detention : "+String.valueOf(intTOTCT1),80)+"\n");
			strRPTLINE = 
			padSTRING('R',"--------------------------------------------------",50)
			+"  "+padSTRING('L',"--------",10)
			+"  "+padSTRING('L',"--------",10)
			+"  "+padSTRING('L',"--------",10);						

			dosREPORT.writeBytes(strRPTLINE+" \n");
			dosREPORT.writeBytes(
			padSTRING('R',"Reason for Detention",50)
			+"  "+padSTRING('L',"No.of",10)
			+"  "+padSTRING('L',"Detention",10)
			+"  "+padSTRING('L',"Overall",10)+"  \n");
			dosREPORT.writeBytes(
			padSTRING('R',"",50)
			+"  "+padSTRING('L',"Vehicles",10)
			+"  "+padSTRING('L',"%",10)
			+"  "+padSTRING('L',"%",10)+"  \n");
			dosREPORT.writeBytes(strRPTLINE+" \n");
			
			int L_intGINCT = 0;
			while(true)
			{
				//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),13));
				L_intGINCT = Integer.parseInt(getRSTVAL(M_rstRSSET,"IV1_GINCT","C"));
				String L_strVHDPR0 = setNumberFormat((L_intGINCT*100)/intTOTCT0,0);
				String L_strVHDPR1 = setNumberFormat((L_intGINCT*100)/intTOTCT1,0);
				dosREPORT.writeBytes(
				padSTRING('R',getRSTVAL(M_rstRSSET,"CMT_CODDS","C"),50)
				+"  "+padSTRING('L',String.valueOf(L_intGINCT),10)
				+"  "+padSTRING('L',L_strVHDPR1+" %",10)
				+"  "+padSTRING('L',L_strVHDPR0+" %",10)+"  \n");
				//System.out.println(getRSTVAL(M_rstRSSET,"CMT_CODDS","C")+"  "+getRSTVAL(M_rstRSSET,"IV1_GINCT","C"));
				if(!M_rstRSSET.next())
					break;
			}
			dosREPORT.writeBytes(strRPTLINE+" \n");
			prnFMTCHR(dosREPORT,M_strEJT);
			M_rstRSSET.close();
		}
		catch(Exception e){setMSG(e,"getDATA");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	

	/** Summary Report
	 */
	private void getDATA_DET()
	{
		try
		{
			flgRPTCHK = true;

			M_strSQLQRY = "select distinct ivt_lryno,ivt_ladno,ivt_laddt,ivt_loddt,(ivt_loddt-ivt_laddt)/10000 ivt_diff1,ivt_invdt,(ivt_invdt-ivt_loddt)/10000 ivt_diff2,iv1_detcd,cmt_codds from fg_ivtr1,co_cdtrn,mr_ivtrn  where "+strWHRSTR1+" and IV1_CMPCD = IV1_CMPCD and IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv1_ginno=ivt_ginno and cmt_cgmtp='SYS' and cmt_cgstp='FGXXVHD' and cmt_codcd = iv1_detcd order by ivt_ladno";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
				{flgRPTCHK = false; JOptionPane.showMessageDialog(this,"No Data found");return;}


			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,70)+padSTRING('L',"Date : "+cl_dat.M_txtCLKDT_pbst.getText(),70)+"\n\n");
			dosREPORT.writeBytes(padSTRING('C',"V E H I C L E   D E T E N T I O N   D E T A I L",140)+"\n\n");
			
			dosREPORT.writeBytes(padSTRING('C',"Period :  From : "+M_txtFMDAT.getText()+" To : "+M_txtTODAT.getText(),140)+"\n\n");
			dosREPORT.writeBytes(padSTRING('R',"Department :"+strHDRDPT+ "   Sale Type :"+strHDRSAL+"    Target Scope :"+strHDRTRGT,140)+"\n\n");
			
			strRPTLINE = 
			padSTRING('R',"--------------------------------------------------",50)
			+"  "+padSTRING('L',"--------",10)
			+"  "+padSTRING('L',"--------",10)
			+"  "+padSTRING('L',"--------",16)
			+"  "+padSTRING('L',"--------",16)
			+"  "+padSTRING('L',"----",6)
			+"  "+padSTRING('L',"--------",16)						
			+"  "+padSTRING('L',"----",6);

			dosREPORT.writeBytes(strRPTLINE+" \n");
			dosREPORT.writeBytes(
			padSTRING('R',"Reason for Detention",50)
			+"  "+padSTRING('L',"Lorry No.",10)
			+"  "+padSTRING('L',"LA No.",10)
			+"  "+padSTRING('L',"L.A.Time",16)
			+"  "+padSTRING('L',"Load.Time",16)
			+"  "+padSTRING('L',"Diff.",6)
			+"  "+padSTRING('L',"Inv.Time",16)
			+"  "+padSTRING('L',"Diff.",6)+"  \n");
			dosREPORT.writeBytes(strRPTLINE+" \n");
			
			int L_intGINCT = 0;
			while(true)
			{
				//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),13));
				dosREPORT.writeBytes(
				padSTRING('R',getRSTVAL(M_rstRSSET,"CMT_CODDS","C"),50)
				+"  "+padSTRING('L',getRSTVAL(M_rstRSSET,"IVT_LRYNO","C"),10)
				+"  "+padSTRING('L',getRSTVAL(M_rstRSSET,"IVT_LADNO","C"),10)
				+"  "+padSTRING('L',getRSTVAL(M_rstRSSET,"IVT_LADDT","T"),16)
				+"  "+padSTRING('L',getRSTVAL(M_rstRSSET,"IVT_LODDT","T"),16)
				+"  "+padSTRING('L',setNumberFormat(Double.parseDouble(getRSTVAL(M_rstRSSET,"IVT_DIFF1","N")),2),6)
				+"  "+padSTRING('L',getRSTVAL(M_rstRSSET,"IVT_INVDT","T"),16)
				+"  "+padSTRING('L',setNumberFormat(Double.parseDouble(getRSTVAL(M_rstRSSET,"IVT_DIFF2","N")),2),6)+"\n");
				//System.out.println(getRSTVAL(M_rstRSSET,"CMT_CODDS","C")+"  "+getRSTVAL(M_rstRSSET,"IV1_GINCT","C"));
				if(!M_rstRSSET.next())
					break;
			}
			dosREPORT.writeBytes(strRPTLINE+" \n");
			prnFMTCHR(dosREPORT,M_strEJT);
			M_rstRSSET.close();
		}
		catch(Exception e){setMSG(e,"getDATA_DET");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	
	void exeSAVE(){}
	
	void clrCOMP()
	{
		super.clrCOMP();
		M_txtFMDAT.setText("01"+cl_dat.M_txtCLKDT_pbst.getText().substring(2,10));
		M_txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText());
		rdbDEPT_ALL.setSelected(true);
		rdbSAL_ALL.setSelected(true);
		rdbTRGT_ALL.setSelected(true);
		rdbSUMRPT.setSelected(true);
		if(prcREPORT!=null)
			prcREPORT.destroy();
	}
	

	
	
	
	
/** Method for returning values from Result Set
 * <br> with respective verifications against various data types
 * @param	LP_RSLSET		Result set name
 * @param       LP_FLDNM                Name of the field for which data is to be extracted
 * @param	LP_FLDTP		Data Type of the field
 */
private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
{
	//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
    try
    {
	if (LP_FLDTP.equals("C"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
		//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
	else if (LP_FLDTP.equals("N"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
	else if (LP_FLDTP.equals("D"))
		return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
	else if (LP_FLDTP.equals("T"))
	    return M_fmtLCDTM.format(LP_RSLSET.getTimestamp(LP_FLDNM));
	else 
		return " ";
	}
	catch (Exception L_EX)
	{setMSG(L_EX,"getRSTVAL");}
return " ";
} 
		
}