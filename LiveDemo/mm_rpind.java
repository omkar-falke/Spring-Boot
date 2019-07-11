/*
System Name   : Material Management System
Program Name  : Indent Printing
Program Desc. : 
Author        : N.K.Virdi
Date          : 28/02/2004
Version       : MMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import javax.swing.JComboBox;import javax.swing.JTextField;import javax.swing.ButtonGroup;import javax.swing.JRadioButton;import javax.swing.JLabel;
import java.sql.ResultSet;import java.io.File;import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.*;
public class mm_rpind extends cl_rbase
{
	private JTextField txtFRIND;
	private JTextField txtTOIND;
	private JTextField txtFRDAT;
	private JTextField txtTODAT;
	private JComboBox cmbINDTP;
	private ButtonGroup bgrRPTOP = new ButtonGroup();
	private JRadioButton rdoOPTIN,rdoOPTDT;
	private JLabel lblINDNO,lblDATE;
	private String strREPFL = cl_dat.M_strREPSTR_pbst+"mm_rpind.doc";
	private String strINDNO,strAMDNO,strDPTCD,strINDDT,strAMDDT,strURGCD,strTEMP;
	private String strPREBY,strFRWBY,strAUTBY,strPREDT,strFRWDT,strAUTDT,strPRVIND ="";
	private java.util.Hashtable<String,String> hstMATCD = new java.util.Hashtable<String,String>();
	boolean flgFIRST  = false;
	private int intSRLNO =0;
	private int intLINNO,intPAGNO,intRECCNT;
	private String strPRINT ="",strPRINT1;
	private String strSTRTP ="";	
	private String strMATCD;
	private ResultSet L_rstRSSET,rstRSSET;
	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	mm_rpind()
	{
		super(2);

		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(rdoOPTIN = new JRadioButton("Indent Wise",true),3,3,1,1.5,this,'L'); // Option Indent wise
			add(rdoOPTDT = new JRadioButton("Date Wise",false),3,5,1,1,this,'L');	// Option Date wise
			bgrRPTOP.add(rdoOPTIN);
			bgrRPTOP.add(rdoOPTDT);
			
			add(new JLabel("Indent Type "),4,3,1,1,this,'L');
			add(cmbINDTP = new JComboBox(),4,4,1,2,this,'L');
			cmbINDTP.addItem("Purchase Indent");
			cmbINDTP.addItem("Rate Contract Request");
			
			add(new JLabel("From "),5,4,1,1,this,'L');
			add(new JLabel("To "),5,5,1,1,this,'L');
			
			add(lblINDNO = new JLabel("Indent No. "),6,3,1,1,this,'L');
			add(txtFRIND = new TxtLimit(10),6,4,1,1,this,'L');
			add(txtTOIND = new TxtLimit(10),6,5,1,1,this,'L');
			add(lblDATE  = new JLabel("Date "),7,3,1,1,this,'L');
			add(txtFRDAT = new TxtDate(),7,4,1,1,this,'L');
			add(txtTODAT = new TxtDate(),7,5,1,1,this,'L');
			txtFRDAT.setEnabled(false);
			txtTODAT.setEnabled(false);
			lblDATE.setEnabled(false);
			txtFRIND.requestFocus();
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	mm_rpind(String P_strSBSCD,String P_strSTRTP)
	{
		M_strSBSCD = P_strSBSCD;
		strSTRTP = P_strSTRTP;
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		//strSTRTP =M_strSBSCD.substring(2,4);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			strSTRTP =M_strSBSCD.substring(2,4);
			if(rdoOPTIN.isSelected())
			{
				txtFRDAT.setEnabled(false);
				txtTODAT.setEnabled(false);
				lblDATE.setEnabled(false);
			}
			else 
			{
				txtFRIND.setEnabled(false);
				txtTOIND.setEnabled(false);
				lblINDNO.setEnabled(false);
			}
		}
		else if(M_objSOURC == rdoOPTDT)
		{
			try
			{
				txtFRDAT.setEnabled(true);
				txtTODAT.setEnabled(true);
				lblDATE.setEnabled(true);
				txtFRIND.setEnabled(false);
				txtTOIND.setEnabled(false);
				lblINDNO.setEnabled(false);
				txtFRIND.setText("");
				txtTOIND.setText("");
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				M_calLOCAL.add(java.util.Calendar.DATE,-1);
				txtFRDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				cl_dat.M_btnSAVE_pbst.requestFocus();		
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"actiop");
			}
		}
		else if(M_objSOURC == rdoOPTIN)
		{
			txtFRDAT.setEnabled(false);
			txtTODAT.setEnabled(false);
			lblDATE.setEnabled(false);
			txtFRIND.setEnabled(true);
			txtTOIND.setEnabled(true);
			lblINDNO.setEnabled(true);
			txtFRDAT.setText("");
			txtTODAT.setText("");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == 9 || L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{ 
			if (M_objSOURC == rdoOPTIN)
			{
				rdoOPTDT.requestFocus();
			}
			else if (M_objSOURC == rdoOPTDT)
			{
				cmbINDTP.requestFocus();
			}
			else if (M_objSOURC == cmbINDTP)
			{
				cmbINDTP.transferFocus();
			}
			else if (M_objSOURC == txtFRIND)
			{
			   if(vldINDNO(M_strSBSCD.substring(2,4),txtFRIND.getText().trim()))
			   {
					txtTOIND.requestFocus();		
					txtTOIND.setText(txtFRIND.getText().trim());
				}
			}
			else if (M_objSOURC == txtTOIND)
			{
				if(vldINDNO(M_strSBSCD.substring(2,4),txtTOIND.getText().trim()))
					cl_dat.M_btnSAVE_pbst.requestFocus();		
			}
			else if (M_objSOURC == txtFRDAT)
			{
				txtTODAT.requestFocus();		
				txtTODAT.setText(txtFRDAT.getText().trim());
			}
			else if (M_objSOURC == txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();		
		}
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{		
			if((M_objSOURC == txtFRIND)||(M_objSOURC == txtTOIND))
			{
				cl_dat.M_flgHELPFL_pbst = true;
				if(M_objSOURC == txtFRIND)
					M_strHLPFLD = "txtFRIND";
				else if(M_objSOURC == txtTOIND)
					M_strHLPFLD = "txtTOIND";
				String L_ARRHDR[] = {"Indent No","Amd. No.","Dept. code"};
				M_strSQLQRY = "Select distinct IN_INDNO,IN_AMDNO,IN_DPTCD ";
				M_strSQLQRY += "  from MM_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') <>'X'";
				M_strSQLQRY += " and IN_STRTP ='"+strSTRTP+"'";
				if(cmbINDTP.getSelectedItem().toString().equals("Purchase Indent"))
					M_strSQLQRY += " and IN_INDTP ='01'";
				else
					M_strSQLQRY += " and IN_INDTP ='02'";
				if(M_objSOURC == txtFRIND)
				{
					if(txtFRIND.getText().trim().length() >0)
					M_strSQLQRY += " AND IN_INDNO like '"+txtFRIND.getText().trim() +"%'";
				}
				else
				{
					if(txtTOIND.getText().trim().length() >0)
					M_strSQLQRY += " AND IN_INDNO like '"+txtTOIND.getText().trim() +"%'";
				}
				M_strSQLQRY += " order by IN_INDNO DESC";
			
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtFRIND"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtFRIND.setText(cl_dat.M_strHLPSTR_pbst);
				txtTOIND.setText(txtFRIND.getText().trim());
		
			}
			else if(M_strHLPFLD.equals("txtTOIND"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtTOIND.setText(cl_dat.M_strHLPSTR_pbst);
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	// Method to validate the indent No.
	private boolean vldINDNO(String P_strSTRTP,String P_strINDNO)
	{
		try
		{
			M_strSQLQRY = "Select IN_INDNO from MM_INMST ";
            M_strSQLQRY += " where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'')  <> 'X'";
			M_strSQLQRY += " and IN_STRTP = '"+P_strSTRTP +"'";
			M_strSQLQRY += " and IN_INDNO = '"+P_strINDNO +"'";
			if(cmbINDTP.getSelectedItem().toString().equals("Purchase Indent"))
				M_strSQLQRY += " and IN_INDTP = '01'";
			else
				M_strSQLQRY += " and IN_INDTP = '02'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				return true;	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldINDNO");
			return false;
        }
		setMSG("Invalid Indent Number..",'E');
		return false;
	}
	boolean vldDATA()
	{
		boolean L_flgRPT = false;
		if(M_rdbHTML.isSelected())
		     strREPFL = cl_dat.M_strREPSTR_pbst + "mm_rpind.html";
		if(M_rdbTEXT.isSelected())
		     strREPFL = cl_dat.M_strREPSTR_pbst + "mm_rpind.doc";
		if(rdoOPTIN.isSelected())
		{
			if(txtFRIND.getText().trim().length()!=8)
			{
				setMSG("From Indent No. should be of 8 chars..",'E');
				txtFRIND.requestFocus();
				return false;
			}
			else if(txtTOIND.getText().trim().length() ==0)
			{
				setMSG("To Indent No. should be of 8 chars..",'E');
				txtTOIND.requestFocus();
				return false;
			}
		
			else if(Integer.parseInt(txtTOIND.getText().trim()) <Integer.parseInt(txtFRIND.getText().trim()))
			{
				setMSG("To Indent No. can not be less than from indent..",'E');
				txtTOIND.requestFocus();
				return false;
			}
			else if(!vldINDNO(M_strSBSCD.substring(2,4),txtFRIND.getText().trim()))
			{
				setMSG("Invalid From Indent No...",'E');
				txtFRIND.requestFocus();
				return false;
			}
			else if(!vldINDNO(M_strSBSCD.substring(2,4),txtTOIND.getText().trim()))
			{
				setMSG("Invalid To Indent No...",'E');
				txtTOIND.requestFocus();
				return false;
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if(M_cmbDESTN.getItemCount() ==0)
			{
				setMSG("Please select E-mail Id by using the F1 list ..",'E');
				return false;
			}
		}
	return true;
	}
	private void prnHEADER(String P_strINDTP)
	{  
		try
		{
			O_DOUT.writeBytes("\n");
			intLINNO =1;
			if(P_strINDTP.equals("PI"))
			{
				String strISO1 = "DOCUMENT REF : "+cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPIND01");
				String strISO2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPIND02");
				String strISO3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPIND03");
				O_DOUT.writeBytes(padSTRING('L',"-------------------------------",86));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',strISO1,86));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',strISO2,86));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',strISO3,86));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',"-------------------------------",86));
				O_DOUT.writeBytes("\n");
				intLINNO += 5;
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(O_DOUT,M_strBOLD);
				prnFMTCHR(O_DOUT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("<b>");
			O_DOUT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",56));
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strCPI12);
			O_DOUT.writeBytes("\n");
			if(P_strINDTP.equals("PI"))
				O_DOUT.writeBytes(padSTRING('R',"PURCHASE INDENT ",65));
			else
				O_DOUT.writeBytes(padSTRING('R',"RATE CONTRACT REQUEST ",65));
			O_DOUT.writeBytes("Report Date      : "+cl_dat.M_strLOGDT_pbst);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"Stores Type      : "+cl_dat.getPRMCOD("CMT_CODDS","SYS","MMXXSST",strSTRTP),65));	
			O_DOUT.writeBytes("Indent No./Date  : "+strSTRTP+"/"+strINDNO + " "+strINDDT);
			
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"Department       : "+cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXDPT",strDPTCD),65));	
			O_DOUT.writeBytes("Amd. No. /Date   : "+strAMDNO +"-"+strAMDDT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"Urgency Code     : "+cl_dat.getPRMCOD("CMT_CODDS","SYS","MMXXURG",strURGCD),65));	
			O_DOUT.writeBytes("Page No.         : " + String.valueOf(intPAGNO) + "\n");
			O_DOUT.writeBytes("\n");
			
			crtLINE(106);
			if(P_strINDTP.equals("PI"))
			{
				O_DOUT.writeBytes("\nSrl. Item Code  UOM      Part Number          T.C/Ins.    Qty. on    Qty. On Indent  Indent Qty.  Req. Date");
				O_DOUT.writeBytes("\nNo.  Description                              Stock Fl.      Hand    Qty. on Order   P.O.Exp.Dt.  Exp. Date \n");
			}
			else
			{
				O_DOUT.writeBytes("\nSrl. Item Code  UOM      Part Number           T.C/Ins.    Prv. Year                  Indent Qty. ");
				O_DOUT.writeBytes("\nNo.  Description                              Stock Fl.   Consumption                P.O.Exp.Dt.   \n");
			}
				
			crtLINE(106);
			O_DOUT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("</b>");
			intLINNO += 10;
		}
		catch(Exception L_exEXCP)
		{
			setMSG(L_exEXCP,"prnhead......");
		}
	}
	private void crtLINE(int P_intCHCNT)
	{
		String L_strLIN = "";
		try
		{
			for(int i=1;i<=P_intCHCNT;i++)
			{
				 L_strLIN += "-";
			}
			O_DOUT.writeBytes(L_strLIN);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"CRTLine");
		}
	}
	private void prnFOOTR(String P_strINDNO)
	{
		try
		{
			String L_strREMDS ="";
			O_DOUT.writeBytes ("\n");
			crtLINE(106);
			O_DOUT.writeBytes ("\n");
			O_DOUT.writeBytes ("Remarks  : ");
			M_strSQLQRY = "SELECT * FROM MM_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND RM_STRTP ='"+strSTRTP +"'";
			M_strSQLQRY += " AND RM_DOCTP ='IN'";
			M_strSQLQRY += " AND RM_TRNTP ='IN'";
			M_strSQLQRY += " AND RM_REMTP IN('IND','OTH')";
			M_strSQLQRY += " AND RM_DOCNO ='"+P_strINDNO+"'";
			M_strSQLQRY += " AND isnull(RM_STSFL,'') <>'X'";
			rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(rstRSSET !=null)
			while(rstRSSET.next())
			{
				if(rstRSSET.getString("RM_REMTP").equals("IND"))
				{
					L_strREMDS = nvlSTRVL(rstRSSET.getString("RM_REMDS"),"");
					if(L_strREMDS.length() >97)
					{
						O_DOUT.writeBytes(L_strREMDS.substring(0,95)+"-");
						O_DOUT.writeBytes ("\n");			
						intLINNO++;
						O_DOUT.writeBytes(padSTRING('L',"",10));
						O_DOUT.writeBytes(L_strREMDS.substring(95));
					}
					else
						O_DOUT.writeBytes(L_strREMDS);
					
				}
				else if(rstRSSET.getString("RM_REMTP").equals("OTH"))
				{
					O_DOUT.writeBytes ("\n");
					intLINNO++;
					O_DOUT.writeBytes ("Comments : ");
					L_strREMDS = nvlSTRVL(rstRSSET.getString("RM_REMDS"),"");
					if(L_strREMDS.length() >97)
					{
						O_DOUT.writeBytes(L_strREMDS.substring(0,95)+"-");
						O_DOUT.writeBytes ("\n");			
						intLINNO++;
						O_DOUT.writeBytes(padSTRING('L',"",10));
						O_DOUT.writeBytes(L_strREMDS.substring(95));
					}
					else
						O_DOUT.writeBytes(L_strREMDS);
				}
			}
			if(rstRSSET !=null)
				rstRSSET.close();
			O_DOUT.writeBytes ("\n");
			crtLINE(106);
			O_DOUT.writeBytes ("\n");
			O_DOUT.writeBytes (padSTRING('R',"Prepared By : "+strPREBY,42)+padSTRING('R',"Recommended By : "+strFRWBY,42)+padSTRING('R',"Approved By : "+strAUTBY,36));
			O_DOUT.writeBytes ("\n");
			O_DOUT.writeBytes (padSTRING('R',"Date : "+strPREDT,42)+padSTRING('R',"Date : "+strFRWDT,42)+padSTRING('R',"Date : "+strAUTDT,36));
			O_DOUT.writeBytes ("\n");
			crtLINE(106);
			O_DOUT.writeBytes ("\n");
			if(!strAMDNO.equals("00"))
				O_DOUT.writeBytes ("Ammended Item codes are marked with *");
			intLINNO += 7;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(O_DOUT,M_strCPI10);
				prnFMTCHR(O_DOUT,M_strEJT);				
			}
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
			intLINNO = 0;
			intPAGNO = 1;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}		
void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				if(O_DOUT !=null)
					O_DOUT.close();
				if(O_FOUT !=null)
					O_FOUT.close();
				if(cmbINDTP.getSelectedItem().toString().equals("Purchase Indent"))
				{
					if(rdoOPTIN.isSelected())
						getALLREC(txtFRIND.getText().trim(),txtTOIND.getText().trim(),'I',"PI"); 
					else
						getALLREC(txtFRDAT.getText().trim(),txtTODAT.getText().trim(),'D',"PI"); 
				}
				else
				{
					if(rdoOPTIN.isSelected())
						getALLREC(txtFRIND.getText().trim(),txtTOIND.getText().trim(),'I',"RCR"); 
					else
						getALLREC(txtFRDAT.getText().trim(),txtTODAT.getText().trim(),'D',"RCR"); 
				}
				strPRVIND ="";
				if(intRECCNT ==0)
				{
					setMSG("No data found..",'E');
					return;
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if (M_rdbTEXT.isSelected())
					    doPRINT(strREPFL);
					else 
			        {    
						Runtime r = Runtime.getRuntime();
						Process p = null;					
						p  = r.exec("c:\\windows\\iexplore.exe "+strREPFL); 
						setMSG("For Printing Select File Menu, then Print  ..",'N');
					}    
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				    Runtime r = Runtime.getRuntime();
					Process p = null;
					if(M_rdbHTML.isSelected())
					    p  = r.exec("c:\\windows\\iexplore.exe "+strREPFL); 
					else
					    p  = r.exec("c:\\windows\\wordpad.exe "+strREPFL); 
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
				   	cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strREPFL,"Purchase Indent"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
			    }
				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error in exePRINT ");
		}
		finally
		{
			try
			{
				if(O_DOUT !=null)
					O_DOUT.close();
				if(O_FOUT !=null)
					O_FOUT.close();
			}
			catch(Exception L_E)
			{
				
			}
		}
	}
	public void getALLREC(String P_strFRVAL,String P_strTOVAL,char P_chrOPTN,String P_strINDTP)
	{
		java.sql.Timestamp L_tmsTEMP;
		java.sql.Date L_datTEMP;
		String L_strLVLNO ="",L_strPMATCD ="",L_strMATCD="",L_strORDQT ="0",L_strDSCTP;
		String L_strPRVLVL="";
		String L_strPREBY="",L_strFRWBY="",L_strAUTBY="",L_strPREDT="",L_strFRWDT="",L_strAUTDT="";
		String L_strPRVAM ="00";
		String L_strSQLQRY ="";
		
		try
		{
			intRECCNT = 0;
			intSRLNO =0;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Progress....",'N');
			O_FOUT = new FileOutputStream(strREPFL);	
			O_DOUT = new DataOutputStream(O_FOUT);
			intLINNO = 0;
			intPAGNO = 1;
            strSTRTP = M_strSBSCD.substring(2,4);
			if(M_rdbHTML.isSelected())
			{
			    O_DOUT.writeBytes("<HTML><HEAD><Title>List of Issues </title> ");
				O_DOUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				O_DOUT.writeBytes("</STYLE></HEAD>"); 
				O_DOUT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
			M_strSQLQRY = " SELECT * from MM_INMST,CO_CTMST where IN_MATCD = CT_MATCD and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND IN_STRTP ='"+strSTRTP+"'";
			if(P_chrOPTN =='I')
			{
				M_strSQLQRY += " AND IN_INDNO BETWEEN  '"+P_strFRVAL.trim()+"'";
				M_strSQLQRY += " AND '"+P_strTOVAL.trim()+"'";
			}
			else if(P_chrOPTN =='D')
			{
				P_strFRVAL = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFRDAT.getText().trim()+" 00:00"));
				P_strTOVAL = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" 23:59"));
				M_strSQLQRY += " AND IN_AUTDT BETWEEN  '"+P_strFRVAL.trim()+"'";
				M_strSQLQRY += " AND '"+P_strTOVAL.trim()+"'";
				M_strSQLQRY += " AND IN_STSFL ='4'";
			}
			M_strSQLQRY += " and isnull(CT_STSFL,'') <>'X' and isnull(IN_STSFL,'') <> 'X' order by IN_INDNO,IN_MATCD";
			System.out.println(M_strSQLQRY);
            M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				intRECCNT++;
				intSRLNO++;
				strINDNO = M_rstRSSET.getString("IN_INDNO");
				strAMDNO = M_rstRSSET.getString("IN_AMDNO");
				L_strMATCD = M_rstRSSET.getString("IN_MATCD");
				if(!strINDNO.equals(strPRVIND))
				{
					intSRLNO =1;
					if(strPRVIND.trim().length() >0)
					{
						prnFOOTR(strPRVIND);
						intRECCNT =1;
					}
					if(!strAMDNO.equals("00"))
					{
						String L_strTEMP ="";
						int L_intTEMP = Integer.parseInt(strAMDNO) - 1;
						for(int i=0;i<2-String.valueOf(L_intTEMP).toString().length();i++)
						L_strTEMP +=0;
						L_strPRVAM = L_strTEMP+String.valueOf(L_intTEMP);
						L_strSQLQRY = "SELECT A.IN_MATCD FROM MM_INMST A,MM_INMAM  B ";
						L_strSQLQRY += " WHERE A.IN_CMPCD = B.IN_CMPCD AND A.IN_INDNO = B.IN_INDNO AND A.IN_MATCD = B.IN_MATCD ";
						L_strSQLQRY += " AND A.IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(A.IN_INDQT,0) <> isnull(B.IN_INDQT,0) and isnull(A.IN_STSFL,'')<>'X' and isnull(B.IN_STSFL,'')<>'X' and a.in_indno ='"+strINDNO.trim()+"'";
						L_strSQLQRY += " AND B.IN_AMDNO ='"+L_strPRVAM.trim()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
						hstMATCD.clear();
						if(L_rstRSSET !=null)
				
						while(L_rstRSSET.next())
						{
							hstMATCD.put(L_rstRSSET.getString(1),"");
						}
					}
					strPRVIND = strINDNO;
				}
				strDPTCD = M_rstRSSET.getString("IN_DPTCD");
				strURGCD = M_rstRSSET.getString("IN_URGTG");
				L_datTEMP = M_rstRSSET.getDate("IN_INDDT");
				if(L_datTEMP !=null)
					strINDDT = M_fmtLCDAT.format(L_datTEMP);
				else
						strINDDT = "";
				L_datTEMP = M_rstRSSET.getDate("IN_AMDDT");
				if(L_datTEMP !=null)
					strAMDDT = M_fmtLCDAT.format(L_datTEMP);
				else
						strAMDDT = "";
				if(intRECCNT ==1)
				{
					prnHEADER(P_strINDTP); 		
					strPREBY = nvlSTRVL(M_rstRSSET.getString("IN_PREBY"),"");
					strFRWBY = nvlSTRVL(M_rstRSSET.getString("IN_FRWBY"),"");
					strAUTBY = nvlSTRVL(M_rstRSSET.getString("IN_AUTBY"),"");
					L_tmsTEMP = M_rstRSSET.getTimestamp("IN_PREDT");
					if(L_tmsTEMP !=null)
						strPREDT = M_fmtLCDTM.format(L_tmsTEMP).substring(0,10);
					else
						strPREDT = "";
					L_tmsTEMP = M_rstRSSET.getTimestamp("IN_FRWDT");
					if(L_tmsTEMP !=null)
						strFRWDT = M_fmtLCDTM.format(L_tmsTEMP).substring(0,10);
					else
						strFRWDT = "";
					L_tmsTEMP = M_rstRSSET.getTimestamp("IN_AUTDT");
					if(L_tmsTEMP !=null)
						strAUTDT = M_fmtLCDTM.format(L_tmsTEMP).substring(0,10);
					else
						strAUTDT = "";
				}
				if(intLINNO > 66)
				{
					crtLINE(106);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);				
					intLINNO = 0;
					intPAGNO += 1;
					if(M_rdbHTML.isSelected())
						O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER(P_strINDTP); 
					
				}
				L_strLVLNO = M_rstRSSET.getString("CT_LVLRF");
				L_strDSCTP = M_rstRSSET.getString("CT_DSCTP");
				//L_strMATCD = M_rstRSSET.getString("IN_MATCD");
				strMATCD = L_strMATCD;
				O_DOUT.writeBytes("\n");
				intLINNO++;
				if(intLINNO > 66)
				{
					crtLINE(106);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);				
					intLINNO = 0;
					intPAGNO += 1;
					if(M_rdbHTML.isSelected())
						O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER(P_strINDTP); 
				}
				strPRINT = "";
				strPRINT1 = "";
				strTEMP = String.valueOf(intSRLNO);
				if(strTEMP.length() ==1)
					strTEMP ="00"+strTEMP+"  ";
				else if(strTEMP.length() ==2)
					strTEMP ="0"+strTEMP+"  ";
				strPRINT += strTEMP;
				if(!strAMDNO.equals("00"))
				{
					if(hstMATCD.contains(L_strMATCD))
					{
						strPRINT += padSTRING('R',L_strMATCD+"*",20);
					}
					else
					{
						strPRINT += padSTRING('R',L_strMATCD,20);
					}
				}
				else
					strPRINT += padSTRING('R',L_strMATCD,11);
					strPRINT += padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD")," "),9);
					strPRINT += padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_PRTNO")," "),21);
					strPRINT += padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IN_TCFFL"),"-")+"/"+nvlSTRVL(M_rstRSSET.getString("IN_INSFL"),"-"),5);
				 	strPRINT1 = padSTRING('R'," ",46);	
					M_strSQLQRY = "SELECT ST_MATCD,ST_STKQT,ST_STKIN,ST_STKOR,ST_STKFL,ST_CONQT from mm_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND st_strtp ='"+strSTRTP+"'";			
					M_strSQLQRY +=" AND ST_MATCD ='"+L_strMATCD +"'";
					L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(L_rstRSSET !=null)
						if(L_rstRSSET.next())
						{
							if(P_strINDTP.equals("PI"))
							{
								strPRINT += padSTRING('L',nvlSTRVL(L_rstRSSET.getString("ST_STKQT"),"0.000"),14);
								strPRINT +=padSTRING('L',nvlSTRVL(L_rstRSSET.getString("ST_STKIN"),"0.000"),18);
								strPRINT1 += padSTRING('L',nvlSTRVL(L_rstRSSET.getString("ST_STKFL"),"-"),5);	
								strPRINT1 += padSTRING('L'," ",14);
								strPRINT1 += padSTRING('L',nvlSTRVL(L_rstRSSET.getString("ST_STKOR"),"0.000"),18);
							}
							else
							{
								strPRINT += padSTRING('L',nvlSTRVL(L_rstRSSET.getString("ST_CONQT"),"0.000"),14);
								strPRINT +=padSTRING('L',"",18);
								strPRINT1 += padSTRING('L',nvlSTRVL(L_rstRSSET.getString("ST_STKFL"),"-"),5);	
								strPRINT1 += padSTRING('L'," ",14);
								strPRINT1 += padSTRING('L',"",18);
							}
							
						}else
						{
							strPRINT += padSTRING('L'," ",32);
							strPRINT1 += padSTRING('L'," ",37);
						}
					if(nvlSTRVL(M_rstRSSET.getString("IN_STSFL"),"").equals("4"))
						strPRINT += padSTRING('L',M_rstRSSET.getString("IN_AUTQT"),13);
					else
						strPRINT += padSTRING('L',M_rstRSSET.getString("IN_INDQT"),13);
					strPRINT += padSTRING('L'," ",2);
					strPRINT1 += padSTRING('L'," ",3);
					if(P_strINDTP.equals("PI"))
						L_datTEMP = M_rstRSSET.getDate("IN_PORBY");
					else 
						L_datTEMP = null;
					if(L_datTEMP !=null)
						strPRINT1 += padSTRING('R',M_fmtLCDAT.format(L_datTEMP),12);
					else 	
						strPRINT1 += padSTRING('R'," ",12);
					if(P_strINDTP.equals("PI"))
						L_datTEMP = M_rstRSSET.getDate("IN_REQDT");
				    else 
						L_datTEMP = null;	

					if(L_datTEMP !=null)
						strPRINT += padSTRING('R',M_fmtLCDAT.format(L_datTEMP),12);
					else 	
						strPRINT += padSTRING('R'," ",12);
						strPRINT += padSTRING('R'," ",12);
				strPRINT += "\n";
				intLINNO++;
				if(intLINNO > 66)
				{
						crtLINE(106);
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(O_DOUT,M_strEJT);				
						intLINNO = 0;
						intPAGNO += 1;
						if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER(P_strINDTP); 
				}
				if(P_strINDTP.equals("PI"))	
					L_datTEMP = M_rstRSSET.getDate("IN_EXPDT");
				else
					L_datTEMP = null;	
				if(L_datTEMP !=null)
					strPRINT1 += padSTRING('R',M_fmtLCDAT.format(L_datTEMP),12);
				else 	
					strPRINT1 += padSTRING('R'," ",12);
				
				if(L_strDSCTP.equals("S"))
				{
					if(L_strPMATCD.equals(""))
					{
						flgFIRST = true;
						// for First Record
						getMATDS(L_strMATCD,"MG",L_strLVLNO,P_strINDTP);
					}
					else if(L_strMATCD.substring(0,2).equals(L_strPMATCD.substring(0,2)))
					{
						flgFIRST = false;
						// If main group is same
						if(L_strMATCD.substring(0,4).equals(L_strPMATCD.substring(0,4)))
					    {
							// If sub group is same
							if(L_strMATCD.substring(0,6).equals(L_strPMATCD.substring(0,6)))
							{
								//if sub -sub group is same
								if(!L_strLVLNO.equals(L_strPRVLVL))
								{
									//if level header is differenet
									getMATDS(L_strMATCD,"HD",L_strLVLNO,P_strINDTP);
								}
								else
								{
									//if description of code
									getMATDS(L_strMATCD,"CD",L_strLVLNO,P_strINDTP);
								}
							}
							else
							{
								//if sub -sub group is different
								getMATDS(L_strMATCD,"SS",L_strLVLNO,P_strINDTP);
							}
						}
						else
						{
							// If sub group is different
							getMATDS(L_strMATCD,"SG",L_strLVLNO,P_strINDTP);
						}
					}
					else
					{
						// If main group is different
						getMATDS(L_strMATCD,"MG",L_strLVLNO,P_strINDTP);
					}
				}
				else if(L_strDSCTP.equals("D"))
				{
					/////
					M_strSQLQRY = "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
					M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='CD' ";
					M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD +"'";
					//M_strSQLQRY += " and CTT_LVLNO ='"+P_strLVLNO+"'";
					M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
					M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
					M_strSQLQRY += " Order by ctt_matcd,ctt_lvlno,ctt_linno ";
					L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					O_DOUT.writeBytes(strPRINT);
					O_DOUT.writeBytes(strPRINT1);
					O_DOUT.writeBytes("\n");
					intLINNO++;
					if(intLINNO > 66)
					{
							crtLINE(106);
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(O_DOUT,M_strEJT);				
							intLINNO = 0;
							intPAGNO += 1;
							if(M_rdbHTML.isSelected())
								O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER(P_strINDTP); 
					}
				
					if(L_rstRSSET !=null)
					while(L_rstRSSET.next())
					{
						O_DOUT.writeBytes(padSTRING('L',"",5)); // added on 03/04
						O_DOUT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
						O_DOUT.writeBytes("\n");
						intLINNO++;
						if(intLINNO > 66)
						{
							crtLINE(106);
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(O_DOUT,M_strEJT);				
							intLINNO = 0;
							intPAGNO += 1;
							if(M_rdbHTML.isSelected())
								O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER(P_strINDTP); 
						}
			
					}
				}
					/////
				L_strPMATCD = L_strMATCD;
				L_strPRVLVL = L_strLVLNO;
				intLINNO++;
				if(intLINNO > 66)
				{
					crtLINE(106);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);				
					intLINNO = 0;
					intPAGNO += 1;
					if(M_rdbHTML.isSelected())
						O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER(P_strINDTP); 
				}
			}
			if(intRECCNT > 0)
				prnFOOTR(strINDNO);
			if(M_rdbHTML.isSelected())
			{
			    O_DOUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			O_DOUT.close();
			O_FOUT.close();
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			setMSG("",'N');
		}catch(Exception L_EX){
			setMSG(L_EX,"getALLREC");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
private void getMATDS(String P_strMATCD,String P_strDSLVL,String P_strLVLNO,String P_strINDTP)
{
	try
	{
		int L_COUNT =0,L_intCOUNT =0;
		M_strSQLQRY ="";
		boolean pflag = true;
		if(P_strDSLVL.equals("MG"))
		{
			M_strSQLQRY = "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='MG' ";
			M_strSQLQRY += " and ctt_matcd = '"+P_strMATCD.substring(0,2) +"0000000A'";
			M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
			M_strSQLQRY += "UNION ";
		}
		if((P_strDSLVL.equals("MG"))||(P_strDSLVL.equals("SG")))
		{
			M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SG' ";
			M_strSQLQRY += " and ctt_matcd = '"+P_strMATCD.substring(0,4) +"00000A'";
			M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
						
			M_strSQLQRY += "UNION ";
		}
		if((P_strDSLVL.equals("MG"))||(P_strDSLVL.equals("SG"))||(P_strDSLVL.equals("SS")))
		{
			M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
			M_strSQLQRY += " and ctt_matcd = '"+P_strMATCD.substring(0,6) +"000A'";
			M_strSQLQRY += " and CTT_LVLNO ='00'";
			M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
			M_strSQLQRY += "UNION ";
		}				
		//if((!P_strLVLNO.equals("00"))&&(!P_strLVLNO.equals("0")))
		if(!P_strLVLNO.equals("00"))
	    {
			if((P_strDSLVL.equals("MG"))||(P_strDSLVL.equals("SG"))||(P_strDSLVL.equals("SS"))||(P_strDSLVL.equals("HD")))
			{
				M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
				M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
				M_strSQLQRY += " and ctt_matcd = '"+P_strMATCD.substring(0,6) +"000A'";
				M_strSQLQRY += " and CTT_LVLNO ='"+P_strLVLNO+"'";
				M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
				M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
				M_strSQLQRY += "UNION ";
			}
		}
		M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
		M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='CD' ";
		M_strSQLQRY += " and ctt_matcd = '"+P_strMATCD +"'";
		M_strSQLQRY += " and CTT_LVLNO ='"+P_strLVLNO+"'";
		M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
		M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
		M_strSQLQRY += " Order by ctt_matcd,ctt_lvlno,ctt_linno ";
		L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		// after this the description of code is getting started
		if(L_rstRSSET !=null)
			while(L_rstRSSET.next())
			{
				if(L_rstRSSET.getString("CTT_CODTP").equals("CD"))
				{
					L_COUNT++;
					if(L_COUNT ==1)
					{
						O_DOUT.writeBytes(strPRINT);
						O_DOUT.writeBytes(strPRINT1);
						O_DOUT.writeBytes("\n");
					}
					O_DOUT.writeBytes(padSTRING('L',"",5)); // added on 03/04
					O_DOUT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
					O_DOUT.writeBytes("\n");
					intLINNO += 2;
					if(intLINNO > 66)
					{
						crtLINE(106);
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(O_DOUT,M_strEJT);				
						intLINNO = 0;
						intPAGNO += 1;
						if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER(P_strINDTP); 
					}
			
				}
				else
				{
					if(L_rstRSSET.getString("CTT_CODTP").equals("SS"))
						{
							if(L_rstRSSET.getString("CTT_LVLNO").equals(P_strLVLNO))
							{
								if(pflag)
								{
									//O_DOUT.writeBytes("\n");// -- additional line
									O_DOUT.writeBytes("     ----------*\n");
									intLINNO++;
									if(intLINNO > 66)
									{
											crtLINE(106);
											if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
												prnFMTCHR(O_DOUT,M_strEJT);				
											intLINNO = 0;
											intPAGNO += 1;
											if(M_rdbHTML.isSelected())
												O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
											prnHEADER(P_strINDTP); 
									}
									pflag = false;
									
								}
							}
						}
					O_DOUT.writeBytes(padSTRING('L',"",5)); // added on 03/04
					O_DOUT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
					O_DOUT.writeBytes("\n");
					intLINNO++;
					if(intLINNO > 66)
					{
						crtLINE(106);
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(O_DOUT,M_strEJT);				
						intLINNO = 0;
						intPAGNO += 1;
						if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER(P_strINDTP); 
					}
				}
		}
		O_DOUT.writeBytes("     ----------\n");
		intLINNO++;
		if(intLINNO > 66)
		{
				crtLINE(106);
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);				
				intLINNO = 0;
				intPAGNO += 1;
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				prnHEADER(P_strINDTP); 
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getMATDS");
	}
}
}
