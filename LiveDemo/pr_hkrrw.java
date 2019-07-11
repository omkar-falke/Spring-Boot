/*
System Name   : Laboratory Information Management System
Program Name  : pr_hkrrw
Program Desc. : Run No. Rework Procedure
Author        : N.K.Virdi
Date          : 12th aug 2002
Version       : LIMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        : 2.0

*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import cl_frm;
import javax.swing.*;
public class pr_hkrrw extends cl_frm
{
	String[] LM_ARRHDR = new String[3];
	JTextField txtTPRCD,txtLINNO,txtGRDCD,txtDATE;
	//JList lstDSPRUN;
	JTextArea lstDSPRUN;
	JButton btnRWK,btnRWALL,btnEXT;
	String LM_HLPFLD;
	String L_STRTAB = "        ";
	String L_TPRCD ;
	String L_LINNO ;
	String L_RUNNO ;
	String L_PSTDT ;
	String L_PENDT ;
	String L_RWRUN ;
	String L_LOTNO ;
	String L_RCLNO ;
	String L_PRPEN ;
	String L_FSTRNSR = "00001" ;
	String LM_YRVAL ;
	String L_DSPSTR = "";
	String LM_INTRCL ="00";
	String LM_PRDTP ="01";
	int LM_ROWCNT =0;
	boolean RWFLG = false;
	pr_hkrrw()
	{
		/**
		 * Run No =  8 digit No.
		 *   2 digits of Line No + Year Digit + 5 digit running serial for a particular grade
		 * For reworking of run no's year digit can be taken from last digit of lot start date.
		 */
	cl_dat.ocl_dat.M_PRGVER =" (v2.0)";
        this.setTitle(super.getTitle());
        Font font_nor = new Font("Arial", Font.PLAIN, 12);
		Font font_bld = new Font("Arial", Font.BOLD, 11);
        int s_rpos = 70;              // Starting Row position
        int s_cpos = 100;
		int l_gap = 80;               // Line Gap
        int txt_h = 20;               // Text field height
		font_par = font_bld;
		lblMODL.setText("Run No. Rework Procedure "+cl_dat.ocl_dat.M_PRGVER);
		crtLBL(pnlMAIN,"Product Type." , s_cpos       , s_rpos,80,20);
		font_par = font_nor;
     	crtLBL(pnlMAIN,"Product Code"  , s_cpos +  100, s_rpos,80,20);
		crtLBL(pnlMAIN,"Target Grade"  , s_cpos +  210, s_rpos,80,20);
       cl_dat.ocl_dat.M_PROSST = "'01','02'";
		txtSTAT.setEnabled(false);
	   crtCMBTP(s_cpos,s_rpos+20,"PR");
		txtTPRCD = crtTXT(pnlMAIN,LEFT,s_cpos+100,s_rpos+20,100,20);
		txtGRDCD = crtTXT(pnlMAIN,LEFT,s_cpos+210,s_rpos+20,100,20);
		crtLBL(pnlMAIN,"Line No.", s_cpos+320       , s_rpos,80,20);
		crtLBL(pnlMAIN,"LOT NO."+L_STRTAB + "Lot Start Date & Time" +L_STRTAB +"Lot end Date & Time   " + "Run No.",s_cpos,200,300,20); 
		txtLINNO = crtTXT(pnlMAIN,LEFT,s_cpos+320,s_rpos+20,75,20);
		btnRWK = crtBTN(pnlMAIN,"Rework Run No.",s_cpos+180,s_rpos+50,100,25);
		btnEXT = crtBTN(pnlMAIN,"Exit ",s_cpos+300,s_rpos+50,100,25);
		crtLBL(pnlMAIN,"Year From : "  , s_cpos+100,s_rpos+100,80,20);
		txtDATE = crtTXT(pnlMAIN,LEFT,s_cpos+200,s_rpos+100,75,20);
		btnRWALL = crtBTN(pnlMAIN,"Rework All ",s_cpos+300,s_rpos+100,100,25);
		lstDSPRUN = new JTextArea();
		lstDSPRUN.setSize(400,200);
		lstDSPRUN.setLocation(s_cpos,240);
		pnlMAIN.add(lstDSPRUN);
		addLSTN();  // add Listeners
		exeINTSTA(); // Initial state - textFields disabled
		exeENBSCR(true);
		cmbOPTN.requestFocus();
	}
		
	private void exeINTSTA()
	{
		txtLINNO.setText("");
		txtTPRCD.setText("");
		txtGRDCD.setText("");
		btnRWK.setEnabled(false);
//		txtDATE.setEnabled(false);
//		btnRWALL.setEnabled(false);
	}
	private void addLSTN()
	{
		txtLINNO.addFocusListener(this);
		txtLINNO.addKeyListener(this);
		txtLINNO.addActionListener(this);
		txtTPRCD.addFocusListener(this);
		txtTPRCD.addKeyListener(this);
		txtTPRCD.addActionListener(this);
		btnRWK.addKeyListener(this);
		btnRWK.addFocusListener(this);
		btnRWALL.addKeyListener(this);
		btnRWALL.addFocusListener(this);
		txtDATE.addKeyListener(this);
		txtDATE.addActionListener(this);
		txtDATE.addFocusListener(this);
	}
	private void exeENBSCR(boolean LP_ENBFL)
	{
		txtLINNO.setEnabled(LP_ENBFL);
		//txtTPRCD.setEnabled(LP_ENBFL);
		txtGRDCD.setEnabled(false);
	}
	private void rwkRUNNO(String LP_PRDCD,String LP_LINNO)
	{
		L_TPRCD = "";
		L_LINNO = "";
		L_RUNNO = "";
		L_PSTDT = "";
		L_PENDT = "";
		L_RWRUN = "";
		L_LOTNO = "";
		L_RCLNO = "";
		L_PRPEN = "";
		int L_CNT =0;
		try
		{
			//lstDSPRUN.setText("");
			L_DSPSTR = L_STRTAB = "        ";
			
			if(RWFLG)
			{
				if(txtDATE.getText().trim().length() == 10)
						LM_YRVAL = txtDATE.getText().trim().substring(9).trim();
			}
			else
			{
				LM_YRVAL = cl_dat.ocl_dat.M_LOGDAT.substring(9).trim();
			}
			LM_STRSQL = " Select LT_TPRCD,LT_LOTNO,LT_LINNO,LT_RCLNO,LT_RUNNO,LT_PSTDT,LT_PENDT ";
		//	LM_STRSQL += "FROM DT_LTMST,CO_PRMST WHERE LT_TPRCD = PR_PRDCD ";
			LM_STRSQL += "FROM DT_LTMST where ";
			LM_STRSQL += " LT_PRDTP = " + "'" +LM_PRDTP+"'";
			LM_STRSQL += " AND LT_TPRCD = " + "'" +LP_PRDCD.trim()+"'";
			LM_STRSQL +=" AND LT_LINNO = "+ "'" +LP_LINNO.trim()+"'";
			LM_STRSQL +=" AND LT_RCLNO = "+ "'" +LM_INTRCL.trim()+"'";
			LM_STRSQL += " AND LT_PSTDT >= "; 
			LM_STRSQL += cc_dattm.occ_dattm.setDBSTM(txtDATE.getText().trim() + " 00:01");
			LM_STRSQL +=" AND LT_CLSFL  "+ "<> '8'";
			LM_STRSQL +=" ORDER BY LT_LOTNO";
			LM_RSLSET =  cl_dat.ocl_dat.exeSQLQRY1(LM_STRSQL,"QC","ACT");
		    
			L_CNT =0;
			if(LM_RSLSET !=null)
			while(LM_RSLSET.next())
			{
			//	L_YRVAL ="";
				L_LOTNO = LM_RSLSET.getString("LT_LOTNO");
				L_RCLNO = LM_RSLSET.getString("LT_RCLNO");
				L_TPRCD = LM_RSLSET.getString("LT_TPRCD");
				L_LINNO = LM_RSLSET.getString("LT_LINNO");
				L_RUNNO = LM_RSLSET.getString("LT_RUNNO");
				L_PSTDT = LM_RSLSET.getString("LT_PSTDT");
				if(L_PSTDT == null)
				{
					L_PSTDT = "";
				//	L_YRVAL = LM_YRVAL;
				}
				//else
				//	L_YRVAL = L_PSTDT.substring(9,1).trim();
				L_PENDT = LM_RSLSET.getString("LT_PENDT");
				if(L_PENDT == null)
					L_PENDT = "";
				String L_TMP = padSTRING('R',cc_dattm.occ_dattm.setDTMFMT(L_PSTDT),15);
				L_DSPSTR = padSTRING('R',L_LOTNO + " "+ L_RCLNO,11) +padSTRING('R',"",6) +L_TMP +padSTRING('R',"",8);
				L_TMP = padSTRING('R',cc_dattm.occ_dattm.setDTMFMT(L_PENDT),15);
				L_DSPSTR +=L_TMP;
				if(L_CNT ==0)
				{
					// changed as per the lot no series logic.(Line no and then yead digit))
					L_RWRUN = LP_LINNO +LM_YRVAL + L_FSTRNSR;
					///L_RWRUN = LM_YRVAL + LP_LINNO +L_FSTRNSR;
					if(L_RUNNO != null)
					{
						if(!L_RUNNO.trim().equals(L_RWRUN))
						{
							updRWRUN(L_LOTNO,L_RWRUN);
						}
					}
					else
						updRWRUN(L_LOTNO,L_RWRUN);
					L_DSPSTR += padSTRING('R'," ",8)+padSTRING('R',L_RWRUN,8);
				}
				else if(L_CNT > 0)
				{
					if(cc_dattm.occ_dattm.cmpDATTM(L_PRPEN,L_PSTDT)== 0)
					{
						updRWRUN(L_LOTNO,L_RWRUN);
					}
					else
					{
						String L_STRTMP = "";
						Integer L_INT = Integer.valueOf(L_RWRUN);
						int L_INTRW = L_INT.intValue();
						L_INTRW = L_INTRW +1;
						String L_NXTRUN = String.valueOf(L_INTRW);
						for(int i=0;i<L_RWRUN.length()-L_NXTRUN.length();i++)
						{
							L_STRTMP += "0";
						}
						L_STRTMP += L_NXTRUN;
						L_RWRUN = L_STRTMP;
						updRWRUN(L_LOTNO,L_RWRUN);
					}
					L_DSPSTR += padSTRING('R'," ",8)+ padSTRING('R',L_RWRUN,8);
				}
				L_PRPEN  = L_PENDT;
				//L_PRVCD = L_TPRCD;
				L_CNT += 1;
				//lstDSPRUN.addItem(L_DSPSTR);
				//lstDSPRUN.insert(L_DSPSTR,0);
			}
			if(L_CNT == 0)
			{
				setMSG("No Lot exists with the given Grade for Line No "+L_LINNO,'N');
			}
		}
		catch(SQLException L_SE)
		{
			System.out.println("Exception "+L_SE.toString());
		}
	}
	private void updRWRUN(String LP_LOTNO,String LP_RUNNO)
	{
		String L_STRQRY = "";
		int L_UPDCNT =0;
		try
		{
			cl_dat.ocl_dat.M_STLSQL = "UPDATE DT_LTMST SET ";
			cl_dat.ocl_dat.M_STLSQL +="LT_TRNFL='0',";
			cl_dat.ocl_dat.M_STLSQL +="LT_RUNNO='"+LP_RUNNO.trim()+"',";
			cl_dat.ocl_dat.M_STLSQL +="LT_LUSBY='"+cl_dat.ocl_dat.M_USUSRCD+"',";
			cl_dat.ocl_dat.M_STLSQL +="LT_LUPDT="+cc_dattm.occ_dattm.setDBSDT(cl_dat.ocl_dat.M_LOGDAT);
			L_STRQRY +=" WHERE LT_LOTNO = " + "'"+LP_LOTNO +"'";
			L_STRQRY +=" AND LT_RCLNO ='"+LM_INTRCL +"'";
			L_STRQRY +=" AND LT_CLSFL "+ "<> '8'";
			cl_dat.ocl_dat.M_STLSQL +=L_STRQRY;
			cl_dat.ocl_dat.exeSQLUPD(cl_dat.ocl_dat.M_STLSQL,"QC","ACT","setLCLUPD");	
			if(cl_dat.ocl_dat.M_LCLUPD)
			{
				cl_dat.ocl_dat.exeDBCMT("QC","ACT","");
			}
			else
				cl_dat.ocl_dat.exeDBRBK("QC","ACT");
			
		}
		catch(Exception L_SE)
		{
			System.out.println(" Error "+L_SE.toString());
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		if (L_AE.getSource().equals(btnRWK))
		{	
			RWFLG = false;
			exeBTNRWK();
		}
		else if (L_AE.getSource().equals(btnRWALL))
		{	
			System.out.println("rework all");
			RWFLG = true;
			exeRWALL();
		}
		else if (L_AE.getSource().equals(btnEXT))
		{	
			this.dispose();
		}
		else if(L_AE.getSource().equals(txtDATE))
		{
			btnRWALL.setEnabled(true);
			btnRWALL.requestFocus();
		}
		else if(L_AE.getSource().equals(txtLINNO))
		{
			if(!cl_dat.ocl_dat.M_HLPFLG)
			{
				if(chkLINNO())
				{
					btnRWK.setEnabled(true);
					btnRWK.requestFocus();
				}
				else
				{
					txtDATE.setEnabled(true);
					txtDATE.requestFocus();
				}
			}
		}
		else if(L_AE.getSource().equals(txtTPRCD))
		{
		//	if(lstDSPRUN.countItems()>0)
		//		lstDSPRUN.removeAll();
			lstDSPRUN.setText("");
			if(!cl_dat.ocl_dat.M_HLPFLG)
			{
				if(!chkPRDCD())
				{
					txtDATE.setEnabled(true);
					txtDATE.requestFocus();
				}
				else
				{
					txtDATE.setEnabled(false);
					btnRWALL.setEnabled(false);
					getPRDDS(txtTPRCD.getText().trim());
						txtLINNO.setEnabled(true);
				}
			}
		}

	}
	public void keyTyped()
	{
	}
	public void keyPressed(KeyEvent L_KE)
	{
		int i =0;
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == 106)
		{
			 exeINTSTA();
			 exeENBSCR(false);
			 super.setENLEXT();
	    }
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			  try
			  {
				if(L_KE.getSource().equals(txtTPRCD))
				{
					cl_dat.ocl_dat.M_HLPFLG = true;
					LM_ARRHDR[0] = "Product Code";
					LM_ARRHDR[1] = "Line No.";	
					LM_ARRHDR[2] = "Grade";	
					LM_STRQRY = "Select distinct LT_TPRCD,LT_LINNO,PR_PRDDS from DT_LTMST,CO_PRMST WHERE LT_TPRCD = PR_PRDCD and PR_STSFL IN('1','2')";
					LM_STRQRY +=" AND LT_PRDTP = " + "'"+LM_PRDTP + "'"+ " ORDER BY PR_PRDDS";
					LM_HLPFLD = "txtTPRCD";
					txtTPRCD.setEnabled(false);
					if(LM_STRQRY != null)
					cl_hlp(LM_STRQRY,"QC","ACT",3,1,LM_ARRHDR,2,"CT");
				}
				else if(L_KE.getSource().equals(txtLINNO))
				{
					cl_dat.ocl_dat.M_HLPFLG = true;
					LM_ARRHDR[0] = "Line No.";
					LM_ARRHDR[1] = "Description";	
					LM_STRQRY = "SELECT cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp =  ";
					LM_STRQRY = LM_STRQRY + "'SYS'" + " AND cmt_cgstp = ";
					LM_STRQRY = LM_STRQRY + "'PRXXLIN'";
					LM_HLPFLD = "txtLINNO";
					txtLINNO.setEnabled(false);
					if(LM_STRQRY != null)
					cl_hlp(LM_STRQRY,"CO","ACT",2,1,LM_ARRHDR,2,"CT");
				}
			  }
			catch(Exception L_E)
			{
			       setMSG("Error in getting help ...",'E');
			}
		}
		if(L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
			if(cl_dat.ocl_dat.M_HLPFLG)
				exeHLPOK();	
			/*else if(L_KE.getComponent().equals(btnRWK))
			{
				RWFLG = false;
				exeBTNRWK();
			}
			else if (L_KE.getSource().equals(btnRWALL))
			{	
				RWFLG = true;
				exeRWALL();
			}*/
			else if(L_KE.getComponent().equals(txtTPRCD))
				txtLINNO.requestFocus();
			else if (L_KE.getSource().equals(btnEXT))
			{	
				this.dispose();
			}
			else
				L_KE.getComponent().transferFocus();
			
	    }
	}
	private void exeHLPOK()
	{
		 if(LM_HLPFLD.equals("txtTPRCD"))
		   {
				txtTPRCD.setText(cl_dat.ocl_dat.M_HLPSTR);
				cl_dat.ocl_dat.M_HLPFLG = false;
				txtTPRCD.setEnabled(true);
				txtLINNO.setText(LM_STRHLP.substring(15,17));
				txtTPRCD.requestFocus();
			}
		   else if(LM_HLPFLD.equals("txtLINNO"))
		   {
				txtLINNO.setText(cl_dat.ocl_dat.M_HLPSTR);
				cl_dat.ocl_dat.M_HLPFLG = false;
				txtLINNO.setEnabled(true);
				txtLINNO.requestFocus();
			}
	}
	public void focusGained(FocusEvent L_FE)
	{
		/*if(L_FE.getSource().equals(txtLINNO))
		{
			setMSG("Enter The Line No. For which the Run No. to be Reworked",'N');
		}
		if(L_FE.getSource().equals(txtTPRCD))
		{
			setMSG("Enter The Product Code For which the Run No. to be Reworked",'N');
		}*/
	}
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
	
	}
	private boolean chkLINNO()
	{
		String L_STRSQL ="";
		if(txtLINNO.getText().trim().length()==0)
		{
			setMSG("Line No can not be Empty ..Enter some valid Line No ", 'E');
			return false;
		}	
		L_STRSQL ="Select count(*) from CO_CDTRN where ";
		L_STRSQL +=" CMT_CGMTP ='SYS' and CMT_CGSTP ='PRXXLIN'";
		L_STRSQL +=" and CMT_CODCD ='"+txtLINNO.getText().trim()+"'";
	    LM_ROWCNT = cl_dat.ocl_dat.getRECCNT("QC","ACT",L_STRSQL);
		if(LM_ROWCNT >0)
			return true;
		else
		{
			setMSG("Invalid Line No...Enter some valid Line No ", 'E');
			return false;
			
		}
	}
	private boolean chkPRDCD()
	{
			
		if(txtTPRCD.getText().trim().length()==0)
		{
			setMSG("Product Code cannot be of zero length",'E');
			return false;
		}
		//LM_STRQRY = "SELECT count(*)L_CNT from CO_PRMST where PR_PRDCD = ";
		LM_STRQRY = "SELECT count(*)L_CNT from DT_LTMST,CO_PRMST where LT_TPRCD = PR_PRDCD AND LT_TPRCD = ";
		LM_STRQRY = LM_STRQRY + "'"+ txtTPRCD.getText().trim()+"'" + " AND PR_STSFL IN ('1','2')";
		LM_STRQRY = LM_STRQRY + " AND LT_PRDTP = " + "'"+ LM_PRDTP+"'";
		LM_ROWCNT = getROWCNT(LM_STRQRY,"QC","ACT");
		if(LM_ROWCNT >0)
		{
			setMSG("Enter the Line No. for which RUN No to be reworked", 'N');
			txtLINNO.requestFocus();
			return true;  // product code exists
		}
		else
		{
			setMSG("Invalid Target Product Code ", 'E');
			return false;
		}
	}
	private int getROWCNT(String LP_STRQRY,String LP_SYSCD,String LP_ACTREM)
	{
		int L_ROWCNT = -1;
		try
		{
			LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY(LP_STRQRY,LP_SYSCD,LP_ACTREM);
			if(LM_RSLSET != null)
			while(LM_RSLSET.next())
			{
				L_ROWCNT = LM_RSLSET.getInt(1);
			}
		}
		catch(SQLException L_SE)
		{
            System.out.println("Exception     getROWCNT" + L_SE.toString());
			return L_ROWCNT ;	
			
		}
		catch(NullPointerException L_NE)
		{
            System.out.println("Exception       getROWCNT" + L_NE.toString());
			return L_ROWCNT ;	
		}
		return L_ROWCNT;
	}
	private void getPRDDS(String LP_PRDCD)
	{
		String L_STRQRY = "";
		String L_STRTMP = "";
		L_STRQRY = "Select PR_PRDDS from co_prmst where pr_prdcd = ";
		L_STRQRY += "'" + LP_PRDCD.trim() + "'";
		try
		{
			LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY(L_STRQRY,"CO","ACT");
			if(LM_RSLSET != null)
			while(LM_RSLSET.next())
			{
				L_STRTMP = LM_RSLSET.getString("PR_PRDDS");
				if(L_STRTMP != null)
					txtGRDCD.setText(L_STRTMP);
			}
		}
		catch(SQLException L_SE)
		{
            System.out.println("Exception     getROWCNT" + L_SE.toString());
			
		}
	}
	private void exeBTNRWK()
	{
		if(txtTPRCD.getText().trim().length() == 0)
		{
			setMSG("Enter or Select the Product Code from F1 List for Reworking the RUN Nos",'E'); 
		}
		else if(txtLINNO.getText().trim().length() == 0)
		{
			setMSG("Enter or Select the LINE No. from F1 List for Reworking the RUN Nos",'E'); 
		}
		else
		{
			txtLINNO.setEnabled(false);
			btnRWK.setEnabled(false);
			rwkRUNNO(txtTPRCD.getText().trim(),txtLINNO.getText().trim());
			
		}
	}
	private void exeRWALL()
	{
		ResultSet L_RSLSET;
		int L_CNT =0;
		String LM_LINNO ="";
		try
		{
			String L_STRQRY = "Select distinct LT_TPRCD,LT_LINNO from DT_LTMST ";
			//L_STRQRY += " where LT_CLSFL <> '8' and LT_PSTDT >= ";
			L_STRQRY += " where LT_PSTDT >= ";
			L_STRQRY += cc_dattm.occ_dattm.setDBSTM(txtDATE.getText().trim() + " 00:01");
			L_RSLSET = cl_dat.ocl_dat.exeSQLQRY(L_STRQRY,"QC","ACT");
			if(L_RSLSET !=null)
			while(L_RSLSET.next())
			{
				L_TPRCD = L_RSLSET.getString("LT_TPRCD");
				LM_LINNO = L_RSLSET.getString("LT_LINNO");
				setMSG(L_TPRCD + "/" +LM_LINNO,'N');
				L_CNT +=1;
				if(LM_LINNO !=null)
				  if(!LM_LINNO.trim().equals(""))
					  rwkRUNNO(L_TPRCD.trim(),LM_LINNO.trim());
			}
		}
		catch(SQLException L_SE)
		{
			System.out.println(L_SE.toString());
		}
	}
}