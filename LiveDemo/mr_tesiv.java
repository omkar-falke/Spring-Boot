import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JComponent;import javax.swing.JCheckBox;
import java.util.StringTokenizer;
import java.awt.event.*;
import java.sql.ResultSet;import java.sql.CallableStatement;
import java.awt.*;
import javax.swing.*;
import java.util.Calendar;import java.util.Hashtable;import java.util.Vector;import java.util.Enumeration;
class mr_tesiv extends cl_pbase
{
    private JLabel lblDSRTP;	 /** Distributor Code */
	private JTextField txtDSRCD;	/** Buyer Code */
	private JTextField txtBYRCD;	/** Consignee Code */
	private JTextField txtCNSCD;	/** Transporter Code */
	private JTextField txtTRPCD;	/** Buyer Name */
	private JTextField txtBYRNM;	/** Comsignee Name */
	private JTextField txtCNSNM;	/** Transporter Name */
	private JTextField txtTRPNM;
	private JTextField txtDLCCD;
	private JTextField txtINVNO;
	private JTextField txtINVDT;
	private JTextField txtINVTM;
	private JTextField txtLRNO;
	private JTextField txtLRDT;
	private JTextField txtPORNO;
	private JTextField txtPORDT;
	private JTextField txtINVRF;
	private JTextField txtIVRDT;
	private JTextField txtLRYNO;
	private JTextField txtTMOCD;
	private JTextField txtDORNO;
	private JTextField txtINDNO;
	private JTextField txtCS2CD;
	private JTextField txtCS2NM;

	private JLabel lblTMODS;
	private JTextField txtCPTVL;
	private JLabel lblINVVL;
	private JTextField txtREMDS;
	private java.util.Date datREFDT;
	private java.util.Date datINVDT;
	
	private JLabel lblDLCDS;
	private JTextField txtDSRNM;
	private cl_JTable tblIVTRN;
	private cl_JTable tblIVENQ;
	private final int TBL_CHKFL =0;
	private final int TBL_INVNO =1;
	private final int TBL_PRDDS =2;
	private final int TBL_AVLQT =3;
	private final int TBL_BASRT =4;
	private final int TBL_BASVL =5;

//	private final int TBL_EXCVL =6;//
//	private final int TBL_EDCVL =7;//
//	private final int TBL_EHCVL =8;//

	private final int TBL_INVRT =6;
	private final int TBL_INVQT =7;
	private final int TBL_ASSVL =8;
	private final int TBL_EX1VL =9;
	private final int TBL_CVDVL =10;
	private final int TBL_ACVVL =11;

	private final int TBL_ED1VL =12;
	private final int TBL_EH1VL =13;
	private final int TBL_STXVL =14;
	private final int TBL_SCHVL =15;
	
	private final int TBL_DSCVL =16;
	private final int TBL_FRTVL =17;

	private final int TBL_NETVL =18;
	private final int TBL_PRDTP =19;
	private final int TBL_PRDCD =20;
	private final int TBL_PKGWT =21;
	private final int TBL_PKGTP =22;
	private final int TBL_INVPK =23;
	private final int TBL_EXCRT =24;
	private final int TBL_EDCRT =25;
	private final int TBL_EHCRT =26;
	private final int TBL_CVDPR =27;
	private final int TBL_ACVPR =28;
	private final int TBL_EXCRT1 =29;

	private final String strMKTTP ="01";
	private final String strSALTP ="14";
	private final String strTFRTP ="15";
	private final String strRTLSL_fn ="R0251";

	private String strSTXRT ="0";
	private String strSCHRT ="0";
	private String strINVNO =" ";
	private String strINVVL ="0";
	private String strBYRDT;	
	private String strDSRDT;	
	private String strCNSDT;
    private String strTRPDT;
	private String strINVPR ="";
	private String strBYRCD ="";
	private TBLINVFR objTBLVRF;
	private String strYREND = "31/03/2009";
      private String strYRDGT ="";
    private int intROWNO =0;  

//	private String strPREFIX  =cl_dat.M_strFNNYR1_pbst.substring(3)+"5"; // 5 for consignment stockist related entries
	private String strPREFIX ="";
	private JComboBox cmbSALTP;
	private Hashtable<String,String[]> hstCDTRN;
	private Hashtable<String,String> hstCODDS;
	private Vector<String> vtrSALTP;

	/** Variables for Code Transaction Table
	 */
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	private String strPRDCD;		
											/** Array elements for hstCDTRN */
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

	CallableStatement cstPTTRN_INV;
    CallableStatement cstPLTRN_INV;
    CallableStatement cstIVTRN_TRF;

	//CallableStatement cstDSHINV;	    // Stored procedure for Delivery Schedule record updation
	//CallableStatement cstPTTRN_INV;	    // Stored procedure for creating Credit Note (Bkg.Discount & Distr.Comm.)Transactions
	//CallableStatement cstPLTRN_INV;		// Stored procedure for creating Sales Invoice records in Party Ledger
	//CallableStatement cstPLTRN_PTT;		// Stored procedure for creating Credit Note (Bkg.Discount & Distr.Comm.) records in Party Ledger
	//CallableStatement cstPATRN_PTT;		// Stored procedure for auto adjustment of Credit Note (Bkg.Discount & Distr.Comm.) records.
	//CallableStatement cstDSPQT_RES;		// Stored procedure for updating Despatch Qty. against reservation

    mr_tesiv()
    {
        super(2);
        setMatrix(20,10);
		vtrSALTP = new Vector<String>();
		hstCDTRN = new Hashtable<String,String[]>();
		hstCODDS = new Hashtable<String,String>();

		hstCDTRN.clear();
        hstCODDS.clear();
		crtCDTRN("'SYSMR00SAL'"," and cmt_codcd in ('"+strSALTP+"','"+strTFRTP+"')",hstCDTRN);

		try
	  {
 		strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "0" : "9";
		strPREFIX  =strYRDGT+"5"; 
		datREFDT = M_fmtLCDAT.parse("06/12/2008");
	  }	
	  catch(Exception L_E)
	  {
		setMSG(L_E,"");
	  }			

		add(new JLabel("Sale Type"),1,1,1,1,this,'L');
		add(cmbSALTP=new JComboBox(),1,2,1,1,this,'L');
		
		add(new JLabel("Distr."),1,3,1,0.75,this,'L');
		add(lblDSRTP=new JLabel(" "),1,3,1,0.25,this,'R');
		add(txtDSRCD=new TxtLimit(5),1,4,1,1,this,'L');
		add(txtDSRNM=new TxtLimit(45),1,5,1,4,this,'L');
		add(new JLabel("D.O. No."),1,9,1,1,this,'L');
		add(txtDORNO=new TxtLimit(8),1,10,1,1,this,'L');

		add(new JLabel("Invoice No"),2,1,1,1,this,'L');
		add(txtINVNO=new TxtLimit(8),2,2,1,1,this,'L');
		add(new JLabel("Invoice Date"),2,3,1,1,this,'L');
        add(txtINVDT=new TxtDate(),2,4,1,1,this,'L');
        add(txtINVTM=new TxtTime(),2,5,1,1,this,'L');
        add(new JLabel("Mode of Transport"),2,6,1,2,this,'L');
        add(txtTMOCD=new TxtLimit(2),2,7,1,1,this,'L');
		add(new JLabel("Indent No."),2,9,1,1,this,'L');
		add(txtINDNO=new TxtLimit(3),2,10,1,1,this,'L');

		add(lblTMODS=new JLabel(),2,8,1,2,this,'L');
       // add(new JLabel("Dispatch Date"),2,5,1,1.5,this,'L');
       // add(txtLODDT=new TxtDate(),2,7,1,1.5,this,'R');
        
        add(new JLabel("Buyer"),3,1,1,1,this,'L');
		add(txtBYRCD=new TxtLimit(5),3,2,1,1,this,'L');
		add(txtBYRNM=new TxtLimit(45),3,3,1,6,this,'L');
		add(new JLabel("Destination"),3,9,1,1,this,'L');
		add(txtDLCCD=new TxtLimit(3),3,10,1,1,this,'L');
		
		add(new JLabel("Consignee"),4,1,1,1,this,'L');
		add(txtCNSCD=new TxtLimit(5),4,2,1,1,this,'L');
		add(txtCNSNM=new TxtLimit(45),4,3,1,6,this,'L');
		add(lblDLCDS=new JLabel(""),4,11,1,2,this,'R');
		
		add(new JLabel("L.R No"),5,1,1,1,this,'L');
		add(txtLRNO=new TxtLimit(15),5,2,1,1,this,'L');
		add(new JLabel("Date"),5,3,1,1,this,'L');
        add(txtLRDT=new TxtDate(),5,4,1,1,this,'L');
        add(new JLabel("P.O No"),5,5,1,1,this,'L');
		add(txtPORNO=new TxtLimit(30),5,6,1,1,this,'L');
		add(new JLabel("Date"),5,7,1,1,this,'L');
        add(txtPORDT=new TxtDate(),5,8,1,1,this,'L');
        add(new JLabel("Payment Days"),5,9,1,1,this,'L');
        add(txtCPTVL=new TxtNumLimit(2.0),5,10,1,1,this,'L');
        add(new JLabel("Transporter"),6,1,1,1,this,'L');
		add(txtTRPCD=new TxtLimit(5),6,2,1,1,this,'L');
		add(txtTRPNM=new TxtLimit(45),6,3,1,6,this,'L');
		add(new JLabel("Lorry No"),6,9,1,1,this,'L');
		add(txtLRYNO=new TxtLimit(15),6,10,1,1,this,'L');
		//add(txtTRPCD=new TxtLimit(5),6,2,1,1,this,'L');
		add(new JLabel("Invoice Ref"),7,1,1,1,this,'L');
		add(txtINVRF=new TxtLimit(8),7,2,1,1,this,'L');
		add(new JLabel("Date"),7,3,1,1,this,'L');
        add(txtIVRDT=new TxtLimit(20),7,4,1,2,this,'L');

		add(new JLabel("C.S Code"),7,6,1,1,this,'L');
		add(txtCS2CD=new TxtLimit(5),7,7,1,1,this,'L');
		add(txtCS2NM=new TxtLimit(45),7,8,1,2,this,'L');
		
       	objTBLVRF = new TBLINVFR();
       	setMatrix(20,10);
		//tblIVTRN=crtTBLPNL1(this,new String[]{"FL","Inv.No","Grade","Avl.Qty","Bas.Rt","Bas.Val.","Exc","Cvd","A.Cvd","Cess","H.Cess","Rate","Inv Qty","Value","Exc","Cess","H.Cess","S.tax","Sur","Net Value","Prdtp","Prdcd","Pkg Wt.","PkgTp","InvPK","Excrt","EDCRT","EHCRT"},20,8,1,7,10,new int[]{20,60,60,35,50,70,75,70,60,60,60,60,60,75,40,35,50,50,50,30,5,5,5,5,5,5,5,5},new int[]{0});
		tblIVTRN=crtTBLPNL1(this,new String[]{"FL","Inv.No","Grade","Avl.Qty","Bas.Rt","Bas.Val.","Rate","Inv Qty","Value","Exc","Cvd","A.Cvd","Cess","H.Cess","S.tax","Sur","DSC","FRT","Net Value","Prdtp","Prdcd","Pkg Wt.","PkgTp","InvPK","Excrt","EDCRT","EHCRT","CVDPR","ACVPR","EXCRT1"},20,8,1,7,10,new int[]{20,60,60,35,50,70,60,60,75,40,35,50,50,50,50,50,50,50,30,5,5,5,5,5,5,5,5,5,5,5},new int[]{0});

		add(new JLabel("Invoice Value"),16,7,1,2,this,'L');
		add(lblINVVL=new JLabel(),16,9,1,2,this,'L');
		add(new JLabel("Remarks"),17,1,1,1,this,'L');
		add(txtREMDS=new TxtLimit(200),17,2,1,8,this,'L');
		cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		objTBLVRF = new TBLINVFR();
		tblIVTRN.setInputVerifier(objTBLVRF);	
		INPVF oINPVF =new INPVF();
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
//		try
//		{
//		    cstPTTRN_INV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPTTRN_INV(?,?,?)");
//		    cstPLTRN_INV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_INV(?,?,?)");
//		    cstIVTRN_TRF = cl_dat.M_conSPDBA_pbst.prepareCall("call insTRFINV(?,?,?,?)");
//
//		}
//		catch(Exception L_E)
//		{
//		}
    }
   void setENBL(boolean L_FLAG)
   {
        super.setENBL(L_FLAG);
        //txtBYRCD.setEnabled(false);
        txtBYRNM.setEnabled(false);
	    txtDSRNM.setEnabled(false);
	    txtCNSNM.setEnabled(false);
	    txtTRPNM.setEnabled(false);
	    txtINDNO.setEnabled(false);
	    txtIVRDT.setEnabled(false);
	    txtCS2NM.setEnabled(false);

	  ///  tblIVTRN.cmpEDITR[TBL_CHKFL].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_PRDCD].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_PRDDS].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_PKGTP].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_AVLQT].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_BASRT].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_BASVL].setEnabled(false);
	//    tblIVTRN.cmpEDITR[TBL_EXCVL].setEnabled(false);
	//    tblIVTRN.cmpEDITR[TBL_EDCVL].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_EX1VL].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_ED1VL].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_PRDTP].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_PRDCD].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_PKGTP].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_PKGWT].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_INVPK].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_EXCRT].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_EDCRT].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_ASSVL].setEnabled(false);
	    tblIVTRN.cmpEDITR[TBL_NETVL].setEnabled(false);

	   //  tblIVTRN.cmpEDITR[TBL_PRDCD].setEnabled(false);
	   // tblIVTRN.cmpEDITR[TBL_INVPK].setEnabled(false);
        txtINVNO.setEnabled(true);
	  /*  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
	    {
	        txtINVNO.setEnabled(true);
	    }
	    else txtINVNO.setEnabled(false);*/
	    
   }
   public void actionPerformed(ActionEvent L_AE)
   {
        super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setCMBSALTP();
				//setCDLST(vtrSALTP,"SYSMR00SAL","CMT_CODDS");
				//setCMBVL(cmbSALTP,vtrSALTP);
				//cmbSALTP.setSelectedItem(getCDTRN("SYSMR00SAL"+M_strSBSCD.substring(2,4),"CMT_CODDS",hstCDTRN));
				cmbSALTP.setEnabled(true);
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ActionPerformed");
		}
   }
   public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
       if(L_KE.getKeyCode()==L_KE.VK_ENTER)
       {
           if(M_objSOURC == txtDSRCD)
		   {
		     txtINVNO.requestFocus();
		     //txtBYRNM.setText(txtDSRNM.getText());
			 //txtBYRCD.setText(txtDSRCD.getText());
			 txtINVDT.setText(cl_dat.M_strLOGDT_pbst);
			 txtINVTM.setText(cl_dat.M_txtCLKTM_pbst.getText());
			 //txtLODDT.setText(cl_dat.M_strLOGDT_pbst);
			 txtTRPCD.setText("S0029");
			 txtTRPNM.setText("SELF");
		   }
		  	else if(M_objSOURC == txtINVNO)
		   {
		        if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		        {
			        txtINVNO.setText(txtINVNO.getText().toUpperCase());
				//	System.out.println("call to getDATA");
					getDATA();
		        }
		        else txtINVDT.requestFocus();    
		   }
		   else if(M_objSOURC == txtINVDT)
		   {
			   // txtINVTM.requestFocus();
			   // txtINVTM.setText(cl_dat.M_txtCLKTM_pbst.getText());
			    txtTMOCD.requestFocus();
		   }
		   else if(M_objSOURC == txtTMOCD)
		   {
			    txtBYRCD.requestFocus();
		   }
		   else if(M_objSOURC == txtINVTM)
		   {
			    txtTMOCD.requestFocus();
		   }
		   else if(M_objSOURC == txtBYRCD)
		   {
			    txtCNSCD.requestFocus();
		   }
		   else if(M_objSOURC == txtCNSCD)
		   {
				if(txtCNSCD.getText().equals(strRTLSL_fn))
				{
				   txtCNSNM.setEnabled(true);
				   txtCNSNM.requestFocus();	
				}
				else
				{
				   txtCNSNM.setEnabled(false);
				    txtLRNO.requestFocus();
				}
		   }
		else if(M_objSOURC == txtCNSNM)
		   {
			    txtLRNO.requestFocus();
		   }

		   else if(M_objSOURC == txtLRNO)
		   {
			    txtLRDT.requestFocus();
		   }
		   else if(M_objSOURC == txtLRDT)
		   {
			    txtPORNO.requestFocus();
		   }
		   else if(M_objSOURC == txtPORNO)
		   {
			    txtPORDT.requestFocus();
		   }
		   else if(M_objSOURC == txtPORDT)
		   {
			    txtCPTVL.requestFocus();
		   }
		   else if(M_objSOURC == txtCPTVL)
		   {
			    txtTRPCD.requestFocus();
		   }
		   else if(M_objSOURC == txtTRPCD)
		   {
			    txtLRYNO.requestFocus();
		   }
		   else if(M_objSOURC == txtLRYNO)
		   {
			    txtINVRF.requestFocus();
		   }
		   else if(M_objSOURC == txtINVRF)
		   {
		       // txtIVRDT.requestFocus();
		        if(intROWNO == 0)
		        	tblIVTRN.clrTABLE();
			    getINVDT(txtINVRF.getText().trim(),intROWNO);
			   /* tblIVTRN.setRowSelectionInterval(tblIVTRN.getSelectedRow(),tblIVTRN.getSelectedRow());		
				tblIVTRN.setColumnSelectionInterval(TBL_INVQT,TBL_INVQT);		
				tblIVTRN.editCellAt(tblIVTRN.getSelectedRow(),TBL_INVQT);
				tblIVTRN.cmpEDITR[TBL_INVQT].requestFocus();*/
		   }
		   
       }
        if(M_objSOURC==txtINVRF)
		{
		    if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtINVRF";
				M_strSQLQRY ="Select distinct IVT_INVNO,IVT_INVDT from MR_IVTR1 where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='04' AND (isnull(IVT_INVQT,0) -isnull(IVT_SALQT,0)) >0 and IVT_DSRCD ='"+txtDSRCD.getText().trim()+"' order by IVT_INVNO" ;
			  	cl_hlp(M_strSQLQRY,1,1,new String[] {"Invoice Ref","date"},2,"CT");
			}
		}
		if(M_objSOURC==txtINVNO)
		{
		    if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtINVNO";
				M_strSQLQRY ="Select distinct IVT_INVNO,IVT_INVDT from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='14' and IVT_DSRCD ='"+txtDSRCD.getText().trim()+"'";//and IVT_INVNO ='"+txtINVNO.getText().trim()+"' order by IVT_INVNO" ;
               		if(txtINVNO.getText().length() >0)
				M_strSQLQRY += " AND IVT_INVNO like '"+txtINVNO.getText().trim() +"%'";
				 System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[] {"Invoice Ref","date"},2,"CT");
			}
		}
		if(M_objSOURC==txtDLCCD)
		{
		    if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtDLCCD";
			    cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP='COXXDST' order by CMT_CODDS" ,2,1,new String[] {"Grade Code","Description"},2,"CT");
			}
		}
		if(M_objSOURC==txtTMOCD)
		{
		    if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtTMOCD";
			    cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP='MR01MOT' order by CMT_CODDS" ,2,1,new String[] {"Grade Code","Description"},2,"CT");
			}
		}
		if(M_objSOURC==txtDORNO)
		{
		    if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtDORNO";
			    //cl_hlp("Select DOT_DORNO,DOT_INDNO from MR_DOTRN where DOT_SBSCD LIKE '61%' AND (DOT_DORQT - isnull(DOT_INVQT,0))>0 order by DOT_DORNO DESC" ,2,1,new String[] {"D.O. NO.","Order No."},2,"CT");
				M_strSQLQRY = "Select DOT_DORNO,DOT_INDNO from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (DOT_DORQT - isnull(DOT_INVQT,0))>0 order by DOT_DORNO DESC";
				System.out.println(M_strSQLQRY);
			    cl_hlp(M_strSQLQRY ,1,1,new String[] {"D.O. NO.","Order No."},2,"CT");

			}
		}
	   if(M_objSOURC==txtTRPCD)
		{
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtTRPCD1";
				cl_hlp("Select PT_PRTNM,PT_PRTCD,PT_FAXNO,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='T' and PT_STSFL<>'X' order by PT_PRTNM" ,1,1,new String[] {"Transporter Name","Transporter Code"},6,"CT");
			}
			else if(L_KE.getKeyCode()==L_KE.VK_F2)
			{
				M_strHLPFLD = "txtTRPCD2";
				cl_hlp("Select PT_PRTCD,PT_PRTNM,PT_FAXNO,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='T' and PT_STSFL<>'X' order by PT_PRTCD" ,1,1,new String[] {"Transporter Code","Transporter Name"},6,"CT");
			}
		}  
		else if(M_objSOURC==txtDSRCD)
		{
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtDSRCD1";
				cl_hlp("Select PT_PRTNM,PT_PRTCD,PT_DSTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='G' and PT_STSFL<>'X' and PT_ZONCD = '"+M_strSBSCD.substring(0,2)+"'  and PT_PRTCD in (select distinct IVT_DSRCD from MR_IVTR1 where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='04' AND (isnull(IVT_INVQT,0) -isnull(IVT_SALQT,0)) >0 ) order by PT_PRTNM" ,1,1,new String[] {"Stockist Name","Stockist Code"},6,"CT");
			}
			else if(L_KE.getKeyCode()==L_KE.VK_F2)
			{
				M_strHLPFLD = "txtDSRCD2";
				cl_hlp("Select PT_PRTCD,PT_PRTNM,PT_DSTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='G' and PT_STSFL<>'X'  and PT_ZONCD = '"+M_strSBSCD.substring(0,2)+"'  order by PT_PRTCD" ,1,1,new String[] {"Transporter Code","Transporter Name"},6,"CT");
			}
			
		}
		else if(M_objSOURC==txtCS2CD)
		{
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "txtCS2CD1";
				cl_hlp("Select PT_PRTNM,PT_PRTCD,PT_DSTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='G' and PT_STSFL<>'X'  and PT_ZONCD = '"+M_strSBSCD.substring(0,2)+"'  order by PT_PRTNM" ,1,1,new String[] {"Stockist Name","Stockist Code"},6,"CT");
			}
			else if(L_KE.getKeyCode()==L_KE.VK_F2)
			{
				M_strHLPFLD = "txtCS2CD2";
				cl_hlp("Select PT_PRTCD,PT_PRTNM,PT_DSTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='G' and PT_STSFL<>'X'  and PT_ZONCD = '"+M_strSBSCD.substring(0,2)+"'  order by PT_PRTCD" ,1,1,new String[] {"Transporter Code","Transporter Name"},6,"CT");
			}
			
		}  
  
		else if(M_objSOURC==txtCNSCD)
		{
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{//Search by Name
					M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and upper(isnull(PT_STSFL,' ')) <> 'X'";
						//and PT_CNSRF = '"+txtDSRCD.getText()+"' 
					if(txtCNSCD.getText().length() >0)
				        M_strSQLQRY +=" and PT_PRTCD like '"+txtCNSCD.getText().trim() +"%'";
				    M_strSQLQRY += "ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtCNSCD1";
					cl_hlp(M_strSQLQRY ,1,2,new String[] {"Consignee Code","Consignee Name"},5 ,"CT");
			}
			else if(L_KE.getKeyCode()==L_KE.VK_F2)
			{//Search by Code
				M_strSQLQRY="SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and upper(isnull(PT_STSFL,' ')) <> 'X' ";
					//and PT_CNSRF = '"+txtDSRCD.getText()+"'
				if(txtCNSCD.getText().length() >0)
				    M_strSQLQRY +=" and PT_PRTCD like '"+txtCNSCD.getText().trim() +"%'";
				M_strSQLQRY += "ORDER BY PT_PRTNM";
				//M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtCNSCD.getText()+"%' "+(M_strSBSCD.equals("511600")==true ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
				M_strHLPFLD = "txtCNSCD2";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Consignee Code","Consignee Name"},5,"CT");
				cl_dat.M_txtHLPPOS_pbst.setText(txtBYRCD.getText().toUpperCase());
			}
		}
		else if(M_objSOURC==txtBYRCD)
		{
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{//Search by Name
					/*if(txtCNSCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(!(txtCNSCD.getText().length()==1))
						return;*/
					M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_CNSRF = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X'";
					if(strBYRCD.trim().length() >0)
						M_strSQLQRY +=" and PT_PRTCD = '"+strBYRCD+"'";
					if(txtBYRCD.getText().length() >0)
				        M_strSQLQRY +=" and PT_PRTCD like '"+txtBYRCD.getText().trim() +"%'";
				   if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strTFRTP))
				   {
					M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and upper(isnull(PT_STSFL,' ')) <> 'X'";
					if(txtBYRCD.getText().length() >0)
				        M_strSQLQRY +=" and PT_PRTCD like '"+txtBYRCD.getText().trim() +"%'";

				   }	
				    M_strSQLQRY += "ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtBYRCD1";
					cl_hlp(M_strSQLQRY ,1,2,new String[] {"Buyer Code","Buyer Name"},5 ,"CT");
			}
		}
	}
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtINVRF"))
			{
			    L_STRTKN.nextToken();
				txtIVRDT.setText(L_STRTKN.nextToken());
				txtINVRF.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtINVNO"))
			{
			   	txtINVNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtTMOCD"))
			{
			    txtTMOCD.setText(L_STRTKN.nextToken());
			    lblTMODS.setText(L_STRTKN.nextToken());
			   	//txtTMOCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtDORNO"))
			{
			    txtDORNO.setText(L_STRTKN.nextToken());
			    txtINDNO.setText(L_STRTKN.nextToken());
			   	//txtTMOCD.setText(cl_dat.M_strHLPSTR_pbst);
			}

			else if(M_strHLPFLD.equals("txtTRPCD1"))
			{
				txtTRPNM.setText(L_STRTKN.nextToken());
				txtTRPCD.setText(L_STRTKN.nextToken());
          	}
			else if(M_strHLPFLD.equals("txtTRPCD2"))
			{
				txtTRPCD.setText(L_STRTKN.nextToken());
                txtTRPNM.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtDSRCD1"))
			{
				txtDSRNM.setText(L_STRTKN.nextToken());
				txtDSRCD.setText(L_STRTKN.nextToken());
	           // txtBYRNM.setText(txtDSRNM.getText());
			//	txtBYRCD.setText(txtDSRCD.getText());
							
          	}
			else if(M_strHLPFLD.equals("txtDSRCD2"))
			{
				txtDSRCD.setText(L_STRTKN.nextToken());
            	    txtDSRNM.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtCS2CD2"))
			{
				txtCS2CD.setText(L_STRTKN.nextToken());
            	    	txtCS2NM.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtCS2CD1"))
			{
				txtCS2NM.setText(L_STRTKN.nextToken());
				txtCS2CD.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtBYRCD1"))
			{
				txtBYRNM.setText(L_STRTKN.nextToken());
				txtBYRCD.setText(L_STRTKN.nextToken());
          	}
			else if(M_strHLPFLD.equals("txtBYRCD2"))
			{
				txtBYRCD.setText(L_STRTKN.nextToken());
                txtBYRNM.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtCNSCD1"))
			{
				txtCNSNM.setText(L_STRTKN.nextToken());
				txtCNSCD.setText(L_STRTKN.nextToken());
				if(txtCNSCD.getText().equals(strRTLSL_fn))
				   txtCNSNM.setEnabled(true);
				else
				   txtCNSNM.setEnabled(false);
	
          	}
			else if(M_strHLPFLD.equals("txtCNSCD2"))
			{
				txtCNSCD.setText(L_STRTKN.nextToken());
	                txtCNSNM.setText(L_STRTKN.nextToken());
			    if(txtCNSCD.getText().equals(strRTLSL_fn))
				   txtCNSNM.setEnabled(true);
				else
				   txtCNSNM.setEnabled(false);

			}
			else if(M_strHLPFLD.equals("txtDLCCD"))
			{
				txtDLCCD.setText(L_STRTKN.nextToken());
				while(L_STRTKN.hasMoreTokens())
					lblDLCDS.setText(L_STRTKN.nextToken());
			}
			/*if(M_strHLPFLD.equals("txtDLCCD"))
			{
				lblDLCDS.setText(L_STRTKN.nextToken());
				txtDLCCD.setText(L_STRTKN.nextToken());
          	}*/
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Child.exeHLPOK");
		}
	}
	void clrCOMP()
	{
	    super.clrCOMP();
	    lblINVVL.setText("");
	    intROWNO = 0;
	}
	void getINVDT(String P_strINVRF,int P_intROWNO)
	{
	    try
	    {
	        double L_dblNETVL =0;
	
		 if(txtDORNO.getText().length() ==0)	
				M_strSQLQRY = "SELECT IVT_PRDTP,IVT_PRDCD,IVT_PRDDS,IVT_PKGTP,IVT_PKGWT,IVT_PKGTP,IVT_INVRT,(IVT_INVQT - isnull(IVT_SALQT,0)) L_AVLQT,(IVT_INVQT - isnull(IVT_SALQT,0)) L_INVQT,IVT_ASSVL,((IVT_INVQT - isnull(IVT_SALQT,0))* IVT_INVRT)IVT_BASVL,(IVT_CVDVL/IVT_INVQT)L_CVDPR,(IVT_ACVVL/IVT_INVQT)L_ACVPR,(IVT_EXCVL/IVT_INVQT)L_EXCPR,(IVT_EDCVL/IVT_INVQT)L_EDCPR,(IVT_EHCVL/IVT_INVQT)L_EHCPR,((IVT_INVQT - isnull(IVT_SALQT,0))*IVT_CVDVL/IVT_INVQT)L_CVDVL,((IVT_INVQT - isnull(IVT_SALQT,0))*IVT_ACVVL/IVT_INVQT)L_ACVVL,((IVT_INVQT - isnull(IVT_SALQT,0))*IVT_EXCVL/IVT_INVQT)L_EXCVL,((IVT_INVQT - isnull(IVT_SALQT,0))*IVT_EDCVL/IVT_INVQT)L_EDCVL,((IVT_INVQT - isnull(IVT_SALQT,0))*IVT_EHCVL/IVT_INVQT)L_EHCVL, int((isnull(IVT_EXCVL,0)*100)/isnull(IVT_ASSVL,1)) L_EXCRT1 FROM MR_IVTR1 WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='04' AND IVT_INVNO ='"+P_strINVRF+"' AND isnull(IVT_STSFL,'') <>'X'";
		  else
		        M_strSQLQRY = "SELECT IVT_PRDTP,IVT_PRDCD,IVT_PRDDS,IVT_PKGTP,IVT_PKGWT,IVT_PKGTP,IVT_INVRT,(IVT_INVQT - isnull(IVT_SALQT,0)) L_AVLQT,(DOT_DORQT - isnull(DOT_INVQT,0)) L_INVQT ,IVT_ASSVL,((DOT_DORQT - isnull(DOT_INVQT,0)) * IVT_INVRT)IVT_BASVL,(IVT_CVDVL/IVT_INVQT)L_CVDPR,(IVT_ACVVL/IVT_INVQT)L_ACVPR,(IVT_EXCVL/IVT_INVQT)L_EXCPR,(IVT_EDCVL/IVT_INVQT)L_EDCPR,(IVT_EHCVL/IVT_INVQT)L_EHCPR,(IVT_CVDVL*(DOT_DORQT - isnull(DOT_INVQT,0))/IVT_INVQT)L_CVDVL,(IVT_ACVVL*(DOT_DORQT - isnull(DOT_INVQT,0))/IVT_INVQT)L_ACVVL,(IVT_EXCVL*(DOT_DORQT - isnull(DOT_INVQT,0))/IVT_INVQT)L_EXCVL,(IVT_EDCVL*(DOT_DORQT - isnull(DOT_INVQT,0))/IVT_INVQT)L_EDCVL,(IVT_EHCVL*(DOT_DORQT - isnull(DOT_INVQT,0))/IVT_INVQT)L_EHCVL, int((isnull(IVT_ASSVL,0)*100)/isnull(IVT_EXCVL,1)) L_EXCRT1 FROM MR_IVTR1,MR_DOTRN, isnull(IVT_ASSVL,0)/isnull(IVT_EXCVL,1) L_EXCRT WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_CMPCD = DOT_CMPCD and IVT_SALTP ='04' and IVT_PRDCD = DOT_PRDCD AND IVT_PKGTP = DOT_PKGTP AND DOT_DORNO = '"+txtDORNO.getText().trim() +"' AND DOT_STSFL ='1' AND IVT_INVNO ='"+P_strINVRF+"' AND isnull(IVT_STSFL,'') <>'X'";

	        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	        System.out.println(M_strSQLQRY);
	        int i=0;
	        i = P_intROWNO;
	        if(M_rstRSSET !=null)
	        while(M_rstRSSET.next())
	        {
	        	tblIVTRN.setValueAt(P_strINVRF,i,TBL_INVNO);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_PRDTP"),""),i,TBL_PRDTP);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),i,TBL_PRDDS);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_PKGTP"),""),i,TBL_PKGTP);
	            tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_PKGWT"),i,TBL_PKGWT);
	            //System.out.println("2");
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),""),i,TBL_PRDCD);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_AVLQT"),""),i,TBL_AVLQT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_CVDPR"),"0"),i,TBL_CVDPR);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_ACVPR"),"0"),i,TBL_ACVPR);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_EXCPR"),"0"),i,TBL_EXCRT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_EDCPR"),"0"),i,TBL_EDCRT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_EHCPR"),"0"),i,TBL_EHCRT);

	            //System.out.println("3");
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("IVT_INVRT"),2),i,TBL_BASRT);
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("IVT_BASVL"),2),i,TBL_BASVL);
				//   tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("IVT_EXCVL"),2),i,TBL_EXCVL);
				//   tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("IVT_EDCVL"),2),i,TBL_EDCVL);
				//   tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("IVT_EHCVL"),2),i,TBL_EHCVL);
	            //	System.out.println("4");
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_INVQT"),"0"),i,TBL_INVQT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INVRT"),"0"),i,TBL_INVRT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_BASVL"),"0"),i,TBL_ASSVL);
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("L_EXCVL"),2),i,TBL_EX1VL);
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("L_CVDVL"),2),i,TBL_CVDVL);
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("L_ACVVL"),2),i,TBL_ACVVL);
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("L_EDCVL"),2),i,TBL_ED1VL);
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("L_EHCVL"),2),i,TBL_EH1VL);
	            tblIVTRN.setValueAt(setNumberFormat(M_rstRSSET.getDouble("L_EXCRT1"),2),i,TBL_EXCRT1);

//                  tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_EXCVL"),"0"),i,TBL_EX1VL);
//			tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_CVDVL"),"0"),i,TBL_CVDVL);
//			tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_ACVVL"),"0"),i,TBL_ACVVL);
//
//	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_EDCVL"),"0"),i,TBL_ED1VL);
//	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_EHCVL"),"0"),i,TBL_EH1VL);
	            //System.out.println("5");
	            L_dblNETVL = Double.parseDouble(tblIVTRN.getValueAt(i,TBL_ASSVL).toString());
	            L_dblNETVL += Double.parseDouble(tblIVTRN.getValueAt(i,TBL_CVDVL).toString());
	            L_dblNETVL += Double.parseDouble(tblIVTRN.getValueAt(i,TBL_ACVVL).toString());
	            L_dblNETVL += Double.parseDouble(tblIVTRN.getValueAt(i,TBL_EX1VL).toString());
	            L_dblNETVL +=Double.parseDouble(tblIVTRN.getValueAt(i,TBL_ED1VL).toString());
	            L_dblNETVL +=Double.parseDouble(tblIVTRN.getValueAt(i,TBL_EH1VL).toString());
	            L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(i,TBL_STXVL).toString(),"0"));
	            L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(i,TBL_SCHVL).toString(),"0"));

	            L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(i,TBL_FRTVL).toString(),"0"));
	            L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(i,TBL_DSCVL).toString(),"0"));

	            tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),i,TBL_NETVL);
	            i++;
	        }
	        intROWNO =i;
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getINVDT");
	    }
	}
	void getDATA()
	{
	  try
	  {
	    java.sql.Date L_datTMPDT;
	    java.sql.Timestamp L_tmsTMPDT;
	    M_strSQLQRY = "SELECT * from MR_IVTRN WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO ='"+txtINVNO.getText().trim() +"'";
	    //System.out.println(M_strSQLQRY);
	    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	    //System.out.println(M_rstRSSET);
		int i=0;
	    String L_strINVNO = txtINVNO.getText().trim().toUpperCase();
	    clrCOMP();
	    tblIVTRN.clrTABLE();
	    txtINVNO.setText(L_strINVNO);
	    if(M_rstRSSET !=null)
	    {
			//System.out.println("M_rstRSSET not null");
            while(M_rstRSSET.next())
	        {
				//System.out.println("M_rstRSSET.next");
	            if(i ==0)
	            {
    	            //txtINVRF.setText(M_rstRSSET.getString("IVT_CSIRF"));
    	            txtCNSCD.setText(M_rstRSSET.getString("IVT_CNSCD"));
    	            txtCNSNM.setText(M_rstRSSET.getString("IVT_CNSDS"));
			if(txtCNSNM.getText().length() ==0)
    	            txtCNSNM.setText(getPRTDS("C",M_rstRSSET.getString("IVT_CNSCD")));
    	            txtDSRCD.setText(M_rstRSSET.getString("IVT_DSRCD"));
		      txtDSRNM.setText(getPRTDS(M_rstRSSET.getString("IVT_DSRTP"),M_rstRSSET.getString("IVT_DSRCD")));
    	            txtBYRCD.setText(M_rstRSSET.getString("IVT_BYRCD"));
    	            txtBYRNM.setText(getPRTDS("C",M_rstRSSET.getString("IVT_BYRCD")));
    	            txtTRPCD.setText(M_rstRSSET.getString("IVT_TRPCD"));
		      txtTRPNM.setText(getPRTDS("T",M_rstRSSET.getString("IVT_TRPCD")));
    	            txtLRYNO.setText(M_rstRSSET.getString("IVT_LRYNO"));
    	            txtPORNO.setText(M_rstRSSET.getString("IVT_PORNO"));
    	            txtDORNO.setText(M_rstRSSET.getString("IVT_DORNO"));
    	            txtINDNO.setText(M_rstRSSET.getString("IVT_INDNO"));
    	            txtTMOCD.setText(M_rstRSSET.getString("IVT_TMOCD"));
		      txtDLCCD.setText(M_rstRSSET.getString("IVT_DSTDS"));
    	            txtCPTVL.setText(M_rstRSSET.getString("IVT_CPTVL"));
    	            txtLRNO.setText(M_rstRSSET.getString("IVT_LR_NO"));
    	            lblINVVL.setText(M_rstRSSET.getString("IVT_INVVL"));
		      L_datTMPDT = M_rstRSSET.getDate("IVT_PORDT");
					if(L_datTMPDT !=null)
					{
						txtPORDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					}
			        L_datTMPDT = M_rstRSSET.getDate("IVT_LR_DT");
					if(L_datTMPDT !=null)
					{
						txtLRDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					}
					L_tmsTMPDT = M_rstRSSET.getTimestamp("IVT_INVDT");
					if(L_tmsTMPDT !=null)
					{
						txtINVDT.setText(M_fmtLCDTM.format(L_tmsTMPDT).substring(0,10));
						txtINVTM.setText(M_fmtLCDTM.format(L_tmsTMPDT).substring(11));
					}
	            }
	            tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_CSIRF"),i,TBL_INVNO);
		      tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_PRDTP"),i,TBL_PRDTP);
	            tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_PRDCD"),i,TBL_PRDCD);
	            tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_PRDDS"),i,TBL_PRDDS);
	            tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_PKGTP"),i,TBL_PKGTP);
	            tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_PKGWT"),i,TBL_PKGWT);
			tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_INVQT"),i,TBL_INVQT);
	            tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_INVPK"),i,TBL_INVPK);
			tblIVTRN.setValueAt(M_rstRSSET.getString("IVT_INVRT"),i,TBL_INVRT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),"0"),i,TBL_ASSVL);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_EXCRT"),"0"),i,TBL_EXCRT);
			tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_EDCRT"),"0"),i,TBL_EDCRT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_EHCRT"),"0"),i,TBL_EHCRT);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_SCHRT"),"0"),i,TBL_SCHVL);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_EXCVL"),"0"),i,TBL_EX1VL);

	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_CVDVL"),"0"),i,TBL_CVDVL);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_ACVVL"),"0"),i,TBL_ACVVL);

	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_EDCVL"),"0"),i,TBL_ED1VL);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_EHCVL"),"0"),i,TBL_EH1VL);
			//System.out.println("d ");	            
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_DSCVL"),"0"),i,TBL_DSCVL);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_FRTVL"),"0"),i,TBL_FRTVL);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_STXVL"),"0"),i,TBL_STXVL);
	            tblIVTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_NETVL"),"0"),i,TBL_NETVL);
			//System.out.println("i "+i);
	            i++;
	        }
	        M_rstRSSET.close();
	    }
	        M_strSQLQRY = "SELECT * from MR_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='IV' AND RM_DOCNO ='"+txtINVNO.getText().trim() +"'";
	        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    	    if(M_rstRSSET !=null)
    	    {
                if(M_rstRSSET.next())
                    txtREMDS.setText(M_rstRSSET.getString("RM_REMDS"));
                M_rstRSSET.close();    
    	    }
	    }
	    catch(Exception L_E)
	    {
	       setMSG(L_E,"getDATA");
	    }
	        
	}
	
	
	// iVT_INVDT,IVT_LR_DT,IVT_PORDT,IVT_INVVL
                    
    boolean vldDATA()
	{
	    if(txtINVNO.getText().length() !=4)
	    {
	        setMSG("Enter 4 digit Invoice No. ..",'E');
	        return false;
	    }
		//System.out.println("strINVPR"+strINVPR);
		if(strINVPR.length() != 4)
	    {
	        setMSG("Error in getting Invoice prefix. ..",'E');
	        return false;
	    }
	    if(txtDSRCD.getText().length() ==0)
	    {
	        setMSG("Enter Distributor Code ..",'E');
	        return false;
	    }
	    if(txtBYRCD.getText().length() ==0)
	    {
	        setMSG("Enter Buyer Code ..",'E');
	        return false;
	    }
	    if(txtCNSCD.getText().length() ==0)
	    {
	        setMSG("Enter Consignee Code ..",'E');
	        return false;
	    }
	    if(txtINVDT.getText().length() ==0)
	    {
	        setMSG("Enter Invoice Date ..",'E');
	        return false;
	    }
	    if(txtINVTM.getText().length() ==0)
	    {
	        setMSG("Enter Invoice Time ..",'E');
	        return false;
	    }
	    if(txtINVRF.getText().length() ==0)
	    {
	        setMSG("Enter Invoice reference ..",'E');
	        return false;
	    }
	    if(txtDLCCD.getText().length() ==0)
	    {
	        setMSG("Enter Destination ..",'E');
	        return false;
	    }
	    if(txtTMOCD.getText().length() ==0)
	    {
	        setMSG("Enter Mode of Transport ..",'E');
	        return false;
	    }
	    if(txtCPTVL.getText().length() ==0)
	    {
	        setMSG("Enter Payment Due days ..",'E');
	        return false;
	    }
		if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strTFRTP))
		{
			if(txtCS2CD.getText().length() == 0)
			{
				setMSG("Enter Consignment stockist code for stock Transfer ..",'E');
				txtCS2CD.requestFocus();
			      return false;
			}	
		}
		String L_strCURVL ="";

	    for(int i=0;i<tblIVTRN.getRowCount();i++)
	    {
		
	        if(tblIVTRN.getValueAt(i,TBL_CHKFL).toString().equals("true"))
	        {
		      L_strCURVL =tblIVTRN.getValueAt(i,TBL_INVNO).toString()+tblIVTRN.getValueAt(i,TBL_PRDCD).toString().trim();
			if(tblIVTRN.getValueAt(i,TBL_PRDDS).toString().length() ==0)
	            {
	                setMSG("Enter Garde ",'E');
	                return false;
	            }
	            if(tblIVTRN.getValueAt(i,TBL_PRDCD).toString().length() ==0)
	            {
	                setMSG("Product Code can not be blank ",'E');
	                return false;
	            }
	            if(tblIVTRN.getValueAt(i,TBL_INVQT).toString().length() ==0)
	            {
	                setMSG("Enter Invoice Qty ",'E');
	                return false;
	            }
	            if(tblIVTRN.getValueAt(i,TBL_INVRT).toString().length() ==0)
	            {
	                setMSG("Enter Invoice Rate ",'E');
	                return false;
	            }
			for(int j=0;j<i-1;j++)
			{
				if(tblIVTRN.getValueAt(j,TBL_CHKFL).toString().equals("true"))
				{
					//System.out.println("i ="+i +" "+L_strCURVL);
					System.out.println(tblIVTRN.getValueAt(j,TBL_INVNO).toString()+tblIVTRN.getValueAt(j,TBL_PRDCD).toString().trim());
					if((tblIVTRN.getValueAt(j,TBL_INVNO).toString()+tblIVTRN.getValueAt(j,TBL_PRDCD).toString().trim()).equals(L_strCURVL.trim()))
					{
						setMSG("Duplicate Grade, Please generate separte Invoices..",'E');
						return false;
					}
				}
			}
	        }

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
    	  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
    	  {
    	        M_strSQLQRY="DELETE FROM MR_IVTRN WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='14' AND IVT_INVNO = '"+txtINVNO.getText().trim() +"'";
    	        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	        if(cl_dat.exeDBCMT("exeSAVE"))
        	    {
        	        setMSG("Data Saved",'N');
        	        intROWNO =0;
        	        clrCOMP();
        	    }
        	    else
        	        setMSG("Error in Saving",'E');
    	  }
    	  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
    	  {
        	    //strINVNO = genDOCNO();
		   String L_strSALTP ="";
		   if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP))
				L_strSALTP =strSALTP;
		   else if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strTFRTP))
				L_strSALTP =strTFRTP;
					
        	    strINVNO = txtINVNO.getText().trim().toUpperCase();
        	    /*if(strINVNO ==null)
        	    {
        	        setMSG("Error in generating Invoice No.",'E');
        	        return;
        	    }
        	    if(strINVNO.length() == 0)
        	    {
        	        setMSG("Error in generating Invoice No.",'E');
        	        return;     
        	    }*/
        	    String L_strINVDT =M_fmtDBDTM.format(M_fmtLCDTM.parse(txtINVDT.getText()+" "+txtINVTM.getText()));
        	    double L_dblINVVL =0;
        	    M_calLOCAL.setTime(M_fmtLCDAT.parse(txtINVDT.getText()));
                M_calLOCAL.add(Calendar.DATE,Integer.parseInt(nvlSTRVL(txtCPTVL.getText(),"0")));
                String L_strPMDDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
                for(int i=0;i<tblIVTRN.getRowCount();i++)
        	    {
        	        if(tblIVTRN.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            L_dblINVVL += Double.parseDouble(tblIVTRN.getValueAt(i,TBL_NETVL).toString());
        	        }
        	    }
                for(int i=0;i<tblIVTRN.getRowCount();i++)
        	    {
        	        /*if(tblIVTRN.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            L_dblINVVL += Double.parseDouble(tblIVTRN.getValueAt(i,TBL_NETVL).toString());
        	        }*/
        	        if(tblIVTRN.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            M_strSQLQRY = "insert into mr_ivtrn(IVT_CMPCD,IVT_MKTTP,IVT_LADNO,IVT_INVNO,IVT_CSIRF,IVT_INVDT,"
                	    +"IVT_SALTP,IVT_ZONCD,IVT_CNSCD,IVT_CNSDS,IVT_BYRCD,IVT_DSRTP,IVT_DSRCD,IVT_CURCD,IVT_ECHRT,"
                	    +"IVT_LRYNO,IVT_LR_NO,IVT_LR_DT,IVT_TRPCD,IVT_PORNO,IVT_PORDT,IVT_DSTDS,IVT_TMOCD,IVT_CPTVL,"
                        +"IVT_PMDDT,IVT_PRDTP,IVT_PRDCD,IVT_PRDDS,IVT_PKGTP,IVT_PKGWT,IVT_INVQT,IVT_INVPK,IVT_INVRT,"
                        +"IVT_ASSVL,IVT_EXCRT,IVT_EXCVL,IVT_CVDVL,IVT_ACVVL,IVT_EDCRT,IVT_EDCVL,IVT_EHCVL,IVT_STXRT,IVT_STXVL,IVT_SCHRT,IVT_DSCVL,IVT_FRTVL,"
                        +"IVT_INVVL,IVT_NETVL,IVT_SBSCD,IVT_SBSCD1,IVT_EPIFL,IVT_TRNFL,IVT_STSFL,IVT_LUSBY,IVT_LUPDT) VALUES("
				+"'"+cl_dat.M_strCMPCD_pbst+"',"
				+"'"+strMKTTP+"',"
                        +"'"+strINVPR+strINVNO+"',"
                        +"'"+strINVPR+strINVNO+"',"
                        +"'"+tblIVTRN.getValueAt(i,TBL_INVNO).toString()+"',"
                        +"'"+L_strINVDT+"',"
                        +"'"+L_strSALTP+"',"
                        +"'"+M_strSBSCD.substring(0,2)+"',"
                        +"'"+txtCNSCD.getText().trim()+"',"
                        +"'"+txtCNSNM.getText().trim()+"',"
                        +"'"+txtBYRCD.getText().trim()+"','G',"
                        +"'"+txtDSRCD.getText().trim()+"',"
                        +"'01',1,"
                        +"'"+txtLRYNO.getText().trim()+"',"
                        +"'"+txtLRNO.getText().trim()+"',";
                        if(txtLRDT.getText().length() >0) 
                            M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtLRDT.getText().trim()))+"',";
                        else
                             M_strSQLQRY += "null,";
                        M_strSQLQRY += "'"+txtTRPCD.getText().trim()+"',";
                       M_strSQLQRY += "'"+txtPORNO.getText().trim()+"',";
                       if(txtPORDT.getText().length() >0) 
                            M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPORDT.getText().trim()))+"',";
                        else
                           M_strSQLQRY += "null,";
                        if(lblDLCDS.getText().trim().length() ==0)   
                            M_strSQLQRY += "'"+txtDLCCD.getText().trim()+"',";
                        else
                            M_strSQLQRY += "'"+lblDLCDS.getText().trim()+"',";
                        M_strSQLQRY += "'"+txtTMOCD.getText().trim()+"',";
                        M_strSQLQRY += nvlSTRVL(txtCPTVL.getText(),"0")+",";
                        if(L_strPMDDT.length() >0) 
                            M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strPMDDT))+"',";
                        else
                           M_strSQLQRY += "null,";
                       
                        M_strSQLQRY +="'"+tblIVTRN.getValueAt(i,TBL_PRDTP).toString()+"',";
                        M_strSQLQRY +="'"+tblIVTRN.getValueAt(i,TBL_PRDCD).toString()+"',";
                        M_strSQLQRY +="'"+tblIVTRN.getValueAt(i,TBL_PRDDS).toString()+"',";
                        M_strSQLQRY +="'"+tblIVTRN.getValueAt(i,TBL_PKGTP).toString()+"',";
                        M_strSQLQRY +=tblIVTRN.getValueAt(i,TBL_PKGWT).toString()+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_INVQT).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_INVPK).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_INVRT).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_ASSVL).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_EXCRT).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_EX1VL).toString(),"0")+",";

                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_CVDVL).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_ACVVL).toString(),"0")+",";

                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_EDCRT).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_ED1VL).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_EH1VL).toString(),"0")+",";
                        M_strSQLQRY +=strSTXRT+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_STXVL).toString(),"0")+",";
                     //    M_strSQLQRY +=strSCHRT+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_SCHVL).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_DSCVL).toString(),"0")+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_FRTVL).toString(),"0")+",";

                        M_strSQLQRY +=setNumberFormat(L_dblINVVL,0)+",";
                        M_strSQLQRY +=nvlSTRVL(tblIVTRN.getValueAt(i,TBL_NETVL).toString(),"0")+",";
                        M_strSQLQRY +="'"+M_strSBSCD+"',";
						M_strSQLQRY +="'"+M_strSBSCD.substring(0,2)+getPRDTP(tblIVTRN.getValueAt(i,TBL_PRDCD).toString())+"00','',";
                        M_strSQLQRY += getUSGDTL("IVT",'I',"")+")";
                        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(txtDORNO.getText().trim().length() >0)
				{
					M_strSQLQRY = "update MR_DOTRN set DOT_TRNFL ='0', DOT_INVQT =  isnull(DOT_INVQT,0) + "+ nvlSTRVL(tblIVTRN.getValueAt(i,TBL_INVQT).toString(),"0") ;
					M_strSQLQRY += " where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"+strMKTTP +"' and DOT_DORNO = '"+txtDORNO.getText().trim() +"' and dot_prdcd ='" +tblIVTRN.getValueAt(i,TBL_PRDCD).toString()+"' AND DOT_PKGTP ='"+tblIVTRN.getValueAt(i,TBL_PKGTP).toString()+"'" ;
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}

        	        }
        	        
        	    }
        	    M_strSQLQRY="Insert into MR_RMMST (RM_CMPCD,RM_MKTTP,RM_TRNTP,RM_DOCNO,RM_REMDS,RM_SBSCD,RM_SBSCD1,RM_TRNFL,RM_STSFL,RM_LUSBY,RM_LUPDT) values ("
    			+ "'"+cl_dat.M_strCMPCD_pbst+"',"
				+ "'"+strMKTTP+"','IV',"
    			+ "'"+strINVPR+strINVNO+"',"
    			+ "'"+txtREMDS.getText().trim() +"',"
    			+ "'"+M_strSBSCD +"',"
				+ "'"+M_strSBSCD.substring(0,2)+"XX00',"
    			+ getUSGDTL("RM",'I',"")+")";
    			if(txtREMDS.getText().length() >0)
    			    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        	///    updINVNO(txtINVNO.getText().trim());
        	    if(cl_dat.exeDBCMT("exeSAVE"))
        	    {
        	        setMSG("Data Saved",'N');
        	      	//cstPTTRN_INV.setString(1,cl_dat.M_strCMPCD_pbst);
    				//cstPTTRN_INV.setString(2,strMKTTP);
    				//cstPTTRN_INV.setString(3,strINVPR+txtINVNO.getText());
    				//cstPTTRN_INV.executeUpdate();
    				//cl_dat.M_conSPDBA_pbst.commit();
					exeCNTRN("03", strINVPR+strINVNO);
        	      	//cstPLTRN_INV.setString(1,cl_dat.M_strCMPCD_pbst);
    				//cstPLTRN_INV.setString(2,strMKTTP);
    				//cstPLTRN_INV.setString(3,strINVPR+txtINVNO.getText());
    				//cstPLTRN_INV.executeUpdate();
    				//cl_dat.M_conSPDBA_pbst.commit();
        		  if(cl_dat.M_flgLCUPD_pbst)
				  {
					if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strTFRTP))
					{
						cstIVTRN_TRF.setString(1,cl_dat.M_strCMPCD_pbst);
		    				cstIVTRN_TRF.setString(2,strMKTTP);
    						cstIVTRN_TRF.setString(3,strINVPR+txtINVNO.getText());
    						cstIVTRN_TRF.setString(4,txtCS2CD.getText());
    						cstIVTRN_TRF.executeUpdate();
    						cl_dat.M_conSPDBA_pbst.commit();
					}
						JOptionPane.showMessageDialog(this,"Invoice No is generated as :  "+strINVPR +txtINVNO.getText().trim(),"Message",JOptionPane.INFORMATION_MESSAGE);
				  } 
    			
					clrCOMP();
        	    }
        	    else
        	        setMSG("Error in Saving",'E');
        	    //,IVT_TMOCD,IVT_CPTVL,IVT_DTPCD,IVT_LODDT,IVT_STXCD
                //IVT_DSCVL,IVT_PKFVL,IVT_INSVL,IVT_LADQT,IVT_REQQT,IVT_AUTBY
                // IVT_COMVL,IVT_PMDDT,IVT_CPRVL
            /* from SPLDATA.mr_ivtrn where 
            ivt_mkttp = new_row.ivt_mkttp and ivt_ladno = new_row.ivt_ladno and 
            ivt_prdcd = new_row.ivt_prdcd and ivt_pkgtp = new_row.ivt_pkgtp;*/
            }
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"exeSAVE");    
	    }
	    
	}
	double calINVVL()
	{
	    double L_dblINVVL = 0 ;
	    for(int i=0;i<tblIVTRN.getRowCount();i++)
	    {
	        if(tblIVTRN.getValueAt(i,TBL_CHKFL).toString().equals("true"))
	        {
	            L_dblINVVL += Double.parseDouble(tblIVTRN.getValueAt(i,TBL_NETVL).toString());
	        }
	    }
	    lblINVVL.setText(setNumberFormat(L_dblINVVL,0));
	    return L_dblINVVL;
	}
	private String genDOCNO()
    {
	    strINVNO ="";
		String L_INVNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for SAN;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MRXXINV' and ";
	//		M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strSALTP +"'";
			M_strSQLQRY += " CMT_CODCD = '" + strYRDGT + strSALTP +"'";

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
			M_strSQLQRY += " and CMT_CGSTP = 'MRXXINV'";	
			//M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strSALTP + "'";			
			M_strSQLQRY += " and CMT_CODCD = '" + strYRDGT + strSALTP + "'";			

			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY," ");
			if(cl_dat.exeDBCMT("genDOCNO"))
		   {
				L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
				for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
					L_INVNO += "0";
			
				L_CCSVL = L_INVNO + L_CCSVL;
				L_INVNO = L_CODCD + L_CCSVL;
				txtINVNO.setText(L_INVNO);
				strINVNO = L_INVNO;
			}
			else return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genDOCNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_INVNO;
	}
	// Method to update the last Issue No.in the CO_CDTRN
	private void updINVNO(String P_strINVNO)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + P_strINVNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP ='MRXXINV'";
	//		M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strSALTP +"'";			
			M_strSQLQRY += " and CMT_CODCD = '" + strYRDGT + strSALTP +"'";			

			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"updINVNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

private class TBLINVFR extends TableInputVerifier
{
	
	public boolean verify(int P_intROWID,int P_intCOLID)
		{
			String L_strTEMP ="";
			String strTEMP ="";
			double L_dblNETVL=0;
			double L_dblEXCVL=0;
			double L_dblCESVL=0;
			double L_dblHCESVL=0;
			double L_dblCVDVL=0;
			double L_dblACVVL=0;

			double L_dblINVVL=0;
			double L_dblINVPK;
			//int L_intEXCRT=14;
			try
			{
				if(P_intCOLID==TBL_INVQT)
				{
					strTEMP = tblIVTRN.getValueAt(P_intROWID,TBL_INVQT).toString();
		    		if(strTEMP.length()>0)
					{
						if(Double.parseDouble(strTEMP) <=0)
						{
							setMSG("Invoice qty. can not be negative..",'E');
							return false;
						}
						if(Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_AVLQT).toString()) < Math.abs(Double.parseDouble(strTEMP)))
					    {
							setMSG("Invoice qty. can not be greater than available qty..",'E');
							return false;
					    }
					    tblIVTRN.setValueAt(setNumberFormat(Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_INVRT).toString())* Double.parseDouble(strTEMP),2),P_intROWID,TBL_ASSVL);

					   L_dblCVDVL = Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_CVDPR).toString())*Double.parseDouble(strTEMP); 
					   L_dblACVVL = Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_ACVPR).toString())*Double.parseDouble(strTEMP);  
		
					    if(Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString()) == 0)
			    		    {	 
						   L_dblEXCVL = Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_BASRT).toString())* Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_INVQT).toString()) * (Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_EXCRT1).toString())/100); 
						   L_dblCESVL = L_dblEXCVL * 2/100;
						   L_dblHCESVL = L_dblEXCVL * 1/100;  
					    }
					    else	
					    {	
					    	L_dblEXCVL = 0;
						L_dblCESVL = Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_EDCRT).toString())*Double.parseDouble(strTEMP); 
					      L_dblHCESVL = Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_EHCRT).toString())*Double.parseDouble(strTEMP);   
					    }
			   		    L_dblINVPK = Double.parseDouble(strTEMP)/Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_PKGWT).toString()); 
					    tblIVTRN.setValueAt(setNumberFormat(L_dblINVPK,0),P_intROWID,TBL_INVPK);
					    // net value
				        tblIVTRN.setValueAt(setNumberFormat(L_dblEXCVL,2),P_intROWID,TBL_EX1VL);
				    
				        tblIVTRN.setValueAt(setNumberFormat(L_dblCVDVL,2),P_intROWID,TBL_CVDVL);
				        tblIVTRN.setValueAt(setNumberFormat(L_dblACVVL,2),P_intROWID,TBL_ACVVL);

				        tblIVTRN.setValueAt(setNumberFormat(L_dblCESVL,2),P_intROWID,TBL_ED1VL);
				        tblIVTRN.setValueAt(setNumberFormat(L_dblHCESVL,2),P_intROWID,TBL_EH1VL);

					L_dblNETVL = Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ASSVL).toString(),"0"));
        	           		L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EX1VL).toString(),"0"));
        	           		L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString(),"0"));
        	           		L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ACVVL).toString(),"0"));
		        	      L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ED1VL).toString(),"0"));
					L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EH1VL).toString(),"0"));
		        	      L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_STXVL).toString(),"0"));
        			      L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_SCHVL).toString(),"0"));
			            L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_DSCVL).toString(),"0"));
	     			      L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_FRTVL).toString(),"0"));        	            
        	                  tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),P_intROWID,TBL_NETVL);
	              	      calINVVL();
					}
				}
				if(P_intCOLID==TBL_INVRT)
				{
					strTEMP = tblIVTRN.getValueAt(P_intROWID,TBL_INVRT).toString();
		    		if(strTEMP.length()>0)
					{
					    tblIVTRN.setValueAt(setNumberFormat(Double.parseDouble(tblIVTRN.getValueAt(P_intROWID,TBL_INVQT).toString())* Double.parseDouble(strTEMP),2),P_intROWID,TBL_ASSVL);
				
					L_dblNETVL = Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ASSVL).toString(),"0"));
				      L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EX1VL).toString(),"0"));
           	           		L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString(),"0"));
        	           		L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ACVVL).toString(),"0"));
					L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ED1VL).toString(),"0"));
				      L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EH1VL).toString(),"0"));
		                  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_STXVL).toString(),"0"));
		      	      L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_SCHVL).toString(),"0"));
	            		L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_DSCVL).toString(),"0"));
		      	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_FRTVL).toString(),"0"));        	            
        	     			tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),P_intROWID,TBL_NETVL);
        	      	       calINVVL();
					}
				}
				if(P_intCOLID==TBL_STXVL)
				{
					
			 	 L_dblNETVL = Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ASSVL).toString(),"0"));
        	        	 L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EX1VL).toString(),"0"));
     	           		L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString(),"0"));
     	           		L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ACVVL).toString(),"0"));
				  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ED1VL).toString(),"0"));
				  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EH1VL).toString(),"0"));
        		        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_STXVL).toString(),"0"));
        	      	  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_SCHVL).toString(),"0"));
		              L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_DSCVL).toString(),"0"));
		     	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_FRTVL).toString(),"0"));        	            
        		        tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),P_intROWID,TBL_NETVL);
        	      	  calINVVL();
				}
				if((P_intCOLID==TBL_ED1VL)||(P_intCOLID==TBL_EH1VL)||(P_intCOLID==TBL_DSCVL)||(P_intCOLID==TBL_FRTVL))
				{
			  L_dblNETVL = Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ASSVL).toString(),"0"));
        	        L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EX1VL).toString(),"0"));
            	  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString(),"0"));
              	  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ACVVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ED1VL).toString(),"0"));
			  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EH1VL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_STXVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_SCHVL).toString(),"0"));
	              L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_DSCVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_FRTVL).toString(),"0"));        	            

			  tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),P_intROWID,TBL_NETVL);
        	        calINVVL();
				}
				if(P_intCOLID==TBL_SCHVL)
				{
				
		    	  L_dblNETVL = Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ASSVL).toString(),"0"));
        	        L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EX1VL).toString(),"0"));
           		  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString(),"0"));
           		  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ACVVL).toString(),"0"));
			  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ED1VL).toString(),"0"));
			  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EH1VL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_STXVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_SCHVL).toString(),"0"));
	              L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_DSCVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_FRTVL).toString(),"0"));        	            

        	            tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),P_intROWID,TBL_NETVL);
        	            calINVVL();
					
				}
				if(P_intCOLID==TBL_CVDVL)
				{
				
		    	  L_dblNETVL = Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ASSVL).toString(),"0"));
        	        L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EX1VL).toString(),"0"));
           		  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString(),"0"));
           		  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ACVVL).toString(),"0"));
			  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ED1VL).toString(),"0"));
			  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EH1VL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_STXVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_SCHVL).toString(),"0"));
	              L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_DSCVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_FRTVL).toString(),"0"));        	            

        	            tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),P_intROWID,TBL_NETVL);
        	            calINVVL();
					
				}

				if(P_intCOLID==TBL_ACVVL)
				{
				
		    	  L_dblNETVL = Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ASSVL).toString(),"0"));
        	        L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EX1VL).toString(),"0"));
           		  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_CVDVL).toString(),"0"));
           		  L_dblNETVL += Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ACVVL).toString(),"0"));
			  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_ED1VL).toString(),"0"));
			  L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_EH1VL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_STXVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_SCHVL).toString(),"0"));
	              L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_DSCVL).toString(),"0"));
        	        L_dblNETVL +=Double.parseDouble(nvlSTRVL(tblIVTRN.getValueAt(P_intROWID,TBL_FRTVL).toString(),"0"));        	            

        	            tblIVTRN.setValueAt(setNumberFormat(L_dblNETVL,2),P_intROWID,TBL_NETVL);
        	            calINVVL();
					
				}

			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				return false;
			}
			return true;
		}
}
private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
{
	//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
    try
    {
	if (LP_FLDTP.equals("C"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
		//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
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
	{setMSG(L_EX,"getRSTVAL");}
return " ";
} 

private class INPVF extends InputVerifier
{
	public boolean verify (JComponent input)
	{
	  	try
		{
	    	      if (input instanceof JTextField&&((JTextField)input).getText().length()==0)	
				return true;
		      if(input==txtDSRCD)
			{
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtDSRCD.getText()+"' and pt_PRTTP='G' and upper(isnull(PT_STSFL,' ')) <> 'X'");
				if(L_rstRSSET!=null)
				{
					if (L_rstRSSET.next())
					{
						txtDSRNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
						strDSRDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
						setMSG("",'N');
						L_rstRSSET.close();
						//System.out.println("valid dist");
					}
					else 
					{	
						setMSG("Invalid Cons. Stockist Code ..",'E');
						return false;
					}
					String L_strTEMP;
					strINVPR ="";
					L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXCST' AND CMT_CODCD = '"+txtDSRCD.getText()+"' AND isnull(CMT_STSFL,'') <>'X'");
					if(L_rstRSSET!=null)
				      {
    						if (L_rstRSSET.next())
    						{
							L_strTEMP = L_rstRSSET.getString("CMT_CHP01");
							strINVPR = strPREFIX + L_strTEMP;
							strBYRCD = nvlSTRVL(L_rstRSSET.getString("CMT_CHP02"),"");
							//System.out.println("strBYRCD "+strBYRCD);
							
						}
						L_rstRSSET.close();
					}
					M_strSQLQRY = "Select * from co_ptmst where PT_PRTTP ='C' AND PT_CNSRF='"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X'";
					if(strBYRCD.length() > 0)
						M_strSQLQRY += " and PT_PRTCD ='"+strBYRCD +"'"; 
					L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
				  	if(L_rstRSSET!=null)
				      {
    						if (L_rstRSSET.next())
    						{
    					    	 	txtBYRCD.setText(getRSTVAL(L_rstRSSET,"PT_PRTCD","C"));
	    						txtBYRNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
    							txtCNSCD.setText(getRSTVAL(L_rstRSSET,"PT_PRTCD","C"));
    							txtCNSNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
    							L_rstRSSET.close();
    						}
					}
				}
				else
				{
					setMSG("Invalid Cons. Stockist Code ..",'E');
					return false;
		
				}
			}
			 else  if(input==txtBYRCD)
			{
					txtBYRCD.setText(txtBYRCD.getText().toUpperCase());
					M_strSQLQRY = "Select * from co_ptmst where pt_prtcd='"+txtBYRCD.getText()+"' and pt_PRTTP='C' and PT_CNSRF = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ";
   				      if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strTFRTP))
				      {
					  M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and upper(isnull(PT_STSFL,' ')) <> 'X'";
				      }	

					if(strBYRCD.trim().length() >0)
						M_strSQLQRY +=" and PT_PRTCD = '"+strBYRCD +"'";
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);				
					if(L_rstRSSET!=null)
					{
						if (L_rstRSSET.next())
						{
							txtBYRNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
							strBYRDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
							if(txtDLCCD.getText().length()==0)
								txtDLCCD.setText(getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
							L_rstRSSET.close();
							/*if(txtCNSCD.getText().length()==0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
							{
								txtCNSCD.setText(txtBYRCD.getText());
								txtCNSNM.setText(txtBYRNM.getText());
							}*/
							setMSG("",'N');
						}
						else 
						{
							setMSG("Invalid Buyer Code ..",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Buyer Code ..",'E');
						return false;
					}
				}
	 	    else if(input==txtCNSCD)
			{
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtCNSCD.getText()+"' and pt_PRTTP='C' and upper(isnull(PT_STSFL,' ')) <> 'X'");
				if(L_rstRSSET!=null)
				{
					if (L_rstRSSET.next())
					{
						txtCNSNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
						strCNSDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
					if(txtDLCCD.getText().length()==0)
						txtDLCCD.setText(getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
						setMSG("",'N');
						L_rstRSSET.close();
					}
					else 
					{
						setMSG("Invalid Consignee Code ..",'E');
						return false;
					}
				}
				else
                {
					setMSG("Invalid Conssignee Code ..",'E');
					return false;
		        }
           	}
			else if(input==txtTRPCD)
			{
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtTRPCD.getText()+"' and pt_PRTTP='T' and upper(isnull(PT_STSFL,' ')) <> 'X'");
				if(L_rstRSSET!=null)
				{
					if (L_rstRSSET.next())
					{
						txtTRPNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
						strTRPDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
								 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
						setMSG("",'N');
						L_rstRSSET.close();
					}
					else 
					{	
						setMSG("Invalid Transporter Code ..",'E');
		    			return false;
				}
				}
				else
				{
					setMSG("Invalid Transporter Code ..",'E');
					return false;
				}
			}
			else if(input==txtDLCCD)
			{
				txtDLCCD.setText(txtDLCCD.getText().toUpperCase());
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_cdtrn where CMT_CGMTP ='SYS' AND cmt_codcd='"+txtDLCCD.getText()+"' and CMT_CGSTP='COXXDST'");
				if(L_rstRSSET!=null)
				{
					if (L_rstRSSET.next())
					{
						lblDLCDS.setText(getRSTVAL(L_rstRSSET,"CMT_CODDS","C"));
						L_rstRSSET.close();
						setMSG("Valid Destination ..",'N');
					}
					else 
					{
						setMSG("Invalid Destination Code ..",'E');
						return false;
					}
				}
				else
				{
					setMSG("Invalid Destination Code ..",'E');
					return false;
				}
			}
			else if(input==txtTMOCD)
			{
				txtTMOCD.setText(txtTMOCD.getText().toUpperCase());
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_cdtrn where CMT_CGMTP ='SYS' AND cmt_codcd='"+txtTMOCD.getText()+"' and CMT_CGSTP='MR01MOT'");
				if(L_rstRSSET!=null)
				{
					if (L_rstRSSET.next())
					{
						lblTMODS.setText(getRSTVAL(L_rstRSSET,"CMT_CODDS","C"));
						L_rstRSSET.close();
						setMSG("Valid Transportation Mode ..",'N');
					}
					else 
					{
						setMSG("Invalid Mode of Transport ..",'E');
						return false;
					}
				}
				else
				{
					setMSG("Invalid Mode of Transport ..",'E');
					return false;
				}
			}

			else if(input==txtINVRF)
			{
			    M_strSQLQRY ="Select IVT_INVNO,IVT_INVDT from MR_IVTR1 where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='04'  and IVT_DSRCD ='"+txtDSRCD.getText().trim()+"'" ;
			    //and ivt_dsrtp ='G'
			    M_strSQLQRY += " AND IVT_INVNO ='"+txtINVRF.getText()+"'";
			    //System.out.println(M_strSQLQRY);
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
			    txtIVRDT.setText("");
			    if (L_rstRSSET != null)
				{
				    if(L_rstRSSET.next())
				    {
				        java.sql.Timestamp tmsTMP = L_rstRSSET.getTimestamp("IVT_INVDT");
				        if(tmsTMP !=null)
				            txtIVRDT.setText(M_fmtLCDTM.format(tmsTMP));
				        else txtIVRDT.setText("");
				            
				    }
				    else
				    {
				        setMSG("Invalid Invoice Ref ..",'E');
				        return false;
				    }
				}  
				else
				{
				    setMSG("Invalid Invoice Ref ..",'E');
				    return false;
				}
			}
			else if(input==txtINVNO)
			{
			    
				 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				 {
					M_strSQLQRY ="Select IVT_INVNO,IVT_INVDT from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='14' and ivt_dsrtp ='G' and IVT_DSRCD ='"+txtDSRCD.getText().trim()+"'" ;
					M_strSQLQRY += " AND IVT_INVNO ='"+strINVPR + txtINVNO.getText()+"'";
				 }
				 else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				 {
					 M_strSQLQRY ="Select IVT_INVNO,IVT_INVDT from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='14' ";
					 M_strSQLQRY += " AND IVT_INVNO ='"+txtINVNO.getText()+"'";
				 }
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
			     if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			        txtINVDT.setText("");
			    if (L_rstRSSET != null)
				{
				    if(L_rstRSSET.next())
				    {
				        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			            {
			                setMSG("Invoice Already exists ..",'E');
			                return false;
			            }
			            
				        /*java.sql.TimeStamp tmsTMP = L_rstRSSET.getTimeStamp("IVT_INVDT");
				        if(tmsTEMP !=null)
				        {
				            txtINVDT.setText(M_fmtLCDTM.format(tmsTEMP).substring(0,10));
				             txtINVTM.setText(M_fmtLCDTM.format(tmsTEMP).substring(11));
				        }
				        else 
				        {
				            txtINVDT.setText("");
				        }*/
				    }
				    else
				    {
				        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				            return true;
				        setMSG("Invalid Invoice Ref ..",'E');
				        return false;
				    }
				}
				else
				{
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				        return true;
				    setMSG("Invalid Invoice Ref ..",'E');
				    return false;
				}
			}
			else if(input==txtLRDT)
			{
			    if(M_fmtLCDAT.parse(txtLRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Date can not be greater than the current date..",'E');
					return false;
				}
			}
			else if(input==txtPORDT)
			{
			    if(M_fmtLCDAT.parse(txtPORDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Date can not be greater than the current date..",'E');
					return false;
				}
			}
			else if(input==txtINVDT)
			{
			    if(M_fmtLCDAT.parse(txtINVDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Date can not be greater than the current date..",'E');
					return false;
				}
			}
			else if(input==txtINVTM)
			{
			    if(txtINVDT.getText().length() == 10)
			    if(M_fmtLCDTM.parse(txtINVDT.getText()+" "+txtINVTM.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Invoice Date Time can not be greater than the current date time..",'E');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
		    setMSG(L_E,"Verify");
		}
		return true;
	}
}
private String getPRTDS(String P_strPRTTP,String P_strPRTCD)
{
	String L_strPRTDS = "";
	ResultSet rstRSSET;
	try{
		M_strSQLQRY = "Select PT_PRTNM from CO_PTMST ";
		M_strSQLQRY += " where PT_PRTTP = '" + P_strPRTTP + "'";
		M_strSQLQRY += " and PT_PRTCD = '" + P_strPRTCD + "'";
		
		rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(rstRSSET !=null)
		if(rstRSSET.next())
		{
			L_strPRTDS = rstRSSET.getString("PT_PRTNM");
			rstRSSET.close();
		}
	}
	catch(Exception e){
		setMSG(e,"getPRTDS ");
	}
	return L_strPRTDS;
}

	/** Method for creating Credit Note Transactions against
	 * the Gate-in Number (This is applicable for Sales Invoice Gate-out approval)
	 */
	public void exeCNTRN(String LP_DOCTP,String LP_DOCNO)
	{
		
		try
		{
			if(!LP_DOCTP.equals("03"))
				return;
	
			//M_strSQLQRY = "select distinct ivt_mkttp, ivt_invno from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ginno = '"+LP_DOCNO+"' and isnull(ivt_invqt,0)>0 and ivt_stsfl<>'X' and ivt_mkttp in ('01','04','05') and ivt_saltp not in ('04','05','16','21') order by ivt_mkttp, ivt_invno";
			M_strSQLQRY = "select distinct ivt_mkttp, ivt_invno from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_invno = '"+LP_DOCNO+"' and isnull(ivt_invqt,0)>0 and ivt_stsfl<>'X' and ivt_mkttp in ('01','04','05') and ivt_saltp not in ('04','05','16','21') order by ivt_mkttp, ivt_invno";
			//System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = null;
				
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			String L_strMKTTP, L_strINVNO;
			while(true)
			{
				L_strMKTTP = getRSTVAL(L_rstRSSET,"IVT_MKTTP","C");
				L_strINVNO = getRSTVAL(L_rstRSSET,"IVT_INVNO","C");
				//System.out.println(L_strMKTTP+"/"+L_strINVNO);

/**				cstDSHINV = cl_dat.M_conSPDBA_pbst.prepareCall("call setDSHINV(?,?)");
				System.out.println("6");
				cstDSHINV.setString(1,cl_dat.M_strCMPCD_pbst);
				System.out.println("7");
				cstDSHINV.setString(2,L_strINVNO);
				System.out.println("8");
				cstDSHINV.executeUpdate();
				System.out.println("9");
				cl_dat.M_conSPDBA_pbst.commit();
				System.out.println("10");*/

				
				cstPTTRN_INV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPTTRN_INV(?,?,?)");
				cstPTTRN_INV.setString(1,cl_dat.M_strCMPCD_pbst);
				cstPTTRN_INV.setString(2,L_strMKTTP);
				cstPTTRN_INV.setString(3,L_strINVNO);
				cstPTTRN_INV.executeUpdate();
				cl_dat.M_conSPDBA_pbst.commit();

				cstPLTRN_INV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_INV(?,?,?)");
				cstPLTRN_INV.setString(1,cl_dat.M_strCMPCD_pbst);
				cstPLTRN_INV.setString(2,L_strMKTTP);
				cstPLTRN_INV.setString(3,L_strINVNO);
				cstPLTRN_INV.executeUpdate();
				cl_dat.M_conSPDBA_pbst.commit();

/**				cstPLTRN_PTT = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_PTT(?,?,?)");
			System.out.println("23");
				cstPLTRN_PTT.setString(1,cl_dat.M_strCMPCD_pbst);
			System.out.println("24");
				cstPLTRN_PTT.setString(2,L_strMKTTP);
			System.out.println("25");
				cstPLTRN_PTT.setString(3,L_strINVNO);
			System.out.println("26");
				cstPLTRN_PTT.executeUpdate();
			System.out.println("27");
				cl_dat.M_conSPDBA_pbst.commit();*/

/**				cstPATRN_PTT = cl_dat.M_conSPDBA_pbst.prepareCall("call updPATRN_PTT(?,?,?)");
			System.out.println("28");
				cstPATRN_PTT.setString(1,cl_dat.M_strCMPCD_pbst);
			System.out.println("29");
				cstPATRN_PTT.setString(2,L_strMKTTP);
			System.out.println("30");
				cstPATRN_PTT.setString(3,L_strINVNO);
			System.out.println("31");
				cstPATRN_PTT.executeUpdate();
			System.out.println("32");
				cl_dat.M_conSPDBA_pbst.commit();
			System.out.println("33");*/
				
/**				cstDSPQT_RES = cl_dat.M_conSPDBA_pbst.prepareCall("call setDSPQT_RES(?,?,?)");
			System.out.println("34");
				cstDSPQT_RES.setString(1,cl_dat.M_strCMPCD_pbst);
			System.out.println("35");
				cstDSPQT_RES.setString(2,L_strMKTTP);
			System.out.println("36");
				cstDSPQT_RES.setString(3,L_strINVNO);
			System.out.println("37");
				cstDSPQT_RES.executeUpdate();
			System.out.println("38");
				cl_dat.M_conSPDBA_pbst.commit();
			System.out.println("39");*/

				//cstPATRN_ADV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPATRN_ADV(?,?,?)");
				//cstPATRN_ADV.setString(1,"01");
				//cstPATRN_ADV.setString(2,L_strMKTTP);
				//cstPATRN_ADV.setString(3,L_strINVNO);
				//cstPATRN_ADV.executeUpdate();
				//cl_dat.M_conSPDBA_pbst.commit();
				
				if(!L_rstRSSET.next())
				{	
					break;
				}	
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeCNTRN");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

		/**  Returning code value for specified code description
	 */
	private String getCODCD(String LP_CODDS_KEY)		
	{
		if(!hstCODDS.containsKey(LP_CODDS_KEY))
			return "";
		return hstCODDS.get(LP_CODDS_KEY).toString();
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
	            setMSG("Records not found in CO_CDTRN",'E');
				 //System.out.println(L_strSQLQRY);
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

	 /** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
	private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
	{
		try
		{
			//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
			if(!hstCDTRN.containsKey(LP_CDTRN_KEY))
			{
				setMSG(LP_CDTRN_KEY+" not found in hstCDTRN",'E');
				//ResultSet L_rstRSSET = cl_dat.exeSQLQRY3("select * from co_cdtrn where cmt_cgmtp||cmt_cgstp||cmt_codcd = '"+LP_CDTRN_KEY+"'");
			    //if(L_rstRSSET!=null && L_rstRSSET.next())
				//	{String L_strRETSTR = getRSTVAL(L_rstRSSET,LP_FLDNM,"C"); L_rstRSSET.close(); return L_strRETSTR;}
			}
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
		}
		return "";
	}
	/** Creting a vector for populating items into a Combo Box
	 */
	private void setCDLST(Vector<String> LP_VTRNM, String LP_CODCT, String LP_FLDNM)
	{
		try
		{
			Enumeration enmCODKEYS=hstCDTRN.keys();
			LP_VTRNM.clear();				
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals(LP_CODCT))
				{
					LP_VTRNM.addElement(getCDTRN(L_strCODCD,LP_FLDNM,hstCDTRN));
				}
			}
			//LP_VTRNM.addElement("Select");
		}
	catch(Exception e){setMSG(e,"setCDLST");}
	}

	/** Populating values in Combo Box from Vector
	 */
	private void setCMBVL(JComboBox LP_CMBNM, Vector<String> LP_VTRNM)
	{
		try
		{
			for (int j=LP_CMBNM.getItemCount()-1;j>0;j--)
				LP_CMBNM.removeItemAt(j);
			//LP_CMBNM.removeAllItems();
			for(int i=LP_VTRNM.size()-1 ; i>=0; i--)
	        {
	                LP_CMBNM.addItem(LP_VTRNM.get(i).toString());
					//System.out.println("setCMBVL : "+LP_VTRNM.get(i).toString());				
	        }
		}
		catch(Exception e){setMSG(e,"setCMBVL");}
	}


	/*private void setCMBMKTTP()
	{
		try
		{
			inlCDTRN("MKT");
			String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls||cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";				
			if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
				crtCDTRN("'MSTCOXXMKT'"," and  CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and  CMT_CHP02 like '%"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"%'",hstCDTRN);
			crtCDTRN("'D"+cl_dat.M_strCMPCD_pbst+"MR01IND','D"+cl_dat.M_strCMPCD_pbst+"MR02IND','D"+cl_dat.M_strCMPCD_pbst+"MR03IND','D"+cl_dat.M_strCMPCD_pbst+"MR04IND','D"+cl_dat.M_strCMPCD_pbst+"MR05IND','D"+cl_dat.M_strCMPCD_pbst+"MR12IND'",L_strDSTCHK+" and  substr(CMT_CODCD,4,1)='"+strYRDGT+"' and CMT_CHP01='"+M_strSBSCD.substring(0,2)+"'",hstCDTRN);
			setCDLST(vtrMKTTP,"MSTCOXXMKT","CMT_CODDS");
			setCMBVL(cmbMKTTP,vtrMKTTP);
		}
		catch(Exception e){setMSG(e,"setCMBMKTTP");}
	}*/


	private void setCMBSALTP()
	{
		try
		{
			inlCDTRN("SAL");
			String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls + cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";				
			//crtCDTRN("'SYSMR00SAL'"," and  CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and  CMT_CHP02 like '%"+M_strSBSCD.substring(2,4)+"%' and cmt_codcd in ('"+strSALTP+"','"+strTFRTP+"') ",hstCDTRN);
			crtCDTRN("'SYSMR00SAL'"," and cmt_codcd in ('"+strSALTP+"','"+strTFRTP+"') ",hstCDTRN);
			setCDLST(vtrSALTP,"SYSMR00SAL","CMT_CODDS");
			setCMBVL(cmbSALTP,vtrSALTP);
		}
		catch(Exception e){setMSG(e,"setCMBSALTP");}
	}


			private void inlCDTRN(String LP_CODTP)
		{
			try
			{
				Enumeration enmCODKEYS=hstCDTRN.keys();
				while(enmCODKEYS.hasMoreElements())
				{
					String L_strCODCD = (String)enmCODKEYS.nextElement();
					if(LP_CODTP.equals("MKT") && (L_strCODCD.substring(0,3).equals("D"+cl_dat.M_strCMPCD_pbst) || L_strCODCD.substring(0,10).equals("MSTCOXXMKT")))
					{
						hstCDTRN.remove(L_strCODCD);
					}
					if(LP_CODTP.equals("SAL") && (L_strCODCD.substring(0,10).equals("SYSMR00SAL")))
					{
						hstCDTRN.remove(L_strCODCD);
					}
				}
			}
			catch (Exception L_Ex)
			{}
		}


}