/*
System Name   : Material Management System
Program Name  : Grade Remark Entry
Program Desc. : Enter Remark for the grade 
Author        : A. T. Chaudhari
Date          : 05/09/2004
Version       : MMS 2.0
*/
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR> <TR><TD>Program Name</TD><TD>Grade Remark</TD></TR> <TR><TD>Program Desc</TD><TD>                                                         Insert the remark for the grade </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\fg_testr.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\fg_testr.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>07/09/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR> <TR><TD>CO_CDTRN</TD><TD>CMT_CGMTP,CMT_CGSTP,CMT_CODCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>FG_STMST</TD><TD>ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_MNLCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class fg_testr extends cl_pbase
{									/**JTextField for ware house type	 */
	private JTextField txtWRHTP;	/**JTextField for product type		 */
	private JTextField txtPRDTP;	/**JTextField for product code		 */
	private JTextField txtPRDCD;	/**JTextField for product description*/
	private JTextField txtPRDDS;	/**JTextField for remark description */
	private JTextField txtREMDS;	/**JRadioButton for lotwise copy */
	private JRadioButton rdbLOTNO;	/**JRadioButton for Location Wise copy*/
	private JRadioButton rdbMNLCD;	/**JRadioButton for add remark*/
	private JRadioButton rdbADDRM;	/**JRadioButton for replace remark*/
	private JRadioButton rdbREPRM;	/**JRadioButton for exclude sample lot*/
	private JRadioButton rdbEXCLD;	/**JRadioButton for include sample lot*/
	private JRadioButton rdbINCLD;	/**ButtonGroup for selection lot or location*/
	private ButtonGroup btgSELEC;	/**ButtonGroup for selection posting add or replace */
	private ButtonGroup btgPOSTI;	/**ButtonGroup for selection include or exclude sample lot*/
	private ButtonGroup btgRETEN;	/**JPanel for selection radio button*/
	private JPanel pnlSELEC;		/**JPanel for Displaying reservation list*/
	private JPanel pnlCHKDS;		/**JPanel for selection posting button */
	private JPanel pnlPOSTI;		/**JPanel for selection  */
	private JPanel pnlRETEN;		/**JTable for product details */
	private cl_JTable tblPRDCD;		/**JTable for reservation detail display */
	private cl_JTable tblCHKDS;		/**String for lot number */
	private String strLOTNO;		/**Int variable for row number */
	private int intROWNO;			/**constant int for column check flag */
	private final int intCHKFL = 0;	/**constant int for column lot number*/
	private final int intLOTNO = 1;	/**constant int for column Relocation */
	private final int intRCLNO = 2;	/**constant int for column remark */
	private final int intREMDS = 3;	/**constant int for column location*/
	private final int intMNLCD = 4;	/**constant int for column stock quantity*/
	private final int intSTKQT = 5;	/**constant int for column package type*/
	private final int intPKGTP = 6;

	private final int TB2_PRDDS  = 1;
	private final int TB2_REMDS  = 2;
	private final int TB2_LOTNO  = 3;
	
	
	//private JList lstCHKDS;			// JList for displaying Lotwise Remark details
	private JCheckBox chkCHKDS;
	//private Vector vtrCHKDS;
	/**Constructor for initialising value and design GUI */
	public fg_testr()
	{
		super(2);
		pnlSELEC = new JPanel(null);
		//pnlCHKDS = new JPanel(null);
		pnlPOSTI = new JPanel(null);
		pnlRETEN = new JPanel(null);
		//lstCHKDS = new JList();
		//vtrCHKDS = new Vector();
		pnlSELEC.setBorder(new TitledBorder("Selection"));
		pnlPOSTI.setBorder(new TitledBorder("Posting"));
		pnlRETEN.setBorder(new TitledBorder("Retention"));
		rdbLOTNO = new JRadioButton("Lot",true);
		rdbMNLCD = new JRadioButton("Location",false);
		rdbADDRM = new JRadioButton("Add",true);
		rdbREPRM = new JRadioButton("Replace",false);
		rdbEXCLD = new JRadioButton("Exclude",true);
		rdbINCLD = new JRadioButton("Include",false);
		btgSELEC = new ButtonGroup();
		btgPOSTI = new ButtonGroup();
		btgRETEN = new ButtonGroup();
		btgSELEC.add(rdbLOTNO);
		btgSELEC.add(rdbMNLCD);
		btgPOSTI.add(rdbADDRM);
		btgPOSTI.add(rdbREPRM);
		btgRETEN.add(rdbEXCLD);
		btgRETEN.add(rdbINCLD);
		setMatrix(20,8);
		//add(new JScrollPane(lstCHKDS),3,3,3,2,this,'L');
		add(rdbLOTNO,1,1,1,1,pnlSELEC,'L');
		add(rdbMNLCD,1,2,1,0.8,pnlSELEC,'L');
		add(rdbADDRM,1,1,1,1,pnlPOSTI,'L');
		add(rdbREPRM,1,2,1,0.8,pnlPOSTI,'L');
		add(rdbEXCLD,1,1,1,1,pnlRETEN,'L');
		add(rdbINCLD,1,2,1,0.8,pnlRETEN,'L');
		add(new JLabel("Ware House Type"),2,1,1,1,this,'L');
		add(txtWRHTP = new TxtLimit(2),2,2,1,1,this,'L');
		add(new JLabel("Product Type"),2,3,1,1,this,'L');
		add(txtPRDTP = new TxtLimit(2),2,4,1,1,this,'L');
		add(new JLabel("Grade"),2,5,1,1,this,'L');
		add(txtPRDCD = new TxtLimit(10),2,6,1,1,this,'L');
		add(txtPRDDS = new JTextField(),2,7,1,1,this,'L');
		add(new JLabel("Remark"),4,1,1,1,this,'L');
		add(txtREMDS = new TxtLimit(40),4,2,1,5,this,'L');
		add(pnlSELEC,5,2,2,1.9,this,'L');
		add(pnlPOSTI,5,4,2,1.9,this,'L');
		add(pnlRETEN,5,6,2,1.9,this,'L');
		chkCHKDS = new JCheckBox("Display List");
		add(chkCHKDS,18,7,1,2,this,'L');
		
		tblPRDCD = crtTBLPNL1(this,new String[]{"","Lot Number","Reclassification No.","Remark","Location","Stock","Package Type"},250,8,1,9,8.1,new int[]{20,100,60,200,100,100,100},new int[]{0});
		tblPRDCD.addMouseListener(this);
		intROWNO = 1;
		setENBL(false);
		//lstCHKDS.setBackground(Color.CYAN);
		//lstCHKDS.setVisible(false);
		//dspCHKDS();
	}

	

	public void dspCHKDS()
	{
		try
		{
			if(chkCHKDS.isSelected()==false)
				return;
			if(pnlCHKDS==null)
			{
				pnlCHKDS=new JPanel(null);
				tblCHKDS = crtTBLPNL1(pnlCHKDS,new String[]{"","Grade","Description","Lot No."},250,1,1,9,6,new int[]{20,100,300,80},new int[]{0});
			}
			//clrVHRTBL();
				
			M_strSQLQRY = "select distinct st_remds,st_lotno,pr_prdds  from fg_stmst, co_prmst where st_prdcd=pr_prdcd and st_stkqt>0 and length(trim(st_remds))>0 order by pr_prdds,st_remds";
			//System.out.println(LM_STRQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
				return;
			int i =0;
			while (true)
			{
				tblCHKDS.setValueAt(M_rstRSSET.getString("pr_prdds"),i,TB2_PRDDS);
				tblCHKDS.setValueAt(M_rstRSSET.getString("st_remds"),i,TB2_REMDS);
				tblCHKDS.setValueAt(M_rstRSSET.getString("st_lotno"),i,TB2_LOTNO);
				i++;
				if(!M_rstRSSET.next())
					break;
			}
			M_rstRSSET.close();
				
			pnlCHKDS.setSize(100,100);
			pnlCHKDS.setPreferredSize(new Dimension(700,250));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlCHKDS,"Stock Remark List",JOptionPane.OK_CANCEL_OPTION);
			//int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlCHKDS,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspCHKDS");
		}
	}
	
	
	/**Event on the action */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			chkCHKDS.setSelected(false);
			rdbLOTNO.setSelected(true);
			rdbADDRM.setSelected(true);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 4)
			{
				txtWRHTP.setEnabled(true);
				txtPRDTP.setEnabled(true);
				txtPRDCD.setEnabled(true);
			}
			txtWRHTP.requestFocus();
		}
		if(M_objSOURC == rdbINCLD || M_objSOURC == rdbEXCLD)
		{
			getPRDDT();
			txtREMDS.requestFocus();
		}
		if(M_objSOURC == chkCHKDS)
		{
			dspCHKDS();
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
			//if(M_objSOURC == txtCHKDS)
			//{
			//	M_strHLPFLD = "txtCHKDS";
			//	M_strSQLQRY = "select distinct st_remds,st_lotno,pr_prdds  from fg_stmst, co_prmst where st_prdcd=pr_prdcd and st_stkqt>0 and length(trim(st_remds))>0 order by pr_prdds,st_remds";
			//	cl_hlp(M_strSQLQRY,1,1,new String[]{"Description","Lot No.","Grade"},3,"CT");
			//}
			//;

			if(M_objSOURC == txtPRDCD)
			{
				M_strHLPFLD = "txtPRDCD";
				M_strSQLQRY = "SELECT DISTINCT ST_PRDCD,PR_PRDDS FROM FG_STMST,CO_PRMST "
					+"WHERE PR_PRDCD = ST_PRDCD AND PR_PRDTP = ST_PRDTP AND ST_WRHTP = '"
					+txtWRHTP.getText().trim()+"' AND ST_PRDTP = '"+txtPRDTP.getText().trim()+"' ";
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
				txtREMDS.requestFocus();
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
			txtREMDS.requestFocus();
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
		if(M_objSOURC == txtREMDS)
			setMSG("Enter Remark For Grade ..",'N');
		if(M_objSOURC == rdbLOTNO)
			setMSG("Check For Lot Wise Copy Of Remark ..",'N');
		if(M_objSOURC == rdbMNLCD)
			setMSG("Check For Location Wise Copy Of Remark ..",'N');
		if(M_objSOURC == rdbADDRM)
			setMSG("Check For Add Remark to Previous Remark ..",'N');
		if(M_objSOURC == rdbREPRM)
			setMSG("Check For Repalce Remark ..",'N');
		if(M_objSOURC == rdbEXCLD)
			setMSG("Check For Exclude Sample Lot ..",'N');
		if(M_objSOURC == rdbINCLD)
			setMSG("Check FOr Include Sample Lot ..",'N');
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
			if(txtPRDCD.getText().trim().length() == 0)
			{
				txtPRDCD.requestFocus();
				setMSG("Enter Product Code Or Press 'F1' For Help ..",'E');
				return false;
			}
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
			if(vldDATA())
			{
				M_strSQLQRY = "SELECT ST_LOTNO,ST_RCLNO,ST_MNLCD,ST_STKQT,ST_REMDS,ST_PKGTP FROM FG_STMST WHERE "
					+"ST_WRHTP = '"+txtWRHTP.getText().trim()+"' AND ST_PRDTP = '"
					+txtPRDTP.getText().trim()+"' AND ST_PRDCD = '"+txtPRDCD.getText().trim()+"' ";
				if(rdbEXCLD.isSelected())
					M_strSQLQRY += " AND isnull(ST_STKQT,0) <> 0.025 ";
				M_strSQLQRY += "AND isnull(ST_STKQT,0) > 0 ORDER BY ST_LOTNO,ST_MNLCD ";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(tblPRDCD.isEditing())
						tblPRDCD.getCellEditor().stopCellEditing();
					int i = 0;
					while(M_rstRSSET.next())
					{
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_LOTNO"),""),i,intLOTNO);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_RCLNO"),""),i,intRCLNO);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_REMDS"),""),i,intREMDS);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MNLCD"),""),i,intMNLCD);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),i,intSTKQT);
						tblPRDCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_PKGTP"),""),i,intPKGTP);
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
				if(rdbLOTNO.isSelected())
				{
					strLOTNO = tblPRDCD.getValueAt(L_intSELRW,intLOTNO).toString();
					for(int i = L_intSELRW ;i<100;i++)
					{
						if(strLOTNO.equals(tblPRDCD.getValueAt(i,intLOTNO)))
						{
							if(rdbADDRM.isSelected())
							{
								tblPRDCD.setValueAt(tblPRDCD.getValueAt(i,intREMDS)+txtREMDS.getText(),i,intREMDS);									
							}
							else
							{
								tblPRDCD.setValueAt(txtREMDS.getText().trim(),i,intREMDS);								
							}
							tblPRDCD.setValueAt(Boolean.TRUE,i,intCHKFL);
							strLOTNO = tblPRDCD.getValueAt(i,intLOTNO).toString();
						}
					}
				}
				//else location wise append or replace of remark is selected
				else
				{
					if(rdbADDRM.isSelected())
					{
						tblPRDCD.setValueAt(tblPRDCD.getValueAt(L_intSELRW,intREMDS)+txtREMDS.getText(),L_intSELRW,intREMDS);
					}
					else
					{
						tblPRDCD.setValueAt(txtREMDS.getText().trim(),L_intSELRW,intREMDS);
					}
					tblPRDCD.setValueAt(Boolean.TRUE,tblPRDCD.getSelectedRow(),intCHKFL);
				}
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
				if(cl_dat.M_flgLCUPD_pbst)		//Update remark in fg_stmst
				{
					M_strSQLQRY = "UPDATE FG_STMST SET ST_REMDS = '"+tblPRDCD.getValueAt(i,intREMDS)+"', "
						+"ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',ST_LUPDT = '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.toString()))+"' "
						+"WHERE ST_WRHTP = '"+txtWRHTP.getText().trim()+"' AND ST_PRDTP = '"
						+txtPRDTP.getText().trim()+"' AND ST_LOTNO = '"
						+tblPRDCD.getValueAt(i,intLOTNO)+"' AND ST_RCLNO = '"
						+tblPRDCD.getValueAt(i,intRCLNO)+"' AND ST_PKGTP = '"
						+tblPRDCD.getValueAt(i,intPKGTP)+"' AND ST_MNLCD = '"
						+tblPRDCD.getValueAt(i,intMNLCD)+"' AND ST_PRDCD = '"+txtPRDCD.getText().trim()+"' ";
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
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
}
