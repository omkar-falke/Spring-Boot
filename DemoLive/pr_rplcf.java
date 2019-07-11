/*
System Name   : Laboratory Information Management System
Program Name  : Bagging Lot control Form
Program Desc. : 
				
Author        : N.K.Virdi
Date          : 12/11/2003
Version       : LIMS 3.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.File; 
import java.io.FileOutputStream; 
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class pr_rplcf extends cl_rbase
{
	ResultSet M_rstRSSET1;
	private JTextField txtLOTNO,txtTPRDS,txtIPRDS,txtPRDQT,txtSILNO;
	private String strREPFL = cl_dat.M_strREPSTR_pbst+"\\pr_rplcf.doc";
	
	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;
	String strPRDTP ="";
	private String strCLSNM ="";
	pr_rplcf()
	{
		super(2);

		try
		{
		    strCLSNM = getClass().getName();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		//	M_lblFMDAT.setVisible(false);M_lblTODAT.setVisible(false);
		//	M_txtFMDAT.setVisible(false);M_txtTODAT.setVisible(false);
			setMatrix(20,6);
			add(new JLabel("Lot Number "),2,2,1,1,this,'L');
			add(new JLabel("Target Grade "),2,3,1,1,this,'L');
			add(new JLabel("Cylo No."),2,4,1,1,this,'L');
			add(new JLabel("Prd. qty"),2,5,1,1,this,'L');
			add(new JLabel("Bagging Grade "),2,6,1,1,this,'L');
			add(txtLOTNO = new TxtLimit(8),3,2,1,1,this,'L');
			add(txtTPRDS = new TxtLimit(20),3,3,1,1,this,'L');
			add(txtSILNO = new TxtLimit(15),3,4,1,1,this,'L');
			add(txtPRDQT = new TxtNumLimit(6.3),3,5,1,1,this,'L');
			add(txtIPRDS = new TxtLimit(15),3,6,1,1,this,'L');
			txtTPRDS.setEnabled(false);
			txtSILNO.setEnabled(false);
			txtPRDQT.setEnabled(false);
			txtLOTNO.requestFocus();
	 	}
		catch(Exception L_EX)
		{
			System.out.println(L_EX.toString());
		}
	}
	pr_rplcf(int i)
	{
		try
		{
		    strCLSNM = getClass().getName();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		//	M_lblFMDAT.setVisible(false);M_lblTODAT.setVisible(false);
		//	M_txtFMDAT.setVisible(false);M_txtTODAT.setVisible(false);
			setMatrix(20,6);
			add(new JLabel("Lot Number "),2,2,1,1,this,'L');
			add(new JLabel("Target Grade "),2,3,1,1,this,'L');
			add(new JLabel("Cylo No."),2,4,1,1,this,'L');
			add(new JLabel("Prd. qty"),2,5,1,1,this,'L');
			add(new JLabel("Bagging Grade "),2,6,1,1,this,'L');
			add(txtLOTNO = new TxtLimit(8),3,2,1,1,this,'L');
			add(txtTPRDS = new TxtLimit(20),3,3,1,1,this,'L');
			add(txtSILNO = new TxtLimit(15),3,4,1,1,this,'L');
			add(txtPRDQT = new TxtNumLimit(6.3),3,5,1,1,this,'L');
			add(txtIPRDS = new TxtLimit(15),3,6,1,1,this,'L');
			txtTPRDS.setEnabled(false);
			txtSILNO.setEnabled(false);
			txtPRDQT.setEnabled(false);
			txtLOTNO.requestFocus();
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX.toString());
		}
	}
	void setENBL(boolean P_flgENBDS)
	{
		txtLOTNO.setEnabled(P_flgENBDS);
		txtTPRDS.setEnabled(false);
		txtSILNO.setEnabled(false);
		txtPRDQT.setEnabled(false);
		txtIPRDS.setEnabled(false);
	}
	void clrCOMP()
	{
		txtTPRDS.setText("");
		txtIPRDS.setText("");
		txtSILNO.setText("");
		txtPRDQT.setText("");
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		strPRDTP = M_strSBSCD.substring(2,4);
		if(M_objSOURC == txtLOTNO)
		{
			clrCOMP();txtIPRDS.setEnabled(false);
		}
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		
		String L_ACT = L_AE.getActionCommand();
		if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
					exeHLPOK();		
		else if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
		//	M_lblFMDAT.setVisible(false);M_lblTODAT.setVisible(false);
		//	M_txtFMDAT.setVisible(false);M_txtTODAT.setVisible(false);
		//	clrCOMP();
		}
		else if (M_objSOURC == txtLOTNO)
		{
			try
			{
			M_strSQLQRY ="SELECT LT_CYLNO,LT_TPRCD,PR_PRDDS,LT_IPRDS,LT_PRDQT,LT_PENDT from PR_LTMST,CO_PRMST ";
			M_strSQLQRY += " WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_TPRCD = PR_PRDCD and LT_PRDTP ='"+strPRDTP +"'";
			M_strSQLQRY += " AND LT_LOTNO ='"+txtLOTNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						txtTPRDS.setText(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
						txtIPRDS.setText(nvlSTRVL(M_rstRSSET.getString("LT_IPRDS"),""));
						txtSILNO.setText(nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),""));
						txtPRDQT.setText(nvlSTRVL(M_rstRSSET.getString("LT_PRDQT"),""));
						if(M_rstRSSET.getString("LT_PENDT")==null)
						{
							setMSG("Lot has not been Closed..",'E');
							clrCOMP();
							txtLOTNO.requestFocus();
						}
						else
						{
						    if(strCLSNM.equals("pr_rplcf"))
							{
							    txtIPRDS.setEnabled(true);
							    txtIPRDS.requestFocus();
							}
							else
							{
							    txtIPRDS.setEnabled(false);
							}
						}
					}
					else
					{
						setMSG("Invalid Lot number..",'E');
						txtIPRDS.setText("");
						txtIPRDS.setEnabled(false);
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"");
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{ 
			if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
					exeHLPOK();		
		}
		if(L_KE.getKeyCode()== L_KE.VK_F1)
		{			
			if(M_objSOURC == txtLOTNO)
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				M_strHLPFLD ="txtLOTNO";
				cl_dat.M_flgHELPFL_pbst = true;
				String L_staHEDG[]={"Lot No.","Lot End Date & Time"};
				 if(strCLSNM.equals("pr_rplcf"))
				 {
    				M_strSQLQRY = "Select LT_LOTNO,LT_PENDT from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL <> '8' ";
    				M_strSQLQRY += " AND LT_PRDTP ='"+strPRDTP+"'";
    				if(txtLOTNO.getText().trim().length() >0)
    					M_strSQLQRY += " and LT_LOTNO like "+ "'"+txtLOTNO.getText().trim() + "%'";
    				M_strSQLQRY += " AND LT_RCLNO ='00'";
    				M_strSQLQRY +=" AND LT_PENDT is not null order by LT_PENDT DESC";
				 }
				 else
				 {
				    M_strSQLQRY = "Select LT_LOTNO,LT_PENDT from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL <> '8' ";
    				M_strSQLQRY += " AND LT_PRDTP ='"+strPRDTP+"'";
    				if(txtLOTNO.getText().trim().length() >0)
    					M_strSQLQRY += " and LT_LOTNO like "+ "'"+txtLOTNO.getText().trim() + "%'";
    				M_strSQLQRY += " AND LT_RCLNO ='00'";
    				M_strSQLQRY +=" AND LT_PENDT is not null and isnull(lt_iprds,'') <> '' order by LT_PENDT DESC";

				 }
				 
				cl_hlp(M_strSQLQRY,1,1,L_staHEDG,2,"CT");
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtLOTNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
	}
	boolean vldDATA(String P_strLOTNO,String P_strIPRDS,String P_strOPT)
	{
		if(P_strLOTNO.trim().length() ==0)
		{
			if(P_strOPT.equals("INT"))
				txtLOTNO.requestFocus();
			setMSG("Please, enter valid Lot No. Press F1 for help.",'E');
			return false;
		}
		if((strCLSNM.equals("pr_rplc1"))&& txtIPRDS.getText().trim().length() == 0)
		{
		    setMSG("Can not generate Report,Bagging Grade not declared by Production ..",'E');
		    return false;
		}
		if(P_strIPRDS.trim().length() ==0)
		{
			if(P_strOPT.equals("INT"))
				txtIPRDS.requestFocus();
			setMSG("Please enter bagging grade..",'E');
			return false;
		}
		else if(P_strIPRDS.trim().length() > 15)
		{
			if(P_strOPT.equals("INT"))
				txtIPRDS.requestFocus();
			setMSG("Bagging grade can be upto 15 chacters",'E');
			return false;
		}
		if(P_strOPT.equals("INT"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPFIL_pbst))
			{
				if(M_txtDESTN.getText().trim().length() ==0)
				{
					setMSG("Please Enter a File Name ..",'E'); 
					return false;
				}
				else
					strREPFL = cl_dat.M_strREPSTR_pbst+"\\"+M_txtDESTN.getText().trim()+".doc";
			}
			else
				strREPFL = cl_dat.M_strREPSTR_pbst+"\\pr_rplcf.doc";
		}
		else
				strREPFL = cl_dat.M_strREPSTR_pbst+"\\pr_rplcf.doc";
		return true;
	}
	void exePRINT()
	{
		crtREPT(strPRDTP,txtLOTNO.getText().trim(),txtIPRDS.getText().trim(),"INT");
	}
	public boolean crtREPT(String P_strPRDTP,String P_strLOTNO,String P_strIPRDS,String P_strOPT)
	{
		if(!vldDATA(P_strLOTNO,P_strIPRDS,P_strOPT))
		{
			//setMSG("Invalid Data..",'E');
			return false;
		}
		crtREPT(P_strPRDTP,P_strLOTNO,P_strIPRDS);
		if(P_strOPT.equals("INT")) // internal report
	    {
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPSCN_pbst))
			{
				Runtime r = Runtime.getRuntime();
					Process p = null;
					try
					{
						p  = r.exec("c:\\windows\\wordpad.exe "+strREPFL); 
					}
					catch(IOException L_EX)
					{
						System.out.println("Error.exescrn.......... "+L_EX);
 					}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPPRN_pbst))
			{
				doPRINT(strREPFL);	
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
					
			}
		}
		/*else if(P_strOPT.equals("EXT")) // called externally
		{
			try
			{
				//doPRINT(strREPFL);	
			}
			catch(Exception L_E)
			{
			//	System.out.println("Printing error "+L_E.toString());
				setMSG(L_E,"IN Printing");
			}
		}*/
		return true;
		
	}
public boolean crtREPT(String P_strPRDTP,String P_strLOTNO,String P_strIPRDS)
	{
		boolean L_RECORD = false;
		int   L_SPCCNT =7;
		String L_strBAGLC ="",L_strSILDS="",L_strUSRNM="";
		String L_strPRDQT="0.0",L_strTPRDS="",L_strSILNO="";
		DataOutputStream O_DOUT;
		FileOutputStream O_FOUT;
		try
		{
			setMSG("Report Creation in Progress .. please wait",'N');
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			M_strSQLQRY = "Select LT_LOTNO,LT_TPRCD,PR_PRDDS,LT_IPRDS,LT_CYLNO,LT_PRDQT,LT_LUSBY from PR_LTMST,CO_PRMST ";
			M_strSQLQRY +=" WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP ='"+P_strPRDTP+"'"+" AND LT_LOTNO ='"+P_strLOTNO+"'"+" and LT_RCLNO ='00'";
			M_strSQLQRY +=" AND LT_TPRCD = PR_PRDCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					L_strTPRDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
					L_strSILNO = nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),"");
					L_strPRDQT = setNumberFormat(M_rstRSSET.getDouble("LT_PRDQT"),3);
                    L_strUSRNM = nvlSTRVL(M_rstRSSET.getString("LT_LUSBY"),"");
				}
			}
			else
			{
				setMSG("Invalid Lot No..",'E');
				//txtLOTNO.requestFocus();
				return false;
			}
			File file = new File(cl_dat.M_strREPSTR_pbst+"\\pr_rplcf.doc");
			O_FOUT = new FileOutputStream(file);
        	O_DOUT = new DataOutputStream(O_FOUT);	
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strCPI12);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"",74)+cl_dat.getPRMCOD("CMT_CODDS","ISO","PRXXLCF","DOC1"));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"",74)+cl_dat.getPRMCOD("CMT_CODDS","ISO","PRXXLCF","DOC2"));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"",74)+cl_dat.getPRMCOD("CMT_CODDS","ISO","PRXXLCF","DOC3"));
			O_DOUT.writeBytes("\n");
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strBOLD);
			O_DOUT.writeBytes(padSTRING('L'," ",10));
			O_DOUT.writeBytes(padSTRING('C',"SUPREME PETROCHEM LIMITED ",70));
			O_DOUT.writeBytes("\n\n");
			O_DOUT.writeBytes(padSTRING('L'," ",6));
			prnFMTCHR(O_DOUT,M_strCPI12);
			prnFMTCHR(O_DOUT,M_strENH);
			O_DOUT.writeBytes(padSTRING('C',"Bagging Lot Control Form",45));
			prnFMTCHR(O_DOUT,M_strNOENH);
			O_DOUT.writeBytes("\n");
			prnFMTCHR(O_DOUT,M_strNOBOLD);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("Date :"+cl_dat.M_txtCLKDT_pbst.getText());
			O_DOUT.writeBytes(padSTRING('L',"|",45));
			O_DOUT.writeBytes("To be filled and signed");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("Time of information to start bagging  :  ");
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_txtCLKTM_pbst.getText() + " Hrs.  ",19)+"|");
			O_DOUT.writeBytes("by Shift Officer");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(60,O_DOUT);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"BAGGING LOC. ",30)+": ");
			M_strSQLQRY = "SELECT CMT_CODDS,CMT_CHP02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp = 'PRXXCYL' ";
			M_strSQLQRY +=" AND CMT_CODCD = '" +L_strSILNO +"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				L_strSILDS = M_rstRSSET.getString("CMT_CODDS");
				L_strBAGLC = M_rstRSSET.getString("CMT_CHP02");
			}
			O_DOUT.writeBytes(padSTRING('R',L_strBAGLC,28)+"|");
			if(strCLSNM.equals("pr_rplcf"))
			{
			    O_DOUT.writeBytes(" ENTERED BY : "+cl_dat.M_strUSRCD_pbst);
			}
			else
			    //O_DOUT.writeBytes(" ENTERED BY : "+L_strUSRNM);
			    O_DOUT.writeBytes(" ENTERED BY : "+"   ");
			O_DOUT.writeBytes("\n");
				
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"SILO NO. ",30)+": ");
			O_DOUT.writeBytes(padSTRING('R',L_strSILDS.trim(),28)+"|");
			O_DOUT.writeBytes(" NAME: ");
			if(strCLSNM.equals("pr_rplcf"))
            {
                O_DOUT.writeBytes(getUSRNM(cl_dat.M_strUSRCD_pbst));
            }
            else
                //O_DOUT.writeBytes(getUSRNM(L_strUSRNM));
                O_DOUT.writeBytes(" ");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"LOT NO.",30)+": ");
			prnFMTCHR(O_DOUT,M_strBOLD);
			O_DOUT.writeBytes(padSTRING('R',P_strLOTNO,28));
			prnFMTCHR(O_DOUT,M_strNOBOLD);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"TARGET GRADE",30)+": ");
			O_DOUT.writeBytes(padSTRING('R',L_strTPRDS,28)+"|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"PROV. GRADE(For Bagging) ",30)+": ");
			prnFMTCHR(O_DOUT,M_strBOLD);
			O_DOUT.writeBytes(padSTRING('R',P_strIPRDS,28));
			prnFMTCHR(O_DOUT,M_strNOBOLD);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"APPROX QTY. (MT)",30)+": ");
			O_DOUT.writeBytes(padSTRING('R',L_strPRDQT,28)+"|");
			O_DOUT.writeBytes(" SIGN:");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("Checklist to be checked and filled in by Executive /T.M. ");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 1.   Right silo connected",50)+"Y/N");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes(padSTRING('L',"Signature",15));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 2.   Correct lot no. is being printed / labelled",50)+"Y/N");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 3.   Correct grade is being printed / labelled",50)+"Y/N");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 4.   Metal detection test done ",50)+"Y/N/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",5));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 5.   Metal detector reset",50)+"Y/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",7));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+5));
			O_DOUT.writeBytes(padSTRING('R',"(Incase of grade change)",50));
			O_DOUT.writeBytes(padSTRING('L',"|",6));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 6.   Stitching is O.K. ",50)+"Y/N/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",5));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 7.   Quantity bagged",42)+"________MT ");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 8.   Magnetic seperator cleant ",50)+"Y/N/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",5));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+5));
			O_DOUT.writeBytes(padSTRING('R',"(Incase of jumbo bagging machine)",50));
			O_DOUT.writeBytes(padSTRING('L',"|",6));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(88,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|9.  Initial",14)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			 O_DOUT.writeBytes(padSTRING('R',"10. Final ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   Sign.",8)+"|");
			O_DOUT.writeBytes(padSTRING('L',"|",18));
				
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Reading",14)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L'," ",4));
			O_DOUT.writeBytes(padSTRING('R',"Reading ",11)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   MHD",8)+"|");
			O_DOUT.writeBytes(padSTRING('L',"|",18));
				
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			crtLINE(13,O_DOUT);
			O_DOUT.writeBytes("|");
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(8,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(17,O_DOUT);
			O_DOUT.writeBytes("|");
				
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|   Total Bags",14)+"|");
			O_DOUT.writeBytes(padSTRING('L',"Nos.",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   Diff. ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L',"Nos.",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   Qty.",8)+"|");
			O_DOUT.writeBytes(padSTRING('L',"   MT",15));
			O_DOUT.writeBytes("  |");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(88,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"11.   Sample taken by QCA",55)+"Y/N");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"12.   Silo completely emptied",55)+"Y/N");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"13.   Time of completion of bagging",77)+"________ Hrs.");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"14.   Difference between actual and estimated qty.",77)+"________ MT");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"15.   Details of packing items used",60));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(89,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Item",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Vendor ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Lot/GRIN No. ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Type ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Quantity ",15));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"Av. wt. of|",11));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"|",15));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('R',"empty bag |",10));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			crtLINE(14,O_DOUT);
			O_DOUT.writeBytes("|");
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(10,O_DOUT);
			O_DOUT.writeBytes("|");

			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Bags",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L',"   Nos.",15));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"gms|",11));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"|",15));
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(42,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L',"   Nos.",15));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"gms|",11));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			crtLINE(14,O_DOUT);
			O_DOUT.writeBytes("|");
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(42,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Thread ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");

			O_DOUT.writeBytes("\n");
				
				
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"|",15));
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|",15)+"|");
			O_DOUT.writeBytes(padSTRING('L'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");

			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(46,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"16.   G.W. of filled bags checked( Only for 25 Kg. packing ):- ",60));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
		//	O_DOUT.writeBytes(padr("      1)........Kgs. 2)........Kgs.3)........Kgs. 4)........Kgs. 5).......Kgs.",60));
			O_DOUT.writeBytes(padSTRING('R',"      1)........Kgs. 2)........Kgs.3)........Kgs. 4)........Kgs. 5).......Kgs.",80));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"17.   Any abnormality observed:-",60));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
				
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"T.M./Exe. - MHD ",37));
			O_DOUT.writeBytes(padSTRING('R',"Exe. - MHD ",37));
			O_DOUT.writeBytes(padSTRING('R',"Sr.Manager - MHD",30));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("cc : Control Room");
			prnFMTCHR(O_DOUT,M_strEJT);
			O_DOUT.close();
			O_FOUT.close();
			cl_dat.M_flgLCUPD_pbst = true;
			if(strCLSNM.equals("pr_rplcf"))
			{
			  	M_strSQLQRY = "Update PR_LTMST set ";
    			if(P_strIPRDS.length() >15)
    				M_strSQLQRY += "LT_IPRDS = '" + P_strIPRDS.substring(0,15) + "',";
    			else
    				M_strSQLQRY += "LT_IPRDS = '" + P_strIPRDS+ "',";
    			M_strSQLQRY += "LT_TRNFL = '0',";
    			M_strSQLQRY += "LT_LUSBY = '" + cl_dat.M_strUSRCD_pbst + "',";
    			M_strSQLQRY += "LT_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";		
    			M_strSQLQRY += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '" + P_strPRDTP + "'";
    			M_strSQLQRY += " and LT_LOTNO = '" + P_strLOTNO + "'";
    			M_strSQLQRY += " and LT_RCLNO = '00'";
    			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    			if(cl_dat.exeDBCMT("exeSAVE"))
    			{
    				setMSG("Report has been created",'N');
    			}
    			else
    			{
    			
    				setMSG("Report falied at saving the bagging grade..",'E');
    			}
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		  }catch(Exception L_IO)
			{
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				System.out.println(L_IO.toString());
				return false;
			}	
		return true;
	}
	private void crtLINE(int LM_CNT,DataOutputStream P_DOUT)
	{
		String strLINE = "";
		try
		{
			for(int i=1;i<=LM_CNT;i++)
			{
				 strLINE += "-";
			}
		P_DOUT.writeBytes(strLINE);
		}
		catch(Exception L_EX){
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}

private String getUSRNM(String P_strUSRCD)
{
	String L_strUSRNM = "";	
	try
	{
		M_strSQLQRY = "Select US_USRNM,current_time L_TIME from SA_USMST where US_USRCD = "+"'"+P_strUSRCD +"'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				L_strUSRNM = nvlSTRVL(M_rstRSSET.getString("US_USRNM"),"");
			}
	}
	catch(SQLException L_SE)
	{
		System.out.println(L_SE.toString());
	}
	return L_strUSRNM;
}
public void setSBSRT(String [][] P_staSBSRT)
{
	this.M_staUSRRT=P_staSBSRT;
}
}
