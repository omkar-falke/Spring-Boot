import javax.swing.*;import javax.swing.table.*;import java.awt.event.*;
import javax.swing.JComboBox;import javax.swing.JLabel;
import javax.swing.JTextField;import java.sql.*;import java.sql.Date;
import java.util.*;import javax.swing.InputVerifier;

class mr_tecnt extends cl_pbase
{
	
	private ButtonGroup btgCRDBT;   
	private JRadioButton rdbCREDT;	  /**Radio Button for Credit Type Note  */
	private JRadioButton rdbDEBIT;	 /**Radio Button for Debit Type Note  */
	
	private JTabbedPane tbpMAIN;
	private JComboBox cmbMKTTP;
	
	private JTextField txtPRTTP;	/** JtextField to display & enter Party Type.*/
	private JTextField txtDOCRF;	/** JtextField to display & Credit Note No.*/
	private JTextField txtDOCTP;	/** JtextField to display & enter Doc. type .*/
	private JTextArea txtREMDS;	/** JtextField to display & enter Remark.*/
	private JTextField txtPRTCD;	/** JtextField to display & enter Group Code.*/
	private JTextField txtPRTNM;	/** JtextField to display & enter Group Description.*/
	private JTextField txtTODAT;
	private JTextField txtFRDAT;
	private JCheckBox chkALINV;
	private JCheckBox chkWIINV;     /** Checkbox for "With Invoice Reference" */
	private JTextField txtINVNO;	/** JtextField to display & enter Invoice No.*/
	private JTextField txtPRDCD;	/** JtextField to display & enter Product code.*/
	private JTextField txtOTHCT;    /** Category in Other Credit Note */
	
	private JTextField txtSRLNO;	/** JtextField to display & enter Serial No.*/
	private JTextField txtCRDRT;	/** JtextField to display & enter Credit rate.*/
	private JTextField txtCRDVL;
	private JLabel lblCRDVL;
	private JTextField txtLTXVL;
//	private JTextField txtRCTVL;
	private JTextField txtTXMOD;
	
	private JTextField txtTAXCD;
	private JTextField txtPRDCD1;
	private JTextField txtPRDDS;
	private JTextField txtAGRPR;
	private JLabel lblAGRPR;
	private JLabel lblPRDCD1;
	private JLabel lblINVQT;
	private JLabel lblPGRVL;
	private JPanel pnlINVDTL; 
	private JPanel pnlCOMNTX; 
	private cl_JTable tblINVDT;
	private cl_JTable tblCOMTX;
	
	private int intTB1_CHKFL = 0;
	private int intTB1_INVNO = 1;
	private int intTB1_PRGRD = 2;
	private int intTB1_PRDDS = 3;
	private int intTB1_PKGTP = 4;
	private int intTB1_SRLNO = 5;
	private int intTB1_DOCRT = 6;
	private int intTB1_INVQT = 7;
	private int intTB1_PGRVL = 8;
	private int intTB1_ATXVL = 9;
	private int intTB1_LTXVL = 10;
	private int intTB1_PNTVL = 11;
	private int intTB1_INTVL = 12;
	private int intTB1_INVVL = 13;
	private int intTB1_OTHVL = 14;
	private int intTB1_INVQT1 = 15;
//	private int intTB1_RCTVL = 13;
	
	private int intTB2_CHKFL = 0;
	private int intTB2_TAXCD = 1;
	private int intTB2_TAXDS = 2;
	private int intTB2_TAXFL = 3;
	private int intTB2_TAXVL = 4;
	private int intTB2_TXMOD = 5;
	
	private String strTRNTP="";
	//private String strCMPCD="01";
	private String strREMDS;
	private String strOTHCN ="09";  // oTHER cREDIT nOTE
	private String strMISCN ="0Z";  // oTHER cREDIT nOTE MISC CATEGORY - WITHOUT INV. REF
	private String strDISCN ="0A";  // BOOKING DISCOUNT - WITH INV REF
	private String strPDFCN ="0B";  // PRICE DIFF. CATEGORY - WITH INV REF
	private String strEPICN ="0C";  // EARLY PAYMENT INTEREST CATEGORY - WITH INV REF
	private String strSLRCN ="0D";  // Sales Return Credit - WITH INV REF
	private String strINTDN ="31";
    private String strOTHDN ="38";
    private String strMISDN ="39";
	private String strDUMINV ="99999999";
	private String strDUMPRD ="9999999999";
	private int intRWCNT;
	private int intCOMTX;
	boolean flgREMRK;
	private TBLINPVF objTBLVRF; 
	private Vector<String> vtrTAXCD;
	private String M_strSQLQRD;
	private String strTXVLA;
	private String strTXVLD;
	private Hashtable<String,String> hstTAXCD=new Hashtable<String,String>();
	private boolean flgCOMTX;
	boolean flgPRMOD = false;
	private mr_rpcrn objREPT;
	private CallableStatement cstPLTRN;
	private CallableStatement cstPLTRN_OTH;
	private CallableStatement cstPLTRN_ODB;
	private CallableStatement cstPATRN_OTH;
	private String strYREND = "31/03/2009";
	private String strYRDGT;
	mr_tecnt()
	{
		super(2);
		setMatrix(20,8);
		tbpMAIN   = new JTabbedPane();
		cmbMKTTP=new JComboBox();
		btgCRDBT=new ButtonGroup();
		pnlINVDTL=new JPanel(null);
		setMatrix(20,8);
		pnlCOMNTX=new JPanel(null);
		
		chkALINV =new JCheckBox();
		chkWIINV =new JCheckBox();
		vtrTAXCD=new Vector<String>(2);
		add(rdbCREDT=new JRadioButton("Credit Note",true),1,1,1,1,this,'L');
		add(rdbDEBIT=new JRadioButton("Dedit Note"),1,2,1,1,this,'L');
		btgCRDBT.add(rdbCREDT);
		btgCRDBT.add(rdbDEBIT);
		
		add(new JLabel("Market Type"),2,1,1,1,this,'L');
	    add(cmbMKTTP,2,2,1,1.8,this,'L');
	
		add(new JLabel("Doc Type/Cat."),2,4,1,1,this,'L');
		add(txtDOCTP=new TxtNumLimit(2),2,5,1,0.5,this,'L');
		add(txtOTHCT=new TxtNumLimit(2),2,5,1,0.5,this,'R');
		add(new JLabel("Doc. No."),2,6,1,1,this,'L');
		add(txtDOCRF=new TxtNumLimit(8),2,7,1,1,this,'L');	
		add(new JLabel("Party Type"),3,1,1,1,this,'L');
		add(txtPRTTP=new TxtLimit(1),3,2,1,1,this,'L');
		add(new JLabel("Party Code"),3,3,1,1,this,'L');
		add(txtPRTCD=new TxtLimit(5),3,4,1,1,this,'L');
		//add(new JLabel("Party Name"),3,3,1,1,this,'L');
		add(txtPRTNM=new TxtLimit(40),3,5,1,3,this,'L');
		add(chkWIINV = new JCheckBox("Misc.Credit Note with Invoice Reference"),5,7,1,3,this,'L');
		add(chkALINV = new JCheckBox("Show All Invoices of this group"),4,7,1,3,this,'L');
		add(new JLabel("From Date"),4,1,1,1,this,'L');
		add(txtFRDAT=new TxtDate(),4,2,1,1,this,'L');	
	    add(new JLabel("To Date"),4,3,1,1,this,'L');
		add(txtTODAT=new TxtDate(),4,4,1,1,this,'L');	
		add(lblCRDVL = new JLabel("Amount"),4,5,1,1,this,'L');
		add(txtCRDVL=new TxtNumLimit(12.2),4,6,1,1,this,'L');	
		add(lblPRDCD1 = new JLabel("Grade"),5,1,1,1,this,'L');
		add(txtPRDCD1=new TxtLimit(10),5,2,1,1,this,'L');	
	    add(txtPRDDS=new TxtLimit(45),5,3,1,3,this,'L');	
	    add(lblAGRPR =new JLabel("Agreed Price"),5,6,1,1,this,'L');
		add(txtAGRPR=new TxtNumLimit(12.2),5,7,1,1,this,'L');	
		
		String L_strCODCD="";
		try
		{
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "0" : "9";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXMKT' order by CMT_NMP01";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			 if(M_rstRSSET!=null)
			 {	 
				while(M_rstRSSET.next())
				{
					System.out.println(L_strCODCD);
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODCD =L_strCODCD+" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbMKTTP.addItem(L_strCODCD);
				}
			 }
		 	if(M_rstRSSET != null)
				M_rstRSSET.close();
			//M_strSQLQRY= "Select  CMT_CODCD,CMT_CODDS  from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'COXXTAX' AND CMT_CHP01='01'";
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,3)CMT_CODCD,CMT_CODDS,CMT_CHP02,CMT_NCSVL,CMT_CHP01,CMT_NMP01  from CO_CDTRN where CMT_CGMTP='TCL' and CMT_CGSTP = 'MRXXDCM'";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					vtrTAXCD.add(M_rstRSSET.getString("CMT_CODCD"));
					hstTAXCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  "),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"  "));
				}
			}
			cstPLTRN = cl_dat.M_conSPDBA_pbst.prepareCall("{call updPLTRN_DCM(?,?,?,?,?,?)}");
			cstPLTRN_OTH = cl_dat.M_conSPDBA_pbst.prepareCall("{call updPLTRN_OTH(?,?,?,?,?,?,?,?)}");
			cstPATRN_OTH = cl_dat.M_conSPDBA_pbst.prepareCall("{call updPATRN_OTH(?,?,?)}");
			cstPLTRN_ODB = cl_dat.M_conSPDBA_pbst.prepareCall("{call updPLTRN_ODB(?,?)}");
			objREPT = new mr_rpcrn(M_strSBSCD);
		}
		catch(Exception E)
		{
		}
		add(lblINVQT = new JLabel(" "),16,4,1,0.5,this,'R');
		add(lblPGRVL = new JLabel(" "),16,5,1,1,this,'L');
		add(new JLabel("Remark"),17,1,1,1,this,'L');
		add(txtREMDS=new JTextArea(),17,2,2,5,this,'L');
		txtREMDS.setLineWrap(true);
		tblINVDT=crtTBLPNL1(pnlINVDTL,new String[]{"","Invoice No","Prd.Code","Grade","Pkg. Type.","Sr.No.","Doc. Rate","Doc.Qty.","Prd.Grs.","tax(+)","Tax(-)","Prd. Net","Inv.Net","Inv.Amt","Other","Inv.Qty"},1500,1,1,7.9,5.8,new int[]{20,80,80,80,50,50,70,70,70,80,80,80,10,10,10,10},new int[]{0});
	//	tblINVDT1=crtTBLPNL1(pnlINVDTL1,new String[]{"","Invoice No","Prd.Code","Grade","Pkg. Type.","Sr.No.","Doc. Rate","Inv Qty.","Prd.Grs.","tax(+)","Tax(-)","Prd. Net","Inv.Net","Inv.Amt"},1500,1,1,7.9,5.8,new int[]{20,80,80,80,50,50,70,70,70,80,80,80,10,10},new int[]{0});
		tbpMAIN.addTab("Invoice Details ",pnlINVDTL);
	//	tbpMAIN.addTab("Invoice Details1 ",pnlINVDTL1);	
		tblCOMTX=crtTBLPNL1(pnlCOMNTX,new String[]{"","Tax.Code","Description","Amt/Prc","Value","Mode"},100,1,1,7.9,5.8,new int[]{20,100,300,110,110,100},new int[]{0});
		tbpMAIN.addTab("Common Tax",pnlCOMNTX);
		
		add(tbpMAIN,6,1,10,6,this,'L');
		
		tblINVDT.setCellEditor(intTB1_INVNO,txtINVNO = new TxtLimit(8));
		tblINVDT.setCellEditor(intTB1_PRGRD,txtPRDCD = new TxtLimit(10));
	
		tblINVDT.setCellEditor(intTB1_SRLNO,txtSRLNO = new TxtLimit(2));
		tblINVDT.setCellEditor(intTB1_DOCRT,txtCRDRT = new TxtNumLimit(8.2));
		
		tblCOMTX.setCellEditor(intTB2_TAXCD,txtTAXCD = new TxtLimit(3));
		tblCOMTX.setCellEditor(intTB2_TXMOD,txtTXMOD = new TxtLimit(1));
		
		txtINVNO.addFocusListener(this);
		txtINVNO.addKeyListener(this);
		
		txtCRDRT.addFocusListener(this);
		txtCRDRT.addKeyListener(this);
		
		txtPRDCD.addFocusListener(this);
		txtPRDCD.addKeyListener(this);
		
		txtTAXCD.addFocusListener(this);
		txtTAXCD.addKeyListener(this);
		txtTXMOD.addFocusListener(this);
		txtTXMOD.addKeyListener(this);
		
		objTBLVRF = new TBLINPVF();
		
		tblINVDT.addFocusListener(this);
		tblINVDT.addKeyListener(this);
		
		tblCOMTX.addFocusListener(this);
		tblCOMTX.addKeyListener(this);
		
		tblINVDT.setInputVerifier(objTBLVRF);
		tblINVDT.setInputVerifier(objTBLVRF);	
		
		tblCOMTX.setInputVerifier(objTBLVRF);
		tblCOMTX.setInputVerifier(objTBLVRF);	
	
		vldINVER objINVER=new vldINVER();
		for(int i=0;i<M_vtrSCCOMP.size();i++)
		{
			if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objINVER);
		}
		setENBL(false);
	}
	void setENBL(boolean L_STAT)
	{
		try
		{
			clrCOMP();
			txtREMDS.setText("");	
			super.setENBL(L_STAT);// default false
			txtPRTNM.setEnabled(false);
			rdbCREDT.setEnabled(true);
			rdbDEBIT.setEnabled(true);
            cmbMKTTP.setEnabled(true);
			tblINVDT.cmpEDITR[intTB1_PKGTP].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_PRDDS].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_INVQT].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_INVVL].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_ATXVL].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_LTXVL].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_PNTVL].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_INTVL].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_PGRVL].setEnabled(false);
			tblCOMTX.cmpEDITR[intTB2_TAXDS].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_OTHVL].setEnabled(false);
			tblINVDT.cmpEDITR[intTB1_INVQT1].setEnabled(false);
			if(txtDOCTP.getText().equals(strINTDN))
			{
				tblINVDT.cmpEDITR[intTB1_PGRVL].setEnabled(true);
				tblINVDT.cmpEDITR[intTB1_DOCRT].setEnabled(false);
			}
			lblCRDVL.setVisible(false);
			txtCRDVL.setVisible(false);
			
			txtPRDCD1.setVisible(false);
			txtPRDDS.setVisible(false);
			txtAGRPR.setVisible(false);
			lblAGRPR.setVisible(false);
			lblPRDCD1.setVisible(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setENBL");
		}
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC==txtINVNO) 
			{
				setMSG("Press F1 to select Invoice  & then press Enter.",'N');
				if(tblINVDT.getSelectedRow()<intRWCNT)
					((JTextField)tblINVDT.cmpEDITR[intTB1_INVNO]).setEditable(false);
				else
					((JTextField)tblINVDT.cmpEDITR[intTB1_INVNO]).setEditable(true);
			}
			if(M_objSOURC==txtPRDCD) 
			{
				setMSG("Press F1 to select Grade  & then press Enter.",'N');
				if(tblINVDT.getSelectedRow()<intRWCNT)
					((JTextField)tblINVDT.cmpEDITR[intTB1_PRGRD]).setEditable(false);
				else
					((JTextField)tblINVDT.cmpEDITR[intTB1_PRGRD]).setEditable(true);
			}
			
			if(M_objSOURC==txtTAXCD) 
			{
				setMSG("Press F1 to select Tax Code & then press Enter.",'N');
				if(tblCOMTX.getSelectedRow()<intCOMTX)
					((JTextField)tblCOMTX.cmpEDITR[intTB2_TAXCD]).setEditable(false);
				else
					((JTextField)tblCOMTX.cmpEDITR[intTB2_TAXCD]).setEditable(true);
			}
			if(M_objSOURC==txtTXMOD) 
			{
				setMSG("Enter Tax Mode (A/D)",'N');
				if(tblCOMTX.getSelectedRow()<intCOMTX)
					((JTextField)tblCOMTX.cmpEDITR[intTB2_TXMOD]).setEditable(false);
				else
					((JTextField)tblCOMTX.cmpEDITR[intTB2_TXMOD]).setEditable(true);
			}
		}
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				txtREMDS.setText("");	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
					setENBL(false);
				else
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						txtDOCRF.setEnabled(true);
					    txtPRTTP.setEnabled(true);
						txtDOCTP.setEnabled(true);
						//cmbMKTTP.setEnabled(false);
						txtDOCTP.requestFocus();
					}
					else 
					{
						
						txtDOCRF.setEnabled(false);
						txtDOCTP.requestFocus();
					}
				}
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
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC==txtPRTTP)
			{
				txtPRTCD.requestFocus();
				intRWCNT=0;
			}
			if(M_objSOURC==txtDOCTP)
			{
			    
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
				{
				    txtOTHCT.setText("");
			   	    if(txtDOCTP.getText().equals(strOTHCN))
					{ 
					    txtOTHCT.setEnabled(true);
					    txtOTHCT.requestFocus();
					}
	                else
	                {		
			  		    txtOTHCT.setEnabled(false);
					    txtDOCRF.requestFocus();
	                }
	           	}
				else
				{
				    if(txtDOCTP.getText().equals(strOTHCN))
					{ 
					    txtOTHCT.setEnabled(true);
					    txtOTHCT.requestFocus();
					}
					else
					{
                        if(txtDOCTP.getText().equals(strMISDN))
        				{ 
        				    lblCRDVL.setVisible(true);
        	                txtCRDVL.setVisible(true);
        				}
                        else
                        {
					        lblCRDVL.setVisible(false);
		                    txtCRDVL.setVisible(false);
                        }
                        txtPRDCD1.setVisible(false);
			            txtPRDDS.setVisible(false);
			            txtAGRPR.setVisible(false);
						lblAGRPR.setVisible(false);
						lblPRDCD1.setVisible(false);
					    txtOTHCT.setEnabled(false);
    				    txtOTHCT.setText("");
    				    txtPRDCD1.setText("");
    				    txtPRDDS.setText("");
    					txtPRTTP.requestFocus();
    					if((rdbCREDT.isSelected())&& (txtDOCTP.getText().trim().equals("02")||txtDOCTP.getText().trim().equals("03")))
    				    	txtPRTTP.setText("D");
    				    else
    				    	txtPRTTP.setText("C");
    				    txtPRTCD.setText("");	
    				    txtPRTNM.setText("");	
              		}
				}
			}
			else if(M_objSOURC==txtOTHCT)
			{
			    if((txtOTHCT.getText().equals(strMISCN))||(txtDOCTP.getText().equals(strMISDN)) && !chkWIINV.isSelected())
				{ 
				    lblCRDVL.setVisible(true);
	                txtCRDVL.setVisible(true);
				}
				else
				{
				    lblCRDVL.setVisible(false);
	                txtCRDVL.setVisible(false);
				}
				if(txtOTHCT.getText().equals(strPDFCN))
				{
				    txtPRDCD1.setVisible(true);
			        txtPRDDS.setVisible(true);
			        txtAGRPR.setVisible(true);
					lblAGRPR.setVisible(true);
					lblPRDCD1.setVisible(true);
				}
				else
				{
				    txtPRDCD1.setVisible(false);
			        txtPRDDS.setVisible(false);
			        txtAGRPR.setVisible(false);
			       	lblAGRPR.setVisible(false);
					lblPRDCD1.setVisible(false);
				
				}

				if(txtOTHCT.getText().equals(strSLRCN))
				{
					tblINVDT.cmpEDITR[intTB1_INVQT].setEnabled(true);
					tblINVDT.cmpEDITR[intTB1_PNTVL].setEnabled(true);
				}
				if(txtOTHCT.getText().length() !=2)
				{
				    setMSG("Enter the Category of Other credit Note",'E');
				    return;
				}
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
				{
				    txtDOCRF.requestFocus();
				}
				else
				{
    		        txtPRTTP.requestFocus();
    				if((rdbCREDT.isSelected())&& (txtDOCTP.getText().trim().equals("02")||txtDOCTP.getText().trim().equals("03")))
    			    	txtPRTTP.setText("D");
    			    else
    			    	txtPRTTP.setText("C");
    			    txtPRTCD.setText("");	
    			    txtPRTNM.setText("");	
				}
				
			}
			if(M_objSOURC==txtDOCRF)
			{
			    String L_strDOCTP = txtDOCTP.getText().trim();
			    String L_strDOCRF = txtDOCRF.getText().trim();
			    String L_strOTHCT = txtOTHCT.getText().trim();
			    clrCOMP();
			    txtREMDS.setText("");
			    txtDOCRF.setText(L_strDOCRF);	
			    txtDOCTP.setText(L_strDOCTP);	
			    txtOTHCT.setText(L_strOTHCT);	
				getDATA(txtDOCRF.getText().trim());		
			}
			if(M_objSOURC==txtFRDAT)
			{
			    txtTODAT.requestFocus();
			}
            if(M_objSOURC==txtTODAT)
			{
			    if(txtDOCTP.getText().equals("02")||txtDOCTP.getText().equals("03"))
			    {
			        getDATA("00000000"); 
					//System.out.println("Other C/N");
			    }
			    else
			    {
					//System.out.println("else part ");
			        try
			        {
						if(((txtOTHCT.getText().equals(strMISCN))||(txtDOCTP.getText().equals(strMISDN))) && !chkWIINV.isSelected())
						{
    			            txtCRDVL.requestFocus();
    			            /*tblINVDT.setValueAt(strDUMINV,0,intTB1_INVNO);
    			            tblINVDT.setValueAt(strDUMPRD,0,intTB1_PRGRD);
    			            tblINVDT.setValueAt("01",0,intTB1_SRLNO);
    			            tblINVDT.setValueAt("1.000",0,intTB1_INVQT);
    			            tblINVDT.setRowSelectionInterval(0,0);		
            				tblINVDT.setColumnSelectionInterval(intTB1_DOCRT,intTB1_DOCRT);		
            				tblINVDT.editCellAt(tblINVDT.getSelectedRow(),intTB1_DOCRT);
            				tblINVDT.cmpEDITR[intTB1_DOCRT].requestFocus();   */
    			        }
    			        else if(txtOTHCT.getText().equals(strPDFCN))
    			        {
    			            txtPRDCD1.requestFocus();
    			        }
						if(txtDOCTP.getText().equals(strSLRCN))
						{
							tblINVDT.cmpEDITR[intTB1_PGRVL].setEnabled(true);
							tblINVDT.cmpEDITR[intTB1_DOCRT].setEnabled(true);
							tblINVDT.cmpEDITR[intTB1_INVQT].setEnabled(true);
						}
    			        else
    			        {
							if(txtDOCTP.getText().equals(strINTDN))
							{
								tblINVDT.cmpEDITR[intTB1_PGRVL].setEnabled(true);
								tblINVDT.cmpEDITR[intTB1_DOCRT].setEnabled(false);
							}
    			            String L_strGRPCD ="";
							if(chkWIINV.isSelected())
								txtCRDVL.setEnabled(false);
    			            if(chkALINV.isSelected())
        					{
        						M_strSQLQRY="Select PT_GRPCD from CO_PTMST where PT_PRTTP='"+txtPRTTP.getText().trim()+"' And  PT_PRTCD='"+txtPRTCD.getText().trim()+"' ";//AND isnull(PT_STSFL,'') <> 'X'";
        						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
        						if(M_rstRSSET!=null && M_rstRSSET.next())
        						{
        							L_strGRPCD=M_rstRSSET.getString("PT_GRPCD");
        						}
        						M_strSQLQRY= "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS from MR_IVTRN,MR_PLTRN where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_DOCTP ='21' AND PL_DOCNO = IVT_INVNO AND PL_CMPCD = IVT_CMPCD AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
        						M_strSQLQRY +=" IVT_BYRCD in ( Select PT_PRTCD from CO_PTMST where PT_GRPCD='"+L_strGRPCD+"' AND PT_PRTTP='C' )";//AND isnull(PT_STSFL,'') <> 'X')";
        					}
        					else
        					{
        						M_strSQLQRY = "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS from MR_IVTRN,MR_PLTRN where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_DOCTP ='21' AND PL_DOCNO = IVT_INVNO AND PL_CMPCD = IVT_CMPCD AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
        						if(txtPRTTP.getText().equals("D"))
								{	
        							M_strSQLQRY += " IVT_BYRCD IN (SELECT PT_PRTCD FROM CO_PTMST WHERE PT_DSRCD = '"+txtPRTCD.getText().trim()+"') AND IVT_SALTP NOT IN('14') ";
									System.out.println("1");
								}	
								else if(txtPRTTP.getText().equals("G"))
								{	
        							M_strSQLQRY += " IVT_BYRCD IN (SELECT PT_PRTCD FROM CO_PTMST WHERE PT_CNSRF = '"+txtPRTCD.getText().trim()+"') AND IVT_SALTP='14' ";
									System.out.println("1");
								}	
        						else if(txtPRTTP.getText().equals("C"))
								{	
        						    M_strSQLQRY += " IVT_BYRCD = '"+txtPRTCD.getText().trim()+"'";
									System.out.println("1");
								}
								
        						M_strSQLQRY += " AND isnull(IVT_STSFL,'') <> 'X'" ;// IVT_MKTTP = '"+strMKTTP1+"' and 
        						M_strSQLQRY += " AND CONVERT(varchar,IVT_INVDT,103) between '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
        					}
        					if(txtOTHCT.getText().equals(strEPICN)) // Condition for Early Payment interest
        					    M_strSQLQRY += " AND PL_PAYDT < PL_DUEDT ";
							
        					tblINVDT.clrTABLE();
							System.out.println("M_strSQLQRY "+M_strSQLQRY);
        					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
        					if(M_rstRSSET !=null)
        					{
        					    int L_intCNT =0;
            					while(M_rstRSSET.next())
            					{
            						tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVNO"),L_intCNT,intTB1_INVNO);
                    				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PRDCD"),L_intCNT,intTB1_PRGRD);
                    				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PKGTP"),L_intCNT,intTB1_PKGTP);
                    				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),L_intCNT,intTB1_INVQT);
                    				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVVL"),L_intCNT,intTB1_INVVL);
                    				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PRDDS"),L_intCNT,intTB1_PRDDS);
                    				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),L_intCNT,intTB1_INVQT1);
                    				L_intCNT++;
            					}
            					M_rstRSSET.close();
        					}
        					/*tblINVDT.setRowSelectionInterval(tblINVDT.getSelectedRow(),tblINVDT.getSelectedRow());		
            				tblINVDT.setColumnSelectionInterval(intTB1_INVNO,intTB1_INVNO);		
            				tblINVDT.editCellAt(tblINVDT.getSelectedRow(),intTB1_INVNO);
            				tblINVDT.cmpEDITR[intTB1_INVNO].requestFocus();*/
    			        }
			        }
			        catch(Exception L_E)
    			    {
    			        setMSG(L_E,"VK_ENTER");
    			    }
    		    }
                setDFTAX();
			}
			if(M_objSOURC==txtPRDCD1)
			{
			    txtAGRPR.requestFocus();
			}
			if(M_objSOURC==txtAGRPR)
			{
			    try
			    {
			       /* pnlINVDTL =null;
			        pnlINVDTL=new JPanel(null);
		            setMatrix(20,8);
			        tblINVDT=crtTBLPNL1(pnlINVDTL,new String[]{"","Invoice No","Basic Rate","Bkd. Dis","D.Bkg D","Frt Rate","Doc. Rate","Inv Qty.","Prd.Grs.","tax(+)","Tax(-)","Prd. Net","Inv.Net","Inv.Amt"},1500,1,1,8.1,5.8,new int[]{20,80,80,80,50,50,70,70,70,80,80,80,10,10},new int[]{0});
		            */
		            if(txtAGRPR.getText().length() ==0)
		            {
		                setMSG("Enter Agreed Price ..",'E');
		                return;
		            }
		            if(Double.parseDouble(txtAGRPR.getText()) ==0)
		            {
		                setMSG("Enter Agreed Price ..",'E');
		                return;
		            }
		            tblINVDT.clrTABLE();
		            String L_strGRPCD ="";
		            if(chkALINV.isSelected())
					{
						M_strSQLQRY="Select PT_GRPCD from CO_PTMST where PT_PRTTP='"+txtPRTTP.getText().trim()+"' And  PT_PRTCD='"+txtPRTCD.getText().trim()+"' ";//AND isnull(PT_STSFL,'') <> 'X'";
						System.out.println(M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET!=null && M_rstRSSET.next())
						{
							L_strGRPCD=M_rstRSSET.getString("PT_GRPCD");
						}
						M_strSQLQRY= "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS,IVT_INVRT,ROUND(IVT_CC1VL/IVT_INVQT,2) IVT_CC1VL, ROUND(IVT_CC2VL/IVT_INVQT,2) IVT_CC2VL,IVT_CC3VL/IVT_INVQT IVT_CC3VL,IVT_FRTRT from MR_IVTRN where ";
						M_strSQLQRY += " IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_BYRCD in ( Select PT_PRTCD from CO_PTMST where PT_GRPCD='"+L_strGRPCD+"' AND PT_PRTTP='C' )";//AND isnull(PT_STSFL,'') <> 'X')";
						//M_strSQLQRY= "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS,IVT_INVRT,ROUND(IVT_CC1VL/IVT_INVQT,2) IVT_CC1VL, ROUND(IVT_CC2VL/IVT_INVQT,2) IVT_CC2VL,IVT_CC3VL/IVT_INVQT IVT_CC3VL,DOT_FRTRT from MR_IVTRN,MR_DOTRN where ";
						//M_strSQLQRY += " IVT_MKTTP  = DOT_MKTTP AND IVT_DORNO = DOT_DORNO AND IVT_PRDCD = DOT_PRDCD AND IVT_PKGTP = DOT_PKGTP AND IVT_CMPCD = DOT_CMPCD and ";
						//M_strSQLQRY +=" IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_BYRCD in ( Select PT_PRTCD from CO_PTMST where PT_GRPCD='"+L_strGRPCD+"' AND PT_PRTTP='C' )";//AND isnull(PT_STSFL,'') <> 'X')";
					}
					else
					{
						//M_strSQLQRY = "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS,IVT_INVRT,IVT_CC1VL/IVT_INVQT IVT_CC1VL,IVT_CC2VL/IVT_INVQT IVT_CC2VL,IVT_CC3VL/IVT_INVQT IVT_CC3VL,DOT_FRTRT from MR_IVTRN,MR_DOTRN ";
						//M_strSQLQRY += " where IVT_MKTTP  = DOT_MKTTP AND IVT_DORNO = DOT_DORNO AND IVT_PRDCD = DOT_PRDCD AND IVT_PKGTP = DOT_PKGTP AND IVT_CMPCD = DOT_CMPCd and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
						M_strSQLQRY = "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS,IVT_INVRT,IVT_CC1VL/IVT_INVQT IVT_CC1VL,IVT_CC2VL/IVT_INVQT IVT_CC2VL,IVT_CC3VL/IVT_INVQT IVT_CC3VL,IVT_FRTRT from MR_IVTRN ";
						M_strSQLQRY += " where  IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
						if(txtPRTTP.getText().equals("D"))
							M_strSQLQRY += " and IVT_BYRCD IN (SELECT PT_PRTCD FROM CO_PTMST WHERE PT_PRTTP='C' and PT_DSRCD = '"+txtPRTCD.getText().trim()+"')" ;// IVT_MKTTP = '"+strMKTTP1+"' and 
						else if(txtPRTTP.getText().equals("B"))
						    M_strSQLQRY += " and IVT_BYRCD = '"+txtPRTCD.getText().trim()+"'";
						//else if(txtPRTTP.getText().equals("G"))
						//    M_strSQLQRY += " and IVT_DSRTP='G' and IVT_DSRCD = '"+txtPRTCD.getText().trim()+"' ";
						else if(txtPRTTP.getText().equals("G"))
						    M_strSQLQRY += " and IVT_BYRCD IN (SELECT PT_PRTCD FROM CO_PTMST WHERE PT_PRTTP='C' and PT_CNSRF = '"+txtPRTCD.getText().trim()+"') and IVT_SALTP='14' " ;// IVT_MKTTP = '"+strMKTTP1+"' and 
						M_strSQLQRY += " AND isnull(IVT_STSFL,'') <> 'X'" ;// IVT_MKTTP = '"+strMKTTP1+"' and 
						M_strSQLQRY += " AND CONVERT(varchar,IVT_INVDT,103) between '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
					}
					M_strSQLQRY += " AND IVT_PRDCD ='"+txtPRDCD1.getText().trim() +"' ORDER BY IVT_INVNO " ;
				
				   // M_strSQLQRY = " SELECT IVT_INVNO,date(IVT_INVDT) IVT_INVDT,IVT_PRDDS,CMT_CODDS DSPTP,IVT_INVQT,IVT_INDNO,IVT_INVRT,IVT_CC1VL/IVT_INVQT IVT_CC1VL, IVT_CC2VL/IVT_INVQT IVT_CC2VL,IVT_CC3VL/IVT_INVQT IVT_CC3VL FROM SPLDATA.MR_IVTRN,SPLDATA.CO_CDTRN WHERE date(IVT_INVDT) BETWEEN  '03/01/2006' AND '03/31/2006' AND IVT_BYRCD='P0022' AND CMT_CGMTP='SYS' AND CMT_CGSTP='MRXXDTP' AND CMT_CODCD=IVT_DTPCD ORDER BY IVT_PRDDS,IVT_INVNO;
					double L_dblBASRT =0,L_dblCC1VL =0,L_dblCC2VL=0,L_dblFRTRT = 0,L_dblINVQT =0,L_dblDOCRT =0,L_dblTOTQT =0,L_dblTOTVL =0;
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
					    int L_intCNT =0;
    					while(M_rstRSSET.next())
    					{
    					    L_dblBASRT = M_rstRSSET.getDouble("IVT_INVRT");
    					    L_dblCC1VL = M_rstRSSET.getDouble("IVT_CC1VL");
    					    L_dblCC2VL = M_rstRSSET.getDouble("IVT_CC2VL");
    					    //L_dblFRTRT = M_rstRSSET.getDouble("DOT_FRTRT");
    					    L_dblFRTRT = M_rstRSSET.getDouble("IVT_FRTRT");
    					    L_dblINVQT = M_rstRSSET.getDouble("IVT_INVQT");
    					   // L_dblDOCRT = L_dblBASRT - (L_dblCC1VL+L_dblCC2VL+L_dblFRTRT+Double.parseDouble(nvlSTRVL(txtAGRPR.getText(),"0")));
                            // fREIGHT Rate removed from consideraration on 06/11/2006 ap NSM
                            L_dblDOCRT = L_dblBASRT - (L_dblCC1VL+L_dblCC2VL+Double.parseDouble(nvlSTRVL(txtAGRPR.getText(),"0")));
                            tblINVDT.setValueAt(new Boolean("true"),L_intCNT,intTB1_CHKFL);
    						tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVNO"),L_intCNT,intTB1_INVNO);
    						tblINVDT.setValueAt(setNumberFormat(L_dblBASRT,0),L_intCNT,intTB1_PRGRD);
    						tblINVDT.setValueAt(setNumberFormat(L_dblCC1VL,2),L_intCNT,intTB1_PRDDS);
    						tblINVDT.setValueAt(setNumberFormat(L_dblCC2VL,2),L_intCNT,intTB1_PKGTP);
    						tblINVDT.setValueAt(setNumberFormat(L_dblFRTRT,0),L_intCNT,intTB1_SRLNO);
    						tblINVDT.setValueAt(setNumberFormat(L_dblDOCRT,2),L_intCNT,intTB1_DOCRT);
            			//	tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PRDCD"),L_intCNT,intTB1_PRGRD);
            			//	tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PRDDS"),L_intCNT,intTB1_PRDDS);
            			
            				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),L_intCNT,intTB1_INVQT);
            				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),L_intCNT,intTB1_INVQT1);
            				tblINVDT.setValueAt(setNumberFormat(L_dblDOCRT * L_dblINVQT,2),L_intCNT,intTB1_PGRVL);
                            tblINVDT.setValueAt(setNumberFormat(L_dblDOCRT * L_dblINVQT,2),L_intCNT,intTB1_PNTVL);
            				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVVL"),L_intCNT,intTB1_INVVL);
            				tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PKGTP"),L_intCNT,intTB1_OTHVL);
            			    L_dblTOTQT += L_dblINVQT;
            			    L_dblTOTVL += L_dblDOCRT * L_dblINVQT;
            				L_intCNT++;
    					}
    					M_rstRSSET.close();
    					lblINVQT.setText(setNumberFormat(L_dblTOTQT,2));
    					lblPGRVL.setText(setNumberFormat(L_dblTOTVL,2));
					}
					/*tblINVDT.setRowSelectionInterval(tblINVDT.getSelectedRow(),tblINVDT.getSelectedRow());		
    				tblINVDT.setColumnSelectionInterval(intTB1_INVNO,intTB1_INVNO);		
    				tblINVDT.editCellAt(tblINVDT.getSelectedRow(),intTB1_INVNO);
    				tblINVDT.cmpEDITR[intTB1_INVNO].requestFocus();*/
			    }
			    catch(Exception L_E)
			    {
			        setMSG(L_E,"Price Diff");
			    }
			}
			if(M_objSOURC==txtCRDVL)
			{
			    txtREMDS.requestFocus();
			    //cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			if(M_objSOURC==txtPRTCD)
			{
			    /*if((rdbCREDT.isSelected())&& (txtDOCTP.getText().trim().equals("02")||txtDOCTP.getText().trim().equals("03")))
			    {
			        txtFRDAT.setText(cl_dat.M_strLOGDT_pbst);
			        txtTODAT.setText(cl_dat.M_strLOGDT_pbst);   
			        txtFRDAT.requestFocus();
			    }
			    else
			    {
    				tblINVDT.setRowSelectionInterval(tblINVDT.getSelectedRow(),tblINVDT.getSelectedRow());		
    				tblINVDT.setColumnSelectionInterval(intTB1_INVNO,intTB1_INVNO);		
    				tblINVDT.editCellAt(tblINVDT.getSelectedRow(),intTB1_INVNO);
    				tblINVDT.cmpEDITR[intTB1_INVNO].requestFocus();
			    }*/
                 txtPRTCD.setText(txtPRTCD.getText().toUpperCase());
			     txtFRDAT.setText(cl_dat.M_strLOGDT_pbst);
		         txtTODAT.setText(cl_dat.M_strLOGDT_pbst);   
		         txtFRDAT.requestFocus();

			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPRTTP)
			{
				M_strHLPFLD="txtPRTTP";
				if((rdbCREDT.isSelected())&& (txtDOCTP.getText().trim().equals("02")||txtDOCTP.getText().trim().equals("03")))
				    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXPRT' AND CMT_CODCD IN('D','G') AND isnull(CMT_STSFL,'') <> 'X' ";					
				else
				    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXPRT' AND CMT_CODCD IN('C','D','G') AND isnull(CMT_STSFL,'') <> 'X' ";					
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
			}
			if(M_objSOURC == txtDOCTP)
			{
				M_strHLPFLD = "txtDOCTP";
				if(rdbCREDT.isSelected())
				{
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT' And CMT_CODCD like '0%' and cmt_chp02 ='Y' ";					
					//M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT'";// And CMT_CODCD ='09'";					
				}
				else
				{
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT' AND CMT_CHP02 ='Y' and CMT_CODCD like '3%'";
				}
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
			}
			if(M_objSOURC == txtOTHCT)
			{
				M_strHLPFLD = "txtOTHCT";
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXOCN' And CMT_CODCD like '0%'";					
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
			}
			if(M_objSOURC == txtPRDCD1)
			{
				try
				{
    				M_strHLPFLD = "txtPRDCD1";
					String L_strADDSTR = " IVT_BYRCD ";
					if(txtPRTTP.getText().equals("D"))
					   L_strADDSTR = " IVT_DSRTP = 'D' and IVT_DSRCD ";
					else if(txtPRTTP.getText().equals("G"))
					   L_strADDSTR = " IVT_DSRTP = 'G' and IVT_DSRCD ";
					M_strSQLQRY = "Select DISTINCT IVT_PRDCD,IVT_PRDDS from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+L_strADDSTR+" = '"+txtPRTCD.getText().trim() +"'" ;
    				M_strSQLQRY +=" AND CONVERT(varchar,IVT_INVDT,103) BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
					System.out.println(M_strSQLQRY);
    				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
				}
				catch(Exception L_E)
				{
				    setMSG(L_E,"VK_F1");
				}
				
			}
			if(M_objSOURC == txtDOCRF)
			{
				M_strHLPFLD = "txtDOCRF";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{	
					if(txtOTHCT.getText().length() >0)
					    M_strSQLQRY = "Select distinct PT_DOCRF,PT_DOCDT from MR_PTTRN where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_CRDTP ='"+txtOTHCT.getText().trim()+"' AND PT_STSFL='0' AND isnull(PT_STSFL,'') <> 'X' order by PT_DOCRF ";
					else
				    	M_strSQLQRY = "Select distinct PT_DOCRF,PT_DOCDT from MR_PTTRN where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_CRDTP ='"+txtDOCTP.getText().trim()+"' AND PT_STSFL='0' AND isnull(PT_STSFL,'') <> 'X' order by PT_DOCRF ";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Doc. Number","Doc Date"},2,"CT");
				}
				else
				{
                    if((rdbCREDT.isSelected())&& (txtDOCTP.getText().trim().equals("02")||txtDOCTP.getText().trim().equals("03")))
					    M_strSQLQRY = "Select distinct A.PT_DOCRF,A.PT_RPTDT,B.PT_PRTNM from MR_PTTRN A,CO_PTMST B where A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD AND A.PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.PT_CRDTP ='"+txtDOCTP.getText().trim()+"' AND isnull(A.PT_STSFL,'') <> 'X' and A.PT_DOCRF <>'00000000' order by A.PT_DOCRF DESC ";
                    else
                    {
                        if(txtOTHCT.getText().length() >0)
                            M_strSQLQRY = "Select distinct A.PT_DOCRF,A.PT_RPTDT,B.PT_PRTNM from MR_PTTRN A,CO_PTMST B where A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD AND A.PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.PT_CRDTP ='"+txtOTHCT.getText().trim()+"' AND isnull(A.PT_STSFL,'') <> 'X'  order by A.PT_DOCRF  ";
                        else
                            M_strSQLQRY = "Select distinct A.PT_DOCRF,A.PT_RPTDT,B.PT_PRTNM from MR_PTTRN A,CO_PTMST B where A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD AND A.PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.PT_CRDTP ='"+txtDOCTP.getText().trim()+"' AND isnull(A.PT_STSFL,'') <> 'X'  order by A.PT_DOCRF  ";
                    }
                        cl_hlp(M_strSQLQRY ,1,1,new String[] {"Doc. Number","Doc Date","Party"},3,"CT");
				}
				
				
			}
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD = "txtPRTCD";
				//M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP ='"+txtPRTTP.getText().trim()+"' and PT_PRTCD in (select distinct IVT_BYRCD from MR_IVTRN where IVT_MKTTP = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"')";
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP ='"+txtPRTTP.getText().trim()+"'";
				if(txtPRTCD.getText().length() >0)
				    M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText().trim() +"%'";
				//M_strSQLQRY += " AND isnull(PT_STSFL,'') <> 'X' ORDER BY PT_PRTNM";
                M_strSQLQRY += " ORDER BY PT_PRTNM";
				cl_hlp(M_strSQLQRY ,2,1,new String[] {"Party code","Party Name"},2,"CT");
			}
			if(M_objSOURC == txtINVNO)
			{
				try
				{
					M_strHLPFLD = "txtINVNO";
					String L_strGRPCD="";
					if(chkALINV.isSelected())
					{
						M_strSQLQRY="Select PT_GRPCD from CO_PTMST where PT_PRTTP='"+txtPRTTP.getText().trim()+"' And  PT_PRTCD='"+txtPRTCD.getText().trim()+"' ";//AND isnull(PT_STSFL,'') <> 'X'";
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET!=null && M_rstRSSET.next())
						{
							L_strGRPCD=M_rstRSSET.getString("PT_GRPCD");
						}
						M_strSQLQRY= "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS from MR_IVTRN where ";
						M_strSQLQRY +=" IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_BYRCD in ( Select PT_PRTCD from CO_PTMST where PT_GRPCD='"+L_strGRPCD+"' AND PT_PRTTP='C')";// AND isnull(PT_STSFL,'') <> 'X')";
					}
					else
					{
						M_strSQLQRY = "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS from MR_IVTRN where ";
						if(txtPRTTP.getText().equals("D"))
							M_strSQLQRY += " IVT_BYRCD IN (SELECT PT_PRTCD FROM CO_PTMST WHERE PT_DSRCD = '"+txtPRTCD.getText().trim()+"')" ;// IVT_MKTTP = '"+strMKTTP1+"' and 
						else
						    M_strSQLQRY += " IVT_BYRCD = '"+txtPRTCD.getText().trim()+"'";
						M_strSQLQRY += " AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_STSFL,'') <> 'X'" ;// IVT_MKTTP = '"+strMKTTP1+"' and 
						M_strSQLQRY += " AND CONVERT(varchar,IVT_INVDT,103) between '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
					}
				
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Inv No","product code","pkg type","Inv Qty","Inv Amt","Grade"},6,"CT");
				}
				catch(Exception E)
				{
					setMSG(E,"KeyPressed");
				}
			}
			if(M_objSOURC == txtPRDCD)
			{
				M_strHLPFLD = "txtPRDCD";
				M_strSQLQRY = "Select IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL,IVT_PRDDS from MR_IVTRN where ";
				M_strSQLQRY +=" IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO = '"+tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVNO)+ "'";
				M_strSQLQRY += " AND IVT_BYRCD = '"+txtPRTCD.getText().trim()+"'  AND isnull(IVT_STSFL,'') <> 'X'" ;
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"product code","pkg type","Inv Qty","Inv Amt","Grade"},5,"CT");
			}
			if(M_objSOURC == txtTAXCD)
			{
				M_strHLPFLD = "txtTAXCD";
				//M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CHP02,CMT_NCSVL,CMT_CHP01,CMT_NMP01  from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'COXXTAX' AND CMT_CHP01='01'";
				M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,3)CMT_CODCD,CMT_CODDS,CMT_CHP02,CMT_NCSVL,CMT_CHP01,CMT_NMP01  from CO_CDTRN where CMT_CGMTP='TCL' and CMT_CGSTP = 'MRXXDCM'";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description","Para1","para2","Mode",},5,"CT");
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
			if(M_strHLPFLD == "txtPRTTP")
			{
				txtPRTTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtDOCTP")
			{
				txtDOCTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtOTHCT")
			{
				txtOTHCT.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtDOCRF")
			{
				txtDOCRF.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtPRTCD")
			{
				txtPRTCD.setText(L_STRTKN.nextToken());
				txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());		
			}
			if(M_strHLPFLD == "txtPRDCD1")
			{
				txtPRDCD1.setText(L_STRTKN.nextToken());
				txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());		
			}
			if(M_strHLPFLD == "txtINVNO")
			{
				
				L_STRTKN.nextToken();			
				txtINVNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblINVDT.getSelectedRow(),intTB1_PRGRD);
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblINVDT.getSelectedRow(),intTB1_PKGTP);
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),tblINVDT.getSelectedRow(),intTB1_INVQT);
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim(),tblINVDT.getSelectedRow(),intTB1_INVVL);
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)).trim(),tblINVDT.getSelectedRow(),intTB1_PRDDS);
				//tblINVDT.setValueAt(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT),tblINVDT.getSelectedRow(),intTB1_INVQT1);
			}
			if(M_strHLPFLD == "txtPRDCD")
			{
				L_STRTKN.nextToken();			
				txtPRDCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblINVDT.getSelectedRow(),intTB1_PKGTP);
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblINVDT.getSelectedRow(),intTB1_INVQT);
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),tblINVDT.getSelectedRow(),intTB1_INVVL);
				tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim(),tblINVDT.getSelectedRow(),intTB1_PRDDS);
				//tblINVDT.setValueAt(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT),tblINVDT.getSelectedRow(),intTB1_INVQT1);
			}
			if(M_strHLPFLD == "txtTAXCD")
			{
				L_STRTKN.nextToken();			
				txtTAXCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				//tblINVDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblINVDT.getSelectedRow(),intTB1_PRGRD);
				tblCOMTX.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblCOMTX.getSelectedRow(),intTB2_TAXDS);
				tblCOMTX.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblCOMTX.getSelectedRow(),intTB2_TAXFL);
				tblCOMTX.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),tblCOMTX.getSelectedRow(),intTB2_TAXVL);
				tblCOMTX.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim(),tblCOMTX.getSelectedRow(),intTB2_TXMOD);
				txtTAXCD.requestFocus();
				tblCOMTX.cmpEDITR[intTB2_TAXCD].requestFocus();
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
		}
	}
	public boolean vldDATA()
	{
		try
		{
			if(txtPRTTP.getText().trim().length()==0)
			{
				setMSG("Party Type Can't be Blank",'E');
				txtPRTTP.requestFocus();
				return false;
			}
			if(txtDOCTP.getText().trim().length()==0)
			{
				setMSG("Document Type Can't be Blank",'E');
				txtDOCTP.requestFocus();
				return false;
			}
			if((txtOTHCT.getText().trim().length()==0)&&(txtDOCTP.getText().equals("09")))
			{
				setMSG("Other Credit note Category Can't be Blank",'E');
				txtOTHCT.requestFocus();
				return false;
			}
			if(txtPRTCD.getText().trim().length()==0)
			{
				setMSG("Party Code Can't be Blank",'E');
				txtPRTCD.requestFocus();
				return false;
			}
			for(int i=0;i < tblINVDT.getRowCount();i++)
			{
			    if(tblINVDT.getValueAt(i,intTB1_CHKFL).toString().equals("true"))
			    {
    			     if(tblINVDT.getValueAt(i,intTB1_INVNO).toString().length() ==0)
    			     {
    			        setMSG("Invoice no. can not be blank..",'E');
    			        return false;
    			     }
    			     if(tblINVDT.getValueAt(i,intTB1_PRGRD).toString().length() ==0)
    			     {
    			        setMSG("Product Code can not be blank..",'E');
    			        return false;
    			     }
    			     if(tblINVDT.getValueAt(i,intTB1_PKGTP).toString().length() ==0)
    			     {
    			        setMSG("Package Type can not be blank..",'E');
    			        return false;
    			     }
       			     if(tblINVDT.getValueAt(i,intTB1_PRGRD).toString().length() ==0)
    			     {
    			        setMSG("Grade can not be blank..",'E');
    			        return false;
    			     }

    			     if(tblINVDT.getValueAt(i,intTB1_DOCRT).toString().length() ==0)
    			     {
    			        setMSG("Rate can not be blank..",'E');
    			        return false;
    			     }
       			     if(tblINVDT.getValueAt(i,intTB1_SRLNO).toString().length() ==0)
    			     {
    			        setMSG("Serial no. can not be blank..",'E');
    			        return false;
    			     }
                    if(tblINVDT.getValueAt(i,intTB1_PGRVL).toString().length() ==0)
    			     {
    			        setMSG("Value can not be blank..",'E');
    			        return false;
    			     }
			    }
			}
			for(int i=0;i < tblCOMTX.getRowCount();i++)
			{
			    if(tblCOMTX.getValueAt(i,intTB1_CHKFL).toString().equals("true"))
			    {
    			     if(tblCOMTX.getValueAt(i,intTB2_TAXCD).toString().length() ==0)
    			     {
    			        setMSG("Tax Code not be blank..",'E');
    			        return false;
    			     }
    			     if(tblCOMTX.getValueAt(i,intTB2_TAXFL).toString().length() ==0)
    			     {
    			        setMSG("Tax Flag not be blank..",'E');
    			        return false;
    			     }
    			     if(tblCOMTX.getValueAt(i,intTB2_TAXVL).toString().length() ==0)
    			     {
    			        setMSG("Tax Value not be blank..",'E');
    			        return false;
    			     }
    			     if(tblCOMTX.getValueAt(i,intTB2_TXMOD).toString().length() ==0)
    			     {
    			        setMSG("Tax Mode not be blank..",'E');
    			        return false;
    			     }
			    }
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	private void getDATA(String P_strDOCNO)
	{
		try
		{
		    flgPRMOD = false;
		    double L_dblINVQT =0;
		    double L_dblPGRVL =0;
			tblINVDT.clrTABLE();
			tblCOMTX.clrTABLE();
			int i=0;
			flgCOMTX=false;
			String L_strMKTTP="";
			String L_strPRTCD="";
			String L_strTAXVL="";
			String L_strTAXFL="";
			String L_strDOCTP="";
			intRWCNT=0;
			String L_strTAXCD="";
			if(rdbCREDT.isSelected())
				strTRNTP="CR";
			else
				strTRNTP="DB";
			
			//if(!chkDOCNO())
			//	return;
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{   
			   /// if(!txtDOCTP.getText().equals(strOTHCN))
			   if((rdbCREDT.isSelected())&& (txtDOCTP.getText().trim().equals("02")||txtDOCTP.getText().trim().equals("03")))
			        flgPRMOD = true;
			   else
				   flgPRMOD = false;
			}
			else
			    flgPRMOD = false;
			if(tblINVDT.isEditing())
				tblINVDT.getCellEditor().stopCellEditing();
			tblINVDT.setRowSelectionInterval(0,0);
			tblINVDT.setColumnSelectionInterval(0,0);
			if(tblCOMTX.isEditing())
				tblCOMTX.getCellEditor().stopCellEditing();
			tblCOMTX.setRowSelectionInterval(0,0);
			tblCOMTX.setColumnSelectionInterval(0,0);
			String L_strCRDTP =txtDOCTP.getText().trim() ;
			if(L_strCRDTP.equals("09"))
			    L_strCRDTP =txtOTHCT.getText().trim();
			//if(txtOTHCT.getText().equals(strMISCN))
			if((L_strCRDTP.equals(strMISCN))||(L_strCRDTP.equals(strMISDN)))
			{
			    M_strSQLQRY = "Select PT_INVNO,PT_PRDCD,PT_PKGTP,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_SRLNO,";
    			M_strSQLQRY +=" PT_TRNTP,PT_DOCRF,PT_INVQT,PT_TRNRT,PT_PGRVL,PT_ATXVL,PT_LTXVL,";
    			M_strSQLQRY +=" PT_PNTVL,PT_INTVL,PT_ADJVL,PT_MKTTP,0 IVT_INVVL,'' IVT_PRDDS from MR_PTTRN ";
    			M_strSQLQRY +=" Where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_CRDTP='"+L_strCRDTP+"'";    // and IVT_MKTTP = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' ";
    			//M_strSQLQRY +=" AND PT_PRTTP ='"+txtPRTTP.getText().trim()+"'";
    			M_strSQLQRY +=" AND PT_DOCRF ='"+P_strDOCNO+"'";
    			if((txtFRDAT.getText().length() ==10)&&(txtTODAT.getText().length() ==10))
    			{
    			    M_strSQLQRY += " AND PT_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
    			}
    			if(txtPRTCD.getText().length() >0)
    			{
    			    M_strSQLQRY += " AND PT_PRTTP ='"+txtPRTTP.getText().trim() +"'";
    			    M_strSQLQRY += " AND PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
    			}
			}
			/*else if(txtOTHCT.getText().equals(strPDFCN))
			{
			    M_strSQLQRY = "Select PT_INVNO,PT_PRDCD,PT_PKGTP,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_SRLNO,";
    			M_strSQLQRY +="PT_TRNTP,PT_DOCRF,PT_INVQT,PT_TRNRT,PT_PGRVL,PT_ATXVL,PT_LTXVL,";
    			M_strSQLQRY +="PT_PNTVL,PT_INTVL,PT_ADJVL,PT_MKTTP,IVT_INVVL,IVT_PRDDS from MR_PTTRN,MR_IVTRN ";
    			M_strSQLQRY +=" Where PT_CMPCD = '"+strCMPCD+"' AND PT_CRDTP='"+L_strCRDTP+"'";
    			//M_strSQLQRY +=" AND PT_PRTTP ='"+txtPRTTP.getText().trim()+"'";
    			M_strSQLQRY +=" AND PT_DOCRF ='"+P_strDOCNO+"'";
    			if((txtFRDAT.getText().length() ==10)&&(txtTODAT.getText().length() ==10))
    			{
    			    M_strSQLQRY += " AND PT_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
    			}
    			if(txtPRTCD.getText().length() >0)
    			{
    			    M_strSQLQRY += " AND PT_PRTTP ='"+txtPRTTP.getText().trim() +"'";
    			    M_strSQLQRY += " AND PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
    			}
    			M_strSQLQRY +=" AND PT_INVNO= IVT_INVNO AND PT_PRDCD = IVT_PRDCD" ;
    			M_strSQLQRY +=" AND PT_PKGTP= IVT_PKGTP ORDER BY IVT_INVNO";
				M_strSQLQRY += " AND IVT_PRDCD ='"+txtPRDCD1.getText().trim()+"'";
			}*/
			else
			{
    			M_strSQLQRY = "Select PT_INVNO,PT_PRDCD,PT_PKGTP,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_SRLNO,";
    			M_strSQLQRY +=" PT_TRNTP,PT_DOCRF,PT_INVQT,PT_TRNRT,PT_PGRVL,PT_ATXVL,PT_LTXVL,";
    			M_strSQLQRY +=" PT_PNTVL,PT_INTVL,PT_ADJVL,PT_MKTTP,IVT_INVVL,IVT_PRDDS from MR_PTTRN,MR_IVTRN ";
    			M_strSQLQRY +=" Where PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_CRDTP='"+L_strCRDTP+"'";
    			//M_strSQLQRY +=" AND PT_PRTTP ='"+txtPRTTP.getText().trim()+"'";
    			M_strSQLQRY +=" AND PT_DOCRF ='"+P_strDOCNO+"'";
    			if((txtFRDAT.getText().length() ==10)&&(txtTODAT.getText().length() ==10))
    			{
    			    M_strSQLQRY += " AND PT_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
    			}
    			if(txtPRTCD.getText().length() >0)
    			{
    			    M_strSQLQRY += " AND PT_PRTTP ='"+txtPRTTP.getText().trim() +"'";
    			    M_strSQLQRY += " AND PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
    			}
    			M_strSQLQRY +=" AND PT_INVNO= IVT_INVNO AND PT_PRDCD = IVT_PRDCD" ;
    			M_strSQLQRY +=" AND PT_PKGTP= IVT_PKGTP ORDER BY IVT_INVNO";
			}
			
			/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			{
				M_strSQLQRY +="	and PT_STSFL <> 'X'";
			}
            else
				M_strSQLQRY +="	and PT_STSFL not in('1','X')";*/
	        System.out.println(M_strSQLQRY);
	       	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			String L_strFRDAT = txtFRDAT.getText().trim();
			String L_strTODAT = txtTODAT.getText().trim();
			String L_strPRTTP = txtPRTTP.getText().trim();
			String L_strOTHCT = txtOTHCT.getText().trim();
			L_strPRTCD = txtPRTCD.getText().trim();
			L_strDOCTP = txtDOCTP.getText().trim();
			clrCOMP();
			txtREMDS.setText("");
			txtFRDAT.setText(L_strFRDAT);
			txtTODAT.setText(L_strTODAT);
			txtPRTTP.setText(L_strPRTTP);
			txtPRTCD.setText(L_strPRTCD);
			txtDOCTP.setText(L_strDOCTP);
			txtOTHCT.setText(L_strOTHCT);
			lblINVQT.setText("");
			lblPGRVL.setText("");
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(intRWCNT > 1000)
					{
					    setMSG("Transactions greater than no. of Rows in table, contact Systems ",'E');
				        return;	    
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				    {
						tblINVDT.cmpEDITR[intTB1_CHKFL].setEnabled(true);
					}
					txtPRTTP.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTTP"),""));
					//txtDOCTP.setText(nvlSTRVL(M_rstRSSET.getString("PT_CRDTP"),""));
					txtDOCRF.setText(nvlSTRVL(M_rstRSSET.getString("PT_DOCRF"),""));
					
					L_strPRTCD=nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
					txtPRTCD.setText(L_strPRTCD);
					if(flgPRMOD)
					{
					    tblINVDT.setValueAt(Boolean.TRUE,i,intTB1_CHKFL); 
					    //System.out.println(intRWCNT);
					    if((intRWCNT ==0)&& (L_strFRDAT.length()==10) && (L_strTODAT.length() ==10))
                        {
                            if(txtDOCTP.getText().trim().equals("02"))
					            txtREMDS.setText("DISTRIBUTOR BOOKING COMMISSION FOR THE PERIOD "+L_strFRDAT+ " TO " +L_strTODAT);   
                            else
					            txtREMDS.setText("DISTRIBUTOR COMMISSION FOR THE PERIOD "+L_strFRDAT+ " TO " +L_strTODAT);   
                        }
                    }
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_INVNO"),i,intTB1_INVNO);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_PRDCD"),i,intTB1_PRGRD);
					tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PRDDS"),i,intTB1_PRDDS);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_PKGTP"),i,intTB1_PKGTP);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_SRLNO"),i,intTB1_SRLNO);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_TRNRT"),i,intTB1_DOCRT);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_INVQT"),i,intTB1_INVQT);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_PGRVL"),i,intTB1_PGRVL);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_ATXVL"),i,intTB1_ATXVL);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_LTXVL"),i,intTB1_LTXVL);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_PNTVL"),i,intTB1_PNTVL);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_INTVL"),i,intTB1_INTVL);
					tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVVL"),i,intTB1_INVVL);
					tblINVDT.setValueAt(M_rstRSSET.getString("PT_INVQT"),i,intTB1_INVQT1);
				//	tblINVDT.setValueAt(M_rstRSSET.getString("PT_ADJVL"),i,intTB1_RCTVL);
					L_strMKTTP=M_rstRSSET.getString("PT_MKTTP");
					strTRNTP=M_rstRSSET.getString("PT_TRNTP");
					L_dblINVQT += Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PT_INVQT"),"0"));
					L_dblPGRVL += Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PT_PGRVL"),"0"));
					//if(txtOTHCT.getText().equals(strMISCN))
					if((L_strCRDTP.equals(strMISCN))||(L_strCRDTP.equals(strMISDN)))
					{
				        txtCRDVL.setText(M_rstRSSET.getString("PT_PGRVL"));	    
					}
					intRWCNT++;
					i++;
				}
				if(flgPRMOD)
				{
				    lblINVQT.setText(setNumberFormat(L_dblINVQT,3));
				    lblPGRVL.setText(setNumberFormat(L_dblPGRVL,2));
				}
			}
			//for(int j=0;j<cmbMKTTP.getItemCount();j++)
			//{
			//	if(L_strMKTTP.equals((cmbMKTTP.getItemAt(j).toString()).substring(0,2)))
			//	{
			//		cmbMKTTP.setSelectedIndex(j);
			//	}
			//}
			if(intRWCNT==0)
			{
				setMSG("Data not found",'E');
				return;
			}
			else
			{
				M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTTP='"+txtPRTTP.getText().trim()+"'";
				M_strSQLQRY +=" AND PT_PRTCD= '"+L_strPRTCD+"'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
					}
					M_rstRSSET.close();
				}
				if(!flgPRMOD)
				{
    				M_strSQLQRY = "Select RM_REMDS,RM_STSFL from MR_RMMST ";
    				M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = '" + strTRNTP + "'";
                    M_strSQLQRY += " and  RM_MKTTP = '" + cmbMKTTP.getSelectedItem().toString().substring(0,2) + "'";
    				M_strSQLQRY += " and RM_DOCNO = '" + txtDOCRF.getText().trim() + "' and  isnull(RM_STSFL,'') <> 'X'";
    				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
    				txtREMDS.setText("");
    				if(M_rstRSSET != null)
    				{
    					if(M_rstRSSET.next())
    					{
    						if(!nvlSTRVL(M_rstRSSET.getString("RM_STSFL"),"").equals("X"))
    							txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
    						else
    							txtREMDS.setText("");
    					}
    					M_rstRSSET.close();
    				}
    				M_strSQLQRY="select * from CO_TXDOC where TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_DOCNO='"+txtDOCRF.getText().trim() +"' AND TX_SBSTP='"+L_strMKTTP+"' AND isnull(TX_STSFL,'') <> 'X'";
    				M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
    				if(M_rstRSSET!=null)
    				{
    					i=0;
    					intCOMTX=0;
    					while(M_rstRSSET.next())
    					{
    						for(int j=0;j<vtrTAXCD.size();j++)
    						{		
    							L_strTAXCD=(String)vtrTAXCD.get(j);
    							L_strTAXFL="TX_"+L_strTAXCD+"FL";
    							String L_strTAXFL1=nvlSTRVL(M_rstRSSET.getString(L_strTAXFL),"null");
    							if(!L_strTAXFL1.equals("null"))
    							{
    								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
    								{
    									tblCOMTX.cmpEDITR[intTB2_CHKFL].setEnabled(true);
    								}
    								L_strTAXVL="TX_"+L_strTAXCD+"VL";
    								L_strDOCTP=M_rstRSSET.getString("TX_DOCTP");
    								if(L_strDOCTP.equals("CRA")||L_strDOCTP.equals("DBA"))
    								{
    									tblCOMTX.setValueAt("A",i,intTB2_TXMOD);
        								tblCOMTX.setValueAt(M_rstRSSET.getString(L_strTAXFL),i,intTB2_TAXFL);
        								tblCOMTX.setValueAt(M_rstRSSET.getString(L_strTAXVL),i,intTB2_TAXVL);
        								tblCOMTX.setValueAt((String)hstTAXCD.get(L_strTAXCD),i,intTB2_TAXDS);
        								tblCOMTX.setValueAt(L_strTAXCD,i,intTB2_TAXCD);
        								intCOMTX++;
        								i++;
    								    flgCOMTX=true;
    
    								}
    								else if(L_strDOCTP.equals("CRD")||L_strDOCTP.equals("DBD"))
    								{
    									tblCOMTX.setValueAt("D",i,intTB2_TXMOD);
    									tblCOMTX.setValueAt(M_rstRSSET.getString(L_strTAXFL),i,intTB2_TAXFL);
        								tblCOMTX.setValueAt(M_rstRSSET.getString(L_strTAXVL),i,intTB2_TAXVL);
        								tblCOMTX.setValueAt((String)hstTAXCD.get(L_strTAXCD),i,intTB2_TAXDS);
        								tblCOMTX.setValueAt(L_strTAXCD,i,intTB2_TAXCD);
        								intCOMTX++;
        								i++;
    								    flgCOMTX=true;
    								}
    								/*tblCOMTX.setValueAt(M_rstRSSET.getString(L_strTAXFL),i,intTB2_TAXFL);
    								tblCOMTX.setValueAt(M_rstRSSET.getString(L_strTAXVL),i,intTB2_TAXVL);
    								tblCOMTX.setValueAt((String)hstTAXCD.get(L_strTAXCD),i,intTB2_TAXDS);
    								tblCOMTX.setValueAt(L_strTAXCD,i,intTB2_TAXCD);
    								intCOMTX++;
    								i++;
    								flgCOMTX=true;*/
    							}
    						}
    					}
    				}
    			} // if !flgprmod
			}
		}
		catch(Exception L_GD)
		{
			setMSG(L_GD,"GetDATA");
		}
	}
	
	void exeSAVE()
	{
		try
		{
			String L_strMKTTP="";
			String L_strDOCTP="";
			cl_dat.M_flgLCUPD_pbst = true;
			
			int L_intCOLA=0;
			int L_intCOLD=0;
			strTXVLA="";
			strTXVLD="";
			
			if(!vldDATA())
			{
				return;
			}
			if(rdbCREDT.isSelected())
				strTRNTP="CR";
			else
				strTRNTP="DB";
			int L_intSRLNO=0;
			String L_strSTSFL="0";
			
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				autCRDNT();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				delCRDNT();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				modCRDNT();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
			    double L_dblPNTVL =0;
				/*String L_strTRNTP="";
				String L_strTRNTP1="";
				String L_strINVNO="",L_strPRDCD="",L_strPKGTP="",L_strSRLNM="",L_strTEMPA="",L_strTEMPD="",L_strWRCON="";
		    	String L_strPGRVL ="";
		    	double L_dblPGRVL = 0.0;
		    	double L_dblPATAX = 0;
		    	double L_dblPLTAX = 0;
		    	double L_dblPNTVL = 0;*/
		    	if(getNEW_DOCRF())
				{
					L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
					L_strMKTTP=L_strMKTTP.substring(0,2);
					if(flgPRMOD)
					{
					   L_dblPNTVL =  PROCESS(txtDOCTP.getText().trim(),txtDOCRF.getText().trim(),"");
					}
					else
					{
					    L_strDOCTP = txtDOCTP.getText().trim();
                        if(txtOTHCT.getText().length() == 2)
                            L_strDOCTP =txtOTHCT.getText().trim();
					  //  if(txtOTHCT.getText().equals(strMISCN)) // dummy entries for MISc. credits
					   if(((L_strDOCTP.equals(strMISCN))||(L_strDOCTP.equals(strMISDN))) && !chkWIINV.isSelected())
					    {
					       // L_strDOCTP =txtOTHCT.getText().trim();
					        M_strSQLQRY  = "insert into MR_PTTRN (PT_CMPCD,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_TRNTP,PT_DOCRF,PT_DOCDT,PT_MKTTP,";
							M_strSQLQRY += "PT_INVNO,PT_PRDCD,PT_PKGTP,PT_SRLNO,PT_TRNRT,PT_INVQT,";
							M_strSQLQRY += "PT_PGRVL,PT_ATXVL,PT_LTXVL,PT_PNTVL,PT_INTVL,PT_SBSCD,PT_SBSCD1,PT_TRNFL,PT_STSFL,PT_LUSBY,PT_LUPDT) values (";
							M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'"+L_strDOCTP+"',";
							M_strSQLQRY += "'"+txtPRTTP.getText().trim()+"',";
							M_strSQLQRY += "'"+txtPRTCD.getText().trim()+"',";
							M_strSQLQRY += "'"+strTRNTP+"',";
							M_strSQLQRY += "'"+txtDOCRF.getText().trim()+"',";
							//M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
							M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse((cl_dat.M_strLOGDT_pbst)))+"',";
							M_strSQLQRY += "'"+L_strMKTTP+"',";
							M_strSQLQRY += "'"+txtDOCRF.getText().trim()+"',";//Credit note no. as strDUMINV 
							M_strSQLQRY += "'"+strDUMPRD+"',";
							M_strSQLQRY += "'',"; // PKGTP
							M_strSQLQRY += "'01',";// SRLNO
							M_strSQLQRY += "0,"; // docrt
							M_strSQLQRY += "0,"; // INVQT
							M_strSQLQRY += ""+txtCRDVL.getText()+",";
							M_strSQLQRY += "0.0,"; // atxvl
							M_strSQLQRY += "0.0,"; // ltxvl
							M_strSQLQRY += ""+txtCRDVL.getText()+",";//pntvl
							M_strSQLQRY += "0.0,";//intvl
							M_strSQLQRY += "'"+M_strSBSCD+"',";
							M_strSQLQRY += "'"+M_strSBSCD.substring(0,2)+getPRDTP(txtPRTCD.getText().trim())+"00',";
						    M_strSQLQRY += getUSGDTL("PR_",'I',"1")+")";
						    System.out.println(M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");

					    }
					    else
					    {
					       	for(int i=0;i < tblINVDT.getRowCount();i++)
        					{
        						if(tblINVDT.getValueAt(i,intTB1_CHKFL).toString().trim().equals("true"))
        						{
        							M_strSQLQRY  = "insert into MR_PTTRN (PT_CMPCD,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_TRNTP,PT_DOCRF,PT_DOCDT,PT_MKTTP,";
        							M_strSQLQRY += "PT_INVNO,PT_PRDCD,PT_PKGTP,PT_SRLNO,PT_TRNRT,PT_INVQT,";
        							M_strSQLQRY += "PT_PGRVL,PT_ATXVL,PT_LTXVL,PT_PNTVL,PT_INTVL,PT_SBSCD,PT_SBSCD1,PT_TRNFL,PT_STSFL,PT_LUSBY,PT_LUPDT) values (";
        							M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
        							M_strSQLQRY += "'"+L_strDOCTP+"',";
        							M_strSQLQRY += "'"+txtPRTTP.getText().trim()+"',";
        							M_strSQLQRY += "'"+txtPRTCD.getText().trim()+"',";
        							M_strSQLQRY += "'"+strTRNTP+"',";
        							M_strSQLQRY += "'"+txtDOCRF.getText().trim()+"',";
        							M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse((cl_dat.M_strLOGDT_pbst)))+"',";
        							M_strSQLQRY += "'"+L_strMKTTP+"',";
        							M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_INVNO).toString()+"',";
        							if(L_strDOCTP.equals(strPDFCN))
        							{
        							    M_strSQLQRY += "'"+txtPRDCD1.getText().trim()+"',";
            							M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_OTHVL).toString()+"',"; // PKG Type
            							if(i <= 9)
            							    M_strSQLQRY += "'"+"0"+i+"',";   
            							else
            							    M_strSQLQRY += "'"+i+"',";   
        							}
        							else
        							{
            							M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_PRGRD).toString()+"',";
            							M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_PKGTP).toString()+"',";
            							M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_SRLNO).toString()+"',";
        							}
        							M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_DOCRT).toString()+",";
        							M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_INVQT).toString()+",";
        							M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_PGRVL).toString()+",";
        							M_strSQLQRY += ""+nvlSTRVL(tblINVDT.getValueAt(i,intTB1_ATXVL).toString(),"0.0")+",";
        							M_strSQLQRY += ""+nvlSTRVL(tblINVDT.getValueAt(i,intTB1_LTXVL).toString(),"0.0")+",";
        							M_strSQLQRY += ""+nvlSTRVL(tblINVDT.getValueAt(i,intTB1_PNTVL).toString(),"0.0")+",";
        							M_strSQLQRY += ""+nvlSTRVL(tblINVDT.getValueAt(i,intTB1_INTVL).toString(),"0.0")+",";
        							M_strSQLQRY += "'"+M_strSBSCD+"',";
									M_strSQLQRY += "'"+M_strSBSCD.substring(0,2)+getPRDTP(txtPRTCD.getText().trim())+"00',";
        							if(flgPRMOD)
        							    M_strSQLQRY += getUSGDTL("PR_",'I',"1")+")";
        							else
        							    M_strSQLQRY += getUSGDTL("PR_",'I',"1")+")";
        							System.out.println(M_strSQLQRY);
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								}
    						}
    					}
    					insTXDOC();
    					updDOCNO();
					}
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					strREMDS=txtREMDS.getText().trim();
					if(strREMDS.length()>0)
					{
						M_strSQLQRY  = "Insert into MR_RMMST(RM_CMPCD,RM_MKTTP,RM_TRNTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_SBSCD,RM_SBSCD1,RM_LUSBY,RM_LUPDT) values (";//,RM_SBSCD
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'"+L_strMKTTP+"',";
						M_strSQLQRY += "'"+strTRNTP+"',";
						M_strSQLQRY += "'"+txtDOCRF.getText()+"',";
						M_strSQLQRY += "'"+txtREMDS.getText()+"',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'1',";
						M_strSQLQRY += "'"+M_strSBSCD+"',";
						M_strSQLQRY += "'"+M_strSBSCD.substring(0,2)+"XX00',";
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse((cl_dat.M_strLOGDT_pbst)))+"')";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Data saved successfully",'N');
						if(flgPRMOD)
						{
							//System.out.println("flgPRMOD");
						    M_strSQLQRY = "select round(sum(PT_PNTVL),0) L_PNTVL from mr_pttrn  where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and PT_PRTTP = '"+txtPRTTP.getText().trim()+"' and PT_PRTCD = '"+txtPRTCD.getText().trim()+"' and PT_CRDTP = '"+txtDOCTP.getText().trim()+"' and PT_DOCRF = '"+txtDOCRF.getText().trim()+"' and PT_STSFL = '1'" ;
						    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						    if(M_rstRSSET !=null)
						    {
						        if(M_rstRSSET.next())
						            L_dblPNTVL = M_rstRSSET.getDouble("L_PNTVL");
						        M_rstRSSET.close();    
						    }
						   /* System.out.println("calling stored procedure");
						    System.out.println(strCMPCD);
						    System.out.println(txtDOCTP.getText().trim());
						    System.out.println(txtPRTCD.getText().trim());
						    System.out.println(txtDOCRF.getText().trim());
						    System.out.println(M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText())));
						    System.out.println(L_dblPNTVL);
						  */
						//	System.out.println("flgPRMOD1");
						    double L_dblRETVL = 0.00;
						    cstPLTRN.setString(1,cl_dat.M_strCMPCD_pbst);
						   	cstPLTRN.setString(2,txtDOCTP.getText().trim());
						 	cstPLTRN.setString(3,txtPRTCD.getText().trim());
                    		cstPLTRN.setString(4,txtDOCRF.getText().trim());
                    		cstPLTRN.setString(5,M_fmtDBDAT.format(M_fmtLCDAT.parse((cl_dat.M_strLOGDT_pbst))));
                    		cstPLTRN.setDouble(6,L_dblPNTVL);
                    		//System.out.println("Before register");
                    		//cstPLTRN.registerOutParameter(7,Types.DOUBLE);
                    		// System.out.println("Before execute update");
                    		System.out.println("BEFORE update");
                    		 cstPLTRN.executeUpdate();
                    		// System.out.println("L_dblRETVL : "+L_dblRETVL);
                    		System.out.println("after update");
                    		//System.out.println("Net value from procedure "+cstPLTRN.getDouble(7));
                    		cl_dat.exeDBCMT("after proceduue");
                    		System.out.println("procedure call complete");
                    		JOptionPane.showMessageDialog(this,"Please Note Down the Transaction Number\n"+txtDOCRF.getText().toString(),"",JOptionPane.ERROR_MESSAGE);
						}
                        else  
                        {
							System.out.println("111");
							if((L_strDOCTP.equals(strINTDN))||(L_strDOCTP.equals(strOTHDN)))
							{
								cstPLTRN_ODB.setString(1,cl_dat.M_strCMPCD_pbst);
								cstPLTRN_ODB.setString(2,txtDOCRF.getText().trim());
                    			cstPLTRN_ODB.executeUpdate();
                    			cl_dat.exeDBCMT("after procedure PLTRN_ODB");
							}
							else
							{
								System.out.println("inside else");
								  M_strSQLQRY = "select round(sum(PT_PNTVL),0) L_PNTVL from mr_pttrn  where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and PT_PRTTP = '"+txtPRTTP.getText().trim()+"' and PT_PRTCD = '"+txtPRTCD.getText().trim()+"' and PT_CRDTP = '"+L_strDOCTP+"' and PT_DOCRF = '"+txtDOCRF.getText().trim()+"' and PT_STSFL = '1'" ;
								  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
								  System.out.println(M_strSQLQRY);
								 if(M_rstRSSET !=null)
								 {
									if(M_rstRSSET.next())
									{	 
										L_dblPNTVL = M_rstRSSET.getDouble("L_PNTVL");
										System.out.println("L_PNTVL : "+L_dblPNTVL);
									}	 
								     M_rstRSSET.close();    
								 }
									
									
								 double L_dblRETVL = 0.00;
									cstPLTRN_OTH.setString(1,cl_dat.M_strCMPCD_pbst);
									cstPLTRN_OTH.setString(2,cmbMKTTP.getSelectedItem().toString().substring(0,2));
									cstPLTRN_OTH.setString(3,L_strDOCTP);
									cstPLTRN_OTH.setString(4,txtPRTTP.getText().trim());
						 			cstPLTRN_OTH.setString(5,txtPRTCD.getText().trim());
                    				cstPLTRN_OTH.setString(6,txtDOCRF.getText().trim());
                    				cstPLTRN_OTH.setString(7,M_fmtDBDAT.format(M_fmtLCDAT.parse((cl_dat.M_strLOGDT_pbst))));
                    				cstPLTRN_OTH.setDouble(8,L_dblPNTVL);
									
                    				//System.out.println("Before register");
                    				//cstPLTRN.registerOutParameter(7,Types.DOUBLE);
                    				// System.out.println("Before execute update");
                    			    System.out.println("BEFORE update");
									cstPLTRN_OTH.executeUpdate();
    								// Adjustment to be done only for Booking discount category
    								if(txtOTHCT.getText().equals(strDISCN))
    								{
    									cstPATRN_OTH.setString(1,cl_dat.M_strCMPCD_pbst);
    									cstPATRN_OTH.setString(2,L_strDOCTP);
    									cstPATRN_OTH.setString(3,txtDOCRF.getText().trim());
    									cstPATRN_OTH.executeUpdate();
    								}
									// System.out.println("L_dblRETVL : "+L_dblRETVL);
                    				//System.out.println("Net value from procedure "+cstPLTRN.getDouble(7));
                    				cl_dat.exeDBCMT("after proceduue");
                    				System.out.println("procedure call complete");
							}
                            JOptionPane.showMessageDialog(this,"Please Note Down the Transaction Number\n"+txtDOCRF.getText().toString(),"",JOptionPane.ERROR_MESSAGE);
                        }
                     	clrCOMP();
                     	txtREMDS.setText("");
						setMSG("Data saved successfully",'N');
					}
				}
				else
				{
				    if(flgPRMOD)
				    {
				        System.out.println("Resetting");
				        PROCESS(txtDOCTP.getText().trim(),txtDOCRF.getText().trim(),"RESET");
				    }
				  	setMSG("Error in saving",'E');
				}
			}
		}
		catch(Exception L_E)
		{
		    if(flgPRMOD)
		    {
		        System.out.println("Resetting");
		        PROCESS(txtDOCTP.getText().trim(),txtDOCRF.getText().trim(),"RESET");
		    }
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	 	setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	private void modCRDNT()
	{
		try
		{
			String L_strINVNO="",L_strPRDCD="",L_strPKGTP="",L_strSRLNM="",L_strMKTTP="",L_strTEMPA="",L_strTEMPD="",L_strWRCON="";
			String L_strDOCTP="";
			strTXVLD="";
			strTXVLA="";
			int L_intCOLA=0;
			int L_intCOLD=0;
			M_strSQLQRD="";
			String L_strSQLQRD="";
			String L_strSQLQRY="";
			String L_strTRNTP="";
			String L_strTRNTP1="";
			L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
			L_strMKTTP=L_strMKTTP.substring(0,2);
			for(int i=0;i<tblINVDT.getRowCount();i++)
			{
				if(tblINVDT.getValueAt(i,intTB1_CHKFL).toString().equals("true"))
				{
					L_strINVNO=tblINVDT.getValueAt(i,intTB1_INVNO).toString();
					L_strPRDCD=tblINVDT.getValueAt(i,intTB1_PRGRD).toString();
					L_strPKGTP=tblINVDT.getValueAt(i,intTB1_PKGTP).toString();
					L_strSRLNM=tblINVDT.getValueAt(i,intTB1_SRLNO).toString();
					if(i<intRWCNT)
					{
						M_strSQLQRY="UPDATE MR_PTTRN SET PT_TRNRT= "+tblINVDT.getValueAt(i,intTB1_DOCRT).toString()+",";
						M_strSQLQRY +="PT_STSFL = '0', ";
						M_strSQLQRY +="PT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						M_strSQLQRY +="PT_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
						M_strSQLQRY += " WHERE PT_CMPCD = '" +cl_dat.M_strCMPCD_pbst+ "'";
						M_strSQLQRY +=" AND PT_PRTTP='"+txtPRTTP.getText().trim()+"'";
						M_strSQLQRY += " AND PT_INVNO= '" +L_strINVNO+ "'";
						M_strSQLQRY += " AND PT_PRDCD = '" +L_strPRDCD+ "'";
						M_strSQLQRY += " AND PT_PKGTP = '" +L_strPKGTP+ "'";
						M_strSQLQRY += " AND PT_SRLNO = '" +L_strSRLNM + "'";
						M_strSQLQRY += " AND PT_PRTCD = '" +txtPRTCD.getText().trim()+ "'";
						M_strSQLQRY += " AND PT_CRDTP = '" +txtDOCTP.getText().trim()+"'";
					}
					else
					{
						M_strSQLQRY  = "insert into MR_PTTRN (PT_CMPCD,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_TRNTP,PT_DOCRF,PT_DOCDT,PT_MKTTP,";
						M_strSQLQRY += "PT_INVNO,PT_PRDCD,PT_PKGTP,PT_SRLNO,PT_TRNRT,PT_INVQT,";
						M_strSQLQRY += "PT_PGRVL,PT_ATXVL,PT_LTXVL,PT_PNTVL,PT_INTVL,PT_SBSCD,PT_SBSCD1,PT_STSFL,PT_TRNFL,PT_LUSBY,PT_LUPDT) values (";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'"+txtDOCTP.getText().trim()+"',";
						M_strSQLQRY += "'"+txtPRTTP.getText().trim()+"',";
						M_strSQLQRY += "'"+txtPRTCD.getText().trim()+"',";
						M_strSQLQRY += "'"+strTRNTP+"',";
						M_strSQLQRY += "'"+txtDOCRF.getText().trim()+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse((cl_dat.M_strLOGDT_pbst)))+"',";
						M_strSQLQRY += "'"+L_strMKTTP+"',";
						M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_INVNO).toString()+"',";
						M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_PRGRD).toString()+"',";
						M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_PKGTP).toString()+"',";
						M_strSQLQRY += "'"+tblINVDT.getValueAt(i,intTB1_SRLNO).toString()+"',";
						M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_DOCRT).toString()+",";
						M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_INVQT).toString()+",";
						M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_PGRVL).toString()+",";
						
						M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_ATXVL).toString()+",";
						M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_LTXVL).toString()+",";
						M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_PNTVL).toString()+",";
						M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_INTVL).toString()+",";
				//		M_strSQLQRY += ""+tblINVDT.getValueAt(i,intTB1_RCTVL).toString()+",";
						M_strSQLQRY += "'"+M_strSBSCD+"',";
						M_strSQLQRY += "'"+M_strSBSCD.substring(0,2)+getPRDTP(txtPRTCD.getText().trim())+"00',";
						M_strSQLQRY += getUSGDTL("PR_",'I',"0")+")";
					}
					//System.out.println("MODIFY MR_PTTRN = "+M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				flgREMRK=chkREMRK();
				if(flgREMRK)
				{
					M_strSQLQRY="UPDATE  MR_RMMST  SET RM_REMDS= '"+txtREMDS.getText().trim()+"' , ";
					M_strSQLQRY += getUSGDTL("RM",'U',"");
					M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = '" + strTRNTP + "'";
					M_strSQLQRY += " and RM_DOCNO = '" + txtDOCRF.getText().trim() + "' and  isnull(RM_STSFL,'') <> 'X'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
					strREMDS=txtREMDS.getText().trim();
					if(strREMDS.length()>0)
					{
						M_strSQLQRY  = "Insert into MR_RMMST(RM_CMPCD,RM_MKTTP,RM_TRNTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_SBSCD,RM_SBSCD1,RM_LUSBY,RM_LUPDT) values (";//,RM_SBSCD
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'"+L_strMKTTP+"',";
						M_strSQLQRY += "'"+strTRNTP+"',";
						M_strSQLQRY += "'"+txtDOCRF.getText()+"',";
						M_strSQLQRY += "'"+txtREMDS.getText()+"',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'1',";
						M_strSQLQRY += "'"+M_strSBSCD+"',";
						M_strSQLQRY += "'"+M_strSBSCD.substring(0,2)+"XX00',";
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse((cl_dat.M_strLOGDT_pbst)))+"')";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				// update Query
				if(flgCOMTX)
				{
					M_strSQLQRY="";
					L_strWRCON=" Where TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_DOCNO='"+txtDOCRF.getText().trim()+"' And TX_SYSCD='MR' AND TX_SBSTP='"+L_strMKTTP+"' ";
					for(int i=0;i < tblCOMTX.getRowCount();i++)
					{
						if(tblCOMTX.getValueAt(i,intTB2_CHKFL).toString().trim().equals("true"))
						{
						
							L_strDOCTP =tblCOMTX.getValueAt(i,intTB2_TXMOD).toString();
							if(L_strDOCTP.equals("A"))
							{
								L_strTEMPA  = " AND TX_DOCTP = '"+strTRNTP+"A'";
								L_strSQLQRY += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"VL="+ tblCOMTX.getValueAt(i,intTB2_TAXVL).toString()+",";
								L_strSQLQRY += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"FL = '" +tblCOMTX.getValueAt(i,intTB2_TAXFL).toString()+"',";
								L_intCOLA++;
							//	M_strSQLQRY +="where "+ L_strWRCON +L_strTEMPA;
							}
							if(L_strDOCTP.equals("D"))
							{
								L_strTEMPD = " AND TX_DOCTP = '"+strTRNTP+"D'";
								L_strSQLQRD += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"VL="+ tblCOMTX.getValueAt(i,intTB2_TAXVL).toString()+",";
								L_strSQLQRD += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"FL = '" +tblCOMTX.getValueAt(i,intTB2_TAXFL).toString()+"',";
							//	M_strSQLQRY +="where "+ L_strWRCON +L_strTEMPD;
								L_intCOLD++;
							}
						}
					}
					if(L_intCOLA>0)
					{
						L_strSQLQRY =L_strSQLQRY.substring(0,L_strSQLQRY.length()-1);
						
						M_strSQLQRY +="UPDATE  CO_TXDOC  SET "+L_strSQLQRY+" "+L_strWRCON +L_strTEMPA ;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					if(L_intCOLD>0)
					{
						L_strSQLQRD =L_strSQLQRD.substring(0,L_strSQLQRD.length()-1);
						M_strSQLQRD +="UPDATE  CO_TXDOC  SET "+L_strSQLQRD+" "+L_strWRCON +L_strTEMPD ;
						cl_dat.exeSQLUPD(M_strSQLQRD ,"setLCLUPD");
					}
				}
				//insert Query
				else
				    insTXDOC();
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeMOD"))
				{
					clrCOMP();
					txtREMDS.setText("");
					setMSG("Data Update successfully",'N');
				}
			}
			else
			{
				setMSG("Error in Updating",'E');
			}
		}
		catch(Exception E_LX)
		{
			setMSG(E_LX,"MOdCRDNT");
		}
	}
	/**
	 * Function For Deletion of Credit/Debit Note
	*/
	private void delCRDNT()
	{
		String L_strINVNO="",L_strPRDCD="",L_strPKGTP="",L_strCRDTP="",L_strPRTTP="",L_strPRTCD="",L_strSRLNO="";
		String L_strMKTTP="",L_strTEMPA="",L_strTEMPD="",L_strWRCON="";
		String L_strDOCTP="";
		strTXVLD="";
		strTXVLA="";
		int L_intCOLA=0;
		int L_intCOLD=0;
		int L_intDLCNT=0;
		M_strSQLQRD="";
		String L_strSQLQRD="";
		String L_strSQLQRY="";
		String L_strTRNTP="";
		String L_strTRNTP1="";
		try
		{
			L_strMKTTP=cmbMKTTP.getSelectedItem().toString();
			L_strMKTTP=L_strMKTTP.substring(0,2);
			for(int i=0;i<tblINVDT.getRowCount();i++)
			{
				if(tblINVDT.getValueAt(i,intTB1_CHKFL).toString().equals("true"))
				{
					L_strINVNO=tblINVDT.getValueAt(i,intTB1_INVNO).toString();
					L_strPRDCD=tblINVDT.getValueAt(i,intTB1_PRGRD).toString();
					L_strPKGTP=tblINVDT.getValueAt(i,intTB1_PKGTP).toString();
					L_strSRLNO=tblINVDT.getValueAt(i,intTB1_SRLNO).toString();
					M_strSQLQRY="UPDATE MR_PTTRN SET ";
					M_strSQLQRY +="PT_STSFL = 'X', ";
					M_strSQLQRY +="PT_TRNFL = '1', ";
					M_strSQLQRY +="PT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="PT_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
					M_strSQLQRY += " WHERE PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
					M_strSQLQRY +=" AND PT_PRTTP='"+txtPRTTP.getText().trim()+"'";
					M_strSQLQRY += " AND PT_INVNO= '" +L_strINVNO+ "'";
					M_strSQLQRY += " AND PT_PRDCD = '" +L_strPRDCD+ "'";
					M_strSQLQRY += " AND PT_PKGTP = '" +L_strPKGTP+ "'";
					M_strSQLQRY += " AND PT_SRLNO = '" +L_strSRLNO + "'";
					M_strSQLQRY += " AND PT_PRTCD = '" +txtPRTCD.getText().trim()+ "'";
					M_strSQLQRY += " AND PT_CRDTP = '" +txtDOCTP.getText().trim()+"'";					
					L_intDLCNT++;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			/**
			 *  When All Row of Invoice Table  will be deleted then
			 *  Status flag of Tax Table Will Be 'X'
			*/
			
			if(intRWCNT==L_intDLCNT)
			{
				M_strSQLQRY="UPDATE CO_TXDOC SET ";
				M_strSQLQRY +="TX_STSFL = 'X', ";
				M_strSQLQRY +="TX_TRNFL = '1', ";
				M_strSQLQRY +="TX_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
				M_strSQLQRY +="TX_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
				M_strSQLQRY +=" Where TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_DOCNO='"+txtDOCRF.getText().trim()+"' And TX_SYSCD='MR' AND TX_SBSTP='"+L_strMKTTP+"' ";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			/**
			 *  If some Row of Tax Table will be deleted then This loop will be Execute
			*/
			else if(flgCOMTX)
			{
				M_strSQLQRY="";
				
				L_strWRCON=" Where TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_DOCNO='"+txtDOCRF.getText().trim()+"' And TX_SYSCD='MR' AND TX_SBSTP='"+L_strMKTTP+"' ";
				for(int i=0;i < tblCOMTX.getRowCount();i++)
				{
					if(tblCOMTX.getValueAt(i,intTB2_CHKFL).toString().trim().equals("true"))
					{
						L_strDOCTP =tblCOMTX.getValueAt(i,intTB2_TXMOD).toString();
						if(L_strDOCTP.equals("A"))
						{
							L_strTEMPA  = " AND TX_DOCTP = '"+strTRNTP+"A'";
							L_strSQLQRY += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"VL= 0 ,";
							L_strSQLQRY += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"FL = ' ',";
							L_intCOLA++;
							//M_strSQLQRY +="where "+ L_strWRCON +L_strTEMPA;
						}
						if(L_strDOCTP.equals("D"))
						{
							L_strTEMPD = " AND TX_DOCTP = '"+strTRNTP+"D'";
							L_strSQLQRD += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"VL= 0 ,";
							L_strSQLQRD += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"FL = ' ',";
						//	M_strSQLQRY +="where "+ L_strWRCON +L_strTEMPD;
							L_intCOLD++;
						}
					}
				}
				if(L_intCOLA>0)
				{
					L_strSQLQRY =L_strSQLQRY.substring(0,L_strSQLQRY.length()-1);
					M_strSQLQRY +="UPDATE  CO_TXDOC  SET "+L_strSQLQRY+" "+L_strWRCON +L_strTEMPA ;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				if(L_intCOLD>0)
				{
					L_strSQLQRD =L_strSQLQRD.substring(0,L_strSQLQRD.length()-1);
					M_strSQLQRD +="UPDATE  CO_TXDOC  SET "+L_strSQLQRD+" "+L_strWRCON +L_strTEMPD ;
					cl_dat.exeSQLUPD(M_strSQLQRD ,"setLCLUPD");
				}							
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeMOD"))
				{
					clrCOMP();
					txtREMDS.setText("");
					setMSG("Data Deleted successfully",'N');
				}
			}
			else
			{
				setMSG("Error in Updating",'E');
			}
		}
		catch(Exception E_LX)
		{
			setMSG(E_LX,"delCRDNT");
		}
	}
	
	/**
	 * Function For Authorization of Credit/Debit Note
	*/
	
	private void autCRDNT()
	{
		try
		{
			String L_strDOCRF=txtDOCRF.getText().trim();
			M_strSQLQRY  ="update MR_PTTRN SET ";
			M_strSQLQRY +="PT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY +="PT_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY +="PT_STSFL= '1'";
			M_strSQLQRY  += " WHERE PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_CRDTP = '" + txtDOCTP.getText().trim() + "'";
			M_strSQLQRY += " and PT_DOCRF = '" + L_strDOCRF + "'";
			M_strSQLQRY += " and isnull(PT_STSFL,'') <> 'X'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
			
			M_strSQLQRY  ="update CO_TXDOC SET ";
			M_strSQLQRY +="TX_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY +="TX_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY +="TX_STSFL= '1'";
			M_strSQLQRY  += " WHERE TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_DOCNO = '" + L_strDOCRF + "'";
			M_strSQLQRY += " and isnull(TX_STSFL,'') <> 'X'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeAUTH"))
				{
					clrCOMP();
					txtREMDS.setText("");
					setMSG("Data Authorized successfully",'N');
				}
			}
			else
			{
				setMSG("Error In Saving Data",'E');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"AUTHCRDNT ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**
	 * Method to perform  check of the Remark In the Data Base, Before updating 
	 *new data in the data base.
	*/
	
	private boolean chkREMRK()
	{
		ResultSet L_rstRSSET;
		String L_strSQLQRY="";
		flgREMRK=false;
		try
		{
			L_strSQLQRY="SELECT RM_REMDS from MR_RMMST ";
			L_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = '" + strTRNTP + "'";
			L_strSQLQRY += " and RM_DOCNO = '" + txtDOCRF.getText().trim() + "' and  isnull(RM_STSFL,'') <> 'X'";
			L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY );
			if(L_rstRSSET!=null)
			{
				if(L_rstRSSET.next())
				{
					flgREMRK= true;
				}
				else
					flgREMRK=false;
			}
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"chkREMRK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return flgREMRK;
	}
	
	/**
	 *  method for generating new Credit Note No.
	*/	
	private boolean getNEW_DOCRF()
	{
		try
		{
			String L_strCRDTP = txtDOCTP.getText().trim();
						
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				return true;		// DOC->document  FGXXXRCT=Finish Good xx Receipt no    
			
			M_strSQLQRY="Select * from CO_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MRXXPTT' and CMT_CODCD='"+strYRDGT+L_strCRDTP+"'";
			//M_strSQLQRY="Select * from CO_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MRXXPTT' and CMT_CODCD='"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+L_strCRDTP+"'";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET==null || (!M_rstRSSET.next()))
			{
				setMSG("Credit Note series not found ..",'E');
				cl_dat.M_flgLCUPD_pbst = false;
				return false;
			}
			String L_strDOCRF=null;
			L_strDOCRF=Integer.toString(Integer.parseInt(M_rstRSSET.getString("CMT_CCSVL"))+1);// taken next Credit Note no (increase by 1)with parseInt.
			txtDOCRF.setText(L_strDOCRF);
			
			if(L_strDOCRF.length()<5)//
			{
				for(int i=0;i<=5;i++)
				{
					txtDOCRF.setText("0"+txtDOCRF.getText());
					if(txtDOCRF.getText().length()==5)
						break;
				}
			}	
			L_strDOCRF = txtDOCRF.getText();
			txtDOCRF.setText(strYRDGT+L_strCRDTP+txtDOCRF.getText());
			//txtDOCRF.setText(cl_dat.M_strFNNYR1_pbst.substring(3,4)+L_strCRDTP+txtDOCRF.getText());
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getNEW_DOCRF"); 
			return false;
		}
		return true;
	}
	private void updDOCNO()
	{
		String L_strCRDTP=txtDOCTP.getText().trim();
		//M_strSQLQRY = "update CO_CDTRN set CMT_CCSVL = '"+txtDOCRF.getText().substring(3,8)+"' where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MRXXPTT' and cmt_CODCD='"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+L_strCRDTP+"'";
		M_strSQLQRY = "update CO_CDTRN set CMT_CCSVL = '"+txtDOCRF.getText().substring(3,8)+"' where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MRXXPTT' and cmt_CODCD='"+strYRDGT+L_strCRDTP+"'";
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	}
	private void setDFTAX()
	{
	    try
	    {
	        int L_intTAXCT =0;
    	    M_strSQLQRY = "SELECT * FROM CO_CDTRN WHERE CMT_CGMTP ='TCL' AND CMT_CGSTP ='MRXXDCM'"
    	    +" ORDER BY CMT_CHP01 ASC, CMT_CODCD DESC";
    	    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    	    if(M_rstRSSET !=null)
    	    {
    	        while(M_rstRSSET.next())
    	        {
    	             if(txtDOCTP.getText().equals("02")||txtDOCTP.getText().equals("03"))
    	                tblCOMTX.setValueAt(new Boolean("true"),L_intTAXCT,intTB2_CHKFL);
    	            tblCOMTX.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"").substring(0,3),L_intTAXCT,intTB2_TAXCD);
    	            tblCOMTX.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),L_intTAXCT,intTB2_TAXDS);
    	            tblCOMTX.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),""),L_intTAXCT,intTB2_TAXFL);
    	            tblCOMTX.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),""),L_intTAXCT,intTB2_TXMOD);
    	            tblCOMTX.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),""),L_intTAXCT,intTB2_TAXVL);
    	            L_intTAXCT++;
    	        }
    	        M_rstRSSET.close();
    	    }
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"setDFTAX");
	    }
	}
	private void insTXDOC()
	{
	   try
	   {
	        String L_strDOCTP ="",L_strTRNTP ="",L_strTRNTP1="",strTXVLA="",strTXVLD="",L_strMKTTP="";
	        int L_intCOLA =0;
	        int L_intCOLD =0; 
	        L_strMKTTP=cmbMKTTP.getSelectedItem().toString().substring(0,2);
			M_strSQLQRY  = "insert into CO_TXDOC (TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,";
			M_strSQLQRY += "TX_PRDCD,TX_SBSCD,TX_TRNTP, ";
			M_strSQLQRD  = "insert into CO_TXDOC (TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,";
			M_strSQLQRD += "TX_PRDCD,TX_SBSCD,TX_TRNTP, ";
			for(int i=0;i < tblCOMTX.getRowCount();i++)
			{
				if(tblCOMTX.getValueAt(i,intTB2_CHKFL).toString().trim().equals("true"))
				{
					L_strDOCTP =tblCOMTX.getValueAt(i,intTB2_TXMOD).toString();
					if(L_strDOCTP.equals("A"))
					{
						L_strTRNTP=strTRNTP+L_strDOCTP;
						M_strSQLQRY += "TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"VL,";
						M_strSQLQRY += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"FL,";
						strTXVLA +=tblCOMTX.getValueAt(i,intTB2_TAXVL).toString()+",'"+tblCOMTX.getValueAt(i,intTB2_TAXFL).toString()+"',";
						L_intCOLA++;
					}
					if(L_strDOCTP.equals("D"))
					{
						L_strTRNTP1=strTRNTP+L_strDOCTP;
						M_strSQLQRD += "TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"VL,";
						M_strSQLQRD += " TX_"+tblCOMTX.getValueAt(i,intTB2_TAXCD).toString()+"FL,";
						strTXVLD += tblCOMTX.getValueAt(i,intTB2_TAXVL).toString()+",'"+tblCOMTX.getValueAt(i,intTB2_TAXFL).toString()+"',";
						L_intCOLD++;
					}
				}
			}
			M_strSQLQRY += " TX_STSFL,TX_TRNFL,TX_LUSBY,TX_LUPDT )values(";
			M_strSQLQRD += " TX_STSFL,TX_TRNFL,TX_LUSBY,TX_LUPDT )values(";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'MR',";
			M_strSQLQRY += "'"+L_strMKTTP+"',";
			M_strSQLQRY += "'"+L_strTRNTP+"',";
			M_strSQLQRY += "'"+txtDOCRF.getText().trim()+"',";
			M_strSQLQRY += "'XXXXXXXXXX',";
			M_strSQLQRY += "'"+M_strSBSCD+"',";
			M_strSQLQRY += "'M',";
						
			M_strSQLQRD	+= "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRD += "'MR',";
			M_strSQLQRD += "'"+L_strMKTTP+"',";
			M_strSQLQRD += "'"+L_strTRNTP1+"',";
			M_strSQLQRD += "'"+txtDOCRF.getText().trim()+"',";
			M_strSQLQRD += "'XXXXXXXXXX',";
			M_strSQLQRD += "'"+M_strSBSCD+"',";
			M_strSQLQRD += "'M',";

			M_strSQLQRY += strTXVLA;
			M_strSQLQRD += strTXVLD;						
			if(flgPRMOD)
			{			
    			M_strSQLQRY += getUSGDTL("TX_",'I',"1")+")";
    			M_strSQLQRD += getUSGDTL("TX_",'I',"1")+")";
			}
			else
			{			
    			M_strSQLQRY += getUSGDTL("TX_",'I',"0")+")";
    			M_strSQLQRD += getUSGDTL("TX_",'I',"0")+")";
			}
			if(L_intCOLA>0)
			{
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			if(L_intCOLD>0)
			{
				cl_dat.exeSQLUPD(M_strSQLQRD ,"setLCLUPD");
			}
	   }
	   catch(Exception L_E)
	   {
	     setMSG(L_E,"insTXDOC");
	   }
	}
	double PROCESS(String P_strCRDTP,String P_strDOCRF,String P_strMODE)
	{
	    double L_dblPNTVL =0;
	    try
	    {
    	    String L_strTRNTP="";
    		String L_strTRNTP1="";
    		String L_strINVNO="",L_strPRDCD="",L_strPKGTP="",L_strSRLNM="",L_strTEMPA="",L_strTEMPD="",L_strWRCON="";
        	String L_strPGRVL ="";
        	double L_dblPGRVL = 0.0;
        	double L_dblPATAX = 0;
        	double L_dblPLTAX = 0;
        	
    	    if(P_strMODE.equals("RESET"))
    	    {
    	        cl_dat.M_flgLCUPD_pbst = true;
    	        String L_strMKTTP = cmbMKTTP.getSelectedItem().toString().substring(0,2);
    	        M_strSQLQRY="UPDATE MR_PTTRN SET PT_DOCRF= '00000000',PT_ATXVL =0,PT_LTXVL =0,PT_PNTVL = PT_PGRVL, ";
    			M_strSQLQRY +="PT_STSFL = '0', ";
    			M_strSQLQRY +="PT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',PT_RPTDT = null, ";
    			M_strSQLQRY +="PT_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
    			M_strSQLQRY += " WHERE PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_CRDTP = '" +P_strCRDTP+ "' AND PT_DOCRF ='"+P_strDOCRF +"' AND isnull(PT_STSFL,'') ='1'";
    	        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	        
    	        M_strSQLQRY  =  "DELETE FROM CO_TXDOC WHERE " ;
    	        M_strSQLQRY += "TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_DOCNO='"+txtDOCRF.getText().trim()+"' And TX_SYSCD='MR' AND TX_SBSTP='"+L_strMKTTP+"' ";
    	        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	        
    	        M_strSQLQRY="DELETE FROM MR_RMMST ";
				M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = '" + strTRNTP + "'";
				M_strSQLQRY += " and RM_DOCNO = '" + txtDOCRF.getText().trim() + "' and  isnull(RM_STSFL,'') <> 'X'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"");
			    
			    // include codes transaction reset also depending on logic
			    
    	        cl_dat.exeDBCMT("RESET");
    	        System.out.println("reset complete");
    	        return 0;
    	    }
    	    for(int i=0;i<tblINVDT.getRowCount();i++)
    		{
    			if(tblINVDT.getValueAt(i,intTB1_CHKFL).toString().equals("true"))
    			{
    				L_strINVNO=tblINVDT.getValueAt(i,intTB1_INVNO).toString();
    				L_strPRDCD=tblINVDT.getValueAt(i,intTB1_PRGRD).toString();
    				L_strPKGTP=tblINVDT.getValueAt(i,intTB1_PKGTP).toString();
    				L_strSRLNM=tblINVDT.getValueAt(i,intTB1_SRLNO).toString();
    				L_strPGRVL=nvlSTRVL(tblINVDT.getValueAt(i,intTB1_PGRVL).toString(),"0");
    				L_dblPGRVL += Double.parseDouble(L_strPGRVL);
    				M_strSQLQRY="UPDATE MR_PTTRN SET PT_DOCRF= '"+txtDOCRF.getText().trim()+"',";
    				M_strSQLQRY +="PT_STSFL = '1', ";
    				M_strSQLQRY +="PT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
    				M_strSQLQRY +="PT_RPTDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', ";
    				M_strSQLQRY +="PT_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
    				///M_strSQLQRY += " WHERE PT_CMPCD = '" +strCMPCD+ "'";
    				M_strSQLQRY +=" WHERE PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_PRTTP='"+txtPRTTP.getText().trim()+"'";
    				M_strSQLQRY += " AND PT_INVNO= '" +L_strINVNO+ "'";
    				M_strSQLQRY += " AND PT_PRDCD = '" +L_strPRDCD+ "'";
    				M_strSQLQRY += " AND PT_PKGTP = '" +L_strPKGTP+ "'";
    				M_strSQLQRY += " AND PT_SRLNO = '" +L_strSRLNM + "'";
    				M_strSQLQRY += " AND PT_PRTCD = '" +txtPRTCD.getText().trim()+ "'";
    				M_strSQLQRY += " AND PT_CRDTP = '" +txtDOCTP.getText().trim()+"'";
    				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    			}
    		}
    		insTXDOC();
    		updDOCNO();
    		if(cl_dat.exeDBCMT("exePRC"))
    		{
              	Hashtable hstTEMP = objREPT.calTAXVL(txtDOCTP.getText().trim(),txtDOCRF.getText().trim());
    	    	double L_dblATXVL = ((Double)hstTEMP.get("ADD")).doubleValue();
    	    	double L_dblLTXVL = ((Double)hstTEMP.get("LESS")).doubleValue();
    	    //	System.out.println("Add "+hstTEMP.get("ADD"));
    	    //	System.out.println("less "+hstTEMP.get("LESS"));
    	    	for(int i=0;i<tblINVDT.getRowCount();i++)
    		    {
    				if(tblINVDT.getValueAt(i,intTB1_CHKFL).toString().equals("true"))
    				{
    				    L_strINVNO=tblINVDT.getValueAt(i,intTB1_INVNO).toString();
    					L_strPRDCD=tblINVDT.getValueAt(i,intTB1_PRGRD).toString();
    					L_strPKGTP=tblINVDT.getValueAt(i,intTB1_PKGTP).toString();
    					L_strSRLNM=tblINVDT.getValueAt(i,intTB1_SRLNO).toString();
    					L_strPGRVL=nvlSTRVL(tblINVDT.getValueAt(i,intTB1_PGRVL).toString(),"0");
    				   	L_dblPATAX = (Double.parseDouble(L_strPGRVL)* L_dblATXVL)/L_dblPGRVL;
    					L_dblPLTAX = (Double.parseDouble(L_strPGRVL)* L_dblLTXVL)/L_dblPGRVL;
    					L_dblPNTVL = Double.parseDouble(L_strPGRVL)+ L_dblPATAX - L_dblPLTAX ;
    				//	System.out.println("Gross :"+L_strPGRVL);
    				//	System.out.println("ADD :"+L_dblPATAX);
    				//	System.out.println("LESS :"+L_dblPLTAX);
    				//	System.out.println("NET :"+L_dblPNTVL);
    					M_strSQLQRY="UPDATE MR_PTTRN SET ";
    					M_strSQLQRY +="PT_ATXVL = "+setNumberFormat(L_dblPATAX,2)+", ";
    					M_strSQLQRY +="PT_LTXVL = "+setNumberFormat(L_dblPLTAX,2)+", ";
    					M_strSQLQRY +="PT_PNTVL = "+setNumberFormat(L_dblPNTVL,2);
    					M_strSQLQRY +=" WHERE PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_PRTTP='"+txtPRTTP.getText().trim()+"'";
    					M_strSQLQRY += " AND PT_INVNO= '" +L_strINVNO+ "'";
    					M_strSQLQRY += " AND PT_PRDCD = '" +L_strPRDCD+ "'";
    					M_strSQLQRY += " AND PT_PKGTP = '" +L_strPKGTP+ "'";
    					M_strSQLQRY += " AND PT_SRLNO = '" +L_strSRLNM + "'";
    					M_strSQLQRY += " AND PT_PRTCD = '" +txtPRTCD.getText().trim()+ "'";
    					M_strSQLQRY += " AND PT_CRDTP = '" +txtDOCTP.getText().trim()+"'";
    				//	System.out.println(M_strSQLQRY);
    					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    				}
    			}
    		}
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"PROCESS");
	    }
	    
		return L_dblPNTVL;
	}
	private class vldINVER extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				//System.out.println("IN INPUT VERIFIER");
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtDOCTP)
				{
					if(txtDOCTP.getText().trim().length()!=2)
						return false;
							
					if(rdbCREDT.isSelected())
					{
						M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT' AND CMT_CHP02 ='Y' And CMT_CODCD ='"+txtDOCTP.getText().trim()+"'";					
					}
					else
					{
						M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT' AND CMT_CHP02 ='Y' AND CMT_CODCD like '3%'";
					}
					//System.out.println(M_strSQLQRY);
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET!=null &&L_rstRSSET.next())
					{
						//System.out.println("IN INPUT VERI");
						//txtDOCTP.setText(L_rstRSSET.getString("CMT_CODCD"));
						L_rstRSSET.close();
						return true;
					}	
					else
					{
						setMSG("Invalid Party Type",'E'); 
						return false;
					}
				}
				if(input==txtPRTCD)
				{
					if(txtPRTCD.getText().trim().length()!=5)
						return false;
					M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTTP ='"+txtPRTTP.getText().trim() +"' AND  PT_PRTCD = '"+txtPRTCD.getText().trim() +"'";//  AND isnull(PT_STSFL,'')<> 'X' "  ;
 					ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET!=null &&L_rstRSSET.next())
					{
						txtPRTNM.setText(L_rstRSSET.getString("PT_PRTNM"));
						L_rstRSSET.close();
							return true;
					}	
					else
					{
						setMSG("Invalid Party Type",'E'); 
						return false;
					}
				}
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"Input Verifier");		
			}
			return true;
		}
	}
	
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{	
			String strTEMP="";
			String strMODE="";
			try
			{
				String L_strTEMP="";
				double L_dblINVVL=0.0;
				double L_dblRCTVL=0.0;
				if(getSource()==tblINVDT)
				{
					if(P_intCOLID>0)
						if(P_intCOLID!=intTB1_CHKFL)
							if(((JTextField)tblINVDT.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
								return true;
					if(P_intCOLID==intTB1_INVNO)			//Validates For Invoice Number in JTable
					{
						L_strTEMP=((JTextField)tblINVDT.cmpEDITR[intTB1_INVNO]).getText();
						M_strSQLQRY= "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL from MR_IVTRN where ";
						M_strSQLQRY += "IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO='"+L_strTEMP+"' " ;//AND IVT_BYRCD='"+txtPRTCD.getText().trim()+"'";
						if(txtPRTTP.getText().equals("D"))
							M_strSQLQRY += " AND IVT_BYRCD IN (SELECT PT_PRTCD FROM CO_PTMST WHERE PT_DSRCD = '"+txtPRTCD.getText().trim()+"')" ;// IVT_MKTTP = '"+strMKTTP1+"' and 
						else
						    M_strSQLQRY += " AND IVT_BYRCD = '"+txtPRTCD.getText().trim()+"'";
				
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET!=null && M_rstRSSET.next())
						{
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PKGTP"),tblINVDT.getSelectedRow(),intTB1_PKGTP);
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),tblINVDT.getSelectedRow(),intTB1_INVQT);
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVVL"),tblINVDT.getSelectedRow(),intTB1_INVVL);
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),tblINVDT.getSelectedRow(),intTB1_INVQT1);
						}
						else
						{
							setMSG("Invalid Invoice Number",'E');
							return false;
						}
						if(M_rstRSSET!=null)
							M_rstRSSET.close();
					}
					if(P_intCOLID==intTB1_PRGRD)			//Validates For Product Code in JTable
					{
						L_strTEMP=((JTextField)tblINVDT.cmpEDITR[intTB1_PRGRD]).getText();
						M_strSQLQRY= "Select IVT_INVNO,IVT_PRDCD,IVT_PKGTP,IVT_INVQT,IVT_INVVL from MR_IVTRN where ";
						M_strSQLQRY += "IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_PRDCD='"+L_strTEMP+"' AND IVT_INVNO = '"+tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVNO)+ "' AND IVT_BYRCD='"+txtPRTCD.getText().trim()+"'";
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET!=null && M_rstRSSET.next())
						{
							//tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PRDCD"),tblINVDT.getSelectedRow(),intTB1_PRGRD);
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_PKGTP"),tblINVDT.getSelectedRow(),intTB1_PKGTP);
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),tblINVDT.getSelectedRow(),intTB1_INVQT);
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVVL"),tblINVDT.getSelectedRow(),intTB1_INVVL);
							tblINVDT.setValueAt(M_rstRSSET.getString("IVT_INVQT"),tblINVDT.getSelectedRow(),intTB1_INVQT1);
						}
						else
						{
							setMSG("Invalid Invoice Number",'E');
							return false;
						}
						if(M_rstRSSET!=null)
							M_rstRSSET.close();
					}
					/*if(P_intCOLID==intTB1_RCTVL)			//Validates For Receipt Amount in JTable
					{
						L_dblINVVL +=Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVVL).toString());
						L_dblRCTVL +=Double.parseDouble(((JTextField)tblINVDT.cmpEDITR[intTB1_RCTVL]).getText().toString());
					
						if(L_dblRCTVL>=L_dblINVVL)
						{
							setMSG("Receipt Amount Can't be more than Invoice Amount",'E');
							return false;
						}
					}	*/
					if(P_intCOLID==intTB1_INVQT)			//Validates For Invoice Qty.
					{
					    //tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_DOCRT).toString()
						if(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT).toString())==0)
						{
				            setMSG("Inv.Qty. cannot be zero",'E');		    
						}
						if(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT).toString())>Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT1).toString()))
						{
				            setMSG("Entered.Qty. cannot be greater than Inv.Qty ("+tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT1).toString()+")",'E');
							return false;
						}
					    tblINVDT.setValueAt(setNumberFormat(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_DOCRT).toString())*Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT).toString()),2),P_intROWID,intTB1_PGRVL);
					    tblINVDT.setValueAt(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PGRVL).toString(),tblINVDT.getSelectedRow(),intTB1_PNTVL);
					}			
					if(P_intCOLID==intTB1_DOCRT)			//Validates For Receipt Amount in JTable
					{
					    //tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_DOCRT).toString()
						if(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_DOCRT).toString())==0)
						{
				            setMSG("Rate cannot be zero",'E');		    
						}
					    tblINVDT.setValueAt(setNumberFormat(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_DOCRT).toString())*Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT).toString()),2),P_intROWID,intTB1_PGRVL);
					    tblINVDT.setValueAt(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PGRVL).toString(),tblINVDT.getSelectedRow(),intTB1_PNTVL);
					}			
					if(P_intCOLID==intTB1_PGRVL)			//Validates For Receipt Amount in JTable
					{
					    //tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_DOCRT).toString()
						if(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PGRVL).toString())==0)
						{
				            setMSG("Value cannot be zero",'E');		    
						}
					    tblINVDT.setValueAt(setNumberFormat(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PGRVL).toString())/Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT).toString()),2),P_intROWID,intTB1_DOCRT);
					    tblINVDT.setValueAt(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PGRVL).toString(),tblINVDT.getSelectedRow(),intTB1_PNTVL);
					}			
					if(P_intCOLID==intTB1_PNTVL)			//Validates For Receipt Amount in JTable
					{
					    //tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_DOCRT).toString()
						if(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PNTVL).toString())==0)
						{
				            setMSG("Value cannot be zero",'E');		    
						}
					    //tblINVDT.setValueAt(setNumberFormat(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PNTVL).toString())+Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_LTXVL).toString())-Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_ATXVL).toString()),2),P_intROWID,intTB1_PGRVL);
					    //tblINVDT.setValueAt(setNumberFormat(Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_PGRVL).toString())/Double.parseDouble(tblINVDT.getValueAt(tblINVDT.getSelectedRow(),intTB1_INVQT).toString()),2),P_intROWID,intTB1_DOCRT);
					}			
				}
				if(getSource()==tblCOMTX)
				{
					if(P_intCOLID==intTB2_TAXCD)    //Validation On Tax Code
					{
						strTEMP = tblCOMTX.getValueAt(P_intROWID,intTB2_TAXCD).toString();
						strMODE = tblCOMTX.getValueAt(P_intROWID,intTB2_TXMOD).toString();
						if(strTEMP.length()>0)
						{
							for(int i=0;i<=P_intROWID-1;i++)
							{
								if(tblCOMTX.getValueAt(i,intTB2_TAXCD).toString().trim().length() >0)
								if((tblCOMTX.getValueAt(i,intTB2_TAXCD).toString().trim().equals(strTEMP.trim()))&&(tblCOMTX.getValueAt(i,intTB2_TXMOD).toString().trim().equals(strMODE.trim())))
								{
									setMSG("Duplicate entry ..",'E');
									return false;
								}
							}
							//M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CHP02,CMT_NCSVL,CMT_CHP01,CMT_NMP01  from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'COXXTAX' AND CMT_CODCD='"+strTEMP+"'";
							M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CHP02,CMT_NCSVL,CMT_CHP01,CMT_NMP01  from CO_CDTRN where CMT_CGMTP='TCL' and CMT_CGSTP = 'MRXXDCM' AND SUBSTRING(CMT_CODCD,1,3)='"+strTEMP+"'";
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET!=null && M_rstRSSET.next())
							{
								tblCOMTX.setValueAt(M_rstRSSET.getString("CMT_CODDS"),tblCOMTX.getSelectedRow(),intTB2_TAXDS);
								tblCOMTX.setValueAt(M_rstRSSET.getString("CMT_CHP02"),tblCOMTX.getSelectedRow(),intTB2_TAXFL);
								tblCOMTX.setValueAt(M_rstRSSET.getString("CMT_NCSVL"),tblCOMTX.getSelectedRow(),intTB2_TAXVL);
								tblCOMTX.setValueAt(M_rstRSSET.getString("CMT_CHP01"),tblCOMTX.getSelectedRow(),intTB2_TXMOD);
								//tblCOMTX.setValueAt(M_rstRSSET.getString("CMT_CHP01"),tblCOMTX.getSelectedRow(),intTB1_INVVL);
								
							}
							else
							{
								setMSG("Invalid Tax Code",'E');
								return false;
							}
							if(M_rstRSSET!=null)
								M_rstRSSET.close();
						}
					}
				}
			}
			catch(Exception E_TL)
			{
				setMSG(E_TL,"Table Input Verifier");				
			}
			return true;
		}
	}
	
}
/* price difference
SELECT IVT_INVNO,date(IVT_INVDT) IVT_INVDT,IVT_PRDDS,CMT_CODDS DSPTP,IVT_INVQT,IVT_INDNO,IVT_INVRT,IVT_CC1VL/IVT_INVQT IVT_CC1VL, IVT_CC2VL/IVT_INVQT IVT_CC2VL,IVT_CC3VL/IVT_INVQT IVT_CC3VL FROM SPLDATA.MR_IVTRN,SPLDATA.CO_CDTRN WHERE date(IVT_INVDT) BETWEEN  '03/01/2006' AND '03/31/2006' AND IVT_BYRCD='P0022' AND CMT_CGMTP='SYS' AND CMT_CGSTP='MRXXDTP' AND CMT_CODCD=IVT_DTPCD ORDER BY IVT_PRDDS,IVT_INVNO;
*/