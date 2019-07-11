/**
System Name   : Human Resource Management System
Program Name  : Training Transaction
Program Desc. : Form for entering and reading Training details
				
Author        : AAP
Date          : 20/03/2003
Version       : HRMS 1.0

Modificaitons 

Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : HRMS 2.0

//			As per joining form given by Mr. VVG
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

class hr_tetgt extends cl_pbase implements ChangeListener
{
	String[]		LM_GRDCD=new String[] {"Management","Non-Management"};

	JComboBox	cmbTRGCD /** For training title and code */,
				cmbDPTCD /** For training title and code */,
				cmbGRDCD /** For training title and code */;
	JCheckBox	chbDPT,
				chbGRD;
	JLabel		lblDPT,
				lblGRD;
	cl_JTBL tblEMPNM /** For training title and code */;
	JScrollPane	srpEMPNM /** For training title and code */;
	
	hr_tetgt()
	{
		super(1);
		cmbTRGCD=new JComboBox();cmbDPTCD=new JComboBox();cmbGRDCD=new JComboBox(LM_GRDCD);
		
		chbDPT=new JCheckBox("Departmentwise");chbGRD=new JCheckBox("Gradewise");
		chbDPT.addChangeListener(this);chbGRD.addChangeListener(this);
		lblDPT=new JLabel("Select Department");lblGRD=new JLabel("Select Grade");
		
		tblEMPNM=crtTBLPNL(new JPanel(null),new String[]{"FL","Employee Name"},10,0,25,M_intCOLWD*4,150,new int[]{20,300},new int[]{0}) ;
		
		srpEMPNM=new JScrollPane(tblEMPNM);
		
		setMatrix(20,4);
		add(new JLabel("Select Training"),1,1,1,1,this,'L');
		add(cmbTRGCD,1,2,1,3,this,'L');
		add(new JLabel("Select Criteria"),3,1,1,1,this,'L');
		add(chbDPT,3,2,1,1,this,'L');
		add(lblDPT,3,3,1,1,this,'L');
		lblDPT.setVisible(false);
		add(cmbDPTCD,3,4,1,1,this,'L');
		cmbDPTCD.setVisible(false);
		add(chbGRD,4,2,1,1,this,'L');
		add(lblGRD,4,3,1,1,this,'L');
		lblGRD.setVisible(false);
		add(cmbGRDCD,4,4,1,1,this,'L');
		cmbGRDCD.setVisible(false);
		add(srpEMPNM,6,1,10,4,this,'L');
	}
	public void stateChanged(ChangeEvent L_CE)
	{
		if(L_CE.getSource()==chbDPT)
		{
			lblDPT.setVisible(!lblDPT.isVisible());
			cmbDPTCD.setVisible(!cmbDPTCD.isVisible());
		}
		else if(L_CE.getSource()==chbGRD)
		{
			lblGRD.setVisible(!lblGRD.isVisible());
			cmbGRDCD.setVisible(!cmbGRDCD.isVisible());
		}
	}/*
	public void actionPerformed(ActionEvent L_AE)
	{
		
		f.setCursor(cl_dat.M_curWTSTS_pbst);
		
		String L_ACTCMD = L_AE.getActionCommand();
		System.out.println("Actionp : Meepm  " + L_ACTCMD);
		if(L_AE.getSource().equals(cl_dat.M_cmbOPTN_pbst))
		{
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPSEL_pbst))
			{
				clearAll();
				setAllEnabled(false);
				System.out.println("sel");
			}
			else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
			{
				clearAll();
				setAllEnabled(true);
				System.out.println("add");
			}
			
			else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPDEL_pbst))
			{
			    setAllEnabled(false);
				txtEMPNO.setEnabled(true);
				System.out.println("del");
			}
		    else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst))
			{
			    setAllEnabled(true);
				System.out.println("mod");
			}
			else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPENQ_pbst))
			{
			    setAllEnabled(false);
				txtEMPNO.setEnabled(true);
			}
	
		}
		else if(L_AE.getSource().equals(cl_dat.M_btnUNDO_pbst))
		{
			System.out.println("M_btnUNDO_pbst");
			clearAll();
			setAllEnabled(false);
		
		}
		else if(L_AE.getSource().equals(cl_dat.M_btnSAVE_pbst))
		{
			System.out.println("M_btnSAVE_pbst");
		}
		else if(L_AE.getSource()==M_btnSAVE_pbstT)
		{
			System.out.println("SAVET   "+tbpMAIN.getSelectedIndex());
		}
		f.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	public void keyTyped(KeyEvent L_KE)
	{
		System.out.println(" KeyTyped  "+L_KE.getComponent());
		if (L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			L_KE.getComponent().transferFocus();
		}
		if(L_KE.getKeyChar()=='*')
		{
			
		}
	}*/
}