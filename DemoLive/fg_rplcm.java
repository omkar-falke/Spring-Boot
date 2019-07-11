/*
System Name   : Finished Goods Inventory Management System
Program Name  : Location Master Report
Program Desc. : Report for taking the Location Master Details
				
Author        : Mr. S.C.SANGOLLI
Date          : 28th May 2001
Version       : FIMS 1.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.lang.*;
import java.util.Properties;
import java.util.Date; 
import java.io.*; 
/**
 * main  
 */
public class fg_rplcm extends cl_rbase
{
	JTextField txtWRHTP,txtRPTDT,txtMNLCD,txtALLQT,txtREMDS,txtMAXC,txtUSEC,txtFREC;
	JButton btnRUN,btnCLEAR;
	JRadioButton rdbMNLSP,rdbMNLAL,rdbQTYSP,rdbQTYAL,rdbLOCRP,rdbSTKRP;
	ButtonGroup repGRP;
	private String strFILNM;
	int intTB_CHKFL = 0;/**adding the element into the  1st column */
	int intTB_MAXQT = 2;/**adding the element into the  2nd column*/
    int intTB_STKQT = 4;/**adding the element into the  3rd column*/
	int intTB_AVLQT = 3;/**adding the element into the  4th column*/
	int intTB_MNLCD = 1;
	
	
	cl_JTBL tblSKDTL;
	String strWRHTP,strRPTDT,strMARGN="  ",strODSTR,strSBLOC;
	String strMNLOC,strLCSTR,strQTSTR,strPRVMNL=" ",strREMDS;
	String strMAXQT,strSTKQT,strAVLQT;
	boolean LM_ERRFL = false;
	//String LM_RESFIN = cl_dat.ocl_dat.M_REPSTR;
      //  String LM_RESSTR = LM_RESFIN.concat("\\fg_rplcm.doc"); 
	FileOutputStream fosREPORT;
    DataOutputStream dosREPORT;
	int intLMRGN=0;
	/**
	 *1.Screen Designing & Inser the table or create the table into the panel 
	 *2.Alongwith their descriptions.
	 */
	   fg_rplcm()
	   {
		super(2);
				
			setMatrix(20,8);
			
			
			
		
			add(new JLabel("Report Date: "),3,1,1,1,this, 'L');
			add(txtRPTDT=new JTextField(),3,2,1,1,this,'L');
			
			
			add(new JLabel("Ware House Type :"),3,4,1,1.5,this,'L');
			add(txtWRHTP=new JTextField(),3,6,1,1,this,'R');
			
		
			add( new JLabel("Main Location"),5,1,1,1,this,'L');
		    add(txtMNLCD=new TxtLimit(1),5,2,1,1,this,'L');
			
			
			add(new JLabel("Available Quantity:"),5,4,1,1.5,this,'L');
			add(txtALLQT=new JTextField(),5,6,1,1,this,'L');
			
			add(new JLabel("Report On"),7,1,1,1,this,'L');
			
			
			
			repGRP=new ButtonGroup();
			
			add(rdbLOCRP = new JRadioButton("Location",true),7,2,1,1,this,'L');
			add(rdbSTKRP = new JRadioButton("Free Stock Available",false),7,3,1,1.5,this,'L');
			repGRP.add(rdbLOCRP);
			repGRP.add(rdbSTKRP);
			
			add(new JLabel("Remarks: "),9,1,1,1,this,'L');
			add(txtREMDS=new JTextField(),9,2,1,4,this,'L');
			
			add(btnRUN=new JButton("RUN"),9,6,1,0.8,this,'L');
			
			String[] L_staSTKHD = {"Status ","Sub Location","Max.Capacity","Used Capacity","Free Capacity"};/**This is for create the coloums */
       	    int[] L_intCOLSZ = {60,120,100,100,115};
		   
		    tblSKDTL = crtTBLPNL(this,L_staSTKHD,5000, 10,2,7.5,5.4 ,L_intCOLSZ,new int[]{0});
			
			add(new JLabel("Total:"),18,3,1,1,this,'L');
			add(txtMAXC=new JTextField(),18,4,1,0.8,this,'L');
			add(txtUSEC=new JTextField(),18,5,1,0.8,this,'L');
			add(txtFREC=new JTextField(),18,6,1,0.8,this,'L');
			add(btnCLEAR=new JButton("CLEAR"),18,1,1,1,this,'L');
			
			M_pnlRPFMT.setVisible(true);
			
	}
	void setENBL(boolean L_flgSTAT)
	{
         super.setENBL(L_flgSTAT);
		 txtRPTDT.setText(cl_dat.M_strLOGDT_pbst);
		 
		 txtRPTDT.requestFocus();
		 
	}
	
	void removeENBL(boolean L_flgSTAT)
     {
        
           M_txtFMDAT.setVisible(false);
	       M_txtTODAT.setVisible(false);
	       M_lblFMDAT.setVisible(false);
		   M_lblTODAT.setVisible(false);		
		    
	 }
	/** This Action Performed event on the Run Button 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) /** Combo Opotion Remove the From Date & To Date 
		                                         */
		removeENBL(false);
		
/** Press Run Button Retrive the data Into DataBase
 */
		if(M_objSOURC == btnRUN)
		{
			strMAXQT="";
			strSTKQT="";
			strAVLQT="";
			System.out.println("This is action Performed");
				try
				{
					
					System.out.println ("This is Button run");
					setMSG("Data Fetching in Progress... ",'N');
					
					
					tblSKDTL.clrTABLE();
					getALLREC(tblSKDTL);
					
					txtMAXC.setText(setNumberFormat(Double.parseDouble(strMAXQT),3));
					txtUSEC.setText(setNumberFormat(Double.parseDouble(strSTKQT),3));
					txtFREC.setText(setNumberFormat(Double.parseDouble(strAVLQT),3));
				}catch(Exception L_EX)
				{
					setMSG(L_EX," This is btn Run");
				}	
		}       
		if(M_objSOURC == btnCLEAR)
		{
			txtWRHTP.setText("");
			txtMNLCD.setText("");
			txtALLQT.setText("");
			txtREMDS.setText("");
			txtMAXC.setText("");
			txtUSEC.setText("");
			txtFREC.setText("");
			clrDTLTBL();
	   }
	}
	/** clear the Lot of Table
	 */
	private void clrDTLTBL()
	{  
	  for(int i = 0;i<tblSKDTL.getRowCount();i++)
	  {
			for(int j = 0;j<tblSKDTL.getColumnCount();j++)
			{
				tblSKDTL.setValueAt(" ",i,j);
			}
	  }
	}
	
	/** This KeyPressed used for "ENTER" event on the TextFields
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			
			if(L_KE.getKeyCode()==L_KE.VK_ENTER)
			{
				if(M_objSOURC== txtRPTDT)
				{
					txtWRHTP.requestFocus();
					
				}	
				if(M_objSOURC == txtWRHTP)
				{
					if(!(txtWRHTP.getText().trim().length()>2)&& !(txtWRHTP.getText().trim().length()==0))
					{
						if(vldWRHTP())
						{
							
							txtMNLCD.requestFocus();
						}
						else
						{
							setMSG("InValid Ware House Type",'E');
							txtWRHTP.requestFocus();
						}	
       				}
					else
					{
						setMSG("Fields Blank / Wrong Data",'E');
					    txtWRHTP.requestFocus();
					}	
				}	
				if(M_objSOURC == txtMNLCD)
				{
					if(!(txtMNLCD.getText().trim().length()>5)&& !(txtMNLCD.getText().trim().length()==0))
					{
						if(vldMNLCD())
						{
							txtALLQT.requestFocus();
						}
						else
						{
							setMSG("Invalid Main Location",'E');
						    txtMNLCD.requestFocus();
						}	
					}
					else
					{
						setMSG("Fields Blank / Wrong Data",'E');
					    txtMNLCD.requestFocus();
					}	
						 
				}	     
				if(M_objSOURC ==txtALLQT)
				{
					if(!(txtWRHTP.getText().trim().length()==0) && !(txtMNLCD.getText().trim().length()==0))
					{
						setMSG(" ",'N');
						rdbLOCRP.requestFocus();
					}
					else
					{
						setMSG("Fields Blank / Wrong Data",'E');
						txtALLQT.requestFocus();
					}	
				}
			
			
		}	
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"This is Key Pressd ");
		}
		
/** This is used for the Press the 'F1' , the Depend upon the Query appear the Help Screen
 */
		
		if (L_KE.getKeyCode() == L_KE.VK_F1)
		{
		     if(M_objSOURC == txtWRHTP)
			{
				try
				{
					M_strSQLQRY="Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXPTD'";
					M_strHLPFLD="txtWRHTP";
					cl_dat.M_flgHELPFL_pbst=true;
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Ware House Type "},1,"CT");
			 		
					
				}catch(Exception L_EX)
				{
					setMSG(L_EX,"F1 HELP ");
				}	
			 }
			 if(M_objSOURC == txtMNLCD)
			 {
			  if(!(txtWRHTP.getText().trim().length()==0))
			  {		 
				 try
				 {
					 M_strSQLQRY="Select distinct(substring(LC_MNLCD,1,1)) from FG_LCMST where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LC_WRHTP='"+txtWRHTP.getText().trim()	+"'";
					 System.out.println(M_strSQLQRY);
					 cl_dat.M_flgHELPFL_pbst=true;
					 M_strHLPFLD= "txtMNLCD";
					 cl_hlp(M_strSQLQRY,1,1,new String[]{"Main Location "},1,"CT");
				 }catch(Exception L_EX)
				 {
					 setMSG(L_EX,"F1 HELP");
				 }
			  }	 
			 }	 
				 
		}	
				
	}
	
	
	   void exeHLPOK()
		{
		   
		   super.exeHLPOK();
		   cl_dat.M_flgHELPFL_pbst = true;
		   if(M_strHLPFLD == "txtWRHTP")
		   {
			   txtWRHTP.setText(cl_dat.M_strHLPSTR_pbst);
			   txtMNLCD.requestFocus();
		   }  
		   if(M_strHLPFLD == "txtMNLCD")
		   {
			   txtMNLCD.setText(cl_dat.M_strHLPSTR_pbst);
			   txtALLQT.requestFocus();
		   }
		}



	/**
 * Validation Method for Main Location  inputted by user
 */
	
	private boolean vldMNLCD(){
		try{
			M_strSQLQRY = "Select * from FG_LCMST where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND substring(LC_MNLCD,1,1)='"+txtMNLCD.getText().toString().trim().toUpperCase()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldMNLCD ....."+L_EX);							   
		}
		return false;
	}
	
	
/**
 * Validation Method for Ware House inputted by user
 */
	private boolean vldWRHTP()
	{
		try
		{
			strWRHTP = txtWRHTP.getText().toString().trim();
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXPTD' and CMT_CODCD='"+strWRHTP+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldWRHTP ....."+L_EX);							   
		}
		return false;
	}	
	
	
		/**
 * Method to get All Records from DataBase and Display the Table 
 */
	private void getALLREC(JTable tblSKDTL)
	{
		
		//System.out.println("This is get All records");
		
		String L_AVLQT = "";  //Available Qty.
		strLCSTR = "";
		strQTSTR = "";
		strODSTR = "";

		strLCSTR = "";
		strQTSTR = "";
		strODSTR = "";
		strWRHTP = txtWRHTP.getText().trim();
		strREMDS = txtREMDS.getText().trim();
		getQRSTR();	
		
	
	}
	
	/** This method is used for fetching the Data from DataBase and display the Table Structure
	 */
	private void getQRSTR()
	{
		try
        {    

			strODSTR = "";
			/*if(rdbSTKRP.isSelected())
				strODSTR = " order by L_AVLQT"; 
			else if(rdbLOCRP.isSelected())
				strODSTR = " order by LC_MNLCD"; */
			 M_strSQLQRY = "Select LC_MNLCD,LC_MAXQT,LC_STKQT,(LC_MAXQT-LC_STKQT)L_AVLQT from FG_LCMST";
			 M_strSQLQRY += " where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LC_WRHTP='"+strWRHTP+"'";
			if((txtMNLCD.getText().toString().trim().length()>0))
				M_strSQLQRY += " and substring(LC_MNLCD,1,1) = '"+txtMNLCD.getText().toString().trim().toUpperCase()+"'";
			if(txtALLQT.getText().trim().length()>0)
				M_strSQLQRY +=" and (LC_MAXQT-LC_STKQT) >= "+txtALLQT.getText().trim()+"";
			if(rdbSTKRP.isSelected())
				//M_strSQLQRY +=" order by LC_MNLCD,L_AVLQT "; 
				M_strSQLQRY +=" order by L_AVLQT "; 
			else if(rdbLOCRP.isSelected())
				M_strSQLQRY +=" order by LC_MNLCD"; 
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			 int j=0;
			 while(M_rstRSSET.next())
			 {
			     strMNLOC = M_rstRSSET.getString("LC_MNLCD");
				 if(strMNLOC != null)
				strSBLOC = strMNLOC.substring(0,1);
				//System.out.println(strSBLOC);
				if(!(strPRVMNL.equals(strSBLOC)))
				{
					calSUMQTY();
					
				}
				 tblSKDTL.setValueAt(strMNLOC,j,intTB_MNLCD);	 
				 tblSKDTL.setValueAt(M_rstRSSET.getString("LC_MAXQT"),j,intTB_MAXQT);
				 tblSKDTL.setValueAt(M_rstRSSET.getString("LC_STKQT"),j,intTB_AVLQT);
				 tblSKDTL.setValueAt(M_rstRSSET.getString("L_AVLQT"),j,intTB_STKQT);;
			 j++;	 
			 }
			 
		}catch(Exception L_EX)
		{}
	} 
	
		/**
 * Method to format the Report Header 
 */
	private void prnHEADER()
	{
		String strRPTDT = txtRPTDT.getText().trim();
		String LM_ALLQT = txtALLQT.getText().trim();
		String LM_MNLCD = txtMNLCD.getText().toString().trim().toUpperCase();
		try{
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			dosREPORT.writeBytes(padSTRING('R'," ",20));//margin
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
			dosREPORT.writeBytes(padSTRING('L',"Report Date :"+cl_dat.M_strLOGDT_pbst,40));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R'," ",20));//margin
			
				if(rdbSTKRP.isSelected())
					dosREPORT.writeBytes(padSTRING('R',"Free Stock Available Report Dt."+strRPTDT+" where Loc. Free >="+txtALLQT.getText().trim()+" , "+LM_MNLCD+" Loc.*/",50));
				
				if(rdbLOCRP.isSelected())
					dosREPORT.writeBytes(padSTRING('R',"Location Detail Report for "+strRPTDT+" , "+LM_MNLCD+" Loc.",50));
				
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(padSTRING('L',"Page No:" + cl_dat.M_PAGENO,40));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			dosREPORT.writeBytes(padSTRING('R'," ",20));//margin
			crtLINE(100);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			dosREPORT.writeBytes(padSTRING('R'," ",20));//margin
			dosREPORT.writeBytes(padSTRING('R'," ",15));
			dosREPORT.writeBytes(padSTRING('R',"Sub Location",15));
			dosREPORT.writeBytes(padSTRING('R',strMARGN,1));
			dosREPORT.writeBytes(padSTRING('R',"Max. Capacity",15));
			dosREPORT.writeBytes(padSTRING('R',strMARGN,1));
			dosREPORT.writeBytes(padSTRING('R',"Used Capacity",15));
			dosREPORT.writeBytes(padSTRING('R',strMARGN,1));
			dosREPORT.writeBytes(padSTRING('L',"Free Capacity",15));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			dosREPORT.writeBytes(padSTRING('R'," ",20));//margin
			crtLINE(100);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
		}catch(Exception L_EX){
			System.out .println ("L_EX..prnhead....... :"+L_EX);
		}
	}
	/**
 * Method to create Lines used in Reports
 */
	private void crtLINE(int LM_CNT){
		String strln = "";
		try{
		for(int i=1;i<=LM_CNT;i++){
		 strln += "-";
		}
		dosREPORT.writeBytes(padSTRING('L'," ",intLMRGN));
		dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX){
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}
	
/** This is used for to print the report on the specified Location " Word Format & HTML Format printer & Fax "
 */	
	void exePRINT()
	{   
	    
			try
			{
	        int RECCT  = 0 ;
	   		    
   		    setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
   		   if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_qrlrs.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_qrlrs.doc";	
							
			getDATA();
			
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
    			    p  = r.exec("C:\\windows\\wordpad.exe " + strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Details"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
			
    			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}	
	}
	/** Method to fetch data from tblSTKDTL table & club it with Header in Data Output Stream  */
	  private void getDATA()
	 {
		try
		{   
			cl_dat.M_PAGENO = 0;
		    String strln="";
		    //System.out.println("sss");
		    //System.out.println("th file name is " +strFILNM);
		    fosREPORT = new FileOutputStream(strFILNM);
		    System.out.println("th file name is " +strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    System.out.println("th file name is " +strFILNM);
		    
		    int RECCT  = 0 ;
	        cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
   		    if(M_rdbHTML.isSelected())
		    {
		        
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
			 }
			 else
				prnFMTCHR(dosREPORT,M_strCPI17);
		    
   		      		    
		   	 prnHEADER();/****gets the header of the report*/
			 
				String  L_AVLQT = "";  //Available Qty.
				 strLCSTR = "";
				 strQTSTR = "";
				 strODSTR = "";
				String strWRHTP = txtWRHTP.getText().trim();
				String LM_MNLCD = txtMNLCD.getText().toString().trim().toUpperCase();
				String LM_ALLQT = txtALLQT.getText().trim();
				String strREMDS = txtREMDS.getText().trim();

			 if(rdbSTKRP.isSelected())
			strODSTR = " order by LC_MNLCD,L_AVLQT desc"; 
			else if(rdbLOCRP.isSelected())
				strODSTR = " order by LC_MNLCD"; 
			M_strSQLQRY = "Select LC_MNLCD,LC_MAXQT,LC_STKQT,(LC_MAXQT-LC_STKQT) L_AVLQT from FG_LCMST";
			M_strSQLQRY += " where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LC_WRHTP='"+strWRHTP+"'";
			 
			if((txtMNLCD.getText().toString().trim().length()>0))
				M_strSQLQRY += " and substring(LC_MNLCD,1,1) = '"+txtMNLCD.getText().toString().trim().toUpperCase()+"'";
			if(txtALLQT.getText().trim().length()>0)
				M_strSQLQRY +=" and (LC_MAXQT-LC_STKQT) >= "+txtALLQT.getText().trim()+"";
			if(rdbSTKRP.isSelected())
				//M_strSQLQRY +=" order by LC_MNLCD,L_AVLQT desc"; 
				M_strSQLQRY +=" order by L_AVLQT"; 
			else if(rdbLOCRP.isSelected())
				M_strSQLQRY +=" order by LC_MNLCD"; 
			
         	//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		
			int i=0;
			while (M_rstRSSET.next())
            {
				
				if(cl_dat.M_intLINNO_pbst > 65)
				{
					dosREPORT.writeBytes("\n");
					crtLINE(122);
					prnFMTCHR(dosREPORT,M_strEJT);
					prnHEADER();
				}
				
				
				strMNLOC = M_rstRSSET.getString("LC_MNLCD");
				
				if(strMNLOC != null)
				strSBLOC = strMNLOC.substring(0,1);
				
				if(!(strPRVMNL.equals(strSBLOC)))
				{
				calSUMQTY();
				
				dosREPORT.writeBytes (padSTRING('R'," ",20));
				
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes (padSTRING('R',strSBLOC + " Location",25));
				
				
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strMAXQT),3),20));
				//System.out.println(setNumberFormat(Double.parseDouble(strMAXQT),3));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strSTKQT),3),17));
				//System.out.println(setNumberFormat(Double.parseDouble(strSTKQT),3));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strAVLQT),3),19));
				//System.out.println(setNumberFormat(Double.parseDouble(strAVLQT),3));
				
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst += 1;
				strPRVMNL = strSBLOC;
				}
				 strMAXQT = M_rstRSSET.getString("LC_MAXQT");
				 if(strMAXQT == null)
				 strMAXQT ="0.000"; 
				 strSTKQT = M_rstRSSET.getString("LC_STKQT");
				 if(strSTKQT == null)
				 strSTKQT = "0.000";
				 strAVLQT = M_rstRSSET.getString("L_AVLQT");
				 i++;
		        outBDPRN();
             
			 
			 
			}
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			dosREPORT.writeBytes(padSTRING('R'," ",20));//margin
			crtLINE(100);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			dosREPORT.writeBytes(padSTRING('R'," ",20));//margin
			dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS,90));
			prnFMTCHR(dosREPORT,M_strEJT);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			dosREPORT.close();
			fosREPORT.close();
         }catch(Exception L_EX)
          {
               setMSG(L_EX," This is getData");
          } 
}


		private void calSUMQTY(){
		try{
			
			ResultSet M_rstRSSET1;
			M_strSQLQRY = "Select sum(LC_MAXQT),sum(LC_STKQT),sum(LC_MAXQT-LC_STKQT) from FG_LCMST";
			M_strSQLQRY += " where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LC_WRHTP='"+ txtWRHTP.getText().trim()+"' and substring(LC_MNLCD,1,1)='"+strSBLOC+"'" ;
			if((txtMNLCD.getText().toString().trim().length()>0))
			M_strSQLQRY += " and substring(LC_MNLCD,1,1) = '"+txtMNLCD.getText().toString().trim().toUpperCase()+"'";
			if(txtALLQT.getText().trim().length()>0)
			M_strSQLQRY +=" and (LC_MAXQT-LC_STKQT) >= "+txtALLQT.getText().trim()+"";
			//System.out.println("LM_STRSQL...."+M_strSQLQRY);
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i=0;
			while (M_rstRSSET1.next())
			{
				
				strMAXQT = M_rstRSSET1.getString(1);
				//tblSKDTL.setValueAt(strMAXQT,0,intTB_MAXQT);
				
				strSTKQT = M_rstRSSET1.getString(2);
				
				
				strAVLQT = M_rstRSSET1.getString(3);
				
				
			i++;	
			}
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"this is callSUMQTY");
		}
	}
	/**
 * Method to format the Body of the Report 
 */
	private void outBDPRN(){
		//float L_STRFRE=Float.parseFloat(strMAXQT)-Float.parseFloat(strSTKQT);
		try{
			dosREPORT.writeBytes(padSTRING('R'," ",35));//margin
			dosREPORT.writeBytes(padSTRING('R',strMNLOC,10));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strMAXQT),3),20));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strSTKQT),3),17));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strAVLQT),3),19));
			dosREPORT.writeBytes ("\n");
			cl_dat.M_intLINNO_pbst +=1;
		}
		catch(Exception L_EX){
			System.out .println ("L_EX.....outBDPRN "+L_EX);
		}
	}
	
	
	
}//main class