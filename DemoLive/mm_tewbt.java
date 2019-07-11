// Entry form for WeighBridge Transaction

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.lang.Number;
import java.util.Date;
import java.util.Enumeration;
import java.awt.Font;
import java.io.*;
import java.util.*;
import javax.comm.*;
import java.util.Hashtable;
import java.math.BigDecimal;

class mm_tewbt extends cl_pbase implements SerialPortEventListener, MouseListener, ActionListener {
	
	private JTextField txtLRYWT,txtCHLNO,txtCHLDT,txtCHLQT,txtORDRF,txtGINNO,txtWEBNO,txtTPRCD;
	private JTextField txtTPRDS,txtDRVCD,txtDRVNM,txtMATDS,txtLRYNO,txtMATCD,txtGINDT,txtBOENO;
	private JTextField txtTNKNO,txtLOCCD,txtLOCNM,txtPRTCD,txtPRTDS,txtREMWB,txtUOMCD,txtGVTVL;
	private JLabel lblDOCRF,lblPRTNM,lblGRSWT,lblTARWT,lblNETWT,lblGRSDT,lblTARDT,lblGWTBY,lblTWTBY;
	private JButton btnACPT, btnSTRT;	
	private JRadioButton btnWB1,btnWB2;
	private JComboBox cmbGINTP,cmbMATTP,cmbWBRTR;
	private JOptionPane optOPTPN;
	private JButton btnPRINT,btnPRINT1;
	private ButtonGroup bgrWBRTR = new ButtonGroup();
	private ButtonGroup bgrEPCHK = new ButtonGroup();
	private ButtonGroup bgrBSCHK = new ButtonGroup();
	private ButtonGroup bgrLOCHK = new ButtonGroup();
	private ButtonGroup bgrERCHK = new ButtonGroup();
	private JRadioButton rdoERYES,rdoERNO;
	
	private JRadioButton rdoLOYES,rdoLONO;

	private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
	
	private JRadioButton rdoBSYES,rdoBSNO;
	private JRadioButton rdoEPYES,rdoEPNO;
	private JPanel pnlWEIGHT = new JPanel();	
	private JPanel pnlWBDTL = new JPanel();	
	private JPanel pnlTFDTL = new JPanel();	
	private JTabbedPane jtpWBDTL;
	private char chrWBOPT = 'I';
	private JTextField txtTFIDT,txtTFITM,txtTFODT,txtTFOTM,txtULSDT,txtULSTM,txtULEDT,txtULETM,txtULDBY;
	ResultSet  M_rstRSSET1;
	
        double LM_CNVFCT = 1000; //9 zero converting to tons.
	private String strWBRNO,
		   strGINTP,
		   strGINNO,
		   strLRYNO,
		   strTPRCD,
		   strTPRDS,
		   strDRVCD,
		   strDRVNM,
		   strMATCD,
	       strCHLNO,
		   strCHLDT,
		   strCHLQT,
		   strGRSWT,
		   strTARWT,
		   strNETWT,
		   strINCTM,
		   strOUTTM,
		   strMATDS,
		   strORDRF,
		   strPRTCD,
		   strPRTDS,
		   strLUSBY,
		   strLUPDT,
		   strSTSFL,
		   strDEFDS,
		   strGINDT,
		   strLOCCD,
		   strLOCDS,
		   strBOENO,
		   strTNKNO,
		   strMATTP,
		   strUOMCD,
		   strGVTVL,
		   strUOMQT,
		   strWINBY,
		   strWOTBY,
		   strPGINTP,
		   strPGINNO,
		   strPSRLNO,
		   strODFFL,
		   strSRLNO,
		   strREMWB;
	
	Date LD_CHLDT;		
	
	Font F_WT = new Font("Times New Roman",Font.PLAIN,34);
	
	// Variables Related to Communication Port
	String strPOTID;
	CommPortIdentifier portId;    
    Enumeration portList;
    InputStream inputStream;   
	SerialPort serialPort;
    String strCOMSTR="",strCOMSTR1= "";//change the String modified 12-04-2007
	
	//FileOutputStream O_FOUT;
    //DataOutputStream O_DOUT; 
	
	int LM_CONFIRM = 0;
	final String strRAWMT = "01";
	final String strRECPT = "02";
	final String strDESPT = "03";
	final String strSLSRT = "04";
	final String strOTHER = "09";
	
	final String strSTSIN = "2";
	final String strSTSOT = "4";
	
	final String strSTYCD = "6805010045";				// Material Code for Styrene
	final String strACPTG = "W";						// Tag for Waiting Status
	final String strTRNFL = "0";						// Transfer flag
	
	final String M_CMBWIN = "WeighBridge-In";
	final String M_CMBWOT = "WeighBridge-Out";
	
	boolean LM_PRESENT = false;
	Hashtable hstPORT = new Hashtable();
	
	   mm_tewbt()
	   {
		super(2);
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		optOPTPN = new JOptionPane();	
		crtSCR();		// Showing form on screen
		setENBL(false);
		setMSG("Select an Option..",'N');
		cl_dat.M_flgHELPFL_pbst = false;
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}	

	void crtSCR()
	{
		try
		{
			setMatrix(15,7);
			setVGAP(15);
			pnlWBDTL.setLayout(null);
			pnlTFDTL.setLayout(null);
	        jtpWBDTL=new JTabbedPane();
			jtpWBDTL.addMouseListener(this);
			setMatrix(15,7);
			jtpWBDTL.add(pnlWBDTL,"Weighment Details");
			jtpWBDTL.add(pnlTFDTL,"T/F Details ");
			
			add(new JLabel("Entry Type"),1,1,1,1,pnlWBDTL,'L');
			add(cmbWBRTR =new JComboBox(),1,2,1,1.5,pnlWBDTL,'L');
			cmbWBRTR.removeActionListener(this);
			cmbWBRTR.addItem(M_CMBWIN);
			cmbWBRTR.addItem(M_CMBWOT);
			cmbWBRTR.addActionListener(this);		
			add(new JLabel("Gate-In Type"),2,1,1,1,pnlWBDTL,'L');
			add(cmbGINTP =new JComboBox(),2,2,1,1,pnlWBDTL,'L');
			getCMBDT("GINTP");
			add(new JLabel("Gate-In No."),2,3,1,1,pnlWBDTL,'L');
			add(txtGINNO = new TxtLimit(10),2,4,1,1,pnlWBDTL,'L');
			add(lblDOCRF = new JLabel("Loading advice"),2,5,1,1,pnlWBDTL,'L');
			add(txtORDRF = new TxtLimit(20),2,6,1,1,pnlWBDTL,'L');
			add(btnPRINT = new JButton("Print"),2,7,1,0.9,pnlWBDTL,'L');
		    add(btnPRINT1 = new JButton("Print Unloading Note"),1,7,1,2,pnlWBDTL,'R');
			add(new JLabel("Lorry No."),3,1,1,1,pnlWBDTL,'L');
			add(txtLRYNO = new TxtLimit(20),3,2,1,1,pnlWBDTL,'L');
			add(new JLabel("Transporter"),3,3,1,1,pnlWBDTL,'L');
			add(txtTPRCD = new TxtLimit(5),3,4,1,1,pnlWBDTL,'L');
			add(txtTPRDS = new TxtLimit(100),3,5,1,3,pnlWBDTL,'L');
			txtTPRDS.setEnabled(false);
		
			add(new JLabel("Driver"),4,1,1,1,pnlWBDTL,'L');
			add(txtDRVCD = new TxtLimit(5),4,2,1,1,pnlWBDTL,'L');
			add(new JLabel("Driver Name"),4,3,1,1,pnlWBDTL,'L');
			add(txtDRVNM = new TxtLimit(30),4,4,1,2,pnlWBDTL,'L');
			txtDRVNM.setEnabled(false);
			add(new JLabel("Gate-In Time"),4,6,1,1,pnlWBDTL,'L');
			add(txtGINDT = new TxtDate(),4,7,1,1.3,pnlWBDTL,'R');
			txtGINDT.setEnabled(false);
		
			add(new JLabel("Material"),5,1,1,1,pnlWBDTL,'L');
			add(txtMATCD = new TxtLimit(10),5,2,1,1,pnlWBDTL,'L');
			add(txtMATDS = new TxtLimit(45),5,3,1,2,pnlWBDTL,'L');
			add(txtUOMCD = new TxtLimit(20),5,5,1,1,pnlWBDTL,'R');
			txtUOMCD.setEnabled(false);
			add(new JLabel("Sp. Gravity"),5,6,1,1.3,pnlWBDTL,'L');
			add(txtGVTVL = new TxtNumLimit(7.3),5,7,1,0.9,pnlWBDTL,'L');
		
			add(new JLabel("B/E No."),6,1,1,1,pnlWBDTL,'L');
			add(txtBOENO = new TxtLimit(20),6,2,1,1,pnlWBDTL,'L');
			add(new JLabel("Tank No."),6,3,1,1,pnlWBDTL,'L');
			add(txtTNKNO = new TxtLimit(15),6,4,1,1,pnlWBDTL,'L');
			add(new JLabel("Mat. Type"),6,5,1,1,pnlWBDTL,'L');
			add(cmbMATTP = new JComboBox(),6,6,1,1,pnlWBDTL,'L');
			getCMBDT("MATTP");
		
			add(new JLabel("Chalan No."),7,1,1,1,pnlWBDTL,'L');
			add(txtCHLNO = new TxtLimit(20),7,2,1,1,pnlWBDTL,'L');
			add(new JLabel("Chalan Date"),7,3,1,1,pnlWBDTL,'L');
			add(txtCHLDT = new TxtDate(),7,4,1,1,pnlWBDTL,'L');
			add(new JLabel("Chalan Qty"),7,5,1,1,pnlWBDTL,'L');
			add(txtCHLQT = new TxtNumLimit(7.3),7,6,1,1,pnlWBDTL,'L');
			btnWB1 = new JRadioButton("W.Bridge 1",true); 
			bgrWBRTR.add(btnWB1);
			add(btnWB1,8,1,1,1,pnlWBDTL,'L');
			btnWB2 = new JRadioButton("W.Bridge 2",false); 
			bgrWBRTR.add(btnWB2);
			add(btnWB2,8,2,1,1,pnlWBDTL,'L');
	
			add(txtLRYWT = new TxtNumLimit(7.3),9,1,1.8,2,pnlWBDTL,'L');
			txtLRYWT.setFont(F_WT);
			txtLRYWT.setEnabled(false);
                        txtLRYWT.setDisabledTextColor(Color.blue);
			add(new JLabel("Gross Weight"),8,3,1,1,pnlWBDTL,'L');
			add(lblGRSWT = new JLabel(" "),8,4,1,1,pnlWBDTL,'L');
			lblGRSWT.setHorizontalAlignment(RIGHT);
			lblGRSWT.setForeground(new Color(176,28,54));			
		
			add(new JLabel("Time"),8,5,1,1,pnlWBDTL,'L');
                        add(lblGRSDT = new JLabel(" "),8,6,1,1.1,pnlWBDTL,'L');
			//lblGRSDT.setHorizontalAlignment(CENTER);
			lblGRSDT.setForeground(new Color(176,28,54));			
                     //   add(new JLabel("By"),8,7,1,0.3,pnlWBDTL,'C');
                        add(lblGWTBY = new JLabel(" "),8,7,1,0.9,pnlWBDTL,'R');
			lblGWTBY.setHorizontalAlignment(RIGHT);
			lblGWTBY.setForeground(new Color(176,28,54));			
		
			add(new JLabel("Tare Weight"),9,3,1,1,pnlWBDTL,'L');
			add(lblTARWT = new JLabel(" "),9,4,1,1,pnlWBDTL,'L');
			lblTARWT.setHorizontalAlignment(RIGHT);
			lblTARWT.setForeground(new Color(176,28,54));	
		
            add(new JLabel("Time"),9,5,1,0.9,pnlWBDTL,'L');
            add(lblTARDT = new JLabel(" "),9,6,1,1.1,pnlWBDTL,'L');
            lblTARDT.setHorizontalAlignment(LEFT);
			lblTARDT.setForeground(new Color(176,28,54));	
		
            //   add(new JLabel("By"),9,7,1,0.3,pnlWBDTL,'C');
            add(lblTWTBY = new JLabel(" "),9,7,1,0.7,pnlWBDTL,'R');
			lblTWTBY.setHorizontalAlignment(RIGHT);
			lblTWTBY.setForeground(new Color(176,28,54));			

			add(btnSTRT = new JButton("Start"),11,1,1,1,pnlWBDTL,'L');
			add(btnACPT = new JButton("Accept"),11,2,1,1,pnlWBDTL,'L');
		
			add(new JLabel("Net Weight"),10,3,1,1,pnlWBDTL,'L');
			add(lblNETWT = new JLabel(" "),10,4,1,1,pnlWBDTL,'L');
			lblNETWT.setHorizontalAlignment(RIGHT);
			lblNETWT.setForeground(new Color(176,28,54));	
		
			add(new JLabel("Location"),10,5,1,1,pnlWBDTL,'L');
			add(txtLOCCD = new TxtLimit(3),10,6,1,1,pnlWBDTL,'L');
			add(txtLOCNM = new TxtLimit(20),10,7,1,0.9,pnlWBDTL,'L');
			txtLOCNM.setEnabled(false);
			add(new JLabel("Supplier"),11,3,1,1,pnlWBDTL,'L');
			add(txtPRTCD = new TxtLimit(5),11,4,1,1,pnlWBDTL,'L');
			add(txtPRTDS = new TxtLimit(50),11,5,1,2.9,pnlWBDTL,'L');
			txtPRTDS.setEnabled(false);
			add(new JLabel("Remarks"),12,1,1,1,pnlWBDTL,'L');
			add(txtREMWB = new TxtLimit(100),12,2,1,5,pnlWBDTL,'L');
			
			add(new JLabel("1. Tanker In Date/ Time "),2,2,1,3,pnlTFDTL,'L');
			add(txtTFIDT = new TxtDate(),2,5,1,1,pnlTFDTL,'L');
			add(txtTFITM = new TxtTime(),2,6,1,1,pnlTFDTL,'L');

			add(new JLabel("2. Bottom Sample Checked for Smell,Colour and Water "),3,2,1,4,pnlTFDTL,'L');
	    	add(rdoBSYES = new JRadioButton("Yes"),3,5,1,1,pnlTFDTL,'L');
			add(rdoBSNO = new JRadioButton("No"),3,6,1,1,pnlTFDTL,'L');
            bgrBSCHK.add(rdoBSYES);
	       	bgrBSCHK.add(rdoBSNO);
	        rdoBSYES.setSelected(true);
	        add(new JLabel("3. Line Out Checked To Tank "),4,2,1,4,pnlTFDTL,'L');
	       	add(rdoLOYES = new JRadioButton("Yes"),4,5,1,1,pnlTFDTL,'L');
			add(rdoLONO = new JRadioButton("No"),4,6,1,1,pnlTFDTL,'L');
            bgrLOCHK.add(rdoLOYES);
	       	bgrLOCHK.add(rdoLONO);
            rdoLOYES.setSelected(true);
	      
	        add(new JLabel("4. Earthing Provided to Tanker "),5,2,1,4,pnlTFDTL,'L');
	    	add(rdoERYES = new JRadioButton("Yes"),5,5,1,1,pnlTFDTL,'L');
			add(rdoERNO = new JRadioButton("No"),5,6,1,1,pnlTFDTL,'L');
            bgrERCHK.add(rdoERYES);
	       	bgrERCHK.add(rdoERNO);
	       	rdoERYES.setSelected(true);
			add(new JLabel("5. Unloading Started at(Date/ Time) "),6,2,1,4,pnlTFDTL,'L');
			add(txtULSDT = new TxtDate(),6,5,1,1,pnlTFDTL,'L');
			add(txtULSTM = new TxtTime(),6,6,1,1,pnlTFDTL,'L');

			add(new JLabel("6. Unloading Ended at(Date/ Time)"),7,2,1,4,pnlTFDTL,'L');
			add(txtULEDT = new TxtDate(),7,5,1,1,pnlTFDTL,'L');
			add(txtULETM = new TxtTime(),7,6,1,1,pnlTFDTL,'L');

            add(new JLabel("7. Tanker Checked From Top and is Empty "),8,2,1,4,pnlTFDTL,'L');
           	add(rdoEPYES = new JRadioButton("Yes"),8,5,1,1,pnlTFDTL,'L');
			add(rdoEPNO = new JRadioButton("No"),8,6,1,1,pnlTFDTL,'L');
            rdoEPYES.setSelected(true);
            bgrEPCHK.add(rdoEPYES);
	       	bgrEPCHK.add(rdoEPNO);
	      
            add(new JLabel("8. Tanker Out Date/ Time "),9,2,1,4,pnlTFDTL,'L');
			add(txtTFODT = new TxtDate(),9,5,1,1,pnlTFDTL,'L');
			add(txtTFOTM = new TxtTime(),9,6,1,1,pnlTFDTL,'L');
            add(new JLabel("9. Tanker Unloaded By "),10,2,1,4,pnlTFDTL,'L');
			add(txtULDBY = new TxtLimit(3),10,5,1,1,pnlTFDTL,'L');
			add(jtpWBDTL,1,1,15,7,this,'L');
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			//	lblGRSWT.requestFocus();
		}catch(Exception E){
			System.out.println(E.toString());
		}					
	}
	
	void setENBL(boolean P_strFLAG){
		txtGINNO.setEnabled(!P_strFLAG);
		cmbGINTP.setEnabled(!P_strFLAG);
		btnWB1.setEnabled(P_strFLAG);
		btnWB2.setEnabled(P_strFLAG);
		txtLRYNO.setEnabled(P_strFLAG);
		txtCHLNO.setEnabled(P_strFLAG);
		txtCHLDT.setEnabled(P_strFLAG);
		txtCHLQT.setEnabled(P_strFLAG);
		txtORDRF.setEnabled(P_strFLAG);
		txtTPRCD.setEnabled(P_strFLAG);
		txtPRTCD.setEnabled(P_strFLAG);
		txtDRVCD.setEnabled(P_strFLAG);
		txtMATCD.setEnabled(P_strFLAG);
		txtMATDS.setEnabled(P_strFLAG);
		strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
		if(strGINTP.equals(strRAWMT)){		// Raw Material
			cmbMATTP.setEnabled(P_strFLAG);
			txtGVTVL.setEnabled(P_strFLAG);
		}
		else{
			cmbMATTP.setEnabled(false);
			txtGVTVL.setEnabled(false);
		}
		txtLOCCD.setEnabled(P_strFLAG);
		txtREMWB.setEnabled(P_strFLAG);
		txtBOENO.setEnabled(false);
		txtTNKNO.setEnabled(P_strFLAG);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			cmbWBRTR.setEnabled(true);
		else
		{
			cmbWBRTR.setEnabled(false);
			txtGINNO.requestFocus();
		}
	}
	public void mouseClicked(MouseEvent L_ME)
	{
		if(L_ME.getSource().equals(jtpWBDTL))
		{
			
			if(jtpWBDTL.getSelectedIndex() == 1)
			{
			    txtTFIDT.requestFocus();
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			
			String L_ACTCMD = L_AE.getActionCommand();
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
				{
					txtGINNO.setEnabled(true);
					cmbGINTP.setEnabled(true);
				}
                if(serialPort !=null)
	                 serialPort.close();
			}
            if(M_objSOURC == cl_dat.M_btnEXIT_pbst)
			{
                if(serialPort !=null)
                   serialPort.close();
            }
			if(M_objSOURC == txtGINNO)
			{				
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				strGINNO = txtGINNO.getText().trim();
				if(vldGINNO(strGINTP,strGINNO))
				{
					if(strGINTP.equals(strOTHER))
						strSRLNO = getSRLNO(strGINTP,strGINNO);
					else
						strSRLNO = "1";
					exeGETREC(strGINTP,strGINNO,strSRLNO);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						setENBL(true);
						cmbGINTP.setEnabled(false);
						btnWB1.setEnabled(true);
						btnWB2.setEnabled(true);
						btnSTRT.setEnabled(true);
						btnACPT.setEnabled(false);
						btnSTRT.requestFocus();
					}
					setMSG("",'N');
				}
				else
				{
						txtGINNO.requestFocus();
						setMSG("Invalid Gate-In No. Press F1 for help.",'E');
				}
			}
			if (M_objSOURC == cmbWBRTR)
			{
				if(((String)cmbWBRTR.getSelectedItem()).equals(M_CMBWIN))
				{
					chrWBOPT ='I';
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					cl_dat.M_btnUNDO_pbst.setEnabled(true);
					cl_dat.M_btnEXIT_pbst.setEnabled(false);
				}
				else if(((String)cmbWBRTR.getSelectedItem()).equals(M_CMBWOT))
				{
					chrWBOPT ='O';
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					cl_dat.M_btnUNDO_pbst.setEnabled(true);
					cl_dat.M_btnEXIT_pbst.setEnabled(false);
				}
				initSCR();
			}
			else if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
			{
              exeHLPOK();
			}
			else if(M_objSOURC == cmbGINTP)
			{
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				if(strGINTP.equals(strRAWMT)){								// Raw Material
					txtMATCD.setText(strSTYCD);
					vldMATTF(strSTYCD);
					txtGVTVL.setEnabled(true);
					lblPRTNM.setText("Supplier");
				}
				else if(strGINTP.equals(strRECPT)){							// Receipt
					txtMATCD.setText("");
					txtMATDS.setText("");
					txtUOMCD.setText("");
					txtGVTVL.setEnabled(false);
					lblPRTNM.setText("Supplier");
				}
				else if(strGINTP.equals(strSLSRT)){							// Sales Return
					txtMATCD.setText("");
					txtMATDS.setText("");
					txtUOMCD.setText("");
					txtGVTVL.setEnabled(false);
					lblPRTNM.setText("Supplier");
				}
				else if(strGINTP.equals(strDESPT)){							// Despatch
					txtMATCD.setText("");
					txtMATDS.setText("POLYSTYRENE");					
					txtUOMCD.setText("");
					txtGVTVL.setEnabled(false);
					lblPRTNM.setText("Buyer");
				}
				else if(strGINTP.equals(strOTHER)){							// Other
					txtMATCD.setText("");
					txtMATDS.setText("Scrap");					
					txtUOMCD.setText("");
					txtGVTVL.setEnabled(false);
					lblPRTNM.setText("Buyer");
				}
				
				txtGINNO.setText("");
				txtORDRF.setText("");
				txtTPRCD.setText("");
				txtTPRDS.setText("");
				txtLRYNO.setText("");
				txtDRVCD.setText("");
				txtDRVNM.setText("");
				txtPRTCD.setText("");
				txtPRTDS.setText("");
				lblGRSWT.setText("");
				lblGRSDT.setText("");
				lblGWTBY.setText("");
				lblTARWT.setText("");
				lblTARDT.setText("");
				lblTWTBY.setText("");
				txtGVTVL.setText("");
				txtGINNO.requestFocus();
			}
			else if(L_ACTCMD.equals("cl_dat.M_btnUNDO_pbst"))
			{
				clrSCR();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))	
					txtGINNO.requestFocus();
				// Weighbridge In 
				else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(chrWBOPT =='O')){
					setENBL(false);
					btnSTRT.setEnabled(false);
					btnACPT.setEnabled(false);
					txtGINNO.requestFocus();
				}
				else if(chrWBOPT == 'I')
				{
					setENBL(true);
					cmbGINTP.setEnabled(true);			
					txtGINNO.setEnabled(true);						
					btnSTRT.setEnabled(true);
					btnACPT.setEnabled(false);
					txtGINNO.requestFocus();					

					strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
					if(strGINTP.equals(strRAWMT)){
						txtMATCD.setText(strSTYCD);
						vldMATTF(strSTYCD);
					}
					else if(strGINTP.equals(strDESPT))
						txtMATDS.setText("POLYSTYRENE");
					else if(strGINTP.equals(strOTHER))
						txtMATDS.setText("Scrap");	
					else if(strGINTP.equals(strSLSRT))
						txtMATDS.setText("");
					else
						txtMATDS.setText("");	
				}
				
			}
			else if (L_AE.getSource().equals(btnSTRT))
			{
				try
				{
					if(btnWB1.isSelected())
                                                strPOTID = "COM4";
					else
                                                strPOTID = "COM3";
                    fosREPORT = new FileOutputStream(cl_dat.M_strREPSTR_pbst +"mm_tewbt.txt");
                    dosREPORT = new DataOutputStream(fosREPORT);
                                                
                    txtLRYWT.setText("");
                    setCursor(cl_dat.M_curWTSTS_pbst);                                                        txtLRYWT.setText("");                                                   txtLRYWT.setText("");                                                   txtLRYWT.setText("");
                    if(commPORT()){
						btnSTRT.setEnabled(false);
						btnACPT.setEnabled(true);
						btnACPT.requestFocus();
						setMSG("",'N');
					}
					else{
						setMSG("Error while connecting to port. Please, close the other programs.",'E');
						btnSTRT.requestFocus();
					}
				}catch(Exception e){
                                        setCursor(cl_dat.M_curDFSTS_pbst);                                   
					System.out.println("Error in btnSTRT pressed : " + e.toString());
				}
                                setCursor(cl_dat.M_curDFSTS_pbst);
		 	}
			else if (L_AE.getSource().equals(btnACPT))
			{
        		if(dosREPORT !=null)
        			dosREPORT.close();
        		if(fosREPORT !=null)
        			fosREPORT.close();
				if(serialPort != null)
					serialPort.close();
				
				double L_NETWT;
				
				// Receipt/Raw Material	- Weigh In	==  Gross Weight
				// Receipt/Raw Material	- Weigh Out	==  Tare Weight				// Pending
				// Despatch				- Weigh In	==  Tare Weight
				// Despatch				- Weigh Out	==  Gross Weight			// Pending
				
				//txtLRYWT.setText(setFMT("RND",String.valueOf(Double.parseDouble("75000000")/LM_CNVFCT),3));
				
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			//	System.out.println(" before strGINDTP " +strGINTP);
			//	System.out.println("before strRAWMT" +strRAWMT);
			//	System.out.println(" before strRECPT" +strRECPT);
				
				try{
					if(strGINTP.equals(strRAWMT) || strGINTP.equals(strRECPT)|| strGINTP.equals(strSLSRT)){		// Rawa Material/Receipt
					    //System.out.println(" after strGINDTP " +strGINTP);
						//System.out.println("after strRAWMT" +strRAWMT);
						//System.out.println(" after strRECPT" +strRECPT);
						
						
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							if(strSTSFL.equals(strSTSIN)){			// WeighBridge-In has been done
							    
								lblGRSWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
								//System.out.println(" WeighBridge-In has been done" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3)); 
								lblGRSDT.setText(cl_dat.getCURDTM());
								lblGWTBY.setText(cl_dat.M_strUSRCD_pbst.trim());
								txtLOCCD.requestFocus();
							}
							else if(strSTSFL.equals(strSTSOT)){		// WeighBridge-Out   has been done
								lblTARWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
						//		System.out.println(" WeighBridge-Out   has been done" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
								lblTARDT.setText(cl_dat.getCURDTM());
								System.out.println(" lblGRSWT.getText().trim() "+lblGRSWT.getText().trim());
								System.out.println(" lblTARWT.getText().trim() "+lblTARWT.getText().trim());
								L_NETWT = Double.parseDouble(lblGRSWT.getText().trim()) - Double.parseDouble(lblTARWT.getText().trim());
								System.out.println(" L_NETWT "+L_NETWT);
						//		System.out.println("before Varible L_neTWT" +L_NETWT);
								lblNETWT.setText(setNumberFormat(L_NETWT,3));
						//		System.out.println("AfterVarible " +L_NETWT);
								lblTWTBY.setText(cl_dat.M_strUSRCD_pbst.trim());
								cl_dat.M_btnSAVE_pbst.requestFocus();
							}
						}
						else if(chrWBOPT == 'I')
						{			
							strSTSFL = strSTSIN;
							if(txtLRYWT.getText().trim().length() >0)
							{
								lblGRSWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
						//		System.out.println("ChrWBOPT ='I" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
							}
							else
								lblGRSWT.setText("0.0");
							lblGRSDT.setText(cl_dat.getCURDTM());
							lblGWTBY.setText(cl_dat.M_strUSRCD_pbst.trim());
							txtLOCCD.requestFocus();
						}
						else if(chrWBOPT == 'O'){		// WeighBridge-Out
							strSTSFL = strSTSOT;
							lblTARWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
						//	System.out.println("chrWBOT 'O" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
							lblTARDT.setText(cl_dat.getCURDTM());
								lblTARDT.setText(cl_dat.getCURDTM());
							//System.out.println(" [1] lblGRSWT.getText().trim() "+lblGRSWT.getText().trim());
							//System.out.println("[1]  lblTARWT.getText().trim() "+lblTARWT.getText().trim());
							L_NETWT = Double.parseDouble(lblGRSWT.getText().trim()) - Double.parseDouble(lblTARWT.getText().trim());
							//System.out.println("[1] L_NETWT "+L_NETWT);
						//	System.out.println(" before wighBridge out O selected" +L_NETWT);
							lblNETWT.setText(setNumberFormat(L_NETWT,3));
						//	System.out.println("After Weighbride out o Selected" +setNumberFormat(L_NETWT,3));
							lblTWTBY.setText(cl_dat.M_strUSRCD_pbst.trim());
							cl_dat.M_btnSAVE_pbst.requestFocus();
						}
					}
					else if(strGINTP.equals(strDESPT) || strGINTP.equals(strOTHER))// **************EKKADA COMMENT UNDIDespatch/Other
					{ 
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)){
							if(strSTSFL.equals(strSTSIN)){			// WeighBridge-In has been done
								lblTARWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
					//			System.out.println("WeighBridge-IN has been done" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
								lblTARDT.setText(cl_dat.getCURDTM());
								txtLOCCD.requestFocus();
							}
							else if(strSTSFL.equals(strSTSOT)){		// WeighBridge-Out has been done
								lblGRSWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
					//			System.out.println("WeighBridge-Out has been done" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
								lblGRSDT.setText(cl_dat.getCURDTM());
								//System.out.println(" [2] lblGRSWT.getText().trim() "+lblGRSWT.getText().trim());
								//System.out.println("[2]  lblTARWT.getText().trim() "+lblTARWT.getText().trim());
								L_NETWT = Double.parseDouble(lblGRSWT.getText().trim()) - Double.parseDouble(lblTARWT.getText().trim());
								//System.out.println("[2] L_NETWT "+L_NETWT);
								lblNETWT.setText(setNumberFormat(L_NETWT,3));
					//			System.out.println("LblnetWT" +setNumberFormat(L_NETWT,3));
								cl_dat.M_btnSAVE_pbst.requestFocus();
							}
						}
						else if(chrWBOPT == 'I'){			// WeighBridge-In
							strSTSFL = strSTSIN;
					//		System.out.println("strSTSIN WeighBride IN" +strSTSIN);
					//		System.out.println("strSTSIN WeighBride IN" +strSTSFL);
							lblTARWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
					//		System.out.println("WEBRIDEG in chrWBOPT I Selected" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
							lblTARDT.setText(cl_dat.getCURDTM());
							lblTWTBY.setText(cl_dat.M_strUSRCD_pbst.trim());
							txtLOCCD.requestFocus();
						}
						else if(chrWBOPT == 'O')
						{		// WeighBridge-Out
							strSTSFL = strSTSOT;
					//		System.out.println("strSTSIN WeighBride Out " +strSTSIN);
					//		System.out.println("strSTSIN WeighBride Out" +strSTSFL);
							lblGRSWT.setText(setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
					//		System.out.println("TWeighBridge Out  O selected" +setNumberFormat(Double.parseDouble(txtLRYWT.getText()),3));
							lblGRSDT.setText(cl_dat.getCURDTM());
							//System.out.println(" [3] lblGRSWT.getText().trim() "+lblGRSWT.getText().trim());
							//System.out.println("[3]  lblTARWT.getText().trim() "+lblTARWT.getText().trim());
							L_NETWT = Double.parseDouble(lblGRSWT.getText().trim()) - Double.parseDouble(lblTARWT.getText().trim());
							//System.out.println("[3] L_NETWT "+L_NETWT);
					//		System.out.println(L_NETWT);
							
							lblNETWT.setText(setNumberFormat(L_NETWT,3));
							lblGWTBY.setText(cl_dat.M_strUSRCD_pbst.trim());
							cl_dat.M_btnSAVE_pbst.requestFocus();
						}
					
					}
				}catch(NumberFormatException e){
					System.out.println("NumberFormatException in btnSTRT Pressed");
				}
				btnSTRT.setEnabled(true);
				btnACPT.setEnabled(false);
		 	}
			else if(L_ACTCMD.equals("help"))
				exeHLPOK();
			if(M_objSOURC == btnPRINT1)
			{
		       if(txtGINNO.getText().trim().length() ==0)
		       {
		            setMSG("Enter Gate In number..",'E');
		            return;
		        }
		        mm_rptun objTUNRP  = new mm_rptun(txtGINNO.getText().trim());
				objTUNRP.getDATA();
				JComboBox L_cmbLOCAL = objTUNRP.getPRNLS();
				objTUNRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rptun.doc",L_cmbLOCAL.getSelectedIndex());
            }
			if(M_objSOURC == btnPRINT)
			{
				try
				{
					strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
					mm_rpwbt omm_rpwbt=new mm_rpwbt(1);
					if(strGINTP.equals(strDESPT))
					{
						if(omm_rpwbt.crtREPT("LA",strPGINTP,strPGINNO,strPSRLNO,"EXT"))
						   JOptionPane.showMessageDialog(this,"Insert Loading Advice Paper in Printer","WeighBridge In/Out Time",JOptionPane.INFORMATION_MESSAGE); 
					}
					else
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						{
							strPGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
							strPGINNO = txtGINNO.getText().trim();
							strPSRLNO = "1";
						}
						if(omm_rpwbt.crtREPT("TKT",strPGINTP,strPGINNO,strPSRLNO,"EXT"))
 						{
							Runtime r = Runtime.getRuntime();
							Process p = null;
							String [] L_staTEMP = omm_rpwbt.getPRINTERS();	//get list of printer
							JComboBox L_cmbTEMP = new JComboBox(L_staTEMP);	//add to combo box
							L_cmbTEMP.insertItemAt("Select",0);
							JOptionPane.showConfirmDialog( this,L_cmbTEMP,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
							omm_rpwbt.M_cmbDESTN = L_cmbTEMP;	//initialise priter selection to destination
							omm_rpwbt.M_cmbDESTN.setSelectedIndex(L_cmbTEMP.getSelectedIndex());
							omm_rpwbt.txtGINTP.setText(strPGINTP);
							omm_rpwbt.txtGINNO.setText(strPGINNO);
							omm_rpwbt.exePRINT();	//Call print method of Repoer Program
							L_cmbTEMP.removeAllItems();	//Remove All Item (List Of Printer ) From combo box 
						}
					}
				//	clrCOMP();
				//	exeWRKSTA();
				//	btnPRINT.setEnabled(false);
				}
				catch(Exception L_E)
				{
					System.out.println(L_E.toString());
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"Wbtrn actionPerformed");
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
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				if(M_objSOURC == txtGINNO)				// Gate-In No.
				{
					try
					{
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtGINNO";
						String L_staHEADG[] = {"Gate-In No.","Lorry No","Gate-In Time"};
						M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_GINDT from MM_WBTRN";
						M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
							M_strSQLQRY += " and WB_STSFL not in ('X','N')";
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							M_strSQLQRY += " and WB_STSFL in ('2','4')";
						else if(chrWBOPT == 'O')	// Weigh-Out
							M_strSQLQRY += " and WB_STSFL in ('2','3')";
						else if(chrWBOPT == 'I')		// Weigh-In
							M_strSQLQRY += " and WB_STSFL in ('0','1')";
						
						M_strSQLQRY += " order by WB_DOCNO desc";
						if(M_strSQLQRY != null)
							cl_hlp(M_strSQLQRY,2,1,L_staHEADG,3,"CT");
					}
					catch(Exception L_EX){
						this.setCursor(cl_dat.M_curDFSTS_pbst);
						setMSG(L_EX,"hlpGINNO");
					}
				}
				else if(M_objSOURC == txtORDRF){					// P.O./D.O. Ref
					if(strGINTP.equals(strDESPT))				    // Despatch
						hlpLARNO();
				}
				else if(M_objSOURC == txtMATCD){					// Material Code
					if(strGINTP.equals(strRECPT))				    // Receipt
						hlpMATCD();
					//else if(strGINTP.equals(strSLSRT))				    // Receipt
					//	hlpMATCD();
					else if(strGINTP.equals(strRAWMT))			    // Raw Material
						hlpMATTF();
				}
				else if(M_objSOURC == txtTNKNO){					// Tank No
					if(strGINTP.equals(strRAWMT)){			        // Raw Material
						strMATCD = txtMATCD.getText().trim();
						hlpTNKNO(strMATCD);
					}
				}
				else if(M_objSOURC == txtTPRCD)			// Transporter
					hlpTPRCD();
				else if(M_objSOURC == txtDRVCD)			// Driver Code
					hlpDRVCD();
				else if(M_objSOURC == txtLOCCD)			// Location Code
					hlpLOCCD();
				else if(M_objSOURC == txtPRTCD)			// Buyer
					hlpPRTCD();
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(L_KE.getKeyCode() == L_KE.VK_TAB || L_KE.getKeyCode() == L_KE.VK_ENTER)
			{				
				if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
					exeHLPOK();		
				else if(M_objSOURC == txtORDRF){			// ORD Ref
					strORDRF = txtORDRF.getText().trim();
					strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
					
					if(strGINTP.equals(strDESPT))				// Despatch
						vldLARNO(strORDRF);
				}
				else if(M_objSOURC == txtLRYNO){			// Lorry No
					txtLRYNO.setText(txtLRYNO.getText().trim().toUpperCase());
					txtTPRCD.requestFocus();
				}
				else if(M_objSOURC == txtTPRCD){			// Transporter Code
					strTPRCD = txtTPRCD.getText().trim();
					vldTPRCD(strTPRCD);
				}
				else if(M_objSOURC == txtPRTCD){			// Buyer Code
					strPRTCD = txtPRTCD.getText().trim();
					vldPRTCD(strPRTCD);
				}
				else if(M_objSOURC == txtDRVCD){			// Driver Code
					strDRVCD = txtDRVCD.getText().trim();
					vldDRVCD(strDRVCD);
				}
				else if(M_objSOURC == txtMATCD){			// Material code
					strMATCD = txtMATCD.getText().trim();
					if(strMATCD.length() != 0){
						if(strGINTP.equals(strDESPT))				// Despatch
							vldMATTF(strMATCD);
						else if(strGINTP.equals(strRAWMT))			// Raw Material
							vldMATCD(strMATCD);
					}
					else{
						txtBOENO.requestFocus();
						setMSG("",'N');
					}
				}
				else if(M_objSOURC == txtMATDS){			// Material Description
					txtTNKNO.requestFocus();
					setMSG("",'N');
				}
				else if(M_objSOURC == txtBOENO){			// B/E No.
					if(txtBOENO.getText().trim().length() >0)
					{						
						String L_STRQRY = "select count(*)L_CNT from mm_betrn where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_BOENO ='"+txtBOENO.getText().trim() +"'";
						if(cl_dat.getRECCNT(L_STRQRY)<= 0)
						{
							txtBOENO.setText("");
							setMSG("Bill of entry master data not present..",'E');
						}
					}
					txtTNKNO.requestFocus();
					setMSG("",'N');
				}
				else if(M_objSOURC == txtTNKNO){			// Tank No.
					strMATCD = txtMATCD.getText().trim();
					strTNKNO = txtTNKNO.getText().trim();
					vldTNKNO(strTNKNO,strMATCD);
				}
				
				else if(M_objSOURC == txtCHLNO){			// Chalan No
					txtCHLDT.requestFocus();
					setMSG("",'N');
				}
				else if(M_objSOURC == txtCHLDT){			// Chalan Date
					if(txtCHLDT.getText().trim().length() == 0)
					{
						txtCHLDT.requestFocus();
						setMSG("Chalan Date should not be empty",'E');
					}
					else
					{
						if(M_fmtLCDAT.parse(txtCHLDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)			
						{
							setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
							txtCHLDT.requestFocus();
						}
						else
						{
							setMSG("",'N');
						////	txtCHLDT.setText(L_CSTDT);
							txtCHLQT.requestFocus();
						}
					}
				}
				else if(M_objSOURC == txtCHLQT){			// Chalan Quantity
					if(txtCHLQT.getText().trim().length() == 0)
					{
						txtCHLQT.requestFocus();
						setMSG("Chalan Quantity should not be empty",'E');
					}
					else
					{
						double L_dblCHLQT;
						
						try
						{
							L_dblCHLQT = Double.parseDouble(txtCHLQT.getText().trim());
							setMSG("",'N');
							if(btnSTRT.isEnabled())
								btnSTRT.requestFocus();
							else
								btnACPT.requestFocus();
						}
						catch(NumberFormatException e)
						{
							setMSG("Enter in correct Decimal format",'E');
							txtCHLQT.requestFocus();
						}
					}
				}
				else if(M_objSOURC == btnSTRT){			// Start Button
					btnACPT.requestFocus();
					setMSG("",'N');
				}
				else if(M_objSOURC == btnACPT){			// Accept Button
					txtLOCCD.requestFocus();
					setMSG("",'N');
				}
				else if(M_objSOURC == txtLOCCD){			// Location
					strLOCCD = txtLOCCD.getText().trim();
					if(vldLOCCD(strLOCCD)){
						cl_dat.M_btnSAVE_pbst.requestFocus();
						setMSG("",'N');
					}
					else{
						txtLOCCD.requestFocus();
						setMSG("Location should not be empty",'E');						
					}
				}
				else if(M_objSOURC == txtTFIDT){			
					txtTFITM.requestFocus();
					setMSG("Enter Tankfarm In Time..",'N');
				}
				else if(M_objSOURC == txtTFITM){			
					txtULSDT.requestFocus();
					setMSG("Enter Unloading Start Date..",'N');
				}
				else if(M_objSOURC == txtULSDT){			
					txtULSTM.requestFocus();
					setMSG("Enter Unloading Start Time..",'N');
				}
				else if(M_objSOURC == txtULSTM){			
					txtULEDT.requestFocus();
					setMSG("Enter Unloading End Date..",'N');
				}
				else if(M_objSOURC == txtULEDT){			
					txtULETM.requestFocus();
					setMSG("Enter Unloading End Time..",'N');
				}
				else if(M_objSOURC == txtULETM){			
					txtTFODT.requestFocus();
					setMSG("Enter Tankfarm Out Date..",'N');
				}
				else if(M_objSOURC == txtTFODT){			
					txtTFOTM.requestFocus();
					setMSG("Enter Tankfarm Out Time..",'N');
				}
				else if(M_objSOURC == txtTFOTM){			
					txtULDBY.requestFocus();
					setMSG("Unloading By..",'N');
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"keyPressed");
		}
	}
	// Check on validity of Gate-In No.
	private boolean vldGINNO(String P_strGINTP,String P_strGINNO)
	{
		try
		{
			M_strSQLQRY = "Select WB_DOCTP,WB_DOCNO from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				M_strSQLQRY += " and WB_STSFL not in ('X','N')";
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				M_strSQLQRY += " and WB_STSFL in ('2','4')";
			else if(chrWBOPT == 'I')		// Weigh-In
				M_strSQLQRY += " and WB_STSFL in ('0','1')";
			else if(chrWBOPT == 'O')	// Weigh-Out
				M_strSQLQRY += " and WB_STSFL in ('2','3')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				M_rstRSSET.close();
				return true;
			}
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			return false;
		}catch(Exception L_EX){
			setMSG(L_EX,"vldGINNO");
			return false;
		}	
	}
	// Help on Loading Advice Reference
	private void hlpLARNO()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtLARNO";
			String L_staHEADG[] = {"L.A.No.","Buyer","Lorry No."};
		
			M_strSQLQRY = "Select IVT_LADNO,IVT_BYRCD,IVT_LRYNO";
			M_strSQLQRY += " from MR_IVTRN ";
			M_strSQLQRY += " where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL = 'A'";
			M_strSQLQRY += " order by IVT_LADNO";
		
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,3,1,L_staHEADG,3,"CT");
		}
		catch(Exception e)
		{
			System.out.println("Error in hlpLARNO : " + e.toString());
		}
	}
	
	// Check valid Loading Advice 
	private boolean vldLARNO(String LP_LARNO)
	{
		try{
			M_strSQLQRY = "Select IVT_BYRCD,IVT_LRYNO";
			M_strSQLQRY += " from MR_IVTRN ";
			M_strSQLQRY += " where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL = 'A'";
			M_strSQLQRY += " and IVT_LADNO = '" + LP_LARNO + "'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET.next()){
				txtORDRF.setText(LP_LARNO);
				strPRTCD = M_rstRSSET.getString("IVT_BYRCD");
				txtPRTCD.setText(strPRTCD);
				txtPRTDS.setText(getPRTDS("C",strPRTCD));
				txtLRYNO.setText(M_rstRSSET.getString("IVT_LRYNO"));
				txtDRVCD.requestFocus();
				
				M_rstRSSET.close();
				return true;			
			}
			
			txtPRTCD.setText("");
			txtPRTDS.setText("");
			txtTPRCD.setText("");
			txtTPRDS.setText("");
			txtLRYNO.setText("");
			txtORDRF.requestFocus();
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			return false;			
		}catch(Exception e){
			System.out.println("Error in vldLARNO : " + e.toString());
			return false;
		}
	}
	
	// Help on Transporter
	private void hlpTPRCD(){
		try{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtTPRCD";
			
			String L_staHEADG[] = {"Code","Transporter"};
			M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
			M_strSQLQRY += " where PT_PRTTP = 'T' and PT_STSFL <> 'X'";
            M_strSQLQRY += " order by PT_PRTCD,PT_PRTNM";
				
			if(M_strSQLQRY != null){
				cl_hlp(M_strSQLQRY,2,1,L_staHEADG,2,"CT");
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"hlpTPRCD");
		}	
	}
	
	// Validation on Transporter
	private boolean vldTPRCD(String LP_TPRCD)
	{
		try{
			M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST";
			M_strSQLQRY += " where PT_PRTTP = 'T' and PT_STSFL <> 'X' and ";
			M_strSQLQRY += " PT_PRTCD = '" + LP_TPRCD + "'";
            	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			
			if(M_rstRSSET.next()){
				txtTPRDS.setText(M_rstRSSET.getString("PT_PRTNM"));
				txtDRVCD.requestFocus();
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			txtTPRDS.setText("");
			txtTPRCD.requestFocus();
			setMSG("Invalid Transporter. Press F1 for help",'E');
				
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"vldTPRCD");
			return false;
		}	
		return false;
	}
	
	// Help on Driver Code
	private void hlpDRVCD(){
		try{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtDRVCD";
			
			String L_staHEADG[] = {"Code","Driver"};
			M_strSQLQRY = "select DV_DRVCD,DV_DRVNM from MM_DVMST";
			M_strSQLQRY += " where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DV_STSFL <> 'X'";	
			
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,2,2,L_staHEADG,2,"CT");
		}catch(Exception L_EX){
			setMSG(L_EX,"hlpDRVCD");
		}	
	}
	
	// Check whether the driver code is valid or not
	private boolean vldDRVCD(String LP_DRVCD){
		try{
			M_strSQLQRY = "select DV_DRVCD,DV_DRVNM from MM_DVMST";
			M_strSQLQRY += " where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DV_DRVCD = '" + LP_DRVCD + "'";
			M_strSQLQRY += " and DV_STSFL <> 'X'";
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET.next()){
				txtDRVNM.setText(M_rstRSSET.getString("DV_DRVNM"));
				if(txtMATCD.isEnabled())
					txtMATCD.requestFocus();
				else
					txtMATDS.requestFocus();
				
				M_rstRSSET.close();
				setMSG("",'N');
				return true;
			}
			else{
				txtDRVCD.requestFocus();
				txtDRVNM.setText("");
				setMSG("Invalid Driver code. Press F1 for help or press F3 for new Driver entry.",'E');
				return false;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"vldDRVCD");
		}	
		return true;
	}
	
	// Help on Material 
	private void hlpMATCD(){
		try{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtMATCD";
			
			String L_ARRHDR[] = {"Code","Material"};
			M_strSQLQRY = "select CT_MATCD,CT_MATDS from CO_CTMST";
			M_strSQLQRY += " where CT_MATCD like '68%' and SUBSTRING(CT_MATCD,10,1) != 'A'";
			M_strSQLQRY += " order by CT_MATDS";
						
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY ,2,2,L_ARRHDR,2,"CT");
		}catch(Exception L_EX){
			setMSG(L_EX,"hlpMATCD");
		}	
	}

	// Validation of Material
	private boolean vldMATCD(String LP_MATCD){
		try{
			M_strSQLQRY = "select CT_MATCD,CT_MATDS from CO_CTMST";
			M_strSQLQRY += " where SUBSTRING(CT_MATCD,10,1) != 'A' ";
			M_strSQLQRY += " and CT_MATCD = '" + LP_MATCD + "'";
            	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			
			if(M_rstRSSET.next()){
				txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));
				txtTNKNO.requestFocus();
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			else
			{
				txtMATDS.setText("");
				txtMATCD.requestFocus();
				setMSG("Invalid Material. Press F1 for help",'E');
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"vldMATCD");
			return false;
		}	
		return false;
	}
	// Help on Location
	private void hlpLOCCD(){
		try{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtLOCCD";
			
			String L_ARRHDR[] = {"Code","Location"};
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'COXXDST'";
						
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY ,2,2,L_ARRHDR,2,"CT");
		}catch(Exception L_EX){
			setMSG(L_EX,"hlpLOCCD");
		}	
	}
	
	// Validation on Location
	private boolean vldLOCCD(String LP_LOCCD){
		try{
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'COXXDST' and ";
			M_strSQLQRY += " CMT_CODCD = '" + LP_LOCCD + "'";
            	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			
			if(M_rstRSSET.next()){
				txtLOCNM.setText(M_rstRSSET.getString("CMT_CODDS"));
				if(M_rstRSSET != null)
					M_rstRSSET.close();			
				return true;
			}
			else
				txtLOCNM.setText("");
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
			return false;
		}catch(Exception L_EX){
			setMSG(L_EX,"vldLOCCD");
			return false;
		}	
	}

	// Get the Location Description
	private String getLOCDS(String LP_LOCCD){
		String L_LOCDS = "";
		
		try{
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'COXXDST' and ";
			M_strSQLQRY += " CMT_CODCD = '" + LP_LOCCD + "'";
            	
			M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY );
			
			if(M_rstRSSET1.next())
				L_LOCDS = M_rstRSSET1.getString("CMT_CODDS");
			
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"getLOCDS");
			return L_LOCDS;
		}	
		return L_LOCDS;
	}
	
	// Method to get the last Driver from MM_LRMST
	private String getDRVDS(String LP_TPRCD,String LP_LRYNO){
		String L_DRVCD = "";
		try{
			M_strSQLQRY = "Select LR_DRVCD from MM_LRMST ";
			M_strSQLQRY += " where LR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LR_TPRCD = '" + LP_TPRCD + "'";
			M_strSQLQRY += " and LR_LRYNO = '" + LP_LRYNO + "'";
		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				L_DRVCD = nvlSTRVL(M_rstRSSET.getString("LR_DRVCD"),"").trim();
				M_rstRSSET.close();
			}	
		}catch(Exception e){
			System.out.println("Error in getDRVDS : " + e.toString());
		}
		return L_DRVCD;
	}
	
	void exeHLPOK(){
		super.exeHLPOK();
		try{
			if(M_strHLPFLD.equals("txtGINNO")){
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				strGINNO = cl_dat.M_strHLPSTR_pbst;
				cl_dat.M_strHLPSTR_pbst = "";
				
				if(strGINTP.equals(strOTHER))
					strSRLNO = getSRLNO(strGINTP,strGINNO);
				else
					strSRLNO = "1";
							
				exeGETREC(strGINTP,strGINNO,strSRLNO);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)){
					setENBL(true);
					cmbGINTP.setEnabled(false);
					btnWB1.setEnabled(true);
					btnWB2.setEnabled(true);
					btnSTRT.setEnabled(true);
					btnACPT.setEnabled(false);
					btnSTRT.requestFocus();
				}
			}
			else if(M_strHLPFLD.equals("txtPORNO")){			// P.O.Ref.No.
				strORDRF = cl_dat.M_strHLPSTR_pbst;
				txtORDRF.setText(strORDRF);
				strPRTCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim();
				txtPRTCD.setText(strPRTCD);
				txtPRTDS.setText(getPRTDS("C",strPRTCD));
				strTPRCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim();
				txtTPRCD.setText(strTPRCD);
				txtTPRDS.setText(getPRTDS("T",strTPRCD));
			    strLRYNO = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim();
				txtLRYNO.setText(strLRYNO);

				// Get the last stored Driver
				getDRVDS(strTPRCD,strLRYNO);
				
				txtDRVCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtLARNO")){			// LA.No.
				strORDRF = cl_dat.M_strHLPSTR_pbst;
				txtORDRF.setText(strORDRF);
				strPRTCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
				txtPRTCD.setText(strPRTCD);
				txtPRTDS.setText(getPRTDS("C",strPRTCD));
				strLRYNO = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim();
				txtLRYNO.setText(strLRYNO);
				txtDRVCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtTPRCD")){			// Transporter
				txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtTPRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				txtDRVCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtDRVCD")){			// Driver
				txtDRVCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtDRVNM.setText(cl_dat.M_strHLPSTR_pbst);
				if(txtMATCD.isEnabled())
					txtMATCD.requestFocus();
				else
					txtMATDS.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtMATCD")){			// Material
				txtMATDS.setText(cl_dat.M_strHLPSTR_pbst);
				txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				if(strGINTP.equals(strRAWMT))
					txtUOMCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
				//txtBOENO.requestFocus();
				txtTNKNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtTNKNO")){			// Tank No
				txtTNKNO.setText(cl_dat.M_strHLPSTR_pbst);
				txtCHLNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtLOCCD")){			// Location
				txtLOCCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtLOCNM.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtPRTCD")){			// Buyer
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"exeHLPOK");
		}
	}
	
	// Clear all the fields
	private void clrSCR(){
		LM_PRESENT = false;		// indicates record is not fetched from table
		LM_CONFIRM = 0;			// initSCR the confirmation flag

		txtGINNO.setText("");
		txtORDRF.setText("");
		txtLRYNO.setText("");
		txtTPRCD.setText("");
		txtTPRDS.setText("");
		txtDRVCD.setText("");
		txtDRVNM.setText("");
		txtGINDT.setText("");
		txtMATCD.setText("");
		txtMATDS.setText("");
		txtUOMCD.setText("");
		cmbMATTP.setSelectedIndex(0);
		txtLRYWT.setText("");
		lblGRSWT.setText("");
		lblTARWT.setText("");
		lblNETWT.setText("");
		lblGRSDT.setText("");
		lblTARDT.setText("");
		txtCHLNO.setText("");
		txtCHLDT.setText("");
		txtCHLQT.setText("");
		txtLOCCD.setText("");
		txtLOCNM.setText("");
		lblGWTBY.setText("");		
		lblTWTBY.setText("");
		txtPRTCD.setText("");
		txtPRTDS.setText("");
		txtREMWB.setText("");
		txtBOENO.setText("");
		txtTNKNO.setText("");
		txtGVTVL.setText("");
        txtTFIDT.setText("");
        txtTFITM.setText("");
        txtTFODT.setText("");
        txtTFOTM.setText("");
        txtULSDT.setText("");
        txtULSTM.setText("");
        txtULEDT.setText("");
        txtULETM.setText("");
        rdoBSYES.setSelected(false);
        rdoBSNO.setSelected(false);
        rdoLOYES.setSelected(false);
        rdoLONO.setSelected(false);
        rdoEPYES.setSelected(false);
        rdoEPNO.setSelected(false);
        rdoERYES.setSelected(false);
        rdoERNO.setSelected(false);
        txtULDBY.setText("");
		if(serialPort != null)
			serialPort.close();
	}
	private void initSCR(){
		clrSCR();		// Clear all fields
		if(chrWBOPT == 'O' ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{
			setENBL(false);
			btnSTRT.setEnabled(false);
			btnACPT.setEnabled(false);
			txtGINNO.requestFocus();
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			setENBL(false);
			btnSTRT.setEnabled(false);
			btnACPT.setEnabled(false);
			txtGINNO.requestFocus();
		}
		else if(chrWBOPT == 'I')
		{
			setENBL(true);
			cmbGINTP.setEnabled(true);			
			txtGINNO.setEnabled(true);						
			btnSTRT.setEnabled(true);
			btnACPT.setEnabled(false);
			txtGINNO.requestFocus();
		}
		else
		{
			setMSG("Select an option",'N');
			setENBL(false);
			cmbGINTP.setEnabled(false);			
			txtGINNO.setEnabled(false);
			btnSTRT.setEnabled(false);
			btnACPT.setEnabled(false);
		}
		
	}
	
	// Method to check whether all necessary data has been provided
	boolean vldDATA(){
		try{
			// If Gate-In Entry is not done by security
			if(!LM_PRESENT){
				if(LM_CONFIRM < 3){
					//JOptionPane.showMessageDialog(this,Object("New Gate-In No. cannot be generated.Contact Security for new Entry"),"New Entry",JOptionPane.INFORMATION_MESSAGE); 
					LM_CONFIRM++;
					return false;
				}
				else{
					LM_CONFIRM = 0;
					genGINNO(strGINTP);
				}
			}
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			strGINNO = txtGINNO.getText().trim();
			strDRVCD = txtDRVCD.getText().trim();
			strMATCD = txtMATCD.getText().trim();
			strBOENO = txtBOENO.getText().trim();
			strTNKNO = txtTNKNO.getText().trim();
			strCHLNO = txtCHLNO.getText().trim();
			if(txtCHLDT.getText().trim().length()>0)
				strCHLDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHLDT.getText().trim()))+"'";
                      //  else
                      //          strCHLDT = "null";
			strCHLQT = txtCHLQT.getText().trim();
			strORDRF = txtORDRF.getText().trim();
						
			if(txtLRYNO.getText().trim().length() == 0){			// Lorry No.
				txtLRYNO.requestFocus();
				setMSG("Lorry No. should not be empty.",'E');
				return false;
			}
		
			if(txtTPRCD.getText().trim().length() != 0){			// Transporter Code
				if(!vldTPRCD(txtTPRCD.getText().trim()))
					return false;
			}
			else{
				txtTPRCD.requestFocus();
				setMSG("Transporter should not be empty.",'E');
				return false;
			}
			if(txtPRTCD.getText().trim().length() != 0){			// Party Code
				if(!vldPRTCD(txtPRTCD.getText().trim()))
					return false;
			}
			
			if(strDRVCD.length() != 0){								// Driver Code
				if(!vldDRVCD(strDRVCD))
					return false;
			}
			if(txtMATCD.getText().trim().length() != 0){			// Material
				if(strGINTP.equals(strRAWMT))						// Raw Material
					if(!vldMATTF(strMATCD))
						return false;
				else if(strGINTP.equals(strDESPT))					// Despatch
					if(!vldMATCD(strMATCD))
						return false;
			}
			else if(txtMATDS.getText().trim().length() == 0){
				txtMATDS.requestFocus();
				setMSG("Please Enter the Material",'N');
				return false;
			}
			if(strGINTP.equals(strRAWMT)){						// Specific Gravity
				if(chrWBOPT == 'O' || strSTSFL.equals(strSTSOT)){ // Out Entry
					strGVTVL = txtGVTVL.getText().trim();
					strUOMCD = txtUOMCD.getText().trim();
					if(strUOMCD.equals("KL")){
						if(strGVTVL.length() == 0 || Double.parseDouble(strGVTVL)==0){
							txtGVTVL.requestFocus();
							setMSG("Please Enter the Specific Gravity.",'E');
							return false;
						}
						try{
							Double.parseDouble(strGVTVL);
						}catch(NumberFormatException e){
							txtGVTVL.requestFocus();
							setMSG("Please Enter the Specific Gravity in proper decimal format.",'E');
							return false;
						}
					}
					else
						strGVTVL = "0";	
				}
			}	
				
			// Checking for B/E No. & Tank No.
			if(chrWBOPT == 'O' || strSTSFL.equals(strSTSOT)){ // Out Entry
				if(strGINTP.equals(strRAWMT)){				// Raw Material
					if(strTNKNO.length() == 0){				// Tank No.
						txtTNKNO.requestFocus();
						setMSG("Please enter Tank No.",'E');
						return false;
					}
					else if(!vldTNKNO(strTNKNO,strMATCD)){
						return false;
					}
				}
			}
			// Checking for weight
			if(chrWBOPT == 'I'){
				if(strGINTP.equals(strRAWMT) || strGINTP.equals(strRECPT)||strGINTP.equals(strSLSRT)){		// Raw Material/Receipt
					if(lblGRSWT.getText().trim().length() == 0){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						
						setMSG("Take the Gross Weight",'E');
						return false;		
					}
					
					try{
						if(Double.parseDouble(lblGRSWT.getText().trim()) == 0){
							if(btnSTRT.isEnabled())
								btnSTRT.requestFocus();
							else
								btnACPT.requestFocus();
							setMSG("Take the Gross Weight",'E');
							return false;							   
						}
					}catch(NumberFormatException e){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						setMSG("Take the Gross Weight",'E');
						return false;		
					}
				}
				else if(strGINTP.equals(strDESPT) || strGINTP.equals(strOTHER)){	// Despatch/Other
					if(lblTARWT.getText().trim().length() == 0){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						
						setMSG("Take the Tare Weight",'E');
						return false;		
					}
					
					try{
						if(Double.parseDouble(lblTARWT.getText().trim()) == 0){
							if(btnSTRT.isEnabled())
								btnSTRT.requestFocus();
							else
								btnACPT.requestFocus();
							setMSG("Take the Tare Weight",'E');
							return false;							   
						}
					}catch(NumberFormatException e){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						setMSG("Take the Tare Weight",'E');
						return false;		
					}
				}
			}
			else if(chrWBOPT == 'O'){
				////////////////////////////////
                                if(strGINTP.equals(strRAWMT) || strGINTP.equals(strRECPT)|| strGINTP.equals(strSLSRT)){		// Raw Material/Receipt
					if(lblTARWT.getText().trim().length() == 0){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						
						setMSG("Take the Tare Weight",'E');
						return false;		
					}
					try{
						if(Double.parseDouble(lblTARWT.getText().trim()) == 0){
							if(btnSTRT.isEnabled())
								btnSTRT.requestFocus();
							else
								btnACPT.requestFocus();
							setMSG("Take the Tare Weight",'E');
							return false;							   
						}
					}catch(NumberFormatException e){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						setMSG("Take the Tare Weight",'E');
						return false;		
					}
				}
				 if(strGINTP.equals(strDESPT) || strGINTP.equals(strOTHER)){	// Despatch/Other
					if(lblGRSWT.getText().trim().length() == 0){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						
						setMSG("Take the Gross Weight",'E');
						return false;		
					}
					
					try{
						if(Double.parseDouble(lblGRSWT.getText().trim()) == 0){
							if(btnSTRT.isEnabled())
								btnSTRT.requestFocus();
							else
								btnACPT.requestFocus();
							setMSG("Take the Gross Weight",'E');
							return false;							   
						}
					}catch(NumberFormatException e){
						if(btnSTRT.isEnabled())
							btnSTRT.requestFocus();
						else
							btnACPT.requestFocus();
						setMSG("Take the Gross Weight",'E');
						return false;		
					}
				}
			}
		
			if(strGINTP.equals(strRAWMT)){
				if(strCHLNO.length() == 0){		// Chalan No.
					txtCHLNO.requestFocus();
					setMSG("Chalan No. should not be empty.",'E');
					return false;
				}
                              
				if(strCHLDT.length() == 0){		// Chalan Date
					txtCHLDT.requestFocus();
					setMSG("Chalan Date should not be empty",'E');
					return false;
				}
				if(txtCHLQT.getText().trim().length() == 0){		// Chalan Quantity
					txtCHLQT.requestFocus();
					setMSG("Chalan Quantity should not be empty",'E');
					return false;
				}
			}

                        if(txtCHLDT.getText().trim().length() != 0){             // Chalan Date
				if(M_fmtLCDAT.parse(txtCHLDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)			
				{
					setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
					txtCHLDT.requestFocus();
					return false;
				}
			}
		
			if(strCHLQT.length() != 0){		// Chalan Quantity
				try{
					Double.parseDouble(txtCHLQT.getText().trim());
				}catch(NumberFormatException e){
					setMSG("Enter in correct Decimal format",'E');
					txtCHLQT.requestFocus();
					return false;
				}
			}
			if(strGINTP.equals(strRAWMT)){			// Raw Material
				if(txtLOCCD.getText().trim().length() == 0){		// Location Code
					txtLOCCD.requestFocus();
					setMSG("Location should not be empty.",'E');
					return false;
				}
			}


			if(txtLOCCD.getText().trim().length() != 0){
				if(!vldLOCCD(txtLOCCD.getText().trim()))
					return false;
			}
		}catch(Exception e){
			System.out.println("Error in vldDATA : " + e.toString());
			return false;
		}
		return true;
	}
	// Get the record from the MM_WBTRN table
	private void exeGETREC(String P_strGINTP,String P_strGINNO,String P_strSRLNO){
		try{
			this.setCursor(cl_dat.M_curWTSTS_pbst);	
			java.sql.Timestamp L_tmsINCTM,L_tmsOUTTM,L_tmsGINDT,L_tmsTEMP;
			M_strSQLQRY = "Select WB_WBRNO,WB_SRLNO,WB_DOCNO,WB_LRYNO,WB_TPRCD,WB_TPRDS,";
			M_strSQLQRY += " WB_DRVCD,WB_DRVNM,WB_MATCD,WB_CHLNO,WB_CHLDT,WB_CHLQT,WB_GRSWT,";
			M_strSQLQRY += " WB_TARWT,WB_NETWT,WB_INCTM,WB_OUTTM,WB_MATDS,WB_ORDRF,";
			M_strSQLQRY += " WB_GINDT,WB_DOCRF,WB_LOCCD,WB_REMWB,WB_STSFL,WB_WINBY,WB_WOTBY,";
			M_strSQLQRY += " WB_PRTCD,WB_PRTDS,WB_BOENO,WB_TNKNO,WB_MATTP,WB_GVTVL,WB_BSCHK,WB_ERCHK,WB_LOCHK,WB_EPCHK,";
			M_strSQLQRY += " WB_TFITM,WB_TFOTM,WB_ULSTM,WB_ULETM,WB_ULDBY from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			M_strSQLQRY += " and WB_SRLNO = '" + P_strSRLNO + "'";
				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
                        if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				LM_PRESENT = true;
                                strWBRNO = nvlSTRVL(M_rstRSSET.getString("WB_WBRNO"),"");
                                strSRLNO = nvlSTRVL(M_rstRSSET.getString("WB_SRLNO"),"");                         
                                strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"");
                                strTPRCD = nvlSTRVL(M_rstRSSET.getString("WB_TPRCD"),"");
                                strTPRDS = nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"");
                                strDRVCD = nvlSTRVL(M_rstRSSET.getString("WB_DRVCD"),"");
                                strDRVNM = nvlSTRVL(M_rstRSSET.getString("WB_DRVNM"),"");
                                strMATCD = nvlSTRVL(M_rstRSSET.getString("WB_MATCD"),"");
                                strCHLNO = nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),"");
                                LD_CHLDT = M_rstRSSET.getDate("WB_CHLDT");
                                strCHLQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"");
                                strGRSWT = nvlSTRVL(M_rstRSSET.getString("WB_GRSWT"),"0");
                                strTARWT = nvlSTRVL(M_rstRSSET.getString("WB_TARWT"),"0");
                                strNETWT = nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0");
                                strINCTM = nvlSTRVL(M_rstRSSET.getString("WB_INCTM"),"");
                                strOUTTM = nvlSTRVL(M_rstRSSET.getString("WB_OUTTM"),"");
				L_tmsINCTM = M_rstRSSET.getTimestamp("WB_INCTM");
				L_tmsOUTTM = M_rstRSSET.getTimestamp("WB_OUTTM");
                                strMATDS = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"").toUpperCase();
                                strORDRF = nvlSTRVL(M_rstRSSET.getString("WB_ORDRF"),"");
                                strGINDT = nvlSTRVL(M_rstRSSET.getString("WB_GINDT"),""); 

                        	L_tmsGINDT = M_rstRSSET.getTimestamp("WB_GINDT");
                                strLOCCD = nvlSTRVL(M_rstRSSET.getString("WB_LOCCD"),"");
                                strREMWB = nvlSTRVL(M_rstRSSET.getString("WB_REMWB"),"");
                                strSTSFL = nvlSTRVL(M_rstRSSET.getString("WB_STSFL"),"");
                                strWINBY = nvlSTRVL(M_rstRSSET.getString("WB_WINBY"),""); 
                                strWOTBY = nvlSTRVL(M_rstRSSET.getString("WB_WOTBY"),""); 

                                strPRTCD = nvlSTRVL(M_rstRSSET.getString("WB_PRTCD"),"");
                                strPRTDS = nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),"");
                                strBOENO = nvlSTRVL(M_rstRSSET.getString("WB_BOENO"),"");
                                strTNKNO = nvlSTRVL(M_rstRSSET.getString("WB_TNKNO"),"");
                                strMATTP = nvlSTRVL(M_rstRSSET.getString("WB_MATTP"),"");
                                strGVTVL = nvlSTRVL(M_rstRSSET.getString("WB_GVTVL"),"");
                                if(nvlSTRVL(M_rstRSSET.getString("WB_BSCHK"),"").equals("Y"))
                                    rdoBSYES.setSelected(true);
                                else
                                    rdoBSNO.setSelected(true);
                                if(nvlSTRVL(M_rstRSSET.getString("WB_ERCHK"),"").equals("Y"))
                                    rdoERYES.setSelected(true);
                                else
                                    rdoERNO.setSelected(true);
                                if(nvlSTRVL(M_rstRSSET.getString("WB_EPCHK"),"").equals("Y"))
                                    rdoEPYES.setSelected(true);
                                else
                                    rdoEPNO.setSelected(true);    
                                 if(nvlSTRVL(M_rstRSSET.getString("WB_LOCHK"),"").equals("Y"))
                                    rdoLOYES.setSelected(true);
                                else
                                    rdoLONO.setSelected(true);
                                L_tmsTEMP = M_rstRSSET.getTimestamp("WB_TFITM");
                                if(L_tmsTEMP !=null)
                                {
                                    txtTFIDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
                                    txtTFITM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
                                    
                                }
                                 L_tmsTEMP = M_rstRSSET.getTimestamp("WB_TFOTM");
                                if(L_tmsTEMP !=null)
                                {
                                    txtTFODT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
                                    txtTFOTM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
                                    
                                }
                                L_tmsTEMP = M_rstRSSET.getTimestamp("WB_ULSTM");
                                if(L_tmsTEMP !=null)
                                {
                                    txtULSDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
                                    txtULSTM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
                                    
                                }
                                L_tmsTEMP = M_rstRSSET.getTimestamp("WB_ULETM");
                                if(L_tmsTEMP !=null)
                                {
                                    txtULEDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
                                    txtULETM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
                                }
                                txtULDBY.setText(nvlSTRVL(M_rstRSSET.getString("WB_ULDBY"),""));
                                
				if(strWBRNO.equals("1"))
					btnWB1.setSelected(true);
				else if(strWBRNO.equals("2"))
					btnWB2.setSelected(true);
				///cmbGINTP.setSelectedIndex(Integer.parseInt(P_strGINTP) - 1);
                               
                                txtGINNO.setText(P_strGINNO);
				txtLRYNO.setText(strLRYNO);
				txtTPRCD.setText(strTPRCD);
				txtTPRDS.setText(strTPRDS);
				txtDRVCD.setText(strDRVCD);
				txtDRVNM.setText(strDRVNM);
				txtMATCD.setText(strMATCD);
				txtMATDS.setText(strMATDS);
				txtCHLNO.setText(strCHLNO);
                                if(LD_CHLDT !=null)
                                        txtCHLDT.setText(M_fmtLCDAT.format(LD_CHLDT));

                                txtCHLQT.setText(strCHLQT);
				lblGRSWT.setText(strGRSWT);
				lblTARWT.setText(strTARWT);
				lblNETWT.setText(strNETWT);
				if(strGINTP.equals(strDESPT) || strGINTP.equals(strOTHER)){	// Despatch/Others
                                      if(L_tmsINCTM !=null)
                                           lblTARDT.setText(M_fmtLCDTM.format(L_tmsINCTM));
					if(L_tmsOUTTM !=null)
					lblGRSDT.setText(M_fmtLCDTM.format(L_tmsOUTTM));
				}
				else if(strGINTP.equals(strRAWMT)){		// Raw Material
		///			cmbMATTP.setSelectedIndex(Integer.parseInt(strMATTP) - 1);
					if(L_tmsINCTM !=null)
						lblGRSDT.setText(M_fmtLCDTM.format(L_tmsINCTM));
					if(L_tmsOUTTM !=null)
						lblTARDT.setText(M_fmtLCDTM.format(L_tmsOUTTM));
				}
				else{									// Stores & Spares
					if(L_tmsINCTM !=null)
					lblGRSDT.setText(M_fmtLCDTM.format(L_tmsINCTM));
					if(L_tmsOUTTM !=null)
					lblTARDT.setText(M_fmtLCDTM.format(L_tmsOUTTM));
				}
				txtMATDS.setText(strMATDS);
				if(strGINTP.equals(strRAWMT))
					vldMATTF(strMATCD);

                        	txtORDRF.setText(strORDRF);
				txtPRTCD.setText(strPRTCD);
				txtPRTDS.setText(strPRTDS);
				txtBOENO.setText(strBOENO);

				txtTNKNO.setText(strTNKNO);
                                if(L_tmsGINDT !=null)
                                         txtGINDT.setText(M_fmtLCDTM.format(L_tmsGINDT));
				txtLOCCD.setText(strLOCCD);
				vldLOCCD(strLOCCD);
				txtREMWB.setText(strREMWB);
				if(strGINTP.equals(strRAWMT) || strGINTP.equals(strRECPT)|| strGINTP.equals(strSLSRT)){		// Raw Material/Receipt
					lblGWTBY.setText(strWINBY);
					lblTWTBY.setText(strWOTBY);
				}
				else{													// Despatch
					lblGWTBY.setText(strWOTBY);
					lblTWTBY.setText(strWINBY);
				}

				txtGVTVL.setText(strGVTVL);
				M_rstRSSET.close();	
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else
				setMSG("No record found",'E');
		}catch(Exception e){
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			System.out.println("Error in exeGETREC : " + e.toString());
		}
	}

	
	// Method to add the record in the database
	private boolean exeADDREC(String LP_SRLNO){
		try{
			if(btnWB1.isSelected())
				strWBRNO = "1";
			else
				strWBRNO = "2";
			
			strLRYNO = txtLRYNO.getText().trim().toUpperCase();
			strTPRCD = txtTPRCD.getText().trim();
			strTPRDS = txtTPRDS.getText().trim();
			strDRVCD = txtDRVCD.getText().trim();
			strDRVNM = txtDRVNM.getText().trim();
			strMATCD = txtMATCD.getText().trim();
			strMATDS = txtMATDS.getText().trim();
			if(strGINTP.equals(strRAWMT))			// Raw Material 
				strMATTP = String.valueOf(cmbMATTP.getSelectedItem()).substring(0,2);
			else
				strMATTP = "";
			strCHLNO = txtCHLNO.getText().trim();
			if(txtCHLDT.getText().length() >0)
				strCHLDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHLDT.getText().trim()))+"'";
			else
				strCHLDT = "null";
			strCHLQT = String.valueOf(nvlNUMDB(txtCHLQT.getText().trim()));
			strGRSWT = String.valueOf(nvlNUMDB(lblGRSWT.getText().trim()));
			strTARWT = String.valueOf(nvlNUMDB(lblTARWT.getText().trim()));
			strNETWT = String.valueOf(nvlNUMDB(lblNETWT.getText().trim()));
			strBOENO = "";
			strTNKNO = "";
			strGVTVL = nvlSTRVL(txtGVTVL.getText().trim(),"0");
			strUOMQT = strNETWT;
			
			if(strGINTP.equals(strRAWMT)){		// Raw Material
				
				if(lblGRSDT.getText().trim().length() >0)	
					strINCTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(lblGRSDT.getText().trim()))+"'";		
				else
					strINCTM = "null";
				if(lblTARDT.getText().trim().length() >0)	
					strOUTTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(lblTARDT.getText().trim()))+"'";		
				else 
					strOUTTM = "null";
				strWINBY = lblGWTBY.getText().trim();
				strWOTBY = lblTWTBY.getText().trim();
				strBOENO = txtBOENO.getText().trim();
				strTNKNO = txtTNKNO.getText().trim();			
				strUOMCD = txtUOMCD.getText().trim();
				if(chrWBOPT == 'O' || strSTSFL.equals(strSTSOT)){ // Out Entry
					if(strUOMCD.equals("KL")){
						strUOMQT = setNumberFormat(Double.parseDouble(strNETWT)/Double.parseDouble(strGVTVL),3);
						// changed on 29/10/2004 as setFMT is deprecated
						//strUOMQT = setFMT(new BigDecimal(Double.parseDouble(strNETWT)/Double.parseDouble(strGVTVL)).toString(),4);
					}
				}
			}
			else if(strGINTP.equals(strRECPT)||strGINTP.equals(strSLSRT)){					// Receipt
				if(lblGRSDT.getText().trim().length() >0)
					strINCTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(lblGRSDT.getText().trim()))+"'";		
				else
					strINCTM = "null";
				if(lblTARDT.getText().trim().length() >0)
					strOUTTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(lblTARDT.getText().trim()))+"'";		
				else
					strOUTTM = "null";
				strWINBY = lblGWTBY.getText().trim();
				strWOTBY = lblTWTBY.getText().trim();			
			}
			else if(strGINTP.equals(strDESPT) || strGINTP.equals(strOTHER)){ // Despatch / Other
				if(lblTARDT.getText().trim().length() >0)
					strINCTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(lblTARDT.getText().trim()))+"'";		
				else
					strINCTM = "null";
				if(lblGRSDT.getText().trim().length() >0)
					strOUTTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(lblGRSDT.getText().trim()))+"'";		
				else
					strOUTTM = "null";

				strWINBY = lblTWTBY.getText().trim();
				strWOTBY = lblGWTBY.getText().trim();
			}
			
			strORDRF = txtORDRF.getText().trim();
			strLOCCD = txtLOCCD.getText().trim();
			strLOCDS = txtLOCNM.getText().trim();
			strREMWB = txtREMWB.getText().trim();
			strPRTCD = txtPRTCD.getText().trim();
			strPRTDS = txtPRTDS.getText().trim();			
			strLUSBY = cl_dat.M_strUSRCD_pbst.trim();
			strLUPDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()))+"'";
			if(txtGINDT.getText().trim().length() >0)
				strGINDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtGINDT.getText().trim()))+"'";
			else
				strGINDT = "null";
			strGINNO =  txtGINNO.getText().trim();
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			
			if(!LM_PRESENT){					// No entry at Gate
				// To update the Driver Record (MM_DVMST Table)
				updTRIPS(strTPRCD,strLRYNO,1);
				strSRLNO = "1";
				strSTSFL = strSTSIN;
				
				if(strGINTP.equals(strRAWMT) || strGINTP.equals(strRECPT)|| strGINTP.equals(strSLSRT))		// Raw Material/Receipt
					addREC(strSRLNO,strGRSWT,"0.000",strINCTM,strWINBY,strSTSFL);
				else if(strGINTP.equals(strDESPT) || strGINTP.equals(strOTHER)) // Despatch / Other
					addREC(strSRLNO,"0.000",strTARWT,strINCTM,strWINBY,strSTSFL);	 
				
				// To save the Last Gate-In No.in CO_CDTRN table
			///	if(cl_dat.M_LCLUPD)
					exeGINNO(strGINTP,strGINNO,strLUSBY,strLUPDT);
			}
			else{
				if(chrWBOPT == 'I') 
					updTRIPS(strTPRCD,strLRYNO,1);	// To update the Lorry Record (MM_LRMST Table)
				
				if(chrWBOPT == 'I')			// Weigh-In
					strSTSFL = strSTSIN;
				else if(chrWBOPT == 'O'){			// Weigh-Out
					strSTSFL = strSTSOT;
					if(strGINTP.equals(strOTHER)){			//	Other
						int L_ACTION = JOptionPane.showConfirmDialog(this,"Do you want to note this as a Tare Weight","Re-Entry",JOptionPane.YES_NO_OPTION); 
						if(JOptionPane.showConfirmDialog(this,"Do you want to note this as a Tare Weight","Re-Entry",JOptionPane.YES_NO_OPTION) == 0){
							strSRLNO = String.valueOf(Integer.parseInt(LP_SRLNO) + 1);
							addREC(strSRLNO,"0.000",strGRSWT,strOUTTM,strWOTBY,strSTSIN);
							strSTSFL = "N";
						}
					}	
				}
				// Updating the record in the database
				M_strSQLQRY = "Update MM_WBTRN set  ";
				M_strSQLQRY += "WB_ORDRF = '" + strORDRF + "',";
				M_strSQLQRY += "WB_LRYNO = '" + strLRYNO + "',";
				M_strSQLQRY += "WB_DRVCD = '" + strDRVCD + "',";
				M_strSQLQRY += "WB_DRVNM = '" + strDRVNM + "',";
				M_strSQLQRY += "WB_TPRCD = '" + strTPRCD + "',";
				M_strSQLQRY += "WB_TPRDS = '" + strTPRDS + "',";
				M_strSQLQRY += "WB_MATCD = '" + strMATCD + "',";
				M_strSQLQRY += "WB_MATDS = '" + strMATDS + "',";
				M_strSQLQRY += "WB_MATTP = '" + strMATTP + "',";				
				M_strSQLQRY += "WB_CHLNO = '" + strCHLNO + "',";
				M_strSQLQRY += "WB_CHLDT = " + strCHLDT + ",";
				M_strSQLQRY += "WB_CHLQT = " + strCHLQT + ",";
				M_strSQLQRY += "WB_WBRNO = '" + strWBRNO + "',";
				M_strSQLQRY += "WB_GRSWT = " + strGRSWT + ",";
				M_strSQLQRY += "WB_TARWT = " + strTARWT + ",";
				M_strSQLQRY += "WB_NETWT = " + strNETWT + ",";
				M_strSQLQRY += "WB_INCTM = " + strINCTM + ",";
				M_strSQLQRY += "WB_OUTTM = " + strOUTTM + ",";
				M_strSQLQRY += "WB_LOCCD = '" + strLOCCD + "',";
				M_strSQLQRY += "WB_REMWB = '" + strREMWB + "',";
				M_strSQLQRY += "WB_STSFL = '" + strSTSFL + "',";
				M_strSQLQRY += "WB_TRNFL = '" + strTRNFL + "',";
				M_strSQLQRY += "WB_WINBY = '" + strWINBY + "',";
				M_strSQLQRY += "WB_WOTBY = '" + strWOTBY + "',";
				M_strSQLQRY += "WB_LUSBY = '" + strLUSBY + "',";
				M_strSQLQRY += "WB_LUPDT = " + strLUPDT + ",";
				M_strSQLQRY += "WB_PRTCD = '" + strPRTCD + "',";
				M_strSQLQRY += "WB_PRTDS = '" + strPRTDS + "',";
				M_strSQLQRY += "WB_BOENO = '" + strBOENO + "',";
				M_strSQLQRY += "WB_TNKNO = '" + strTNKNO + "',";
				M_strSQLQRY += "WB_GVTVL = " + strGVTVL + ",";
				M_strSQLQRY += "WB_UOMQT = " + strUOMQT;
			    if(rdoBSYES.isSelected())
			        M_strSQLQRY += ",WB_BSCHK = 'Y'";
			    else if(rdoBSNO.isSelected())
			        M_strSQLQRY += ",WB_BSCHK = 'N'";
			    if(rdoERYES.isSelected())
			        M_strSQLQRY += ",WB_ERCHK = 'Y'";
			    else if(rdoERNO.isSelected())
			        M_strSQLQRY += ",WB_ERCHK = 'N'";
			    if(rdoLOYES.isSelected())
			        M_strSQLQRY += ",WB_LOCHK = 'Y'";
			    else if(rdoLONO.isSelected())
			        M_strSQLQRY += ",WB_LOCHK = 'N'";
			    if(rdoEPYES.isSelected())
			        M_strSQLQRY += ",WB_EPCHK = 'Y'";
			    else if(rdoEPNO.isSelected())
			        M_strSQLQRY += ",WB_EPCHK = 'N'";
			    if((txtTFIDT.getText().length() >0)&&(txtTFITM.getText().length() >0))        
			        M_strSQLQRY += ",WB_TFITM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTFIDT.getText().trim()+" "+txtTFITM.getText().trim()))+"'";		
			     if((txtTFODT.getText().length() >0)&&(txtTFOTM.getText().length() >0))        
			        M_strSQLQRY += ",WB_TFOTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTFODT.getText().trim()+" "+txtTFOTM.getText().trim()))+"'";		;
			    if((txtULSDT.getText().length() >0)&&(txtULSTM.getText().length() >0))        
			        M_strSQLQRY += ",WB_ULSTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtULSDT.getText().trim()+" "+txtULSTM.getText().trim()))+"'";		;
			    if((txtULEDT.getText().length() >0)&&(txtULETM.getText().length() >0))        
			        M_strSQLQRY += ",WB_ULETM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtULSDT.getText().trim()+" "+txtULETM.getText().trim()))+"'";		;
			    if(txtULDBY.getText().length() >0)    
			         M_strSQLQRY += ",WB_ULDBY = '"+txtULDBY.getText().trim()+"' ";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
				M_strSQLQRY += " and WB_DOCNO = '" + strGINNO + "'";
				M_strSQLQRY += " and WB_SRLNO = '" + LP_SRLNO + "'";
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)){
					if(strGINTP.equals(strOTHER) && !strSRLNO.equals("1")){			//	Other
						strSRLNO = String.valueOf(Integer.parseInt(LP_SRLNO) - 1);
						strGRSWT = strTARWT;
						strOUTTM = strINCTM;						
						strWOTBY = strWINBY;
						updREC();
					}
				}
			}	
			if(cl_dat.exeDBCMT(" "))
			{
				setMSG("Record saved successfully",'N');
				return true;
			}
			else
			{
				setMSG("Record could not be saved",'E');
				return false;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"exeADDREC");
			return false;
		}
	}
	
	// Method to establish the connection with the Comm Port
	public boolean commPORT(){
		Enumeration en;
                System.out.println(CommPortIdentifier.getPortIdentifiers().hasMoreElements());
		if(CommPortIdentifier.getPortIdentifiers().hasMoreElements())
		{
		}
		try
		{
    		portList = CommPortIdentifier.getPortIdentifiers();
    		System.out.println("Prot List" +portList);
    		
    		while(portList.hasMoreElements()){
    			portId = (CommPortIdentifier)portList.nextElement();
    			System.out.println("portId" +portId);
    			System.out.println("portId.getPortType()" +portId.getPortType());
    			if(portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
    			{
                     System.out.println("PortId .getName" +portId.getName());
    				if(portId.getName().equals(strPOTID)){
    					try{
    						serialPort = (SerialPort) portId.open(strPOTID,2000);
    						System.out.println("serial Port" +serialPort);
    					}catch(PortInUseException e){
    						System.out.println("Error 1 : " + e.toString());
                                                    setCursor(cl_dat.M_curDFSTS_pbst);
    						return false;
    					}
    					
    					try {
    					    inputStream = serialPort.getInputStream();
    					    dosREPORT.writeBytes("inputStream" +inputStream +"\n");
    					}
    					catch(IOException e)
    					{
    						System.out.println("Error 2 : " + e.toString());
                                                    setCursor(cl_dat.M_curDFSTS_pbst);
                                                    serialPort.close();
    						return false;
    					}
    			
    					try{ 
    					     serialPort.addEventListener(this);
    					}
    					catch(TooManyListenersException e)
    					{
    						System.out.println("Error 3 : " + e.toString());
                                                    setCursor(cl_dat.M_curDFSTS_pbst);
                                                    serialPort.close();
    						return false;
    					}
    
    					try{
    						int L_BUDRT,L_DATBT;
    						setPORT();
    						L_BUDRT = Integer.parseInt(hstPORT.get("01").toString());
    						System.out.println("LBUDRT" +L_BUDRT);
    						L_DATBT = Integer.parseInt(hstPORT.get("02").toString());
    						System.out.println("L_DATBT"+L_DATBT);
    						serialPort.setSerialPortParams(L_BUDRT,L_DATBT,SerialPort.STOPBITS_1,SerialPort.PARITY_EVEN);
                                            serialPort.enableReceiveThreshold(8);
                                            serialPort.enableReceiveTimeout(100);
    
    					}
    					catch (UnsupportedCommOperationException e)
    					{
    						System.out.println("Error 4 : " + e.toString());
    						System.out.println("UnsupportedCommOperationException fired");
                                                    setCursor(cl_dat.M_curDFSTS_pbst);
    						serialPort.close(); 
    						return false;
    					}
    					strCOMSTR1 = "";
                		
                		//if(serialPort != null)
                		//	serialPort.close(); 
    					serialPort.notifyOnDataAvailable(true);
                                             setCursor(cl_dat.M_curDFSTS_pbst);
    					return true;
    				}
    			}
    		}
    		if(dosREPORT !=null)
    			dosREPORT.close();
    		if(fosREPORT !=null)
    			fosREPORT.close();
    		
    		if(serialPort != null)
    			serialPort.close(); 
    	}
		catch(Exception L_EX)
		{
			setMSG(L_EX," comPORT ");
		}	
		return false;
	}
	
	public void serialEvent(SerialPortEvent event){
		switch(event.getEventType()){
			case SerialPortEvent.BI:
				break;
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
 			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:            
				break;
			case SerialPortEvent.DATA_AVAILABLE:
			//actually original is  byte [] readBuffer = new byte[8]; is there
			//modified is  byte[] readBuffer=new byte[0]; and then readBuffer = new byte[8];
			byte[] readBuffer=new byte[0]; //modified 18-05-2007 intially reader buffer is 0 bytes
				 readBuffer = new byte[8];
				try{
				    
				    dosREPORT.writeBytes("inputStream.available()" +inputStream.available() +"\n");
				    inputStream = serialPort.getInputStream();
				    dosREPORT.writeBytes("inputStream" +inputStream +"\n");
				    // modiied 18-05-2007 this is actually while loop i.e (while(inputStream.available() >= 0)) is there, changed the 1f condition
				    //if(inputStream.available() >= 8)
					if(inputStream.available() >= 8)
					{
					    int numBytes=0;//modified is 18-05-2007 
					    numBytes = inputStream.read(readBuffer);
					    dosREPORT.writeBytes(" in while loop inputStream.available()" +inputStream.available() +"\n");
					    // numBytes = inputStream.read(readBuffer);
					   
					   //dosREPORT.writeBytes(" Bytes" +numBytes +"\n");
					}
					
					strCOMSTR= "";
					strCOMSTR = new String(readBuffer);
					dosREPORT.writeBytes("After strCOMSTR=new string buffer" +strCOMSTR + "\n");
					
					int L_INDEX = strCOMSTR.indexOf(41);	//ascii char of ')'
					
					//dosREPORT.writeBytes("strCOMSTR : "+strCOMSTR+"\n");
					//dosREPORT.writeBytes("strCOMSTR1 : "+strCOMSTR1+"\n");
					
					
                                        if(L_INDEX != -1)// ')' is found
                                        {   
                                                if(strCOMSTR1.trim().length() >= 6)
                                                {
                                                  // Final Formatted string
                                                   //System.out.println("F : "+strCOMSTR1.trim().substring(0,6));
                                                   txtLRYWT.setText("");
                                                   txtLRYWT.setText(setNumberFormat(Double.parseDouble(strCOMSTR1.trim().substring(0,6))/LM_CNVFCT,3));
                                                   dosREPORT.writeBytes("Weight : "+txtLRYWT.getText()+"\n\n");
                                                   setCursor(cl_dat.M_curDFSTS_pbst);
                                                }
                        
                                                if(strCOMSTR.length() > L_INDEX + 3)
                                                {
                                                    strCOMSTR1 = strCOMSTR.substring(L_INDEX + 3).trim();
							
                                                 }
					}
                                        else{ 
                                            
                                                if(strCOMSTR1.trim().length() != 0)
                                                {
                                                    strCOMSTR1 = strCOMSTR1.trim()+strCOMSTR.trim();
                         
                         
                                                }
						else
                                                {
                                            //          dosREPORT.writeBytes("')' not found : " + strCOMSTR +"\n");
						}
                                        }
                                }catch (Exception e)
                                {
                                        System.out.println("Error in IOException : " + e.toString());
                                        setCursor(cl_dat.M_curDFSTS_pbst);
				}
				break;        
		}    
	}
	
	// Method to update the trips in Lorry Master
	private boolean updTRIPS(String LP_TPRCD,String LP_LRYNO,int LP_TRPCT){
		try{
			int LM_TRPCT = 0;
		
			M_strSQLQRY = " Select LR_TRPCT from MM_LRMST ";
			M_strSQLQRY += " where LR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LR_TPRCD = '" + LP_TPRCD + "'";
			M_strSQLQRY += " and LR_LRYNO = '" + LP_LRYNO + "'";
			M_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);
			
			if(M_rstRSSET1.next()){			// If record already exists in MM_LRMST
				LM_TRPCT = M_rstRSSET1.getInt("LR_TRPCT");
				LM_TRPCT += LP_TRPCT;
				M_strSQLQRY = "Update MM_LRMST set  ";
				M_strSQLQRY += "LR_TRPCT = " + LM_TRPCT + ",";
				M_strSQLQRY += "LR_TRNFL = '" + strTRNFL + "',";
				M_strSQLQRY += "LR_LUSBY = '" + strLUSBY + "',";
				M_strSQLQRY += "LR_LUPDT = " + strLUPDT;
				M_strSQLQRY += " where LR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LR_TPRCD = '" + LP_TPRCD + "'";
				M_strSQLQRY += " and LR_LRYNO = '" + LP_LRYNO + "'";
			}	
			else
			{	// New Record in Lorry master
				// Inserting to lorry master for tankers blocked on 12/01/04, due to bill 
				// passing problems b'coz of duplicate lorry entries in lorry master.
				if(!strGINTP.equals(strRAWMT)) 
				{
					M_strSQLQRY = "Insert into MM_LRMST(LR_CMPCD,LR_TPRCD,LR_LRYNO,LR_LRYDS,";
					M_strSQLQRY += "LR_TRPCT,LR_STSFL,LR_LUSBY,";
					M_strSQLQRY += "LR_LUPDT) values (";
					M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
					M_strSQLQRY += "'" + LP_TPRCD + "',";
					M_strSQLQRY += "'" + LP_LRYNO + "',";
					M_strSQLQRY += "'" + LP_LRYNO + "',";
					M_strSQLQRY += LP_TRPCT + ",";
					M_strSQLQRY += "'R',";
					M_strSQLQRY += "'" + strLUSBY + "',";
					M_strSQLQRY += strLUPDT + ")";
				}
				else
					return true;
			}
			
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT(" "))
			{
				return true;
			}
			else
				return false;
		}catch(Exception e){
			setMSG("Error in updTRIPS" ,'E');
		}
		return true;
	}
																		 

	// Method to create Combo Box by Adding Values from DataBase
	private void getCMBDT(String P_CMBTP){
		String L_strVALUE;
		
		try{
			if(P_CMBTP.equals("GINTP"))
			{
				cmbGINTP.removeActionListener(this);
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'";
				M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			}
			else if(P_CMBTP.equals("MATTP"))
			{
				cmbMATTP.removeActionListener(this);
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
				M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			}
			M_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY );
			while(M_rstRSSET1.next()){
				L_strVALUE = M_rstRSSET1.getString("CMT_CODCD") + " " + M_rstRSSET1.getString("CMT_CODDS");
				if(P_CMBTP.equals("GINTP"))	
					cmbGINTP.addItem(L_strVALUE);
				else
					cmbMATTP.addItem(L_strVALUE);
			}
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();
			else if(P_CMBTP.equals("GINTP"))
				cmbGINTP.addActionListener(this);
			else if(P_CMBTP.equals("MATTP"))
				cmbMATTP.addActionListener(this);
		}catch(Exception e){
			System.out.println("Error in getCMBDT : " + e.toString());
		}
	}
	
	// Method to get the User Name from the MSTDATA/CO_USMST
	private String getUSRNM(String LP_USRCD){
		String L_USRNM = LP_USRCD;
		try{
			M_strSQLQRY = "Select US_USRNM from CO_USMST";
			M_strSQLQRY += " where US_USRCD = '" + LP_USRCD + "'";
		
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY );
		
			if(M_rstRSSET1.next())
				L_USRNM = M_rstRSSET1.getString("US_USRNM");
		
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();
		}catch(Exception e){
			System.out.println("Error in getUSRNM : " + e.toString());
		}
		return L_USRNM;
	}
	
	// Method to generate the new Gate-In No. depending upon the Gate-In type
	private String genGINNO(String LP_GINTP){
		String L_GINNO  = "",  L_CODCD = "", L_CCSVL = "";
			
		try{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXWBT' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + LP_GINTP + "'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null){
				if(M_rstRSSET.next()){
					L_CODCD = M_rstRSSET.getString("CMT_CODCD");
					L_CCSVL = M_rstRSSET.getString("CMT_CCSVL");
					M_rstRSSET.close();
				}
			}
			
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
			
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_GINNO += "0";
			
			L_CCSVL = L_GINNO + L_CCSVL;
			L_GINNO = L_CODCD + L_CCSVL;
			txtGINNO.setText(L_GINNO);
		}catch(Exception L_EX){
			setMSG(L_EX,"genGINNO");
		}
		return L_GINNO;
	}
	
	// Method to update the last Gate-In No.in the CO_CDTRN
	private void exeGINNO(String LP_GINTP,String LP_GINNO,String LP_LUSBY,String LP_LUPDT){
		try{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += "CMT_CCSVL = '" + LP_GINNO.substring(3) + "',"; 
			M_strSQLQRY += "CMT_TRNFL = '" + strTRNFL + "',";
			M_strSQLQRY += "CMT_LUSBY = '" + LP_LUSBY + "',";
			M_strSQLQRY += "CMT_LUPDT = " + LP_LUPDT;
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MMXXWBT'";
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + LP_GINTP + "'";
			
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}catch(Exception e){
			System.out.println("Error in exeGINNO : " + e.toString());
		}
	}
	
	// Method to get the Party Description from Party Master
	private String getPRTDS(String LP_PRTTP,String LP_PRTCD){
		String L_PRTDS = "";
		
		LP_PRTCD = LP_PRTCD.trim();
		
		try{
			M_strSQLQRY = "Select PT_PRTNM from CO_PTMST ";
			M_strSQLQRY += " where PT_PRTTP = '" + LP_PRTTP + "'";
			M_strSQLQRY += " and PT_PRTCD = '" + LP_PRTCD + "'";
			
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY );
			
			if(M_rstRSSET1.next()){
				L_PRTDS = M_rstRSSET1.getString("PT_PRTNM");
				M_rstRSSET1.close();
			}
		}catch(Exception e){
			System.out.println("Error in getPRTDS : " + e.toString());
		}
		return L_PRTDS;
	}
	
	// Help on Raw Material (Tankfarm) 
	private void hlpMATTF(){
		try{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtMATCD";
			
			String L_ARRHDR[] = {"Code","Material","UOM"};
			M_strSQLQRY = "Select Distinct TK_MATCD,TK_MATDS,CT_UOMCD from MM_TKMST,CO_CTMST";
			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_MATCD = CT_MATCD";
			M_strSQLQRY += " order by TK_MATDS desc";
						
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,3,"CT");
		}catch(Exception L_EX){
			setMSG(L_EX,"hlpMATTF");
		}	
	}

	// Validation of Raw Material (Tankfarm) 
	private boolean vldMATTF(String LP_MATCD){
		try{
			M_strSQLQRY = "select TK_MATCD,TK_MATDS,CT_UOMCD from MM_TKMST,CO_CTMST";
			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_MATCD = '" + LP_MATCD + "'";
			M_strSQLQRY += " and ltrim(str(TK_MATCD,20,0)) = CT_MATCD";
            System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET.next()){
				txtMATDS.setText(M_rstRSSET.getString("TK_MATDS"));
				txtUOMCD.setText(M_rstRSSET.getString("CT_UOMCD"));
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			txtMATDS.setText("");
			txtUOMCD.setText("");
			txtMATCD.requestFocus();
			setMSG("Invalid Material. Press F1 for help",'E');
				
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"vldMATTF");
			return false;
		}	
		return false;
	}
	
	// Help on Consignee / Customer / Banker
	private void hlpPRTCD(){
		try{
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtPRTCD";
			String L_ARRHDR[] = {"Code","Party"};
			
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
			
			if(strGINTP.equals(strDESPT))
				M_strSQLQRY += " where PT_PRTTP = 'C'";
            else if(strGINTP.equals(strSLSRT))
				M_strSQLQRY += " where PT_PRTTP = 'C'";
			else
				M_strSQLQRY += " where PT_PRTTP = 'S'";
				
			M_strSQLQRY += " and PT_STSFL <> 'X'";
			M_strSQLQRY += " order by PT_PRTCD";
		
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
		}catch(Exception e){
			System.out.println("Error in hlpPRTCD : " + e.toString());
		}
	}
	
	// Check valid Party Code
	private boolean vldPRTCD(String LP_PRTCD){
		try{
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
			if(strGINTP.equals(strDESPT))
				M_strSQLQRY += " where PT_PRTTP = 'C'";
			else if(strGINTP.equals(strSLSRT))	
			    M_strSQLQRY += " where PT_PRTTP = 'C'";
			else
				M_strSQLQRY += " where PT_PRTTP = 'S'";
				
			M_strSQLQRY += " and PT_STSFL <> 'X'";
			M_strSQLQRY += " and PT_PRTCD = '" + LP_PRTCD + "'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
             if(M_rstRSSET !=null)
			if(M_rstRSSET.next()){
				txtPRTDS.setText(M_rstRSSET.getString("PT_PRTNM"));
				M_rstRSSET.close();
				cl_dat.M_btnSAVE_pbst.requestFocus();
				return true;			
			}
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			setMSG("Invalid " + lblPRTNM.getText() + ".Press F1 for help.",'E');
			txtPRTCD.requestFocus();
			txtPRTDS.setText("");
			return false;			
		}catch(Exception e){
			System.out.println("Error in vldPRTCD : " + e.toString());
			return false;
		}
	}
	
	// To update the record in the MM_WBTRN
	private void updREC(){
		try{
			M_strSQLQRY = "Update MM_WBTRN set  ";
			M_strSQLQRY += "WB_ORDRF = '" + strORDRF + "',";
			M_strSQLQRY += "WB_LRYNO = '" + strLRYNO + "',";
			M_strSQLQRY += "WB_DRVCD = '" + strDRVCD + "',";
			M_strSQLQRY += "WB_DRVNM = '" + strDRVNM + "',";
			M_strSQLQRY += "WB_TPRCD = '" + strTPRCD + "',";
			M_strSQLQRY += "WB_TPRDS = '" + strTPRDS + "',";
			M_strSQLQRY += "WB_MATCD = '" + strMATCD + "',";
			M_strSQLQRY += "WB_MATDS = '" + strMATDS + "',";
			M_strSQLQRY += "WB_MATTP = '" + strMATTP + "',";				
			M_strSQLQRY += "WB_CHLNO = '" + strCHLNO + "',";
			M_strSQLQRY += "WB_CHLDT = " + strCHLDT + ",";
			M_strSQLQRY += "WB_CHLQT = " + strCHLQT + ",";
			M_strSQLQRY += "WB_WBRNO = '" + strWBRNO + "',";
			M_strSQLQRY += "WB_GRSWT = " + strGRSWT + ",";
			M_strSQLQRY += "WB_NETWT = " + strNETWT + ",";
			M_strSQLQRY += "WB_OUTTM = " + strOUTTM + ",";
			M_strSQLQRY += "WB_LOCCD = '" + strLOCCD + "',";
			M_strSQLQRY += "WB_REMWB = '" + strREMWB + "',";
			M_strSQLQRY += "WB_WINBY = '" + strWINBY + "',";
			M_strSQLQRY += "WB_TRNFL = '" + strTRNFL + "',";
			M_strSQLQRY += "WB_LUSBY = '" + strLUSBY + "',";
			M_strSQLQRY += "WB_LUPDT = " + strLUPDT + ",";
			M_strSQLQRY += "WB_PRTCD = '" + strPRTCD + "',";
			M_strSQLQRY += "WB_PRTDS = '" + strPRTDS + "',";
			M_strSQLQRY += "WB_BOENO = '" + strBOENO + "',";
			M_strSQLQRY += "WB_TNKNO = '" + strTNKNO + "'";
			M_strSQLQRY += "WB_GVTVL = '" + strGVTVL + "',";
			M_strSQLQRY += "WB_UOMQT = '" + strUOMQT + "'";
		    if(rdoBSYES.isSelected())
		        M_strSQLQRY += ",WB_BSCHK = 'Y'";
		    else if(rdoBSNO.isSelected())
		        M_strSQLQRY += ",WB_BSCHK = 'N'";
		    if(rdoERYES.isSelected())
		        M_strSQLQRY += ",WB_ERCHK = 'Y'";
		    else if(rdoERNO.isSelected())
		        M_strSQLQRY += ",WB_ERCHK = 'N'";
		    if(rdoLOYES.isSelected())
		        M_strSQLQRY += ",WB_LOCHK = 'Y'";
		    else if(rdoLONO.isSelected())
		        M_strSQLQRY += ",WB_LOCHK = 'N'";
		    if(rdoEPYES.isSelected())
		        M_strSQLQRY += ",WB_EPCHK = 'Y'";
		    else if(rdoEPNO.isSelected())
		        M_strSQLQRY += ",WB_EPCHK = 'N'";
		    if((txtTFIDT.getText().length() >0)&&(txtTFITM.getText().length() >0))        
		        M_strSQLQRY += ",WB_TFITM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTFIDT.getText().trim()+" "+txtTFITM.getText().trim()))+"'";		
		     if((txtTFODT.getText().length() >0)&&(txtTFOTM.getText().length() >0))        
		        M_strSQLQRY += ",WB_TFOTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTFODT.getText().trim()+" "+txtTFOTM.getText().trim()))+"'";		;
		    if((txtULSDT.getText().length() >0)&&(txtULSTM.getText().length() >0))        
		        M_strSQLQRY += ",WB_ULSTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtULSDT.getText().trim()+" "+txtULSTM.getText().trim()))+"'";		;
		    if((txtULEDT.getText().length() >0)&&(txtULETM.getText().length() >0))        
		        M_strSQLQRY += ",WB_ULETM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtULSDT.getText().trim()+" "+txtULETM.getText().trim()))+"'";		;
		    if(txtULDBY.getText().length() >0)    
		         M_strSQLQRY += ",WB_ULDBY = '"+txtULDBY.getText().trim()+"' ";

			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + strGINNO + "'";
			M_strSQLQRY += " and WB_SRLNO = '" + strSRLNO + "'";
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
		}catch(Exception e){
			System.out.println("Error in updREC : " + e.toString());
		}
	}
	
	// To insert the record in the MM_WBTRN
	private void addREC(String LP_SRLNO,String LP_GRSWT,String LP_TARWT,String LP_INCTM,String LP_WINBY,String LP_STSFL){
		try{
			//strGINDT = cc_dattm.setDBSTM(txtGINDT.getText().trim());
			if(txtGINDT.getText().trim().length() >0)
				strGINDT = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtGINDT.getText().trim()))+"'";
			else
				strGINDT = "null";
			M_strSQLQRY = "Insert into MM_WBTRN(WB_CMPCD,WB_DOCTP,WB_DOCNO,WB_SRLNO,";
			M_strSQLQRY += "WB_WBRNO,WB_LRYNO,WB_TPRCD,WB_TPRDS,WB_DRVCD,";
			M_strSQLQRY += "WB_DRVNM,WB_MATCD,WB_CHLNO,WB_CHLDT,WB_CHLQT,";
			M_strSQLQRY += "WB_GRSWT,WB_TARWT,WB_NETWT,WB_INCTM,WB_MATDS,";
			M_strSQLQRY += "WB_STSFL,WB_ORDRF,WB_MATTP,WB_LUSBY,WB_LUPDT,";
			M_strSQLQRY += "WB_GINDT,WB_LOCCD,WB_REMWB,WB_WINBY,WB_PRTCD,";
			M_strSQLQRY += "WB_PRTDS,WB_ACPTG,WB_BOENO,WB_TNKNO,WB_TRNFL) values (";
			M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
			M_strSQLQRY += "'" + strGINTP + "',";
			M_strSQLQRY += "'" + strGINNO + "',";
			M_strSQLQRY += "'" + LP_SRLNO + "',";				
			M_strSQLQRY += "'" + strWBRNO + "',";
			M_strSQLQRY += "'" + strLRYNO + "',";
			M_strSQLQRY += "'" + strTPRCD + "',";
			M_strSQLQRY += "'" + strTPRDS + "',";
			M_strSQLQRY += "'" + strDRVCD + "',";
			M_strSQLQRY += "'" + strDRVNM + "',";
			M_strSQLQRY += "'" + strMATCD + "',";
			M_strSQLQRY += "'" + strCHLNO + "',";
			M_strSQLQRY += strCHLDT + ",";
			M_strSQLQRY += strCHLQT + ",";
			M_strSQLQRY += LP_GRSWT + ",";
			M_strSQLQRY += LP_TARWT + ",";
			M_strSQLQRY += "0.000,";
			M_strSQLQRY += LP_INCTM + ",";
			M_strSQLQRY += "'" + strMATDS + "',";
			M_strSQLQRY += "'" + LP_STSFL + "',";
			M_strSQLQRY += "'" + strORDRF + "',";
			M_strSQLQRY += "'" + strMATTP + "',";
			M_strSQLQRY += "'" + strLUSBY + "',";
			M_strSQLQRY += strLUPDT + ",";
			M_strSQLQRY += strGINDT + ",";
			M_strSQLQRY += "'" + strLOCCD + "',";
			M_strSQLQRY += "'" + strREMWB + "',";
			M_strSQLQRY += "'" + LP_WINBY + "',";
			M_strSQLQRY += "'" + strPRTCD + "',";
			M_strSQLQRY += "'" + strPRTDS + "',";
			M_strSQLQRY += "'" + strACPTG + "',";
			M_strSQLQRY += "'" + strBOENO + "',";
			M_strSQLQRY += "'" + strTNKNO + "',";
			M_strSQLQRY += "'" + strTRNFL + "')";
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
		}catch(Exception e){
			System.out.println("Error in addREC : " + e.toString());
		}
	}
	
	// To get the Serial no. 
	private String getSRLNO(String LP_GINTP,String LP_GINNO){
		String L_SRLNO = "1";
		try{
			M_strSQLQRY = "select max(WB_SRLNO) strSRLNO from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + LP_GINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + LP_GINNO + "'";
            	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET.next())
				L_SRLNO = M_rstRSSET.getString("strSRLNO");

			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"vldMATTF");
		}	
		return L_SRLNO;
	}
	
	// Method to set the parameters of the commPort by taking the values from database
	private void setPORT(){
		try{
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'MMXXPRT'";
            	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(M_strSQLQRY);
			while(M_rstRSSET.next()){
				hstPORT.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
			}

			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"setPORT");
		}
	}
	// Get the available Tank Nos
	private void hlpTNKNO(String LP_MATCD){
		try{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtTNKNO";
			String L_ARRHDR[] = {"Tank No","Material"};
			M_strSQLQRY = "Select TK_TNKNO,TK_MATCD from MM_TKMST";
                        M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_STSFL <> 'X' ";
            M_strSQLQRY += " and TK_MATCD = '" + LP_MATCD + "'";
                          M_strSQLQRY += " order by tk_tnkno desc";
			
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
		}catch(Exception L_EX){
			setMSG(L_EX,"hlpTNKNO");
		}
	}
	
	// Check Tank No. for validity
	public boolean vldTNKNO(String LP_TNKNO,String LP_MATCD){	
		try{
			M_strSQLQRY = "Select TK_TNKNO,TK_MATCD from MM_TKMST";
			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_TNKNO = '" + LP_TNKNO + "'";
			M_strSQLQRY += " and TK_MATCD = '" + LP_MATCD + "'";
			M_strSQLQRY += " and TK_STSFL <> 'X'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				txtCHLNO.requestFocus();
				M_rstRSSET.close();
				return true;
			}
			setMSG("Invalid Tank No.Press F1 for help",'E');
		}catch(Exception L_EX){
			setMSG(L_EX,"vldTNKNO");
		}
		return false;
	}
	void exeSAVE()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				// Reminder for Re-Login
				if(strGINTP.equals(strRAWMT) || strGINTP.equals(strRECPT)|| strGINTP.equals(strSLSRT)){	// R.M./Receipt
					strINCTM = lblGRSDT.getText().trim();
					strOUTTM = lblTARDT.getText().trim();
				}
				else if(strGINTP.equals(strDESPT) || strGINTP.equals(strOTHER)){ // Despatch / Other
					strINCTM = lblTARDT.getText().trim();
					strOUTTM = lblGRSDT.getText().trim();
				}
				/*if(M_fmtLCDTM.parse(strOUTTM).compareTo(M_fmtLCDTM.parse(strINCTM))<0){
					JOptionPane.showMessageDialog(this,"Please,Log in again","Login",JOptionPane.INFORMATION_MESSAGE);
					//this.dispose();
					return;
				}*/
				if(vldDATA())
				{
					if(exeADDREC(strSRLNO))
					{
						if(strSTSFL.equals(strSTSIN))
							JOptionPane.showMessageDialog(this,"Please note down Gate-In No. " + strGINNO,"Gate-In No.",JOptionPane.INFORMATION_MESSAGE); 
						if(strSTSFL.equals(strSTSOT))
						{
							// Storing the recent values of Gate-In Type and Gate-In No.
							strPGINTP = strGINTP;
							strPGINNO = strGINNO;
							strPSRLNO = strSRLNO;
							btnPRINT.setEnabled(true);
						}
						clrSCR();
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
							txtGINNO.requestFocus();
						else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(chrWBOPT =='O'))
						{
							setENBL(false);
							btnSTRT.setEnabled(false);
							btnACPT.setEnabled(false);
							txtGINNO.requestFocus();
						}
						else if(chrWBOPT == 'I')
						{
							setENBL(true);
							cmbGINTP.setEnabled(true);			
							txtGINNO.setEnabled(true);						
							btnSTRT.setEnabled(true);
							btnACPT.setEnabled(false);
							
							strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
							if(strGINTP.equals(strRAWMT)){
								txtMATCD.setText(strSTYCD);
								vldMATTF(strSTYCD);
							}
							else if(strGINTP.equals(strDESPT))
								txtMATDS.setText("POLYSTYRENE");
							else if(strGINTP.equals(strOTHER))
								txtMATDS.setText("Scrap");	
							else
								txtMATDS.setText("");	
							
							txtGINNO.requestFocus();					
						}
						
					}
				}		
				this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	catch(Exception L_E)
	{
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		System.out.println(L_E.toString());
	}
	}
	
}
