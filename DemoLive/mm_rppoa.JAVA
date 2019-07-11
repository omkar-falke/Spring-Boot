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

			   : Modified on 22/03/2005 to include functionality for printing, if only tax
 				 Description is changed in STX/OTH tax codes.	
*/

import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.File;import java.io.FileOutputStream;import java.io.FileInputStream;import java.io.DataOutputStream;
import java.io.PrintWriter;import java.io.FileNotFoundException;import java.io.IOException;
import java.util.Hashtable;import java.util.Vector;import java.util.StringTokenizer;

public class mm_rppoa extends cl_rbase
{
	private ResultSet L_rstRSSET,rstRSSET;
	private JTextField txtPORNO,txtAMDNO;
	private String strREPFL = cl_dat.M_strREPSTR_pbst+"mm_rppoa.doc";
	private String strPORNO,strAMDNO,strPORDT,strAMDDT,strURGCD,strVENCD,strPOSRL,strDSTCD,strDELTP,strDLTDS,strDLTDS1,strPMTRF,strCRDDY,strTEMP,strITMDS="";
	private String strQTNNO,strQTNDT,strCURCD,strCURDS,strCURDS1,strEFFDT,strCMPDT,strVENNM,strSHRDS,strPORVL;
	private String strPREBY,strFRWBY,strAUTBY,strPREDT,strFRWDT,strAUTDT,strPRVPOR ="";
	private String strISOR1,strISOR2,strISOR3,L_strPRTNO = "";
	private String strPRINT ="",strPRINT1,strSTRTP ="",strMATCD;
	private int intLINNO,intPAGNO,intRECCNT;
	private int intSRLNO =0;
	private double dblPORVL=0.00,dblEXGRT=0.0,dblEXVAL =0.0;
	private double dblPPOVL=0.0,dblPEXRT=0.0;
	boolean flgFIRST = false,flgFIRSTTM = true,flgFIRSTTM1 = true;
	private boolean	flgITTAX = false;
	private boolean	flgITSTX = false; // Flag item specific tax
    private boolean flgEXCFL = false;
	private boolean flgITHDG = false;
	private Hashtable<String,String> hst1to100;
	private Hashtable<String,String> hstTAXCD = new Hashtable<String,String>();
	private java.util.Hashtable hstMATCD = new java.util.Hashtable();
	private java.util.Vector<String> vtrTAXCD = new java.util.Vector<String>();
	private java.util.Vector<String> vtrTAXTP = new java.util.Vector<String>();
	private java.util.Vector<String> vtrTAXDT = new java.util.Vector<String>();
	private java.util.Vector<String> vtrTAXAM = new java.util.Vector<String>();
	private StringTokenizer L_STRTKN;
	private FileOutputStream O_FOUT;
    private DataOutputStream dosREPORT;

	mm_rppoa()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("P.O. No."),1,4,1,1,this,'L');
			add(txtPORNO = new TxtLimit(8),2,4,1,1,this,'L');
			add(new JLabel("Amd. No."),1,5,1,1,this,'L');
			add(txtAMDNO = new TxtLimit(2),2,5,1,1,this,'L');
			txtPORNO.requestFocus();
			txtAMDNO.setEnabled(false);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			crtHSTXCD();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		String L_ACT = L_AE.getActionCommand();
		strSTRTP =M_strSBSCD.substring(2,4);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			strSTRTP =M_strSBSCD.substring(2,4);
			txtAMDNO.setEnabled(false);
		}
	}
	public void keyPressed(KeyEvent L_KE){
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == 9 || L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{ 
			if (M_objSOURC == txtPORNO)
			{
				if(vldPORNO(M_strSBSCD.substring(2,4),txtPORNO.getText().trim()))
					cl_dat.M_btnSAVE_pbst.requestFocus();	
			}
		}
		if (L_KE.getKeyCode()== L_KE.VK_F1){					//F1 starts from here
			if(M_objSOURC == txtPORNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				if(M_objSOURC == txtPORNO)
					M_strHLPFLD = "txtPORNO";
				String L_ARRHDR[] = {"P.O No.","Amd. No."};
				M_strSQLQRY = "Select distinct PO_PORNO,PO_AMDNO";
				M_strSQLQRY += "  from MM_POMST where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'') <>'X' and isnull(po_amdno,'') <>'00'";
				M_strSQLQRY += " and PO_MMSBS ='"+M_strSBSCD+"'";
				M_strSQLQRY += " and PO_STRTP ='"+strSTRTP+"'";
				if(M_objSOURC == txtPORNO)
				{
					if(txtPORNO.getText().trim().length() >0)
						M_strSQLQRY +=" AND PO_PORNO like '"+txtPORNO.getText().trim()+"%'";
				}
				M_strSQLQRY += " order by PO_PORNO DESC";
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtPORNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"exeHLPOK");
		}
	}
	// Method to validate the P.O. No.
	private boolean vldPORNO(String P_strSTRTP,String P_strPORNO)
	{
		try
		{
			M_strSQLQRY = "Select PO_PORNO,PO_AMDNO from MM_POMST ";
            M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'')  <> 'X'";
			M_strSQLQRY += " and PO_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " and PO_STRTP ='"+P_strSTRTP+"'";
			M_strSQLQRY += " and PO_PORNO ='"+P_strPORNO+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				strTEMP =nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),"");
				if(strTEMP.equals("00"))
				{
					setMSG("Amendment printing is not available for amendment no '00'",'E');
					return false;
				}
				txtAMDNO.setText(M_rstRSSET.getString("PO_AMDNO"));
				return true;	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldPORNO");
			return false;

		}
		setMSG("Invalid .P.O. number ..",'E');
		return false;
	}
	boolean vldDATA()
	{
		boolean L_flgRPT = false;
		if(M_rdbHTML.isSelected())
		     strREPFL = cl_dat.M_strREPSTR_pbst + "mm_rppoa.html";
		if(M_rdbTEXT.isSelected())
		     strREPFL = cl_dat.M_strREPSTR_pbst + "mm_rppoa.doc";
	
		if(txtPORNO.getText().trim().length() ==0)
		{
			setMSG("from P.O. No. can not be blank..",'E');
			txtPORNO.requestFocus();
			return false;
		}
		else if(!vldPORNO(strSTRTP,txtPORNO.getText().trim()))
		{
			setMSG("Invalid P.O. No...",'E');
			txtPORNO.requestFocus();
			return false;
		}
		return true;
	}
	private void prnMNHDR()
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
			strISOR1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPPOR01");
			strISOR2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPPOR02");
			strISOR3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPPOR03");
			
			M_strSQLQRY = "Select PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM,PT_CONNM,PT_PINCD FROM CO_PTMST WHERE ";
			M_strSQLQRY +=" PT_PRTTP ='S' AND PT_PRTCD ='"+strVENCD+"'";
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
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strCPI17);
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOR1,152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOR2,152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOR3,152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",152));
			dosREPORT.writeBytes("\n");
			M_strSQLQRY = "Select CMT_CODDS,CMT_CHP01 FROM CO_CDTRN WHERE ";
			M_strSQLQRY +=" CMT_CGMTP ='MST' AND CMT_CGSTP ='COXXCUR' AND CMT_CODCD ='"+strCURCD+"'";
			rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(rstRSSET !=null)
			if(rstRSSET.next())
			{
				strCURDS = nvlSTRVL(rstRSSET.getString("CMT_CODDS"),"");
				strCURDS1 = nvlSTRVL(rstRSSET.getString("CMT_CHP01"),"");
			}
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			{
    			prnFMTCHR(dosREPORT,M_strNOCPI17);
    			prnFMTCHR(dosREPORT,M_strCPI10);
    			prnFMTCHR(dosREPORT,M_strENH);
    			prnFMTCHR(dosREPORT,M_strBOLD);
    		}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"",10));
			dosREPORT.writeBytes("PURCHASE ORDER AMENDMENT");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			{
			    prnFMTCHR(dosREPORT,M_strNOBOLD);
			    prnFMTCHR(dosREPORT,M_strNOENH);
			    prnFMTCHR(dosREPORT,M_strCPI10);
			    prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes(padSTRING('R',"Attn: "+L_strCONNM,65));	
			dosREPORT.writeBytes("P.O. Series           : "+strPOSRL);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Supplier Code      : "+strVENCD,65));	
			dosREPORT.writeBytes("P.O. No.              : "+strSTRTP+"/"+strPORNO );
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");    
			dosREPORT.writeBytes(padSTRING('R',strVENNM,65));	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");    
			dosREPORT.writeBytes("P.O. Date             : "+strPORDT );
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',L_strADR01,65));	
			dosREPORT.writeBytes("Quotation No          : "+strQTNNO);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',L_strADR02,65));	
			dosREPORT.writeBytes("Quotation Date        : "+strQTNDT);
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('R',L_strADR03,65));	
			dosREPORT.writeBytes("P.O. Effective Date   : "+strEFFDT);
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('R',L_strADR04,65));	
			dosREPORT.writeBytes("P.O. Completion Date  : "+strCMPDT);
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");    
			dosREPORT.writeBytes("\n");
			intLINNO = 17;
			dosREPORT.writeBytes(padSTRING('L',"Amendment No.         : "+strAMDNO,91));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"Amendment Date        : "+strAMDDT,99));
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strNOBOLD);
            if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			crtLINE(106);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("P.O.Short Desc. : "+strSHRDS);
			dosREPORT.writeBytes("\n");
			crtLINE(106);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Further to our Purchase Order, ("+strPORNO +"), we are pleased to release this amendment, ("+strAMDNO +").\n");
			dosREPORT.writeBytes("This amended purchase order does not incorporates the earlier amendments (if any).\n");
			dosREPORT.writeBytes("(Receipt of this purchase order and its acceptance must be acknowledged on the confirmation  copy\n");
			dosREPORT.writeBytes("within 15 days of receipt of this order,failing which it will be deemed to have been accepted  by\n");
			dosREPORT.writeBytes("you.Completion / Delivery time must be strictly adhered to. PO Number & Material code must appear\n");
			dosREPORT.writeBytes("on all challans, invoices and further correspondence.\n");
			crtLINE(106);
			dosREPORT.writeBytes("\n");
			intLINNO += 12;
		}
		catch(Exception L_E)
		{
			setMSG("prnMNHDR",'E');
		}
	}
	private void prnPGHDR(String P_strPORNO)
	{  
		try
		{
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			{
			    prnFMTCHR(dosREPORT,M_strCPI10);
			    prnFMTCHR(dosREPORT,M_strCPI17);
			}
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOR1,152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOR2,152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOR3,152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",152));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			{
    			prnFMTCHR(dosREPORT,M_strNOCPI17);
    			prnFMTCHR(dosREPORT,M_strCPI10);
    			prnFMTCHR(dosREPORT,M_strCPI12);
			}
			dosREPORT.writeBytes(padSTRING('R',"Supplier Code      : "+strVENCD,65));	
			dosREPORT.writeBytes("P.O. No.              : "+strSTRTP+"/"+P_strPORNO );
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");    
			dosREPORT.writeBytes(padSTRING('R',strVENNM,65));	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");    
			dosREPORT.writeBytes("P.O. Date             : "+strPORDT );
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"Amendment No.         : "+strAMDNO,91));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"Amendment Date        : "+strAMDDT,99));
			dosREPORT.writeBytes("\n");
			crtLINE(106);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("P.O.Short Desc. : "+strSHRDS);
			dosREPORT.writeBytes("\n");
			crtLINE(106);
			dosREPORT.writeBytes("\n");
			intLINNO +=14;
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
			dosREPORT.writeBytes(L_strLIN);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"CRTLine");
		}
	}
	private void prnFOOTR(String P_strPORNO,String P_strAMDNO)
	{
		try
		{
			if(intLINNO > 58)
            {
               	crtLINE(106);
				dosREPORT.writeBytes ("\n");
				intPAGNO += 1;
				dosREPORT.writeBytes("Contd..."+intPAGNO);
				for(int i=0;i<60-intLINNO;i++)
				{
					dosREPORT.writeBytes ("\n");
				}
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
				    prnFMTCHR(dosREPORT,M_strEJT);
				if(M_rdbHTML.isSelected())
				    dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
				intLINNO = 0;
				prnPGHDR(P_strPORNO);
			}
			strPORVL ="0.0";
			mm_rppor obj = new mm_rppor(M_strSBSCD,hstTAXCD,vtrTAXCD,vtrTAXTP,strCURCD,dblEXGRT);
			strPORVL = obj.calPOVAL(P_strPORNO,P_strAMDNO,'P');
			//strPORVL = calPOVAL(P_strPORNO);
			String L_strINWRD = obj.getCURWRD(strPORVL,strCURDS1);
		 	if((strCURDS+L_strINWRD).length() >65)
			{
				dosREPORT.writeBytes(padSTRING('R',"(In Words) "+strCURDS+" "+L_strINWRD.substring(0,55)+"-",76)+"Order Value     "+padSTRING('L',strPORVL,13));
				dosREPORT.writeBytes ("\n");			
				intLINNO++;
				dosREPORT.writeBytes(padSTRING('L',"",11));
				dosREPORT.writeBytes(L_strINWRD.substring(55));
				dosREPORT.writeBytes ("\n");			
				intLINNO++;
			}
			else
				dosREPORT.writeBytes(padSTRING('R',"(In Words) "+strCURDS+" "+L_strINWRD,80)+"Order Value "+padSTRING('L',strPORVL,13));
			dosREPORT.writeBytes ("\n");
           // System.out.println("dblPPOVL "+ dblPPOVL+" "+dblPEXRT);
			dosREPORT.writeBytes(padSTRING('R'," ",76)+"Prv. Order Value"+padSTRING('L',setNumberFormat(dblPPOVL/dblPEXRT,2),13));
			intLINNO++;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");    
			dosREPORT.writeBytes ("\n");
			intLINNO ++;
			crtLINE(106);
			dosREPORT.writeBytes ("\n");
			intLINNO++;
			chkNEWPG();		
			if(intLINNO > 53)
            {
               	crtLINE(106);
				dosREPORT.writeBytes ("\n");
				intPAGNO += 1;
				dosREPORT.writeBytes("Contd..."+intPAGNO);
				for(int i=0;i<60-intLINNO;i++)
				{
					dosREPORT.writeBytes ("\n");
				}
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
				    prnFMTCHR(dosREPORT,M_strEJT);
                if(M_rdbHTML.isSelected())
				    dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				intLINNO = 0;
				prnPGHDR(P_strPORNO);
			}
         	dosREPORT.writeBytes (padSTRING('R',"Supplier's Acceptance",38)+padSTRING('R',"Prepared & Checked By",38)+padSTRING('R',"For "+cl_dat.M_strCMPNM_pbst,36));
			dosREPORT.writeBytes ("\n");
			if(strAUTBY.trim().length() >0)
				strAUTBY ="NG";
			dosREPORT.writeBytes(padSTRING('R'," ",38)+padSTRING('R',strPREBY,38)+padSTRING('R',strAUTBY,36));
			strAUTBY ="";
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			intLINNO+=4;
			dosREPORT.writeBytes (padSTRING('R',"",70)+padSTRING('L',"Authorised Signatory ",36));
			dosREPORT.writeBytes ("\n");
			intLINNO++;
			crtLINE(106);
			dosREPORT.writeBytes ("\n");
			for(int i=0;i<61-intLINNO;i++)
			{
				dosREPORT.writeBytes ("\n");
			}
			crtLINE(106);
			dosREPORT.writeBytes ("\n");
			M_strSQLQRY ="Select CP_CSTNO,CP_CSTDT,CP_BSTNO,CP_BSTDT,CP_ECCNO from co_CPMST";
			rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(rstRSSET !=null)
			if(rstRSSET.next())
			{
				String L_strCSTNO = nvlSTRVL(rstRSSET.getString("CP_CSTNO"),"");
				String L_strCSTDT = nvlSTRVL(rstRSSET.getString("CP_CSTDT"),"");
				String L_strSSTNO = nvlSTRVL(rstRSSET.getString("CP_BSTNO"),"");
				String L_strSSTDT = nvlSTRVL(rstRSSET.getString("CP_BSTDT"),"");
				String L_strECCNO = nvlSTRVL(rstRSSET.getString("CP_ECCNO"),"");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
				    prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
				    dosREPORT.writeBytes("<b>");    
				dosREPORT.writeBytes("CST NO.: "+L_strCSTNO+" wef."+L_strCSTDT+"     BST No.: "+L_strSSTNO+" wef."+L_strSSTDT +"     ECC NO.: "+L_strECCNO);
				dosREPORT.writeBytes ("\n");
				dosREPORT.writeBytes ("\n");
				dosREPORT.writeBytes ("\n");
				flgFIRSTTM = true;
				flgFIRSTTM1 = true;				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
				    prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
				    dosREPORT.writeBytes("</b>");    
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			{
			    prnFMTCHR(dosREPORT,M_strEJT);				
			    prnFMTCHR(dosREPORT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
			if(rstRSSET !=null)
				rstRSSET.close();
			intLINNO = 0;
			intPAGNO = 1;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}	
	void chkNEWPG()
	{
		try
		{
			if(intLINNO > 60)
			{
				crtLINE(106);
				dosREPORT.writeBytes ("\n");
				intPAGNO += 1;
				dosREPORT.writeBytes("Contd..."+intPAGNO);
				dosREPORT.writeBytes ("\n");
				dosREPORT.writeBytes ("\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
			    	prnFMTCHR(dosREPORT,M_strEJT);
			    if(M_rdbHTML.isSelected())
				    dosREPORT.writeBytes("<P CLASS = \"breakhere\">");	
				intLINNO = 0;
				prnPGHDR(strPORNO); 
			}
		}
		catch(Exception L_E)
		{
			setMSG("Error in chkNWPAGE ",'E');
		}
	}
void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				if(dosREPORT !=null)
					dosREPORT.close();
				if(O_FOUT !=null)
					O_FOUT.close();
				prnPOAMD(txtPORNO.getText().trim(),txtAMDNO.getText().trim()); 
				strPRVPOR ="";
				if(M_rdbHTML.isSelected())
    			{
    			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
    			}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
				   Runtime r = Runtime.getRuntime();
					Process p = null;
				    if(M_rdbTEXT.isSelected())
		                 doPRINT(strREPFL);
					else if(M_rdbHTML.isSelected())
					    p  = r.exec("c:\\windows\\iexplore.exe "+strREPFL); 
					if(dosREPORT !=null)
						dosREPORT.close();
					if(O_FOUT !=null)
						O_FOUT.close();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					try
					{
						if(M_rdbTEXT.isSelected())
						    p  = r.exec("c:\\windows\\wordpad.exe "+strREPFL); 
						else if(M_rdbHTML.isSelected())
						    p  = r.exec("c:\\windows\\iexplore.exe "+strREPFL); 
						if(dosREPORT !=null)
							dosREPORT.close();
						if(O_FOUT !=null)
							O_FOUT.close();
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"Error.exescrn.. ");
 					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
				   	cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strREPFL,"Purchase Order Amendment"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
			    }
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				{
					if(M_txtDESTN.getText().length()>0)
						setMSG("File Copied to file:"+M_txtDESTN.getText().trim()+"",'N');
					else
						setMSG("File Copied to file:c:\\reports\\mm_rppoa.doc",'N');
				}
				
			}//vldDATA
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Error in exePRINT ");
		}
		finally
		{
			try
			{
				if(dosREPORT !=null)
					dosREPORT.close();
				if(O_FOUT !=null)
					O_FOUT.close();
			}
			catch(Exception L_E)
			{
				
			}
		}
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
			// Added
			L_strHSVAL += "|"+nvlSTRVL(L_rstRSSET.getString("CMT_MODLS")," "+"|") ;// Multiplier
		//	if(nvlSTRVL(L_rstRSSET.getString("CMT_CHP01"),"").equals("01"))
		//	{
				hstTAXCD.put(L_rstRSSET.getString("CMT_CODCD"),L_strHSVAL);
				vtrTAXCD.addElement(L_rstRSSET.getString("CMT_CODCD"));
		//	}
		vtrTAXTP.addElement(L_rstRSSET.getString("CMT_CHP01"));
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"crtHSTCD");
	}

}
 private void prnPOAMD(String P_strPORNO,String P_strAMDNO)
 {
	String L_strPAMDNO="",L_strMATCD="",L_strMATDS ="",L_strINDNO ="";
	double L_dblPORQT=0.0;
	double L_dblPPOQT=0.0;
	double L_dblPORRT=0.0;
	double L_dblPPORT=0.0;
	int L_intSRLNO =0;
	java.sql.Date L_datTEMP;
	try
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
		setMSG("Report Generation in Progress....",'N');
		O_FOUT = new FileOutputStream(strREPFL);	
		dosREPORT = new DataOutputStream(O_FOUT);
		intLINNO = 0;
		intPAGNO = 1;
		if(M_rdbHTML.isSelected())
		{
		    dosREPORT.writeBytes("<HTML><HEAD><Title>Purchase Order Amendment </title> ");
			dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
			dosREPORT.writeBytes("</STYLE></HEAD>"); 
			dosREPORT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
		}
			
		int L_intTEMP = Integer.parseInt(P_strAMDNO)-1;
		for(int i=0;i<(2-String.valueOf(L_intTEMP).length());i++)
			L_strPAMDNO +="0";
		L_strPAMDNO += String.valueOf(L_intTEMP);
		M_strSQLQRY =" select cmt_shrds from co_cdtrn where cmt_cgmtp ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp ='MM"+M_strSBSCD.substring(2,4) +"POR'and SUBSTRING(cmt_codcd,3,1)='"+txtPORNO.getText().trim().substring(2,3)+"'";
		//System.out.println(M_strSQLQRY);
		L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
		strPOSRL ="";
		if(L_rstRSSET !=null)
		if(L_rstRSSET.next())
		strPOSRL = nvlSTRVL(L_rstRSSET.getString("cmt_shrds"),"");
		M_strSQLQRY = "select a.po_porno,a.po_indno L_INDNO1,a.po_matcd L_MATCD1,st_matds,a.po_matds L_MATDS1,a.po_amdno,a.po_porqt L_PORQT1,a.po_porrt L_PORRT1,a.po_exgrt L_EXGRT1,b.po_porqt L_PORQT2,b.po_porrt L_PORRT2,b.po_exgrt L_EXGRT2,"+
					  "a.po_shrds,a.po_pordt,a.po_qtnno,a.po_qtndt,a.po_effdt,a.po_cmpdt,a.po_amdno,a.po_amddt,a.po_vencd,a.po_pmtrf L_PMTRF1,a.po_dstcd L_DSTCD1,a.po_crddy L_CRDDY1"+
					  ",b.po_pmtrf L_PMTRF2,b.po_matds L_MATDS2,b.po_dstcd L_DSTCD2,b.po_crddy L_CRDDY2,a.po_preby,a.po_autby,a.po_curcd,b.po_porvl L_PORVL,a.po_stsfl L_STSFL1,b.po_stsfl L_STSFL2 from mm_stmst,mm_pomst a "+
					  "LEFT OUTER JOIN mm_pomam b ON  a.po_cmpcd = b.po_cmpcd and a.po_strtp = b.po_strtp and a.po_porno = b.po_porno and a.po_indno = b.po_indno and a.po_matcd = b.po_matcd and b.po_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND b.po_amdno ='"+ L_strPAMDNO+"'"+
					  " where a.po_cmpcd = st_cmpcd and a.po_strtp = st_strtp and a.po_matcd =  ST_MATCD and "+
					  "a.po_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND a.po_strtp ='"+M_strSBSCD.substring(2,4) +"'"+
					  " and a.po_porno ='"+P_strPORNO +"' and a.po_amdno ='"+P_strAMDNO +"' order by a.po_matcd,a.po_indno";
					  // order by sequence in above line changed from indno,matcd to matcd,indno on 25/08/2005
					  // as it was giving prob in printing item wise tax
		//System.out.println(M_strSQLQRY);
		L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		int L_intRECCT = 0;
		String L_strPMTR1="",L_strPMTR2="",L_strDSTC1="",L_strDSTC2="",L_strCRDD1="",L_strCRDD2="";
		boolean L_flgPRN = false;
		boolean L_flgHDG = false;
		flgITTAX = false;
		flgITSTX = false;
		String L_strTAXCD = "";
		M_strSQLQRY = "select a.TX_PRDCD L_PRDCD1";
		for(int i=0;i<vtrTAXCD.size();i++)
		{
			L_strTAXCD = vtrTAXCD.elementAt(i).toString();
			if(vtrTAXTP.elementAt(i).equals("01"))
				M_strSQLQRY += ",a.TX_"+L_strTAXCD+ "VL L_"+L_strTAXCD+"VL1,a.TX_"+L_strTAXCD +"FL L_"+L_strTAXCD+"FL1,b.TX_"+L_strTAXCD+ "VL L_"+L_strTAXCD+"VL2,b.TX_"+L_strTAXCD +"FL L_"+L_strTAXCD+"FL2";
			if((L_strTAXCD.equals("STX"))||(L_strTAXCD.equals("OTH")))
			{
				M_strSQLQRY += ",a.TX_"+L_strTAXCD+ "DS L_"+L_strTAXCD+"DS1,b.TX_"+L_strTAXCD +"DS L_"+L_strTAXCD+"DS2";
			}
		}
				
		M_strSQLQRY +=  " from co_txdoc a left outer join co_txdam b on "+
					  " a.tx_cmpcd = b.tx_cmpcd AND a.tx_syscd = b.tx_syscd AND a.tx_doctp = b.tx_doctp and a.tx_sbstp = b.tx_sbstp and a.tx_prdcd = b.tx_prdcd and a.tx_docno = b.tx_docno "+
					  " and b.tx_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND b.tx_amdno ='"+ L_strPAMDNO+"' and b.tx_stsfl <>'X' "+
					  " where a.tx_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND a.tx_sbstp ='"+M_strSBSCD.substring(2,4) +"'"+  
					  " and a.tx_docno ='"+P_strPORNO+"' and a.tx_amdno ='"+P_strAMDNO+
					  "' and a.tx_prdcd <>'XXXXXXXXXX' and a.tx_stsfl <>'X'  order by a.tx_prdcd";
		//System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
		if(M_rstRSSET != null)
		{
			if(M_rstRSSET.next())
				flgITTAX = true;
			// Resultset is kept open , will be used afterwards
		}
		String L_strSQLQRY="";
		ResultSet L_rstTXSPC ;
				
		L_strSQLQRY ="Select a.txt_codfl L_CODFL1,A.TXT_CODVL L_CODVL1,A.TXT_STSFL L_STSFL1,B.txt_codfL L_CODFL2,B.TXT_CODVL L_CODVL2,B.TXT_STSFL L_STSFL2 "+
					 "from CO_TXSPC A LEFT OUTER JOIN CO_TXSAM B ON A.TXT_CMPCD = B.TXT_CMPCD AND A.TXT_SYSCD = B.TXT_SYSCD AND A.TXT_SBSCD = B.TXT_SBSCD AND A.TXT_DOCTP = B.TXT_DOCTP AND "+
					 " A.TXT_DOCNO =B.TXT_DOCNO AND A.TXT_PRDCD = B.TXT_PRDCD AND B.TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.TXT_AMDNO ='"+L_strPAMDNO+"'" +
					 " WHERE A.TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.TXT_SYSCD ='MM' and A.TXT_DOCTP ='POR' AND A.TXT_SBSCD = '"+M_strSBSCD +"'" +" AND A.TXT_SBSTP = '"+M_strSBSCD.substring(2,4) +"'";
		L_strSQLQRY += "AND a.TXT_DOCNO ='"+P_strPORNO +"' and A.TXT_AMDNO = '"+P_strAMDNO+"' AND a.TXT_PRDCD <> 'XXXXXXXXXX' ORDER BY a.TXT_PRDCD,a.txt_CODCD";
		//System.out.println(L_strSQLQRY);
		L_rstTXSPC	= cl_dat.exeSQLQRY2(L_strSQLQRY);
		if(L_rstTXSPC != null)
		{
			if(L_rstTXSPC.next())
			{
				//flgITTAX = true;
				flgITSTX = true;
			}
		
		}
		String L_strTAXDS="",L_strSTXDS,L_strOTHDS,L_strMULT,L_strPRVFL="",L_strCURFL="",L_strPRVVL="",L_strCURVL="",L_strTXPRD ="",L_strPRVDS ="",L_strCURDS ="" ;
		String L_strADDDS="",L_strPADDS="",L_strPSTDS="",L_strCSTDS="",L_strPOTDS="",L_strCOTDS="";
		String L_strSTSFL="",L_strPSTSFL="",L_strPRVST="",L_strCURST="";
		L_strTAXCD="";
		if(L_rstRSSET !=null)
			while(L_rstRSSET.next())
			{
				L_intSRLNO =0;
				flgITHDG = false;
				if(L_intRECCT ==0)
				{
					strPORNO = nvlSTRVL(L_rstRSSET.getString("PO_PORNO"),"");
					strAMDNO = L_rstRSSET.getString("PO_AMDNO");
					strSHRDS = nvlSTRVL(L_rstRSSET.getString("PO_SHRDS"),"");
					strVENCD = nvlSTRVL(L_rstRSSET.getString("PO_VENCD"),"");
					strPREBY = nvlSTRVL(L_rstRSSET.getString("PO_PREBY"),"");
					strAUTBY = nvlSTRVL(L_rstRSSET.getString("PO_AUTBY"),"");
					strCURCD = nvlSTRVL(L_rstRSSET.getString("PO_CURCD"),"");
					dblEXGRT = L_rstRSSET.getDouble("L_EXGRT1");
					dblPEXRT = L_rstRSSET.getDouble("L_EXGRT2");
					dblPPOVL = L_rstRSSET.getDouble("L_PORVL");
				//	System.out.println("1 dblPPOVL "+dblPPOVL);
					L_strPMTR1 = nvlSTRVL(L_rstRSSET.getString("L_PMTRF1"),"");
					L_strPMTR2 = nvlSTRVL(L_rstRSSET.getString("L_PMTRF2"),"");
					L_strDSTC1 = nvlSTRVL(L_rstRSSET.getString("L_DSTCD1"),"");
					L_strDSTC2 = nvlSTRVL(L_rstRSSET.getString("L_DSTCD2"),"");
					L_strCRDD1 = nvlSTRVL(L_rstRSSET.getString("L_CRDDY1"),"");
					L_strCRDD2 = nvlSTRVL(L_rstRSSET.getString("L_CRDDY2"),"");
					L_datTEMP = L_rstRSSET.getDate("PO_EFFDT");
					if(L_datTEMP !=null)
						strEFFDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strEFFDT = "";
					L_datTEMP = L_rstRSSET.getDate("PO_CMPDT");
					if(L_datTEMP !=null)
						strCMPDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strCMPDT = "";
					L_datTEMP = L_rstRSSET.getDate("PO_PORDT");
					if(L_datTEMP !=null)
						strPORDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strPORDT = "";			
					strQTNNO = L_rstRSSET.getString("PO_QTNNO");
					L_datTEMP = L_rstRSSET.getDate("PO_QTNDT");
					if(L_datTEMP !=null)
						strQTNDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strQTNDT = "";
					L_datTEMP = L_rstRSSET.getDate("PO_AMDDT");
					if(L_datTEMP !=null)
						strAMDDT = M_fmtLCDAT.format(L_datTEMP);
					else
						strAMDDT = "";
					prnMNHDR();
				}
				L_strSTSFL = nvlSTRVL(L_rstRSSET.getString("L_STSFL1"),"");
				L_strPSTSFL = nvlSTRVL(L_rstRSSET.getString("L_STSFL2"),"");
		        // THIS CONDITION INCLUDED ON 25/10/2005 AS in case of newly added row,
		        // previous value is not available in amendment table    
				if(dblPPOVL ==0)
				{
					dblEXGRT = L_rstRSSET.getDouble("L_EXGRT1");
					dblPEXRT = L_rstRSSET.getDouble("L_EXGRT2");
					dblPPOVL = L_rstRSSET.getDouble("L_PORVL");
				}
				L_dblPORQT = L_rstRSSET.getDouble("L_PORQT1");
				L_dblPPOQT = L_rstRSSET.getDouble("L_PORQT2");
				L_dblPORRT = L_rstRSSET.getDouble("L_PORRT1");
				L_dblPPORT = L_rstRSSET.getDouble("L_PORRT2");
				L_strMATCD = L_rstRSSET.getString("L_MATCD1");
             	L_strINDNO = L_rstRSSET.getString("L_INDNO1");
				L_strMATDS = nvlSTRVL(L_rstRSSET.getString("ST_MATDS"),"");
				L_strADDDS = nvlSTRVL(L_rstRSSET.getString("L_MATDS1"),"");
				L_strPADDS = nvlSTRVL(L_rstRSSET.getString("L_MATDS2"),"");
				flgITHDG = false;
				if(!L_strSTSFL.equals("X"))
				{
					if(!L_strADDDS.equals(L_strPADDS))
					{
						L_intSRLNO++;
						if(L_intSRLNO == 1)
						{
							dosREPORT.writeBytes("Change in ");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
							    prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("<b>");    
							dosREPORT.writeBytes(L_strINDNO +"/");
							dosREPORT.writeBytes(L_strMATCD);
							dosREPORT.writeBytes(" : "+L_strMATDS);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
							    prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("</b>");    
							dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended");
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("\n");
							intLINNO += 3;
							chkNEWPG();
							flgITHDG = true;
						}
						dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    Description",38));	
						dosREPORT.writeBytes(padSTRING('L',L_strADDDS,30)+padSTRING('L',L_strPADDS,30));	
						dosREPORT.writeBytes("\n");
						intLINNO++;
						chkNEWPG();
		
					}
					if(L_dblPORQT != L_dblPPOQT)
					{
						L_intSRLNO++;
						if(L_intSRLNO ==1)
						{
							dosREPORT.writeBytes("Change in ");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
							    prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("<b>");    
							dosREPORT.writeBytes(L_strINDNO +"/");
							dosREPORT.writeBytes(L_strMATCD);
							dosREPORT.writeBytes(" : "+L_strMATDS);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
							    prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("</b>");    
							dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended");
							intLINNO++;
							chkNEWPG();
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("\n");
							intLINNO +=2;
							chkNEWPG();
							flgITHDG = true;
						}
						dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    Quantity",38));	
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPPOQT,3),30)+padSTRING('L',setNumberFormat(L_dblPORQT,3),30));	
						dosREPORT.writeBytes("\n");
						intLINNO++;
						chkNEWPG();
					}
					if(L_dblPORRT != L_dblPPORT)
					{
						L_intSRLNO++;
						if(L_intSRLNO == 1)
						{
							dosREPORT.writeBytes("Change in ");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
							    prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("<b>");    
							dosREPORT.writeBytes(L_strINDNO +"/");
							dosREPORT.writeBytes(L_strMATCD);
							dosREPORT.writeBytes(" : "+L_strMATDS);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
							    prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("</b>");    
							dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended");
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("\n");
							intLINNO += 3;
							chkNEWPG();
							flgITHDG = true;
						}
						dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    Price",38));	
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPPORT,3),30)+padSTRING('L',setNumberFormat(L_dblPORRT,3),30));	
						dosREPORT.writeBytes("\n");
						intLINNO++;
						chkNEWPG();
					}
					L_intRECCT++;
					// check the item wise taxes
					L_strSQLQRY ="";
					if(flgITTAX)
					{
					   L_strTXPRD = M_rstRSSET.getString("L_PRDCD1");
					   if(L_strMATCD.equals(L_strTXPRD))   // Incase of specific tax ???
					   {
						  // print item wise taxes	
						   for(int i=0;i<vtrTAXCD.size();i++)
							{
							    if(vtrTAXTP.elementAt(i).equals("01"))
								{	
									L_flgPRN = false;
									L_strTAXCD = vtrTAXCD.elementAt(i).toString();
							   		/*L_strCURVL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"VL1"),"");
									L_strCURFL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"FL1"),"");
									L_strPRVVL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"VL2"),"");
									L_strPRVFL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"FL2"),"");*/
									L_strCURVL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"VL1"),"0.00");
									L_strCURFL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"FL1"),"");
									L_strPRVVL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"VL2"),"0.00");
									L_strPRVFL = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"FL2"),"");
									if((L_strTAXCD.equals("STX"))||(L_strTAXCD.equals("OTH")))
									{
										if(L_strTAXCD.equals("STX"))
										{
											L_strCSTDS = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"DS1"),"");
											L_strPSTDS = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"DS2"),"");
										}
										else
										{
											L_strCOTDS = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"DS1"),"");
											L_strPOTDS = nvlSTRVL(M_rstRSSET.getString("L_"+L_strTAXCD+"DS2"),"");
										}
										
									}
									if((L_strPRVVL.equals(L_strCURVL))&&(L_strPRVFL.equals(L_strCURFL)))
									{
										L_flgPRN = false;
									}
									else
									{
										L_flgPRN = true;
										L_intSRLNO++;
									}
									if(L_flgPRN)    // print flag is true
									{
									    // Commented on 18/08/2005, check in case of problem 
										/*if(!L_flgHDG)	// Heading is not given
										{
											L_intSRLNO =0;
											L_intSRLNO++;
											dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended\n\n");
											intLINNO+=3;
											chkNEWPG();
											L_flgHDG = true;
										}*/
										if(!flgITHDG)
										{
											dosREPORT.writeBytes("Change in ");
											if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
											    prnFMTCHR(dosREPORT,M_strBOLD);
											if(M_rdbHTML.isSelected())
				                                dosREPORT.writeBytes("<b>");    
											dosREPORT.writeBytes(L_strINDNO +"/");
											dosREPORT.writeBytes(L_strMATCD);
											dosREPORT.writeBytes(" : "+L_strMATDS);
											if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
											    prnFMTCHR(dosREPORT,M_strNOBOLD);
											if(M_rdbHTML.isSelected())
				                                dosREPORT.writeBytes("</b>");    
											dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended");
											dosREPORT.writeBytes("\n");
											dosREPORT.writeBytes("\n");
											intLINNO += 3;
											chkNEWPG();
											flgITHDG = true;
										}
										if(L_flgPRN)
										{
											if((!L_strTAXCD.equals("STX"))&&(!L_strTAXCD.equals("OTH")))
											{
												L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
												if(L_STRTKN !=null)
												{
													L_strMULT = L_STRTKN.nextToken();
													L_strTAXDS = L_STRTKN.nextToken();
												}
											}
											dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    "+L_strTAXDS,38));
											if(L_strPRVFL.equals("X"))
												L_strPRVVL	= "At Actuals";
											else if(L_strPRVFL.equals("I"))
												L_strPRVVL	= "Included";
											else if(L_strPRVFL.equals("P"))
												L_strPRVVL	= L_strPRVVL + " %";
										
											if(L_strCURFL.equals("X"))
												L_strCURVL	= "At Actuals";
											else if(L_strCURFL.equals("I"))
												L_strCURVL	= "Included";
											else if(L_strCURFL.equals("P"))
												L_strCURVL	= L_strCURVL + " %";
											dosREPORT.writeBytes(padSTRING('L',L_strPRVVL,30)+padSTRING('L',L_strCURVL,30));	
											dosREPORT.writeBytes("\n");
											intLINNO++;
											chkNEWPG();
										}
									}
									else if((L_strTAXCD.equals("STX"))||(L_strTAXCD.equals("OTH"))) // If only description is changed, added on 22/03/2005
									{
										if(!flgITHDG)
										{
											if((!L_strPSTDS.equals(L_strCSTDS))||(!L_strPOTDS.equals(L_strCOTDS)))
											{
    											L_intSRLNO++;
    											dosREPORT.writeBytes("Change in ");
    											if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
    											    prnFMTCHR(dosREPORT,M_strBOLD);
    											if(M_rdbHTML.isSelected())
				                                    dosREPORT.writeBytes("<b>");    
    											dosREPORT.writeBytes(L_strINDNO +"/");
    											dosREPORT.writeBytes(L_strMATCD);
    											dosREPORT.writeBytes(" : "+L_strMATDS);
    											if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
    											    prnFMTCHR(dosREPORT,M_strNOBOLD);
    											if(M_rdbHTML.isSelected())
				                                    dosREPORT.writeBytes("</b>");    
    											dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended");
    											dosREPORT.writeBytes("\n");
    											dosREPORT.writeBytes("\n");
    											intLINNO += 3;
    											chkNEWPG();
    											flgITHDG = true;
    											dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    "+L_strTAXDS,38));
    											if(L_strTAXCD.equals("STX"))
    												dosREPORT.writeBytes(padSTRING('L',L_strPSTDS,30)+padSTRING('L',L_strCSTDS,30));	
    											else if(L_strTAXCD.equals("OTH"))
    												dosREPORT.writeBytes(padSTRING('L',L_strPOTDS,30)+padSTRING('L',L_strCOTDS,30));	
    											
    											dosREPORT.writeBytes("\n");
    											intLINNO++;
    											chkNEWPG();
											}
										}
									}
								}
							}
						   // check if some prob occurs , vtrtxtp is included
							if(M_rstRSSET.next())
								flgITTAX = true;
							else
								flgITTAX = false;
					   }
					}
					if(flgITSTX)
					{
					 	L_strSQLQRY ="Select a.txt_codfl L_CODFL1,A.TXT_CODVL L_CODVL1,A.TXT_STSFL L_STSFL1,B.txt_codfL L_CODFL2,B.TXT_CODVL L_CODVL2,B.TXT_STSFL L_STSFL2 "+
					 				 "from CO_TXSPC A LEFT OUTER JOIN CO_TXSAM B ON A.TXT_CMPCD = B.TXT_CMPCD AND A.TXT_SYSCD = B.TXT_SYSCD AND A.TXT_SBSCD = B.TXT_SBSCD AND A.TXT_DOCTP = B.TXT_DOCTP AND "+
					 				 " A.TXT_DOCNO =B.TXT_DOCNO AND A.TXT_PRDCD = B.TXT_PRDCD and B.TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.TXT_AMDNO ='"+L_strPAMDNO +"'" +
					 				 " WHERE A.TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.TXT_SYSCD ='MM' and A.TXT_DOCTP ='POR' AND A.TXT_SBSCD = '"+M_strSBSCD +"'" +" AND A.TXT_SBSTP = '"+M_strSBSCD.substring(2,4) +"'";
					 	L_strSQLQRY += "AND A.TXT_DOCNO ='"+P_strPORNO +"'"+
					 	               " AND A.TXT_AMDNO ='"+	P_strAMDNO+"'"+
									  " AND A.TXT_PRDCD ='"+	L_strMATCD+"'";		
						//System.out.println(L_strSQLQRY);   
						L_rstTXSPC	= cl_dat.exeSQLQRY2(L_strSQLQRY);
					 	if(L_rstTXSPC!=null)
					 	{
					 		while(L_rstTXSPC.next())
					 		{
					 			L_strCURFL = nvlSTRVL(L_rstTXSPC.getString("L_CODFL1"),"");
					 			L_strPRVFL = nvlSTRVL(L_rstTXSPC.getString("L_CODFL2"),"");
					 			L_strCURVL = nvlSTRVL(L_rstTXSPC.getString("L_CODVL1"),"");
					 			L_strPRVVL = nvlSTRVL(L_rstTXSPC.getString("L_CODVL2"),"");
					 			L_strCURST = nvlSTRVL(L_rstTXSPC.getString("L_STSFL1"),"");
					 			L_strPRVST = nvlSTRVL(L_rstTXSPC.getString("L_STSFL2"),"");
								if((L_strPRVVL.equals(L_strCURVL))&&(L_strPRVFL.equals(L_strCURFL)))
							    {
									L_flgPRN = false;
								}
								else
								{
									L_flgPRN = true;
									L_intSRLNO++;
								}
								if(L_flgPRN)    // print flag is true
								{
									if(!L_flgHDG)	// Heading is not given
									{
										L_intSRLNO =0;
										L_intSRLNO++;
										dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended\n\n");
										intLINNO+=3;
										chkNEWPG();
										L_flgHDG = true;
									}
									if(hstTAXCD.containsKey((String)L_strTAXCD))
									{
										L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
										L_strMULT = L_STRTKN.nextToken();
										L_strTAXDS = L_STRTKN.nextToken();
									}
									else
										L_strTAXDS = L_strTAXCD;
										
									dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    "+L_strTAXDS,38));
									if(L_strPRVFL.equals("X"))
										L_strPRVVL	= "At Actuals";
									else if(L_strPRVFL.equals("I"))
										L_strPRVVL	= "Included";
									else if(L_strPRVFL.equals("P"))
										L_strPRVVL	= L_strPRVVL + " %";
									
									if(L_strCURFL.equals("X"))
										L_strCURVL	= "At Actuals";
									else if(L_strCURFL.equals("I"))
										L_strCURVL	= "Included";
									else if(L_strCURFL.equals("P"))
										L_strCURVL	= L_strCURVL + " %";
									if(L_strCURST.equals("X"))
									{
										if(!L_strPRVST.equals("X"))
											dosREPORT.writeBytes(padSTRING('L',L_strPRVVL,30)+padSTRING('L',"-",30));	
									}
									else
									{
										if(L_strPRVST.equals("X"))
											dosREPORT.writeBytes(padSTRING('L',"-",30)+padSTRING('L',L_strPRVVL,30));	
										else
										{
											dosREPORT.writeBytes(padSTRING('L',L_strPRVVL,30)+padSTRING('L',L_strCURVL,30));	
										}
									}
									//dosREPORT.writeBytes(padSTRING('L',L_strPRVVL,30)+padSTRING('L',L_strCURVL,30));	
									dosREPORT.writeBytes("\n");
									intLINNO++;
									chkNEWPG();
								}
					 		}
					 		L_rstTXSPC.close();
					 	}
						
					 }
				}
				else if(!L_strPSTSFL.equals("X"))
				{
					// Deletion 
					L_intSRLNO++;
					if(L_intSRLNO ==1)
					{
						dosREPORT.writeBytes("Change in ");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
						    prnFMTCHR(dosREPORT,M_strBOLD);
						 if(M_rdbHTML.isSelected())
				            dosREPORT.writeBytes("<b>");   
						dosREPORT.writeBytes(L_strINDNO +"/");
						dosREPORT.writeBytes(L_strMATCD);
						dosREPORT.writeBytes(" : "+L_strMATDS);
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&M_rdbTEXT.isSelected())
						    prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
				            dosREPORT.writeBytes("</b>");    
						dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended");
						intLINNO++;
						chkNEWPG();
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("\n");
						intLINNO +=2;
						chkNEWPG();
						flgITHDG = true;
					}
					dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    Quantity",38));	
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPPOQT,3),30)+padSTRING('L',"0.000",30));	
					dosREPORT.writeBytes("\n");
					intLINNO++;
					chkNEWPG();
				}
				
			}
		L_flgHDG = false; // added on 11/11/2004 as flag after itemwise tax should be false;
		boolean L_flgCCOTX = true,L_flgPCOTX = true;
	//	boolean L_flgCCSTX = true,L_flgPCSTX = true;  // Current common specific tax and previous
													  // Common specific Tax		
		M_strSQLQRY = "SELECT * FROM CO_TXDOC WHERE TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_SBSTP ='"+M_strSBSCD.substring(2,4) +"'"+
					" AND TX_SYSCD = 'MM' AND TX_DOCTP ='POR' AND TX_PRDCD = 'XXXXXXXXXX' and TX_DOCNO ='"+txtPORNO.getText().trim()+"'"+
					" AND isnull(TX_AMDNO,'00') = '"+P_strAMDNO +"'";
		//System.out.println(M_strSQLQRY);
		L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if(L_rstRSSET !=null)
		{
			if(L_rstRSSET.next())	
				L_flgCCOTX = true;
			else
				L_flgCCOTX = false;
		}
		else
			L_flgCCOTX = false;
		M_strSQLQRY = "SELECT * FROM CO_TXDAM WHERE TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_SBSTP ='"+M_strSBSCD.substring(2,4) +"'"+
					" AND TX_SYSCD = 'MM' AND TX_DOCTP ='POR' AND TX_PRDCD = 'XXXXXXXXXX' and TX_DOCNO ='"+txtPORNO.getText().trim()+"'"+
					" AND isnull(TX_AMDNO,'00') = '"+L_strPAMDNO +"'";
		//System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
		if(M_rstRSSET !=null)
		{
			if(M_rstRSSET.next())
				L_flgPCOTX = true;	
			else
				L_flgPCOTX = false;
		}
		else
			L_flgPCOTX = false;
		L_intSRLNO =0;
		if((L_flgCCOTX)||(L_flgPCOTX))
		for(int i=0;i<vtrTAXCD.size();i++)
		{
			L_strPRVDS ="";
			L_strCURDS ="";
			if(vtrTAXTP.elementAt(i).equals("02"))
			{
				continue;
			}
			L_flgPRN = false;
			L_strTAXCD = vtrTAXCD.elementAt(i).toString();
			if(!L_flgPCOTX)
			{
				L_strPRVVL ="";
				L_strPRVFL ="";
			}
			else
			{
				L_strPRVVL = nvlSTRVL(M_rstRSSET.getString("TX_"+L_strTAXCD+"VL"),"");
				L_strPRVFL = nvlSTRVL(M_rstRSSET.getString("TX_"+L_strTAXCD+"FL"),"");
				if((L_strTAXCD.equals("STX"))||(L_strTAXCD.equals("OTH")))
             		L_strPRVDS = nvlSTRVL(M_rstRSSET.getString("TX_"+L_strTAXCD+"DS"),"");
          	}
			if(!L_flgCCOTX)
			{
				L_strCURVL ="";
				L_strCURFL ="";
			}
			else
			{
				L_strCURVL = nvlSTRVL(L_rstRSSET.getString("TX_"+L_strTAXCD+"VL"),"");
				L_strCURFL = nvlSTRVL(L_rstRSSET.getString("TX_"+L_strTAXCD+"FL"),"");
				if((L_strTAXCD.equals("STX"))||(L_strTAXCD.equals("OTH")))
                {
					L_strCURDS = nvlSTRVL(L_rstRSSET.getString("TX_"+L_strTAXCD+"DS"),"");
                }
			}
			
			if((L_strTAXCD.equals("STX"))||(L_strTAXCD.equals("OTH"))&& L_flgCCOTX)
			{
				/// changed on 29/06/2005
				if(L_strCURFL.length() >0)  // Flag for this tax code is set
				    L_strTAXDS = L_strCURDS;
				else if(L_strPRVFL.length() >0)    
				    L_strTAXDS = L_strPRVDS;
			   else
			    {
			     	L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
					L_strMULT = L_STRTKN.nextToken();
					L_strTAXDS = L_STRTKN.nextToken();
			    }
				/*if(L_strTAXCD.equals("STX"))
					L_strTAXDS = nvlSTRVL(L_rstRSSET.getString("TX_"+L_strTAXCD+"DS"),"");
				else
					L_strTAXDS = nvlSTRVL(L_rstRSSET.getString("TX_"+L_strTAXCD+"DS"),"");*/
					
				/// end of changes on 29/06/2005
    		}
			if((L_strPRVVL.equals(L_strCURVL))&&(L_strPRVFL.equals(L_strCURFL)))
		    {
				L_flgPRN = false;
			}
			else
			{
				L_flgPRN = true;
				L_intSRLNO++;
			}
			if(L_flgPRN)    // print flag is true
			{
				if(!L_flgHDG)	// Heading is not given
				{
					crtLINE(106);
					dosREPORT.writeBytes("\nChanges for the completed Order");
					intLINNO++;
					chkNEWPG();
					dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended\n\n");
					intLINNO+=3;
					chkNEWPG();
					L_flgHDG = true;
				}
				if((!L_strTAXCD.equals("STX"))&&(!L_strTAXCD.equals("OTH")))
				{
					L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
					L_strMULT = L_STRTKN.nextToken();
					L_strTAXDS = L_STRTKN.nextToken();
				}
             	dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    "+L_strTAXDS,38));
				if(L_strPRVFL.equals("X"))
					L_strPRVVL	= "At Actuals";
				else if(L_strPRVFL.equals("I"))
					L_strPRVVL	= "Included";
				else if(L_strPRVFL.equals("P"))
					L_strPRVVL	= L_strPRVVL + " %";
				
				if(L_strCURFL.equals("X"))
					L_strCURVL	= "At Actuals";
				else if(L_strCURFL.equals("I"))
					L_strCURVL	= "Included";
				else if(L_strCURFL.equals("P"))
					L_strCURVL	= L_strCURVL + " %";
				dosREPORT.writeBytes(padSTRING('L',L_strPRVVL,30)+padSTRING('L',L_strCURVL,30));	
				dosREPORT.writeBytes("\n");
				intLINNO++;
				chkNEWPG();
			}
			else   // else part added on 22/03/2005 for printing change in description
			{
				if(!L_strPRVDS.equals(L_strCURDS))
				{
					L_intSRLNO++;
					if(!L_flgHDG)	// Heading is not given
					{
						crtLINE(106);
						dosREPORT.writeBytes("\nChanges for the completed Order");
						intLINNO++;
						chkNEWPG();
						dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended\n\n");
						intLINNO+=3;
						chkNEWPG();
						L_flgHDG = true;
					}
					dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    "+L_strTAXDS,38));
					dosREPORT.writeBytes(padSTRING('L',L_strPRVDS,30)+padSTRING('L',L_strCURDS,30));	
					dosREPORT.writeBytes("\n");
					intLINNO++;
					chkNEWPG();
				}
			}
		}
		////
		L_strCURST="";
		L_strPRVST ="";
		String L_strTEMP ="";
		L_strSQLQRY="";
		// Multiple amendments , amdno check
		L_strSQLQRY ="Select a.txt_codcd L_CODCD1,a.txt_codfl L_CODFL1,A.TXT_CODVL L_CODVL1,A.TXT_STSFL L_STSFL1,b.txt_codcd L_CODCD2,B.txt_codfL L_CODFL2,B.TXT_CODVL L_CODVL2,B.TXT_STSFL L_STSFL2 "+
					 "from CO_TXSPC A LEFT OUTER JOIN CO_TXSAM B ON A.TXT_CMPCD = B.TXT_CMPCD AND A.TXT_SYSCD = B.TXT_SYSCD AND A.TXT_SBSCD = B.TXT_SBSCD AND A.TXT_DOCTP = B.TXT_DOCTP AND "+
					 " A.TXT_DOCNO =B.TXT_DOCNO AND A.TXT_PRDCD = B.TXT_PRDCD and B.TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.TXT_AMDNO ='"+L_strPAMDNO +"'"+
					 " WHERE A.TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.TXT_SYSCD ='MM' and A.TXT_DOCTP ='POR' AND A.TXT_SBSCD = '"+M_strSBSCD +"'" +" AND A.TXT_SBSTP = '"+M_strSBSCD.substring(2,4) +"'";
		L_strSQLQRY += "AND A.TXT_DOCNO ='"+P_strPORNO +"' and a.txt_amdno ='"+P_strAMDNO +"' and A.TXT_PRDCD ='XXXXXXXXXX' ORDER BY a.TXT_CODCD";
		//System.out.println(L_strSQLQRY);
		L_rstTXSPC	= cl_dat.exeSQLQRY2(L_strSQLQRY);
		L_strPRVFL ="";
		if(L_rstTXSPC!=null)
		{
			while(L_rstTXSPC.next())
			{
				L_flgPRN = true;
				L_intSRLNO++;
				L_strTEMP = L_rstTXSPC.getString("L_CODCD1");
				L_strCURFL = L_rstTXSPC.getString("L_CODFL1");
				L_strCURVL = nvlSTRVL(L_rstTXSPC.getString("L_CODVL1"),"");
				L_strCURST = nvlSTRVL(L_rstTXSPC.getString("L_STSFL1"),"");
				L_strPRVST = nvlSTRVL(L_rstTXSPC.getString("L_STSFL2"),"");
				if(nvlSTRVL(L_rstTXSPC.getString("L_CODCD2"),"").length() >0)
				{
					// Code is present in both the cases
					L_strPRVFL = L_rstTXSPC.getString("L_CODFL2");
					L_strPRVVL = nvlSTRVL(L_rstTXSPC.getString("L_CODVL2"),"");
					//L_strPRVST = nvlSTRVL(L_rstTXSPC.getString("L_STSFL2"),"");
				}
				else
				{
					L_strPRVVL ="-";
					L_strPRVVL ="-";
					//L_strPRVST ="-";
				}
				if((L_strPRVVL.equals(L_strCURVL))&&(L_strPRVFL.equals(L_strCURFL)))
				{
					L_flgPRN = false;
				}
				else
				{
					L_flgPRN = true;
					L_intSRLNO++;
				}
				if(L_flgPRN)
				{
					if(!L_flgHDG)	// Heading is not given
					{
						crtLINE(106);
						dosREPORT.writeBytes("\nChanges for the completed Order");
						intLINNO++;
						chkNEWPG();
						dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended\n\n");
						intLINNO+=3;
						chkNEWPG();
						L_flgHDG = true;
					}
					L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTEMP).toString(),"|");
					L_strMULT = L_STRTKN.nextToken();
					L_strTAXDS = L_STRTKN.nextToken();
					dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    "+L_strTAXDS,38));
					if(L_strPRVFL.equals("X"))
						L_strPRVVL	= "At Actuals";
					else if(L_strPRVFL.equals("I"))
						L_strPRVVL	= "Included";
					else if(L_strPRVFL.equals("P"))
						L_strPRVVL	= L_strPRVVL + " %";
					if(L_strCURFL.equals("X"))
						L_strCURVL	= "At Actuals";
					else if(L_strCURFL.equals("I"))
						L_strCURVL	= "Included";
					else if(L_strCURFL.equals("P"))
						L_strCURVL	= L_strCURVL + " %";
					if(L_strCURST.equals("X"))
					{
						if(!L_strPRVST.equals("X"))
							dosREPORT.writeBytes(padSTRING('L',L_strPRVVL,30)+padSTRING('L',"-",30));	
					}
					else
					{
						if(L_strPRVST.equals("X"))
							dosREPORT.writeBytes(padSTRING('L',"-",30)+padSTRING('L',L_strPRVVL,30));	
						else
						{
							dosREPORT.writeBytes(padSTRING('L',L_strPRVVL,30)+padSTRING('L',L_strCURVL,30));	
						}
					}
				
					dosREPORT.writeBytes("\n");
					intLINNO++;
					chkNEWPG();
				}
			}
			L_rstTXSPC.close();
		}

		////
		//if(((L_strDSTC1.length() >0)&&(L_strDSTC2.length() >0))||((L_strDSTC1.length() >0)&&(L_strDSTC2.length() >0)))
	//	if((L_strDSTC1.length() >0)&&(L_strDSTC2.length() >0))
		if((L_strDSTC1 !=null)&&(L_strDSTC2 !=null) &&(L_strPMTR1 !=null)&&(L_strPMTR2 !=null)&&(L_strCRDD1 !=null)&&(L_strCRDD2 !=null))
	    if((!L_strDSTC1.equals(L_strDSTC2))||(!L_strPMTR1.equals(L_strPMTR2))||(!L_strCRDD1.equals(L_strCRDD2)))
	    {
			if(!L_flgHDG)	// Heading is not given
			{
				crtLINE(106);
				dosREPORT.writeBytes("\nChanges for the completed Order");
				intLINNO ++;
				chkNEWPG();
				dosREPORT.writeBytes("\nS.No. Particulars                                            Previous                       Amended\n\n");
				intLINNO+=3;
				chkNEWPG();
				L_flgHDG = true;
			}
			if(!L_strDSTC1.equals(L_strDSTC2))  // Destination Code not matching
			{
				L_intSRLNO++;
				dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    Delivery At",38));	
		
				if(L_strDSTC1.equals("NGT"))
					dosREPORT.writeBytes(padSTRING('L',"SPL PLANT",30));
				else if(L_strDSTC1.equals("ADR"))	
					dosREPORT.writeBytes(padSTRING('L',"SPL ANDHERI",30));
				
				if(L_strDSTC2.equals("NGT"))
					dosREPORT.writeBytes(padSTRING('L',"SPL PLANT",30));
				else if(L_strDSTC2.equals("ADR"))	
					dosREPORT.writeBytes(padSTRING('L',"SPL ANDHERI",30));
				
				
				dosREPORT.writeBytes("\n");
				intLINNO++;
				chkNEWPG();
		
		
			}
			if((!L_strPMTR1.equals(L_strPMTR2))||(!L_strCRDD1.equals(L_strCRDD2)))  // Destination Code not matching
			{
				L_intSRLNO++;
				dosREPORT.writeBytes(String.valueOf(L_intSRLNO) +padSTRING('R',".    Payment Terms",38));	
				dosREPORT.writeBytes(padSTRING('L',L_strPMTR2+(L_strCRDD2),30)+padSTRING('L',L_strPMTR1+(L_strCRDD1),30));
				dosREPORT.writeBytes("\n");
				intLINNO++;
				chkNEWPG();
			}
		}
		crtLINE(106);
		dosREPORT.writeBytes ("\n");
		intLINNO++;chkNEWPG();
		prnFOOTR(txtPORNO.getText().trim(),txtAMDNO.getText().trim());
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"prnPOAMD");
	}
 }
}
