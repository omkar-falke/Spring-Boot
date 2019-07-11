/*
System Name   : Material Management System
Program Name  : Bill Clearence Statement.
Program Desc. : This Report gives details about the Tankfarm material received, Shortage, 
				Deducaion amount & Net Amount to pay according to challan Number.
Author        : Mr S.R.Mehesare
Date          : 19.04.2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;
/**<pre>
System Name : Material Management System.
 
Program Name : Bill Clearence Statement.

Purpose : This Report gives Bill passing deatails for transportation.Details about the 
Tankfarm material received, Shortage,Deducaion amount, Net Amount to pay, bill amount & so on.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
MM_BLMST       BL_BILTP, BL_DOCNO                                      #
MM_BILTR       BIL_BILTP,BIL_DOCNO,BIL_DOCRF,BIL_MATCD                 #
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
CO_CTMST       CT_MATCD                                                #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name      Type/Size       Description
--------------------------------------------------------------------------------------
txtDOCNO    BL_DOCNO       MM_BLMST        Varchar(8)      Document Number
cmbBILTP    BL_BILTP       MM_BLMST        VARCHAR(2)      Bill Type
--------------------------------------------------------------------------------------
<B>
For Bill Type:</b>
     Table used  : 1)CO_CDTRN                             
     Conditation : 1) CMT_CGMTP = 'SYS' 
                    2) AND CMT_CGSTP = 'MMXXBTP' 
                    3) AND CMT_CODCD ='03'"
<B>For Document Number:</B>
     Table used  : 1)MM_BLMST                             
     Conditation : 1) BL_BILTP = Selected Bill Type
		           2) And isnull(BL_STSFL,'') <> 'X'";
<b>For Report Body:</b>
     Tables used : 1)MM_BLMST
                   2)MM_BILTR
                   3)MM_GRMST                             
     Conditation : 1) BL_BILTP = BIL_BILTP
                   2) AND BL_DOCNO = BIL_DOCNO
                   3) AND BIL_DOCRF = GR_GRNNO
                   4) AND BL_BILTP = Selected Bill Type
                   5) AND BL_DOCNO = given Valid Document Number 
                   6) AND isnull(BL_STSFL,'') <> 'X'
                   7) AND isnull(BIL_STSFL,'') <> 'X'
<I><B>Validations :</B>
    - Consignment Number must be exists in the DataBase.    
</I> */

public class mm_rptbp extends cl_rbase
{  									/** JTextField to accept & display Consignment Number.*/
	private JTextField txtDOCNO;	/** JComboBOx to display & select Bill Type.*/
	private JComboBox cmbBILTP;		/** String variable for generated report File Name*/	                     	
	private String strFILNM;		/** Integer variable for counting the records fetched from DB.*/	                     
	private int intRECCT=0;			/** String varaible for Store Type Code.*/	                     
	private String strSTRTP;		/** String variable for Document Numebr.*/	                     
	private String strDOCNO;		/** String variable for rate per Metric Ton.*/	                     
	private String strRTPMT;		/** String variable for location Descrition.*/
	private String strLOCDS;		/** String variable for Bill Date.*/
	private String strBILDT;		/** String variable for Vendor Type.*/
	private String strVENTP="T";	/** String variable for Transporter Code.*/
	private String strTRNCD;		/** String variable for Bill Number.*/
	private String strBILNO;		/** String variable for Transporter Name.*/
	private String strTRNNM;		/** String variable for Bill Type.*/
									/** Hash Table to Store number & its corresponding string repretation of that number.*/
	private Hashtable<String,String> hst1to100;	/** Integer variable for Serial number.*/
	private int intSRLNO;			/** Charector variable for Deducation Type*/
	private char chrDEDTP;				/** FileOutputStream for File Handling.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream to hold data for generating Report File.*/
    private DataOutputStream dosREPORT;	/** ResultSet Object for Database I/O Functions.*/
    private ResultSet M_rstRSSET1;   	/** Hash Table to hold Excise Category Code, Description & Location Code, Description.*/
	mm_rptbp()
	{
	    super(2);
	    try
	    {	
			setCursor(cl_dat.M_curWTSTS_pbst);	
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Bill Type"),4,4,1,1,this,'L');
			add(cmbBILTP= new JComboBox(),4,5,1,2,this,'L');			
			add(new JLabel("SPL/DOC No."),5,4,1,1,this,'L');
			add(txtDOCNO= new TxtNumLimit(8),5,5,1,1,this,'L');								
			M_pnlRPFMT.setVisible(true);			
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXBTP' and CMT_CODCD ='03' and isnull(cmt_stsfl,'') <>'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);						
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{								
					cmbBILTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}			
			setCursor(cl_dat.M_curDFSTS_pbst);	
	 		setENBL(true);
			txtDOCNO.setEnabled(false);
			cmbBILTP.setEnabled(false);
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX+" GUI Designing",'E');
		}	
    }
	mm_rptbp(String P_strSBSCD)
	{
		//M_strSBSCD = P_strSBDCD;
		M_rdbTEXT.setSelected(true);
		M_rdbHTML.setSelected(false);
		strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rptbp.doc";
		crtHSTBL();
	}
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtDOCNO.setEnabled(L_flgSTAT);
		cmbBILTP.setEnabled(L_flgSTAT);		
		if (L_flgSTAT==false)
		{			
			txtDOCNO.setText("");			
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE); 
	    if(M_objSOURC == txtDOCNO)
		{			
			if((txtDOCNO.getText().trim()).length()== 8)
			{					
				setMSG("Press Button to generate report..",'N');						
				cl_dat.M_btnSAVE_pbst.requestFocus();					
			}
			else
			{
				txtDOCNO.requestFocus();
				setMSG("Invalid Document No., Press F1 Key for Help..",'N');
			}			
		}			
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;		
			intSRLNO = 0;
			intRECCT=0;
		}
		else if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			{
				setENBL(false);
				setMSG("Select an option..",'N');
			}
			else
			{
				setENBL(true);
				txtDOCNO.requestFocus();
			}
				
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
 	        if(M_objSOURC == txtDOCNO)		// GRIN No //hlpDOCNO()
			{
				try
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDOCNO";												
					String L_ARRHDR[] = {"SPL Doc. No.","Bill No.","Bill Date","Transporter Code"};
					M_strSQLQRY = "Select BL_DOCNO,BL_BILNO,BL_BILDT,BL_PRTCD";
					M_strSQLQRY += " from MM_BLMST";
					M_strSQLQRY += " where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '" + (String.valueOf(cmbBILTP.getSelectedItem()).toString()).trim().substring(0,2) + "'";					
					M_strSQLQRY += " and isnull(BL_STSFL,'') <> 'X'";
					if(txtDOCNO.getText().length() >0)
						M_strSQLQRY += " and BL_DOCNO LIKE '"+txtDOCNO.getText().trim() +"%'";
					M_strSQLQRY += " order by BL_DOCNO Desc";											
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"hlpDOCNO");
				}	
			}
 	    }
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtDOCNO")
		{
			txtDOCNO.setText(cl_dat.M_strHLPSTR_pbst);							
			if (txtDOCNO.getText().length()==8)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
	}
	void crtHSTBL()
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
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (vldDATA())
		{	
			String L_strBILTP; //to pass as argument to getALLREC "03" for incomming Transportation.
			try
			{
			    if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rptbp.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rptbp.doc";
				crtHSTBL();						
				strDOCNO = txtDOCNO.getText().trim();
				L_strBILTP =(String.valueOf(cmbBILTP.getSelectedItem()).toString()).trim().substring(0,2);
				getALLREC(strDOCNO,L_strBILTP);
				if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
				if(intRECCT == 0)
				{
					setMSG("Data not found for the given Inputs..",'E');
					return;
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{					
					if (M_rdbTEXT.isSelected())
					    doPRINT(strFILNM);
					else 
			        {    
						Runtime r = Runtime.getRuntime();
						Process p = null;								
						p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
						setMSG("For Printing Select File Menu, then Print  ..",'N');
					}    					
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				     Runtime r = Runtime.getRuntime();
					 Process p = null;					
				     if(M_rdbHTML.isSelected())
				        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
				     else
    				    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 					 
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
					cl_eml ocl_eml = new cl_eml();				    
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Transporter Bill Passing"," ");
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					}
			    }				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Error.exescrn.. ");
			}
		}	
		else
		{
			txtDOCNO.requestFocus();
			setMSG("Please Enter Document Number Or Press F1 for Help..",'N');
		}
	}	
    /**
    *Method to fetch data from MM_BLMST,MM_BILTR & MM_GRMST tables on the Basis of Document number
	*/
	public void getALLREC(String P_strDOCNO, String P_strBILTP)
	{ 			
		
		boolean L_flgFIRST = true;
		String L_strTOTAM,L_strPRNSTR;
		String L_strDRTPK="",L_strBILAM="",L_strOTHPA="",L_strOTHDA="",L_strREMDS="",L_strSHRP2="",L_strBILTP="";
		String L_strDIFQT,L_strRECQT,L_strCHLNO,L_strCHLDT="",L_strCHLQT,L_strLOCCD,L_strSHRPR,L_strSHRP1="";
		double L_dblTDED2=0,L_dblDCHQT=0,L_dblTDED1=0,L_dblCALVL=0,L_dblTRCQT=0,L_dblCALAM=0;
		double L_dblTCHQT=0,L_dblTSHRT=0,L_dblTDFQT=0;
		double L_dblDEDAM = 0,L_dblTDDAM=0,L_dblNETAM;			
		String L_strDEDA1="",L_strDEDA2="";
		java.sql.Date L_datTEMP;
		ResultSet L_rstRSSET;		
		setCursor(cl_dat.M_curWTSTS_pbst);			
	    try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);						
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Transporter Bill Passing </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}										
			M_strSQLQRY = "Select BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,BL_PRTTP,BL_PRTCD,BL_BILAM,";
			M_strSQLQRY += " BL_CALAM,BL_DRPKG,BL_DEDTP,BL_OTPMT,BL_OTDED,BL_REMDS,BIL_DOCRF,";
			M_strSQLQRY += " BIL_MATCD,BIL_STRTP,BIL_CHLQT,BIL_RECQT,BIL_RTPMT,BIL_LOCCD,GR_CHLDT,";
			M_strSQLQRY += " GR_CHLNO,GR_MATCD from MM_BLMST,MM_BILTR,MM_GRMST";
			M_strSQLQRY += " where BL_CMPCD = BIL_CMPCD AND BL_BILTP = BIL_BILTP";
			M_strSQLQRY += " AND BL_DOCNO = BIL_DOCNO";
			M_strSQLQRY += " AND BIL_CMPCD = GR_CMPCD and BIL_STRTP=GR_STRTP and BIL_DOCRF=GR_GRNNO";
			M_strSQLQRY += " AND BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and BL_CMPCD = BIL_CMPCD AND BL_BILTP = '" + P_strBILTP + "'";
			M_strSQLQRY += " AND BL_DOCNO = '"+P_strDOCNO +"'";
			M_strSQLQRY += " AND isnull(BL_STSFL,'') <> 'X'";
			M_strSQLQRY += " AND BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(BIL_STSFL,'') <> 'X'";
			M_strSQLQRY += " AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <> 'X'";
			M_strSQLQRY += " order by GR_CHLDT,GR_CHLNO";							
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);												
			if(M_rstRSSET != null)				
			while(M_rstRSSET.next())
			{				
				intSRLNO += 1;
				L_strSHRP1=L_strSHRP2="";				
				if(L_flgFIRST == true)
				{					
					strTRNCD = nvlSTRVL(M_rstRSSET.getString("BL_PRTCD"),"");
					try/// to get Transporter Name from Transporter Code
					{							
						M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
						M_strSQLQRY += " where PT_PRTTP = '"+strVENTP+"'";
						M_strSQLQRY += " and PT_PRTCD = '" + strTRNCD + "'";						    	
						L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET!= null)
						if(L_rstRSSET.next())
						{
							strTRNNM=L_rstRSSET.getString("PT_PRTNM");							
							L_rstRSSET.close();											
						}															
						if(L_rstRSSET != null)
							L_rstRSSET.close();			
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"fetch PRTNM");
					}									
					strBILNO = nvlSTRVL(M_rstRSSET.getString("BL_BILNO"),"");
					L_datTEMP = M_rstRSSET.getDate("BL_BILDT");
					if(L_datTEMP!= null)
						strBILDT = M_fmtLCDAT.format(L_datTEMP);

					L_strLOCCD = nvlSTRVL(M_rstRSSET.getString("BIL_LOCCD"),"");																	
					//strLOCDS = getLOCDS(strLOCCD);						
					try/// to get From-Location Name from location Code
					{
						M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
						M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
						M_strSQLQRY += " and CMT_CGSTP = 'QC11TKL' and ";
						M_strSQLQRY += " CMT_CODCD = '" + L_strLOCCD + "'";					    	
						L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET!=null)
						if(L_rstRSSET.next())
							strLOCDS = L_rstRSSET.getString("CMT_CODDS");						
						if(L_rstRSSET != null)
							L_rstRSSET.close();			
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"getLOCDS");						
					}										
					strRTPMT = nvlSTRVL(M_rstRSSET.getString("BIL_RTPMT"),"0");					
					L_strDRTPK = nvlSTRVL(M_rstRSSET.getString("BL_DRPKG"),"0");					
					L_strBILAM = nvlSTRVL(M_rstRSSET.getString("BL_BILAM"),"0");										
					L_strOTHPA = nvlSTRVL(M_rstRSSET.getString("BL_OTPMT"),"0");					
					L_strOTHDA = nvlSTRVL(M_rstRSSET.getString("BL_OTDED"),"0");					
					L_strREMDS = nvlSTRVL(M_rstRSSET.getString("BL_REMDS"),"");					
					chrDEDTP = nvlSTRVL(M_rstRSSET.getString("BL_DEDTP"),"").charAt(0);					
					L_flgFIRST = false;				
					prnHEADER(L_strBILTP);					
				}																								
				L_datTEMP = M_rstRSSET.getDate("GR_CHLDT");
				if(L_datTEMP!= null)					
					L_strCHLDT = M_fmtLCDAT.format(L_datTEMP);
				L_strCHLNO = nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"");
				L_strCHLQT = nvlSTRVL(M_rstRSSET.getString("BIL_CHLQT"),"0");			
				L_strRECQT = nvlSTRVL(M_rstRSSET.getString("BIL_RECQT"),"0");						
				L_strDIFQT = setNumberFormat(Double.parseDouble(L_strCHLQT) - Double.parseDouble(L_strRECQT),3);
				L_strSHRPR = setNumberFormat(((Double.parseDouble(L_strDIFQT)/Double.parseDouble(L_strCHLQT))*100),2);			
				if(chrDEDTP == 'B')
				{
					L_dblDEDAM =0;
				}
				else if(chrDEDTP == 'V')
				{
					L_dblDEDAM =0;
					if(Double.parseDouble(L_strSHRPR) >0.5)
					{
						L_dblDEDAM = Double.parseDouble(L_strDIFQT)*Double.parseDouble(L_strDRTPK)*1000;
						L_dblTDED2 +=L_dblDEDAM;
						L_strSHRP2 = setNumberFormat(L_dblDEDAM,2);
					}
					else
					{
						if(Double.parseDouble(L_strSHRPR) >0.3)
						{
							L_dblDCHQT = Double.parseDouble(L_strCHLQT);
							L_dblDEDAM = ((L_dblDCHQT-(L_dblDCHQT*0.3/100))-Double.parseDouble(L_strRECQT))*Double.parseDouble(L_strDRTPK)*1000;
							L_dblTDED1 +=L_dblDEDAM;
							L_strSHRP1 = setNumberFormat(L_dblDEDAM,2);
						}
						else
							L_dblDEDAM =0;
					}
				}			
				L_dblCALVL= Double.parseDouble(L_strCHLQT)*Double.parseDouble(nvlSTRVL(strRTPMT,"0"))-L_dblDEDAM;
				L_dblCALAM += L_dblCALVL; 
				L_dblTRCQT += Double.parseDouble(L_strRECQT);
				L_dblTCHQT += Double.parseDouble(L_strCHLQT);		
				if(Double.parseDouble(L_strDIFQT)>0)
					L_dblTDFQT += Double.parseDouble(L_strDIFQT);
				L_strTOTAM = setNumberFormat(L_dblCALVL,2);								
							
				if(chrDEDTP =='V')
				{
					L_strDEDA1 = L_strSHRP1;
					L_strDEDA2 = L_strSHRP2; 
				}
				L_strPRNSTR = padSTRING('L',String.valueOf(intSRLNO).toString(),3)+" " 
					+ padSTRING('R',L_strCHLDT,12)
					+ padSTRING('R',L_strCHLNO,8)
					+ padSTRING('L',L_strCHLQT,12)
					+ padSTRING('L',L_strRECQT,14)
					+ padSTRING('L',L_strDIFQT,12)
					+ padSTRING('L',L_strSHRPR,10)
					+ padSTRING('L',L_strDEDA1,9)
					+ padSTRING('L',L_strDEDA2,10)
					+ padSTRING('L',L_strTOTAM,12)+"\n";
				dosREPORT.writeBytes(L_strPRNSTR);								
				cl_dat.M_intLINNO_pbst +=1;				
				if(cl_dat.M_intLINNO_pbst > 64)
				{				
					dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					prnHEADER(L_strBILTP);					
				}				
				intRECCT++;
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------\n");			
			cl_dat.M_intLINNO_pbst +=1;		
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTCHQT,3),36));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTRCQT,3),14));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTDFQT,3),12));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTSHRT,2),10));
			if(chrDEDTP =='V')
			{
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTDED1,2),9));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTDED2,2),10));
			}
			else
			{
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTDDAM,2),9));
				dosREPORT.writeBytes(padSTRING('L'," ",10));
			}			
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCALAM,2),12));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst +=1;			
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------\n\n");
			cl_dat.M_intLINNO_pbst +=2;			
			if(cl_dat.M_intLINNO_pbst > 46)		// 20 lines of footer can not accomodate
			{			
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);
				cl_dat.M_intLINNO_pbst = 0;				
				String L_STRHD,L_STRHD1;									
				dosREPORT.writeBytes("\n\n\n");	
				if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B><H2 Style =\"font-size:12pt\">");			
				if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))				
					prnFMTCHR(dosREPORT,M_strBOLD);							
				dosREPORT.writeBytes(padSTRING('R'," ",32));
				dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n");							
				dosREPORT.writeBytes(padSTRING('R'," ",20));
				dosREPORT.writeBytes("Bill Clearance Statement -("+P_strBILTP+")"+"\n\n");						
				if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))				
					prnFMTCHR(dosREPORT,M_strNOBOLD);			
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</H2></B>");				
				dosREPORT.writeBytes(padSTRING('R',"Transporter   : "+strTRNCD +" / "+strTRNNM ,82)+"\n");					
				dosREPORT.writeBytes("Bill No.      : "+strBILNO+"\n");				
				dosREPORT.writeBytes(padSTRING('R',"Bill Date     : "+strBILDT,86));
				dosREPORT.writeBytes("Date : "+cl_dat.M_strLOGDT_pbst+"\n");				
				dosREPORT.writeBytes("Location From : "+padSTRING('R',strLOCDS,25));
				dosREPORT.writeBytes("Rate : "+ padSTRING('R',strRTPMT+ " Rs./MT ",38));
				dosREPORT.writeBytes("Page : " + (cl_dat.M_PAGENO + 1) + "\n\n");				
				cl_dat.M_intLINNO_pbst +=2;					
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------\n");				
			}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</PRE></p><P Style =\"font-size:10pt\"><PRE Style =\"font-size:10pt\">");
			dosREPORT.writeBytes("Bill Amount              : Rs. "+L_strBILAM+"\n");			
			if(chrDEDTP =='B')
			{
				L_strBILTP ="Bill wise";
				L_dblDEDAM = L_dblTDDAM ;
			}
			else if(chrDEDTP =='V')
			{
				L_strBILTP ="Truck wise";
				L_dblDEDAM = L_dblTDED1 + L_dblTDED2;
			}			
			dosREPORT.writeBytes("Deduction Type           : "+L_strBILTP +"\n");										
			dosREPORT.writeBytes("Deduction Rate/Kg.       : Rs. " +L_strDRTPK+"\n");									
			dosREPORT.writeBytes("Deduction Amount         : Rs. " +setNumberFormat(L_dblDEDAM,2)+"\n");						
			dosREPORT.writeBytes("Other Amount Payable     : Rs. "+L_strOTHPA+"\n");					
			dosREPORT.writeBytes("Other Amount Deduction   : Rs. "+L_strOTHDA+"\n");						
			dosREPORT.writeBytes("Remarks                  : "+L_strREMDS+"\n\n");
			L_dblNETAM = L_dblCALAM-L_dblTDDAM +Double.parseDouble(L_strOTHPA)-Double.parseDouble(L_strOTHDA);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B><H3 Style =\"font-size:10pt\">");
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))				
				prnFMTCHR(dosREPORT,M_strBOLD);			
			dosREPORT.writeBytes("Net Amount Payable  : Rs. "+setNumberFormat(L_dblNETAM,0)+"/-"+"\n");						
			String L_WRDDS = getCURWRD(setNumberFormat(L_dblNETAM,0)+".00","Paise");
			String L_WRDDS1="";					
			if(L_WRDDS.trim().length() > 65)
			{
				int index =0;
				StringBuffer L_SBWRD = new StringBuffer(L_WRDDS);
				L_SBWRD = L_SBWRD.reverse();
				index = L_SBWRD.toString().indexOf(" ");
				L_WRDDS = L_WRDDS.substring(0,L_WRDDS.length()-1-index);
				L_WRDDS1 = L_WRDDS.substring(L_WRDDS.length()-index);		
			}			
			dosREPORT.writeBytes("Amount In Words     : Rupees " + L_WRDDS + "\n");						
			dosREPORT.writeBytes(padSTRING('R',"",22)+L_WRDDS1 + "\n");						
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))				
				prnFMTCHR(dosREPORT,M_strNOBOLD);			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</H3></B>\n");						
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n\n\n\n\n");			
			dosREPORT.writeBytes("Prepared By				Checked By				Approved By \n");						
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</PRE></P></BODY></HTML>");															
			if(M_rstRSSET != null)
				M_rstRSSET.close();												
			setMSG("Report completed.. ",'N');				
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}			
			dosREPORT.close();
			fosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	/**
	Method to check the validity of document number, before execution, 
	i.e. to check for blank and wrong input.
	*/
	boolean vldDATA()
	{		
		if(txtDOCNO.getText().length()==0)			
		{
			setMSG("Please Enter Consignment andPress Enter Key.. ",'E');
			txtDOCNO.requestFocus();			
			return false;
		}
		else
		{
			try
			{
			ResultSet L_rstRSSET;
			M_strSQLQRY = "Select BL_DOCNO";
			M_strSQLQRY += " from MM_BLMST";
			M_strSQLQRY += " where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '" + (String.valueOf(cmbBILTP.getSelectedItem()).toString()).trim().substring(0,2) + "'";
			M_strSQLQRY += " AND isnull(BL_STSFL,'') <> 'X'";
			M_strSQLQRY += " AND BL_DOCNO ='" +txtDOCNO.getText().trim()+"'";								
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(!L_rstRSSET.next())
				{
					setMSG("Invalid Document number, press F1 for Help..",'E');
					return false;					
				}
				L_rstRSSET.close();
			}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"vldDATA");
			}
		}			
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount() == 0)
			{					
				setMSG("Please Select the Email/s from List through F1 Help ..",'N');
				return false;
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{ 
			if (M_cmbDESTN.getSelectedIndex() == 0)
			{	
				setMSG("Please Select the Printer from Printer List ..",'N');
				return false;
			}
		}
		return true;
	}
	/**
	* Method to generate the Header part of the Report.
	*/
	private void prnHEADER(String P_strBILTP)
	{
		try
		{
			cl_dat.M_PAGENO += 1;
			cl_dat.M_intLINNO_pbst=0;			
			String L_STRHD,L_STRHD1;			
			dosREPORT.writeBytes("\n\n\n");						
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B><H2 Style =\"font-size:12pt\">");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
				prnFMTCHR(dosREPORT,M_strBOLD);													
			dosREPORT.writeBytes(padSTRING('R'," ",33));
			dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n");						
			dosREPORT.writeBytes(padSTRING('R'," ",20));
			dosREPORT.writeBytes("Bill Clearance Statement -(Incoming Transportation)"+"\n\n");						
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))				
				prnFMTCHR(dosREPORT,M_strNOBOLD);			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</H2></B>");
			dosREPORT.writeBytes("Transporter   : "+strTRNCD +" / "+ strTRNNM +"\n");			
			dosREPORT.writeBytes("Bill No.      : "+strBILNO+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Bill Date     : "+strBILDT,86));
			dosREPORT.writeBytes("Date : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes("Location From : "+padSTRING('R',strLOCDS,25));
			dosREPORT.writeBytes("Rate : "+padSTRING('R',strRTPMT + " Rs./MT ",38));
			dosREPORT.writeBytes("Page : "+cl_dat.M_PAGENO +"\n\n");					
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------\n");						
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))				
				prnFMTCHR(dosREPORT,M_strBOLD);			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");			
			dosREPORT.writeBytes("Sr.  Date        Challan     Challan      Received    Shortage  Shortage  Deduction  Amount       Amount\n");
			if(chrDEDTP =='V')				
				dosREPORT.writeBytes("No.              No.            Qty.          Qty.                   %    >0.3<=0.5    >0.5      Rs.\n");
			else				
				dosREPORT.writeBytes("No.              No.            Qty.          Qty.                    %          >0.2               Rs.\n");	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))				
				prnFMTCHR(dosREPORT,M_strNOBOLD);			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------\n");			
			cl_dat.M_intLINNO_pbst +=13;						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	 * Method to get the convert the numeric value into text format.
	 * @param LP_CURVL String argument to pass net amount.
	 * @param LP_DENOM String argument to pass String representing "paise"
	 */
	private String getCURWRD(String LP_CURVL,String LP_DENOM)
	{        
		String L_PRNSTR = "";
		try
		{			
			int L_REMVL = 0;			
			int L_QUOVL = 0;			
			String L_RPSVL = LP_CURVL.substring(0,LP_CURVL.indexOf(".")).trim();			
			int L_RUPVL = Integer.parseInt(L_RPSVL);			
			String L_PAIVL = LP_CURVL.substring(LP_CURVL.lastIndexOf(".")+1,LP_CURVL.length()).trim();			
			int L_ABSVL = L_RPSVL.length();                        
			if(L_ABSVL > 0)
			{				
				if(L_ABSVL == 1)
				{
					L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}				
				else if(L_ABSVL == 2)
				{
					L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}				
				else if(L_ABSVL == 3)
				{
					L_QUOVL = L_RUPVL/100;
					L_REMVL = L_RUPVL%100;
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}				
				else if(L_ABSVL == 4 || L_ABSVL == 5)
				{
					L_QUOVL = L_RUPVL/1000;
					L_REMVL = L_RUPVL%1000;                  
                       L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');                    
					L_QUOVL = L_REMVL/100;
					L_REMVL = L_REMVL%100;                    
                       L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');                    
					   L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');                    
				}				
				else if(L_ABSVL == 6 || L_ABSVL == 7)
				{
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
				}								//Crores
				else if(L_ABSVL == 8 || L_ABSVL == 9)
				 {
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
			if(L_PAIVL.length() > 0 && !L_PAIVL.equals("00"))
			{
				L_PRNSTR += chkZERO(hst1to100.get(L_PAIVL).toString(),"and "+LP_DENOM,'F');
			}
			L_PRNSTR += "Only";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCURWRD");
		}
		return L_PRNSTR;
	}
	/**
	 * Metod to check the Zero's while converting the numaric value to String 
	 * @param LP_STRVL String argument to pass string representing numeric value.
	 * @param LP_RPSVL String argument to pass 
	 * @param LP_PLCSTR char argument to pass 
	 */
	private String chkZERO(String LP_STRVL,String LP_RPSVL,char LP_PLCSTR)
	{
		String L_RTNSTR = "";
		try
		{
			if(String.valueOf(LP_STRVL).equals(null) || LP_STRVL.equalsIgnoreCase("zero"))
				return "";
			else
			{
				if(LP_PLCSTR == 'F')
				{
					L_RTNSTR = LP_RPSVL + " " +  LP_STRVL + " ";
				}
				else if(LP_PLCSTR == 'B')
					L_RTNSTR = LP_STRVL + " " + LP_RPSVL + " ";			
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkZERO");
		}
		return L_RTNSTR;
	}
}