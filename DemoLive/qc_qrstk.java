/*
System Name   : Management Information System
Program Name  : Statement of Stock Position
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          :  August 2005
Version       : MIS 2.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;import java.awt.event.FocusEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JButton;import javax.swing.JOptionPane;import javax.swing.JCheckBox;
import javax.swing.JPanel;import java.awt.Dimension;
import java.awt.Color;import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JComponent;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;

class qc_qrstk extends cl_rbase
{									/** JLabels to display Messages on the Screen.*/
	private JLabel lblTBL1,lblTBL2,lblTBL3,lbl4; 
	
	private JLabel lblQTY;	/** JLabels to display Stock Qualtity.*/	
	private JLabel lblPKG;	/** JLabels to display Stock Qualtity.*/	
								/** JLabels to display Lot Count.*/		
	private JLabel lblLTCNT;	/** JLabels to display Stock Qualtity.*/	
	private JLabel lblSTKQT;	/** JLabels to display Unclassified Stock  Quantity.*/
	private JLabel lblSTKUC;	/** JLabels to display Avaliable Stock Quantity.*/
	private JLabel lblSTKAV;	/** JLabels to display Reserved Stock Quantity.*/
	private JLabel lblTDSQT;	

	private JLabel lblSTKQT_PK;	/** JLabels to display Unclassified Stock  Quantity.*/
	private JLabel lblSTKUC_PK;	/** JLabels to display Avaliable Stock Quantity.*/
	private JLabel lblSTKAV_PK;	/** JLabels to display Reserved Stock Quantity.*/
	private JLabel lblTDSQT_PK;	
	
	private JTextField txtPRDTP;	
	private JTextField txtPRDCD;
									/** JButton to view Summary of the Stock.*/
	private JButton btnSUMRY;		/** JButton to view Classification Details of Selected Product.*/
//	private JButton btnCLSDL;		/** JTable to enter & display Product Details.*/
	private cl_JTable tblPRDDL;		/** JTable to enter & display Test Details of Selected Product.*/
	private cl_JTable tblTSTDL;		/** JTable to enter & display Reserved Quantity Details.*/
	private cl_JTable tblTDSDL;		/** JRadioButton to specify Query Condition to view Day Opening Stock.*/
	private JRadioButton rdoDOSTK;	/** JRadioButton to specify Query Condition to view Current Stock.*/ 
	private JRadioButton rdoCUSTK;	/** ButtonGroup to bind JRadioButton together.*/
	private ButtonGroup bgrSTKST;	/** ButtonGroup to bind JRadioButton together.*/
	private ButtonGroup bgrSMPLE;	/** JRadioButton to specify Query Condition for Include Retention Sample.*/ 
	private JRadioButton rdoINSAM;	/** JRadioButton to specify Query Condition for Exclude Retention Sample.*/
	private JRadioButton rdoEXSAM;	/** JRadioButton to specify Query Condition for Exclude Retention Sample.*/ 
	
	private JCheckBox chbINSAM;	/**	JPanel to display Stock Summay of selected product.*/
	private JPanel pnlSUMRY;
									/** Final Integer to represent Check Flag Column of the Product Details Table.*/
	private final int TB1_DSPFL =0;	/** Final Integer to represent Product Type Column of the Product Details Table.*/	
	private final int TB1_PRDTP =1;	/** Final Integer to represent Product Type Description Column of the Product Details Table.*/
    private final int TB1_PRDCT =2;	/** Final Integer to represent Product Code Description Column of the Product Details Table.*/
	private final int TB1_PRDDS =3;	/** Final Integer to represent Product Code Column of the Product Details Table.*/
	private final int TB1_PRDCD =4;
    //private final int TB1_DSPFL =1;
									/** Final Integer to represent Check Flage Column of Quality Parameter Details Table.*/
	private final int TB2_CHKFL =0;	/** Final Integer to represent Lot Number Column of the Quality Parameter Details Table.*/
	private final int TB2_LOTNO =1;	/** Final Integer to represent Reclassification Number Column of the Quality Parameter Details Table.*/
	private final int TB2_RCLNO =2;	/** Final Integer to represent Stock Quantity Column of the Quality Parameter Details Table.*/
	private final int TB2_PKGTP =3;	/** Final Integer to represent Classification Flag Column of the Quality Parameter Details Table.*/
	private final int TB2_STKQT =4;	/** Final Integer to represent Classification Flag Column of the Quality Parameter Details Table.*/
	private final int TB2_STKPK =5;	/** Final Integer to represent Classification Flag Column of the Quality Parameter Details Table.*/
	private final int TB2_CLSFL =6;
	
	
									/** Final Integer to represent Check Flag Column of the Reserved Stock Details Table.*/
	private final int TB3_CHKFL =0;	/** Final Integer to represent Lot Number Column of  the Reserved Stock Details Table.*/
	private final int TB3_LOTNO =1;	/** Final Integer to represent Reclassification Number Column of the Reserved Stock Details Table.*/
	private final int TB3_RCLNO =2;	/** Final Integer to represent Stock Quantity Column of the Reserved Stock Details Table.*/
	private final int TB3_STKQT =3;	/** Final Integer to represent Remark Description of the Reserved Stock Details Table.*/
	private final int TB3_REMDS =4;
									/** StringBuffer object to dynamically generate the Dotted Line.*/
	private StringBuffer stbDOTLN;	/** StringBuffer Object to genetate Header Part of the Report.*/
	private StringBuffer stbHEADR;	/** StringBuffer Object to hold Report Data.*/
	private StringBuffer stbDATA;
									/** String variable for generated Report File Name.*/
	private String strFILNM;		/** String variable for Lot Count.*/
	private String strLTCNT="00";	/** String variable for Stock Quantity.*/
	private String strSTKQT="00";	/** String variable for Reserved Stock Quantity.*/
	private String strTDSQT="00";	/** String variable for Available Stock Quantity.*/
	private String strSTKAV="00";	/** String variable for Unclassified Stock Quantity.*/
	private String strSTKUC="00";	/** String variable for Classification Flag.*/
	private String strSTKQT_PK="00";	/** String variable for Reserved Stock Quantity.*/
	private String strTDSQT_PK="00";	/** String variable for Available Stock Quantity.*/
	private String strSTKAV_PK="00";	/** String variable for Unclassified Stock Quantity.*/
	private String strSTKUC_PK="00";	/** String variable for Classification Flag.*/
	private String strCLSFL ="";	/** String variable for Remark Description.*/
	private String strRMKDS;		/** String variable for Reservation Number.*/
	private String strRESNO ="";	/** Array of String for Quality Parameter Code. */
	private String[] arrQPRCD;  
									/** HashTable for Product Code & Description.*/
	private Hashtable<String,String> hstPRDDS;		/** HashTable for Package Type Code & Description.*/
	private Hashtable<String,String> hstPKGTP;		/** Integer variable for Total Row Width.*/
	private Hashtable<String,String> hstPKGTP1;		/** Integer variable for Total Row Width.*/
	private int intROWWD;			/** Integer variable for Width of a Column.*/
	private int intCOLWD;
								/** Flag to check that Procuct Type & Code for Blank.*/
	private boolean flgINPOK;	/**	Integer variable for Test Count.*/
	private int intTSTCT;		/** Integer Variable for Quality parameter Count.*/
	private int intQPRCT =11;   /** Charector variable for Stock Query Condiation.*/
	private char chrOPTST ;   	
	private final char chrDOPST_fnl = 'D';
	private final char chrCURST_fnl = 'C';				        
   	 										/** FileOutPutSteam Object to generate the Report file form stream of Data.*/	
    private FileOutputStream fosREPORT;		/** DataOutPutSteam Object to generate the Stream of Report File.*/	
    private DataOutputStream dosREPORT;		/** Table Input Verifier to manage the data fetchingfor selected Lot Number.*/
	private TableInputVerifier TBLINPVF;
	private TBLINPVF objTBLVRF;
	
	qc_qrstk()
	{
	    super(2);
		try
		{
			//System.out.println(" IN RECT Constructours ");
		    add(lbl4 = new JLabel("Display Option"),1,1,1,1,this,'L');
			rdoDOSTK = new JRadioButton("Day Opening Stock",true);
			rdoCUSTK = new JRadioButton("Current Stock");
			bgrSTKST = new ButtonGroup();
			bgrSTKST.add(rdoDOSTK);
			bgrSTKST.add(rdoCUSTK);
			add(rdoDOSTK,1,3,1,1.7,this,'R');
			add(rdoCUSTK,1,4,1,1.5,this,'L');	
		  	//add(chbINSAM = new JCheckBox("Include Retention Sample",false),1,6,1,2,this,'L');	
			rdoINSAM = new JRadioButton("Include Retention Sample");
			rdoEXSAM = new JRadioButton("Exclude Retention Sample",true);
			bgrSMPLE = new ButtonGroup();
			bgrSMPLE.add(rdoINSAM);
			bgrSMPLE.add(rdoEXSAM);
			add(rdoINSAM,1,6,1,2,this,'L');
			add(rdoEXSAM,2,6,1,2,this,'L');
			
		  	int LM_ROWCNT =200;
		  	setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);		
			add(lblTBL1 = new JLabel("Product Details "),2,1,1,1.1,this,'L');		
	
			lblTBL1.setForeground(Color.blue);
					
			LM_ROWCNT =6;
			String[] LM_ARRGHD = {"Select","Product Type","Product ","Grade","Product Code"};
			int[] LM_ARRGSZ = {30,78,125,90,120};
			tblPRDDL = crtTBLPNL1(this,LM_ARRGHD,LM_ROWCNT,3,1,3.6,5,LM_ARRGSZ,new int[]{0}) ;	
			
			add(lblTBL2 = new JLabel("Lot Details "),7,1,1,1,this,'L');
			lblTBL2.setForeground(Color.blue);		
			LM_ROWCNT =300;
			String[] LM_ARRGHD1 = {"Select","Lot No.","RCL No.","Pkg Type","Stock Qty ","Pkgs","Cl. Status ","DSP","MFI","IZO","VIC","TS","EL","RSM","WI","a","b","Y1"};// Table Header
			int[] LM_ARRGSZ1 = {25,65,30,55,65,60,70,40,40,40,40,40,40,40,40,40,40,40,40}; // Columnm Size in Table    int LM_ROWCNT =50;
			tblTSTDL = crtTBLPNL1(this,LM_ARRGHD1,LM_ROWCNT,8,1,5,8,LM_ARRGSZ1,new int[]{0}) ;
			tblTSTDL.setEnabled(false);	
			
			add(lblTBL3 = new JLabel("Lot Reservation Details "),14,1,1,2,this,'L');
			lblTBL3.setForeground(Color.blue);		
			String[] LM_ARRGHD3 = {"Select","Lot Number","RCL No.","Stock ","Remark        "};
			int[] LM_ARRGSZ3 = {40,80,50,80,247}; // Columnm Size in Table    int LM_ROWCNT =50;
			tblTDSDL = crtTBLPNL1(this,LM_ARRGHD3,LM_ROWCNT,15,1,3.9,5,LM_ARRGSZ3,new int[]{0}) ;
			tblTDSDL.setEnabled(false);
		
		///
		    add(lblQTY = new JLabel("Qty."),13,7,1,1,this,'L');
		    add(lblPKG = new JLabel("Pkgs."),13,8,1,1,this,'L');			
		    
		    add(new JLabel("Number Of Lots"),14,6,1,1.3,this,'L');
			add(lblLTCNT = new JLabel(" "),14,7,1,1,this,'L');			
			
			add(new JLabel("Total Stock"),15,6,1,1.3,this,'L');
			add(lblSTKQT = new JLabel(" "),15,7,1,1,this,'L');			
			add(lblSTKQT_PK = new JLabel(" "),15,8,1,1,this,'L');			
			
			add(new JLabel("Reserved Stock "),16,6,1,1.3,this,'L');
			add(lblTDSQT = new JLabel(" "),16,7,1,1,this,'L');			
			add(lblTDSQT_PK = new JLabel(" "),16,8,1,1,this,'L');			
			
			add(new JLabel("Available Stock "),17,6,1,1.3,this,'L');
			add(lblSTKAV = new JLabel(" "),17,7,1,1,this,'L');			
			add(lblSTKAV_PK = new JLabel(" "),17,8,1,1,this,'L');			
			
			add(new JLabel("Unclassified Stock "),18,6,1,1.3,this,'L');														
			add(lblSTKUC = new JLabel(" "),18,7,1,1,this,'L');
			add(lblSTKUC_PK = new JLabel(" "),18,8,1,1,this,'L');
			
			lblSTKQT.setForeground(Color.blue);
			lblTDSQT.setForeground(Color.blue);
			lblSTKAV.setForeground(Color.blue);
			lblLTCNT.setForeground(Color.blue);
			lblSTKUC.setForeground(Color.blue);	
			
			lblSTKQT_PK.setForeground(Color.blue);
			lblTDSQT_PK.setForeground(Color.blue);
			lblSTKAV_PK.setForeground(Color.blue);
			lblSTKUC_PK.setForeground(Color.blue);			
		///										
		//	add(btnSUMRY = new JButton("Summary"),16,7,1,1.2,this,'L');
		//	add(btnCLSDL = new JButton("Cls Details"),17,7,1,1.2,this,'L');
			
			lbl4.setForeground(Color.blue);	
			tblPRDDL.addFocusListener(this);
			tblPRDDL.addKeyListener(this);
			tblPRDDL.addMouseListener(this);
			tblTSTDL.addMouseListener(this);
			tblTDSDL.addMouseListener(this);
			M_pnlRPFMT.setVisible(true);
		    					
			//getDTL();
			arrQPRCD = new String[intQPRCT]; 		    
			M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_NMP02 from CO_CDTRN where CMT_CGMTP = 'RPT' ";
			M_strSQLQRY += " AND CMT_CGSTP = 'QCXXPTA'" ;
			M_strSQLQRY += " ORDER BY CMT_NCSVL";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				int i=0;
				while(M_rstRSSET.next())
				{
					arrQPRCD[i] = M_rstRSSET.getString("CMT_CODCD");
					i=i+1;					
				}
			    M_rstRSSET.close();
			}		
			
			stbHEADR = new StringBuffer();
			stbDOTLN = new StringBuffer("--");			
			
			hstPKGTP = new Hashtable<String,String>();			
			hstPKGTP1 = new Hashtable<String,String>();			
			M_strSQLQRY = "select CMT_CODCD,CMT_SHRDS,CMT_NCSVL from CO_CDTRN where"
			+ " CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'FGXXPKG'";
			ResultSet M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstPKGTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_SHRDS"));
					hstPKGTP1.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_NCSVL"));
				}
				M_rstRSSET.close();
			}	
			tblPRDDL.setCellEditor(TB1_PRDTP, txtPRDTP = new TxtLimit(2));
			txtPRDTP.addKeyListener(this);
			txtPRDTP.addFocusListener(this);
			
			tblPRDDL.setCellEditor(TB1_PRDDS, txtPRDCD = new JTextField());
			txtPRDCD.addKeyListener(this);
			txtPRDCD.addFocusListener(this);
		
			chrOPTST = chrDOPST_fnl;
			objTBLVRF = new TBLINPVF();
				tblPRDDL.setInputVerifier(objTBLVRF);
  			//tblPRDDL.requestFocus();
			//tblPRDDL.setRowSelectionInterval(TB1_CHKFL,TB1_CHKFL);
			///tblPRDDL.setColumnSelectionInterval(1,2);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	void setENBL(boolean L_STAT)
	{
	    super.setENBL(L_STAT);
	    //tblPRDDL.cmpEDITR[TB1_PRDTP].setEnabled(false);
		M_rdbTEXT.setSelected(false);
		M_rdbHTML.setSelected(true);
	    tblPRDDL.cmpEDITR[TB1_PRDCD].setEnabled(false);
	    tblPRDDL.cmpEDITR[TB1_PRDCT].setEnabled(false);
	    tblPRDDL.cmpEDITR[TB1_DSPFL].setEnabled(true);
	    tblTSTDL.setEnabled(false);
	    tblTDSDL.setEnabled(false);
	    tblTSTDL.cmpEDITR[TB2_CHKFL].setEnabled(true);
	}
	
	public void actionPerformed(ActionEvent L_AE)
    {
	    super.actionPerformed(L_AE);
		/*if(M_objSOURC == btnSUMRY)
		{
			pnlSUMRY = new JPanel(null);			
			add(new JLabel("Number Of Lots"),1,1,1,1.3,pnlSUMRY,'L');
			add(lblLTCNT = new JLabel("A"),1,2,1,1,pnlSUMRY,'L');			
			add(new JLabel("Total Stock"),2,1,1,1.3,pnlSUMRY,'L');
			add(lblSTKQT = new JLabel("A"),2,2,1,1,pnlSUMRY,'L');			
			add(new JLabel("Reserved Stock "),3,1,1,1.3,pnlSUMRY,'L');
			add(lblTDSQT = new JLabel("A"),3,2,1,1,pnlSUMRY,'L');			
			add(new JLabel("Available Stock "),4,1,1,1.3,pnlSUMRY,'L');
			add(lblSTKAV = new JLabel("A"),4,2,1,1,pnlSUMRY,'L');			
			add(new JLabel("Unclassified Stock "),5,1,1,1.3,pnlSUMRY,'L');														
			add(lblSTKUC = new JLabel("A"),5,2,1,1,pnlSUMRY,'L');
			
			lblSTKQT.setForeground(Color.blue);
			lblTDSQT.setForeground(Color.blue);
			lblSTKAV.setForeground(Color.blue);
			lblLTCNT.setForeground(Color.blue);
			lblSTKUC.setForeground(Color.blue);			
			
			lblLTCNT.setText(padSTRING('L',strLTCNT,20));
			lblSTKQT.setText(padSTRING('L',strSTKQT,15)+" MT");
			lblTDSQT.setText(padSTRING('L',strTDSQT,17)+" MT");
			lblSTKAV.setText(padSTRING('L',strSTKAV,15)+" MT");
			lblSTKUC.setText(padSTRING('L',strSTKUC,17)+" MT");	
						
			pnlSUMRY.setSize(100,100);
			pnlSUMRY.setPreferredSize(new Dimension(300,300));				
			JOptionPane.showConfirmDialog( this,pnlSUMRY,"Stock Summary",JOptionPane.OK_CANCEL_OPTION);				
		}*/
		if(M_objSOURC == rdoDOSTK)		
			chrOPTST = chrDOPST_fnl;					
		else if(M_objSOURC == rdoCUSTK)		
			chrOPTST = chrCURST_fnl;		
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{						
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
		}							
    }
	public void keyPressed(KeyEvent L_KE)
    {
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
			/*if(M_objSOURC == tblPRDDL)
			{
				if(tblPRDDL.getSelectedColumn() == TB1_PRDTP)
				{
					tblPRDDL.setColumnSelectionInterval(TB1_PRDTP,TB1_PRDDS);	
					tblPRDDL.setRowSelectionInterval(tblPRDDL.getSelectedRow(),tblPRDDL.getSelectedRow());
				    tblPRDDL.editCellAt(tblPRDDL.getSelectedRow(),TB1_PRDTP);
					if(!cl_dat.M_flgHELPFL_pbst)
					{
						if(exePTPVL(tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDTP).toString()))
						{
							setMSG("",'N');
						}
						else
						{
							setMSG("Invalid Product Type Category ",'N');
						}
					}					
				}
				else if(tblPRDDL.getSelectedColumn() == TB1_PRDCT)
				{
					tblPRDDL.setColumnSelectionInterval(TB1_PRDCT,TB1_PRDDS);	
				}
				else if(tblPRDDL.getSelectedColumn() == TB1_PRDDS)
				{
					tblPRDDL.setRowSelectionInterval(tblPRDDL.getSelectedRow(),tblPRDDL.getRowCount());
					tblPRDDL.setColumnSelectionInterval(TB1_PRDTP,TB1_PRDDS);	
					tblPRDDL.editCellAt(tblPRDDL.getSelectedRow(),TB1_PRDTP);
					if(!cl_dat.M_flgHELPFL_pbst)
					{
						if(exePRDVL(tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDTP).toString(),tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDDS).toString()))						
							setMSG("",'N');						
						else						
							setMSG("Invalid Grade ",'N');						
					}										
				}
			}*/			 
		}
        else if (L_KE.getKeyCode()== L_KE.VK_F1)        
        {	
			//if(M_objSOURC == tblPRDDL.cmpEDITR[TB1_PRDTP])				
			if(M_objSOURC == txtPRDTP)				
			{				
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPRDTP";				
				M_strSQLQRY = "Select CMT_CCSVL,CMT_CODDS from CO_CDTRN where "
					+"CMT_CGMTP = 'MST' AND CMT_CGSTP ='COXXSST' AND CMT_CODCD like 'PR_0%' ";				
				String[] L_staHEADR = {"Product Type","Description "};				
				cl_hlp(M_strSQLQRY,2,1,L_staHEADR,2,"CT");				
			}				
			else if(M_objSOURC == txtPRDCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;				
				M_strHLPFLD = "txtPRDCD";																			
				M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_STSFL <> 'X' ";
				M_strSQLQRY += " AND PR_PRDTP = "+"'"+tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDTP)+"'"+" order by PR_PRDDS ";				
				String[] L_staHEADR = {"Grade","Product Code"};				
				cl_hlp(M_strSQLQRY,2,2,L_staHEADR,2,"CT");				
			}			
		}        
    }
	public void focusGained(FocusEvent L_FE)
    {
		if(L_FE.getSource().equals(tblPRDDL))
		{
			if(tblPRDDL.getSelectedColumn() == TB1_PRDTP)
				setMSG("press F1 to select the Product Type,click to view the details..",'N');
			else if(tblPRDDL.getSelectedColumn() == TB1_PRDDS)
				setMSG("press F1 to select the Grade..",'N');
			else
				setMSG(" ",'N');
		}				
    }
	/**
	 * super class Method Overrided here to execute the F1 Help. 
	 */
	void exeHLPOK()
    {
        super.exeHLPOK(); 
		//if (M_strHLPFLD.equals("LM_PRDTP"))
		if(M_strHLPFLD.equals("txtPRDTP"))
        {
			//if(tblPRDDL.isEditing())
			//	tblPRDDL.getCellEditor().stopCellEditing();
			//System.out.println("SelectedRow"+tblPRDDL.getSelectedRow());
			//tblPRDDL.setRowSelectionInterval(tblPRDDL.getSelectedRow(),1);//(tblPRDDL.getSelectedRow(),tblPRDDL.getSelectedColumn());
			//tblPRDDL.setColumnSelectionInterval(tblPRDDL.getSelectedRow(),1);//(tblPRDDL.getSelectedRow(),tblPRDDL.getSelectedColumn());
			
			//tblPRDDL.setValueAt(cl_dat.M_strHLPSTR_pbst,tblPRDDL.getSelectedRow(),TB1_PRDTP);
			//tblPRDDL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblPRDDL.getSelectedRow(),TB1_PRDCT);						
			
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_STRTKN.nextToken();
			txtPRDTP.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
			tblPRDDL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblPRDDL.getSelectedRow(),TB1_PRDCT);
		
			
			/*tblPRDDL.requestFocus();
			tblPRDDL.setRowSelectionInterval(tblPRDDL.getSelectedRow(),0);//(tblPRDDL.getSelectedRow(),TB1_PRDCD);
			tblPRDDL.setColumnSelectionInterval(tblPRDDL.getSelectedRow(),0);//(tblPRDDL.getSelectedRow(),tblPRDDL.getSelectedColumn()+2);*/
			//tblPRDDL.editCellAt(tblPRDDL.getSelectedRow(),tblPRDDL.getSelectedColumn()+2);
	    }
		if (M_strHLPFLD.equals("txtPRDCD"))
        {
			//if(tblPRDDL.isEditing())
			//	tblPRDDL.getCellEditor().stopCellEditing();
			//System.out.println("SelectedRow"+tblPRDDL.getSelectedRow());
			//tblPRDDL.setRowSelectionInterval(tblPRDDL.getSelectedRow(),3);//(tblPRDDL.getSelectedRow(),tblPRDDL.getSelectedColumn());
			//tblPRDDL.setColumnSelectionInterval(tblPRDDL.getSelectedRow(),3);//(tblPRDDL.getSelectedRow(),tblPRDDL.getSelectedColumn());	
			
			//txtBNKCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
			//tblRECEN.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblRECEN.getSelectedRow(),TB1_BNKNM);
		
				
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_STRTKN.nextToken();
			txtPRDCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());	
			tblPRDDL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim(),tblPRDDL.getSelectedRow(),TB1_PRDCD);
			tblPRDDL.setValueAt(new Boolean(false),tblPRDDL.getSelectedRow(),TB1_DSPFL);

			
			//tblPRDDL.setValueAt(cl_dat.M_strHLPSTR_pbst,tblPRDDL.getSelectedRow(),TB1_PRDDS);//TB1_PRDDS);
			//tblPRDDL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim(),tblPRDDL.getSelectedRow(),TB1_PRDCD);
			/*
			tblPRDDL.requestFocus();
			tblPRDDL.setRowSelectionInterval(1,1);
			tblPRDDL.setColumnSelectionInterval(1,0);*/
		}		 
        cl_dat.M_flgHELPFL_pbst = false;
    }			
	public void mouseClicked(MouseEvent L_ME)
	{
		int L_intTDSCNT =0;
		double L_dblSTKQTY = 0;	
		String L_strLOTNO,L_strRCLNO;		
		/*if(M_objSOURC == tblTSTDL)
		{			
			if(tblTSTDL.getSelectedColumn() == 1)
			{
				if(tblTSTDL.getValueAt(tblTSTDL.getSelectedRow(),TB2_LOTNO).toString().length()>0)
				{
					setMSG("Fetching the Classification Details..",'N');
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					getCHKREC("tblTSTDL");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else
					setMSG("Please click on a valid Row ..",'E');
			}
		}
		else if(M_objSOURC == tblTDSDL)
		{
			if(tblTDSDL.getSelectedColumn() == 1)
			{
				if(tblTDSDL.getValueAt(tblTDSDL.getSelectedRow(),TB2_LOTNO).toString().length()>0)
				{
					setMSG("Fetching the Classification Details..",'N');
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					getCHKREC("tblTDSDL");		
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else
					setMSG("Please click on a valid Row ..",'E');
			}
		}		
		if(M_objSOURC == tblTDSDL)
		{
			if(tblTDSDL.getSelectedColumn() == TB3_REMDS)
			{
				if(tblTDSDL.getValueAt(tblTDSDL.getSelectedRow(),TB3_REMDS).toString().length()>0)
				{
					//setDSC(tblTDSDL.getValueAt(tblTDSDL.getSelectedRow(),TB3_REMDS).toString());
				}
			}
		}*/
       if(M_objSOURC == tblTSTDL)
        {
            for(int i=0;i<tblTSTDL.getRowCount();i++)
            {
                if(i !=tblTSTDL.getSelectedRow())
                    tblTSTDL.setValueAt(Boolean.FALSE,i,TB2_CHKFL);
                else
                    tblTSTDL.setValueAt(Boolean.TRUE,i,TB2_CHKFL);
            }
            cl_dat.M_btnUNDO_pbst.setEnabled(false);
            qc_qrlcl obj = new qc_qrlcl(0);             // create an object
        	cl_dat.M_pnlFRBTM_pbst.add("screen1", obj); // add it to the card layout
			cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen1"); // show this card
            //obj.exeLTQRY(tblTSTDL.getValueAt(tblTSTDL.getSelectedRow(),TB2_LOTNO).toString(),"00","01","01");  90
            obj.exeLTQRY(tblTSTDL.getValueAt(tblTSTDL.getSelectedRow(),TB2_LOTNO).toString(),"00","01",tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDTP).toString().trim());  
        }
        if(M_objSOURC == tblPRDDL)
        {
            if(tblTDSDL.getSelectedColumn() == TB1_DSPFL)
    		{
    			setMSG("",'N');
    			int L_intROWNO = tblPRDDL.getSelectedRow();						
    			for (int i =0; i<tblPRDDL.getRowCount();i++)
    				tblPRDDL.setValueAt(new Boolean(false),i,TB1_DSPFL);
    			tblPRDDL.setValueAt(new Boolean(true),L_intROWNO,TB1_DSPFL);
    			String strPRDTP = tblPRDDL.getValueAt(L_intROWNO,TB1_PRDTP).toString();
    			String strPRDCD = tblPRDDL.getValueAt(L_intROWNO,TB1_PRDCD).toString();
    			if((strPRDTP.trim().length() != 0) && (strPRDCD.trim().length() != 0))
    			{
    				getLOTDTL();					
    			}
    		}
        }
  	}
	public void mousePressed(MouseEvent L_ME)
	{}	
	public void mouseReleased(MouseEvent L_ME)
	{}
	/**
	 * Method to get the Lot Details.
	 */
	private void getLOTDTL()
	{
		String L_strQPRVL,L_strPRDCD;
		strLTCNT = "00";strSTKQT = "00";strTDSQT = "00";strSTKAV = "00";strSTKUC ="00";
		strSTKQT_PK = "00";strTDSQT_PK = "00";strSTKAV_PK = "00";strSTKUC_PK ="00";
		int L_intCOUNT =0,L_intTDSCNT =0;
		String L_strLOTNO,L_STKQT,L_strPRDTP="",L_strRCLNO,L_strPKGTP;
		double L_dblCLSQT = 0,L_dblCLSPK = 0,L_dblUCLQT=0,L_dblUCLPK=0,L_dblSTKQTY=0,L_dblSTKPKG=0,L_dblSMCLQT=0,L_dblSMCLPK=0,L_dblSMUCQT=0,L_dblSMUCPK=0,L_dblSMTDQT=0,L_dblSMTDPK=0;
		boolean L_flgTDSFL = false;
		strRMKDS ="";
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(tblTSTDL.isEditing())
				tblTSTDL.getCellEditor().stopCellEditing();
			tblTSTDL.setRowSelectionInterval(0,0);
			tblTSTDL.setColumnSelectionInterval(0,0);
			
			L_dblSTKQTY =0;
			L_dblSMCLQT=0;
			L_dblSMUCQT=0;
			L_dblSMTDQT =0;
			double L_dblPKGWT = 0;
			if(chrOPTST == chrCURST_fnl)
			{
				M_strSQLQRY = "Select ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_STKQT)L_dblCLSQT,sum(ST_UCLQT)L_dblUCLQT from FG_STMST,PR_LTMST where ";
				M_strSQLQRY += " ST_CMPCD = LT_CMPCD AND ST_PRDTP = LT_PRDTP AND ST_LOTNO = LT_LOTNO AND ST_RCLNO =LT_RCLNO  and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(lt_resfl,'X')) not in ('Q','H') AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (ST_STKQT+ST_UCLQT) > 0 AND ST_PRDTP = "+ "'"+ tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDTP).toString()+"'";						
			}
			else // Day opening Stock
			{
				M_strSQLQRY = "Select ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_DOSQT)L_dblCLSQT,sum(ST_DOUQT)L_dblUCLQT from FG_STMST,PR_LTMST where ";
				M_strSQLQRY += " ST_PRDTP = LT_PRDTP AND ST_LOTNO = LT_LOTNO AND ST_RCLNO =LT_RCLNO  and ST_CMPCD = LT_CMPCD and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(lt_resfl,'X')) not in ('Q','H') AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (ST_DOSQT+ST_DOUQT) > 0 AND ST_PRDTP = "+ "'"+ tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDTP).toString()+"'";					 
			}
			if(rdoEXSAM.isSelected())
                M_strSQLQRY +=" AND isnull(st_stkqt,0) <> 0.025 ";
			M_strSQLQRY += " AND ST_PRDCD = "+ "'"+ tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDCD).toString() +"'"+ " group by ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_RESNO,LT_CLSFL,ST_REMDS Order by ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO";
			
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			// commented code modified on 07/10/2002 as classified AND unclassified stocks both were getting dispalyed at HO.(as per KVM)
			tblTSTDL.clrTABLE();
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strPRDTP = M_rstRSSET.getString("ST_PRDTP");			
					L_strLOTNO = M_rstRSSET.getString("ST_LOTNO");	
					L_strRCLNO = M_rstRSSET.getString("ST_RCLNO");			
					L_strPKGTP = M_rstRSSET.getString("ST_PKGTP");
					L_dblPKGWT = Double.parseDouble(hstPKGTP1.get(L_strPKGTP).toString());
					L_dblCLSQT = M_rstRSSET.getDouble("L_dblCLSQT");					
					L_dblCLSPK = L_dblCLSQT / L_dblPKGWT;
					L_dblUCLQT = M_rstRSSET.getDouble("L_dblUCLQT");
					L_dblUCLPK = L_dblUCLQT / L_dblPKGWT;
					
					
					strRESNO = nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"");			
					strCLSFL = M_rstRSSET.getString("LT_CLSFL");			
					strRMKDS = M_rstRSSET.getString("ST_REMDS");			
					L_dblSTKQTY = L_dblCLSQT+L_dblUCLQT;
					L_dblSTKPKG = L_dblCLSPK+L_dblUCLPK;
					L_dblSMCLQT += L_dblCLSQT; // sum of Classified Quantity
					L_dblSMUCQT += L_dblUCLQT; // sum of Unclassified Quantity
					L_dblSMCLPK += L_dblCLSPK; // sum of Classified Packages
					L_dblSMUCPK += L_dblUCLPK; // sum of Unclassified Packages
					if(L_dblSTKQTY > 0 )			
					{
						
						tblTSTDL.setValueAt(L_strLOTNO,L_intCOUNT,TB2_LOTNO);
						tblTSTDL.setValueAt(L_strRCLNO,L_intCOUNT,TB2_RCLNO);			
						tblTSTDL.setValueAt(setNumberFormat(L_dblCLSQT,3),L_intCOUNT,TB2_STKQT);
						tblTSTDL.setValueAt(setNumberFormat(L_dblCLSPK,0),L_intCOUNT,TB2_STKPK);
						tblTSTDL.setValueAt(hstPKGTP.get(L_strPKGTP).toString(),L_intCOUNT,TB2_PKGTP);
						if(strCLSFL !=null)
						{
								if((strCLSFL.trim().equals("9"))||(strCLSFL.trim().equals("8")))
	   								strCLSFL ="Classified";
	   							else if(strCLSFL.trim().equals("4"))
	   								strCLSFL ="Prov. Classified";
	   							else 
	   								strCLSFL ="Unclassified";
						}
						else
						strCLSFL ="";
						tblTSTDL.setValueAt(strCLSFL,L_intCOUNT,TB2_CLSFL);
						//tblTDSDL.clrTABLE();
						//if(strRESNO.trim().length()>0)
						//{
						//	if(L_dblSTKQTY > 0)
						//	{
						//		tblTDSDL.setValueAt(L_strLOTNO,L_intTDSCNT,TB3_LOTNO);
						//		tblTDSDL.setValueAt(L_strRCLNO,L_intTDSCNT,TB3_RCLNO);
						//		tblTDSDL.setValueAt(setNumberFormat(L_dblCLSQT,3),L_intTDSCNT,TB3_STKQT);
						//		tblTDSDL.setValueAt(strRMKDS,L_intTDSCNT,TB3_REMDS);
						//		L_intTDSCNT++;
						//		L_dblSMTDQT +=L_dblCLSQT;
						//	}
						//	L_flgTDSFL = true;
						//}
						//else
						//	L_flgTDSFL = false;
						if(strRMKDS == null)
							strRMKDS ="";
						getTSTDT(L_strPRDTP,L_strLOTNO,L_strRCLNO,L_intCOUNT,"QRY");
						L_intCOUNT++;
					}
				}
				M_rstRSSET.close();
			}
			setRESDTL();
			strLTCNT = String.valueOf(L_intCOUNT).toString();
			strSTKQT = String.valueOf(setNumberFormat(L_dblSMCLQT+L_dblSMUCQT,3)).toString();
			//strSTKPK = String.valueOf(setNumberFormat(L_dblSMCLPK+L_dblSMUCPK,3)).toString();
			strTDSQT = String.valueOf(setNumberFormat(L_dblSMTDQT,3)).toString();
			//strTDSPK = String.valueOf(setNumberFormat(L_dblSMTDPK,3)).toString();
			strSTKAV = String.valueOf(setNumberFormat(L_dblSMCLQT-L_dblSMTDQT,3)).toString();
			strSTKUC = String.valueOf(setNumberFormat(L_dblSMUCQT,3)).toString();
			
			strSTKQT_PK = String.valueOf(setNumberFormat(L_dblSMCLPK+L_dblSMUCPK,0)).toString();
			//strSTKPK = String.valueOf(setNumberFormat(L_dblSMCLPK+L_dblSMUCPK,3)).toString();
			strTDSQT_PK = String.valueOf(setNumberFormat(L_dblSMTDPK,0)).toString();
			//strTDSPK = String.valueOf(setNumberFormat(L_dblSMTDPK,0)).toString();
			strSTKAV_PK = String.valueOf(setNumberFormat(L_dblSMCLPK-L_dblSMTDPK,0)).toString();
			strSTKUC_PK = String.valueOf(setNumberFormat(L_dblSMUCPK,0)).toString();
	
			if(L_strPRDTP.equals("07"))
			{
				lblQTY.setText("Qty. (Sqr.Mtr.)");
				lblPKG.setText("Pcs.");
			}
			else
			{
				lblQTY.setText("Qty. (MT)");
				lblPKG.setText("Pkgs.");
			}
    		lblLTCNT.setText(padSTRING('L',strLTCNT,20));
			lblSTKQT.setText(padSTRING('L',strSTKQT,15));
			//lblTDSQT.setText(padSTRING('L',strTDSQT,17)+" MT");
			lblSTKAV.setText(padSTRING('L',strSTKAV,15));
			lblSTKUC.setText(padSTRING('L',strSTKUC,17));	
			
			lblSTKQT_PK.setText(padSTRING('L',strSTKQT_PK,15));
			//lblTDSQT_PK.setText(padSTRING('L',strTDSQT_PK,17)+" MT");
			lblSTKAV_PK.setText(padSTRING('L',strSTKAV_PK,15));
			lblSTKUC_PK.setText(padSTRING('L',strSTKUC_PK,17));	
			
            setCursor(cl_dat.M_curDFSTS_pbst);
			/*lblLTCNT.setText(padSTRING('L',String.valueOf(L_intCOUNT),16));
			lblSTKQT.setText(padSTRING('L',String.valueOf(setNumberFormat(L_dblSMCLQT+L_dblSMUCQT,3)).toString(),12)+" MT");				
			lblTDSQT.setText(padSTRING('L',String.valueOf(setNumberFormat(L_dblSMTDQT,3)).toString(),14)+" MT");				
			lblSTKAV.setText(padSTRING('L',String.valueOf(setNumberFormat(L_dblSMCLQT-L_dblSMTDQT,3)).toString(),12)+" MT");
			lblSTKUC.setText(padSTRING('L',String.valueOf(setNumberFormat(L_dblSMUCQT,3)).toString(),14)+" MT");	*/
		}	
		catch(Exception L_SE)
		{
			//System.out.println("Error 14"+L_SE.toString());
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}			

	
	/**
	 * Displaying Reservation details
	 */
	void setRESDTL()
	{
		int L_intTDSCNT = 0;
		try
		{
			//M_strSQLQRY = "Select SR_PRDTP,SR_LOTNO,SR_RCLNO,SR_GRPCD,min(PT_PRTNM) PT_PRTNM,sum(isnull(SR_RESQT,0)) SR_RESQT from FG_SRTRN,CO_PTMST  where SR_PRDCD = '"+tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDCD).toString()+"' and isnull(SR_RESQT,0)>0 and PT_PRTTP=SR_PRTTP and PT_GRPCD = SR_GRPCD group by SR_PRDTP,SR_LOTNO,SR_RCLNO,SR_GRPCD";
			M_strSQLQRY = "Select SR_PRDTP,SR_LOTNO,SR_RCLNO,SR_PRTTP,SR_GRPCD,sum(isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)) SR_RESQT from FG_SRTRN  where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_PRDCD = '"+tblPRDDL.getValueAt(tblPRDDL.getSelectedRow(),TB1_PRDCD).toString()+"' and (isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)) >0  group by SR_PRDTP,SR_LOTNO,SR_RCLNO,SR_PRTTP,SR_GRPCD";
			//System.out.println(M_strSQLQRY);
			double L_dblRESQT_TOT = 0.000;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
				return;
			while (true)
			{
				tblTDSDL.setValueAt(M_rstRSSET.getString("SR_LOTNO").toString(),L_intTDSCNT,TB3_LOTNO);
				tblTDSDL.setValueAt(M_rstRSSET.getString("SR_RCLNO").toString(),L_intTDSCNT,TB3_RCLNO);
				tblTDSDL.setValueAt(setNumberFormat(M_rstRSSET.getDouble("SR_RESQT"),3),L_intTDSCNT,TB3_STKQT);
				L_dblRESQT_TOT += M_rstRSSET.getDouble("SR_RESQT");
				M_strSQLQRY = "Select min(PT_PRTNM) PT_PRTNM from CO_PTMST where PT_PRTTP= '"+M_rstRSSET.getString("SR_PRTTP").toString()+"' and PT_GRPCD = '"+M_rstRSSET.getString("SR_GRPCD").toString()+"'";
				System.out.println(M_strSQLQRY);
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET.next() && L_rstRSSET!=null)
					tblTDSDL.setValueAt(L_rstRSSET.getString("PT_PRTNM").toString(),L_intTDSCNT,TB3_REMDS);
				if(!M_rstRSSET.next())
					break;
				L_intTDSCNT ++;
			}
			lblTDSQT.setText(setNumberFormat(L_dblRESQT_TOT,3)+" MT");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setRESDTL ");
		}
	}
	
	/**
	* Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;	
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_qrstk.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_qrstk.doc";				
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			/*if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}*/
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
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Statement Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}		
	/**
	* Method to validate Consignment number, before execution, Check for blank AND wrong Input.
	*/
	boolean vldDATA()
	{				
		try
		{
			flgINPOK = false;
			hstPRDDS = new Hashtable<String,String>();
			for(int i=0;i<tblPRDDL.getRowCount();i++)
			{			
				if(!tblPRDDL.getValueAt(i,TB1_PRDTP).toString().equals("")) 
				{				
					if(!tblPRDDL.getValueAt(i,TB1_PRDDS).toString().equals(""))				
					{					
						flgINPOK = true;				
						hstPRDDS.put(tblPRDDL.getValueAt(i,TB1_PRDCD).toString(),tblPRDDL.getValueAt(i,TB1_PRDDS).toString());						
					}					
				}			
			}			
			if(flgINPOK == false)
			{
				setMSG("Product Type or Product Code Cannnot be Blank.. ",'E');
				return false;
			}		   		
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount()==0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
		}
		catch(Exception L_EX)		
		{
			setMSG(L_EX,"vldDATA");
		}		
		return true;
	}
	/**
    *Method to fetch data from database & club it with Header & footer in Data Output Stream.
	*/
	private void getDATA()
	{				
		String L_strQPRVL,L_strPRDCD;
		cl_dat.M_PAGENO = 0;
		int L_intCOUNT =0;
		String L_strPKGTP="";
		String L_strLOTNO,L_strRCLNO,L_STKQT,L_strPRDTP,L_PRVPRD="",L_CNDSTR="";
		double L_dblCLSQT = 0,L_dblUCLQT=0,L_dblSTKQTY=0,L_dblSMCLQT=0,L_dblSMUCQT=0,L_dblSMSTQT=0,L_dblSMTDQT=0;
		double L_dblCLSPK = 0,L_dblUCLPK=0,L_dblSTKPKG=0,L_dblSMCLPK=0,L_dblSMUCPK=0,L_dblSMSTPK=0,L_dblSMTDPK=0;
		
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
		{
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Stock Statement Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}	
			prnHEADER();
						
			//getREPDTL();
			
			L_dblSTKQTY =0;
			L_dblSMCLQT=0;
			L_dblSMUCQT=0;
			if(chrOPTST == chrCURST_fnl)
			{
				M_strSQLQRY = "Select ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_PKGTP,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_STKQT)L_dblCLSQT,sum(ST_UCLQT)L_dblUCLQT from FG_STMST,PR_LTMST where ";
				M_strSQLQRY += " ST_CMPCD = LT_CMPCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STKQT+ST_UCLQT >0 AND ST_STSFL <> 'X' AND ";		
			}
			else // Day opening Stock
			{
				M_strSQLQRY = "Select ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_DOSQT)L_dblCLSQT,sum(ST_DOUQT)L_dblUCLQT from FG_STMST,PR_LTMST where ";
				M_strSQLQRY += " ST_CMPCD=LT_CMPCD AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_DOSQT+ST_DOUQT >0 AND ";		
			}
			if(rdoEXSAM.isSelected())
	                M_strSQLQRY += " isnull(ST_STKQT,0) <> 0.025 AND"; 
			M_strSQLQRY += " ST_PRDTP = LT_PRDTP AND ST_LOTNO = LT_LOTNO AND ST_RCLNO = LT_RCLNO  and upper(isnull(lt_resfl,'X')) not in ('Q','H')  AND ST_PRDTP  + ST_PRDCD IN(";
			for(int i=0;i< tblPRDDL.getRowCount();i++)
			{
				if((tblPRDDL.getValueAt(i,TB1_PRDTP).toString().length()>0)&&(tblPRDDL.getValueAt(i,TB1_PRDCD).toString().length()>0))
				{
					if(i==0)
						L_CNDSTR = "'"+tblPRDDL.getValueAt(i,TB1_PRDTP).toString().trim()+tblPRDDL.getValueAt(i,TB1_PRDCD).toString().trim()+"'";
					else
						L_CNDSTR += ",'"+tblPRDDL.getValueAt(i,TB1_PRDTP).toString().trim()+tblPRDDL.getValueAt(i,TB1_PRDCD).toString().trim()+"'";
				}
			}
			L_CNDSTR +=")";
			M_strSQLQRY +=L_CNDSTR ;
			M_strSQLQRY += " group by ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS,ST_PKGTP Order by ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_RCLNO";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println("M_strSQLQRY" +M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			   	while(M_rstRSSET.next())
				{
					stbDATA = new StringBuffer();
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("ST_PRDCD"),"");			
					L_strPRDTP = nvlSTRVL(M_rstRSSET.getString("ST_PRDTP"),"");
					L_strPKGTP = M_rstRSSET.getString("ST_PKGTP");
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("ST_LOTNO"),"");			
					L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("ST_RCLNO"),"");			
					strRESNO = nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"");
					strCLSFL = M_rstRSSET.getString("LT_CLSFL");			
					strRMKDS = M_rstRSSET.getString("ST_REMDS");			
					L_dblCLSQT = M_rstRSSET.getDouble("L_dblCLSQT");			
					L_dblUCLQT = M_rstRSSET.getDouble("L_dblUCLQT");			
					
					if(!L_strPRDCD.equals(L_PRVPRD))
					{			
					  	if(L_PRVPRD.trim().length()>0)
						{
							dosREPORT.writeBytes(padSTRING('R'," ",9));
						  	dosREPORT.writeBytes(stbDOTLN.toString());
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							//prnSMRY(L_intCOUNT,L_dblSMSTQT,L_dblSMTDQT,L_dblSMUCQT);
							dosREPORT.writeBytes("\n");		
							dosREPORT.writeBytes(padSTRING('R'," ",9));
							dosREPORT.writeBytes(padSTRING('R',"Number of Lots : "+String.valueOf(L_intCOUNT),35));
							dosREPORT.writeBytes(padSTRING('R',"Total Stock       : "+padSTRING('L',setNumberFormat(L_dblSMSTQT,3),12),35));
							dosREPORT.writeBytes(padSTRING('R',"Stock Available    : "+padSTRING('L',setNumberFormat(L_dblSMSTQT-L_dblSMTDQT-L_dblSMUCQT,3),12),35));
							dosREPORT.writeBytes("\n");			
							dosREPORT.writeBytes(padSTRING('R'," ",44));
							dosREPORT.writeBytes(padSTRING('R',"Target Despatches : "+padSTRING('L',setNumberFormat(L_dblSMTDQT,3),12),35));
							dosREPORT.writeBytes(padSTRING('R',"Unclassified Stock : "+padSTRING('L',setNumberFormat(L_dblSMUCQT,3),12),35));
							dosREPORT.writeBytes("\n");			
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b>");
							dosREPORT.writeBytes(padSTRING('R'," ",9));
							dosREPORT.writeBytes(stbDOTLN.toString());
							cl_dat.M_intLINNO_pbst += 4;
						
							L_dblSMSTQT = 0;
							L_dblSMTDQT = 0;
							L_dblSMUCQT = 0;
							L_dblSMCLQT = 0;
							L_dblSTKQTY = 0;
							L_intCOUNT= 0;
						}
						if(cl_dat.M_intLINNO_pbst> 68)
						{
							dosREPORT.writeBytes("\n");	
							dosREPORT.writeBytes(padSTRING('R'," ",9));
							dosREPORT.writeBytes(stbDOTLN.toString());			
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
						}
						dosREPORT.writeBytes("\n\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						dosREPORT.writeBytes(padSTRING('R'," ",9));
						dosREPORT.writeBytes(hstPRDDS.get(L_strPRDCD).toString());
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst += 4;
						L_dblSTKQTY = L_dblCLSQT+L_dblUCLQT;
						L_dblSMSTQT +=L_dblSTKQTY;
						L_dblSMCLQT +=L_dblCLSQT;
						L_dblSMUCQT +=L_dblUCLQT;
						L_PRVPRD = L_strPRDCD;
					}
					else
					{
					  	L_dblSTKQTY = L_dblCLSQT+L_dblUCLQT;
						L_dblSMSTQT +=L_dblSTKQTY;
						L_dblSMCLQT +=L_dblCLSQT;
						L_dblSMUCQT +=L_dblUCLQT;					
					}
					if((strCLSFL.trim().equals("9"))||(strCLSFL.trim().equals("8")))
		   				strCLSFL ="C";
		   			else if(strCLSFL.trim().equals("4"))
		   				strCLSFL ="P";
		   			else 
		   				strCLSFL ="U";
					if(strRMKDS == null)
						strRMKDS =" ";					
					if(L_dblSTKQTY > 0 )		
					{
						stbDATA.append(padSTRING('R'," ",9));
						stbDATA.append(padSTRING('R',L_strLOTNO,9));
						stbDATA.append(padSTRING('L',hstPKGTP.get(L_strPKGTP).toString(),8));
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblCLSQT,3),8));
						stbDATA.append(padSTRING('L',strCLSFL,4));	
						getTSTDT(L_strPRDTP,L_strLOTNO,L_strRCLNO,L_intCOUNT,"REP");
						if(intTSTCT == 0)
							stbDATA.append(padSTRING('L',"",65));	
						if(strRESNO.trim().length()>0)				
							L_dblSMTDQT += L_dblCLSQT;
						dosREPORT.writeBytes(stbDATA.toString());						
						dosREPORT.writeBytes(padSTRING('L',"",3));
						dosREPORT.writeBytes(strRMKDS);							
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						if(cl_dat.M_intLINNO_pbst> 68)
						{
							dosREPORT.writeBytes("\n");	
							dosREPORT.writeBytes(padSTRING('R'," ",9));
							dosREPORT.writeBytes(stbDOTLN.toString());			
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 1;
						}
						L_intCOUNT++;
					}
				}
				M_rstRSSET.close();
			}
			if(L_intCOUNT >0)
			{
				dosREPORT.writeBytes(padSTRING('R'," ",9));
				dosREPORT.writeBytes(stbDOTLN.toString());				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<b>");
				//prnSMRY(L_intCOUNT,L_dblSMSTQT,L_dblSMTDQT,L_dblSMUCQT);
				dosREPORT.writeBytes("\n");		
				dosREPORT.writeBytes(padSTRING('R'," ",9));
				dosREPORT.writeBytes(padSTRING('R',"Number of Lots : "+String.valueOf(L_intCOUNT),35));
				dosREPORT.writeBytes(padSTRING('R',"Total Stock       : "+padSTRING('L',setNumberFormat(L_dblSMSTQT,3),12),35));
				dosREPORT.writeBytes(padSTRING('R',"Stock Available    : "+padSTRING('L',setNumberFormat(L_dblSMSTQT-L_dblSMTDQT-L_dblSMUCQT,3),12),35));
				dosREPORT.writeBytes("\n");			
				dosREPORT.writeBytes(padSTRING('R'," ",44));
				dosREPORT.writeBytes(padSTRING('R',"Target Despatches : "+padSTRING('L',setNumberFormat(L_dblSMTDQT,3),12),35));
				dosREPORT.writeBytes(padSTRING('R',"Unclassified Stock : "+padSTRING('L',setNumberFormat(L_dblSMUCQT,3),12),35));
				dosREPORT.writeBytes("\n");			
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</b>");				
			}
			dosREPORT.writeBytes(padSTRING('R'," ",9));
			dosREPORT.writeBytes(stbDOTLN.toString());
						
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			//if(M_rdbHTML.isSelected())			
			  //  dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    			
		}
		catch(Exception L_EX)
		{
			//System.out.println(L_EX.toString());
			setMSG(L_EX,"getDATA");
		}
	}	
	/**
	 * Method to Generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			intCOLWD = 6;
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			if(cl_dat.M_PAGENO ==1)				
			{
				stbHEADR.delete(0,stbHEADR.toString().length());
				stbHEADR.append(padSTRING('R'," ",9));
				stbHEADR.append(padSTRING('R',"Lot No.",9));
				stbHEADR.append(padSTRING('L',"Pkg.Type",8));
				stbHEADR.append(padSTRING('L',"Stock",8)+" ");
				stbHEADR.append(padSTRING('R',"Cls",3));	
				intROWWD = 58;
				for(int j=0;j<intQPRCT;j++)
				{
					String LM_DESC = cl_dat.getPRMCOD("CMT_SHRDS","SYS","QCXXQPR",arrQPRCD[j]);
					if(LM_DESC !=null)
					{
						LM_DESC = LM_DESC.trim();
						stbHEADR.append(padSTRING('L',LM_DESC,intCOLWD));
						intROWWD += intCOLWD;
					}
				}
				stbHEADR.append("  Remarks");								
				stbDOTLN.delete(0,stbDOTLN.toString().length());
				for(int i =0; i< intROWWD;i++)
					stbDOTLN.append("-");
			}			
			dosREPORT.writeBytes("\n\n\n\n\n");	
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R'," ",9));
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWWD -46));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst +"\n");
			dosREPORT.writeBytes(padSTRING('R'," ",9));
			dosREPORT.writeBytes(padSTRING('R',"Statement of Stock Position on "+cl_dat.M_strLOGDT_pbst,intROWWD -46));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n" );			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(padSTRING('R'," ",9));
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			dosREPORT.writeBytes(stbHEADR.toString()+"\n");
			dosREPORT.writeBytes(padSTRING('R'," ",9));
			dosREPORT.writeBytes(stbDOTLN.toString());
			cl_dat.M_intLINNO_pbst =10;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}				
	/**
	 * Method to get The Quality parameter values gor given lots.
	 * @param P_strPRDTP String argument to pass Product type.
	 * @param P_strLOTNO String argument to pass Lot Number.
	 * @param P_strRCLNO String argument to pass Reclassification Numner.
	 * @param P_intCNT integer argumnet to pass Quality Parameter Count
	 * @param P_strREPQRY String argument to pass SQL Query.
	 */
	public void getTSTDT(String P_strPRDTP,String P_strLOTNO,String P_strRCLNO,int P_intCNT,String P_strREPQRY) // Get the Resultset of details, Lot details in a run No. 
	{
		intTSTCT =0;
		String L_strTSTTP ="0103";		
		String L_strQPRVL ="";
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "Select LT_LOTNO, ";
			int k=0;
			M_strSQLQRY += "PS_"+arrQPRCD[k].trim()+"VL";
			for(k=1;k<arrQPRCD.length;k++)
			{
				M_strSQLQRY += ","+"PS_"+arrQPRCD[k].trim()+"VL";
			}
			M_strSQLQRY +=" FROM QC_PSMST,PR_LTMST where LT_CMPCD = PS_CMPCD AND LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO  and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(lt_resfl,'X')) not in ('Q','H') AND";
			M_strSQLQRY +=" PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = " + "'"+P_strPRDTP + "'" + " AND PS_TSTTP = " + "'"+ L_strTSTTP + "'";
			M_strSQLQRY += " AND PS_LOTNO = "+"'"+P_strLOTNO +"'";
			M_strSQLQRY += " AND PS_RCLNO = "+"'"+P_strRCLNO +"'";
			M_strSQLQRY += " AND PS_STSFL <> 'X' ";		
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
	 		if(L_rstRSSET.next())
			{
				intTSTCT ++;
				for(int l=0;l<arrQPRCD.length;l++)
	   			{
	   				L_strQPRVL = "-"; 	   			
	   				L_strQPRVL = L_rstRSSET.getString("PS_"+arrQPRCD[l].trim()+"VL");
	   				if(L_strQPRVL == null)
	   					L_strQPRVL = "-";
	   				if(P_strREPQRY.equals("QRY"))
					{
						//tblTSTDL.setValueAt(strCLSFL,P_intCNT,TB2_CLSFL);
	   					tblTSTDL.setValueAt(L_strQPRVL,P_intCNT,l+6);				
					}
					else if(P_strREPQRY.equals("REP"))					
						stbDATA.append(padSTRING('L',L_strQPRVL,6));					
	   			}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSTDT");
		}	
	}		
	private void getCHKREC(String LP_TBLNM)
	{
		try
		{
			int L_ROWCNT =0;
			if(LP_TBLNM.equals("tblTSTDL"))			
				L_ROWCNT = tblTSTDL.getRowCount(); 			
			else if(LP_TBLNM.equals("tblTDSDL"))			
				L_ROWCNT = tblTDSDL.getRowCount(); 			
			for(int i = 0;i<(L_ROWCNT-1);i++)
			{
				if(LP_TBLNM.equals("tblTSTDL"))
				{
					tblTSTDL.setValueAt(new Boolean(false),i,TB2_CHKFL);
					tblTSTDL.setValueAt(new Boolean(true),tblTSTDL.getSelectedRow(),TB2_CHKFL);	
				}
				else if(LP_TBLNM.equals("tblTDSDL"))
				{
					tblTDSDL.setValueAt(new Boolean(false),i,TB3_CHKFL);
					tblTDSDL.setValueAt(new Boolean(true),tblTDSDL.getSelectedRow(),TB3_CHKFL);				
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCHKREC");
		}
	}
	private boolean exePTPVL(String P_strPRDTP)
	{
		try
		{
			M_strSQLQRY = "Select * from co_cdtrn where CMT_CGMTP = 'MST' AND CMT_CGSTP ='COXXSST' AND cmt_codcd like 'PR_0%' AND cmt_ccsvl = '"+P_strPRDTP+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					tblPRDDL.setValueAt(M_rstRSSET.getString("CMT_CODDS"),tblPRDDL.getSelectedRow(),TB1_PRDCT);
					M_rstRSSET.close();
					return true;
				}
				M_rstRSSET.close();
				return false;				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePTPVL");
		}
		return true;
	}
	
	private boolean exePRDVL(String P_strPRDTP,String LP_PRDDS)
	{
		try
		{
			M_strSQLQRY = "Select * from CO_PRMST where PR_PRDTP = '"+P_strPRDTP.trim() +"'"+ "AND PR_PRDDS ='"+LP_PRDDS.trim().toUpperCase()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					tblPRDDL.setValueAt(M_rstRSSET.getString("PR_PRDCD"),tblPRDDL.getSelectedRow(),TB1_PRDCD);
					M_rstRSSET.close();
					return true;
				}
				else
				{
					M_rstRSSET.close();
					return false;
				}			
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRDVL");
		}
		return true;
	}
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try  
			{
				if(getSource()==tblPRDDL)
				{
					if(P_intCOLID == TB1_PRDTP)
					{
						String L_strPRDTP=((JTextField)tblPRDDL.cmpEDITR[TB1_PRDTP]).getText();
						M_strSQLQRY = "Select CMT_CCSVL,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'MST' AND CMT_CGSTP ='COXXSST' AND CMT_CCSVL ='"+L_strPRDTP+ "' AND CMT_CODCD like 'PR_0%' ";				
						//System.out.println(M_strSQLQRY);
						
						M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET.next())
						{
							tblPRDDL.setValueAt(M_rstRSSET.getString("CMT_CODDS"),P_intROWID,TB1_PRDCT);
							M_rstRSSET.close();
							return true;
						}	
						else
						{
							setMSG("Invalid Product Type",'E'); 
							return false;
						}

					}
					if(P_intCOLID == TB1_DSPFL)
					{
						setMSG("",'N');
						int L_intROWNO = tblPRDDL.getSelectedRow();						
						for (int i =0; i<tblPRDDL.getRowCount();i++)
							tblPRDDL.setValueAt(new Boolean(false),i,0);
						tblPRDDL.setValueAt(new Boolean(true),L_intROWNO,0);
						String strPRDTP = tblPRDDL.getValueAt(L_intROWNO,TB1_PRDTP).toString();
						String strPRDCD = tblPRDDL.getValueAt(L_intROWNO,TB1_PRDCD).toString();
						
						if((strPRDTP.trim().length() != 0) && (strPRDCD.trim().length() != 0))
						{
							getLOTDTL();					
							return true;
						}
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"table verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}	
}	