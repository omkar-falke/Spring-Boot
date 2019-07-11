import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import javax.swing.BorderFactory;import javax.swing.JPanel;import javax.swing.JOptionPane;import javax.swing.JTabbedPane;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.JButton;
import java.util.Hashtable;import java.util.Vector;
class mm_tedor extends cl_pbase implements MouseListener
{
	private JRadioButton rdbINDSP,rdbINDALL;
	private JPanel pnlITDTL,pnlVNDTL,pnlDODTL;
	private JLabel lblTBLIN,lblTBLVN;
	private cl_JTable tblITDTL,tblVNDTL,tblDODTL;
	private JTextField txtINDNO;
	private JButton btnACPDT,btnDODTL;
	private JTabbedPane tbpMAIN;/** Panel to contain GRIN base details in tabbed pane*/
	private final int TBL_CHKFL =0;
	private final int TBL_INDNO =1;
	private final int TBL_MATCD =2;
	private final int TBL_MATDS =3;
	private final int TBL_UOMCD =4;
	private final int TBL_PKGQT =5;
	private final int TBL_AUTQT =6;
	private final int TBL_PENQT =7;
	private final int TBL_PORBY =8;
	private final int TBL_DPTCD =9;

	private final int TBL_VENCD =1;
	private final int TBL_VENNM =2;
	private final int TBL_ARCNO =3;
	private final int TBL_ARCQT =4;
	private final int TBL_DORQT =5;
	
	private Hashtable<String,Object> hstTBLDT;    /** Hashtable to save the vendor table details in a vector against Indent /Item code */
	private Hashtable hstDODTL;    /** Hashtable to save the vendor/ARC wise Details for D.O to be generated.           */
	private Vector<String> vtrDORCT = new Vector<String>();
	private Vector<String> vtrDORQT = new Vector<String>();
	private String strINDNO="",strMATCD="",strPORNO ="";
	private int intCOUNT=0,intROWCT =0;
	mm_tedor()
	{
		super(2);
		setMatrix(20,8);
		tblITDTL=crtTBLPNL1(pnlITDTL=new JPanel(null),new String[]{"FL","Indent No.","Item Code","Description ","UOM ","Min.Pkg","Aut. Qty","Pen.Qty","PO By Dt","Dept"},300,1,1,8,7.9,new int[]{20,65,75,300,30,60,60,70,70,10},new int[]{0});
		add(pnlITDTL,1,1,9,7.9,this,'L');
		
		add(btnACPDT=new JButton("Accept"),11,6,1,1,this,'L');
		add(btnDODTL=new JButton("Display"),11,7,1,1,this,'L');
		
		tbpMAIN=new JTabbedPane();
		
		pnlVNDTL=new JPanel(null);
		tblVNDTL=crtTBLPNL1(pnlVNDTL,new String[]{"FL","Vendor Code","Vendor Name","ARC Ref","ARC Qty","D.O. Qty"},10,1,1,6,7,new int[]{20,70,300,75,75,75},new int[]{0});
		tbpMAIN.add("Vendor Details",pnlVNDTL);
		
		pnlDODTL=new JPanel(null);
		tblDODTL=crtTBLPNL1(pnlDODTL,new String[]{"FL","Vendor Code","ARC REf.","Indent No.","Item Code","Description ","UOM ","DO Qty"},300,1,1,9,7.9,new int[]{20,60,80,65,75,300,40,60},new int[]{0});
		tbpMAIN.add("D.O. Details",pnlDODTL);
		
		add(tbpMAIN,12,1,12.3,8.1,this,'L');
		
	//	add(pnlVNDTL,12,1,6,7,this,'L');
		tblITDTL.addMouseListener(this);
		tblITDTL.cmpEDITR[1].addMouseListener(this);
		hstTBLDT = new Hashtable<String,Object>();
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		tblITDTL.setEnabled(false);
		tblVNDTL.setEnabled(false);
		tblDODTL.setEnabled(false);
		tblITDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblVNDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblVNDTL.cmpEDITR[TBL_DORQT].setEnabled(true);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_btnEXIT_pbst)
		{
			try
			{
				cl_dat.exeSQLUPD("drop table tt_do"+cl_dat.M_strUSRCD_pbst,"setLCLUPD");
				cl_dat.exeDBCMT("btnEXIT");
			}
			catch(Exception L_E)
			{
				setMSG("Error in dropping the table..",'E');
			}
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			try
			{
				if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					int i=0;
					// FETCHING THE PENDING INDENTS FOR WHICH A VALID ,PENDING RATE CONTRACT EXIST
					M_strSQLQRY = "SELECT distinct IN_INDNO,IN_MATCD,CT_MATDS,CT_UOMCD,CT_PKGQT,IN_PORBY,IN_AUTQT,"
					+"IFNULL(IN_AUTQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) L_PENQT,IN_DPTCD "
					+"from MM_INMST,MM_POMST,CO_CTMST WHERE IN_STRTP = PO_STRTP AND IN_MATCD = PO_MATCD AND IN_CMPCD=PO_CMPCD AND IN_MATCD = CT_MATCD "
					+"AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDTP ='01' AND IFNULL(IN_AUTQT,0)-IFNULL(IN_ORDQT,0)-IFNULL(IN_FCCQT,0) >0 AND IN_STSFL ='4'"
					+" AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_PORTP ='03' and IFNULL(PO_PORQT,0)-IFNULL(PO_ORDQT,0)-IFNULL(PO_FRCQT,0) >0 AND PO_STSFL NOT IN('X','O')";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					intROWCT =0;
					if(M_rstRSSET !=null)
					{
						while(M_rstRSSET.next())
						{
							tblITDTL.setValueAt(M_rstRSSET.getString("IN_INDNO"),i,TBL_INDNO);
							tblITDTL.setValueAt(M_rstRSSET.getString("IN_MATCD"),i,TBL_MATCD);
							tblITDTL.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,TBL_MATDS);
							tblITDTL.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,TBL_UOMCD);
							tblITDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),"0"),i,TBL_PKGQT);
							tblITDTL.setValueAt(M_rstRSSET.getString("IN_AUTQT"),i,TBL_AUTQT);
							tblITDTL.setValueAt(M_rstRSSET.getString("L_PENQT"),i,TBL_PENQT);
							tblITDTL.setValueAt(M_rstRSSET.getString("IN_PORBY"),i,TBL_PORBY);
							tblITDTL.setValueAt(M_rstRSSET.getString("IN_DPTCD"),i++,TBL_DPTCD);
						}
						intROWCT = i;
						M_rstRSSET.close();
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG("Error in Fetching Item details..",'E');
			}
				
		}
		/**
		 * At Button Accept data is checked for Validity and then a vector of vendor details 
		 * is put into the hashtable.
		 * Anothor vector is formed for distinct Vendor and ARC
		 * Anothor vector is formed for full details of vendors
		 */
		else if(M_objSOURC == btnDODTL)
		{
			int L_intRECCT =0;
			tblDODTL.clrTABLE();
			tblDODTL.setRowSelectionInterval(0,0);
			tblDODTL.setColumnSelectionInterval(0,0);
			cl_dat.M_flgLCUPD_pbst = true;
			int i=0;
			try
			{
				M_strSQLQRY = "select * from tt_do"+cl_dat.M_strUSRCD_pbst;
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
						tblDODTL.setValueAt(M_rstRSSET.getString("PO_VENCD"),i,1);
						tblDODTL.setValueAt(M_rstRSSET.getString("PO_PORNO"),i,2);
						tblDODTL.setValueAt(M_rstRSSET.getString("IN_INDNO"),i,3);
						tblDODTL.setValueAt(M_rstRSSET.getString("IN_MATCD"),i,4);
						tblDODTL.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,5);
						tblDODTL.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,6);
						tblDODTL.setValueAt(M_rstRSSET.getString("PO_DORQT"),i++,7);
					}
					M_rstRSSET.close();
					tbpMAIN.setSelectedIndex(1);
				}
			}
			catch(Exception L_E)
			{
					
			}
		}
		else if(M_objSOURC == btnACPDT)
		{
			int L_intRECCT =0;
			if(tblVNDTL.isEditing())
				tblVNDTL.getCellEditor().stopCellEditing();
			tblVNDTL.setRowSelectionInterval(0,0);
			tblVNDTL.setColumnSelectionInterval(0,0);
			cl_dat.M_flgLCUPD_pbst = true;
			if(intCOUNT ==0) // Table is generated for first time only
			{
				try
				{
					M_strSQLQRY = "select count(*) L_CNT from tt_do"+cl_dat.M_strUSRCD_pbst;
					M_rstRSSET = cl_dat.M_conSPDBA_pbst.createStatement().executeQuery(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						if(M_rstRSSET.next())
							L_intRECCT = M_rstRSSET.getInt("L_CNT");
						M_rstRSSET.close();
					}
					if(L_intRECCT >0)
					{
					//	setMSG("Previous details found..",'E');
					//	int L_SELOPT = JOptionPane.showConfirmDialog(this,"Records found,Continue with prv.session ..?","D.O. confirmation ",JOptionPane.YES_NO_OPTION); 
					//	if(L_SELOPT == 1)
					//	{
						M_strSQLQRY = "delete from tt_do"+cl_dat.M_strUSRCD_pbst;
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						cl_dat.exeDBCMT("btnACPDT");
					//	}
					}
				}
				catch(Exception L_E)
				{
					// IF table does not exist error will be there, create the table
					System.out.println("table not found");
					try
					{
						M_strSQLQRY = "create table tt_do"+cl_dat.M_strUSRCD_pbst
						+"(IN_INDNO varchar(8),IN_MATCD varchar(10),CT_MATDS varchar(60),"
						+"CT_UOMCD varchar(3),CT_PKGQT decimal(9,3),IN_PORBY date,"
						+"IN_AUTQT decimal(12,3),IN_PENQT decimal(12,3),IN_DPTCD varchar(3),"
						+"PO_PORNO varchar(8),PO_VENCD varchar(5),PT_PRTNM varchar(40),"
						+"PO_PORQT decimal(12,3),PO_DORQT decimal(12,3))";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						cl_dat.exeDBCMT("btnACPDT");
					}
					catch(Exception L_EX)
					{
						setMSG("Error in Create table ..",'E');
						return;
					}
				}
				intCOUNT++;
			}
			Vector<String> L_vtrDORQT = new Vector<String>();
			String L_strTEMP; /// Temp string to store the contents in Vector
			int L_intCOUNT =0;
			double L_dblDORQT =0;
			for(int i=0;i<tblITDTL.getRowCount();i++)
				if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					L_intCOUNT++;
			if(L_intCOUNT != 1)
			{
				setMSG("Please select one Row in Item details table..",'E');
				return;
			}
			L_intCOUNT =0;
			for(int i=0;i<tblVNDTL.getRowCount();i++)
			{
				if(tblVNDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(tblVNDTL.getValueAt(i,TBL_DORQT).toString().length() ==0)
					{
						setMSG("Enter the quantity ..",'E');
						return;
					}
					else if(tblVNDTL.getValueAt(i,TBL_ARCQT).toString().length() ==0)
					{
						setMSG("Invalid ARC Quantity ..",'E');
						return;
					}
					else if(Double.parseDouble(tblVNDTL.getValueAt(i,TBL_DORQT).toString())>Double.parseDouble(tblVNDTL.getValueAt(i,TBL_ARCQT).toString()))
					{
						setMSG("Quantity can not exceed Pending ARC Quantity ..",'E');
						return;
					}
					L_strTEMP = tblVNDTL.getValueAt(i,TBL_VENCD).toString()+"|"
								+tblVNDTL.getValueAt(i,TBL_VENNM).toString()+"|"
								+tblVNDTL.getValueAt(i,TBL_ARCNO).toString()+"|"
								+tblVNDTL.getValueAt(i,TBL_ARCQT).toString()+"|"
								+tblVNDTL.getValueAt(i,TBL_DORQT).toString();
					L_dblDORQT +=Double.parseDouble(tblVNDTL.getValueAt(i,TBL_DORQT).toString());
					L_intCOUNT++;
				}
			}
			if(L_intCOUNT ==0)
			{
				setMSG("Select at least one vendor ..",'E');
				return;
			}
			try
			{
			for(int i=0;i<tblITDTL.getRowCount();i++)
			{
				if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(L_dblDORQT > Double.parseDouble(tblITDTL.getValueAt(i,TBL_PENQT).toString()))
					{
						setMSG("D.O. qty can not exceed Pending qty..",'E');
						return;
					}
					hstTBLDT.put(tblITDTL.getValueAt(i,TBL_INDNO).toString()+"|"+tblITDTL.getValueAt(i,TBL_MATCD).toString(),L_vtrDORQT);
					for(int j=0;j<tblVNDTL.getRowCount();j++)
					{
						if(tblVNDTL.getValueAt(j,TBL_CHKFL).toString().equals("true"))
						{
							M_strSQLQRY = "Insert into tt_do"+cl_dat.M_strUSRCD_pbst+" values("
							+"'"+cl_dat.M_strCMPCD_pbst+"',"
							+"'"+tblITDTL.getValueAt(i,TBL_INDNO).toString()+"',"
							+"'"+tblITDTL.getValueAt(i,TBL_MATCD).toString()+"',"							
							+"'"+tblITDTL.getValueAt(i,TBL_MATDS).toString()+"',"
							+"'"+tblITDTL.getValueAt(i,TBL_UOMCD).toString()+"',"	
							+tblITDTL.getValueAt(i,TBL_PKGQT).toString()+",";
							if(tblITDTL.getValueAt(i,TBL_PORBY).toString().length() >0)
								M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblITDTL.getValueAt(i,TBL_PORBY).toString()))+"',";
							else
								M_strSQLQRY +="null,";
							M_strSQLQRY +=tblITDTL.getValueAt(i,TBL_AUTQT).toString()+","		
							+tblITDTL.getValueAt(i,TBL_PENQT).toString()+","		
							+"'"+tblITDTL.getValueAt(i,TBL_DPTCD).toString()+"',"	
							+"'"+tblVNDTL.getValueAt(j,TBL_ARCNO).toString()+"',"		
							+"'"+tblVNDTL.getValueAt(j,TBL_VENCD).toString()+"',"		
							+"'"+tblVNDTL.getValueAt(j,TBL_VENNM).toString()+"',"		
							+tblVNDTL.getValueAt(j,TBL_ARCQT).toString()+","		
							+tblVNDTL.getValueAt(j,TBL_DORQT).toString()+")";			
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
					if(!cl_dat.exeDBCMT("btnACPDT"))
					{
						setMSG("Error in accepting details..",'E');
						return;
					}
					btnACPDT.setEnabled(false);
					setMSG("Vendor details have been Accepted..",'N');
					tblVNDTL.clrTABLE();
				}
			}
			}
			catch(Exception L_E)
			{
				setMSG("Error in Accepting ..",'E');
				return;
			}
			L_dblDORQT =0;
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
	}
	public void mousePressed(MouseEvent L_ME)
	{
		super.mousePressed(L_ME);
		M_objSOURC = L_ME.getSource();
		if(M_objSOURC == tblITDTL)
	    {
			for(int i=0;i<tblITDTL.getRowCount();i++)
			{
				if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			    {
					tblITDTL.setValueAt(Boolean.FALSE,i,TBL_CHKFL);
				}
			}
		}
	}
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			super.mouseReleased(L_ME);
			M_objSOURC = L_ME.getSource();
			if(M_objSOURC == tblITDTL)//.cmpEDITR[TBL_CHKFL])
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				for(int i=0;i<tblITDTL.getRowCount();i++)
				{
					if(tblITDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				    {
						if(i >intROWCT)
						{
							setMSG("Invalid Row for selection..",'E');
							return;
						}
						strINDNO = tblITDTL.getValueAt(i,TBL_INDNO).toString();
						strMATCD = tblITDTL.getValueAt(i,TBL_MATCD).toString();
						tblVNDTL.clrTABLE();
						if(hstTBLDT.containsKey((String)strINDNO+"|"+strMATCD))
						{
							setMSG("Item is already Accepted ..",'E');
							btnACPDT.setEnabled(false); // presently disabled
							setCursor(cl_dat.M_curDFSTS_pbst);
							return;
						}
						else
						{
							setMSG("",'N');
							btnACPDT.setEnabled(true);
						}
						// Fetching the distinct Vendor /ARC against the clicked items and pending ARC qty
						M_strSQLQRY ="Select distinct PO_PORNO,PO_VENCD,PT_PRTNM,PO_PORQT,ifnull(PO_PORQT,0)-ifnull(PO_ORDQT,0)-ifnull(PO_FRCQT,0) L_PENQT"
									+" FROM MM_POMST,CO_PTMST WHERE PO_VENCD = PT_PRTCD and PT_PRTTP ='S' and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+M_strSBSCD.substring(2,4) +"'"
									+" AND PO_MATCD ='"+tblITDTL.getValueAt(i,TBL_MATCD).toString()+"'"
									+ " AND PO_PORTP ='03' and PO_STSFL NOT IN('X','O','C')" 
							        +" AND ifnull(PO_PORQT,0)-ifnull(PO_ORDQT,0)-ifnull(PO_FRCQT,0) >0 " ;
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						int j=0;
						if(M_rstRSSET !=null)
						{
							while(M_rstRSSET.next())
							{
								tblVNDTL.setValueAt(M_rstRSSET.getString("PO_VENCD"),j,TBL_VENCD);
								tblVNDTL.setValueAt(M_rstRSSET.getString("PT_PRTNM"),j,TBL_VENNM);
								tblVNDTL.setValueAt(M_rstRSSET.getString("PO_PORNO"),j,TBL_ARCNO);
								tblVNDTL.setValueAt(M_rstRSSET.getString("PO_PORQT"),j++,TBL_ARCQT);
							}
							setMSG("Select the Vendor ..",'N');
							M_rstRSSET.close();
						}
					}
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		
		}
		catch(Exception L_E)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
		    setMSG(L_E,"MouseClick");
		}
	}
	boolean vldDATA()
	{
		return true;		
	}
	void exeSAVE()
	{
		try
		{
			// DEPARTMENT CODE
		  String L_strPRVDT ="",L_strCURDT ="";
		  String L_strPORNO ="",L_strINDNO ="",L_strMATCD ="";
		  Vector<String> L_vtrDORCT = new Vector<String>();
		  cl_dat.M_flgLCUPD_pbst = true;
		 if(!genPORNO())
		 {
			setCursor(cl_dat.M_curDFSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			cl_dat.M_flgLCUPD_pbst = false;
			//setMSG("Error in generating P.O. Number ..",'E');
			return;
		 }
		 String L_strDORNO = strPORNO;
		  M_rstRSSET = cl_dat.exeSQLQRY1("Select distinct PO_VENCD||PO_PORNO L_STRDT from tt_do"+cl_dat.M_strUSRCD_pbst);
		  if(M_rstRSSET !=null)
		  {
			  if(M_rstRSSET.next())
				L_vtrDORCT.addElement(M_rstRSSET.getString("L_STRDT"));	  
			  M_rstRSSET.close();
		  }
		  int L_intRECCT =0;
		  boolean L_flgTXDOC =false; // Flag to check whether tax entry is required or not
		  M_rstRSSET = cl_dat.exeSQLQRY1("Select * from tt_do"+cl_dat.M_strUSRCD_pbst +" order by PO_VENCD,PO_PORNO");
		  if(M_rstRSSET !=null)
		  while(M_rstRSSET.next())
		  {
			  L_flgTXDOC = false;
			  L_strCURDT = M_rstRSSET.getString("PO_VENCD")+M_rstRSSET.getString("PO_PORNO");
			  if(L_intRECCT ==0)
			  {
				  L_flgTXDOC = true;
				  L_strPRVDT = L_strCURDT	 ;
			  }
			 // Generate single P.O. for each Entry in Vector
			  if(!L_strCURDT.equals(L_strPRVDT))
			  {
				  // change the POR nO
				  strPORNO = setNumberFormat(Double.parseDouble(strPORNO) +1,0);
				  L_strDORNO +=","+strPORNO;
				  L_strPRVDT = L_strCURDT;
				  L_flgTXDOC = true;
			  }
			  // for this single P.O. Indent,Item deails are to be added
			  M_strSQLQRY = "INSERT INTO MM_POMST(PO_CMPCD,PO_MMSBS,PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD,"
						+"PO_MATDS,PO_PORDT,PO_AMDNO,PO_SHRDS,PO_BUYCD,PO_ORDRF,"
						+"PO_EFFDT,PO_CMPDT,PO_VENTP,PO_VENCD,PO_CURCD,PO_EXGRT,PO_PORVL,"
						+"PO_QTNTP,PO_QTNNO,PO_QTNDT,PO_PREBY,PO_PREDT,PO_DELTP,PO_DSTCD,"
						+"PO_UOMCD,PO_UCNVL,PO_PORRT,PO_PERRT,PO_PORQT,PO_DELCT,PO_ITVAL,"
						+"PO_TCFFL,PO_INSFL,PO_DPTCD,PO_PMTRF,PO_CRDDY,PO_TRNFL,PO_STSFL,"
						+"PO_LUSBY,PO_LUPDT)"
						+" SELECT PO_CMPCD,'"+M_strSBSCD+"','"+M_strSBSCD +"','"+M_strSBSCD.substring(2,4) +"','01','"
						+ strPORNO+"',"	
					    + "'"+M_rstRSSET.getString("IN_INDNO") +"','"+M_rstRSSET.getString("IN_MATCD")+"',"
						+"PO_MATDS,"
						+"CURRENT_DATE,'00',PO_SHRDS,'"+cl_dat.M_strUSRCD_pbst+"',"
						+ "'"+M_rstRSSET.getString("PO_PORNO") +"',"   
						+"CURRENT_DATE,PO_CMPDT,PO_VENTP,PO_VENCD,PO_CURCD,PO_EXGRT,0,"
						+"PO_QTNTP,PO_QTNNO,PO_QTNDT,'"+cl_dat.M_strUSRCD_pbst+"',CURRENT_DATE,"
						+"PO_DELTP,PO_DSTCD,PO_UOMCD,PO_UCNVL,PO_PORRT,PO_PERRT,"
						+M_rstRSSET.getString("PO_DORQT")//PO_PORQT,
						+",PO_DELCT,0,PO_TCFFL,PO_INSFL,"
						+"'"+M_rstRSSET.getString("IN_DPTCD") +"',"  
						+"PO_PMTRF,PO_CRDDY,'0','O','"+cl_dat.M_strUSRCD_pbst+"',CURRENT_DATE"
						+" FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+M_strSBSCD.substring(2,4)+"'"
						+" AND PO_PORTP ='03' and PO_PORNO ='"+M_rstRSSET.getString("PO_PORNO") +"'"
						+" AND PO_MATCD ='"+M_rstRSSET.getString("IN_MATCD") +"'"
						+" AND PO_VENCD ='"+M_rstRSSET.getString("PO_VENCD") +"'";
			  cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst && L_flgTXDOC)
			{
			  M_strSQLQRY ="INSERT INTO CO_TXDOC (TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_SBSCD,TX_DOCNO,TX_TRNTP,TX_PRDCD,TX_EDCFL,TX_EDCVL,"
			       +" TX_DSBFL, TX_DSBVL,TX_EXCFL, TX_EXCVL,"
				   +" TX_PNFFL, TX_PNFVL,TX_CSTFL, TX_CSTVL,"
				   +" TX_STXFL, TX_STXVL, TX_STXDS,TX_OCTFL, TX_OCTVL,"
				   +" TX_FRTFL, TX_FRTVL,TX_INSFL,TX_INSVL,"
				   +" TX_CDSFL, TX_CDSVL,TX_INCFL,TX_INCVL,TX_ENCFL , TX_ENCVL ,"
				  +" TX_FNIFL , TX_FNIVL ,TX_CDUFL , TX_CDUVL ,"
				  +" TX_CLRFL , TX_CLRVL ,TX_SCHFL , TX_SCHVL ,"
				  +" TX_CVDFL , TX_CVDVL ,TX_WCTFL , TX_WCTVL ,"
				  +" TX_RSTFL , TX_RSTVL ,TX_VATFL , TX_VATVL ,"
				  +" TX_OTHFL , TX_OTHVL , TX_OTHDS,TX_AMDNO,TX_STSFL,TX_TRNFL,TX_LUSBY,TX_LUPDT)" 
				  + " select  TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_SBSCD,"
				  +"'"+strPORNO +"',"			 
				  + "TX_TRNTP,TX_PRDCD,TX_EDCFL,TX_EDCVL,"
			       +" TX_DSBFL, TX_DSBVL,TX_EXCFL, TX_EXCVL,"
				   +" TX_PNFFL, TX_PNFVL,TX_CSTFL, TX_CSTVL,"
				   +" TX_STXFL, TX_STXVL, TX_STXDS,TX_OCTFL, TX_OCTVL,"
				   +" TX_FRTFL, TX_FRTVL,TX_INSFL,TX_INSVL,"
				   +" TX_CDSFL, TX_CDSVL,TX_INCFL,TX_INCVL,TX_ENCFL , TX_ENCVL ,"
				  +" TX_FNIFL , TX_FNIVL ,TX_CDUFL , TX_CDUVL ,"
				  +" TX_CLRFL , TX_CLRVL ,TX_SCHFL , TX_SCHVL ,"
				  +" TX_CVDFL , TX_CVDVL ,TX_WCTFL , TX_WCTVL ,"
				  +" TX_RSTFL , TX_RSTVL ,TX_VATFL , TX_VATVL ,"
				  +" TX_OTHFL , TX_OTHVL , TX_OTHDS,'00',TX_STSFL,'0',"
				  + "'"+cl_dat.M_strUSRCD_pbst +"',"
			  	  +" current_date FROM CO_TXDOC WHERE  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SBSTP ='"+M_strSBSCD.substring(2,4)+"'"
			 	  +" AND TX_DOCNO ='"+M_rstRSSET.getString("PO_PORNO") +"'"
				  +" AND TX_DOCTP ='POR'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
			/*M_strSQLQRY = "INSERT INTO MM_PODEL(POD_MMSBS,POD_STRTP,POD_PORTP,POD_PORNO,POD_MATCD,"
						+ "POD_INDNO,POD_EDLDT,POD_EDLQT,POD_AMDNO,POD_TRNFL,POD_STSFL,POD_LUSBY,POD_LUPDT)"
				        + "SELECT POD_MMSBS,POD_STRTP,'01',"
				        +"POD_PORNO,"
						+"POD_MATCD,POD_INDNO,"
						+"POD_EDLDT,POD_EDLQT,"
								+"POD_AMDNO,POD_TRNFL,POD_STSFL,POD_LUSBY,POD_LUPDT)"
				*/
				L_intRECCT++;		
		  }// end while
		   if(M_rstRSSET !=null)
				M_rstRSSET.close();
		  if(cl_dat.M_flgLCUPD_pbst)
		  {
			   M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CCSVL = '"+strPORNO.substring(3) +"',CMT_CHP01 =''";
				M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"POR'";
				M_strSQLQRY += " AND CMT_SHRDS = 'ARDO'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		  }
		  if(cl_dat.exeDBCMT("exeSAVE"))
		  {
			  setMSG("Saved Sucessfully..",'N');
			  cl_dat.exeSQLUPD("Delete from tt_do"+cl_dat.M_strUSRCD_pbst,"");
			  clrCOMP();
			  JOptionPane.showMessageDialog(this,"D.O. No's : "+L_strDORNO,"D.O Numbers",JOptionPane.INFORMATION_MESSAGE);
		  }
		  else
			  setMSG("Error in Saving..",'E');
		}				
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");  	
		}
	}
	private boolean genPORNO()
	{
		try
		{
			String L_strPORNO  = "",  L_strCODCD = "", L_strCCSVL = "",L_strCHP01 ="",L_strSRLNO="";
			strPORNO ="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"POR' and ";
			M_strSQLQRY += " CMT_SHRDS = 'ARDO'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_strCHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			if(L_strCHP01.trim().length() ==3)
			{
				setMSG("In use,try after some time..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
			L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
			for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
				L_strPORNO += "0";
				
			L_strCCSVL = L_strPORNO + L_strCCSVL;
			L_strSRLNO = L_strCCSVL;
			strPORNO = L_strCODCD + L_strCCSVL;
			if(strPORNO.length() == 8)
				return true;
			else
			{
				setMSG("Error in P.O. number generation..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genPORNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
	}
}
	