/*
System Name   : Lebortory Information Management System
Program Name  : Data Entry Screen For Various water Analysis
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 20 JUNE 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
import java.sql.ResultSet;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTable;import java.util.Hashtable;import javax.swing.JComboBox;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;

/** <P><PRE >
<b>System Name :</b> Labortoty Information Management System.
 
<b>Program Name :</b> Entry Screen for Water Analysis.

<b>Purpose :</b> This Entry Screen is used to enter the details of various water Test 
carried Out at different Time for different water Type & Test Type.
			
List of tables used :
Table Name      Primary key                          Operation done
                                               Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
QC_WTTRN      WTT_QCATP,WTT_TSTTP,WTT_TSTNO,      #        #       #       #	
              WTT_TSTDT,WTT_WTRTP
QC_RMMST      RM_QCATP,RM_TSTTP,RM_TSTNO          #        #       #       #
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD                #       #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name    Table name    Type/Size      Description
----------------------------------------------------------------------------------------------------------
txtTSTTP    CMT_CODCD      CO_CDTRN      VARCHAR(15)    Test Type
txtTSTDS    CMT_CODDS      CO_CDTRN      VARCHAR(30)    Test Description
txtTSTNO    CMT_CCSVL      CO_CDTRN      VARCHAR(4)     Test Number
txtTSTDT,   WTT_TSTDT      QC_WTTRN      Timestamp      Test date & time
txtTSTTM    
txtREMDS    RM_REMDS       QC_RMMST      VARCHAR((200)   Remark
txtWTRTP    WTT_WTRTP      QC_WTTRN      VARCHAR((2)     Water type	
txtWTRDS    CMT_CODDS      CO_CDTRN      VARCHAR((30)    Water code desc
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description         Display Columns                  Table Name
----------------------------------------------------------------------------------------------------------
txtTSTTP    Test Type & Description	    CMT_CODCD,CMT_CODDS              CO_CDTRN
                                        (SYS/QCXXTST),NMP01 = 4
txtTSTNO    Test No, Water code,        WTT_TSTNO,WTT_WTRTP,WTT_TSTDT,   QC_WTTRN
            Test Date & Test Details    WTT_TSTTP
txtWTRTP    Water Code & description    CMT_CODCD,CMT_CODDS              CO_CDTRN
                                        (SYS/QCXXWTP)
                                        CMT_MODLS like '%Test type %'
----------------------------------------------------------------------------------------------------------
<I><B>
Logic:</B>
Each water test has some quality parameters. & these parameters change as per 
new standards adopted. 
System manages these parameters of each test & makes Latest parameters available.
According to the new standards, some new parameters are added and some unwanted
are removed for specific Water Test.
Hence to generate report every time list of parameters are generated dynamically as :-
   1) Latest parameters are fetched from the database associated with perticular water Test.
   2) These parameters are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    "WTT_"+ Vector.elementAt(i)+"VL"  
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

<b>Test Parameter Details are taken from CO_QSMST for conditions</b>
   1) QS_TSTLS like '% given Test Type %' ";
   

Test Details such as Numbers,QCA Type,Test Type,Test Report Type are taken from QC_WTTRN 
     1) WTT_TSTDT Between given Date range.
     2) AND WTT_TSTTP = '0401'

Remark is taken from QC_RMMST
     1) RM_QCATP = Given QCA Type
     2) AND RM_TSTTP ='0401' 
     3) AND RM_TSTNO = Test number fetched from QC_WTTRN For Conation
               i)WTT_QCATP = given QCA Type
               ii)AND WTT_TSTTP ='0401' 
               iii)AND DATE(WTT_TSTDT) = Current date.
Serial Number to add the Test Details for Current date (at first time) is generated as
Finacial Years Last integer + given QCA Type Code + "Last Test Number + 1"
i.e. Finacial year 2005, QCA Type Code "01" & Last Test Number 280
Hence the generated Test Number is 50100281.
Last Test Number is taken from CO_CDTRN
     cl_dat.getPRMCOD("CMT_CCSVL","DOC","QCXXTST",L_strCODCD);									
If Serial Number is already generated then it is Taken from QC_WTTRN for Conditation
     1) WTT_QCATP = Selected QCA Type
     2) AND WTT_TSTTP = Given Test Type.
     3) AND Date(WTT_TSTDT) = Current Date.

Water Type Code & Description are taken from CO_CDTRN for Condiation
     1) CMT_CGMTP = 'SYS' 
     2) AND CMT_CGSTP = 'QCXXWTP'
     3) AND CMT_MODLS like given Test Type Code.					`

Test details are stored & fetched from talbe WTT_WTTRN for given Test Type, Text Number,
Water Type, Date & Time

Validations :
    1) Test Type must be valid.
    2) Test Numnber must be valid.
    3) Data & Time entered must be smaller than the current dat & time.
    4) Water Type Code must be valid.</I>
*/

class qc_tewtm extends cl_pbase
{									/** JTextField to enter & display Test Type.*/
	private JTextField txtTSTTP;	/** JTextField to display Test Description.*/
	private JTextField txtTSTDS;	/** JTextField to enter & display Test Number.*/
	private JTextField txtTSTNO;	/** JTextField to enter & display Test Date.*/
	private JTextField txtTSTDT;	/** JTextField to enter & display Test Time.*/
	private JTextField txtTSTTM;	/** JTextField to enter & display Remark.*/
	private JTextField txtREMDS;	/** JTextField to enter & display Water Type.*/
	private JTextField txtWTRTP;	/** JTextField to display Water type Description.*/
	private JTextField txtWTRDS;	/** JTextField to enter Test Parameter values in the JTable. .*/
	private JTextField txtVALUE;	/** JTable to display quality parameter & also to enter & display quality parameter values.*/
	private cl_JTable tblITMDT;			
									/** Integer variable to represent the Check Flag Column.*/
	private int TBL_CHKFL = 0;		/** Integer variable to represent the Quality parameter Code Column.*/
	private int TBL_QPRCD = 1;		/** Integer variable to represent the Code Description Column.*/
	private int TBL_CODDS = 2;		/** Integer variable to represent the Unit of Measurement Column.*/
	private int TBL_UOMCD = 3;		/** Integer variable to represent the Values Column.*/
	private int TBL_VALUE = 4;		/** String variable for QCA Type.*/		
	private String strQCATP = "";	/** String variable for Old Remark fetched from DataBase.*/
	private String strOREMDS="";	/** Integer variable for Row Count.*/
	private int intROWCT;			/** Bollean variable to manage the Serial Number Generation.*/
	private boolean flgNEWNO = false;	
										/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();/** Boolean variable to manage the Remark fetchinfg only once for given date & test Type.*/
	private boolean flgFIRST = true;
	qc_tewtm()
	{
		super(2);
		try
		{
			setMatrix(20,8);			
			add(new JLabel("Test Type"),2,2,1,.8,this,'R');
			add(txtTSTTP = new TxtNumLimit(4),2,3,1,1,this,'L');
			add(new JLabel("Description"),2,4,1,.8,this,'R');
			add(txtTSTDS = new JTextField(),2,5,1,3,this,'L');						
			
			add(new JLabel("Test No."),3,2,1,.8,this,'R');
			add(txtTSTNO = new TxtNumLimit(8),3,3,1,1,this,'L');
			add(new JLabel("Date"),3,4,1,.8,this,'R');
			add(txtTSTDT = new TxtDate(),3,5,1,1,this,'L');
			add(new JLabel("Time"),3,6,1,.5,this,'R');
			add(txtTSTTM = new TxtTime(),3,7,1,.7,this,'L');
			
			add(new JLabel("Water Code"),4,2,1,.8,this,'R');
			add(txtWTRTP = new TxtNumLimit(2),4,3,1,1,this,'L');
			add(new JLabel("Description"),4,4,1,.8,this,'R');
			add(txtWTRDS = new JTextField(),4,5,1,3,this,'L');
			
			add(new JLabel("Remark"),5,2,1,.8,this,'R');
			add(txtREMDS = new JTextField(),5,3,1,5,this,'L');
			
			String[] L_COLHD = {"Select","Para.Code","Description","UOM","Value"};
      		int[] L_COLSZ = {50,100,175,100,125};	    				
			tblITMDT = crtTBLPNL1(this,L_COLHD,50,7,2,10.3,6,L_COLSZ,new int[]{0});	
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);									
			txtTSTTP.setInputVerifier(objINPVR);
			txtTSTNO.setInputVerifier(objINPVR);
			txtWTRTP.setInputVerifier(objINPVR);
			txtTSTDT.setInputVerifier(objINPVR);
			txtTSTTM.setInputVerifier(objINPVR);
			tblITMDT.setCellEditor(TBL_VALUE, txtVALUE = new TxtNumLimit(6.1));
			txtVALUE.addKeyListener(this);txtVALUE.addFocusListener(this);
			
			setENBL(false);									
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");		
		}
	}
	/**
	 * Method super class method overrided to enable & disable components
	 * according to the requriement.
	 * @param P_flgSTAT boolean argument to pass state of the Component.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			return ;
		txtTSTTP.setEnabled(true);		
		tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_QPRCD].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_CODDS].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_VALUE].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
		{							
			txtWTRTP.setEnabled(true);
			txtTSTDT.setEnabled(true);
			txtTSTTM.setEnabled(true);
			txtREMDS.setEnabled(true);
			//tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
			tblITMDT.cmpEDITR[TBL_VALUE].setEnabled(true);						
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{	
			txtTSTNO.setEnabled(true);
			txtREMDS.setEnabled(true);				
			//tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
			tblITMDT.cmpEDITR[TBL_VALUE].setEnabled(true);			
		}
		else
			txtTSTNO.setEnabled(true);		
		txtTSTTP.requestFocus();
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);			
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
				{
					setMSG("select an option",'N');
					setENBL(false);
				}					
				else
				{		
				    strQCATP = M_strSBSCD.substring(2,4);					
					flgFIRST = true;
					txtTSTTP.requestFocus();
					setMSG("Enter Test Type OR Press F1 to select from list..",'N');
					setENBL(false);
				}				
			}										
			if(M_objSOURC == txtTSTTP)
			{
				flgFIRST = true;				
				if(txtTSTDT.getText().length()== 0)
					txtTSTDT.setText(cl_dat.M_strLOGDT_pbst); 
				if(txtTSTTM.getText().length()== 0)
				txtTSTTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
			}			
			if(M_objSOURC == txtTSTDT)
			{
				flgFIRST = true;
				txtREMDS.setText("");
				strOREMDS="";
				if((txtTSTTP.getText().trim().length()>0)&&(txtTSTDT.getText().trim().length()>0)&&(txtREMDS.getText().trim().length()==0))
				{									
					M_strSQLQRY = "SELECT RM_REMDS FROM QC_RMMST WHERE RM_QCATP ='"+strQCATP
						+"' AND RM_TSTTP ='"+ txtTSTTP.getText().trim()+"' AND RM_TSTNO = "
						+"(SELECT DISTINCT WTT_TSTNO FROM QC_WTTRN WHERE WTT_QCATP ='"+strQCATP
						//+"' AND WTT_TSTTP ='"+txtTSTTP.getText().trim()+"' AND DATE(WTT_TSTDT) ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						+"' AND WTT_TSTTP ='"+txtTSTTP.getText().trim()+"' AND CONVERT(varchar,WTT_TSTDT,103) ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTSTDT.getText().trim()))+"')";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
					if(M_rstRSSET != null)
					{						
						if(M_rstRSSET.next())
						{
							txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));									
							strOREMDS = txtREMDS.getText().trim();							
						}
						M_rstRSSET.close();
					}
					flgFIRST= false;
				}
			}				
			if(M_objSOURC == txtWTRTP)
			{
				strOREMDS="";
				if((txtTSTTP.getText().trim().length()>0)&&(txtTSTDT.getText().trim().length()>0) &&(flgFIRST == true))
				{								
					M_strSQLQRY = "SELECT RM_REMDS FROM QC_RMMST WHERE RM_QCATP ='"+strQCATP
					+"' AND RM_TSTTP ='"+ txtTSTTP.getText().trim()+"' AND RM_TSTNO = "
					+"(SELECT DISTINCT WTT_TSTNO FROM QC_WTTRN WHERE WTT_QCATP ='"+strQCATP
					+"' AND WTT_TSTTP ='"+ txtTSTTP.getText().trim()+"' AND CONVERT(varchar,WTT_TSTDT,103) ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTSTDT.getText().trim()))+"')";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
					if(M_rstRSSET != null)
					{						
						if(M_rstRSSET.next())
						{
							txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));									
							strOREMDS = txtREMDS.getText().trim();							
						}
						M_rstRSSET.close();
						flgFIRST= false;
					}					
				}
			}						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{		
		super.focusGained(L_FE);
		if(M_objSOURC == tblITMDT.cmpEDITR[TBL_VALUE])
	    {				
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
			{								
				if(tblITMDT.getSelectedRow() >= intROWCT)
					((JTextField)tblITMDT.cmpEDITR[TBL_VALUE]).setEditable(false);
				else
					((JTextField)tblITMDT.cmpEDITR[TBL_VALUE]).setEditable(true);				
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);			
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC == txtTSTTP)			// Test Type
				{
					txtTSTDS.setText("");
					txtREMDS.setText("");
					M_strHLPFLD = "txtTSTTP";
					cl_dat.M_flgHELPFL_pbst = true;										
					String L_ARRHDR[] = {"Test Type","description"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'QCXXTST'";
					M_strSQLQRY += " AND CMT_NMP01 = 4";
					if(txtTSTTP.getText().length() > 0)
						M_strSQLQRY += " AND CMT_CODCD like '"+ txtTSTTP.getText().trim()+"%'";
					cl_hlp	(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");					
				}
				else if(M_objSOURC == txtTSTNO)		// Test No
				{	
					if(txtTSTTP.getText().trim().length()==0)
					{
						setMSG("Please Enter Test Type First..",'E');
						return;
					}					
					M_strHLPFLD = "txtTSTNO";
					cl_dat.M_flgHELPFL_pbst = true;										
					txtREMDS.setText("");
					String L_ARRHDR[] = {"Test No","Water Code","Test Date","Test Type"};
					M_strSQLQRY = "Select WTT_TSTNO,WTT_WTRTP,WTT_TSTDT,WTT_TSTTP from QC_WTTRN";
					M_strSQLQRY += " where WTT_QCATP = '" + strQCATP + "'";
					M_strSQLQRY += " AND WTT_TSTTP = '" + txtTSTTP.getText().trim() + "'";
					M_strSQLQRY += " and isnull(WTT_STSFL,'') <> 'X'";
					if(txtTSTNO.getText().length() > 0)
						M_strSQLQRY += " AND WTT_TSTNO like '"+ txtTSTNO.getText().trim()+"%'";
					M_strSQLQRY += " order by WTT_TSTDT desc";																	
					
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");						
				}
				else if(M_objSOURC == txtWTRTP)		// Water Code
				{
					txtWTRDS.setText("");
					//strOREMDS="";
					if(txtTSTTP.getText().trim().length()==0)
					{
						setMSG("Please Enter Test Type at First..",'E');
						return;
					}					
					M_strHLPFLD = "txtWTRTP";
					cl_dat.M_flgHELPFL_pbst = true;											
					String L_ARRHDR[] = {"Water Code","description"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'QCXXWTP' and ";
					M_strSQLQRY += " CMT_MODLS like '%" + txtTSTTP.getText().trim() + "%'";					
					if(txtWTRTP.getText().length() > 0)
						M_strSQLQRY += " AND CMT_CODCD like '"+ txtWTRTP.getText().trim()+"%'";
					
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");					
				}
			}										
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{						
				if(M_objSOURC == txtTSTTP)					// Test Type
				{
					txtTSTDS.setText("");					
					if(txtTSTTP.getText().trim().length()==0)					
						setMSG("Test Type Cannot be blank, Please Enter Test Type..",'N');											
					else
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							setMSG("Please Enter Date..",'N');
							txtTSTDT.requestFocus();
						}
						else
						{
							setMSG("Please Enter Test Number..",'N');
							txtTSTNO.requestFocus();
						}					
					}										
				}			
				if((M_objSOURC == txtTSTNO) && (txtTSTNO.getText().trim().length() == 8))					// Test Type
				{
					if(txtWTRTP.getText().trim().length()>0)
					{
						try
						{			
							M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS'";
							M_strSQLQRY += " AND CMT_CGSTP='QCXXWTP'";
							M_strSQLQRY += " AND CMT_MODLS like '%" + txtTSTTP.getText().trim() + "%'";
							M_strSQLQRY += " AND CMT_CODCD = '" + txtTSTTP.getText().trim() + "'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET!= null)
							{
								if(M_rstRSSET.next())						
									txtWTRDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));							
								M_rstRSSET.close();
							}
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"vldWTRTP");
						}				
					}
					getQPRVL(strQCATP,txtTSTTP.getText().trim(),txtTSTNO.getText().trim(),txtWTRTP.getText().trim());					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					{
						txtTSTTP.setEnabled(false);
						txtTSTNO.setEnabled(false);
					}
					if(txtTSTNO.getText().trim().length() == 0)					
						setMSG("Test Number cannot be Blank..",'E');					
				}
				if(M_objSOURC == txtTSTDT)
				{
					if(txtTSTDT.getText().trim().length()==0)					
						setMSG("Please Enter Test Date, As Test Date cannot be Blank..",'E');											
					else
					{
						setMSG("Please Enter Test Time..",'N');
						txtTSTTM.requestFocus();
					}
				}
				if(M_objSOURC == txtTSTTM)
				{
					if(txtTSTTM.getText().trim().length()== 0)
						setMSG("Please Enter Test Time, As Test Time cannot be Blank..",'E');					
					else
					{
						setMSG("Please Enter Water Code OR Press F1 to select From list..",'N');
						txtWTRTP.requestFocus();
					}
				}				
				if(M_objSOURC == txtWTRTP)
				{
					if(txtWTRTP.getText().trim().length()==0)					
						setMSG("Please Enter Water Type, As Water Type cannot be Blank..",'E');						
					else
					{
						setMSG("Please Enter Remarks if any..",'N');
						txtREMDS.requestFocus();
						tblITMDT.requestFocus();
					}
				}
				if(M_objSOURC == txtREMDS)
    			{								
    				tblITMDT.setRowSelectionInterval(0,0);				
    				tblITMDT.setColumnSelectionInterval(TBL_VALUE,TBL_VALUE);				
    				tblITMDT.editCellAt(0,TBL_VALUE);				
    				tblITMDT.cmpEDITR[TBL_VALUE].requestFocus();				
    				setMSG("Please Enter Test Parameter Values.. ",'N');				
    			}				
			}				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}	
	/**
	 * Super Class Method overrided here to execute help for F1 key press.	 
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtTSTTP"))
			{
				txtTSTTP.setText(cl_dat.M_strHLPSTR_pbst);
				txtTSTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				initTABLE(txtTSTTP.getText().trim());
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(txtTSTDT.getText().length()==0)
						txtTSTDT.setText(cl_dat.M_strLOGDT_pbst); 
					if(txtTSTTM.getText().length()==0)
						txtTSTTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());									
				}
				cl_dat.M_flgHELPFL_pbst = false;
			}
			if(M_strHLPFLD.equals("txtTSTNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtTSTNO.setText(cl_dat.M_strHLPSTR_pbst);								
				String strTEMP = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim();				
				txtTSTDT.setText(strTEMP.substring(0,10));				
				txtTSTTM.setText(strTEMP.substring(11).trim());				
				txtWTRTP.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());						
			}
			if(M_strHLPFLD.equals("txtWTRTP"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtWTRTP.setText(cl_dat.M_strHLPSTR_pbst);
				txtWTRDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());				
				txtREMDS.requestFocus();			
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}	
	
	/**
	 * Super class method overrided here to inhance its functionality, to perform 
	 * Database operations.
	 */
	void exeSAVE()
	{					
		String L_strTSTNO = "";		
		String L_strTSTDT = ""; 			
		try
		{				
			if(!vldDATA())			
				return ;			
			else
				setMSG("",'E');		
            cl_dat.M_flgLCUPD_pbst	= true; 
			L_strTSTDT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim() + " " + txtTSTTM.getText().trim()));												
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{					
				L_strTSTNO = genTSTNO();
															
				this.setCursor(cl_dat.M_curWTSTS_pbst);									
				M_strSQLQRY = "Insert into QC_WTTRN(WTT_SBSCD,WTT_QCATP,WTT_TSTTP,";
				M_strSQLQRY += "WTT_TSTNO,WTT_TSTDT,WTT_WTRTP";				
				for(int i = 0; i < intROWCT; i++)
				{
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						if(tblITMDT.getValueAt(i,TBL_QPRCD).toString().length() >0)
				    		M_strSQLQRY += ",WTT_" +  tblITMDT.getValueAt(i,1) + "VL";
				}
				M_strSQLQRY += ",WTT_STSFL,WTT_TRNFL,WTT_LUSBY,WTT_LUPDT) values (";
				M_strSQLQRY += "'" + M_strSBSCD + "',";								
				M_strSQLQRY += "'" + strQCATP + "',";
				M_strSQLQRY += "'" + txtTSTTP.getText().trim() + "',";
				M_strSQLQRY += "'" + L_strTSTNO + "',";
				M_strSQLQRY += "'" + L_strTSTDT + "',";
				M_strSQLQRY += "'" + txtWTRTP.getText().trim() + "',";								
				for(int i = 0; i < intROWCT; i++)
				{
					if(tblITMDT.getValueAt(i,TBL_QPRCD).toString().length() >0)
					{    					
						if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(tblITMDT.getValueAt(i,TBL_VALUE).toString().trim().length()>0)
    							M_strSQLQRY += String.valueOf(tblITMDT.getValueAt(i,TBL_VALUE)) + ",";
    					    else 
    					        M_strSQLQRY += String.valueOf(tblITMDT.getValueAt(i,TBL_VALUE)) + "null,";
						}
					}
				}				
				M_strSQLQRY += getUSGDTL("WTT",'I',"") + ")";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");						
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(flgNEWNO)
					{
						// Update the Last No. in codes transaction
						M_strSQLQRY ="Update CO_CDTRN SET CMT_CCSVL ='"+txtTSTNO.getText().substring(3)+"'";
						M_strSQLQRY += " WHERE CMT_CGMTP ='DOC' and CMT_CGSTP ='QCXXTST' AND CMT_CODCD ='"+txtTSTTP.getText()+"_"+cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()+"'";						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}				
			}									
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{				
				M_strSQLQRY = "Update QC_WTTRN set ";				
				for(int i = 0; i < intROWCT; i++)
				{
					//if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().trim().equals("true"))
					if(tblITMDT.getValueAt(i,TBL_QPRCD).toString().trim().length() != 0)
					{			
						if(tblITMDT.getValueAt(i,4) !=null)
                        {
                            if(tblITMDT.getValueAt(i,4).toString().equals(""))
							    M_strSQLQRY += "WTT_" + tblITMDT.getValueAt(i,TBL_QPRCD).toString() + "VL = " + null + ",";
					        else
					   		M_strSQLQRY += "WTT_" + tblITMDT.getValueAt(i,TBL_QPRCD).toString() + "VL = " + tblITMDT.getValueAt(i,4).toString() + ",";
                        }
                        else
                            M_strSQLQRY += "WTT_" + tblITMDT.getValueAt(i,TBL_QPRCD).toString() + "VL = " + null + ",";

					}
				}						
				M_strSQLQRY += getUSGDTL("WTT",'U',"")
				+ " where WTT_QCATP = '" + strQCATP + "'"
				+ " AND WTT_TSTTP = '" + txtTSTTP.getText().trim() + "'"
				+ " AND WTT_TSTNO = '" + txtTSTNO.getText().trim() + "'"	
				+ " AND WTT_TSTDT = '" + L_strTSTDT +"'"
				+ " AND WTT_WTRTP = '" + txtWTRTP.getText().trim() + "'";				
                 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
                
			}									
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				M_strSQLQRY = "Update QC_WTTRN set "				
				+ getUSGDTL("WTT",'U',"X");
				M_strSQLQRY += " where WTT_QCATP = '" + strQCATP + "'";
				M_strSQLQRY += " AND WTT_TSTTP = '" + txtTSTTP.getText().trim()+"'";
				M_strSQLQRY += " AND WTT_TSTNO = '" + txtTSTNO.getText().trim()+ "'";	
				M_strSQLQRY += " AND WTT_TSTDT = '" + L_strTSTDT +"'";
				M_strSQLQRY += " AND WTT_WTRTP = '" + txtWTRTP.getText().trim() + "'";							
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}			
			addREMDS();            
			if(cl_dat.exeDBCMT("exeSAVE"))
			{                
				clrCOMP();
				setENBL(false);
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
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
	 * Method to initialse the Details table with Test codes & descriptions
	 * @param P_strTSTTP String argument to pass the Test Type.
	 */
	private void initTABLE(String P_strTSTTP)
	{
		ResultSet L_rstRSSET;
		intROWCT = 0;		
		try
		{	
			tblITMDT.clrTABLE();
			if(tblITMDT.isEditing())
				tblITMDT.getCellEditor().stopCellEditing();
			tblITMDT.setRowSelectionInterval(0,0);
			tblITMDT.setColumnSelectionInterval(0,0);			
			/*M_strSQLQRY = "select SUBSTRING(CMT_CODCD,1,3) TS_QPRCD,CMT_CODDS,CMT_CCSVL,CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'QCXXQPR' and ";
			M_strSQLQRY += " CMT_MODLS like '%" + P_strTSTTP.trim() + "%' ";
			M_strSQLQRY += " order by CMT_CHP02";			 */
            M_strSQLQRY = "select SUBSTRING(QS_QPRCD,1,3) TS_QPRCD,QS_QPRDS,QS_UOMCD from CO_QSMST ";
			M_strSQLQRY += " where QS_TSTLS like '%" + P_strTSTTP.trim() + "%' ";
			M_strSQLQRY += " order by QS_ORDBY";			 

			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					tblITMDT.setValueAt(L_rstRSSET.getString("TS_QPRCD"),intROWCT,1);
					tblITMDT.setValueAt(L_rstRSSET.getString("QS_QPRDS"),intROWCT,2);
					tblITMDT.setValueAt(L_rstRSSET.getString("QS_UOMCD"),intROWCT,3);
					intROWCT++;					
				}			
				L_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"initTABLE");
		}
	}	
	
	/**
	 * Method to get the quality parameter values for given test type & test number
	 * @param P_strQCATP String argument to pass QCA Type.
	 * @param P_strTSTTP String argument to pass Test Type.
	 * @param P_strTSTNO String argument to pass Test Number.
	 * @param P_strWTRTP String argument to pass water Type.
	 */
	private void getQPRVL(String P_strQCATP,String P_strTSTTP,String P_strTSTNO,String P_strWTRTP)
	{
		ResultSet L_rstRSSET;
		try
		{			
			if(tblITMDT.isEditing())
				tblITMDT.getCellEditor().stopCellEditing();
			tblITMDT.setRowSelectionInterval(0,0);
			tblITMDT.setColumnSelectionInterval(0,0);
			
			M_strSQLQRY = "Select CMT_CODDS";			
			for(int i = 0; i < intROWCT; i++)			
				M_strSQLQRY += ",WTT_" +  tblITMDT.getValueAt(i,1) + "VL";			
			M_strSQLQRY += " from QC_WTTRN,CO_CDTRN where";
			M_strSQLQRY += " WTT_QCATP = '" + P_strQCATP + "'";
			M_strSQLQRY += " AND WTT_TSTTP = '" + P_strTSTTP + "'";
			M_strSQLQRY += " AND WTT_TSTNO = '" + P_strTSTNO + "'";			
			M_strSQLQRY += " AND WTT_WTRTP = '" + P_strWTRTP + "'";
			M_strSQLQRY += " AND CMT_CGMTP = 'SYS' AND CMT_CGSTP='QCXXWTP'";
			M_strSQLQRY += " AND CMT_MODLS like '%" + P_strTSTTP + "%'";			
			M_strSQLQRY += " AND CMT_CODCD = '" + P_strWTRTP + "'";
			M_strSQLQRY += " AND WTT_TSTDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim())) + "'";												  
			M_strSQLQRY += " AND isnull(WTT_STSFL,'') <> 'X'";			
			
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if (L_rstRSSET != null)
			{
				if(L_rstRSSET.next())
				{					
					txtWTRDS.setText(L_rstRSSET.getString("CMT_CODDS"));
					for(int i = 0; i < intROWCT; i++)
						tblITMDT.setValueAt(L_rstRSSET.getString(i+2),i,TBL_VALUE);
				}
				L_rstRSSET.close();
			}
			// To display the Remark description
			strOREMDS="";
			M_strSQLQRY = "Select RM_REMDS from QC_RMMST";
			M_strSQLQRY += " where RM_QCATP = '" + P_strQCATP + "'";
			M_strSQLQRY += " and RM_TSTTP = '" + P_strTSTTP + "'";
			M_strSQLQRY += " and RM_TSTNO = '" + P_strTSTNO + "'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if (M_rstRSSET != null)
			{								
				if(M_rstRSSET.next())
				{					
					strOREMDS = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
					txtREMDS.setText(strOREMDS);					
				}
				M_rstRSSET.close();
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getQPRVL");
		}
	}
	
	/*
	* Method to generate the new Test No if not exists.
	*/
	private String genTSTNO()
	{
		String L_strTSTNO = "",L_strTEMP="";
		int L_intTSTNO =0;
		String L_strCODCD ="";
		try
		{	
			flgNEWNO = false;
			M_strSQLQRY = "Select distinct WTT_TSTNO from QC_WTTRN where ";
			M_strSQLQRY += " WTT_QCATP = '" + strQCATP + "'";
			M_strSQLQRY += " AND WTT_TSTTP = '" + txtTSTTP.getText().trim() + "'";
			M_strSQLQRY += " AND CONVERT(varchar,WTT_TSTDT,103) = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTSTDT.getText().trim()))+"'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())				
					L_strTSTNO = nvlSTRVL(M_rstRSSET.getString("WTT_TSTNO"),"");									
				else
				{
					flgNEWNO = true;					
					L_strCODCD = txtTSTTP.getText().trim() + "_" + cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim();
					L_strTEMP = cl_dat.getPRMCOD("CMT_CCSVL","DOC","QCXXTST",L_strCODCD);					
					L_intTSTNO = Integer.parseInt(L_strTEMP)+1;
					
					L_strTSTNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()
							+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
							+ String.valueOf(L_intTSTNO);										
				}
				txtTSTNO.setText(L_strTSTNO);				
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genTSTNO");
			return L_strTSTNO;
		}		
		return L_strTSTNO;
	}
	
	/**
	 * Method to update the Remarks if new remark inserted or old remark updated.	
	 */
	private void addREMDS()
	{		
		try
		{			
			if((strOREMDS.length()>0) && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
			{
				M_strSQLQRY = "Update QC_RMMST set RM_STSFL = 'X',RM_TRNFL ='0' ";
				M_strSQLQRY += " where RM_QCATP = '" + strQCATP + "'";
				M_strSQLQRY += " and RM_TSTTP = '" + txtTSTTP.getText().trim() + "'";
				M_strSQLQRY += " and RM_TSTNO = '" + txtTSTNO.getText().trim() + "'";				
				System.out.println(M_strSQLQRY);
				if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				return;									
			}				
			if(!strOREMDS.equals(txtREMDS.getText().trim()))
			{				
				if(strOREMDS.equals(""))
				{					
					M_strSQLQRY = "Insert into QC_RMMST(RM_QCATP,RM_TSTTP,";
					M_strSQLQRY += "RM_TSTNO,RM_REMDS,RM_LUSBY,RM_TRNFL,RM_LUPDT) Values(";
					M_strSQLQRY += "'" + strQCATP + "',";
					M_strSQLQRY += "'" + txtTSTTP.getText().trim() + "',";
					M_strSQLQRY += "'" + txtTSTNO.getText().trim() + "',";
					M_strSQLQRY += "'" + txtREMDS.getText().trim() + "',";
					M_strSQLQRY += "'" + cl_dat.M_strUSRCD_pbst.trim() + "',";
					M_strSQLQRY += "'0',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim())) + "')";					
					System.out.println(M_strSQLQRY);
					if(cl_dat.M_flgLCUPD_pbst)						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");										
				}				
				else
				{
					if(txtREMDS.getText().trim().equals(""))
					{
						M_strSQLQRY = "Delete from QC_RMMST ";			
						M_strSQLQRY += " where RM_QCATP = '"+ strQCATP +"'";
						M_strSQLQRY += " AND RM_TSTTP = '"+ txtTSTTP.getText().trim() +"'";
						M_strSQLQRY += " and RM_TSTNO = '" + txtTSTNO.getText().trim() + "'";
					}
					else
					{
						M_strSQLQRY = "Update QC_RMMST set ";
						M_strSQLQRY += "RM_REMDS = '" + txtREMDS.getText().trim()+ "',";
						M_strSQLQRY += "RM_LUSBY = '" + cl_dat.M_strUSRCD_pbst.trim() + "',";
						M_strSQLQRY += "RM_TRNFL = '0',";
						M_strSQLQRY += "RM_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()))+"'";
						M_strSQLQRY += " where RM_QCATP = '" + strQCATP + "'";
						M_strSQLQRY += " and RM_TSTTP = '" + txtTSTTP.getText().trim()+ "'";
						M_strSQLQRY += " and RM_TSTNO = '" + txtTSTNO.getText().trim() + "'";								
					}					
					System.out.println(M_strSQLQRY);
					if(cl_dat.M_flgLCUPD_pbst)
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
				}
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"addREMDS");		
		}		
	}		
	
	/**
	 * Method to perform validy check of the Data entered, Before inserting 
	 *new data in the data base.
	 */
	boolean vldDATA()
	{			
		
		if(txtTSTTP.getText().trim().length()==0)
		{
			setMSG("Test Type cannot be Blank.. Please Enter Test Type..",'E');
			return false;
		}		
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			if(txtTSTNO.getText().trim().length()==0)
			{
				setMSG("Test Number cannot be Blank, Please Enter Test Number..",'E');
				return false;
			}
		}		
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{
			if(txtTSTDT.getText().trim().length()== 0)
			{
				setMSG("Test Date cannot be Blank, Please Enter Test Date..",'E');
				txtTSTDT.requestFocus();
				return false;
			}
			if(txtTSTTM.getText().trim().length()== 0)
			{
				setMSG("Test Time cannot be Blank, Please Enter Test Time..",'E');
				txtTSTTM.requestFocus();
				return false;
			}
			if(txtWTRTP.getText().trim().length()== 0)
			{
				setMSG("Water Type cannot be Blank, Please Enter Water Type..",'E');
				txtWTRTP.requestFocus();
				return false;
			}													
		}								
		if(tblITMDT.isEditing())
			tblITMDT.getCellEditor().stopCellEditing();
		tblITMDT.setRowSelectionInterval(0,0);
		tblITMDT.setColumnSelectionInterval(0,0);					
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{						
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{									
				if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{										
					if ((i>=intROWCT)&& ( ! tblITMDT.getValueAt(i,TBL_VALUE).toString().equals("")))
					{						
						setMSG("Values are not allowed in the Blank \nQuality parameter Code Row..",'E');											
						return false;
					}
				}
			}
		}		
		//if no row selected.
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{				
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
				setMSG("Data is not Updated as Data isnot changed for Modification..",'E');
				return false;
			}			
		}					
		return true;
	}	
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if((input == txtTSTTP) && (txtTSTTP.getText().trim().length() == 4))
				{						
					txtREMDS.setText("");
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'QCXXTST'";
					M_strSQLQRY += " AND CMT_NMP01 = 4";					
					M_strSQLQRY += " AND CMT_CODCD = '"+ txtTSTTP.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{	
							txtTSTDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));							
							initTABLE(txtTSTTP.getText().trim());
							M_rstRSSET.close();
							return true;								
						}
						else
						{
							M_rstRSSET.close();
							setMSG("No Data found ..",'E');
							return false;				
						}							
					}				
				}
				if((input == txtTSTNO) && (txtTSTNO.getText().trim().length() == 8))
				{					
					M_strSQLQRY = "Select WTT_TSTNO,WTT_WTRTP from QC_WTTRN";
					M_strSQLQRY += " where WTT_QCATP = '" + strQCATP + "'";
					M_strSQLQRY += " AND WTT_TSTTP = '" + txtTSTTP.getText().trim() + "'";
					M_strSQLQRY += " and isnull(WTT_STSFL,'') <> 'X'";				
					M_strSQLQRY += " AND WTT_TSTNO = '"+ txtTSTNO.getText().trim()+"'";	
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())						
						{							
							M_rstRSSET.close();
							return true;
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Test Number..",'E');						
							return false;
						}							
					}					
				}				
				if(input == txtWTRTP)
				{					
					if(txtWTRTP.getText().length() > 0)
					{								
						M_strSQLQRY = "select CMT_CODDS from CO_CDTRN";
						M_strSQLQRY += " where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'QCXXWTP'";
						M_strSQLQRY += " AND CMT_MODLS like '%" + txtTSTTP.getText().trim() + "%'";					
						M_strSQLQRY += " AND CMT_CODCD = '"+ txtWTRTP.getText().trim()+"'";											
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								txtWTRDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
								M_rstRSSET.close();							
								return true;
							}
							else
							{
								M_rstRSSET.close();
								setMSG("Enter valid Water Type..",'E');
								return false;
							}							
						}
					}					
				}
				if(input == txtTSTDT)
				{
					if(txtTSTDT.getText().trim().length()>0)						
					if (M_fmtLCDAT.parse(txtTSTDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
					{			    
						setMSG("Date Must be Samaller than Curren date.. ",'E');																		
						return false;
					}					
				}
				if(input == txtTSTTM)
				{
					if((txtTSTDT.getText().trim().length()>0) &&(txtTSTTP.getText().trim().length()>0) )
					{
						if(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
						{
							setMSG("Date & Time can not be greater than current Date & Time..",'E');				
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
			return true;	
		}
	}
}
	
