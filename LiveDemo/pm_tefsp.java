/*
System Name : Preventive Maintenance.
Program Name :  Format specification.

Purpose : This module used for accepting the attribute of equipment.

Source Directory : d:\source\splerp3\                     
Executable Directory : d:\exec\splerp3\

List of tables used:
Table Name		Primary key					            Operation done
								             Insert   Update	   Query    Delete	
---------------------------------------------------------------------------------------------
PM_ATMST	    AT_ATIND,AT_SRLNO,				/		/			/			/
                AT_EQSTP,AT_EQMTP,
                AT_CMPCD
                
CO_CDTRN        CMT_CGMTP,CMT_CGSTP,CMT_CODCD           	        /            	 
                 
-------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on screen:
Field Name		Column Name			    Table name		      Type/Size	       Description
-------------------------------------------------------------------------------------------------------
txtEQMTP		cmt_codcd/AT_EQMTP	    co_cdtrn/PM_ATMST	  Varchar(3)	   Equipment type code
txtEQMDS		cmt_codds			    co_cdtrn        	  Varchar(50)	   Equipment type Desp.	
txtEQSTP        cmt_codcd/AT_EQSTP      co_cdtrn/PM_ATMST     Varchar(3)       Sub Type
txtEQSDS        cmt_codds       		co_cdtrn              Varchar(50)      Sub Type Description	

Table tblSPECS:
txtSRLNO	    AT_SRLNO			    PM_ATMST		      Varchar(3)       Attribute Serial no
txtATDSC        AT_ATDSC      		    PM_ATMST		      Varchar(50)	   Description
txtATIND        AT_ATIND                PM_ATMST              VARCHAR(1)       Indicator

----------------------------------------------------------------------------------------------------------

List of fields with help facility: 
Field Name	 Display Description		     		Display Columns			  	Table Name
-----------------------------------------------------------------------------------------------------------------------
txtEQMTP	 Equipment main type, Description       CMT_CODCD,CMT_CODDS 		CO_CDTRN/MST/PMXXEQT
txtEQSTP	 Equipment Sub type, Description        CMT_CODCD,CMT_CODDS 		CO_CDTRN/MST/PMXXSTP									PT_ADR01,PT_CTYNM		    
-------------------------------------------------------------------------------------------------------------------

Validations & Other Requirement:

	->Attributes are pre_defined for each equipment type as standards which in turn are displayed automatically in Eqpt-Specification & the value for attribute are accepted.
	->Equipment Main Type is compulsory.
	->In Table Attribute Serial No. is compulsory.
	->Attribute description is compulsory.
	->Indicator is compulsory. 
	->In Addition option all sub types are shown in F1 screen.
	->In Modification, Enquiry, Deletion option only those sub types are shown which are related to  Equipment Main type
	->If user entered Equipment Main type then all data is fetch on that Equipment Maint type.
	->If user entered Equipment Main type & Equipment sub type then data is fetched on both Equipment Main type & Equipment sub type

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import javax.swing.table.*;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;
import javax.swing.*;

public class pm_tefsp extends cl_pbase 
{	
  JTextField txtEQMTP,txtEQMDS,txtEQSTP,txtEQSDS;
  private  cl_JTable tblSPECS;
  
  //for tblSPEC
  int TB1_CHKFL = 0;                   JCheckBox chkCHKFL;
  int TB1_SRLNO = 1;                   JTextField txtSRLNO;
  int TB1_ATDSC = 2;                   JTextField txtATDSC;
  int TB1_ATIND = 3;                   JTextField txtATIND;
  
  private TBLINPVF objTBLVRF;
  private INPVF oINPVF;
  
  int L_CNT=0;        // Row count for tblSPECS in getdata method & focus gain method
     
  pm_tefsp()
  {
	super(2);
	
	try
	{
		setMatrix(20,20);	
		add(new JLabel("Equipment Type"),2,2,1,3,this,'L');  
		add(txtEQMTP = new TxtLimit(3),3,2,1,2,this,'L'); 
		add(txtEQMDS = new TxtLimit(50),3,4,1,5,this,'L'); 
		
		add(new JLabel("Sub Type"),2,10,1,3,this,'L');  
		add(txtEQSTP = new TxtLimit(3),3,10,1,2,this,'L');
		add(txtEQSDS = new TxtLimit(50),3,12,1,5,this,'L'); 
		
		String[] L_strTBLHD4 = {"","Attribute","Description","Indicators"};
		int[] L_intCOLSZ4 = {20,100,450,90};
		tblSPECS = crtTBLPNL1(this,L_strTBLHD4,1000,6,2,8,14,L_intCOLSZ4,new int[]{0});
		
		objTBLVRF = new TBLINPVF();
		oINPVF=new INPVF();
		tblSPECS.addMouseListener(this);
		//tblSPECS.addKeyListener(this);
		tblSPECS.setCellEditor(TB1_CHKFL,chkCHKFL =new JCheckBox());
		tblSPECS.setCellEditor(TB1_SRLNO,txtSRLNO =new TxtNumLimit(3));
		tblSPECS.setCellEditor(TB1_ATDSC,txtATDSC =new TxtLimit(25));
		tblSPECS.setCellEditor(TB1_ATIND,txtATIND =new TxtLimit(1));
		
		txtSRLNO.addFocusListener(this);
	
		tblSPECS.setInputVerifier(objTBLVRF);
		txtEQMTP.setInputVerifier(oINPVF);
		txtEQSTP.setInputVerifier(oINPVF);
		
		setENBL(false);
		txtEQMDS.setEnabled(false);
		txtEQSDS.setEnabled(false);
		
	} 
	catch(Exception L_EX)
	{
	    setMSG(L_EX,"Constructor");
	}
  }
  
  public void actionPerformed(ActionEvent L_AE)
  {
	super.actionPerformed(L_AE);
	    try
	    {
	    	if (M_objSOURC == cl_dat.M_cmbOPTN_pbst) 
	        {
	  		  clrCOMP();
	  		  tblSPECS.clrTABLE();
	  		  clrEDITR(tblSPECS);
	          txtEQMTP.requestFocus();
	          setMSG("Enter the Equipment Main Type Code or Press F1 to Select from List..",'N'); 
	          
	            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		    	{
		    		txtEQMTP.setEnabled(true);
		    		txtEQMDS.setEnabled(false);
		    		txtEQSTP.setEnabled(true);
		    		txtEQSDS.setEnabled(false);
		    	    tblSPECS.setEnabled(false);
		    	}
		    	
	            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		  	    {
		    		txtEQMTP.setEnabled(true);
		    		txtEQMDS.setEnabled(false);
		    		txtEQSTP.setEnabled(true);
		    		txtEQSDS.setEnabled(false);
		    		((JCheckBox)tblSPECS.cmpEDITR[TB1_CHKFL]).setEnabled(true);
		    	    ((JTextField)tblSPECS.cmpEDITR[TB1_SRLNO]).setEditable(false);
			      	((JTextField)tblSPECS.cmpEDITR[TB1_ATDSC]).setEditable(false);
			      	((JTextField)tblSPECS.cmpEDITR[TB1_ATIND]).setEditable(false);
		  	    }
		    
	            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
		    	{
		           txtEQMTP.setEnabled(true);
		           txtEQMDS.setEnabled(false);
		    	   txtEQSTP.setEnabled(true);
		    	   txtEQSDS.setEnabled(false);
		    	   tblSPECS.setEnabled(true);
		    	   ((JCheckBox)tblSPECS.cmpEDITR[TB1_CHKFL]).setEnabled(true);
		    	   ((JTextField)tblSPECS.cmpEDITR[TB1_SRLNO]).setEditable(true);
			       ((JTextField)tblSPECS.cmpEDITR[TB1_ATDSC]).setEditable(true);
			       ((JTextField)tblSPECS.cmpEDITR[TB1_ATIND]).setEditable(true);
		        }
		    	
		    	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		    	{
		    		 txtEQMTP.setEnabled(true);
			         txtEQMDS.setEnabled(false);
			    	 txtEQSTP.setEnabled(true);
			    	 txtEQSDS.setEnabled(false);
			    	 tblSPECS.setEnabled(true);
			    	 ((JCheckBox)tblSPECS.cmpEDITR[TB1_CHKFL]).setEnabled(true);
			    	 ((JTextField)tblSPECS.cmpEDITR[TB1_SRLNO]).setEditable(false);
				     ((JTextField)tblSPECS.cmpEDITR[TB1_ATDSC]).setEditable(true);
				     ((JTextField)tblSPECS.cmpEDITR[TB1_ATIND]).setEditable(false);
		    	}
	        }
	    }
	    catch(Exception L_EX)
	    {
	      setMSG(L_EX,"exeMODREC()");
	    }
    }
  
  
  public void keyPressed(KeyEvent L_KE)
  {
    super.keyPressed(L_KE);
	try
	{
		if(L_KE.getKeyCode() == L_KE.VK_F1 )
    	{						
			if(M_objSOURC==txtEQMTP)	
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtEQMTP";
    		  String L_ARRHDR[] = {"Equipment Code","Equipment Description"};
    		  M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
    		  M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXEQT' AND isnull(CMT_STSFL,'')<>'X'";
    		  if(txtEQMTP.getText().length()>0)				
			  M_strSQLQRY+= " AND cmt_codcd like '"+txtEQMTP.getText().trim()+"%'";
    		  M_strSQLQRY += " order by cmt_codcd";  
    		  System.out.println("equipment"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    		}
			else if(M_objSOURC==txtEQSTP)	
    		{
			   if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			   {	
				   if(txtEQMTP.getText().length()>0)
				   {
					   cl_dat.M_flgHELPFL_pbst = true;
			    	   M_strHLPFLD = "txtEQSTP";
			    	   String L_ARRHDR[] = {"Sub Type Code","Sub Type Description"};
			    	   M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
			    	   M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXEST' AND isnull(CMT_STSFL,'')<>'X'";
			    	   if(txtEQSTP.getText().length()>0)				
					   M_strSQLQRY+= " AND cmt_codcd like '"+txtEQSTP.getText().trim()+"%'";
					   M_strSQLQRY += " order by cmt_codcd";
					   System.out.println("subtype for add"+M_strSQLQRY);
			    	   cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");  
				   }
			   }
			 
			   else if(!(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
			   {		 
				   if(txtEQMTP.getText().length()>0)
				   {
					  cl_dat.M_flgHELPFL_pbst = true;
			    	  M_strHLPFLD = "txtEQSTP";
			    	  String L_ARRHDR[] = {"Sub Type Code","Sub Type Description"}; 
		    		  M_strSQLQRY = "select distinct AT_EQSTP,A.CMT_CODDS STPDS from PM_ATMST,co_cdtrn A where" ;
		    		  M_strSQLQRY+= " AT_EQMTP ='"+txtEQMTP.getText()+"'";
		    		  if(txtEQSTP.getText().trim().length()>0)
		    		  M_strSQLQRY+= " And AT_EQSTP like ='"+txtEQSTP.getText().trim()+"%'";
		    		  M_strSQLQRY+= " And isnull(AT_STSFL,'')<>'X'";
		    		  M_strSQLQRY+=" and AT_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'"; 
		    		  M_strSQLQRY+=" and A.CMT_CGMTP ='MST' AND A.CMT_CGSTP = 'PMXXEST' AND isnull(A.CMT_STSFL,'')<>'X' AND A.CMT_CODCD=AT_EQSTP";
		    		  M_strSQLQRY+=" order by AT_EQSTP";
		    		  System.out.println("subtype expect add"+M_strSQLQRY);
		    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");  
				   }
			   }  
    		}
    	}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER )
	    {
			if(M_objSOURC==txtEQMTP)
			{
				if(txtEQMTP.getText().length()==0)
	    	    	txtEQMDS.setText("");
				txtEQMTP.setText(txtEQMTP.getText().toUpperCase());
				txtEQSTP.requestFocus();
				setMSG("Enter the Equipment Sub Type Code or Press F1 to Select from List..",'N');	
		    }
			else if(M_objSOURC==txtEQSTP)
			{
				 if(txtEQSTP.getText().length()==0)
		    	    	txtEQSDS.setText("");
				 txtEQSTP.setText(txtEQSTP.getText().toUpperCase());
				tblSPECS.requestFocus();	
			}
			else if(M_objSOURC == txtATIND)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
	    }	
	}  
   	catch(NullPointerException L_NPE)
	{
	  setMSG("keyPressed",'E');  
	}
  }
  
  void exeHLPOK()
  {
 	try
 	{
 	  super.exeHLPOK();
 	   if(M_strHLPFLD.equals("txtEQMTP"))
 	   {
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		txtEQMTP.setText(L_STRTKN.nextToken());
		txtEQMDS.setText(L_STRTKN.nextToken());
 	   }
 	   else if(M_strHLPFLD.equals("txtEQSTP"))
	   {
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		txtEQSTP.setText(L_STRTKN.nextToken());
		txtEQSDS.setText(L_STRTKN.nextToken());
	   }
 	}
 	catch(Exception L_EX)
 	{
 		setMSG(L_EX,"exeHLPOK"); 
 	}
  }	
  
  /**method to focus Gained in empty field to add new record & in wrk date to set To Date**/
  public void focusGained(FocusEvent L_FE)
  {
		super.focusGained(L_FE);
		if(M_objSOURC == txtSRLNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				  if(tblSPECS.getSelectedRow()>=L_CNT)
				  {
					  ((JCheckBox)tblSPECS.cmpEDITR[TB1_CHKFL]).setEnabled(true); 
					  //tblSPECS.setValueAt(new Boolean(true),tblSPECS.getSelectedRow(),TB1_CHKFL);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_SRLNO]).setEditable(true);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATDSC]).setEditable(true);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATIND]).setEditable(true);
				  }
				  else
				  {
					  ((JCheckBox)tblSPECS.cmpEDITR[TB1_CHKFL]).setEnabled(false);
					  //tblSPECS.setValueAt(new Boolean(false),tblSPECS.getSelectedRow(),TB1_CHKFL);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_SRLNO]).setEditable(false);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATDSC]).setEditable(false); 
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATIND]).setEditable(false);
				  }
			}	
			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				  if(tblSPECS.getSelectedRow()>=L_CNT)
				  {
					  ((JCheckBox)tblSPECS.cmpEDITR[TB1_CHKFL]).setEnabled(false);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_SRLNO]).setEditable(false);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATDSC]).setEditable(false);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATIND]).setEditable(false);
				  }
				  else
				  {
					  ((JCheckBox)tblSPECS.cmpEDITR[TB1_CHKFL]).setEnabled(true); 
					  ((JTextField)tblSPECS.cmpEDITR[TB1_SRLNO]).setEditable(false);
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATDSC]).setEditable(true); 
					  ((JTextField)tblSPECS.cmpEDITR[TB1_ATIND]).setEditable(false);
				  }
			}
		}
   }
  
  void clrEDITR(cl_JTable tblTABLE)
  {
	  if(tblTABLE.isEditing())
	  tblTABLE.getCellEditor().stopCellEditing();
	  tblTABLE.setRowSelectionInterval(0,0);
	  tblTABLE.setColumnSelectionInterval(0,0);	
  }
  
  
	/** Method to get Data from PM_ATMST in tblSPECS*/
  void getDATA()
  {
	  try
	  {
		  tblSPECS.clrTABLE();
		  clrEDITR(tblSPECS);
		  M_strSQLQRY = "select AT_SRLNO,AT_ATDSC,AT_ATIND from PM_ATMST where" ;
		  M_strSQLQRY+= " AT_EQMTP ='"+txtEQMTP.getText()+"'";
		  if(txtEQSTP.getText().trim().length()>0)
		  M_strSQLQRY+= " And AT_EQSTP ='"+txtEQSTP.getText()+"'";
		  M_strSQLQRY+= " And isnull(AT_STSFL,'')<>'X'";
		  M_strSQLQRY+=" and AT_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'order by AT_SRLNO"; 
		  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		  System.out.println("getData"+M_strSQLQRY);
		  L_CNT=0;
		  
		  if(M_rstRSSET !=null)                           
		  { 
			 while(M_rstRSSET.next())
			 {
				 //tblSFWFL.setValueAt(new Boolean(false),L_CNT,TB4_CHKFL);   
				tblSPECS.setValueAt(nvlSTRVL(M_rstRSSET.getString("AT_SRLNO"),""),L_CNT,TB1_SRLNO);		
				tblSPECS.setValueAt(nvlSTRVL(M_rstRSSET.getString("AT_ATDSC"),""),L_CNT,TB1_ATDSC);
				tblSPECS.setValueAt(nvlSTRVL(M_rstRSSET.getString("AT_ATIND"),""),L_CNT,TB1_ATIND);
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
  
  /** Method to validate data  */
  boolean vldDATA()
  {
	  String L_strSRLNO = "";
	try
	{
			
			if(txtEQMTP.getText().toString().length()== 0)
			{
				setMSG("Enter Equipment Main Type or Press F1 to Select from List..",'E');
				txtEQMTP.requestFocus();
				return false;
			}
			else if(txtEQSTP.getText().toString().length()== 0)
			{
				setMSG("Enter Equipment Sub Type or Press F1 to Select from List..",'E');
				txtEQSTP.requestFocus();
				return false;
			}
			//else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				for(int P_intROWNO=0;P_intROWNO<tblSPECS.getRowCount();P_intROWNO++)
				{
					if(tblSPECS.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
				
						if(tblSPECS.getValueAt(P_intROWNO,TB1_SRLNO).toString().length()==0)
						{
					  		setMSG("Enter Attribute Serial no. in The Table",'E');
					  		return false;
					  	}
						if(tblSPECS.getValueAt(P_intROWNO,TB1_ATDSC).toString().length()==0)
						{
					  		setMSG("Enter Attribute Description in The Table",'E');
					  		return false;
					  	}
						if(tblSPECS.getValueAt(P_intROWNO,TB1_ATIND).toString().length()==0)
						{
					  		setMSG("Enter Indicator in The Table",'E');
					  		return false;
						}
					}
				}
			}
		    if(tblSPECS.getValueAt(tblSPECS.getSelectedRow(),TB1_CHKFL).toString().equals("false"))
			{
				if(tblSPECS.getValueAt(tblSPECS.getSelectedRow(),TB1_SRLNO).toString().length()>0)
				{
					setMSG("No row Selected in Format Specification table..",'E');	
					tblSPECS.requestFocus();
					return false;
				}
			}
		/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
        {
			if(tblSPECS.getValueAt(tblSPECS.getSelectedRow(),TB1_SRLNO).toString().length()>0);
			{
				M_strSQLQRY = "select AT_SRLNO from PM_ATMST where" ;
				M_strSQLQRY+= " AT_EQMTP ='"+txtEQMTP.getText()+"'";
				if(txtEQSTP.getText().trim().length()>0)
					M_strSQLQRY+= " And AT_EQSTP ='"+txtEQSTP.getText()+"'";
				M_strSQLQRY+= " And AT_SRLNO ='"+tblSPECS.getValueAt(tblSPECS.getSelectedRow(),TB1_SRLNO).toString()+"'";
				M_strSQLQRY+=" and AT_ATIND ='"+tblSPECS.getValueAt(tblSPECS.getSelectedRow(),TB1_ATIND).toString()+"'";
				M_strSQLQRY+= " And isnull(AT_STSFL,'')<>'X'";
				M_strSQLQRY+=" and AT_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'"; 
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				System.out.println("getData vld"+M_strSQLQRY);
				if(M_rstRSSET !=null)                           
				{ 
				   while(M_rstRSSET.next())
				   {
					   if(M_rstRSSET.getString("AT_SRLNO").trim().length()>0)
						L_strSRLNO = M_rstRSSET.getString("AT_SRLNO");	
				   }
				} 
				     
	     			if(tblSPECS.getValueAt(tblSPECS.getSelectedRow(),TB1_CHKFL).toString().equals("true"))
	     			{
	     				tblSPECS.getValueAt(tblSPECS.getSelectedRow(),TB1_SRLNO).toString().equals(L_strSRLNO);
	     				setMSG("Checked value is already saved in the database table.",'E');
				  		return false;
	     			}
		    }	
        }**/	
	}	  
  	catch(Exception L_EX)
  	{
  	  setMSG(L_EX,"This is vldDATA");
  	}	
  	return true;
  }
  
  /**save record after click on save button**/
  void exeSAVE()
  {
    int P_intROWNO=0;
    try
    {
	    if(!vldDATA())
	  	{
		  	//cl_dat.M_btnSAVE_pbst.setEnabled(true);
		  	return;
	  	}
	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
        {
     		for(P_intROWNO=0;P_intROWNO<tblSPECS.getRowCount();P_intROWNO++)
     		{
     			if(tblSPECS.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
     			{
     				exeADDREC(P_intROWNO);
     			}
     		} 
        }	  
        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
  	    {
        	for(P_intROWNO=0;P_intROWNO<tblSPECS.getRowCount();P_intROWNO++)
	  		{
        		if(tblSPECS.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
        		{
        			exeMODREC(P_intROWNO);
        		}
	  		}
  	    }
        
    	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
  	    {
    		for(P_intROWNO=0;P_intROWNO<tblSPECS.getRowCount();P_intROWNO++)
    		{
    			if(tblSPECS.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
    			{
    				exeDELREC(P_intROWNO);
    			}
  	      	}
  	    }    	   	        			
    	if(cl_dat.exeDBCMT("exeSAVE"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG(" Data Saved Successfully..",'N'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				setMSG(" Data Modified Successfully..",'N'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Data Deleted Successsfully ..",'N');
			tblSPECS.clrTABLE();
			clrEDITR(tblSPECS);
     		clrCOMP();
		}
		else
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG("Error in saving details..",'E'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			   setMSG("Error in Modified Data details..",'E'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Error in Deleting data..",'E');
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
		  	String strSQLQRY=" select count(*) from PM_ATMST where";
		    strSQLQRY+=" AT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(AT_STSFL,'')<>'X' AND AT_EQMTP ='"+txtEQMTP.getText()+"'";
			strSQLQRY +=" and AT_EQSTP ='"+txtEQSTP.getText()+"'";
			strSQLQRY +=" and AT_SRLNO ='"+tblSPECS.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"'";
			strSQLQRY +=" and AT_ATIND ='"+tblSPECS.getValueAt(P_intROWNO,TB1_ATIND).toString().toUpperCase()+"'";
			ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			//System.out.println(">>>Count>>"+strSQLQRY);
			if(rstRSSET.next() && rstRSSET != null)
			{
				if(rstRSSET.getInt(1)>0)
				{
				  exeMODREC(P_intROWNO);
				}
				else
				{
					M_strSQLQRY =" insert into PM_ATMST(AT_EQMTP,AT_EQSTP,AT_SRLNO,AT_ATDSC,AT_ATIND,AT_LUSBY,AT_LUPDT,AT_TRNFL,AT_STSFL,AT_CMPCD,AT_SBSCD)";
					M_strSQLQRY += " values (";
					M_strSQLQRY += "'"+txtEQMTP.getText()+"',";
					M_strSQLQRY += "'"+txtEQSTP.getText()+"',";
					M_strSQLQRY += "'"+tblSPECS.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"',";
					M_strSQLQRY += "'"+tblSPECS.getValueAt(P_intROWNO,TB1_ATDSC).toString()+"',";
					M_strSQLQRY += "'"+tblSPECS.getValueAt(P_intROWNO,TB1_ATIND).toString().trim().toUpperCase()+"',";
					M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
					M_strSQLQRY += "'1',";
					M_strSQLQRY += "'0',";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+M_strSBSCD+"')";
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					System.out.println("insert"+M_strSQLQRY);
				}
			}
	  }
	  catch(Exception L_EX)
	  {
		  cl_dat.M_flgLCUPD_pbst=false;
		  setMSG(L_EX,"exeADDREC()"); 
	  }
  }
  
  /** Method to modify data  */
  private void exeMODREC(int P_intROWNO)
  {
	  try
	  {
		M_strSQLQRY = " Update PM_ATMST set";
		M_strSQLQRY +=" AT_ATDSC ='"+tblSPECS.getValueAt(P_intROWNO,TB1_ATDSC).toString().toUpperCase()+"',";
		M_strSQLQRY +=" AT_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
		M_strSQLQRY +=" AT_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
		M_strSQLQRY +=" where AT_EQMTP ='"+txtEQMTP.getText()+"'";
		M_strSQLQRY +=" and AT_EQSTP ='"+txtEQSTP.getText()+"'";
		M_strSQLQRY +=" and AT_SRLNO ='"+tblSPECS.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"'";
		M_strSQLQRY +=" and AT_ATIND ='"+tblSPECS.getValueAt(P_intROWNO,TB1_ATIND).toString().toUpperCase()+"'";
		M_strSQLQRY+=" and AT_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
		System.out.println(">>>update>>"+M_strSQLQRY);
		cl_dat.M_flgLCUPD_pbst = true;
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");		
	  }
	  catch(Exception L_EX)
	  {
		  	setMSG(L_EX,"exeMODREC()");
	  }
  }
  
  /** Method to delete data  */
  private void exeDELREC(int P_intROWNO)
  {
	  try
	  {
		M_strSQLQRY="update PM_ATMST set";
		M_strSQLQRY+=" AT_STSFL = 'X'";
		M_strSQLQRY+=" where AT_EQSTP ='"+txtEQSTP.getText()+"'and AT_EQMTP ='"+txtEQMTP.getText()+"' and AT_SRLNO ='"+tblSPECS.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"'";
		M_strSQLQRY+=" and AT_ATIND ='"+tblSPECS.getValueAt(P_intROWNO,TB1_ATIND).toString()+"'";
		M_strSQLQRY+=" and AT_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
		cl_dat.M_flgLCUPD_pbst = true;
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
		System.out.println(">>>delete>>"+M_strSQLQRY);
	  }  
	  catch(Exception L_EX)
	  {
		  setMSG(L_EX,"exeDELREC()");		
	  }
  }
  
  /**
	 * Method to clear data*/
	void clrCOMP()
	{
		try
		{
			txtEQMTP.setText(""); 
			txtEQMDS.setText("");
			txtEQSTP.setText("");
			txtEQSDS.setText("");
			
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
  
  /**Verify fields for valid data entry***/  
    class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{						
				if(input == txtEQMTP)
				{
					if(txtEQMTP.getText().toString().length()== 0)
					{
						setMSG("Enter Equipment Main Type..",'E');
						txtEQMTP.requestFocus();
						return false;
					}
						
					M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
		    		M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXEQT'";
		    		M_strSQLQRY+= " and cmt_codcd = '"+txtEQMTP.getText().toUpperCase()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					System.out.println("INPVF txtEQMTP : "+M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
					   txtEQMDS.setText(M_rstRSSET.getString("cmt_codds"));
					   setMSG("",'N');
					}
					else
					{
						txtEQMDS.setText("");
						txtEQMTP.requestFocus();
						setMSG("Enter Valid Equipment Main Type Code",'E');
						return false;
					}
					M_rstRSSET.close();	
				}
				
				
				else if(input == txtEQSTP)
				{
					try
					{	
						if(txtEQSTP.getText().toString().length()== 0)
						{
							setMSG("Enter Equipment Sub Type..",'E');
							txtEQSTP.requestFocus();
							return false;
						}
						M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
				    	M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXEST'";
				    	M_strSQLQRY+= " and cmt_codcd = '"+txtEQSTP.getText().toUpperCase()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						System.out.println("INPVF txtEQSTP : "+M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
						{
							 txtEQSDS.setText(M_rstRSSET.getString("cmt_codds"));	
							 setMSG("",'N');
							 getDATA();
						}	
						else
						{
							 txtEQSDS.setText("");	
							 setMSG("Enter Valid Equipment Sub Code",'E');
							 txtEQSTP.requestFocus();
							 return false;
						}
						M_rstRSSET.close();
					 }
					 catch(Exception E_VR)
					 {
						setMSG(E_VR,"sub type verify");		
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
  
    /**Verify table for valid data entry***/  
  private class TBLINPVF extends TableInputVerifier
  {
    public boolean verify(int P_intROWID,int P_intCOLID)
    { 
  	    String L_strLVDT =""; 
		try
		{
				
			if(getSource()==tblSPECS)
			{
		       if(P_intCOLID == TB1_SRLNO)
		       {  
				    L_strLVDT = tblSPECS.getValueAt(P_intROWID,TB1_SRLNO).toString();
				    	
				     if(((JCheckBox) tblSPECS.cmpEDITR[TB1_CHKFL]).isSelected())
				     {	 
				    	for(int i=0;i<tblSPECS.getRowCount();i++)
				    	{	
						    if(tblSPECS.getValueAt(i,TB1_SRLNO).toString().length()>0 && i!=P_intROWID)
						    {	  
						      if(L_strLVDT.equals(tblSPECS.getValueAt(i,TB1_SRLNO).toString()))
						      {
						    	setMSG("Entered Attribute serial number Already Exist",'E');
						    	return false;
						      }
						      setMSG("",'N');
						    } 
				    	}
				    	if(tblSPECS.getValueAt(P_intROWID,TB1_SRLNO).toString().length()<3)
					    {	
						  setMSG("Attribute Serial No. should be in three digits",'E');	
						  return false;
					    }
				    	setMSG("",'N');
				     }	
		       }
			       
		       if(P_intCOLID == TB1_ATDSC)
		       { 
		    	   tblSPECS.setValueAt(tblSPECS.getValueAt(P_intROWID,TB1_ATDSC).toString().replace("'","`"),P_intROWID,TB1_ATDSC);
		    	   tblSPECS.setValueAt(tblSPECS.getValueAt(P_intROWID,TB1_ATDSC).toString().toUpperCase(),P_intROWID,TB1_ATDSC);  
		       }  
		       
		       if(P_intCOLID == TB1_ATIND)
		       { 
		    	     tblSPECS.setValueAt(tblSPECS.getValueAt(P_intROWID,TB1_ATIND).toString().toUpperCase(),P_intROWID,TB1_ATIND);
		    	     
			         if(!(tblSPECS.getValueAt(P_intROWID,TB1_ATIND).toString().equals("Y") || tblSPECS.getValueAt(P_intROWID,TB1_ATIND).toString().equals("N")))
				     {	
					   setMSG("Attribute Indicator should be 'Y' or 'N'",'E');	
					   return false;
				     }
			         setMSG("",'N');
		       }   
			}	
		}	
	  	catch(Exception L_E)
		{
	  		setMSG(L_E,"Table Verifier");
		}
	  	return true;
    }
  }
}
 