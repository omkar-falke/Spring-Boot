/*
System Name   : Common Master Records
Program Name  : Party type grouping
Program Desc. : Party Group code.
Author        : Mrs.Dipti.S.Shinde.
Date          : 8th Aug 2005
System        : Master maintenance
Version       : MMS v2.0.0
Modificaitons : 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/
/**
<P><PRE style = font-size : 10 pt >

<b>System Name :</b> Common Master Records
 
<b>Program Name :</b> Party wise Grouping Entry program.

<b>Purpose :</b> This module captures party type & group wise party such as .
Insertion & updation of data in the Party Master.1)CO_PTMST 
Here we canbe able add new Records, Modify existing Records and to Delete unwanted records.
			
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
CO_PTMST        PT_PRTCD,PT_PRTTP            #               #       #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		 Type/Size       Description
----------------------------------------------------------------------------------------------------------
 txtPRTTP    PT_PRTCD       CO_PTMST          varchar(5)     Party code
 txtPRTTP    PT_PRTTP       CO_PTMST          varchar(1)     Party type
 txtPRTDS    PT_PRTNM       CO_PTMST          varchar(40)    Party name
 tblPRTCD    PT_ADR01       CO_PTMST          varchar(40)    Party adress
 tblPRTCD    PT_ZONCD       CO_PTMST          varchar(2)     Zone code
 tblGRPCD    PT_GRPCD       CO_PTMST          varchar(5)     Group code                              
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description     Display Columns       Table Name
----------------------------------------------------------------------------------------------------------
txtPRTTP        Party code             PT_PRTCD             CO_PTMST
tblPRTCD        Group code             PT_GRPCD             CO_PTMST
----------------------------------------------------------------------------------------------------------
Validations :
*/
import java.sql.ResultSet;

import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.*;
import java.awt.event.*;

public class co_meptg extends cl_pbase implements MouseListener 
{
	                                 /**Label for Group table*/
	private JLabel lblGRPCD;         /**Label for Party table*/
	private JLabel lblPRTCD;         /**Label for Party type*/
	private JLabel lblPRTTP;         /**Label for Group type*/
	private JLabel lblGRPTP;         /**Label for Specific Group Desc*/
	private JLabel lblGRPDS;         /**TextField for Group code*/
	private JTextField txtGRPCD;     /**TextField for Group Desc*/
	private JTextField txtGRDSC;     /**TextField for Party code*/
	private JTextField txtPRTTP;     /**TextField for Party Desc*/
	private JTextField txtPRTDS;     /**TextField for Specific Group Desc*/
	private JTextField txtFLTDS;     /**Table for Party List*/
	private cl_JTable tblPRTLS;      /**Tabel for Grou List*/
	private cl_JTable tblGRPLS;      /**Flag for Party tabel*/
	private final int TB1_CHKFL =0;  /**Flag for Party tabel*/
	private	final int TB1_PRTCD =1;  /**Flag for Party tabel*/
	private	final int TB1_PRTNM =2;  /**Flag for Party tabel*/
	private	final int TB1_ADD01 =3;  /**Flag for Party tabel*/
	private	final int TB1_ZONCD =4;  /**Record counter*/
	private int intRECCT=0;          /**Flag for Group tabel*/
	private final int TB2_CHKFL =0; /**Flag for Group tabel*/
	private	final int TB2_PRTCD =1; /**Flag for Group tabel*/
	private	final int TB2_PRTNM =2; /**Flag for Group tabel*/
	private	final int TB2_ADD01 =3; /**Flag for Group tabel*/
	private	final int TB2_ZONCD =4; /**Check box for adding new record*/
	private JButton btnDISPY;
	private JCheckBox chkNEWRE;
	private JCheckBox chkSTRFL;
	co_meptg()    
	{
		super(2);
		try
		{
			setMatrix(20,8);
		
			add(lblPRTCD = new JLabel("Pary type "),1,2,1,1,this,'L');
			add(txtPRTTP = new JTextField(),1,3,1,1,this,'L');
			add(txtPRTDS = new JTextField(),1,4,1,3,this,'L');
			txtPRTTP.addMouseListener(this);
			add(chkNEWRE = new  JCheckBox("NEW"),1,7,1,1,this,'L');
			
			add(lblGRPCD = new JLabel("Group code "),2,2,1,1,this,'L');
			add(txtGRPCD = new JTextField(),2,3,1,1,this,'L');
			add(txtGRDSC = new JTextField(),2,4,1,3,this,'L');
			add(lblGRPDS = new JLabel("Filtering"),3,2,1,1,this,'L');
			add(txtFLTDS = new JTextField(),3,3,1,4,this,'L');
			add(chkSTRFL = new  JCheckBox("Stating With"),3,7,1,2,this,'L');
			add(btnDISPY = new JButton("Display"),4,3,1,1,this,'L');
			
			add(lblPRTTP = new JLabel("Parties not in Group"),4,1,1,2,this,'L');
			String[] L_strCOLHD = {"Select","Party code","Party Name","Address","Zone code"};
      		int[] L_intCOLSZ = {50,80,200,200,40};	    				
			tblPRTLS = crtTBLPNL1(this,L_strCOLHD,10000,5,1,13.5,4,L_intCOLSZ,new int[]{0});
			
			add(lblGRPTP = new JLabel("Parties in Group"),4,5,1,2,this,'L');
			String[] L_strCOLHD1 = {"Select","Party code","Party Name","Address","Zone code"};
      		int[] L_intCOLSZ1 = {50,80,200,200,40};	    				
			tblGRPLS = crtTBLPNL1(this,L_strCOLHD1,10000,5,5,13.5,4,L_intCOLSZ1,new int[]{0});
			setENBL(false);
		}   
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}   
	} 
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		tblPRTLS.clrTABLE();
		tblGRPLS.clrTABLE();
		txtPRTTP.setText("C");
		tblPRTLS.cmpEDITR[TB1_CHKFL].setEnabled(false);
		tblPRTLS.cmpEDITR[TB1_PRTCD].setEnabled(false);
		tblPRTLS.cmpEDITR[TB1_PRTNM].setEnabled(false);
		tblPRTLS.cmpEDITR[TB1_ADD01].setEnabled(false);
		tblPRTLS.cmpEDITR[TB1_ZONCD].setEnabled(false);
		//txtPRTTP.setEnabled(false);
		tblGRPLS.cmpEDITR[TB2_CHKFL].setEnabled(false);
		tblGRPLS.cmpEDITR[TB2_PRTCD].setEnabled(false);
		tblGRPLS.cmpEDITR[TB2_PRTNM].setEnabled(false);
		tblGRPLS.cmpEDITR[TB2_ADD01].setEnabled(false);
		tblGRPLS.cmpEDITR[TB2_ZONCD].setEnabled(false);
		chkNEWRE.setEnabled(false);
		chkSTRFL.setEnabled(true);
		chkNEWRE.setVisible(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			tblPRTLS.cmpEDITR[TB1_CHKFL].setEnabled(true);
			chkNEWRE.setEnabled(true);
			chkNEWRE.setVisible(true);
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			tblGRPLS.cmpEDITR[TB2_CHKFL].setEnabled(true);
		}
	}

	
	/** Method for action performing 
	*/
	public void actionPerformed(ActionEvent L_AE)
	{
	     super.actionPerformed(L_AE);
		 try
		 {
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{ 
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
				{
					txtPRTTP.requestFocus();
					setMSG("Please Select Option through F1..",'N');
					setENBL(true);
				}
				else
				{					
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					setMSG("select option..",'N');
					setENBL(false);
				}
			}
			if(M_objSOURC==txtPRTTP)
			{
				setMSG("select a Party type through f1..",'N');
				txtGRPCD.requestFocus();
			}
			if(M_objSOURC==btnDISPY)
			{
				txtFLTDS.setText(txtFLTDS.getText().toUpperCase());
				tblPRTLS.clrTABLE();
				tblGRPLS.clrTABLE();
				if(tblPRTLS.isEditing())
					tblPRTLS.getCellEditor().stopCellEditing();
				tblPRTLS.setRowSelectionInterval(0,0);
				tblPRTLS.setColumnSelectionInterval(0,0);
				if(tblGRPLS.isEditing())
					tblGRPLS.getCellEditor().stopCellEditing();
				tblGRPLS.setRowSelectionInterval(0,0);
				tblGRPLS.setColumnSelectionInterval(0,0);
				getDATA();
				getDATA1();
			}
			if(M_objSOURC==chkNEWRE)
			{
				tblPRTLS.clrTABLE();
				tblGRPLS.clrTABLE();
				txtGRPCD.requestFocus();
				txtGRPCD.setText("");
				txtGRDSC.setText("");
			}
			if(M_objSOURC==txtGRPCD)
			{
				if(txtPRTTP.getText().trim().length()<1)
					setMSG("please select a party type..",'E');
				txtFLTDS.requestFocus();	
				
			}
			if(M_objSOURC==txtGRDSC)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			    {
					tblPRTLS.requestFocus();
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					tblGRPLS.requestFocus();
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				 	setMSG("Add the Record from non group parties to group parties.. ",'N');
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				 	setMSG("Modification not allow ",'E');
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				 	setMSG("Remove the Group parties from group parties to non  group parties.. ",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}	
	}
	public void keyPressed(KeyEvent L_KE)
	{ 
		super.keyPressed(L_KE);
		try
		 {
			if((L_KE.getKeyCode()== L_KE.VK_F1))
			{		
				txtGRPCD.setText(txtGRPCD.getText().trim().toUpperCase());
				txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
				this.setCursor(cl_dat.M_curWTSTS_pbst);
							
				if(M_objSOURC==txtGRPCD)
			 	{
					txtGRPCD.setText(txtGRPCD.getText().trim().toUpperCase());
					M_strHLPFLD = "txtGRPCD";
					if(chkNEWRE.isSelected())
					{
						M_strSQLQRY=" select  PT_PRTCD,PT_PRTNM  from CO_PTMST where upper(isnull(PT_GRPCD,''))='XXXXX' AND PT_PRTTP= '"+txtPRTTP.getText().trim()+"'";
						//M_strSQLQRY=" SELECT distinct PT_GRPCD,PT_PRTNM from CO_PTMST where PT_PRTTP='G' AND isnull(PT_STSFL,' ') <> 'X'";
						if(txtGRPCD.getText().length() >0)
							M_strSQLQRY += " and PT_PRTCD like '"+txtGRPCD.getText().trim()+"%'";
						M_strSQLQRY += " order by PT_PRTNM";
						//System.out.println(M_strSQLQRY);
					}
					else
					{
						M_strSQLQRY=" select PT_GRPCD,PT_PRTNM from CO_PTMST where PT_GRPCD <>'XXXXX' AND PT_PRTTP= '"+txtPRTTP.getText().trim()+"' and PT_PRTCD = PT_GRPCD" ;
						//M_strSQLQRY=" SELECT distinct PT_GRPCD,PT_PRTNM from CO_PTMST where PT_PRTTP='G' AND isnull(PT_STSFL,' ') <> 'X'";
						if(txtGRPCD.getText().length() > 0)
							M_strSQLQRY += " and PT_GRPCD like '"+txtGRPCD.getText().trim()+"%'";
						M_strSQLQRY += " order by PT_PRTNM";
						//System.out.println(M_strSQLQRY);
					}
					cl_hlp(M_strSQLQRY,1,1,new String[]{"group code","Name"},2,"CT");
				}
				if(M_objSOURC==txtPRTTP)
				{
					txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
					M_strHLPFLD = "txtPRTTP";
					M_strSQLQRY=" SELECT  cmt_codcd,cmt_codds from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPRT' and cmt_codcd='C'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Party type","Description"},2,"CT");
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if (L_KE.getKeyCode()== L_KE.VK_ENTER)
			{

				if(M_objSOURC==txtPRTTP)
				{
					if(!setTXTDS("PRTTP_DS")) txtPRTTP.requestFocus();
				}
				
				if(M_objSOURC==txtGRPCD)
				{
					//if(chkNEWRE.isSelected())
					//{
					if(!setTXTDS("GRPCD_DS")) {txtGRPCD.requestFocus(); return;}
					M_strSQLQRY="SELECT PT_PRTCD,PT_PRTNM,PT_GRPCD  from CO_PTMST where  PT_PRTTP= '"+txtPRTTP.getText().trim()+"' and PT_PRTCD = '"+txtGRPCD.getText().trim()+"' AND isnull(PT_STSFL,' ') <> 'X'";
					ResultSet L_rstRSSET =  cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(L_rstRSSET !=null && L_rstRSSET.next())
					{
						String L_strGRPCD = L_rstRSSET.getString("PT_GRPCD");
						if(L_strGRPCD.equalsIgnoreCase("XXXXX"))
						{
							L_rstRSSET.close();
							int L_intOPTN=JOptionPane.showConfirmDialog( this,"This party will be added as a Group ... Please Confirm..","Message",JOptionPane.OK_CANCEL_OPTION);
							if(L_intOPTN!=0)
								return;
							M_strSQLQRY =  "UPDATE CO_PTMST SET ";
							M_strSQLQRY += " PT_GRPCD ='" + txtGRPCD.getText().toString().trim()+"' WHERE ";
							M_strSQLQRY += " PT_PRTTP = '" + txtPRTTP.getText().toString().trim()+"' and PT_PRTCD = '" + txtGRPCD.getText().toString().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
					}
					
					/*tblPRTLS.clrTABLE();
					tblGRPLS.clrTABLE();
					if(tblPRTLS.isEditing())
						tblPRTLS.getCellEditor().stopCellEditing();
					tblPRTLS.setRowSelectionInterval(0,0);
					tblPRTLS.setColumnSelectionInterval(0,0);
					if(tblGRPLS.isEditing())
						tblGRPLS.getCellEditor().stopCellEditing();
					tblGRPLS.setRowSelectionInterval(0,0);
					tblGRPLS.setColumnSelectionInterval(0,0);
					getDATA();
					getDATA1();*/
					
				}
				if(M_objSOURC==txtFLTDS)
				{
					txtFLTDS.setText(txtFLTDS.getText().toUpperCase());
					btnDISPY.requestFocus();
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"key pressed");
		}	
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	public void exeHLPOK()
	{
		try
		{
		  super.exeHLPOK();
			if(M_strHLPFLD == "txtGRPCD")
			{
				txtGRPCD.setText(cl_dat.M_strHLPSTR_pbst);	
				txtGRPCD.setEnabled(true);
				txtGRPCD.requestFocus();
			}
			if(M_strHLPFLD == "txtPRTTP")
			{
				txtPRTTP.setText(cl_dat.M_strHLPSTR_pbst); 
				txtPRTTP.setEnabled(true);
				txtPRTTP.requestFocus();
			}
		}
	  catch(Exception L_EX)
		{
			setMSG(L_EX,"Help..");
		}	
	}
	/**
	* Method to Save display report as per selection
	*/
	void exeSAVE()
	{
		if (!vldDATA())
			return;	
		else 
			setMSG("",'E');
		try
		{	
			String L_strGRPCD="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;
				for(int i=0;i<tblPRTLS.getRowCount();i++)
					{
						if(tblPRTLS.getValueAt(i,TB1_PRTCD).toString().trim().length()<5)
							break;
						if(tblPRTLS.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
						{
							M_strSQLQRY =  "UPDATE CO_PTMST SET ";
							M_strSQLQRY += " PT_GRPCD = '" + txtGRPCD.getText().toString().trim()+"' WHERE ";
							M_strSQLQRY += " PT_PRTTP= '"+txtPRTTP.getText().trim()+"'";
							M_strSQLQRY += " AND PT_PRTCD = '" + tblPRTLS.getValueAt(i,TB1_PRTCD).toString().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
					}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				tblPRTLS.clrTABLE();
				tblGRPLS.clrTABLE();
				getDATA();
				getDATA1();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;	
				for(int i=0;i<tblGRPLS.getRowCount();i++)
				{
					if(tblGRPLS.getValueAt(i,TB2_PRTCD).toString().trim().length()<5)
						break;
					if(tblGRPLS.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
					{
						M_strSQLQRY =  "UPDATE CO_PTMST SET ";
						M_strSQLQRY += " PT_GRPCD = 'XXXXX' WHERE ";
						M_strSQLQRY += " PT_PRTTP= '"+txtPRTTP.getText().trim()+"'";
						M_strSQLQRY += " AND PT_PRTCD = '" + tblGRPLS.getValueAt(i,TB2_PRTCD).toString().trim()+"'";
						 
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					}
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				tblPRTLS.clrTABLE();
				tblGRPLS.clrTABLE();
				getDATA();
				getDATA1();
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG(" Data Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					setMSG(" Data Modified Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				   setMSG("Error in Modified Data details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}	
	}
	/** Method to set Description in Text Fields */
	private boolean setTXTDS(String LP_CHKSTR)
	{        
		boolean L_flgRETFL = true;
		ResultSet L_rstRSSET = null;
		try  
		{  
			if(LP_CHKSTR.equals("PRTTP_DS"))
			{
				M_strSQLQRY="SELECT CMT_CODDS  from CO_CDTRN where  CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT' and CMT_CODCD = '"+txtPRTTP.getText().trim()+"'";
				L_rstRSSET =  cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET !=null && L_rstRSSET.next())
				{
					txtPRTDS.setText(L_rstRSSET.getString("CMT_CODDS"));
					L_rstRSSET.close();
				}
			}
			if(LP_CHKSTR.equals("GRPCD_DS"))
			{
				M_strSQLQRY="SELECT PT_PRTNM,PT_GRPCD  from CO_PTMST where  PT_PRTTP= '"+txtPRTTP.getText().trim()+"' and PT_PRTCD ='"+txtGRPCD.getText().trim()+"' AND isnull(PT_STSFL,' ') <> 'X'";
				L_rstRSSET =  cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET !=null && L_rstRSSET.next())
				{
					txtGRDSC.setText(L_rstRSSET.getString("PT_PRTNM"));
					if(!L_rstRSSET.getString("PT_GRPCD").equals(txtGRPCD.getText().trim()))
					{
						if(!L_rstRSSET.getString("PT_GRPCD").equalsIgnoreCase("XXXXX"))
							{L_flgRETFL = false; setMSG("This party belongs to group code : "+L_rstRSSET.getString("PT_GRPCD"),'E');}
					}
					L_rstRSSET.close();
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setTXTDS");
		}
		return L_flgRETFL;
	}
	
	
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
			if(M_objSOURC==txtFLTDS)
			{
				txtFLTDS.setText(txtFLTDS.getText().toUpperCase());
			}
			if(M_objSOURC==txtGRPCD)
			{
				txtGRPCD.setText(txtGRPCD.getText().toUpperCase());
			}
		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"FocusLost");
		}
	}
	/** Method for Getting All Records from Table1. */
	private void getDATA()
	{        
		try  
		{  
			int L_intROWNO =0;
			intRECCT = 0;
			M_strSQLQRY=" select PT_PRTCD,PT_ADR01,PT_ZONCD,PT_PRTNM from CO_PTMST ";
			M_strSQLQRY+=" where PT_GRPCD ='XXXXX' ";
			M_strSQLQRY+=" and PT_PRTTP= '"+txtPRTTP.getText().trim()+"'";
			M_strSQLQRY+=  (txtFLTDS.getText().length()==0 ? "" : " and upper(PT_PRTNM) like '"+(chkSTRFL.isSelected() ? "" : "%")+txtFLTDS.getText().trim()+"%'");
			M_strSQLQRY+= " AND isnull(PT_STSFL,' ') <> 'X' order by PT_PRTNM ";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				intRECCT = 0;
				while(M_rstRSSET.next())
				{
					tblPRTLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),""),L_intROWNO,TB1_PRTCD);
					tblPRTLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""),L_intROWNO,TB1_ADD01);
					tblPRTLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_ZONCD"),""),L_intROWNO,TB1_ZONCD);
					tblPRTLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),L_intROWNO,TB1_PRTNM);
					L_intROWNO ++;
					intRECCT++;
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getdata");
		}
	}
	/** Method for Getting All Records from Table2. */
		private void getDATA1()
		{        
		try  
		{  
			int L_intROWNO1 =0;
			intRECCT = 0;
			M_strSQLQRY=" select PT_PRTCD,PT_ADR01,PT_ZONCD,PT_PRTNM from CO_PTMST ";
			M_strSQLQRY+=" where PT_PRTTP= '"+txtPRTTP.getText().trim()+"' and PT_GRPCD = '"+txtGRPCD.getText().trim()+"'";
			M_strSQLQRY+= " AND isnull(PT_STSFL,' ') <> 'X' order by PT_PRTNM ";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					tblGRPLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),""),L_intROWNO1,TB2_PRTCD);
					tblGRPLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""),L_intROWNO1,TB2_ADD01);
					tblGRPLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_ZONCD"),""),L_intROWNO1,TB2_ZONCD);
					tblGRPLS.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),L_intROWNO1,TB2_PRTNM);
					L_intROWNO1 ++;	
					intRECCT++;
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getdata1");
		}
	}
	/**
	Method to validate DATA before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{
			if(txtGRPCD.getText().trim().length()<1)
			{
				setMSG("please select Group..",'E');
					return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				for(int i=0;i<tblGRPLS.getRowCount();i++)
				{
					if(tblGRPLS.getValueAt(i,TB2_PRTCD).toString().length()<5)
						break;
					if(tblGRPLS.getValueAt(i,TB2_CHKFL).toString().equalsIgnoreCase("true"))
					{
						if(txtGRPCD.getText().toString().equals(tblGRPLS.getValueAt(i,TB2_PRTCD).toString()))
						{
							int k=0;
							for(k=0;k<tblGRPLS.getRowCount();k++)
							{
								if(tblGRPLS.getValueAt(k,TB2_PRTCD).toString().length()<5)
									break;
							}
							if(k>1)
							{
								setMSG("can not Remove the party with Original Group value ("+tblGRPLS.getValueAt(i,TB2_PRTCD).toString()+")",'E');
								return false;
							}
						}
					}
				}
			}
			/*if(!txtPRTTP.getText().toString().equals("C"))
			{
				setMSG("Grouping for the Customer so type 'C' ",'E');
				return false;
			}*/
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				setMSG("Modification not applicable",'E');
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			
		}
		return true;
	}
}//main....................................................................
