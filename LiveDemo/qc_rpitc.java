/*
System Name   : Finished Goods Inventory Management System
Program Name  : Despatch Summary Report
Program Desc. : Gives Despatch Summary Report according to the Selected Sale Type

Modified By    : Ms. A M. Kulkarni.
Modified Date  : 08/01/07
Modified det.  :
Version        : FIMS 2.0
*/

import java.io.*; 
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.math.BigDecimal;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.*;
import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.DataOutputStream; 
import java.awt.Cursor.*;

public class qc_rpitc extends cl_rbase
{
    JComboBox cmbPRDTP;                     /** ComboBox for Product Type */
	JTextField txtINVNO;                    /** JTextField to display Invoice No */
	private String strPREVDT;               /** Variable for previous date*/
	private String strFILNM;                /** String for generated Report File Name*/
    private int intRECCT = 0;               /** Integer for counting the records fetched from DB.*/
	private FileOutputStream fosREPORT;	    /** File output Stream for file handling.*/
    private DataOutputStream dosREPORT;     /** Data output Stream for generating Report File.*/
    final int L_intLMRGN = 3;
    
    BigDecimal LM_NPF,LM_NPT;
    //String LM_INTRCL ="00";
	String L_strTSTTP = "0103"; // composite certification Test
	//String LM_SYSCD = "PR" ; // for Production
	String L_strCMPNM = "SUPREME PETROCHEM LIMITED ";   /** Variable for company name*/
	String L_strLABNM= "QUALITY CONTROL LABORATORY ";   /** Variable for laboratory name*/
	String L_strDOCNM = "PRODUCT TEST CERTIFICATE";     /** Variable for document name*/
	boolean flgSPCFL = false;
	boolean flgTXTFL = false;
	boolean flgREPFL = true;
	//String LM_DFLSRL ="00000";
	String strTXFMT;
	String L_strISODOC1,L_strISODOC2,L_strISODOC3;      /** Variable for handling ISO documents*/
	String L_strDATE,L_strSUBHEAD;
    String L_strQCATP,L_strPRDTP,L_strLOTNO,L_strRCLNO,L_strPRDCD,L_strPRDDS,L_strUOMDS,L_strQPRCD;
	String L_strRMKDS="",L_strSTSFL,L_strPRDGR = "",L_strPRGDS,L_strQPRDS,L_strTSMDS,L_strCLSDT;
	String LM_NPFVL,LM_NPTVL,L_strTXTVL,L_strRSLVL,L_strCMPFL,L_strFLDNM,L_strGRPCD="";
	//String LM_STOPT = "CNT"; // continuous stationary
	
	ResultSet L_RSLSET,M_rstRSSET,M_rstRSLSET,M_rstRSLCNT;	
	PreparedStatement pstmtTST,pstmtINV,pstmtISS,pstVLDINV,pstCNTCDT;
    java.util.StringTokenizer LM_STRTKN;
	
	int count;
	    
    //String L_strPRDGR;

    qc_rpitc()
    {
        super(2);
	    try
        {
            setMatrix(20,8);
        
            M_pnlRPFMT.setVisible(true);
		    
		    add(new JLabel("Product Type"),3,3,2,1,this,'L');
		    add(cmbPRDTP = new JComboBox(),3,4,1,1,this,'L');
		    cmbPRDTP.addItem("01 Polystyrene");
		    cmbPRDTP.addItem("02 Speciality Polystyrene");
		
		    add(new JLabel("Invoice No."),5,3,1,1,this,'L');
	        add(txtINVNO = new TxtLimit(8),5,4,1,1,this,'L');
	        
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));  // Convert Into Local Date Format
            M_calLOCAL.add(Calendar.DATE,-1);                              // Decrease Date by 1 from Login Date
            strPREVDT = M_fmtLCDAT.format(M_calLOCAL.getTime());           // Assign Date to Variable 
            M_txtFMDAT.setText(strPREVDT);
                    
	        crtPRESTM();
	    }    
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"GUI Designing");
	    }
    }   // end of constructor
    
    /**
	 * super class Method overrided to enhance its funcationality, to enable & disable components 
	 * according to requriement.
	*/
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);				
		txtINVNO.setEnabled(L_flgSTAT);
		//M_txtFMDAT.setText(strPREVDT);
		//M_txtTODAT.setText(strPREVDT);
	}
	
	public void focusGained(FocusEvent L_fe)
    {
	    if(L_fe.getSource().equals (txtINVNO))
	    {
			L_strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			L_strQCATP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			System.out.println("L_strPRDTP : "+L_strPRDTP);
				
	    }	
    }
    
    public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
						
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
		    setCursor(cl_dat.M_curWTSTS_pbst);
		    
		    if(M_objSOURC == txtINVNO)
 	        {
				try
 	            {
					M_strSQLQRY = "Select distinct IVT_INVNO,IVT_INVDT,IVT_CNSCD from mr_Ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_invno is not null and length(ivt_invno) >0 order by IVT_INVDT DESC";
 	                M_strHLPFLD = "txtINVNO";
 	                //System.out.println(M_strSQLQRY);
                    cl_hlp(M_strSQLQRY,2,1,new String[]{"Invoice No.","Invoice Date","Consignee code"},3,"CT");
				}
				catch(Exception L_EX)
    			{
    			    setMSG(L_EX ," F1 help..");    		    
    			} 
 	        }
 	        setCursor(cl_dat.M_curDFSTS_pbst);
		}
		
		if (L_KE.getKeyCode()== L_KE.VK_ENTER)
        {
            setCursor(cl_dat.M_curWTSTS_pbst);
		}
		
	}    // end of key pressed
	
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
				
		if(M_strHLPFLD == "txtINVNO")
		{			
			txtINVNO.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();				
		}		
	}
	
	void exePRINT()
	{
		if (!vldDATA())
		{				
			setMSG("Please Enter Invoice No. Or Press F1 for Help..",'N');
			return;
		}
		try
		{
            if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpitc.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpitc.doc";
			if(exePTC())
			{			
			
			    if(dosREPORT !=null)
				    dosREPORT.close();
			    if(fosREPORT !=null)
				    fosREPORT.close();
			    //if(intRECCT == 0)
			    //{
				//    setMSG("Data not found for the given Inputs..",'E');
				//    return;
			    //}
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			    {					
				    if (M_rdbTEXT.isSelected())
				        doPRINT(strFILNM);
				    else 
			        {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+ strFILNM); 
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
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Product Test Certificate"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				    }
			    }
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}	
	}
	
	/*  Method to generate the Header Part of the Report  */ 
	private void repHEAD()
	{
	   try
	   {  
	        cl_dat.M_intLINNO_pbst = 0;
		    cl_dat.M_PAGENO += 1;    
		    //dosREPORT.writeBytes("\n\n\n");
	      if(M_rdbTEXT.isSelected())
	      {    
	            cl_dat.M_PAGENO+=1;
                prnFMTCHR(dosREPORT,M_strBOLD);
                prnFMTCHR(dosREPORT,M_strCPI12);
                dosREPORT.writeBytes(padSTRING('L'," ",30));
	            dosREPORT.writeBytes(padSTRING('C',L_strCMPNM,30));
	            //dosREPORT.writeBytes(padSTRING('C',"\n\n\n",30));
	            dosREPORT.writeBytes("\n");
				count++;
                dosREPORT.writeBytes("\n");
				count++;
	            dosREPORT.writeBytes(padSTRING('L'," ",30));
	            dosREPORT.writeBytes(padSTRING('C',L_strLABNM,30));
	            dosREPORT.writeBytes("\n");
				count++;
	            dosREPORT.writeBytes("\n");
				count++;
	            dosREPORT.writeBytes(padSTRING('L'," ",30));
	            dosREPORT.writeBytes(padSTRING('C',L_strDOCNM,30));
	            dosREPORT.writeBytes("\n"); 
				count++;
	            dosREPORT.writeBytes("\n");
				count++;
                L_strISODOC1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXPTC","DOC1");
			    L_strISODOC2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXPTC","DOC2");
			    L_strISODOC3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXPTC","DOC3");
			    dosREPORT.writeBytes(padSTRING('L'," ",30));
			    dosREPORT.writeBytes(padSTRING('C',L_strISODOC1,30)+"\n");
				count++;
			    dosREPORT.writeBytes(padSTRING('L'," ",32));
			    dosREPORT.writeBytes(padSTRING('C',L_strISODOC2,30)+"\n");
				count++;
			    dosREPORT.writeBytes(padSTRING('L'," ",32));
			    dosREPORT.writeBytes(padSTRING('C',L_strISODOC3,30));
                dosREPORT.writeBytes("\n");
				count++;
	            dosREPORT.writeBytes("\n");
				count++;
	            dosREPORT.writeBytes("\n");
				count++;
	            exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
	            exePRNLIN("GRADE   : ",0);
	            L_strPRDGR = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",L_strPRDCD.substring(0,4)+"00000A");
                dosREPORT.writeBytes(padSTRING('R',L_strPRDGR,6));
	            L_strPRGDS = padSTRING('R',L_strPRDDS.trim(),15);
                exePRNLIN(L_strPRGDS,2);
	            exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
                exePRNLIN("LOT NO  : ",0);
                dosREPORT.writeBytes(padSTRING('R',L_strLOTNO.trim(),10));
	            L_strDATE = " Date :" + cl_dat.M_strLOGDT_pbst;
                L_strDATE = padSTRING('L',L_strDATE,64);
                exePRNLIN(L_strDATE,1);
                exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
                dosREPORT.writeBytes("\n___________________________________________________________________________________________\n");
				count++;
				count++;
	            cl_dat.M_intLINNO_pbst++;
	            L_strSUBHEAD = "";
	            L_strSUBHEAD = padSTRING('R',"TEST PARAMETERS",30);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "UNITS",42);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "TEST-METHOD",62);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "SPECS",77);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "RESULTS",90);
                exePRNLIN("",1);
                exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
                exePRNLIN(L_strSUBHEAD,1);
	            exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
                dosREPORT.writeBytes(padSTRING('R'," ",9));
	            dosREPORT.writeBytes(padSTRING('L',"(ASTM Edition-2000)",50));
                exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
                dosREPORT.writeBytes("\n___________________________________________________________________________________________\n");
				count++;
				count++;
	            cl_dat.M_intLINNO_pbst++;
	            exePRNLIN("",1);
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	      }
	      else
	      {
				repHEAD1();
	            dosREPORT.writeBytes("<B><CENTER><PRE style = \"font-size : 15 pt \">");
		        dosREPORT.writeBytes(" SUPREME PETROCHEM LIMITED"+"\n");
		        //dosREPORT.writeBytes("\n\n\n\n");
				count++;
		        dosREPORT.writeBytes("</B></CENTER>");
		        dosREPORT.writeBytes("\n");
				count++;
		        dosREPORT.writeBytes("<B><CENTER><PRE style = \"font-size : 11 pt \">");
		        dosREPORT.writeBytes(" QUALITY CONTROL LABORATORY "+"\n");
				count++;
		        dosREPORT.writeBytes("</B></CENTER>");
		        dosREPORT.writeBytes("\n");
				count++;
		        dosREPORT.writeBytes("<B><CENTER><PRE style = \"font-size : 11 pt \">");
		        dosREPORT.writeBytes(" PRODUCT TEST CERTIFICATE "+"\n");
				count++;
		        dosREPORT.writeBytes("</B></CENTER>");
		        dosREPORT.writeBytes("\n");
				count++;
		        dosREPORT.writeBytes("<PRE style = \" font-size : 9 pt \">");
		        L_strISODOC1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXPTC","DOC1");
			    L_strISODOC2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXPTC","DOC2");
			    L_strISODOC3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXPTC","DOC3");
			    dosREPORT.writeBytes(padSTRING('L'," ",30));
			    dosREPORT.writeBytes(padSTRING('C',L_strISODOC1,30)+ "\n");
				count++;
			    dosREPORT.writeBytes(padSTRING('L'," ",30));
			    dosREPORT.writeBytes(padSTRING('C',L_strISODOC2,30)+ "\n");
				count++;
			    dosREPORT.writeBytes(padSTRING('L'," ",30));
			    dosREPORT.writeBytes(padSTRING('C',L_strISODOC3,30)); 
			    dosREPORT.writeBytes("\n");
				count++;
			    dosREPORT.writeBytes("\n");
				count++;
	            dosREPORT.writeBytes(padSTRING('L'," ",L_intLMRGN));
	            exePRNLIN("GRADE   : ",0);
	            L_strPRDGR = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",L_strPRDCD.substring(0,4)+"00000A");
                dosREPORT.writeBytes(padSTRING('R',L_strPRDGR,6));
	            L_strPRGDS = padSTRING('R',L_strPRDDS.trim(),15);
	            exePRNLIN(L_strPRGDS,1);
	            dosREPORT.writeBytes("\n");
				count++;
                dosREPORT.writeBytes(padSTRING('L'," ",L_intLMRGN));
	            exePRNLIN("LOT NO  : ",0);
                dosREPORT.writeBytes(padSTRING('R',L_strLOTNO.trim(),10));
	            L_strDATE = " Date :" + cl_dat.M_strLOGDT_pbst;
                L_strDATE = padSTRING('L',L_strDATE,64);
                exePRNLIN(L_strDATE,0);
                dosREPORT.writeBytes("<B>\n_________________________________________________________________________________________</B>\n");
				count++;
				count++;
	            cl_dat.M_intLINNO_pbst++;
	            L_strSUBHEAD = "";
	            L_strSUBHEAD = padSTRING('R',"TEST PARAMETERS",30);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "UNITS",42);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "TEST-METHOD",62);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "SPECS",77);
	            L_strSUBHEAD = padSTRING('R',L_strSUBHEAD + "RESULTS",90);
                dosREPORT.writeBytes(padSTRING('L'," ",L_intLMRGN));
                exePRNLIN(L_strSUBHEAD,1);
	            dosREPORT.writeBytes(padSTRING('R'," ",11));
	            dosREPORT.writeBytes(padSTRING('L',"(ASTM Edition-2000)",50));
                dosREPORT.writeBytes(padSTRING('L'," ",L_intLMRGN));
                dosREPORT.writeBytes("<B>\n_________________________________________________________________________________________</B>\n");
				count++;
				count++;
	            cl_dat.M_intLINNO_pbst++;
	      }
	      cl_dat.M_intLINNO_pbst+=15;
	    }  
	    catch(Exception L_EX)
	    {
		    setMSG(L_EX,"prnHEADER");
	    }
        
   }
   
   private void repHEAD1()
    {
	    try
        {			
			dosREPORT.writeBytes("<HTML><HEAD><Title>Product Specification Sheet</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
			dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
		}
        catch(Exception se)
        {
	     System.out.println("Error:in inner try "+se);
        }
    }	
    
    public static DataOutputStream createDataOutStr(FileOutputStream outfile)
    {
		DataOutputStream outStream = new DataOutputStream(outfile);
		return outStream;
    }
 
    public static FileOutputStream createFile(String strFile)
    {
		FileOutputStream outFile = null;
		try
		{
			File file = new File(strFile);
			outFile = new FileOutputStream(file);
       		return outFile;
		}
        catch(Exception ex) 
		{
			System.out.println("Error");
			return outFile;		
		}
    }
	
	/*  Method to fetch data from database & club it with Header in Data Output Stream  */
	private boolean getALLREC()
    {
	 int i = 0;
	 int L_CNT =0;
	 //L_strQCATP="01";
	 try
	 {
	    //System.out.println("001");
	    M_strSQLQRY  = "select * from QC_PSMST,CO_QPMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ="+"'"+L_strQCATP.trim() +"'";
		M_strSQLQRY += " AND PS_LOTNO ="+"'"+L_strLOTNO.trim() +"'";
		M_strSQLQRY += " AND PS_RCLNO ="+"'"+L_strRCLNO.trim() +"'";
		M_strSQLQRY += " AND PS_TSTTP ='"+L_strTSTTP.trim() +"'";
		M_strSQLQRY += " and qp_strdt <='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strCLSDT))+"'";
		M_strSQLQRY += " and isnull(qp_enddt,current_date) >='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strCLSDT))+"'";
		M_strSQLQRY += " AND QP_PRGFL like '%T%'";
		M_strSQLQRY += " AND QP_PRDCD = '"+L_strPRDCD.trim()+"'" ;
		M_strSQLQRY += " AND QP_SRLNO = '"+L_strGRPCD.trim() +"'" ;
        M_strSQLQRY += " and QP_ORDBY IS NOT NULL ORDER BY QP_ORDBY ";
        //System.out.println("003");
        //System.out.println(M_strSQLQRY);
    	M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		String L_TMP = "";
		if(M_rstRSSET ==null)
			return false;
		while(M_rstRSSET.next()) //for(int j=0;j<LM_QPRCNT;j++)
		{
				L_strUOMDS = "";
				L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),"");
				L_strUOMDS = nvlSTRVL(M_rstRSSET.getString("QP_UOMDS"),"-");
				L_strQPRDS = nvlSTRVL(M_rstRSSET.getString("QP_QPRDS"),"-");
				L_strTSMDS = nvlSTRVL(M_rstRSSET.getString("QP_TSMDS"),"-");
							
				flgSPCFL = false;
				L_strCMPFL ="";
				try
				{   
				    LM_NPF = null; 
					LM_NPT = null;
					if(!flgSPCFL)
						flgSPCFL =true;   
						LM_NPF = M_rstRSSET.getBigDecimal("QP_NPFVL");
						//System.out.println("LM_NPF = " + LM_NPF);
						//exePRNLIN(LM_NPF.toString(),0);
					    LM_NPT = M_rstRSSET.getBigDecimal("QP_NPTVL");
					    //System.out.println("LM_NPT = "+ LM_NPT);
					    //exePRNLIN(LM_NPT.toString(),0);
					if(LM_NPF !=null)
					    LM_NPF.setScale(3);
					if(LM_NPT !=null)
					    LM_NPT.setScale(3);
					L_strTXTVL = M_rstRSSET.getString("QP_TXTVL");
					//System.out.println("L_strTXTVL ="+ L_strTXTVL);	 
					L_strCMPFL = M_rstRSSET.getString("QP_CMPFL");	 
					//System.out.println("L_strCMPFL ="+L_strCMPFL);
				}
				catch(SQLException L_SE)
				{
					 System.out.println("Error 3"+L_SE.toString());
				}
				L_strFLDNM = "PS_"+L_strQPRCD.trim()+"VL";
				//System.out.println("L_strFLDNM = "+ L_strFLDNM);
				L_strRSLVL = M_rstRSSET.getString(L_strFLDNM);
				//System.out.println("L_strRSLVL ="+ L_strRSLVL);
				if(L_strQPRCD.trim().equals("MOI"))
			    {
					if(L_strRSLVL !=null)
						L_strRSLVL = setNumberFormat(Double.parseDouble(L_strRSLVL),3);
						//System.out.println("L_strRSLVL1 ="+ L_strRSLVL);
				}
				if(L_strRSLVL ==null)
					L_strRSLVL = "-";
				if(L_strTSMDS.trim().length() == 0)
					L_strTSMDS = padSTRING('R',"-",15);
				else
					L_strTSMDS = padSTRING('R',L_strTSMDS,20);
				    L_strQPRDS = padSTRING('R',L_strQPRDS,30);
				    //System.out.println("HHHH");
                    exePRNLIN("",1);
                    dosREPORT.writeBytes(padSTRING('L'," ",3));
                    exePRNLIN(L_strQPRDS,0);
                    //System.out.println("JJJJ");
				    L_strUOMDS = padSTRING('R',L_strUOMDS,12);
                    exePRNLIN(L_strUOMDS,0);
                    exePRNLIN(L_strTSMDS,0);
				    L_TMP = ""; 
				    //System.out.println("KKKK");
				if((LM_NPF != null)&& (LM_NPT != null) && (L_strRSLVL != null))
				{
				    //System.out.println("aaa");
					if(L_strCMPFL.equals("Y"))
					{   
					    //System.out.println("jjj");
						if(L_strRSLVL.equals("-"))
						{   
						    //System.out.println("www");
							setMSG("All the result values are not available,test certificate cannot be generated..",'E');
							return false;
						}
						else
						{
							setMSG("",'N');
							if(!chkQPRRNG(LM_NPF.floatValue(),LM_NPT.floatValue(),L_strCMPFL,Float.valueOf(L_strRSLVL).floatValue()))
							{
								System.out.println("sss");
								return false;
							}
						}
					}
					else if((L_strCMPFL.equals("N"))&&(L_strTXTVL.length() >0))
					{
						System.out.println("system");
						flgTXTFL = true;
						L_strRSLVL = L_strTXTVL;
					}
				}
				//System.out.println("sss" +L_strSTSFL );
				
				
				if(!L_strSTSFL.equals("2")) // if not a prime grade
				{
                       L_TMP = padSTRING('R',"-",14); 
				}
				else
				{
					if((LM_NPF == null)&&(LM_NPT == null))	
						{
                            L_TMP = padSTRING('R',"-",11);
						}
						else if(LM_NPF.floatValue() == 0.0)
						{
							if(LM_NPT.floatValue() == 0.0)
							{
                                L_TMP = padSTRING('R',"-",11);
							}
							else
							{
                                L_TMP = padSTRING('R',LM_NPT.intValue() + " max",11);
							}
						}
						else
						{
							if(LM_NPT.floatValue()== 0.0)
							{
                                L_TMP =padSTRING('R',LM_NPF.intValue()+" min",11);
							}
							else 
							{
                                if(LM_NPF.floatValue()== LM_NPT.floatValue())
									L_TMP = padSTRING('R',LM_NPF.floatValue()+"",11);
								else
								L_TMP = padSTRING('R',LM_NPF.floatValue() + "-"+LM_NPT.floatValue(),11);
							}
						}
					}
		            exePRNLIN(L_TMP,0);
                    dosREPORT.writeBytes(padSTRING('L',L_strRSLVL,10)+"\n");
					count++;
                    System.out.println("L_CNT" +L_CNT );
                    intRECCT++;
				    L_CNT++;
			    }
			    
	        }
            catch(Exception L_IE)
	        {
			System.out.println(L_IE.toString());
			}
	        if(L_CNT >0)
	 	        return true;
	        else
	        {
		    setMSG("Data not found",'E');
		    return false;
	        }
    }
    
    private boolean exeREP()
    {
 	        cl_dat.M_intLINNO_pbst = 0;
	        repHEAD();
	        if(!getALLREC())
		    return false;
	    try
	    {
            exePRNLIN("",1);
            prnFMTCHR(dosREPORT,M_strBOLD);
            dosREPORT.writeBytes("_________________________________________________________________________________________\n");
			count++;
            exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
		    exePRNLIN("",2);
            exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
            exePRNLIN("REMARK - ",0);
            prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(!L_strSTSFL.equals("2")) // if not prime grade
          		L_strRMKDS = "";        // remark input has been blocked.not required         
			else
			{
				if(flgSPCFL)
					L_strRMKDS ="The above lot meets specified requirements.";
				else
					L_strRMKDS = "";
			}
			exePRNLIN(L_strRMKDS.trim(),0);
		 	exePRNLIN("",2);
			exePRNLIN(padSTRING('L'," ",L_intLMRGN),0);
			exePRNLIN("Note : System generated report,hence signature not required.",1);
			
			for(int i=count;i<55;i++)
			{
				dosREPORT.writeBytes("\n");
				count++;
			}
			if(txtINVNO.getText().length()==8)
				exePRNLIN("Invoice Ref : "+txtINVNO.getText().substring(1),1);
			for(int i=count;i<64;i++)
			{
				dosREPORT.writeBytes("\n");
			}
			/*dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("\n");
            */
			prnFMTCHR(dosREPORT,M_strEJT);
			count=0;
		}
        catch(Exception L_IE)
		{
			System.out.println("error IO");	
		}	
	    return true;
    }
    
    private void exePRNLIN(String L_PRNSTR, int L_LINCTR)
    {
        try
        {
	        dosREPORT.writeBytes(L_PRNSTR);
             cl_dat.M_intLINNO_pbst+= L_LINCTR;
            for (int i=1;i<=L_LINCTR;i++)
             {
				dosREPORT.writeBytes("\n");
				count++;
	         }
        }
        catch (IOException L_IE)
        {}
    }
    
    private boolean exeINVVLD(String LP_INVNO)
    {
    	try
	    {
		    /* LM_STRQRY = "select count(*) L_CNT from MR_IVTRN";
		       LM_STRQRY += " Where isnull(IVT_INVNO,'') ='"+LP_INVNO.trim()+"'"; // for classified lots
		       LM_STRQRY += " AND isnull(IVT_STSFL,'') <>'X'"; // for classified lots
		       M_rstRSLSET = cl_dat.ocl_dat.exeSQLQRY(LM_STRQRY,"SP","ACT");*/
		    pstVLDINV.setString(1,LP_INVNO.trim());
		    M_rstRSLSET = pstVLDINV.executeQuery();
		    if(M_rstRSLSET == null)
			   System.out.println("Null Result Set");
		    else
		    {
			    while(M_rstRSLSET.next())
			    {
				   int L_CNT = M_rstRSLSET.getInt("L_CNT");
				   if(L_CNT > 0)
				   return true;
				   else
				   return false;
			    }
		    }
	    }
	    catch(SQLException L_SE)
	    {
		    System.out.println("Error 5"+L_SE.toString());
		    return false;
	    }
	    return false;
    }
    
    private boolean exePTC()
    {
	    int L_CNT =0;
	    try
	    {   System.out.println("AAA");
		    String L_STRQRY="";
		    String L_INVNO ="";
		    String L_LADNO="";
		    int L_RECCNT =0;
			//L_strPRDTP = "01";
		    java.util.Vector<String> L_VTRLOT = new java.util.Vector<String>();
		    fosREPORT = new FileOutputStream(strFILNM);
		    System.out.println(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    setMSG("Report Creation in Progress .. please wait",'N');
		    setCursor(cl_dat.M_curWTSTS_pbst);
		    // selecting the loading advice no. and group code
		    L_INVNO = txtINVNO.getText().trim();
		    /*L_STRQRY ="SELECT IVT_LADNO,PT_GRPCD from MR_IVTRN,CO_PTMST WHERE IVT_INVNO ='"+L_INVNO.trim() +"'";
		    L_STRQRY +=" and IVT_CNSCD = PT_PRTCD and PT_PRTTP ='C' AND IVT_STSFL <>'X'";
		    L_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(L_STRQRY,"SP","ACT");*/
		    pstmtINV.setString(1,L_INVNO.trim());
		    System.out.println("BBB");
		    L_RSLSET = pstmtINV.executeQuery();
		    if(L_RSLSET != null)
		    if(L_RSLSET.next())
		    {
			    L_LADNO = nvlSTRVL(L_RSLSET.getString("IVT_LADNO"),"");
			    L_strGRPCD = nvlSTRVL(L_RSLSET.getString("PT_GRPCD"),"");
		    }
		    // creating the LOT Vector
		    if(L_VTRLOT != null)
			    L_VTRLOT.removeAllElements();
			System.out.println("CCC");
	       	L_STRQRY = "SELECT distinct(IST_LOTNO + IST_RCLNO)L_LTRCL from FG_ISTRN WHERE IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSNO ='"+L_LADNO.trim()+"' and IST_STSFL <>'X'";
	       	//System.out.println(" L_STRQRY ="+L_STRQRY);
		    L_RSLSET = cl_dat.exeSQLQRY2(L_STRQRY);
		        
		    //pstmtISS.setString(1,L_LADNO.trim());
		    //System.out.println("NNN");
		    //L_RSLSET = cl_dat.exeSQLQRY1(L_STRQRY);
		    //L_RSLSET = pstmtISS.executeQuery();
		    //System.out.println("GGG");
		    if(L_RSLSET != null) 
		    {   //System.out.println("fff");
		        while(L_RSLSET.next())
		        {
			        System.out.println("OOO");
			        L_VTRLOT.addElement(nvlSTRVL(L_RSLSET.getString("L_LTRCL"),""));
			    }
		    }
		    L_STRQRY = " SELECT LT_LOTNO,LT_RCLNO,PR_PRDCD,PR_PRDDS,PR_STSFL,CONVERT(varchar,lt_clstm,103) L_CLSDT from PR_LTMST,CO_PRMST ";
		    L_STRQRY += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO + LT_RCLNO IN(";
		    
		    //System.out.println("L_VTRLOT.size =" + L_VTRLOT.size());
						
		    for(int i=0;i<L_VTRLOT.size();i++)
		    { 
		        System.out.println("DDD"+ i +" = "+ L_VTRLOT.elementAt(i).toString().trim());
				
			    if(i !=L_VTRLOT.size()-1)
				    L_STRQRY +="'"+L_VTRLOT.elementAt(i).toString().trim()+"',";
			    else
			        L_STRQRY +="'"+L_VTRLOT.elementAt(i).toString().trim()+"')";
		    }
			 System.out.println("DDD333");
			 
		    L_STRQRY += " AND LT_PRDTP ='"+L_strPRDTP.trim()+"'";
			 System.out.println("DDD311");
		    L_STRQRY += " AND LT_CLSFL = '9'"; // for classified lots
			 System.out.println("DDD3");
		    L_STRQRY += " AND LT_PRDCD = PR_PRDCD and SUBSTRING(LT_PRDCD,7,1)='0' and LT_STSFL <> 'X'";
			
		    System.out.println("MMM" + L_STRQRY);
		    L_RSLSET = cl_dat.exeSQLQRY3(L_STRQRY);
			qc_rpptc objRPPTC=new qc_rpptc(M_strSBSCD);
		    //repHEAD1();
		    if(L_RSLSET != null)
		    while(L_RSLSET.next())
		    {
			    L_CNT ++;
			    L_strCLSDT ="";
			    L_strLOTNO = nvlSTRVL(L_RSLSET.getString("LT_LOTNO"),"");
			    //System.out.println(L_strLOTNO);
			    L_strRCLNO = nvlSTRVL(L_RSLSET.getString("LT_RCLNO"),"");
			    L_strPRDCD = nvlSTRVL(L_RSLSET.getString("PR_PRDCD"),"");
			    L_strPRDDS = nvlSTRVL(L_RSLSET.getString("PR_PRDDS"),"");
				//System.out.println("PR_PRDDS " +L_strPRDDS);
				pstmtINV.setString(1,L_INVNO.trim());
				//System.out.println("BBB");
				ResultSet L_RSLSET1 = pstmtINV.executeQuery();
				if(L_RSLSET1 != null)
				if(L_RSLSET1.next())
				{
				    //L_LADNO = nvlSTRVL(L_RSLSET1.getString("IVT_LADNO"),"");
				    L_strGRPCD = nvlSTRVL(L_RSLSET1.getString("PT_GRPCD"),"");
				}
				L_RSLSET1.close();
				//System.out.println("L_strGRPCD "+L_strGRPCD);
			    pstCNTCDT.setString(1,L_strGRPCD.trim());
			    pstCNTCDT.setString(2,L_strPRDCD.trim());
			    M_rstRSLCNT = pstCNTCDT.executeQuery();
				//System.out.println("L_strCLSDT " +L_strCLSDT);
			    if(M_rstRSLCNT != null && M_rstRSLCNT.next())
			    {
					if(M_rstRSLCNT.getInt("L_CNT")==0)
						L_strGRPCD="00000";
			    }
				objRPPTC.M_rdbHTML.setSelected(false);
				objRPPTC.M_rdbTEXT.setSelected(false);
				if(M_rdbHTML.isSelected())
				   objRPPTC.M_rdbHTML.setSelected(true);
				if(M_rdbTEXT.isSelected())
				   objRPPTC.M_rdbTEXT.setSelected(true);
				//System.out.println("L_strGRPCD : "+L_strGRPCD);
				System.out.println(L_strQCATP+" / "+txtINVNO.getText()+" / "+L_strLOTNO+" / "+L_strLOTNO+" / "+L_strRCLNO+" / "+L_strGRPCD);
				objRPPTC.getDATA(L_strQCATP,txtINVNO.getText(),L_strLOTNO,L_strLOTNO,L_strRCLNO,L_strGRPCD,L_strPRDCD,dosREPORT);
			    /*L_strSTSFL = nvlSTRVL(L_RSLSET.getString("PR_STSFL"),"");
			    L_strCLSDT = nvlSTRVL(M_fmtLCDAT.format(L_RSLSET.getDate("L_CLSDT")).toString(),"");
				System.out.println("L_strCLSDT " +L_strCLSDT);
		        //int L_RECCNT = cl_dat.ocl_dat.getRECCNT("CO","ACT","SELECT count(*) from co_cdtrn where cmt_cgmtp ='SYS' and cmt_cgstp ='QCXXTCG' and cmt_codcd ='"+L_strGRPCD.trim()+"' AND CMT_CCSVL ='"+L_strPRDCD.trim()+"'");
			    if(L_RECCNT ==0)
			    L_strGRPCD ="00000";
			    if(!flgREPFL)
			    {
				    setCursor(cl_dat.M_curDFSTS_pbst);
				    return false;
			    }
			    else
			    {
				    if(exeREP())
				    {
					    flgREPFL = true;
				    }
				    else
				    {
					    flgREPFL = false;
					    setMSG("Specifications out of Range, cannot generate test certificate",'E');
				    }
			    }*/
		}
		flgREPFL=true;
	}
	catch(SQLException L_SE)
	{
		System.out.println("Error 6"+L_SE.toString());
		return false;
	}
	catch(Exception L_IE)
	{
		System.out.println("Error"+L_IE.toString());
		return false;
	}
	if(flgREPFL)
	{
		if(L_CNT >0)
			return true;
		else
		{
			setMSG("Data not found",'E');
			return false;
		}
	}
	else
		return false;
    }
 
    public boolean chkQPRRNG(float L_NPFVL,float L_NPTVL,String LP_CMPFL,float L_QPRVL)
    {
	    // if parameter is going out of range, it will return false.
	     //System.out.println("zz888z");
	    if (LP_CMPFL.trim().equals("Y"))
	    {   
	        if ((L_NPFVL != 0) && (L_NPTVL == 0))
	        {   
	             //System.out.println("z666zz");
	            if (L_QPRVL < L_NPFVL)
			    return false;
	        }
	        else if ((L_NPFVL == 0) && (L_NPTVL != 0))
	        {
	             //System.out.println("zz00z");
	            if (L_QPRVL > L_NPTVL)
	            return false;
	        }
	        else
	        {
	             //System.out.println("z33zz");
	            if (( L_QPRVL < L_NPFVL) || (L_QPRVL > L_NPTVL))
	            return false;
            }
		}
	    return true; // within range 
    }
          
    /*  Method to validate Product Type, Market Type, Sale Type  */	        
    boolean vldDATA()
	{
	    if(txtINVNO.getText().length()==0)
	    {
	        setMSG("Please enter Invoice No. Or Press F1 to select from list..",'N');
	        txtINVNO.requestFocus();
	        return true;
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
	            
    public static String getCURTIM()
    {
		String L_STRQRY;
		ResultSet L_RSLSET;
	
		String L_CURTM = "";
		try
		{
			L_STRQRY = "select current_time SP_CURTM from CO_SPTRN";
			L_RSLSET = cl_dat.exeSQLQRY(L_STRQRY);
			if(L_RSLSET.next())
			{
				L_CURTM = L_RSLSET.getString("SP_CURTM").toString().trim().substring(0,8);
				L_RSLSET.close();
			}
		}
		catch (Exception L_EX)
		{
			System.out.println("Exception : " + L_EX);
		}
        return L_CURTM;
	}

    private void crtPRESTM()
    {
		try
		{
			pstmtINV = 	cl_dat.M_conSPDBA_pbst.prepareStatement(
						"SELECT IVT_LADNO,PT_GRPCD from MR_IVTRN,CO_PTMST WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO = ? "+
						" and IVT_CNSCD = PT_PRTCD and PT_PRTTP ='C' AND isnull(IVT_STSFL,'') <>'X'");
	
			pstmtISS = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"SELECT distinct(IST_LOTNO + IST_RCLNO) L_LTRCL from FG_ISTRN WHERE IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSNO = ? and isnull(IST_STSFL,'') <>'X'");
			
			pstVLDINV = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"select count(*) L_CNT from MR_IVTRN "+
						" Where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_INVNO,'') =? "+ 
						 " AND isnull(IVT_STSFL,'') <>'X'");
			pstCNTCDT = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"SELECT count(*) L_CNT from co_cdtrn where cmt_cgmtp ='SYS' and cmt_cgstp ='QCXXTCG' and cmt_codcd = ? AND CMT_CCSVL = ? ");	
	
	 	}
		catch(Exception e)
		{
			System.out.println("Error in crtPRESTM : " + e.toString());
		}
    }
  
}   // end of class

