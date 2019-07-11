/*
System Name   : Finished Goods Inventory Management System
Program Name  : Unclassifed Stocks Details
Program Desc. : Unclassifed Stocks Details

Author        : MMr. Zaheer Khan
Date          : 16.08.2006
Version       : FIMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/



import java.sql.ResultSet;import java.util.Date;
import java.awt.event.KeyEvent;import javax.swing.JComponent;import javax.swing.JComboBox; 
import java.io.FileOutputStream;import java.io.DataOutputStream;import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;

class fg_rpucl extends cl_rbase
{			
	private ButtonGroup btgRPTST;    
	private JRadioButton rdbCURNT;	 
	private JRadioButton rdbDYOPN;	 	 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intPAGENO=0;					 /** Hashtable for storing different Receipt Types */  
	private Hashtable<String,String> hstRCTTP = new Hashtable<String,String>();/** Hashtable for storing different Main Product Types*/
	private Hashtable<String,String> hstMNGRP = new Hashtable<String,String>();/** Hashtable for storing different Sub Product Types*/
	private Hashtable<String,String> hstSBGRP = new Hashtable<String,String>();/** Hashtable for storing different Package Types*/
    	private Hashtable<String,String> hstPKGTP = new Hashtable<String,String>();
	private Hashtable<String,String> hstGENCD = new Hashtable<String,String>();	
	private String strISODC1,strISODC2,strISODC3;									
		
	public fg_rpucl()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			String L_strCODCD="";
			btgRPTST=new ButtonGroup();		
			add(rdbCURNT=new JRadioButton("Current Stock",true),4,3,1,2,this,'L');
			add(rdbDYOPN=new JRadioButton("Day Opening Stock"),5,3,1,2,this,'L');
			
			btgRPTST.add(rdbCURNT);
			btgRPTST.add(rdbDYOPN);

			M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ( 'ISOFGXXRPT','M"+cl_dat.M_strCMPCD_pbst+"COXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL')";
			M_strSQLQRY += " and ISNULL(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	

			if(M_rstRSSET != null)   
			{
		    	while(M_rstRSSET.next())
        		{   
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("MG"))
						hstMNGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,2),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("SG"))
						hstSBGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,4),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("FGXXPKG"))
						hstPKGTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("FGXXRTP"))
						hstRCTTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					else
						hstGENCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
			}
			strISODC1 = hstGENCD.get("FG_RPRCM01").toString();
			strISODC2 = hstGENCD.get("FG_RPRCM02").toString();
			strISODC3 = hstGENCD.get("FG_RPRCM03").toString();
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
	}
	
	public boolean vldDATA()
	{
		try
		{
						
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			//System.out.println("rec 1 = "+intRECCT);
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rpucl.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpucl.doc";
			getDATA();
		
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"UnClassified Stock Statement"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	
	private void getDATA()
	{
		
		try
		{
			/* Variables Declared to store PREVIOUS values of Resultset **/
			double L_dblPKGQT=0;
			double L_dblPKGTT=0;
			double L_dblTOTQT=0;
			double L_dblTOTPK=0;
			double L_dblLOTQT=0;
			double L_dblPRDQT=0;
			double L_dblGRTOT=0;
			double L_dblGRPTT=0;
			int L_intLOCCT=0;
			
			String L_strPRDTP="",L_strPRDTP1="";
			String L_strSUBPD="",L_strSUBPD1="";
			String L_strTPRCD="",L_strTPRCD1="";
			String L_strPKGTP="",L_strPKGTP1="";
			double[] dbaXPSDT = new double[2]; 
			double dblMTCQT = 0.000;
			double dblMTTQT = 0.000;
			String L_strMTCQT0 = "",L_strMTTQT0 = "";
			double L_dblRCTPK = 0;
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    //prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>UnClassified Stock Statement</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();			
    		
			setMSG("Report Generation is in Progress.......",'N');			   			
						
		
			//String L_UCLQT = "";
			String L_strUCLVAR = "";
			if(rdbCURNT.isSelected())
			{
				L_strUCLVAR = "ST_UCLQT";
			}
			else if(rdbDYOPN.isSelected())
			{
				L_strUCLVAR = "ST_DOUQT";
			}
			M_strSQLQRY  = "Select ST_LOTNO,ST_RCLNO,ST_TPRCD,ST_PKGTP,PR_PRDTP,SUBSTRING(ST_TPRCD,1,2) LM_PRDTP,SUBSTRING(ST_TPRCD,1,4) LM_SUBPD,";
            M_strSQLQRY += " sum("+L_strUCLVAR+") L_UCLQT,PR_PRDCD,PR_PRDDS,cmt_ncsvl from FG_STMST,CO_PRMST,CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and cmt_codcd= st_pkgtp and "+L_strUCLVAR+" > 0 AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_TPRCD=PR_PRDCD";
			 
            String L_DTBSTR = " PR_PRDTP,SUBSTRING(ST_TPRCD,1,2),SUBSTRING(ST_TPRCD,1,4),ST_TPRCD,ST_PKGTP,ST_LOTNO,ST_RCLNO,PR_PRDCD,PR_PRDDS,CMT_NCSVL";
            String L_DBSSTR = " PR_PRDTP,LM_PRDTP,LM_SUBPD,ST_TPRCD,ST_PKGTP,ST_LOTNO,ST_RCLNO";
			
			M_strSQLQRY += " group by " + L_DTBSTR;
			M_strSQLQRY += " order by " + L_DBSSTR;
            
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
          
		//	boolean L_1STFL = true;
		//	boolean L_EOF = false;
			
			//int i=0;
			System.out.println("M_strSQLQRY out>>"+M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				if(cl_dat.M_intLINNO_pbst >= 62)
				{
					dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------\n");		
					cl_dat.M_intLINNO_pbst = 0;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER(); //gets the Header of the report
				}
				L_strPRDTP = M_rstRSSET.getString("LM_PRDTP").trim();
				L_strSUBPD = M_rstRSSET.getString("LM_SUBPD").trim();
				L_strTPRCD = M_rstRSSET.getString("ST_TPRCD").trim();
				L_strPKGTP = M_rstRSSET.getString("ST_PKGTP").trim();
				L_dblRCTPK = M_rstRSSET.getDouble("L_UCLQT")/M_rstRSSET.getDouble("CMT_NCSVL");
				/////XPS details
				L_strMTCQT0 = "";L_strMTTQT0 = "";
				
				if(M_rstRSSET.getString("LM_PRDTP").equals("SX") && !M_rstRSSET.getString("ST_PKGTP").equals("99"))
				{
					dbaXPSDT[0]=0; dbaXPSDT[1]=0;
					//System.out.println(L_dblRCTPK+">>"+M_rstRSSET.getString("PR_PRDCD")+">>"+M_rstRSSET.getString("ST_PKGTP"));
					dbaXPSDT = getXPSDT(L_dblRCTPK,M_rstRSSET.getString("PR_PRDCD"),M_rstRSSET.getString("ST_PKGTP"));
					//System.out.println("out>>"+dbaXPSDT[0]+"|"+dbaXPSDT[1]);
					L_strMTCQT0 = setNumberFormat(dbaXPSDT[0],3);
					L_strMTTQT0 = setNumberFormat(dbaXPSDT[1],3);
					dblMTCQT += dbaXPSDT[0];
					dblMTTQT += dbaXPSDT[1];
				}
				
				/////////////////////////////////////////////
				
				
				if(!L_strPRDTP.equals(L_strPRDTP1))
				{
					//dosREPORT.writeBytes(padSTRING('R'," ",3));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if(L_intLOCCT>0)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes(padSTRING('R'," ",64));
						dosREPORT.writeBytes(padSTRING('R'," ",20));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,3),16));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTPK,3),16));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R'," ",64));
						dosREPORT.writeBytes(padSTRING('R'," ",20));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPKGQT,3),16));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPKGTT,3),16));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"  ",7));
						dosREPORT.writeBytes(padSTRING('R'," ",20));
						dosREPORT.writeBytes(padSTRING('R',"   Total  "+hstMNGRP.get(L_strPRDTP1).toString(),57));
						
						
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRDQT,3),16));
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst += 4;
						L_dblTOTQT=0;
						L_dblTOTPK=0;
						L_dblPKGQT=0;
						L_dblPKGTT=0;
						L_dblPRDQT=0;
						L_intLOCCT=0;
					}
					
					dosREPORT.writeBytes(padSTRING('R',hstMNGRP.get(L_strPRDTP).toString(),71));
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					
				}
				else
					dosREPORT.writeBytes(padSTRING('R'," ",3));
				if(!L_strSUBPD.equals(L_strSUBPD1))
				{
					dosREPORT.writeBytes("\n");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if(L_intLOCCT>0)
					{
						
						dosREPORT.writeBytes(padSTRING('R'," ",64));
						dosREPORT.writeBytes(padSTRING('R'," ",20));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,3),16));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTPK,3),16));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"  ",7));
						
						
						dosREPORT.writeBytes(padSTRING('R',"   Total  "+hstSBGRP.get(L_strSUBPD1).toString(),57));
						/////XPS details
						if((dblMTCQT+dblMTTQT)>0)
						{
	    					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblMTCQT,3),10));
	    					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblMTTQT,3),10));
	    				}
	    				else
	    					dosREPORT.writeBytes(padSTRING('L'," ",20));
	    				/////////////////////////////////////////////////////
	    				
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPKGQT,3),16));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPKGTT,3),16));
						dosREPORT.writeBytes("\n");cl_dat.M_intLINNO_pbst+= 1;
						
						/////XPS details
						if((dblMTCQT+dblMTTQT)>0)
						{
							dosREPORT.writeBytes(padSTRING('R'," ",64));
	    					dosREPORT.writeBytes(padSTRING('L',"Mt.Cube",10));
	    					dosREPORT.writeBytes(padSTRING('L',"MT",10));
	    					dosREPORT.writeBytes(padSTRING('L',"Sq.Mtr",16));
			   				dosREPORT.writeBytes(padSTRING('L',"Pcs",16));
			   				dosREPORT.writeBytes("\n");cl_dat.M_intLINNO_pbst+= 1;
	    					//dblMTCQT = 0;dblMTTQT = 0;
	    					
	    				}
	    				else
	    				{
	    					dosREPORT.writeBytes("\n");
			   				cl_dat.M_intLINNO_pbst+= 1;
			   			}
	    				///////////////////////////////////////
	    				
						L_dblPKGQT=0;
						L_dblPKGTT=0;
						L_dblTOTQT=0;
						L_dblTOTPK=0;
						L_intLOCCT=0;
						dblMTCQT = 0;
		    			dblMTTQT = 0;
						dosREPORT.writeBytes("\n");cl_dat.M_intLINNO_pbst += 1;
					}	
					dosREPORT.writeBytes(padSTRING('R'," ",7));
					dosREPORT.writeBytes(padSTRING('R',hstSBGRP.get(L_strSUBPD).toString(),71));
					cl_dat.M_intLINNO_pbst += 1;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
				}
				else
					dosREPORT.writeBytes(padSTRING('R'," ",7));
				if(!(L_strTPRCD+L_strPKGTP).equals(L_strTPRCD1+L_strPKGTP1))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("\n");
					if(L_intLOCCT>0)
					{
						
						dosREPORT.writeBytes(padSTRING('R'," ",64));
						dosREPORT.writeBytes(padSTRING('R'," ",20));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,3),16));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTPK,3),16));
						L_dblTOTQT=0;
						L_dblTOTPK=0;
						dosREPORT.writeBytes("\n\n");
						L_intLOCCT=0;
						cl_dat.M_intLINNO_pbst += 2;
					}	
					dosREPORT.writeBytes(padSTRING('R'," ",12));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PR_PRDDS"),20));
					cl_dat.M_intLINNO_pbst += 1;
					dosREPORT.writeBytes(padSTRING('R',hstPKGTP.get(L_strPKGTP).toString(),20));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
				}
				else
					dosREPORT.writeBytes(padSTRING('R'," ",42));
				
				dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_LOTNO"),12));
				
				if(M_rstRSSET.getString("LM_PRDTP").equals("SX") && !M_rstRSSET.getString("ST_PKGTP").equals("99"))
				{
					dosREPORT.writeBytes(padSTRING('L',L_strMTCQT0,10));
					dosREPORT.writeBytes(padSTRING('L',L_strMTTQT0,10));
				}
				else
					dosREPORT.writeBytes(padSTRING('R'," ",20));
				
				L_dblLOTQT=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_UCLQT"),"0"));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblLOTQT,3),16));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCTPK,3),16));
						
				L_strPKGTP1=L_strPKGTP;
				L_strTPRCD1=L_strTPRCD;
				L_strPRDTP1=L_strPRDTP;
				L_strSUBPD1=L_strSUBPD;
				
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				L_dblTOTQT+=L_dblLOTQT;
				L_dblTOTPK+=L_dblRCTPK;
				L_dblPKGQT+=L_dblLOTQT;
				L_dblPKGTT+=L_dblRCTPK;
				L_dblPRDQT+=L_dblLOTQT;
				L_dblGRTOT+=L_dblLOTQT;
				L_dblGRPTT+=L_dblRCTPK;
				intRECCT++;
				L_intLOCCT++;
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R'," ",64));
			dosREPORT.writeBytes(padSTRING('R'," ",20));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,3),16));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTPK,3),16));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"  ",7));
			dosREPORT.writeBytes(padSTRING('R'," ",20));
			dosREPORT.writeBytes(padSTRING('R',"   Total  "+hstSBGRP.get(L_strSUBPD1).toString(),57));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPKGQT,3),16));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPKGTT,3),16));
				
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"  ",7));
			dosREPORT.writeBytes(padSTRING('R'," ",20));
			dosREPORT.writeBytes(padSTRING('R',"   Total  "+hstMNGRP.get(L_strPRDTP1).toString(),57));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRDQT,3),16));
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes(padSTRING('R',"  ",7));
			dosREPORT.writeBytes(padSTRING('R'," ",20));
			dosREPORT.writeBytes(padSTRING('R',"   Grand Total  ",57));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT,3),16));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRPTT,3),16));
						
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();	
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 6;
			prnFOOTR();
			
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
		}
		catch(Exception L_EX)
		{
		
			setMSG(L_EX  + " GetDATA",'E');
		}
	}
	
	public double[] getXPSDT(double LP_PKGQT, String LP_PRDCD,String LP_PKGTP)
	{
		double[] LP_dbaXPSDT = new double[2];
		try
		{
			//LP_staXPSDT[0] = LP_PRDCD.substring(2,4);
			//System.out.println("PRDCD>>"+LP_PRDCD.substring(2,4));
			String L_strSQLQRY = "select cmt_nmp01,cmt_nmp02,cmt_chp02 from co_cdtrn where cmt_cgstp='FGXXPKG' and cmt_codcd='"+LP_PKGTP+"'";
			//System.out.println(L_strSQLQRY);
			java.sql.ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			double L_dblMTCQT = 0.000;
			double L_dblMTTQT = 0.000;
			if(L_rstRSSET != null)
			{	
				if(L_rstRSSET.next())
				{
					//LP_staXPSDT[1] = L_rstRSSET.getString("cmt_nmp01");
					//LP_staXPSDT[2] = L_rstRSSET.getString("cmt_nmp02");
					//LP_staXPSDT[3] = L_rstRSSET.getString("cmt_chp02");
					//System.out.println("1.."+L_rstRSSET.getString("cmt_nmp01"));
					//System.out.println("2.."+L_rstRSSET.getString("cmt_nmp02"));	
					//System.out.println("3.."+L_rstRSSET.getString("cmt_chp02"));	
					LP_dbaXPSDT[0] = LP_PKGQT * ((Double.parseDouble(L_rstRSSET.getString("cmt_chp02")) *Double.parseDouble(L_rstRSSET.getString("cmt_nmp01")) * Double.parseDouble(L_rstRSSET.getString("cmt_nmp02")))/(1000*1000*1000));
					LP_dbaXPSDT[1] = (Double.parseDouble(LP_PRDCD.substring(2,4))*LP_dbaXPSDT[0])/1000;
				}
			}
		}
		catch(Exception L_EX)
	    {
			setMSG(L_EX  + " getXPSDT",'E');
		}
		return LP_dbaXPSDT;
	}
	
	/**
	 Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",65));
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			dosREPORT.writeBytes("------------------------------\n");
			dosREPORT.writeBytes(padSTRING('L'," ",65));
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			dosREPORT.writeBytes(strISODC1+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",65));
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			dosREPORT.writeBytes(strISODC2+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",65));
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			dosREPORT.writeBytes(strISODC3+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",65));
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			dosREPORT.writeBytes("------------------------------\n");
			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst ,70));
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			dosREPORT.writeBytes("Report Date:"+ cl_dat.M_strLOGDT_pbst + "\n");	
			
			dosREPORT.writeBytes(padSTRING('R',"UnClassified Stock Statement as on '"+cl_dat.M_strLOGDT_pbst+"' at 07:00 Hrs.",70));			
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("     Target Grade                                   Lot No.                                 Total Qty     Total pkgs\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------\n");		
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst += 11;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
	/**
  	 *Method to Generate the Page Footer of the Report.
	*/
	private void prnFOOTR()
	{
		try
		{
			if(cl_dat.M_intLINNO_pbst >= 64)
			{
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
				prnHEADER();
			}
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes ("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('L'," ",10));//margin
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",40));
			dosREPORT.writeBytes(padSTRING('R',"CHECKED BY  ",40));
			dosREPORT.writeBytes(padSTRING('R',"H.O.D (MHD)  ",25));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------\n");		
			
			
			dosREPORT.writeBytes(" System generatede report, hence signature not required \n");

			cl_dat.M_intLINNO_pbst += 8;
		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		    {   
				prnFMTCHR(dosREPORT,M_strEJT);
				//prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}	
		}
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnFOOTR",'E');
		}	
	}
}
