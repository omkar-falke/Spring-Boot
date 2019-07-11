import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import java.io.*;
public class qc_dtmtb extends cl_rbase
{
   String[] LM_QPRCD;
   String LM_STRQRY,LM_LOTNO,LM_CLSFL,LM_PRDCD,LM_TSTTP,LM_LOTDT,LM_LINNO;
   String LM_TODT,LM_FRDT,LM_RUNNO,LM_PRDDS,LM_HLPFLD,LM_PSTDT;
   String LM_DSPVL,LM_MFIVL,LM_IZOVL,LM_VICVL,LM_A__VL,LM_TS_VL,LM_EL_VL,LM_B__VL,LM_Y1_VL,LM_WI_VL,LM_RSMVL;
   String[] LM_ARRHDR = new String[2]; // For Help Header 
   JTextField txtFRDT,txtTODT,txtPRDCD,txtPRDDS,txtLINNO;
   JButton btnDTR,btnEXT;
   ButtonGroup chkGRP;
   JRadioButton chkTPRCD,chkPRDCD;	   
   ButtonGroup chkGRP1;
   JRadioButton chkGRAB,chkCOMP;	   
   ResultSet LM_RSLPRD;
   String LM_OPTN = "T";
   String LM_TPR = "T";
   String LM_PRD = "C";
   String LM_GRAB = "0101";
   String LM_COMP = "0103";
   boolean LM_VLD = false;   
	qc_dtmtb()
	{
		super(2);
		chkGRP = new ButtonGroup();
		crtLBL(this,"Grade Type"  , s_cpos  , s_rpos + 100,80,20);
		chkTPRCD = crtRDOGRP(this,chkGRP, "Target Grade",s_cpos+100,s_rpos +100,95,20,true);
		chkPRDCD = crtRDOGRP(this,chkGRP, "Classified Grade",s_cpos+220,s_rpos+100,220,20,false);
		chkGRP1 = new ButtonGroup();
		crtLBL(this,"Test Type"  , s_cpos  , s_rpos + 150,80,20);
		chkCOMP = crtRDOGRP(this,chkGRP1, "Composite",s_cpos+100,s_rpos +150,95,20,true);
		chkGRAB = crtRDOGRP(this,chkGRP1, "Grab Test",s_cpos+220,s_rpos+150,220,20,false);
		LBL_WID =90;
		crtLBL(this,"Product Code"  , s_cpos  , s_rpos + 200,80,20);
		txtPRDCD = crtTXT(this,LEFT,s_cpos+100,s_rpos + 200,80,20);
		txtPRDDS = crtTXT(this,LEFT,s_cpos+200,s_rpos +200,80,20);
		crtLBL(this,"Date Range "  , s_cpos  , s_rpos + 250,80,20);
		txtFRDT = crtTXT(this,LEFT,s_cpos+100,s_rpos + 250,80,20);
		txtTODT = crtTXT(this,LEFT,s_cpos+200,s_rpos + 250,80,20);
		crtLBL(this,"Line Number "  , s_cpos  , s_rpos + 300,80,20);
		txtLINNO = crtTXT(this,LEFT,s_cpos+100,s_rpos + 300,80,20);
		btnDTR = crtBTN(this,"Transfer",s_cpos +200,s_rpos + 380,90,20);
		btnEXT = crtBTN(this,"Exit",s_cpos +340,s_rpos + 380,90,20);
		LM_TSTTP = LM_COMP;	
	}
	
private void getLOTRNG()
{
	ResultSet L_RSLSET ;
	String L_QPRVL,L_INSQRY;
	LM_PRDCD = txtPRDCD.getText().trim();
	LM_FRDT = cc_dattm.occ_dattm.setDBSDT(txtFRDT.getText().trim());
	LM_TODT = cc_dattm.occ_dattm.setDBSDT(txtTODT.getText().trim());	
	LM_STRQRY =  " SELECT LT_LOTNO,LT_RUNNO,date(LT_PSTDT) L_DATE,LT_PRDCD,PS_TSTDT, ";
	LM_STRQRY += "PS_DSPVL,PS_MFIVL,PS_IZOVL,PS_VICVL,PS_TS_VL,PS_EL_VL,PS_RSMVL,PS_WI_VL,PS_A__VL,PS_B__VL,PS_Y1_VL ";
	LM_STRQRY += " from PR_LTMST,QC_PSMST where LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO AND";
	LM_STRQRY += " PS_TSTTP ='" + LM_TSTTP +"' and ";
	if(LM_OPTN.trim().equals(LM_TPR))
	{
		LM_STRQRY += " LT_TPRCD = ";
		LM_STRQRY += "'"+ LM_PRDCD+"'";
	}
	else if(LM_OPTN.trim().equals(LM_PRD))
	{
		LM_STRQRY += " LT_CLSFL = '9' and LT_PRDCD = ";
		LM_STRQRY += "'"+ LM_PRDCD+"'";
	}
	LM_STRQRY += " AND LT_RCLNO ='00'";
	LM_STRQRY += " AND LT_LINNO ='"+LM_LINNO+"'";
	LM_STRQRY += " AND date(LT_PSTDT) BETWEEN " + LM_FRDT.trim() + " AND " + LM_TODT.trim() ;
	//LM_STRQRY += " AND LT_CLSFL = '9'";
	LM_STRQRY += " AND LT_CLSFL <> '8'";
	LM_STRQRY += " ORDER BY LT_RUNNO,LT_PRDCD,LT_LOTNO,PS_TSTDT";
	int cnt =0;
	try
	{
		int i=0,L_ROWCT = 0,L_CNT =0;
		
		M_rstRSSET = cl_dat.exeSQLQRY1(LM_STRQRY);
		if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_INSQRY = "Insert into dt_mtbdt(runno,lotno,grade,date,dsp,mfi,";
				L_INSQRY +="izo,vic,ts,el,rsm,wi,a,b,y1)values(";
				LM_LOTNO = M_rstRSSET.getString("LT_LOTNO");
				LM_RUNNO = M_rstRSSET.getString("LT_RUNNO");
				LM_PSTDT = cc_dattm.occ_dattm.setDATE("MDY",M_rstRSSET.getDate("L_DATE"));
				 getPRDDS(LM_PRDCD);
				 LM_DSPVL = M_rstRSSET.getString("PS_DSPVL");				
				 LM_MFIVL = M_rstRSSET.getString("PS_MFIVL");				
				 LM_IZOVL = M_rstRSSET.getString("PS_IZOVL");				
				 LM_VICVL = M_rstRSSET.getString("PS_VICVL");				
				 LM_TS_VL = M_rstRSSET.getString("PS_TS_VL");				
				 LM_EL_VL = M_rstRSSET.getString("PS_EL_VL");				
				 LM_RSMVL = M_rstRSSET.getString("PS_RSMVL");				
				 LM_WI_VL = M_rstRSSET.getString("PS_WI_VL");
				 LM_A__VL = M_rstRSSET.getString("PS_A__VL");				
				 LM_B__VL = M_rstRSSET.getString("PS_B__VL");				
				 LM_Y1_VL = M_rstRSSET.getString("PS_Y1_VL");				
		
				 L_INSQRY += "'" + nvlSTRVL(LM_RUNNO,"") + "',";
				 L_INSQRY += "'" + nvlSTRVL(LM_LOTNO,"") + "',";
				 L_INSQRY += "'" + nvlSTRVL(LM_PRDDS,"") + "',";
				 if(nvlSTRVL(LM_PSTDT.trim(),"").length()>0)
					L_INSQRY += "ctod('"+LM_PSTDT+ "'),";
				 else
					 L_INSQRY += "ctod('  /  /    '),";
				 L_INSQRY += nvlSTRVL(LM_DSPVL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_MFIVL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_IZOVL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_VICVL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_TS_VL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_EL_VL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_RSMVL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_WI_VL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_A__VL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_B__VL,"0") + ",";
				 L_INSQRY += nvlSTRVL(LM_Y1_VL,"0")+")";
				 cl_dat.M_LCLUPD = true;
				L_CNT++;
				conSPBKA.executeUpdate(L_INSQRY);
				 if(!cl_dat.M_LCLUPD)
				 {
					 System.out.println(L_INSQRY);
				 }
				 if(cl_dat.M_LCLUPD)
				 {
					L_CNT ++;
					 cl_dat.exeDBCMT("QC","REM","");
				 }
				 
			}
		if(L_CNT >0)
		{
			setMSG("Data Transfer File has been created at c:\\backup\\temp\\dt_mtbdt.dbf ",'N');
		}
		else
			setMSG("No Data Found for the given input data.. ",'E');
	}
	catch(SQLException L_SE)
	{
		System.out.println("Error 14"+L_SE.toString());
	}
	catch(Exception L_SE)
	{
		System.out.println("Error 15");
	}
}
public void getPRDDS(String LP_PRDCD)
{
			
	LM_PRDDS = "";
	LM_STRQRY = " Select PR_PRDDS from CO_PRMST where PR_PRDCD = ";
	LM_STRQRY += "'"+ LP_PRDCD + "'";
	try
	{
		LM_RSLPRD = cl_dat.exeSQLQRY(LM_STRQRY);
		if(LM_RSLPRD.next())
		{
			LM_PRDDS = LM_RSLPRD.getString("PR_PRDDS");
		}
		txtPRDDS.setText(LM_PRDDS);
	}
	catch(SQLException L_SE)
	{
		System.out.println("Exception 1"+L_SE.toString());
	}
}
public boolean exePRDVLD(String LP_PRDCD)
{
	int L_CNT =0;		
	LM_PRDDS = "";
	LM_STRQRY = " Select count(*) L_CNT from CO_PRMST where PR_PRDCD = ";
	LM_STRQRY += "'"+ LP_PRDCD + "'";
	try
	{
		M_rstRSSET = cl_dat.exeSQLQRY(LM_STRQRY);
		if(M_rstRSSET.next())
		{
			L_CNT = M_rstRSSET.getInt("L_CNT");
		}
		if(L_CNT == 0)
		{
			setMSG("Product Code does not exist.. ",'N');
			return false;
		}
		else
		{
			setMSG(" ",'N');
			txtFRDT.requestFocus();
			return true;
		}
	}
	catch(SQLException L_SE)
	{
		System.out.println("Exception 1"+L_SE.toString());
	}
	return false;
}
public void actionPerformed(ActionEvent L_AE)
{
     super.actionPerformed(L_AE);
       if (L_AE.getSource().equals(btnHLP_OK))
           exeHLPOKY();	
	   else if (L_AE.getSource().equals(chkTPRCD))
	   {
		   LM_OPTN = LM_TPR;
		   
	   }
	   else if (L_AE.getSource().equals(chkPRDCD))
	   {
		  if(LM_TSTTP.equals(LM_GRAB))
			{
				setMSG("Grab Test can not be taken for Classified Grade..",'E');
				chkCOMP.setSelected(true);
				LM_TSTTP = LM_COMP;
			}
			else
			{
				setMSG("",'N');
				
			}
		   LM_OPTN = LM_PRD;
	   }
	   else if (L_AE.getSource().equals(chkGRAB))
	   {
		  //LM_TSTTP = LM_GRAB;
		   
			if(LM_OPTN.equals(LM_PRD))
			{
				setMSG("Grab Test can not be taken for Classified Grade..",'E');
				chkCOMP.setSelected(true);
				LM_TSTTP = LM_COMP;
			}
			else
			{
				setMSG("",'N');
				LM_TSTTP = LM_GRAB;
			}
	   }
	   else if (L_AE.getSource().equals(chkCOMP))
	   {
		   LM_TSTTP = LM_COMP;
	   }
	   else if (L_AE.getSource().equals(txtLINNO))
	   {
		   LM_LINNO = txtLINNO.getText().trim();
		   if(chkLINNO())
		   {
			   setMSG("Valid Line number",'N');
			   btnDTR.setEnabled(true);
			   btnDTR.requestFocus();
				
		   }
		   else
		   {
			   setMSG("Invalid Line number",'E');
		   }
	   }
	   else if(L_AE.getSource().equals(btnDTR))
	   {
                        cl_dat.conSPDTA =cl_dat.setCONDBS("DTRDATA",cl_dat.M_SPDTA);
                        cl_dat.conSPBKA =cl_dat.setCONFTB(cl_dat.M_SPBKA);
		   if((txtPRDCD.getText().trim().length() ==0) || (txtFRDT.getText().trim().length() ==0)||(txtTODT.getText().trim().length() ==0)||(txtLINNO.getText().trim().length() ==0))
		   {
			   setMSG("Incomplete data.Please type all the required data ..",'N');
			   btnDTR.setEnabled(false);
		   }
		   else
		   {
			btnDTR.setEnabled(false);
			setMSG("Data Transfer is in Progress..",'N');
			boolean LM_EXTVL = false;
			Process p = null;
			this.setCursor(curWTSTS);
		 		LM_EXTVL = false;	
				Runtime r = Runtime.getRuntime();
				try{
				File L_TMPFL = new File(cl_dat.M_SPBKA + "/"+"dt_mtbdt.dbf");
					if(L_TMPFL.exists())
							L_TMPFL.delete();
					
                                    p  = r.exec("c:\\windows\\command\\xcopy.exe" + " f:\\foxdat\\0102\\datatfr\\qcadata\\dt_mtbdt.dbf " + cl_dat.M_SPBKA); 
					while(!LM_EXTVL){
					try
					{
							LM_EXTVL = true;
							p.exitValue();
					}catch(Exception L_E)
					{
							LM_EXTVL = false;
					}
				 }
				}
				catch(Exception L_E)
				{
			
				}
				p.destroy();
			    getLOTRNG();
				btnEXT.requestFocus();
				this.setCursor(curDFSTS);
		   }
		  
	   }
	  else if(L_AE.getSource().equals(btnEXT))
	   {
		   try
		   {
			if(cl_dat.conSPDTA != null)
						cl_dat.conSPDTA.close();
					if(cl_dat.conSPBKA != null)
						cl_dat.conSPBKA.close();
		   }
		   catch(Exception L_E)
		   {
		   }
		this.dispose();
	   }
	  else if(L_AE.getSource().equals(txtFRDT))
	  {
		   if(!cc_dattm.occ_dattm.exeVLDDT(txtFRDT.getText().trim()))
			{
					setMSG("Invalid Date .. ",'E'); 
			
			}
		   else
		   {
			   if(cc_dattm.occ_dattm.cmpDATE(txtFRDT.getText().trim(),cl_dat.M_LOGDAT) >0)
			   {
				   setMSG("Date can not be greater than the current date..",'E');
			   }
			   else
			   {
				   txtFRDT.transferFocus();
				   txtTODT.setText(cl_dat.M_LOGDAT);
			   }
		   }
		   
	  }
	  else if(L_AE.getSource().equals(txtTODT))
	  {
		   if(!cc_dattm.occ_dattm.exeVLDDT(txtTODT.getText().trim()))
			{
				setMSG("Invalid Date .. ",'E'); 
			
			}
		   else
		   {
				if(cc_dattm.occ_dattm.cmpDATE(txtFRDT.getText().trim(),txtTODT.getText().trim()) >0)
				{
					setMSG("From Date can not be greater that TO date ..",'E');
				}
				else if(cc_dattm.occ_dattm.cmpDATE(txtTODT.getText().trim(),cl_dat.M_LOGDAT) >0)
			    {
				   setMSG("Date can not be greater than the current date..",'E');
			    }
			    else
				{
					txtLINNO.requestFocus();
				}
		   }
	  }
	  else if(L_AE.getSource().equals(txtPRDCD))
	  {
		   LM_PRDCD = txtPRDCD.getText().trim();
		   if(exePRDVLD(LM_PRDCD))
			{	getPRDDS(txtPRDCD.getText().trim());
				txtPRDDS.setText(LM_PRDDS);
				setMSG("Valid Product Code",'N');
				txtFRDT.requestFocus();
			}
		   else
		   {
			   setMSG("Invalid Product Code",'E');
		   }
	  }

}
public void keyPressed(KeyEvent L_KE)
    {
	      super.keyPressed(L_KE);
      	  if (L_KE.getKeyCode()== L_KE.VK_ENTER)
          {
               if(L_KE.getSource().equals(btnEXT))
			   {  
				 try
				 {
						if(cl_dat.conSPDTA != null)
							cl_dat.conSPDTA.close();
							if(cl_dat.conSPBKA != null)
							cl_dat.conSPBKA.close();
						this.dispose();
				 }
				 catch(Exception L_E)
				 {
				 }
			   }
               else if (cl_dat.M_HLPFLG)
                     exeHLPOKY();
			 
          }
          else if (L_KE.getKeyCode()== L_KE.VK_F1)
          try
          {
				LM_STRQRY = null;
				char L_DSNTYP = 'M';
				int L_SCHCOL = 2;
				int L_NUMFLD = 2;
				
				if(L_KE.getSource().equals(txtPRDCD))
				{
					cl_dat.M_HLPFLG = true;
					cl_dat.M_PRDTP = cmbOPTTP.getSelectedItem().toString().substring(0,2);
					cl_dat.M_QCATP = cmbOPTTP.getSelectedItem().toString().substring(0,2);
				    LM_STRQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_STSFL <> 'X' order by PR_PRDDS  ";
				    LM_HLPFLD = "txtPRDCD";
					txtPRDCD.setEnabled(false);
				    LM_ARRHDR[0] = " Product Code ";
				    LM_ARRHDR[1] = " Grade ";
					int[] L_ARRGSZ = {40,40};
				  	if(LM_STRQRY != null)
					{
						cl_hlp(LM_STRQRY,"CO","ACT",2,1,LM_ARRHDR,2,"CT");
					}
				}
				else if(L_KE.getSource().equals(txtLINNO))
				{
					cl_dat.M_HLPFLG = true;
					cl_dat.M_PRDTP = cmbOPTTP.getSelectedItem().toString().substring(0,2);
					cl_dat.M_QCATP = cmbOPTTP.getSelectedItem().toString().substring(0,2);
					LM_ARRHDR[0] = "Line No.";
					LM_ARRHDR[1] = "Description";	
					LM_STRQRY = "SELECT cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp =  ";
					LM_STRQRY = LM_STRQRY + "'SYS'" + " AND cmt_cgstp = ";
					LM_STRQRY = LM_STRQRY + "'PRXXLIN'";
					LM_STRQRY += " AND CMT_CCSVL = " + "'"+cl_dat.M_PRDTP + "'";			    LM_HLPFLD = "txtPRDCD";
					txtLINNO.setEnabled(false);
					LM_HLPFLD = "txtLINNO";
				    int[] L_ARRGSZ = {40,40};
				  	if(LM_STRQRY != null)
					{
						cl_hlp(LM_STRQRY,"CO","ACT",2,1,LM_ARRHDR,2,"CT");
					}
				}
	      }
          catch(NullPointerException L_NPE)
          {
              setMSG("Help not available",'N');                            
          }
          else if (L_KE.getKeyCode() == 106)
          {              
              btnEXT.setEnabled(true);
              btnEXT.requestFocus();
          }
		 	  
   }
private void exeHLPOKY()
    {
         if (LM_HLPFLD.equals("txtPRDCD"))
         {
             txtPRDCD.setText(cl_dat.M_HLPSTR);
             txtPRDCD.setEnabled(true);
             txtPRDCD.requestFocus();
         }
		 else if (LM_HLPFLD.equals("txtLINNO"))
         {
             txtLINNO.setText(cl_dat.M_HLPSTR);
             txtLINNO.setEnabled(true);
             txtLINNO.requestFocus();
         }
         cl_dat.M_HLPFLG = false;
    }
public void focusGained(FocusEvent L_FE)
{
	if(L_FE.getSource().equals(txtPRDCD))
	{
		setMSG("Press F1 to select the Product Code..",'N');
	}
	else if(L_FE.getSource().equals(txtFRDT))
	{
		setMSG("Please enter the From Date..",'N');
	}	
	else if(L_FE.getSource().equals(txtTODT))
	{
		setMSG("Please enter the To Date..",'N');
	}	
		
}
private boolean chkLINNO()
	{
	String L_STRSQL ="";
		if(txtLINNO.getText().trim().length()==0)
		{
			setMSG("Line No can not be Empty ..Enter some valid Line No ", 'E');
			return false;
		}	
		L_STRSQL ="select count(*) from co_cdtrn where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'PRXXLIN' AND CMT_CODCD =";
		L_STRSQL +="'"+ txtLINNO.getText().trim()+"'";
		L_STRSQL +=" AND CMT_CCSVL = '"+ cl_dat.M_PRDTP+"'";
		if(cl_dat.getRECCNT("CO","ACT",L_STRSQL)>0)
			return true;
		else
		{
			setMSG("Invalid Line No...Enter some valid Line No ", 'E');
			return false;
			
		}
	}
	public Connection setCONFTB(String LP_PTHWD)
{
	String L_URLSTR ="";
    L_URLSTR = "jdbc:odbc:Visual FoxPro Tables;SourceDB = " + LP_PTHWD;
	try{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        conSPBKA = DriverManager.getConnection(L_URLSTR,"","");
	    stmSTBK1 = conSPBKA.createStatement();
	}
	catch(ClassNotFoundException L_CNFE){
		System.out.println("Connectiob not found");
	}
	catch(SQLException L_SQLE){
		System.out.print("Database not found "+M_strSQLQRY);
	}
	return conSPBKA;
}
void cpyDBFFL()
{

	boolean L_flgEXTVL = false;
	Process p = null;
	this.setCursor(cl_dat.M_curWTSTS_pbst);
 	L_flgEXTVL = false;	
	Runtime r = Runtime.getRuntime();
	try
	{
		    if(conSPBKA !=null)
		        conSPBKA.close();
		    setCONFTB("c:\\reports");
		    File L_TMPFL = new File("c:\\reports\\adv_rct.dbf");
			if(L_TMPFL.exists())
					L_TMPFL.delete();
			p  = r.exec("c:\\windows\\xcopy.exe" + " f:\\foxdat\\0102\\datatfr\\mmsdata\\adv_rct.dbf " + "c:\\reports"); 
			while(!L_flgEXTVL)
			{
    			try
    			{
    				L_flgEXTVL = true;
    				p.exitValue();
    			}catch(Exception L_E)
    			{
    				L_flgEXTVL = false;
    			}
			}
		}
		catch(Exception L_E)
		{
	        setMSG(L_E,"cpyDBFFL");
		}
		p.destroy();

}

}
