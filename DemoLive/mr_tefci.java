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

class mr_tefci extends cl_pbase
{
	
	private JTextField txtINDNO,txtAMDNO,txtINDDT,txtAMDDT,txtINDST,txtPREBY,txtPREDT,txtPRETM,txtAUTDT,txtAUTTM,txtAUTBY,txtFRWDT,txtFRWTM,txtFRWBY,txtITVAL,txtMKTTP,txtMKTDS;
	private JTabbedPane jtpINDTL;
	private java.sql.PreparedStatement pstmUPDREC;
	private JPanel pnlGNDTL,pnlINDTL;//pnlHISTR;
	private cl_JTable tblINDTL,tblVNDTL;
	private JButton btnFRWD;
	private JLabel lblPRDCD;
	private String strMKTTP,strINDNO,strPRAMD,strSRLNO,strTEMP,strINDRM,strOTHRM,strPRDCD,strINDST ="",strMATTP ="",strINDDT="",strFRWTO,strFRWBY,strFRWDT;
	private float fltINDVL ;
	private int intROWCT =0;
	private boolean flgINDRM = false;
	private boolean flgAMDFL = false;
	private JTextField txtDSBEDT,txtENBEDT;	
	private JCheckBox chkENBFL ;
	private Hashtable<String,String> hstITMVL = new Hashtable<String,String>();
	private Vector vtrFRWBY = new Vector();
	private mr_teindTBLINVFR objTBLVRF;
	private Hashtable<String,String> hstPENQT = new Hashtable<String,String>();
	final String fnlAUTFL ="4";
	final String strINDTR_fn ="10";

	final int TBL_CHKFL = 0;
	final int TBL_PRDCD = 1;
	final int TBL_UOMCD = 2;
	final int TBL_PRDDS = 3;
	final int TBL_INDQT = 4;
	final int TBL_DORQT = 5;
	final int TBL_INVQT = 6;
	final int TBL_FCMQT = 7;
	final int TBL_BALQT = 8;
	final int TBL_STKFL = 9;
	final String strTRNTP ="IN";

	mr_tefci()
	{
		super(2);
		try
		{
			setMatrix(20,8);		
			java.awt.Color colBLUE = new java.awt.Color(63,91,167);
			pnlINDTL = new JPanel(null);
		
			jtpINDTL=new JTabbedPane();
		
			add(new JLabel("Market Type"),1,1,1,1,this,'L');
			add(txtMKTTP=new TxtLimit(2),1,2,1,1,this,'L');
			add(txtMKTDS=new TxtLimit(40),1,3,1,1,this,'L');
			add(new JLabel("Indent Number"),1,4,1,1,this,'L');
			add(txtINDNO = new TxtLimit(8),1,5,1,1,this,'L');
			add(new JLabel("Amd. Number"),1,6,1,1,this,'L');
			add(txtAMDNO = new TxtLimit(2),1,7,1,1,this,'L');
			String[] L_strTB1HD = {" ","Prod.Code","UOM","Description","Aut.Qty","D.O.Qty","Inv.Qty","F.C.Qty","Bal qty"};
			int[] L_intCOLSZ = {20,75,35,338,55,55,55,55,55};
			//int[] L_intCOLSZ = {20,75,35,338,55,55,55,55,55,15};
			tblINDTL = crtTBLPNL1(pnlINDTL,L_strTB1HD,100,1,1,8,7.9,L_intCOLSZ,new int[]{0});
			tblINDTL.addMouseListener(this);
			add(pnlINDTL,2,1,9,8,this,'L');
			//add(jtpINDTL,12,1,6,8,this,'L');		

			pstmUPDREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"UPDATE MR_INTRN SET INT_FCMQT = isnull(INT_FCMQT,0) + ?"+
					    ",INT_TRNFL =?,INT_LUSBY=?,INT_LUPDT = ?,INT_STSFL='1'"+
						" WHERE INT_CMPCD = ? AND INT_MKTTP = ? AND INT_INDNO = ? AND INT_AMDNO = ? AND INT_PRDCD = ? AND isnull(INT_STSFL,'') <>'X'"); 
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mr_teindTBLINVFR();
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
		String L_strPRDCD ="",L_strINDQT ="0.00"	;
		String L_strDORQT ="0.00",L_strINVQT ="0.00",L_strFCMQT ="0.00",L_strBALQT ="0.00";
		String L_strDPTCD;
		java.sql.Date datTEMP;
		strINDDT ="";
		intROWCT =0;
		M_strSQLQRY = " Select INT_SBSCD,INT_MKTTP,INT_INDNO,INT_AMDNO,INT_PRDCD,INT_INDDT,"+
					  "INT_INDQT,INT_AUTBY,INT_INVQT,INT_DORQT,INT_FCMQT,(isnull(INT_INDQT,0) - isnull(INT_FCMQT,0)- isnull(INT_DORQT,0))L_BALQT,INT_STSFL,INT_PRDDS,INT_ORDUM from MR_INTRN ";
		M_strSQLQRY +="where INT_MKTTP ='"+strMKTTP+"' ";
		M_strSQLQRY +=" AND INT_INDNO ='"+txtINDNO.getText().trim()+"' and isnull(INT_INDQT,0) >0 and isnull(INT_STSFL,'')<>'X' and isnull(INT_INDQT,0)-isnull(INT_FCMQT,0)-isnull(INT_DORQT,0) >0";
		System.out.println(M_strSQLQRY);		
		clrCOMP();
		strINDST ="";
		this.setCursor(cl_dat.M_curWTSTS_pbst);		
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
			if(L_intROWCT == 0)
			{
				strINDST = nvlSTRVL(M_rstRSSET.getString("INT_STSFL"),""); 
				txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("INT_AMDNO"),""));
				setMSG("",'N');
				txtINDNO.setText(nvlSTRVL(M_rstRSSET.getString("INT_INDNO"),""));
				L_datTEMP = M_rstRSSET.getDate("INT_INDDT");
			}
			L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("INT_PRDCD"),"");
			L_strINDQT = nvlSTRVL(M_rstRSSET.getString("INT_INDQT"),"0.00");
			L_strDORQT = nvlSTRVL(M_rstRSSET.getString("INT_DORQT"),"0.00");
			L_strINVQT = nvlSTRVL(M_rstRSSET.getString("INT_INVQT"),"0.00");
			L_strFCMQT = nvlSTRVL(M_rstRSSET.getString("INT_FCMQT"),"0.00");
			L_strBALQT = nvlSTRVL(M_rstRSSET.getString("L_BALQT"),"0.00");
			tblINDTL.setValueAt(L_strPRDCD,L_intROWCT,TBL_PRDCD);
			tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("INT_PRDDS"),""),L_intROWCT,TBL_PRDDS);
			tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("INT_ORDUM"),""),L_intROWCT,TBL_UOMCD);
			tblINDTL.setValueAt(String.valueOf(L_strINDQT),L_intROWCT,TBL_INDQT);
			tblINDTL.setValueAt(String.valueOf(L_strDORQT),L_intROWCT,TBL_DORQT);
			tblINDTL.setValueAt(String.valueOf(L_strINVQT),L_intROWCT,TBL_INVQT);
			tblINDTL.setValueAt(String.valueOf(L_strFCMQT),L_intROWCT,TBL_FCMQT);
			tblINDTL.setValueAt(String.valueOf(L_strBALQT),L_intROWCT,TBL_BALQT);
			if(hstPENQT !=null)
				hstPENQT.put(L_strPRDCD,L_strBALQT);
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
		System.out.println("1");
		txtINDNO.setText("");
		System.out.println("2");
		txtAMDNO.setText("");
		System.out.println("3");
		hstITMVL.clear();
		System.out.println("4");
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
		{
			setENBL(false);
			setMSG("Please select an option ..",'N');
			System.out.println("5");
		}
		if(M_strSBSCD !=null)
		{
			M_strSBSCD = M_strSBSCD;
			strMKTTP = txtMKTTP.getText().trim();
		}
			setMSG("Press F1 to select the indent number..",'N');
			txtMKTTP.requestFocus();
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
			String L_ARRHDR[] = {"Indent number","Amd no.","Status"};
			M_strSQLQRY = "Select distinct INT_INDNO,INT_AMDNO,INT_STSFL from MR_INTRN ";
			M_strSQLQRY += " where isnull(INT_STSFL,'')<>'X' and isnull(INT_INDQT,0)-isnull(INT_FCMQT,0)-isnull(INT_DORQT,0) >0  and substring(INT_SBSCD,1,2)=substring('"+M_strSBSCD+"',1,2)";
			if(txtINDNO.getText().trim().length() >0)
				M_strSQLQRY += " and INT_INDNO like '" + txtINDNO.getText().trim() + "%'";
			M_strSQLQRY += " and INT_MKTTP ='"+txtMKTTP.getText().trim()+"'";
			M_strSQLQRY +=" Order by INT_INDNO ";
			System.out.println(M_strSQLQRY);
			cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
		}
	}
	if(L_KE.getKeyCode()==L_KE.VK_ENTER)
	{	
		if(M_objSOURC==txtMKTTP)
		{	
			strMKTTP = txtMKTTP.getText().trim();
			txtINDNO.requestFocus();
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
	txtMKTTP.setEnabled(true);
	txtINDNO.setEnabled(true);
	tblINDTL.cmpEDITR[0].setEnabled(true);
	tblINDTL.cmpEDITR[TBL_BALQT].setEnabled(true);
}
private class mr_teindTBLINVFR extends TableInputVerifier
{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID==TBL_BALQT)
				{
					strTEMP = tblINDTL.getValueAt(P_intROWID,TBL_BALQT).toString();
					if(Double.parseDouble(strTEMP)> (Double.parseDouble(hstPENQT.get(tblINDTL.getValueAt(P_intROWID,TBL_PRDCD).toString()).toString())))
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
				if(Double.parseDouble(strTEMP)> (Double.parseDouble(hstPENQT.get(tblINDTL.getValueAt(i,TBL_PRDCD).toString()).toString())))
				{
					setMSG("Forceful completion qty. can not be greater than pending Indent qty..",'E');
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
						pstmUPDREC.setString(5,cl_dat.M_strCMPCD_pbst);
						System.out.println("strMKTTP "+strMKTTP);
						pstmUPDREC.setString(6,strMKTTP);
						pstmUPDREC.setString(7,txtINDNO.getText().trim());
						pstmUPDREC.setString(8,txtAMDNO.getText().trim());
						pstmUPDREC.setString(9,tblINDTL.getValueAt(i,TBL_PRDCD).toString());
						pstmUPDREC.executeUpdate();
						System.out.println(pstmUPDREC);
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
