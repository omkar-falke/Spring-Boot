/*
System Name   : Material Management System
Program Name  : Pending Customer Order

Purpose : This program generates Report for List Of Pending Customer Order for given date Range.

<pre>
SUPREME PETROCHEM LTD.                                                 
Customer Orders Pending for ____ between:    and        
Sale Type :                                                            Date    : 26/04/2006
Zone      :                                                            Page No : 1
--------------------------------------------------------------------------------------------
Ind No    Bkg.Date    Buyer Name                               Grade       Ind.Qty. Bal.Qty.
--------------------------------------------------------------------------------------------

*/

import java.sql.Date;import java.sql.ResultSet;import java.awt.event.*;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.*;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.*;

class mr_rppco extends cl_rbase
{
	private JComboBox cmbRPTOP;	   /** JComboBox to display & select Report.*/
	private JLabel lblFMDAT;       /** JLabel to display message on the Screen.*/
	private JLabel lblTODAT;       /**JLabel to display message on the Screen.*/
	private JTextField txtFMDAT;   /** JtextField to display & enter Date to generate the Report.*/
	private JTextField txtTODAT;   /** String Variable for date.*/
	private JTextField txtSALTP,txtZONCD;
	private String strFMDAT;		/** String Variable for date.*/ 
	private String strTODAT;		/** String Variable for date.*/ 
	private JRadioButton rdbAMMND,rdbCANCL,rdbPANCO;  /** JRadioButton for Ammended. */
	private ButtonGroup btgPDCOR; 
	
	private String strFILNM;	    /** String Variable for generated Report file Name.*/ 
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
	private String strDOTLN = "--------------------------------------------------------------------------------------------";
	
	mr_rppco()
	{
		super(2);
		try
		{
			cmbRPTOP = new JComboBox();
			cmbRPTOP.addItem("Authorisation");
			cmbRPTOP.addItem("DO preparation");
			cmbRPTOP.addItem("LA preparation");
			cmbRPTOP.addItem("Dispatch");
			btgPDCOR=new ButtonGroup();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		
			setMatrix(20,8);
			add(new JLabel("Pending Order"),4,3,1,1,this,'L');
			add(cmbRPTOP,4,4,1,1.5,this,'L');
			add(lblFMDAT = new JLabel("From Date "),5,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),5,4,1,1.5,this,'L');
			
			add(lblTODAT = new JLabel("To Date "),6,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),6,4,1,1.5,this,'L');
			add(new JLabel("Sale Type"),7,3,1,1,this,'L');
			add(txtSALTP = new JTextField(),7,4,1,1,this,'L');
			add(new JLabel("Zone"),8,3,1,1,this,'L');
			add(txtZONCD = new JTextField(),8,4,1,1,this,'L');
			add(rdbAMMND=new JRadioButton("Order Ammended",true),2,2,1,2,this,'L');
			add(rdbCANCL=new JRadioButton("Order Cancelled",true),2,4,1,2,this,'L');
			add(rdbPANCO=new JRadioButton("Pending Customer Order",true),2,6,1,2,this,'L');
			btgPDCOR.add(rdbAMMND);
			btgPDCOR.add(rdbCANCL);
			btgPDCOR.add(rdbPANCO);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				txtFMDAT.requestFocus();
				setMSG("Please Enter Date to generate the Report..",'N');
			}
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		if(rdbAMMND.isSelected() || rdbCANCL.isSelected())
			cmbRPTOP.setEnabled(false);
		else
			cmbRPTOP.setEnabled(true);
		
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)		
			cl_dat.M_PAGENO = 0;	

	}

	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbRPTOP)
			{
				txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
				txtFMDAT.select(0,txtFMDAT.getText().length());
				//if(cmbRPTOP.getSelectedItem() == 0)
					txtFMDAT.requestFocus();
				//else
					//cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					txtTODAT.requestFocus();
				else
				{
					txtFMDAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
				{
					txtSALTP.requestFocus();
					//cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else
				{
					txtTODAT.requestFocus();
					setMSG("Enter Date",'N');
				}
				
			}
			if(M_objSOURC == txtSALTP)
			{
				txtZONCD.requestFocus();
			}
			if(M_objSOURC == txtZONCD)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			if(M_objSOURC == rdbAMMND)
			{
				txtFMDAT.requestFocus();
			}
			if(M_objSOURC == rdbCANCL)
			{
				txtFMDAT.requestFocus();
				//cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			if(M_objSOURC == rdbPANCO)
			{
				cmbRPTOP.requestFocus();
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtSALTP)
			{
				M_strHLPFLD = "txtSALTP";
				String L_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MR00SAL' ";
				cl_hlp(L_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");//
			}
			if(M_objSOURC == txtZONCD)
			{
				M_strHLPFLD = "txtZONCD";
				String L_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MR00ZON' ";
				cl_hlp(L_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");//
			}
			
		setMSG("",'N');
		}
	}
	
	
	
	/** Function for getting value from F1 after clicking OK 
	 */
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");

			if(M_strHLPFLD.equals("txtSALTP"))
			{
				txtSALTP.setText(L_STRTKN.nextToken());
				txtSALTP.requestFocus();
			}
			if(M_strHLPFLD.equals("txtZONCD"))
			{
				txtZONCD.setText(L_STRTKN.nextToken());
				txtZONCD.requestFocus();
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
		}
	}
	/**User friendly messagees	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(L_FE.getSource().equals(txtFMDAT))
			{
                setMSG("Enter  Date in format dd/mm/yyyy",'N');
			}
			if(L_FE.getSource().equals(txtTODAT))
			{
                setMSG("Enter  Date in format dd/mm/yyyy",'N');
			}
			if(L_FE.getSource().equals(txtSALTP))
			{
	   			setMSG("Press F1 to select the Sale Type ",'N');
			}
			if(L_FE.getSource().equals(txtZONCD))
			{
	   			setMSG("Press F1 to select the Zone ",'N');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"TEIND.FocusGained"+M_objSOURC);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Method to fetch data from Database & club it with Header & Footer.
	 */
	public void getDATA()
	{		
		java.sql.Date jdtTEMP;
		cl_dat.M_intLINNO_pbst =0;
		setCursor(cl_dat.M_curWTSTS_pbst);
		String L_strFMDAT = txtFMDAT.getText().trim();
       	strFMDAT = L_strFMDAT.substring(6,10);
		strFMDAT += "-";
		strFMDAT += L_strFMDAT.substring(3,5);
		strFMDAT += "-";
		strFMDAT += L_strFMDAT.substring(0,2);	
			
		String L_strTODAT = txtTODAT.getText().trim();
		strTODAT = L_strTODAT.substring(6,10);
		strTODAT += "-";
		strTODAT += L_strTODAT.substring(3,5);
		strTODAT += "-";
		strTODAT += L_strTODAT.substring(0,2);	
		try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>"+ cmbRPTOP.getSelectedItem().toString()+"</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			prnHEADER();
				if(rdbPANCO.isSelected())
				{
					if(cmbRPTOP.getSelectedItem().toString().equals("Authorisation"))
					{
						System.out.println("Aut");
						M_strSQLQRY = "select IN_INDNO,IN_BKGDT,PT_PRTNM ,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,INT_INDQT L_BALQT ";
						M_strSQLQRY += " from VW_INTRN,co_PTMST where ";
						M_strSQLQRY +=" IN_BYRCD = PT_PRTCD and PT_PRTTP = 'C'";
						M_strSQLQRY +=" and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT between '"+strFMDAT+"' and '"+strTODAT+"'";
						M_strSQLQRY +=" and INT_STSFL='0'";
						if(txtSALTP.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_SALTP = '"+txtSALTP.getText()+"'";
						}
						if(txtZONCD.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_ZONCD = '"+txtZONCD.getText()+"'";
						}
						
					}
					else 
					if(cmbRPTOP.getSelectedItem().toString().equals("DO preparation"))
					{
						System.out.println("DO pre");
						M_strSQLQRY = "select IN_INDNO,IN_BKGDT,PT_PRTNM,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,(INT_INDQT-INT_DORQT) L_BALQT ";
						M_strSQLQRY += " from VW_INTRN,co_PTMST where";
						M_strSQLQRY +=" IN_BYRCD = PT_PRTCD and PT_PRTTP = 'C'";
						M_strSQLQRY +="and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT between '"+strFMDAT+"' and '"+strTODAT+"'";
						M_strSQLQRY +=" and INT_STSFL='1'";
						if(txtSALTP.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_SALTP = '"+txtSALTP.getText()+"'";
						}
						if(txtZONCD.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_ZONCD = '"+txtZONCD.getText()+"'";
						}
					}
					else
					if(cmbRPTOP.getSelectedItem().toString().equals("LA preparation"))
					{
						System.out.println("LA pre");
						M_strSQLQRY = "select IN_INDNO,IN_BKGDT,PT_PRTNM,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,(INT_INDQT-INT_LADQT) L_BALQT ";
						M_strSQLQRY += " from VW_INTRN,co_PTMST where";
						M_strSQLQRY +=" IN_BYRCD = PT_PRTCD and PT_PRTTP = 'C'";
						M_strSQLQRY +="and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT between '"+strFMDAT+"' and '"+strTODAT+"'";
						M_strSQLQRY +=" and INT_STSFL in ('1','2')";
						if(txtSALTP.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_SALTP = '"+txtSALTP.getText()+"'";
						}
						if(txtZONCD.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_ZONCD = '"+txtZONCD.getText()+"'";
						}
					}
					else
					if(cmbRPTOP.getSelectedItem().toString().equals("Dispatch"))
					{
						System.out.println("Disp");
						M_strSQLQRY = "select IN_INDNO,IN_BKGDT,PT_PRTNM,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,(INT_INDQT-INT_INVQT) L_BALQT ";
						M_strSQLQRY += " from VW_INTRN, co_PTMST where";
						M_strSQLQRY +=" IN_BYRCD = PT_PRTCD and PT_PRTTP = 'C'";
						M_strSQLQRY +="and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_BKGDT between '"+strFMDAT+"' and '"+strTODAT+"'";
						M_strSQLQRY +=" and INT_STSFL in ('1','2')";
						if(txtSALTP.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_SALTP = '"+txtSALTP.getText()+"'";
						}
						if(txtZONCD.getText().length() > 0)
						{
							M_strSQLQRY +=" and IN_ZONCD = '"+txtZONCD.getText()+"'";
						}
					}
				}
				else
				if(rdbAMMND.isSelected())
				{
					System.out.println("Amm");
					M_strSQLQRY = "select IN_INDNO,IN_AMDDT,PT_PRTNM ,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,INT_INDQT L_BALQT ";
					M_strSQLQRY += " from VW_INTRN,co_PTMST where ";
					M_strSQLQRY +=" IN_BYRCD = PT_PRTCD and PT_PRTTP = 'C'";
					M_strSQLQRY +="and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_AMDDT between '"+strFMDAT+"' and '"+strTODAT+"'";
					M_strSQLQRY +=" and IN_AMDNO > '00'";
				}
				else
				if(rdbCANCL.isSelected())
				{
					System.out.println("Canc");
					M_strSQLQRY = "select IN_INDNO,IN_LUPDT,PT_PRTNM ,INT_PRDDS,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT,INT_INDQT L_BALQT ";
					M_strSQLQRY += " from VW_INTRN,co_PTMST where ";
					M_strSQLQRY +=" IN_BYRCD = PT_PRTCD and PT_PRTTP = 'C'";
					M_strSQLQRY +="and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_LUPDT between '"+strFMDAT+"' and '"+strTODAT+"'";
					M_strSQLQRY +=" and IN_STSFL = 'X'";
				}
				
				
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{ 
					while(M_rstRSSET.next())
					{
						int intRECCT=1;
						if(cl_dat.M_intLINNO_pbst >58)
						{
							//dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(strDOTLN);
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst = 0;
						//	cl_dat.M_PAGENO += 1;
						//	System.out.println("In loop "+cl_dat.M_PAGENO);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							//System.out.println("in ammd");
							
							prnHEADER();
						}
							//M_fmtLCDAT.format(L_rstRSSET.getDate("IN_BKGDT"))
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""),10));
							if(rdbPANCO.isSelected())
							{
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_BKGDT")),""),12));	
							}
							if(rdbAMMND.isSelected())
							{
								//System.out.println("in ammd");
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_AMDDT")),""),12));	
							}
							if(rdbCANCL.isSelected())
							{
								//System.out.println("in Canc");
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IN_LUPDT")),""),12));	
							}
							
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),41));	
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("INT_PRDDS"),""),11));	
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("INT_INDQT"),""),8));	
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_BALQT"),""),9));	
												
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						
						
					}
				//	dosREPORT.writeBytes("\n");
				//	cl_dat.M_intLINNO_pbst += 1;
					M_rstRSSET.close();
				}
			
			dosREPORT.writeBytes(strDOTLN);		
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			    fosREPORT.close();
			    dosREPORT.close();
			    setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to generate the header of the Report.
	 */
	void prnHEADER()
	{
		String L_strSALTP="";
		String L_strZONCD="";
		ResultSet L_rstRSSET;
		try
		{
			try
			{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MR00SAL' and CMT_CODCD = '"+txtSALTP.getText()+"'";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				 if(L_rstRSSET !=null)
				if(L_rstRSSET.next())
				{
					// for getting code desc from sale type
					L_strSALTP = L_rstRSSET.getString("CMT_CODDS");
					//System.out.println(L_strSALTP);
				}
			 	if(L_rstRSSET != null)
				L_rstRSSET.close();
			}
			catch(Exception e)
			{
				setMSG(e,"GetSAL");
			}
			try
			{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MR00ZON' and CMT_CODCD = '"+txtZONCD.getText()+"'";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				 if(L_rstRSSET !=null)
				if(L_rstRSSET.next())
				{
					// for getting code desc from sale type
					L_strZONCD = L_rstRSSET.getString("CMT_CODDS");
					//System.out.println(L_strZONCD);
				}
			 	if(L_rstRSSET != null)
				L_rstRSSET.close();
			}
			catch(Exception e)
			{
				setMSG(e,"GetSAL");
			}
					
			
			cl_dat.M_PAGENO ++;
		//	System.out.println("In header "+cl_dat.M_PAGENO);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			//	dosREPORT.writeBytes("\n");
			
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-21));
				dosREPORT.writeBytes("\n");
				if(rdbPANCO.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('R',"Customer Orders Pending for "+cmbRPTOP.getSelectedItem()+" between:"+txtFMDAT.getText()+" and "+txtTODAT.getText(),strDOTLN.length()-10));
				}				
				if(rdbAMMND.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('R',"Customer "+rdbAMMND.getText()+" between:"+txtFMDAT.getText()+" and "+txtTODAT.getText(),strDOTLN.length()-10));
				}
				if(rdbCANCL.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('R',"Customer "+rdbCANCL.getText()+" between:"+txtFMDAT.getText()+" and "+txtTODAT.getText(),strDOTLN.length()-10));
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',"Sale Type :"+L_strSALTP ,strDOTLN.length()-21));
				dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");	
				dosREPORT.writeBytes(padSTRING('R',"Zone      :"+L_strZONCD ,strDOTLN.length()-21));
				dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes("Ind No    Bkg.Date    Buyer Name                               Grade       Ind.Qty. Bal.Qty."+"\n");
				dosREPORT.writeBytes(strDOTLN +"\n");	
			cl_dat.M_intLINNO_pbst += 7;

		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}

	/**
	 * Method to validate inputs given before execuation of SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				if(txtFMDAT.getText().trim().length() != 10)
				{
					setMSG("Enter the proper Date..",'E');
					return false;
				}
				if(txtTODAT.getText().trim().length() != 10)
				{
					setMSG("Enter the proper Date..",'E');
					return false;
				}
			}
			if(txtFMDAT.getText().trim().length()==10 && txtTODAT.getText().trim().length()==10)
			{
				//System.out.println("in Second IF");
				setMSG(" ",'N');			
				if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
				{
					setMSG("From Date can not be greater than TO Date's date..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	/**
	 * Method to generate the Report & send to the selected Destination.
	 */
	public void exePRINT()
	{
		//System.out.println("IN Print");
		if(!vldDATA())
			return;
		try
		{
			int intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rppco.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rppco.doc";
			
			getDATA();
			
			//if(intRECCT == 0)
			//{
			//	setMSG("Data not found for the given Inputs..",'E');
			//	return;
			//}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
		        {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
}

			
	