/*
System Name   : Finished Goods Inventory Management System
Program Name  : Despatch Summary Report
Program Desc. : Gives Despatch Summary Report according to the Selected Sale Type
Author        : Mr. Deepal N. Mehta
Date          : 5th May 2001
Version       : FIMS 1.0

Modifications 

Modified By    : Ms. A M. Kulkarni.
Modified Date  : 27/12/06
Modified det.  :
Version        : FIMS 2.0
*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
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
import java.io.*; 
import java.awt.Cursor.*;

public class fg_rpdsw extends cl_rbase implements MouseListener
{
	JComboBox cmbWRHTP;                     /** ComboBox for WareHouse Type */
	JTextField txtPRDTP;                    /** JTextField to display Product Type */
	JLabel lblWRHTP,lblPRDCG,lblPRDTP;
	JRadioButton rdbSPECG, rdbALLCG;
	ButtonGroup chkGRP;
	JOptionPane L_OPTNPN;
	cl_JTable tblSALTBL;
	
	private String strPREVDT;               /** Variable for previous date*/
	private String strFILNM;                /** String for generated Report File Name*/
    private int intRECCT = 0;               /** Integer for counting the records fetched from DB.*/
	private FileOutputStream fosREPORT;	    /** File output Stream for file handling.*/
    private DataOutputStream dosREPORT;     /** Data output Stream for generating Report File.*/
    
    String M_strSQLQRY,L_strPRDTP,L_strPRTYP;
	String L_strWRHTP,L_strPRDCD,L_strPRDDS,L_strSTRCNT,L_strDUMST,L_strSUBPD,L_strPRSGR,L_strPRMGR;
	String L_strMGRDS,L_strSGRDS,L_strCODCD;
	
	String L_strPRMGR1 = "";
	String L_strPRDCD1 = "";
	String L_strPRSGR1 = "";
	
	String L_strPRMGR2 = "";
	String L_strPRDCD2 = "";
	String L_strPRSGR2 = "";
	
	String L_strTOTPRQT = "";
	String L_strTOTSGQT = "";
	String L_strTOTMGQT = "";
	String L_strTOTGTQT = "";
	

    int L_intSALTP_MAX = 20;

	String L_strPRDCDQT[];
	String L_strPRSGRQT[];
	String L_strPRMGRQT[];
	String L_strGRTOTQT[];
	
	String L_strSALTYP[];
	String L_strSALDSC[];
	
	StringBuffer L_PRNSTR;

	int L_intLMRGN=0;
	int L_intROWCNT = 8;
	int i,j,k,l,L_intSLRWCT,L_intCOUNT;
	double L_dblISSQT = 0;
	boolean L_flgERRFL = false;
	boolean L_flgTBLHDR = true;
       
	fg_rpdsw()
	{
	    super(2);
	    try
	    {
	        setMatrix(20,8);
	        L_OPTNPN = new JOptionPane();
		    chkGRP = new ButtonGroup();
		    
		    M_pnlRPFMT.setVisible(true);
		    
		    add(new JLabel("WareHouse Type"),3,3,2,1,this,'L');
		    add(cmbWRHTP = new JComboBox(),3,4,1,.8,this,'L');
		    cmbWRHTP.addItem("01");
		    	
	        add(new JLabel("Product Category"),4,3,2,1,this,'L');
	        add(rdbSPECG = new JRadioButton("Specific"),4,4,1,1,this,'L');
	        add(rdbALLCG = new JRadioButton("All"),4,5,1,1,this,'L');
	        chkGRP.add(rdbSPECG);
	        chkGRP.add(rdbALLCG);
	        
	        add(new JLabel("Product Type"),6,3,1,1,this,'L');
	        add(txtPRDTP = new TxtLimit(6),6,4,1,1,this,'L');
	        	        	        	               
	        String[] L_strSALHD = {"Status","Sale Type","Description"};
		    int[] L_intCOLSZ = {50,70,110};
		    //LM_SALTBL = crtTBLPNL(pnlMAIN,L_SALHD,8,7,3,10,8,L_intCOLSZ,new int[] {0});
		    tblSALTBL = crtTBLPNL1(this,L_strSALHD ,10,8,3,6,2.5,L_intCOLSZ,new int[]{0});
		   		       		   
		    int L_intROWCNT = 8;
		             
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));  // Convert Into Local Date Format
            M_calLOCAL.add(Calendar.DATE,-1);                              // Decrease Date by 1 from Login Date
            strPREVDT = M_fmtLCDAT.format(M_calLOCAL.getTime());           // Assign Date to Variable 
            M_txtFMDAT.setText(strPREVDT);
                
        }    
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"GUI Designing");
	    }
	}  // end of constructor
	
		
	/* super class Method overrided to enhance its functionality, to enable & disable components 
       according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{
         super.setENBL(L_flgSTAT);
		 M_txtFMDAT.setText(strPREVDT);
		 M_txtTODAT.setText(strPREVDT);
		//  M_txtFMDAT.setText("11/12/2006");
		//  M_txtTODAT.setText("12/12/2006");
		 
		 getSALTP();
	}
	
	//fetches Sale Type code from CO_CDTRN table 
	private void getSALTP()
	{ 
		try
		{			
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS'";
            M_strSQLQRY += " AND CMT_CGSTP = 'MR00SAL' and CMT_CODCD < '20' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			i=0;
			while(M_rstRSSET.next())
			{
			    //System.out.println(i +"= "+  M_rstRSSET.getString("CMT_CODCD"));
				tblSALTBL.setValueAt(new Boolean(true),i,0);
				tblSALTBL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"0"),i,1);
				tblSALTBL.setValueAt(String.valueOf(M_rstRSSET.getString("CMT_SHRDS")),i,2);
				i++;
		    }
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);	 
		} 
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
	    super.actionPerformed(L_AE);
	    if(L_AE.getSource().equals(rdbSPECG))
	    {
			if(rdbSPECG.isSelected())
			{
				txtPRDTP.setEnabled(true);
				txtPRDTP.requestFocus();
			}
		}
		else if(L_AE.getSource().equals(rdbALLCG))
		{
			if(rdbALLCG.isSelected())
			{
				txtPRDTP.setEnabled(false);
			}
		}
	}
	
	/* method to be executed when event is fired on press of a key */ 
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
						
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
		    setCursor(cl_dat.M_curWTSTS_pbst);
		    
		    if(M_objSOURC == txtPRDTP)
 	        {
 	                try
 	                {
 	                    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPRD'";
 	                    M_strHLPFLD = "txtPRDTP";
 	                    System.out.println(M_strSQLQRY);
                        cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Type","Description"},2,"CT");
 	                }
 	                catch(Exception L_EX)
    			    {
    				    setMSG(L_EX ," F1 help..");    		    
    			    } 
 	        }
 	        setCursor(cl_dat.M_curDFSTS_pbst);
		}
		else if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
		    setCursor(cl_dat.M_curWTSTS_pbst);
		   		   
		    if(M_objSOURC == cmbWRHTP)
		    {
			    rdbALLCG.requestFocus();
		    }
		    else if(M_objSOURC == rdbALLCG)
		    {
				txtPRDTP.setEnabled(false);
		    }
		    else if(M_objSOURC == rdbSPECG)
		    {
				txtPRDTP.setEnabled(true);
				txtPRDTP.requestFocus();
		    }
		    setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/* method to be executed when event is fired on click of a mouse */ 
	public void mouseClicked(MouseEvent L_ME)
	{
		if(M_objSOURC == rdbSPECG)
		{
			if(rdbSPECG.isSelected())
			{
				txtPRDTP.setEnabled(true);
				txtPRDTP.requestFocus();
			}
		}
		if(M_objSOURC == (rdbALLCG))
		{
			if(rdbALLCG.isSelected())
			{
				txtPRDTP.setEnabled(false);
			}
		}
	}
	
	/* 	Method for execution of help for Memo Number Field. */
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPRDTP")
		{			
			txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst);
							
		}
	}
	
	void exePRINT()
	{
		if (!vldDATA())
		{				
			setMSG("Please Enter Product Code Or Press F1 for Help..",'N');
			return;
		}
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rpdsw.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpdsw.doc";				
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Summary Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}	
	}
	
	/* Method to get all records */
	private void getALLREC()
	{ 
	    try
	    {
	        fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT  = 0 ;
	        cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
   		    
			L_strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
			L_strPRDTP = txtPRDTP.getText().toString().trim();
			String L_ISSQT = "";
			j=0;
			k=0;
			L_intCOUNT=0;
			
                        L_strPRDCDQT = new String[L_intSALTP_MAX];
                        L_strPRSGRQT = new String[L_intSALTP_MAX];
                        L_strPRMGRQT = new String[L_intSALTP_MAX];
                        L_strGRTOTQT = new String[L_intSALTP_MAX];
            for(int i=0;i<L_intSALTP_MAX;i++)
			{
				L_strPRDCDQT[i] ="0";
				L_strPRSGRQT[i] ="0";
				L_strPRMGRQT[i] ="0";
				L_strGRTOTQT[i] ="0";
			}
			L_strTOTPRQT = "0";
			L_strTOTSGQT = "0";
			L_strTOTMGQT = "0";
			L_strTOTGTQT = "0";
			
			L_strPRMGR1 = "";  // Previous Product Type
			L_strPRDCD1 = "";  // Previous Product Description
			L_strPRSGR1 = "";  // Previous Product Sub Type
			
			L_strPRMGR2 = "";  // Previous Product Type
			L_strPRDCD2 = "";  // Previous Product Description
			L_strPRSGR2 = "";  // Previous Product Sub Type
			
			
			L_intSLRWCT = tblSALTBL.getRowCount();
            L_strSALTYP = new String[L_intSALTP_MAX];
            L_strSALDSC = new String[L_intSALTP_MAX];

            for(int i=0;i<L_intSALTP_MAX;i++)
			{
                L_strSALTYP[i] ="";
                L_strSALDSC[i] ="";
			}

			j=0;
			L_intCOUNT=0;
                        int L_intRUNSTP = 0;
			/**
			 * The following for loop assigns selected Sale Type to the String Array.
			 * i.e L_strSALTYP[] & L_strSALDSC[]
			 */
             for(i=0; i <= (L_intSLRWCT- 1); i++)
             {
				if(tblSALTBL.getValueAt(i,0).toString().trim().equals("true"))
                {
                     L_intRUNSTP = Integer.parseInt(tblSALTBL.getValueAt(i,1).toString().trim())-1;
                     L_strSALTYP[L_intRUNSTP] = tblSALTBL.getValueAt(i,1).toString().trim();
                     L_strSALDSC[L_intRUNSTP] = tblSALTBL.getValueAt(i,2).toString().trim();
				     L_intCOUNT++;
                     L_strSTRCNT = getSTRCNT(L_strSALTYP[L_intRUNSTP]);
                }
             }
			int k = L_strSTRCNT.length();
			
			L_strSTRCNT = L_strSTRCNT.substring(0,k-1).toString().trim();
			L_strSTRCNT = L_strSTRCNT + ")";
			
			L_strPRMGR = "";
			L_strPRSGR = "";
			L_strPRDCD = "";
			L_strCODCD = "";  
			L_dblISSQT = 0;
			
			for(l = 1;l <= L_intCOUNT;l++)
			{
				L_strPRDCDQT[l] = "0";
				L_strPRSGRQT[l] = "0";
				L_strPRMGRQT[l] = "0";
				L_strGRTOTQT[l] = "0";
			}

			
			M_strSQLQRY  = "Select SUBSTRING(IST_PRDCD,1,2) L_strPRDTP,SUBSTRING(IST_PRDCD,1,4) L_strSUBPD,";
			M_strSQLQRY += "IST_PRDCD,CMT_CODCD,sum(IST_ISSQT) L_ISSQT from FG_ISTRN,CO_CDTRN where";
            M_strSQLQRY += " CMT_CGMTP='SYS' and CMT_CGSTP='MR00SAL' and IST_SALTP=CMT_CODCD";
			M_strSQLQRY += " and IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+"' and '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"' ";
			M_strSQLQRY += " and IST_SALTP in "+L_strSTRCNT+" and IST_STSFL='2' ";
			if(txtPRDTP.getText().toString().trim().length()==2 && rdbSPECG.isSelected())
				M_strSQLQRY += " and IST_PRDTP = '"+txtPRDTP.getText().toString().trim()+"'";
			M_strSQLQRY += " group by SUBSTRING(IST_PRDCD,1,2),SUBSTRING(IST_PRDCD,1,4),IST_PRDCD,CMT_CODCD";
			M_strSQLQRY += " order by L_strPRDTP,L_strSUBPD,IST_PRDCD,CMT_CODCD";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			
			boolean L_1STFL = true;
			boolean L_flgEOF = false;
			
			if(M_rstRSSET.next())
			{
			    //System.out.println("01");
			    prnHEADER(); //gets the Header of the report
				getTBLHDR(); // gets the Header of the Table
				L_strCODCD  = M_rstRSSET.getString("CMT_CODCD").trim();
				L_strPRDCD2 = M_rstRSSET.getString("IST_PRDCD").trim();
				L_strPRSGR2 = M_rstRSSET.getString("L_strSUBPD").trim();
				L_strPRMGR2 = M_rstRSSET.getString("L_strPRDTP").trim();
				System.out.println(L_strPRMGR2);
				L_dblISSQT  = M_rstRSSET.getDouble("L_ISSQT");
				//System.out.println("02");
				while(!L_flgEOF)
			    {
				    //System.out.println("03");
					L_strPRDCD = L_strPRDCD2;
					//System.out.println(L_strPRDCD);
					L_strPRSGR = L_strPRSGR2;
					//System.out.println(L_strPRSGR);
					L_strPRMGR = L_strPRMGR2;
					//System.out.println(L_strPRMGR);
					
					/*if(cl_dat.M_intLINNO_pbst >= 68)
					{
					    System.out.println("04");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						prnHEADER(); //gets the Header of the report
						getTBLHDR(); // gets the Header of the Table
						System.out.println("05");
					}*/
					
					if(L_1STFL)
					{
					    //System.out.println("06");
						L_strPRDCD1 = L_strPRDCD;
						//System.out.println(L_strPRDCD1);
						L_strPRSGR1 = L_strPRSGR;
						//System.out.println(L_strPRSGR1);
						L_strPRMGR1 = L_strPRMGR;
						//System.out.println(L_strPRMGR1);
						L_1STFL = false;
						//System.out.println("07");
					}
					prnGRPHDR("MG",4);
					L_strPRMGR1 = L_strPRMGR;
					while((L_strPRMGR).equals(L_strPRMGR1) && !L_flgEOF)
					{
					    //System.out.println("08");
						prnGRPHDR("SG",6);
						L_strPRMGR = L_strPRMGR2;
						L_strPRMGR1 = L_strPRMGR;
						while((L_strPRMGR+L_strPRSGR).equals(L_strPRMGR1+L_strPRSGR1) && !L_flgEOF)
						{
						    //System.out.println("09");
							L_strPRMGR = L_strPRMGR2;
							L_strPRSGR = L_strPRSGR2;
							L_strPRMGR1 = L_strPRMGR;
							L_strPRSGR1 = L_strPRSGR;
							while((L_strPRMGR+L_strPRSGR+L_strPRDCD).equals(L_strPRMGR1+L_strPRSGR1+L_strPRDCD1) && !L_flgEOF)
							{
							    //System.out.println("10");
							    int L_CCSVL = Integer.parseInt(L_strCODCD)-1;
								//System.out.println("L_CCSVL = "+L_CCSVL);	
								L_strPRDCDQT[L_CCSVL] = setNumberFormat((Double.parseDouble(L_strPRDCDQT[L_CCSVL])+ L_dblISSQT),3);
								//System.out.println("11");
								L_strPRSGRQT[L_CCSVL] = setNumberFormat((Double.parseDouble(L_strPRSGRQT[L_CCSVL])+ L_dblISSQT),3);
								//System.out.println("12");
								L_strPRMGRQT[L_CCSVL] = setNumberFormat((Double.parseDouble(L_strPRMGRQT[L_CCSVL])+ L_dblISSQT),3);
								L_strGRTOTQT[L_CCSVL] = setNumberFormat((Double.parseDouble(L_strGRTOTQT[L_CCSVL])+ L_dblISSQT),3);
								L_strTOTPRQT = setNumberFormat((Double.parseDouble(L_strTOTPRQT)+L_dblISSQT),3);
								//System.out.println("11");
								L_strTOTSGQT = setNumberFormat((Double.parseDouble(L_strTOTSGQT)+L_dblISSQT),3);
								//System.out.println("11");
								L_strTOTMGQT = setNumberFormat((Double.parseDouble(L_strTOTMGQT)+L_dblISSQT),3);
								L_strTOTGTQT = setNumberFormat((Double.parseDouble(L_strTOTGTQT)+L_dblISSQT),3);
								
								//System.out.println(L_strTOTPRQT);
								//System.out.println(L_strTOTSGQT);
								//System.out.println("mmmmmmmmm"+L_strPRDCDQT[L_CCSVL]);
								
								if(!M_rstRSSET.next())
								{
								    //System.out.println("112");
									L_flgEOF = true;
									//System.out.println("L_flgEOF = "+L_flgEOF);		
									break;
								}
								
								//System.out.println("121");		
								L_strPRMGR2 = M_rstRSSET.getString("L_strPRDTP").trim();
								//System.out.println("12111");		
								L_strPRSGR2 = M_rstRSSET.getString("L_strSUBPD").trim();
								//System.out.println("12441");		
								L_strPRDCD2 = M_rstRSSET.getString("IST_PRDCD").trim();
								//System.out.println("1221");		

								L_dblISSQT = M_rstRSSET.getDouble("L_ISSQT");
								//System.out.println("1211");		
								L_strCODCD = M_rstRSSET.getString("CMT_CODCD").trim();
								
								L_strPRMGR = L_strPRMGR2;
								L_strPRSGR = L_strPRSGR2;
								L_strPRDCD = L_strPRDCD2;
								//System.out.println("12");	
								intRECCT=1;
							}
							//System.out.println("121");		
							prnGRPTOT("PR",L_strTOTPRQT,"N");
							intGRPTOT("PR");
							//System.out.println("13");
						}
						prnGRPTOT("SG",L_strTOTSGQT,"B");
						intGRPTOT("SG");
						//System.out.println("14");
					}
					prnGRPTOT("MG",L_strTOTMGQT,"B");
					intGRPTOT("MG");
					//System.out.println("15");
				}
				//System.out.println("16");	
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			//System.out.println("17");
			prnFOOTR();
			setCursor(cl_dat.M_curDFSTS_pbst);
			//System.out.println("18");		
		}
		catch(ArrayIndexOutOfBoundsException L_EX)
		{
			//System.out.println(L_EX.getClass()+L_EX.getMessage());
			//System.out.println(L_EX.toString());
			setMSG(L_EX,"getALLREC");
		}
		catch(Exception L_EX)
		{
			//System.out.println("lllllllllll");
			setMSG(L_EX,"getALLREC");
		}
     }
		
	 
	 private void prnGRPHDR(String LP_GRPCT, int LP_MRGVL)
	 {
	    try
	    {   if (M_rdbTEXT.isSelected())
	        {
			prnFMTCHR(dosREPORT,M_strCPI17);
			prnFMTCHR(dosREPORT,M_strBOLD);
			}
			String L_GRPDS = "";
			if (LP_GRPCT.equals("MG"))
			{
			    System.out.println("SS");
				L_GRPDS = cl_cust.ocl_cust.getMNPRD(L_strPRMGR1);
			}
		    else if (LP_GRPCT.equals("SG"))
		    {
			    L_GRPDS = cl_cust.ocl_cust.getSBPRD(L_strPRSGR1);
			    System.out.println("AA");
			}	
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",LP_MRGVL));
			dosREPORT.writeBytes(padSTRING('R',L_GRPDS,(28-LP_MRGVL)));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 2;
			/*if(cl_dat.M_intLINNO_pbst >= 68)
			{
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				//crtLINE(136);
				//prnFMTCHR(dosREPORT,M_strEJT);				
				prnHEADER(); //gets the Header of the report
				getTBLHDR(); // gets the Header of the Table
			}*/			
	    }
	    catch(Exception L_EX)
	    {
		setMSG(L_EX,"prnGRPHDR");
		}
	 }
	 
	 private void prnGRPTOT(String LP_GRPCT,String LP_TOTXXQT,String LP_BLDFL)
	 {
	    try
	    {
	        if (M_rdbTEXT.isSelected())
	        {
	           prnFMTCHR(dosREPORT,M_strCPI17);
	           prnFMTCHR(dosREPORT,M_strBOLD);
	        }
			L_PRNSTR = new StringBuffer("");
			//System.out.println("AS");
			if (LP_GRPCT.equals("PR"))
			{
				L_strPRDDS = cl_cust.ocl_cust.getPRDDS(L_strPRDCD1);
				dosREPORT.writeBytes(padSTRING('R',"",10));
				L_PRNSTR.append(padSTRING('R',L_strPRDDS,18));
                       for(int l=0;l<L_intSALTP_MAX;l++)
                           if (!L_strSALTYP[l].equals(""))
                               L_PRNSTR.append(padSTRING('L',getDASH(L_strPRDCDQT[l]),15));
			}
			else if (LP_GRPCT.equals("SG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst += 1;
				L_strSGRDS = cl_cust.ocl_cust.getSBPRD(L_strPRSGR1);
				dosREPORT.writeBytes(padSTRING('R',"",6));
				L_PRNSTR.append(padSTRING('R',"TOTAL " + L_strSGRDS,22));
                       for(int l=0;l<L_intSALTP_MAX;l++)
                           if (!L_strSALTYP[l].equals(""))
                               L_PRNSTR.append(padSTRING('L',getDASH(L_strPRSGRQT[l]),15));
			}
			else if (LP_GRPCT.equals("MG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst += 1;
				L_strMGRDS = cl_cust.ocl_cust.getMNPRD(L_strPRMGR1);
				dosREPORT.writeBytes(padSTRING('R',"",4));
				L_PRNSTR.append(padSTRING('R',"TOTAL " + L_strMGRDS,24));
                       for(int l=0;l<L_intSALTP_MAX;l++)
                           if (!L_strSALTYP[l].equals(""))
                               L_PRNSTR.append(padSTRING('L',getDASH(L_strPRMGRQT[l]),15));
			}
			else if (LP_GRPCT.equals("GT"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst += 1;
				dosREPORT.writeBytes(padSTRING('R',"",4));
				L_PRNSTR.append(padSTRING('R',"GRAND TOTAL",24));
                       for(int l=0;l<L_intSALTP_MAX;l++)
                           if (!L_strSALTYP[l].equals(""))
                               L_PRNSTR.append(padSTRING('L',getDASH(L_strGRTOTQT[l]),15));
				}
			L_PRNSTR.append(padSTRING('L',getDASH(LP_TOTXXQT),18));
					
			dosREPORT.writeBytes(L_PRNSTR.toString());
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			if(cl_dat.M_intLINNO_pbst >= 65)
			{
			    //dosREPORT.writeBytes("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				System.out.println("Page No. :" + cl_dat.M_PAGENO);
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				//crtLINE(136);
				prnFMTCHR(dosREPORT,M_strEJT);				
				prnHEADER(); //gets the Header of the report
				getTBLHDR(); // gets the Header of the Table
			}
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX,"prnGRPTOT");
			System.out.println("Exception: "+L_PRNSTR.toString());
		}
	 }
	 
	 private String getDASH(String LP_DSHSTR)
	 {
		try
		{
			if(LP_DSHSTR == null || LP_DSHSTR.equals("0"))
				LP_DSHSTR = "-";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDASH");
		}
		return LP_DSHSTR;
	 }
	 
	 private void intGRPTOT(String LP_GRPCT)
	 {
	    try
	    {
			if (LP_GRPCT.equals("PR"))
			{
                for(l = 0;l < L_intSALTP_MAX;l++)
					L_strPRDCDQT[l] = "0";
				    L_strTOTPRQT = "0";
			    	L_strPRDCD1 = L_strPRDCD;
			}
			else if (LP_GRPCT.equals("SG"))
			{
                for(l = 0;l < L_intSALTP_MAX;l++)
					L_strPRSGRQT[l] = "0";
				    L_strTOTSGQT = "0";
				    L_strPRSGR1 = L_strPRSGR;
			}
			else if (LP_GRPCT.equals("MG"))
			{
                for(l = 0;l < L_intSALTP_MAX;l++)
					L_strPRMGRQT[l] = "0";
				    L_strTOTMGQT = "0";
				    L_strPRMGR1 = L_strPRMGR;
			}
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX,"intGRPTOT");
	    }
	 }
	 
	 /*  Method to validate data */
	 boolean vldDATA()
	 {
	    if(txtPRDTP.getText().length()==0)
	    {
	        setMSG("Please enter Product Type Or Press F1 to select from list..",'N');
	        txtPRDTP.requestFocus();
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
	 private String getSTRCNT(String SALTP)
	 {
	      if(j == 0)                           
                L_strDUMST = "(";
                L_strDUMST = L_strDUMST + "'" + SALTP + "'" + ",";
                j++;
		        return L_strDUMST;
	 }
	 
	 private void getTBLHDR()
	 {
	    try
	    {
	        if (M_rdbTEXT.isSelected())
	        {
	           prnFMTCHR(dosREPORT,M_strCPI17);
	           prnFMTCHR(dosREPORT,M_strBOLD);
	        }
			dosREPORT.writeBytes(padSTRING('R'," ",10));
			dosREPORT.writeBytes(padSTRING('R',"Grade",18));
            for(i =0;i < L_intSALTP_MAX;i++)
                  if (!L_strSALTYP[i].equals(""))
                  dosREPORT.writeBytes(padSTRING('L',L_strSALDSC[i],15));
			dosREPORT.writeBytes(padSTRING('L',"Total Qty.",18));
			dosREPORT.writeBytes("\n-------------------------------------------------------------------------------------------------------------------------------------------------------");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			//crtLINE(136);
			cl_dat.M_intLINNO_pbst += 2;
	    }
	    catch(Exception L_EX)
	    {
			System.out.println(L_EX);
	    }
	 }   
	
	/* Method to get the Header of the Report */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			//cl_dat.M_PAGENO += 1;										
			dosREPORT.writeBytes("\n");			
			if(M_rdbTEXT.isSelected())
			{
			    prnFMTCHR(dosREPORT,M_strCPI17);
				prnFMTCHR(dosREPORT,M_strBOLD);						
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,70));
				dosREPORT.writeBytes(padSTRING('L',"Report Date :" + cl_dat.M_strLOGDT_pbst,60) + "\n");														
				dosREPORT.writeBytes(padSTRING('R',"Sale TypeWise Despatch Summary Report for :" + M_txtFMDAT.getText()+" to "+ M_txtTODAT.getText(),111));							
				dosREPORT.writeBytes(padSTRING('L',"Page No : " + cl_dat.M_PAGENO,10));			
			}
			else
			{	
			    							
				dosREPORT.writeBytes("<B><CENTER><PRE style =\" font-size : 9 pt \">");
				dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n\n");							
				dosREPORT.writeBytes("</B></CENTER>");				
				dosREPORT.writeBytes(padSTRING('R',"Sale TypeWise Despatch Summary Report for :" + M_txtFMDAT.getText()+" to "+ M_txtTODAT.getText(),68));				
				dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,20) );
				dosREPORT.writeBytes("</FONT></PRE>");				
				dosREPORT.writeBytes("<PRE style =\" font-size : 8 pt \">");
			}
			dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------------------------------------------------------\n");												
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	
	private void prnFOOTR()
	 {
	    try
	    {
			if(cl_dat.M_intLINNO_pbst >= 61)
			//prnFMTCHR(dosREPORT,M_strEJT);
			dosREPORT.writeBytes ("\n");
			prnGRPTOT("GT",L_strTOTGTQT,"B");
			dosREPORT.writeBytes("\n-----------------------------------------------------------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			//crtLINE(136);
			//for(int i = 1;i <= 5;i++)
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",20));//margin
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",32));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY  ",25));
			dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)  ",35));
			dosREPORT.writeBytes ("\n");
			//crtLINE(136);
			dosREPORT.writeBytes("\n System generated report, hence signature not required \n");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			cl_dat.M_intLINNO_pbst += 9;
			//prnFMTCHR(dosREPORT,M_strEJT);				
			//prnFMTCHR(dosREPORT,M_strNOCPI17);
			dosREPORT.close();
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX,"prnFOOTR");
	    }
	 }
	
	
}	//end of main class
	