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

class mm_tefci extends cl_pbase
{
	
	private JTextField txtINDNO,txtAMDNO,txtINDDT,txtAMDDT,txtINDST,txtPREBY,txtPREDT,txtPRETM,txtAUTDT,txtAUTTM,txtAUTBY,txtFRWDT,txtFRWTM,txtFRWBY,txtITVAL,txtDPTCD,txtDPTDS;
	private JTabbedPane jtpINDTL;
	private java.sql.PreparedStatement pstmUPDREC;
	private JPanel pnlGNDTL,pnlINDTL,pnlHISTR;
	private cl_JTable tblINDTL,tblVNDTL;
	private JButton btnFRWD;
	private JLabel lblMATCD;
	private String strSTRTP,strINDNO,strPRAMD,strSRLNO,strTEMP,strINDRM,strOTHRM,strMATCD,strINDST ="",strMATTP ="",strINDDT="",strFRWTO,strFRWBY,strFRWDT;
	private float fltINDVL ;
	private int intROWCT =0;
	private boolean flgINDRM = false;
	private boolean flgAMDFL = false;
	private JTextField txtDSBEDT,txtENBEDT;	
	private JCheckBox chkENBFL ;
	//private Hashtable hstITMDT = new Hashtable();
	private Hashtable<String,String> hstITMVL = new Hashtable<String,String>();
	private Vector vtrFRWBY = new Vector();
	private mm_teindTBLINVFR objTBLVRF;
	private Hashtable<String,String> hstPENQT = new Hashtable<String,String>();
	final String fnlAUTFL ="4";
	final String strINDTR_fn ="10";

	final int TBL_CHKFL = 0;
	final int TBL_MATCD = 1;
	final int TBL_UOMCD = 2;
	final int TBL_MATDS = 3;
	final int TBL_AUTQT = 4;
	final int TBL_ORDQT = 5;
	final int TBL_ACPQT = 6;
	final int TBL_FCCQT = 7;
	final int TBL_BALQT = 8;
	final int TBL_STKFL = 9;
	final String strTRNTP ="IN";

	mm_tefci()
	{
		super(2);
		try
		{
			setMatrix(20,8);		
			java.awt.Color colBLUE = new java.awt.Color(63,91,167);
			pnlINDTL = new JPanel(null);
			pnlHISTR = new JPanel(null);
		
		
			jtpINDTL=new JTabbedPane();
			jtpINDTL.add(pnlHISTR,"History");
		
			add(new JLabel("Indent Number"),1,1,1,1,this,'L');
			add(txtINDNO = new TxtLimit(8),1,2,1,1,this,'L');
			add(new JLabel("Amd. Number"),1,3,1,1,this,'L');
			add(txtAMDNO = new TxtLimit(2),1,4,1,1,this,'L');
			add(new JLabel("Department"),1,5,1,1,this,'L');
			add(txtDPTCD=new TxtLimit(3),1,6,1,1,this,'L');
			add(txtDPTDS=new TxtLimit(40),1,7,1,2,this,'L');
	
			add(new JLabel("Prepared by "),1,1,1,1,pnlHISTR,'L');
			add(txtPREBY = new TxtLimit(3),1,2,1,1,pnlHISTR,'L');
			add(new JLabel("Prep. Date/Tm."),1,3,1,1,pnlHISTR,'L');
			add(txtPREDT = new TxtDate(),1,4,1,1,pnlHISTR,'L');
			add(txtPRETM = new TxtTime(),1,5,1,0.6,pnlHISTR,'L');
			add(new JLabel("Indent Date"),1,6,1,1,pnlHISTR,'L');
			add(txtINDDT = new TxtDate(),1,7,1,1,pnlHISTR,'L');
	
			add(new JLabel("Forwarded by"),2,1,1,1,pnlHISTR,'L');
			add(txtFRWBY = new TxtLimit(3),2,2,1,1,pnlHISTR,'L');
			add(new JLabel("Forw.Date/Tm."),2,3,1,1,pnlHISTR,'L');
			add(txtFRWDT = new TxtDate(),2,4,1,1,pnlHISTR,'L');
			add(txtFRWTM = new TxtTime(),2,5,1,0.6,pnlHISTR,'L');
		
			add(new JLabel("Amendment Dt."),2,6,1,1,pnlHISTR,'L');
			add(txtAMDDT = new TxtDate(),2,7,1,1,pnlHISTR,'L');
		
			add(new JLabel("Indent Status"),3,6,1,1,pnlHISTR,'L');
			add(txtINDST = new TxtLimit(30),3,7,1,1.7,pnlHISTR,'L');
	
			add(new JLabel("Authorised by"),3,1,1,1,pnlHISTR,'L');
			add(txtAUTBY = new TxtDate(),3,2,1,1,pnlHISTR,'L');
			add(new JLabel("Auth.Date/Tm."),3,3,1,1,pnlHISTR,'L');
			add(txtAUTDT = new TxtDate(),3,4,1,1,pnlHISTR,'L');
			add(txtAUTTM = new TxtTime(),3,5,1,0.6,pnlHISTR,'L');

		//	txtDPTCD.setInputVerifier(new inpVRFY());
			String[] L_strTB1HD = {" ","Item Code","UOM","Description","Aut.Qty","Ord.Qty","Acp.Qty","F.C.Qty","Bal qty","Stk"};
			int[] L_intCOLSZ = {20,75,35,338,55,55,55,55,55,15};
			tblINDTL = crtTBLPNL1(pnlINDTL,L_strTB1HD,100,1,1,8,7.9,L_intCOLSZ,new int[]{0});
			tblINDTL.addMouseListener(this);
			add(pnlINDTL,2,1,9,8,this,'L');
			add(jtpINDTL,12,1,6,8,this,'L');		
			pstmUPDREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"UPDATE MM_INMST SET IN_FCCQT = isnull(IN_FCCQT,0) + ?"+
					    ",IN_TRNFL =?,IN_LUSBY=?,IN_LUPDT = ?"+
						" WHERE IN_MMSBS = ? AND IN_STRTP = ? AND IN_INDNO = ? AND IN_AMDNO = ? AND IN_MATCD = ? AND isnull(IN_STSFL,'') <>'X'"); 
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_teindTBLINVFR();
			tblINDTL.setInputVerifier(objTBLVRF);		
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
		int L_intINDQT=0;
		int L_intDAYS= 0;
		String L_strMATCD ="",L_strINDQT ="0.00"	;
		String L_strORDQT ="0.00",L_strACPQT ="0.00",L_strFCCQT ="0.00",L_strBALQT ="0.00";
		String L_strDPTCD;
		java.sql.Date datTEMP;
		strINDDT ="";
		intROWCT =0;
		M_strSQLQRY = " Select IN_MMSBS,IN_STRTP,IN_INDNO,IN_AMDNO,IN_MATCD,IN_INDDT,IN_AMDDT,"+
					  "IN_INDQT,IN_URGTG,IN_DPTCD,IN_PREBY,IN_PREDT,IN_FRWBY,IN_FRWDT,IN_FRWTO,"+
					  " IN_AUTBY,IN_AUTDT,IN_ACPQT,IN_REQDT,IN_EXPDT,(DATEDIFF(day,IN_INDDT,getdate()))L_DAYS,"+
					  " IN_ENQNO,IN_ENQSR,IN_CCTCD,IN_ORDQT,IN_AUTQT,IN_FCCQT,(isnull(IN_AUTQT,0) - isnull(IN_FCCQT,0)- isnull(IN_ORDQT,0))L_BALQT,IN_INSFL,IN_TCFFL,IN_STSFL,CT_MATDS,CT_UOMCD,ST_STKFL from MM_INMST,CO_CTMST,MM_STMST ";
		M_strSQLQRY +=" where IN_MATCD = CT_MATCD AND isnull(CT_STSFL,'') <>'X' and IN_STRTP = ST_STRTP AND IN_MATCD = ST_MATCD and IN_MMSBS ='"+M_strSBSCD+"'" +" AND IN_STRTP ='"+strSTRTP +"'";
		M_strSQLQRY +=" AND IN_INDNO ='"+txtINDNO.getText().trim()+"' and isnull(IN_AUTQT,0) >0 and isnull(IN_STSFL,'')<>'X' and isnull(IN_AUTQT,0)-isnull(IN_FCCQT,0)-isnull(IN_ORDQT,0) >0";
		M_strSQLQRY += " and isnull(IN_STSFL,'') <>'X' ";
		clrCOMP();
		strINDST ="";
		this.setCursor(cl_dat.M_curWTSTS_pbst);		
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
			if(L_intROWCT == 0)
			{
				strINDST = nvlSTRVL(M_rstRSSET.getString("IN_STSFL"),""); 
				strFRWTO = nvlSTRVL(M_rstRSSET.getString("IN_FRWTO"),""); 
				strFRWBY = nvlSTRVL(M_rstRSSET.getString("IN_FRWBY"),""); 
				txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("IN_AMDNO"),""));
				setMSG("",'N');
				txtINDNO.setText(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""));
				L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"");
				txtDPTDS.setText(cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXDPT",L_strDPTCD));
				L_datTEMP = M_rstRSSET.getDate("IN_INDDT");
				if(L_datTEMP !=null)
				{
					txtINDDT.setText(M_fmtLCDAT.format(L_datTEMP));
					strINDDT = txtINDDT.getText().trim();
				}
				txtDPTCD.setText(L_strDPTCD);
				txtPREBY.setText(nvlSTRVL(M_rstRSSET.getString("IN_PREBY"),""));
				txtFRWBY.setText(strFRWBY);
				txtAUTBY.setText(nvlSTRVL(M_rstRSSET.getString("IN_AUTBY"),""));
				L_tmsTEMP = M_rstRSSET.getTimestamp("IN_PREDT");
				if(L_tmsTEMP !=null)
				{
					txtPREDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
					txtPRETM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
				}	
				L_tmsTEMP = M_rstRSSET.getTimestamp("IN_AUTDT");
				if(L_tmsTEMP !=null)
				{
					txtAUTDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
					txtAUTTM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
				}
				L_tmsTEMP = M_rstRSSET.getTimestamp("IN_FRWDT");
				if(L_tmsTEMP !=null)
				{
					txtFRWDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
					txtFRWTM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
				}
				L_datTEMP = M_rstRSSET.getDate("IN_AMDDT");
				if(L_datTEMP !=null)
				{
					txtAMDDT.setText(M_fmtLCDAT.format(L_datTEMP));
				}
					
				txtINDST.setText(cl_dat.getPRMCOD("CMT_CODDS","STS","MMXXIND",strINDST));
			}
			L_strMATCD = nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),"");
			L_strINDQT = nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.00");
			L_strORDQT = nvlSTRVL(M_rstRSSET.getString("IN_ORDQT"),"0.00");
			L_strACPQT = nvlSTRVL(M_rstRSSET.getString("IN_ACPQT"),"0.00");
			L_strFCCQT = nvlSTRVL(M_rstRSSET.getString("IN_FCCQT"),"0.00");
			L_strBALQT = nvlSTRVL(M_rstRSSET.getString("L_BALQT"),"0.00");
			tblINDTL.setValueAt(L_strMATCD,L_intROWCT,TBL_MATCD);
			tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),L_intROWCT,TBL_UOMCD);
			tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intROWCT,TBL_MATDS);
			tblINDTL.setValueAt(String.valueOf(L_strINDQT),L_intROWCT,TBL_AUTQT);
			tblINDTL.setValueAt(String.valueOf(L_strORDQT),L_intROWCT,TBL_ORDQT);
			tblINDTL.setValueAt(String.valueOf(L_strACPQT),L_intROWCT,TBL_ACPQT);
			tblINDTL.setValueAt(String.valueOf(L_strFCCQT),L_intROWCT,TBL_FCCQT);
			tblINDTL.setValueAt(String.valueOf(L_strBALQT),L_intROWCT,TBL_BALQT);
			tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),""),L_intROWCT,TBL_STKFL);
			if(hstPENQT !=null)
				hstPENQT.put(L_strMATCD,L_strBALQT);
			L_intROWCT++;
			
		}
		intROWCT =L_intROWCT;
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
		txtINDNO.setText("");
		txtAMDNO.setText("");
		hstITMVL.clear();
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
			setMSG("Press F1 to select the indent number..",'N');
			txtINDNO.requestFocus();
	}
	else if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
	{
		clrCOMP();
	}
	else if(M_objSOURC == txtINDNO)
	{
		getDATA();
	}
	}
public void keyPressed(KeyEvent L_KE)
{
	super.keyPressed(L_KE);
	if(L_KE.getKeyCode() ==L_KE.VK_F1)
	{
		if(M_objSOURC == txtINDNO)
		{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtINDNO";
			String L_ARRHDR[] = {"Indent number","Amd no.","Status","Dept Code"};
			M_strSQLQRY = "Select distinct IN_INDNO,IN_AMDNO,IN_STSFL,IN_DPTCD from MM_INMST ";
			M_strSQLQRY += " where isnull(IN_STSFL,'')<>'X' and isnull(IN_AUTQT,0)-isnull(IN_FCCQT,0)-isnull(IN_ORDQT,0) >0 ";
			if(txtINDNO.getText().trim().length() >0)
				M_strSQLQRY += " and IN_INDNO like '" + txtINDNO.getText().trim() + "%'";
			M_strSQLQRY += " and IN_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
	// commented on 03/08/2004 API HBP
		//	M_strSQLQRY += " AND IN_DPTCD = (SELECT EP_DPTCD FROM HR_EPMST WHERE ";
		//	M_strSQLQRY += " EP_EMPNO =(SELECT US_EMPCD FROM SA_USMST WHERE US_USRCD ='"+cl_dat.M_strUSRCD_pbst+"'))";
			M_strSQLQRY +=" Order by IN_INDNO ";
			cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
		}
	}
}
void exeHLPOK()
{
	super.exeHLPOK();
	if(M_strHLPFLD.equals("txtINDNO"))
	{
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
		txtINDNO.setText(L_STRTKN.nextToken());
		txtAMDNO.setText(L_STRTKN.nextToken());
	}
}
void setENBL(boolean L_STAT)
{
	super.setENBL(false);
	txtINDNO.setEnabled(true);
	tblINDTL.cmpEDITR[0].setEnabled(true);
	tblINDTL.cmpEDITR[TBL_BALQT].setEnabled(true);
}
private class mm_teindTBLINVFR extends TableInputVerifier
{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID==TBL_BALQT)
				{
					strTEMP = tblINDTL.getValueAt(P_intROWID,TBL_BALQT).toString();
					//if(Float.parseFloat(strTEMP)> (Float.parseFloat(tblINDTL.getValueAt(P_intROWID,TBL_AUTQT).toString())-Float.parseFloat(tblINDTL.getValueAt(P_intROWID,TBL_ORDQT).toString())-Float.parseFloat(tblINDTL.getValueAt(P_intROWID,TBL_FCCQT).toString())))
					if(Double.parseDouble(strTEMP)> (Double.parseDouble(hstPENQT.get(tblINDTL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString())))
					{
						setMSG("Qty. for F.C. can not be greater than Pending Indent Qty..",'E');
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
		setMSG("",'N');
		if(txtINDNO.getText().trim().length() ==0)
		{
			setMSG("Indent No. can not be blank..",'E');
			return false;
		}
		for(int i=0;i<tblINDTL.getRowCount();i++)
		{
			strTEMP = tblINDTL.getValueAt(i,TBL_BALQT).toString();
			if(tblINDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				//if(Float.parseFloat(strTEMP)> (Float.parseFloat(tblINDTL.getValueAt(i,TBL_AUTQT).toString())-Float.parseFloat(tblINDTL.getValueAt(i,TBL_ORDQT).toString())))
				if(Double.parseDouble(strTEMP)> (Double.parseDouble(hstPENQT.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()).toString())))
				{
					setMSG("Forceful completion qty. can not be greater than pending Indent qty..",'E');
					return false;
				}
				if(tblINDTL.getValueAt(i,TBL_STKFL).toString().equals("Y"))
				{
					setMSG(" F.C. is not allowed for stock controlled item at row.."+i+1,'E');
					return false;
				}
			}
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
				cl_dat.M_flgLCUPD_pbst = true;
				for(int i=0;i<tblINDTL.getRowCount();i++)
				{
					if(tblINDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						pstmUPDREC.setFloat(1,Float.valueOf(tblINDTL.getValueAt(i,TBL_BALQT).toString()).floatValue());
						pstmUPDREC.setString(2,"0");
						pstmUPDREC.setString(3,cl_dat.M_strUSRCD_pbst);
						if(L_strDATE !=null)	
							pstmUPDREC.setDate(4,Date.valueOf(L_strDATE));
						else
							pstmUPDREC.setDate(4,null);
						pstmUPDREC.setString(5,M_strSBSCD);
						pstmUPDREC.setString(6,strSTRTP);
						pstmUPDREC.setString(7,txtINDNO.getText().trim());
						pstmUPDREC.setString(8,txtAMDNO.getText().trim());
						pstmUPDREC.setString(9,tblINDTL.getValueAt(i,TBL_MATCD).toString());
						pstmUPDREC.executeUpdate();
						M_strSQLQRY ="UPDATE MM_STMST SET ST_STKIN = ST_STKIN - "+tblINDTL.getValueAt(i,TBL_BALQT).toString()+", ";
						M_strSQLQRY +=" ST_TRNFL ='0' where ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
						M_strSQLQRY +=" AND ST_MATCD ='"+tblINDTL.getValueAt(i,TBL_MATCD).toString()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Record saved Successfully",'N');
				clrCOMP();
				if(hstPENQT !=null)
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
	strINDDT = cl_dat.M_strLOGDT_pbst;
}
class inpVRFY extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		if(input == txtINDNO)
		{
			getDATA();
			if(intROWCT == 0)
			{
				setMSG("Indent is not available for Forceful completion ..",'E');
				return false;
			}
		}
		return true;
	}
}
}
