
/*
System Name   : Material Management System.
Program Name  : List of Issue
Program Desc. : Report to generate list of issues between given data range.
Author        : Mr.S.R.Mehesare
Date          : 19 MAY 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;
/**<pre>
System Name  : Material Management System.
 
Program Name : List of Issue.

Purpose : Program to generate list of issues between given data range according to 
the selected Criteria.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #
MM_ISMST       IS_STRTP,IS_ISSTP,IS_ISSNO,              
               IS_MATCD,IS_TAGNO,IS_BATNO                              #                
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtDPTSL    IS_DPTCD       MM_ISMST      VARCHAR(3)     Deprament Code
cmbMATCD    IS_MATCD       MM_ISMST      VARCHAR(10)    Material Code
cmbEXCCT    IS_EXCCT       MM_ISMST      VARCHAR(2)     Excise Category
txtFMDAT    IS_ISSDT       MM_ISMST      DATE           Issue Date
txtTODAT    IS_ISSDT       MM_ISMST      DATE           Issue Date  
--------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
      For Material Code
          Data is taken from CO_CDTRN for CMT_CGMTP||CMT_CGSTP ='SYSMMXXMAT'.
      For Department Code
          Data is taken from CO_CDTRN for CMT_CGMTP||CMT_CGSTP ='SYSCOXXDPT'. 
      For Excise Category (Material Type)
          Data is taken from CO_CDTRN for CMT_CGMTP||CMT_CGSTP ='SYSMMXXMTP'. 
      
      Report Data is taken from tables MM_ISMST & CO_CTMST for given condition
       For all Departments :-
      	  1) IS_STRTP = Selected Stores Type
          2) AND IS_MATTP = Selected Material Type
          3) AND IS_MATCD = CT_MATCD 
          4) AND IS_AUTDT Between given date range						
         If Perticular excise category is selected
          5) AND IS_EXCCT = Selected Excise Category.
         If Obsolete is checked
          6) AND IFNULL(CT_STSFL,'') ='9'
         If Specific Department is Selected
          7) AND IS_DPTCD = given Department Code.
         If contractor code is Selected
          8) AND IS_PRTCD = given Contractor Code.
          9) AND IS_ISSTP = given Issue Type
      For Issue wise Report Order by IS_DPTCD,IS_ISSNO.
      For Material Wise Report Order by IS_DPTCD,IS_MATCD.      		            					

<I>
<B>Validations :</B>
    - To date must be greater then From data & Smaller than Current date.
    - Department Code must be valid.
</I> */

class mm_rpiss extends cl_rbase 
{
    											/** JComboBox to Select & Display Material Type.*/
	private JComboBox cmbMATTP;				/** JComboBox to Select & Display Issue Type.*/
	private JComboBox cmbISSTP;				/** JComboBox to Select & Display Excise Categories.*/
	private JComboBox cmbEXCCT;				/** JTextField to enter & display From Date to spacify Date range.*/
	private JTextField txtFRDAT;			/** JTextField to enter & display  To Date to spacify Date range.*/
	private JTextField txtTODAT;			/** JRedioButton to Select Report type as Issue wise Report.*/
	private JRadioButton rdbISSNO;			/** JRedioButton to Select Report type as Material wise Report.*/
	private JRadioButton rdbMATCD;			/** JRedioButton to specify all departments for the reports.*/
	private JRadioButton rdbDPTAL;			/** JRedioButton to specify perticular Department for the Report.*/	
	private JRadioButton rdbDPTSP;			/** JTextField to enter & display Department code.*/
	private JTextField txtDPTSL;			/** JTextField to enter & display Department Description.*/		
	private JTextField txtDPTDS;			/** JCheckBox to to include Obsolete items in the Report.*/
	private JCheckBox chbOBSOL;							/**Button Group for Report Type Selection as Issue Wise Or Material Wise.*/
	private ButtonGroup btgISMAT = new ButtonGroup();	/**Button Group for Department Selection.*/
	private ButtonGroup btgDPTSL = new ButtonGroup();	
								/**	String variable for Store Type.*/
	private String strSTRTP;	/**	String variable for Store Type Description.*/
	private String strSTRDS;	/**	String variable for Issue Date.*/
	
	private String strISSDT;	/**	String variable for Issue Number.*/
	private String strISSNO;	/**	String variable for Material Code*/		
	private String strMATCD;	/**	String variable for Material Description.*/
	private String strMATDS;	/**	String variable for Unit of Measurement.*/	
	private String strUOMCD;	/**	String variable for Required Quantity.*/
	private String strREQQT;	/**	String variable for Issue Quantity.*/
	private String strISSQT;	/**	String variable for Repartment Code.*/
	private String strDPTCD;	/**	String variable for Issue Type.*/
	private String strISSTP;	/**	String variable for Excise Category.*/
	private String strEXCCT;	/**	String variable for Temporary Department Code to monitor department change.*/
	private String strDPTMP="";	/**	String variable for Temporary Material Code to monitor material change.*/
	private String strMTTMP="";	/**	String variable for Department Name.*/
	private String strDPTNM="";	/**	String variable for generated Report file Name.*/
	private String strFILNM;							
								/** String variable for ISO Specification.*/
	private String strISOS1;	/** String variable for ISO Specification.*/
	private String strISOS2;	/** String variable for ISO Specification.*/
	private String strISOS3;		
								/** Float variable for Required Quantity.*/
	private float fltREQQT;		/** Float variable for Issue Quantity.*/
	private float fltISSQT;		/** Float variable for Total Transfer Quantity.*/
	private float fltTRQQT;		/** Float variable for Total Issue Quantity.*/
	private float fltTISQT;		/** Integer variable to count number of records fetched.*/
	private int intRECCT = 0;	/** Hashtable to Store Department code & associated description.*/
	private Hashtable hstDPTCD; /** Hashtable to Store Material Type & associated description.*/
	private Hashtable hstMATTP; /** Hashtable to Store Excise Ctegory Code & associated description.*/
	private Hashtable hstEXCCT;			/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    private JTextField txtPRTCD,txtPRTNM;
    private int intLINNO = 0;
	private int intPAGNO = 1;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mm_rpiss.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;
	
	mm_rpiss()
	{
		super(2);
		try
		{
			hstDPTCD = new Hashtable();
			hstMATTP = new Hashtable();
			hstEXCCT = new Hashtable();
			cmbISSTP = new JComboBox();
			cmbMATTP = new JComboBox();
			cmbEXCCT = new JComboBox();
			cmbMATTP.addItem("Select");
			cmbEXCCT.addItem("ALL");
			txtDPTSL = new TxtNumLimit(3.0);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS,CMT_CGSTP FROM CO_CDTRN "
				+"WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP IN('MMXXMTP','COXXDPT','MMXXMAT','MMXXISS')"
				+" and ifnull(cmt_stsfl,' ') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXMTP"))
					{
						cmbMATTP.addItem(M_rstRSSET.getString("CMT_CODDS"));
						hstMATTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),M_rstRSSET.getString("CMT_CODCD"));
					}
					else if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXISS"))
					{
						cmbISSTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
					}
					else if(M_rstRSSET.getString("CMT_CGSTP").equals("COXXDPT"))
						hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					else if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXMAT"))
					{
						cmbEXCCT.addItem(M_rstRSSET.getString("CMT_CODDS"));
						hstEXCCT.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),M_rstRSSET.getString("CMT_CODCD"));
					}
				}
				M_rstRSSET.close();
			}
			txtFRDAT = new TxtDate();
			txtTODAT = new TxtDate();
			
			rdbISSNO = new JRadioButton("Issue Wise",true);
			rdbMATCD = new JRadioButton("Material Wise",false);
			btgISMAT.add(rdbISSNO);
			btgISMAT.add(rdbMATCD);
			
			rdbDPTAL = new JRadioButton("ALL",true);
			rdbDPTSP = new JRadioButton("Specific",false);
			btgDPTSL.add(rdbDPTAL);		
			btgDPTSL.add(rdbDPTSP);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			setMatrix(20,7);
			add(new JLabel("Report Type    :"),4,3,1,1,this,'L');
			add(rdbISSNO,4,4,1,1,this,'L');
			add(rdbMATCD,4,5,1,1.5,this,'L');
						   
			add(new JLabel("Department    :"),5,3,1,1,this,'L');
			add(rdbDPTAL,5,4,1,1,this,'L');
			add(rdbDPTSP,5,5,1,1,this,'L');
			
			add(txtDPTSL,6,4,1,1,this,'L');
			add(txtDPTDS=new JTextField(),6,5,1,2,this,'L');
			add(new JLabel("Issue Type      :"),7,3,1,1,this,'L');
			add(cmbISSTP,7,4,1,1.3,this,'L');
			add(new JLabel("Material Type      :"),8,3,1,1,this,'L');
			add(cmbMATTP,8,4,1,1.3,this,'L');
			add(new JLabel("Excise Category      :"),9,3,1,1,this,'L');
			add(cmbEXCCT,9,4,1,1.3,this,'L');
						
			add(new JLabel("Date Range      :"),10,3,1,1,this,'L');
			add(txtFRDAT,10,4,1,1,this,'L');
			add(txtTODAT,10,5,1,1,this,'L');			
			add(new JLabel("Contractor         :"),11,3,1,1,this,'L');
			add(txtPRTCD= new TxtLimit(5),11,4,1,1,this,'L');
			add(txtPRTNM = new TxtLimit(40),11,5,1,2,this,'L');
			add(chbOBSOL = new JCheckBox("Obsolete"),12,4,1,1,this,'L');
			txtFRDAT.setText(cl_dat.M_strLOGDT_pbst.toString());
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst.toString());
			
			setENBL(false);
			INPVF oINPVF=new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(oINPVF);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
					{
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
					}
				}
				else
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}
			M_pnlRPFMT.setVisible(true);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
				M_cmbDESTN.requestFocus();
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
				setENBL(true);
				rdbISSNO.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst)){
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst)){
				setENBL(true);
			}
		}
		if(M_objSOURC == rdbDPTSP)
		{
			txtDPTSL.setEnabled(true);
			txtDPTSL.requestFocus();
		}
		if(M_objSOURC == rdbDPTAL)
		{
			txtDPTSL.setText("");
			txtDPTDS.setText("");
			txtDPTSL.setEnabled(false);
		}
		setMSG("",'N');
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			
			if(M_objSOURC == rdbISSNO)
			{
				rdbMATCD.requestFocus();
			}
			if(M_objSOURC == rdbMATCD)
			{
				rdbDPTAL.requestFocus();
			}
			if(M_objSOURC == rdbDPTAL)
			{
				rdbDPTSP.requestFocus();
			}
			if(M_objSOURC == rdbDPTSP)
			{
				cmbMATTP.requestFocus();
			}
			if(M_objSOURC == cmbMATTP)
			{
				cmbEXCCT.requestFocus();
			}
			if(M_objSOURC == cmbEXCCT)
			{
				txtFRDAT.requestFocus();
			}
			if(M_objSOURC == txtFRDAT)
			{
				if(txtTODAT.getText().length()==0 )
					txtTODAT.setText(txtFRDAT.getText());
				txtTODAT.requestFocus();
			}
			if(M_objSOURC == txtDPTSL)
			{
				cmbMATTP.requestFocus();
			}
			if(M_objSOURC == txtTODAT)
			{
				txtPRTCD.requestFocus();
			}
			if(M_objSOURC == txtPRTCD)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		else if(L_KE.getKeyCode()==L_KE.VK_F1 && M_objSOURC == txtDPTSL)
		{
			String L_strADDQR="";
			M_strHLPFLD = "txtDPTSL";
			if(txtDPTSL.getText().length()>0)
				L_strADDQR=" and CMT_CODCD like '"+txtDPTSL.getText()+"%' ";
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN "
					 +"WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' and ifnull(cmt_stsfl,' ') <>'X'"+L_strADDQR+" ORDER BY CMT_CODDS";
			cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code","Name"},2,"CT",new int[]{110,400});		
		}
		else if(L_KE.getKeyCode()==L_KE.VK_F1 && M_objSOURC == txtPRTCD)
		{
			txtPRTCD.setText(txtPRTCD.getText().toUpperCase());
		    M_strHLPFLD="txtPRTCD";
			M_strSQLQRY = " SELECT DISTINCT IS_PRTCD,PT_PRTNM FROM MM_ISMST,CO_PTMST WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IFNULL(IS_PRTCD,'') <>'' AND IS_PRTTP = PT_PRTTP AND"
                        + " IS_PRTCD = PT_PRTCD AND IS_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
            if(txtPRTCD.getText().length()>0)
				M_strSQLQRY+=" where PT_PRTCD like '"+txtPRTCD.getText()+"%' ";
            cl_hlp(M_strSQLQRY,1,1,new String[] {"Party Code","Description"},2 ,"CT");
		}
	}
	
	void exeHLPOK()
	{
		cl_dat.M_flgHELPFL_pbst = false;
		super.exeHLPOK();
		cl_dat.M_wndHLP_pbst=null;
		StringTokenizer L_stkTEMP=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD.equals("txtDPTSL"))
		{
			txtDPTSL.setText(L_stkTEMP.nextToken());
			txtDPTDS.setText(L_stkTEMP.nextToken());
			cmbMATTP.requestFocus();
		}
		else if(M_strHLPFLD == "txtPRTCD")
		{
		    txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			L_stkTEMP=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_stkTEMP.nextToken();
			txtPRTNM.setText(L_stkTEMP.nextToken());
		}
	}

	void genRPTFL()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			F_OUT = new FileOutputStream(strRPFNM);
			D_OUT = new DataOutputStream(F_OUT);
			
			strISOS1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPISS01");
			strISOS2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPISS02");
			strISOS3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPISS03");
			
			strSTRTP = M_strSBSCD.substring(2,4);
			if(cmbMATTP.getSelectedIndex() >0)
				strISSTP = hstMATTP.get(cmbMATTP.getSelectedItem().toString()).toString();
			if(cmbEXCCT.getSelectedIndex() >0)
				strEXCCT = hstEXCCT.get(cmbEXCCT.getSelectedItem().toString()).toString();
			intLINNO = 0;	
			intPAGNO = 1;
			
			fltTRQQT = 0;
			fltTISQT = 0;
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("<HTML><HEAD><Title>List of Issues </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
			genRPHDR();
			if(rdbDPTAL.isSelected())
			{
				if(rdbISSNO.isSelected())
				{
					M_strSQLQRY = "SELECT IS_ISSDT,IS_ISSNO,IS_MATCD,IS_REQQT,IS_ISSQT,IS_DPTCD, "
						+"CT_MATDS,CT_UOMCD "
						+"FROM MM_ISMST,CO_CTMST "
						+"WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+strSTRTP+"' AND IS_MATTP = '"+strISSTP+"' AND "
						+"IS_ISSTP = '"+cmbISSTP.getSelectedItem().toString().substring(0,2)+"' AND "
						+"IS_MATCD = CT_MATCD AND ifnull(is_stsfl,' ') <>'X' and "
						+"date(IS_AUTDT) BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
					if(cmbEXCCT.getSelectedIndex() >0)
						M_strSQLQRY +=" AND IS_EXCCT ='"+strEXCCT +"'"; 
					if(txtPRTCD.getText().length() >0)
						M_strSQLQRY +=" AND IS_PRTCD ='"+txtPRTCD.getText().trim() +"'"; 
					if(chbOBSOL.isSelected())
						M_strSQLQRY +=" AND IFNULL(CT_STSFL,'') ='9' "; 
					M_strSQLQRY +="ORDER BY IS_DPTCD,IS_ISSNO";
				}
				if(rdbMATCD.isSelected())
				{
					M_strSQLQRY = "SELECT IS_ISSDT,IS_ISSNO,IS_MATCD,IS_REQQT,IS_ISSQT,IS_DPTCD, "
						+"CT_MATDS,CT_UOMCD FROM MM_ISMST,CO_CTMST "
						+"WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+strSTRTP+"' AND IS_MATTP = '"+strISSTP+"' AND "
						+"IS_ISSTP = '"+cmbISSTP.getSelectedItem().toString().substring(0,2)+"' AND "
						+"IS_MATCD = CT_MATCD AND ifnull(IS_STSFL,' ') <>'X' and "
						+"date(IS_AUTDT) BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
						if(cmbEXCCT.getSelectedIndex() >0)
							M_strSQLQRY +=" AND IS_EXCCT ='"+strEXCCT +"'"; 
						if(txtPRTCD.getText().length() >0)
						    M_strSQLQRY +=" AND IS_PRTCD ='"+txtPRTCD.getText().trim() +"'"; 
						if(chbOBSOL.isSelected())
							M_strSQLQRY +=" AND IFNULL(CT_STSFL,'') ='9' "; 
						M_strSQLQRY += "ORDER BY IS_DPTCD,IS_MATCD";
				}
			}
			if(rdbDPTSP.isSelected())
			{
				
				strDPTCD = txtDPTSL.getText();
							
				if(rdbISSNO.isSelected())
				{
					M_strSQLQRY = "SELECT IS_ISSDT,IS_ISSNO,IS_MATCD,IS_REQQT,IS_ISSQT,IS_DPTCD, "
						+"CT_MATDS,CT_UOMCD "
						+"FROM MM_ISMST,CO_CTMST "
						+"WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+strSTRTP+"' AND IS_MATTP = '"+strISSTP+"' AND "
						+"IS_ISSTP = '"+cmbISSTP.getSelectedItem().toString().substring(0,2)+"' AND "
						+"IS_MATCD = CT_MATCD AND "
						+"IS_DPTCD = '"+strDPTCD+"' AND "
						+"ifnull(IS_STSFL,' ') <>'X' and date(IS_AUTDT) BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
					if(cmbEXCCT.getSelectedIndex() >0)
						M_strSQLQRY +=" AND IS_EXCCT ='"+strEXCCT +"'"; 
					if(txtPRTCD.getText().length() >0)
					    M_strSQLQRY +=" AND IS_PRTCD ='"+txtPRTCD.getText().trim() +"'"; 
					if(chbOBSOL.isSelected())
							M_strSQLQRY +=" AND IFNULL(CT_STSFL,'') ='9' "; 
					M_strSQLQRY +="ORDER BY IS_DPTCD,IS_ISSNO";
				}
				if(rdbMATCD.isSelected())
				{
					M_strSQLQRY = "SELECT IS_ISSDT,IS_ISSNO,IS_MATCD,IS_REQQT,IS_ISSQT,IS_DPTCD, "
						+"CT_MATDS,CT_UOMCD "
						+"FROM MM_ISMST,CO_CTMST "
						+"WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+strSTRTP+"' AND IS_MATTP = '"+strISSTP+"' AND "
						+"IS_ISSTP = '"+cmbISSTP.getSelectedItem().toString().substring(0,2)+"' AND "
						+"IS_MATCD = CT_MATCD AND "
						+"IS_DPTCD = '"+strDPTCD+"' AND "
						+"ifnull(IS_STSFL,' ') <>'X' and date(IS_AUTDT) BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
						if(cmbEXCCT.getSelectedIndex() >0)
							M_strSQLQRY +=" AND IS_EXCCT ='"+strEXCCT +"'"; 
					    if(txtPRTCD.getText().length() >0)
				    		M_strSQLQRY +=" AND IS_PRTCD ='"+txtPRTCD.getText().trim() +"'"; 
					    if(chbOBSOL.isSelected())
							M_strSQLQRY +=" AND IFNULL(CT_STSFL,'') ='9' "; 
						M_strSQLQRY +="ORDER BY IS_DPTCD,IS_MATCD";
				}
			}
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			intRECCT =0;
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT++;
					strISSDT = M_fmtLCDAT.format(M_rstRSSET.getDate("IS_ISSDT")).toString();
					strISSNO = M_rstRSSET.getString("IS_ISSNO");
					strMATCD = M_rstRSSET.getString("IS_MATCD");
					fltREQQT = Float.parseFloat(M_rstRSSET.getString("IS_REQQT"));
					fltISSQT = Float.parseFloat(M_rstRSSET.getString("IS_ISSQT"));
					strDPTCD = M_rstRSSET.getString("IS_DPTCD");
					strMATDS = M_rstRSSET.getString("CT_MATDS");
					strUOMCD = M_rstRSSET.getString("CT_UOMCD");
					
					/*if(rdbDPTAL.isSelected()){
						strDPTNM = M_rstRSSET.getString("CMT_CODDS");	
					}
					else */
						strDPTNM = hstDPTCD.get(strDPTCD).toString();
					
					if(intLINNO >60)
					{		
						genRPFTR();
						intPAGNO += 1;
						genRPHDR();			
					}
					if(rdbISSNO.isSelected())
					{
						if(!strDPTCD.equals(strDPTMP))
						{
							crtNWLIN();
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
								prnFMTCHR(D_OUT,M_strBOLD);
							if(M_rdbHTML.isSelected())
				                    D_OUT.writeBytes("<b>");	
							D_OUT.writeBytes("DEPARTMENT : "+strDPTCD+" "+strDPTNM);
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
								prnFMTCHR(D_OUT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
				                D_OUT.writeBytes("</b>");
							crtNWLIN();
							strDPTMP = strDPTCD;
//							intLINNO += 2;
						}
						fltTRQQT += fltREQQT;
						fltTISQT += fltISSQT;
						
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('R',strISSDT,13));
						D_OUT.writeBytes(padSTRING('R',strISSNO,12));
						D_OUT.writeBytes(padSTRING('R',strMATCD,15));
						D_OUT.writeBytes(padSTRING('R',strMATDS,27));
						D_OUT.writeBytes(padSTRING('L',strUOMCD,3));
						D_OUT.writeBytes(padSTRING('L',String.valueOf(fltREQQT),13));
						D_OUT.writeBytes(padSTRING('L',String.valueOf(fltISSQT),13));
					}
					if(rdbMATCD.isSelected())
					{
						if(!strDPTCD.equals(strDPTMP))
						{
							if(strMTTMP.length()>0){
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('L',"TOTAL     :",69));
								D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltTRQQT,3),12));
								D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltTISQT,3),13));
								fltTRQQT = 0;
								fltTISQT = 0;
							}
							crtNWLIN();
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
								prnFMTCHR(D_OUT,M_strBOLD);
							if(M_rdbHTML.isSelected())
				               D_OUT.writeBytes("<b>");
							crtNWLIN();
							D_OUT.writeBytes("DEPARTMENT : "+strDPTCD+" "+strDPTNM);
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
								prnFMTCHR(D_OUT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
				                D_OUT.writeBytes("</b>");
							crtNWLIN();
							D_OUT.writeBytes(" "+strMATCD+" "+strMATDS+ "("+M_rstRSSET.getString("CT_UOMCD")+")");
							strMTTMP = strMATCD;
							strDPTMP = strDPTCD;
						}
						else
						{
							if(!strMATCD.equals(strMTTMP))
							{
							if(strMTTMP.length()>0)
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('L',"TOTAL     :",69));
								D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltTRQQT,3),12));
								D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltTISQT,3),13));
								 
								fltTRQQT = 0;
								fltTISQT = 0;
							}
							crtNWLIN();
							D_OUT.writeBytes(" "+strMATCD+" "+strMATDS+ "("+M_rstRSSET.getString("CT_UOMCD")+")");
							strMTTMP = strMATCD;
						}
						}
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('L',"",34));
						 
											
						fltTRQQT += fltREQQT;
						fltTISQT += fltISSQT;
						
						D_OUT.writeBytes(padSTRING('L',strISSNO,14));
						D_OUT.writeBytes(padSTRING('L',"",5));
						D_OUT.writeBytes(padSTRING('R',strISSDT,15));
						D_OUT.writeBytes(padSTRING('L',String.valueOf(fltREQQT),13));
						D_OUT.writeBytes(padSTRING('L',String.valueOf(fltISSQT),13));
					}
				}
			}
			if(rdbMATCD.isSelected())
			{
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('L',"TOTAL     :",70));
				D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltTRQQT,3),12));
				D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltTISQT,3),13));
				 
			}
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"strRPFIL");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	private void crtNWLIN() 
	{
		try
		{
			
			D_OUT.writeBytes("\n");
			intLINNO++;
			if(intLINNO >60)
			{		
				genRPFTR();
				intPAGNO += 1;
				genRPHDR();			
			}
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}
	void genRPHDR()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strCPI12);
				
			crtNWLIN();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<b>");
			D_OUT.writeBytes(padSTRING('L',"-------------------------------",96));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L',"Document Ref : "+strISOS1,96));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L',"   Issue No. : "+strISOS1,96));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L',"     Rev No. : "+strISOS1,96));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L',"-------------------------------",96));
			crtNWLIN();
			crtNWLIN();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strENH);
			D_OUT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LIMITED",96));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOENH);
			crtNWLIN();
			if(chbOBSOL.isSelected())
				D_OUT.writeBytes(padSTRING('R',"List of Obsolete Issues from  "+txtFRDAT.getText().trim()+" To "+txtTODAT.getText().trim(),75));
			else
			D_OUT.writeBytes(padSTRING('R',"List of Issues from  "+txtFRDAT.getText().trim()+" To "+txtTODAT.getText().trim(),75));
			D_OUT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString()+" / "+cmbMATTP.getSelectedItem().toString(),75));
			D_OUT.writeBytes(padSTRING('R',"Page No : "+intPAGNO,20));
            crtNWLIN();		
	      	D_OUT.writeBytes(padSTRING('R',"Issue Type  :"+cmbISSTP.getSelectedItem().toString().substring(2),75));
            crtNWLIN();				
            D_OUT.writeBytes("Contractor  : "+txtPRTNM.getText().trim());
          	if(cmbEXCCT.getSelectedIndex() >0)
			{
				crtNWLIN();
				D_OUT.writeBytes("Excise Category "+cmbEXCCT.getSelectedItem().toString());
				crtNWLIN();
			}
			if(rdbISSNO.isSelected())
			{
				crtNWLIN();
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes("Issue Date   Issue No.   Material Code. Mat.Description             UOM     Req.Qty.     Iss.Qty.");
				crtNWLIN();
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------");
				crtNWLIN();
			}
			if(rdbMATCD.isSelected())
			{
				crtNWLIN();
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes(" Mat. Code  Description(UOM)             Issue No.     Issue Date           Req.Qty       Iss.Qty");
				crtNWLIN();
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------");
				crtNWLIN();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");
							
		}
		catch(IOException ioe){
			setMSG(ioe,"Header");
		}
		
	}
	void genRPFTR()
	{
		try
		{
			intLINNO = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------");
			crtNWLIN();
			strDPTMP = "";
			strMTTMP = "";
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);
		}
		catch(IOException L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}
	boolean vldDATA()
	{
		try
		{
			if(txtFRDAT.getText().trim().length() ==0)
			{
				setMSG("Enter From Date ..",'E');
				return false;
			}
			else if(txtFRDAT.getText().trim().length() ==0)
			{
				setMSG("Enter To Date ..",'E');
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
			if(rdbDPTAL.isSelected())
			{
				if(cmbMATTP.getSelectedIndex() > 0)
				{
					if(0 >= M_fmtLCDAT.parse(txtFRDAT.getText()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText())))
						return true;
					else
					{
						setMSG("To Date can not be smaller than From Date",'E');
						txtTODAT.requestFocus();
					}
				}
				else
				{
					setMSG("Select Material Type",'E');
					cmbMATTP.requestFocus();
				}
			}
			else
			{
				if(rdbDPTSP.isSelected())
					if(txtDPTSL.getText().length()> 0)
						if(cmbMATTP.getSelectedIndex() > 0)
							if(0 >= M_fmtLCDAT.parse(txtFRDAT.getText()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText())))
								return true;
							else{
								txtTODAT.requestFocus();
								setMSG("To Date can not be smaller than From Date",'E');
							}
						else{
							cmbMATTP.requestFocus();
							setMSG("Select Material Type",'E');
						}
					else{
						setMSG("Enter Department",'E');
						txtDPTSL.requestFocus();
					}
				return false;
			}
			return false;
		}
		catch(Exception L_VLD)
		{
			return false;
		}
	}
	void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				if(M_rdbHTML.isSelected())
				     strRPFNM = strRPLOC + "mm_rpiss.html";
				if(M_rdbTEXT.isSelected())
				     strRPFNM = strRPLOC + "mm_rpiss.doc";
				genRPTFL();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT >0)
					    if (M_rdbTEXT.isSelected())
					        doPRINT(strRPFNM);
					    else 
			            {    
							Runtime r = Runtime.getRuntime();
							Process p = null;					
							p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
							setMSG("For Printing Select File Menu, then Print  ..",'N');
					    }    
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				    Runtime r = Runtime.getRuntime();
					Process p = null;
					if(intRECCT >0)
					{	
					    if(M_rdbHTML.isSelected())
					        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					    else
					        p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				    }
					else
					    setMSG("No data found..",'E');
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
			    	cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"List Of Issues"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
			    }
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		if(rdbDPTAL.isSelected())
			txtDPTSL.setEnabled(false);
		txtDPTDS.setEnabled(false);
		txtPRTNM.setEnabled(false);
	}
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
					
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length()==0)
						return true;
				if(input == txtDPTSL)
				{
					if(!hstDPTCD.containsKey((String)txtDPTSL.getText()))
					{
						setMSG("Invalid Department Code ..",'E');
						return false;
					}
						
				}
			  if(input == txtPRTCD)
			  {
			    M_strSQLQRY = "SELECT PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP ='S' AND "
			                + "PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
			    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			    if(M_rstRSSET !=null)
			        if(M_rstRSSET.next())
			        {
			            txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
			        }
			        else
			        {
			            setMSG("Invalid Party Name ..",'E');
			            txtPRTNM.setText("");
			            return false;
			        }
			  }
			  return true;
			}
			catch (Exception e)
			{
				setMSG(e,"Chlid.INPVF");
				return false;
			}
		}
	}
}



