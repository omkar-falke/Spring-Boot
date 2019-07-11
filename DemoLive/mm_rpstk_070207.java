/*
System Name   : Material Management System
Program Name  : Material Stock Statements 
Program Desc. : User Enter the Material Group Type Or Material Sub Sub Group Type 
Author        : Mr.S.R.Mehesare
Date          : 22/10/2005
Version       : MMS v2.0.0
*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;import javax.swing.JTextField;
import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JPanel;import javax.swing.JComponent;import javax.swing.InputVerifier;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.sql.Date;import java.sql.ResultSet;import java.util.Hashtable;
/**<pre>
System Name : Material Management System.
 
Program Name : Material Stock Statements 

Purpose : Program to generate the reports for :- 
               1) Materials below Minimum Level
               2) Items With NIL Stock
               3) Probable Stock Out List
               4) Stock Statement
               5) Weekly Stock Status Of Packing Materials
               6) Materials Below Re-order Level
               7) Location wise Inventory
               8) Stock Master List

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD            #
MM_INMST       IN_MMSBS,IN_STRTP,IN_INDNO,IN_MATCD                     #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name      Type/Size     Description
--------------------------------------------------------------------------------------
txtFMGRP    substr(ST_MATCD,1,2)    MM_STMST        VARCHAR(10)   Material Code
txtTOGRP    substr(ST_MATCD,1,2)    MM_STMST        VARCHAR(10)   Material Code
txtFMSUB    substr(ST_MATCD,1,6)    MM_STMST        VARCHAR(10)   Material Code
txtTOSUB    substr(ST_MATCD,1,6)    MM_STMST        VARCHAR(10)   Material Code
txtFMMAT    ST_MATCD                MM_STMST        VARCHAR(10)   Material Code
txtTOMAT    ST_MATCD                MM_STMST        VARCHAR(10)   Material Code
txtFMLOC    ST_LOCCD                MM_STMST        VARCHAR(10)   Location Code
txtTOLOC    ST_LOCCD                MM_STMST        VARCHAR(10)   Location Code
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
1)For Materials below Minimum Level Report, Data is taken from MM_STMST,MM_POMST 
  and MM_INMST for condiations :-
     1) MM_STMST LEFT OUTER JOIN MM_POMST ON 
     2) ST_STRTP = PO_STRTP 
     3) AND  ST_MATCD = PO_MATCD 
     4) AND ifnull(PO_PORQT,0)-ifnull(PO_ACPQT,0)-IFNULL(PO_FRCQT,0) >0 
     5) AND IFNULL(PO_STSFL ,'') <>'X'
     6) LEFT OUTER JOIN MM_INMST ON 
     7) ST_STRTP = IN_STRTP 
     8) AND ST_MATCD = IN_MATCD 
     9) AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) >0 
    10) AND IFNULL(IN_STSFL,'') <>'X'"
    11) WHERE ST_STKFL = 'Y' 
    12) AND ifnull(ST_STKQT,0) < ifnull(ST_MINLV,0) 
    13) AND ST_MMSBS = Specified Subsystem Code
    14) AND substr(ST_MATCD,1,2) between given range of Material Code

2)For Items With NIL Stock Report, Data is taken from MM_STMST,MM_POMST & MM_INMST for condiations :-
    1) MM_STMST left outer join MM_POMST ON ST_STRTP = PO_STRTP 
    2) AND ST_MATCD = PO_MATCD 
    3) AND IFNULL(PO_PORQT,0)-IFNULL(PO_ACPQT,0)- IFNULL(PO_FRCQT,0) >0 
    4) AND PO_STSFL NOT IN('C','X') 
    5) LEFT OUTER JOIN MM_INMST ON  ST_STRTP = IN_STRTP 
    6) AND ST_MATCD = IN_MATCD  
    7) AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0) - IFNULL(IN_FCCQT,0) >0 
    8) AND IN_STSFL <> 'X' AND IN_INDTP ='01' 
    9) WHERE ifnull(ST_STKQT,0) = 0 
   10) AND ST_MMSBS = Specified Sub System Code.
   11) AND  ST_STSFL <> 'X' 
   12) AND substr(ST_MATCD,1,6) between given Sub Sub Group Code.
  if Stock Controlled Item requried
   13) AND IFNULL(ST_STKFL,'') ='Y'

3)For Probable Stock Out List Report, Data is taken from MM_STMST for condiations :-
    1) ST_STKFL = 'Y' 
    2) AND (ifnull(ST_STKQT,0)+ifnull(ST_STKIN,0)+ifnull(ST_STKOR,0)) <= ifnull(ST_MINLV,0) 
    3) AND ST_MMSBS = specified Sub System Code
    4) AND ST_STRTP = Selected Store type
    5) AND substr(ST_MATCD,1,6) between Specified Material Code Range 
    6) AND ifnull(ST_STSFL,'') <> 'X'					

4)For Stock Statement Report, Data is taken from MM_STMST for condiations :-
    1) ifnull(ST_STKQT,0) > 0 
    2) AND substr(ST_MATCD,1,2) between given range of Material Group Code
	3) AND ST_MMSBS = Specified Sub System Code 
    4) AND ifnull(ST_STSFL,'') <> 'X' 
    5) AND ST_STRTP = Specified Store Type.

5)For Weekly Stock Status Of Packing Materials Report, Data is taken from MM_STMST for condiations :-
    1) substr(ST_MATCD,1,6) in ('690520','691010') 
    2) AND ifnull(ST_STKQT,0) > 0 
    3) AND ST_STSFL <> 'X' 
    4) AND ST_MMSBS = Specified Sub System Code.
	
6) For Materials Below Re-order Level Report, Data is taken from MM_STMST for condiations :-
    1) FROM MM_STMST left OUTER JOIN MM_POMST ON ST_STRTP = PO_STRTP 
    2) AND  ST_MATCD = PO_MATCD 
    3) AND ifnull(PO_PORQT,0)- ifnull(PO_ACPQT,0)-IFNULL(PO_FRCQT,0) >0 
    4) AND IFNULL(PO_STSFL ,'') <>'X'
    5) LEFT OUTER JOIN MM_INMST ON ST_STRTP = IN_STRTP 
    6) AND ST_MATCD = IN_MATCD 
    7) AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) >0 
    8) AND IFNULL(IN_STSFL,'') <>'X'
    9) WHERE ST_STKFL = 'Y' 
   10) AND ifnull(ST_STKQT,0) < ifnull(ST_RORLV,0) 
   11) AND ST_MMSBS = Specified SubSystem Code 
   12) AND substr(ST_MATCD,1,2) between th given range of Material Group Code
   13) AND  ST_STSFL <> 'X'

7)For Location wise Inventory Report, Data is taken from MM_STMST for condiations :-
    1) ST_STRTP = Speciied Store Type
    2) AND ST_LOCCD between specified Range of location code.
    3) AND IFNULL(ST_STSFL,'') <> 'X' 
    4) AND IFNULL(ST_STKQT,0) <> 0

8)For Stock Master List Report, Data is taken from MM_STMST & CO_CTMST for condiations :-
    1) ST_MATCD = CT_MATCD 
    2) AND CT_CODTP ='CD' 
    3) AND ST_STRTP = Specified Store Type 
    4) AND ifnull(ST_STSFL,'') <> 'X' 
    5) AND ST_MATCD between th Range of Specified Material Code Range.

<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then current Date.
    - Material Code if Specified must be Valid.
    - Location Code must be valid.
</I> */

class mm_rpstk extends cl_rbase 
{									/** JPanel to display Main Group title.*/
	private JPanel pnlMGROP;		/** JPanel to display Sub Group title.*/
	private JPanel pnlSGROP;		/** JPanel to display Material Type.*/
	private JPanel pnlMATCD;		/** JPanel to display Weekly Stock Items.*/
	private JPanel pnlWEEST;		/** JPanel to display Location codes.*/
	private JPanel pnlLCNWS;		/** JTable to display weekly stock details	 */
	private cl_JTable tblWEEST;		/** JTextField to display & to specify From Group Code.*/
	private JTextField txtFMGRP;	/** JTextField to display to specify To Group.*/
	private JTextField txtTOGRP;	/** JTextField to display to specify From Sub Group Code.*/
	private JTextField txtFMSUB;	/** JTextField to display & to spacify TO Sub Group Code.*/
	private JTextField txtTOSUB;	/** JTextField to display & to specify From Material Code.*/
	private JTextField txtFMMAT;	/** JTextField to display & to specify To Material Code */
	private JTextField txtTOMAT;	/** JTextField to display & to specify From Location Code.*/
	private JTextField txtFMLOC;	/** JTExtField to display & to specify To Location Code.*/
	private JTextField txtTOLOC;	/** JComboBox to specify the Report Options.*/
	private JComboBox cmbRPTOP;		/** JCheckBox to specify (nil Item) Stock Control Item.*/
	private JCheckBox chbSTKFL;	
	private JRadioButton rdoDETAIL;
	private JRadioButton rdoSUMRY;
									/** Integer Variable for serial number.*/
	private int intSRLNO;			/** Integer variable row Number.*/
	private int intROWNO;			/** Integer variable row count.*/
	private int intROWCT;			/** String vaiable for generated Report File Name.*/
	private String strFILNM;		/** String varaible for Expected Date.*/
	private String strINDDT="";		/** String variable for Required Date.*/
	private String strEXPDT="";		/** String variable for P.O. Rate.*/
	private String strREQDT="";		/** String varaible for Will Lost For Day.*/
	private String strSTODT;		/** String varaible for Material Type.*/
	private String strMATTP;		/** FileOutputStream Object to generate the report file from the  stream of data.*/
	private FileOutputStream fosREPORT ;	/** DateoutputStream Object to generate & hold the stream of Report data.*/ 
    private DataOutputStream dosREPORT ;	/** Integer variable to count the number of records fetched to block the report if no data found.*/
	private int intRECCT =0;
	private String strDOTLN;
	private Hashtable hstLOCPR;
	mm_rpstk()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			pnlMGROP = new JPanel(null);	//Panel For manin group  option
			pnlSGROP = new JPanel(null);	//Panel for sub group option
			pnlWEEST = new JPanel(null);	//Panel for weekly stock statement	
			pnlMATCD = new JPanel(null);	//Panel for material code option
			cmbRPTOP = new JComboBox();	
			cmbRPTOP.setMaximumRowCount(10);	//add items to report option combo box			
			cmbRPTOP.addItem("1 Materials Below Minimum Level ");
			cmbRPTOP.addItem("2 Items With NIL Stock");
			cmbRPTOP.addItem("3 Probable Stock Out List");
			cmbRPTOP.addItem("4 Stock Statement ");
			cmbRPTOP.addItem("5 Weekly Stock Status Of Packing Materials ");
			cmbRPTOP.addItem("6 Materials Below Re-order Level");
			cmbRPTOP.addItem("7 Location wise Inventory");
			cmbRPTOP.addItem("8 Stock Master List");
			setMatrix(20,8);
			add(new JLabel("  Report Option"),3,4,1,2,this,'R');
			add(cmbRPTOP,3,5,1,3,this,'L');
			add(new JLabel("From Main Group"),3,4,1,2,pnlMGROP,'R');
			add(txtFMGRP = new TxtNumLimit(2.0),3,5,1,1,pnlMGROP,'L');
			add(new JLabel("To Main Group"),4,4,1,2,pnlMGROP,'R');
			add(txtTOGRP = new TxtNumLimit(2.0),4,5,1,1,pnlMGROP,'L');
			add(new JLabel("From Sub Sub Group"),3,4,1,2,pnlSGROP,'R');
			add(txtFMSUB = new TxtNumLimit(6.0),3,5,1,1,pnlSGROP,'L');
			add(new JLabel("To Sub Sub Group"),4,4,1,2,pnlSGROP,'R');
			add(txtTOSUB = new TxtNumLimit(6.0),4,5,1,1,pnlSGROP,'L');
			add(chbSTKFL = new JCheckBox("Stock Controlled Item"),5,5,1,2,pnlSGROP,'L');
			tblWEEST = crtTBLPNL1(pnlWEEST,new String[]{"","Mat. Code","Description","UOM","Stk. On Hand","Stk. On Insp.","Daily Consumption","For Days","Remarks"},300,2,1,14,7.96,new int[]{20,80,191,40,80,80,80,80,95},new int[]{0});
			tblWEEST.setInputVerifier(new TBLINPVF());
			add(new JLabel("From Material Code"),3,4,1,2,pnlMATCD,'R');
			add(txtFMMAT = new TxtNumLimit(10.0),3,5,1,1,pnlMATCD,'L');
			add(new JLabel("To Material Code"),4,4,1,2,pnlMATCD,'R');
			add(txtTOMAT = new TxtNumLimit(10.0),4,5,1,1,pnlMATCD,'L');
			///***ADDED BY APP 07/06/04 for LOCATION WISE INVENTORY
			pnlLCNWS=new JPanel(null);
			add(rdoDETAIL = new JRadioButton("Details"),2,5,1,1,pnlLCNWS,'R');
			add(rdoSUMRY = new JRadioButton("Summary"),2,6,1,1,pnlLCNWS,'R');
			ButtonGroup bgrRPTOP = new ButtonGroup();
			bgrRPTOP.add(rdoSUMRY);
			bgrRPTOP.add(rdoDETAIL);
			add(new JLabel("From Location"),3,4,1,2,pnlLCNWS,'R');
			add(txtFMLOC = new TxtLimit(5),3,5,1,1,pnlLCNWS,'L');
			add(new JLabel("To Location"),4,4,1,2,pnlLCNWS,'R');
			add(txtTOLOC = new TxtLimit(5),4,5,1,1,pnlLCNWS,'L');
			add(pnlLCNWS,2,1,17,8,this,'L');
			pnlLCNWS.setVisible(false);			
			add(pnlMGROP,2,1,17,8,this,'L');
			add(pnlSGROP,2,1,17,8,this,'L');
			add(pnlWEEST,3,1,16,8,this,'L');
			add(pnlMATCD,2,1,17,8,this,'L');
			pnlMGROP.setVisible(true);
			pnlSGROP.setVisible(false);
			pnlWEEST.setVisible(false);
			pnlMATCD.setVisible(false);
			txtFMGRP.setInputVerifier(new INPVF());
			txtTOGRP.setInputVerifier(new INPVF());
			txtFMSUB.setInputVerifier(new INPVF());
			txtTOSUB.setInputVerifier(new INPVF());
			txtFMMAT.setInputVerifier(new INPVF());
			txtTOMAT.setInputVerifier(new INPVF());			
			txtTOLOC.setInputVerifier(new INPVF());
			txtFMLOC.setInputVerifier(new INPVF());			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			tblWEEST.addMouseListener(this);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				setENBL(true);				
				setMSG("Select Report Type..",'N');
				if(cmbRPTOP.getSelectedIndex() == 1 )
					txtFMSUB.requestFocus();
				else if(cmbRPTOP.getSelectedIndex() == 2 )
					txtFMSUB.requestFocus();
				else if(cmbRPTOP.getSelectedIndex() == 0 || cmbRPTOP.getSelectedIndex() == 3 || cmbRPTOP.getSelectedIndex() == 5)			
					txtFMGRP.requestFocus();
				else if(cmbRPTOP.getSelectedIndex() == 6)
					txtFMLOC.requestFocus();
				else if(cmbRPTOP.getSelectedIndex() == 7)
					txtFMMAT.requestFocus();
			}
			else
			{
				setENBL(false);				
			}
		}
		if((M_objSOURC == rdoSUMRY)||(M_objSOURC == rdoDETAIL))
		{
		    if(rdoSUMRY.isSelected())
		    {
		        txtFMLOC.setEnabled(false);
		        txtTOLOC.setEnabled(false);
		        txtFMLOC.setText("");
		        txtTOLOC.setText("");
		    }
		    else
		    {
		        txtFMLOC.setEnabled(true);
		        txtTOLOC.setEnabled(true);
		    }
		}
		if(M_objSOURC == cmbRPTOP)
		{
			if(cmbRPTOP.getSelectedIndex() == 1 )
			{
				pnlMGROP.setVisible(false);
				pnlLCNWS.setVisible(false);
				pnlSGROP.setVisible(true);
				chbSTKFL.setVisible(true);
				txtFMSUB.requestFocus();
				txtFMSUB.setText("");
				txtTOSUB.setText("");
				setMSG("Enter Sub Sub Group Code Or Press 'F1' For Help",'N');
			}
			if(cmbRPTOP.getSelectedIndex() == 2 )
			{
				pnlMGROP.setVisible(false);
				pnlLCNWS.setVisible(false);
				pnlSGROP.setVisible(true);
				chbSTKFL.setVisible(false);
				txtFMSUB.requestFocus();
				txtFMSUB.setText("");
				txtTOSUB.setText("");
				setMSG("Enter Sub Sub Group Code Or Press 'F1' For Help",'N');
			}
			if(cmbRPTOP.getSelectedIndex() == 0 || cmbRPTOP.getSelectedIndex() == 3 || cmbRPTOP.getSelectedIndex() == 5)
			{
				pnlMGROP.setVisible(true);
				pnlLCNWS.setVisible(false);
				pnlSGROP.setVisible(false);
				pnlMATCD.setVisible(false);
				txtFMGRP.requestFocus();
				txtFMGRP.setText("");
				txtTOGRP.setText("");
				setMSG("Enter Main Group Code Or Press 'F1' For Help",'N');
			}
			if(cmbRPTOP.getSelectedIndex() == 4)
			{
				pnlMGROP.setVisible(false);
				pnlSGROP.setVisible(false);
				pnlLCNWS.setVisible(false);
				pnlWEEST.setVisible(true);
				tblWEEST.setEnabled(false);
				pnlMATCD.setVisible(false);
				tblWEEST.clrTABLE();
				tblWEEST.cmpEDITR[6].setEnabled(true);
				tblWEEST.cmpEDITR[8].setEnabled(true);
				setMSG("Enter Daily Consumption..",'N');
				getTBLDT();
			}
			if(cmbRPTOP.getSelectedIndex() == 6)
			{
				pnlMGROP.setVisible(false);
				pnlSGROP.setVisible(false);
				pnlWEEST.setVisible(false);
				pnlMATCD.setVisible(false);
				pnlLCNWS.setVisible(true);
				txtFMLOC.requestFocus();
				setMSG("Enter From Location Or Press 'F1' For Help",'N');
			}			
			///****ADDED BY AAP 05/08/2004 To OCCOMODATE STOCK MASTER LIST
			if(cmbRPTOP.getSelectedIndex() == 7)
			{
				pnlLCNWS.setVisible(false);
				pnlMGROP.setVisible(false);
				pnlSGROP.setVisible(false);
				pnlWEEST.setVisible(false);
				pnlMATCD.setVisible(true);
				txtFMMAT.requestFocus();
			}		
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			cl_dat.M_flgHELPFL_pbst = true; 
			setMSG("",'N');
			setCursor(cl_dat. M_curWTSTS_pbst);			
			if(M_objSOURC == txtFMLOC)
			{	
				txtFMLOC.setText(txtFMLOC.getText().toUpperCase());
				M_strHLPFLD = "txtFMLOC";
				M_strSQLQRY ="SELECT distinct ST_LOCCD FROM MM_STMST Where ifnull(ST_STSFL,'')<>'X'";
				if(txtFMLOC.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtFMLOC.getText().trim() +"%'";
				if(txtTOLOC.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD <= '"+txtTOLOC.getText().trim() +"'";
				M_strSQLQRY+=" order by ST_LOCCD";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Location Code"},1,"CT");
			}
			else if(M_objSOURC == txtTOLOC)
			{	
				txtTOLOC.setText(txtTOLOC.getText().toUpperCase());
				M_strHLPFLD = "txtTOLOC";
				M_strSQLQRY ="SELECT distinct ST_LOCCD FROM MM_STMST Where ifnull(ST_STSFL,'')<>'X'";
				if(txtTOLOC.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtTOLOC.getText().trim() +"%'";
				if(txtFMLOC.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD >='"+txtFMLOC.getText().trim() +"'";
				M_strSQLQRY+=" order by ST_LOCCD";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Location Code"},1,"CT");
			}
			if(M_objSOURC == txtFMGRP)
			{
				M_strHLPFLD = "txtFMGRP";
				M_strSQLQRY ="SELECT CT_GRPCD,CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='MG' ";
				if(txtFMGRP.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_GRPCD like '"+txtFMGRP.getText().trim() +"%'";
				if(!M_strSBSCD.substring(2,4).equals("01"))
				{
					if(!M_strSBSCD.substring(2,4).equals("11"))
						M_strSQLQRY += " AND CT_GRPCD ='68'";
					else
						M_strSQLQRY += " AND CT_GRPCD ='95'";
				}				
				if(txtTOGRP.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_GRPCD <='"+txtTOGRP.getText().trim() +"'";
				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Group Name"},2,"CT");
			}
			if(M_objSOURC == txtTOGRP)
			{
				M_strHLPFLD = "txtTOGRP";
				M_strSQLQRY ="SELECT CT_GRPCD,CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='MG' ";
				if(txtTOGRP.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_GRPCD like '"+txtTOGRP.getText().trim() +"%'";				
				if(!M_strSBSCD.substring(2,4).equals("01"))
				{
					if(!M_strSBSCD.substring(2,4).equals("11"))
						M_strSQLQRY += " AND CT_GRPCD ='68'";
					else
						M_strSQLQRY += " AND CT_GRPCD ='95'";
				}
				if(txtFMGRP.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_GRPCD >='"+txtFMGRP.getText().trim() +"'";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Group Name"},2,"CT");
			}
			if(M_objSOURC == txtFMSUB)
			{
				M_strHLPFLD = "txtFMSUB";
				M_strSQLQRY ="SELECT SUBSTR(CT_MATCD,1,6),CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='SS' ";
				if(txtFMSUB.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_MATCD like '"+txtFMSUB.getText().trim() +"%'";
				if(!M_strSBSCD.substring(2,4).equals("01"))
				{
					if(!M_strSBSCD.substring(2,4).equals("11"))
						M_strSQLQRY += " AND CT_GRPCD ='68'";
					else
						M_strSQLQRY += " AND CT_GRPCD ='95'";
				}
				if(txtTOSUB.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_MATCD <= '"+txtTOSUB.getText().trim() +"'";
				M_strSQLQRY += " ORDER BY substr(CT_MATCD,1,6)";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Sub Sub Group Code","Group Name"},2,"CT");
			}
			if(M_objSOURC == txtTOSUB)
			{
				M_strHLPFLD = "txtTOSUB";
				M_strSQLQRY ="SELECT SUBSTR(CT_MATCD,1,6),CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='SS' ";
				if(txtFMSUB.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_MATCD like '"+txtTOSUB.getText().trim() +"%'";				
				if(!M_strSBSCD.substring(2,4).equals("01"))
				{
					if(!M_strSBSCD.substring(2,4).equals("11"))
						M_strSQLQRY += " AND CT_GRPCD ='68'";
					else
						M_strSQLQRY += " AND CT_GRPCD ='95'";
				}
				if(txtFMSUB.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_MATCD >= '"+txtFMSUB.getText().trim() +"'";	
				M_strSQLQRY += " ORDER BY substr(CT_MATCD,1,6)";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Group Name"},2,"CT");
			}
			if(M_objSOURC == txtFMMAT)
			{
				M_strHLPFLD = "txtFMMAT";
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS FROM MM_STMST WHERE ifnull(ST_STSFL,'') <> 'X' AND ";
				M_strSQLQRY +=" ST_MMSBS ='"+M_strSBSCD+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				if(txtFMMAT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_MATCD like '"+txtFMMAT.getText().trim() +"%'";
				if(txtTOMAT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_MATCD <='"+txtTOMAT.getText().trim() +"'";
				M_strSQLQRY += " ORDER BY ST_MATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");
			}
			if(M_objSOURC == txtTOMAT)
			{
				M_strHLPFLD = "txtTOMAT";
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS FROM MM_STMST WHERE ifnull(ST_STSFL,'') <> 'X' AND ";
				M_strSQLQRY +=" ST_MMSBS ='"+M_strSBSCD+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				if(txtTOMAT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_MATCD like '"+txtTOMAT.getText().trim() +"%'";
				if(txtFMMAT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_MATCD >= '"+txtFMMAT.getText().trim() +"'";
				M_strSQLQRY += " ORDER BY ST_MATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");				
			}
			setCursor(cl_dat. M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			setMSG("",'N');
			if(M_objSOURC == cmbRPTOP)
				if(cmbRPTOP.getSelectedIndex() == 4)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			if(M_objSOURC == txtFMGRP)
			{
				if(txtFMGRP.getText().trim().length()>0)
					txtTOGRP.requestFocus();
				else
					setMSG("To group Code Cannot be Blank..",'E');
			}
			if(M_objSOURC == txtTOGRP)
			{
				if(txtTOGRP.getText().trim().length()>0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					setMSG("From group Code Cannot be Blank..",'E');
				
			}
			if(M_objSOURC == txtFMSUB)
			{
				if(txtFMSUB.getText().trim().length()>0)
					txtTOSUB.requestFocus();
				else
					setMSG("From Sub Sub Group cannnot be Blank..",'E');
			}
			if(M_objSOURC == txtTOSUB)
			{
				if(txtTOSUB.getText().trim().length()>0)
				{
					if(cmbRPTOP.getSelectedIndex() == 1)
						chbSTKFL.requestFocus();
					if(cmbRPTOP.getSelectedIndex() == 2)
						cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else
					setMSG("To Sub Sub group cannot be Blank..",'E');
			}
			if(M_objSOURC == txtFMMAT)
			{
				if(txtFMMAT.getText().trim().length()>0)				
					txtTOMAT.requestFocus();
				else
					setMSG("From Material Code Cannot be Blank..",'E');
			}
			if(M_objSOURC == txtTOMAT)
			{
				if(txtTOMAT.getText().trim().length()>0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					setMSG("TO Material Code cannot blank..",'E');
			}
			if(M_objSOURC == txtFMLOC)
			{
				if(txtFMLOC.getText().trim().length()>0)
					txtTOLOC.requestFocus();
				else
					setMSG("From Loaction code Cannot be Blank..",'E');
			}
			if(M_objSOURC == txtTOLOC)
			{
				if(txtTOLOC.getText().trim().length()>0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					setMSG("To Location Code Cannot Be Blank..",'E');
			}
			if(M_objSOURC == chbSTKFL)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	public void focusGained(FocusEvent P_FE)
	{
		super.focusGained(P_FE);
		if(!M_flgERROR)
		{
			if(M_objSOURC == txtFMGRP)
				setMSG("Enter From Group Code, Press 'F1' For Help ..",'N');
			if(M_objSOURC == txtTOGRP)
				setMSG("Enter To Group code, Press 'F1' For Help .. ",'N');
			if(M_objSOURC == txtFMMAT)
				setMSG("Enter From Material Code, Press 'F1' For Help ..",'N');
			if(M_objSOURC == txtTOMAT)
				setMSG("Enter To Material Code, Press 'F1' For Help ..",'N');
			if(M_objSOURC == txtFMLOC)
				setMSG("Enter From Location, Press 'F1' for help ..",'N');
			if(M_objSOURC == txtTOLOC)
				setMSG("Enter To Location, Press 'F1' for help ..",'N');
			if(M_objSOURC == txtFMSUB)
				setMSG("Enter From Sub Sub Group Code, Press 'F1' For Help ..",'N');
			if(M_objSOURC == txtTOSUB)
				setMSG("Enter To Sub Sub Group Code, Press 'F1' For Help .. ",'N');
			if(M_objSOURC == chbSTKFL)
				setMSG("Click For Stock Controlled Items ..",'N');
		}
	}
	/**
	 * Super Class Method overrided to execuate the F1 help for selected component.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;		
		if(M_strHLPFLD == "txtFMLOC")
		{
			txtFMLOC.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOLOC.requestFocus();
			setMSG("Enter Location Code Or Press F1 for Help ..",'N');
		}
		else if(M_strHLPFLD == "txtTOLOC")
		{
			txtTOLOC.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
		if(M_strHLPFLD == "txtFMGRP")
		{
			txtFMGRP.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOGRP.requestFocus();
		}
		if(M_strHLPFLD == "txtTOGRP")
		{
			txtTOGRP.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		if(M_strHLPFLD == "txtFMSUB")
		{
			txtFMSUB.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOSUB.requestFocus();
		}
		if(M_strHLPFLD == "txtTOSUB")
		{
			txtTOSUB.setText(cl_dat.M_strHLPSTR_pbst);
			if(cmbRPTOP.getSelectedIndex() == 1)
				chbSTKFL.requestFocus();
			else
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		if(M_strHLPFLD == "txtFMMAT")
		{
			txtFMMAT.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOMAT.requestFocus();
		}
		if(M_strHLPFLD == "txtTOMAT")
		{
			txtTOMAT.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	private void getDATA()
	{
		try
		{
		    String L_strLOCCD ="",L_strPRLOC="";
		    int L_intITMCT =0;
		    double L_dblSTKVL =0;
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');			
			intRECCT =0;
			//Materials Below Minimum Level
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				{
				    prnFMTCHR(dosREPORT,M_strNOCPI17);
				    prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI17);
				}
				if(M_rdbHTML.isSelected())
				{
				    dosREPORT.writeBytes("<HTML><HEAD><Title>Materials Below Minimum Level</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}
			     // Old query - Repititive data
				/* M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,ST_RORLV,ST_STODT,"
					+"ST_RORQT,ST_STKQT,ST_STKIP,ST_STKIN,ST_STKOR,ST_YTDIS,PO_PORNO,IFNULL(PO_PORQT,0)-IFNULL(PO_ACPQT,0) L_PORQT,PO_PORRT,"
					+"IN_INDNO,IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0) L_INDQT,ST_PPONO "
					+"FROM MM_STMST left OUTER JOIN MM_POMST ON "
					+"ST_STRTP = PO_STRTP AND  ST_MATCD = PO_MATCD "
					+" AND ifnull(PO_PORQT,0) > ifnull(PO_ACPQT,0) AND IFNULL(PO_STSFL ,'') <>'X' "
					+" LEFT OUTER JOIN MM_INMST ON ST_STRTP = IN_STRTP AND ST_MATCD = IN_MATCD "
					+" AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0) >0 AND IFNULL(IN_STSFL,'') <>'X'"
					+" WHERE ST_STKFL = 'Y' AND "
					+"ifnull(ST_STKQT,0) < ifnull(ST_MINLV,0) AND ST_MMSBS = '"+M_strSBSCD+"' AND "
				    +"substr(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText()+"' AND '"+txtTOGRP.getText()+"' AND  ST_STSFL <> 'X' "
					+"ORDER BY ST_MATCD"; */
				
			    // changed on 17/12/2004, request by JGV, To be observed properly. 
				M_strSQLQRY = "SELECT distinct ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,ST_RORLV,ST_STODT,"
					+"ST_RORQT,ST_STKQT,ST_STKIP,ST_STKIN,ST_STKOR,ST_YTDIS,PO_PORNO,IFNULL(PO_PORQT,0)-IFNULL(PO_ACPQT,0)-IFNULL(PO_FRCQT,0) L_PORQT,PO_PORRT,"
					+"IN_INDNO,IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) L_INDQT,ST_PPONO "
					+"FROM MM_STMST left OUTER JOIN MM_POMST ON "
					+"ST_STRTP = PO_STRTP AND  ST_MATCD = PO_MATCD "
					+" AND ifnull(PO_PORQT,0)-ifnull(PO_ACPQT,0)-IFNULL(PO_FRCQT,0) >0 AND IFNULL(PO_STSFL ,'') <>'X' "
					+" LEFT OUTER JOIN MM_INMST ON ST_STRTP = IN_STRTP AND ST_MATCD = IN_MATCD "
					+" AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) >0 AND IFNULL(IN_STSFL,'') <>'X' AND IN_INDTP ='01'"
					+" WHERE ST_STKFL = 'Y' AND "
					+"ifnull(ST_STKQT,0) < ifnull(ST_MINLV,0) AND ST_MMSBS = '"+M_strSBSCD+"' AND "
				    +"substr(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText()+"' AND '"+txtTOGRP.getText()+"' AND  ST_STSFL <> 'X' "
					+"ORDER BY ST_MATCD";
			}
			//Items With NIL Stock
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				{
				    prnFMTCHR(dosREPORT,M_strNOCPI17);
				    prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI12);
				}
				if(M_rdbHTML.isSelected())
				{
				    dosREPORT.writeBytes("<HTML><HEAD><Title>Items With NIL Stock</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_STKIN,ST_STKOR,ST_STKIP,"
					+"ST_STODT,PO_PORNO,IN_INDNO,IN_INDDT,IN_EXPDT,IN_REQDT "
					+"FROM MM_STMST left outer join MM_POMST ON  ST_STRTP = PO_STRTP AND "
					+"ST_MATCD = PO_MATCD  AND IFNULL(PO_PORQT,0)-IFNULL(PO_ACPQT,0)- IFNULL(PO_FRCQT,0) >0 AND "
					+"PO_STSFL NOT IN('C','X') LEFT OUTER JOIN MM_INMST ON  ST_STRTP = IN_STRTP "
					+"AND ST_MATCD = IN_MATCD  AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0) - IFNULL(IN_FCCQT,0) >0 "
					+"AND IN_STSFL <> 'X' AND IN_INDTP ='01' WHERE ifnull(ST_STKQT,0) = 0 AND ST_MMSBS = '"+M_strSBSCD+"' "
					+"AND  ST_STSFL <> 'X' AND substr(ST_MATCD,1,6) BETWEEN '"+txtFMSUB.getText()+"' AND '"
					+txtTOSUB.getText()+"' ";
				if(chbSTKFL.isSelected())
					M_strSQLQRY += "AND IFNULL(ST_STKFL,'') ='Y' ";
				M_strSQLQRY += "ORDER BY ST_MATCD ";
			}
			//Probable Stock Out List			
			if(cmbRPTOP.getSelectedIndex() == 2)
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				{
				    prnFMTCHR(dosREPORT,M_strNOCPI17);
				    prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI12);
				}
				if(M_rdbHTML.isSelected())
				{
				    dosREPORT.writeBytes("<HTML><HEAD><Title>Probable Stock Out List</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_MINLV,ST_STKQT,ST_STKIN,"
					+"ST_STKOR,ST_YTDIS,ST_PPOVL FROM MM_STMST WHERE ST_STKFL = 'Y' AND "
					+"(ifnull(ST_STKQT,0)+ifnull(ST_STKIN,0)+ifnull(ST_STKOR,0)) <= ifnull(ST_MINLV,0) AND ST_MMSBS = '"+M_strSBSCD+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+" substr(ST_MATCD,1,6) BETWEEN '"+txtFMSUB.getText()+"' AND '"+txtTOSUB.getText()+"' AND  ifnull(ST_STSFL,'') <> 'X' "
					+"ORDER BY ST_MATCD ";
			}
			//Stock Statement
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				{
				    prnFMTCHR(dosREPORT,M_strNOCPI17);
				    prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI12);
				}
				if(M_rdbHTML.isSelected())
				{
				    dosREPORT.writeBytes("<HTML><HEAD><Title>Stock Statement</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS, ST_UOMCD, ST_STKQT,ST_LOCCD FROM MM_STMST "
					+"WHERE ifnull(ST_STKQT,0) > 0 AND "
					+"substr(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText()+"' AND '"+txtTOGRP.getText()+"' "
					+"AND ST_MMSBS = '"+M_strSBSCD+"' AND ifnull(ST_STSFL,'') <> 'X' AND "
					+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' ORDER BY ST_MATCD ";
			}
			//MATERIALS BELOW RE-ORDER LEVEL
			if(cmbRPTOP.getSelectedIndex() == 5)
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				{
				    prnFMTCHR(dosREPORT,M_strNOCPI17);
				    prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI17);
				}
				if(M_rdbHTML.isSelected())
				{
				    dosREPORT.writeBytes("<HTML><HEAD><Title>Materials below Re-order Level</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}
				// Original query , FCC quqntity not considered
			/*	M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,ST_RORLV,ST_STODT,"
					+"ST_RORQT,ST_STKQT,ST_STKIP,ST_STKIN,ST_STKOR,ST_YTDIS,PO_PORNO,IFNULL(PO_PORQT,0)-IFNULL(PO_ACPQT,0) L_PORQT,PO_PORRT,"
					+"IN_INDNO,IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0) L_INDQT,ST_PPONO "
					+"FROM MM_STMST left OUTER JOIN MM_POMST ON "
					+"ST_STRTP = PO_STRTP AND  ST_MATCD = PO_MATCD "
					+" AND ifnull(PO_PORQT,0) > ifnull(PO_ACPQT,0) AND IFNULL(PO_STSFL ,'') <>'X' "
					+" LEFT OUTER JOIN MM_INMST ON ST_STRTP = IN_STRTP AND ST_MATCD = IN_MATCD "
					+" AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0) >0 AND IFNULL(IN_STSFL,'') <>'X'"
					+" WHERE ST_STKFL = 'Y' AND "
					+"ifnull(ST_STKQT,0) < ifnull(ST_RORLV,0) AND ST_MMSBS = '"+M_strSBSCD+"' AND "
				    +"substr(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText()+"' AND '"+txtTOGRP.getText()+"' AND  ST_STSFL <> 'X' "
					+"ORDER BY ST_MATCD";*/
				
				// Below query is in line with query for below min level. If existing 
				// query has problem, of repititive data, this option is to be checked.
				
				M_strSQLQRY = "SELECT distinct ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,ST_RORLV,ST_STODT,"
					+"ST_RORQT,ST_STKQT,ST_STKIP,ST_STKIN,ST_STKOR,ST_YTDIS,PO_PORNO,IFNULL(PO_PORQT,0)-IFNULL(PO_ACPQT,0)-IFNULL(PO_FRCQT,0) L_PORQT,PO_PORRT,"
					+"IN_INDNO,IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) L_INDQT,ST_PPONO "
					+"FROM MM_STMST left OUTER JOIN MM_POMST ON "
					+"ST_STRTP = PO_STRTP AND  ST_MATCD = PO_MATCD "
					+" AND ifnull(PO_PORQT,0)- ifnull(PO_ACPQT,0)-IFNULL(PO_FRCQT,0) >0 AND IFNULL(PO_STSFL ,'') <>'X' "
					+" LEFT OUTER JOIN MM_INMST ON ST_STRTP = IN_STRTP AND ST_MATCD = IN_MATCD "
					+" AND IFNULL(IN_INDQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) >0 AND IFNULL(IN_STSFL,'') <>'X' AND IN_INDTP ='01'"
					+" WHERE ST_STKFL = 'Y' AND "
					+"ifnull(ST_STKQT,0) < ifnull(ST_RORLV,0) AND ST_MMSBS = '"+M_strSBSCD+"' AND "
				    +"substr(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText()+"' AND '"+txtTOGRP.getText()+"' AND  ST_STSFL <> 'X' "
					+"ORDER BY ST_MATCD";
			}
			///***ADDED BY APP 07/06/04 for LOCATION WISE INVENTORY
			else if(cmbRPTOP.getSelectedIndex() == 6)
			{
				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				{
				    prnFMTCHR(dosREPORT,M_strNOCPI17);
				    prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI12);
				}
				if(M_rdbHTML.isSelected())
				{
				    dosREPORT.writeBytes("<HTML><HEAD><Title>Location wise Inventory</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}
				hstLOCPR = new Hashtable();
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='MMXXLCP' AND IFNULL(CMT_STSFL,'') <>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
				    while(M_rstRSSET.next())
				    {
				        hstLOCPR.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				    }
				    M_rstRSSET.close();
				}
				if(rdoSUMRY.isSelected())
				{
				     M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS,COUNT(STP_MATCD)L_ITMCT,SUM(IFNULL(STP_YCSVL,0))L_ITMVL FROM CO_CDTRN,MM_STPRC,MM_STMST "+
				     " WHERE STP_STRTP = ST_STRTP AND STP_MATCD = ST_MATCD AND CMT_CGMTP = 'SYS' AND CMT_CGSTP ='MMXXLCP' AND CMT_CODCD = SUBSTR(ST_LOCCD,1,2)"+ 
                     " AND STP_STRTP ='"+M_strSBSCD.substring(0,2)+"' AND STP_YCSQT >0 GROUP BY CMT_CODCD,CMT_CODDS";
				}
				else
				{
				    M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_STKQT,STP_YCSVL "
					+"FROM MM_STMST,MM_STPRC WHERE ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"ST_LOCCD BETWEEN '"+txtFMLOC.getText()+"' AND '"+txtTOLOC.getText()+"' "
					+"AND  IFNULL(ST_STSFL,'') <> 'X' AND IFNULL(ST_STKQT,0) <> 0 ORDER BY ST_LOCCD,ST_MATCD";				
				}
			}
			///****ADDED BY AAP 05/08/2004 To ACCOMODATE STOCK MASTER LIST
			if(cmbRPTOP.getSelectedIndex() == 7)
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				{
				    prnFMTCHR(dosREPORT,M_strNOCPI17);
				    prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI12);
				}
				if(M_rdbHTML.isSelected())
				{
				    dosREPORT.writeBytes("<HTML><HEAD><Title>Stock Master List</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_ABCFL,ST_VEDFL,ST_HMLFL,"
					+"ST_XYZFL,ST_MATTP,ST_MINLV,ST_MAXLV,ST_RORLV,ST_RORQT,ST_STKQT,ST_STKIP,"
					+"ST_STKOR,ST_STKIN,ST_YTDGR,ST_YTDIS,ST_YTDMR,ST_YTDSN,CT_ILDTM,CT_ELDTM,CT_PKGQT "
					+"FROM MM_STMST,CO_CTMST WHERE ST_MATCD = CT_MATCD and ct_codtp ='CD' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ifnull(ST_STSFL,'') <> 'X' AND "
					+"ST_MATCD BETWEEN '"+txtFMMAT.getText().trim()+"' AND '"+txtTOMAT.getText().trim()+"'";
			}
			///
			if(cmbRPTOP.getSelectedIndex() == 0)
				prnHEAD0();
			if(cmbRPTOP.getSelectedIndex() == 1)
				prnHEAD1();
			if(cmbRPTOP.getSelectedIndex() == 2)
				prnHEAD2();
			if(cmbRPTOP.getSelectedIndex() == 3)
				prnHEAD3();
			if(cmbRPTOP.getSelectedIndex() == 5)
				prnHEAD5();
			///***ADDED BY APP 07/06/04 for LOCATION WISE INVENTORY
			if(cmbRPTOP.getSelectedIndex() == 6)
				prnHEAD6();//PRINT HEADER
			///***
			///****ADDED BY AAP 05/08/2004 To OCCOMODATE STOCK MASTER LIST
			if(cmbRPTOP.getSelectedIndex() == 7)
				prnHEAD7();
			///
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);		
			java.sql.Date L_sdtTEMP;			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(cmbRPTOP.getSelectedIndex() == 0)
					{
						L_sdtTEMP = M_rstRSSET.getDate("ST_STODT");
						if(L_sdtTEMP != null)
							strSTODT = M_fmtLCDAT.format(L_sdtTEMP);
						
						if(cl_dat.M_intLINNO_pbst >60)
						{
							dosREPORT.writeBytes("\n");							
							dosREPORT.writeBytes(strDOTLN);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEAD0();
						}
						
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"0"),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),"0"),11));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"),12));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0"),11));
						if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0")) > 0)
						{
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),10));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_INDQT"),""),9));
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('L',"",10));
							dosREPORT.writeBytes(padSTRING('L',"",9));
						}
						if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0")) > 0)
						{
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),9));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_PORQT"),""),9));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),""),16));
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('L',"",9));
							dosREPORT.writeBytes(padSTRING('L',"",9));
							dosREPORT.writeBytes(padSTRING('L',"",16));
						}
						dosREPORT.writeBytes(padSTRING('L',"",8));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),"0"),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORQT"),"0"),11));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),"0"),12));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0"),11));
						dosREPORT.writeBytes(padSTRING('L',"",41));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDIS"),"0"),12));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
					if(cmbRPTOP.getSelectedIndex() == 1)
					{
						if(M_rstRSSET.getDate("IN_EXPDT") != null)
							strEXPDT = M_fmtLCDAT.format(M_rstRSSET.getDate("IN_EXPDT"));
						else
							strEXPDT = "";
						
						if(M_rstRSSET.getDate("IN_INDDT") != null)
							strINDDT = M_fmtLCDAT.format(M_rstRSSET.getDate("IN_INDDT"));
						else
							strINDDT = "";
						
						if(M_rstRSSET.getDate("IN_REQDT") != null)
							strREQDT = M_fmtLCDAT.format(M_rstRSSET.getDate("IN_REQDT"));
						else
							strREQDT = "";
						
						if(M_rstRSSET.getDate("ST_STODT") != null)
							strSTODT = M_fmtLCDAT.format(M_rstRSSET.getDate("ST_STODT"));
						else
							strSTODT = "";
						
						if(cl_dat.M_intLINNO_pbst >60)
						{
							dosREPORT.writeBytes("\n");							
							dosREPORT.writeBytes(strDOTLN);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEAD1();
						}
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),22));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('L',strSTODT,29));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0"),11));
						if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0")) > 0)
						{
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),11));
							dosREPORT.writeBytes(padSTRING('L',strREQDT,11));
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('L',"",11));
							dosREPORT.writeBytes(padSTRING('L',"",11));
						}
						if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0")) > 0)
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),11));
						else
							dosREPORT.writeBytes(padSTRING('L',"",11));
						
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),54));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0"),11));
						if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0")) > 0)
						{
							dosREPORT.writeBytes(padSTRING('L',strINDDT,11));
							dosREPORT.writeBytes(padSTRING('L',strEXPDT,11));
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('L',"",11));
							dosREPORT.writeBytes(padSTRING('L',"",11));
						}
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),"0"),11));
						dosREPORT.writeBytes("\n");
						
						cl_dat.M_intLINNO_pbst += 3;
					}
					if(cmbRPTOP.getSelectedIndex() == 2)
					{
						if(cl_dat.M_intLINNO_pbst >60)
						{
							dosREPORT.writeBytes("\n");							
							dosREPORT.writeBytes(strDOTLN);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEAD2();
						}
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),22));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"0"),13));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"),14));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0"),14));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDIS"),"0"),14));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_PPOVL"),"0"),16));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),55));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0"),11));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
					if(cmbRPTOP.getSelectedIndex() == 3)
					{
						if(cl_dat.M_intLINNO_pbst >60)
						{
							dosREPORT.writeBytes("\n");							
							dosREPORT.writeBytes(strDOTLN);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEAD3();
						}
					
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),60));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),9));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0.0"),13));
						cl_dat.M_intLINNO_pbst += 1;
						intRECCT++;
					}
					if(cmbRPTOP.getSelectedIndex() == 5)
					{
						L_sdtTEMP = M_rstRSSET.getDate("ST_STODT");
						if(L_sdtTEMP != null)
							strSTODT = M_fmtLCDAT.format(L_sdtTEMP);
					
						if(cl_dat.M_intLINNO_pbst >60)
						{
							dosREPORT.writeBytes("\n");							
							dosREPORT.writeBytes(strDOTLN);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEAD5();
						}
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"0"),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),"0"),11));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"),12));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0"),11));
						if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0")) > 0)
						{
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),10));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_INDQT"),""),9));
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('L',"",10));
							dosREPORT.writeBytes(padSTRING('L',"",9));
						}
						if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0")) > 0)
						{
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),9));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_PORQT"),""),9));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),""),16));
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('L',"",9));
							dosREPORT.writeBytes(padSTRING('L',"",9));
							dosREPORT.writeBytes(padSTRING('L',"",16));
						}
						dosREPORT.writeBytes(padSTRING('L',"",8));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),30));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),"0"),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORQT"),"0"),11));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),"0"),12));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0"),11));
						dosREPORT.writeBytes(padSTRING('L',"",41));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDIS"),"0"),12));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 3;
					}
					///***ADDED BY APP 07/06/04 for LOCATION WISE INVENTORY
					else if (cmbRPTOP.getSelectedIndex() == 6)
					{
						
                        if(rdoSUMRY.isSelected())
                        {
                            dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODCD"),9));
    						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODDS"),40));
    						dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("L_ITMCT"),15));
    						dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("L_ITMVL"),15));
    						dosREPORT.writeBytes("\n");
                        }
                        else
                        {
                            if(cl_dat.M_intLINNO_pbst > 55)
    						{//START NEW PAGE
    							dosREPORT.writeBytes("\n");							
    							dosREPORT.writeBytes(strDOTLN);
    							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
    								prnFMTCHR(dosREPORT,M_strEJT);
    							if(M_rdbHTML.isSelected())
    								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
    							prnHEAD6();
    						}
    						L_strLOCCD = M_rstRSSET.getString("ST_LOCCD");
    						if(!L_strLOCCD.substring(0,2).equals(L_strPRLOC))
    						{
    						  if(intRECCT >0)
    						  {
    						        //dosREPORT.writeBytes("\n");							
    							    dosREPORT.writeBytes(strDOTLN);
    							//    dosREPORT.writeBytes("\n");							
    						         if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
                                    	prnFMTCHR(dosREPORT,M_strBOLD);
                                   	if(M_rdbHTML.isSelected())
                                   		dosREPORT.writeBytes("<B>");	
                    		    	dosREPORT.writeBytes("\nTotal Items / Value "+padSTRING('L',String.valueOf(L_intITMCT),64)+padSTRING('L',setNumberFormat(L_dblSTKVL,2),12));
                    		    	if(M_rdbHTML.isSelected())
                                   		dosREPORT.writeBytes("</B>");	
    						        //START NEW PAGE
        							dosREPORT.writeBytes("\n");							
        							dosREPORT.writeBytes(strDOTLN);
        							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
        								prnFMTCHR(dosREPORT,M_strEJT);
        							if(M_rdbHTML.isSelected())
        								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
            						prnHEAD6();
    						  }
    						   L_dblSTKVL =0;
    						   L_intITMCT =0;
    						   L_strPRLOC =L_strLOCCD.substring(0,2); 
    						   if(hstLOCPR.containsKey((String)L_strPRLOC))
    						   {
    						    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
                    				prnFMTCHR(dosREPORT,M_strBOLD);
                    			if(M_rdbHTML.isSelected())
                       				dosREPORT.writeBytes("<B>");	
    					        dosREPORT.writeBytes(hstLOCPR.get(L_strPRLOC).toString());
                      			if(M_rdbHTML.isSelected())
                    				dosREPORT.writeBytes("</B>");	
    
    						   }
    						   dosREPORT.writeBytes("\n\n");
    						   cl_dat.M_intLINNO_pbst += 2;
    						}
    						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_LOCCD"),9));
    						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_MATCD"),11));
    						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_MATDS"),50));
    						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_UOMCD"),4));
    						dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("ST_STKQT"),10));
                            dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("STP_YCSVL"),12));
    						L_intITMCT++;
    						L_dblSTKVL += M_rstRSSET.getDouble("STP_YCSVL");
    						dosREPORT.writeBytes("\n");
    						cl_dat.M_intLINNO_pbst += 1;
    						intRECCT++;
                        }
					}
					///*****ADDED BY AAP 05/08/2004 To OCCOMODATE STOCK MASTER LIST
					if(cmbRPTOP.getSelectedIndex() == 7)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),11));	//Material Code
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),50));	//Material Description
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDIS"),""),10));	//Year To Date ISS
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),""),12));	//Minimum Level
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),13));	//Quantity On Hand
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));	//Unit Of Measurment Code
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),10));	//Location Code
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_ABCFL"),""),5));	//ABC Flag
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_VEDFL"),""),6));	//VED Flag
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),""),6));	//HML Flag
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_XYZFL"),""),6));	//XYZ Flag
						dosREPORT.writeBytes(padSTRING('L',"",1));
						//Material Type
						strMATTP = nvlSTRVL(M_rstRSSET.getString("ST_MATTP"),"");
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY0("SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXMTP' AND CMT_CODCD = '"+strMATTP+"' AND CMT_STSFL <> 'X'");
						if(L_rstRSSET.next())
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""),19));  //Material Type
						L_rstRSSET.close();

						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDGR"),""),14));	//Year To Date For Receipt
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),""),12));	//Maximum Level
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),""),13));	//Stock On Inspection
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('L',"",45));	//Blank
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0")+" / "+nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0"),12));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDMR"),""),14));	//Year To Date MR
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),""),12));	//Reorder Level
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),""),13));	//Quantity On Order
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",45));	//Blank
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),""),12));  // standare packing
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDSN"),""),14));	//Year To Date SN
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORQT"),""),12));	//Reorder Quantity
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),""),13));	//Quantity On Indent
						cl_dat.M_intLINNO_pbst += 5;
					}
				    }// end sel index 6
				//prnFMTCHR(dosREPORT,M_strNOCPI17);
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n");
			if(cmbRPTOP.getSelectedIndex() == 0|| cmbRPTOP.getSelectedIndex() == 5)				
				dosREPORT.writeBytes(strDOTLN);
			if(cmbRPTOP.getSelectedIndex() == 1 || cmbRPTOP.getSelectedIndex() == 2 || cmbRPTOP.getSelectedIndex() == 3 || cmbRPTOP.getSelectedIndex() == 6 || cmbRPTOP.getSelectedIndex() == 7)				
				dosREPORT.writeBytes(strDOTLN);
			if(cmbRPTOP.getSelectedIndex() == 3)
			{
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("Number of Items : "+intRECCT);
			}
			if(cmbRPTOP.getSelectedIndex() == 6)
			{
		    	 if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
                	prnFMTCHR(dosREPORT,M_strBOLD);
               	if(M_rdbHTML.isSelected())
               		dosREPORT.writeBytes("<B>");	
		    	if(rdoDETAIL.isSelected())
		    	dosREPORT.writeBytes("\nTotal Items / Value "+padSTRING('L',String.valueOf(L_intITMCT),64)+padSTRING('L',setNumberFormat(L_dblSTKVL,2),12));
		    	if(M_rdbHTML.isSelected())
               		dosREPORT.writeBytes("</B>");	
			}
			
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}	
	/**
	 * Method to generate the report for Weekly Stock Status Of Packing Materials
	 */
	private void prnREPORT4()
	{
		try
		{
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Receipt Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			prnHEAD4();			
			for(int i= 0;i<intROWCT;i++)
			{				
				if(tblWEEST.getValueAt(i,0).toString() == "true")
				{
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n");						
						dosREPORT.writeBytes(strDOTLN);
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEAD4();
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',tblWEEST.getValueAt(i,1).toString(),17));
					dosREPORT.writeBytes(padSTRING('R',tblWEEST.getValueAt(i,3).toString(),3));
					dosREPORT.writeBytes(padSTRING('L',tblWEEST.getValueAt(i,4).toString(),18));
					dosREPORT.writeBytes(padSTRING('L',tblWEEST.getValueAt(i,5).toString(),17));
					dosREPORT.writeBytes(padSTRING('L',tblWEEST.getValueAt(i,6).toString(),13));
					dosREPORT.writeBytes(padSTRING('L',tblWEEST.getValueAt(i,7).toString(),11));
					dosREPORT.writeBytes(padSTRING('L',"",2));
					dosREPORT.writeBytes(padSTRING('R',tblWEEST.getValueAt(i,8).toString(),14));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',tblWEEST.getValueAt(i,2).toString(),60));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 3;
				}
			}			
			dosREPORT.writeBytes(strDOTLN);
			dosREPORT.close();
			fosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnREPORT4");
		}
	}
	/*
	* Method to generate the Header of the report for Materials Below Minimum Level.
	*/
	private void prnHEAD0()
	{
		try
		{	cl_dat.M_PAGENO++;
			strDOTLN = "--------------------------------------------------------------------------------------------------------------------------------";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes("\n"+"SUPREME PETROCHEM LIMITED."+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Statement Of Material Below Minimum Level",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Mat. Code.  UOM  Location     Min. Level Reord Lvl. Stk On Hand Stk.On Ind   Ind No. Ind Qty. P.O. No. P.O. Qty  Last P.O. Rate  "+"\n");		
			dosREPORT.writeBytes("Description                   Max. Level Reord Qty. Stk In Insp Stk.On Ord                                          Upto Dt Iss "+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEAD0");
		}
	}
	/**
	 * Method to generrate the header for the ReportItems With NIL Stock
	 */	
	private void prnHEAD1()
	{
		try
		{
			cl_dat.M_PAGENO++;
			strDOTLN = "----------------------------------------------------------------------------------------------------";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes("\n"+"SUPREME PETROCHEM LIMITED."+"\n");			
			if(chbSTKFL.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Stock controlled Item With NIL Stock",strDOTLN.length()-21));
			else
				dosREPORT.writeBytes(padSTRING('R',"Items With NIL Stock",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n\n");
			if(chbSTKFL.isSelected())
				dosREPORT.writeBytes("Stock Controlled item as per the list given below are out of stock. You are requested to expedite"+"\n");
			else
				dosREPORT.writeBytes("Items as per the list given below are out of stock.You are requested to expedite"+"\n");			
			dosREPORT.writeBytes("the supplies."+"\n\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Material Code         UOM              Stock Out Since   Ind.Qty.    Ind.No.  Comp. Dt.  P. O. No."+"\n");			
			dosREPORT.writeBytes("Description                                              Ord.Qty.    Ind.Dt.   Exp. Dt.  Qt.On.Insp."+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst = 12;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEAD1");
		}
	}
	/**
	 * Method to generate the header for the Probable Stock Out List Report.
	 */
	private void prnHEAD2()
	{
		try
		{
			cl_dat.M_PAGENO++;
			strDOTLN = "------------------------------------------------------------------------------------------------";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes("\n"+"SUPREME PETROCHEM LIMITED."+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Probable Stock Out List ",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");		
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Mat. Cd.               UOM    Min.Level   Stk.On.Hand   Qty.On Ind.   Year To Date     Last P.O."+"\n");			
			dosREPORT.writeBytes("Description                                             Qty.On Ord.   Consumption      Rate"+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst = 8;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEAD2");
		}
	}
	/**
	 * Method to generate the header for Stock Statement Report.	
	 */
	private void prnHEAD3()
	{
		try
		{
			cl_dat.M_PAGENO++;
			strDOTLN = "------------------------------------------------------------------------------------------------";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes("\n"+"SUPREME PETROCHEM LIMITED."+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stock Statement As On Date "+cl_dat.M_strLOGDT_pbst,strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Material Code And Description                                         UOM  Location  Stk.On Hand"+"\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEAD3");
		}
	}
	/**
	 * Method to generate the header for Weekly Stock Status Of Packing Materials Report.
	 */
	private void prnHEAD4()
	{
		try
		{
			cl_dat.M_PAGENO++;
			strDOTLN ="------------------------------------------------------------------------------------------------";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes("\n"+"SUPREME PETROCHEM LIMITED."+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Weekly Stock Status Of Packing Material",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Mat. Code        UOM       Stock On           Stock On        Daily    Will Last       Remarks"+"\n");			
			dosREPORT.writeBytes("Description                   Hand           Inspection  Consumption    For Days      "+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEAD4");
		}
	}
	/**
	 * Method to generate the header for the report	Materials Below Re-order Level.
	 */	
	private void prnHEAD5()
	{
		try
		{
			cl_dat.M_PAGENO++;
			strDOTLN = "-------------------------------------------------------------------------------------------------------------------------------";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes("\n"+"SUPREME PETROCHEM LIMITED."+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Statement Of Material Below Re-Order Level",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Mat. Code.  UOM  Location     Min. Level Reord Lvl. Stk On Hand Stk.On Ind   Ind No. Ind Qty. P.O. No. P.O. Qty  Last P.O. Rate  "+"\n");			
			dosREPORT.writeBytes("Description                   Max. Level Reord Qty. Stk In Insp Stk.On Ord                                          Upto Dt Iss "+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEAD5");
		}
	}
	/**
	 * Method to generate the header for Location wise Inventory Report
	 */	
	private void prnHEAD6()
	{
		try
		{
			cl_dat.M_PAGENO++;
			strDOTLN = "------------------------------------------------------------------------------------------------";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			
			dosREPORT.writeBytes("\n"+"SUPREME PETROCHEM LIMITED."+"\n");			
			if(rdoSUMRY.isSelected())
			    dosREPORT.writeBytes(padSTRING('R',"Location wise Inventory Summary as on : "+cl_dat.M_txtCLKDT_pbst.getText(),76));
			else
		    	dosREPORT.writeBytes(padSTRING('R',"Location wise Inventory as on : "+cl_dat.M_txtCLKDT_pbst.getText(),76));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),76));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");						
			dosREPORT.writeBytes(strDOTLN+"\n");
			dosREPORT.writeBytes("\n");
			if(rdoSUMRY.isSelected())
			{
			   	dosREPORT.writeBytes(padSTRING('R',"Locn",9));
		    	dosREPORT.writeBytes(padSTRING('R',"Description",40));
		        dosREPORT.writeBytes(padSTRING('L',"Total Items",15));
                dosREPORT.writeBytes(padSTRING('L',"Value",15));
			}
			else
			{
			    dosREPORT.writeBytes(padSTRING('R',"Locn",9));
			    dosREPORT.writeBytes(padSTRING('R',"Material Code & Description",11+50));
			    dosREPORT.writeBytes(padSTRING('R',"UOM",4));
			    dosREPORT.writeBytes(padSTRING('L',"Stock Qty",11));
                dosREPORT.writeBytes(padSTRING('L',"Value",11));
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
			//dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst = 6;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEAD6");
		}
	}
	/**
	 * Method to generate the header for the Stock Master List Report.
	 */
	private void prnHEAD7()
	{
		try
		{
			cl_dat.M_PAGENO++;
			strDOTLN = "------------------------------------------------------------------------------------------------";			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("SUPREME PERTOCHEM LIMITED"+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stock Master List",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Material Code And Description                                 YTD Issue   Min.Level  Qty.On Hand"+"\n");			
			dosREPORT.writeBytes("UOM   Location  ABC   VED   HML   XYZ           Mat. Type     YTD Recpt   Max.Level  Qty.On Insp"+"\n");			
			dosREPORT.writeBytes("                                            Lead Time I/E       YTD MRN         ROL  Qty.On Ord."+"\n");			
			dosREPORT.writeBytes("                                                 Std.Pkg.       YTD SAN         ROQ  Qty.On Ind."+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEAD7");
		}
	}
	
	/**
	 * Method to valiate the Inputs before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		cl_dat.M_PAGENO = 0;		
		if(M_rdbHTML.isSelected())
			strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpstk.html";
		else
			strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpstk.doc";
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{
			if(M_cmbDESTN.getSelectedIndex() == 0)
			{
				setMSG("Select Printer..",'E');
				M_cmbDESTN.requestFocus();
				return false;
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst) && M_cmbDESTN.getItemCount() == 0)
		{
			setMSG("Please Enter Reciepents..",'E');
			M_cmbDESTN.requestFocus();
			return false;
		}
		if(cmbRPTOP.getSelectedIndex() == 0 || cmbRPTOP.getSelectedIndex() == 3 || cmbRPTOP.getSelectedIndex() == 5)
		{
			if(txtFMGRP.getText().toString().length() == 0)
			{
				setMSG("Please Enter Group Type Or Press F1 For Help..",'E');
				txtFMGRP.requestFocus();
				return false;
			}
			if(txtTOGRP.getText().toString().length() == 0)
			{
				setMSG("Please Enter Group Type Or Press F1 For Help..",'E');
				txtTOGRP.requestFocus();
				return false;
			}
			if(Integer.parseInt(txtTOGRP.getText().toString()) < Integer.parseInt(txtFMGRP.getText().toString()))
			{
				setMSG("To Group Should Be Greater Than Or Equal To From Group ",'E');
				txtTOGRP.requestFocus();
				return false;
			}
		}
		if(cmbRPTOP.getSelectedIndex() == 1 || cmbRPTOP.getSelectedIndex() == 2)
		{
			if(txtFMSUB.getText().toString().length() == 0)
			{
				setMSG("Please Enter Sub Sub Group Type Or Press F1 For Help..",'E');
				txtFMSUB.requestFocus();
				return false;
			}
			if(txtTOSUB.getText().toString().length() == 0)
			{
				setMSG("Please Enter Sub Sub Group Type Or Press F1 For Help..",'E');
				txtTOSUB.requestFocus();
				return false;
			}
			if(Integer.parseInt(txtTOSUB.getText().toString()) < Integer.parseInt(txtFMSUB.getText().toString()))
			{
				setMSG("To Sub Sub Group Should Be Greater than or equal to From Sub Sub Group ",'E');
				txtTOSUB.requestFocus();
				return false;
			}
		}
		///***ADDED BY APP 07/06/04 for LOCATION WISE INVENTORY
		//USER FRIENDLY MESSAGES
		if(cmbRPTOP.getSelectedIndex() == 6)
		{
			if(rdoDETAIL.isSelected())
			{
    			if(txtFMLOC.getText().trim().length() == 0)
    			{
    				txtFMLOC.requestFocus();
    				setMSG("Enter Location Code Or Press F1 For Help..",'E');
    				return false;
    			}
    			if(txtTOLOC.getText().trim().length() == 0)
    			{
    				txtTOLOC.requestFocus();
    				setMSG("Enter Location Code Or Press F1 For Help..",'E');
    				return false;
    			}
			}
		}
		///***
		///****ADDED BY AAP 05/08/2004 To OCCOMODATE STOCK MASTER LIST
		if(cmbRPTOP.getSelectedIndex() == 7)
		{
			if(txtFMMAT.getText().trim().length() == 0)
			{
				txtFMMAT.requestFocus();
				setMSG("Enter Material Code Or Press F1 For Help..",'E');
				return false;
			}
			if(txtTOMAT.getText().trim().length() == 0)
			{
				txtTOMAT.requestFocus();
				setMSG("Enter Material Code Or Press F1 For Help..",'E');
				return false;
			}
			if(Float.parseFloat(txtTOMAT.getText().trim()) < Float.parseFloat(txtFMMAT.getText().trim()))
			{
				txtTOMAT.requestFocus();
				setMSG("To Materia Code Should Be Greater Than From Material Code..",'E');
				return false;
			}
			else 
				return true;
		}
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount() == 0)
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
		setMSG("",'N');
		return true;
	}
	/**
	 * Method to fetch Weekly Stock Status Of Packing Materials details & to insert it in the JTable.
	 */
	private void getTBLDT()
	{
		try
		{
			M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_STKQT,ST_STKIP "
				+"FROM MM_STMST WHERE substr(ST_MATCD,1,6) in ('690520','691010') AND ifnull(ST_STKQT,0) > 0 AND  ST_STSFL <> 'X' AND "
				+"ST_MMSBS = '"+M_strSBSCD+"' ORDER BY ST_MATCD";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			int i = 0;
			if(M_rstRSSET != null)
				while(M_rstRSSET.next())
				{
					tblWEEST.setValueAt(new Boolean(true),i,0);
					tblWEEST.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),i,1);
					tblWEEST.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),i,2);
					tblWEEST.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),i,3);
					tblWEEST.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),i,4);
					tblWEEST.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),""),i,5);
					intROWCT ++;
					i++;
				}
			setMSG("Enter Daily Consumption and Press Enter..",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTBLDT");
		}
		
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	void exePRINT()
	{
		cl_dat.M_PAGENO = 0;
		try
		{
		setCursor(cl_dat. M_curWTSTS_pbst);
		if(cmbRPTOP.getSelectedIndex() == 4)
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpstk.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpstk.doc";
			prnREPORT4();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Weekly Stock Status Of Packing Materials"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}			
		}
		else
		if(vldDATA())
		{
			getDATA();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {					
					if(cmbRPTOP.getSelectedIndex() == 0)
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Materials Below Minimum Level","");
					else if(cmbRPTOP.getSelectedIndex() == 1)
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Report Items With NIL Stock ","");
					else if(cmbRPTOP.getSelectedIndex() == 2)
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Report Probable Stock Out List ","");
					else if(cmbRPTOP.getSelectedIndex() == 3)					
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Report Stock Statement ","");
					else if(cmbRPTOP.getSelectedIndex() == 5)
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Report Materials Below Re-order Level ","");
					else if(cmbRPTOP.getSelectedIndex() == 6)
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Report Location wise Inventory","");
					else if(cmbRPTOP.getSelectedIndex() == 7)
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Report Stock Master List","");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}			
		}
		setCursor(cl_dat. M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}	
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			if(cl_dat.M_flgHELPFL_pbst)
				return true;
			try
			{
				if(input == txtFMGRP || input == txtTOGRP )
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select count(*) from MM_STMST where ST_MATCD LIKE '"+((JTextField)input).getText()+"%' AND ST_MMSBS = '"+M_strSBSCD+"'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								setMSG("Invalid Material Group ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Material Group ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Material Group ",'E');
						return false;
					}
				}
				else if(input == txtFMSUB || input == txtTOSUB )
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select count(*) from MM_STMST where substr(ST_MATCD,1,6) ='"+((JTextField)input).getText()+"' AND ST_MMSBS = '"+M_strSBSCD+"'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)== 0)
							{
								setMSG("Invalid Material Sub Sub Group ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Material Sub Sub Group ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Material Group ",'E');
						return false;
					}
				}
				if(input == txtFMMAT || input == txtTOMAT)
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select count(*) from CO_CTMST where CT_MATCD = '"+((JTextField)input).getText()+"'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								setMSG("Invalid Material Code ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Material Code ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Material Code ",'E');
						return false;
					}
				}
				///***ADDED BY APP 07/06/04 for LOCATION WISE INVENTORY
				if(input == txtFMLOC || input == txtTOLOC)
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY0("Select count(*) from MM_STMST where ST_LOCCD = '"+((JTextField)input).getText()+"'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								setMSG("Invalid Location Code ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Location Code ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Location Code ",'E');
						return false;
					}
				}				
				return true;
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"verify");
				return false;
			}
		}
	}
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(getSource() == tblWEEST)
				{
					if(P_intCOLID == 6)
					{
						if(((JTextField)tblWEEST.cmpEDITR[6]).getText().length()>0)
						{
							if(Float.parseFloat(((JTextField)tblWEEST.cmpEDITR[6]).getText()) > 0)							
								tblWEEST.setValueAt(setNumberFormat(Float.parseFloat(tblWEEST.getValueAt(P_intROWID,4).toString())/Float.parseFloat(((JTextField)tblWEEST.cmpEDITR[6]).getText()),0),P_intROWID,7);							
						}
					}
				}
				return true;
			}
			catch(Exception L_TBLVR)
			{
				setMSG(L_TBLVR,"TBLVER");
				setMSG("Invalid Data ",'E');
				return false;
			}
		}
	}
}

