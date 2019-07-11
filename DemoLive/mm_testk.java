//Stock Master 
import java.util.Calendar;import java.util.StringTokenizer;
import java.util.Vector;
import java.sql.*;
import java.awt.Color;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.*;
//import javax.swing.event.*;
import javax.swing.border.*;
import java.sql.SQLException;
import java.sql.ResultSet;

class mm_testk extends cl_pbase
{/**TEXT FIELD FOR MATERIAL CODE WITH LIMIT  */
	private TxtLimit txtMATCD;/** */
	private JTextField txtMATDS;/** */
	private JTextField txtMATTP;/** */
	private JTextField txtUOMCD;/** */
	private JTextField txtPKGTP;/** */
	private JTextField txtMINLV;/** */
	private JTextField txtMAXLV;/** *///TEXT FIELDS FOR MATERIAL DETAILS
	private JTextField txtSRPQT;/** */
	private JTextField txtRORLV;/** */
	private JTextField txtRORQT;/** */
	private JTextField txtSTODT;/** */
	private JTextField txtLOCCD;/** */
	private JTextField txtILDTM;/** */
	private JTextField txtELDTM;/** */
	private JTextField txtPSVDT;/** */
	//Text Fields for previous Transactions
	private JTextField txtPGRNO;/** */
	private JTextField txtPGRDT;/** */
	private JTextField txtPISNO;/** */
	private JTextField txtPISDT;/** */
	private JTextField txtPMRNO;/** */
	private JTextField txtPMRDT;/** */
	private JTextField txtPSNNO;/** */
	private JTextField txtPSNDT;/** */
	private JTextField txtPSTNO;/** */
	private JTextField txtPSTDT;/** */
	private JTextField txtPPONO;/** */
	private JTextField txtPPOVL;/** */
	private JTextField txtYTDGR;/** */
	private JTextField txtYTDIS;/** */
	private JTextField txtYTDMR;/** */
	private JTextField txtYTDSN;/** */
	private JTextField txtYTDST;/** */
	private JTextField txtSTKQT;/** */
	private JTextField txtSTKIP;/** */
	private JTextField txtSTKIN;/** */
	private JTextField txtSTKOR;/** */
	private JTextField txtMDVQT;/** */
	private JTextField txtYOPST;/** */
	private JTextField txtABCFL;/** */
	private JTextField txtHMLFL;/** */
	private JTextField txtVEDFL;/** */
	private JTextField txtXYZFL;/** */
	private JTextField txtMDVVL;/** */
	private JTextField txtYOPVL;/** */
	
	private JLabel lblPRVTR;/** */
	private JLabel lblYTODT;/** */										//LABEL FOR PREVIOUS DETAILS
	private JCheckBox chbSTKFL;/** */
	private JCheckBox chbSTSFL;/** */
	private JCheckBox chbPSVFL;/** */							//CHECKBOX
	///***ADDED BY AAP ON 29/05/04
	private cl_Combo cmbMNGRP;
	private cl_Combo cmbSSGRP;
	private JButton btnFIRST;
	private JButton btnPREV;
	private JButton btnNEXT;
	private JButton btnLAST;
	private JButton btnFIND;
	private JTextField txtPRTDS;
	private Vector<String> vtrSSGRP;
	private Statement stmTESTK;
	private int intROWID=-1;
	ResultSet L_rstRSSET;
	String [][] staQRDAT;
	///***
	private JPanel pnlPRVTR;/** */										//PANEL FOR PREVIOUS TRANSACTION DETAILS
	private String strSTRTP="";/** */
	private String strCLSNM="";/** To identify the program being used */
	private boolean flgERROR = false;
	mm_testk()
	{
		super(2);
		pnlPRVTR = new JPanel(null);
		pnlPRVTR.setBorder(BorderFactory.createTitledBorder("Previous Transaction Details"));
		lblPRVTR = new JLabel("Reference No.");
		lblYTODT = new JLabel(" Yr.To Dt.Values");
//		lblPRVTR.setForeground(Color.blue);
//		lblYTODT.setForeground(Color.blue);
		
		txtMATCD = new TxtLimit(10);	txtMATDS = new JTextField();
		txtMATTP = new JTextField();	txtUOMCD = new JTextField();
		txtPKGTP = new JTextField();	txtMINLV = new JTextField();
		txtMAXLV = new JTextField();	txtSRPQT = new JTextField();
		txtRORLV = new JTextField();	txtRORQT = new JTextField();	
		txtSTODT = new JTextField();	txtLOCCD = new TxtLimit(10);
		txtILDTM = new JTextField();	txtELDTM = new JTextField();
		txtPSVDT = new JTextField();
		
		txtPGRNO = new JTextField();	txtPGRDT = new JTextField();
		txtPISNO = new JTextField();	txtPISDT = new JTextField();
		txtPMRNO = new JTextField();	txtPMRDT = new JTextField();
		txtPSNNO = new JTextField();	txtPSNDT = new JTextField();
		txtPSTNO = new JTextField();	txtPSTDT = new JTextField();
		txtPPONO = new JTextField();	txtPPOVL = new JTextField();
		
		txtYTDGR = new JTextField();	txtYTDIS = new JTextField();
		txtYTDMR = new JTextField();	txtYTDSN = new JTextField();
		txtYTDST = new JTextField();	txtSTKQT = new JTextField();	
		txtSTKIP = new JTextField();	txtSTKIN = new JTextField();	
		txtSTKOR = new JTextField();	txtMDVQT = new JTextField();
		txtYOPST = new JTextField();	txtYOPVL = new JTextField();
		txtABCFL = new JTextField();	txtHMLFL = new JTextField();	
		txtVEDFL = new TxtLimit(1);		txtXYZFL = new JTextField();
		txtMDVVL = new JTextField();	txtYOPVL = new JTextField();
			
		
		chbSTKFL = new JCheckBox("Stock Item");		chbSTSFL = new JCheckBox("Obsolete Tag");
		chbPSVFL = new JCheckBox("Preservation Tag");
		JPanel L_pnlSTKDT=new JPanel(null);
		setMatrix(20,7);
		add(lblPRVTR,1,2,1,1.3,pnlPRVTR,'L');
		add(new JLabel("        \t     Date"),1,3,1,1,pnlPRVTR,'L');
		add(lblYTODT,1,4,1,1,pnlPRVTR,'L');
						
		add(new JLabel("     Receipt"),2,1,1,1,pnlPRVTR,'L');
		add(txtPGRNO,2,2,1,1.3,pnlPRVTR,'R');
		add(txtPGRDT,2,3,1,1,pnlPRVTR,'L');
		add(txtYTDGR,2,4,1,0.93,pnlPRVTR,'L');
		add(new JLabel("    Stk. On Hand"),2,1,1,1,L_pnlSTKDT,'R');
		add(txtSTKQT,2,2,1,1,L_pnlSTKDT,'L');
		add(new JLabel(" ABC"),2,3,1,0.5,L_pnlSTKDT,'L');
		add(txtABCFL,2,3,1,0.7,L_pnlSTKDT,'R');
		
		add(new JLabel("     Issue"),3,1,1,1,pnlPRVTR,'L');
		add(txtPISNO,3,2,1,1.3,pnlPRVTR,'R');
		add(txtPISDT,3,3,1,1,pnlPRVTR,'L');
		add(txtYTDIS,3,4,1,0.93,pnlPRVTR,'L');
		add(new JLabel("    Stk. On Insp."),3,1,1,1,L_pnlSTKDT,'L');
		add(txtSTKIP,3,2,1,1,L_pnlSTKDT,'L');
		add(new JLabel(" HML"),3,3,1,1,L_pnlSTKDT,'L');
		add(txtHMLFL,3,3,1,0.7,L_pnlSTKDT,'R');
		
		add(new JLabel("     MRN"),4,1,1,1,pnlPRVTR,'L');
		add(txtPMRNO,4,2,1,1.3,pnlPRVTR,'R');
		add(txtPMRDT,4,3,1,1,pnlPRVTR,'L');
		add(txtYTDMR,4,4,1,0.93,pnlPRVTR,'L');
		add(new JLabel("    Stk. On Indent"),4,1,1,1,L_pnlSTKDT,'L');
		add(txtSTKIN,4,2,1,1,L_pnlSTKDT,'L');
		add(new JLabel(" VED"),4,3,1,1,L_pnlSTKDT,'L');
		add(txtVEDFL,4,3,1,0.7,L_pnlSTKDT,'R');
		
		add(new JLabel("     SAN"),5,1,1,1,pnlPRVTR,'L');
		add(txtPSNNO,5,2,1,1.3,pnlPRVTR,'R');
		add(txtPSNDT,5,3,1,1,pnlPRVTR,'L');
		add(txtYTDSN,5,4,1,0.93,pnlPRVTR,'L');	
		add(new JLabel("    Stk. On Order"),5,1,1,1,L_pnlSTKDT,'L');
		add(txtSTKOR,5,2,1,1,L_pnlSTKDT,'L');
		add(new JLabel(" XYZ"),5,3,1,1,L_pnlSTKDT,'L');
		add(txtXYZFL,5,3,1,0.7,L_pnlSTKDT,'R');
		
		add(new JLabel("     STN"),6,1,1,1,pnlPRVTR,'L');
		add(txtPSTNO,6,2,1,1.3,pnlPRVTR,'R');
		add(txtPSTDT,6,3,1,1,pnlPRVTR,'L');
		add(txtYTDST,6,4,1,0.93,pnlPRVTR,'L');
		add(new JLabel("    M. Vat. Qty."),6,1,1,1,L_pnlSTKDT,'L');
		add(txtMDVQT,6,2,1,1,L_pnlSTKDT,'L');
		add(new JLabel(" Val."),6,3,1,1,L_pnlSTKDT,'L');
		add(txtMDVVL,6,3,1,0.7,L_pnlSTKDT,'R');
											 
		add(new JLabel("     P.O. No."),7,1,1,1,pnlPRVTR,'L');
		add(txtPPONO,7,2,1,1.3,pnlPRVTR,'R');
		add(txtPPOVL,7,3,1,1,pnlPRVTR,'L');
		add(new JLabel("   Year Op. Stk."),7,1,1,1,L_pnlSTKDT,'R');
		add(txtYOPST,7,2,1,1,L_pnlSTKDT,'L');
		add(new JLabel(" Val."),7,3,1,1,L_pnlSTKDT,'L');
		add(txtYOPVL,7,3,1,0.7,L_pnlSTKDT,'R');
		
		
		setMatrix(20,6);
		int L_intROWID=1;
		///*** ADDED BY AAP ON 29/05/04
		JPanel L_pnlSERCH=new JPanel(null);
		setMatrix(20,12);
		add(cmbMNGRP=new cl_Combo(),1,1,1,3,L_pnlSERCH,'L');
		add(cmbSSGRP=new cl_Combo(),1,4,1,3,L_pnlSERCH,'L');
		cmbMNGRP.addItem("Select Group","");
		cmbSSGRP.addItem("Select SubSubGroup","");
		add(new JLabel("     Partial Desc."),1,7,1,2,L_pnlSERCH,'L');
		add(txtPRTDS=new JTextField(),1,9,1,4,L_pnlSERCH,'L');
		L_pnlSERCH.setBorder(BorderFactory.createTitledBorder("Search Criteria"));
		add(L_pnlSERCH,L_intROWID++,1,2,12.1,this,'L');
		//L_intROWID++;
		
		setMatrix(20,18);
		JPanel L_pnlBTNPL=new JPanel(null);
		M_intVSTRT=5;
		add(btnFIND=new JButton("Find"),1,2,1,2,L_pnlBTNPL,'L');
		setMatrix(20,12);
		M_intVSTRT=5;
		add(btnFIRST=new JButton("<<"),1,4,1,1,L_pnlBTNPL,'L');
		add(btnPREV=new JButton("<"),1,5,1,1,L_pnlBTNPL,'L');
		add(btnNEXT=new JButton(">"),1,6,1,1,L_pnlBTNPL,'L');
		add(btnLAST=new JButton(">>"),1,7,1,1,L_pnlBTNPL,'L');
		L_pnlBTNPL.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setMatrix(18,13);
		add(L_pnlBTNPL,++L_intROWID,10,1.4,8.4,this,'R');
		setMatrix(20,6);
		++L_intROWID;
		///***
		add(new JLabel("   Material Code"),++L_intROWID,1,1,1,this,'L');
		add(txtMATCD,L_intROWID,2,1,1,this,'L');
		add(txtMATDS,L_intROWID,3,1,4,this,'L');
		
		add(new JLabel("   Material Type"),++L_intROWID,1,1,1,this,'L');
		add(txtMATTP,L_intROWID,2,1,1,this,'L');
		add(new JLabel("   UOM"),L_intROWID,3,1,1,this,'L');
		add(txtUOMCD,L_intROWID,4,1,1,this,'L');
		add(new JLabel("Std. Packing"),L_intROWID,5,1,1,this,'L');
		add(txtPKGTP,L_intROWID,6,1,1,this,'L');
		
		add(new JLabel("   Min. Level"),++L_intROWID,1,1,1,this,'L');
		add(txtMINLV,L_intROWID,2,1,1,this,'L');
		add(new JLabel("   Max. Level"),L_intROWID,3,1,1,this,'L');
		add(txtMAXLV,L_intROWID,4,1,1,this,'L');
		add(new JLabel("Surplus Qty."),L_intROWID,5,1,1,this,'L');
		add(txtSRPQT,L_intROWID,6,1,1,this,'L');
		
		add(new JLabel("   Reorder Level"),++L_intROWID,1,1,1,this,'L');
		add(txtRORLV,L_intROWID,2,1,1,this,'L');
		add(new JLabel("   Reorder Qty."),L_intROWID,3,1,1,this,'L');
		add(txtRORQT,L_intROWID,4,1,1,this,'L');
		add(new JLabel("Stock Out Dt."),L_intROWID,5,1,1,this,'L');
		add(txtSTODT,L_intROWID,6,1,1,this,'L');
		
		add(new JLabel("   Location"),++L_intROWID,1,1,1,this,'L');
		add(txtLOCCD,L_intROWID,2,1,1,this,'L');
		add(new JLabel("   Int. Lead Time"),L_intROWID,3,1,1,this,'L');
		add(txtILDTM,L_intROWID,4,1,1,this,'L');
		add(new JLabel("Ext. Lead Time"),L_intROWID,5,1,1,this,'L');
		add(txtELDTM,L_intROWID,6,1,1,this,'L');
		
		//add(new JLabel("   Stock Item"),7,1,1,1,this,'L');
		add(chbSTKFL,++L_intROWID,1,1,1,this,'L');
		//add(new JLabel(" Obsoltes Tag"),7,2,1,1,this,'L');
		add(chbSTSFL,L_intROWID,2,1,1,this,'L');
		//add(new JLabel("   Preservation Tag"),7,3,1,1,this,'L');
		add(chbPSVFL,L_intROWID,4,1,1,this,'L');
		add(new JLabel("Due Date"),L_intROWID,5,1,1,this,'L');
		add(txtPSVDT,L_intROWID,6,1,1,this,'L');
		setMatrix(20,7);
		L_pnlSTKDT.setBorder(BorderFactory.createTitledBorder("Stock Status"));
		add(pnlPRVTR,11,1,8,4,this,'L');
		add(L_pnlSTKDT,11,5,8,3,this,'L');

		///***ADDED BY AAP 29/05/04
		try
		{
			M_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CTMST where CT_CODTP in ('MG','SS')");
			if(M_rstRSSET!=null)
			{
				vtrSSGRP=new Vector<String>(50,10);
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("CT_CODTP").equals("MG"))
						cmbMNGRP.addItem(M_rstRSSET.getString("CT_GRPCD")+" : "+M_rstRSSET.getString("CT_MATDS"),M_rstRSSET.getString("CT_GRPCD"));
					else
						vtrSSGRP.addElement(M_rstRSSET.getString("CT_GRPCD")+"|"
								+M_rstRSSET.getString("CT_MATCD").substring(0,6)+" : "+M_rstRSSET.getString("CT_MATDS")+"|"
								+M_rstRSSET.getString("CT_MATCD").substring(0,6));
				}
				M_rstRSSET.close();
			}
//			System.out.println("iso  "+cl_dat.M_conSPDBA_pbst.getTransactionIsolation());
//			cl_dat.M_conSPDBA_pbst.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			stmTESTK=cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY  );
		}catch(Exception e)
		{setMSG(e,"Child.Constructor");}
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		//Action Perform on combo option
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst){
			txtLOCCD.setEnabled(false);
			txtVEDFL.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)){
				setENBL(false);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)){
				setENBL(false);
				strCLSNM = getClass().getName();
				if(strCLSNM.equals("mm_testk"))
				{
					txtMATCD.setEnabled(true);
					txtMINLV.setEnabled(true);
					txtMAXLV.setEnabled(true);
	
					txtLOCCD.setEnabled(true);
					txtVEDFL.setEnabled(true);
					txtRORLV.setEnabled(true);
					txtRORQT.setEnabled(true);
					txtSRPQT.setEnabled(true);
					chbSTKFL.setEnabled(true);
					chbSTSFL.setEnabled(true);
				}
				else if(strCLSNM.equals("mm_test1"))
				{
					txtMATCD.setEnabled(true);
					txtMINLV.setEnabled(true);
					txtMAXLV.setEnabled(true);
				}
				txtMATCD.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)){
				setENBL(false);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				///*** ADDED BY AAP 29/05/04
				//Enable search fields in case of enquirey
				btnFIND.setEnabled(true);btnFIRST.setEnabled(true);btnNEXT.setEnabled(true);
				btnPREV.setEnabled(true);btnLAST.setEnabled(true);cmbMNGRP.setEnabled(true);
				cmbSSGRP.setEnabled(true);txtPRTDS.setEnabled(true);
				///***
				setMSG("Please Ener Material Code OR F1 For Help",'N');
				txtMATCD.setEnabled(true);
				cmbMNGRP.requestFocus();
				clrCOMP(); 
			}
		}
		///**** ADDED BY AAP 29/05/04
		if(M_objSOURC == btnFIND)//GET DATA FROM BACK END
			getDATA();
		else if(M_objSOURC == btnNEXT)//GO TO NEXT RECORD
			dspDATA(++intROWID);
		else if(M_objSOURC == btnPREV)//GO TO PREVIOUS RECORD
			dspDATA(--intROWID);
		else if(M_objSOURC == btnFIRST)//GO TO FIRST RECORD
			dspDATA(0);
		else if(M_objSOURC == btnLAST)//GO TO LAST RECORD
			dspDATA(staQRDAT.length-1);
	}
	public void keyPressed(KeyEvent L_KE){
		super.keyPressed(L_KE);
		int key = L_KE.getKeyCode();
		//Key Event For F1
		if(M_objSOURC == txtMATCD)
		{
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				String L_strADDQR="";
				if(txtMATCD.getText().length()>0)
					L_strADDQR=" and ST_MATCD like '"+txtMATCD.getText()+"%' ";
				M_strSQLQRY="SELECT ST_MATCD,ST_MATDS FROM MM_STMST,CO_CTMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = CT_MATCD and ST_MMSBS='"+M_strSBSCD+"' ";
				M_strSQLQRY+= " AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"' and isnull(CT_STSFL,'') not in ('9','X') "+L_strADDQR+" order by ST_MATCD";
				M_strHLPFLD = "txtMATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"MATERIAL CODE","DESCRIPTION"},2,"CT",new int[]{110,400});
			}
			else if(L_KE.getKeyCode()==L_KE.VK_F2)
			{
				String L_strADDQR="";
				if(txtMATCD.getText().length()>0)
					L_strADDQR=" and ST_MATCD like '"+txtMATCD.getText()+"%' ";
				M_strSQLQRY="SELECT ST_MATCD,ST_MATDS FROM MM_STMST,CO_CTMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = CT_MATCD AND st_MMSBS='"+M_strSBSCD+"' ";
				M_strSQLQRY+=" AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"' and isnull(CT_STSFL,'') not in ('9','X') "+L_strADDQR+" order by ST_MATDS";
				M_strHLPFLD = "txtMATCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"MATERIAL CODE","DESCRIPTION"},2,"CT",new int[]{110,400});
			}
			///*** ADDED BY AAP 31/05/04
			else if(L_KE.getKeyCode()==L_KE.VK_DOWN && intROWID<staQRDAT.length-1)
				dspDATA(++intROWID);//GO TO NEXT RECORD
			else if(L_KE.getKeyCode()==L_KE.VK_UP && intROWID>0)
				dspDATA(--intROWID);//GO TO PRECIOUS RECORD
			else if(L_KE.getKeyCode()==L_KE.VK_END)
				dspDATA(staQRDAT.length-1);//GO TO LAST RECORD
			else if(L_KE.getKeyCode()==L_KE.VK_HOME)
				dspDATA(0);//GO TO FIRST RECORD
			///*** 
		}
		//Key Event For Enter Key
		if(L_KE.getKeyCode()==L_KE.VK_ENTER){
			
			strSTRTP = M_strSBSCD.substring(2,4);
			if(M_objSOURC == txtMATCD){
				exeSELREC();
			}
			if(M_objSOURC == txtMINLV)
			{
				txtMAXLV.requestFocus();
			}
			if(M_objSOURC == txtMAXLV)
			{
				txtRORLV.requestFocus();
			}
			if(M_objSOURC == txtRORLV)
			{
				txtRORQT.requestFocus();
			}
			if(M_objSOURC == txtRORQT)
			{
				txtLOCCD.requestFocus();
			}
			if(M_objSOURC == txtLOCCD)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			
		}
	}
	
	//Event when  enter or click on ok button of help window
	void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
		cl_dat.M_wndHLP_pbst=null;
		StringTokenizer L_stkTEMP=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD.equals("txtMATCD"))
		{
			txtMATCD.setText(L_stkTEMP.nextToken());
			txtMATCD.requestFocus();
			exeSELREC();
		}
	}
	private void exeSELREC()
	{
		java.sql.Date datTEMP;
		strSTRTP = M_strSBSCD.substring(2,4);
		if(txtMATCD.getText().length() !=0)
		{
			try
			{
				flgERROR = false;
				M_strSQLQRY="SELECT ST_MATCD,ST_MATDS,ST_MATTP,ST_UOMCD,ST_MINLV,ST_MAXLV,ST_RORLV,"
					+"ST_RORQT,ST_STODT,ST_LOCCD,ST_PGRNO,ST_PISNO,ST_PMRNO,ST_PSNNO,ST_PSTNO,ST_PPONO,"
					+"ST_PGRDT,ST_PISDT,ST_PMRDT,ST_PSNDT,ST_PSTDT,ST_PPOVL,ST_YTDGR,ST_YTDIS,ST_YTDMR,"
					+"ST_YTDSN,ST_YTDST,ST_STKQT,ST_STKIP,ST_STKIN,ST_STKOR,ST_MDVQT,ST_YOPST,ST_ABCFL,"
					+"ST_HMLFL,ST_VEDFL,ST_XYZFL,ST_MDVVL,ST_YOPVL,ST_STKFL,CT_STSFL,"
					+"CT_PKGTP,CT_PSVFL,CT_PSVFR,CT_PSVDT,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,CT_PSVFR,ST_SRPQT "
					+"FROM MM_STMST,CO_CTMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = CT_MATCD and ST_MMSBS = '"+M_strSBSCD+"'"
					+" AND ST_STRTP ='"+strSTRTP+"'AND ST_MATCD = '"+txtMATCD.getText().trim()+"'"
					+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) ? "" : " and isnull(CT_STSFL,'') not in ('9','X')");
				System.out.println(M_strSQLQRY);
				String L_strMATCD = txtMATCD.getText();
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				clrCOMP(); 
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_rstRSSET != null){
					txtMATCD.setText(L_strMATCD);	
					if(M_rstRSSET.next())
					{
						txtMATCD.setText(nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""));
						String L_strFLAG =nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"").toUpperCase();
						if(L_strFLAG.equals("Y"))
						{
							chbSTKFL.setSelected(true);
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							{
								if(strCLSNM.equals("mm_test1"))
								{
									txtMINLV.setEnabled(false);
									txtMAXLV.setEnabled(false);
									setMSG("Modification of Min./Max level is only for non stock controlled items..",'E');
									flgERROR = true;
									return;
								}
							
							}
						}
						else
						{
							chbSTKFL.setSelected(false);
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							{
								txtMINLV.setEnabled(true);
								txtMAXLV.setEnabled(true);
								setMSG(" ",'E');
							}
						}
					txtMATDS.setText(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""));//STRING
					txtMATTP.setText(nvlSTRVL(M_rstRSSET.getString("ST_MATTP"),""));//STRING
					txtUOMCD.setText(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""));//STRING
							
					txtPKGTP.setText(nvlSTRVL(M_rstRSSET.getString("CT_PKGTP"),""));
											
					txtMINLV.setText(nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"0"));//DECIMAL
					txtMAXLV.setText(nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),"0"));//DECIMAL
							
					
							
					txtRORLV.setText(nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),"0"));//DECIMAL
					txtRORQT.setText(M_rstRSSET.getString("ST_RORQT"));//DECIMAL
							
					txtLOCCD.setText(nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"").toUpperCase());//STRING
					if((txtMATCD.getText().trim().substring(9).equals("2"))||(txtMATCD.getText().trim().substring(9).equals("6")))
				    {
						txtILDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),""));
						txtELDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),""));
					}
					else
					{
						txtILDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),""));
						txtELDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),""));
					}
					txtPGRNO.setText(nvlSTRVL(M_rstRSSET.getString("ST_PGRNO"),""));//STRING
							
					datTEMP = M_rstRSSET.getDate("ST_STODT");
					if(datTEMP !=null)
						txtSTODT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtSTODT.setText("");
																				
					datTEMP = M_rstRSSET.getDate("ST_PGRDT");			
					if(datTEMP !=null)
						txtPGRDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPGRDT.setText("");
							
					datTEMP = M_rstRSSET.getDate("ST_PISDT");
					if(datTEMP !=null)
						txtPISDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPISDT.setText("");
														
					datTEMP = M_rstRSSET.getDate("ST_PMRDT");
					if(datTEMP !=null)
						txtPMRDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPMRDT.setText("");
							
					datTEMP = M_rstRSSET.getDate("ST_PSNDT");
					if(datTEMP !=null)
						txtPSNDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPSNDT.setText("");
							
					datTEMP = M_rstRSSET.getDate("ST_PSTDT");
					if(datTEMP !=null)
						txtPSTDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPSTDT.setText("");
							
					txtPISNO.setText(nvlSTRVL(M_rstRSSET.getString("ST_PISNO"),""));//STRING
					txtPMRNO.setText(nvlSTRVL(M_rstRSSET.getString("ST_PMRNO"),""));//STRING
					txtPSNNO.setText(nvlSTRVL(M_rstRSSET.getString("ST_PSNNO"),""));//STIRNG
					txtPSTNO.setText(nvlSTRVL(M_rstRSSET.getString("ST_PSTNO"),""));//STRING
					txtPPONO.setText(nvlSTRVL(M_rstRSSET.getString("ST_PPONO"),""));//STRING
					txtPPOVL.setText(nvlSTRVL(M_rstRSSET.getString("ST_PPOVL"),""));//DECIMAL
					txtYTDGR.setText(nvlSTRVL(M_rstRSSET.getString("ST_YTDGR"),""));//DECIMAL
					txtYTDIS.setText(nvlSTRVL(M_rstRSSET.getString("ST_YTDIS"),"0"));//DECIMAL
					txtYTDMR.setText(nvlSTRVL(M_rstRSSET.getString("ST_YTDMR"),"0"));//DECIMAL
					txtYTDSN.setText(nvlSTRVL(M_rstRSSET.getString("ST_YTDSN"),"0"));//DECIMAL
					txtYTDST.setText(nvlSTRVL(M_rstRSSET.getString("ST_YTDST"),"0"));//DECIMAL
					txtSTKQT.setText(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"));//DECIMAL
					txtSRPQT.setText(nvlSTRVL(M_rstRSSET.getString("ST_SRPQT"),"0")); 
					txtSTKIP.setText(nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),"0"));//DECIMAL
					txtSTKIN.setText(nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0"));//DECIMAL
					txtSTKOR.setText(nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0"));//DECIMAL
					txtMDVQT.setText(nvlSTRVL(M_rstRSSET.getString("ST_MDVQT"),"0"));//DECIMAL
					txtYOPST.setText(nvlSTRVL(M_rstRSSET.getString("ST_YOPST"),"0"));//DECIMAL
					txtABCFL.setText(nvlSTRVL(M_rstRSSET.getString("ST_ABCFL"),""));//CHR
					txtHMLFL.setText(nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),""));//CHR
					txtVEDFL.setText(nvlSTRVL(M_rstRSSET.getString("ST_VEDFL"),""));//CHR
					txtXYZFL.setText(nvlSTRVL(M_rstRSSET.getString("ST_XYZFL"),""));//CHR
					txtMDVVL.setText(nvlSTRVL(M_rstRSSET.getString("ST_MDVVL"),"0"));//DECIMAL
					txtYOPVL.setText(nvlSTRVL(M_rstRSSET.getString("ST_YOPVL"),"0"));//DECIMAL
						
					
					L_strFLAG ="";
					L_strFLAG = nvlSTRVL(M_rstRSSET.getString("CT_STSFL")," ");
					if(L_strFLAG.equals("9"))
						chbSTSFL.setSelected(true);
					else 
						chbSTSFL.setSelected(false);
					L_strFLAG =nvlSTRVL(M_rstRSSET.getString("CT_PSVFL"),"").toUpperCase();
					if(L_strFLAG.equals("Y"))
						chbPSVFL.setSelected(true);
					else 
						chbPSVFL.setSelected(false);
						datTEMP=M_rstRSSET.getDate("CT_PSVDT");
						int L_intNODAY = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_PSVFR"),"0"));
						if(datTEMP !=null){
							M_calLOCAL.setTime(datTEMP);
							M_calLOCAL.add(Calendar.DATE,+L_intNODAY);
							txtPSVDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						}
					}
					if(chbSTKFL.isSelected())
						txtMINLV.requestFocus();
					else
						txtLOCCD.requestFocus();
				}
				else
				{
					setMSG("Data Not Found",'E');
				}
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
		//	}
			catch(SQLException L_SQLE){
				//setMSG("Error in SQL",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		else{
			setMSG("Please Enter Material Code OR Press F1",'N');
		}
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC==txtMATCD && !M_flgERROR)
				setMSG("Enter Material Code; 'F1' : Search by Code, 'F2' : Search by Description ..",'N');
			///****ADDED BY AAP 29/05/04
			if(M_objSOURC == cmbSSGRP)
			{
				cmbSSGRP.removeAllItems();
				cmbSSGRP.addItem("Select SubSubGroup","");
				StringTokenizer L_stkTEMP=null;
				
				for(int i=0;i<vtrSSGRP.size();i++)
				{
					L_stkTEMP=new StringTokenizer((String)vtrSSGRP.elementAt(i),"|");
					if(cmbMNGRP.getSelectedIndex()>0)
					{
						if(!L_stkTEMP.nextToken().equals(cmbMNGRP.getITMCD()))
							continue;
					}
					else
						L_stkTEMP.nextToken();
					
					cmbSSGRP.addItem(L_stkTEMP.nextToken(),L_stkTEMP.nextToken());
				}
				cmbSSGRP.hidePopup();
				cmbSSGRP.showPopup();
			}
			///****
		}catch(Exception e)
		{setMSG(e,"Child.KeyPressed");}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	boolean vldDATA()
	{
		if(flgERROR)
		{
			// flgERROR is set when saving is blocked after some validation .
			setMSG("Invalid Details..",'E');
			return false;
		}
		if(txtLOCCD.getText().trim().length() == 0)
		{
			setMSG("Location code can not be blank..",'E');
			return false;
		}
		if(txtMINLV.getText().trim().length() == 0)
			txtMINLV.setText("0");
		if(txtMAXLV.getText().trim().length() == 0)
			txtMAXLV.setText("0");
		if(txtRORLV.getText().trim().length() == 0)
			txtRORLV.setText("0");
		if(txtRORQT.getText().trim().length() == 0)
			txtRORQT.setText("0");
		if(txtSRPQT.getText().trim().length() == 0)
			txtSRPQT.setText("0");
		if(chbSTKFL.isSelected())
		{
			if(Double.parseDouble(txtMINLV.getText().trim())==0)
			{
				setMSG("Minimum level can not be 0 for stock controlled items..",'E'); 		
				txtMINLV.requestFocus();
				return false;
			}
			else if(Double.parseDouble(txtMAXLV.getText().trim())==0)
			{
				setMSG("Maximum level can not be 0 for stock controlled items..",'E'); 		
				txtMAXLV.requestFocus();
				return false;
			}
			if(Double.parseDouble(txtRORLV.getText().trim())==0)
			{
				setMSG("Reorder level can not be 0 for stock controlled items..",'E'); 		
				txtRORLV.requestFocus();
				return false;
			}
			if(Double.parseDouble(txtRORQT.getText().trim())==0)
			{
				setMSG("Reorder Qty. can not be 0 for stock controlled items..",'E'); 		
				txtRORQT.requestFocus();
				return false;
			}	
			if(Double.parseDouble(txtMINLV.getText())>Double.parseDouble(txtMAXLV.getText()))
			{
				setMSG("Min. Qty. can not be greater than Max. Qty ..",'E'); 		
				txtMINLV.requestFocus();
				return false;
			}
			if(Double.parseDouble(txtRORLV.getText())>Double.parseDouble(txtMAXLV.getText()))
			{
				setMSG("Re-order Level can not be greater than Max. Qty ..",'E'); 		
				txtRORLV.requestFocus();
				return false;
			}
			if(Double.parseDouble(txtRORQT.getText())>Double.parseDouble(txtMAXLV.getText()))
			{
				setMSG("Re-order Qty. can not be greater than Max. Qty ..",'E'); 		
				txtRORQT.requestFocus();
				return false;
			}
		}
		if(txtVEDFL.getText().trim().length() >0)
		{
			if(txtVEDFL.getText().trim().length() > 1)
			{
				setMSG("VED FLAG can be of single character..(V/E/D)",'E');
				return false;
			}
			else
			{
				if(!(txtVEDFL.getText().trim().equals("V")))
				if(!(txtVEDFL.getText().trim().equals("E")))
					if(!(txtVEDFL.getText().trim().equals("D")))
					{
						setMSG("Invalid input for VED FLAG ..(V/E/D)",'E');
						return false;
					}
			}
		}
		return true;
	}
	void exeSAVE()
	{
		try
		{
			if(vldDATA())
			{
				cl_dat.M_flgLCUPD_pbst = true;
				M_strSQLQRY =" UPDATE MM_STMST SET ST_LOCCD ='"+txtLOCCD.getText().trim().toUpperCase() +"',";
				M_strSQLQRY +=" ST_VEDFL ='"+txtVEDFL.getText().trim() +"',";
				M_strSQLQRY +=" ST_MINLV ="+txtMINLV.getText().trim() +",";
				M_strSQLQRY +=" ST_MAXLV ="+txtMAXLV.getText().trim() +",";
				M_strSQLQRY +=" ST_SRPQT ="+txtSRPQT.getText().trim() +",";
				M_strSQLQRY +=" ST_RORLV ="+txtRORLV.getText().trim() +",";
				M_strSQLQRY +=" ST_RORQT ="+txtRORQT.getText().trim() +",";
				if(chbSTKFL.isSelected())
					M_strSQLQRY +=" ST_STKFL  ='Y',";
				else
					M_strSQLQRY +=" ST_STKFL  ='N',";
				M_strSQLQRY += getUSGDTL("ST",'U',chbSTSFL.isSelected() ? "9" : null);
				M_strSQLQRY += " WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MMSBS ='"+M_strSBSCD +"'";
				M_strSQLQRY += " AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY += " AND ST_MATCD = '"+txtMATCD.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Data has been Modified..",'N');
					clrCOMP();
				}
				else
					setMSG("Error in Modification..",'E');
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
		}
	}
	/**
	 * Method to get data from back end which fits in user defined criteria
	 * 
	 * Method to get data from back end which fits in user defined criteria
	 * 
	 * <p>Adds filter to the qry for MAIN GRP and/or SUBSUB GRP and/or PARTIAL DESCRIPTION.<br>
	 * Puts the data into staQRDAT string array. size of the array is determined using ResultSet MEtadata<br>
	 * Puts data in the same sequence as that of resultset.<br>
	 * If no data is found, displays msg and returns. Otherwise, displays first record.
	 */
	private void getDATA()
	{
		try
		{
			txtPRTDS.setText(txtPRTDS.getText().toUpperCase());
			String L_strFILTR="";
			if(cmbSSGRP.getSelectedIndex()>0)
				L_strFILTR=" and CT_MATCD like '"+cmbSSGRP.getITMCD()+"%'";
			else if(cmbMNGRP.getSelectedIndex()>0)
				L_strFILTR=" and CT_GRPCD = '"+cmbMNGRP.getITMCD()+"'";
			if(txtPRTDS.getText().trim().length()>0)
				L_strFILTR+=" and CT_MATDS like '%"+txtPRTDS.getText().trim().toUpperCase()+"%'";
			//IMPORTANT : DO NOT CHANGE SEQUESNCE OF THE COLUMNS IN THE QRY as RECORD DISPLAY USES COLUMN INDEX INSTEAD OF DESCRIPTION IN dspDATA() method
			M_strSQLQRY="SELECT ST_MATCD,ST_MATDS,ST_MATTP,ST_UOMCD,ST_MINLV,ST_MAXLV,ST_RORLV,"
				+"ST_RORQT,ST_STODT,ST_LOCCD,ST_PGRNO,ST_PISNO,ST_PMRNO,ST_PSNNO,ST_PSTNO,ST_PPONO,"
				+"ST_PGRDT,ST_PISDT,ST_PMRDT,ST_PSNDT,ST_PSTDT,ST_PPOVL,ST_YTDGR,ST_YTDIS,ST_YTDMR,"
				+"ST_YTDSN,ST_YTDST,ST_STKQT,ST_STKIP,ST_STKIN,ST_STKOR,ST_MDVQT,ST_YOPST,ST_ABCFL,"
				+"ST_HMLFL,ST_VEDFL,ST_XYZFL,ST_MDVVL,ST_YOPVL,ST_STKFL,CT_STSFL,"
				+"CT_PKGTP,CT_PSVFL,CT_PSVFR,CT_PSVDT,CT_ILDTM,CT_ELDTM,CT_PSVFR,ST_SRPQT,CT_IILTM,CT_IELTM "
				+"FROM MM_STMST,CO_CTMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = CT_MATCD and ST_MMSBS = '"+M_strSBSCD+"'"
				+" AND ST_STRTP ='"+M_strSBSCD.substring(2,4)+"' "+L_strFILTR
				+" and isnull(CT_STSFL,'') not in ('9','X')";
			L_rstRSSET=stmTESTK.executeQuery(M_strSQLQRY);
			ResultSetMetaData L_rsmTEMP=L_rstRSSET.getMetaData();
			int L_intCOLCT=L_rsmTEMP.getColumnCount();
			
			int L_intROWCT=0;
			if(L_rstRSSET!=null)
			{
				while (L_rstRSSET.next())
					L_intROWCT++;
				L_intROWCT--;
				if(L_intROWCT<1)
				{
					setMSG("No data found ..",'E');
					return;
				}
				L_rstRSSET.first();
				staQRDAT=new String[L_intROWCT][L_intCOLCT];
				int L_intROW=0;
				while (L_rstRSSET.next()&&L_intROW<L_intROWCT)
				{
					for(int i=0;i<L_intCOLCT;i++)
					{
						if(L_rsmTEMP.getColumnType(i+1)==91)
						{
							if(L_rstRSSET.getDate(i+1)!=null)
								staQRDAT[L_intROW][i]=L_rstRSSET.getDate(i+1).toString();
						}
						else
							staQRDAT[L_intROW][i]=L_rstRSSET.getString(i+1);
					}
					L_intROW++;
				}
				dspDATA(0);
			}
		}catch(Exception e)
		{setMSG(e,"Child.getData");}
	}
	/**
	 * Method to display record at a given index of staQRDAT.
	 * 
	 * Method to display record at a given index of staQRDAT.
	 * 
	 * <p>Sets state of the navigation buttons and displays data cinsidering that, sequence of values in staQRDAT is appropriate.
	 * 
	 * @param P_intROWID Index of the row in staQRDAT to be displayed
	 */
	private void dspDATA(int P_intROWID)
	{
		try
		{
			if(staQRDAT==null)
			{
				setMSG("No data found ..",'E');
				return;
			}
			intROWID=P_intROWID;
			btnFIRST.setEnabled(intROWID >0);
			btnPREV.setEnabled(intROWID >0);
			btnLAST.setEnabled(intROWID <staQRDAT.length-1);
			btnNEXT.setEnabled(intROWID <staQRDAT.length-1);
			if(!btnNEXT.isEnabled())
				btnPREV.requestFocus();
			txtMATCD.setText(nvlSTRVL(staQRDAT[P_intROWID][0],""));
			txtMATDS.setText(nvlSTRVL(staQRDAT[P_intROWID][1],""));//STRING
			txtMATTP.setText(nvlSTRVL(staQRDAT[P_intROWID][2],""));//STRING
			txtUOMCD.setText(nvlSTRVL(staQRDAT[P_intROWID][3],""));//STRING
			txtMINLV.setText(nvlSTRVL(staQRDAT[P_intROWID][4],"0"));//DECIMAL
			
			txtPKGTP.setText(nvlSTRVL(staQRDAT[P_intROWID][41],""));
											
			txtMAXLV.setText(nvlSTRVL(staQRDAT[P_intROWID][5],"0"));//DECIMAL
							
			txtRORLV.setText(nvlSTRVL(staQRDAT[P_intROWID][6],"0"));//DECIMAL
			txtRORQT.setText(staQRDAT[P_intROWID][7]);//DECIMAL
							
			txtLOCCD.setText(nvlSTRVL(staQRDAT[P_intROWID][9],""));//STRING
			if((txtMATCD.getText().trim().substring(9).equals("2"))||(txtMATCD.getText().trim().substring(9).equals("6")))
			{
				// Lead time for imported codes
				txtILDTM.setText(nvlSTRVL(staQRDAT[P_intROWID][49],""));
				txtELDTM.setText(nvlSTRVL(staQRDAT[P_intROWID][50],""));
			}
			else
			{
				txtILDTM.setText(nvlSTRVL(staQRDAT[P_intROWID][45],""));
				txtELDTM.setText(nvlSTRVL(staQRDAT[P_intROWID][46],""));
			}
			txtPGRNO.setText(nvlSTRVL(staQRDAT[P_intROWID][10],""));//STRING
			if(staQRDAT[P_intROWID][8]!=null)
				txtSTODT.setText(M_fmtLCDAT.format(java.sql.Date.valueOf(staQRDAT[P_intROWID][8])));
			else
				txtSTODT.setText("");
			
			if(staQRDAT[P_intROWID][16]!=null)																				
				txtPGRDT.setText(M_fmtLCDAT.format(java.sql.Date.valueOf( staQRDAT[P_intROWID][16])));
			else
				txtPGRDT.setText("");
							
			if(staQRDAT[P_intROWID][17]!=null)
				txtPISDT.setText(M_fmtLCDAT.format(java.sql.Date.valueOf(staQRDAT[P_intROWID][17])));
			else
				txtPISDT.setText("");
			
			if(staQRDAT[P_intROWID][18]!=null)
				txtPMRDT.setText(M_fmtLCDAT.format(java.sql.Date.valueOf(staQRDAT[P_intROWID][18])));
			else
				txtPMRDT.setText("");
			
			if(staQRDAT[P_intROWID][19]!=null)
				txtPSNDT.setText(M_fmtLCDAT.format(java.sql.Date.valueOf(staQRDAT[P_intROWID][19])));
			else
				txtPSNDT.setText("");
			
			if(staQRDAT[P_intROWID][20] !=null)
				txtPSTDT.setText(M_fmtLCDAT.format(java.sql.Date.valueOf(staQRDAT[P_intROWID][20])));
			else
				txtPSTDT.setText("");
			txtPISNO.setText(nvlSTRVL(staQRDAT[P_intROWID][11],""));//STRING
			txtPMRNO.setText(nvlSTRVL(staQRDAT[P_intROWID][12],""));//STRING
			txtPSNNO.setText(nvlSTRVL(staQRDAT[P_intROWID][13],""));//STIRNG
			txtPSTNO.setText(nvlSTRVL(staQRDAT[P_intROWID][14],""));//STRING
			txtPPONO.setText(nvlSTRVL(staQRDAT[P_intROWID][15],""));//STRING
			txtPPOVL.setText(nvlSTRVL(staQRDAT[P_intROWID][21],""));//DECIMAL
			txtYTDGR.setText(nvlSTRVL(staQRDAT[P_intROWID][22],""));//DECIMAL
			txtYTDIS.setText(nvlSTRVL(staQRDAT[P_intROWID][23],"0"));//DECIMAL
			txtYTDMR.setText(nvlSTRVL(staQRDAT[P_intROWID][24],"0"));//DECIMAL
			txtYTDSN.setText(nvlSTRVL(staQRDAT[P_intROWID][25],"0"));//DECIMAL
			txtYTDST.setText(nvlSTRVL(staQRDAT[P_intROWID][26],"0"));//DECIMAL
			txtSTKQT.setText(nvlSTRVL(staQRDAT[P_intROWID][27],"0"));//DECIMAL
			txtSTKIP.setText(nvlSTRVL(staQRDAT[P_intROWID][28],"0"));//DECIMAL
			txtSTKIN.setText(nvlSTRVL(staQRDAT[P_intROWID][29],"0"));//DECIMAL
			txtSTKOR.setText(nvlSTRVL(staQRDAT[P_intROWID][30],"0"));//DECIMAL
			txtMDVQT.setText(nvlSTRVL(staQRDAT[P_intROWID][31],"0"));//DECIMAL
			txtYOPST.setText(nvlSTRVL(staQRDAT[P_intROWID][32],"0"));//DECIMAL
			txtABCFL.setText(nvlSTRVL(staQRDAT[P_intROWID][33],""));//CHR
			txtHMLFL.setText(nvlSTRVL(staQRDAT[P_intROWID][34],""));//CHR
			txtVEDFL.setText(nvlSTRVL(staQRDAT[P_intROWID][35],""));//CHR
			txtXYZFL.setText(nvlSTRVL(staQRDAT[P_intROWID][36],""));//CHR
			txtMDVVL.setText(nvlSTRVL(staQRDAT[P_intROWID][37],"0"));//DECIMAL
			txtYOPVL.setText(nvlSTRVL(staQRDAT[P_intROWID][38],"0"));//DECIMAL
						
			String L_strFLAG =nvlSTRVL(staQRDAT[P_intROWID][39],"").toUpperCase();
			if(L_strFLAG.equals("Y"))
			{
				chbSTKFL.setSelected(true);
			}
		//SET SURPLACE QTY HERE CHANGED BY AAP 31/05/04
			txtSRPQT.setText(nvlSTRVL(staQRDAT[P_intROWID][48],"0"));
			L_strFLAG ="";
			L_strFLAG = nvlSTRVL(staQRDAT[P_intROWID][40]," ");
			if(L_strFLAG.equals("9"))
				chbSTSFL.setSelected(true);
			else 
				chbSTSFL.setSelected(false);
			L_strFLAG =nvlSTRVL(staQRDAT[P_intROWID][42],"").toUpperCase();
			if(L_strFLAG.equals("Y"))
				chbPSVFL.setSelected(true);
			else 
				chbPSVFL.setSelected(false);
			int L_intNODAY = Integer.parseInt(nvlSTRVL(staQRDAT[P_intROWID][47],"0"));
			if(staQRDAT[P_intROWID][44] !=null)
			{
				M_calLOCAL.setTime(java.sql.Date.valueOf(staQRDAT[P_intROWID][44]));
				M_calLOCAL.add(Calendar.DATE,+L_intNODAY);
				txtPSVDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
			}
			setMSG("Record "+Integer.toString(intROWID+1)+" of "+staQRDAT.length,'N');
		}catch(Exception e)
		{setMSG(e,"Child.dspDATA");}
	}
}