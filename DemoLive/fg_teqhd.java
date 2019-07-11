/*
System Name   : Material Management System
Program Name  : Lot status marking for "Quality Hold for Despatch"
Program Desc. : "Quality Hold for Despatch"
Author        : A. T. Chaudhari (Modified by SRD)
Date          : 31/07/2007
Version       : MMS 2.0
*/
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR> <TR><TD>Program Name</TD><TD>Grade Remark</TD></TR> <TR><TD>Program Desc</TD><TD>                                                         Insert the remark for the grade </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\fg_teqhd.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\fg_teqhd.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>07/09/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR> <TR><TD>CO_CDTRN</TD><TD>CMT_CGMTP,CMT_CGSTP,CMT_CODCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>FG_STMST</TD><TD>ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_MNLCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class fg_teqhd extends cl_pbase
{									/**JTextField for ware house type	 */
	private JTextField txtWRHTP;	/**JTextField for product type		 */
	private JTextField txtPRDTP;	/**JTextField for product code		 */
	private JTextField txtPRDCD;	/**JTextField for product description*/
	private JTextField txtPRDDS;	/**JTextField for Party  description */
	//private JTextField txtREMDS;	/**JRadioButton for lotwise copy */
	private ButtonGroup btgSELEC;	/**ButtonGroup for selection posting add or replace */
	private ButtonGroup btgPOSTI;	/**ButtonGroup for selection include or exclude sample lot*/
	private ButtonGroup btgRETEN;	/**JPanel for selection radio button*/
	private cl_JTable tblPRDCD;		/**JTable for reservation detail display */
	private cl_JTable tblCHKDS;		/**String for lot number */
	private String strLOTNO;		/**Int variable for row number */
	private int intROWNO;			/**constant int for column check flag */
	private final int intCHKFL = 0;	
	private final int intPRDDS = 1;	
	private final int intLOTNO = 2;	
	private final int intRCLNO = 3;	
	private final int intREMDS = 4;	
	private final int intSTKQT = 5;	
	private final int intQLHFL = 6;	

	private final int TB2_PRDDS  = 1;
	private final int TB2_REMDS  = 2;
	private final int TB2_LOTNO  = 3;
	
	private String strREMDS="";
	private String strPRTCD="";
	private String strLOTNO1="";
	private String strPLOTNO="";
	
	
	
	//private JList lstCHKDS;			// JList for displaying Lotwise Remark details

	/**Constructor for initialising value and design GUI */
	public fg_teqhd()
	{
		super(2);
		//pnlCHKDS = new JPanel(null);
		btgSELEC = new ButtonGroup();
		btgPOSTI = new ButtonGroup();
		btgRETEN = new ButtonGroup();
		setMatrix(20,8);
		add(new JLabel("Ware House Type"),2,1,1,1,this,'L');
		add(txtWRHTP = new TxtLimit(2),2,2,1,1,this,'L');
		add(new JLabel("Product Type"),2,3,1,1,this,'L');
		add(txtPRDTP = new TxtLimit(2),2,4,1,0.5,this,'L');
		add(new JLabel("Grade"),2,5,1,1,this,'L');
		add(txtPRDCD = new TxtLimit(10),2,6,1,1,this,'L');
		add(txtPRDDS = new JTextField(),2,7,1,1,this,'L');
		
		
		
		//add(new JLabel("Remark"),4,1,1,1,this,'L');
		
		//add(txtREMDS = new TxtLimit(40),4,2,1,5,this,'L');
		
		tblPRDCD = crtTBLPNL1(this,new String[]{"","Grade","Lot Number","Rcl.No.","Remark","Stk.Qty","Q.Hld"},250,5,2,12,5,new int[]{20,150,100,40,250,100,60},new int[]{0,6});
		tblPRDCD.setInputVerifier(new TBLINPVF());
		tblPRDCD.addMouseListener(this);
		intROWNO = 1;
		setENBL(false);
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
	}
	

	
	
	/**Event on the action */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 4)
			{
				txtWRHTP.setEnabled(true);
				txtPRDTP.setEnabled(true);
				txtPRDCD.setEnabled(true);
			}
			txtWRHTP.setText("01");
			txtPRDTP.requestFocus();
		}
		
	}

	
	/**Event on key pressed */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtWRHTP)
			{
				M_strHLPFLD = "txtWRHTP";
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FORM FROM CO_CDTRN WHERE "
					+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'FGXXPTD' ORDER BY CMT_CODCD ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Ware House Code","Ware House Description"},2,"CT");
			}
			if(M_objSOURC == txtPRDTP)
			{
				M_strHLPFLD = "txtPRDTP";
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN "
					+"WHERE CMT_CGMTP = 'MST' AND "
					+"CMT_CGSTP = 'COXXPRD' ORDER BY CMT_CODCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Product Type","Description"},2,"CT");
			}

			if(M_objSOURC == txtPRDCD)
			{
				M_strHLPFLD = "txtPRDCD";
				M_strSQLQRY = "SELECT DISTINCT ST_PRDCD,PR_PRDDS FROM FG_STMST,CO_PRMST "
					+"WHERE PR_PRDCD = ST_PRDCD AND PR_PRDTP = ST_PRDTP AND ST_WRHTP = '"
					+txtWRHTP.getText().trim()+"' AND ST_PRDTP = '"+txtPRDTP.getText().trim()+"' and isnull(st_stkqt,0)>0 ";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					M_strSQLQRY += " AND ST_PRDCD in (select LT_PRDCD from pr_ltmst where isnull(lt_resfl,' ') in ('Q','H')) ";
				if(txtPRDCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND ST_PRDCD LIKE '"+txtPRDCD.getText().trim()+"%' ";
				M_strSQLQRY += "ORDER BY ST_PRDCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtPRDCD)
			{
				getPRDDT();
				//txtREMDS.requestFocus();
			}
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}

	
	/**method on pressing enter or ok button on hepl window */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtWRHTP")
		{
			txtWRHTP.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRDTP.requestFocus();
		}
		if(M_strHLPFLD == "txtPRDTP")
		{
			txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRDCD.requestFocus();
		}
		if(M_strHLPFLD == "txtPRDCD")
		{
			txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			getPRDDT();
			//txtREMDS.requestFocus();
		}
	}
	
	
	/**method to display message on focus on component */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtWRHTP)
			setMSG("Enter Ware House Code Or Press 'F1' For Help ..",'N');
		if(M_objSOURC == txtPRDTP)
			setMSG("Enter Product Type Or Press 'F1' For Help ..",'N');
		if(M_objSOURC == txtPRDCD)
			setMSG("Enter Procuct Code Or Press 'F1' For Help ..",'N');
		//if(M_objSOURC == txtREMDS)
		//	setMSG("Enter Remark For Grade ..",'N');
	}

	
	/**Method to validate in put data before processing  */
	public boolean vldDATA()
	{
		try
		{
			if(txtWRHTP.getText().trim().length() == 0)
			{
				txtWRHTP.requestFocus();
				setMSG("Enter Ware House Type Or Press 'F1' For Help ..",'E');
				return false;
			}
			if(txtPRDTP.getText().trim().length() == 0)
			{
				txtPRDTP.requestFocus();
				setMSG("Enter Product Type Or Press 'F1' For Help ..",'E');
				return false;
			}
			//if(txtPRDCD.getText().trim().length() == 0)
			//{
			//	txtPRDCD.requestFocus();
			//	setMSG("Enter Product Code Or Press 'F1' For Help ..",'E');
			//	return false;
			//}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}

	
	/**Method to get grade data depending on the input and display data on screen */
	public void getPRDDT()
	{
		try
		{
			intROWNO = 0;
			tblPRDCD.clrTABLE();

			if(vldDATA())
			{
				M_strSQLQRY = "SELECT PR_PRDDS,LT_LOTNO,LT_RCLNO,LT_REMDS,sum(isnull(st_stkqt,0)) LT_STKQT,LT_RESFL  FROM FG_STMST,PR_LTMST,CO_PRMST WHERE  LT_PRDCD = PR_PRDCD and LT_PRDTP=ST_PRDTP  AND LT_LOTNO=ST_LOTNO AND LT_RCLNO=ST_RCLNO AND  "
					+"ST_WRHTP = '"+txtWRHTP.getText().trim()+"' AND ST_PRDTP = '"+txtPRDTP.getText().trim()+"' "
							  + (txtPRDCD.getText().trim().length()==10 ? " AND ST_PRDCD = '"+txtPRDCD.getText().trim()+"' " : "")
							  +(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) ? " and LT_RESFL in ('Q','H') " : "");
				M_strSQLQRY += "AND isnull(ST_STKQT,0) > 0 group by PR_PRDDS,LT_LOTNO,LT_RCLNO,LT_REMDS,LT_RESFL ORDER BY PR_PRDDS,LT_LOTNO,LT_RCLNO ";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(tblPRDCD.isEditing())
						tblPRDCD.getCellEditor().stopCellEditing();
					int i = 0;
					while(M_rstRSSET.next())
					{
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""),i,intPRDDS);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("LT_LOTNO"),""),i,intLOTNO);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("LT_RCLNO"),""),i,intRCLNO);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("LT_REMDS"),""),i,intREMDS);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("LT_STKQT"),""),i,intSTKQT);
						//tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("LT_STSFL"),""),i,intQLHFL);
						tblPRDCD.setValueAt(new Boolean((nvlSTRVL(M_rstRSSET.getString("LT_RESFL"),"").equalsIgnoreCase("Q") || nvlSTRVL(M_rstRSSET.getString("LT_RESFL"),"").equalsIgnoreCase("H")) ? true : false),i,intQLHFL);
						i++;
					}
					intROWNO = i;
				}
				else
				{
					setMSG("Record Not Round",'E');
				}
			}
			tblPRDCD.setEnabled(false);
			tblPRDCD.cmpEDITR[intCHKFL].setEnabled(true);
			tblPRDCD.cmpEDITR[intQLHFL].setEnabled(true);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDDT");
		}
	}

	
	/**Method to append or replace remark from remark text field to table */
	public void mouseReleased(MouseEvent L_ME)
	{
		super.mouseReleased(L_ME);
		if(M_objSOURC == tblPRDCD)
		{
			if(tblPRDCD.getSelectedColumn() == intCHKFL)
			{
				int L_intSELRW = tblPRDCD.getSelectedRow();
				//if lot wise appendig or replacing of remark is seleceted
			}
		}
	}
	
	
	/**method to save remark for grade */
	public void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(tblPRDCD.isEditing())
				tblPRDCD.getCellEditor().stopCellEditing();
			for(int i = 0; i < intROWNO;i++)
			{
				if(tblPRDCD.getValueAt(i,intCHKFL).toString().equals("true"))
				{
					if(cl_dat.M_flgLCUPD_pbst)		//Update remark in fg_stmst
					{
						String L_strLTRESFL = "";
						if(tblPRDCD.getValueAt(i,intQLHFL).toString().equals("true"))
							L_strLTRESFL = "Q";
						M_strSQLQRY = "UPDATE PR_LTMST SET LT_RESFL = '"+L_strLTRESFL+"', "
						+"LT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',LT_LUPDT = '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.toString()))+"' "
						+"WHERE LT_PRDTP = '"
						+txtPRDTP.getText().trim()+"' AND LT_LOTNO = '"
						+tblPRDCD.getValueAt(i,intLOTNO)+"' AND LT_RCLNO = '"
						+tblPRDCD.getValueAt(i,intRCLNO)+"' ";
						System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Remark Saved Successfully..",'N');
				clrCOMP();
				if(tblPRDCD.isEditing())
					tblPRDCD.getCellEditor().stopCellEditing();
			}
			else
			{
				setMSG("Error in saving the data..",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
	}
	public void clrCOMP()
	{
		super.clrCOMP();
		strREMDS="";
		strPRTCD="";
		//txtPRTDS.setEnabled(false);
	}

	
	
	/**
	 *  Table Input Verifier Class for Validation
	*/
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				if(P_intCOLID==intQLHFL)
					tblPRDCD.setValueAt(new Boolean(true),P_intROWID,intCHKFL);
				if(getSource()==tblPRDCD)
				{
					if(P_intCOLID==intQLHFL)
					{ 
						tblPRDCD.setValueAt(new Boolean(true),P_intROWID,intCHKFL);
					}
				}
				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"TBLINPVF");
			}
			return true;
		}
	}
	
}
