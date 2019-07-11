import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.JTable;
import java.util.StringTokenizer;
import java.util.Hashtable;
public class mm_teenq extends cl_pbase
{
	private JTextField txtENQNO,txtENQDT,txtINDNO,txtMATCD,txtVENCD,txtRMKDS,txtPOUOM,txtENQQT;
	private cl_JTable tblENQDT,tblVENDT;
	private JButton btnPROVN;
	private PreparedStatement pstmINSEQ,pstmINSRM;
	private String strSRLNO,strTEMP;
	private StringTokenizer L_STRTKN;
	private Hashtable hstINDNO = new Hashtable();
	private Hashtable hstMATCD = new Hashtable();
	private Hashtable hstVENCD = new Hashtable();
	private int intINDCT =0;
	private int intVENCT =0;
	private final String strTRNTP_fn ="EQ";	
	private final String strENQTR_fn ="04";

	private final int TBL_CHKFL =0;
	private final int TBL_INDNO =1;
	private final int TBL_MATCD =2;
	private final int TBL_UOMCD =3;
	private final int TBL_MATDS =4;
	private final int TBL_ENQQT =5;
	
	private final int TBL_VENCD =1;
	private final int TBL_VENNM =2;
	
	
	mm_teenq()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Enquiry No."),1,1,1,1,this,'L');
			add(txtENQNO=new TxtLimit(8),1,2,1,1,this,'L');
			add(new JLabel("Enquiry Date"),1,3,1,1,this,'L');
			add(txtENQDT=new TxtDate(),1,4,1,1,this,'L');
			JLabel lblENQDT = new JLabel("Enquiry Details");
		//	lblENQDT.setFont(Color.blue);
			add(lblENQDT,2,1,1,1,this,'L');
			add(new JLabel("Indent No."),2,5,1,1,this,'L');
			add(txtINDNO=new TxtLimit(8),2,6,1,1,this,'L');
			
			add(new JLabel("Item Code"),2,7,1,1,this,'L');
			add(txtMATCD=new TxtLimit(10),2,8,1,1,this,'L');
			tblENQDT=crtTBLPNL1(this,new String[]{"FL","Indent No.","Item Code","UOM","Description","QTY."},150,3,1,7,8,new int[]{20,80,100,50,400,100},new int[]{0});
			JLabel lblVENDT = new JLabel("Vendor Details");
		//	lblVENDT.setColour(Color.blue);
			add(lblVENDT,11,1,1,1,this,'L');
			add(btnPROVN = new JButton("Get Probable Vendors"),11,5,1,2,this,'L');
			add(new JLabel("Vendor Code"),11,7,1,1,this,'L');
			add(txtVENCD=new TxtLimit(5),11,8,1,1,this,'L');
			tblVENDT=crtTBLPNL1(this,new String[]{"FL","Vendor Code","Description"},150,12,1,5,7,new int[]{20,80,400},new int[]{0});	
			add(new JLabel("Remarks"),20,1,1,1,this,'L');
			add(txtRMKDS=new TxtLimit(5),20,2,1,6,this,'L');	
	
			pstmINSEQ = cl_dat.M_conSPDBA_pbst.prepareStatement(
			"INSERT INTO MM_IQMST(IQ_MMSBS,IQ_STRTP,IQ_ENQNO,IQ_ENQDT,IQ_INDNO,IQ_MATCD,IQ_UOMCD,IQ_VENCD,IQ_ENQQT"+
				",IQ_TRNFL,IQ_STSFL,IQ_LUSBY,IQ_LUPDT)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmINSRM =cl_dat.M_conSPDBA_pbst.prepareStatement(
			"INSERT INTO MM_RMMST(RM_MMSBS,RM_STRTP,RM_DOCTP,RM_TRNTP,RM_REMTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES("+
			"?,?,?,?,?,?,?,?,?,?,?)");
		
			txtPOUOM = new TxtLimit(3);
			txtENQQT = new TxtNumLimit(12.3);
			txtPOUOM.addFocusListener(this);
			txtPOUOM.addKeyListener(this);
			txtENQQT.addFocusListener(this);
			txtENQQT.addKeyListener(this);
		
			tblENQDT.setCellEditor(TBL_UOMCD,txtPOUOM);
			tblENQDT.setCellEditor(TBL_ENQQT,txtENQQT);
		
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}

	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);	
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
             if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			 {
				 txtINDNO.requestFocus();
			 }
			 else
			 	 txtENQNO.requestFocus();
			 
        }
		else if(M_objSOURC == btnPROVN)
		{
             getPROVN();
        }
		else if(M_objSOURC == txtINDNO)
		{
			if(txtINDNO.getText().trim().length() >0)
				getINDDT();
			else
				txtMATCD.requestFocus();
	    }
		else if(M_objSOURC == txtMATCD)
		{
			if(txtMATCD.getText().trim().length() >0)
				getMATDT();
			else
				txtVENCD.requestFocus();
        }
		else if(M_objSOURC == txtENQNO)
		{
             getDATA();
        }
		if(M_objSOURC == txtVENCD)
		{
             getVENDT();
        }
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int[] L_inaCOLSZ = new int[]{100,400};;
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtMATCD)
			{
				if(txtMATCD.getText().trim().length() ==0)
				{
					setMSG("Please enter the Group code(First two digits of item code)..",'N'); 
					return;
				}
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and ifnull(CT_STSFL,'') <>'X'";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY += " Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtENQNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtENQNO";
				String L_ARRHDR[] = {"ENQ. No.","Enq Date"};
				M_strSQLQRY = "Select distinct IQ_ENQNO,IQ_ENQDT from MM_IQMST ";
				M_strSQLQRY += " where IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'" +" and ifnull(IQ_STSFL,'') <>'X'";
				if(txtENQNO.getText().trim().length() >0)
					M_strSQLQRY += " and IQ_ENQNO like '" + txtENQNO.getText().trim() + "%'";
				M_strSQLQRY += " Order by IQ_ENQNO desc ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtINDNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtINDNO";
				String L_ARRHDR[] = {"Indent No.","Indent Date"};
				M_strSQLQRY = "Select distinct IN_INDNO,IN_INDDT from MM_INMST ";
				M_strSQLQRY += " where IN_STRTP ='"+M_strSBSCD.substring(2,4)+"'" +" and ifnull(IN_STSFL,'') ='4'";
				M_strSQLQRY += " AND IFNULL(IN_AUTQT,0) -IFNULL(IN_ORDQT,0)- IFNULL(IN_FCCQT,0) >0";
				if(txtINDNO.getText().trim().length() >0)
					M_strSQLQRY += " and IN_INDNO like '" + txtINDNO.getText().trim() + "%'";
				M_strSQLQRY += " Order by IN_INDNO desc ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtVENCD)
			{
				int[] L_inaCOLVN = new int[]{100,400};
				if(txtVENCD.getText().trim().length() ==0)
				{
					setMSG("Please enter the First digits of Vendor Code..",'N'); 
					return;
				}
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtVENCD";
				String L_ARRHDR[] = {"Supplier Code","Name"};
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
				M_strSQLQRY += " where PT_PRTTP ='S' and ifnull(PT_STSFL,'') <>'X'";
				if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " and PT_PRTCD like '" + txtVENCD.getText().trim().toUpperCase() + "%'";
				M_strSQLQRY += " Order by pt_prtcd ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLVN);
			}
			else if(M_objSOURC == tblENQDT.cmpEDITR[TBL_UOMCD])
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPOUOM";
				String L_ARRHDR[] = {"UOM Code","Description"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP ='MST' and CMT_CGSTP ='COXXUOM' and ifnull(CMT_STSFL,'') <>'X'";
				if(txtPOUOM.getText().trim().length() >0)
					M_strSQLQRY += " and CMT_CODCD like '" + txtPOUOM.getText().trim() + "%'";
				M_strSQLQRY += " Order by cmt_codcd ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
			if((M_strHLPFLD.equals("txtVENCD"))||(M_strHLPFLD.equals("txtVENCDF2")))
			{
				txtVENCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtMATCD"))
			{
				txtMATCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtINDNO"))
			{
				txtINDNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtENQNO"))
			{
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtENQNO.setText(L_STRTKN.nextToken());
				txtENQDT.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtPOUOM"))
			{
				txtPOUOM.setText(cl_dat.M_strHLPSTR_pbst);
			}
		
	}
	void setENBL(boolean P_flgSTAT)
	{
		super.setEnabled(P_flgSTAT);
		txtENQDT.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			txtENQNO.setEnabled(false);
			txtINDNO.setEnabled(true);
			txtMATCD.setEnabled(true);
			txtVENCD.setEnabled(true);
			btnPROVN.setEnabled(true);
		}
		else
		{
			txtENQNO.setEnabled(true);
			txtINDNO.setEnabled(false);
			txtMATCD.setEnabled(false);
			txtVENCD.setEnabled(false);
			btnPROVN.setEnabled(false);
		}
		tblVENDT.setEnabled(false);
		tblENQDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblENQDT.cmpEDITR[TBL_MATDS].setEnabled(false);
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
			tblVENDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
	    {
			tblENQDT.cmpEDITR[TBL_UOMCD].setEnabled(true);
			tblENQDT.cmpEDITR[TBL_ENQQT].setEnabled(true);
		
		}
	}
	boolean vldDATA()
	{
		int L_intRECCT =0,L_intVENCT =0;
		/*if(txtENQDT.getText().trim().length() ==0)
		{
			setMSG("Enquiry Date can not be blank..",'E');
		}*/
		if(tblENQDT.isEditing())
			tblENQDT.getCellEditor().stopCellEditing();
		tblENQDT.setRowSelectionInterval(0,0);
		tblENQDT.setColumnSelectionInterval(0,0);
		for(int i=0;i<tblVENDT.getRowCount();i++)
		{
			if(tblVENDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_intVENCT++;
			}
		}
		if((L_intVENCT ==0)&&(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
		{
			setMSG("Please Select at least one Vendor",'E');
			return false;
		}
		for(int i=0;i<tblENQDT.getRowCount();i++)
		{
			if(tblENQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				if(tblENQDT.getValueAt(i,TBL_UOMCD).toString().equals(""))
				{
					setMSG("Please Enter UOM Code..",'E');
					tblENQDT.cmpEDITR[TBL_UOMCD].requestFocus();
					return false;
				}
				if(tblENQDT.getValueAt(i,TBL_ENQQT).toString().equals(""))
				{
					setMSG("Please Enter Quantity..",'E');
					tblENQDT.cmpEDITR[TBL_ENQQT].requestFocus();
					return false;
				}
				else if(Double.parseDouble(tblENQDT.getValueAt(i,TBL_ENQQT).toString())==0)
				{
					setMSG("Please Enter Quantity..",'E');
					tblENQDT.cmpEDITR[TBL_ENQQT].requestFocus();
					return false;
				}
				L_intRECCT++;
			}
		}
		if((L_intRECCT ==0)&&(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
		{
			setMSG("Please Select at least one Item..",'E');
			return false;
		}
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			if((L_intRECCT >0)&&(L_intVENCT >0))
		    {
				setMSG("Select either the Item code or Vendor ..",'E');
				return false;
			}
		}
			return true;
	}
	boolean getDATA()
	{
		try
		{
			int L_intRECCT =0;
			int L_intVENCT =0;
			String L_strVENCD="",L_strPRVEN="";
			M_strSQLQRY = "SELECT distinct IQ_MMSBS,IQ_STRTP,IQ_ENQNO,IQ_ENQDT,IQ_INDNO,IQ_MATCD,IQ_UOMCD,IQ_ENQQT"+
						  ",IQ_TRNFL,IQ_STSFL,IQ_LUSBY,IQ_LUPDT,CT_MATDS from MM_IQMST,CO_CTMST "+
						  " WHERE IQ_MATCD = CT_MATCD and CT_CODTP ='CD' AND IFNULL(IQ_STSFL,'') <>'X'"+
						 // " AND PT_PRTTP ='S' and PT_PRTCD = IQ_VENCD AND IFNULL(PT_STSFL,'') <>'X'"+
						  " AND IQ_MMSBS ='"+M_strSBSCD+"' AND IQ_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
						  " AND IQ_ENQNO ='"+txtENQNO.getText().trim() +"' ORDER BY IQ_ENQNO,IQ_MATCD";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
					tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_INDNO"),""),L_intRECCT,TBL_INDNO);
					tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_MATCD"),""),L_intRECCT,TBL_MATCD);
					tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intRECCT,TBL_MATDS);
					tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_UOMCD"),""),L_intRECCT,TBL_UOMCD);
					tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_ENQQT"),""),L_intRECCT,TBL_ENQQT);
				
					L_intRECCT++;
				}
			if(L_intRECCT ==0)
			{
				setMSG("Invalid Enquiry No ..",'E');
				return false;
			}
			else
			{
				M_strSQLQRY = "SELECT distinct IQ_MMSBS,IQ_STRTP,IQ_ENQNO,IQ_VENCD,PT_PRTNM"+
						  " from MM_IQMST,CO_PTMST "+
						  " WHERE PT_PRTTP ='S' and PT_PRTCD = IQ_VENCD AND IFNULL(IQ_STSFL,'') <>'X' AND IFNULL(PT_STSFL,'') <>'X'"+
						  " AND IQ_MMSBS ='"+M_strSBSCD+"' AND IQ_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
						  " AND IQ_ENQNO ='"+txtENQNO.getText().trim() +"' ORDER BY IQ_VENCD";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
					tblVENDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_VENCD"),""),L_intVENCT,TBL_VENCD);
					tblVENDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),L_intVENCT,TBL_VENNM);
					L_intVENCT++ ;
				}
			}
				
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
			return false;
		}
		return true;
	}
	void exeSAVE()
	{
		try
		{
			
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			if(tblENQDT.isEditing())
				tblENQDT.getCellEditor().stopCellEditing();
			if(tblVENDT.isEditing())
				tblVENDT.getCellEditor().stopCellEditing();
			cl_dat.M_flgLCUPD_pbst = true;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Saving of data in progress..",'N');
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtENQNO.setText(cl_dat.M_strLOGDT_pbst);
				if(!genENQNO())
				{
					setCursor(cl_dat.M_curDFSTS_pbst);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
				strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
			//	for(int i=0;i<tblENQDT.getRowCount();i++)
				for(int i=0;i<intINDCT;i++)
				{
					if(tblENQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						for(int j=0;j<intVENCT;j++)
						{
							if(tblVENDT.getValueAt(j,TBL_CHKFL).toString().equals("true"))
							{
								
								pstmINSEQ.setString(1,M_strSBSCD);
								pstmINSEQ.setString(2,M_strSBSCD.substring(2,4));
								pstmINSEQ.setString(3,txtENQNO.getText().trim());
								pstmINSEQ.setDate(4,Date.valueOf(strTEMP));
							    pstmINSEQ.setString(5,tblENQDT.getValueAt(i,TBL_INDNO).toString());
								pstmINSEQ.setString(6,tblENQDT.getValueAt(i,TBL_MATCD).toString());
								pstmINSEQ.setString(7,tblENQDT.getValueAt(i,TBL_UOMCD).toString());
								pstmINSEQ.setString(8,tblVENDT.getValueAt(j,TBL_VENCD).toString());
								pstmINSEQ.setFloat(9,Float.valueOf(tblENQDT.getValueAt(i,TBL_ENQQT).toString()).floatValue());
								pstmINSEQ.setString(10,"0");
								pstmINSEQ.setString(11,"0"); // FRESH ORDER ,ORDERED
								pstmINSEQ.setString(12,cl_dat.M_strUSRCD_pbst);
								pstmINSEQ.setDate(13,Date.valueOf(strTEMP));
								pstmINSEQ.executeUpdate();
							}
						}
					}
				}
			M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CCSVL = '"+strSRLNO +"',CMT_CHP01 =''";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MMXXENQ'";
			M_strSQLQRY += " AND CMT_CODCD = '" +cl_dat.M_strFNNYR_pbst.substring(3)+ strENQTR_fn+ "'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
			else 
				updENQDT();
			if(cl_dat.M_flgLCUPD_pbst)
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Data saved successfully..",'N');
				JOptionPane.showMessageDialog(this,"Please, Note down the Enquiry No. " + txtENQNO.getText().trim(),"Enquiry No.",JOptionPane.INFORMATION_MESSAGE);
				clrCOMP();
				if(hstINDNO !=null)
					hstINDNO.clear();
				if(hstMATCD !=null)
					hstMATCD.clear();
				if(hstVENCD !=null)
					hstVENCD.clear();
				intINDCT =0;
				intVENCT =0;
			//	setCursor(cl_dat.curDFSTS_pbst);
			}
			else
			{
				setMSG("Error in Saving data..",'E');
				
			}
		}
		catch(Exception L_E)
		{
		}
		finally
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			
		}
	}
	private boolean genENQNO()
	{
		try
		{
		
			String L_strENQNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01 ="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MMXXENQ' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) +  strENQTR_fn + "'";
		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					M_rstRSSET.close();
				}
			}
			if(L_CHP01.trim().length() ==3)
			{
				setMSG("In use,try after some time..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
			
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_strENQNO += "0";
			
			L_CCSVL = L_strENQNO + L_CCSVL;
			strSRLNO = L_CCSVL;
			L_strENQNO = L_CODCD + L_CCSVL;
			txtENQNO.setText(L_strENQNO);
			return true;
		}catch(Exception L_EX){
			setMSG(L_EX,"genENQNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
	}
boolean insRMMST(String P_strREMTP,String P_strREMDS)
{
	try
	{
		strTEMP = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
		pstmINSRM.setString(1,M_strSBSCD);
		pstmINSRM.setString(2,M_strSBSCD.substring(2,4));
		pstmINSRM.setString(3,strTRNTP_fn);
		pstmINSRM.setString(4,strTRNTP_fn);
		pstmINSRM.setString(5,P_strREMTP);
		pstmINSRM.setString(6,txtENQNO.getText().trim());
		pstmINSRM.setString(7,P_strREMDS);
		pstmINSRM.setString(8," ");
		pstmINSRM.setString(9,"0");
		pstmINSRM.setString(10,cl_dat.M_strUSRCD_pbst);
		pstmINSRM.setDate(11,Date.valueOf(strTEMP));
		pstmINSRM.executeUpdate();
		pstmINSRM.clearParameters();
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"insRMMST");
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
	return true;
}
boolean updENQDT()
{
	boolean L_flgENQFL =false;
	boolean L_flgVENFL =false;
	boolean L_flgDELFL =false;
	try
	{
		int i=0;
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			L_flgDELFL = true;
		for(i=0;i<tblENQDT.getRowCount();i++)
		{
			if(tblENQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_flgENQFL = true;
			}
		}
		i=0;
		for(i=0;i<tblVENDT.getRowCount();i++)
		{
			if(tblVENDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_flgVENFL = true;
			}
		}
		i=0;
		if((L_flgENQFL) &&(!L_flgVENFL))
		{
			// Selected enq (Ind. No /Mat cd) is to be removed for all the vendors
			for(i=0;i<tblENQDT.getRowCount();i++)
			{
				if(tblENQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY = "UPDATE MM_IQMST SET ";
					if(L_flgDELFL)
						M_strSQLQRY += getUSGDTL("EQ",'U',"X");
					else
					{
						M_strSQLQRY += getUSGDTL("EQ",'U',null)+",";
						M_strSQLQRY += " IQ_ENQQT ="+ tblENQDT.getValueAt(i,TBL_ENQQT).toString() ;
					}
					M_strSQLQRY += " WHERE IQ_MMSBS ='"+M_strSBSCD+"' and IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND IQ_ENQNO ='"+txtENQNO.getText().trim() +"'";
					M_strSQLQRY += " AND IQ_INDNO ='"+ tblENQDT.getValueAt(i,TBL_INDNO).toString() +"'";
					M_strSQLQRY += " AND IQ_MATCD ='"+ tblENQDT.getValueAt(i,TBL_MATCD).toString() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
		}
		i=0;
		if((!L_flgENQFL) &&(L_flgVENFL))
		{
			// Selected Vendors is to be removed for all the Indent /Matcd
			for(i=0;i<tblVENDT.getRowCount();i++)
			{
				if(tblVENDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY = "UPDATE MM_IQMST SET ";
					if(L_flgDELFL)
						M_strSQLQRY += getUSGDTL("EQ",'U',"X");
					else
					{
						M_strSQLQRY += getUSGDTL("EQ",'U',null)+",";
						M_strSQLQRY += " AND IQ_ENQQT ='"+ tblENQDT.getValueAt(i,TBL_ENQQT).toString() +"'";
					}
					M_strSQLQRY += " WHERE IQ_MMSBS ='"+M_strSBSCD+"' and IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND IQ_ENQNO ='"+txtENQNO.getText().trim() +"'";
					M_strSQLQRY += " AND IQ_VENCD ='"+ tblVENDT.getValueAt(i,TBL_VENCD).toString() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
		}
	/*	if((L_flgENQFL) &&(L_flgVENFL))
		{
			// Selected Vendors is to be removed for all the Indent /Matcd
			for(i=0;i<tblENQDT.getRowCount();i++)
			{
				if(tblENQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					for(int j=0;j<tblVENDT.getRowCount();j++)
					{
						if(tblVENDT.getValueAt(j,TBL_CHKFL).toString().equals("true"))
						{
							M_strSQLQRY = "UPDATE MM_IQMST SET ";
							if(L_flgDELFL)
								M_strSQLQRY += getUSGDTL("EQ",'U',"X");
							else
							{
								M_strSQLQRY += getUSGDTL("EQ",'U',null)+",";
								M_strSQLQRY += " AND IQ_ENQQT ='"+ tblENQDT.getValueAt(i,TBL_ENQQT).toString() +"'";
							}
							M_strSQLQRY += " WHERE IQ_MMSBS ='"+M_strSBSCD+"' and IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
							M_strSQLQRY += " AND IQ_ENQNO ='"+txtENQNO.getText().trim() +"'";
							M_strSQLQRY += " AND IQ_INDNO ='"+ tblENQDT.getValueAt(i,TBL_INDNO).toString() +"'";
							M_strSQLQRY += " AND IQ_MATCD ='"+ tblENQDT.getValueAt(i,TBL_MATCD).toString() +"'";
							M_strSQLQRY += " AND IQ_VENCD ='"+ tblVENDT.getValueAt(j,TBL_VENCD).toString() +"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
				}
			}
		}*/
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"");
		return false;
	}
	return true;
}
private boolean getINDDT()
{
   try
   {
        String L_strINDNO,L_strMATCD,L_strPINQT;
        M_strSQLQRY ="";
		int L_intRECCT =0;
		if(tblENQDT.isEditing())
			tblENQDT.getCellEditor().stopCellEditing();
		tblENQDT.setRowSelectionInterval(0,0);
		tblENQDT.setColumnSelectionInterval(0,0);
        M_strSQLQRY = "Select IN_INDNO,IN_MATCD,CT_MATDS,CT_UOMCD,IFNULL(IN_AUTQT,0)-IFNULL(IN_ORDQT,0) L_PQTY from MM_INMST,CO_CTMST WHERE IN_MATCD = CT_MATCD AND IFNULL(IN_STSFL,'') <>'X'";
        M_strSQLQRY +=" AND (IFNULL(IN_AUTQT,0)-IFNULL(IN_ORDQT,0)) >0";
        M_strSQLQRY +=" and IN_MMSBS ='"+M_strSBSCD+"' AND IN_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
        M_strSQLQRY +=" and IN_INDNO ='"+txtINDNO.getText().trim()+"'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		setCursor(cl_dat.M_curWTSTS_pbst);
		if(hstINDNO.get(txtINDNO.getText().trim())== null)
		{
			hstINDNO.put(txtINDNO.getText().trim(),"");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
			   L_intRECCT++;
			   tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),intINDCT,TBL_INDNO);
			   tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),""),intINDCT,TBL_MATCD);
			   tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),intINDCT,TBL_MATDS);
			   tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),intINDCT,TBL_UOMCD);
			   L_strPINQT = nvlSTRVL(M_rstRSSET.getString("L_PQTY"),"");
			   tblENQDT.setValueAt(L_strPINQT,intINDCT,TBL_ENQQT);
			   intINDCT++;
			}
			if(L_intRECCT ==0)
			setMSG("Invalid Indent No. Or not pending for P.O...",'E');
		else
			setMSG("",'N');
      
		}
		else
		{
			setMSG("Given Indent no. is already taken ..",'E');  
			txtINDNO.requestFocus();
		}
		  txtINDNO.setText("");
    }
    catch(Exception L_E)
    {
        setMSG(L_E,"grtINDDT");
		return false;
    }
    finally
	{
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	return true;
}
private void getMATDT()
{
   try
   {
        M_strSQLQRY ="";
		int L_intRECCT =0;
        M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST WHERE ifnull(CT_STSFL,'') <>'X'";
        M_strSQLQRY +=" and CT_MATCD ='"+txtMATCD.getText().trim()+"'";
		if(tblENQDT.isEditing())
			tblENQDT.getCellEditor().stopCellEditing();
		tblENQDT.setRowSelectionInterval(0,0);
		tblENQDT.setColumnSelectionInterval(0,0);
		if(hstMATCD.get(txtMATCD.getText().trim())== null)
		{
			hstMATCD.put(txtMATCD.getText().trim(),"");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
			   L_intRECCT++;
			   tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),intINDCT,TBL_MATCD);
			   tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),intINDCT,TBL_MATDS);
			   tblENQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),intINDCT,TBL_UOMCD);
			   intINDCT++;
			}
			if(L_intRECCT ==0)
			setMSG("Invalid Item Code..",'E');
		else
			setMSG("",'N');
      
		}
		else
		{
			setMSG("Given Material Code is already taken ..",'E');  
		}
		  txtMATCD.setText("");
    }
    catch(Exception L_E)
    {
        setMSG(L_E,"grtINDDT");
    }
}
private void getVENDT()
{
   try
   {
        M_strSQLQRY ="";
		int L_intRECCT =0;
	    M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST WHERE ifnull(PT_STSFL,'') <>'X'";
        M_strSQLQRY +=" and PT_PRTTP ='S' and PT_PRTCD ='"+txtVENCD.getText().trim()+"'" ;
		if(hstVENCD.get(txtVENCD.getText().trim())== null)
		{
			hstVENCD.put(txtVENCD.getText().trim(),"");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
			   L_intRECCT++;
			   tblVENDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),""),intVENCT,TBL_VENCD);
			   tblVENDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),intVENCT,TBL_VENNM);
			   intVENCT++;
			}
			if(L_intRECCT ==0)
			setMSG("Data not Found for .."+txtVENCD.getText().trim(),'E');
		else
			setMSG("",'N');
      
		}
		else
		{
			setMSG("Given Vendor is already taken ..",'E');  
		}
		  txtVENCD.setText("");
    }
    catch(Exception L_E)
    {
        setMSG(L_E,"grtINDDT");
    }
}
private void getPROVN()
{
   try
   {
        M_strSQLQRY ="";
		int L_intRECCT =0;
        M_strSQLQRY = "Select distinct CTP_PRTCD,PT_PRTNM from CO_CTPTR,CO_PTMST WHERE ifnull(CTP_STSFL,'') <>'X'";
		M_strSQLQRY +=" and CTP_PRTTP = PT_PRTTP AND CTP_PRTCD = PT_PRTCD AND CTP_PRTTP ='S' and CTP_MATCD IN(";
		int intFIRST =0;
		for(int i=0;i<tblENQDT.getRowCount();i++)
		{
			if(tblENQDT.getValueAt(i,TBL_CHKFL).equals(Boolean.TRUE))
			{
				intFIRST++;
				if(intFIRST == 1)
				{
					M_strSQLQRY += "'"+tblENQDT.getValueAt(i,TBL_MATCD).toString()+"'";
				}
				else
					M_strSQLQRY += ",'"+tblENQDT.getValueAt(i,TBL_MATCD).toString()+"'";
			}
		}
		M_strSQLQRY +=")";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);		
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
		   strTEMP = nvlSTRVL(M_rstRSSET.getString("CTP_PRTCD"),"");
		   L_intRECCT++;
		   hstVENCD.put(strTEMP,"");
		   tblVENDT.setValueAt(strTEMP,intVENCT,TBL_VENCD);
		   tblVENDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),intVENCT,TBL_VENNM);
		   intVENCT++;
		}
		if(L_intRECCT ==0)
		setMSG("Probable Vendor Data not Found ..",'E');
		
	}
    catch(Exception L_E)
    {
        setMSG(L_E,"grtINDDT");
    }
}

}
