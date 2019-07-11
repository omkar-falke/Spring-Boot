/*
System Name   : Software (System Configuration)
Program Name  : sa_qrisl
Program Desc. : Query program to display installed software on computer
Author        : Arati Nare
Date          : 29/01/09
Version       : 
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

/**  Software List program
	 */
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.text.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.InputVerifier;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox; import javax.swing.JLabel;
import javax.swing.table.DefaultTableColumnModel;
import java.util.Timer;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;
import java.io.*;
import java.sql.*;
import javax.activation.FileDataSource;
import javax.swing.border.*;
import java.awt.Color;

public class sa_qrisl extends cl_pbase
{		 
	  String msg1 = "Collecting Information ...";

	  String strFLAG="";/** String variable for selected Radio_button.*/
	  
	  protected boolean M_flgERROR;
	  
	  private JLabel lblIPADS;
	  private JLabel lblDNSPRM;
	  private JLabel lblDNSSEC;
	  private JLabel lblCMPNM;
	  
	  private JCheckBox chkREFRSH;
	  
	  private JPanel pnlDELFIL;
	  
	  JButton btnADDISL,btnDELISL,btnPRINT,btnUPDATE;
	  private cl_JTable tblISFWL;/** table for installed software list*/
	  //private cl_JTable tblSWDTL;/** table for Software details*/
	  private cl_JTable tblUNISFW;/** table for unwanted installed software list*/
	  private cl_JTable tblSFWFL;/** table to display files*/
	  private JTabbedPane jtpMANTAB;/** Tab for SWDTL table and ADDTL table */
	  private JPanel pnlISFWL,pnlSFWFL,pnlPRINT;/** panel for table*/
	  //private JPanel pnlSWDTL;
	  
	  /** Sowftware List table */
	  private final int TB1_CHKFL =0;   JCheckBox chkSFL;
	  private final int TB1_ISWNM =1;   JTextField txtISWNM;
	    
	  
	  /** for specifis columns of SWDTL */
	 /* private	final int TB2_CHKFL =0;	
	  private	final int TB2_SFTCD =1;	
	  private	final int TB2_LICTP =2;
	  private	final int TB2_LICNO =3;	
	  private	final int TB2_USRNO =4;
	  private	final int TB2_LOCDS =5;**/
	  
	  
	  /**Unwanted  Sowftware List table */
	  private final int TB3_CHKFL =0;   JCheckBox chkISF;
	  private final int TB3_ISWNM =1;
	  
	  
	  /**File List table */
	  private final int TB4_CHKFL =0;  JCheckBox chkFLENM;
	  private final int TB4_FILENM =1;
	  
	  
	  
	  private int  intROWCNT =200;/** Integer for counting a Row no*/
	  
	  boolean flgCHK_EXIST;	
	  Cursor curWTSTS = new Cursor(Cursor.WAIT_CURSOR);
	  Cursor curDFSTS = new Cursor(Cursor.DEFAULT_CURSOR);
	  private static String M_strCMPCD_prst = "01";
	  private static String M_strCMPNM_prst = "";
	  
	 
	  private Vector<String> vtrUNISFW;
	  private Vector<String> vtrSFWL1;
	  
	  private JRadioButton rdbISFWL,rdbUNISFW;
	  private ButtonGroup btgPRINT;
	  
	  private TBLINPVF objTBLVRF;
	      		   
	  sa_qrisl()
	  {
		 super(2);
		 setMatrix(20,20);		
		 try
		 { 
			 
			add(chkREFRSH = new JCheckBox(),1,1,1,1,this,'L');
			add(new JLabel("Refresh"),1,2,1,3,this,'L');
			chkREFRSH.addMouseListener(this); 
	
			String[] LM_TBLHD3 = {"","File List"};
			int[] LM_COLSZ3 = {5,150};
			tblSFWFL = crtTBLPNL1(this,LM_TBLHD3,intROWCNT,2,1,3,4,LM_COLSZ3,new int[]{0});
			tblSFWFL.setCellEditor(TB4_CHKFL,chkFLENM = new JCheckBox());
			tblSFWFL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			
			pnlPRINT = new JPanel();
			pnlPRINT.setBorder(new EtchedBorder(Color.gray,Color.lightGray)); 
		    btgPRINT=new ButtonGroup();
			add(rdbISFWL=new JRadioButton("Software List"),1,3,1,4,pnlPRINT,'L');
			add(rdbUNISFW=new JRadioButton("Unwanted Software List"),1,4,1,4,pnlPRINT,'R');
			btgPRINT.add(rdbISFWL); 
			btgPRINT.add(rdbUNISFW);
			add(btnPRINT = new JButton("PRINT"),3,3,1,2,pnlPRINT,'L');
			btnPRINT.setEnabled(true);
			add(pnlPRINT,2,5,3,7,this,'L');
		
				
			pnlISFWL = new JPanel();
			//pnlSWDTL = new JPanel();
			jtpMANTAB = new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			
			pnlISFWL.setLayout(null);
			String[] LM_TBLHD = {"","Software List"};
			int[] LM_COLSZ = {5,350};
		    tblISFWL = crtTBLPNL1(pnlISFWL,LM_TBLHD,intROWCNT,1,1,9,8,LM_COLSZ,new int[]{0});
		    
		    add(btnADDISL = new JButton(">>>"),2,10,1,2,pnlISFWL,'L');
			btnADDISL.setEnabled(false);
			
			add(btnDELISL = new JButton("<<<"),4,10,1,2,pnlISFWL,'L');
			btnDELISL.setEnabled(true);
			
			add(btnUPDATE = new JButton("UPDATE"),6,10,1,2,pnlISFWL,'L');
			btnUPDATE.setEnabled(true);
			
		    
		    String[] L_TBLHD = {""," Unwanted Software List"};
			int[] L_COLSZ = {5,350};
		    tblUNISFW = crtTBLPNL1(pnlISFWL,L_TBLHD,intROWCNT,1,13,9,7,L_COLSZ,new int[]{0});
		    add(pnlISFWL,1,1,10,10,jtpMANTAB,'L');
		    
		    tblISFWL.setCellEditor(TB1_CHKFL,chkSFL = new JCheckBox());
		    tblUNISFW.setCellEditor(TB3_CHKFL,chkISF = new JCheckBox());
		    
		    add(new JLabel("IP ADDRESS OF :"),10,1,1,4,pnlISFWL,'L');
			add(lblIPADS=new JLabel(),10,3,1,7,pnlISFWL,'L');
			
			add(new JLabel("DNS SERVER Primary Value Of:"),11,1,1,4,pnlISFWL,'L');
			add(lblDNSPRM=new JLabel(),11,5,1,7,pnlISFWL,'L');
			add(new JLabel("DNS SERVER Secondary Value Of:"),11,10,1,5,pnlISFWL,'L');
			add(lblDNSSEC=new JLabel(),11,14,1,7,pnlISFWL,'L');
		    
		    /*pnlSWDTL.setLayout(null);
			String[] L_strTBLHD1 = {"Select","Code","No.of user"};// Table Header
			int[]L_intCOLSZ = {40,100,75}; // Columnm Size in Table  
			tblSWDTL = crtTBLPNL1(pnlSWDTL,L_strTBLHD1,intROWCNT,1,2,10,5,L_intCOLSZ,new int[]{0});
			
			String[] L_strTBLHD2 = {"Select","Code","Dept","Computer Name"};// Table Header
			int[]L_intCOLSZ2 = {40,90,80,100}; // Columnm Size in Table  
			tblSWDTL = crtTBLPNL1(pnlSWDTL,L_strTBLHD2,intROWCNT,1,11,10,7,L_intCOLSZ2,new int[]{0});
		    add(pnlSWDTL,1,1,10,12,jtpMANTAB,'L');**/
		    
		    
		    jtpMANTAB.addTab("Installed Software",pnlISFWL);	
			//jtpMANTAB.addTab("Software details",pnlSWDTL);	
			add(jtpMANTAB,5,1,13,20,this,'L');
	
			
			
			((JTextField)tblISFWL.cmpEDITR[TB1_ISWNM]).setEditable(false);
			((JTextField)tblUNISFW.cmpEDITR[TB3_ISWNM]).setEditable(false);
			((JTextField)tblSFWFL.cmpEDITR[TB4_FILENM]).setEditable(false);
			
			 objTBLVRF = new TBLINPVF();
			 tblISFWL.setInputVerifier(objTBLVRF);
			 tblUNISFW.setInputVerifier(objTBLVRF);
			 tblSFWFL.setInputVerifier(objTBLVRF);
 
			 vtrUNISFW = new Vector<String>();
			 vtrSFWL1 = new Vector<String>();
			  
		     setENBL(false);
		     tblISFWL.addMouseListener(this); 
		     chkISF.addMouseListener(this); 
		     chkSFL.addMouseListener(this);
		     chkFLENM.addMouseListener(this); 
		     tblSFWFL.addMouseListener(this); 
		    
		     vtrUNISFW.clear();
			 vtrSFWL1.clear();
		  }
		  catch(Exception L_EX)
		  {
			 System.out.println("constructor "+L_EX);
		  }		  
	  }
	    
	  void setENBL(boolean P_flgENBFL)
	   {
			super.setENBL(P_flgENBFL);
			//tblSWDTL.clrTABLE();
			tblISFWL.clrTABLE();
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
					setENBL(true);
					//tblSWDTL.setEnabled(false);
					getDATA2();
					exeREAD();
				}
			}
	       
	       if(M_objSOURC == btnUPDATE)
		    {  
	    	   for(int P_intROWNO=0;P_intROWNO<tblSFWFL.getRowCount();P_intROWNO++)
			   {
			      if(tblSFWFL.getValueAt(P_intROWNO,TB4_CHKFL).toString().equals("true"))
			      {
	        	     exeDELFILE();
			      }
			   }  
		    } 
	       
	        if(M_objSOURC == btnADDISL)
		    {
	          int L_ROWNO1=0;
	  		  int L_ROWNO2=0;
	        	for(int P_intROWNO=0;P_intROWNO<tblISFWL.getRowCount();P_intROWNO++)
			    {
			      if(tblISFWL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
			      {
			    	  if(tblISFWL.getValueAt(P_intROWNO,TB1_ISWNM).toString().length()>0)
			    	  exeADDREC(P_intROWNO);  
			      } 
			    }
	        	  tblISFWL.clrTABLE();
		 		  inlTBLEDIT(tblISFWL);
		 		  exeSORT(vtrSFWL1);
		 		  for(int i=0; i<vtrSFWL1.size(); i++)
		    	  {	
		    	    tblISFWL.setValueAt(nvlSTRVL(vtrSFWL1.get(i).toString(),""),L_ROWNO1,TB1_ISWNM);
		    	    L_ROWNO1++;
		    	  }
		 		  tblUNISFW.clrTABLE();
		 		  inlTBLEDIT(tblUNISFW);
		 		  exeSORT(vtrUNISFW);
			 	  for(int j=0; j<vtrUNISFW.size(); j++)
			      {	 	
			 		  tblUNISFW.setValueAt(nvlSTRVL(vtrUNISFW.get(j).toString(),""),L_ROWNO2,TB3_ISWNM);
			    	  L_ROWNO2++;
			      } 
			 	 if(tblISFWL.getValueAt(tblISFWL.getSelectedRow(),TB1_CHKFL).toString().equals("false"))
				  {	
			    	  setMSG("Pls select row from Software List table",'E');
				  }
		     }
	        
	        if(M_objSOURC ==  btnDELISL)
		    {
	          int L_ROWNO1=0;
		  	  int L_ROWNO2=0;
	        	for(int P_intROWNO=0;P_intROWNO<tblUNISFW.getRowCount();P_intROWNO++)
			    {
			      if(tblUNISFW.getValueAt(P_intROWNO,TB3_CHKFL).toString().equals("true"))
			      {
			    	  String strDELSW = tblUNISFW.getValueAt(P_intROWNO,TB3_ISWNM).toString();
			    	  if(strDELSW.length()>0)
			    	  exeDELREC(P_intROWNO);
			      }
			    }
		 		  tblISFWL.clrTABLE();
		 		  inlTBLEDIT(tblISFWL);
		 		  exeSORT(vtrSFWL1);
			 		for(int i=0; i<vtrSFWL1.size(); i++)
			    	  {	 	
			    	    tblISFWL.setValueAt(nvlSTRVL(vtrSFWL1.get(i).toString(),""),L_ROWNO1,TB1_ISWNM);
			    	    L_ROWNO1++;
			    	  }
			 	  tblUNISFW.clrTABLE();
				  inlTBLEDIT(tblUNISFW);
			 	  exeSORT(vtrUNISFW);
			 		for(int j=0; j<vtrUNISFW.size(); j++)
			    	  {	 	
			 			tblUNISFW.setValueAt(nvlSTRVL(vtrUNISFW.get(j).toString(),""),L_ROWNO2,TB3_ISWNM);
			    	    L_ROWNO2++;
			    	  }	
			 		
			 		if(tblUNISFW.getValueAt(tblUNISFW.getSelectedRow(),TB3_CHKFL).toString().equals("false"))
					  {	
				    	  setMSG("Pls select row from  Unwanted Software List table",'E');
					  }
		     }
	        
	        if(M_objSOURC == chkREFRSH)
		    {
	        	exeREFRESH();
	        	exeREAD();
		    }
	        
	        if(M_objSOURC == btnPRINT)
		    {
	        	sa_rpisl osa_rpisl = new sa_rpisl(rdbISFWL.isSelected(),rdbUNISFW.isSelected());
				osa_rpisl.exeDSPRP();				
		    }
	         
	     }
	     catch(Exception L_EA)
	     {
	       setMSG(L_EA,"Action Performed");
	     }
	  }
	     
	    public void mouseReleased(MouseEvent L_KE)
		{
	    	super.mouseReleased(L_KE);
			try
			{ 	 
				 if(M_objSOURC == chkFLENM)
			     {
				      if(((JCheckBox)tblSFWFL.cmpEDITR[TB4_CHKFL]).isSelected())
				      {
				    	if(tblSFWFL.getValueAt(tblSFWFL.getSelectedRow(),TB4_CHKFL).toString().equals("true"))
					   	{  
				    	 String strFLNME = tblSFWFL.getValueAt(tblSFWFL.getSelectedRow(),TB4_FILENM).toString();
				    	 if(strFLNME.length()>0)
				    	 {	 
				    	   getDATA(strFLNME);
				    	 }
					   	}
				      }
				      else
					  {  
					    tblISFWL.clrTABLE(); 
					  }
				 }
				 
			 }
			 catch(Exception x)
			 {
				 setMSG(x,"Mouse");
			 }
		}
  private class TBLINPVF extends TableInputVerifier
  {
	public boolean verify(int P_intROWID,int P_intCOLID)
	{
	   	int i;
	   	try
	   	{
	   	      
	   	 if(((JCheckBox)tblISFWL.cmpEDITR[TB1_CHKFL]).isSelected())
		 {
	   	    for(i=0;i<tblISFWL.getRowCount();i++)
	   		   if(tblISFWL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
	   		   {  
	            if(tblISFWL.getValueAt(i,TB1_ISWNM).toString().length()==0)
	   	        {	
	              setMSG("You can't add blank row from installed software list table to unwanted software list table ",'E');  
	   	          return false;
	   	        }
	   	       }
	   	 }
	   	  
	   	 if(((JCheckBox)tblUNISFW.cmpEDITR[TB3_CHKFL]).isSelected())
		 {
	   	   for(i=0;i<tblUNISFW.getRowCount();i++)
		   if(tblUNISFW.getValueAt(i,TB3_CHKFL).toString().equals("true"))
		   {  
		     if(tblUNISFW.getValueAt(i,TB3_ISWNM).toString().length()==0)
		     {	 
	    	  setMSG("You can't add blank row from unwanted software list table to installed software list table.",'E');
	    	  return false;
		     }
		   } 
		 }
	   	 
	   	  if(((JCheckBox)tblSFWFL.cmpEDITR[TB4_CHKFL]).isSelected())
		  {
	   	    for(i=0;i<tblSFWFL.getRowCount();i++)
	   	    {	
		   		if(tblSFWFL.getValueAt(i,TB4_CHKFL).toString().equals("true"))
		   		{
		   		  
		   		  if(i !=P_intROWID)	
		  	      {
		  	         tblSFWFL.setValueAt(new Boolean(false),P_intROWID,TB4_CHKFL);  
		  	      }	
		          if(tblSFWFL.getValueAt(i,TB4_FILENM).toString().length()==0)
		   	      {	
		              setMSG("You can't select blank row from File List table ",'E');  
		   	          return false;
		   	      }
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
	  
	  private void getDATA(String L_strFILNM)
		{        
			try  
			{	
				String strFILE = "";
				int intISDOT = L_strFILNM.indexOf(".");
				if(intISDOT != -1)
	            {
	              strFILE =	 L_strFILNM.toString().substring(0,intISDOT);
	            }
				tblISFWL.clrTABLE();
				inlTBLEDIT(tblISFWL);
				vtrSFWL1.clear();  
			    java.sql.Date L_datTEMP;
			    java.sql.Timestamp L_tmsTEMP;  
			    java.sql.Timestamp L_tmpTIME;
	            int L_ROWNO1=0;
			    int L_intCOUNT = 0;
				String M_strSQLQRY;
				ResultSet M_rstRSSET;
				String strUNISFW; 
				
							 	
			     /*Software List**/  
			    M_strSQLQRY = "select distinct is_iswnm,is_ipavl,is_pdnvl,is_cmpnm,is_sdnvl from sa_istrn  where is_cmpnm = '"+strFILE+"'";
			    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    //System.out.println("select"+M_strSQLQRY);
				        	
			            if(M_rstRSSET !=null)
				        {
				    		while(M_rstRSSET.next())
							{ 
				    			vtrSFWL1.add(M_rstRSSET.getString("is_iswnm").toString().trim());
				    			exeSORT(vtrSFWL1);
				    			lblIPADS.setText(M_rstRSSET.getString("is_cmpnm").toString().trim()+" = "+M_rstRSSET.getString("is_ipavl").toString().trim());
				    			lblDNSPRM.setText(M_rstRSSET.getString("is_cmpnm").toString().trim()+" = "+M_rstRSSET.getString("is_pdnvl").toString().trim());
				    			lblDNSSEC.setText(M_rstRSSET.getString("is_cmpnm").toString().trim()+" = "+M_rstRSSET.getString("is_sdnvl").toString().trim());
				    		}
				   		    M_rstRSSET.close();
				        }
			            for(int i=0;i<vtrSFWL1.size();i++)
		    			{	
		    		     if(!(vtrUNISFW.contains(vtrSFWL1.get(i))))
		    		     {
		    		    	 tblISFWL.setValueAt(nvlSTRVL(vtrSFWL1.get(i),""),L_ROWNO1,TB1_ISWNM);  
		    		    	 L_ROWNO1++;
		    		     }
		    			}  
			    }				   											
			     catch(Exception L_EX){
				System.out.println("getDATA "+L_EX);
			}
		}
	  private void exeADDREC(int P_intROWNO)
	  {   
	 	try
	 	{
	 		M_strSQLQRY =" insert into sa_uslst(us_swrnm)";
	 		M_strSQLQRY += " values (";
	 		M_strSQLQRY += "'"+tblISFWL.getValueAt(P_intROWNO,TB1_ISWNM).toString()+"')";
	 		//System.out.println("insert"+M_strSQLQRY);
	 		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	 		if(cl_dat.exeDBCMT("exeSAVE"))
	 		{	
	 		   setMSG("record updated successfully",'N');  
	 		} 
	 		
	 		   vtrSFWL1.remove(tblISFWL.getValueAt(P_intROWNO,TB1_ISWNM).toString());
	 		   vtrUNISFW.add(tblISFWL.getValueAt(P_intROWNO,TB1_ISWNM).toString());
		}
	 	catch(Exception L_EX)
	    {
	 	  setMSG(L_EX,"exeADDREC()"); 
	    }
	  }

	  private void exeDELREC(int P_intROWNO)
	  {
		   int L_ROWNO1=0;
		   int L_ROWNO2=0;
	 	try
	 	{
	 		M_strSQLQRY =" delete from sa_uslst where ";
	 		M_strSQLQRY +="us_swrnm='"+ tblUNISFW.getValueAt(P_intROWNO,TB3_ISWNM).toString()+"'"; 
	 		cl_dat.M_flgLCUPD_pbst = true;
	    	cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	    	//System.out.println("delete"+M_strSQLQRY);
	 		if(cl_dat.exeDBCMT("exeSAVE"))
	 		{	
	 		   setMSG("record deleted successfully",'N');
	 		}
	 		vtrSFWL1.add(tblUNISFW.getValueAt(P_intROWNO,TB3_ISWNM).toString());
	 		vtrUNISFW.remove(tblUNISFW.getValueAt(P_intROWNO,TB3_ISWNM).toString());	  
		}
	 	catch(Exception L_EX)
	    {
	 	  setMSG(L_EX,"exeDELREC()"); 
	    }
	  }
	   
	   private  void  exeSORT(Vector<String> LP_VTR)
	   {
		   try
		   {
			   for(int i=0; i<LP_VTR.size();i++)
			   {
				  for(int j=i+1;j<LP_VTR.size();j++)
				  {
					 if(LP_VTR.get(j).toString().compareTo(LP_VTR.get(i).toString())<1)
					 {
						 String L_strTEMP = LP_VTR.get(j).toString();
						 LP_VTR.set(j,LP_VTR.get(i));
						 LP_VTR.set(i,L_strTEMP);
					 } 	   
				  }
			   }   
		   }
		   catch(Exception L_E)
		   {
			 setMSG(L_E,"exeSORT");
		   }  
	   }
	   private void exeREFRESH()
	   {
		   try
		   {
			 tblSFWFL.clrTABLE();
		   }
		   catch(Exception L_E)
		   {
			   setMSG(L_E,"Refresh"); 
		   }
	   }
	   
	   private void  exeDELFILE()
		{
			try
			{
				String strFILENM = tblSFWFL.getValueAt(tblSFWFL.getSelectedRow(),TB4_FILENM).toString();
				pnlDELFIL = new JPanel();
				pnlDELFIL.setLayout(null);			 		
				pnlDELFIL.setSize(400,100);
				pnlDELFIL.setPreferredSize(new Dimension(400,100));
				add(new JLabel("Do you want to delete the file?\n"+strFILENM+"\n from folder sa_swtxt in g: drive"),1,1,1,15,pnlDELFIL,'L');
				int L_intOPTN = JOptionPane.showConfirmDialog(this,pnlDELFIL," File Deletion",JOptionPane.OK_CANCEL_OPTION);
			
				if(L_intOPTN==0)
				{
				  for(int P_intROWNO=0;P_intROWNO<tblSFWFL.getRowCount();P_intROWNO++)
				  {
					if(tblSFWFL.getValueAt(P_intROWNO,TB4_CHKFL).toString().equals("true"))
					{
				     if(strFILENM.length()>0)
				     {	  
				       exeDELETE(strFILENM);
				     }
				     else
				     {
				       setMSG("You can't select blank row",'E'); 
				     }
					} 
				}
			   }
			}	
			catch(Exception E)
			{
				setMSG("Delete file",'E');
			}	
								  
		}
	  
	  private void getDATA2()
	  {
		try
		{
			int L_ROWNO=0;
			vtrUNISFW.clear();
			tblUNISFW.clrTABLE();
			inlTBLEDIT(tblUNISFW);
		  	  M_strSQLQRY = "select distinct us_swrnm from sa_uslst" ;
			  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			  //System.out.println("select"+ M_strSQLQRY);
		   if(M_rstRSSET !=null)                           
		   {
			  
			 while(M_rstRSSET.next())
			 {	
			   vtrUNISFW.add(M_rstRSSET.getString("us_swrnm").toString().trim());
			   //System.out.println("vec"+vtrUNISFW);
			   exeSORT(vtrUNISFW);
			 }
			 M_rstRSSET.close();		
		   }
		   for(int j=0;j<vtrUNISFW.size();j++)
		   {	   
		     tblUNISFW.setValueAt(nvlSTRVL(vtrUNISFW.get(j).toString(),""),L_ROWNO,TB3_ISWNM);
		     L_ROWNO++;
		   }  
		  
		} 
		catch(Exception L_E)
		{
		  setMSG(L_E,"getData2");
		}	  
	  }

	  	private void  exeREAD()
	  	{
	  	   try
	  	   {	
	  	   
	  		int L_COUNT = 0;  
	  		String dirname = "g:\\sa_swtxt";
	  		File f1 = new File(dirname);
	  		if(f1.isDirectory())
	  		{
	  			//System.out.println("Directory of" +dirname);
	  			String s[] = f1.list();
	  			
	  			for(int i=0; i< s.length; i++)
	  			{
	  				File f = new File(dirname + "/" + s[i]);
	  				if(f.isDirectory())
	  				{
	  					//System.out.println(s[i]+"is a directory");
	  				}
	  				else
	  				{
	                                       
	  					int dotPos = s[i].lastIndexOf(".");
	  					String  extension = s[i].substring(dotPos);
	  					if(extension.equals(".txt"))//if txt then display content
	  					{
	  						//System.out.println(s[i]+"is a file");
	  						tblSFWFL.setValueAt(nvlSTRVL(s[i],""),L_COUNT,TB4_FILENM);
	  						L_COUNT++;
	  					}

	  				}
	                          
	  			}
	  		}
	  		else
	  		{
	  			//System.out.println(dirname + "is not a directory");
	  		}
	  	  }
	  	   catch(Exception L_E)
	  	   {
	  		 setMSG(L_E,"Read Directory"); 
	  	   }
	  }
	  	
	  	private void exeDELETE(String strFILNAME)
	  	{
	  	  try
	  	  {
	  		
	  		String[] files;
	  	    String fileName = "g:\\sa_swtxt";
	      // A File object to represent the filename
	      File f = new File(fileName);

	      // Make sure the file or directory exists and isn't write protected
	      if (!f.exists())
	    	  setMSG("Delete: no such file or directory: " + fileName,'E');

	      if (!f.canWrite())
	    	  setMSG("Delete: write protected: "+ fileName,'E');

	      // If it is a directory, make sure it is empty
	      if (f.isDirectory()) {
	         files = f.list();
	        if (files.length > 0)
	          System.out.println("Delete: directory not empty: " + fileName);
	              
	      for(int i=0; i<files.length;i++)
	      { 
	    	 
	        if(files[i].equals(strFILNAME))
	        {
	        	
	          File fds = new File("g:\\sa_swtxt\\"+files[i]);
	        
	         // Attempt to delete it
	          boolean success = fds.delete();
	          
	          if (!success)
	        	  setMSG("Delete: deletion failed",'E');
	        } 
	      }
	  	   }	
	  	  }
	  	   catch(Exception L_E)
	  	   {
	  		 setMSG(L_E,"Delete File"); 
	  	   }	  
	   }
	  
	  /** Initializing table editing before poulating/capturing data
	   */
	  private void inlTBLEDIT(JTable P_tblTBLNM)
	  {
	  	if(!P_tblTBLNM.isEditing())
	  		return;
	  	P_tblTBLNM.getCellEditor().stopCellEditing();
	  	P_tblTBLNM.setRowSelectionInterval(0,0);
	  	P_tblTBLNM.setColumnSelectionInterval(0,0);		
	  }	    
}

    
	