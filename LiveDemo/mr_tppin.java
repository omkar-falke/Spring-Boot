/*
System Name   : Marketing System
Program Name  : 
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 13/10/2006"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
/**
 * 

Program Name : Xport Proforma Invoice Entry 

Purpose : This Program is used to adition ,Deletion And Modification of Xport Performa Invoice Details .


List of tables used :
Table Name     Primary key                                 Operation done
                                                            Insert   Update   Query   Delete	
---------------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD						            #
CO_PTMST       PT_PRTTP,PT_PRTCD									            #
MR_PIMST       PI_MKTTP,PI_PINNO							  #        #        #
MR_PITRN       PIT_MKTTP,PIT_PINNO,PIT_LADNO,PIT_PRDCD        #        #        #
MR_IVTRN       IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP                 #        #
--------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtBYRCD    IVT_BYRCD       MR_IVTRN      VARCHAR(5)     Buyer Code
txtPINNO    PI_PINNO        MR_PITRN      VARCHAR(8)     Invoice Number
--------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
       In this programe we are using prepared Statment And SCROLL_SENSITIVE,ResultSet.
       we have to check if record is already exist then it update otherwise it will insert
       the new recod.

  * 
 */


import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import javax.swing.JPanel;import javax.swing.JButton;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable;
//import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import javax.swing.JComponent;import javax.swing.JOptionPane;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.InputVerifier;
import java.awt.Dimension;
import java.io.File;
import java.util.Calendar;

public class mr_tppin extends cl_pbase
{
	private JTextField txtBYRCD,txtBYRDS,txtCNSCD,txtCNSDS,txtMKTTP,txtPINNO,txtPINDT,txtPLDDS,txtPDSDS;
	private JTextField txtCNTFD,txtCURCD,txtADLNO,txtADLDT,txtMRKSH,txtVSLNO,txtBYRRF,txtOTHRF,txtPMTTM;
	private JTextField txtFSPNO,txtFDSDS,txtPRDVL,txtENBED,txtDSBED,txtCURDS,txtDELTM,txtDSCVL;
	
	private String strMKTTP,strBYRCD,strCNSCD,strPINNO,strPINDT,strPLDDS,strPDSDS,strCNTFD,strCURCD;
	private String strFDSDS,strADLNO,strADLDT,strMRKSH,strVSLNO,strBYRRF,strOTHRF,strPMTTM,strFSPNO,strDSCVL;
	private String strLADNO,strPRDQT,strPKGWT,strPRDPK,strPRDRT,strPRDVL,strCNTDS,strPRDDS;
	private String strCNTTP,strTSLNO,strCSLNO,strCNSDS,strPRDCD,strPKGTP;
	private String strCURDS;
	

	String strDELTM,strREQQT,strLADQT;
	
	String LM_RESFIN = cl_dat.M_strREPSTR_pbst;
	//String strLFTMRG, strLFTMRG_10, strVLINE, strVLINE1;
	//String LM_RESSTR = LM_RESFIN.trim().concat("\\mr_tppin.doc"); 
	
	PreparedStatement pstPIMST,pstPITRN,pstIVTRN;
	boolean flgPREPN;
	private JCheckBox chkOLDANNX;
	
	private ButtonGroup btgRETCPL;              // ButtonGroup for adding Radio Button
	private JRadioButton rdbPREPN;              //JRadioButton for Transaction Date Selection
	private JRadioButton rdbPLNPN;              //JRadioButton for Lot Number Selection
	
	private JButton btnPRINT;   
	int TBL_CHKFL = 0; 
	int TBL_LADNO = 1; 
	int TBL_PRDDS = 2; 
	int TBL_CNTDS = 3; 
	int TBL_CNTTP = 4; 
	int TBL_TSLNO = 5; 
	int TBL_CSLNO = 6; 
	int TBL_PRDQT = 7; 
	int TBL_PRDPK = 8; 
	int TBL_PRDRT = 9; 
	int TBL_PRDVL = 10;
	JOptionPane LM_OPTNPN;
	
	private Hashtable<String,String> hstPRDCD,hstPKGTP,hst1to100;
	//private Hashtable hstCNTDS;	
	//ResultSet LM_RSLSET;
		
	private cl_JTable tblLODDTL;
	mr_tppin()
	{
		super(2);
		try
		{	
			btgRETCPL=new ButtonGroup();
			//txtDSBED = new JTextField();
			hstPRDCD = new Hashtable<String,String>();
			hstPKGTP = new Hashtable<String,String>();
			setMatrix(21,7);
			
			add(new JLabel("Product Type"),1,1,1,1,this,'L');
			add(txtMKTTP= new TxtLimit(2),2,1,1,1,this,'L');
			//System.out.println("KK");
			add(new JLabel("Buyer Code"),1,2,1,1,this,'L');
			add(txtBYRCD = new TxtLimit(5),2,2,1,1,this,'L');
			//System.out.println("KK");
			add(new JLabel("Buyer Description"),1,3,1,2,this,'L');
			add(txtBYRDS = new TxtLimit(40),2,3,1,2,this,'L');
			
			add(new JLabel("Consignee Code"),1,5,1,1,this,'L');
			add(txtCNSCD = new TxtLimit(5),2,5,1,1,this,'L');
			
			add(new JLabel("Consignee Description"),1,6,1,2,this,'L');
			add(txtCNSDS = new TxtLimit(40),2,6,1,2,this,'L');
			
			add(new JLabel("Prve Inv. No"),3,1,1,1,this,'L');
			add(txtPINNO = new TxtLimit(8),4,1,1,1,this,'L');
			
			add(new JLabel("Date"),3,2,1,1,this,'L');
			add(txtPINDT = new TxtDate(),4,2,1,1,this,'L');
			
			add(new JLabel("Port of Loading"),3,3,1,1,this,'L');
			add(txtPLDDS = new TxtLimit(20),4,3,1,1,this,'L');
			
			add(new JLabel("Port of discharge"),3,4,1,1,this,'L');
			add(txtPDSDS = new TxtLimit(20),4,4,1,1,this,'L');
			
			add(new JLabel("Country Code"),3,5,1,1,this,'L');
			add(txtCNTFD = new TxtLimit(3),4,5,1,1,this,'L');
			
			add(new JLabel("Currency Code"),3,6,1,1,this,'L');
			add(txtCURCD = new TxtLimit(2),4,6,1,1,this,'L');
			
			add(new JLabel("Final Destination"),3,7,1,1,this,'L');
			add(txtFDSDS = new TxtLimit(20),4,7,1,1,this,'L');
			
			add(new JLabel("Adv License NO."),5,1,1,1,this,'L');
			add(txtADLNO = new TxtLimit(20),6,1,1,1,this,'L');
			
			add(new JLabel("Date"),5,2,1,1,this,'L');
			add(txtADLDT = new TxtDate(),6,2,1,1,this,'L');
			
			add(new JLabel("Shipping Mark"),5,3,1,2,this,'L');
			add(txtMRKSH= new TxtLimit(20),6,3,1,2,this,'L');
			
			add(new JLabel("Vissel No."),5,5,1,2,this,'L');
			add(txtVSLNO = new TxtLimit(20),6,5,1,3,this,'L');
			
			add(new JLabel("Buyer Reference No."),7,1,1,2,this,'L');
			add(txtBYRRF = new TxtLimit(20),8,1,1,2,this,'L');
			
			add(new JLabel("Other Reference"),7,3,1,1,this,'L');
			add(txtOTHRF = new TxtLimit(30),8,3,1,1,this,'L');
			
			add(new JLabel("Payment"),7,4,1,1,this,'L');
			add(txtPMTTM = new TxtLimit(80),8,4,1,1,this,'L');
			
			add(new JLabel("Delivery"),7,5,1,1,this,'L');
			add(txtDELTM = new TxtLimit(80),8,5,1,1,this,'L');
						
			add(new JLabel("FSP NO"),7,6,1,2,this,'L');
			add(txtFSPNO = new TxtLimit(45),8,6,1,2,this,'L');
			
			add(new JLabel("Discount"),9,1,1,1,this,'L');
			add(txtDSCVL = new TxtNumLimit(10.2),9,2,1,1,this,'L');
			
			add(chkOLDANNX = new JCheckBox("Old Annx."),10,4,1,2,this,'L');
			add(new JLabel("Loading Advice Details"),10,1,1,2,this,'L');
			add(rdbPREPN = new JRadioButton("PrePrinted",true),9,3,1,1,this,'L');
			add(rdbPLNPN = new JRadioButton("Plain",false),10,3,1,1,this,'L');
					
			btgRETCPL.add(rdbPREPN);
			btgRETCPL.add(rdbPLNPN);
			add(btnPRINT = new JButton("PRINT"),9,4,1,1,this,'L');
			
			add(new JLabel("Currancy"),9,6,1,1,this,'L');
			add(txtCURDS = new TxtLimit(20),10,6,1,1,this,'L');
			
			add(new JLabel("Total value"),9,7,1,1,this,'L');
			add(txtPRDVL = new TxtLimit(20),10,7,1,1,this,'L');
						
			String[] L_strTBLHD = {"Status","L.A No.","Grade","Container","Type","Shp.Seal No.","C.Ex.Seal No","Qty.","Pkgs.","Rate","Amount"};
			int[] L_intCOLSZ = {30,60,100,100,50,60,80,60,60,65,80};
			
			tblLODDTL = crtTBLPNL1(this,L_strTBLHD,100,11,1,8,7,L_intCOLSZ,new int[]{0});
			setENBL(false);
			//crtHST();
		}
		catch(Exception E)
		{
			setMSG(E,"Constructor");
		}
	}
	/**
	 * Super Class metdhod overrided to inhance its funcationality, to enable disable 
	 * components according to requriement.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtBYRDS.setEnabled(false);
		txtCNSCD.setEnabled(false);	
		txtCNSDS.setEnabled(false);	
		//txtFDSDS.setEnabled(false);	
		txtCURDS.setEnabled(false);	
		txtPRDVL.setEnabled(false);
		btnPRINT.setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_LADNO].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_PRDDS].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_CNTDS].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_CNTTP].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_TSLNO].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_PRDQT].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_PRDPK].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_PRDRT].setEnabled(false);
		tblLODDTL.cmpEDITR[TBL_PRDVL].setEnabled(false);
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			strMKTTP = txtMKTTP.getText().toString().trim();
			strBYRCD = txtBYRCD.getText().toString().trim();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				txtMKTTP.setEnabled(true);
				txtBYRCD.setEnabled(true);
				txtPINNO.setEnabled(true);
			}
			if(M_objSOURC==btnPRINT)
			{
				Runtime r = Runtime.getRuntime();
				flgPREPN=true;
				Process p = null;
				try
				{
					mr_rppin objTPLAD = new mr_rppin();
					strPINNO=txtPINNO.getText().trim();
					strBYRCD=txtBYRCD.getText().trim();
					if(!rdbPREPN.isSelected())
						flgPREPN=false;
					
					int L_intSELOPT = LM_OPTNPN.showConfirmDialog(this,"Do you want to print on front side.","Print Status",JOptionPane.YES_NO_OPTION);
					if(L_intSELOPT == 0)
					{
						String strPRDTP = txtMKTTP.getText().trim();
						objTPLAD.prnALLREC(strPRDTP,strBYRCD,strPINNO,flgPREPN);
						JComboBox L_cmbLOCAL = objTPLAD.getPRNLS();
						objTPLAD.doPRINT(cl_dat.M_strREPSTR_pbst+"mr_tppin.doc",L_cmbLOCAL.getSelectedIndex());
						//p  = r.exec("c:\\windows\\wordpad.exe "+cl_dat.M_strREPSTR_pbst+"mr_tppin.doc"); 

					}
					L_intSELOPT = LM_OPTNPN.showConfirmDialog(this,"Do you want to print on back side.","Print Status",JOptionPane.YES_NO_OPTION);
					if(L_intSELOPT == 0)
					{
						if(chkOLDANNX.isSelected())
							objTPLAD.prnCERREC_OLD(strBYRCD,strPINNO);
						else
							objTPLAD.prnCERREC_NEW(strBYRCD,strPINNO);
						JComboBox L_cmbLOCAL = objTPLAD.getPRNLS();
						objTPLAD.doPRINT(cl_dat.M_strREPSTR_pbst+"mr_tppin1.doc",L_cmbLOCAL.getSelectedIndex());
					}
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"btnPRINT");
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"actionPerformed");
		}
	}

	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC==txtMKTTP)
				{
					M_strHLPFLD="txtMKTTP";
					String L_staHADER[] ={"Market Type","Description"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXMKT' ";
					if(txtMKTTP.getText().trim().length() >0)
						M_strSQLQRY += " and CMT_CODCD like '" + txtMKTTP.getText().trim() + "%' ";
					M_strSQLQRY += " order by CMT_CODCD";
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_staHADER,2,"CT");            
				}
				if(M_objSOURC==txtBYRCD)
				{
					M_strHLPFLD="txtBYRCD";
					String L_staHADER[]=null;
					
					if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						L_staHADER = new String[]{"Buyer Code","Description","L.A. No.","L.A.Date"};
						
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));       // Convert Into Local Date Format
						M_calLOCAL.add(Calendar.DATE,-7);									// Increase Date from +1 with Locked Date
						String L_strRETRN = M_fmtLCDAT.format(M_calLOCAL.getTime()); 
						
						M_strSQLQRY = "Select distinct IVT_BYRCD,PT_PRTNM,IVT_LADNO,CONVERT(varchar,IVT_LADDT,103) IVT_LADDT from MR_IVTRN,CO_PTMST where IVT_MKTTP = '"+strMKTTP+"'";
                        M_strSQLQRY += " and IVT_BYRCD = PT_PRTCD and IVT_SALTP = '12' and isnull(IVT_EPIFL,'Z') not in ('1')";
						//M_strSQLQRY += " and date(IVT_LADDT) >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						M_strSQLQRY += " and CONVERT(varchar,IVT_LADDT,103) >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strRETRN))+"'";
						M_strSQLQRY += " and PT_PRTTP='C' ";
						if(txtBYRCD.getText().trim().length()>0)
							M_strSQLQRY += " and IVT_BYRCD like '" + txtBYRCD.getText().trim() + "%' ";
						M_strSQLQRY += " Order by ivt_ladno desc";
						
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						L_staHADER  = new String[]{"Buyer Code","Description","L.A. No.","Prov. Inv. No"};
						M_strSQLQRY = "Select distinct PI_BYRCD,PT_PRTNM,PIT_LADNO,PI_PINNO from MR_PITRN,MR_PIMST,CO_PTMST where";
						M_strSQLQRY += " PI_MKTTP = PIT_MKTTP and PI_PINNO = PIT_PINNO and PI_MKTTP = '"+strMKTTP+"'";
						M_strSQLQRY += " and PI_BYRCD = PT_PRTCD and PT_PRTTP='C'";
						if(txtBYRCD.getText().trim().length()>0)
							M_strSQLQRY += " and PI_BYRCD like '" + txtBYRCD.getText().trim().toUpperCase() + "%' ";
						M_strSQLQRY += " Order by PI_PINNO desc";
					}
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_staHADER,4,"CT");            
				}
				
				if(M_objSOURC==txtPINNO)
				{
					M_strHLPFLD="txtPINNO";
					String L_staHADER[] ={"Prov. Invoice No.","L.A. No."};
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						M_strSQLQRY = "Select distinct PI_PINNO,PIT_LADNO from MR_PITRN,MR_PIMST where";
						M_strSQLQRY += " PI_MKTTP = PIT_MKTTP and PI_PINNO = PIT_PINNO and";
						M_strSQLQRY += " PI_MKTTP = '"+strMKTTP+"' and PI_BYRCD = '"+strBYRCD+"'";
						if(txtPINNO.getText().trim().length()>0)
							M_strSQLQRY += " and PI_PINNO like '" + txtPINNO.getText().trim() + "%' ";
						M_strSQLQRY += " order by pi_pinno";
						//System.out.println(" txtPINNO "+M_strSQLQRY);
						cl_hlp(M_strSQLQRY,1,1,L_staHADER,2,"CT");            
					}
				}
				if(M_objSOURC==txtPMTTM)
				{
					M_strHLPFLD="txtPMTTM";
					String L_staHADER[] ={"Delivery Type","Description"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXDTP'";
					//System.out.println(" txtPMTTM "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,2,L_staHADER,2,"CT");
				}
				
				if(M_objSOURC==txtCNTFD)
				{
					M_strHLPFLD="txtCNTFD";
					String L_staHADER[] ={"Country Code","Description"};
					
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXCNT'";
					
					//System.out.println("M_strSQLQRY"+M_strSQLQRY);
					
					cl_hlp(M_strSQLQRY,2,1,L_staHADER,2,"CT");
				}
				
				if(M_objSOURC==txtCURCD)
				{
					M_strHLPFLD="txtCURCD";
					String L_staHADER[] ={"Currency Code","Description"};
					
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXCUR'";
					//System.out.println("M_strSQLQRY"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_staHADER,2,"CT");
				}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtMKTTP)
				{
					txtBYRCD.requestFocus();
				}
				if(M_objSOURC==txtBYRCD)
				{
					txtPINNO.requestFocus();
				}
				if(M_objSOURC==txtPINNO)
				{
					if(txtPINNO.getText().trim().length()>0 && txtPINNO.getText().trim().length()<=8)
					{
						getALLDAT();						
					}
					else
					{
						setMSG("Please check the length of invoice no, it should not be zero or greater than 8 characters.",'E');
						txtPINNO.requestFocus();
					}
					
					//txtPINDT.requestFocus();
				}
				if(M_objSOURC==txtPINDT)
				{
					txtPLDDS.requestFocus();
				}
				if(M_objSOURC==txtPLDDS)
				{
					txtPDSDS.requestFocus();
				}
				
				
				
				if(M_objSOURC==txtPDSDS)
				{
					txtCNTFD.requestFocus();
				}
				if(M_objSOURC==txtCNTFD)
				{
					txtCURCD.requestFocus();
				}
				
				
				
				if(M_objSOURC==txtCNTFD)
				{
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXCNT' and CMT_CODCD = '"+txtCNTFD.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						txtCURCD.requestFocus();
						txtFDSDS.setText(nvlSTRVL(M_rstRSSET.getString("cmt_codds"),""));
						setMSG("Enter country code or press F1 key for help",'N');
					}
					else
					{
						txtCNTFD.requestFocus();
						setMSG("Invalid country code.Press F1 key for help",'E');
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();
				}
				if(M_objSOURC==txtCURCD)
				{
					M_strSQLQRY = "Select * from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXCUR' and CMT_CODCD = '"+txtCURCD.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						txtCURDS.setText(nvlSTRVL(M_rstRSSET.getString("cmt_codds"),""));
						txtFDSDS.requestFocus();
						setMSG("Enter final destination.",'N');
					}
					else
					{
						txtCURDS.setText("");
						txtCURCD.requestFocus();
						setMSG("Invalid currency code.Press F1 key for help",'E');
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();
				}
							
				if(M_objSOURC==txtFDSDS)
				{
					txtADLNO.requestFocus();
				}
				if(M_objSOURC==txtADLNO)
				{
					txtADLDT.requestFocus();
				}
				if(M_objSOURC==txtADLDT)
				{
					txtMRKSH.requestFocus();
				}
				if(M_objSOURC==txtMRKSH)
				{
					txtVSLNO.requestFocus();
				}
				if(M_objSOURC==txtVSLNO)
				{
					txtBYRRF.requestFocus();
				}
				if(M_objSOURC==txtBYRRF)
				{
					txtOTHRF.requestFocus();
				}
				if(M_objSOURC==txtOTHRF)
				{
					txtPMTTM.requestFocus();
				}
				//if(M_objSOURC==txtPMTTM)
				//{
				//	txtDELTM.requestFocus();
				//}
				if(M_objSOURC==txtDELTM)
				{
					txtFSPNO.requestFocus();
				}
				if(M_objSOURC==txtFSPNO)
				{
					txtDSCVL.requestFocus();
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"keyPressed");
		}
	}
	/**
	 * Method to execute F1 help for the selected field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtMKTTP"))
			{
				txtMKTTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtBYRCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtBYRCD.setText(L_STRTKN1.nextToken());
				//L_STRTKN1.nextToken();
				 txtBYRDS.setText(L_STRTKN1.nextToken());
				 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				 {
					 L_STRTKN1.nextToken();
					 txtPINNO.setText(L_STRTKN1.nextToken());
				 }
				 
				
			}
			if(M_strHLPFLD.equals("txtCNTFD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtCNTFD.setText(L_STRTKN1.nextToken());
				//L_STRTKN1.nextToken();
				 txtFDSDS.setText(L_STRTKN1.nextToken());
				
			}
			if(M_strHLPFLD.equals("txtCURCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtCURCD.setText(L_STRTKN1.nextToken());
				//L_STRTKN1.nextToken();
				 txtCURDS.setText(L_STRTKN1.nextToken());
				
			}
			
			if(M_strHLPFLD.equals("txtPINNO"))
			{
				txtPINNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception E)
		{
			setMSG(E," exeHLPOK ");
		}
	}
	
	private void getALLDAT()
	{
		try
		{
			//this.setCursor(curWTSTS);
			int L_CNT = 0;
			double L_PRDVL = 0;
			boolean L_INVFL = true;
			hstPRDCD.clear();
			hstPKGTP.clear();
			java.sql.Date L_datTMPDT1;
			strCNSCD = "";
			strCNSDS = "";
			strPINDT = cl_dat.M_strLOGDT_pbst;
			strPLDDS = "JNPT,INDIA";
			strPDSDS = "";
			strCNTFD = "";
			strCURCD = "";
			strADLNO = "";
			//strADLDT = cl_dat.M_LOGDAT;
			strADLDT = "";
			strMRKSH = "";
			strVSLNO = "";
			strBYRRF = "";
			strOTHRF = "ARE 1 No.";
			strPMTTM = "CFR";
			strDELTM = "";
			strFSPNO = "";
			strDSCVL = "";
			strCNTTP = (txtMKTTP.getText().equals("07") ? "02" : "01");
			strMKTTP=txtMKTTP.getText().trim();
			strBYRCD=txtBYRCD.getText().trim();
			strPINNO=txtPINNO.getText().trim();
			if(tblLODDTL.isEditing())
				tblLODDTL.getCellEditor().stopCellEditing();
			tblLODDTL.setColumnSelectionInterval(0,0);
			tblLODDTL.setRowSelectionInterval(0,0);
			M_strSQLQRY = " ";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY = "Select * from mr_pimst where pi_pinno='"+strPINNO+"'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					setMSG("Provisional Invoice Number already exists. Please enter non existing number.",'E');
					txtPINNO.requestFocus();
					L_INVFL = false;
					return;
					
				}
				M_rstRSSET.close();
				
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,-7);									// Increase Date from +1 with Locked Date
				String L_strRETRN = M_fmtLCDAT.format(M_calLOCAL.getTime()); 
				
				M_strSQLQRY = "Select ivt_cnscd,ivt_adlno,ivt_adldt,ivt_ladno,ivt_prdds,ivt_cntds,ivt_pkgwt,ivt_prdcd,ivt_ladqt,";
				M_strSQLQRY += "ivt_tslno,isnull(ivt_cslno,' ') ivt_cslno,ivt_reqqt,ivt_ladrt,(ivt_reqqt*ivt_ladrt) l_prdvl,ivt_curcd,ivt_pkgtp from mr_ivtrn";
                //M_strSQLQRY += " where ivt_mkttp = '"+strMKTTP+"' and ivt_saltp = '12' and ivt_byrcd = '"+strBYRCD+"' and isnull(IVT_EPIFL,'Z') not in ('1')";
                M_strSQLQRY += " where SUBSTRING(ivt_prdcd,1,2) "+(txtMKTTP.getText().equals("07") ? "='SX'" : "<>'SX'")+" and ivt_mkttp = '"+strMKTTP+"' and ivt_saltp = '12' and ivt_byrcd = '"+strBYRCD+"' and isnull(IVT_EPIFL,'Z') not in ('1')";
				M_strSQLQRY += " and CONVERT(varchar,IVT_LADDT,103) >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strRETRN))+"'";
				M_strSQLQRY += " and ivt_stsfl not in ('X')";
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				M_strSQLQRY = "Select pi_cnscd,pit_ladno,pit_prdds,pit_cntds,pit_cnttp,pit_tslno,isnull(pit_cslno,' ') pit_cslno,pit_prdqt,";
				M_strSQLQRY += "pit_prdpk,pit_prdrt,pit_prdvl,pi_adlno,pi_adldt,pi_curcd,pi_mrksh,pi_pindt,";
				M_strSQLQRY += "pi_fspno,pi_pldds,pi_pdsds,pi_fdsds,pi_othrf,pi_pmttm,pi_deltm,pi_vslno,pit_prdcd,";
				M_strSQLQRY += "pi_byrrf,pi_pinno,pi_cntfd,pi_dscvl from mr_pitrn,mr_pimst where pi_mkttp=pit_mkttp";
				//M_strSQLQRY += " and pi_pinno=pit_pinno and pi_byrcd = '"+strBYRCD+"' and pi_pinno='"+strPINNO+"'";
				M_strSQLQRY += " and pi_pinno=pit_pinno and pi_byrcd = '"+strBYRCD+"' and pi_pinno='"+strPINNO+"' and SUBSTRING(pit_prdcd,1,2) "+(txtMKTTP.getText().equals("07") ? "='SX'" : "<>'SX'");
			}
			if(L_INVFL)
			{
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					//if(cl_dat.ocl_dat.M_FLGOPT == 'A')
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						strCNSCD = nvlSTRVL(M_rstRSSET.getString("ivt_cnscd"),"");
						strCURCD = nvlSTRVL(M_rstRSSET.getString("ivt_curcd"),"");
						strADLNO = nvlSTRVL(M_rstRSSET.getString("ivt_adlno"),"");
						//strADLDT = nvlSTRVL(cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("ivt_adldt")),"");
						strADLDT = "";
					}
					//else if(cl_dat.ocl_dat.M_FLGOPT == 'M' || cl_dat.ocl_dat.M_FLGOPT == 'D')
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						strCNSCD = nvlSTRVL(M_rstRSSET.getString("pi_cnscd"),"");
						strPINNO = nvlSTRVL(M_rstRSSET.getString("pi_pinno"),"");
						//System.out.println("strPINNO = "+strPINNO);
						
						//strPINDT = nvlSTRVL(cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("pi_pindt")),"");
						L_datTMPDT1 = M_rstRSSET.getDate("pi_pindt");
						if(L_datTMPDT1 !=null)
							strPINDT=M_fmtLCDAT.format(L_datTMPDT1);
						else
							strPINDT="";
						strPLDDS = nvlSTRVL(M_rstRSSET.getString("pi_pldds"),"");
						strPDSDS = nvlSTRVL(M_rstRSSET.getString("pi_pdsds"),"");
						strCNTFD = nvlSTRVL(M_rstRSSET.getString("pi_cntfd"),"");
						strCURCD = nvlSTRVL(M_rstRSSET.getString("pi_curcd"),"");
						strADLNO = nvlSTRVL(M_rstRSSET.getString("pi_adlno"),"");
						//strADLDT = cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("pi_adldt"));
						L_datTMPDT1 = M_rstRSSET.getDate("pi_adldt");
						if(L_datTMPDT1 !=null)
						{
							strADLDT=M_fmtLCDAT.format(L_datTMPDT1);
						}
						else
							strADLDT="";
						strMRKSH = nvlSTRVL(M_rstRSSET.getString("pi_mrksh"),"");
						strVSLNO = nvlSTRVL(M_rstRSSET.getString("pi_vslno"),"");
						strBYRRF = nvlSTRVL(M_rstRSSET.getString("pi_byrrf"),"");
						strOTHRF = nvlSTRVL(M_rstRSSET.getString("pi_othrf"),"");
						strPMTTM = nvlSTRVL(M_rstRSSET.getString("pi_pmttm"),"");
						strDELTM = nvlSTRVL(M_rstRSSET.getString("pi_deltm"),"");
						strFSPNO = nvlSTRVL(M_rstRSSET.getString("pi_fspno"),"");
						strFDSDS = nvlSTRVL(M_rstRSSET.getString("pi_fdsds"),"");
						strDSCVL = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("pi_dscvl"),"0.00")),2);
					}
					if(strFSPNO.trim().length()<2)
						strFSPNO = "S/6 FSP 1990/2005 EXP DT.14/06/2005";
					strCNSDS = getPRTNM(strCNSCD);
					strCURDS = cl_dat.getPRMCOD("cmt_codds","MST","COXXCUR",strCURCD);
					//System.out.println("1");
					txtCNSCD.setText(strCNSCD);
					txtCNSDS.setText(strCNSDS);
					//System.out.println("2");
					txtPINNO.setText(strPINNO);
					txtPINDT.setText(strPINDT);
					txtPLDDS.setText(strPLDDS);
					//System.out.println("KKKKKKKKKKKKKKKKKKKKKK 3 "+strPLDDS);
					txtPDSDS.setText(strPDSDS);
					txtCNTFD.setText(strCNTFD);
					txtCURCD.setText(strCURCD);
					//System.out.println("KKKKKKKKKKKKKKKKKKKKKK 4 "+strCURCD);
					txtFDSDS.setText(strFDSDS);
					txtADLNO.setText(strADLNO);
					txtADLDT.setText(strADLDT);
					txtMRKSH.setText(strMRKSH);
					txtVSLNO.setText(strVSLNO);
					txtBYRRF.setText(strBYRRF);
					txtOTHRF.setText(strOTHRF);
					txtPMTTM.setText(strPMTTM);
					txtDELTM.setText(strDELTM);
					txtCURDS.setText(strCURDS);
					txtFSPNO.setText(strFSPNO);
					txtDSCVL.setText(strDSCVL);
					//System.out.println("KKKKKKKKKKKKKKKKKKKKKKPPPPPPPPP");
					while(true)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							strLADNO = nvlSTRVL(M_rstRSSET.getString("ivt_ladno"),"");
							strPRDCD = nvlSTRVL(M_rstRSSET.getString("ivt_prdcd"),"");
							strPRDDS = nvlSTRVL(M_rstRSSET.getString("ivt_prdds"),"");
							strCNTDS = nvlSTRVL(M_rstRSSET.getString("ivt_cntds"),"");
							strTSLNO = nvlSTRVL(M_rstRSSET.getString("ivt_tslno"),"");
							strCSLNO = nvlSTRVL(M_rstRSSET.getString("ivt_cslno"),"");
							strLADQT = nvlSTRVL(M_rstRSSET.getString("ivt_ladqt"),"");
							strREQQT = nvlSTRVL(M_rstRSSET.getString("ivt_reqqt"),"");
							strPRDRT = nvlSTRVL(M_rstRSSET.getString("ivt_ladrt"),"");
							strPRDQT = strREQQT;
							if(Double.parseDouble(strLADQT) > 0)
								strPRDQT = strLADQT;
							//strPRDVL = setFMT(String.valueOf(Double.parseDouble(strPRDQT)*Double.parseDouble(strPRDRT)),2);
							strPRDVL = setNumberFormat((Double.parseDouble(strPRDQT)*Double.parseDouble(strPRDRT)),2);
							//padSTRING('L',setNumberFormat(L_dblDEBIT,0),15);
							
							strPKGWT = nvlSTRVL(M_rstRSSET.getString("ivt_pkgwt"),"");
							if(strPKGWT.equals("0.000"))
								strPKGWT = "1";
							//strPRDPK = String.valueOf(Integer.parseInt(setFMT(String.valueOf(Double.parseDouble(strPRDQT)/Double.parseDouble(strPKGWT)),0)));
							strPRDPK = String.valueOf(Integer.parseInt(setNumberFormat((Double.parseDouble(strPRDQT)/Double.parseDouble(strPKGWT)),0)));
							strPKGTP = nvlSTRVL(M_rstRSSET.getString("ivt_pkgtp"),"");
							hstPKGTP.put(String.valueOf(L_CNT),strPKGTP);
						}
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						{
							strLADNO = nvlSTRVL(M_rstRSSET.getString("pit_ladno"),"");
							strPRDCD = nvlSTRVL(M_rstRSSET.getString("pit_prdcd"),"");
							strPRDDS = nvlSTRVL(M_rstRSSET.getString("pit_prdds"),"");
							strCNTDS = nvlSTRVL(M_rstRSSET.getString("pit_cntds"),"");
							strCNTTP = nvlSTRVL(M_rstRSSET.getString("pit_cnttp"),"");
							strTSLNO = nvlSTRVL(M_rstRSSET.getString("pit_tslno"),"");
							strCSLNO = nvlSTRVL(M_rstRSSET.getString("pit_cslno"),"");
							strPRDQT = nvlSTRVL(M_rstRSSET.getString("pit_prdqt"),"");
							strPRDPK = nvlSTRVL(M_rstRSSET.getString("pit_prdpk"),"");
							strPRDRT = nvlSTRVL(M_rstRSSET.getString("pit_prdrt"),"");
							strPRDVL = nvlSTRVL(M_rstRSSET.getString("pit_prdvl"),"");
						}
						tblLODDTL.setValueAt(strLADNO,L_CNT,TBL_LADNO);
						tblLODDTL.setValueAt(strPRDDS,L_CNT,TBL_PRDDS);
						tblLODDTL.setValueAt(strCNTDS,L_CNT,TBL_CNTDS);
						tblLODDTL.setValueAt(strCNTTP,L_CNT,TBL_CNTTP);
						tblLODDTL.setValueAt(strTSLNO,L_CNT,TBL_TSLNO);
						tblLODDTL.setValueAt(strCSLNO,L_CNT,TBL_CSLNO);
						tblLODDTL.setValueAt(strPRDQT,L_CNT,TBL_PRDQT);
						tblLODDTL.setValueAt(strPRDPK,L_CNT,TBL_PRDPK);
						tblLODDTL.setValueAt(strPRDRT,L_CNT,TBL_PRDRT);
						tblLODDTL.setValueAt(strPRDVL,L_CNT,TBL_PRDVL);
						hstPRDCD.put(String.valueOf(L_CNT),strPRDCD);
						L_CNT++;
						L_PRDVL += Double.parseDouble(strPRDVL);
						if(!M_rstRSSET.next())
							break;
					}
					txtBYRCD.setEnabled(false);	  
					//txtPRDVL.setText(setFMT(String.valueOf(L_PRDVL),2));
					txtPRDVL.setText(setNumberFormat(L_PRDVL,2));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						btnPRINT.setEnabled(true);
						txtPINNO.setEnabled(false);
						txtPINDT.requestFocus();
						setMSG("Enter provisional invoice date.",'N');
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						btnPRINT.setEnabled(false);
						txtPINNO.setEnabled(false);
						txtPINDT.requestFocus();
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtPINDT.requestFocus();
						setMSG("Enter provisional invoice date.",'N');
					}
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			//this.setCursor(curDFSTS);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLDAT");
		}
	}
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
    public boolean vldDATA()
	{
		String strTEMP=txtBYRCD.getText().trim();
		if(strTEMP.length()==0)
		{
			setMSG("Please Enter Buyer Code ",'E');
			txtBYRCD.requestFocus();
			return false;
		}
		if(txtPINNO.getText().trim().length()==0)
		{
			setMSG("Please Enter Invoice Number ..",'E');
			txtPINNO.requestFocus();
			return false;
		}
		String L_strSILNO="";
		int L_intCOUNT=0;
		if(tblLODDTL.isEditing())
			tblLODDTL.getCellEditor().stopCellEditing();
		tblLODDTL.setColumnSelectionInterval(0,0);
		tblLODDTL.setRowSelectionInterval(0,0);
		
		
		for(int i=0;i<tblLODDTL.getRowCount();i++)
		{
			if(tblLODDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_intCOUNT++;
				L_strSILNO=tblLODDTL.getValueAt(i,TBL_CSLNO).toString();
				if(L_strSILNO.length()==0)
				{
					setMSG("C.EX seal No can Not be Blank",'E');
					return false;
				
				}
			}
		}
		//if(L_intCOUNT==0)
		//{
		//	setMSG("No Row is Selected ",'E');
		//}
		return true;
	}
	
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	*/
	
	public void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			//dblADJVL=0.0;
			if(!vldDATA())
			{
				return;
			}
			
			strMKTTP = txtMKTTP.getText().toString().trim();
			strBYRCD = txtBYRCD.getText().toString().trim();
			strCNSCD = txtCNSCD.getText().toString().trim();
			strPINNO = txtPINNO.getText().toString().trim();
			strPINDT = txtPINDT.getText().toString().trim();
			strPLDDS = txtPLDDS.getText().toString().trim();
			strPDSDS = txtPDSDS.getText().toString().trim();
			strCNTFD = txtCNTFD.getText().toString().trim();
			strCURCD = txtCURCD.getText().toString().trim();
			strFDSDS = txtFDSDS.getText().toString().trim();
			strADLNO = txtADLNO.getText().toString().trim();
			strADLDT = txtADLDT.getText().toString().trim();
			strMRKSH = txtMRKSH.getText().toString().trim();
			strVSLNO = txtVSLNO.getText().toString().trim();
			strBYRRF = txtBYRRF.getText().toString().trim();
			strOTHRF = txtOTHRF.getText().toString().trim();
			strPMTTM = txtPMTTM.getText().toString().trim();
			strDELTM = txtDELTM.getText().toString().trim();
			strFSPNO = txtFSPNO.getText().toString().trim();
			strDSCVL = txtDSCVL.getText().toString().trim();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				delPINREC(strMKTTP,strPINNO);
				//this.setCursor(curDFSTS);
				return;
			}
			else 
			{
				//L_PINDT = cc_dattm.setSQLDAT(strPINDT);
				//L_ADLDT = cc_dattm.setSQLDAT(strADLDT);
				pstPIMST = cl_dat.M_conSPDBA_pbst.prepareStatement("Select * from mr_pimst where pi_mkttp = ? and pi_pinno = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				pstPIMST.setString(1,strMKTTP);
				pstPIMST.setString(2,strPINNO);
				M_rstRSSET = pstPIMST.executeQuery();
				if(M_rstRSSET.next())
				{
					M_rstRSSET.updateDate("PI_PINDT",setSQLDAT1(strPINDT));
					M_rstRSSET.updateString("PI_BYRCD",strBYRCD);
					M_rstRSSET.updateString("PI_CNSCD",strCNSCD);
					M_rstRSSET.updateString("PI_CURCD",strCURCD);
					M_rstRSSET.updateString("PI_ADLNO",strADLNO);
					M_rstRSSET.updateDate("PI_ADLDT",setSQLDAT1(strADLDT));
					M_rstRSSET.updateString("PI_MRKSH",strMRKSH);
					M_rstRSSET.updateString("PI_FSPNO",strFSPNO);
					M_rstRSSET.updateString("PI_PLDDS",strPLDDS);
					M_rstRSSET.updateString("PI_PDSDS",strPDSDS);
					M_rstRSSET.updateString("PI_FDSDS",strFDSDS);
					M_rstRSSET.updateString("PI_CNTFD",strCNTFD);
					M_rstRSSET.updateString("PI_OTHRF",strOTHRF);
					M_rstRSSET.updateString("PI_PMTTM",strPMTTM);
					M_rstRSSET.updateString("PI_DELTM",strDELTM);
					M_rstRSSET.updateString("PI_VSLNO",strVSLNO);
					M_rstRSSET.updateString("PI_BYRRF",strBYRRF);
					M_rstRSSET.updateDouble("PI_DSCVL",Double.parseDouble(strDSCVL));
					M_rstRSSET.updateRow();
				}
				else
				{
					M_rstRSSET.moveToInsertRow();
					M_rstRSSET.updateString("PI_MKTTP",strMKTTP);
					M_rstRSSET.updateString("PI_PINNO",strPINNO);
					M_rstRSSET.updateString("PI_LADRF","");
					M_rstRSSET.updateDate("PI_PINDT",setSQLDAT1(strPINDT));
					M_rstRSSET.updateString("PI_BYRCD",strBYRCD);
					M_rstRSSET.updateString("PI_CNSCD",strCNSCD);
					M_rstRSSET.updateString("PI_CURCD",strCURCD);
					M_rstRSSET.updateString("PI_ADLNO",strADLNO);
					M_rstRSSET.updateDate("PI_ADLDT",setSQLDAT1(strADLDT));
					M_rstRSSET.updateString("PI_MRKSH",strMRKSH);
					M_rstRSSET.updateString("PI_FSPNO",strFSPNO);
					M_rstRSSET.updateString("PI_PLDDS",strPLDDS);
					M_rstRSSET.updateString("PI_PDSDS",strPDSDS);
					M_rstRSSET.updateString("PI_FDSDS",strFDSDS);
					M_rstRSSET.updateString("PI_CNTFD",strCNTFD);
					M_rstRSSET.updateString("PI_OTHRF",strOTHRF);
					M_rstRSSET.updateString("PI_PMTTM",strPMTTM);
					M_rstRSSET.updateString("PI_DELTM",strDELTM);
					M_rstRSSET.updateString("PI_VSLNO",strVSLNO);
					M_rstRSSET.updateString("PI_BYRRF",strBYRRF);
					M_rstRSSET.updateDouble("PI_DSCVL",Double.parseDouble(strDSCVL));
					M_rstRSSET.insertRow();
					M_rstRSSET.moveToCurrentRow();
				}
				pstPIMST.clearParameters();
				cl_dat.M_conSPDBA_pbst.commit();
				M_rstRSSET.close();
				pstPIMST.close();
				//pstPITRN = cl_dat.M_conSPDBA_pbst.prepareStatement("Select * from mr_pitrn where pit_mkttp = ? and pit_pinno = ? and pit_ladno = ? and pit_prdcd = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				pstPITRN = cl_dat.M_conSPDBA_pbst.prepareStatement("Select * from mr_pitrn where pit_mkttp = ? and pit_pinno = ? and pit_ladno = ? and pit_prdcd = ? and SUBSTRING(pit_prdcd,1,2) "+(txtMKTTP.getText().equals("07") ? "='SX'" : "<>'SX'"),ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				for(int i = 0;i < tblLODDTL.getRowCount();i++)
				{ 
					if(tblLODDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						strLADNO = tblLODDTL.getValueAt(i,TBL_LADNO).toString().trim();
						strPRDCD = hstPRDCD.get(String.valueOf(i)).toString();
						strPRDDS = tblLODDTL.getValueAt(i,TBL_PRDDS).toString().trim();
						strCNTDS = tblLODDTL.getValueAt(i,TBL_CNTDS).toString().trim();
						strCNTTP = tblLODDTL.getValueAt(i,TBL_CNTTP).toString().trim();
						strTSLNO = tblLODDTL.getValueAt(i,TBL_TSLNO).toString().trim();
						strCSLNO = tblLODDTL.getValueAt(i,TBL_CSLNO).toString().trim();
						strPRDQT = tblLODDTL.getValueAt(i,TBL_PRDQT).toString().trim();
						strPRDPK = tblLODDTL.getValueAt(i,TBL_PRDPK).toString().trim();
						strPRDRT = tblLODDTL.getValueAt(i,TBL_PRDRT).toString().trim();
						strPRDVL = tblLODDTL.getValueAt(i,TBL_PRDVL).toString().trim();
						pstPITRN.setString(1,strMKTTP);
						pstPITRN.setString(2,strPINNO);
						pstPITRN.setString(3,strLADNO);
						pstPITRN.setString(4,strPRDCD);
						M_rstRSSET = pstPITRN.executeQuery();
						if(M_rstRSSET.next())
						{
							M_rstRSSET.updateString("PIT_PRDDS",strPRDDS);
							M_rstRSSET.updateString("PIT_CNTDS",strCNTDS);
							M_rstRSSET.updateString("PIT_TSLNO",strTSLNO);
							M_rstRSSET.updateString("PIT_CSLNO",strCSLNO);
							M_rstRSSET.updateDouble("PIT_PRDQT",Double.parseDouble(strPRDQT));
							M_rstRSSET.updateInt("PIT_PRDPK",Integer.parseInt(strPRDPK));
							M_rstRSSET.updateDouble("PIT_PRDRT",Double.parseDouble(strPRDRT));
							M_rstRSSET.updateDouble("PIT_PRDVL",Double.parseDouble(strPRDVL));
							M_rstRSSET.updateString("PIT_CNTTP",strCNTTP);
							M_rstRSSET.updateRow();
						}
						else
						{
							M_rstRSSET.moveToInsertRow();
							M_rstRSSET.updateString("PIT_MKTTP",strMKTTP);
							M_rstRSSET.updateString("PIT_PINNO",strPINNO);
							M_rstRSSET.updateString("PIT_LADNO",strLADNO);
							M_rstRSSET.updateString("PIT_PRDCD",strPRDCD);
							M_rstRSSET.updateString("PIT_PRDDS",strPRDDS);
							M_rstRSSET.updateString("PIT_CNTDS",strCNTDS);
							M_rstRSSET.updateString("PIT_TSLNO",strTSLNO);
							M_rstRSSET.updateString("PIT_CSLNO",strCSLNO);
							M_rstRSSET.updateDouble("PIT_PRDQT",Double.parseDouble(strPRDQT));
							M_rstRSSET.updateInt("PIT_PRDPK",Integer.parseInt(strPRDPK));
							M_rstRSSET.updateDouble("PIT_PRDRT",Double.parseDouble(strPRDRT));
							M_rstRSSET.updateDouble("PIT_PRDVL",Double.parseDouble(strPRDVL));
							M_rstRSSET.updateString("PIT_CNTTP",strCNTTP);
							M_rstRSSET.insertRow();
							M_rstRSSET.moveToCurrentRow();
						}
						pstPITRN.clearParameters();
						cl_dat.M_conSPDBA_pbst.commit();
						M_rstRSSET.close();
					}
				}
				pstPITRN.close();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					//pstIVTRN = cl_dat.M_conSPDBA_pbst.prepareStatement("Update mr_ivtrn set ivt_epifl=? where ivt_mkttp = ? and ivt_ladno = ? and ivt_prdcd = ? and ivt_pkgtp = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
					pstIVTRN = cl_dat.M_conSPDBA_pbst.prepareStatement("Update mr_ivtrn set ivt_epifl=? where ivt_mkttp = ? and ivt_ladno = ? and ivt_prdcd = ? and ivt_pkgtp = ? and SUBSTRING(ivt_prdcd,1,2) "+(txtMKTTP.getText().equals("07") ? "='SX'" : "<>'SX'"),ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
					for(int i = 0;i < tblLODDTL.getRowCount();i++)
					{ 
						if(tblLODDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							strLADNO = tblLODDTL.getValueAt(i,TBL_LADNO).toString().trim();
							strPRDCD = hstPRDCD.get(String.valueOf(i)).toString();
							strPKGTP = hstPKGTP.get(String.valueOf(i)).toString();
							pstIVTRN.setString(1,"1");
							pstIVTRN.setString(2,strMKTTP);
							pstIVTRN.setString(3,strLADNO);
							pstIVTRN.setString(4,strPRDCD);
							pstIVTRN.setString(5,strPKGTP);
							pstIVTRN.addBatch();
						}
					}
					pstIVTRN.executeBatch();
					//cl_dat.M_conSPDBA_pbst.commit();
					M_rstRSSET.close();
					pstIVTRN.close();
				}
				
				if(cl_dat.M_flgLCUPD_pbst)
				{
					btnPRINT.setEnabled(true);
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Saved Successfully ",'N');
						exeEXPINTMN(strMKTTP,strPINNO);
					}
				}
				else
				setMSG("Error In Saving",'E');	
				
			//	this.setCursor(curDFSTS);
			}
			
			
		}
		catch(Exception E)
		{
			setMSG(E,"ExeSAVE");
		}
	}
	/**
	 * Function for Converting Simple date format into Data bese format
	*/
	public java.sql.Date setSQLDAT1(String LP_DATFMT)
	{
		java.sql.Date L_SQLDAT = new java.sql.Date(1234);
		try
		{
			if(LP_DATFMT != null && LP_DATFMT.length() == 10)
			{
				String L_strYEAR = LP_DATFMT.substring(6,10).trim();
				String L_strMONTH = LP_DATFMT.substring(3,5).trim();
				String L_strDAY = LP_DATFMT.substring(0,2).trim();
				L_SQLDAT = java.sql.Date.valueOf(L_strYEAR+"-"+L_strMONTH+"-"+L_strDAY);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setSQLDAT1");
		}
		return L_SQLDAT;
	}
	/**
	 * Function to delate Invoice Records
	 */
	private void delPINREC(String LP_MKTTP, String LP_PINNO)
	{
		try
		{
			M_strSQLQRY = "update mr_ivtrn set ivt_epifl='0' where ivt_ladno in (select pit_ladno from mr_pitrn where pit_mkttp='"+LP_MKTTP+"' and pit_pinno = '"+LP_PINNO+"')";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");                   
			M_strSQLQRY = "delete from mr_pitrn where pit_mkttp='"+LP_MKTTP+"' and pit_pinno = '"+LP_PINNO+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");                   
			M_strSQLQRY = "delete from mr_pimst where pi_mkttp='"+LP_MKTTP+"' and pi_pinno = '"+LP_PINNO+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");                   
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					
					setMSG("Deleted Successfully ",'N');
				}
			}
			else
				setMSG("Error In Deletion",'E');	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"delPINREC");
		}
	}
	
	
	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	*/
	private String getRSTVAL(ResultSet P_rstRSSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
			if (LP_FLDTP.equals("C"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? P_rstRSSET.getString(LP_FLDNM).toString() : "";
			//return P_rstRSSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString()," ")) : "";
			else if (LP_FLDTP.equals("N"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString(),"0") : "0";
			else if (LP_FLDTP.equals("D"))
				return P_rstRSSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(P_rstRSSET.getDate(LP_FLDNM)) : "";
			else if (LP_FLDTP.equals("T"))
				return P_rstRSSET.getTimestamp(LP_FLDNM) != null ? M_fmtLCDTM.format(P_rstRSSET.getTimestamp(LP_FLDNM)) : "";
			 //   return M_fmtLCDTM.parse(P_rstRSSET.getString(LP_FLDNM)));
			else 
				return " ";
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 

	/**
	 * Function for generating report
	 * 
	 */
	
	private void exeEXPINTMN(String LP_MKTTP,String LP_PINNO)
	{
		try
		{
			FileOutputStream fosREPORT;
			DataOutputStream dosREPORT;

			
			String L_strFILNM = LM_RESFIN.trim().concat("\\"+LP_PINNO+".html"); 
			String L_strPRTNM = "";
			fosREPORT = new FileOutputStream(L_strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			//fosREPORT = crtFILE(L_strFILNM);
			//dosREPORT = crtDTOUTSTR(fosREPORT);	
			// e-mail functionality start
		    String L_strSQLQRY = "SELECT PI_PINNO,PI_PINDT,PI_PDSDS,PI_BYRCD,PI_BYRRF,PI_OTHRF,PI_ADLNO,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM,isnull(PT_PINCD,' ') PT_PINCD from MR_PIMST,CO_PTMST where PI_MKTTP = '"+LP_MKTTP+"' and PI_PINNO = '"+LP_PINNO+"' and PT_PRTTP='C' and PT_PRTCD = PI_BYRCD";
			//System.out.println(L_strSQLQRY);
		    ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			//System.out.println(L_strSQLQRY);
			if(L_rstRSSET ==null || !L_rstRSSET.next())
				return;
			L_strPRTNM = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
			dosREPORT.writeBytes("<HTML><HEAD><Title> Export Intimation </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
			dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");
			dosREPORT.writeBytes("<H1><CENTRE>");
			dosREPORT.writeBytes("Export Intimation \n\n\n");
			dosREPORT.writeBytes("</H1></CENTRE>");
			dosREPORT.writeBytes(padSTRING('R',"Invoice No. & Date  : "+ getRSTVAL(L_rstRSSET,"PI_PINNO","C") + "    "+getRSTVAL(L_rstRSSET,"PI_PINDT","D"),55)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Port                : "+ getRSTVAL(L_rstRSSET,"PI_PDSDS","C") ,55)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Party               : "+ getRSTVAL(L_rstRSSET,"PT_PRTNM","C") ,55)+"\n");
			dosREPORT.writeBytes("                      "+padSTRING('R', getRSTVAL(L_rstRSSET,"PT_ADR01","C") ,55)+"\n");
			dosREPORT.writeBytes("                      "+padSTRING('R', getRSTVAL(L_rstRSSET,"PT_ADR02","C") ,55)+"\n");
			dosREPORT.writeBytes("                      "+padSTRING('R', getRSTVAL(L_rstRSSET,"PT_ADR03","C") ,55)+"\n");
			dosREPORT.writeBytes("                      "+padSTRING('R', getRSTVAL(L_rstRSSET,"PT_ADR04","C") ,55)+"\n");
			dosREPORT.writeBytes("                      "+padSTRING('R', getRSTVAL(L_rstRSSET,"PT_CTYNM","C")+" "+getRSTVAL(L_rstRSSET,"PT_PINCD","C") ,55)+"\n\n");
			dosREPORT.writeBytes(padSTRING('R',"Order No.           : "+ getRSTVAL(L_rstRSSET,"PI_BYRRF","C") ,55)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"ARE No.             : "+ getRSTVAL(L_rstRSSET,"PI_OTHRF","C") ,55)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"File No.            : "+ getRSTVAL(L_rstRSSET,"PI_ADLNO","C") ,55)+"\n\n");
			L_rstRSSET.close();
				
			dosREPORT.writeBytes("<HR>");
		    //L_strSQLQRY = "SELECT PIT_CNTDS, PIT_TSLNO,PIT_CNTTP,PIT_PRDCD,PIT_PRDQT,PIT_PRDPK, PIT_PRDVL,PR_PRDDS,IVT_PKGTP from MR_PITRN,CO_PRMST,MR_IVTRN where PIT_MKTTP = ivt_mkttp AND PIT_LADNO = IVT_LADNO and PIT_PRDCD = IVT_PRDCD and PIT_MKTTP = '"+LP_MKTTP+"' and PIT_PINNO = '"+LP_PINNO+"' and isnull(PIT_PRDQT,0) > 0  and PIT_PRDCD = PR_PRDCD";
		    L_strSQLQRY = "SELECT PIT_CNTDS, PIT_TSLNO,PIT_CNTTP,PIT_PRDCD,PIT_PRDQT,PIT_PRDPK, PIT_PRDVL,PR_PRDDS,IVT_PKGTP from MR_PITRN,CO_PRMST,MR_IVTRN where PIT_MKTTP = ivt_mkttp AND PIT_LADNO = IVT_LADNO and PIT_PRDCD = IVT_PRDCD and PIT_MKTTP = '"+LP_MKTTP+"' and PIT_PINNO = '"+LP_PINNO+"' and isnull(PIT_PRDQT,0) > 0  and PIT_PRDCD = PR_PRDCD and SUBSTRING(pr_prdcd,1,2) "+(txtMKTTP.getText().equals("07") ? "='SX'" : "<>'SX'");
			//System.out.println(L_strSQLQRY);
		    L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			dosREPORT.writeBytes(padSTRING('R',"Contr.No.",15)+padSTRING('R',"Seal No.",15)+padSTRING('R',"FCL",10)+padSTRING('R',"Grade",15)+padSTRING('L',"Grs.(MT)",10)+padSTRING('L',"Pkgs",10)+padSTRING('L',"Net(MT)",10)+padSTRING('L',"CIF/USD",10)+"\n\n");
			//System.out.println(L_strSQLQRY);
			//M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET == null || !L_rstRSSET.next())
				return;
			while(true)
			{
				double L_BAGWT = Double.parseDouble(getPRMCOD("cmt_chp01","SYS","FGXXPKG",getRSTVAL(L_rstRSSET,"IVT_PKGTP","C")))/1000;

				//String L_GRSQT = setFMT(String.valueOf(Double.parseDouble(getRSTVAL(L_rstRSSET,"PIT_PRDPK","N"))*L_BAGWT),3);
				String L_GRSQT = setNumberFormat((Double.parseDouble(getRSTVAL(L_rstRSSET,"PIT_PRDPK","N"))*L_BAGWT),3);
				
			    dosREPORT.writeBytes(padSTRING('R',getRSTVAL(L_rstRSSET,"PIT_CNTDS","C"),15)
									 +padSTRING('R',getRSTVAL(L_rstRSSET,"PIT_TSLNO","C"),15)
									  +padSTRING('R',(getRSTVAL(L_rstRSSET,"PIT_CNTTP","C").equals("01") ? "20 Ft." : "40 Ft."),8)
									  +padSTRING('R',getRSTVAL(L_rstRSSET,"PR_PRDDS","C"),15)
									  +padSTRING('L',L_GRSQT,12)
									  +padSTRING('L',getRSTVAL(L_rstRSSET,"PIT_PRDPK","C"),8)
									  +padSTRING('L',getRSTVAL(L_rstRSSET,"PIT_PRDQT","C"),12)
									  +padSTRING('L',getRSTVAL(L_rstRSSET,"PIT_PRDVL","C"),10)+"\n");
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
			dosREPORT.writeBytes("<hr>");
			dosREPORT.writeBytes("\n\n\nFor Supreme Petrochem Ltd.\n\n");
			dosREPORT.writeBytes("Commercial Department\n");
			dosREPORT.writeBytes("excise@spl.co.in \n");
			dosREPORT.close();
			fosREPORT.close();
			dosREPORT = null;
			fosREPORT = null;
			int L_SELOPT = JOptionPane.showConfirmDialog(this,"Do you want to mail the report?","E-Mail Status",JOptionPane.YES_NO_OPTION);
			
			if(L_SELOPT == 0)
			{
				cl_eml ocl_eml = new cl_eml();
				ocl_eml.setFRADR("excise@spl.co.in"); 
				ocl_eml.sendfile("EXT","jsp@hathway.com",L_strFILNM,LP_PINNO+" for "+L_strPRTNM," ");
				//ocl_eml.sendfile("EXT","sr_deshpande@spl.co.in",L_strFILNM,LP_PINNO+" for "+L_strPRTNM," ");
				//ocl_eml.sendfile("EXT","anilyeole@gmail.com",L_strFILNM,LP_PINNO+" for "+L_strPRTNM," ");
				ocl_eml.sendfile("EXT","manoj_chawan@spl.co.in",L_strFILNM,LP_PINNO+" for "+L_strPRTNM," ");
				setMSG("Report has been mailed succesfully",'N');
			}
		}
		catch(Exception e)
		{
			setMSG("Error in exeEXPINTMN "+e,'E');
			return;
		}		
	}
	
	
	public String getPRMCOD(String LP_FLDRTN, String LP_STRMGP, String LP_STRSGP, String LP_STRCOD)
	{
		String L_strTEMP="";
		ResultSet L_rstRSSET;
		M_strSQLQRY ="select " + LP_FLDRTN ;
		M_strSQLQRY +=" from CO_CDTRN where CMT_CGMTP = "+ "'" + LP_STRMGP.trim() +"'" ;
		M_strSQLQRY +=" AND CMT_CGSTP = " + "'" + LP_STRSGP.trim() + "'";
		M_strSQLQRY +=" AND CMT_CODCD = " + "'" + LP_STRCOD.trim() + "'";
		try
		{
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET.next())
				L_strTEMP = L_rstRSSET.getString(1);
			L_rstRSSET.close();
		}
		catch(Exception L_SE)
		{
			//System.out.println("Error in getPRMCOD : "+L_SE.toString()); 
			setMSG(L_SE,"getPRMCOD");
		}
	return L_strTEMP;
	}
	
	
	/**
 * method for getting Customer Name
 * 
*/
	public  String getPRTNM(String LP_PRTCD)
	{
		String L_PRTNM = "";
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where";
			M_strSQLQRY += " PT_PRTTP='C' and PT_PRTCD='"+LP_PRTCD+"'";
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET.next())
				L_PRTNM = L_rstRSSET.getString("PT_PRTNM"); 
			if(L_PRTNM != null)
				L_PRTNM = L_PRTNM.trim();
			else
				L_PRTNM = "";
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRTNM");
		}
		return L_PRTNM;
	}
	
}
