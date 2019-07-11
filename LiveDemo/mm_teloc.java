/*
System Name   : Material Management System
Program Name  : Location Code Modification Entry.
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
import javax.swing.DefaultCellEditor;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
/**
 <P><PRE style = font-size : 10 pt >
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b>Location Code modification entry.

<b>Purpose :</b> This module is helpfull for the Store Keepers to manage the Location of the
various goods.
			
List of tables used :
Table Name      Primary key                      Operation done
                                        Insert	Update	Query	Delete	
-----------------------------------------------------------------------------------------------
MM_STMST                                           #       #              
-----------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		Type/Size       Description
-----------------------------------------------------------------------------------------------
TBL_OLDLO	ST_LOCCD		MM_STMST		Varchar (5)	    Old Location Code
TBL_NEWLO	ST_LOCCD		MM_STMST		Varchar (5)	    New Location Code
-----------------------------------------------------------------------------------------------
*/
public class mm_teloc extends cl_pbase
{	
	private cl_JTable tblITMDT;
	private JCheckBox chkALL;
	private JTextField txtNEWLO;
										/** Integer final variable for Check Flag.*/				
	private final int TBL_CHKFL = 0;	/** Integer final variable for Serial Number.*/	
	private final int TBL_OLDLO = 1;	/** Integer final variable for Lorry Number.*/	
	private final int TBL_NEWLO = 2;		                    	
	mm_teloc()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(chkALL = new JCheckBox("View All Location Codes"),2,3,1,2,this,'L');
			
			String[] L_arrCOLHD = {"Select","Old Location","New Location"};
      		int[] L_arrCOLSZ = {50,126,186};	    				
			tblITMDT = crtTBLPNL1(this,L_arrCOLHD,3000,3,3,14.7,4,L_arrCOLSZ,new int[]{0});				
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			
			tblITMDT.setCellEditor(TBL_NEWLO, txtNEWLO = new TxtLimit(10));
			txtNEWLO.addKeyListener(this);
			txtNEWLO.addFocusListener(this);			
			setENBL(false);			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"const");			
		}
	}			
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);			
		String L_ACTCMD = L_AE.getActionCommand();							
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			int L_intLENTH = 0;
			try
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)
				{
					tblITMDT.clrTABLE();
					getDATA(6);					
				}						
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"ActionPerformed");
			}
		}
		if(M_objSOURC == chkALL)
		{
			if(chkALL.isSelected())
			{
				tblITMDT.clrTABLE();
				getDATA(11);
			}
			else
			{
				tblITMDT.clrTABLE();
				getDATA(6);
			}
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
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				for(int i=0;i<tblITMDT.getRowCount();i++)
				{				
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{						
						M_strSQLQRY = "Update MM_STMST set ST_LOCCD='"+ tblITMDT.getValueAt(i,TBL_NEWLO).toString() +"'";
						M_strSQLQRY += " where ST_STRTP ='"+ M_strSBSCD.substring(2,4) +"' AND ST_LOCCD ='"+ tblITMDT.getValueAt(i,TBL_OLDLO).toString()+"'";
						//System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");									
						setMSG("",'E');
					}						
				}
			}			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{				
				setMSG("Location Code Modified Successfully..",'N'); 				
				clrCOMP();
			}
			else			
				setMSG("Error in updating Location Code ..",'N'); 			
			this.setCursor(cl_dat.M_curDFSTS_pbst);		
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
		return true;
	}	
	void getDATA(int P_intLENTH)
	{
	
		try
		{
			/*if(chkALL.isSelected())
				L_intLENTH = 11;
			else
				L_intLENTH = 6;
			*/	
			if(tblITMDT.isEditing())
				tblITMDT.getCellEditor().stopCellEditing();
			tblITMDT.setRowSelectionInterval(0,0);
			tblITMDT.setColumnSelectionInterval(0,0);
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
			tblITMDT.cmpEDITR[TBL_OLDLO].setEnabled(false);
			tblITMDT.cmpEDITR[TBL_NEWLO].setEnabled(true);
			M_strSQLQRY ="select distinct ST_LOCCD from MM_STMST where ST_STRTP ='"+ M_strSBSCD.substring(2,4) +"' order by ST_LOCCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				int i = 0;						
				while(M_rstRSSET.next())
				{							
					String L_strTEMP = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
					if(L_strTEMP.length()>0 && L_strTEMP.length()< P_intLENTH)
					{
						tblITMDT.setValueAt(L_strTEMP,i,TBL_OLDLO);
						i++;
					}
				}					
				M_rstRSSET.close();
			}
			tblITMDT.requestFocus();
			tblITMDT.setRowSelectionInterval(0,0);
			tblITMDT.setColumnSelectionInterval(2,2);
			tblITMDT.editCellAt(0,TBL_NEWLO);
			tblITMDT.cmpEDITR[TBL_NEWLO].requestFocus();
		}							
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
}
