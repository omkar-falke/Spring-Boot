/*
System Name   : Laboratory Information Management System
Program Name  : Quality Monitoring
Program Desc. : 
Author        : N. K. Virdi
Date          : 24 sept 2003
Version       : LIMS 3.0 (jdk 1.4)

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.Hashtable;

class qc_qrmon extends cl_pbase implements MouseListener, FocusListener, KeyListener, ActionListener {
	private JPanel pnlTSTDTL,pnlRSDTL,pnlSMTDTL,pnlREBDTL,pnlTBCDTL,pnlGRBDTL;
	private JPanel pnlEFWDTL,pnlEXWDTL,pnlUWDTL,pnlWTTST,pnlFOMDTL,pnlSMTKDT,pnlTNKTST;
	private cl_JTBL tblTSTDTL,tblRSDTL,tblSMTDTL,tblREBDTL,tblTBCDTL,tblFOMDTL,tblGRBDTL;
	private cl_JTBL tblEFWDTL,tblEXWDTL,tblUWDTL,tblFOMDT,tblSMTKDT;
	private JTabbedPane jtpMANTB,jtpSBTB1,jtpSBTB2;    
	private JLabel lblDATE;
	private JTextField txtDATE;

	private Hashtable<String,String> hstQPRNG = new Hashtable<String,String>();	// hastable for quality para. range
	private Vector<String> vtrQPRLS,						//  vector for quality para.
			       vtrQPRDS,						//	vector for quality para desc
			       vtrQPUOM, 						//	vector for UOM
		               vtrWTCOD,						//	Vector for desc of water codes
		               vtrWTDES;						//  Vector for water codes
	private String strQRYTM1 = "",					// Time1 for query range
				   strQRYTM2 = "",					// Time2 for query range
				   strINPDT   = "",					// variable for input date 
				   strPRVDT  = "";					// previous value of date is stored
	private int intRWCNT = 50,						// Row count
				intCOUNT = 0;						// integer counter

													// flags for checking,the corresponding details.
	boolean flgQCDTL = true,						// QC details 	
			flgRSDTL = true,						// Reactor sample
			flgSMDTL = true,						// Styrene test details
			flgREBDT = true,						// REB test details
			flgTBCDT = true,						// TBC test details
			flgFOMDT = true,						// FO/MO test details
			flgSMTDT = true,						// Styrene tanker test details
			flgGRBDT = true,						// Grab test details
			flgEFWDT = true,						// Effulent water test 
			flgEXWDT = true,						// Extra water
			flgUWDTL = true;						// Utility water
	/**
	 *	 
	 */
	qc_qrmon()
	{
		super(2);
		try
		{
			pnlTSTDTL = new JPanel(null);
			pnlRSDTL = new JPanel(null);
			pnlSMTDTL = new JPanel(null);
			pnlREBDTL = new JPanel(null);
			pnlTBCDTL = new JPanel(null);
			pnlFOMDTL = new JPanel(null);
			pnlSMTKDT = new JPanel(null);
			pnlGRBDTL = new JPanel(null);
			pnlEFWDTL = new JPanel(null);
			pnlEXWDTL = new JPanel(null);
			pnlUWDTL = new JPanel(null);
			pnlWTTST = new JPanel(null);
			pnlTNKTST = new JPanel(null);
			setMatrix(8,4);
			add(new JLabel("Quey Date"),1,1,1,1,this,'L');
			add(txtDATE=new TxtDate(),1,2,0.5,1,this,'L');
			jtpMANTB=new JTabbedPane();
			jtpSBTB1=new JTabbedPane();
			jtpSBTB2=new JTabbedPane();
			jtpMANTB.add(pnlGRBDTL,"Grab Test");
			jtpMANTB.add(pnlRSDTL,"Reactor Sample");
			jtpMANTB.add(pnlREBDTL,"REB Details");
			jtpMANTB.add(pnlTBCDTL,"TBC Removal Bed");
			jtpMANTB.add(jtpSBTB2,"Tank / Tankers");
			jtpMANTB.add(pnlTSTDTL,"Finished Goods");
			jtpMANTB.add(jtpSBTB1,"Water Test");
			jtpSBTB1.add(pnlEFWDTL,"Effluent Analysis");
			jtpSBTB1.add(pnlEXWDTL,"Extra Water Analysis");
			jtpSBTB1.add(pnlUWDTL,"Utility Water Analysis");
			jtpSBTB2.add(pnlSMTDTL,"SM TANKS");
			jtpSBTB2.add(pnlSMTKDT,"SM TANKERS");
			jtpSBTB2.add(pnlFOMDTL,"FO/MO TANKERS");
		
			add(jtpMANTB,2,1,6,4,this,'L');
			updateUI();
			addLSTN1();
			vtrQPRLS = new  Vector<String>();
			vtrQPUOM = new Vector<String>();
			vtrQPRDS = new  Vector<String>();
			vtrWTCOD = new  Vector<String>();
			vtrWTDES = new Vector<String>();
	//		txtDATE.setText(cc_dattm.occ_dattm.getABDATE(cl_dat.cl_dat.M_strLOGDAT_pbst,1,'B'));
			txtDATE.requestFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"qc_qrmon");
		}
	}
	/**
	 * 
	 */
	void addLSTN1()
	{ 
		txtDATE.addFocusListener(this);
		txtDATE.addKeyListener(this);
		txtDATE.addActionListener(this);
		txtDATE.setActionCommand("txtDATE");
		jtpMANTB.addMouseListener(this);
		jtpSBTB2.addMouseListener(this);
		jtpSBTB1.addMouseListener(this);
	}
	void addLSTN()
	{
		
	}
	public void focusGained(FocusEvent L_FE)
	{
		if(M_objSOURC ==txtDATE)
		{
			try
			{
				setMSG("Please click on the corresponding tab for which you wish to query..",'N');
				strINPDT = txtDATE.getText().trim();
				strQRYTM2 = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim() +" "+"23:59"));
				strQRYTM1 = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim() +" "+"00:01"));
			}
			catch(Exception L_E)
			{
				
			}
			
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			strINPDT = txtDATE.getText().toString().trim();
			if(L_KE.getKeyCode() == L_KE.VK_ENTER || L_KE.getKeyCode() == L_KE.VK_TAB)
			{
				if(M_objSOURC == txtDATE)
				{
					if(M_fmtLCDAT.parse(strINPDT).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
						setMSG("Date can not be greater than today's date..",'E');
						else
						{
							flgQCDTL = true;
							flgRSDTL = true;
							flgSMDTL = true;
							flgREBDT = true;
							flgTBCDT = true;
							flgFOMDT = true;
							flgSMTDT = true;
							flgGRBDT = true;
							flgEFWDT = true;
							flgEXWDT = true;
							flgUWDTL = true;
							
							strQRYTM2 = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim() +" "+"23:59"));
							strQRYTM1 = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim() +" "+"00:01"));
							jtpMANTB.setSelectedIndex(0);
						}
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"keyPressed");
		}
	}
	public void mouseClicked(MouseEvent L_ME){
		try{
			super.mouseClicked(L_ME);
			strINPDT = txtDATE.getText().toString().trim();
			if(strINPDT.equals(""))
			{
				setMSG("Please enter the query date..",'E');
				return;
			}
			if(!strINPDT.equals(strPRVDT))
			{
				strPRVDT =strINPDT;
				flgQCDTL = true;
				flgRSDTL = true;
				flgSMDTL = true;
				flgREBDT = true;
				flgTBCDT = true;
				flgFOMDT = true;
				flgSMTDT = true;
				flgGRBDT = true;
				flgEFWDT = true;
				flgEXWDT = true;
				flgUWDTL = true;
				strQRYTM2 = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim() +" "+"23:59"));
				strQRYTM1 = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim() +" "+"00:01"));
			
			 }
			if(L_ME.getSource().equals(jtpMANTB))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				if(jtpMANTB.getSelectedIndex() == 0)
				{
					if(flgGRBDT)
					{System.out.println("grabtest clicked ");
						getGRBREC();
					}
				}
				else if(jtpMANTB.getSelectedIndex() == 1)
				{
					if(flgRSDTL)
					{
						getRSDTL();
					}
				}
				else if(jtpMANTB.getSelectedIndex() == 2)
				{
					if(flgREBDT)
					{
						getREBREC();
					}
				}
				else if(jtpMANTB.getSelectedIndex() == 3)
				{
					if(flgTBCDT)
					{
						
						getTBCREC();
					}
				}
				else if(jtpMANTB.getSelectedIndex() == 4)
				{
					if(jtpSBTB2.getSelectedIndex() == 0)
					{
						if(flgSMDTL)
						{
							getSMTANK();
						}
					}
					
				}
				else if(jtpMANTB.getSelectedIndex() == 5)
				{
					if(flgQCDTL)
					{
						getTSTREC();
					}
				}
				else if(jtpMANTB.getSelectedIndex() == 6)
				{
					if(pnlEFWDTL !=null)
					{
						pnlEFWDTL.removeAll();
						pnlEFWDTL.updateUI();
					}
						getEFWANY();
						pnlEFWDTL.updateUI();
				
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == jtpSBTB2)
			{
				if(jtpSBTB2.getSelectedIndex() == 0)
				{
					if(flgSMDTL)
					{
						if(pnlSMTDTL !=null)
						{
							pnlSMTDTL.removeAll();
							pnlSMTDTL.updateUI();
						}
						getSMTANK();
					}
				}
				if(jtpSBTB2.getSelectedIndex() == 1)
				{
					if(flgSMTDT)
					{
						if(pnlSMTKDT !=null)
						{
							pnlSMTKDT.removeAll();
							pnlSMTKDT.updateUI();
						}
						getSMTKDT();
					}
				}
			   else if(jtpSBTB2.getSelectedIndex() == 2)
			   {
			   		if(flgFOMDT)
			   		{
			   			if(pnlFOMDTL !=null)
			   			{
			   				pnlFOMDTL.removeAll();
			   				pnlFOMDTL.updateUI();
			   			}
			   			getFOMREC();
			   		}
			   }
			}
			else if(M_objSOURC == jtpSBTB1)
			{
				if(jtpSBTB1.getSelectedIndex() == 0)
				{
					if(pnlEFWDTL !=null)
					{
						pnlEFWDTL.removeAll();
						pnlEFWDTL.updateUI();
					}
						getEFWANY();
						pnlEFWDTL.updateUI();
				}
				if(jtpSBTB1.getSelectedIndex() == 1)
				{
					if(pnlEXWDTL !=null)	
					{
						pnlEXWDTL.removeAll();
						pnlEXWDTL.updateUI();
					}
						getEXWANY();
						pnlEXWDTL.updateUI();
				}
			   else if(jtpSBTB1.getSelectedIndex() == 2)
			   {
				   if(pnlUWDTL !=null)
				   {
			   			pnlUWDTL.removeAll();
						pnlUWDTL.updateUI();
				   }
						getUWANY();
						pnlUWDTL.updateUI();
			   }
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"mouseClicked");
		}
	}
	private void clrTBLDT(JTable P_TBLNM)
	{
		if(P_TBLNM !=null)
		{
			int intRWCNT = P_TBLNM.getRowCount();
			int intCLCNT = P_TBLNM.getColumnCount();
			for(int i=0;i<(intRWCNT);i++)
			{
				for(int j=0;j<=(intCLCNT-1);j++)
				{
					P_TBLNM.setValueAt(" ",i,j);
				}
			}
		}
	}
	private void getTSTREC(){
		try{
			Vector<String> vtrTSTDT = new Vector<String>();
			flgQCDTL = false;
			String L_strTSTDS = "";
			String L_strTSTTP = "";
			intCOUNT = 0;
		
			M_strSQLQRY = "Select ps_lotno,ps_rclno,ps_tsttp,ps_tstdt,ps_dspvl,ps_mfivl,ps_izovl,ps_vicvl,";
			M_strSQLQRY += "ps_ts_vl,ps_el_vl,ps_rsmvl,ps_wi_vl,ps_a__vl,ps_b__vl,ps_y1_vl,lt_prdcd,lt_clsfl,pr_prdds ";
			M_strSQLQRY += "from co_prmst,pr_ltmst,qc_psmst where pr_prdcd = lt_prdcd and lt_cmpcd = ps_cmpcd and lt_lotno = ps_lotno and lt_rclno = ps_rclno";
			M_strSQLQRY += " and PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ps_stsfl <> 'X' ";
			M_strSQLQRY += " and ps_tstdt between ";
			M_strSQLQRY += "'"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
			M_strSQLQRY += " order by ps_tsttp,ps_lotno,ps_rclno,ps_tstdt";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_lotno"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_rclno"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
				L_strTSTTP = nvlSTRVL(M_rstRSSET.getString("ps_tsttp"),"");
				L_strTSTDS = "";
				if(L_strTSTTP.equals("0101"))
					L_strTSTDS = "Grab";
				else if(L_strTSTTP.equals("0103"))
					L_strTSTDS = "Comp.";
				else if(L_strTSTTP.equals("0104"))
					L_strTSTDS = "Bag ";
				vtrTSTDT.addElement(L_strTSTDS);
				//vtrTSTDT.addElement(M_fmtLCDTM.format(M_fmtQRDTM.parse(nvlSTRVL(M_rstRSSET.getString("ps_tstdt"),""))));
				vtrTSTDT.addElement(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("ps_tstdt")));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_dspvl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_mfivl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_izovl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_vicvl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_ts_vl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_el_vl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_rsmvl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_wi_vl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_a__vl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_b__vl"),""));
				vtrTSTDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_y1_vl"),""));
				intCOUNT++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			String[] L_staTBLHD = {"Lot No.","R.No.","Grade","Type","Test Date","DSP","MFI","IZO","VIC","TS","EL","RSM","WI","a","b","Y1"};
			if(intCOUNT > 0)
			{
				if(pnlTSTDTL !=null)
				{
					pnlTSTDTL.removeAll();
					pnlTSTDTL.updateUI();
				}
				int[] L_inaCOLSZ = {70,40,80,40,110,40,35,35,40,35,35,35,35,40,40,40};
				int j = 0;
				//tblTSTDTL = crtTBLPNL(pnlTSTDTL,L_staTBLHD,intCOUNT,20,400,350,450,L_inaCOLSZ,new int[]{});
				tblTSTDTL=(cl_JTBL)crtTBLPNL(pnlTSTDTL,L_staTBLHD,intCOUNT,1,1,4,3.95,L_inaCOLSZ,new int[]{0});
				tblTSTDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				tblTSTDTL.setEnabled(false);
				vtrTSTDT.trimToSize();
				for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
				{
					for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
					{
						if(vtrTSTDT.elementAt(j).equals("0"))
							tblTSTDTL.setValueAt("",L_intRWCNT,L_intCOLCT);
						else
							tblTSTDTL.setValueAt(vtrTSTDT.elementAt(j),L_intRWCNT,L_intCOLCT);
						j++;
					}
				}
				setMSG(" ",'N');
			}
			else
			{
				clrTBLDT(tblTSTDTL);
				setMSG("No Record exists....",'E');
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"getTSTREC");
		}
	}
	private void getRSDTL(){
		try{
			Vector<String> vtrRSDTL = new Vector<String>();
			flgRSDTL = false;
			intCOUNT = 0;
			
			M_strSQLQRY = "Select * from qc_rsmst where ";
			M_strSQLQRY += " RS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rs_tstdt between ";
			M_strSQLQRY += "'"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
			M_strSQLQRY += " and rs_stsfl <> 'X'";
			M_strSQLQRY += " order by rs_tstdt";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				vtrRSDTL.addElement(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("rs_tstdt")));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_10101"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_10102"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_10103"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_10104"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_11101"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_11102"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_11103"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_12101"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_12102"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_12103"),""));
				vtrRSDTL.addElement(nvlSTRVL(M_rstRSSET.getString("rs_12104"),""));
				intCOUNT++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			String[] L_staTBLHD = {"Test Date","10/101","10/102","10/103","10/104","11/101","11/102","11/103","12/101","12/102A","12/102B","12/103"};
			if(intCOUNT > 0)
			{
				if(pnlRSDTL !=null)
				{
					pnlRSDTL.removeAll();
					pnlRSDTL.updateUI();
				}
				int[] L_inaCOLSZ = {100,60,60,60,60,60,60,60,60,60,60,60};
				int j = 0;
			//	tblRSDTL = crtTBLPNL(pnlRSDTL,L_staTBLHD,intCOUNT,20,400,750,300,L_inaCOLSZ,new int[]{});
				tblRSDTL=(cl_JTBL)crtTBLPNL(pnlRSDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
				tblRSDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				tblRSDTL.setEnabled(false);
				vtrRSDTL.trimToSize();
				for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
				{
					for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
					{
						if(vtrRSDTL.elementAt(j).toString().trim().equals("0.0"))
						   tblRSDTL.setValueAt("-",L_intRWCNT,L_intCOLCT);
						else
						{
							if(vtrRSDTL.elementAt(j).toString().trim().equals("0"))
								tblRSDTL.setValueAt("",L_intRWCNT,L_intCOLCT);
							else
								tblRSDTL.setValueAt(vtrRSDTL.elementAt(j),L_intRWCNT,L_intCOLCT);
						}
						j++;
					}
				}
			}
			else
			{
				clrTBLDT(tblRSDTL);
				setMSG("No Record exists....",'E');
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"getRSDTL");
		}
	}
private void getSMTANK()
{
		try{
			Vector<String> vtrSMTDT = new Vector<String>();
			flgSMDTL = false;
			String L_strTSTTP = "0302",L_strTSTRF ="";
			intCOUNT = 0;
		    
			M_strSQLQRY = "Select * from qc_smtrn where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND smt_tsttp ='"+L_strTSTTP +"'";
			M_strSQLQRY += " and smt_stsfl <> 'X'  ";
			M_strSQLQRY += " and smt_tstdt between ";
			M_strSQLQRY += "'"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
			M_strSQLQRY += " order by smt_tstrf,smt_tstdt";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				
				L_strTSTRF = nvlSTRVL(M_rstRSSET.getString("SMT_TSTRF"),"");
			    vtrSMTDT.addElement(nvlSTRVL(cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXLOT",L_strTSTTP.trim() +"_"+L_strTSTRF.trim()),""));
				vtrSMTDT.addElement(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SMT_TSTDT")));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_COLVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_RI_VL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TBCVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_POLVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_BZ_VL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TOLVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_EB_VL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_XYLVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_BHDVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_STYVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_ULBVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_UHBVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_QLTFL"),""));
				intCOUNT++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			String[] L_staTBLHD = {"Tank Ref.","Test Date","COL","RI","TBC","POL","BZ","TOL","EB","XYL","BHD","STY","ULB","UHB","QLT(OK)"};
			if(intCOUNT > 0)
			{
				if(pnlSMTDTL !=null)
				{
					pnlSMTDTL.removeAll();
					pnlSMTDTL.updateUI();
				}
				int[] L_inaCOLSZ = {80,100,40,50,40,40,50,50,50,50,50,50,40,40,50};
				int j = 0;
				//tblSMTDTL = crtTBLPNL(pnlSMTDTL,L_staTBLHD,intCOUNT,20,400,750,300,L_inaCOLSZ,new int[]{});
				tblSMTDTL=(cl_JTBL)crtTBLPNL(pnlSMTDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
				tblSMTDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				tblSMTDTL.setEnabled(false);
				vtrSMTDT.trimToSize();
				for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
				{
					for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
					{
						if(vtrSMTDT.elementAt(j).toString().trim().equals("0"))
							tblSMTDTL.setValueAt("",L_intRWCNT,L_intCOLCT);
						else
							tblSMTDTL.setValueAt(vtrSMTDT.elementAt(j),L_intRWCNT,L_intCOLCT);
						j++;
					}
				}
				setMSG(" ",'N');
			}
			else
			{
				clrTBLDT(tblSMTDTL);
				setMSG("No Record exists....",'E');
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"getSMTANK");
		}
}
private void getREBREC()
{
	try
	{
		Vector<String> vtrREBDT = new Vector<String>();
		flgREBDT = false;
		String L_strTSTTP = "0203",L_strTSTRF ="";
		intCOUNT = 0;
		
		M_strSQLQRY = "Select * from qc_smtrn where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND smt_stsfl <> 'X' and smt_tsttp ='"+L_strTSTTP +"'";
		M_strSQLQRY += " and smt_tstdt between ";
		M_strSQLQRY += "'"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
		M_strSQLQRY += " order by smt_tstrf,smt_tstdt";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
			L_strTSTRF = nvlSTRVL(M_rstRSSET.getString("SMT_TSTRF"),"");
			vtrREBDT.addElement(nvlSTRVL(cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXLOT",L_strTSTTP.trim() +"_"+L_strTSTRF.trim()),""));
			vtrREBDT.addElement(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SMT_TSTDT")));
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_COLVL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_BZ_VL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TOLVL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_EB_VL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_XYLVL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_STYVL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_BHDVL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_ULBVL"),"").trim());
			vtrREBDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_UHBVL"),"").trim());
			intCOUNT++;
		}
		if(M_rstRSSET != null)
			M_rstRSSET.close();
		String[] L_staTBLHD = {"Line","Test Date","COL","BZ","TOL","EB","XYL","STY","BHD","ULB","UHB"};
		if(intCOUNT > 0)
		{
			if(pnlREBDTL !=null)
			{
				pnlREBDTL.removeAll();
				pnlREBDTL.updateUI();
			}
					
			int[] L_inaCOLSZ = {80,120,60,60,60,60,60,60,60,60,65};
			int j = 0;
			//tblREBDTL = crtTBLPNL(pnlREBDTL,L_staTBLHD,intCOUNT,20,400,750,300,L_inaCOLSZ,new int[]{});
			tblREBDTL=(cl_JTBL)crtTBLPNL(pnlREBDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
			tblREBDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblREBDTL.setEnabled(false);
			vtrREBDT.trimToSize();
			for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
			{
				for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
				{
					if(vtrREBDT.elementAt(j).toString().trim().equals("0"))
						tblREBDTL.setValueAt("",L_intRWCNT,L_intCOLCT);
					else
						tblREBDTL.setValueAt(vtrREBDT.elementAt(j),L_intRWCNT,L_intCOLCT);
					j++;
				}
			}
			setMSG(" ",'N');
		}
		else
		{
			clrTBLDT(tblREBDTL);
			setMSG("No Record exists....",'E');
		}
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"getREBREC");
	}
}
private void getTBCREC()
{
	try
	{
		Vector<String> vtrTBCDT = new Vector<String>();
		flgTBCDT = false;
		String L_strTSTTP = "0202",L_strTSTRF ="";
		intCOUNT = 0;
		
		M_strSQLQRY = "Select * from qc_smtrn where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND smt_stsfl <> 'X' and smt_tsttp ='"+L_strTSTTP +"'";
		M_strSQLQRY += " and smt_tstdt between '"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
		M_strSQLQRY += " order by smt_tstdt,smt_tstrf";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
			vtrTBCDT.addElement(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SMT_TSTDT")));
			L_strTSTRF = nvlSTRVL(M_rstRSSET.getString("SMT_TSTRF"),"").trim();
			vtrTBCDT.addElement(cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXLOT",L_strTSTTP.trim() +"_"+L_strTSTRF.trim()));
			vtrTBCDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_COLVL"),"").trim());
			vtrTBCDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TBCVL"),"").trim());
			vtrTBCDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_MOIVL"),"").trim());
			vtrTBCDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_EB_VL"),"").trim());
			vtrTBCDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_POLVL"),"").trim());
			vtrTBCDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_STYVL"),"").trim());
			
			intCOUNT++;
		}
		if(M_rstRSSET != null)
			M_rstRSSET.close();
		String[] L_staTBLHD = {"Test Date","Sample Point","COL","TBC","MOI","EB","POL","STY"};
		if(intCOUNT > 0)
		{
			if(pnlTBCDTL !=null)
			{
				pnlTBCDTL.removeAll();
				pnlTBCDTL.updateUI();
			}
			int[] L_inaCOLSZ = {120,120,70,70,70,70,70,70};
			int j = 0;
			//tblTBCDTL = crtTBLPNL(pnlTBCDTL,L_staTBLHD,intCOUNT,20,400,750,300,L_inaCOLSZ,new int[]{});
			tblTBCDTL=(cl_JTBL)crtTBLPNL(pnlTBCDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
			tblTBCDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblTBCDTL.setEnabled(false);
			vtrTBCDT.trimToSize();
			for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
			{
				for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
				{
					if(vtrTBCDT.elementAt(j).toString().trim().equals("0"))
						tblTBCDTL.setValueAt("",L_intRWCNT,L_intCOLCT);
					else
					tblTBCDTL.setValueAt(vtrTBCDT.elementAt(j),L_intRWCNT,L_intCOLCT);
					j++;
				}
			}
			setMSG(" ",'N');
		}
		else
		{
			clrTBLDT(tblTBCDTL);
			setMSG("No Record exists....",'E');
		}
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"getTBCREC");
	}
}
private void getFOMREC()
{
	try
	{
		Vector<String> vtrFOMDT = new Vector<String>();
		flgFOMDT = false;
		String L_strTSTTP = "0301";
		intCOUNT = 0;
		
		M_strSQLQRY = "Select SMT_PRDCD,SMT_TSTDT,SMT_TSTRF,SMT_TMPVL,SMT_SPGVL,PR_PRDDS from CO_PRMST,qc_smtrn where PR_PRDCD = SMT_PRDCD AND SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND smt_stsfl <> 'X' and smt_tsttp ='"+L_strTSTTP +"'";
		M_strSQLQRY += " and smt_tstdt between '"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
		M_strSQLQRY += " order by smt_tstdt,smt_tstrf";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
			vtrFOMDT.addElement(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SMT_TSTDT")));
			vtrFOMDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TSTRF"),""));
			vtrFOMDT.addElement(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
			vtrFOMDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TMPVL"),"").trim());
			vtrFOMDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_SPGVL"),"").trim());
			
			intCOUNT++;
		}
		if(M_rstRSSET != null)
			M_rstRSSET.close();
		String[] L_staTBLHD = {"Test Date","Tanker No.","Of","TMP","SPG"};
		if(intCOUNT > 0)
		{
			int[] L_inaCOLSZ = {120,120,100,80,80};
			int j = 0;
			//tblFOMDTL = crtTBLPNL(pnlFOMDTL,L_staTBLHD,intCOUNT,20,400,750,300,L_inaCOLSZ,new int[]{});
			tblFOMDTL=(cl_JTBL)crtTBLPNL(pnlFOMDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
			tblFOMDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblFOMDTL.setEnabled(false);
			vtrFOMDT.trimToSize();
			for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
			{
				for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
				{
					if(vtrFOMDT.elementAt(j).toString().trim().equals("0"))
						tblFOMDTL.setValueAt("",L_intRWCNT,L_intCOLCT);
					else
					tblFOMDTL.setValueAt(vtrFOMDT.elementAt(j),L_intRWCNT,L_intCOLCT);
					j++;
				}
			}
			setMSG(" ",'N');
		}
		else
		setMSG("No Record exists....",'E');
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"getFOMREC");
	}
}
private void getSMTKDT()
{
		try{
			Vector<String> vtrSMTDT = new Vector<String>();
			flgSMDTL = false;
			String L_strTSTTP = "0303",L_strTSTRF ="";
			intCOUNT = 0;
			
			M_strSQLQRY = "Select SMT_TSTRF,SMT_TSTDT,SMT_TNKCT,SMT_TKLCD,SMT_COLVL,SMT_RI_VL,SMT_TBCVL,SMT_POLVL,SMT_BZ_VL,SMT_TOLVL,";
			M_strSQLQRY +="SMT_EBSVL,SMT_XYSVL,SMT_STYVL,SMT_ULBVL,SMT_UHBVL,SMT_QLTFL from qc_smtrn ";
			M_strSQLQRY += " where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND smt_stsfl <> 'X' AND smt_tsttp ='"+L_strTSTTP +"'";
			M_strSQLQRY += " and smt_tstdt between ";
			M_strSQLQRY += "'"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
			M_strSQLQRY += " order by smt_tstrf,smt_tstdt";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TNKCT"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TKLCD"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_COLVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_RI_VL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TBCVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_POLVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_BZ_VL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_TOLVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_EBSVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_XYSVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_STYVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_ULBVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_UHBVL"),""));
				vtrSMTDT.addElement(nvlSTRVL(M_rstRSSET.getString("SMT_QLTFL"),""));
				intCOUNT++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			String[] L_staTBLHD = {"Tanker Count","From","COL","RI","TBC","POL","BZ","TOL","EB","XYL","STY","ULB","UHB","QLT(OK)"};
			if(intCOUNT > 0)
			{
				int[] L_inaCOLSZ = {80,70,50,50,50,50,50,50,50,50,50,50,50,60};
				int j = 0;
			//	tblSMTKDT = crtTBLPNL(pnlSMTKDT,L_staTBLHD,intCOUNT,20,400,750,300,L_inaCOLSZ,new int[]{});
			tblSMTKDT=(cl_JTBL)crtTBLPNL(pnlSMTKDT,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
			tblSMTKDT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblSMTKDT.setEnabled(false);
				vtrSMTDT.trimToSize();
				for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
				{
					for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
					{
						if(vtrSMTDT.elementAt(j).toString().trim().equals("0"))
							tblSMTKDT.setValueAt("",L_intRWCNT,L_intCOLCT);
						else
							tblSMTKDT.setValueAt(vtrSMTDT.elementAt(j),L_intRWCNT,L_intCOLCT);
						j++;
					}
				}
				setMSG(" ",'N');
			}
			else
				setMSG("No Record exists....",'E');
		}catch(Exception L_EX){
			setMSG(L_EX,"getSMTKDT");
		}
}
private void getGRBREC()
{
		setMSG("Data fetching in Progress....",'N');
		try
		{
			Vector<String> vtrGRBDT = new Vector<String>();
			flgGRBDT = false;
			String L_strTSTTP = "0101";
			intCOUNT = 0;
			
			M_strSQLQRY = "Select PS_LINNO,PS_TSTDT,PS_DSPVL,PS_MFIVL,PS_IZOVL,PS_TS_VL,PS_EL_VL,PS_VICVL,PS_RP1VL,";
			M_strSQLQRY +="PS_RP2VL,PS_RP3VL,PS_RP4VL,PS_A__VL,PS_B__VL,PS_Y1_VL,PS_WI_VL,PS_MN_VL,PS_MW_VL,";
			M_strSQLQRY +="PS_MZ_VL,PS_GL1VL,PS_GL2VL,PS_GL3VL FROM QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ps_stsfl <> 'X'";
			M_strSQLQRY += " and ps_tsttp ='"+L_strTSTTP.trim() +"'";
			M_strSQLQRY += " and ps_rclno ='00' and ps_tstdt between ";
			M_strSQLQRY += "'"+strQRYTM1 +"' and '"+ strQRYTM2+"'";
			M_strSQLQRY += " order by ps_linno,ps_tstdt";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_linno"),""));
				vtrGRBDT.addElement(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("ps_tstdt")));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_dspvl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_mfivl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_izovl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_ts_vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_el_vl"),""));
			    vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_vicvl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_rp1vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_rp2vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_rp3vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_rp4vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_a__vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_b__vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_y1_vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_wi_vl"),""));
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_mn_vl"),""));	
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_mw_vl"),""));	
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_mz_vl"),""));	
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_gl1vl"),""));	
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_gl2vl"),""));	
				vtrGRBDT.addElement(nvlSTRVL(M_rstRSSET.getString("ps_gl3vl"),""));	
				intCOUNT++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			int[] L_inaCOLSZ = {30,100,30,30,30,30,30,35,30,30,30,30,35,35,35,30,30,30,30,30,30,30};
			String[] L_staTBLHD = {"Line","Test Date","DSP","MFI","IZO","TS","EL","VIC","RP1","RP2","RP3","RP4","a","b","Y1","WI","Mn","Mw","Mz","GLS20","GLS60","GLS80"};
			if(intCOUNT > 0)
			{
//				int[] L_inaCOLSZ = {30,100,20,20,20,20,20,25,20,20,20,20,20,20,20,20,20,20,20,20,20,20};
				int j = 0;
				if(pnlGRBDTL !=null)
				{
					pnlGRBDTL.removeAll();
					pnlGRBDTL.updateUI();
				}
				//tblGRBDTL = crtTBLPNL(pnlGRBDTL,L_staTBLHD,intCOUNT,20,400,750,350,L_inaCOLSZ,new int[]{});
				tblGRBDTL=(cl_JTBL)crtTBLPNL(pnlGRBDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
				tblGRBDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				tblGRBDTL.setEnabled(false);
				vtrGRBDT.trimToSize();
				for(int L_intRWCNT = 0;L_intRWCNT < intCOUNT;L_intRWCNT++)
				{
					for(int L_intCOLCT = 0;L_intCOLCT < L_staTBLHD.length;L_intCOLCT++)
					{
						if(vtrGRBDT.elementAt(j).equals("0"))
						  tblGRBDTL.setValueAt("",L_intRWCNT,L_intCOLCT);
						 else
  						  tblGRBDTL.setValueAt(vtrGRBDT.elementAt(j),L_intRWCNT,L_intCOLCT);
						j++;
					}
				}
				setMSG(" ",'N');
			}
			else
			{
				clrTBLDT(tblGRBDTL);
				setMSG("No Record exists....",'E');
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"getGRBREC");
		}
	}
private void getTSTDET(String P_TSTPAR)
{
	String L_strQPRCD ="";
	ResultSet L_rstRSSET;
	if(vtrQPRLS !=null)
		vtrQPRLS.removeAllElements();
	if(vtrQPRDS !=null)
		vtrQPRDS.removeAllElements();
	if(vtrQPUOM !=null)
		vtrQPUOM.removeAllElements();
	M_strSQLQRY = "Select CMT_CODCD,CMT_NMP02 from CO_CDTRN where CMT_CGMTP = ";
	M_strSQLQRY += "'"+"RPT"+"' and CMT_CGSTP = '";
	M_strSQLQRY += P_TSTPAR.trim() + "'";
	M_strSQLQRY += " order by CMT_NMP02"; 
	try
	{
		L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(L_rstRSSET !=null)
		while (L_rstRSSET.next())
		{
			L_strQPRCD = L_rstRSSET.getString("CMT_CODCD").trim();
			vtrQPRLS.addElement(L_strQPRCD);
			if(P_TSTPAR.equals("QCXXUWA"))
				vtrQPRDS.addElement(cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXQPR",L_strQPRCD).trim());
			else
				vtrQPRDS.addElement(nvlSTRVL(cl_dat.getPRMCOD("CMT_SHRDS","SYS","QCXXQPR",L_strQPRCD),""));
			vtrQPUOM.addElement(nvlSTRVL(cl_dat.getPRMCOD("CMT_CCSVL","SYS","QCXXQPR",L_strQPRCD),""));
		}
		if(L_rstRSSET !=null)
			L_rstRSSET.close();
	}catch(SQLException L_SE){
			System.out.println("Exception:" + L_SE.toString());}		
	}

	private void getWATCOD(String L_strTSTTP){
		if(vtrWTCOD !=null)
			vtrWTCOD.removeAllElements();
		if(vtrWTDES !=null)
			vtrWTDES.removeAllElements();
		M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from co_cdtrn where cmt_cgmtp='SYS'and CMT_CGSTP = 'QCXXWTP'";
		M_strSQLQRY = M_strSQLQRY + " and CMT_MODLS like '%" + L_strTSTTP + "%'";
		try{
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while (M_rstRSSET.next()){
				vtrWTCOD.addElement(M_rstRSSET.getString("CMT_CODCD").trim());
				vtrWTDES.addElement(M_rstRSSET.getString("CMT_CODDS").trim());
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}catch(SQLException L_SE){
			System.out.println("Exception"+L_SE.toString());
		}
	}
	private void getEFWANY(){
		String L_strUMVAL =""	;
		try{
			ResultSet L_rstRSLSET ;
			String L_strSQLQR ="";
			getTSTDET("QCXXEFA");
			getWATCOD("0401");
			if(vtrQPRLS !=null)
				vtrQPRLS.trimToSize();
			if(vtrQPRLS !=null)
				vtrQPRDS.trimToSize();
			String L_staUMVAL [][] = new String[vtrQPRLS.size()][vtrWTCOD.size()];
			getQPRRNG();
		    M_strSQLQRY = "Select distinct WTT_QCATP,WTT_TSTTP,WTT_TSTNO From QC_WTTRN Where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_TSTDT ";
            M_strSQLQRY += " Between '" + strQRYTM1 + "' And '"+ strQRYTM2 + "' AND WTT_TSTTP = '0401'";
            M_strSQLQRY += " AND WTT_STSFL <> 'X'";
         	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
           if(M_rstRSSET !=null)
				if(M_rstRSSET.next()){
				for(int i=0;i<vtrWTCOD.size();i++){
                   L_strSQLQR = "Select * FROM QC_WTTRN where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_QCATP = '" ;
				   L_strSQLQR +=  nvlSTRVL(M_rstRSSET.getString("WTT_QCATP"),"").trim() + "'And WTT_TSTTP ='" + nvlSTRVL(M_rstRSSET.getString("WTT_TSTTP"),"").trim() + "'And WTT_TSTNO ='" + nvlSTRVL(M_rstRSSET.getString("WTT_TSTNO"),"").trim() + "'And WTT_WTRTP ='" + ((String)vtrWTCOD.elementAt(i)).trim()+ "'" ;
				   L_strSQLQR +=" AND WTT_STSFL <> 'X'";
                   L_rstRSLSET = cl_dat.exeSQLQRY1(L_strSQLQR);
					if(L_rstRSLSET !=null)
					while(L_rstRSLSET.next()){
						for(int j=0;j<vtrQPRLS.size();j++){
							String L_strQPRCD = "WTT_" + ((String)(vtrQPRLS.elementAt(j))).toString().trim()+ "VL";
						L_strUMVAL = L_rstRSLSET.getString(L_strQPRCD);
						if(L_strUMVAL !=null)	
						   L_staUMVAL [j][i] =  L_strUMVAL.trim();////.trim()
						}
					
					}
					if(L_rstRSLSET !=null)
					L_rstRSLSET.close();	
				}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
			}
		   String[] L_staTBLHD= {"Parameter","Unit","MPCB Limits","Effluent Eq.Tank","Effluent Guard pond","Effluent Non org. pond"};
		   int[] L_inaCOLSZ =  {100,80,100,120,120,140};
		   //	tblEFWDTL = crtTBLPNL(pnlEFWDTL,L_staTBLHD,vtrQPRLS.size(),20,400,750,300,L_inaCOLSZ,new int[]{});
	       tblEFWDTL=(cl_JTBL)crtTBLPNL(pnlEFWDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
		   tblEFWDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		   tblEFWDTL.setEnabled(false);
		   for(int i=0;i<vtrQPRLS.size();i++)
			{
				tblEFWDTL.setValueAt(vtrQPRDS.elementAt(i).toString().trim(),i,0);
				tblEFWDTL.setValueAt(vtrQPUOM.elementAt(i).toString().trim(),i,1);
				tblEFWDTL.setValueAt((String)hstQPRNG.get(vtrQPRLS.elementAt(i).toString().trim()),i,2);
				for(int j=0;j<vtrWTCOD.size();j++)
				 {
					if(L_staUMVAL[i][j] !=null)
					{
					    if(L_staUMVAL[i][j].trim().length()>0)
					    {
					 		if(Double.valueOf(L_staUMVAL [i][j].trim()).doubleValue()!=0.0)
					 			tblEFWDTL.setValueAt(L_staUMVAL [i][j].trim(),i,j+3);
					 		else
							{
								if(cl_dat.M_strSYSLC_pbst.equals("02"))
									tblEFWDTL.setValueAt("-",i,j+3);
								else
									tblEFWDTL.setValueAt("ND",i,j+3);
							}
					 	}else
					 	tblEFWDTL.setValueAt("-",i,j+3);	
					}
					else 
					    tblEFWDTL.setValueAt("-",i,j+3);		
				 }
			}	
		}catch(Exception E){
			System.out.println("efa:" + E.toString());
		}
	}
	private void getQPRRNG(){
	
		M_strSQLQRY = "Select QP_QPRCD,QP_NPFVL,QP_NPTVL from CO_QPMST where QP_QCATP ='01'"; 
    	M_strSQLQRY = M_strSQLQRY + " and QP_PRDCD = '6800000001'";
		ResultSet L_rstRSLSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		try{
			if(M_rstRSSET !=null)
			while(L_rstRSLSET.next()){
				String L_strQPRCD = L_rstRSLSET.getString("QP_QPRCD").trim();
				String L_NPFVL = L_rstRSLSET.getString("QP_NPFVL").trim();
				String L_NPTVL = L_rstRSLSET.getString("QP_NPTVL").trim();
				if(Double.valueOf(L_NPFVL).doubleValue()!=0.0){
					if(!L_strQPRCD.equals("DO_"))
						hstQPRNG.put(L_strQPRCD,Double.valueOf(L_NPFVL).toString()+ "-" +Double.valueOf(L_NPTVL).toString());
					else
							hstQPRNG.put(L_strQPRCD,"min"+ " " +Double.valueOf(L_NPFVL).toString());
				}
                else
				{
						hstQPRNG.put(L_strQPRCD,Double.valueOf(L_NPTVL).toString());
				}
			}
			if(L_rstRSLSET !=null)
				L_rstRSLSET.close();
		}catch (SQLException e){
          setMSG("Invalid SQL Statement ... getQPRRNG",'E');
	    }
	}
	private void getEXWANY()
	{
		try
		{
			int L_intCNT =0;
			String L_strSQLQR ="";
			String L_strQPRCD ="";
			ResultSet L_rstRSLSET;
			String L_strDTMVL = " ";
			String[] L_staTBLHD= {"SAMPLE","TIME","PH","HAR","TSS","FE","DO","TBD","PO4"};
		    int[] L_inaCOLSZ =  {140,75,75,75,75,75,75,75,75};
			//tblEXWDTL = crtTBLPNL(pnlEXWDTL,L_staTBLHD,vtrQPRLS.size(),20,400,750,300,L_inaCOLSZ,new int[]{});
			tblEXWDTL=(cl_JTBL)crtTBLPNL(pnlEXWDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
			tblEXWDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblEXWDTL.setEnabled(false);
			getTSTDET("QCXXEWA");
			getWATCOD("0402");
			vtrQPRLS.trimToSize();
			M_strSQLQRY = "Select * From QC_WTTRN Where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_TSTDT ";
            M_strSQLQRY += " between '" + strQRYTM1 + "' and '"+ strQRYTM2 + "' AND WTT_TSTTP = '0402' ";
            M_strSQLQRY += " AND WTT_STSFL <> 'X'";
            M_strSQLQRY += "order by WTT_WTRTP";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
		        while(M_rstRSSET.next())
				{
					L_strSQLQR = "Select CMT_CODDS from co_cdtrn where cmt_cgmtp='SYS'and CMT_CGSTP = 'QCXXWTP'and CMT_CODCD = '";
					L_strSQLQR += M_rstRSSET.getString("WTT_WTRTP").trim()+ "'";
					L_rstRSLSET = cl_dat.exeSQLQRY1(L_strSQLQR);
					if(L_rstRSLSET.next())
						tblEXWDTL.setValueAt(nvlSTRVL(L_rstRSLSET.getString("CMT_CODDS"),"").trim(),L_intCNT,0);
					L_strDTMVL = M_fmtLCDTM.format(M_rstRSSET.getTimestamp("WTT_TSTDT"));
					if(L_strDTMVL != null)
					{
						if(L_strDTMVL.trim().length()>0)
							tblEXWDTL.setValueAt(L_strDTMVL.substring(11,16).trim(),L_intCNT,1);
							
					}
					for(int j=0;j<vtrQPRLS.size();j++)
					{
						L_strQPRCD = "WTT_" + ((String)(vtrQPRLS.elementAt(j))).trim()+ "VL";
						L_strQPRCD = M_rstRSSET.getString(L_strQPRCD);
						if(L_strQPRCD !=null)
						{
							L_strQPRCD = L_strQPRCD.trim();
							if(L_strQPRCD.length()>0){
							if(Double.valueOf(L_strQPRCD).doubleValue()!=0.0)
								tblEXWDTL.setValueAt(L_strQPRCD,L_intCNT,j+2);
								
							else
							{
								if(cl_dat.M_strSYSLC_pbst.equals("02"))
									tblEXWDTL.setValueAt("-",L_intCNT,j+2);
								else
									tblEXWDTL.setValueAt("ND",L_intCNT,j+2);
								
							}
						}else
							tblEXWDTL.setValueAt("-",L_intCNT,j+2);
						}
						else
							tblEXWDTL.setValueAt("-",L_intCNT,j+2);
							
					}
					if(L_rstRSLSET !=null)
						L_rstRSLSET.close();
					L_intCNT++;
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}catch(Exception E){
			System.out.println("ext:" + E);
		}
	}
	private void getUWANY()
	{
		String L_strUMVAL ="";
		try
		{
			String[] L_staTBLHD= {"Parameter","Unit","Softner Wt.","Boiler Feed","Bolier Blow Dn.","Cooling Water(O)","Cooling Water(N)","Process Wt.","Raw Water"};
		    int[] L_inaCOLSZ =  {90,70,80,80,80,90,90,80,80};
			getTSTDET("QCXXUWA");
			getWATCOD("0403");
			String L_staUMVAL [][] = new String[vtrQPRLS.size()][vtrWTCOD.size()];
		  //	tblUWDTL = crtTBLPNL(pnlUWDTL,L_staTBLHD,vtrQPRLS.size(),20,400,750,300,L_inaCOLSZ,new int[]{});
			tblUWDTL=(cl_JTBL)crtTBLPNL(pnlUWDTL,L_staTBLHD,intCOUNT,1,1,2,3.95,L_inaCOLSZ,new int[]{0});
			tblUWDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblUWDTL.setEnabled(false);
			M_strSQLQRY = "Select distinct WTT_QCATP,WTT_TSTTP,WTT_TSTNO From QC_WTTRN Where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_TSTDT "; //WTT_TSTTP = '0403'and WTT_TSTNO = '10100006'" ;
            M_strSQLQRY += " Between '" + strQRYTM1.trim() + "' And '"+ strQRYTM2.trim() + "' AND WTT_TSTTP = '0403'";
            M_strSQLQRY += " AND WTT_STSFL <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				vtrWTCOD.trimToSize();
				for(int i=0;i<vtrWTCOD.size();i++)
				{
                    String L_strSQLQR = "Select * FROM QC_WTTRN where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_QCATP = '" + M_rstRSSET.getString("WTT_QCATP").trim() + "'And WTT_TSTTP ='" + M_rstRSSET.getString("WTT_TSTTP").trim() + "'And WTT_TSTNO ='" + M_rstRSSET.getString("WTT_TSTNO").trim() + "'And WTT_WTRTP ='" + ((String)vtrWTCOD.elementAt(i)).trim()+ "'" ;
					L_strSQLQR += " AND WTT_STSFL <> 'X'";
					ResultSet L_rstRSLSET = cl_dat.exeSQLQRY1(L_strSQLQR);
					if(M_rstRSSET !=null)
					while(L_rstRSLSET.next())
					{
						for(int j=0;j<vtrQPRLS.size();j++)
						{
							String L_strQPRCD = "WTT_" + ((String)(vtrQPRLS.elementAt(j)).toString().trim())+ "VL";
							L_strUMVAL = L_rstRSLSET.getString(L_strQPRCD);
							if(L_strUMVAL !=null)
								L_staUMVAL [j][i] =  L_strUMVAL.trim();
						}
					}
					if(L_rstRSLSET !=null)
						L_rstRSLSET.close();
				}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
			}
		    for(int i=0;i<vtrQPRLS.size();i++)
			{
				String L_QPRTST = ((String)(vtrQPRDS.elementAt(i))).trim();
				if(L_QPRTST.trim().length()> 13)
					L_QPRTST.substring(13);
				tblUWDTL.setValueAt(L_QPRTST.trim(),i,0);
				tblUWDTL.setValueAt(vtrQPUOM.elementAt(i).toString().trim(),i,1);
				for(int j=0;j<vtrWTCOD.size();j++)
				{
					if(L_staUMVAL[i][j]!=null)
				   {
						if(L_staUMVAL[i][j]!=null && L_staUMVAL[i][j].trim().length()>0)
						{
							if(Double.valueOf(L_staUMVAL [i][j]).doubleValue()!=0.0)
								tblUWDTL.setValueAt(L_staUMVAL [i][j],i,j+2);
							else
							{
								if(cl_dat.M_strSYSLC_pbst.equals("02"))
									tblUWDTL.setValueAt("-",i,j+2);
								else
									tblUWDTL.setValueAt("ND",i,j+2);
							}
						}
						else
							tblUWDTL.setValueAt("-",i,j+2);
					}
					 else 
							tblUWDTL.setValueAt("-",i,j+2);
								
				}
			}	
		
		}catch(Exception E){
			System.out.println("uwa:" + E);}
		
	}
}
