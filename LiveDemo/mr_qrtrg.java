/*
System Name                 : Marketing System
Program Name                : Marketing Target Query
Program Desc.               : Marketing target,Order booking & Pnd.Bkg. details zonewise & regionwise.
Author						: Mr. Deepal N. Mehta
Date                        : 17th February 2003
Version						: MKT 1.0
Special functionality used	: fetching of data through hashtable

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class mr_qrtrg extends cl_rbase 
{
   

    private JButton btnQUERY,btnRUN;
	private JTabbedPane tabQRTRG;
    JPanel pnlSLTRG,pnlSTKAV,pnlSLT01,pnlSLT02,pnlSTA01,pnlSTA02,pnlGSTAV,pnlGST01,pnlMKK,pnlMAIN;
	 JTextField txtSPEFC,txtPRDTP;
	 TxtDate txtMMYDT;
	 JRadioButton rdbZONWS,rdbRGNWS,rdbALLCG,rdbDOMWS,rdbEXPWS,rdbALLWS,rdbSPECF,rdbDETAL,rdbSUMRY;
	 JCheckBox chkDEXWS,chkDOMWS;
     cl_JTable tblSTKAV,tblGSTAV;
     cl_JTable tblSLTRG;
     JLabel lblTBLHDR;
    JOptionPane LM_OPTNPN;
	ButtonGroup grpSLTRG,grpSTKAV,grpSELEC,grpREPOT;
	PreparedStatement prpCOMSTM;
	ResultSet prpRSLSET,LM_RSLSET;
	StringBuffer stbSTRQRY;
	String LM_STRQRY,strPKGTP,strPRDCD,strPRDDS,strZONCD,strSALTP,strCHPZN,strCHPSL,strMMYDT,strCHP02="";
	String strDBSMY,strMONTH,strYEAR,strCHP01="",strSPEFC,LM_HLPFLD,strMNGRP,strPRDTP,strRESNO,strPRTDS;
	String strTITLE1,strTITLE2,strTITLE3;
	
    String strPRDCD1 = "",strPRDCD2,strPRDDS1,strPRDDS2;
	String strMNGRP1 = "",strMNGRP2;
	String strSBGRP1 = "",strSBGRP = "",strSBGRP2 = "";
	String strSTSFL1 = "",strSTSFL = "",strSTSFL2 = "";
	String strPKGTP1 = "",strPKGTP2 = "";
  //  String LM_RESFIN = cl_dat.M_REPSTR;
	String LM_RESSTR = ""; 
	String strFILNM;
    String strHDR_PRDDS = "Grade";
    String strHDR_TRGQT = "Target Qty.";
    String strHDR_DSPQT = "Despatch Qty.";
	String strHDR_PNDQT = "Bkd:pnd.toGO";
	String strHDR_BALQT = "Bal. T Book";
    String strHDR_EXPTG = "Trg. Export";
    String strHDR_TOTQT = "Target Total";
    String strHDR_DOOQT = "Ord. Dom.";
    String strHDR_DEOQT = "Ord. D. Export";
    String strHDR_EXOQT = "Ord. Export";
    String strHDR_TOOQT = "Order Total";
    String strHDR_DOPQT = "Dom.  P/toGo";
    String strHDR_DEPQT = "D.Exp P/toGo";
    String strHDR_EXPQT = "Exp.  P/toGo";
    String strHDR_TOPQT = "Total P/toGo";
    String strHDR_PDOQT = "Ord.  P/toGo";
    String strHDR_CLSQT = "Clsfd. Qty.";
    String strHDR_NCLQT = "Unclsfd. Qty.";
    String strHDR_RESQT = "Reserved Qty.";
    String strHDR_BLSQT = "Closing Stock";
	String strHDR_DOMTG = "Trg. Dom.";
	String strHDR_DEXTG = "Trg. D.Exp";
	String strHDR_DOMDS = "Dsp. Dom.";
	String strHDR_DEXDS = "Dsp. D.Exp";
	String strHDR_EXPDS = "Dsp. Export";
	String strHDR_DOMPD = "Dom.  pnd.toGO";
	String strHDR_DEXPD = "D.Exp pnd.toGO";
	String strHDR_EXPPD = "Exp   pnd.toGO";
	String strHDR_DOMBL = "Dom.  Bal/ToBk";
	String strHDR_DEXBL = "D.Exp Bal/ToBk";
	String strHDR_EXPBL = "Exp   Bal/ToBk";
	String strHDR_PRTQT = "Prd. Target";
	String strHDR_PRDQT = "Prd. Qty.";
	String strHDR_TPRQT = "Total Prd.";
	String strHDR_TOTST = "Total Stock";
	String strHDR_STADO = "Stock Available";
	
	int intTBL_PRDDS = 0;
	int intTBL_TRGQT = 1;
    int intTBL_ORDQT = 2;
	int intTBL_PDOQT = 3;
    int intTBL_CLSQT = 4;
    int intTBL_NCLQT = 5;
    int intTBL_RESQT = 6;
    int intTBL_BALQT = 7;
	
	int intROWCNT = 0;
	int intLINENO = 0;
	int intLINLEN = 0;
	
	int intSLTLEN = 20;  //length of sale type column in reports
	int intSTKLEN = 20;  //length of stock column in reports
	int intCLMLEN = 20;  //length of column inside sale type in reports
	int intTOTLEN = 20;  //length of total column inside sale type in reports
	int LM_GRDLEN = 20;  //length of grade column
	
	int intTOTQT,intDSPQT,intTOPQT,intCLSQT,intNCLQT,intTRGQT;
	int intDEOQT,intDOOQT,intEXOQT,intDOTQT,intDODQT,intDOPQT,intDETQT,intDEPQT,intDEDQT,intEXTQT;
	int intEXDQT,intEXPQT,intPRDQT,intPRTQT;
	int intDOTMNP = 0;
	int intDETMNP = 0;
	int intEXTMNP = 0;
	int intDODMNP = 0;
	int intDEDMNP = 0;
	int intEXDMNP = 0;
	int intDOPMNP = 0;
	int intDEPMNP = 0;
	int intEXPMNP = 0;
	int intTOCMNP = 0;
	int intTOUMNP = 0;
	int intPRTMNP = 0;
	int intPRDMNP = 0;
					  									
	int intDOTSBP = 0;
	int intDETSBP = 0;
	int intEXTSBP = 0;
	int intDODSBP = 0;
	int intDEDSBP = 0;
	int intEXDSBP = 0;
	int intDOPSBP = 0;
	int intDEPSBP = 0;
	int intEXPSBP = 0;
	int intTOCSBP = 0;
	int intTOUSBP = 0;
	int intPRTSBP = 0;
	int intPRDSBP = 0;
					  									
	int intDOTCTG = 0;
	int intDETCTG = 0;
	int intEXTCTG = 0;
	int intDODCTG = 0;
	int intDEDCTG = 0;
	int intEXDCTG = 0;
	int intDOPCTG = 0;
	int intDEPCTG = 0;
	int intEXPCTG = 0;
	int intTOCCTG = 0;
	int intTOUCTG = 0;
	int intPRTCTG = 0;
	int intPRDCTG = 0;
					  									
	int intDOTALL = 0;
	int intDETALL = 0;
	int intEXTALL = 0;
	int intDODALL = 0;
	int intDEDALL = 0;
	int intEXDALL = 0;
	int intDOPALL = 0;
	int intDEPALL = 0;
	int intEXPALL = 0;
	int intTOCALL = 0;
	int intTOUALL = 0;
	int intPRTALL = 0;
	int intPRDALL = 0;
					  									
	int intDOTPRD = 0;
	int intDETPRD = 0;
	int intEXTPRD = 0;
	int intDODPRD = 0;
	int intDEDPRD = 0;
	int intEXDPRD = 0;
	int intDOPPRD = 0;
	int intDEPPRD = 0;
	int intEXPPRD = 0;
	int intTOCPRD = 0;
	int intTOUPRD = 0;
	int intPRTPRD = 0;
	int intPRDPRD = 0;
	
	int intDOPPKG = 0;
	int intEXPPKG = 0;
	int intTOCPKG = 0;
	int intTOUPKG = 0;
	int intTORPKG = 0;
	
	int intTOTMNP = 0;
	int intTODMNP = 0;
	int intTOPMNP = 0;
	int intTORMNP = 0;
									
	int intTOTSBP = 0;
	int intTODSBP = 0;
	int intTOPSBP = 0;
	int intTORSBP = 0;
									
	int intTOTCTG = 0;
	int intTODCTG = 0;
	int intTOPCTG = 0;
	int intTORCTG = 0;
									
	int intTOTALL = 0;
	int intTODALL = 0;
	int intTOPALL = 0;
	int intTORALL = 0;
	
	int intTOTPRD = 0;
	int intTODPRD = 0;
	int intTOPPRD = 0;
	int intTORPRD = 0;
	
	int intEXOPRD = 0;
	int intDOOPRD = 0;
	int intDEOPRD = 0;
				
	int intEXOMNP = 0;
	int intDOOMNP = 0;
	int intDEOMNP = 0;
	
	int intEXOSBP= 0;
	int intDOOSBP = 0;
	int intDEOSBP = 0;
	
	int intEXOCTG = 0;
	int intDOOCTG = 0;
	int intDEOCTG = 0;
										
	int intEXOALL = 0;
	int intDOOALL = 0;
	int intDEOALL = 0;
	
	
	float fltINDQT,fltPNDQT,fltSTKQT,fltUCLQT,fltRESQT,fltTODQT;
    Hashtable<String,String> hstDAYMTH,hstDOPQT,hstDEPQT,hstEXPQT,hstTOTPQT,hstALLREC,hstPRDDS,hstMTHDSC;
    
    boolean flgUPDFL=false,flgEOF=false,flgTB1FL = false,flgTB2FL = false,flgTB3FL = false;
    DefaultTableCellRenderer LM_DFTCRN;
    TableColumn TCL_PRDDS,TCL_DOTQT,TCL_DETQT,TCL_EXTQT,TCL_TOTQT,TCL_DOOQT,TCL_DEOQT,TCL_EXOQT,TCL_DSPQT,TCL_DOPQT,TCL_DEPQT,TCL_EXPQT,TCL_TOPQT;
	
	FileOutputStream fosREPORT;
	DataOutputStream dosREPORT;
	
	
	mr_qrtrg()
	{
	    super(1);
	    try
	    {
	        setMatrix(20,20);
	        
	            
	       
     
        pnlSLT01 = new JPanel(null);
        pnlSLT02 = new JPanel();
		
		pnlSTA01 = new JPanel();
		pnlSTA02 = new JPanel();
		
		pnlGST01 = new JPanel();
		pnlMAIN=new JPanel();
	//        pnlSLTRG.setLayout( null);
		//  pnlSTKAV.setLayout( null);
	//	 //pnlGSTAV.setLayout( null);
	//	pnlSLT01.setLayout(null);
		//pnlSTA01.setLayout(null);
	    
		pnlSLTRG=new JPanel(null);
		 pnlSTKAV=new JPanel(null);
		 pnlGSTAV=new JPanel(null);
		
		
		
		
		LM_DFTCRN = new DefaultTableCellRenderer();
        LM_DFTCRN.setHorizontalAlignment(JLabel.RIGHT);
        tabQRTRG = new JTabbedPane();
        grpSLTRG = new ButtonGroup();
		grpSTKAV = new ButtonGroup();
		grpSELEC = new ButtonGroup();
		grpREPOT = new ButtonGroup();
        add(new JLabel("Date"),3,1,1,2.5,this,'L');
        add(txtMMYDT = new TxtDate(),3,2,1,2.3,this,'L');
		
		add(new JLabel("Product Type"),3,5,1,2.5,this,'L');
		add(txtPRDTP = new JTextField(),3,7,1,3,this,'L');       
		
		
		
		add(btnRUN = new JButton("RUN"),3,10,1,2,this,'L');
		add(btnQUERY=new JButton("QUERY"),3,12,1,2.2,this,'L');
		
	
		
	    setMatrix(20,20);  
		add(rdbZONWS = new JRadioButton("Zonewise",true),1,2,1,2.5,pnlSLTRG,'L');
		grpSLTRG.add(rdbZONWS);
		
        add(rdbRGNWS = new JRadioButton("RegionWise",false),2,2,1,2.5,pnlSLTRG,'L');
         grpSLTRG.add(rdbRGNWS);
		
        add(rdbALLCG = new JRadioButton("All",true),1,5,1,2,pnlSLTRG,'L');
                grpSELEC.add(rdbALLCG);
		
		add(rdbSPECF = new JRadioButton("Specific",false),2,5,1,2,pnlSLTRG,'L');
		grpSELEC.add(rdbSPECF);
		
		add(txtSPEFC =new JTextField(),2,7,1,2.5,pnlSLTRG,'L');
		//add(lblTBLHDR = new JLabel(" "),3,7,1,1,pnlSLTRG,'L');
		
        
		
		 
		//pnlSLT02.setLayout( null);
     	//pnlSLTRG.setLayout(null);
     	 setMatrix(20,20);
        //pnlSLT02.setLayout(null);
        //pnlSLTRG.add(pnlSLT02);
        
        pnlSTKAV.setLayout( null);
        setMatrix(20,20);
        add(rdbDOMWS = new JRadioButton("Domestic",true),1,2,1,2.3,pnlSTKAV,'L');
		grpSTKAV.add(rdbDOMWS);
		
		add(rdbEXPWS = new JRadioButton("Export",false),2,2,1,2,pnlSTKAV,'L');
		        grpSTKAV.add(rdbEXPWS);
		
		add(rdbALLWS = new JRadioButton("All",false),3,2,1,2,pnlSTKAV,'L');
		        grpSTKAV.add(rdbALLWS);
		
		add(rdbDETAL = new JRadioButton("Detail",true),3,5,1,2,pnlSTKAV,'L');
		grpREPOT.add(rdbDETAL);
		
		add(rdbSUMRY = new JRadioButton("Summary",false),3,7,1,2.5,pnlSTKAV,'L');
        grpREPOT.add(rdbSUMRY);
		
		add(chkDOMWS = new JCheckBox("Dom."),1,5,1,2,pnlSTKAV,'L');
		
		add(chkDEXWS = new JCheckBox("Deemed Export"),1,7,1,3,pnlSTKAV,'L');
        
       // pnlSTA01.setLayout( null);
        //pnlSTKAV.setLayout(new BorderLayout());
		//pnlSTKAV.add(pnlSTA01,BorderLayout.CENTER);
		//pnlGSTAV.setLayout(new BorderLayout());
	
		strTITLE1 = "Region & Zonewise Sales Target Details";
		strTITLE2 = "Sales Target v/s Stock Availability Details";
		strTITLE3 = "Daily Gradewise Availability Position";
		
		tabQRTRG.add(strTITLE1,pnlSLTRG);
		tabQRTRG.add(strTITLE2,pnlSTKAV);
		tabQRTRG.add(strTITLE3,pnlGSTAV);
        
     	add(tabQRTRG,4,1,15,19.5,this,'L');
        //tabQRTRG.setLayout(new BorderLayout());
       //tabQRTRG.add(pnlMAIN,BorderLayout.SOUTH);
        
     	tabQRTRG.addMouseListener( this);
    
    
     	
     	
        
      //  pnlSTKAV.setLayout(new BorderLayout());
	//	pnlSTKAV.add(pnlSTA01);
		//pnlGSTAV.setLayout());
     	LM_OPTNPN = new JOptionPane();  
     	 hstDOPQT = new Hashtable<String,String>();
 		hstDEPQT = new Hashtable<String,String>();
 		hstEXPQT = new Hashtable<String,String>();
 		hstTOTPQT = new Hashtable<String,String>();
 		hstALLREC = new Hashtable<String,String>();
 		hstPRDDS = new Hashtable<String,String>();
 		hstDAYMTH = new Hashtable<String,String>();
 		hstDAYMTH.put("01","31");
 		hstDAYMTH.put("02","28");
 		hstDAYMTH.put("03","31");
 		hstDAYMTH.put("04","30");
 		hstDAYMTH.put("05","31");
 		hstDAYMTH.put("06","30");
 		hstDAYMTH.put("07","31");
 		hstDAYMTH.put("08","31");
 		hstDAYMTH.put("09","30");
 		hstDAYMTH.put("10","31");
 		hstDAYMTH.put("11","30");
 		hstDAYMTH.put("12","31");
 		hstMTHDSC = new Hashtable<String,String>();
 		hstMTHDSC.put("01","January");
 		hstMTHDSC.put("02","February");
 		hstMTHDSC.put("03","March");
 		hstMTHDSC.put("04","April");
 		hstMTHDSC.put("05","May");
 		hstMTHDSC.put("06","June");
 		hstMTHDSC.put("07","July");
 		hstMTHDSC.put("08","August");
 		hstMTHDSC.put("09","September");
 		hstMTHDSC.put("10","October");
 		hstMTHDSC.put("11","November");
 		hstMTHDSC.put("12","December");
 		  
      
		
       
		txtPRDTP.setEnabled(false);
		txtSPEFC.setEnabled(false);
		btnQUERY.setEnabled(false);
		btnRUN.setEnabled( false);
		 M_pnlRPFMT.setVisible(true);
		
		
		setMSG("Please press Run button for updating data",'N');
		 
		 
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX,"constructor");
	    }
	}
	
	
	void setENBL(boolean L_flgSTAT)
	{
	    super.setEnabled( L_flgSTAT);
	
	   try
	   {
	    
	    txtMMYDT.setText(cl_dat.M_strLOGDT_pbst);
		txtPRDTP.setEnabled(false);
		txtSPEFC.setEnabled(false);
		btnQUERY.setEnabled(false);
		btnRUN.setEnabled( false);
	   }catch(Exception L_EX)
	   {
	       setMSG(L_EX,"this is setEnabled");
	   }
		  
	    
	}  
	/*/**
	 * 
	 * @param L_flgSTAT this function used for the remove the M_txtTODAT,M_txtFRDAT textFields
	 */
	 void removeENBL(boolean L_flgSTAT)
     {
        
           M_txtFMDAT.setVisible(false);
	     	M_txtTODAT.setVisible(false);
	    	M_lblFMDAT.setVisible(false);
		    M_lblTODAT.setVisible(false);
		    
		    
		    
	   }	
		public void mouseClicked(MouseEvent L_ME)
		{
		    
		 
		 
			try
			{
			   // super.mouseClicked( L_ME);
			    System.out.println(tabQRTRG.getSelectedIndex());
			    if(L_ME.getSource().equals(tabQRTRG))
				{
			        System.out.println("This is mouseClicked2");
					System.out.println(tabQRTRG.getSelectedIndex());
					System.out.println("This is mouseClicked4");
					if(tabQRTRG.getSelectedIndex() == -1)
					{
					    
					}
				}
			   
			    
			    
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"This is iteam changed");
			    
			}
		}	

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
		    strSPEFC = txtSPEFC.getText().toString().trim();
			strMMYDT = txtMMYDT.getText().toString().trim();
			strPRTDS = txtPRDTP.getText().toString().trim();
			strMONTH = strMMYDT.substring(3,5).toString().trim();
			strYEAR = strMMYDT.substring(6).toString().trim();
			strDBSMY = strMONTH+strMMYDT.substring(8).toString().trim();
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) /** Combo Opotion Remove the From Date & To Date\*/
			{
			    removeENBL(false);
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			    {
			        txtMMYDT.setEnabled(false);
			        txtPRDTP.setEnabled(false);
					txtSPEFC.setEnabled(false);
					btnQUERY.setEnabled(false);
					btnRUN.setEnabled( false);
					
			        
			    }
			    else
			    {
			        txtMMYDT.setEnabled(true);
			        txtPRDTP.setEnabled(false);
					txtSPEFC.setEnabled(true);
					btnQUERY.setEnabled(false);
					btnRUN.setEnabled( true);
			        
			        
			    }
			}  
			
			
			if(M_objSOURC==  btnRUN)
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst );
                flgUPDFL = false;
                if(chkUPDDT())
                {
					intALLQT(); /**initializes all the qty to Zero*/
                    updORDQT();	/**updates order booking qty*/
                    updPNDQT();	/**updates Pnd.Bkg. order qty*/
					updDSPQT();	/**updates despatch qty*/
                    updSTKQT();	/**updates stock qty*/
					updPRDQT();	/**updates production qty*/
                }
                else
                {
                    setMSG("Data for the entered date already updated",'E');
                    int L_SELOPT = LM_OPTNPN.showConfirmDialog(this,"Do you want to update data again","Confirmation",JOptionPane.YES_NO_OPTION);
                    if(L_SELOPT == 0)
                    {
						intALLQT(); /**initializes all the qty to Zero*/
						updORDQT();	/**updates order booking qty*/
						updPNDQT();	/**updates Pnd.Bkg. order qty*/
						updDSPQT();	/**updates despatch qty*/
						updSTKQT();	/**updates stock qty*/
						updPRDQT();	/**updates production qty*/
                    }
                    else
                        flgUPDFL = true;
                }   
                    setMSG("Enter Product Description or press F1 for help.",'N');
                    btnRUN.setEnabled(false);
                    txtPRDTP.setEnabled(true);
    				txtPRDTP.setText("Polystyrene");
    				strPRDTP = "51";
    				txtPRDTP.requestFocus();
    				this.setCursor(cl_dat.M_curDFSTS_pbst);    
				}

			if(M_objSOURC ==rdbDOMWS)
			{
				chkDOMWS.setEnabled(true);
				chkDEXWS.setEnabled(true);
				chkDOMWS.setSelected(true);
				chkDOMWS.requestFocus();
				btnQUERY.setEnabled(true);
				btnQUERY.requestFocus();
			}
			if(M_objSOURC== rdbEXPWS || M_objSOURC==rdbALLWS)
			{
				chkDOMWS.setSelected(false);
				chkDEXWS.setSelected(false);
				chkDOMWS.setEnabled(false);
				chkDEXWS.setEnabled(false);
				btnQUERY.setEnabled(true);
				btnQUERY.requestFocus();
			}
			if(M_objSOURC==rdbDETAL || M_objSOURC== rdbSUMRY)
			{
				chkDOMWS.setSelected(false);
				chkDEXWS.setSelected(false);
				chkDOMWS.setEnabled(false);
				chkDEXWS.setEnabled(false);
				btnQUERY.setEnabled(true);
				btnQUERY.requestFocus();
			}
			if(M_objSOURC==btnQUERY)
			{
			    try
			    {
			       
			        
			        getDATA();
			        tabQRTRG.remove(tblSLTRG);
			        
			        
			        
			    }catch(Exception L_EX)
			    {
			        setMSG(L_EX,"This is the btnQUERY");
			    }
			    
			}  
			if(M_objSOURC==rdbZONWS || M_objSOURC==rdbRGNWS)
			{
			    rdbALLCG.setSelected(true);
				txtSPEFC.setText("");
				txtSPEFC.setEnabled(false);
				rdbALLCG.requestFocus();
			}
			if(M_objSOURC==rdbSPECF)
			{
			    txtSPEFC.setEnabled(true);
				txtSPEFC.requestFocus();
			}
			if(M_objSOURC==rdbALLCG)
			{
				txtSPEFC.setText("");
				txtSPEFC.setEnabled(false);
				strCHP01 = "";
				strCHP02 = "";
				btnQUERY.setEnabled(true);
				btnQUERY.requestFocus();
			}
			if(M_objSOURC==txtSPEFC)
			{
				strSPEFC = txtSPEFC.getText().toString().trim();
				stbSTRQRY = new StringBuffer("Select cmt_chp01,cmt_chp02 from co_cdtrn where ");
				if(rdbZONWS.isSelected()){
                                        stbSTRQRY.append("cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codds='"+strSPEFC+"'");
				}
				else if(rdbRGNWS.isSelected()){
					stbSTRQRY.append("cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN' and cmt_codds='"+strSPEFC+"'");
				}
                M_rstRSSET = cl_dat.exeSQLQRY1(stbSTRQRY.toString().trim());
				if(M_rstRSSET.next()){
					strCHP01 = nvlSTRVL(M_rstRSSET.getString("cmt_chp01"),"");
					strCHP02 = nvlSTRVL(M_rstRSSET.getString("cmt_chp02"),"");
					btnQUERY.setEnabled(true);
					btnQUERY.requestFocus();
				}else
				{
					setMSG("Please enter valid data or press F1 for help.",'E');
					txtSPEFC.requestFocus();
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			if(M_objSOURC== txtPRDTP)
			{
				M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,2) l_codcd from CO_CDTRN where CMT_CGMTP='MST'";
				M_strSQLQRY += "and CMT_CGSTP = 'COXXPGR' and cmt_codds='"+txtPRDTP.getText().toString().trim()+"' and cmt_ccsvl='MG'";
                M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					setMSG("Valid product type.",'N');
					strPRDTP = nvlSTRVL(M_rstRSSET.getString("l_codcd"),"");
					rdbZONWS.requestFocus();
				}else
				{
					setMSG("Please enter valid product type or press F1 key for help",'E');
					txtPRDTP.requestFocus();
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
	}catch(Exception L_EX)
	  {
			        setMSG(L_EX,"This is action performed");
			    }
			}
		

    public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			int L_intKEYCD=L_KE.getKeyCode();
			if(L_intKEYCD==L_KE.VK_LEFT&&L_KE.isControlDown())
			    tabQRTRG.setSelectedIndex(tabQRTRG.getSelectedIndex()%tabQRTRG.getTabCount()-1);
			else if(L_intKEYCD==L_KE.VK_RIGHT&&L_KE.isControlDown())
			    tabQRTRG.setSelectedIndex(tabQRTRG.getSelectedIndex()%tabQRTRG.getTabCount()+1);
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC==txtSPEFC)
				{
				    String L_ARRHDR[] = {"Code","Description"};
					stbSTRQRY = new StringBuffer("Select cmt_codcd,cmt_codds from co_cdtrn where ");
					if(rdbZONWS.isSelected())
					{
                     stbSTRQRY.append("cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON'");
					}
					else if(rdbRGNWS.isSelected())
					{
						stbSTRQRY.append("cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN'");
					}
                    cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPEFC";
	   				cl_hlp(stbSTRQRY.toString().trim(),1,2,L_ARRHDR,2,"CT");
				    
				}
				if(M_objSOURC==txtPRDTP)
				{
					String L_ARRHDR[] ={ "Product Type.","Product Description"};
					M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST'";
					M_strSQLQRY += " and CMT_CGSTP = 'COXXPGR' and cmt_ccsvl='MG'";
					System.out.println(M_strSQLQRY);
                    cl_dat.M_flgHELPFL_pbst  = true;
					M_strHLPFLD = "txtPRDTP";
						cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
					
				}
				
			}	
			
			
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is keypressed");
		    
		}
	}
	void exeHLPOK()
	{
	    super.exeHLPOK();
	    //StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
	    try
	    {
	    if(M_strHLPFLD== "txtSPEFC")
	    {
	        txtSPEFC.setText(cl_dat.M_strHLPSTR_pbst  );
	        
	    }
	    if(M_strHLPFLD=="txtPRDTP")
	    {
	        txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst );
	        
	    }
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX, "this is exehlpok");
	    }
	}
	
	void exePRINT()
	{   
	    //if (!vldDATA())
			//return;
			try
			{
	        int RECCT  = 0 ;
	   		    
   		    setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
   		   if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_qrtrg.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "mr_qrtrg.doc";	
							
			getDATA();
			
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
    			    p  = r.exec("C:\\windows\\wordpad.exe " + strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Details"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
			
    			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}	
	}
	/** Method to fetch data from tblSTKDTL table & club it with Header in Data Output Stream  */
	  private void getDATA()
	 {
		try
		{   
		
	        strSPEFC = txtSPEFC.getText().toString().trim();
			strMMYDT = txtMMYDT.getText().toString().trim();
			strPRTDS = txtPRDTP.getText().toString().trim();
			strMONTH = strMMYDT.substring(3,5).toString().trim();
			strYEAR = strMMYDT.substring(6).toString().trim();
			strDBSMY = strMONTH+strMMYDT.substring(8).toString().trim();
	    	if(flgUPDFL) //entered for first time
	    	{      
			
	    	    cl_dat.M_PAGENO = 1;
			    String strln="";
			    
			    strFILNM = cl_dat.M_strREPSTR_pbst +"mr_qrtrg.html";
			    
			    fosREPORT = new FileOutputStream(strFILNM);
			    
			    dosREPORT = new DataOutputStream(fosREPORT);
			    
			    
			    int RECCT  = 0 ;
		        cl_dat.M_intLINNO_pbst=0;
	   		    setMSG("Report Generation in Process....." ,'N');
	   		  //  setCursor(cl_dat.M_curWTSTS_pbst);
	   		     if(M_rdbHTML.isSelected())
			    {
			        
			        dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
			        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
			        dosREPORT.writeBytes("</STYLE>");
				 }
				 else
					prnFMTCHR(dosREPORT,M_strCPI10);
	   		  prnHEADER();
	   		     
			 	fosREPORT.flush();
		  		dosREPORT.flush();
		  		intLINENO = 52;
		  		intLINLEN = 140;
	        
				        hstTOTPQT.clear();	hstALLREC.clear();hstDOPQT.clear();	hstPRDDS.clear();
				        strPRDCD = "";strPRDCD1 = "";strPRDCD2 = "";strMNGRP = "";strMNGRP1 = "";
				  		strMNGRP2 = "";strSBGRP = "";strSBGRP1 = "";strSBGRP2 = "";strSTSFL = "";strSTSFL1 = "";
				  		strSTSFL2 = "";	strPRDDS = "";	strPRDDS1 = "";	strPRDDS2 = "";intDOTQT = 0;intDETQT = 0;
						intEXTQT = 0;intDODQT = 0;intDEDQT = 0;intEXDQT = 0;intDOPQT = 0;intDEPQT = 0;intEXPQT = 0;
						intCLSQT = 0;intNCLQT = 0;intPRTQT = 0;	intPRDQT = 0;intTRGQT = 0;intDOTMNP = 0;intDETMNP = 0;
						intEXTMNP = 0;intDODMNP = 0;intDEDMNP = 0;intEXDMNP = 0;intDOPMNP = 0;intDEPMNP = 0;intEXPMNP = 0;
						intTOCMNP = 0;intTOUMNP = 0;intTORMNP = 0;intPRTMNP = 0;intPRDMNP = 0;intDOTSBP = 0;intDETSBP = 0;
						intEXTSBP = 0;intDODSBP = 0;intDEDSBP = 0;intEXDSBP = 0;intDOPSBP = 0;intDEPSBP = 0;
						intEXPSBP = 0;intTOCSBP = 0;intTOUSBP = 0;intTORSBP = 0;intPRTSBP = 0;intPRDSBP = 0;
													
															
				  		intDOTCTG = 0;intDETCTG = 0;intEXTCTG = 0;intDODCTG = 0;intDEDCTG = 0;intEXDCTG = 0;intDOPCTG = 0;
						intDEPCTG = 0;intEXPCTG = 0;intTOCCTG = 0;intTOUCTG = 0;intTORCTG = 0;intPRTCTG = 0;intPRDCTG = 0;
													
															
				  		intDOTALL = 0;intDETALL = 0;intEXTALL = 0;intDODALL = 0;intDEDALL = 0;intEXDALL = 0;intDOPALL = 0;
						intDEPALL = 0;intEXPALL = 0;intTOCALL = 0;intTOUALL = 0;intTORALL = 0;intPRTALL = 0;intPRDALL = 0;
												
															
				  		intDOTPRD = 0;intDETPRD = 0;intEXTPRD = 0;intDODPRD = 0;intDEDPRD = 0;intEXDPRD = 0;
						intDOPPRD = 0;intDEPPRD = 0;intEXPPRD = 0;intTOCPRD = 0;intTOUPRD = 0;intTORPRD = 0;
						intPRTPRD = 0;intPRDPRD = 0;
									
						intDOPPKG = 0;intEXPPKG = 0;intTOCPKG = 0;	intTOUPKG = 0;intTORPKG = 0;
						boolean L_1STFL = true;
				  		flgEOF = false;
						flgTB1FL = false;
						flgTB2FL = false;
						flgTB3FL = false;
				  		intROWCNT = 0;
				  		/**
						 * making query as per the sale type selected if no saletype is selected
						 * overall transactions are shown
						 */
				  		String L_ADDSTR = "";
						String L_CNDSTR = " (";
						stbSTRQRY = new StringBuffer("");
						intSLTLEN = 17;  //length of sale type column in reports
						intSTKLEN = 17;  //length of stock column in reports
						intCLMLEN = 17;  //length of column inside sale type in reports
						intTOTLEN = 17;  //length of total column inside sale type in reports
						LM_GRDLEN = 17;  //length of grade column
						if(tabQRTRG.getTitleAt(tabQRTRG.getSelectedIndex()).equals(strTITLE1)){
							stbSTRQRY.append("Select tm_stsfl,SUBSTRING(tm_prdcd,1,4) l_sbgrp,tm_prdcd,tm_prdds,SUBSTRING(tm_prdcd,1,2) tm_prdtp,");
							flgTB1FL = true;
							intSLTLEN = 15;  //length of sale type column in reports
							intCLMLEN = 15;  //length of column inside sale type in reports
							LM_GRDLEN = 20;  //length of grade column
							if(rdbZONWS.isSelected()){
								if(strCHP02.trim().length() > 0){
									if(strCHP02.equals("03")){
										intSTKLEN = 15;
										L_ADDSTR += " sum(tm_exext) l_extqt,sum(tm_exexd) l_exdqt,sum(tm_exexp) l_expqt";
									    L_CNDSTR += "tm_exext+tm_exexd+tm_exexp+";
									}else if(strCHP02.equals("01") || strCHP02.equals("02")){
										intSLTLEN = 22;
										intCLMLEN = 11;
										L_ADDSTR += " sum(tm_"+strCHP01+"dot) l_dotqt,sum(tm_"+strCHP01+"det) l_detqt,";
									    L_ADDSTR += " sum(tm_"+strCHP01+"dod) l_dodqt,sum(tm_"+strCHP01+"ded) l_dedqt,";
										L_ADDSTR += " sum(tm_"+strCHP01+"dop) l_dopqt,sum(tm_"+strCHP01+"dep) l_depqt";
									    L_CNDSTR += "tm_"+strCHP01+"dot+tm_"+strCHP01+"det+tm_"+strCHP01+"dod+tm_"+strCHP01+"ded+";
										L_CNDSTR += "tm_"+strCHP01+"dop+tm_"+strCHP01+"dep+";
									    }
								}else{
									intSLTLEN = 30;  //length of sale type column in reports
									intCLMLEN = 10;  //length of column inside sale type in reports
									LM_GRDLEN = 20;  //length of grade column
									L_ADDSTR += " sum(tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot) l_dotqt,sum(tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet) l_detqt,sum(tm_exext) l_extqt,";
									L_ADDSTR += " sum(tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod) l_dodqt,sum(tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded) l_dedqt,sum(tm_exexd) l_exdqt,";
									L_ADDSTR += " sum(tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop) l_dopqt,sum(tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep) l_depqt,sum(tm_exexp) l_expqt";
									L_CNDSTR += "tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot+tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet+tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod+";
									L_CNDSTR += "tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded+tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop+tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep+";
									L_CNDSTR += "tm_exext+tm_exexd+tm_exexp+";
								}
							}
							else if(rdbRGNWS.isSelected()){
								if(strCHP01.trim().length() > 0){
									intSLTLEN = 22;
									intCLMLEN = 11;
									if(strCHP01.equals("01")){
										L_ADDSTR += " sum(tm_wzdot+tm_czdot) l_dotqt,sum(tm_wzdet+tm_czdet) l_detqt,";
									    L_ADDSTR += " sum(tm_wzdod+tm_czdod) l_dodqt,sum(tm_wzded+tm_czded) l_dedqt,";
										L_ADDSTR += " sum(tm_wzdop+tm_czdop) l_dopqt,sum(tm_wzdep+tm_czdep) l_depqt";
									    L_CNDSTR += "tm_wzdot+tm_czdot+tm_wzdet+tm_czdet+tm_wzdod+tm_czdod+tm_wzded+tm_czded+";
										L_CNDSTR += "tm_wzdop+tm_czdop+tm_wzdep+tm_czdep+";
									}
									else if(strCHP01.equals("02")){
										L_ADDSTR += " sum(tm_nzdot+tm_szdot+tm_ezdot) l_dotqt,sum(tm_nzdet+tm_szdet+tm_ezdet) l_detqt,";
									    L_ADDSTR += " sum(tm_nzdod+tm_szdod+tm_ezdod) l_dodqt,sum(tm_nzded+tm_szded+tm_ezded) l_dedqt,";
										L_ADDSTR += " sum(tm_nzdop+tm_szdop+tm_ezdop) l_dopqt,sum(tm_nzdep+tm_szdep+tm_ezdep) l_depqt";
									    L_CNDSTR += "tm_nzdot+tm_szdot+tm_ezdot+tm_nzdet+tm_szdet+tm_ezdet+tm_nzdod+tm_szdod+tm_ezdod+";
										L_CNDSTR += "tm_nzded+tm_szded+tm_ezded+tm_nzdop+tm_szdop+tm_ezdop+tm_nzdep+tm_szdep+tm_ezdep+";
									}
									else if(strCHP01.equals("03")){
										intSLTLEN = 15;  
										intCLMLEN = 15;
										intSTKLEN = 15;
									    L_ADDSTR += " sum(tm_exext) l_extqt,sum(tm_exexd) l_exdqt,sum(tm_exexp) l_expqt";
									    L_CNDSTR += "tm_exext+tm_exexd+tm_exexp+";
									}
								}else{
									intSLTLEN = 30;  
									intCLMLEN = 10;  
									LM_GRDLEN = 20;  
									L_ADDSTR += " sum(tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot) l_dotqt,sum(tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet) l_detqt,sum(tm_exext) l_extqt,";
									L_ADDSTR += " sum(tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod) l_dodqt,sum(tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded) l_dedqt,sum(tm_exexd) l_exdqt,";
									L_ADDSTR += " sum(tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop) l_dopqt,sum(tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep) l_depqt,sum(tm_exexp) l_expqt";
									L_CNDSTR += "tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot+tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet+tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod+";
									L_CNDSTR += "tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded+tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop+tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep+";
									L_CNDSTR += "tm_exext+tm_exexd+tm_exexp+";
								}
							}
							L_ADDSTR += " from mr_tmtrn where ";
						}
						else if(tabQRTRG.getTitleAt(tabQRTRG.getSelectedIndex()).equals(strTITLE2)){
							stbSTRQRY.append("Select tm_stsfl,SUBSTRING(tm_prdcd,1,4) l_sbgrp,tm_prdcd,tm_prdds,SUBSTRING(tm_prdcd,1,2) tm_prdtp,");
							flgTB2FL = true;
							if(rdbDOMWS.isSelected()){
								if(chkDOMWS.isSelected() && chkDEXWS.isSelected()){
									intSLTLEN = 22;
									intCLMLEN = 11;
									intTOTLEN = 11;
									intSTKLEN = 10;
								}else{
									intSLTLEN = 17;
									intCLMLEN = 17;
									intSTKLEN = 17;
									intTOTLEN = 17;
								}
								if(chkDOMWS.isSelected()){
									L_ADDSTR += " sum(tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot) l_dotqt,";
									L_ADDSTR += " sum(tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod) l_dodqt,";
									L_ADDSTR += " sum(tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop) l_dopqt,";
									L_CNDSTR += "tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot+";
									L_CNDSTR += "tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod+";
									L_CNDSTR += "tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop+";
								}
								if(chkDEXWS.isSelected()){
									L_ADDSTR += " sum(tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet) l_detqt,";
									L_ADDSTR += " sum(tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded) l_dedqt,";
									L_ADDSTR += " sum(tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep) l_depqt,";
									L_CNDSTR += "tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet+";
									L_CNDSTR += "tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded+";
									L_CNDSTR += "tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep+";
								}
							}
							else if(rdbEXPWS.isSelected()){
								L_ADDSTR += " sum(tm_exext) l_extqt,";
								L_ADDSTR += " sum(tm_exexd) l_exdqt,";
								L_ADDSTR += " sum(tm_exexp) l_expqt,";
								L_CNDSTR += "tm_exext+tm_exexd+tm_exexp+";
							}
							else if(rdbALLWS.isSelected()){
								if(rdbSUMRY.isSelected()){
									intSLTLEN = 12;
									intCLMLEN = 12;
									intSTKLEN = 12;
									intTOTLEN = 12;
								}
								else if(rdbDETAL.isSelected()){
									intSLTLEN = 21;
									intCLMLEN = 7;
									intSTKLEN = 12;
									intTOTLEN = 12;
								}
								L_ADDSTR += " sum(tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot) l_dotqt,";
								L_ADDSTR += " sum(tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod) l_dodqt,";
								L_ADDSTR += " sum(tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop) l_dopqt,";
								L_ADDSTR += " sum(tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet) l_detqt,";
								L_ADDSTR += " sum(tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded) l_dedqt,";
								L_ADDSTR += " sum(tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep) l_depqt,";
								L_ADDSTR += " sum(tm_exext) l_extqt,";
								L_ADDSTR += " sum(tm_exexd) l_exdqt,";
								L_ADDSTR += " sum(tm_exexp) l_expqt,";
								L_ADDSTR += " sum(tm_prtqt) l_prtqt,";
								L_ADDSTR += " sum(tm_prdqt) l_prdqt,";
								L_CNDSTR += "tm_exext+tm_exexd+tm_exexp+";
								L_CNDSTR += "tm_nzdot+tm_szdot+tm_ezdot+tm_wzdot+tm_czdot+";
								L_CNDSTR += "tm_nzdod+tm_szdod+tm_ezdod+tm_wzdod+tm_czdod+";
								L_CNDSTR += "tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop+";
								L_CNDSTR += "tm_nzdet+tm_szdet+tm_ezdet+tm_wzdet+tm_czdet+";
								L_CNDSTR += "tm_nzded+tm_szded+tm_ezded+tm_wzded+tm_czded+";
								L_CNDSTR += "tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep+";
								L_CNDSTR += "tm_clsqt+tm_uclqt+";
								}
								L_ADDSTR += "sum(tm_clsqt) l_clsqt,sum(tm_uclqt) l_nclqt from mr_tmtrn where ";
						}else if(tabQRTRG.getTitleAt(tabQRTRG.getSelectedIndex()).equals(strTITLE3)){
							stbSTRQRY.append("Select tm_stsfl,SUBSTRING(tm_prdcd,1,4) l_sbgrp,tm_prdcd,tm_prdds,SUBSTRING(tm_prdcd,1,2) tm_prdtp,tm_pkgtp,");
							intCLMLEN = 15;
							LM_GRDLEN = 15;
							flgTB3FL = true;
							L_ADDSTR += " sum(tm_clsqt) l_clsqt,sum(tm_resqt) l_resqt,sum(tm_uclqt) l_nclqt,";
							L_ADDSTR += " sum(tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop+tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep) l_dopqt,";
							L_ADDSTR += " sum(tm_exexp) l_expqt";
							L_CNDSTR += " tm_exexp+tm_nzdop+tm_szdop+tm_ezdop+tm_wzdop+tm_czdop+tm_nzdep+tm_szdep+tm_ezdep+tm_wzdep+tm_czdep+";
							L_CNDSTR += "tm_clsqt+tm_uclqt+tm_resqt+";
							L_ADDSTR += " from mr_tmtrn where ";
						}
						stbSTRQRY.append(L_ADDSTR);
						if(L_CNDSTR.length()>5)
						{
							L_CNDSTR = L_CNDSTR.substring(0,L_CNDSTR.length()-1) + ") > 0 and ";
							stbSTRQRY.append(L_CNDSTR);
						}
						stbSTRQRY.append(" tm_mmydt='"+strDBSMY+"' and SUBSTRING(tm_prdcd,1,2)='"+strPRDTP+"'");
						if(flgTB1FL || flgTB2FL)
							stbSTRQRY.append(" group by SUBSTRING(tm_prdcd,1,2),SUBSTRING(tm_prdcd,1,4),tm_stsfl,tm_prdcd,tm_prdds order by tm_prdtp,l_sbgrp,tm_stsfl,tm_prdcd");
						else if(flgTB3FL)
							stbSTRQRY.append(" group by SUBSTRING(tm_prdcd,1,2),SUBSTRING(tm_prdcd,1,4),tm_stsfl,tm_pkgtp,tm_prdcd,tm_prdds order by tm_prdtp,l_sbgrp,tm_stsfl,tm_pkgtp,tm_prdcd");
						System.out.println(stbSTRQRY.toString().trim());
						PreparedStatement prpSTATMT = cl_dat.M_conSPDBA_pbst .prepareStatement(stbSTRQRY.toString().trim());
						ResultSet prpRSLSET = prpSTATMT.executeQuery();
				  		
				  		
						if(flgTB1FL || flgTB2FL)
						{
				  			if(prpRSLSET.next())
				  			{
				  				strMNGRP2 = nvlSTRVL(prpRSLSET.getString("tm_prdtp"),"");
				  				strPRDCD2 = nvlSTRVL(prpRSLSET.getString("tm_prdcd"),"");
				  				strSBGRP2 = nvlSTRVL(prpRSLSET.getString("l_sbgrp"),"");
				  				strPRDDS2 = nvlSTRVL(prpRSLSET.getString("tm_prdds"),"");
				  				strSTSFL2 = nvlSTRVL(prpRSLSET.getString("tm_stsfl"),"");
				  				System.out.println("prepare Result statment" +strSTSFL2);
								if(flgTB3FL)
									strPKGTP2 = nvlSTRVL(prpRSLSET.getString("tm_pkgtp"),"");
								if(strSTSFL2.equals("4"))
									strSTSFL2 = "3";
								
								if(flgTB1FL){
									if(rdbZONWS.isSelected()){
										if(strCHP02.trim().length() > 0){
											if(strCHP02.equals("03")){
											    intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  								intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  								intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
											}else if(strCHP02.equals("01") || strCHP02.equals("02")){
											    intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  								intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  								intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
												intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  								intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  								intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
											}
										}else{
											intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  							intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  							intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
											intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  							intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  							intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
											intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  							intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  							intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
										}
									}
									else if(rdbRGNWS.isSelected()){
										if(strCHP01.trim().length() > 0){
											if(strCHP01.equals("01") || strCHP01.equals("02")){
												intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  								intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  								intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
												intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  								intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  								intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
											}
											else if(strCHP01.equals("03")){
											    intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  								intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  								intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
											}
										}else{
											intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  							intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  							intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
											intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  							intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  							intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
											intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  							intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  							intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
										}
									}
								}
								else if(flgTB2FL){
									if(rdbDOMWS.isSelected())
                                    {
										if(chkDOMWS.isSelected())
                                        {
											intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  							intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  							intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
										}
										if(chkDEXWS.isSelected()){
											intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  							intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  							intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
										}
									}
									else if(rdbEXPWS.isSelected()){
										intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  						intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  						intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
									}
									else if(rdbALLWS.isSelected()){
										intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  						intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  						intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
										intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  						intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  						intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
										intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  						intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  						intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
										intPRTQT = Math.round(prpRSLSET.getFloat("l_prtqt"));
										intPRDQT = Math.round(prpRSLSET.getFloat("l_prdqt"));
									}
									intCLSQT = Math.round(prpRSLSET.getFloat("l_clsqt"));
				  					intNCLQT = Math.round(prpRSLSET.getFloat("l_nclqt"));
								}
								while(!flgEOF)
								{
				  					if(cl_dat.M_intLINNO_pbst >= intLINENO)
				  					{
							            cl_dat.M_intLINNO_pbst = 0;
							            cl_dat.M_PAGENO += 1;
				  						prnHEADER(); //gets the Header of the report
							        }
				  					strMNGRP = strMNGRP2;
				  					strSBGRP = strSBGRP2;
				  					strSTSFL = strSTSFL2;
				  					strPRDCD = strPRDCD2;
				  					strPRDDS = strPRDDS2;
									if(L_1STFL){
				  						strMNGRP1 = strMNGRP;
				  						strPRDCD1 = strPRDCD;
				  						strSBGRP1 = strSBGRP;
				  						strSTSFL1 = strSTSFL;
				  						strPRDDS1 = strPRDDS;
										L_1STFL = false;
				  						}
				  					prnGRPHDR("MG",0);
				  					strMNGRP1 = strMNGRP;
				  					while((strMNGRP).equals(strMNGRP1) && !flgEOF){
				  						prnGRPHDR("SG",2);
				  						strMNGRP = strMNGRP2;
				  						strMNGRP1 = strMNGRP;
				  						while((strMNGRP+strSBGRP).equals(strMNGRP1+strSBGRP1) && !flgEOF){
				  							prnGRPHDR("CG",4);
				  							strMNGRP = strMNGRP2;
				  							strMNGRP1 = strMNGRP;
				  							strSBGRP = strSBGRP2;
				  							strSBGRP1 = strSBGRP;
				  							while((strMNGRP+strSBGRP+strSTSFL).equals(strMNGRP1+strSBGRP1+strSTSFL1) && !flgEOF){
				  								strMNGRP = strMNGRP2;
				  								strMNGRP1 = strMNGRP;
				  								strSBGRP = strSBGRP2;
				  								strSBGRP1 = strSBGRP;
				  								strSTSFL = strSTSFL2;
				  								strSTSFL1 = strSTSFL;
				  								while((strMNGRP+strSBGRP+strSTSFL+strPRDCD).equals(strMNGRP1+strSBGRP1+strSTSFL1+strPRDCD1) && !flgEOF){
													
														
													intDOTMNP += intDOTQT;
													intDETMNP += intDETQT;
													intEXTMNP += intEXTQT;
													intDODMNP += intDODQT;
													intDEDMNP += intDEDQT;
													intEXDMNP += intEXDQT;
													intDOPMNP += intDOPQT;
													intDEPMNP += intDEPQT;
													intEXPMNP += intEXPQT;
													intTOCMNP += intCLSQT;
				  									intTOUMNP += intNCLQT;
													intPRTMNP += intPRTQT;
													intPRDMNP += intPRDQT;
				  										
																
				  									intDOTSBP += intDOTQT;
													intDETSBP += intDETQT;
													intEXTSBP += intEXTQT;
													intDODSBP += intDODQT;
													intDEDSBP += intDEDQT;
													intEXDSBP += intEXDQT;
													intDOPSBP += intDOPQT;
													intDEPSBP += intDEPQT;
													intEXPSBP += intEXPQT;
				  									intTOCSBP += intCLSQT;
				  									intTOUSBP += intNCLQT;
													intPRTSBP += intPRTQT;
													intPRDSBP += intPRDQT;
				  										
																
				  									intDOTCTG += intDOTQT;
													intDETCTG += intDETQT;
													intEXTCTG += intEXTQT;
													intDODCTG += intDODQT;
													intDEDCTG += intDEDQT;
													intEXDCTG += intEXDQT;
													intDOPCTG += intDOPQT;
													intDEPCTG += intDEPQT;
													intEXPCTG += intEXPQT;
				  									intTOCCTG += intCLSQT;
				  									intTOUCTG += intNCLQT;
													intPRTCTG += intPRTQT;
													intPRDCTG += intPRDQT;
													
													
													intDOTALL += intDOTQT;
													intDETALL += intDETQT;
													intEXTALL += intEXTQT;
													intDODALL += intDODQT;
													intDEDALL += intDEDQT;
													intEXDALL += intEXDQT;
													intDOPALL += intDOPQT;
													intDEPALL += intDEPQT;
													intEXPALL += intEXPQT;
				  									intTOCALL += intCLSQT;
				  									intTOUALL += intNCLQT;
													intPRTALL += intPRTQT;
													intPRDALL += intPRDQT;
				  										
																
				  									intDOTPRD += intDOTQT;
													intDETPRD += intDETQT;
													intEXTPRD += intEXTQT;
													intDODPRD += intDODQT;
													intDEDPRD += intDEDQT;
													intEXDPRD += intEXDQT;
													intDOPPRD += intDOPQT;
													intDEPPRD += intDEPQT;
													intEXPPRD += intEXPQT;
				  									intTOCPRD += intCLSQT;
				  									intTOUPRD += intNCLQT;
													intPRTPRD += intPRTQT;
													intPRDPRD += intPRDQT;
				  										
																
				  									if (!prpRSLSET.next())
				  									{
				  										flgEOF = true;
				  										break;
				  									}
																				
				  									strMNGRP2 = nvlSTRVL(prpRSLSET.getString("tm_prdtp"),"");
				  									strPRDCD2 = nvlSTRVL(prpRSLSET.getString("tm_prdcd"),"");
				  									strSBGRP2 = nvlSTRVL(prpRSLSET.getString("l_sbgrp"),"");
				  									strSTSFL2 = nvlSTRVL(prpRSLSET.getString("tm_stsfl"),"");
				  									strPRDDS2 = nvlSTRVL(prpRSLSET.getString("tm_prdds"),"");
													
													if(strSTSFL2.equals("4"))
														strSTSFL2 = "3";
																
													if(flgTB1FL){
														if(rdbZONWS.isSelected()){
															if(strCHP02.trim().length() > 0){
																if(strCHP02.equals("03")){
																    intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  													intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  													intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
																}else if(strCHP02.equals("01") || strCHP02.equals("02")){
																    intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  													intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  													intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
																	intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  													intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  													intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
																}
															}else{
																intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  												intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  												intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
																intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  												intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  												intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
																intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  												intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  												intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
															}
														}
														else if(rdbRGNWS.isSelected()){
															if(strCHP01.trim().length() > 0){
																if(strCHP01.equals("01") || strCHP01.equals("02")){
																	intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  													intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  													intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
																	intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  													intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  													intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
																}
																else if(strCHP01.equals("03")){
																    intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  													intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  													intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
																}
															}else{
																intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  												intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  												intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
																intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  												intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  												intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
																intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  												intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  												intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
															}
														}
													}
													else if(flgTB2FL){
														if(rdbDOMWS.isSelected()){
															if(chkDOMWS.isSelected()){
																intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  												intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  												intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
															}
															if(chkDEXWS.isSelected()){
																intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  												intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  												intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
															}
														}
														else if(rdbEXPWS.isSelected()){
															intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  											intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  											intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
														}
														else if(rdbALLWS.isSelected()){
															intDOTQT = Math.round(prpRSLSET.getFloat("l_dotqt"));
				  											intDODQT = Math.round(prpRSLSET.getFloat("l_dodqt"));
				  											intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
															intDETQT = Math.round(prpRSLSET.getFloat("l_detqt"));
				  											intDEDQT = Math.round(prpRSLSET.getFloat("l_dedqt"));
				  											intDEPQT = Math.round(prpRSLSET.getFloat("l_depqt"));
															intEXTQT = Math.round(prpRSLSET.getFloat("l_extqt"));
				  											intEXDQT = Math.round(prpRSLSET.getFloat("l_exdqt"));
				  											intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
															intPRTQT = Math.round(prpRSLSET.getFloat("l_prtqt"));
															intPRDQT = Math.round(prpRSLSET.getFloat("l_prdqt"));
														}
												intCLSQT = Math.round(prpRSLSET.getFloat("l_clsqt"));
				  								intNCLQT = Math.round(prpRSLSET.getFloat("l_nclqt"));
											}
														
											strSTSFL = strSTSFL2;
				  							strSBGRP = strSBGRP2;
				  							strPRDCD = strPRDCD2;
				  							strMNGRP = strMNGRP2;
				  							strPRDDS = strPRDDS2;
																			
				  							}
										prnGRPTOT("PR",intDOTPRD,intDETPRD,intEXTPRD,intDODPRD,intDEDPRD,intEXDPRD,intDOPPRD,intDEPPRD,intEXPPRD,intTOCPRD,intTOUPRD,intPRTPRD,intPRDPRD,6,"N");
				  						intGRPTOT("PR");
				  						}
									prnGRPTOT("CG",intDOTCTG,intDETCTG,intEXTCTG,intDODCTG,intDEDCTG,intEXDCTG,intDOPCTG,intDEPCTG,intEXPCTG,intTOCCTG,intTOUCTG,intPRTCTG,intPRDCTG,4,"B");
				  					intGRPTOT("CG");
				  					}
								prnGRPTOT("SG",intDOTSBP,intDETSBP,intEXTSBP,intDODSBP,intDEDSBP,intEXDSBP,intDOPSBP,intDEPSBP,intEXPSBP,intTOCSBP,intTOUSBP,intPRTSBP,intPRDSBP,2,"B");
				  				intGRPTOT("SG");
				  				}
							prnGRPTOT("MG",intDOTMNP,intDETMNP,intEXTMNP,intDODMNP,intDEDMNP,intEXDMNP,intDOPMNP,intDEPMNP,intEXPMNP,intTOCMNP,intTOUMNP,intPRTMNP,intPRDMNP,0,"B");
				  			intGRPTOT("MG");
				  			}
							prnGRPTOT("GT",intDOTALL,intDETALL,intEXTALL,intDODALL,intDEDALL,intEXDALL,intDOPALL,intDEPALL,intEXPALL,intTOCALL,intTOUALL,intPRTALL,intPRDALL,0,"B");
				  			crtLINE(intLINLEN);
							if(cl_dat.M_intLINNO_pbst > intLINENO)
							{
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("Booking/Despatch/Bkd:pnd.toGo is upto "+cl_dat.M_strLOGDT_pbst+" "+cl_dat.getCURTIM()+" Hrs.");
								prnFMTCHR(dosREPORT,M_strEJT);
							}
    				  			fosREPORT.flush();
    				  			dosREPORT.flush();
    				  			if(dosREPORT !=null)
    			     				dosREPORT.close();
    			     			
    				  			setMSG("",'N');
    				  		}else
    				  			setMSG("No Record exists.",'N');
    						}
    						else if(flgTB3FL){  
    							if(prpRSLSET.next()){
    				  				strMNGRP2 = nvlSTRVL(prpRSLSET.getString("tm_prdtp"),"");
    				  				strPRDCD2 = nvlSTRVL(prpRSLSET.getString("tm_prdcd"),"");
    				  				strSBGRP2 = nvlSTRVL(prpRSLSET.getString("l_sbgrp"),"");
    				  				strPRDDS2 = nvlSTRVL(prpRSLSET.getString("tm_prdds"),"");
    				  				strSTSFL2 = nvlSTRVL(prpRSLSET.getString("tm_stsfl"),"");
    								strPKGTP2 = nvlSTRVL(prpRSLSET.getString("tm_pkgtp"),"");
    								
    								if(strSTSFL2.equals("4"))
    									strSTSFL2 = "3";
    								
    								intCLSQT = Math.round(prpRSLSET.getFloat("l_clsqt"));
    				  				intNCLQT = Math.round(prpRSLSET.getFloat("l_nclqt"));
    								intTRGQT = Math.round(prpRSLSET.getFloat("l_resqt"));
    								intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
    								intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
    								
    									
    				  				while(!flgEOF){
    				  					if(cl_dat.M_intLINNO_pbst >= intLINENO)
    				  					{
    							            cl_dat.M_intLINNO_pbst = 0;
    							            cl_dat.M_PAGENO += 1;
    				  						prnHEADER(); //gets the Header of the report
    							       }
    				  					strMNGRP = strMNGRP2;
    				  					strSBGRP = strSBGRP2;
    				  					strSTSFL = strSTSFL2;
    				  					strPRDCD = strPRDCD2;
    				  					strPRDDS = strPRDDS2;
    									strPKGTP = strPKGTP2;
    									if(L_1STFL){
    				  						strMNGRP1 = strMNGRP;
    				  						strPRDCD1 = strPRDCD;
    				  						strSBGRP1 = strSBGRP;
    				  						strSTSFL1 = strSTSFL;
    				  						strPRDDS1 = strPRDDS;
    										strPKGTP1 = strPKGTP;
    										L_1STFL = false;
    				  						}
    				  					prnGRPHDR("MG",0);
    				  					strMNGRP1 = strMNGRP;
    				  					while((strMNGRP).equals(strMNGRP1) && !flgEOF){
    				  						prnGRPHDR("SG",2);
    				  						strMNGRP = strMNGRP2;
    				  						strMNGRP1 = strMNGRP;
    				  						while((strMNGRP+strSBGRP).equals(strMNGRP1+strSBGRP1) && !flgEOF){
    				  							prnGRPHDR("CG",4);
    				  							strMNGRP = strMNGRP2;
    				  							strMNGRP1 = strMNGRP;
    				  							strSBGRP = strSBGRP2;
    				  							strSBGRP1 = strSBGRP;
    				  							while((strMNGRP+strSBGRP+strSTSFL).equals(strMNGRP1+strSBGRP1+strSTSFL1) && !flgEOF){
    												prnGRPHDR("PK",6);
    				  								strMNGRP = strMNGRP2;
    				  								strMNGRP1 = strMNGRP;
    				  								strSBGRP = strSBGRP2;
    				  								strSBGRP1 = strSBGRP;
    				  								strSTSFL = strSTSFL2;
    				  								strSTSFL1 = strSTSFL;
    				  								while((strMNGRP+strSBGRP+strSTSFL+strPKGTP).equals(strMNGRP1+strSBGRP1+strSTSFL1+strPKGTP1) && !flgEOF){
    													strMNGRP = strMNGRP2;
    				  									strMNGRP1 = strMNGRP;
    				  									strSBGRP = strSBGRP2;
    				  									strSBGRP1 = strSBGRP;
    				  									strSTSFL = strSTSFL2;
    				  									strSTSFL1 = strSTSFL;
    													strPKGTP = strPKGTP2;
    				  									strPKGTP1 = strPKGTP;
    													while((strMNGRP+strSBGRP+strSTSFL+strPKGTP+strPRDCD).equals(strMNGRP1+strSBGRP1+strSTSFL1+strPKGTP1+strPRDCD1) && !flgEOF){
    													
    														intDOPMNP += intDOPQT;
    														intEXPMNP += intEXPQT;
    														intTOCMNP += intCLSQT;
    				  										intTOUMNP += intNCLQT;
    														intTORMNP += intTRGQT;
    				  											
    																	
    				  										intDOPSBP += intDOPQT;
    														intEXPSBP += intEXPQT;
    				  										intTOCSBP += intCLSQT;
    				  										intTOUSBP += intNCLQT;
    														intTORSBP += intTRGQT;
    				  											
    																	
    				  										intDOPCTG += intDOPQT;
    														intEXPCTG += intEXPQT;
    				  										intTOCCTG += intCLSQT;
    				  										intTOUCTG += intNCLQT;
    														intTORCTG += intTRGQT;
    				  											
    																	
    				  										intDOPALL += intDOPQT;
    														intEXPALL += intEXPQT;
    				  										intTOCALL += intCLSQT;
    				  										intTOUALL += intNCLQT;
    														intTORALL += intTRGQT;
    				  											
    																	
    				  										intDOPPRD += intDOPQT;
    														intEXPPRD += intEXPQT;
    				  										intTOCPRD += intCLSQT;
    				  										intTOUPRD += intNCLQT;
    														intTORPRD += intTRGQT;
    														
    														intDOPPKG += intDOPQT;
    														intEXPPKG += intEXPQT;
    				  										intTOCPKG += intCLSQT;
    				  										intTOUPKG += intNCLQT;
    														intTORPKG += intTRGQT;
    				  											
    																	
    				  										if (!prpRSLSET.next()){
    				  											flgEOF = true;
    				  											break;
    				  										}
    																					
    				  										strMNGRP2 = nvlSTRVL(prpRSLSET.getString("tm_prdtp"),"");
    				  										strPRDCD2 = nvlSTRVL(prpRSLSET.getString("tm_prdcd"),"");
    				  										strSBGRP2 = nvlSTRVL(prpRSLSET.getString("l_sbgrp"),"");
    				  										strSTSFL2 = nvlSTRVL(prpRSLSET.getString("tm_stsfl"),"");
    				  										strPRDDS2 = nvlSTRVL(prpRSLSET.getString("tm_prdds"),"");
    														strPKGTP2 = nvlSTRVL(prpRSLSET.getString("tm_pkgtp"),"");
    														
    														if(strSTSFL2.equals("4"))
    															strSTSFL2 = "3";
    																	
    														intCLSQT = Math.round(prpRSLSET.getFloat("l_clsqt"));
    				  										intNCLQT = Math.round(prpRSLSET.getFloat("l_nclqt"));
    														intTRGQT = Math.round(prpRSLSET.getFloat("l_resqt"));
    														intDOPQT = Math.round(prpRSLSET.getFloat("l_dopqt"));
    														intEXPQT = Math.round(prpRSLSET.getFloat("l_expqt"));
    															
    														strSTSFL = strSTSFL2;
    				  										strSBGRP = strSBGRP2;
    				  										strPRDCD = strPRDCD2;
    				  										strMNGRP = strMNGRP2;
    				  										strPRDDS = strPRDDS2;
    														strPKGTP = strPKGTP2;
    													}
    												prnGRPTOT("PR",intDOPPRD,intEXPPRD,intTOCPRD,intTOUPRD,intTORPRD,6,"N");
    				  								intGRPTOT("PR");
    												}
    											prnGRPTOT("PK",intDOPPKG,intEXPPKG,intTOCPKG,intTOUPKG,intTORPKG,6,"N");
    				  							intGRPTOT("PK");
    				  							}
    										prnGRPTOT("CG",intDOPCTG,intEXPCTG,intTOCCTG,intTOUCTG,intTORCTG,4,"B");
    				  						intGRPTOT("CG");
    				  						}
    									prnGRPTOT("SG",intDOPSBP,intEXPSBP,intTOCSBP,intTOUSBP,intTORSBP,2,"B");
    				  					intGRPTOT("SG");
    				  					}
    								prnGRPTOT("MG",intDOPMNP,intEXPMNP,intTOCMNP,intTOUMNP,intTORMNP,0,"B");
    				  				intGRPTOT("MG");
    				  				}
    							prnGRPTOT("GT",intDOPALL,intEXPALL,intTOCALL,intTOUALL,intTORALL,0,"B");
    				  			crtLINE(intLINLEN);
    							if(cl_dat.M_intLINNO_pbst > intLINENO)
    							{
    								dosREPORT.writeBytes("\n");
    								dosREPORT.writeBytes("Booking/Despatch/Bkd:pnd.toGO is upto "+cl_dat.M_strLOGDT_pbst+" "+cl_dat.getCURTIM()+" Hrs.");
    								prnFMTCHR(dosREPORT,M_strEJT);
    							}
    				  			fosREPORT.flush();
    				  			dosREPORT.flush();
    				  			if(dosREPORT !=null)
    			     				dosREPORT.close();
    			     			
    			     			
    				  			setMSG("",'N');
    				  		}else
    				  			setMSG("No Record exists.",'N');
    
    						}
						
						
						
					
	    	
			  if(intROWCNT > 0)
			  {
					cl_dat.M_flgCHKTB_pbst  = false;
					String L_TBLHD[] = null;
					int L_COLSZ[] = null;
					
					 
					if(flgTB1FL)
					{
					   
						if(rdbZONWS.isSelected())
						{
						    
							if(strCHP02.trim().length() > 0)
							{
							    
								if(strCHP02.equals("03"))
								{
								    
								    L_TBLHD = new String[]{"status" ,strHDR_PRDDS,strHDR_TRGQT,strHDR_DSPQT,strHDR_PDOQT,strHDR_BALQT,strHDR_PRDDS};
								    System.out.println("++++5" +L_TBLHD);
									L_COLSZ = new int[]{80,120,80,80,80,80,120};
							
									 
								}else if(strCHP02.equals("01") || strCHP02.equals("02")){
								    L_TBLHD = new String[]{strHDR_PRDDS,strHDR_DOMTG,strHDR_DEXTG,strHDR_DOMDS,strHDR_DEXDS,strHDR_DOMPD,strHDR_DEXPD,strHDR_DOMBL,strHDR_DEXBL,strHDR_PRDDS};
									L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80,120};
							
									
							
								}
							}else{
								L_TBLHD = new String[]{strHDR_PRDDS,strHDR_DOMTG,strHDR_DEXTG,strHDR_EXPTG,strHDR_DOMDS,strHDR_DEXDS,strHDR_EXPDS,strHDR_DOMPD,strHDR_DEXPD,strHDR_EXPPD,strHDR_DOMBL,strHDR_DEXBL,strHDR_EXPBL,strHDR_PRDDS};
								L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80,80,80,80,80,120};
							
							}
							 
						}
						else if(rdbRGNWS.isSelected())
						{
							if(strCHP01.trim().length() > 0)
							{
								if(strCHP01.equals("01") || strCHP01.equals("02"))
								{
									L_TBLHD = new String[]{strHDR_PRDDS,strHDR_DOMTG,strHDR_DEXTG,strHDR_DOMDS,strHDR_DEXDS,strHDR_DOMPD,strHDR_DEXPD,strHDR_DOMBL,strHDR_DEXBL,strHDR_PRDDS};
									L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80,120};
								
								
								
								}
								else if(strCHP01.equals("03"))
								{
								    L_TBLHD = new String[]{strHDR_PRDDS,strHDR_TRGQT,strHDR_DSPQT,strHDR_PDOQT,strHDR_BALQT,strHDR_PRDDS};
									L_COLSZ = new int[]{120,80,80,80,80,120};
								
								
								
								}
							}else{
								L_TBLHD = new String[]{strHDR_PRDDS,strHDR_DOMTG,strHDR_DEXTG,strHDR_EXPTG,strHDR_DOMDS,strHDR_DEXDS,strHDR_EXPDS,strHDR_DOMPD,strHDR_DEXPD,strHDR_EXPPD,strHDR_DOMBL,strHDR_DEXBL,strHDR_EXPBL,strHDR_PRDDS};
								L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80,80,80,80,80,120};
								
								
								
							}
						}
						
						setMatrix(20,8);
						pnlSLT02.setLayout( null);
						pnlSLT02.removeAll();
						pnlSLT02.updateUI();
						
						tblSLTRG = crtTBLPNL1(pnlSLT02,L_TBLHD,intROWCNT,3,1,10,7.6,L_COLSZ,new int[]{0});
						tblSLTRG.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						if(strCHP02.equals("03") || strCHP01.equals("03"))
						{
						    tblSLTRG.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
						  
							tblSLTRG.getColumn(strHDR_TRGQT).setCellRenderer(LM_DFTCRN);
							
							tblSLTRG.getColumn(strHDR_DSPQT).setCellRenderer(LM_DFTCRN);
							
		  					tblSLTRG.getColumn(strHDR_PDOQT).setCellRenderer(LM_DFTCRN);
		  					
							tblSLTRG.getColumn(strHDR_BALQT).setCellRenderer(LM_DFTCRN);
							
							tblSLTRG.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
							
						}else if(strCHP02.equals("01") || strCHP02.equals("02") || strCHP01.equals("01") || strCHP01.equals("02")){
						    tblSLTRG.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
							tblSLTRG.getColumn(strHDR_DOMTG).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXTG).setCellRenderer(LM_DFTCRN);
		  					tblSLTRG.getColumn(strHDR_DOMDS).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXDS).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DOMPD).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXPD).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DOMBL).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXBL).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
						}
						else{
							tblSLTRG.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
							tblSLTRG.getColumn(strHDR_DOMTG).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXTG).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_EXPTG).setCellRenderer(LM_DFTCRN);
		  					tblSLTRG.getColumn(strHDR_DOMDS).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXDS).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_EXPDS).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DOMPD).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXPD).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_EXPPD).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DOMBL).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_DEXBL).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_EXPBL).setCellRenderer(LM_DFTCRN);
							tblSLTRG.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
						}
						
						add(pnlSLT02,1,1,18.5,7.8,pnlSLTRG,'L');
						pnlSLTRG.updateUI();
						
						StringTokenizer stkALLREC;
		  				int j = 0;
						for(int L_ROWCNT = 0;L_ROWCNT < intROWCNT;L_ROWCNT++)
						{
		  					j = 0;
							stkALLREC = new StringTokenizer(hstALLREC.get(String.valueOf(L_ROWCNT)).toString(),"|");
							System.out.println(stkALLREC);
		  					if(stkALLREC.countTokens() > 0)
		  					{
		  						while(stkALLREC.hasMoreTokens())
		  						{
		  							tblSLTRG.setValueAt(stkALLREC.nextToken(),L_ROWCNT,j);
		  							j++;
		  						}
		  					}
						}
						
					}
					else if(flgTB2FL){
						if(chkDOMWS.isSelected() && chkDEXWS.isSelected()){
							L_TBLHD = new String[]{strHDR_PRDDS,strHDR_DOMTG,strHDR_DEXTG,strHDR_DOMDS,strHDR_DEXDS,strHDR_DOMPD,strHDR_DEXPD,strHDR_DOMBL,strHDR_DEXBL,strHDR_CLSQT,strHDR_NCLQT,strHDR_BLSQT,strHDR_PRDDS};
							L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80,80,80,80,120};
						}else{
							if(rdbALLWS.isSelected()){
								if(rdbSUMRY.isSelected()){
									L_TBLHD = new String[]{strHDR_PRDDS,strHDR_TRGQT,strHDR_DSPQT,strHDR_PDOQT,strHDR_BALQT,strHDR_PRTQT,strHDR_PRDQT,strHDR_TPRQT,strHDR_CLSQT,strHDR_NCLQT,strHDR_BLSQT,strHDR_PRDDS};
									L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80,80,80,120};
								}
								else if(rdbDETAL.isSelected()){
									L_TBLHD = new String[]{strHDR_PRDDS,strHDR_DOMTG,strHDR_DEXTG,strHDR_EXPTG,strHDR_DOMDS,strHDR_DEXDS,strHDR_EXPDS,strHDR_DOMPD,strHDR_DEXPD,strHDR_EXPPD,strHDR_DOMBL,strHDR_DEXBL,strHDR_EXPBL,strHDR_CLSQT,strHDR_NCLQT,strHDR_BLSQT,strHDR_PRDDS};
									L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,120};
								}
							}else{
								L_TBLHD = new String[]{strHDR_PRDDS,strHDR_TRGQT,strHDR_DSPQT,strHDR_PDOQT,strHDR_BALQT,strHDR_CLSQT,strHDR_NCLQT,strHDR_BLSQT,strHDR_PRDDS};
								L_COLSZ = new int[]{120,80,80,80,80,80,80,80,120};
							}
						}
						setMatrix(20,8);
						pnlSTA02.setLayout( null);
						pnlSTA02.removeAll();
						pnlSTA02.updateUI();
						tblSTKAV = crtTBLPNL1(pnlSTA02,L_TBLHD,intROWCNT,3,1,10,7.6,L_COLSZ,new int[]{0});
						tblSTKAV.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						
						if(chkDOMWS.isSelected() && chkDEXWS.isSelected())
						{
							tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
							tblSTKAV.getColumn(strHDR_DOMTG).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_DEXTG).setCellRenderer(LM_DFTCRN);
		  					tblSTKAV.getColumn(strHDR_DOMDS).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_DEXDS).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_DOMPD).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_DEXPD).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_DOMBL).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_DEXBL).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_CLSQT).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_NCLQT).setCellRenderer(LM_DFTCRN);
							tblSTKAV.getColumn(strHDR_BLSQT).setCellRenderer(new rowRenderer1());
							tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
						}else
						{
							if(rdbALLWS.isSelected())
							{
								if(rdbSUMRY.isSelected())
								{
									tblSTKAV.getColumn(strHDR_PRTQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_PRDQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_TPRQT).setCellRenderer(LM_DFTCRN);
		  							tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
									tblSTKAV.getColumn(strHDR_TRGQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_DSPQT).setCellRenderer(LM_DFTCRN);
		  							tblSTKAV.getColumn(strHDR_PDOQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_BALQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_CLSQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_NCLQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_BLSQT).setCellRenderer(new rowRenderer1());
									tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
								}
								else if(rdbDETAL.isSelected())
								{
									tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
									tblSTKAV.getColumn(strHDR_DOMTG).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_DEXTG).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_EXPTG).setCellRenderer(LM_DFTCRN);
		  							tblSTKAV.getColumn(strHDR_DOMDS).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_DEXDS).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_EXPDS).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_DOMPD).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_DEXPD).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_EXPPD).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_DOMBL).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_DEXBL).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_EXPBL).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_CLSQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_NCLQT).setCellRenderer(LM_DFTCRN);
									tblSTKAV.getColumn(strHDR_BLSQT).setCellRenderer(new rowRenderer1());
									tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
								}
							}
							else if(rdbEXPWS.isSelected())
							{
								tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
								tblSTKAV.getColumn(strHDR_TRGQT).setCellRenderer(LM_DFTCRN);
								tblSTKAV.getColumn(strHDR_DSPQT).setCellRenderer(LM_DFTCRN);
		  						tblSTKAV.getColumn(strHDR_PDOQT).setCellRenderer(LM_DFTCRN);
								tblSTKAV.getColumn(strHDR_BALQT).setCellRenderer(LM_DFTCRN);
								tblSTKAV.getColumn(strHDR_CLSQT).setCellRenderer(LM_DFTCRN);
								tblSTKAV.getColumn(strHDR_NCLQT).setCellRenderer(LM_DFTCRN);
								tblSTKAV.getColumn(strHDR_BLSQT).setCellRenderer(new rowRenderer1());
								tblSTKAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
							}
						}
						add(pnlSTA02,1,1,18.5,7.8,pnlSTKAV,'L');
					
						pnlSTKAV.updateUI();
						StringTokenizer stkALLREC;
		  				int j = 0;
						for(int L_ROWCNT = 0;L_ROWCNT < intROWCNT;L_ROWCNT++)
						{
		  					j = 0;
		  					stkALLREC = new StringTokenizer(hstALLREC.get(String.valueOf(L_ROWCNT)).toString(),"|");
		  					if(stkALLREC.countTokens() > 0)
		  					{
		  						while(stkALLREC.hasMoreTokens())
		  						{
		  							tblSTKAV.setValueAt(stkALLREC.nextToken(),L_ROWCNT,j);
		  							j++;
		  						}
		  					}
						}
					}
					else if(flgTB3FL)
					{
						L_TBLHD = new String[]{strHDR_PRDDS,strHDR_CLSQT,strHDR_TRGQT,strHDR_NCLQT,strHDR_TOTST,strHDR_DOMPD,strHDR_STADO,strHDR_EXPPD,strHDR_BLSQT};
						L_COLSZ = new int[]{120,80,80,80,80,80,80,80,80};
						setMatrix(20,8);
						pnlGST01.setLayout(null);
						pnlGST01.removeAll();
						pnlGST01.updateUI();
						tblGSTAV = crtTBLPNL1(pnlGSTAV,L_TBLHD,intROWCNT,3,1,11,7.6,L_COLSZ,new int[]{0});
						tblGSTAV.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						tblGSTAV.getColumn(strHDR_PRDDS).setCellRenderer(new rowRenderer4());
						tblGSTAV.getColumn(strHDR_CLSQT).setCellRenderer(LM_DFTCRN);
						tblGSTAV.getColumn(strHDR_TRGQT).setCellRenderer(LM_DFTCRN);
		  				tblGSTAV.getColumn(strHDR_NCLQT).setCellRenderer(LM_DFTCRN);
						tblGSTAV.getColumn(strHDR_TOTST).setCellRenderer(LM_DFTCRN);
						tblGSTAV.getColumn(strHDR_DOMPD).setCellRenderer(LM_DFTCRN);
						tblGSTAV.getColumn(strHDR_STADO).setCellRenderer(LM_DFTCRN);
						tblGSTAV.getColumn(strHDR_EXPPD).setCellRenderer(LM_DFTCRN);
						tblGSTAV.getColumn(strHDR_BLSQT).setCellRenderer(new rowRenderer1());
						add(pnlGST01,1,1,18.5,7.8,pnlGSTAV,'L');
						pnlGSTAV.updateUI();
						StringTokenizer stkALLREC;
		  				int j = 0;
						for(int L_ROWCNT = 0;L_ROWCNT < intROWCNT;L_ROWCNT++){
		  					j = 0;
		  					stkALLREC = new StringTokenizer(hstALLREC.get(String.valueOf(L_ROWCNT)).toString(),"|");
		  					if(stkALLREC.countTokens() > 0){
		  						while(stkALLREC.hasMoreTokens()){
		  							tblGSTAV.setValueAt(stkALLREC.nextToken(),L_ROWCNT,j);
		  							j++;
		  						}
		  					}
						}
					}
					setMSG(" ",'N');
			  }
					 btnQUERY.setEnabled(false);
			
						    
						    
			
	    	}else
	    	 
	    	    setMSG("Data Not Found",'N');
	    	
				        
				        
	        
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX,"This is the getaalrec");
	    }
	}
	    
	    
	
	    
	
	
	
	
	public void crtLINE(int LM_CNT)
	{
		String strln = " ";
		try{
			for(int i=1;i<=LM_CNT;i++){
				strln += "-";
			}
			StringBuffer L_CRTLIN = new StringBuffer(padSTRING('L'," ",0));
			L_CRTLIN.append(strln);
			dosREPORT.writeBytes(L_CRTLIN.toString().trim());
		}catch(Exception L_EX){
			setMSG(L_EX,"crtLINE");
		}
	}
	
     		   	 
     		   	 
	
	/**
	 * @return void
	 * Prints the Header of the report when the report is displayed or printed
	 * for the first time.
	 */
     private void prnHEADER(){  //gets the Header of the Report
		try{
		    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,30)+padSTRING('L',"Report Date :"+cl_dat.M_strLOGDT_pbst,(intLINLEN-30))+"\n");
            if(flgTB1FL || flgTB2FL){
				String L_TTLSTR = "";
				StringBuffer L_HDRSTR = new StringBuffer(padSTRING('R'," ",6));
				StringBuffer L_TRGSTR = new StringBuffer("");
				StringBuffer L_DSPSTR = new StringBuffer("");
				StringBuffer L_PNDSTR = new StringBuffer("");
				StringBuffer L_BALSTR = new StringBuffer("");
				StringBuffer L_PRDSTR = new StringBuffer("");
				if(flgTB1FL){
					L_TTLSTR = "Monitoring of Sales Target for the month of "+hstMTHDSC.get(strMONTH).toString().trim()+" "+strYEAR;
					cl_dat.M_intLINNO_pbst += 1;
					if(rdbZONWS.isSelected()){
						if(strCHP02.trim().length() > 0){
							L_TTLSTR += " Zone "+strSPEFC;
							if(strCHP02.equals("03")){
								L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',"Target",intSLTLEN)+ padSTRING('L',"Despatch",intSLTLEN)+ padSTRING('L',"Bkd:pnd.toGO",intSLTLEN)+ padSTRING('L',"Bal.toBK",intSLTLEN)+"\n");
								L_TRGSTR.append(padSTRING('L',"Export",intCLMLEN));
								L_DSPSTR.append(padSTRING('L',"Export",intCLMLEN));
								L_PNDSTR.append(padSTRING('L',"Export",intCLMLEN));
								L_BALSTR.append(padSTRING('L',"Export",intCLMLEN));
							}else if(strCHP02.equals("01") || strCHP02.equals("02")){
								L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',getARROWS("Target",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Despatch",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bkd:pnd.toGO",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bal.toBK",intSLTLEN),intSLTLEN)+"\n");
							    L_TRGSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
								L_DSPSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
								L_PNDSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
								L_BALSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
							}
						}else{
							L_TTLSTR += " All Zones ";
							L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',getARROWS("Target",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Despatch",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bkd:pnd.toGO",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bal,toBK",intSLTLEN),intSLTLEN)+"\n");
							L_TRGSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
							L_DSPSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
							L_PNDSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
							L_BALSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
						}
					}
					else if(rdbRGNWS.isSelected()){
						if(strCHP01.trim().length() > 0){
							L_TTLSTR += " Region "+strSPEFC;
							if(strCHP01.equals("01") || strCHP01.equals("02")){
								L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',getARROWS("Target",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Despatch",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bkd:pnd.toGO",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bal.toBK",intSLTLEN),intSLTLEN)+"\n");
								L_TRGSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
								L_DSPSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
								L_PNDSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
								L_BALSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN));
							}
							else if(strCHP01.equals("03")){
								L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',"Target",intSLTLEN)+ padSTRING('L',"Despatch",intSLTLEN)+ padSTRING('L',"Bkd:pnd.toGO",intSLTLEN)+ padSTRING('L',"Bal.toBK",intSLTLEN)+"\n");
							    L_TRGSTR.append(padSTRING('L',"Export",intCLMLEN));
								L_DSPSTR.append(padSTRING('L',"Export",intCLMLEN));
								L_PNDSTR.append(padSTRING('L',"Export",intCLMLEN));
								L_BALSTR.append(padSTRING('L',"Export",intCLMLEN));
							}
						}else{
							L_TTLSTR += " All Regions ";
							L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',getARROWS("Target",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Despatch",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bkd:pnd.toGO",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bal.toBK",intSLTLEN),intSLTLEN)+"\n");
							L_TRGSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
							L_DSPSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
							L_PNDSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
							L_BALSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN));
						}
					}
				}
				else if(flgTB2FL){
					L_TTLSTR = "Target v/s Stock Availability";
					if(rdbDOMWS.isSelected()){
						if(chkDOMWS.isSelected() && chkDEXWS.isSelected()){
							L_TTLSTR += " Details of "+strPRTDS+" for Domestic & Deemed Export";
							L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',getARROWS("Target",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Despatch",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bkd:pnd.toGO",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bal.toBK",intSLTLEN),intSLTLEN)+ padSTRING('L',"Clsfd.",intSTKLEN)+ padSTRING('L',"Unclsfd.",intSTKLEN)+ padSTRING('L',"Closing",intSTKLEN)+"\n");
							cl_dat.M_intLINNO_pbst += 1;
						}else{
							if(chkDOMWS.isSelected())
								L_TTLSTR += " Details of "+strPRTDS+" for Domestic";
							else if(chkDEXWS.isSelected())
								L_TTLSTR += " Details of "+strPRTDS+" for Deemed Export";
							L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',"Target",intSLTLEN)+ padSTRING('L',"Despatch",intSLTLEN)+ padSTRING('L',"Bkd:pnd.toGO",intSLTLEN)+ padSTRING('L',"Bal.toBK",intSLTLEN)+ padSTRING('L',"Clsfd.",intSTKLEN)+ padSTRING('L',"Unclsfd.",intSTKLEN)+ padSTRING('L',"Closing",intSTKLEN)+"\n");
							cl_dat.M_intLINNO_pbst += 1;
						}
						if(chkDOMWS.isSelected()){
							L_TRGSTR.append(padSTRING('L',"Dom.",intCLMLEN));
							L_DSPSTR.append(padSTRING('L',"Dom.",intCLMLEN));
							L_PNDSTR.append(padSTRING('L',"Dom.",intCLMLEN));
							L_BALSTR.append(padSTRING('L',"Dom.",intCLMLEN));
						}
						if(chkDEXWS.isSelected()){
							L_TRGSTR.append(padSTRING('L',"D.Exp.",intCLMLEN));
							L_DSPSTR.append(padSTRING('L',"D.Exp.",intCLMLEN));
							L_PNDSTR.append(padSTRING('L',"D.Exp.",intCLMLEN));
							L_BALSTR.append(padSTRING('L',"D.Exp.",intCLMLEN));
						}
						/*if(chkDOMWS.isSelected() && chkDEXWS.isSelected()){
							L_TRGSTR.append(padSTRING('L',"Total",intTOTLEN));
							L_DSPSTR.append(padSTRING('L',"Total",intTOTLEN));
							L_PNDSTR.append(padSTRING('L',"Total",intTOTLEN));
							L_BALSTR.append(padSTRING('L',"Total",intTOTLEN));
						}*/
					}
					else if(rdbEXPWS.isSelected()){
						L_TTLSTR += " Details of "+strPRTDS+" for Export";
						L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',"Target",intSTKLEN)+ padSTRING('L',"Despatch",intSTKLEN)+ padSTRING('L',"Bkd:pnd.toGO",intSTKLEN)+ padSTRING('L',"Bal.toBK",intSTKLEN)+ padSTRING('L',"Clsfd.",intSTKLEN)+ padSTRING('L',"Unclsfd.",intSTKLEN)+ padSTRING('L',"Closing",intSTKLEN)+"\n");
						cl_dat.M_intLINNO_pbst += 1;
						L_TRGSTR.append(padSTRING('L',"Export",intSTKLEN));
						L_DSPSTR.append(padSTRING('L',"Export",intSTKLEN));
						L_PNDSTR.append(padSTRING('L',"Export",intSTKLEN));
						L_BALSTR.append(padSTRING('L',"Export",intSTKLEN));
					}
					else if(rdbALLWS.isSelected()){
						if(rdbSUMRY.isSelected()){
							L_TTLSTR += " Summary of "+strPRTDS+" for the month of "+hstMTHDSC.get(strMONTH).toString().trim()+" "+strYEAR;
							L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',"Target",intSTKLEN)+ padSTRING('L',"Despatch",intSTKLEN)+ padSTRING('L',"Bkd:pnd.toGO",intSTKLEN)+ padSTRING('L',"Bal.toBK",intSTKLEN)+ padSTRING('L',"Production",intSTKLEN)+ padSTRING('L',"Produced",intSTKLEN)+ padSTRING('L',"Bkd:pnd.toGO",intSTKLEN)+ padSTRING('L',"Clsfd.",intSTKLEN)+ padSTRING('L',"Unclsfd.",intSTKLEN)+ padSTRING('L',"Closing",intSTKLEN)+"\n");
							cl_dat.M_intLINNO_pbst += 1;
							L_TRGSTR.append(padSTRING('L',"Total",intSTKLEN));
							L_DSPSTR.append(padSTRING('L',"Total",intSTKLEN));
							L_PNDSTR.append(padSTRING('L',"Total",intSTKLEN));
							L_BALSTR.append(padSTRING('L',"Total",intSTKLEN));
							L_PRDSTR.append(padSTRING('L',"Target",intSTKLEN)+padSTRING('L',"Qty.",intSTKLEN)+padSTRING('L',"Production",intSTKLEN));
						}
						else if(rdbDETAL.isSelected()){
							L_TTLSTR += " Detail of "+strPRTDS+" for the month of "+hstMTHDSC.get(strMONTH).toString().trim()+" "+strYEAR;
							L_HDRSTR.append(padSTRING('R',"Grade",LM_GRDLEN-6)+ padSTRING('L',getARROWS("Target",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Despatch",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bkd:pnd.toGO",intSLTLEN),intSLTLEN)+ padSTRING('L',getARROWS("Bal.toBK",intSLTLEN),intSLTLEN)+ padSTRING('L',"Clsfd.",intSTKLEN)+ padSTRING('L',"Unclsfd.",intSTKLEN)+ padSTRING('L',"Closing",intSTKLEN)+"\n");
							cl_dat.M_intLINNO_pbst += 1;
							L_TRGSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp",intCLMLEN)+padSTRING('L',"Exp.",intCLMLEN));
							L_DSPSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp",intCLMLEN)+padSTRING('L',"Exp.",intCLMLEN));
							L_PNDSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp",intCLMLEN)+padSTRING('L',"Exp.",intCLMLEN));
							L_BALSTR.append(padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"D.Exp",intCLMLEN)+padSTRING('L',"Exp.",intCLMLEN));
						}
					}
				}
				dosREPORT.writeBytes(padSTRING('R',L_TTLSTR,90)+padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,(intLINLEN-99))+"\n");
				crtLINE(intLINLEN);
				dosREPORT.writeBytes("\n"+L_HDRSTR.toString());
				dosREPORT.writeBytes(padSTRING('R'," ",LM_GRDLEN)+L_TRGSTR.toString()+L_DSPSTR.toString()+L_PNDSTR.toString()+L_BALSTR.toString()+L_PRDSTR.toString()+"\n");
			}
			else if(flgTB3FL){
			    dosREPORT.writeBytes(padSTRING('R',"Daily Gradewise Availability Position for the month of "+hstMTHDSC.get(strMONTH).toString().trim(),90)+padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,(intLINLEN-99))+"\n");
				crtLINE(intLINLEN);
				dosREPORT.writeBytes("\n"+padSTRING('R',"Grade",LM_GRDLEN)+padSTRING('L',"Today's",intCLMLEN)+padSTRING('L',"Target",intCLMLEN)+padSTRING('L',"Unclassified",intCLMLEN)+padSTRING('L',"Stock",intCLMLEN)+padSTRING('L',"Bkd:Pnd.toGO",intCLMLEN)+padSTRING('L',"Avlbl. after",intCLMLEN)+padSTRING('L',"Bkd:pnd.toGO",intCLMLEN)+padSTRING('L',"Bal.toBK",intCLMLEN));
				dosREPORT.writeBytes("\n"+padSTRING('R'," ",intCLMLEN)+padSTRING('L',"Clsfd. Stock",intCLMLEN)+padSTRING('L'," ",intCLMLEN)+padSTRING('L'," ",intCLMLEN)+padSTRING('L',"Available",intCLMLEN)+padSTRING('L',"Dom.",intCLMLEN)+padSTRING('L',"Dom. Bkg.",intCLMLEN)+padSTRING('L',"Export",intCLMLEN)+padSTRING('L'," ",intCLMLEN)+"\n");
			}
            crtLINE(intLINLEN);
            dosREPORT.writeBytes("\n");
            dosREPORT.writeBytes("</B>");
            cl_dat.M_intLINNO_pbst += 7;
		}catch(Exception L_EX){
           setMSG(L_EX,"prnHEADER");
		}
	}
    
     
	 
	 private String getARROWS(String LP_PARSTR,int LP_PADLEN){
		 String L_RTNSTR = "";
		 try{
			 int L_STRLEN = LP_PARSTR.length();
			 if(LP_PADLEN > L_STRLEN){
				 int L_DIFFLN = LP_PADLEN - L_STRLEN; //Difference length
				 if(L_DIFFLN >= 4){
					 int L_LOPLEN = L_DIFFLN/2;  //length for the for loop to iterate
					 String L_LINSTR = "-";
					 for(int i = 1; i < L_LOPLEN-1; i++)
						 L_LINSTR += "-";
					 L_RTNSTR += "<" + L_LINSTR + LP_PARSTR + L_LINSTR + ">";
				 }else
					 L_RTNSTR = LP_PARSTR;
			 }else
				 L_RTNSTR = LP_PARSTR;
		 }catch(Exception L_EX){
			 setMSG(L_EX,"getARROWS");
		 }
		 return L_RTNSTR;
	 }

	 
	
	private void prnGRPHDR(String LP_GRPCT, int LP_CLMGAP)
	{
		String L_GRPDS = "";
		String L_HSTVL = "";
		try{
			if (LP_GRPCT.equals("CG"))
			{
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				System.out.println("strSTSFL1" +strSTSFL1);
                L_GRPDS = cl_dat.getPRMCOD("cmt_codds","STS","MRXXSTS",strSTSFL1);
                System.out.println("this is prnGRPHdr :" +L_GRPDS);
			}
			else if (LP_GRPCT.equals("PK")){
				hstPRDDS.put(String.valueOf(intROWCNT),"");
                L_GRPDS = cl_dat.getPRMCOD("cmt_codds","SYS","FGXXPKG",strPKGTP1);
                System.out.println("this is prnGRPHdr1 :"+L_GRPDS);
			}
			else if (LP_GRPCT.equals("SG")){
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = getSBPRD(strSBGRP1);
				System.out.println("this is prnGRPHdr2 :" +L_GRPDS);
			}
			else if (LP_GRPCT.equals("MG")){
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = getMNPRD(strMNGRP1);
				System.out.println("this is prnGRPHDR2" +L_GRPDS);
			}
		L_HSTVL = L_GRPDS + "|" + " " + "|" + " " + "|" + " " + "|" + " " + "|" + " ";
			dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',L_GRPDS,LM_GRDLEN-LP_CLMGAP)+"\n");
			dosREPORT.writeBytes("</B>");
            cl_dat.M_intLINNO_pbst += 1;
			hstALLREC.put(String.valueOf(intROWCNT),L_HSTVL);
			intROWCNT++;			
			if(cl_dat.M_intLINNO_pbst >= intLINENO){
                crtLINE(intLINLEN);
                dosREPORT.writeBytes("\n");
				if(flgTB1FL || flgTB2FL)
				    dosREPORT.writeBytes("Booking/Despatch/Bkd:pnd.toGO is upto "+cl_dat.M_strLOGDT_pbst+" "+cl_dat.getCURTIM()+" Hrs.");
				else
				    dosREPORT.writeBytes(cl_dat.getCURTIM()+" Hrs.");
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				cl_dat.M_intLINNO_pbst = 0;
                cl_dat.M_PAGENO += 1;
				prnHEADER(); //gets the Header of the report*
				prnFMTCHR(dosREPORT,M_strEJT);
            }
	}catch(Exception L_EX){
		setMSG(L_EX,"prnGRPHDR");
		}
	}
	
	private void prnGRPTOT(String LP_GRPCT,int LP_DOTXXX,int LP_DETXXX,int LP_EXTXXX,int LP_DODXXX,int LP_DEDXXX,int LP_EXDXXX,int LP_DOPXXX,int LP_DEPXXX,int LP_EXPXXX,int LP_TOCXXX,int LP_TOUXXX,int LP_PRTXXX,int LP_PRDXXX,int LP_CLMGAP,String LP_BLDFL)
	{
		String L_GRPDS = "";
		String L_HSTVL = "";
		boolean L_FNTFL = false;
		try
		{
			int L_DODPER = 0;
			int L_DEDPER = 0;
			int L_EXDPER = 0;
			int L_DOPPER = 0;
			int L_DEPPER = 0;
			int L_EXPPER = 0;
			int L_DOBPER = 0;
			int L_DEBPER = 0;
			int L_EXBPER = 0;
			int L_TOTXXX = LP_DOTXXX + LP_DETXXX + LP_EXTXXX;
			int L_TODXXX = LP_DODXXX + LP_DEDXXX + LP_EXDXXX;
			int L_TOPXXX = LP_DOPXXX + LP_DEPXXX + LP_EXPXXX;
			int L_DOMBAL = LP_DOTXXX - LP_DODXXX - LP_DOPXXX;
			int L_DEXBAL = LP_DETXXX - LP_DEDXXX - LP_DEPXXX;
			int L_EXPBAL = LP_EXTXXX - LP_EXDXXX - LP_EXPXXX;
			int L_TOBXXX = L_DOMBAL + L_DEXBAL + L_EXPBAL;
			int L_TOTSTK = LP_TOCXXX + LP_TOUXXX;
			int L_STKXXX = L_TOTSTK - L_TOBXXX;
			int L_PRDXXX = LP_PRTXXX - LP_PRDXXX;
			if(rdbALLWS.isSelected()){
				if(rdbSUMRY.isSelected())
					L_STKXXX = (L_TOTSTK + L_PRDXXX) - (L_TOPXXX + L_TOBXXX);
			}
			if (LP_GRPCT.equals("PR")){
				L_GRPDS = strPRDDS1;
			}
			else if (LP_GRPCT.equals("CG")){
			    dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
                L_GRPDS = "Total "+cl_dat.getPRMCOD("cmt_codds","STS","MRXXSTS",strSTSFL1);
			}
			else if (LP_GRPCT.equals("SG")){
			    dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = "Total "+getSBPRD(strSBGRP1);
			}
			else if (LP_GRPCT.equals("MG")){
			    dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = "Total "+getMNPRD(strMNGRP1);
			}
			else if (LP_GRPCT.equals("GT")){
				if(LP_DOTXXX != 0){
					L_DODPER = (LP_DODXXX*100)/LP_DOTXXX;
					L_DOPPER = (LP_DOPXXX*100)/LP_DOTXXX;
					L_DOBPER = (L_DOMBAL*100)/LP_DOTXXX;
				}
				if(LP_DETXXX != 0){
					L_DEDPER = (LP_DEDXXX*100)/LP_DETXXX;
					L_DEPPER = (LP_DEPXXX*100)/LP_DETXXX;
					L_DEBPER = (L_DEXBAL*100)/LP_DETXXX;
				}
				if(LP_EXTXXX != 0){
					L_EXDPER = (LP_EXDXXX*100)/LP_EXTXXX;
					L_EXPPER = (LP_EXPXXX*100)/LP_EXTXXX;
					L_EXBPER = (L_EXPBAL*100)/LP_EXTXXX;
				}
				dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = "Grand Total";
			}
			if(L_STKXXX < 0){
				L_FNTFL = true;
				hstDOPQT.put(String.valueOf(intROWCNT),"");
			}
			L_HSTVL = L_GRPDS + "|";
			StringBuffer L_TRGSTR = new StringBuffer("");
			StringBuffer L_DSPSTR = new StringBuffer("");
			StringBuffer L_PNDSTR = new StringBuffer("");
			StringBuffer L_BALSTR = new StringBuffer("");
			StringBuffer L_PRDSTR = new StringBuffer("");
			StringBuffer L_DSPPER = new StringBuffer("");
			StringBuffer L_PNDPER = new StringBuffer("");
			StringBuffer L_BALPER = new StringBuffer("");
			StringBuffer L_PRDPER = new StringBuffer("");
			StringBuffer L_TRGPER = new StringBuffer("");
			String L_HSTTRG = "";
			String L_HSTDSP = "";
			String L_HSTPND = "";
			String L_HSTBAL = "";
			String L_HSTPRD = "";
			if(flgTB1FL){
				if(rdbZONWS.isSelected()){
					if(strCHP02.trim().length() > 0){
						if(strCHP02.equals("03")){
						    L_TRGSTR.append(padSTRING('L',String.valueOf(LP_EXTXXX),intSTKLEN));
							L_DSPSTR.append(padSTRING('L',String.valueOf(LP_EXDXXX),intSTKLEN));
							L_PNDSTR.append(padSTRING('L',String.valueOf(LP_EXPXXX),intSTKLEN));
							L_BALSTR.append(padSTRING('L',String.valueOf(L_EXPBAL),intSTKLEN));
							L_TRGPER.append(padSTRING('L'," ",intSTKLEN));
							L_DSPPER.append(padSTRING('L',String.valueOf(L_EXDPER)+" %",intSTKLEN));
							L_PNDPER.append(padSTRING('L',String.valueOf(L_EXPPER)+" %",intSTKLEN));
							L_BALPER.append(padSTRING('L',String.valueOf(L_EXBPER)+" %",intSTKLEN));
							L_HSTTRG += String.valueOf(LP_EXTXXX)+ "|";
							L_HSTDSP += String.valueOf(LP_EXDXXX)+ "|";
							L_HSTPND += String.valueOf(LP_EXPXXX)+ "|";
							L_HSTBAL += String.valueOf(L_EXPBAL)+ "|";
						}else if(strCHP02.equals("01") || strCHP02.equals("02")){
						    L_TRGSTR.append(padSTRING('L',String.valueOf(LP_DOTXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DETXXX),intCLMLEN));
							L_DSPSTR.append(padSTRING('L',String.valueOf(LP_DODXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEDXXX),intCLMLEN));
							L_PNDSTR.append(padSTRING('L',String.valueOf(LP_DOPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEPXXX),intCLMLEN));
							L_BALSTR.append(padSTRING('L',String.valueOf(L_DOMBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_DEXBAL),intCLMLEN));
							L_TRGPER.append(padSTRING('L'," ",intCLMLEN)+padSTRING('L'," ",intCLMLEN));
							L_DSPPER.append(padSTRING('L',String.valueOf(L_DODPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEDPER)+" %",intCLMLEN));
							L_PNDPER.append(padSTRING('L',String.valueOf(L_DOPPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEPPER)+" %",intCLMLEN));
							L_BALPER.append(padSTRING('L',String.valueOf(L_DOBPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEBPER)+" %",intCLMLEN));
							L_HSTTRG += String.valueOf(LP_DOTXXX)+ "|" + String.valueOf(LP_DETXXX)+ "|";
							L_HSTDSP += String.valueOf(LP_DODXXX)+ "|" + String.valueOf(LP_DEDXXX)+ "|";
							L_HSTPND += String.valueOf(LP_DOPXXX)+ "|" + String.valueOf(LP_DEPXXX)+ "|"; 
							L_HSTBAL += String.valueOf(L_DOMBAL)+ "|" +  String.valueOf(L_DEXBAL)+ "|";
						}
					}else{
						L_TRGSTR.append(padSTRING('L',String.valueOf(LP_DOTXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DETXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXTXXX),intCLMLEN));
						L_DSPSTR.append(padSTRING('L',String.valueOf(LP_DODXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEDXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXDXXX),intCLMLEN));
						L_PNDSTR.append(padSTRING('L',String.valueOf(LP_DOPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXPXXX),intCLMLEN));
						L_BALSTR.append(padSTRING('L',String.valueOf(L_DOMBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_DEXBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_EXPBAL),intCLMLEN));
						L_TRGPER.append(padSTRING('L'," ",intCLMLEN)+padSTRING('L'," ",intCLMLEN)+padSTRING('L'," ",intCLMLEN));
						L_DSPPER.append(padSTRING('L',String.valueOf(L_DODPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEDPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_EXDPER)+" %",intCLMLEN));
						L_PNDPER.append(padSTRING('L',String.valueOf(L_DOPPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEPPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_EXPPER)+" %",intCLMLEN));
						L_BALPER.append(padSTRING('L',String.valueOf(L_DOBPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEBPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_EXBPER)+" %",intCLMLEN));
						L_HSTTRG += String.valueOf(LP_DOTXXX)+ "|" + String.valueOf(LP_DETXXX)+ "|" + String.valueOf(LP_EXTXXX)+ "|";
						L_HSTDSP += String.valueOf(LP_DODXXX)+ "|" + String.valueOf(LP_DEDXXX)+ "|" + String.valueOf(LP_EXDXXX)+ "|";
						L_HSTPND += String.valueOf(LP_DOPXXX)+ "|" + String.valueOf(LP_DEPXXX)+ "|" + String.valueOf(LP_EXPXXX)+ "|"; 
						L_HSTBAL += String.valueOf(L_DOMBAL)+ "|" +  String.valueOf(L_DEXBAL)+ "|" + String.valueOf(L_EXPBAL)+ "|";
					}
				}
				else if(rdbRGNWS.isSelected()){
					if(strCHP01.trim().length() > 0){
						if(strCHP01.equals("01") || strCHP01.equals("02")){
							L_TRGSTR.append(padSTRING('L',String.valueOf(LP_DOTXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DETXXX),intCLMLEN));
							L_DSPSTR.append(padSTRING('L',String.valueOf(LP_DODXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEDXXX),intCLMLEN));
							L_PNDSTR.append(padSTRING('L',String.valueOf(LP_DOPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEPXXX),intCLMLEN));
							L_BALSTR.append(padSTRING('L',String.valueOf(L_DOMBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_DEXBAL),intCLMLEN));
							L_TRGPER.append(padSTRING('L'," ",intCLMLEN)+padSTRING('L'," ",intCLMLEN));
							L_DSPPER.append(padSTRING('L',String.valueOf(L_DODPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEDPER)+" %",intCLMLEN));
							L_PNDPER.append(padSTRING('L',String.valueOf(L_DOPPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEPPER)+" %",intCLMLEN));
							L_BALPER.append(padSTRING('L',String.valueOf(L_DOBPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEBPER)+" %",intCLMLEN));
							L_HSTTRG += String.valueOf(LP_DOTXXX)+ "|" + String.valueOf(LP_DETXXX)+ "|";
							L_HSTDSP += String.valueOf(LP_DODXXX)+ "|" + String.valueOf(LP_DEDXXX)+ "|";
							L_HSTPND += String.valueOf(LP_DOPXXX)+ "|" + String.valueOf(LP_DEPXXX)+ "|"; 
							L_HSTBAL += String.valueOf(L_DOMBAL)+ "|" +  String.valueOf(L_DEXBAL)+ "|";
						}
						else if(strCHP01.equals("03")){
						    L_TRGSTR.append(padSTRING('L',String.valueOf(LP_EXTXXX),intSTKLEN));
							L_DSPSTR.append(padSTRING('L',String.valueOf(LP_EXDXXX),intSTKLEN));
							L_PNDSTR.append(padSTRING('L',String.valueOf(LP_EXPXXX),intSTKLEN));
							L_BALSTR.append(padSTRING('L',String.valueOf(L_EXPBAL),intSTKLEN));
							L_TRGPER.append(padSTRING('L'," ",intSTKLEN));
							L_DSPPER.append(padSTRING('L',String.valueOf(L_EXDPER)+" %",intSTKLEN));
							L_PNDPER.append(padSTRING('L',String.valueOf(L_EXPPER)+" %",intSTKLEN));
							L_BALPER.append(padSTRING('L',String.valueOf(L_EXBPER)+" %",intSTKLEN));
							L_HSTTRG += String.valueOf(LP_EXTXXX)+ "|";
							L_HSTDSP += String.valueOf(LP_EXDXXX)+ "|";
							L_HSTPND += String.valueOf(LP_EXPXXX)+ "|";
							L_HSTBAL += String.valueOf(L_EXPBAL)+ "|";
						}
					}else{
						L_TRGSTR.append(padSTRING('L',String.valueOf(LP_DOTXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DETXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXTXXX),intCLMLEN));
						L_DSPSTR.append(padSTRING('L',String.valueOf(LP_DODXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEDXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXDXXX),intCLMLEN));
						L_PNDSTR.append(padSTRING('L',String.valueOf(LP_DOPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXPXXX),intCLMLEN));
						L_BALSTR.append(padSTRING('L',String.valueOf(L_DOMBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_DEXBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_EXPBAL),intCLMLEN));
						L_TRGPER.append(padSTRING('L'," ",intCLMLEN)+padSTRING('L'," ",intCLMLEN)+padSTRING('L'," ",intCLMLEN));
						L_DSPPER.append(padSTRING('L',String.valueOf(L_DODPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEDPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_EXDPER)+" %",intCLMLEN));
						L_PNDPER.append(padSTRING('L',String.valueOf(L_DOPPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEPPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_EXPPER)+" %",intCLMLEN));
						L_BALPER.append(padSTRING('L',String.valueOf(L_DOBPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_DEBPER)+" %",intCLMLEN)+padSTRING('L',String.valueOf(L_EXBPER)+" %",intCLMLEN));
						L_HSTTRG += String.valueOf(LP_DOTXXX)+ "|" + String.valueOf(LP_DETXXX)+ "|" + String.valueOf(LP_EXTXXX)+ "|";
						L_HSTDSP += String.valueOf(LP_DODXXX)+ "|" + String.valueOf(LP_DEDXXX)+ "|" + String.valueOf(LP_EXDXXX)+ "|";
						L_HSTPND += String.valueOf(LP_DOPXXX)+ "|" + String.valueOf(LP_DEPXXX)+ "|" + String.valueOf(LP_EXPXXX)+ "|"; 
						L_HSTBAL += String.valueOf(L_DOMBAL)+ "|" +  String.valueOf(L_DEXBAL)+ "|" + String.valueOf(L_EXPBAL)+ "|";
					}
				}
			}
			else if(flgTB2FL){
				if(rdbDOMWS.isSelected()){
					if(chkDOMWS.isSelected()){
						L_TRGSTR.append(padSTRING('L',String.valueOf(LP_DOTXXX),intCLMLEN));
						L_DSPSTR.append(padSTRING('L',String.valueOf(LP_DODXXX),intCLMLEN));
						L_PNDSTR.append(padSTRING('L',String.valueOf(LP_DOPXXX),intCLMLEN));
						L_BALSTR.append(padSTRING('L',String.valueOf(L_DOMBAL),intCLMLEN));
						L_HSTTRG += String.valueOf(LP_DOTXXX)+ "|";
						L_HSTDSP += String.valueOf(LP_DODXXX)+ "|";
						L_HSTPND += String.valueOf(LP_DOPXXX)+ "|";
						L_HSTBAL += String.valueOf(L_DOMBAL)+ "|";
					}
					if(chkDEXWS.isSelected()){
						L_TRGSTR.append(padSTRING('L',String.valueOf(LP_DETXXX),intCLMLEN));
						L_DSPSTR.append(padSTRING('L',String.valueOf(LP_DEDXXX),intCLMLEN));
						L_PNDSTR.append(padSTRING('L',String.valueOf(LP_DEPXXX),intCLMLEN));
						L_BALSTR.append(padSTRING('L',String.valueOf(L_DEXBAL),intCLMLEN));
						L_HSTTRG += String.valueOf(LP_DETXXX)+ "|";
						L_HSTDSP += String.valueOf(LP_DEDXXX)+ "|";
						L_HSTPND += String.valueOf(LP_DEPXXX)+ "|";
						L_HSTBAL += String.valueOf(L_DEXBAL)+ "|";
					}
					/*if(chkDOMWS.isSelected() && chkDEXWS.isSelected()){
						L_TRGSTR.append(padSTRING('L',String.valueOf(L_TOTXXX),intTOTLEN));
						L_DSPSTR.append(padSTRING('L',String.valueOf(L_TODXXX),intTOTLEN));
						L_PNDSTR.append(padSTRING('L',String.valueOf(L_TOPXXX),intTOTLEN));
						L_BALSTR.append(padSTRING('L',String.valueOf(L_TOBXXX),intTOTLEN));
						L_HSTTRG += String.valueOf(L_TOTXXX);
						L_HSTDSP += String.valueOf(L_TODXXX);
						L_HSTPND += String.valueOf(L_TOPXXX);
						L_HSTBAL += String.valueOf(L_TOBXXX);
					}*/
				}
				else if(rdbEXPWS.isSelected())
				{
					L_TRGSTR.append(padSTRING('L',String.valueOf(LP_EXTXXX),intSTKLEN));
					L_DSPSTR.append(padSTRING('L',String.valueOf(LP_EXDXXX),intSTKLEN));
					L_PNDSTR.append(padSTRING('L',String.valueOf(LP_EXPXXX),intSTKLEN));
					L_BALSTR.append(padSTRING('L',String.valueOf(L_EXPBAL),intSTKLEN));
					L_HSTTRG += String.valueOf(LP_EXTXXX)+ "|";
					L_HSTDSP += String.valueOf(LP_EXDXXX)+ "|";
					L_HSTPND += String.valueOf(LP_EXPXXX)+ "|";
					L_HSTBAL += String.valueOf(L_EXPBAL)+ "|";
				}
				else if(rdbALLWS.isSelected())
				{
					if(rdbSUMRY.isSelected())
					{
						L_TRGSTR.append(padSTRING('L',String.valueOf(L_TOTXXX),intSTKLEN));
						L_DSPSTR.append(padSTRING('L',String.valueOf(L_TODXXX),intSTKLEN));
						L_PNDSTR.append(padSTRING('L',String.valueOf(L_TOPXXX),intSTKLEN));
						L_BALSTR.append(padSTRING('L',String.valueOf(L_TOBXXX),intSTKLEN));
						L_PRDSTR.append(padSTRING('L',String.valueOf(LP_PRTXXX),intSTKLEN)+padSTRING('L',String.valueOf(LP_PRDXXX),intSTKLEN)+padSTRING('L',String.valueOf(L_PRDXXX),intSTKLEN));
						L_HSTTRG += String.valueOf(L_TOTXXX)+ "|";
						L_HSTDSP += String.valueOf(L_TODXXX)+ "|";
						L_HSTPND += String.valueOf(L_TOPXXX)+ "|";
						L_HSTBAL += String.valueOf(L_TOBXXX)+ "|";
						L_HSTPRD += String.valueOf(LP_PRTXXX)+ "|" + String.valueOf(LP_PRDXXX)+ "|" + String.valueOf(L_PRDXXX) + "|";
					}
					else if(rdbDETAL.isSelected()){
						L_TRGSTR.append(padSTRING('L',String.valueOf(LP_DOTXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DETXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXTXXX),intCLMLEN));
						L_DSPSTR.append(padSTRING('L',String.valueOf(LP_DODXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEDXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXDXXX),intCLMLEN));
						L_PNDSTR.append(padSTRING('L',String.valueOf(LP_DOPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_DEPXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_EXPXXX),intCLMLEN));
						L_BALSTR.append(padSTRING('L',String.valueOf(L_DOMBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_DEXBAL),intCLMLEN)+padSTRING('L',String.valueOf(L_EXPBAL),intCLMLEN));
						L_HSTTRG += String.valueOf(LP_DOTXXX) + "|" + String.valueOf(LP_DETXXX)+ "|" + String.valueOf(LP_EXTXXX)+ "|";
						L_HSTDSP += String.valueOf(LP_DODXXX) + "|" + String.valueOf(LP_DEDXXX)+ "|" + String.valueOf(LP_EXDXXX)+ "|";
						L_HSTPND += String.valueOf(LP_DOPXXX) + "|" + String.valueOf(LP_DEPXXX)+ "|" + String.valueOf(LP_EXPXXX)+ "|";
						L_HSTBAL += String.valueOf(L_DOMBAL) + "|" + String.valueOf(L_DEXBAL)+ "|" + String.valueOf(L_EXPBAL)+ "|";
					}
				}
			}
			L_HSTVL = L_GRPDS + "|" + L_HSTTRG + L_HSTDSP + L_HSTPND + L_HSTBAL;
			if(flgTB2FL)
				 L_HSTVL += L_HSTPRD + String.valueOf(LP_TOCXXX) + "|" + String.valueOf(LP_TOUXXX) + "|" + String.valueOf(L_STKXXX) + "|";
			L_HSTVL += L_GRPDS;
			/*O_DOUT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',L_GRPDS,LM_GRDLEN-LP_CLMGAP)+L_TRGSTR.toString()+L_DSPSTR.toString()+L_PNDSTR.toString());
			if(L_FNTFL)
				O_DOUT.writeBytes("<FONT COLOR = "+Color.red+">");
			O_DOUT.writeBytes(L_BALSTR.toString());
			if(L_FNTFL)
				O_DOUT.writeBytes("</FONT>");
			O_DOUT.writeBytes(padSTRING('L',String.valueOf(LP_TOCXXX),intSTKLEN)+ padSTRING('L',String.valueOf(LP_TOUXXX),intSTKLEN)+ padSTRING('L',String.valueOf(L_STKXXX),intSTKLEN)+"\n");*/
			if(LP_GRPCT.equals("CG") || LP_GRPCT.equals("SG") || LP_GRPCT.equals("MG") || LP_GRPCT.equals("GT"))
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',L_GRPDS,LM_GRDLEN-LP_CLMGAP)+L_TRGSTR.toString()+L_DSPSTR.toString()+L_PNDSTR.toString()+L_BALSTR.toString());
			if(flgTB2FL)
			    dosREPORT.writeBytes(L_PRDSTR.toString()+padSTRING('L',String.valueOf(LP_TOCXXX),intSTKLEN)+ padSTRING('L',String.valueOf(LP_TOUXXX),intSTKLEN)+ padSTRING('L',String.valueOf(L_STKXXX),intSTKLEN));
			dosREPORT.writeBytes("\n");
			if(LP_GRPCT.equals("GT")){
				if(flgTB1FL){
				    dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',"     %     ",LM_GRDLEN-LP_CLMGAP)+L_TRGPER.toString()+L_DSPPER.toString()+L_PNDPER.toString()+L_BALPER.toString()); 
				    dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
				}
				dosREPORT.writeBytes("</B>");
			}
			else if(LP_GRPCT.equals("CG") || LP_GRPCT.equals("SG") || LP_GRPCT.equals("MG"))
			    dosREPORT.writeBytes("</B>");
			hstALLREC.put(String.valueOf(intROWCNT),L_HSTVL);
			intROWCNT++;
            cl_dat.M_intLINNO_pbst += 1;
			if(cl_dat.M_intLINNO_pbst >= intLINENO)
			{
                crtLINE(intLINLEN);
                dosREPORT.writeBytes("\n");
                dosREPORT.writeBytes("Booking/Despatch/Bkd:pnd.toGO is upto "+cl_dat.M_strLOGDT_pbst+" "+cl_dat.getCURTIM()+" Hrs.");
                dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				cl_dat.M_intLINNO_pbst = 0;
                cl_dat.M_PAGENO += 1;
				prnHEADER(); //gets the Header of the report
				prnFMTCHR(dosREPORT,M_strEJT);
            }
		}catch(Exception L_EX){
			setMSG(L_EX,"prnGRPTOT");
		}
	}
	

	private void prnGRPTOT(String LP_GRPCT,int LP_DOPXXX,int LP_EXPXXX,int LP_TOCXXX,int LP_TOUXXX,int LP_TORXXX,int LP_CLMGAP,String LP_BLDFL)
	{
		String L_GRPDS = "";
		String L_HSTVL = "";
		boolean L_FNTFL1 = false;
		boolean L_FNTFL2 = false;
		try{
			int L_TOTSTK = LP_TOCXXX + LP_TOUXXX - LP_TORXXX;
			int L_STADOM = L_TOTSTK - LP_DOPXXX;
			int L_STKBAL = L_STADOM - LP_EXPXXX;
			if (LP_GRPCT.equals("PR")){
				L_GRPDS = strPRDDS1;
			}
			else if (LP_GRPCT.equals("PK")){
				hstPRDDS.put(String.valueOf(intROWCNT),"");
                L_GRPDS = "Total "+cl_dat.getPRMCOD("cmt_codds","SYS","FGXXPKG",strPKGTP1);
			}
			else if (LP_GRPCT.equals("CG")){
				dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
                L_GRPDS = "Total "+cl_dat.getPRMCOD("cmt_codds","STS","MRXXSTS",strSTSFL1);
			}
			else if (LP_GRPCT.equals("SG")){
				dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = "Total "+getSBPRD(strSBGRP1);
			}
			else if (LP_GRPCT.equals("MG")){
				dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = "Total "+getMNPRD(strMNGRP1);
			}
			else if (LP_GRPCT.equals("GT")){
				dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst += 1;
				hstPRDDS.put(String.valueOf(intROWCNT),"");
				L_GRPDS = "Grand Total";
			}
			if(L_STADOM < 0){
				L_FNTFL1 = true;
				hstDOPQT.put(String.valueOf(intROWCNT),"");
			}
			if(L_STKBAL < 0){
				L_FNTFL2 = true;
			}
			L_HSTVL += L_GRPDS + "|" + String.valueOf(LP_TOCXXX) + "|" + String.valueOf(LP_TORXXX) + "|" + String.valueOf(LP_TOUXXX) + "|" + String.valueOf(L_TOTSTK) + "|" + String.valueOf(LP_DOPXXX) + "|" + String.valueOf(L_STADOM) + "|" + String.valueOf(LP_EXPXXX) + "|" + String.valueOf(L_STKBAL);
			if(LP_GRPCT.equals("PK") || LP_GRPCT.equals("CG") || LP_GRPCT.equals("SG") || LP_GRPCT.equals("MG") || LP_GRPCT.equals("GT"))
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R'," ",LP_CLMGAP)+padSTRING('R',L_GRPDS,LM_GRDLEN-LP_CLMGAP)+padSTRING('L',String.valueOf(LP_TOCXXX),intCLMLEN)+padSTRING('L',String.valueOf(LP_TORXXX),intCLMLEN)+ padSTRING('L',String.valueOf(LP_TOUXXX),intCLMLEN)+ padSTRING('L',String.valueOf(L_TOTSTK),intCLMLEN)+padSTRING('L',String.valueOf(LP_DOPXXX),intCLMLEN));
			if(L_FNTFL1)
				dosREPORT.writeBytes("<FONT COLOR = "+Color.red+">");
			dosREPORT.writeBytes(padSTRING('L',String.valueOf(L_STADOM),intCLMLEN));
			if(L_FNTFL1)
				dosREPORT.writeBytes("</FONT>");
			dosREPORT.writeBytes(padSTRING('L',String.valueOf(LP_EXPXXX),intCLMLEN));
			if(L_FNTFL2)
				dosREPORT.writeBytes("<FONT COLOR = "+Color.red+">");
			dosREPORT.writeBytes(padSTRING('L',String.valueOf(L_STKBAL),intCLMLEN));
			if(L_FNTFL2)
				dosREPORT.writeBytes("</FONT>");
			dosREPORT.writeBytes("\n");
			if(LP_GRPCT.equals("PK") || LP_GRPCT.equals("CG") || LP_GRPCT.equals("SG") || LP_GRPCT.equals("MG") || LP_GRPCT.equals("GT"))
				dosREPORT.writeBytes("</B>");
			hstALLREC.put(String.valueOf(intROWCNT),L_HSTVL);
			intROWCNT++;
            cl_dat.M_intLINNO_pbst += 1;
			if(cl_dat.M_intLINNO_pbst >= intLINENO){
                crtLINE(intLINLEN);
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(cl_dat.getCURTIM()+" Hrs.");
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				cl_dat.M_intLINNO_pbst = 0;
                cl_dat.M_PAGENO += 1;
				prnHEADER(); //gets the Header of the report
				prnFMTCHR(dosREPORT,M_strEJT);
            }
		}catch(Exception L_EX){
			setMSG(L_EX,"prnGRPTOT");
		}
	}
	
	public static String getSBPRD(String LP_SBPRD){ //To get Sub Product Code i.e HIPS,GPPS
		String L_SBPRD = "";
		String L_strSQLQRY;
		ResultSet L_rstRSSET;
		try{
			L_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and SUBSTRING(CMT_CODCD,1,4)='"+LP_SBPRD+"' and CMT_CCSVL='SG'";
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY );
			while(L_rstRSSET.next()){
				L_SBPRD = L_rstRSSET.getString("CMT_CODDS");
			}
			L_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println(L_EX);
			}
		if(L_SBPRD == null)
			L_SBPRD = "";
		return L_SBPRD;
	}
	
	public static String getMNPRD(String LP_MNPRD){  //To get Main Product Code i.e Polystyrene
		String L_MNPRD = "";
		String L_strSQLQRY;
		ResultSet L_rstRSSET;
		try{
			L_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and SUBSTRING(CMT_CODCD,1,3)='"+LP_MNPRD+"' and CMT_CCSVL='MG'";
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY );
			while(L_rstRSSET.next()){
				L_MNPRD = L_rstRSSET.getString("CMT_CODDS");
			}
			L_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println(L_EX);
			}
		if(L_MNPRD == null)
			L_MNPRD = "";
		return L_MNPRD;
	}	
	
	private void intGRPTOT(String LP_GRPCT)
	{
		try{
			if (LP_GRPCT.equals("PR")){
				intDOTPRD = 0;
				intDETPRD = 0;
				intEXTPRD = 0;
				intDODPRD = 0;
				intDEDPRD = 0;
				intEXDPRD = 0;
				intDOPPRD = 0;
				intDEPPRD = 0;
				intEXPPRD = 0;
				intTOCPRD = 0;
				intTOUPRD = 0;
				intTORPRD = 0;
				intPRTPRD = 0;
				intPRDPRD = 0;
				strPRDCD1 = strPRDCD;
				strPRDDS1 = strPRDDS;
			}
			else if (LP_GRPCT.equals("PK"))
			{
				intDOPPKG = 0;
				intEXPPKG = 0;
				intTOCPKG = 0;
				intTOUPKG = 0;
				intTORPKG = 0;
				strPKGTP1 = strPKGTP;
			}
			else if (LP_GRPCT.equals("CG"))
			{
				intDOTCTG = 0;
				intDETCTG = 0;
				intEXTCTG = 0;
				intDODCTG = 0;
				intDEDCTG = 0;
				intEXDCTG = 0;
				intDOPCTG = 0;
				intDEPCTG = 0;
				intEXPCTG = 0;
				intTOCCTG = 0;
				intTOUCTG = 0;
				intTORCTG = 0;
				intPRTCTG = 0;
				intPRDCTG = 0;
				strSTSFL1 = strSTSFL;
			}
			else if (LP_GRPCT.equals("SG"))
			{
				intDOTSBP = 0;
				intDETSBP = 0;
				intEXTSBP = 0;
				intDODSBP = 0;
				intDEDSBP = 0;
				intEXDSBP = 0;
				intDOPSBP = 0;
				intDEPSBP = 0;
				intEXPSBP = 0;
				intTOCSBP = 0;
				intTOUSBP = 0;
				intTORSBP = 0;
				intPRTSBP = 0;
				intPRDSBP = 0;
				strSBGRP1 = strSBGRP;
				}
			else if (LP_GRPCT.equals("MG"))
			{
				intDOTMNP = 0;
				intDETMNP = 0;
				intEXTMNP = 0;
				intDODMNP = 0;
				intDEDMNP = 0;
				intEXDMNP = 0;
				intDOPMNP = 0;
				intDEPMNP = 0;
				intEXPMNP = 0;
				intTOCMNP = 0;
				intTOUMNP = 0;
				intTORMNP = 0;
				intPRTMNP = 0;
				intPRDMNP = 0;
				strMNGRP1 = strMNGRP;
				}
			if(cl_dat.M_intLINNO_pbst >= intLINENO)
			{
                crtLINE(intLINLEN);
                dosREPORT.writeBytes("\n");
				if(flgTB1FL || flgTB2FL)
				    dosREPORT.writeBytes("Booking/Despatch/Bkd:pnd.toGO is upto "+cl_dat.M_strLOGDT_pbst+" "+cl_dat.getCURTIM()+" Hrs.");
				else
				    dosREPORT.writeBytes(cl_dat.getCURTIM()+" Hrs.");
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				cl_dat.M_intLINNO_pbst = 0;
                cl_dat.M_PAGENO += 1;
				prnHEADER(); //gets the Header of the report
            }
		}catch(Exception L_EX){
			setMSG(L_EX,"intGRPTOT");
		}
	}
	
/*	public void mouseClicked(MouseEvent L_ME)
	{
		try{
            super.mouseClicked(L_ME);
		}catch(Exception L_EX){
			setMSG(L_EX,"mouseClicked");
		}
	}*/
	
	
	 private boolean chkUPDDT()
	 {
	        try
	        {
	            M_strSQLQRY = "Select * from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXTRG'";
	            M_strSQLQRY += " and cmt_codcd='UDT' and cmt_ccsvl='"+strMMYDT+"'";
	            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	            if(M_rstRSSET.next())
	                return false;
	            if(M_rstRSSET != null)
	                M_rstRSSET.close();
	        }catch(Exception L_EX){
	            setMSG(L_EX,"chkUPDDT");
	        }
	        return true;
	    }
		
	
	
	
	private void intALLQT()
	{
        try
        {
            
            setMSG("Initializing all the qty. to zero",'N');
			M_strSQLQRY = "Update mr_tmtrn set tm_ezdoo=0,tm_ezdeo=0,tm_wzdoo=0,tm_wzdeo=0,tm_nzdoo=0,";
			M_strSQLQRY += "tm_nzdeo=0,tm_szdoo=0,tm_szdeo=0,tm_czdoo=0,tm_czdeo=0,tm_exexo=0,";
			M_strSQLQRY += "tm_ezdop=0,tm_ezdep=0,tm_wzdop=0,tm_wzdep=0,tm_nzdop=0,tm_nzdep=0,";
			M_strSQLQRY += "tm_szdop=0,tm_szdep=0,tm_czdop=0,tm_czdep=0,tm_exexp=0,";
			M_strSQLQRY += "tm_ezdod=0,tm_ezded=0,tm_wzdod=0,tm_wzded=0,tm_nzdod=0,tm_nzded=0,";
			M_strSQLQRY += "tm_szdod=0,tm_szded=0,tm_czdod=0,tm_czded=0,tm_exexd=0,";
			M_strSQLQRY += "tm_clsqt=0,tm_uclqt=0,tm_resqt=0,tm_prdqt=0 where TM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND tm_mmydt = '"+strDBSMY+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst )
				cl_dat.exeDBCMT("setLCLUPD");
			else
			{
			    
			}
        }catch(Exception L_EX){
            setMSG(L_EX,"intALLQT");
        }
    }
	
	
	private void updORDQT()
	{
        try
        {
            setMSG("Updating booking order qty.",'N');
            String L_STRMTH = "01"+"/"+strMONTH+"/"+strYEAR;
            String L_ENDMTH = hstDAYMTH.get(strMONTH).toString().trim()+"/"+strMONTH+"/"+strYEAR;
            M_strSQLQRY = "update mr_tmtrn set tm_wzdoo=0, tm_wzdeo=0,tm_czdoo=0, tm_czdeo=0,tm_nzdoo=0, tm_nzdeo=0,tm_ezdoo=0, tm_ezdeo=0,tm_exexo=0 where TM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND tm_mmydt = '"+strDBSMY+"'" ;
			System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			M_strSQLQRY = "Select * from mr_tmtrn where tm_cmpcd = ? and tm_mmydt = ? and tm_prdcd=? and tm_pkgtp=?";
            prpCOMSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stbSTRQRY = new StringBuffer("select int_pkgtp,int_prdcd,int_prdds,in_zoncd,in_saltp,sum((isnull(int_indqt,0)-isnull(int_fcmqt,0))) l_indqt");
            stbSTRQRY.append(" from mr_intrn,mr_inmst where int_cmpcd=in_cmpcd and int_mkttp=in_mkttp and int_indno=in_indno");
            stbSTRQRY.append(" and (in_bkgdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_STRMTH))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' "+ ") and in_stsfl != 'X' ");
            stbSTRQRY.append(" and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_saltp in ('01','12','03') and in_mkttp='01' and INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(int_indqt,0)-isnull(int_fcmqt,0))>isnull(int_invqt,0) group by int_pkgtp,int_prdcd,int_prdds,in_zoncd,in_saltp ");
            stbSTRQRY.append(" union all  select int_pkgtp,int_prdcd,int_prdds,in_zoncd,in_saltp,sum((isnull(int_indqt,0)-isnull(int_fcmqt,0))) l_indqt");
            stbSTRQRY.append(" from mr_intrn,mr_inmst where int_cmpcd=in_cmpcd and int_mkttp=in_mkttp and int_indno=in_indno");
            stbSTRQRY.append(" and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_bkgdt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_STRMTH))+"'" +" and in_stsfl != 'X'");
            stbSTRQRY.append(" and in_saltp in ('01','12','03') and in_mkttp='01' and INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(int_indqt,0)-isnull(int_fcmqt,0))>isnull(int_invqt,0) group by int_pkgtp,int_prdcd,int_prdds,in_zoncd,in_saltp");
			System.out.println(stbSTRQRY.toString().trim());
            M_rstRSSET = cl_dat.exeSQLQRY1(stbSTRQRY.toString().trim());
            while(M_rstRSSET.next())
            {
                strPKGTP = nvlSTRVL(M_rstRSSET.getString("int_pkgtp"),"");
                strPRDCD = nvlSTRVL(M_rstRSSET.getString("int_prdcd"),"");
				strSTSFL = getSTSFL(strPRDCD);
                strPRDDS = nvlSTRVL(M_rstRSSET.getString("int_prdds"),"");
                fltINDQT = M_rstRSSET.getFloat("l_indqt");
                strZONCD = nvlSTRVL(M_rstRSSET.getString("in_zoncd"),"");
                strSALTP = nvlSTRVL(M_rstRSSET.getString("in_saltp"),"");
                strCHPZN = cl_dat.getPRMCOD("CMT_CHP01","SYS","MR00ZON",strZONCD);
                strCHPSL = cl_dat.getPRMCOD("CMT_CHP01","SYS","MR00SAL",strSALTP);
				prpCOMSTM.setString(1,cl_dat.M_strCMPCD_pbst);
				prpCOMSTM.setString(2,strDBSMY);
				prpCOMSTM.setString(3,strPRDCD);
				prpCOMSTM.setString(4,strPKGTP);
				try
				{
				    prpRSLSET = prpCOMSTM.executeQuery();
				    if(prpRSLSET.next())
				    {
						prpRSLSET.updateFloat("tm_"+strCHPZN+strCHPSL+"o",fltINDQT);
				        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
				        prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
						prpRSLSET.updateString("tm_stsfl",strSTSFL);
						prpRSLSET.updateString("tm_prdds",strPRDDS);
				        prpRSLSET.updateRow();
				    }
				    else{
						prpRSLSET.moveToInsertRow();
						prpRSLSET.updateString("tm_mmydt",strDBSMY);
						prpRSLSET.updateString("tm_prdcd",strPRDCD);
						prpRSLSET.updateString("tm_pkgtp",strPKGTP);
						prpRSLSET.updateString("tm_prdds",strPRDDS);
						prpRSLSET.updateFloat("tm_"+strCHPZN+strCHPSL+"o",fltINDQT);
				        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
				        prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
						prpRSLSET.updateString("tm_stsfl",strSTSFL);
						prpRSLSET.insertRow();
						prpRSLSET.moveToCurrentRow();
					}
					cl_dat.M_conSPDBA_pbst .commit();
				    if(prpRSLSET != null)
				        prpRSLSET.close();
					prpCOMSTM.clearParameters();
				}catch(Exception L_EX)
				{
				    setMSG(L_EX,"updORDQT");
				    prpRSLSET.close();
				}
			}
            if(M_rstRSSET != null)
                M_rstRSSET.close();
            prpCOMSTM.close();
        }catch(Exception L_EX)
        {
            setMSG(L_EX,"updORDQT");
        }
    }
	 private void updPNDQT()
	 {
	        try
	        {
				setMSG("Updating Bkd:pnd.toGO order qty.",'N');
	            String L_STRMTH = "01"+"/"+strMONTH+"/"+strYEAR;
	            String L_ENDMTH = hstDAYMTH.get(strMONTH).toString().trim()+"/"+strMONTH+"/"+strYEAR;
	            M_strSQLQRY = "Select * from mr_tmtrn where TM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND tm_mmydt = ? and tm_prdcd=? and tm_pkgtp=?";
	            prpCOMSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	            stbSTRQRY = new StringBuffer("select int_pkgtp,int_prdcd,int_prdds,in_zoncd,in_saltp,");
	            stbSTRQRY.append("sum((isnull(int_indqt,0)-isnull(int_fcmqt,0))-isnull(int_invqt,0)) l_pndqt from mr_intrn,mr_inmst where int_cmpcd=in_cmpcd and int_mkttp=in_mkttp");
	            stbSTRQRY.append(" and int_indno=in_indno and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_saltp in ('01','12','03') and INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND int_stsfl != 'X'");
	            stbSTRQRY.append(" and in_mkttp='01' and (isnull(int_indqt,0)-isnull(int_fcmqt,0))>isnull(int_invqt,0) group by int_prdcd,int_pkgtp,in_zoncd,in_saltp,int_prdds");
	            M_rstRSSET = cl_dat.exeSQLQRY1(stbSTRQRY.toString().trim());
	            System.out.println(stbSTRQRY);
	            while(M_rstRSSET.next())
	            {
	                strPKGTP = nvlSTRVL(M_rstRSSET.getString("int_pkgtp"),"");
	                strPRDCD = nvlSTRVL(M_rstRSSET.getString("int_prdcd"),"");
					strSTSFL = getSTSFL(strPRDCD);
	                strPRDDS = nvlSTRVL(M_rstRSSET.getString("int_prdds"),"");
	                fltPNDQT = M_rstRSSET.getFloat("l_pndqt");
	                strZONCD = nvlSTRVL(M_rstRSSET.getString("in_zoncd"),"");
	                strSALTP = nvlSTRVL(M_rstRSSET.getString("in_saltp"),"");
	                strCHPZN = cl_dat.getPRMCOD("CMT_CHP01","SYS","MR00ZON",strZONCD);
	                strCHPSL = cl_dat.getPRMCOD("CMT_CHP01","SYS","MR00SAL",strSALTP);
	                prpCOMSTM.setString(1,strDBSMY);
	                prpCOMSTM.setString(2,strPRDCD);
	                prpCOMSTM.setString(3,strPKGTP);
	                try
	                {
	                    prpRSLSET = prpCOMSTM.executeQuery();
	                    if(prpRSLSET.next())
						{
							prpRSLSET.updateFloat("tm_"+strCHPZN+strCHPSL+"p",fltPNDQT);
	                        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst );
	                        prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							prpRSLSET.updateString("tm_stsfl",strSTSFL);
							prpRSLSET.updateString("tm_prdds",strPRDDS);
	                        prpRSLSET.updateRow();
	                    }
	                    else
						{
	                        prpRSLSET.moveToInsertRow();
	                        prpRSLSET.updateString("tm_mmydt",strDBSMY);
							prpRSLSET.updateString("tm_prdcd",strPRDCD);
							prpRSLSET.updateString("tm_pkgtp",strPKGTP);
	                        prpRSLSET.updateString("tm_prdds",strPRDDS);
							prpRSLSET.updateFloat("tm_"+strCHPZN+strCHPSL+"p",fltPNDQT);
	                        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
	                        prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							prpRSLSET.updateString("tm_stsfl",strSTSFL);
	                        prpRSLSET.insertRow();
	                        prpRSLSET.moveToCurrentRow();
	                    }
	                    prpCOMSTM.clearParameters();
	                    cl_dat.M_conSPDBA_pbst .commit();
	                    if(prpRSLSET != null)
	                        prpRSLSET.close();
	                }catch(Exception L_EX){
	                    setMSG(L_EX,"updPNDQT");
	                    prpRSLSET.close();
	                }
	            }
	            if(M_rstRSSET != null)
	                M_rstRSSET.close();
	            prpCOMSTM.close();
	        }catch(Exception L_EX){
	            setMSG(L_EX,"updPNDQT");
	        }
	    }
	 
	 
	 
	 private void updDSPQT(){
	        try{
	            setMSG("Updating despatch qty.",'N');
	            String L_STRMTH = "01"+"/"+strMONTH+"/"+strYEAR;
	            String L_ENDMTH = hstDAYMTH.get(strMONTH).toString().trim()+"/"+strMONTH+"/"+strYEAR;
	            M_strSQLQRY = "Select * from mr_tmtrn where tm_cmpcd = ? and tm_mmydt = ? and tm_prdcd=? and tm_pkgtp=?";
	            prpCOMSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	            stbSTRQRY = new StringBuffer("select ivt_pkgtp,ivt_prdcd,pr_prdds,ivt_saltp,ivt_zoncd,");
	            stbSTRQRY.append("sum(isnull(ivt_invqt,0)) l_dspqt from mr_ivtrn,co_prmst where ivt_prdcd=pr_prdcd");
	            stbSTRQRY.append(" and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_stsfl='D' and ivt_saltp in ('01','12','03') and");
				stbSTRQRY.append(" CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_STRMTH))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_ENDMTH))+"'" );
				
	            stbSTRQRY.append(" group by ivt_prdcd,ivt_pkgtp,ivt_zoncd,ivt_saltp,pr_prdds");
	            
	            M_rstRSSET = cl_dat.exeSQLQRY1(stbSTRQRY.toString().trim());
	            System.out.println(stbSTRQRY);
	            while(M_rstRSSET.next())
	            {
	                strPKGTP = nvlSTRVL(M_rstRSSET.getString("ivt_pkgtp"),"");
	                strPRDCD = nvlSTRVL(M_rstRSSET.getString("ivt_prdcd"),"");
					strSTSFL = getSTSFL(strPRDCD);
	                strPRDDS = nvlSTRVL(M_rstRSSET.getString("pr_prdds"),"");
	                fltTODQT = M_rstRSSET.getFloat("l_dspqt");
	                strZONCD = nvlSTRVL(M_rstRSSET.getString("ivt_zoncd"),"");
	                strSALTP = nvlSTRVL(M_rstRSSET.getString("ivt_saltp"),"");
	                strCHPZN = cl_dat.getPRMCOD("CMT_CHP01","SYS","MR00ZON",strZONCD);
	                strCHPSL = cl_dat.getPRMCOD("CMT_CHP01","SYS","MR00SAL",strSALTP);
	                prpCOMSTM.setString(1,cl_dat.M_strCMPCD_pbst);
					prpCOMSTM.setString(2,strDBSMY);
	                prpCOMSTM.setString(3,strPRDCD);
	                prpCOMSTM.setString(4,strPKGTP);
	                try
	                {
	                    prpRSLSET = prpCOMSTM.executeQuery();
	                    if(prpRSLSET.next())
	                    {
	                        prpRSLSET.updateFloat("tm_"+strCHPZN+strCHPSL+"d",fltTODQT);
	                        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst );
	                        prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							prpRSLSET.updateString("tm_stsfl",strSTSFL);
							prpRSLSET.updateString("tm_prdds",strPRDDS);
	                        prpRSLSET.updateRow();
	                    }
	                    else
	                    {
	                        prpRSLSET.moveToInsertRow();
	                        prpRSLSET.updateString("tm_mmydt",strDBSMY);
							prpRSLSET.updateString("tm_prdcd",strPRDCD);
							prpRSLSET.updateString("tm_pkgtp",strPKGTP);
	                        prpRSLSET.updateString("tm_prdds",strPRDDS);
	                        prpRSLSET.updateFloat("tm_"+strCHPZN+strCHPSL+"d",fltTODQT);
	                        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
	                        prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							prpRSLSET.updateString("tm_stsfl",strSTSFL);
	                        prpRSLSET.insertRow();
	                        prpRSLSET.moveToCurrentRow();
	                    }
	                    prpCOMSTM.clearParameters();
	                    cl_dat.M_conSPDBA_pbst .commit();
	                    if(prpRSLSET != null)
	                        prpRSLSET.close();
	                }catch(Exception L_EX){
	                    setMSG(L_EX,"updDSPQT");
	                    prpRSLSET.close();
	                }
	            }
	            if(M_rstRSSET != null)
	                M_rstRSSET.close();
	            prpCOMSTM.close();
	        }catch(Exception L_EX){
	            setMSG(L_EX,"updDSPQT");
	        }
	    }

	    private void updSTKQT()
	    {
	        try
	        {
	            setMSG("Updating classified,unclassified & reserved stock qty.",'N');
				M_strSQLQRY = "Select * from mr_tmtrn where TM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND tm_mmydt = ? and tm_prdcd=? and tm_pkgtp=?";
	            prpCOMSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	            stbSTRQRY.delete(0,stbSTRQRY.length());
				stbSTRQRY.append("Select st_prdtp,st_prdcd,st_pkgtp,st_resno,pr_prdds,pr_stsfl,sum(isnull(st_stkqt,0)) l_stkqt,sum(isnull(st_uclqt,0)) l_uclqt");
	            stbSTRQRY.append(" from fg_stmst,co_prmst where st_prdcd=pr_prdcd and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(st_stkqt,0)+isnull(st_uclqt,0))>0group by st_prdtp,st_prdcd,pr_stsfl,st_pkgtp,st_resno,pr_prdds");
	            M_rstRSSET = cl_dat.exeSQLQRY(stbSTRQRY.toString().trim());
	            while(M_rstRSSET.next())
	            {
					strPKGTP = nvlSTRVL(M_rstRSSET.getString("st_pkgtp"),"");
					strPRDCD = nvlSTRVL(M_rstRSSET.getString("st_prdcd"),"");
					strSTSFL = nvlSTRVL(M_rstRSSET.getString("pr_stsfl"),"");
					strMNGRP = nvlSTRVL(M_rstRSSET.getString("st_prdtp"),"");
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("pr_prdds"),"");
					strRESNO = nvlSTRVL(M_rstRSSET.getString("st_resno"),"");
					fltSTKQT = M_rstRSSET.getFloat("l_stkqt");
					fltUCLQT = M_rstRSSET.getFloat("l_uclqt");
					prpCOMSTM.setString(1,strDBSMY);
					prpCOMSTM.setString(2,strPRDCD);
					prpCOMSTM.setString(3,strPKGTP);
					try{
					    prpRSLSET = prpCOMSTM.executeQuery();
						fltRESQT = 0;  
					    if(strRESNO.trim().length() > 0)
					          fltRESQT = fltSTKQT;
					    if(prpRSLSET.next())
					    {
							  prpRSLSET.updateString("tm_prdds",strPRDDS);
					          prpRSLSET.updateString("tm_prdtp",strMNGRP);
					          prpRSLSET.updateFloat("tm_clsqt",Math.round(prpRSLSET.getFloat("tm_clsqt"))+fltSTKQT);
					          prpRSLSET.updateFloat("tm_uclqt",Math.round(prpRSLSET.getFloat("tm_uclqt"))+fltUCLQT);
					          prpRSLSET.updateFloat("tm_resqt",Math.round(prpRSLSET.getFloat("tm_resqt"))+fltRESQT);
							  prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
							  prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							  prpRSLSET.updateString("tm_stsfl",strSTSFL);
					          prpRSLSET.updateRow();
						}
					    else
						{
							prpRSLSET.moveToInsertRow();
	                        prpRSLSET.updateString("tm_mmydt",strDBSMY);
	                        prpRSLSET.updateString("tm_prdcd",strPRDCD);
	                        prpRSLSET.updateString("tm_pkgtp",strPKGTP);
	                        prpRSLSET.updateString("tm_prdtp",strMNGRP);
							prpRSLSET.updateString("tm_prdds",strPRDDS);
					        prpRSLSET.updateFloat("tm_clsqt",fltSTKQT);
					        prpRSLSET.updateFloat("tm_uclqt",fltUCLQT);
					        prpRSLSET.updateFloat("tm_resqt",fltRESQT);
							prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
							prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							prpRSLSET.updateString("tm_stsfl",strSTSFL);
	                        prpRSLSET.insertRow();
	                        prpRSLSET.moveToCurrentRow();
						}
					prpCOMSTM.clearParameters();
					cl_dat.M_conSPDBA_pbst .commit();
					if(prpRSLSET != null)
					    prpRSLSET.close();
	            }
					catch(Exception L_EX)
					{
	                setMSG(L_EX,"updSTKQT");
	                prpRSLSET.close();
	            }
	        }
	        if(M_rstRSSET != null)
	           M_rstRSSET.close();
	        prpCOMSTM.close();
	        }catch(Exception L_EX)
	        {
	            setMSG(L_EX,"updSTKQT");
	        }
	    }
		
		private void updPRDQT()
		{
	        try
	        {
	            setMSG("Updating production qty.",'N');
				String L_STRMTH = "01"+"/"+strMONTH+"/"+strYEAR;
	            String L_ENDMTH = hstDAYMTH.get(strMONTH).toString().trim()+"/"+strMONTH+"/"+strYEAR;
	            M_strSQLQRY = "Select * from mr_tmtrn where tm_cmpcd = ? and tm_mmydt = ? and tm_prdcd=? and tm_pkgtp=?";
	            prpCOMSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	            stbSTRQRY.delete(0,stbSTRQRY.length());
	            stbSTRQRY.append("Select lt_prdtp,lt_prdcd,rct_pkgtp,pr_prdds,pr_stsfl,sum(isnull(rct_rctqt,0)) l_stkqt");
	            stbSTRQRY.append(" from fg_rctrn,pr_ltmst,co_prmst where rct_cmpcd=lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno=lt_lotno");
				stbSTRQRY.append(" and rct_rclno=lt_rclno and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp in ('10','15') and rct_stsfl='2'");
				stbSTRQRY.append(" and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse((L_STRMTH)))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_ENDMTH))+"'");
				stbSTRQRY.append(" and lt_prdcd=pr_prdcd group by lt_prdtp,lt_prdcd,pr_stsfl,rct_pkgtp,pr_prdds");
				M_rstRSSET = cl_dat.exeSQLQRY(stbSTRQRY.toString().trim());
	            while(M_rstRSSET.next())
	            {
					strPKGTP = nvlSTRVL(M_rstRSSET.getString("rct_pkgtp"),"");
					strPRDCD = nvlSTRVL(M_rstRSSET.getString("lt_prdcd"),"");
					strSTSFL = nvlSTRVL(M_rstRSSET.getString("pr_stsfl"),"");
					strMNGRP = nvlSTRVL(M_rstRSSET.getString("lt_prdtp"),"");
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("pr_prdds"),"");
					fltSTKQT = M_rstRSSET.getFloat("l_stkqt");
					prpCOMSTM.setString(1,cl_dat.M_strCMPCD_pbst);
					prpCOMSTM.setString(2,strDBSMY);
					prpCOMSTM.setString(3,strPRDCD);
					prpCOMSTM.setString(4,strPKGTP);
					try
					{
					    prpRSLSET = prpCOMSTM.executeQuery();
					    if(prpRSLSET.next())
					    {
							prpRSLSET.updateString("tm_prdds",strPRDDS);
							prpRSLSET.updateString("tm_prdtp",strMNGRP);
					        prpRSLSET.updateFloat("tm_prdqt",fltSTKQT);
					        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
							prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							prpRSLSET.updateString("tm_stsfl",strSTSFL);
					        prpRSLSET.updateRow();
						}else{
							prpRSLSET.moveToInsertRow();
	                        prpRSLSET.updateString("tm_mmydt",strDBSMY);
	                        prpRSLSET.updateString("tm_prdcd",strPRDCD);
	                        prpRSLSET.updateString("tm_pkgtp",strPKGTP);
							prpRSLSET.updateString("tm_prdds",strPRDDS);
	                        prpRSLSET.updateString("tm_prdtp",strMNGRP);
					        prpRSLSET.updateFloat("tm_prdqt",fltSTKQT);
					        prpRSLSET.updateString("tm_lusby",cl_dat.M_strUSRCD_pbst);
							prpRSLSET.updateString("tm_lupdt",cl_dat.M_strLOGDT_pbst);
							prpRSLSET.updateString("tm_stsfl",strSTSFL);
	                        prpRSLSET.insertRow();
	                        prpRSLSET.moveToCurrentRow();
						}
					prpCOMSTM.clearParameters();
					cl_dat.M_conSPDBA_pbst .commit();
					if(prpRSLSET != null)
					    prpRSLSET.close();
	            }catch(Exception L_EX)
	            {
	                setMSG(L_EX,"updPRDQT");
	                prpRSLSET.close();
	            }
	        }
	        if(LM_RSLSET != null)
	           LM_RSLSET.close();
	        prpCOMSTM.close();
	        prpCOMSTM = cl_dat.M_conSPDBA_pbst .prepareStatement("Update co_cdtrn set cmt_ccsvl='"+strMMYDT+"' where cmt_cgmtp='SYS' and cmt_cgstp='MRXXTRG' and cmt_codcd='UDT'");
	        prpCOMSTM.executeUpdate();
	        cl_dat.M_conSPDBA_pbst .commit();
	        prpCOMSTM.close();
	        flgUPDFL = true;
	        }catch(Exception L_EX)
	        {
	            setMSG(L_EX,"updPRDQT");
	        }
	    }

		class rowRenderer extends DefaultTableCellRenderer{
			public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col){
	            if(hstTOTPQT.get(String.valueOf(row)) != null)
					setForeground(Color.red);
				else
	                setForeground(Color.black);
				int cellValue = (value instanceof Number)? ((Number)value).intValue() : 0;
				setText((value == null)? "":value.toString());
				setHorizontalAlignment(JLabel.RIGHT);	
				return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			}
		}

	    class rowRenderer1 extends DefaultTableCellRenderer{
			public Component getTableCellRendererComponent(cl_JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col){
	            if(hstDOPQT.get(String.valueOf(row)) != null)
					setForeground(Color.red);
				else
					setForeground(Color.black);
				int cellValue = (value instanceof Number)? ((Number)value).intValue() : 0;
				setText((value == null)? "":value.toString());
				setHorizontalAlignment(JLabel.RIGHT);	
				return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			}
		}
			
		class rowRenderer2 extends DefaultTableCellRenderer{
			public Component getTableCellRendererComponent(cl_JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col){
	            if(hstDEPQT.get(String.valueOf(row)) != null)
					setForeground(Color.red);
				else
					setForeground(Color.black);
				int cellValue = (value instanceof Number)? ((Number)value).intValue() : 0;
				setText((value == null)? "":value.toString());
				setHorizontalAlignment(JLabel.RIGHT);	
				return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			}
		}

	    class rowRenderer3 extends DefaultTableCellRenderer{
			public Component getTableCellRendererComponent(cl_JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col){
	            if(hstEXPQT.get(String.valueOf(row)) != null)
					setForeground(Color.red);
				else
					setForeground(Color.black);
				int cellValue = (value instanceof Number)? ((Number)value).intValue() : 0;
				setText((value == null)? "":value.toString());
				setHorizontalAlignment(JLabel.RIGHT);	
				return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			}
		}
			
		class rowRenderer4 extends DefaultTableCellRenderer{
			public Component getTableCellRendererComponent(cl_JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col){
	            if(hstPRDDS.get(String.valueOf(row)) != null)
					setForeground(Color.blue);
				else
					setForeground(Color.black);
				return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			}
		}
		
		private String getSTSFL(String LP_PRDCD)
		{
			String L_RTNSTR = "";
			try{
				String L_STRQRY = "Select pr_stsfl from co_prmst where pr_prdcd='"+LP_PRDCD+"'";
				ResultSet L_RSLSET = cl_dat.exeSQLQRY2(L_STRQRY);
				if(L_RSLSET.next())
				{
					L_RTNSTR = nvlSTRVL(L_RSLSET.getString("pr_stsfl"),"");
				}
				L_RSLSET.close();
			}catch(Exception L_EX){
				setMSG(L_EX,"getSTSFL");
			}
			return L_RTNSTR;
		}
		

	
	
	
	
	
	
	
}
	
