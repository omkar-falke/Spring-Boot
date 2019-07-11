/**FORM TO BE MODIFIED IN SECOND PHASE
System Name   : SPS Recipe Management System
Program Name  : Internal Stores Master
Program Desc. : Form for adding, modifying and retrieving details of Raw Material available in Internal Stroes
				
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

class pr_mestm extends cl_pbase implements ActionListener 
{
	JPanel M_pnlHELP_pbst;
	cl_JTBL		tblSTMST;
	TxtNumLimit	txtMTLCD$;
	DefaultCellEditor dceMTLCD;
	Boolean FlgCHKED=new Boolean(true);
	JScrollPane	srpSTMST;
	JComboBox	cmbLOCDS;
	Hashtable<String,double[]> htbLCMST;
	TxtNumLimit txtISSNO;
	TxtDate	txtISSDT;
	pr_mestm()
	{
		super(2);
		cmbLOCDS=new JComboBox();cmbLOCDS.addItem("Select");
		cmbLOCDS.addActionListener(this);txtMTLCD$=new TxtNumLimit(10);
		txtISSNO=new TxtNumLimit(8.0);txtISSDT=new TxtDate();
		htbLCMST=new Hashtable<String,double[]>();
/*		M_strSQLQRY="SELECT * FROM pr_LCMST WHERE LC_LCCAP>LC_LCSTK";
		M_objSOURC = cl_dat.ocl_dat.exeSQLQRY1(M_strSQLQRY,"HR","ACT");
		if(M_objSOURC!=null)
		{
			try
			{
				while(M_objSOURC.next())
				{
					cmbLOCDS.addItem(M_objSOURC.getString("LC_LOCID")+" "+M_objSOURC.getString("LC_LOCDS"));
					htbLCMST.put(M_objSOURC.getString("LC_LOCID"),new double[]{M_objSOURC.getDouble("LC_LCCAP"),M_objSOURC.getDouble("LC_LCSTK")});
				}
			}catch(Exception L_E)
			{
				System.out.print(L_E);
				setMSG("Error in fetching details of stores locations ..",'E');
			}
		}
*/		setMatrix(18,4);
		String[] names=new String[]{"FL","Material Code","Description","Manufacturer","Batch No.","Qty. received","Stock on Hand","Location","UOM"};
		int[] wid=new int[]{20,100,150,75,75,75,75,75,75};
		tblSTMST=((cl_JTBL)crtTBLPNL(this,names,25,2,1,10,3.9,wid,new int[]{0}));
		tblSTMST.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblSTMST.addKeyListener(this);
		for(int i=0;i<tblSTMST.cmpEDITR.length;i++)
			tblSTMST.cmpEDITR[i].addKeyListener(this);
		tblSTMST.clrTABLE();
		((cl_JTBL)tblSTMST).setCellEditor(7,cmbLOCDS);
		tblSTMST.addFocusListener(this);
		add(new JLabel("Issue Note Number : "),1,1,1,1,this,'L');
		add(txtISSNO,1,2,1,1,this,'L');
		add(new JLabel("Issue Date : "),1,3,1,1,this,'L');
		add(txtISSDT,1,4,1,1,this,'L');
	}
	public void FocusLost(FocusEvent LP_KE)
	{
		Object M_objSOURC=LP_KE.getSource();
	}
	public void FocusGained(KeyEvent LP_KE)
	{
		Object M_objSOURC=LP_KE.getSource();
	}
	
	public void keyReleased(KeyEvent LP_KE)
	{
		Object M_objSOURC=LP_KE.getSource();
	}

	public void keyPressed(KeyEvent LP_KE)
	{
		super.keyPressed(LP_KE);
		int key=LP_KE.getKeyCode();
		if(M_objSOURC==tblSTMST.cmpEDITR[1]&&LP_KE.getKeyCode()==LP_KE.VK_F1)
		{
			if(M_pnlHELP_pbst==null)
			{
				M_strSQLQRY="SELECT CT_MATCD,CT_MATDS,CT_UOMCD FROM CO_CTMST where ct_grpcd='68' ORDER BY CT_MATCD";
				M_strHLPFLD="cmpEDITR[1]";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Material Code","Material Description","UOM"},3,"CT");
//				M_pnlHELP_pbst=(ClonePanel)((ClonePanel)cl_dat.M_pnlHELP_pbst).clone();
				System.out.println(M_pnlHELP_pbst);
			}
			else
			{
				System.out.println("in");
				//dljHELP.show();
				//dljHELP.toFront();
//				cpyHELP("CT");
			}
		}
		else if(M_objSOURC==tblSTMST.cmpEDITR[5]&&LP_KE.getKeyCode()==LP_KE.VK_F1)
		{
//			if(M_pnlHELP_pbst==null)
			{
				M_strSQLQRY="SELECT EP_EMPNO,EP_SHRNM,EP_FULNM FROM HR_EPMST ";
				M_strHLPFLD="cmpEDITR[5]";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Material Code","Material Description","UOM"},3,"CT");
//		M_pnlHELP_pbst=cl_dat.M_pnlHELP_pbst;
			}
//	else
			{
//		System.out.println("in");
				//dljHELP.show();
				//dljHELP.toFront();
//		cpyHELP("CT");
			}
		}
	}
	
	public void actionPerformed(ActionEvent LP_AE)
	{
		super.actionPerformed(LP_AE);
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Enquiry"))
					txtISSNO.setEnabled(true);
				txtISSNO.requestFocus();
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Addition")||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Modification"))&&cmbLOCDS.getItemCount()<2)
			{
				M_strSQLQRY="SELECT * FROM pr_LCMST WHERE LC_LCCAP>LC_LCSTK";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					try
					{
						while(M_rstRSSET.next())
						{
							cmbLOCDS.addItem(M_rstRSSET.getString("LC_LOCID")+" "+M_rstRSSET.getString("LC_LOCDS"));
							htbLCMST.put(M_rstRSSET.getString("LC_LOCID"),new double[]{M_rstRSSET.getDouble("LC_LCCAP"),M_rstRSSET.getDouble("LC_LCSTK")});
						}
					}catch(Exception L_E)
					{
						System.out.print(L_E);
						setMSG("Error in fetching details of stores locations ..",'E');
					}
				}
			}
		}
		else if(M_objSOURC==txtISSNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Enquiry")||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Modification"))
				getDATA();
		}
		else if(M_objSOURC==cmbLOCDS)
		{
			/*try
			{
				int row=tblSTMST.getSelectedRow();
				if(row!=-1)
				{
					String L_STRTMP=cmbLOCDS.getSelectedItem().toString();
					StringTokenizer st=new StringTokenizer(L_STRTMP," ");
					L_STRTMP=st.nextToken();
					double[] L_ARRDBL=(double[])htbLCMST.get(L_STRTMP);
					if(Double.valueOf(tblSTMST.getValueAt(row,6).toString()).doubleValue()+L_ARRDBL[1]>L_ARRDBL[0])
						setMSG("Space not sufficient ..",'E');
				}
			}catch(Exception e){System.out.println(e);}
			*/
		}
	}
	public void exeHLPOK()
	{
		if(M_strHLPFLD.equals("cmpEDITR[1]"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			((JTextField)((cl_JTBL)tblSTMST).cmpEDITR[1]).setText(L_STRTKN.nextToken());
			tblSTMST.setValueAt(L_STRTKN.nextToken(),tblSTMST.getSelectedRow(),2);
			tblSTMST.setValueAt(L_STRTKN.nextToken(),tblSTMST.getSelectedRow(),8);
			//((JTextField)((cl_JTBL)tblRECIP).cmpEDITR[2]).setText(L_STRTKN.nextToken());
		}
	}
	
	boolean vldDATA()
	{
		tblSTMST.setRowSelectionInterval(1,2);
		tblSTMST.setColumnSelectionInterval(1,2);
		if(txtISSNO.getText().length()==0)
		{
			txtISSNO.requestFocus();
			setMSG("Please enter Issue Number ..",'E');
			return false;
		}
		else if(txtISSDT.getText().length()==0)
		{
			txtISSDT.requestFocus();
			setMSG("Please enter Issue Date ..",'E');
			return false;
		}
		else if(tblSTMST.getValueAt(0,1).toString().length()==0)
		{
			tblSTMST.cmpEDITR[1].requestFocus();
			setMSG("Please enter Material Issued ..",'E');
			return false;
		}
		else
		{
			for(int i=0;i<tblSTMST.getRowCount();i++)
			{
				if(tblSTMST.getValueAt(i,1).toString().length()>0)
				{
					/*if(tblSTMST.getValueAt(i,3).toString().length()==0)
					{
						setMSG("Please enter Manufacturer ..",'E');
						return false;
					}*/
					if(tblSTMST.getValueAt(i,4).toString().length()==0)
					{
						setMSG("Please enter Batch No. ..",'E');
						return false;
					}
					else if(tblSTMST.getValueAt(i,5).toString().length()==0)
					{
						setMSG("Please enter Quantity Received ..",'E');
						return false;
					}
					else if(tblSTMST.getValueAt(i,7).toString().length()==0)
					{
						setMSG("Please select Location ..",'E');
						return false;
					}
				}
			}
		}
		return true;
	}
	void exeSAVE()
	{
		try
		{
			int i=0;
			if(vldDATA())
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Addition"))
				{
					while(tblSTMST.getValueAt(i,1).toString().length()>2)
					{
						M_strSQLQRY="INSERT INTO MM_ISMST (IS_STRTP,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_MATTP,IS_TAGNO,IS_ISSDT,IS_DPTCD,IS_PREBY,IS_PREDT,IS_AUTBY,IS_AUTDT,IS_REQQT,IS_ISSQT,IS_USGTP,IS_CCTCD,IS_BATNO,IS_GRNNO,IS_CONQT,IS_LOCCD,IS_TRNFL,IS_STSFL,IS_LUSBY,IS_LUPDT) VALUES ( "
							+""+"'RM',"//IS_STRTP,
							+"'MANUAL',"//IS_ISSTP,
							+"'"+txtISSNO.getText()+"',"//IS_ISSNO,
							+"'"+tblSTMST.getValueAt(i,1)+"',"//IS_MATCD,
							+"'RM',"//IS_MATTP,
							+"'Not Avlbl',"//IS_TAGNO,
							+" "+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+","//IS_ISSDT,
							+"'008',"//IS_DPTCD,
							+"'"+cl_dat.M_strUSRCD_pbst+"',"//IS_PREBY,
							+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//IS_PREDT,
							+"'"+cl_dat.M_strUSRCD_pbst+"',"//IS_AUTBY,
							+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//IS_AUTDT,
							+""+tblSTMST.getValueAt(i,5).toString()+","//IS_REQQT,
							+""+tblSTMST.getValueAt(i,5).toString()+","//IS_ISSQT,
							+"'RM',"//IS_USGTP,
							+"'SPS',"//IS_CCTCD,
							+"'"+tblSTMST.getValueAt(i,4)+"',"//IS_BATNO,
							+"'N.A.',"//IS_GRNNO,
							+"0,"//IS_CONQT,
							+"'"+tblSTMST.getValueAt(i,7).toString()+"',"//IS_LOCCD,
							+getUSGDTL("IS",'i',"")+")";//IS_TRNFL,IS_STSFL,IS_LUSBY,IS_LUPDT
							//+"'0',"//IS_TRNFL,
							//+"'',"//IS_STSFL,
							//+"'"+cl_dat.M_strUSRCD_pbst+"',"//IS_LUSBY,
							//+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";//IS_LUPDT
						i++;
						cl_dat.M_flgLCUPD_pbst=true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						if(!cl_dat.exeDBCMT("exeSAVE"))
							setMSG("Addition Completed..",'N');
						else
							setMSG("Addition Failed..",'E');
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Modification"))
				{
					while(tblSTMST.getValueAt(i,1).toString().length()>2)
					{
						M_strSQLQRY="UPDATE MM_ISMST SET "
							+"IS_STRTP="+"'RM',"//IS_STRTP,
							+"IS_ISSTP='MANUAL',"//IS_ISSTP,
		//					+"'"+txtISSNO.getText()+"',"//IS_ISSNO,
		//					+"IS_MATCD='"+tblSTMST.getValueAt(i,1)+"',"//IS_MATCD,
							+"IS_MATTP='RM',"//IS_MATCD,
							+"IS_TAGNO='Not Avlbl',"//IS_TAGNO,
							+"IS_ISSDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//IS_ISSDT,
							+"IS_DPTCD='008',"//IS_DPTCD,
							+"IS_PREBY='"+cl_dat.M_strUSRCD_pbst+"',"//IS_PREBY,
		//					+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//IS_PREDT,
		//					+"'"+cl_dat.M_strUSRCD_pbst+"',"//IS_AUTBY,
		//					+" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//IS_AUTDT,
		//					+""+tblSTMST.getValueAt(i,5).toString()+","//IS_REQQT,
							+"IS_ISSQT= "+tblSTMST.getValueAt(i,5).toString()+","//IS_ISSQT,
							+"IS_USGTP='RM',"//IS_USGTP,
		//					+"IS_CCTCD='SPS',"//IS_CCTCD,
							+"IS_BATNO='"+tblSTMST.getValueAt(i,4)+"',"//IS_BATNO,
							+"IS_GRNNO='N.A.',"//IS_GRNNO,
		//					+"0,"//IS_CONQT,
							+"IS_REQQT="+tblSTMST.getValueAt(i,5).toString()+","//IS_REQQT,
							+"IS_LOCCD='"+tblSTMST.getValueAt(i,7).toString()+"',"//IS_REQQT,
		//					+"'0',"//IS_TRNFL,
		//					+"'',"//IS_STSFL,
							+"IS_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//IS_LUSBY,
							+"IS_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' WHERE IS_ISSNO="+"'"+txtISSNO.getText()+"' AND IS_MATCD='"+tblSTMST.getValueAt(i,1)+"'";//IS_LUPDT
						i++;
						cl_dat.M_flgLCUPD_pbst=true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						if(!cl_dat.exeDBCMT("exeSAVE"))
							setMSG("Modification Completed..",'N');
						else
							setMSG("Modification Failed..",'E');
					}
				}
			}
		}catch(Exception e)
			{setMSG(e,"Child.exeSAVE");}
	}
	void clrCOMP()
	{
		super.clrCOMP();
		tblSTMST.clrTABLE();
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		tblSTMST.setEnabled(L_STAT);
		tblSTMST.cmpEDITR[2].setEnabled(false);
		tblSTMST.cmpEDITR[8].setEnabled(false);
		
	}
	
	void getDATA()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals("Modification"))
			{
				txtISSNO.setEnabled(false);
				txtISSDT.setEnabled(false);
				tblSTMST.cmpEDITR[1].setEnabled(false);
			}
			M_strSQLQRY="SELECT IS_MATCD,IS_ISSQT,IS_BATNO,IS_ISSDT IS_ISSDT,IS_LOCCD,CT_MATDS,CT_UOMCD FROM MM_ISMST,CO_CTMST WHERE IS_ISSNO='"+txtISSNO.getText()+"' AND IS_MATCD=CT_MATCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET==null)
			{
				setMSG("Invalid Issue No. ..",'E');
			}
			else
			{
				int i=0;
				while(M_rstRSSET.next())
				{
					tblSTMST.setValueAt(M_rstRSSET.getString("IS_MATCD"),i,1);
					tblSTMST.setValueAt(M_rstRSSET.getString("IS_ISSQT"),i,5);
					tblSTMST.setValueAt(M_rstRSSET.getString("IS_BATNO"),i,4);
					tblSTMST.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,2);
					tblSTMST.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,8);
					tblSTMST.setValueAt(nvlSTRVL(M_rstRSSET.getString("IS_LOCCD"),"Select"),i,7);
					if(i==0)
					{	
						//fmtDAT is deprecated 
						//****txtISSDT.setText(nvlSTRVL(fmtDAT(M_rstRSSET.getDate("IS_ISSDT")),""));
						if(M_rstRSSET.getDate("IS_ISSDT") != null)
							txtISSDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("IS_ISSDT")));
					}	
					i++;
				}
			}
		}catch(Exception E)
		{System.out.println(E);}
	}
/*	private void cpyHELP(String LP_HLPPOS)
	{
		cl_dat.ocl_dat.M_flgCHKTB_pbst = false;
		////////////
		cl_dat.M_pnlDYNTBL_pbst=M_pnlHELP_pbst.M_pnlDYNTBL_pbst;
		//cl_dat.M_pnlHELP_pbst.add(cl_dat.M_pnlDYNTBL_pbst);
		//cl_dat.M_pnlDYNTBL_pbst.setEnabled(false);
		cl_dat.M_pnlPOSBTN_pbst.add(M_pnlHELP_pbst.M_lblHELP_pbst);
		cl_dat.M_pnlPOSBTN_pbst.add(M_pnlHELP_pbst.M_txtHLPPOS_pbst);
		cl_dat.M_pnlPOSBTN_pbst.add(M_pnlHELP_pbst.M_btnHLPOK_pbst);
		cl_dat.M_pnlHELP_pbst=M_pnlHELP_pbst.M_pnlHELP_pbst;
		cl_dat.M_pnlHELP_pbst.add(M_pnlHELP_pbst.M_pnlPOSBTN_pbst);
		cl_dat.M_pnlHELP_pbst.setVisible(true);
		

		//////////
		cl_dat.ocl_dat.M_flgCHKTB_pbst = false;
		cl_dat.M_pnlHELP_pbst = M_pnlHELP_pbst.M_pnlHELP_pbst;
		cl_dat.M_lblHLPHDG_pbst = M_pnlHELP_pbst.M_lblHLPHDG_pbst;
		cl_dat.M_pnlDYNTBL_pbst = M_pnlHELP_pbst.M_pnlDYNTBL_pbst;
		cl_dat.M_pnlHELP_pbst.add(cl_dat.M_pnlDYNTBL_pbst);
		cl_dat.M_pnlDYNTBL_pbst.setEnabled(false);
		cl_dat.M_lblHELP_pbst = M_pnlHELP_pbst.M_lblHELP_pbst;
		cl_dat.M_txtHLPPOS_pbst = M_pnlHELP_pbst.M_txtHLPPOS_pbst;
		cl_dat.M_txtHLPPOS_pbst.getDocument().addDocumentListener(this);
		cl_dat.M_txtHLPPOS_pbst.addActionListener(this);
		cl_dat.M_txtHLPPOS_pbst.addKeyListener(this);
		cl_dat.M_txtHLPPOS_pbst.addFocusListener(this);
		cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_txtHLPPOS_pbst);
		cl_dat.M_btnHLPOK_pbst = M_pnlHELP_pbst.M_btnHLPOK_pbst;
	  	cl_dat.M_btnHLPOK_pbst.addActionListener(this);
		cl_dat.M_btnHLPOK_pbst.addMouseListener(this);
		cl_dat.M_btnHLPOK_pbst.setActionCommand("help");
		cl_dat.M_btnHLPOK_pbst.addKeyListener(this);
		cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_btnHLPOK_pbst);
		cl_dat.M_pnlHELP_pbst.add(cl_dat.M_pnlPOSBTN_pbst);
		cl_dat.M_pnlHELP_pbst.setVisible(true);
		cl_dat.M_wndHLP_pbst = new JDialog();
		cl_dat.M_wndHLP_pbst.getContentPane().add(cl_dat.M_pnlHELP_pbst);
//		cl_dat.M_pnlHELP_pbst.add(cl_dat.M_pnlDYNTBL_pbst);
//		cl_dat.M_pnlDYNTBL_pbst.setEnabled(false);
//		cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_lblHELP_pbst);
//		cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_txtHLPPOS_pbst);
//		cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_btnHLPOK_pbst);
//		cl_dat.M_pnlHELP_pbst.add(cl_dat.M_pnlPOSBTN_pbst);
///		cl_dat.M_pnlHELP_pbst.setVisible(true);
		M_pnlHELP_pbst.setVisible(true);
		cl_dat.M_wndHLP_pbst = new JDialog();
///		cl_dat.M_wndHLP_pbst.getContentPane().add(cl_dat.M_pnlHELP_pbst);
		cl_dat.M_wndHLP_pbst.getContentPane().add(cl_dat.M_pnlHELP_pbst);
		if(LP_HLPPOS.equals("TL")){
			cl_dat.M_intXPOS_pbst = 10;
			cl_dat.M_intYPOS_pbst = 35;
		}
		else if(LP_HLPPOS.equals("TR")){
			cl_dat.M_intXPOS_pbst = 150;
			cl_dat.M_intYPOS_pbst = 35;
		}
		else if(LP_HLPPOS.equals("BL")){
			cl_dat.M_intXPOS_pbst = 10;
			cl_dat.M_intYPOS_pbst = 300;
		}
		else if(LP_HLPPOS.equals("BR")){
			cl_dat.M_intXPOS_pbst = 150;
			cl_dat.M_intYPOS_pbst = 300;
		}
		else if(LP_HLPPOS.equals("CT")){
			cl_dat.M_intXPOS_pbst = 100;
			cl_dat.M_intYPOS_pbst = 160;
		}
		M_pnlHELP_pbst.updateUI();
///cl_dat.M_pnlHELP_pbst.updateUI();		
		cl_dat.M_wndHLP_pbst.setBounds(cl_dat.M_intXPOS_pbst,cl_dat.M_intYPOS_pbst,600,230);
		cl_dat.M_wndHLP_pbst.toFront();
		cl_dat.M_wndHLP_pbst.show();
		cl_dat.M_wndPOS_pbst = new JWindow(cl_dat.M_wndHLP_pbst);
		cl_dat.M_wndHLP_pbst.setTitle(padSTRING('L',"Help Screen",70));
		cl_dat.M_txtHLPPOS_pbst.requestFocus();
	}*/
}