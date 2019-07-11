/*
System Name   : Material Management System
Program Desc. : Issue (M.I.N.) Entry
Author        : N.K. Virdi
Date          : 15/04/2004
Version       : MMS 2.0

Last Modified	: 03/09/2004 
Documented On	: 
*/

import javax.swing.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.MouseEvent;import java.awt.event.ItemEvent;
import java.util.Hashtable;import java.util.Vector;import java.util.StringTokenizer;import java.util.Calendar;
import java.sql.CallableStatement;import java.sql.ResultSet;
class mm_teist extends cl_pbase
{
	private ResultSet rstRSSET;
	private cl_JTable tblISSVL;
	private int intTBLCL,intTBLRW,intRWCNT,LM_OLDROW = 0,intKEYCD =0;
	private boolean flgRMKFL = false;		
	private JComboBox cmbMATTP,cmbPRTTP,cmbISSTP;
	private JTextField txtISSNO,txtISSDT,txtPREBY,txtAUTBY,txtREMDS,txtPREDT,txtPRETM,txtAUTDT,txtAUTTM,txtDPTCD,txtDPTDS,txtPRTCD,txtPRTNM,txtWORNO;
	private JTextField txtMATCD,txtLOCCD,txtUSGTP,txtEXCCT,txtEXCDS,txtCCTCD,txtREQQT,txtISSQT,txtBATNO;
	private JButton btnPRNT;
	private String strSTRTP,strISSNO,strISSDT,strDPTCD,strMATCD,strTAGNO,strMATTP,strPRTTP,strREQQT,strMATDS,strGRNNO,strOGRNO,strOBTNO;
	private String strISSQT,strISSRT,strUSGTP,strCCTCD,strBATNO,strPREBY,strPRDTM,strAUDTM;
	private String strSTSFL="",strLUSBY,strLUPDT,strUOMCD,strLOCCD,strREMDS,strEXCCT;
	private String strCALQT,strTEMP;
	private String strDMCCT = "99PRTH999";	// Dummy cost centre code Raw material Issue to plant	
	private TableInputVerifier TBLINPVF;
	private boolean flgSTKIT = false;
	private boolean flgCYLFL = false;
	private Hashtable<String,String> hstMATCD = new Hashtable<String,String>();
	private Hashtable<String,String> hstPRTTP = new Hashtable<String,String>();
	private Hashtable<String,String> hstISSQT = new Hashtable<String,String>();
	private Hashtable<String,String> hstSTKQT = new Hashtable<String,String>();
	private TBLINPVF objTBLVRF;
	private CallableStatement cstATIND;
	private Hashtable<String,String> hstDPTCD = new java.util.Hashtable<String,String>();
	final int TBL_CHKFL = 0;
	final int TBL_MATCD = 1;
	final int TBL_UOMCD = 2;
	final int TBL_STDPK = 3;
      final int TBL_CCTCD = 4;
      final int TBL_BATNO = 5;
      final int TBL_LOCCD = 6;
      final int TBL_AVLQT = 7;
      final int TBL_REQQT = 8;
      final int TBL_ISSQT = 9; 
      final int TBL_USGTP = 10; 
	final int TBL_STKFL = 11; 
	final int TBL_GRNNO = 12; 
	final int TBL_BALQT = 13; 
	final int TBL_OGRNO = 14; 
	final int TBL_OBTNO = 15; 

	                                            /** Engineering Stores */
	final String strEGSTR_fn = "01";			/** Store Type for Tankfarm*/
	final String strTKSTR_fn = "04";			/** Regular Dip*/
	final String strREGDP_fn = "81";			/** Exbonded Material*/
   	final String strEXBTP_fn = "01";			/** Adv.Lic.Material*/
	final String strADVTP_fn = "03";			/** Transfer Flag*/
	final String strTRNFL_fn = "0";				/** Transaction Type*/    
	final String strTRNTP_fn = "IS";			/** Issue Document Type*/    	
	final String strDOCTP_fn ="4";				/** Revamp Store*/
	final String strRVSTR_fn ="11";				/** Packing Material*/	
	final String strPKGMT_fn ="3";              /** Raw Material*/
	final String strRAWMT_fn ="2";              /** Stores and spares*/
	final String strSTRSP_fn ="1";              /**  Issue Type - Regular*/
	final String strISSRG_fn ="01";             /**  Issue Type - Project*/
	final String strISSPJ_fn ="02";             /**  Issue Type - Sale*/
	final String strISSSL_fn ="03";             /** Annexure-45 Material*/
	final String strANXTP_fn = "04";            /** DFCE Material*/            
    final String strDFCTP_fn = "05";             /** Target Plus Material*/            
    final String strTGPTP_fn = "06";            /** Doc Type for Indent - for gen. auto indent*/                                          
	final String strINDTR_fn ="10"; 
	final String strISSJO_fn ="05";             // Job Work outside             

	String strAUTBY = "RSP";				// Authorised by
	///final String strDFLBT ="COMMON";
	INPVF objINPVR = new INPVF();	
	///*** ADDED BY AAP FOR CYLINDERS
	private String [] staCYLCD;
	private Hashtable hstCYLDT;
	private cl_JTable tblCYLDT;
	private JPanel pnlCYLDT;
	private JTabbedPane tbpMAIN;
	private JPanel pnlISSDT;
	private Vector vtrMATCD;
	private Vector vtrOLDCL;
	///***
	
	mm_teist()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Issue Type"),1,1,1,1,this,'L');
			add(cmbISSTP = new JComboBox(),1,2,1,2,this,'L');
			add(new JLabel("Material Type"),2,1,1,1,this,'L');
			add(cmbMATTP = new JComboBox(),2,2,1,2,this,'L');
			add(new JLabel("Dept.Code"),2,4,1,1,this,'L');
			add(txtDPTCD = new TxtLimit(3),2,5,1,1,this,'L');
			add(txtDPTDS = new TxtLimit(50),2,6,1,3,this,'L');
			add(btnPRNT = new JButton("Print"),1,8,1,1,this,'L');
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CGSTP from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in('SYSCOXXDPT','SYSMMXXISS') and ISNULL(cmt_stsfl,'') <>'X' ORDER BY CMT_CGSTP,CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXISS"))
						cmbISSTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));
					else hstDPTCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CHP01 from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP='MST' and CMT_CGSTP ='COXXPRT' and ISNULL(CMT_CHP01,'') <>'' and ISNULL(cmt_stsfl,'') <>'X' ORDER BY CMT_CGSTP,CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					hstPRTTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"") +" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			add(new JLabel("Issue No./Date"),3,1,1,1,this,'L');
			add(txtISSNO = new TxtLimit(8),3,2,1,1,this,'L');
			add(txtISSDT = new TxtDate(),3,3,1,1,this,'L');
			add(new JLabel("Prep.By"),3,4,1,1,this,'L');
			add(txtPREBY = new TxtLimit(3),3,5,1,1,this,'L');
			add(new JLabel("Prep.Date Tm."),3,6,1,1,this,'L');
			add(txtPREDT = new TxtDate(),3,7,1,1,this,'L');
			add(txtPRETM = new TxtTime(),3,8,1,1,this,'L');
			add(new JLabel("Excise Category"),4,1,1,1,this,'L');
			add(txtEXCCT = new TxtLimit(2),4,2,1,0.5,this,'L');
			add(txtEXCDS = new TxtLimit(15),4,3,1,1.5,this,'R');
			add(new JLabel("Auth. By"),4,4,1,1,this,'L');
			add(txtAUTBY = new TxtLimit(3),4,5,1,1,this,'L');
			add(new JLabel("Auth.Date Tm."),4,6,1,1,this,'L');
			add(txtAUTDT = new TxtDate(),4,7,1,1,this,'L');
			add(txtAUTTM = new TxtTime(),4,8,1,1,this,'L');
			add(new JLabel("Order Ref."),5,1,1,1,this,'L');
			add(txtWORNO = new TxtLimit(20),5,2,1,2,this,'L');
			
			add(new JLabel("Party Type/Code"),5,4,1,1,this,'L');
			add(cmbPRTTP = new JComboBox(),5,5,1,1,this,'L');
			cmbPRTTP.addItem("S Supplier");
			//add(new JLabel("Party Code"),5,6,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(5),5,6,1,1,this,'L');
			add(txtPRTNM = new TxtLimit(40),5,7,1,2,this,'L');
			
		    String[] L_COLHD = {"","Material","UOM","Std. pkg.","Cost Centre","Batch/Lot","Location","Avail.Qty","Req.Qty","Issued Qty","Usage","Stock Item","GRIN No.","BAl Qty","Old GRIN","Old Batch"};
		    int[] L_COLSZ = {20,100,40,60,90,70,70,70,70,70,70,40,40,40,10,10};
		    intRWCNT = 100;
			///*** ADDED BY AAP FOR CYLINDER ENTRY
			pnlISSDT=new JPanel(null);
			
			setMatrix(20,8);
		//	JTable tblTEMP = new JTable(1,5);
		//	tblTEMP.setBackground(new java.awt.Color(213,213,255));
		//	add(tblTEMP,5,1,1,8,pnlISSDT,'L');
	         tblISSVL = crtTBLPNL1(pnlISSDT,L_COLHD,intRWCNT,1,1,8,8,L_COLSZ,new int[]{0,TBL_STKFL});
			 tbpMAIN=new JTabbedPane();
			 tbpMAIN.add("Issue Details",pnlISSDT);
			pnlCYLDT=new JPanel(null);
			tblCYLDT = crtTBLPNL1(pnlCYLDT,new String[]{"FL","Mat. Code","Cylinder No.","Rent Free Days"},50,1,1,10.2,8,new int[]{20,100,150,100},new int[]{0});
			((JTextField)tblCYLDT.cmpEDITR[1]).addKeyListener(this);
			tbpMAIN.add("Cylinder Details",pnlCYLDT);
			add(tbpMAIN,7,1,10,8.1,this,'L');
			///***
			tblISSVL.setCellEditor(TBL_MATCD,txtMATCD = new TxtLimit(10));
			tblISSVL.setCellEditor(TBL_LOCCD,txtLOCCD = new TxtLimit(10));
			tblISSVL.setCellEditor(TBL_USGTP,txtUSGTP = new TxtLimit(2));
			tblISSVL.setCellEditor(TBL_CCTCD,txtCCTCD = new TxtLimit(15));
			tblISSVL.setCellEditor(TBL_BATNO,txtBATNO = new TxtLimit(20));
			tblISSVL.setCellEditor(TBL_REQQT,txtREQQT = new TxtNumLimit(10.3));
			tblISSVL.setCellEditor(TBL_ISSQT,txtISSQT = new TxtNumLimit(10.3));
			
			tblISSVL.addMouseListener(this);
			txtMATCD.addFocusListener(this);txtMATCD.addKeyListener(this);
			txtLOCCD.addFocusListener(this);txtLOCCD.addKeyListener(this);
			txtUSGTP.addFocusListener(this);txtUSGTP.addKeyListener(this);
			txtCCTCD.addFocusListener(this);txtCCTCD.addKeyListener(this);
			txtBATNO.addFocusListener(this);txtBATNO.addKeyListener(this);
			txtISSQT.addFocusListener(this);txtISSQT.addKeyListener(this);
			txtREQQT.addFocusListener(this);txtREQQT.addKeyListener(this);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtISSDT.setInputVerifier(objINPVR);		
			txtEXCCT.setInputVerifier(objINPVR);		
			txtDPTCD.setInputVerifier(objINPVR);
			txtPRTCD.setInputVerifier(objINPVR);		
			objTBLVRF = new TBLINPVF();
			tblISSVL.setInputVerifier(objTBLVRF);		
			add(new JLabel("Remarks"),18,1,1,1,this,'L');
			add(txtREMDS = new TxtLimit(200),18,2,1,7,this,'L');
			////cstATIND = cl_dat.M_conSPDBA_pbst.prepareCall("{call mm_isprc(?,?,?,?,?,?,?)}");
			setENBL(false);
			txtISSNO.setEnabled(false);
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			
			///***ADDED BY AAP ON 12/10/2004 FOR CYLINDER GROUP CODES FROM BACK END
		//Finding out no. of groups for Material in Cylinder
			String L_strDATA;
			M_rstRSSET=cl_dat.exeSQLQRY("select count(*) from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXCLG' and ISNULL(cmt_stsfl,'') <>'X'");
			if(M_rstRSSET.next())
				if(M_rstRSSET.getInt(1)>0)
					staCYLCD=new String[M_rstRSSET.getInt(1)];
			M_strSQLQRY = "Select CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP in ( 'MMXXMTP', 'MMXXCLG') and ISNULL(cmt_stsfl,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i=0;
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
			//Storing group codes for Material in Cylinder
				if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXCLG") && staCYLCD!=null)
					staCYLCD[i++]=M_rstRSSET.getString("CMT_CODCD");
				else
				{
					L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
					cmbMATTP.addItem(L_strDATA);
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			///***
		}
		catch(Exception E)
		{
			setMSG(E,"Child.constructor");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}					
	}
	void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(P_flgSTATE);
		txtDPTCD.setEnabled(true);
		cmbMATTP.setEnabled(true);
		cmbISSTP.setEnabled(true);
		txtPREBY.setEnabled(false);
		txtAUTBY.setEnabled(false);
		txtPREDT.setEnabled(false);
		txtPRETM.setEnabled(false);
		txtAUTDT.setEnabled(false);
		txtAUTTM.setEnabled(false);
		txtEXCDS.setEnabled(false);
		txtDPTDS.setEnabled(false);
		txtPRTNM.setEnabled(false);
		btnPRNT.setEnabled(false);

		tblISSVL.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblISSVL.cmpEDITR[TBL_STDPK].setEnabled(false);
		tblISSVL.cmpEDITR[TBL_LOCCD].setEnabled(false);
		tblISSVL.cmpEDITR[TBL_AVLQT].setEnabled(false);
		tblISSVL.cmpEDITR[TBL_STKFL].setEnabled(false);
	//	tblISSVL.cmpEDITR[TBL_BATNO].setEnabled(false);
		tblISSVL.cmpEDITR[TBL_GRNNO].setEnabled(false);
		tblISSVL.cmpEDITR[TBL_OBTNO].setEnabled(false);
		tblISSVL.cmpEDITR[TBL_OGRNO].setEnabled(false);

		tblISSVL.cmpEDITR[TBL_BALQT].setEnabled(false);


		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtISSNO.setEnabled(false);	
				btnPRNT.setEnabled(true);
			/*	if(M_strSBSCD !=null)
					if(M_strSBSCD.substring(2,4).equals(strRVSTR_fn))
					{
						txtWORNO.setEnabled(true);
						cmbPRTTP.setEnabled(true);
						txtPRTCD.setEnabled(true);
					}
					else
					{
						txtWORNO.setEnabled(false);
						cmbPRTTP.setEnabled(false);
						txtPRTCD.setEnabled(false);
					}*/
			}
			else
				txtISSNO.setEnabled(true);	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
			    btnPRNT.setEnabled(true);   
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				tblISSVL.cmpEDITR[TBL_MATCD].setEnabled(true);
				tblISSVL.cmpEDITR[TBL_ISSQT].setEnabled(true);
				tblISSVL.cmpEDITR[TBL_BATNO].setEnabled(true);
				tblCYLDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
				
			}
			else
				tblISSVL.cmpEDITR[TBL_ISSQT].setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				tblISSVL.cmpEDITR[TBL_CHKFL].setEnabled(true);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				btnPRNT.setEnabled(true);
				txtWORNO.setEnabled(false);
				cmbPRTTP.setEnabled(false);
				txtPRTCD.setEnabled(false);
			}
			else
			{
				txtWORNO.setEnabled(true);
				cmbPRTTP.setEnabled(true);
				txtPRTCD.setEnabled(true);
			}
		}
		if(strSTRTP !=null)
		if(strSTRTP.equals(strTKSTR_fn))
		{
			txtEXCCT.setEnabled(true);
			txtISSDT.setEnabled(true);
		}
		else	
		{
			txtEXCCT.setEnabled(false);
			txtISSDT.setEnabled(false);
		}
	}	
	// Clear all the fields
	void clrCOMP()
	{
		try
		{
			if(M_strSBSCD !=null)
				strSTRTP =M_strSBSCD.substring(2,4);
			txtISSNO.setText("");
			txtISSDT.setText("");
			txtEXCCT.setText("");
			txtEXCDS.setText("");
			txtPREBY.setText("");
			txtPREDT.setText("");
			txtPRETM.setText("");
			txtAUTBY.setText("");
			txtAUTDT.setText("");
			txtAUTTM.setText("");
			txtREMDS.setText("");
			txtWORNO.setText("");
			txtPRTCD.setText("");
			txtPRTNM.setText("");
			tblISSVL.clrTABLE();
			txtDPTCD.setText("");
			txtDPTDS.setText("");
			strCALQT = "0.000";		// Initialize the Global Variable
			flgRMKFL = false;
			cmbMATTP.requestFocus();
			cl_dat.M_txtDESC_pbst.setText("");
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getItemCount() == 0)
					return;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				setENBL(true);
				if(M_strSBSCD.substring(2,4).equals(strTKSTR_fn))
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(Calendar.DATE,-1);
					txtISSDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				}
				else
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					txtISSDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				}
				txtPREBY.setText(cl_dat.M_strUSRCD_pbst);
				txtPREDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
				txtPRETM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
			}
			else
				setENBL(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"clrCOMP");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	public void itemStateChanged(ItemEvent L_IE)
	{
		super.itemStateChanged(L_IE);
		if(M_objSOURC == cl_dat.M_cmbSBSL2_pbst)
		{
			try
			{
				String L_strDATA ="";
				if(M_strSBSCD !=null)
				{
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMTP' and ISNULL(cmt_stsfl,'') <>'X'";
					M_strSQLQRY += " AND CMT_CHP01 like '%"+M_strSBSCD.substring(2,4) +"%'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						cmbMATTP.removeAllItems();
						while(M_rstRSSET.next())
						{
							L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
							cmbMATTP.addItem(L_strDATA);
						}
						M_rstRSSET.close();
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Child.itemStateChanged");	
			}
		}
	}
	
	public void mousePressed(MouseEvent L_ME)
	{
		try
		{
			super.mousePressed(L_ME);		
			setMSG("",'N');
			if(L_ME.getSource().equals(tblISSVL))
			{		
				strMATCD = String.valueOf(tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_MATCD)).trim();
				if(strMATCD.length() == 0)
					cl_dat.M_txtDESC_pbst.setText("");
				else
					cl_dat.M_txtDESC_pbst.setText(getMATDS(strMATCD));
			}
		}
		catch(Exception e)
		{
			setMSG(e,"Error" + e.toString());
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == tblISSVL.cmpEDITR[TBL_MATCD])
	    {
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_MATCD).toString().length() ==10)
					((JTextField)tblISSVL.cmpEDITR[TBL_MATCD]).setEditable(false);
				else
					((JTextField)tblISSVL.cmpEDITR[TBL_MATCD]).setEditable(true);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				if(tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_MATCD).toString().length() ==10)
					((JTextField)tblISSVL.cmpEDITR[TBL_MATCD]).setEditable(false);
				else
					((JTextField)tblISSVL.cmpEDITR[TBL_MATCD]).setEditable(true);
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == btnPRNT)
			{
				if(txtISSNO.getText().trim().length() ==8)
					strISSNO = txtISSNO.getText().trim();
				if(strISSNO.length() ==0)
				{
					setMSG("Issue number is not specified..",'E');
					return;
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
				mm_rpmin objISSRP  = new mm_rpmin(M_strSBSCD);
				objISSRP.genRPTFL(strISSNO,strISSNO);
				JComboBox L_cmbLOCAL = objISSRP.getPRNLS();
				objISSRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpmin.doc",L_cmbLOCAL.getSelectedIndex());
					
			}
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				if(tblISSVL !=null)
					if(tblISSVL.isEditing())
						tblISSVL.getCellEditor().stopCellEditing();
				tblISSVL.clrTABLE();
				if(cl_dat.M_cmbOPTN_pbst.getItemCount() == 0)
					return;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
				{
					setENBL(false);
					txtISSNO.setEnabled(false);
				}
				else
				{
					cmbISSTP.requestFocus();
					cmbISSTP.showPopup();
				}
				/*else
				{
					cmbMATTP.requestFocus();
					cmbMATTP.showPopup();
				}*/
			}
			else if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
			{
				clrCOMP();
				strSTRTP = M_strSBSCD.substring(2,4);
			}
			else if(M_objSOURC == cmbISSTP)
			{
			    if(cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSRG_fn))
			    {
			        cmbPRTTP.setEnabled(false);
			        txtPRTCD.setEnabled(false);
	       			txtWORNO.setEnabled(false);
	
			    }
			    else if(cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSPJ_fn))
			    {
			        cmbPRTTP.setEnabled(true);
			        txtPRTCD.setEnabled(true);
	       			txtWORNO.setEnabled(true);
			        cmbPRTTP.removeAllItems();
			        cmbPRTTP.addItem(hstPRTTP.get(strISSPJ_fn));
			    }
			    else
			    {
			        cmbPRTTP.setEnabled(true);
			        txtPRTCD.setEnabled(true);
	       			txtWORNO.setEnabled(true);
			        cmbPRTTP.removeAllItems();
			         cmbPRTTP.addItem(hstPRTTP.get(strISSSL_fn));
			    }
			    cmbMATTP.requestFocus();
			    cmbMATTP.showPopup();
			}
			else if(M_objSOURC == cmbMATTP)
			{
				strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
				/*if(M_strSBSCD!=null)
				if(!M_strSBSCD.substring(2,4).equals("04"))
					if((cmbMATTP.getSelectedItem().toString().substring(0,2).equals("47"))||(cmbMATTP.getSelectedItem().toString().substring(0,2).equals("48")))
					{
						setMSG("Invalid Catergory..",'E');
						cmbMATTP.setSelectedIndex(0);
					}*/
			}
			else if(M_objSOURC == cl_dat.M_cmbSBSL2_pbst)
			{		
				if(cl_dat.M_cmbOPTN_pbst.getItemCount() == 0)
					return;
				strSTRTP = M_strSBSCD.substring(0,2);
            	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(strSTRTP.equals(strTKSTR_fn))
						txtAUTBY.setText(strAUTBY);
					else
						cmbMATTP.requestFocus();
				}
				cmbMATTP.requestFocus();			
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			int[] L_inaCOLSZ = new int[]{110,300,110};
			intKEYCD = L_KE.getKeyCode();
			setMSG("",'N');
			if(M_objSOURC == txtMATCD)
			{
				if(intKEYCD == L_KE.VK_F2)
				{
					setCursor(cl_dat.M_curWTSTS_pbst);
					updateUI();
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMATCD1";
					strSTRTP =M_strSBSCD.substring(2,4);
					String L_ARRHDR[] = {"Code","Material                                              ","UOM"};
					if(strSTRTP.equals(strTKSTR_fn))
					{
						M_strSQLQRY = "Select Distinct CT_MATCD,CT_MATDS,CT_UOMCD";
						M_strSQLQRY += " from CO_CTMST,MM_TKMST";
						M_strSQLQRY += " where TK_MATCD = CT_MATCD AND TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
						M_strSQLQRY += " order by CT_MATDS desc";
					}
					else
					{		
						strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
						M_strSQLQRY = "select ST_MATCD,ST_MATDS,ST_UOMCD from MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ISNULL(ST_STKQT,0) > 0 ";
						M_strSQLQRY += " AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"' ";
						if(strMATTP.equals(strPKGMT_fn))
							M_strSQLQRY += " and ST_MATCD like '69%'";
						else if(strMATTP.equals(strRAWMT_fn))
						{
							if(!M_strSBSCD.substring(2,4).equals("01"))
								M_strSQLQRY += " and (ST_MATCD like '68%' OR ST_MATCD LIKE '99%')";
							else
								M_strSQLQRY += " and ST_MATCD not like '68%'";
						}
						else if(strMATTP.equals(strSTRSP_fn))
							M_strSQLQRY += " and ST_MATCD not like '68%' and st_matcd not like '69%' ";
						else
							M_strSQLQRY += " and ST_MATCD not like '68%' and st_matcd not like '69%' ";
						
						if(txtMATCD.getText().trim().length() >0)
							M_strSQLQRY += " and ST_MATCD like '"+txtMATCD.getText().trim() +"%'";
						M_strSQLQRY += " order by ST_MATDS";
					}
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,3,"CT",L_inaCOLSZ);
					updateUI();
						setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(intKEYCD == L_KE.VK_F1)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMATCD";
					strSTRTP =M_strSBSCD.substring(2,4);
					String L_ARRHDR[] = {"Code","Material                                              ","UOM"};
					if(strSTRTP.equals(strTKSTR_fn))
					{
						M_strSQLQRY = "Select Distinct CT_MATCD,CT_MATDS,CT_UOMCD";
						M_strSQLQRY += " from CO_CTMST,MM_TKMST";
						M_strSQLQRY += " where TK_MATCD = CT_MATCD AND TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
						M_strSQLQRY += " order by CT_MATDS desc";
					}
					else
					{		
						strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
						M_strSQLQRY = "select ST_MATCD,ST_MATDS,ST_UOMCD from MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ISNULL(ST_STKQT,0) > 0 ";
						M_strSQLQRY += " AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"' ";
						if(strMATTP.equals(strPKGMT_fn))
							M_strSQLQRY += " and ST_MATCD like '69%'";
						else if(strMATTP.equals(strRAWMT_fn))
						{
							if(!M_strSBSCD.substring(2,4).equals("01"))
								M_strSQLQRY += " and (ST_MATCD like '68%' OR ST_MATCD LIKE '99%')";
							else
								M_strSQLQRY += " and ST_MATCD not like '68%'";
						}
						else if(strMATTP.equals(strSTRSP_fn))
						{
							if(M_strSBSCD.substring(2,4).equals(strRVSTR_fn))
								M_strSQLQRY += " and ST_MATCD like '95%'";
							else
								M_strSQLQRY += " and ST_MATCD not like '68%' and st_matcd not like '69%' ";
						}
						
						else
							M_strSQLQRY += " and ST_MATCD not like '68%' and st_matcd not like '69%' ";
						if(txtMATCD.getText().trim().length() >0)
							M_strSQLQRY += " and ST_MATCD like '"+txtMATCD.getText().trim() +"%'";
						M_strSQLQRY += " order by ST_MATCD";
					}
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT",L_inaCOLSZ);		
				}
			}
			else if(M_objSOURC == txtCCTCD)				// Usage Type
			{
				if(intKEYCD == L_KE.VK_F2)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtCCTCD1";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "Select EQ_TAGNO,EQ_TAGDS from mm_eqmst WHERE EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
					if(txtCCTCD.getText().trim().length() >0)
						M_strSQLQRY +="  AND EQ_TAGNO like '"+txtCCTCD.getText().trim() +"%'";
					M_strSQLQRY += " order by EQ_TAGDS ";
					cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
				}
				else if(intKEYCD == L_KE.VK_F1)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtCCTCD";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "Select EQ_TAGNO,EQ_TAGDS from mm_eqmst  WHERE EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
					if(txtCCTCD.getText().trim().length() >0)
						M_strSQLQRY +=" AND EQ_TAGNO like '"+txtCCTCD.getText().trim() +"%'";
					M_strSQLQRY += " order by eq_tagno";
					cl_hlp(M_strSQLQRY ,1,1,L_ARRHDR,2,"CT");
				}
			}
			else if(M_objSOURC == txtBATNO)				// Usage Type
			{
				if(intKEYCD == L_KE.VK_F2)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtBATNO1";
					String L_ARRHDR[] = {"Code","Description","Bal.Qty."};
					M_strSQLQRY = "Select GR_GRNNO,GR_BATNO,(ISNULL(GR_ACPQT,0) - ISNULL(GR_ISSQT,0))L_BALQT from MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
					M_strSQLQRY += " AND GR_MATCD ='"+tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_MATCD) +"'";
					M_strSQLQRY += " AND ISNULL(GR_ACPQT,0) - ISNULL(GR_ISSQT,0) >0 ";
					M_strSQLQRY += " order by GR_GRNNO ";
					cl_hlp(M_strSQLQRY ,2,2,L_ARRHDR,3,"CT");
				}
				else if(intKEYCD == L_KE.VK_F1)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtBATNO";
					String L_ARRHDR[] = {"Code","Description","Bal Qty."};
					M_strSQLQRY = "Select GR_GRNNO,GR_BATNO,(ISNULL(GR_ACPQT,0) - ISNULL(GR_ISSQT,0))L_BALQT from MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
					M_strSQLQRY += " AND GR_MATCD ='"+tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_MATCD) +"'";
					M_strSQLQRY += " AND ISNULL(GR_ACPQT,0) - ISNULL(GR_ISSQT,0) >0 ";
					M_strSQLQRY += " order by GR_GRNNO ";
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,2,2,L_ARRHDR,3,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_F1)
			{	
				try
				{
					setCursor(cl_dat.M_curWTSTS_pbst);
					if(M_objSOURC == txtISSNO)
					{
					try
					{
						strSTRTP =M_strSBSCD.substring(2,4);
						strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtISSNO";
						String L_ARRHDR[] = {"Issue No.","Date","Department code"};
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
						{
							if(txtDPTCD.getText().trim().length() ==0)
								{setMSG("Enter the Department Code..",'E');setCursor(cl_dat.M_curDFSTS_pbst); return;}
							else setMSG("",'N');
						}
						M_strSQLQRY = "select distinct IS_ISSNO,IS_ISSDT,IS_DPTCD from MM_ISMST";
						M_strSQLQRY += " where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ISNULL(IS_STSFL,' ') <>'X' and IS_STRTP = '" + strSTRTP + "'";
						M_strSQLQRY += " AND IS_MATTP ='"+strMATTP +"'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							M_strSQLQRY +=" and IS_STSFL <> '2'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							M_strSQLQRY +=" and IS_STSFL <> '2'";
						else
							M_strSQLQRY +=" AND IS_DPTCD ='"+txtDPTCD.getText().trim()+"'";
						if(txtISSNO.getText().trim().length() >0)
							M_strSQLQRY +=" AND IS_ISSNO LIKE '"+txtISSNO.getText().trim() +"%'";
						M_strSQLQRY += " order by IS_ISSDT desc ";
						//System.out.println(M_strSQLQRY);
						if(M_strSQLQRY != null)
							cl_hlp(M_strSQLQRY ,1,1,L_ARRHDR,3,"CT");
						setMSG("",'N');
					}
					catch(NullPointerException L_NE)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							setMSG("No Issues are pending for authorisation ..",'E');
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							setMSG("All Issues are authorised ..",'E');
						}
						setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				else if(M_objSOURC == txtDPTCD)
				{	
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDPTCD";
					strSTRTP =M_strSBSCD.substring(2,4);
					String L_ARRHDR[] = {"Department Code","Description"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' and ISNULL(cmt_stsfl,'') <>'X' ";
					if(txtDPTCD.getText().trim().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtDPTCD.getText().trim() +"%'";
					M_strSQLQRY += " order by CMT_CODDS";
					
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtLOCCD)
				{			
					strSTRTP =M_strSBSCD.substring(2,4);
					strMATCD = String.valueOf(tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_MATCD));
					if(strSTRTP.equals(strTKSTR_fn))
					{	
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtLOCCD";
						String L_ARRHDR[] = {"Tank No.","Material"};
						M_strSQLQRY = "Select TK_TNKNO,CT_MATDS from MM_TKMST,CO_CTMST";
						M_strSQLQRY += " where TK_MATCD = CT_MATCD";
						M_strSQLQRY += " and TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_MATCD = '" + strMATCD + "'";
						M_strSQLQRY += " order by CT_MATDS desc";
						if(M_strSQLQRY != null)
							cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
					}
				}
				else if(M_objSOURC == txtUSGTP)				// Usage Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtUSGTP";
					String L_ARRHDR[] = {"Code","Usage Type"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " and CMT_CGSTP = 'MMXXUSG'";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
				}
		 		else if(M_objSOURC == txtEXCCT)				// Usage Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtEXCCT";
					String L_ARRHDR[] = {"Code","Excise Category"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " and CMT_CGSTP = 'MMXXMAT'";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtPRTCD)				// Usage Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPRTCD";
					String L_ARRHDR[] = {"Party Code","Party Name"};
					M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST";
					M_strSQLQRY += " where PT_PRTTP = '"+cmbPRTTP.getSelectedItem().toString().substring(0,1) +"'";
					if(txtPRTCD.getText().trim().length() >0)
						M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText()+"%'";	
					M_strSQLQRY += " Order By PT_PRTCD";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
				}
				catch(Exception L_E)
				{
					setMSG("error",'E');
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == cmbMATTP)
				{
					strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						txtISSNO.requestFocus();
						setMSG("Select the Issue no. from F1 List..",'N');
					}
					else
					{
						txtDPTCD.requestFocus();
						setMSG("Select the Department code from F1 List..",'N');
					}
				}
				else if(M_objSOURC == txtDPTCD)
				{
					if(txtDPTCD.getText().length() ==0)
					{
						setMSG("Select the department code from F1 list..",'E');
						return;
					}
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
						txtISSNO.requestFocus();
					else
					{
						if(M_strSBSCD.substring(2,4).equals(strTKSTR_fn))
						{
							txtISSDT.requestFocus();
						}
						else 
					//	if(M_strSBSCD.substring(2,4).equals(strRVSTR_fn))
						{
							txtWORNO.requestFocus();
							setMSG("Enter the Work Order number ..",'N');
						}
					/*	else
						{
							tblISSVL.setRowSelectionInterval(0,0);
							tblISSVL.setColumnSelectionInterval(1,1);
							tblISSVL.editCellAt(tblISSVL.getSelectedRow(),TBL_MATCD);
							tblISSVL.cmpEDITR[0].requestFocus();
							setMSG("press F1 to select the item code ..",'N');
						}*/
					}
				}
				else if(M_objSOURC == txtWORNO)
				{
					cmbPRTTP.requestFocus();
					setMSG("Select the Party Type ..",'N');
				}
				else if(M_objSOURC == cmbPRTTP)
				{
					txtPRTCD.requestFocus();
					setMSG("Enter the Party Code,F1 to select from the LIst ..",'N');
				}
				else if(M_objSOURC == txtPRTCD)
				{
					tblISSVL.setRowSelectionInterval(0,0);
					tblISSVL.setColumnSelectionInterval(1,1);
					tblISSVL.editCellAt(tblISSVL.getSelectedRow(),TBL_MATCD);
					tblISSVL.cmpEDITR[0].requestFocus();
					setMSG("press F1 to select the item code ..",'N');
				}
				else if(M_objSOURC == txtISSNO)
				{
					strSTRTP =M_strSBSCD.substring(2,4);
					strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
					strISSNO = txtISSNO.getText().trim();
					if(getDATA())
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
							setENBL(true);
						else
							setENBL(false);
						txtISSNO.requestFocus();
					}
				}
				else if(M_objSOURC == txtEXCCT)
				{			
					if(strSTRTP.equals(strTKSTR_fn))
					{
						setCursor(cl_dat.M_curWTSTS_pbst);
                        if(cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSJO_fn))
                        {
                            if(!txtEXCCT.getText().trim().equals(strEXBTP_fn))
                            {
                                setMSG("Please select the Excise category as Exbonded for Job Work-Outside",'E');
                                return;
                            }
                        }
						if(getISSUE(txtISSDT.getText().trim()))
						{
							tblISSVL.setRowSelectionInterval(0,0);
							tblISSVL.setColumnSelectionInterval(TBL_ISSQT,TBL_ISSQT);
							tblISSVL.editCellAt(tblISSVL.getSelectedRow(),TBL_ISSQT);
							tblISSVL.cmpEDITR[TBL_ISSQT].requestFocus();
							setMSG("press F1 to select the item code ..",'N');
						}
						setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				else if(M_objSOURC == txtISSDT)
					txtEXCCT.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"keyPressed");
		}
	}
	void exeSAVE()
	{
		try
		{	String L_strSQLQRY ="";
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			if(tblISSVL !=null)
				if(tblISSVL.isEditing())
					tblISSVL.getCellEditor().stopCellEditing();
			boolean L_RECORD = false;
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			strSTRTP =M_strSBSCD.substring(2,4);
			strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				strSTSFL ="0";
			if(strSTSFL.equals("2"))
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setMSG("Issue is Authorised, cannot process..",'E');
				return ;
			}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{	
			strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
			/*if(M_strSBSCD.substring(2,4).equals("04"))
			{
				strMATTP ="2";
				strISSNO = genTFISS(cmbMATTP.getSelectedItem().toString().substring(0,2));
			}
			else*/
				strISSNO = genISSNO(strMATTP);
			if(strISSNO == null)
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setMSG("In Use, please try after some time..",'E');
				return ;
			}
			else
				txtISSNO.setText(strISSNO);
			strEXCCT = txtEXCCT.getText().trim();
		}
		strISSNO = txtISSNO.getText().trim();
		strISSDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtISSDT.getText().trim()));
		strDPTCD = txtDPTCD.getText().trim();
		strPREBY = txtPREBY.getText().trim().toUpperCase();
		strAUTBY = txtAUTBY.getText().trim().toUpperCase();
		strSTSFL = "0";
		strLUSBY = cl_dat.M_strUSRCD_pbst.trim();
		strLUPDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
		for(int i = 0; i < intRWCNT; i++)
		{
			if(tblISSVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_RECORD = true;
				strMATCD = String.valueOf(tblISSVL.getValueAt(i,TBL_MATCD)).trim();
				strCCTCD = String.valueOf(tblISSVL.getValueAt(i,TBL_CCTCD)).trim();
				strGRNNO = String.valueOf(tblISSVL.getValueAt(i,TBL_GRNNO)).trim();
				strBATNO = String.valueOf(tblISSVL.getValueAt(i,TBL_BATNO)).trim();
				strOGRNO = String.valueOf(tblISSVL.getValueAt(i,TBL_OGRNO)).trim();
				strOBTNO = String.valueOf(tblISSVL.getValueAt(i,TBL_OBTNO)).trim();
				strLOCCD = String.valueOf(tblISSVL.getValueAt(i,TBL_LOCCD)).trim();
				strREQQT = String.valueOf(tblISSVL.getValueAt(i,TBL_REQQT)).trim();
				if(strBATNO.equals(""))
					strBATNO = strISSNO;
				if(strGRNNO.equals(""))
					strGRNNO = strISSNO;

				strISSQT ="0";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					strISSQT = String.valueOf(tblISSVL.getValueAt(i,TBL_ISSQT)).trim();
				strISSRT = "0";
				strUSGTP = String.valueOf(tblISSVL.getValueAt(i,TBL_USGTP)).trim();
				strTAGNO = String.valueOf(tblISSVL.getValueAt(i,TBL_CCTCD)).trim();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					M_strSQLQRY = "Insert into MM_ISMST(IS_CMPCD,IS_MMSBS,IS_STRTP,IS_ISSTP,IS_ISSNO,IS_ISSDT," +	
					"IS_DPTCD,IS_MATCD,IS_TAGNO,IS_REQQT,IS_ISSQT,IS_ISSRT," +
					"IS_USGTP,IS_CCTCD,IS_PREBY,IS_PREDT,IS_AUTBY,IS_LOCCD,IS_MATTP,IS_GRNNO,IS_BATNO,IS_EXCCT,";
					M_strSQLQRY +="IS_WORNO,IS_PRTTP,IS_PRTCD,";
					M_strSQLQRY +="IS_TRNFL,IS_STSFL,IS_LUSBY,IS_LUPDT) values (" +
					"'" + cl_dat.M_strCMPCD_pbst + "'," +
					"'" + M_strSBSCD + "'," +
					"'" + strSTRTP + "'," +
					"'" + cmbISSTP.getSelectedItem().toString().substring(0,2) + "'," +
					"'" + strISSNO + "'," +
					"'" + strISSDT + "'," +
					"'" + strDPTCD + "'," +
					"'" + strMATCD + "'," +
					"'" + strTAGNO + "'," +
					strREQQT + "," +
					strISSQT + "," +
					strISSRT + "," +
					"'" + strUSGTP + "'," +
					"'" + strCCTCD + "'," +
					"'" + strPREBY + "',current_timestamp," +
					"'" + strAUTBY + "'," +
					"'" + strLOCCD + "'," +
					"'" + strMATTP + "'," +
					"'" + strGRNNO + "'," +
					"'" + strBATNO + "'," +
					"'" + strEXCCT + "'," +
					"'" + txtWORNO.getText() + "'," +
					"'"+cmbPRTTP.getSelectedItem().toString().substring(0,1) + "'," +
					"'" + txtPRTCD.getText() + "'," ;
					M_strSQLQRY +=getUSGDTL("IS",'I',strSTSFL)+")";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				// Removed from Addition to Authorisation
				//	if(strSTRTP.equals(strTKSTR_fn))
				//		updDPTRN(strISSDT,strLOCCD,strISSQT);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
						strEXCCT = txtEXCCT.getText().trim();
						L_strSQLQRY ="";
						if(hstMATCD.containsKey(strMATCD.trim()+strTAGNO.trim()+strOBTNO.trim()+strOGRNO.trim()))
					    {
							L_strSQLQRY = "Update MM_ISMST set " +
								"IS_DPTCD = '" + strDPTCD + "'," +
								"IS_BATNO = '" + strBATNO + "'," +
								"IS_GRNNO = '" + strGRNNO + "'," +
								"IS_ISSDT = '" + strISSDT + "'," +
								"IS_CCTCD = '" + strCCTCD + "'," +
								"IS_REQQT = " + strREQQT + "," +
								"IS_ISSQT = " + strISSQT + "," +
								"IS_USGTP = '" + strUSGTP + "'," +
								"IS_LOCCD = '" + strLOCCD + "'," +
								"IS_EXCCT = '" + strEXCCT + "',"+
								"IS_WORNO = '" + txtWORNO.getText() + "',"+
								"IS_PRTTP = '" + cmbPRTTP.getSelectedItem().toString().substring(0,1) + "',"+
								"IS_PRTCD = '" + txtPRTCD.getText() + "',";
							L_strSQLQRY+= getUSGDTL("IS",'U',null);
						    L_strSQLQRY+=" where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '" + strSTRTP + "'" +
								" and IS_ISSNO = '" + strISSNO + "'" +
								" and IS_MATCD = '" + strMATCD + "'" +
								" and IS_MATCD = '" + strMATCD + "'" +
								" and IS_TAGNO = '" + strTAGNO + "'" +
								" and IS_BATNO = '" + strOBTNO + "'" +
								" and IS_GRNNO = '" + strOGRNO + "'";
							//System.out.println(M_strSQLQRY);
							cl_dat.exeSQLUPD(L_strSQLQRY ,"setLCLUPD");
						}
						else
						{
							L_strSQLQRY = "Insert into MM_ISMST(IS_CMPCD,IS_MMSBS,IS_STRTP,IS_ISSTP,IS_ISSNO,IS_ISSDT," +	
							"IS_DPTCD,IS_MATCD,IS_TAGNO,IS_REQQT,IS_ISSQT,IS_ISSRT," +
							"IS_USGTP,IS_CCTCD,IS_PREBY,IS_PREDT,IS_AUTBY,IS_LOCCD,IS_MATTP,IS_BATNO,IS_GRNNO,IS_EXCCT,";
							L_strSQLQRY +="IS_WORNO,IS_PRTTP,IS_PRTCD,";
							L_strSQLQRY +="IS_TRNFL,IS_STSFL,IS_LUSBY,IS_LUPDT) values (" +
							"'" + cl_dat.M_strCMPCD_pbst + "'," +
							"'" + M_strSBSCD + "'," +
							"'" + strSTRTP + "'," +
							"'" + cmbISSTP.getSelectedItem().toString().substring(0,2) + "'," +
							"'" + strISSNO + "'," +
							"'" + strISSDT + "'," +
							"'" + strDPTCD + "'," +
							"'" + strMATCD + "'," +
							"'" + strTAGNO + "'," +
							strREQQT + "," +
							strISSQT + "," +
							strISSRT + "," +
							"'" + strUSGTP + "'," +
							"'" + strCCTCD + "'," +
							"'" + strPREBY + "',CURRENT_TIMESTAMP," +
							"'" + strAUTBY + "'," +
							"'" + strLOCCD + "'," +
							"'" + strMATTP + "'," +
							"'" + strBATNO + "'," +
							"'" + strGRNNO + "'," +
							"'" + strEXCCT + "'," +
						    "'" + txtWORNO.getText() + "'," +
							"'" + cmbPRTTP.getSelectedItem().toString().substring(0,1) + "'," +
							"'" + txtPRTCD.getText() + "'," ;
							L_strSQLQRY +=getUSGDTL("IS",'I',strSTSFL)+")";
							cl_dat.exeSQLUPD(L_strSQLQRY ,"setLCLUPD");
							hstMATCD.put(strMATCD.trim()+strTAGNO.trim()+strBATNO.trim()+strGRNNO.trim()," ");
						}
					}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					strEXCCT = txtEXCCT.getText().trim();
						if(hstMATCD.containsKey(strMATCD.trim()+strTAGNO.trim()+strOBTNO.trim()+strOGRNO.trim()))
						{
							
						}
						else
						{
							
							L_strSQLQRY = "Insert into MM_ISMST(IS_CMPCD,IS_MMSBS,IS_STRTP,IS_ISSTP,IS_ISSNO,IS_ISSDT," +	
							"IS_DPTCD,IS_MATCD,IS_TAGNO,IS_REQQT,IS_ISSQT,IS_ISSRT," +
							"IS_USGTP,IS_CCTCD,IS_PREBY,IS_PREDT,IS_AUTBY,IS_LOCCD,IS_MATTP,IS_BATNO,IS_GRNNO,IS_EXCCT,";
							L_strSQLQRY +="IS_WORNO,IS_PRTTP,IS_PRTCD,";
							L_strSQLQRY +="IS_TRNFL,IS_STSFL,IS_LUSBY,IS_LUPDT) values (" +
							"'" + cl_dat.M_strCMPCD_pbst + "'," +
							"'" + M_strSBSCD + "'," +
							"'" + strSTRTP + "'," +
							"'" + cmbISSTP.getSelectedItem().toString().substring(0,2) + "'," +
							"'" + strISSNO + "'," +
							"'" + strISSDT + "'," +
							"'" + strDPTCD + "'," +
							"'" + strMATCD + "'," +
							"'" + strTAGNO + "'," +
							strREQQT + "," +
							"0"  + "," +
							strISSRT + "," +
							"'" + strUSGTP + "'," +
							"'" + strCCTCD + "'," +
							"'" + strPREBY + "',CURRENT_TIMESTAMP," +
							"'" + strAUTBY + "'," +
							"'" + strLOCCD + "'," +
							"'" + strMATTP + "'," +
							"'" + strBATNO + "'," +
							"'" + strGRNNO + "'," +
							"'" + strEXCCT + "'," +
						    	"'" + txtWORNO.getText() + "'," +
							"'" + cmbPRTTP.getSelectedItem().toString().substring(0,1) + "'," +
							"'" + txtPRTCD.getText() + "'," ;
							L_strSQLQRY +=getUSGDTL("IS",'I',strSTSFL)+")";
							cl_dat.exeSQLUPD(L_strSQLQRY ,"setLCLUPD");
							hstMATCD.put(strMATCD.trim()+strTAGNO.trim()+strBATNO.trim()+strGRNNO.trim()," ");
						}

						M_strSQLQRY = "Update MM_ISMST set " +
						"IS_DPTCD = '" + strDPTCD + "'," +
						"IS_BATNO = '" + strBATNO + "'," +
						"IS_GRNNO = '" + strGRNNO + "'," +
						"IS_ISSDT = '" + strISSDT + "'," +
						"IS_CCTCD = '" + strCCTCD + "'," +
						"IS_REQQT = " + strREQQT + "," +
						"IS_ISSQT = " + strISSQT + "," +
						"IS_USGTP = '" + strUSGTP + "'," +
						"IS_LOCCD = '" + strLOCCD + "'," +
						"IS_EXCCT = '" + strEXCCT + "',"+
						"IS_AUTBY = '" +cl_dat.M_strUSRCD_pbst + "',";
					if(strSTRTP.equals(strTKSTR_fn))
					{
						M_strSQLQRY +=	"IS_PREDT = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtISSDT.getText().trim()+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
						M_strSQLQRY +=	"IS_AUTDT = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtISSDT.getText().trim()+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
					}
					else
						M_strSQLQRY +=	"IS_AUTDT = current_timestamp, ";
					M_strSQLQRY +=	"IS_WORNO = '" + txtWORNO.getText() + "',"+
						"IS_PRTTP = '" + cmbPRTTP.getSelectedItem().toString().substring(0,1) + "',"+
						"IS_PRTCD = '" + txtPRTCD.getText() + "',";
					M_strSQLQRY+= getUSGDTL("IS",'U',"2");  // Status 2 is authorised
					M_strSQLQRY+=" where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '" + strSTRTP + "'" +
						" and IS_ISSNO = '" + strISSNO + "'" +
						" and IS_MATCD = '" + strMATCD + "'" +
						" and IS_TAGNO = '" + strTAGNO + "'" +
						" and IS_BATNO = '" + strOBTNO + "'" +
						" and IS_GRNNO = '" + strOGRNO + "'" +
						" and ISNULL(IS_STSFL,'') <>'X'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst)
					if(strSTRTP.equals(strTKSTR_fn))
						updDPTRN(strISSDT,strLOCCD,strISSQT);
					else
					{
						if((strGRNNO.length() >0) && (!strBATNO.equals(strISSNO))&& (!strGRNNO.equals(strISSNO)))
						{ 
							M_strSQLQRY = "Update MM_GRMST set " +
							"GR_ISSQT = ISNULL(GR_ISSQT,0) + " + strISSQT + "," ;
							M_strSQLQRY+= getUSGDTL("GR",'U',null);  					
							M_strSQLQRY+=" where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'" +
							" and GR_GRNNO = '" + strGRNNO + "'" +
							" and GR_MATCD = '" + strMATCD + "'" +
							" and GR_BATNO = '" + strBATNO + "'" +
							" and ISNULL(GR_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						}
					}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				M_strSQLQRY = "Update MM_ISMST set " ;
				M_strSQLQRY +=getUSGDTL("IS",'U',"X");
				M_strSQLQRY +=" where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '" + strSTRTP + "'" +
				 " and IS_ISSNO = '" + strISSNO + "'" +
				 " and IS_MATCD = '" + strMATCD + "'" +
				 " and IS_TAGNO = '" + strTAGNO + "'" +			  
				 " and IS_BATNO = '" + strBATNO + "'" +
				 " and IS_GRNNO = '" + strGRNNO + "'" +
				 " and ISNULL(IS_STSFL,' ') <> '2'"; // Authorised
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				strREMDS = txtREMDS.getText().trim();
			// To check whether the remark against the memo no is exists or not
				if(flgRMKFL)							// To update existing record
				{					
					M_strSQLQRY = "Update MM_RMMST set ";
					M_strSQLQRY += "RM_STSFL = 'X',";
					M_strSQLQRY += "RM_TRNFL = '" + strTRNFL_fn + "',";
					M_strSQLQRY += "RM_LUSBY = '" + strLUSBY + "',";
					M_strSQLQRY += "RM_LUPDT = '" + strLUPDT+"'";
					M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + strSTRTP + "'";
					M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
					M_strSQLQRY += " and RM_DOCTP = '" + strMATTP + "'";
					M_strSQLQRY += " and RM_DOCNO = '" + strISSNO + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			 }
			}
		}
		if((cl_dat.M_flgLCUPD_pbst)&& (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
		{
			for(int i=0;i<tblCYLDT.getRowCount();i++)
			{
				if(tblCYLDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY = "Update MM_CLTRN SET CLT_ISSNO ='"+txtISSNO.getText().trim() +"',CLT_DPTCD ='"+txtDPTCD.getText().trim()+"',"+
								  getUSGDTL("CLT",'U',"2")+
								  " where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
								  " and CLT_SRLNO ='"+tblCYLDT.getValueAt(i,2) +"'"+
								  " and CLT_MATCD ='"+tblCYLDT.getValueAt(i,1) +"'"+
								  " and CLT_STSFL ='1'";
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
		}
		if(!L_RECORD)
		{
			setMSG("Please select at least one item in Issue Details",'E');
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			return;
		}
		if(cl_dat.M_flgLCUPD_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			    exeISSNO(strISSNO,strMATTP);
				/*if(M_strSBSCD.substring(2,4).equals("04"))
				{
					strMATTP ="2";
					exeTFISS(strISSNO,cmbMATTP.getSelectedItem().toString().substring(0,2));
				}
				else
				{
					exeISSNO(strISSNO,strMATTP);
				}*/
		}
		if((cl_dat.M_flgLCUPD_pbst)&&(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
		{
			strREMDS = txtREMDS.getText().trim();
			// To check whether the remark against the memo no is exists or not
			if(flgRMKFL){												// To update existing record
				M_strSQLQRY = "Update MM_RMMST set ";
				M_strSQLQRY += "RM_REMDS = '" + strREMDS + "',";
				M_strSQLQRY += "RM_TRNFL = '" + strTRNFL_fn + "',";
				M_strSQLQRY += "RM_LUSBY = '" + strLUSBY + "',";
				M_strSQLQRY += "RM_LUPDT = '" + strLUPDT +"'";
				M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + strSTRTP + "'";
				M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
				M_strSQLQRY += " and RM_DOCTP = '" + strMATTP + "'";
				M_strSQLQRY += " and RM_DOCNO = '" + strISSNO + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			else if(strREMDS.length() > 0){								// To add new record
				M_strSQLQRY = "Insert into MM_RMMST(rm_cmpcd,RM_STRTP,RM_TRNTP,";
				M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_LUSBY,";
				M_strSQLQRY += "RM_LUPDT) values (";
				M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
				M_strSQLQRY += "'" + strSTRTP + "',";
				M_strSQLQRY += "'" + strTRNTP_fn + "',";
				M_strSQLQRY += "'" + strMATTP + "',";
				M_strSQLQRY += "'" + strISSNO + "',";
				M_strSQLQRY += "'" + strREMDS + "',";
				M_strSQLQRY += "'" + strTRNFL_fn + "',";
				M_strSQLQRY += "'" + strLUSBY + "',";
				M_strSQLQRY += "'" + strLUPDT + "')";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
		}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				boolean L_flgATIND = false;
				flgRMKFL = false;
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Data saved successfully",'N');
					flgCYLFL = false;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						JOptionPane.showMessageDialog(this,"Please, Note down the Issue No. " + strISSNO,"Issue No.",JOptionPane.INFORMATION_MESSAGE);
						strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
						btnPRNT.setEnabled(true);
						clrCOMP();
						txtISSNO.setText(strISSNO);
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						if(flgSTKIT)
						{
						/*	if(!genAUTIND())
							{
								L_flgATIND = false;
								do
								{
									int L_SELOPT = JOptionPane.showConfirmDialog(this,"Record In Use,Press OK after some time to generate Auto Indent ..?","Auto Indent confirmation ",JOptionPane.OK_CANCEL_OPTION); 
									if(L_SELOPT == 0)
										L_flgATIND = genAUTIND();		
									else if(L_SELOPT ==1)
										L_flgATIND = true;
								}while(!L_flgATIND);
							}*/
						}
					}
					clrCOMP();		
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
				}
				else
				{
					setMSG("Error in saving data..",'E');
					setCursor(cl_dat.M_curDFSTS_pbst);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
				}
			}	
		}
		catch(Exception e)
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setMSG(e,"exeSAVE");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	private boolean getDATA()
	{
		try
		{
			ResultSet M_rstRSSET1;
			String L_strSTKFL ="";
			String L_strCYLMT ="";
			int L_intCYLCT =0;
			boolean L_FIRST = true;
			java.sql.Date L_datTEMP;
			java.sql.Timestamp L_tmsTEMP;
			int L_intROWCT = 0;
			hstMATCD.clear();
			strISSNO = txtISSNO.getText().trim();
			String L_strCYLMAT ="";
			tblISSVL.clrTABLE();
			tblCYLDT.clrTABLE();
			tblISSVL.setRowSelectionInterval(0,0);
			tblISSVL.setColumnSelectionInterval(0,0);
			tblCYLDT.setRowSelectionInterval(0,0);
			tblCYLDT.setColumnSelectionInterval(0,0);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{
			M_strSQLQRY = "Select IS_ISSDT,IS_DPTCD,IS_MATTP,IS_MATCD,ST_MATDS,IS_ISSTP,IS_TAGNO,IS_BATNO,IS_GRNNO,IS_REQQT,(ISNULL(GR_ACPQT,0) - ISNULL(GR_ISSQT,0))L_BALQT,";
			M_strSQLQRY += "IS_ISSQT,IS_ISSRT,IS_USGTP,IS_CCTCD,IS_PREBY,IS_AUTBY,IS_PREDT,IS_AUTDT,";
			M_strSQLQRY += "IS_LOCCD,IS_EXCCT,is_stsfl,IS_WORNO,IS_PRTTP,IS_PRTCD,ST_STKFL,IS_LUSBY,IS_LUPDT from MM_STMST,MM_ISMST left outer join MM_GRMST ON ";
			M_strSQLQRY += " GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_CMPCD = GR_CMPCD and IS_STRTP = GR_STRTP AND IS_MATCD = GR_MATCD AND IS_GRNNO = GR_GRNNO AND IS_BATNO = GR_BATNO AND ISNULL(GR_ACPQT,0)- ISNULL(GR_ISSQT,0) > 0";
			M_strSQLQRY += " where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_CMPCD = ST_CMPCD and IS_STRTP = ST_STRTP AND IS_MATCD=ST_MATCD AND ISNULL(IS_STSFL,'') <>'X' AND IS_STRTP = '" + strSTRTP + "'";
			M_strSQLQRY += " and IS_ISSNO = '" + strISSNO+ "'";
		}
		else
		{
			M_strSQLQRY = "Select IS_ISSDT,IS_DPTCD,IS_MATTP,IS_MATCD,ST_MATDS,IS_ISSTP,IS_TAGNO,IS_BATNO,IS_GRNNO,IS_REQQT,";
			M_strSQLQRY += "IS_ISSQT,IS_ISSRT,IS_USGTP,IS_CCTCD,IS_PREBY,IS_AUTBY,IS_PREDT,IS_AUTDT,";
			M_strSQLQRY += "IS_LOCCD,IS_EXCCT,is_stsfl,IS_WORNO,IS_PRTTP,IS_PRTCD,ST_STKFL,IS_LUSBY,IS_LUPDT from MM_ISMST,MM_STMST ";
			M_strSQLQRY += " where IS_STRTP = ST_STRTP AND IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MATCD = ST_MATCD AND IS_CMPCD=ST_CMPCD AND  ISNULL(IS_STSFL,'') <>'X' AND IS_STRTP = '" + strSTRTP + "'";
			M_strSQLQRY += " and IS_ISSNO = '" + strISSNO+ "'";
		}
		//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			flgSTKIT = false;
			boolean L_flgFIRST = false;
		   if(rstRSSET !=null)
			   while(rstRSSET.next())
			   {
					L_strSTKFL = nvlSTRVL(rstRSSET.getString("ST_STKFL"),"");
					if(L_FIRST)
					{
						strSTSFL = nvlSTRVL(rstRSSET.getString("IS_STSFL"),"");
												
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
						{
							if(strSTSFL.equals("2"))
							{
								setMSG("Given Issue number is already authorised..",'E');
								return false;
							}
						}
						strDPTCD = nvlSTRVL(rstRSSET.getString("IS_DPTCD"),"");
						cl_dat.M_txtUSER_pbst.setText(rstRSSET.getString("IS_LUSBY"));
						if(rstRSSET.getString("IS_LUPDT")!=null)
							cl_dat.M_txtDATE_pbst.setText(M_fmtLCDAT.format(rstRSSET.getDate("IS_LUPDT")));
						if(!strDPTCD.equals(txtDPTCD.getText().trim()))
					    {
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							{
								setMSG("Given Issue number belongs to some other department..",'E');
								return false;
							}
						}
						txtDPTCD.setText(strDPTCD);
						txtDPTDS.setText(hstDPTCD.get(strDPTCD).toString());
						strMATTP = nvlSTRVL(rstRSSET.getString("IS_MATTP"),"");
						for(int i=0;i<cmbMATTP.getItemCount();i++)
						{
							if(cmbMATTP.getItemAt(i).toString().substring(0,1).equals(strMATTP))
							{
								cmbMATTP.setSelectedIndex(i);
								break;
							}
						}
						strPRTTP = nvlSTRVL(rstRSSET.getString("IS_PRTTP"),"");
						for(int i=0;i<cmbPRTTP.getItemCount();i++)
						{
							if(cmbPRTTP.getItemAt(i).toString().substring(0,1).equals(strPRTTP))
							{
								cmbPRTTP.setSelectedIndex(i);
								break;
							}
						}
						txtPRTCD.setText(nvlSTRVL(rstRSSET.getString("IS_PRTCD"),""));
						txtWORNO.setText(nvlSTRVL(rstRSSET.getString("IS_WORNO"),""));
						txtEXCCT.setText(nvlSTRVL(rstRSSET.getString("IS_EXCCT"),""));
						M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='"+cmbPRTTP.getSelectedItem().toString().substring(0,1) +"'";
						M_strSQLQRY +=" AND PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
						M_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(M_rstRSSET1 !=null)
						{
							if(M_rstRSSET1.next())
							{
								txtPRTCD.transferFocus();
								txtPRTNM.setText(nvlSTRVL(M_rstRSSET1.getString("PT_PRTNM"),""));
							}
							M_rstRSSET1.close();
						}
						L_datTEMP = rstRSSET.getDate("IS_ISSDT");
						strISSDT ="";
						if(L_datTEMP !=null)
						{
							strISSDT = M_fmtLCDAT.format(L_datTEMP);
						}
						txtISSDT.setText(strISSDT);
						L_tmsTEMP = rstRSSET.getTimestamp("IS_PREDT");
						if(L_tmsTEMP !=null)
						{
							strPRDTM = M_fmtLCDTM.format(L_tmsTEMP);
							txtPREDT.setText(strPRDTM.substring(0,10));
							txtPRETM.setText(strPRDTM.substring(11));
						}
						else 
						{
							txtPREDT.setText("");
							txtPRETM.setText("");
						}
						L_tmsTEMP = rstRSSET.getTimestamp("IS_AUTDT");
						if(L_tmsTEMP !=null)
						{
							strAUDTM = M_fmtLCDTM.format(L_tmsTEMP);
							txtAUTDT.setText(strAUDTM.substring(0,10));
							txtAUTTM.setText(strPRDTM.substring(11));
						}
						else 
						{
							txtAUTDT.setText("");
							txtAUTTM.setText("");
						}
						txtISSDT.setText(strISSDT);
						txtPREBY.setText(nvlSTRVL(rstRSSET.getString("IS_PREBY"),""));
						txtAUTBY.setText(nvlSTRVL(rstRSSET.getString("IS_AUTBY"),""));
						L_FIRST = false;
						
					}
					if(L_strSTKFL.equals("Y"))
						flgSTKIT = true;
					strMATCD = nvlSTRVL(rstRSSET.getString("IS_MATCD"),"");
					strTAGNO = nvlSTRVL(rstRSSET.getString("IS_TAGNO"),"");
					strBATNO = nvlSTRVL(rstRSSET.getString("IS_BATNO"),"");
					strGRNNO = nvlSTRVL(rstRSSET.getString("IS_GRNNO"),"");
					strMATDS = nvlSTRVL(rstRSSET.getString("ST_MATDS"),"");
					hstMATCD.put(strMATCD.trim()+strTAGNO.trim()+strBATNO+strGRNNO,strMATDS);
					tblISSVL.setValueAt(strMATCD,L_intROWCT,TBL_MATCD);
					//tblISSVL.setValueAt(strMATDS,L_intROWCT,TBL_MATDS);
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_TAGNO"),""),L_intROWCT,TBL_CCTCD);
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_BATNO"),""),L_intROWCT,TBL_BATNO);
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_GRNNO"),""),L_intROWCT,TBL_GRNNO);
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_BATNO"),""),L_intROWCT,TBL_OBTNO);
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_GRNNO"),""),L_intROWCT,TBL_OGRNO);

					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
						tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("L_BALQT"),"0"),L_intROWCT,TBL_BALQT);
					strLOCCD = nvlSTRVL(rstRSSET.getString("IS_LOCCD"),"");
					tblISSVL.setValueAt(strLOCCD,L_intROWCT,TBL_LOCCD);
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_REQQT"),"0"),L_intROWCT,TBL_REQQT);	
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_ISSQT")," "),L_intROWCT,TBL_ISSQT);	
					tblISSVL.setValueAt(nvlSTRVL(rstRSSET.getString("IS_USGTP"),""),L_intROWCT,TBL_USGTP);	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						tblISSVL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_CHKFL);
						tblISSVL.setValueAt(tblISSVL.getValueAt(L_intROWCT,TBL_REQQT),L_intROWCT,TBL_ISSQT);	
						txtAUTBY.setText(cl_dat.M_strUSRCD_pbst);
						txtAUTDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						txtAUTTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					
						tblISSVL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_CHKFL);
					else 

						tblISSVL.setValueAt(Boolean.FALSE,L_intROWCT,TBL_CHKFL);
					flgCYLFL = false;
					
					for(int j=0;j<staCYLCD.length;j++)
					{
						if(tblISSVL.getValueAt(L_intROWCT,TBL_MATCD).toString().substring(0,staCYLCD[j].length()).equals(staCYLCD[j]))
						{
							L_intCYLCT++;
							if(!L_flgFIRST)
							{
								L_strCYLMT = "'"+strMATCD +"'";
								L_flgFIRST = true;
							}
							else
								L_strCYLMT += ",'"+strMATCD +"'";	
							flgCYLFL = true;
						}
					}	
					
					intTBLRW = L_intROWCT;
					vldMATCD(strMATCD,L_intROWCT);
					L_intROWCT++;
				}
		   if(L_intROWCT ==0)
		   {
			   setMSG("Data not found..",'E');
			   return false;
		   }
			// check for the existance of remark
			M_strSQLQRY = "Select RM_REMDS from MM_RMMST ";
			M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + strSTRTP + "'";
			M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
			M_strSQLQRY += " and RM_DOCNO = '" + strISSNO + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			txtREMDS.setText("");
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					flgRMKFL = true;
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
				}
				M_rstRSSET.close();
			}
			intTBLRW = tblISSVL.getSelectedRow();
			intTBLCL = tblISSVL.getSelectedColumn();
			if(rstRSSET != null)
				rstRSSET.close();
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))&& (L_strCYLMT.length() >0))
				M_strSQLQRY = "Select * from MM_CLTRN where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and CLT_STSFL ='1' and CLT_MATCD IN("+L_strCYLMT+") order by CLT_MATCD,CLT_SRLNO";
			else
				M_strSQLQRY = "Select DISTINCT CLT_MATCD,CLT_SRLNO,CLT_RFDAY from MM_CLTRN where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and CLT_ISSNO ='"+txtISSNO.getText().trim()+"' order by CLT_MATCD,CLT_SRLNO";
			System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				int i=0;
				while (M_rstRSSET.next())
				{
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_MATCD"),i,1);
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_SRLNO"),i,2);
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_RFDAY"),i++,3);
				}
			}
		///***
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			if(L_intROWCT > 0)
				return true;
			else 
				return false;
			
		}catch(Exception e){
			setMSG(e,"getDATA");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();	
		try
		{
			intTBLRW = tblISSVL.getSelectedRow();
			intTBLCL = tblISSVL.getSelectedColumn();
			if(M_strHLPFLD.equals("txtISSNO"))
			{				
				txtISSNO.setText(cl_dat.M_strHLPSTR_pbst);
				if(getDATA())
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
						setENBL(true);
					else
						setENBL(false);
					txtISSNO.requestFocus();
				}
			}
			else if(M_strHLPFLD.equals("txtEXCCT"))
			{
				txtEXCCT.setText(cl_dat.M_strHLPSTR_pbst);
				txtEXCDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtPRTCD"))
			{
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtDPTCD"))
			{
				txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtDPTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtCCTCD"))
				txtCCTCD.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtBATNO"))
			{
				txtBATNO.setText(cl_dat.M_strHLPSTR_pbst);
				tblISSVL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim(),intTBLRW,TBL_GRNNO);
				tblISSVL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intTBLRW,TBL_BALQT);

			}
			else if(M_strHLPFLD.equals("txtCCTCD1"))
				txtCCTCD.setText(cl_dat.M_strHLPSTR_pbst);
			else if((M_strHLPFLD.equals("txtMATCD"))||(M_strHLPFLD.equals("txtMATCD1")))
			{		
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				strMATCD = cl_dat.M_strHLPSTR_pbst;
				txtMATCD.setText(strMATCD);
				vldMATCD(strMATCD,tblISSVL.getEditingRow());
				tblISSVL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intTBLRW,TBL_UOMCD);
			}
			else if(M_strHLPFLD.equals("txtLOCCD"))
			{			// Location/Tank Code
				txtLOCCD.setText(cl_dat.M_strHLPSTR_pbst);
				tblISSVL.setValueAt(cl_dat.M_strHLPSTR_pbst,tblISSVL.getSelectedRow(),TBL_LOCCD);
				strISSDT = txtISSDT.getText().trim();
			}
			else if(M_strHLPFLD.equals("txtUSGTP"))
			{			// Usage Type
				txtUSGTP.setText(cl_dat.M_strHLPSTR_pbst);
				tblISSVL.setValueAt(cl_dat.M_strHLPSTR_pbst,intTBLRW,TBL_USGTP);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	// Validation of Usage Type
	private boolean vldUSGTP(String P_strUSGTP)
	{
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'MMXXUSG'";
			M_strSQLQRY += " and CMT_CODCD = '" + P_strUSGTP + "'";
	    	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			setMSG("Invalid Usage Type.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldUSGTP");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}	
		return false;
	}
	// Validation of Material
	private boolean vldMATCD(String P_strMATCD,int P_intROWID)
	{
		try
		{
			String L_STKFL ="";
			strSTRTP =M_strSBSCD.substring(2,4);
			if(strSTRTP.equals(strTKSTR_fn))
			{
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD,CT_PKGTP from CO_CTMST,MM_TKMST";
				M_strSQLQRY += " where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and CT_MATCD = '" + P_strMATCD + "'";
			
				M_strSQLQRY += " and TK_MATCD = CT_MATCD AND TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
				if(M_rstRSSET.next())
				{
					tblISSVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),P_intROWID,TBL_UOMCD);
					tblISSVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_PKGTP"),""),P_intROWID,TBL_STDPK);
					setMSG("",'N');
					M_rstRSSET.close();			
					return true;
				}
        	}
			else
			{
				if(strMATTP.equals(strPKGMT_fn))
				{
					if(!P_strMATCD.substring(0,2).equals("69"))
					{
						setMSG("Invalid Material code for Packing category..",'E');	
						return false;
					}
				}
				else if(strMATTP.equals(strRAWMT_fn))
				{
					if(M_strSBSCD.substring(2,4).equals("01"))
					{
						setMSG("Raw Material category not allowed under Engg. stores..",'N');	
						return false;	
					}
					else
					{ 
						if(!P_strMATCD.substring(0,2).equals("68"))
						{
							if(!P_strMATCD.substring(0,2).equals("99"))
							{
								setMSG("Invalid Material code for Raw Material category..",'E');	
								return false;	
							}
						}
					}
				}
				else if(strMATTP.equals(strSTRSP_fn))
				{
					if(strMATTP.equals(strRVSTR_fn))
					{
						if(!P_strMATCD.substring(0,2).equals("95"))
						{
							setMSG("Invalid Material code for Revamp stores..",'E');	
							return false;	
						}
					}
					else
					{
						if((P_strMATCD.substring(0,2).equals("68"))||(P_strMATCD.substring(0,2).equals("69")))
						{
							setMSG("Invalid Material code for Stores and Spares category..",'E');	
							return false;	
						}
					}
				}
				
				M_strSQLQRY = "select ST_MATCD,ST_MATDS,ST_UOMCD,ST_STKQT,ST_LOCCD,ST_STKFL,CT_PKGTP from MM_STMST,CO_CTMST";
				M_strSQLQRY += " where CT_CODTP ='CD' AND ST_MATCD = CT_MATCD AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ISNULL(st_stkqt,0) > 0 and ";
				M_strSQLQRY += " ST_STRTP = '" + M_strSBSCD.substring(2,4) + "' AND ";
				M_strSQLQRY += " ST_MATCD = '" + P_strMATCD + "'";
			}
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				L_STKFL = nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"");
				tblISSVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),P_intROWID,TBL_UOMCD);
				tblISSVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"),P_intROWID,TBL_AVLQT);
				hstSTKQT.put(P_strMATCD,M_rstRSSET.getString("ST_STKQT"));
				tblISSVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),P_intROWID,TBL_LOCCD);
				if(L_STKFL.equals("Y"))
					tblISSVL.setValueAt(Boolean.TRUE,P_intROWID,TBL_STKFL);
				else
					tblISSVL.setValueAt(Boolean.FALSE,P_intROWID,TBL_STKFL);
				tblISSVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_PKGTP"),""),P_intROWID,TBL_STDPK);
				if(P_intROWID > 0)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						tblISSVL.setValueAt(tblISSVL.getValueAt(P_intROWID-1,TBL_USGTP),P_intROWID,TBL_USGTP);
						tblISSVL.setValueAt(tblISSVL.getValueAt(P_intROWID-1,TBL_CCTCD),P_intROWID,TBL_CCTCD);
					}
					setMSG("press F1 to select the item code ..",'N');
				}
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			tblISSVL.setValueAt(new Boolean(false),P_intROWID,TBL_CHKFL);
			tblISSVL.setValueAt("",P_intROWID,TBL_UOMCD);
			tblISSVL.setValueAt("",P_intROWID,TBL_LOCCD);
			tblISSVL.setValueAt("",P_intROWID,TBL_AVLQT);
			tblISSVL.setValueAt(Boolean.FALSE,P_intROWID,TBL_STKFL);
			setMSG("Invalid Material Code.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception e){
			setMSG(e,"vldMATCD");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	private boolean vldCCTCD(String P_strCCTCD)
	{
		try
		{
			M_strSQLQRY = "select EQ_TAGNO from MM_EQMST";
			M_strSQLQRY +=" where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EQ_TAGNO ='"+P_strCCTCD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				M_rstRSSET.close();			
				return true;
			}
			setMSG("Invalid Cost Center Code.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"vldMATCD");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	// Help on Material Type 
	private void hlpEXCCT()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtEXCCT";
			String L_ARRHDR[] = {"Code","Material Type"};
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"hlpMATTP");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}
	private boolean vldEXCCT(String P_strMATTP)
	{
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
			M_strSQLQRY += " and CMT_CODCD = '" + P_strMATTP + "'";
			M_strSQLQRY += " and CMT_STSFL <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				txtEXCDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();			
				return true;
			}
			setMSG("Invalid Excise Category. Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"vldEXCCT ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	// Validation of Location Code
	private boolean vldLOCCD(String P_strLOCCD,String P_strMATCD)
	{
		try
		{
			M_strSQLQRY = "Select TK_TNKNO from MM_TKMST";
			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_TNKNO = '" + P_strLOCCD + "'";
        	M_strSQLQRY += " and TK_MATCD = '" + P_strMATCD + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				strLOCCD = nvlSTRVL(M_rstRSSET.getString("TK_TNKNO"),"");
				tblISSVL.setValueAt(strLOCCD,intTBLRW,TBL_LOCCD);
				strISSDT = txtISSDT.getText().trim();
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			tblISSVL.setValueAt("",intTBLRW,TBL_LOCCD);
			tblISSVL.setValueAt("",intTBLRW,TBL_AVLQT);
			setMSG("Invalid Location.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"vldLOCCD ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	private String getMATDS(String P_strMATCD)
	{
		String L_MATDS = "";
		try
		{
			M_strSQLQRY = "Select CT_UOMCD,CT_MATDS from CO_CTMST";
			M_strSQLQRY += " where CT_MATCD = '" + P_strMATCD + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				L_MATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"").trim();
				M_rstRSSET.close();
			}	
		}
		catch(Exception e)
		{
			setMSG(e,"getMATDS ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_MATDS;
	}
	// Method to generate the new Issue No.
	private String genISSNO(String P_strMATTP)
	{
		String L_ISSNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for issues;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+strSTRTP+"ISS' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + P_strMATTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP01.trim().length() >0)
					{
						M_rstRSSET.close();
						return null;
					}
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+strSTRTP+"ISS'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + P_strMATTP + "'";			
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT("genISSNO"))
			{
				L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
				for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
					L_ISSNO += "0";
				L_CCSVL = L_ISSNO + L_CCSVL;
				L_ISSNO = L_CODCD + L_CCSVL;
				txtISSNO.setText(L_ISSNO);
			}
			else 
				return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genISSNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_ISSNO;
	}
	
	// Method to update the last Issue No.in the CO_CDTRN
	private void exeISSNO(String P_strISSNO,String P_strMATTP)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + P_strISSNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+strSTRTP+"ISS'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + P_strMATTP + "'";			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeISSNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	// Method to get the available Quantity from Dip Register
	private String getAVLQT(String P_strTNKNO,String P_strMEMDT)
	{
		String L_AVLQT = "0.000";
		String L_RCTQT = "0";
		String L_DOPQT1 = "0";
		String L_DOPQT2 = "0";
		try
		{// Previous Date's Dip
			M_strSQLQRY = "Select DP_DOPQT,DP_RCTQT from MM_DPTRN ";
			M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP_fn + "'";
			M_strSQLQRY += " and DP_TNKNO = '" + P_strTNKNO + "'";
			M_strSQLQRY += " and CONVERT(varchar,DP_MEMDT,103) = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strMEMDT))+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_DOPQT1 = nvlSTRVL(M_rstRSSET.getString("DP_DOPQT"),"0");
					L_RCTQT = nvlSTRVL(M_rstRSSET.getString("DP_RCTQT"),"0");
				}
				M_rstRSSET.close();
			}
			// Today's Dip
			M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strMEMDT));
			M_calLOCAL.add(Calendar.DATE,+1);
			P_strMEMDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
			M_strSQLQRY = "Select DP_DOPQT from MM_DPTRN ";
			M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP_fn + "'";
			M_strSQLQRY += " and DP_TNKNO = '" + P_strTNKNO + "'";
			M_strSQLQRY += " and CONVERT(varchar,DP_MEMDT,103) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strMEMDT))+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					L_DOPQT2 = nvlSTRVL(M_rstRSSET.getString("DP_DOPQT"),"0");
				M_rstRSSET.close();
			}
		L_AVLQT = setNumberFormat(Double.parseDouble(L_DOPQT1) + Double.parseDouble(L_RCTQT),3);
		strCALQT = setNumberFormat(Double.parseDouble(L_AVLQT) - Double.parseDouble(L_DOPQT2),3);
		}catch(Exception e){
			setMSG(e,"getAVLQT");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_AVLQT;
	}
	// Method to update the Issued Quantity in Dip Register(previous Day)
	private void updDPTRN(String P_strMEMDT,String P_strTNKNO,String P_strISSQT)
	{
		try
		{
			M_strSQLQRY = "Update MM_DPTRN set ";
			M_strSQLQRY += "DP_ISSQT = DP_ISSQT + " + P_strISSQT + ",";
			M_strSQLQRY += getUSGDTL("DP",'U',"");
			M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,DP_MEMDT,103) = '" + P_strMEMDT +"'";
			M_strSQLQRY += " and DP_TNKNO = '" + P_strTNKNO + "'";
			M_strSQLQRY += " and DP_MEMTP = '" + strREGDP_fn + "'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");			
		}
		catch(Exception e)
		{
			setMSG(e,"updDPTRN");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	// Method to check whether Requested Quantity is valid or Not
	private boolean vldREQQT(int LP_TBLROW,String LP_REQQT)
	{
		try
		{
			float L_AVLQT = 0,L_ADVQT = 0,L_REQQT = 0;
			String L_ISSTP;
			L_AVLQT = Float.parseFloat(nvlSTRVL(tblISSVL.getValueAt(LP_TBLROW,TBL_AVLQT).toString(),"0"));
			if(L_REQQT > L_AVLQT)
			{
				setMSG("Requested Quantity can not be greater than Available Quantity",'E');
				return false;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"vldREQQT ");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		return true;
	}
	// Method to check whether Issued Quantity is valid or Not
	private boolean vldISSQT(int LP_TBLROW,String LP_ISSQT)
	{
		try
		{
			float L_REQQT = 0,L_ISSQT = 0;
			try
			{
				L_ISSQT = Float.parseFloat(LP_ISSQT);
				L_REQQT = Float.parseFloat(tblISSVL.getValueAt(LP_TBLROW,TBL_REQQT).toString());
			}
			catch(NumberFormatException e)
			{
				setMSG("Please Enter Quantity in valid format",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
			if(L_ISSQT > L_REQQT)
			{
				setMSG("Issued Quantity can not be greater than Requested Quantity",'E');
				return false;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"vldISSQT ");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		return true;
	}
	
	/** Method to get the Issued Records from Dip register(MM_DPTRN) for given date
	    First available Quantity is calculated from MM_DPTRN using 
	    L_AVLQT = L_dblDOPQT + L_dblRCTQT + L_dblTRNQT - L_dblDCLQT - L_dblISSQT
	    then Advanced liceneced qty / Annexure 45 qty is calculated,
	    which is pending for issue.
	*/
    private boolean getISSUE(String P_strMEMDT)
	{
		try
		{
			int L_intROWCNT = 0;
			String L_strTNKNO,L_strMATCD,L_strUOMCD;
			double L_dblDOPQT,L_dblRCTQT,L_dblISSQT,L_dblTRNQT,L_dblDCLQT;
            String L_strAVLQT = "0",L_strADVQT = "0",L_strANXQT = "0",L_strDFCQT = "0",L_strTGPQT ="0";
			tblISSVL.clrTABLE();
            strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
            if(tblISSVL.isEditing())
            {
                tblISSVL.getCellEditor().stopCellEditing();
                tblISSVL.setRowSelectionInterval(0,0);
                tblISSVL.setColumnSelectionInterval(0,0);
            }
            // Get Opening Stock, Receipt & Closing Stock from Dip Register for given date
			M_strSQLQRY = "Select DP_MATCD,CT_UOMCD,DP_TNKNO,DP_DOPQT,DP_RCTQT,DP_ISSQT," +
						"DP_TRNQT,DP_DCLQT from MM_DPTRN,CO_CTMST " +
						" where DP_MATCD = CT_MATCD" +
						" and DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP_fn + "'" +
						" and CONVERT(varchar,DP_MEMDT,103) = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strMEMDT))+"'"+
						" order by DP_MATCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strMATCD = nvlSTRVL(M_rstRSSET.getString("DP_MATCD"),"");
					L_strUOMCD = M_rstRSSET.getString("CT_UOMCD");
					L_strTNKNO = M_rstRSSET.getString("DP_TNKNO");
					L_dblDOPQT = M_rstRSSET.getDouble("DP_DOPQT");
					L_dblRCTQT = M_rstRSSET.getDouble("DP_RCTQT");
					L_dblISSQT = M_rstRSSET.getDouble("DP_ISSQT");
					L_dblTRNQT = M_rstRSSET.getDouble("DP_TRNQT");
					L_dblDCLQT = M_rstRSSET.getDouble("DP_DCLQT");
					L_strAVLQT = setNumberFormat((L_dblDOPQT + L_dblRCTQT + L_dblTRNQT - L_dblDCLQT - L_dblISSQT),3);
					L_strADVQT = getPENQT(L_strMATCD,"",strADVTP_fn);
					L_strANXQT = getPENQT(L_strMATCD,"",strANXTP_fn);
                    L_strDFCQT = getPENQT(L_strMATCD,"",strDFCTP_fn);
                    L_strTGPQT = getPENQT(L_strMATCD,"",strTGPTP_fn);
               		// If excise category is other than advanced licence
					// validation skipped for Job work outside on 22/03/2006 API HBP
					if((Double.parseDouble(L_strAVLQT) >0)&&(!cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSJO_fn)))
					{
    					if(!txtEXCCT.getText().trim().equals(strADVTP_fn))
    					{
    					    if(Double.parseDouble(L_strADVQT) >0 && !cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSSL_fn))
    					    {
    					         setMSG("Advanced Licence quantity is pending for .."+L_strMATCD,'E');   
    					         return false;
    					    }
                            if((txtEXCCT.getText().trim().equals(strEXBTP_fn))&&(Double.parseDouble(L_strANXQT) >0))
    					    {
    					          setMSG("Annexure-45 quantity is pending for.."+L_strMATCD,'E');   
        					      return false;
        					}
                            if((txtEXCCT.getText().trim().equals(strEXBTP_fn))&&(Double.parseDouble(L_strDFCQT) >0))
    					    {
    					          setMSG("DFCE quantity is pending for.."+L_strMATCD,'E');   
        					      return false;
        					}
                            if((txtEXCCT.getText().trim().equals(strEXBTP_fn))&&(Double.parseDouble(L_strTGPQT) >0))
    					    {
    					          setMSG("Target Plus quantity is pending for.."+L_strMATCD,'E');   
        					      return false;
    					    }
    					    if((txtEXCCT.getText().trim().equals(strTGPTP_fn))&&(Double.parseDouble(L_strANXQT) >0))
    					    {
    					          setMSG("Annexure-45 quantity is pending for.."+L_strMATCD,'E');   
        					      return false;
        					}
    		                if((txtEXCCT.getText().trim().equals(strTGPTP_fn))&&(Double.parseDouble(L_strDFCQT) >0))
    					    {
    					          setMSG("DFCE quantity is pending for.."+L_strMATCD,'E');   
        					      return false;
        					}			    
    					    if((txtEXCCT.getText().trim().equals(strDFCTP_fn))&&(Double.parseDouble(L_strANXQT) >0))
    					    {
    					          setMSG("Annexure-45 quantity is pending for.."+L_strMATCD,'E');   
        					      return false;
        					}
                            
    					}
    					if(txtEXCCT.getText().trim().equals(strEXBTP_fn))
    					{
    					    // If condition added on 22/03/2006 to skip validation for job work -outside
    					    if(!cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSJO_fn))
    					    {
        					    if(Double.parseDouble(L_strADVQT) >0 &&  !cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSSL_fn))
        					        setMSG("Advanced Licence quantity is pending for .."+L_strMATCD,'E');
        					    else if(Double.parseDouble(L_strANXQT) >0) 
        					        setMSG("Annexure-45  quantity is pending for .."+L_strMATCD,'E');
        					    else if(Double.parseDouble(L_strDFCQT) >0) 
        					        setMSG("DFCE  quantity is pending for .."+L_strMATCD,'E');
        					    else if(Double.parseDouble(L_strTGPQT) >0) 
        					        setMSG("Target Plus  quantity is pending for .."+L_strMATCD,'E');
    					    }
    					}
    					// Added on 29/12/2005 for DFCE category provision
    					else if(txtEXCCT.getText().trim().equals(strDFCTP_fn))
    					{
    					    if(Double.parseDouble(L_strADVQT) >0  && !cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSSL_fn))
    					        setMSG("Advanced Licence quantity is pending for .."+L_strMATCD,'E');
    					    else if(Double.parseDouble(L_strANXQT) >0) 
    					        setMSG("Annexure-45  quantity is pending for .."+L_strMATCD,'E');
    					}
    					else if(txtEXCCT.getText().trim().equals(strTGPTP_fn))
    					{
    					    if(Double.parseDouble(L_strADVQT) >0 && !cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSSL_fn))
    					        setMSG("Advanced Licence quantity is pending for .."+L_strMATCD,'E');
    					    else if(Double.parseDouble(L_strANXQT) >0) 
    					        setMSG("Annexure-45  quantity is pending for .."+L_strMATCD,'E');
    					    else if(Double.parseDouble(L_strDFCQT) >0) 
    					        setMSG("DFCE  quantity is pending for .."+L_strMATCD,'E');
    					}
    					else if(txtEXCCT.getText().trim().equals(strANXTP_fn))
    					{
    					    if(Double.parseDouble(L_strADVQT) >0 && !cmbISSTP.getSelectedItem().toString().substring(0,2).equals(strISSSL_fn))
    					        setMSG("Advanced Licence quantity is pending for .."+L_strMATCD,'E');
    					}
    					//
    				}
					if(txtEXCCT.getText().trim().equals(strADVTP_fn))
					{ 
			            if(Double.parseDouble(L_strADVQT) <= 0)
                            continue;
					}
					else if(txtEXCCT.getText().trim().equals(strANXTP_fn))
					{ 
			            if(Double.parseDouble(L_strANXQT) <= 0)
                            continue;
					}
					else if(txtEXCCT.getText().trim().equals(strDFCTP_fn))
					{ 
			            if(Double.parseDouble(L_strDFCQT) <= 0)
                            continue;
					}
					else if(txtEXCCT.getText().trim().equals(strTGPTP_fn))
					{ 
			            if(Double.parseDouble(L_strTGPQT) <= 0)
                            continue;
                    }
					else
						L_strADVQT = L_strAVLQT;
				    if(Double.parseDouble(L_strAVLQT) > 0)
					{
						tblISSVL.setValueAt(new Boolean(true),L_intROWCNT,TBL_CHKFL);
                        tblISSVL.setValueAt(L_strMATCD,L_intROWCNT,TBL_MATCD);
						tblISSVL.setValueAt(L_strUOMCD,L_intROWCNT,TBL_UOMCD);
						tblISSVL.setValueAt(strDMCCT,L_intROWCNT,TBL_CCTCD);
                        tblISSVL.setValueAt(L_strTNKNO,L_intROWCNT,TBL_LOCCD);
                        if(txtEXCCT.getText().trim().equals(strANXTP_fn))
                            tblISSVL.setValueAt(L_strANXQT,L_intROWCNT,TBL_AVLQT);
                        else if(txtEXCCT.getText().trim().equals(strDFCTP_fn))
                            tblISSVL.setValueAt(L_strDFCQT,L_intROWCNT,TBL_AVLQT);
                            else if(txtEXCCT.getText().trim().equals(strTGPTP_fn))
                            tblISSVL.setValueAt(L_strTGPQT,L_intROWCNT,TBL_AVLQT);
                        else
                            tblISSVL.setValueAt(L_strADVQT,L_intROWCNT,TBL_AVLQT);
                        tblISSVL.setValueAt(L_strAVLQT,L_intROWCNT,TBL_REQQT);
                        tblISSVL.setValueAt(L_strAVLQT,L_intROWCNT,TBL_ISSQT);
						if(txtEXCCT.getText().trim().equals(strADVTP_fn))
						if(Double.parseDouble(L_strAVLQT) > Double.parseDouble(L_strADVQT))
							 tblISSVL.setValueAt(L_strADVQT,L_intROWCNT,TBL_ISSQT);
						if(txtEXCCT.getText().trim().equals(strANXTP_fn))
						if(Double.parseDouble(L_strAVLQT) > Double.parseDouble(L_strANXQT))
							 tblISSVL.setValueAt(L_strANXQT,L_intROWCNT,TBL_ISSQT);
						if(txtEXCCT.getText().trim().equals(strDFCTP_fn))
						if(Double.parseDouble(L_strAVLQT) > Double.parseDouble(L_strDFCQT))
							 tblISSVL.setValueAt(L_strDFCQT,L_intROWCNT,TBL_ISSQT);	 
					   if(txtEXCCT.getText().trim().equals(strTGPTP_fn))
						if(Double.parseDouble(L_strTGPQT) > Double.parseDouble(L_strDFCQT))
							 tblISSVL.setValueAt(L_strTGPQT,L_intROWCNT,TBL_ISSQT);	 
					    L_intROWCNT++;
					}
				}
				M_rstRSSET.close();
			}
			if(L_intROWCNT > 0)
                return true;
		}
		catch(Exception e)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(e,"getISSUE");
		}
		//if(txtEXCCT.getText().trim().equals(strADVIS_fn))
		if(txtEXCCT.getText().trim().equals(strADVTP_fn))
		    setMSG("Adv.Lic.Qty. is not available for issue.Check for Annexure-45 Qty.",'E');
		else if(txtEXCCT.getText().trim().equals(strANXTP_fn))
		    setMSG("Annexure-45 is not available for issue.Check for DFCE Qty.",'E');
		else if(txtEXCCT.getText().trim().equals(strDFCTP_fn))
		    setMSG("DFCE Type is not available for issue.Check for Exbonded Qty.",'E');
		else if(txtEXCCT.getText().trim().equals(strTGPTP_fn))
		    setMSG("Target Plus Type is not available for issue.Check for Exbonded Qty.",'E');
        else
            setMSG("No issues are pending.",'E');
		return false;
	}
	// Method to get the Available Advanced-License/ Annexure-45 Quantity
	// for getting the mat code wise qty pass LP_TNKNO as blank
    // Pass Advanced lic / Annexure 45 as Excise Category
	// Advance Lience quantity is calcultated by checking the Advance licence qty.
	// recieve from mm_wbtrn and subtractiong the advance licence issue quantity from Issues.
	private String getPENQT(String P_strMATCD,String P_strTNKNO,String P_strEXCCT)
	{
		String L_strADVQT = "0",L_strISSQT = "0",L_strAVLQT = "0";
		try
		{
			M_strSQLQRY = "Select sum(WB_UOMQT) L_ADVQT from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '01'";
			M_strSQLQRY += " and WB_MATTP = '" + P_strEXCCT + "'";
			M_strSQLQRY += " and WB_MATCD = '" + P_strMATCD + "'";
			M_strSQLQRY += " and WB_ACPTG = 'A'" ;
			// For tankwise Adv .lic. qty LP_TNKNO <> ' '
			if(P_strTNKNO.trim().length() >0)
				M_strSQLQRY += " and WB_TNKNO = '" + P_strTNKNO + "'";
			M_strSQLQRY += " and WB_STSFL <> 'X'";
            rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
            if(rstRSSET != null)
			{
                if(rstRSSET.next())
                    L_strADVQT = nvlSTRVL(rstRSSET.getString("L_ADVQT"),"0");
                rstRSSET.close();
			}
			M_strSQLQRY = "Select sum(IS_ISSQT) L_ISSQT from MM_ISMST";
			M_strSQLQRY += " where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MATCD = '" + P_strMATCD + "'";
			// Previousl checking was with IS_MATTP
			M_strSQLQRY += " and IS_EXCCT = '" + P_strEXCCT + "'";
			////M_strSQLQRY += " and IS_BATNO = '" + strDFLBT + "'"; // check it, commented on 08/08/07 for batch no  modi 
			//M_strSQLQRY += " and ISNULL(IS_STSFL,'') <> 'X'";
			M_strSQLQRY += " and ISNULL(IS_STSFL,'') = '2'";
			// If authorisation is taken, status flag check to be included.
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
            if(rstRSSET != null)
			{
                if(rstRSSET.next())
                    L_strISSQT = nvlSTRVL(rstRSSET.getString("L_ISSQT"),"0");
                rstRSSET.close();
			}
			L_strAVLQT = setNumberFormat(Double.parseDouble(L_strADVQT) - Double.parseDouble(L_strISSQT),3);	
		}
		catch(Exception e)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(e,"getADVQT ");
		}
		return L_strAVLQT;
	}
	boolean vldDATA()
	{
		try
		{
			int L_intCYLCT =0;
			strDPTCD = txtDPTCD.getText().trim();
			strISSDT = txtISSDT.getText().trim();
			strPREBY = txtPREBY.getText().trim();
			strAUTBY = txtAUTBY.getText().trim();
			strEXCCT =  txtEXCCT.getText().trim();
			String L_strMATCD,L_strISSQT,L_strTAGNO,L_strBATNO,L_strGRNNO;
			double L_dblISSQT =0;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			    hstISSQT = new Hashtable<String,String>();
			if(strISSDT.length() == 0)
			{	// Issue Date
				txtISSDT.requestFocus();
				setMSG("Please Enter the Issue Date",'E');
				return false;
			}
			else if(txtDPTCD.getText().trim().length() ==0)
			{
				txtDPTCD.requestFocus();
				setMSG("Please select the department code..",'E');
				return false;
			}
			if(strSTRTP.equals(strTKSTR_fn))
				if(strEXCCT.length() == 0)
				{				
					txtEXCCT.requestFocus();
					setMSG("Please Enter the Excise Category, F1 to select from the list..",'E');
					return false;
				}
			for(int i=0;i<tblISSVL.getRowCount();i++)
			{
				int L_intROWID = tblISSVL.getSelectedRow();
				L_strMATCD = tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_MATCD).toString();
				L_strTAGNO = tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_CCTCD).toString();
				L_strBATNO = tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_BATNO).toString();
				L_strGRNNO = tblISSVL.getValueAt(tblISSVL.getSelectedRow(),TBL_GRNNO).toString();

				if(tblISSVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(tblISSVL.getValueAt(i,TBL_MATCD).toString().length() == 0)
					{
						tblISSVL.editCellAt(i,TBL_MATCD);
						setMSG("Item code can not be empty..",'E');
						return false;
					}
					else if(tblISSVL.getValueAt(i,TBL_REQQT).toString().length() == 0)
					{
						tblISSVL.editCellAt(i,TBL_REQQT);
						setMSG("Requested qty can not be empty..",'E');
						return false;
					}
					else if(tblISSVL.getValueAt(i,TBL_CCTCD).toString().length() == 0)
					{
						tblISSVL.editCellAt(i,TBL_CCTCD);
						setMSG("Cost center code can not be empty..",'E');
						return false;
					}
					else if(tblISSVL.getValueAt(i,TBL_USGTP).toString().length() == 0)
					{
						tblISSVL.editCellAt(i,TBL_USGTP);
						setMSG("Usage Type can not be empty..",'E');
						return false;
					}
					for(int j=0;j<L_intROWID-1;j++)
					{
						if(tblISSVL.getValueAt(j,TBL_MATCD).toString().trim().length() >0)
							if((tblISSVL.getValueAt(j,TBL_MATCD).toString()+tblISSVL.getValueAt(j,TBL_CCTCD).toString()+tblISSVL.getValueAt(j,TBL_BATNO).toString()+tblISSVL.getValueAt(j,TBL_GRNNO).toString()).equals(L_strMATCD.trim()+L_strTAGNO.trim()+L_strBATNO.trim()+L_strGRNNO.trim()))
							{
								setMSG("Duplicate entry ..",'E');
								return false;
							}
					}
					if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
						{
							strTEMP = tblISSVL.getValueAt(i,TBL_ISSQT).toString();
							if(tblISSVL.getValueAt(i,TBL_REQQT).toString().length() == 0)
							{
								tblISSVL.editCellAt(i,TBL_REQQT);
								setMSG("Requested qty can not be empty..",'E');
								return false;
							}
							else if(tblISSVL.getValueAt(i,TBL_ISSQT).toString().length() == 0)
							{
								tblISSVL.editCellAt(i,TBL_ISSQT);
								setMSG("Authorised qty can not be empty..",'E');
								return false;
							}
							else
							{
								if(Float.parseFloat(strTEMP)> Float.parseFloat(tblISSVL.getValueAt(i,TBL_REQQT).toString()))
								{
									setMSG("Issued qty. can not be greater than the Requested quantity..",'E');
									return false;
								}
							}
							if(!strSTRTP.equals(strTKSTR_fn))
							if(Float.parseFloat(strTEMP)> Float.parseFloat(tblISSVL.getValueAt(i,TBL_AVLQT).toString()))
							{
								setMSG("Issued qty. can not be greater than the Stock quantity..",'E');
								return false;
							}
							L_strMATCD =tblISSVL.getValueAt(i,TBL_MATCD).toString();
							L_strISSQT = tblISSVL.getValueAt(i,TBL_ISSQT).toString();
						    if(hstISSQT.containsKey((String)L_strMATCD))
							{
							    L_dblISSQT = Double.parseDouble(L_strISSQT)+Double.parseDouble(hstISSQT.get(L_strMATCD).toString());
							    hstISSQT.remove(L_strMATCD);
							    hstISSQT.put(L_strMATCD,setNumberFormat(L_dblISSQT,3));
							}
							else
							    hstISSQT.put(L_strMATCD,L_strISSQT);
                      	}
					if((flgCYLFL)&& (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
					{
						for(int x=0;x<tblCYLDT.getRowCount();x++)
						{
							if(tblCYLDT.getValueAt(x,TBL_CHKFL).toString().equals("true"))
								L_intCYLCT++;
						}
						if(L_intCYLCT ==0)
						{
							setMSG("Please select the Cylinder details..",'E');
							return false;
						}
					}
				}
			}
			//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))&& (!M_strSBSCD.substring(2,4).equals(strTKSTR_fn))&&(!flgCYLFL))
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))&& (!M_strSBSCD.substring(2,4).equals(strTKSTR_fn)))
			{
			 	//System.out.println("2");
			 	for(int i=0;i<tblISSVL.getRowCount();i++)
    			{
    				if(tblISSVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
    				{
    				    if(Double.parseDouble(hstISSQT.get(tblISSVL.getValueAt(i,TBL_MATCD).toString()).toString()) > Double.parseDouble(hstSTKQT.get(tblISSVL.getValueAt(i,TBL_MATCD).toString()).toString()))
    			        {
    			            setMSG("Issue Quantity can not be greater than Stock Qty for "+tblISSVL.getValueAt(i,TBL_MATCD).toString(),'E');
    			            return false;
    			        }
    			 	}
    			}
    		}
		}
		catch(Exception e)
		{
			setMSG(e,"vldDATA ");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		return true;
	}
	
boolean genAUTIND()
{
	String L_strINDNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";
	try
	{
		strSTSFL = "2";
		cl_dat.M_flgLCUPD_pbst = true;
		M_strSQLQRY = "Select CMT_CHP01,CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
		M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(0,2)+"IND' and ";
		M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn +"'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET != null)
		{
			if(M_rstRSSET.next())
			{
				    L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
			}
			M_rstRSSET.close();
		}
		if(L_CHP01.trim().length()== 3)
		{
			setMSG("In use for generating auto indent..",'E');
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			return false;
		}
		else
		{
			M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CHP01 = '"+cl_dat.M_strUSRCD_pbst +"'";
			M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2)+"IND' ";
			M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(!cl_dat.exeDBCMT("genAUTIND"))
				setMSG("Could not lock the record..",'E');
			else
				setMSG(" ",'N');
		}
		L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL.trim()) + 1);
		int strTEMP = 5-L_CCSVL.length();
		for(int i=0;i<strTEMP;i++)
			L_CCSVL = "0"+L_CCSVL;
		L_strINDNO = L_CODCD + L_CCSVL;
		////cstATIND.setString(1,cl_dat.M_strCMPCD_pbst);
		////cstATIND.setString(2,M_strSBSCD.substring(2,4));
		////cstATIND.setString(3,L_strINDNO.trim());
		////cstATIND.setString(4,cl_dat.M_strUSRCD_pbst);
		////cstATIND.setString(5,"KVM");
		////cstATIND.setString(6,"302");
		////cstATIND.setString(7,txtISSNO.getText().trim());
		////cstATIND.executeUpdate();
		////cl_dat.exeDBCMT("after proceduue");
		M_strSQLQRY ="SELECT COUNT(*) L_CNT from mm_inmst where in_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_strtp ='"+M_strSBSCD.substring(2,4)+"'";
		M_strSQLQRY +=" AND IN_INDNO ='"+L_strINDNO+"'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
				if(M_rstRSSET.getInt("L_CNT") >0)
				{
					M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CHP01 =null,CMT_CCSVL = '"+L_CCSVL +"'";
					M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2) +"IND' ";
					M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.exeDBCMT("genAUTIND"))
					{
						String L_strEML ="";
						setMSG("Generated an Auto Indent no..."+L_strINDNO,'N');			
						cl_eml ocl_eml = new cl_eml();
						M_strSQLQRY = "Select CMT_CODds from CO_CDTRN ";
						M_strSQLQRY += " where CMT_CGMTP = 'EML' and CMT_CGSTP = 'MMXXATI'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							while(M_rstRSSET.next())
							{
								L_strEML = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
								ocl_eml.sendfile(L_strEML,null,"Intimation of Auto Indent ","Auto Indent No."+L_strINDNO+" has been generated");
							}
							M_rstRSSET.close();
						}
						return true;
					}
				}
				else
				{
					M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CHP01 =null";
					M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2)+"IND' ";
					M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.exeDBCMT("genAUTIND"))
						setMSG("Issue has been Authorised,Auto Indent is not Required.. ",'N');
				}
		// check if it is excuted, save the last number and send an email
		//call spltest.mm_isprc('01','40100015','SYS','KVM','302','44100003');
		return true;	
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"btnAUTH ");
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;	
	}
}
			
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input == txtISSDT)
				{
					if(M_fmtLCDAT.parse(txtISSDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{							
						setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
						return false;
					}
				}
				else if(input == txtDPTCD)
				{
					if(!hstDPTCD.containsKey(txtDPTCD.getText().trim()))
					{
						setMSG("Department code is invalid.. press F1 to select from list..",'E');
						return false;
					}
					else
						txtDPTDS.setText(hstDPTCD.get(txtDPTCD.getText().trim()).toString());
				}
				else if(input == txtPRTCD)
				{
					if(txtPRTCD.getText().length() != 5)
					{
						setMSG("Invalid Party Code..",'E');
						return false;
					}
					else
					{	
						M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='"+cmbPRTTP.getSelectedItem().toString().substring(0,1) +"'";
						M_strSQLQRY +=" AND PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET !=null)
							if(M_rstRSSET.next())
							{
								//txtPRTCD.transferFocus();
								txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
							}
							else
							{
								setMSG("Invalid Party Code,Press F1 to select from List..",'E');
								return false;
							}
					}
					
				}
				else if(input == txtEXCCT)
				{
					if(strSTRTP.equals(strTKSTR_fn))
					{
						if(txtEXCCT.getText().trim().length() != 2)
						{
							setMSG("Invalid Excise Category..",'E');
							return false;
						}
						else
						{
							if(vldEXCCT(txtEXCCT.getText().trim()))
							   return true;
							else
								return true;
						}
					}
					else
						return true;
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;	
		}
	}

	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID==TBL_MATCD)
				{
					strTEMP = txtMATCD.getText().trim();
					if(strTEMP.length() == 0)
						return true;
					if(!vldMATCD(strTEMP,P_intROWID))
					{
						setMSG("Invalid Item Code,Press F1 to select From the list..",'E');
						return false;
					}
				}
				else if(P_intCOLID==TBL_CCTCD)
				{
					strTEMP = txtCCTCD.getText().trim();
					if(strTEMP.length() == 0)
						return true;
					if(!vldCCTCD(strTEMP))
						return false;
				}
				else if(P_intCOLID==TBL_USGTP)
				{
					strTEMP = txtUSGTP.getText().trim();
					if(strTEMP.length() == 0)
						return true;
					if(!vldUSGTP(strTEMP))
					{
						setMSG("Invalid Usage Type,Press F1 to select from the list..",'E');
						return false;
					}
				}
				else if(P_intCOLID==TBL_REQQT)
				{
					strTEMP = txtREQQT.getText().trim();
					
					if(strTEMP.length() == 0)
						return true;
					if(tblISSVL.getValueAt(P_intROWID,TBL_ISSQT).toString().equals(""))
						tblISSVL.setValueAt("0",P_intROWID,TBL_ISSQT);
					if(getQTY("REQ",tblISSVL.getValueAt(P_intROWID,TBL_MATCD).toString(),"","")> Float.parseFloat(tblISSVL.getValueAt(P_intROWID,TBL_AVLQT).toString()))
					{
						setMSG("Requested qty. can not be greater than the Stock quantity..",'E');
						return false;
					}
					else
					{
						if(tblISSVL.getValueAt(P_intROWID,TBL_BATNO).toString().length() >0)
						{
							if(getQTY("REQ",tblISSVL.getValueAt(P_intROWID,TBL_MATCD).toString(),tblISSVL.getValueAt(P_intROWID,TBL_GRNNO).toString(),tblISSVL.getValueAt(P_intROWID,TBL_BATNO).toString())> Float.parseFloat(tblISSVL.getValueAt(P_intROWID,TBL_BALQT).toString()))
							{
								setMSG("Requested qty. can not be greater than the Balance quantity for the Batch..",'E');
								return false;
							}
						}
						else
							return true;
					}
				}
				else if(P_intCOLID==TBL_ISSQT)
				{
					strTEMP = txtISSQT.getText().trim();
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
						return true;
					if(strTEMP.length() == 0)
						return true;
					if(tblISSVL.getValueAt(P_intROWID,TBL_MATCD).toString().length() == 0)
						return true;
					if(tblISSVL.getValueAt(P_intROWID,TBL_ISSQT).toString().equals(""))
					{
						if(tblISSVL.getValueAt(P_intROWID,TBL_MATCD).toString().length() >0)
							tblISSVL.setValueAt("0",P_intROWID,TBL_ISSQT);
					}
					//if(Float.parseFloat(strTEMP)> Float.parseFloat(tblISSVL.getValueAt(P_intROWID,TBL_AVLQT).toString()))
					if(getQTY("ISS",tblISSVL.getValueAt(P_intROWID,TBL_MATCD).toString(),"","") > Float.parseFloat(tblISSVL.getValueAt(P_intROWID,TBL_AVLQT).toString()))
					{
						setMSG("Issued qty. can not be greater than the Stock quantity..",'E');
						return false;
					}
					else
					{
						if(Float.parseFloat(strTEMP)> Float.parseFloat(tblISSVL.getValueAt(P_intROWID,TBL_REQQT).toString()))
						{
							setMSG("Issued qty. can not be greater than the Requested quantity..",'E');
							return false;
						}
						if(!tblISSVL.getValueAt(P_intROWID,TBL_BATNO).toString().equals(strISSNO))
						if(getQTY("ISS",tblISSVL.getValueAt(P_intROWID,TBL_MATCD).toString(),tblISSVL.getValueAt(P_intROWID,TBL_GRNNO).toString(),tblISSVL.getValueAt(P_intROWID,TBL_BATNO).toString())> Float.parseFloat(tblISSVL.getValueAt(P_intROWID,TBL_BALQT).toString()))
						{
							setMSG("Issued qty. can not be greater than the Balance quantity for the Batch..",'E');
							return false;
						}
						return true;
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"table verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}
	// Method to be commected afterwards , temp for separate tankfarm series
	private String genTFISS(String P_strMATTP)
	{
		String L_ISSNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for issues;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+strSTRTP+"ISS' and ";
			//M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + P_strMATTP + "'";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strMATTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP01.trim().length() >0)
					{
						M_rstRSSET.close();
						return null;
					}
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+strSTRTP+"ISS'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strMATTP + "'";			
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT("genISSNO"))
			{
				L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
				for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
					L_ISSNO += "0";
				L_CCSVL = L_ISSNO + L_CCSVL;
				L_ISSNO = L_CODCD + L_CCSVL;
				txtISSNO.setText(L_ISSNO);
			}
			else 
				return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genISSNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_ISSNO;
	}
	// Method to update the last Issue No.in the CO_CDTRN
	private void exeTFISS(String P_strISSNO,String P_strMATTP)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + P_strISSNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+strSTRTP+"ISS'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strMATTP + "'";			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeISSNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	private float getQTY(String P_strQTYTP,String P_strMATCD,String P_strGRNNO,String P_strBATNO) 
	{
		float L_fltTOTQT =0;
		try
		{
			int L_intCOLID =0; 
			if(P_strQTYTP.equals("REQ"))
				L_intCOLID = TBL_REQQT;
			if(P_strQTYTP.equals("ISS"))
				L_intCOLID = TBL_ISSQT;
			if(P_strQTYTP.equals("BAL"))
				L_intCOLID = TBL_BALQT;
			if(P_strBATNO.length() >0 && P_strGRNNO.length() >0)
			{
				for(int i=0;i<tblISSVL.getRowCount();i++)
				{
					if((tblISSVL.getValueAt(i,TBL_CHKFL).toString().equals("true")) && ((tblISSVL.getValueAt(i,TBL_MATCD).toString()+tblISSVL.getValueAt(i,TBL_GRNNO).toString()+tblISSVL.getValueAt(i,TBL_BATNO).toString()).equals(P_strMATCD+P_strGRNNO+P_strBATNO)))
					if(tblISSVL.getValueAt(i,L_intCOLID).toString().length() >0)
						L_fltTOTQT += Float.parseFloat(tblISSVL.getValueAt(i,L_intCOLID).toString());
				}
			}
			else
			for(int i=0;i<tblISSVL.getRowCount();i++)
			{
				if((tblISSVL.getValueAt(i,TBL_CHKFL).toString().equals("true")) && (tblISSVL.getValueAt(i,TBL_MATCD).toString().equals(P_strMATCD)))
					if(tblISSVL.getValueAt(i,L_intCOLID).toString().length() >0)
						L_fltTOTQT += Float.parseFloat(tblISSVL.getValueAt(i,L_intCOLID).toString());
			}
			L_fltTOTQT = Float.parseFloat(setNumberFormat(L_fltTOTQT,3));
			System.out.println("Qty "+ P_strQTYTP + " "+L_fltTOTQT);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getQTY");		
		}
		return L_fltTOTQT;

	}

}
