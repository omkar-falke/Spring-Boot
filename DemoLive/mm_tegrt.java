// Modified on 02/07/2005 to generated singe series for tankfarm exbonded / advanced lic. GRIN
// variables strEXBGR_fn/strADVGR_fn and related functionality removed. number generation changed
import javax.swing.JTextField;import javax.swing.JComponent;import javax.swing.JLabel;import javax.swing.JTable;import javax.swing.JComboBox;
import java.awt.event.*;
import javax.swing.InputVerifier;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
class mm_tegrt extends cl_pbase
{
	private JTextField txtGRNDT,txtBOENO,txtCONNO,txtPORNO,txtPRTCD,txtPRTNM,txtGRNNO,txtEXCCT,txtTCHQT,txtTNTQT,txtTRNCD;
	private JLabel lblTOTQT,lblBOENO;
	private JComboBox cmbGRNTP;
	private JComboBox cmbMATTP;
	private cl_JTable tblGRNDT;
	private final int TBL_CHKFL =0;
	private final int TBL_GINNO =1;
	private final int TBL_TRNCD =2;
	private final int TBL_LRYNO =3;
	private final int TBL_MATCD =4;
	private final int TBL_UOMCD =5;
	private final int TBL_CHLNO =6;
	private final int TBL_CHLDT =7;
	private final int TBL_CHLQT =8;
	private final int TBL_RCTQT =9;
	private final int TBL_DIFQT =10;
	private final int TBL_TRNNM =11;
	private String strDFLBT_fn = "COMMON";
	private String strGRNNO ="",strGRNTP ="";
	private final String strTNKST_fn = "04";           // Tankfarm Stores
	private final String strEXBTP_fn = "01";           // Exbonded Material
    private final String strBNDTP_fn = "02";           // Bonded Material
    private final String strADVTP_fn = "03";           // Adv.Lic.Material
	private INPVF objINPVR = new INPVF();	
	private TBLINPVF objTBLVRF;
//    private final String strEXBGR_fn = "47";           // EX-Bonded GRIN
//    private final String strADVGR_fn = "48";           // Adv.Lic.GRIN
    private final String strGRNTR_fn = "3";            // GRIN Doc. No.
	private PreparedStatement pstmBETRN,pstmWBTRN;
	private String strPRTNM ="";
	mm_tegrt()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			String L_strDATA ="";
			add(new JLabel("GRIN Type"),2,1,1,0.7,this,'L');
			add(cmbGRNTP = new JComboBox(),2,2,1,1.3,this,'R');									
			add(new JLabel("Mat. Type"),2,3,1,0.7,this,'L');
			add(cmbMATTP = new JComboBox(),2,4,1,1.3,this,'R');									
			
			add(new JLabel("GRIN No."),2,5,1,1,this,'L');
			add(txtGRNNO = new TxtLimit(8),2,6,1,1,this,'L');
			
			add(new JLabel("GRIN Date"),2,7,1,1,this,'L');
			add(txtGRNDT = new TxtDate(),2,8,1,1,this,'L');
			
			add(new JLabel("B/E No."),3,1,1,0.7,this,'L');
			add(txtBOENO = new TxtLimit(15),3,2,1,1.3,this,'R');
			add(new JLabel("Exc. Cat."),3,3,1,0.7,this,'L');
			add(txtEXCCT = new TxtLimit(2),3,4,1,1.3,this,'R');
			add(new JLabel("Consg. No."),3,5,1,1,this,'L');
			add(txtCONNO = new TxtLimit(15),3,6,1,1,this,'L');
			add(new JLabel("P.O. No."),3,7,1,1,this,'L');
			add(txtPORNO = new TxtLimit(15),3,8,1,1,this,'L');
			
			
			add(new JLabel("Party Code"),4,1,1,0.7,this,'L');
			add(txtPRTCD = new TxtLimit(5),4,2,1,1.3,this,'R');
			add(new JLabel("Description"),4,3,1,1,this,'L');
			add(txtPRTNM = new TxtLimit(15),4,4,1,5,this,'L');
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXGRN'";
			M_strSQLQRY += " AND CMT_STSFL <>'X' AND CMT_CHP01 like '%"+strTNKST_fn +"%'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next()){
				L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
				cmbGRNTP.addItem(L_strDATA);
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();	
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMTP' and isnull(CMT_STSFL,'') <>'X'";
			M_strSQLQRY += " AND CMT_CHP01 like '%"+strTNKST_fn +"%'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next()){
				L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
				cmbMATTP.addItem(L_strDATA);
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();	
			String[] L_COLHD = {"","Gate In","Trans.","Lorry No.","Material","UOM","Chl.No","Chl Date","Chl. Qty.","Rct. Qty.","Diff Qty.","TRN Name"};
			int[] L_COLSZ = {10,75,50,85,80,40,60,70,60,60,60,150};			
			tblGRNDT = crtTBLPNL1(this,L_COLHD,500,6,1,10,8,L_COLSZ,new int[]{0});	
			tblGRNDT.setCellEditor(TBL_TRNCD,txtTRNCD = new TxtLimit(5));
			txtTRNCD.addKeyListener(this);txtTRNCD.addFocusListener(this);
			add(lblTOTQT = new JLabel("Total Challan/Net Qty for B/E No."),17,1,1,2,this,'L');
			add(lblBOENO =  new JLabel(" "),17,3,1,1,this,'L');
			add(txtTCHQT = new TxtNumLimit(12.3),17,5,1,0.75,this,'R');
			add(txtTNTQT = new TxtNumLimit(12.3),17,6,1,0.75,this,'R');
			
			setENBL(false);	
			pstmBETRN = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Update MM_BETRN set BE_CHLQT = BE_CHLQT + ?," +
				"BE_NETQT = BE_NETQT + ?,BE_TRNFL = ?,BE_LUSBY = ?,BE_LUPDT = ?" + 
				" where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and BE_PORNO = ? and BE_CONNO = ? and BE_BOENO = ?"
				);
			pstmWBTRN = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Update MM_WBTRN set " +
				" WB_DOCRF = ?,WB_LUSBY = ?,WB_LUPDT = ?," + 
				" WB_LRYNO = ?,WB_CHLNO = ?,WB_CHLDT = ?," + 
				" WB_TPRCD = ?,WB_TPRDS = ? "+
				" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and WB_DOCTP ='01' and isnull(WB_STSFL,'') <>'X' and isnull(WB_DOCRF,'') ='' and WB_DOCNO = ?"
				);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtGRNDT.setInputVerifier(objINPVR);
			txtBOENO.setInputVerifier(objINPVR);
			txtEXCCT.setInputVerifier(objINPVR);
			txtPRTCD.setInputVerifier(objINPVR);
			objTBLVRF = new TBLINPVF();
			tblGRNDT.setInputVerifier(objTBLVRF);
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"const");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtPRTNM.setEnabled(false);
		txtCONNO.setEnabled(false);
		txtPORNO.setEnabled(false);
		txtTCHQT.setEnabled(false);
		txtTNTQT.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
		{
			txtGRNNO.setEnabled(false);
			lblTOTQT.setVisible(true);
			lblBOENO.setVisible(true);
			txtTCHQT.setVisible(true);
		    txtTNTQT.setVisible(true);
            tblGRNDT.cmpEDITR[TBL_TRNCD].setEnabled(true);
            try
            {					
    	        M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
    		    M_calLOCAL.add(java.util.Calendar.DATE,-1);																
           		txtGRNDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
    		}
    		catch(Exception L_EX)
    		{
    	       setMSG(L_EX,"setENBL");
    	    }			
		}
		else
		{
			txtGRNNO.setEnabled(true);
			lblTOTQT.setVisible(false);
			lblBOENO.setVisible(false);
			txtTCHQT.setVisible(false);
		    txtTNTQT.setVisible(false);
            tblGRNDT.cmpEDITR[TBL_TRNCD].setEnabled(false);
		}
		tblGRNDT.cmpEDITR[TBL_GINNO].setEnabled(false);
		tblGRNDT.cmpEDITR[TBL_MATCD].setEnabled(false);
		tblGRNDT.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblGRNDT.cmpEDITR[TBL_CHLQT].setEnabled(false);
		tblGRNDT.cmpEDITR[TBL_RCTQT].setEnabled(false);
		tblGRNDT.cmpEDITR[TBL_DIFQT].setEnabled(false);
		tblGRNDT.cmpEDITR[TBL_TRNNM].setEnabled(false);
		
	}
	void getDATA()
	{
		try
		{
			java.sql.Date L_datTEMP;
			String L_strTEMP ="";
			int L_intRECCT =0;
			double L_dblCHLQT = 0,L_dblRECQT = 0,L_dblTCHQT =0,L_dblTNTQT =0;
			tblGRNDT.setRowSelectionInterval(0,0);
			tblGRNDT.setColumnSelectionInterval(0,0);
			if(tblGRNDT.isEditing())
				tblGRNDT.getCellEditor().stopCellEditing();
			//tblGRNDT.setColumnSelectionInterval(0,0);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				if(txtGRNDT.getText().length() ==0)
				{
					txtGRNDT.requestFocus();
					setMSG("Enter GRIN Date ..",'E');
					return;	
				}
				if(txtEXCCT.getText().length() ==0)
				{
					txtEXCCT.requestFocus();
					setMSG("Enter Excise Category ..",'E');
					return;	
				}
				if(txtPRTCD.getText().length() !=5)
				{
					txtPRTCD.requestFocus();
					setMSG("Enter Party Code  ..",'E');
					return;	
				}
				//txtPRTNM.setText(strPRTNM);
				txtGRNDT.setEnabled(false);
				txtEXCCT.setEnabled(false);
				txtBOENO.setEnabled(false);
				txtPRTCD.setEnabled(false);
				M_strSQLQRY = "SELECT WB_DOCNO,WB_TPRCD,WB_TPRDS,WB_LRYNO,WB_MATCD,WB_CHLNO,WB_CHLDT,WB_CHLQT,WB_UOMQT,";
				M_strSQLQRY += "CT_MATDS,CT_UOMCD from MM_WBTRN,CO_CTMST ";
				M_strSQLQRY += " where WB_MATCD = CT_MATCD and WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CT_STSFL,'') <>'X' and WB_DOCTP = '01'";
				M_strSQLQRY += " and WB_DOCRF is null";
				M_strSQLQRY += " and WB_ACPTG = 'A'";
				M_strSQLQRY += " and WB_ACPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtGRNDT.getText().trim()))+"'";
				if(txtBOENO.getText().trim().length() >0)
				{
					M_strSQLQRY += " and WB_BOENO = '"+txtBOENO.getText().trim()+"'";
				}
				else if(txtPRTCD.getText().trim().length() >0)
					M_strSQLQRY += " and WB_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'";
				if(txtEXCCT.getText().trim().length() >0)
					M_strSQLQRY += " and WB_MATTP = '"+txtEXCCT.getText().trim()+"'";
		        setCursor(cl_dat.M_curWTSTS_pbst);
				setMSG("Data fetching in progress..",'N');
				tblGRNDT.clrTABLE();
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),""),L_intRECCT,TBL_GINNO);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_TPRCD"),""),L_intRECCT,TBL_TRNCD);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),""),L_intRECCT,TBL_TRNNM);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),""),L_intRECCT,TBL_LRYNO);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_MATCD"),""),L_intRECCT,TBL_MATCD);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),L_intRECCT,TBL_UOMCD);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),""),L_intRECCT,TBL_CHLNO);
					L_datTEMP = M_rstRSSET.getDate("WB_CHLDT");
					if(L_datTEMP !=null)
						tblGRNDT.setValueAt(M_fmtLCDAT.format(L_datTEMP),L_intRECCT,TBL_CHLDT);
					else
						tblGRNDT.setValueAt("",L_intRECCT,TBL_CHLDT);
					L_dblCHLQT = M_rstRSSET.getDouble("WB_CHLQT");
					L_dblRECQT = M_rstRSSET.getDouble("WB_UOMQT");
					L_dblTCHQT +=L_dblCHLQT;
					L_dblTNTQT +=L_dblRECQT;
					tblGRNDT.setValueAt(setNumberFormat(L_dblCHLQT,3),L_intRECCT,TBL_CHLQT);
					tblGRNDT.setValueAt(setNumberFormat(L_dblRECQT,3),L_intRECCT,TBL_RCTQT);
					tblGRNDT.setValueAt(setNumberFormat(L_dblCHLQT-L_dblRECQT,3),L_intRECCT,TBL_DIFQT);
					L_intRECCT++;
				}
				if(L_intRECCT ==0)
				{
					setMSG("Data not found ..",'E');
					txtGRNDT.setEnabled(true);
					txtBOENO.setEnabled(true);
					txtPRTCD.setEnabled(true);
					txtEXCCT.setEnabled(true);
				}
				else
				{
				   lblBOENO.setText(txtBOENO.getText());
				   txtTCHQT.setText(setNumberFormat(L_dblTCHQT,3));
				   txtTNTQT.setText(setNumberFormat(L_dblTNTQT,3));
				}
			}
			else
			{
				
				M_strSQLQRY = "SELECT GR_GINNO,GR_TRNCD,GR_TRNNM,GR_LRYNO,GR_MATCD,GR_CHLNO,GR_CHLDT,GR_GRNDT,GR_GRNTP,GR_MATTP,";
				M_strSQLQRY += "GR_CHLQT,GR_RECQT,GR_CNSNO,GR_BOENO,GR_PORNO,GR_VENCD,GR_VENNM,GR_EXCCT,CT_UOMCD,CT_MATDS ";
				M_strSQLQRY +=" from MM_GRMST,CO_CTMST WHERE GR_MATCD = CT_MATCD AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CT_STSFL,'') <> 'X' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
				M_strSQLQRY +=" AND GR_GRNNO ='"+txtGRNNO.getText().trim() +"' AND isnull(GR_STSFL,'') <>'X'";
				setCursor(cl_dat.M_curWTSTS_pbst);
				setMSG("Data fetching in progress..",'N');
				tblGRNDT.clrTABLE();
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				if(M_rstRSSET.next())
				{
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),""),L_intRECCT,TBL_GINNO);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_TRNCD"),""),L_intRECCT,TBL_TRNCD);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_TRNNM"),""),L_intRECCT,TBL_TRNNM);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_LRYNO"),""),L_intRECCT,TBL_LRYNO);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),L_intRECCT,TBL_MATCD);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),""),L_intRECCT,TBL_CHLNO);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),L_intRECCT,TBL_UOMCD);
					txtEXCCT.setText(nvlSTRVL(M_rstRSSET.getString("GR_EXCCT"),""));
					cl_dat.M_txtDESC_pbst.setText(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
					L_datTEMP = M_rstRSSET.getDate("GR_CHLDT");
					if(L_datTEMP !=null)
						tblGRNDT.setValueAt(M_fmtLCDAT.format(L_datTEMP),L_intRECCT,TBL_CHLDT);
					else
						tblGRNDT.setValueAt("",L_intRECCT,TBL_CHLDT);
					L_datTEMP = M_rstRSSET.getDate("GR_GRNDT");
					if(L_datTEMP !=null)
						txtGRNDT.setText(M_fmtLCDAT.format(L_datTEMP));
					else
						txtGRNDT.setText("");
					L_dblCHLQT = M_rstRSSET.getDouble("GR_CHLQT");
					L_dblRECQT = M_rstRSSET.getDouble("GR_RECQT");
					tblGRNDT.setValueAt(setNumberFormat(L_dblCHLQT,3),L_intRECCT,TBL_CHLQT);
					tblGRNDT.setValueAt(setNumberFormat(L_dblRECQT,3),L_intRECCT,TBL_RCTQT);
					tblGRNDT.setValueAt(setNumberFormat(L_dblCHLQT-L_dblRECQT,3),L_intRECCT,TBL_DIFQT);
					txtCONNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_CNSNO"),""));
					txtBOENO.setText(nvlSTRVL(M_rstRSSET.getString("GR_BOENO"),""));
					txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""));
					txtPRTCD.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""));
					txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_MATTP"),"");
					for(int i=0;i<cmbMATTP.getItemCount();i++)
					{
						if(L_strTEMP.equals(cmbMATTP.getItemAt(i).toString().substring(0,1)))
						   cmbMATTP.setSelectedIndex(i);
					}
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_GRNTP"),"");
					for(int i=0;i<cmbGRNTP.getItemCount();i++)
					{
						if(L_strTEMP.equals(cmbGRNTP.getItemAt(i).toString().substring(0,2)))
						   cmbGRNTP.setSelectedIndex(i);
					}
				}
				else
					setMSG("Data Not found.. ",'E');
				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			try
			{
    			cmbGRNTP.requestFocus();	
    			clrCOMP();
    			if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
    			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
    			{
    			    M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
        		    M_calLOCAL.add(java.util.Calendar.DATE,-1);																
               	    txtGRNDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
    			}
			}
			catch(Exception L_E)
			{
			    setMSG(L_E,"action Performed");    
			}
		}
		if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				setENBL(true);
				txtGRNDT.requestFocus();
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == cmbGRNTP)
		{
			if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				/*if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strEXBGR_fn))
					txtEXCCT.setText(strEXBTP_fn);
				else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strADVGR_fn))
					txtEXCCT.setText(strADVTP_fn);	*/
				txtGRNDT.requestFocus();
			}
		}
		if(M_objSOURC == txtBOENO)
		{
			cl_dat.M_txtDESC_pbst.setText("");
			if(txtBOENO.getText().length() >0)
			{
				tblGRNDT.clrTABLE();
				getDATA();
				txtEXCCT.setEnabled(false);
				txtPRTCD.setEnabled(false);
			}
			else
			{
				txtEXCCT.setEnabled(true);
				txtPRTCD.setEnabled(true);
				/*if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strEXBGR_fn))
					txtEXCCT.setText(strEXBTP_fn);
				else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strADVGR_fn))
					txtEXCCT.setText(strADVTP_fn);*/
				setMSG("Enter Excise Category..",'N');
					txtEXCCT.requestFocus();
			}
		}
		
		if(M_objSOURC == txtGRNNO)
		{
			String L_strGRNNO = txtGRNNO.getText().trim();
			clrCOMP();
			tblGRNDT.clrTABLE();
			cl_dat.M_txtDESC_pbst.setText("");
			txtGRNNO.setText(L_strGRNNO);
			getDATA();
		}
		if(M_objSOURC == txtPRTCD)
		{
			tblGRNDT.clrTABLE();
			cl_dat.M_txtDESC_pbst.setText("");
			if(txtPRTCD.getText().length() >0)
				txtPRTCD.setText(txtPRTCD.getText().toUpperCase());
			getDATA();
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbGRNTP)
			{
				cmbMATTP.requestFocus();
				setMSG("Enter Material Type..",'N');
			}
			else if(M_objSOURC == cmbMATTP)
			{
				txtGRNDT.requestFocus();
				setMSG("Enter GRIN Date..",'N');
			}
			else if(M_objSOURC == txtGRNDT)
			{
				txtBOENO.requestFocus();
				setMSG("Enter Bill of Entry No...",'N');
			}
			else if(M_objSOURC == txtBOENO)
			{
				txtBOENO.transferFocus();
				//setMSG("Enter Bill of Entry No...",'N');
			}
			else if(M_objSOURC == txtEXCCT)
			{
				setMSG("Enter Party Code..",'N');
				txtPRTCD.requestFocus();
			}
			else if(M_objSOURC == tblGRNDT.cmpEDITR[TBL_TRNCD])
			{
				try
				{
    				M_strSQLQRY = "SELECT PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='T'";
    				M_strSQLQRY +=" AND PT_PRTCD ='"+txtTRNCD.getText().trim() +"'";
    				M_rstRSSET =cl_dat.exeSQLQRY(M_strSQLQRY);
    				if(M_rstRSSET !=null)
    				{
    	                if(M_rstRSSET.next())
    	                {
    	                    tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),tblGRNDT.getSelectedRow(),TBL_TRNNM);    
    	                }	
    	                else
    	                    setMSG("Invalid Code..",'E');
    				}
				}
				catch(Exception L_E)
				{
				    setMSG(L_E,"vk_enter");    
				}
			}

		}
		else if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			 if(M_objSOURC == txtBOENO)	// B/E No.
			 {
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtBOENO";
                strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
                String L_ARRHDR[] = {"B/E No","B/E Date","B/E Qty","Chalan Qty","Consign.No.","Consign Name","Supp.Code","Supplier","P.O.No.","Exicse Category"};
				M_strSQLQRY = "Select BE_BOENO,BE_BOEDT,BE_BOEQT,BE_CHLQT,BE_CONNO,BE_CONDS,PO_VENCD,PT_PRTNM,BE_PORNO,BE_MATTP";
				M_strSQLQRY += " from MM_BETRN,MM_POMST,CO_PTMST";    
				M_strSQLQRY += " where BE_CMPCD = PO_CMPCD AND BE_PORNO = PO_PORNO";
				M_strSQLQRY += " and BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and PO_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
				M_strSQLQRY += " and PO_VENCD = PT_PRTCD";
				M_strSQLQRY += " and PT_PRTTP = 'S'";
				M_strSQLQRY += " and isnull(BE_BOEQT,0) > isnull(BE_CHLQT,0)";
                M_strSQLQRY += " and isnull(BE_STSFL,'') <> 'X'";
             //   if(strGRNTP.equals(strEXBGR_fn))
             //     M_strSQLQRY += " and BE_MATTP = '" + strEXBTP_fn + "'";        
             //   else if(strGRNTP.equals(strADVGR_fn))
             //       M_strSQLQRY += " and BE_MATTP = '" + strADVTP_fn + "'";     
				if(txtBOENO.getText().trim().length() >0)
					M_strSQLQRY += " and BE_BOENO like '" + txtBOENO.getText() + "%'";     
				M_strSQLQRY += " order by BE_BOEDT desc" ;
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,10,"CT");
			}
		 else if(M_objSOURC == txtGRNNO)	// B/E No.
		 {
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtGRNNO";
            strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
            String L_ARRHDR[] = {"GRIN No.","GRIN DATE","Excise Category"};
			M_strSQLQRY = "Select GR_GRNNO,GR_GRNDT,GR_EXCCT";
			M_strSQLQRY += " FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and GR_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
		    M_strSQLQRY += " and isnull(GR_STSFL,'') <> 'X'";
			if(txtGRNNO.getText().length() >0)
				M_strSQLQRY +=" AND GR_GRNNO like '"+txtGRNNO.getText().trim() +"%'";	
         	M_strSQLQRY += " order by GR_GRNDT desc" ;
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
		}
		  else if(M_objSOURC == txtPRTCD)	// B/E No.
		 {
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtPRTCD";
            String L_ARRHDR[] = {"Party Code","Party Name"};
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM ";
			M_strSQLQRY += " FROM CO_PTMST WHERE PT_PRTTP ='S' ";
		    M_strSQLQRY += " and isnull(PT_STSFL,'') <> 'X'";
			if(txtPRTCD.getText().length() >0)
				M_strSQLQRY +=" AND PT_PRTCD like '"+txtPRTCD.getText().trim() +"%'";	
			M_strSQLQRY += " ORDER BY PT_PRTCD";
         	if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
		}
		 else if(M_objSOURC == txtEXCCT)	// Excise Category
		 {
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtEXCCT";
            String L_ARRHDR[] = {"Excise Category","Description"};
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS ";
			M_strSQLQRY += " FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' and CMT_CGSTP = 'MMXXMAT'";
		    M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X'";
			if(txtEXCCT.getText().length() >0)
				M_strSQLQRY +=" AND CMT_CODCD like '"+txtEXCCT.getText().trim() +"%'";	
         	if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
		 }
		 else if(M_objSOURC == txtTRNCD)	// B/E No.
		 {
		  	cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtTPRCD";
            String L_ARRHDR[] = {"Transporter Code","Name"};
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM ";
			M_strSQLQRY += " FROM CO_PTMST WHERE PT_PRTTP ='T' ";
		    M_strSQLQRY += " and isnull(PT_STSFL,'') <> 'X'";
		//	if(txtPRTCD.getText().length() >0)
		//		M_strSQLQRY +=" AND PT_PRTCD like '"+txtPRTCD.getText().trim() +"%'";	
			M_strSQLQRY += " ORDER BY PT_PRTCD";
         	if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
		}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtBOENO"))
		{	
			txtBOENO.setText(cl_dat.M_strHLPSTR_pbst);
			txtCONNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim());
			txtPRTCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),6)).trim());
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),7)).trim());
			txtPORNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),8)).trim());
			txtEXCCT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),9)).trim());
		}
		else if(M_strHLPFLD.equals("txtGRNNO"))
		{	
			txtGRNNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
		else if(M_strHLPFLD.equals("txtPRTCD"))
		{	
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD.equals("txtTPRCD"))
		{	
		    txtTRNCD.setText(cl_dat.M_strHLPSTR_pbst);
			//txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);
			tblGRNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblGRNDT.getSelectedRow(),TBL_TRNNM);
		}
		else if(M_strHLPFLD.equals("txtEXCCT"))
		{	
			txtEXCCT.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
	boolean vldDATA()
	{
		try
		{
			if(tblGRNDT.isEditing())
    		{
    			if(tblGRNDT.getValueAt(tblGRNDT.getSelectedRow(),tblGRNDT.getSelectedColumn()).toString().length()>0)
    			{
        			TBLINPVF obj=new TBLINPVF();
        			obj.setSource(tblGRNDT);
        			if(obj.verify(tblGRNDT.getSelectedRow(),tblGRNDT.getSelectedColumn()))
        				tblGRNDT.getCellEditor().stopCellEditing();
        			else
        				return false;
    			}
    		}
			int L_intRECCT =0;
			if(txtGRNDT.getText().length() ==0)
			{
				setMSG("GRIN Date can not be blank..",'E');
				return false;
			}
			else if(txtEXCCT.getText().length() ==0)
			{
				setMSG("Excise Category can not be blank..",'E');
				return false;
			}
			else if(txtPRTCD.getText().length() ==0)
			{
				setMSG("Party Code can not be blank..",'E');
				return false;
			}
			else if(M_fmtLCDAT.parse(txtGRNDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
			{							
				setMSG("Invalid GRIN Date,Should not be greater than Login Date(DD/MM/YYYY)",'E');
				return false;
			}
			/*if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strEXBGR_fn))
			{
				if(!txtEXCCT.getText().equals(strEXBTP_fn))
				{
					setMSG("Please check the GRIN Type and Excise Category..",'E');
					txtEXCCT.requestFocus();
					return false;
				}
			}
			else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strADVGR_fn))
			{
				if(!txtEXCCT.getText().equals(strADVTP_fn))
				{
					setMSG("Please check the GRIN Type and Excise Category..",'E');
					txtEXCCT.requestFocus();
					return false;
				}	
			}*/
			for(int i=0;i<tblGRNDT.getRowCount();i++)
			{
				if(tblGRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intRECCT++;
					if(tblGRNDT.getValueAt(i,TBL_GINNO).toString().length() ==0)
					{
						setMSG("Gate in number can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(tblGRNDT.getValueAt(i,TBL_TRNCD).toString().length() ==0)
					{
						setMSG("Transporter Code can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(tblGRNDT.getValueAt(i,TBL_LRYNO).toString().length() ==0)
					{
						setMSG("Lorry No. can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(tblGRNDT.getValueAt(i,TBL_MATCD).toString().length() ==0)
					{
						setMSG("Material Code can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(tblGRNDT.getValueAt(i,TBL_CHLNO).toString().length() ==0)
					{
						setMSG("Challan no.can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(tblGRNDT.getValueAt(i,TBL_CHLDT).toString().length() ==0)
					{
						setMSG("Challan date can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(M_fmtLCDAT.parse(tblGRNDT.getValueAt(i,TBL_CHLDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{							
						setMSG("Invalid Challan Date at row .."+(i+1),'E');
						return false;
					}
					else if(tblGRNDT.getValueAt(i,TBL_CHLQT).toString().length() ==0)
					{
						setMSG("Challan qty. can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(tblGRNDT.getValueAt(i,TBL_RCTQT).toString().length() ==0)
					{
						setMSG("Recieved qty. can not be blank.. at row "+(i+1),'E');
						return false;
					}
					else if(Double.parseDouble(tblGRNDT.getValueAt(i,TBL_CHLQT).toString())<= 0)
					{
						setMSG("Invalid Challan qty. at row "+(i+1),'E');
						return false;
					}
					else if(Double.parseDouble(tblGRNDT.getValueAt(i,TBL_RCTQT).toString())<= 0)
					{
						setMSG("Invalid Received qty. at row "+(i+1),'E');
						return false;
					}
				}
			}
			if(L_intRECCT ==0)
			{
				setMSG("Please select at least one row for saving..",'E');
				return false;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
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
			if(tblGRNDT.isEditing())
				tblGRNDT.getCellEditor().stopCellEditing();
			tblGRNDT.setRowSelectionInterval(0,0);
			tblGRNDT.setColumnSelectionInterval(0,0);
			String L_strLUPDT = cl_dat.M_strLOGDT_pbst.substring(6) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
			String L_strCHLDT ="";
			cl_dat.M_flgLCUPD_pbst = true;
			strGRNNO ="";
			strGRNNO = genGRNNO(cmbMATTP.getSelectedItem().toString().substring(0,1));
			if(strGRNNO.length() ==0)
			{
			    setMSG("Error in Number Generation ..",'E');
			    return;    
			}
			String L_strFGRNO = strGRNNO;
			boolean L_flgFIRST = false;
			for(int i=0;i<tblGRNDT.getRowCount();i++)
			{
				if(tblGRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(!L_flgFIRST)
					{
						L_flgFIRST = true;
					}
					else
					{
						strGRNNO =String.valueOf(Integer.valueOf(strGRNNO).intValue() +1);
						for(int j=strGRNNO.length(); j<8; j++)				// for padding zero(s)
							strGRNNO = "0"+strGRNNO;
					}
					M_strSQLQRY = "Insert into MM_GRMST(GR_STRTP,GR_GRNTP,GR_GRNNO,GR_AMDNO,GR_GRNDT,";
					M_strSQLQRY += "GR_MATTP,GR_GINNO,GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_CHLQT,GR_BOENO,";      
					M_strSQLQRY += "GR_CNSNO,GR_PORNO,GR_VENCD,GR_VENNM,GR_TRNCD,GR_TRNNM,";      
				    M_strSQLQRY += "GR_MATCD,GR_RECQT,GR_ACPQT,";
					M_strSQLQRY += "GR_MODFL,GR_TRNFL,GR_STSFL,GR_LUSBY,GR_LUPDT,GR_EXCCT,GR_BATNO) values (";
					M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
					M_strSQLQRY += "'"+cmbGRNTP.getSelectedItem().toString().substring(0,2)+"',";
					M_strSQLQRY += "'"+strGRNNO+"',";
					M_strSQLQRY += "'00',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtGRNDT.getText()))+"',";			
					M_strSQLQRY += "'" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "',";
					M_strSQLQRY += "'" + tblGRNDT.getValueAt(i,TBL_GINNO) + "',";
					M_strSQLQRY += "'" + tblGRNDT.getValueAt(i,TBL_LRYNO) + "',";
					M_strSQLQRY += "'" + tblGRNDT.getValueAt(i,TBL_CHLNO) + "',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblGRNDT.getValueAt(i,TBL_CHLDT).toString()))+"',";			
						M_strSQLQRY += tblGRNDT.getValueAt(i,TBL_CHLQT) + ",";
					M_strSQLQRY += "'" + txtBOENO.getText().trim() + "',";
					M_strSQLQRY += "'" + txtCONNO.getText().trim() + "',";
					M_strSQLQRY += "'" + txtPORNO.getText().trim() + "',";
					M_strSQLQRY += "'" + txtPRTCD.getText().trim() + "',";
					M_strSQLQRY += "'" + txtPRTNM.getText().trim() + "',";
					M_strSQLQRY += "'" + tblGRNDT.getValueAt(i,TBL_TRNCD) + "',";
					M_strSQLQRY += "'" + tblGRNDT.getValueAt(i,TBL_TRNNM) + "',";
					M_strSQLQRY += "'" + tblGRNDT.getValueAt(i,TBL_MATCD) + "',";	
					M_strSQLQRY += tblGRNDT.getValueAt(i,TBL_RCTQT) + ",";
					M_strSQLQRY += tblGRNDT.getValueAt(i,TBL_RCTQT) + ","; // ACPQT
					M_strSQLQRY += "'Y',";
					M_strSQLQRY += getUSGDTL("GR",'I',"");
					M_strSQLQRY += ",'"+txtEXCCT.getText().trim()+"',";
					M_strSQLQRY += "'" + strDFLBT_fn + "')";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst)
					{
					    M_strSQLQRY = "UPDATE MM_STMST SET ST_STKQT = ST_STKQT + " + tblGRNDT.getValueAt(i,TBL_RCTQT)
					                + " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'"
					                + " AND ST_MATCD ='" + tblGRNDT.getValueAt(i,TBL_MATCD) + "' ";	
					   cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					   M_strSQLQRY = "UPDATE MM_STPRC SET STP_YCSQT = STP_YCSQT + " + tblGRNDT.getValueAt(i,TBL_RCTQT)
					                + " where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP ='"+M_strSBSCD.substring(2,4) +"'"
					                + " AND STP_MATCD ='"+ tblGRNDT.getValueAt(i,TBL_MATCD) + "' ";	
					   cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					if(cl_dat.M_flgLCUPD_pbst)
					{
						
						L_strCHLDT =  tblGRNDT.getValueAt(i,TBL_CHLDT).toString().substring(6) + "-" + tblGRNDT.getValueAt(i,TBL_CHLDT).toString().substring(3,5) + "-" + tblGRNDT.getValueAt(i,TBL_CHLDT).toString().substring(0,2);
						//L_strCHLDT =  tblGRNDT.getValueAt(i,TBL_).toString().substring(6) + "-" + tblGRNDT.getValueAt(i,TBL_CHLDT).toString().substring(3,5) + "-" + tblGRNDT.getValueAt(i,TBL_CHLDT).toString().substring(0,2);
						pstmWBTRN.setString(1,strGRNNO);
						pstmWBTRN.setString(2,cl_dat.M_strUSRCD_pbst);
						pstmWBTRN.setDate(3,java.sql.Date.valueOf(L_strLUPDT));
						pstmWBTRN.setString(4,tblGRNDT.getValueAt(i,TBL_LRYNO).toString());
						pstmWBTRN.setString(5,tblGRNDT.getValueAt(i,TBL_CHLNO).toString());
						pstmWBTRN.setDate(6,java.sql.Date.valueOf(L_strCHLDT));
						pstmWBTRN.setString(7,tblGRNDT.getValueAt(i,TBL_TRNCD).toString());
						pstmWBTRN.setString(8,tblGRNDT.getValueAt(i,TBL_TRNNM).toString());
						pstmWBTRN.setString(9,tblGRNDT.getValueAt(i,TBL_GINNO).toString());
						if(pstmWBTRN.executeUpdate() == 1)
							cl_dat.M_flgLCUPD_pbst = true;
						else
							cl_dat.M_flgLCUPD_pbst = false;
					}
					if(cl_dat.M_flgLCUPD_pbst)
					{
						if(txtBOENO.getText().length() > 0)
						{	// If Bill of Entry is not available
							pstmBETRN.setDouble(1,Double.parseDouble(tblGRNDT.getValueAt(i,TBL_CHLQT).toString()));
							pstmBETRN.setDouble(2,Double.parseDouble(tblGRNDT.getValueAt(i,TBL_RCTQT).toString()));
							pstmBETRN.setString(3,"0");
							pstmBETRN.setString(4,cl_dat.M_strUSRCD_pbst);
							pstmBETRN.setDate(5,java.sql.Date.valueOf(L_strLUPDT));
							pstmBETRN.setString(6,txtPORNO.getText());
							pstmBETRN.setString(7,txtCONNO.getText());
							pstmBETRN.setString(8,txtBOENO.getText());
							if(pstmBETRN.executeUpdate() == 1)
								cl_dat.M_flgLCUPD_pbst = true;
							else
								cl_dat.M_flgLCUPD_pbst = false;
						}
					}
				}
				
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				exeGRNNO(strGRNNO,cmbMATTP.getSelectedItem().toString().substring(0,1));
			}
			if(!cl_dat.M_flgLCUPD_pbst)
				cl_dat.M_conSPDBA_pbst.rollback();
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				JOptionPane.showMessageDialog(this,"GRIN's created from "+L_strFGRNO +" to "+strGRNNO,"GRIN ",JOptionPane.INFORMATION_MESSAGE);
				setMSG("GRIN's Created from "+L_strFGRNO +" to "+ strGRNNO,'N');
				clrCOMP();
				lblBOENO.setText("");
				setENBL(true);
			}
			else
				setMSG("Error in saving..",'E');
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
		}
	}
// Method to generate the new GRIN No.
	private String genGRNNO(String P_strMATTP)
	{
		String L_strGRNNO  = "",  L_strCODCD = "", L_strCCSVL = "";
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"GRN' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strGRNTR_fn + P_strMATTP + "'";
			//M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + cmbGRNTP.getSelectedItem().toString().substring(0,2) + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_strCODCD = M_rstRSSET.getString("CMT_CODCD");
					L_strCCSVL = M_rstRSSET.getString("CMT_CCSVL");
				}
				M_rstRSSET.close();
			}
			L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
			for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
				L_strGRNNO += "0";
			L_strCCSVL = L_strGRNNO + L_strCCSVL;
			L_strGRNNO = L_strCODCD + L_strCCSVL;
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"genGRNNO");
		}
		return L_strGRNNO;
	}
	private void exeGRNNO(String P_strGRNNO,String P_strMATTP)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CCSVL = '" + P_strGRNNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"GRN'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strGRNTR_fn + P_strMATTP + "'";			
		//  cOMMENTED ON 02/07/2005 SO AS TO KEEP SINGLE SERIES FOR TANKFARM GRIN 
		//	M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + cmbGRNTP.getSelectedItem().toString().substring(0,2) + "'";			
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}catch(Exception e){
			setMSG(e,"exeGRNNO");
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
				if(input == txtGRNDT)
				{
					if(M_fmtLCDAT.parse(txtGRNDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{							
						setMSG("Invalid Date,Should not be greater than Login Date..",'E');
						return false;
					}
				}
				if(input == txtBOENO)
				{
					M_strSQLQRY = "Select BE_BOENO,BE_CONNO,BE_BOEQT,BE_CHLQT,PO_VENCD,PT_PRTNM,BE_PORNO";
					M_strSQLQRY += " from MM_BETRN,MM_POMST,CO_PTMST";    
					M_strSQLQRY += " where BE_CMPCD=PO_CMPCD and BE_PORNO = PO_PORNO";
					M_strSQLQRY += " and PT_PRTTP = 'S'";
					M_strSQLQRY += " and PT_PRTCD = PO_VENCD";
					M_strSQLQRY += " and BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and BE_BOENO = '" + txtBOENO.getText().trim() + "'";
					M_strSQLQRY += " and BE_BOEQT > BE_CHLQT";
					M_strSQLQRY += " and BE_STSFL <> 'X'" ;
                 /*   if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strEXBGR_fn))
                         M_strSQLQRY += " and BE_MATTP = '" + strEXBTP_fn + "'";        
                    else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strADVGR_fn))
                         M_strSQLQRY += " and BE_MATTP = '" + strADVTP_fn + "'";*/
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						txtCONNO.setText(M_rstRSSET.getString("BE_CONNO"));
						txtPRTCD.setText(M_rstRSSET.getString("PO_VENCD"));
						txtPRTNM.setText(M_rstRSSET.getString("PT_PRTNM"));
						txtPORNO.setText(M_rstRSSET.getString("BE_PORNO"));
						txtEXCCT.setEnabled(false);
						txtPRTCD.setEnabled(false);
					}
				}
				if(input == txtEXCCT)
				{
					try
					{
						M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN"+
									  " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'"+
						              " and isnull(CMT_STSFL,'') <>'X' and CMT_CODCD = '" + txtEXCCT.getText().trim() + "'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET !=null)
						if(M_rstRSSET.next())
						{
							setMSG("",'N');
							M_rstRSSET.close();			
							return true;
						}
						else
						{
							if(M_rstRSSET != null)
								M_rstRSSET.close();		
							setMSG("Invalid Excise Category..",'E');
							return false;
						}
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"vldEXCCT");
						return false;
					}	
				}
				if(input == txtPRTCD)
				{
					try
					{
						strPRTNM ="";
						
						M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST"+
									  " where PT_PRTTP = 'S' "+
						              " and isnull(PT_STSFL,'') <>'X'" +
									  "	and PT_PRTCD = '" + txtPRTCD.getText().trim() + "'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET !=null)
						if(M_rstRSSET.next())
						{
							strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
							txtPRTNM.setText(strPRTNM);
							M_rstRSSET.close();			
							return true;
						}
						else
						{
							if(M_rstRSSET != null)
								M_rstRSSET.close();		
							setMSG("Invalid Party Code..",'E');
							return false;
						}
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"vldEXCCT");
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
			String L_strTEMP ="";
			try
			{
				if(P_intCOLID==TBL_TRNCD)
				{
				  	M_strSQLQRY = "SELECT PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='T'";
    				M_strSQLQRY +=" AND PT_PRTCD ='"+txtTRNCD.getText().trim() +"'";
    				M_rstRSSET =cl_dat.exeSQLQRY(M_strSQLQRY);
    				if(M_rstRSSET !=null)
    				{
    	                if(M_rstRSSET.next())
    	                {
    	                    tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),tblGRNDT.getSelectedRow(),TBL_TRNNM);    
    	                }	
    	                else
    	                {
    	                    setMSG("Invalid Code..",'E');
    	                    return false;
    	                }
    	    		}
				}
			}
			catch(Exception L_E)
			{
			    setMSG(L_E,"table verify");
			    return false;
			    }
			return true;
		}
    }
    
}