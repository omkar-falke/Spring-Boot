/*
System Name		: Materials Management System
Program Name	: Discrepancy Report order stock quatity, indent stock quantity, inspection stock quantity
Author			: Abhijit T. Chaudhari
Modified Date	: 08/09/2004
Version			: v2.0.0
*/
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import javax.swing.border.*;
import javax.swing.JComponent;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR> <TR><TD>Program Name</TD><TD>P.O.&nbsp;Query</TD></TR> <TR><TD>Program Desc</TD><TD>Display                                        All the information i.e. P.O. Details,GRIN Detail and Indent Detail       for given P.O. Number And Vendor Code. </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_qrpor.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\mm_qrpor.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>20/08/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR> <TR><TD>MM_STMST</TD><TD>ST_MMSBS,ST_STRTP,ST_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_POMST</TD><TD>PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_INMST</TD><TD>IN_MMSBS,IN_STRTP,IN_INDNO,IN_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_GRMST</TD><TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO&nbsp;</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class mm_qrdes extends cl_pbase
{									/**Jcombobox for descripency option	 */
	private JComboBox cmbDSOPT;		/**Table for stock detail	 */
	private cl_JTable tblSTKQT;		/**Table for p.o detail	 */
	private cl_JTable tblPORQT;		/**Table for grin detail	 */
	private cl_JTable tblGRNQT;		/**Table for indent descripency	 */
	private cl_JTable tblINDQT;		/**Text Field for total no of item */
	private JTextField txtITMCT;	
	private JButton btnPRINT;
	private JButton btnUPDST;		
	private JButton btnPOFCQ;		
									/**JTabbed pane for p.o and grin details	 */
	private JTabbedPane pnlDETIL;	/**Panel for p.o. details	 */
	private JPanel pnlPORQT;		/**panel for grin details	 */
	private JPanel pnlGRNQT;
	private JPanel pnlINDQT;
	private String strSTRNM;
	
	private final int intSTCHKFL = 0;
	private final int intSTSTRTP = 1;
	private final int intSTMATCD = 2;
	private final int intSTSTKFL = 3;
	private final int intSTMATDS = 4;
	private final int intSTUOMCD = 5;
	private final int intSTACTQT = 6;
	private final int intSTCALQT = 7;
	
									/**constant for column number */
	private final int intPOPORNO = 1;
	private final int intPOAMDNO = 2;
	private final int intPOPORDT = 3;
	private final int intPOINDNO = 4;
	private final int intPOUOMCD = 5;
	private final int intPOPORQT = 6;
	private final int intPOACPQT = 7;
	private final int intPOFRCQT = 8;
	private final int intPOPORTP = 9;
	
	private final int intGRGRNNO = 1;
	private final int intGRAMDNO = 2;
	private final int intGRGRNDT = 3;
	private final int intGRPORNO = 4;
	private final int intGRRECQT = 5;
	private final int intGRACPQT = 6;
	private final int intGRREJQT = 7;

	private final int intININDNO = 1;
	private final int intININDDT = 2;
	private final int intININDQT = 3;
	private final int intINAUTQT = 4;
	private final int intINORDQT = 5;
	private final int intINACPQT = 6;
	private final int intINFCCQT = 7;
										/**initialize rererence variable for report		 */
	private mm_rpdes objREPRT;
	/**constructor for the descripency report */
	public mm_qrdes()
	{
		super(2);
		cmbDSOPT = new JComboBox();
		cmbDSOPT.addItem("Discripancy For P.O.");
		cmbDSOPT.addItem("Discripancy For Indent");
		cmbDSOPT.addItem("Discripancy For Inspection");
		cmbDSOPT.addItem("-ve Stock on Hand");
		cmbDSOPT.addItem("-ve Stock on Indent");
		cmbDSOPT.addItem("-ve Stock on Order");
		cmbDSOPT.addItem("-ve Stock on Inspection");
		
		pnlDETIL = new JTabbedPane();
		pnlPORQT = new JPanel(null);
		pnlGRNQT = new JPanel(null);
		pnlINDQT = new JPanel(null);
		strSTRNM = "";
		
		setMatrix(20,8);																
		add(new JLabel("Discripancy Type"),1,2,1,1.5,this,'R');
		add(cmbDSOPT,1,3,1,2,this,'L');
		add(btnPRINT = new JButton("PRINT"),1,7,1,1,this,'R');
		
		tblSTKQT = crtTBLPNL1(this,new String[]{"","STR TP","Material Code","Stk. Control","Description","UOM","Stock Qty.","Calculated Qty."},800,2,1,6,7.9,new int[]{20,40,100,30,300,50,100,100},new int[]{0});
		
		add(new JLabel("Total Mat. Code"),9,1,1,1,this,'L');
		add(txtITMCT = new JTextField(),9,2,1,1,this,'L');
		add(btnUPDST  = new JButton("Update"),9,7,1,1,this,'L');
		
		
		tblPORQT = crtTBLPNL1(pnlPORQT,new String[]{"","P.O. Number","Amd No","P.O. Date","Indent No.","P.O. UOM ","P.O. Qty","Acp. Qty.","Fcc. Qty","P.O. Type"},500,1,1,6,7.8,new int[]{20,80,40,100,100,40,100,100,100,50},new int[]{0});
		tblGRNQT = crtTBLPNL1(pnlGRNQT,new String[]{"","GRIN Number","Amd. No.","GRIN Dt.","P.O. Number","Rec. Qty","Accp. Qty","Rej. Qty."},500,1,1,6,7.8,new int[]{20,100,40,100,100,100,100,100},new int[]{0});
		tblINDQT = crtTBLPNL1(pnlINDQT,new String[]{"","Indent Number","Indent Dt","Ind. Qty","Aut. Qty","Ord. Qty.","Accp. Qty","Fcc. Qty"},500,1,1,6,7.8,new int[]{20,100,100,100,100,100,100,100},new int[]{0});
		add(btnPOFCQ = new JButton("PO FRC.QTY "),7,7,1,1.5,pnlPORQT,'R');		
		add(pnlDETIL,10,1,9,7.9,this,'L');
		pnlDETIL.addTab("P.O. Detail",pnlPORQT);
		pnlDETIL.addTab("GRIN Detail",pnlGRNQT);
		pnlDETIL.addTab("Indent Detail",pnlINDQT);
		
		tblSTKQT.addMouseListener(this);
	}
	/**method on action performed */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) 
		{
			clrCOMP();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() > 0)
			{
				cmbDSOPT.setEnabled(true);
				btnPRINT.setEnabled(true);
				cmbDSOPT.requestFocus();
				//getSTDTL();	//call method to display despripency in table
			}
		}
		if(M_objSOURC == cmbDSOPT)
		{
			getSTDTL();		//call method to display despripency in table
		}
		/**action on pressign button 'PRINT' for print report for perticualr descripency */
		if(M_objSOURC == btnPRINT)
		{
			objREPRT = new mm_rpdes(M_strSBSCD.substring(2,4),cmbDSOPT.getSelectedIndex());
			String [] L_staTEMP = objREPRT.getPRINTERS();	//get list of printer
			JComboBox L_cmbTEMP = new JComboBox(L_staTEMP);	//add to combo box
			L_cmbTEMP.insertItemAt("Select",0);
			//popup priter selection dialog 
			JOptionPane.showConfirmDialog( this,L_cmbTEMP,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
			objREPRT.M_cmbDESTN = L_cmbTEMP;	//initialise priter selection to destination
			objREPRT.M_cmbDESTN.setSelectedIndex(L_cmbTEMP.getSelectedIndex());
			objREPRT.M_strSBSCD = M_strSBSCD;
			objREPRT.exePRINT();	//Call print method of Repoer Program
			L_cmbTEMP.removeAllItems();	//Remove All Item (List Of Printer ) From combo box 
			strSTRNM = "";
		}
		if(M_objSOURC == btnUPDST)
		{
			for(int i=0;i<tblSTKQT.getRowCount();i++)
			{
				if(tblSTKQT.getValueAt(i,intSTCHKFL).toString().equals("true"))
				{
					if(cmbDSOPT.getSelectedIndex() == 0)
					M_strSQLQRY = "Update MM_STMST set ST_STKOR = "+tblSTKQT.getValueAt(i,intSTCALQT)+" where "+
								  " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+tblSTKQT.getValueAt(i,intSTSTRTP)+"'"+
								  " AND ST_MATCD ='"+tblSTKQT.getValueAt(i,intSTMATCD)+"'";
					else if (cmbDSOPT.getSelectedIndex() == 1)
						M_strSQLQRY = "Update MM_STMST set ST_STKIN = "+tblSTKQT.getValueAt(i,intSTCALQT)+" where "+
									 " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+tblSTKQT.getValueAt(i,intSTSTRTP)+"'"+
									  " AND ST_MATCD ='"+tblSTKQT.getValueAt(i,intSTMATCD)+"'";
				
					else if (cmbDSOPT.getSelectedIndex() == 2)
						M_strSQLQRY = "Update MM_STMST set ST_STKIP = "+tblSTKQT.getValueAt(i,intSTCALQT)+" where "+
								       " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+tblSTKQT.getValueAt(i,intSTSTRTP)+"'"+
									  " AND ST_MATCD ='"+tblSTKQT.getValueAt(i,intSTMATCD)+"'";
					
					else if (cmbDSOPT.getSelectedIndex() == 3)
						M_strSQLQRY = "Update MM_STMST set ST_STKQT = "+tblSTKQT.getValueAt(i,intSTCALQT)+" where "+
								      " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+tblSTKQT.getValueAt(i,intSTSTRTP)+"'"+
									  " AND ST_MATCD ='"+tblSTKQT.getValueAt(i,intSTMATCD)+"'";
					
					else if (cmbDSOPT.getSelectedIndex() == 4)
						M_strSQLQRY = "Update MM_STMST set ST_STKIN = "+tblSTKQT.getValueAt(i,intSTCALQT)+" where "+
								      " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+tblSTKQT.getValueAt(i,intSTSTRTP)+"'"+
									  " AND ST_MATCD ='"+tblSTKQT.getValueAt(i,intSTMATCD)+"'";
					
					else if (cmbDSOPT.getSelectedIndex() == 5)
						M_strSQLQRY = "Update MM_STMST set ST_STKOR = "+tblSTKQT.getValueAt(i,intSTCALQT)+" where "+
								      " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+tblSTKQT.getValueAt(i,intSTSTRTP)+"'"+
									  " AND ST_MATCD ='"+tblSTKQT.getValueAt(i,intSTMATCD)+"'";
					
					else if (cmbDSOPT.getSelectedIndex() == 6)
						M_strSQLQRY = "Update MM_STMST set ST_STKIP = "+tblSTKQT.getValueAt(i,6)+" where "+
								      " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+tblSTKQT.getValueAt(i,intSTSTRTP)+"'"+
									   " AND ST_MATCD ='"+tblSTKQT.getValueAt(i,intSTMATCD)+"'";
					
					System.out.println(M_strSQLQRY);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY,"selLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst)
						if(cl_dat.exeDBCMT("save"))
							setMSG("updated successfully ",'N');
						else
							setMSG("Error in updating",'E');
				}
			}
		}
		if(M_objSOURC == btnPOFCQ) // P.O. Forceful qty
		{
			String L_strSTRTP="",L_strMATCD ="";
			for(int i=0;i<tblSTKQT.getRowCount();i++)
			{
				if(tblSTKQT.getValueAt(intSTCHKFL,0).toString().equals("true"))
				{
					L_strSTRTP = tblSTKQT.getValueAt(i,intSTSTRTP).toString();
					L_strMATCD = tblSTKQT.getValueAt(i,intSTMATCD).toString();
				}
			}
			for(int i=0;i<tblPORQT.getRowCount();i++)
			{
				if(tblPORQT.getValueAt(i,0).equals("true"))
				{
					M_strSQLQRY = "Update MM_POMST set PO_FRCQT = PO_PORQT - PO_ACPQT where "+
								  " PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+L_strSTRTP +"'"+
								  " AND PO_PORNO = '"+
								  tblPORQT.getValueAt(i,1)+"' and po_indno ='"+tblPORQT.getValueAt(i,4)+"' and po_matcd ='"+L_strMATCD+"' ";
				//	System.out.println(M_strSQLQRY);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY,"selLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst)
						if(cl_dat.exeDBCMT("save"))
							setMSG("updated successfully ",'N');
						else
							setMSG("Error in updating",'E');
				}
			}
		}
	}
	/**method to get detail for the descripency of stock and display into table */
	public void getSTDTL()
	{
		try
		{
			if(tblSTKQT.isEditing())
				tblSTKQT.getCellEditor().stopCellEditing();
			if(tblPORQT.isEditing())
				tblPORQT.getCellEditor().stopCellEditing();
			if(tblGRNQT.isEditing())
				tblGRNQT.getCellEditor().stopCellEditing();
			if(tblINDQT.isEditing())
				tblINDQT.getCellEditor().stopCellEditing();
			tblSTKQT.clrTABLE();
			tblPORQT.clrTABLE();
			tblGRNQT.clrTABLE();
			tblINDQT.clrTABLE();
			/**Querys for descripencys */
			if(cmbDSOPT.getSelectedIndex() == 0)
				M_strSQLQRY = "SELECT ST_STRTP,ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKOR AS TRNQT,SUM(isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)- isnull(PO_FRCQT,0)) "
					+"AS ACTQT FROM MM_STMST,MM_POMST WHERE PO_CMPCD=ST_CMPCD and PO_STRTP = ST_STRTP AND PO_MATCD = ST_MATCD AND "
					+" PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'') NOT IN ('X','O') "
					+"AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' AND isnull(PO_PORTP,'') ='01' GROUP BY ST_STRTP,ST_MATCD,ST_STKFL,ST_STKOR,ST_MATDS,ST_UOMCD "
					+"HAVING SUM(isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0)) <> ST_STKOR "
					+"ORDER BY ST_STRTP,ST_MATCD ";
			if(cmbDSOPT.getSelectedIndex() == 1)
				M_strSQLQRY = "SELECT ST_STRTP,ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKIN AS TRNQT,SUM(isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)- isnull(IN_FCCQT,0)) "
					+"AS ACTQT FROM MM_STMST,MM_INMST WHERE IN_CMPCD=ST_CMPCD AND IN_STRTP = ST_STRTP AND IN_MATCD = ST_MATCD AND "
					+" IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') = '4' AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' "
					+"AND isnull(IN_INDTP,'') = '01' GROUP BY ST_STRTP,ST_MATCD,ST_STKFL,ST_STKIN,ST_MATDS,ST_UOMCD "
					+"HAVING SUM(isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)) <> ST_STKIN "
					+"ORDER BY ST_STRTP,ST_MATCD ";
			if(cmbDSOPT.getSelectedIndex() == 2)
				M_strSQLQRY = "SELECT ST_STRTP,ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKIP AS TRNQT,SUM(isnull(GR_RECQT,0) - isnull(GR_ACPQT,0)- isnull(GR_REJQT,0)) "
					+"AS ACTQT FROM MM_STMST,MM_GRMST WHERE GR_CMPCD = ST_CMPCD and GR_STRTP = ST_STRTP AND GR_MATCD = ST_MATCD AND "
					+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <> 'X' AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X'  "
					+" GROUP BY ST_STRTP,ST_MATCD,ST_STKFL,ST_STKIP,ST_MATDS,ST_UOMCD "
					+"HAVING SUM(isnull(GR_RECQT,0) - isnull(GR_ACPQT,0)- isnull(GR_REJQT,0)) <> isnull(ST_STKIP,0) "
					+"ORDER BY ST_STRTP,ST_MATCD ";
			
			if(cmbDSOPT.getSelectedIndex() == 3)
				M_strSQLQRY = "SELECT ST_STRTP,ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKQT AS TRNQT,0.00 AS ACTQT "
					+" FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STKQT,0) < 0 AND "
					+"isnull(ST_STSFL,'') <> 'X' "
				//	+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"'"
					+" ORDER BY ST_STRTP,ST_MATCD ";
			
			if(cmbDSOPT.getSelectedIndex() == 4)
				M_strSQLQRY = "SELECT ST_STRTP,ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKIN AS TRNQT,0.00 AS ACTQT "
					+" FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STKIN,0) < 0 AND "
					+"isnull(ST_STSFL,'') <> 'X' "
				//	+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"'"
					+" ORDER BY ST_STRTP,ST_MATCD ";
			
			if(cmbDSOPT.getSelectedIndex() == 5)
				M_strSQLQRY = "SELECT ST_STRTP,ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKOR AS TRNQT,0.00 AS ACTQT "
					+" FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STKOR,0) < 0 AND "
					+"isnull(ST_STSFL,'') <> 'X' "
				//	+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"'"
					+" ORDER BY ST_STRTP,ST_MATCD ";
			
			if(cmbDSOPT.getSelectedIndex() == 6)
				M_strSQLQRY = "SELECT ST_STRTP,ST_MATCD,ST_STKFL,ST_MATDS,ST_UOMCD,ST_STKIP AS TRNQT,0.00 AS ACTQT "
					+" FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STKIP,0) < 0 AND "
					+"isnull(ST_STSFL,'') <> 'X' "
					//+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"'"
					+" ORDER BY ST_STRTP,ST_MATCD ";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				int i = 0;
				while(M_rstRSSET.next())
				{
					tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STRTP"),""),i,intSTSTRTP);
					tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),i,intSTMATCD);
					if(nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"").equals("Y"))
						tblSTKQT.setValueAt("Y",i,intSTSTKFL);
					tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),i,intSTMATDS);
					tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),i,intSTUOMCD);
					tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("TRNQT"),"0.0"),i,intSTACTQT);
					tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ACTQT"),"0.0"),i,intSTCALQT);
					i++;
					/*	if(cmbDSOPT.getSelectedIndex() == 0)
						tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0.0"),i,5);
					
					if(cmbDSOPT.getSelectedIndex() == 1)
						tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0.0"),i,5);
					
					if(cmbDSOPT.getSelectedIndex() == 2)
						tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),"0.0"),i,5);
					
					if(cmbDSOPT.getSelectedIndex() == 3)
						tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0.0"),i,5);
					
					if(cmbDSOPT.getSelectedIndex() == 4)
						tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),"0.0"),i,5);
					
					if(cmbDSOPT.getSelectedIndex() == 5)
						tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),"0.0"),i,5);
					
					if(cmbDSOPT.getSelectedIndex() == 6)
						tblSTKQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),"0.0"),i,5);*/
					
				}
				txtITMCT.setText(String.valueOf(i));
			}
			tblSTKQT.cmpEDITR[0].setEnabled(true);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTDTL");
		}
	}
	/**event on the mouse clicked for display addition informat about descripency */
	public void mouseClicked(MouseEvent L_ME)
	{
		super.mouseClicked(L_ME);
		String L_strSTRTP ="",L_strMATCD ="";
		try
		{
			if(M_objSOURC == tblSTKQT)
			{
				
				//if mouse clicled on the check status flag column then display detail in tabbed pane table
				if(tblSTKQT.getSelectedColumn() == 0)
				{
					tblPORQT.clrTABLE();
					tblGRNQT.clrTABLE();
					tblINDQT.clrTABLE();

					if(tblPORQT.isEditing())
						tblPORQT.getCellEditor().stopCellEditing();
					if(tblGRNQT.isEditing())
						tblGRNQT.getCellEditor().stopCellEditing();
					if(tblINDQT.isEditing())
						tblINDQT.getCellEditor().stopCellEditing();
					
					for(int i = 0;i < tblSTKQT.getRowCount();i++)
						if(i != tblSTKQT.getSelectedRow())
							tblSTKQT.setValueAt(new Boolean(false),i,0);
					L_strSTRTP = tblSTKQT.getValueAt(tblSTKQT.getSelectedRow(),intSTSTRTP).toString();
					L_strMATCD = tblSTKQT.getValueAt(tblSTKQT.getSelectedRow(),intSTMATCD).toString();
					//display detail for p.o.
					M_strSQLQRY = "SELECT PO_PORNO,PO_PORDT,PO_INDNO,PO_PORQT,PO_ACPQT,PO_FRCQT,PO_AMDNO,PO_UOMCD,po_portp "
						+"FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '"+L_strSTRTP+"' AND "
						+"PO_MATCD = '"+L_strMATCD+"' AND "
						+"isnull(PO_STSFL,'') NOT IN ('X','O') AND isnull(PO_PORTP,'') in('01','02') ORDER BY PO_STRTP,PO_PORNO,PO_MATCD";

					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						int i = 0;
						while(M_rstRSSET.next())
						{
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),i,intPOPORNO);
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),""),i,intPOAMDNO);
							if(M_rstRSSET.getDate("PO_PORDT") != null)
								tblPORQT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("PO_PORDT")),i,intPOPORDT);
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),""),i,intPOINDNO);
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_UOMCD"),""),i,intPOUOMCD);
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0.0"),i,intPOPORQT);
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_ACPQT"),"0.0"),i,intPOACPQT);
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_FRCQT"),"0.0"),i,intPOFRCQT);
							tblPORQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORTP"),"0.0"),i,intPOPORTP);
							i++;
						}
						M_rstRSSET.close();
					}
					//display detail for grin
					M_strSQLQRY = "SELECT GR_GRNNO,GR_AMDNO,GR_GRNDT,GR_PORNO,GR_RECQT,GR_ACPQT,GR_REJQT "
						+"FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+L_strSTRTP+"' AND "
						+"GR_MATCD = '"+L_strMATCD+"' AND "
						+"isnull(GR_STSFL,'') <> 'X' ORDER BY GR_GRNNO ";
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						int i = 0;
						while(M_rstRSSET.next())
						{
							tblGRNQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),i,intGRGRNNO);
							tblGRNQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_AMDNO"),""),i,intGRAMDNO);
							if(M_rstRSSET.getDate("GR_GRNDT") != null)
								tblGRNQT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_GRNDT")),i,intGRGRNDT);
							tblGRNQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),i,intGRPORNO);
							tblGRNQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0.0"),i,intGRRECQT);
							tblGRNQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.0"),i,intGRACPQT);
							tblGRNQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0.0"),i,intGRREJQT);
							i++;
						}
						M_rstRSSET.close();
					}
					//display detail for indent 
					M_strSQLQRY = "SELECT IN_INDNO,IN_INDDT,IN_INDQT,IN_AUTQT,IN_ORDQT,IN_REQQT,IN_ACPQT,IN_FCCQT "
						+"FROM MM_INMST WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+L_strSTRTP+"' AND "
						+"IN_MATCD = '"+L_strMATCD+"' "
						+" AND isnull(IN_STSFL,'') = '4' AND isnull(IN_INDTP,'') = '01' ORDER BY IN_STRTP,IN_INDNO,IN_MATCD ";
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						int i = 0;
						while(M_rstRSSET.next())
						{
							tblINDQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),i,intININDNO);
							if(M_rstRSSET.getDate("IN_INDDT") != null)
								tblINDQT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_INDDT")),i,intININDDT);
							tblINDQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDQT"),"0.0"),i,intININDQT);
							tblINDQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),"0.0"),i,intINAUTQT);
							tblINDQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_ORDQT"),"0.0"),i,intINORDQT);
							tblINDQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_ACPQT"),"0.0"),i,intINACPQT);
							tblINDQT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_FCCQT"),"0.0"),i,intINFCCQT);
							i++;
						}
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseClicked");
		}
	}
}

