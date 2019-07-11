/*
System Name   : Marketing Management System
Program Name  : Despatch Details report 
Program Desc. : This program generates Report for Tentitive/Delay  Despatch Details 
Author        : 
Date          :27/05/2006
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :15/06/2006
Modified det.  :
Version        :
*/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.Date;
import java.sql.SQLException;
import java.io.*;
import java.awt.Color;
import java.sql.ResultSet;
//import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;

/**<pre>
System Name : Material Management System.
 
Program Name : Despatch Details report
Purpose : This program generates Report for Tentitive/Delay  Despatch Details .

List of tables used :
Table Name              Primary key                                             Operation done
                                                                   Insert   Update   Query   Delete	
---------------------------------------------------------------------------------------------------
MR_INMST			   IN_MKTTP,IN_INDNO												#
MR_DODEL               DOD_MKTTP,DOD_DORNO,DOD_PRDCD,DOD_PKGTP,DOD_SRLNO    			#
MR_DOTRN			   DOT_MKTTP,DOT_DORNO,DOT_PRDCD,DOT_PKGTP							#
---------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------

*/

class mr_rpdsp extends cl_rbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;	//JPanel For Tentitive Despatch REport
	private JPanel pnlRPTRPT;       //JPanel For Delay  Despatch REport
	private JPanel pnlDELAY;		
	private String strSPCCT,strPRDCD,strPRTTP,strPRTCD,strWHRSTR,strORDBY;	//strSTRDT,strENDDT,
	private String strZONSTR, strSALSTR, strSBSSTR;
	private String strRESSTR ; // Report File name 
	private JTextField txtSPCCT,txtDELTP;
									//JPanel For Tentitive  Despatch REport
	private cl_JTable tblRPTRPT;    //JPanel For Delay  Despatch REport
	private cl_JTable tblDELAY;

	private JLabel lblSPCCT, lblSPCDS,lblRPTTP,lblDELTP,lblDELDS;
	
	private String strCGMTP;
	private String strCGSTP;
	private String strCODCD;

	private int intCDTRN_TOT = 9;
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;

	private int intPRMST_TOT = 3;
	private int intRECCT=0;
  //  private int intAE_PR_PRDCD = 0;
    private int intAE_PR_PRDDS = 1;
    private int intAE_PR_AVGRT = 2;

	private int intPTMST_TOT = 6;
    private int intAE_PT_PRTNM = 0;
    private int intAE_PT_ZONCD = 1;
    private int intAE_PT_SHRNM = 2;
    private int intAE_PT_ADR01 = 3;
    private int intAE_PT_ADR02 = 4;		
    private int intAE_PT_ADR03 = 5;		
	
	private String strALLWS = "Overall";		
	private String strPRDWS = "Grade";	
	private String strPKGWS = "Pkg.Type";	
	private String strPCTWS = "Prod.Category";	
	private String strSALWS = "Sale Type";	
	
	private String strDFTSQ = "Default";		
	private String strPRDSQ = "Grade";		
	private String strPKGSQ = "Pkg.Type";		
	private String strBYRSQ = "Buyer";		
	private String strTRPSQ = "Transporter";		
	private String strDDTSQ = "Desp.Date";		
	
	private String strTENDS="Tentitive Despatch Details";
	private String strDLYDS="Delay Despatch Details";
	private JComboBox cmbPRINTERS;
	
	private String strSHDRNM = "", strSHDRDS = "";
	
	private int intPAGENO=0;
	private int intRPTWD = 178;      // report width
	private double dblORDQT = 0.000, dblORDTOT = 0.000, dblGRNTOT;
	private String strORDFLD1 = "", strORDFLD2 = "",strDELTP;
	private int intORDCT = 0;
	private int intBRKCN=-1;
	private int L_intDSRCT=0;

	String strORDVAL1, strORDVAL2, strORDVAL1_OLD, strORDVAL2_OLD;
	
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ; 
	
	private JLabel lblRPTWS, lblRPTSQ,lblREGON;		
	private Vector<String> vtrRPTWS,vtrRPTSQ,vtrRPTTP;		/**Vector for adding elements to cmbRPTWS, cmbRPTSQ */
	private JComboBox cmbRPTWS,cmbRPTSQ;		/**Combo-box for defining scope & order of the report */

	private JLabel lblMKTTP;		
	private Vector<String> vtrMKTTP;		/**Vector for adding elements to cmbMKTTP */
	private JComboBox cmbMKTTP,cmbRPTTP;
	private JComboBox cmbREGON;

	private Hashtable<String,String[]> hstCDTRN;			// Code Transaction details
	private Hashtable<String,String> hstCODDS;			// Code Description
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstPTMST;			// Party Details
	private Hashtable<String,String> hstREGON;

	mr_rpdsp()
	{
		super(2);
		try
		{
			
			pnlRPTRPT = new JPanel(null);
			pnlDELAY  = new JPanel(null);
			txtSPCCT  = new JTextField();
			txtDELTP  = new JTextField();
			hstCDTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstPRMST = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			hstREGON =new Hashtable<String,String>();
						
			vtrRPTWS = new Vector<String>();
			vtrRPTSQ = new Vector<String>();
			vtrMKTTP = new Vector<String>();
			vtrRPTTP = new Vector<String>();
			
			lblRPTWS  = new JLabel("Scope");
			lblRPTSQ  = new JLabel("Order");
			lblMKTTP  = new JLabel("Market Type");
			lblRPTTP  = new JLabel("Report type");
			lblREGON  = new JLabel("Region");

			lblRPTWS.setForeground(Color.blue);
			lblRPTSQ.setForeground(Color.blue);
			lblMKTTP.setForeground(Color.blue);
			lblRPTTP.setForeground(Color.blue);

			lblSPCCT  = new JLabel("Specify");
			lblSPCDS  = new JLabel("");
			lblDELTP  = new JLabel("Del Type");
			lblDELDS  = new JLabel("");
			lblSPCCT.setForeground(Color.blue);
			lblSPCDS.setForeground(Color.blue);
			lblREGON.setForeground(Color.blue);
			lblDELTP.setForeground(Color.blue);
			lblDELDS.setForeground(Color.blue);
			
			String L_strTEMP="";
			String L_strNXTTEMP="";
			String L_strLSTTEMP="";
			String L_strTEMP2="";
			String L_strTEMP3="";
			String L_strRGNNM="";
			int intCOUNT=0;
			crtPRMST();
			setMatrix(20,8);

			add(lblRPTTP,3,1,1,2,this,'L');
			add(cmbRPTTP=new JComboBox(),3,2,1,2,this,'L');
						
			add(lblRPTWS,3,4,1,1,this,'L');
			add(cmbRPTWS=new JComboBox(),3,5,1,2,this,'L');
			
			add(lblMKTTP,4,1,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),4,2,1,2,this,'L');
			
			add(lblRPTSQ,4,4,1,1,this,'L');
			add(cmbRPTSQ=new JComboBox(),4,5,1,2,this,'L');
			
			add(lblSPCCT,6,1,1,1,this,'L');
			add(txtSPCCT,6,2,1,1,this,'L');
			add(lblSPCDS,6,3,1,3,this,'L');
			
			add(lblREGON,5,4,1,1,this,'L');
			add(cmbREGON=new JComboBox(),5,5,1,2,this,'L');
			
			add(lblDELTP,6,4,1,1,this,'L');
			add(lblDELDS,6,6,1,2,this,'L');
			add(txtDELTP,6,5,1,1,this,'L');

			txtSPCCT.setText("");
			txtSPCCT.setVisible(false);
			lblSPCCT.setText("");
			lblSPCDS.setText("");
			
			lblDELTP.setText("");
			lblDELDS.setText("");
			txtDELTP.setVisible(false);
			lblREGON.setText("");
			cmbREGON.setVisible(false);
			
			
			M_strSQLQRY="SELECT (A.CMT_CHP02)RGNCD ,(A.CMT_CODCD) ZON,(B.CMT_CODDS)RGNNM,(A.CMT_CODDS)ZONNM FROM CO_CDTRN A, CO_CDTRN B WHERE A.CMT_CGMTP ='SYS' AND A.CMT_CGSTP ='MR00ZON' AND B.CMT_CGMTP ='SYS' AND B.CMT_CGSTP ='MRXXRGN' AND A.CMT_CHP02 = B.CMT_CODCD ORDER BY B.CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				cmbREGON.addItem("Over All");
				while(M_rstRSSET.next())
				{
					L_strNXTTEMP  =  nvlSTRVL(M_rstRSSET.getString("RGNCD"),"");
					if(!L_strNXTTEMP.equals(L_strLSTTEMP))
					{
						L_strTEMP2= nvlSTRVL(M_rstRSSET.getString("ZON"),"  ");
						L_strTEMP=L_strNXTTEMP;
						L_strTEMP +=  "   ";
				    	L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("RGNNM"),"  ");
				    	cmbREGON.addItem(L_strTEMP);	
					}
					else
					{
						
						L_strTEMP2= L_strTEMP2 +"','"+nvlSTRVL(M_rstRSSET.getString("ZON"),"  ");
					}
					if(intCOUNT>0)
					{
						if(!L_strNXTTEMP.equals(L_strLSTTEMP))
						{
	
							hstREGON.put(L_strLSTTEMP,L_strTEMP3);
						}
						
					}
					intCOUNT++;
						
					L_strLSTTEMP=L_strNXTTEMP;	
					L_strTEMP3=L_strTEMP2;
				}
				hstREGON.put(L_strLSTTEMP,L_strTEMP3);
				
			}
				   
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			
			
			hstCDTRN.clear();
			crtCDTRN("'MSTCOXXCUR','STSMRXXIND','SYSMRXXDTP', 'SYSMR00SAL', 'SYSMRXXPMT','SYSMR01MOT','SYSMR00EUS','SYSCOXXTAX','SYSCOXXDST','SYSFGXXPKG','SYSCOXXAMT'","",hstCDTRN);
			
			M_txtFMDAT.setText("01/04/20"+cl_dat.M_strFNNYR1_pbst.substring(0,2));
			setVTRRPTWS();
			setVTRRPTSQ();
			setVTRMKTTP();
			setVTRRPTTP();
			setCMBVL(cmbRPTWS,vtrRPTWS);
			setCMBVL(cmbRPTSQ,vtrRPTSQ);
			setCMBVL(cmbMKTTP,vtrMKTTP);
			setCMBVL(cmbRPTTP,vtrRPTTP);
			
			INPVF objFGIPV=new INPVF();
			//REGISTERING INPUTVERIFIER for COMPONENTS OTHER THAN JLABEL
			//ADDING ITEM LISTENER TO COMBO's TO DISABLE USAGE WHEN NOT HAVING FOCUS ON IT
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objFGIPV);
					//if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
					//	((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
				}
			}
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
			updateUI();
			
			
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"mr_qrtds");}
	}

	/** Adding elements to vtrRPTSQ, for defining Scope of the report
	 * 
	*/
	void setVTRRPTSQ()
	{
		try
		{
			vtrRPTSQ.clear();
			vtrRPTSQ.addElement(strDFTSQ);		
			vtrRPTSQ.addElement(strPRDSQ);		
			vtrRPTSQ.addElement(strPKGSQ);		
			vtrRPTSQ.addElement(strBYRSQ);		
			vtrRPTSQ.addElement(strTRPSQ);		
			vtrRPTSQ.addElement(strDDTSQ);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRRPTSQ");}
	}
	
	
	/** Adding elements to vtrRPTSQ, for defining Type of the report
	 * 
	*/
	void setVTRRPTTP()
	{
		try
		{
			vtrRPTTP.clear();
			vtrRPTTP.addElement(strTENDS);		
			vtrRPTTP.addElement(strDLYDS);		
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setVTRRPTTP");
		}
	}
	
	/** Adding elements to vtrRPTWS, for defining Scope of the report
	 * 
	*/
	void setVTRRPTWS()
	{
		try
		{
			vtrRPTWS.clear();
			vtrRPTWS.addElement(strALLWS);		
			vtrRPTWS.addElement(strPRDWS);		
			vtrRPTWS.addElement(strPKGWS);		
			vtrRPTWS.addElement(strPCTWS);		
			vtrRPTWS.addElement(strSALWS);		
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setVTRRPTWS");
		}
	}

	/** Adding elements to vtrMKTTP, for defining Scope of the report
	*/
	void setVTRMKTTP()
	{
		try
		{
			vtrMKTTP.clear();
			vtrMKTTP.addElement("01 Polystyrene");		
			vtrMKTTP.addElement("02 Styrene");		
			vtrMKTTP.addElement("03 Captive Consumption");		
			vtrMKTTP.addElement("04 Wood Profile");	
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setVTRMKTTP");
		}
	}
	
	/** Initializing components before accepting data
	 * 
	*/
	void clrCOMP()
	{
		super.clrCOMP();
		try
		{
			M_txtFMDAT.setText("01/04/20"+cl_dat.M_strFNNYR1_pbst.substring(0,2));
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			M_calLOCAL.add(java.util.Calendar.DATE,-1);
			M_txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
		}
		catch(Exception E)
		{
		}
		
	}
	/**
	 * 
	*/
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				if( cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{
					setENBL(true);
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))||(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst)))
							M_cmbDESTN.requestFocus();
					else
						M_txtFMDAT.requestFocus();
				}
				else
				{
					txtDELTP.setVisible(false);
					lblDELTP.setText("");
				}
			}
			if(M_objSOURC==M_txtFMDAT)
			{
				if(M_txtFMDAT.getText().length()<10)
					M_txtFMDAT.setText("01/04/20"+cl_dat.M_strFNNYR1_pbst.substring(0,2));
			}
			if(M_txtTODAT.getText().length()<10)
				M_txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
			if(M_objSOURC == cmbRPTWS)
			{
				setCMBVL(cmbMKTTP,vtrMKTTP);
				getSUBHDR();
			}
			
			if(M_objSOURC ==cmbRPTTP)
			{
				if(cmbRPTTP.getSelectedItem().toString().equals(strDLYDS))
				{
					cmbRPTSQ.setSelectedIndex(0);
					cmbRPTSQ.setEnabled(false);
					lblDELTP.setText("Del Type");
					txtDELTP.setVisible(true);
					lblREGON.setText("Region");
					cmbREGON.setVisible(true);
				}
				else
				{
					cmbRPTSQ.setEnabled(true);
					txtDELTP.setText("");
					lblREGON.setText("");
					txtDELTP.setVisible(false);
					cmbREGON.setVisible(false);
					
					
					lblDELTP.setText("");
					lblDELDS.setText("");
					lblREGON.setText("");
				}
					
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"acionPerformed");
		}
	}
	/**
	 * 
	*/
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			M_objSOURC=L_KE.getSource();
			
			
			if(M_objSOURC==txtSPCCT && L_KE.getKeyCode()==L_KE.VK_ENTER)
				getSUBHDR();
			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
				{
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)))
							M_cmbDESTN.requestFocus();
					else
						M_txtFMDAT.requestFocus();
				}
				if(M_objSOURC==M_cmbDESTN)
				{
					M_txtFMDAT.requestFocus();
				}
				if(M_objSOURC == M_txtFMDAT)
				{
					M_txtTODAT.requestFocus();
				}
				if(M_objSOURC == M_txtTODAT)
				{
					cmbRPTTP.requestFocus();
				}
				if(M_objSOURC == cmbRPTTP)
				{
					cmbMKTTP.requestFocus();
				}
				if(M_objSOURC == cmbMKTTP)
				{
					cmbRPTWS.requestFocus();
				}
				
				if(M_objSOURC == cmbRPTWS)
				{
					if(cmbRPTTP.getSelectedItem().toString().equals(strDLYDS))
						cl_dat.M_btnSAVE_pbst.requestFocus();
				
					else
						cmbRPTSQ.requestFocus();
				}
				if(M_objSOURC == cmbRPTSQ)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				if(M_objSOURC == txtDELTP)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
			
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC==txtSPCCT)
				{
					M_strHLPFLD = "txtSPCCT";
					if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
						cl_hlp("Select PR_PRDCD, PR_PRDDS from CO_PRMST where SUBSTRING(pr_prdcd,1,2) in ('51','52','53') and pr_prdcd in (select distinct dod_prdcd from mr_dodel where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PR_PRDCD" ,2,1,new String[] {"Code","Description"},2,"CT");
					if(cmbRPTWS.getSelectedItem().toString().equals(strPKGWS))
						cl_hlp("Select CMT_CODCD, CMT_CODDS  from CO_CDTRN where cmt_cgmtp='SYS'  and cmt_cgstp='FGXXPKG' and cmt_codcd in (select dod_pkgtp from mr_dodel where  "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Name"},2,"CT");
					if(cmbRPTWS.getSelectedItem().toString().equals(strPCTWS))
						cl_hlp("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and SUBSTRING(cmt_codcd,1,4) in (select distinct SUBSTRING(dod_prdcd,1,4) dod_prdcd from mr_dodel where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
					if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
						cl_hlp("Select CMT_CODCD, CMT_CODDS  from CO_CDTRN where cmt_cgmtp='SYS'  and cmt_cgstp='MR00SAL' order by CMT_CODCD" ,2,1,new String[] {"Code","Name"},2,"CT");
				}
				if(M_objSOURC==txtDELTP)
				{
					M_strHLPFLD = "txtDELTP";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MRXXDTP' AND isnull(CMT_STSFL,'')<> 'X' "  ;
				
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Description"},2,"CT");
				}
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}

	/**
	 * 
	*/
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtSPCCT"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtSPCCT"))
			{
				txtSPCCT.setText(L_STRTKN.nextToken());
				lblSPCDS.setText(L_STRTKN.nextToken());
			}
		}
		if(M_strHLPFLD.equals("txtDELTP"))
		{
			StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
			if(M_strHLPFLD.equals("txtDELTP"))
			{
				txtDELTP.setText(L_STRTKN1.nextToken());
				strDELTP=L_STRTKN1.nextToken();
				
				lblDELDS.setText(strDELTP);
			}
		}
	}
	
	/**
	 *   Displaying Report on Screen
	*/
	private void dspRPTDTL()
	{
		try
		{
			intORDCT =0;
			ResultSet rstRSSET = null;
			setORDBY();
			intPAGENO =0;
			M_strSQLQRY = "select dot_dorno,dot_dordt,in_byrcd,in_byrnm,dot_trpcd,dod_dorno,dod_dspdt,dod_prdcd,dod_pkgtp,isnull(dod_dorqt,0)-isnull(dod_ladqt,0) dod_dspqt from mr_dodel,mr_inmst,mr_dotrn  where "+strWHRSTR+ " and dot_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and dot_SBSCD1 in "+M_strSBSLS+"  and in_cmpcd=dot_cmpcd and in_mkttp=dot_mkttp and in_indno=dot_indno and dot_cmpcd=dod_cmpcd and dot_mkttp=dod_mkttp and dot_dorno=dod_dorno and dot_prdcd=dod_prdcd and dot_pkgtp=dod_pkgtp "+strORDBY;
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			int i;
			i=0;
		
			cl_dat.M_intLINNO_pbst=0;	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strCPI17);			    
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			dblORDTOT = 0.000;
			dblGRNTOT = 0.000;
			strORDVAL1_OLD = getRSTVAL(rstRSSET,strORDFLD1,"C");
			strORDVAL2_OLD = (strORDFLD2.length()>0  ? getRSTVAL(rstRSSET,strORDFLD2,"C") : "");
			prnHEADER(0);
			while (true)
			{
				if(cl_dat.M_intLINNO_pbst >60)
				{
					dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO +=1;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER(0);
				}
				intRECCT++;
				i = chkTOTPRN(i, rstRSSET, false,0);
				dblORDQT = Double.parseDouble(getRSTVAL(rstRSSET,"DOD_DSPQT","N"));
				dblORDTOT += dblORDQT;
				dblGRNTOT += dblORDQT;
				intORDCT +=1;
				
				String L_strPRDCD = getRSTVAL(rstRSSET,"DOD_PRDCD","C");
				String L_strPKGTP = getRSTVAL(rstRSSET,"DOD_PKGTP","C");
				String L_strDSPDT = getRSTVAL(rstRSSET,"DOD_DSPDT","D");				
				dosREPORT.writeBytes(padSTRING('R',getPRMST(L_strPRDCD,"PR_PRDDS"),30));
				dosREPORT.writeBytes(padSTRING('R',getCDTRN("SYSFGXXPKG"+L_strPKGTP,"CMT_SHRDS",hstCDTRN),11));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblORDQT,3),11));
				dosREPORT.writeBytes(padSTRING('L',L_strDSPDT,14));	
				dosREPORT.writeBytes("     ");
				dosREPORT.writeBytes(padSTRING('R',getPTMST("T",rstRSSET.getString("DOT_TRPCD"),"PT_PRTNM"),40));
				
				dosREPORT.writeBytes(padSTRING('R',rstRSSET.getString("DOT_DORNO"),14));
				dosREPORT.writeBytes(padSTRING('R',getRSTVAL(rstRSSET,"DOT_DORDT","D"),14));
				dosREPORT.writeBytes(padSTRING('R',rstRSSET.getString("IN_BYRNM"),40));
										
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst+=1;		
							
				i += 1;
				if(!rstRSSET.next())
					break;
			}
			i = chkTOTPRN(intBRKCN, rstRSSET, true,0);
			
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspRPTDTL");}
	}
	
	private void dspDLYDTL()
	{
		String L_strDSBCD="",L_strBYRCD="",L_strDSBCD1="",L_strBYRCD1="";
		cl_dat.M_intLINNO_pbst=0;
		
		intPAGENO =0;
		try
		{
			intORDCT =0;
			ResultSet rstRSSET = null;
			setORDBY();
			M_strSQLQRY = "select dot_dorno,dot_dordt,dot_indno,in_byrcd,in_dsrcd,in_dsrnm,in_byrnm,";
			M_strSQLQRY += "in_dtpcd,in_dtpds,dot_trpcd,dod_dorno,dod_dspdt,(days(current_date)-days(dod_dspdt))dod_pendy,";
			M_strSQLQRY += "dod_prdcd,dod_pkgtp,isnull(dod_dorqt,0)-isnull(dod_ladqt,0) dod_dspqt from mr_dodel,mr_inmst,mr_dotrn ";
			M_strSQLQRY += " where "+strWHRSTR+" and dot_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and  DOT_SBSCD1 in "+M_strSBSLS+" " ;
			M_strSQLQRY += " and in_cmpcd=dot_cmpcd and in_mkttp=dot_mkttp and in_indno=dot_indno ";
			M_strSQLQRY += " and dot_cmpcd = dod_cmpcd and dot_mkttp=dod_mkttp and dot_dorno=dod_dorno and dot_prdcd=dod_prdcd and dot_pkgtp=dod_pkgtp ";
			if(txtDELTP.getText().trim().length()>0)
			{
				M_strSQLQRY+= " and in_dtpcd = '"+txtDELTP.getText().trim()+"' ";
			}
			if(!cmbREGON.getSelectedItem().equals("Over All"))
			{
				String L_strZONCD=(cmbREGON.getSelectedItem().toString()).substring(0,2);
				M_strSQLQRY += " And IN_ZONCD in ('"+(String)(hstREGON.get(L_strZONCD))+"')";
			}
				
			
			M_strSQLQRY+=strORDBY;
			
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			int i;
			i=0;
			dblORDTOT = 0.000;
			dblGRNTOT = 0.000;
			strORDVAL1_OLD = getRSTVAL(rstRSSET,strORDFLD1,"C");
			strORDVAL2_OLD = (strORDFLD2.length()>0  ? getRSTVAL(rstRSSET,strORDFLD2,"C") : "");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			 	prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			prnHEADER(1);
			while (true)
			{
				i = chkTOTPRN(i, rstRSSET, false,1);
				intRECCT++;
				dblORDQT = Double.parseDouble(getRSTVAL(rstRSSET,"DOD_DSPQT","N"));
				dblORDTOT += dblORDQT;
				dblGRNTOT += dblORDQT;
				intORDCT +=1;
				
				String L_strPRDCD = getRSTVAL(rstRSSET,"DOD_PRDCD","C");
				String L_strDSPDT = getRSTVAL(rstRSSET,"DOD_DSPDT","D");
				
				L_strDSBCD=	rstRSSET.getString("in_dsrcd");
				L_strBYRCD= rstRSSET.getString("in_byrcd");
				if(!(L_strDSBCD1.equals(L_strDSBCD)))
				{
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(rstRSSET.getString("in_dsrnm"),"  "),55));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst+=2;
					L_intDSRCT=1;
				}
				if(!(L_strBYRCD1.equals(L_strBYRCD)))
				{
					if(L_intDSRCT==1)
					{}
					else
					{	dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst+=1;
						L_intDSRCT=0;
					}
					dosREPORT.writeBytes("     ");
					dosREPORT.writeBytes(padSTRING('R',rstRSSET.getString("in_byrnm"),55));	
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst+=1;
				}
				
				dosREPORT.writeBytes("              ");
				dosREPORT.writeBytes("\t"+padSTRING('R',rstRSSET.getString("DOT_INDNO"),11)+"\t");
				dosREPORT.writeBytes(padSTRING('R',getPRMST(L_strPRDCD,"PR_PRDDS"),11)+"\t");
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblORDQT,3),10)+"\t");
				
				dosREPORT.writeBytes(padSTRING('R',rstRSSET.getString("IN_DTPDS"),14)+"\t");
				dosREPORT.writeBytes(padSTRING('R',rstRSSET.getString("DOT_DORNO"),10)+"\t");
				dosREPORT.writeBytes(padSTRING('R',L_strDSPDT,11)+"\t");
				
				dosREPORT.writeBytes(padSTRING('R',rstRSSET.getString("dod_pendy"),7)+"\t");
				dosREPORT.writeBytes(padSTRING('R',getPTMST("T",rstRSSET.getString("DOT_TRPCD"),"PT_PRTNM"),20)+"\t");
				L_strDSBCD1=L_strDSBCD;
				L_strBYRCD1=L_strBYRCD;
				
				dosREPORT.writeBytes("\n");
				i += 1;
				cl_dat.M_intLINNO_pbst++;
				if(!rstRSSET.next())
					break;
			}
			
			i = chkTOTPRN(intBRKCN, rstRSSET, true,1);
		}
		catch (Exception L_EX) 
		{
			setMSG(L_EX,"dspDLYDTL");
		}
	}
	/**
	 *  Printing Category Total
	*/
	private int chkTOTPRN(int LP_LINCT, ResultSet LP_RSSET, boolean LP_ENDFL,int P_intCOND)
	{
		try
		{
			if(LP_LINCT!=intBRKCN)
			{
				strORDVAL1 = getRSTVAL(LP_RSSET,strORDFLD1,"C");
				strORDVAL2 = (strORDFLD2.length()>0  ? getRSTVAL(LP_RSSET,strORDFLD2,"C") : "");
				if(!(strORDVAL1+strORDVAL2).equals(strORDVAL1_OLD+strORDVAL2_OLD))
				{
					strORDVAL1_OLD = getRSTVAL(LP_RSSET,strORDFLD1,"C");
					strORDVAL2_OLD = (strORDFLD2.length()>0  ? getRSTVAL(LP_RSSET,strORDFLD2,"C") : "");
					if(P_intCOND==0)
					{
						if(intORDCT>1)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblORDTOT,3),52));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst+=2;
							LP_LINCT += 1;
						}
						LP_LINCT += 1;
						dblORDTOT = 0.000; 
						intORDCT = 0;
					}
					else if(P_intCOND==1)
					{
						if(intORDCT>1)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("\t\t\t"+padSTRING('L',setNumberFormat(dblORDTOT,3),46));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst+=2;
							LP_LINCT += 1;
							L_intDSRCT=0;
						}
						LP_LINCT += 1;
						dblORDTOT = 0.000; 
						intORDCT = 0;
					}
				}
			}
			else
			{
				if(P_intCOND==0)
				{
					if(LP_ENDFL)
					{
						dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
						dosREPORT.writeBytes(padSTRING('L',"Grand Total:",40));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblGRNTOT,3),12));
						dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
					}
				}
				else if(P_intCOND==1)
				{
					if(LP_ENDFL)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("\t Grand Total: \t\t");
						dosREPORT.writeBytes(setNumberFormat(dblGRNTOT,3));
					}
				}
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"chkTOTPRN");
		}
		return LP_LINCT;
	}
	public boolean vldDATA()
	{
		try
		{
			if(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))>0)
			{
				setMSG("To Date can not be Less than From Date ..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}	
			if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	/**
	 * 
	*/
	public void exePRINT()
	{
		intRECCT=0;
		try
		{
			if(!vldDATA())
			   return;
			
			if(M_rdbHTML.isSelected())
		        strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_rpdsp.html";
		    else if(M_rdbTEXT.isSelected())
		        strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_rpdsp.xls"; 
			
			getDATA();
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
					doPRINT(strRESSTR);
				else 
		        {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strRESSTR); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
					Runtime r = Runtime.getRuntime();
				    Process p = null;
				    if(M_rdbHTML.isSelected())
						p  = r.exec("C:\\windows\\iexplore.exe " + strRESSTR); 
					 else
    					//p  = r.exec("C:\\windows\\excel.exe "+ strRESSTR); 
    					p  = r.exec("D:\\Program Files (x86)\\Microsoft Office\\Office12\\excel.exe "+ strRESSTR); 
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRESSTR,"Despatch Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}		
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
		finally
		{
			this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	private void getDATA()
	{
		try
		{
			fosREPORT = new FileOutputStream(strRESSTR);
			dosREPORT = new DataOutputStream(fosREPORT);
			this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
			strSBSSTR = setSBSSTR("SBS");
			getSUBHDR();
			crtPTMST();
					
			if(cmbRPTTP.getSelectedItem().toString().equals(strTENDS))
			{
				dspRPTDTL();
			}
			if(cmbRPTTP.getSelectedItem().toString().equals(strDLYDS))
			{
				dspDLYDTL();					
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			   	prnFMTCHR(dosREPORT,M_strCPI12);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}
			dosREPORT.close();
			fosREPORT.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	
	/**
	 * 
	*/
	private void setORDBY()
	{
		try
		{
			strORDBY = "";
			if(cmbRPTTP.getSelectedItem().toString().equals(strTENDS))
			{
				if(cmbRPTSQ.getSelectedItem().toString().equals(strDFTSQ))
				{
					strORDBY = " order by dod_dspdt desc,dod_prdcd asc";
					strORDFLD1 = "dod_dspdt";
					strORDFLD2 = "dod_prdcd";
				}
				else if(cmbRPTSQ.getSelectedItem().toString().equals(strPRDSQ))
				{
					strORDBY = " order by  dod_prdcd,dod_pkgtp ";
					strORDFLD1 = "dod_prdcd";
					strORDFLD2 = "dod_pkgtp";
				}
				else if(cmbRPTSQ.getSelectedItem().toString().equals(strBYRSQ))
				{
					strORDBY = " order by  in_byrcd "; 
					strORDFLD1 = "in_byrcd";
					strORDFLD2 = "";
				}
				else if(cmbRPTSQ.getSelectedItem().toString().equals(strPKGSQ))
				{
					strORDBY = " order by  dod_pkgtp ";
					strORDFLD1 = "dod_pkgtp"; 
					strORDFLD2 = "";
				}
				else if(cmbRPTSQ.getSelectedItem().toString().equals(strTRPSQ))
				{
					strORDBY = " order by  dot_trpcd ";
					strORDFLD1 = "dot_trpcd";
					strORDFLD2 = "";
				}
				else if(cmbRPTSQ.getSelectedItem().toString().equals(strDDTSQ))
				{
					strORDBY = " order by  dod_dspdt,dot_trpcd ";
					strORDFLD1 = "dod_dspdt";
					strORDFLD2 = "dot_trpcd";
				}			
			}
			if(cmbRPTTP.getSelectedItem().toString().equals(strDLYDS))
			{
				strORDBY = " order by  in_dsrcd,in_byrcd "; 
				strORDFLD1= "in_dsrcd";
				strORDFLD2= "in_byrcd";	
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setORDBY");
		}
	}
	/**
	 * 
	*/
	private void getSUBHDR()
	{
		try
		{
			strWHRSTR = setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText());
			strSHDRNM = "";
			strSHDRDS = "";
			if (cmbRPTWS.getSelectedIndex()>0)
			{
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
				{
					strWHRSTR += " and dod_prdcd = '"+txtSPCCT.getText()+"'";
					strSHDRDS = getPRMST(txtSPCCT.getText(),"PR_PRDDS");
					strSHDRNM = "Grade:";
				}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strPCTWS))
				{
					strWHRSTR += " and SUBSTRING(dod_prdcd,1,4) = '"+txtSPCCT.getText()+"'";
					strSHDRDS = getCDTRN("MSTCOXXPGR"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN);
					strSHDRNM = "Prod Category:";
				}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
				{
					strWHRSTR += " and dod_dorno in (select dot_dorno from mr_dotrn where dot_indno in (select in_indno from mr_inmst where in_saltp = '"+txtSPCCT.getText()+"'))";
					strSHDRDS = getCDTRN("SYSMR00SAL"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); 
					strSHDRNM = "Sale Type :";
				}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strPKGWS))
				{
					strWHRSTR += " and dod_pkgtp = '"+txtSPCCT.getText()+"'";
					strSHDRDS = getCDTRN("SYSFGXXPKG"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); 
					strSHDRNM = "Pkg.Type :";
				}
			}
			txtSPCCT.setVisible(true);
			if(strSHDRNM.equals(""))
				txtSPCCT.setVisible(false);
			lblSPCDS.setText(strSHDRDS);
			lblSPCCT.setText(strSHDRNM);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"getSUBHDR");}
	}
	/**
	 * 
	*/	
	private void prnHEADER(int P_intCONDT)
	{
		try
		{
			intPAGENO +=1; 
			String L_strREGON="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
				
			if(P_intCONDT==0)
			{
				dosREPORT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd",intRPTWD-25)+padSTRING('L',"Report Date : "+cl_dat.M_txtCLKDT_pbst.getText(),25)+"\n");
				dosREPORT.writeBytes(padSTRING('R',"Tentitive Despatch Details "+strSHDRNM+" "+strSHDRDS+" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intRPTWD-24)+padSTRING('R',"Page No.    : "+intPAGENO,21)+"\n");
				dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); 
				cl_dat.M_intLINNO_pbst +=1;
									
			}
			if(P_intCONDT==1)
			{
				dosREPORT.writeBytes("Supreme Petrochem Ltd"+"\t\t\t\t\t\t\t\t"+ "Report Date : \t"+cl_dat.M_txtCLKDT_pbst.getText()+"\n");
            M_calLOCAL.setTime(M_fmtLCDAT.parse(M_txtTODAT.getText()));
			M_calLOCAL.add(java.util.Calendar.DATE,+1);
	        dosREPORT.writeBytes("Delay Despatch Details "+strSHDRNM+" "+strSHDRDS+" as on "+ M_fmtLCDAT.format(M_calLOCAL.getTime())+"\n");
				if(txtDELTP.getText().trim().length()>0)
				{
					dosREPORT.writeBytes("Delivery Type  "+"\t"+lblDELDS.getText()+"\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
				if(!cmbREGON.getSelectedItem().equals("Over All"))
				{
					L_strREGON=cmbREGON.getSelectedItem().toString();
					dosREPORT.writeBytes("Region   \t"+L_strREGON.substring(2,L_strREGON.length())+"\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
		
			cl_dat.M_intLINNO_pbst +=2;
			
			if(P_intCONDT==0)
			{
				dosREPORT.writeBytes("Grade                         Pkg.Type          Qty.    Desp Date      Transporter                             D.O.No.       D.O.Date      Buyer ");
				dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
				cl_dat.M_intLINNO_pbst +=2;
			}
			else if(P_intCONDT==1)
			{
				dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); 
				dosREPORT.writeBytes("Distributor                ");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("     Buyer                     ");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("\tIndent No \tGrade  \tQty. \tDel Type \tD.O.No. \tDisp.Date \tPen.Days \tTransporter   \tRemarks" +"\n");
				dosREPORT.writeBytes(crtLINE(intRPTWD,"-")); 
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 *  Setting string for Sub-system, Zone & Sale Type Filter
	*/	
	private String setSBSSTR(String LP_SBSTP)
	{
		String L_strRETSTR = ""; 
		try
		{
			for(int i=0;i<M_staUSRRT.length;i++)
			{
				if(LP_SBSTP.equals("ZON"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(2,4)+"',";
				else if(LP_SBSTP.equals("SAL"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(4,6)+"',";
				else if(LP_SBSTP.equals("SBS"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(2)+"',";
			}
			L_strRETSTR=L_strRETSTR.substring(0,L_strRETSTR.length()-1);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setSBSSTR");
		}
		return L_strRETSTR;
	}
		
	/**
	 * 
	*/
	private String setWHRSTR(String LP_STRDT, String LP_ENDDT)
	{
		String L_strRETSTR = "";
		if (LP_STRDT==null || LP_ENDDT==null)
			return L_strRETSTR;
		try
		{
			
			L_strRETSTR = "  dod_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and dod_dspdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and isnull(dot_dorqt,0)-isnull(dot_ladqt,0)>0 and isnull(dod_dorqt,0)-isnull(dod_ladqt,0)>0  and dod_stsfl<>'X' and  dod_mkttp='"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'";
			
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"setWHRSTR");
		}
		return L_strRETSTR;
	}
	/** Picking up Product Master Details
	 * @param LP_PRDCD		Product Code 
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */

	private String getPRMST(String LP_PRDCD, String LP_FLDNM)
	{
		String L_RETSTR = "";
		try
		{
		    if(hstPRMST.containsKey(LP_PRDCD))
			{
				String[] staPRMST = (String[])hstPRMST.get(LP_PRDCD);
		        if (LP_FLDNM.equals("PR_PRDDS"))
		                L_RETSTR = staPRMST[intAE_PR_PRDDS];
		        else if (LP_FLDNM.equals("PR_AVGRT"))
		                L_RETSTR = staPRMST[intAE_PR_AVGRT];
			}
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPRMST");
		}
		return L_RETSTR;
	}
	
	/** One time data capturing for Product Master
	*	into the Hash Table
	*/
	 private void crtPRMST()
	{
		String L_strSQLQRY = "";
	    try
	    {
	        hstPRMST.clear();
	        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl <> 'X' and SUBSTRING(pr_prdcd,1,2) in ('51','52','53')";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	           return;
	        }
	        while(true)
	        {
				strPRDCD = getRSTVAL(L_rstRSSET,"PR_PRDCD","C");
	            String[] staPRMST = new String[intPRMST_TOT];
	            staPRMST[intAE_PR_PRDDS] = getRSTVAL(L_rstRSSET,"PR_PRDDS","C");
	            staPRMST[intAE_PR_AVGRT] = getRSTVAL(L_rstRSSET,"PR_AVGRT","N");
	            hstPRMST.put(strPRDCD,staPRMST);
	            if (!L_rstRSSET.next())
	                break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtPRMST");
	    }
	return;
	}

	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
	    try
	    {
		if (LP_FLDTP.equals("C"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
		else if (LP_FLDTP.equals("N"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
		else if (LP_FLDTP.equals("D"))
			return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
		else if (LP_FLDTP.equals("T"))
		    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
		else 
			return " ";
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRSTVAL");
		}
	return " ";
	}	
	/** One time data capturing for Distributors
	 * into the Hash Table
	*/
	 private void crtPTMST()
    {
		String L_strSQLQRY = "";
        try
        {
            hstPTMST.clear();
		
			L_strSQLQRY ="select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'T' and pt_prtcd in (select distinct dot_trpcd from mr_dotrn where dot_dorno in (select dod_dorno from mr_dodel where  "+strWHRSTR+"))";
	    
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 return;
            }
            while(true)
            {
					strPRTTP=L_rstRSSET.getString("PT_PRTTP");
					strPRTCD=L_rstRSSET.getString("PT_PRTCD");
					
                    String[] staPTMST = new String[intPTMST_TOT];
                    staPTMST[intAE_PT_PRTNM] = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
                    hstPTMST.put(strPRTTP+strPRTCD,staPTMST);
		            if (!L_rstRSSET.next())
                            break;
            }
            L_rstRSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtPTMST");
        }
	return;
	}
	/** Picking up Distributor Details
	 * @param LP_PRTCD		Party Code 
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
    private String getPTMST(String LP_PRTTP,String LP_PRTCD, String LP_FLDNM)
    {
		String L_RETSTR = "";
		try
		{
		        String[] staPTMST = (String[])hstPTMST.get(LP_PRTTP+LP_PRTCD);
		        if (LP_FLDNM.equals("PT_PRTNM"))
		                L_RETSTR = staPTMST[intAE_PT_PRTNM];
		        else if (LP_FLDNM.equals("PT_ZONCD"))
		                L_RETSTR = staPTMST[intAE_PT_ZONCD];
		        else if (LP_FLDNM.equals("PT_SHRNM"))
		                L_RETSTR = staPTMST[intAE_PT_SHRNM];
		        else if (LP_FLDNM.equals("PT_ADR01"))
		                L_RETSTR = staPTMST[intAE_PT_ADR01];
		        else if (LP_FLDNM.equals("PT_ADR02"))
		                L_RETSTR = staPTMST[intAE_PT_ADR02];
		        else if (LP_FLDNM.equals("PT_ADR03"))
		                L_RETSTR = staPTMST[intAE_PT_ADR03];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPTMST");
		}
		return L_RETSTR;
    }
	/**
	 *
	 *Method to create lines that are used in the Reports
	 */
     private String crtLINE(int P_strCNT,String P_strLINCHR)
     {
		String strln = "";
		try
		{
			for(int i=1;i<=P_strCNT;i++)   
				strln += P_strLINCHR;
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
        return strln;
	}
	
	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	*/
    private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
    {
		String L_strSQLQRY = "";
        try
        {
            L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 return;
            }
            while(true)
            {
				strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                String[] staCDTRN = new String[intCDTRN_TOT];
                staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
                staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
                staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
                staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
                staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
                staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
                staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
                LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
				hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
                if (!L_rstRSSET.next())
					break;
            }
            L_rstRSSET.close();
        }
        catch(Exception L_EX)
        {
			setMSG(L_EX,"crtCDTRN");
        }
	}
	
/** Populating values in Combo Box from Vector
 */
	private void setCMBVL(JComboBox LP_CMBNM, Vector<String> LP_VTRNM)
	{
		LP_CMBNM.removeAllItems();
		for(int i=0;i<LP_VTRNM.size(); i++)
        {
            LP_CMBNM.addItem(LP_VTRNM.get(i).toString());
        }
	}
	
	/** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	*/
    private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
    {
	    try
        {
		    if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
			{
				return "";
			}
		    if (LP_FLDNM.equals("CMT_CODCD"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
            else if (LP_FLDNM.equals("CMT_CODDS"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
            else if (LP_FLDNM.equals("CMT_SHRDS"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
            else if (LP_FLDNM.equals("CMT_CHP01"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
            else if (LP_FLDNM.equals("CMT_CHP02"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
            else if (LP_FLDNM.equals("CMT_NMP01"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
            else if (LP_FLDNM.equals("CMT_NMP02"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
            else if (LP_FLDNM.equals("CMT_NCSVL"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
            else if (LP_FLDNM.equals("CMT_CCSVL"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return "";
   }
	
/** Input Verifier
 */	
	private class INPVF extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtDELTP)
				{
						M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MRXXDTP'AND CMT_CODCD= '"+txtDELTP.getText().trim() +"'  AND isnull(CMT_STSFL,'')<> 'X' "  ;
						
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET!=null)
						{
							if(L_rstRSSET.next())
							{
								
								lblDELDS.setText(L_rstRSSET.getString("CMT_CODDS"));
							
								L_rstRSSET.close();
								return true;
							}	
							else
							{
								setMSG("Invalid Type",'E'); 
								return false;
							}
						}
						else
						{
							setMSG("Invalid  Type",'E'); 
							return false;
						}
				}
			}
			catch(Exception e)
			{}
			return true;
		}
	}
}	