/*
System Name   : Material Management System
Program Desc. : GRIN Entry
Author        : A. A. Patil
Date          : 15/04/2004
Version       : MMS 2.0

Last Modified	: 03/09/2004 
Documented On	: 

*/
// GRIN Updation
import java.sql.ResultSet;import java.sql.SQLException;import java.sql.PreparedStatement;
import javax.swing.*;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;import java.awt.event.KeyEvent;import java.awt.event.ItemEvent;
import javax.swing.event.*;
import java.util.Vector;import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;

class mm_tegrn extends cl_pbase implements ChangeListener{
	private PreparedStatement pstmBETRN;
	private JComboBox cmbMATTP,cmbGRNTP;
	private JTextField txtEDITR;			// Table Cell Editor
	private JTextField txtGRNNO,txtGRNDT,txtBOENO,txtCNSNO,txtPORNO,txtVENCD,txtVENNM,txtTRNCD;
    private JTextField txtTRNNM,txtGINNO,txtLRYNO,txtCHLNO,txtCHLDT;
	private JTextField txtMATCD,txtCHLQT,txtRECQT,txtACPQT,txtEXCCT,txtREJCD,txtINDNO,txtAMDNO;
	private JCheckBox chkENBFL;
	private JButton btnACPT,btnPRNT;
	private String strGRNNO,strGRNDT,strBOENO,strCNSNO,strPORNO,strINDNO;
	private String strVENCD,strVENNM,strTRNCD,strTRNNM,strSBSCD,strSTRTP,strGRNTP,strMATCD,strMATTP,strBEMAT,strBATNO;
	private String strUOMCD,strCHLNO,strCHLDT,strCHLQT,strRECQT,strREJQT,strACPQT,strPREBY,strPREDT,strAMDNO ="00";
	private String strLRYNO,strGINNO,LM_LUSBY,LM_LUPDT,strEXCCT,strMODFL,strGINTP,strDELFL,strQLTFL,strREJCD,strLOCCD,strTEMP ="";
	private int intTBLRW,intTBLCL,intROWCT,intOLDRW,intINDRW=0;
	private TBLINPVF objTBLVRF;
	private Hashtable<String,String> hstITMDT = new Hashtable<String,String>();
	private Hashtable<String,String> hstINDNO = new Hashtable<String,String>();
	private INPVF objINPVR = new INPVF();	
    private cl_JTable tblGINVL;
    private Hashtable<String,String> hstMATCD = new Hashtable<String,String>();
	
	final int TBL_CHKFL = 0;
	final int TBL_MATCD = 1;
	final int TBL_MATDS = 2;
	final int TBL_UOMCD = 3;
	final int TBL_BATNO = 4;
	final int TBL_EXCCT = 5;
	final int TBL_CHLQT = 6;
	final int TBL_RECQT = 7;
	final int TBL_ACPQT = 8;
	final int TBL_REJQT = 9;
	final int TBL_LOCCD = 10;
	final int TBL_MODFL = 11;
	final int TBL_INDNO = 12;		
	final int TBL_REJCD = 13;
	final int TBL_DELFL = 14;
	final int TBL_QLTFL = 15;

	final String strPORGR_fn ="01";					// PO GRIN
	final String strCSHGR_fn ="02";					// CAsh GRIN
	final String strREPGR_fn ="03";					// replacement GRIN
	final String strJBWGR_fn ="05";					// Job Work GRIN	
    final String strTNSTR_fn = "04";                // Tank Farm Store type
    final String strEGSTR_fn = "01";                // Tank Farm Store type
	final String strTNLTP_fn = "01";		// Document Type - Tanker
	final String strTRNFL_fn = "0";		// Transfer flag
    final String strEXBTP_fn = "01";           // Exbonded Material
    final String strBNDTP_fn = "02";           // Bonded Material
    final String strADVTP_fn = "03";           // Adv.Lic.Material

    final String strEXBGR_fn = "47";           // EX-Bonded GRIN
    final String strADVGR_fn = "48";           // Adv.Lic.GRIN
	final String strDFLBT_fn ="COMMON";			// default bach no  
	final String strGRNTR_fn ="3";				// GRIN Transaction is 3 	
	
	final String strPKGMT_fn ="3"; 
	final String strRAWMT_fn ="2";
	final String strSTRSP_fn ="1";

	///*** ADDED BY AAP FOR CYLINDERS
	/** Final String array to contain group/subgroup codes for cylinders	 */
	private  String [] staCYLCD;/** Hashtable<String,String> to collect/maintain details of cylinders material code wise. KEY : Material Code VALUE : Vector<String> in format "CYL. NO. || RENT FREE DAYS*/
	private Hashtable<Object,Object> hstCYLDT;/** Table for cylinder details*/
	private cl_JTable tblCYLDT;/** Panel in tabbed pane to contain cylinder details*/
	private JPanel pnlCYLDT;/** Tabbed pane for GRIN details and cylinder details*/
	private JTabbedPane tbpMAIN;/** Panel to contain GRIN base details in tabbed pane*/
	private JPanel pnlGINVL;/**Vector<String> for material codes having cylinders. Used for help module */
	private Vector<String[]> vtrMATCD;/** Vector<String> for old data of cylinders in modification*/
	private Vector<String> vtrOLDCL;/** Hashtable<String,String> for material descriptions to be displayed in help on tblCYLDT*/
	private Hashtable<String,String> hstMATDS;
	private Hashtable<String,String> hstRFDAY;/** Hashtable<String,String> for storing recent Rent free days from cylinder table                   */
	///***
	private ResultSet L_rstRSSET;
	
	mm_tegrn()
	{
		super(2);
		try
		{	
			setMatrix(20,8);
			add(new JLabel("GRIN Type"),1,1,1,1,this,'L');
			add(cmbGRNTP = new JComboBox(),1,2,1,2,this,'L');
			
			add(new JLabel("Material Type"),1,4,1,1,this,'L');
			add(cmbMATTP = new JComboBox(),1,5,1,1.5,this,'L');

			add(new JLabel("Amd. No."),1,6,1,0.5,this,'R');
			add(txtAMDNO = new TxtLimit(2),1,7,1,0.5,this,'L');
			add(btnPRNT = new JButton("Print"),1,8,1,1,this,'L');
		//	add(new JLabel("Amd. No."),1,7,1,1,this,'L');
		//	add(txtAMDNO = new TxtLimit(2),1,8,1,1,this,'L');

			add(new JLabel("GRIN No."),2,1,1,1,this,'L');
			add(txtGRNNO = new TxtLimit(8),2,2,1,1,this,'L');
			
			add(new JLabel("GRIN Date"),2,3,1,1,this,'L');
			add(txtGRNDT = new TxtDate(),2,4,1,1,this,'L');
			add(new JLabel("B/E No."),2,5,1,1,this,'L');
			add(txtBOENO = new TxtLimit(15),2,6,1,1,this,'L');
		
			add(new JLabel("Consign No."),2,7,1,1,this,'L');
			add(txtCNSNO = new TxtLimit(8),2,8,1,1,this,'L');
			txtCNSNO.setEnabled(false);
		
			add(new JLabel("Supplier"),4,1,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),4,2,1,1,this,'L');
			add(txtVENNM = new TxtLimit(40),4,3,1,4,this,'L');
			txtVENNM.setEnabled(false);
			add(new JLabel("P.O. No."),4,7,1,1,this,'L');
			add(txtPORNO = new TxtLimit(8),4,8,1,1,this,'L');
			
			add(new JLabel("Gate In No."),3,1,1,1,this,'L');
			add(txtGINNO = new TxtLimit(8),3,2,1,1,this,'L');
		
			add(new JLabel("Challan No."),3,3,1,1,this,'L');
			add(txtCHLNO = new TxtLimit(10),3,4,1,1,this,'L');
			add(new JLabel("Challan Date"),3,5,1,1,this,'L');
			add(txtCHLDT = new TxtDate(),3,6,1,1,this,'L');
		
			add(new JLabel("Transporter"),5,1,1,1,this,'L');
			add(txtTRNCD = new TxtLimit(5),5,2,1,1,this,'L');
			add(txtTRNNM = new TxtLimit(40),5,3,1,4,this,'L');
			add(new JLabel("Lorry No."),5,7,1,1,this,'L');
			add(txtLRYNO = new TxtLimit(15),5,8,1,1,this,'L');
	
			add(new JLabel("Indent No."),6,1,1,1,this,'L');
			add(txtINDNO = new TxtLimit(8),6,2,1,1,this,'L');
			txtTRNCD.setEnabled(false);
			txtTRNNM.setEnabled(false);
			String L_strDATA;
			hstMATDS=new Hashtable<String,String>(10,0.75f);
		//Finding out no. of groups for Material in Cylinder
			M_rstRSSET=cl_dat.exeSQLQRY("select count(*) from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXCLG' and isnull(cmt_stsfl,'') <>'X'");
			if(M_rstRSSET.next())
				if(M_rstRSSET.getInt(1)>0)
					staCYLCD=new String[M_rstRSSET.getInt(1)];
			M_strSQLQRY = "Select CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP in ( 'MMXXMTP', 'MMXXCLG') and isnull(cmt_stsfl,'') <>'X'";
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
			String[] L_COLHD = {"","Material","Description","UOM","Batch/Lot","Cat.","Chl.Qty.","Recvd.Qty.","Acp.Qty.","Rej.Qty.","Location","Mod","Ind .No.","Rej.Cd","D.Tg","Q.Tg"};
			int[] L_COLSZ = {18,75,195,35,50,30,65,65,65,60,55,25,60,40,30,30};
			intROWCT = 150;
			///*** ADDED BY AAP FOR CYLINDER ENTRY
			pnlGINVL=new JPanel(null);
	        tblGINVL = crtTBLPNL1(pnlGINVL,L_COLHD,intROWCT,1,1,10.2,8,L_COLSZ,new int[]{0,TBL_MODFL});
			tbpMAIN=new JTabbedPane();
			tbpMAIN.addChangeListener(this);
			tbpMAIN.add("GRIN Details",pnlGINVL);
			pnlCYLDT=new JPanel(null);
			tblCYLDT = crtTBLPNL1(pnlCYLDT,new String[]{"FL","Mat. Code","Cylinder No.","Rent Free Days"},50,1,1,10.2,8,new int[]{20,100,150,100},new int[]{0});
			tblCYLDT.addFocusListener(this);
			tblCYLDT.setInputVerifier(new TBLINPVF());
			tbpMAIN.add("Cylinder Details",pnlCYLDT);
			add(tbpMAIN,7,1,12.3,8.1,this,'L');
			///***
			
			txtEDITR = (JTextField)tblGINVL.getCellEditor(1,1).getTableCellEditorComponent(tblGINVL,"",false,1,1);
			tblGINVL.clrTABLE();
			cl_dat.M_flgHELPFL_pbst = false;
			strGINTP = "";
			tblGINVL.setCellEditor(TBL_MATCD,txtMATCD= new TxtLimit(10));
			tblGINVL.setCellEditor(TBL_CHLQT,txtCHLQT = new TxtNumLimit(10.3));
			tblGINVL.setCellEditor(TBL_RECQT,txtRECQT = new TxtNumLimit(10.3));
			tblGINVL.setCellEditor(TBL_EXCCT,txtEXCCT = new TxtLimit(2));
			txtMATCD.addFocusListener(this);
			txtCHLQT.addFocusListener(this);
			txtRECQT.addFocusListener(this);
			txtEXCCT.addFocusListener(this);
			txtMATCD.addKeyListener(this);
			txtCHLQT.addKeyListener(this);
			txtRECQT.addKeyListener(this);
			txtEXCCT.addKeyListener(this);
			vtrMATCD = new Vector<String[]>();
			setENBL(false);
			cmbGRNTP.setEnabled(false);
			cmbMATTP.setEnabled(false);
			txtGRNNO.setEnabled(false);
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			pstmBETRN = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Update MM_BETRN set BE_CHLQT = BE_CHLQT + ?," +
				"BE_NETQT = BE_NETQT + ?,BE_TRNFL = ?,BE_LUSBY = ?,BE_LUPDT = ?" + 
				" where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_PORNO = ? and BE_CONNO = ? and BE_BOENO = ?"
				);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtGINNO.setInputVerifier(objINPVR);
			txtINDNO.setInputVerifier(objINPVR);
			//txtGRNDT.setInputVerifier(objINPVR);
			txtCHLDT.setInputVerifier(objINPVR);
			txtPORNO.setInputVerifier(objINPVR);
			objTBLVRF = new TBLINPVF();
			tblGINVL.setInputVerifier(objTBLVRF);		
			tblCYLDT.cmpEDITR[3].addFocusListener(this);
			//RETRIEVING DETAILS OF  RENT FREE DAYS & PUTTING IN hstRFDAY
			hstRFDAY=new Hashtable<String,String>(10,0.75f);
			M_strSQLQRY="Select CLT_MATCD, isnull(CLT_RFDAY,0) CLT_RFDAY from MM_CLTRN A where "
				+"CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_MATCD in (Select clt_matcd from MM_CLTRN B where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STSFL in ('7','8')) "
				+"and CLT_MGPDT=(Select  max(isnull(CLT_MGPDT,'01/01/1995')) from MM_CLTRN C where C.CLT_MATCD=A.CLT_MATCD and C.CLT_CMPCD=A.CLT_CMPCD) ";

			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					hstRFDAY.put(M_rstRSSET.getString("CLT_MATCD"),M_rstRSSET.getString("CLT_RFDAY"));
				}
				M_rstRSSET.close();
			}

		}
		catch(Exception E)
		{
			setMSG(E,"constructor");
		}	
	}	
	void clrCOMP()
	{
		super.clrCOMP();
		hstCYLDT=new Hashtable<Object,Object>(5,0.75f);
	}

	void setENBL(boolean P_FLAG)
	{
		super.setENBL(P_FLAG);
		if(tblGINVL.isEditing())
			tblGINVL.getCellEditor().stopCellEditing();
		if(tblCYLDT.isEditing())
			tblCYLDT.getCellEditor().stopCellEditing();
		btnPRNT.setEnabled(false);
		if(M_strSBSCD !=null && cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
			if(M_strSBSCD.substring(2,4).equals(strTNSTR_fn))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					txtBOENO.setEnabled(P_FLAG);
					txtCNSNO.setEnabled(P_FLAG);
				}
				else
				{
					txtBOENO.setEnabled(false);
					txtCNSNO.setEnabled(false);
				}
			}
			else
			{
				txtBOENO.setEnabled(false);
				txtCNSNO.setEnabled(false);
			}
        cmbGRNTP.setEnabled(!P_FLAG);
		txtGRNNO.setEnabled(!P_FLAG);
		cmbMATTP.setEnabled(!P_FLAG);
		//txtGRNDT.setEnabled(P_FLAG);
		txtPORNO.setEnabled(P_FLAG);
		txtGINNO.setEnabled(P_FLAG);
		txtLRYNO.setEnabled(P_FLAG);
		txtCHLNO.setEnabled(P_FLAG);
		txtCHLDT.setEnabled(P_FLAG);
		txtVENCD.setEnabled(P_FLAG);
        txtVENNM.setEnabled(false);
        txtTRNNM.setEnabled(false);
		txtINDNO.setEnabled(false);
		txtGRNDT.setEnabled(false);
		txtEDITR.setEnabled(P_FLAG);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
		{
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				chkENBFL = (JCheckBox)tblGINVL.getCellEditor(0,TBL_CHKFL).getTableCellEditorComponent(tblGINVL,"",true,0,0);
				chkENBFL.setEnabled(true);
				tblCYLDT.cmpEDITR[0].setEnabled(true);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				txtPORNO.setEnabled(false);
				txtINDNO.setEnabled(true);
				tblCYLDT.cmpEDITR[1].setEnabled(false);tblCYLDT.cmpEDITR[2].setEnabled(false);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				btnPRNT.setEnabled(true);
			//else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			//	txtGRNDT.setEnabled(true);
		}
		((JTextField)tblGINVL.cmpEDITR[TBL_ACPQT]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_REJQT]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_REJCD]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_DELFL]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_QLTFL]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_UOMCD]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_MATDS]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_LOCCD]).setEnabled(false);
		((JTextField)tblGINVL.cmpEDITR[TBL_INDNO]).setEnabled(false);
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
					cmbGRNTP.removeAllItems();
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXGRN'";
					M_strSQLQRY += " AND CMT_CHP01 like '%"+M_strSBSCD.substring(2,4) +"%'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					while(M_rstRSSET.next()){
						L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
						cmbGRNTP.addItem(L_strDATA);
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();	
					if(M_strSBSCD.substring(2,4).equals(strTNSTR_fn))
					    strGINTP = "01";
					else
					    strGINTP = "02";
				
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMTP' and isnull(cmt_stsfl,'') <>'X'";
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
	/** To collect help information for tblCYLDT in vtrMATCD	 */
	public void stateChanged(ChangeEvent L_CE)
	{
		if(L_CE.getSource() == tbpMAIN)
		{
			if(tbpMAIN.getSelectedIndex() == 1)
			{
				vtrMATCD = new Vector<String[]>(5,2);
				for(int i=0;i<tblGINVL.getRowCount();i++)
				{
					if(tblGINVL.getValueAt(i,TBL_MATCD).toString().length()>0 && tblGINVL.getValueAt(i,0).toString().equals("true"))
					{
						for(int j=0;j<staCYLCD.length;j++)
							if(tblGINVL.getValueAt(i,1).toString().substring(0,staCYLCD[j].length()).equals(staCYLCD[j]))
								vtrMATCD.addElement(new String[]{tblGINVL.getValueAt(i,1).toString(),hstMATDS.get(tblGINVL.getValueAt(i,1)).toString()});
								//System.out.println("test");
					}
				}
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
				if(txtGRNNO.getText().trim().length() ==8)
					strGRNNO = txtGRNNO.getText().trim();
				if(strGRNNO.length() ==0)
				{
					setMSG("GRIN number is not specified..",'E');
					return;
				}
                cl_dat.M_PAGENO = 0;
		        cl_dat.M_intLINNO_pbst = 0;
				mm_rpgrn objGRNRP  = new mm_rpgrn(M_strSBSCD);
				objGRNRP.getDATA(strGRNNO,strGRNNO);
				JComboBox L_cmbLOCAL = objGRNRP.getPRNLS();
				objGRNRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpgrn.doc",L_cmbLOCAL.getSelectedIndex());
					
			}
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				intINDRW =0;
				cmbGRNTP.setEnabled(true);
				cmbMATTP.setEnabled(true);
				strSBSCD = M_strSBSCD;
				strSTRTP = M_strSBSCD.substring(2,4);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					setENBL(true);
					cmbGRNTP.setEnabled(true);
					cmbMATTP.setEnabled(true);
					txtGRNDT.setText(cl_dat.M_strLOGDT_pbst);
                   if(strSTRTP.equals(strTNSTR_fn))
						txtBOENO.requestFocus();
					else
						txtGINNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst)){
                    setENBL(true);
                    cmbGRNTP.setEnabled(true);
                    txtGRNNO.setEnabled(true);
                    txtGRNNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)){
					setENBL(false);
					txtGRNNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst)){
					setENBL(false);
					txtGRNNO.requestFocus();
				}
				else{
					setMSG("Select an option",'N');
					setENBL(false);
					cmbGRNTP.setEnabled(false);
					cmbMATTP.setEnabled(false);
					txtGRNNO.setEnabled(false);
				}
				cmbGRNTP.setEnabled(true);
				cmbMATTP.setEnabled(true);
				cmbGRNTP.requestFocus();
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(L_AE.getSource().equals(cmbGRNTP))
			{
				strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
				int L_intINDEX = cmbGRNTP.getSelectedIndex();
				clrCOMP();
				cmbGRNTP.setSelectedIndex(L_intINDEX);
				txtGRNDT.setText(cl_dat.M_strLOGDT_pbst);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					//txtBOENO.requestFocus();
					txtGINNO.requestFocus();
				}
				else
					txtGRNNO.requestFocus();
					strGRNTP = cmbGRNTP.getSelectedItem().toString().substring(0,2);
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				{
					if(strGRNTP.equals("01")) // P.O
					{
						txtINDNO.setEnabled(false);
						txtPORNO.setEnabled(true);
						tblGINVL.cmpEDITR[TBL_MATCD].setEnabled(false);
					}
					else if(strGRNTP.equals("02")) // Cash
					{
					 	txtPORNO.setEnabled(false);
						txtINDNO.setEnabled(true);
						tblGINVL.cmpEDITR[TBL_MATCD].setEnabled(true);
					}
				}
				else
				{
					txtINDNO.setEnabled(false);
					tblGINVL.cmpEDITR[TBL_MATCD].setEnabled(false);
				}
				intINDRW =0;
			}
			else if(L_AE.getSource().equals(txtINDNO))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
						getINDTL(txtINDNO.getText().trim());
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"ActionPerformed");
		}
	}
	/** To disable changes in material code and cylinder no. of existing records in modification	 */
	public void focusGained(FocusEvent P_FE)
	{
	//USER FRIENDLY MESSAGES
		super.focusGained(P_FE);
		if(!M_flgERROR)
		{
			if(M_objSOURC == cmbGRNTP)
				setMSG("Select GRIN Type ..",'N');
			else if(M_objSOURC == cmbMATTP)
				setMSG("Select Material Type ..",'N');
			else if(M_objSOURC == txtGINNO)
				setMSG("Enter Gate-In-No., 'F1' for help ..",'N');
			else if(M_objSOURC == txtPORNO)
				setMSG("Enter P.O No., 'F1' for help ..",'N');
			else if(M_objSOURC == txtGRNNO)
				setMSG("Enter GRIN No., 'F1' for help ..",'N');
			else if(M_objSOURC == txtCHLNO)
				setMSG("Enter Challan No. ..",'N');
			else if(M_objSOURC == txtCHLDT)
				setMSG("Enter Challan date ..",'N');
			else if(M_objSOURC == txtLRYNO)
				setMSG("Enter Lorry No. ..",'N');
			else if(M_objSOURC == txtVENCD)
				setMSG("Enter Vendor Code, 'F1' for help ..",'N');
			else if(M_objSOURC == txtTRNCD)
				setMSG("Enter Transporter Code, 'F1' for help ..",'N');
			else if(M_objSOURC == txtINDNO)
				setMSG("Enter Indent No., 'F1' for help ..",'N');
			else if(M_objSOURC == tblCYLDT.cmpEDITR[1])
				setMSG("Enter Material Code, 'F1' for help ..",'N');
		}
		if(M_objSOURC == tblCYLDT.cmpEDITR[1])
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblCYLDT.getValueAt(tblCYLDT.getSelectedRow(),1).toString().length()>0)
					((JTextField)tblCYLDT.cmpEDITR[1]).setEditable(false);
				else
					((JTextField)tblCYLDT.cmpEDITR[1]).setEditable(true);
			}
		}
		else if(M_objSOURC == tblCYLDT.cmpEDITR[2])
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblCYLDT.getValueAt(tblCYLDT.getSelectedRow(),2).toString().length()>0)
					((JTextField)tblCYLDT.cmpEDITR[2]).setEditable(false);
				else
					((JTextField)tblCYLDT.cmpEDITR[2]).setEditable(true);
			}
		}
		else if(M_objSOURC == tblCYLDT.cmpEDITR[3])
		{
			
			if(((JTextField)tblCYLDT.cmpEDITR[3]).getText().length() == 0)
			((JTextField)tblCYLDT.cmpEDITR[3]).setText((String)hstRFDAY.get(tblCYLDT.getValueAt(tblCYLDT.getSelectedRow(),1)));
		//	System.out.println((String)hstRFDAY.get(tblCYLDT.getValueAt(tblCYLDT.getSelectedRow(),1)));
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
				if(L_KE.getSource().equals(txtGRNNO))		// GRIN No
				{
					String L_strSTSFL=" and isnull(GR_STSFL,' ') <>'X' ";
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
						L_strSTSFL=" and isnull(GR_STSFL, ' ') ='0' ";
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtGRNNO";
					strGRNTP = cmbGRNTP.getSelectedItem().toString().substring(0,2);
					strSTRTP = M_strSBSCD.substring(2,4);
					strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
					String L_ARRHDR[] = {"GRIN No.","GRIN Date","Gate-In No.","Lorry No."};
                    M_strSQLQRY = "Select distinct GR_GRNNO,GR_GRNDT,GR_GINNO,GR_LRYNO from MM_GRMST";
					M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
					M_strSQLQRY += " and GR_GRNTP = '" + strGRNTP + "'";
					M_strSQLQRY += " and GR_MATTP = '" + strMATTP + "'";
					if(txtGRNNO.getText().trim().length() >0)
						M_strSQLQRY += " and GR_GRNNO like '" + txtGRNNO.getText().trim() + "%'";
					M_strSQLQRY += L_strSTSFL+"  order by GR_GRNNO desc";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				}
				else if(L_KE.getSource().equals(txtBOENO))	// B/E No.
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtBOENO";
                    strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
                    String L_ARRHDR[] = {"B/E No","B/E Date","B/E Qty","Chalan Qty","Consign.No.","Consign Name","Supp.Code","Supplier","P.O.No.","Exicse Category"};
					M_strSQLQRY = "Select BE_BOENO,BE_BOEDT,BE_BOEQT,BE_CHLQT,BE_CONNO,BE_CONDS,PO_VENCD,PT_PRTNM,BE_PORNO,BE_MATTP";
					M_strSQLQRY += " from MM_BETRN,MM_POMST,CO_PTMST";    
					M_strSQLQRY += " where BE_PORNO = PO_PORNO and BE_CMPCD=PO_CMPCD ";
					M_strSQLQRY += " and PO_VENCD = PT_PRTCD";
					M_strSQLQRY += " and PT_PRTTP = 'S'";
					M_strSQLQRY += " and BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_BOEQT > BE_CHLQT";
                    M_strSQLQRY += " and BE_STSFL <> 'X'";
                    if(strGRNTP.equals(strEXBGR_fn))
                          M_strSQLQRY += " and BE_MATTP = '" + strEXBTP_fn + "'";        
                    else if(strGRNTP.equals(strADVGR_fn))
                          M_strSQLQRY += " and BE_MATTP = '" + strADVTP_fn + "'";        
					M_strSQLQRY += " order by BE_BOEDT desc" ;
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,10,"CT");
				}
				else if(L_KE.getSource().equals(txtPORNO))	// P.O.No.
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPORNO";
					String L_ARRHDR[] = {"P.O.No","P.O.Date","Vendor Code","Vendor"};
					
					M_strSQLQRY = "Select distinct PO_PORNO,PO_PORDT,PO_VENCD,PT_PRTNM from MM_POMST,CO_PTMST";
					M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  PO_MMSBS = '"+M_strSBSCD+"'";
					M_strSQLQRY += " and PO_STRTP = '"+strSTRTP+"' AND isnull(PO_PORTP,'') <>'03' ";
					if(txtVENCD.getText().trim().length() == 5)
					{
						M_strSQLQRY += " AND PO_VENCD = PT_PRTCD AND PT_PRTTP ='S' ";
						M_strSQLQRY += " AND PO_VENCD = '"+txtVENCD.getText().trim()+"'";
					}
					if(txtPORNO.getText().trim().length() >0)
						M_strSQLQRY += " AND PO_PORNO like '"+txtPORNO.getText().trim()+"%'";
					M_strSQLQRY += " and isnull(PO_STSFL,' ') IN('A','H')";  // Authorised
					System.out.println(M_strSQLQRY);
				 	if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
					
				}
				else if(L_KE.getSource().equals(txtINDNO))	// Indent.No.
				{
					M_strHLPFLD = "txtINDNO";
					String L_ARRHDR[] = {"Indent No."};
					M_strSQLQRY = "Select distinct IN_INDNO from MM_INMST";
                    M_strSQLQRY += " where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS = '"+M_strSBSCD+"'";
					M_strSQLQRY += " and IN_STRTP = '"+strSTRTP+"' AND isnull(IN_INDTP,'') ='01' ";
					if(txtINDNO.getText().trim().length() >0)
						M_strSQLQRY += " AND IN_INDNO like '"+txtINDNO.getText().trim()+"%'";
                    M_strSQLQRY += " and isnull(IN_STSFL,' ') ='4'";  // Authorised
					M_strSQLQRY += " and isnull(in_autqt,0)-isnull(in_ordqt,0)-isnull(in_fccqt,0) >0 order by in_indno desc";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
				}
				else if(L_KE.getSource().equals(txtGINNO)){	// Gate-In No.
					strBOENO = txtBOENO.getText().trim();
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtGINNO";
					String L_ARRHDR[] = {"Gate-In No.","Lorry No.","Transporter","Transporter Name","Chalan No.","Chalan Date"};
					M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_TPRCD,WB_TPRDS,WB_CHLNO,WB_CHLDT";
					M_strSQLQRY += " from MM_WBTRN";
					M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
					M_strSQLQRY += " and isnull(WB_STSFL,'') <> 'X'";
		//REMOVED ON 03/09/2004 as MULTIPLE GRIN REQ. AGAINST ONE GATE IN. : API HBP/RSP
//					M_strSQLQRY += " and isnull(WB_DOCRF,'')=''";
					if(txtGINNO.getText().trim().length() >0)
						M_strSQLQRY += " and WB_DOCNO like '"+txtGINNO.getText().trim() +"%'";
                    if(strSTRTP.equals(strTNSTR_fn))
                        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					        M_strSQLQRY += " and WB_ACPTG = 'A'";
                  	if(!txtBOENO.getText().equals(""))
						M_strSQLQRY += " and WB_BOENO = '" + txtBOENO.getText().trim() + "'";
					M_strSQLQRY += " order by WB_DOCNO DESC";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,6,"CT");
				}
				else if(L_KE.getSource().equals(txtVENCD))	// Supplier Code
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVENCD";
					String L_ARRHDR[] = {"Code","Name"};
					M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST";
					M_strSQLQRY += " where PT_PRTTP = 'S'";
					if(txtVENCD.getText().trim().length() >0)
						M_strSQLQRY += " and PT_PRTCD like '"+txtVENCD.getText().trim().toUpperCase() +"%'";
					M_strSQLQRY += " ORDER BY PT_PRTNM";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtMATCD)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMATCD";
					String L_ARRHDR[] = {"Item Code","Description","UOM","Pen.Qty."};
					if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
					{
						M_strSQLQRY = "Select PO_MATCD,ST_MATDS,PO_UOMCD,isnull(PO_PORQT,0) -isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0) L_QTY";
						M_strSQLQRY += "  from MM_POMST,MM_STMST where isnull(PO_STSFL,' ') <> 'X'";
						M_strSQLQRY += " and PO_STRTP = ST_STRTP AND PO_MATCD = ST_MATCD AND PO_CMPCD=ST_CMPCD ";
						M_strSQLQRY += " and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '"+strSTRTP+"'";
						M_strSQLQRY += " and isnull(PO_PORTP,'') <> '03' AND PO_PORNO = '"+txtPORNO.getText().trim()+"'";
						M_strSQLQRY += " and isnull(PO_PORQT,0) -isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0) >0";
						if(txtMATCD.getText().trim().length() >0)
							M_strSQLQRY += " and PO_MATCD like '"+txtMATCD.getText().trim()+"%'";
						M_strSQLQRY += " order by ST_MATDS";
						if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,4,"CT",new int[]{85,335,35,60});
					}
					else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
					{
						M_strSQLQRY = "Select IN_MATCD,ST_MATDS,ST_UOMCD,isnull(IN_AUTQT,0) -isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0) L_QTY ";
						M_strSQLQRY += "  from MM_STMST,MM_INMST where in_strtp = st_strtp and IN_MATCD = ST_MATCD and IN_CMPCD = ST_CMPCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,' ') <> 'X' and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,' ') ='4'";
						M_strSQLQRY += " and IN_STRTP ='"+M_strSBSCD.substring(2,4) +"' and isnull(IN_INDTP,'') ='01' AND IN_INDNO ='"+tblGINVL.getValueAt(TBL_INDNO,tblGINVL.getSelectedRow()).toString() +"'";
						M_strSQLQRY += " and isnull(IN_AUTQT,0) -isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0) >0";
						if(txtMATCD.getText().trim().length() >0)
							M_strSQLQRY += " and IN_MATCD like '"+txtMATCD.getText().trim()+"%'";
						M_strSQLQRY += " order by ST_MATDS";
						if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,4,"CT",new int[]{85,335,35,60});
					}
                    else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strJBWGR_fn))
                    {
                        if(cmbMATTP.getSelectedItem().toString().substring(0,1).equals("5"))
                            M_strSQLQRY = "Select CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_GRPCD IN('51','52','53','54') order by CT_MATCD";    
                        else if(cmbMATTP.getSelectedItem().toString().substring(0,1).equals("2"))
                            M_strSQLQRY = "Select CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_GRPCD IN('68') order by CT_MATCD";    
                        else
                            M_strSQLQRY = "Select CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_GRPCD IN('69') order by CT_MATCD";    
                      	    cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"CT");
                    }
					
				}
				else if(M_objSOURC == txtEXCCT)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtEXCCT";
					String L_ARRHDR[] = {"Code","Material Type"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
			///***
				else if(M_objSOURC == tblCYLDT.cmpEDITR[1])
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtCLMAT";
					String L_staHEADR[] = {"Code","Material Type"};
					if(vtrMATCD.size()>0)
						cl_hlp(vtrMATCD,2,1,L_staHEADR,2,"CT");
					else
						setMSG("No material in Cylinder ..",'N');
				}
			///***
			//	protected  void cl_hlp(Object P_objSQLQRY,int P_intCLSCH,int P_intCLRTN,String[] P_staHEADR,int P_intNUMCL,String P_strHLPOS)
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(L_KE.getSource().equals(cmbGRNTP))
				{
					cmbMATTP.requestFocus();
					setMSG("Please select the Material Type..",'N');
				}
				if(L_KE.getSource().equals(cmbMATTP))
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					{
						txtGRNNO.requestFocus();
						setMSG("Enter the GRIN No, F1 o select from list..",'N');
					}
					else
					{
                         if(!strSTRTP.equals(strTNSTR_fn))
						 {
						   txtGINNO.requestFocus();
						   setMSG("Enter the Gate In Number, F1 o select from list..",'N');
						 }
						else
						{
							txtBOENO.requestFocus();
							 setMSG("Enter Bill of Entry No., F1 o select from list..",'N');
						}
					}
				}
				if(L_KE.getSource().equals(txtGRNNO))
				{			
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					strSTRTP = M_strSBSCD.substring(2,4);
					strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
					strMATTP = String.valueOf(cmbMATTP.getSelectedItem()).trim().substring(0,1);
					strGRNNO = txtGRNNO.getText().trim();
					tblGINVL.clrTABLE();
					if(getDATA(strSTRTP,strGRNTP,strGRNNO)){
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
						setENBL(false);
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						setENBL(true);
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(L_KE.getSource().equals(txtGRNDT))
				{			
					strGRNDT = txtGRNDT.getText().trim();
					txtBOENO.requestFocus();
							
				}
				else if(L_KE.getSource().equals(txtBOENO)){			// B/E No.
					strBOENO = txtBOENO.getText().trim();
					txtCNSNO.requestFocus();
				}
				else if(L_KE.getSource().equals(txtCNSNO)){			// B/E No.
					strCNSNO = txtBOENO.getText().trim();
					txtGINNO.requestFocus();
					 setMSG("Enter the Gate In Number, F1 o select from list..",'N');
				}
				else if(L_KE.getSource().equals(txtPORNO)){			// P.O.No.
					strPORNO = txtPORNO.getText().trim();
					txtTRNCD.requestFocus();
				}
				else if(L_KE.getSource().equals(txtTRNCD)){
					txtLRYNO.requestFocus();
				}
				else if(L_KE.getSource().equals(txtLRYNO)){
					strGRNTP = cmbGRNTP.getSelectedItem().toString().substring(0,2);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						if(strGRNTP.equals("01")) // P.O
						{
							tblGINVL.setRowSelectionInterval(0,0);
							tblGINVL.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);
							tblGINVL.editCellAt(tblGINVL.getSelectedRow(),TBL_MATCD);
							tblGINVL.cmpEDITR[1].requestFocus();
							setMSG("press F1 to select the item code ..",'N');
						}
						else if(strGRNTP.equals("02")) // P.O
							txtINDNO.requestFocus();
					}
				}
				else if(L_KE.getSource().equals(txtGINNO)){			// Gate-In No.
					strGINNO = txtGINNO.getText().trim();
					strBOENO = txtBOENO.getText().trim();
					txtCHLNO.requestFocus();
				}
				else if(L_KE.getSource().equals(txtCHLNO))
					txtCHLDT.requestFocus();
				else if(L_KE.getSource().equals(txtCHLDT))
					txtVENCD.requestFocus();
				else if(L_KE.getSource().equals(txtVENCD)){			// Supplier Code
					strVENCD = txtVENCD.getText().trim();
					if(strVENCD.equals("Z0999"))
					{
						txtVENNM.setEnabled(true);
						txtVENNM.requestFocus();
					}
					else
					{
						txtVENNM.setEnabled(false);
						txtVENCD.transferFocus();
					}
				}
				else if(L_KE.getSource().equals(txtVENNM))
					txtPORNO.requestFocus();
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"keyPressed");
		}
	}
	
	// Validation of P.O.No.
	private boolean vldPORNO(String LP_PORNO){
		try{
			M_strSQLQRY = "Select PO_PORNO,PO_PORDT,PO_VENCD from MM_POMST";
			M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,' ') in('A','H')";
			if(txtVENCD.getText().trim().length() >0)
				M_strSQLQRY += " and PO_VENCD = '"+txtVENCD.getText().trim() +"'";
			M_strSQLQRY += " and PO_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_strSQLQRY += " and isnull(PO_PORTP,'') <>'03' AND PO_PORNO = '" + LP_PORNO + "'";
	      	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		    if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				M_rstRSSET.close();			
				if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
					getPODTL(txtPORNO.getText().trim(),true);
				M_rstRSSET.close();
				return true;
			}
			setMSG("Invalid P.O.No. Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
			
		}catch(Exception L_EX){
			setMSG(L_EX,"vldPORNO");
		}
		return false;
	}
	
	// Check Bill of Entry No. for validity
	public boolean vldBOENO(String LP_BOENO)
	{	
		try
		{
            strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
			M_strSQLQRY = "Select BE_BOENO,BE_CONNO,BE_BOEQT,BE_CHLQT,PO_VENCD,PT_PRTNM,BE_PORNO";
			M_strSQLQRY += " from MM_BETRN,MM_POMST,CO_PTMST";    
			M_strSQLQRY += " where BE_PORNO = PO_PORNO and BE_CMPCD = PO_CMPCD ";
			M_strSQLQRY += " and PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_PRTTP = 'S'";
			M_strSQLQRY += " and PT_PRTCD = PO_VENCD";
			M_strSQLQRY += " and BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_BOENO = '" + LP_BOENO + "'";
			M_strSQLQRY += " and BE_BOEQT > BE_CHLQT";
			M_strSQLQRY += " and BE_STSFL <> 'X'" ;
            if(strGRNTP.equals(strEXBGR_fn))
                    M_strSQLQRY += " and BE_MATTP = '" + strEXBTP_fn + "'";        
            else if(strGRNTP.equals(strADVGR_fn))
                    M_strSQLQRY += " and BE_MATTP = '" + strADVTP_fn + "'";        
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				txtCNSNO.setText(M_rstRSSET.getString("BE_CONNO"));
				txtVENCD.setText(M_rstRSSET.getString("PO_VENCD"));
				txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));
				txtPORNO.setText(M_rstRSSET.getString("BE_PORNO"));
				M_rstRSSET.close();
				return true;
			}
			txtCNSNO.setText("");
			txtVENCD.setText("");
			txtVENNM.setText("");
			txtPORNO.setText("");
			setMSG("Invalid Bill of Entry No.Press F1 for help",'E');
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"vldBOENO");
		}
		return false;
	}
	// Validation of Vendor Code
	private boolean vldVENCD(String LP_VENCD)
	{
		try
		{
			M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
			M_strSQLQRY += " where PT_PRTTP = 'S'";
			M_strSQLQRY += " and PT_PRTCD = '" + LP_VENCD + "'";
        	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));
				tblGINVL.setRowSelectionInterval(0,0);
				tblGINVL.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);
				tblGINVL.editCellAt(0,TBL_MATCD);
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			txtVENNM.setText("");
			setMSG("Invalid Supplier. Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();			

		}catch(Exception L_EX){
			setMSG(L_EX,"vldVENCD");
			return false;
		}
		return false;
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtGRNNO")){				// GRIN No
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
				if(getDATA(strSTRTP,strGRNTP,cl_dat.M_strHLPSTR_pbst)){
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
						setENBL(false);
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						setENBL(true);
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_strHLPFLD.equals("txtBOENO")){			// Bill of Entry No.
				strBEMAT ="";
				txtBOENO.setText(cl_dat.M_strHLPSTR_pbst);
				txtCNSNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim());
				txtVENCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),6)).trim());
				txtVENNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),7)).trim());
				txtPORNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),8)).trim());
				strBEMAT = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),9)).trim();
			}
			else if(M_strHLPFLD.equals("txtPORNO")){			// P.O No.
				txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);
				txtVENCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
				txtVENNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());
			}
			else if(M_strHLPFLD.equals("txtVENCD")){			// Supplier Code
				txtVENCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtVENNM.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtINDNO")){			// Indent
				txtINDNO.setText(cl_dat.M_strHLPSTR_pbst);
				if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
					getINDTL(txtINDNO.getText().trim());
			}
			else if(M_strHLPFLD.equals("txtGINNO")){			// Gate-In No.
        		txtGINNO.setText(cl_dat.M_strHLPSTR_pbst);
				txtLRYNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				txtTRNCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
				txtTRNNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());
				txtCHLNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim());
				txtCHLDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)).trim());
                if(strSTRTP.equals(strTNSTR_fn))
					getWBDTL(strGINTP,cl_dat.M_strHLPSTR_pbst);
				else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
					getPODTL(txtPORNO.getText().trim(),true);
        	}
			else if(M_strHLPFLD.equals("txtMATCD")){			// Material Code
				if(tblGINVL.isEditing())
					tblGINVL.getCellEditor().stopCellEditing();
				strMATCD = tblGINVL.getValueAt(tblGINVL.getSelectedRow(),TBL_MATCD).toString();
				if(objTBLVRF.verify(tblGINVL.getSelectedRow(),TBL_MATCD))
				{
					tblGINVL.setRowSelectionInterval(tblGINVL.getSelectedRow(),tblGINVL.getSelectedRow());		
					tblGINVL.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);		
					tblGINVL.editCellAt(tblGINVL.getSelectedRow(),TBL_MATCD);
					tblGINVL.cmpEDITR[TBL_MATCD].requestFocus();
				}
				txtMATCD.setText(cl_dat.M_strHLPSTR_pbst);
				
			}
			else if(M_strHLPFLD.equals("txtEXCCT")){			// Excise Material Category
				txtEXCCT.setText(cl_dat.M_strHLPSTR_pbst);
				if(tblGINVL.isEditing())
					tblGINVL.getCellEditor().stopCellEditing();
			}
			else if(M_strHLPFLD.equals("txtCLMAT")){			// Cylinder Material
				((JTextField)tblCYLDT.cmpEDITR[1]).setText(cl_dat.M_strHLPSTR_pbst);
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"exeHLPOK");
		}
	}
	// Method to check whether all necessary data has been entered
	@SuppressWarnings("unchecked") boolean vldDATA()
	{
		try
		{
			if(tblCYLDT.isEditing())
				tblCYLDT.getCellEditor().stopCellEditing();
			strGRNDT = txtGRNDT.getText().trim();
			strPORNO = txtPORNO.getText().trim();
			strBOENO = txtBOENO.getText().trim();
			strGINNO = txtGINNO.getText().trim();
			strCHLNO = txtCHLNO.getText().trim();
			strCHLDT = txtCHLDT.getText().trim();
			strVENCD = txtVENCD.getText().trim();
			
			/*if(strGRNDT.length() == 0){					//  GRIN Date
				txtGRNDT.requestFocus();
				setMSG("GRIN Date can not be empty",'E');
				return false;
			}
			else
			{
				strGRNDT = txtGRNDT.getText().trim();
				if(M_fmtLCDAT.parse(strGRNDT).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
					return false;
				}
			}*/
			if(strGINNO.length() < 8){					// Chalan No
				txtGINNO.requestFocus();
				setMSG("Invalid Gate In No. ..",'E');
				return false;
			}
			else if(strCHLNO.length() == 0){					// Chalan No
				txtCHLNO.requestFocus();
				setMSG("Chalan No. can not be empty",'E');
				return false;
			}
			else if(strCHLDT.length() == 0){					//  Chalan Date
				txtCHLDT.requestFocus();
				setMSG("Chalan Date can not be empty",'E');
				return false;
			}
			else if(strVENCD.length() == 0){					//Vendor
				txtVENCD.requestFocus();
				setMSG("Vendor Ref. can not be empty",'E');
				return false;
			}
			else if(strPORNO.length() == 0){					//  GRIN Date
             if(strSTRTP.equals(strTNSTR_fn))
			   {
					txtPORNO.requestFocus();
					setMSG("P.O. Ref. can not be empty",'E');
					return false;
				}
			   else
			   {
				   if(strGRNTP.equals(strPORGR_fn))
				   {
					   txtPORNO.requestFocus();
						setMSG("P.O. Ref. can not be empty",'E');
						return false;
				   }
				   //else return true;
			   }
			}
			if(strGRNTP.equals(strPORGR_fn))
				if(strPORNO.length() < 8)
				{
					txtPORNO.requestFocus();
					setMSG("Invalid P.O.No.",'E');
					return false;
				}
			else if(strGRNTP.equals(strCSHGR_fn))
				if(txtINDNO.getText().trim().length() < 8)
				{
					txtINDNO.requestFocus();
					setMSG("Invalid Indent No.",'E');
					return false;
				}
            if(strSTRTP.equals(strTNSTR_fn))
			{
				if(strBOENO.length() == 0)
				{	
					txtBOENO.requestFocus();
					setMSG("Bill of Entry no. can not be empty",'E');
					return false;
				}
				if(strCNSNO.length() == 0)
				{	
					txtCNSNO.requestFocus();
					setMSG("Consignment no. can not be empty",'E');
					return false;
				}
			}
			if(tblGINVL.isEditing())
				tblGINVL.getCellEditor().stopCellEditing();
			strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(tblGINVL.getValueAt(i,TBL_CHLQT).toString().length() == 0)
					{
						setMSG("Chalan quantity can not be empty",'E');
						return false;
					}
					else if(tblGINVL.getValueAt(i,TBL_RECQT).toString().length() == 0)
					{
						setMSG("Recieved quantity can not be empty",'E');
						return false;
					}
					else if(tblGINVL.getValueAt(i,TBL_INDNO).toString().length() == 0)
					{
						if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
						{
							setMSG("Indent No.can not be empty",'E');
							return false;
						}
					}
					if(strMATTP.equals(strPKGMT_fn))
					{
						if(!tblGINVL.getValueAt(i,TBL_MATCD).toString().substring(0,2).equals("69"))
						{
							setMSG("Invalid Material Category..",'E');
							return false;
						}
					}
					else if(strMATTP.equals(strRAWMT_fn))
					{
						if((!tblGINVL.getValueAt(i,TBL_MATCD).toString().substring(0,2).equals("68"))&&(!tblGINVL.getValueAt(i,TBL_MATCD).toString().substring(0,2).equals("99")))
						{
							setMSG("Invalid Material Category..",'E');
							return false;
						}
					}
					else if(strMATTP.equals(strSTRSP_fn))
					{
						if(tblGINVL.getValueAt(i,TBL_MATCD).toString().substring(0,2).equals("68"))
						{
							setMSG("Invalid Material Category..",'E');
							return false;
						}
						else if(tblGINVL.getValueAt(i,TBL_MATCD).toString().substring(0,2).equals("69"))
						{
							setMSG("Invalid Material Category..",'E');
							return false;
						}
					}
					if(strSTRTP.equals(strTNSTR_fn))
					{
						if(!tblGINVL.getValueAt(i,TBL_EXCCT).toString().equals(strBEMAT))
						{
							setMSG("Material Category does not match with Excise Category in B/E..",'E');
							return false;	   
						}
					}
				}
			}
			///*** ADDED BY AAP FOR CYLINDER
			Vector<String> L_vtrTEMP=null;
		//Gather deatils of cylinders and put in hstCYLDT
			for(int i=0;i<tblCYLDT.getRowCount()&&tblCYLDT.getValueAt(i,0).toString().equals("true");i++)
			{
				if(hstCYLDT.containsKey(tblCYLDT.getValueAt(i,TBL_MATCD)))
					L_vtrTEMP=(Vector)hstCYLDT.get(tblCYLDT.getValueAt(i,TBL_MATCD));
				else
					L_vtrTEMP=new Vector<String>(5,2);
				if(!L_vtrTEMP.contains(tblCYLDT.getValueAt(i,2)+"|"+tblCYLDT.getValueAt(i,3)))
					L_vtrTEMP.addElement(tblCYLDT.getValueAt(i,2)+"|"+tblCYLDT.getValueAt(i,3));
				if(L_vtrTEMP.size()>0)
					hstCYLDT.put(tblCYLDT.getValueAt(i,TBL_MATCD),L_vtrTEMP );
			}
		//Check that, at least one cylinder is entered for every material code in staCYLCD
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
			for (int i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,0).equals(Boolean.TRUE))
					for(int j=0;j<staCYLCD.length;j++)
					{
						if(tblGINVL.getValueAt(i,TBL_MATCD).toString().substring(0,staCYLCD[j].length()).equals(staCYLCD[j]))
							if(!hstCYLDT.containsKey(tblGINVL.getValueAt(i,TBL_MATCD)))
							{
								setMSG("Enter Cylinder Numbers for "+tblGINVL.getValueAt(i,TBL_MATCD),'E');
								tbpMAIN.setSelectedIndex(1);
								for(int k=0;k<tblCYLDT.getRowCount();k++)
									if(tblCYLDT.getValueAt(k,1).toString().length()==0)
									{
										tblCYLDT.editCellAt(k,1);
										break;
									}
								return false;
							}
					}
			}
		//Check cylinder no.s for deletion
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
				for(int i=0;i<tblGINVL.getRowCount();i++)
				{
					if(tblGINVL.getValueAt(i,0).equals(Boolean.FALSE))
						continue;
					for(int j=0;j<tblCYLDT.getRowCount();j++)
					{
						if(tblCYLDT.getValueAt(j,TBL_MATCD).equals(tblGINVL.getValueAt(i,TBL_MATCD)))
							tblCYLDT.setValueAt(Boolean.TRUE,j,0);
					}
				}
			///***
		}
		catch(Exception e){
			setMSG(e,"vldDATA ");
			return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked") private boolean getDATA(String P_strSTRTP,String P_strGRNTP,String P_strGRNNO)
	{
		boolean L_FIRST = true;
		java.sql.Date L_datTMPDT;
		try
		{
			
			String L_TPRCD,L_CHLQT,L_RECQT;
			String L_strMATCD ="",L_strINDNO ="";
			int i = 0;
			M_strSQLQRY = "Select GR_AMDNO,GR_GRNDT,GR_BOENO,GR_CNSNO,GR_PORNO,GR_VENCD,GR_VENNM,";
			M_strSQLQRY += " GR_GINNO,GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_TRNCD,GR_TRNNM,GR_MATCD,ST_MATDS,ST_UOMCD,";
			M_strSQLQRY += " GR_CHLQT,GR_RECQT,GR_ACPQT,GR_REJQT,GR_REJCD,GR_QLTFL,GR_DELFL,GR_MODFL,GR_EXCCT,GR_LUSBY,GR_LUPDT,GR_STSFL,GR_BATNO from MM_GRMST,MM_STMST";
			M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + P_strSTRTP + "'";
			M_strSQLQRY += " and GR_STRTP = ST_STRTP AND GR_MATCD =  ST_MATCD and GR_CMPCD = ST_CMPCD";
			M_strSQLQRY += " and GR_GRNTP = '" + P_strGRNTP + "'";
			M_strSQLQRY += " and GR_GRNNO = '" + P_strGRNNO + "'";
//			M_strSQLQRY += " and GR_BATNO = '" + strDFLBT_fn + "'";
			M_strSQLQRY += " and isnull(GR_STSFL,'') <> 'X'";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(hstITMDT !=null)
				hstITMDT.clear();
			while(M_rstRSSET.next())
			{
				//TO CHECK WHETHER GRIN IS ACCEPTED
				if(M_rstRSSET.getString("GR_STSFL").equals("2") && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
				{
					setMSG("GRIN is already Accepted ..",'E');
					M_rstRSSET.close();
					return false;
				}
				///***ADDED BY AAP FOR CYLINDER
				hstMATDS.put(M_rstRSSET.getString("GR_MATCD"),M_rstRSSET.getString("ST_MATDS"));
				if(L_FIRST){
					txtGRNNO.setText(P_strGRNNO);
					txtAMDNO.setText(M_rstRSSET.getString("GR_AMDNO"));
					L_datTMPDT = M_rstRSSET.getDate("GR_GRNDT");
					if(L_datTMPDT !=null)
						txtGRNDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					else
						txtGRNDT.setText("");
					txtBOENO.setText(M_rstRSSET.getString("GR_BOENO"));
					txtCNSNO.setText(M_rstRSSET.getString("GR_CNSNO"));
					if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
						txtPORNO.setText(M_rstRSSET.getString("GR_PORNO"));
					else
						txtPORNO.setText("");
					txtVENCD.setText(M_rstRSSET.getString("GR_VENCD"));
					txtVENNM.setText(M_rstRSSET.getString("GR_VENNM"));
					txtGINNO.setText(M_rstRSSET.getString("GR_GINNO"));
					txtLRYNO.setText(M_rstRSSET.getString("GR_LRYNO"));
					txtCHLNO.setText(M_rstRSSET.getString("GR_CHLNO"));
					L_datTMPDT = M_rstRSSET.getDate("GR_CHLDT");
					if(L_datTMPDT !=null)
						txtCHLDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					else
						txtCHLDT.setText("");
					txtTRNCD.setText(M_rstRSSET.getString("GR_TRNCD"));
					txtTRNNM.setText(M_rstRSSET.getString("GR_TRNNM"));
					L_FIRST = false;
					L_datTMPDT = M_rstRSSET.getDate("GR_LUPDT");
					if(L_datTMPDT !=null)
						cl_dat.M_txtDATE_pbst.setText(M_fmtLCDAT.format(L_datTMPDT));
					else
						cl_dat.M_txtDATE_pbst.setText("");
					cl_dat.M_txtUSER_pbst.setText(M_rstRSSET.getString("GR_LUSBY"));
					//////
					if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))  
						getPODTL(txtPORNO.getText().trim(),false);
					//tblGINVL.clrTABLE();
					///// 

				}
				if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
				{
					L_strINDNO = nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),"");
					tblGINVL.setValueAt(L_strINDNO,i,TBL_INDNO);
				}
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
				tblGINVL.setValueAt(L_strMATCD,i,TBL_MATCD);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),i,TBL_MATDS);
				hstITMDT.put(L_strMATCD+nvlSTRVL(M_rstRSSET.getString("GR_BATNO"),""),"");
				tblGINVL.setValueAt(M_rstRSSET.getString("ST_UOMCD"),i,TBL_UOMCD);
				strCHLQT = M_rstRSSET.getString("GR_CHLQT");
				tblGINVL.setValueAt(strCHLQT,i,TBL_CHLQT);
				strRECQT = M_rstRSSET.getString("GR_RECQT");
				tblGINVL.setValueAt(strRECQT,i,TBL_RECQT);
				strACPQT = M_rstRSSET.getString("GR_ACPQT");
				tblGINVL.setValueAt(strACPQT,i,TBL_ACPQT);
				strREJQT = M_rstRSSET.getString("GR_REJQT");
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_REJCD"),""),i,TBL_REJCD);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_DELFL"),""),i,TBL_DELFL);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_QLTFL"),""),i,TBL_QLTFL);
				tblGINVL.setValueAt(strREJQT,i,TBL_REJQT);
				tblGINVL.setValueAt(M_rstRSSET.getString("GR_EXCCT"),i,TBL_EXCCT);
				tblGINVL.setValueAt(M_rstRSSET.getString("GR_BATNO"),i,TBL_BATNO);
				strMODFL = nvlSTRVL(M_rstRSSET.getString("GR_MODFL"),"N");
				if(strMODFL.equals("Y"))
					tblGINVL.setValueAt(Boolean.TRUE,i,TBL_MODFL);
				else
					tblGINVL.setValueAt(Boolean.FALSE,i,TBL_MODFL);
				i++;
			}
			if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
			{
				intINDRW = i;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
				{
					txtINDNO.setEnabled(true);
				}
			}
			// Added on 17/11/2007, to rectify problem in adding new item through modification
				//if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))  
				//	getPODTL(txtPORNO.getText().trim());
		///*** ADDED FOR CYLINDER
			vtrOLDCL=new Vector(5,2);
			Vector L_vtrTEMP=null;
		//Coolect data from MM_CLTRN
		System.out.println(cl_dat.M_strCMPCD_pbst);
		
		System.out.println(M_strSBSCD.substring(2,4));
		
		System.out.println(txtGRNNO.getText());
			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from MM_CLTRN where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and CLT_GRNNO='"+txtGRNNO.getText()+"' and CLT_STSFL<>'X' order by CLT_MATCD,CLT_SRLNO");
			if(M_rstRSSET!=null)
			{
				i=0;
				while (M_rstRSSET.next())
				{
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_MATCD"),i,1);
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_SRLNO"),i,2);
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_RFDAY"),i,3);
					vtrOLDCL.addElement(M_rstRSSET.getString("CLT_MATCD")+"|"+M_rstRSSET.getString("CLT_SRLNO"));
					//ADD DATA TO hstCYLDT
					if(hstCYLDT.containsKey(tblCYLDT.getValueAt(i,TBL_MATCD)))
						L_vtrTEMP=(Vector)hstCYLDT.get(tblCYLDT.getValueAt(i,TBL_MATCD));
					else
						L_vtrTEMP=new Vector(5,2);
					if(!L_vtrTEMP.contains(tblCYLDT.getValueAt(i,2)+"|"+tblCYLDT.getValueAt(i,3)))
						L_vtrTEMP.addElement(tblCYLDT.getValueAt(i,2)+"|"+tblCYLDT.getValueAt(i,3));
					if(L_vtrTEMP.size()>0)
						hstCYLDT.put(tblCYLDT.getValueAt(i,TBL_MATCD),L_vtrTEMP );
					i++;
				}
			}
		///***
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception e){
			setMSG(e,"getDATA");
		}
		if(L_FIRST)
			setMSG("Record not found ..",'E');
		return !L_FIRST;
	}
	
	// Method to add the data in mm_tegrt table
	@SuppressWarnings("unchecked") void exeSAVE()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(!vldDATA())
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			cl_dat.M_flgLCUPD_pbst = true;
			strSTRTP = M_strSBSCD.substring(2,4);
			strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
			strMATTP = String.valueOf(cmbMATTP.getSelectedItem()).trim().substring(0,1);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				txtGRNNO.setText(genGRNNO(strMATTP));
			strGRNNO = txtGRNNO.getText().trim();
			strGRNDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtGRNDT.getText().trim()));
			strBOENO = nvlSTRVL(txtBOENO.getText().trim(),"");
			strCNSNO = nvlSTRVL(txtCNSNO.getText().trim(),"");
			strPORNO = nvlSTRVL(txtPORNO.getText().trim(),"");
			strVENCD = nvlSTRVL(txtVENCD.getText().trim(),"");
			strVENNM = nvlSTRVL(txtVENNM.getText().trim(),"");
			strGINNO = txtGINNO.getText().trim();
			strLRYNO = txtLRYNO.getText().trim();
			strCHLNO = txtCHLNO.getText().trim();
			strCHLDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHLDT.getText().trim()));
			strTRNCD = txtTRNCD.getText().trim();
			strTRNNM = txtTRNNM.getText().trim();
			strPREBY = cl_dat.M_strUSRCD_pbst.trim();
			strPREDT = strGRNDT;
			LM_LUSBY = strPREBY;
			LM_LUPDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			
			for(int i=0;i<intROWCT;i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					strINDNO = String.valueOf(tblGINVL.getValueAt(i,TBL_INDNO));
					strMATCD = String.valueOf(tblGINVL.getValueAt(i,TBL_MATCD));
					strUOMCD = String.valueOf(tblGINVL.getValueAt(i,TBL_UOMCD));
					strCHLQT = String.valueOf(tblGINVL.getValueAt(i,TBL_CHLQT));	
					strRECQT = String.valueOf(tblGINVL.getValueAt(i,TBL_RECQT));
					strREJCD = String.valueOf(tblGINVL.getValueAt(i,TBL_REJCD));
					strACPQT = String.valueOf(tblGINVL.getValueAt(i,TBL_ACPQT));
					strREJQT = String.valueOf(tblGINVL.getValueAt(i,TBL_REJQT));
					strDELFL = String.valueOf(tblGINVL.getValueAt(i,TBL_DELFL));
					strQLTFL = String.valueOf(tblGINVL.getValueAt(i,TBL_QLTFL));
					strLOCCD = String.valueOf(tblGINVL.getValueAt(i,TBL_LOCCD));
					strEXCCT = String.valueOf(tblGINVL.getValueAt(i,TBL_EXCCT));
					strBATNO = String.valueOf(tblGINVL.getValueAt(i,TBL_BATNO));
					if(strBATNO.length() ==0)
						strBATNO = strGRNNO;
					if(tblGINVL.getValueAt(i,TBL_MODFL).toString().equals("true"))
						strMODFL = "Y";
					else
						strMODFL = "N";
					/*
						// pending -- if replacement material then automatic issue note should get generated.
					*/
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					{
						exeINSREC();	
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
					{
						if(hstITMDT.containsKey(tblGINVL.getValueAt(i,TBL_MATCD).toString()+tblGINVL.getValueAt(i,TBL_BATNO).toString()))
						{
							M_strSQLQRY = "Update MM_GRMST set ";
						//	M_strSQLQRY += "GR_GRNDT = '" + strGRNDT + "',";
							M_strSQLQRY += "GR_GINNO = '" + strGINNO + "',";
							M_strSQLQRY += "GR_LRYNO = '" + strLRYNO + "',";
							M_strSQLQRY += "GR_CHLNO = '" + strCHLNO + "',";
							M_strSQLQRY += "GR_CHLDT = '" + strCHLDT + "',";
							M_strSQLQRY += "GR_CHLQT = " + strCHLQT + ",";
							M_strSQLQRY += "GR_BOENO = '" + strBOENO + "',";
							M_strSQLQRY += "GR_CNSNO = '" + strCNSNO + "',";
							if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
								M_strSQLQRY += "GR_PORNO = '" + strINDNO + "',";
							M_strSQLQRY += "GR_VENCD = '" + strVENCD + "',";
							M_strSQLQRY += "GR_VENNM = '" + strVENNM + "',";
							M_strSQLQRY += "GR_TRNCD = '" + strTRNCD + "',";
							M_strSQLQRY += "GR_TRNNM = '" + strTRNNM + "',";
							M_strSQLQRY += "GR_RECQT = " + strRECQT + ",";
							M_strSQLQRY += "GR_MODFL = '" + strMODFL + "',";
							M_strSQLQRY += "GR_EXCCT = '" + strEXCCT + "',";
							M_strSQLQRY += "GR_TRNFL = '" + strTRNFL_fn + "',";
							M_strSQLQRY += "GR_LUSBY = '" + LM_LUSBY + "',";
							M_strSQLQRY += "GR_LUPDT = '" + LM_LUPDT+"'";
							M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
							M_strSQLQRY += " and GR_GRNTP = '" + strGRNTP + "'";
							M_strSQLQRY += " and GR_GRNNO = '" + strGRNNO + "'";
							M_strSQLQRY += " and GR_MATCD = '" + strMATCD + "'";
							M_strSQLQRY += " and GR_BATNO = '" + strBATNO + "'";
							M_strSQLQRY += " and isnull(GR_ACPQT,0) = 0 ";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
						else
						{
							exeINSREC();
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
					{
						M_strSQLQRY = "Update MM_GRMST set ";
						M_strSQLQRY += "GR_STSFL = 'X',";
						M_strSQLQRY += "GR_TRNFL = '" + strTRNFL_fn + "',";
						M_strSQLQRY += "GR_LUSBY = '" + LM_LUSBY + "',";
						M_strSQLQRY += "GR_LUPDT = '" + LM_LUPDT+"'";
						M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
						M_strSQLQRY += " and GR_GRNTP = '" + strGRNTP + "'";
						M_strSQLQRY += " and GR_GRNNO = '" + strGRNNO + "'";
						M_strSQLQRY += " and GR_MATCD = '" + strMATCD + "'";
						M_strSQLQRY += " and GR_BATNO = '" + strBATNO+ "'";
						M_strSQLQRY += " and isnull(GR_ACPQT,0) = 0 ";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					
						M_strSQLQRY = "Update MM_WBTRN set";
						M_strSQLQRY += " WB_DOCRF = '',";
                        M_strSQLQRY += " WB_ORDRF = '" + strPORNO + "',";
                        M_strSQLQRY += " WB_CONNO = '" + strCNSNO + "',";
                        M_strSQLQRY += " WB_PRTCD = '" + strVENCD + "',";
                        M_strSQLQRY += " WB_PRTDS = '" + strVENNM + "',";
						M_strSQLQRY += " WB_BOENO = '" + strBOENO + "',";
						M_strSQLQRY += " WB_TRNFL = '" + strTRNFL_fn + "',";
						M_strSQLQRY += " WB_LUSBY = '" + LM_LUSBY + "',";
						M_strSQLQRY += " WB_LUPDT = '" + LM_LUPDT +"'";
						M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
						M_strSQLQRY += " and WB_DOCNO = '" + strGINNO + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)&& cl_dat.M_flgLCUPD_pbst)
					{
						M_strSQLQRY = "Update MM_WBTRN set";
						M_strSQLQRY += " WB_DOCRF = '" + strGRNNO + "',";
                        M_strSQLQRY += " WB_ORDRF = '" + strPORNO + "',";
                        M_strSQLQRY += " WB_CONNO = '" + strCNSNO + "',";
                        M_strSQLQRY += " WB_PRTCD = '" + strVENCD + "',";
                        M_strSQLQRY += " WB_PRTDS = '" + strVENNM + "',";
						M_strSQLQRY += " WB_BOENO = '" + strBOENO + "',";
						M_strSQLQRY += " WB_TRNFL = '" + strTRNFL_fn + "',";
						M_strSQLQRY += " WB_LUSBY = '" + LM_LUSBY + "',";
						M_strSQLQRY += " WB_LUPDT = '" + LM_LUPDT +"'";
						M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
						M_strSQLQRY += " and WB_DOCNO = '" + strGINNO + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						String L_LUPDT = cl_dat.M_strLOGDT_pbst;
						L_LUPDT = L_LUPDT.substring(6) + "-" + L_LUPDT.substring(3,5) + "-" + L_LUPDT.substring(0,2);
                        if(strSTRTP.equals(strTNSTR_fn))
						{
							if(!strBOENO.equals(""))
							{	// If Bill of Entry is not available
								pstmBETRN.setDouble(1,Double.parseDouble(strCHLQT));
								pstmBETRN.setDouble(2,Double.parseDouble(strRECQT));
								pstmBETRN.setString(3,strTRNFL_fn);
								pstmBETRN.setString(4,LM_LUSBY);
								pstmBETRN.setDate(5,java.sql.Date.valueOf(L_LUPDT));
								pstmBETRN.setString(6,strPORNO);
								pstmBETRN.setString(7,strCNSNO);
								pstmBETRN.setString(8,strBOENO);
								if(pstmBETRN.executeUpdate() == 1)
									cl_dat.M_flgLCUPD_pbst = true;
								else
									cl_dat.M_flgLCUPD_pbst = false;
							}
						}
					}
				///***
				}
			}
		///*** ADDED FOR CYLINDER
		//DELETION OF CYLINDERS
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
			{
					if(vtrOLDCL!=null)
					{
						if(vtrOLDCL.size()>0)
						{
							String L_strMATCD=null;
							for(int i=0;i<tblCYLDT.getRowCount();i++)
							{
								if(tblCYLDT.getValueAt(i,0).equals(Boolean.FALSE))
									continue;
								cl_dat.exeSQLUPD("Update MM_CLTRN set CLT_STSFL='X' where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and CLT_GRNNO='"+txtGRNNO.getText()+"' and CLT_MATCD='"+tblCYLDT.getValueAt(i,TBL_MATCD)+"' and CLT_SRLNO='"+tblCYLDT.getValueAt(i,2)+"'","setLCLUPD");
							}
						}
					}
			}
		//MODIFICATION OF CYLINDERS
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			{
				for(int i=0;i<tblCYLDT.getRowCount();i++)
				{
					if(tblCYLDT.getValueAt(i,0).equals(Boolean.FALSE))
						continue;
					if(vtrOLDCL.contains(tblCYLDT.getValueAt(i,1)+"|"+tblCYLDT.getValueAt(i,2)))
					{
						M_strSQLQRY="Update MM_CLTRN set CLT_RFDAY="+tblCYLDT.getValueAt(i,3)+","+getUSGDTL("CLT",'U',"0")+" where "
							+" CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and  "
							+" CLT_GRNNO='"+txtGRNNO.getText()+"' and "
							+" CLT_MATCD='"+tblCYLDT.getValueAt(i,TBL_MATCD)+"' and "
							+" CLT_SRLNO='"+tblCYLDT.getValueAt(i,TBL_MATCD+1)+"' ";
					}
					else
					{
						M_strSQLQRY="Insert into MM_CLTRN (CLT_CMPCD,CLT_GRNNO, CLT_STRTP, CLT_MATCD, CLT_SRLNO, CLT_GRNDT, CLT_VENTP, CLT_VENCD, CLT_RFDAY, CLT_ISSNO, CLT_RTSDT, CLT_MGPNO,CLT_MGPDT,CLT_RNTRT,CLT_BILRF, CLT_RNTAM,CLT_TRNFL,CLT_STSFL,CLT_LUSBY,CLT_LUPDT) values ("
							+"'"+cl_dat.M_strCMPCD_pbst+"',"
							+"'"+ strGRNNO+"',"
							+"'"+strSTRTP+"',"
							+"'"+tblCYLDT.getValueAt(i,1)+"',"
							+"'"+tblCYLDT.getValueAt(i,2)+"',"
							+"'"+strGRNDT+"',"
							+"'S',"
							+"'"+strVENCD+"',"
							+tblCYLDT.getValueAt(i,3)+","
							+"null,null,null,null,null,null,null,"
							+getUSGDTL("CLT",'I',"0")+")";
								
					}
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							   
				}
			}
		//ADDITION OF CYLINDERS
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)){
				Enumeration L_enmCYLDT=hstCYLDT.keys();
				Vector<String> L_vtrTEMP=null;
				String L_strMATCD=null;
				StringTokenizer L_stkTEMP=null;
				while(L_enmCYLDT.hasMoreElements())
				{
					L_strMATCD=(String)L_enmCYLDT.nextElement();
					L_vtrTEMP=(Vector<String>)hstCYLDT.get(L_strMATCD);
					for(int z=0;z<L_vtrTEMP.size();z++)
					{
						L_stkTEMP=new StringTokenizer((String)L_vtrTEMP.elementAt(z),"|");
						M_strSQLQRY="Insert into MM_CLTRN (CLT_CMPCD,CLT_GRNNO, CLT_STRTP, CLT_MATCD, CLT_SRLNO, CLT_GRNDT, CLT_VENTP, CLT_VENCD, CLT_RFDAY, CLT_ISSNO, CLT_RTSDT, CLT_MGPNO,CLT_MGPDT,CLT_RNTRT,CLT_BILRF, CLT_RNTAM,CLT_TRNFL,CLT_STSFL,CLT_LUSBY,CLT_LUPDT) values ("
							+"'"+cl_dat.M_strCMPCD_pbst+"',"
							+"'"+ strGRNNO+"',"
							+"'"+strSTRTP+"',"
							+"'"+L_strMATCD+"',"
							+"'"+L_stkTEMP.nextToken()+"',"
							+"'"+strGRNDT+"',"
							+"'S',"
							+"'"+strVENCD+"',"
							+L_stkTEMP.nextToken()+","
							+"null,null,null,null,null,null,null,"
							+getUSGDTL("CLT",'I',"0")+")";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}							
		///***END OF CYLINDER
			}
			if(cl_dat.M_flgLCUPD_pbst)
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					exeGRNNO(strGRNNO,strMATTP,LM_LUSBY,LM_LUPDT);
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("GRIN Updated successfully",'N');
				intINDRW =0;
				clrCOMP();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					JOptionPane.showMessageDialog(this,"Please note down GRIN No. " + strGRNNO,"Gate-In No.",JOptionPane.INFORMATION_MESSAGE); 
					txtGRNDT.setText(cl_dat.M_strLOGDT_pbst);
					setENBL(true);
					btnPRNT.setEnabled(true);
					txtBOENO.requestFocus();
				}
				else
				{
					setENBL(false);
					txtGRNNO.setEnabled(true);
					txtGRNNO.requestFocus();
				}	
				cmbMATTP.setEnabled(true);
				cmbGRNTP.setEnabled(true);
				if(hstINDNO !=null)
					hstINDNO.clear();
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG("GRIN could not be updated",'E');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
		}
	}
	// Check on validity of Gate-In No.
	private boolean vldGINNO(String LP_GINNO,String LP_BOENO){
		try{
			java.sql.Date L_datTMPDT;
			M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_TPRCD,WB_TPRDS,WB_CHLNO,WB_CHLDT,WB_ORDRF,";
			M_strSQLQRY += "WB_CHLQT,WB_UOMQT,WB_PRTCD,WB_PRTDS from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + LP_GINNO + "'";
	//REMOVED ON 03/09/2004 as MULTIPLE GRIN REQ. AGAINST ONE GATE IN. : API HBP/RSP
//			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
//				M_strSQLQRY += " and isnull(WB_DOCRF,'')=''";
            if(strSTRTP.equals(strTNSTR_fn))
			{
				//M_strSQLQRY += " and isnull(WB_DOCRF,'')=''";
                M_strSQLQRY += " and WB_ACPTG = 'A'";
			}
			if(!LP_BOENO.equals(""))
				M_strSQLQRY += " and WB_BOENO = '" + LP_BOENO + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				txtLRYNO.setText(nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),""));
				txtTRNCD.setText(nvlSTRVL(M_rstRSSET.getString("WB_TPRCD"),""));
				txtTRNNM.setText(nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),""));
				txtCHLNO.setText(nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),""));
				txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("WB_PRTCD"),""));
				txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),""));
				txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("WB_ORDRF"),""));
				L_datTMPDT = M_rstRSSET.getDate("WB_CHLDT");
				if(L_datTMPDT !=null)
					txtCHLDT.setText(M_fmtLCDAT.format(L_datTMPDT));
				else
					txtCHLDT.setText("");
                if(strSTRTP.equals(strTNSTR_fn))
					getWBDTL(strGINTP,LP_GINNO);
				else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
					getPODTL(txtPORNO.getText().trim(),true);
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			txtLRYNO.setText("");
			txtTRNCD.setText("");
			txtTRNNM.setText("");
			txtCHLNO.setText("");
			txtCHLDT.setText("");
			tblGINVL.clrTABLE();
			setMSG("Invalid Gate-In No.",'E');
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"vldGINNO");
		}	
		return false;
	}
	
	// Method to generate the new GRIN No.
	private String genGRNNO(String P_strMATTP){
		String L_GRNNO  = "",  L_CODCD = "", L_CCSVL = "";
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"GRN' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strGRNTR_fn + P_strMATTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = M_rstRSSET.getString("CMT_CODCD");
					L_CCSVL = M_rstRSSET.getString("CMT_CCSVL");
				}
				M_rstRSSET.close();
			}
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_GRNNO += "0";
			L_CCSVL = L_GRNNO + L_CCSVL;
			L_GRNNO = L_CODCD + L_CCSVL;
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"genGRNNO");
		}
		return L_GRNNO;
	}
	
	// Method to update the last GRIN No.in the CO_CDTRN
	private void exeGRNNO(String LP_GRNNO,String LP_MATTP,String LP_LUSBY,String LP_LUPDT)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CCSVL = '" + LP_GRNNO.substring(3) + "',";
			M_strSQLQRY += " CMT_TRNFL = '" + strTRNFL_fn + "',";			
			M_strSQLQRY += " CMT_LUSBY = '" + LP_LUSBY + "',";			
			M_strSQLQRY += " CMT_LUPDT = '" + LP_LUPDT+"'";			
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"GRN'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strGRNTR_fn + LP_MATTP + "'";			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}catch(Exception e){
			setMSG(e,"exeGRNNO");
		}
	}
	// Validation on Excise Material Category
	private boolean vldEXCCT(String LP_EXCCT)
	{
		try
		{
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
			M_strSQLQRY += " and CMT_CODCD = '" + LP_EXCCT + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldEXCCT");
			return false;
		}	
		return false;
	}
	
	// Method to return Material Description
	private String getMATDS(String LP_MATCD)
	{
		String L_MATDS = "";
		try
		{
			M_strSQLQRY = "Select ST_MATDS from MM_STMST";
			M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = '" + LP_MATCD + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
				L_MATDS = M_rstRSSET.getString("ST_MATDS");
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMATDS");
		}	
		return L_MATDS;
	}
	
	// Method to get the data corresponding to the given Gate-In No.
	private void getWBDTL(String LP_GINTP,String LP_GINNO)
	{
		try
		{
			int i = 0;
			double L_CHLQT,L_UOMQT;
			M_strSQLQRY = "Select WB_MATCD,ST_MATDS,ST_UOMCD,WB_MATTP,WB_CHLQT,WB_UOMQT";
			M_strSQLQRY += " from MM_WBTRN,MM_STMST";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + LP_GINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + LP_GINNO + "'";
			M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_strSQLQRY += " and WB_MATCD = ST_MATCD and WB_CMPCD = ST_CMPCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				tblGINVL.setValueAt(new Boolean(true),i,TBL_CHKFL);
				tblGINVL.setValueAt(M_rstRSSET.getString("WB_MATCD"),i,TBL_MATCD);
				tblGINVL.setValueAt(M_rstRSSET.getString("ST_MATDS"),i,TBL_MATDS);
				tblGINVL.setValueAt(M_rstRSSET.getString("ST_UOMCD"),i,TBL_UOMCD);
				tblGINVL.setValueAt(M_rstRSSET.getString("WB_MATTP"),i,TBL_EXCCT);
				L_CHLQT = M_rstRSSET.getDouble("WB_CHLQT");
				tblGINVL.setValueAt(setNumberFormat(L_CHLQT,3),i,TBL_CHLQT);
				L_UOMQT = M_rstRSSET.getDouble("WB_UOMQT");
				tblGINVL.setValueAt(setNumberFormat(L_UOMQT,3),i,TBL_RECQT);
				i++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"getWBDTL" );
		}
	}
	
	private void getPODTL(String P_strPORNO,boolean L_flgPRN)
	{
		try
		{
			int i = 0;
			double L_CHLQT,L_UOMQT;
            String L_strMATCD ="";
        	if(tblGINVL.isEditing())
				tblGINVL.getCellEditor().stopCellEditing();
			M_strSQLQRY = "Select PO_MATCD,ST_MATDS,PO_UOMCD,sum(isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0))L_QTY ";
            M_strSQLQRY += " from MM_POMST,MM_STMST";
			M_strSQLQRY += " where ST_STRTP=PO_STRTP and ST_MATCD=PO_MATCD and ST_CMPCD = PO_CMPCD and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '" + strSTRTP + "'";
			M_strSQLQRY += " and isnull(PO_PORTP,'') <>'03' AND PO_PORNO = '" + P_strPORNO + "' and isnull(po_stsfl,'') <>'X' group by PO_MATCD,ST_MATDS,PO_UOMCD ORDER BY PO_MATCD";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
            hstMATCD.clear();
			if(hstMATDS ==null)
				hstMATDS=new Hashtable<String,String>(10,0.75f);
			tblGINVL.clrTABLE();
            while(L_rstRSSET.next())
			{
               		 L_strMATCD = L_rstRSSET.getString("PO_MATCD");
				if(L_flgPRN)
				{
					tblGINVL.setValueAt(new Boolean(true),i,TBL_CHKFL);
             		   	tblGINVL.setValueAt(L_strMATCD,i,TBL_MATCD);
					tblGINVL.setValueAt(L_rstRSSET.getString("ST_MATDS"),i,TBL_MATDS);
					tblGINVL.setValueAt(L_rstRSSET.getString("PO_UOMCD"),i,TBL_UOMCD);
					tblGINVL.setValueAt(getLOCCD(L_strMATCD),i,TBL_LOCCD);
				}
             	      hstMATCD.put(L_strMATCD,L_rstRSSET.getString("L_QTY"));
		            hstMATDS.put(L_strMATCD,L_rstRSSET.getString("ST_MATDS"));
				i++;
			}
			if(L_rstRSSET != null)
				L_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"getPODTL");
		}
	}
	
	private boolean getINDTL(String P_strINDNO)
	{  // get indent details for cash purchase given indent
		try
		{
			double L_CHLQT,L_UOMQT;
			int L_intRECCT=0;
            String L_strMATCD ="";
			if(hstMATDS ==null)
				hstMATDS=new Hashtable<String,String>(10,0.75f);
			if(hstINDNO.get(P_strINDNO)==null)
			{
        		M_strSQLQRY = "Select in_indno,IN_MATCD,ST_MATDS,ST_UOMCD,(isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0))L_QTY,ST_LOCCD ";
				M_strSQLQRY += " from MM_INMST,MM_STMST";
				M_strSQLQRY += " where IN_STRTP = ST_STRTP AND IN_MATCD = ST_MATCD AND IN_CMPCD = ST_CMPCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '" + strSTRTP + "'";
				M_strSQLQRY += " and isnull(IN_INDTP,'') ='01' AND ";
				M_strSQLQRY += " isnull(IN_STSFL,'') = '4' AND ";
				M_strSQLQRY += " (isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)) >0 AND ";
				M_strSQLQRY += " IN_INDNO = '" + P_strINDNO + "'";
				if(hstINDNO !=null)
					hstINDNO.put(P_strINDNO,"");
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
					L_strMATCD = M_rstRSSET.getString("IN_MATCD");
					tblGINVL.setValueAt(new Boolean(true),intINDRW,TBL_CHKFL);
					tblGINVL.setValueAt(P_strINDNO,intINDRW,TBL_INDNO);
					tblGINVL.setValueAt(L_strMATCD,intINDRW,TBL_MATCD);
					tblGINVL.setValueAt(M_rstRSSET.getString("ST_UOMCD"),intINDRW,TBL_UOMCD);
					tblGINVL.setValueAt(M_rstRSSET.getString("ST_MATDS"),intINDRW,TBL_MATDS);
					tblGINVL.setValueAt(M_rstRSSET.getString("ST_LOCCD"),intINDRW,TBL_LOCCD);
					hstMATCD.put(L_strMATCD,M_rstRSSET.getString("L_QTY"));
					hstMATDS.put(L_strMATCD,M_rstRSSET.getString("ST_MATDS"));
					intINDRW++;
					L_intRECCT++;
				}
				txtINDNO.setText("");
			}
			else
			{
				setMSG("Duplicate Indent Number..",'E');
				txtINDNO.setText("");
				return false;
			}
			if(L_intRECCT ==0)
			{
				setMSG("Invalid Indent Number..",'E');
				return false;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"getINDTL");
		}
		return true;
	}
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input == txtPORNO)
				{
					if(txtPORNO.getText().trim().length() ==8)
                        if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))					
	                        if(!vldPORNO(txtPORNO.getText().trim()))				
							    return false;
				}
				else if(input == txtINDNO)
				{
					if(txtINDNO.getText().trim().length() ==8)
						if(!getINDTL(txtINDNO.getText().trim()))				
							return false;
				}
				else if(input == txtGINNO)
				{
					if(txtGINNO.getText().trim().length() ==8)
						if(!vldGINNO(txtGINNO.getText().trim(),txtBOENO.getText().trim()))				
							return false;
				}
				else if(input == txtGRNDT)
				{
					if(M_fmtLCDAT.parse(txtGRNDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{							
						setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
						return false;
					}
				}
				else if(input == txtCHLDT)
				{
					if(M_fmtLCDAT.parse(txtCHLDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{							
						setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
						return false;
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
			}
			return true;	
		}
	}
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			if(P_intCOLID==TBL_EXCCT)
			{
	            strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_EXCCT).toString();
				if(strTEMP.length()>0)
				   if(!vldEXCCT(strTEMP))
						return false;
			}
			else if(P_intCOLID==TBL_MATCD)
			{	
				try
				{
					strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString();
			    	if(strTEMP.length()>0)
					{
						/*for(int i=0;i<=P_intROWID-1;i++)
						{
							if(tblGINVL.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
							if(tblGINVL.getValueAt(i,TBL_MATCD).toString().trim().equals(strTEMP.trim()))
							{
								setMSG("Duplicate entry ..",'E');
								return false;
							}
						}*/
						if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
						{
							M_strSQLQRY = "Select PO_MATCD,ST_MATDS,PO_UOMCD from MM_POMST,MM_STMST";
							M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STSFL <> 'X'";
							M_strSQLQRY += " and ST_STRTP = PO_STRTP and PO_MATCD = ST_MATCD AND PO_CMPCD=ST_CMPCD ";
							M_strSQLQRY += " and PO_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
							M_strSQLQRY += " and isnull(PO_PORTP,'') <>'03' AND PO_PORNO = '" + txtPORNO.getText().trim() + "'";
							M_strSQLQRY += " and PO_MATCD = '" + strTEMP + "'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							//System.out.println("inside verifier");
							if(M_rstRSSET != null)
							if(M_rstRSSET.next())
							{
								//tblGINVL.setValueAt(M_rstRSSET.getString("PO_UOMCD"),intTBLRW,TBL_UOMCD);
								tblGINVL.setValueAt(M_rstRSSET.getString("PO_UOMCD"),P_intROWID,TBL_UOMCD);
								String L_strTEMP= M_rstRSSET.getString("ST_MATDS");
								tblGINVL.setValueAt(M_rstRSSET.getString("ST_MATDS"),P_intROWID,TBL_MATDS);// added
								M_rstRSSET.close();
							//	System.out.println("closed");
								return true;
							}
						}
						// check
						else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
						{
							M_strSQLQRY = "Select ST_MATCD,ST_MATDS,ST_UOMCD from MM_STMST";
							M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,' ') <> 'X'";
							M_strSQLQRY += " and ST_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
							M_strSQLQRY += " and ST_MATCD = '" + strTEMP + "'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							if(M_rstRSSET.next()){
								tblGINVL.setValueAt(M_rstRSSET.getString("ST_UOMCD"),intTBLRW,TBL_UOMCD);
								tblGINVL.setValueAt(M_rstRSSET.getString("ST_MATDS"),intTBLRW,TBL_MATDS);
								//**** FOR CYLINDER ENTRY
								String L_strTEMP= M_rstRSSET.getString("ST_MATDS");
								if((strTEMP.substring(0,2).equals("83"))||(strTEMP.substring(0,4).equals("6810")))
								if(!vtrMATCD.contains(new String[]{strTEMP,L_strTEMP}))
									vtrMATCD.addElement(new String[]{strTEMP,L_strTEMP});
								hstMATDS.put(strTEMP,L_strTEMP);
								//****
								M_rstRSSET.close();
								return true;
							}
						}
						else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strJBWGR_fn))
						{
						    M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST";
							M_strSQLQRY += " where isnull(CT_STSFL,' ') <> 'X'";
							M_strSQLQRY += " and CT_MATCD = '" + strTEMP + "'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							if(M_rstRSSET.next())
							{
								tblGINVL.setValueAt(M_rstRSSET.getString("CT_UOMCD"),intTBLRW,TBL_UOMCD);
								tblGINVL.setValueAt(M_rstRSSET.getString("CT_MATDS"),intTBLRW,TBL_MATDS);
								return true;
							}
							
						}
						if(M_rstRSSET != null)
							M_rstRSSET.close();
						tblGINVL.setValueAt("",P_intROWID,TBL_UOMCD);
						setMSG("Invalid Item Code,,",'E');
						return false;
					}
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"vldMATCD");
					return false;
				}
			}
		    else if(P_intCOLID==TBL_CHLQT)
			{
				///strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_CHLQT).toString();
				strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString();
				//System.out.println("1");
				if(strTEMP.length()>0)
				{
					//System.out.println(getQTY("CHL",strTEMP));
					//System.out.println(hstMATCD);
					if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
					if(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString()) < getQTY("CHL",strTEMP))
					     setMSG("Challan qty. can not be greater than the pending order qty..",'E');
				}
			}
		    else if(P_intCOLID==TBL_RECQT)
			{
			  strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString();
				if(strTEMP.length()>0)
				{
		    		if(Float.parseFloat(tblGINVL.getValueAt(P_intROWID,TBL_CHLQT).toString()) < getQTY("CHL",strTEMP))
					       setMSG("Recieved qty. can not be greater than the Challan qty..",'E');
		            if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))  
					if(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString()) < getQTY("RCT",strTEMP))
		                   setMSG("Recieved qty. can not be greater than the pending order qty..",'E');
				} 
			}
			else if(P_intCOLID==TBL_BATNO)
			{	
				try
				{
					strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()+tblGINVL.getValueAt(P_intROWID,TBL_BATNO).toString();
			    		if(strTEMP.length()>0)
					{
						for(int i=0;i< tblGINVL.getRowCount();i++)
						{
							if((tblGINVL.getValueAt(i,TBL_MATCD).toString()+tblGINVL.getValueAt(i,TBL_BATNO).toString()).trim().length() >0)
							if((tblGINVL.getValueAt(i,TBL_MATCD).toString()+tblGINVL.getValueAt(i,TBL_BATNO).toString()).trim().equals(strTEMP.trim()))
							{
								if(i != P_intROWID)
								{ 
									setMSG("Duplicate entry ..",'E');
									return false;
								}
							}
						}
					}
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"vldMATCD");
					return false;
				}

			}	

		///*** ADDED BY AAP FOR CYLINDER
			else if( getSource() == tblCYLDT)
			{
				if(P_intCOLID == 1)
				{
					boolean L_flgRTNVL=false;
					for(int i=0;i<tblGINVL.getRowCount();i++)
					{
						if(tblGINVL.getValueAt(i,TBL_MATCD).equals(tblCYLDT.getValueAt(P_intROWID,P_intCOLID)))
						{
							for(int j=0;j<staCYLCD.length;j++)
								if(tblCYLDT.getValueAt(P_intROWID,P_intCOLID).toString().substring(0,staCYLCD[j].length()).equals(staCYLCD[j]))
									L_flgRTNVL=true;
						}
					}
					if(!L_flgRTNVL)
						setMSG("Invalid material code ..",'E');
					return L_flgRTNVL;
				}
			}
	///***
			return true;
		}
	}
	
	private void exeINSREC()
	{
		M_strSQLQRY = "Insert into MM_GRMST(GR_CMPCD,GR_STRTP,GR_GRNTP,GR_GRNNO,GR_AMDNO,GR_GRNDT,";
		M_strSQLQRY += "GR_MATTP,GR_GINNO,GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_CHLQT,GR_BOENO,";      
		M_strSQLQRY += "GR_CNSNO,GR_PORNO,GR_VENCD,GR_VENNM,GR_TRNCD,GR_TRNNM,";      
	    M_strSQLQRY += "GR_MATCD,GR_RECQT,";
	//    if(strSTRTP.equals(strTNSTR_fn))
	//		M_strSQLQRY +="GR_ACPQT,";
		M_strSQLQRY += "GR_MODFL,GR_STSFL,GR_TRNFL,GR_LUSBY,GR_LUPDT,GR_EXCCT,GR_BATNO) values (";
		M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
		M_strSQLQRY += "'" + strSTRTP + "',";
		M_strSQLQRY += "'" + strGRNTP + "',";
		M_strSQLQRY += "'" + strGRNNO + "',";
		M_strSQLQRY += "'" + strAMDNO + "',";
		M_strSQLQRY += "current_date,";			
		// GRIN DATE modification blocked on 29/01/2004
		//M_strSQLQRY += "'" + strGRNDT + "',";
		M_strSQLQRY += "'" + strMATTP + "',";
		M_strSQLQRY += "'" + strGINNO + "',";
		M_strSQLQRY += "'" + strLRYNO + "',";
		M_strSQLQRY += "'" + strCHLNO + "',";
		M_strSQLQRY += "'" + strCHLDT + "',";
		M_strSQLQRY += strCHLQT + ",";
		M_strSQLQRY += "'" + strBOENO + "',";
		M_strSQLQRY += "'" + strCNSNO + "',";
		if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
			M_strSQLQRY += "'" + strINDNO + "',";
		else
	//	if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
			M_strSQLQRY += "'" + strPORNO + "',";
		
		M_strSQLQRY += "'" + strVENCD + "',";
		M_strSQLQRY += "'" + strVENNM + "',";
		M_strSQLQRY += "'" + strTRNCD + "',";
		M_strSQLQRY += "'" + strTRNNM + "',";
		M_strSQLQRY += "'" + strMATCD + "',";
		M_strSQLQRY += strRECQT + ",";
	//	if(strSTRTP.equals(strTNSTR_fn))
	//		M_strSQLQRY += strCHLQT + ",";
		M_strSQLQRY += "'" + strMODFL + "','0',";
		M_strSQLQRY += "'" + strTRNFL_fn + "',";
	    M_strSQLQRY += "'" + LM_LUSBY + "',";
		M_strSQLQRY += "'" + LM_LUPDT + "',";
		M_strSQLQRY += "'" + strEXCCT + "',";
		M_strSQLQRY += "'" + strBATNO+ "')";
	}
	private float getQTY(String P_strQTYTP,String P_strMATCD) 
	{
		float L_fltTOTQT =0;
		try
		{
			int L_intCOLID =0; 
			if(P_strQTYTP.equals("CHL"))
				L_intCOLID = TBL_CHLQT;
			if(P_strQTYTP.equals("RCT"))
				L_intCOLID = TBL_RECQT;
			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if((tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true")) && (tblGINVL.getValueAt(i,TBL_MATCD).toString().equals(P_strMATCD)))
					if(tblGINVL.getValueAt(i,L_intCOLID).toString().length() >0)
						L_fltTOTQT += Float.parseFloat(tblGINVL.getValueAt(i,L_intCOLID).toString());
			}
			//System.out.println("Qty "+ P_strQTYTP + " "+L_fltTOTQT);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getQTY");		
		}
		return L_fltTOTQT;

	}
	private String getLOCCD(String P_strMATCD)
	{
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "SELECT ST_LOCCD from MM_STMST where ST_STRTP ='"+strSTRTP+"'";
			M_strSQLQRY += " AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD ='"+P_strMATCD+"'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET !=null)
				if(L_rstRSSET.next())
				{
					String L_strRTNVL=nvlSTRVL(L_rstRSSET.getString("ST_LOCCD"),"");
					L_rstRSSET.close();
					return L_strRTNVL;
				}
		}
		catch(SQLException L_E)
		{
			setMSG(L_E,"getLOCCD");
		}
		return "";
	}
}
