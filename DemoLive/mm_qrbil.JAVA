//Purchase Order Query
/*
System Name   : Material Management System
Program Name  : Bill Passing Query 
Program Desc. : User Enter the Vendor Code For Detail of Each Bill 
Author        : A.T.Chaudhari
Date          : 23/09/2004
Version       : MMS 2.0
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.SQLException;
import java.awt.Color;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.table.*; 
import javax.swing.table.DefaultTableCellRenderer;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR> <TR><TD>Program Name</TD><TD>Bill Derail Query</TD></TR> <TR><TD>Program Desc</TD><TD>Enter Bill Type And Vendor Code If Required                                              Enter P.O. Number For       Specific P.O. And View Details For Bill </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_qrbil.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\mm_qrbil.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>27/09/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD>							<TD><P align=center>Mod</P>								  </TD>	<TD><P align=center>Del</P></TD>			<TD><P align=center>Enq</P></TD></TR> <TR><TD>CO_CDTRN</TD><TD>CMT_CGMTP,CMT_CGSTP,CMT_CODCD					</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_BLMST</TD><TD>BL_BILTP,BL_DOCNO			</TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_BILTR</TD><TD>BIL_BILTP,BIL_DOCNO,BIL_DOCRF,BIL_MATCD	</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>CO_PTMST</TD><TD>PT_PRTTP,PT_PRTCD								</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class mm_qrbil extends cl_pbase
{									/**JCombo Box For BilType*/
	private JComboBox cmbBILTP;		/**JTextField For Party Code*/
	private JTextField txtPRTCD;	/**JTextField For Party Name*/
	private JTextField txtPRTNM;	/**JTextField for P.O. Number*/
	private JTextField txtPORNO;	/**Label For Bill Details*/
	private JLabel lblBILDT;		/**Lebel For GRIN Details*/
	private JLabel lblGRNDT;		/**Table For Bill Details*/
	private cl_JTable tblBILDT;		/**Table For GRIN Details*/
	private cl_JTable tblGRNDT;		/**Vector For Document Number*/
	private Vector<String> vtrDOCNO;
	private boolean blnFIRST;
	private Hashtable<String,String> hstPRTCD;
	/**Constructor For Bill Query Add Bill Type To Bill Type Combo Box
	 */
	public mm_qrbil()
	{
		super(2);
		try
		{
			lblBILDT = new JLabel("Bill Detail");
			lblGRNDT = new JLabel("GRIN Detail");
			lblBILDT.setForeground(Color.blue);
			lblGRNDT.setForeground(Color.blue);
		
			cmbBILTP = new JComboBox();
			//Add the Items to combo box
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'MMXXBTP' AND isnull(CMT_STSFL,' ') <>'X' AND CMT_CODCD IN "
				+"('01','03') ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				cmbBILTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
			}
			M_rstRSSET.close();
			
			//method to add party code as key and party name as vale in the hash tabel hstprtcd
			hstPRTCD = new Hashtable<String,String>(10,0.8f);
			M_strSQLQRY = "SELECT DISTINCT BL_PRTCD,PT_PRTNM FROM MM_BLMST,CO_PTMST "
					+"WHERE PT_PRTCD = BL_PRTCD AND BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP IN ('01','03') ORDER BY BL_PRTCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				hstPRTCD.put(M_rstRSSET.getString("BL_PRTCD"),M_rstRSSET.getString("PT_PRTNM"));
			}
			M_rstRSSET.close();
		
			setMatrix(20,8);
		
			add(new JLabel("Bill Type"),2,1,1,1,this,'L');
			add(cmbBILTP,2,2,1,2,this,'L');
			add(new JLabel("Ven. Code"),3,1,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(5),3,2,1,1,this,'L');
			add(txtPRTNM = new JTextField(),3,3,1,3,this,'L');
			add(new JLabel("P. O. Number"),4,1,1,1,this,'L');
			add(txtPORNO = new TxtNumLimit(8.0),4,2,1,1,this,'L');
			add(lblBILDT,5,1,1,1,this,'L');
			tblBILDT = crtTBLPNL1(this,new String[]{"","Bill No","Bill Date","Bill Passing Date","Bill Amount","Calculated Amt"},200,6,1,6,5.7,new int[]{20,100,100,100,100,100,100},new int[]{0});
			add(lblGRNDT,12,1,1,1,this,'L');
			tblGRNDT = crtTBLPNL1(this,new String[]{"","GRIN No","Mat. Cd.","Chal Qty.","Rec. Qty","Accp. Qty","Bill Pass Qty","P.O. No","P.O. Val",},100,13,1,6,7.93,new int[]{20,90,90,90,90,90,90,90,90,90},new int[]{0});
			tblBILDT.addMouseListener(this);
			DefaultTableCellRenderer tcrTEMP=new DefaultTableCellRenderer();
			tcrTEMP.setHorizontalAlignment(JTextField.LEFT);
			tcrTEMP.setForeground(Color.red);
			tblBILDT.setDefaultRenderer(tblBILDT.getColumnClass(1),null);
			tblBILDT.setDefaultRenderer(tblBILDT.getColumnClass(1),tcrTEMP);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Method To Set Enabled Component */
	public void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(P_flgSTATE);
		cmbBILTP.setEnabled(true);
		txtPRTCD.setEnabled(true);
		txtPORNO.setEnabled(true);
		txtPRTNM.setEnabled(false);
		tblBILDT.setEnabled(false);
		tblBILDT.cmpEDITR[0].setEnabled(true);
		tblGRNDT.setEnabled(false);
	}
	/** Method on Action Performed On Event 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			tblBILDT.clrTABLE();
			tblGRNDT.clrTABLE();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 4)
			{
				cmbBILTP.requestFocus();
			}
		}
		if(M_objSOURC == txtPRTCD)
		{
			txtPRTCD.setText(txtPRTCD.getText().toUpperCase());
		}
		if(M_objSOURC == cmbBILTP)
		{
			txtPRTCD.setText("");
			txtPRTNM.setText("");
			txtPORNO.setText("");
			tblBILDT.clrTABLE();
			tblGRNDT.clrTABLE();
		}
	}
	/** Method to Display Messager When Forcus On Pertucular Component
	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == cmbBILTP)
			setMSG("Select Bill Type .. ",'N');
		if(M_objSOURC == txtPRTCD)
			setMSG("Enter Party Code Or Press F1 For Help .. ",'N');
		if(M_objSOURC == txtPORNO)
			setMSG("Enter P.O. No Or Press 'F1' For Help Or Blank For All P.O. ..",'N');
	}
	/** Method on Key Pressed (F1, Enter)
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		/** Display Help Window For The input value in text field
		 */
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD = "txtPRTCD";
				M_strSQLQRY = "SELECT DISTINCT BL_PRTCD,PT_PRTNM FROM MM_BLMST,CO_PTMST "
					+"WHERE PT_PRTCD = BL_PRTCD AND BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '"+cmbBILTP.getSelectedItem().toString().substring(0,2)+"' ";
				if(txtPRTCD.getText().trim().length() > 0)
					M_strSQLQRY += " AND BL_PRTCD LIKE '"+txtPRTCD.getText().trim().toString().toUpperCase()+"%' ";
				M_strSQLQRY +=" ORDER BY BL_PRTCD ";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT");
			}
			if(M_objSOURC == txtPORNO)
			{
				M_strHLPFLD = "txtPORNO";
				M_strSQLQRY = "SELECT DISTINCT BIL_PORRF,sum(BIL_PORVL) FROM MM_BILTR,MM_BLMST "
					+"WHERE BL_CMPCD = BIL_CMPCD AND BL_DOCNO = BIL_DOCNO AND BL_BILTP = BIL_BILTP AND BIL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BIL_BILTP = '"
					+cmbBILTP.getSelectedItem().toString().substring(0,2)+"' ";
				if(txtPRTCD.getText().trim().length() > 0)
					M_strSQLQRY += " AND BL_PRTCD = '"+txtPRTCD.getText().trim()+"' ";
				if(txtPORNO.getText().trim().length() > 0)
					M_strSQLQRY += " AND BIL_PORRF LIKE '"+txtPORNO.getText().trim()+"%' ";
				M_strSQLQRY += " GROUP BY BIL_PORRF ORDER BY BIL_PORRF ";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"P.O. No.","P.O. Val"},2,"CT");
			}
		}
		/** Event On Presseing Enter Button
		 */
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtPORNO)
				getBILDT();		//Call Method to Display Bill Details 
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	/** Method on Event On clicking 'OK' Or Enter Button On Help Window
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPRTCD")
		{
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtPORNO.requestFocus();
		}
		if(M_strHLPFLD == "txtPORNO")
		{
			txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);
			getBILDT();		//Call Method To Ddsplay Bill Details 
		}
	}
	/** Method to Display Details Of GRIN for the Selected Bill Number 
	 */
	public void mouseReleased(MouseEvent L_ME)
	{
		super.mouseReleased(L_ME);
		try
		{
			if((M_objSOURC == tblBILDT) && (tblBILDT.getSelectedColumn() == 0))
			{
				tblGRNDT.clrTABLE();
				if(tblGRNDT.isEditing())
					tblGRNDT.getCellEditor().stopCellEditing();
			
				for(int i=0;i<tblBILDT.getRowCount();i++)
					if(i != tblBILDT.getSelectedRow())
						tblBILDT.setValueAt(new Boolean(false),i,0);
				
				M_strSQLQRY = "SELECT BIL_DOCRF,BIL_MATCD,BIL_CHLQT,BIL_RECQT,BIL_ACPQT,"
					+"BIL_BLPQT,BIL_PORRF,BIL_PORVL FROM MM_BILTR,MM_BLMST WHERE "
					+"BIL_CMPCD =BL_CMPCD and BIL_BILTP = BL_BILTP AND BIL_DOCNO = BL_DOCNO AND BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILNO = '"
					+tblBILDT.getValueAt(tblBILDT.getSelectedRow(),1)+"' AND BL_BILDT = '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblBILDT.getValueAt(tblBILDT.getSelectedRow(),2).toString()))+"' "
					+"AND BL_PRTCD = '"+txtPRTCD.getText().trim()+"' AND BIL_BILTP = '"+cmbBILTP.getSelectedItem().toString().substring(0,2)+"' ";
				if(txtPORNO.getText().trim().length() > 0)
					M_strSQLQRY += " AND BIL_PORRF = '"+txtPORNO.getText().trim()+"' ";
				M_strSQLQRY += " ORDER BY BIL_DOCRF,BIL_MATCD ";
								
				//System.out.println(M_strSQLQRY);
				
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					int i = 0;
					while(M_rstRSSET.next())
					{
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_DOCRF"),""),i,1);
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_MATCD"),""),i,2);
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_CHLQT"),"0.0"),i,3);
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_RECQT"),"0.0"),i,4);
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_ACPQT"),"0.0"),i,5);
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_BLPQT"),"0.0"),i,6);
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_PORRF"),""),i,7);
						tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("BIL_PORVL"),"0.0"),i,8);
						i++;
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseRealeased");
		}
	}
	/** Method to display Bill Details
	 */
	public void getBILDT()
	{	
		try
		{
			tblBILDT.clrTABLE();
			tblGRNDT.clrTABLE();
			blnFIRST = true;
			if(tblBILDT.isEditing())
				tblBILDT.getCellEditor().stopCellEditing();
			if(tblGRNDT.isEditing())
				tblGRNDT.getCellEditor().stopCellEditing();
			M_strSQLQRY = "SELECT BL_DOCNO,BL_BILNO,BL_BILDT,BL_BILAM,BL_CALAM,BL_BLPDT,BL_PRTCD FROM "
				+"MM_BLMST,MM_BILTR WHERE BIL_CMPCD = BL_CMPCD and BIL_BILTP = BL_BILTP AND BIL_DOCNO = BL_DOCNO AND BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_BILTP = '"
				+cmbBILTP.getSelectedItem().toString().substring(0,2)+"' ";
			if(txtPRTCD.getText().trim().length() > 0)
				M_strSQLQRY += " AND BL_PRTCD = '"+txtPRTCD.getText().trim()+"' ";
			if(txtPORNO.getText().trim().length() != 0)
				M_strSQLQRY += "AND BIL_PORRF = '"+txtPORNO.getText().trim()+"' ";
			M_strSQLQRY += "AND isnull(BL_STSFL,'') <> 'X' ORDER BY BL_BILDT ";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			vtrDOCNO = new Vector<String>(10,2);
			if(M_rstRSSET != null)
			{
				int i = 0;
				while(M_rstRSSET.next())
				{
					if(blnFIRST)
					{
						txtPRTCD.setText(M_rstRSSET.getString("BL_PRTCD"));
						txtPRTNM.setText(hstPRTCD.get(M_rstRSSET.getString("BL_PRTCD")).toString());
						blnFIRST = false;
					}
					vtrDOCNO.addElement(M_rstRSSET.getString("BL_DOCNO"));
					tblBILDT.setValueAt(M_rstRSSET.getString("BL_BILNO"),i,1);
					if(M_rstRSSET.getDate("BL_BILDT") != null)
						tblBILDT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_BILDT")),i,2);
					if(M_rstRSSET.getDate("BL_BLPDT") != null)
						tblBILDT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_BLPDT")),i,3);
					tblBILDT.setValueAt(M_rstRSSET.getString("BL_BILAM"),i,4);
					tblBILDT.setValueAt(M_rstRSSET.getString("BL_CALAM"),i,5);
					i++;
				}
				M_rstRSSET.close();
			}
			else
				setMSG("Recode Not Found ..",'E');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getBILDT");
		}
	}
}