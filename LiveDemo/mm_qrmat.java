//Material Status Query 

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

class mm_qrmat extends cl_pbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;
	private JPanel pnlINDDT,pnlORDDT,pnlRECDT,pnlISSDT;
	private String strINDNO,strINDDT,strINDQT,strFCCQT,strAUTQT,strEXPDT,strREQDT,strSTSFL;
	private String strPORNO,strPORDT,strPORQT,strPORRT,strFRCQT,strCMPDT,strVENCD,strVENNM;
	private String strGRNNO,strGRNDT,strRECQT,strACPQT,strREJQT,strACPDT;
	private String strISSNO,strISSDT,strDPTCD,strDPTDS,strISSQT;
	private JLabel lblINDST;
	private JTextField txtMATCD,txtMATDS,txtUOMCD;
	private cl_JTable tblINDTB,tblPORTB,tblRECTB,tblISSTB;
	private String strMATCD,strMATDS,strUOM;
	private int intSRLNO = 1;
	mm_qrmat()
	{
		super(2);
		tbpMAIN = new JTabbedPane();
		pnlINDDT = new JPanel(null);
		pnlORDDT = new JPanel(null);
		pnlRECDT = new JPanel(null);
		pnlISSDT = new JPanel(null);
		lblINDST = new JLabel("Material Status");
		lblINDST.setForeground(new Color(40,95,240));
		txtMATCD = new TxtNumLimit(10.0);
		txtMATDS = new JTextField();
		txtUOMCD = new JTextField();
		setMatrix(20,8);
		add(lblINDST,1,1,1,1,this,'L');
		add(new JLabel("Material Code                :"),2,1,1,1.5,this,'L');
		add(txtMATCD,2,3,1,1.2,this,'L');
		add(new JLabel("Unit Of Measurment    :"),3,1,1,1.5,this,'L');
		add(txtUOMCD,3,3,1,1.2,this,'L');
		add(new JLabel("Material Desc.               :"),4,1,1,1.5,this,'L');
		add(txtMATDS,4,3,1,5,this,'L');
		
		tblINDTB = crtTBLPNL1(pnlINDDT,new String[]{"","Ind. No.","Ind Date","Ind Qty.","Aut.Qty","F.C.Qty","Exp. Date","Req. Date","Status"},500,2,1,9.1,7.9,new int[]{20,60,80,80,80,80,80,60,280},new int[]{0});
		tblPORTB = crtTBLPNL1(pnlORDDT,new String[]{"","P. O. No.","P. O. Date","P. O. Qty.","P.O. Rate","F.C.Qty","Comp. Date","Ven. Code","Ven. Name"},500,2,1,9.1,7.9,new int[]{20,65,70,70,70,95,90,90,300},new int[]{0});
		tblRECTB = crtTBLPNL1(pnlRECDT,new String[]{"","GRIN No.","GRIN Date","Rec. Qty.","Acp. Qty.","Rej.Qty","Acp. Date","Status"},500,2,1,9.1,7.9,new int[]{20,65,90,90,90,90,70,180},new int[]{0});
		tblISSTB = crtTBLPNL1(pnlISSDT,new String[]{"","Issue No.","Issue Date","Dept. Code","Department Name","Issue Qty."},500,2,1,9.1,7.9,new int[]{20,65,90,100,320,100},new int[]{0});
		add(tbpMAIN,6,1,13,8,this,'L');
		
		tbpMAIN.addTab("Indent Detail",pnlINDDT);
		tbpMAIN.addTab("Order Detail",pnlORDDT);
		tbpMAIN.addTab("Receipt Detail",pnlRECDT);
		tbpMAIN.addTab("Issue Detail",pnlISSDT);
		
		tblINDTB.addMouseListener(this);
		tblPORTB.addMouseListener(this);
		tblRECTB.addMouseListener(this);
		tblISSTB.addMouseListener(this);
		
		//txtMATCD.setInputVerifier(new INPVF());
		
		//cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		//cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				txtMATCD.setEnabled(true);
				txtMATCD.requestFocus();
				setMSG("Please Enter Material Code Or Press F1",'N');
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				setENBL(false);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				setENBL(false);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				setENBL(false);
			}
		}
		if(M_objSOURC == txtMATCD)
		{
			if(txtMATCD.getText().trim().length() != 0)
			{
				String strTEMP = txtMATCD.getText().trim();
				tblINDTB.clrTABLE();
				tblPORTB.clrTABLE();
				tblRECTB.clrTABLE();
				tblISSTB.clrTABLE();
				getRECRD();
				setENBL(false);
				txtMATCD.setEnabled(true);
			}
			else{
				setMSG("Enter Material Code Or Press F1 For Help",'E');
			}
		}
		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			
			if(M_objSOURC == txtMATCD){
				tblINDTB.clrTABLE();
				tblPORTB.clrTABLE();
				tblRECTB.clrTABLE();
				tblISSTB.clrTABLE();
				M_strHLPFLD = "txtMATCD";
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtMATCD.getText().trim().length() >0)		 
					M_strSQLQRY +=	"AND ST_MATCD LIKE '"+txtMATCD.getText().trim()+"%'";
				M_strSQLQRY +=" ORDER BY ST_MATCD ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Material Description"},2,"CT",new int[]{100,400});
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F2)
		{
			if(M_objSOURC == txtMATCD){
				tblINDTB.clrTABLE();
				tblPORTB.clrTABLE();
				tblRECTB.clrTABLE();
				tblISSTB.clrTABLE();
				M_strHLPFLD = "txtMATCD";
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtMATCD.getText().trim().length() >0)		 
					M_strSQLQRY +=	"AND ST_MATCD LIKE '"+txtMATCD.getText().trim()+"%'";
				M_strSQLQRY +=" ORDER BY ST_MATDS ";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Material Code","Material Description"},2,"CT",new int[]{100,400});
			}
		}

	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtMATCD"))
		{
			txtMATCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtMATCD.requestFocus();
			getITMDTL();	
		}
	}
	private boolean getITMDTL()
	{
		try
		{
			txtMATDS.setText("");
			txtUOMCD.setText("");
			M_strSQLQRY = "SELECT ST_MATDS,ST_UOMCD FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
			M_strSQLQRY +=" AND ST_MATCD = '"+txtMATCD.getText()+"' "
				+"AND isnull(ST_STSFL, ' ')<>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					txtMATDS.setText(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""));
					txtUOMCD.setText(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""));
					setMSG("Press Enter To View The Details ",'N');
					return true;
				}
				else
				{
					setMSG("Invalid Item Code..",'E');
					return false;
				}
			}
		}
		catch(SQLException L_SQLE)
		{
			setMSG(L_SQLE,"exeHLPOK");				
			return false;
		}
		return true;
	}
	//Method for Get Data
	void getRECRD()
	{
		java.sql.Date datTEMP;
		
		try
		{
			
		//	M_strSQLQRY ="Select * from co_cdtrn where cmt_cgmtp ='SYS' and cmt_cgstp ='COXXDPT' 
			if(tblINDTB.isEditing())
				tblINDTB.getCellEditor().stopCellEditing();
			tblINDTB.setColumnSelectionInterval(0,0);
			tblINDTB.setRowSelectionInterval(0,0);
			if(tblPORTB.isEditing())
				tblPORTB.getCellEditor().stopCellEditing();
			tblPORTB.setColumnSelectionInterval(0,0);
			tblPORTB.setRowSelectionInterval(0,0);
			if(tblRECTB.isEditing())
				tblRECTB.getCellEditor().stopCellEditing();
			tblRECTB.setColumnSelectionInterval(0,0);
			tblRECTB.setRowSelectionInterval(0,0);
			if(tblISSTB.isEditing())
				tblISSTB.getCellEditor().stopCellEditing();
			tblISSTB.setColumnSelectionInterval(0,0);
			tblISSTB.setRowSelectionInterval(0,0);
			setCursor(cl_dat.M_curWTSTS_pbst);
			//	,tblPORTB,tblRECTB,tblISSTB
		//	setCursor(cl_dat.M_curWTSTS_pbst);
			if(getITMDTL())
			{
				M_strSQLQRY = "SELECT IN_INDNO,IN_INDDT,IN_INDQT,IN_AUTQT,IN_FCCQT,IN_EXPDT,IN_REQDT,IN_STSFL,CMT_CODDS FROM MM_INMST,CO_CDTRN "
					+"WHERE IN_STSFL = CMT_CODCD AND CMT_CGMTP ='STS' AND CMT_CGSTP ='MMXXIND' AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MATCD = '"+txtMATCD.getText()+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,' ')<>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				int i = 0;
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						strINDNO = nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),"");
									
						datTEMP = M_rstRSSET.getDate("IN_INDDT");
						if(datTEMP != null)
							strINDDT = M_fmtLCDAT.format(datTEMP);
											
						strINDQT = nvlSTRVL(M_rstRSSET.getString("IN_INDQT"),"0.00");
						strAUTQT = nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.00");
						strFCCQT = nvlSTRVL(M_rstRSSET.getString("IN_FCCQT"),"0.00");
						datTEMP = M_rstRSSET.getDate("IN_EXPDT");
						if(datTEMP != null)
							strEXPDT = M_fmtLCDAT.format(datTEMP);
						
						datTEMP = M_rstRSSET.getDate("IN_REQDT");
						if(datTEMP != null)
							strREQDT = M_fmtLCDAT.format(datTEMP);
						
						strSTSFL = nvlSTRVL(M_rstRSSET.getString("IN_STSFL"),"");
						strSTSFL = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						tblINDTB.setValueAt(strINDNO,i,1);
						tblINDTB.setValueAt(strINDDT,i,2);
						tblINDTB.setValueAt(strINDQT,i,3);
						tblINDTB.setValueAt(strAUTQT,i,4);
						tblINDTB.setValueAt(strFCCQT,i,5);
						tblINDTB.setValueAt(strEXPDT,i,6);
						tblINDTB.setValueAt(strREQDT,i,7);
						tblINDTB.setValueAt(strSTSFL,i,8);
						
						i++;
					}
					M_rstRSSET.close();
				}
				M_strSQLQRY = "SELECT PO_PORNO,PO_PORQT,PO_PORRT,PO_FRCQT, PO_PORDT, PO_CMPDT,PO_VENCD,PT_PRTNM FROM "
					+"MM_POMST,CO_PTMST WHERE "
					+" PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MATCD = '"+txtMATCD.getText()+"' AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
					+"AND PT_PRTTP ='S' AND PT_PRTCD = PO_VENCD and isnull(PO_STSFL,' ') <>'X' AND "
					+"isnull(PT_STSFL,' ') <>'X' order by po_pordt desc";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				i = 0;
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						strPORNO = nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),"");
						
						datTEMP = M_rstRSSET.getDate("PO_PORDT");
						if(datTEMP != null)
							strPORDT = M_fmtLCDAT.format(datTEMP);
						
						strPORQT = nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0");
						strPORRT = nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),"0");
						strFRCQT = nvlSTRVL(M_rstRSSET.getString("PO_FRCQT"),"0");
				
						datTEMP = M_rstRSSET.getDate("PO_CMPDT");
						if(datTEMP != null)
							strCMPDT = M_fmtLCDAT.format(datTEMP);
						
						strVENCD = nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),"");
						
						strVENNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
						
						tblPORTB.setValueAt(strPORNO,i,1);
						tblPORTB.setValueAt(strPORDT,i,2);
						tblPORTB.setValueAt(strPORQT,i,3);
						tblPORTB.setValueAt(strPORRT,i,4);
						tblPORTB.setValueAt(strFRCQT,i,5);
						tblPORTB.setValueAt(strCMPDT,i,6);
						tblPORTB.setValueAt(strVENCD,i,7);
						tblPORTB.setValueAt(strVENNM,i,8);
						i++;
					}
					M_rstRSSET.close();
				}
				M_strSQLQRY ="SELECT GR_GRNNO,GR_GRNDT,GR_RECQT,GR_ACPQT,GR_REJQT,GR_ACPDT,GR_STSFL   "
					+"FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' "+" AND GR_MATCD = '"+txtMATCD.getText()+"'"
					+"AND isnull(GR_STSFL,'') <>'X' order by gr_grndt desc";
					
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				i = 0;
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
						datTEMP = M_rstRSSET.getDate("GR_GRNDT");
						if(datTEMP != null)
							strGRNDT = M_fmtLCDAT.format(datTEMP);
						
						strRECQT = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0");
						
						strACPQT = nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0");
						strREJQT = nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0");
						datTEMP = M_rstRSSET.getDate("GR_ACPDT");
						if(datTEMP != null)
							strACPDT = M_fmtLCDAT.format(datTEMP);
						
						strSTSFL = nvlSTRVL(M_rstRSSET.getString("GR_STSFL"),"");
						
						tblRECTB.setValueAt(strGRNNO,i,1);
						tblRECTB.setValueAt(strGRNDT,i,2);
						tblRECTB.setValueAt(strRECQT,i,3);
						tblRECTB.setValueAt(strACPQT,i,4);
						tblRECTB.setValueAt(strREJQT,i,5);
						tblRECTB.setValueAt(strACPDT,i,6);
						if(strSTSFL.equals("0"))
							strSTSFL ="Not Accepted";
						else if(strSTSFL.equals("2"))
							strSTSFL ="Accepted";
						tblRECTB.setValueAt(strSTSFL,i,7);
						i++;
					}
					M_rstRSSET.close();
				}
				M_strSQLQRY = "SELECT IS_ISSNO,IS_ISSDT,IS_DPTCD,IS_ISSQT,CMT_CODDS "
					+"FROM CO_CDTRN,MM_ISMST WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='COXXDPT' "
					+"AND CMT_CODCD = IS_DPTCD AND "
					+"IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MATCD = '"+txtMATCD.getText()+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"isnull(IS_STSFL, ' ') <> 'X' AND isnull(CMT_STSFL, ' ') <> 'X'";
				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				i = 0;
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						strISSNO = nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),"");
						
						datTEMP = M_rstRSSET.getDate("IS_ISSDT");
						if(datTEMP != null)
							strISSDT = M_fmtLCDAT.format(datTEMP);
						
						strDPTCD = nvlSTRVL(M_rstRSSET.getString("IS_DPTCD"),"");
						
						strISSQT = nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),"0");
						
						strDPTDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						
						tblISSTB.setValueAt(strISSNO,i,1);
						tblISSTB.setValueAt(strISSDT,i,2);
						tblISSTB.setValueAt(strDPTCD,i,3);
						tblISSTB.setValueAt(strDPTDS,i,4);
						tblISSTB.setValueAt(strISSQT,i,5);
						i++;
					}
					M_rstRSSET.close();
				}
			}
			else
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(SQLException L_SQLE)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_SQLE,"getRECRD");
		}
	}
	/*
	private class INPVF extends InputVerifier
	{
		//String stSTRTP = M_strSBSCD.substring(2,4);
		public boolean verify(JComponent input)
		{
			try
			{
				if(input == txtMATCD)
				{
					if(((JTextField)input).getText().trim().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select count(*) from MM_STMST where ST_MATCD = '"+((JTextField)input).getText()+"' ");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)== 0)
							{
								setMSG("Invalid Material Code ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Material Code ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Material Code ",'E');
						return false;
					}
				}
				return true;
			}
			catch(SQLException L_SQL)
			{
				setMSG(L_SQL,"verify");
				return false;
			}
		}
	}
*/	
}	
