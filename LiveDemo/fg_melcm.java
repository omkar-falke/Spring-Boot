/*

System Name : Material Management System.
Program Name :  Location  Entry
Source Directory : d:\source\splerp3\fg_melcm..java                        Executable Directory : d:\exec\splerp3\fg_melcm.class

Purpose : This module shows all  Materials Locations.

List of tables used :
Table Name		Primary key							Operation done
													Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
FG_LCMST								    
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name			Table name			 Type/Size	     Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -

txtMNLCD1   TextField year      Label Main Location   Char/5         Main Location
txtMNLCD    Code                tblMELCM              Char/5         Code
txtLOCDS    Description         tblMELCM              Char/20        Location Description
txtMAXQT    Maximum Quantity    tblMELCM              Num/(10.3)     Max Qty available
txtSTKQT    Stock Quantity      tblMELCM              Num/(10.3      Stock Quantity available
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description		Display Columns			Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtMNLCD1	Main Location		      				        tblMELCM

Leave Distribution
TB1_MNLCD	Code							                tblMELCM				
TB1_LOCDS	Location Description 		        			tblMELCM
TB1_MAXQT	Maximum Quantity         			            tblMELCM
TB1_STKQT	Stock Quantity         			                tblMELCM

-----------------------------------------------------------------------------------------------------------------------------------------------------
Validations :
->Validation is used for all four columns viz Code, Location Description,Maximum Qty.,Stock Qty. while addition of new row 
->In modification mode columns Code and Stock Qty. can't be updated.
->In Enquiry mode whole table is kept Editable false.
->Characters inserted in columns Code and Location Description are converted into UpperCase.

Other  Requirements :
->Main Location textfield  has given F1 help.
->Characters in Main Location are converted into UpperCase.
->On Code column validation is given for same Code.

Location Of Materials stored in FG_LCMST table.
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

public class fg_melcm extends cl_pbase implements KeyListener
{

    private  cl_JTable tblMELCM;/** Table For Location Master Entry */
    private JTextField txtMNLCD1;/** TextField For Main Location Code Entry*/
    
    // For Table MELCM
    int TB1_CHKFL = 0; /** For Columns in Table MELCM */
	int TB1_MNLCD = 1;                    JTextField txtMNLCD;
    int TB1_LOCDS = 2;                    JTextField txtLOCDS;	
    int TB1_MAXQT = 3;                    JTextField txtMAXQT;
    int TB1_STKQT = 4;                    JTextField txtSTKQT;
    int TB1_VRFQT = 5;                    JTextField txtVRFQT;
    private TBLINPVF objTBLVRF;
    private int intHOL_STSFL=1;
    private int intHOL_SSCFL=0;
    private Hashtable<String,String> hstMELCM; /** HashTable For all codes & stock quantity available */ 
    String strRSSET;
    
       
  /** Constructor*/  
    
  fg_melcm()
  {
	super(2);  
   try
   {
	  setMatrix(20,20);
	  add(new JLabel("Main Location"),3,8,1,2,this,'L');
	  add(txtMNLCD1 = new TxtLimit(5),3,10,1,2,this,'L');
      objTBLVRF = new TBLINPVF();
      String[] L_strTBLHD = {"","Code","Description","Maximum Quantity","Stock Quantity","Verify Stock"};
	  int[]L_intCOLSZ = {40,80,100,100,100,100};
	  tblMELCM = crtTBLPNL1(this, L_strTBLHD,1500,5,5,12,11,L_intCOLSZ,new int[]{0});
	  tblMELCM.addKeyListener(this);
	  tblMELCM.setCellEditor(TB1_MNLCD,txtMNLCD= new TxtLimit(5));
	  tblMELCM.setCellEditor(TB1_LOCDS,txtLOCDS= new TxtLimit(20));
	  tblMELCM.setCellEditor(TB1_MAXQT,txtMAXQT= new TxtNumLimit(10.3));
	  tblMELCM.setCellEditor(TB1_STKQT,txtSTKQT= new TxtNumLimit(10.3));
	  tblMELCM.setCellEditor(TB1_VRFQT,txtVRFQT= new TxtLimit(2));
	  txtMNLCD1.addKeyListener(this);
	  tblMELCM.setInputVerifier(objTBLVRF);
	  txtMNLCD.addKeyListener(this);
	  txtLOCDS.addKeyListener(this);
	  hstMELCM = new Hashtable<String,String>();
	  crtMELCM();
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
	  tblMELCM.setEnabled(true);
	  txtMNLCD1.setEnabled(true);
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
	
  /** Method To Fetch Data From DataBase Table*/
  void getDATA()
  {
	  try
	  { 
	    tblMELCM.clrTABLE();
	    if(tblMELCM.isEditing())
		tblMELCM.getCellEditor().stopCellEditing();
		tblMELCM.setRowSelectionInterval(0,0);
		tblMELCM.setColumnSelectionInterval(0,0);
	    int L_CNT=0;
	    M_strSQLQRY = "select LC_MNLCD,LC_LOCDS,LC_MAXQT,LC_STKQT from FG_LCMST where" ;
		M_strSQLQRY+= " LC_MNLCD like '"+txtMNLCD1.getText().toUpperCase()+"%'";
		M_strSQLQRY+= " and LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'"; 
		M_strSQLQRY+= " and LC_STSFL<>'X' order by LC_MNLCD";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)                           
		{ 
		  while(M_rstRSSET.next())
		  {	
			tblMELCM.setValueAt(M_rstRSSET.getString("LC_MNLCD"),L_CNT,TB1_MNLCD);		
			tblMELCM.setValueAt(M_rstRSSET.getString("LC_LOCDS"),L_CNT,TB1_LOCDS);
			tblMELCM.setValueAt(M_rstRSSET.getString("LC_MAXQT"),L_CNT,TB1_MAXQT);
		 			
			if(!(M_rstRSSET.getString("LC_STKQT")).equals(hstMELCM.get(M_rstRSSET.getString("LC_MNLCD"))))
			{
				tblMELCM.setValueAt(M_rstRSSET.getString("LC_STKQT"),L_CNT,TB1_STKQT);
				tblMELCM.setValueAt("*",L_CNT,TB1_VRFQT);
			}
			else
			{	
				tblMELCM.setValueAt(M_rstRSSET.getString("LC_STKQT"),L_CNT,TB1_STKQT);
			}
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
					
	   if(L_KE.getKeyCode() == L_KE.VK_ENTER )
       {
		if(M_objSOURC==txtMNLCD1)
		{
		   getDATA();
		   txtMNLCD1.setText(txtMNLCD1.getText().toString().toUpperCase());
		}
       	else
		{	
		    setMSG("",'E');
		}
       }	
	}
	catch(NullPointerException L_NPE)
	{
	  setMSG("inside keyPressed()",'E');                            
	}
  }
  
  	
  /** Method to validate data  */
  boolean vldDATA(int P_intROWNO)
  {
	try
	{
	  if(tblMELCM.getValueAt(P_intROWNO,TB1_MNLCD).toString().length()==0)
	  {
		setMSG("Enter the CODE in The Table",'E');
  		return false;
  	  }
	  if(tblMELCM.getValueAt(P_intROWNO,TB1_LOCDS).toString().length()==0)
	  {
  		setMSG("Enter the Location Description in The Table",'E');
  		return false;
  	  }
	  if(tblMELCM.getValueAt(P_intROWNO,TB1_MAXQT).toString().length()==0)
	  {
  		setMSG("Enter the Maximum Quantity in The Table",'E');
  		return false;
	  }
	  if(tblMELCM.getValueAt(P_intROWNO,TB1_STKQT).toString().length()==0)
	  {
  		setMSG("Enter the Stock Quantity in The Table",'E');
  		return false;
	  }
	}	  
  	catch(Exception L_EX)
  	{
  	  setMSG(L_EX,"This is vldDATA");
  	}	
  	  return true;
  }
  /** One time data capturing for specified codes and stock quantity from fg_stmst
	 * into the Hash Table
	 */
  private void crtMELCM()
  {
    String M_strSQLQRY = "";
      try
      {
		hstMELCM.clear();
	    M_strSQLQRY = "select sum(st_stkqt) STKQT,st_mnlcd from fg_stmst where" ;  
	    M_strSQLQRY+= " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'"; 
		M_strSQLQRY+= " group by st_stkqt,st_mnlcd order by st_mnlcd";
	    ResultSet M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
          if(M_rstRSSET == null || !M_rstRSSET.next())
          {
	        setMSG("Records not found in fg_stmst",'E');
            return;
          }
		  while(true)
          {
			hstMELCM.put(M_rstRSSET.getString("ST_MNLCD"),M_rstRSSET.getString("STKQT"));
			if (!M_rstRSSET.next())
	              break;
          }
		    M_rstRSSET.close();
      }
      catch(Exception L_EX)
      {
             setMSG(L_EX,"crtMELCM");
      }
  }
  
  void exeSAVE()
  {
    int P_intROWNO=0;
    try
    {
      int i=0;
      for(P_intROWNO=0;P_intROWNO<tblMELCM.getRowCount();P_intROWNO++)
      {
        if(tblMELCM.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
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
		for(P_intROWNO=0;P_intROWNO<tblMELCM.getRowCount();P_intROWNO++)
		{
		  if(tblMELCM.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
		  {
			  exeMODREC(P_intROWNO);
		  }
		}
	  }
		
  	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
	    {
    	  for(P_intROWNO=0;P_intROWNO<tblMELCM.getRowCount();P_intROWNO++)
	      {
	    	if(tblMELCM.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
	    	{
	    	  exeDELREC(P_intROWNO);
	    	}
	      }
	    }   
  	  	   	        			
   	  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
      {
    	for(P_intROWNO=0;P_intROWNO<tblMELCM.getRowCount();P_intROWNO++)
    	{
    	  if(tblMELCM.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
    	  {
    		  exeADDREC(P_intROWNO);
    	  }
    	  
    	}  
      }	
	  if(cl_dat.exeDBCMT("exeSAVE"))
	  {
		 tblMELCM.clrTABLE();			
		 setMSG("record updated successfully",'N');
	  }
	  else
	  {
	//shubham	 JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
		 setMSG("Error in updating data..",'E');
	  }
    }  
   	 catch(Exception L_E)
	 {
	  setMSG(L_E,"exeSAVE");
	 }
  }
 /** Method To Insert DATA*/ 
  
 private void exeADDREC(int P_intROWNO)
 { 
	try
	{ 
	  M_strSQLQRY =" insert into FG_LCMST(LC_MNLCD,LC_LOCDS,LC_MAXQT,LC_STKQT,LC_TRNFL,LC_LUSBY,LC_LUPDT,LC_WRHTP,LC_CMPCD,LC_STSFL)";
	  M_strSQLQRY += " values (";
	  M_strSQLQRY += "'"+tblMELCM.getValueAt(P_intROWNO,TB1_MNLCD).toString().toUpperCase()+"',";
	  M_strSQLQRY += "'"+tblMELCM.getValueAt(P_intROWNO,TB1_LOCDS).toString().toUpperCase()+"',";
	  M_strSQLQRY += "'"+tblMELCM.getValueAt(P_intROWNO,TB1_MAXQT).toString()+"',";
	  M_strSQLQRY += "'"+tblMELCM.getValueAt(P_intROWNO,TB1_STKQT).toString()+"',";
      M_strSQLQRY += "'1',";
      M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
	  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
	  M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
	  M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
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
 
 /** Method to Delete the record */
  
 private void exeDELREC( int P_intROWNO)
 {
	 try
	 {
       M_strSQLQRY="update FG_LCMST set";
	   M_strSQLQRY+=" LC_STSFL= 'X'";
	   M_strSQLQRY+=" where LC_MNLCD ='"+tblMELCM.getValueAt(P_intROWNO,TB1_MNLCD).toString()+"'";
	   M_strSQLQRY +=" and LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
	   cl_dat.M_flgLCUPD_pbst = true;
	   cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");		
	 }  
	 catch(Exception L_EX)
	 {
	   setMSG(L_EX,"exeDELREC()");		
	 }
  }
 
 /** Method to modify the record */
 
 private void exeMODREC(int P_intROWNO)
 {
	  try
	  {
		M_strSQLQRY = " Update FG_LCMST set";
		M_strSQLQRY +=" LC_LOCDS='"+tblMELCM.getValueAt(P_intROWNO,TB1_LOCDS).toString().toUpperCase()+"',";
		M_strSQLQRY +=" LC_MAXQT='"+tblMELCM.getValueAt(P_intROWNO,TB1_MAXQT).toString()+"'";
		M_strSQLQRY +=" where LC_MNLCD='"+tblMELCM.getValueAt(P_intROWNO,TB1_MNLCD).toString()+"'";
		M_strSQLQRY +=" and LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
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
    	clrEDITR(tblMELCM);
      }  
      if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
	  {
		  tblMELCM.setEnabled(false);
	  }
	  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)
		 ||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	  
	  {
		 ((JTextField)tblMELCM.cmpEDITR[TB1_STKQT]).setEditable(false);
		 ((JTextField)tblMELCM.cmpEDITR[TB1_MNLCD]).setEditable(false);
		 ((JTextField)tblMELCM.cmpEDITR[TB1_VRFQT]).setEditable(false);
	  }
	  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
	  {
		((JTextField)tblMELCM.cmpEDITR[TB1_STKQT]).setEditable(false);
		((JTextField)tblMELCM.cmpEDITR[TB1_MNLCD]).setEditable(true);
		((JTextField)tblMELCM.cmpEDITR[TB1_MAXQT]).setEditable(true);
		((JTextField)tblMELCM.cmpEDITR[TB1_LOCDS]).setEditable(true);
		((JTextField)tblMELCM.cmpEDITR[TB1_VRFQT]).setEditable(false);
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
	 String L_strMNLCD =""; 
	try
	{	
     if(getSource()==tblMELCM)
	 {
      if(P_intCOLID == TB1_MNLCD)
      {
    	L_strMNLCD = tblMELCM.getValueAt(P_intROWID,TB1_MNLCD).toString().toUpperCase();
    	for(i=0;i<tblMELCM.getRowCount();i++)
	    if(tblMELCM.getValueAt(i,TB1_MNLCD).toString().length()>0 && i!=P_intROWID)
	    {
	      if(L_strMNLCD.equals(tblMELCM.getValueAt(i,TB1_MNLCD).toString()))
	      {
	    	setMSG("Entered Code Already Exist",'E');
	    	return false;
	      }
	       tblMELCM.setValueAt(tblMELCM.getValueAt(P_intROWID,TB1_MNLCD).toString().toUpperCase(),P_intROWID,TB1_MNLCD);
	       tblMELCM.setValueAt("0.000",P_intROWID,TB1_STKQT);
        }
    	   setMSG("",'N');
	   }
	  }
     if(getSource()==tblMELCM)
	  {
	    if(P_intCOLID == TB1_LOCDS)
       {
	    if(tblMELCM.getValueAt(P_intROWID,TB1_LOCDS).toString().length()>0)
	    {	
	      tblMELCM.setValueAt(tblMELCM.getValueAt(P_intROWID,TB1_LOCDS).toString().toUpperCase(),P_intROWID,TB1_LOCDS);
	    }
	     setMSG("",'N');
       }
	  }
       if(M_rstRSSET != null)
       M_rstRSSET.close();
     }	
     catch(Exception L_E)
     {
       setMSG(L_E,"Input Verifier");
     }
       return true;
    }
  }
}

    