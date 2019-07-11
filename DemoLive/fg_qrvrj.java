/** Program Name : fg_qrvrj
 *  Purpose : Query cum display of Vehicle Rejection detail
 *  Developed by : SRD
 *  Implementation date : 14/03/2005
 *  Program detail :
 *        - This program is copied from mr_qrtds (Tentative Despatch) 
 *        - Date Range, Scope and Display Order is specified by the user
 *        - Vehicle Rejection data is fetched from fg_vrtrn 
 *          According to the scope, date range and order selection query is formed
 *          and records are fetched from fg_vrtrn
 *        Tables			Used for
 *        co_cdtrn			Rejection category  (sys/fgxxrej)
 *        co_ptmst			Transporter Name
 *		  co_prmst			Product description
 * 
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
import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;

class fg_qrvrj extends cl_rbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;
	private JPanel pnlRPTRPT;
	private String strSPCCT,strPRDCD,strPRTTP,strPRTCD,strWHRSTR,strORDBY;	//strSTRDT,strENDDT,
	private String strZONSTR, strSALSTR, strSBSSTR;
	private String strRESSTR = cl_dat.M_strREPSTR_pbst+"fg_qrvrj.doc"; 
	private JTextField txtSPCCT;

	private cl_JTable tblRPTRPT;

	//private JCheckBox chkSTOTFL;
	private JLabel lblSPCCT, lblSPCDS;
	private ButtonGroup btgRPTWS, btgRPTSQ;
	private JButton btnRPTPRINT;

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
	private int intTB1_TRPNM = 1;
	private int intTB1_LRYNO = 2;
	private int intTB1_CNTDS = 3;
	private int intTB1_REJDT = 4;
	private int intTB1_REJCD = 5;
	private int intTB1_REJDS = 6;
	private int intTB1_REMDS = 7;
	private int intTB1_REJBY = 8;
	private int intTB1_DOCNO = 9;
	private int intTB1_CORFL = 10;
	private int intTB1_DSPFL = 11;
	
	
	
	private String strALLWS = "Overall";		
	private String strTRPWS = "Transporter";	
	private String strLRYWS = "Lorry No.";	
	private String strCNTWS = "Container";	
	private String strRCDWS = "Rej.Category";	
	

	private String strDFTSQ = "Default";		
	private String strTRPSQ = "Transporter";		
	private String strLRYSQ = "Lorry No.";		
	private String strRCDSQ = "Rej.Code";		
	private String strRDTSQ = "Rej.Date";		
	private String strDOCSQ = "Gate In No.";		
	private String strCNTSQ = "Container";		
	
	
	private String[] arrRPTHDR = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11"};
	private int[] arrRPTHDR_WD = new int[]{0,1,2,3,4,5,6,7,8,9,0,1};
	private char[] arrRPTHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8','9','0','1'};
	


	private JComboBox cmbPRINTERS;
	
	
	private	int intDSRCTR = 0, intBYRCTR = 0, intBLKCTR = 0; 
	//private double[] arrCOLTOT;
	private String strSHDRNM = "", strSHDRDS = "";

	
	
	private int intRPTWD = 130;      // report width
	private int intRPTCL = 6;		 // No.of columns in Report

	private int intLINECT=72, intPAGENO=0, intRUNCL=0;
	//boolean flgDSRCD_NEW = true, flgBYRCD_NEW = true, flgPRDCD_NEW = true, flgEOFCHK = false;

	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	
	private JLabel lblRPTWS, lblRPTSQ;		
	private Vector<String> vtrRPTWS,vtrRPTSQ;		/**Vector for adding elements to cmbRPTWS, cmbRPTSQ */
	private JComboBox cmbRPTWS,cmbRPTSQ;		/**Combo-box for defining scope & order of the report */

	private JLabel lblWRHTP;		
	private Vector<String> vtrWRHTP;		/**Vector for adding elements to cmbWRHTP */
	private JComboBox cmbWRHTP;
	
	private Hashtable<String,String[]> hstCDTRN;			// Code Transaction details


	
	private Hashtable<String,String> hstCODDS;			// Code Description
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstPTMST;			// Party Details
	//private Hashtable hstDSRNM;			// Distibutor Short Name (as key) & Code (as value)
	private Vector<String> vtrDSRNM;			// Distributor Code & Short Name
	private Object[] arrHSTKEY;			// Object array for getting hash table key in sorted order
	
	fg_qrvrj()
	{
		super(2);
		try
		{

			arrRPTHDR[intTB1_CHKFL] = "";
			arrRPTHDR[intTB1_TRPNM] = "Transp.";
			arrRPTHDR[intTB1_LRYNO] = "Lorr.No.";
			arrRPTHDR[intTB1_CNTDS] = "Contr.No.";
			arrRPTHDR[intTB1_REJDT] = "Date";
			arrRPTHDR[intTB1_REJCD] = "Rej.Cd.";
			arrRPTHDR[intTB1_REJDS] = "Rej.Desc";
			arrRPTHDR[intTB1_REMDS] = "Remark";
			arrRPTHDR[intTB1_REJBY] = "Rej.By";
			arrRPTHDR[intTB1_DOCNO] = "Gate In";
			arrRPTHDR[intTB1_CORFL] = "Corr.";
			arrRPTHDR[intTB1_DSPFL] = "Desp.";

			arrRPTHDR_WD[intTB1_CHKFL] = 20;
			arrRPTHDR_WD[intTB1_TRPNM] = (80*2)+(8*1);
			arrRPTHDR_WD[intTB1_LRYNO] = 80;
			arrRPTHDR_WD[intTB1_CNTDS] = 80;
			arrRPTHDR_WD[intTB1_REJDT] = 80;
			arrRPTHDR_WD[intTB1_REJCD] = 80;
			arrRPTHDR_WD[intTB1_REJDS] = (80*3)+(8*2);
			arrRPTHDR_WD[intTB1_REMDS] = (80*3)+(8*2);
			arrRPTHDR_WD[intTB1_REJBY] = 80;
			arrRPTHDR_WD[intTB1_DOCNO] = 80;
			arrRPTHDR_WD[intTB1_CORFL] = 80;
			arrRPTHDR_WD[intTB1_DSPFL] = 80;

			
			arrRPTHDR_PAD[intTB1_CHKFL] = 'R';
			arrRPTHDR_PAD[intTB1_TRPNM] = 'R';
			arrRPTHDR_PAD[intTB1_LRYNO] = 'R';
			arrRPTHDR_PAD[intTB1_CNTDS] = 'R';
			arrRPTHDR_PAD[intTB1_REJDT] = 'R';
			arrRPTHDR_PAD[intTB1_REJCD] = 'R';
			arrRPTHDR_PAD[intTB1_REJDS] = 'R';
			arrRPTHDR_PAD[intTB1_REMDS] = 'R';
			arrRPTHDR_PAD[intTB1_REJBY] = 'R';
			arrRPTHDR_PAD[intTB1_DOCNO] = 'R';
			arrRPTHDR_PAD[intTB1_CORFL] = 'R';
			arrRPTHDR_PAD[intTB1_DSPFL] = 'R';
			
			
			
			tbpMAIN = new JTabbedPane();
			pnlRPTRPT = new JPanel(null);
			txtSPCCT = new JTextField();
			
			hstCDTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstPRMST = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			//hstLOTNO = new Hashtable();
			//hstDSRNM = new Hashtable();
			
			vtrRPTWS = new Vector<String>();
			vtrRPTSQ = new Vector<String>();
			vtrWRHTP = new Vector<String>();
			
			lblRPTWS  = new JLabel("Scope");
			lblRPTSQ  = new JLabel("Order");
			lblWRHTP  = new JLabel("W/H Type");

			lblRPTWS.setForeground(Color.blue);
			lblRPTSQ.setForeground(Color.blue);
			lblWRHTP.setForeground(Color.blue);

			lblSPCCT  = new JLabel("Specify");
			lblSPCDS  = new JLabel("");
			//chkSTOTFL = new JCheckBox("With Sub-Total");

			btnRPTPRINT = new JButton("Print");
			//rdbALLWS.setForeground(Color.blue);
			//rdbDFTSQ.setForeground(Color.blue);
			lblSPCCT.setForeground(Color.blue);
			lblSPCDS.setForeground(Color.blue);
			

			crtPRMST();
			
			setMatrix(20,8);

			add(lblRPTWS,2,2,1,2,this,'L');
			add(cmbRPTWS=new JComboBox(),2,3,1,2,this,'L');
			
			add(lblRPTSQ,3,2,1,2,this,'L');
			add(cmbRPTSQ=new JComboBox(),3,3,1,2,this,'L');

			add(lblWRHTP,1,1,1,1,this,'L');
			add(cmbWRHTP=new JComboBox(),2,1,1,1,this,'L');


			//add(rdbALLWS,2,1,1,1,this,'L');
			add(lblSPCCT,4,1,1,1,this,'L');
			add(txtSPCCT,4,2,1,1,this,'L');
			add(lblSPCDS,5,2,1,3,this,'L');


			
			//add(chkSTOTFL,2,7,1,2,this,'L');
			add(new JLabel("Print"),4,6,1,1,this,'L');
			add(btnRPTPRINT,5,6,1,1,this,'L');
			txtSPCCT.setText("");
			txtSPCCT.setVisible(false);
			lblSPCCT.setText("");
			lblSPCDS.setText("");
			
			tblRPTRPT = crtTBLPNL1(pnlRPTRPT,arrRPTHDR,500,2,1,9.1,7.9,arrRPTHDR_WD,new int[]{0});  //,"Amount"
			
			tbpMAIN.addTab("Vehicle Rejection Details",pnlRPTRPT);
			
			add(tbpMAIN,7,1,13,8,this,'L');
			tblRPTRPT.addMouseListener(this);

			setVTRRPTWS();
			setVTRRPTSQ();
			setVTRWRHTP();
			
			M_pnlRPFMT.setVisible(true);
			remove(M_cmbDESTN);
			M_cmbDESTN.setVisible(true);
			M_vtrSCCOMP.remove(M_cmbDESTN);
			updateUI();
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"fg_qrvrj");}
	}


	/** Adding elements to vtrRPTSQ, for defining Scope of the report
	 */
	void setVTRRPTSQ()
	{
		try
		{
			vtrRPTSQ.clear();
			vtrRPTSQ.addElement(strDFTSQ);		
			vtrRPTSQ.addElement(strTRPSQ);		
			vtrRPTSQ.addElement(strLRYSQ);		
			vtrRPTSQ.addElement(strRCDSQ);		
			vtrRPTSQ.addElement(strRDTSQ);		
			vtrRPTSQ.addElement(strDOCSQ);		
			vtrRPTSQ.addElement(strCNTSQ);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRRPTSQ");}
	}
	
	
	

	/** Adding elements to vtrRPTWS, for defining Scope of the report
	 */
	void setVTRRPTWS()
	{
		try
		{
			vtrRPTWS.clear();
			vtrRPTWS.addElement(strALLWS);		
			vtrRPTWS.addElement(strTRPWS);		
			vtrRPTWS.addElement(strLRYWS);		
			vtrRPTWS.addElement(strCNTWS);		
			vtrRPTWS.addElement(strRCDWS);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRRPTWS");}
	}


	/** Adding elements to vtrWRHTP, for defining Scope of the report
	 */
	void setVTRWRHTP()
	{
		try
		{
			vtrWRHTP.clear();
			vtrWRHTP.addElement("01 SPL site");		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRWRHTP");}
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
				setCMBVL(cmbRPTSQ,vtrRPTSQ);
				setCMBVL(cmbWRHTP,vtrWRHTP);
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
			if(M_objSOURC == cmbRPTSQ)
				setORDBY();
			if(M_objSOURC == M_txtFMDAT)
				M_txtTODAT.requestFocus();
			if(M_objSOURC == btnRPTPRINT)
				{
			    
			    
			    if(M_rdbHTML.isSelected())
			        strRESSTR = cl_dat.M_strREPSTR_pbst +"fg_qrvrj.html";
			    else if(M_rdbTEXT.isSelected())
					strRESSTR= cl_dat.M_strREPSTR_pbst + "fg_qrvrj.doc";						
				
			    if(M_rdbHTML.isSelected())
			    {   
			        Runtime r = Runtime.getRuntime();
					Process p = null;	
			        
					p  = r.exec("C:\\windows\\iexplore.exe "+strRESSTR);
			        
			    }   
			    else if(M_rdbTEXT.isSelected())
			    {
			        Runtime r = Runtime.getRuntime();
					Process p = null;	
			         p  = r.exec("C:\\windows\\wordpad.exe " + strRESSTR); 
			        	
			       // strRESSTR = cl_dat.M_strREPSTR_pbst+"fg_qrvrj.doc";
			    }   
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtRPTRPT();
					exePRINT1();
				}
		}
		catch (Exception L_EX) {setMSG(L_EX,"acionPerformed");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
		
	}

	/** method from cl_rbase overidden, as Printing is taken
	 * after selecting Scrren Display option
	 */
/*	String[] getPRINTERS()
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
	*/
	
	
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
				if(cmbRPTWS.getSelectedItem().toString().equals(strTRPWS))
					cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='T' and pt_prtcd in (select vr_trpcd from fg_vrtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Description"},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strLRYWS))
					cl_hlp("Select distinct VR_LRYNO, VR_CNTDS from fg_vrtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+" order by vr_lryno" ,2,1,new String[] {"Lorry No.","Contr.No."},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strLRYWS))
					cl_hlp("Select distinct VR_CNTDS, VR_LRYNO from fg_vrtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+" and length(vr_cntds)>5 order by vr_cntds" ,2,1,new String[] {"Contr.No.","Lorry.No."},2,"CT");
				if(cmbRPTWS.getSelectedItem().toString().equals(strRCDWS))
					cl_hlp("Select distinct CMT_CODCD, CMT_CODDS from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='FGXXREJ' and cmt_codcd in (select vr_rejcd from fg_vrtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+" ) order by cmt_codcd" ,2,1,new String[] {"Contr.No.","Lorry.No."},2,"CT");
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
	private void dspRPTDTL()
	{
		try
		{
			ResultSet rstRSSET = null;
			setORDBY();
			M_strSQLQRY = "select *  from fg_vrtrn  where "+strWHRSTR+strORDBY;

			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			
			int i;
			i=0;
			tblRPTRPT.clrTABLE();

			String L_strDSRCD = "";
			while (true)
			{
				tblRPTRPT.setValueAt(getPTMST("T",getRSTVAL(rstRSSET,"VR_TRPCD","C"),"PT_PRTNM"),i,intTB1_TRPNM);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_LRYNO","C"),i,intTB1_LRYNO);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_CNTDS","C"),i,intTB1_CNTDS);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_REJDT","D"),i,intTB1_REJDT);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_REJCD","C"),i,intTB1_REJCD);
				tblRPTRPT.setValueAt(getCDTRN("SYSFGXXREJ"+getRSTVAL(rstRSSET,"VR_REJCD","C"),"CMT_CODDS",hstCDTRN),i,intTB1_REJDS);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_REMDS","C"),i,intTB1_REMDS);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_REJBY","C"),i,intTB1_REJBY);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_DOCNO","C"),i,intTB1_DOCNO);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_CORFL","C"),i,intTB1_CORFL);
				tblRPTRPT.setValueAt(getRSTVAL(rstRSSET,"VR_DSPFL","C"),i,intTB1_DSPFL);
				i += 1;
				if(!rstRSSET.next())
					break;
			}
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspRPTDTL");}
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
			crtCDTRN("'SYSFGXXREJ'","",hstCDTRN);
			if(M_txtFMDAT.getText().length()!=10 || M_txtTODAT.getText().length()!=10)
				return;
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
			strSBSSTR = setSBSSTR("SBS");
			getSUBHDR();
			setORDBY();
			crtPTMST();
						
			if(!exeTBLREFSH())
				return;
			
			dspRPTDTL();
		
		}
		catch (Exception L_EX) {setMSG(L_EX,"exePRINT");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}

	
	/**
	 */
	private void setORDBY()
	{
		try
		{
			strORDBY = "";
			if(cmbRPTSQ.getSelectedItem().toString().equals(strDFTSQ))
				strORDBY = " order by vr_rejdt desc,vr_docno asc";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strTRPSQ))
				strORDBY = " order by  vr_trpcd";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strLRYSQ))
				strORDBY = " order by  vr_lryno";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strRCDSQ))
				strORDBY = " order by  vr_rejcd ";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strRDTSQ))
				strORDBY = " order by  vr_rejdt ";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strDOCSQ))
				strORDBY = " order by  vr_docno ";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strCNTSQ))
				strORDBY = " order by  vr_cntds ";
		}
		catch (Exception L_EX) {setMSG(L_EX,"setORDBY");}
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
				if(cmbRPTWS.getSelectedItem().toString().equals(strTRPWS))
					{strWHRSTR += " and vr_trpcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPTMST("T",txtSPCCT.getText(),"PT_PRTNM"); strSHDRNM = "Transporter:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strLRYWS))
					{strWHRSTR += " and vr_lryno = '"+txtSPCCT.getText()+"'";strSHDRDS = txtSPCCT.getText(); strSHDRNM = "Lorry No:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strCNTWS))
					{strWHRSTR += " and vr_cntds = '"+txtSPCCT.getText()+"'";strSHDRDS = txtSPCCT.getText(); strSHDRNM = "Contr.No.:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strRCDWS))
					{strWHRSTR += " and vr_rejcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSFGXXREJ"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Rej.Category:";}
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
	private void crtRPTHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Vehicle Rejection Statement "+strSHDRNM+" "+strSHDRDS+" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intRPTWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); intLINECT +=1;
				  for(i=1;i<arrRPTHDR.length;i++)
			{
				O_DOUT.writeBytes(padSTRING(arrRPTHDR_PAD[i],arrRPTHDR[i],arrRPTHDR_WD[i]/8)+" "); intRUNCL +=1;
				if(intRUNCL>intRPTCL)
					{O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrRPTHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;}
			}
			O_DOUT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtRPTHDR");}
	}
	
	

	
	
	/**
	 */
	private void crtRPTRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			 if(M_rdbHTML.isSelected())
			    {
			        
			        O_DOUT.writeBytes("<HTML><HEAD><Title>Vehicle Detention Summary</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
			        O_DOUT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
			        O_DOUT.writeBytes("</STYLE>");
				 }
			 else
			     prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72;

			int i=0, j = tblRPTRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtRPTHDR();
			for(i=0;i<tblRPTRPT.getRowCount();i++)
			{
				if(tblRPTRPT.getValueAt(i,intTB1_TRPNM).toString().equals(""))
					break;
				else if(tblRPTRPT.getValueAt(i,intTB1_LRYNO).toString().length()==0)
					break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrRPTHDR_PAD[k],tblRPTRPT.getValueAt(i,k).toString().trim(),arrRPTHDR_WD[k]/8)+" "); intRUNCL += 1;
					if(intRUNCL>intRPTCL)
					{
						O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrRPTHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						
					}
				}
				O_DOUT.writeBytes("\n\n");intLINECT +=2;
				if(intLINECT>60) crtRPTHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n"); intLINECT +=2;
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtRPTRPT");}
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
			tbpMAIN.remove(pnlRPTRPT);
			pnlRPTRPT.removeAll();

			pnlRPTRPT = new JPanel(null);
			
			int i;
			M_strSQLQRY = "select count(*) vr_recct from fg_vrtrn where "+strWHRSTR;
			ResultSet rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next()  || rstRSSET==null)
				{setMSG("No Detail Records Found",'E');return false;}
			int L_intRECCT = Integer.parseInt(getRSTVAL(rstRSSET,"VR_RECCT","N"));
			L_intRECCT = 3*L_intRECCT;
			rstRSSET.close();
			//System.out.println("004");
			tblRPTRPT = crtTBLPNL1(pnlRPTRPT,arrRPTHDR,L_intRECCT,2,1,9.1,7.9,arrRPTHDR_WD,new int[]{0});    //,"Amount"
			tbpMAIN.addTab("Vehicle Rejection Details",pnlRPTRPT);
		
			//System.out.println("005");
			tblRPTRPT.addMouseListener(this);
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
		//System.out.println("LP_STRDT : "+LP_STRDT+"  LP_ENDDT : "+LP_ENDDT+"   strSBSSTR : "+strSBSSTR);
		String L_strRETSTR = "";
		if (LP_STRDT==null || LP_ENDDT==null)
			return L_strRETSTR;
		try
		{
			System.out.println("002");
			L_strRETSTR = "  VR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND vr_rejdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'";
			//System.out.println("003");
			//System.out.println(L_strRETSTR);
		}
		catch (Exception L_EX){setMSG(L_EX,"setWHRSTR");}
		System.out.println("004");
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
	        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl <> 'X' and SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54')";
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
			//hstDSRNM.clear();
            L_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'T' and pt_prtcd in (select distinct vr_trpcd from fg_vrtrn where "+strWHRSTR+")";
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
					//if(strPRTTP.equals("D"))
					//	{hstDSRNM.put(getRSTVAL(L_rstRSSET,"PT_SHRNM","C"),strPRTCD);}
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
private void setCMBVL(JComboBox LP_CMBNM, Vector LP_VTRNM)
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