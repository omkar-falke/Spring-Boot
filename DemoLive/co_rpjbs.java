/*
System Name		: 
Program Name	: 
Author			: Mr. S.R.Mehesare
Modified Date	: 11/02/2006
Documented Date	: 
Version			: SA v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.KeyEvent;import java.sql.ResultSet;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.Color;
import javax.swing.JScrollPane;

/**<pre>
System Name : 
 
Program Name :

Purpose : 

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from MM_GRMST and CO_CTMST for condiations :-

<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then current Date.
</I> 
Requirements:
	- To fetch Accountable Data from Data base.
	- To fetch Non_Accountable Data from Data base.
	- To fetch All(Accountable & Non_Accountable) Data from Data base.
	- To display Specific Report of combination data that taken in textarea.
*/

class co_rpjbs extends cl_rbase
{								/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtFMDAT;/** JTextField to display to enter To date to specify date range.*/
	private JTextField txtTODAT;
	private JTextField txtGRPCD;
	private JTextField txtSYSCD;
	private JTextField txtJOBTP;
	private JTextField txtJOBOR; 
	private JTextField txtJOBCT;
	private JTextField txtDVCNM;
	
	private JTextField txtGRPDS;
	private JTextField txtSYSDS;
	private JTextField txtJOBTD;
	private JTextField txtJOBOD;
	private JTextField txtJOBCD;
	private JTextField txtDVCND;
	private JTextField txtALCTO;
	private JComboBox cmbSTATS;
	
	//txtGRPCD	txtSYSCD	txtJOBTP	txtJOBOR	txtJOBCT	txtDVCNM
								/** String variable for Store Type.*/
	private String strSTRTP;	/** String variable for Store Type Description.*/
	private String strSTRNM;	/** String variable for generated Report File Name.*/
	private String strFILNM;	/** Integer Variable to count the Number of records fetched to block the Report if No data Found.*/
	private int intRECCT;		/** DataOutputStream to generate hld the stream of data.*/
	private DataOutputStream dosREPORT;/** FileOutputStream to generate the Report file form stream of data.*/
	private FileOutputStream fosREPORT;/** String variable to print Dotted Line in the Report.*/	
	
	String strFMDAT,strTODAT;
	private String strDOTLN  = "-------------------------------------------------------------------------------------------------------------";
	private String strDOTLN1 = "--------------------------------------------------------------------------------------------------------------------------";
	private String strDOTLN2 = "-----------------------------------------------------------------------------------------------------------";
	private String strDOTLN3 = "------------------------------------------------------------------------------------------------------------------";
	private boolean flgFIRST = true;
	//$
	private JRadioButton  rdbACCT;
	private JRadioButton  rdbNACCT;
	private JRadioButton  rdbALL;
	private ButtonGroup   btgANS;
	private JCheckBox chkSPECF;
	private JTextArea txtAREA;
	private JScrollPane scrollAREA;
	
	private JPanel pnlRDB;
	//private JPanel pnlTXT;
	private String strFLAG="";/** String variable for selected Radio_button.*/
	private String strCMPTP="";
	private String strSYSCD="";
	private String strJOBTP="";
	private String strJOBOR="";
	private String strJOBCT="";
	private String strDVCNM="";
	
	private int intROW;
	//$
	public co_rpjbs()
	{
		super(2);
		setMatrix(20,8);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);					
		
		add(new JLabel("From Date"),3,3,1,.8,this,'R');
		add(txtFMDAT = new TxtDate(),3,4,1,1,this,'L');
		add(new JLabel("To Date"),3,5,1,.5,this,'R');
		add(txtTODAT = new TxtDate(),3,6,1,1.1,this,'L');  
		   
		add(new JLabel("Status"),4,3,1,.8,this,'R');
		add(cmbSTATS = new JComboBox(),4,4,1,2,this,'L');
		
		add(new JLabel("Comp. Type"),5,3,1,.8,this,'R');
		add(txtGRPCD = new TxtLimit(2),5,4,1,.6,this,'L');
		add(txtGRPDS = new JTextField(),5,6,1,2.5,this,'R');
		
		add(new JLabel("Sys Code"),6,3,1,.8 ,this,'R');
		add(txtSYSCD = new TxtLimit(2),6,4,1,.6,this,'L');
		add(txtSYSDS = new JTextField(),6,6,1,2.5,this,'R');
		
		add(new JLabel("Job Type"),7,3,1,.8,this,'R');
		add(txtJOBTP = new TxtLimit(2),7,4,1,.6,this,'L');
		add(txtJOBTD = new JTextField(),7,6,1,2.5,this,'R');
		
		add(new JLabel("Job Origin"),8,3,1,.8,this,'R');
		add(txtJOBOR = new TxtLimit(2),8,4,1,.6,this,'L');
		add(txtJOBOD = new JTextField(),8,6,1,2.5,this,'R');
		
		add(new JLabel("Category"),9,3,1,.8,this,'R');
		add(txtJOBCT = new TxtLimit(2),9,4,1,.6,this,'L');
		add(txtJOBCD = new JTextField(),9,6,1,2.5,this,'R');
		
		add(new JLabel("Device Type"),10,3,1,.8,this,'R');
		add(txtDVCNM = new TxtLimit(2),10,4,1,.6,this,'L');
		add(txtDVCND = new JTextField(),10,6,1,2.5,this,'R');
		
		add(new JLabel("Allocated To"),11,3,1,.8,this,'R');
		add(txtALCTO = new TxtLimit(3),11,4,1,.6,this,'L');				
		
		cmbSTATS.addItem("Summary Report");
		cmbSTATS.addItem("Pending Jobs");
		cmbSTATS.addItem("Completed Jobs");
		cmbSTATS.addItem("Overall Report");
		cmbSTATS.addItem("Reported Jobs");
		//$
		pnlRDB= new JPanel();
		pnlRDB.setBorder(new EtchedBorder(Color.gray,Color.lightGray)); 
		add(rdbACCT=new JRadioButton("Accountable"),13,3,1,1.8,pnlRDB,'R');
		add(rdbNACCT=new JRadioButton("Non_Accountable"),13,4,1,1.6,pnlRDB,'L');
		add(rdbALL=new JRadioButton("All"),13,6,1,1.4,pnlRDB,'R'); 
		add(pnlRDB,13,5,1.5,3,this,'R');
		add(chkSPECF=new JCheckBox("Specific"),13,7,1,2.4,this,'L');  
		btgANS=new ButtonGroup();
		btgANS.add(rdbACCT); 
		btgANS.add(rdbNACCT);           
		btgANS.add(rdbALL);
		rdbALL.setSelected(true);
		add(txtAREA = new JTextArea(),5,8,6,1.8,this,'R'); 
		add(scrollAREA = new JScrollPane(txtAREA,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),5,8,6,1.8,this,'R');
		scrollAREA.setVisible(false);
		//$
		M_pnlRPFMT.setVisible(true);
		setENBL(false); 
	}
	/**
	 * Super Class Method overrided to enable & disable the Components.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);		 
		txtGRPDS.setEnabled(false);	
		txtSYSDS.setEnabled(false);
		txtJOBTD.setEnabled(false);
		txtJOBOD.setEnabled(false);
		txtJOBCD.setEnabled(false);
		txtDVCND.setEnabled(false);
		if((flgFIRST == true) && (P_flgSTAT == true))
		{
			txtGRPCD.setEnabled(false);	
			txtSYSCD.setEnabled(false);
			txtJOBOR.setEnabled(false);
			txtJOBCT.setEnabled(false);
			txtDVCNM.setEnabled(false);
			txtALCTO.setEnabled(false);
		}
		
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{ 
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						//M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						//M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					//txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						txtFMDAT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
				}
				else
					setENBL(false);
			}
			/**When selected  Summery Report from cmbSTATS,only Job Type TextField  enabled  and for another cmbSTATS, enabled all TextField **/
			
			else if(M_objSOURC == cmbSTATS ) 
			{
				if(cmbSTATS.getSelectedIndex() == 0)// Summary Report
				{
					txtGRPCD.setEnabled(false);	
					txtSYSCD.setEnabled(false);
					txtJOBOR.setEnabled(false);
					txtJOBCT.setEnabled(false);
					txtDVCNM.setEnabled(false);
					txtALCTO.setEnabled(false);
				}
				else
				{
					txtGRPCD.setEnabled(true);	
					txtSYSCD.setEnabled(true);
					txtJOBOR.setEnabled(true);
					txtJOBCT.setEnabled(true);
					txtDVCNM.setEnabled(true);
					txtALCTO.setEnabled(true);
				}
				
			}
			//$
			else if(M_objSOURC == chkSPECF) //for visible textarea 
			{
				if(chkSPECF.isSelected()) 
					scrollAREA.setVisible(true); 
				else
					scrollAREA.setVisible(false); 
			}
			else if(M_objSOURC == rdbACCT) //Accountable
			{
				if(rdbACCT.isSelected()) 
					strFLAG="'A'";  
			}
			else if(M_objSOURC == rdbNACCT) //Non_Accountable
			{
				if(rdbNACCT.isSelected())
					strFLAG="'N'";
			}
			else if(M_objSOURC == rdbALL) //All
			{
				if(rdbALL.isSelected()) 
					strFLAG="'A','N'";
			}
			//$
		} 
		catch(Exception L_EX) 
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	/**this method set focus in txtFMDAT & txtTODAT & set Message for enter Date**/
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == txtFMDAT)			
			setMSG("Enter From Date..",'N');			
		else if(M_objSOURC == txtTODAT)			
			setMSG("Enter To Date..",'N');		
	}	

	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		/** Method to fetch data from the database for Help Screen ,when press F1 that display Help Screen**/
		if(L_KE.getKeyCode()== L_KE.VK_F1)
		{				
			if(M_objSOURC == txtGRPCD)
			{
				M_strHLPFLD = "txtGRPCD";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT distinct SUBSTRING(CMT_CHP01,1,2)CHP01,CMT_CHP01 from CO_CDTRN"		
					+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXGRP'"
					+" AND isnull(CMT_STSFL,'')<>'X'";										
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Complaint Type","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}	
			else if(M_objSOURC == txtSYSCD)
			{
				if(txtGRPCD.getText().trim().length() == 0)
				{
					setMSG("Please Enter the Complaint Type first..",'E');
					txtGRPCD.requestFocus();
					return;
				}
				M_strHLPFLD = "txtSYSCD";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT distinct CMT_CODCD,CMT_CODDS from SA_JBMST,CO_CDTRN"
					+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXGRP'"
					+" AND isnull(CMT_STSFL,'')<>'X'"
					+" AND CMT_CHP01 like '"+txtGRPCD.getText().trim().substring(0,2)+"%'";
				if(txtSYSCD.getText().length() == 2)
					M_strSQLQRY += " AND CMT_CODDS like '"+txtGRPCD.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"System Code","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			} 
			else if(M_objSOURC == txtJOBTP)
			{
				M_strHLPFLD = "txtJOBTP";				
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN"
					+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXJOB'"
					+" AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like '1%'";					
				if(txtJOBTP.getText().length() > 0)
					M_strSQLQRY += " AND CMT_CODDS like '"+txtJOBTP.getText().trim()+"%'";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Type Code","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtJOBOR)
			{
				M_strHLPFLD = "txtJOBOR";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN"
					+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXJOB'"
					+" AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like '2%'";
				if(txtJOBOR.getText().length() >0)
					M_strSQLQRY += " AND CMT_CODDS like '"+txtJOBOR.getText().trim()+"%'";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Origin Code","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtJOBCT)
			{
				M_strHLPFLD = "txtJOBCT";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN"
					+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXJOB'"
					+" AND isnull(CMT_STSFL,'')<>'X'  AND CMT_CHP02 = '4'";
				if(txtJOBCT.getText().length() > 0)
					M_strSQLQRY += " AND CMT_CODDS like '"+txtJOBCT.getText().trim()+"%'";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Category Code","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtDVCNM)
			{
				M_strHLPFLD = "txtDVCNM";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN"
					+" where CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXJOB'"
					+" AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like '4%'";
				if(txtDVCNM.getText().length() > 0)
					M_strSQLQRY += " AND CMT_CODDS like '"+txtDVCNM.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Job Category Code","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);			
			}			
		}
		/** Method to request Focus in all TextField of component,when press ENTER **/
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{	
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					txtTODAT.requestFocus();
			}
			else if(M_objSOURC == txtTODAT)
			{
				if(cmbSTATS.getSelectedIndex() == 0)
				{
					txtJOBTP.requestFocus();
					setMSG("please Enter the Job Type or Press F1 to Select form List..",'N');
				}
				else
				{
					txtGRPCD.requestFocus();
					setMSG("please Enter the Device Name..",'N');
				}
			}
			else if(M_objSOURC == txtGRPCD)
			{
				txtSYSCD.requestFocus();
				setMSG("please Enter the System Code or Press F1 to Select form List..",'N');
			}
			else if(M_objSOURC == txtSYSCD)
			{
				txtJOBTP.requestFocus();
				setMSG("please Enter the Job Type or Press F1 to Select form List..",'N');
				
			}
			else if(M_objSOURC == txtJOBTP )
			{
				if(cmbSTATS.getSelectedIndex() == 0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
				{
					txtJOBOR.requestFocus();
					setMSG("please Enter the Job Origin or Press F1 to Select form List..",'N');
				}
			}
			else if(M_objSOURC == txtJOBOR)
			{
				txtJOBCT.requestFocus();
				setMSG("please Enter the Job Category or Press F1 to Select form List..",'N');
	 		}
			else if(M_objSOURC == txtJOBCT)
		 	{
				txtDVCNM.requestFocus();
				setMSG("please Enter the Device Name or Press F1 to Select from List..",'N');
			}
			else if(M_objSOURC == txtDVCNM)		
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	/**
	 * Method to enter data in Textarea ,when released ENTER button & Selected Specific chkbox 
	 **/
	public void keyReleased(KeyEvent L_KE)
	{
		super.keyReleased(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(chkSPECF.isSelected())
			{
				if(M_objSOURC == txtGRPCD)
					{
					if(!txtAREA.getText().contains(txtGRPCD.getText()))
					{
						if(txtAREA.getText().length()==0)
							txtAREA.setText(txtGRPCD.getText()+":"+txtGRPDS.getText());
						else
							txtAREA.setText(txtAREA.getText()+" "+txtGRPCD.getText()+":"+txtGRPDS.getText());
					}
				//String to select Multiple Compliant Type
					if(strCMPTP.length() == 0)
						strCMPTP = strCMPTP +" '"+txtGRPCD.getText()+"'";
					else 
						strCMPTP = strCMPTP + ", '"+txtGRPCD.getText()+"' ";
		 		
					System.out.println("strCMPTP>>"+strCMPTP);
					}
				else if(M_objSOURC == txtSYSCD)
				{
					if(!txtAREA.getText().contains(txtSYSCD.getText()))
					{
						if(txtAREA.getText().length()==0)
							txtAREA.setText(txtSYSCD.getText()+":"+txtSYSDS.getText());
						else
							txtAREA.setText(txtAREA.getText()+" "+txtSYSCD.getText()+":"+txtSYSDS.getText());
					}
					if(strSYSCD.length() == 0)
						strSYSCD = strSYSCD +" '"+txtSYSCD.getText()+"'";
					else 
						strSYSCD = strSYSCD + ", '"+txtSYSCD.getText()+"' ";
				}
				else if(M_objSOURC == txtJOBTP )
				{
					if(!txtAREA.getText().contains(txtJOBTP.getText()))
					{	
						if(txtAREA.getText().length()==0 )
							txtAREA.setText(txtJOBTP.getText()+":"+txtJOBTD.getText()); 
						else
							txtAREA.setText(txtAREA.getText()+" "+txtJOBTP.getText()+":"+txtJOBTD.getText());
					}
					if(strJOBTP.length() == 0)
						strJOBTP = strJOBTP +" '"+txtJOBTP.getText()+"'";
					else 
						strJOBTP = strJOBTP + ", '"+txtJOBTP.getText()+"' ";
				}
				else if(M_objSOURC == txtJOBOR)
				{
					if(!txtAREA.getText().contains(txtJOBOR.getText()))
					{
						if(txtAREA.getText().length()==0)
							txtAREA.setText(txtJOBOR.getText()+":"+txtJOBOD.getText());
						else
							txtAREA.setText(txtAREA.getText()+" "+txtJOBOR.getText()+":"+txtJOBOD.getText());
					}
					if(strJOBOR.length() == 0)
						strJOBOR = strJOBOR +" '"+txtJOBOR.getText()+"'";
					else 
						strJOBOR = strJOBOR + ", '"+txtJOBOR.getText()+"' ";
				}
				else if(M_objSOURC == txtJOBCT)
				{
					if(!txtAREA.getText().contains(txtJOBCT.getText()))
					{
						if(txtAREA.getText().length()==0)
							txtAREA.setText(txtJOBCT.getText()+":"+txtJOBCD.getText());
						else
							txtAREA.setText(txtAREA.getText()+" "+txtJOBCT.getText()+":"+txtJOBCD.getText());
					}
					if(strJOBCT.length() == 0)
						strJOBCT = strJOBCT +" '"+txtJOBCT.getText()+"'";
					else 
						strJOBCT = strJOBCT + ", '"+txtJOBCT.getText()+"' ";
				}
				else if(M_objSOURC == txtDVCNM)		
				{
					if(!txtAREA.getText().contains(txtDVCNM.getText()))
					{
						if(txtAREA.getText().length()==0)
							txtAREA.setText(txtDVCNM.getText()+":"+txtDVCND.getText());
						else
							txtAREA.setText(txtAREA.getText()+" "+txtDVCNM.getText()+":"+txtDVCND.getText());
					}
					if(strDVCNM.length() == 0)
			    		strDVCNM =strDVCNM +" '"+txtDVCNM.getText()+"'";
			    	else 
			    		strDVCNM = strDVCNM + ",'"+txtDVCNM.getText()+"'";
				}	
			}	
		 }
	}
	/** Method to set Help Screen Data in TextField 
	 * */
	void exeHLPOK()
	{
		super.exeHLPOK();		  	 
		if(M_strHLPFLD == "txtGRPCD")
		{
			txtGRPCD.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());
			txtGRPDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD == "txtSYSCD")
		{
			txtSYSCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtSYSDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD == "txtJOBTP")
		{
			txtJOBTP.setText(cl_dat.M_strHLPSTR_pbst);
			txtJOBTD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD == "txtJOBOR")
		{
			txtJOBOR.setText(cl_dat.M_strHLPSTR_pbst);
			txtJOBOD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD == "txtJOBCT")
		{
			txtJOBCT.setText(cl_dat.M_strHLPSTR_pbst);
			txtJOBCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD == "txtDVCNM")
		{
			txtDVCNM.setText(cl_dat.M_strHLPSTR_pbst);
			txtDVCND.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"sa_rpjbs.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"sa_rpjbs.doc";
			
			getDATA();
			
			/*if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Job Status"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to validate the data before execuation of the SQL Quary.
	 * To Date must be greater than From Date & smaller than current Date
	 * From Date Should Not Be Grater Than Todays Date
	 */
	boolean vldDATA()
	{
		try
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("From Date Should Not Be Grater Than Todays Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("To Date Should Not Be Grater Than Todays Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
				return false;
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 * call Methods of Summery Report, Pending Jobs, Completed Jobs, Reported Jobs & Overall Report.
	 */
	public void getDATA()
	{
		if(!vldDATA())
			return;		
		try
		{
			intRECCT = 0;		
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("<HTML><HEAD><Title>Job Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim())); 
			
			if(cmbSTATS.getSelectedIndex() == 0 )//summary
			{	
				getDATA0();
			}	
			else if(cmbSTATS.getSelectedIndex() == 1)//pending 
			{
				getDATA1();
			}
			else if(cmbSTATS.getSelectedIndex() == 2)//Jobs completed
			{
				getDATA2();
			}
			else if(cmbSTATS.getSelectedIndex() == 3)//All Details
			{
				getDATA0();				
				
				dosREPORT.writeBytes("\n\n\n"+"Jobs Pending as on : "+txtTODAT.getText()+"\n");			
				dosREPORT.writeBytes(strDOTLN1+"\n");			
				dosREPORT.writeBytes("Sys  Job Description                                                         Rep.Date   Pln.Days  Cat.   Job SNo."+"\n");
				dosREPORT.writeBytes(strDOTLN1);
				cl_dat.M_intLINNO_pbst +=6 ;
				getDATA1();				
				
				if(cl_dat.M_intLINNO_pbst > 60)
				{
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					cl_dat.M_intLINNO_pbst = 0;
					prnHEADER1();
				}
				else
				{					
					dosREPORT.writeBytes("\n\n\n"+"Jobs Completed in the Date Range "+txtFMDAT.getText()+" To : "+txtTODAT.getText()+"\n");
					dosREPORT.writeBytes(strDOTLN2+"\n");			
					dosREPORT.writeBytes("Sys  Job Description                                                    End Date   Pln.Days  Cat. Act.Days"+"\n");
					dosREPORT.writeBytes(strDOTLN2);
					cl_dat.M_intLINNO_pbst +=6;
				}
				getDATA2();
			}
			else if(cmbSTATS.getSelectedIndex() == 4)// Reported Jobs
			{
				getDATA3();
			}
			else if(cmbSTATS.getSelectedIndex() == 3)
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>"); //<P CLASS = \"breakhere\">
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
	 * Method to generate Header part of the report for Pending Jobs.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;									
			dosREPORT.writeBytes("\n"+ padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN1.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Pending Jobs as on : "+txtTODAT.getText(),strDOTLN1.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			//dosREPORT.writeBytes("Period From : "+txtFMDAT.getText()+" To : "+txtTODAT.getText() +"\n");
			dosREPORT.writeBytes(strDOTLN1+"\n");			
			dosREPORT.writeBytes("Sys  Job Description                                                         Rep.Date   Pln.Days  Cat.  Job.SNo.  Priority"+"\n");			
			dosREPORT.writeBytes(strDOTLN1);
			cl_dat.M_intLINNO_pbst = 5;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to generate Header part of the report for Jobs Completed.
	 */
	public void prnHEADER1()
	{
		try
		{
			cl_dat.M_PAGENO++;									
			dosREPORT.writeBytes("\n"+ padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN2.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Jobs Completed in the Date Range "+txtFMDAT.getText()+" To : "+txtTODAT.getText(),strDOTLN2.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");
			dosREPORT.writeBytes(strDOTLN2+"\n");			
			dosREPORT.writeBytes("Sys  Job Description                                                    End Date   Pln.Days  Cat. Act.Days"+"\n");
			dosREPORT.writeBytes(strDOTLN2);
			cl_dat.M_intLINNO_pbst = 5;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to generate Header part of the report for Jobs Reported.
	 */
	public void prnHEADER2()
	{
		try
		{
			cl_dat.M_PAGENO++;									
			dosREPORT.writeBytes("\n"+ padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN2.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Jobs Reported in the Date Range "+txtFMDAT.getText()+" To : "+txtTODAT.getText(),strDOTLN2.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");
			dosREPORT.writeBytes(strDOTLN2+"-------"+"\n");			
			dosREPORT.writeBytes("Sys  Job Description                                                    Rep.Date   Pln.Days  Cat. Status Act.Days"+"\n");
			dosREPORT.writeBytes(strDOTLN2+"-------");
			cl_dat.M_intLINNO_pbst = 5;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to print the dash if value is zero
	 * @param P_intVAL int Value to check for value equal to zero
	 */
	String prnSTR(int P_intVAL)
	{
		if(P_intVAL == 0)
			return "-";
		else
			return String.valueOf(P_intVAL).toString();
	}
	/**
	 * Method to fetch data from the Data base for Summary Report.
	 */
	void getDATA0()
	{
		try
		{
			intRECCT = 0;
			String L_strSTATS="",L_strTEMP="";
			String L_strSYSCD="",L_strOSYSCD="";
			int L_intA1=0,L_intB1=0,L_intC1=0;
			int L_intTA1=0,L_intTB1=0,L_intTC1=0;
			int L_intA2=0,L_intB2=0,L_intC2=0;
			int L_intTA2=0,L_intTB2=0,L_intTC2=0;
			int L_intA3=0,L_intB3=0,L_intC3=0;
			int L_intTA3=0,L_intTB3=0,L_intTC3=0;
			int L_intP=0,L_intR=0,L_intC=0;
				
			int L_intTP=0,L_intTR=0,L_intTC=0;
				
			cl_dat.M_PAGENO++;									
		 	dosREPORT.writeBytes("\n"+ padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");				
			cl_dat.M_intLINNO_pbst +=2;
			
			dosREPORT.writeBytes(padSTRING('R',"Summary Report",strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes("Period From : "+txtFMDAT.getText()+" To : "+txtTODAT.getText() +"\n");
			cl_dat.M_intLINNO_pbst +=2;
			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Sys  <--Previous Pending-->  <---Jobs Reported--->    Total  <---Jobs Completed---->  <--Current Pending--->"+"\n");							
			dosREPORT.writeBytes("         A    B    C  Total      A    B    C  Total Pending       A    B    C  Total      A    B    C  Total"+"\n");
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst +=3;	
			
			String L_strSQL = "";
			/*if((cmbSTATS.getSelectedIndex() == 0) 
			if(txtJOBTP.getText().trim().length()==2)
			{
				L_strSQL = " AND JB_JOBTP = '"+ txtJOBTP.getText().trim() +"'";
			}
			else 
				L_strSQL = " AND JB_JOBTP <> '13'"; 
			
			M_strSQLQRY = "SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'P' L_STATS FROM SA_JBMST WHERE DATE(JB_REPDT) <'"+ strFMDAT +"' AND ((JB_ENDDT is null) or (date(JB_ENDDT) > '"+ strFMDAT +"')) AND isnull(JB_STSFL,'')<>'X'" + L_strSQL// Previous pending 
		 	      +" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'P1' L_STATS FROM SA_JBMST WHERE DATE(JB_REPDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND Date(JB_ENDDT) is null AND isnull(JB_STSFL,'')<>'X'" + L_strSQL// Pending, reported in the date Range						 
	 			  +" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'R' L_STATS FROM SA_JBMST WHERE DATE(JB_REPDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND isnull(JB_STSFL,'')<>'X'" + L_strSQL//Reported in the Date  Date Range			
			  	  +" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'C' L_STATS FROM SA_JBMST WHERE DATE(JB_ENDDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND isnull(JB_STSFL,'')<>'X'"+ L_strSQL +" ORDER BY JB_SYSCD";*/		
			
			//$
			if(txtAREA.getText().trim().length()>0 && chkSPECF.isSelected())
			{
				L_strSQL = " AND JB_JOBTP IN('"+ txtAREA.getText().replace(" ","','") +"')";
			}
			if(txtAREA.getText().trim().length()==0 && txtJOBTP.getText().trim().length()==2)
			{
				L_strSQL = " AND JB_JOBTP = '"+ txtJOBTP.getText().trim() +"'";
			}
			else 
				L_strSQL = " AND JB_JOBTP <> '13'"; 
		 	
			M_strSQLQRY = "SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,CONVERT(varchar,JB_STRDT,103) L_STRDT,CONVERT(varchar,JB_ENDDT,103) L_ENDDT,'P' L_STATS FROM SA_JBMST WHERE CONVERT(varchar,JB_REPDT,103) <'"+ strFMDAT +"' AND ((JB_ENDDT is null) or (CONVERT(varchar,JB_ENDDT,103) > '"+ strFMDAT +"')) AND isnull(JB_STSFL,'')<>'X'" +L_strSQL +" AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN ("+strFLAG+"))"
		 		+" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,CONVERT(varchar,JB_STRDT,103) L_STRDT,CONVERT(varchar,JB_ENDDT,103) L_ENDDT,'P1' L_STATS FROM SA_JBMST WHERE CONVERT(varchar,JB_REPDT,103) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND CONVERT(varchar,JB_ENDDT,103) is null AND isnull(JB_STSFL,'')<>'X'"+ L_strSQL+" AND JB_SYSCD IN(SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN ("+strFLAG+"))"// Pending, reported in the date Range						 
		 		+" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,CONVERT(varchar,JB_STRDT,103) L_STRDT,CONVERT(varchar,JB_ENDDT,103) L_ENDDT,'R' L_STATS FROM SA_JBMST WHERE CONVERT(varchar,JB_REPDT,103) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND isnull(JB_STSFL,'')<>'X'" + L_strSQL+" AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN("+strFLAG+"))"//Reported in the Date  Date Range			
		 		+" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,CONVERT(varchar,JB_STRDT,103) L_STRDT,CONVERT(varchar,JB_ENDDT,103) L_ENDDT,'C' L_STATS FROM SA_JBMST WHERE CONVERT(varchar,JB_REPDT,103) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND isnull(JB_STSFL,'')<>'X'"+ L_strSQL +" AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN("+strFLAG+"))"+"ORDER BY JB_SYSCD";		
			
			 //$
			
			System.out.println("data"+M_strSQLQRY);     
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())  
				{ 
					intRECCT = 1;
					L_strSYSCD = M_rstRSSET.getString("JB_SYSCD");
					if(!L_strSYSCD.equals(L_strOSYSCD))
					{
						if(!L_strOSYSCD.equals(""))
						{
							L_intTA1 += L_intA1;
							L_intTB1 += L_intB1;
							L_intTC1 += L_intC1;
							L_intTA2 += L_intA2;
							L_intTB2 += L_intB2;
							L_intTC2 += L_intC2;
							L_intTA3 += L_intA3;
							L_intTB3 += L_intB3;
							L_intTC3 += L_intC3;
							//Write stream of data in dosREPORT variable of DataOutPutStream class.
							dosREPORT.writeBytes("\n"+L_strOSYSCD+"  ");
							cl_dat.M_intLINNO_pbst ++;
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA1),6));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB1),5));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC1),5));
							L_intP = L_intA1+L_intB1+L_intC1;
							L_intTP += L_intP;
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intP),7));
								
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA2),7));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB2),5));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC2),5));
							L_intR = L_intA2+L_intB2+L_intC2;
							L_intTR += L_intR;
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intR),7));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intP+L_intR),8));
								
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA3),8));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB3),5));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC3),5));
							L_intC = L_intA3+L_intB3+L_intC3;
							L_intTC += L_intC;
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC),7));
								
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA1+L_intA2-L_intA3),7));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB1+L_intB2-L_intB3),5));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC1+L_intC2-L_intC3),5));
							dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intP+L_intR-L_intC),7));
						}
						L_intA1=0;L_intB1=0;L_intC1=0;
						L_intA2=0;L_intB2=0;L_intC2=0;
						L_intA3=0;L_intB3=0;L_intC3=0;
						L_strOSYSCD = L_strSYSCD;
					}
					L_strSTATS = M_rstRSSET.getString("L_STATS");
					if((L_strSTATS.equals("P"))||(L_strSTATS.equals("P1")))// pending before From Date.
					{
						L_strTEMP = M_rstRSSET.getString("JB_JOBCT");
						if(L_strTEMP.equals("A"))
							L_intA1++;
						else if(L_strTEMP.equals("B"))
							L_intB1++;
						else if(L_strTEMP.equals("C"))
							L_intC1++;
					}
					else if(L_strSTATS.equals("R"))// Jobs Reported in the Given Date Range
					{
						L_strTEMP = M_rstRSSET.getString("JB_JOBCT");
						if(L_strTEMP.equals("A"))
							L_intA2++;
						else if(L_strTEMP.equals("B"))
							L_intB2++;
						else if(L_strTEMP.equals("C"))
							L_intC2++;
					}
					else if(L_strSTATS.equals("C"))//Completed in the given date range.
					{
						L_strTEMP = M_rstRSSET.getString("JB_JOBCT");
						if(L_strTEMP.equals("A"))
							L_intA3++;
						else if(L_strTEMP.equals("B"))
							L_intB3++;
						else if(L_strTEMP.equals("C"))
							L_intC3++;
					}
				}
				M_rstRSSET.close();
			}
			// last Record
			dosREPORT.writeBytes("\n"+L_strSYSCD+"  ");
			cl_dat.M_intLINNO_pbst ++;
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA1),6));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB1),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC1),5));
			L_intP = L_intA1+L_intB1+L_intC1;
			L_intTP += L_intP;
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intP),7));
								
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA2),7));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB2),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC2),5));
			L_intR = L_intA2+L_intB2+L_intC2;
			L_intTR += L_intR;
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intR),7));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intP+L_intR),8));
								
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA3),8));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB3),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC3),5));
			L_intC = L_intA3+L_intB3+L_intC3;
			L_intTC += L_intC;
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC),7));
			//calculate current pending by previous pending + job Reported - Job Completed			
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA1+L_intA2-L_intA3),7));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB1+L_intB2-L_intB3),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC1+L_intC2-L_intC3),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intP+L_intR-L_intC),7));																
				
			dosREPORT.writeBytes("\n"+strDOTLN);
			dosREPORT.writeBytes("\n"+"Total");
			cl_dat.M_intLINNO_pbst +=2;
			int L_intSUMA=0,L_intSUMB=0,L_intSUMC=0,L_intSUMT=0,L_intTEMP =0;
			int L_intTOTALP=0;
			L_intTEMP = L_intA1+L_intTA1+L_intB1+L_intTB1+L_intC1+L_intTC1;
			L_intSUMA = L_intTEMP;
			L_intTOTALP = L_intTEMP;
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA1+L_intTA1),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB1+L_intTB1),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC1+L_intTC1),5));				
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intTEMP),7));
								
			L_intTEMP = L_intA2+L_intTA2+L_intB2+L_intTB2+L_intC2+L_intTC2;
			L_intSUMA += L_intTEMP;			
			L_intTOTALP += L_intTEMP;
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA2+L_intTA2),7));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB2+L_intTB2),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC2+L_intTC2),5));				
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intTEMP),7));

			L_intTEMP = L_intA3+L_intTA3+L_intB3+L_intTB3+L_intC3+L_intTC3;
			L_intSUMA -= L_intTEMP;
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intTOTALP),8));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA3+L_intTA3),8));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB3+L_intTB3),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC3+L_intTC3),5));				
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intTEMP),7));
								
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intA1+L_intTA1+L_intA2+L_intTA2-(L_intA3+L_intTA3)),7));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intB1+L_intTB1+L_intB2+L_intTB2-(L_intB3+L_intTB3)),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intC1+L_intTC1+L_intC2+L_intTC2-(L_intC3+L_intTC3)),5));
			dosREPORT.writeBytes(padSTRING('L',prnSTR(L_intSUMA),7));
			dosREPORT.writeBytes("\n"+strDOTLN);	
			cl_dat.M_intLINNO_pbst ++;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA0");
		}
	}
	/**
	 * Method to fetch data from the Data base for Pending Jobs.
	 */
	void getDATA1()
	{
		try 
		{
			int L_intCOUNT = 0;
			double L_dblTDAY = 0;
			java.sql.Date L_datTEMP;
			java.sql.Timestamp L_tmsTEMP;
			String L_strSYSCD="",L_strTEMP="";
			String L_strOSYSCD="";
			intRECCT = 0;
			String str1 ="";
			int L_intLINDX = 0;
			if(cmbSTATS.getSelectedIndex() != 3)
				prnHEADER();
			//M_strSQLQRY = "Select JB_SYSCD,JB_JOBDS,JB_REPBY,JB_REPDT,JB_PLNDY,JB_JOBCT,JB_STRDT,JB_ENDDT,JB_SRLNO,JB_PRTNO from SA_JBMST where isnull(JB_STSFL,'')<>'X'"
			  //+" AND DATE(JB_REPDT) <'"+ strTODAT +"' AND JB_ENDDT IS NULL";
            
			//$ 
			M_strSQLQRY = "Select JB_SYSCD,JB_JOBDS,JB_REPBY,JB_REPDT,JB_PLNDY,JB_JOBCT,JB_STRDT,JB_ENDDT,JB_SRLNO,JB_PRTNO from SA_JBMST where isnull(JB_STSFL,'')<>'X'"
				+" AND DATE(JB_REPDT) <'"+ strTODAT +"' AND JB_ENDDT IS NULL " +" AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN("+strFLAG+"))";
		if(chkSPECF.isSelected())
			{
			if(strCMPTP.trim().length()>0) 
					M_strSQLQRY += " AND JB_GRPCD IN("+strCMPTP+")"; 
			if(strSYSCD.trim().length()>0)
					M_strSQLQRY += " AND JB_SYSCD IN("+strSYSCD+")";
			if(strJOBTP.trim().length()>0)
					M_strSQLQRY += " AND JB_JOBTP IN("+strJOBTP+")";
			if(strJOBOR.trim().length()>0)
					M_strSQLQRY += " AND JB_JOBOR IN("+strJOBOR+")";
			if(strJOBCT.trim().length()>0)
					M_strSQLQRY += " AND JB_JOBCT IN("+strJOBCT+")";
			if(strDVCNM.trim().length()>0)
					M_strSQLQRY += " AND JB_DVCNM IN("+strDVCNM+")";
			//M_strSQLQRY += " AND SUBSTRING(JB_DVCNM,1,2) IN("+strDVCNM+")";
			}
		if(!chkSPECF.isSelected())
		{
	//$		
			if(txtGRPCD.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_GRPCD ='"+ txtGRPCD.getText().trim() +"'";	
			if(txtSYSCD.getText().trim().length() == 2 )
				M_strSQLQRY +=" AND JB_SYSCD ='"+ txtSYSCD.getText().trim() +"'";	
			if(txtJOBTP.getText().trim().length() == 2)
			{
				/*if(txtJOBTP.getText().trim().equals("13"))// 13 for Conversion
					M_strSQLQRY +=" AND JB_JOBTP = '13'";
			    else
				    M_strSQLQRY +="  AND JB_JOBTP <> '13'";		*/
				M_strSQLQRY +=" AND JB_JOBTP = '"+ txtJOBTP.getText().trim() +"'";
			}
			else		
				M_strSQLQRY +=" AND JB_JOBTP <> '13'";				
			if(txtJOBCT.getText().trim().length() == 1)
				M_strSQLQRY +=" AND JB_JOBCT ='"+ txtJOBCT.getText().trim() +"'";	
			if(txtJOBOR.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_JOBOR ='"+ txtJOBOR.getText().trim() +"'";			
			if(txtDVCNM.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_DVCNM='"+ txtDVCNM.getText().trim() +"'";			
			if(txtALCTO.getText().trim().length() == 3)
				M_strSQLQRY +=" AND JB_ALCTO ='"+ txtALCTO.getText().trim().toUpperCase()+"'";
		}
			M_strSQLQRY +=" order by JB_SYSCD,JB_PRTNO";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{											
					intRECCT = 1; 
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN1);
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						cl_dat.M_intLINNO_pbst = 0;
						prnHEADER();
					}
					L_strSYSCD = nvlSTRVL(M_rstRSSET.getString("JB_SYSCD"),"");
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst ++;
					if(!L_strOSYSCD.equals(L_strSYSCD))
					{
						if(!L_strOSYSCD.equals(""))
						{
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst ++;
						}
						L_strOSYSCD = L_strSYSCD;
					}
					dosREPORT.writeBytes(L_strSYSCD+"   ");					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),"");
					if(L_strTEMP.length() > 70)
					{
						str1 = L_strTEMP.substring(0,70);
						L_intLINDX = str1.lastIndexOf(" ");
						if(L_intLINDX < 60)
							dosREPORT.writeBytes(padSTRING('R',L_strTEMP.substring(0,70),72) +"\n     -"+padSTRING('R',L_strTEMP.substring(70),71));
						else							
							dosREPORT.writeBytes(padSTRING('R',L_strTEMP.substring(0,L_intLINDX),72) +"\n     "+padSTRING('R',L_strTEMP.substring(L_intLINDX),72));
						cl_dat.M_intLINNO_pbst++;
					}
					else
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,72));
					
					//dosREPORT.writeBytes(padSTRING('R',L_strTEMP,100));					
					L_datTEMP = M_rstRSSET.getDate("JB_REPDT");
					if(L_datTEMP != null)
						L_strTEMP = M_fmtLCDAT.format(L_datTEMP);
					else
						L_strTEMP = "";
					dosREPORT.writeBytes(padSTRING('R',L_strTEMP,12));
					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_PLNDY"),"");					
					dosREPORT.writeBytes(padSTRING('L',L_strTEMP,7)+"  ");
					if(!L_strTEMP.equals(""))
						L_dblTDAY += Double.valueOf(L_strTEMP).doubleValue();
					
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("JB_JOBCT"),""),4));						
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("JB_SRLNO"),""),10));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("JB_PRTNO"),""),10));
					L_intCOUNT++;
					//cl_dat.M_intLINNO_pbst++;
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n"+strDOTLN1+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Total Pending Jobs : "+L_intCOUNT,66)+padSTRING('L',"Total Planned Days : "+ setNumberFormat(L_dblTDAY,2),30));
			dosREPORT.writeBytes("\n"+strDOTLN1);	
			cl_dat.M_intLINNO_pbst +=3;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA1");
		}
	}
	/**
	 * Method to fetch data from the Data base for Completed Jobs.
	 */
	void getDATA2()
	{
		try
		{			
			java.sql.Date L_datTEMP;
			java.sql.Timestamp L_tmsTEMP;
			String L_strSYSCD="",L_strTEMP="";
			String L_strOSYSCD="";
			int L_intCOUNT = 0;
			intRECCT = 0;
			String str1 ="";
			int L_intLINDX = 0;
			if(cmbSTATS.getSelectedIndex() != 3)
				prnHEADER1();
				
			M_strSQLQRY = "Select JB_SYSCD,JB_JOBDS,JB_REPBY,CONVERT(varchar,JB_ENDDT,103) ENDDT,JB_PLNDY,JB_JOBCT,DAY(JB_ENDDT - JB_STRDT) L_DAYS,JB_SRLNO from SA_JBMST"
			+" where isnull(JB_STSFL,'')<>'X'"
			+" AND DATE(JB_REPDT) < '"+ strTODAT +"' AND DATE(JB_ENDDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"'";
			if(txtGRPCD.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_GRPCD ='"+ txtGRPCD.getText().trim() +"'";			
			if(txtSYSCD.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_SYSCD ='"+ txtSYSCD.getText().trim() +"'";			
			if(txtJOBTP.getText().trim().length() == 2)
			{
			/*	if(txtJOBTP.getText().trim().equals("13"))// 13 for Conversion
					M_strSQLQRY +=" AND JB_JOBTP = '13'";
			    else
				    M_strSQLQRY +="  AND JB_JOBTP <> '13'";	*/
				M_strSQLQRY +=" AND JB_JOBTP = '"+ txtJOBTP.getText().trim() +"'";
			}
			else
				M_strSQLQRY +="  AND JB_JOBTP <> '13'";	
				//M_strSQLQRY +=" AND JB_JOBTP ='"+ txtJOBTP.getText().trim() +"'";			
			if(txtJOBCT.getText().trim().length() == 1)
				M_strSQLQRY +=" AND JB_JOBCT ='"+ txtJOBCT.getText().trim() +"'";			
			if(txtJOBOR.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_JOBOR ='"+ txtJOBOR.getText().trim() +"'";			
			if(txtDVCNM.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_DVCCD ='"+ txtDVCNM.getText().trim() +"'";
			if(txtALCTO.getText().trim().length() == 3)
				M_strSQLQRY +=" AND JB_ALCTO ='"+ txtALCTO.getText().trim().toUpperCase()+"'";
			M_strSQLQRY +=" order by JB_SYSCD,ENDDT";

			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);				
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{	
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN2);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						cl_dat.M_intLINNO_pbst = 0;
						prnHEADER1();
					}
					L_strSYSCD = nvlSTRVL(M_rstRSSET.getString("JB_SYSCD"),"");
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst ++;
					if(!L_strOSYSCD.equals(L_strSYSCD))
					{
						if(!L_strOSYSCD.equals(""))
						{
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst ++;
						}
						L_strOSYSCD = L_strSYSCD;
					}					
					dosREPORT.writeBytes(L_strSYSCD+"   ");					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),"");
					if(L_strTEMP.length() > 65)
					{
						str1 = L_strTEMP.substring(0,65);
						L_intLINDX = str1.lastIndexOf(" ");						
						if(L_intLINDX < 55)
							dosREPORT.writeBytes(padSTRING('R',L_strTEMP.substring(0,65),67) +"\n     -"+padSTRING('R',L_strTEMP.substring(65),66));
						else							
							dosREPORT.writeBytes(padSTRING('R',L_strTEMP.substring(0,L_intLINDX),67) +"\n     "+padSTRING('R',L_strTEMP.substring(L_intLINDX),67));
						cl_dat.M_intLINNO_pbst++;
					}
					else
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,67));
					//dosREPORT.writeBytes(padSTRING('R',L_strTEMP,100));
					L_datTEMP = M_rstRSSET.getDate("ENDDT");
					if(L_datTEMP != null)
						L_strTEMP = M_fmtLCDAT.format(L_datTEMP);
					else
						L_strTEMP = "";
					dosREPORT.writeBytes(padSTRING('R',L_strTEMP,12));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("JB_PLNDY"),""),7)+"  ");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("JB_JOBCT"),""),5));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_DAYS"),""),8));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("JB_SRLNO"),""),10));
					
					//cl_dat.M_intLINNO_pbst++;
					L_intCOUNT++;
				}
				dosREPORT.writeBytes("\n"+strDOTLN2+"\n");
				dosREPORT.writeBytes("Total Jobs Completed : "+L_intCOUNT);
				dosREPORT.writeBytes("\n"+strDOTLN2);
				cl_dat.M_intLINNO_pbst +=3;
			}
			M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA2");
		}
	}
	/**
	 * Method to get Data for Jobs Reported in the Given Date Range.
	 */
	void getDATA3()
	{
		try
		{			
			java.sql.Date L_datTEMP;
			java.sql.Timestamp L_tmsTEMP;
			String L_strSYSCD="",L_strTEMP="";
			String L_strOSYSCD="";
			int L_intCOUNT = 0;
			intRECCT = 0;
			String str1 ="";
			int L_intLINDX = 0;			
			
			prnHEADER2();
				
			M_strSQLQRY = "Select JB_SYSCD,JB_JOBDS,JB_REPBY,JB_REPDT,JB_PLNDY,JB_JOBCT,DAY(JB_ENDDT - JB_STRDT) L_DAYS,JB_STSFL from SA_JBMST"
			+" where isnull(JB_STSFL,'')<>'X'"
			+" AND DATE(JB_REPDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"'";
			if(txtGRPCD.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_GRPCD ='"+ txtGRPCD.getText().trim() +"'";			
			if(txtSYSCD.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_SYSCD ='"+ txtSYSCD.getText().trim() +"'";			
			if(txtJOBTP.getText().trim().length() == 2)
			{
			/*	if(txtJOBTP.getText().trim().equals("13"))// 13 for Conversion
					M_strSQLQRY +=" AND JB_JOBTP = '13'";
			    else
				    M_strSQLQRY +="  AND JB_JOBTP <> '13'";	*/
				M_strSQLQRY +=" AND JB_JOBTP = '"+ txtJOBTP.getText().trim() +"'";
			}
			else
				M_strSQLQRY +="  AND JB_JOBTP <> '13'";	
				//M_strSQLQRY +=" AND JB_JOBTP ='"+ txtJOBTP.getText().trim() +"'";			
			if(txtJOBCT.getText().trim().length() == 1)
				M_strSQLQRY +=" AND JB_JOBCT ='"+ txtJOBCT.getText().trim() +"'";			
			if(txtJOBOR.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_JOBOR ='"+ txtJOBOR.getText().trim() +"'";			
			if(txtDVCNM.getText().trim().length() == 2)
				M_strSQLQRY +=" AND JB_DVCCD ='"+ txtDVCNM.getText().trim() +"'";
			if(txtALCTO.getText().trim().length() == 3)
				M_strSQLQRY +=" AND JB_ALCTO ='"+ txtALCTO.getText().trim().toUpperCase()+"'";
			M_strSQLQRY +=" order by JB_SYSCD,JB_REPDT";

			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);				
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{	
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN3);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						cl_dat.M_intLINNO_pbst = 0;
						prnHEADER2();
					}
					L_strSYSCD = nvlSTRVL(M_rstRSSET.getString("JB_SYSCD"),"");
					dosREPORT.writeBytes("\n");					
					cl_dat.M_intLINNO_pbst ++;
					if(!L_strOSYSCD.equals(L_strSYSCD))
					{
						if(!L_strOSYSCD.equals(""))
						{
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst ++;
						}
						L_strOSYSCD = L_strSYSCD;
					}
					dosREPORT.writeBytes(L_strSYSCD+"   ");					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),"");
					if(L_strTEMP.length() > 65)
					{
						str1 = L_strTEMP.substring(0,65);
						L_intLINDX = str1.lastIndexOf(" ");						
						if(L_intLINDX < 55)
							dosREPORT.writeBytes(padSTRING('R',L_strTEMP.substring(0,65),67) +"\n     -"+padSTRING('R',L_strTEMP.substring(65),66));
						else							
							dosREPORT.writeBytes(padSTRING('R',L_strTEMP.substring(0,L_intLINDX),67) +"\n     "+padSTRING('R',L_strTEMP.substring(L_intLINDX),67));
						cl_dat.M_intLINNO_pbst++;
					}
					else
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,67));
					//dosREPORT.writeBytes(padSTRING('R',L_strTEMP,100));
					L_datTEMP = M_rstRSSET.getDate("JB_REPDT");
					if(L_datTEMP != null)
						L_strTEMP = M_fmtLCDAT.format(L_datTEMP);
					else
						L_strTEMP = "";
					dosREPORT.writeBytes(padSTRING('R',L_strTEMP,12));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("JB_PLNDY"),""),7)+"  ");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("JB_JOBCT"),""),5));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("JB_STSFL"),""),7));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_DAYS"),""),8));
					//cl_dat.M_intLINNO_pbst++;
					L_intCOUNT++;
				}
				dosREPORT.writeBytes("\n"+strDOTLN3+"\n");
				dosREPORT.writeBytes("Total Jobs Completed : "+L_intCOUNT);
				dosREPORT.writeBytes("\n"+strDOTLN3);
				cl_dat.M_intLINNO_pbst +=3;
			}
			M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA2");
		}
	}
}