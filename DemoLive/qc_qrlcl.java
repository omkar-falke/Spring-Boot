/*
System Name   : Management Information System
Program Name  : Classification Query.
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : August 2005
Version       : LIMS v2.0.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.awt.event.KeyEvent;import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;import java.awt.event.FocusEvent;
import java.awt.Color;import java.awt.Component;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import javax.swing.JComboBox;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JPanel;
import javax.swing.JTable;import javax.swing.JRadioButton; import javax.swing.JTextField;
import javax.swing.JTextArea;import javax.swing.JButton;
import javax.swing.ButtonGroup;import java.util.Hashtable;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.table.*;import java.util.StringTokenizer;
import javax.swing.JComponent;import javax.swing.InputVerifier;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Classification Query.

Purpose : Query Screen to view & generate the various Product Test Details.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
CO_PRMST 
QC_PSMST       PS_QCATP,PS_TSTTP,PS_LOTNO,PS_RCLNO,PS_TSTNO,PSTSTDT    #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtLOTNO   LT_LOTNO        PR_LTMST      VARCHAR(8)    Lot Number
txtRCLNO   LT_RCLNO        PR_LTMST      VARCHAR(2)    Reclassification Number.
--------------------------------------------------------------------------------------
<B>
Logic</B> Lot Details & Quality parameter values are taken from PR_LTMST and CO_QPMST 
       for condiations
     1) LT_PRDTP = selected Product Type.
     2) AND LT_LOTNO = given Lot Number.
     3) AND LT_RCLNO = given reclassification Number.
     4) AND QP_QCATP = Selected QCA Type.
     5) AND QP_TSTTP = given Test Type.
     6) AND QP_PRDCD = given product Code.
     7) AND Qp_srlno ='00000' 
     8) AND QP_ENDDT IS NULL
     9) AND QP_ORDBY is not null
<B>Validations :</B>
    - Lot Number Must Be valid, i.e. must Be Present in the Database.
    - Reclassification Number must be valid.i.e. must Be Present in the Database.
</I> */
public class qc_qrlcl extends cl_rbase
{									/**	JTextField to display & enter Lot number.*/
	private JTextField txtLOTNO;	/**	JTextField to display & enter Reclassification Number.*/
	private JTextField txtRCLNO;	/**	JTextField to display Produced Quantity.*/
	private JTextField txtPRDQT;	/**	JTextField to display Bagged Quantity.*/
	private JTextField txtBAGQT;	/**	JTextField to display Remark Description.*/
	private JTextField txtREMDS;	/**	JTextField to display the initial of the Person Whow performed the Tests*/		
	private JTextField txtTTSBY;	/**	JTextField to display the initial of the Person Whow Performed Provisional Classification.*/	
	private JTextField txtPTSBY;	/**	JTextField to display the initial of the Person Whow performed the Final Classification.*/		
	private JTextField txtFTSBY;	/**	JTextField to display the Testing Date.*/										
	private JTextField txtTTSDT;	/**	JTextField to display the Provisional Classification Date.*/
	private JTextField txtPTSDT;	/**	JTextField to display the final Classification Date.*/
	
	private JTextField txtFTSDT;	/**	JTextField to display Product Code assigned after Primary Testing.*/
	private JTextField txtTPRCD;	/**	JTextField to display Product Code Provisionally assigned to the Product.*/
	private JTextField txtPPRCD;	/**	JTextField to display Final Product Code Assigned to the product.*/
	private JTextField txtFPRCD;	/**	JTextField to display Product Description.*/
	private JTextField txtTPRDS;	/**	JTextField to display Description of the provisionally classified Product Code.*/ 
	private JTextField txtPPRDS;	/**	JTextField to display Description of the finally classified Product.*/
	private JTextField txtFPRDS;
									/** JTextArea to Display Grab Test Details.*/		
	private JTextArea txaGRBTS;		/** JTextArea to Display Additional Test Details.*/
	private JTextArea txaADDTS;		/** JTable to Display Compsite & Bag Test Details.*/
	private cl_JTable tblTSTDL;		/** JLabel to Display some Messages on the Screen.*/
	private JLabel lbl1,lbl2,lbl3;
									/** ButtonGroup to group RedioButtons specifying the various test together */
	private ButtonGroup bgrTSTTP;	/** ButtonGroup to group RedioButtons used to specify inclusion & exclusion of Specifications.*/
	private ButtonGroup bgrSPCTP;	/** JRedioButton to specify Composite Test Details.*/
    private JRadioButton rdbCMPTS;	/** JRedioButton to specify All Test Details.*/
	private JRadioButton rdbALLTS;	/** JRedioButton to specify insersion of Test specifications.*/
	private JRadioButton rdbINCSP;	/** JRedioButton to specify exersion of Test Specifications.*/
	private JRadioButton rdbEXCSP;
									/** HashTable to hold Bag Details.*/
	private Hashtable<String,String> hstBAGDL;		/** HashTable to hold Composite Details.*/
	private Hashtable<String,String> hstCMPDL;		/** HashTable to hold Test Details.*/
	private Hashtable<String,String> hstTSTDL;     /** HashTable to hold Quality Para Desc*/ 
	private Hashtable<String,String> hstQPRDS;		/** HashTable to hold UOMCD*/
	private Hashtable<String,String> hstUOMCD;
	
									/**Final integer variable to represent Check Flag Column of the Table.*/
	private final int TB_CHKFL = 0;	/**Final integer variable to represent Product Code Column of the Table.*/
	private final int TB_QPRCD = 1;	/**Final integer variable to represent Unit of Measurement Column of the Table.*/
	private final int TB_UOMCD = 2;	/**Final integer variable to represent Specification Column of the Table.*/
	private final int TB_SPEC  = 3;	/**Final integer variable to represent Parameter Descrition Column of the Table.*/
	private final int TB_QPRDS = 4;	/**Final integer variable to represent Composite Test values Column of the Table.*/
	private final int TB_CMPVL = 5;	/**Final integer variable to represent Bag Test values Column of the Table.*/
	private final int TB_BAGVL = 6;
									/**	StringBuffer object to hold the Dyamically generated Header(1st Line).*/
	private StringBuffer stbHEADR;	/**	StringBuffer object to hold the Dyamically generated Header(2nd Line).*/
	private StringBuffer stbHEAD1;	/**	StringBuffer object to hold the Dyamically generated Dotted line.*/
	private StringBuffer stbDOTLN;
									/** Float variable Quality parameter value.*/
	private float L_fltNPFVL;		/** Float variable Quality parameter value.*/
	private float L_fltNPTVL;		/** Float variable Quality parameter value.*/
	private float L_fltQPRVL;				
	private boolean flgRETFL = false;
	private int intROWWD;
								/** Integer variable for Quality parameter Count.*/
	private int intQPRCNT =0;	/** Integer variable for Record Count.*/
	private int intRECCT =0;	/** Integer variable for Grab Test parameter Count.*/
	private int intGRBCT=0;
	private int intIDXVL =0;		
								/** String variable for Lot Number.*/
	private String strLOTNO;	/** String variable for generated File Name.*/	
	private String strFILNM;	/** String variable for Product Description.*/		
	private String strPRDDS ="";/** String variable for Classification Flag.*/
	private String strCLSFL;	
								/** String variable for Reclassification Number.*/   	
	private String strRCLNO;	/** String variable for QCA Type.*/ 
	private String strQCATP;	/** String variable for Product Type.*/	
	private String strPRDTP;	/** String variable for Test Number.*/
	private String strTSTNO="";	/** String variable for Finally Classified Product Code..*/
	private String strCHKCD;	/** String variable for Product Description.*/
	private String strCODDS;	/** String variable for Unit of Measurement.*/
	private String strUOMDS;	
								/** String array for Composite Test Paramaters.*/
	private String arrCMPFL[];	/** String array for Grab Test Paramaters Details.*/
	private String arrGRBDT[][];/** String array for Addiational Test Paramaters Details.*/
	private String arrADDDT[][];/** String array for Quality Parameter Test Details.*/
	private String arrQPRDT[][];				
								/** Integer variable for Total Row Count for Test Details Table.*/
	private int intTOROW = 10;	/** Charector variable for Test option.*/
	private char chrTSTOPT;		
												/** Final String to represent initial Reclassification Number.*/
	private final String strINTRCL_fn ="00";	/** Final String to represent Composite Test Code.*/	
	private final String strCMPTP_fn ="0103";	/** Final String to represent Test Code.*/
	private final String strDSPTP_fn = "0103\',\'0104";/** Final String to represent Test Type.*/
	private final String strCLSTP_fn ="0199";	/** Final String to represent Grab Test Code.*/
	private final String strGRBTP_fn = "0101";	/** Final String to represent Final classification Flag.*/
	private final String strFNLCLS_fn = "9";	/** Final String to represent Provisional Classification Flag.*/
    private final String strPRVCLS_fn = "4";
												/** Final Charector to represent All Test Details.*/
	private final char chrTSTALL_fn = 'A';		/** Final Charector to represent Composite Test Details.*/
    private final char chrTSTCMP_fn = 'C';		/** Final Charector to specify inclusion of specification Details in the Report.*/
    private final char chrSPCINC_fn = 'I';		/** Final Charector to specify exclusion of specification Details in the Report.*/
    private final char chrSPCEXC_fn = 'E';		
    
    private char strSPCOPT ;			
	private TableColumn tbcCMP;
	private TableColumn tbcBAG;
	private TableColumn tbcFLG;				/** FileOutPutStream object to generated Report File from Data Stream.*/
	private FileOutputStream fosREPORT;		/** DataOutPutStream object to generate Data Stream to generate Report File.*/
    private DataOutputStream dosREPORT;		/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();
	private JPanel pnlTEMP;
	private JButton btnBACK;
	qc_qrlcl()
	{
		super(2);
		crtSCR();
	}
	qc_qrlcl(int P_intSBSCT)
	{
		super(P_intSBSCT);
		crtSCR();
	}
    void crtSCR()
    {
        try
		{
		    //M_FLAG = false;
			pnlTEMP = new JPanel();
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Lot / RCL No"),2,2,1,1,this,'L');
			add(txtLOTNO = new TxtNumLimit(8),2,3,1,.7,this,'L');									
			add(txtRCLNO = new TxtNumLimit(2),2,3,1,.3,this,'R');
			add(new JLabel("Produced Qty"),2,4,1,.9,this,'R');
			add(txtPRDQT = new TxtNumLimit(8.3),2,5,1,1,this,'L');
			add(new JLabel("Bagged Qty"),2,6,1,.8,this,'R');
			add(txtBAGQT = new TxtNumLimit(8.3),2,7,1,1,this,'L');
			
			add(new JLabel("Remark"),3,2,1,1,this,'L');
			add(txtREMDS = new JTextField(),3,3,1,5,this,'L');
			
			add(new JLabel("Grade Code"),4,3,1,1,this,'R');
			add(new JLabel("Description"),4,4,1,1,this,'R');			
			add(new JLabel("Date & Time"),4,6,1,1,this,'R');
			add(new JLabel("Tested By"),4,7,1,.8,this,'R');			
						
			add(lbl1 = new JLabel("Testing"),5,2,1,1,this,'L');
			add(txtTPRCD = new TxtNumLimit(8),5,3,1,1,this,'L');
			add(txtTPRDS = new JTextField(),5,4,1,2,this,'L');			
			add(txtTTSDT = new JTextField(),5,6,1,1.2,this,'L');			
			add(txtTTSBY = new TxtLimit(3),5,7,1,0.8,this,'R');
									
			add(lbl2 = new JLabel("Prov. Class."),6,2,1,1,this,'L');			
			add(txtPPRCD = new TxtNumLimit(8),6,3,1,1,this,'L');
			add(txtPPRDS = new JTextField(),6,4,1,2,this,'L');			
			add(txtPTSDT = new JTextField(),6,6,1,1.2,this,'L');			
			add(txtPTSBY = new TxtLimit(3),6,7,1,.8,this,'R');
			
			add(lbl3 = new JLabel("Final Class."),7,2,1,1,this,'L');			
			add(txtFPRCD = new TxtNumLimit(8),7,3,1,1,this,'L');
			add(txtFPRDS = new JTextField(),7,4,1,2,this,'L');			
			add(txtFTSDT = new JTextField(),7,6,1,1.2,this,'L');			
			add(txtFTSBY = new TxtLimit(3),7,7,1,.8,this,'R');
			
			lbl1.setForeground(Color.blue);
			lbl2.setForeground(Color.blue);
			lbl3.setForeground(Color.blue);
			
			String[] L_arrCOLHD = {"Select","Para Code","UOM","Specification","Description","Comp Test","Bag Test"};
			int[] L_arrCOLSZ = {20,60,50,150,150,72,72};
			tblTSTDL = crtTBLPNL1(this,L_arrCOLHD,intTOROW,8,2,6.4,6.2,L_arrCOLSZ,new int[]{0});
			
			add(txaGRBTS = new JTextArea(),15,2,4,2,this,'L');			
			add(txaADDTS = new JTextArea(),15,4,4,2,this,'L');
			
			rdbCMPTS = new JRadioButton("Comp Test",true);
			rdbALLTS = new JRadioButton("All Tests");
			bgrTSTTP = new ButtonGroup();
			bgrTSTTP.add(rdbCMPTS);
			bgrTSTTP.add(rdbALLTS);
			add(rdbCMPTS,16,6,1,1,this,'L');
			add(rdbALLTS,17,6,1,1,this,'L');
			chrTSTOPT = chrTSTCMP_fn;
			
			rdbINCSP = new JRadioButton("Include Spec.",true);
			rdbEXCSP = new JRadioButton("Exclude Spec.");
			bgrSPCTP = new ButtonGroup();
			bgrSPCTP.add(rdbINCSP);
			bgrSPCTP.add(rdbEXCSP);
			add(rdbINCSP,16,7,1,1.2,this,'L');
			add(rdbEXCSP,17,7,1,1.2,this,'L');
			strSPCOPT = chrSPCINC_fn;
									
			txtLOTNO.setInputVerifier(objINPVR);
			txtRCLNO.setInputVerifier(objINPVR);
			
			arrGRBDT = new String[50][3];
			arrADDDT = new String[20][3];
			arrQPRDT = new String[50][3];			
			arrCMPFL = new String[intTOROW];
			for(int i=0; i<50; i++)
			{
				for(int j=0; j<3; j++)
				{
					arrGRBDT[i][j] = "";
				}
			 }
			for(int i=0; i<20; i++)
			{
				for(int j=0; j<3; j++)
				{
					arrADDDT[i][j] = "";
				}
			}			
			tbcCMP = tblTSTDL.getColumn(tblTSTDL.getColumnName(TB_CMPVL));
			tbcBAG = tblTSTDL.getColumn(tblTSTDL.getColumnName(TB_BAGVL));
			tbcFLG = tblTSTDL.getColumn(tblTSTDL.getColumnName(TB_CHKFL));
			
			stbHEADR = new StringBuffer();
			stbHEAD1 = new StringBuffer();
			stbDOTLN = new StringBuffer();
						
			M_pnlRPFMT.setVisible(true);
			setENBL(false);	
			hstQPRDS = new Hashtable<String,String>();
			hstUOMCD = new Hashtable<String,String>(); 		
			M_strSQLQRY = "select QS_QPRCD,QS_QPRDS,QS_UOMCD from CO_QSMST ";
            M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{
				while (M_rstRSSET.next())
				{
		 		    hstQPRDS.put(M_rstRSSET.getString("QS_QPRCD"),M_rstRSSET.getString("QS_QPRDS"));
		 			hstUOMCD.put(M_rstRSSET.getString("QS_QPRCD"),nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}       
        add(btnBACK = new JButton("Back"),17,1,1,1,this,'L');
    }
    
	/**
	 * Method to enable & disable the Components according to requriements
	 * @param P_flgSTAT boolean argument to pass the state of the variable.
	 */
	void setENBL(boolean P_flgSTAT)
	{		
		super.setENBL(P_flgSTAT);						
		txtBAGQT.setEnabled(false);
		txtPRDQT.setEnabled(false);
        txtTTSBY.setEnabled(false);
        txtTTSDT.setEnabled(false);
        txtTPRCD.setEnabled(false);
        txtTPRDS.setEnabled(false);
        txtPTSBY.setEnabled(false);
        txtPTSDT.setEnabled(false);
        txtPPRCD.setEnabled(false);
        txtPPRDS.setEnabled(false);
        txtFTSBY.setEnabled(false);
        txtFTSDT.setEnabled(false);
        txtFPRCD.setEnabled(false);
        txtFPRDS.setEnabled(false);
        txtFTSDT.setEnabled(false);
        txtTTSDT.setEnabled(false);
        txtFPRCD.setEnabled(false);
        txtFPRDS.setEnabled(false);
        txtREMDS.setEnabled(false);
		tblTSTDL.setEnabled(false);
		txaGRBTS.setEnabled(false);
		txaADDTS.setEnabled(false);
	}
		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{							
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{
					strPRDTP = "01";//cl_dat.M_strSBSCD.subString(0,2);
					strQCATP = M_strSBSCD.substring(2,4);
					setENBL(true);
					setMSG("Please Enter Lot No OR Press F1 to select Lot No. from List..",'N');								
					txtLOTNO.requestFocus();
				}
				else
				{				
					setMSG("Please Select an Option..",'N');
					setENBL(false);	
				}
			}
			if(M_objSOURC == rdbCMPTS)		
				chrTSTOPT = chrTSTCMP_fn;		
			else if(M_objSOURC == rdbALLTS)		
				chrTSTOPT = chrTSTALL_fn;		
			else if(M_objSOURC == rdbINCSP)		
				strSPCOPT = chrSPCINC_fn;		
			else if(M_objSOURC == rdbEXCSP)		
				strSPCOPT = chrSPCEXC_fn;			
			if(M_objSOURC == txtLOTNO)
			{	
				strLOTNO = txtLOTNO.getText().trim();
				clrCOMP();
				txtLOTNO.setText(strLOTNO);
				tblTSTDL.clrTABLE();
				txaGRBTS.setText("");
				txaADDTS.setText("");
				if(txtLOTNO.getText().trim().length() == 0)			
					setMSG("Lot number can not be blank",'E');			
				else
				{
					strLOTNO = txtLOTNO.getText().trim();
					txtRCLNO.setText(strINTRCL_fn);
					strRCLNO = strINTRCL_fn;				
					txtRCLNO.requestFocus();
				}
			}
			else if(M_objSOURC == txtRCLNO)
			{				
				strLOTNO = txtLOTNO.getText().trim();
				strRCLNO = txtRCLNO.getText().trim();				
				exeLTQRY(strLOTNO,strRCLNO,strQCATP,strPRDTP);				
			}
			else if(M_objSOURC == btnBACK)
			{
			   /*qc_qrdpa obj = new qc_qrdpa();
			   cl_dat.M_pnlFRBTM_pbst.remove(this);
			   cl_dat.M_pnlFRBTM_pbst.add("screen", obj);
			   cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen");*/
			    cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen");
			    cl_dat.M_btnUNDO_pbst.setEnabled(true);
			}
		}
				catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
	
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		/*if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		{            
			if(M_objSOURC == tblTSTDL)
			{
			   exeRNGCHK(tblTSTDL.getSelectedRow());
			}
		}
		else */
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{                
                if(M_objSOURC == txtLOTNO)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					txaGRBTS.setText("");
					txaADDTS.setText("");
				    cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtLOTNO";
					String[] LM_LOTHDR = {"Lot Number","Rcl. No.","Lot Start Date  ","  Grade ","Classfn. Flag"};
            	    M_strSQLQRY = "select LT_LOTNO,LT_RCLNO,LT_PSTDT,PR_PRDDS,LT_CLSFL from PR_LTMST,CO_PRMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '";
                    M_strSQLQRY += strPRDTP + "' AND ltrim(str(LT_TPRCD,20,0)) = PR_PRDCD AND  " ;
			        M_strSQLQRY += " LT_LOTNO in (select PS_LOTNO from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '";
                   	M_strSQLQRY += strQCATP + "' AND PS_TSTTP = '" + strCMPTP_fn.trim()+"') ";
					if(txtLOTNO.getText().trim().length() >0)
						M_strSQLQRY = M_strSQLQRY + " AND LT_LOTNO like '" + txtLOTNO.getText().trim() + "%'  AND LT_STSFL <> 'X' order by LT_CLSFL desc,LT_LOTNO desc";
					else	
						M_strSQLQRY = M_strSQLQRY + " order by LT_CLSFL desc,LT_LOTNO desc";										
					cl_hlp(M_strSQLQRY,1,1,LM_LOTHDR,5,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
        		else if(M_objSOURC == txtRCLNO)
				{
					M_strHLPFLD = "txtRCLNO";
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgHELPFL_pbst = true;					
					M_strSQLQRY = "select LT_RCLNO,PR_PRDDS from PR_LTMST,CO_PRMST where LT_PRDCD = PR_PRDCD AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = ";
					M_strSQLQRY +="'"+txtLOTNO.getText().trim()+"'";										
					String[] L_arrHDR = {"RCL No ","Grade"};
					cl_hlp(M_strSQLQRY,1,1,L_arrHDR,2,"CT");
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
	    }		
	}	
	/**
	 * Super class method overrided to execute the F1 Help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();										
		if (M_strHLPFLD.equals("txtLOTNO"))
        {
			clrCOMP();
			tblTSTDL.clrTABLE();
			txaGRBTS.setText("");
			txaADDTS.setText("");
			txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);            
			txtRCLNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtRCLNO.requestFocus();						
		}
		else if (M_strHLPFLD.equals("txtRCLNO"))
        {
			txtRCLNO.setText(cl_dat.M_strHLPSTR_pbst);            
		}
        cl_dat.M_flgHELPFL_pbst = false;
    }		
	/**
	 * Method to get Details & Quality Parameter values for given Lot Number.
	 * @param P_strLOTNO String argument to pass Lot number
	 * @param P_strRCLNO String argument to pass Reclassification Number.
	 * @param P_strQCATP String argument to pass QCA Type.
	 * @param P_strPRDTP String argument to pass Product Type.
	 */
	public void exeLTQRY(String P_strLOTNO,String P_strRCLNO,String P_strQCATP,String P_strPRDTP)
	{
		String L_strPPRCD,L_strCPRCD,L_strTPRCD,L_strTPRDS,L_strBAGQT,L_strPRDQT,L_strPRSTS,L_strPCLBY,L_strPCLTM="";
		String L_strCLSBY,L_strCLSTM="",L_strCHKCD ="";
		ResultSet L_rstRSSET,L_rstRSSET1;
		String L_strSQLQRY="";	
		boolean L_TSTFL = false;
		//strLOTNO = P_strLOTNO.trim();
		//strRCLNO = P_strRCLNO.trim();
		//strQCATP = P_strQCATP.trim();
		//strPRDTP = P_strPRDTP.trim();			
		txaGRBTS.setText("");
		txaADDTS.setText("");
		txtLOTNO.setText(P_strLOTNO);
		txtRCLNO.setText(P_strRCLNO);		
		this.setMSG("",'N');	
		this.setCursor(cl_dat.M_curWTSTS_pbst);	
		
		//LM_LOTFL = getLOTDET();
		try
		{
			L_strSQLQRY = "SELECT LT_LOTNO,LT_RCLNO,LT_TPRCD,LT_CLSFL,LT_BAGQT,LT_PRDQT,LT_PCLBY,LT_PPRCD,LT_PCLTM,LT_CPRCD,LT_CLSTM,LT_CLSBY,PR_PRDDS,PR_STSFL FROM PR_LTMST,CO_PRMST WHERE ltrim(str(LT_TPRCD,20,0)) = PR_PRDCD AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = "+ "'"+P_strPRDTP + "'";
			L_strSQLQRY += " AND LT_LOTNO = " + "'" + P_strLOTNO + "'";
			L_strSQLQRY += " AND LT_RCLNO = " + "'" + P_strRCLNO + "'";		
			L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					strCLSFL = nvlSTRVL(L_rstRSSET.getString("LT_CLSFL"),"");
					L_strPPRCD = nvlSTRVL(L_rstRSSET.getString("LT_PPRCD"),"");
					L_strCPRCD = nvlSTRVL(L_rstRSSET.getString("LT_CPRCD"),"");	
					L_strTPRCD = nvlSTRVL(L_rstRSSET.getString("LT_TPRCD"),"");
					L_strTPRDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
					L_strBAGQT = nvlSTRVL(L_rstRSSET.getString("LT_BAGQT"),"");
					L_strPRDQT = nvlSTRVL(L_rstRSSET.getString("LT_PRDQT"),"");
					L_strPRSTS = nvlSTRVL(L_rstRSSET.getString("PR_STSFL"),"");
					L_strPCLBY = nvlSTRVL(L_rstRSSET.getString("LT_PCLBY"),"");									
					java.sql.Timestamp L_tmsTEMP = L_rstRSSET.getTimestamp("LT_PCLTM");
					if(L_tmsTEMP != null)
						L_strPCLTM = M_fmtLCDTM.format(L_tmsTEMP);
					
					L_strCLSBY = nvlSTRVL(L_rstRSSET.getString("LT_CLSBY"),"");				
					java.sql.Timestamp L_tmsTEMP1 = L_rstRSSET.getTimestamp("LT_CLSTM");
					if(L_tmsTEMP1 != null)
						L_strCLSTM = M_fmtLCDTM.format(L_tmsTEMP1);
													  	
					txtTPRCD.setText(L_strTPRCD);
				    txtTPRDS.setText(L_strTPRDS);
				    txtBAGQT.setText(L_strBAGQT);
					txtPRDQT.setText(L_strPRDQT);
					txtPPRCD.setText(L_strPPRCD);
					txtFPRCD.setText(L_strCPRCD);
					
					if(!txtPPRCD.getText().trim().equals(""))
					{
						M_strSQLQRY= "select PR_PRDDS from CO_PRMST where PR_PRDCD = '"+ txtPPRCD.getText().trim() +"'";						
						L_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET1 != null)
						{
							if(L_rstRSSET1.next())							
								txtPPRDS.setText(L_rstRSSET1.getString("PR_PRDDS"));							
							L_rstRSSET1.close();
						}
					    txtPTSBY.setText(L_strPCLBY);
						txtPTSDT.setText(L_strPCLTM);					
					}
					if(!txtFPRCD.getText().trim().equals(""))
					{			
						M_strSQLQRY= "select PR_PRDDS from CO_PRMST where PR_PRDCD = '"+ txtPPRCD.getText().trim() +"'";						
						L_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(L_rstRSSET1 != null)
						{
							if(L_rstRSSET1.next())							
								txtFPRDS.setText(L_rstRSSET1.getString("PR_PRDDS"));							
							L_rstRSSET1.close();
						}
						txtFTSBY.setText(L_strCLSBY);				
						txtFTSDT.setText(L_strCLSTM);			       
					}
				  	strCHKCD ="";
				/*	if (!txtFPRCD.getText().trim().equals(""))
					    strCHKCD = txtFPRCD.getText().trim();
					else if (!txtPPRCD.getText().trim().equals(""))
					    strCHKCD = txtPPRCD.getText().trim();
					else
					    strCHKCD = txtTPRCD.getText().trim();*/
                    if (!L_strCPRCD.equals(""))
					    strCHKCD = L_strCPRCD;
					else if (!L_strPPRCD.equals(""))
					    strCHKCD = L_strPPRCD;
					else
					    strCHKCD = L_strTPRCD;
				   											
					L_rstRSSET.close();
					System.out.println("1");					
					L_TSTFL = getTSTDET(P_strLOTNO,P_strRCLNO,P_strQCATP);						
					System.out.println("2");
					hstTSTDL = new Hashtable<String,String>();
					int L_CNT =0;
					tblTSTDL.clrTABLE();       
					
					/*if (!txtFPRCD.getText().trim().equals(""))
						L_strCHKCD = txtFPRCD.getText().trim();
					else if (!txtPPRCD.getText().trim().equals(""))
				    L_strCHKCD = txtPPRCD.getText().trim();
					else
				    L_strCHKCD = txtTPRCD.getText().trim();		   */
				    if (!L_strCPRCD.equals(""))
					    L_strCHKCD = L_strCPRCD;
					else if (!L_strPPRCD.equals(""))
					    L_strCHKCD = L_strPPRCD;
					else
					    L_strCHKCD = L_strTPRCD;
					    
					M_strSQLQRY = "select LT_LOTNO,LT_PRDCD,QP_QPRCD,QP_QPRDS,QP_UOMDS,QP_ORDBY,QP_NPFVL,QP_NPTVL,QP_CMPFL";
					M_strSQLQRY +=" from PR_LTMST,CO_QPMST where LT_PRDTP = '";
					M_strSQLQRY += P_strPRDTP+"'";
					M_strSQLQRY += " AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = '" + P_strLOTNO + "'";
					M_strSQLQRY += " AND LT_RCLNO = '" + P_strRCLNO.trim() + "'";
					M_strSQLQRY += "AND QP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QP_QCATP = '" + P_strQCATP.trim() + "' AND QP_TSTTP = '";
					M_strSQLQRY += strDSPTP_fn.trim().substring(0,4)+ "'"; 
					M_strSQLQRY += " AND QP_PRDCD ='"+ L_strCHKCD.trim()+"'";	
					M_strSQLQRY += " AND Qp_srlno ='00000' AND QP_ENDDT IS NULL ";
					M_strSQLQRY += " AND QP_ORDBY is not null order by QP_ORDBY";	   	   
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
	                System.out.println("3 "+M_strSQLQRY);
					L_CNT =0;
					intQPRCNT = 0;					
					if(L_rstRSSET !=null)
					{
						while(L_rstRSSET.next())
						{				
							getQPRVL(L_rstRSSET,L_CNT);
	  						L_CNT+=1;
						}
						L_rstRSSET.close();			
					}
					 System.out.println("4");
					getRMK(P_strQCATP,P_strLOTNO,strTSTNO);	   									
					getGRADDT(P_strQCATP,P_strLOTNO);						
					 System.out.println("5");
					getGRBARR(0);
					 System.out.println("6");
					tbcCMP.setCellRenderer(new RowRenderer());
					this.setCursor(cl_dat.M_curDFSTS_pbst);		
    			}							
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeLTQRY");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			txtLOTNO.requestFocus();
		}				
	}
	/**
	 * Method to get Test Details.
	 */
	private boolean getTSTDET(String P_strLOTNO,String P_strRCLNO,String P_strQCATP)
	{
		flgRETFL = false;
		boolean L_flgTSTTP = false;
		String L_strCMPVL="",L_strBAGVL="";
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "Select * from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '" + P_strQCATP.trim() + "' AND PS_LOTNO = '" ;
			M_strSQLQRY += P_strLOTNO + "'";
			M_strSQLQRY += " AND PS_RCLNO = '"+P_strRCLNO.trim() + "'";
			M_strSQLQRY += " AND PS_TSTTP in ('" + strDSPTP_fn.trim() + "')";
			M_strSQLQRY += " AND PS_STSFL <> 'X'";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			String L_STRDTM="";
			hstCMPDL = new Hashtable<String,String>();
			hstBAGDL = new Hashtable<String,String>();
			hstCMPDL.clear();
			hstBAGDL.clear();		
			if(L_rstRSSET !=null)
			{			
				while (L_rstRSSET.next())
				{				
				   	L_flgTSTTP = L_rstRSSET.getString("PS_TSTTP").trim().equals(strCMPTP_fn);
				    if (L_flgTSTTP)
				    {
						flgRETFL = true;						
						java.sql.Timestamp L_tmsTEMP = L_rstRSSET.getTimestamp("PS_TSTDT");
						if(L_tmsTEMP != null)
						L_STRDTM = M_fmtLCDTM.format(L_tmsTEMP);
						
						txtTTSDT.setText(L_STRDTM);
				        strTSTNO = L_rstRSSET.getString("PS_TSTNO");				        
				        txtTTSBY.setText(L_rstRSSET.getString("PS_TSTBY"));
						  
						L_strCMPVL = L_rstRSSET.getString("PS_A__VL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("A__",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_B__VL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("B__",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_DSPVL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("DSP",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_EL_VL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("EL_",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_IZOVL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("IZO",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_MFIVL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("MFI",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_RSMVL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("RSM",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_TS_VL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("TS_",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_VICVL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("VIC",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_WI_VL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("WI_",L_strCMPVL);
						L_strCMPVL = L_rstRSSET.getString("PS_Y1_VL");
						if(L_strCMPVL !=null)
							hstCMPDL.put("Y1_",L_strCMPVL);
					}
					else
					{
						L_strBAGVL = L_rstRSSET.getString("PS_A__VL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("A__",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_B__VL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("B__",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_DSPVL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("DSP",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_EL_VL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("EL_",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_IZOVL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("IZO",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_MFIVL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("MFI",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_RSMVL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("RSM",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_TS_VL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("TS_",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_VICVL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("VIC",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_WI_VL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("WI_",L_strBAGVL);
						L_strBAGVL = L_rstRSSET.getString("PS_Y1_VL");
						if(L_strBAGVL !=null)
							hstBAGDL.put("Y1_",L_strBAGVL);
					}				
	   			}
				L_rstRSSET.close();
			}		
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getTSTDET");
		}
		return flgRETFL;
	}
	/**
	 * Method to get Remark Description
	 * @param P_strLOTNO String argument to pass Lot Number.
	 * @param P_strTSTNO String argumenet to pass Test Number.
	 */
	private void getRMK(String P_strQCATP,String P_strLOTNO,String P_strTSTNO)
	{
		ResultSet L_rstRSSET;
		String L_strSQLQRY = "";
		try
		{	
			L_strSQLQRY = "Select RM_REMDS from qc_rmmst where";
			L_strSQLQRY += " RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+P_strQCATP+"' AND RM_TSTTP  ='"+strCLSTP_fn+"' AND RM_TSTNO ='"+P_strLOTNO.trim()+"' ";
			intRECCT = 0;
			L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				while(L_rstRSSET.next())
				{
					intRECCT++;	
				}
				L_rstRSSET.close();
			}		
			if(intRECCT == 1)		
				getTSRMK(P_strQCATP,P_strLOTNO,strCLSTP_fn);		
			else
			{
				if(strCLSFL.indexOf("'4','9'")<=0) 
					if(P_strTSTNO !=null)
						getTSRMK(P_strQCATP,P_strTSTNO,"0103");	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRMK");
		}
	}
	/**
	 * Method to get Quality parameter values.
	 * @param L_rstRSSET ResultSet object to pass ReesultSet
	 * @param P_intRECNO integer variable to pass Record Count.
	 */
	private void getQPRVL(ResultSet L_rstRSSET,int P_intRECNO)
	{
		try
	    {
			arrCMPFL[P_intRECNO] = "";		
	        L_fltNPFVL=0;
			L_fltNPTVL=0;
			L_fltQPRVL=0;
			String L_strQPRCD="",L_strCMPFL;
	        String L_strNPFDS="",L_strNPTDS="";			
			if(L_rstRSSET != null)
			{
				tblTSTDL.setValueAt(new Boolean(false),P_intRECNO,TB_CHKFL);			
				L_strQPRCD = nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),"");
				if(!L_strQPRCD.equals(""))
				{
					tblTSTDL.setValueAt(nvlSTRVL(L_strQPRCD,""),P_intRECNO,TB_QPRCD);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_QPRDS"),""),P_intRECNO,TB_QPRDS);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_UOMDS"),""),P_intRECNO,TB_UOMCD);			
				}
	        	try
				{
					L_fltNPFVL = L_rstRSSET.getFloat("QP_NPFVL");
					L_strNPFDS = "" + L_fltNPFVL;
				}
				catch (Exception L_E)
				{
					L_fltNPFVL = 0;
					L_strNPFDS = " ";
				}			
				try
				{
					L_fltNPTVL = L_rstRSSET.getFloat("QP_NPTVL");
					L_strNPTDS = "" + L_fltNPTVL;				
				}
				catch (Exception L_E)
				{
					L_fltNPTVL = 0;
					L_strNPTDS = " ";
				}			
				String L_strRNGVL = " " + L_strNPFDS + " - " + L_strNPTDS;
				tblTSTDL.setValueAt(nvlSTRVL(L_strRNGVL,""),P_intRECNO,TB_SPEC);  
				String L_strCMPVL ="";
				String L_strBAGVL ="";			
				if(hstCMPDL !=null)
				{
					L_strCMPVL = (String)hstCMPDL.get(L_strQPRCD);
					if(L_strCMPVL != null)
					L_fltQPRVL = Float.valueOf(L_strCMPVL).floatValue();
					tblTSTDL.setValueAt(L_strCMPVL,P_intRECNO,TB_CMPVL);
				}			
				if(hstBAGDL !=null)
				{
					L_strBAGVL = (String)hstBAGDL.get(L_strQPRCD);
					tblTSTDL.setValueAt(L_strBAGVL,P_intRECNO,TB_BAGVL);		
				}
				L_strCMPFL = nvlSTRVL(L_rstRSSET.getString("QP_CMPFL"),"");			
				if (L_strCMPFL.equals("Y"))
				{
					arrCMPFL[P_intRECNO] = "Y";
					chkQPRRNG(L_fltNPFVL,L_fltNPTVL,L_fltQPRVL,P_intRECNO);
				}			
				intQPRCNT += 1;
			}
	    }
	    catch (Exception L_EX)
	    {
			setMSG(L_EX,"getQPRVL");		
	    }
	}	
	/**
	 * Method to get Test Remark.
	 * @param P_strTSTNO String argument to pass Test Number.
	 * @param P_strTSTTP String argument to pass Test Type.
	 */
	private void getTSRMK(String P_strQCATP,String P_strTSTNO,String P_strTSTTP)
	{		
		ResultSet L_rstRSSET;
		try
	    {		
			M_strSQLQRY = "Select RM_TSTTP, RM_REMDS from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '" + P_strQCATP.trim() + "' AND RM_TSTTP = '" ;
			M_strSQLQRY += P_strTSTTP.trim() +"' AND RM_TSTNO = '";
			M_strSQLQRY += P_strTSTNO.trim()+"'";	    
	        L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				while(L_rstRSSET.next())
				{
					txtREMDS.setText(L_rstRSSET.getString("RM_REMDS"));
				}
				L_rstRSSET.close();
			}
	    }
	    catch(Exception L_SE)
	    {
			setMSG(L_SE,"getTSRMK");
		}  
	}	
	/**
	 * Method to check the quality parameter values within the specification Range.
	 * @param P_fltNPFVL float 
	 * @param P_fltNPTVL float 
	 * @param P_fltQPRVL float 
	 * @param P_intROWNO Integer argument to pass total Number of Rows.
	 */
	private void chkQPRRNG(float P_fltNPFVL,float P_fltNPTVL,float P_fltQPRVL,int P_intROWNO)
	{
		String L_strRNGVL ="";
		try
		{
			if ((P_fltNPFVL == 0) && (P_fltNPTVL == 0))
			    tblTSTDL.setValueAt(new Boolean(false),P_intROWNO,TB_CHKFL);
			else if ((P_fltNPFVL != 0) && (P_fltNPTVL == 0))
			{
			   if (P_fltQPRVL < P_fltNPFVL)			  
				   tblTSTDL.setValueAt(new Boolean(true),P_intROWNO,TB_CHKFL);			   
			   else
			       tblTSTDL.setValueAt(new Boolean(false),P_intROWNO,TB_CHKFL);
			}
			else if ((P_fltNPFVL == 0) && (P_fltNPTVL != 0))
			{
				if (P_fltQPRVL > P_fltNPTVL)				
					tblTSTDL.setValueAt(new Boolean(true),P_intROWNO,TB_CHKFL);				
				else				
					tblTSTDL.setValueAt(new Boolean(false),P_intROWNO,TB_CHKFL);				
			}
			else
			{
			   if (( P_fltQPRVL < P_fltNPFVL) || (P_fltQPRVL > P_fltNPTVL))			   
				   tblTSTDL.setValueAt(new Boolean(true),P_intROWNO,TB_CHKFL);			   
			   else
			       tblTSTDL.setValueAt(new Boolean(false),P_intROWNO,TB_CHKFL);
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"chkQPRRNG");
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_qrlcl.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_qrlcl.doc";
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			if(tblTSTDL.getValueAt(1,1).toString().equals(""))//if(intRECCT == 0)
			{
				txtRCLNO.requestFocus();
				setMSG("Press Enter Key first & then click the button to generate Report..",'E');
				return;
			}
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Quality Details"," ");
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
		    cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 0;
			stbHEADR.delete(0,stbHEADR.toString().length());
			stbHEAD1.delete(0,stbHEAD1.toString().length());
			stbDOTLN.delete(0,stbDOTLN.toString().length());
			if(txtLOTNO.getText().trim().length() != 8 )
			{
				setMSG("Invalid Lot Number, Please Enter valid Lot No...",'E');
				return false;
			}
			if(txtRCLNO.getText().trim().length() != 2)
			{
				setMSG("Invalid RCL Number, Please Enter valid RCL No...",'E');
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
		int L_CNT =0;
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
				//prnFMTCHR(dosREPORT,M_strCPI12);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Quality Details</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}	
			prnHEADER();
			
			for(int i=0;i<intTOROW;i++)
			{
				if(tblTSTDL.getValueAt(i,TB_QPRCD).toString().trim().length()>0)				
					dosREPORT.writeBytes("\n");				
				dosREPORT.writeBytes(padSTRING('R',tblTSTDL.getValueAt(i,TB_QPRCD).toString(),10));
				dosREPORT.writeBytes(padSTRING('R',tblTSTDL.getValueAt(i,TB_QPRDS).toString(),30));
				if(strSPCOPT == chrSPCINC_fn)
				{
					dosREPORT.writeBytes(padSTRING('R',tblTSTDL.getValueAt(i,TB_UOMCD).toString(),12));
					dosREPORT.writeBytes(padSTRING('R',tblTSTDL.getValueAt(i,TB_SPEC).toString(),15));
				}			
				if(tblTSTDL.getValueAt(i,TB_CMPVL) != null)				
					dosREPORT.writeBytes(padSTRING('L',tblTSTDL.getValueAt(i,TB_CMPVL).toString(),9));				
				else
					dosREPORT.writeBytes(padSTRING('L',"-",9));
				
				if(chrTSTOPT == chrTSTALL_fn)
				{
					dosREPORT.writeBytes(padSTRING('L',tblTSTDL.getValueAt(i,TB_BAGVL).toString(),9));				    
					if(hstTSTDL.get(String.valueOf(i)) != null)					
					{
						L_CNT =0;
						getGRBARR(i);
						for (int j = 0;j<intGRBCT;j++)
						{
						    if (arrGRBDT[j][0].trim().equals(tblTSTDL.getValueAt(i,TB_QPRCD).toString().trim())) 				
							{
								if(L_CNT ==0)								
									dosREPORT.writeBytes(padSTRING('L',"",7));								
								else
								{
									dosREPORT.writeBytes("\n");
									if(strSPCOPT == chrSPCINC_fn)
									dosREPORT.writeBytes(padSTRING('L',"",92));
									else
									dosREPORT.writeBytes(padSTRING('L',"",65));
								}						
								dosREPORT.writeBytes(padSTRING('R',arrGRBDT[j][1].trim().substring(11),8) +  arrGRBDT[j][2].trim());								
								L_CNT++;
							}						
						}
					}
				}
				if(tblTSTDL.getValueAt(i,TB_QPRCD).toString().trim().length()>0)	
					dosREPORT.writeBytes("\n");								
			}
			dosREPORT.writeBytes("\n");
			if(chrTSTOPT == chrTSTALL_fn)
			{
				dosREPORT.writeBytes("Additional Test Details");				
				dosREPORT.writeBytes("\n\n");
				for (int j = 0;j<arrADDDT.length;j++)
				{										
				    if(arrADDDT[j][0].length()>0)
					{
						//getQPRDS(arrADDDT[j][0]);						
						strCODDS = hstQPRDS.get(arrADDDT[j][0]).toString();
						strUOMDS = hstUOMCD.get(arrADDDT[j][0]).toString();
						dosREPORT.writeBytes(padSTRING('R',arrADDDT[j][0].trim(),10));
						dosREPORT.writeBytes(padSTRING('R',strCODDS,30));

						if(strSPCOPT == chrSPCINC_fn)
						{
							dosREPORT.writeBytes(padSTRING('R',strUOMDS,12));
							dosREPORT.writeBytes(padSTRING('R',"-",15));
						}						
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L'," ",7));						
						dosREPORT.writeBytes(padSTRING('R',arrADDDT[j][1].trim().substring(11),8) +  arrADDDT[j][2].trim());
						dosREPORT.writeBytes("\n\n");
					}
				}
			}			
			dosREPORT.writeBytes(stbDOTLN.toString());						
		
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
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
			String L_strCLSST="";			
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
				
			intROWWD = 0;				
			stbHEADR.append(padSTRING('R',"Parameter",10)+padSTRING('R',"Description",30));
			intROWWD = 40;
			if(strSPCOPT == chrSPCINC_fn)
			{
				stbHEADR.append(padSTRING('R',"UOM",12)+padSTRING('R',"Spec. Range",15));
				intROWWD += 12+15;
			}
			stbHEADR.append(padSTRING('R',"Composite",15));
			intROWWD += 15;
				
			if(chrTSTOPT == chrTSTALL_fn)
			{
				stbHEADR.append(padSTRING('R',"Bag",10));
				stbHEADR.append("Grab    Details");//13
				intROWWD += 26;
			}
				
			if(intROWWD < 80)
				intROWWD = 80;
				
			stbHEAD1.append(padSTRING('R',"Code ",40));
			if(strSPCOPT == chrSPCINC_fn)
			{
				stbHEAD1.append(padSTRING('L',"",27));
			}
			stbHEAD1.append(padSTRING('R',"Test",15));
			if(chrTSTOPT == chrTSTALL_fn)
			{
				stbHEAD1.append(padSTRING('R',"Test",8));
				stbHEAD1.append(padSTRING('L',"Time    Value",15));
			}																								
			for(int i = 0; i< intROWWD;i++)
				stbDOTLN.append("-");						
			
			if(strCLSFL.equals(strPRVCLS_fn))
			{
				strPRDDS = txtPPRDS.getText().trim();
				L_strCLSST = "Prov. Classified";
			}
			else if(strCLSFL.equals(strFNLCLS_fn))
			{
				strPRDDS = txtFPRDS.getText().trim();
				L_strCLSST = "Classified";
			}
			else
			{
				strPRDDS = txtTPRDS.getText().trim();
				L_strCLSST = "UnClassified";
			}												
			dosREPORT.writeBytes("\n\n\n\n\n");			 
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWWD -25));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Lot Number : " +txtLOTNO.getText().trim()+ "  RCL No.: " +txtRCLNO.getText().trim(),intROWWD -25));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n" );						
			dosREPORT.writeBytes("Grade : "+ strPRDDS +"  Status : "+L_strCLSST + "\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			dosREPORT.writeBytes(stbHEADR.toString()+"\n");
			dosREPORT.writeBytes(stbHEAD1.toString()+"\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			cl_dat.M_intLINNO_pbst =13;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}	
	/**
	 * Method to get Quality Paramter Details.
	 * @param P_strQPRCD String argument to pass product Code.
	 */
	/*public void getQPRDS(String P_strQPRCD)
	{
        try
        {
			strCODDS ="";
			strUOMDS ="";
			ResultSet L_rstRSSET;
            M_strSQLQRY = "select QS_QPRDS,QS_UOMCD from CO_QSMST ";
            L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET!= null)
			{
				while (L_rstRSSET.next())
				{
		 		    hstQPRDS.put(L_rstRSSET.getString("QS_QPRDS"));
		 			hstUOMCD.put(nvlSTRVL(L_rstRSSET.getString("QS_UOMCD"),"");
				}
				L_rstRSSET.close();
			}
        }
        catch(Exception L_SE)
        {
             setMSG(L_SE,"getQPRDS");
        }        
	}*/
	/**
	 * Method to get Grab Test Details & to Store in the array.
	 * @param P_intROWNUM Integer argument to pass Row Number.
	 */
	private void getGRBARR(int P_intROWNUM)
	{
		try
		{
			String L_GRBDT = "";
			txaGRBTS.setText("");
			for (int i = 0;i<intGRBCT;i++)
			{
				if (arrGRBDT[i][0].trim().equals(tblTSTDL.getValueAt(P_intROWNUM,TB_QPRCD).toString().trim()))
					L_GRBDT = L_GRBDT + arrGRBDT[i][0].trim() + "            " + arrGRBDT[i][1].trim().substring(11) + "            " + arrGRBDT[i][2].trim() + "\n";
			}
			txaGRBTS.insert(L_GRBDT,0);
			txaGRBTS.select(0,0);
		}
		catch(Exception L_E)
		{
			setMSG("Grab test Details not available ... ",'E');
		}                 
	}
	/**
	 * Method to grab & additional Details.
	 */
	private void getGRADDT(String P_strQCATP,String P_strLOTNO)// Grab AND additional details
	{
		ResultSet L_RSLQPR;	
		ResultSet L_rstRSSET;
		ResultSetMetaData L_RSLMDT;
		Hashtable L_HSQPR = new Hashtable();
	    String L_ADDDT = "";
        String L_STRDTM="",L_COLNM;
        String L_TSTNO = "";
		String L_strQPRCD ="";
		String L_QPVAL ="";
		String L_QPRLST ="";
		int l=0;
		int i=0,p=0,x =0;
		int L_QPCNT =0;
		int L_COLCNT =0;
		boolean L_GRBVL = false;
        try
        {
			if(hstTSTDL !=null)
				hstTSTDL.clear();
			M_strSQLQRY ="Select * from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_STSFL <> 'X'" ;
			M_strSQLQRY += " AND PS_QCATP = '" + P_strQCATP.trim() + "' AND PS_TSTTP = '" + strGRBTP_fn.trim() + "'";
			M_strSQLQRY += " AND PS_LOTNO = '"+P_strLOTNO.trim() + "'" + " order by PS_TSTDT";
		    L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				L_RSLMDT = L_rstRSSET.getMetaData();
				L_COLCNT = L_RSLMDT.getColumnCount();
				while (L_rstRSSET.next())
				{
					for(i=0;i<L_COLCNT;i++)
					{
						L_COLNM = L_RSLMDT.getColumnName(i+1);
						if(L_COLNM !=null)
							L_COLNM = L_COLNM.toUpperCase().trim();
						if(L_COLNM !=null)
						if(L_COLNM.trim().substring(6).equals("VL"))
					    {
							L_strQPRCD = L_COLNM.substring(3,6); 
							L_QPVAL = L_rstRSSET.getString(L_COLNM.trim());	
							if(L_QPVAL !=null)
							{
								arrQPRDT[p][0] = L_strQPRCD;
								//L_STRDTM = cc_dattm.occ_dattm.setDTMFMT(L_rstRSSET.getString("PS_TSTDT"));
								java.sql.Timestamp L_tmsTEMP = L_rstRSSET.getTimestamp("PS_TSTDT");
								if(L_tmsTEMP != null)
									L_STRDTM = M_fmtLCDTM.format(L_tmsTEMP);
								
								arrQPRDT[p][1] = L_STRDTM.trim();
								arrQPRDT[p][2] = L_QPVAL.trim();
								p++;
							}
						}
					}
				}
			}
			for(int k=0;k<p;k++)
			{
				L_GRBVL = false;
				for(int j=0;j<intQPRCNT;j++)
				{
					if(arrQPRDT[k][0].trim().equals(tblTSTDL.getValueAt(j,TB_QPRCD)))
					{
						arrGRBDT[l][0] = arrQPRDT[k][0];
						arrGRBDT[l][1] = arrQPRDT[k][1];
						arrGRBDT[l][2] = arrQPRDT[k][2];
						l += 1;
						L_GRBVL = true;
						hstTSTDL.put(String.valueOf(j).trim(),"x");
						
					}
				}
				if(!L_GRBVL)
				{
					arrADDDT[x][0] = arrQPRDT[k][0];
					arrADDDT[x][1] = arrQPRDT[k][1];
					arrADDDT[x][2] = arrQPRDT[k][2];					
					x++;
					L_STRDTM = arrQPRDT[k][1];
					L_ADDDT = L_ADDDT + arrQPRDT[k][0].trim() +"\t"+  L_STRDTM.substring(11) +"\t"+ arrQPRDT[k][2].trim() + "\n";
				}					
			}
			txaADDTS.insert(L_ADDDT,0);
			txaADDTS.select(0,0);
         	intGRBCT = l;
	        L_rstRSSET.close();
	    }
        catch(Exception L_E)
        {
            setMSG(L_E,"getGRADDT");
			setMSG("Grab test Details not available... ",'E');
	    }
    }								
	private void exeRNGCHK(int P_intROWNUM) 
	{
		boolean L_OUTRG = false;
		String L_FLAG="";
	    if(tblTSTDL.getSelectedColumn() == TB_CMPVL)
		{
			int intIDXVL = 0;
		    L_fltNPFVL = 0;
		 	L_fltNPTVL = 0;
		 	tblTSTDL.editCellAt(P_intROWNUM,TB_CMPVL);
		 	try
		 	{
		 		L_fltQPRVL = Float.valueOf(tblTSTDL.getValueAt(P_intROWNUM,TB_CMPVL).toString().trim()).floatValue();
		 		intIDXVL = tblTSTDL.getValueAt(P_intROWNUM,TB_SPEC).toString().trim().indexOf("-");
		 		try
		 		{
		 			L_fltNPFVL = Float.valueOf(tblTSTDL.getValueAt(P_intROWNUM,TB_SPEC).toString().substring(0,intIDXVL - 1).trim()).floatValue();
		 		}
		 		catch(Exception L_E)
		 		{
		 			L_fltNPFVL = 0;
		 		}
				try
				{
				 	L_fltNPTVL =Float.valueOf(tblTSTDL.getValueAt(P_intROWNUM,TB_SPEC).toString().substring(intIDXVL + 2).trim()).floatValue(); 				
				}
				catch(Exception L_E)
				{
				 	L_fltNPTVL = 0;
				}
				if (arrCMPFL[P_intROWNUM].trim().equals("Y"))
				{
				    chkQPRRNG(L_fltNPFVL,L_fltNPTVL,L_fltQPRVL,P_intROWNUM);
				    L_OUTRG = false;
				 	for(int i=0;i<intTOROW;i++)
				 	{
				 		L_FLAG = tblTSTDL.getValueAt(i,TB_CHKFL).toString().trim();
				 		if(L_FLAG.length() >0)
				 		{
				 			if(L_FLAG.equals("true"))
				 			{							
				 				L_OUTRG = true;			// Out of Range
				 			}
				 		}
				 	}
				}                                          
			}
			catch(NumberFormatException L_NFE)
			{
			  setMSG("Invalid Value ...",'E');
			}
		}
	} 
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{											
				if((input == txtLOTNO) && (txtLOTNO.getText().trim().length() == 8))
				{ 
					M_strSQLQRY = "select LT_LOTNO,LT_RCLNO from PR_LTMST,CO_PRMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '";
                    M_strSQLQRY += strPRDTP + "' AND ltrim(str(LT_TPRCD,20,0)) = PR_PRDCD AND LT_LOTNO in";
			        M_strSQLQRY += " (select PS_LOTNO from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+ strQCATP +"'";
                   	M_strSQLQRY += " AND PS_TSTTP = '" + strCMPTP_fn.trim()+"') ";					
					M_strSQLQRY += " AND LT_LOTNO = '" + txtLOTNO.getText().trim() + "'  AND LT_STSFL <> 'X'";
					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						    txtRCLNO.setText(L_rstRSSET.getString("LT_RCLNO"));
						else
						{
							setMSG("Invalid Lot Number, press F1 to Select from List..",'E');							
							L_rstRSSET.close();
							return false;
						}
						L_rstRSSET.close();
					}	
				}
				if((input == txtRCLNO) && (txtRCLNO.getText().trim().length() == 2))
				{ 
					M_strSQLQRY = "select LT_RCLNO,PR_PRDDS from PR_LTMST,CO_PRMST where LT_PRDCD = PR_PRDCD";
					M_strSQLQRY +=" AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = '"+ txtLOTNO.getText().trim() +"'";
					M_strSQLQRY +=" AND LT_RCLNO = '"+ txtRCLNO.getText().trim() +"'";					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
							L_rstRSSET.close();   
						else
						{
							setMSG("Invalid Reclassification No, press F1 to Select from List..",'E');							
							L_rstRSSET.close();
							return false;
						}						
					}	
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			return true;
		}
	}
	class RowRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isselected,boolean hasFocus,int row,int col)
		{
			if(hstTSTDL.get(String.valueOf(row)) !=null)
				setForeground(Color.blue);
			else
				setForeground(Color.black);
			int cellValue = (value instanceof Number) ? ((Number)value).intValue() : 0;
			setText((value == null) ? "" : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);
			return super.getTableCellRendererComponent(table,value,isselected,hasFocus,row,col);
		}
	}
}