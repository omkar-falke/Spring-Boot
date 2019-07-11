import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.JTable;
import java.util.StringTokenizer;
import java.util.Hashtable;
public class mm_teinq extends cl_pbase
{
	private JTextField txtINQNO,txtINQDT,txtINDNO,txtMATCD,txtVENCD,txtRMKDS,txtPOUOM,txtINQQT;
	private cl_JTable tblINQDT,tblVENDT;
	private JButton btnPROVN;
	private PreparedStatement pstmINSEQ,pstmINSRM;
	private String strSRLNO,strTEMP;
	private StringTokenizer L_STRTKN;
	private Hashtable<String,String> hstINDNO = new Hashtable<String,String>();
	private Hashtable<String,String> hstMATCD = new Hashtable<String,String>();
	private Hashtable<String,String> hstVENCD = new Hashtable<String,String>();
	private int intINDCT =0;
	private int intVENCT =0;
	private final String strTRNTP_fn ="IQ";	
	private final String strINQTR_fn ="04";

	private final int TBL_CHKFL =0;
	private final int TBL_INDNO =1;
	private final int TBL_MATCD =2;
	private final int TBL_UOMCD =3;
	private final int TBL_MATDS =4;
	private final int TBL_INQQT =5;
	
	private final int TBL_VENCD =1;
	private final int TBL_VENNM =2;
	
	
	mm_teinq()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Inquiry No."),1,1,1,1,this,'L');
			add(txtINQNO=new TxtLimit(8),1,2,1,1,this,'L');
			add(new JLabel("Inquiry Date"),1,3,1,1,this,'L');
			add(txtINQDT=new TxtDate(),1,4,1,1,this,'L');
			JLabel lblINQDT = new JLabel("Inquiry Details");
		//	lblINQDT.setFont(Color.blue);
			add(lblINQDT,2,1,1,1,this,'L');
			add(new JLabel("Indent No."),2,5,1,1,this,'L');
			add(txtINDNO=new TxtLimit(8),2,6,1,1,this,'L');
			
			add(new JLabel("Item Code"),2,7,1,1,this,'L');
			add(txtMATCD=new TxtLimit(10),2,8,1,1,this,'L');
			tblINQDT=crtTBLPNL1(this,new String[]{"FL","Indent No.","Item Code","UOM","Description","QTY."},150,3,1,7,8,new int[]{20,80,100,50,400,100},new int[]{0});
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
			"INSERT INTO MM_IQMST(IQ_MMSBS,IQ_STRTP,IQ_INQNO,IQ_INQDT,IQ_INDNO,IQ_MATCD,IQ_UOMCD,IQ_VENCD,IQ_INQQT"+
				",IQ_TRNFL,IQ_STSFL,IQ_LUSBY,IQ_LUPDT)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmINSRM =cl_dat.M_conSPDBA_pbst.prepareStatement(
			"INSERT INTO MM_RMMST(RM_MMSBS,RM_STRTP,RM_DOCTP,RM_TRNTP,RM_REMTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES("+
			"?,?,?,?,?,?,?,?,?,?,?)");
		
			txtPOUOM = new TxtLimit(3);
			txtINQQT = new TxtNumLimit(12.3);
			txtPOUOM.addFocusListener(this);
			txtPOUOM.addKeyListener(this);
			txtINQQT.addFocusListener(this);
			txtINQQT.addKeyListener(this);
		
			tblINQDT.setCellEditor(TBL_UOMCD,txtPOUOM);
			tblINQDT.setCellEditor(TBL_INQQT,txtINQQT);
		
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
			 	 txtINQNO.requestFocus();
			 
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
		else if(M_objSOURC == txtINQNO)
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
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY += " Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtINQNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtINQNO";
				String L_ARRHDR[] = {"INQ. No.","INQ Date"};
				M_strSQLQRY = "Select distinct IQ_INQNO,IQ_INQDT from MM_IQMST ";
				M_strSQLQRY += " where IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'" +" and isnull(IQ_STSFL,'') <>'X'";
				if(txtINQNO.getText().trim().length() >0)
					M_strSQLQRY += " and IQ_INQNO like '" + txtINQNO.getText().trim() + "%'";
				M_strSQLQRY += " Order by IQ_INQNO desc ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtINDNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtINDNO";
				String L_ARRHDR[] = {"Indent No.","Indent Date"};
				M_strSQLQRY = "Select distinct IN_INDNO,IN_INDDT from MM_INMST ";
				M_strSQLQRY += " where IN_STRTP ='"+M_strSBSCD.substring(2,4)+"'" +" and isnull(IN_STSFL,'') ='4'";
				M_strSQLQRY += " AND isnull(IN_AUTQT,0) -isnull(IN_ORDQT,0)- isnull(IN_FCCQT,0) >0";
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
				M_strSQLQRY += " where PT_PRTTP ='S' and isnull(PT_STSFL,'') <>'X'";
				if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " and PT_PRTCD like '" + txtVENCD.getText().trim().toUpperCase() + "%'";
				M_strSQLQRY += " Order by pt_prtcd ";
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT",L_inaCOLVN);
			}
			else if(M_objSOURC == tblINQDT.cmpEDITR[TBL_UOMCD])
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPOUOM";
				String L_ARRHDR[] = {"UOM Code","Description"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP ='MST' and CMT_CGSTP ='COXXUOM' and isnull(CMT_STSFL,'') <>'X'";
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
			else if(M_strHLPFLD.equals("txtINQNO"))
			{
				L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtINQNO.setText(L_STRTKN.nextToken());
				txtINQDT.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtPOUOM"))
			{
				txtPOUOM.setText(cl_dat.M_strHLPSTR_pbst);
			}
		
	}
	void setENBL(boolean P_flgSTAT)
	{
		super.setEnabled(P_flgSTAT);
		txtINQDT.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			txtINQNO.setEnabled(false);
			txtINDNO.setEnabled(true);
			txtMATCD.setEnabled(true);
			txtVENCD.setEnabled(true);
			btnPROVN.setEnabled(true);
		}
		else
		{
			txtINQNO.setEnabled(true);
			txtINDNO.setEnabled(false);
			txtMATCD.setEnabled(false);
			txtVENCD.setEnabled(false);
			btnPROVN.setEnabled(false);
		}
		tblVENDT.setEnabled(false);
		tblINQDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblINQDT.cmpEDITR[TBL_MATDS].setEnabled(false);
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
			tblVENDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
	    {
			tblINQDT.cmpEDITR[TBL_UOMCD].setEnabled(true);
			tblINQDT.cmpEDITR[TBL_INQQT].setEnabled(true);
		
		}
	}
	boolean vldDATA()
	{
		int L_intRECCT =0,L_intVENCT =0;
		/*if(txtINQDT.getText().trim().length() ==0)
		{
			setMSG("INQuiry Date can not be blank..",'E');
		}*/
		if(tblINQDT.isEditing())
			tblINQDT.getCellEditor().stopCellEditing();
		tblINQDT.setRowSelectionInterval(0,0);
		tblINQDT.setColumnSelectionInterval(0,0);
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
		for(int i=0;i<tblINQDT.getRowCount();i++)
		{
			if(tblINQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				if(tblINQDT.getValueAt(i,TBL_UOMCD).toString().equals(""))
				{
					setMSG("Please Enter UOM Code..",'E');
					tblINQDT.cmpEDITR[TBL_UOMCD].requestFocus();
					return false;
				}
				if(tblINQDT.getValueAt(i,TBL_INQQT).toString().equals(""))
				{
					setMSG("Please Enter Quantity..",'E');
					tblINQDT.cmpEDITR[TBL_INQQT].requestFocus();
					return false;
				}
				else if(Double.parseDouble(tblINQDT.getValueAt(i,TBL_INQQT).toString())==0)
				{
					setMSG("Please Enter Quantity..",'E');
					tblINQDT.cmpEDITR[TBL_INQQT].requestFocus();
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
		String L_strVENCD ="";
		for(int i=0;i<tblVENDT.getRowCount();i++)
		{
			if(tblVENDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_strVENCD = tblVENDT.getValueAt(i,TBL_VENCD).toString();
				vldVENCD(L_strVENCD);
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
			M_strSQLQRY = "SELECT distinct IQ_MMSBS,IQ_STRTP,IQ_INQNO,IQ_INQDT,IQ_INDNO,IQ_MATCD,IQ_UOMCD,IQ_INQQT"+
						  ",IQ_TRNFL,IQ_STSFL,IQ_LUSBY,IQ_LUPDT,CT_MATDS from MM_IQMST,CO_CTMST "+
						  " WHERE ltrim(str(IQ_MATCD,20,0)) = CT_MATCD and CT_CODTP ='CD' AND isnull(IQ_STSFL,'') <>'X'"+
						 // " AND PT_PRTTP ='S' and PT_PRTCD = IQ_VENCD AND isnull(PT_STSFL,'') <>'X'"+
						  " AND IQ_MMSBS ='"+M_strSBSCD+"' AND IQ_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
						  " AND IQ_INQNO ='"+txtINQNO.getText().trim() +"' ORDER BY IQ_INQNO,IQ_MATCD";
						  System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
					tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_INDNO"),""),L_intRECCT,TBL_INDNO);
					tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_MATCD"),""),L_intRECCT,TBL_MATCD);
					tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intRECCT,TBL_MATDS);
					tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_UOMCD"),""),L_intRECCT,TBL_UOMCD);
					tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IQ_INQQT"),""),L_intRECCT,TBL_INQQT);
				
					L_intRECCT++;
				}
			if(L_intRECCT ==0)
			{
				setMSG("Invalid INQuiry No ..",'E');
				return false;
			}
			else
			{
				M_strSQLQRY = "SELECT distinct IQ_MMSBS,IQ_STRTP,IQ_INQNO,IQ_VENCD,PT_PRTNM"+
						  " from MM_IQMST,CO_PTMST "+
						  " WHERE PT_PRTTP ='S' and PT_PRTCD = IQ_VENCD AND isnull(IQ_STSFL,'') <>'X' AND isnull(PT_STSFL,'') <>'X'"+
						  " AND IQ_MMSBS ='"+M_strSBSCD+"' AND IQ_STRTP ='"+M_strSBSCD.substring(2,4) +"'"+
						  " AND IQ_INQNO ='"+txtINQNO.getText().trim() +"' ORDER BY IQ_VENCD";
				System.out.println(M_strSQLQRY);
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
			if(tblINQDT.isEditing())
				tblINQDT.getCellEditor().stopCellEditing();
			if(tblVENDT.isEditing())
				tblVENDT.getCellEditor().stopCellEditing();
			cl_dat.M_flgLCUPD_pbst = true;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Saving of data in progress..",'N');
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtINQNO.setText(cl_dat.M_strLOGDT_pbst);
				if(!genINQNO())
				{
					setCursor(cl_dat.M_curDFSTS_pbst);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
				strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
			//	for(int i=0;i<tblINQDT.getRowCount();i++)
				for(int i=0;i<intINDCT;i++)
				{
					if(tblINQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						for(int j=0;j<intVENCT;j++)
						{
							if(tblVENDT.getValueAt(j,TBL_CHKFL).toString().equals("true"))
							{
								
								pstmINSEQ.setString(1,M_strSBSCD);
								pstmINSEQ.setString(2,M_strSBSCD.substring(2,4));
								pstmINSEQ.setString(3,txtINQNO.getText().trim());
								pstmINSEQ.setDate(4,Date.valueOf(strTEMP));
							    pstmINSEQ.setString(5,tblINQDT.getValueAt(i,TBL_INDNO).toString());
								pstmINSEQ.setString(6,tblINQDT.getValueAt(i,TBL_MATCD).toString());
								pstmINSEQ.setString(7,tblINQDT.getValueAt(i,TBL_UOMCD).toString());
								pstmINSEQ.setString(8,tblVENDT.getValueAt(j,TBL_VENCD).toString());
								pstmINSEQ.setFloat(9,Float.valueOf(tblINQDT.getValueAt(i,TBL_INQQT).toString()).floatValue());
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
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MMXXINQ'";
			M_strSQLQRY += " AND CMT_CODCD = '" +cl_dat.M_strFNNYR_pbst.substring(3)+ strINQTR_fn+ "'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
			else 
				updINQDT();
			if(cl_dat.M_flgLCUPD_pbst)
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Data saved successfully..",'N');
				JOptionPane.showMessageDialog(this,"Please, Note down the INQuiry No. " + txtINQNO.getText().trim(),"INQuiry No.",JOptionPane.INFORMATION_MESSAGE);
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
	private boolean genINQNO()
	{
		try
		{
		
			String L_strINQNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01 ="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MMXXINQ' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) +  strINQTR_fn + "'";
		
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
				L_strINQNO += "0";
			
			L_CCSVL = L_strINQNO + L_CCSVL;
			strSRLNO = L_CCSVL;
			L_strINQNO = L_CODCD + L_CCSVL;
			txtINQNO.setText(L_strINQNO);
			return true;
		}catch(Exception L_EX){
			setMSG(L_EX,"genINQNO");
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
		pstmINSRM.setString(6,txtINQNO.getText().trim());
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
boolean updINQDT()
{
	boolean L_flgINQFL =false;
	boolean L_flgVENFL =false;
	boolean L_flgDELFL =false;
	try
	{
		int i=0;
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			L_flgDELFL = true;
		for(i=0;i<tblINQDT.getRowCount();i++)
		{
			if(tblINQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				L_flgINQFL = true;
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
		if((L_flgINQFL) &&(!L_flgVENFL))
		{
			// Selected INQ (Ind. No /Mat cd) is to be removed for all the vendors
			for(i=0;i<tblINQDT.getRowCount();i++)
			{
				if(tblINQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					M_strSQLQRY = "UPDATE MM_IQMST SET ";
					if(L_flgDELFL)
						M_strSQLQRY += getUSGDTL("EQ",'U',"X");
					else
					{
						M_strSQLQRY += getUSGDTL("EQ",'U',null)+",";
						M_strSQLQRY += " IQ_INQQT ="+ tblINQDT.getValueAt(i,TBL_INQQT).toString() ;
					}
					M_strSQLQRY += " WHERE IQ_MMSBS ='"+M_strSBSCD+"' and IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND IQ_INQNO ='"+txtINQNO.getText().trim() +"'";
					M_strSQLQRY += " AND IQ_INDNO ='"+ tblINQDT.getValueAt(i,TBL_INDNO).toString() +"'";
					M_strSQLQRY += " AND IQ_MATCD ='"+ tblINQDT.getValueAt(i,TBL_MATCD).toString() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
		}
		i=0;
		if((!L_flgINQFL) &&(L_flgVENFL))
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
						M_strSQLQRY += " AND IQ_INQQT ='"+ tblINQDT.getValueAt(i,TBL_INQQT).toString() +"'";
					}
					M_strSQLQRY += " WHERE IQ_MMSBS ='"+M_strSBSCD+"' and IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND IQ_INQNO ='"+txtINQNO.getText().trim() +"'";
					M_strSQLQRY += " AND IQ_VENCD ='"+ tblVENDT.getValueAt(i,TBL_VENCD).toString() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
		}
	/*	if((L_flgINQFL) &&(L_flgVENFL))
		{
			// Selected Vendors is to be removed for all the Indent /Matcd
			for(i=0;i<tblINQDT.getRowCount();i++)
			{
				if(tblINQDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
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
								M_strSQLQRY += " AND IQ_INQQT ='"+ tblINQDT.getValueAt(i,TBL_INQQT).toString() +"'";
							}
							M_strSQLQRY += " WHERE IQ_MMSBS ='"+M_strSBSCD+"' and IQ_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
							M_strSQLQRY += " AND IQ_INQNO ='"+txtINQNO.getText().trim() +"'";
							M_strSQLQRY += " AND IQ_INDNO ='"+ tblINQDT.getValueAt(i,TBL_INDNO).toString() +"'";
							M_strSQLQRY += " AND IQ_MATCD ='"+ tblINQDT.getValueAt(i,TBL_MATCD).toString() +"'";
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
		if(tblINQDT.isEditing())
			tblINQDT.getCellEditor().stopCellEditing();
		tblINQDT.setRowSelectionInterval(0,0);
		tblINQDT.setColumnSelectionInterval(0,0);
        M_strSQLQRY = "Select IN_INDNO,IN_MATCD,CT_MATDS,CT_UOMCD,isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0) L_PQTY from MM_INMST,CO_CTMST WHERE IN_MATCD = CT_MATCD AND isnull(IN_STSFL,'') <>'X'";
        M_strSQLQRY +=" AND (isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)) >0";
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
			   tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),intINDCT,TBL_INDNO);
			   tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),""),intINDCT,TBL_MATCD);
			   tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),intINDCT,TBL_MATDS);
			   tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),intINDCT,TBL_UOMCD);
			   L_strPINQT = nvlSTRVL(M_rstRSSET.getString("L_PQTY"),"");
			   tblINQDT.setValueAt(L_strPINQT,intINDCT,TBL_INQQT);
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
        M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST WHERE isnull(CT_STSFL,'') <>'X'";
        M_strSQLQRY +=" and CT_MATCD ='"+txtMATCD.getText().trim()+"'";
		if(tblINQDT.isEditing())
			tblINQDT.getCellEditor().stopCellEditing();
		tblINQDT.setRowSelectionInterval(0,0);
		tblINQDT.setColumnSelectionInterval(0,0);
		if(hstMATCD.get(txtMATCD.getText().trim())== null)
		{
			hstMATCD.put(txtMATCD.getText().trim(),"");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
			   L_intRECCT++;
			   tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),intINDCT,TBL_MATCD);
			   tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),intINDCT,TBL_MATDS);
			   tblINQDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),intINDCT,TBL_UOMCD);
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
	    M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST WHERE isnull(PT_STSFL,'') <>'X'";
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
        M_strSQLQRY = "Select distinct CTP_PRTCD,PT_PRTNM from CO_CTPTR,CO_PTMST WHERE isnull(CTP_STSFL,'') <>'X'";
		M_strSQLQRY +=" and CTP_PRTTP = PT_PRTTP AND CTP_PRTCD = PT_PRTCD AND CTP_PRTTP ='S' and CTP_MATCD IN(";
		int intFIRST =0;
		for(int i=0;i<tblINQDT.getRowCount();i++)
		{
			if(tblINQDT.getValueAt(i,TBL_CHKFL).equals(Boolean.TRUE))
			{
				intFIRST++;
				if(intFIRST == 1)
				{
					M_strSQLQRY += "'"+tblINQDT.getValueAt(i,TBL_MATCD).toString()+"'";
				}
				else
					M_strSQLQRY += ",'"+tblINQDT.getValueAt(i,TBL_MATCD).toString()+"'";
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

private boolean vldVENCD(String P_strVENCD)
{
	try
	{
		String L_strPRTNM="";
		int L_intDAYS =0;
		M_strSQLQRY ="SELECT PT_PRTCD,PT_PRTNM,(days(current_date)-days(PT_EVLDT))L_DAYS FROM CO_PTMST WHERE PT_PRTTP ='S' and PT_PRTCD ='"+P_strVENCD.trim().toUpperCase() +"'";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		{
			if(M_rstRSSET.next())
			{
				L_intDAYS = M_rstRSSET.getInt("L_DAYS");
			}
			M_rstRSSET.close();
		}
		M_strSQLQRY ="SELECT MAX(PO_PORDT)L_PORDT,days(current_date)-days(MAX(PO_PORDT))L_DAYS FROM MM_POMST WHERE isnull(PO_STSFL,'') <>'X' and PO_VENCD ='"+P_strVENCD.trim() +"'";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		java.sql.Date datTEMP;
		if(M_rstRSSET !=null)
		{
			if(M_rstRSSET.next())
			{
				datTEMP = M_rstRSSET.getDate("L_PORDT");
				if(datTEMP == null)
				{
					if(L_intDAYS > 730)
					{
						JOptionPane.showMessageDialog(this,"No order is placed,Please Approve the vendor "+P_strVENCD,"Vendor",JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
				}
				if(M_rstRSSET.getInt("L_DAYS")>730)
				{
					if(L_intDAYS > 730)
					{
						JOptionPane.showMessageDialog(this,"No order is placed for last two years, Re-evaluate the Vendor.."+P_strVENCD,"Vendor",JOptionPane.INFORMATION_MESSAGE);
						setMSG("No Order has been Placed for last 2 years ..",'E');
					}
					//return false;
				}
				//System.out.println(L_intDAYS);
			}
			else
			{
				setMSG("Last Order date not found for checking..",'E');
			}
			M_rstRSSET.close();
		}
		return true;
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"vldPRTCD");
		return false;
	}	
		
}

}
