/**System Name   : Laboratoty Information Management System
Program Name     : Lot Control Sheet
Program Desc.    : Report for Selected Tests & Selected Quality Tests Details.
Author           : Mr.S.R.Mehesare
Date             : 11/06/2005
Version          : LIMS V2.0.0

Modificaitons 
Modified By      : 
Modified Date    : 
Modified det.    : 
Version          : 
*/

import java.sql.ResultSet;import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
import javax.swing.JRadioButton;import javax.swing.JTable;import javax.swing.ButtonGroup;
	
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Lot Control Sheet

Program Desc.: Report to view the various selected test details for given lots.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_PRMST       PR_PRDCD                                                #
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
QC_PSMST       PS_QCATP,PS_TSTTP,PS_LOTNO,PS_RCLNO,
               PS_TSTNO,PS_TSTDT                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtFMLOT	LT_LOTNO       PR_LTMST      varchar(10)    From Lot Number
txtTOLOT	LT_LOTNO       PR_LTMST      varchar(10)    To Lot Number
txtLOTSPC	LT_LOTNO       PR_LTMST      varchar(10)    Specific lot numbers
tblTSTTP    Quality Test codes and descriptions are displayed in JTable.
tblLINTP    Line no. & Descriptions are displayed in this JTable.
tblQCPRM    JTable for availble Quality parameters.
--------------------------------------------------------------------------------------
<B>
Logic</B>
Each grade (finshed product) has some Quality Parameters. & these Parameters changes
as per new standards adopted. 
These parameter values are varing from grade to grade.
System maintains these parameters of each grade & makes Latest Parameters available.
According to the new standards, some new quality parameters are added and some unwanted
are removed for specific grade.
Hence to generate report every time list of quality parameters are generated dynamically 
 as :- 1) Latest parameters are fetched from the database associated with given grade.
   2) These Properties are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    "PS_"+ Vector.elementAt(i)+"VL" i.e SMT_COLVL, SMT_TOLVL
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

<I><B>Conditions Give in Query:</b>
Line Numbers are taken from CO_CDTRN for given condistion as
     1) CMT_CGMTP ='SYS'
     2) AND CMT_CGSTP = 'PRXXLIN'"
     3) AND CMT_CCSVL = given QCA Type.

Test Types are taken from CO_CDTRN for given condistion as
     1) CMT_CGMTP ='SYS' 
     2) AND CMT_CGSTP = 'QCXXTST' 
     3) AND SUBSTRING(CMT_CODCD,1,2)='01'";

Quality parameters are taken from CO_CDTRN for condiations as 
     1) CMT_CGMTP ='RPT' 
     2) AND CMT_CGSTP = 'QCXXLCS'		

Report Data is taken from PR_LTMST and CO_PRMST for condiation :-
     1) LT_LINNO in(Selected Lines)
     2) AND LT_PRDCD = PR_PRDCD 
     3) AND LT_CLSFL <> '8'"
     4) AND LT_PRDTP = selected grades.					
	<B>For From Lot number</B>
     if To Lot is already given
     5) AND LT_LOTNO < To Lot Number.
     if Some Starting integers for From Lot number is given
     6) AND LT_LOTNO like text from txtFMTOL textbox.
    <B>For To Lot number</B>
     if From Lot is already given
     5) AND LT_LOTNO > From Lot Number.
     if Some Starting integers for From Lot number is given
     6) AND LT_LOTNO like text from txtTOTOL Textfield.
    <B>For Specific Lot number</B>            
         No extra condiation is applied.

     Lot Details are taken from PR_LTMST and CO_PRMST for given Lot Numbers        		  
          1) AND LT_PRDCD = PR_PRDCD
          2) AND LT_PRDTP = Selected Products.
        For Range Selection
          3) LT_LOTNO BETWEEN given Lot Range 
        For Specific lot Numbers 
          4) AND LT_LOTNO in (Selected Lot Numbers)
     
     Different quality parameter Details for different Lots are taken from QC_PSMST 
     LEFT OUTER JOIN QC_RMMST ON PS_QCATP = RM_QCATP AND PS_TSTTP = RM_TSTTP 
     AND PS_TSTNO = RM_TSTNO For Condiations :- 
          1) PS_QCATP = Selected QCA Type here it is "01".
          2) AND PS_TSTTP in (Selected Quality Tests)						
		  3) AND PS_LOTNO = Selected Lot Numbers.

<B>Validations :</B>
    - Lot numbers Entered must be valid.
</I> */

public class qc_rplcs extends cl_rbase
{									/** JTextField to to show & enter FromLot number to specify lot range.*/
	private JTextField txtFMLOT;	/** JTextField to to show & enter ToLot number to specify lot range.*/
	private JTextField txtTOLOT;	/** JTextField to to show & enter specific lot number to specify lot No.*/
	private JTextField txtSPLOT;	/** JRadioButton to specify, report for range selection.*/
	private JRadioButton rdbLTRNG;	/** JRadioButton to specify, report for given Lot Numbers.*/
	private JRadioButton rdbLTSPC;	/** ButtonGroup to group JRedioButton in a group.*/
	private ButtonGroup bgrOPT;		/** JTable to display different Test Type available.*/
	private JTable tblTSTTP;		/** JTable to display Different Production Lines available.*/
	private JTable tblLINTP;		/** JTable to display Different quality parameters available.*/
	private JTable tblQCPRM;		/** JOptionPane to display message, to choose the paper size.*/
	private JOptionPane jopOPTPN;			/**	Vector to store the quality parameter Descrition.*/
	private Vector<String> vtrQPRDS = new Vector<String>();	/**	Vector to store the quality parameter Code.*/
	private Vector<String> vtrQPRLS = new Vector<String>();	/**	Vector to store the quality parameter Short Descrition.*/
	private Vector<String> vtrQPRSH = new Vector<String>();	/** FileOutputStream Object for generated Report File Name.*/
	private FileOutputStream fosREPORT ;	/** DataOutputStream object to hold & append data in the Stream to generate the Report.*/
    private DataOutputStream dosREPORT ;	/** String variable for generated Report File Name.*/
	private String strFILNM;				/** String variable for QCA Type.*/
	private String strQCATP = "";			/** String variable for Product Type.*/
	private String strPRDTP = "";			/** String variable for Lot Number.*/
	private String strLOTNO;				/** String variable for List of different Quality Tests.*/
	private String strTESTS;				/** String variable for Reclassification Number.*/	
	private String strRCLNO;				/** String variable for Product Description.*/
	private String strPRDDS;				/** String variable for quality Test Number. */
	private String strTSTNO;				/** String variable for Sotted Line.*/
	private StringBuffer stbDOTLN;
										/** String variable for.*/
	final String strINTRCL_fn ="00";	/** Integer variable to specify the default number of Rows in the JTable*/
	private int intROWCT = 10;			/** Integer variable for total lines selected for the Report.*/
	private int intRWCTL;				/** Array of Integers for Column Width. */	
	private int arrCOLWD[];				/** Array of Integers for number of integers after decimal Point.*/
	private int arrDECCT[];				/** Integer variable to count the width of the Report before RPS Column.*/
	private int intRPSWD;				/** Integer variable to count the width of the Report before GL Column.*/
	private int intGLWID;				/** Integer variable to count QC Proparty Count.*/
	private int intQCPCT;				/** Boolean varialbe to manage the dynamic generation of header only once .*/	
	private boolean flgFIRST = true;
									/**	String variable for ISO Document Number.*/	
	private String strISOD1;		/** String variable for ISO Document Number.*/
	private String strISOD2;		/** String variable for ISO Document Number.*/
	private String strISOD3;		/** Array of string for RPS & GL Sub Type.*/
	private String arrCHP02[];		/** Integer string for Line Count.*/
	private int intLINCT;			/** Integer string for Row Length.*/
	private int intROWLN;
	
	private final int TB1_CHKFL = 0;
	private final int TB1_LINNO = 1;
	private final int TB1_LINDS = 2;
		
	private final int TB2_CHKFL = 0;
	private final int TB2_TSTTP = 1;
	private final int TB2_TSTDS = 2;
			
	private final int TB3_CHKFL = 0;
	private final int TB3_QPRCD = 1;
	private final int TB3_QPRDS = 2;
	private final int TB3_UOMDS = 3;
	private final int TB3_MAXVL = 4;
	qc_rplcs()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
						
			add(new JLabel("Lines"),3,2,1,1,this,'R');
			add(new JLabel("Test Types"),3,5,1,1,this,'R');
			String[] L_COLHD = {"Select","Line NO.","Description"};
      		int[] L_COLSZ = {50,60,135};	    				
			tblLINTP = crtTBLPNL1(this,L_COLHD,intROWCT,4,2,5.2,2.8,L_COLSZ,new int[]{0});
			
			String[] L_COLHD1 = {"Select","Type","Description"};      		
			tblTSTTP = crtTBLPNL1(this,L_COLHD1,intROWCT,4,5,5.2,2.8,L_COLSZ,new int[]{0});
			
			rdbLTSPC = new JRadioButton ("Specific Lot");
			rdbLTRNG = new JRadioButton ("Lot Range",true);
			bgrOPT = new ButtonGroup();
			bgrOPT.add(rdbLTSPC);
			bgrOPT.add(rdbLTRNG);
			add(rdbLTSPC,10,3,1,1,this,'L');			
			add(rdbLTRNG,10,5,1,1,this,'L');
			
		    add(new JLabel("From Lot No."),11,2,1,.9,this,'R');
			add(txtFMLOT = new TxtNumLimit(8),11,3,1,1.5,this,'L');
			add(new JLabel("To"),11,4,1,.3,this,'R');
			add(txtTOLOT = new TxtNumLimit(8),11,5,1,1.5,this,'L');
						
			add(new JLabel("Enter Lot Nos."),12,2,1,.9,this,'R');
			add(txtSPLOT = new TxtLimit(50),12,3,1,4.7,this,'L');
			
			String[] L_COLHD2 = {"Select","Param Code","Description","Unit",""};
      		int[] L_COLSZ1 = {50,95,125,92,50};	    				
			tblQCPRM = crtTBLPNL1(this,L_COLHD2,50,13,3,6,4.5,L_COLSZ1,new int[]{0});
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			stbDOTLN = new StringBuffer("--");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}		
	}
	/**
	 * Super class method override to inhance its funcationality, to enable & disable 
	 * components according to Requriement.
	 * @param L_flgSTAT boolean argument to pass state of component.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		if(L_flgSTAT == false)		
			clrCOMP();
		else
		{
			if(rdbLTSPC.isSelected())
			{
				txtFMLOT.setText("");
				txtTOLOT.setText("");
				txtFMLOT.setEnabled(false);
				txtTOLOT.setEnabled(false);
				txtSPLOT.setEnabled(true);	
				txtSPLOT.requestFocus();
			}
			if(rdbLTRNG.isSelected())
			{
				txtFMLOT.setEnabled(true);
				txtTOLOT.setEnabled(true);
				txtSPLOT.setText("");
				txtSPLOT.setEnabled(false);	
				txtFMLOT.requestFocus();
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{				
				int L_intROWCT = 0;
				if(flgFIRST)
				{				
					strQCATP = M_strSBSCD.substring(2,4);
					strPRDTP = strQCATP;
					setMSG("Enter the Lot number Or press F1 to select from list..",'N');					
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					try
					{
						M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,2)L_LINNO,CMT_CODDS from CO_CDTRN"
							+" where CMT_CGMTP ='SYS'and CMT_CGSTP = 'PRXXLIN'"
							+" AND CMT_CCSVL = '"+strQCATP+"'";												
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						L_intROWCT = 0;					
						if (M_rstRSSET != null)
						{
							while (M_rstRSSET.next())
							{
								tblLINTP.setValueAt(new Boolean(true),L_intROWCT,TB1_CHKFL);
								tblLINTP.setValueAt(M_rstRSSET.getString("L_LINNO"),L_intROWCT,TB1_LINNO);
								tblLINTP.setValueAt(M_rstRSSET.getString("CMT_CODDS"),L_intROWCT,TB1_LINDS);
								L_intROWCT += 1;
							}												
							M_rstRSSET.close();
						}	
						intRWCTL = L_intROWCT;
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN"
							+ " where CMT_CGMTP ='SYS' AND CMT_CGSTP = 'QCXXTST' AND SUBSTRING(CMT_CODCD,1,2)='01'";										
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						L_intROWCT = 0;	
						intQCPCT = 0;
						if(M_rstRSSET != null)
						{						
							while (M_rstRSSET.next())
							{														
								tblTSTTP.setValueAt(new Boolean(true),L_intROWCT,TB2_CHKFL);
								tblTSTTP.setValueAt(M_rstRSSET.getString("CMT_CODCD").trim().substring(0,4),L_intROWCT,TB2_TSTTP);
								tblTSTTP.setValueAt(M_rstRSSET.getString("CMT_CODDS").trim(),L_intROWCT,TB2_TSTDS);
								L_intROWCT += 1;
								intQCPCT++;
							}										
							M_rstRSSET.close();
						}					
						getTSTDET();
					}
					catch(Exception L_SE)
					{
						setMSG(L_SE,"Data fetching for JTables");
					}		
					this.setCursor(cl_dat.M_curDFSTS_pbst);																				
					setENBL(true);								
					flgFIRST = false;
				}
			}
			else
			{
				flgFIRST = true;
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;		
			vtrQPRLS.removeAllElements();
			vtrQPRDS.removeAllElements();
			vtrQPRSH.removeAllElements();
			stbDOTLN.delete(0,stbDOTLN.toString().length());			
		}		
		else if(M_objSOURC == txtFMLOT)
		{
			setMSG("Enter Lot number OR Prass F1 key to Select From List..",'N');
			txtTOLOT.requestFocus();
		}		
		else if(M_objSOURC == rdbLTSPC)
		{
			txtFMLOT.setText("");
			txtTOLOT.setText("");
			txtFMLOT.setEnabled(false);
			txtTOLOT.setEnabled(false);
			txtSPLOT.setEnabled(true);	
			txtSPLOT.requestFocus();
		}
		else if(M_objSOURC == rdbLTRNG)
		{			
			txtFMLOT.setEnabled(true);
			txtTOLOT.setEnabled(true);
			txtSPLOT.setText("");
			txtSPLOT.setEnabled(false);	
			txtFMLOT.requestFocus();
		}
		else if(M_objSOURC == txtTOLOT)
			cl_dat.M_btnSAVE_pbst.requestFocus();				
		else if(M_objSOURC == txtSPLOT)
			cl_dat.M_btnSAVE_pbst.requestFocus();		
	}	
	public void keyPressed(KeyEvent L_KE)
	{	    		
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {		
			try
    	  	{	
				if((M_objSOURC == txtFMLOT) ||(M_objSOURC == txtTOLOT) || (M_objSOURC == txtSPLOT))
				{
					M_strSQLQRY="";
					int L_intLINCT=0;
					int j=0;
					StringBuffer L_stbLINES = new StringBuffer();
					for(int i=0;i<intROWCT;i++)
					{
						if(tblLINTP.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
						{
							if (j>0)					
								L_stbLINES.append(",");							
					  		L_stbLINES.append("'"+tblLINTP.getValueAt(i,TB1_LINNO).toString().trim()+"'");														
							L_intLINCT++;					  		
							j++;
						}
					}
					if(L_intLINCT == 1)
					{												
						M_strSQLQRY = "Select LT_LOTNO,PR_PRDDS,LT_PSTDT FROM PR_LTMST,CO_PRMST"
						+" where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LINNO =" + L_stbLINES.toString()
						+" AND LT_PRDCD = PR_PRDCD AND LT_CLSFL <> '8'"
						+" AND LT_PRDTP = '" + strPRDTP + "' ";						
					}
					else if(L_intLINCT == intRWCTL) //total row in lineTP table == selected Lines 
					{
						M_strSQLQRY = "Select LT_LOTNO,PR_PRDDS,LT_PSTDT FROM PR_LTMST,CO_PRMST"
						+" where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDCD = PR_PRDCD and LT_CLSFL <> '8'"
						+" and LT_PRDTP = '" + strPRDTP + "'";						
					}				
					else // if more than 1 lines & less than all Lines are selected.
					{
						M_strSQLQRY = "Select LT_LOTNO,PR_PRDDS,LT_PSTDT FROM PR_LTMST,CO_PRMST"
						+" where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LINNO in(" + L_stbLINES.toString() + ")"
						+" AND LT_PRDCD = PR_PRDCD and LT_CLSFL <> '8'"
						+" and LT_PRDTP = '" + strPRDTP + "'";						
					}				
				}
				if(M_objSOURC == txtFMLOT)
				{	
					M_strHLPFLD = "txtFMLOT";										
 					setCursor(cl_dat.M_curWTSTS_pbst);					
					if(txtTOLOT.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_LOTNO < '"+ txtTOLOT.getText().trim()+"'";						
					if(txtFMLOT.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_LOTNO like '"+ txtFMLOT.getText().trim()+"%'";					
					M_strSQLQRY += " order by LT_LOTNO";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No.","Description","Start Date"},3,"CT"); 										
    			}   
				if(M_objSOURC == txtTOLOT)
				{	
					M_strHLPFLD = "txtTOLOT";					
 					setCursor(cl_dat.M_curWTSTS_pbst);					
					if(txtFMLOT.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_LOTNO > '"+ txtFMLOT.getText().trim()+"'";	
					if(txtTOLOT.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_LOTNO like '"+ txtTOLOT.getText().trim()+"%'";					
					M_strSQLQRY += " order by LT_LOTNO";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No.","Description","Start Date"},3,"CT"); 										
    			}
				if(M_objSOURC == txtSPLOT)
				{	
					M_strHLPFLD = "txtSPLOT";					
					StringBuffer L_stbLINES = new StringBuffer();
 					setCursor(cl_dat.M_curWTSTS_pbst);					
					M_strSQLQRY += " order by LT_LOTNO";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No.","Description","Start Date"},3,"CT"); 										
    			}   
			}
			catch(Exception L_EX)
    		{
    		    setMSG(L_EX ," F1 help..");    		    
				setCursor(cl_dat.M_curDFSTS_pbst);
    		}
    		setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	Method for execution of help for Lot number.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMLOT")
		{
			txtFMLOT.setText(cl_dat.M_strHLPSTR_pbst);							
			if (cl_dat.M_strHLPSTR_pbst.length() !=0)			
			    txtTOLOT.requestFocus();
			setMSG("Enter Lot Number OR Press F1 to Select Fom list..",'N');
		}
		if(M_strHLPFLD == "txtTOLOT")
		{
			txtTOLOT.setText(cl_dat.M_strHLPSTR_pbst);							
			if (cl_dat.M_strHLPSTR_pbst.length() !=0)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		if(M_strHLPFLD == "txtSPLOT")
		{
			if(txtSPLOT.getText().trim().length()==0)
				txtSPLOT.setText(cl_dat.M_strHLPSTR_pbst);
			else
				txtSPLOT.setText(txtSPLOT.getText().trim()+","+cl_dat.M_strHLPSTR_pbst);										
		}
		setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		cl_dat.M_flgLCUPD_pbst = true;
		if(!vldDATA())
			return;					
		try
		{
			if(M_rdbHTML.isSelected())
			{
			    strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rplcs.html";
				intLINCT = 80;
			}
			else if(M_rdbTEXT.isSelected())
			{
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rplcs.doc";							
				intLINCT = 65;
			}
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
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Lot Control Sheet"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}		
	}
	/**
    *Method to fetch data from the data base & club it with Header &
    *footer in DataOutputStream.
	*/
	private void getDATA()
	{ 									
		String L_strLOTNO="",L_strPSTDT="",L_strTSTTP="",L_strREMDS;
		java.sql.Timestamp L_tmsTSTDT;
		String L_intTEMP = "0";	
		StringBuffer L_stbLOTLS = new StringBuffer();	
		StringBuffer L_stbTESTS = new StringBuffer();
		int L_intLOTCT = 0,L_intQCPCT = 0;
		String L_strTMPDT = "",L_strPENDT ="",L_strFSTDT = "",L_strFPEDT = "";
		String L_strCLSFL ="", L_strQPFLD ="";
		strPRDDS ="";		
		ResultSet L_rstRSSET;				
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
				prnFMTCHR(dosREPORT,M_strCPI17);				
			}
			if(M_rdbHTML.isSelected())
			{
				setMSG("For HTML Report Printing Please insert 120 column Page..",'N');
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Lot Control Sheet </title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}	
			prnHEADER();
			
			if(rdbLTSPC.isSelected() == true)
			{	//List of lots for "in" condition			
				StringTokenizer st = new StringTokenizer(txtSPLOT.getText().trim(),",");					
				L_intLOTCT = 0;
				while(st.hasMoreTokens())
				{
					if(L_intLOTCT==0)
						L_stbLOTLS.append("'"+st.nextToken()+"'");
					else
						L_stbLOTLS.append(",'"+st.nextToken()+"'");
					L_intLOTCT++;
				}				
			}				
			int j = 0;
			for(int i=0;i<intROWCT;i++)//List of tests for "in" condition
			{ 
				if(tblTSTTP.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					if(j != 0)
						L_stbTESTS.append(",");
					L_stbTESTS.append("'"+tblTSTTP.getValueAt(i,TB2_TSTTP).toString().trim()+"'");
					L_intQCPCT++;
					j++;
				}
			}						
			strTESTS = L_stbTESTS.toString();
			if(rdbLTRNG.isSelected())
			{				
				M_strSQLQRY = " SELECT LT_LOTNO,LT_LINNO,PR_PRDDS,LT_PSTDT,LT_PENDT,LT_PRDCD,LT_CLSFL"
				+" FROM PR_LTMST,CO_PRMST WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO BETWEEN '" + txtFMLOT.getText().trim() 
				+ "' AND '" + txtTOLOT.getText().trim()+ "'and LT_PRDCD = PR_PRDCD"
				+ " AND LT_PRDTP = '" + strPRDTP.trim() + "' order by LT_LINNO,LT_LOTNO";
			}
			else
			{				
				M_strSQLQRY = " SELECT LT_LOTNO,LT_LINNO,PR_PRDDS,LT_PSTDT,LT_PENDT,LT_PRDCD,LT_CLSFL"
				+" FROM PR_LTMST,CO_PRMST WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDCD = PR_PRDCD";
				if(L_intLOTCT == 1)		 
					M_strSQLQRY += " AND LT_LOTNO = " + L_stbLOTLS.toString();
				else
					M_strSQLQRY += " AND LT_LOTNO in (" + L_stbLOTLS.toString() +")";				
				M_strSQLQRY +=" AND LT_PRDTP = '" + strPRDTP.trim() + "' order by LT_LINNO,LT_LOTNO";
			}												
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
				{									
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst ++;										
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("LT_LOTNO"),"-");					
					L_tmsTSTDT =M_rstRSSET.getTimestamp("LT_PSTDT");
					if(L_tmsTSTDT != null)
						L_strPSTDT = M_fmtLCDTM.format(L_tmsTSTDT);					
					
					L_tmsTSTDT =M_rstRSSET.getTimestamp("LT_PENDT");
					if(L_tmsTSTDT != null)
						L_strPENDT = M_fmtLCDTM.format(L_tmsTSTDT);															
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");						
					dosREPORT.writeBytes(padSTRING('R',L_strLOTNO,11));					
					if(!L_strPSTDT.equals(""))
					{
						if(L_strPSTDT.length()>10)
							L_strFSTDT = L_strPSTDT.substring(0,10);
						else
							L_strFSTDT = "*";
						if(L_strPSTDT.length()>=16)
							L_strFSTDT = L_strFSTDT + "-" +  L_strPSTDT.substring(11,16);
						else
							L_strFSTDT = L_strFSTDT + "-" +  "*";
						dosREPORT.writeBytes(padSTRING('R',L_strFSTDT,20));						
					}	
					else
						dosREPORT.writeBytes(padSTRING('R',"-",20));										
					if(!L_strPENDT.equals(""))
					{
						if(L_strPENDT.length()>10)
							L_strFPEDT = L_strPENDT.substring(0,10);
						else
							L_strFPEDT = "*";
						if(L_strPENDT.length()>=16)
							L_strFPEDT = L_strFPEDT + "-" +  L_strPENDT.substring(11,16);
						else
							L_strFPEDT = L_strFPEDT + "-" +  "*";
						dosREPORT.writeBytes(padSTRING('R',L_strFPEDT,20));
					}	
					else
						dosREPORT.writeBytes(padSTRING('R',"-",20));							
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"-");										
					dosREPORT.writeBytes(padSTRING('R',strPRDDS.trim(),14));					
					L_strCLSFL = nvlSTRVL(M_rstRSSET.getString("LT_CLSFL"),"");
					if(!L_strCLSFL.equals(""))
					{
						if(L_strCLSFL.trim().equals(String.valueOf('9')))
							dosREPORT.writeBytes(padSTRING('R',"YES",12));
						else
							dosREPORT.writeBytes(padSTRING('R',"-",12));
					}					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					dosREPORT.writeBytes("\n");										
					cl_dat.M_intLINNO_pbst++;								
					////DATA .....					
					M_strSQLQRY = " SELECT * FROM QC_PSMST LEFT OUTER JOIN QC_RMMST ON"
						+" PS_QCATP = RM_QCATP  AND PS_TSTTP = RM_TSTTP AND PS_CMPCD=RM_CMPCD "
						+" AND PS_TSTNO = RM_TSTNO where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '" + strQCATP +"'";
					
					if( L_intQCPCT != intQCPCT)
					{
						if(L_intQCPCT == 1)
							M_strSQLQRY += " AND PS_TSTTP =" + strTESTS ;
						else 
							M_strSQLQRY += " AND PS_TSTTP in (" + strTESTS +")";
					}					
					M_strSQLQRY += " AND PS_LOTNO = '" + L_strLOTNO + "'";					
					L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);										
					if(L_rstRSSET!=null)
					{
						while(L_rstRSSET.next())
						{							
							L_tmsTSTDT =L_rstRSSET.getTimestamp("PS_TSTDT");
							if(L_tmsTSTDT != null)
							{
								L_strTMPDT = M_fmtLCDTM.format(L_tmsTSTDT);	
								dosREPORT.writeBytes(padSTRING('R',L_strTMPDT.trim().substring(11,16),6));
							}
							else
								dosREPORT.writeBytes(padSTRING('R',"-",6));						
							strTSTNO = nvlSTRVL(L_rstRSSET.getString("PS_TSTNO"),"");
							L_strTSTTP = nvlSTRVL(L_rstRSSET.getString("PS_TSTTP"),"");
							strRCLNO = nvlSTRVL(L_rstRSSET.getString("PS_RCLNO"),"");
							strLOTNO = nvlSTRVL(L_rstRSSET.getString("PS_LOTNO"),"");								
							if (L_strTSTTP.equals("0103"))
								dosREPORT.writeBytes(padSTRING('R',"COMP",6));
							else if (L_strTSTTP.equals("0104"))
								dosREPORT.writeBytes(padSTRING('R',"BAG",6));
							else if (L_strTSTTP.equals("0101"))
								dosREPORT.writeBytes(padSTRING('R',"GRAB",6));
							
							for(int i=0;i<vtrQPRLS.size();i++)
							{
								L_intTEMP = "0";				
								L_strQPFLD ="";
								L_strQPFLD = "PS_" + ((String)(vtrQPRLS.elementAt(i))).trim()+ "VL";
								L_strQPFLD = nvlSTRVL(L_rstRSSET.getString(L_strQPFLD),"0");									
								if(Double.parseDouble(L_strQPFLD)==0)
									L_strQPFLD="-";	
								else
								{
									L_intTEMP = setNumberFormat(Double.valueOf(L_strQPFLD).doubleValue(),arrDECCT[i]);
									L_strQPFLD = L_intTEMP;
								}															
								dosREPORT.writeBytes(padSTRING('L',L_strQPFLD,arrCOLWD[i]));									
							}								
							L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");									
							if(L_strREMDS.trim().length()>0)
							{								
								cl_dat.M_intLINNO_pbst++ ;
								dosREPORT.writeBytes("\n"+"      Remark:" + L_strREMDS.trim());								
							}
							else
							{
								if((L_strTSTTP.trim().equals("0103"))&& (!strRCLNO.trim().equals(strINTRCL_fn)))
								{
									dosREPORT.writeBytes("\n");
									cl_dat.M_intLINNO_pbst++;
									dosREPORT.writeBytes(padSTRING('R'," ",14));
								    dosREPORT.writeBytes(padSTRING('R',strRCLNO.trim() +" "+ getPRDDS(strLOTNO,strRCLNO),41));
								}
							}							
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							if(cl_dat.M_intLINNO_pbst> intLINCT)
							{				
								dosREPORT.writeBytes(stbDOTLN.toString());
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
									prnFMTCHR(dosREPORT,M_strEJT);
								prnHEADER();
							}
						}	
						L_rstRSSET.close();							
					}				
				}				
				M_rstRSSET.close();	
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst++;
				if(cl_dat.M_intLINNO_pbst> intLINCT)
				{				
					dosREPORT.writeBytes(stbDOTLN.toString());
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
						prnFMTCHR(dosREPORT,M_strEJT);
					prnHEADER();
				}
			}	
			dosREPORT.writeBytes(stbDOTLN.toString());										
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);												
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
				setMSG("For HTML Report Printing, Please insert 120 column Page..",'N');
			}			
			dosREPORT.close();
			fosREPORT.close();	
		}
		catch(Exception L_EX)
		{			
			setMSG(L_EX,"getDATA");
		}
	}			
	/**
	Method to validate the inputs given, before execution of the query part.
	*/
	boolean vldDATA()
	{
		try
		{				
			int L_intRPWID = 12;
			arrCHP02 = new String[10];			
			int j = 0;
			for(int i=0;i<50;i++)
			{							
				if(tblQCPRM.getValueAt(i,TB3_CHKFL).toString().trim().equals("true"))
				{					
					vtrQPRLS.addElement(tblQCPRM.getValueAt(i,TB3_QPRCD).toString().trim());															
					vtrQPRDS.addElement(tblQCPRM.getValueAt(i,TB3_QPRDS).toString().trim());					
					vtrQPRSH.addElement(tblQCPRM.getValueAt(i,TB3_UOMDS).toString().trim());
					if((tblQCPRM.getValueAt(i,TB3_QPRDS).toString().trim().equals("RPS")) || (tblQCPRM.getValueAt(i,TB3_QPRDS).toString().trim().equals("GL")))
					{
						arrCHP02[j] = tblQCPRM.getValueAt(i,TB3_MAXVL).toString().trim();
						j++;					
					}
					L_intRPWID += 7;
				}													
			}				
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))			
			{
				if( L_intRPWID > 232)
				{				
					jopOPTPN.showMessageDialog(this,"Number of QC Parameters Selected are more,\n Report willnot fit in Fanfold 358mm - 12in Paper..","Report Status",JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				else if( L_intRPWID <= 146)				
					jopOPTPN.showMessageDialog(this,"For Report Printing, Please insert \"80 column\" Paper..","Report Status",JOptionPane.INFORMATION_MESSAGE);				
				else				
					jopOPTPN.showMessageDialog(this,"For Report Printing, Please insert \"120 column\" Paper..","Report Status",JOptionPane.INFORMATION_MESSAGE);				
			}			
			if(M_rdbHTML.isSelected())
			{
				if( L_intRPWID > 203)
				{				
					jopOPTPN.showMessageDialog(this,"Number of QC Parameters Selected are more,\n  Report willnot fit in Fanfold 358mm - 12in Paper..","Report Status",JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				else if( L_intRPWID <= 124)				
					jopOPTPN.showMessageDialog(this,"For Report Printing, please insert \"80 column\" Paper,\n And through \"Page Setup\" set paper size as Fanfold 358mm - 12in..","Report Status",JOptionPane.INFORMATION_MESSAGE);
				else
					jopOPTPN.showMessageDialog(this,"For Report Printing, please insert \"120 column\" Paper,\n And through \"Page Setup\" set paper size as Fanfold 358mm - 12in..","Report Status",JOptionPane.INFORMATION_MESSAGE);
			}			
			if(rdbLTSPC.isSelected())
			{
				if(txtSPLOT.getText().length() < 8)			
				{
					setMSG("Enter valid Lot number/s to generate the Report OR Press F1 to select from list.. ",'E');
					txtSPLOT.requestFocus();			
					return false;
				}
			}
			else
			{
				if(txtTOLOT.getText().length() < 8)
				{
					setMSG("Enter Lot number to Specify the Lot range Or Press F1 toselect from List.. ",'E');
					txtTOLOT.requestFocus();			
					return false;
				}
				if(txtFMLOT.getText().length() < 8)
				{
					setMSG("Enter Lot number to Specify the Lot range Or Press F1 toselect from List.. ",'E');
					txtFMLOT.requestFocus();			
					return false;
				}				
			}			
			if(rdbLTRNG.isSelected())
			{
				if(Integer.valueOf(txtFMLOT.getText().trim()).intValue() >= Integer.valueOf(txtTOLOT.getText().trim()).intValue())
				{
					setMSG("FromLot number must be smaller than Tolot number.. ",'E');
					txtFMLOT.requestFocus();			
					return false;
				}
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
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{		
		try
		{				
			cl_dat.M_PAGENO ++;														
			int	L_intSPCRP = 0;
			int L_intSPCGL = 0;			
			String L_strQPRDS = " ";												
			StringBuffer L_stbHEAD1 = new StringBuffer(); 
			StringBuffer L_stbHEAD2 = new StringBuffer();
			StringBuffer L_stbHEAD3 = new StringBuffer(padSTRING('R',"",12));
																									
			L_stbHEAD2.append(padSTRING('R',"TIME",6)+padSTRING('R',"TYPE",6));			
			intROWLN = 12;
			int j=0;
			for(int i=0;i<vtrQPRDS.size();i++)
			{
				intROWLN += 7;
				L_strQPRDS = ((String)(vtrQPRDS.elementAt(i))).trim();
				L_stbHEAD2.append(padSTRING('L',L_strQPRDS,arrCOLWD[i]));
				
				if((L_strQPRDS.equals("RPS")) || (L_strQPRDS.equals("GL")))
				{
					L_stbHEAD3.append(padSTRING('L',arrCHP02[j],arrCOLWD[i]));	
					j++;
				}
				else
					L_stbHEAD3.append(padSTRING('L',"",arrCOLWD[i]));
			}
			stbDOTLN.delete(0,stbDOTLN.toString().length());						
			for(int i=0;i<intROWLN;i++)
				stbDOTLN.append("-");
			
			if(cl_dat.M_PAGENO ==1)
			{								
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
					+ " AND CMT_CGSTP ='QCXXLCS' AND isnull(CMT_STSFL,'') <> 'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
				if(M_rstRSSET != null)
				{
					String L_strTEMP="";
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
						if(!L_strTEMP.equals(""))
						{
							if(L_strTEMP.equals("DOC1"))
								strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals("DOC2"))
								strISOD2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals("DOC3"))
								strISOD3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						}					
					}
					M_rstRSSET.close();					
				}		
			}
			L_stbHEAD1.append("\n"+padSTRING('L',"-----------------------------------",intROWLN));
			L_stbHEAD1.append("\n"+padSTRING('L',strISOD1,intROWLN));
			L_stbHEAD1.append("\n"+padSTRING('L',strISOD2,intROWLN));
			L_stbHEAD1.append("\n"+padSTRING('L',strISOD3,intROWLN));
			L_stbHEAD1.append("\n"+padSTRING('L',"-----------------------------------",intROWLN));
			
			dosREPORT.writeBytes(L_stbHEAD1.toString());
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWLN - 24));
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"LOT CONTROL ANALYSIS SHEET",intROWLN - 24));
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");									
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO));
									
			dosREPORT.writeBytes("\n"+stbDOTLN.toString());
			dosREPORT.writeBytes("\n"+L_stbHEAD2.toString());
			dosREPORT.writeBytes("\n"+L_stbHEAD3.toString());
				
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n"+padSTRING('R',"Lot.",11));
			dosREPORT.writeBytes(padSTRING('R',"Start Date-Time",20));
			dosREPORT.writeBytes(padSTRING('R',"End Date-Time",20));
			dosREPORT.writeBytes(padSTRING('R',"Grade",11));
			dosREPORT.writeBytes(padSTRING('L',"Classfied",12));			
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n" + stbDOTLN.toString()+"\n");			
			cl_dat.M_intLINNO_pbst =13;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	 * Method to get the report details such as column name, description & number 
	 * of integers after the decimal point for the quality parameter values.
	 */
	private void getTSTDET()
	{
		int intROWCT = 0;		
		int L_intROWCT = 0;
		String L_strQPRCD = "";
		String L_strCHP02 ="";
		String L_strSHRDS ="";		
		try
		{
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,6,3)L_strQPRCD,CMT_NCSVL,CMT_SHRDS,CMT_NMP01,"
			+" CMT_NMP02,CMT_CHP02 from CO_CDTRN where CMT_CGMTP ='RPT' AND CMT_CGSTP = 'QCXXLCS'"			
			+" order by CMT_NCSVL";			
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,   ResultSet.CONCUR_UPDATABLE);		
			M_rstRSSET = L_stat.executeQuery(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{
				intROWCT=0;
				while(M_rstRSSET.next())
					intROWCT++;					
				if(intROWCT >0)
				{
					arrCOLWD = new int[intROWCT]; 
					arrDECCT = new int[intROWCT]; 
				}
			}
			M_rstRSSET.beforeFirst();										
			L_intROWCT = 0;
		    while (M_rstRSSET.next())
			{
				L_strQPRCD ="";
				L_strSHRDS ="";
				L_strCHP02 ="";
				L_strQPRCD = M_rstRSSET.getString("L_strQPRCD");
				
				arrDECCT[L_intROWCT]= M_rstRSSET.getInt("CMT_NMP02");
				arrCOLWD[L_intROWCT]= M_rstRSSET.getInt("CMT_NMP01");				
				
				L_strCHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"");				
				L_strSHRDS = M_rstRSSET.getString("CMT_SHRDS");
								
				if(L_intROWCT < 10)
					tblQCPRM.setValueAt(new Boolean(true),L_intROWCT,TB3_CHKFL);
				else
					tblQCPRM.setValueAt(new Boolean(false),L_intROWCT,TB3_CHKFL);
					
				tblQCPRM.setValueAt(L_strQPRCD,L_intROWCT,TB3_QPRCD);
				tblQCPRM.setValueAt(L_strSHRDS,L_intROWCT,TB3_QPRDS);
				tblQCPRM.setValueAt(String.valueOf(arrDECCT[L_intROWCT]).toString(),L_intROWCT,TB3_UOMDS);////Temporary data in place of unot of measurement
				tblQCPRM.setValueAt(L_strCHP02,L_intROWCT,TB3_MAXVL);
				L_intROWCT += 1;															
			}			
			M_rstRSSET.close();						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSTDET");
		}	
	}	
	/**
	 * Mehod to get the Product description from lot number.
	 * @param P_strLOTNO String argument to pass the lot number.
	 * @param P_strRCLNO String argument to pass Reclassification number.
	 */
	private String getPRDDS(String P_strLOTNO,String P_strRCLNO)
	{
		String L_strSQLQRY ="";
		ResultSet L_rstRSSET;
		try
		{
			strPRDDS ="";
			L_strSQLQRY ="Select LT_PRDCD,PR_PRDDS from PR_LTMST,CO_PRMST where"
				+" PR_PRDCD = LT_PRDCD and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = '"+P_strLOTNO.trim() +"'"
				+" AND LT_RCLNO = '"+P_strRCLNO.trim() +"'";
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
					strPRDDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
				L_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDDS");
			return " ";
		}
		return strPRDDS;
	}
}
