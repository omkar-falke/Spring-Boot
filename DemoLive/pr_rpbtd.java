/*
System Name   : Production System
Program Name  : 
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 14/10/2006
Version       : PR v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.sql.ResultSet;import java.util.Date;import java.sql.Timestamp;
import java.awt.event.KeyEvent;import javax.swing.JComponent;import javax.swing.JComboBox; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import java.util.Hashtable;import java.util.StringTokenizer;

class pr_rpbtd extends cl_rbase
{			
	
	private JTextField txtBATNO;
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 120;      // report width
	private int intPAGENO=0;
	private ResultSet L_rstRSSET ;
	String strPRTNM="";
	private String strDOTLN = "--------------------------------------------------------------------------------";
	String strTODAT=cl_dat.M_strLOGDT_pbst;
	String strLINNO="";
	String strSTRDT="";
	String strENDDT="";
	String strPSTDT="";
	String strPSTTM="";
	String strPENDT="";
	String strPENTM="";
	
	
	public pr_rpbtd()
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
			add(new JLabel("Batch No."),5,3,1,1,this,'L');
			add(txtBATNO=new TxtLimit(8),5,4,1,1,this,'L');
			
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
			if(txtBATNO.getText().trim().length()!=8)
			{
				setMSG("Invalid Batch Number..Please Enter Proper Batch Number ..",'E');
				txtBATNO.requestFocus();
				return false;
			}
					
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			if(M_objSOURC == txtBATNO)
			{
			    setCursor(cl_dat.M_curWTSTS_pbst);
			   
				M_strSQLQRY = "SELECT distinct BT_BATNO,CONVERT(varchar,BT_BSTDT,101) FROM PR_BTMST WHERE BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO <>'00000000' order by bt_batno desc";//WHERE LT_RUNNO ='"+txtRUNNO.getText().trim()+"'";
				M_strHLPFLD = "txtBATNO";
				cl_hlp(M_strSQLQRY ,1,1,new String[]{"Batch No.","Production Date"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtBATNO )
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD == "txtBATNO")
			{
				txtBATNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
		}
	}
	
	
	
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			cl_dat.M_PAGENO =1;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"pr_rpbtd.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "pr_rpbtd.doc";
			getDATA();
			fosREPORT.close();
			dosREPORT.close();
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Party Ledger"," ");
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
			ResultSet L_rstRSSET;
			String L_strPRTTP="";
			String L_strPRTCD="";
			String L_strFMDAT="";
			String L_strPRDQT="";
			String L_strPSTDT="";
			String L_strPENDT="";
			Timestamp L_TMPTM=null;
			double L_dblPRDQT=0;
					
			
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Batchwise Production Details </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			int i=0;
		//	M_strSQLQRY="Select PR_PRDDS,LT_LINNO,LT_PSTDT,LT_PENDT,LT_LOTNO,LT_PRDQT from PR_LTMST,CO_PRMST where LT_PRDCD=PR_PRDCD";
		//	M_strSQLQRY += " AND LT_RUNNO='"+txtBATNO.getText().trim()+"' ";
			
			//M_strSQLQRY="Select BT_BATNO,BT_RCTNO,BT_BSTDT,BT_BENDT,BT_RUNNO,PR_PRDDS,BT_SAMQT*100/(SELECT SUM(B.BT_SAMQT) from PR_BTMST B WHERE BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO ='"+txtBATNO.getText().trim() +"') L_PER from PR_BTMST ,CO_PRMST where BT_GRDCD=PR_PRDCD";
			//M_strSQLQRY += " AND BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO='"+txtBATNO.getText().trim()+"' ";
			M_strSQLQRY="Select BT_BATNO,BT_RCTNO,BT_BSTDT,BT_BENDT,BT_RUNNO,PR_PRDDS,BT_PRDQT from PR_BTMST ,CO_PRMST where BT_GRDCD=PR_PRDCD";
			M_strSQLQRY += " AND BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO='"+txtBATNO.getText().trim()+"' ";
			
			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(i==0)
					{
						
						strLINNO=M_rstRSSET.getString("BT_RCTNO");
						//System.out.println(" strLINNO "+strLINNO);
						
					    L_TMPTM = M_rstRSSET.getTimestamp("BT_BSTDT");
        				if(L_TMPTM !=null)
        					L_strPSTDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BSTDT"));
        				L_TMPTM = M_rstRSSET.getTimestamp("BT_BENDT");
        				if(L_TMPTM !=null)
        				{
        					L_strPENDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BENDT"));
        				}
        				if(L_strPSTDT.trim().length() > 0)
        				{
        					strPSTDT = L_strPSTDT.substring(0,10);
        					strPSTTM = L_strPSTDT.substring(11,16);
        				}
        				if(L_strPENDT.trim().length() > 0)
        				{
        					strPENDT = L_strPENDT.substring(0,10);
        					strPENTM =L_strPENDT.substring(11,16);
						}
						prnHEADER();
        				
    				}
					if(cl_dat.M_intLINNO_pbst >64)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN+"\n");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO +=1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PR_PRDDS"),40));
					//dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("BT_RUNNO"),15));
					dosREPORT.writeBytes(padSTRING('R'," ",15));
					L_strPRDQT= nvlSTRVL(M_rstRSSET.getString("BT_PRDQT"),"0");
					L_dblPRDQT +=Double.parseDouble(L_strPRDQT);
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("BT_PRDQT"),2),10));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					i++;
					intRECCT++;
				}
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes(padSTRING('L'," Total : ",53));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRDQT,3),12));
				dosREPORT.writeBytes("\n"+strDOTLN+"\n");
				//System.out.println(" i  =="+i);
				M_rstRSSET.close();
			}
			M_strSQLQRY="Select CT_MATDS,CT_UOMCD,LTR_ISSQT from PR_LTRDT,CO_CTMST where LTR_MATCD=CT_MATCD ";
			M_strSQLQRY += " AND LTR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LTR_BATNO='"+txtBATNO.getText().trim()+"' ";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			prnHEADER1();
			i=0;
			L_dblPRDQT=0;
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(cl_dat.M_intLINNO_pbst >64)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN+"\n");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO +=1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER1();
					}
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CT_MATDS"),40));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CT_UOMCD"),15));
					L_strPRDQT= nvlSTRVL(M_rstRSSET.getString("LTR_ISSQT"),"0");
					L_dblPRDQT +=Double.parseDouble(L_strPRDQT);					
					dosREPORT.writeBytes(padSTRING('L',L_strPRDQT,10));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					i++;
				}
				dosREPORT.writeBytes(strDOTLN+"\n");
			//	dosREPORT.writeBytes(padSTRING('L'," Total : ",53));
			//	dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRDQT,3),12));
			//	dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			//	System.out.println(" i  =="+i);
				M_rstRSSET.close();
			}
		}
		catch(Exception E)
		{
			setMSG(E,"selDATA");
			
		}
	}

	/**
	 * Method to generate the header of the Report.
	*/
	void prnHEADER()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',"\n"+cl_dat.M_strCMPNM_pbst,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Batchwise Production Details ",strDOTLN.length()-22)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Batch No   : "+txtBATNO.getText().trim(),strDOTLN.length()-22));
			//  dosREPORT.writeBytes(padSTRING('R',cmbPRTTP.getSelectedItem().toString().substring(2, cmbPRTTP.getSelectedItem().toString().length())     +":"+txtPRTNM.getText().trim()+" ("+txtPRTCD.getText().trim()+")",strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Report Date:"+(cl_dat.M_strLOGDT_pbst),22)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Reactor No : "+strLINNO,strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Page No    :"+cl_dat.M_PAGENO ,22)+"\n");
			dosREPORT.writeBytes("Production Period From : "+strPSTDT+" "+strPSTTM+" To "+strPENDT+" "+strPENTM+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			cl_dat.M_intLINNO_pbst +=3;
			dosREPORT.writeBytes(strDOTLN +"\n");	
			dosREPORT.writeBytes("Grade                                                    Prd.Qty.    ");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst += 3;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
			
		}
	}
		
	void prnHEADER1()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			
			dosREPORT.writeBytes(padSTRING('R',"Raw Material Consumption ",strDOTLN.length()-22)+"\n");
			//  dosREPORT.writeBytes(padSTRING('R',cmbPRTTP.getSelectedItem().toString().substring(2, cmbPRTTP.getSelectedItem().toString().length())     +":"+txtPRTNM.getText().trim()+" ("+txtPRTCD.getText().trim()+")",strDOTLN.length()-22));
			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");		
			
			cl_dat.M_intLINNO_pbst +=1;
			
			dosREPORT.writeBytes(strDOTLN +"\n");	
			dosREPORT.writeBytes("Description                             Uom              Quantity    ");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst += 3;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
			
		}
	}
	
}