/*
System Name   : Laboratory Information Management System
Program Name  : pr_hkrrw
Program Desc. : Run No. Rework Procedure
Author        : N.K.Virdi
Date          : 11th aug 2005
Version       : LIMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        : 2.0

*/

import java.awt.event.*;
import java.sql.Date;import java.sql.Timestamp;import java.sql.ResultSet;import java.sql.SQLException;
import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JTextArea;import javax.swing.JComboBox;import javax.swing.JButton;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
public class pr_terrw extends cl_pbase
{
	
	private JTextField txtTPRCD,txtLINNO,txtGRDCD,txtDATE;
	private JTextArea lstDSPRUN;
    private JRadioButton rdoRWSPC,rdoRWALL;
    private JComboBox cmbPRDTP;
	private String[] arrHEADR = new String[3];
	private String strTPRCD ;
	private String strLINNO ;
	private String strRUNNO ;
	private String strPSTDT ;
	private String strPENDT ;
	private String strRWRUN ;
	private String strLOTNO ;
	private String strRCLNO ;
	private String strPRPEN ;
	private String strFSTRUN = "00001" ;
	private String strYRVAL ;
	private String strDSPSTR = "";
	private String strINTRCL ="00";
	private String strPRDTP;
	private int intROWCT =0;
	private boolean flgRWFLG = false;
	pr_terrw()
	{
		/**
		 * Run No =  8 digit No.
		 *   2 digits of Line No + Year Digit + 5 digit running serial for a particular grade
		 * For reworking of run no's year digit can be taken from last digit of lot start date.
		 */
        super(2);	  
	    setMatrix(20,8);
	    add(new JLabel("Rework Option"),4,2,1,1,this,'L');
	    add(rdoRWSPC = new JRadioButton("Specific",true),4,3,1,1,this,'L');
	    add(rdoRWALL = new JRadioButton("All",false),4,4,1,1,this,'L');
	    ButtonGroup bgrRWOPT = new ButtonGroup();
	    bgrRWOPT.add(rdoRWSPC);
	    bgrRWOPT.add(rdoRWALL); 
	   	add(new JLabel("Target Grade"),5,2,1,1,this,'L');
		add(txtTPRCD = new TxtLimit(10),5,3,1,1,this,'L');
		add(txtGRDCD = new TxtLimit(10),5,4,1,2,this,'L');
		add(new JLabel("Line No. "),6,2,1,1,this,'L');
		add(txtLINNO = new TxtLimit(2),6,3,1,1,this,'L');
		add(new JLabel("Year From "),6,4,1,1,this,'L');
		add(txtDATE = new TxtDate(),6,5,1,1,this,'L');
		add(new JLabel("LOT NO.        " + "Lot Start Date & Time        Lot end Date & Time   Run No."),7,2,1,5,this,'L');	
		lstDSPRUN = new JTextArea();
		add(lstDSPRUN ,8,2,6,5,this,'L');
		   add(new JLabel("Product Type "),3,2,1,1,this,'L');
		add(cmbPRDTP = new JComboBox(),3,3,1,2,this,'L');
		cmbPRDTP.addItem("01 PolyStyrene");	
	
		clrCOMP(); 
		setENBL(true);
	}
	void clrCOMP()
	{
		txtLINNO.setText("");
		txtTPRCD.setText("");
		txtGRDCD.setText("");
	}
	void setENBL(boolean P_flgSTAT)
	{
		txtLINNO.setEnabled(P_flgSTAT);
		txtTPRCD.setEnabled(P_flgSTAT);
		txtGRDCD.setEnabled(false);
	}
	private void rwkRUNNO(String P_strPRDCD,String P_strLINNO) 
	{
		strTPRCD = "";
		strLINNO = "";
		strRUNNO = "";
		strPSTDT = "";
		strPENDT = "";
		strRWRUN = "";
		strLOTNO = "";
		strRCLNO = "";
		strPRPEN = "";
		java.sql.Timestamp L_datTEMP;
		int L_CNT =0;
		try
		{
			strDSPSTR = "        ";
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			if(flgRWFLG)
			{
				if(txtDATE.getText().trim().length() == 10)
						strYRVAL = txtDATE.getText().trim().substring(9).trim();
			}
			else
			{
				strYRVAL = cl_dat.M_strLOGDT_pbst.substring(9).trim();
			}
			M_strSQLQRY = " Select LT_TPRCD,LT_LOTNO,LT_LINNO,LT_RCLNO,LT_RUNNO,LT_PSTDT,LT_PENDT ";
			M_strSQLQRY += "FROM PR_LTMST where ";
			M_strSQLQRY += " LT_PRDTP = " + "'" +strPRDTP+"'";
			M_strSQLQRY += " AND LT_TPRCD = " + "'" +P_strPRDCD.trim()+"'";
			M_strSQLQRY +=" AND LT_LINNO = "+ "'" +P_strLINNO.trim()+"'";
			M_strSQLQRY +=" AND LT_RCLNO = "+ "'" +strINTRCL.trim()+"'";
			M_strSQLQRY += " AND LT_PSTDT >= "; 
			M_strSQLQRY += M_fmtDBDTM.format(M_fmtDBDTM.parse(txtDATE.getText().trim() + " 00:01"));
			M_strSQLQRY +=" AND LT_CLSFL  "+ "<> '8'";
			M_strSQLQRY +=" ORDER BY LT_LOTNO";
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
		    String L_TMP="";
			L_CNT =0;
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				strLOTNO = M_rstRSSET.getString("LT_LOTNO");
				strRCLNO = M_rstRSSET.getString("LT_RCLNO");
				strTPRCD = M_rstRSSET.getString("LT_TPRCD");
				strLINNO = M_rstRSSET.getString("LT_LINNO");
				strRUNNO = M_rstRSSET.getString("LT_RUNNO");
				L_datTEMP= M_rstRSSET.getTimestamp("LT_PSTDT");
				if(L_datTEMP == null)
				{
					strPSTDT = "";
				}
				else
				    strPSTDT = M_fmtLCDTM.format(L_datTEMP);
                L_datTEMP= M_rstRSSET.getTimestamp("LT_PENDT");
				if(L_datTEMP == null)
				{
					strPENDT = "";
				}
				else
				    strPENDT = M_fmtLCDTM.format(L_datTEMP);
				    
				L_datTEMP = M_rstRSSET.getTimestamp("LT_PSTDT");
				if(L_datTEMP !=null)
				    L_TMP = padSTRING('R',M_fmtLCDTM.format(L_datTEMP),15);
				strDSPSTR = padSTRING('R',strLOTNO + " "+ strRCLNO,11) +padSTRING('R',"",6) +L_TMP +padSTRING('R',"",8);
				L_datTEMP = M_rstRSSET.getTimestamp("LT_PENDT");
				if(L_datTEMP !=null)
				     L_TMP = padSTRING('R',M_fmtLCDTM.format(L_datTEMP),15);
				strDSPSTR +=L_TMP;
				if(L_CNT ==0)
				{
					// changed as per the lot no series logic.(Line no and then yead digit))
					strRWRUN = P_strLINNO +strYRVAL + strFSTRUN;
					///strRWRUN = strYRVAL + P_strLINNO +strFSTRUN;
					if(strRUNNO != null)
					{
						if(!strRUNNO.trim().equals(strRWRUN))
						{
							updRWRUN(strLOTNO,strRWRUN);
						}
					}
					else
						updRWRUN(strLOTNO,strRWRUN);
					strDSPSTR += padSTRING('R'," ",8)+padSTRING('R',strRWRUN,8);
				}
				else if(L_CNT > 0)
				{
				    if(M_fmtLCDTM.parse(strPRPEN).compareTo(M_fmtLCDTM.parse(strPSTDT))==0)
					{
						updRWRUN(strLOTNO,strRWRUN);
					}
					else
					{
						String strTMPSTR = "";
						Integer L_INT = Integer.valueOf(strRWRUN);
						int L_intTEMP = L_INT.intValue();
						L_intTEMP = L_intTEMP +1;
						String L_strNXTRN = String.valueOf(L_intTEMP);
						for(int i=0;i<strRWRUN.length()-L_strNXTRN.length();i++)
						{
							strTMPSTR += "0";
						}
						strTMPSTR += L_strNXTRN;
						strRWRUN = strTMPSTR;
						updRWRUN(strLOTNO,strRWRUN);
					}
					strDSPSTR += padSTRING('R'," ",8)+ padSTRING('R',strRWRUN,8);
				}
				strPRPEN  = strPENDT;
				L_CNT += 1;
			}
			if(L_CNT == 0)
			{
				setMSG("No Lot exists with the given Grade for Line No "+strLINNO,'N');
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"rwkRUNNO");
		}
	}
	private void updRWRUN(String P_strLOTNO,String P_strRUNNO)
	{
		String L_strSQLQR = "";
		int L_UPDCNT =0;
		try
		{
			L_strSQLQR = "UPDATE PR_LTMST SET ";
            L_strSQLQR +="LT_RUNNO='"+P_strRUNNO.trim()+"',";			
            L_strSQLQR +=getUSGDTL("LT",'U',null);
			L_strSQLQR +=" WHERE LT_LOTNO = " + "'"+P_strLOTNO +"'";
			L_strSQLQR +=" AND LT_RCLNO ='"+strINTRCL +"'";
			L_strSQLQR +=" AND LT_CLSFL "+ "<> '8'";
			cl_dat.exeSQLUPD(L_strSQLQR,"setLCUPD");	
			if(cl_dat.M_flgLCUPD_pbst)
			{
				cl_dat.exeDBCMT("exeSAVE");
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"updRWRUN");
		}
	}
	void exeSAVE()
	{
        if(rdoRWALL.isSelected())	
        {
            flgRWFLG = true;    
            exeRWALL();
        }
        else
        {
            flgRWFLG = false;    
            exeBTNRWK();
        }
        
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		if(M_objSOURC == rdoRWALL)
		{
		    setENBL(false);
		    clrCOMP();
		}
		else if(M_objSOURC == rdoRWSPC)
		{
		    setENBL(true);
		}
		else if(M_objSOURC == txtDATE)
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		else if(M_objSOURC == txtLINNO)
		{
			if(!cl_dat.M_flgHELPFL_pbst)
			{
				if(chkLINNO())
				{
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else
				{
					txtDATE.setEnabled(true);
					txtDATE.requestFocus();
				}
			}
		}
		else if(M_objSOURC ==  txtTPRCD)
		{
			lstDSPRUN.setText("");
			if(!cl_dat.M_flgHELPFL_pbst)
			{
				if(!chkPRDCD())
				{
					txtDATE.setEnabled(true);
					txtDATE.requestFocus();
				}
				else
				{
					txtDATE.setEnabled(false);
					getPRDDS(txtTPRCD.getText().trim());
					txtLINNO.setEnabled(true);
				}
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		int i =0;
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			  try
			  {
				if(M_objSOURC == txtTPRCD)
				{
			    	strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
					cl_dat.M_flgHELPFL_pbst = true;
					arrHEADR[0] = "Product Code";
					arrHEADR[1] = "Line No.";	
					arrHEADR[2] = "Grade";	
					M_strSQLQRY = "Select distinct LT_TPRCD,LT_LINNO,PR_PRDDS from PR_LTMST,CO_PRMST WHERE LT_TPRCD = PR_PRDCD and PR_STSFL IN('1','2')";
					M_strSQLQRY +=" AND LT_PRDTP = " + "'"+strPRDTP + "'"+ " ORDER BY PR_PRDDS";
					M_strHLPFLD = "txtTPRCD";
					txtTPRCD.setEnabled(false);
					cl_hlp(M_strSQLQRY,3,1,arrHEADR,3,"CT");
				}
				else if(M_objSOURC == txtLINNO)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					//arrHEADR[0] = "Line No.";
					//arrHEADR[1] = "Description";	
					M_strSQLQRY = "SELECT cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp =  ";
					M_strSQLQRY = M_strSQLQRY + "'SYS'" + " AND cmt_cgstp = ";
					M_strSQLQRY = M_strSQLQRY + "'PRXXLIN'";
					M_strHLPFLD = "txtLINNO";
					txtLINNO.setEnabled(false);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Line No.","Description"},2,"CT");
				}
			  }
			catch(Exception L_E)
			{
			       setMSG("Error in getting help ...",'E');
			}
		}
		if(L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtTPRCD)
				txtLINNO.requestFocus();
			else
				L_KE.getComponent().transferFocus();
			
	    }
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		 if(M_strHLPFLD.equals("txtTPRCD"))
		   {
				txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;
				txtTPRCD.setEnabled(true);
				//txtLINNO.setText(cl_dat.M_strHELPST.substring(15,17));
				txtTPRCD.requestFocus();
			}
		   else if(M_strHLPFLD.equals("txtLINNO"))
		   {
				txtLINNO.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_flgHELPFL_pbst = false;
				txtLINNO.setEnabled(true);
				txtLINNO.requestFocus();
			}
	}
	private boolean chkLINNO()
	{
		String L_strSQLQR ="";
		if(txtLINNO.getText().trim().length()==0)
		{
			setMSG("Line No can not be Empty ..Enter some valid Line No ", 'E');
			return false;
		}	
		L_strSQLQR ="Select count(*) from CO_CDTRN where ";
		L_strSQLQR +=" CMT_CGMTP ='SYS' and CMT_CGSTP ='PRXXLIN'";
		L_strSQLQR +=" and CMT_CODCD ='"+txtLINNO.getText().trim()+"'";
	    intROWCT = cl_dat.getRECCNT(L_strSQLQR);
		if(intROWCT >0)
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
		strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
		M_strSQLQRY = "SELECT count(*)L_CNT from PR_LTMST,CO_PRMST where LT_TPRCD = PR_PRDCD AND LT_TPRCD = ";
		M_strSQLQRY = M_strSQLQRY + "'"+ txtTPRCD.getText().trim()+"'" + " AND PR_STSFL IN ('1','2')";
		M_strSQLQRY = M_strSQLQRY + " AND LT_PRDTP = " + "'"+ strPRDTP+"'";
		intROWCT = getROWCNT(M_strSQLQRY);
		if(intROWCT >0)
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
	private int getROWCNT(String P_STRQRY)
	{
		int L_ROWCNT = -1;
		try
		{
			M_rstRSSET = cl_dat.exeSQLQRY(P_STRQRY);
			if(M_rstRSSET != null)
			while(M_rstRSSET.next())
			{
				L_ROWCNT = M_rstRSSET.getInt(1);
			}
		}
		catch(Exception L_SE)
		{
           setMSG(L_SE,"getROWCT");
			return L_ROWCNT ;	
			
		}
		return L_ROWCNT;
	}
	private void getPRDDS(String P_strPRDCD)
	{
		String L_strSQLQR = "";
		String strTMPSTR = "";
		L_strSQLQR = "Select PR_PRDDS from co_prmst where pr_prdcd = ";
		L_strSQLQR += "'" + P_strPRDCD.trim() + "'";
		try
		{
			M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQR);
			if(M_rstRSSET != null)
			while(M_rstRSSET.next())
			{
				strTMPSTR = M_rstRSSET.getString("PR_PRDDS");
				if(strTMPSTR != null)
					txtGRDCD.setText(strTMPSTR);
			}
		}
		catch(SQLException L_SE)
		{
           setMSG(L_SE,"getPRDDS");
			
		}
	}
	private void exeBTNRWK()
	{
		try
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
    			//btnRWK.setEnabled(false);
    			rwkRUNNO(txtTPRCD.getText().trim(),txtLINNO.getText().trim());
    			
    		}
		}
		catch(Exception L_E)
		{
		    setMSG(L_E,"exeBTNRWK");
		}
		
	}
	private void exeRWALL()
	{
		ResultSet L_rstRSSET;
		int L_intCNT =0;
		String LM_LINNO ="";
		try
		{
			String L_strSQLQR = "Select distinct LT_TPRCD,LT_LINNO from PR_LTMST ";
			//L_strSQLQR += " where LT_CLSFL <> '8' and LT_PSTDT >= ";
			L_strSQLQR += " where LT_PSTDT >= ";
			L_strSQLQR += M_fmtDBDTM.format(M_fmtDBDTM.parse(txtDATE.getText().trim() + " 00:01"));
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQR);
			if(L_rstRSSET !=null)
			while(L_rstRSSET.next())
			{
				strTPRCD = L_rstRSSET.getString("LT_TPRCD");
				LM_LINNO = L_rstRSSET.getString("LT_LINNO");
				setMSG(strTPRCD + "/" +LM_LINNO,'N');
				L_intCNT +=1;
				if(LM_LINNO !=null)
				  if(!LM_LINNO.trim().equals(""))
					  rwkRUNNO(strTPRCD.trim(),LM_LINNO.trim());
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"exeRWALL");
		}
	}
}