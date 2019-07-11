/*
System Name   : Lebortory Information Management System
Program Name  : Polystyrene Test Proparty Entry Screen for various tests.
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 18 JULY 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
/**
<P><PRE style = font-size : 10 pt >
<b>System Name :</b> Lebortory Information Management System
 
<b>Program Name :</b> Polystyrene Test Proparty Entry Screen for various tests.

<b>Purpose :</b> This module is used for addition, modification,deletion AND 
enquiry of Composite / Bag  / Grab test entries. It updates Test details in 
Polystyrene master (qc_psmst), AND remarks  in (qc_rmmst) table.
Test number generated AND updating is done using codes transaction.

List of tables used :
Table Name     Primary key                         Operation done
                                             Insert  Update  Query  Delete	
-----------------------------------------------------------------------------------
QC_PSMST        PS_QCATP,PS_TSTTP,PS_LOTNO
                PS_RCLNO,PS_TSTNO,PS_TSTDT     #        #       #
PR_LTMST        LT_PRDTP,LT_LOTNO,LT_RCLNO              #       #	      
QC_RMMST        RM_QCATP,RM_TSTTP,RM_TSTNO     #        #       #      #
CO_CDTRN        CMT_CGMTP,CMT_CGSTP,CMT_CODCD           #       #
CO_PRMST        PR_PRDCD								        #
-----------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
-----------------------------------------------------------------------------------
txtTSTTP    CMT_CODCD      CO_CDTRN      VARCHAR(15)   Lab Type
txtTSTTP    CMT_CODCD      CO_CDTRN      VARCHAR(15)   Test Type
txtTSTDS    CMT_CODDS      CO_CDTRN      VARCHAR(30)   Test Description
txtLOTNO    LT_LOTNO       PR_LTMST      VARCHAR(8)    Lot No.
txtRCLNO    LT_RCLNO       PR_LTMST      VARCHAR(2)    Rcl No.
txtTSTDT    PS_TSTDT       QC_RSMST      Timestamp     Test date & time
txtTSTTM 
txtTSTNO    CMT_CCSVL      CO_CDTRN			
txtTSTBY    CMT_CODCD      CO_CDTRN      VARCHAR(15)   Tested by
txtMORTP	
txtREMDS    RM_REMDS       QC_RMMST      VARCHAR(200)  Remark
txtLINNO    LT_LINNO       PR_LTMST      VARCHAR(2)    Line No.		
txtCYLNO    LT_CYLNO       PR_LTMST      VARCHAR(5)    Silo No.
txtRUNNO    LT_RUNNO       PR_LTMST      VARCHAR(8)    Run No.
txtTPRDS    PR_PRDDS       CO_PRMST      VARCHAR(45)   Target Grade 
txtTPRDS    PR_PRDDS       CO_PRMST      VARCHAR(45)   Target Grade
-----------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description        Display Columns       Table Name
-----------------------------------------------------------------------------------
txtTSTTP    Test type, desc            CMT_CODCD,CMT_CODDS   CO_CDTRN
txtLOTNO    Lot No., Product Code,     LT_LOTNO,LT_PRDCD,    PR_LTMST
            Grade,Lot start timestamp  PR_PRDDS,LT_PSTDT
txtTSTBY    User Initial, Name         CMT_CODCD,CMT_CODDS   CO_CDTRN/AUT/QCXXTST
                                                             CMT_CHP01 = LM_QCATP
txtLOTNO    Lot No, Rcl No,Test type   PS_LOTNO,PS_RCLNO,    QC_PSMST
            Test Date & Time           PS_TSTDT,PS_TSTTP
txtRCLNO    Rcl No., Grade             LT_RCLNO,PR_PRDDS     PR_LTMST
txtTSTDT    Lot No., Test date & time  PS_LTMST,PS_TSTDT     QC_PSMST
-----------------------------------------------------------------------------------

<B>Queries:</b>
<I><B>To Insert New Record :</B> 
   Quality parameter Values are inserter in the Table QC_PSMST.
   Lot Details are update in the Table PR_LTMST
   If Remark is Given the it is inserted in the Table QC_RMMST
   After All these Serial Number is inserted in the Table CO_CDTRN.

<B>To Update Records : </B>
  Quality parameter Values are updated in Table QC_PSMST for given condiations
      1) PS_QCATP = selected QCA Type.
      2) AND PS_TSTTP = given Test Type
      3) AND PS_LOTNO = selected Lot Number
      4) AND PS_RCLNO = initial Reclassification Number.
      5) AND PS_TSTDT = given Date & Time.
											   
   If Remark is already given & it is modified then it is updated in table QC_RMMST
   If Remark is not given then it is inserted in table QC_RMMST
<B>To Delete Records : </B>
	Deletation of record is not allowed here.
</I>
Validations : 
 - Entries are accepted for Composite/ Grab / Bag test. Test Types (0103,0101,0104)
 - For composite / Bag test single entry is allowed per lot , whereas Grab test 
   can have entries for diff.  time.
 - RCL No. is taken as '00' by default in addition mode.
 - Composite test entry can be done only after the lot closing. (LT_PENDT sholud 
   not be null)
 - Test Remark if entered is saved in QC_RMMST .
		(RM_REMDS with RM_TSTTP = LM_TSTTP AND RM_TSTNO = LM_TSTNO)  
 - Quality parameters  are fecthed from  CO_QPMST table AND displayed in Jtable 
   in case of composite AND bag .	
 - Quality parameters  are fecthed from  CO_CDTRN table AND displayed in Jtable 
   in case of grab test.
 - Specifications are fetched for that set -
   where PS_TATDT falls between QP_STRDT AND nullif(QP_ENDDT,current date)
 - Tested by / Test date AND Time validation is done.
 - Test No generation / Updation is done using -
		exeSRLGET( "D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",LM_CODCD)	
		exeSRLGET( "D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",LM_CODCD)	
		where		LM_CODCD = LM_TSTTP +"_"+ fin. year digit + LM_QCATP
 - For Grab test , if a test entry is already done for that lot, same test no. 
   is fecthed AND updated.
 - LT_CLSFL is updated in Lot matser , after composite entry , FLAG = 3
      Grab = 1
      Bag  = 2 		
 - Modification option is allowed till lot is finally classified, whereas 
   modification after final class. is available 	for user type 'AU2'
*/

public class qc_tepsm extends cl_pbase
{
	private JComboBox cmbPRDTP;
	private JTextField txtTSTTP;
	private JTextField txtTSTDS;
	private JTextField txtLOTNO;
	private JTextField txtRCLNO;
	private JTextField txtTSTNO;
	private JTextField txtTSTDT;
	private JTextField txtTSTTM;
	private JTextField txtTSTBY;
	private JTextField txtMORTP;
	private JTextField txtLINNO;
	private JTextField txtCYLNO;
	private JTextField txtRUNNO;
	private JTextField txtTPRDS;
	private JTextField txtCPRDS;
	private JTextField txtREMDS;
	private JTextField txtQPRVL;
	private JTextField txtQPRCD;
	private cl_JTable tblITMDT;
	private JTextField txtVALUE;
	
	private final int TBL_CHKFL =0;
	private final int TBL_QPRCD =1;
	private final int TBL_QPRDS =2;
	private final int TBL_UOMCD =3;
	private final int TBL_QPRVL =4;	
	
	private String strQCATP;
	private String strPRDTP ="01";
	private String strPRDCD;	
	private String strOREMDS="";								
	private double dblNPFVL;
	private double dblNPTVL;	
	private String[] L_arrHEADR = new String[2];
	
	private String strRCLNO;
	private String strTSTNO;
	private String strLOTNO;
	private String strTSTDT;
	private String strTSTTP;
	private String strCLSFL;
	private String strTPRCD;	
	private String strPENDT;
	private String strSTSFL;
	private String strCODCD;	
	private String strTSTDS;
	
	private String strVLDLOT="YES";
					
	private int intROWCT;	
	private int intROWNO = 50;
	private String strCMPTP="0103";
	private final String strINRCL_fn ="00";
	private final String strCOTST_fn ="0103";
	private final String strGRTST_fn ="0101";
	private final String strBGTST_fn ="0104";		
	private int intQPRCT = 0;	
	private boolean flgLOTFL  = false;	
	private boolean flgSPCFL  = false;	
	private final String strDFLSRL_fn ="00000";
	private double dblNPVL ;
	private JOptionPane jopOPTPN = new JOptionPane();	/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();
	qc_tepsm()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Prod. Type"),1,2,1,1,this,'R');
			add(cmbPRDTP = new JComboBox(),1,3,1,1.3,this,'R');		
		
			add(new JLabel("Test Type"),2,2,1,1,this,'R');
			add(txtTSTTP = new TxtNumLimit(4),2,3,1,1.3,this,'R');
			add(new JLabel("Lot No."),2,4,1,.7,this,'R');
			add(txtLOTNO = new TxtNumLimit(8),2,5,1,1.2,this,'L');
			add(new JLabel("RCL No."),2,6,1,.7,this,'R');
			add(txtRCLNO = new TxtNumLimit(2),2,7,1,1,this,'L');
		
			add(new JLabel("Description"),3,2,1,1,this,'R');
			add(txtTSTDS = new TxtLimit(30),3,5,1,3.48,this,'R');
			add(new JLabel("Tested By"),3,6,1,.7,this,'R');
			add(txtTSTBY = new TxtLimit(3),3,7,1,1,this,'L');
					
			add(new JLabel("Test NO"),4,2,1,1,this,'R');
			add(txtTSTNO = new TxtNumLimit(10),4,3,1,1.3,this,'R');							
			add(new JLabel("Date"),4,4,1,.7,this,'R');			
			add(txtTSTDT = new TxtDate(),4,5,1,1.2,this,'L');
			add(new JLabel("Time"),4,6,1,.7,this,'R');			
			add(txtTSTTM = new TxtTime(),4,7,1,1,this,'L');
		
			add(new JLabel("Mat Source"),5,2,1,1,this,'R');
			add(txtMORTP = new JTextField(),5,3,1,1.3,this,'R');		
			add(new JLabel("Line No"),5,4,1,.7,this,'R');
			add(txtLINNO = new JTextField(),5,5,1,1.2,this,'L');			
			add(new JLabel("Silo No."),5,6,1,.7,this,'R');			
			add(txtCYLNO = new JTextField(),5,7,1,1,this,'L');
		
			add(new JLabel("Run No."),6,2,1,1,this,'R');
			add(txtRUNNO = new JTextField(),6,3,1,1.3,this,'R');		
			add(new JLabel("Tgt Grade"),6,4,1,.7,this,'R');
			add(txtTPRDS = new JTextField(),6,5,1,1.2,this,'L');		
			add(new JLabel("Cls. Grade"),6,6,1,.7,this,'R');
			add(txtCPRDS = new JTextField(),6,7,1,1,this,'L');
		
			add(new JLabel("Remark"),7,2,1,1,this,'R');
			add(txtREMDS = new JTextField(),7,7,1,5.66,this,'R');
		
			String[] L_COLHD = {"Select","Para Code","Description","UOM","Value"};
      		int[] L_COLSZ = {50,129,200,90,125};	    				
			tblITMDT = crtTBLPNL1(this,L_COLHD,intROWNO,8,2,10.3,6.4,L_COLSZ,new int[]{0});			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);									
			txtTSTTP.setInputVerifier(objINPVR);
			txtLOTNO.setInputVerifier(objINPVR);
			txtTSTBY.setInputVerifier(objINPVR);	
			txtTSTNO.setInputVerifier(objINPVR);
			txtTSTDT.setInputVerifier(objINPVR);
			txtTSTTM.setInputVerifier(objINPVR);
		
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}			
            tblITMDT.setCellEditor(TBL_QPRVL, txtVALUE = new JTextField());
			tblITMDT.setCellEditor(TBL_QPRCD, txtQPRCD = new TxtLimit(3));
			txtVALUE.addKeyListener(this);txtVALUE.addFocusListener(this);
			txtQPRCD.addKeyListener(this);txtQPRCD.addFocusListener(this);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
 	}		
	/**
	 * Super Class Method overrided here to enable & disable the components according to requriement
	 * @ param P_flgSTAT boolean argument to pass the state of the components
	 */
	void setENBL(boolean P_flgSTAT)
	{	
		super.setENBL(P_flgSTAT);	
		cmbPRDTP.setEnabled(true);
		tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblITMDT.cmpEDITR[TBL_QPRVL].setEnabled(true);
        tblITMDT.cmpEDITR[TBL_QPRCD].setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)
		{
			txtTSTTP.setEnabled(true);			
			txtLOTNO.setEnabled(true);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
			{
				tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(false);
				tblITMDT.cmpEDITR[TBL_QPRVL].setEnabled(false);
				tblITMDT.cmpEDITR[TBL_QPRCD].setEnabled(false);
			}
		}				
	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{			
			txtRCLNO.setEnabled(true);
			txtTSTDT.setEnabled(true);
			txtREMDS.setEnabled(true);
			txtTSTBY.setEnabled(true);
			txtTSTDT.setEnabled(true);
			txtTSTTM.setEnabled(true);			
		}	
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{			
			txtTSTDT.setEnabled(true);
			txtREMDS.setEnabled(true);			
			if(txtTSTTP.getText().equals(strGRTST_fn))
			{
				txtTSTDT.setEnabled(true);
				txtTSTTM.setEnabled(true);
			}
			else
		    {
				txtTSTDT.setEnabled(false);	
				txtTSTTM.setEnabled(false);					
		    }            
		}									
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
		    if(txtTSTTP.getText().equals(strGRTST_fn))			
		    {
				txtTSTDT.setEnabled(true);	
				txtTSTTM.setEnabled(true);					
		    }
		    else
		    {
				txtTSTDT.setEnabled(false);	
				txtTSTTM.setEnabled(false);					
		    }
		}					
	}
	
	
	private void clrCOMP_1()
	{
		try
		{
			cmbPRDTP.removeFocusListener(this);
			String L_strPRDTP = cmbPRDTP.getSelectedItem().toString();
			int L_intPRDTP = cmbPRDTP.getSelectedIndex();
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			clrCOMP();
			cmbPRDTP.setSelectedItem(L_strPRDTP);
			cmbPRDTP.setSelectedIndex(L_intPRDTP);
			cmbPRDTP.addFocusListener(this);
		
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP_1");}
	}

   	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			{
				clrCOMP();	
				//setENBL(false);			
				setMSG("Please Select an option ..",'N');				
			}		
			else
			{
				clrCOMP();
				cmbPRDTP.setEnabled(true);
				strQCATP = M_strSBSCD.substring(2,4);
				strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
				setMSG("Please Enter Test Type OR Press F1 to Select from List..",'N');
				txtTSTTP.requestFocus();
				//cmbPRDTP.requestFocus();
			}
			setENBL(false);			
		}
		else if(M_objSOURC == cmbPRDTP)
		{
	        //if(!cmbPRDTP.getSelectedItem().toString().substring(0,2).equals(strPRDTP))
			//{
				strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
				clrCOMP_1();
			//}
			txtTSTTP.requestFocus();
			setMSG("Please Enter Test Type OR Press F1 to select from List..",'N');
		}
		else if(M_objSOURC == txtLOTNO)
		{			
			String L_strSQLQRY ="";
			ResultSet L_rstRSSET;
			char L_chrPRGFL=' ';
			flgLOTFL = false;
			txtTSTDT.setText("");
			txtTSTTM.setText("");
			strLOTNO = txtLOTNO.getText().trim();
			strRCLNO = strINRCL_fn;
			txtRCLNO.setText(strRCLNO);				
			try
			{				
				if(txtLOTNO.getText().trim().length() == 0)				
				{
					setMSG("Lot number can not be blank",'E');
					return;
				}
				if(strVLDLOT == "NO")
					return;
				this.setCursor(cl_dat.M_curWTSTS_pbst);								
				flgLOTFL = getLOTDET();				
				flgSPCFL = true;
    			if(strPRDTP.equals("02"))
    			{
    				L_strSQLQRY ="Select QP_TSTTP from CO_QPMST where QP_QCATP ='"+strQCATP.trim()+"'";
    				L_strSQLQRY +=" AND QP_PRDCD ='"+strPRDCD+"'";
    				L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);				
    				if(L_rstRSSET != null)
    				{
    					if(!(L_rstRSSET.next() && flgLOTFL == true))
    					{						
    						setMSG("Specification not available, Test entry is not allowed for this grade",'E');
    						L_rstRSSET.close();
    						flgSPCFL = false;
    						return;
    					}					
    					L_rstRSSET.close();
    				}
    			}
				tblITMDT.clrTABLE();
				intQPRCT = 0;
				if(flgLOTFL && flgSPCFL)				
				if(strTSTTP.trim().equals(strGRTST_fn))
				{
					//getTSTPAR();									
					/*L_strSQLQRY = "select SUBSTRING(CMT_CODCD,1,3)TS_QPRCD,CMT_CODDS,CMT_CHP02,CMT_CCSVL from CO_CDTRN";
					L_strSQLQRY += " where CMT_CGMTP = 'SYS'";
					L_strSQLQRY += " AND CMT_CGSTP = 'QCXXQPR'";
					L_strSQLQRY += " AND CMT_MODLS LIKE '%"+ txtTSTTP.getText().trim()+"%'";
					L_strSQLQRY += " AND CMT_CHP01 LIKE '%"+ strTPRCD.substring(0,4) + "%'";
					L_strSQLQRY += " order by CMT_CHP02";					*/
					L_strSQLQRY = "Select QS_QPRCD,QS_QPRDS,QS_UOMCD from CO_QSMST";
					L_strSQLQRY += " where ";
					L_strSQLQRY += " QS_PRDCT LIKE '%"+ strTPRCD.substring(0,4) + "%'";
					L_strSQLQRY += " order by QS_ORDBY";					
//System.out.println(L_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					
					if(M_rstRSSET != null)
					{						
						tblITMDT.clrTABLE();
						while (M_rstRSSET.next())
						{							
							tblITMDT.setValueAt(M_rstRSSET.getString("QS_QPRCD"),intQPRCT,TBL_QPRCD);							
							tblITMDT.setValueAt(M_rstRSSET.getString("QS_QPRDS"),intQPRCT,TBL_QPRDS);							
						    tblITMDT.setValueAt(M_rstRSSET.getString("QS_UOMCD"),intQPRCT,TBL_UOMCD);
						    intQPRCT ++;									
						}
						M_rstRSSET.close();
					}
				}
				else 
				{
					if(strTSTTP.trim().equals(strCOTST_fn))
						L_chrPRGFL='C';
					else
						L_chrPRGFL='B';
								
					L_strSQLQRY = "select QP_QPRCD,QP_QPRDS,QP_UOMDS,QP_ORDBY from CO_QPMST where QP_QCATP ='"+strQCATP+"'" ;
					L_strSQLQRY += " AND QP_PRDCD = '"+strTPRCD.trim()+"'";
					L_strSQLQRY += " AND QP_SRLNO = '"+strDFLSRL_fn.trim()+"'";
					L_strSQLQRY += " AND QP_PRGFL LIKE '%"+L_chrPRGFL+ "%' AND QP_ENDDT is null AND QP_ORDBY is NOT NULL order by qp_ordby";					
					//System.out.println("else"+L_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(M_rstRSSET !=null)
					{						
						tblITMDT.clrTABLE();
						while (M_rstRSSET.next())
						{							
							tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),""),intQPRCT,TBL_QPRCD);							
							tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRDS"),""),intQPRCT,TBL_QPRDS);							
						    tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_UOMDS"),""),intQPRCT,TBL_UOMCD);
							intQPRCT ++;
						}
						M_rstRSSET.close();
					}					
				}					
							
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
				{					
					if((strTSTTP.trim().equals(strCOTST_fn)) ||strTSTTP.trim().equals(strBGTST_fn))
						getDATA();					
				}
				else
					txtTSTDT.setText(cl_dat.M_strLOGDT_pbst);					
				this.setCursor(cl_dat.M_curDFSTS_pbst);			
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"ActionPerformed for txtLOTNO");
			}
		}																			
	
	}
	public void focusGained(FocusEvent L_FE)
	{		
		super.focusGained(L_FE);
        if((M_objSOURC == tblITMDT.cmpEDITR[TBL_QPRCD]) && (txtTSTTP.getText().trim().equals(strCOTST_fn)))
		{			
			if(tblITMDT.getSelectedRow() < intQPRCT)
			    ((JTextField)(tblITMDT.cmpEDITR[TBL_QPRCD])).setEditable(false);
			else
			    ((JTextField)(tblITMDT.cmpEDITR[TBL_QPRCD])).setEditable(true);
		}			    
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if (L_KE.getKeyCode()== L_KE.VK_F1)
			{
				String L_strSQLQRY = "";
				if(M_objSOURC == tblITMDT.cmpEDITR[TBL_QPRCD])
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtQPRCD";
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String L_arrHEADR[] ={"Code","Description","UOM"};
				  	//L_strSQLQRY = "Select QS_QPRCD,QS_QPRDS,QS_UOMCD from CO_QSMST";
					//L_strSQLQRY +=" where QS_TSTLS NOT LIKE '%"+ txtTSTTP.getText().trim()+"%'";
					//cl_hlp(L_strSQLQRY,1,1,L_arrHEADR,3,"CT");
						M_strSQLQRY = "select QS_QPRCD ,QS_QPRDS1 QS_QPRDS,QS_UOMCD1 QS_UOMCD,QS_TSMCD1,QS_TSTSP,QS_TSTCN,QS_ORDBY  from co_qsmst where  QS_TSTLS NOT LIKE '%"+ txtTSTTP.getText().trim()+"%'  order by QS_QPRDS1"; //,QS_QPRDS,QS_UOMCD,QS_TSMCD,QS_ORDBY 
						String L_arrQPR[] = {"Code","Description","UOM","Test Method","Test Cond","Test Spec","Order By"};
						//System.out.println("M_strSQLQRY(f1)>>"+M_strSQLQRY);
						M_flgBIGHLP = true;
						cl_hlp(M_strSQLQRY,1,1,L_arrQPR,7,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtTSTTP)
				{					
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTSTTP";
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String L_arrHEADR[] ={"Test Type","Description"};
				  	L_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,4),CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP ='SYS'AND CMT_CGSTP = 'QCXXTST' AND CMT_NMP01 =2";	
					if(txtTSTTP.getText().trim().length() >0)
						L_strSQLQRY += " AND CMT_CODCD like '"+txtTSTTP.getText().trim() +"%'";						
					cl_hlp(L_strSQLQRY,1,1,L_arrHEADR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtTSTBY)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTSTBY";
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String L_arrHEADR[]= {"Initial","Description"};
				  	L_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='A"+cl_dat.M_strCMPCD_pbst+"'AND CMT_CGSTP = 'QCXXTST'";
					L_strSQLQRY +=" AND CMT_CHP01 ='"+strQCATP +"'";
					L_strSQLQRY +=" ORDER BY CMT_CODCD";					
					cl_hlp(L_strSQLQRY,1,1,L_arrHEADR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtTSTDT)
				{
					if(strTSTTP.equals(strGRTST_fn))
					{
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtTSTDT";
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						String L_arrHEADR[] = {"Lot Number","Test Date AND Time"};
					  	L_strSQLQRY = "Select PS_LOTNO,PS_TSTDT from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_TSTTP ='0101'";
						L_strSQLQRY +=" AND PS_LOTNO ='"+strLOTNO +"'";
						L_strSQLQRY +=" AND PS_QCATP ='"+strQCATP.trim()+"'";
						L_strSQLQRY +=" AND isnull(PS_STSFL,'')<>'X' ORDER BY PS_TSTDT";						
						cl_hlp(L_strSQLQRY,1,2,L_arrHEADR,2,"CT");
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				if(M_objSOURC == txtLOTNO)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtLOTNO";
					String[] L_arrHEADR1;					
					String L_strTEMP="";
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
						L_arrHEADR1 =new String []{"Lot No","Product Code","Grade ","Lot Start Date"};
					else
						L_arrHEADR1 = new String []{"Lot No","RCL No","Test Date & Time","Test Type"};	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
					{
						if(txtLOTNO.getText().trim().length()>0)
							L_strTEMP = " AND LT_LOTNO like '"+ txtLOTNO.getText().trim() +"%'";
						
						if(txtTSTTP.getText().trim().equals(strCOTST_fn))
						{
						    L_strSQLQRY = "Select LT_LOTNO,LT_PRDCD,PR_PRDDS,LT_PSTDT from PR_LTMST,CO_PRMST WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '" + strPRDTP + "' AND LT_STSFL IN ('0','1')";
						    L_strSQLQRY += " AND LT_CLSFL IN('0','2','X') AND LT_PRDCD = PR_PRDCD AND LT_PSTDT is not null "+ L_strTEMP +" order by LT_LOTNO ";
						}
						else if(txtTSTTP.getText().trim().equals(strBGTST_fn))
						{
						    L_strSQLQRY = "Select LT_LOTNO,LT_PRDCD,PR_PRDDS,LT_PSTDT from PR_LTMST,CO_PRMST WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '" + strPRDTP + "' AND LT_STSFL IN ('0','1')";
						    L_strSQLQRY += " AND LT_CLSFL IN('0','1','X') AND LT_PRDCD = PR_PRDCD AND LT_PSTDT is not null "+ L_strTEMP +" order by LT_LOTNO";
						}
						else if(txtTSTTP.getText().trim().equals(strGRTST_fn))
						{
							L_strSQLQRY = "Select LT_LOTNO,LT_PRDCD,PR_PRDDS,LT_PSTDT from PR_LTMST,CO_PRMST WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '" + strPRDTP + "' AND LT_STSFL IN ('0','1')";
							L_strSQLQRY += " AND LT_CLSFL IN('0','X') AND LT_PRDCD = PR_PRDCD "+ L_strTEMP +" order by LT_LOTNO ";
						}
					}
					else
					{					
						if(txtLOTNO.getText().trim().length()>0)
							L_strTEMP = " AND PS_LOTNO like '"+ txtLOTNO.getText().trim() +"%'";
						L_strSQLQRY = "Select PS_LOTNO,PS_RCLNO,PS_TSTDT,PS_TSTTP from QC_PSMST WHERE PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+strQCATP+"'";
						L_strSQLQRY +=" AND PS_TSTTP = '" + strTSTTP +"'"+ L_strTEMP +" and isnull(PS_STSFL,'') <>'X' order by PS_TSTDT desc";
					}					
					cl_hlp(L_strSQLQRY,1,1,L_arrHEADR1,4,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtRCLNO)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					L_strSQLQRY = " ";
					L_strSQLQRY = "select LT_RCLNO,PR_PRDDS from PR_LTMST,CO_PRMST where LT_PRDCD = PR_PRDCD AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = ";
					L_strSQLQRY +="'"+txtLOTNO.getText().trim()+"'";
					L_strSQLQRY +=" AND LT_PRDTP = "+"'"+strPRDTP.trim()+"'";
					M_strHLPFLD = "txtRCLNO";
					String L_arrHEADR[] ={"RCL No","Grade"};
					cl_hlp(L_strSQLQRY,1,1,L_arrHEADR,2,"CT");
				}
			}
			else if (L_KE.getKeyCode()== L_KE.VK_ENTER)
			{   
                if(M_objSOURC == cmbPRDTP)
				{
				    txtTSTTP.requestFocus();
				    setMSG("Enter Test Type..",'N');
				}				
                else if (M_objSOURC == tblITMDT.cmpEDITR[TBL_QPRVL])
				{					
					if(tblITMDT.getSelectedColumn() == TBL_QPRVL)
					{						
						intROWCT = tblITMDT.getSelectedRow();
						getQPRRNG();
						vldQPRNG();
					}
				}		
				else if (M_objSOURC == tblITMDT.cmpEDITR[TBL_QPRCD])
				{					
					intROWCT = tblITMDT.getSelectedRow();
					M_strSQLQRY = "SELECT QS_QPRDS,QS_UOMCD from CO_QSMST WHERE QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QS_QPRCD ='" ;
					M_strSQLQRY += txtQPRCD.getText().toUpperCase() +"'";
					//System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
    					if(M_rstRSSET.next())
    					{
    					    txtQPRCD.setText(txtQPRCD.getText().toUpperCase());    					   
    					    tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_QPRDS"),""),intROWCT,TBL_QPRDS);
    					    tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""),intROWCT,TBL_UOMCD);
    					}
    					M_rstRSSET.close();
					}
				}		
				else if(M_objSOURC == txtTSTTP)
				{
					strTSTTP = txtTSTTP.getText().trim();	
					clrCOMP_1();		
					txtTSTTP.setText(strTSTTP);	
					txtTSTDS.setText(strTSTDS);	
					if(strTSTTP.trim().length()== 4)
					{
						if(strTSTTP.trim().equals(strGRTST_fn))
						{
							txtTSTDT.setEnabled(true);
							txtTSTTM.setEnabled(true);
						}
						txtLOTNO.requestFocus();
						setMSG("Please Enter Lot Number..",'N');
					}
					else					
						setMSG("Test Type cannot be blank or less than 4 digits..",'E');					
				}
				else if(M_objSOURC == txtLOTNO)
				{
				    strLOTNO = txtLOTNO.getText().trim();
				    clrCOMP_1();
				    txtTSTTP.setText(strTSTTP);
				    txtTSTDS.setText(strTSTDS);	
				    txtLOTNO.setText(strLOTNO);
					if(txtLOTNO.getText().trim().length() == 8)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{							
							if(txtTSTBY.getText().trim().length()==0) 
								txtTSTBY.setText(cl_dat.M_strUSRCD_pbst);							
							txtTSTBY.requestFocus();
							setMSG("Enter Initial of the Person who performed the Test..",'N');						
						}
						else						
						{
							txtTSTDT.requestFocus();
							if(txtTSTTP.getText().trim().equals(strGRTST_fn))
								setMSG("Enter test Date OR Press F1, to select from list..",'N');
							else
								setMSG("Enter test Date ..",'N');
						}
					}
					else
					    setMSG("Lot number cannot be blank or less than 8 digits..",'E');						
				}
				else if(M_objSOURC == txtRCLNO)
				{
					if(txtRCLNO.getText().trim().length()>0)
					{
						strLOTNO = txtLOTNO.getText().trim();
						strRCLNO = txtRCLNO.getText().trim();	
						txtTSTBY.setText(cl_dat.M_strUSRCD_pbst);
						txtTSTBY.requestFocus();										
						setMSG("Please Enter the initial of the Person Whow completed the test..",'N');					
					}													
				}
				else if(M_objSOURC == txtTSTBY)
				{
					if(txtTSTBY.getText().trim().length()>0)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							txtTSTDT.setText(cl_dat.M_strLOGDT_pbst);
							if(strTSTTP.equals(strCOTST_fn))
                            {
                                if(strVLDLOT.equals("YES"))
                                {
                                    txtTSTDT.setText(strPENDT.substring(0,10));    
                                    txtTSTTM.setText(strPENDT.substring(11,16));    
                                }
                            }
                            else
                                txtTSTTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
						}
						txtTSTDT.requestFocus();
						setMSG("Enter Test Date.. ",'N');
					}
				}
				else if(M_objSOURC == txtTSTDT)
				{
					if(txtTSTDT.getText().trim().length()>0) 
					{						
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							txtTSTTM.requestFocus();
							setMSG("Enter Test Time.. ",'N');
						} 
						else
						{
							if(txtTSTTM.getText().trim().length()>0)
								getDATA();
						}
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							txtREMDS.requestFocus();
							setMSG("Please Enter Remarks if Any..",'N');
						}										
					}
				}
				else if(M_objSOURC == txtTSTTM)
				{
					if((txtTSTTM.getText().trim().length()>0) &&((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))))
					{
						txtREMDS.requestFocus();
						setMSG("Please Enter Remarks if any.. ",'N');
					}
				}			
				else if(M_objSOURC == txtREMDS)
				{
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					{
						tblITMDT.requestFocus();
						tblITMDT.setRowSelectionInterval(0,0);
						tblITMDT.setColumnSelectionInterval(TBL_QPRVL,TBL_QPRVL);
						tblITMDT.editCellAt(0,TBL_QPRVL);
						tblITMDT.cmpEDITR[TBL_QPRVL].requestFocus();
						setMSG("Please Enter Quality Parameter Values.. ",'N');
					}
				}					
			}		
		}
        catch(Exception L_EX)
        {
			setMSG(L_EX,"vk_ENTER");                            
		}          
	}	
	/**
	 * Method to execute the F1 Help for Corresponding TextField.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();		
		cl_dat.M_flgHELPFL_pbst = false;
		if(M_strHLPFLD.equals("txtQPRCD"))
		{
			txtQPRCD.setText(cl_dat.M_strHLPSTR_pbst);				
			tblITMDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblITMDT.getSelectedRow(),2);
			tblITMDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblITMDT.getSelectedRow(),3);
		}
		else if(M_strHLPFLD.equals("txtTSTTP"))
		{
			txtTSTTP.setText(cl_dat.M_strHLPSTR_pbst);				
			txtTSTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD.equals("txtLOTNO"))
		{		    
			txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);			
		}		
		else if(M_strHLPFLD.equals("txtTSTBY"))
		{
        	txtTSTBY.setText(cl_dat.M_strHLPSTR_pbst);
		}
		else if(M_strHLPFLD.equals("txtTSTDT"))
		{
        	txtTSTDT.setText(cl_dat.M_strHLPSTR_pbst.substring(0,10));
			txtTSTTM.setText(cl_dat.M_strHLPSTR_pbst.substring(11,16));
		}
    }
	/**
	 * Method to get the Lot details for given Lot number & Reclassification number
	 */
	private boolean getLOTDET()
	{
		String L_strSQLQRY= "";
		strCLSFL ="";
		strPENDT = "";
		ResultSet L_rstRSSET;
		java.sql.Timestamp L_tmsDTM;
		String L_strCPRCD,L_strTPRDS,L_strLINNO,L_strCYLNO,L_strRUNNO;		
		try
		{
			L_strSQLQRY = "SELECT LT_LOTNO,LT_PENDT,LT_RCLNO,LT_LINNO,LT_CYLNO,LT_RUNNO,LT_TPRCD,LT_PRDCD,LT_CPRCD,LT_CLSFL,PR_PRDDS FROM PR_LTMST,CO_PRMST WHERE LT_TPRCD = PR_PRDCD AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = "+ "'"+strPRDTP.trim() + "'";
			L_strSQLQRY += " AND LT_LOTNO = " + "'" + strLOTNO.trim() + "'";
			L_strSQLQRY += " AND LT_RCLNO = " + "'" + strRCLNO.trim() + "'";
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					setMSG("",'N');
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
					{
						if((strTSTTP.equals(strCOTST_fn)) || (strTSTTP.equals(strBGTST_fn)))
						{
                            L_tmsDTM = L_rstRSSET.getTimestamp("LT_PENDT");
                            if(L_tmsDTM !=null)
                            {
							    strPENDT = M_fmtLCDTM.format(L_tmsDTM);
						    	strVLDLOT ="YES";
                            }
							if(L_tmsDTM == null)
							{
								setMSG("Lot Has Not Been Closed .. ",'E');
								strVLDLOT ="NO";
								return false;
							}
							if(nvlSTRVL(strPENDT,"").length() == 0)
							{
								setMSG("Lot Has Not Been Closed .. ",'E');
								return false;
							}
						}						
					}					
					strCLSFL = nvlSTRVL(L_rstRSSET.getString("LT_CLSFL"),"");
					strSTSFL = strCLSFL;
					L_strCPRCD = nvlSTRVL(L_rstRSSET.getString("LT_CPRCD"),"");	
					strTPRCD = nvlSTRVL(L_rstRSSET.getString("LT_TPRCD"),"");
					strPRDCD = nvlSTRVL(L_rstRSSET.getString("LT_PRDCD"),"");
					L_strTPRDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
					L_strLINNO = nvlSTRVL(L_rstRSSET.getString("LT_LINNO"),"");
					L_strCYLNO = nvlSTRVL(L_rstRSSET.getString("LT_CYLNO"),"");
					L_strRUNNO = nvlSTRVL(L_rstRSSET.getString("LT_RUNNO"),"");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
					{
						txtLINNO.setText(L_strLINNO);
						//for Material Origin						
						L_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
						L_strSQLQRY += " where CMT_CGMTP='SYS'AND CMT_CGSTP = 'QCXXMOR'";
						L_strSQLQRY += " AND CMT_MODLS LIKE '%" + strTSTTP + "%'";
						M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(M_rstRSSET != null)
						{
						   	if(M_rstRSSET.next())
							{
							      txtMORTP.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""));
						    }
							M_rstRSSET.close();
						}						
					}
					txtCYLNO.setText(L_strCYLNO);
					txtRUNNO.setText(L_strRUNNO);
					txtTPRDS.setText(L_strTPRDS);
					txtCPRDS.setText(getPRDDS(L_strCPRCD));
			  	}
				else
				{
					//setMSG("Lot No. Not Found..",'E');
					txtRCLNO.setText("");
					txtTSTNO.setText("");
					txtMORTP.setText("");
					txtLINNO.setText("");
					txtCYLNO.setText("");
					txtRUNNO.setText("");
					txtTPRDS.setText("");
					txtREMDS.setText("");
					tblITMDT.clrTABLE();
					return false;
				}
				L_rstRSSET.close();
			}			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(txtTSTTP.getText().trim().equals(strCOTST_fn))
				{				   
					setMSG("",'N');
					if((strCLSFL.equals("9"))||(strCLSFL.equals("4")))
					{
					    if(cl_dat.M_strUSRTP_pbst.indexOf("AU1") < 0)
						{
					        if(cl_dat.M_strUSRTP_pbst.indexOf("QC010") < 0)
    					   	{
    					  		setMSG("",'N');							
    							setMSG("Lot has been classified, can not modify..",'E');
								((JTextField)(tblITMDT.cmpEDITR[TBL_QPRCD])).setEditable(false);
								((JTextField)(tblITMDT.cmpEDITR[TBL_QPRVL])).setEditable(false);								 
    							cl_dat.M_btnSAVE_pbst.setEnabled(false);
    						}
						}
					}
					else
					{
					    cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
				}			   
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getLOTDET");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch the test Details for the given test Lot number.
	 */
	private void getDATA()
	{		
		try
		{	
		    java.sql.Timestamp L_tmsTEMP;		
			String L_TMP ="",L_TSTDT="",L_strSTRDTM="";
			if(tblITMDT.isEditing())
				tblITMDT.getCellEditor().stopCellEditing();
			tblITMDT.setRowSelectionInterval(0,0);
			tblITMDT.setColumnSelectionInterval(0,0);
	   		this.setCursor(cl_dat.M_curWTSTS_pbst);		          
	   		strRCLNO = txtRCLNO.getText().trim();
			String L_strSQLQRY="";
	   		L_strSQLQRY  = "Select * from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '" + strQCATP.trim() + "'";
			L_strSQLQRY += " AND PS_TSTTP='" + txtTSTTP.getText().trim()+"'";
			L_strSQLQRY += " AND PS_LOTNO = '"+txtLOTNO.getText().trim() +"'";
			L_strSQLQRY += " AND PS_RCLNO = '"+strRCLNO.trim() +"'";
			if(strTSTTP.trim().equals(strGRTST_fn))
			{				
				L_TSTDT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()));			
				L_strSQLQRY += " AND PS_TSTDT = '"+L_TSTDT.trim()+"'";
			}
			L_strSQLQRY += " AND isnull(PS_STSFL,'') <> 'X'";	
//			System.out.println(L_strSQLQRY);
	   		M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
		
			txtMORTP.setText("");
			txtTSTNO.setText("");
			txtLINNO.setText("");
			txtTSTBY.setText("");
			txtREMDS.setText("");
            if(M_rstRSSET !=null)
			{
				if (M_rstRSSET.next())
				{				
					L_tmsTEMP = M_rstRSSET.getTimestamp("PS_TSTDT");
					if(L_tmsTEMP!= null)
					{
    	   				L_strSTRDTM = M_fmtLCDTM.format(L_tmsTEMP);
    				    txtTSTDT.setText(L_strSTRDTM.substring(0,10));
    				    txtTSTTM.setText(L_strSTRDTM.substring(11,16));
					}
					txtMORTP.setText(nvlSTRVL(M_rstRSSET.getString("PS_MORTP"),""));
					txtTSTNO.setText(nvlSTRVL(M_rstRSSET.getString("PS_TSTNO"),""));
	   				txtLINNO.setText(M_rstRSSET.getString("PS_LINNO"));
	   				for(int i=0;i<intQPRCT;i++)
					{
	   	 				L_TMP = "PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL";
	   					tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString(L_TMP),""),i,TBL_QPRVL);
	   				}
	    			txtTSTBY.setText(M_rstRSSET.getString("PS_TSTBY"));				     
					ResultSet L_rstRSSET;
					strOREMDS="";
					L_strSQLQRY = "Select RM_REMDS from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '" + strQCATP.trim() + "'AND RM_TSTTP='" ;
					L_strSQLQRY += txtTSTTP.getText().trim()+"' AND RM_TSTNO = '";
					L_strSQLQRY += txtTSTNO.getText().trim()+"'";
					L_strSQLQRY += " AND isnull(RM_STSFL,'') <> 'X'";
					try
					{
						L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(L_rstRSSET !=null)
						{
							if(L_rstRSSET.next())				
								txtREMDS.setText(L_rstRSSET.getString("RM_REMDS"));										
							strOREMDS = txtREMDS.getText().trim();
							L_rstRSSET.close();
						}
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"getPSRMK");
					}  	   				
	   			}
				else
                    setMSG("Can not find Record ... ",'E');				
				M_rstRSSET.close();
			}			
		}
		catch (Exception L_EX)
		{
			this.setCursor(cl_dat.M_curDFSTS_pbst);		  
			setMSG(L_EX,"getDATA");   
		}	
		this.setCursor(cl_dat.M_curDFSTS_pbst);		  
	}
	
	/**
	 * Super class method overrided here to inhance its functionality, to perform 
	 * Database operations.
	 */
	void exeSAVE()
	{					
		try
		{
			if(!vldDATA())
				return ;
			else
				setMSG("",'E');
			cl_dat.M_flgLCUPD_pbst = true;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				int L_intTSTNO = 0;
				String L_strTEMP ="";
				try
				{
					if(strSTSFL.equals(String.valueOf('0'))&&  txtTSTTP.getText().trim().equals(strCOTST_fn))
						strSTSFL = String.valueOf('1');
					else if(strSTSFL.equals(String.valueOf('X'))&&  txtTSTTP.getText().trim().equals(strCOTST_fn))
						strSTSFL = String.valueOf('1');
					if(strSTSFL.equals(String.valueOf('0')) &&  txtTSTTP.getText().trim().equals(strBGTST_fn))
						strSTSFL = String.valueOf('2');
					else if(strSTSFL.equals(String.valueOf('X')) &&  txtTSTTP.getText().trim().equals(strBGTST_fn))
						strSTSFL = String.valueOf('2');
					if(strSTSFL.equals(String.valueOf('2'))&& txtTSTTP.getText().trim().equals(strCOTST_fn))
						strSTSFL = String.valueOf('3');
					else if(strSTSFL.equals(String.valueOf('X'))&& txtTSTTP.getText().trim().equals(strCOTST_fn))
						strSTSFL = String.valueOf('3');
					if(strSTSFL.equals(String.valueOf('1'))&& txtTSTTP.getText().trim().equals(strBGTST_fn))
						strSTSFL = String.valueOf('3');
					else if(strSTSFL.equals(String.valueOf('1'))&& txtTSTTP.getText().trim().equals(strBGTST_fn))
						strSTSFL = String.valueOf('3');
					//System.out.println("strSTSFL : "+strSTSFL);
	   				
					strCODCD = txtTSTTP.getText().trim() + "_" + cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim();
							
					//System.out.println("strCODCD : "+strCODCD);
					L_strTEMP = cl_dat.getPRMCOD("CMT_CCSVL","D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",strCODCD);
					L_intTSTNO = Integer.valueOf(L_strTEMP).intValue()+1;										
					strTSTNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()
						+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
						+ String.valueOf(L_intTSTNO);
					//System.out.println("strTSTNO : "+strTSTNO);
					txtTSTNO.setText(strTSTNO);
					
					M_strSQLQRY = "INSERT INTO QC_PSMST (PS_CMPCD,PS_SBSCD,PS_QCATP,PS_TSTTP,PS_TSTNO,PS_TSTDT,PS_MORTP,PS_PRDTP,PS_LOTNO,PS_RCLNO,PS_LINNO,";
					/*for(int i=0;i< intQPRCT;i++)
					{
					    if(txtTSTTP.getText().trim().equals(strGRTST_fn))
					    {
						    if(tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim().length() != 0)							
							    M_strSQLQRY += "PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL"+",";		
					    }
					    else
					        M_strSQLQRY += "PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL"+",";		
				    }*/
					for(int i=0;i< intROWNO; i++)
					{			    
						if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))
							M_strSQLQRY += "PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL"+",";
						//System.out.println("PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL"+",");
					}							
					M_strSQLQRY += "PS_TSTBY,PS_ADDTM,PS_ADDBY,PS_STSFL,PS_TRNFL,PS_LUSBY,PS_LUPDT)"; 
					M_strSQLQRY += " VALUES(";
                    M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+ M_strSBSCD.trim()+"',";
					M_strSQLQRY += "'"+ strQCATP.trim()+"',";
					M_strSQLQRY += "'"+ txtTSTTP.getText().trim()+"',";
					M_strSQLQRY += "'"+ strTSTNO.trim()+"','";					
					M_strSQLQRY += M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"',";
					M_strSQLQRY += "'"+ txtMORTP.getText().trim()+"',";
					M_strSQLQRY += "'"+ strPRDTP.trim()+"',";
					M_strSQLQRY += "'"+ txtLOTNO.getText().trim()+"',";
					M_strSQLQRY += "'"+ strINRCL_fn +"',";
					M_strSQLQRY += "'"+ txtLINNO.getText().trim()+"',";		   
					/*
					for(int i=0;i<intQPRCT;i++)
					{
						if(txtTSTTP.getText().trim().equals(strGRTST_fn))
						{
							if(tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim().length() != 0)							
								M_strSQLQRY +=	tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim()+",";		
						}
						else
						{
							if(tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim().length() != 0)
								M_strSQLQRY +=tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim()+",";
							else
								M_strSQLQRY +="0,";		
						}			  
					}	*/
					for(int i=0;i< intROWNO; i++)
					{			    
						if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))
						{
							if(tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim().length() != 0)
								M_strSQLQRY +=tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim()+",";
							else
								M_strSQLQRY +="0,";
						}
					}												
					M_strSQLQRY += "'"+txtTSTBY.getText().trim()+"',";					
					M_strSQLQRY += "'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst +" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
				    M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";					
					M_strSQLQRY += getUSGDTL("PS",'I',strSTSFL.trim()) + ")";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			        if(cl_dat.M_flgLCUPD_pbst)
			        {
    					M_strSQLQRY = "update PR_LTMST set ";
    					M_strSQLQRY +=" LT_TRNFL ='0',";
    					M_strSQLQRY +=" LT_CLSFL ='"+ strSTSFL.trim()+"',";
    					M_strSQLQRY +=" LT_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
    					M_strSQLQRY +=" LT_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
    					M_strSQLQRY +=" where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP ='"+ strPRDTP.trim()+"'";
    					M_strSQLQRY +=" AND LT_LOTNO ='"+ strLOTNO.trim()+"'";	 			  
    					M_strSQLQRY +=" AND LT_RCLNO ='"+ strINRCL_fn.trim()+"'";	 					
    					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
			        }
			        if(cl_dat.M_flgLCUPD_pbst)
			        {
    						M_strSQLQRY = "";
							if(chkRMMST(cl_dat.M_strCMPCD_pbst,strQCATP.trim(),strTSTTP.trim(),txtTSTNO.getText().trim()))
							{
    							M_strSQLQRY = "update QC_RMMST set " ;
    							M_strSQLQRY += " RM_REMDS = '" +txtREMDS.getText().trim()+"',";
    							M_strSQLQRY += " RM_TRNFL = '0',";
    							M_strSQLQRY += " RM_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
    							M_strSQLQRY += " RM_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";						
    							M_strSQLQRY += " where RM_CMPCD = '" +cl_dat.M_strCMPCD_pbst+"' ";
								M_strSQLQRY += " and RM_QCATP = '" +strQCATP.trim()+"' ";
    							M_strSQLQRY += " and RM_TSTTP = '" +strTSTTP.trim()+"' ";
    							M_strSQLQRY += " and RM_TSTNO = '" +txtTSTNO.getText().trim()+"'";
							}
							else
							{
								if(!txtREMDS.getText().trim().equals(""))
								{
    								M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
    								M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
									M_strSQLQRY += "'" +strQCATP.trim()+"',";
    								M_strSQLQRY += "'" +strTSTTP.trim()+"',";
    								M_strSQLQRY += "'" +txtTSTNO.getText().trim()+"',";
    								M_strSQLQRY += "'" +txtREMDS.getText().trim()+"',";
    								M_strSQLQRY += "'0',";
    								M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
    								M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' )";						
								}
							}
							if(M_strSQLQRY.length()>0)
							{
    	 						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");;
    							setMSG("Updating the Remark ..",'N');
							}
			        }
			        if(cl_dat.M_flgLCUPD_pbst)
			        {
    					M_strSQLQRY ="Update CO_CDTRN SET CMT_CCSVL ='"+txtTSTNO.getText().substring(3)+"'";
    					M_strSQLQRY += " WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='QCXXTST'";
    					M_strSQLQRY += " AND CMT_CODCD ='"+txtTSTTP.getText()+"_"+cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()+"'";					
    					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");				
			        }			        
				}
				catch(Exception L_SE)
				{
					setMSG(L_SE,"exeSAVE");
				}	
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				String L_COLNM;
				String L_TSTDT ="";
				ResultSet L_RSLRMK;	   	   
				cl_dat.M_flgLCUPD_pbst = true;
				strTSTTP = txtTSTTP.getText().trim();
				strTSTNO = txtTSTNO.getText().trim();
				L_TSTDT = txtTSTDT.getText().trim() + " "+txtTSTTM.getText().trim();
            
				M_strSQLQRY = "update QC_PSMST set ";  							
				/*if(intQPRCT > 0)
				for(int i=0;i<intQPRCT;i++)
				{
					if(tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim().length() != 0)
					{
						L_COLNM = "PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL";
						    M_strSQLQRY += L_COLNM +" ="+ tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim()+",";
					}
					else if(tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim().length() != 0)
					{
					    L_COLNM = "PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL";
					    M_strSQLQRY += L_COLNM +" =null,";
					}
				}	*/
				for(int i=0;i< intROWNO; i++)
				{			    
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))
					{
						M_strSQLQRY += "PS_"+tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim()+"VL = ";		
						if(tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim().length() != 0)
							M_strSQLQRY += tblITMDT.getValueAt(i,TBL_QPRVL).toString().trim()+",";
						else
							M_strSQLQRY += " =null,";
					}
				}
				
				M_strSQLQRY += getUSGDTL("PS",'U',null);
				/*M_strSQLQRY += "PS_TRNFL ='0',";
				M_strSQLQRY += "PS_STSFL =' ',";
				M_strSQLQRY += "PS_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "PS_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				 */
				M_strSQLQRY +=" where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+ strQCATP.trim()+"'";
				M_strSQLQRY +=" AND PS_TSTTP ='"+ strTSTTP.trim()+"'";
				M_strSQLQRY +=" AND PS_LOTNO ='"+ strLOTNO.trim()+"'";
				M_strSQLQRY +=" AND PS_RCLNO ='"+ strINRCL_fn.trim()+"'";
//				System.out.println(M_strSQLQRY);
				M_strSQLQRY +=" AND PS_TSTDT ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				setMSG("Updating the test details.. ",'N');
			
				
    			M_strSQLQRY = "";
				if(chkRMMST(cl_dat.M_strCMPCD_pbst,strQCATP.trim(),strTSTTP.trim(),txtTSTNO.getText().trim()))
				{
					M_strSQLQRY = "update QC_RMMST set ";
					M_strSQLQRY +=" RM_REMDS ='"+txtREMDS.getText().trim()+"',";
					M_strSQLQRY +=" RM_TRNFL ='0',";
					M_strSQLQRY +=" RM_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +=" RM_LUPDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"; 
					M_strSQLQRY +=" WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"+ strQCATP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTTP ='"+ strTSTTP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTNO ='"+ txtTSTNO.getText().trim()+"'";						
				}
				else
				{
					if(!txtREMDS.getText().trim().equals(""))
					{
							M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
							M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'" +strQCATP.trim()+"',";
							M_strSQLQRY += "'" +strTSTTP.trim()+"',";
							M_strSQLQRY += "'" +txtTSTNO.getText().trim()+"',";
							M_strSQLQRY += "'" +txtREMDS.getText().trim()+"',";
							M_strSQLQRY += "'0',";
							M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' )";						
					}
				}
				if(M_strSQLQRY.length()>0)
				{
    	 			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");;
    				setMSG("Updating the Remark ..",'N');
				}
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
			{
			    
			    String L_TSTDT ="";
				strTSTTP = txtTSTTP.getText().trim();
				strTSTNO = txtTSTNO.getText().trim();
				L_TSTDT = txtTSTDT.getText().trim() + " "+txtTSTTM.getText().trim();
               	cl_dat.M_flgLCUPD_pbst = true;
			    M_strSQLQRY = "UPDATE QC_PSMST SET "
			                + getUSGDTL("PS",'U',"X");
			    M_strSQLQRY +=" where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+ strQCATP.trim()+"'";
				M_strSQLQRY +=" AND PS_TSTTP ='"+ strTSTTP.trim()+"'";
				M_strSQLQRY +=" AND PS_LOTNO ='"+ strLOTNO.trim()+"'";
				M_strSQLQRY +=" AND PS_RCLNO ='"+ strINRCL_fn.trim()+"'";
				M_strSQLQRY +=" AND PS_TSTDT ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
				if(strSTSFL.equals(String.valueOf('3'))&&  txtTSTTP.getText().trim().equals(strCOTST_fn))
					strSTSFL = String.valueOf('2');
				else if(strSTSFL.equals(String.valueOf('X'))&&  txtTSTTP.getText().trim().equals(strCOTST_fn))
					strSTSFL = String.valueOf('2');
				if(strSTSFL.equals(String.valueOf('3')) &&  txtTSTTP.getText().trim().equals(strBGTST_fn))
					strSTSFL = String.valueOf('1');
				else if(strSTSFL.equals(String.valueOf('X')) &&  txtTSTTP.getText().trim().equals(strBGTST_fn))
					strSTSFL = String.valueOf('1');
				if(strSTSFL.equals(String.valueOf('2'))&& txtTSTTP.getText().trim().equals(strBGTST_fn))
					strSTSFL = String.valueOf('0');
				else if(strSTSFL.equals(String.valueOf('X'))&& txtTSTTP.getText().trim().equals(strBGTST_fn))
					strSTSFL = String.valueOf('0');
				if(strSTSFL.equals(String.valueOf('1'))&& txtTSTTP.getText().trim().equals(strCOTST_fn))
					strSTSFL = String.valueOf('0');
				else if(strSTSFL.equals(String.valueOf('X'))&& txtTSTTP.getText().trim().equals(strCOTST_fn))
					strSTSFL = String.valueOf('0');
			
				M_strSQLQRY = "update PR_LTMST set ";
		        M_strSQLQRY += "LT_TRNFL ='0',";
		        M_strSQLQRY += "LT_CLSFL ='"+strSTSFL+"',";
		        M_strSQLQRY += "LT_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
		        M_strSQLQRY += "LT_LUPDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"; 
		        M_strSQLQRY += " WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP ='"+strPRDTP+"'";
		        M_strSQLQRY += " AND LT_LOTNO ='"+txtLOTNO.getText().trim()+"'";
		        M_strSQLQRY += " AND LT_RCLNO ='"+txtRCLNO.getText().trim()+"'";
		        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		        if(!strOREMDS.equals(""))
				{
					M_strSQLQRY = "UPDATE QC_RMMST SET ";
					M_strSQLQRY += getUSGDTL("RM",'U',"X");					
					M_strSQLQRY +=" WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '" + strQCATP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTTP = '" + strTSTTP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTNO = '" + txtTSTNO.getText().trim() + "'";					
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		    
				}	
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				clrCOMP_1();
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
				setENBL(false);
			}
			else
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'N');
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
	}
	

	/**
	 */
	boolean chkRMMST(String LP_CMPCD,String LP_QCATP,String LP_TSTTP,String LP_TSTNO)
	{
		try
		{
			String L_strSQLQRY = "select count(*) RM_RECCT FROM QC_RMMST where RM_CMPCD = '"+LP_CMPCD+"' and RM_QCATP = '"+LP_QCATP+"' and RM_TSTTP = '"+LP_TSTTP+"' and RM_TSTNO = '"+LP_TSTNO+"'";
			//System.out.println(L_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			int L_intRECCT = 0;
			if(L_rstRSSET != null && L_rstRSSET.next())
				{L_intRECCT = L_rstRSSET.getInt("RM_RECCT"); L_rstRSSET.close();}
			if(L_intRECCT == 0)
				return false;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
		return true;
	}
	/**
	 * Method to perform validy check of the Data entered, Before inserting 
	 *new data in the data base.
	 */
	boolean vldDATA()
	{
		try
		{
			String L_strSQLQRY="";
			if(txtTSTTP.getText().trim().length()< 4)
			{
				setMSG("Invalid Test Type .. Please Enter Test Type..",'E');
				return false;
			}
			if(txtLOTNO.getText().trim().length()< 8)
			{
				setMSG("Invalid Lot Number .. Please Enter Lot Number..",'E');
				return false;
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{			
				if(txtRCLNO.getText().trim().length()==0)
				{
					setMSG("Reclassification No. cannot be Blank, Please Enter it..",'E');
					return false;
				}
				if(txtTSTDT.getText().trim().length()==0)
				{
					setMSG("Test Date cannot be Blank, Please Enter Test Date..",'E');
					return false;
				}
				if(txtTSTTM.getText().trim().length()==0)
				{
					setMSG("Test Time cannot be Blank, Please Enter Test Time..",'E');
					return false;
				}
				if(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()).compareTo(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))>0)
				{
					setMSG("Date Time can not be greater than current Date Time..",'E');
					txtTSTDT.requestFocus();
					return false;
				}	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					L_strSQLQRY = "Select PS_LOTNO,PS_TSTNO,PS_TSTDT,PS_STSFL from QC_PSMST";//PS_TSTTP,
					L_strSQLQRY +=" WHERE PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+strQCATP.trim() +"'";
					L_strSQLQRY +=" AND PS_TSTTP ='"+txtTSTTP.getText().trim() +"'";		
					L_strSQLQRY +=" AND PS_LOTNO = '"+txtLOTNO.getText().trim()+"'";
					L_strSQLQRY +=" AND PS_RCLNO ='"+txtRCLNO.getText().trim() +"'";
					L_strSQLQRY +=" AND isnull(PS_STSFL,'') <> 'X'";						
					if(txtTSTTP.getText().equals(strGRTST_fn))
					if(txtTSTDT.getText().trim().length() >0) // For validation of Grab test after entering the time.
						L_strSQLQRY +=" AND PS_TSTDT ='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"'";
					L_strSQLQRY += " order by PS_TSTTP,PS_LOTNO,PS_TSTDT";										
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						if(M_rstRSSET.next())
						{
							setMSG(txtTSTDS.getText().trim()+" has already been entered ..",'E');
							txtTSTNO.setText(strTSTNO);
							M_rstRSSET.close();
							return false;
						}
						M_rstRSSET.close();
					}
				}
				boolean L_flgCHKFL= false;
				for(int i=0; i<tblITMDT.getRowCount(); i++)
				{				
					if (tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{				
						L_flgCHKFL= true;
						break;
					}				
				}			
				if((L_flgCHKFL== false) && (strOREMDS.equals(txtREMDS.getText().trim())))
				{
					setMSG("Data is not Update as Data is not changed for Modification..",'E');
					return false;
				}
			}		
			if(tblITMDT.isEditing())
				tblITMDT.getCellEditor().stopCellEditing();
			tblITMDT.setRowSelectionInterval(0,0);
			tblITMDT.setColumnSelectionInterval(0,0);					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}	
	/**
	 * Method to get the quality parameter value Range.
	 */
	private void getQPRRNG()
	{
		ResultSet L_rstRSSET;
		String L_strTEMP ="";
	    String L_strSQLQRY = "";
        L_strSQLQRY = "Select QP_NPFVL,QP_NPTVL,QP_CMPFL from CO_QPMST where QP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QP_QCATP ='"; 
        L_strSQLQRY += strQCATP.trim();
		L_strSQLQRY += "' AND QP_PRDCD ='";
        L_strSQLQRY += strTPRCD.trim()+ "' AND QP_QPRCD = '";
        L_strSQLQRY += tblITMDT.getValueAt(intROWCT,TBL_QPRCD).toString().trim() + "'";
		L_strSQLQRY += " AND QP_SRLNO ='"+strDFLSRL_fn+"'";
		L_strSQLQRY += " AND QP_ENDDT is null ";
		L_strTEMP = getQPRCND(strQCATP.trim(),txtTSTDT.getText().trim(),tblITMDT.getValueAt(intROWCT,TBL_QPRCD).toString().trim(),strTPRCD.trim());
		if(L_strTEMP.trim().length() > 0)
			L_strSQLQRY += " AND "+ L_strTEMP; 
		L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
		try
		{
			if(L_rstRSSET.next())
			{
                dblNPFVL = L_rstRSSET.getDouble("QP_NPFVL");
                dblNPTVL = L_rstRSSET.getDouble("QP_NPTVL");
				strCMPTP = L_rstRSSET.getString("QP_CMPFL");
				if(strCMPTP.equalsIgnoreCase(String.valueOf('Y'))&& dblNPTVL == 0)
						dblNPTVL = 999999;
				setMSG("Range for  " + tblITMDT.getValueAt(intROWCT,TBL_QPRCD).toString().trim()+ "  is:  " + dblNPFVL + "  to  " + dblNPTVL,'N');
			}
			else			
				setMSG(" ",'N');            
		}
		catch (Exception L_EX)
		{
          setMSG(L_EX,"getQPRRNG");   
        }
	}
	/**
	 * Method to validiate the quality parameter Values entered by the user.
	 */
	private void vldQPRNG()
	{
	   	if(strCMPTP.equalsIgnoreCase(String.valueOf('Y')))
		{
			dblNPVL = Double.valueOf(tblITMDT.getValueAt(intROWCT,TBL_QPRVL).toString().trim()).doubleValue();
			if((dblNPVL < dblNPFVL) || (Double.valueOf(tblITMDT.getValueAt(intROWCT,TBL_QPRVL).toString().trim()).doubleValue()> dblNPTVL))			
				 jopOPTPN.showConfirmDialog(null,"Out of specification Range", "Confirm",JOptionPane.YES_NO_OPTION);			
	    }  
	}
	/**
	 * Method to get the test date condiation i.e. test date must be between start date & end date
	 */
	public String getQPRCND(String P_strQCATP,String P_strTSTDT,String P_strQPRCD,String P_strPRDCD)
	{
		String L_strSTRDT ="",L_strENDDT="",L_strSQLQRY,L_strTEMP="";
		java.util.Date L_datTEMP;
		try
		{
			L_strSQLQRY = "Select QP_STRDT,QP_ENDDT from CO_QPMST where QP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QP_QCATP = "+"'"+P_strQCATP +"'";
			L_strSQLQRY += " AND QP_PRDCD ="+"'"+P_strPRDCD.trim()+"'"+" AND QP_QPRCD = "+"'"+P_strQPRCD +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{					
					L_datTEMP = M_rstRSSET.getDate("QP_ENDDT");
					if(L_datTEMP !=null)
						L_strENDDT =  M_fmtLCDAT.format(L_datTEMP);
					if(L_strENDDT.trim().length() == 0)
						L_strENDDT = cl_dat.M_strLOGDT_pbst;	
					
					L_datTEMP = M_rstRSSET.getDate("QP_STRDT");					
					if(L_datTEMP !=null)
						L_strSTRDT = M_fmtLCDAT.format(L_datTEMP);
					if (M_fmtLCDAT.parse(P_strTSTDT.trim()).compareTo(M_fmtLCDAT.parse(L_strSTRDT))>=0)								
					{
						if (M_fmtLCDAT.parse(P_strTSTDT.trim()).compareTo(M_fmtLCDAT.parse(L_strENDDT)) < 0)						
						{
							// Test Date is lying between the spec. start date AND end date.
							L_strTEMP = " QP_STRDT = '"+L_datTEMP+"'";//cl_dat.M_fmtDBDAT(cl_dat.M_fmtLCDAT.parse(L_datTEMP));//(L_strSTRDT);
							return L_strTEMP;
						}					 
					}
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getQPRCND");
		}
		return "";
	}
	/**
	 * Method to get the reclassified product Description.
	 * @ param P_strPRDCD String argument to pass the classified product code.
	 */
	private String getPRDDS(String P_strPRDCD)
	{
		try
		{
			String L_strSQLQRY = "";
			ResultSet L_rstRSSET;
			L_strSQLQRY = "select PR_PRDDS from CO_PRMST where PR_PRDCD ='"+P_strPRDCD.trim() +"'";
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET!= null)
			{
		   		if(L_rstRSSET.next())			
					return nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");		    
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)
		{
	      setMSG(L_EX,"getPRDDS");
	    }
	     return "";
	}
	/**
	 */
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if((input == txtTSTTP)&&(txtTSTTP.getText().trim().length() == 4))
				{
                	M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " AND CMT_CGSTP = 'QCXXTST'";
					M_strSQLQRY += " AND CMT_CODCD = '";
					M_strSQLQRY += txtTSTTP.getText().trim() + "' AND CMT_NMP01 =2";	   						
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{
							strTSTDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							txtTSTDS.setText(strTSTDS);
							M_rstRSSET.close();														
							return true;
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Test Type..",'E');
							return false;
						}
					}
				}
				else if((input == txtLOTNO) && (txtLOTNO.getText().trim().length() == 8))
				{					   	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
					{													
						if(!txtTSTTP.getText().trim().equals(strGRTST_fn))
						{
							String L_strSQLQRY = "Select PS_LOTNO from QC_PSMST WHERE PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+strQCATP+"'";
							L_strSQLQRY +=" AND PS_TSTTP = '" + strTSTTP + "'";
							L_strSQLQRY += " AND PS_LOTNO = '"+ txtLOTNO.getText().trim() +"'";					
							M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
							if (M_rstRSSET != null)
							{  
								if(M_rstRSSET.next())
								{									
									M_rstRSSET.close();
									strVLDLOT = "NO";
									setMSG(txtTSTDS.getText().trim()+" Details already entered for this Lot Number..",'E');
									return false;
								}
								else
								{
									M_rstRSSET.close();
									strVLDLOT = "YES";								
								}								
							}
						}
						M_strSQLQRY = "Select LT_LOTNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '" + strPRDTP + "'";							
						M_strSQLQRY += " AND LT_LOTNO = '"+ txtLOTNO.getText().trim() +"'";												   
					}
					else
					{					
						M_strSQLQRY = "Select PS_LOTNO from QC_PSMST WHERE PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+strQCATP+"'";
						M_strSQLQRY +=" AND PS_TSTTP = '" + strTSTTP + "'";
						M_strSQLQRY += " AND PS_LOTNO = '"+ txtLOTNO.getText().trim() +"'";
					}					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{							
							M_rstRSSET.close();
							strVLDLOT = "YES";
							return true;
						}
						else
						{
							M_rstRSSET.close();
							strVLDLOT = "NO";
							setMSG("Invalid Lot Number..",'E');
							return false;
						}
					}					
				}
				else if(input == txtTSTBY) 
				{					
					if(txtTSTBY.getText().trim().length() == 3)
					{
						txtTSTBY.setText(txtTSTBY.getText().toUpperCase());
						M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
						M_strSQLQRY += "'A"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'QCXXTST' AND CMT_CODCD = '" + txtTSTBY.getText().trim() + "'";
						M_strSQLQRY += "AND CMT_CHP01 ='"+strQCATP +"'";
						//System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);		
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{							
								M_rstRSSET.close();
								return true;
							}
							else
							{
								M_rstRSSET.close();
								setMSG("You are not Authorised to perform this Test..",'E');
								return false;
							}
						}				
					}
				}
				else if(((input == txtTSTTM)||(input == txtTSTDT)) && (txtTSTDT.getText().trim().length()>0) && (txtTSTTM.getText().trim().length()>0)) 
				{
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) && (txtTSTTP.getText().trim().equals(strGRTST_fn)))
					{
						String L_strSQLQRY = "Select PS_LOTNO from QC_PSMST WHERE PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ='"+strQCATP+"'";
						L_strSQLQRY +=" AND PS_TSTTP = '" + strTSTTP + "'";
						L_strSQLQRY += " AND PS_LOTNO = '"+ txtLOTNO.getText().trim() +"'";
						L_strSQLQRY += " AND PS_TSTDT = '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim() +" "+ txtTSTTM.getText().trim())) +"'";						
				        M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{															
								M_rstRSSET.close();								
								setMSG("Grab Test Details for this Date,Time & Lot are already entered ..",'E');
								return false;
							}						
						}					
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;	
		}
	}	
}
