/**
System Name   : Finished Goods Inventory Management System
Program Name  : Lotwise Residence Details
Program Desc. : 1)Gives detail regarding Lot Nos. residence within the WareHouse.
Author        : Mr. Deepal N. Mehta
Date          : 22nd July 2001
Version       : FIMS 1.0

Modificaitons 

Modified By    :SATISH REDDY
Modified Date  :28-02-2007
Modified det.  :
Version        :

*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Vector;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;

public class fg_qrlrs extends cl_rbase
{
	private JTextField txtFMDAT,txtTODAT,txtGRDFR,txtGRDTO,txtLOTFR,txtLOTTO,txtPERIOD;
	private TxtNumLimit txtTOTSQ; /**TxtNumLimit field was totalStock Quantity*/
	private JOptionPane LM_OPTNPN;
	private JLabel lblTOTSQ; 
	private JButton btnRUN,btnCLEAR; /** Button component was RUN & CLEAR */
	
	
	FileOutputStream fosREPORT;/** File OutputStream Object for file handling.*/
	
    DataOutputStream dosREPORT; /** Data OutputStream for generating Report File.*/	
    cl_JTBL tblSTKDTL;
	int TB_CHKFL = 0;/**adding the element into the  1st column */
	int TB_GRADE = 1;/**adding the element into the  2nd column*/
    int TB_LOTNO = 2;/**adding the element into the  3rd column*/
	int TB_BAGDT = 3;/**adding the element into the  4th column*/
    int TB_RESVL = 4;/**adding the element into the  5th column*/
	int TB_STKQT = 5;/**adding the element into the  6th column*/
	int intRECCT;
	int LM_LMRGN=0;
	//Object[][] L_DATA; 
	ResultSet M_rstRSSET;
	String strGRDFR,strGRDTO,strLOTFR,strLOTTO,strPERIOD,L_strUCLQT,strRESVL;
	String L_strHLPFLD,strSQLQRY,strPRDDS,strBAGDT,strPRDTP;
	
	String strGRDSTR = "";
	String strLOTSTR = "";
	
	String strGRADE="";
	String strLOTNO="";
	String strRCLNO="";
	String strADDSTR="";
	String strPRVPDS = "";
	String strTOTPRQT= "0";
	private String strFILNM;    /* taken the file fg_qrlrs.doc or fg_qrlrs.html */
	int L_intCNT = 0;
	
	double dblDIFQT = 0;
	double dblSTKQT = 0;
	double dblTOTQT=0;
	double dblTOTPRQT=0;
	
	boolean LM_FRMFL,LM_TOFLG;
	/**
	 *1.Screen Designing & Inser the table or create the table into the panel 
	 *2.Alongwith their descriptions.
	 */
	fg_qrlrs()
	{
	    super(2);
        LM_OPTNPN = new JOptionPane();
		
		try
		{
    		setMatrix(20,8);/* insert the 20 rows & 20 columns on the panel */
    		add(new JLabel("Starting From:"),2,3,1,1,this ,'L'); /** this Label for starting From                                               */
    		add(new JLabel("Ending to:"),2,4,1,1,this,'L');/** this label for Ending TO   */
    		add(new JLabel("Grade :"),3,2,1,1,this,'L');
    		add(new JLabel("Lot.No:"),4,2,1,1,this,'L');
    		add(new JLabel("Period in Days:"),5,2,1,1,this,'L');
    		add(txtGRDFR=new JTextField(),3,3,1,1,this,'L');
    		add(txtGRDTO=new JTextField(),3,4,1,1,this,'L');
    		add(txtLOTFR=new JTextField(),4,3,1,1,this,'L');
    		add(txtLOTTO=new JTextField(),4,4,1,1,this,'L');
    		add(txtPERIOD=new JTextField(),5,3,1,1,this,'L');
    		
    		
    		
    		
    		
    		add(btnRUN=new JButton("RUN"),5,7,1,1,this,'L');
    		add(btnCLEAR=new JButton("CLEAR"),17,1,1,1,this,'R');
    		add(lblTOTSQ=new JLabel("Total:"),16,6,1,1,this,'L');
    		add(txtTOTSQ=new TxtNumLimit(10.3),16,7,1,0.75,this,'L');
    		txtPERIOD.setText("0");
	       	String[] L_staSTKHD = {"Status","Grade","Lot No.","Bagging Date","Residence Period","Stock Qty."};/**This is for create the coloums */
       	    int[] L_intCOLSZ = {60,150,100,100,105,137};
		    //M_intCNT=getDTLRW();
		    tblSTKDTL = crtTBLPNL(this,L_staSTKHD,5000,7 ,1,8,7 ,L_intCOLSZ,new int[]{0});
    	    
    		M_pnlRPFMT.setVisible(true);
    		
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"Gui Designing");
		}
		
	}
	
	 	 
/** Remove the FromDate and ToDate Text field and Labels(FromDate,ToDate) and removeENBL it takes Boolean argument  */
     void removeENBL(boolean L_flgSTAT)
     {
        
           M_txtFMDAT.setVisible(false);
	     	M_txtTODAT.setVisible(false);
	    	M_lblFMDAT.setVisible(false);
		    M_lblTODAT.setVisible(false);		
		    
	   }	
	
/** we perform the key event 'F1' to text Fields*/
	
	   public void keyPressed(KeyEvent L_KE)
	        {
	        	
		         super.keyPressed(L_KE);
		        if (L_KE.getKeyCode() ==L_KE.VK_F1)
		        {
                     /*LM_FRMFL = false;
			         LM_TOFLG = false;
			         M_strADDSTR = "";	 */
		    
		             if (M_objSOURC == txtGRDFR )
			         {
			            try
			            {
                    	/*LM_FRMFL = true;
				        LM_TOFLG = false;		     */
    			        
    			       M_strSQLQRY = "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where PR_PRDCD=ST_PRDCD";
    		           M_strSQLQRY += strADDSTR + strLOTSTR;
    		           M_strHLPFLD = "txtGRDFR";
    			       cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade From.."},1,"CT");
    			       }catch(Exception L_EX)
    			       {
    			        setMSG(L_EX ," F1 help..");    		    
    			       }
		            }
		            if(M_objSOURC == txtGRDTO)
			        {
			          //System.out.println("txtGRDttto");
			          try
			          {  
			             LM_TOFLG = true;
						 LM_FRMFL = false;
						 strADDSTR = " and PR_PRDDS > '"+strGRDFR+"'";
    					 M_strSQLQRY = "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where PR_PRDCD=ST_PRDCD";
    					 M_strSQLQRY += strADDSTR + strLOTSTR;
    					 M_strHLPFLD="txtGRDTO";
    					 cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade To"},1,"CT");
    					
    			    } catch(Exception L_EX)
			        {
			            setMSG(L_EX, "F1 Help");
			        }
			       }
       			 if(M_objSOURC == txtLOTFR)       
    			 {
              		  try
    		     	  {
        			    
        				
        			    LM_FRMFL = true;
						LM_TOFLG = false;
        			    M_strSQLQRY = "Select distinct ST_LOTNO from FG_STMST,CO_PRMST where ST_PRDCD=PR_PRDCD";
        		        M_strSQLQRY += strADDSTR + strGRDFR;
        	//		    System.out.println(M_strSQLQRY);
        			    M_strHLPFLD="txtLOTFR";
        			  
        			    cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No"},1,"CT");
        			   
        			  } catch(Exception L_EX)
    			         {
    			            setMSG(L_EX, "F1 Help");
    			         }
    			 }
    			 
    			 if(M_objSOURC == txtLOTTO)       
    			 {
    			     try
    			     {
    			       
    			       LM_TOFLG = true;
					   LM_FRMFL = false;
					   strADDSTR = " and ST_LOTNO > '"+strLOTFR+"'";
        			   M_strSQLQRY = "Select distinct ST_LOTNO from FG_STMST,CO_PRMST where ST_PRDCD=PR_PRDCD";
        		       M_strSQLQRY += strADDSTR + strGRDFR;
        			   
        			   M_strHLPFLD="txtLOTTO";
        			   cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No"},1,"CT");
        			   
				     }
    			    catch(Exception L_EX)
    			    {
    			    setMSG(L_EX, "F1 Help");
    			    }
    			    
    			 }
    			    
		}//Kekcode	 
		
/**  
  We perform the event on "ENTER" on the text Field*/ 
		if (L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
		    
		    if (M_objSOURC==txtGRDFR)
		    {
				
				strGRDFR = txtGRDFR.getText().toString().trim();
				strGRADE = strGRDFR;
				if(vldGRADE()){
					setMSG("Valid Grade",'N');
					txtGRDTO.setText(txtGRDFR.getText().toString().trim());
					txtGRDTO.requestFocus();
				}else{
					setMSG("InValid Grade Please press F1",'E');
				}
			}
			else if (M_objSOURC== txtGRDTO)
			{
				strGRDTO = txtGRDTO.getText().toString().trim();
				strGRADE = strGRDTO;
				if(vldGRADE()){
					setMSG("Valid Grade ",'N');
					txtLOTFR.requestFocus();
				}else{
					setMSG("InValid Grade Please press F1 ",'E');
				}
			}
			else if (M_objSOURC == txtLOTFR){
				strLOTFR = txtLOTFR.getText().toString().trim();
				strLOTNO = strLOTFR;
				if(vldLOTNO()){
					setMSG("Valid LOTNO",'N');
					txtLOTTO.setText(txtLOTFR.getText().toString().trim());
					txtLOTTO.requestFocus();
				}else{
					setMSG("InValid LOTNO  & Please press F1",'E');
				}
			}
			else if (M_objSOURC== txtLOTTO)
			{
				strLOTTO = txtLOTTO.getText().toString().trim();
				strLOTNO = strLOTTO;
				if(vldLOTNO()){
					setMSG("Valid LOTNO",'N');
					txtPERIOD.requestFocus();
				}else{
					setMSG("InValid LOTNO & Please press F1",'E');
				}
			}
			else if (M_objSOURC==txtPERIOD){
				vldPERIOD();
			}
		
		    
		    
		}
		
			        
	        } //keyaction
	        
	  /** we press F1 key on TextField  appear the HELPSCREEN,  select the any field and Press Ok set the value into TextField */      
	 void exeHLPOK()
     {
        cl_dat.M_flgHELPFL_pbst = true;
        super.exeHLPOK();
    	if(M_strHLPFLD == "txtGRDFR")
    	{			
    		txtGRDFR.setText(cl_dat.M_strHLPSTR_pbst);
    		//txtGRDTO.requestFocus();				
    		
    	}
    	
    	 if(M_strHLPFLD=="txtGRDTO")
    	{
    	    txtGRDTO.setText(cl_dat.M_strHLPSTR_pbst);
    		txtLOTFR.requestFocus();
    	}
    	 if(M_strHLPFLD=="txtLOTFR")
    	{
    	    txtLOTFR.setText(cl_dat.M_strHLPSTR_pbst);
    		
    	}
    	 if(M_strHLPFLD== "txtLOTTO")
    	{
    	    txtLOTTO.setText(cl_dat.M_strHLPSTR_pbst);
    		txtPERIOD.requestFocus();
    	}
    	 if(M_strHLPFLD== "txtPERIOD")
    	 {
    	      btnRUN.requestFocus();
    	  }    
    	
    	
    }
	 
    /** Perform the actionperformed the RUN & CLEAR */
  public void actionPerformed(ActionEvent L_AE)
    {
    	super.actionPerformed(L_AE);
	try
	{
	    
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) /** Combo Opotion Remove the From Date & To Date 
		                                         */
			removeENBL(false);
			
	    if(M_objSOURC == btnRUN)/** Click run button fetch the records from database and display the table */
	    {
	    	setMSG("Data Fetching in Progress... ",'N');
			
			
	        getQRSTR();
	        tblSTKDTL.clrTABLE();
			getDTLREC(tblSTKDTL);     
			
		//	System.out.println(setNumberFormat(dblTOTPRQT,3));
			txtTOTSQ.setText(setNumberFormat(dblTOTQT,3));
    	  
		}	    
	    if(M_objSOURC==btnCLEAR)/**clear the table and textfield & Table */
	    {
	        txtGRDFR.setText("");
			txtGRDTO.setText("");
			txtLOTFR.setText("");
			txtLOTTO.setText("");
			txtPERIOD.setText("");
			txtTOTSQ.setText("");
			clrDTLTBL();
	    }
	}catch(Exception L_EX)
	{
	    setMSG(L_EX,"the Run Performed");
	}
}	
	/**
          we check the Vald garde & Vald LOT no.. & Vld PERIOD.*/
	 private boolean vldGRADE(){/** valid garde the retun value is boolean*/
		try{
			M_strSQLQRY = "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where PR_PRDCD=ST_PRDCD and PR_PRDDS='"+strGRADE+"'";
			M_strSQLQRY += strLOTSTR;
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				return true;
				}
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
		return false;
	}
	
	private boolean vldLOTNO(){/** valid LOT NO the retun value is boolean */
		try{
			M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO from FG_STMST,CO_PRMST where ST_PRDCD=PR_PRDCD and ST_LOTNO='"+strLOTNO+"'";
			M_strSQLQRY += strGRDFR;
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				return true;
				}
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
		return false;
	}
	
	private void vldPERIOD(){ /**Validates Entered From Date*/
		strPERIOD = txtPERIOD.getText().toString().trim();
		if(Integer.parseInt(strPERIOD) >= 0){
			setMSG("Press Run Button to fetch the data.",'N');
			btnRUN.requestFocus();
		}else{
			setMSG("InValid No. of Days.",'E');
			txtPERIOD.requestFocus();
		}
	}
	            
    /**........ Insert the Records into the Table..................*/
    
    private void getDTLREC(JTable tblSTKDTL)
	            {
		        try
		        {
		          strPRVPDS = "";
			     String L_SUMQT = "";
			     String L_STKQT = "";
			     String L_UCLQT = "";
			     String L_DATE = "";
			     strTOTPRQT= "";
			     dblSTKQT = 0;
				 dblTOTPRQT=0;
				 dblTOTQT=0;
			     M_strSQLQRY = "Select PR_PRDDS,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RCTDT,(day(getdate())-day(ST_RCTDT)) L_DATE,sum(ST_STKQT+ST_UCLQT) L_STKQT";
			     M_strSQLQRY += " from CO_PRMST,FG_STMST where ST_PRDCD = PR_PRDCD and (ST_STKQT+ST_UCLQT) > 0 ";
			     M_strSQLQRY += strGRDFR + strLOTSTR + " group by PR_PRDDS,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RCTDT,(day(getdate())-day(ST_RCTDT))";
			     M_strSQLQRY += " having day(getdate())-day(ST_RCTDT) >= "+strPERIOD+" order by PR_PRDDS,(day(getdate())-day(ST_RCTDT)),ST_LOTNO,ST_RCLNO";
			    
			     M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			     
			     int i = 0;
			    /* if(M_rstRSSET != null)
			     setMSG("No Records Found ",'N');*/
			     while(M_rstRSSET.next())
			     {
            	    tblSTKDTL.setValueAt(M_rstRSSET.getString("L_DATE").trim(),i,TB_RESVL);
				   // if(M_strPRDDS != null)
				    tblSTKDTL.setValueAt(M_rstRSSET.getString("PR_PRDDS").trim(),i,TB_GRADE);
				    //if(M_strLOTNO != null)
				    tblSTKDTL.setValueAt(M_rstRSSET.getString("ST_LOTNO").trim()+"  ",i,TB_LOTNO);
				    //if(M_strBAGDT != null)
				    tblSTKDTL.setValueAt(" "+M_fmtLCDAT.format(M_rstRSSET.getDate("ST_RCTDT")),i,TB_BAGDT);
				    dblSTKQT = M_rstRSSET.getDouble("L_STKQT");
 				    //  tblSTKDTL.setValueAt(setNumberFormat(M_dblSTKQT,3),i,TB_STKQT);
				    tblSTKDTL.setValueAt(setNumberFormat(dblSTKQT,3),i,TB_STKQT);
					
					
					dblTOTQT+= dblSTKQT;
					//System.out.println(dblTOTQT);
						
			i++;
			} 
			M_rstRSSET.close();
		  }catch(Exception L_EX)
		     {
			      System.out.println(L_EX);
		     }
				     
	   }
 /**......This is used the get the No.Of records ...*/   
    private int getDTLRW(){
		try{
		   
			M_strSQLQRY = "Select count(*) from CO_PRMST,FG_STMST";
			M_strSQLQRY += " where ST_PRDCD=PR_PRDCD and (ST_STKQT+ST_UCLQT) > 0";
			M_strSQLQRY += strGRDFR + strLOTSTR;
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			while(M_rstRSSET.next()){
				L_intCNT = M_rstRSSET.getInt(1);
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			return L_intCNT;
	}catch(Exception L_EX){
			System.out.println(L_EX);
		}
		return 0;
	}
    
/* This is Print the Header*/
    
     private void prnHEADER(){
		try{
		    
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO +=1;
	    	cl_dat.M_intLINNO_pbst = 0;
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			prnFMTCHR(dosREPORT,M_strBOLD);
		    dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
			dosREPORT.writeBytes(padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,94));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			dosREPORT.writeBytes(padSTRING('R',"LotWise Residence Statement for the period >= "+strPERIOD,65));
			dosREPORT.writeBytes(padSTRING('L',"Page No    : " + cl_dat.M_PAGENO,45));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			crtLINE(122);
			dosREPORT.writeBytes("\n");
			
			cl_dat.M_intLINNO_pbst+=1;
			dosREPORT.writeBytes(padSTRING('R'," ",6));
			dosREPORT.writeBytes(padSTRING('R',"Grade",15));
			dosREPORT.writeBytes(padSTRING('R',"Lot No",15));
			dosREPORT.writeBytes(padSTRING('R',"Bagging Date",22));
			dosREPORT.writeBytes(padSTRING('L',"Residence Period",28));
			dosREPORT.writeBytes(padSTRING('L',"Stock Qty.",20));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			dosREPORT.writeBytes(padSTRING('R'," ",58));
			
			dosREPORT.writeBytes(padSTRING('L',"Approx. in Days",28));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			crtLINE(122);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
		}catch(Exception L_EX){
			System.out .println ("L_EX..prnhead....... :"+L_EX);
		}
     }
 /* *To print the the screen  fetch & display the  Records..........*/
    private void outRECPRN(){
		try{
			
			if(!strPRDDS.equals(strPRVPDS))
			 {
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('R',"",86));
				dosREPORT.writeBytes(padSTRING('L',strTOTPRQT,20));
				
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst +=1;
				strTOTPRQT= "0";
				strPRVPDS = strPRDDS;
			}
			dosREPORT.writeBytes(padSTRING('R',"",6));
			dosREPORT.writeBytes(padSTRING('R',strPRDDS,15));
			dosREPORT.writeBytes(padSTRING('R',strLOTNO,15));
			if(strBAGDT == null || strBAGDT.equals(""))
				dosREPORT.writeBytes(padSTRING('R',"-",22));
			else
				dosREPORT.writeBytes(padSTRING('R',strBAGDT,22));
			dosREPORT.writeBytes(padSTRING('L',strRESVL,28));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblSTKQT,3),20));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst +=1;
			
			dblTOTPRQT =(Double.parseDouble(strTOTPRQT)) + dblSTKQT;
			
			strTOTPRQT = setNumberFormat(dblTOTPRQT,3);
			
    	  
          
		}catch(Exception L_EX){
			System.out.println(L_EX);
			}
			
	}
 /* *This is used to the from given inputs to Fetch the Data */
    private void getQRSTR()
    {
		try{
			strGRDFR = "";
			strLOTSTR = "";
			strGRDFR = txtGRDFR.getText().toString().trim();
			strGRDTO = txtGRDTO.getText().toString().trim();
			strLOTFR = txtLOTFR.getText().toString().trim();    
			strLOTTO = txtLOTTO.getText().toString().trim();
			strPERIOD = txtPERIOD.getText().toString().trim();
			if(strPERIOD.length() == 0)
				strPERIOD = "0";
			if(!strGRDFR.equals("") && !strGRDTO.equals(""))
			{
				strGRDFR = " and PR_PRDDS between '"+strGRDFR.trim()+"' and '"+strGRDTO.trim()+"'";
				}
			if(!strLOTFR.equals("") && !strLOTTO.equals("")){
				strLOTSTR = " and ST_LOTNO between '"+strLOTFR.trim()+"' and '"+strLOTTO.trim()+"'";
				}
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
		
    }
 /** Insert the Line crtLINE argument is Integer */
    private void crtLINE(int L_intCNT){
		String strln = "";
		try{
			for(int i=1;i<=L_intCNT;i++){
				strln += "-";
				}
				dosREPORT.writeBytes(padSTRING('L'," ",LM_LMRGN));
				dosREPORT.writeBytes(strln);
		        }
		        
			catch(Exception L_EX){
				System.out.println("L_EX Error in Line:"+L_EX);
				}
		}
   
   /**This method is to print the data into document & HTML format and also print & fax & Mail*/
    
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
		    System.out.println("sss");
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
   		     strPRVPDS = "";
			 String L_SUMQT = "";
			 String L_STKQT = "";
			 String L_UCLQT = "";
			 String L_DATE = "";
			 strTOTPRQT= "";
			 dblSTKQT = 0;
			 M_strSQLQRY = "Select PR_PRDDS,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RCTDT,(day(getdate())-day(ST_RCTDT)) L_DATE,sum(ST_STKQT+ST_UCLQT) L_STKQT";
			 M_strSQLQRY += " from CO_PRMST,FG_STMST where ST_PRDCD = PR_PRDCD and (ST_STKQT+ST_UCLQT) > 0 ";
			 M_strSQLQRY += strGRDFR + strLOTSTR + " group by PR_PRDDS,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RCTDT,(day(getdate())-day(ST_RCTDT))";
			 M_strSQLQRY += " having day(getdate())-day(ST_RCTDT) >= "+strPERIOD+" order by PR_PRDDS,(day(getdate())-day(ST_RCTDT)),ST_LOTNO,ST_RCLNO";
			 
			 M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			     
			 int i = 0;
			 while(M_rstRSSET.next())
			 {
			    strPRDDS = M_rstRSSET.getString("PR_PRDDS").trim();
			    strPRDTP = M_rstRSSET.getString("ST_PRDTP").trim();
			    strLOTNO = M_rstRSSET.getString("ST_LOTNO").trim();
			    dblSTKQT = M_rstRSSET.getDouble("L_STKQT");
                strBAGDT = M_fmtLCDAT.format(M_rstRSSET.getDate("ST_RCTDT"));
			    strRESVL = M_rstRSSET.getString("L_DATE").trim()  ;
				i++;	  
   				outRECPRN(); 
   				if(cl_dat.M_intLINNO_pbst > 60)
				{
					dosREPORT.writeBytes("\n");
					crtLINE(122);
					prnFMTCHR(dosREPORT,M_strEJT);
					prnHEADER();
				}
			}	     
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			dosREPORT.writeBytes(padSTRING('R'," ",6));
			dosREPORT.writeBytes(padSTRING('R',"(*) Indicates Lots are not Completely Despatched.",65));
			//prnFMTCHR(dosREPORT,M_strCPI17);
			//prnFMTCHR(dosREPORT,M_CPI10);
				
			dosREPORT.writeBytes("\n");
			crtLINE(122); System.out.println(dblTOTPRQT);
			dosREPORT.writeBytes("\n");		
			dblTOTPRQT =(Double.parseDouble(strTOTPRQT)) + dblSTKQT;
			dblTOTPRQT+=dblSTKQT;
			
			dosREPORT.writeBytes(padSTRING('R',"",65));
			prnFMTCHR(dosREPORT,M_strBOLD);
			//prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(padSTRING('L',"Grand Total",26));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblTOTQT,3),15));
			prnFMTCHR(dosREPORT,M_strEJT);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			setMSG("Clicked Status Indicates Lots are not Completely Despatched.",'N');
			dosREPORT.close();
			fosREPORT.close();
		}
		catch(Exception L_EX)
		{
		    setMSG(L_EX,"GetAllrecords ");
		 }   
	 }  
	 
	 /***clears Lot Table*/
	 private void clrDTLTBL(){  
		for(int i = 0;i<tblSTKDTL.getRowCount();i++){
			for(int j = 0;j<tblSTKDTL.getColumnCount();j++){
				tblSTKDTL.setValueAt(" ",i,j);
				}
			}
	}
	    
}//end of class
			
	
	
	
	
	
	
	
	
	
