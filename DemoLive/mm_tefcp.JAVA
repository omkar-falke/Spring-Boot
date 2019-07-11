import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.util.Vector;
import javax.swing.InputVerifier;
import java.util.StringTokenizer;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

class mm_tefcp extends cl_pbase
{
	
	private JTextField txtPORNO,txtAMDNO,txtPORDT,txtAMDDT;
	private JTabbedPane jtpPODTL;
	private java.sql.PreparedStatement pstmUPDREC;
	private JPanel pnlPODTL;
	private cl_JTable tblPODTL;
	private JButton btnFRWD;
	private JLabel lblMATCD;
	private String strSTRTP,strPORNO,strPRAMD,strSRLNO,strTEMP,strMATCD,strPORDT="";
	private JTextField txtDSBEDT,txtENBEDT;	
	private JCheckBox chkENBFL ;
	//private Hashtable<String,String> hstITMDT = new Hashtable<String,String>();
	private Hashtable<String,String> hstPENQT = new Hashtable<String,String>();
	private mm_teporTBLINVFR objTBLVRF;
	private int intROWCT =0; 
	private final String fnlAUTFL ="A";
	
	final int TBL_CHKFL = 0;
	final int TBL_INDNO = 1;
	final int TBL_MATCD = 2;
	final int TBL_UOMCD = 3;
	final int TBL_MATDS = 4;
	final int TBL_PORQT = 5;
	final int TBL_ACPQT = 6;
	final int TBL_FRCQT = 7;
	final int TBL_BALQT = 8;
	final int TBL_STSFL = 9;

	mm_tefcp()
	{
		super(2);
		try
		{
			setMatrix(20,8);		
			java.awt.Color colBLUE = new java.awt.Color(63,91,167);
			pnlPODTL = new JPanel(null);
		//	jtpPODTL=new JTabbedPane();
		//	jtpPODTL.add(pnlHISTR,"History");
		
			add(new JLabel("P.O. Number"),1,1,1,1,this,'L');
			add(txtPORNO = new TxtLimit(8),1,2,1,1,this,'L');
			add(new JLabel("P.O. Date"),1,3,1,1,this,'L');
			add(txtPORDT = new TxtDate(),1,4,1,1,this,'L');
			add(new JLabel("Amd. Number"),1,5,1,1,this,'L');
			add(txtAMDNO = new TxtLimit(2),1,6,1,1,this,'L');
			add(new JLabel("Amendment Dt."),1,7,1,1,this,'L');
			add(txtAMDDT = new TxtDate(),1,8,1,1,this,'L');
			
			String[] L_strTB1HD = {" ","Indent No.","Item Code","UOM","Description","Ord.Qty","Acp.Qty","F.C.Qty","Bal qty","Sts."};
			int[] L_intCOLSZ = {20,60,75,35,330,55,55,55,55,10};
			tblPODTL = crtTBLPNL1(pnlPODTL,L_strTB1HD,200,1,1,12,7.9,L_intCOLSZ,new int[]{0});
			tblPODTL.addMouseListener(this);
			add(pnlPODTL,2,1,13,8,this,'L');
		//	add(jtpPODTL,12,1,6,8,this,'L');	
			pstmUPDREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"UPDATE MM_POMST SET PO_FRCQT = ISNULL(PO_FRCQT,0) + ?"+
					    ",PO_TRNFL =?,PO_LUSBY=?,PO_LUPDT = ?"+
						// Commented on 30/04/2005, as trigger was getting fired due to this
						// which resulted in stock discreancies
						//",PO_STSFL =? "+
						" WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS = ? AND PO_STRTP = ? AND PO_PORNO = ? AND PO_AMDNO = ? AND PO_INDNO = ? and PO_MATCD = ? AND ISNULL(PO_STSFL,'') <>'X'"); 
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_teporTBLINVFR();
			tblPODTL.setInputVerifier(objTBLVRF);		
			setENBL(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"Constuctor");
		}
}
private void getDATA()
{
	try
	{
		java.sql.Date L_datTEMP;
		java.sql.Timestamp L_tmsTEMP;
		int L_intROWCT =0;
		int L_intPORQT=0;
		int L_intDAYS= 0;
		String L_strMATCD ="",L_strINDNO ="",L_strPORQT ="0.00"	;
		String L_strACPQT ="0.00",L_strFCCQT ="0.00",L_strBALQT ="0.00";
		String L_strDPTCD;
		java.sql.Date datTEMP;
		strPORDT ="";
		M_strSQLQRY = " Select PO_MMSBS,PO_STRTP,PO_PORNO,PO_INDNO,PO_AMDNO,PO_MATCD,PO_PORDT,PO_AMDDT,"+
					  " PO_PORQT,PO_ACPQT,PO_FRCQT,(ISNULL(PO_PORQT,0) - ISNULL(PO_FRCQT,0)- ISNULL(PO_ACPQT,0))L_BALQT,PO_STSFL,CT_MATDS,CT_UOMCD from MM_POMST,CO_CTMST ";
		M_strSQLQRY +=" where PO_MATCD = CT_MATCD AND ISNULL(CT_STSFL,'') <>'X' and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  PO_MMSBS ='"+M_strSBSCD+"'" +" AND PO_STRTP ='"+strSTRTP +"'";
		M_strSQLQRY +=" AND PO_PORNO ='"+txtPORNO.getText().trim()+"' and ISNULL(PO_PORQT,0) >0 and ISNULL(PO_STSFL,'') IN('A','H') and ISNULL(PO_PORQT,0)-ISNULL(PO_FRCQT,0)-ISNULL(PO_ACPQT,0) >0";
		clrCOMP();
		this.setCursor(cl_dat.M_curWTSTS_pbst);		
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		intROWCT =0;
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
			if(L_intROWCT == 0)
			{
				txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),""));
				setMSG("",'N');
				txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""));
				L_datTEMP = M_rstRSSET.getDate("PO_PORDT");
				if(L_datTEMP !=null)
				{
					txtPORDT.setText(M_fmtLCDAT.format(L_datTEMP));
					strPORDT = txtPORDT.getText().trim();
				}
				L_datTEMP = M_rstRSSET.getDate("PO_AMDDT");
				if(L_datTEMP !=null)
				{
					txtAMDDT.setText(M_fmtLCDAT.format(L_datTEMP));
				}
			}
			L_strINDNO = nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),"");
			L_strMATCD = nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),"");
			L_strPORQT = nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0.00");
			L_strACPQT = nvlSTRVL(M_rstRSSET.getString("PO_ACPQT"),"0.00");
			L_strFCCQT = nvlSTRVL(M_rstRSSET.getString("PO_FRCQT"),"0.00");
			L_strBALQT = nvlSTRVL(M_rstRSSET.getString("L_BALQT"),"0.00");
			if(hstPENQT !=null)
				hstPENQT.put(L_strINDNO+L_strMATCD,L_strBALQT);
			//tblPODTL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_CHKFL);
			//tblPODTL.setValueAt("C",L_intROWCT,TBL_STSFL);
			tblPODTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_STSFL"),""),L_intROWCT,TBL_STSFL);
			tblPODTL.setValueAt(L_strMATCD,L_intROWCT,TBL_MATCD);
			tblPODTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),L_intROWCT,TBL_UOMCD);
			tblPODTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intROWCT,TBL_MATDS);
			tblPODTL.setValueAt(String.valueOf(L_strINDNO),L_intROWCT,TBL_INDNO);
			tblPODTL.setValueAt(String.valueOf(L_strPORQT),L_intROWCT,TBL_PORQT);
			tblPODTL.setValueAt(String.valueOf(L_strACPQT),L_intROWCT,TBL_ACPQT);
			tblPODTL.setValueAt(String.valueOf(L_strFCCQT),L_intROWCT,TBL_FRCQT);
			tblPODTL.setValueAt(String.valueOf(L_strBALQT),L_intROWCT,TBL_BALQT);
			L_intROWCT++;
			
		}
		intROWCT = L_intROWCT;
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getDATA");
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
}
public void actionPerformed(ActionEvent L_AE)
{
	super.actionPerformed(L_AE);
	if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
	{
		clrCOMP();
		txtPORNO.setText("");
		txtAMDNO.setText("");
		hstPENQT.clear();
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
		{
			setENBL(false);
			setMSG("Please select an option ..",'N');
		}
		if(M_strSBSCD !=null)
		{
			M_strSBSCD = M_strSBSCD;
			strSTRTP = M_strSBSCD.substring(2,4);
		}
			setMSG("Press F1 to select the P.O. number..",'N');
			txtPORNO.requestFocus();
	}
	else if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
	{
		clrCOMP();
	}
	else if(M_objSOURC == txtPORNO)
	{
		getDATA();
	}
	}
public void keyPressed(KeyEvent L_KE)
{
	super.keyPressed(L_KE);
	if(L_KE.getKeyCode() ==L_KE.VK_F1)
	{
		if(M_objSOURC == txtPORNO)
		{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtPORNO";
			String L_ARRHDR[] = {"p.o. Number","Amd no.","Status"};
			M_strSQLQRY = "Select distinct PO_PORNO,PO_AMDNO,PO_STSFL from MM_POMST ";
			M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ISNULL(PO_STSFL,'') IN('A','H') and ISNULL(PO_PORQT,0)-ISNULL(PO_FRCQT,0)-ISNULL(PO_ACPQT,0) >0 ";
			if(txtPORNO.getText().trim().length() >0)
				M_strSQLQRY += " and PO_PORNO like '" + txtPORNO.getText().trim() + "%'";
			M_strSQLQRY += " and PO_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
			M_strSQLQRY +=" Order by PO_PORNO ";
			cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
		}
	}
}
void exeHLPOK()
{
	super.exeHLPOK();
	if(M_strHLPFLD.equals("txtPORNO"))
	{
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
		txtPORNO.setText(L_STRTKN.nextToken());
		txtAMDNO.setText(L_STRTKN.nextToken());
	}
}
void setENBL(boolean L_STAT)
{
	super.setENBL(false);
	txtPORNO.setEnabled(true);
	tblPODTL.cmpEDITR[0].setEnabled(true);
	tblPODTL.cmpEDITR[TBL_BALQT].setEnabled(true);
}
private class mm_teporTBLINVFR extends TableInputVerifier
{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID==TBL_BALQT)
				{
					strTEMP = tblPODTL.getValueAt(P_intROWID,TBL_BALQT).toString();
					//if(Double.parseDouble(strTEMP)> (Double.parseDouble(tblPODTL.getValueAt(P_intROWID,TBL_PORQT).toString())-Double.parseDouble(tblPODTL.getValueAt(P_intROWID,TBL_FRCQT).toString())-Double.parseDouble(tblPODTL.getValueAt(P_intROWID,TBL_ACPQT).toString())))
					//System.out.println(Double.parseDouble(strTEMP));
					//System.out.println(Double.parseDouble(hstPENQT.get(tblPODTL.getValueAt(P_intROWID,TBL_INDNO).toString()+tblPODTL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString()));
					if(Double.parseDouble(strTEMP)> (Double.parseDouble(hstPENQT.get(tblPODTL.getValueAt(P_intROWID,TBL_INDNO).toString()+tblPODTL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString())))
					{
						setMSG("Qty. for F.C. can not be greater than Pending P.O. Qty..",'E');
						return false;
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
boolean vldDATA()
{
	try
	{
		int L_intCOUNT=0;
		setMSG("",'N');
		if(txtPORNO.getText().trim().length() ==0)
		{
			setMSG("P.O. No. can not be blank..",'E');
			return false;
		}
		for(int i=0;i<tblPODTL.getRowCount();i++)
		{
			strTEMP = tblPODTL.getValueAt(i,TBL_BALQT).toString();
			if(tblPODTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				//System.out.println(Double.parseDouble(strTEMP));
				//System.out.println(Double.parseDouble(hstPENQT.get(tblPODTL.getValueAt(i,TBL_INDNO).toString()+tblPODTL.getValueAt(i,TBL_MATCD).toString()).toString()));
				
				//if(Double.parseDouble(strTEMP)> (Double.parseDouble(tblPODTL.getValueAt(i,TBL_PORQT).toString())-Double.parseDouble(tblPODTL.getValueAt(i,TBL_ACPQT).toString())-Double.parseDouble(tblPODTL.getValueAt(i,TBL_FRCQT).toString())))
				if(Double.parseDouble(strTEMP)> (Double.parseDouble(hstPENQT.get(tblPODTL.getValueAt(i,TBL_INDNO).toString()+tblPODTL.getValueAt(i,TBL_MATCD).toString()).toString())))
				{
					setMSG("Forceful completion qty. can not be greater than pending P.O. qty..",'E');
					return false;
				}
				if(Double.parseDouble(tblPODTL.getValueAt(i,TBL_PORQT).toString())-Double.parseDouble(tblPODTL.getValueAt(i,TBL_ACPQT).toString())-Double.parseDouble(tblPODTL.getValueAt(i,TBL_FRCQT).toString())-Double.parseDouble(tblPODTL.getValueAt(i,TBL_BALQT).toString())==0)
				{
					tblPODTL.setValueAt("C",i,TBL_STSFL);
				}
				L_intCOUNT++;
			}
		}
		if(L_intCOUNT ==0)
		{
			setMSG("Select at least one Row for updating..",'E');
			return false;
		}
	}
	catch(Exception L_E)
	{
		return false;
	}
	return true;
}
void exeSAVE()
{
	try
	{
		int L_intCOUNT =0;
		cl_dat.M_flgLCUPD_pbst = true;
		cl_dat.M_btnSAVE_pbst.setEnabled(false);
		String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
		if(!vldDATA())
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
			return;
		}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
			{
				for(int i=0;i<tblPODTL.getRowCount();i++)
				{
					if(tblPODTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						pstmUPDREC.setFloat(1,Float.valueOf(tblPODTL.getValueAt(i,TBL_BALQT).toString()).floatValue());
						pstmUPDREC.setString(2,"0");
						pstmUPDREC.setString(3,cl_dat.M_strUSRCD_pbst);
						if(L_strDATE !=null)	
							pstmUPDREC.setDate(4,Date.valueOf(L_strDATE));
						else
							pstmUPDREC.setDate(4,null);
					// Commented on 30/04/2005
					//	pstmUPDREC.setString(5,tblPODTL.getValueAt(i,TBL_STSFL).toString());
						pstmUPDREC.setString(5,M_strSBSCD);
						pstmUPDREC.setString(6,strSTRTP);
						pstmUPDREC.setString(7,txtPORNO.getText().trim());
						pstmUPDREC.setString(8,txtAMDNO.getText().trim());
						pstmUPDREC.setString(9,tblPODTL.getValueAt(i,TBL_INDNO).toString());
						pstmUPDREC.setString(10,tblPODTL.getValueAt(i,TBL_MATCD).toString());
						pstmUPDREC.executeUpdate();
						
						M_strSQLQRY =" SELECT COUNT(*) L_CNT FROM MM_INMST WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP ='"+strSTRTP+"'";
						M_strSQLQRY +=" AND IN_INDNO ='"+tblPODTL.getValueAt(i,TBL_INDNO).toString()+"'";
						M_strSQLQRY +=" AND IN_MATCD ='"+tblPODTL.getValueAt(i,TBL_MATCD).toString()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						L_intCOUNT =0;
						if(M_rstRSSET !=null)
							if(M_rstRSSET.next())
							{
								L_intCOUNT = M_rstRSSET.getInt("L_CNT");
							}
						M_strSQLQRY ="UPDATE MM_STMST SET ST_STKOR = ST_STKOR - "+tblPODTL.getValueAt(i,TBL_BALQT).toString()+", ";
						if(L_intCOUNT >0)
							M_strSQLQRY +="ST_STKIN = ST_STKIN + "+tblPODTL.getValueAt(i,TBL_BALQT).toString()+",";
						M_strSQLQRY +=" ST_TRNFL ='0' where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
						M_strSQLQRY +=" AND ST_MATCD ='"+tblPODTL.getValueAt(i,TBL_MATCD).toString()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(L_intCOUNT >0)
						{
							M_strSQLQRY ="UPDATE MM_INMST SET IN_ORDQT = IN_ORDQT - "+tblPODTL.getValueAt(i,TBL_BALQT).toString()+", ";
							M_strSQLQRY +=" IN_TRNFL ='0' where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
							M_strSQLQRY +=" AND IN_INDNO ='"+tblPODTL.getValueAt(i,TBL_INDNO).toString()+"'";
							M_strSQLQRY +=" AND IN_MATCD ='"+tblPODTL.getValueAt(i,TBL_MATCD).toString()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"");
						}
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Record saved Successfully",'N');
				clrCOMP();
				hstPENQT.clear();
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
			}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"exeSAVE");
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	strPORDT = cl_dat.M_strLOGDT_pbst;
}
class inpVRFY extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		if(input == txtPORNO)
		{
			getDATA();
			if(intROWCT == 0)
			{
				setMSG("P.O. is not available for Forceful completion ..",'E');
				return false;
			}
		}
		return true;
	}
}

}
