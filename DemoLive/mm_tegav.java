import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
class mm_tegav extends cl_pbase
{
	final int TBL_CHKFL =0;
	final int TBL_GRPCD =1;
	final int TBL_GRPDS =2;
	private cl_JTable tblITMDT ;
	private TableInputVerifier TBLINPVF;
	private TBLINPVF objTBLVRF;
	private JTextField txtPRTCD,txtPRTNM,txtAPRDT,txtEVLDT,txtGRPCD; 
	private java.util.Vector<String> vtrGRPCD = new java.util.Vector<String>();
	mm_tegav()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			String[] L_COLHD = {" ","Group Code","Group Description "};
			int[] L_COLSZ = {30,80,500};
			add(new JLabel("Party Code"),2,1,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(5),2,2,1,1,this,'L');
			add(new JLabel("Description"),2,3,1,1,this,'L');
			add(txtPRTNM = new JTextField(""),2,4,1,5,this,'L');
			add(new JLabel("Approval Date"),3,1,1,1,this,'L');
			add(txtAPRDT = new TxtDate(),3,2,1,1,this,'L');
			add(new JLabel("Evaluation Date"),3,3,1,1,this,'L');
			add(txtEVLDT = new TxtDate(),3,4,1,1,this,'L');
			add(new JLabel("Group Details"),4,1,1,1,this,'L');
			tblITMDT = crtTBLPNL1(this,L_COLHD,550,5,1,10,7,L_COLSZ,new int[]{0});	
			tblITMDT.setCellEditor(TBL_GRPCD, txtGRPCD = new TxtLimit(2));
			txtGRPCD.addKeyListener(this);txtGRPCD.addFocusListener(this);
			INPVF objINPVR = new INPVF();	
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtPRTCD.setInputVerifier(objINPVR);		
			tblITMDT.setInputVerifier(objTBLVRF);
			tblITMDT.addMouseListener(this);
			setENBL(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"const");			
		}
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtPRTNM.setEnabled(false);
		txtAPRDT.setEnabled(false);
		txtEVLDT.setEnabled(false);
		tblITMDT.cmpEDITR[2].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				txtPRTCD.setEnabled(true);
				tblITMDT.cmpEDITR[0].setEnabled(true);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				txtPRTCD.setEnabled(true);
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == txtPRTCD)
		{
			try
			{
				int L_intRECCT =0; 
				if(vtrGRPCD !=null)
					vtrGRPCD.removeAllElements();
				String L_strTEMP = txtPRTCD.getText().trim().toUpperCase();
				clrCOMP();
				txtPRTCD.setText(L_strTEMP);
				if(!vldPRTCD(L_strTEMP))
				   return;
				tblITMDT.setRowSelectionInterval(0,0);
				tblITMDT.setColumnSelectionInterval(0,0);
				M_strSQLQRY = "SELECT AV_GRPCD,CT_MATDS FROM MM_AVMST,CO_CTMST WHERE "+
							  "  (isnull(AV_STSFL,'') <>'X' and isnull(CT_STSFL,'') <>'X') AND AV_GRPCD = CT_GRPCD and CT_CODTP ='MG' AND AV_PRTCD ='"+txtPRTCD.getText().trim() +"'"; 
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("AV_GRPCD"),"");
						tblITMDT.setValueAt(L_strTEMP,L_intRECCT,TBL_GRPCD);
						tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intRECCT,TBL_GRPDS);
						L_intRECCT++;
						vtrGRPCD.addElement(L_strTEMP);
					}
					M_rstRSSET.close();
				}
				if(L_intRECCT==0)
				{
					setMSG("No Data found ..",'E');
				}
				else
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						tblITMDT.setColumnSelectionInterval(0,0);
						tblITMDT.setRowSelectionInterval(0,0);
						tblITMDT.editCellAt(L_intRECCT,1);
						tblITMDT.cmpEDITR[1].requestFocus();
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"actionP");
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPRTCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPRTCD";
				String L_ARRHDR[] = {"Code","Description"};
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from co_ptmst where PT_PRTTP ='S' AND pt_stsfl <>'X'";
				if(txtPRTCD.getText().length() >0)
					M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText().trim()+"%'";
				M_strSQLQRY += " order by PT_PRTCD ";
				cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
			}
			else if(M_objSOURC == tblITMDT.cmpEDITR[1])
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtGRPCD";
				String L_ARRHDR[] = {"Code","Description"};
				M_strSQLQRY = "Select CT_GRPCD,CT_MATDS from co_ctmst where CT_CODTP ='MG'";
				if(vtrGRPCD.size() >0)
				{
					M_strSQLQRY += "AND CT_GRPCD NOT IN(";
					for(int i=0;i<vtrGRPCD.size();i++)
					{
						if(i==0)
							M_strSQLQRY+="'"+vtrGRPCD.elementAt(i).toString()+"'";
						else
							M_strSQLQRY+=",'"+vtrGRPCD.elementAt(i).toString()+"'";
					}
					M_strSQLQRY +=")";
				}
				M_strSQLQRY += " order by CT_GRPCD ";
				cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtPRTCD"))
		{
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD.equals("txtGRPCD"))
		{
			txtGRPCD.setText(cl_dat.M_strHLPSTR_pbst);
			//tblITMDT.setValueAt(cl_dat.M_strHLPSTR_pbst,tblITMDT.getSelectedRow(),TBL_GRPCD);
			tblITMDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblITMDT.getSelectedRow(),TBL_GRPDS);
		}
	}
	boolean vldDATA()
	{
		if(txtPRTCD.getText().trim().length() ==0)
		{
			setMSG("Party Code can not be blank..",'E');
			return false;
		}
		int L_intSELCT =0;
		if(tblITMDT.isEditing())
			tblITMDT.getCellEditor().stopCellEditing();
		tblITMDT.setRowSelectionInterval(0,0);
		tblITMDT.setColumnSelectionInterval(0,0);
		for(int i=0;i<tblITMDT.getRowCount();i++)
		{
			if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_intSELCT++;
				if(tblITMDT.getValueAt(i,TBL_GRPCD).toString().length() ==0)
				{
					setMSG("Group Code can not be blank at row.."+i+1,'E');
					return false;
				}
				if(vtrGRPCD.contains((String)tblITMDT.getValueAt(i,TBL_GRPCD).toString()))
				{
					setMSG("Group Code,"+tblITMDT.getValueAt(i,TBL_GRPCD).toString()+" is already approved..",'E');
					return false;
				}
			}
		}
		if(L_intSELCT ==0)
		{
			setMSG("No item is selected,Select at least one item..",'E');
			return false;
		}
		// party code and group code validation
		return true;
	}
	void exeSAVE()
	{
		try
		{
			if(!vldDATA())
				return ;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				for(int i=0;i<tblITMDT.getRowCount();i++)
				{
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY ="INSERT INTO MM_AVMST(AV_PRTTP,AV_PRTCD,AV_GRPCD,AV_TRNFL,AV_STSFL,AV_LUSBY,AV_LUPDT)VALUES("
							        +"'S','"+txtPRTCD.getText().trim()+"','"
									+tblITMDT.getValueAt(i,TBL_GRPCD).toString()+"',"
							        +getUSGDTL("AV",'I',"")+")";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				for(int i=0;i<tblITMDT.getRowCount();i++)
				{
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY ="UPDATE MM_AVMST SET "
							         +getUSGDTL("AV",'U',"X")
							         + " WHERE AV_PRTTP ='S' and AV_PRTCD ='"+txtPRTCD.getText().trim()+"'"
									 +	" AND AV_GRPCD ='"+tblITMDT.getValueAt(i,TBL_GRPCD).toString()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Saved Successfully..",'N'); 
				if(vtrGRPCD!=null)
					vtrGRPCD.removeAllElements();
				clrCOMP();
			}
			else
			{
				setMSG("Error in saving details..",'N'); 
			}
		}
		catch(Exception L_E)
		{
			setMSG("Error in saving ..",'E');
		}
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input == txtPRTCD)
					if(vldPRTCD(txtPRTCD.getText().trim()))
					   return true;
			}
			catch(Exception L_E)
			{
				setMSG("Error in verify ..",'E');		
			}
			setMSG("Invalid Party code..",'E');
			return false;
		}
	}
	class TBLINPVF extends TableInputVerifier 
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			String strTEMP ="";
			try
			{
				if(P_intCOLID==TBL_GRPCD)
				{
					strTEMP = tblITMDT.getValueAt(P_intROWID,TBL_GRPCD).toString();
					if(strTEMP.length() == 0)
						return true;
					if(vtrGRPCD.contains((String)strTEMP))
				    {
						setMSG("Duplicate Group Code..",'E');
						return false;
					}
					if(!vldGRPCD(strTEMP,P_intROWID))
					{
						setMSG("Invalid Group Code,Press F1 to select From the list..",'E');
						return false;
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG("Error in verify ..",'E');		
				return false;
			}
			return true;
		}
	}
	boolean vldPRTCD(String P_strPRTCD)
	{
		try
		{
			java.sql.Date L_datTEMP;
			M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM,PT_APRDT,PT_EVLDT from CO_PTMST WHERE"
				          +" isnull(PT_STSFL,'') <>'X' AND PT_PRTTP ='S' AND PT_PRTCD ='"+P_strPRTCD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
					L_datTEMP = M_rstRSSET.getDate("PT_APRDT");
					if(L_datTEMP !=null)
					{
						txtAPRDT.setText(M_fmtLCDAT.format(L_datTEMP));
					}
					L_datTEMP = M_rstRSSET.getDate("PT_EVLDT");
					if(L_datTEMP !=null)
					{
						txtEVLDT.setText(M_fmtLCDAT.format(L_datTEMP));
					}
					return true;
				}
				else
				{
					setMSG("Invalid Party Code, Press F1 for help..",'E');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG("Error in vldPRTCD..",'E');
		}
		return false;
	}
	boolean vldGRPCD(String P_strGRPCD,int P_intROWID)
	{
		try
		{
			System.out.println("inside vldGRPCD");
			M_strSQLQRY = "SELECT CT_MATDS from CO_CTMST WHERE"
				          +" isnull(CT_STSFL,'') <>'X' AND CT_CODTP ='MG'" ;
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					System.out.println(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
					tblITMDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),P_intROWID,TBL_GRPDS);
					return true;
				}
				else
				{
					setMSG("Invalid Group Code, Press F1 for help..",'E');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG("Error in vldGRPCD..",'E');
		}
		return false;
	}
}