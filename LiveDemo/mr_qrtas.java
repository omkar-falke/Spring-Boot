//Time Analysis Sheet for Marketing 
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
import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;

class mr_qrtas extends cl_rbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;
	private JPanel pnlTASRPT;
	private String strSPCCT,strPRDCD,strPRTTP,strPRTCD,strWHRSTR,strORDBY;	//strSTRDT,strENDDT,
	private String strZONSTR, strSALSTR, strSBSSTR;
	private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_qrtas.doc"; 
	private JTextField txtSPCCT;

	private cl_JTable tblTASRPT;

	//private JRadioButton rdbALLWS, rdbRGNWS, rdbZONWS, rdbGRDWS, rdbDSRWS, rdbBYRWS, rdbSALWS, rdbPRDWS;
	//private JRadioButton rdbDFTSQ, rdbGRDSQ, rdbDSRSQ, rdbBYRSQ, rdbINOSQ, rdbIDTSQ;
	//private JCheckBox chkSTOTFL;
	private JLabel lblSPCCT, lblSPCDS;
	private ButtonGroup btgRPTWS;
	private JButton btnTASPRINT;

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
    private int intAE_PR_PRDCD = 0;
    private int intAE_PR_PRDDS = 1;
    private int intAE_PR_AVGRT = 2;

	private int intPTMST_TOT = 6;
    private int intAE_PT_PRTNM = 0;
    private int intAE_PT_ZONCD = 1;
    private int intAE_PT_SHRNM = 2;
    private int intAE_PT_ADR01 = 3;
    private int intAE_PT_ADR02 = 4;		
    private int intAE_PT_ADR03 = 5;		

	private int intTB1_CHKFL = 0;
	private int intTB1_INDNO = 1;
	private int intTB1_PRDDS = 2;
	private int intTB1_INDQT = 3;
	private int intTB1_BKGDT = 4;
	private int intTB1_DORDT = 5;
	private int intTB1_LADDT = 6;
	private int intTB1_INVDT = 7;
	private int intTB1_INVQT = 8;
	
	
	private String strALLWS = "Overall";		
	private String strRGNWS = "Region";		
	private String strZONWS = "Zone";
	private String strGRDWS = "Grade";	
	private String strDSRWS = "Distributor";	
	private String strBYRWS = "Buyer";	
	private String strSALWS = "Sale Type";	
	private String strPRDWS = "Prod.Category";	
	private String strCNSWS = "Consignee";	
	

	private String strDFTSQ = "Default";		
	private String strGRDSQ = "Grade";		
	private String strDSRSQ = "Distributor";		
	private String strBYRSQ = "Buyer";		
	private String strINOSQ = "Invoice No.";		
	
	
	private String[] arrTASHDR = new String[]{"0","1","2","3","4","5","6","7","8"};
	private int[] arrTASHDR_WD = new int[]{0,1,2,3,4,5,6,7,8};
	private char[] arrTASHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8'};

	private JComboBox cmbPRINTERS;
	
	
	private	int intDSRCTR = 0, intBYRCTR = 0, intBLKCTR = 0; 
	//private double[] arrCOLTOT;
	private double dblDSRQT = 0.000, dblBYRQT = 0.000, dblTOTQT = 0.000;
	private double dblDSRVL = 0.000, dblBYRVL = 0.000;
	private String  strDSRCD_OLD = "", strBYRCD_OLD="", strPRDCD_OLD ="";
	private String strSHDRNM = "", strSHDRDS = "";

	private int intTASWD = 130;      // report width
	private int intTASCL = 8;		 // No.of columns in Report

	private int intLINECT=72, intPAGENO=0, intRUNCL=0;
	boolean flgDSRCD_NEW = true, flgBYRCD_NEW = true, flgPRDCD_NEW = true, flgEOFCHK = false;

	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	
	private JLabel lblRPTWS;		
	private Vector<String> vtrRPTWS;		/**Vector for adding elements to cmbRPTWS */
	private JComboBox cmbRPTWS;		/**Combo-box for defining scope of the report */

	private JLabel lblMKTTP;		
	private Vector<String> vtrMKTTP;		/**Vector for adding elements to cmbMKTTP */
	private JComboBox cmbMKTTP;
	
	private Hashtable<String,String[]> hstCDTRN;			// Code Transaction details
	
	private Hashtable<String,String> hstCODDS;			// Code Description
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstPTMST;			// Party Details
	private Hashtable<String,String> hstDSRNM;			// Distibutor Short Name (as key) & Code (as value)
	private Vector<String> vtrDSRNM;			// Distributor Code & Short Name
	private Object[] arrHSTKEY;			// Object array for getting hash table key in sorted order
	
	mr_qrtas()
	{
		super(2);
		try
		{

			arrTASHDR[intTB1_CHKFL] = "";
			arrTASHDR[intTB1_INDNO] = "Indent No.";
			arrTASHDR[intTB1_PRDDS] = "Grade";
			arrTASHDR[intTB1_INDQT] = "Qty";
			arrTASHDR[intTB1_BKGDT] = "Bkg.Date";
			arrTASHDR[intTB1_DORDT] = "D.O.Date";
			arrTASHDR[intTB1_LADDT] = "L.A.Date";
			arrTASHDR[intTB1_INVDT] = "Inv.Date";
			arrTASHDR[intTB1_INVQT] = "Inv.Qty";

			arrTASHDR_WD[intTB1_CHKFL] = 20;
			arrTASHDR_WD[intTB1_INDNO] = 120;
			arrTASHDR_WD[intTB1_PRDDS] = 120;
			arrTASHDR_WD[intTB1_INDQT] = 120;
			arrTASHDR_WD[intTB1_BKGDT] = 120;
			arrTASHDR_WD[intTB1_DORDT] = 120;
			arrTASHDR_WD[intTB1_LADDT] = 120;
			arrTASHDR_WD[intTB1_INVDT] = 120;
			arrTASHDR_WD[intTB1_INVQT] = 120;
			
			arrTASHDR_PAD[intTB1_CHKFL] = 'R';
			arrTASHDR_PAD[intTB1_INDNO] = 'R';
			arrTASHDR_PAD[intTB1_PRDDS] = 'R';
			arrTASHDR_PAD[intTB1_INDQT] = 'L';
			arrTASHDR_PAD[intTB1_BKGDT] = 'L';
			arrTASHDR_PAD[intTB1_DORDT] = 'L';
			arrTASHDR_PAD[intTB1_LADDT] = 'L';
			arrTASHDR_PAD[intTB1_INVDT] = 'L';
			arrTASHDR_PAD[intTB1_INVQT] = 'L';

			
			
			
			tbpMAIN = new JTabbedPane();
			pnlTASRPT = new JPanel(null);
			txtSPCCT = new JTextField();
			
			hstCDTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstPRMST = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			hstDSRNM = new Hashtable<String,String>();
			
			vtrRPTWS = new Vector<String>();
			vtrMKTTP = new Vector<String>();
			
			lblRPTWS  = new JLabel("Scope");
			lblMKTTP  = new JLabel("Market Type");

			lblRPTWS.setForeground(Color.blue);
			lblMKTTP.setForeground(Color.blue);

			lblSPCCT  = new JLabel("Specify");
			lblSPCDS  = new JLabel("");
			//chkSTOTFL = new JCheckBox("With Sub-Total");

			btnTASPRINT = new JButton("PRINT");
			//rdbALLWS.setForeground(Color.blue);
			//rdbDFTSQ.setForeground(Color.blue);
			lblSPCCT.setForeground(Color.blue);
			lblSPCDS.setForeground(Color.blue);
			

			crtPRMST();
			
			setMatrix(20,8);
			//btgRPTWS.add(rdbALLWS);btgRPTWS.add(rdbRGNWS);btgRPTWS.add(rdbZONWS);btgRPTWS.add(rdbGRDWS);btgRPTWS.add(rdbDSRWS);btgRPTWS.add(rdbBYRWS);btgRPTWS.add(rdbSALWS);btgRPTWS.add(rdbPRDWS);
			//btgRPTSQ.add(rdbDFTSQ);btgRPTSQ.add(rdbGRDSQ);btgRPTSQ.add(rdbDSRSQ);btgRPTSQ.add(rdbBYRSQ);btgRPTSQ.add(rdbINOSQ);btgRPTSQ.add(rdbIDTSQ);

			add(lblRPTWS,1,3,1,2,this,'L');
			add(cmbRPTWS=new JComboBox(),2,3,1,2,this,'L');
			
			add(lblMKTTP,1,1,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),2,1,1,1,this,'L');


			//add(rdbALLWS,2,1,1,1,this,'L');
			add(lblSPCCT,3,1,1,1,this,'L');
			add(txtSPCCT,3,2,1,1,this,'L');
			add(lblSPCDS,5,2,1,3,this,'L');

			//add(rdbRGNWS,3,1,1,1,this,'L');
			//add(rdbZONWS,4,1,1,1,this,'L');
			//add(rdbGRDWS,5,1,1,1,this,'L');
			//add(rdbDSRWS,6,1,1,1,this,'L');
			//add(rdbBYRWS,4,2,1,1,this,'L');
			//add(rdbSALWS,5,2,1,1,this,'L');
			//add(rdbPRDWS,5,2,1,1,this,'L');

			
			//add(chkSTOTFL,2,7,1,2,this,'L');
			add(new JLabel("Print"),4,6,1,1,this,'L');
			add(btnTASPRINT,5,6,1,1,this,'L');
			txtSPCCT.setText("");
			txtSPCCT.setVisible(false);
			lblSPCCT.setText("");
			lblSPCDS.setText("");
			
			tblTASRPT = crtTBLPNL1(pnlTASRPT,arrTASHDR,500,2,1,9.1,7.9,arrTASHDR_WD,new int[]{0});  //,"Amount"
			
			tbpMAIN.addTab("Time Analysis",pnlTASRPT);
			
			add(tbpMAIN,7,1,13,8,this,'L');
			tblTASRPT.addMouseListener(this);

			setVTRRPTWS();
			setVTRMKTTP();
			
			
			remove(M_cmbDESTN);
			M_cmbDESTN.setVisible(true);
			M_vtrSCCOMP.remove(M_cmbDESTN);
			updateUI();
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"mr_qrtas");}
	}

	

	/** Adding elements to vtrRPTWS, for defining Scope of the report
	 */
	void setVTRRPTWS()
	{
		try
		{
			vtrRPTWS.clear();
			vtrRPTWS.addElement(strALLWS);		
			vtrRPTWS.addElement(strRGNWS);		
			vtrRPTWS.addElement(strZONWS);		
			vtrRPTWS.addElement(strGRDWS);		
			vtrRPTWS.addElement(strDSRWS);		
			vtrRPTWS.addElement(strBYRWS);		
			vtrRPTWS.addElement(strSALWS);		
			vtrRPTWS.addElement(strPRDWS);		
			vtrRPTWS.addElement(strCNSWS);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRRPTWS");}
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
			vtrMKTTP.addElement("05 Master Batch");		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRMKTTP");}
	}
	
	
	
	
	
	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		super.clrCOMP();
		//chkSTOTFL.setSelected(true);
		M_txtFMDAT.setText("01"+cl_dat.M_txtCLKDT_pbst.getText().substring(2,10));
		M_txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText());
	}

	
	
	
	
	/**
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{	
				//chkSTOTFL.setSelected(true);
				setCMBVL(cmbRPTWS,vtrRPTWS);
				setCMBVL(cmbMKTTP,vtrMKTTP);
			}

			if(M_txtFMDAT.getText().length()<10)
				M_txtFMDAT.setText("01/04/2004");
			if(M_txtTODAT.getText().length()<10)
				M_txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
			strSBSSTR = setSBSSTR("SBS");
			if(M_objSOURC == cmbRPTWS)
				getSUBHDR();
			if(M_objSOURC == M_txtFMDAT)
				M_txtTODAT.requestFocus();
			if(M_objSOURC == btnTASPRINT)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_qrtas.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtTASRPT();
					exePRINT1();
				}
		}
		catch (Exception L_EX) {setMSG(L_EX,"acionPerformed");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
		
	}

	/** method from cl_rbase overidden, as Printing is taken
	 * after selecting Scrren Display option
	 //
	String[] getPRINTERS()
	{
		
					flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//					flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
					aset = new HashPrintRequestAttributeSet();
					pservices =PrintServiceLookup.lookupPrintServices(flavor, aset);
					M_cmbDESTN.insertItemAt("Select",0);
					for(int i=0;i<pservices.length;i++)
						M_cmbDESTN.addItem(pservices[i].getName());
					M_cmbDESTN.setSelectedIndex(1);
					if(M_cmbDESTN.getItemCount()==2)
						M_cmbDESTN.setEnabled(false);
		String [] L_staTEMP=new String[M_cmbDESTN.getItemCount()];
		for(int i=0;i<L_staTEMP.length;i++)
			L_staTEMP[i]=M_cmbDESTN.getItemAt(i).toString();
		return L_staTEMP;
	}
	
	
/**
 */
private void exePRINT1()
{
	try
	{
		//M_cmbDESTN.removeAllItems();
		getPRINTERS();
		int intRESP = JOptionPane.showConfirmDialog( this,M_cmbDESTN,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
		if(intRESP==0)
			doPRINT(strRESSTR);
	}
	catch(Exception L_EX) {setMSG(L_EX,"exePRINT1");}
}
	
	



	/**
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
		M_objSOURC=L_KE.getSource();
		if(M_objSOURC==txtSPCCT && L_KE.getKeyCode()==L_KE.VK_ENTER)
			getSUBHDR();
		if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				M_strHLPFLD = "txtSPCCT";
				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN' and cmt_codcd in (select cmt_chp02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+"))  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strZONWS))
					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strGRDWS))
					cl_hlp("Select PR_PRDCD, PR_PRDDS from CO_PRMST where SUBSTRING(pr_prdcd,1,2) in ('51','52','53') and pr_prdcd in (select distinct int_prdcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PR_PRDCD" ,2,1,new String[] {"Code","Description"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strDSRWS))
					cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='D' and pt_prtcd in (select distinct in_dsrcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strBYRWS))
					cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' and pt_prtcd in (select distinct in_byrcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd in (select in_saltp from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
					cl_hlp("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and SUBSTRING(cmt_codcd,1,4) in (select distinct SUBSTRING(int_prdcd,1,4) int_prdcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strCNSWS))
					cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' and pt_prtcd in (select distinct in_cnscd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");
			}
		}
		catch (Exception L_EX) {setMSG(L_EX,"keyPressed");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}



	/**
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
	}
	

	/**
	 */
	private void dspTASDTL()
	{
		try
		{
			ResultSet rstRSSET = null;
			M_strSQLQRY  = "select in_indno,int_prdds,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) int_indqt,in_bkgdt,dot_dordt,ivt_laddt,ivt_invdt,ivt_invqt from vw_intrn left outer join mr_dotrn on int_cmpcd=dot_cmpcd and int_mkttp=dot_mkttp and int_indno=dot_indno and int_prdcd=dot_prdcd and int_pkgtp=dot_pkgtp  left outer join mr_ivtrn on int_cmpcd=ivt_cmpcd and int_mkttp=ivt_mkttp and int_indno=ivt_indno and int_prdcd=ivt_prdcd and int_pkgtp=ivt_pkgtp where "+strWHRSTR;

			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			tblTASRPT.clrTABLE();

			String L_strDSRCD = "";
			int i=0;
			while(true)
			{
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"IN_INDNO","C"),i,intTB1_INDNO);
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"INT_PRDDS","C"),i,intTB1_PRDDS);
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"INT_INDQT","N"),i,intTB1_INDQT);
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"IN_BKGDT","D"),i,intTB1_BKGDT);
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"DOT_DORDT","D"),i,intTB1_DORDT);
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_LADDT","T"),i,intTB1_LADDT);
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVDT","T"),i,intTB1_INVDT);
			    tblTASRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVQT","N"),i,intTB1_INVQT);
				i++;
				if(!rstRSSET.next())
					break;
			}
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspTASDTL");}
	}
	



	
	
	
	
	
	
	/**
	 */
	public void exePRINT()
	{
		try
		{
			//super.exePRINT();
			//strSTRDT = M_txtFMDAT.getText();
			//strENDDT = M_txtTODAT.getText();
			this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
			hstCDTRN.clear();
			crtCDTRN("'MSTCOXXCUR','STSMRXXIND','SYSMRXXDTP', 'SYSMR00SAL', 'SYSMRXXPMT','SYSMR01MOT','SYSMR00EUS','SYSCOXXTAX','SYSCOXXDST','SYSFGXXPKG','SYSCOXXAMT'","",hstCDTRN);
			if(M_txtFMDAT.getText().length()!=10 || M_txtTODAT.getText().length()!=10)
				return;
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
			strSBSSTR = setSBSSTR("SBS");
			getSUBHDR();

			//System.out.println("-002");
			strORDBY = "";
			if(cmbRPTWS.getSelectedItem().toString().equals(strDFTSQ))
				strORDBY = " order by in_bkgdt,in_indno,int_prdds";
			else if(cmbRPTWS.getSelectedItem().toString().equals(strGRDSQ))
				strORDBY = " order by  int_prdds ";
			crtPTMST();
						
			if(!exeTBLREFSH())
				return;
			
			dspTASDTL();
		
		}
		catch (Exception L_EX) {setMSG(L_EX,"exePRINT");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}


	/**
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
				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
					{strWHRSTR += " and in_zoncd in (select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_chp02 = '"+txtSPCCT.getText()+"')"; strSHDRDS = getCDTRN("SYSMR00RGN"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Region:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strZONWS))
					{strWHRSTR += " and in_zoncd = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00ZON"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Zone:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strGRDWS))
					{strWHRSTR += " and int_prdcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00ZON"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Grade:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strDSRWS))
					{strWHRSTR += " and in_dsrcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPRTNM("D",txtSPCCT.getText()); strSHDRNM = "Distributor:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strBYRWS))
					{strWHRSTR += " and in_byrcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPRTNM("C",txtSPCCT.getText()); strSHDRNM = "Buyer:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
					{strWHRSTR += " and in_saltp = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00SAL"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Sale Type:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
					{strWHRSTR += " and SUBSTRING(int_prdcd,1,4) = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00PGR"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Prod Category:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strCNSWS))
				{strWHRSTR += " and in_cnscd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPRTNM("C",txtSPCCT.getText()); strSHDRNM = "Consignee :";}
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
 */	
	private void crtTASHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intTASWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Time Analysis "+strSHDRNM+" "+strSHDRDS+" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intTASWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intTASWD,"-")+"\n"); intLINECT +=1;
				  for(i=1;i<arrTASHDR.length;i++)
			{
				O_DOUT.writeBytes(padSTRING(arrTASHDR_PAD[i],arrTASHDR[i],arrTASHDR_WD[i]/8)+" "); intRUNCL +=1;
				if(intRUNCL>intTASCL)
					{O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrTASHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;}
			}
			O_DOUT.writeBytes("\n"+crtLINE(intTASWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtTASHDR");}
	}
	

	

	
	
	/**
	 */
	private void crtTASRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72;

			int i=0, j = tblTASRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtTASHDR();
			for(i=0;i<tblTASRPT.getRowCount();i++)
			{
				if(tblTASRPT.getValueAt(i,intTB1_INDNO).toString().equals(""))
					break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrTASHDR_PAD[k],tblTASRPT.getValueAt(i,k).toString().trim(),arrTASHDR_WD[k]/8)+" "); intRUNCL += 1;
					if(intRUNCL>intTASCL)
					{
						O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrTASHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						
					}
				}
				O_DOUT.writeBytes("\n\n");intLINECT +=2;
				if(intLINECT>60) crtTASHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intTASWD,"-")+"\n"); intLINECT +=2;
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtTASRPT");}
	}
	

	
	
	
	
	/**
	 */
	private int getDSRCOL(String LP_SHRNM)
	{
		int i = 0;
		for (i=0; i<arrHSTKEY.length;i++)
			{if(arrHSTKEY[i].equals(LP_SHRNM)) break;}
		return i;
	}
	

	/**Dynamic Recreation of display table
	 * According to content/values applicable at the time of execution
	 */
	private boolean exeTBLREFSH()
	{
		try
		{
			tbpMAIN.remove(pnlTASRPT);
			pnlTASRPT.removeAll();

			pnlTASRPT = new JPanel(null);
			
			int i;
			M_strSQLQRY = "select count(*) int_recct from vw_intrn where "+strWHRSTR;
			ResultSet rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next()  || rstRSSET==null)
				{setMSG("No Detail Records Found",'E');return false;}
			int L_intRECCT = Integer.parseInt(getRSTVAL(rstRSSET,"INT_RECCT","N"));
			L_intRECCT = 3*L_intRECCT;
			rstRSSET.close();
			//System.out.println("004");
			tblTASRPT = crtTBLPNL1(pnlTASRPT,arrTASHDR,L_intRECCT,2,1,9.1,7.9,arrTASHDR_WD,new int[]{0});    //,"Amount"
			tbpMAIN.addTab("Time Analysis",pnlTASRPT);
		
			//System.out.println("005");
			tblTASRPT.addMouseListener(this);
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeREFSH");}
		return true;
	}

	
	/** Setting string for Sub-system, Zone & Sale Type Filter
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
		catch(Exception L_EX) {setMSG(L_EX,"setSBSSTR");}
		return L_strRETSTR;
	}
	
	
	/**
	 */
	private String setWHRSTR(String LP_STRDT, String LP_ENDDT)
	{
		String L_strRETSTR = "";
		try
		{
			L_strRETSTR = " IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_bkgdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and int_stsfl not in ('0','X') and (isnull(int_indqt,0)-isnull(int_fcmqt,0))>0  and in_mkttp='"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'"
				+ " and int_SBSCD1 in "+M_strSBSLS;
			System.out.println(L_strRETSTR);
		}
		catch (Exception L_EX){setMSG(L_EX,"setWHRSTR");}
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
		        String[] staPRMST = (String[])hstPRMST.get(LP_PRDCD);
		        if (LP_FLDNM.equals("PR_PRDDS"))
		                L_RETSTR = staPRMST[intAE_PR_PRDDS];
		        else if (LP_FLDNM.equals("PR_AVGRT"))
		                L_RETSTR = staPRMST[intAE_PR_AVGRT];
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
	             setMSG("Product Records not found in CO_PRMST",'E');
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
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP+"/"+LP_RSLSET.getString(LP_FLDNM).toString())			;
		if (LP_FLDTP.equals("C"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
			//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
		else if (LP_FLDTP.equals("N"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
		else if (LP_FLDTP.equals("D"))
			return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "-";
		else if (LP_FLDTP.equals("T"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).substring(8,10)+"/"+LP_RSLSET.getString(LP_FLDNM).substring(5,7)+" "+LP_RSLSET.getString(LP_FLDNM).substring(10,16) : "-";
		    //return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
		else
			return " ";
		}
		catch (Exception L_EX)
		{System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);setMSG(L_EX,"getRSTVAL");}
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
			hstDSRNM.clear();
            L_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct in_byrcd from vw_intrn  where "+strWHRSTR+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct in_cnscd from vw_intrn  where "+strWHRSTR+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'D' and pt_prtcd in (select distinct in_dsrcd from vw_intrn  where "+strWHRSTR+")";
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 setMSG("Party records not found in CO_PTMST",'E');
                  return;
            }
            while(true)
            {
                    strPRTTP = getRSTVAL(L_rstRSSET,"PT_PRTTP","C");
                    strPRTCD = getRSTVAL(L_rstRSSET,"PT_PRTCD","C");
                    String[] staPTMST = new String[intPTMST_TOT];
                    staPTMST[intAE_PT_PRTNM] = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
                    staPTMST[intAE_PT_ZONCD] = getRSTVAL(L_rstRSSET,"PT_ZONCD","C");
                    staPTMST[intAE_PT_SHRNM] = getRSTVAL(L_rstRSSET,"PT_SHRNM","C");
                    staPTMST[intAE_PT_ADR01] = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
                    staPTMST[intAE_PT_ADR02] = getRSTVAL(L_rstRSSET,"PT_ADR02","C");
                    staPTMST[intAE_PT_ADR03] = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
                    hstPTMST.put(strPRTTP+strPRTCD,staPTMST);
					if(strPRTTP.equals("D"))
						{hstDSRNM.put(getRSTVAL(L_rstRSSET,"PT_SHRNM","C"),strPRTCD);}
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
			{System.out.println("getPTMST : "+LP_PRTTP+"/"+LP_PRTCD+"/"+LP_FLDNM);setMSG(L_EX,"getPTMST");}
		return L_RETSTR;
    }


	/**  Conversion of Hash table keys to sorted array
	 *   For retrieving elements in sorted order
	 */
	private void setHST_ARR(Hashtable LP_HSTNM)
	{
		try
		{
			arrHSTKEY = LP_HSTNM.keySet().toArray();
			Arrays.sort(arrHSTKEY);
		}
		catch(Exception e){setMSG(e,"setHST_ARR");}
	}

	
	

	/**
	 */
	public static DataOutputStream crtDTOUTSTR(FileOutputStream outfile){
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}

	/**
	 */
	public static FileOutputStream crtFILE(String strFILE){
		FileOutputStream outFILE = null;
		try{
			File file = new File(strFILE);
			outFILE = new FileOutputStream(file);
        	return outFILE;
		}
		catch(IOException L_IO){
			System.out.println("L_IO FOS...........:"+L_IO);
			return outFILE;		
		}
	}


	/**
	 *
	 *Method to create lines that are used in the Reports
	 */
        private String crtLINE(int P_strCNT,String P_strLINCHR)
        {
		String strln = "";
		try{
              for(int i=1;i<=P_strCNT;i++)    strln += P_strLINCHR;
		}catch(Exception L_EX){
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
			//System.out.println(L_strSQLQRY);
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 //setMSG("Records not found in CO_CDTRN",'E');
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

	 
	/** Validating party code
	 */
	private String getPRTNM(String LP_PRTTP, String LP_PRTCD)
	{
		String L_RETSTR = "";
		try
		{
			if(LP_PRTCD.length()>0)
			{
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select PT_PRTNM from co_ptmst where pt_prttp = '"+LP_PRTTP+"' and pt_prtcd = '"+LP_PRTCD+"'");
					if(L_rstRSSET!=null && L_rstRSSET.next())
					{
						L_RETSTR = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
						L_rstRSSET.close();
					}
			}
		}
		catch (Exception L_EX)
		{setMSG(L_EX,"getPRTNM");}
		return L_RETSTR;
	}


	
/** Populating values in Combo Box from Vector
 */
private void setCMBVL(JComboBox LP_CMBNM, Vector<String> LP_VTRNM)
{
		LP_CMBNM.removeAllItems();
		for(int i=0;i<LP_VTRNM.size(); i++)
        {
			
				//System.out.println(i+"  : "+LP_VTRNM.get(i).toString());
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
		//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
        try
        {
			    if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
					{System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM); return "";}
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
			System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
		}
        return "";
        }
	

	
		
		
		
	/**
	 */
	private   String padNUMBER(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		try
		{
			
			if(P_strSTRVL.length()==0)
				P_strSTRVL = "-";
			else if(Double.parseDouble(P_strSTRVL)==0)
				P_strSTRVL = "-";
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			if(P_intPADLN <= L_STRLN)
			{
				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			int L_STRDF = P_intPADLN - L_STRLN;
			
			StringBuffer L_STRBUF;
			switch(P_chrPADTP)
			{
				case 'C':
					L_STRDF = L_STRDF / 2;
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
					break;
				case 'R':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
					{
						if(M_rdbTEXT.isSelected())
						L_STRBUF.insert(j,' ');
						else
							L_STRBUF.insert(j,"\t");
					}
					P_strTRNVL =  P_strSTRVL+L_STRBUF ;
					break;
				case 'L':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL ;
					break;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"padNUMBER");
		}
		return P_strTRNVL;
	}

}	