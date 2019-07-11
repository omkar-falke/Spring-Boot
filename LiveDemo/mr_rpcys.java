/*
 * Created on May 17, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author SSR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.awt.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.lang.*;
//import cl_eml;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Date; 
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.*;



public class mr_rpcys extends  cl_rbase
{


    private JComboBox cmbZON;
    private JComboBox cmbSALT;
    private JButton btnRUN;
    private String strCMTCOD;
    private String strCODSAL;
    private DataOutputStream dosREPORT;
    private String strFILNM;
    private FileOutputStream fosREPORT;
   
    private String[] strINVDT;
    //private String[] strHEADER;
    private int[] intCOLZ;
    private cl_JTable tblSLTRG;
    private String strCODDS;
    private String strCODCD;
    private String strCODCDSAL;
    private Vector cache;
    private int colCount;
    private String[] headers;
    private String[] strINV;
    JPanel pnlTBL;
    private JTabbedPane tabMAIN;
    private String strYYM;
    private Hashtable<String,String> hstMONNM /*,hstVALU*/;
	String[] arrMONNM = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	String[] arrLINE = {"-"}; 
	    private Object[] arrHSTKEY;
    	
    private int intSUMFIX = 3;
    private String strBYRCD,strPRTNM;
    private String[] arrFILDVL; 
    private String[] arrSUMHDR = new String[] {"0","1","2"};
	private double[] arrCOLTOT = new double[] {0.000,0.000,0.000};
	private int[] arrSUMHDR_WD = new int[] {0,1,2};
	private char[] arrSUMHDR_PAD = new char[] {'0','1','2'};
    //private Hashtable hstVALU1;
    //private String[] strIVTQT= new String[intAE_PT];
    private double L_dblIVTQT;
    private int intAE_PT_PRTNM=0;
    private int intAE_PT_CODCD=1;
    private int intAE_PT_YYM=2;
    private int intAE_PT_IVTQT;
    private int intPTMSTROW;
    private String strIVTQT;
    private int intIVTQT;
    String[] staPTMST = new String[intPTMSTROW];
    private Object L_strIVTQT;
    private boolean L_flgEOF;
    private int intLINECT;
    private int intPAGENO;
    private int intSUMWD=193;
    private int intRUNCL;
    private int intSUMCL;
    private static final int L_ROWNO = 0;
    private double L_dblIVTQT1;
    private double dblCOLTOT;
    private String strQRDAT1;
    private String strQRDAT2;
    private java.sql.Date dtYSTDT;
    /**
      * This constructor create the Components into the Frame
     */
    mr_rpcys()
	{
		super(2);
	  try
	  {	
	      setMatrix(20,12);
	      
	      add(new JLabel("Zone :"),3,2,1,1,this,'L');
	      add(cmbZON=new JComboBox(),3,3,1,1.5,this,'L');
	      
	      
	      add(new JLabel("Sale Type :"),3,5,1,1.5,this,'L');
	      add(cmbSALT=new JComboBox(),3,6,1,2,this,'L');
	      
	      add(btnRUN=new JButton("RUN"),3,8,1,1.5,this,'L');
	       tabMAIN=new JTabbedPane();
	       
	     
	      cache = new Vector();
	      //and CMT_CODCD <= '11'
	      M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MR00ZON' and CMT_CODCD <= '11' ";
	      M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
	      if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
					strCMTCOD = M_rstRSSET.getString("CMT_CODDS") ;
					strCODCD=M_rstRSSET.getString("cmt_codcd");
					
					
					
					
					cmbZON.addItem(strCODCD +" "+strCMTCOD);
			}
	      M_strSQLQRY="Select CMT_CODDS,CMT_CODCD from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MR00SAL' ";
	      M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	      if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
						strCODSAL = M_rstRSSET.getString("CMT_CODDS") ;
						strCODCDSAL =M_rstRSSET.getString("cmt_codcd");
						cmbSALT.addItem(strCODCDSAL +" " +strCODSAL);
				}
	     
 	     
	      hstMONNM=new Hashtable<String,String>();
	      //hstVALU=new Hashtable();
	      //hstVALU1=new Hashtable();
	      cmbZON.addMouseListener(this);
	      cmbSALT.addMouseListener(this);
	      M_pnlRPFMT.setVisible(true);
	      
	    
	      setENBL(false);
	      
	      
	      
	      
	  }catch(Exception L_EX)
	  {
	      setMSG(L_EX,"This is constructor");
	  }
	}
    
    /**
     *  ActionPerformed is used for the events on the components
     * 
     */
    
    public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE);
	  try
	  {
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{   
			//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)))
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{				
				
				M_txtFMDAT.requestFocus();
				setMSG("Please Select Option..",'N');	
				
				if(M_txtFMDAT.getText().length()<10)
				{
				    
				  M_strSQLQRY="  select sp_ystdt,sp_yendt from sa_spmst where sp_syscd='MR'";
				  M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				  while(M_rstRSSET.next() )
				  {    
                    dtYSTDT=M_rstRSSET.getDate("sp_ystdt");
                   // System.out.println(dtYSTDT);
				    
				  } 
				   M_txtFMDAT.setText(M_fmtLCDTM.format(dtYSTDT).substring(0,10));
				}
				if(M_txtTODAT.getText().length()<10)
				{
				   
					M_txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				}  
				setENBL(true);
				btnRUN.setEnabled( false);
			}
			else
			{					
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				setMSG("select option..",'N');
				setENBL(false);
			}	
		}
		
		
		if(M_objSOURC == M_txtFMDAT)
		{
			M_txtTODAT.requestFocus();
		}	
		if(M_objSOURC == M_txtTODAT)
		    cmbZON.requestFocus();
		if(M_objSOURC==cmbZON)
		{
			btnRUN.setEnabled( true);
			btnRUN.requestFocus();
		    
		}
		if(M_objSOURC==cmbSALT)
		{
		    btnRUN.setEnabled( true);
			btnRUN.requestFocus();
		    
		}
		
		
		if(M_objSOURC==btnRUN)
		{
		    
		   try
		   {
		      tabMAIN.remove(pnlTBL);  
		      tblHEADER(tblSLTRG);
		      tblSLTRG.setEnabled(false);
		    //  tabMAIN.remove(pnlTBL);
		    
//   tblSLTRG.removeAll() ;
		   }catch(Exception L_EX)
		   {
		       
		   }
		    
		    
		    
		}
	  }catch(Exception L_EX)
	  {
	      setMSG(L_EX,"This is action Performed");
	  }
		
		
	}
    /**
     * This is used for the print the report . this extends cl_rbase this is default funcion
     */
    void exePRINT()
	{   
	    
		try
	    {
		    if (!vldDATA())
    			return;
	
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
	/** Method to fetch data from tblSLTRG table & club it with Header in Data Output Stream  and Display the SummaryHeader */
	  private void getDATA()
	 {
		try
		{   
			cl_dat.M_PAGENO = 0;
		    String strln="";
		    System.out.println("sss");
		    //System.out.println("th file name is " +strFILNM);
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpcys.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpcys.doc";	
			
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
		        
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Monthly Wise Sale Analysis</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
			 }
			 else
				prnFMTCHR(dosREPORT,M_strCPI17);
   		    
   		    
       	
   	       //   pnlTBL=new JPanel(null);
   		   
   	          tblSLTRG= crtTBLPNL1(pnlTBL,arrSUMHDR,5000,1,1,13,11,arrSUMHDR_WD,new int[]{0});
   	 	 	 // tabMAIN.addTab("Montly ",pnlTBL);
   		 	 //add(tabMAIN,4,1,15,11.5,this,'L');
   	        tblSLTRG.setEnabled(false);
   	 	      
   	 	     
   	 	  
   		     String L_strCMTCOD="",L_strPRTNM="",L_strBYRCD="";
   		     
   		     
   		     
   		  //   pnlTBL.removeAll() ;
   		     M_strSQLQRY= "select ivt_byrcd,pt_prtnm, cmt_codds,SUBSTRING(CONVERT(varchar,ivt_invdt,103),7,2) + SUBSTRING(CONVERT(varchar,ivt_invdt,103),4,2) YYMM,sum(ivt_invqt) ivt_invqt  from mr_ivtrn,co_ptmst,co_cdtrn  ";
   			 M_strSQLQRY += " where ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_zoncd='"+cmbZON.getSelectedItem().toString().trim().substring(0,2)+"' and CONVERT(varchar,ivt_invdt,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText() .toString() .trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText() .toString().trim()))+"' and ivt_saltp='"+cmbSALT.getSelectedItem() .toString() .trim().substring(0,2) +"'  and pt_prttp='C' and pt_prtcd=ivt_byrcd and ";
   			 M_strSQLQRY += " ivt_stsfl != 'X' and ivt_SBSCD1 in "+M_strSBSLS; 
   			 M_strSQLQRY += " and cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and left(cmt_codcd,4) = left(ivt_prdcd,4) group by ivt_byrcd,pt_prtnm,cmt_codds,SUBSTRING(CONVERT(varchar,ivt_invdt,103),7,2) + SUBSTRING(CONVERT(varchar,ivt_invdt,101),4,2) ";
   			 M_strSQLQRY += " order by ivt_byrcd,pt_prtnm,cmt_codds,SUBSTRING(CONVERT(varchar,ivt_invdt,103),7,2) + SUBSTRING(CONVERT(varchar,ivt_invdt,103),4,2)";
   			 M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
   			    System.out.println(M_strSQLQRY);
   			   if(!M_rstRSSET.next() || M_rstRSSET==null)
   					return;
   			 
   			    int  i=0;
   			    int k=0;
   			    
   			    double[] arrCOLTOT = new double[arrHSTKEY.length+intSUMFIX+1];
   				for(  k=0;k<arrCOLTOT.length;k++)
   					arrCOLTOT[k]=0.000;
   				strBYRCD = M_rstRSSET.getString("ivt_byrcd");
   				strPRTNM = M_rstRSSET.getString("pt_prtnm");
   				strCODDS = M_rstRSSET.getString("cmt_codds");
   				strIVTQT=M_rstRSSET.getString("ivt_invqt");
   				tblSLTRG.setValueAt(M_rstRSSET.getString("pt_prtnm"),i,1);
				tblSLTRG.setValueAt(M_rstRSSET.getString("cmt_codds"),i,2);
				
   				L_dblIVTQT =0.000;	  
   			    while(true )
   			    {
   					if(!strBYRCD.equals(M_rstRSSET.getString("ivt_byrcd")) && !strPRTNM.equals(M_rstRSSET.getString("pt_prtnm")))
					{
						tblSLTRG.setValueAt(M_rstRSSET.getString("pt_prtnm"),i,1);
   						strPRTNM = M_rstRSSET.getString("pt_prtnm");
   						strBYRCD = M_rstRSSET.getString("ivt_byrcd");
					}
   					if(!(strBYRCD+strPRTNM+strCODDS).equals(M_rstRSSET.getString("ivt_byrcd")+M_rstRSSET.getString("pt_prtnm")+M_rstRSSET.getString("cmt_codds")))
   					{
   					    tblSLTRG.setValueAt(M_rstRSSET.getString("cmt_codds"),i,2);
   						strBYRCD = M_rstRSSET.getString("ivt_byrcd");
   						strPRTNM = M_rstRSSET.getString("pt_prtnm");
   						strCODDS = M_rstRSSET.getString("cmt_codds");
   						
   					
   					// L_dblIVTQT =0.000; 
   					}
   					 int L_intCOLVL = 0;
   					 String L_strCOLVL="";
   					
   					 if(hstMONNM.containsKey(M_rstRSSET.getString("YYMM")) )
   					 {
   						L_strCOLVL = hstMONNM.get(M_rstRSSET.getString("YYMM")).toString();
   						L_intCOLVL = Integer.parseInt(L_strCOLVL);
   					    tblSLTRG.setValueAt(M_rstRSSET.getString("ivt_invqt"),i,L_intCOLVL);
   					    arrCOLTOT[L_intCOLVL]+= Double.parseDouble(M_rstRSSET.getString("ivt_invqt"));
   					        
   						 
   					 }
   					L_dblIVTQT+=Double.parseDouble(M_rstRSSET.getString("ivt_invqt"));
   					 tblSLTRG.setValueAt(setNumberFormat(L_dblIVTQT,3),i,arrHSTKEY.length +intSUMFIX);
   					 arrCOLTOT[arrCOLTOT.length-1] += L_dblIVTQT;
   					
   					if(!M_rstRSSET.next())
   					     break;
   					
   					 if(!(strBYRCD+strPRTNM+strCODDS).equals(M_rstRSSET.getString("ivt_byrcd")+M_rstRSSET.getString("pt_prtnm")+M_rstRSSET.getString("cmt_codds")))
					 {
   					 		i++;
   							L_dblIVTQT =0.000;	  
					 }
   					 
   			    }
   			 if(M_rstRSSET ==null)
			        M_rstRSSET.close() ;
   			    i++;
   			    
   			 //   pnlTBL.removeAll() ;*/
   			   
   			   cl_dat.M_PAGENO  = 0;// cl_dat.M_intLINNO_pbst  = 72;
				 
				int  intRUNCL = 0;	
                
				prnHEADER();	
				System.out.println("column Count  " +tblSLTRG.getColumnCount() );
				System.out.println(tblSLTRG.getRowCount());
				for(  i=0;i<tblSLTRG.getRowCount()-5;i++)
				{
					if(tblSLTRG.getValueAt(i,arrHSTKEY.length+intSUMFIX).toString().equals(""))
						break;
					else if(Double.parseDouble(tblSLTRG.getValueAt(i,arrHSTKEY.length+intSUMFIX).toString())==0)
						break;
					intRUNCL = 0;
					dosREPORT.writeBytes(padSTRING(arrSUMHDR_PAD[1],tblSLTRG.getValueAt(i,1).toString (),arrSUMHDR_WD[1]/8)  + "      " );
					dosREPORT.writeBytes(padSTRING(arrSUMHDR_PAD[2],tblSLTRG.getValueAt(i,2).toString(), arrSUMHDR_WD[2]/8));
					intRUNCL += 1;
				
					for( k=3;k<tblSLTRG.getColumnCount() ;k++)
					{
						dosREPORT.writeBytes(padNUMBER(arrSUMHDR_PAD[k],tblSLTRG.getValueAt(i,k).toString(),arrSUMHDR_WD[k]/8)); 
						intRUNCL += 1;
						if(intRUNCL>intSUMCL)
						{
							dosREPORT.writeBytes(padSTRING('R'," ",arrSUMHDR_WD[1]/64)+" ");
							dosREPORT.writeBytes(padSTRING('R'," ",arrSUMHDR_WD[2]/64));
							intRUNCL=0;
							cl_dat.M_intLINNO_pbst  +=1;
							if(cl_dat.M_intLINNO_pbst >60) 
							    prnHEADER();
						}
					}
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst  +=2;
				}
				int L_intCOLVL=0;
				if(hstMONNM.containsKey(strYYM))
			    {
		           String  L_strCOLVL = hstMONNM.get(strYYM).toString();
			      L_intCOLVL=arrSUMHDR.length+Integer.parseInt(L_strCOLVL);
			     }
				dosREPORT.writeBytes("\n"+crtLINE(L_intCOLVL*(6),"-")+"\n");
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('R',"Total :" ,21));
				for(  k=intSUMFIX;k<arrCOLTOT.length;k++)
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(arrCOLTOT[k],3),arrSUMHDR_WD[k]/6));;
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
				
				dosREPORT.close();
				dosREPORT.close();
				setMSG(" ",'N');   
		     
   			   
   		       
   		    
   		    
   		    
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"Get Data");
		}
	 }	
   /**
    *  Table Header used for the fetch the records on the DabaBase. and display the Dynamic Table
    * @param tblSLTRG
    */ 
    
 private void tblHEADER(cl_JTable tblSLTRG)
 {
     try
     {
         int j ;
	     M_strSQLQRY= "select distinct SUBSTRING(CONVERT(varchar,ivt_invdt,103),7,2) + SUBSTRING(CONVERT(varchar,ivt_invdt,103),4,2) YYMM from mr_ivtrn where ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_zoncd='"+cmbZON.getSelectedItem().toString().trim().substring(0,2)+"' and CONVERT(varchar,ivt_invdt,101) " ;
	     M_strSQLQRY += "between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText() .toString() .trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText() .toString().trim()))+"' and ivt_saltp='"+cmbSALT.getSelectedItem() .toString() .trim().substring(0,2) +"'  and  ivt_stsfl != 'X' and ivt_SBSCD1 in "+M_strSBSLS  ;
	     M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
	     System.out.println(M_strSQLQRY);
	   hstMONNM.clear();
	   
	   if(!M_rstRSSET.next() || M_rstRSSET==null)
			return;
	   j=intSUMFIX;
	   while(true)
	     {
		          strYYM = M_rstRSSET.getString("YYMM");
		          
		          hstMONNM.put(strYYM,String.valueOf(j));
		          j++;
				  if(!M_rstRSSET.next())
					  break;
		         
	     }
	   
	   		setHST_ARR(hstMONNM);
		
		arrSUMHDR = new String[arrHSTKEY.length+intSUMFIX+1];
   		arrCOLTOT = new double[arrHSTKEY.length+intSUMFIX+1];
		arrSUMHDR[0] = ""; arrSUMHDR[1] = "Customer Name";arrSUMHDR[2] = "Product";
		arrSUMHDR_WD = new int[arrHSTKEY.length+intSUMFIX+1];
	    arrSUMHDR_WD[0] = 20;arrSUMHDR_WD[1] = 75;arrSUMHDR_WD[2] =75 ;
	    
	    
		 for( int i=intSUMFIX; i<arrHSTKEY.length+intSUMFIX; i++)
		 {
			   int L_intMM = Integer.parseInt(arrHSTKEY[i-intSUMFIX].toString().substring(2,4));
		       arrSUMHDR[i] = arrMONNM[L_intMM-1]+"-"+arrHSTKEY[i-intSUMFIX].toString().substring(0,2);
				   
		 }
		 arrSUMHDR[arrHSTKEY.length+intSUMFIX] = "Total";
		 
		 for( int i=intSUMFIX; i<arrHSTKEY.length+intSUMFIX; i++)
		 	     arrSUMHDR_WD[i] = 75; 
		 arrSUMHDR_WD[arrHSTKEY.length+intSUMFIX] = 75;
		 
		 arrSUMHDR_PAD = new char[arrHSTKEY.length+intSUMFIX+1];
			arrSUMHDR_PAD[0] = 'R'; arrSUMHDR_PAD[1] = 'R';arrSUMHDR_PAD[2] = 'R';
			for(int i=intSUMFIX; i<arrHSTKEY.length+intSUMFIX; i++)
				arrSUMHDR_PAD[i] = 'L';
			arrSUMHDR_PAD[arrHSTKEY.length+intSUMFIX] = 'L';
			
	         pnlTBL=new JPanel(null);
	         
	         
	         
	         M_strSQLQRY= "select count(*) ivt_invqt from mr_ivtrn where ivt_zoncd='"+cmbZON.getSelectedItem().toString().trim().substring(0,2)+"' and ivt_saltp='"+cmbSALT.getSelectedItem() .toString() .trim().substring(0,2) +"'";
	         M_strSQLQRY += " and CONVERT(varchar,ivt_invdt,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText() .toString() .trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText() .toString().trim()))+"' ";
	         ResultSet rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
	         System.out.println(M_strSQLQRY);
	         if(!rstRSSET.next()  || rstRSSET==null)
				{setMSG("No Detail Records Found",'E');}
	         int L_intRECCT = Integer.parseInt(rstRSSET.getString("ivt_invqt"));
			//	L_intRECCT = L_intRECCT; 
				System.out.println("No. of rows" +L_intRECCT);
				rstRSSET.close();
		     tblSLTRG= crtTBLPNL1(pnlTBL,arrSUMHDR,L_intRECCT,1,1,13,11,arrSUMHDR_WD,new int[]{0});
	 	 	  tabMAIN.addTab("Montly ",pnlTBL);
		 	 add(tabMAIN,4,1,15,11.5,this,'L');
	 	      updateUI() ;
	 	     tblSLTRG.addMouseListener( this);
	 	    tblSLTRG.setEnabled(false);
		     String L_strCMTCOD="",L_strPRTNM="",L_strBYRCD="";
		     
		     
		     
		     
		     M_strSQLQRY= "select ivt_byrcd,pt_prtnm, cmt_codds,SUBSTRING(CONVERT(varchar,ivt_invdt,103),7,2) + SUBSTRING(CONVERT(varchar,ivt_invdt,103),4,2) YYMM,sum(ivt_invqt) ivt_invqt  from mr_ivtrn,co_ptmst,co_cdtrn  ";
			 M_strSQLQRY += " where ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_zoncd='"+cmbZON.getSelectedItem().toString().trim().substring(0,2)+"' and CONVERT(varchar,ivt_invdt,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText() .toString() .trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText() .toString().trim()))+"' and ivt_saltp='"+cmbSALT.getSelectedItem() .toString() .trim().substring(0,2) +"'  and pt_prttp='C' and pt_prtcd=ivt_byrcd and ";
			 M_strSQLQRY += " ivt_stsfl != 'X' and ivt_SBSCD1 in "+M_strSBSLS;
			 M_strSQLQRY +="  and cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and left(cmt_codcd,4) = left(ivt_prdcd,4) group by ivt_byrcd,pt_prtnm,cmt_codds,SUBSTRING(CONVERT(varchar,ivt_invdt,103),7,2) + SUBSTRING(CONVERT(varchar,ivt_invdt,103),4,2) ";
			 M_strSQLQRY += " order by ivt_byrcd,pt_prtnm,cmt_codds,SUBSTRING(CONVERT(varchar,ivt_invdt,103),7,2) + SUBSTRING(CONVERT(varchar,ivt_invdt,103),4,2)";
			 M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    System.out.println(M_strSQLQRY);
			    L_dblIVTQT =0.000;
			    
			    int  i=0;
			    int k=0;
			   
			    double[] arrCOLTOT = new double[arrHSTKEY.length+intSUMFIX+1];
				for(  k=0;k<arrCOLTOT.length;k++)
					arrCOLTOT[k]=0.000;
					double[] arrROWTOT = new double[arrHSTKEY.length +intSUMFIX];
				for(int l=0;l<arrROWTOT.length ;l++)	
					arrROWTOT[l]=0;
					
				 if(!M_rstRSSET.next() || M_rstRSSET==null)
							return;
				
				  
				strBYRCD = M_rstRSSET.getString("ivt_byrcd");
				strPRTNM = M_rstRSSET.getString("pt_prtnm");
				strCODDS = M_rstRSSET.getString("cmt_codds");
				tblSLTRG.setValueAt(M_rstRSSET.getString("pt_prtnm"),i,1);
				tblSLTRG.setValueAt(M_rstRSSET.getString("cmt_codds"),i,2);
				
						 	  
			    while(true)
			    {
			       // if((strPRTNM).equals(M_rstRSSET.getString("pt_prtnm")))
					 //   tblSLTRG.setValueAt(M_rstRSSET.getString("pt_prtnm"),0,1);
					
			        if(!(strPRTNM).equals(M_rstRSSET.getString("pt_prtnm")) && !(strBYRCD).equals(M_rstRSSET.getString("ivt_byrcd")))
					{
					    tblSLTRG.setValueAt(M_rstRSSET.getString("pt_prtnm"),i,1);
						strBYRCD = M_rstRSSET.getString("ivt_byrcd");
						strPRTNM = M_rstRSSET.getString("pt_prtnm");
					}
					if(!(strBYRCD+strPRTNM+strCODDS).equals(M_rstRSSET.getString("ivt_byrcd")+M_rstRSSET.getString("pt_prtnm")+M_rstRSSET.getString("cmt_codds")))
					{
					    tblSLTRG.setValueAt(M_rstRSSET.getString("cmt_codds"),i,2);
					 
						strBYRCD = M_rstRSSET.getString("ivt_byrcd");
						strPRTNM = M_rstRSSET.getString("pt_prtnm");
						strCODDS = M_rstRSSET.getString("cmt_codds");
					//	L_dblIVTQT+=M_rstRSSET.getDouble("ivt_invqt");
					
					}
					 
					 int L_intCOLVL = 0;
					 String L_strCOLVL="";
					
					 if(hstMONNM.containsKey(M_rstRSSET.getString("YYMM")) )
					 {
						 L_strCOLVL = hstMONNM.get(M_rstRSSET.getString("YYMM")).toString();
						 L_intCOLVL = Integer.parseInt(L_strCOLVL);
					     tblSLTRG.setValueAt(M_rstRSSET.getString("ivt_invqt"),i,L_intCOLVL);
						 arrCOLTOT[L_intCOLVL] += Double.parseDouble(M_rstRSSET.getString("ivt_invqt"));
						
					 }
					 L_dblIVTQT+= Double.parseDouble(M_rstRSSET.getString("ivt_invqt"));
					 tblSLTRG.setValueAt(setNumberFormat(L_dblIVTQT,3),i,arrHSTKEY.length +intSUMFIX);
					 arrCOLTOT[arrCOLTOT.length-1] += L_dblIVTQT;
			    	 
					 if(!M_rstRSSET.next())
						    break;
					 if(!(strBYRCD+strPRTNM+strCODDS).equals(M_rstRSSET.getString("ivt_byrcd")+M_rstRSSET.getString("pt_prtnm")+M_rstRSSET.getString("cmt_codds")))
					 {
						i++;
		   				L_dblIVTQT =0.000;	  
					 }
	 	
			    }
			    i++;
    		     tblSLTRG.setRowColor(i,Color.blue);
    		    
				tblSLTRG.setValueAt("Total:",i,1);
				for(  k=intSUMFIX;k<arrCOLTOT.length;k++)
					    tblSLTRG.setValueAt(setNumberFormat(arrCOLTOT[k],3),i,k);
				
				
				
				
			   
		    }
		    
	 catch(Exception L_EX)
     {
         setMSG(L_EX,"This is table Header");
     }
      
	
 }


 private void setHST_ARR(Hashtable LP_HSTNM)
	{
		try
		{
			arrHSTKEY = LP_HSTNM.keySet().toArray();
			Arrays.sort(arrHSTKEY);
		}
		catch(Exception e){setMSG(e,"setHST_ARR");}
	}
 /** print the Header report on the page 
	*/
	private void prnHEADER()
	{
	   try
	   {
	       int L_intMM=0;
		    int  L_intCOLVL=0;
	    if(cl_dat.M_intLINNO_pbst>60)
	        return;
	    int i=0;
	    if(hstMONNM.containsKey(strYYM))
	    {
           String  L_strCOLVL = hstMONNM.get(strYYM).toString();
	       //L_intCOLVL = Integer.parseInt(L_strCOLVL+arrSUMHDR.length );
           L_intCOLVL=arrSUMHDR.length+Integer.parseInt(L_strCOLVL);
	    
	    }
		cl_dat.M_PAGENO +=1;
    	
		if(cl_dat.M_PAGENO>1)
		{
		    dosREPORT.writeBytes("\n"+crtLINE(L_intCOLVL*(6),"-")+"\n");
		    cl_dat.M_intLINNO_pbst+=2;
		    for(i=cl_dat.M_intLINNO_pbst;i<72;i++)
		    {
		        dosREPORT.writeBytes("\n");
		    }
		}   
		    prnFMTCHR(dosREPORT,M_strBOLD);
		    cl_dat.M_intLINNO_pbst=0;
		    dosREPORT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd",25)+padSTRING('C',"Monthly Wise Sales Analysis " +" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),(L_intCOLVL*6)-50)+padSTRING('L',"Page "+cl_dat.M_PAGENO ,20)+"\n"); 
		    cl_dat.M_intLINNO_pbst+=2;
		    intRUNCL = 0;
		    dosREPORT.writeBytes( "\n"+crtLINE(L_intCOLVL*(6)   ,"-") +"\n");
		    cl_dat.M_intLINNO_pbst  +=1;
		 
		    for(i=1;i<arrSUMHDR.length;i++)
			{
		        
		        dosREPORT.writeBytes(padSTRING(arrSUMHDR_PAD[i],arrSUMHDR[i],arrSUMHDR_WD[i]/7)); 
		        intRUNCL +=1;
		        if(intRUNCL>intSUMCL)
				{
		           // dosREPORT.writeBytes(padSTRING('L',"",5));
		            dosREPORT.writeBytes(padSTRING('R'," " ,arrSUMHDR_WD[1]/64	));
		            dosREPORT.writeBytes(padSTRING('R'," " ,arrSUMHDR_WD[2]/64	));
		            
		            
		            intRUNCL=0;
		            cl_dat.M_intLINNO_pbst  +=1;
		         }
		        		        
			}  
		   
					               
		        System.out.println("Create Line    "+L_intCOLVL);
		        dosREPORT.writeBytes( "\n"+crtLINE(L_intCOLVL*(6),"-") +"\n");
		
		   
		    prnFMTCHR(dosREPORT,M_strNOBOLD);
		        cl_dat.M_intLINNO_pbst  +=2;
		        
			
			
		    
		    
	
		
		
		
	
		
		
	   }catch(Exception L_EX)
	   {
	       setMSG(L_EX,"This is prnHEADER");
	   }
	    
	}
	/**
	 *
	 *Method to create lines that are used in the Reports
	 */
       private String crtLINE(int P_strCNT,String P_strLINCHR)
       {
		String strln = "";
		try{
             for(int i=1;i<=P_strCNT;i++)    strln += P_strLINCHR;
		}catch(Exception L_EX){
			System.out.println("L_EX Error in Line:"+L_EX);
		}
               return strln;
	    }
	
      
       
       
       /**
   	 */
   	private   String padNUMBER(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
   	{
   		String P_strTRNVL = "";
   		try
   		{
   			
   			if(P_strSTRVL.length()==0)
   				P_strSTRVL = "-";
   			else if(Double.parseDouble(P_strSTRVL)==0)
   				P_strSTRVL = "-";
   			P_strSTRVL = P_strSTRVL.trim();
   			int L_STRLN = P_strSTRVL.length();
   			if(P_intPADLN <= L_STRLN)
   			{
   				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
   				L_STRLN = P_strSTRVL.length();
   				P_strTRNVL = P_strSTRVL;
   			}
   			int L_STRDF = P_intPADLN - L_STRLN;
   			
   			StringBuffer L_STRBUF;
   			switch(P_chrPADTP)
   			{
   				case 'C':
   					L_STRDF = L_STRDF / 2;
   					L_STRBUF = new StringBuffer(L_STRDF);
   					for(int j = 0;j < L_STRBUF.capacity();j++)
   						L_STRBUF.insert(j,' ');
   					P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
   					break;
   				case 'R':
   					L_STRBUF = new StringBuffer(L_STRDF);
   					for(int j = 0;j < L_STRBUF.capacity();j++)
   					{
   						if(M_rdbTEXT.isSelected())
   						L_STRBUF.insert(j,' ');
   						else
   							L_STRBUF.insert(j,"\t");
   					}
   					P_strTRNVL =  P_strSTRVL+L_STRBUF ;
   					break;
   				case 'L':
   					L_STRBUF = new StringBuffer(L_STRDF);
   					for(int j = 0;j < L_STRBUF.capacity();j++)
   						L_STRBUF.insert(j,' ');
   					P_strTRNVL =  L_STRBUF+P_strSTRVL ;
   					break;
   			}
   		}catch(Exception L_EX){
   			setMSG(L_EX,"padNUMBER");
   		}
   		return P_strTRNVL;
   	}

   	  
    /**
	 * Method to validate the Inputs before execuation of the query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(M_txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				M_txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				M_txtFMDAT.requestFocus();
				return false;
			}
			if(M_txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}		
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In vldDATA");
			return false;
		}
		return true;
	}
    
 
 
 
 
}
