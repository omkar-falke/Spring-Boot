/**
 * Program Name				: MR_QRDSS
 * Program Description		: Gives information regarding despatch schedules
 *							  The order of information depends on the category selected by User
 * Module Undertaken Date	: 09/04/2003
 * Module Completed Date	:
 */
/**
 * New Concept Used			: Order by information is stored in Hashtable which is retrieved
 *							  from database. i.e Column Name & Description are stored
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.sql.*;

public class mr_qrdss extends cl_rbase
{
	JRadioButton rdbDETAL,rdbSUMRY;
	
	ButtonGroup bgrSELEC;
	
	JButton btnWRK;
	
	JTextField txtDSPDT;
	
	JComboBox cmbORDBY;
	
	Hashtable<String,String> hstORDBY;
	Hashtable<String,String> hstREPHDR;
	Hashtable<String,String> hstCODDS;
	
	String strDSPDT,LP_DSPDT,strORDBY,strHSTVL,strMKTTP,strDORNO,strPRDCD,strPKGTP,strSTSFL;
	String strSRLNO;
	String strHDTRP = "Transporter";
	String strHDPRD = "Grade";
	String strHDDOR = "D.O. No.";
	String str1stLVL,str2ndLVL,str3rdLVL,str4thLVL,str5thLVL,str6thLVL;
	String str1stLVL1,str2ndLVL1,str3rdLVL1,str4thLVL1,str5thLVL1,str6thLVL1;
	String str1stLVL2,str2ndLVL2,str3rdLVL2,str4thLVL2,str5thLVL2,str6thLVL2;
	
	double dblTOT1LV = 0;
	double dblTOT2LV = 0;
	double dblTOT6LV = 0;
	double dblTOTALL = 0;
	double dblPNDQT = 0;
	double dblINVQT = 0;
	double dblDORQT = 0;
	double dblLADQT = 0;
	double dblDTRQT = 0; //represents dot_dorqt of table MR_DOTRN
	
	boolean flgHDRFL = false;
	
	FileOutputStream fosREPORT;
    DataOutputStream dosREPORT;
	String strFILNM;
	ResultSet LM_RSLSET,LM_RSLSET1;
	/**
	 *1.Screen Designing 
	 *2.Alongwith their descriptions.
	 */
	mr_qrdss()
	{
		super(1);
		try
		{
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    setMatrix(20,8);
		
			    hstORDBY = new Hashtable<String,String>();
				hstREPHDR = new Hashtable<String,String>();
				hstCODDS = new Hashtable<String,String>();
				bgrSELEC = new ButtonGroup();
				hstORDBY.put(strHDTRP,"pt_prtnm,dod_dspdt,dot_dorno,dot_prdds,dot_lryno,dot_pkgtp");
				hstORDBY.put(strHDPRD,"dot_prdds,dod_dspdt,dot_dorno,pt_prtnm,dot_lryno,dot_pkgtp");
				hstORDBY.put(strHDDOR,"dot_dorno,dod_dspdt,dot_prdds,pt_prtnm,dot_lryno,dot_pkgtp");
				hstREPHDR.put("dod_dspdt","Date");
				hstREPHDR.put("pt_prtnm","Transporter");
				hstREPHDR.put("dot_dorno","D.O. No.");
				hstREPHDR.put("dot_lryno","Lorry No.");
				hstREPHDR.put("dot_prdds","Grade");
				hstREPHDR.put("dot_pkgtp","Package Type");
				add(new JLabel("Date :"),3,3,1,1,this,'L');
				add(txtDSPDT =new TxtDate(),3,4,1,1,this,'L');
				
				add(btnWRK = new JButton("Rework"),3,5,1,1,this,'L');
				add(new JLabel("Order By : "),4,3,1,1,this,'L');
				add(cmbORDBY = new JComboBox(),4,4,1,1.1,this,'L');
				cmbORDBY.addItem( "Select");
				add(new JLabel("Report : "),5,3,1,1,this,'L');
				
				add(rdbDETAL = new JRadioButton("Detail",true),5,4,1,1,this,'L');
				add(rdbSUMRY = new JRadioButton("Summary",false),6,4,1,1,this,'L');
				bgrSELEC.add(rdbDETAL);
				bgrSELEC.add(rdbSUMRY);
				Enumeration L_HSTKEY = hstORDBY.keys();
				M_pnlRPFMT.setVisible(true);
				while(L_HSTKEY.hasMoreElements())
					cmbORDBY.addItem(L_HSTKEY.nextElement().toString().trim());
				
				
				txtDSPDT.setEnabled( false);
				btnWRK.setEnabled( false);
				cmbORDBY.setEnabled( false);
				rdbDETAL.setEnabled( false);
				rdbSUMRY.setEnabled( false);
				
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is constructor");
		}

	}
	/**
	 * Enabled the companents
	 */
	void setENBL(boolean L_flgSTAT)
	{
	    super.setENBL( L_flgSTAT);
	    txtDSPDT.setEnabled( false);
		btnWRK.setEnabled( false);
		cmbORDBY.setEnabled( false);
		rdbDETAL.setEnabled( false);
		rdbSUMRY.setEnabled( false);
	}
	/**
	 * 
	 * @param L_flgSTAT this function used for the remove the M_txtTODAT,M_txtFRDAT textFields
	 */
	 void removeENBL(boolean L_flgSTAT)
     {
        
           M_txtFMDAT.setVisible(false);
	     	M_txtTODAT.setVisible(false);
	    	M_lblFMDAT.setVisible(false);
		    M_lblTODAT.setVisible(false);
		    
		    
		    
	   }	
	 /**
	  * Action performed used for the extablished the actions on the components
	  * 
	  */
	 
	 public void actionPerformed(ActionEvent L_AE)
	 {
	    	super.actionPerformed(L_AE);
		try
		{
		    
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) /** Combo Opotion Remove the From Date & To Date\*/
			{
			    removeENBL(false);
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			    {    
				    txtDSPDT.setEnabled( false);
					btnWRK.setEnabled( false);
					cmbORDBY.setEnabled( false);
					rdbDETAL.setEnabled( false);
					rdbSUMRY.setEnabled( false);
			    }
			    else
			    {   
			        txtDSPDT.setText(cl_dat.M_strLOGDT_pbst);
			        txtDSPDT.setEnabled( true);
					btnWRK.setEnabled( true);
					cmbORDBY.setEnabled( true);
					rdbDETAL.setEnabled( true);
					rdbSUMRY.setEnabled( true);
			    }
				    
			}
			if(M_objSOURC==btnWRK)
			{
			    try
			    {
			        strDSPDT = txtDSPDT.getText().toString().trim();
			        String LP_DORNO="";
					this.setCursor(cl_dat.M_curWTSTS_pbst );
					setMSG("Reworking of data in progress",'N');
					
						updDODEL("", strDSPDT);
					
					
					
					setMSG("Data Updated",'N');
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}catch(Exception L_EX)
				{
					setMSG(L_EX,"btnWRK");
				}
			}
			
				
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is the action performed");
		    
		}
	 }
	 /**
	  * This is Keypressed to establish the events on the components
	  */
	  public void keyPressed(KeyEvent L_KE)
	    {
	        super.keyPressed(L_KE);
	        try
	        {
			        if (L_KE.getKeyCode() ==L_KE.VK_ENTER)
			        {
			            if(M_objSOURC ==txtDSPDT)
			            {
			                if(txtDSPDT.getText().trim().length() == 0)
			    			{
			    				setMSG("Enter From Date..",'E');
			    				txtDSPDT.requestFocus();
			    			}
			                else
			                    txtDSPDT.requestFocus();    
			                if(M_fmtLCDAT.parse(txtDSPDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			    			{
			    				setMSG("Date Should Not Be Grater Than Today..",'E');
			    				txtDSPDT.requestFocus();
			    			
			    			}
			                else
			                    cmbORDBY.requestFocus();
			                
			                
			            }
			        }
	        }catch(Exception L_EX)
	        {
	            setMSG(L_EX,"Key pressed");
	        }
	    }
	  /**
	   * 
	   * @param LP_DORNO This function used for the updated the MR_DOTRN table this is from cl_cust class
	   * @param LP_DSPDT
	   */
	 private void updDODEL(String LP_DORNO,String LP_DSPDT)
	 {
		try
		{
				int L_CNT = 0;
				double L_UPDQT = 0;
				boolean L_UPDFL = false;
				String L_MKTTP1 = "";
				String L_DORNO1 = "";
				String L_PRDCD1 = "";
				String L_PKGTP1 = "";
				String L_MKTTP = "";
				String L_DORNO = "";
				String L_PRDCD = "";
				String L_PKGTP = "";
				String L_SRLNO = "";
				String L_DSPDT = "";
				double L_DORQT = 0;
				double L_LADQT = 0;
				double L_DTRQT = 0;
				double L_INVQT = 0;
				String L_ADDSTR = "";
				if(LP_DORNO.trim().length() > 0)
				{
					L_ADDSTR += " and dod_dorno in ('"+LP_DORNO+"')";
				}
				if(LP_DSPDT.trim().length() > 0){
					L_ADDSTR += " and dod_dspdt <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DSPDT))+"'";
				}
						
				M_strSQLQRY = "Select dod_mkttp,dod_dorno,dod_prdcd,dod_pkgtp,dod_srlno,dod_dspdt,";
				M_strSQLQRY += "sum(dod_dorqt) dod_dorqt,sum(dod_ladqt) dod_ladqt from mr_dodel where";
				M_strSQLQRY += " DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND dod_dorqt > dod_ladqt and dod_stsfl != 'X'"+L_ADDSTR;
				M_strSQLQRY += " group by dod_mkttp,dod_dorno,dod_prdcd,dod_pkgtp,dod_srlno,dod_dspdt";
				M_strSQLQRY += " order by dod_mkttp,dod_dorno,dod_prdcd,dod_pkgtp,dod_srlno,dod_dspdt";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				while(M_rstRSSET.next())
				{
					L_MKTTP = M_rstRSSET.getString("dod_mkttp");
					L_DORNO = M_rstRSSET.getString("dod_dorno");
					L_PRDCD = M_rstRSSET.getString("dod_prdcd");
					L_PKGTP = M_rstRSSET.getString("dod_pkgtp");
					L_SRLNO = M_rstRSSET.getString("dod_srlno");
					L_DSPDT = M_rstRSSET.getString("dod_dspdt");
					L_DORQT = M_rstRSSET.getDouble("dod_dorqt");
					L_LADQT = M_rstRSSET.getDouble("dod_ladqt");
					if(!(L_MKTTP+L_DORNO+L_PRDCD+L_PKGTP).equals(L_MKTTP1+L_DORNO1+L_PRDCD1+L_PKGTP1)){
						M_strSQLQRY = "Select dot_dorqt,dot_invqt from mr_dotrn where";
						M_strSQLQRY += " DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND dot_mkttp='"+L_MKTTP+"' and dot_dorno='"+L_DORNO+"'";
						M_strSQLQRY += " and dot_prdcd='"+L_PRDCD+"' and dot_pkgtp='"+L_PKGTP+"'";
						ResultSet L_RSLSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_RSLSET1.next())
						{
							L_DTRQT = L_RSLSET1.getDouble("dot_dorqt");
							L_INVQT = L_RSLSET1.getDouble("dot_invqt");
						}else{
							System.out.println(L_MKTTP+"     "+L_DORNO+"     "+L_PRDCD+"     "+L_PKGTP);
							L_CNT++;
						}
						if(L_RSLSET1 != null)
							L_RSLSET1.close();
						L_MKTTP1= L_MKTTP;
						L_DORNO1= L_DORNO;
						L_PRDCD1= L_PRDCD;
						L_PKGTP1= L_PKGTP;
						L_UPDFL = true;
					}
					/**
					 * if this condition is true than dod_ladqt in mr_dodel is replaced
					 * with dod_dorqt for retrieved D.O. Ref. No. with status flag not
					 * equal to 'X'
					 */
					if(L_INVQT > 0)
					{
						if(L_UPDFL)
						{
							if(L_DTRQT == L_INVQT)
							{ 
								M_strSQLQRY = "Update MR_DODEL set dod_ladqt=dod_dorqt where";
								M_strSQLQRY += " DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND dod_mkttp='"+L_MKTTP+"' and dod_dorno='"+L_DORNO+"'";
								M_strSQLQRY += " and dod_prdcd='"+L_PRDCD+"' and dod_pkgtp='"+L_PKGTP+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								if(cl_dat.M_flgLCUPD_pbst)
									cl_dat.exeDBCMT("");
								else
								{
								    
								}
								L_UPDFL = false;
							}
						}
						/**
						 * if this condition is true than dod_ladqt in mr_dodel is replaced
						 * with dod_dorqt if dot_invqt >= dod_dorqt or replaced by 
						 * dod_dorqt - dot_invqt if dot_invqt < dod_dorqt
						 * for retrieved D.O. Ref. No. with status flag not
						 * equal to 'X'
						 */
						if(L_DTRQT > L_INVQT){
							L_UPDQT =  0;
							if(L_INVQT > L_DORQT){
								L_UPDQT = L_DORQT;
							}
							else if(L_INVQT <= L_DORQT)
							{
								L_UPDQT = L_INVQT;
							}
							L_INVQT -= L_UPDQT;
							M_strSQLQRY = "Update MR_DODEL set dod_ladqt=dod_ladqt+"+String.valueOf(L_UPDQT);
							M_strSQLQRY += " where DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND dod_mkttp='"+L_MKTTP+"' and dod_dorno='"+L_DORNO+"'";
							M_strSQLQRY += " and dod_prdcd='"+L_PRDCD+"' and dod_pkgtp='"+L_PKGTP+"'";
							M_strSQLQRY += " and dod_srlno='"+L_SRLNO+"' and";
							M_strSQLQRY += " dod_dspdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_DSPDT))+"'" ;
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst)
								cl_dat.exeDBCMT("");
							else
							{
							    
							}
						}
					}
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				System.out.println("No. of records not found in MR_DOTRN are: "+L_CNT);
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"the getprdcd");
		}
	 }
	 
	 
	 /**
	  * print the header of the report
	  *
	  */
	 
	 private void prnHEADER()
	 {
	     	try
			{
	     	   
	     	    strDSPDT = txtDSPDT.getText().toString().trim();
				strORDBY = cmbORDBY.getSelectedItem().toString().trim();
			
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n"+padSTRING('L'," ",110));
				crtLINE(30);
				dosREPORT.writeBytes("\n"+padSTRING('L'," ",140)+"\n"+padSTRING('L'," ",140)+"\n"+padSTRING('L'," ",140)+"\n");
				dosREPORT.writeBytes(padSTRING('L'," ",110));
				crtLINE(30);
				dosREPORT.writeBytes("\n"+padSTRING('R',"SUPREME PETROCHEM LTD.",25)+padSTRING('L',"Report Date :"+cl_dat.M_strLOGDT_pbst,115));
				if(rdbDETAL.isSelected())
					dosREPORT.writeBytes("\n"+padSTRING('R',strORDBY+"wise Despatch Schedule Detail as on "+strDSPDT,70)+padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,61)+"\n");
				else if(rdbSUMRY.isSelected())
					dosREPORT.writeBytes("\n"+padSTRING('R',strORDBY+"wise Despatch Schedule Summary as on "+strDSPDT,70)+padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,61)+"\n");
				crtLINE(140);
				dosREPORT.writeBytes("\n");
				StringBuffer L_1stLIN = new StringBuffer(padSTRING('R'," ",6));
				StringTokenizer stkREPHDR = new StringTokenizer(strHSTVL,",");
				if(rdbDETAL.isSelected())
				{
					int L_CNT = 0;
					if(stkREPHDR.countTokens() > 0)
					{
						while(stkREPHDR.hasMoreTokens())
						{
							String L_COLMNM = stkREPHDR.nextToken().toString();
							String L_HDRNM = hstREPHDR.get(L_COLMNM).toString();
							L_1stLIN.append(padSTRING('R',L_HDRNM,24));
							if(L_CNT < 1)
							{
								L_1stLIN.append("\n"+padSTRING('R'," ",6));
								cl_dat.M_intLINNO_pbst  += 1;
							}
							L_CNT++;
						}
					}
				}
				else if(rdbSUMRY.isSelected())
				{
					String L_COLMNM = stkREPHDR.nextToken().toString();
					String L_HDRNM = hstREPHDR.get(L_COLMNM).toString();
					L_1stLIN.append(padSTRING('R',L_HDRNM,25));
				}
				L_1stLIN.append(padSTRING('L',"Quantity",15)+"\n");
				dosREPORT.writeBytes(L_1stLIN.toString());
				crtLINE(140);
				dosREPORT.writeBytes("\n");
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				cl_dat.M_intLINNO_pbst  += 9;
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"prnHEADER");
			}
		}
	 
	 
	 
	 
	/* private boolean vldDATA()
	 {
	    try
	    {
             if(txtDSPDT.getText().trim().length() == 0)
 			{
 				setMSG("Enter From Date..",'E');
 				txtDSPDT.requestFocus();
 				return false;
 			}
             
             if(M_fmtLCDAT.parse(txtDSPDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
 			{
 				setMSG("Date Should Not Be Grater Than Today..",'E');
 				txtDSPDT.requestFocus();
 				return false;
 			}
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX,"This is getDATA");
	        return false;
	    }
	    return true;
	 }*/
	 /**This method is to print the data into document & HTML format and also print & fax & Mail*/
	 
	 
	    void exePRINT()
		{   
		    
			try
		{
		
		        int RECCT  = 0 ;
		     		    
	   		    setMSG("Report Generation in Process....." ,'N');
	   		    setCursor(cl_dat.M_curWTSTS_pbst);
	   		   if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_tetmn.html";
			    else if(M_rdbTEXT.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + "mr_tetmn.doc";	
								
				getDATA();
				
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
	    			    p  = r.exec("C:\\windows\\wordpad.exe " + strFILNM); 
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
					cl_eml ocl_eml = new cl_eml();				    
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Details"," ");
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					}
				}
				
	    			if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
				
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"Error.exescrn.. ");
			}	
		}
		/** Method to fetch data from DB & club it with Header in Data Output Stream  */
		  private void getDATA()
		 {
		      strDSPDT = txtDSPDT.getText().toString().trim();
				 strORDBY = cmbORDBY.getSelectedItem().toString().trim();
			try
			{   
				cl_dat.M_PAGENO = 1;
			    String strln="";
			    
			    //System.out.println("th file name is " +strFILNM);
			    fosREPORT = new FileOutputStream(strFILNM);
			    System.out.println("th file name is " +strFILNM);
			    dosREPORT = new DataOutputStream(fosREPORT);
			    System.out.println("th file name is " +strFILNM);
			    
			    int RECCT  = 0 ;
		        cl_dat.M_intLINNO_pbst=0;
	   		    setMSG("Report Generation in Process....." ,'N');
	   		    setCursor(cl_dat.M_curWTSTS_pbst);
	   		    if(M_rdbHTML.isSelected())
			    {
			        
			        dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
			        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
			        dosREPORT.writeBytes("</STYLE>");
				 }
				 else
					prnFMTCHR(dosREPORT,M_strCPI17);
	   		    prnHEADER();
	   		    
	   		
	   		    
			 boolean L_1STFL = true;
				boolean L_EOF = false;
				flgHDRFL = false;
				this.setCursor(cl_dat.M_curWTSTS_pbst);	
				//prnFMTCHR(dosREPORT,M_strNOCPI17);
				//prnFMTCHR(dosREPORT,M_strCPI10);
				//prnFMTCHR(dosREPORT,M_strCPI17);
			//	cl_dat.M_intLINNO_pbst = 69;
				cl_dat.M_PAGENO = 1;
				dblTOT1LV = 0;
				dblTOT2LV = 0;
				dblTOT6LV = 0;
				dblTOTALL = 0;
				dblPNDQT = 0;
				strHSTVL = hstORDBY.get(strORDBY).toString().trim();
				M_strSQLQRY = "Select "+strHSTVL+",(ifnull(dod_dorqt,0)-ifnull(dod_ladqt,0)) l_pndqt";
				M_strSQLQRY += " from mr_dotrn,mr_dodel,co_ptmst where DOD_CMPCD=DOT_CMPCD and dod_mkttp=dot_mkttp";
				M_strSQLQRY += " and dod_dorno=dot_dorno and dod_prdcd=dot_prdcd";
				M_strSQLQRY += " and dod_pkgtp=dot_pkgtp and DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(dod_dorqt,0) > ifnull(dod_ladqt,0)";
				M_strSQLQRY += " and dod_dspdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strDSPDT))+"'";
				M_strSQLQRY += " and dod_stsfl != 'X' and dot_trpcd=pt_prtcd and pt_prttp = 'T'";
				M_strSQLQRY += " order by "+strHSTVL;
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				System.out.println(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					
			  		str1stLVL2 = nvlSTRVL(M_rstRSSET.getString(1),"");
			  		str2ndLVL2 = M_rstRSSET.getString(2);
			  		str3rdLVL2 = nvlSTRVL(M_rstRSSET.getString(3),"");
					str4thLVL2 = nvlSTRVL(M_rstRSSET.getString(4),"");
			  		str5thLVL2 = nvlSTRVL(M_rstRSSET.getString(5),"");
					str6thLVL2 = nvlSTRVL(M_rstRSSET.getString(6),"");
					dblPNDQT = M_rstRSSET.getDouble(7);	
					/*StringTokenizer stkREPHDR = new StringTokenizer(strHSTVL,",");
					int L_CNT = 1;
					if(stkREPHDR.countTokens() > 0){
						while(stkREPHDR.hasMoreTokens()){
							String L_COLMNM = stkREPHDR.nextToken().toString();
							if(L_COLMNM.equals("dot_trpcd")){
								if(L_CNT == 1)
									str1stLVL2 = getSUBSTR(cl_cust.getTRNNM(str1stLVL2),0,20);
								else if(L_CNT == 4)
									str4thLVL2 = getSUBSTR(cl_cust.getTRNNM(str4thLVL2),0,20);
							}
							L_CNT++;		
						}
					}*/
					while(!L_EOF)
					{
			  			if(cl_dat.M_intLINNO_pbst >= 65){
					        cl_dat.M_intLINNO_pbst = 0;
					        cl_dat.M_PAGENO += 1;
			  				prnHEADER(); //gets the Header of the report
					 }
			  			if(L_1STFL)
			  			{
							
							str1stLVL = str1stLVL2;
							str2ndLVL = str2ndLVL2;
							str3rdLVL = str3rdLVL2;
							str4thLVL = str4thLVL2;
							str5thLVL = str5thLVL2;
							str6thLVL = str6thLVL2;
							
							str1stLVL1 = str1stLVL2;
							str2ndLVL1 = str2ndLVL2;
							str3rdLVL1 = str3rdLVL2;
							str4thLVL1 = str4thLVL2;
							str5thLVL1 = str5thLVL2;
							str6thLVL1 = str6thLVL2;
							
							L_1STFL = false;
			  			}
			  			prnGRPHDR("1LV",2);
						str1stLVL1 = str1stLVL;
			  			while((str1stLVL).equals(str1stLVL1) && !L_EOF){
			  				prnGRPHDR("2LV",4);
							str1stLVL = str1stLVL2;
							str1stLVL1 = str1stLVL;
			  				while((str1stLVL+str2ndLVL).equals(str1stLVL1+str2ndLVL1) && !L_EOF){
			  					str1stLVL = str1stLVL2;
			  					str1stLVL1 = str1stLVL;
								str2ndLVL = str2ndLVL2;
			  					str2ndLVL1 = str2ndLVL;
			  					while((str1stLVL+str2ndLVL+str3rdLVL+str4thLVL+str5thLVL+str6thLVL).equals(str1stLVL1+str2ndLVL1+str3rdLVL1+str4thLVL1+str5thLVL1+str6thLVL1) && !L_EOF){
									
									dblTOT1LV += dblPNDQT;
									dblTOT2LV += dblPNDQT;
									dblTOT6LV += dblPNDQT;
									dblTOTALL += dblPNDQT;
									
									if(!M_rstRSSET.next())
									{
										L_EOF = true;
										break;
									}
									str1stLVL2 = nvlSTRVL(M_rstRSSET.getString(1),"");
			  						str2ndLVL2 = M_rstRSSET.getString(2);
			  						str3rdLVL2 = nvlSTRVL(M_rstRSSET.getString(3),"");
									str4thLVL2 = nvlSTRVL(M_rstRSSET.getString(4),"");
			  						str5thLVL2 = nvlSTRVL(M_rstRSSET.getString(5),"");
									str6thLVL2 = nvlSTRVL(M_rstRSSET.getString(6),"");
										
									dblPNDQT = M_rstRSSET.getDouble(7);		
									
									/*stkREPHDR = new StringTokenizer(strHSTVL,",");
									L_CNT = 1;
									if(stkREPHDR.countTokens() > 0){
										while(stkREPHDR.hasMoreTokens()){
											String L_COLMNM = stkREPHDR.nextToken().toString();
											if(L_COLMNM.equals("dot_trpcd")){
												if(L_CNT == 1)
													str1stLVL2 = getSUBSTR(cl_cust.getTRNNM(str1stLVL2),0,20);
												else if(L_CNT == 4)
													str4thLVL2 = getSUBSTR(cl_cust.getTRNNM(str4thLVL2),0,20);
											}
											L_CNT++;		
										}
									}*/
									
									str1stLVL = str1stLVL2;
									str2ndLVL = str2ndLVL2;
									str3rdLVL = str3rdLVL2;
									str4thLVL = str4thLVL2;
									str5thLVL = str5thLVL2;
									str6thLVL = str6thLVL2;
								}
								prnGRPTOT("6LV",dblTOT6LV,6,"N");
								str3rdLVL1 = str3rdLVL;
								str4thLVL1 = str4thLVL;
								str5thLVL1 = str5thLVL;
								str6thLVL1 = str6thLVL;
								dblTOT6LV = 0;
							}
							//prnGRPTOT("2LV",dblTOT2LV,4,"B");
							str2ndLVL1 = str2ndLVL;
							dblTOT2LV = 0;
						}
						prnGRPTOT("1LV",dblTOT1LV,2,"B");
						str1stLVL1 = str1stLVL;
						dblTOT1LV = 0;
					}
					prnGRPTOT("ALL",dblTOTALL,2,"B");
					dosREPORT.writeBytes("\n");
					crtLINE(140);
					prnFMTCHR(dosREPORT,M_strEJT);
					prnFMTCHR(dosREPORT,M_strNOCPI17);
					prnFMTCHR(dosREPORT,M_strCPI10);
					dosREPORT.close();
				}
				if(LM_RSLSET != null)
					LM_RSLSET.close();
				this.setCursor(cl_dat.M_curDFSTS_pbst );   
	   		    
	   		    
	   		    
	   		    
	   		    
	   		    
	   		    
	   		    
	   		    
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"This is the getData");
			}
		 }
		  
		  
		  
	/**
	 * 
	 * @param LP_GRPHDR pick up the values from enmuration class into hash table
	 * @param LP_CLMGAP
	 */	  
		  
		  private void prnGRPHDR(String LP_GRPHDR,int LP_CLMGAP)
		  {
				try
				{
					if(rdbDETAL.isSelected())
					{
						if(LP_GRPHDR.equals("1LV"))
						{
							prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes("\n"+padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',getSUBSTR(str1stLVL1,0,20),(25-LP_CLMGAP))+"\n");
							prnFMTCHR(dosREPORT,M_strNOBOLD);
							cl_dat.M_intLINNO_pbst += 2;
						}
						else if(LP_GRPHDR.equals("2LV"))
						{
							//O_DOUT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',str2ndLVL1,(25-LP_CLMGAP))+"\n");
							//cl_dat.M_LINENO += 1;
							flgHDRFL = true;
						}
					}
					if(cl_dat.M_intLINNO_pbst >= 65){
					    cl_dat.M_intLINNO_pbst = 0;
					    cl_dat.M_PAGENO += 1;
						crtLINE(140);
						prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER(); //gets the Header of the report
					}
				}catch(Exception L_EX){
					setMSG(L_EX,"prnGRPHDR");
				}
			}
		  /**
		   * 
		   * @param LP_GRPHDR Pick up the values from Enmuration Datatype into the HashTable
		   * @param LP_TOTXXX
		   * @param LP_CLMGAP
		   * @param LP_FNTFL
		   */
			
			private void prnGRPTOT(String LP_GRPHDR,double LP_TOTXXX,int LP_CLMGAP,String LP_FNTFL)
			{
				try
				{
					if(LP_FNTFL.equals("B"))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(rdbSUMRY.isSelected())
					{
						if(LP_GRPHDR.equals("1LV"))
						{
							dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',getSUBSTR(str1stLVL1,0,20),25));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(LP_TOTXXX,3),15)+"\n");
							cl_dat.M_intLINNO_pbst += 1;
						}
						else if(LP_GRPHDR.equals("ALL")){
							dosREPORT.writeBytes("\n"+padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',"Grand Total",25));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(LP_TOTXXX,3),15)+"\n");
							cl_dat.M_intLINNO_pbst += 2;
						}
					}else if(rdbDETAL.isSelected())
					{
						if(LP_GRPHDR.equals("1LV"))
						{
							dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',"Total of "+getSUBSTR(str1stLVL1,0,20),124));
						}
						else if(LP_GRPHDR.equals("2LV"))
						{
							dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',str2ndLVL1,122));
						}
						else if(LP_GRPHDR.equals("6LV")){
							if(flgHDRFL)
								dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',str2ndLVL1,24)+padSTRING('R',getSUBSTR(str3rdLVL1,0,20),24)+padSTRING('R',getSUBSTR(str4thLVL1,0,20),24)+padSTRING('R',getSUBSTR(str5thLVL1,0,20),24)+padSTRING('R',getSUBSTR(cl_dat.getPRMCOD("cmt_shrds","SYS","FGXXPKG",str6thLVL1),0,20),24));
							else
								dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R'," ",24)+padSTRING('R',getSUBSTR(str3rdLVL1,0,20),24)+padSTRING('R',getSUBSTR(str4thLVL1,0,20),24)+padSTRING('R',getSUBSTR(str5thLVL1,0,20),24)+padSTRING('R',getSUBSTR(cl_dat.getPRMCOD("cmt_shrds","SYS","FGXXPKG",str6thLVL1),0,20),24));
							flgHDRFL = false;
						}
						else if(LP_GRPHDR.equals("ALL"))
						{
							dosREPORT.writeBytes("\n"+padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',"Grand Total",124));
							cl_dat.M_intLINNO_pbst += 1;
						}
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(LP_TOTXXX,3),15)+"\n");
						cl_dat.M_intLINNO_pbst += 1;
					}
					if(LP_FNTFL.equals("B"))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(cl_dat.M_intLINNO_pbst >= 65){
					    cl_dat.M_intLINNO_pbst = 0;
					    cl_dat.M_PAGENO += 1;
						crtLINE(140);
						prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER(); //gets the Header of the report
					}
				}catch(Exception L_EX){
					setMSG(L_EX,"prnGRPTOT");
				}
			}
			
/**
 * 
 * @param LM_CNT print the line
 */	 
	private void crtLINE(int LM_CNT){
		String strln = "";
		try{
			for(int i=1;i<=LM_CNT;i++){
				 strln += "-";
			}
			dosREPORT.writeBytes(padSTRING('L'," ",0));
			dosREPORT.writeBytes(strln);
		}catch(Exception L_EX){
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}

}
	