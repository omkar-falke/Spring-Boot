/*
System Name   : Material Management System
Program Name  : Tanker Routine Entry Screen
Program Desc. : Entry program for tanker routine slip data.
Author        : Mr. S.R.Mehesare
Date          : 12/11/2005
System        : 
Version       : MMS v2.0.0
Modificaitons : 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.InputVerifier;
import java.awt.Color;
import java.util.Date;
import java.util.Hashtable;

/**
 * <PRE>
 * <B>Program Name : </B>Tanker Routine Entry Screen
 * 
 * <b>Purpose :</b> This module captures details about tanker load routine Here
 * we can be able to get information about routing slip AND add Tanker to
 * perticular routine.
 * 
 * List of tables used : Table Name Primary key Operation done Insert Update
 * Query Delete
 * ----------------------------------------------------------------------------------------------------------
 * MM_WBTRN WB_DOCNO # # QC_SMTRN SMT_QCATP # #
 * ----------------------------------------------------------------------------------------------------------
 * List of fields accepted/displayed on screen : Field Name Column Name Table
 * name Type/Size Description
 * ----------------------------------------------------------------------------------------------------------
 * txtTNKNO WB_TNKNO MM_WBTRN varchar(10) Tanker number tblTNKDT all test Result
 * MM_WBTRN,CO_SMTRN all test related value from TSTNO-0303 tblTSTDT
 * SMT_TMPVL,SMT_SPGVL MM_WBTRN,CO_SMTRN SP.Gravity value Temperature value
 * ----------------------------------------------------------------------------------------------------------
 * 
 * List of fields with help facility : Field Name Display Description Display
 * Columns Table Name
 * ----------------------------------------------------------------------------------------------------------
 * txtTNKNO Tanker number in first table WB_TNKNO MM_WBTRN txtTNKNO1 Tanker
 * number in second table WB_TNKNO MM_WBTRN
 * ----------------------------------------------------------------------------------------------------------
 * Program details:- Table tblTNKDL consist of details about the various tankers
 * arrieved. Table tblTSTDL consist of testing details of leatest selected
 * tanker. <I> <B>Query : </B> Various Tanker Details are taken from MM_WBTRN
 * for condiations :- For Modification. 1) ifnull(WB_STSFL,' ') not in('9','X')"
 * 2) AND ifnull(WB_ACPTG,'')<>'A' 3) AND WB_DOCTP='01' For Addition 1)
 * ifnull(WB_TNKNO,'') ='' 2) AND WB_DOCTP='01' 3) AND ifnull(WB_STSFL,'') NOT
 * IN('9','X') Testing Datails for selected tank are taken form MM_WBTRN &
 * QC_SMTRN tables for Condiation. 1) WB_QLLRF = SMT_TSTNO 2) AND WB_DOCTP='01'
 * 3) AND SMT_QCATP='11' 4) AND ifnull(SMT_STSFL,'')<>'X'" 5) AND WB_DOCNO =
 * Document Number from the Selected Row. Data is updated in the Table
 * 
 * Validations : - Tank Number entered to unload the Tanker must be valid.. </I>
 */
class mm_tetkr extends cl_pbase implements KeyListener, FocusListener 
{   
										/** Final Integer to represent the Check Flag Column of the Tanker Details Table.*/
	private	final int TB1_CHKFL = 0;    /** Final Integer to represent Material Description Column of the Tanker Details Table.*/
	private	final int TB1_MATDS = 1;	/** Final Integer to represent Column to display Lorry Numberof the Tanker Details Table.*/
	private	final int TB1_LRYNO = 2;	/** Final Integer to represent In Time column of the Tanker Details Table.*/
	private	final int TB1_INCTM = 3;	/** Final Integer to represent Column index to display the Gate In Number of the Tanker Details Table.*/	
	private	final int TB1_DOCNO = 4;	/** Final Integer to represent Column index to display the Gate In Number of the Tanker Details Table.*/	
	private	final int TB1_TNKNO = 5;	/** Final Integer to represent Column index to enter Tank Number of the Tanker Details Table.*/
	private	final int TB1_PRDRM = 6;	/** Final Integer to represent Column index to display & to enter Remark By SIC (Control Room) of the Tanker Details Table.*/	
	private	final int TB1_QCARM = 8;	/** Final Integer to represent Column index to select to view Details of the Tanker Details Table.*/
	private	final int TB1_DETAL = 9;
	private	final int TB1_QLTFL = 7;
	
										/** Final Integer to represent the Check Flag Column of the testing Details of Styrene.*/
	private	final int TB2_CHKFL = 0;	/** Final Integer to represent Column index to display Loory number.*/
	private	final int TB2_LRYNO = 1;    /** Final Integer to represent Column index to display Challan Quantity.*/
	private	final int TB2_CHLQT = 2;	/** Final Integer to represent Column index to display RI value.*/
	private	final int TB2_RI_VL = 3;    /** Final Integer to represent Column index to display Benzene value.*/
	private	final int TB2_BZ_VL = 4;    /** Final Integer to represent Column index to display Colour value.*/
	private	final int TB2_COLVL = 5;    /** Final Integer to represent Column index to display Poly value.*/
	private	final int TB2_POLVL = 6;    /** Final Integer to represent Column index to display EB value.*/
	private	final int TB2_EB_VL = 7;    /** Final Integer to represent Column index to display TBC Value.*/
	private	final int TB2_TBCVL = 8;    /** Final Integer to represent Column index to display Toluene value.*/
	private	final int TB2_TOLVL = 9;    /** Final Integer to represent Column index to display XYL value.*/
    private	final int TB2_XYLVL = 10;    /** Final Integer to represent Column index to display Styrene value.*/
	private	final int TB2_STYVL = 11;   /** Final Integer to represent Column index to display ULB value.*/
	private	final int TB2_ULBVL = 12;   /** Final Integer to represent Column index to display UHB value */
	private	final int TB2_UHBVL = 13;   	
										/** Final Integer to represent the Check Flag Column of the testing Details of Raw Materails except Styrene.*/
	private	final int TB3_CHKFL = 0;	/** Final Integer to represent Column index to display Loory Number.*/
	private	final int TB3_LRYNO = 1;    /** Final Integer to represent Column index to display Challan Quantity.*/
	private	final int TB3_CHLQT = 2;	/** Final Integer to represent Column index to display sp Gravity value.*/
	private	final int TB3_SPGVL = 4;    /** Final Integer to represent Column indexto display Temparature value.*/
	private	final int TB3_TMPVL = 5;              
	
	private JPanel pnlSTRYN;
	private JPanel pnlOTHER;
    private JTextField txtTNKNO;
  	private final int intROWCNT = 100;
  	private cl_JTable tblTNKDT;
	private cl_JTable tblTSTDT;
	private cl_JTable tblTSTDT1;
											/** Input verifier for table */
	private TableInputVerifier TBLINPVF;    /** Object for Input verifier  */
	private TBLINPVF objTBLVRF;             /** String for temparary value of input tanker number with respective table */
	private String strTEMP,strTEMP1;        /** final material code value for fetching query */
	private String strMATCD;				/** table tst Row counter */
	private int L_intROWNO=0;               /** table tnk row counter */
	private int L_intROWNO1=0;              /** integer for reccord counter AND storing row counter */	        
	private int intRECCT=0 ,intSELROW=0;
	private Hashtable<String,String> hstMATDS;
	
	private String arrOPRDTM[];
	private String strOPRDRM;// Old remark Entered By Contol Room.
	//DefaultTableCellRenderer cel;
	mm_tetkr()    
	{
		super(2);
		try
		{			
			setMatrix(20,8);				
			//add(lblTNKDT= new JLabel("All Raw Material Tankers"),1,1,1,3,this,'L');
			String[] L_strTBLHD1 = {"Select","Material","Lorry No","Date time","Gate-In No","Tank No.","Remarks","Quality","QCA Remarks","Test Details"};
			int[]L_intCOLSZ = {20,60,90,100,60,70,150,30,150,20};   
			tblTNKDT = crtTBLPNL1(this,L_strTBLHD1,intROWCNT,2,1,9,7.9,L_intCOLSZ,new int[]{TB1_CHKFL,TB1_DETAL});
			tblTNKDT.setCellEditor(TB1_TNKNO, txtTNKNO = new JTextField());//TxtLimit(10));
			txtTNKNO.addFocusListener(this);
			txtTNKNO.addKeyListener(this);
			
			add(new JLabel("Quality Testing Details"),12,1,1,2,this,'L');			
			pnlSTRYN = new  JPanel();
			pnlSTRYN.setLayout(null);
			String[] L_strTBLHD2 = {"Select","Lorry No.","Challan qty","RI","Benzene","Colour","Poly","E-BZ","TBC","Toluene","Xylene","Styrene","L/BOIL","H/BOIL"};// Table Header
			int[]L_intCOLSZ2 = {20,100,80,49,49,49,49,49,49,49,49,49,49,50};			
			tblTSTDT = crtTBLPNL1(pnlSTRYN,L_strTBLHD2,5,1,1,4.6,7.9,L_intCOLSZ2,new int[]{0});
			add(pnlSTRYN,13,1,6,8,this,'L');
						
			pnlOTHER = new  JPanel();				
			pnlOTHER.setLayout(null);
			String[] L_strTBLHD3 = {"Select","Lorry No.","Challan qty","Sp.gravity ","Temprature "};			
			int[]L_intCOLSZ3 = {20,102,80,80,80};   						
			tblTSTDT1 = crtTBLPNL1(pnlOTHER,L_strTBLHD3,5,1,1,4.6,4,L_intCOLSZ3,new int[]{0});
			add(pnlOTHER,13,1,6,8,this,'L');												
			setENBL(false);
			
			hstMATDS =  new Hashtable<String,String>();
			M_strSQLQRY = "Select distinct DP_MATCD,CT_MATDS from MM_DPTRN,CO_CTMST";
			M_strSQLQRY += " where isnull(CT_STSFL,'')<>'X' AND DP_MATCD = CT_MATCD AND isnull(DP_STSFL,'') <>'X' ";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String strTEMP="";
				while(M_rstRSSET.next())
				{
					strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
					if(!strTEMP.equals(""))
						hstMATDS.put(strTEMP,nvlSTRVL(M_rstRSSET.getString("DP_MATCD"),""));
				}
				M_rstRSSET.close();
			}
			objTBLVRF = new TBLINPVF();
			tblTNKDT.setInputVerifier(objTBLVRF);
			arrOPRDTM = new String[intROWCNT];			
			
			//cel = new DefaultTableCellRenderer();
		}   
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}   
	}
	/**
	 * Super Class method overrided to enable & disable the Components.
	 * @param L_flgSTAT boolean argument to pass State of the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		if(L_flgSTAT == true)
		{
			tblTNKDT.setEnabled(false);
			tblTSTDT.setEnabled(false);
			tblTSTDT1.setEnabled(false);
			tblTNKDT.cmpEDITR[TB1_CHKFL].setEnabled(true);
			tblTNKDT.cmpEDITR[TB1_TNKNO].setEnabled(true);
			tblTNKDT.cmpEDITR[TB1_DETAL].setEnabled(true);
			tblTNKDT.cmpEDITR[TB1_PRDRM].setEnabled(true);						
		}		
	}       
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);    
		try 
		{ 
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{				
					if(tblTNKDT.isEditing())
						tblTNKDT.getCellEditor().stopCellEditing();
					tblTNKDT.setRowSelectionInterval(0,0);
					tblTNKDT.setColumnSelectionInterval(0,0);				
					setENBL(true);
					getDATA();
				}
				else
					setENBL(false);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{ 
		super.keyPressed(L_KE);		
		if((L_KE.getKeyCode()== L_KE.VK_F1))
		{
			cl_dat.M_flgHELPFL_pbst = true;	
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC==txtTNKNO)
			{
				intSELROW = tblTNKDT.getSelectedRow();
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtTNKNO";
				M_strSQLQRY =" SELECT TK_TNKNO,TK_MATCD,TK_MATDS from MM_TKMST where"
				+" TK_MATCD='"+hstMATDS.get(tblTNKDT.getValueAt(tblTNKDT.getSelectedRow(),TB1_MATDS).toString()).toString().trim()+"'"
				+" AND isnull(TK_STSFL,' ') <> 'X'";
				if(txtTNKNO.getText().length()>0)
						M_strSQLQRY += " AND TK_TNKNO like '"+txtTNKNO.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Tanker No","Material Code","Description"},3,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}				          
		}
	}
	/**
	 * Super class Method to execuate the F1 help.
	 */
	public void exeHLPOK()
	{		
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtTNKNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;			
			txtTNKNO.setText(cl_dat.M_strHLPSTR_pbst);
		}		
	}
	/**
	 * Method to insert, update & delete Data to / from the database.
	 */
	void exeSAVE()
	{
		if (!vldDATA())
			return;	
		else 
			setMSG("",'E');
		try
		{
			if(tblTNKDT.isEditing())
				tblTNKDT.getCellEditor().stopCellEditing();
			tblTNKDT.setRowSelectionInterval(0,0);
			tblTNKDT.setColumnSelectionInterval(0,0);
			String L_strREMDS = "";			
			/// For Both Addition & modification the Same Query to update tank number may Req.
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;	
				for(int i=0;i<tblTNKDT.getRowCount();i++)
				{
					if(tblTNKDT.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
					{						
						L_strREMDS = tblTNKDT.getValueAt(i,TB1_PRDRM).toString().trim();
						strMATCD = hstMATDS.get(tblTNKDT.getValueAt(i,TB1_MATDS).toString().trim()).toString();
							
						M_strSQLQRY ="UPDATE MM_WBTRN SET ";
						M_strSQLQRY += "WB_TNKNO = '" +tblTNKDT.getValueAt(i,TB1_TNKNO).toString().trim()+"',";
						M_strSQLQRY += "WB_UCLBY = '"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "WB_UCLTM = current_Timestamp";
						//M_strSQLQRY += " WHERE WB_MATCD='"+strMATCD+"' AND WB_DOCTP='01' AND WB_DOCNO = '"+tblTNKDT.getValueAt(i,TB1_DOCNO).toString().trim()+"' AND isnull(WB_STSFL,' ') not in('9','X') AND WB_ACPTG='W'";
						M_strSQLQRY += " WHERE WB_DOCTP='01' AND WB_DOCNO = '"+tblTNKDT.getValueAt(i,TB1_DOCNO).toString().trim()+"' AND isnull(WB_STSFL,' ') not in('9','X') AND WB_ACPTG='W'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						setMSG("Data has been Changed",'N');
						
						strOPRDRM = arrOPRDTM[i];							
						if(!strOPRDRM.equals(L_strREMDS))
						{
							if(strOPRDRM.equals(""))
							{
								M_strSQLQRY = "Insert into MM_RMMST(RM_DOCTP,RM_TRNTP,RM_STRTP,RM_REMTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,RM_LUPDT)values(";
								M_strSQLQRY += "'RS',";
								M_strSQLQRY += "'RS',";
								M_strSQLQRY += "'"+M_strSBSCD.substring(2,4)+"',";
								M_strSQLQRY += "'ULC',";//unloading clearance
								M_strSQLQRY += "'"+ tblTNKDT.getValueAt(i,TB1_DOCNO).toString().trim()+"',";
								M_strSQLQRY += "'"+ L_strREMDS +"',";
								M_strSQLQRY += getUSGDTL("RM",'I',"")+")";								
							}
							else
							{
								M_strSQLQRY = "Update MM_RMMST set ";
								if(!L_strREMDS.equals(""))//update
									M_strSQLQRY += "RM_REMDS ='"+ L_strREMDS +"',"; 
								else if(L_strREMDS.equals(""))//Delete
									M_strSQLQRY += "RM_REMDS = '-',";
								M_strSQLQRY +=" RM_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
								M_strSQLQRY +=" RM_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'";
								M_strSQLQRY += "where RM_STRTP ='01'";
								M_strSQLQRY += " AND RM_REMTP = 'ULC'AND RM_DOCNO ='"+tblTNKDT.getValueAt(i,TB1_DOCNO).toString().trim()+"'";						
							}							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
					}
				}				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{		
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				for(int i=0;i<tblTNKDT.getRowCount();i++)
				{
					if(tblTNKDT.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
					{
						L_strREMDS = tblTNKDT.getValueAt(i,TB1_PRDRM).toString().trim();
						strMATCD = hstMATDS.get(tblTNKDT.getValueAt(i,TB1_MATDS).toString().trim()).toString();
						M_strSQLQRY ="UPDATE MM_WBTRN SET ";
						M_strSQLQRY += "WB_TNKNO = '" + tblTNKDT.getValueAt(i,TB1_TNKNO).toString().trim()+"',";
						M_strSQLQRY += "WB_UCLBY = '"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "WB_UCLTM = current_Timestamp";
					//	M_strSQLQRY += " WHERE WB_MATCD='"+strMATCD+"' AND WB_DOCTP='01' AND WB_DOCNO = '"+tblTNKDT.getValueAt(i,TB1_DOCNO).toString().trim()+"'";
						M_strSQLQRY += " WHERE WB_DOCTP='01' AND WB_DOCNO = '"+tblTNKDT.getValueAt(i,TB1_DOCNO).toString().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						
						if(!L_strREMDS.equals(""))
						{
							M_strSQLQRY = "Insert into MM_RMMST(RM_DOCTP,RM_TRNTP,RM_STRTP,RM_REMTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,RM_LUPDT)values(";
							M_strSQLQRY += "'',";
							M_strSQLQRY += "'',";
							M_strSQLQRY += "'01',";
							M_strSQLQRY += "'ULC',";//unloading clearance
							M_strSQLQRY += "'"+ tblTNKDT.getValueAt(i,TB1_DOCNO).toString().trim()+"',";
							M_strSQLQRY += "'"+ L_strREMDS +"',";
							M_strSQLQRY += getUSGDTL("RM",'I',"")+")";							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
						setMSG("Data has been Saved",'N');
					}
				}				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG(" Data Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					setMSG(" Data Modified Successfully..",'N'); 
				clrCOMP();
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				   setMSG("Error in Modification..",'E'); 
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"exeADDREC");
		}	
	}
	/**
	 * Method to validate the Inputs before execuastion of the SQL Queries.
	 */
	boolean vldDATA()
	{
		try
		{	
            cl_dat.M_flgLCUPD_pbst = true;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				boolean L_flgCHKFL= false;
				
				for(int i=0; i<tblTNKDT.getRowCount(); i++)
				{				
					if (tblTNKDT.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{	
						L_flgCHKFL= true;
						break;
					}		
				}	
				for(int i=0; i<tblTSTDT.getRowCount(); i++)
				{				
					if (tblTSTDT.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					{	
						L_flgCHKFL= true;
						break;
					}		
				}	
				if (L_flgCHKFL== false)
				{
					setMSG("No row Selected..",'E');				
					return false;
				}			
			}
			for(int i=0; i<tblTNKDT.getRowCount(); i++)
			{
				if (tblTNKDT.getValueAt(i,TB1_CHKFL).toString().equals("true"))
				{
					if(tblTNKDT.getValueAt(i,TB1_TNKNO).toString().length()==0)
					{
						setMSG("Tank no can not blank",'E');	
						return false;
					}
				}
			}
			for(int i=0; i<tblTSTDT.getRowCount(); i++)
			{
				if (tblTSTDT.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					if(tblTSTDT.getValueAt(i,TB1_TNKNO).toString().length()==0)
					{
						setMSG("Tank no can not blank",'E');	
						return false;
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_btnSAVE_pbst))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vlddata");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch Tanker Details from the database & display it in the Table.
	 */
	private void getDATA()
	{
		try
		{ 
			java.sql.Timestamp L_tmpTIME;
			ResultSet L_rstRSSET;
			String L_strTEMP="";
			L_intROWNO=0;
						
			M_strSQLQRY  =" Select WB_DOCNO,WB_INCTM,WB_LRYNO,WB_MATCD,WB_MATDS,WB_TNKNO,RM_REMDS,SMT_QLTFL from MM_WBTRN,QC_SMTRN left outer join ";
			M_strSQLQRY  += " QC_RMMST ON RM_QCATP ='11' AND SMT_TSTTP = RM_TSTTP AND SMT_TSTNO = RM_TSTNO AND isnull(RM_STSFL,'') <>'X'"; 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY+= " where WB_QLLRF = SMT_TSTNO AND isnull(WB_STSFL,' ') not in('9','X')" //WB_MATCD='"+strMATCD+"' AND 
					+" AND isnull(WB_ACPTG,'')<>'A' AND WB_DOCTP='01'";
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY+= " where WB_QLLRF = SMT_TSTNO AND isnull(WB_STSFL,'') NOT IN('9','X')"
				+" AND WB_DOCTP='01' AND isnull(WB_TNKNO,'') =''";
			}
			M_strSQLQRY+= " ORDER BY WB_MATDS DESC";
           //	System.out.println(M_strSQLQRY);
           	M_rstRSSET =  cl_dat.exeSQLQRY(M_strSQLQRY);			
			int L_intSRLNO=0;
			String L_strDOCNO = "";
			if(M_rstRSSET !=null)
			{	
				while(M_rstRSSET.next())
				{			
					//cel.setForeground(Color.red);
					
					tblTNKDT.setRowColor(L_intROWNO,Color.black);
					tblTNKDT.setValueAt(new Boolean(false),L_intROWNO,TB1_CHKFL);					
					strTEMP = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"");					
					tblTNKDT.setValueAt(strTEMP,L_intROWNO,TB1_MATDS);
					tblTNKDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),""),L_intROWNO,TB1_LRYNO);
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_INCTM");
					L_strTEMP="";
					if (L_tmpTIME != null)
						L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
					tblTNKDT.setValueAt(L_strTEMP,L_intROWNO,TB1_INCTM);
					L_strDOCNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"");
					tblTNKDT.setValueAt(L_strDOCNO,L_intROWNO,TB1_DOCNO);					
					tblTNKDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_intROWNO,TB1_QCARM);
					if(nvlSTRVL(M_rstRSSET.getString("SMT_QLTFL"),"").equals("N"))
					{
					    tblTNKDT.setValueAt("NOT OK ",L_intROWNO,TB1_QLTFL);					
					    tblTNKDT.setRowColor(L_intROWNO,Color.red);
					}
					else if(nvlSTRVL(M_rstRSSET.getString("SMT_QLTFL"),"").equals("Y"))
					    tblTNKDT.setValueAt("OK ",L_intROWNO,TB1_QLTFL);					
					if(tblTNKDT.getSelectedRow()!= L_intROWNO)
					    tblTNKDT.setSelectionForeground(Color.blue);    
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						tblTNKDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_TNKNO"),""),L_intROWNO,TB1_TNKNO);					
						//SIC REMARK
						M_strSQLQRY = "select RM_REMDS from MM_RMMST where RM_STRTP ='01'";
						M_strSQLQRY += " AND RM_REMTP = 'ULC' AND RM_DOCNO ='"+ L_strDOCNO +"'";
						L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						{
							strOPRDRM = "";
							if(L_rstRSSET != null)
							{
								if(L_rstRSSET.next())							
									strOPRDRM = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
								tblTNKDT.setValueAt(strOPRDRM,L_intROWNO,TB1_PRDRM);																
							}
							arrOPRDTM[L_intROWNO] = strOPRDRM;
							L_rstRSSET.close();
						}								
					}
					tblTNKDT.setValueAt(new Boolean(false),L_intROWNO,TB1_DETAL);
					L_intROWNO++;
				}				
				M_rstRSSET.close();
			}						
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getdata");
		}
	}
	/**
	 * Method to fetch Test Datails for selected Tanker.
	 */
	void getTSTDL(String P_strRPTTP)
	{
		try
		{
			tblTSTDT.clrTABLE();
			if(P_strRPTTP.equals("STY"))
			{					
				pnlOTHER.setVisible(false);
				pnlSTRYN.setVisible(true);
				M_strSQLQRY  ="Select WB_LRYNO,WB_CHLQT,WB_TNKNO,SMT_RI_VL,SMT_BZ_VL,SMT_COLVL,SMT_POLVL,SMT_EB_VL,SMT_TBCVL,SMT_XYLVL,SMT_STYVL,SMT_ULBVL,SMT_UHBVL from MM_WBTRN,QC_SMTRN ";
			}
			else if(P_strRPTTP.equals("OTH"))
			{
				pnlOTHER.setVisible(true);
				pnlSTRYN.setVisible(false);		
				M_strSQLQRY  ="Select WB_LRYNO,WB_CHLQT,WB_TNKNO,SMT_SPGVL,SMT_TMPVL from MM_WBTRN,QC_SMTRN ";
			}			
			M_strSQLQRY+= " where WB_QLLRF = SMT_TSTNO AND WB_DOCTP='01' AND SMT_QCATP='11' AND isnull(SMT_STSFL,'')<>'X'";
			M_strSQLQRY+= " AND WB_DOCNO = '"+ tblTNKDT.getValueAt(tblTNKDT.getSelectedRow(),TB1_DOCNO).toString() +"'";
			M_strSQLQRY+= " AND WB_MATDS = '"+ tblTNKDT.getValueAt(tblTSTDT.getSelectedRow(),TB1_MATDS).toString() +"'";			
			M_rstRSSET =  cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(P_strRPTTP.equals("STY"))
					{						
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),""),0,TB2_LRYNO);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),""),0,TB2_CHLQT);					
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_RI_VL"),""),0,TB2_RI_VL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_BZ_VL"),""),0,TB2_BZ_VL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_COLVL"),""),0,TB2_COLVL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_POLVL"),""),0,TB2_POLVL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_EB_VL"),""),0,TB2_EB_VL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_TBCVL"),""),0,TB2_TBCVL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_XYLVL"),""),0,TB2_XYLVL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_STYVL"),""),0,TB2_STYVL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_ULBVL"),""),0,TB2_ULBVL);
						tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_UHBVL"),""),0,TB2_UHBVL);
					}
					else if(P_strRPTTP.equals("OTH"))
					{						
						tblTSTDT1.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),""),L_intROWNO1,TB3_LRYNO);
						tblTSTDT1.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),""),L_intROWNO1,TB3_CHLQT);
						tblTSTDT1.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_SPGVL"),""),L_intROWNO1,TB3_SPGVL);
						tblTSTDT1.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_TMPVL"),""),L_intROWNO1,TB3_TMPVL);
					}
					L_intROWNO1++;
				}				
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSTDL");
		}
	}
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try  
			{				
				if(P_intCOLID==TB1_TNKNO)
				{
					strTEMP = tblTNKDT.getValueAt(tblTNKDT.getSelectedRow(),TB1_TNKNO).toString();
					if(strTEMP.length() == 0)
						return true;
					if((strTEMP.indexOf("'") >=0)||(strTEMP.indexOf("\"") >=0)||(strTEMP.indexOf(" ") >=0)||(strTEMP.indexOf(",") >=0)||(strTEMP.indexOf("\\") >=0)||(strTEMP.indexOf("/") >=0))
					{
						setMSG("Characters like \', \", \\, /, blank & , are not allowed in Tank number field",'E');
						return false;
					}
					if(strTEMP.length()>1)
					{
						M_strSQLQRY =" SELECT TK_TNKNO from MM_TKMST where TK_MATCD='"+hstMATDS.get(tblTNKDT.getValueAt(tblTNKDT.getSelectedRow(),TB1_MATDS).toString()).toString().trim()+"' AND isnull(TK_STSFL,' ') <> 'X'";
						M_strSQLQRY +=" AND TK_TNKNO ='"+strTEMP+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET==null)
						{
							M_rstRSSET.close();	
							setMSG("No Data found ..",'E');
							return false;				
						}
						if(M_rstRSSET.next())						
							M_rstRSSET.close();														
						else
						{
							M_rstRSSET.close();	
				    		setMSG("No Data found ..",'E');
				    		return false;				
						}
					}
				}					
				//if(P_intCOLID == TB1_DETAL)
				//{					
					setMSG("",'N');
					int L_intROWNO = tblTNKDT.getSelectedRow();
					for (int i =0; i< intROWCNT;i++)
						tblTNKDT.setValueAt(new Boolean(false),i,TB1_DETAL);
					tblTNKDT.setValueAt(new Boolean(true),L_intROWNO,TB1_DETAL);
					
					String L_strTEMP = tblTNKDT.getValueAt(tblTNKDT.getSelectedRow(),TB1_MATDS).toString();
					if(L_strTEMP.length()>3)
						L_strTEMP = L_strTEMP.substring(0,3).toUpperCase();
					
					 String L_strDOCNO = tblTNKDT.getValueAt(tblTNKDT.getSelectedRow(),TB1_DOCNO).toString();
					 String L_strLRYNO = tblTNKDT.getValueAt(tblTNKDT.getSelectedRow(),TB1_LRYNO).toString();
					
					if((L_strDOCNO.trim().length() != 0) && (L_strLRYNO.trim().length() != 0))
					{
						if(L_strTEMP.equals("STY"))
							getTSTDL("STY");
						else
							getTSTDL("OTH");
						return true;
					}
				//}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"TBLInput verifire");
				return false;
			}
			return true;
		}
	}			
}		