import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
class mm_teblp extends cl_pbase implements ChangeListener
{

	private JTabbedPane tbpMAIN;
	private JPanel pnlCODTL,pnlITTAX,pnlCOTAX,pnlITDTL;
	private JTextField txtVENCD,txtVENNM,txtAMDNO,txtAMDDT,txtOTPMT,txtOTDED,txtOTHDS,txtACPDT,txtPMDDT,txtEXGRT,txtCURCD,txtCURDS;
	private JTextField txtBILAM,txtCALAM,txtBILNO,txtBILDT,txtSRLNO,txtBLPDT,txtGRNNO,txtGRNDT,txtPORNO;
	private JButton btnPROCS;
	private JComboBox cmbBLPTP;
	private JLabel lblBLPTP;
	private cl_JTable tblITTAX,tblCOTAX,tblITDTL;
	private ResultSet rstRSSET; 
	private TableInputVerifier objTBLVRF;
	private Vector<String> vtrCURCD,vtrUOMCD;
	private String strUSRNM ="",strSTXDS="",strOTHDS="",strGTXDS="",strGOTDS,strPORNO; // gardewise other tax description
	private double dblITMVL=0.00,dblEXGRT=0.0,dblTXVAL =0.0,dblBILVL =0.0;
	private int intCTXRW =0,intGTXRW =0;    
	private final int TBL_CHKFL =0;
	private final int TBL_MATCD =1;
	private final int TBL_MATDS =2;
	private final int TBL_UOMCD =3;
	private final int TBL_POUOM =4;
	private final int TBL_CNVFT =5;
	private final int TBL_ACPQT =6;
	private final int TBL_BILQT =7;
	private final int TBL_BLPQT =8;
	private final int TBL_PORRT =9;
	private final int TBL_PERRT =10;
	private final int TBL_ITVAL =11;
	private final int TBL_MODVL =12;
	private final int TBL_CHLQT =13;
	private final int TBL_RECQT =14;
	private final int TBL_TRNQT =15;
	private final int TBL_FRTAM =16;
	private final int TBL_PORVL =17;
	private final int TBL_VATVL =18;
	private final int TBL_VATBL =19;
	private final int TB1_MATCD =1;

	private String strTRNTP ="PO";
	private String strSRLNO ="";
	private java.sql.Date datTEMP;	
	private String strTEMP="";
	private boolean flgCOTAX = false;
	private boolean flgITTAX = false;
	private boolean flgFOUND = false;
	private Hashtable<String,String> hstTXCAT,hstCOTAX,hstTAXCD,hstTAXSP;
	private Vector<String> vtrTAXCD = new Vector<String>();
	private Vector<String> vtrTAXTP = new Vector<String>();
	private PreparedStatement pstmDODREC,pstmINSREC,pstmINSRMK,pstmUPDREC;
	private String strYSTDT ="",strBLPTP="";
	CallableStatement cstGRMST_BIL;	// Stored procedure for updating bill calculations in MM_GRMST(batchwise)
	CallableStatement cstISMST_BIL;	// Stored procedure for updating bill calculations in MM_ISMST(batchwise)
	CallableStatement cstPLTRN_BIL; // Stored procedure for updating bill amount into party ledger (MM_PLTRN)
	StringTokenizer L_STRTKN;
	Calendar calendar ;
	Date nxtDATE;
	mm_teblp()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(lblBLPTP=new JLabel("Bill Pass Type"),1,1,1,1,this,'L');
			add(cmbBLPTP=new JComboBox(new String[]{"Select"}),1,2,1,1.5,this,'L');	

			add(new JLabel("SPL SNo."),1,4,1,1.5,this,'C');
			add(txtSRLNO=new TxtLimit(8),1,4,1,1,this,'R');
			add(new JLabel("Supplier"),1,5,1,1,this,'L');
			add(txtVENCD=new TxtLimit(5),1,6,1,1,this,'L');
			add(txtVENNM=new TxtLimit(40),1,7,1,2,this,'L');

			add(new JLabel("Bill Pass.Date"),2,1,1,1,this,'L');
			add(txtBLPDT=new TxtDate(),2,2,1,1,this,'L');
			add(new JLabel("Bill No."),2,3,1,1,this,'L');
			add(txtBILNO=new TxtLimit(10),2,4,1,1,this,'L');
			add(new JLabel("Bill Date"),2,5,1,1,this,'L');
			add(txtBILDT=new TxtDate(),2,6,1,1,this,'L');
			add(new JLabel("Bill Amount"),2,7,1,1,this,'L');
			add(txtBILAM=new TxtNumLimit(12.2),2,8,1,1,this,'L');
			add(new JLabel("GRIN No."),3,1,1,1,this,'L');
			add(txtGRNNO=new TxtLimit(10),3,2,1,1,this,'L');
			add(new JLabel("ACP. Date"),3,3,1,1,this,'L');
			add(txtACPDT=new TxtDate(),3,4,1,1,this,'L');
			add(new JLabel("P.O.No"),3,5,1,1,this,'L');
			add(txtPORNO=new TxtLimit(8),3,6,1,1,this,'L');
			add(new JLabel("Cal. Amount"),3,7,1,1,this,'L');
			add(txtCALAM=new TxtNumLimit(12.2),3,8,1,1,this,'L');
			txtCALAM.setDisabledTextColor(Color.blue);
			add(new JLabel("Cur. Code"),4,5,1,1,this,'L');
			add(txtCURCD=new TxtLimit(10),4,6,1,1,this,'L');
			add(new JLabel("Cur. Desc"),4,7,1,1,this,'L');
			add(txtCURDS=new TxtLimit(10),4,8,1,1,this,'L');
			add(new JLabel("Exg. Rate"),5,5,1,1,this,'L');
			add(txtEXGRT=new TxtNumLimit(7.2),5,6,1,1,this,'L');
			add(new JLabel("Pay.Due Date"),6,5,1,1,this,'L');
			add(txtPMDDT=new TxtDate(),6,6,1,1,this,'L');

			add(txtACPDT=new TxtDate(),3,4,1,1,this,'L');
			add(new JLabel("Other Pay."),4,1,1,1,this,'L');
			add(txtOTPMT=new TxtNumLimit(12.2),4,2,1,1,this,'L');
			add(new JLabel("Other Ded."),4,3,1,1,this,'L');
			add(txtOTDED=new TxtNumLimit(12.2),4,4,1,1,this,'L');
			add(new JLabel("Pay /Ded.Desc."),5,1,1,1,this,'L');
			add(txtOTHDS=new TxtLimit(100),5,2,1,3,this,'L');
			add(btnPROCS = new JButton("Process"),5,8,1,1,this,'L');

			setMatrix(20,6);

			pnlCOTAX = new JPanel(null);
			pnlITTAX = new JPanel(null);
			pnlITDTL = new JPanel(null);

			tblCOTAX=crtTBLPNL1(pnlCOTAX=new JPanel(null),new String[]{"FL","Code","Description","Amt./Percent","Value","Tax Value"},50,1,1,8,6,new int[]{20,100,200,100,150,150},new int[]{0});
			tblCOTAX.setCellEditor(2,new TxtLimit(30));
			tblCOTAX.setCellEditor(3,new TxtLimit(1));
			tblCOTAX.setCellEditor(4,new TxtNumLimit(10.2)); // changes at H.O.	
			tblITDTL=crtTBLPNL1(pnlITDTL=new JPanel(null),new String[]{"FL","Item Code","Description","UOM ","P.UOM","Conv.fac.","Acp.Qty","Billed Qty.","Qty. to bill","Rate","Rate Per","Item Value","ModVat","Chl","Rec","Bil","Frt","POR","VAT","VAT BL"},150,1,1,9,7,new int[]{20,75,155,45,45,50,75,65,65,60,55,60,50,5,5,5,5,5,5,5},new int[]{0});
			tblITTAX=crtTBLPNL1(pnlITTAX=new JPanel(null),new String[]{"FL","Item Code","tax","Description","Amt./Percent","Value","Tax Value"},150,1,1,8,6,new int[]{20,100,60,200,100,100,100},new int[]{0});
			tbpMAIN=new JTabbedPane();
			tbpMAIN.add("Item Details",pnlITDTL);
			tbpMAIN.add("Common Tax",pnlCOTAX);
			tbpMAIN.add("Itemwise Tax",pnlITTAX);
			add(tbpMAIN,7,1,12,7.8,this,'L');
			tbpMAIN.addChangeListener(this);
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_teblpTBLINVFR();
			tblITDTL.setInputVerifier(objTBLVRF);
			tblCOTAX.setInputVerifier(objTBLVRF);
			tblITTAX.setInputVerifier(objTBLVRF);
			hstCOTAX = new Hashtable<String,String>();
			hstTAXCD = new Hashtable<String,String>();
			hstTXCAT = new Hashtable<String,String>();
			hstTAXSP = new Hashtable<String,String>();
			crtHSTXCD();
			M_strSQLQRY = "Select CMT_CHP01 from CO_CDTRN where CMT_CGMTP ='SYS' and cmt_cgstp ='MMXXYRD' "+
			" AND CMT_CODCD ='01' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
					strYSTDT = M_rstRSSET.getString("CMT_CHP01");
				M_rstRSSET.close();
			}

			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN"; 
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXBTP'";    
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);       

			if(M_rstRSSET != null) 
			{ 
				while(M_rstRSSET.next()) 
				{       
					cmbBLPTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS")); 
				} 
				M_rstRSSET.close(); 
			}   

			calendar=Calendar.getInstance();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
	}

	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtSRLNO.setEnabled(false);
				txtBLPDT.setEnabled(false);
				btnPROCS.setEnabled(true);


			}
			else
				txtSRLNO.setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			btnPROCS.setEnabled(true);
		}

		txtVENNM.setEnabled(false);
		txtACPDT.setEnabled(false);
		txtCALAM.setEnabled(false);
		txtCURCD.setEnabled(false);
		txtCURDS.setEnabled(false);
		txtEXGRT.setEnabled(false);
		tblITDTL.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_POUOM].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_PORRT].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_PERRT].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_BILQT].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_ACPQT].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_ITVAL].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_MATCD].setEnabled(false);
		tblITDTL.cmpEDITR[TBL_MATDS].setEnabled(false);
		tblCOTAX.cmpEDITR[5].setEnabled(false);
		tblITTAX.cmpEDITR[6].setEnabled(false);
		if(tblCOTAX.isEditing())
			tblCOTAX.getCellEditor().stopCellEditing();
		if(tblITDTL.isEditing())
			tblITDTL.getCellEditor().stopCellEditing();
		if(tblITTAX.isEditing())
			tblITTAX.getCellEditor().stopCellEditing();
		tblITDTL.setRowSelectionInterval(0,0);
		tblITDTL.setColumnSelectionInterval(0,0);
		tblCOTAX.setRowSelectionInterval(0,0);
		tblCOTAX.setColumnSelectionInterval(0,0);
		tblITTAX.setRowSelectionInterval(0,0);
		tblITTAX.setColumnSelectionInterval(0,0);
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int[] L_inaCOLSZ = new int[]{100,420};
		if(L_KE.getKeyCode() == L_KE.VK_F2)
		{
			if(M_objSOURC == txtVENCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtVENCDF2";
				String L_ARRHDR[] = {"Supplier Code","Name"};
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
				M_strSQLQRY += " where PT_PRTTP ='S' and isnull(PT_STSFL,'') <>'X'";
				if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " and PT_PRTCD like '" + txtVENCD.getText().trim() + "%'";
				M_strSQLQRY += " Order by pt_prtnm ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if((M_objSOURC == txtVENCD)&& (!cmbBLPTP.getSelectedItem().toString().trim().substring(0,2).equalsIgnoreCase("07")))
			{
				int[] L_inaCOLVN = new int[]{100,400};
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtVENCD";
				String L_ARRHDR[] = {"Supplier Code","Name"};
				M_strSQLQRY = "Select DISTINCT GR_VENCD,GR_VENNM from MM_GRMST ";
				M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_ACPQT,0)-isnull(GR_BILQT,0) >0 and isnull(GR_STSFL,'') <>'X'";
				M_strSQLQRY += " AND GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " and GR_VENCD like '" + txtVENCD.getText().trim().toUpperCase() + "%'";
				M_strSQLQRY += " Order by GR_VENCD ";
				System.out.println("1 "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLVN);
			}
			else if((M_objSOURC == txtVENCD)&& (cmbBLPTP.getSelectedItem().toString().trim().substring(0,2).equalsIgnoreCase("07")))
			{			
				
				int[] L_inaCOLVN = new int[]{100,400};
				if(txtVENCD.getText().trim().length() ==0)
				{
					setMSG("Please enter the First digits of Vendor Code..",'N'); 
					return;
				}
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtVENCD";
				String L_ARRHDR[] = {"Supplier Code","Name"};
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
				M_strSQLQRY += " where PT_PRTTP ='S' and isnull(PT_STSFL,'') <>'X'";
				if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " and PT_PRTCD like '" + txtVENCD.getText().trim().toUpperCase() + "%'";
				M_strSQLQRY += " Order by pt_prtcd ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLVN);				
			
			}
			if(M_objSOURC == txtCURCD)
			{
				int[] L_inaCOLVN = new int[]{100,400};
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtCURCD";
				String L_ARRHDR[] = {"Currency Code","Currency Description"};
				M_strSQLQRY = "Select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgstp='COXXCUR' ";
				if(txtCURCD.getText().trim().length() >0)
					M_strSQLQRY += " and cmt_codcd like '" + txtCURCD.getText().trim().toUpperCase() + "%'";
				System.out.println("2 "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLVN);
			}

			//select po_porno,po_curcd,co_curds,po_exgrt,round(po_poblp,2),cmt_codds from mm_pomst where po_strtp==M_strSBSCD.substring(2,4) and po_stsfl not in (o,X)

			if(M_objSOURC == txtPORNO)
			{
				if(((String)cmbBLPTP.getSelectedItem()).substring(0,2).trim().equals("06"))
				{
					int[] L_inaCOLVN = new int[]{100,400};
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPORNO";
					String L_ARRHDR[] = {"PO No.","Party Name"};
					//M_strSQLQRY = "select po_porno,pt_prtnm,po_curcd,cmt_codds,po_exgrt,round(po_porvl,2),cmt_codds from mm_pomst left outer join co_cdtrn on  po_curcd=cmt_codcd left outer join co_ptmst on po_vencd=pt_prtcd where pt_prttp='S' and cmt_cgstp='COXXCUR' and po_strtp='"+M_strSBSCD.substring(2,4)+"' and po_stsfl not in ('O','X')";
					//M_strSQLQRY = "select po_porno,pt_prtnm from mm_pomst left outer join co_ptmst on po_vencd=pt_prtcd where po_strtp='"+M_strSBSCD.substring(2,4)+"' and pt_prttp='S' and po_stsfl not in ('O','X')";
					M_strSQLQRY = "select distinct po_porno,pt_prtnm from mm_pomst,co_ptmst where po_vencd=pt_prtcd and po_strtp='"+M_strSBSCD.substring(2,4)+"' and pt_prttp='S' and po_stsfl not in ('O','X')";
					
					if(txtPORNO.getText().trim().length() >0)
						M_strSQLQRY += " and po_porno like '" + txtPORNO.getText().trim() + "%'";
					System.out.println("**porno** "+M_strSQLQRY);				
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLVN);	
				}
				else
				{
					setMSG("help not available",'N');
				}
				
			}



			else if(M_objSOURC == txtGRNNO)
			{
				int[] L_inaCOLVN = new int[]{100,400};
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtGRNNO";
				String L_ARRHDR[] = {"GRIN NO.","Accepted Date"};
				M_strSQLQRY = "Select DISTINCT GR_GRNNO,GR_ACPDT from MM_GRMST ";
				M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_ACPQT,0)-isnull(GR_BILQT,0) >0 and isnull(GR_STSFL,'') <>'X'";
				M_strSQLQRY += " AND GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				if(txtVENCD.getText().length() ==5)
					M_strSQLQRY += " AND GR_VENCD ='"+txtVENCD.getText().trim() +"'";
				if(txtGRNNO.getText().trim().length() >0)
					M_strSQLQRY += " and GR_GRNNO like '" + txtGRNNO.getText().trim().toUpperCase() + "%'";
				M_strSQLQRY += " Order by GR_GRNNO DESC";
				System.out.println("2 "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLVN);
			}
			else if(M_objSOURC==tblCOTAX.cmpEDITR[1])
			{
				M_strHLPFLD = "tblCOTAX";
				//cl_hlp("Select CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' order by CMT_CODCD" ,2,1,new String[] {"Code","Name"},6,"CT");
				cl_hlp("Select CMT_CODCD,CMT_CODDS,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' order by CMT_CODCD" ,2,1,new String[] {"Code","Name"},5,"CT");
			}
			else if(M_objSOURC==tblITTAX.cmpEDITR[2])
			{
				M_strHLPFLD = "tblITTAX";
				//M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' ";
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' ";

				int i =0;
				boolean L_flgFOUND = false;
				for(i=0;i<tblCOTAX.getRowCount();i++)
				{
					if(tblCOTAX.getValueAt(i,1).toString().trim().length() >0)
					{
						L_flgFOUND = true;
						if( i >0)
							M_strSQLQRY +=	",'"+tblCOTAX.getValueAt(i,1).toString()	+"'";
						else
						{
							M_strSQLQRY += " AND CMT_CODCD not in('";
							M_strSQLQRY +=	tblCOTAX.getValueAt(i,1).toString()	+"'";
						}
					}
				}
				if(L_flgFOUND)
					M_strSQLQRY += ")"	;
				M_strSQLQRY += " order by CMT_CODCD";
				cl_hlp(M_strSQLQRY,2,1,new String[] {"Code","Name"},5,"CT");
			}
			else if(M_objSOURC == tblITTAX.cmpEDITR[1])
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "tblITTAX1";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				M_strSQLQRY += " AND CT_MATCD IN(";
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					//if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					if(tblITDTL.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
					{
						if( i >0)
							M_strSQLQRY +=	",'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()	+"'";
						else
							M_strSQLQRY +=	"'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()	+"'";
					}
				}

				M_strSQLQRY += ") Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			if(M_objSOURC == txtSRLNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtSRLNO";
				String L_ARRHDR[] = {"SPL Doc. No.","Bill No.","Bill Date","Vendor Code"};
				M_strSQLQRY = "Select BL_DOCNO,BL_BILNO,BL_BILDT,BL_PRTCD";
				M_strSQLQRY += " from MM_BLMST ";

				//	M_strSQLQRY += " where BL_BILTP = '" + LM_BILTP + "'";
				//M_strSQLQRY += " where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND bl_prttp ='S' and bl_biltp ='"+strBLPTP+"' and isnull(BL_STSFL,'') <> 'X'";
				M_strSQLQRY += " where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND bl_prttp ='S' and isnull(BL_STSFL,'') <> 'X'";
				M_strSQLQRY += " order by BL_DOCNO Desc";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtVENCD)
			{

				txtVENCD.setText(txtVENCD.getText().trim().toUpperCase());
				if(!vldVENCD(txtVENCD.getText().trim()))
					setMSG("Invalid Vendor",'E');
				else
				{
					if(!txtVENCD.getText().trim().equalsIgnoreCase("Z9999"))
					{
						txtVENNM.setEnabled(false);
						txtBILNO.requestFocus();
						setMSG("Enter the Bill No...",'N');
					}
					else
					{
						txtVENNM.setEnabled(true);
						txtVENNM.requestFocus();
						setMSG("Enter the Vendor Name..",'N');
					}
					
					 
				}
				
			}
			else if(M_objSOURC == txtBILNO)
			{
				txtBILDT.requestFocus();
				setMSG("Enter the Bill date ..",'N'); 
			}
			else if(M_objSOURC == txtVENNM)
			{
				
				txtBILNO.requestFocus();
			}
			else if((M_objSOURC == txtBILDT))				
			{
				
				
				if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
				{
					txtPORNO.requestFocus();
					setMSG("Enter the PORNO ..",'N');
				
				
				}
				else
				{
					txtBILAM.requestFocus();
					//cl_dat.M_btnSAVE_pbst.requestFocus();
					setMSG("Enter bill amt",'N'); 
					
				}
				
			}

			else if(M_objSOURC == txtBILAM)
			{
				if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07"))
				{txtCALAM.setText(txtBILAM.getText());	
				txtPORNO.setEnabled(true);
				txtPORNO.requestFocus();
				//cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("Enter PORNO",'N'); 
				}
				else if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
				{txtCALAM.setText(txtBILAM.getText());				
				txtPMDDT.requestFocus();
				//cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("Enter PORNO date.",'N'); 
				}
				else
				{
					//txtCALAM.setText(txtBILAM.getText());
					txtGRNNO.requestFocus();
					setMSG("Enter the GRIN NO. ..",'N'); 
				}
			}

			else if(M_objSOURC == txtPORNO)
			{
				if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07"))
				{

					txtCURCD.setEnabled(true);
					txtCURCD.requestFocus();
					
					setMSG("Enter the CURRENCY Code. ..",'N'); 
					
					
				}
				else if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
				{
					txtGRNNO.setEnabled(false);
					txtACPDT.setEnabled(false);	
					getPODATA();
					txtBILAM.requestFocus();
					
					setMSG("Enter the Payment Date ..",'N'); 
				}
			}
			else if(M_objSOURC == txtCURCD)
			{
				if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
				{				
					txtEXGRT.setEnabled(true);
					txtCURDS.setText(getCURDS((txtCURCD.getText().trim())));
					txtEXGRT.requestFocus();
					setMSG("Enter the Exchange Rate",'N'); 
				}
			}
			else if(M_objSOURC == txtEXGRT)
			{
				if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
				{				
					txtPMDDT.requestFocus();
					setMSG("enter pymt date",'N'); 
				}
			}
			else if(M_objSOURC == txtGRNNO)
			{
				getGRNDT();
			}
			
			else if(M_objSOURC == txtPMDDT)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("saves the record",'N'); 

			}
		}
	}
	public void getPODATA()
	{
		String L_strCURDS="";
		String L_strCURCD="";
		String L_strEXGRT="";
		String L_strBLAMT="";


		if(txtPORNO.getText().trim().length()>0)
		{
			try{

					M_strSQLQRY = "select po_curcd,cmt_codds,po_exgrt,po_porvl from mm_pomst left outer join co_cdtrn on  po_curcd=cmt_codcd where po_porno='"+txtPORNO.getText().trim()+"' and cmt_cgstp='COXXCUR' and po_strtp='"+M_strSBSCD.substring(2,4)+"' and po_stsfl not in ('O','X')";
					System.out.println("**getPODATA**"+M_strSQLQRY);
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						M_rstRSSET.next();
						L_strCURDS=M_rstRSSET.getString("cmt_codds");
						txtCURDS.setText(L_strCURDS);
						L_strCURCD=M_rstRSSET.getString("po_curcd");
						txtCURCD.setText(L_strCURCD);						
						L_strEXGRT=M_rstRSSET.getString("po_exgrt");
						txtEXGRT.setText(L_strEXGRT);
						L_strBLAMT=M_rstRSSET.getString("po_porvl");
						txtBILAM.setText(L_strBLAMT);
						//M_rstRSSET.close();
					}
			}
			catch(Exception e)
			{
				
				setMSG("error in getPODATA",'E');
			}
			

		}
	}

		public String getCURDS(String LP_strCURCD)
		{
			String L_strCURDS="";
			String L_srtCURCD=LP_strCURCD;
			try{
				M_strSQLQRY = "Select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgstp='COXXCUR' and cmt_codcd='"+L_srtCURCD+"'";
				//System.out.println("getCURCDS QRY :"+M_strSQLQRY);
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					M_rstRSSET.next();
					L_strCURDS=M_rstRSSET.getString("cmt_codds");
					M_rstRSSET.close();
				}
			}
			catch(Exception e)
			{
				setMSG("error inside getCURDS",'E');
			}


			return L_strCURDS;
		}

		public void stateChanged(ChangeEvent L_CE)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
				return;
			if(tblCOTAX.isEditing())
				tblCOTAX.getCellEditor().stopCellEditing();
			if(tblITDTL.isEditing())
				tblITDTL.getCellEditor().stopCellEditing();
			if(tblITTAX.isEditing())
				tblITTAX.getCellEditor().stopCellEditing();
			tblITDTL.setRowSelectionInterval(0,0);
			tblITDTL.setColumnSelectionInterval(0,0);
			tblCOTAX.setRowSelectionInterval(0,0);
			tblCOTAX.setColumnSelectionInterval(0,0);
			tblITTAX.setRowSelectionInterval(0,0);
			tblITTAX.setColumnSelectionInterval(0,0);
		}
		public void actionPerformed(ActionEvent L_AE)
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst )
			{
				//if( cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				//cmbBLPTP.requestFocus();
				if( cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
					txtSRLNO.requestFocus();
				else if(!(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)))
					
					cmbBLPTP.requestFocus();

			}
			
			
			if(M_objSOURC == cmbBLPTP )
			{
				try{
					txtGRNNO.setEnabled(true);
					txtACPDT.setEnabled(true);
					if(cmbBLPTP.getSelectedIndex()==0)
						clrCOMP();
					if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
						{
						txtGRNNO.setEnabled(false);
						txtACPDT.setEnabled(false);

					}
					if(cmbBLPTP.getSelectedIndex()>0)
					{	


						strBLPTP=((String)cmbBLPTP.getSelectedItem()).substring(0,2);
						System.out.println("*******strBLPTP** :"+strBLPTP);	
						txtBLPDT.setText(cl_dat.M_strLOGDT_pbst);			

						//calendar.(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).getDate(),1);
						calendar.add(calendar.DAY_OF_MONTH,1);		        
						//nxtDATE=calendar.getInstance().add(M_fmtDBDAT.parse(cl_dat.M_strLOGDT_pbst).getDate());//.getDate(),1);
						txtPMDDT.setText(M_fmtLCDAT.format(calendar.getTime()));
						calendar.add(calendar.DAY_OF_MONTH,-1);			
					}

					txtVENCD.requestFocus();

				}
				catch(Exception ex)
				{
					
					setMSG("error in BIL TP combo", 'E');
				}
			}



			if(M_objSOURC == btnPROCS)
			{
				dblITMVL=0.00;
				dblEXGRT=0.0;
				dblTXVAL =0.0;
				dblBILVL =0.0;
				if(tblITDTL.isEditing())
					tblITDTL.getCellEditor().stopCellEditing();
				tblITDTL.setColumnSelectionInterval(0,0);
				tblITDTL.setRowSelectionInterval(0,0);
				calBILVL(txtBILNO.getText().trim());
			}
			if(M_objSOURC == txtSRLNO)
			{
				String L_strSRLNO = txtSRLNO.getText().trim(); 
				clrCOMP();
				txtSRLNO.setText(L_strSRLNO);
				getDATA();
				// For displaying the Processed values
				/*  dblITMVL=0.00;
		dblEXGRT=0.0;
		dblTXVAL =0.0;
		dblBILVL =0.0;
		if(tblITDTL.isEditing())
			tblITDTL.getCellEditor().stopCellEditing();
		tblITDTL.setColumnSelectionInterval(0,0);
		tblITDTL.setRowSelectionInterval(0,0);
		calBILVL(txtBILNO.getText().trim());*/
			}
			else if(M_objSOURC == txtOTPMT)
				txtOTDED.requestFocus();
		}
		void exeHLPOK()
		{
			super.exeHLPOK();	
			String L_strMATCD ="";
			int L_intROWID = tblITDTL.getSelectedRow();
			StringTokenizer L_STRTKN;
			String L_strPINQT ="0";

			try
			{
				setMSG("",'N');
				if(M_strHLPFLD.equals("txtCURCD"))
				{
					L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
					txtCURCD.setText(L_STRTKN.nextToken());
					txtCURDS.setText(L_STRTKN.nextToken());

				}
				else if(M_strHLPFLD.equals("txtPORNO"))
				{
					L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
					txtPORNO.setText(L_STRTKN.nextToken());
					

				}
				else if((M_strHLPFLD.equals("txtVENCD"))||(M_strHLPFLD.equals("txtVENCDF2")))
				{
					L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");

					txtVENCD.setText(L_STRTKN.nextToken());
					txtVENNM.setText(L_STRTKN.nextToken());
					/*if(txtVENCD.getText().trim().equalsIgnoreCase("D0098"))
					{
						txtVENNM.setEnabled(true);
						txtVENNM.requestFocus();
					}
					else
					{
						txtVENNM.setEnabled(false);
					}*/
					
					
				}
				else if(M_strHLPFLD.equals("txtGRNNO"))
				{
					txtGRNNO.setText(cl_dat.M_strHLPSTR_pbst);
				}

				else if(M_strHLPFLD.equals("txtSRLNO"))
				{
					txtSRLNO.setText(cl_dat.M_strHLPSTR_pbst);
				}
				else if(M_strHLPFLD.equals("tblCOTAX"))
				{
					L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
					tblCOTAX.setValueAt(Boolean.TRUE,tblCOTAX.getSelectedRow(),0);
					String L_strTEMP=L_STRTKN.nextToken();
					if(hstTXCAT==null) hstTXCAT=new Hashtable<String,String>(10,0.2f);
					((JTextField)tblCOTAX.cmpEDITR[1]).setText(L_strTEMP);
					hstCOTAX.put(L_strTEMP,"");
					tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),2);
					tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),4);
					strTEMP = L_STRTKN.nextToken().toUpperCase();
					if(strTEMP.equals("%"))
						tblCOTAX.setValueAt("P",tblCOTAX.getSelectedRow(),3);
					else
						tblCOTAX.setValueAt(strTEMP,tblCOTAX.getSelectedRow(),3);
				}
				else if(M_strHLPFLD.equals("tblITTAX"))
				{
					L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
					String L_strTEMP=L_STRTKN.nextToken();
					((JTextField)tblITTAX.cmpEDITR[2]).setText(L_strTEMP);
					tblITTAX.setValueAt(Boolean.TRUE,tblITTAX.getSelectedRow(),0);
					tblITTAX.setValueAt(L_STRTKN.nextToken(),tblITTAX.getSelectedRow(),3);
					tblITTAX.setValueAt(L_STRTKN.nextToken(),tblITTAX.getSelectedRow(),5);
					strTEMP = L_STRTKN.nextToken();
					if(strTEMP.equals("%"))
						tblITTAX.setValueAt("P",tblITTAX.getSelectedRow(),4);
					else
						tblITTAX.setValueAt(strTEMP,tblITTAX.getSelectedRow(),4);
				}
				else if(M_strHLPFLD.equals("tblITTAX1"))
				{
					((JTextField)tblITTAX.cmpEDITR[1]).setText(cl_dat.M_strHLPSTR_pbst);
				}
			}

			catch(Exception e)
			{
				setMSG(e,"exeHLPOK ");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		boolean vldDATA()
		{
			try
			{
				if(txtVENCD.getText().trim().length() ==0)
				{
					setMSG("Enter Vendor code..",'E');
					return false;
				}
				else if(txtBILNO.getText().trim().length() ==0)
				{
					setMSG("Enter Bill. No...",'E');
					return false;
				}
				else if(txtBILDT.getText().trim().length() ==0)
				{
					setMSG("Enter Bill. Date...",'E');
					return false;
				}
				else if(txtPMDDT.getText().trim().length() ==0)
				{
					setMSG("Enter Payment Due Date...",'E');
					return false;
				}
				else if(txtBILAM.getText().trim().length() ==0)
				{
					setMSG("Enter Bill. Amount...",'E');
					return false;
				}
				else if(Double.parseDouble(txtBILAM.getText().trim())==0)
				{
					setMSG("Bill. Amount can not be zero...",'E');
					return false;
				}
				else if(txtCALAM.getText().trim().length() ==0)
				{
					setMSG("Click on Process button to calculate the amount...",'E');
					return false;
				}
				else if((Double.parseDouble(txtCALAM.getText().trim())==0))
				{
					if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
					{
						return true;
					}
					else
					{
						setMSG("Click on Process button to calculate the amount...",'E');
						return false;
					}
				}
				else if((txtGRNNO.getText().trim().length())==0)
				{
					if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
					{
						return true;
					}
					else
					{
						setMSG("GRIN No. can not be blank...",'E');
						return false;
					}
				}

				else if(M_fmtLCDAT.parse(txtBILDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Bil date can not be greater than current date..",'E');
					return false;
				}
				else if(M_fmtLCDAT.parse(txtBILDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtBILDT.getText().trim()))>0)
				{
					setMSG("Payment Due Date can not be less than Bill date..",'E');
					return false;
				}
				strSTXDS ="";
				strOTHDS ="";
				for(int i=0;i<tblCOTAX.getRowCount();i++)
				{
					if(tblCOTAX.getValueAt(i,1).toString().length() >0 && tblCOTAX.getValueAt(i,1).equals(Boolean.TRUE))
					{
						if(tblCOTAX.getValueAt(i,1).toString().equals("STX"))
							strSTXDS =tblCOTAX.getValueAt(i,2).toString();
						if(tblCOTAX.getValueAt(i,1).toString().equals("OTH"))
							strOTHDS =tblCOTAX.getValueAt(i,2).toString();

						strTEMP = tblCOTAX.getValueAt(i,3).toString();

						{
							if(!strTEMP.equals("P"))
								if(!strTEMP.equals("A"))
								{
									setMSG("Tax flags values can be 'A' or 'P' only..",'E');  
									return false;
								}
							if(Double.parseDouble(tblCOTAX.getValueAt(i,4).toString()) <= 0)
							{
								setMSG("Tax value can not be zero .. ",'E');  
								return false;
							}
						}

					}
				}
				strGTXDS ="";
				strGOTDS =""; // Gradewise other tax
				// check for duplicate tax code
				//////
				for(int i=0;i<tblITTAX.getRowCount();i++)
				{
					if(tblITTAX.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						strTEMP = tblITTAX.getValueAt(i,4).toString();
						if(!strTEMP.equals("P"))
							if(!strTEMP.equals("A"))
							{
								setMSG("Tax flags values can be 'A' or 'P' only..",'E');  
								return false;
							}
						if(Double.parseDouble(tblITTAX.getValueAt(i,5).toString()) <= 0)
						{
							setMSG("Tax value can not be zero .. ",'E');  
							return false;
						}
						if(hstCOTAX.containsKey(tblITTAX.getValueAt(i,2).toString().trim()))
						{
							setMSG("Duplicate Tax Code ..",'E');
							return false;
						}
					}
				}
			}
			catch(Exception L_E)
			{
				
				setMSG("Invalid Data..",'E');
				return false;
			}

			return true;
		}
		void exeSAVE()
		{
			try
			{
				String L_CALAM ;
				double L_OTHAM =0;
				double L_FRTAM =0;
				boolean L_flgUPDRC = true;
				cl_dat.M_btnSAVE_pbst.setEnabled(false);
				cl_dat.M_flgLCUPD_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				/////
				//	L_OTHAM = (Double.parseDouble(txtOTHPA.getText().trim())- Double.parseDouble(txtOTHDA.getText().trim()))/LM_ROWCNT;
				if(!vldDATA())
				{
					setCursor(cl_dat.M_curDFSTS_pbst);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					// Added on 05/07/2005 ,as for prv. year bill passing cumm. receipt /issue
					// value should not be changed.  	
					M_strSQLQRY = "SELECT sp_ystdt,sp_yendt,sp_stsfl from sa_spmst where  sp_syscd ='MM' ";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					String L_strYENDT="",L_strYSTDT="",L_strPRCFL ="";
					if(M_rstRSSET !=null)
					{
						if(M_rstRSSET.next())
						{
							datTEMP = M_rstRSSET.getDate("SP_YSTDT");
							if(datTEMP !=null)
							{
								L_strYSTDT = M_fmtLCDAT.format(datTEMP);
							}
							L_strPRCFL = M_rstRSSET.getString("SP_STSFL");
						}
						M_rstRSSET.close();
					}
					if(!((((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07"))||(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))))
					{
						if(M_fmtLCDAT.parse(txtACPDT.getText().trim()).compareTo(M_fmtLCDAT.parse(L_strYSTDT))<0)
						{
							// Processing is not done for the prevous year
							if(!L_strPRCFL.equals("1"))
								L_flgUPDRC = true;
							else
								L_flgUPDRC = false;
						}
						else
							L_flgUPDRC = true;
					}

					// end of block Added on 05/07/2005
					if(!genDOCNO(strBLPTP))
					{
						setCursor(cl_dat.M_curDFSTS_pbst);
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
						setMSG("Error in generating document No..",'E');
						return;
					}
					System.out.println("001");
					M_strSQLQRY = "Insert into MM_BLMST(BL_CMPCD,BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,BL_BLPDT,";
					M_strSQLQRY += "BL_PRTTP,BL_PRTCD,BL_PRTNM,BL_BILAM,BL_CALAM,BL_OTPMT,BL_OTDED,BL_REMDS,BL_CURCD,BL_EXGRT,BL_PMDDT,BL_TRNFL,BL_STSFL,BL_LUSBY,BL_LUPDT) values(";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+strBLPTP+"',";  // biltype from combo
					M_strSQLQRY += "'"+txtSRLNO.getText().trim() +"',"; 
					M_strSQLQRY += "'"+txtBILNO.getText().trim() +"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBILDT.getText().trim())) +"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"',";
					M_strSQLQRY += "'S',";
					M_strSQLQRY += "'"+txtVENCD.getText().trim()+"',";
					M_strSQLQRY += "'"+txtVENNM.getText().trim()+"',";
					
					
					M_strSQLQRY += "'"+Math.round(Double.parseDouble(txtBILAM.getText().trim())) +"',";
					System.out.println("### bill amt is :"+Math.round(Double.parseDouble(txtBILAM.getText().trim())));
					if(txtCALAM.getText().trim().length() >0)
						{
						//M_strSQLQRY += txtCALAM.getText().trim() +",";
						M_strSQLQRY += "'"+Math.round(Double.parseDouble(txtCALAM.getText().trim())) +"',";
						System.out.println("### cal amt is :"+Math.round(Double.parseDouble(txtCALAM.getText().trim())));
						
						}
					if(txtOTPMT.getText().trim().length() >0)
						M_strSQLQRY += txtOTPMT.getText().trim() +",";
					else
						M_strSQLQRY +="0,";
					if(txtOTDED.getText().trim().length() >0)
						M_strSQLQRY += txtOTDED.getText().trim() +",";
					else
						M_strSQLQRY +="0,";
					M_strSQLQRY += "'"+txtOTHDS.getText().trim() +"',";
					M_strSQLQRY += "'"+txtCURCD.getText().trim() +"',";
					M_strSQLQRY += txtEXGRT.getText().trim() +",";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPMDDT.getText().trim())) +"',";
					M_strSQLQRY += getUSGDTL("BL",'I',"")+")";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					System.out.println("002");
					
					///code added for advance pymt and work order
				if(((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
				{
					System.out.println("**f");
					
					M_strSQLQRY = "Insert into MM_BILTR(BIL_CMPCD,BIL_BILTP,BIL_DOCNO,BIL_DOCRF,BIL_PORRF,BIL_MATCD,BIL_STRTP,BIL_CHLQT,BIL_RECQT,";
					M_strSQLQRY += "BIL_ACPQT,BIL_BILRT,BIL_BILQT,BIL_BLPQT,BIL_ITMVL,BIL_PORVL,BIL_FRTAM,BIL_POUOM,BIL_PORRT,BIL_PERRT,BIL_UCNVL,BIL_TRNFL,BIL_STSFL,BIL_LUSBY,BIL_LUPDT) values(";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";					
					M_strSQLQRY += "'"+((String)cmbBLPTP.getSelectedItem()).substring(0,2)+"',";
					System.out.println("**1f");
					M_strSQLQRY += "'"+ txtSRLNO.getText().trim() +"',";
					M_strSQLQRY += "'XXXXXXXX',";
					System.out.println("**2f");					
					M_strSQLQRY += "'"+ txtPORNO.getText().trim()+"',";	
					M_strSQLQRY += "'XXXXXXXXXX',";
					M_strSQLQRY += "'"+ M_strSBSCD.substring(2,4) +"',";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'"+Math.round(Double.parseDouble(txtBILAM.getText().trim())) +"',";
					System.out.println("### bill amt is(for 06/07) :"+Math.round(Double.parseDouble(txtBILAM.getText().trim())));
					System.out.println("**3f");
					M_strSQLQRY += setNumberFormat(L_FRTAM,2) +",";					
					M_strSQLQRY += "'XXX',";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					
					M_strSQLQRY += getUSGDTL("BIL",'I',"")+")";	
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					System.out.println("**4f");
					
					/*    the fields which are reqd is Y and not reqd in N which is set to 0
					 * Y		M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					Y		M_strSQLQRY += "'01',";
					Y		M_strSQLQRY += "'"+ txtSRLNO.getText().trim() +"',";
					N		M_strSQLQRY += "'"+ txtGRNNO.getText().trim()+"',";
					Y		M_strSQLQRY += "'"+ txtPORNO.getText().trim()+"',";
					XXXXXXXXXX		M_strSQLQRY += "'"+ tblITDTL.getValueAt(i,TBL_MATCD) +"',";
					Y		M_strSQLQRY += "'"+ M_strSBSCD.substring(2,4) +"',";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_CHLQT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_RECQT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_ACPQT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PORRT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_BILQT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_BLPQT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_ITVAL) +",";
					BILL AMOUNT		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PORVL) +",";
					N		M_strSQLQRY += setNumberFormat(L_FRTAM,2) +",";
					N		M_strSQLQRY += "'"+tblITDTL.getValueAt(i,TBL_POUOM) +"',";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PORRT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PERRT) +",";
					N		M_strSQLQRY += tblITDTL.getValueAt(i,TBL_CNVFT) +",";
					y		M_strSQLQRY += getUSGDTL("BIL",'I',"")+")";
							
					 */
								
				}				
				// code pending  ends
				
				else  //if 07 or 06 not selected										
				{
					for(int i=0;i<tblITDTL.getRowCount();i++)
					{
						if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							M_strSQLQRY = "Insert into MM_BILTR(BIL_CMPCD,BIL_BILTP,BIL_DOCNO,BIL_DOCRF,BIL_PORRF,BIL_MATCD,BIL_STRTP,BIL_CHLQT,BIL_RECQT,";
							M_strSQLQRY += "BIL_ACPQT,BIL_BILRT,BIL_BILQT,BIL_BLPQT,BIL_ITMVL,BIL_PORVL,BIL_FRTAM,BIL_POUOM,BIL_PORRT,BIL_PERRT,BIL_UCNVL,BIL_TRNFL,BIL_STSFL,BIL_LUSBY,BIL_LUPDT) values(";
							M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'"+strBLPTP+"',";
							M_strSQLQRY += "'"+ txtSRLNO.getText().trim() +"',";
							M_strSQLQRY += "'"+ txtGRNNO.getText().trim()+"',";
							M_strSQLQRY += "'"+ txtPORNO.getText().trim()+"',";
							M_strSQLQRY += "'"+ tblITDTL.getValueAt(i,TBL_MATCD) +"',";
							M_strSQLQRY += "'"+ M_strSBSCD.substring(2,4) +"',";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_CHLQT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_RECQT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_ACPQT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PORRT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_BILQT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_BLPQT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_ITVAL) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PORVL) +",";
							M_strSQLQRY += setNumberFormat(L_FRTAM,2) +",";
							M_strSQLQRY += "'"+tblITDTL.getValueAt(i,TBL_POUOM) +"',";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PORRT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_PERRT) +",";
							M_strSQLQRY += tblITDTL.getValueAt(i,TBL_CNVFT) +",";
							M_strSQLQRY += getUSGDTL("BIL",'I',"")+")";
							//					if(true)
							//					return;
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							System.out.println("003");
							if(cl_dat.M_flgLCUPD_pbst)
							{
								//  updGRMST_BIL(LP_STRTP,LP_GRNNO,LP_BILRF,LP_MATCD,LP_BLPQT,LP_FRTAM,LP_VATBL,LP_ITVAL,LP_USRCD,LP_DELFL) 
								/* 
								  System.out.println(M_strSBSCD.substring(2,4)+","+txtGRNNO.getText().trim()+","+txtSRLNO.getText().trim()+","+
										tblITDTL.getValueAt(i,TBL_MATCD).toString()+","+
										setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_BLPQT).toString()),3)+","+
										setNumberFormat(L_FRTAM,2)+","+
										setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_VATBL).toString()),2)+","+
										setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_ITVAL).toString()),2)+","+
										cl_dat.M_strUSRCD_pbst+","+
										"'A'"+",");
								 */
									//for option other than 07 or 06	
								if(!((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
									{
									cstGRMST_BIL = cl_dat.M_conSPDBA_pbst.prepareCall("{ call updGRMST_BIL(?,?,?,?,?,?,?,?,?,?,?)}");
									System.out.println("003a");
									cstGRMST_BIL.setString(1,cl_dat.M_strCMPCD_pbst);
									cstGRMST_BIL.setString(2,M_strSBSCD.substring(2,4).trim());
									System.out.println("003b");
									cstGRMST_BIL.setString(3,txtGRNNO.getText().trim());
									System.out.println("003c");
									cstGRMST_BIL.setString(4,txtSRLNO.getText().trim());
									System.out.println("003d");
									cstGRMST_BIL.setString(5,tblITDTL.getValueAt(i,TBL_MATCD).toString());
									System.out.println("003e");
									cstGRMST_BIL.setString(6,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_BLPQT).toString()),3));
									System.out.println("003f");
									cstGRMST_BIL.setString(7,setNumberFormat(L_FRTAM,2));
									System.out.println("003g");
									cstGRMST_BIL.setString(8,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_VATBL).toString()),2));
									System.out.println("003h");
									cstGRMST_BIL.setString(9,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_ITVAL).toString()),2));
									System.out.println("003i");
									cstGRMST_BIL.setString(10,cl_dat.M_strUSRCD_pbst);
									System.out.println("003j");
									cstGRMST_BIL.setString(11,"A");
									System.out.println("003k");
									cstGRMST_BIL.executeUpdate();
								}

								/*						cstGRMST_BIL.setString(1,M_strSBSCD.substring(2,4).trim());
			System.out.println("003b");
						cstGRMST_BIL.setString(2,"'"+txtGRNNO.getText().trim()+"'");
			System.out.println("003c");
						cstGRMST_BIL.setString(3,"'"+txtSRLNO.getText().trim()+"'");
			System.out.println("003d");
						cstGRMST_BIL.setString(4,"'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()+"'");
			System.out.println("003e");
						cstGRMST_BIL.setString(5,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_BLPQT).toString()),3));
			System.out.println("003f");
						cstGRMST_BIL.setString(6,setNumberFormat(L_FRTAM,2));
			System.out.println("003g");
						cstGRMST_BIL.setString(7,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_VATBL).toString()),2));
			System.out.println("003h");
						cstGRMST_BIL.setString(8,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_ITVAL).toString()),2));
			System.out.println("003i");
						cstGRMST_BIL.setString(9,"'"+cl_dat.M_strUSRCD_pbst+"'");
			System.out.println("003j");
						cstGRMST_BIL.setString(10,"'A'");
			System.out.println("003k");*/

							}

							System.out.println("004");
							if((cl_dat.M_flgLCUPD_pbst) && L_flgUPDRC)
							{
								float L_fltPORVL=Float.parseFloat(tblITDTL.getValueAt(i,TBL_PORVL).toString());
								float L_fltBILVL=Float.parseFloat(tblITDTL.getValueAt(i,TBL_ITVAL).toString());
								M_strSQLQRY = "SELECT STP_CRCVL,STP_CRCQT,STP_YCSVL,STP_YCSQT from MM_STPRC WHERE ";
								M_strSQLQRY+=" STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP='"+M_strSBSCD.substring(2,4)+"' and STP_MATCD='"+tblITDTL.getValueAt(i,TBL_MATCD)+"'";
								M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
								float L_fltCRCVL=0f,L_fltCRCQT=0F,L_fltYCSVL=0F,L_fltYCSQT=0F,L_fltYCLRT=0F,L_fltWAVRT=0F;
								boolean L_flgFOUND = false;
								if(M_rstRSSET !=null)
								{
									if(M_rstRSSET.next())
									{
										L_fltCRCVL = M_rstRSSET.getFloat("STP_CRCVL");
										L_fltCRCQT = M_rstRSSET.getFloat("STP_CRCQT");
										L_fltYCSVL = M_rstRSSET.getFloat("STP_YCSVL");
										L_fltYCSQT = M_rstRSSET.getFloat("STP_YCSQT");
										L_flgFOUND = true;
									}
									M_rstRSSET.close();	
									if(L_flgFOUND)
									{
										M_strSQLQRY="Update MM_STPRC set STP_CRCVL=STP_CRCVL-"+String.valueOf(L_fltPORVL)+"+"+String.valueOf(L_fltBILVL);		
										if(L_fltYCSQT >0)
										{
											L_fltYCLRT = (L_fltYCSVL -L_fltPORVL+L_fltBILVL)/L_fltYCSQT;
											M_strSQLQRY+= ",STP_YCSVL=STP_YCSVL- "+String.valueOf(L_fltPORVL)+"+"+String.valueOf(L_fltBILVL);
											M_strSQLQRY+= ",STP_YCLRT = "+String.valueOf(L_fltYCLRT);
										}
										if(L_fltCRCQT >0)
										{
											L_fltWAVRT = (L_fltCRCVL -L_fltPORVL+L_fltBILVL)/L_fltCRCQT;
											M_strSQLQRY+= ",STP_WAVRT = "+String.valueOf(L_fltWAVRT);
										}
										M_strSQLQRY+=" where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP='"+M_strSBSCD.substring(2,4)+"' and STP_MATCD='"+tblITDTL.getValueAt(i,TBL_MATCD)+"' and STP_UOMCD='"+tblITDTL.getValueAt(i,TBL_UOMCD)+"'";
										cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
									}
									//System.out.println("005");
								}
								//M_rstRSSET.close();
							}
						}
					}
				}//end of else 
					System.out.println("005a");
					//if(cl_dat.M_flgLCUPD_pbst)
					//{
					updDOCNO(strBLPTP);
					//updDOCNO("01");// orignal 
					if(cl_dat.M_flgLCUPD_pbst)
					{	
						if(insTXDTL(txtSRLNO.getText().trim()))
						{
							System.out.println("005b");
							adjISSVL();
							cstPLTRN_BIL = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_BIL(?,?,?,?)");
							cstPLTRN_BIL.setString(1,cl_dat.M_strCMPCD_pbst);
							cstPLTRN_BIL.setString(2,strBLPTP);
							cstPLTRN_BIL.setString(3,txtSRLNO.getText().trim());
							cstPLTRN_BIL.setString(4,txtVENNM.getText().trim());
							cstPLTRN_BIL.executeUpdate();
							cl_dat.M_conSPDBA_pbst.commit();

							if(cl_dat.exeDBCMT("exeSAVE"))
							{
								setMSG("Data Updated successfully",'N');
								JOptionPane.showMessageDialog(this,"Please Note Down Reference Number\n"+txtSRLNO.getText(),"",JOptionPane.INFORMATION_MESSAGE);
								clrCOMP();
							}
							else
								setMSG("Error in saving Data..",'N');
						}
					}

				}
				// Deltion commented as BIL_PORVL and BIL_ITMVL are not updated in system.Check and then use.
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					System.out.println("006");
					M_strSQLQRY = "UPDATE MM_BILTR SET ";
					M_strSQLQRY += getUSGDTL("BIL",'U',"X");
				    //old code
					//M_strSQLQRY += " WHERE BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BIL_BILTP = '"+strBLPTP+"'";
					//new code
					M_strSQLQRY += " WHERE BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst;
					M_strSQLQRY += " and BIL_DOCNO = '"+txtSRLNO.getText().trim()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	

					System.out.println("007");
					if(cl_dat.M_flgLCUPD_pbst)
					{
						M_strSQLQRY = "UPDATE MM_BLMST SET ";
						M_strSQLQRY += getUSGDTL("BL",'U',"X"); 
						//old code
						//M_strSQLQRY += " WHERE BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '"+strBLPTP+"'";
						//new code
						M_strSQLQRY += " WHERE BL_CMPCD='"+cl_dat.M_strCMPCD_pbst;						
						M_strSQLQRY += " and BL_DOCNO = '"+txtSRLNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					}
					if(cl_dat.M_flgLCUPD_pbst)
					{
						int L_intROWCT = tblITDTL.getRowCount();
						for(int i=0;i<L_intROWCT;i++)
						{
							if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
							{
								System.out.println("008");
								//  updGRMST_BIL(LP_STRTP,LP_GRNNO,LP_BILRF,LP_MATCD,LP_BLPQT,LP_FRTAM,LP_VATBL,LP_ITVAL,LP_USRCD,LP_DELFL) 
								float L_fltBILVL=Float.parseFloat(tblITDTL.getValueAt(i,TBL_ITVAL).toString());
								float L_fltPORVL=Float.parseFloat(tblITDTL.getValueAt(i,TBL_PORVL).toString());
								cstGRMST_BIL = cl_dat.M_conSPDBA_pbst.prepareCall("{ call updGRMST_BIL(?,?,?,?,?,?,?,?,?,?)}");
								cstGRMST_BIL.setString(1,cl_dat.M_strCMPCD_pbst);
								cstGRMST_BIL.setString(2,M_strSBSCD.substring(2,4));
								cstGRMST_BIL.setString(3,txtGRNNO.getText().trim());
								cstGRMST_BIL.setString(4,"");
								cstGRMST_BIL.setString(5,tblITDTL.getValueAt(i,TBL_MATCD).toString());
								cstGRMST_BIL.setString(6,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_TRNQT).toString()),3));
								cstGRMST_BIL.setString(7,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_FRTAM).toString()),2));
								cstGRMST_BIL.setString(8,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_VATBL).toString()),2));
								cstGRMST_BIL.setString(9,setNumberFormat(Double.parseDouble(tblITDTL.getValueAt(i,TBL_ITVAL).toString()),2));
								cstGRMST_BIL.setString(10,cl_dat.M_strUSRCD_pbst);
								cstGRMST_BIL.setString(11,"D");
								cstGRMST_BIL.executeUpdate();

								//M_strSQLQRY = "UPDATE MM_GRMST SET GR_BILQT = isnull(GR_BILQT,0) - "+tblITDTL.getValueAt(i,TBL_TRNQT).toString();
								//M_strSQLQRY +=",GR_BILRF ='',";
								//M_strSQLQRY +="GR_FRTAM =isnull(GR_FRTAM,0) - "+tblITDTL.getValueAt(i,TBL_FRTAM).toString()+",";
								//M_strSQLQRY +="GR_BILVL =isnull(GR_BILVL,0) - "+tblITDTL.getValueAt(i,TBL_ITVAL).toString()+",";
								//M_strSQLQRY += getUSGDTL("GR",'U',null); 
								//M_strSQLQRY += " WHERE GR_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
								//M_strSQLQRY += " and GR_GRNNO = '"+txtGRNNO.getText().trim()+"'";
								//M_strSQLQRY += " and GR_MATCD = '"+tblITDTL.getValueAt(i,TBL_MATCD).toString()+"'";
								//cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
								// Number format exception prob. in this query
								M_strSQLQRY="Update MM_STPRC set STP_CRCVL=STP_CRCVL+"+String.valueOf(L_fltPORVL)+" - "+tblITDTL.getValueAt(i,TBL_ITVAL).toString()+
								",STP_YCSVL=STP_CRCVL+"+String.valueOf(L_fltPORVL)+" - "+tblITDTL.getValueAt(i,TBL_ITVAL).toString()+
								" where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP='"+M_strSBSCD.substring(2,4)+"' and STP_MATCD='"+tblITDTL.getValueAt(i,TBL_MATCD)+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
						}
					}
					if(!cl_dat.M_flgLCUPD_pbst)
					{
						cl_dat.M_conSPDBA_pbst.rollback();
					}
					if(cl_dat.M_flgLCUPD_pbst)
					{
						// Add query for deleting the tax details related to doc no.
						if(cl_dat.exeDBCMT("exeSAVE"))
						{
							setMSG("Data Updated successfully",'N');
							clrCOMP();
						}
					}
					else
					{
						setMSG("Error in updating..",'E');
					}
				}

				cl_dat.M_btnSAVE_pbst.setEnabled(true);
			}
			catch(Exception e)
			{
				
				setMSG(e,"exeSAVE");
				setCursor(cl_dat.M_curDFSTS_pbst);
				cl_dat.exeDBCMT("exeSAVE");
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
			}
		}




		private boolean genDOCNO(String P_strBILTP)
		{
			String L_DOCNO  = "",  L_CODCD = "", L_CCSVL = "";
			try
			{
				M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXBLP' and ";
				M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strBILTP + "'";
				System.out.println("**M_strSQLQRY in docno** : "+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						L_CODCD = M_rstRSSET.getString("CMT_CODCD");
						L_CCSVL = M_rstRSSET.getString("CMT_CCSVL");
						M_rstRSSET.close();
					}
				}
				L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
				for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
					L_DOCNO += "0";

				L_CCSVL = L_DOCNO + L_CCSVL;
				L_DOCNO = L_CODCD + L_CCSVL;
				txtSRLNO.setText(L_DOCNO);
				txtBLPDT.setText(cl_dat.M_strLOGDT_pbst);
				System.out.println(">>>>>P_strBILTP**"+P_strBILTP);
			}
			catch(Exception L_EX)
			{
				
				setMSG(L_EX,"genGRNNO");
				return false;
			}
			return true;
		}
		private void updDOCNO(String P_strBILTP)
		{
			try
			{
				M_strSQLQRY = "Update CO_CDTRN set ";
				M_strSQLQRY += " CMT_CCSVL = '" + txtSRLNO.getText().trim().substring(3) + "',";
				M_strSQLQRY += getUSGDTL("CMT",'U',"");
				M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
				M_strSQLQRY += " and CMT_CGSTP = 'MMXXBLP'";	
				M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strBILTP + "'";			
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				cl_dat.exeDBCMT("updDOCNO");
			}
			catch(Exception e)
			{
				setMSG(e,"updDOCNO");
			}
		}
		private boolean vldVENCD(String P_strVENCD)
		{
			try
			{
				String L_strPRTNM="";
				int L_intDAYS =0;
				M_strSQLQRY ="SELECT PT_PRTCD,PT_PRTNM,(days(current_date)-days(PT_EVLDT))L_DAYS FROM CO_PTMST WHERE PT_PRTTP ='S' and PT_PRTCD ='"+P_strVENCD.trim() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
						txtVENNM.setText(L_strPRTNM);
						L_intDAYS = M_rstRSSET.getInt("L_DAYS");
						//return true;
					}
					M_rstRSSET.close();
				}
				M_strSQLQRY ="SELECT MAX(CTP_LPODT),days(current_date)-days(MAX(CTP_LPODT))L_DAYS FROM CO_CTPTR WHERE CTP_PRTTP ='S' and CTP_PRTCD ='"+P_strVENCD.trim() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt("L_DAYS")>730)
						{
							if(L_intDAYS > 730)
								setMSG("No Order has been Placed for last 2 years ..",'E');
							//return false;
						}
						M_rstRSSET.close();
					}
					else
					{
						setMSG("Last Order date not found for checking..",'E');
					}
				return true;
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"vldPRTCD");
				return false;
			}	

		}
		private boolean getDATA()
		{
			String L_strMATCD="";
			ResultSet L_rstRSSET;
			try
			{
				int L_intRECCT =0;
				String strTMPBLTP="";
				setCursor(cl_dat.M_curDFSTS_pbst);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					if(txtSRLNO.getText().substring(1,3).equals("06")||txtSRLNO.getText().substring(1,3).equals("07"))
					{
						M_strSQLQRY = "Select BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,CONVERT(varchar,BL_BLPDT,103),BL_PMDDT,BL_PRTTP,BL_PRTCD,BL_PRTNM,BL_BILAM,BL_CALAM,BL_OTPMT,BL_OTDED,BL_REMDS,";
						M_strSQLQRY += " BL_CURCD,BL_EXGRT,BIL_POUOM,BIL_UCNVL,BIL_PERRT,BIL_PORRT,BIL_DOCRF,BIL_MATCD,BIL_STRTP,";
						M_strSQLQRY += " 0 BIL_CHLQT,0 BIL_RECQT,0 BIL_ACPQT,0 BIL_BILRT,0 BIL_BILQT,0 BIL_BLPQT,0 BIL_ITMVL,0 BIL_PORVL,0 BIL_FRTAM,'XXXXXXXXXX' GR_MATCD,0 GR_PORNO,CONVERT(varchar,GR_ACPDT,103),'XXXXXXXX' gr_grnno ";
						M_strSQLQRY += " ,'XXX' ST_UOMCD,'XXXXXXXXXX' ST_MATDS,0 GR_RECQT,0 GR_BILQT,0 GR_CHLQT,0 GR_MODVL from MM_BLMST,MM_BILTR ";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
						{
							strTMPBLTP="' ";
							
						}
						else
							strTMPBLTP="' AND BL_BILTP = '"+strBLPTP+"'";
							
						M_strSQLQRY += " where BL_CMPCD = BIL_CMPCD and BL_BILTP = BIL_BILTP and BL_DOCNO = BIL_DOCNO AND BL_CMPCD=BIL_CMPCD  and BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+strTMPBLTP;
						M_strSQLQRY += " AND BL_DOCNO = '"+txtSRLNO.getText().trim() +"' ";
						M_strSQLQRY += " and isnull(BL_STSFL,'') <> 'X' and BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(BIL_STSFL,'') <> 'X' ";
						

					
					/* 
					M_strSQLQRY += "'"+ txtSRLNO.getText().trim() +"',";
					M_strSQLQRY += "'XXXXXXXX',";
					System.out.println("**2f");					
					M_strSQLQRY += "'"+ txtPORNO.getText().trim()+"',";	
					M_strSQLQRY += "'XXXXXXXXXX',";
					M_strSQLQRY += "'"+ M_strSBSCD.substring(2,4) +"',";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";					
					M_strSQLQRY += Math.round(Double.parseDouble(txtBILAM.getText().trim())) +",";
					System.out.println("### bill amt is(for 06/07) :"+Math.round(Double.parseDouble(txtBILAM.getText().trim())));
					System.out.println("**3f");
					M_strSQLQRY += setNumberFormat(L_FRTAM,2) +",";					
					M_strSQLQRY += "'XXX',";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";
					M_strSQLQRY += "'0'" +",";	
					
					*/
					}				
					else //if doc no dosent contain 07 or 06
					{	
						M_strSQLQRY = "Select BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,BL_BLPDT,BL_PMDDT,BL_PRTTP,BL_PRTCD,BL_PRTNM,BL_BILAM,BL_CALAM,BL_OTPMT,BL_OTDED,BL_REMDS,";
						M_strSQLQRY += " BL_CURCD,BL_EXGRT,BIL_POUOM,BIL_UCNVL,BIL_PERRT,BIL_PORRT,BIL_DOCRF,BIL_MATCD,BIL_STRTP,";
						M_strSQLQRY += " BIL_CHLQT,BIL_RECQT,BIL_ACPQT,BIL_BILRT,BIL_BILQT,BIL_BLPQT,BIL_ITMVL,BIL_PORVL,BIL_FRTAM,GR_MATCD,GR_PORNO,GR_ACPDT,gr_grnno ";
						M_strSQLQRY += " ,ST_UOMCD,ST_MATDS,sum(isnull(GR_RECQT,0)) GR_RECQT,sum(isnull(GR_BILQT,0)) GR_BILQT,sum(isnull(GR_CHLQT,0)) GR_CHLQT,sum(isnull(GR_MODVL,0)) GR_MODVL from MM_BLMST,MM_BILTR,MM_GRMST,mm_stmst ";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
						{
							strTMPBLTP="' ";
							
						}
						else
							strTMPBLTP="' AND BL_BILTP = '"+strBLPTP+"'";
							
						M_strSQLQRY += " where BL_CMPCD = BIL_CMPCD and BL_BILTP = BIL_BILTP and BL_DOCNO = BIL_DOCNO AND BL_CMPCD=BIL_CMPCD AND BIL_DOCRF = GR_GRNNO and BIL_CMPCD=GR_CMPCD and BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+strTMPBLTP+" and bil_strtp=gr_strtp AND GR_STRTP = ST_STRTP AND GR_MATCD = ST_MATCD and GR_CMPCD=ST_CMPCD ";
						M_strSQLQRY += " AND BL_DOCNO = '"+txtSRLNO.getText().trim() +"' and GR_MATCD=BIL_MATCD and GR_CMPCD=BIL_CMPCD ";
						M_strSQLQRY += " and isnull(BL_STSFL,'') <> 'X' and BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(BIL_STSFL,'') <> 'X' ";
						M_strSQLQRY += " group by  BL_BILTP,BL_DOCNO,BL_BILNO,BL_BILDT,BL_BLPDT,BL_PMDDT,BL_PRTTP,BL_PRTCD,BL_PRTNM,BL_BILAM,BL_CALAM,BL_OTPMT,BL_OTDED,BL_REMDS,";
						M_strSQLQRY += " BL_CURCD,BL_EXGRT,BIL_POUOM,BIL_UCNVL,BIL_PERRT,BIL_PORRT,BIL_DOCRF,BIL_MATCD,BIL_STRTP,";
						M_strSQLQRY += " BIL_CHLQT,BIL_RECQT,BIL_ACPQT,BIL_BILRT,BIL_BILQT,BIL_BLPQT,BIL_ITMVL,BIL_PORVL,BIL_FRTAM,GR_MATCD,GR_PORNO,GR_ACPDT,gr_grnno ";
						M_strSQLQRY += " ,ST_UOMCD,ST_MATDS ";
					}
				}
				//M_strSQLQRY += " order by GR_CHLDT,GR_CHLNO";
				System.out.println("3 "+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
				//code added to reset
				txtCALAM.setText("0.00");
				txtOTPMT.setText("0.00");
				txtOTDED.setText("0.00");
				if(M_rstRSSET !=null)				
					while(M_rstRSSET.next())
					{
						if(L_intRECCT ==0)
						{
							datTEMP = M_rstRSSET.getDate("GR_ACPDT");
							if(datTEMP !=null)
							{
								txtACPDT.setText(M_fmtLCDAT.format(datTEMP));
							}
							else
							{
								if(!(txtSRLNO.getText().substring(1,3).equals("06")||txtSRLNO.getText().substring(1,3).equals("07")))
								{
									setMSG("GRIN is not Accepted, Bill passing is  not allowed..",'E');
									return false;
								}
								
							}
							strPORNO = nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),"");
							L_strMATCD = nvlSTRVL(M_rstRSSET.getString("BIL_MATCD"),"");
							txtPORNO.setText(strPORNO);	
							txtGRNNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""));	
							txtSRLNO.setText(nvlSTRVL(M_rstRSSET.getString("BL_DOCNO"),""));	
							txtBILNO.setText(nvlSTRVL(M_rstRSSET.getString("BL_BILNO"),""));	
							txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("BL_PRTCD"),""));
							txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("BL_PRTNM"),""));							
							System.out.println("***BL_BILAM**"+M_rstRSSET.getString("BL_BILAM"));
							System.out.println("***BL_CALAM**"+M_rstRSSET.getString("BL_CALAM"));
							System.out.println("***BIL_ITMVL**"+M_rstRSSET.getString("BIL_ITMVL"));							
							txtBILAM.setText(nvlSTRVL(M_rstRSSET.getString("BL_BILAM"),""));	
							txtCALAM.setText(nvlSTRVL(M_rstRSSET.getString("BL_CALAM"),""));	
							txtOTPMT.setText(nvlSTRVL(M_rstRSSET.getString("BL_OTPMT"),""));	
							txtOTDED.setText(nvlSTRVL(M_rstRSSET.getString("BL_OTDED"),""));	
							txtOTHDS.setText(nvlSTRVL(M_rstRSSET.getString("BL_REMDS"),""));	
							datTEMP = M_rstRSSET.getDate("BL_BILDT");
							if(datTEMP !=null)
							{
								txtBILDT.setText(M_fmtLCDAT.format(datTEMP));
							}
							datTEMP = M_rstRSSET.getDate("BL_BLPDT");
							if(datTEMP !=null)
							{
								txtBLPDT.setText(M_fmtLCDAT.format(datTEMP));
							}
							datTEMP = M_rstRSSET.getDate("BL_PMDDT");
							if(datTEMP !=null)
							{
								txtPMDDT.setText(M_fmtLCDAT.format(datTEMP));
							}
							//	txtCURCD.setText(nvlSTRVL(M_rstRSSET.getString("PO_CURCD"),""));	
							//	txtEXGRT.setText(nvlSTRVL(M_rstRSSET.getString("PO_EXGRT"),""));	
						}
						tblITDTL.setValueAt(Boolean.TRUE,L_intRECCT,TBL_CHKFL);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_MATCD"),""),L_intRECCT,TBL_MATCD);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_CHLQT"),"0"),L_intRECCT,TBL_CHLQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_RECQT"),"0"),L_intRECCT,TBL_RECQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_ACPQT"),"0"),L_intRECCT,TBL_ACPQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_BILQT"),"0"),L_intRECCT,TBL_BILQT);
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_BLPQT"),"0"),L_intRECCT,TBL_TRNQT);
						if( cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
					    	tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_BLPQT"),"0"),L_intRECCT,TBL_BLPQT);
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_FRTAM"),"0"),L_intRECCT,TBL_FRTAM);
						System.out.println("***BIL_ITMVL just before table**"+nvlSTRVL(M_rstRSSET.getString("BIL_FRTAM"),"0"));							
						
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_ITMVL"),"0"),L_intRECCT,TBL_ITVAL);
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_PORVL"),"0"),L_intRECCT,TBL_PORVL);
						txtCURCD.setText(nvlSTRVL(M_rstRSSET.getString("BL_CURCD"),""));
						txtEXGRT.setText(nvlSTRVL(M_rstRSSET.getString("BL_EXGRT"),""));
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),L_intRECCT,TBL_UOMCD);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),L_intRECCT,TBL_MATDS);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_POUOM"),""),L_intRECCT,TBL_POUOM);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_UCNVL"),""),L_intRECCT,TBL_CNVFT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_PERRT"),""),L_intRECCT,TBL_PERRT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_PORRT"),""),L_intRECCT,TBL_PORRT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),""),L_intRECCT,TBL_MODVL);	

						/*M_strSQLQRY = "SELECT PO_PORNO,PO_UOMCD,PO_PORRT,PO_UCNVL,PO_PERRT,ST_UOMCD,ST_MATDS,PO_CURCD,PO_EXGRT from MM_POMST,MM_STMST WHERE PO_STRTP = ST_STRTP AND PO_MATCD = ST_MATCD ";
			M_strSQLQRY += " AND PO_PORNO ='"+strPORNO +"' and PO_MATCD='"+L_strMATCD+"'";
			L_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(L_rstRSSET !=null)
			while(L_rstRSSET.next())
			{
				txtCURCD.setText(nvlSTRVL(L_rstRSSET.getString("PO_CURCD"),""));
				txtEXGRT.setText(nvlSTRVL(L_rstRSSET.getString("PO_EXGRT"),""));
				tblITDTL.setValueAt(nvlSTRVL(L_rstRSSET.getString("ST_UOMCD"),""),L_intRECCT,TBL_UOMCD);	
				tblITDTL.setValueAt(nvlSTRVL(L_rstRSSET.getString("ST_MATDS"),""),L_intRECCT,TBL_MATDS);	
				tblITDTL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PO_UOMCD"),""),L_intRECCT,TBL_POUOM);	
				tblITDTL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PO_UCNVL"),""),L_intRECCT,TBL_CNVFT);	
				tblITDTL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PO_PERRT"),""),L_intRECCT,TBL_PERRT);	
				tblITDTL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PO_PORRT"),""),L_intRECCT,TBL_PORRT);	
			}*/
						
						
						L_intRECCT++;

					}
				M_rstRSSET.close();

				if(L_intRECCT == 0)
				{
					setMSG("Data not found for Bill Passing..",'E');
					return false;
				}
				else
				{
					M_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXDOC where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD='MM' and TX_DOCTP='BLP' and "
							+"tx_DOCNO='"+txtSRLNO.getText().trim()+"' AND isnull(TX_STSFL,'') <>'X'");

					flgCOTAX = false;
					if(M_rstRSSET!=null)
					{
						intGTXRW =0;
						intCTXRW =0;
						while(M_rstRSSET.next())
						{
							strTEMP = M_rstRSSET.getString("TX_PRDCD");
							if(strTEMP.equalsIgnoreCase("XXXXXXXXXX"))
							{
								flgCOTAX = true;
								dspCOTAX(M_rstRSSET,"TX_DSBVL");
								dspCOTAX(M_rstRSSET,"TX_EXCVL");
								dspCOTAX(M_rstRSSET,"TX_PNFVL");
								dspCOTAX(M_rstRSSET,"TX_CSTVL");
								dspCOTAX(M_rstRSSET,"TX_STXVL");
								dspCOTAX(M_rstRSSET,"TX_OCTVL");
								dspCOTAX(M_rstRSSET,"TX_FRTVL");
								dspCOTAX(M_rstRSSET,"TX_INSVL");
								dspCOTAX(M_rstRSSET,"TX_CDSVL");
								dspCOTAX(M_rstRSSET,"TX_INCVL");
								dspCOTAX(M_rstRSSET,"TX_ENCVL");
								dspCOTAX(M_rstRSSET,"TX_FNIVL");
								dspCOTAX(M_rstRSSET,"TX_CDUVL");
								dspCOTAX(M_rstRSSET,"TX_CLRVL");
								dspCOTAX(M_rstRSSET,"TX_OTHVL");
								dspCOTAX(M_rstRSSET,"TX_RSTVL");
								dspCOTAX(M_rstRSSET,"TX_EDCVL");
								dspCOTAX(M_rstRSSET,"TX_SCHVL");
								dspCOTAX(M_rstRSSET,"TX_CVDVL");
								dspCOTAX(M_rstRSSET,"TX_WCTVL");
								dspCOTAX(M_rstRSSET,"TX_HICVL");
								dspCOTAX(M_rstRSSET,"TX_VATVL");
								dspCOTAX(M_rstRSSET,"TX_TOTVL");
							}
							else
							{
								dspGRTAX(M_rstRSSET,"TX_DSBVL");
								dspGRTAX(M_rstRSSET,"TX_EXCVL");
								dspGRTAX(M_rstRSSET,"TX_PNFVL");
								dspGRTAX(M_rstRSSET,"TX_CSTVL");
								dspGRTAX(M_rstRSSET,"TX_STXVL");
								dspGRTAX(M_rstRSSET,"TX_OCTVL");
								dspGRTAX(M_rstRSSET,"TX_FRTVL");
								dspGRTAX(M_rstRSSET,"TX_INSVL");
								dspGRTAX(M_rstRSSET,"TX_CDSVL");
								dspGRTAX(M_rstRSSET,"TX_INCVL");
								dspGRTAX(M_rstRSSET,"TX_ENCVL");
								dspGRTAX(M_rstRSSET,"TX_FNIVL");
								dspGRTAX(M_rstRSSET,"TX_CDUVL");
								dspGRTAX(M_rstRSSET,"TX_CLRVL");
								dspGRTAX(M_rstRSSET,"TX_OTHVL");
								dspGRTAX(M_rstRSSET,"TX_RSTVL");
								dspGRTAX(M_rstRSSET,"TX_EDCVL");
								dspGRTAX(M_rstRSSET,"TX_SCHVL");
								dspGRTAX(M_rstRSSET,"TX_CVDVL");
								dspGRTAX(M_rstRSSET,"TX_WCTVL");
								dspGRTAX(M_rstRSSET,"TX_HICVL");
								dspGRTAX(M_rstRSSET,"TX_VATVL");
								dspGRTAX(M_rstRSSET,"TX_TOTVL");

							}
						}
						M_rstRSSET.close();
					}
					M_strSQLQRY="Select * from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD='MM' "
					+"and TXT_DOCTP='POR' and TXT_SBSCD='"+M_strSBSCD+"' and "
					+"txT_DOCNO='"+strPORNO+"' and TXT_PRDCD='"+L_strMATCD+"' and isnull(TXT_STSFL,'') <>'X'"// and "
					//					+"TXT_PRTTP='C'"
					;

					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						int L_intROWCT=0;
						L_intROWCT= intCTXRW;
						while(M_rstRSSET.next())
						{
							if(M_rstRSSET.getString("TXT_PRDCD").equals("XXXXXXXXXX"))
							{
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODCD"),L_intROWCT,1);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODDS"),L_intROWCT,2);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODFL"),L_intROWCT,3);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODVL"),L_intROWCT++,4);
								//	tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_PRCSQ"),L_intROWCT++,5);
							}
							else
							{
								strTEMP = M_rstRSSET.getString("TXT_PRDCD");
								tblITTAX.setValueAt(strTEMP,intGTXRW,1);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODCD"),intGTXRW,2);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODDS"),intGTXRW,3);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODFL"),intGTXRW,4);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODVL"),intGTXRW,5);
								//tblITTAX.setValueAt(M_rstRSSET.getString("TXT_PRCSQ"),intGTXRW,6);
								intGTXRW++;

							}
						}
						M_rstRSSET.close();

					}
				}
				// To show values in enquiry, included on 21 sep 2007
				calBILVL(txtBILNO.getText().trim());
			}
			catch(Exception L_E)
			{
				
				setMSG(L_E,"getGRNDT");
				return false;
			}
			return true;
		}

		private boolean getGRNDT()
		{
			try
			{
				int L_intRECCT =0;
				setCursor(cl_dat.M_curDFSTS_pbst);


				// NEW QUERY changed on 07/03/2008 to take care of bill passing containing items which are accepted (GRIN) in the stock with Batch Nos and
				// involving POs preapred with different indents for the same items.

				M_strSQLQRY ="SELECT distinct GR_GRNNO,GR_MATCD,GR_ACPDT,GR_PORNO,GR_VENCD,GR_VENNM,PO_CURCD,PO_UCNVL,PO_EXGRT,PO_UOMCD,PO_PORRT,PO_PERRT,ST_MATDS,ST_UOMCD,GR_PORVL,SUM(GR_CHLQT) GR_CHLQT,SUM(isnull(GR_RECQT,0)) GR_RECQT,SUM(GR_ACPQT) GR_ACPQT,SUM(GR_BILQT) GR_BILQT,SUM(isnull(gr_acpqt,0)-isnull(gr_bilqt,0)) L_BLPQT,SUM(GR_VATVL) GR_VATVL,SUM(GR_MODVL) GR_MODVL from MM_GRMST,MM_POMST,MM_STMST ";
				M_strSQLQRY +=" WHERE GR_STRTP = PO_STRTP and GR_PORNO = PO_PORNO  AND GR_MATCD = PO_MATCD and GR_CMPCD=PO_CMPCD and GR_STRTP = ST_STRTP and GR_MATCD = ST_MATCD and GR_CMPCD = ST_CMPCD and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_ACPQT,0)-isnull(GR_BILQT,0) > 0 and GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY +=" AND GR_GRNNO ='"+txtGRNNO.getText().trim()+"'";
				if(txtVENCD.getText().length() >0)
					M_strSQLQRY +=" AND GR_VENCD ='"+txtVENCD.getText().trim()+"'";
				M_strSQLQRY+= " GROUP BY GR_GRNNO,GR_MATCD,GR_ACPDT,GR_PORNO,GR_VENCD,GR_VENNM,PO_CURCD,PO_UCNVL,PO_EXGRT,PO_UOMCD,PO_PORRT,PO_PERRT,ST_MATDS,ST_UOMCD,GR_PORVL,PO_INDNO";


				//NEW QUERY changed on 04/03/2008 to take care of bill passing containing items which are accepted (GRIN) in the stock with Batch Nos.
				//		M_strSQLQRY ="SELECT distinct GR_GRNNO,GR_MATCD,GR_ACPDT,GR_PORNO,GR_VENCD,GR_VENNM,PO_CURCD,PO_UCNVL,PO_EXGRT,PO_UOMCD,PO_PORRT,PO_PERRT,ST_MATDS,ST_UOMCD,GR_PORVL,SUM(GR_CHLQT) GR_CHLQT,SUM(GR_RECQT) GR_RECQT,SUM(GR_ACPQT) GR_ACPQT,SUM(GR_BILQT) GR_BILQT,SUM(isnull(gr_acpqt,0)-isnull(gr_bilqt,0)) L_BLPQT,PO_PORRT,SUM(GR_VATVL) GR_VATVL,SUM(GR_MODVL) GR_MODVL from MM_GRMST,MM_POMST,MM_STMST ";
				//		M_strSQLQRY +=" WHERE GR_STRTP = PO_STRTP and GR_PORNO = PO_PORNO  AND GR_MATCD = PO_MATCD and GR_STRTP = ST_STRTP and GR_MATCD = ST_MATCD and isnull(GR_ACPQT,0)-isnull(GR_BILQT,0) > 0 and GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				//		M_strSQLQRY +=" AND GR_GRNNO ='"+txtGRNNO.getText().trim()+"'";
				//if(txtVENCD.getText().length() >0)
				//    M_strSQLQRY +=" AND GR_VENCD ='"+txtVENCD.getText().trim()+"'";
				//M_strSQLQRY+= " GROUP BY GR_GRNNO,GR_MATCD,GR_ACPDT,GR_PORNO,GR_VENCD,GR_VENNM,PO_CURCD,PO_UCNVL,PO_EXGRT,PO_UOMCD,PO_PORRT,PO_PERRT,ST_MATDS,ST_UOMCD,GR_PORVL";

				//M_strSQLQRY ="SELECT distinct GR_GRNNO,GR_ACPDT,GR_MATCD,GR_CHLQT,GR_RECQT,GR_ACPQT,GR_PORNO,GR_VENCD,GR_VENNM,GR_BILQT,isnull(gr_acpqt,0)-isnull(gr_bilqt,0) L_BLPQT,PO_PORRT,PO_CURCD,PO_UCNVL,PO_EXGRT,PO_UOMCD,PO_PORRT,PO_PERRT,ST_MATDS,ST_UOMCD,GR_PORVL,GR_VATVL,GR_MODVL from MM_GRMST,MM_POMST,MM_STMST ";
				//M_strSQLQRY +=" WHERE GR_STRTP = PO_STRTP and GR_PORNO = PO_PORNO  AND GR_MATCD = PO_MATCD and GR_STRTP = ST_STRTP and GR_MATCD = ST_MATCD and isnull(GR_ACPQT,0)-isnull(GR_BILQT,0) > 0 and GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				//M_strSQLQRY +=" AND GR_GRNNO ='"+txtGRNNO.getText().trim()+"'";
				//if(txtVENCD.getText().length() >0)
				//    M_strSQLQRY +=" AND GR_VENCD ='"+txtVENCD.getText().trim()+"'";
				System.out.println("5 "+M_strSQLQRY);	
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
								
				txtCALAM.setText("0.00");
				
				txtOTPMT.setText("0.00");
				txtOTDED.setText("0.00");
				tblITDTL.clrTABLE();
				tblCOTAX.clrTABLE();
				tblITTAX.clrTABLE();
				if(M_rstRSSET !=null)
					while(M_rstRSSET.next())
					{
						if(L_intRECCT ==0)
						{
							datTEMP = M_rstRSSET.getDate("GR_ACPDT");
							if(datTEMP !=null)
							{
								txtACPDT.setText(M_fmtLCDAT.format(datTEMP));
							}
							else
							{
								setMSG("GRIN is not Accepted, Bill passing is  not allowed..",'E');
								M_rstRSSET.close();
								return false;
							}
							if(M_fmtLCDAT.parse(strYSTDT).compareTo(M_fmtLCDAT.parse(M_fmtLCDAT.format(datTEMP)))>0)
							{
								// Year start date is greater than GRIN acceptance date, prv year GRIN
								setMSG("GRIN is Accepted in previous year, Bill passing is  not allowed..",'E');
								M_rstRSSET.close();
								return false;
							}

							strPORNO = nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),"");
							txtPORNO.setText(strPORNO);	
							txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""));	
							txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""));	
							txtCURCD.setText(nvlSTRVL(M_rstRSSET.getString("PO_CURCD"),""));	
							txtEXGRT.setText(nvlSTRVL(M_rstRSSET.getString("PO_EXGRT"),""));	
							if(!nvlSTRVL(M_rstRSSET.getString("PO_CURCD"),"").equals("01"))
								txtEXGRT.setEnabled(true);
							else
								txtEXGRT.setEnabled(false);
						}
						tblITDTL.setValueAt(Boolean.TRUE,L_intRECCT,TBL_CHKFL);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),L_intRECCT,TBL_MATCD);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),L_intRECCT,TBL_UOMCD);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),L_intRECCT,TBL_MATDS);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_UOMCD"),""),L_intRECCT,TBL_POUOM);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_UCNVL"),""),L_intRECCT,TBL_CNVFT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0"),L_intRECCT,TBL_CHLQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0"),L_intRECCT,TBL_RECQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0"),L_intRECCT,TBL_ACPQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_BILQT"),"0"),L_intRECCT,TBL_BILQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_BLPQT"),"0"),L_intRECCT,TBL_BLPQT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),""),L_intRECCT,TBL_PORRT);	
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PERRT"),""),L_intRECCT,TBL_PERRT);
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_PORVL"),""),L_intRECCT,TBL_PORVL);
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),""),L_intRECCT,TBL_MODVL);

						// Added on 27/12/2005 for VAT consideration
						tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_VATVL"),"0"),L_intRECCT,TBL_VATVL);
						tblITDTL.setValueAt("0",L_intRECCT,TBL_VATBL);
						L_intRECCT++;
					}
				M_rstRSSET.close();
				if(L_intRECCT == 0)
				{
					setMSG("Data not found for Bill Passing..",'E');
					return false;
				}
				else
				{
					M_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXDOC where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD='MM' and TX_DOCTP='POR' and "
							+"tx_DOCNO='"+strPORNO+"' and "
							+"TX_SBSCD='"+M_strSBSCD+"' and isnull(TX_STSFL,'') <>'X'");
					flgCOTAX = false;
					if(M_rstRSSET!=null)
					{
						intGTXRW =0;
						intCTXRW =0;
						while(M_rstRSSET.next())
						{
							strTEMP = M_rstRSSET.getString("TX_PRDCD");
							if(strTEMP.equalsIgnoreCase("XXXXXXXXXX"))
							{
								flgCOTAX = true;
								dspCOTAX(M_rstRSSET,"TX_DSBVL");
								dspCOTAX(M_rstRSSET,"TX_EXCVL");
								dspCOTAX(M_rstRSSET,"TX_PNFVL");
								dspCOTAX(M_rstRSSET,"TX_CSTVL");
								dspCOTAX(M_rstRSSET,"TX_STXVL");
								dspCOTAX(M_rstRSSET,"TX_OCTVL");
								dspCOTAX(M_rstRSSET,"TX_FRTVL");
								dspCOTAX(M_rstRSSET,"TX_INSVL");
								dspCOTAX(M_rstRSSET,"TX_CDSVL");
								dspCOTAX(M_rstRSSET,"TX_INCVL");
								dspCOTAX(M_rstRSSET,"TX_ENCVL");
								dspCOTAX(M_rstRSSET,"TX_FNIVL");
								dspCOTAX(M_rstRSSET,"TX_CDUVL");
								dspCOTAX(M_rstRSSET,"TX_CLRVL");
								dspCOTAX(M_rstRSSET,"TX_OTHVL");
								dspCOTAX(M_rstRSSET,"TX_RSTVL");
								dspCOTAX(M_rstRSSET,"TX_SCHVL");
								dspCOTAX(M_rstRSSET,"TX_CVDVL");
								dspCOTAX(M_rstRSSET,"TX_WCTVL");
								dspCOTAX(M_rstRSSET,"TX_VATVL");
								dspCOTAX(M_rstRSSET,"TX_EDCVL");
								dspCOTAX(M_rstRSSET,"TX_HICVL");
								dspCOTAX(M_rstRSSET,"TX_TOTVL");
							}
							else
							{
								dspGRTAX(M_rstRSSET,"TX_DSBVL");
								dspGRTAX(M_rstRSSET,"TX_EXCVL");
								dspGRTAX(M_rstRSSET,"TX_PNFVL");
								dspGRTAX(M_rstRSSET,"TX_CSTVL");
								dspGRTAX(M_rstRSSET,"TX_STXVL");
								dspGRTAX(M_rstRSSET,"TX_OCTVL");
								dspGRTAX(M_rstRSSET,"TX_FRTVL");
								dspGRTAX(M_rstRSSET,"TX_INSVL");
								dspGRTAX(M_rstRSSET,"TX_CDSVL");
								dspGRTAX(M_rstRSSET,"TX_INCVL");
								dspGRTAX(M_rstRSSET,"TX_ENCVL");
								dspGRTAX(M_rstRSSET,"TX_FNIVL");
								dspGRTAX(M_rstRSSET,"TX_CDUVL");
								dspGRTAX(M_rstRSSET,"TX_CLRVL");
								dspGRTAX(M_rstRSSET,"TX_OTHVL");
								dspGRTAX(M_rstRSSET,"TX_RSTVL");
								dspGRTAX(M_rstRSSET,"TX_SCHVL");
								dspGRTAX(M_rstRSSET,"TX_CVDVL");
								dspGRTAX(M_rstRSSET,"TX_WCTVL");
								dspGRTAX(M_rstRSSET,"TX_VATVL");
								dspGRTAX(M_rstRSSET,"TX_EDCVL");
								dspGRTAX(M_rstRSSET,"TX_HICVL");
								dspGRTAX(M_rstRSSET,"TX_TOTVL");
							}
						}
						M_rstRSSET.close();
					}
					M_strSQLQRY="Select * from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD='MM' "
					+"and TXT_DOCTP='POR' and TXT_SBSCD='"+M_strSBSCD+"' and "
					+"txT_DOCNO='"+strPORNO+"' and isnull(TXT_STSFL,'') <>'X'"// and "
					//					+"TXT_PRTTP='C'"
					;

					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						int L_intROWCT=0;
						L_intROWCT= intCTXRW;
						while(M_rstRSSET.next())
						{
							if(M_rstRSSET.getString("TXT_PRDCD").equals("XXXXXXXXXX"))
							{
								tblCOTAX.setValueAt(Boolean.TRUE,L_intROWCT,0);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODCD"),L_intROWCT,1);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODDS"),L_intROWCT,2);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODFL"),L_intROWCT,3);
								tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_CODVL"),L_intROWCT,4);
								//	tblCOTAX.setValueAt(M_rstRSSET.getString("TXT_PRCSQ"),L_intROWCT++,5);
							}
							else
							{
								strTEMP = M_rstRSSET.getString("TXT_PRDCD");
								tblITTAX.setValueAt(strTEMP,intGTXRW,1);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODCD"),intGTXRW,2);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODDS"),intGTXRW,3);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODFL"),intGTXRW,4);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_CODVL"),intGTXRW,5);
								tblITTAX.setValueAt(M_rstRSSET.getString("TXT_PRCSQ"),intGTXRW,6);
								intGTXRW++;

							}
						}
						M_rstRSSET.close();

					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"getGRNDT");
				setMSG(L_E,"getGRNDT");
				return false;
			}
			tblITDTL.editCellAt(0,TBL_BLPQT);
			return true;
		}
		private void dspCOTAX(ResultSet P_rstRSSET,String P_strCOLNM) throws Exception
		{
			String L_strTEMP1;

			if(P_rstRSSET.getString(P_strCOLNM)!=null)
			{
				int i=0;
				for(i=0;i<tblCOTAX.getRowCount();i++)
					if(tblCOTAX.getValueAt(i,1).toString().length()==0)
						break;
				strTEMP = nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),"");
				if((strTEMP.length() >0) && (!strTEMP.equals("I")))
				{
					tblCOTAX.setValueAt(Boolean.TRUE,i,TBL_CHKFL);	
					tblCOTAX.setValueAt(P_strCOLNM.substring(3,P_strCOLNM.length()-2),i,1);
					tblCOTAX.setValueAt(P_rstRSSET.getString(P_strCOLNM),i,4);
					tblCOTAX.setValueAt(strTEMP,i,3);
					strTEMP = nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),"");
					if((P_strCOLNM.equals("TX_STXVL"))||(P_strCOLNM.equals("TX_OTHVL")))
					{
						tblCOTAX.setValueAt(nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"DS"),""),i,2);
					}
					else
						tblCOTAX.setValueAt(cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXTAX",tblCOTAX.getValueAt(i,1).toString()),i,2);
					intCTXRW++;
				}

			}
		}
		private void dspGRTAX(ResultSet P_rstRSSET,String P_strCOLNM)throws Exception
		{
			if(P_rstRSSET.getString(P_strCOLNM)!=null)
			{
				int i=0;
				for(i=0;i<tblITTAX.getRowCount();i++)
					if(tblITTAX.getValueAt(i,1).toString().length()==0)
						break;
				strTEMP = nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),"");
				if(strTEMP.length() >0)
				{
					tblITTAX.setValueAt(Boolean.TRUE,intGTXRW,TBL_CHKFL);	
					tblITTAX.setValueAt(nvlSTRVL(P_rstRSSET.getString("TX_PRDCD"),""),intGTXRW,1);
					tblITTAX.setValueAt(P_strCOLNM.substring(3,6),intGTXRW,2);
					tblITTAX.setValueAt(P_rstRSSET.getString(P_strCOLNM),intGTXRW,5);
					if((P_strCOLNM.equals("TX_STXVL"))||(P_strCOLNM.equals("TX_OTHVL")))
					{
						tblITTAX.setValueAt(nvlSTRVL(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"DS"),""),intGTXRW,3);
					}
					else
						tblITTAX.setValueAt(cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXTAX",tblITTAX.getValueAt(intGTXRW,2).toString()),intGTXRW,3);
					tblITTAX.setValueAt(P_rstRSSET.getString(P_strCOLNM.substring(0,6)+"FL"),intGTXRW,4);
					intGTXRW++;
				}
			}

		}
		private class mm_teblpTBLINVFR extends TableInputVerifier
		{

			public boolean verify(int P_intROWID,int P_intCOLID)
			{
				try
				{
					if(getSource() == tblITDTL)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							if(P_intCOLID== TBL_BLPQT)  
							{
								// If qty. to bill is changed then PORVL as per the billed qty.
								tblITDTL.setValueAt(setNumberFormat((Double.parseDouble(tblITDTL.getValueAt(P_intROWID,TBL_PORVL).toString())/Double.parseDouble(tblITDTL.getValueAt(P_intROWID,TBL_ACPQT).toString()))*Double.parseDouble(tblITDTL.getValueAt(P_intROWID,TBL_BLPQT).toString()),2),P_intROWID,TBL_PORVL);
							}

					}
					else if(getSource() == tblCOTAX)
					{
						if(P_intCOLID== 3)  
						{
							strTEMP = tblCOTAX.getValueAt(P_intROWID,3).toString().toUpperCase();
							tblCOTAX.setValueAt(strTEMP,P_intROWID,3);
							if(!strTEMP.equals("P"))
								if(!strTEMP.equals("A"))
								{
									setMSG("Tax Flags can be A or P only..",'E');
									return false;
								}
						}
						if(P_intCOLID== 4)  // value
						{
							strTEMP = tblCOTAX.getValueAt(P_intROWID,4).toString();
							if(Double.parseDouble(strTEMP) <= 0)
							{
								setMSG("Tax Value can not be 0,",'E');
								return false;
							}

						}

					}
					else if(getSource() == tblITTAX)
					{
						if(P_intCOLID== 4)  
						{
							strTEMP = tblITTAX.getValueAt(P_intROWID,4).toString().toUpperCase();
							tblITTAX.setValueAt(strTEMP,P_intROWID,4);
							if(!strTEMP.equals("P"))
								if(!strTEMP.equals("A"))
								{
									setMSG("Tax Flags can be A or P only..",'E');
									return false;
								}
						}
						if(P_intCOLID== 5)  // value
						{
							strTEMP = tblITTAX.getValueAt(P_intROWID,5).toString();
							if(Double.parseDouble(strTEMP) <= 0)
							{
								setMSG("Tax value can not be zero..",'E');
								return false;
							}

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
		class inpVRFY extends InputVerifier 
		{
			public boolean verify(JComponent input) 
			{
				if(input == txtVENCD)
				{
					if(txtVENCD.getText().trim().length() == 0)
					{
						setCursor(cl_dat.M_curDFSTS_pbst);
						return true;
					}
					if(!vldVENCD(txtVENCD.getText().trim()))
					{
						setMSG("Invalid Supplier code..",'E');
						return false;
					}

				}
				return true;	
			}
		}
		@SuppressWarnings("unchecked") private boolean insTXDTL(String P_strPORNO) // Insert Tax Details
		{
			try
			{
				M_strSQLQRY ="";
				Hashtable L_hstTXCT1=new Hashtable(10,0.2f);
				Vector<String> L_vtrTXCT2=new Vector<String>(10,2);
				for(int i=0;i<tblCOTAX.getRowCount();i++)
				{
					String L_strTEMP=nvlSTRVL(tblCOTAX.getValueAt(i,1).toString(),"");
					if(L_strTEMP.length()>0)
					{
						if(hstTXCAT.get(L_strTEMP).toString().equals("01"))//FOR CO_TXDOC
						{
							//if(Float.parseFloat(tblCOTAX.getValueAt(i,4).toString())>0.0f)
							if(tblCOTAX.getValueAt(i,1).toString().length() >0)
								L_hstTXCT1.put(L_strTEMP,tblCOTAX.getValueAt(i,4).toString()+",'"+tblCOTAX.getValueAt(i,3).toString().toUpperCase()+"',");
						}
						else//FOR CO_TXSPC
						{
							//if(Float.parseFloat(tblCOTAX.getValueAt(i,4).toString())>0.0f)
							if(tblCOTAX.getValueAt(i,1).toString().length() >0)
								L_vtrTXCT2.addElement("'"+L_strTEMP+"','"+tblCOTAX.getValueAt(i,2).toString()+"','XXXXXXXXXX',"+tblCOTAX.getValueAt(i,4).toString()+(tblCOTAX.getValueAt(i,3).toString().equals("P") ? ",'P','" : ",'A',' ',"));
							// changed on 20/12/2004 as Specific tax saving was giving problem, in processing seq column we are displaying value.
							//L_vtrTXCT2.addElement("'"+L_strTEMP+"','"+tblCOTAX.getValueAt(i,2).toString()+"','XXXXXXXXXX',"+tblCOTAX.getValueAt(i,4).toString()+(tblCOTAX.getValueAt(i,3).toString().equals("P") ? ",'P','" : ",'A','")+tblCOTAX.getValueAt(i,5).toString()+"',");
						}
					}
				}
				if(L_hstTXCT1.size()>0)
				{
					Enumeration e=L_hstTXCT1.elements();

					M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_SBSCD,TX_TRNTP,TX_DSBVL,TX_DSBFL,TX_EXCVL,TX_EXCFL,TX_PNFVL,TX_PNFFL,TX_CSTVL,TX_CSTFL,TX_STXVL,TX_STXFL,TX_STXDS,TX_OCTVL,TX_OCTFL,TX_FRTVL,TX_FRTFL,TX_INSVL,TX_INSFL,TX_CDSVL,TX_CDSFL,TX_INCVL,TX_INCFL,TX_ENCVL,TX_ENCFL,TX_FNIVL,TX_FNIFL,TX_CDUVL,TX_CDUFL,TX_CLRVL,TX_CLRFL,TX_OTHVL,TX_OTHFL,TX_OTHDS,TX_RSTVL,TX_RSTFL,"+
					"TX_EDCVL,TX_EDCFL,TX_SCHVL,TX_SCHFL,TX_CVDVL,TX_CVDFL,TX_WCTVL,TX_WCTFL,TX_VATVL,TX_VATFL,"+	
					"TX_TRNFL,TX_STSFL,TX_LUSBY,TX_LUPDT) values("
					+"'"+cl_dat.M_strCMPCD_pbst +"',"//TX_SBSTP
					+"'MM',"//TX_SYSCD
					+"'"+M_strSBSCD.substring(2,4) +"',"//TX_SBSTP
					+"'BLP',"//TX_DOCTP
					+"'"+txtSRLNO.getText().trim()+"',"//TX_DOCNO
					+"'XXXXXXXXXX',"//TX_PRDCD
					+"'"+M_strSBSCD+"',"//TX_SBSCD
					+"'M',"//TX_TRNTP
					+(L_hstTXCT1.containsKey("DSB")==true ? L_hstTXCT1.get("DSB").toString() : "0,'',")//TX_DSBVL//TX_DSBFL
					+(L_hstTXCT1.containsKey("EXC")==true ? L_hstTXCT1.get("EXC").toString() : "0,'',")//TX_EXCVL//TX_EXCFL
					+(L_hstTXCT1.containsKey("PNF")==true ? L_hstTXCT1.get("PNF").toString() : "0,'',")//TX_PNFVL//TX_PNFFL
					+(L_hstTXCT1.containsKey("CST")==true ? L_hstTXCT1.get("CST").toString() : "0,'',")//TX_CSTVL//TX_CSTFL
					+(L_hstTXCT1.containsKey("STX")==true ? L_hstTXCT1.get("STX").toString() : "0,'',")//TX_STXVL//TX_STXFL;
					+"'"+strSTXDS+"',"
					+(L_hstTXCT1.containsKey("OCT")==true ? L_hstTXCT1.get("OCT").toString() : "0,'',")//TX_OCTVL//TX_OCTFL
					+(L_hstTXCT1.containsKey("FRT")==true ? L_hstTXCT1.get("FRT").toString() : "0,'',")//TX_FRTVL//TX_FRTFL
					+(L_hstTXCT1.containsKey("INS")==true ? L_hstTXCT1.get("INS").toString() : "0,'',")//TX_INSVL//TX_INSFL
					+(L_hstTXCT1.containsKey("CDS")==true ? L_hstTXCT1.get("CDS").toString() : "0,'',")//TX_CDSVL//TX_CDSFL
					+(L_hstTXCT1.containsKey("INC")==true ? L_hstTXCT1.get("INC").toString() : "0,'',")//TX_INCVL//TX_INCFL
					+(L_hstTXCT1.containsKey("ENC")==true ? L_hstTXCT1.get("ENC").toString() : "0,'',")//TX_ENCVL//TX_ENCFL
					+(L_hstTXCT1.containsKey("FNI")==true ? L_hstTXCT1.get("FNI").toString() : "0,'',")//TX_FNIVL//TX_FNIFL
					+(L_hstTXCT1.containsKey("CDU")==true ? L_hstTXCT1.get("CDU").toString() : "0,'',")//TX_CDUVL//TX_CDUFL
					+(L_hstTXCT1.containsKey("CLR")==true ? L_hstTXCT1.get("CLR").toString() : "0,'',")//TX_CLRVL//TX_CLRFL
					+(L_hstTXCT1.containsKey("OTH")==true ? L_hstTXCT1.get("OTH").toString() : "0,'',")//TX_OTHVL//TX_OTHFL;
					+"'"+strOTHDS+"',"
					+(L_hstTXCT1.containsKey("RST")==true ? L_hstTXCT1.get("RST").toString() : "0,'',")//TX_rstVL//TX_RSTFL
					+(L_hstTXCT1.containsKey("EDC")==true ? L_hstTXCT1.get("EDC").toString() : "0,'',")//TX_EDCVL//TX_EDCFL
					+(L_hstTXCT1.containsKey("SCH")==true ? L_hstTXCT1.get("SCH").toString() : "0,'',")//TX_SCHVL//TX_SCHFL
					+(L_hstTXCT1.containsKey("CVD")==true ? L_hstTXCT1.get("CVD").toString() : "0,'',")//TX_CVDVL//TX_CVDFL
					+(L_hstTXCT1.containsKey("WCT")==true ? L_hstTXCT1.get("WCT").toString() : "0,'',")//TX_WCTVL//TX_WCTFL
					+(L_hstTXCT1.containsKey("VAT")==true ? L_hstTXCT1.get("VAT").toString() : "0,'',")//TX_VATVL//TX_VATFL
					+getUSGDTL("TX",'I',"")+")";
					if(M_strSQLQRY.trim().length() >0)
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				// creating grdaewise taxes
				M_strSQLQRY ="";
				if(L_hstTXCT1 !=null)
					L_hstTXCT1.clear();
				L_hstTXCT1=new Hashtable<String,String>(10,0.2f);
				for(int i=0;i<tblITTAX.getRowCount();i++)
				{//GETHERING INFORMATIOJN ABT GRADE WISE MASTER TAXES
					String L_strTEMP=nvlSTRVL(tblITTAX.getValueAt(i,2).toString(),"");
					if(L_strTEMP.length()>0)
					{
						String L_strPRDCD=tblITTAX.getValueAt(i,1).toString();
						for(int j=0;j<tblITDTL.getRowCount();j++)
						{
							if(nvlSTRVL(tblITDTL.getValueAt(j,TB1_MATCD).toString(),"").length()>0)
								if(tblITDTL.getValueAt(j,TB1_MATCD).toString().equals(L_strPRDCD))
								{
									L_strPRDCD=tblITDTL.getValueAt(j,TB1_MATCD).toString();
									break;
								}
						}
						if(hstTXCAT.get(L_strTEMP).toString().equals("01"))
						{
							if(tblITTAX.getValueAt(i,2).toString().trim().equals("STX"))
								strGTXDS = tblITTAX.getValueAt(i,3).toString();
							else
								strGTXDS ="";
							if(tblITTAX.getValueAt(i,2).toString().trim().equals("OTH"))
								strGOTDS = tblITTAX.getValueAt(i,3).toString();
							else
								strGOTDS ="";
							if(!L_hstTXCT1.containsKey(L_strPRDCD))
							{
								Hashtable hst=new Hashtable(5,0.2f);
								//	if(L_strTEMP.equals("STX"))
								if(tblITTAX.getValueAt(i,2).toString().trim().equals("STX"))
								{
									//hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','"+strGTXDS+"',": ",'A','"+strGTXDS+"',"));
									hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+",'"+(tblITTAX.getValueAt(i,4).toString().toUpperCase()+"','"+strGTXDS+"',"));
								}
								else if(tblITTAX.getValueAt(i,2).toString().trim().equals("OTH"))
								{
									//	hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','"+strGOTDS+"',": ",'A','"+strGOTDS+"',"));
									hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+",'"+(tblITTAX.getValueAt(i,4).toString().toUpperCase()+"','"+strGOTDS+"',"));
								}
								else
									//hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P'," : ",'A',"));
									hst.put(L_strTEMP,nvlSTRVL(tblITTAX.getValueAt(i,5).toString(),"")+",'"+(tblITTAX.getValueAt(i,4).toString().toUpperCase()+"',"));
								L_hstTXCT1.put(L_strPRDCD,hst);
							}
							else
							{
								if(tblITTAX.getValueAt(i,2).toString().trim().equals("STX"))
								{
									//	((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','" : ",'A','"+strGTXDS+"',"));
									((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','"+strGTXDS+"'," : ",'A','"+strGTXDS+"',"));
								}
								else if(tblITTAX.getValueAt(i,2).toString().trim().equals("OTH"))
								{
									//	((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','" : ",'A','"+strGTXDS+"',"));
									((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','"+strGOTDS+"'," : ",'A','"+strGOTDS+"',"));
								}

								else
									((Hashtable)L_hstTXCT1.get(L_strPRDCD)).put(L_strTEMP,tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P'," : ",'A',"));
							}
						}
						else
						{
							// test afterwards 
							//	if(Float.parseFloat(tblITTAX.getValueAt(i,3).toString())>0.0f)
							if(tblITTAX.getValueAt(i,2).toString().length() >0)
								L_vtrTXCT2.addElement("'"+L_strTEMP+"','"+tblITTAX.getValueAt(i,3).toString()+"','"+L_strPRDCD+"',"+tblITTAX.getValueAt(i,5).toString()+(tblITTAX.getValueAt(i,4).toString().equals("P") ? ",'P','" : ",'A','")+tblITTAX.getValueAt(i,6).toString()+"',");
						}
					}
				}
				if(L_hstTXCT1.size()>0)
				{
					int cnt =1;
					Enumeration L_enmTXKEYS=L_hstTXCT1.keys();
					while(L_enmTXKEYS.hasMoreElements())
					{
						M_strSQLQRY="";
						String L_strGRDCD=(String)L_enmTXKEYS.nextElement();
						Hashtable L_hstGRDTX=(Hashtable)L_hstTXCT1.get(L_strGRDCD);
						M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_SBSCD,TX_TRNTP,"+
						"TX_DSBVL,TX_DSBFL,TX_EXCVL,TX_EXCFL,TX_PNFVL,TX_PNFFL,TX_CSTVL,TX_CSTFL,TX_STXVL,TX_STXFL,TX_STXDS,TX_OCTVL,TX_OCTFL,TX_FRTVL,TX_FRTFL,TX_INSVL,TX_INSFL,TX_CDSVL,TX_CDSFL,TX_INCVL,TX_INCFL,TX_ENCVL,TX_ENCFL,TX_FNIVL,TX_FNIFL,TX_CDUVL,TX_CDUFL,TX_CLRVL,TX_CLRFL,TX_OTHVL,TX_OTHFL,TX_OTHDS,TX_RSTVL,TX_RSTFL,"+
						"TX_EDCVL,TX_EDCFL,TX_SCHVL,TX_SCHFL,TX_CVDVL,TX_CVDFL,TX_WCTVL,TX_WCTFL,TX_VATVL,TX_VATFL,"+	
						"TX_TRNFL,TX_STSFL,TX_LUSBY,TX_LUPDT) values("
						+"'"+cl_dat.M_strCMPCD_pbst +"',"//TX_SBSTP
						+"'MM',"//TX_SYSCD
						+"'"+M_strSBSCD.substring(2,4) +"',"//TX_SBSTP
						+"'BLP',"//TX_DOCTP
						+"'"+txtSRLNO.getText().trim()+"',"//TX_DOCNO
						+"'"+L_strGRDCD+"',"//TX_PRDCD
						+"'"+M_strSBSCD+"',"//TX_SBSCD
						+"'M',"//TX_TRNTP
						+(L_hstGRDTX.containsKey("DSB")==true ? L_hstGRDTX.get("DSB").toString() : "0,'',")//TX_DSBVL//TX_DSBFL
						+(L_hstGRDTX.containsKey("EXC")==true ? L_hstGRDTX.get("EXC").toString() : "0,'',")//TX_EXCVL//TX_EXCFL
						+(L_hstGRDTX.containsKey("PNF")==true ? L_hstGRDTX.get("PNF").toString() : "0,'',")//TX_PNFVL//TX_PNFFL
						+(L_hstGRDTX.containsKey("CST")==true ? L_hstGRDTX.get("CST").toString() : "0,'',")//TX_CSTVL//TX_CSTFL
						+(L_hstGRDTX.containsKey("STX")==true ? L_hstGRDTX.get("STX").toString() : "0,'','',")//TX_STXVL//TX_STXFL//TX_STXDS
						+(L_hstGRDTX.containsKey("OCT")==true ? L_hstGRDTX.get("OCT").toString() : "0,'',")//TX_OCTVL//TX_OCTFL
						+(L_hstGRDTX.containsKey("FRT")==true ? L_hstGRDTX.get("FRT").toString() : "0,'',")//TX_FRTVL//TX_FRTFL
						+(L_hstGRDTX.containsKey("INS")==true ? L_hstGRDTX.get("INS").toString() : "0,'',")//TX_INSVL//TX_INSFL
						+(L_hstGRDTX.containsKey("CDS")==true ? L_hstGRDTX.get("CDS").toString() : "0,'',")//TX_CDSVL//TX_CDSFL
						+(L_hstGRDTX.containsKey("INC")==true ? L_hstGRDTX.get("INC").toString() : "0,'',")//TX_INCVL//TX_INCFL
						+(L_hstGRDTX.containsKey("ENC")==true ? L_hstGRDTX.get("ENC").toString() : "0,'',")//TX_ENCVL//TX_ENCFL
						+(L_hstGRDTX.containsKey("FNI")==true ? L_hstGRDTX.get("FNI").toString() : "0,'',")//TX_FNIVL//TX_FNIFL
						+(L_hstGRDTX.containsKey("CDU")==true ? L_hstGRDTX.get("CDU").toString() : "0,'',")//TX_CDUVL//TX_CDUFL
						+(L_hstGRDTX.containsKey("CLR")==true ? L_hstGRDTX.get("CLR").toString() : "0,'',")//TX_CLRVL//TX_CLRFL
						+(L_hstGRDTX.containsKey("OTH")==true ? L_hstGRDTX.get("OTH").toString() : "0,'','',")//TX_OTHVL//TX_OTHFL//TX_OTHDS		
						+(L_hstGRDTX.containsKey("RST")==true ? L_hstGRDTX.get("RST").toString() : "0,'',")//TX_RSTVL//TX_RSTFL
						+(L_hstGRDTX.containsKey("EDC")==true ? L_hstGRDTX.get("EDC").toString() : "0,'',")//TX_EDCVL//TX_EDCFL
						+(L_hstGRDTX.containsKey("SCH")==true ? L_hstGRDTX.get("SCH").toString() : "0,'',")//TX_SCHVL//TX_SCHFL
						+(L_hstGRDTX.containsKey("CVD")==true ? L_hstGRDTX.get("CVD").toString() : "0,'',")//TX_CVDVL//TX_CVDFL
						+(L_hstGRDTX.containsKey("WCT")==true ? L_hstGRDTX.get("WCT").toString() : "0,'',")//TX_WCTVL//TX_WCTFL
						+(L_hstGRDTX.containsKey("VAT")==true ? L_hstGRDTX.get("VAT").toString() : "0,'',")//TX_VATVL//TX_VATFL
						+getUSGDTL("TX",'I',"")+")";
						cnt++;							
						if(M_strSQLQRY.trim().length() >0)
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}

				}
				for(int i=0;i<L_vtrTXCT2.size();i++)
				{
					M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_CODCD,"+
					"TXT_CODDS,TXT_PRDCD,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,"+
					"TXT_TRNFL,TXT_STSFL,TXT_LUSBY,TXT_LUPDT) values ("
					+"'"+cl_dat.M_strCMPCD_pbst+"','MM',"//TXT_SYSCD,
					+"'"+M_strSBSCD.substring(2,4) +"',"//TX_SBSTP
					+"'BLP',"//TXT_DOCTP,
					+"'"+P_strPORNO+"',"//TXT_DOCNO,
					+L_vtrTXCT2.elementAt(i).toString()//TXT_CODCD,
					//TXT_CODDS,//TXT_PRDCD,//TXT_CODVL,//TXT_CODFL,//TXT_PRCSQ,
					+"'"+M_strSBSCD+"',"//TXT_SBSCD,
					+getUSGDTL("TXT",'I',"")+")"; //TXT_LUPDT	
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			catch(Exception L_E)
			{

				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG("Error in Saving Tax Details..",'E');
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return false;
			}
			return true;
		}
		private void  crtHSTXCD()
		{
			try
			{
				String L_strHSVAL ="",
				//hstTXCAT=new Hashtable(10,0.2f);
				M_strSQLQRY = " SELECT * FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXTAX' order by CMT_CCSVL,CMT_NMP01";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
					while(M_rstRSSET.next())
					{
						L_strHSVAL = nvlSTRVL(M_rstRSSET.getString("CMT_NMP01"),"") ;// Multiplier
						L_strHSVAL += "|"+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"") ;// Multiplier
						// added
						L_strHSVAL += "|"+nvlSTRVL(M_rstRSSET.getString("CMT_MODLS")," "+"|") ;// Multiplier
						hstTXCAT.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CHP01"));
						hstTAXCD.put(M_rstRSSET.getString("CMT_CODCD"),L_strHSVAL);
						vtrTAXCD.addElement(M_rstRSSET.getString("CMT_CODCD"));
						vtrTAXTP.addElement(M_rstRSSET.getString("CMT_CHP01"));
					}
				M_rstRSSET.close();
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"crtHSTCD");
			}

		}
		/**
    Jtable for Common Tax and Item Tax is navigated and Hashtables are maintained for
    Common Tax codes as (Code,P/A + TAXVL)
    and Itemwise Tax code (Item Code + TAX Code,P/A + TAXVL)
		 */
		private String calBILVL(String P_strBILNO)
		{
			double L_dblITVAL=0.00;
			double L_dblTOTAL=0.00;
			double L_dblFRACT=0.00;
			boolean flgOTTAX = false;
			String L_strTXOCD ="";
			try
			{
				ResultSet L_rstRSSET;
				Hashtable<String,String> hstITTAX = new Hashtable<String,String>();
				Hashtable<String,String> hstCOTAX = new Hashtable<String,String>();
				Hashtable<String,String> hstITCOD = new Hashtable<String,String>();
				Hashtable<String,Double> hstTXVAL = new Hashtable<String,Double>();
				String L_strTEMP ="";
				String L_strMULT ="";
				String L_strBILNO="",L_strMATCD;
				String L_strTAXVL,L_strITMCD="",L_strTAXCD="",L_strTAXFL="";
				dblTXVAL = 0.0;
				for(int j=0;j<tblCOTAX.getRowCount();j++)
				{
					if(tblCOTAX.getValueAt(j,0).toString().equals("true"))
					{
						L_strTAXCD = tblCOTAX.getValueAt(j,1).toString();
						L_strTAXFL = tblCOTAX.getValueAt(j,3).toString();
						L_strTAXVL = tblCOTAX.getValueAt(j,4).toString();
						if(L_strTAXFL.equals("P"))
							L_strTAXVL = String.valueOf(Double.parseDouble(L_strTAXVL)/100);
						// put in a hashtable 
						if(L_strTAXFL.equals("P"))
							hstCOTAX.put(L_strTAXCD,"P"+L_strTAXVL); 
						else
							hstCOTAX.put(L_strTAXCD,"A"+L_strTAXVL); 
					}
				}
				for(int j=0;j<tblITTAX.getRowCount();j++)
				{
					if(tblITTAX.getValueAt(j,0).toString().equals("true"))
					{
						L_strITMCD = tblITTAX.getValueAt(j,1).toString();
						L_strTAXCD = tblITTAX.getValueAt(j,2).toString();
						L_strTAXFL = tblITTAX.getValueAt(j,4).toString();
						L_strTAXVL = tblITTAX.getValueAt(j,5).toString();
						if(L_strTAXFL.equals("P"))
							L_strTAXVL = String.valueOf(Double.parseDouble(L_strTAXVL)/100);
						// put in a hashtable 
						if(L_strTAXFL.equals("P"))
							hstITTAX.put(L_strITMCD+L_strTAXCD,"P"+L_strTAXVL); 
						else
							hstITTAX.put(L_strITMCD+L_strTAXCD,"A"+L_strTAXVL); 
					}
				}
				L_strBILNO = txtSRLNO.getText().trim();
				dblBILVL =0;
				for(int j=0;j<tblITDTL.getRowCount();j++)
				{
					if(tblITDTL.getValueAt(j,0).toString().equals("true"))
					{
						L_dblITVAL = (Double.parseDouble(tblITDTL.getValueAt(j,TBL_BLPQT).toString()))/(Double.parseDouble(tblITDTL.getValueAt(j,TBL_CNVFT).toString()))
						* (Double.parseDouble(tblITDTL.getValueAt(j,TBL_PORRT).toString()))/(Double.parseDouble(tblITDTL.getValueAt(j,TBL_PERRT).toString()))*Double.parseDouble(txtEXGRT.getText())	;
						L_dblTOTAL +=L_dblITVAL;
					}
				}
				for(int j=0;j<tblITDTL.getRowCount();j++)
				{
					if(tblITDTL.getValueAt(j,0).toString().equals("true"))
					{
						L_strMATCD =	tblITDTL.getValueAt(j,1).toString();
						L_dblITVAL = (Double.parseDouble(tblITDTL.getValueAt(j,TBL_BLPQT).toString()))/(Double.parseDouble(tblITDTL.getValueAt(j,TBL_CNVFT).toString()))
						* (Double.parseDouble(tblITDTL.getValueAt(j,TBL_PORRT).toString()))/(Double.parseDouble(tblITDTL.getValueAt(j,TBL_PERRT).toString()))*Double.parseDouble(txtEXGRT.getText())	;
						L_dblFRACT = L_dblITVAL /L_dblTOTAL;
						dblTXVAL =0;
						for(int i=0;i<vtrTAXCD.size();i++)
						{
							L_strTAXCD = vtrTAXCD.elementAt(i).toString();
							//	dblTXVAL =0;
							// Item wise tax
							if(hstITTAX.get(L_strMATCD+L_strTAXCD)==null)
							{
								// Item wise tax is not present
							}
							else
								//if(hstITTAX.contains(L_strMATCD+L_strTAXCD))
							{
								// Item wise tax is present
								L_strTEMP = hstITTAX.get(L_strMATCD+L_strTAXCD).toString();
								L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
								L_strMULT = L_STRTKN.nextToken();
								L_STRTKN.nextToken();
								L_strTXOCD = L_STRTKN.nextToken(); 
								if(L_strTEMP.substring(0,1).equals("P"))
								{
									//if(L_strTAXCD.equals("EXC"))

									if(L_strTXOCD.trim().length() >0)
										hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))));
									else
										hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))));
									dblTXVAL = L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);
									if(L_strTXOCD.trim().length() >0)
										L_dblITVAL = L_dblITVAL + Double.parseDouble(hstTXVAL.get(L_strMATCD+L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
									else
										L_dblITVAL += L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
								}
								else
								{									
									//	if(L_strTAXCD.equals("EXC"))
									hstTXVAL.put(L_strMATCD+L_strTAXCD,new Double(Double.parseDouble(L_strTEMP.substring(1))));
									dblTXVAL = (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor
									//	if(L_strTXOCD.trim().length() >0)
									//	    L_dblITVAL = L_dblITVAL + (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add
									//	else
									L_dblITVAL += (Double.parseDouble(L_strTEMP.substring(1)))* Double.parseDouble(L_strMULT);  // multiply with the multiplier factor and add

								}
								for(int k=0;k<tblITTAX.getRowCount();k++)
								{
									if(tblITTAX.getValueAt(k,0).toString().equals("true"))
									{
										if((L_strITMCD+L_strTAXCD).equals(tblITTAX.getValueAt(k,1).toString()+tblITTAX.getValueAt(k,2).toString()))
										{
											flgFOUND = true;
											if(L_strTXOCD.trim().length() >0)
											{    
												tblITTAX.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strMATCD+L_strTAXCD).toString()),2),k,6);   
												tblITDTL.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strMATCD+L_strTAXCD).toString()),2),j,TBL_VATBL);   
											}
											else
											{
												tblITTAX.setValueAt(setNumberFormat(dblTXVAL,2),k,6);   
												tblITDTL.setValueAt(setNumberFormat(dblTXVAL,2),j,TBL_VATBL);   
											}
										}
									}
								}	
							}
							// Common Tax
							if(hstCOTAX.get(L_strTAXCD)== null)
							{
								// // common tax is not present
							}
							else
							{
								L_strTEMP = hstCOTAX.get(L_strTAXCD).toString();
								L_STRTKN=new StringTokenizer(hstTAXCD.get(L_strTAXCD).toString(),"|");
								L_strMULT = L_STRTKN.nextToken();
								L_STRTKN.nextToken();
								L_strTXOCD = L_STRTKN.nextToken(); 
								if(L_strTEMP.substring(0,1).equals("P"))
								{
									//if(L_strTAXCD.equals("EXC"))
									dblTXVAL = (L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add
									if(L_strTXOCD.trim().length() >0)
										hstTXVAL.put(L_strTAXCD,new Double(Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*(Double.parseDouble(L_strTEMP.substring(1)))));
									else
										hstTXVAL.put(L_strTAXCD,new Double(L_dblITVAL*(Double.parseDouble(L_strTEMP.substring(1)))));
									if(L_strTXOCD.trim().length() >0)
										L_dblITVAL = L_dblITVAL+(Double.parseDouble(hstTXVAL.get(L_strTXOCD).toString())*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
									else
										L_dblITVAL = L_dblITVAL+(L_dblITVAL*Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
								}
								else 
								{
									dblTXVAL = L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add
									/////////////////////DO below only when 06/07 not present in srno
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
									{ if(!(txtSRLNO.getText().substring(1,3).equals("06")||txtSRLNO.getText().substring(1,3).equals("07")))
									   {  System.out.println("inside chanage");
										L_dblITVAL =L_dblITVAL +L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
									   }
									}
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
									{	
										if(!((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
										{
											L_dblITVAL =L_dblITVAL +L_dblFRACT*(Double.parseDouble(L_strTEMP.substring(1))* Double.parseDouble(L_strMULT));  // multiply with the multiplier factor and add	
											  
										}
									}
								}
								for(int x=0;x<tblCOTAX.getRowCount();x++)
								{
									if(tblCOTAX.getValueAt(x,0).toString().equals("true"))
									{
										if(L_strTAXCD.equals(tblCOTAX.getValueAt(x,1).toString()))
										{
											flgFOUND = true;
											if(L_strTXOCD.trim().length() >0)
											{
												tblCOTAX.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strTAXCD).toString()),2),x,5);   
												if(L_strTAXCD.equals("VAT"))
													tblITDTL.setValueAt(setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strTAXCD).toString()),2),j,TBL_VATBL);
												//System.out.println("VAT from 1" +setNumberFormat(Double.parseDouble(hstTXVAL.get(L_strTAXCD).toString()),2));
											}
											else
											{
												tblCOTAX.setValueAt(setNumberFormat(dblTXVAL,2),x,5);   
												if(L_strTAXCD.equals("VAT"))
													tblITDTL.setValueAt(setNumberFormat(dblTXVAL,2),j,TBL_VATBL);
												System.out.println("VAT from 2" +setNumberFormat(dblTXVAL,2));
											}

										}

									}
								}
							}

						}
						// Update the Item Value to ITVAL in POMST.
						if(!txtCURCD.getText().trim().equals("01"))
						{
							//			dblTXVAL = dblTXVAL*dblEXGRT;
							//			L_dblITVAL = L_dblITVAL*dblEXGRT;
						}
						// if for 06 or 07 not in srno 
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))							
							{
								if(!(txtSRLNO.getText().substring(1,3).equals("06")||txtSRLNO.getText().substring(1,3).equals("07")))
								{	
								tblITDTL.setValueAt(setNumberFormat(L_dblITVAL,2),j,TBL_ITVAL);
								dblBILVL +=L_dblITVAL;
								}
							}
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{	
							if(!((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
							{
								tblITDTL.setValueAt(setNumberFormat(L_dblITVAL,2),j,TBL_ITVAL);
								dblBILVL +=L_dblITVAL;
							}
						}
						
					}
				}

				if(!txtCURCD.getText().trim().equals("01"))
				{
					//		dblBILVL = dblBILVL*dblEXGRT;
				}
				//code added if srl no not has 06 or 07
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						if(!(txtSRLNO.getText().substring(1,3).equals("06")||txtSRLNO.getText().substring(1,3).equals("07")))
						{
						txtCALAM.setText(setNumberFormat(dblBILVL,2));
						}
					}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(!((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
					{
						txtCALAM.setText(setNumberFormat(dblBILVL,2));
					}
				}
				//System.out.println(dblBILVL*Double.parseDouble(txtEXGRT.getText().trim()));

			}
			catch(Exception L_E)
			{
				
				setMSG(L_E,"calBILVL");
			}
			return setNumberFormat(dblBILVL,2);
		}
		/**
   Procedure for adjusting the Cummulative issue value, if grin is accepted, material issued
   and stock has become 0.bill is passed afterwards. last issue no. is loaded with additional value.
		 */
		private void adjISSVL()
		{
			try
			{
				double L_dblSTKQT =0.0,L_dblPISVL=0.0,L_dblISSVL=0.0,L_dblISSRT=0.0,L_dblADJVL=0.0;
				String L_strPISNO,L_strTAGNO="",L_strMATCD,L_strISSDT="";
				ResultSet L_rstRSSET,L_rstRSSET1 ;
				M_strSQLQRY = "SELECT sp_ystdt,sp_yendt,sp_stsfl from sa_spmst where  "+
				" sp_syscd ='MM' ";
				System.out.println("005c");
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				String L_strPRCFL ="",L_strYSTDT="",L_strYENDT="";
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						datTEMP = M_rstRSSET.getDate("SP_YSTDT");
						if(datTEMP !=null)
						{
							L_strYSTDT = M_fmtLCDAT.format(datTEMP);
						}
						L_strPRCFL = M_rstRSSET.getString("SP_STSFL");
					}
					M_rstRSSET.close();
				}
				// Checking the previous Issue number, if stock qty is 0 then load the previous issue.  
				//M_strSQLQRY = "SELECT ST_MATCD,ST_STKQT,ST_PISNO from MM_STMST,MM_GRMST WHERE ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND "+
				//              "isnull(GR_ACPQT,0) >0 and ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
				//              " and isnull(st_stkqt,0) =0 and isnull(st_pisno,'') <> ''"+
				//              " and gr_grnno ='"+txtGRNNO.getText().trim()+"'";
				// Checking the previous Issue number, if stock qty is 0 then load the previous issue.  
				M_strSQLQRY = "SELECT ST_MATCD,ST_STKQT,ST_PISNO from MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
				" and isnull(st_stkqt,0) =0 and isnull(st_pisno,'') <> '' and st_CMPCD + st_strtp + st_matcd in (select gr_CMPCD + gr_strtp + gr_matcd from MM_GRMST where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_ACPQT,0) >0 and  gr_grnno ='"+txtGRNNO.getText().trim()+"')";
				System.out.println("005d "+M_strSQLQRY);
				L_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(L_rstRSSET1 !=null)
					while(L_rstRSSET1.next())
					{
						L_dblSTKQT = L_rstRSSET1.getDouble("ST_STKQT");
						L_strPISNO  = nvlSTRVL(L_rstRSSET1.getString("ST_PISNO"),"");
						L_strMATCD  = L_rstRSSET1.getString("ST_MATCD");
						if(L_strPISNO.length() >0)
						{
							//M_strSQLQRY = "SELECT IS_TAGNO,IS_ISSVL,isnull(STP_CISVL,0),IS_ISSQT,DATE(IS_AUTDT) L_ISSDT,"+
							//"isnull(STP_YOSVL,0)+isnull(STP_CRCVL,0)+isnull(STP_CSAVL,0)-(isnull(STP_CISVL,0)-isnull(STP_CMRVL,0))-(isnull(STP_YCLRT,0)*isnull(STP_YCSQT,0))L_ISSVL "+
							//" FROM MM_ISMST,MM_STPRC WHERE IS_STRTP = STP_STRTP AND IS_MATCD = STP_MATCD AND IS_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
							//" AND IS_ISSNO ='"+L_strPISNO +"' AND IS_MATCD ='"+L_strMATCD +"' and isnull(is_stsfl,'')='2' and isnull(is_issqt,0) >0";

							M_strSQLQRY = "SELECT IS_TAGNO,isnull(STP_CISVL,0),CONVERT(varchar,IS_AUTDT,103) L_ISSDT,"+
							"isnull(STP_YOSVL,0)+isnull(STP_CRCVL,0)+isnull(STP_CSAVL,0)-(isnull(STP_CISVL,0)-isnull(STP_CMRVL,0))-(isnull(STP_YCLRT,0)*isnull(STP_YCSQT,0))L_ISSVL,sum(IS_ISSQT) IS_ISSQT,sum(IS_ISSVL) IS_ISSVL "+
							" FROM MM_ISMST,MM_STPRC WHERE IS_STRTP = STP_STRTP AND IS_MATCD = STP_MATCD AND IS_CMPCD = STP_CMPCD AND IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
							" AND IS_ISSNO ='"+L_strPISNO +"' AND IS_MATCD ='"+L_strMATCD +"' and isnull(is_stsfl,'')='2' and isnull(is_issqt,0) >0 group by IS_TAGNO,isnull(STP_CISVL,0),CONVERT(varchar,IS_AUTDT,103),"+
							"isnull(STP_YOSVL,0)+isnull(STP_CRCVL,0)+isnull(STP_CSAVL,0)-(isnull(STP_CISVL,0)-isnull(STP_CMRVL,0))-(isnull(STP_YCLRT,0)*isnull(STP_YCSQT,0))";
							System.out.println("005e");
							System.out.println(M_strSQLQRY);
							L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							if(L_rstRSSET !=null)
							{
								if(L_rstRSSET.next())
								{
									L_dblPISVL = L_rstRSSET.getDouble("IS_ISSVL");
									L_dblADJVL = L_rstRSSET.getDouble("L_ISSVL");
									L_dblISSVL = L_dblPISVL+L_dblADJVL ;
									L_dblISSRT =  L_dblISSVL/L_rstRSSET.getDouble("IS_ISSQT");
									L_strTAGNO  = L_rstRSSET.getString("IS_TAGNO");
									datTEMP = L_rstRSSET.getDate("L_ISSDT");
									if(datTEMP !=null)
										L_strISSDT = M_fmtLCDAT.format(datTEMP);
									// Issue belongs to previous year
									if(M_fmtLCDAT.parse(L_strISSDT).compareTo(M_fmtLCDAT.parse(L_strYSTDT))<0)
									{
										// Processing is not done for the prevous year
										if(!L_strPRCFL.equals("1"))
										{
											//updISMST_BIL();
											//  updISMST_BIL(LP_STRTP,LP_ISSNO,LP_MATCD,LP_TAGNO,LP_ISSVL,LP_ISSRT,LP_USRCD) 
											System.out.println(M_strSBSCD.substring(2,4)+","+L_strPISNO+","+L_strMATCD+","+L_strTAGNO+","+setNumberFormat(L_dblISSVL,2)+","+setNumberFormat(L_dblISSRT,2)+","+cl_dat.M_strUSRCD_pbst);
											if(!((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("07")||((String)(cmbBLPTP.getSelectedItem())).substring(0, 2).equals("06"))
												{
												cstISMST_BIL = cl_dat.M_conSPDBA_pbst.prepareCall("{ call updISMST_BIL(?,?,?,?,?,?,?,?)}");
												cstISMST_BIL.setString(1,cl_dat.M_strCMPCD_pbst);
												cstISMST_BIL.setString(2,M_strSBSCD.substring(2,4));
												cstISMST_BIL.setString(3,L_strPISNO);
												cstISMST_BIL.setString(4,L_strMATCD);
												cstISMST_BIL.setString(5,L_strTAGNO);
												cstISMST_BIL.setString(6,setNumberFormat(L_dblISSVL,2));
												cstISMST_BIL.setString(7,setNumberFormat(L_dblISSRT,2));
												cstISMST_BIL.setString(8,cl_dat.M_strUSRCD_pbst);
												cstISMST_BIL.executeUpdate();
											}
											//M_strSQLQRY = " UPDATE mm_ismst set is_issvl = " +L_dblISSVL +
											//	 ", IS_ISSRT ="+L_dblISSRT +
											//	 " WHERE IS_STRTP = '"+M_strSBSCD.substring(2,4) +"'"+
											//	 " and IS_ISSNO = '"+L_strPISNO +"'"+
											//	 " and IS_MATCD = '"+L_strMATCD +"'"+
											//	 " and IS_TAGNO = '"+L_strTAGNO +"'";
											//cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
											if((L_dblISSVL -L_dblPISVL) !=0)
											{
												M_strSQLQRY = " UPDATE mm_stprc set stp_cisvl = isnull(stp_cisvl,0) + "+(L_dblISSVL -L_dblPISVL) ;
												M_strSQLQRY += " where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_strtp ='"+M_strSBSCD.substring(2,4) +"'";
												M_strSQLQRY += " and stp_matcd ='"+L_strMATCD +"'";
												//M_strSQLQRY += " and stp_matcd ='"+L_strMATCD +"' and isnull(stp_cisqt,0) >0";
												cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
											}
										}
									}
									else
									{
										System.out.println("006a"+M_strSBSCD.substring(2,4)+","+L_strPISNO+","+L_strMATCD+","+L_strTAGNO+","+setNumberFormat(L_dblISSVL,2)+","+setNumberFormat(L_dblISSRT,2)+","+cl_dat.M_strUSRCD_pbst);
										cstISMST_BIL = cl_dat.M_conSPDBA_pbst.prepareCall("{ call updISMST_BIL(?,?,?,?,?,?,?,?)}");
										cstISMST_BIL.setString(1,cl_dat.M_strCMPCD_pbst);
										cstISMST_BIL.setString(2,M_strSBSCD.substring(2,4));
										cstISMST_BIL.setString(3,L_strPISNO);
										cstISMST_BIL.setString(4,L_strMATCD);
										cstISMST_BIL.setString(5,L_strTAGNO);
										cstISMST_BIL.setString(6,setNumberFormat(L_dblISSVL,2));
										cstISMST_BIL.setString(7,setNumberFormat(L_dblISSRT,2));
										cstISMST_BIL.setString(8,cl_dat.M_strUSRCD_pbst);
										cstISMST_BIL.executeUpdate();

										//M_strSQLQRY = " UPDATE mm_ismst set is_issvl ="+L_dblISSVL +
										//		", IS_ISSRT ="+L_dblISSRT +
										//	   	 " WHERE IS_STRTP = '"+M_strSBSCD.substring(2,4) +"'"+
										//		 " and IS_ISSNO = '"+L_strPISNO +"'"+
										//		 " and IS_MATCD = '"+L_strMATCD +"'"+
										//		 " and IS_TAGNO = '"+L_strTAGNO +"'";
										//cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
										if((L_dblISSVL -L_dblPISVL) !=0)
										{
											M_strSQLQRY = " UPDATE mm_stprc set stp_cisvl = isnull(stp_cisvl,0) + "+(L_dblISSVL -L_dblPISVL) ;
											M_strSQLQRY += " where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_strtp ='"+M_strSBSCD.substring(2,4) +"'";
											M_strSQLQRY += " and stp_matcd ='"+L_strMATCD +"'";
											//M_strSQLQRY += " and stp_matcd ='"+L_strMATCD +"' and isnull(stp_cisqt,0) >0";
											cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
										}
									}
								}
								L_rstRSSET.close();
							}
						}
					}
				if(L_rstRSSET1 !=null)
					L_rstRSSET1.close();
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"adjISSVL");
			}
		}

	}