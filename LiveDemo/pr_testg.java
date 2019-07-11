/**
System Name   : SPS Recipe Management System
Program Name  : Stores Ledger
Program Desc. : Form for modifying and retrieving details of Stores stock and consumption for given period.
				
Author        : AAP
Date          : 01/07/2003
Version       : SRMS 1.0

Modificaitons 

Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : SRMS 2.0
*/

import javax.swing.*;
//import spl.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.StringTokenizer;

class pr_testg extends cl_pbase 
{
	cl_JTBL			tblSTMST;
	TxtDate			txtSTDAT,
					txtEDDAT;

	pr_testg()
	{
		txtSTDAT=new TxtDate();txtEDDAT=new TxtDate();
		setMatrix(18,4);
		String[] names=new String[]{"FL","Material Code","Description","Manufacturer","Batch No.","Opening Stock","Consumption","Closing Stock","Physical Stock","UOM"};
		int[] wid=new int[]{20,100,150,75,75,75,75,75,75,75};
		tblSTMST=((cl_JTBL)crtTBLPNL(this,names,25,2,1,16,3.9,wid,new int[]{0}));
		tblSTMST.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSTMST.setColumnSelectionAllowed(false);
		tblSTMST.addKeyListener(this);
		for(int i=0;i<tblSTMST.cmpEDITR.length;i++)
			tblSTMST.cmpEDITR[i].addKeyListener(this);
		tblSTMST.clrTABLE();
		tblSTMST.addFocusListener(this);
		add(new JLabel("Start Date : "),1,1,1,1,this,'L');
		add(txtSTDAT,1,2,1,1,this,'L');
		add(new JLabel("End Date : "),1,3,1,1,this,'L');
		add(txtEDDAT,1,4,1,1,this,'L');
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int key=L_KE.getKeyCode();
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==txtSTDAT)
		{
			txtEDDAT.requestFocus();
		}
		else if(M_objSOURC==txtEDDAT)
		{
			getDATA();
		}
	}
	public void exeHLPOK()
	{
		this.setCursor(cl_dat.M_curWTSTS_pbst);
/*		if(M_strHLPFLD.equals("txtRUNNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtRUNNO.setText(L_STRTKN.nextToken());
			txtGRDDS.setText(L_STRTKN.nextToken());
			LM_TRLCT=Integer.parseInt( L_STRTKN.nextToken());
			LM_GRDCD=L_STRTKN.nextToken();
			txtBATNO.requestFocus();
		}
		else if(M_strHLPFLD.equals("txtBATNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtBATNO.setText(L_STRTKN.nextToken());
			txtBATDT.setText(L_STRTKN.nextToken());
			txtBATSZ.setText(L_STRTKN.nextToken());
			txtRCLQT.setText(L_STRTKN.nextToken());
			LM_TRLFL=L_STRTKN.nextToken();
			if(LM_TRLFL.equals("P"))
				cmbPRDTP.setSelectedIndex(1);
			else if(LM_TRLFL.equals("T"))
				cmbPRDTP.setSelectedIndex(0);
			else
				cmbPRDTP.setSelectedIndex(2);
			getDATA();
			cmbPRDTP.requestFocus();
		}
*/		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	boolean vldDATA()
	{
		
		return true;
	}
	void exeSAVE()
	{
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
		if(cl_dat.exeDBCMT("exeSAVE"))
		{
			setMSG("Data saved",'N');
		}
	}
	
	void clrCOMP()
	{
		super.clrCOMP();
		tblSTMST.clrTABLE();
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		tblSTMST.setEnabled(false);tblSTMST.cmpEDITR[8].setEnabled(L_STAT);
	}
	
	void getDATA()
	{
		try
		{
			M_strSQLQRY="SELECT BTT_MTLCD,BTT_MTLMF,BTT_MTLBT,CT_MATDS,SUM(BTT_MTLQT) BTT_MTLQT FROM pr_BTTRN,CO_CTMST"
				+" WHERE BTT_MTLCD=CT_MATCD "
				+" AND BTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BTT_batno in(select distinct bt_batno from pr_btmst where BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND bt_batdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtEDDAT.getText()))+"')"
				+" GROUP BY BTT_MTLCD,BTT_MTLMF,BTT_MTLBT,CT_MATDS";//and bt_batdt between(+"cc_dattm.setDBSDT(txtSTDAT.getText())+","+cc_dattm.setDBSDT(txtEDDAT.getText())+")
			//SELECT  COUNT(*),BTT_MTLCD,BTT_MTLMF,BTT_MTLBT,CT_MATDS,SUM(BTT_MTLQT) BTT_MTLQT FROM spltest/pr_BTTRN,spltest/CO_CTMST WHERE BTT_MTLCD=CT_MATCD AND BTT_batno in(select distinct bt_batno from spltest/pr_btmst where bt_batdt between '07/07/2003'and '08/05/2003') GROUP BY BTT_MTLCD,BTT_MTLMF,BTT_MTLBT,CT_Matds 
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_objSOURC==null)
			{
			}
			else
			{
				int i=0;
				while(M_rstRSSET.next())
				{
					System.out.println(M_rstRSSET.getString("BTT_MTLCD"));
					tblSTMST.setValueAt(M_rstRSSET.getString("BTT_MTLCD"),i,1);
					tblSTMST.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,2);
					tblSTMST.setValueAt(M_rstRSSET.getString("BTT_MTLMF"),i,3);
					tblSTMST.setValueAt(M_rstRSSET.getString("BTT_MTLBT"),i,4);
					tblSTMST.setValueAt(M_rstRSSET.getString("BTT_MTLQT"),i,6);
//					tblSTMST.setValueAt(M_rstRSSET.getString("BTT_MATCD"),i,7);
					i++;
				}
				M_rstRSSET.close();
			}
		}catch(Exception E)
		{System.out.println(E);}
	}
}