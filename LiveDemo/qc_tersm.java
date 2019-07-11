/*
System Name   : Lebortory Information Management System
Program Name  : Reactor Sample Analysis Entry Screen
Program Desc. : Screen to Enter & Modify Reactor Sample Test parameter Values.
Author        : Mr.S.R.Mehesare
Date          : 27th JUNE 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTable;import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.InputVerifier;import javax.swing.JComponent;
/**
<P><PRE style = font-size : 10 pt >
<b>System Name :</b> Lebortory Information Management System
 
<b>Program Name :</b> Reactor Sample Analysis Entry Screen

<b>Purpose :</b> Screen to Enter/Modify/Delete Reactor Sample Test Details.

List of tables used :
Table Name     Primary key                         Operation done
                                             Insert  Update  Query  Delete	
-----------------------------------------------------------------------------
QC_RSMST       
QC_RMMST       RM_QCATP,RM_TSTTP,RM_TSTNO                     #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                  #
-----------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name  Type/Size    Description
-----------------------------------------------------------------------------
txtTSTTP    RS_TSTTP       QC_RSMST    VARCHAR(2)   Test Type
txtTSTDs    RS_TSTDS       QC_RSMST    VARCHAR(2)   Test Type Description                      
txtTSTNO    RS_TSTNO       QC_RSMST    VARCHAR(8)   Test Number
txtTSTBY    RS_TSTBY       QC_RSMST    VARCHAR(3)   Tested by                
txtTSTDT    RS_TSTDT       QC_RSMST    Timestamp    Test Date & Time         
txtTSTRM    RM_REMDS       QC_RMMST    VARCHAR(100) Test Remark              
-----------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description      Display Columns        Table Name
-----------------------------------------------------------------------------
txtTSTTP    Test Type                RS_TSTTP               QC_RSMST
txtTSTNO    Test Number              RS_TSTNO               QC_RSMST
txtTSTBY    Tested by                RS_TSTBY               QC_RSMST
-----------------------------------------------------------------------------

<B>Queries:</b>
<I><B>To Insert New Record :</B> 
	To insert the record, Test Number is generated first
Last updated Serial Number is fetched from CO_CDTRN for condiations
1) CMT_CGMTP = "D"+cl_dat.M_strCMPCD_pbst
2) CMT_CGSTP = "QCXXTST"
3) CMT_CODCD = txtTSTTP.getText().trim()+"_"+ cl_dat.M_strFNNYR_pbst.substring(3) + M_strSBSCD.substring(0,2);
then this Serial number is incremented by one & appended to finanacial Year Char & QCA Type 2 Char + Serial Number of 5 Char.
as financial Year 6, QCA Type 01 for Polystyrene & Serial Number 210
then the Serial Number will be 61000211

Quality Parameter values are inserted in the Table QC_RSMST.

if Remark is enter then it is inserted in table QC_RMMST.

After All these Insertiations the last five charectors from Serial number is updated in table CO_CDTRN.

<B>To Update Records : </B>
For Modification, Quality Parameter values are updated in the Table QC_RSMST for condiatons
1) RS_QCATP = selected QCA Type.
2) AND RS_TSTTP = given Test Type.
3) AND RS_TSTNO = Given Test Number.

if Remark is Already present & it is modified then it is updated.

if Remark is not present & through modification Remark is entered then it is inserted in the QC_RMMST.

<B>To Delete Records : </B>
For the Effect of Deletation the Status Flag is Marked As 'X'.
If Quality Parameters are Marked As 'X' then Corresponding Remark is also marked as 'X';
    
</I>
Validations :
 - Test Type must be valid. 
 - For Modification & deletation Test Number must be valid.
 - To insert the Quality Parameter Values QCA Person must have Authority to perform Testing.
 - Test Date & Time ist be valid.
*/

public class qc_tersm extends cl_pbase
{									/** JtextField to enter & display Test Type.*/
	private JTextField txtTSTTP;	/** JtextField to display test Description.*/
	private JTextField txtTSTDS;	/** JtextField to enter & display Test Number.*/
	private JTextField txtTSTNO;	/** JtextField to enter & display initial of the Person whow did the Test.*/
	private JTextField txtTSTBY;	/** JtextField to enter & display Test Date.*/
	private JTextField txtTSTDT;	/** JtextField to enter & display Test Time.*/
	private JTextField txtTSTTM;	/** JtextField to enter & display Test Remark.*/
	private JTextField txtTSTRM;	/** JtextField to display Name of the Person whow did the Test.*/
	private JTextField txtTBYDS;	/** JtextField to enter Test Value.*/
	private JTextField txtVALUE;	/**	JTable to display the test values for different values. */
	private cl_JTable tblTSDTL;
									/**String Variable for Old Test Number.*/
	private String strOTSTNO ="";	/**String Variable for Old Test Remarks.*/
	private String strOTSTRM ="";	/**String Variable for Test Type.*/
	private String strTSTTP = "";	/**String Variable for Test Number.*/
	private String strTSTNO = "";	/**String Variable for QCA Type.*/
	private String strQCATP = "";	/**String Variable to hold the initial of the person whow completed the Test*/
	private String strTSTBY = "";		/** Final integer variable for Check Flag Column Index.*/
	private final int TBL_CHKFL = 0;	/** Final integer variable for Line Reactor Column Index.*/
	private final int TBL_LINRT = 1;	/** Final integer variable for Description Column Index.*/
	private final int TBL_DESC  = 2;	/** Final integer variable for Short Description Column Index.*/
	private final int TBL_SHRDS = 3;	/** Final integer variable for Test value Column Index.*/
	private final int TBL_TSTVL = 4;	
    private int intROWCT;
	private INPVF objINPVF = new INPVF();
	qc_tersm()
	{
		super(2);
		try
		{		
			setMatrix(20,8);			
			add(new JLabel("Test Type"),2,2,1,.7,this,'R');
			add(txtTSTTP = new TxtLimit(4),2,3,1,1,this,'L');			
			add(new JLabel("Description"),2,4,1,1,this,'R');		
			add(txtTSTDS = new TxtLimit(35),2,5,1,3,this,'L');		
			
			add(new JLabel("Test No."),3,2,1,.7,this,'R');
			add(txtTSTNO = new TxtLimit(8),3,3,1,1,this,'L');						
			add(new JLabel("Test Date/Time"),3,4,1,1,this,'R');
			add(txtTSTDT = new TxtDate(),3,5,1,1,this,'L');					
			add(txtTSTTM = new TxtTime(),3,6,1,1,this,'L');
			
			add(new JLabel("Tested By"),4,2,1,.7,this,'R');
			add(txtTSTBY = new TxtLimit(3),4,3,1,1,this,'L');
			add(new JLabel("Name"),4,4,1,1,this,'R');
			add(txtTBYDS = new JTextField(),4,5,1,2,this,'L');
				
			add(new JLabel("Remark"),5,2,1,.7,this,'R');
			add(txtTSTRM = new TxtLimit(80),5,3,1,5,this,'L');
		
			String[] L_strTSDHD = {"Select","Code","Line/Reactor","Description","Value (%SOL)"};
			int[] L_intTSTSZ = {50,80,175,150,100};
			tblTSDTL = crtTBLPNL1(this,L_strTSDHD,100,7,2,10.3,6,L_intTSTSZ,new int[]{0});
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			tblTSDTL.setCellEditor(TBL_TSTVL, txtVALUE = new TxtNumLimit(3.1));//JTextField
			txtVALUE.addKeyListener(this);txtVALUE.addFocusListener(this);
			txtTSTTP.setInputVerifier(objINPVF);
			txtTSTNO.setInputVerifier(objINPVF);
			txtTSTBY.setInputVerifier(objINPVF);
			txtTSTBY.setInputVerifier(objINPVF);			
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
	}
	/**
	 * Method to enable & disable the Components according to requriements
	 * @param P_flgSTAT boolean argument to pass the state of the variable.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		tblTSDTL.cmpEDITR[TBL_CHKFL].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)		
		{
			txtTSTTP.setEnabled(true);
			txtTSTTP.requestFocus();
			setMSG("Enter Test Type Or Press F1 To Select From List..",'N');
		}						
		else
		{
			clrCOMP();			
			tblTSDTL.clrTABLE();
			setMSG("Select an Option..",'N');
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
		{		
			txtTSTBY.setEnabled(true);
			txtTSTDT.setEnabled(true);
			txtTSTTM.setEnabled(true);
			txtTSTRM.setEnabled(true);
			tblTSDTL.cmpEDITR[TBL_TSTVL].setEnabled(true);
		}		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{	
			txtTSTNO.setEnabled(true);
			txtTSTRM.setEnabled(true);
			//tblTSDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
			tblTSDTL.cmpEDITR[TBL_TSTVL].setEnabled(true);
		}				
		else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))				
			txtTSTNO.setEnabled(true);		
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{				
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			{				
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				setMSG("Please Select an option ..",'N');
				setENBL(false);
			}		
			else
			{				
				strQCATP = M_strSBSCD.substring(2,4);				
				setMSG("Please Enter Test Type OR Press F1 to select from List..",'N');				
				setENBL(false);
				txtTSTTP.requestFocus();
				txtTSTTP.setText("0201");
			}
		}							
		else if(M_objSOURC==txtTSTTM)
		{			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				try
				{
					M_strSQLQRY = "Select * from QC_RSMST where ";
					M_strSQLQRY +=" RS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RS_QCATP ='"+strQCATP.trim()+"'";
					M_strSQLQRY +=" AND RS_TSTTP ='"+ txtTSTTP.getText().trim() +"'";
					M_strSQLQRY +=" AND RS_TSTDT ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim())) +"'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							setMSG("Data already exist for the given date and time..",'E');
							return;
						}
						M_rstRSSET.close();
					}	
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX," Fetching Existing Record for Duplication check");
				}	
			}	
		}			
		else if (M_objSOURC==txtTSTBY)
		{
			if(txtTSTBY.getText().trim().length() >0)
				txtTSTBY.setText(txtTSTBY.getText().trim().toUpperCase());
		}
		else if (M_objSOURC==txtTSTNO)
		{
			strOTSTNO = txtTSTNO.getText().trim();
			getTBLDT();
			getQPRVL();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				cl_dat.M_btnSAVE_pbst.requestFocus();	
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode() == L_KE.VK_ENTER)
		{          	
			if(M_objSOURC == txtTSTTP)
			{				
				if(txtTSTTP.getText().trim().length()>0)
				{					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						strTSTTP = txtTSTTM.getText();
						txtTSTDT.requestFocus();
						txtTSTDT.setText(cl_dat.M_strLOGDT_pbst);												
						setMSG("Please Enter Test Date..",'N');
					}
					else
					{			
						txtTSTNO.requestFocus();
						setMSG("Please Enter Test Number OR Press F1 to Select from List..",'N');
					}
				}
				else
					setMSG("Enter Test Type OR Press F1 To select From List..",'N');
			}
			if(M_objSOURC==txtTSTDT)
			{
				if(txtTSTDT.getText().trim().length()>0)
				{
					txtTSTTM.setEnabled(true);
					txtTSTTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					txtTSTTM.requestFocus();
					setMSG("Please Enter Test Time..",'N');					
				}
			}	
			if(M_objSOURC == txtTSTTM)
			{
				if(txtTSTTM.getText().trim().length()>0)
				{					
					txtTSTBY.setText(cl_dat.M_strUSRCD_pbst);
					txtTSTBY.requestFocus();
					setMSG("Please Enter the initial Of the Person Whow Completed The Tests..",'n');														
				}
			}							
			if(M_objSOURC==txtTSTNO)
		    {
				if(txtTSTNO.getText().trim().length()>0)
				{
					strTSTNO = txtTSTNO.getText();
					txtTSTRM.requestFocus();
					setMSG("Please Enter Test Number to get Test Details..",'N');
				}				
			}	
			if(M_objSOURC==txtTSTBY)
			{	
				if(txtTSTBY.getText().trim().length()>0)
				{
					strTSTNO = txtTSTNO.getText();
					txtTSTRM.requestFocus();
					setMSG("Please Enter Test Number to get Test Details..",'N');
				}								
			}			
			if(M_objSOURC == txtTSTRM)
			{								
				tblTSDTL.setRowSelectionInterval(0,0);				
				tblTSDTL.setColumnSelectionInterval(TBL_TSTVL,TBL_TSTVL);				
				tblTSDTL.editCellAt(0,TBL_TSTVL);				
				tblTSDTL.cmpEDITR[TBL_TSTVL].requestFocus();				
				setMSG("Please Enter Test Parameter Values.. ",'N');				
			}
		}	
		else if (L_KE.getKeyCode()== L_KE.VK_F1)		
		{			
			if(M_objSOURC==txtTSTTP)
			{
			 	setCursor(cl_dat.M_curWTSTS_pbst);
   			 	cl_dat.M_flgHELPFL_pbst = true;
			 	M_strHLPFLD = "txtTSTTP";			 	
   			 	M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
			 	M_strSQLQRY+= "'"+"SYS"+"' and CMT_CGSTP = '";
			 	M_strSQLQRY+= "QCXXTST"+"' and CMT_NMP01 = 1 and CMT_CODCD='0201'";			 
			 	if(txtTSTTP.getText().trim().length() > 0)
			 		M_strSQLQRY += " AND CMT_CODCD LIKE '"+txtTSTTP.getText().trim()+"%'";
			 	M_strSQLQRY+= "order by CMT_CODCD";			 	
			 	cl_hlp(M_strSQLQRY,1,1,new String[]{"Test Type","Description"},2,"CT");
			 	setCursor(cl_dat.M_curDFSTS_pbst);
			 }
			else if(M_objSOURC==txtTSTNO)
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtTSTNO";
				M_strSQLQRY  = "Select RS_TSTNO,RS_TSTDT from QC_RSMST where RS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RS_QCATP = '";
                M_strSQLQRY += strQCATP.trim() + "' and RS_TSTTP = '";
				M_strSQLQRY += txtTSTTP.getText().trim()+"'";
				if(txtTSTNO.getText().trim().length()>0)
			 		M_strSQLQRY +=" and RS_TSTNO like '" + txtTSTNO.getText().trim() + "%'";
				M_strSQLQRY += " and RS_STSFL <> 'X'";
                M_strSQLQRY += " order by RS_TSTDT DESC";       			 				 	
			 	cl_hlp(M_strSQLQRY,1,1,new String[]{"Test Number","Test Date and time"},2,"CT");					
				this.setCursor(cl_dat.M_curDFSTS_pbst);
             }
			 if(M_objSOURC==txtTSTBY)
			 {
				txtTBYDS.setText("");
			 	this.setCursor(cl_dat.M_curWTSTS_pbst);
			 	cl_dat.M_flgHELPFL_pbst = true;
			 	M_strHLPFLD = "txtTSTBY";
                M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
                M_strSQLQRY += "'A"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'QCXXTST' and CMT_CHP01 ='"+strQCATP +"'";
			 	M_strSQLQRY +=" order by CMT_CODCD";
			 	cl_hlp(M_strSQLQRY,1,1,new String[]{"Initial","Name"},2,"CT");
			 	this.setCursor(cl_dat.M_curDFSTS_pbst);
			 }          
		}
	}
	/**
	 * Method to execute the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtTSTTP"))
		{
			txtTSTTP.setText(cl_dat.M_strHLPSTR_pbst);
			txtTSTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			strTSTTP = txtTSTTP.getText();
		}
		else if(M_strHLPFLD.equals("txtTSTBY"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTSTBY.setText(cl_dat.M_strHLPSTR_pbst);
			txtTBYDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			strTSTBY = txtTSTBY.getText();
		}
		else if(M_strHLPFLD.equals("txtTSTNO"))
		{
	          cl_dat.M_flgHELPFL_pbst = false;
			  txtTSTNO.setText(cl_dat.M_strHLPSTR_pbst);			  
			  strTSTNO = txtTSTNO.getText();
			  strOTSTNO = strTSTNO;
		}
	}
	/**
	 * Method to perform addition, Modification & deletion of data from the data base.
	 */
	void exeSAVE()
	{		
		try
		{
			if(!vldDATA())
				return ;
			else
				setMSG("",'N');
			if(tblTSDTL.isEditing())
				tblTSDTL.getCellEditor().stopCellEditing();
			tblTSDTL.setRowSelectionInterval(0,0);
			tblTSDTL.setColumnSelectionInterval(0,0);
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{				
				String strCODCD =  txtTSTTP.getText().trim()+"_"+ cl_dat.M_strFNNYR_pbst.substring(3) + M_strSBSCD.substring(2,4);			
				String L_strTEMP = cl_dat.getPRMCOD("CMT_CCSVL","D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",strCODCD);
				int L_intTSTNO = Integer.valueOf(L_strTEMP).intValue()+1;														
				strTSTNO = String.valueOf(L_intTSTNO).toString();								
				strTSTNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()
					+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
					+ String.valueOf(L_intTSTNO);
				txtTSTNO.setText(strTSTNO);
								
				M_strSQLQRY = "Insert into qc_rsmst(RS_CMPCD,RS_SBSCD,RS_QCATP,RS_TSTTP,RS_TSTNO,RS_TSTDT,RS_TSTBY";
				for(int i=0;i<intROWCT;i++)
				{
					//if(tblTSDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))					
	                if(tblTSDTL.getValueAt(i,TBL_LINRT).toString().length() >0)					
                        	M_strSQLQRY += ","+"RS_" + tblTSDTL.getValueAt(i,TBL_LINRT).toString();					
				}					
				M_strSQLQRY +=",RS_TRNFL,RS_STSFL,RS_LUSBY,RS_LUPDT) values( ";
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
				M_strSQLQRY += "'"+M_strSBSCD+"',";
				M_strSQLQRY += "'"+strQCATP+"',";
				M_strSQLQRY += "'"+txtTSTTP.getText().trim()+"',";
				M_strSQLQRY += "'"+strTSTNO.trim()+"',";
				M_strSQLQRY += "'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim())) +"',";
				M_strSQLQRY += "'"+txtTSTBY.getText().trim()+"',";				
				for(int i=0;i<intROWCT;i++)
				{
					//if(tblTSDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))					
                    if(tblTSDTL.getValueAt(i,TBL_LINRT).toString().length() >0)	
                    {				
                        if((tblTSDTL.getValueAt(i,TBL_TSTVL).toString().equals("")))						
                            M_strSQLQRY += "null,";					
                         else
                            M_strSQLQRY += tblTSDTL.getValueAt(i,TBL_TSTVL).toString()+",";					
                    }
				}	
				M_strSQLQRY += getUSGDTL("RS",'I',"")+")";
				cl_dat.M_flgLCUPD_pbst = true;				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				setMSG("Adding the test record..",'N'); 
				
				if(txtTSTRM.getText().trim().length() >0)
				{
					M_strSQLQRY = "Insert into qc_rmmst(rm_cmpcd,rm_qcatp,rm_tsttp,rm_tstno,rm_remds,rm_trnfl,rm_stsfl,rm_lusby,rm_lupdt)values(";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+strQCATP+"',";
					M_strSQLQRY += "'"+txtTSTTP.getText().trim()+"',";
					M_strSQLQRY += "'"+txtTSTNO.getText().trim()+"',";
					M_strSQLQRY += "'"+txtTSTRM.getText().trim()+"',";
					M_strSQLQRY += getUSGDTL("RM",'I',"")+")";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
				}									
				M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CCSVL = '"+txtTSTNO.getText().substring(3) +"'";
				M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='QCXXTST' ";
				M_strSQLQRY += " AND CMT_CODCD = '"+txtTSTTP.getText().trim()+"_"+ cl_dat.M_strFNNYR_pbst.substring(3) + M_strSBSCD.substring(2,4) + "'";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");				
			}							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{				
				String L_strTEMP = "";
				if(!strOTSTNO.equals(txtTSTNO.getText().trim()))
				{
					setMSG("Modification of Test Number is not Allowed..",'N');					
					return;
				}
				//exeMODREC();	
				M_strSQLQRY = "Update qc_rsmst set ";		
				M_strSQLQRY +=" RS_TRNFL ='0',";				
				for(int i=0;i<intROWCT;i++)
				{
					//if(tblTSDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					if(tblTSDTL.getValueAt(i,TBL_LINRT).toString().length() >0)					
					{
						if((tblTSDTL.getValueAt(i,TBL_TSTVL).toString().equals("")))
							L_strTEMP = null;
						else
							L_strTEMP = tblTSDTL.getValueAt(i,TBL_TSTVL).toString();
							
						M_strSQLQRY += "RS_"+tblTSDTL.getValueAt(i,TBL_LINRT).toString()+"="+ L_strTEMP +",";
					}						
				}	
				M_strSQLQRY +=" RS_LUSBY ='"+ cl_dat.M_strUSRCD_pbst +"',";
				M_strSQLQRY +=" RS_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'";
				M_strSQLQRY +=" Where RS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RS_QCATP ='"+strQCATP.trim()+"'";
				M_strSQLQRY +=" AND RS_TSTTP ='"+txtTSTTP.getText().trim()+"'";
				M_strSQLQRY +=" AND RS_TSTNO ='"+txtTSTNO.getText().trim()+"'";
				cl_dat.M_flgLCUPD_pbst = true;				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				if(!strOTSTRM.equals(txtTSTRM.getText().trim()))
				{
					if(strOTSTRM.equals(""))//insert
					{
						M_strSQLQRY = "Insert into qc_rmmst(rm_cmpcd,rm_sbscd,rm_qcatp,rm_tsttp,rm_tstno,rm_remds,rm_trnfl,rm_stsfl,rm_lusby,rm_lupdt)values(";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'"+ M_strSBSCD.trim()+"',";
						M_strSQLQRY += "'"+ strQCATP.trim()+"',";
						M_strSQLQRY += "'"+ txtTSTTP.getText().trim()+"',";
						M_strSQLQRY += "'"+ txtTSTNO.getText().trim()+"',";
						M_strSQLQRY += "'"+ txtTSTRM.getText().trim()+"',";
						M_strSQLQRY += getUSGDTL("RM",'I',"")+")";						
					}
					else
					{
						M_strSQLQRY = "Update qc_rmmst set ";
						M_strSQLQRY += "RM_TRNFL ='0',"; 
						if(!txtTSTRM.getText().trim().equals(""))//update						
							M_strSQLQRY += "RM_REMDS ='"+txtTSTRM.getText().trim()+"',"; 
						else if(txtTSTRM.getText().trim().equals(""))//Delete						
							//M_strSQLQRY += "RM_STSFL ='X',";
							M_strSQLQRY += "RM_REMDS = '-',"; 
						M_strSQLQRY +=" RM_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY +=" RM_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'";
						M_strSQLQRY +=" Where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"+strQCATP.trim()+"'";
						M_strSQLQRY +=" AND RM_TSTTP ='"+txtTSTTP.getText().trim()+"'";
						M_strSQLQRY +=" AND RM_TSTNO ='"+txtTSTNO.getText().trim()+"'";						
					}
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					setMSG("Updating the Remark record..",'N'); 
				}									
			}	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				//exeDELREC();	
				cl_dat.M_flgLCUPD_pbst = true;
				M_strSQLQRY = "Update QC_RSMST set ";
				M_strSQLQRY += getUSGDTL("RS",'U',"X");				
				M_strSQLQRY +=" Where RS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RS_QCATP ='"+strQCATP.trim()+"'";
				M_strSQLQRY +=" AND RS_TSTTP ='"+ txtTSTTP.getText().trim() +"'";
				M_strSQLQRY +=" AND RS_TSTNO ='"+ txtTSTNO.getText().trim() +"'";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				setMSG("Deleting the test record..",'N'); 
				if(strOTSTRM.length() > 0)
				{
					M_strSQLQRY = "Update QC_RMMST set ";
					M_strSQLQRY += getUSGDTL("RM",'U',"X");
					M_strSQLQRY +=" Where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"+strQCATP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTTP ='"+ txtTSTTP.getText().trim() +"'";
					M_strSQLQRY +=" AND RM_TSTNO ='"+ txtTSTNO.getText().trim() +"'";				
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					setMSG("Deleting the Remark..",'N');
				}	
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				setMSG("Deleting the Remark record..",'N'); 
			}	
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exesave"))
				{	
					setMSG("Transaction Completed ..",'N');
					clrCOMP();					
					tblTSDTL.clrTABLE();
				}	
				else
					setMSG("Error in Saving/Retrieving",'E');
			}
		}		
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * Method to validate the data before execution of the	
	 */
	boolean vldDATA()
	{
		try
		{	
			if(txtTSTDT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid Test Date ..",'E');
				txtTSTDT.requestFocus();
				return false;
			}
			else if(txtTSTTM.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter valid Test Time ..",'E');
				txtTSTTM.requestFocus();
				return false;
			}					
			else if (M_fmtLCDAT.parse(txtTSTDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Test Date Can not be greater than login date .. ",'E');				
				return false;	
			}										
			return true;
		}
		catch(Exception L_EX)
		{
			return false;
		}
	}
	/**
	 * Method to get the quality parameters for different Tests.
	 */
	private void getQPRVL()
	{			
		try
	    {
	        java.sql.Timestamp L_tmsTSDTM;
			String L_strTSDTM="";
			String L_strTEMP="";
			M_strSQLQRY = "Select * from QC_RSMST where RS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RS_QCATP = '"+ strQCATP.trim() +"'";			
			M_strSQLQRY += " AND isnull(RS_STSFL,'') <> 'X'";
			M_strSQLQRY += " AND RS_TSTTP ='"+ txtTSTTP.getText().trim() +"' AND RS_TSTNO = '"+ txtTSTNO.getText().trim() +"'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			int i = 0;			
			if(M_rstRSSET !=null)
			{				
				if(M_rstRSSET.next())
				{										
					L_strTEMP ="";
					L_tmsTSDTM = M_rstRSSET.getTimestamp("RS_TSTDT");
					if(L_tmsTSDTM != null)
						L_strTSDTM = M_fmtLCDTM.format(L_tmsTSDTM);
					txtTSTDT.setText(L_strTSDTM.trim().substring(0,10));
					txtTSTTM.setText(L_strTSDTM.trim().substring(11).trim());
					txtTSTBY.setText(nvlSTRVL(M_rstRSSET.getString("RS_TSTBY"),""));					
					for(int j=0;j<tblTSDTL.getRowCount();j++)
					{
						L_strTEMP = tblTSDTL.getValueAt(j,TBL_LINRT).toString();
						if(!L_strTEMP.equals(""))
							tblTSDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("RS_"+L_strTEMP),""),j,TBL_TSTVL);
					}
						
				}
				M_rstRSSET.close();
			}			
			if(txtTSTBY.getText().trim().length() == 3)
			{				
				M_strSQLQRY = "select CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
				M_strSQLQRY += "'A"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'QCXXTST' AND CMT_CHP01 ='"+strQCATP +"'";
				M_strSQLQRY += " AND CMT_CODCD ='"+ txtTSTBY.getText().trim() +"'";				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())				
						txtTBYDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));				
					M_rstRSSET.close();
				}				
			}			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{				
				M_strSQLQRY = "Select RM_REMDS from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP.trim() +"'";
				M_strSQLQRY +=" AND RM_TSTTP = '"+ txtTSTTP.getText().trim() +"'";
				M_strSQLQRY +=" AND RM_TSTNO = '"+ txtTSTNO.getText().trim() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if (M_rstRSSET.next())
					{	
						strOTSTRM = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
						if(!strOTSTRM.equals("-"))
							txtTSTRM.setText(strOTSTRM);
					}						
					M_rstRSSET.close();
				}				
			}			
		}
		catch (Exception L_EX)
		{			
			setMSG(L_EX,"getQPRVL");
		}
	}
	/**
	 * Method to get Test parameter values.
	 */
	private void getTBLDT()
	{     
		if(tblTSDTL.isEditing())
				tblTSDTL.getCellEditor().stopCellEditing();
		tblTSDTL.setRowSelectionInterval(0,0);
		tblTSDTL.setColumnSelectionInterval(0,0);		
		String L_strCODCD="";
	    try
	     {			
	        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_SHRDS from CO_CDTRN where CMT_CGMTP ='SYS'";
			M_strSQLQRY +=" AND CMT_CGSTP = 'QCXXLNR' AND isnull(CMT_STSFL,'') <>'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			intROWCT = 0;
			if(M_rstRSSET !=null)
			{
				while (M_rstRSSET.next())
				{
				   L_strCODCD = M_rstRSSET.getString("CMT_CODCD");
				   tblTSDTL.setValueAt(L_strCODCD,intROWCT,TBL_LINRT); 
				   tblTSDTL.setValueAt(M_rstRSSET.getString("CMT_CODDS"),intROWCT,TBL_DESC); 
				   tblTSDTL.setValueAt(M_rstRSSET.getString("CMT_SHRDS"),intROWCT,TBL_SHRDS); 
				   intROWCT ++;
				}					
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
	    {
			setMSG("getTBLDT",'N');
		}
	}			
	class INPVF extends InputVerifier
	{	
		public boolean verify( JComponent input)
		{
			try
			{
				if((input == txtTSTTP) &&(txtTSTTP.getText().trim().length()==4))
				{
					M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CODCD='"+ txtTSTTP.getText()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!= null)
					{
						if(M_rstRSSET.next())
						{
							txtTSTDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
							M_rstRSSET.close();
							getTBLDT();
							return true;
						}
						else
						{
							setMSG("Invalid Test Type, Press F1 To SelectFrom List..",'N');
							M_rstRSSET.close();
							return false;
						}				
					}
				}
				if(input == txtTSTBY)
				{          
					strTSTBY = txtTSTBY.getText();
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from co_cdtrn where CMT_CGMTP ='A"+cl_dat.M_strCMPCD_pbst+"'";
					M_strSQLQRY +=" AND CMT_CGSTP = 'QCXXTST' AND CMT_CODCD = '" + strTSTBY + "'";
					M_strSQLQRY +=" AND CMT_CHP01 ='"+strQCATP +"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!= null)
					{
						if(M_rstRSSET.next())
						{	
							txtTBYDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
							M_rstRSSET.close();
							return true;
						}	
					    else
						{
							setMSG("You are not Authorised to do this test ...",'E');
							M_rstRSSET.close();
							return false;
						}
					}
				}							
			}
	        catch (Exception L_EX)
	        {
				setMSG(L_EX,"INPVF");
	            return false;
	        }		
			return true;
		}
	}
}
