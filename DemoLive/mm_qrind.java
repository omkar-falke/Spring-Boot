// Indendt Status Query 
/*
System Name   : Material Management System
Program Name  : Indent Status Query For Given Material  
Program Desc. : User Enter the Material Code And Get List Or P. O. For That Material code 
Author        : A.T.Chaudhari
Date          : 05/04/2004
Version       : MMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.Date;
import java.sql.SQLException;
import java.io.*;
import java.awt.Color;
import java.sql.ResultSet;

class mm_qrind extends cl_pbase implements MouseListener {
	private JTabbedPane tbpMAIN;
	private JPanel pnlPORDT,pnlGRNDT;
	//private String strSTRTP = "01";
	private JLabel lblTBLNM,lblINDST;	
	private JTextField txtDPTCD,txtDPTNM,txtINDNO,txtINDDT,txtAUTBY;
	private cl_JTable tblINDTL,tblPORTB,tblGRNTB;
	private String strDPTCD,strDPTNM,strINDNO,strPOSTS;
	private int intSRLNO = 1;
	
	final int TBL_CHKFL = 0;
	final int TBL_SRLNO = 1;
	final int TBL_MATCD = 2;
	final int TBL_MATDS = 3;
	final int TBL_UOMCD = 4;
	final int TBL_AUTQT = 5;
	final int TBL_FCCQT = 6;
	final int TBL_REQDT = 7;
	final int TBL_STSFL = 8;
		
	mm_qrind(){
		super(2);
		
		tbpMAIN = new JTabbedPane();
		pnlPORDT = new JPanel(null);
		pnlGRNDT = new JPanel(null);
		lblTBLNM = new JLabel("");
		lblINDST = new JLabel("Indent Status");
		lblTBLNM.setForeground(Color.blue);
		lblINDST.setForeground(Color.blue);
		txtDPTNM = new JTextField();
		txtINDDT = new JTextField();
		txtAUTBY = new JTextField();
		
		setMatrix(20,8);
		add(lblINDST,1,1,1,1,this,'L');
		
		add(new JLabel("Departent :"),2,1,1,1,this,'L');
		add(txtDPTCD = new TxtNumLimit(3.0),2,2,1,1.2,this,'L');
		add(new JLabel("Indent No  :",RIGHT),2,3,1,1,this,'L');
		add(txtINDNO = new TxtNumLimit(8.0),2,4,1,1.2,this,'L');
		add(new JLabel("Indent Dt. :",RIGHT),2,5,1,1,this,'L');
		add(txtINDDT,2,6,1,1.2,this,'L');
		add(new JLabel("Auth. By :",RIGHT),2,7,1,1,this,'L');
		add(txtAUTBY,2,8,1,1,this,'L');
		add(new JLabel("Dept. Name : "),3,1,1,1,this,'L');
		add(txtDPTNM,3,2,1,3.2,this,'L');
		
		tblINDTL = crtTBLPNL1(this,new String[]{"","Sr.No.","Mat. Code.","Material Description","UOM","Auth. Qty.","FC Qty","Req. Dt.","Ind Status"},40,4,1,6,8,new int[]{15,20,75,330,30,60,60,70,90},new int[]{0});
		tblPORTB = crtTBLPNL1(pnlPORDT,new String[]{"P.O.No.","P.O.Dt.","P.o.Qty.","Acp.Aty.","Cmp.Dt.","Ven.Cd.","Ven.Name","P.O.Sts"},40,1,1,6,7.93,new int[]{80,80,80,80,80,80,180,80},new int[]{});
		tblGRNTB = crtTBLPNL1(pnlGRNDT,new String[]{"GRIN No.","GRIN Dt.","Rec.Qty.","Acp.Dt.","Acp.Qt.","Gate In No.","Ven.Cd.","Ven.Name"},40,1,1,6,7.93,new int[]{80,80,80,80,80,80,80,180},new int[]{});
		add(tbpMAIN,11,1,8,8,this,'L');
		tbpMAIN.addTab("Order Detail",pnlPORDT);
		tbpMAIN.addTab("Receipt Detail",pnlGRNDT);
		
		tblINDTL.addMouseListener(this);
		
		txtDPTCD.setInputVerifier(new INPVF());
		txtINDNO.setInputVerifier(new INPVF());
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst){
			clrCOMP();
			setENBL(false);
			txtDPTCD.setEnabled(true);
			txtINDNO.setEnabled(true);
			tblINDTL.cmpEDITR[0].setEnabled(true);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)){
				txtDPTCD.setEnabled(true);
				txtINDNO.setEnabled(true);
				tblINDTL.cmpEDITR[0].setEnabled(true);
				txtDPTCD.requestFocus();
			}
		}
	}
	public void mouseClicked(MouseEvent L_ME){
		if(M_objSOURC == tblINDTL){
			int L_intSELROW = tblINDTL.getSelectedRow();
			if(tblINDTL.getSelectedColumn()== TBL_CHKFL)
			if((tblINDTL.getValueAt(L_intSELROW,0).toString()).equals("false"))
			{				
				tblPORTB.clrTABLE();
				tblGRNTB.clrTABLE();
			}
		}
	}
	
	public void mouseReleased(MouseEvent L_ME){
		super.mouseReleased(L_ME);
		if((M_objSOURC == tblINDTL)&&(tblINDTL.getSelectedColumn() == TBL_CHKFL))
		{
			int L_intSELROW = tblINDTL.getSelectedRow();
			for(int i=0;i<tblINDTL.getRowCount();i++){
				if(i != L_intSELROW)
					tblINDTL.setValueAt(new Boolean(false),i,0);
		}
			String L_strPINDN = txtINDNO.getText().toString();
			String L_strPMACD = tblINDTL.getValueAt(L_intSELROW,2).toString();
			try{
				tblPORTB.clrTABLE();
				if(tblPORTB.isEditing())
					tblPORTB.getCellEditor().stopCellEditing();
				tblPORTB.setColumnSelectionInterval(0,0);
				tblPORTB.setRowSelectionInterval(0,0);
		
				java.sql.Date datTEMP;
				M_strSQLQRY = "SELECT PO_PORNO,PO_PORDT,PO_CMPDT,PO_VENCD,PO_STSFL,PO_PORQT,PO_ACPQT,PT_PRTNM "
					+"FROM MM_POMST,CO_PTMST "
					+"WHERE PT_PRTTP = 'S' AND PT_PRTCD = PO_VENCD AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(po_stsfl,'') <>'X' and isnull(pt_stsfl,'') <>'X' AND " 
					+"PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND  "
					+"PO_MATCD = '"+L_strPMACD+"' AND PO_INDNO = '"+L_strPINDN+"' ";
					
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				int i = 0;
				if(M_rstRSSET != null){
					while(M_rstRSSET.next()){
						tblPORTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),i,0);
						datTEMP = M_rstRSSET.getDate("PO_PORDT");
						if(datTEMP != null)
							tblPORTB.setValueAt(M_fmtLCDAT.format(datTEMP),i,1);
						tblPORTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),""),i,2);
						tblPORTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_ACPQT"),""),i,3);
						datTEMP = M_rstRSSET.getDate("PO_CMPDT");
						if(datTEMP != null)
							tblPORTB.setValueAt(M_fmtLCDAT.format(datTEMP),i,4);
						tblPORTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),""),i,5);
						tblPORTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),i,6);
						//strTEMP = nvlSTRVL(M_rstRSSET.getString("PO_STSFL"),"");
						/*if(strTEMP.equals("A"))
							strTEMP = "Authorised";
						else if(strTEMP.equals("A"))
							strTEMP = "Ordered";
						else if (strTEMP.equals("H"))
							strTEMP = "Partial Completed";
						else if (strTEMP.equals("C"))
							strTEMP = "Completed";*/
						tblPORTB.setValueAt(cl_dat.getPRMCOD("CMT_CODDS","STS","MMXXPOR",nvlSTRVL(M_rstRSSET.getString("PO_STSFL"),"")),i,7);
						i++;
					}
				}
				if(i==0)
				{
					setMSG("P.O. Details not found..",'E');
					return;
				}
				M_rstRSSET.close();
				tblGRNTB.clrTABLE();
				M_strSQLQRY = "SELECT GR_GINNO,GR_GRNNO,GR_GRNDT,GR_RECQT,GR_ACPDT,GR_ACPQT,GR_VENCD,GR_VENNM "
					+"FROM MM_GRMST,MM_POMST "
					+"WHERE  GR_STRTP = PO_STRTP AND GR_PORNO = PO_PORNO AND GR_MATCD =PO_MATCD AND "
					+"PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_INDNO = '"+txtINDNO.getText().toString()+"' AND "
					+"PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' and isnull(GR_STSFL,' ') <>'X' and isnull(PO_STSFL,' ') <>'X'";
	
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				i = 0;									
				if(M_rstRSSET != null){
					while(M_rstRSSET.next()){
						tblGRNTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),i,0);
						datTEMP = M_rstRSSET.getDate("GR_GRNDT");
						if(datTEMP != null)
							tblGRNTB.setValueAt(M_fmtLCDAT.format(datTEMP),i,1);
						tblGRNTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),""),i,2);
						datTEMP = M_rstRSSET.getDate("GR_ACPDT");
						if(datTEMP != null)
							tblGRNTB.setValueAt(M_fmtLCDAT.format(datTEMP),i,3);
						tblGRNTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),""),i,4);
						tblGRNTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),""),i,5);
						tblGRNTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""),i,6);
						tblGRNTB.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),i,7);
					}
				}
				M_rstRSSET.close();
			}
			catch(SQLException L_SQLE){
				setMSG(L_SQLE,"PO DETAILS ");
			}
		}

	}
	public void keyPressed(KeyEvent L_KE){
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1){
			intSRLNO = 1;
			if(M_objSOURC == txtDPTCD){
				clrCOMP();
				M_strSQLQRY = "SELECT  DISTINCT CMT_CODDS,CMT_CODCD FROM CO_CDTRN "
					+"WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='COXXDPT' and isnull(CMT_STSFL,' ') <> 'X' ORDER BY CMT_CODCD";
				cl_hlp(M_strSQLQRY,1,2,new String[]{"Department Name","Department Code"},2,"CT");
				M_strHLPFLD = "txtDPTCD";
			}
			if(M_objSOURC == txtINDNO){
				strDPTCD = txtDPTCD.getText().toString();
				M_strSQLQRY = "SELECT distinct IN_INDNO FROM MM_INMST WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' and isnull(in_stsfl,'') <>'X' ";
				if(txtDPTCD.getText().trim().length() >0)
					M_strSQLQRY += " AND IN_DPTCD like '"+strDPTCD+"%' ";
				if(txtINDNO.getText().trim().length() >0)
					M_strSQLQRY += " AND IN_INDNO LIKE '"+txtINDNO.getText().toString()+"%'";
				M_strSQLQRY +=" ORDER BY IN_INDNO DESC";
				System.out.println(M_strSQLQRY);
                cl_hlp(M_strSQLQRY,1,1,new String[]{"INDENT NUMBER"},1,"CT");
				M_strHLPFLD = "txtINDNO";
                
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER){
			try
			{
			if(M_objSOURC == txtDPTCD){
				tblINDTL.clrTABLE();
			//	if(txtDPTCD.getText().toString().length() != 0)
					txtINDNO.requestFocus();
			//	else
			//		setMSG("Please Enter Department Code Or Press F1 For Help ",'E');
			}
			if(M_objSOURC == txtINDNO){
			////
					if(txtINDNO.getText().trim().length() < 8)
					{
						setMSG("Please enter 8 digits of indent no, F1 to select from the list..",'E');
						tblINDTL.clrTABLE();
						tblPORTB.clrTABLE();
						tblGRNTB.clrTABLE();
					}
					else
					{
						String L_strSQLQRY = "Select count(*) from MM_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDNO = '"+txtINDNO.getText().trim()+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,'') <>'X'";
						if(txtDPTCD.getText().trim().length() >0)
						L_strSQLQRY +=" AND IN_DPTCD = '"+strDPTCD+"'";
						ResultSet L_rstTEMP = cl_dat.exeSQLQRY3(L_strSQLQRY);
						if(L_rstTEMP != null)
						{
							if(L_rstTEMP.next())
							{
								tblINDTL.clrTABLE();
								tblPORTB.clrTABLE();
								tblGRNTB.clrTABLE();
								if(L_rstTEMP.getInt(1)<= 0)
								{
									setMSG("Invalid Indent No.. ",'E');
								}
								else
									getINDDT();
							}
							else
							setMSG("Invalid Indent No, Press F1 For Help ",'E');
						}
					}
			}
		}
			catch(SQLException L_E)
			{
				setMSG(L_E,"VK_ENTER");
			}
		}
	}
	public void exeHLPOK(){
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtDPTCD")){
			txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtDPTCD.requestFocus();
		}
		if(M_strHLPFLD.equals("txtINDNO")){
			txtINDNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtINDNO.requestFocus();
		}
	}
	boolean getINDDT(){
		java.sql.Date datTEMP;
		strDPTCD = txtDPTCD.getText().trim();
		strINDNO = txtINDNO.getText().trim();
		try{
			if(tblINDTL.isEditing())
				tblINDTL.getCellEditor().stopCellEditing();
			tblINDTL.setColumnSelectionInterval(0,0);
			tblINDTL.setRowSelectionInterval(0,0);
			M_strSQLQRY = "SELECT IN_INDDT, IN_AUTBY,IN_MATCD,IN_AUTQT,IN_FCCQT,IN_REQDT,IN_STSFL, "
				+"CT_MATDS,CT_UOMCD,CMT_CODDS "
				+"FROM MM_INMST,CO_CTMST,CO_CDTRN WHERE CMT_CGMTP ='STS' AND CMT_CGSTP ='MMXXIND' AND CMT_CODCD = IN_STSFL AND CT_MATCD = IN_MATCD AND "
				+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND IN_INDNO = '"+strINDNO+"' AND isnull(IN_STSFL,'') <>'X'";
			if(txtDPTCD.getText().trim().length() >0)
			M_strSQLQRY += " AND IN_DPTCD = '"+strDPTCD+"'";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null){
				int i = 0;
				while(M_rstRSSET.next()){
					
					datTEMP = M_rstRSSET.getDate("IN_INDDT");
					if(datTEMP != null)
						txtINDDT.setText(M_fmtLCDAT.format(datTEMP));
					txtAUTBY.setText(M_rstRSSET.getString("IN_AUTBY"));
					
					tblINDTL.setValueAt(String.valueOf(intSRLNO),i,TBL_SRLNO);
					tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),""),i,TBL_MATCD);
					tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),i,TBL_MATDS);
					tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),i,TBL_UOMCD);
					tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),""),i,TBL_AUTQT);
					tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_FCCQT"),""),i,TBL_FCCQT);
					
					datTEMP = M_rstRSSET.getDate("IN_REQDT");
					if(datTEMP != null)
						tblINDTL.setValueAt(M_fmtLCDAT.format(datTEMP),i,TBL_REQDT);
					tblINDTL.setValueAt(M_rstRSSET.getString("CMT_CODDS").toString(),i,TBL_STSFL);
					intSRLNO += 1;
					i++;
				}
				setMSG("Click On Check Box To View Details ",'N');
				M_rstRSSET.close();
				intSRLNO = 1;
			}
		}
		catch(SQLException L_SQLE){
			
			setMSG(L_SQLE,"Indent detials");
			return false;
		}
		return true;
	}
	
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(input == txtDPTCD )
				{
					if(txtDPTCD.getText().trim().length() ==0)
					{
						txtDPTNM.setText("");
						return true;
					}
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select CMT_CODDS from CO_CDTRN where CMT_CODCD = '"+txtDPTCD.getText().trim()+"' AND CMT_CGMTP='SYS' AND CMT_CGSTP='COXXDPT' and isnull(cmt_stsfl,' ') <>'X' and cmt_codcd ='"+txtDPTCD.getText().trim() +"'");
					if(L_rstTEMP == null)
					{
						setMSG("Invalid Department Code..",'E');
						tblINDTL.clrTABLE();
						tblPORTB.clrTABLE();
						tblGRNTB.clrTABLE();
						return false;
					}
					if(L_rstTEMP.next())
					{
						txtDPTNM.setText(nvlSTRVL(L_rstTEMP.getString("CMT_CODDS"),""));
					}
					else
					{
						setMSG("Invalid Department Code.. ",'E');
						return false;
					}
				}
				/*else if(input == txtINDNO)
				{
					if(txtINDNO.getText().trim().length() < 8)
					{
						setMSG("Please enter 8 digits of indent no, F1 to select from the list..",'E');
						return false;
					}
					setMSG("",'N');
					String L_strSQLQRY = "Select count(*) from MM_INMST where IN_INDNO = '"+txtINDNO.getText().trim()+"' AND IN_STRTP = '"+strSTRTP+"' AND IN_DPTCD = '"+strDPTCD+"'";
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3(L_strSQLQRY);
					if(L_rstTEMP != null)
					{
						System.out.println("not null");
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								System.out.println("invalid");
								setMSG("Invalid Indent No.. ",'E');
								return false;
							}
							tblINDTL.clrTABLE();
							return getINDDT();
						}
						else
						{
							System.out.println("not next");
							setMSG("Invalid Indent No.. ",'E');
							return false;
						}
			
					}
					else
					{
						setMSG("Invalid Indent No.. ",'E');
						return false;
					}
				}*/
				return true;
			}
			catch(SQLException L_SQL)
			{
				setMSG(L_SQL,"verify");
				return false;
			}
		}
		
	}
	
}	

