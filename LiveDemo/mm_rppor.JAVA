/*
System Name   : Material Management System
Program Name  : Order Printing
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
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;

public class mm_rppor extends cl_rbase
{
	ResultSet L_rstRSSET,rstRSSET,L_rstSPCRSSET,rstSPCRSSET;
	private JTextField txtFRPOR,txtTOPOR;
	private JComboBox cmbPORTP;
	private String strREPFL = cl_dat.M_strREPSTR_pbst+"mm_rppor.doc";
	private int intLINNO,intPAGNO,intRECCNT;
	private String strPORNO,strAMDNO,strDPTCD,strPORDT,strAMDDT,strURGCD,strVENCD,strPOSRL,strDSTCD,strDELTP,strDLTDS,strDLTDS1,strPMTRF,strCRDDY,strTEMP,strITMDS="";
	private String strQTNNO,strQTNDT,strCURCD,strPCURCD,strCURDS,strCURDS1,strEFFDT,strCMPDT,strVENNM,strSHRDS,strPORVL;
	private String strPREBY,strFRWBY,strAUTBY,strPREDT,strFRWDT,strAUTDT,strPRVPOR ="",strPRVAMD ="";
	private String strISOR1,strISOR2,strISOR3,strARCNO ="";
	private String L_strPRTNO = "";
	private int intSRLNO =0;
	private java.util.Hashtable<String,String> hstMATCD = new java.util.Hashtable<String,String>();
	boolean flgFIRST = false,flgFIRSTTM = true,flgFIRSTTM1 = true;
	private Hashtable<String,String> hst1to100;
	private Hashtable<String,String> hstTAXCD = new Hashtable<String,String>();
	private Hashtable<String,String> hstTAXSP = new Hashtable<String,String>();
	private java.util.Vector<String> vtrTAXCD = new java.util.Vector<String>();
	private java.util.Vector<String> vtrTAXTP = new java.util.Vector<String>();
	private java.util.Vector<String> vtrITTAX = new java.util.Vector<String>();
	private java.util.Vector<String> vtrMATDS = new java.util.Vector<String>();
	private Vector<String> vtrPODEL = new Vector<String>();
	private Vector<String> vtrDLSCH = new Vector<String>();
	private boolean	flgITTAX = false;
    private boolean flgEXCFL = false;
	private boolean flgTXSPC = false;
	double dblEXGRT=0.0;
	private final String strARCCD_fn ="03";	
	private final String strDORCD_fn ="DO";	
	String strDOSRL ="26";
	StringTokenizer L_STRTKN;
	String strPRINT ="",strPRINT1;
	String strSTRTP ="";	
	String strMATCD;
	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;
	mm_rppor()
	{
		super(2);

		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("P.O. Type "),2,3,1,1,this,'L');
			add(cmbPORTP = new JComboBox(),2,4,1,2,this,'L');
			M_rstRSSET =cl_dat.exeSQLQRY("Select CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='MMXXPOT' and isnull(cmt_stsfl,'') <>'X'");
			
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				String L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),3) +M_rstRSSET.getString("CMT_CODDS");
				cmbPORTP.addItem(L_strDATA);
				System.out.println("1");
			}
			// hard coded for delivery order
			cmbPORTP.addItem("DO Delivery Order");
			add(new JLabel("From P.O. "),3,4,1,1,this,'L');
			add(txtFRPOR = new TxtLimit(8),4,4,1,1,this,'L');
			add(new JLabel("To P.O."),3,5,1,1,this,'L');
			add(txtTOPOR = new TxtLimit(8),4,5,1,1,this,'L');
			txtFRPOR.requestFocus();
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			crtHST();
			crtHSTXCD();
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 *  This constuctor is for calling value calculation procedure from outside.
	 */
	mm_rppor(String P_strSBSCD,Hashtable<String,String> P_hstTAXCD,Vector<String> P_vtrTAXCD,Vector<String> P_vtrTAXTP,String P_strCURCD,double P_dblEXGRT)
	{
		hstTAXCD = P_hstTAXCD;
		vtrTAXCD = P_vtrTAXCD;
		vtrTAXTP = P_vtrTAXTP;
		strPCURCD ="";
		strCURCD = P_strCURCD;
		M_strSBSCD = P_strSBSCD;
		dblEXGRT = P_dblEXGRT;
		crtHST();
	}
	mm_rppor(String P_strSBSCD)
	{
		M_strSBSCD = P_strSBSCD;
		crtHST();
		crtHSTXCD();
		M_rdbTEXT.setSelected(true);
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		String L_ACT = L_AE.getActionCommand();
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			strSTRTP =M_strSBSCD.substring(2,4);
		}
	}
	public void keyPressed(KeyEvent L_KE){
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == 9 || L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{ 
			if (M_objSOURC == cmbPORTP)
			{
				txtFRPOR.requestFocus();		
			}
			if (M_objSOURC == txtFRPOR)
			{
				if(vldPORNO(M_strSBSCD.substring(2,4),txtFRPOR.getText().trim()))
				{
					txtTOPOR.requestFocus();		
					txtTOPOR.setText(txtFRPOR.getText().trim());
				}
			
			}
			else if (M_objSOURC == txtTOPOR)
			{
				if(vldPORNO(M_strSBSCD.substring(2,4),txtTOPOR.getText().trim()))
					cl_dat.M_btnSAVE_pbst.requestFocus();		
			}
		}
		if (L_KE.getKeyCode()== L_KE.VK_F1){					//F1 starts from here
			if((M_objSOURC == txtFRPOR)||(M_objSOURC == txtTOPOR))
			{
				cl_dat.M_flgHELPFL_pbst = true;
				if(M_objSOURC == txtFRPOR)
					M_strHLPFLD = "txtFRPOR";
				else if(M_objSOURC == txtTOPOR)
					M_strHLPFLD = "txtTOPOR";
				String L_ARRHDR[] = {"P.O No.","Amd. No.","Dept. code"};
				M_strSQLQRY = "Select distinct PO_PORNO,PO_AMDNO,PO_DPTCD ";
				M_strSQLQRY += "  from MM_POMST where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'') <>'X'";
				M_strSQLQRY += " and PO_MMSBS ='"+M_strSBSCD+"'";
				M_strSQLQRY += " and PO_STRTP ='"+strSTRTP+"'";
				if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))
					M_strSQLQRY += " and SUBSTRING(PO_PORNO,2,2) ='"+strDOSRL +"'";
				else 
					M_strSQLQRY += " and PO_PORTP ='"+cmbPORTP.getSelectedItem().toString().substring(0,2)+"'";
				if(M_objSOURC == txtFRPOR)
				{
					if(txtFRPOR.getText().trim().length() >0)
						M_strSQLQRY +=" AND PO_PORNO like '"+txtFRPOR.getText().trim()+"%'";
				}
				if(M_objSOURC == txtTOPOR)
				{
					if(txtTOPOR.getText().trim().length() >0)
						M_strSQLQRY +=" AND PO_PORNO like '"+txtTOPOR.getText().trim()+"%'";
				}
				M_strSQLQRY += " order by PO_PORNO DESC";
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
			if(M_strHLPFLD.equals("txtFRPOR"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtFRPOR.setText(cl_dat.M_strHLPSTR_pbst);
				txtTOPOR.setText(txtFRPOR.getText().trim());
		
			}
			else if(M_strHLPFLD.equals("txtTOPOR"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtTOPOR.setText(cl_dat.M_strHLPSTR_pbst);
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"exeHLPOK");
		}
	}
	// Method to validate the P.O. No.
	private boolean vldPORNO(String P_strSTRTP,String P_strPORNO){
		try{
			M_strSQLQRY = "Select PO_PORNO from MM_POMST ";
            M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'')  <> 'X'";
			M_strSQLQRY += " and PO_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " and PO_STRTP ='"+P_strSTRTP+"'";
			if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals("DO"))
				M_strSQLQRY += " and SUBSTRING(PO_PORNO,2,2) ='"+strDOSRL +"'";
			else 
					M_strSQLQRY += " and PO_PORTP ='"+cmbPORTP.getSelectedItem().toString().substring(0,2)+"'";
			M_strSQLQRY += " and PO_PORNO ='"+P_strPORNO +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				return true;	
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"vldPORNO");
			return false;

		}
		setMSG("Invalid P.O. number ..",'E');
		return false;
	}
	boolean vldDATA()
	{
		boolean L_flgRPT = false;
		if(txtFRPOR.getText().trim().length() ==0)
		{
			setMSG("from P.O. No. can not be blank..",'E');
			txtFRPOR.requestFocus();
			return false;
		}
		else if(txtTOPOR.getText().trim().length() ==0)
		{
			setMSG("To P.O. No. can not be blank..",'E');
			txtTOPOR.requestFocus();
			return false;
		}
		
		else if(!vldPORNO(strSTRTP,txtFRPOR.getText().trim()))
		{
			setMSG("Invalid From P.O. No...",'E');
			txtFRPOR.requestFocus();
			return false;
		}
		else if(!vldPORNO(strSTRTP,txtTOPOR.getText().trim()))
		{
			setMSG("Invalid To P.O. No...",'E');
			txtTOPOR.requestFocus();
			return false;
		}
		if(!cmbPORTP.getSelectedItem().toString().substring(0,2).equals("05")) // Project P.O.
		if(Integer.parseInt(txtTOPOR.getText().trim()) <Integer.parseInt(txtFRPOR.getText().trim()))
		{
			setMSG("To P.O. No. can not be less than from P.O...",'E');
			txtTOPOR.requestFocus();
			return false;
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if(M_cmbDESTN.getItemCount() ==0)
			{
				setMSG("Please select E-mail Id by using the F1 list ..",'E');
				return false;
			}
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{
			if(M_cmbDESTN.getItemCount() ==0)
			{
				setMSG("Please select a valid Printer ..",'E');
				return false;
			}
		}
	return true;
	}
	private void prnMNHDR(String P_strDOCTP)
	{
		try
		{
			strVENNM ="";
			String L_strADR01 ="";
			String L_strADR02 ="";
			String L_strADR03 ="";
			String L_strADR04 ="";
			String L_strCTYNM ="";
			String L_strCONNM ="";
			String L_strPINCD ="";
			M_strSQLQRY = "Select PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM,PT_CONNM,PT_PINCD FROM CO_PTMST WHERE ";
			M_strSQLQRY +=" PT_PRTTP ='S' AND PT_PRTCD ='"+strVENCD+"' and isnull(pt_stsfl,'') <>'X'";
			rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(rstRSSET !=null)
			if(rstRSSET.next())
			{
				strVENNM = nvlSTRVL(rstRSSET.getString("PT_PRTNM"),"");
				L_strADR01 = nvlSTRVL(rstRSSET.getString("PT_ADR01"),"");
				L_strADR02 = nvlSTRVL(rstRSSET.getString("PT_ADR02"),"");
				L_strADR03 = nvlSTRVL(rstRSSET.getString("PT_ADR03"),"");
				L_strADR04 = nvlSTRVL(rstRSSET.getString("PT_ADR04"),"");
				L_strCTYNM = nvlSTRVL(rstRSSET.getString("PT_CTYNM"),"");
				L_strCONNM = nvlSTRVL(rstRSSET.getString("PT_CONNM"),"");
				
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strCPI17);
			O_DOUT.writeBytes(padSTRING('L',"-------------------------------",152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',strISOR1,152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',strISOR2,152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',strISOR3,152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"-------------------------------",152));
			O_DOUT.writeBytes("\n");
			M_strSQLQRY = "Select CMT_CODDS,CMT_CHP01 FROM CO_CDTRN WHERE ";
			M_strSQLQRY +=" CMT_CGMTP ='MST' AND CMT_CGSTP ='COXXCUR' AND CMT_CODCD ='"+strCURCD+"'";
			M_strSQLQRY +=" AND isnull(CMT_STSFL,'') <>'X'";
			rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(rstRSSET !=null)
			if(rstRSSET.next())
			{
				strCURDS = nvlSTRVL(rstRSSET.getString("CMT_CODDS"),"");
				strCURDS1 = nvlSTRVL(rstRSSET.getString("CMT_CHP01"),"");
			}
			O_DOUT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(O_DOUT,M_strNOCPI17);
				prnFMTCHR(O_DOUT,M_strCPI10);
				prnFMTCHR(O_DOUT,M_strENH);
				prnFMTCHR(O_DOUT,M_strBOLD);
			}
			if(M_rdbHTML.isSelected())
			{
				O_DOUT.writeBytes("<b><center>");
			}
			O_DOUT.writeBytes("\n");
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))
			{
				O_DOUT.writeBytes(padSTRING('L',"",12));
				O_DOUT.writeBytes("ANNUAL RATE CONTRACT");
			}
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn)) // Delivery Order
			{
				O_DOUT.writeBytes(padSTRING('L',"",15));
				O_DOUT.writeBytes("DELIVERY ORDER ");
			}
			else
			{
				if(M_rdbTEXT.isSelected())
					O_DOUT.writeBytes(padSTRING('L',"",15));
				O_DOUT.writeBytes("PURCHASE ORDER ");
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(O_DOUT,M_strNOBOLD);
				prnFMTCHR(O_DOUT,M_strNOENH);
				prnFMTCHR(O_DOUT,M_strCPI10);
				prnFMTCHR(O_DOUT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("</center></b>");
			O_DOUT.writeBytes("\n\n");
			O_DOUT.writeBytes(padSTRING('R',"Attn: "+L_strCONNM,65));	
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))
				O_DOUT.writeBytes("ARC  Series           : "+strPOSRL);
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))
				O_DOUT.writeBytes("D.O. Series           : "+strPOSRL);
			else	
				O_DOUT.writeBytes("P.O. Series           : "+strPOSRL);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"Supplier Code      : "+strVENCD,65));
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))
				O_DOUT.writeBytes("ARC  No.              : "+strSTRTP+"/"+strPORNO );
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))
				O_DOUT.writeBytes("D.O. No.              : "+strSTRTP+"/"+strPORNO );
			else
				O_DOUT.writeBytes("P.O. No.              : "+strSTRTP+"/"+strPORNO );
			O_DOUT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("<b>");
			O_DOUT.writeBytes(padSTRING('R',strVENNM,65));	
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("</b>");
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))	
				O_DOUT.writeBytes("ARC  Date             : "+strPORDT );
			else if(P_strDOCTP.equals(strDORCD_fn))
			//	else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))	
				O_DOUT.writeBytes("D.O. Date             : "+strPORDT );
			else
				O_DOUT.writeBytes("P.O. Date             : "+strPORDT );
			O_DOUT.writeBytes("\n");
			
			O_DOUT.writeBytes(padSTRING('R',L_strADR01,65));	
			O_DOUT.writeBytes("Quotation No          : "+strQTNNO);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_strADR02,65));	
			O_DOUT.writeBytes("Quotation Date        : "+strQTNDT);
			O_DOUT.writeBytes("\n");
			
			O_DOUT.writeBytes(padSTRING('R',L_strADR03,65));	
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))	
				O_DOUT.writeBytes("ARC  Effective Date   : "+strEFFDT);
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))	
				O_DOUT.writeBytes("D.O. Effective Date   : "+strEFFDT);
			else	
				O_DOUT.writeBytes("P.O. Effective Date   : "+strEFFDT);
			O_DOUT.writeBytes("\n");
			
			O_DOUT.writeBytes(padSTRING('R',L_strADR04,65));	
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))	
				O_DOUT.writeBytes("ARC  Completion Date  : "+strCMPDT);
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))	
			{
				O_DOUT.writeBytes("D.O. Completion Date  : "+strCMPDT);
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',"",65));	
				O_DOUT.writeBytes("ARC Number            : "+strARCNO);	
				
			}
			else
				O_DOUT.writeBytes("P.O. Completion Date  : "+strCMPDT);
			O_DOUT.writeBytes("\n");
			
			O_DOUT.writeBytes("\n");
			crtLINE(106);
			O_DOUT.writeBytes("\n");
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))	
				O_DOUT.writeBytes("ARC Short Desc. : "+strSHRDS);
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))	
				O_DOUT.writeBytes("D.O. Short Desc.: "+strSHRDS);
			else
				O_DOUT.writeBytes("P.O.Short Desc. : "+strSHRDS);
			O_DOUT.writeBytes("\n");
			crtLINE(106);
			O_DOUT.writeBytes("\n");
			if(P_strDOCTP.equals(strDORCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))	
				intLINNO = 21;
			else intLINNO = 20;
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))	
			{
				O_DOUT.writeBytes("With reference to our enquiry and your offer, as referred above,  we are pleased to release this\n");
				O_DOUT.writeBytes("Annual rate contract for items mentioned below  in accordance with conditions made part of this contract.\n");
				O_DOUT.writeBytes("Receipt of this Annual Rate Contract and its acceptance must be acknowledged on the  confirmation  copy\n");
				O_DOUT.writeBytes("within 15 days of receipt of this contract, failing which contract will be deemed to have been accepted\n");
				O_DOUT.writeBytes("by you. Material to be supplied as and when required by SPL for which we will release delivery order \n");
				O_DOUT.writeBytes("against above Annual Rate Contract. Delivery order Number must appear on all challans, invoices and \n");
				O_DOUT.writeBytes("further correspondence.\n");
				intLINNO += 7;
			}
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))	
			{
				O_DOUT.writeBytes("With reference to above mentioned Annual Rate Contract we are pleased to release this Delivery order for\n");
				O_DOUT.writeBytes("the items mentioned below,in accordance with terms and conditions made part of above mentioned contract.\n");
				O_DOUT.writeBytes("Completion/Delivery time must be strictly adhered to.Delivery order no. must appear on all challans, \n");
				O_DOUT.writeBytes("invlices and further correspondence.\n");
				intLINNO += 4;
			}
			else
			{
				O_DOUT.writeBytes("With reference to our enquiry and your offer, as referred above,  we are pleased to release this\n");
				O_DOUT.writeBytes("purchase order for items mentioned below  in accordance with conditions made part of this order.\n");
				O_DOUT.writeBytes("Receipt of this purchase order and its acceptance must be acknowledged on the confirmation  copy\n");
				O_DOUT.writeBytes("within 15 days of receipt of this order,failing which order will be deemed to have been accepted\n");
				O_DOUT.writeBytes("by you.  Completion / Delivery time  must be strictly adhered to.  PO Number  must appear on all\n");
				O_DOUT.writeBytes("challans, invoices and further correspondence.\n");
				intLINNO += 6;
			}
			
			if (flgFIRSTTM==true)
			{	
				prnCOLHDR();
				flgFIRSTTM = false;
			}				
		}
		catch(Exception L_E)
		{
			
		}
	}
	private void prnPGHDR(String P_strPORNO,String P_strDOCTP)
	{  
		try
		{
		if(flgFIRSTTM1 == false)	
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(O_DOUT,M_strCPI10);
				prnFMTCHR(O_DOUT,M_strCPI17);
			}
			O_DOUT.writeBytes(padSTRING('L',"-------------------------------",152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',strISOR1,152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',strISOR2,152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',strISOR3,152));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"-------------------------------",152));
			O_DOUT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(O_DOUT,M_strNOCPI17);
				prnFMTCHR(O_DOUT,M_strCPI10);
				prnFMTCHR(O_DOUT,M_strCPI12);
			}
			O_DOUT.writeBytes(padSTRING('R',"Supplier Code      : "+strVENCD,65));	
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))
				O_DOUT.writeBytes("ARC  No.              : "+strSTRTP+"/"+P_strPORNO );
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))
				O_DOUT.writeBytes("D.O. No.              : "+strSTRTP+"/"+P_strPORNO );
			else	
				O_DOUT.writeBytes("P.O. No.              : "+strSTRTP+"/"+P_strPORNO );
			O_DOUT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("<b>");
			O_DOUT.writeBytes(padSTRING('R',strVENNM,65));	
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("</b>");
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))
				O_DOUT.writeBytes("ARC  Date             : "+strPORDT );
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))
				O_DOUT.writeBytes("D.O. Date             : "+strPORDT );
			else
				O_DOUT.writeBytes("P.O. Date             : "+strPORDT );
			O_DOUT.writeBytes("\n");
			crtLINE(106);
			O_DOUT.writeBytes("\n");
			if(P_strDOCTP.equals(strARCCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))
				O_DOUT.writeBytes("ARC Short Desc. : "+strSHRDS);
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))
				O_DOUT.writeBytes("D.O. Short Desc.: "+strSHRDS);
			else	
				O_DOUT.writeBytes("P.O.Short Desc. : "+strSHRDS);
			O_DOUT.writeBytes("\n");
			crtLINE(106);
			O_DOUT.writeBytes("\n");
			intLINNO +=10;
		}
			flgFIRSTTM1 = false;				
		}
		catch(Exception L_exEXCP)
		{
			setMSG(L_exEXCP,"prnhead......");
		}
	}
//PRINTS COLUMN HEADER	
	void prnCOLHDR()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("<b>");
			crtLINE(106);
			O_DOUT.writeBytes("\nSrl  Ind. No.  Material Code       UOM                 * T.C/Ins.       Order Qty.   Unit Rate      Amount");
			O_DOUT.writeBytes("\nNo.            Description                                                          ( " +padSTRING('R',strCURDS +" )",10)+" Del.Sched.\n");

			crtLINE(106);
			O_DOUT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("</b>");
			
			intLINNO += 4;
		}
		catch(Exception L_exEXCP)
		{
			setMSG(L_exEXCP,"prnhead......");
		}
	}	
//	
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
	private void prnFOOTR(String P_strPORNO,String P_strAMDNO,String P_strDOCTP)
	{
		try
		{
			///// added on 26/05
			if(P_strPORNO.trim().length() ==0)
			{
				P_strPORNO = strPORNO;
				P_strAMDNO = strAMDNO;
			}
			ResultSet L_rstTXDTL,L_rstSPCTXDTL;
			String L_strTXSTR="",L_strCOLNM,L_strTAXDS,L_strTAXFL,L_strTEMP,L_strREMDS;
			double L_strTAXVL =0;
			O_DOUT.writeBytes ("\nCommon Tax Details :-");
			O_DOUT.writeBytes ("\n");
			intLINNO +=2;
		//correct sequence
			if(intLINNO > 63)
			{
				crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO,P_strDOCTP); 
			}
		//PRINTING COMMON TAX DETAILS
			M_strSQLQRY ="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXTAX' AND CMT_CHP01 ='01' AND isnull(CMT_STSFL,'') <>'X' ORDER BY CMT_CCSVL";
			rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			M_strSQLQRY ="Select * from CO_TXDOC where TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSCD ='"+M_strSBSCD+"' and TX_DOCNO ='"+P_strPORNO+"' and TX_PRDCD ='XXXXXXXXXX' AND isnull(TX_STSFL,'') <>'X'"; 
			L_rstTXDTL = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstTXDTL !=null)
			if(L_rstTXDTL.next())
			{
				if(rstRSSET !=null)
				while(rstRSSET.next())
				{
					L_strTAXDS ="";
					L_strCOLNM ="";
					L_strTAXVL =0;
					L_strTEMP = nvlSTRVL(rstRSSET.getString("CMT_CODCD"),"");
					L_strTAXDS =nvlSTRVL(rstRSSET.getString("CMT_CODDS"),"");
					if((L_strTEMP.trim().equals("STX")) ||(L_strTEMP.trim().equals("OTH")))
					{
						L_strTAXDS = nvlSTRVL(L_rstTXDTL.getString("TX_"+L_strTEMP+"DS"),"");
					}
					L_strCOLNM = "TX_"+L_strTEMP+"FL";
					L_strTAXFL = nvlSTRVL(L_rstTXDTL.getString(L_strCOLNM),"");
					L_strTAXVL = L_rstTXDTL.getFloat("TX_"+L_strTEMP+"VL");
					if(L_strTAXFL.length() > 0 )
					{
						if(L_strTAXVL == 0)
						{
			                    L_strTXSTR =L_strTAXDS;
			                if((L_strTEMP.trim().equals("STX")) ||(L_strTEMP.trim().equals("OTH")))
							{
								L_strTXSTR +="";
							}
							else
							{
								if(L_strTAXFL.equals("I"))
			                            L_strTXSTR +=" : INCLUDED";
			                    else if(L_strTAXFL.equals("X"))            
			                         L_strTXSTR +=" : AT ACTUALS";
							}
			            }
			            else
						{
							
							if(L_strTAXFL.equals("P"))
							{
								L_strTXSTR +=L_strTAXDS +" : "+setNumberFormat(L_strTAXVL,3)+" % ";
							}
							else
								L_strTXSTR +=L_strTAXDS +" : "+setNumberFormat(L_strTAXVL,3);
						}
						O_DOUT.writeBytes(padSTRING('L',"",20));
						O_DOUT.writeBytes(L_strTXSTR);
						O_DOUT.writeBytes("\n");
						intLINNO++;
						if(intLINNO > 63)
						{
							crtLINE(106);
							O_DOUT.writeBytes ("\n");
							intPAGNO += 1;
							O_DOUT.writeBytes("Contd..."+intPAGNO);
							O_DOUT.writeBytes ("\n");
							O_DOUT.writeBytes ("\n");						
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(O_DOUT,M_strEJT);
							if(M_rdbHTML.isSelected())
								O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
							intLINNO = 0;	
							prnPGHDR(P_strPORNO,P_strDOCTP); 
						}
								L_strTXSTR ="";
					}
				}
			}
//---------------------------------------------------
//PRINTING Common SPECIFIC TAX DETAILS
			flgTXSPC = false;
			M_strSQLQRY ="Select * from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SBSCD ='"+M_strSBSCD+"' and TXT_DOCNO ='"+P_strPORNO+"' AND TXT_PRDCD ='XXXXXXXXXX' AND isnull(TXT_STSFL,'') <>'X'"; 
			L_rstSPCTXDTL = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstSPCTXDTL !=null)
			{
				while(L_rstSPCTXDTL.next())
				{
					flgTXSPC = true;
					L_strTAXDS ="";
					L_strCOLNM ="";
					L_strTAXVL =0;
					L_strTAXDS = L_rstSPCTXDTL.getString("TXT_CODDS");
					L_strTAXVL = L_rstSPCTXDTL.getDouble("TXT_CODVL");
					L_strTAXFL = L_rstSPCTXDTL.getString("TXT_CODFL");
						if(L_strTAXFL.equals("P"))
						{
							L_strTXSTR +=L_strTAXDS +" : "+setNumberFormat(L_strTAXVL,3)+" % ";
						}
						else
						{
						    if(L_strTAXVL > 0)
							    L_strTXSTR +=L_strTAXDS +" : "+setNumberFormat(L_strTAXVL,3);
							else
							{ 
							    if(L_strTAXFL.equals("I"))
			                     L_strTXSTR +=L_strTAXDS +" : INCLUDED";
			                    else if(L_strTAXFL.equals("X"))            
			                         L_strTXSTR +=L_strTAXDS +" : AT ACTUALS";
							}
						}
						
					O_DOUT.writeBytes(padSTRING('L',"",20));
					O_DOUT.writeBytes(L_strTXSTR);
					O_DOUT.writeBytes("\n");
					intLINNO++;
					if(intLINNO > 63)
					{
						crtLINE(106);
						O_DOUT.writeBytes ("\n");
						intPAGNO += 1;
						O_DOUT.writeBytes("Contd..."+intPAGNO);
						O_DOUT.writeBytes ("\n");
						O_DOUT.writeBytes ("\n");
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(O_DOUT,M_strEJT);
						if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
						intLINNO = 0;
						prnPGHDR(P_strPORNO,P_strDOCTP); 
					}
						L_strTXSTR ="";
				}		
			}			
		//}
//---------------------------------------------------			
			O_DOUT.writeBytes("Payment Terms     :- "+strPMTRF +" "+"("+strCRDDY+")");
			O_DOUT.writeBytes ("\n");
			O_DOUT.writeBytes("Order Price Basis :- ");
			if(strDELTP.equals("02"))
		    {
				O_DOUT.writeBytes(" DELIVERED AT");
				if(strDSTCD.equals("NGT"))
					O_DOUT.writeBytes(" SPL PLANT");
				else if(strDSTCD.equals("ADR"))	
				O_DOUT.writeBytes(" SPL ANDHERI");
			    else if(strDSTCD.equals("MUM"))	
				O_DOUT.writeBytes(" SPL ANDHERI");
			    else if(strDSTCD.equals("CHN"))	
				O_DOUT.writeBytes(" SPL CHENNAI");
			    else if(strDSTCD.equals("CHE"))	
				O_DOUT.writeBytes(" CHENNAI(ENNORE)");
			    else if(strDSTCD.equals("TRN"))	
				O_DOUT.writeBytes(" TRANSPORTER GODOWN");
					
			}
			if(strDELTP.equals("01"))
		    {
				//O_DOUT.writeBytes(strDLTDS);
                O_DOUT.writeBytes(strDLTDS+" Delivery At : "+strDLTDS1);
			}
			if(strDELTP.equals("03")||strDELTP.equals("04")||strDELTP.equals("05"))
				O_DOUT.writeBytes(strDLTDS+" "+strDLTDS1);
			O_DOUT.writeBytes ("\n");
			intLINNO +=2;
			if(intLINNO > 63)
			{
				crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO,P_strDOCTP); 
			}
			crtLINE(106);
			O_DOUT.writeBytes ("\n");
			strPORVL ="0.0";
			strPORVL = calPOVAL(P_strPORNO,P_strAMDNO,'P');
			String L_strINWRD = getCURWRD(strPORVL,strCURDS1);
			if((strCURDS+L_strINWRD).length() >65)
			{
				O_DOUT.writeBytes(padSTRING('R',"(In Words) "+strCURDS+" "+L_strINWRD.substring(0,55)+"-",80)+"Order Value "+padSTRING('L',strPORVL,13));
				O_DOUT.writeBytes ("\n");			
				intLINNO++;
				O_DOUT.writeBytes(padSTRING('L',"",11));
				O_DOUT.writeBytes(L_strINWRD.substring(55));
				O_DOUT.writeBytes ("\n");			
				intLINNO++;
			}
			else
				O_DOUT.writeBytes(padSTRING('R',"(In Words) "+strCURDS+" "+L_strINWRD,80)+"Order Value "+padSTRING('L',strPORVL,13));
			O_DOUT.writeBytes ("\n");
			crtLINE(106);
			
			O_DOUT.writeBytes ("\n");
			intLINNO+=3;
			if(intLINNO > 63)
			{
				crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO,P_strDOCTP); 
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(O_DOUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("<b>");
			O_DOUT.writeBytes ("\n");
			intLINNO +=1;
			if(intLINNO > 63)
			{
				crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO,P_strDOCTP); 
			}
			O_DOUT.writeBytes ("* T.C./Ins.: Test Certificate Required / Inspection Prior to Despatch");
			O_DOUT.writeBytes ("\n");
			intLINNO++;
			if(intLINNO > 63)
			{
				crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO,P_strDOCTP); 
			}
			O_DOUT.writeBytes ("Remarks    : ");
			M_strSQLQRY = "SELECT * FROM MM_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND RM_STRTP ='"+strSTRTP +"'";
			M_strSQLQRY += " AND RM_DOCTP ='PO'";
			M_strSQLQRY += " AND RM_TRNTP ='PO'";
			M_strSQLQRY += " AND RM_REMTP ='POR'";
			M_strSQLQRY += " AND RM_DOCNO ='"+strPORNO+"'";
			M_strSQLQRY += " AND isnull(RM_STSFL,'') <>'X'";
			rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(rstRSSET !=null)
			while(rstRSSET.next())
			{
				if(rstRSSET.getString("RM_REMTP").equals("POR"))
				{
					L_strREMDS = nvlSTRVL(rstRSSET.getString("RM_REMDS"),"");
					if(L_strREMDS.length() >97)
					{
						O_DOUT.writeBytes(L_strREMDS.substring(0,95)+"-");
						O_DOUT.writeBytes ("\n");			
						intLINNO++;
						O_DOUT.writeBytes(padSTRING('L',"",10));
						O_DOUT.writeBytes(L_strREMDS.substring(95));
						O_DOUT.writeBytes ("\n");			
					}
					else
						O_DOUT.writeBytes(L_strREMDS);
				}
			}
			if(rstRSSET !=null)
				rstRSSET.close();
			O_DOUT.writeBytes ("\n");
			intLINNO++;
			if(intLINNO > 63)
			{
				crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO,P_strDOCTP); 
			}
			if(P_strDOCTP.equals(strDORCD_fn))
			//if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strARCCD_fn))	
				O_DOUT.writeBytes("VENDOR SHOULD QUOTE HIS NAME,INVOICE NUMBER AND OUR D.O.NO. ON EACH PACKED BOX PRIOR TO DISPATCH.");
			else if(P_strDOCTP.equals(strDORCD_fn))
			//else if(cmbPORTP.getSelectedItem().toString().substring(0,2).equals(strDORCD_fn))	
				O_DOUT.writeBytes("VENDOR SHOULD QUOTE HIS NAME,INVOICE NUMBER AND OUR D.O.NO. ON EACH PACKED BOX PRIOR TO DISPATCH.");
			else
				O_DOUT.writeBytes("VENDOR SHOULD QUOTE HIS NAME,INVOICE NUMBER AND OUR P.O.NO. ON EACH PACKED BOX PRIOR TO DISPATCH.");
			O_DOUT.writeBytes ("\n");
			intLINNO+=2;
            if(intLINNO > 49)
            {

               	crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				for(int i=0;i<63-intLINNO;i++)
				{
					O_DOUT.writeBytes ("\n");
				}
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO,P_strDOCTP);
			}
            if(flgEXCFL)
            {
                O_DOUT.writeBytes("Kindly send us confirmation/certification  within 7 days  from the date of actual excise duty paid, with\n");
                O_DOUT.writeBytes("details of Invoice No.& Date,Invoice amount Duty payable, Duty paid, serial No. of cenvat/PLA account and \n");
                O_DOUT.writeBytes("date.Failureto provide the certificate within time limit will compel us to raise debit note equivalent\n");
                O_DOUT.writeBytes("to the excise duty applicable & will be recovered from you.\n");
                O_DOUT.writeBytes ("\n");
                intLINNO+=4;
                flgEXCFL = false;
            }
			crtLINE(106);
			O_DOUT.writeBytes ("\n");
			O_DOUT.writeBytes (padSTRING('R',"Supplier's Acceptance",38)+padSTRING('R',"Prepared & Checked By",38)+padSTRING('R',"For Supreme Petrochem Ltd ",36));
			O_DOUT.writeBytes ("\n");
			if(strAUTBY.trim().length() >0)
				strAUTBY ="NG";
			O_DOUT.writeBytes(padSTRING('R'," ",38)+padSTRING('R',strPREBY,38)+padSTRING('R',strAUTBY,36));
			strAUTBY ="";
			O_DOUT.writeBytes ("\n");
			O_DOUT.writeBytes ("\n");
			O_DOUT.writeBytes ("\n");
			intLINNO+=5;
			O_DOUT.writeBytes (padSTRING('R',"",70)+padSTRING('L',"Authorised Signatory ",36));
			O_DOUT.writeBytes ("\n");
			intLINNO++;
			crtLINE(106);
			O_DOUT.writeBytes ("\n");
			for(int i=0;i<61-intLINNO;i++)
			{
				O_DOUT.writeBytes ("\n");
			}
			crtLINE(106);
			O_DOUT.writeBytes ("\n");
			M_strSQLQRY ="Select CP_CSTNO,CP_CSTDT,CP_BSTNO,CP_BSTDT,CP_ECCNO,CP_VATNO,CP_VATDT from co_CPMST ";
			M_strSQLQRY+= "WHERE CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(rstRSSET !=null)
			if(rstRSSET.next())
			{
				String L_strCSTNO = nvlSTRVL(rstRSSET.getString("CP_CSTNO"),"");
				String L_strCSTDT = nvlSTRVL(rstRSSET.getString("CP_CSTDT"),"");
			//	String L_strSSTNO = nvlSTRVL(rstRSSET.getString("CP_BSTNO"),"");
			//	String L_strSSTDT = nvlSTRVL(rstRSSET.getString("CP_BSTDT"),"");
            	String L_strSSTNO = nvlSTRVL(rstRSSET.getString("CP_VATNO"),"");
				String L_strSSTDT = nvlSTRVL(rstRSSET.getString("CP_VATDT"),"");
						
	            String L_strECCNO = nvlSTRVL(rstRSSET.getString("CP_ECCNO"),"");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<b>");
				// changed on 04/08/2006
                ////O_DOUT.writeBytes("CST NO.: "+L_strCSTNO+" wef."+L_strCSTDT+"     BST No.: "+L_strSSTNO+" wef."+L_strSSTDT +"     ECC NO.: "+L_strECCNO);
                    O_DOUT.writeBytes("CST TIN NO.: "+L_strCSTNO+" wef."+L_strCSTDT+"   VAT TIN No.: "+L_strSSTNO+" wef."+L_strSSTDT +"   ECC NO.: "+L_strECCNO);
				//CST TIN NO : 27530283527C W.E.F 01/04/06 
               // VAT TIN NO : 27530283527V W.E.F 01/04/06 
                O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("</b>");
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(O_DOUT,M_strEJT);				
				prnFMTCHR(O_DOUT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
				O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
			if(rstRSSET !=null)
				rstRSSET.close();
			intLINNO = 0;
			intPAGNO = 1;
			
			M_strSQLQRY = "SELECT PO_PORMK from MM_PORMK WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND PO_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
			M_strSQLQRY += " AND PO_PORNO ='"+strPORNO.trim()+"'";
			M_strSQLQRY += " AND isnull(PO_STSFL,'') <>'X'";
			rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(rstRSSET !=null)
			if(rstRSSET.next())
			{
				prnPGHDR(P_strPORNO,P_strDOCTP); 
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(nvlSTRVL(rstRSSET.getString("PO_PORMK"),""));
				O_DOUT.writeBytes("\n");
			}
			if(rstRSSET !=null)
				rstRSSET.close();
			flgFIRSTTM = true;
			flgFIRSTTM1 = true;				
			
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
				if(M_rdbHTML.isSelected())
			     strREPFL = cl_dat.M_strREPSTR_pbst + "mm_rppor.html";
				if(M_rdbTEXT.isSelected())
				     strREPFL = cl_dat.M_strREPSTR_pbst + "mm_rppor.doc";
		
				getALLREC(txtFRPOR.getText(),txtTOPOR.getText(),cmbPORTP.getSelectedItem().toString().substring(0,2)); 
				strPRVPOR ="";
				strPRVAMD ="";
				strPCURCD ="";
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
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strREPFL,"Purchase Order"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
			    }
				if(O_DOUT !=null)
					O_DOUT.close();
				if(O_FOUT !=null)
					O_FOUT.close();
			}//vldDATA
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
	public void getALLREC(String P_strFRPOR,String P_strTOPOR,String P_strDOCTP)
	{
		ResultSet L_rstRSPOS;
		java.sql.Timestamp L_tmsTEMP;
		java.sql.Date L_datTEMP;
		String L_strLVLNO ="",L_strINDNO="",L_strPMATCD ="",L_strMATCD="",L_strDPTCD="",L_strORDQT ="0",L_strDSCTP;
		String L_strPRVLVL="";
		String L_strPREBY="",L_strFRWBY="",L_strAUTBY="",L_strPREDT="",L_strFRWDT="",L_strAUTDT="";
		String L_strPRVAM ="00";
		String L_strSQLQRY ="";
		double L_dblORDQT =0;
		double L_dblUNTRT =0;
		double L_dblPOAMT =0;
		try
		{
			intSRLNO =0;
			intRECCNT = 0;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Progress....",'N');
			O_FOUT = new FileOutputStream(strREPFL);	
			O_DOUT = new DataOutputStream(O_FOUT);
			strSTRTP = M_strSBSCD.substring(2,4);
			intLINNO = 0;
			intPAGNO = 1;
			if(M_rdbHTML.isSelected())
			{
			    O_DOUT.writeBytes("<HTML><HEAD><Title>Purchase Order </title> ");
				O_DOUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				O_DOUT.writeBytes("</STYLE></HEAD>"); 
				O_DOUT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
		
			strISOR1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPPOR01");
			strISOR2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPPOR02");
			strISOR3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPPOR03");
			M_strSQLQRY =" select * from CO_CTMST,MM_POMST LEFT OUTER JOIN CO_TXDOC ON ";
			M_strSQLQRY +=" PO_CMPCD = TX_CMPCD and PO_MMSBS = TX_SBSCD AND PO_STRTP = TX_SBSTP AND PO_PORNO = TX_DOCNO AND PO_MATCD = TX_PRDCD and tx_prdcd <>'XXXXXXXXXX' AND isnull(TX_STSFL,'') <>'X'";
			M_strSQLQRY +=" where PO_MATCD = CT_MATCD ";
			M_strSQLQRY += " AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+strSTRTP+"'";
			M_strSQLQRY += " AND PO_PORNO BETWEEN  '"+P_strFRPOR+"'";
			M_strSQLQRY += " AND '"+P_strTOPOR+"'";
			M_strSQLQRY += " and isnull(CT_STSFL,'') <>'X' and isnull(PO_STSFL,'') <> 'X' order by PO_PORNO,PO_INDNO,PO_MATCD";
		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				L_dblORDQT =0;
				L_dblUNTRT =0;
				L_dblPOAMT =0;
				intRECCNT++;
				intSRLNO ++;
				strPORNO = M_rstRSSET.getString("PO_PORNO");
				strARCNO = nvlSTRVL(M_rstRSSET.getString("PO_ORDRF"),"");
				strAMDNO = M_rstRSSET.getString("PO_AMDNO");
				L_strINDNO = M_rstRSSET.getString("PO_INDNO");
				L_strMATCD = M_rstRSSET.getString("PO_MATCD");
				strITMDS = M_rstRSSET.getString("PO_MATDS");
				L_strDPTCD = M_rstRSSET.getString("PO_DPTCD");
				strDSTCD = M_rstRSSET.getString("PO_DSTCD");
				strDELTP = M_rstRSSET.getString("PO_DELTP");
				strPMTRF = (M_rstRSSET.getString("PO_PMTRF").trim());
				strCURCD = M_rstRSSET.getString("PO_CURCD");
				dblEXGRT = M_rstRSSET.getDouble("PO_EXGRT");
				strTEMP = nvlSTRVL(M_rstRSSET.getString("PO_CRDDY"),"0");
				if(Integer.parseInt(strTEMP) >0)
				{
					strCRDDY = strTEMP +" DAYS"; 
				}
				else
					strCRDDY ="";
				if(!strPORNO.equals(strPRVPOR))
				{
					intSRLNO =1;
					flgITTAX = false;
					strDLTDS = cl_dat.getPRMCOD("CMT_CODDS","SYS","MRXXDTP",strDELTP);
					strDLTDS1 = cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXDST",strDSTCD);

					M_strSQLQRY =" select cmt_shrds from co_cdtrn where cmt_cgmtp ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp ='MM"+M_strSBSCD.substring(2,4) +"POR'and SUBSTRING(cmt_codcd,3,1)='"+strPORNO.substring(2,3)+"'";
					L_rstRSPOS = cl_dat.exeSQLQRY3(M_strSQLQRY);
					strPOSRL ="";
					if(L_rstRSPOS !=null)
						if(L_rstRSPOS.next())
						strPOSRL = nvlSTRVL(L_rstRSPOS.getString("cmt_shrds"),"");
					getPODEL(strPORNO);
					if(strPRVPOR.trim().length() >0)
					{
						prnFOOTR(strPRVPOR,strPRVAMD,P_strDOCTP);
						intRECCNT =1;
					}
					if(!strPRVPOR.equals(strPORNO))
						strPCURCD = strCURCD;
					strPRVPOR = strPORNO;
					strPRVAMD = strAMDNO;
				}
				strPREBY = nvlSTRVL(M_rstRSSET.getString("PO_PREBY"),"");
				strAUTBY = nvlSTRVL(M_rstRSSET.getString("PO_AUTBY"),"");
				strDPTCD = nvlSTRVL(M_rstRSSET.getString("PO_DPTCD"),"");
				strQTNNO = nvlSTRVL(M_rstRSSET.getString("PO_QTNNO"),"");
				L_datTEMP = M_rstRSSET.getDate("PO_PORDT");
				if(L_datTEMP !=null)
					strPORDT = M_fmtLCDAT.format(L_datTEMP);
				else
						strPORDT = "";
				L_datTEMP = M_rstRSSET.getDate("PO_AMDDT");
				if(L_datTEMP !=null)
					strAMDDT = M_fmtLCDAT.format(L_datTEMP);
				else
						strAMDDT = "";
				if(intRECCNT ==1)
				{
					strSHRDS = M_rstRSSET.getString("PO_SHRDS");
					strPORVL = M_rstRSSET.getString("PO_PORVL");
					L_datTEMP = M_rstRSSET.getDate("PO_EFFDT");
					if(L_datTEMP !=null)
						strEFFDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strEFFDT = "";
			
					L_datTEMP = M_rstRSSET.getDate("PO_CMPDT");
					if(L_datTEMP !=null)
						strCMPDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strCMPDT = "";
			
					L_datTEMP = M_rstRSSET.getDate("PO_QTNDT");
					if(L_datTEMP !=null)
						strQTNDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strQTNDT = "";
					strVENCD = nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),"");
					strCURCD = nvlSTRVL(M_rstRSSET.getString("PO_CURCD"),"");
					
					prnMNHDR(P_strDOCTP);
					prnPGHDR(strPORNO,P_strDOCTP); 		
					strPREBY = nvlSTRVL(M_rstRSSET.getString("PO_PREBY"),"");
					strAUTBY = nvlSTRVL(M_rstRSSET.getString("PO_AUTBY"),"");
					L_datTEMP = M_rstRSSET.getDate("PO_PREDT");
					if(L_datTEMP !=null)
						strPREDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strPREDT = "";
					L_datTEMP = M_rstRSSET.getDate("PO_AUTDT");
					if(L_datTEMP !=null)
						strAUTDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strAUTDT = "";
				}
				if(intLINNO > 63)
				{
					crtLINE(106);
					O_DOUT.writeBytes ("\n");
					intPAGNO += 1;
					O_DOUT.writeBytes("Contd..."+intPAGNO);
					O_DOUT.writeBytes ("\n");
					O_DOUT.writeBytes ("\n");					
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);
					if(M_rdbHTML.isSelected())
						O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					intLINNO = 0;
					prnPGHDR(strPORNO,P_strDOCTP);
					prnCOLHDR();
				}
				L_strLVLNO = M_rstRSSET.getString("CT_LVLRF");
				L_strDSCTP = M_rstRSSET.getString("CT_DSCTP");
				L_strPRTNO = nvlSTRVL(M_rstRSSET.getString("CT_PRTNO"),"");
				strMATCD = L_strMATCD;
				O_DOUT.writeBytes("\n");
				intLINNO++;
				if(intLINNO > 63)
				{
					crtLINE(106);
					O_DOUT.writeBytes ("\n");
					intPAGNO += 1;
					O_DOUT.writeBytes("Contd..."+intPAGNO);
					O_DOUT.writeBytes ("\n");
					O_DOUT.writeBytes ("\n");
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);
					if(M_rdbHTML.isSelected())
						O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					intLINNO = 0;
					prnPGHDR(strPORNO,P_strDOCTP); 
					prnCOLHDR();
				}
				strPRINT = "";
				strPRINT1 = "";
				strTEMP = String.valueOf(intSRLNO);
				if(strTEMP.length() ==1)
					strTEMP ="00"+strTEMP+"  ";
				else if(strTEMP.length() ==2)
					strTEMP ="0"+strTEMP+"  ";
				strPRINT += strTEMP;
			/*	if(!strAMDNO.equals("00"))
				{
					strPRINT += padSTRING('R',L_strINDNO,10);
					if(hstMATCD.contains(L_strMATCD))
					{
						strPRINT += padSTRING('R',L_strMATCD+"*",20);
					}
					else
					{
						strPRINT += padSTRING('R',L_strMATCD,20);
					}
				}
				else*/
				strPRINT += padSTRING('R',L_strINDNO,10);
				strPRINT += padSTRING('R',L_strMATCD,12);
				strPRINT += padSTRING('R',"("+L_strDPTCD+")",8);
				strPRINT += padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_UOMCD")," "),21);
				strPRINT += padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_TCFFL"),"-")+"/"+nvlSTRVL(M_rstRSSET.getString("PO_INSFL"),"-"),5);
				strPRINT1 = padSTRING('R'," ",46);	
				strPRINT1 +=padSTRING('L'," ",5);	
				L_dblORDQT = M_rstRSSET.getDouble("PO_PORQT")/M_rstRSSET.getDouble("PO_UCNVL");
				L_dblUNTRT =  M_rstRSSET.getDouble("PO_PORRT")/M_rstRSSET.getDouble("PO_PERRT");
				L_dblPOAMT = L_dblORDQT *L_dblUNTRT;
				strPRINT += padSTRING('L'," ",6);
				strPRINT += padSTRING('L',setNumberFormat(L_dblORDQT,3),13);
				strPRINT += padSTRING('L',setNumberFormat(L_dblUNTRT,2),13);
				strPRINT += padSTRING('L',setNumberFormat(L_dblPOAMT,2),13);
				strPRINT1 += padSTRING('L'," ",15);
				strPRINT += "\n";
				intLINNO++;
				if(intLINNO > 63)
				{
					crtLINE(106);
					O_DOUT.writeBytes ("\n");
					intPAGNO += 1;
					O_DOUT.writeBytes("Contd..."+intPAGNO);
					O_DOUT.writeBytes ("\n");
					O_DOUT.writeBytes ("\n");
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);
					if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					intLINNO = 0;
					prnPGHDR(strPORNO,P_strDOCTP); 
					prnCOLHDR();
				}
				/////// added
				if(vtrITTAX !=null)
					vtrITTAX.removeAllElements();
				if(nvlSTRVL(M_rstRSSET.getString("TX_PRDCD"),"").length() >0)
				{
					flgITTAX = true;
					vtrITTAX = getITTAX(M_rstRSSET);
				}
				else
					vtrITTAX = new Vector<String>();
				getITSPTX(strPORNO,L_strMATCD);
				vtrDLSCH = getDLSCH(L_strINDNO+L_strMATCD);
				
				if(L_strDSCTP.equals("S"))
				{
					if(L_strPMATCD.equals(""))
					{
						flgFIRST = true;
						// for First Record
						getMATDS(L_strMATCD,"MG",L_strLVLNO,P_strDOCTP);
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
									getMATDS(L_strMATCD,"HD",L_strLVLNO,P_strDOCTP);
								}
								else
								{
									//if description of code
									getMATDS(L_strMATCD,"CD",L_strLVLNO,P_strDOCTP);
								}
							}
							else
							{
								//if sub -sub group is different
								getMATDS(L_strMATCD,"SS",L_strLVLNO,P_strDOCTP);
							}
						}
						else
						{
							// If sub group is different
							getMATDS(L_strMATCD,"SG",L_strLVLNO,P_strDOCTP);
						}
					}
					else
					{
						// If main group is different
						getMATDS(L_strMATCD,"MG",L_strLVLNO,P_strDOCTP);
					}
				 }
				else if(L_strDSCTP.equals("D"))
				{
					// Descriptive Item codes , i.e 99
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
					if(vtrMATDS !=null)
						vtrMATDS.removeAllElements();
			/// changed on 26/08/05 to print purchase added desc afterwards, moved below
            ///		if(strITMDS.trim().length() >0)
			///			vtrMATDS.addElement(strITMDS);
					if(intLINNO > 63)
					{
						crtLINE(106);
						O_DOUT.writeBytes ("\n");
						intPAGNO += 1;
						O_DOUT.writeBytes("Contd..."+intPAGNO);
						O_DOUT.writeBytes ("\n");
						O_DOUT.writeBytes ("\n");
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(O_DOUT,M_strEJT);
						if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
						intLINNO = 0;
						prnPGHDR(strPORNO,P_strDOCTP); 
						prnCOLHDR();
					}
					if(L_rstRSSET !=null)
					while(L_rstRSSET.next())
					{
						//O_DOUT.writeBytes(padSTRING('L',"",10)); // added on 03/04
						vtrMATDS.addElement(L_rstRSSET.getString("CTT_MATDS"));
						//O_DOUT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
					/*	O_DOUT.writeBytes("\n");
						intLINNO++;
						if(intLINNO > 63)
						{
							crtLINE(106);
							O_DOUT.writeBytes ("\n");
							intPAGNO += 1;
							O_DOUT.writeBytes("Contd..."+intPAGNO);
							O_DOUT.writeBytes ("\n");
							O_DOUT.writeBytes ("\n");
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(O_DOUT,M_strEJT);
							if(M_rdbHTML.isSelected())
								O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
							intLINNO = 0;
							prnPGHDR(strPORNO,P_strDOCTP); 
							prnCOLHDR();
						}*/
			
					}
		// print the matds and tax from vtrMATDS and vtrITTAX;
		//////
        /// changed on 26/08/05 to print purchase added desc afterwards, moved from above
		            if(strITMDS.trim().length() >0)
						vtrMATDS.addElement(strITMDS);
					String L_strMAX ="";
					int L_intMAXSZ =0;
					if(vtrITTAX.size() >vtrMATDS.size())
					{
						L_strMAX = "vtrITTAX";
						if(vtrITTAX.size()<vtrDLSCH.size())
							L_strMAX = "vtrDLSCH";
							
					}
					else
					{
						L_strMAX = "vtrMATDS";
						if(vtrMATDS.size()<vtrDLSCH.size())
							L_strMAX = "vtrDLSCH";
					}
					if(L_strMAX.equals("vtrMATDS"))
					{
						L_intMAXSZ = vtrMATDS.size();
					}
					else if(L_strMAX.equals("vtrITTAX"))
					{
						L_intMAXSZ = vtrITTAX.size();
					}
					else if(L_strMAX.equals("vtrDLSCH"))
					{
						L_intMAXSZ = vtrDLSCH.size();
					}
						int L_intLPCNT =0; // loop count;
						L_intLPCNT = L_intMAXSZ;
						for(int i=0;i<L_intLPCNT;i++)
						{
							O_DOUT.writeBytes(padSTRING('L',"",10));
							if(vtrMATDS.size() > i)
								O_DOUT.writeBytes(padSTRING('R',vtrMATDS.elementAt(i).toString(),45));
							else
								O_DOUT.writeBytes(padSTRING('R',"",45));
							if(vtrITTAX.size() >i)
								O_DOUT.writeBytes(padSTRING('R',vtrITTAX.elementAt(i).toString(),40));
							else
								O_DOUT.writeBytes(padSTRING('R',"",40));
							if(vtrDLSCH.size() >i)
							{
								if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
									prnFMTCHR(O_DOUT,M_strCPI17);	
								O_DOUT.writeBytes(vtrDLSCH.elementAt(i).toString());
								if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								{
									prnFMTCHR(O_DOUT,M_strNOCPI17);	
									prnFMTCHR(O_DOUT,M_strCPI10);	
									prnFMTCHR(O_DOUT,M_strCPI12);	
								}
							}
							
							else
								O_DOUT.writeBytes(" ");
							O_DOUT.writeBytes("\n");
							intLINNO += 1;
							if(intLINNO > 63)
							{
								crtLINE(106);
								O_DOUT.writeBytes ("\n");
								intPAGNO += 1;
								O_DOUT.writeBytes("Contd..."+intPAGNO);
								O_DOUT.writeBytes ("\n");
								O_DOUT.writeBytes ("\n");
								if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
									prnFMTCHR(O_DOUT,M_strEJT);
								if(M_rdbHTML.isSelected())
									O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
								intLINNO = 0;
								prnPGHDR(strPORNO,P_strDOCTP); 
								prnCOLHDR();
							}
						}
	// Commented on 19/03/2005 whilw sorteing the Item tax printing problem for Descriptive item
	// Purchase added description added in Vector<String> itself.					
	/*		///////////////////////////
					/// added on 24/07/04 for printing the user added description in
					// case of descriptive items.
					O_DOUT.writeBytes(padSTRING('L',"",10));
					O_DOUT.writeBytes(strITMDS);
					O_DOUT.writeBytes("\n");
					intLINNO++;
					if(intLINNO > 63)
					{
						crtLINE(106);
						O_DOUT.writeBytes ("\n");
						intPAGNO += 1;
						O_DOUT.writeBytes("Contd..."+intPAGNO);
						O_DOUT.writeBytes ("\n");
						O_DOUT.writeBytes ("\n");
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(O_DOUT,M_strEJT);
						if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
						intLINNO = 0;
						prnPGHDR(strPORNO,P_strDOCTP); 
						prnCOLHDR();
					}
					/// end printing purchase's added description*/
				}
				L_strPMATCD = L_strMATCD;
				L_strPRVLVL = L_strLVLNO;
				intLINNO++;
				if(intLINNO > 63)
				{
					crtLINE(106);
					O_DOUT.writeBytes ("\n");
					intPAGNO += 1;
					O_DOUT.writeBytes("Contd..."+intPAGNO);
					O_DOUT.writeBytes ("\n");
					O_DOUT.writeBytes ("\n");
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);
					if(M_rdbHTML.isSelected())
						O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					intLINNO = 0;
					prnPGHDR(strPORNO,P_strDOCTP); 
					prnCOLHDR();
				}
			}
			if(intRECCNT > 0)
				prnFOOTR(strPORNO,strAMDNO,P_strDOCTP);
			O_DOUT.close();
			O_FOUT.close();
			if(M_rdbHTML.isSelected())
			{
			    O_DOUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			setMSG("",'N');
		}catch(Exception L_EX){
			setMSG(L_EX,"getALLREC");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
private void getMATDS(String P_strMATCD,String P_strDSLVL,String P_strLVLNO,String P_strDOCTP)
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
		String L_strMATDS ="";
		if(vtrMATDS !=null)
			vtrMATDS.removeAllElements();
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
						intLINNO += 1;
					}
					L_strMATDS = L_rstRSSET.getString("CTT_MATDS");
					vtrMATDS.addElement(L_strMATDS);
			
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
									O_DOUT.writeBytes("          ----------*\n");
									intLINNO++;
									if(intLINNO > 63)
									{
											crtLINE(106);
											O_DOUT.writeBytes ("\n");
											intPAGNO += 1;
											O_DOUT.writeBytes("Contd..."+intPAGNO);
											O_DOUT.writeBytes ("\n");
											O_DOUT.writeBytes ("\n");
											if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
												prnFMTCHR(O_DOUT,M_strEJT);
											if(M_rdbHTML.isSelected())
												O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
											intLINNO = 0;
											prnPGHDR(strPORNO,P_strDOCTP); 
											prnCOLHDR();
									}
									pflag = false;
									
								}
							}
						}
					O_DOUT.writeBytes(padSTRING('L',"",10)); // added on 03/04
					O_DOUT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
					O_DOUT.writeBytes("\n");
					intLINNO++;
					if(intLINNO > 63)
					{
						crtLINE(106);
						O_DOUT.writeBytes ("\n");
						intPAGNO += 1;
						O_DOUT.writeBytes("Contd..."+intPAGNO);
						O_DOUT.writeBytes ("\n");
						O_DOUT.writeBytes ("\n");
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(O_DOUT,M_strEJT);
						if(M_rdbHTML.isSelected())
							O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
						intLINNO = 0;
						prnPGHDR(strPORNO,P_strDOCTP); 
						prnCOLHDR();
					}
				}
		}
		if(strITMDS.trim().length() >0)
			vtrMATDS.addElement(strITMDS);
		// print the matds and tax from vtrMATDS and vtrITTAX;
		//////
		String L_strMAX ="";
		int L_intMAXSZ =0;
		if(vtrITTAX.size() >vtrMATDS.size())
		{
			L_strMAX = "vtrITTAX";
			if(vtrITTAX.size()<vtrDLSCH.size())
				L_strMAX = "vtrDLSCH";
				
		}
		else
		{
			L_strMAX = "vtrMATDS";
			if(vtrMATDS.size()<vtrDLSCH.size())
				L_strMAX = "vtrDLSCH";
		}
		if(L_strMAX.equals("vtrMATDS"))
		{
			L_intMAXSZ = vtrMATDS.size();
		}
		else if(L_strMAX.equals("vtrITTAX"))
		{
			L_intMAXSZ = vtrITTAX.size();
		}
		else if(L_strMAX.equals("vtrDLSCH"))
		{
			L_intMAXSZ = vtrDLSCH.size();
		}
			int L_intLPCNT =0; // loop count;
			L_intLPCNT = L_intMAXSZ;
			for(int i=0;i<L_intLPCNT;i++)
			{
				O_DOUT.writeBytes(padSTRING('L',"",10));
				if(vtrMATDS.size() > i)
					O_DOUT.writeBytes(padSTRING('R',vtrMATDS.elementAt(i).toString(),45));
				else
					O_DOUT.writeBytes(padSTRING('R',"",45));
				if(vtrITTAX.size() >i)
					O_DOUT.writeBytes(padSTRING('R',vtrITTAX.elementAt(i).toString(),40));
				else
					O_DOUT.writeBytes(padSTRING('R',"",40));
				if(vtrDLSCH.size() >i)
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strCPI17);	
					O_DOUT.writeBytes(vtrDLSCH.elementAt(i).toString());
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					{
						prnFMTCHR(O_DOUT,M_strNOCPI17);	
						prnFMTCHR(O_DOUT,M_strCPI10);	
						prnFMTCHR(O_DOUT,M_strCPI12);	
					}
				}
				
				else
					O_DOUT.writeBytes(" ");
				O_DOUT.writeBytes("\n");
				intLINNO += 1;
				if(intLINNO > 63)
				{
					crtLINE(106);
					O_DOUT.writeBytes ("\n");
					intPAGNO += 1;
					O_DOUT.writeBytes("Contd..."+intPAGNO);
					O_DOUT.writeBytes ("\n");
					O_DOUT.writeBytes ("\n");
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(O_DOUT,M_strEJT);
					if(M_rdbHTML.isSelected())
						O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
					intLINNO = 0;
					prnPGHDR(strPORNO,P_strDOCTP); 
					prnCOLHDR();
				}
			}
	//	}
			
		if(L_strPRTNO != "")
		{	
			O_DOUT.writeBytes("          Part No. : "+L_strPRTNO);
			O_DOUT.writeBytes("\n");
			intLINNO++;
		}	
			
		O_DOUT.writeBytes("          ----------\n");
		intLINNO++;
		if(intLINNO > 63)
		{
				crtLINE(106);
				O_DOUT.writeBytes ("\n");
				intPAGNO += 1;
				O_DOUT.writeBytes("Contd..."+intPAGNO);
				O_DOUT.writeBytes ("\n");
				O_DOUT.writeBytes ("\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(O_DOUT,M_strEJT);
				if(M_rdbHTML.isSelected())
					O_DOUT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(strPORNO,P_strDOCTP); 
				prnCOLHDR();
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getMATDS");
	}
}
/////////
public String getCURWRD(String LP_CURVL,String LP_DENOM){
      	//String to be printed
		String L_PRNSTR = "";
		try{
			//Remainder of divison
			int L_REMVL = 0;
			//Quotient of divison
			int L_QUOVL = 0;
			//Getting rupees
			String L_RPSVL = LP_CURVL.substring(0,LP_CURVL.indexOf(".")).trim();
			//Converting rupees in string to integer
			int L_RUPVL = Integer.parseInt(L_RPSVL);
			//Getting paise
			String L_PAIVL = LP_CURVL.substring(LP_CURVL.lastIndexOf(".")+1,LP_CURVL.length()).trim();
			//Taking length of rupees value
			int L_ABSVL = L_RPSVL.length();
           if(L_ABSVL > 0){
				//Ones
				if(L_ABSVL == 1){
                                        L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Tens
				else if(L_ABSVL == 2){
                                        L_PRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Hundreds
				else if(L_ABSVL == 3){
					L_QUOVL = L_RUPVL/100;
					L_REMVL = L_RUPVL%100;
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                                        L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
				//Thousands
				else if(L_ABSVL == 4 || L_ABSVL == 5){
					L_QUOVL = L_RUPVL/1000;
					L_REMVL = L_RUPVL%1000;
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
                 	L_QUOVL = L_REMVL/100;
					L_REMVL = L_REMVL%100;
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                    L_PRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
                               
				}
				//Lacs
				else if(L_ABSVL == 6 || L_ABSVL == 7){
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
				}
				//Crores
				else if(L_ABSVL == 8 || L_ABSVL == 9){
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
			if(L_PAIVL.length() > 0 && !L_PAIVL.equals("00")){
				if(L_PAIVL.substring(0,1).equals("0"))
					L_PAIVL =L_PAIVL.substring(1);
                                L_PRNSTR += chkZERO(hst1to100.get(L_PAIVL).toString(),"and "+LP_DENOM,'F');
			}
			L_PRNSTR += "Only";
		}catch(Exception L_EX){
                        setMSG(L_EX,"getCURWRD");
		}
		return L_PRNSTR;
	}
	
        private String chkZERO(String LP_STRVL,String LP_RPSVL,char LP_PLCSTR){
		String L_RTNSTR = "";
		try{
			if(String.valueOf(LP_STRVL).equals(null) || LP_STRVL.equalsIgnoreCase("zero")){
				return "";
			}else{
				if(LP_PLCSTR == 'F'){
					L_RTNSTR = LP_RPSVL + " " +  LP_STRVL + " ";
				}
				else if(LP_PLCSTR == 'B'){
					L_RTNSTR = LP_STRVL + " " + LP_RPSVL + " ";
				}
			}
		}catch(Exception L_EX){
          	setMSG(L_EX,"chkZERO");
		}
		return L_RTNSTR;
	}
		public void crtHST()
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
		hst1to100.put("49","Fourty Nine");hst1to100.put("50","Fifty");hst1to100.put("51","Fifty One");hst1to100.put("52","Fifty Two");
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
private void crtHSTXCD()
{
	try
	{
		String L_strHSVAL ="",
		M_strSQLQRY = " SELECT * FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXTAX' order by CMT_CCSVL,CMT_NMP01";
		L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(L_rstRSSET !=null)
				
		while(L_rstRSSET.next())
		{
			L_strHSVAL = nvlSTRVL(L_rstRSSET.getString("CMT_NMP01"),"") ;// Multiplier
			L_strHSVAL += "|"+nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"") ;// Multiplier
		    // ADDED
		    L_strHSVAL += "|"+nvlSTRVL(L_rstRSSET.getString("CMT_MODLS")," "+"|") ;// Multiplier
		//	if(nvlSTRVL(L_rstRSSET.getString("CMT_CHP01"),"").equals("01"))
		//	{
				hstTAXCD.put(L_rstRSSET.getString("CMT_CODCD"),L_strHSVAL);
				vtrTAXCD.addElement(L_rstRSSET.getString("CMT_CODCD"));
				vtrTAXTP.addElement(L_rstRSSET.getString("CMT_CHP01"));
		//	}
		//	else 
		//		hstTAXSP.put(L_rstRSSET.getString("CMT_CODCD"),L_strHSVAL);
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"crtHSTCD");
	}

}
	public String calPOVAL(String P_strPORNO,String P_strAMDNO,char P_strCALFR)
	{
		double L_dblITVAL=0.00;
		double L_dblTOTAL=0.00;
		double L_dblFRACT=0.00;
		double dblPORVL=0.00,dblEXVAL =0.0,dblVATVL =0.0;
		double L_dblINDTX=0.00;
		boolean flgOTTAX = false;
		boolean L_flgEXCFL = false;
		String L_strTXOCD ="";
		String L_strTXOCD1 ="";

		try
        {
			dblEXVAL = 0.0;dblVATVL = 0.0;
		    L_dblINDTX =0.0;
			ResultSet L_rstRSSET;
			ResultSet L_rstTXSPC;
			
			Hashtable<String,String> hstITTAX = new Hashtable<String,String>();
			Hashtable<String,String> hstCOTAX = new Hashtable<String,String>();
			Hashtable<String,String> hstITCOD = new Hashtable<String,String>();
			Hashtable<String,Double> hstTXVAL = new Hashtable<String,Double>();
			String L_strTEMP ="";
			String L_strMULT ="";
			String L_strINDNO,L_strPORNO="",L_strMATCD,L_strSQLQRY ="";
			M_strSQLQRY = " SELECT * FROM CO_TXDOC WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSCD ='"+M_strSBSCD +"'" +" AND TX_SBSTP = '"+M_strSBSCD.substring(2,4) +"' AND TX_DOCNO ='"+P_strPORNO +"' AND isnull(TX_STSFL,'') <>'X' order by TX_PRDCD";
			System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			String L_strTAXVL="0",L_strITMCD="",L_strTAXCD,L_strCLPRF,L_strTAXFL="";
			int L_intTXCNT=0;
        	boolean L_flgSTXFL = false;
			 if(L_rstRSSET !=null)
			while(L_rstRSSET.next())
			{
				L_strPORNO = L_rstRSSET.getString("TX_DOCNO");
				L_strITMCD = L_rstRSSET.getString("TX_PRDCD");
				for(int i=0;i<vtrTAXCD.size();i++)
				{
					// IF TAX TYPE IS SPECIFIC, ThEN CHECK IN TAXSPC FOR AVAILABLITY
					// IF THIS CODE IS PRESENT THEN TAKE THE VALUES.
					L_strTAXCD = vtrTAXCD.elementAt(i).toString();
					L_strCLPRF = "TX_"+L_strTAXCD;  // column prefix
					L_flgSTXFL = false;
					L_strTAXFL ="";
					if(vtrTAXTP.elementAt(i).equals("02"))
					{
						L_strSQLQRY ="Select * from CO_TXSPC WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' and TXT_DOCTP ='POR' AND TXT_SBSCD = '"+M_strSBSCD +"'" +" AND TXT_SBSTP = '"+M_strSBSCD.substring(2,4) +"'";
						L_strSQLQRY += "AND TXT_DOCNO ='"+P_strPORNO +"' AND TXT_CODCD = '"+L_strTAXCD +"' and isnull(txt_stsfl,'') <>'X'";
						System.out.println(L_strSQLQRY);
						L_rstTXSPC	= cl_dat.exeSQLQRY2(L_strSQLQRY);
						if(L_rstTXSPC!=null)
						{
							if(L_rstTXSPC.next())
							{
								L_flgSTXFL = true;
								L_strTAXFL = L_rstTXSPC.getString("TXT_CODFL");
								L_strTAXVL = nvlSTRVL(L_rstTXSPC.getString("TXT_CODVL"),"");
								L_strITMCD = nvlSTRVL(L_rstTXSPC.getString("TXT_PRDCD"),"");
							}
							L_rstTXSPC.close();
						}
					}
					if(vtrTAXTP.elementAt(i).equals("01"))
						L_strTAXFL =nvlSTRVL(L_rstRSSET.getString(L_strCLPRF+"FL"),"");
					if(L_strTAXFL.length() >0)
					{
					   // if(!L_flgSTXFL) // Not a Specific tax.It is normal tax 
						if(vtrTAXTP.elementAt(i).equals("01"))
							L_strTAXVL =nvlSTRVL(L_rstRSSET.getString(L_strCLPRF+"VL"),"0");
					  
					   if(L_strTAXCD.equals("EXC"))
					   {
                        	 L_flgEXCFL = true;
							 //dblEXCVL =Double.parseDouble(L_strTAXVL);
					   }
                     	if(Double.parseDouble(L_strTAXVL) >0)
						{
							if(L_strTAXFL.equals("P"))
								L_strTAXVL = String.valueOf(Double.parseDouble(L_strTAXVL)/100);
							if(!L_strITMCD.equals("XXXXXXXXXX")) // Item Level Tax
							{
								if(hstITCOD.get(L_strITMCD)== null)
								{
									hstITCOD.put(L_strITMCD,"");
								}
								if(L_strTAXFL.equals("P"))
									hstITTAX.put(L_strITMCD+L_strTAXCD,"P"+L_strTAXVL);
								else	
									hstITTAX.put(L_strITMCD+L_strTAXCD,"A"+L_strTAXVL); 
							}
							else
							{
								if(L_strTAXFL.equals("P"))
									hstCOTAX.put(L_strTAXCD,"P"+L_strTAXVL); 
								else
									hstCOTAX.put(L_strTAXCD,"A"+L_strTAXVL); 
							}
						}
					}
				}
				L_intTXCNT++;
			}
			// IF THERE IS NO RECORD IN CO_TXDOC , PROBLEM MAY BE IN CALCULATION OF SPECIFIC TAX,
			// SEPARATE LOGIC TO BE INCLUDED.
			if(L_intTXCNT ==0)
			{
				for(int i=0;i<vtrTAXCD.size();i++)
				{
					// IF tAX TYPE IS SPECIFIC, ThEN CHECK IN TAXSPC FOR AVAILABLITY
					L_strTAXCD = vtrTAXCD.elementAt(i).toString();
					L_strCLPRF = "TX_"+L_strTAXCD;  // column prefix
					L_flgSTXFL = false;
					///////added 
                    L_strTAXFL ="";
                     if(vtrTAXTP.elementAt(i).equals("02")) // specific tax type
					{
						L_strSQLQRY ="Select * from CO_TXSPC WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' and TXT_DOCTP ='POR' AND TXT_SBSCD = '"+M_strSBSCD +"'" +" AND TXT_SBSTP = '"+M_strSBSCD.substring(2,4) +"'";
						L_strSQLQRY += "AND TXT_DOCNO ='"+P_strPORNO +"' AND TXT_CODCD = '"+L_strTAXCD +"'";
						System.out.println(L_strSQLQRY);
						L_rstTXSPC	= cl_dat.exeSQLQRY2(L_strSQLQRY);
						if(L_rstTXSPC!=null)
						{
							if(L_rstTXSPC.next())
							{
								L_flgSTXFL = true;
								L_strPORNO = L_rstTXSPC.getString("TXT_DOCNO");
								L_strTAXFL = L_rstTXSPC.getString("TXT_CODFL");
								L_strTAXVL = nvlSTRVL(L_rstTXSPC.getString("TXT_CODVL"),"");
								L_strITMCD = nvlSTRVL(L_rstTXSPC.getString("TXT_PRDCD"),"");
							}
							L_rstTXSPC.close();
						}
					}
					if(L_strTAXFL.length() >0)
					{
        			   if(L_strTAXCD.equals("EXC"))
					   {
                        	 L_flgEXCFL = true;
							 //dblEXCVL =Double.parseDouble(L_strTAXVL);
					   }
                      if(Double.parseDouble(L_strTAXVL) >0)
						{
							if(L_strTAXFL.equals("P"))
								L_strTAXVL = String.valueOf(Double.parseDouble(L_strTAXVL)/100);
							if(!L_strITMCD.equals("XXXXXXXXXX")) // Item Level Tax
							{
								if(hstITCOD.get(L_strITMCD)== null)
								{
									hstITCOD.put(L_strITMCD,"");
								}
								if(L_strTAXFL.equals("P"))
									hstITTAX.put(L_strITMCD+L_strTAXCD,"P"+L_strTAXVL);
								else	
									hstITTAX.put(L_strITMCD+L_strTAXCD,"A"+L_strTAXVL); 
							}
							else
							{
								if(L_strTAXFL.equals("P"))
									hstCOTAX.put(L_strTAXCD,"P"+L_strTAXVL); 
								else
									hstCOTAX.put(L_strTAXCD,"A"+L_strTAXVL); 
							}
						}
					}
				}
			}
			M_strSQLQRY = " SELECT PO_STRTP,PO_PORNO,PO_INDNO,PO_MATCD,PO_PORQT,((isnull(PO_PORQT,0)/PO_UCNVL)*isnull(PO_PORRT,0))/PO_PERRT L_ITVAL FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STSFL <>'X' AND PO_MMSBS ='"+M_strSBSCD +"'"+ " AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"'"+" AND PO_PORNO ='"+P_strPORNO +"' ORDER BY PO_INDNO,PO_MATCD";
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
																		   ResultSet.CONCUR_UPDATABLE);
			System.out.println(M_strSQLQRY);															   
			L_rstRSSET = L_stat.executeQuery(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{	
				dblPORVL =0;
				while(L_rstRSSET.next())
				{
					L_dblITVAL = L_rstRSSET.getDouble("L_ITVAL");
					L_dblTOTAL +=L_dblITVAL;
				}
				L_rstRSSET.beforeFirst();
				while(L_rstRSSET.next())
				{
					L_strMATCD = L_rstRSSET.getString("PO_MATCD");
					L_strINDNO = L_rstRSSET.getString("PO_INDNO");
					L_dblITVAL = L_rstRSSET.getDouble("L_ITVAL");
					L_dblFRACT = L_dblITVAL /L_dblTOTAL;
			        for(int i=0;i<vtrTAXCD.size();i++)
					{
						L_strTXOCD ="";
						L_strTXOCD1 ="";
						L_strTAXCD = vtrTAXCD.elementAt(i).toString();
						// Item wise tax
						if(hstITTAX.get(L_strMATCD+L_strTAXCD)==null)
						{
							// Item wise tax is not present
						}
						else
						//if(hstITTAX.contains(L_strMATCD+L_strTAXCD))
						{
							// Item wise tax is present
							//System.out.println("Itemwise Tax");
							L_strTEMP = hstITTAX.get(L_strMATCD+L_strTAXCD).toString();
							L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
							L_strMULT = L_STRTKN.nextToken();
							///
							    L_STRTKN.nextToken();
							    ///////////L_strTXOCD = L_STRTKN.nextToken(); 
						////////////////////////////////////////////
							L_strTXOCD1 = L_STRTKN.nextToken(); 
							if(L_strTXOCD1.trim().length() == 0)
								L_strTXOCD ="";
							else
							{
								L_STRTKN=new StringTokenizer(L_strTXOCD1,"_");
								while(L_STRTKN.hasMoreTokens())
								{
									L_strTXOCD = L_STRTKN.nextToken(); 
									//System.out.println("while L_strTXOCD "+L_strTXOCD);
									if(hstTXVAL.containsKey(L_strTXOCD))
									{
										//System.out.println("contains "+L_strTAXCD);
										break;
									}
								}
							}
							///
							if(L_strTEMP.substring(0,1).equals("P"))
							{
							    /*
								if(L_strTAXCD.equals("EXC"))
									dblEXVAL = L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);
								L_dblITVAL += L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
								*/
								//changed on 13/10/2005
								//L_dblINDTX = L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);
								L_dblINDTX = L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)));
								hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(L_dblINDTX));
								
								if(L_strTAXCD.equals("EXC"))
									dblEXVAL = L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);
                                else if(L_strTAXCD.equals("VAT"))
									dblVATVL = L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);
							//	if(L_strTAXCD.equals("EDC"))
							    if(L_strTXOCD.trim().length() >0)
									L_dblITVAL += Double.parseDouble(hstTXVAL.get(L_strMATCD+L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
								else
								    L_dblITVAL += L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
							}
							else
							{	
							    //L_dblINDTX =(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor	
							    L_dblINDTX =(Double.parseDouble(L_strTEMP.substring(1)));
							    hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(L_dblINDTX));							
								if(L_strTAXCD.equals("EXC"))
									dblEXVAL = (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor
                                else if(L_strTAXCD.equals("VAT"))
									dblVATVL = (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor
							    L_dblITVAL += (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
							}
						}
						// Common Tax
						if(hstCOTAX.get(L_strTAXCD)== null)
						{
							// // common tax is not present
						}
						else
						{
				           		L_strTEMP = hstCOTAX.get(L_strTAXCD).toString();
							L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
							L_strMULT = L_STRTKN.nextToken();
  						      L_STRTKN.nextToken();
						////////////////////////////////////////					
							// Added on 17/08/07 for including EDC and EHC on Excise and CVD both( at all places)
							///////L_strTXOCD = L_STRTKN.nextToken(); 
							L_strTXOCD1 = L_STRTKN.nextToken(); 
							//System.out.println("L_strTXOCD1 "+L_strTXOCD1);
							if(L_strTXOCD1.trim().length() == 0)
								L_strTXOCD ="";
							
							else
							{
								L_STRTKN=new StringTokenizer(L_strTXOCD1,"_");
								while(L_STRTKN.hasMoreTokens())
								{
									L_strTXOCD = L_STRTKN.nextToken(); 
									//System.out.println("while L_strTXOCD "+L_strTXOCD);
									if(hstTXVAL.containsKey(L_strTXOCD))
									{
										//System.out.println("contains "+L_strTAXCD);
										break;
									}
								}
							}
							////////////////////////////////////////					

		    				if(L_strTEMP.substring(0,1).equals("P"))
							{
						      //  L_dblINDTX = L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add	
						       L_dblINDTX = L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1));//* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add	
							  //  System.out.println("L_dblINDTX "+L_strTAXCD +": " +L_dblINDTX);
							    hstTXVAL.put(L_strTAXCD,new Double(L_dblINDTX));
								if(L_strTAXCD.equals("EXC"))
									dblEXVAL = (L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
                                else if(L_strTAXCD.equals("VAT"))
									dblVATVL = (L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
						    	 if(L_strTXOCD.trim().length() >0)
							    {
							    	//System.out.println(L_strTAXCD +" "+L_strTXOCD+" 1"+Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString()));
							    	L_dblITVAL += Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
							    }
							     else
								    L_dblITVAL = L_dblITVAL+(L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
		                            //System.out.println("L_dblITVAL after "+L_strTAXCD+" "+L_dblITVAL);						    
							}
							else 
							{
							   // L_dblINDTX = L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
							    L_dblINDTX = L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1)));
							    hstTXVAL.put(L_strTAXCD,new Double(L_dblINDTX));
								if(L_strTAXCD.equals("EXC"))
									dblEXVAL = L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
                                else if(L_strTAXCD.equals("VAT"))
									dblVATVL = L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
								L_dblITVAL =L_dblITVAL +L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
							}
						}
					}
               		// Update the Item Value to ITVAL in POMST.
					if(strPCURCD.trim().length() ==0)
						strPCURCD = strCURCD;
					if(P_strCALFR =='A')
                    {
                        if(!strPCURCD.equals("01"))
    					{
    						M_strSQLQRY  ="Update MM_POMST SET PO_TRNFL ='0',PO_ITVAL ="+L_dblITVAL*dblEXGRT+
    									  ",PO_EXVAL ="+dblEXVAL*dblEXGRT +",PO_VATVL ="+dblVATVL*dblEXGRT +
    									  " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"'";
    					}
    					else
    					{
    						M_strSQLQRY  ="Update MM_POMST SET PO_TRNFL ='0',PO_ITVAL ="+L_dblITVAL+
    									 ",PO_EXVAL ="+dblEXVAL+",PO_VATVL ="+dblVATVL +
    									 " WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"'";
    					}
    					M_strSQLQRY += " AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND PO_INDNO ='"+L_strINDNO+
    								   "' AND PO_MATCD ='"+L_strMATCD+"'"+
    								   " AND PO_PORNO ='"+P_strPORNO+"'";
    				  	cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
                    }				
					dblPORVL +=L_dblITVAL;
			//	
			}
		}
				if(P_strCALFR =='A')
                {
                    if(!strPCURCD.equals("01"))
					    M_strSQLQRY  ="Update MM_POMST SET PO_PORVL ="+dblPORVL*dblEXGRT+" WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"'";
    				else 
    					M_strSQLQRY  ="Update MM_POMST SET PO_PORVL ="+dblPORVL+" WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"'";
    				M_strSQLQRY += " AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+
    								   "' AND PO_PORNO ='"+P_strPORNO+"'";
    				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    				cl_dat.exeDBCMT("test");
                    if(P_strAMDNO.equals("00"))
    				{
    				    //** Added on 18/08/2005 to save per unit value in stock master instead of rate */
    				    M_strSQLQRY = "UPDATE MM_STMST SET ST_PPOVL =(SELECT AVG(isnull(PO_ITVAL,0)/PO_PORQT) FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = ST_STRTP AND PO_MATCD = ST_MATCD "
    				              +" AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND PO_PORNO ='"+P_strPORNO+"' AND isnull(PO_PORQT,0) >0 AND isnull(PO_STSFL,'') ='A')"
    				              +" WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND ST_MATCD IN(SELECT B.PO_MATCD FROM MM_POMST B WHERE B.PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND B.PO_PORNO ='"+P_strPORNO+"' AND isnull(B.PO_PORQT,0) >0 AND isnull(B.PO_STSFL,'') ='A')";
    				   cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    				   M_strSQLQRY = "UPDATE CO_CTMST SET CT_PPORT =(SELECT AVG(isnull(PO_ITVAL,0)/PO_PORQT) FROM MM_POMST WHERE PO_MATCD = CT_MATCD "
    				              +" AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND PO_PORNO ='"+P_strPORNO+"' AND isnull(PO_PORQT,0) >0 AND isnull(PO_STSFL,'') ='A') "
    				              +" WHERE CT_MATCD IN(SELECT B.PO_MATCD FROM MM_POMST B WHERE B.PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND B.PO_PORNO ='"+P_strPORNO+"' AND isnull(B.PO_PORQT,0) >0 AND isnull(PO_STSFL,'') ='A')";          
    				   cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    				   cl_dat.exeDBCMT("exeSAVE");
    				   //** end Added on 18/08/2005 to save per unit value in stock master instead of rate */
    				}     
    				else
    				{
    				    	  //** Added on 18/08/2005 to save per unit value in stock master instead of rate */
    				   
    				  M_strSQLQRY = "UPDATE MM_STMST SET ST_PPOVL =(SELECT AVG(isnull(PO_ITVAL,0)/PO_PORQT) FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = ST_STRTP AND PO_MATCD = ST_MATCD "
    				              +" AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND PO_PORNO ='"+P_strPORNO+"' AND isnull(PO_PORQT,0) >0 AND isnull(PO_STSFL,'') ='A')"
    				              +" WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND ST_PPONO ='"+P_strPORNO +"' AND ST_MATCD IN(SELECT B.PO_MATCD FROM MM_POMST B WHERE B.PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND B.PO_PORNO ='"+P_strPORNO+"' AND isnull(B.PO_PORQT,0) >0 AND isnull(B.PO_STSFL,'') ='A')";
    				   cl_dat.exeSQLUPD(M_strSQLQRY,"");
    				   M_strSQLQRY = "UPDATE CO_CTMST SET CT_PPORT =(SELECT AVG(isnull(PO_ITVAL,0)/PO_PORQT) FROM MM_POMST WHERE PO_MATCD = CT_MATCD "
    				              +" AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_PORNO ='"+P_strPORNO+"' AND isnull(PO_PORQT,0) >0 AND isnull(PO_STSFL,'') ='A') "
    				              +" WHERE CT_PPONO ='"+P_strPORNO +"' AND CT_MATCD IN(SELECT B.PO_MATCD FROM MM_POMST B WHERE B.PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.PO_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND B.PO_PORNO ='"+P_strPORNO+"' AND isnull(B.PO_PORQT,0) >0 AND isnull(PO_STSFL,'') ='A')";          
    				   cl_dat.exeSQLUPD(M_strSQLQRY,"");
    				   cl_dat.exeDBCMT("exeSAVE");
    				//** end Added on 18/08/2005 to save per unit value in stock master instead of rate */
    				}
                }
    	}
		catch(Exception L_E)
		{
			setMSG(L_E,"calPOVAL");
		}
		return setNumberFormat(dblPORVL,2);

	}
private void getPODEL(String P_strPORNO)
{
	try
	{
	vtrPODEL = new Vector<String>();
	ResultSet L_rstRSSET;
	String L_strTEMP;
	String L_strLINE;
	java.util.Date L_datTEMP;
	M_strSQLQRY = " SELECT * from MM_PODEL where ";//PO_MMSBS ='"+M_strSBSCD+"'";
	M_strSQLQRY +=" POD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND POd_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
	M_strSQLQRY +=" AND POd_PORNO = '"+P_strPORNO +"' AND isnull(POD_STSFL,'') <>'X' order by POD_INDNO,POD_MATCD";
	L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
	if(L_rstRSSET !=null)
	{
		while(L_rstRSSET.next())
		{
			L_strLINE ="";
			L_strTEMP = nvlSTRVL(L_rstRSSET.getString("POD_INDNO"),"");
			L_strLINE = L_strTEMP;
			L_strTEMP = nvlSTRVL(L_rstRSSET.getString("POD_MATCD"),"");
			L_strLINE += L_strTEMP;
			L_datTEMP = L_rstRSSET.getDate("POD_EDLDT");
			if(L_datTEMP !=null)
			{
				L_strTEMP = M_fmtLCDAT.format(L_datTEMP);
			}
			else L_strTEMP ="          ";
			L_strLINE += L_strTEMP +" ";
			L_strTEMP = nvlSTRVL(L_rstRSSET.getString("POD_EDLQT"),"");
			L_strLINE += L_strTEMP;
			vtrPODEL.addElement(L_strLINE);
		}
	}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getPODEL");
	}
	
}
private Vector<String> getDLSCH(String P_strMATCD)// indent no + matcd
{
	
	Vector<String> L_vtrDLSCH = new Vector<String>();
	for(int i=0;i<vtrPODEL.size();i++)
	{
		if(vtrPODEL.elementAt(i).toString().substring(0,18).equals(P_strMATCD))
		{
			L_vtrDLSCH.addElement(vtrPODEL.elementAt(i).toString().substring(18));
		}
	}
	if(L_vtrDLSCH.size() <= 1)
		L_vtrDLSCH.removeAllElements();
	return L_vtrDLSCH;
}
 private Vector<String> getITTAX(ResultSet P_rstRSSET)
 {
	java.util.Vector<String> L_vtrITTAX = new java.util.Vector<String>();
	String L_strTAXCD,L_strCLPRF,L_strTAXFL,L_strTAXVL,L_strPRNLN="",L_strTEMP ="",L_strTXSTR="",L_strTAXDS; 
	try
	{
		for(int i=0;i<vtrTAXCD.size();i++)
		{
			// Added on 04/12/2004 when Vector<String> TAXCD included elements of 02 also.
			if(vtrTAXTP.elementAt(i).toString().equals("02"))
				continue;
			L_strTAXCD = vtrTAXCD.elementAt(i).toString();
			L_strCLPRF =  "TX_"+L_strTAXCD;  // column prefix
			L_strTAXFL =nvlSTRVL(P_rstRSSET.getString(L_strCLPRF+"FL"),"");
			L_strTAXDS ="";
			if(L_strTAXFL.length() >0)
			{
				L_strTAXVL =nvlSTRVL(P_rstRSSET.getString(L_strCLPRF+"VL"),"");
				L_strTEMP = hstTAXCD.get(L_strTAXCD).toString();
				int j=0;
				for(j=0;j<L_strTEMP.length();j++)
				{
					if(L_strTEMP.charAt(j)== '|')
						break;
				}
				L_strTAXDS = L_strTEMP.substring(j+1);
				//// ?
				if((L_strTAXCD.equals("STX")) ||(L_strTAXCD.equals("OTH")))
				{
					L_strTAXDS = nvlSTRVL(P_rstRSSET.getString("TX_"+L_strTAXCD+"DS"),"");
				}
				//L_strTAXFL =nvlSTRVL(P_rstRSSET.getString(L_strCLPRF+"FL"),"");
				if(Double.parseDouble(L_strTAXVL) == 0)
				{
					L_strTXSTR =L_strTAXDS;
					if((L_strTAXCD.trim().equals("STX")) ||(L_strTAXCD.trim().equals("OTH")))
					{
						L_strTXSTR +="";
					}
					else
					{
						if(L_strTAXFL.equals("I"))
						    L_strTXSTR +=" : INCLUDED";
						else if(L_strTAXFL.equals("X")) 
						    L_strTXSTR +=" : AT ACTUALS";
					}
				}
				else
				{
							
					if(L_strTAXFL.equals("P"))
					{
						L_strTXSTR =L_strTAXDS +" : "+setNumberFormat(Double.parseDouble(L_strTAXVL),3)+" % ";
					}
					else
						L_strTXSTR =L_strTAXDS +" : "+setNumberFormat(Double.parseDouble(L_strTAXVL),3);
				}			
				L_vtrITTAX.addElement(L_strTXSTR);
			}
		
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getITTAX");
	}
	 return L_vtrITTAX;
 }
 private void getITSPTX(String P_strPORNO,String P_strMATCD) // Get Item specific TAX
 {
	
	String L_strTAXCD,L_strCLPRF,L_strTAXFL,L_strTAXVL,L_strPRNLN="",L_strTEMP ="",L_strTXSTR="",L_strTAXDS; 
	if(vtrITTAX ==null)
		vtrITTAX = new Vector<String>();
	ResultSet L_rstTXSPC;
	try
	{
		M_strSQLQRY ="Select * from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_SBSCD ='"+M_strSBSCD+"'"+
					 "and TXT_DOCNO ='"+P_strPORNO+"' AND TXT_PRDCD ='"+P_strMATCD+"' AND isnull(TXT_STSFL,'') <>'X'"; 
		L_rstTXSPC = cl_dat.exeSQLQRY0(M_strSQLQRY);
		if(L_rstTXSPC !=null)
		{
			while(L_rstTXSPC.next())
			{
				L_strTAXCD = nvlSTRVL(L_rstTXSPC.getString("TXT_CODCD"),"");
				L_strTAXFL = nvlSTRVL(L_rstTXSPC.getString("TXT_CODFL"),"");
				L_strTAXVL = nvlSTRVL(L_rstTXSPC.getString("TXT_CODVL"),"0.00");
				L_strTAXDS = nvlSTRVL(L_rstTXSPC.getString("TXT_CODDS"),"");
				if(Double.parseDouble(L_strTAXVL) == 0)
				{
					L_strTXSTR =L_strTAXDS;
					if((L_strTAXCD.trim().equals("STX")) ||(L_strTAXCD.trim().equals("OTH")))
					{
						L_strTXSTR +="";
					}
					else
					{
						if(L_strTAXFL.equals("I"))
						    L_strTXSTR +=" : INCLUDED";
						else if(L_strTAXFL.equals("X")) 
						    L_strTXSTR +=" : AT ACTUALS";
					}
				}
				else
				{
							
					if(L_strTAXFL.equals("P"))
					{
						L_strTXSTR =L_strTAXDS +" : "+setNumberFormat(Double.parseDouble(L_strTAXVL),3)+" % ";
					}
					else
						L_strTXSTR =L_strTAXDS +" : "+setNumberFormat(Double.parseDouble(L_strTAXVL),3);
				}			
				vtrITTAX.addElement(L_strTXSTR);											
			}
			L_rstTXSPC.close();
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getITTAX");
	}
	finally
	{
		L_rstTXSPC = null;
	}
 }
///////
}
	/*'Kindly send us confirmation/certification  within 7 days  from the date of'
	  'actual excise duty paid, with details of Invoice No.& Date,Invoice amount'
	  'Duty payable, Duty paid, serial No. of cenvat/PLA account and date.Failure'
	  'to provide the certificate within time limit will compel us to raise debit'
	  'note equivalent to the excise duty applicable & will be recovered from you.'*/

