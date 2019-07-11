/*
System Name   : Marketing System
Program Name  : Lot Detail Analysis
Program Desc. : Gives customer,grade & lot no. details of despatch transaction.
Author        : Mr.S.R.Mehesare
Date          : 02/09/2005
Version       : MKT v2.0.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/
import java.sql.ResultSet;import java.sql.Timestamp;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
import javax.swing.JComponent;import javax.swing.InputVerifier;import javax.swing.JButton;
import javax.swing.JRadioButton;import javax.swing.JTable;import javax.swing.ButtonGroup;
import javax.swing.table.*;import javax.swing.JCheckBox;import java.util.StringTokenizer;
import javax.swing.JComponent;import javax.swing.InputVerifier;import java.awt.Color;

public class mr_qrlot extends cl_rbase
{
	private JTextField txtPRDTP;
	private JTextField txtFMDAT;
	private JTextField txtTODAT;
	private JTextField txtTOTQT;
	private JTextField txtTOTPK;
	private JTextField txtSPBUY;
	private JTextField txtSPLOT;	
	private JTextField txtSPECD;
	private JTextField txtSPEDS;
	private JButton btnFETCH;
	
	private JLabel lbl1,lbl2;	
	private JComboBox cmbPRDTP;
	private JComboBox cmbSPECI;			
	private ButtonGroup bgrSPECI;
	
	private final int intROWCT_fn = 1000;		
	private JRadioButton rdbSPECT;
	private JRadioButton rdbALLCT;
	private cl_JTable tblITMDL;
	
	TableColumn TCL_INVQT,TCL_INVPK;
	
	private FileOutputStream fosREPORT;
	private DataOutputStream dosREPORT;
	
	private String strPRDTP = "01";
	private String strSPECI="Customer";
	private String strFMDAT;
	private String strTODAT;
	private String strFILNM;
	private String strDOTLN = "------------------------------------------------------------------------------------------------------------------------------------------------";
	private String strHEAD1="";
	private String strHEAD2="";
	private double dblTOTQT;
	private int intTOTPK;
	
	final private int TB_CHKFL = 0;
	final private int TB_INVNO = 1;
    final private int TB_INVDT = 2;
	final private int TB_BYRCD = 3;
    final private int TB_LOTNO = 4;	
	final private int TB_PRDDS = 5;	
	final private int TB_ZONCD = 6;		//zone Description	
	final private int TB_CNSCD = 7; 	// consignee
	final private int TB_INVQT = 8;
    final private int TB_INVPK = 9;
    final private int TB_LADNO = 10;
		
	int LM_STRLEN,LM_VALSTR;//to calculate the length of a string in getSTRTKN()
	int LM_CNT;
	int LM_INVPK;
	int LM_LMRGN = 0;
	
	float LM_INVQT = 0;
	
	boolean LM_TBLFL = false;
		
	String LM_ACTTXT,LM_DATFR,LM_DATTO,LM_INVNO,LM_INVDT,LM_COLM3,LM_COLM4;
	String LM_LADNO,LM_SPCTG,M_strHLPFLD,LM_CODCD,LM_CODDS,LM_SPSCG,L_strSELC3;
	String L_strSELC4,LM_CL3DS,LM_PRDDS,LM_QRSTR,LM_DSPSTR,LM_QRYON;
	String LM_SPCF1,LM_SPCF2;
		
	private Hashtable hstZONCD;
	private Hashtable hstPRTDS;
	private INPVF objINPVR = new INPVF();
	boolean flgFIRST= true;
	boolean flgFIRST1= true;
	mr_qrlot()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Product Type"),3,1,1,.9,this,'R');
			add(cmbPRDTP = new JComboBox(),3,2,1,1.2,this,'L');
			add(new JLabel("From Date"),3,3,1,.7,this,'R');
			add(txtFMDAT = new TxtDate(),3,4,1,1,this,'L');
			add(new JLabel("To Date"),3,5,1,.6,this,'R');
			add(txtTODAT = new TxtDate(),3,6,1,1.2,this,'L');
						
			add(new JLabel("Specification"),4,1,1,.9,this,'R');
			add(cmbSPECI = new JComboBox(),4,2,1,1.2,this,'L');						
			add(new JLabel("Buyer"),4,3,1,.7,this,'R');
			add(txtSPBUY = new TxtLimit(5),4,4,1,1,this,'L');
			add(new JLabel("Lot No."),4,5,1,.6,this,'R');
			add(txtSPLOT = new TxtNumLimit(10),4,6,1,1.2,this,'L');			
			add(new JLabel("Quantity"),4,7,1,.7,this,'R');
			add( txtTOTQT = new JTextField(),4,8,1,1,this,'L');			
			
			rdbSPECT = new JRadioButton("Specific",true);
			rdbALLCT = new JRadioButton("All");
			bgrSPECI = new ButtonGroup();
			bgrSPECI.add(rdbSPECT);
			bgrSPECI.add(rdbALLCT);
			add(rdbALLCT,5,1,1,.9,this,'R');
			add(rdbSPECT,5,2,1,1,this,'L');			
			add(lbl1 = new JLabel("Customer"),5,3,1,.7,this,'R');
			add(txtSPECD = new TxtLimit(10),5,4,1,1,this,'R');
			add(txtSPEDS = new JTextField(),5,5,1,2.2,this,'L');			
			add(new JLabel("Packages"),5,7,1,.7,this,'R');
			add( txtTOTPK = new JTextField(),5,8,1,1,this,'L');			
			
			add(lbl2 = new JLabel("msh"),6,1,1,6,this,'L');
			lbl2.setForeground(Color.blue);			
			add(btnFETCH = new JButton("Run"),6,8,1,1,this,'L');
				
			cmbSPECI.addItem("Customerwise");
			cmbSPECI.addItem("Gradewise");
			cmbSPECI.addItem("Lotwise");
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{					
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}					
			hstZONCD = new Hashtable();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP||CMT_CGSTP = 'SYSMR00ZON'";				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{					
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						hstZONCD.put(L_strQPRCD, nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}		
			
			String[] L_arrCOLHD = {"","Inv. No.","Inv. Date","Customer","Lot No.","Grade","Zone","Consignee Name","Inv. Qty.","Inv. Pkg.","LA. No."}; 
			int L_arrCOLSZ[] = {10,75,60,130,75,75,50,80,60,60,70};				
			tblITMDL = crtTBLPNL1(this,L_arrCOLHD,intROWCT_fn,7,1,10.9,8,L_arrCOLSZ,new int[]{0});			
			
			txtFMDAT.setInputVerifier(objINPVR);
			txtTODAT.setInputVerifier(objINPVR);
			txtSPBUY.setInputVerifier(objINPVR);
			txtSPLOT.setInputVerifier(objINPVR);
			txtSPECD.setInputVerifier(objINPVR);
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	
/**
	 * Super class method override to inhance its funcationality, to enable & disable 
	 * Components according to Requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtSPEDS.setEnabled(false);
		txtTOTQT.setEnabled(false);
		txtTOTPK.setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				flgFIRST = false;
				flgFIRST1 = false;
				setMSG("Selcet Product Type from the List..",'N');				
				setENBL(true);
				txtSPBUY.setEnabled(false);
				cmbPRDTP.requestFocus();
				if(txtFMDAT.getText().trim().length() == 0)
				{					
					try
					{					
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);						
						txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
					}
					catch(Exception L_EX)
					{	       
						setMSG(L_EX, "calDATE");						
					}		
				}
				if(txtTODAT.getText().trim().length() == 0)				
					txtTODAT.setText(cl_dat.M_strLOGDT_pbst);									
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;				
		}
		if(M_objSOURC == btnFETCH)
		{
			String L_strTEMP= "";
			if(txtSPBUY.getText().trim().length()>0)			
				L_strTEMP = "Customer : "+txtSPBUY.getText().trim();						
			if(txtSPLOT.getText().trim().length()>0)
				L_strTEMP = L_strTEMP + " Lot No. : "+txtSPLOT.getText().trim();
			if(txtSPECD.getText().trim().length()>0)			
				L_strTEMP = L_strTEMP +" "+ strSPECI+" : "+txtSPEDS.getText().trim();			
			if(L_strTEMP.trim().length()==0)
				L_strTEMP = "Lot Details Between Date "+ txtFMDAT.getText().trim() +" AND "+ txtTODAT.getText().trim();
			lbl2.setText(L_strTEMP);
				
			tblITMDL.clrTABLE();
			getDATA("TABLE");
		}
		else if(M_objSOURC == cmbPRDTP)
		{
			strPRDTP = cmbPRDTP.getSelectedItem().toString().trim().substring(0,2);
			txtFMDAT.requestFocus();
			txtSPBUY.setText("");
			txtSPLOT.setText("");
			txtSPECD.setText("");
			txtSPEDS.setText("");
			txtTOTQT.setText("");
			txtTOTPK.setText("");
			lbl2.setText("");
			if(flgFIRST == false)
				tblITMDL.clrTABLE();
			setMSG("Enter From Date to view lot details within the given Date Range..",'N');
		}
		else if(M_objSOURC == txtFMDAT)
		{			
			txtTODAT.requestFocus();
			setMSG("Enter TO Date to view lot details within the given Date Range..",'N');
		}
		else if(M_objSOURC == txtTODAT)
		{			
			cmbSPECI.requestFocus();
			setMSG("Select Criteria to view lot details ..",'N');
		}
		if(M_objSOURC == cmbSPECI)
		{				
			StringTokenizer L_stkTEMP = new StringTokenizer(cmbSPECI.getSelectedItem().toString(),"w");
			if(L_stkTEMP.hasMoreTokens())			
				strSPECI =L_stkTEMP.nextToken();
			lbl1.setText(strSPECI);
			txtSPBUY.setText("");
			txtSPLOT.setText("");
			txtSPECD.setText("");
			txtSPEDS.setText("");
			txtTOTQT.setText("");
			txtTOTPK.setText("");
			lbl2.setText("");
			if(flgFIRST == false)
				tblITMDL.clrTABLE();
			if(strSPECI.equals("Customer"))
			{
				txtSPBUY.setText("");
				txtSPBUY.setEnabled(false);
				txtSPLOT.setEnabled(true);
				rdbALLCT.setEnabled(true);
				setMSG("Enter Customer code OR Press F1 to select from List..",'N');
				txtSPLOT.requestFocus();
			}
			else if(strSPECI.equals("Lot"))
			{
				txtSPLOT.setText("");
				txtSPBUY.setEnabled(true);
				txtSPLOT.setEnabled(false);
				rdbALLCT.setEnabled(false);
				setMSG("Enter Lot Number OR Press F1 to select from List..",'N');				
				txtSPBUY.requestFocus();
			}
			else
			{
				txtSPBUY.requestFocus();
				txtSPBUY.setEnabled(true);
				txtSPLOT.setEnabled(true);
				rdbALLCT.setEnabled(false);
				setMSG("Enter Grade OR Press F1 to select from List..",'N');
			}
		}
		if(M_objSOURC == txtSPBUY)
		{
			if(strSPECI.equals("Lot"))
			{
				txtSPECD.requestFocus();
				setMSG("Enter "+ lbl1.getText()+" or press F1 to Select from List..",'N');				
			}
			else
			{
				txtSPLOT.requestFocus();
				setMSG("Enter Lot Number  or press F1 to Select from List..",'N');
			}
		}
		if(M_objSOURC == txtSPLOT)
		{
			txtSPECD.requestFocus();
			setMSG("Enter "+ lbl1.getText()+" or press F1 to Select from List..",'N');
		}
		if(M_objSOURC == rdbALLCT)
		{
			if(rdbALLCT.isSelected())
			{
				txtSPBUY.setText("");
				txtSPLOT.setText("");
				txtSPECD.setText("");
				txtSPBUY.setEnabled(false);
				txtSPLOT.setEnabled(false);
				txtSPECD.setEnabled(false);				
				setMSG("Enter "+lbl1.getText()+" Code or Press F1 to Select from List..",'N');
			}
			else
			{
				txtSPBUY.setEnabled(true);
				txtSPLOT.setEnabled(true);
				txtSPECD.setEnabled(true);
			}
		}
		if(M_objSOURC == rdbSPECT)
		{
			if(rdbSPECT.isSelected())
			{
				if(strSPECI.equals("Customer"))
				{
					txtSPBUY.setText("");
					txtSPBUY.setEnabled(false);
					txtSPLOT.setEnabled(true);
					rdbALLCT.setEnabled(true);
					setMSG("Enter Customer code OR Press F1 to select from List..",'N');
					txtSPLOT.requestFocus();
				}
				else if(strSPECI.equals("Lot"))
				{
					txtSPLOT.setText("");					
					txtSPBUY.setEnabled(true);
					txtSPLOT.setEnabled(false);
					rdbALLCT.setEnabled(false);
					setMSG("Enter Lot Number OR Press F1 to select from List..",'N');				
					txtSPBUY.requestFocus();
				}
				else
				{
					txtSPBUY.requestFocus();
					txtSPBUY.setEnabled(true);
					txtSPLOT.setEnabled(true);
					rdbALLCT.setEnabled(false);
					setMSG("Enter Grade OR Press F1 to select from List..",'N');
				}
			}			
		}
		if(M_objSOURC == txtSPECD)
		{
			if(txtSPECD.getText().trim().length()==0)
				txtSPEDS.setText("");
			//getDATA("TABLE");
			btnFETCH.requestFocus();
		}			
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		if(L_KE.getKeyCode() == KeyEvent.VK_F1)
		{
			try
			{
				if(((M_objSOURC == txtSPECD) && (strSPECI.equals("Customer"))) ||(M_objSOURC == txtSPBUY))
				{
					txtSPEDS.setText("");
					if(txtFMDAT.getText().trim().length()>0)
						strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					if(txtTODAT.getText().trim().length()>0)
						strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
				
					M_strSQLQRY = "Select distinct IVT_BYRCD,PT_PRTNM from CO_PTMST,MR_IVTRN,FG_ISTRN where "
						+ " PT_PRTCD = IVT_BYRCD AND PT_PRTTP='C' AND IVT_MKTTP = IST_MKTTP AND"		
						+ " IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO ";					
					
					if((strFMDAT.length()>0)&&(strTODAT.length()>0))				
						M_strSQLQRY +=" AND date(IVT_INVDT) between '"+strFMDAT+"' AND '"+strTODAT+"' ";
					else if(strFMDAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) >'"+ strFMDAT +"' ";
					else if(strTODAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) <'"+ strTODAT +"' ";
														
					if(txtSPLOT.getText().trim().length()>0)
						M_strSQLQRY +=" AND IST_LOTNO ='"+ txtSPLOT.getText().trim() +"'";
					
					if(txtSPECD.getText().trim().length()>0)
					{
						if(strSPECI.equals("Customer"))						
							M_strSQLQRY +=" AND IVT_BYRCD = '"+ txtSPECD.getText().trim() +"'";
						else if(strSPECI.equals("Grade"))						
							M_strSQLQRY +=" AND IVT_PRDCD = '"+ txtSPECD.getText().trim() +"'";
						else if(strSPECI.equals("Lot"))						
							M_strSQLQRY +=" AND IST_LOTNO = '"+ txtSPECD.getText().trim() +"'";
					}										
					M_strSQLQRY +="order by IVT_BYRCD";
				}
				if(((M_objSOURC == txtSPECD) && (strSPECI.equals("Lot"))) ||(M_objSOURC == txtSPLOT))		
				{
					if(txtFMDAT.getText().trim().length()>0)
						strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					if(txtTODAT.getText().trim().length()>0)
						strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));															
										
					M_strSQLQRY = "Select distinct IST_LOTNO,PR_PRDDS from MR_IVTRN,FG_ISTRN,CO_PRMST where "
						+" IVT_MKTTP = IST_MKTTP AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO AND IVT_PRDCD = IST_PRDCD AND IST_PRDCD = PR_PRDCD";		
					
					if((strFMDAT.length()>0)&&(strTODAT.length()>0))										
						M_strSQLQRY +=" AND date(IVT_INVDT) between '"+strFMDAT+"' AND '"+strTODAT+"' ";
					else if(strFMDAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) >'"+ strFMDAT +"' ";
					else if(strTODAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) <'"+ strTODAT +"' ";
										
					if(txtSPECD.getText().trim().length()>0)
					{
						if(strSPECI.equals("Customer"))						
							M_strSQLQRY +=" AND IVT_BYRCD = '"+ txtSPECD.getText().trim() +"'";						
						else if(strSPECI.equals("Grade"))						
							M_strSQLQRY +=" AND IVT_PRDCD = '"+ txtSPECD.getText().trim() +"'";						
						else if(strSPECI.equals("Lot"))						
							M_strSQLQRY +=" AND IST_LOTNO = '"+ txtSPECD.getText().trim() +"'";						
					}					
					if(txtSPBUY.getText().trim().length()>0)					
						M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtSPBUY.getText().trim() +"'";
				}
				if(M_objSOURC == txtSPECD)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPECD";												
					if(strSPECI.equals("Customer"))
					{
						String L_arrHDR[] = {"Party Code","Party Description"};						
						if(txtSPECD.getText().trim().length()>0)
							M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtSPECD.getText().trim() +"%'";					
						cl_hlp(M_strSQLQRY,1,1,L_arrHDR,2,"CT");
					}
					else if(strSPECI.equals("Grade"))
					{
						String L_arrHDR[] = {"Product Code","Product Description"};						
						if(txtFMDAT.getText().trim().length()>0)
							strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
						if(txtTODAT.getText().trim().length()>0)
							strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));															
										
						M_strSQLQRY = "Select distinct IVT_PRDCD,PR_PRDDS from MR_IVTRN,FG_ISTRN,CO_PRMST where "
							+" IVT_MKTTP = IST_MKTTP AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO AND IVT_PRDCD = IST_PRDCD AND IST_PRDCD = PR_PRDCD";		
					
						if((strFMDAT.length()>0)&&(strTODAT.length()>0))										
							M_strSQLQRY +=" AND date(IVT_INVDT) between '"+strFMDAT+"' AND '"+strTODAT+"' ";
						else if(strFMDAT.length()>0)
							M_strSQLQRY +=" AND date(IVT_INVDT) >'"+ strFMDAT +"' ";
						else if(strTODAT.length()>0)
							M_strSQLQRY +=" AND date(IVT_INVDT) <'"+ strTODAT +"' ";
											
						if(txtSPECD.getText().trim().length()>0)
						{
							if(strSPECI.equals("Customer"))						
								M_strSQLQRY +=" AND IVT_BYRCD = '"+ txtSPECD.getText().trim() +"'";						
							else if(strSPECI.equals("Grade"))						
								M_strSQLQRY +=" AND IVT_PRDCD = '"+ txtSPECD.getText().trim() +"'";						
							else if(strSPECI.equals("Lot"))						
								M_strSQLQRY +=" AND IST_LOTNO = '"+ txtSPECD.getText().trim() +"'";						
						}
						if(txtSPBUY.getText().trim().length()>0)					
							M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtSPBUY.getText().trim() +"'";
						if(txtSPLOT.getText().trim().length()>0)					
							M_strSQLQRY +=" AND IST_LOTNO ='"+ txtSPLOT.getText().trim() +"'";
						cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");
					}
					else if(strSPECI.equals("Lot"))
					{
						String L_arrHDR[] = {"Lot No.","Grade"};						
						if(txtSPECD.getText().trim().length()>0)
							M_strSQLQRY +=" AND IST_LOTNO LIKE '"+ txtSPECD.getText().trim() +"%'";
						M_strSQLQRY +=" order by IST_LOTNO";			
						cl_hlp(M_strSQLQRY,1,1,L_arrHDR,2,"CT");
					}																
				}
				else if(M_objSOURC == txtSPBUY)
				{									
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPBUY";		
					String L_arrHDR[] = {"Party Code","Party Description"};					
					if(txtSPBUY.getText().trim().length()>0)
						M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtSPBUY.getText().trim() +"%'";					
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");
				}
				else if(M_objSOURC == txtSPLOT)
				{										
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPLOT";				
					String L_arrHDR[] = {"Lot No.","Grade"};
					if(txtSPLOT.getText().trim().length()>0)					
						M_strSQLQRY +=" AND IST_LOTNO like ='"+ txtSPLOT.getText().trim() +"%'";
					M_strSQLQRY +=" order by IST_LOTNO";					
					cl_hlp(M_strSQLQRY,1,1,L_arrHDR,2,"CT");								
				}
			}		
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
		}
	}
	/**
	 * Method to execute the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{					
			if(M_strHLPFLD.equals("txtSPECD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;				
				txtSPECD.setText(cl_dat.M_strHLPSTR_pbst);
				txtSPEDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtSPBUY"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtSPBUY.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtSPLOT"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtSPLOT.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())		
			return;		
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_qrlot.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "mr_qrlot.doc";				
			getDATA("REPORT");			
			/*
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}*/
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
			       {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Lot Details Query"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}	
	}	
	
	void getDATA(String P_strREPORT)
	{
		String L_strTEMP ="",L_strORDER="",L_strBUYER="";
		String L_strGRADE="",L_strLOTNO="",L_strPRTDS="",L_strSELC3="",L_strSELC4="";
		int L_intCOUNT = 0;
		StringBuffer L_stbDATA = new StringBuffer();
		strHEAD2 = "";
		intTOTPK = 0;
		dblTOTQT = 0;
		double L_dblTOTQT = 0;
		int L_intTOTPK = 0;
		L_intCOUNT = 0;
		ResultSet L_rstRSSET;
		try 
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(txtFMDAT.getText().trim().length()>0)
				strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			if(txtTODAT.getText().trim().length()>0)
				strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));												
								
			if(strSPECI.equals("Customer"))
			{					
				L_strSELC3 = "IST_LOTNO";
				L_strSELC4 = "IVT_PRDDS";
				if((rdbSPECT.isSelected()) && (txtSPECD.getText().trim().length()>0))
				{
					L_strBUYER = " and IVT_BYRCD = '"+txtSPECD.getText().toString().trim()+"' ";				
					strHEAD2 = "Customer : "+txtSPECD.getText().trim()+"  ";
				}
				L_strORDER = "IVT_BYRCD";				
			}				
			else if(strSPECI.equals("Grade"))
			{					
				L_strSELC3 = "IVT_BYRCD";
				L_strSELC4 = "IST_LOTNO";
				if((rdbSPECT.isSelected()) && (txtSPECD.getText().trim().length()>0))
				{
					L_strGRADE = " and IVT_PRDCD = '"+txtSPECD.getText().toString().trim()+"' ";					
					strHEAD2 = "Grade : "+txtSPECD.getText().trim()+"  ";
				}
				L_strORDER = "IVT_PRDDS";				
			}				
			else if(strSPECI.equals("Lot"))
			{				
				L_strSELC3 = "IVT_BYRCD";
				L_strSELC4 = "IVT_PRDDS";
				if((rdbSPECT.isSelected()) && (txtSPECD.getText().trim().length()>0))
				{
					L_strLOTNO = " AND IST_LOTNO = '"+txtSPECD.getText().toString().trim()+"'";
					strHEAD2 = "Lot : "+txtSPECD.getText().trim()+"  ";
				}
				L_strORDER = "IST_LOTNO";				
			}				
			if(txtSPBUY.getText().trim().length() > 0)				
			{
				L_strPRTDS += " AND IVT_BYRCD = '"+txtSPBUY.getText().toString().trim()+"'";				
				strHEAD2 +="Buyer : "+ txtSPBUY.getText().trim()+"  ";
			}
			if(txtSPLOT.getText().trim().length() > 0)
			{
				L_strPRTDS += " AND IST_LOTNO = '"+txtSPLOT.getText().toString().trim()+"'";			
				strHEAD2 +="Lot : "+ txtSPLOT.getText().trim()+"  ";
			}
				
			if((strFMDAT.length()>0)&&(strTODAT.length()>0))
			{
				L_strTEMP =" AND IST_ISSDT between '"+strFMDAT+"' AND '"+strTODAT+"' ";
				strHEAD1 = " Issue Date between "+ txtFMDAT.getText().trim()+" and "+txtTODAT.getText().trim();
			}
			else if(strFMDAT.length()>0)
			{
				L_strTEMP =" AND IST_ISSDT >'"+ strFMDAT +"' ";
				strHEAD1 = " Issue Date greater than "+ txtFMDAT.getText().trim();
			}
			else if(strTODAT.length()>0)
			{
				L_strTEMP =" AND IST_ISSDT <'"+ strTODAT +"' ";
				strHEAD1 = " Issue Date less than "+ txtTODAT.getText().trim();
			}
			else
			{
				L_strTEMP ="";				
				strHEAD1 = "";
			}
			/****************to fetch customer description, consignee description & to put in hastable Dyanamically.*******************/				
			hstPRTDS = new Hashtable();
			M_strSQLQRY = "Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_IVTRN,FG_ISTRN where "
				+ " PT_PRTCD = IVT_BYRCD AND PT_PRTTP='C' AND IVT_MKTTP = IST_MKTTP AND"		
				+ " IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO "
				+ L_strBUYER + L_strGRADE + L_strLOTNO + L_strPRTDS+L_strTEMP;
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strTEMP1="";
				while(M_rstRSSET.next())
				{
					L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
					if(!L_strTEMP1.equals(""))
						hstPRTDS.put(L_strTEMP1,nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
				}
				M_rstRSSET.close();
			}					
			/********************************/
					
			M_strSQLQRY = "Select IVT_ZONCD,IVT_INVNO,date(IVT_INVDT) datINVDT,IVT_CNSCD,IVT_BYRCD,IST_LOTNO,IVT_PRDDS,"
				+"SUM(IST_ISSQT) IVT_INVQT,SUM(IST_ISSPK) IVT_INVPK,IVT_LADNO,"				//IVT_CNSCD
				+ L_strORDER+" from MR_IVTRN,FG_ISTRN where IVT_MKTTP = IST_MKTTP AND"		
				+" IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO "
				+" AND ifnull(IST_STSFL,'') <> 'X'"				
				+L_strBUYER + L_strGRADE + L_strLOTNO + L_strPRTDS+L_strTEMP
			    +"group by IVT_ZONCD,IVT_CNSCD,IVT_BYRCD,IVT_INVNO,date(IVT_INVDT),"+L_strSELC3+","
				+L_strSELC4+",IVT_LADNO,"+L_strORDER;
				//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
//			System.out.println("11");
			if(M_rstRSSET != null)
			{						
				if(P_strREPORT.equals("TABLE"))
				{
					dblTOTQT = 0;
					intTOTPK = 0;
					while(M_rstRSSET.next())
					{
						if(L_intCOUNT == intROWCT_fn)
						{
							setMSG("More than 1000 Records Found Please Specify Some Specific Criteria..",'E');
							return;
						}
						tblITMDL.setValueAt(new Boolean(false),L_intCOUNT,TB_CHKFL);						
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),L_intCOUNT,TB_INVNO);						
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("datINVDT"),""),L_intCOUNT,TB_INVDT);						
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
						if(hstPRTDS.containsKey(L_strTEMP))
							L_strTEMP = hstPRTDS.get(L_strTEMP).toString();
						tblITMDL.setValueAt(L_strTEMP,L_intCOUNT,TB_BYRCD);
							
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),""),L_intCOUNT,TB_LOTNO);						
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),L_intCOUNT,TB_PRDDS);
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_ZONCD"),"");
						if(hstZONCD.containsKey(L_strTEMP))
							L_strTEMP = hstZONCD.get(L_strTEMP).toString();						
						tblITMDL.setValueAt(L_strTEMP,L_intCOUNT,TB_ZONCD);
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
						if(hstPRTDS.containsKey(L_strTEMP))
							L_strTEMP = hstPRTDS.get(L_strTEMP).toString();
						else
						{
							M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTCD = '"+ L_strTEMP.trim() +"' AND PT_PRTTP ='C'";							
							L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);							
							if(L_rstRSSET != null)
							{																
								if(L_rstRSSET.next())								
									L_strTEMP = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");								
							}
							L_rstRSSET.close();
						}
						tblITMDL.setValueAt(L_strTEMP,L_intCOUNT,TB_CNSCD);											
						
						L_dblTOTQT = M_rstRSSET.getDouble("IVT_INVQT");
						dblTOTQT += L_dblTOTQT;						
						tblITMDL.setValueAt(setNumberFormat(L_dblTOTQT,3),L_intCOUNT,TB_INVQT);						
						
						L_intTOTPK = M_rstRSSET.getInt("IVT_INVPK");
						intTOTPK += L_intTOTPK;
						tblITMDL.setValueAt(String.valueOf(L_intTOTPK).toString(),L_intCOUNT,TB_INVPK);
						
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),""),L_intCOUNT,TB_LADNO);
						L_intCOUNT ++;
					}
					M_rstRSSET.close();
					txtTOTQT.setText(String.valueOf(setNumberFormat(dblTOTQT,3)).toString());
					txtTOTPK.setText(String.valueOf(intTOTPK).toString());					
				}
				else if(P_strREPORT.equals("REPORT"))
				{
					dblTOTQT = 0;
					intTOTPK = 0;
					fosREPORT = new FileOutputStream(strFILNM);
					dosREPORT = new DataOutputStream(fosREPORT);			
					setMSG("Report Generation in Process.......",'N');
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					{
					    prnFMTCHR(dosREPORT,M_strNOCPI17);
					    prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strCPI17);
					}
					if(M_rdbHTML.isSelected())
					{
					    dosREPORT.writeBytes("<HTML><HEAD><Title>Lot Details Query</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
						dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
						dosREPORT.writeBytes("</STYLE>"); 
					}			
					prnHEADER();					
					while(M_rstRSSET.next())
					{	
						L_stbDATA.delete(0,L_stbDATA.length());
						L_stbDATA.append(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),12));
						L_stbDATA.append(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("datINVDT"),""),12));
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
						if(hstPRTDS.containsKey(L_strTEMP))
							L_strTEMP = hstPRTDS.get(L_strTEMP).toString();
						if(L_strTEMP.length()>30)
							L_strTEMP = L_strTEMP.substring(0,28);
						L_stbDATA.append(padSTRING('R',L_strTEMP,30));
							
						L_stbDATA.append(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),""),10));
						L_stbDATA.append(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),12));
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_ZONCD"),"");
						if(hstZONCD.containsKey(L_strTEMP))
							L_strTEMP = hstZONCD.get(L_strTEMP).toString();						
						L_stbDATA.append(padSTRING('R',L_strTEMP,8));
													   
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
						if(hstPRTDS.containsKey(L_strTEMP))
							L_strTEMP = hstPRTDS.get(L_strTEMP).toString();
						else
						{
							M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTCD = '"+ L_strTEMP.trim() +"' AND PT_PRTTP ='C'";							
							L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);							
							if(L_rstRSSET != null)
							{																
								if(L_rstRSSET.next())								
									L_strTEMP = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");								
							}
							L_rstRSSET.close();
						}
						if(L_strTEMP.length()>30)
							L_strTEMP = L_strTEMP.substring(0,28);
						L_stbDATA.append(padSTRING('R',L_strTEMP,30));

						//L_stbDATA.append(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),10));
						//L_stbDATA.append(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVPK"),""),6));
						L_dblTOTQT = M_rstRSSET.getDouble("IVT_INVQT");
						dblTOTQT += L_dblTOTQT;						
						//tblITMDL.setValueAt(setNumberFormat(L_dblTOTQT,3),L_intCOUNT,TB_INVQT);
						L_stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTQT,3),10));
						
						L_intTOTPK = M_rstRSSET.getInt("IVT_INVPK");
						intTOTPK += L_intTOTPK;
						//tblITMDL.setValueAt(String.valueOf(L_intTOTPK).toString(),L_intCOUNT,TB_INVPK);
						L_stbDATA.append(padSTRING('L',String.valueOf(L_intTOTPK).toString(),6));						
						
						L_stbDATA.append(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),""),10)+"\n");
						dosREPORT.writeBytes(L_stbDATA.toString());						
						cl_dat.M_intLINNO_pbst ++;
						if(cl_dat.M_intLINNO_pbst > 64) 
						{							
							dosREPORT.writeBytes("\n" +strDOTLN);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();					
						}	
					}
					M_rstRSSET.close();					
				}				
				if(P_strREPORT.equals("REPORT"))
				{	
					dosREPORT.writeBytes(padSTRING('L',"Total Quantity : "+setNumberFormat(dblTOTQT,3),124)+"\n");
					dosREPORT.writeBytes(padSTRING('L',"Total Packages : "+String.valueOf(intTOTPK).toString(),130));
					dosREPORT.writeBytes("\n"+strDOTLN);
					setMSG("Report completed.. ",'N');
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strEJT);
					}
					if(M_rdbHTML.isSelected())						
					    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");												
					dosREPORT.close();						
					fosREPORT.close();					
				}
			}			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		finally
		{
			if(hstPRTDS != null)
				hstPRTDS = null;
		}
	}
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;										
			dosREPORT.writeBytes("\n\n\n");
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LIMITED",strDOTLN.length()-25));										
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
						
			dosREPORT.writeBytes(padSTRING('R',strSPECI +"wise Lot Details for"+strHEAD1 ,strDOTLN.length()-25));
			dosREPORT.writeBytes("Page No.    : " + cl_dat.M_PAGENO + "\n");
			
			if(strHEAD2.length()>2)				
				dosREPORT.writeBytes(strHEAD2+"\n");								 
			dosREPORT.writeBytes(strDOTLN+"\n");
			dosREPORT.writeBytes("Invoice No. Date        Customer                      Lot No.   Grade      Zone    Consignee                     Invoice Qty   Pkg  LA No."+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");						
			cl_dat.M_intLINNO_pbst = 8;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	Method to validate Party Type & Party Code,i.e. to check for blank and wrong Inputs.
	*/
	boolean vldDATA()
	{			
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount() == 0)
			{					
				setMSG("Please Select the Email/s from List through F1 Help ..",'N');
				return false;
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{ 
			if (M_cmbDESTN.getSelectedIndex() == 0)
			{	
				setMSG("Please Select the Printer from Printer List ..",'N');
				return false;
			}
		}		
		return true;
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if((input == txtFMDAT) && (txtFMDAT.getText().trim().length() == 10))
				{
					if(txtTODAT.getText().trim().length()==10)
					{
						if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
						{			    
							setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
							return false;
						}
					}					
					if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{			    
						setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
						return false;					
					}
				}
				if((input == txtTODAT) && (txtTODAT.getText().trim().length() == 10))
				{					
					if(txtFMDAT.getText().trim().length()==10)
					{
						if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
						{			    
							setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
							return false;
						}
					}					
					if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{			    
						setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
						return false;
					}					
				}
				if((input == txtSPBUY) && (txtSPBUY.getText().trim().length() == 5))
				{									
					M_strSQLQRY = "select PT_PRTCD from CO_PTMST"
						+" where PT_PRTTP ='C' AND PT_PRTCD ='"+ txtSPBUY.getText().trim() +"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{								
							M_rstRSSET.close();							
							return true;
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Buyer Code, Press F1 to select from List..",'E');							
							return false;				
						}							
					}
				}
				if((input == txtSPLOT) && (txtSPLOT.getText().trim().length() == 8))
				{					
					M_strSQLQRY = "select LT_LOTNO from PR_LTMST "
						+"where LT_LOTNO = '"+ txtSPLOT.getText().trim() +"' AND ifnull(LT_STSFL,'') <> 'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{								
							M_rstRSSET.close();							
							return true;								
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Lot Number ..",'E');							
							return false;				
						}							
					}
				}
				if(input == txtSPECD) 
				{
					String L_strMSG="";						
					if((strSPECI.equals("Customer")) && (txtSPECD.getText().trim().length() == 5))
					{
						L_strMSG = "Invalid Customer Code, Press F1 to select from List..";						
						M_strSQLQRY = "select PT_PRTCD from CO_PTMST"
						+" where PT_PRTTP ='C' AND PT_PRTCD ='"+ txtSPECD.getText().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{		
								M_rstRSSET.close();								
								return true;								
							}
							else
							{
								M_rstRSSET.close();
								setMSG(L_strMSG,'E');
								return false;				
							}							
						}
					}
					else if((strSPECI.equals("Lot"))&& (txtSPECD.getText().trim().length() == 10))
					{
						L_strMSG = "Invalid Lot Number, Press F1 to select from List..";						
						M_strSQLQRY = "select LT_LOTNO from PR_LTMST "
						+" where LT_LOTNO = '"+ txtSPECD.getText().trim() +"' AND ifnull(LT_STSFL,'') <> 'X'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{		
								M_rstRSSET.close();								
								return true;								
							}
							else
							{
								M_rstRSSET.close();
								setMSG(L_strMSG,'E');
								return false;				
							}							
						}
					}
					else if((strSPECI.equals("Grade"))&& (txtSPECD.getText().trim().length() == 8))
					{						
						L_strMSG = "Invalid Grade, Press F1 to select from List..";						
						M_strSQLQRY = "Select PR_PRDCD from CO_PRMST where PR_PRDCD ='"+txtSPECD.getText().trim()+"'"
							+" AND ifnull(PR_STSFL,'')<>'X'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{		
								M_rstRSSET.close();								
								return true;								
							}
							else
							{
								M_rstRSSET.close();
								setMSG(L_strMSG,'E');
								return false;				
							}							
						}					
					}
				}																													
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"INPVF");
			}
			return true;
		}
	}	
}