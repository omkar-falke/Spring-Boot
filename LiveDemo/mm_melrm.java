/*
System Name   : Material Management System
Program Name  : Entry form for Tanker(Lorry) Master
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 14 Mar 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
import javax.swing.DefaultCellEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import java.sql.*;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.awt.event.*;
/**
 <P><PRE style = font-size : 10 pt >
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Tanker Master

<b>Purpose :</b> This module captures transporter wise Vehicle Details such as Lorry No. and Description.
Trip count & Default Trip count are auto updated from Gate In Entry.This Information is used in Gate in
entry,where Transaporter code is picked up from Lorry master table after giving the lorry number.
			
List of tables used :
Table Name      Primary key                      Operation done
                                        Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
MM_LRMST        LR_TPRCD,LR_LRYDS         #       #       #       #       
CO_PTMST        PT_PRTTP,PT_PRTCD                         #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		Type/Size       Description
----------------------------------------------------------------------------------------------------------
txtTPRCD	LR_TPRCD		MM_LRMST		Varchar (5)     Transporter Code
txtTPRDS	PT_PRTNM		CO_PTMST		Varchar (40)	Transporter Name
TBL_LRYNO	LR_LRYNO		MM_LRMST		Varchar (15)	Lorry No.
TBL_LRYDS	LR_LRYDS		MM_LRMST		Varchar(30)     Lorry Description
TBL_TRPCT	LR_TRPCT		MM_LRMST		Decimal (4,0)	Trip Count
TBL_DEFCT	LR_DEFCT		MM_LRMST		Decimal(4,0)	Default Trip Count
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description     Display Columns         Table Name
----------------------------------------------------------------------------------------------------------
txtTPRCD    Code,Transporter        PT_PRTCD,PT_PRTNM       CO_PTMST
txtDRVCD    Code,Driver             DV_DRVCD,DV_DRVNM       MM_DVMST
----------------------------------------------------------------------------------------------------------

Validations :
	* 1)Transporter Code should exists in Party Master, where Party Type is 'T'
	* 2)Update LR_STSFL  to 'X' to indicate that record is deleted.
	* 3)Lorry Number cannot be greater than 15 character.  
	* 4)Lorry Description cannot be greater than 30 Character.
	* 5)Spacial Charecters are not allowed in the Lorry Number and Lorry Description.
	* 6)In Addition mode only Lorry number, description is allowed to be entered.
	* &)In Modification only Lorry Description is allowed.
*/
public class mm_melrm extends cl_pbase implements KeyListener, FocusListener
{									/** JTextField to input Transporter Code.*/
	private JTextField txtPRTCD;	/** JTextField to Display Transporter Name*/
	private JTextField txtPRTNM;	/** JTextField to pick up data entered in table cell.*/	
	private JTextField txtEDITR;	/** JTextField to set over Table cell to apply length validity for Lorry Number Entered*/
	private JTextField txtLRYNO;	/** JTextField to set over Table cell to apply length validity for Lorry Description Entered*/
	private JTextField txtLRYDS;	/** JTable to Show the Data in Tabular Form.*/
	private cl_JTable tblITMDT ;	/** Integer variable for serial Number.*/	
	private int intSRLNO;			/** String final variable for Party Code.*/	
    private String strPRTCD;		/** Integer final variable for Check Flag.*/			
	final int TBL_CHKFL = 0;		/** Integer final variable for Serial Number.*/	
	final int TBL_SRLNO = 1;		/** Integer final variable for Lorry Number.*/	
	final int TBL_LRYNO = 2;		/** Integer final variable for Lorry Descripton.*/	
	final int TBL_LRYDS = 3;		/** Integer final variable for Trip Count.*/	
	final int TBL_TRPCT = 4;		/** Integer final variable for Default Trip Count.*/	
	final int TBL_DEFCT = 5;		/** String variable for Temprory Storage.*/	                        
	private String strTEMP;
	private TableInputVerifier TBLINPVF;
	private TBLINPVF objTBLVRF;
	private CallableStatement cstATIND;
	private INPVF objINPVR = new INPVF();	
	mm_melrm()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Transporter Code"),2,1,1,1.5,this,'L');
			add(txtPRTCD = new TxtLimit(5),2,2,1,.8,this,'R');
			add(new JLabel("Description"),2,3,1,1,this,'L');
			add(txtPRTNM = new JTextField(""),2,4,1,5,this,'L');						
			String[] L_COLHD = {"Select","Sr.No.","Lorry Number","Lorry Description","Trips","Default Trips"};
      		int[] L_COLSZ = {50,50,125,250,80,100};	    				
			tblITMDT = crtTBLPNL1(this,L_COLHD,600,5,1,10,7,L_COLSZ,new int[]{0});				
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);						
			objTBLVRF = new TBLINPVF();
			tblITMDT.setInputVerifier(objTBLVRF);		
			txtPRTCD.setInputVerifier(objINPVR);			
			tblITMDT.setCellEditor(TBL_LRYNO, txtLRYNO = new TxtLimit(15));
			tblITMDT.setCellEditor(TBL_LRYDS, txtLRYDS = new TxtLimit(30));
			txtLRYNO.addKeyListener(this);
			txtLRYNO.addFocusListener(this);
			txtLRYDS.addKeyListener(this);
			txtLRYDS.addFocusListener(this);
			setENBL(false);			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"const");			
		}
	}		
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		clrCOMP();
		txtPRTNM.setEnabled(false);		
		txtPRTCD.setEnabled(true);
		tblITMDT.cmpEDITR[TBL_SRLNO].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_TRPCT].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_DEFCT].setEnabled(false);								
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
		{
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);									
			tblITMDT.cmpEDITR[TBL_LRYNO].setEnabled(true);									
			tblITMDT.cmpEDITR[TBL_LRYDS].setEnabled(true);			
			tblITMDT.cmpEDITR[TBL_CHKFL].requestFocus();			
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{
			txtPRTCD.setEnabled(true);			
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);			
			tblITMDT.cmpEDITR[TBL_LRYNO].setEnabled(true);			
			tblITMDT.cmpEDITR[TBL_LRYDS].setEnabled(true);			
			tblITMDT.cmpEDITR[TBL_CHKFL].requestFocus();			
		}				
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			txtPRTCD.setEnabled(true);			
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
			txtPRTCD.requestFocus();
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);			
		String L_ACTCMD = L_AE.getActionCommand();			
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			setENBL(false);				
		if(M_objSOURC == txtPRTCD)
		{				
			txtPRTNM.setText("");
		    txtPRTCD.setText((txtPRTCD.getText()).toUpperCase());			    
		    strPRTCD=txtPRTCD.getText();
			clrCOMP();
			txtPRTCD.setText(strPRTCD);			    		    
		    try
		    {
    		    if(txtPRTCD.getText().length()==5)
    		    {	
    		        setCursor(cl_dat.M_curWTSTS_pbst);				    	
    			    M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTTP ='S' AND isnull(PT_STSFL,'') <>'X'";
    				M_strSQLQRY += " AND PT_PRTCD ='" + txtPRTCD.getText().trim() + "'";				    
    				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);									
    				if(M_rstRSSET != null)    			        		
    				{    					   
    					if (M_rstRSSET.next())
    					{					    						  
    						txtPRTNM.setText(M_rstRSSET.getString("PT_PRTNM"));
    					}
						else
							setMSG("Invalid Transporter code, Press F1 key for help ..",'E' );   
    					M_rstRSSET.close();
    					getALLREC(strPRTCD);
						if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==1)
							tblITMDT.editCellAt(intSRLNO,TBL_LRYNO);
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
						{	
							tblITMDT.editCellAt(intSRLNO,TBL_LRYDS);							
						}
    				}
    				else 
    				    setMSG("Invalid Transporter code, Press F1 key for help ..",'E' );   
    			}
    			else 
    			    setMSG("Invalid Transporter code, press F1 key for help ..",'E' );       			    
    		    setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_EX)
			{
			    setMSG(L_EX,"Enter Key Event of txtPRTCD");
			}
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)
			{
				txtPRTCD.requestFocus();				
			}						
		}
			
		if(L_ACTCMD.equals("cl_dat.M_btnUNDO_pbst"))
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);			
			clrCOMP();			
			if ((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				txtEDITR.setEnabled(true);
			else
				txtEDITR.setEnabled(false);				
			txtPRTCD.setEnabled(true);
			txtPRTCD.requestFocus();
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		else if (L_ACTCMD.equals("cl_dat.M_btnSAVE_pbst"))
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;				
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}				
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{						
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{				
				if(M_objSOURC == txtPRTCD)
				{
					txtPRTCD.setText((txtPRTCD.getText()).toUpperCase());
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPRTCD";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from co_ptmst where PT_PRTTP ='T' AND pt_stsfl <>'X'";
					if(txtPRTCD.getText().length() >0)
						M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText().trim()+"%'";
					M_strSQLQRY += " order by PT_PRTCD ";
					clrCOMP();					
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");					
				}
			}						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{		
		super.focusGained(L_FE);
		if(M_objSOURC == tblITMDT.cmpEDITR[TBL_LRYNO])
	    {			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{				
				if(tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_SRLNO).toString().length() >0)
					((JTextField)tblITMDT.cmpEDITR[TBL_LRYNO]).setEditable(false);
				else
					((JTextField)tblITMDT.cmpEDITR[TBL_LRYNO]).setEditable(true);
			}
		}
	}
	/**
	 * Super Class Method Overrided here to execute help for F1 key press.
	 *and for that super class cunstructor called first.
	 *@see< a href= "exeHLPOK" ,"file:d:\\\splerp\\cl_pbase.exeHLPOK()">
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtPRTCD"))
			{				
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				getALLREC(txtPRTCD.getText().trim());				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}	
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	 */
	void exeSAVE()
	{
		try
		{
			if(!vldDATA())
				return ;
			else
				setMSG("",'E');
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{	
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				for(int i=0;i<tblITMDT.getRowCount();i++)
				{					
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{						
						M_strSQLQRY ="INSERT INTO MM_LRMST(LR_TPRCD,LR_LRYNO,LR_LRYDS,LR_TRNFL,LR_STSFL,LR_LUSBY,LR_LUPDT)VALUES(" +"'"
									 +txtPRTCD.getText().trim()+"','"+tblITMDT.getValueAt(i,TBL_LRYNO).toString()+"','"
									 +tblITMDT.getValueAt(i,TBL_LRYDS).toString()+ "',"
									 +getUSGDTL("LR",'I',"")+")";						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{				
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				for(int i=0;i<intSRLNO;i++)
				{
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY ="UPDATE MM_LRMST SET "
							         +getUSGDTL("LR",'U',"X")
							         +" WHERE LR_TPRCD ='"+txtPRTCD.getText().trim()+"'"
									 + "And LR_LRYNO ='" + tblITMDT.getValueAt(i,TBL_LRYNO).toString()+"'";						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{				
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				for(int i=0;i<tblITMDT.getRowCount();i++)
				{				
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						if (i<intSRLNO)
						{	
							M_strSQLQRY = "Update MM_LRMST set "+ 
									" LR_LRYDS = '" +tblITMDT.getValueAt(i,TBL_LRYDS).toString()+"',"+
									getUSGDTL("LR",'U',"")+								  										  
									" where LR_TPRCD = '" + txtPRTCD.getText().trim() + "'" +
									" and LR_LRYNO = '" + tblITMDT.getValueAt(i,TBL_LRYNO).toString() + "'";																					
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");												
							setMSG("",'E');
						}
						else
						{
							M_strSQLQRY ="INSERT INTO MM_LRMST(LR_TPRCD,LR_LRYNO,LR_LRYDS,LR_TRNFL,LR_STSFL,LR_LUSBY,LR_LUPDT)VALUES(" +"'"
									 +txtPRTCD.getText().trim()+"','"+tblITMDT.getValueAt(i,TBL_LRYNO).toString()+"','"
									 +tblITMDT.getValueAt(i,TBL_LRYDS).toString()+ "',"
									 +getUSGDTL("LR",'I',"")+")";							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
				clrCOMP();
			}
			else
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeDBCMT");
		}
	}	
	/**
	 * Method to perform validy check of the Data entered, Before inserting 
	 *new data in the data base.
	 */
	boolean vldDATA()
	{	
		if(tblITMDT.isEditing())
			tblITMDT.getCellEditor().stopCellEditing();
		tblITMDT.setRowSelectionInterval(0,0);
		tblITMDT.setColumnSelectionInterval(0,0);			
		//if no row selected.
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
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
			if (L_flgCHKFL== false)
			{
				setMSG("No row Selected..",'E');				
				return false;
			}			
		}	
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{			
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{								
				if (tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{			
					if(i>= intSRLNO)
					{
						setMSG("Blank Row cannot be Deleted..",'E');
						return false;
					}
				}
			}
		}
		// if the user tried to modify datawhe addition is selected.
		if( cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{				
			for(int i=0; i<intSRLNO; i++)
			{				
				if (tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{					
					setMSG("Insert Data at very first Blank Row & remove check mark of upper Rows..",'E');								
					return false;					
				}
			}		
		}											
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{						
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{	
				if (tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if((tblITMDT.getValueAt(i,TBL_LRYNO).toString()).length()==0)
					{
						setMSG("Lorry number can not be Blank..",'E');					
						return false;					
					}
					/*else if((tblITMDT.getValueAt(i,TBL_LRYDS).toString()).length()==0)
					{
						setMSG("Length of Lorry Description must not be blanck..",'E');					
						return false;					
					}*/
					else if ((tblITMDT.getValueAt(i,TBL_LRYNO).toString()).length()>15)
					{
						setMSG("Length of Lorry number must be less than 15 charectors..",'E');					
						return false;					
					}
				
					else if ((tblITMDT.getValueAt(i,TBL_LRYDS).toString()).length()>30)
					{
						setMSG("Length of Lorry description must be less than 15 charectors..",'E');					
						return false;					
					}
				}	
			}		
		}		
		return true;
	}			
	/**	 
	* Method to fetch records from the MM_LRMST table,
	*Details of Lorry Number, Description, Trip Count & Default Trip Count 
	*for the given Transporter Code.	
	*@param strPRTCD the Party code given by the user.
	*/
	private void getALLREC(String strPRTCD)throws SQLException
	{
		setMSG("",'N');		
		tblITMDT.setRowSelectionInterval(0,0);
		tblITMDT.setColumnSelectionInterval(0,0);		
		M_strSQLQRY = " Select LR_LRYNO,LR_LRYDS,LR_TRPCT,LR_DEFCT,LR_STSFL from MM_LRMST";
		M_strSQLQRY += " where LR_TPRCD = '" + strPRTCD + "' and isnull(LR_STSFL,'') <> 'X'";		
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
		if(M_rstRSSET != null)
		{			
			int i = 0;
			intSRLNO = 0;
			while(M_rstRSSET.next())
			{							
				intSRLNO = intSRLNO + 1;
				tblITMDT.setValueAt(String.valueOf(intSRLNO).toString(),i,TBL_SRLNO);
				tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("LR_LRYNO"),"").trim(),i,TBL_LRYNO);
				tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("LR_LRYDS"),"").trim(),i,TBL_LRYDS);
				tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("LR_TRPCT"),"").trim(),i,TBL_TRPCT);
				tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("LR_DEFCT"),"").trim(),i,TBL_DEFCT);				
				i++;
			}			
			M_rstRSSET.close();						
			if ((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				tblITMDT.requestFocus();
			// tblITMDT.getValueAt(i,TBL_CHKFL).toString()
			else
				txtPRTCD.requestFocus();
		} 
		else
		{
			setMSG("No Data found",'E');
			txtPRTCD.requestFocus();
		}
	}		
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{				
				if(input == txtPRTCD)
				{					
					if (txtPRTCD.getText().trim().length() == 5)
					{
						try
						{
							M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST" +
								" where PT_PRTCD = '" + strPRTCD + "' and PT_PRTTP = 'T'" +
								" and isnull(PT_STSFL,'' ) <> 'X'";			
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if (M_rstRSSET !=null)
							{
								if(M_rstRSSET.next())
								{
									txtPRTNM.setText(M_rstRSSET.getString("PT_PRTNM"));
									M_rstRSSET.close();						
								}
							}
							else
							{
								setMSG("No Data found ..",'E');
								return false;				
							}
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"txtPRTCD");
						}
					}	
					else if(txtPRTCD.getText().trim().length() != 5)
					{
						if(txtPRTCD.getText().trim().length() == 0)
							setMSG("Enter Transporter Code or Press F1 for Help..",'N');
						else
							setMSG("Invalid Transporter Code, Press F1 for Help..",'E');
						return false;
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
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try  
			{
				if ((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				{
					if(P_intCOLID==TBL_LRYNO)
					{
						strTEMP = tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_LRYNO).toString();
						if(strTEMP.length() == 0)
							return true;
						if((strTEMP.indexOf("'") >=0)||(strTEMP.indexOf("\"") >=0)||(strTEMP.indexOf(" ") >=0)||(strTEMP.indexOf(",") >=0)||(strTEMP.indexOf("\\") >=0)||(strTEMP.indexOf("/") >=0))
						{
							setMSG("Characters like \', \", \\, /, blank & , are not allowed in Lorry number field",'E');
							return false;
						}
					}
					if(P_intCOLID==TBL_LRYDS)
					{
						strTEMP = tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TBL_LRYNO).toString();
						if(strTEMP.length() == 0)
							return true;
						if((strTEMP.indexOf("'") >=0)||(strTEMP.indexOf("\"") >=0)||(strTEMP.indexOf(",") >=0)||(strTEMP.indexOf("\\") >=0)||(strTEMP.indexOf("/") >=0))
						{
							setMSG("Characters like \', \", \\, / are not allowed in Lorry number field",'E');
							return false;
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
