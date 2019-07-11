
/*
System Name : Administration System.
Program Name : Administration Bill Entry.
Source Directory : d:\source\splerp3\ad_rpblm.java                        Executable Directory : d:\exec\splerp3\hr_terph.class

Purpose : This module displays all  Report  for Company in a year.

List of tables used :
Table Name		Primary key							Operation done
													Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
ad_rpblm		BL_BILTP,BL_TRDAT,BL_DPTCD,BL_EMPNO					    /
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	    TextField Name			 Type/Size	       Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtDPTCD        Deparment Code           Integer/3        Deparment Code 
txtEMPNO        Employee Number          Integer/4        Employee Number   
txtFMDAT        From Date                Date             From Date
txtTODAT        To Date                  Date             To Date   
cmbBILTP        Bill Type                String           Bill  Main Type
cmbBILTP1       Bill Type                String           Bill Sub Type
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description		Display Columns			                    Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtDPTCD    Deparment Code         Deparment Code,Deparment Description         CO_CDTRN
txtEMPNO    Employee Number        Employee Number,Employee Name                HR_EPMST

-----------------------------------------------------------------------------------------------------------------------------------------------------
Validations :
->Enter From Date.
->Enter To Date.
->From Date can not be greater than Current date time.
->To Date can not be greater than Current date time.
->To Date can not be smaller than From date.

Other  Requirements :
->If only Department code is entered in a Department Code TextField and Employee no TextField is Kept blank then Report will be generated  for all employee in that Deparment. 
->If only Employee No. is entered in a Employee No. TextField and Deparment code TextField is Kept blank then Report will be generated  for that employee only. 
->If Deparment Code and Employee Code TextFields are kept blank  and "Sumarry Report" combobox option is selected then Report will be generated for all employee.
->If Deparment code is entered in a Deparment code TextField then F1 on Employee No. will show only those employee which belongs to that Department.

*/

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JOptionPane; 
import javax.swing.JPanel;
import javax.swing.JTabbedPane; 
import java.sql.ResultSet; 
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.Color;

class ad_rpblm extends cl_rbase implements FocusListener, KeyListener
{	
  private JTextField txtFMDAT;
  private JTextField txtTODAT;
  private JTextField txtDPTCD;
  private JTextField txtEMPNO;
  private JTextField txtEMPNM;
  private JTextField txtTRDAT;
  private JLabel lblDPTNM;
  private JLabel lblEMPNM;
  private JComboBox cmbBILTP;
  private JComboBox cmbBILTP1;
  private cl_JTable tblTOTAM;
  private JPanel pnlALL;
  private INPVF oINPVF;
  private TBLINPVF objTBLVRF;
  private Vector<String> vtrBILTP;
  private String strREPFL =cl_dat.M_strREPSTR_pbst+"ad_rpblm.doc"; 
  private String strFMDAT,strTODAT,strCRDAT,strBILTP,strDPTCD,strEMPNO;
  private int intRECCNT,intLINNO,intPAGNO;
  private String strTOTQT = "0";
  private DataOutputStream dosREPORT; /** DataOutputStream to generate the Report file form stream of data.*/
  private FileOutputStream fosREPORT; /** String variable to print Dotted Line in the Report.*/
  private JRadioButton  rdbBILRP;
  private JRadioButton  rdbTOTAM;
  private JRadioButton  rdbDBILAM;
  private JCheckBox chkCHKFL;
  private JTextField txtCODE,txtDISCP,txtPRDTP;
  String[] arrDPTNM;
  int TB1_CHKFL = 0;
  int TB1_BILTP = 1;
  int TB1_BILDS = 2;
  private Hashtable<String,String> hstBILAM; /** HashTable For all codes*/
  private Hashtable<String,String> hstBILTP; /** HashTable For all codes*/
  private ButtonGroup   btgALL;
  private String strDOTLN = "------------------------------------------------------------------------------------------------------------------------------";
  private String strDOTLN1 = "-------------------------------------------------------------------------------------------------------";
  private String strDOTLN2 = "------------------------------------------------------------------------------------------";
  private String strDOTLN3 = "----------------------------------------------------------------";
  
 public ad_rpblm()
 {
   super(1);
   setMatrix(20,20);
   
   try
   {
	     
    M_vtrSCCOMP.remove(M_lblFMDAT);
	M_vtrSCCOMP.remove(M_lblTODAT);
	M_vtrSCCOMP.remove(M_txtTODAT);
	M_vtrSCCOMP.remove(M_txtFMDAT);

    pnlALL= new JPanel();
    pnlALL.setBorder(new EtchedBorder(Color.gray,Color.lightGray)); 
    btgALL=new ButtonGroup();
      
	add(rdbBILRP=new JRadioButton("Bill Report"),3,3,1,4,pnlALL,'L');
	add(rdbTOTAM=new JRadioButton("Total Amount"),3,4,1,4,pnlALL,'R');
	add(rdbDBILAM=new JRadioButton("Dept. Bill Amount"),3,5,1,4,pnlALL,'R');
	add(pnlALL,3,11,1.5,7,this,'R');
	btgALL.add(rdbTOTAM); 
    btgALL.add(rdbBILRP);
    btgALL.add(rdbDBILAM);
    
       
	add(new JLabel("From Date"),6,4,1,3,this,'L');   
    add(txtFMDAT = new TxtDate(),6,6,1,3,this,'L');  
    add(new JLabel("To Date"),6,11,1,3,this,'L');   
    add(txtTODAT = new TxtDate(),6,13,1,3,this,'L');  
   	add(new JLabel("Main Type"),7,4,1,3,this,'L'); 
	add(cmbBILTP = new JComboBox(),7,6,1,3,this,'L');   
	add(new JLabel("Sub Type"),7,11,1,3,this,'L');   
	add(cmbBILTP1 = new JComboBox(),7,13,1,3,this,'L');
	cmbBILTP1.addItem("Select Sub Type");
    add(new JLabel("Dept.Code"),8,4,1,3,this,'L');
    add(txtDPTCD = new TxtNumLimit(3),8,6,1,3,this,'L');
    add(lblDPTNM=new JLabel(),8,9,1,2, this,'L');
    add(new JLabel("Emp.No."),8,11,1,3,this,'L');
    add(txtEMPNO = new TxtNumLimit(4),8,13,1,3,this,'L');
    add(lblEMPNM=new JLabel(),8,16,1,3, this,'L');
    vtrBILTP = new Vector<String>();
    oINPVF=new INPVF();
    objTBLVRF = new TBLINPVF();
    txtDPTCD.addFocusListener(this);
    txtEMPNO.addFocusListener(this); 
  	txtFMDAT.addKeyListener(this);
	txtTODAT.addKeyListener(this);
	txtDPTCD.setInputVerifier(oINPVF);
	txtFMDAT.setInputVerifier(oINPVF);
	txtTODAT.setInputVerifier(oINPVF);
	txtEMPNO.setInputVerifier(oINPVF);
 
	txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
	txtFMDAT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2));
	tblTOTAM=crtTBLPNL1(this,new String[]{"Status","Main Loc","Description"},8,10,6,4,5,new int[]{50,65,100},new int[]{0});
	tblTOTAM.addKeyListener(this);
	tblTOTAM.setCellEditor(TB1_CHKFL,chkCHKFL = new JCheckBox());
	tblTOTAM.setCellEditor(TB1_BILTP,txtCODE = new TxtLimit(4));
	tblTOTAM.setCellEditor(TB1_BILDS,txtDISCP = new TxtLimit(20));
	tblTOTAM.setInputVerifier(objTBLVRF);
	((JTextField)tblTOTAM.cmpEDITR[TB1_BILTP]).setEditable(false);
	((JTextField)tblTOTAM.cmpEDITR[TB1_BILDS]).setEditable(false);
  
	hstBILAM = new Hashtable<String,String>(); /**Hashtable to store values for dept.name, main bill type, &  bill amount */
	hstBILTP = new Hashtable<String,String>(); /**Hashtable to store values for dept.name, sub bill type, & bill amount*/
	
	/** Vector created to store Bill code and description */ 
	cmbBILTP.addItem("Select Main Type");
 
	M_strSQLQRY = "Select cmt_codcd,cmt_codds from co_cdtrn ";
	M_strSQLQRY += " where cmt_cgmtp = 'SYS' and cmt_cgstp ='ADXXBTP'";
	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	
	if(M_rstRSSET !=null)                           
	{ 
	  while(M_rstRSSET.next())
	  {	
	    M_rstRSSET.getString("cmt_codds");
	    vtrBILTP.add(M_rstRSSET.getString("cmt_codcd")+"|"+M_rstRSSET.getString("cmt_codds"));
	  }
	} 
	for(int i=0; i<vtrBILTP.size(); i++)
	{
	  if(vtrBILTP.get(i).toString().substring(2,4).equals("00"))
	  {
	    cmbBILTP.addItem(vtrBILTP.get(i).toString().substring(5));
	  }
	}
	M_pnlRPFMT.setVisible(true);
	setENBL(false);
	rdbBILRP.setSelected(true);
	
	
   }
   catch(Exception L_EX)
   {
	 setMSG(L_EX,"Constructor");
   }
 }
  
 /*public void focusGained (FocusEvent L_FE)
 {
	super.focusGained(L_FE);
	if(M_objSOURC == txtFMDAT)
	{
	if(txtFMDAT.getText().length()== 0)
	{	
	setMSG("Enter from Date",'E');
	}
	}
    if (M_objSOURC == txtTODAT)
	{	
    if(txtTODAT.getText().length()== 0)
	{	
	  setMSG("Enter to Date",'E');
	}
   }
 }**/
  
 /** Method to insert Bill type into table tblTOTAM*/    
 private void getMNLCD()
 { 
		try
		{
		  int intROWNO = 0;
		  for(int m=0; m<vtrBILTP.size(); m++)
		  {
			 if(vtrBILTP.get(m).toString().substring(2,4).equals("00"))
			 {
			  tblTOTAM.setValueAt(new Boolean(true),intROWNO,TB1_CHKFL);
			  tblTOTAM.setValueAt(vtrBILTP.get(m).toString().substring(0,4),intROWNO,TB1_BILTP);
			  tblTOTAM.setValueAt(vtrBILTP.get(m).toString().substring(5),intROWNO,TB1_BILDS);
			  intROWNO++;
      		 }
		  }
		  if(M_rstRSSET != null)
		  M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMNLCD");	
		}
 }
  
 public void keyPressed(KeyEvent L_KE)
 {
   super.keyPressed(L_KE);
   try
   {	
	if(L_KE.getKeyCode()== L_KE.VK_F1)
	{
       if(M_objSOURC==txtDPTCD)
	   {
    	  M_strHLPFLD = "txtDPTCD";
		  M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
		  M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and  cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"')";
		  //System.out.println(">>>>DPTCD>>>>"+M_strSQLQRY);
		  cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code", "Department Description"},2,"CT");
		  setCursor(cl_dat.M_curWTSTS_pbst);
	   }
           				
	   if(M_objSOURC==txtEMPNO)		
	   {
		 if(txtDPTCD.getText().length()==0)
	     {
		  cl_dat.M_flgHELPFL_pbst = true;
		  M_strHLPFLD = "txtEMPNO";
		  String L_ARRHDR[] = {"Employee No","Employee Name"};
		  M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' '+ SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
		  //System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
	     } 
		 if(txtDPTCD.getText().length()>0)
		 {
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtEMPNO";
			M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'and EP_DPTCD='"+txtDPTCD.getText()+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
			//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
			cl_hlp(M_strSQLQRY,2,1,new String[]{"Code", "Name"},2,"CT",new int[]{107,400});
		 }
	   }
    }
	else if(L_KE.getKeyCode()== L_KE.VK_ENTER)
	{
		if(M_objSOURC == txtFMDAT)
		   txtTODAT.requestFocus();
		else if(M_objSOURC == txtTODAT)
		   txtDPTCD.requestFocus();
		else if(M_objSOURC == txtDPTCD)
		   txtEMPNO.requestFocus();
		else if(M_objSOURC == txtEMPNO)
			  cl_dat.M_btnSAVE_pbst.requestFocus();
		
	 }
   }
   catch(Exception L_EX)
   {
	  setMSG(L_EX,"F1 help");
	  setCursor(cl_dat.M_curDFSTS_pbst);	
   }
 }
 
 void setENBL(boolean L_STAT)
 {
	super.setENBL(L_STAT);
		
 }
 
 void exeHLPOK()
 {
	    try
		{
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDPTCD.setText(L_STRTKN.nextToken());
				lblDPTNM.setText(L_STRTKN.nextToken());
			}
						
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
			  if(txtDPTCD.getText().length()==0)
			  {	
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNO.setText(L_STRTKN.nextToken());
				lblEMPNM.setText(L_STRTKN.nextToken());
			  }	
			}
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
			  if(txtDPTCD.getText().length()>0)
			  {	
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNO.setText(L_STRTKN.nextToken());
				lblEMPNM.setText(L_STRTKN.nextToken());
			  }	
			}
			cl_dat.M_flgHELPFL_pbst = false;
        }	
        catch(Exception L_EX)
        {
	      setMSG(L_EX,"in Child.exeHLPOK");
        } 
}
 public void actionPerformed(ActionEvent L_AE)
 {
	super.actionPerformed(L_AE);
  try
  {
		 
	if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
	{
	  if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
	  {
		tblTOTAM.setVisible(false);  
		txtFMDAT.requestFocus();
		if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
		{
		  setMSG ("Please enter Date to generate Report..",'N');	
		}
		getMNLCD();	
	  }
	}

	if(M_objSOURC == txtDPTCD)
	{
	    if(txtDPTCD.getText().length()==0)
	    	lblDPTNM.setText("");
	}
	
	if(M_objSOURC == txtEMPNO)
	{
	  if(txtEMPNO.getText().length()==0)
		 lblEMPNM.setText("");
	}
	
	if(M_objSOURC == rdbTOTAM)
	{
	  cmbBILTP.setEnabled(false);
	  cmbBILTP1.setEnabled(false);
	  txtDPTCD.setEnabled(false);
	  txtEMPNO.setEnabled(false);
	  tblTOTAM.setVisible(true);
	  txtFMDAT.setEnabled(true);
	  txtTODAT.setEnabled(true);
	  txtDPTCD.setText("");
	  lblDPTNM.setText("");
	  txtEMPNO.setText("");
	  lblEMPNM.setText("");
	  cmbBILTP.setSelectedIndex(0);
	  cmbBILTP1.setSelectedIndex(0); 
	}

	if(M_objSOURC == rdbBILRP)
	{
	  cmbBILTP.setEnabled(true);
	  cmbBILTP1.setEnabled(true);
	  txtDPTCD.setEnabled(true);
	  txtEMPNO.setEnabled(true);
	  tblTOTAM.setVisible(false);
	  txtDPTCD.setText("");
	  lblDPTNM.setText("");
	  txtEMPNO.setText("");
	  lblEMPNM.setText("");
	  cmbBILTP.setSelectedIndex(0);
	  cmbBILTP1.setSelectedIndex(0); 
	}
	if(M_objSOURC == rdbDBILAM)
	{
	  txtDPTCD.setEnabled(false);
	  txtFMDAT.setEnabled(true);
	  txtTODAT.setEnabled(true);
	  cmbBILTP.setEnabled(true);
	  tblTOTAM.setVisible(false);
	  cmbBILTP1.setEnabled(false);
	  txtDPTCD.setText("");
	  lblDPTNM.setText("");
	  txtEMPNO.setText("");
	  lblEMPNM.setText("");
	  cmbBILTP.setSelectedIndex(0);
	  cmbBILTP1.setSelectedIndex(0); 
	 }
				
	if(M_objSOURC == cmbBILTP && cmbBILTP.getItemCount()>0) 
	{
	    String L_strBILTP=cmbBILTP.getSelectedItem().toString();
	
		for(int i=0;i<vtrBILTP.size();i++)
		{
			if(vtrBILTP.get(i).toString().substring(5).equals(L_strBILTP))
			{	
				//System.out.println("L_strBILTP>>"+L_strBILTP);
				String L_strBILCD = vtrBILTP.get(i).toString().substring(0,2); 
				//System.out.println("L_strBILTP>>1");
				for (int k=cmbBILTP1.getItemCount()-1;k>0;k--)
				{
					//System.out.println("L_strBILTP>>4");
					cmbBILTP1.removeItemAt(k);	
				}
				//System.out.println("L_strBILTP>>2");
				for(int j=0;j<vtrBILTP.size();j++)
				{
				  //System.out.println("L_strBILCD5>>");
				  if(vtrBILTP.get(j).toString().substring(0,2).equals(L_strBILCD) && !vtrBILTP.get(j).toString().substring(2,4).equals("00"))	
				  cmbBILTP1.addItem(vtrBILTP.get(j).toString().substring(5));
				}
				//System.out.println("L_strBILTP>>3");
			}
		}
	 } 
	if(M_objSOURC == cmbBILTP && cmbBILTP.getSelectedItem().toString().equals("Select Main Type")) 
	{
		for (int k=cmbBILTP1.getItemCount()-1;k>0;k--)
		{
			cmbBILTP1.removeItemAt(k);	
		}
	}
   		
	
  }
  catch(Exception L_EX)
  {
	setMSG(L_EX,"actionPerformed");
  }
   setMSG("",'N');
}
/**Method to generate the Report & to forward it to specified destination*/
 public void exePRINT()
 {
	 try
		{
		 	cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 1;
			
			if(vldDATA())
			{
				if(M_rdbHTML.isSelected())
					strREPFL = cl_dat.M_strREPSTR_pbst+"ad_rpblm.html";
				else
					strREPFL = cl_dat.M_strREPSTR_pbst+"ad_rpblm.doc";
				
				   getDATA(); 
							
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
				  if (M_rdbTEXT.isSelected())
					doPRINT(strREPFL);
				 
				  else 
					{    
						Runtime r = Runtime.getRuntime();
						Process p = null;					
						p  = r.exec("c:\\windows\\iexplore.exe "+strREPFL); 
						setMSG("For Printing Select File Menu, then Print  ..",'N');
					} 
			    }
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
					/*if(intRECCNT > 0)
					{*/
						Runtime r = Runtime.getRuntime();
						Process p = null;
						try
						{
							if(M_rdbHTML.isSelected())
							    p  = r.exec("c:\\windows\\iexplore.exe "+strREPFL); 
							else
							    p  = r.exec("c:\\windows\\wordpad.exe "+strREPFL);
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"Error.exescrn.. ");
						}
					/*}
					else
						setMSG("Record is not found",'E');**/
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error in exePRINT ");
		}
}
/**Method to validate the data before execuation of the SQL Query*/
 boolean vldDATA()
 {
  try
  {
	strFMDAT = txtFMDAT.getText().trim();
	strTODAT = txtTODAT.getText().trim();
	strCRDAT = cl_dat.M_strLOGDT_pbst;
	if(txtFMDAT.getText().trim().length()== 0)
	{
	  setMSG("Enter From Date..",'E');
	  txtFMDAT.requestFocus();
	  return false;
	}
	
	else if(txtTODAT.getText().trim().length()== 0)
	{
	  setMSG("Enter To Date..",'E');
	  txtTODAT.requestFocus();
	  return false;
	 
	}
	else if(M_fmtLCDAT.parse(strFMDAT).compareTo(M_fmtLCDAT.parse(strCRDAT))> 0)
	{
		
		setMSG("From Date can not be greater than Current date time..",'E');
		return false;
		
	}
	else if(M_fmtLCDAT.parse(strTODAT).compareTo(M_fmtLCDAT.parse(strCRDAT))> 0)
	{
		setMSG("To Date can not be greater than Current date time..",'E');
		return false;
		
	}
	else if(M_fmtLCDAT.parse(strTODAT).compareTo(M_fmtLCDAT.parse(strFMDAT))< 0)
	{
		setMSG("To Date can not be smaller than From date..",'E');
		return false;
		
	}
	else if(rdbDBILAM.isSelected())
	{
	  if(cmbBILTP.getSelectedIndex()==0)
	  {
		setMSG ("Please select Main-Type Item",'E');
		return false;
	  }
	}
	
	else if(rdbTOTAM.isSelected())
	{
	  int P_intROWNO=0;
	  if(tblTOTAM.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("false")) 
	  {	 
	 	setMSG ("Please select Table checkbox",'E');
		return false;
	  }
	  if(((JCheckBox)tblTOTAM.cmpEDITR[TB1_CHKFL]).isSelected());
	  {
		for(int i=0;i<tblTOTAM.getRowCount();i++)
		{
		  if(tblTOTAM.getValueAt(i,TB1_CHKFL).toString().equals("true"))
		  {  
            if(tblTOTAM.getValueAt(i,TB1_BILTP).toString().length()==0)
	        {	
	    	  setMSG("You can't select blank column",'E');
	          return false;
	        }
	      }  
	    }
	  }
	}
	return true;
  }
  catch(Exception L_EX)
  {
	 setMSG(L_EX,"vldDATA");
	 return false;
  }
}
 
 /** Method to generate Header part of the Report*/
  public void prnHEADER()
  {
	try
	{
		
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
		{
		    prnFMTCHR(dosREPORT,M_strNOCPI17);
		    prnFMTCHR(dosREPORT,M_strCPI10);				
			prnFMTCHR(dosREPORT,M_strCPI17);
			prnFMTCHR(dosREPORT,M_strBOLD);
		}
	
		if(M_rdbHTML.isSelected())
		{
		  dosREPORT.writeBytes("<b>");	
		  dosREPORT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
		  dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
		}
		
				
	  if(rdbBILRP.isSelected())
	  { 
		  
	  dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-18));
	  dosREPORT.writeBytes("Date: "+cl_dat.M_strLOGDT_pbst+"\n");
	  cl_dat.M_intLINNO_pbst +=2;
	  dosREPORT.writeBytes(padSTRING('R',"Expense Report",strDOTLN.length()-18));
	  dosREPORT.writeBytes("Page No: "+cl_dat.M_PAGENO+"\n");
	  dosREPORT.writeBytes("Period From: "+txtFMDAT.getText()+ " To: "+txtTODAT.getText()+"\n");
	  cl_dat.M_intLINNO_pbst +=2;
	  dosREPORT.writeBytes(strDOTLN+"\n");
	  dosREPORT.writeBytes("Bill Type       Dept.Name    Emp.No.   Emp.Name                 Transaction No.     Bill No.       Bill Date        Bill Amt.\n");
	  dosREPORT.writeBytes(strDOTLN);
	  
	  cl_dat.M_intLINNO_pbst +=3;
	  
	  }	
	 
	  if(rdbTOTAM.isSelected())
	  {
		dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN1.length()-30));
		dosREPORT.writeBytes("Date: "+cl_dat.M_strLOGDT_pbst+"\n");
		cl_dat.M_intLINNO_pbst +=2;
		dosREPORT.writeBytes(padSTRING('R',"Summary of Expenses",strDOTLN1.length()-30));
		dosREPORT.writeBytes("Page No: "+cl_dat.M_PAGENO+"\n");
		dosREPORT.writeBytes("Period From: "+txtFMDAT.getText()+ " To: "+txtTODAT.getText()+"\n");
		cl_dat.M_intLINNO_pbst +=2;
		dosREPORT.writeBytes(strDOTLN2+"\n");  
		dosREPORT.writeBytes(padSTRING('R',"Dept.Name",10));  
		for(int i=0; i<tblTOTAM.getRowCount();i++)
		{
		  if(tblTOTAM.getValueAt(i,TB1_CHKFL).toString().equals("true"))
		  {
			dosREPORT.writeBytes((padSTRING('L',tblTOTAM.getValueAt(i,TB1_BILDS).toString(),15)));
		  }
		}
		dosREPORT.writeBytes(padSTRING('L',"Total",15)); 
		dosREPORT.writeBytes("\n"); 
		dosREPORT.writeBytes(strDOTLN2);
	  }
	 
	  if(rdbDBILAM.isSelected())
	  {
		 
		  dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN1.length()-30));
		  dosREPORT.writeBytes("Date: "+cl_dat.M_strLOGDT_pbst+"\n");
		  dosREPORT.writeBytes(padSTRING('R',"Expense Report for: "+cmbBILTP.getSelectedItem().toString(),strDOTLN1.length()-30));
		  dosREPORT.writeBytes("Page No: "+cl_dat.M_PAGENO+"\n");
		  dosREPORT.writeBytes("Period From: "+txtFMDAT.getText()+ " To: "+txtTODAT.getText()+"\n");
		  dosREPORT.writeBytes(strDOTLN2+"\n");
		  dosREPORT.writeBytes(padSTRING('R',"Dept.Name",10)); 
		  for(int i=0;i<vtrBILTP.size();i++)
		  {
			if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP.getSelectedItem().toString()))
			{
			  for(int j=0;j<vtrBILTP.size();j++)
			  {
				String L_strBILCD = vtrBILTP.get(i).toString().substring(0,2);   
				if(vtrBILTP.get(j).toString().substring(0,2).equals(L_strBILCD) && !vtrBILTP.get(j).toString().substring(2,4).equals("00"))	
				dosREPORT.writeBytes(padSTRING('L',vtrBILTP.get(j).toString().substring(5),15));
			  }
			}
		  }
		   dosREPORT.writeBytes(padSTRING('L',"Total",15)); 
		   dosREPORT.writeBytes("\n"); 
		   dosREPORT.writeBytes("\n"+strDOTLN2+"\n");
	   }
	 
	  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		   prnFMTCHR(dosREPORT,M_strNOBOLD);
		  if(M_rdbHTML.isSelected())
		   dosREPORT.writeBytes("</b>");
			  			
	}
	catch(Exception L_EX)
	{
	  setMSG(L_EX,"prnHEADER");
	}
  }
  /** Method to fetch data from database & to club it with header & footer*/
  public void getDATA()
  {
		if(!vldDATA())
			return;		
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strREPFL);	
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
								
		 if(rdbTOTAM.isSelected())
		 {	
			getREC();
		 }
		 if(rdbDBILAM.isSelected())
		 {	
			getREC1();
		 }	
		 if(rdbBILRP.isSelected())
		 {
			getALLREC();	
		 }
		 				 
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
  }
  
  private void getREC()
  {
	String strNEW_DPTNM="";
	String strOLD_DPTNM="";
	String strHSTAM;
	String[] arrDPTNM = new String[20];
	int intARR = 0;
	int intTOTAM = 0;// total no of elements
	int intINDEX = 0; // index for arrTOTAM
	for(int i=0;i<arrDPTNM.length;i++)
	arrDPTNM[i]="";
	
	try
	{
		prnHEADER();
		double L_dblTOTQT = 0;
		for(int j=0;j<tblTOTAM.getRowCount();j++)
        {
    	  if(tblTOTAM.getValueAt(j,TB1_CHKFL).toString().equals("true"))
    	  {
    		 intTOTAM++;
    	  }
        } 
		double[] arrTOTAM = new double[20];
		for(int i=0;i<arrTOTAM.length;i++)
		arrTOTAM[i]=0;
		hstBILAM.clear();
		M_strSQLQRY = " select BL_DPTNM,SUBSTRING(BL_BILTP,1,2) BL_BILTP,sum(BL_BILAM) BL_BILAM FROM AD_BLMST";
		M_strSQLQRY +=" WHERE BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_STSFL <> 'X'";
		 if(txtFMDAT.getText().trim().length()>0 && txtTODAT.getText().trim().length()>0)
		 {
			M_strSQLQRY +=" AND CONVERT(varchar,BL_BILDT,103) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
		 }
					
		M_strSQLQRY +=" group by BL_DPTNM,SUBSTRING(BL_BILTP,1,2) order by BL_DPTNM,SUBSTRING(BL_BILTP,1,2)"; 
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
 
	   if(M_rstRSSET !=null) 
	   {	
		 while(M_rstRSSET.next())
         {
			strNEW_DPTNM=M_rstRSSET.getString("BL_DPTNM") ;
			if(!strOLD_DPTNM.equals(strNEW_DPTNM))
			{	
			  strOLD_DPTNM=M_rstRSSET.getString("BL_DPTNM");
			  arrDPTNM[intARR++] = M_rstRSSET.getString("BL_DPTNM");
			}
		   hstBILAM.put(M_rstRSSET.getString("BL_DPTNM")+"|"+M_rstRSSET.getString("BL_BILTP"),M_rstRSSET.getString("BL_BILAM"));
		   //System.out.println("hstBILAM>>"+ hstBILAM);
         }						
	   }
	   for(int i=0;i<intARR;i++)
	     {
	    	 dosREPORT.writeBytes("\n");
			 cl_dat.M_intLINNO_pbst ++;	  
	    	 dosREPORT.writeBytes(padSTRING('R',arrDPTNM[i],10)); 
	    	 intINDEX=0;
	        for(int j=0;j<tblTOTAM.getRowCount();j++)
	        {
	    	  if(tblTOTAM.getValueAt(j,TB1_CHKFL).toString().equals("true"))
	    	  {
	    		  
	    		if(hstBILAM.containsKey(arrDPTNM[i]+"|"+tblTOTAM.getValueAt(j,TB1_BILTP).toString().substring(0,2)))
	    	    {
	    	     dosREPORT.writeBytes(padSTRING('L',hstBILAM.get(arrDPTNM[i]+"|"+tblTOTAM.getValueAt(j,TB1_BILTP).toString().substring(0,2)),15));
	    	     strHSTAM = hstBILAM.get(arrDPTNM[i]+"|"+tblTOTAM.getValueAt(j,TB1_BILTP).toString().substring(0,2)); 
	    	     L_dblTOTQT += Double.parseDouble(strHSTAM);
	    	   	 arrTOTAM[intINDEX] += Double.parseDouble(strHSTAM);
   		    	}
	    	    else
	    	    {
	    		 dosREPORT.writeBytes(padSTRING('L',"-",15));
	    	    }
	    		intINDEX++;
	    	  }
	  	    }
	        dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,2),15));
	        arrTOTAM[intINDEX]+=L_dblTOTQT;
	        L_dblTOTQT = 0;
	      }
	            crtNWLIN(); 
			    dosREPORT.writeBytes("\n");
			    dosREPORT.writeBytes(strDOTLN2+"\n");
			    dosREPORT.writeBytes(padSTRING('R',"Total",10));
			 for(int i=0;i<=intINDEX;i++)
			 {	 
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(arrTOTAM[i],2),15));
			 }
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN2+"\n");	
			if(M_rdbHTML.isSelected())
		    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");
			fosREPORT.close();
			dosREPORT.close();
			if(M_rstRSSET != null)
			M_rstRSSET.close();
			setMSG("",'N');
	}
	catch(Exception L_EX)
	{
	  setMSG(L_EX,"getREC");
	}
  }
  private void getREC1()
  {
	String strNEW_DPTNM="";
	String strOLD_DPTNM="";
	String strHSTAM;
	String[] arrDPTNM = new String[20];
	String[] arrBILTP = new String[20];
	int intBILTP = 0;
	int intARR = 0;
	for(int i=0;i<arrDPTNM.length;i++)
	arrDPTNM[i]="";
	int intTOTAM = 0;// total no of elements
	int intINDEX = 0; // index for arrTOTAM
		
  try
  {
	prnHEADER();
	double L_dblTOTQT = 0;
	double[] arrTOTAM = new double[20];
	for(int i=0;i<arrTOTAM.length;i++)
	arrTOTAM[i]=0;
	hstBILTP.clear();
   
	for(int i=0;i<vtrBILTP.size();i++)
	{
	  if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP.getSelectedItem().toString()))
	  {	
		String L_strBILCD = vtrBILTP.get(i).toString().substring(0,2); 
	   for(int j=0;j<vtrBILTP.size();j++)
	   {
		if(vtrBILTP.get(j).toString().substring(0,2).equals(L_strBILCD) && !vtrBILTP.get(j).toString().substring(2,4).equals("00"))	
		arrBILTP[intBILTP++]= vtrBILTP.get(j).toString().substring(0,4);
	   }
	  }
	} 
     M_strSQLQRY =" select BL_DPTNM,BL_BILTP,sum(BL_BILAM) BL_BILAM FROM AD_BLMST ";
	 M_strSQLQRY +=" WHERE BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_STSFL <> 'X'";
	 if(cmbBILTP.getSelectedIndex()!=0)
	  for(int i=0;i<vtrBILTP.size();i++)
	  {
		if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP.getSelectedItem().toString()))
		{
		  M_strSQLQRY += " and SUBSTRING(BL_BILTP,1,2) = '"+vtrBILTP.get(i).toString().substring(0,2)+"'";
		}
	                  
	  }
	
	 if(txtFMDAT.getText().trim().length()>0 && txtTODAT.getText().trim().length()>0)
		{
		  M_strSQLQRY +=" AND CONVERT(varchar,BL_BILDT,103) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
		}
		
	M_strSQLQRY +=" group by BL_DPTNM,BL_BILTP order by BL_DPTNM,BL_BILTP";
	//System.out.println(M_strSQLQRY);
	M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
	if(M_rstRSSET !=null) 
	{	
	 while(M_rstRSSET.next())
     {
		
	   strNEW_DPTNM=M_rstRSSET.getString("BL_DPTNM");
	   if(!strOLD_DPTNM.equals(strNEW_DPTNM))
	   {	
		  strOLD_DPTNM=M_rstRSSET.getString("BL_DPTNM");
		  arrDPTNM[intARR++] = M_rstRSSET.getString("BL_DPTNM");
	   }
	   hstBILTP.put(M_rstRSSET.getString("BL_DPTNM")+"|"+M_rstRSSET.getString("BL_BILTP"),M_rstRSSET.getString("BL_BILAM"));
	   //System.out.println("hstBILTP>>"+ hstBILTP);
     }
	}
	 for(int i=0;i<intARR;i++)
	 {
	   dosREPORT.writeBytes("\n");
	   cl_dat.M_intLINNO_pbst ++;	  
	   dosREPORT.writeBytes(padSTRING('R',arrDPTNM[i],10));
	   intINDEX=0;   
		   for(int j=0;j<intBILTP;j++)
			{
			 if(hstBILTP.containsKey(arrDPTNM[i]+"|"+arrBILTP[j]))
			 {
				dosREPORT.writeBytes(padSTRING('L',hstBILTP.get(arrDPTNM[i]+"|"+arrBILTP[j]),15));
				strHSTAM = hstBILTP.get(arrDPTNM[i]+"|"+arrBILTP[j]);
				L_dblTOTQT += Double.parseDouble(strHSTAM);
				arrTOTAM[intINDEX] += Double.parseDouble(strHSTAM);
			}
			  else
			  {
				dosREPORT.writeBytes(padSTRING('L',"-",15));
			  }
			   intINDEX++;
			}
	     	 dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,2),15));
	     	arrTOTAM[intINDEX]+=L_dblTOTQT;
		 	 L_dblTOTQT = 0;
	     }
	            crtNWLIN(); 
			    dosREPORT.writeBytes("\n");
			    dosREPORT.writeBytes(strDOTLN2+"\n");
			    dosREPORT.writeBytes(padSTRING('R',"Total",10));
			    for(int i=0;i<=intINDEX;i++)
				 {	 
				  dosREPORT.writeBytes(padSTRING('L',setNumberFormat(arrTOTAM[i],2),15));
				 }
			   	dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN2+"\n");
			if(M_rdbHTML.isSelected())
			dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");
			fosREPORT.close();
			dosREPORT.close();
			if(M_rstRSSET != null)
			M_rstRSSET.close();
			setMSG("",'N');
	}
	catch(Exception L_EX)
	{
	  setMSG(L_EX,"getREC1");
	}
 }
   	 
  private void getALLREC()
  {
	String strOLD_BILTP="";
	String strNEW_BILTP="";
	String strOLD_DPTNM="";
	String strNEW_DPTNM="";
	String strOLD_EMPNO="";
	String strNEW_EMPNO="";
	String strOLD_EMPNM="";
	String strNEW_EMPNM="";
					
			try
			{
				prnHEADER();
				double L_dblTOTQT = 0;
			    String strTOAMT;
				strTOTQT = "0";
									     
			  M_strSQLQRY = "select BL_BILTP,BL_TRNNO,BL_DPTNM,BL_EMPNO,BL_EMPNM,BL_BILDT,BL_BILNO,BL_BILAM FROM AD_BLMST";
			  M_strSQLQRY +=" WHERE BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_STSFL <> 'X'";
			  
			  if(txtFMDAT.getText().trim().length()>0 && txtTODAT.getText().trim().length()>0)
				{
				  M_strSQLQRY +=" AND CONVERT(varchar,BL_BILDT,103) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
				}
							 
			  if(cmbBILTP.getSelectedIndex()!=0)
			   for(int i=0;i<vtrBILTP.size();i++)
			   {
				 if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP.getSelectedItem().toString()))
				 {
					M_strSQLQRY += " and SUBSTRING(BL_BILTP,1,2) = '"+vtrBILTP.get(i).toString().substring(0,2)+"'";
				 }
					  		                           
			   }
			  
			  if(cmbBILTP1.getSelectedIndex()!=0)
			  for(int i=0;i<vtrBILTP.size();i++)
			  {
				  if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP1.getSelectedItem().toString()))
				  {
					 M_strSQLQRY += " and BL_BILTP = '"+vtrBILTP.get(i).toString().substring(0,4)+"'";
				  }
				  		                           
			  }
			    if (txtDPTCD.getText().length()>0)
			     	  M_strSQLQRY += " and BL_DPTCD = '" + txtDPTCD.getText().toString()+"'";

				if (txtEMPNO.getText().length()>0)
					M_strSQLQRY += " and BL_EMPNO = '" + txtEMPNO.getText().toString()+"'";
							
				M_strSQLQRY +=" order by BL_BILTP";
											
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
				//System.out.println(">>>M_strSQLQRY>>"+M_strSQLQRY);
		
			if(M_rstRSSET !=null)                           
			{ 
				while(M_rstRSSET.next())
				{
					strNEW_BILTP=M_rstRSSET.getString("BL_BILTP");
					strNEW_DPTNM=M_rstRSSET.getString("BL_DPTNM");
					strNEW_EMPNO=M_rstRSSET.getString("BL_EMPNO");
					strNEW_EMPNM=M_rstRSSET.getString("BL_EMPNM");
									 	  					
					   if(!strOLD_EMPNO.equals(strNEW_EMPNO)||!strOLD_BILTP.equals(strNEW_BILTP))
				       {
						  for(int i=0;i<vtrBILTP.size();i++)
						  {
							if((vtrBILTP.get(i).toString().substring(0,4)).equals(M_rstRSSET.getString("BL_BILTP")))
						    {
							  dosREPORT.writeBytes("\n");
						      cl_dat.M_intLINNO_pbst ++;
						      dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(vtrBILTP.get(i).toString().substring(5),""),16));
						    }
						  }
						  dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("BL_DPTNM"),13));
						  dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("BL_EMPNO"),10));
						  dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("BL_EMPNM"),25));
						  strOLD_EMPNO=M_rstRSSET.getString("BL_EMPNO");
						  strOLD_BILTP=M_rstRSSET.getString("BL_BILTP");
					  }        
				  										   
					   else 
					   {	
						   dosREPORT.writeBytes("\n");
						   cl_dat.M_intLINNO_pbst ++;  
						   dosREPORT.writeBytes(padSTRING('R',"",64));
					   }
					    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("BL_TRNNO"),""),20));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("BL_BILNO"),""),15));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_BILDT")),""),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("BL_BILAM"),""),15));
						strTOAMT = setNumberFormat(M_rstRSSET.getDouble("BL_BILAM"),2).toString();
						L_dblTOTQT += Double.parseDouble(strTOAMT);
						crtNWLIN(); 				  																														
				}
		     }
			strTOTQT = setNumberFormat(L_dblTOTQT,2);
		    dosREPORT.writeBytes ("\n");
		    dosREPORT.writeBytes(strDOTLN);
		   	dosREPORT.writeBytes("\n" + padSTRING('L',"Total : ",113) + padSTRING('L',strTOTQT,11));
		  	prnFOOTR();
		  	if(M_rdbHTML.isSelected())
		    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");
			fosREPORT.close();
			dosREPORT.close();
			if(M_rstRSSET != null)
			M_rstRSSET.close();
			setMSG("",'N');
		 }
		 catch(Exception L_EX)
		 {
			setMSG(L_EX,"getALLREC");
		 }
  }
  
  private void crtNWLIN() 
	{
		try
		{
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >66)
				{
				   dosREPORT.writeBytes("<P CLASS = \"breakhere\">");	
				   cl_dat.M_intLINNO_pbst = 0;
				   cl_dat.M_PAGENO+=1;
				   prnHEADER();
				}
			}	
			else if(cl_dat.M_intLINNO_pbst >45)
			{
			 	cl_dat.M_intLINNO_pbst = 0;
			    cl_dat.M_PAGENO+=1;
			   	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(dosREPORT,M_strEJT);	
			  	prnHEADER();
			}			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}
  
 private class TBLINPVF extends TableInputVerifier
 {
   public boolean verify(int P_intROWID,int P_intCOLID)
   {
	int i;
	try
	{
	      
	   if(((JCheckBox)tblTOTAM.cmpEDITR[TB1_CHKFL]).isSelected())
	   {
	    for(i=0;i<tblTOTAM.getRowCount();i++)
		   if(tblTOTAM.getValueAt(i,TB1_CHKFL).toString().equals("true"))
		   {  
            if(tblTOTAM.getValueAt(i,TB1_BILTP).toString().length()==0)
	        {	
	    	  setMSG("You can't select blank column",'E');
	          return false;
	        }
	       }
		}
	   
	 }
	 catch(Exception L_E)
	 {
	     setMSG(L_E,"Table Input Verifier");
	 }
	 setMSG("",'N');
	  return true;
   }
 }  
    
  class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;								
				if(input == txtTODAT)
				{
				    if(txtTODAT.getText().toString().length()== 0)
					{
						setMSG("Enter To Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtTODAT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("To Date can not be greater Than Todays Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtTODAT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))<0)
					{
						setMSG("To Date can not be greater than Or equal to From Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
				}
				if(input == txtFMDAT)
				{	
				  	if(txtFMDAT.getText().toString().length()== 0)
					{
						setMSG("Enter From Date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtFMDAT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("From Date Should Not Be Grater Than Todays Date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
				}	
						
				if(input == txtDPTCD)
				{
					M_strSQLQRY = "select CMT_CODCD ,CMT_SHRDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and CMT_CODCD = '"+txtDPTCD.getText()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
					   lblDPTNM.setText(M_rstRSSET.getString("CMT_SHRDS"));
					   setMSG("",'N');
					}
					else
					{
						lblDPTNM.setText("");
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
					
						
				}
				if(input == txtEMPNO)
				{
					if(txtDPTCD.getText().length() == 0)
					{
					  M_strSQLQRY = "select EP_EMPNO,EP_LSTNM +' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U'  and EP_EMPNO = '"+txtEMPNO.getText()+"'";
					}
					
					if(txtDPTCD.getText().length()>0)
					{
						M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'and EP_DPTCD='"+txtDPTCD.getText()+"' and isnull(ep_stsfl,'X') <> 'U'  and EP_EMPNO = '"+txtEMPNO.getText()+"'";	
					}
					//System.out.println("INPVF EMPNO : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
					  lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));	
					  setMSG("",'N');
					}	
					else
					{
					   lblEMPNM.setText("");	
					   setMSG("Enter Valid Employee Code",'E');
					   return false;
					}
				}
			  }	
				
				catch(Exception E_VR)
				{
					setMSG(E_VR,"class INPVF");		
				}
				return true;
			}
  }
  
  private void prnFOOTR()
  {
		try
		{
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
     		dosREPORT.writeBytes ("\n");
     	}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
   }
 }
  
  

