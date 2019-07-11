import javax.swing.JTextField;import javax.swing.JPanel;import javax.swing.JCheckBox;import javax.swing.JPanel;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.BorderFactory;import javax.swing.border.TitledBorder;import javax.swing.JTable;
import javax.swing.JComponent;import javax.swing.InputVerifier;import javax.swing.JTable.*;import javax.swing.JTabbedPane;
import java.awt.event.*;import java.awt.Font;
import java.io.IOException;import java.sql.ResultSet;
import java.util.Hashtable;import java.util.Date;

class mm_mecns extends cl_pbase implements FocusListener, KeyListener
{
	//private ResultSet M_rstRSSET1;
	private cl_JTable tblBOEVL,tblCOTAX;
	private JTextField txtCNSNO,txtMATCD,txtPORNO,txtSHRDS;
	private JTextField txtCNSDT,txtCNSQT,txtBALQT,txtCNSDS;
	private JTextField txtVENCD,txtVENNM;
	private JTextField txtMATTP,txtPKGTP,txtLOCCD,txtBOEQT,txtCHLQT,txtNETQT,txtBOEDT,txtDSRCD,txtBOETP; 
	private JLabel lblCONNO;
	private JCheckBox chkCONFL ;
	private java.awt.Color colBLUE = new java.awt.Color(63,91,167);
	private int intBOECL,intBOERW,intBOECT,intOLDRW = 0,intBECNT =0;
	private String strPORNO,strPORDT,strORDQT,strMATCD,strUOMCD,strCNSNO,strCNSDT,strCNSQT,strCNSDS,strCSBCD="";
	private String strBOENO,strBOEDT,strLOCCD,strBOEQT,strACCVL,strDUTVL,strTEMP;
	private String strDUTPR,strBOETP,strMATTP,strCHLQT,strSTSFL="",strLUSBY,strLUPDT,strNETQT,strOLDFL;
	private String strVENCD,strSTRTP,strDATA;
      private JPanel pnlCNSDT = new JPanel();
	private JPanel pnlBOEDT = new JPanel(null);
	private JPanel pnlCOTAX = new JPanel(null);

	private JTabbedPane tbpMAIN;
	private double dblBALQT;
	private boolean isCHILD = false;

	private final int TB_CHKFL = 0;
	private final int TB_BOENO = 1;
	private final int TB_BOEDT = 2;
	private final int TB_BOETP = 3;
	private final int TB_CSDCD = 4;
	private final int TB_MATCD = 5;
	private final int TB_UOMCD = 6;
	private final int TB_MATDS = 7;

	private final int TB_LOCCD = 8;
	private final int TB_BOEQT = 9;
	private final int TB_ACCVL = 10;
	private final int TB_DUTVL = 11;
	private final int TB_DUTPR = 12;
	private final int TB_CVDVL = 13;
	private final int TB_ACVVL = 14;
	private final int TB_EDCVL = 15;
	private final int TB_EHCVL = 16;
	private final int TB_PKGTP = 17;
	private final int TB_MATTP = 18;
	private final int TB_CHLQT = 19;
	private final int TB_NETQT = 20;
	private final int TB_OLDFL = 21;
	private final int TB_CSBCD = 22;

//	private final int TB_COLCNT = 18; 
	private final String strTRNFL = "0";			
	INPVF objINPVF = new INPVF();
	TBLINPVF objTBLVF = new TBLINPVF(); 
	mm_mecns()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			tbpMAIN=new JTabbedPane();
			tbpMAIN.add("B/E Details",pnlBOEDT);
			// tbpMAIN.addChangeListener(this);
			//ADDING TABBED PANE TO SCREEN			
			pnlCNSDT.setLayout(null);
			pnlCNSDT.setBorder(BorderFactory.createTitledBorder(null,"Consignment Details",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),colBLUE));
			setMatrix(20,8);
			add(new JLabel("Consign. No."),1,1,1,1,pnlCNSDT,'L');
			add(txtCNSNO = new TxtLimit(8),1,2,1,1,pnlCNSDT,'L');
			add(new JLabel("Consign. Date"),1,3,1,1,pnlCNSDT,'L');
			add(txtCNSDT = new TxtDate(),1,4,1,1,pnlCNSDT,'L');
			add(new JLabel("Consign.Qty"),1,5,1,1,pnlCNSDT,'L');
			add(txtCNSQT = new TxtNumLimit(12.3),1,6,1,1,pnlCNSDT,'L');
			add(new JLabel("Balance Qty"),1,7,1,1,pnlCNSDT,'L');
			add(txtBALQT = new TxtNumLimit(12.2),1,8,1,0.8,pnlCNSDT,'L');

			add(new JLabel("Description"),2,1,1,1,pnlCNSDT,'L');
			add(txtCNSDS = new TxtLimit(40),2,2,1,5,pnlCNSDT,'L');
			
		//	add(new JLabel("Sale Type"),2,7,1,1,pnlCNSDT,'L');
		//	add(txtBOETP1 = new TxtLimit(2),2,8,1,0.8,pnlCNSDT,'L');
			
			//add(txtMATCD = new TxtLimit(10),3,2,1,1,pnlCNSDT,'L');
			//add(txtMATDS = new TxtLimit(60),3,3,1,4,pnlCNSDT,'L');			
			
			//add(new JLabel("UOM"),3,7,1,1,pnlCNSDT,'L');
			//add(txtUOMCD = new TxtLimit(10),3,8,1,0.8,pnlCNSDT,'L');
			
			add(new JLabel("Vendor"),3,1,1,1,pnlCNSDT,'L');
			add(txtVENCD = new TxtLimit(10),3,2,1,1,pnlCNSDT,'L');
			add(txtVENNM = new TxtLimit(40),3,3,1,4,pnlCNSDT,'L');

		//	add(new JLabel("C.S.Code"),3,7,1,1,pnlCNSDT,'L');
		//	add(txtDSRCD = new TxtLimit(5),3,8,1,1,pnlCNSDT,'L');
			
			add(new JLabel("P.O.No."),4,1,1,1,pnlCNSDT,'L');
			add(txtPORNO = new TxtLimit(8),4,2,1,1,pnlCNSDT,'L');
			add(txtSHRDS = new TxtLimit(60),4,3,1,4,pnlCNSDT,'L');			
		
			add(chkCONFL = new JCheckBox("Completion Tag"),4,7,1,1.5,pnlCNSDT,'L');
			add(lblCONNO = new JLabel("Bill Of Entry Details"),7,1,1,2,this,'L');
			add(pnlCNSDT,1,1,5,8,this,'L');
			
			txtBALQT.setEnabled(false);
			//txtMATDS.setEnabled(false);
			//txtUOMCD.setEnabled(false);
			txtVENNM.setEnabled(false);
			String[] L_COLHD = {"","B/E No","B/E Date","B/E Type","C.S Code","Item Code","UOM","Item Desc","Dsp.Loc","Quantity","Acc.Value","Duty","%age","CVD Val","Add. CVD","ED.Cess","H.ED Cess","Pkg Type.","Mat.Type","Chalan Qty","Net Qty","O","CSBCD"};
			int[] L_COLSZ = {10,110,80,40,50,80,30,100,40,70,70,50,50,50,50,50,50,50,30,80,75,5,5};
			intBOECT = 2000;
			//tblBOEVL = crtTBLPNL1(pnlBOEDT,L_COLHD,intBOECT,1,1,9,8,L_COLSZ,new int[]{0});	
			tblBOEVL = crtTBLPNL1(pnlBOEDT,L_COLHD,intBOECT,1,1,7,7.9,L_COLSZ,new int[]{0});	
			add(tbpMAIN,8,1,9,8,this,'L');
			tblBOEVL.setCellEditor(TB_BOETP,txtBOETP = new TxtLimit(2));
			tblBOEVL.setCellEditor(TB_CSDCD,txtDSRCD = new TxtLimit(5));
			tblBOEVL.setCellEditor(TB_MATTP,txtMATTP = new TxtLimit(2));
			tblBOEVL.setCellEditor(TB_PKGTP,txtPKGTP = new TxtLimit(2));
			tblBOEVL.setCellEditor(TB_LOCCD,txtLOCCD = new TxtLimit(3));
			tblBOEVL.setCellEditor(TB_BOEDT,txtBOEDT = new TxtDate());
			tblBOEVL.setCellEditor(TB_BOEQT,txtBOEQT = new TxtNumLimit(12.3));
			tblBOEVL.setCellEditor(TB_CHLQT,txtCHLQT = new TxtNumLimit(12.3));
			tblBOEVL.setCellEditor(TB_NETQT,txtNETQT = new TxtNumLimit(12.3));
			tblBOEVL.setCellEditor(TB_MATCD,txtMATCD = new TxtLimit(10));

			txtBOETP.addFocusListener(this);txtBOETP.addKeyListener(this);
			txtDSRCD.addFocusListener(this);txtDSRCD.addKeyListener(this);
			txtBOEDT.addFocusListener(this);txtBOEDT.addKeyListener(this);
			txtMATTP.addFocusListener(this);txtMATTP.addKeyListener(this);
			txtPKGTP.addFocusListener(this);txtPKGTP.addKeyListener(this);
			txtLOCCD.addFocusListener(this);txtLOCCD.addKeyListener(this);
			txtBOEQT.addFocusListener(this);txtBOEQT.addKeyListener(this);
			txtNETQT.addFocusListener(this);txtNETQT.addKeyListener(this);
			txtCHLQT.addFocusListener(this);txtCHLQT.addKeyListener(this);
			txtMATCD.addFocusListener(this);txtMATCD.addKeyListener(this);

			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		//	txtMATCD.setInputVerifier(objINPVF);
			txtCNSNO.setInputVerifier(objINPVF);
			txtVENCD.setInputVerifier(objINPVF);
		//	txtDSRCD.setInputVerifier(objINPVF);
			txtCNSDT.setInputVerifier(objINPVF);
			tblBOEVL.setInputVerifier(objTBLVF);
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			setENBL(false);
			txtCNSNO.setEnabled(false);
	
		}
		catch(Exception E)
		{
			setMSG(E,"constructor");
		}					
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtCNSNO.setEnabled(true);
		//txtMATCD.setEnabled(L_flgSTAT);
		txtCNSDT.setEnabled(L_flgSTAT);
		txtCNSQT.setEnabled(L_flgSTAT);
		txtVENCD.setEnabled(L_flgSTAT);
		txtSHRDS.setEnabled(false);
		//txtMATDS.setEnabled(false);
		//txtUOMCD.setEnabled(false);
		txtBALQT.setEnabled(false);
		txtVENNM.setEnabled(false);
        tblBOEVL.cmpEDITR[TB_CHLQT].setEnabled(false);
        tblBOEVL.cmpEDITR[TB_NETQT].setEnabled(false);
        tblBOEVL.cmpEDITR[TB_UOMCD].setEnabled(false);
        tblBOEVL.cmpEDITR[TB_MATDS].setEnabled(false);


		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
	    {
			tblBOEVL.cmpEDITR[TB_CHKFL].setEnabled(true);
		}
	//	txtEDITR2.setEnabled(act);
	
	}
	void clrCOMP()
	{
		txtCNSNO.setText("");
		txtCNSDT.setText("");
		txtCNSQT.setText("");
		txtCNSDS.setText("");
		//txtBOETP1.setText("");
		//txtDSRCD.setText("");
		txtBALQT.setText("");
		//txtMATCD.setText("");
		//txtMATDS.setText("");
		//txtUOMCD.setText("");
		txtVENCD.setText("");
		txtVENNM.setText("");
		lblCONNO.setText("");
		txtPORNO.setText("");
		txtSHRDS.setText("");
		chkCONFL.setSelected(false);
		lblCONNO.setText("Bill of Entry Detail");
		//exeCLRTBL(tblBOEVL);
		tblBOEVL.clrTABLE();
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				txtCNSNO.requestFocus();
				clrCOMP();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					setENBL(true);
				else
					setENBL(false);
				
			}
			if(M_objSOURC == txtCNSNO)
			{	
				strSTRTP = M_strSBSCD.substring(2,4);
				//strPORNO = txtCNSNO.getText().trim();
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
				{
					if(exeGETREC(strSTRTP,txtCNSNO.getText().trim()))
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
						{
							setENBL(true);
							//txtMATCD.requestFocus();
						}
					}
					else
						setMSG("Record not found",'E');
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			setMSG("",'N');
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{	
				if(M_objSOURC == txtCNSNO)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtCNSNO";
					String L_ARRHDR[] = {"Cons. No.","Desc","Date","Vendor"};
					M_strSQLQRY = "select Distinct BE_CONNO,BE_CONDS,BE_CONDT,PT_PRTNM";                        
					M_strSQLQRY += " from MM_BETRN,MM_POMST,CO_PTMST";                                         
					M_strSQLQRY += " where BE_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and BE_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
					M_strSQLQRY += " and PO_STRTP = '"+M_strSBSCD.substring(2,4)+ "'";
					M_strSQLQRY += " and PT_PRTTP = 'S'";
					M_strSQLQRY += " and PO_VENCD = PT_PRTCD and BE_PORNO = PO_PORNO";
					if(txtCNSNO.getText().trim().length() >0)
					M_strSQLQRY +=	" AND BE_CONNO like '"+txtCNSNO.getText().trim()+"%'";
					M_strSQLQRY += " and BE_STSFL <> 'X' order by BE_CONDT DESC";
				  cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				}
				else if(M_objSOURC == txtPORNO)	
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPORNO";
					String L_ARRHDR[] = {"P.O.No.","P.O.Date"};
					M_strSQLQRY = "Select Distinct PO_PORNO,PO_SHRDS from MM_POMST ";
					M_strSQLQRY += " where PO_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PO_VENCD = '"+txtVENCD.getText().trim()+"'";
					M_strSQLQRY += " ORDER BY PO_PORNO DESC";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtMATCD)	
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMATCD";
					String L_ARRHDR[] = {"Code","Material Desc","UOM"};
				//	M_strSQLQRY = "Select Distinct CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST,MM_TKMST";
				//	M_strSQLQRY += " where CT_MATCD = TK_MATCD and CT_CODTP ='CD'";
					M_strSQLQRY = "Select Distinct PO_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST,MM_POMST ";
					M_strSQLQRY += " where CT_CODTP ='CD' AND CT_MATCD = PO_MATCD AND PO_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PO_PORNO ='"+txtPORNO.getText().trim()+"'";

					if(txtMATCD.getText().trim().length() >0)
						M_strSQLQRY +=	" AND PO_MATCD like '"+txtMATCD.getText().trim()+"%'";
					M_strSQLQRY += " order by CT_MATDS desc";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
				}
				else if(M_objSOURC==txtVENCD)	// Vendor
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVENCD";
					String L_ARRHDR[] = {"Code","Supplier"};
					M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST";
					M_strSQLQRY += " where PT_PRTTP = 'S'";
					if(txtVENCD.getText().trim().length() >0)
						M_strSQLQRY +=	" AND PT_PRTCD like '"+txtVENCD.getText().trim()+"%'";
					M_strSQLQRY += "Order BY PT_PRTCD";
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC==txtDSRCD)	// Vendor
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDSRCD";
					String L_ARRHDR[] = {"Code","Supplier"};
					M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST";
					M_strSQLQRY += " where PT_PRTTP = 'G'";
					if(txtVENCD.getText().trim().length() >0)
						M_strSQLQRY +=	" AND PT_PRTCD like '"+txtDSRCD.getText().trim()+"%'";
					M_strSQLQRY += "Order BY PT_PRTCD";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtBOETP)				// Bill of Entry Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtBOETP";
					String L_ARRHDR[] = {"Code","Bill of Entry Type"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXBOE'";
					if(txtBOETP.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtBOETP.getText()+"%'";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
				/*else if(M_objSOURC == txtBOETP1)				// Bill of Entry Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtBOETP1";
					String L_ARRHDR[] = {"Code","Bill of Entry Type"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXBOE'";
					if(txtBOETP1.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtBOETP1.getText()+"%'";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}*/

				else if(M_objSOURC == txtMATTP)			// Material Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMATTP";
					String L_ARRHDR[] = {"Code","Material Type"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
					if(txtMATTP.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtMATTP.getText()+"%'";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtPKGTP)			// Material Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPKGTP";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG'";
					if(txtMATTP.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtPKGTP.getText()+"%'";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}

				else if(M_objSOURC == txtLOCCD)			// Location Code
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtLOCCD";
					String L_ARRHDR[] = {"Code","Location"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " and CMT_CGSTP = 'QC11TKL'";
					if(txtLOCCD.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtLOCCD.getText()+"%'";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}

			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtCNSNO)
				{
					txtCNSDT.requestFocus();
					setMSG("Enter the Consignment Date ..",'N');
				}
				else if(M_objSOURC == txtCNSDT)
				{
					txtCNSQT.requestFocus();
					setMSG("Enter the Consignment Quantity ..",'N');
				}
				else if(M_objSOURC == txtDSRCD)
				{
					txtPORNO.requestFocus();
					setMSG("Enter the P.O.NO. ..",'N');
				}

				else if(M_objSOURC == txtCNSQT)			// Ordered Quantity
				{
					txtCNSDS.requestFocus();
					setMSG("Enter Consignment Description ..",'N');
				}
				else if(M_objSOURC == txtCNSDS)			// Ordered Quantity
				{
					txtVENCD.requestFocus();
					setMSG("Enter the Vendor Code..",'N'); 
				}
				/*else if(M_objSOURC == txtBOETP1)			// Ordered Quantity
				{
					txtVENCD.requestFocus();
					setMSG("Enter the Vendor Code..",'N'); 
				}*/
				else if(M_objSOURC == txtVENCD)
				{		
					strVENCD = txtVENCD.getText().trim().toUpperCase();
					txtVENCD.setText(strVENCD);
					/*if(txtBOETP1.getText().equals("02"))
						txtDSRCD.requestFocus();
					else*/
						txtPORNO.requestFocus();
				}
				else if(M_objSOURC == txtPORNO)
				{	
					tblBOEVL.setRowSelectionInterval(0,0);
					tblBOEVL.setColumnSelectionInterval(0,0);
					tblBOEVL.editCellAt(0,1);
					//tblBOEVL.cmpEDITR[1].requestFoucs();
				}
				else if(M_objSOURC == tblBOEVL.cmpEDITR[TB_BOENO])
				{		
					tblBOEVL.setValueAt("0.00",tblBOEVL.getSelectedRow(),TB_ACCVL);
					tblBOEVL.setValueAt("0.00",tblBOEVL.getSelectedRow(),TB_DUTPR);
					tblBOEVL.setValueAt("0.00",tblBOEVL.getSelectedRow(),TB_DUTVL);
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"keyPressed");
		}
	}
	// Validation of Vendor
	private boolean vldVENCD(String P_strVENCD)
	{
		try
		{
			M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
			M_strSQLQRY += " where PT_PRTTP = 'S'";
			M_strSQLQRY += " and PT_PRTCD = '" + P_strVENCD + "' and isnull(Pt_stsfl,'') <>'X'";
     		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));
					setMSG("",'N');
					M_rstRSSET.close();
					return true;
				}
			}
			txtVENNM.setText("");
			setMSG("Invalid Vendor. Press F1 for help",'E');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldVENCD");
			return false;
		}	
		return false;
	}
	
	// Validation on Location
	private boolean vldLOCCD(String P_strLOCCD)
	{
		try
		{
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'QC11TKL' and ";
			M_strSQLQRY += " CMT_CODCD = '" + P_strLOCCD + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					setMSG("",'N');
					M_rstRSSET.close();			
					return true;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldLOCCD");
		}
		finally
		{
			M_rstRSSET = null;
		}
		return false;
	}

	// Validation on Bill of Entry Type
	private boolean vldBOETP(String P_strBOETP)
	{
		try
		{
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXBOE'";
			M_strSQLQRY += " and CMT_CODCD = '" + P_strBOETP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldBOETP");
			return false;
		}	
		return false;
	}
	// Validation on Material Type
	private boolean vldMATTP(String P_strMATTP)
	{
		try
		{
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
			M_strSQLQRY += " and CMT_CODCD = '" + P_strMATTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldMATTP");
			return false;
		}	
		return false;
	}
	private boolean vldPKGTP(String P_strPKGTP)
	{
		try
		{
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG'";
			M_strSQLQRY += " and CMT_CODCD = '" + P_strPKGTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldPKGTP");
			return false;
		}	
		return false;
	}

	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtCNSNO"))
			{
				strPORNO = cl_dat.M_strHLPSTR_pbst;
				txtCNSNO.setText(strPORNO);
			}
			else if(M_strHLPFLD.equals("txtMATCD"))
			{
				txtMATCD.setText(cl_dat.M_strHLPSTR_pbst);
				//System.out.println(cl_dat.M_strHLPSTR_pbst);
				tblBOEVL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblBOEVL.getSelectedRow(),TB_MATDS);
				tblBOEVL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblBOEVL.getSelectedRow(),TB_UOMCD);
//				tblBOEVL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblBOEVL.getSelectedRow(),TB_MATCD);
			}
			else if(M_strHLPFLD.equals("txtVENCD"))
			{
				txtVENNM.setText(cl_dat.M_strHLPSTR_pbst);
				txtVENCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			}
		/*	else if(M_strHLPFLD.equals("txtDSRCD"))
			{
//				txtVENNM.setText(cl_dat.M_strHLPSTR_pbst);
				txtDSRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			}*/

			else if(M_strHLPFLD.equals("txtPORNO"))
			{
				txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);
				txtSHRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtBOETP"))
				txtBOETP.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtDSRCD"))
				txtDSRCD.setText(cl_dat.M_strHLPSTR_pbst);

		//	else if(M_strHLPFLD.equals("txtBOETP1"))
		//		txtBOETP1.setText(cl_dat.M_strHLPSTR_pbst);

			else if(M_strHLPFLD.equals("txtLOCCD"))
				txtLOCCD.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtMATTP"))
				 txtMATTP.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtPKGTP"))
				 txtPKGTP.setText(cl_dat.M_strHLPSTR_pbst);

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	// Method to retrieve the data from MM_TEBET table
	private boolean exeGETREC(String P_strSTRTP,String P_strPORNO)
	{
		intOLDRW = 0;			
		try
		{
			boolean L_FIRST = true;
			java.sql.Date L_datTEMP;
			int i =0;
			String L_OLDCNS = "", L_DATA = "",L_KEY = "";
			double L_TBEQT =0;
			
			M_strSQLQRY = "Select distinct BE_STRTP,BE_MATCD,BE_UOMCD,BE_CONNO,BE_PORNO,BE_CONDT,BE_STSFL,";
			M_strSQLQRY += "BE_CONQT,BE_CONDS,BE_BOENO,BE_BOEDT,BE_CVDVL,BE_EDCVL,BE_EHCVL,BE_ACVVL,";
			M_strSQLQRY += "BE_LOCCD,BE_BOEQT,BE_ACCVL,BE_DUTVL,BE_DUTPR,BE_BOETP,BE_MATTP,BE_PKGTP,BE_CSDCD,BE_CSBCD,PO_SHRDS,";
			M_strSQLQRY += "BE_CHLQT,BE_NETQT,PO_VENCD,PO_MATDS from MM_BETRN,MM_POMST ";
			M_strSQLQRY += "where BE_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and BE_STRTP = '" + P_strSTRTP + "'";
			M_strSQLQRY += " and BE_CMPCD = PO_CMPCD and BE_STRTP = PO_STRTP ";
			M_strSQLQRY += " and BE_PORNO = PO_PORNO AND BE_MATCD = PO_MATCD " ;
			M_strSQLQRY += " and BE_CONNO = '" + P_strPORNO + "'";
			M_strSQLQRY += " and BE_STSFL <> 'X'";
			M_strSQLQRY += " order by BE_CONNO,BE_BOENO";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			tblBOEVL.clrTABLE();
			tblBOEVL.setRowSelectionInterval(0,0);
			tblBOEVL.setColumnSelectionInterval(0,0);
			dblBALQT = 0;
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				if(L_FIRST)
				{
					L_FIRST = false;
					
					//strMATCD = M_rstRSSET.getString("BE_MATCD");
					//txtMATCD.setText(strMATCD);
					//txtMATDS.setText(getMATDS(strMATCD));
					//txtUOMCD.setText(M_rstRSSET.getString("BE_UOMCD"));
					txtPORNO.setText(M_rstRSSET.getString("BE_PORNO"));
					txtSHRDS.setText(M_rstRSSET.getString("PO_SHRDS"));
					txtVENCD.setText(M_rstRSSET.getString("PO_VENCD"));
					//txtBOETP1.setText(M_rstRSSET.getString("BE_BOETP"));
					txtDSRCD.setText(nvlSTRVL(M_rstRSSET.getString("BE_CSDCD"),""));
				//	txtDSRCD.setText(nvlSTRVL(M_rstRSSET.getString("BE_CSDCD"),""));
				//	strCSBCD =nvlSTRVL(M_rstRSSET.getString("BE_CSBCD"),"");
					txtVENNM.setText(getVENNM(txtVENCD.getText().trim()));
					strCNSNO = nvlSTRVL(M_rstRSSET.getString("BE_CONNO"),"");
					txtCNSNO.setText(strCNSNO);	
					L_datTEMP =  M_rstRSSET.getDate("BE_CONDT");
					if(L_datTEMP !=null)
					{
						strCNSDT = M_fmtLCDAT.format(L_datTEMP);
					}
					txtCNSDT.setText(strCNSDT);	
					strCNSQT = nvlSTRVL(M_rstRSSET.getString("BE_CONQT"),"0");
					if(nvlSTRVL(M_rstRSSET.getString("BE_STSFL")," ").equals("C"))
					{
						chkCONFL.setSelected(true);
						strSTSFL ="C";
						chkCONFL.setEnabled(false);
					}
					else
					{
						chkCONFL.setSelected(false);
						chkCONFL.setEnabled(true);
						strSTSFL ="";
					}
					txtCNSQT.setText(strCNSQT);	
					intBOERW =0;
				}
				txtCNSDS.setText(nvlSTRVL(M_rstRSSET.getString("BE_CONDS"),""));
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_BOENO"),""),intBOERW,TB_BOENO);
				//tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_BOETP"),""),intBOERW,TB_BOETP);
				L_datTEMP =  M_rstRSSET.getDate("BE_BOEDT");
				if(L_datTEMP !=null)
				{
					tblBOEVL.setValueAt(M_fmtLCDAT.format(L_datTEMP),intBOERW,TB_BOEDT);
				}
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_LOCCD")," "),intBOERW,TB_LOCCD);
				strBOEQT = nvlSTRVL(M_rstRSSET.getString("BE_BOEQT"),"0");
				tblBOEVL.setValueAt(strBOEQT,intBOERW,TB_BOEQT);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_BOETP"),"0"),intBOERW,TB_BOETP);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_CSDCD"),"0"),intBOERW,TB_CSDCD);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_CSBCD"),"0"),intBOERW,TB_CSBCD);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_MATCD"),"0"),intBOERW,TB_MATCD);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_MATDS"),"0"),intBOERW,TB_MATDS);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_UOMCD"),"0"),intBOERW,TB_UOMCD);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_ACCVL"),"0"),intBOERW,TB_ACCVL);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_DUTVL"),"0"),intBOERW,TB_DUTVL);

				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_CVDVL"),"0"),intBOERW,TB_CVDVL);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_EDCVL"),"0"),intBOERW,TB_EDCVL);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_EHCVL"),"0"),intBOERW,TB_EHCVL);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_ACVVL"),"0"),intBOERW,TB_ACVVL);

				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_DUTPR"),"0"),intBOERW,TB_DUTPR);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_MATTP"),""),intBOERW,TB_MATTP);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_PKGTP"),""),intBOERW,TB_PKGTP);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_CHLQT"),"0"),intBOERW,TB_CHLQT);
				tblBOEVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_NETQT"),"0"),intBOERW,TB_NETQT);
				tblBOEVL.setValueAt("O",intBOERW,TB_OLDFL);
				L_TBEQT += Double.parseDouble(strBOEQT);
				intBOERW++;
			}
			intBECNT = intBOERW;
			dblBALQT = Double.parseDouble(strCNSQT)-L_TBEQT;
			txtBALQT.setText(setNumberFormat(dblBALQT,3));
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			lblCONNO.setText("Bill of Entry Detail for " + strCNSNO);
			strCNSNO = txtCNSNO.getText().trim();
			
			return !L_FIRST;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeGETREC");
			return false;
		}
	}
	// Method to get the Vendor Name
	private String getVENNM(String P_strVENCD)
	{
		String L_strVENNM = "";
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "Select PT_PRTNM from CO_PTMST";
			M_strSQLQRY += " where PT_PRTTP = 'S' and isnull(PT_STSFL,'') <>'X'";
			M_strSQLQRY += " and PT_PRTCD = '" + P_strVENCD + "'";
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					L_strVENNM = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"").trim();
					
				}
				L_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getVENNM");
		}
		return L_strVENNM;
	}
	
	// Method to get the Material Description
	private String getMATDS(String P_strMATCD)
	{
		String L_strMATDS = "";
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "Select TK_MATDS from MM_TKMST";
			M_strSQLQRY += " where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TK_MATCD = '" + P_strMATCD + "'";
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					L_strMATDS = nvlSTRVL(L_rstRSSET.getString("TK_MATDS"),"").trim();
				}	
				L_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getMATDS");
		}
		return L_strMATDS;
	}
	// Method to validate the Bill of Entry Quantity
	private boolean vldBOEQT()
	{
		double L_dblCNSQT = 0;
		strCNSQT = txtCNSQT.getText().trim();
		try
		{
			for(int i=0;i<intBOECT;i++)
			{
				if(tblBOEVL.getValueAt(i,TB_BOENO).toString().trim().length() >0)
				{
					L_dblCNSQT += Double.parseDouble(tblBOEVL.getValueAt(i,TB_BOEQT).toString().trim());
				}
			}
			if(Double.parseDouble(strCNSQT) < L_dblCNSQT)
			{
				return false;
			}	
		}
		catch(Exception e)
		{
			setMSG(e,"vldBOEQT");
            return false;
		}
		return true;
	}
	// Method to get the Details from P.O.Master
	private void exeGETPO(String P_strSTRTP,String P_strPORNO,String P_strMATCD)
	{
		try
		{
			java.sql.Date L_datTEMP;
			M_strSQLQRY = "Select PO_PORDT,POT_PORQT,PO_VENCD from MM_POMST,MM_POTRN";
			M_strSQLQRY += " where PO_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PO_STRTP = '" + P_strSTRTP + "'";
			M_strSQLQRY += " and POT_STRTP = '" + P_strSTRTP + "'";
			M_strSQLQRY += " and PO_PORNO = '" + P_strPORNO + "'";
			M_strSQLQRY += "  and PO_CMPCD = POT_CMPCD and POT_PORNO = PO_PORNO ";
			M_strSQLQRY += " and POT_MATCD = '" + P_strMATCD + "'";
			M_strSQLQRY += " and isnull(PO_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					
					L_datTEMP =  M_rstRSSET.getDate("PO_PORDT");
					if(L_datTEMP !=null)
					{
						txtCNSDT.setText(M_fmtLCDAT.format(L_datTEMP));
					}
					//txtCNSDT.setText(cc_dattm.setDATE("DMY",M_rstRSSET.getDate("PO_PORDT")));
					txtCNSQT.setText(M_rstRSSET.getString("POT_PORQT"));
					txtVENCD.setText(M_rstRSSET.getString("PO_VENCD"));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeGETPO");
		}
	}
	// Method to insert the record into MM_POMST & MM_POTRN
	/*private void exeADDPO()
	{
		try
		{
			String L_strPORTP = "01",L_strVENTP = "S",L_strCURCD = "01",L_strSTSFL = "";
			int L_intPORVL = 0;
			String  L_strINDNO = "",L_strDPTCD = "";
			int L_intPORRT = 0,L_intPERRT = 0,L_intACPQT = 0,L_intDELCT = 0;
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				// Inserting into MM_POMST
				M_strSQLQRY = "Insert into MM_POMST(PO_CMPCD,PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD,PO_UOMCD,PO_PORDT,";
				M_strSQLQRY += "PO_VENTP,PO_VENCD,PO_CURCD,PO_PORVL,PO_TRNFL,PO_STSFL,";
				M_strSQLQRY += "PO_LUSBY,PO_LUPDT) values (";
				M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
				M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
				M_strSQLQRY += "'" + L_strPORTP + "',";
				M_strSQLQRY += "'" + txtCNSNO.getText().trim() + "',";
				M_strSQLQRY += "'DUMMY',"; // Indent No.
				M_strSQLQRY += "'FG',"; // Mat. code 
				M_strSQLQRY += "'',"; // Mat. code 
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCNSDT.getText().trim())) + "',";
				M_strSQLQRY += "'" + L_strVENTP + "',";
				M_strSQLQRY += "'" + txtVENCD.getText().trim() + "',";
				M_strSQLQRY += "'" + L_strCURCD + "',";
				M_strSQLQRY += L_intPORVL + ",";
				M_strSQLQRY += getUSGDTL("PO",'I',"") + ")";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				// Inserting into MM_POTRN
				M_strSQLQRY = "Insert into MM_POTRN(POT_CMPCD,POT_STRTP,POT_PORTP,POT_PORNO,POT_MATCD,";
				M_strSQLQRY += "POT_INDNO,POT_DPTCD,POT_UOMCD,POT_PORRT,POT_PERRT,POT_PORQT,";
				M_strSQLQRY += "POT_ACPQT,POT_DELCT,POT_TRNFL,POT_STSFL,POT_LUSBY,POT_LUPDT) values (";
				M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
				M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
				M_strSQLQRY += "'" + L_strPORTP + "',";
				M_strSQLQRY += "'" + txtCNSNO.getText().trim() + "',";
				M_strSQLQRY += "'" + txtMATCD.getText().trim() + "',";
				M_strSQLQRY += "'" + L_strINDNO + "',";
				M_strSQLQRY += "'" + L_strDPTCD + "',";
				M_strSQLQRY += "'" + txtUOMCD.getText().trim() + "',";
				M_strSQLQRY += L_intPORRT + ",";
				M_strSQLQRY += L_intPERRT + ",";
				M_strSQLQRY += strORDQT + ",";
				M_strSQLQRY += L_intACPQT + ",";
				M_strSQLQRY += L_intDELCT + ",";
				M_strSQLQRY += getUSGDTL("POT",'I',"") + ")";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
			//else if(cl_dat.M_FLGOPT == 'M')
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				// Updating MM_POMST
				M_strSQLQRY = "Update MM_POMST set ";
				M_strSQLQRY += "PO_PORDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(strPORDT)) + "',";
				M_strSQLQRY += "PO_VENCD = '" + strVENCD + "',";
				M_strSQLQRY += getUSGDTL("PO",'U',"");
				M_strSQLQRY += " where PO_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += " and PO_PORTP = '01'";
				M_strSQLQRY += " and PO_PORNO = '" +  txtCNSNO.getText() + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				// Updating MM_POTRN
				M_strSQLQRY = "Update MM_POTRN set ";
				M_strSQLQRY += "POT_PORQT = " + strORDQT + ",";
				M_strSQLQRY += getUSGDTL("POT",'U',"");
				M_strSQLQRY += " where POT_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += " and POT_PORTP = '01'";
				M_strSQLQRY += " and POT_PORNO = '" +  txtCNSNO.getText() + "'";
				M_strSQLQRY += " and POT_MATCD = '" +  txtMATCD.getText() + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeADDPO");
		}
	}*/
	// Method to add the data in MM_TEBET table
	private boolean exeADDREC(String P_strPORNO)
	{
		try
		{
			boolean L_flgCNSVL = false;			// Flag to indicate presence of Consignee Detail
			boolean L_flgBOEVL = false;			// Flag to indicate presence of BOE Detail
		
			cl_dat.M_flgLCUPD_pbst = true;
			L_flgBOEVL = false;
			L_flgCNSVL = true;
			lblCONNO.setText("Bill of Entry Detail for " + txtCNSNO.getText());
			if(strSTSFL.equals("C"))
			{
				setMSG("Consignment is marked as Completed,further modifications are not allowed..",'E');
				return false;	
			}
			else
			{
				setMSG("",'N');
			}
			if(chkCONFL.isSelected())
				strSTSFL = "C";
			else
				strSTSFL = "";
			//exeADDPO();
			for(int j=0;j<intBOECT;j++)
			{
				if(tblBOEVL.getValueAt(j,TB_CHKFL).toString().trim().equals("true"))
				{
					L_flgBOEVL = true;
					strBOENO = tblBOEVL.getValueAt(j,TB_BOENO).toString().trim();
					strBOEDT = tblBOEVL.getValueAt(j,TB_BOEDT).toString().trim();
					strLOCCD = tblBOEVL.getValueAt(j,TB_LOCCD).toString().trim();
					strBOEQT = nvlSTRVL(tblBOEVL.getValueAt(j,TB_BOEQT).toString(),"0").trim();
					strACCVL = tblBOEVL.getValueAt(j,TB_ACCVL).toString().trim();
					strDUTVL = tblBOEVL.getValueAt(j,TB_DUTVL).toString().trim();
					strDUTPR = tblBOEVL.getValueAt(j,TB_DUTPR).toString().trim();
					//strBOETP = tblBOEVL.getValueAt(j,TB_BOETP).toString().trim();
					strMATTP = tblBOEVL.getValueAt(j,TB_MATTP).toString().trim();
//					strMATTP = tblBOEVL.getValueAt(j,TB_MATTP).toString().trim();
					strCHLQT = nvlSTRVL(tblBOEVL.getValueAt(j,TB_CHLQT).toString(),"0").trim();
					strNETQT = nvlSTRVL(tblBOEVL.getValueAt(j,TB_NETQT).toString(),"0").trim();
					strOLDFL = tblBOEVL.getValueAt(j,TB_OLDFL).toString().trim();
			
				strOLDFL = tblBOEVL.getValueAt(j,TB_OLDFL).toString().trim();
				if(!strOLDFL.equals("O"))
				{
					M_strSQLQRY = "Insert into MM_BETRN (BE_CMPCD,BE_STRTP,BE_PORNO,BE_MATCD,";
					M_strSQLQRY += "BE_UOMCD,BE_CONNO,BE_CONDT,BE_CONQT,BE_CONDS,";
					M_strSQLQRY += "BE_BOENO,BE_BOEDT,BE_LOCCD,BE_BOEQT,BE_ACCVL,";
					M_strSQLQRY += "BE_DUTVL,BE_DUTPR,BE_CVDVL,BE_ACVVL,BE_EDCVL,BE_EHCVL,BE_BOETP,BE_PKGTP,BE_MATTP,BE_CHLQT,BE_NETQT";
					M_strSQLQRY += ",BE_CSDCD,BE_CSBCD,BE_TRNFL,BE_STSFL,BE_LUSBY,BE_LUPDT) values (";
					M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
					M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
					M_strSQLQRY += "'" + P_strPORNO + "',";
					M_strSQLQRY += "'" + tblBOEVL.getValueAt(j,TB_MATCD).toString().trim()+ "',";
					M_strSQLQRY += "'" + tblBOEVL.getValueAt(j,TB_UOMCD).toString().trim()+ "',";
//					M_strSQLQRY += "'" + txtUOMCD.getText() + "',";
					M_strSQLQRY += "'" + txtCNSNO.getText() + "','";
					M_strSQLQRY += M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCNSDT.getText())) + "',";
					M_strSQLQRY += txtCNSQT.getText() + ",";
					M_strSQLQRY += "'" + txtCNSDS.getText() + "',";
					M_strSQLQRY += "'" + strBOENO + "','";
					M_strSQLQRY += M_fmtDBDAT.format(M_fmtLCDAT.parse(strBOEDT)) + "',";
					M_strSQLQRY += "'" + strLOCCD + "',";
					M_strSQLQRY += strBOEQT + ",";
					M_strSQLQRY += strACCVL + ",";
					M_strSQLQRY += strDUTVL + ",";
					M_strSQLQRY += strDUTPR + ",";
					M_strSQLQRY += tblBOEVL.getValueAt(j,TB_CVDVL).toString().trim()+ ",";
					M_strSQLQRY += tblBOEVL.getValueAt(j,TB_ACVVL).toString().trim()+ ",";
					M_strSQLQRY += tblBOEVL.getValueAt(j,TB_EDCVL).toString().trim()+ ",";
					M_strSQLQRY += tblBOEVL.getValueAt(j,TB_EHCVL).toString().trim()+ ",";

					M_strSQLQRY += "'" + tblBOEVL.getValueAt(j,TB_BOETP).toString().trim() + "',";
					M_strSQLQRY += "'" + tblBOEVL.getValueAt(j,TB_PKGTP).toString().trim()+ "',";
					M_strSQLQRY += "'" + strMATTP + "',";
					M_strSQLQRY += strCHLQT + ",";
					M_strSQLQRY += strNETQT + ",";
					M_strSQLQRY += "'"+tblBOEVL.getValueAt(j,TB_CSDCD).toString().trim() +"',";
					M_strSQLQRY += "'"+tblBOEVL.getValueAt(j,TB_CSBCD).toString().trim() +"',";
					M_strSQLQRY += getUSGDTL("BE",'I',strSTSFL)+")";
				}
				else if ((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))&& strOLDFL.equals("O"))
				{
					M_strSQLQRY = "Update MM_BETRN set ";
					M_strSQLQRY += " BE_CONDT ='" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCNSDT.getText())) + "',";
					M_strSQLQRY += "BE_CONQT = " + nvlSTRVL(strCNSQT,"0") + ",";
					M_strSQLQRY += "BE_CONDS = '" + strCNSDS + "',";
					M_strSQLQRY += " BE_BOEDT ='" + M_fmtDBDAT.format(M_fmtLCDAT.parse(strBOEDT)) + "',";
					M_strSQLQRY += "BE_LOCCD = '" + strLOCCD + "',";
					M_strSQLQRY += "BE_BOEQT = " + nvlSTRVL(strBOEQT,"0") + ",";
					M_strSQLQRY += "BE_ACCVL = " + strACCVL + ",";
					M_strSQLQRY += "BE_DUTVL = " + strDUTVL + ",";
					M_strSQLQRY += "BE_DUTPR = " + strDUTPR + ",";
					M_strSQLQRY += "BE_CVDVL = " + tblBOEVL.getValueAt(j,TB_CVDVL).toString().trim() + ",";
					M_strSQLQRY += "BE_ACVVL = " + tblBOEVL.getValueAt(j,TB_ACVVL).toString().trim() + ",";
					M_strSQLQRY += "BE_EDCVL = " + tblBOEVL.getValueAt(j,TB_ACVVL).toString().trim() + ",";
					M_strSQLQRY += "BE_EHCVL = " + tblBOEVL.getValueAt(j,TB_ACVVL).toString().trim() + ",";
					M_strSQLQRY += "BE_BOETP = '" + tblBOEVL.getValueAt(j,TB_BOETP).toString().trim() + "',";
					M_strSQLQRY += "BE_MATTP = '" + strMATTP + "',";
//					M_strSQLQRY += "BE_MATTP = '" + strMATTP + "',";
					M_strSQLQRY += "BE_CHLQT = " + nvlSTRVL(strCHLQT,"0") + ",";
					M_strSQLQRY += "BE_NETQT = " + nvlSTRVL(strNETQT,"0") + ",";
					M_strSQLQRY += getUSGDTL("BE",'U',strSTSFL);
					M_strSQLQRY += " where BE_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and BE_PORNO = '" +  P_strPORNO + "'";
					M_strSQLQRY += " and BE_CONNO = '" +  txtCNSNO.getText() + "'";
					M_strSQLQRY += " and BE_BOENO = '" +  strBOENO + "'";
				}
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(!cl_dat.M_flgLCUPD_pbst)
				{
					setMSG("Record could not be saved",'E');
					return false;
				}
			}
			}
			if(!L_flgBOEVL)
			{
				tblBOEVL.requestFocus();
				tblBOEVL.setColumnSelectionInterval(1,1);
				tblBOEVL.setRowSelectionInterval(0,0);
				tblBOEVL.editCellAt(0,1);
				setMSG("Bill of Entry Details Should Exists against this Consignee",'E');
				return false;
			}
			/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "Update MM_BETRN set ";
				M_strSQLQRY += "BE_MATCD = '" + txtMATCD.getText() + "',";
				M_strSQLQRY += "BE_UOMCD = '" + txtUOMCD.getText() + "',";
				M_strSQLQRY += getUSGDTL("BE",'U',strSTSFL);
				M_strSQLQRY += " where BE_PORNO = '" +  P_strPORNO + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}*/
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Record saved successfully",'N');
					return true;
				}
				else
				{
					setMSG("Record could not be saved",'E');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeADDREC");
			return false;
			
		}
		return true;
	}
boolean vldDATA()
{
	// consignment date can not be greater than today's date validation
	if(tblBOEVL.isEditing())
	   tblBOEVL.getCellEditor().stopCellEditing();
	strPORNO = txtCNSNO.getText().trim();
	strPORDT = txtCNSDT.getText().trim();
	strORDQT = txtCNSQT.getText().trim();
//	strMATCD = txtMATCD.getText().trim();
//	strUOMCD = txtUOMCD.getText().trim();
	strVENCD = txtVENCD.getText().trim();
	
	if(txtCNSDT.getText().trim().length() ==0)
	{
		setMSG("Consignment Date can not be Empty ..",'E'); 
		return false;
	}
	else if(txtCNSNO.getText().length() == 0)
	{
		txtCNSNO.requestFocus();
		setMSG("Cons. No. can not be empty",'E');
		return false;
	}
	/*else if(txtBOETP1.getText().length() == 0)
	{
		txtBOETP1.requestFocus();
		setMSG("Enter Bill of entry Type",'E');
		return false;
	}*/
	/*else if(txtDSRCD.getText().length() == 0)
	{
		if(txtBOETP1.getText().equals("02"))
		{
			setMSG("Enter Cons. Stockist code..",'E');
			txtDSRCD.requestFocus();
			return false;
		}
	}*/

	else if(txtCNSQT.getText().length() == 0)
	{
		txtCNSQT.requestFocus();
		setMSG("Consignment Quantity can not be empty",'E');
		return false;
	}
	/*else if(txtMATCD.getText().length() != 10)
	{	
		txtMATCD.requestFocus();
		setMSG("Please enter the Material code",'E');
		return false;
	}*/
	else if(txtVENCD.getText().length() != 5)
	{	
		txtVENCD.requestFocus();
		setMSG("Please enter the Vendor code",'E');
		return false;
	}
	if(strSTSFL.equals("C"))
	{
		setMSG("Consignment is marked as Completed,further modifications are not allowed..",'E');
		return false;	
	}
	else
	{
		setMSG("",'N');
	}
	if(chkCONFL.isSelected())
		strSTSFL = "C";
	else
		strSTSFL = "";
	boolean L_flgSELRW = false;
	for(int i=0;i<intBOECT;i++)
	{
		if(tblBOEVL.getValueAt(i,TB_CHKFL).toString().trim().equals("true"))
		{
			L_flgSELRW = true;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				if(tblBOEVL.getValueAt(i,TB_BOENO).toString().length() ==0)
				{
					setMSG("Bill of Entry number can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_BOEDT).toString().length() ==0)
				{
					setMSG("Bill of Entry Date can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_BOETP).toString().length() ==0)
				{
					setMSG("Bill of Entry Type can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_MATTP).toString().length() ==0)
				{
					setMSG("Material Type can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_PKGTP).toString().length() ==0)
				{
					setMSG("PACKAGE Type can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_MATCD).toString().length() ==0)
				{
					setMSG("Material Code can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_LOCCD).toString().length() ==0)
				{
					setMSG("Location Code can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_BOEQT).toString().length() ==0)
				{
					setMSG("B/E quantity can not be blank..",'E');
					return false;
				}
				else if(Double.parseDouble(tblBOEVL.getValueAt(i,TB_BOEQT).toString())== 0)
				{
					setMSG("B/E quantity can not be zero ..",'E');
					return false;
				}
				/*else if(tblBOEVL.getValueAt(i,TB_CHLQT).toString().length() ==0)
				{
					setMSG("Challan quantity can not be blank..",'E');
					return false;
				}
				else if(Double.parseDouble(tblBOEVL.getValueAt(i,TB_CHLQT).toString())== 0)
				{
					setMSG("Challan quantity can not be zero ..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_NETQT).toString().length() ==0)
				{
					setMSG("Net quantity can not be blank..",'E');
					return false;
				}
				else if(Double.parseDouble(tblBOEVL.getValueAt(i,TB_NETQT).toString())== 0)
				{
					setMSG("Net quantity can not be zero ..",'E');
					return false;
				}*/
				else if(tblBOEVL.getValueAt(i,TB_ACCVL).toString().length() ==0)
				{
					setMSG("Accessible value can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_DUTVL).toString().length() ==0)
				{
					setMSG("Duty value can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_DUTPR).toString().length() ==0)
				{
					setMSG("Duty % can not be blank..",'E');
					return false;
				}
				else if(tblBOEVL.getValueAt(i,TB_CSDCD).toString().length() ==0)
				{
					if(tblBOEVL.getValueAt(i,TB_BOETP).toString().equals("02"))
					{
						setMSG("Enter Cons. Stockist code..",'E');
						return false;
					}
				}

			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				if(Double.parseDouble(tblBOEVL.getValueAt(i,TB_NETQT).toString()) >0)
					setMSG("GRIN is Accepted, can not delete the Bill of Entry..",'E');
			}
		}
	}
	if(!L_flgSELRW)
	{
		setMSG("Select at least one row ..",'E');
		return false;
	}
 return true;	
}
void exeSAVE()
{
	try
	{
		if(!vldDATA())
			return;
		cl_dat.M_flgLCUPD_pbst = true;
		strCNSNO = txtCNSNO.getText().trim();
			intBOERW = tblBOEVL.getSelectedRow();
			if(intBOERW < 0)
				intBOERW = 0;
				
			intBOECL = tblBOEVL.getSelectedColumn();
			if(intBOECL < 0)
				intBOECL = 0;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
			{
				String L_strBOELS ="";
				boolean L_FIRST = true;
				for(int j=0;j<intBECNT;j++)
				{
					if(tblBOEVL.getValueAt(j,TB_CHKFL).toString().trim().equals("true"))
					{
						if(L_FIRST)
						{
							L_strBOELS += "('"+ tblBOEVL.getValueAt(j,TB_BOENO).toString().trim()+"'";
							L_FIRST = false;
						}
						else
							L_strBOELS += ",'"+ tblBOEVL.getValueAt(j,TB_BOENO).toString().trim()+"'";
					}
				}
				L_strBOELS +=")";
				M_strSQLQRY = "Update MM_BETRN SET ";
				M_strSQLQRY += getUSGDTL("BE",'U',"X");
				M_strSQLQRY += " where BE_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and BE_PORNO = '" +  txtPORNO.getText().trim() + "'";
				M_strSQLQRY += " and BE_CONNO = '" +  txtCNSNO.getText().trim() + "'";
				M_strSQLQRY += " and isnull(BE_NETQT,0) = 0 and isnull(BE_CHLQT,0) =0 ";
				M_strSQLQRY += " and BE_BOENO IN " +  L_strBOELS;
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						clrCOMP();
						setMSG("Record saved successfully",'N');
					}
					else
					{
						setMSG("Record could not be saved",'E');
					}
				}
			}
			else
			{
				tblBOEVL.editCellAt(intBOERW,intBOECL);
				//strPORNO = txtCNSNO.getText().trim();
				strPORNO = txtPORNO.getText().trim();
				if(exeADDREC(strPORNO))
				{
					clrCOMP();
					txtCNSNO.requestFocus();
					//if(cl_dat.M_FLGOPT == 'A')
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						setENBL(true);
						txtCNSNO.setEnabled(true);
					}
					else
						setENBL(false);
				}
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
		}
}
class INPVF extends InputVerifier
{
	public boolean verify(JComponent input)
	{
		if(input instanceof JTextField && ((JTextField)input).getText().length() ==0)
			return true;
		/*if(input == txtMATCD)
		{
			try
			{
				if(txtMATCD.getText().length() < 10)
					return true;
				M_strSQLQRY = "select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST,MM_TKMST";
				M_strSQLQRY += " where CT_MATCD = '" + txtMATCD.getText().trim() + "'";
			    M_strSQLQRY += " and CT_MATCD = TK_MATCD";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						//txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));
						//txtUOMCD.setText(M_rstRSSET.getString("CT_UOMCD"));
						//txtVENCD.requestFocus();
						setMSG("",'N');
						M_rstRSSET.close();			
						return true;
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"vldMATCD");
				//txtMATDS.setText("");
				//txtUOMCD.setText("");
				setMSG("Invalid Material. Press F1 for help",'E');
				return false;
			}	
			//txtMATDS.setText("");
			//txtUOMCD.setText("");
			setMSG("Invalid Material. Press F1 for help",'E');
			return false;
		}*/
		if(input == txtCNSNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				M_strSQLQRY = "select count(*) from MM_BETRN where BE_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and BE_STRTP ='"+M_strSBSCD.substring(2,4) +"' AND BE_CONNO ='"+txtCNSNO.getText().trim() +"'";
				int L_RECCNT = cl_dat.getRECCNT(M_strSQLQRY);
				if(L_RECCNT >0)
				{
					setMSG("Consignment already exists, goto modification option for further entries",'E');
					return false;
				}
			}
		}
		/*else if(input == txtBOETP1)
		{
			if(!vldBOETP(txtBOETP1.getText().trim()))
			{
				setMSG("Invalid B/E Type,Press F1 to select From the list..",'E');
				return false;
			}

		}*/
		else if(input == txtVENCD)
		{
			try
			{
				if(txtVENCD.getText().length() < 5)
					return true;
				M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
				M_strSQLQRY += " where PT_PRTTP = 'S'";
				M_strSQLQRY += " and PT_PRTCD = '" + txtVENCD.getText() + "' and isnull(Pt_stsfl,'') <>'X'";
     			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));
						setMSG("",'N');
						M_rstRSSET.close();
						return true;
					}
				}
			}	
			catch(Exception L_EX)
			{
				setMSG(L_EX,"vldVENCD");
				return false;
			}
			txtVENNM.setText("");
			setMSG("Invalid Vendor. Press F1 for help",'E');
			return false;
		}
		/*else if(input == txtDSRCD)
		{
			try
			{
				if(txtDSRCD.getText().length() < 5)
					return true;
				M_strSQLQRY = "select cmT_CODDS,CMT_CHP02 from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXCST' AND ";
				M_strSQLQRY += " CMT_CODCD = '" + txtDSRCD.getText() + "' and isnull(CMt_stsfl,'') <>'X'";

     			      M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						//txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));
						setMSG("",'N');
						strCSBCD = M_rstRSSET.getString("CMT_CHP02");
						System.out.println(strCSBCD);
						M_rstRSSET.close();
						return true;
					}
				}
			}	
			catch(Exception L_EX)
			{
				setMSG(L_EX,"vldDSRCD");
				return false;
			}
			//txtVENNM.setText("");
			setMSG("Invalid Consignment Stockist. Press F1 for help",'E');
			return false;
		}*/
		else if(input == txtCNSDT)
		{
			try
			{
				if(M_fmtLCDAT.parse(txtCNSDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{							
					setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
					//flgVLDDT = false;
					return false;
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
			}
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
			if(P_intCOLID==TB_BOETP)
			{
				strTEMP = tblBOEVL.getValueAt(P_intROWID,TB_BOETP).toString();
				if(strTEMP.length() == 0)
					return true;
				if(!vldBOETP(strTEMP))
				{
					setMSG("Invalid B/E Type,Press F1 to select From the list..",'E');
					return false;
				}
			}
 			else if(P_intCOLID == TB_CSDCD)
			{
			try
			{
				strTEMP = tblBOEVL.getValueAt(P_intROWID,TB_CSDCD).toString();
				if(strTEMP.length() == 0)
					return true;
				if((strTEMP.length() < 5)&& tblBOEVL.getValueAt(P_intROWID,TB_BOETP).toString().equals("02"))
				{
					setMSG("Enter Consignment stockist code..",'E');
					return false;
				}
				M_strSQLQRY = "select cmT_CODDS,CMT_CHP02 from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXCST' AND ";
				M_strSQLQRY += " CMT_CODCD = '" + strTEMP + "' and isnull(CMt_stsfl,'') <>'X'";

     			      M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						//txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));
						setMSG("",'N');
						tblBOEVL.setValueAt(M_rstRSSET.getString("CMT_CHP02"),P_intROWID,TB_CSBCD);
						//System.out.println(strCSBCD);
						M_rstRSSET.close();
						return true;
					}
				}
			}	
			catch(Exception L_EX)
			{
				setMSG(L_EX,"vldDSRCD");
				return false;
			}
			//txtVENNM.setText("");
			setMSG("Invalid Consignment Stockist. Press F1 for help",'E');
			return false;
		}

			else if(P_intCOLID==TB_MATTP)
			{
				strTEMP = tblBOEVL.getValueAt(P_intROWID,TB_MATTP).toString();
				if(strTEMP.length() == 0)
					return true;
				if(!vldMATTP(strTEMP))
				{
					setMSG("Invalid Material Type,Press F1 to select From the list..",'E');
					return false;
				}
			}
			else if(P_intCOLID==TB_PKGTP)
			{
				strTEMP = tblBOEVL.getValueAt(P_intROWID,TB_PKGTP).toString();
				if(strTEMP.length() == 0)
					return true;
				if(!vldPKGTP(strTEMP))
				{
					setMSG("Invalid Package Type,Press F1 to select From the list..",'E');
					return false;
				}
			}

			else if(P_intCOLID==TB_LOCCD)
			{
				strTEMP = tblBOEVL.getValueAt(P_intROWID,TB_LOCCD).toString();
				if(strTEMP.length() == 0)
					return true;
				if(!vldLOCCD(strTEMP))
				{
					setMSG("Invalid Location,Press F1 to select From the list..",'E');
					return false;
				}
			}
			else if(P_intCOLID==TB_BOEDT)
			{
				strTEMP = tblBOEVL.getValueAt(P_intROWID,TB_BOEDT).toString();
				if(strTEMP.length() == 0)
					return true;
				if(M_fmtLCDAT.parse(strTEMP).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{							
					setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
					//flgVLDDT = false;
					return false;
				}
				
			}
			else if(P_intCOLID==TB_BOEQT)
			{
				strTEMP = tblBOEVL.getValueAt(P_intROWID,TB_BOEQT).toString();
				if(strTEMP.length() == 0)
					return true;
				if(!vldBOEQT())
				{
					setMSG("B/E quantity can not be greater than consignment quantity..",'E');
					return false;
				}
				
			}
		/*if(input == txtMATCD)
		{
			try
			{
				if(txtMATCD.getText().length() < 10)
					return true;
				M_strSQLQRY = "select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST,MM_TKMST";
				M_strSQLQRY += " where CT_MATCD = '" + txtMATCD.getText().trim() + "'";
			    M_strSQLQRY += " and CT_MATCD = TK_MATCD";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						//txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));
						//txtUOMCD.setText(M_rstRSSET.getString("CT_UOMCD"));
						//txtVENCD.requestFocus();
						setMSG("",'N');
						M_rstRSSET.close();			
						return true;
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"vldMATCD");
				//txtMATDS.setText("");
				//txtUOMCD.setText("");
				setMSG("Invalid Material. Press F1 for help",'E');
				return false;
			}	
			//txtMATDS.setText("");
			//txtUOMCD.setText("");
			setMSG("Invalid Material. Press F1 for help",'E');
			return false;
		}*/
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"table verifier");
			return false;
		}
		return true;
	}
}
}
		