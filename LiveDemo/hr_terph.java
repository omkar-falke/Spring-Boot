/*

System Name : Human Resurce Management System.
Program Name :  Holidays (RH & PH) Entry.
Source Directory : d:\source\splerp3\hr_terph..java                        Executable Directory : d:\exec\splerp3\hr_terph.class

Purpose : This module shows all  Holidays for Company in a year.

List of tables used :
Table Name		Primary key							Operation done
													Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
co_cdtrn		CMT_CGMTP,CMT_CGSTP,CMT_CODCD						    /
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name			 Table name			Type/Size	Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtYEAR     TextField year        Label Year                    Current Year
txtHOLDT    DATE                  tblTERPH                      Holiday Date available in year
txtHOLTY    Holiday type          tblTERPH                      Holiday Types PH or RH in year
txtHOLDSC   Holiday description   tblTERPH                      Holiday Description
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description		Display Columns			Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtHOLTY 	Holiday type			 Holiday type 			


Leave Distribution
TB1_HOLDT	Holiday Date				        			 tblTERPH				
TB1_HOLTY	Holiday Type 		                 			 tblTERPH
TB1_HOLDSC	Holiday Description                  			 tblTERPH

-----------------------------------------------------------------------------------------------------------------------------------------------------
Validations :
->Holiday Description column is updated in modification mode.
->Holiday date and Holiday type canot be updated in modification.
->Insertion of new row is done in addition mode.
->While inserting new row user should enter values in all three columns. 
->Deletion of row is done in deletion mode.

Other  Requirements :
->Year entered in text field should be equal to current year.
->The Holiday type must be in PH(Paid Holidays) or RH (Restricted Holidays).
->Year entered in text field should be in four digits
Yearly Holidays are stored in CO_CDTRN.

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.table.*;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.JTable;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.Hashtable;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.*;

public class hr_terph extends cl_pbase implements KeyListener 
{ 
    private  cl_JTable tblTERPH; /** Table For Holidays available in a year */
    private JTextField txtYEAR; /** TextField For Year */
    //For Table TERPH
	int TB1_CHKFL = 0;/** For Columns in Table TERPH */	
	int TB1_HOLDT = 1;                    JTextField txtHOLDT;
    int TB1_HOLTY = 2;                    JTextField txtHOLTY;	
    int TB1_HOLDSC =3;                    JTextField txtHOLDSC;
    private TBLINPVF objTBLVRF;
     
  hr_terph()
  {
	super(2);

	try
  	{
	  setMatrix(20,20);
	  add(new JLabel("Year"),5,7,1,2,this,'L');
	  add(txtYEAR = new TxtNumLimit(4),5,9,1,2,this,'L');
	  objTBLVRF = new TBLINPVF();
	  String[] L_strTBLHD= {"","Date","HOLIDAY TYPE ","HOLIDAY DESCRIPTION"};
	  int[] L_intCOLSZ = {40,80,200,400};
	  tblTERPH = crtTBLPNL1(this,L_strTBLHD,80,8,3,10,14,L_intCOLSZ,new int[]{0});
	 tblTERPH.addFocusListener(this);
	 // tblTERPH.addMouseListener(this);
	  tblTERPH.addKeyListener(this);
	  tblTERPH.setCellEditor(TB1_HOLDT,txtHOLDT =new TxtDate());
	  tblTERPH.setCellEditor(TB1_HOLTY,txtHOLTY =new TxtLimit(2));
	  tblTERPH.setCellEditor(TB1_HOLDSC,txtHOLDSC =new TxtLimit(20));
	  txtYEAR.addKeyListener(this);
	  tblTERPH.setInputVerifier(objTBLVRF);
	  txtHOLDT.addKeyListener(this);
	  txtHOLTY.addKeyListener(this);
	  txtHOLDSC.addKeyListener(this);
	 /* ((JCheckBox) tblTERPH.cmpEDITR[TB1_CHKFL]).addMouseListener(this);
	  txtHOLDT.addMouseListener(this);
	  txtHOLTY.addMouseListener(this);
	  txtHOLDSC.addMouseListener(this);**/
	}
    
	catch(Exception L_EX)
	{
	  setMSG(L_EX,"Constructor");
	}
  }
  void setENBL(boolean P_flgENBFL)
  {
	 super.setENBL(P_flgENBFL);
	try
	{
	  tblTERPH.setEnabled(true);
	  txtYEAR.setEnabled(true);
	}
	catch(Exception E)
	{
	   setMSG(E,"setENBL()");			
	}
  }
  void clrEDITR(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
		tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}
           
  void getDATA()
	{
	  try
	  {
		tblTERPH.clrTABLE();
		if(tblTERPH.isEditing())
		  tblTERPH.getCellEditor().stopCellEditing();
		  tblTERPH.setRowSelectionInterval(0,0);
		  tblTERPH.setColumnSelectionInterval(0,0);
	    int L_CNT=0;
	    String strHOLDT="";
	    M_strSQLQRY = "select cmt_cgstp,cmt_codcd,cmt_codds from co_cdtrn where" ;
		M_strSQLQRY+= " CMT_CGMTP='SYS' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"PH"+txtYEAR.getText().substring(3,4)+"',"+"'HR"+cl_dat.M_strCMPCD_pbst+"RH"+txtYEAR.getText().substring(3,4)+"') and cmt_stsfl<>'X' order by cmt_codcd ";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)                           
		{ 
		  while(M_rstRSSET.next())
		  {	
			strHOLDT = M_rstRSSET.getString("cmt_codcd").substring(6,8)+"/"+M_rstRSSET.getString("cmt_codcd").substring(4,6)+"/"+M_rstRSSET.getString("cmt_codcd").substring(0,4);
			tblTERPH.setValueAt(strHOLDT,L_CNT,TB1_HOLDT);		
			tblTERPH.setValueAt(M_rstRSSET.getString("cmt_cgstp").substring(4,6),L_CNT,TB1_HOLTY);
			tblTERPH.setValueAt(M_rstRSSET.getString("cmt_codds"),L_CNT,TB1_HOLDSC);
			L_CNT++;
	      }	
		if(M_rstRSSET != null)
		{	
     	  M_rstRSSET.close();			
		}
	   }
	  }	
	   catch(Exception L_E)
	   {
	    setMSG(L_E,"getDATA");
	   }
	 }
   
  public void keyPressed(KeyEvent L_KE)
  {
    super.keyPressed(L_KE);
	try
	{
		//help for Holiday  Code
		if(L_KE.getKeyCode() == L_KE.VK_F1 )
    	{						
			if(M_objSOURC==txtHOLTY)
			   //if(M_objSOURC == tblTERPH.cmpEDITR[TB1_HOLTY])		
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtHOLTY";
    		  String L_ARRHDR[] = {"Leave Code","Leave Description"};
    		  M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
    		  M_strSQLQRY+= " where cmt_cgmtp='SYS' and cmt_cgstp='HRXXLVE' and cmt_ccsvl='H'";
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    		}
    	}
									    		
	   if(L_KE.getKeyCode() == L_KE.VK_ENTER )
       {
		  /* if(M_objSOURC == txtHOLDT)
			   txtHOLTY.requestFocus();
			else if(M_objSOURC == txtHOLTY)
			   txtHOLDSC.requestFocus();**/
						
		if(M_objSOURC==txtYEAR )
		{
			if(txtYEAR.getText().equals(cl_dat.M_strLOGDT_pbst.substring(6,10)))
			{	
			  getDATA(); 
			}
			else
			{	
		      setMSG("Year Should Be Equal To Current Year",'E');
			}
			  
			if(txtYEAR.getText().toString().length()==4)
			{
			  getDATA();
			}  
			else
			{	
			  setMSG("Year Should Be In Four Digits",'E');
			}
	     }
	  }
	}  
   	catch(NullPointerException L_NPE)
	{
	  setMSG("inside keyPressed()",'E');  
	  setCursor(cl_dat.M_curDFSTS_pbst);
	}
  }

 void exeHLPOK()
 {
	try
	{
	  super.exeHLPOK();
	   if(M_strHLPFLD.equals("txtHOLTY"))
	   {
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		txtHOLTY.setText(L_STRTKN.nextToken());
	   }
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"exeHLPOK"); 
	}
 }
 	
  /** Method to validate data  */
  boolean vldDATA(int P_intROWNO)
  {
	try
	{
	  if(tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().length()==0)
	  {
  		setMSG("Enter the Date in The Table",'E');
  		return false;
  	  }
	  if(tblTERPH.getValueAt(P_intROWNO,TB1_HOLTY).toString().length()==0)
	  {
  		setMSG("Enter the Leave Type in The Table",'E');
  		return false;
  	  }
	  if(tblTERPH.getValueAt(P_intROWNO,TB1_HOLDSC).toString().length()==0)
	  {
  		setMSG("Enter the Description in The Table",'E');
  		return false;
	  }
	}	  
  	catch(Exception L_EX)
  	{
  	  setMSG(L_EX,"This is vldDATA");
  	}	
  	  return true;
  }
  
  void exeSAVE()
  {
    int P_intROWNO=0;
    try
    {
     
      for(P_intROWNO=0;P_intROWNO<tblTERPH.getRowCount();P_intROWNO++)
      {
        if(tblTERPH.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
    	{
          if(!vldDATA(P_intROWNO))
		  {
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			return;
		  }
  	    }
  	  }
      if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
	  {
		for(P_intROWNO=0;P_intROWNO<tblTERPH.getRowCount();P_intROWNO++)
		{
		  if(tblTERPH.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
		  {
			  exeMODREC(P_intROWNO);
		  }
		}
	  }
		
  	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
	    {
    	  for(P_intROWNO=0;P_intROWNO<tblTERPH.getRowCount();P_intROWNO++)
	      {
	    	if(tblTERPH.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
	    	{
	    	  exeDELREC(P_intROWNO);
	    	}
	      }
	    }   
  	  	   	        			
   	  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
      {
    	for(P_intROWNO=0;P_intROWNO<tblTERPH.getRowCount();P_intROWNO++)
    	{
    	  if(tblTERPH.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
    	  {
    		  exeADDREC(P_intROWNO);
    	  }
    	}  
      }	
	  if(cl_dat.exeDBCMT("exeSAVE"))
	  {
		  tblTERPH.clrTABLE();			
		 setMSG("record updated successfully",'N');
	  }
	  else
	  {
		 JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
		 setMSG("Error in updating data..",'E');
	  }
    }  
   	 catch(Exception L_E)
	 {
	  setMSG(L_E,"exeSAVE");
	 }
  }
  
  /** Method to insert data  */
  
 private void exeADDREC(int P_intROWNO)
 { 
	try
	{
	  M_strSQLQRY =" insert into co_cdtrn(CMT_CGMTP,cmt_cgstp,cmt_codcd,cmt_codds,cmt_shrds,CMT_nmp01,CMT_nmp02,CMT_ncsvl,CMT_trnfl,CMT_Lusby,CMT_lupdt,CMT_stsfl)";
	  M_strSQLQRY += " values (";
	  M_strSQLQRY += "'SYS',";
	  M_strSQLQRY += "'HR"+cl_dat.M_strCMPCD_pbst+tblTERPH.getValueAt(P_intROWNO,TB1_HOLTY).toString()+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(9,10)+"',";
	  M_strSQLQRY += "'"+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(6,10)+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(3,5)+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(0,2)+"',";
	  M_strSQLQRY += "'"+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDSC).toString()+"',";
	  if(tblTERPH.getValueAt(P_intROWNO,TB1_HOLDSC).toString().trim().length()>15)
	  M_strSQLQRY += "'"+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDSC).toString().trim().substring(0,15)+"',";
	  else
	  M_strSQLQRY += "'"+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDSC).toString().trim()+"',";	  
	  M_strSQLQRY += "'0',";
	  M_strSQLQRY += "'0',";
	  M_strSQLQRY += "'0',";
	  M_strSQLQRY += "'1',";
	  M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
	  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
	  M_strSQLQRY += "'0')";
	  cl_dat.M_flgLCUPD_pbst = true;
	  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
   }
   catch(Exception L_EX)
   {
	  cl_dat.M_flgLCUPD_pbst=false;
	  setMSG(L_EX,"exeADDREC()"); 
   }
 }
 
 private void exeDELREC( int P_intROWNO)
 {
	 try
	 {
	   M_strSQLQRY="update co_cdtrn set";
	   M_strSQLQRY+=" cmt_stsfl = 'X'";
	   M_strSQLQRY+=" where cmt_cgmtp='SYS' and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+tblTERPH.getValueAt(P_intROWNO,TB1_HOLTY).toString()+txtYEAR.getText().substring(3,4)+"' and cmt_codcd='"+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(6,10)+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(3,5)+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(0,2)+"'";
	   cl_dat.M_flgLCUPD_pbst = true;
	   cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");		
	 }  
	 catch(Exception L_EX)
	 {
	   setMSG(L_EX,"exeDELREC()");		
	 }
  }
 private void exeMODREC(int P_intROWNO)
 {
	  try
	  {
		M_strSQLQRY = " Update co_cdtrn set";
		M_strSQLQRY +=" cmt_codds='"+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDSC).toString()+"' ";
		M_strSQLQRY +=" where cmt_cgmtp='SYS'";
		M_strSQLQRY +=" and cmt_codcd='"+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(6,10)+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(3,5)+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(0,2)+"'";
		M_strSQLQRY +=" and cmt_cgstp='HR"+cl_dat.M_strCMPCD_pbst+tblTERPH.getValueAt(P_intROWNO,TB1_HOLTY).toString()+tblTERPH.getValueAt(P_intROWNO,TB1_HOLDT).toString().substring(9,10)+"'";			
		//System.out.println(">>>update>>"+M_strSQLQRY);
		cl_dat.M_flgLCUPD_pbst = true;
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");		
	  }
	  catch(Exception L_EX)
	  {
	    setMSG(L_EX,"exeMODREC()");
	  }
  }
 
 
 	
  public void actionPerformed(ActionEvent L_AE)
  {
	super.actionPerformed(L_AE);
   try
   {    
	  if (M_objSOURC == cl_dat.M_cmbOPTN_pbst) 
      {
		clrEDITR(tblTERPH);
        txtYEAR.setText(cl_dat.M_strLOGDT_pbst.substring(6,10));
        txtYEAR.requestFocus();
       // txtYEAR.setEnabled(true);
      }
	 
      if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
	  {
		 tblTERPH.setEnabled(false);
	  }
      if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)
    	 ||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	  
      {
    	((JTextField)tblTERPH.cmpEDITR[TB1_HOLDT]).setEditable(false);
    	((JTextField)tblTERPH.cmpEDITR[TB1_HOLTY]).setEditable(false);
      }
      if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
  	  {
    	//((JCheckBox)tblTERPH.cmpEDITR[TB1_CHKFL]).setEnabled(true);
    	((JTextField)tblTERPH.cmpEDITR[TB1_HOLDT]).setEditable(true);
    	((JTextField)tblTERPH.cmpEDITR[TB1_HOLTY]).setEditable(true);
    	((JTextField)tblTERPH.cmpEDITR[TB1_HOLDSC]).setEditable(true);
      }
	  
    }
    catch(Exception L_EA)
    {
      setMSG(L_EA,"Action Performed");
    }
 }
  
  private class TBLINPVF extends TableInputVerifier
  {
    public boolean verify(int P_intROWID,int P_intCOLID)
    {
	  int i;
	  String L_strLVDT =""; 
	try
	{	
      if(getSource()==tblTERPH)
	  {
       if(P_intCOLID == TB1_HOLDT)
       {  
    	L_strLVDT = tblTERPH.getValueAt(P_intROWID,TB1_HOLDT).toString();
    	for(i=0;i<tblTERPH.getRowCount();i++)
	    if(tblTERPH.getValueAt(i,TB1_HOLDT).toString().length()>0 && i!=P_intROWID)
	    {	  
	      if(L_strLVDT.equals(tblTERPH.getValueAt(i,TB1_HOLDT).toString()))
	      {
	    	setMSG("Entered Date Already Exist",'E');
	    	return false;
	      }
	     }
    	if(tblTERPH.getValueAt(P_intROWID,TB1_HOLDT).toString().length()>0)
	    {	
	     if(!tblTERPH.getValueAt(P_intROWID,TB1_HOLDT).toString().substring(9,10).equals(txtYEAR.getText().substring(3,4))) // Looking for valid and accountable leave
		 {
		  setMSG("Year Should Match With The Current Year",'E');	
		  return false;
		 }
	    }
    	  setMSG("",'N');
        }  
	  }
          				
	  if(getSource()==tblTERPH)
	  {   
	    if(P_intCOLID == TB1_HOLTY)
        {
	    if(tblTERPH.getValueAt(P_intROWID,TB1_HOLTY).toString().length()>0)
	    {	
	     if(!tblTERPH.getValueAt(P_intROWID,TB1_HOLTY).toString().trim().toUpperCase().equals("PH") && !tblTERPH.getValueAt(P_intROWID,TB1_HOLTY).toString().trim().toUpperCase().equals("RH") ) 
		 {
		  setMSG("Enter Valid Leave Code",'E');	
		  return false;
		 }
	     tblTERPH.setValueAt(tblTERPH.getValueAt(P_intROWID,TB1_HOLTY).toString().trim().toUpperCase(),P_intROWID,TB1_HOLTY); 
	    }
	     setMSG("",'N');
        }		
	  }
	 }	
  	 catch(Exception L_E)
	 {
	  setMSG(L_E,"Input Verifier");
	 }
	  return true;
   }
 }
}
