/*
System Name   : Material Management System
Program Name  : List Of Pending Returnable Gate Pass
Program Desc. : 
Author        : A.T.Chaudhari
Date          : 13/05/2004
Version       : MMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.sql.ResultSet;
import java.io.IOException;

class mm_rppgp extends cl_rbase
{	/**JTextField for Date*/
	private JTextField txtONDAT;		/**JTextField For Departmetnt Code*/
	private JTextField txtDPTCD;		/**JTextField For Departmetnt Name*/
	private JTextField txtDPTNM;		/**Radio Button For All Department*/
	private JRadioButton rdbDPTAL;		/**Radio Button For Specific Department*/
	private JRadioButton rdbDPTSP;		/**Radio Button For Over Due Report Option*/
	private JRadioButton rdbOVRDU;		/**Radio Button For Pending Report Option*/
	private JRadioButton rdbPENDI;		/**Button Group for Department Selection*/
	private ButtonGroup btgDPTSL;		/**Button Group for Report Option*/
	private ButtonGroup btgRPTOP;		
	
	private String strDPTCD;	//Departmetn Code
	private String strDPTNM;	//Departmetn Name
	private String strDUEDT;	//Due Date 
	private String strSTRTP;	//Store Type
	private String strSTRNM;	//Store Name
	private String strMGPNO;	//Mane Gate Pass Number
	private String strMATCD;	//Material Code
	private char chrDATA;		//Flag For Date in Database
	
	private FileOutputStream F_OUT ;   
    private DataOutputStream D_OUT ;
	
	private final String strFILNM = "C:\\Reports\\mm_rppgp.doc";
	
	public mm_rppgp()
	{
		super(2);
		chrDATA = 'N';
		M_intPAGNO = 1;
		M_intLINNO = 0;
		rdbDPTAL = new JRadioButton("All",true);	
		rdbDPTSP = new JRadioButton("Specific",false);
		btgDPTSL = new ButtonGroup();	//Button Group for Department Selection
		btgDPTSL.add(rdbDPTAL);		
		btgDPTSL.add(rdbDPTSP);
		
		rdbPENDI = new JRadioButton("Pending",true);
		rdbOVRDU = new JRadioButton("OverDue",false);
		btgRPTOP = new ButtonGroup();
		btgRPTOP.add(rdbPENDI);
		btgRPTOP.add(rdbOVRDU);
		
				
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		
		setMatrix(20,8);
		add(new JLabel("Report Type"),2,3,1,1,this,'L');
		add(rdbPENDI,2,4,1,1,this,'L');
		add(rdbOVRDU,2,5,1,1,this,'L');
		add(new JLabel("Pending On Dt. "),3,3,1,1,this,'L');
		add(txtONDAT = new TxtDate(),3,4,1,1,this,'L');
		add(new JLabel("Department "),4,3,1,1,this,'L');
		add(rdbDPTAL,4,4,1,1,this,'L');
		add(rdbDPTSP,4,5,1,1,this,'L');
		add(txtDPTCD = new TxtNumLimit(3.0),5,4,1,1,this,'L');
		add(txtDPTNM = new JTextField(),5,5,1,2.3,this,'L');
		
		txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
		
		txtDPTCD.setInputVerifier(new INPVF());
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);
				txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
				rdbPENDI.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
				txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
			{
				txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
				rdbPENDI.requestFocus();
			}
		}
		if(M_objSOURC == rdbDPTSP)
		{
			txtDPTCD.setEnabled(true);
			txtDPTCD.requestFocus();
			if(txtONDAT.getText().trim().length() ==0)
				txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
			setMSG("Enter Departmetn Code Or Press F1 For Help..",'N');
		}
		if(M_objSOURC == rdbDPTAL)
		{
			txtDPTCD.setText("");
			txtDPTNM.setText("");
			if(txtONDAT.getText().trim().length() == 0)
				txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtDPTCD.setEnabled(false);
		}
		if(M_objSOURC == rdbPENDI)
		{
			if(txtONDAT.getText().trim().length() ==0)
				txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		if(M_objSOURC == rdbOVRDU)
		{
			if(txtONDAT.getText().trim().length() ==0)
				txtONDAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		setMSG("",'N');
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == rdbPENDI)
			{
				rdbOVRDU.requestFocus();
			}
			if(M_objSOURC == rdbOVRDU)
			{
				txtONDAT.requestFocus();
				setMSG("Enter Date..",'N');
			}
			if(M_objSOURC == txtONDAT)
			{
				if(txtONDAT.getText().trim().length() > 0)
				{
					rdbDPTAL.requestFocus();
				}
				else
				{
					txtONDAT.requestFocus();
					setMSG("Enter Date..",'E');
				}
			}
			if(M_objSOURC == rdbDPTAL)
			{
				rdbDPTSP.requestFocus();
			}
			if(M_objSOURC == rdbDPTSP)
			{
				if(rdbDPTSP.isSelected())
				{
				   txtDPTCD.requestFocus();
				   setMSG("Enter Departmetn Code Or Press F1 For Help..",'N');
				}
				else
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			if(M_objSOURC == txtDPTCD)
			{
				if(txtDPTCD.getText().trim().length() > 0)
				{
					try
					{
						M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
							+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' and "
							+"ifnull(cmt_stsfl,' ') <>'X' and CMT_CODCD='"+txtDPTCD.getText()+"'";
					
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
						if(L_rstRSSET.next())
						cl_dat.M_btnSAVE_pbst.requestFocus();
						else
						{
							txtDPTCD.requestFocus();
							setMSG("Invalid Department Code..",'E');
						}
					}
					catch(SQLException L_SQL)
					{
						setMSG(L_SQL,"keyPressed,txtDPTCD");
					}
				}
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtDPTCD)
			{
				M_strHLPFLD = "txtDPTCD";
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
					+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' AND ";
				if(txtDPTCD.getText().trim().length() > 0)
					M_strSQLQRY += "CMT_CODCD LIKE '"+txtDPTCD.getText().trim()+"%' AND ";
				M_strSQLQRY += "ifnull(CMT_STSFL,' ') <>'X' ORDER BY CMT_CODDS";

				cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code","Name"},2,"CT");
			}
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtDPTCD"))
		{
			txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtDPTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	public void setENBL(boolean blnSTRING)
	{
		super.setENBL(blnSTRING);
		txtDPTNM.setEnabled(false);
		txtONDAT.setEnabled(true);
		rdbDPTAL.setEnabled(true);
		rdbDPTSP.setEnabled(true);
		rdbOVRDU.setEnabled(true);
		rdbPENDI.setEnabled(true);
		txtDPTCD.setEnabled(true);
	}
	/** Method For Validate Input
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtONDAT.getText().trim().length() == 0)
			{
				txtONDAT.requestFocus();
				setMSG("Enter Date..",'E');
				return false;
			}
			if(M_fmtLCDAT.parse(txtONDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Curretn Date..",'E');
				txtONDAT.requestFocus();
				return false;
			}
			if(rdbDPTSP.isSelected())
			{
				if(txtDPTCD.getText().trim().length() == 0){
					txtDPTCD.requestFocus();
					setMSG("Enter Department Code Or Press F1 For Help..",'E');
					return false;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	/** Method To generate Report File
	 */
	public void getRPTFL()
	{
		try
		{
			F_OUT = new FileOutputStream(strFILNM);
			D_OUT = new DataOutputStream(F_OUT);
			prnFMTCHR(D_OUT,M_strNOCPI17);
			prnFMTCHR(D_OUT,M_strCPI12);
		}
		catch(IOException L_IO)
		{
			setMSG(L_IO,"getRPTFL");
		}
		strSTRTP = M_strSBSCD.substring(2,4);
		java.sql.Date jdtTEMP;
		String L_strDPTCD = "";
		String L_strMGPNO = "";
		
		try
		{
			getRPHDR();		//Call Header Hethod
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY("SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSST' AND CMT_CODCD = '"+strSTRTP+"'");
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
					strSTRNM = L_rstRSSET.getString("CMT_CODDS").toString();
				L_rstRSSET.close();
			}
			//Convert Local Date To Database Format
			strDUEDT = txtONDAT.getText().trim().toString().substring(3,5);
			strDUEDT += "/";
			strDUEDT += txtONDAT.getText().trim().toString().substring(0,2);
			strDUEDT += "/";
			strDUEDT += txtONDAT.getText().trim().toString().substring(6,10);
			
			//Query For Report 
			M_strSQLQRY = "SELECT GP_MGPNO,GP_MGPDT,GP_VENCD,GP_VENNM,GP_DUEDT,GP_ISSQT,"
				+"GP_RECQT,GP_MATCD,";
			
			if(rdbPENDI.isSelected())
				M_strSQLQRY += "(days(CURRENT_DATE) - days(GP_MGPDT)) PDAY,";	//Pending Days
			if(rdbOVRDU.isSelected())
				M_strSQLQRY += "(days(CURRENT_DATE) - days(GP_DUEDT)) PDAY,";	//Days OverDue
			
			M_strSQLQRY += "GP_DPTCD,CT_MATDS FROM MM_GPTRN,CO_CTMST WHERE "
				+"CT_MATCD = GP_MATCD AND GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+strSTRTP+"' AND GP_MGPTP = '451' AND ";
			
			if(rdbDPTSP.isSelected())
				M_strSQLQRY += "GP_DPTCD = '"+txtDPTCD.getText().trim()+"' AND ";	//Specific Department
			
			M_strSQLQRY += "ifnull(GP_ISSQT,0) - ifnull(GP_RECQT,0) > 0 AND ifnull(GP_STSFL,' ') <> 'X' ";
			
			if(rdbOVRDU.isSelected())
				M_strSQLQRY += " AND (days(CURRENT_DATE) - days(GP_DUEDT)) >0 " ;
			
			M_strSQLQRY += "AND ifnull(CT_STSFL,'') <> 'X' ORDER BY GP_DPTCD,PDAY";
			
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET != null){
				while(M_rstRSSET.next())
				{
					chrDATA = 'Y';		//Flag For if date is Present
					strDPTNM = "";
					strDPTCD = nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),"");
					
					if(M_intLINNO >60)
					{
						D_OUT.writeBytes("\n");
						D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
						M_intLINNO = 0;
						M_intPAGNO += 1;
						prnFMTCHR(D_OUT,M_strEJT);
						L_strDPTCD = "";
						L_strMGPNO = "";
						getRPHDR();
					}
					/**Print Deparrtmet Name If UnEqual to Privious Department name
					 */
					if(!strDPTCD.equals(L_strDPTCD))
					{
						L_strDPTCD = strDPTCD;
						String L_strSQLQRY = "SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
							+"CMT_CGSTP = 'COXXDPT' AND CMT_CODCD = '"+strDPTCD+"'";
						L_rstRSSET = cl_dat.exeSQLQRY3(L_strSQLQRY);
						if(L_rstRSSET.next())
						{
							strDPTNM = nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"");
						}
						L_rstRSSET.close();
						D_OUT.writeBytes("\n");
						prnFMTCHR(D_OUT,M_strBOLD);
						D_OUT.writeBytes("Department : "+strDPTNM);
						prnFMTCHR(D_OUT,M_strNOBOLD);
						D_OUT.writeBytes("\n");
						M_intLINNO += 2;
					}
					/**Print Gate pass No If not Equal to Previous one
					 */
					strMGPNO = nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),"");
					if(!strMGPNO.equals(L_strMGPNO))
					{
						L_strMGPNO = strMGPNO;
						D_OUT.writeBytes("\n");
						prnFMTCHR(D_OUT,M_strBOLD);
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MGPNO"),""),10));
						jdtTEMP =  M_rstRSSET.getDate("GP_MGPDT");
						if(jdtTEMP != null)
							D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(jdtTEMP),11));
						else
							D_OUT.writeBytes(padSTRING('R',"",11));
					
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),""),6));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),""),38));
					
						jdtTEMP = M_rstRSSET.getDate("GP_DUEDT");
						if(jdtTEMP != null)
							D_OUT.writeBytes(padSTRING('L',M_fmtLCDAT.format(jdtTEMP),15));
						else
							D_OUT.writeBytes(padSTRING('L',"",15));
					
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PDAY"),""),15));
						prnFMTCHR(D_OUT,M_strNOBOLD);
						D_OUT.writeBytes("\n\n");
						M_intLINNO += 3;
					}
					D_OUT.writeBytes(padSTRING('R',"",1));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),11));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),55));
					if(rdbPENDI.isSelected())
					{
						D_OUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0")),3),13));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0"),15));
					}
					else
					{
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0"),13));
						D_OUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0")),3),15));
						
					}
					D_OUT.writeBytes("\n");										
					M_intLINNO += 1;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPTFL");
		}
		
		try
		{
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
		}
		catch(IOException L_IO)
		{
			setMSG(L_IO,"End ");
		}
		try
		{
			D_OUT.close();
			F_OUT.close();
			M_intLINNO = 0;
			M_intPAGNO = 1;
			strMGPNO = "";
			strDPTCD = "";
		}
		catch(Exception L_RP)
		{
			setMSG(L_RP,"genRPFIL");
		}
	}
	/**Method To Print The Header For The Report
	 */
	public void getRPHDR()
	{
		strSTRTP = M_strSBSCD.substring(2,4);
		try
		{
			/**For Store Name
			 */
			ResultSet L_rstTEM = cl_dat.exeSQLQRY3("SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSST' AND CMT_CODCD = '"+strSTRTP+"'");
			if(L_rstTEM !=null)
			{
				if(L_rstTEM.next())
					strSTRNM = L_rstTEM.getString("CMT_CODDS").toString();
				L_rstTEM.close();
			}
		}
		catch(SQLException L_SQL)
		{
			setMSG(L_SQL,"STRNM");
		}
		try
		{
			prnFMTCHR(D_OUT,M_strBOLD);
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,96));
			D_OUT.writeBytes("\n");
			if(rdbPENDI.isSelected())
				D_OUT.writeBytes(padSTRING('R',"LIST OF PENDING RETURNABLE GATE PASSES AS ON "+txtONDAT.getText().trim().toString(),76));
			else
				D_OUT.writeBytes(padSTRING('R',"LIST OF OVERDUE RETURNABLE GATE PASSES AS ON "+txtONDAT.getText().trim().toString(),76));
			D_OUT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,76));
			D_OUT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
			prnFMTCHR(D_OUT,M_strNOBOLD);
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
			D_OUT.writeBytes("\n");
			if(rdbPENDI.isSelected())
				D_OUT.writeBytes("GP No     Date       Vendor Code And Name                          Expected Date   Pending Days");
			else
				D_OUT.writeBytes("GP No     Date       Vendor Code And Name                          Expected Date   Overdue Days");
			D_OUT.writeBytes("\n");
			if(rdbPENDI.isSelected())
				D_OUT.writeBytes("Material Code & Description                                       Qty. To Receive  Received Qty");
			else
				D_OUT.writeBytes("Material Code & Description                                        Gate Pass Qty.   Pending Qty");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
			M_intLINNO += 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	public void exePRINT()
	{
		setMSG("",'N');
		if(vldDATA())
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generatting Report...",'N');
			getRPTFL();
			if(chrDATA == 'Y')
			{
				chrDATA = 'N';
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					doPRINT(strFILNM);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
					try
					{
						Runtime r = Runtime.getRuntime();
						Process p = null;
						p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"Error.exescrn.. ");
					}
				}
				setMSG("",'N');
				txtONDAT.setText("");
				txtDPTCD.setText("");
				txtDPTNM.setText("");
			}
			else
				setMSG("Record Not Found",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**Method To Check Departmetn Code
	 */
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length()==0)
						return true;
				if(input == txtDPTCD)
				{
					if(txtDPTCD.getText().length() < 3)
						return true;
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
						+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' and "
						+"ifnull(cmt_stsfl,' ') <>'X' and CMT_CODCD='"+txtDPTCD.getText()+"'";
					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
					
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							txtDPTNM.setText(L_rstRSSET.getString("CMT_CODDS"));
							L_rstRSSET.close();
							return true;
						}	
						else
						{
							setMSG("Invalid Department Code ..",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Department Code ..",'E');
						return false;
					}
				}
				return true;
			}
			catch (Exception e)
			{
				setMSG(e,"Chlid.INPVF");
				return false;
			}
		}
	}
}