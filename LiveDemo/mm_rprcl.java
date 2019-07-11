/*
System Name   : Material Management System
Program Name  : Reciept Summary
Program Desc. : Report for Receipt / GRIN Summary
Author        : Mr S.R.Mehesare
Date          : 01.03.2005
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
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.util.Hashtable;
public class mm_rprcl extends cl_rbase
{  	
	private JTextField txtFMDAT;
	private JTextField txtTODAT;
	private JRadioButton rdbRCPT;
	private JRadioButton rdbGRIN;
	private String strFILNM;
	private	String strSTRTP;
	private String strPRNSTR;
	private ButtonGroup bgrOPTN;
	private int intRECCT;
	private	String strFMDAT;
	private String strTODAT;
	private Hashtable<String,String> hstMATTP;				   
	private FileOutputStream fosREPORT;
    private DataOutputStream dosREPORT ;
	private ResultSet M_rstRSSET1;	
	mm_rprcl()
	{
	    super(2);
	    try
	    {				
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			rdbGRIN= new JRadioButton ("GRIN",true);
			rdbRCPT= new JRadioButton ("Reciept");
			bgrOPTN = new ButtonGroup();
			bgrOPTN.add(rdbRCPT);
			bgrOPTN.add(rdbGRIN);
			add(rdbGRIN,4,4,1,1,this,'L');
			add(rdbRCPT,4,5,1,1,this,'L');			
			add(new JLabel("From Date"),5,4,1,1,this,'L');
			add(txtFMDAT= new TxtDate(),5,5,1,1,this,'L');			
			add(new JLabel("To Date"),6,4,1,1,this,'L');
			add(txtTODAT= new TxtDate(),6,5,1,1,this,'L');				
			M_pnlRPFMT.setVisible(true);
			hstMATTP=new Hashtable<String,String>();
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);			
            if(M_rstRSSET1 != null)    			        		
		    {				
        		while(M_rstRSSET1.next())
        		{        		                 			
					hstMATTP.put(M_rstRSSET1.getString("CMT_CODCD"),M_rstRSSET1.getString("CMT_CODDS"));					
        		}
        		M_rstRSSET1.close();
            } 					
			setCursor(cl_dat.M_curDFSTS_pbst);	
			setENBL(false);	
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"GUI Designing");
		}	
    }
	// Super class method over write to inhance its funcationality
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);
		txtTODAT.setEnabled(L_flgSTAT);
		rdbGRIN.setEnabled(L_flgSTAT);
		rdbRCPT.setEnabled(L_flgSTAT);					
		if (L_flgSTAT==false)
		{			
			txtTODAT.setText("");
			txtFMDAT.setText("");			    			
		}
		else
		{
			if((txtFMDAT.getText().length()==0) || (txtTODAT.getText().length()==0 ))
			{
				txtFMDAT.setText(calDATE(cl_dat.M_strLOGDT_pbst));
				txtTODAT.setText(calDATE(cl_dat.M_strLOGDT_pbst));
				txtFMDAT.requestFocus();
			}
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
	    super.actionPerformed(L_AE);
		if (M_objSOURC == txtFMDAT)
			txtTODAT.requestFocus();
		if (M_objSOURC == txtTODAT)
			cl_dat.M_btnSAVE_pbst.requestFocus();
		if (M_objSOURC == cl_dat.M_btnSAVE_pbst )
			cl_dat.M_PAGENO=0;
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (vldDATA())
		{	
			try
			{
			    if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rprcl.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rprcl.doc";				
				getALLREC();
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
					/*if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().equals("Select Printer"))
					{
						setMSG("Please select the printer ..",'E');
						return;
					}*/
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
						if (rdbRCPT.isSelected())
							ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Tankfarm List of GRIN."," ");
						else
							ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Tankfarm List of Receiprs."," ");
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
			txtTODAT.requestFocus();
			setMSG("Please Enter Date Range..",'N');
		}
	}	
	/**
    Method to fetch data and clup it with Header & footer in Data Output Stream
	*/
	private void getALLREC()
	{ 		
		String L_strOCNSNO ="",L_strTRNNM="";
		String L_strMATCD,L_strMATDS,L_strUOMCD,L_strEXCCT,L_strTRNCD,L_strGINNO,L_strLRYNO,L_strCHLNO,L_strGRNNO;
		String L_strCHLDT="", L_strCHLQT, L_strRECQT, L_strSHRQT, L_strGRNDT, L_strMATTP="",L_strBOENO,L_strCNSNO="",L_strVENCD ="",L_strOVENCD ="";
		String L_strOEXCCT ="", L_strOMATCD = "", L_strOTRNCD = "", strPRNSTR,L_strVENNM ="";//L_strOBOENO ="",L_strTRNNM,L_strVENCD =""
		float L_fltTCHLQT=0, L_fltTRECQT=0, L_fltTDIFQT=0 ,L_fltDIFQT=0;
		float L_fltMCHLQT=0, L_fltMRECQT=0, L_fltMDIFQT=0;
		intRECCT = 0;
		java.sql.Date tmpDATE;
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	        
			strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Reciept Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 10pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}				
			prnHEADER();			
			strSTRTP=M_strSBSCD.substring(2,4);
			if (rdbRCPT.isSelected())
			{				
				M_strSQLQRY = "Select WB_MATCD, WB_MATDS, CT_UOMCD, WB_MATTP, WB_TPRCD, WB_TPRDS,";
				M_strSQLQRY += " ' ' L_strVENCD,' 'L_strVENNM, WB_DOCNO, WB_LRYNO, WB_CHLNO, WB_DOCRF,";
				M_strSQLQRY += " WB_CHLDT, WB_CHLQT, WB_UOMQT, WB_BOENO,' ' L_strCNSNO from MM_WBTRN,CO_CTMST";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '01' and WB_MATCD = CT_MATCD  and WB_ACPTG = 'A'";			// Tankfarm
				M_strSQLQRY += " and WB_TNKNO is not null";
				M_strSQLQRY += " and CONVERT(varchar,WB_ACPDT,101) between '" + strFMDAT + "' and '" + strTODAT;
				M_strSQLQRY += "' order by WB_MATCD,WB_MATTP, WB_TPRCD,WB_DOCNO";				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
				if(M_rstRSSET !=null)
				{					
					while(M_rstRSSET.next())
					{
						L_strGRNNO = "";
						L_strGRNDT = "";
						L_strMATCD = nvlSTRVL(M_rstRSSET.getString("WB_MATCD"),"").trim();
						L_strMATDS = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"").trim();
						L_strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"").trim();					
						L_strEXCCT = nvlSTRVL(M_rstRSSET.getString("WB_MATTP"),"").trim();//L_strEXCCT
						L_strTRNCD = nvlSTRVL(M_rstRSSET.getString("WB_TPRCD"),"-").trim();//WB_TPRCD L_TRNCD
						L_strTRNNM = nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"-").trim();//WB_TPRDS L_strTRNNM
						L_strGINNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"").trim(); //WB_DOCNO L_strGINNO					
						L_strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"").trim();
						L_strCHLNO = nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),"").trim();
						L_strGRNNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCRF"),"").trim();					 
						tmpDATE = M_rstRSSET.getDate("WB_CHLDT");
						if (tmpDATE!= null)
						    L_strCHLDT = M_fmtLCDAT.format(tmpDATE);					
						L_strCHLQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0");					
						L_strRECQT = nvlSTRVL(M_rstRSSET.getString("WB_UOMQT"),"0");//WB_UOMQT L_strRECQT					
						L_fltDIFQT = Float.valueOf(L_strRECQT).floatValue() - Float.valueOf(L_strCHLQT).floatValue();
						L_strSHRQT = setNumberFormat(L_fltDIFQT,3);					
						L_strBOENO = nvlSTRVL(M_rstRSSET.getString("WB_BOENO"),"").trim();											
						if(cl_dat.M_intLINNO_pbst > 64)
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    							
							prnHEADER();
						}
						if(!L_strMATCD.trim().equals(L_strOMATCD) || !L_strEXCCT.trim().equals(L_strOEXCCT))	// Material Changes
						{
							if(hstMATTP.containsKey(L_strEXCCT))
								L_strMATTP = hstMATTP.get(L_strEXCCT).toString();														
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							if(intRECCT != 0)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes(padSTRING('L',"Total : ",46));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTCHLQT,3),12));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTRECQT,3),13));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTDIFQT,3),11) + "\n");
								cl_dat.M_intLINNO_pbst += 1;
								if(cl_dat.M_intLINNO_pbst > 64)
								{
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										dosREPORT.writeBytes(padSTRING('R'," ",4));
									dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    								
									prnHEADER();
								}
								L_strOTRNCD = L_strTRNCD;
								L_fltTCHLQT = L_fltTRECQT = L_fltTDIFQT = 0;
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------\n");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes(padSTRING('L',"Total : ",46)
									 + padSTRING('L',setNumberFormat(L_fltMCHLQT,3),12)  
									 + padSTRING('L',setNumberFormat(L_fltMRECQT,3),13)  
									 + padSTRING('L',setNumberFormat(L_fltMDIFQT,3),11) + "\n\n");
								cl_dat.M_intLINNO_pbst += 3;
								if(cl_dat.M_intLINNO_pbst > 64)
								{
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										dosREPORT.writeBytes(padSTRING('R'," ",4));
									dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
									prnHEADER();
								}
								L_fltMCHLQT = L_fltMRECQT = L_fltMDIFQT = 0;
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if (M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</b>");
							}
							L_strOMATCD = L_strMATCD;
							L_strOEXCCT = L_strEXCCT;
							L_strOTRNCD = L_strTRNCD;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes(padSTRING('R',L_strMATCD,13) + padSTRING('R',L_strMATDS,50) + padSTRING('R',L_strUOMCD,5) + padSTRING('R',L_strMATTP,20) + "\n\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes(padSTRING('R',L_strTRNCD,7) + L_strTRNNM + "\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b></b>");
							cl_dat.M_intLINNO_pbst += 2;
							if(cl_dat.M_intLINNO_pbst > 64)
							{							
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
								prnHEADER();
							}
						}						
						if(!L_strTRNCD.trim().equals(L_strOTRNCD))		// Transporter Changes
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							if(intRECCT != 0)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes(padSTRING ('L',"Total : ",46)); 
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTCHLQT,3),12));  
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTRECQT,3),13));  
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTDIFQT,3),11) + "\n");								
								cl_dat.M_intLINNO_pbst += 1;			
								if(cl_dat.M_intLINNO_pbst > 64)
								{
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										dosREPORT.writeBytes(padSTRING('R'," ",4));
									dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
									prnHEADER();
								}								
								L_fltTCHLQT = L_fltTRECQT = L_fltTDIFQT = 0;
							}
							L_strOTRNCD = L_strTRNCD;
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes(padSTRING('R',L_strTRNCD,7) + L_strTRNNM + "\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b></b>");
							cl_dat.M_intLINNO_pbst += 2;
							if(cl_dat.M_intLINNO_pbst > 64)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
								prnHEADER();
							}
						}						
						L_fltTCHLQT += Float.valueOf(L_strCHLQT).floatValue();
						L_fltTRECQT += Float.valueOf(L_strRECQT).floatValue();
						L_fltTDIFQT += L_fltDIFQT;						
						L_fltMCHLQT += Float.valueOf(L_strCHLQT).floatValue();
						L_fltMRECQT += Float.valueOf(L_strRECQT).floatValue();
						L_fltMDIFQT += L_fltDIFQT;						
						strPRNSTR = "";
						strPRNSTR += padSTRING('R',L_strGINNO,12);
						strPRNSTR += padSTRING('R',L_strLRYNO,12);
						strPRNSTR += padSTRING('R',L_strCHLNO,10);
						strPRNSTR += padSTRING('R',L_strCHLDT,12);
						strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strCHLQT).floatValue(),3),12);
						strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strRECQT).floatValue(),3),13);
						strPRNSTR += "  " + padSTRING('L',setNumberFormat(Float.valueOf(L_strSHRQT).floatValue(),3),9)+"   ";						
						strPRNSTR += padSTRING('R',L_strGRNNO,11);
						//strPRNSTR += padSTRING('R',L_strGRNDT,11);
						strPRNSTR += padSTRING('R',L_strBOENO,12);
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",4));
						dosREPORT.writeBytes(strPRNSTR + "\n");
						cl_dat.M_intLINNO_pbst += 1;
						intRECCT++;
					}
					M_rstRSSET.close();
				}
				else
				{
					setMSG("No Data Found..",'E');
					return ;
				}													
				if(intRECCT == 0)
				{
					setMSG("No Data Found for the given selection..",'E');
					return ;
				}
				else
				{
					if(cl_dat.M_intLINNO_pbst > 64)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",4));
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
						prnHEADER();
					}
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if (M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes(padSTRING('L',"Total : ",46) 
							 + padSTRING('L',setNumberFormat(L_fltTCHLQT,3),12)  
							 + padSTRING('L',setNumberFormat(L_fltTRECQT,3),13)  
							 + padSTRING('L',setNumberFormat(L_fltTDIFQT,3),11) + "\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes(padSTRING('L',"Total : ",46) 
						+ padSTRING('L',setNumberFormat(L_fltMCHLQT,3),12)  
						+ padSTRING('L',setNumberFormat(L_fltMRECQT,3),13)  
						+ padSTRING('L',setNumberFormat(L_fltMDIFQT,3),11) + "\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if (M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");					
				}		
			}						
			//for GRIN
			if(rdbGRIN.isSelected())
			{								
				M_strSQLQRY = "Select GR_MATCD, CT_MATDS, CT_UOMCD,";
				M_strSQLQRY += " GR_EXCCT, GR_TRNCD, GR_TRNNM, GR_VENCD, GR_VENNM,";
				M_strSQLQRY += "GR_GINNO, GR_LRYNO, GR_CHLNO,";
				M_strSQLQRY += "GR_CHLDT, GR_CHLQT, GR_RECQT,";
				M_strSQLQRY += "GR_GRNNO, GR_GRNDT, GR_BOENO, GR_CNSNO";
				M_strSQLQRY += " from MM_GRMST,CO_CTMST";
				M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_MATCD = CT_MATCD and GR_STRTP = '" + strSTRTP + "'";
				M_strSQLQRY += " and GR_GRNDT between '" + strFMDAT + "' and '" + strTODAT;
				M_strSQLQRY += "' and isnull(GR_STSFL,'') <>'X'";
				M_strSQLQRY += " order by GR_TRNCD,GR_CNSNO,GR_VENCD,GR_GINNO";												
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
						L_strGRNNO = "";
						L_strGRNDT = "";
						L_strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"").trim();
						L_strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"").trim();
						L_strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"").trim();
						L_strEXCCT = nvlSTRVL(M_rstRSSET.getString("GR_EXCCT"),"").trim();
						L_strTRNCD = nvlSTRVL(M_rstRSSET.getString("GR_TRNCD"),"-").trim();
						L_strTRNNM = nvlSTRVL(M_rstRSSET.getString("GR_TRNNM"),"-").trim();
						L_strGINNO = nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),"").trim();
						L_strLRYNO = nvlSTRVL(M_rstRSSET.getString("GR_LRYNO"),"").trim();
						L_strCHLNO = nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"").trim();					
						tmpDATE = M_rstRSSET.getDate("GR_CHLDT");
						if (tmpDATE != null)
						    L_strCHLDT = M_fmtLCDAT.format(tmpDATE);
						L_strCHLQT = nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0");
						L_strRECQT = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0");
						L_strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"").trim();						
						tmpDATE = M_rstRSSET.getDate("GR_GRNDT");
						if (tmpDATE!= null)
						    L_strGRNDT = M_fmtLCDAT.format(tmpDATE);
						L_strCNSNO = nvlSTRVL(M_rstRSSET.getString("GR_CNSNO"),"").trim();
						L_strVENCD = nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"").trim();
						L_strVENNM = nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"").trim();
						L_strBOENO = nvlSTRVL(M_rstRSSET.getString("GR_BOENO"),"").trim();
						L_fltDIFQT = Float.valueOf(L_strRECQT).floatValue() - Float.valueOf(L_strCHLQT).floatValue();
						L_strSHRQT = setNumberFormat(L_fltDIFQT,3);						
						if(cl_dat.M_intLINNO_pbst > 64)
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
							    dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
							prnHEADER();
						}
						if(!L_strTRNCD.trim().equals(L_strOTRNCD))		// Transporter Changes
						{	
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							if(intRECCT != 0)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes (padSTRING('L',"Supplier's Total    : ",47)
									+ padSTRING('L',setNumberFormat(L_fltMCHLQT,3),10) 
									+ padSTRING('L',setNumberFormat(L_fltMRECQT,3),11)  
									+ padSTRING('L',setNumberFormat(L_fltMDIFQT,3),11) + "\n");
								cl_dat.M_intLINNO_pbst += 1;
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes(padSTRING('L',"Transpotrer's Total : ",47) 
										+ padSTRING('L',setNumberFormat(L_fltTCHLQT,3),10)  
										+ padSTRING('L',setNumberFormat(L_fltTRECQT,3),11)  
										+ padSTRING('L',setNumberFormat(L_fltTDIFQT,3),11) + "\n");
								cl_dat.M_intLINNO_pbst += 1;
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if (M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</b>");
								if(cl_dat.M_intLINNO_pbst > 64)
								{
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										dosREPORT.writeBytes(padSTRING('R'," ",4));
									dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
									    dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
									prnHEADER();
								}
								L_fltTCHLQT = L_fltTRECQT = L_fltTDIFQT = 0;
								L_fltMCHLQT = L_fltMRECQT = L_fltMDIFQT = 0;
							}
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes(padSTRING('R',L_strTRNCD,7) + L_strTRNNM + "\n");
							strPRNSTR = padSTRING('R',L_strCNSNO,11);
							strPRNSTR += padSTRING('R',L_strVENCD,13);
							strPRNSTR += padSTRING('R',L_strVENNM,40);
							strPRNSTR += padSTRING('R',L_strMATDS,25);
							if(hstMATTP.containsKey(L_strEXCCT))								
								strPRNSTR += padSTRING('R',hstMATTP.get(L_strEXCCT).toString(),20);							
							else
								strPRNSTR += padSTRING('R'," ",20);																					
							dosREPORT.writeBytes("\n" );
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes(strPRNSTR + "\n\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b></b>");
							cl_dat.M_intLINNO_pbst += 3;
							strPRNSTR ="";
							L_strOTRNCD = L_strTRNCD;
							L_strOCNSNO = L_strCNSNO;
							L_strOVENCD = L_strVENCD;							
							cl_dat.M_intLINNO_pbst += 2;
							if(cl_dat.M_intLINNO_pbst > 64)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
							    dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
								prnHEADER();
							}				
						}					
						if(!L_strVENCD.equals(L_strOVENCD)) 
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							if(intRECCT != 0)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",4));
								dosREPORT.writeBytes(padSTRING('L',"Total : ",47) 
										 + padSTRING('L',setNumberFormat(L_fltMCHLQT,3),10)  
										 + padSTRING('L',setNumberFormat(L_fltMRECQT,3),11)  
										 + padSTRING('L',setNumberFormat(L_fltMDIFQT,3),11) + "\n");
								cl_dat.M_intLINNO_pbst += 1;
							}
							L_fltMCHLQT = L_fltMRECQT = L_fltMDIFQT = 0;
							strPRNSTR = padSTRING('R',L_strCNSNO,11);
							strPRNSTR += padSTRING('R',L_strVENCD ,13);
							strPRNSTR += padSTRING('R',L_strVENNM ,40);
							strPRNSTR += padSTRING('R',L_strMATDS,25);							
							if(hstMATTP.containsKey(L_strEXCCT))								
								strPRNSTR += padSTRING('R',hstMATTP.get(L_strEXCCT).toString(),20);							
							else
								strPRNSTR += padSTRING('R'," ",20);									
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",4));
							dosREPORT.writeBytes(strPRNSTR + "\n\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if (M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b></b>");
							cl_dat.M_intLINNO_pbst += 3;
							L_strOCNSNO = L_strCNSNO;
							L_strOVENCD = L_strVENCD;
							strPRNSTR ="";
						}					
						L_fltTCHLQT += Float.valueOf(L_strCHLQT).floatValue();
						L_fltTRECQT += Float.valueOf(L_strRECQT).floatValue();
						L_fltTDIFQT += L_fltDIFQT;						
						L_fltMCHLQT += Float.valueOf(L_strCHLQT).floatValue();
						L_fltMRECQT += Float.valueOf(L_strRECQT).floatValue();
						L_fltMDIFQT += L_fltDIFQT;
						strPRNSTR = "";
						strPRNSTR += padSTRING('R',L_strGINNO,13);						
						strPRNSTR += padSTRING('R',L_strLRYNO,13);
						strPRNSTR += padSTRING('R',L_strCHLNO,9);
						strPRNSTR += padSTRING('R',L_strCHLDT,12);
						strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strCHLQT).floatValue(),3),10);
						strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strRECQT).floatValue(),3),11);
						strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strSHRQT).floatValue(),3),11);
						strPRNSTR += "  ";
						strPRNSTR += padSTRING('R',L_strGRNNO,13);
						strPRNSTR += padSTRING('R',L_strGRNDT,14);
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",4));
						dosREPORT.writeBytes(strPRNSTR + "\n");
						cl_dat.M_intLINNO_pbst += 1;
						intRECCT++;
					}					
					M_rstRSSET.close();					
				}
				else
				{
					setMSG("No Data Found..",'E');
					return;
				}									
				if(intRECCT == 0)
				{
					setMSG("No Data Found for the given selection..",'E');
				}
				else
				{
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if (M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes(padSTRING('L',"Total : ",47) 
						 + padSTRING('L',setNumberFormat(L_fltMCHLQT,3),10)  
						 + padSTRING('L',setNumberFormat(L_fltMRECQT,3),11)  
						 + padSTRING('L',setNumberFormat(L_fltMDIFQT,3),11) + "\n");
					cl_dat.M_intLINNO_pbst += 1;
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes(padSTRING('L',"Total : ",47)
						 + padSTRING('L',setNumberFormat(L_fltTCHLQT,3),10)  
						 + padSTRING('L',setNumberFormat(L_fltTRECQT,3),11)  
						 + padSTRING('L',setNumberFormat(L_fltTDIFQT,3),11) + "\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if (M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");
					cl_dat.M_intLINNO_pbst += 1;
					if(cl_dat.M_intLINNO_pbst > 64)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",4));
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
						prnHEADER();
					}
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if (M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");
					dosREPORT.writeBytes("\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if (M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");
					setMSG("Report completed.. ",'N');
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes("Prepared By              Checked By               Inspected By               Approved By\n\n\n");   
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes("Dt:                      Dt:                      Dt:                        Dt: \n");   					
				}
			}					
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);								
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	/**
	Method to validate data, before execution. Check for blank input and wrong selection of Memo type
	*/
	boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Spacify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter valid To-Date To Spacify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}				
			else if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				txtTODAT.requestFocus();
				return false;
			}
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("Please Note that From-Date must be Older than To-Date .. ",'E');								
				txtFMDAT.setText(calDATE(cl_dat.M_strLOGDT_pbst));            
				txtFMDAT.requestFocus();
				return false;
			}				
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
		}
		catch (Exception L_EX)
		{
		setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	/**
	Method to Generate the Report Part of the file.
	*/
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
			dosREPORT.writeBytes("\n\n\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",4));
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,65));
			dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,42) + "\n");			
			if(rdbGRIN.isSelected())
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_cmbSBSL2_pbst.getSelectedItem() + " List of GRIN between " + txtFMDAT.getText().trim() + " and " + txtTODAT.getText().trim(),66));
				dosREPORT.writeBytes(padSTRING('L',"Page No.    : " +String.valueOf(cl_dat.M_PAGENO).toString(),32) + "\n");
			}
			else
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_cmbSBSL2_pbst.getSelectedItem() + " List of Reciepts between " + txtFMDAT.getText().trim() + " and " + txtTODAT.getText().trim(),66));
				dosREPORT.writeBytes(padSTRING('L',"Page No.    : " +String.valueOf(cl_dat.M_PAGENO).toString(),32) + "\n");
			}
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",4));
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");			
			dosREPORT.writeBytes("\n");		
			if(rdbGRIN.isSelected())
			{	
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes("Transporter code & Description \n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes("Cons.No    Vendor code  Vendor Description                      Material Description     Category\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes("Gate-In No.  Vehicle No.  Chl.No.  Chl.Date     Chl. Qty.   Rcpt.Qty   Shortage  GRIN No.     GRIN Date \n");
				cl_dat.M_intLINNO_pbst += 12;
			}
			else
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes("Material Code & Description\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes("Transporter Code & Name\n");				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",4));
				dosREPORT.writeBytes("GateIn No.  Vehicle No. Chl.No.   Chl.Date       Chl. Qty.    Rcpt.Qty.   Shortage   GRIN No.   B/E No.      \n");
				cl_dat.M_intLINNO_pbst += 12;			
			}
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			dosREPORT.writeBytes(padSTRING('R'," ",4));		
		dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------");		
		dosREPORT.writeBytes("\n");
	
		}		
		catch(Exception L_XE)
		{
			setMSG(L_XE,"prnHEADER");
		}
	}	
	/**
	Method to Calculate From-Date older than To-Date.
	*/
    private String calDATE(String P_strTODAT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strTODAT));
		    M_calLOCAL.add(java.util.Calendar.DATE,-1);																
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{	       
			setMSG(L_EX, "calDATE");
			return (cl_dat.M_strLOGDT_pbst);
        }					
	}
}
