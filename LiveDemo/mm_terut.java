/*
System Name   : Material Management System
Program Name  : Receipt Updating (Tankfarm) 
Program Desc. :
Author        : N.K.Virdi
Date          : 16 June 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

/** 
<PRE>
System Name : Material Management System.
 
Program Name : Receipt Updation (mm_terut.java)

Purpose : This module updates Raw Material Received.

List of tables used :
Table Name		Primary key							Operation done
										Insert	Update	   Query    Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
MM_WBTRN		WB_DOCTP,WB_DOCNO,WB_SRLNO			 *	     *            
MM_DPTRN		DP_MEMTP,DP_MEMNO,DP_TNKNO			 *     
CO_CTMST		CT_MATCD								     *
MM_TKMST		TK_TNKNO							                 
MM_BETRN		BE_STRTP,BE_PORNO,BE_CONNO,BE_CONNO		     * 
CO_PTMST        PT_PRTTP,PT_PRTCD				             * 		
CO_CDTRN        CMT_CGMTP,CMT_CGSTP,CMT_CODCD	             *
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name		Column Name		Table name		Type/Size	Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtBOENO		WB_BOENO		MM_WBTRN		Varchar(15)	Bill of Entry No.
cmbMATTP		WB_MATTP		MM_WBTRN		Varchar(2)	Material Type
txtRCTQT		DP_RCTQT		MM_DPTRN		Decimal(12,3)	Qty Received
txtTNKNO		WB_TNKNO		MM_WBTRN		Varchar(10)	Tank No.
txtQNLRF		WB_QNLRF		MM_WBTRN		Varchar (8)	Quantity Lot Reference
txtACPDT		WB_ACPDT		MM_WBTRN		Date		Accpeted Date
TBL_GINNO		WB_GINNO		MM_WBTRN		Varchar(8)	Gate-In No.
TBL_TPRDS		WB_TPRDS		MM_WBTRN		Varchar(40)	Gate-In No.
TBL_LRYNO		WB_LRYNO		MM_WBTRN		Varchar(15)	Lorry No.
TBL_CHLNO		WB_CHLNO		MM_WBTRN		Varchar(15)	Chalan No.
TBL_CHLDT		WB_CHLDT		MM_WBTRN		Date		Chalan Date
TBL_LOCCD 		WB_LOCCD		MM_WBTRN		Varchar(2)	Location
txtMATCD		WB_MATCD		MM_WBTRN		Varchar(10)	Material Code
txtMATDS		WB_MATDS		MM_WBTRN		Varchar(45)	Material Description
txtUOMCD		CT_UOMCD		CO_CTMST		Varchar(3)	Unit of Measurement
txtCHLQT		WB_CHLQT		MM_WBTRN		Decimal(12,3)	Chalan Quantity
txtNETWT		WB_UOMQT		MM_WBTRN		Deciaml(12,3)	Qty in UOM
txtSHRQT		Calculated
txtACPTG		WB_ACPTG		MM_WBTRN		Varchar(1)	Acceptance Tag
chkDRVRT		WB_DEFFL		MM_WBTRN		Varchar(1)	Defaulter flag
txtRATDS		WB_DEFDS		MM_WBTRN		Varchar(30)	Remarks, if marked as defaulter
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description			      Display Columns			Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtBOENO	B/E No,B/E Date,B/E Qty,Mat.Type  BE_BOENO,BE_BOEDT          MM_BETRN
		    B/E No,Tank No,Receipt Date		  BE_BOEQT,BE_MATTP		
				                              WB_BOENO,WB_TNKNO,WB_ACPDT MM_WBTRN
						
txtTNKNO	Tank No,Material			     TK_TNKNO,TK_MATDS		MM_TKMST
txtLOCCD	Location code,desc			     CMT_CODCD,CMT_CODDS	CO_CDTRN (SYS/QC11TKL)
txtACPTG	code,desc			             CMT_CODCD,CMT_CODDS	CO_CDTRN (SYS/MMXXACP)
txtTPRCD	Transporter code,Name			 PT_PRTCD,PT_PRTNM	    CO_PTMST  PT_PRTTP ='T'
txtVENCD	Vendor code,Name			    PT_PRTCD,PT_PRTNM	    CO_PTMST  PT_PRTTP ='S'

-----------------------------------------------------------------------------------------------------------------------------------------------------

Validations :
	1. Once the Tanker is accepted ,its quantity is updated in Dip Register for the Receipt Date and Tank No..
	2. Update WB_ACPTG  = 'A'  to show that material is accepted.
	3. For Styrene receipts, validation for blank bill of entry no.
	4. Dummy Transporter code validation included.(dummy code Z9999 can not be accepted)
	5. Material Type combo is generated from CO_CDTRN (SYS/MMXXMAT)

</PRE>
*/

import java.sql.PreparedStatement;import java.sql.ResultSet;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.InputVerifier;import javax.swing.JComponent;
import java.util.Hashtable;import java.util.StringTokenizer;

class mm_terut extends cl_pbase
{                               
                                                 /** Result set variable used in program */
	private ResultSet rstRSSET1;                 /** prepared statement for updating mm_wbtrn table */
	private PreparedStatement pstmWBTRN;         /** prepared statement for updating mm_dptrn table */
	private PreparedStatement pstmDPTRN ;        /** prepared statement for query in mm_betrn table */
	private PreparedStatement pstmBETRN;         /** Hashtable for storing data related to Gate in number   */
	private Hashtable<String,String> hstBOEVL = new Hashtable<String,String>();   /** Hashtable for storing saved Net Qty - to be used in modification   */
	private Hashtable<String,String> hstNETQT = new Hashtable<String,String>();  /** Hashtable for storing codes from co_cdtrn */
	private Hashtable<String,String> hstCODDS = new Hashtable<String,String>();  /** String array for storing data related to selected gate in Numbers */
	private String staDATA[] = new String[20]; 	

	                                               /** JTable for displaying record for receipt updating */  
	private cl_JTable tblWBRVL; 
	private JTextField txtCHLQT,txtNETWT,txtSHRQT,txtBOENO,txtACPTG,txtBOEQT,txtBECQT;
	private JTextField txtTNKNO,txtQNLRF,txtMATCD,txtMATDS,txtRATDS,txtUOMCD,txtVENCD,txtVENNM;
	private JTextField txtRCTQT,txtRCTDT;
	private JTextField txtLOCCD,txtTPRCD,txtGINNO;
	private JComboBox cmbMATTP;
	private JCheckBox chkDRVRT;
	private JLabel lblGINNO;
	
	private String strGINNO,strCHLQT,strUOMQT,strSHRQT="",strACPTG,strBOENO;
	private String strTNKNO,strQNLRF,strDRVRT,strLUSBY,strLUPDT;
	private String strRCTDT,LM_MATCD;
	private String strRATDS,strVENCD="",strVENNM="",strTPRNM ="";
	private String strCHKFL,strBOEQT,strBOCQT,LM_OBECQT,strBEMAT,strBENQT;
	private String strMATTP,strOLDFL,strONETFL;
	private String strFRDTM,strTODTM;
	private String strHLPFLD,strPRTNM;
	private double dblCHLQT,dblUOMQT,dblBECQT,dblBCHQT,dblRCTQT,dblBENQT;
	private int intTBLCL,intTBLRW,intROWCT,intOLDRW = 0;
	
	private final int TBL_DUMFL = 0;
	private final int TBL_CHKFL = 1;
	private final int TBL_GINNO = 2;
	private final int TBL_TPRCD = 3;
	private final int TBL_TPRDS = 4;
	private final int TBL_LRYNO = 5;
	private final int TBL_CHLNO = 6;
	private final int TBL_CHLDT = 7;
	private final int TBL_LOCCD = 8; 
	private final int TBL_OLDFL = 9;            
	                                                /** Document Type - Tanker */
	private final String strDOCTP_fn = "01";		/** Regular Dip */
	private final String strREGTP_fn = "81";	    /** Transfer flag */	
	private final String strTRNFL_fn = "0";		    /**  Message to select Mat.Type */
	private final String LM_SELECT = "Select";	    /** Code for styrene monomer, for specific validation */
    private final String M_strCODSTY_fn = "6805010045" ;     
	
	mm_terut()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Receipt Date"),2,1,1,1,this,'L');
			add(txtRCTDT = new TxtDate(),2,2,1,1,this,'L');
			
			add(new JLabel("B/E No."),2,3,1,1,this,'L');
			add(txtBOENO = new TxtLimit(15),2,4,1,1,this,'L');
		
			add(new JLabel("Mat. Type"),2,5,1,1,this,'L');
			add(cmbMATTP = new JComboBox(),2,6,1,2,this,'L');
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CGSTP from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP IN('MMXXMAT','MMXXACP','QC11TKL')";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			cmbMATTP.addItem(LM_SELECT);
			if(M_rstRSSET != null)
			while(M_rstRSSET.next())
			{
			    if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXMAT"))
				    cmbMATTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));
				else
				  hstCODDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));    
			}
			cmbMATTP.setEnabled(false);
			add(new JLabel("Rcpt. Qty."),3,1,1,1,this,'L');
			add(txtRCTQT = new TxtNumLimit(12.3),3,2,1,1,this,'L');
		
			add(new JLabel("B/E Qty."),3,3,1,1,this,'L');
			add(txtBOEQT = new TxtNumLimit(12.3),3,4,1,1,this,'L');
		
			add(new JLabel("Chl/Net Qty."),3,5,1,1,this,'L');
			add(txtBECQT = new TxtNumLimit(12.3),3,6,1,1,this,'L');
			
			add(new JLabel("Supplier"),4,1,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),4,2,1,1,this,'L');
			add(txtVENNM = new TxtLimit(40),4,3,1,4,this,'L');
			
			String[] L_COLHD = {"","","Gate-In No.","Code","Transporter","Lorry No.","Chalan No.","Chalan Date","Location","O"};
			int[] L_COLSZ = {10,10,70,60,200,100,60,90,30,10};
			//intROWCT = 100;
			intROWCT = 250;
			tblWBRVL = crtTBLPNL1(this,L_COLHD,intROWCT,5,1,9,8,L_COLSZ,new int[]{1});
	        tblWBRVL.addMouseListener(this);
			tblWBRVL.setCellEditor(TBL_LOCCD,txtLOCCD = new TxtLimit(3));
			tblWBRVL.setCellEditor(TBL_TPRCD,txtTPRCD = new TxtLimit(5));
			tblWBRVL.setCellEditor(TBL_GINNO,txtGINNO = new TxtLimit(8));
			txtLOCCD.addKeyListener(this);txtLOCCD.addFocusListener(this);
			txtTPRCD.addKeyListener(this);txtTPRCD.addFocusListener(this);
			txtGINNO.addKeyListener(this);txtGINNO.addFocusListener(this);
			add(lblGINNO = new JLabel("            "),14,1,1,2,this,'L');
			lblGINNO.setForeground(Color.red);
					 
			add(new JLabel("Material"),15,1,1,1,this,'L');
			add(txtMATCD = new TxtLimit(10),15,2,1,1,this,'L');
			add(txtMATDS = new TxtLimit(60),15,3,1,2,this,'L');
			add(new JLabel("UOM"),15,5,1,1,this,'L');
			add(txtUOMCD = new TxtLimit(3),15,6,1,1,this,'L');
			
			add(new JLabel("Challan Qty."),16,1,1,1,this,'L');
			add(txtCHLQT = new TxtNumLimit(12.3),16,2,1,1,this,'L');
			add(new JLabel("Net Qty."),16,3,1,1,this,'L');
			add(txtNETWT = new TxtNumLimit(12.3),16,4,1,1,this,'L');
			add(new JLabel("Shortage Qty."),16,5,1,1,this,'L');
			add(txtSHRQT = new TxtNumLimit(12.3),16,6,1,1,this,'L');
			
			add(new JLabel("Tank No."),17,1,1,1,this,'L');
			add(txtTNKNO = new TxtLimit(10),17,2,1,1,this,'L');
			add(new JLabel("Lot No."),17,3,1,1,this,'L');
			add(txtQNLRF = new TxtLimit(10),17,4,1,1,this,'L');
			add(new JLabel("Acp. Tag"),17,5,1,1,this,'L');
			add(txtACPTG = new TxtLimit(1),17,6,1,1,this,'L');
			
			add(chkDRVRT = new JCheckBox("Driver Rating"),18,1,1,1,this,'L');
			add(txtRATDS = new TxtLimit(30),18,2,1,4,this,'L');
			setENBL(false);
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			crtPRESTM();
			INPVF objINPVF = new INPVF();
			txtVENCD.setInputVerifier(objINPVF);		
			//txtBOENO.setInputVerifier(objINPVF);		
			txtTNKNO.setInputVerifier(objINPVF);		
			txtACPTG.setInputVerifier(objINPVF);	
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			M_calLOCAL.add(java.util.Calendar.DATE,-1);
			txtRCTDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
	
		}
		catch(Exception E)
		{
			setMSG(E,"constrctor");
		}					
	}
	/**
	 *  Overrides super method to Enables/Disbles the screen components
	 */
	 void setENBL(boolean L_flgSTAT)
	 {
		 super.setENBL(L_flgSTAT);
		 txtBECQT.setEnabled(false);
		 txtBOEQT.setEnabled(false);
		 txtRCTQT.setEnabled(false);
		 txtMATCD.setEnabled(false);
		 txtMATDS.setEnabled(false);
		 txtUOMCD.setEnabled(false);
		 txtVENNM.setEnabled(false);
		 txtTNKNO.setEnabled(L_flgSTAT);
		 txtQNLRF.setEnabled(L_flgSTAT);
		 cmbMATTP.setEnabled(L_flgSTAT);
		 txtCHLQT.setEnabled(L_flgSTAT);
		 txtNETWT.setEnabled(L_flgSTAT);
		 txtACPTG.setEnabled(L_flgSTAT);
		 chkDRVRT.setEnabled(L_flgSTAT);
		 tblWBRVL.cmpEDITR[TBL_TPRDS].setEnabled(false);
		 tblWBRVL.cmpEDITR[TBL_OLDFL].setEnabled(false);
		 tblWBRVL.cmpEDITR[TBL_CHKFL].setEnabled(false);
		 for(int i=0;i<tblWBRVL.getRowCount();i++)
		    tblWBRVL.setValueAt("",i,TBL_DUMFL);
	}
	 /**
	 *  Overrides super method to clear the screen components
	 */
	void clrCOMP()
	{
		cmbMATTP.setSelectedIndex(0);
		txtMATCD.setText("");
		txtMATDS.setText("");
		txtCHLQT.setText("");
		txtNETWT.setText("");
		txtSHRQT.setText("");
		txtBOENO.setText("");
		txtTNKNO.setText("");
		txtQNLRF.setText("");
		txtACPTG.setText("");
		txtRATDS.setText("");
		txtUOMCD.setText("");
		txtRCTQT.setText("");
		txtBOEQT.setText("");
		txtBECQT.setText("");
		txtRCTDT.setText("");
		lblGINNO.setText("");
		txtVENCD.setText("");
		txtVENNM.setText("");
		tblWBRVL.clrTABLE();
		hstBOEVL.clear();
		hstNETQT.clear();
		tblWBRVL.setRowSelectionInterval(0,0);
		intOLDRW = 0;
		chkDRVRT.setSelected(false);
        try
        {
            M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
    		M_calLOCAL.add(java.util.Calendar.DATE,-1);
    		txtRCTDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
    		txtRCTDT.requestFocus();
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"clrCOMP");
        }
  	}
	/**
	   On Action performed on Receipt Date, Bill of entry No. data is fectched in Add /Mod mode
	*/
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					clrCOMP();
					txtRCTDT.setEnabled(true);
				}	
			}	
			if (M_objSOURC == cl_dat.M_btnUNDO_pbst)
			{
				clrCOMP();
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				M_calLOCAL.add(java.util.Calendar.DATE,-1);
				txtRCTDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				{
					setENBL(true);
					txtBOENO.setEnabled(true);
				}
				else
					setENBL(false);
				txtRCTDT.requestFocus();
			}
			else if(M_objSOURC == chkDRVRT)
			{
				if(chkDRVRT.isSelected())
				{
					txtRATDS.setEnabled(true);
					txtRATDS.requestFocus();
				}
				else
				{
					txtRATDS.setText("");
					txtRATDS.setEnabled(false);
				}
			}
			if(M_objSOURC == txtRCTDT)
			{
				strRCTDT = txtRCTDT.getText().trim();
				strFRDTM = strRCTDT+" "+"00:00";
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				M_calLOCAL.add(java.util.Calendar.DATE,1);
				strTODTM = M_fmtLCDAT.format(M_calLOCAL.getTime())+" "+"06:00";
			    clrCOMP();
			    txtRCTDT.setText(strRCTDT);
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					getDATA("","",strRCTDT);
					txtBOENO.requestFocus();
				}
				else
					txtBOENO.requestFocus();
			}
			else if(M_objSOURC == txtBOENO)
			{	
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				strBOENO = txtBOENO.getText().trim();
				
				strRCTDT = txtRCTDT.getText().trim();
				strFRDTM = strRCTDT+" "+"00:00";
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				M_calLOCAL.add(java.util.Calendar.DATE,1);
				strTODTM = M_fmtLCDAT.format(M_calLOCAL.getTime())+" "+"06:00";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
				{
					if(!strBOENO.equals(""))
					{
						vldBOENO(strBOENO);
					}
					else
						txtBOENO.requestFocus();
				}
				else 
				{
					if(!strBOENO.equals(""))
						vldBOENO(strBOENO);
					getDATA(txtBOENO.getText().trim(),"",strRCTDT);	
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(strVENCD.equals(""))
						txtVENCD.requestFocus();
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	/**
	   On Mouse pressed event,data related to old selected row is put into Hashtable
	   Data for the selected row is displayed in the textfields below the JTable  
	*/
	public void mousePressed(MouseEvent L_ME)
	{
		try
		{
			super.mousePressed(L_ME);		
			setMSG("",'N');
			if(L_ME.getSource().equals(tblWBRVL)) 
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
				{
					intTBLRW = tblWBRVL.getSelectedRow();
					intTBLCL = tblWBRVL.getSelectedColumn();
					if(intOLDRW != intTBLRW)
					{
						tblWBRVL.editCellAt(intTBLRW,intTBLCL);
						if(tblWBRVL.getValueAt(intOLDRW,TBL_CHKFL).toString().equals("true"))
						{
							if(chkDATA())
							{
								// Method to put the values in the Hashtable
								strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intOLDRW,TBL_GINNO).toString().trim(),"");
								putVALUE(strGINNO);
								strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_GINNO).toString().trim(),"");
								getVALUE(strGINNO);
								lblGINNO.setText("Details for " + strGINNO);
								intOLDRW = intTBLRW;
							}
						}
						else
						{
							// Method to put the values in the Hashtable
							strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intOLDRW,TBL_GINNO).toString().trim(),"");
							putVALUE(strGINNO);
							strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_GINNO).toString().trim(),"");
							getVALUE(strGINNO);
							lblGINNO.setText("Details for " + strGINNO);
							intOLDRW = intTBLRW;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"mousePressed ");
		}
	}
	public void keyTyped(KeyEvent L_KE)
	{
		//tblWBRVL.getCellEditor(1,1).cancelCellEditing();
	}
	/**
	    Functionality for +(plus) key and -(minus) key is included.
	    Total receipt qty. is displayed in Text Field accordingly
	*/
	public void keyReleased(KeyEvent L_KE)
	{
		try
		{
			super.keyReleased(L_KE);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				if(M_objSOURC == tblWBRVL.cmpEDITR[TBL_GINNO])
				{
					strGINNO = tblWBRVL.getValueAt(intTBLRW,intTBLCL).toString();
					if(!strGINNO.equals(""))
					{
						try
						{
							dblRCTQT = Double.parseDouble(nvlSTRVL(txtRCTQT.getText().trim(),"0"));
							if(txtBOENO.getText().trim().equals(""))
							{
								txtBECQT.setText("0");
								txtBOEQT.setText("0");
								strBENQT ="0";
							}
							dblBECQT = Double.parseDouble(nvlSTRVL(txtBECQT.getText().trim(),"0"));
							dblBENQT = Double.parseDouble(nvlSTRVL(strBENQT,"0"));
							dblUOMQT = Double.parseDouble(txtNETWT.getText().trim());
							dblBCHQT = Double.parseDouble(nvlSTRVL(txtCHLQT.getText().trim(),"0"));
						}
						catch(NumberFormatException e)
						{
							txtNETWT.requestFocus();
							setMSG("Enter Net Qty in valid decimal format",'E');
						}
						int  L_KEY = L_KE.getKeyCode();
						if(L_KEY == 61 || L_KEY == 109) //- (minus key)
						{		
						  	strCHKFL = tblWBRVL.getValueAt(intTBLRW,TBL_CHKFL).toString();
							if(strCHKFL.equals("true"))
							{
								txtRCTQT.setText(setNumberFormat((dblRCTQT - dblUOMQT),3));
								txtBECQT.setText(setNumberFormat((dblBECQT - dblBCHQT),3));
								strBENQT = String.valueOf(dblBENQT - dblUOMQT);
								tblWBRVL.setValueAt(new Boolean(false),intTBLRW,TBL_CHKFL);
								txtACPTG.setText("W");
							}
						}
						else if(L_KEY == 45 || L_KEY == 107) //+(plus key)
						{
						   	strCHKFL = tblWBRVL.getValueAt(intTBLRW,TBL_CHKFL).toString();
							if(strCHKFL.equals("false"))
							{
								// To set the balance Qty
								if(intTBLCL != TBL_CHKFL)
								{
									strCHKFL = tblWBRVL.getValueAt(intTBLRW,TBL_CHKFL).toString();
									if(txtBOENO.getText().trim().length()>0)
									{
										if(Double.parseDouble(setNumberFormat((dblBECQT + dblBCHQT),3))>Double.parseDouble(txtBOEQT.getText().trim()))
										{
											setMSG("Total Challan qty. is exceeding Bill of entry qty..",'E');
											tblWBRVL.setValueAt(new Boolean(false),intTBLRW,TBL_CHKFL);	
										}
										else
										{
											tblWBRVL.setValueAt(new Boolean(true),intTBLRW,TBL_CHKFL);
											txtBECQT.setText(setNumberFormat((dblBECQT + dblBCHQT),3));
											txtRCTQT.setText(setNumberFormat((dblRCTQT + dblUOMQT),3));
											strBENQT = String.valueOf(dblBENQT + dblUOMQT);
											tblWBRVL.setValueAt(new Boolean(true),intTBLRW,TBL_CHKFL);	
										}
									}
									else
									{
										tblWBRVL.setValueAt(new Boolean(true),intTBLRW,TBL_CHKFL);
										txtRCTQT.setText(setNumberFormat((dblRCTQT + dblUOMQT),3));
										//txtBECQT.setText(setFMT(String.valueOf(dblRCTQT + dblUOMQT),3));
									}
									txtACPTG.setText("A");
								}
							}
						}
						strGINNO = tblWBRVL.getValueAt(intTBLRW,intTBLCL).toString();
						tblWBRVL.setValueAt(strGINNO.substring(0,strGINNO.length()),intTBLRW,intTBLCL);
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyReleased");
		}
	}
	/**
	   Functions for Help, Focus Navigation (VK_Enter) is used
	   On VK_Up /VK_down Key data for the selected gate in is displyed from hashtable
	*/
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			setMSG("",'N');
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{	
				if(M_objSOURC == txtBOENO)
				{		
					cl_dat.M_flgHELPFL_pbst = true;
					strHLPFLD = "txtBOENO";
					String L_staARRHD[] = {"B/E No","B/E Date","B/E Qty","Mat.Type"};
					M_strSQLQRY = "Select BE_BOENO,BE_BOEDT,BE_BOEQT,BE_MATTP";
					M_strSQLQRY += " from MM_BETRN";    
					M_strSQLQRY += " where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(BE_STSFL,'') <> 'X'" ;
					if(txtBOENO.getText().trim().length() >0)
					    M_strSQLQRY += " AND BE_BOENO like '"+txtBOENO.getText()+"%'";
					M_strSQLQRY += " order by BE_BOEDT desc";
					cl_hlp(M_strSQLQRY,1,1,L_staARRHD,4,"CT");
				}
				else if(M_objSOURC == txtVENCD)
				{		
					strHLPFLD = "txtVENCD";
					hlpVENCD("S");
				}
				else if(M_objSOURC == txtLOCCD)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					strHLPFLD = "txtLOCCD";
					String L_staARRHD[] = {"Code","Location"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " and CMT_CGSTP = 'QC11TKL'";
					if(txtLOCCD.getText().length() >0)
					    M_strSQLQRY += " and CMT_CODCD like '"+txtLOCCD.getText() +"%'";
					cl_hlp(M_strSQLQRY,2,1,L_staARRHD,2,"CT");
				}
				else if(M_objSOURC == txtTPRCD)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					strHLPFLD = "txtTPRCD";
					hlpVENCD("T");
				}
				else if(M_objSOURC == txtACPTG)	
				{
					cl_dat.M_flgHELPFL_pbst = true;
					strHLPFLD = "txtACPTG";
					String L_staARRHD[] = {"Code","Description"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " and CMT_CGSTP = 'MMXXACP'";
					cl_hlp(M_strSQLQRY,2,2,L_staARRHD,2,"CT");
				}
				else if(M_objSOURC == txtTNKNO)	
				{
				    cl_dat.M_flgHELPFL_pbst = true;
        			strHLPFLD = "txtTNKNO";
        			String L_staARRHD[] = {"Tank No","Material"};
        			M_strSQLQRY = "Select TK_TNKNO,TK_MATDS from MM_TKMST";
        			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_STSFL <> 'X'" ;
        			if(txtMATCD.getText().length() >0)
        			    M_strSQLQRY += " AND TK_MATCD ='"+txtMATCD.getText().trim() +"'";
        			if(txtTNKNO.getText().trim().length() >0)
					    M_strSQLQRY += " AND TK_TNKNO like '"+txtTNKNO.getText()+"%'";
            		cl_hlp(M_strSQLQRY,1,1,L_staARRHD,2,"CT");
        		}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
			    if(M_objSOURC == txtCHLQT)
				{
				    txtNETWT.requestFocus();
				    setMSG("Enter Net Qty. ..",'E');
				}
				else if(M_objSOURC == txtNETWT)
				{
				    txtSHRQT.requestFocus();
				    txtSHRQT.setText(setNumberFormat(Double.parseDouble(txtCHLQT.getText())-Double.parseDouble(txtNETWT.getText()),3));
				}
				else if(M_objSOURC == txtSHRQT)
				{
				    txtTNKNO.requestFocus();
				    setMSG("Enter Tank No. ..",'E');  
				}
				else if(M_objSOURC == txtTNKNO)
				{	
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					strBOENO = txtBOENO.getText().trim();
					strTNKNO = txtTNKNO.getText().trim();
					vldTNKNO(strTNKNO);
					strRCTDT = txtRCTDT.getText().trim();
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtQNLRF)
				{		
					intTBLRW = 0;
					intTBLCL = TBL_GINNO;
					tblWBRVL.requestFocus();
					tblWBRVL.setColumnSelectionInterval(TBL_GINNO,TBL_GINNO);
					tblWBRVL.editCellAt(intTBLRW,intTBLCL);
				}
				else if(M_objSOURC == txtACPTG)
				{		
					txtACPTG.setText(txtACPTG.getText().trim().toUpperCase());
					strACPTG = txtACPTG.getText().trim();
					if(!hstCODDS.containsKey((String)txtACPTG.getText()))
					{
					    setMSG("Invalid Acceptance Tag,Press F1 to select..",'E');    
					}
				}
				/*else if(M_objSOURC == txtVENCD)
				{	
				    if(!vldPRTCD("S",txtVENCD.getText()))
				    {
				        setMSG("Invalid Vendor Code..",'E');
				    }
				    else
				    {
				        txtVENNM.setText(strPRTNM);
				     }
				}*/
				else if(M_objSOURC == tblWBRVL)
				{		
					intTBLRW = tblWBRVL.getSelectedRow();
					if(intTBLRW != 0)
						intTBLRW--;
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_UP)
			{  			
			  	if(M_objSOURC == txtGINNO)
			  	{
			  	  	if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))	
					{
						intTBLRW = tblWBRVL.getSelectedRow();
						intTBLCL = tblWBRVL.getSelectedColumn();
						if(intTBLRW != 0)
							intTBLRW--;
						strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intOLDRW,TBL_GINNO).toString().trim(),"");
						if(tblWBRVL.getValueAt(intOLDRW,TBL_CHKFL).toString().equals("true")){
							if(chkDATA())
							{
								// Method to put the values in the Hashtable
								putVALUE(strGINNO);
								strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_GINNO).toString().trim(),"");
								lblGINNO.setText("Details for " + strGINNO);
								getVALUE(strGINNO);
								intOLDRW = intTBLRW;
							}
							else
							{
								tblWBRVL.setRowSelectionInterval(intOLDRW,intOLDRW);
								tblWBRVL.editCellAt(intOLDRW,intTBLCL);
								intTBLRW = intOLDRW;
							}
						}
						else
						{
							// Method to put the values in the Hashtable
							putVALUE(strGINNO);
							strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_GINNO).toString().trim(),"");
							lblGINNO.setText("Details for " + strGINNO);
							getVALUE(strGINNO);
							intOLDRW = intTBLRW;
						}
					}
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_DOWN) 			// Down Arrow
			{ 
			  	if(M_objSOURC == txtGINNO)
			  	{
			  	  	if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
					{
						intTBLRW = tblWBRVL.getSelectedRow();
						intTBLCL = tblWBRVL.getSelectedColumn();
						if(intTBLRW != intROWCT)
							intTBLRW++;
						strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intOLDRW,TBL_GINNO).toString().trim(),"");
						if(tblWBRVL.getValueAt(intOLDRW,TBL_CHKFL).toString().equals("true"))
						{
							if(chkDATA())
							{
								// Method to put the values in the Hashtable
								putVALUE(strGINNO);
								strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_GINNO).toString().trim(),"");
								lblGINNO.setText("Details for " + strGINNO);
								getVALUE(strGINNO);
								intOLDRW = intTBLRW;
							}
							else
							{
								tblWBRVL.setRowSelectionInterval(intOLDRW,intOLDRW);
								tblWBRVL.editCellAt(intOLDRW,intTBLCL);
								intTBLRW = intOLDRW;
							}
						}
						else
						{
							// Method to put the values in the Hashtable
							putVALUE(strGINNO);
							strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_GINNO).toString().trim(),"");
							lblGINNO.setText("Details for " + strGINNO);
							getVALUE(strGINNO);
							intOLDRW = intTBLRW;
						}
					}
				}
			}
			else
			{
				//tblWBRVL.getCellEditor(1,1).cancelCellEditing();
				strGINNO = tblWBRVL.getValueAt(intOLDRW,intTBLCL).toString();
				//tblWBRVL.getCellEditor(1,1).cancelCellEditing();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	/* 
	    Method to put the values in the HashTable
	    Material Code,Description ,UOM,Challan qty, Net Weight,Shortage,Tank No,Accepted
	    Tag, Driver rating, Description,Matreil type etc is displayed
	*/
	private void putVALUE(String P_strGINNO)
	{
		try
		{
			String L_strDATA = "",L_strKEYVL = "";
			String L_strCHLQT,L_strUOMQT,L_strSHRQT,L_strACCFL,L_strDRVRT;
			String L_strMATCD,L_strMATDS,L_strRATDS,L_strACPDS,L_strUOMCD,L_strTNKNO,L_strLOTNO,L_strMATTP,L_strGRNTP="",L_strLRYNO;
			String L_strCHLNO,L_strTRNCD="",L_strTRNNM,L_strRECQT,L_strEXCCT,L_strCHLDT;
			
			L_strCHLQT = nvlSTRVL(txtCHLQT.getText().trim()," ");
			L_strUOMQT = nvlSTRVL(txtNETWT.getText().trim()," ");
			L_strMATCD = nvlSTRVL(txtMATCD.getText().trim()," ");
			L_strMATDS = nvlSTRVL(txtMATDS.getText().trim()," ");
			L_strSHRQT = nvlSTRVL(txtSHRQT.getText().trim()," ");
			L_strACCFL = nvlSTRVL(txtACPTG.getText().trim()," ");
			if(chkDRVRT.isSelected())
				L_strDRVRT = "Y";
			else
				L_strDRVRT = "N";
			L_strRATDS = nvlSTRVL(txtRATDS.getText().trim()," ");
			L_strUOMCD = nvlSTRVL(txtUOMCD.getText().trim()," ");
			
			L_strTNKNO = nvlSTRVL(txtTNKNO.getText().trim()," ");
			L_strLOTNO = nvlSTRVL(txtQNLRF.getText().trim()," ");
			L_strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,2);
			   L_strLRYNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_LRYNO).toString().trim(),"");
			   L_strCHLNO = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_CHLNO).toString().trim(),"");
			   L_strTRNCD = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_TPRCD).toString().trim(),"");
			   L_strTRNNM = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_TPRDS).toString().trim(),"");
			   L_strRECQT = txtNETWT.getText().trim();
			   L_strEXCCT = L_strMATTP;
			   L_strCHLDT = nvlSTRVL(tblWBRVL.getValueAt(intTBLRW,TBL_CHLDT).toString().trim(),"");	
			
			// Storing Bill of Entry Data
			L_strDATA = L_strCHLQT;
			L_strDATA += "|" + L_strUOMQT;
			L_strDATA += "|" + L_strMATCD;
			L_strDATA += "|" + L_strMATDS;
			L_strDATA += "|" + L_strSHRQT;
			L_strDATA += "|" + L_strACCFL;
			L_strDATA += "|" + L_strDRVRT;
			L_strDATA += "|" + L_strRATDS;
			L_strDATA += "|" + L_strUOMCD+"|";
		
			L_strDATA += L_strTNKNO + "|";
			L_strDATA += L_strLOTNO + "|";
			L_strDATA += L_strMATTP + "|";

/*			if(L_strMATTP.trim().equals("01"))
				L_strGRNTP =LM_EXBGR;
			else if(L_strMATTP.trim().equals("03"))
				L_strGRNTP =LM_ADVGR;*/
				L_strGRNTP ="X";
				//////
				L_strDATA += L_strGRNTP + "|";
				L_strDATA += L_strLRYNO + "|";
				L_strDATA += L_strCHLNO + "|";
				L_strDATA += L_strTRNCD + "|";
				L_strDATA += L_strTRNNM + "|";
				L_strDATA += L_strRECQT + "|";
				L_strDATA += L_strEXCCT + "|";
				L_strDATA += L_strCHLDT + "|";
	    		hstBOEVL.put(P_strGINNO,L_strDATA);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"putVALUE ");
		}
	}
	/*
	    Method to get the values from the HashTable
	*/
	private void getVALUE(String P_strGINNO)
	{
		try
		{
			StringTokenizer L_strKEYVL;
		
			for(int i =0;i<staDATA.length;i++)
			{
				staDATA[i] =" ";	
			}
			int i = 0;
			
			if(!String.valueOf(hstBOEVL.get(P_strGINNO)).equals("null"))
				L_strKEYVL = new StringTokenizer(hstBOEVL.get(P_strGINNO).toString(),"|");
			else
				L_strKEYVL = null;
			if(L_strKEYVL != null)
			{
				while(L_strKEYVL.hasMoreTokens())
				{
					staDATA[i] = L_strKEYVL.nextToken().toString();
					i++;
				}
			}
			txtCHLQT.setText(staDATA[0]);
			txtNETWT.setText(staDATA[1]);
			txtMATCD.setText(staDATA[2]);
			txtMATDS.setText(staDATA[3]);
			txtSHRQT.setText(staDATA[4]);
			txtACPTG.setText(staDATA[5]);
			if(staDATA[6].equals("Y"))
				chkDRVRT.setSelected(true);
			else
				chkDRVRT.setSelected(false);
			txtRATDS.setText(staDATA[7]);
			txtUOMCD.setText(staDATA[8]);
			txtTNKNO.setText(staDATA[9]);
			txtQNLRF.setText(staDATA[10]);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getVALUE ");
		}
	}
	/* 
	Method to Update the receipts in Dip Register & WeighBridge Tables
	*/
	private boolean exeADDREC()
	{
		try
		{
			int L_intROWCT = 0;
			String L_strMSG = "Record could not be saved";
			boolean L_flgRECORD = false;
			String L_strTPRCD ="";
			String L_strTPRDS ="";
			String L_strLOCCD ="";
			cl_dat.M_flgLCUPD_pbst = true;
			dblCHLQT = dblUOMQT = 0;
			
			strMATTP = String.valueOf(cmbMATTP.getSelectedItem()).substring(0,2);
			strRCTDT = txtRCTDT.getText().trim();
			strRCTDT = strRCTDT.substring(6,10) + "-" + strRCTDT.substring(3,5) + "-" + strRCTDT.substring(0,2);
            strLUSBY = cl_dat.M_strUSRCD_pbst;			
            strLUPDT = cl_dat.M_strLOGDT_pbst.trim();
			strLUPDT = strLUPDT.substring(6,10) + "-" + strLUPDT.substring(3,5) + "-" + strLUPDT.substring(0,2);
			strVENCD = txtVENCD.getText().trim();
			strVENNM = txtVENNM.getText().trim();
			for(int i = 0; i < intROWCT; i++)
			{
				strCHKFL = tblWBRVL.getValueAt(i,TBL_CHKFL).toString();
				strOLDFL = tblWBRVL.getValueAt(i,TBL_OLDFL).toString();
		/*		//if(cl_dat.M_FLGOPT != 'M' && strCHKFL.equals("false"))	// Other than Modification
				// Addition Mode && check Flag is false, then skip that row and continue with next row;
				if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))&& strCHKFL.equals("false"))
					continue;

				if(!strOLDFL.equals("O") && strCHKFL.equals("false"))
					continue;
		*/			
				if(strCHKFL.equals("false"))
					continue;
				
				L_intROWCT++;
				strGINNO = nvlSTRVL(tblWBRVL.getValueAt(i,TBL_GINNO).toString(),"");
				L_strLOCCD = nvlSTRVL(tblWBRVL.getValueAt(i,TBL_LOCCD).toString(),"");
				L_strTPRCD = nvlSTRVL(tblWBRVL.getValueAt(i,TBL_TPRCD).toString(),"");
				L_strTPRDS = nvlSTRVL(tblWBRVL.getValueAt(i,TBL_TPRDS).toString(),"");
						
				// Get the record corresponding to given Gate-In No.
				lblGINNO.setText("Details for " + strGINNO);
				getVALUE(strGINNO); 
				strBOENO = txtBOENO.getText().trim();
				strSHRQT = txtSHRQT.getText().trim();
				strCHLQT = txtCHLQT.getText().trim();
				strUOMQT = txtNETWT.getText().trim();
				strTNKNO = txtTNKNO.getText().trim();
				strQNLRF = txtQNLRF.getText().trim();
				strACPTG = txtACPTG.getText().trim();
				if(chkDRVRT.isSelected())
					strDRVRT = "Y";
				else
					strDRVRT = "N";
				strRATDS = txtRATDS.getText().trim();
				
				dblUOMQT =0;
				dblCHLQT =0;
				// Modification
				if(strOLDFL.equals("O"))
				{
					if(strCHKFL.equals("false"))
					{
						dblCHLQT -= Double.parseDouble(strCHLQT);
						dblUOMQT -= Double.parseDouble(strUOMQT);
					}
					else
					{
						strONETFL = hstNETQT.get(strGINNO).toString();
						dblCHLQT += Double.parseDouble(strCHLQT);
						dblUOMQT += Double.parseDouble(strUOMQT) - Double.parseDouble(strONETFL);
					}
				}
				else if(strCHKFL.equals("true"))
				{
					dblCHLQT += Double.parseDouble(strCHLQT);
					dblUOMQT += Double.parseDouble(strUOMQT);
				}
				pstmWBTRN.setString(1,strBOENO);
				pstmWBTRN.setDouble(2,Double.parseDouble(strCHLQT));
				pstmWBTRN.setDouble(3,Double.parseDouble(strUOMQT));
				pstmWBTRN.setString(4,strTNKNO);
				pstmWBTRN.setString(5,strQNLRF);
				pstmWBTRN.setString(6,strACPTG);
				pstmWBTRN.setDate(7,java.sql.Date.valueOf(strRCTDT));
				pstmWBTRN.setString(8,strDRVRT);
				pstmWBTRN.setString(9,strRATDS);
				pstmWBTRN.setString(10,strMATTP);
				pstmWBTRN.setString(11,strTRNFL_fn);
				pstmWBTRN.setString(12,strLUSBY);
				pstmWBTRN.setString(13,L_strLOCCD);
				pstmWBTRN.setString(14,L_strTPRCD);
				pstmWBTRN.setString(15,L_strTPRDS);
				pstmWBTRN.setString(16,strVENCD);
				pstmWBTRN.setString(17,strVENNM);
				pstmWBTRN.setDate(18,java.sql.Date.valueOf(strLUPDT));
				pstmWBTRN.setString(19,strGINNO);
				pstmWBTRN.addBatch();
				// Dip
				pstmDPTRN.setDouble(1,dblUOMQT);
				pstmDPTRN.setString(2,strTRNFL_fn);
				pstmDPTRN.setString(3,strLUSBY);
				pstmDPTRN.setDate(4,java.sql.Date.valueOf(strLUPDT));
				pstmDPTRN.setString(5,strREGTP_fn);
				pstmDPTRN.setDate(6,java.sql.Date.valueOf(strRCTDT));
				pstmDPTRN.setString(7,strTNKNO);
				pstmDPTRN.addBatch();
			}
			if(L_intROWCT > 0)
			{
				int rows[] = pstmWBTRN.executeBatch();
				pstmWBTRN.clearParameters();
				if(rows.length == L_intROWCT)
				{
					// Update Received Qty in Dip Register
					int rows1[] = pstmDPTRN.executeBatch();
					pstmDPTRN.clearParameters();
					if(rows1.length == L_intROWCT)
						cl_dat.M_flgLCUPD_pbst = true;
					else
					{
						L_strMSG = "Dip is not entered";
						cl_dat.M_flgLCUPD_pbst = false;
					}
				}
				else
					cl_dat.M_flgLCUPD_pbst = false;
				
				if(cl_dat.M_flgLCUPD_pbst)
				{
				   if(cl_dat.exeDBCMT("exeSAVE"))
				   {
					 setMSG("Record saved successfully",'N');
					 M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
            		 M_calLOCAL.add(java.util.Calendar.DATE,-1);
            		 txtRCTDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));

					 return true;
					}
					else
					{
						setMSG("Could not save the Data..",'E');
						setMSG(L_strMSG,'E');
						return false;
					}
				}
			}
			else
			{
				setMSG("Select atleast one record",'E');
				return false;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeADDREC ");
			cl_dat.exeDBCMT("exeSAVE");
		}
		return true;
	}
	/**
	   Method to fetch data from MM_WBTRN,CO_CTMST where WB_DOCTP ='01' (Tankers) and
	   Gate in date between 
	*/
	private boolean getDATA(String P_strBOENO,String P_strTNKNO,String P_strACPDT)
	{
		try
		{
			java.sql.Date L_datTEMP;
			int i = 0;
			String L_strLOCCD,L_strGINNO,L_strCHLNO,L_strCHLQT = "0",L_strUOMQT = "0",L_strRECQT ="0",L_strCHLDT="";
			String L_strMATCD,L_strMATDS,L_strSHRQT,L_ACPTG,L_strDRVRT,L_strRATDS,L_strTNKNO,L_strLOTNO,L_strMATTP="";
			String L_strACPDS,L_strUOMCD,L_strDATA,L_strLRYNO,L_strTRNCD,L_strTRNNM,L_strEXCCT =" ",L_strGRNTP;
			
			double L_RCTQT = 0.00;			// Receipt Qty.
			double L_BECQT = 0.00;			// challan qty against bill of entry
		    tblWBRVL.clrTABLE();
			setCursor(cl_dat.M_curWTSTS_pbst);
			M_strSQLQRY = "Select WB_DOCNO,WB_TPRCD,WB_TPRDS,WB_LRYNO,WB_MATCD,WB_MATDS,WB_MATTP,CT_MATCD,CT_UOMCD,";
			M_strSQLQRY += "WB_CHLNO,WB_CHLDT,WB_CHLQT,WB_UOMQT,WB_LOCCD,WB_BOENO,WB_TNKNO,";
			M_strSQLQRY += "WB_QNLRF,WB_ACPTG,WB_DEFFL,WB_DEFDS,WB_CONNO,WB_PRTCD,WB_PRTDS";
			M_strSQLQRY += " from MM_WBTRN,CO_CTMST where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CT_MATCD = WB_MATCD";
			M_strSQLQRY += " AND WB_DOCTP = '" + strDOCTP_fn + "'";
			M_strSQLQRY += " and isnull(WB_STSFL,'') <> 'X'";
			if(tblWBRVL.isEditing())
			{
			    tblWBRVL.getCellEditor().stopCellEditing();
			    tblWBRVL.setRowSelectionInterval(0,0);
			    tblWBRVL.setColumnSelectionInterval(0,0);
			    }
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) ||
			   cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))	
			{
				M_strSQLQRY += " AND isnull(WB_ACPTG,'') <>'A'";
				M_strSQLQRY += " and ((WB_GINDT between '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(strFRDTM)) +"' AND '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(strTODTM))+"'";												   
				M_strSQLQRY += ") or (WB_GINDT < '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strFRDTM))+"'))";
				M_strSQLQRY += " and isnull(WB_DOCRF,'') =''"; // DOCRF IS NULL
			}
			else
			{
				if(!P_strBOENO.equals(""))
				{
					M_strSQLQRY += " and WB_BOENO = '" + P_strBOENO + "'";
				}
				M_strSQLQRY += " and WB_ACPTG = 'A'";
				M_strSQLQRY += " and WB_DOCRF is null";
				
			}
			M_strSQLQRY += " order by WB_ACPTG,WB_DOCNO";
			System.out.println(M_strSQLQRY);
	        rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(rstRSSET1.next())
			{
				
				L_strMATTP = nvlSTRVL(rstRSSET1.getString("WB_MATTP"),"");
				L_strGINNO = nvlSTRVL(rstRSSET1.getString("WB_DOCNO"),"");
				L_strTRNCD = nvlSTRVL(rstRSSET1.getString("WB_TPRCD"),"");
				L_strTRNNM = nvlSTRVL(rstRSSET1.getString("WB_TPRDS"),"");
				L_strLRYNO = nvlSTRVL(rstRSSET1.getString("WB_LRYNO"),"");
				L_strMATCD = nvlSTRVL(rstRSSET1.getString("WB_MATCD"),"");
				L_strMATDS = nvlSTRVL(rstRSSET1.getString("WB_MATDS"),"");
				L_strUOMCD =nvlSTRVL(rstRSSET1.getString("CT_UOMCD"),"");
				L_strCHLQT = nvlSTRVL(rstRSSET1.getString("WB_CHLQT"),"0");
				L_strUOMQT = nvlSTRVL(rstRSSET1.getString("WB_UOMQT"),"0");
				L_strTNKNO = nvlSTRVL(rstRSSET1.getString("WB_TNKNO"),"");
				L_ACPTG = nvlSTRVL(rstRSSET1.getString("WB_ACPTG")," ");
				L_strLOTNO = nvlSTRVL(rstRSSET1.getString("WB_QNLRF")," ");
				L_strCHLNO = nvlSTRVL(rstRSSET1.getString("WB_CHLNO"),"");
				L_datTEMP = rstRSSET1.getDate("WB_CHLDT");
				if(L_datTEMP !=null)
				{
					L_strCHLDT = M_fmtLCDAT.format(L_datTEMP);
				}
				if(nvlSTRVL(L_strMATTP,"").length() >0)
				    cmbMATTP.setSelectedIndex(Integer.parseInt(L_strMATTP));
				tblWBRVL.setValueAt(L_strGINNO,i,TBL_GINNO);
				tblWBRVL.setValueAt(L_strTRNCD,i,TBL_TPRCD);
				tblWBRVL.setValueAt(L_strTRNNM,i,TBL_TPRDS);
				tblWBRVL.setValueAt(L_strLRYNO,i,TBL_LRYNO);
				tblWBRVL.setValueAt(L_strCHLNO,i,TBL_CHLNO);
				tblWBRVL.setValueAt(L_strCHLDT,i,TBL_CHLDT);
				L_strSHRQT ="0";
				//L_strSHRQT =setNumberFormat(Double.parseDouble(txtCHLQT.getText()) - Double.parseDouble(txtNETWT.getText()),3);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(L_ACPTG.equals("A"))
					{
						tblWBRVL.setValueAt(new Boolean(true),i,TBL_CHKFL);
						tblWBRVL.setValueAt("O",i,TBL_OLDFL);
						L_RCTQT += Double.parseDouble(L_strUOMQT);
						L_BECQT += Double.parseDouble(L_strCHLQT);
					}
				}
				tblWBRVL.setValueAt(nvlSTRVL(rstRSSET1.getString("WB_LOCCD"),""),i,TBL_LOCCD);
				L_strDRVRT = nvlSTRVL(rstRSSET1.getString("WB_DEFFL")," ");
				L_strRATDS = nvlSTRVL(rstRSSET1.getString("WB_DEFDS")," ");
				L_strEXCCT = L_strMATTP;	
				
				L_strDATA = L_strCHLQT + "|" + L_strUOMQT + "|" + L_strMATCD + "|" + L_strMATDS + "|";
				L_strDATA += L_strSHRQT + "|";
				L_strDATA += L_ACPTG + "|" + L_strDRVRT + "|" + L_strRATDS + "|";
				L_strDATA += L_strUOMCD + "|";
				L_strDATA += L_strTNKNO + "|";
				L_strDATA += L_strLOTNO + "|";
				L_strDATA += L_strMATTP + "|";
				L_strGRNTP ="X";	
				L_strDATA += L_strGRNTP + "|";
				L_strDATA += L_strLRYNO + "|";
				L_strDATA += L_strCHLNO + "|";
				L_strDATA += L_strTRNCD + "|";
				L_strDATA += L_strTRNNM + "|";
				L_strDATA += L_strRECQT + "|";
				L_strDATA += L_strEXCCT + "|";
				L_strDATA += L_strCHLDT + "|";
	      		hstBOEVL.put(L_strGINNO,L_strDATA);
				hstNETQT.put(L_strGINNO,L_strUOMQT);
				i++;
			}
			if(rstRSSET1 != null)
				rstRSSET1.close();
			if(i == 0 )
			{
				setMSG("Record could not be found.",'E');
				return false;
			}
			else
			{
				// To display the Chalan Quantity of first Record
				L_strGINNO = tblWBRVL.getValueAt(0,TBL_GINNO).toString().trim();
				lblGINNO.setText("Details for " + L_strGINNO);
				StringTokenizer L_strKEYVL;
				L_strKEYVL = new StringTokenizer(hstBOEVL.get(L_strGINNO).toString(),"|");
				String L_staVALUE[] = {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "};
				i =0 ;
				while(L_strKEYVL.hasMoreTokens())
				{
					L_staVALUE[i] = L_strKEYVL.nextToken().toString();
					i++;
				}
				txtCHLQT.setText(L_staVALUE[0]);
				txtNETWT.setText(L_staVALUE[1]);
				txtMATCD.setText(L_staVALUE[2]);
				txtMATDS.setText(L_staVALUE[3]);
				txtSHRQT.setText(L_staVALUE[4]);
				txtACPTG.setText(L_staVALUE[5]);
				if(L_staVALUE[6].equals("Y"))
					chkDRVRT.setSelected(true);
				else
					chkDRVRT.setSelected(false);
				txtRATDS.setText(L_staVALUE[7]);
				txtUOMCD.setText(L_staVALUE[8]);
				txtTNKNO.setText(L_staVALUE[9]);
				txtQNLRF.setText(L_staVALUE[10]);
				txtRCTQT.setText(setNumberFormat(L_RCTQT,3));
				//if(cl_dat.M_FLGOPT == 'M')
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					L_BECQT = Double.parseDouble(strBOCQT)+L_BECQT;
					txtBECQT.setText(setNumberFormat(L_BECQT,3));
					strBENQT = setNumberFormat(L_RCTQT,3);
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		return true;
	}
	/**
	   Method for displaying help from Party master table, for Vendor and Transporter
	   @param P_strPRTTP : Party Type
	*/
	private void hlpVENCD(String P_strPRTTP)
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = true;
			String L_staARRHD[] = {"Supplier Code","Supplier Description"};
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM";
			M_strSQLQRY += " from CO_PTMST";    
			M_strSQLQRY += " where PT_PRTTP ='"+P_strPRTTP+"' and PT_STSFL <> 'X'" ;
			if(P_strPRTTP.equals("S"))
			{
			    if(txtVENCD.getText().length() >0)
				    M_strSQLQRY += " and PT_PRTCD like '"+txtVENCD.getText() +"%'";
			}
			else if(P_strPRTTP.equals("T"))
			{
			    if(txtTPRCD.getText().length() >0)
				    M_strSQLQRY += " and PT_PRTCD like '"+txtTPRCD.getText() +"%'";
			 }
			M_strSQLQRY += " order by PT_PRTCD";
			cl_hlp(M_strSQLQRY,2,1,L_staARRHD,2,"CT");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"hlpVENCD");
		}
	}
/**
   Validation for Vendor code and Transporter code
   @param P_strPRTTP : Party Type 
   @param P_strPRTCD : Party Code
*/	
 private boolean vldPRTCD(String P_strPRTTP,String P_strPRTCD)
{
		try
		{
			strPRTNM ="";
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM";
			M_strSQLQRY += " from CO_PTMST";    
			M_strSQLQRY += " where PT_PRTTP ='"+P_strPRTTP+"' and PT_STSFL <> 'X'" ;
			M_strSQLQRY += " and PT_PRTCD = '"+P_strPRTCD +"'";
			System.out.println(M_strSQLQRY);
		    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		    if(M_rstRSSET !=null)
		    {
		        if(M_rstRSSET.next())
		        {
		           strPRTNM = M_rstRSSET.getString("PT_PRTNM");
		           txtVENNM.setText(strVENNM);
		           //System.out.println("1 "+strPRTNM);
		           return true; 
		        } 
		        
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"hlpVENCD");
			return false;
		}
		return false;
	}
    /**
     Validation for Bill of Entry No. from MM_BETRN table
     @param P_stRBOENO : Bill of entry number
    */
	public boolean vldBOENO(String P_strBOENO)
	{	
		try
		{
			//System.out.println("callijg vldBOENO");
			M_strSQLQRY = "Select BE_BOENO,BE_BOEQT,BE_CHLQT,BE_NETQT,BE_MATTP,BE_CONNO,BE_PORNO,PO_VENTP,PO_VENCD,PT_PRTNM";
			M_strSQLQRY += " from MM_BETRN,MM_POMST,CO_PTMST";    
			M_strSQLQRY += " WHERE BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_PORNO = PO_PORNO AND BE_CMPCD = PO_CMPCD AND PT_PRTTP ='S' AND PT_PRTCD = PO_VENCD";
			M_strSQLQRY += " AND BE_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_strSQLQRY += " AND BE_BOENO = '" + P_strBOENO + "'";
			M_strSQLQRY += " and BE_STSFL <> 'X'" ;
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				strBOEQT = setNumberFormat(M_rstRSSET.getDouble("BE_BOEQT"),3);
				strBOCQT = setNumberFormat(M_rstRSSET.getDouble("BE_CHLQT"),3);
				strBENQT ="0";
				LM_OBECQT = strBOCQT;
				txtBOEQT.setText(strBOEQT);
				txtBECQT.setText(strBOCQT);
				strBEMAT = nvlSTRVL(M_rstRSSET.getString("BE_MATTP"),"");
				strVENCD = nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),"");
				txtVENCD.setText(strVENCD);
				strVENNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
				txtVENNM.setText(strVENNM);
				cmbMATTP.setSelectedIndex(Integer.parseInt(strBEMAT));
				return true;
			}
			else
			setMSG("Invalid Bill of Entry No.Press F1 for help",'E');
			txtBOENO.requestFocus();
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldBOENO");
		}
		return false;
	}
	/**
	* Check Tank No. for validity from MM_TKMST
	  @param P_strTNKNO - given Tank Number
	*/
	public boolean vldTNKNO(String P_strTNKNO)
	{	
		try
		{
			M_strSQLQRY = "Select TK_TNKNO from MM_TKMST";
			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_TNKNO = '" + P_strTNKNO + "'";
			M_strSQLQRY += " and isnull(TK_STSFL,'') <> 'X'";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					M_rstRSSET.close();
					return true;
				}
				M_rstRSSET.close();
			}
			setMSG("Invalid Tank No.Press F1 for help",'E');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTNKNO");
		}
		return false;
	}
	/**
	 *  This method sets the value of Data seleted from F1 Help
	 * 
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(strHLPFLD.equals("txtBOENO"))
			{	
				txtBOENO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(strHLPFLD.equals("txtVENCD"))
			{			
				String L_HLPSTR = getSTRHLP();
				strVENCD =cl_dat.M_strHLPSTR_pbst;
				strVENNM =L_HLPSTR.substring(L_HLPSTR.indexOf("|"));
				strVENNM =strVENNM.substring(1,strVENNM.length() -1);
				txtVENCD.setText(strVENCD);
				txtVENNM.setText(strVENNM);
			}
			else if(strHLPFLD.equals("txtLOCCD"))
			{			
			    txtLOCCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(strHLPFLD.equals("txtTPRCD"))
			{
			    txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);
			    strPRTNM ="";
			    vldPRTCD("T",cl_dat.M_strHLPSTR_pbst);
			    tblWBRVL.setValueAt(strPRTNM,tblWBRVL.getSelectedRow(),TBL_TPRDS);
			}
			else if(strHLPFLD.equals("txtTNKNO"))
			{
				txtTNKNO.setText(cl_dat.M_strHLPSTR_pbst);
				strBOENO = txtBOENO.getText().trim();
				strTNKNO = txtTNKNO.getText().trim();
				strRCTDT = txtRCTDT.getText().trim();
			}
			else if(strHLPFLD.equals("txtACPTG"))
			{			
				txtACPTG.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeHLPOK");
		}
	}
	/*
	 * Method to check the validity of data, before storing it into a hashtable
	 * Values from hashtable are taken at the time of saving to database   
	*/
	private boolean chkDATA()
	{
		try
		{
			if(txtRCTQT.getText().length() ==0)
			{
				setMSG("Enter Receipt Quantity..",'E');
				txtRCTQT.requestFocus();
				return false;
			}
			else if(txtCHLQT.getText().length() ==0)
			{
				setMSG("Enter Challan Quantity..",'E');
				txtCHLQT.requestFocus();
				return false;
			}
			else if(txtNETWT.getText().length() ==0)
			{
				setMSG("Enter Net Quantity..",'E');
				txtNETWT.requestFocus();
				return false;
			}
			if(txtMATCD.getText().equals(M_strCODSTY_fn))
			{
			    if(txtBOENO.getText().length() ==0)
    			{
    				setMSG("Enter Bill Of Entry No..",'E');
    				txtBOENO.requestFocus();
    				return false;
    			} 
			}
			String L_strDRVRT="";
			if(chkDRVRT.isSelected())
				L_strDRVRT = "Y";
			else
				L_strDRVRT = "N";
			if(!hstCODDS.containsKey((String)txtACPTG.getText())) // Aceepted tag validation
			{					
				tblWBRVL.setRowSelectionInterval(intOLDRW,intOLDRW);
				tblWBRVL.editCellAt(intOLDRW,intTBLCL);
				txtACPTG.requestFocus();
				return false;
			}
			if(chkDRVRT.isSelected())
			{				
				if(txtRATDS.getText().trim().length() == 0)
				{
					tblWBRVL.setRowSelectionInterval(intOLDRW,intOLDRW);
					tblWBRVL.editCellAt(intOLDRW,intTBLCL);
					txtRATDS.requestFocus();
					setMSG("Please enter the Reason",'E');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
			return false;
		}
		return true;
	}
	
	/**
	 *  Method to create the PreparedStatements
	 *  Creates prepared statement for updating in WBTRN Table for given Gate-In No.,
	    Creates prepared statement for updating Receipt Qty in Dip Table for given Memo date and Tank No.
	*/ 
	private void crtPRESTM()
	{
		try
		{
			// Statement to update WeighBridge Transaction table
			pstmWBTRN = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"Update MM_WBTRN set WB_BOENO = ?,WB_CHLQT = ?,WB_UOMQT = ?,"+
						"WB_TNKNO = ?,WB_QNLRF = ?,WB_ACPTG = ?,WB_ACPDT = ?," +
						"WB_DEFFL = ?,WB_DEFDS = ?,WB_MATTP = ?,WB_TRNFL = ?,WB_LUSBY = ?," +
						"WB_LOCCD =?,WB_TPRCD =?,WB_TPRDS =?,"+
						"WB_PRTCD =?,WB_PRTDS =?,"+
						"WB_LUPDT = ? where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strDOCTP_fn + "' and WB_DOCNO = ?"
						);
			// Statement to Dip Entry table
			pstmDPTRN = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"Update MM_DPTRN set DP_RCTQT = DP_RCTQT + ?,DP_TRNFL = ?," +
						"DP_LUSBY = ?,DP_LUPDT = ? where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = ? and " +
						"CONVERT(varchar,DP_MEMDT,103) = ? and DP_TNKNO = ?"
						);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"crePRESTM");
		}
	}
	/**
	 *  Method to check validity of Data, before saving
	 * */
	boolean vldDATA()
	{
		try
		{
			strBOENO = txtBOENO.getText().trim();
			strTNKNO = txtTNKNO.getText().trim();
            if(txtVENCD.getText().trim().length() ==0)
			{
				setMSG("Enter Party Code..",'E');
				return false;
			}
            if(txtVENNM.getText().trim().length() ==0)
			{
				setMSG("Party Name can not be blank,Press Enter on Party Code..",'E');
				return false;
			}			
            if(txtBOENO.getText().length()==0)
			{
				if(txtVENCD.getText().trim().length() ==0)
				{
					setMSG("Enter Party Code..",'E');
					return false;
				}
			}
			if(cmbMATTP.getSelectedItem().equals(LM_SELECT))
			{
				setMSG("Please select the Material Category",'E');
				return false;
			}
		//	if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		//		return true;
			   
			for(int i = 0; i < intROWCT; i++)
			{
			    if(tblWBRVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			    {
			    	if(tblWBRVL.getValueAt(i,TBL_LOCCD).toString().length() == 0)
			    	{
			    	    setMSG("Enter Location code for Gate In No."+tblWBRVL.getValueAt(i,TBL_GINNO).toString(),'E');
			    	    return false;    
			    	}
			    	else if(tblWBRVL.getValueAt(i,TBL_TPRCD).toString().equals(" "))
			    	{
			    	    setMSG("Enter Transporter code for Gate In No."+tblWBRVL.getValueAt(i,TBL_GINNO).toString(),'E');
			    	    return false;    
			    	}
                    else if(tblWBRVL.getValueAt(i,TBL_TPRCD).toString().equals("Z9999"))
			    	{
			    	    setMSG("Enter Transporter code for Gate In No."+tblWBRVL.getValueAt(i,TBL_GINNO).toString(),'E');
			    	    return false;    
			    	}
			    }
			}
			if(tblWBRVL.isEditing())
			{
			    tblWBRVL.getCellEditor().stopCellEditing();
			    tblWBRVL.setRowSelectionInterval(0,0);
			    tblWBRVL.setColumnSelectionInterval(0,0);
			}
			return true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return false;
	}
	/**
	    Method for saving the data, if vldDATA returns true
	*/
	void exeSAVE()
	{
		try
		{
    		this.setCursor(cl_dat.M_curWTSTS_pbst);
    		intOLDRW = tblWBRVL.getSelectedRow();
    		strGINNO = nvlSTRVL(tblWBRVL.getValueAt(intOLDRW,TBL_GINNO).toString().trim(),"");
    		putVALUE(strGINNO);
    		if(vldDATA())
    		{
    			if(exeADDREC())
    			{
    				clrCOMP();
    				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
    			    M_calLOCAL.add(java.util.Calendar.DATE,-1);
    			    txtRCTDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
    				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
    				{
    					setENBL(true);
    					txtBOENO.setEnabled(true);
    					txtRCTDT.requestFocus();
    				}
    				else
    				{
    					setENBL(false);
    				}
    			}
    		}
    		this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
		    setMSG(L_E,"exeSAVE");    
		    this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	class INPVF extends InputVerifier
	{
	    public boolean verify(JComponent input)
	    {
	        if(((JTextField)input).getText().length() ==0)
	            return true;
	        if(input == txtVENCD)
	        {
	            if(txtVENCD.getText().length() != 5)
	            {
	                setMSG("Invalid Vendor Code..",'E');
	                return false;    
	            }   
	            if(!vldPRTCD("S",txtVENCD.getText()))
	            {
                    setMSG("Invalid Vendor Code..",'E');  
                    return false;  	            
	            }
	            else txtVENNM.setText(strPRTNM);
	        }
	        /*else if(input == txtBOENO)
	        {
	            if(!vldBOENO(txtBOENO.getText()))
	            {
                    setMSG("Invalid Bill of Entry No...",'E');  
                    return false;  	            
	            }
	        }*/
	        else if(input == txtTNKNO)
	        {
	            if(!vldTNKNO(txtTNKNO.getText()))
	            {
                    setMSG("Invalid Tank No...",'E');  
                    return false;  	            
	            }
	        }
	        else if(input == txtACPTG)
	        {
	            if(!hstCODDS.containsKey((String)txtACPTG.getText()))
	            {
                    setMSG("Invalid Acceptance Tag...",'E');  
                    return false;  	            
	            }
	        }
	    return true;
	    }  
	}
	}