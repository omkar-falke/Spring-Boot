 /*
System Name					: Marketing System
Program Name				: Marketing Target Management
Program Desc.				: Entry provision for setting marketing targets zonewise.
Author						: Mr. Deepal N. Mehta
Date						: 8th February 2003
Version						: MKT 1.0
Special functionality used	: fetching of data through hashtable

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/



import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class mr_tetmn extends cl_pbase implements MouseListener
{
	String strPRDDS,strPKGTP;
	String strMMYDT,strPRDCD;
	String strPRTQT,strCELVAL,strPRDTP,strPKGDS,strHSTVAL,strDOMQT,strDEXQT,strEXPQT,LM_HLPFLD;
	String strCHP01,strMZONE,LM_DUMSTR,strADDSTR,strKEYVAL,strTOTDO,strTOTEX,strTOTDE,strCODDS;
    String strSTATS;
	JLabel lblTBLHDR;
	cl_JTable tblMKTTBL,tblMKTTBL1;
	private JTabbedPane tbpMAIN,tbpMAIN1;
	//cl_jTable tblMKTTBL;
	JPanel pnlMKK,pnlMKK1;
	TableColumn tclDOMQT,tclEXPQT,tclDEXQT,tclPRTQT;
	JTextField txtMMYDT,txtPRDDS,txtGRADE,txtMZONE,txtDOMQT,txtEXPQT,txtDEXQT,txtPRDTP,txtPKGTP,txtTBLQT;
	int intSTSFL,LM_CNT;
	
	
	int intTBL_CHKFL = 0;
	int intTBL_PRDDS = 1;
	int intTBL_PKGTP = 2;
	
	int intTBL_EXPQT = 3;
	
	int intTBL_DOMQT = 3;
	int intTBL_DEXQT = 4;
	int intTBL_PRTQT = 5;
	
	
	
	int intCNT=0;
	
	
/*	private String[] arrDTLHDR1 = new String[] {"0","1","2","3","4"};
	private int[] arrDTLHDR1_WD = new int[] {0,1,2,3,4};
	private char[] arrDTLHDR1_PAD = new char[] {'0','1','2','3','4'};*/
	
	int intPRMCNT,intNPRCNT,intOFGCNT,intSCRCNT;
	boolean flgTBLFL = false,flgUPDFL = false;
	Vector<String> vtrUNIDT,vtrSPECL,vtrPRIME,vtrNPRIM,vtrOFGRD,vtrSCRAP;
	Hashtable<String,String> hstPRKEY,hstTRGQT,hstDBSVL;
	PreparedStatement LM_PRPSTM;
	JOptionPane LM_OPTNPN;
	mr_tetmn()
	{
	    super(2);
	    try
	    {
	        LM_OPTNPN=new JOptionPane();
	        
	        setMatrix(20,20);
	       
	        vtrUNIDT = new Vector<String>();
			vtrSPECL = new Vector<String>();
			vtrPRIME = new Vector<String>();
			vtrNPRIM = new Vector<String>();
			vtrOFGRD = new Vector<String>();
			vtrSCRAP = new Vector<String>();
			hstPRKEY = new Hashtable<String,String>();
			hstTRGQT = new Hashtable<String,String>();
			hstDBSVL = new Hashtable<String,String>();
			
			
		/*	arrDTLHDR[intTBL_CHKFL] = "";
			arrDTLHDR[intTBL_PRDDS] = "Grades.";
			arrDTLHDR[intTBL_PKGTP] = "Package Type";
			arrDTLHDR[intTBL_EXPQT] ="Target Quantiry";
			arrDTLHDR[intTBL_PRTQT ] = "ProductTarget";
			
			arrDTLHDR_WD[intTBL_CHKFL] = 50;
			arrDTLHDR_WD[intTBL_PRDDS] = 100;
			arrDTLHDR_WD[intTBL_PKGTP] = 100;
			arrDTLHDR_WD[intTBL_DOMQT] = 100;
			arrDTLHDR_WD[intTBL_EXPQT] =100;
			arrDTLHDR_WD[intTBL_PRTQT] = 120;
			
			arrDTLHDR_PAD[intTBL_CHKFL] = 'R';
			arrDTLHDR_PAD[intTBL_PRDDS] = 'R';
			arrDTLHDR_PAD[intTBL_PKGTP] = 'R';
			arrDTLHDR_PAD[intTBL_DOMQT] = 'R';
			arrDTLHDR_PAD[intTBL_EXPQT] ='R';
			arrDTLHDR_PAD[intTBL_PRTQT] = 'R';
			
			arrDTLHDR[ intTBL_DOMQT] += "Domestic";
			arrDTLHDR[ intTBL_DOMQT] += "Domestic";*/
			
			
			
	        add(new JLabel("Product Type"),2,1,1,2.5,this,'L');
	        
	        add(txtPRDTP=new JTextField(),3,1,1,2.8,this,'L');
	        
	         add(new JLabel("Date"),2,4,1,1,this,'L');
	        
	        add(txtMMYDT=new TxtDate(),3,4,1,2.5,this,'L');
	        
	        add(new JLabel("Marketing Zone"),2,7,1,2.5,this,'L');
	        
	        add(txtMZONE=new JTextField(),3,7,1,2.5,this,'L');
	        add(new JLabel("Total Domestic"),2,10,1,2.5,this,'L');
	        add(txtDOMQT=new JTextField(),3,10,1,2.8,this,'L');
	        
	        add(new JLabel("Total Export"),2,13,1,2.5,this,'L');
	        add(txtEXPQT=new JTextField(),3,13,1,2.8,this,'L');
	        
	        add(new JLabel("Total Deemed Export"),2,16,1,3.5,this,'L');
	        add(txtDEXQT=new JTextField(),3,16,1,3.5,this,'L');

	        tbpMAIN = new JTabbedPane();
	    
	     
	        String L_YYMDT = cl_dat.M_strLOGDT_pbst.substring(8,10).trim() + cl_dat.M_strLOGDT_pbst .substring(3,5).trim();
			int L_YYMM = Integer.parseInt(L_YYMDT);
			String L_MMYDT = cl_dat.M_strLOGDT_pbst .substring(3,5).trim() + cl_dat.M_strLOGDT_pbst.substring(8,10).trim();
			if(chkUPDATA(L_YYMM))//checking whether data for current month is updated in
			{  
							//target management table i.e sys/mrxxtrg/udt/cmt_chp01
				updPRDDAT(L_MMYDT,L_YYMM); //updating product code,package type details in 
							//target management detail table from product master table
			}
	        
	       
	        
	        
	        txtPRDTP.setEnabled( false);
	        txtMMYDT.setEnabled( false);
	        txtMZONE.setEnabled( false);
	        txtDOMQT.setEnabled( false);
	        txtEXPQT.setEnabled( false);
	        txtDEXQT.setEnabled(false);
	        
	        
	        
	        
	        
	        
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX,"Constructor");
	    }
		
		    
		
	}
	void setENBL(boolean L_flgSTAT)
	{
	    super.setEnabled( L_flgSTAT);
	    txtPRDTP.setEnabled( false);
        txtMMYDT.setEnabled( false);
       
        txtMZONE.setEnabled( false);
        txtDOMQT.setEnabled( false);
        txtEXPQT.setEnabled( false);
        txtDEXQT.setEnabled(false);
      
        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
        {
            txtPRDTP.requestFocus() ;
            txtMMYDT.setText(cl_dat.M_strLOGDT_pbst );
		
        }
        

	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
		    strMMYDT = txtMMYDT.getText().toString().trim();
			strMZONE = txtMZONE.getText().toString().trim();
			strCODDS = txtPRDTP.getText().toString().trim();
			strTOTDO = txtDOMQT.getText().toString().trim();
			strTOTDE = txtDEXQT.getText().toString().trim();
			strTOTEX = txtEXPQT.getText().toString().trim();
			strMMYDT = strMMYDT.substring(3,5).toString().trim()+strMMYDT.substring(8).toString().trim();
			
			  if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
				{
				   if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				    setENBL(false);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
					    
							
					
					    txtPRDTP.setText("");
					    txtMZONE.setText("");
					    txtDOMQT.setText("");
					    txtEXPQT.setText("");
					    txtDEXQT.setText("");
					    txtPRDTP.setEnabled(true);
					    txtDOMQT.setEnabled( false);
				        txtEXPQT.setEnabled( false);
				        txtDEXQT.setEnabled(false);
				        
				    }
					
				}
			 
			      
			          if(M_objSOURC==tblMKTTBL)
					  {
					      if(tblMKTTBL.isEditing())
							 {
									((JTextField)tblMKTTBL.cmpEDITR[intTBL_PRDDS]).setEditable(false);
									((JTextField)tblMKTTBL.cmpEDITR[intTBL_PKGTP]).setEditable(false);
									
							 }
				       }
					 
			  
			  
			   if(M_objSOURC== txtMMYDT)
		        {
			       if(M_fmtLCDAT.parse(txtMMYDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>=0)
					{
		                setMSG("Enter marketing zone or press F1 key for help.",'N');
			             txtMZONE.setEnabled(true);
			             txtMZONE.requestFocus();
						
					}	    
	                if(M_fmtLCDAT.parse(txtMMYDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
	    			{
	    				setMSG("Date Should Not Be Grater Than Today..",'E');
	    				txtMMYDT.requestFocus();
	    			
	    			}
					
		        }
			  
				  if(M_objSOURC==txtMZONE)
				  {
					M_strSQLQRY = "Select * from co_cdtrn where cmt_cgmtp='SYS' and";
	                M_strSQLQRY += " cmt_cgstp='MR00ZON' and cmt_codds='"+strMZONE+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						strCHP01 = nvlSTRVL(M_rstRSSET.getString("cmt_chp01"),"");
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						strTOTDO = "0.000";
						strTOTEX = "0.000";
						strTOTDE = "0.000";
						LM_CNT = 0;
						hstPRKEY.clear();
						hstTRGQT.clear();
						vtrSPECL.clear();
						vtrPRIME.clear();
						vtrNPRIM.clear();
						vtrOFGRD.clear();
						vtrSCRAP.clear();
						
						getTMNREC();
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}else{
						setMSG("Enter valid zone or press F1 key for help",'E');
						txtMZONE.requestFocus();
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();
					 
					 
	  			}
				 if(M_objSOURC== txtPRDTP)
				 {
					M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='MST'";
					M_strSQLQRY += "and CMT_CGSTP = 'COXXPRD' and cmt_codds='"+strCODDS+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						setMSG("Valid product type.",'N');
						strPRDTP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
						txtMMYDT.setEnabled(true);
						txtMMYDT.requestFocus();
					}else
					{
						setMSG("Please enter valid product type or press F1 key for help",'E');
						txtPRDTP.requestFocus();
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();
				}
				
		      
		        
		
			
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is action Performed");
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed( L_KE);
	   
		
	  try
	  {
	        strMMYDT = txtMMYDT.getText().toString().trim();
			strMZONE = txtMZONE.getText().toString().trim();
			strCODDS = txtPRDTP.getText().toString().trim();
			strMMYDT = strMMYDT.substring(3,5).toString().trim()+strMMYDT.substring(8).toString().trim();
			
			
			
	  
	   if(L_KE.getKeyCode()  == 106)
	    {
	        if(flgTBLFL)
	            clrMKTTBL();
	        txtDOMQT.setText("");
			txtEXPQT.setText("");
			txtDEXQT.setText("");
			txtMZONE.setText("");
			txtPRDTP.setText("");
			txtPRDTP.requestFocus();
			
	    }
	     
		   
		
	    if(L_KE.getKeyCode() == L_KE.VK_F1)
	    {
	       
	        if(M_objSOURC== txtPRDTP)
	        {
				
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST'";
				M_strSQLQRY += "and CMT_CGSTP = 'COXXPRD'";
				cl_dat.M_flgHELPFL_pbst  = true;
				M_strHLPFLD = "txtPRDTP";
				cl_hlp(M_strSQLQRY,1,2,new String[]{"Product Type","Product Description"} ,2,"CT");
			}
	        if(M_objSOURC== txtMZONE)
	        {
                M_strSQLQRY="Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MR00ZON' and CMT_CODCD <= '11'";
                cl_dat.M_flgHELPFL_pbst  = true;
				M_strHLPFLD = "txtMZONE";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Marketing Zone"},1,"CT");
	        }
	        if(M_objSOURC ==txtGRADE)
	        {
	            M_strSQLQRY = "Select PR_PRDDS from CO_PRMST where pr_prdtp='"+strPRDTP+"' order by pr_prdds";
				cl_dat.M_flgHELPFL_pbst  = true;
				M_strHLPFLD = "txtGRADE";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade"} ,1,"CT");
				
	        }
	        if(M_objSOURC==txtPKGTP)
	        {
	            M_strSQLQRY = "Select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='SYS'";
				M_strSQLQRY += " and cmt_cgstp='FGXXPKG'";
				cl_dat.M_flgHELPFL_pbst  = true;
				M_strHLPFLD="txtPKGTP";
				cl_hlp(M_strSQLQRY,1,2,new String[]{"Package Type","Description"},2,"CT");
	        }
	        
	        
	    }
	   
	  }catch(Exception L_EX)
	  {
	      setMSG(L_EX,"This is KeyPressed");
	      
	  }
	}
	void exeHLPOK()
	{
	    super.exeHLPOK() ;
		try
		{
		    cl_dat.M_flgHELPFL_pbst = true;
		    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		    if(M_strHLPFLD == "txtMZONE")
			{
		        
		       txtMZONE.setText(cl_dat.M_strHLPSTR_pbst);
		        
			}
		    if(M_strHLPFLD == "txtPRDTP")
			{
		        
		        String L_strPRDTP =  L_STRTKN.nextToken();
	     		txtPRDTP.setText(L_STRTKN.nextToken());
		        
			}
		    if(M_strHLPFLD== "txtGRADE")
			{
		       
		        String L_strGRADE =  L_STRTKN.nextToken();
		        
	     		txtGRADE.setText(L_strGRADE);
	     	}
		    if(M_strHLPFLD == "txtPKGTP")
		    {
		        txtPKGTP.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
		        
		    }
		
		    
		    
		    
		
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK")  ; 
		}	
	}
	private void clrMKTTBL()
	{
		try
		{
			for(int i = 0;i <tblMKTTBL.getRowCount();i++)
			{
				tblMKTTBL.setValueAt(new Boolean(false),i,intTBL_CHKFL);
				for(int j = 0;j < tblMKTTBL.getColumnCount();j++){
					tblMKTTBL.setValueAt("",i,j);
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"clrMKTTBL");
		}
	}
	
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			super.mouseClicked(L_ME);
			 if(tblMKTTBL.isEditing())
			 {
					((JTextField)tblMKTTBL.cmpEDITR[intTBL_PRDDS]).setEditable(false);
					((JTextField)tblMKTTBL.cmpEDITR[intTBL_PKGTP]).setEditable(false);
					
			 }
			if(M_objSOURC==tblMKTTBL)
			{
				tblMKTTBL.editCellAt(tblMKTTBL.getSelectedRow(),tblMKTTBL.getSelectedColumn());
				if(tblMKTTBL.getSelectedColumn() == intTBL_PRDDS)
				{
					if(tblMKTTBL.getValueAt(tblMKTTBL.getSelectedRow(),intTBL_CHKFL).toString().trim().equals("true"))
						tblMKTTBL.setValueAt(new Boolean(false),tblMKTTBL.getSelectedRow(),intTBL_CHKFL);
					else if(tblMKTTBL.getValueAt(tblMKTTBL.getSelectedRow(),intTBL_CHKFL).toString().trim().equals("false"))
						tblMKTTBL.setValueAt(new Boolean(true),tblMKTTBL.getSelectedRow(),intTBL_CHKFL);
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"mouseClicked");
		}
	}
	private void getCHKQT()
	{
		try
		{
		   
			double L_DOMQT = 0;
			double L_EXPQT = 0;
			double L_DEXQT = 0;
			strTOTDO="0.00";
			strTOTDE="0.00";
			
			
		
			for(int i = 0;i < tblMKTTBL.getRowCount();i++)
			{
				if(tblMKTTBL.getValueAt(i,intTBL_CHKFL).toString().trim().equals("true"))
				{
					strEXPQT = tblMKTTBL.getValueAt(i,intTBL_EXPQT).toString().trim();
					
					if(!strMZONE.equalsIgnoreCase("OVERALL"))
					{
					
						
					    strDOMQT = tblMKTTBL.getValueAt(i,intTBL_DOMQT).toString().trim();
					
						strDEXQT = tblMKTTBL.getValueAt(i,intTBL_DEXQT).toString().trim();
					
						if(strDOMQT.length() == 0)
						    strDOMQT = "0.000";
					
						if(strDEXQT.length() == 0)
							strDEXQT = "0.000";
					
						L_DOMQT += Double.parseDouble(strDOMQT);
					
						L_DEXQT += Double.parseDouble(strDEXQT);
					}
					if(strEXPQT.length() == 0)
						strEXPQT = "0.000";
					L_EXPQT += Double.parseDouble(strEXPQT);
					
				}
			}
			if(!strMZONE.equalsIgnoreCase("OVERALL"))
			{
				L_DOMQT += Double.parseDouble(strTOTDO);
				
				L_DEXQT += Double.parseDouble(strTOTDE);
				
				txtDOMQT.setText(setNumberFormat(L_DOMQT,3));
				txtDEXQT.setText(setNumberFormat(L_DEXQT,3));
			}
			else
			{
				L_EXPQT += Double.parseDouble(strTOTEX);
				
				txtEXPQT.setText(setNumberFormat(L_EXPQT,3));
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"getCHKQT");
		}
	}
	private void getTMNREC()
	{
	    
		
		try
		{
		    strMZONE = txtMZONE.getText().toString().trim();
		    setMSG("Fetching grade from target management table.",'N');
			double L_TOTDO = 0;
			double L_TOTDE = 0;
			double L_TOTEX = 0;
			if(txtMZONE.getText() .toString().trim().equalsIgnoreCase("OVERALL"))
			{
				intTBL_PRTQT = 4;
                M_strSQLQRY = "select tm_stsfl,tm_prdcd,tm_pkgtp,tm_prdds,";
				M_strSQLQRY += "tm_"+strCHP01+"ext l_expqt,tm_prtqt l_prtqt";
                M_strSQLQRY += " from mr_tmtrn where tm_mmydt='"+strMMYDT +"'";
				M_strSQLQRY += " and tm_prdtp = '"+strPRDTP+"' order by tm_prdcd,tm_pkgtp";
			
				LM_PRPSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY);
				
				M_rstRSSET = LM_PRPSTM.executeQuery();
			
				while(M_rstRSSET.next())
				{
                    intSTSFL = M_rstRSSET.getInt("tm_stsfl");
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("tm_prdds"),"");
					strPRDCD = nvlSTRVL(M_rstRSSET.getString("tm_prdcd"),"");
					strPKGTP = nvlSTRVL(M_rstRSSET.getString("tm_pkgtp"),"");
					strEXPQT =  (nvlSTRVL1(M_rstRSSET.getString("l_expqt")," "));
					
					strPRTQT = (nvlSTRVL1(M_rstRSSET.getString("l_prtqt")," "));
					
                    strHSTVAL = strPRDCD+"|"+strPKGTP+"|"+String.valueOf(intSTSFL);
					strPKGDS = cl_dat.getPRMCOD("CMT_CODDS","SYS","FGXXPKG",strPKGTP);
					hstPRKEY.put(strPRDDS+strPKGDS,strHSTVAL);
					hstTRGQT.put(strHSTVAL,strPRDDS+"|"+strPKGDS+"|"+(strEXPQT)+"|"+(strPRTQT));
                    switch(intSTSFL)
                    {
						case 0:
							vtrUNIDT.addElement(strHSTVAL);
							break;
						case 1:
							vtrSPECL.addElement(strHSTVAL);
							break;
						case 2:
							vtrPRIME.addElement(strHSTVAL);
							break;
						case 3:
							vtrNPRIM.addElement(strHSTVAL);
							break;
						case 4:
							vtrOFGRD.addElement(strHSTVAL);
							break;
						case 9:
							vtrSCRAP.addElement(strHSTVAL);
							break;
					}
					LM_CNT++;
					L_TOTEX += Double.parseDouble(setNumberFormat(Double.parseDouble(nvlSTRVL(strEXPQT,"0.000")),3));
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				LM_PRPSTM.close();
			}
			else
			{
				intTBL_PRTQT = 5;
                M_strSQLQRY = "select tm_stsfl,tm_prdcd,tm_pkgtp,tm_prdds,tm_"+strCHP01+"dot l_domqt,";
				M_strSQLQRY += "tm_"+strCHP01+"det l_dexqt,tm_prtqt l_prtqt from mr_tmtrn";
                M_strSQLQRY += " where tm_prdtp = '"+strPRDTP+"' and tm_mmydt='"+strMMYDT+"'";
                M_strSQLQRY += " order by tm_prdcd,tm_pkgtp";
              
				LM_PRPSTM = cl_dat.M_conSPDBA_pbst.prepareStatement(M_strSQLQRY);
			    M_rstRSSET = LM_PRPSTM.executeQuery();
               while(M_rstRSSET.next())
				{
                    intSTSFL = M_rstRSSET.getInt("tm_stsfl");
                    strPRDDS = nvlSTRVL(M_rstRSSET.getString("tm_prdds"),"");
					strPRDCD = nvlSTRVL(M_rstRSSET.getString("tm_prdcd"),"");
					strPKGTP = nvlSTRVL(M_rstRSSET.getString("tm_pkgtp"),"");
					strDOMQT =(nvlSTRVL1(M_rstRSSET.getString("l_domqt")," "));
				
					strDEXQT = nvlSTRVL1(M_rstRSSET.getString("l_dexqt")," ");
				
					strPRTQT = nvlSTRVL1(M_rstRSSET.getString("l_prtqt")," ");
				
                    strHSTVAL = strPRDCD+"|"+strPKGTP+"|"+String.valueOf(intSTSFL);
					strPKGDS = cl_dat.getPRMCOD("CMT_CODDS","SYS","FGXXPKG",strPKGTP);
					hstPRKEY.put(strPRDDS+strPKGDS,strHSTVAL);
					hstTRGQT.put(strHSTVAL,strPRDDS+"|"+strPKGDS+"|"+strDOMQT+"|"+strDEXQT+"|"+(strPRTQT));
					switch(intSTSFL)
					{
						case 0:
							vtrUNIDT.addElement(strHSTVAL);
							break;
						case 1:
							vtrSPECL.addElement(strHSTVAL);
							break;
						case 2:
							vtrPRIME.addElement(strHSTVAL);
							
							break;
						case 3:
							vtrNPRIM.addElement(strHSTVAL);
							break;
						case 4:
							vtrOFGRD.addElement(strHSTVAL);
							break;
						case 9:
						    vtrSCRAP.addElement(strHSTVAL);
							break;
					}
					LM_CNT++;
					L_TOTDO += Double.parseDouble(setNumberFormat(Double.parseDouble(nvlSTRVL(strDOMQT,"0.000")),3));
					L_TOTDE += Double.parseDouble(setNumberFormat(Double.parseDouble(nvlSTRVL(strDEXQT,"0.000")),3));
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				LM_PRPSTM.close();
			}
			
			txtDOMQT.setText(setNumberFormat(Double.parseDouble(nvlSTRVL(String.valueOf(L_TOTDO),"0.000")),3));
			txtEXPQT.setText(setNumberFormat(Double.parseDouble(nvlSTRVL(String.valueOf(L_TOTEX),"0.000")),3));
			txtDEXQT.setText(setNumberFormat(Double.parseDouble(nvlSTRVL(String.valueOf(L_TOTDE),"0.000")),3));
			 
			
			crtMKTTBL(LM_CNT);
			
			
			getTBLDATA();
			
			
			/*setMSG("Fetching total domestic,export & deemed export target qty.",'N');
			LM_STRQRY = "select sum(tm_ezdot+tm_wzdot+tm_nzdot+tm_szdot+tm_czdot) l_domqt,";
			LM_STRQRY += "sum(tm_exext) l_expqt,";
			LM_STRQRY += "sum(tm_ezdet+tm_wzdet+tm_nzdet+tm_szdet+tm_czdet) l_dexqt from mr_tmtrn";
			LM_STRQRY += " where tm_mmydt='"+strMMYDT+"' and tm_prdtp = '"+strPRDTP+"'";
			LM_PRPSTM = cl_dat.conSPDBA.prepareStatement(LM_STRQRY);
			LM_RSLSET = LM_PRPSTM.executeQuery();
			if(LM_RSLSET.next()){
				strTOTDO = setFMT(nvlSTRVL(LM_RSLSET.getString("l_domqt"),"0.000"),3);
				strTOTEX = setFMT(nvlSTRVL(LM_RSLSET.getString("l_expqt"),"0.000"),3);
				strTOTDE = setFMT(nvlSTRVL(LM_RSLSET.getString("l_dexqt"),"0.000"),3);
				txtDOMQT.setText(strTOTDO);
				txtEXPQT.setText(strTOTEX);
				txtDEXQT.setText(strTOTDE);
			}
			if(LM_RSLSET != null)
				LM_RSLSET.close();
			LM_PRPSTM.close();*/
			setMSG("Data fetched.",'N');
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"getTMNREC");
		}
		
	}
	private void crtMKTTBL(int intCNT)
	{
		try
		{
		    
			cl_dat.M_flgCHKTB_pbst  = true;
			if(!exeTBLREFSH(LM_CNT))
				return;  
			
			if((txtMZONE.getText().toString() .trim().equalsIgnoreCase("OVERALL")))
			{
			   
			   
			   
			    String L_TBLHDR[] = {"Status","Grades","Package Type","Target Qty.","Prd. Target"};
				int L_COLSZ[] = {50,150,150,150,170};
				tblMKTTBL =crtTBLPNL1(pnlMKK=new JPanel(null),L_TBLHDR,intCNT,1,1,11,18.5,L_COLSZ,new int[]{0});
				tbpMAIN.addTab("Marketing Target Management",pnlMKK);
				add(tbpMAIN,5,1,13,19,this,'L');   
				
				tclEXPQT = tblMKTTBL.getColumn(tblMKTTBL.getColumnName(intTBL_EXPQT));    
				tclPRTQT = tblMKTTBL.getColumn(tblMKTTBL.getColumnName(intTBL_PRTQT));
				tclEXPQT.setCellRenderer(new RowRenderer());
				tclPRTQT.setCellRenderer(new RowRenderer());
				updateUI();
				 
				
		 }
			
			else
			{
			   
			  
			    
			   
			    String L_TBLHDR[] = {"Status","Grades","Package Type","Domestic","Deemed Export","Prd. Target"};
				int L_COLSZ[] = {50,150,120,120,130,130};
				tblMKTTBL = crtTBLPNL1(pnlMKK=new JPanel(null),L_TBLHDR,intCNT,1,1,11,18.5,L_COLSZ,new int[]{0});
				
				tbpMAIN.addTab("Marketing Target Management",pnlMKK);
				add(tbpMAIN,5,1,13,19,this,'L');
				
				tclDOMQT = tblMKTTBL.getColumn(tblMKTTBL.getColumnName(intTBL_DOMQT));    
				tclDEXQT = tblMKTTBL.getColumn(tblMKTTBL.getColumnName(intTBL_DEXQT));    
				tclPRTQT = tblMKTTBL.getColumn(tblMKTTBL.getColumnName(intTBL_PRTQT));
				tclDOMQT.setCellRenderer(new RowRenderer());
				tclDEXQT.setCellRenderer(new RowRenderer());
				tclPRTQT.setCellRenderer(new RowRenderer());
				updateUI();
				
				
				
				
			}
			/*LM_MKTTBL.getColumn(LM_MKTTBL.getColumnName(intTBL_PRDDS)).setCellEditor(new DefaultCellEditor(txtPRDDS));
			LM_MKTTBL.getColumn(LM_MKTTBL.getColumnName(intTBL_PKGTP)).setCellEditor(new DefaultCellEditor(txtPKGTP));
			txtPRDDS.setEnabled(true);
			txtPKGTP.setEnabled(true);
			txtPRDDS.addKeyListener(this);
			txtPKGTP.addKeyListener(this);*/
			
			tblMKTTBL.setCellEditor(intTBL_PRDDS,txtGRADE=new JTextField());
	        tblMKTTBL.setCellEditor(intTBL_PKGTP,txtPKGTP=new JTextField());
	        tblMKTTBL.setInputVerifier(new TBLINPVF());
			txtGRADE.addKeyListener( this);
			txtPKGTP.addKeyListener( this);
			
			
			tblMKTTBL.addKeyListener(this);
			tblMKTTBL.addMouseListener(this);
		
			
			//LM_MKTTBL.editCellAt(0,intTBL_EXPQT);
			tblMKTTBL.setColumnSelectionInterval(intTBL_EXPQT,intTBL_EXPQT);
			tblMKTTBL.setRowSelectionInterval(intTBL_CHKFL,intTBL_CHKFL);
			flgTBLFL=true;
			
			
			
			
		}catch(Exception L_EX){
			setMSG(L_EX,"crtMKTTBL");
		}
	}

	/**Dynamic Recreation of display table
	 * According to content/values applicable at the time of execution
	 */
	private boolean exeTBLREFSH( int intCNT)
	{
		try
		{
				
		   
			
			
				if((txtMZONE.getText().toString() .trim().equalsIgnoreCase("OVERALL")))
				{
				    tbpMAIN.remove(pnlMKK);
			     //   pnlMKK.removeAll();
			        pnlMKK = new JPanel(null);
			
				         	
						String L_TBLHDR[] = {"Status","Grades","Package Type","Target Qty.","Prd. Target"};
						int L_COLSZ[] = {50,150,150,150,170};
						tblMKTTBL =crtTBLPNL1(pnlMKK,L_TBLHDR,intCNT,1,1,11,18.5,L_COLSZ,new int[]{0});
			
						add(tbpMAIN,5,1,13,19,this,'L');
						tclEXPQT.setCellRenderer(new RowRenderer());
						tclPRTQT.setCellRenderer(new RowRenderer());
				}
				else
				{ tbpMAIN.remove(pnlMKK);
		     //   pnlMKK.removeAll();
		        pnlMKK = new JPanel(null);
					
				    String L_TBLHDR[] = {"Status","Grades","Package Type","Domestic","Deemed Export","Prd. Target"};
					int L_COLSZ[] = {50,150,120,120,130,130};
					tblMKTTBL = crtTBLPNL1(pnlMKK,L_TBLHDR,intCNT,1,1,11,18.5,L_COLSZ,new int[]{0});
					add(tbpMAIN,5,1,13,19,this,'L');
					
					tclDOMQT.setCellRenderer(new RowRenderer());
					tclDEXQT.setCellRenderer(new RowRenderer());
					tclPRTQT.setCellRenderer(new RowRenderer());
				}
				
			
				
			//System.out.println("005");
			tblMKTTBL.addMouseListener(this);
			
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeREFSH");}
		return true;
	}
	

	class RowRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(cl_JTable table,Object value,boolean isselected,boolean hasFocus,int row,int col)
		{
			int cellValue = (value instanceof Number) ? ((Number)value).intValue() : 0;
			setText((value == null) ? "" : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);
			return super.getTableCellRendererComponent(table,value,isselected,hasFocus,row,col);
		}
	}
	
	
	
	private void getTBLDATA()
	{
	    
	    
		try
		{
			intPRMCNT = vtrPRIME.size();
			
			intNPRCNT = vtrNPRIM.size();
			
			intOFGCNT = vtrOFGRD.size();
			
			intSCRCNT = vtrSCRAP.size();
			
			String L_STRVAL = "";
			int L_CNT = 0;
			for(int i = 0;i < intPRMCNT;i++)
			{
				strADDSTR = "PRIME";
				strHSTVAL = vtrPRIME.get(i).toString().trim();
			
				
				
				getCELDTA(strHSTVAL,L_CNT);
				
				L_CNT++;
			}
			for(int i = 0;i < intNPRCNT;i++)
			{
				strADDSTR = "NON-PRIME";
				strHSTVAL = vtrNPRIM.get(i).toString().trim();
				getCELDTA(strHSTVAL,L_CNT);
				
				
				
				L_CNT++;
			}
			for(int i = 0;i < intOFGCNT;i++)
			{
				strADDSTR = "OFF GRADE";
				strHSTVAL = vtrOFGRD.get(i).toString().trim();
				getCELDTA(strHSTVAL,L_CNT);
				
				
				
				L_CNT++;
			}
			for(int i = 0;i < intSCRCNT;i++)
			{
				strADDSTR = "SCRAP";
				strHSTVAL = vtrSCRAP.get(i).toString().trim();
				
				
				
				getCELDTA(strHSTVAL,L_CNT);
				
				L_CNT++;
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"getTBLDATA");
		}
		
	}
	private void getCELDTA(String LP_KEYVL, int LP_ROWCNT)
	{
	    
	   
		try
		{   
		
		    tblMKTTBL.setValueAt(new Boolean(false),LP_ROWCNT,intTBL_CHKFL);
			
			StringTokenizer stkPRIME = new StringTokenizer(hstTRGQT.get(LP_KEYVL).toString(),"|");
			if(stkPRIME.hasMoreTokens())
			{
				for(int j = intTBL_PRDDS ;j <= intTBL_PRTQT;j++)
				{
				   
				   
				    
				    tblMKTTBL.setValueAt(stkPRIME.nextToken(),LP_ROWCNT,j);
				    
				     
				    
				}
				
			}
			
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"getCELDTA");
		}
	}
	/**
	 * This method is used to replace 0.000 qty as " "
	 */
	private String nvlSTRVL1(String LP_VARVL, String LP_DEFVL)
	{
		try
		{
			if(LP_VARVL != null)
			{
				 if(LP_VARVL.equals("0.000"))
                 	 LP_VARVL = LP_DEFVL ;
				 else
					 LP_VARVL = LP_VARVL;
            }else
				 LP_VARVL = LP_DEFVL;
			
        }catch (Exception L_EX)
        {
			setMSG(L_EX,"nvlSTRVL1");
		}
        return (LP_VARVL);
        
	}

	private boolean vldGRADE()
	{
	    strCELVAL = tblMKTTBL.getValueAt(tblMKTTBL.getSelectedRow(),tblMKTTBL.getSelectedColumn()).toString().trim();
	    try
		{
	        M_strSQLQRY = "Select * from co_prmst where pr_prdtp='"+strPRDTP+"'";
		 M_strSQLQRY += " and pr_prdds = '"+strCELVAL+"'";
		 System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
				return true;
			M_rstRSSET.close();
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"vldLOTNO");
		}
		return false;
	}
	private boolean vldPKGTP()
	{
	    try
		{
	        strCELVAL =tblMKTTBL.getValueAt(tblMKTTBL.getSelectedRow(),tblMKTTBL.getSelectedColumn()).toString().trim();
	        M_strSQLQRY = "Select * from co_cdtrn where cmt_cgmtp='SYS'";
		    M_strSQLQRY += " and cmt_cgstp='FGXXPKG' and cmt_codds='"+strCELVAL+"'";
	
	
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET.next())
			return true;
		M_rstRSSET.close();
	}catch(Exception L_EX)
	{
		setMSG(L_EX,"vldLOTNO");
	}
	return false;
}
	    
	
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
			    strMMYDT = txtMMYDT.getText().toString().trim();
				strMZONE = txtMZONE.getText().toString().trim();
				strCODDS = txtPRDTP.getText().toString().trim();
				strMMYDT = strMMYDT.substring(3,5).toString().trim()+strMMYDT.substring(8).toString().trim();
				
			 
				if(getSource()==tblMKTTBL)
				{
				    
				   
				    if(tblMKTTBL.isEditing())
					 {
							((JTextField)tblMKTTBL.cmpEDITR[intTBL_PRDDS]).setEditable(false);
							((JTextField)tblMKTTBL.cmpEDITR[intTBL_PKGTP]).setEditable(false);
							
					 }
				
				    if(P_intCOLID == intTBL_PRDDS)
				    {
						
						
						if(vldGRADE())
						{
							setMSG("Press F1 for help on package type",'N');
							
							tblMKTTBL.setRowSelectionInterval(tblMKTTBL.getSelectedRow(),tblMKTTBL.getSelectedRow());
							tblMKTTBL.setColumnSelectionInterval(tblMKTTBL.getSelectedColumn(),intTBL_PRTQT);
						}else
						{
							setMSG("Please enter valid grade",'E');
							tblMKTTBL.setColumnSelectionInterval(intTBL_PKGTP,intTBL_PRDDS);
						}
						if(M_rstRSSET != null)
							M_rstRSSET.close();
					}
					 if(P_intCOLID == intTBL_PKGTP)
					{
						
						
						if(vldPKGTP())
						{
							setMSG("Enter Target Qty.",'N');
							tblMKTTBL.setColumnSelectionInterval(tblMKTTBL.getSelectedColumn(),tblMKTTBL.getSelectedColumn()+1);
						}else
						{
							setMSG("Please enter valid package description.",'E');
							tblMKTTBL.setColumnSelectionInterval(tblMKTTBL.getSelectedColumn(),tblMKTTBL.getSelectedColumn());
							tblMKTTBL.setColumnSelectionInterval(intTBL_PKGTP,intTBL_PKGTP);
						}
						
					}
				    if(P_intCOLID == intTBL_DOMQT || P_intCOLID == intTBL_EXPQT || P_intCOLID== intTBL_DEXQT)
					{
							
							tblMKTTBL.editCellAt(tblMKTTBL.getSelectedRow(),tblMKTTBL.getSelectedColumn());
						    tblMKTTBL.setValueAt(new Boolean(true),tblMKTTBL.getSelectedRow(),intTBL_CHKFL);
							tblMKTTBL.setRowSelectionInterval(tblMKTTBL.getSelectedRow(),tblMKTTBL.getSelectedRow());
							tblMKTTBL.setColumnSelectionInterval(tblMKTTBL.getSelectedColumn(),tblMKTTBL.getSelectedColumn()+1);
						
	
					}
					 if(P_intCOLID == intTBL_PRTQT)
					 {
						     tblMKTTBL.editCellAt(tblMKTTBL.getSelectedRow(),tblMKTTBL.getSelectedColumn());
						     tblMKTTBL.setRowSelectionInterval(tblMKTTBL.getSelectedRow() ,tblMKTTBL.getRowCount());
						     tblMKTTBL.setColumnSelectionInterval(intTBL_EXPQT,intTBL_PRTQT);
							 tblMKTTBL.setValueAt(new Boolean(true),tblMKTTBL.getSelectedRow(),intTBL_CHKFL);
 					   }
					
						 getCHKQT();	
				}
		       
			
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"Table InPUT VERFIFER");
			}
			return true;
		}	
		
	}
	void exeSAVE()
	{
	    try
	    {
	        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
            {
                setCursor(cl_dat.M_curWTSTS_pbst);
                exeAUTADD();
                setCursor(cl_dat.M_curDFSTS_pbst);
            }
	        
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX,"This is save");
	    }
	    
	}
	private void exeAUTADD()
	{
	    try
		{
			    strMMYDT = txtMMYDT.getText().toString().trim();
				strMZONE = txtMZONE.getText().toString().trim();
				strCODDS = txtPRDTP.getText().toString().trim();
				strTOTDO = txtDOMQT.getText().toString().trim();
				strTOTDE = txtDEXQT.getText().toString().trim();
				strTOTEX = txtEXPQT.getText().toString().trim();
				strMMYDT = strMMYDT.substring(3,5).toString().trim()+strMMYDT.substring(8).toString().trim();
				
	
				this.setCursor(cl_dat.M_curWTSTS_pbst );
				hstDBSVL.clear();
				int L_HSTCNT = 0;
				setMSG("Updating data of marketing target detail table",'N');
				LM_PRPSTM = cl_dat.M_conSPDBA_pbst .prepareStatement("Select * from mr_tmtrn where tm_mmydt = ? and tm_prdtp = ? and tm_prdcd = ? and tm_pkgtp = ?",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet L_RSLSET = null;
				StringTokenizer stkPRIME;
				String L_STRVAL = "";
				loop : for(int i =0; i < tblMKTTBL.getRowCount();i++)
				{
							if(tblMKTTBL.getValueAt(i,intTBL_CHKFL).toString().trim().equals("true"))
							{
								strPRDDS = tblMKTTBL.getValueAt(i,intTBL_PRDDS).toString().trim();
	
								strPKGDS = tblMKTTBL.getValueAt(i,intTBL_PKGTP).toString().trim();
	
								strPRTQT = nvlSTRVL(tblMKTTBL.getValueAt(i,intTBL_PRTQT).toString().trim(),"0.000");
	
								if(!strMZONE.equalsIgnoreCase("OVERALL"))
								{
									strDOMQT = nvlSTRVL(tblMKTTBL.getValueAt(i,intTBL_DOMQT).toString().trim(),"0.000");
	
									strDEXQT = nvlSTRVL(tblMKTTBL.getValueAt(i,intTBL_DEXQT).toString().trim(),"0.000");
	
								}else
									strEXPQT = nvlSTRVL(tblMKTTBL.getValueAt(i,intTBL_EXPQT).toString().trim(),"0.000");
	
								strPRDCD = "";
								strPKGTP = "";
                                strSTATS = "";
								L_STRVAL = hstPRKEY.get(strPRDDS+strPKGDS).toString().trim();
								stkPRIME = new StringTokenizer(L_STRVAL,"|");
								if(stkPRIME.hasMoreTokens())
									strPRDCD = stkPRIME.nextToken().toString().trim();
								if(stkPRIME.hasMoreTokens())
									strPKGTP = stkPRIME.nextToken().toString().trim();
								if(stkPRIME.hasMoreTokens())
									strSTATS = stkPRIME.nextToken().toString().trim();
								strHSTVAL = strPRDCD+"|"+strPKGTP;
								LM_PRPSTM.setString(1,strMMYDT);
								LM_PRPSTM.setString(2,strPRDTP);
								LM_PRPSTM.setString(3,strPRDCD);
								LM_PRPSTM.setString(4,strPKGTP);
								M_rstRSSET = LM_PRPSTM.executeQuery();
								try
								{
									if(M_rstRSSET.next())
									{
										updTMTRN(M_rstRSSET);
										M_rstRSSET.updateRow();
									}else
									{
										M_rstRSSET.moveToInsertRow();
										M_rstRSSET.updateString("TM_MMYDT",strMMYDT);
										M_rstRSSET.updateString("TM_PRDTP",strPRDTP);
										if(strPRDCD.length() == 0)
											strPRDCD = getPRDCD(strPRDTP,strPRDDS);
										if(strPKGTP.length() == 0)
											strPKGTP = getPKGTP(strPKGDS);
										M_rstRSSET.updateString("TM_PRDCD",strPRDCD);
										M_rstRSSET.updateString("TM_PKGTP",strPKGTP);
										updTMTRN(L_RSLSET);
										M_rstRSSET.insertRow();
									}
									LM_PRPSTM.clearParameters();
									cl_dat.M_conSPDBA_pbst .commit();
									if(strMZONE.equalsIgnoreCase("OVERALL"))
										hstDBSVL.put(String.valueOf(L_HSTCNT),strPRDDS+"|"+strPKGDS+"|"+strEXPQT+"|"+strPRTQT);
									else
										hstDBSVL.put(String.valueOf(L_HSTCNT),strPRDDS+"|"+strPKGDS+"|"+strDOMQT+"|"+strDEXQT+"|"+strPRTQT);
									L_HSTCNT++;
							}catch(Exception L_EX){
								setMSG(L_EX,"Updatable resulset exception1");
								cl_dat.M_conSPDBA_pbst .rollback();
								L_RSLSET.close();
								LM_PRPSTM.close();
								setMSG("Data not updated successfully",'E');
								break loop;
							}
								
						}
					}
				M_rstRSSET.close();
				LM_PRPSTM.close();	   
				setMSG("Data updated successfully",'N');
				crtMKTTBL(L_HSTCNT);
				for(int i = 0;i < L_HSTCNT;i++)
				{
					tblMKTTBL.setValueAt(new Boolean(true),i,intTBL_CHKFL);
					stkPRIME = new StringTokenizer(hstDBSVL.get(String.valueOf(i)).toString().trim(),"|");
					if(stkPRIME.hasMoreTokens())
					{
						for(int j = intTBL_PRDDS;j <= intTBL_PRTQT;j++)
						{
						    System.out.println(intTBL_PRDDS);
							tblMKTTBL.setValueAt(stkPRIME.nextToken().toString().trim(),i,j);
						}
					}
				}
				getCHKQT();
				this.setCursor(cl_dat.M_curDFSTS_pbst);
	}catch(Exception L_EX)
	{
	 setMSG(L_EX,"this is exeupd");   
	}
}

	
	private boolean chkUPDATA(int LP_YYMM)
	{
		try
		{
			ResultSet L_RSLSET = null;
			LM_PRPSTM = cl_dat.M_conSPDBA_pbst .prepareStatement("Select cmt_chp01 from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXTRG' and cmt_codcd='UDT'");
			L_RSLSET = LM_PRPSTM.executeQuery();
			if(L_RSLSET.next())
			{
				if(L_RSLSET.getInt(1) < LP_YYMM)
					return true;
			}
			L_RSLSET.close();
			LM_PRPSTM.close();
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"chkUPDATA");
		}
		return false;
	}
	
	private void updPRDDAT(String LP_MMYDT,int LP_YYMM)
	{
		try{
			M_strSQLQRY = "insert into mr_tmtrn select distinct '"+LP_MMYDT+"',pr_prdcd,pr_prdds,";
			M_strSQLQRY += "'01',0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,";
			M_strSQLQRY += "0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,";
			M_strSQLQRY += "0.000,0.000,0.000,0.000,0.000,0.000,0.000,'SYS',current date,";
			M_strSQLQRY += "pr_prdtp,0.000,0.000,pr_stsfl,0.000,0.000,0.000,0.000,0.000,";
			M_strSQLQRY += "0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,";
			M_strSQLQRY += "0.000,0.000,0.000,0.000,0.000,0.000,0.000 from co_prmst where pr_stsfl != 'X'";
	
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst )
			{
				cl_dat.exeDBCMT("");
				LM_PRPSTM.close();
				LM_PRPSTM = cl_dat.M_conSPDBA_pbst .prepareStatement("Update co_cdtrn set cmt_chp01='"+LP_YYMM+"' where cmt_cgmtp='SYS' and cmt_cgstp='MRXXTRG' and cmt_codcd='UDT'");
				LM_PRPSTM.executeUpdate();
				cl_dat.M_conSPDBA_pbst .commit();
				LM_PRPSTM.close();
			}
			else
			{
			    
			}
				
		}catch(Exception L_EX){
			setMSG(L_EX,"updPRDDAT");
		}
	}
	
	private String getPRDCD(String LP_PRDTP,String LP_PRDDS)
	{
		String L_PRDCD = "";
		try{
            M_strSQLQRY = "Select pr_prdcd,pr_stsfl from co_prmst where pr_prdtp='"+LP_PRDTP+"'";
			M_strSQLQRY += " and pr_prdds='"+LP_PRDDS+"'";
			LM_PRPSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY);
	
			M_rstRSSET = LM_PRPSTM.executeQuery();
            if(M_rstRSSET.next())
            {
				L_PRDCD = nvlSTRVL(M_rstRSSET.getString("pr_prdcd"),"");
                strSTATS = nvlSTRVL(M_rstRSSET.getString("pr_stsfl"),"");
            }
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			LM_PRPSTM.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"getPRDCD");
		}
		return L_PRDCD;
	}
	
	private String getPKGTP(String LP_PKGDS)
	{
		String L_PKGTP = "";
		try{
			M_strSQLQRY = "Select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS'";
			M_strSQLQRY += " and cmt_cgstp='FGXXPKG' and cmt_codds='"+LP_PKGDS+"'";
	
			LM_PRPSTM = cl_dat.M_conSPDBA_pbst .prepareStatement(M_strSQLQRY);
			M_rstRSSET = LM_PRPSTM.executeQuery();
	
			if(M_rstRSSET.next())
				L_PKGTP = nvlSTRVL(M_rstRSSET.getString("cmt_codcd"),"");
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			LM_PRPSTM.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"getPKGTP");
		}
		return L_PKGTP;
	}
	
	private void updTMTRN(ResultSet LP_RSLSET)
	{
		try
		{
	
			LP_RSLSET.updateString("TM_PRDDS",strPRDDS);
	
            LP_RSLSET.updateString("TM_STSFL",strSTATS);
    
			LP_RSLSET.updateFloat("TM_PRTQT",nvlNUMFL(strPRTQT));
	
			if(!strMZONE.equalsIgnoreCase("OVERALL"))
			{
				LP_RSLSET.updateFloat("TM_"+strCHP01+"DOT",nvlNUMFL(strDOMQT));
				LP_RSLSET.updateFloat("TM_"+strCHP01+"DET",nvlNUMFL(strDEXQT));
			}else
			{
				LP_RSLSET.updateFloat("TM_"+strCHP01+"EXT",nvlNUMFL(strEXPQT));
	
			}
			LP_RSLSET.updateString("TM_LUSBY",cl_dat.M_strUSRCD_pbst );
			
			LP_RSLSET.updateString("TM_LUPDT",cl_dat.M_strLOGDT_pbst );
		}catch(Exception L_EX){
			setMSG(L_EX,"updTMTRN");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}	
	
	