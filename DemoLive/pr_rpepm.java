
/**
System Name   : SPS Recipe Management System
Program Name  : Recipe Master
Program Desc. : Form for adding, modifying and retrieving details of Grade Run & recipe
				
Author        : AAP
Date          : 15/05/2003
Version       : SRMS 1.0

Modificaitons 

Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : SRMS 2.0
*/

import javax.swing.*;
import javax.swing.tree.*;
//import spl.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

class pr_rpepm extends cl_pbase
{
	TxtLimit	txtGRDCD,//Grade code
		txtRNETM,//RUN END TIME HH:MM
		txtRNSTM;//RUN START TIME HH:MM
	TxtNumLimit txtRUNNO;//RUN NUMBER
	TxtDate		txtRNEDT,//RUN END DATE DD
				txtRNSDT;//RUN START DATE
	cl_JTBL		tblRECIP;//RECIPE TABLE
	JScrollPane srpRECIP;//SCROLL PANE FOR RECIPE TABLE
	TxtNumLimit txtBHSIZ,//BATCH SIZE
				txtPRQTY,//QUANTITY TO BE PRODUCED
				txtBHCNT,//NO. OF BATCHES TO BE FORMULATED
				txtBATNO;//BATCH NO.
	JButton		btnSTFNL;//BUTTON TO FINALISE RECIPE FROM TRIAL RECIPES.
	DefaultCellEditor dceTBLTXT;
	JFormattedTextField M_txtDATE_pbst;
	
	pr_rpepm()
	{
		txtGRDCD=new TxtLimit(10);txtRNETM=new TxtLimit(5);txtRUNNO=new TxtNumLimit(8);txtRNEDT=new TxtDate();
		txtRNSTM=new TxtLimit(5);txtRNSDT=new TxtDate();txtBHSIZ=new TxtNumLimit(4.3);txtPRQTY=new TxtNumLimit(5.3);
		txtBHCNT=new TxtNumLimit(2);M_txtDATE_pbst=new JFormattedTextField(new java.util.Date());
		btnSTFNL=new JButton("Set Final Recipe");
		String[] names=new String[]{"FL","Material Code","Description","Grade","Manufacturer","Lot No.","PHR","%age","Qty in batch","UOM"};
		int[] wid=new int[]{20,100,150,75,75,75,75,75,75,40};
		tblRECIP=crtTBLPNL(new JPanel(null),names,10,0,25,M_intCOLWD*4,150,wid,new int[]{0}) ;
		tblRECIP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		dceTBLTXT=new DefaultCellEditor(txtBHSIZ);
		dceTBLTXT.setClickCountToStart(0);
		tblRECIP.getColumn(tblRECIP.getColumnName(1)).setCellEditor(dceTBLTXT);
		
		tblRECIP.clrTABLE();
		int h1=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
		int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		srpRECIP=new JScrollPane(tblRECIP,v1,h1);
		JPanel ptemp=new JPanel(new BorderLayout());
		//ptemp.add(srpRECIP);
		setMatrix(1,1);
		add(srpRECIP,1,1,1,1,ptemp,'L');
		ptemp.setBorder(BorderFactory.createTitledBorder(" Recipe "));
		setMatrix(18,4);
		add(new JLabel("Grade code : "),1,1,1,1,this,'L');
		add(txtGRDCD,1,2,1,1,this,'L');
		add(new JLabel("Run Start Date/Time : "),1,3,1,1,this,'L');
		add(txtRNSDT,1,4,1,0.5,this,'L');
		add(txtRNSTM,1,4,1,0.5,this,'R');
		add(new JLabel("Run No. : "),2,1,1,1,this,'L');
		add(txtRUNNO,2,2,1,1,this,'L');
		add(new JLabel("Run End Date/Time"),2,3,1,1,this,'L');
		add(txtRNEDT,2,4,1,0.5,this,'L');
		add(txtRNETM,2,4,1,0.5,this,'R');
		add(new JLabel("Batch Size : "),3,1,1,1,this,'L');
		add(txtBHSIZ,3,2,1,1,this,'L');
		add(ptemp,4,1,6,4,this,'L');
		add(btnSTFNL,10,1,1,1,this,'L');
		add(new JLabel("Total Quantity to be produced : "),11,1,1,1,this,'L');
		add(txtPRQTY,11,2,1,1,this,'L');
		add(new JLabel("No. of batches : "),12,1,1,1,this,'L');
		add(txtBHCNT,12,2,1,1,this,'L');
		add(M_txtDATE_pbst,12,3,1,1,this,'L');
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);		
		//M_objSOURC=L_AE.getSource();
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		//M_objSOURC=L_KE.getSource();
		int key=L_KE.getKeyCode();
		if(M_objSOURC==txtGRDCD&&key==L_KE.VK_F1)
		{
			M_strSQLQRY="SELECT PR_PRDDS, PR_PRDCD FROM CO_PRMST WHERE PR_PRDDS LIKE 'SP%' ORDER BY PR_PRDDS";
			M_strHLPFLD = "txtGRDCD";
			cl_hlp(M_strSQLQRY,1,1,new String[] {"Grade Name","Grade Code"},2,"CT");
		}
	}

	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtGRDCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				txtGRDCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}catch(Exception L_EX)
		{
			System.out.println("Exception in Recipe Master exeHLPOK : "+L_EX);
		}
	}
}

