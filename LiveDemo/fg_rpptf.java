/*
System Name   : Finished Goods Inventory Management System
Program Name  : Statement of PTF Generation
Program Desc. : Provides a Statement by which PTF can be generated.
Author        : Mr S.R.Mehesare
Date          : 4th Jan 2006
Version       : FIMS V2.0.0

Modificaitons
Modified By	  :
Modified Date :
Modified det. :
Version       :
*/

import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JOptionPane;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.util.Hashtable;

/**<pre>
System Name : Finished Goods Inventory Management System.
 
Program Name : PTF Statement

Purpose : This module provides grade wise information of lots that are transferred
        from unclassified stock to classified stock for despatch purpose.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
FG_PTFRF       PTF_PTFNO, PTF_PRDTP, PTF_LOTNO
               PTF_RCLNO, PTF_PKGTP                                    #
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              # 
CO_PRMST       PR_PRDCD                                                #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name          Table name      Type/Size        Description
--------------------------------------------------------------------------------------
txtPTFNO    PTF_PTFNO            FG_PTFRF        VARCHAR(4)       PTF Number
txtPTFDT    PTF_PTFDT            FG_PTFRF        DATE             PTF Number
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from from FG_PTFRF and PR_LTMST for condiations :-
   1) PTF_PTFNO = given PTF Number.
   2) AND PTF_PRDTP = LT_PRDTP
   3) AND PTF_LOTNO = LT_LOTNO
   4) AND PTF_RCLNO = LT_RCLNO
   5) group by SUBSTRING(PTF_PRDCD,1,4),PTF_PRDCD,PTF_OPRCD,PTF_PTFCT,PTF_LOTNO,PTF_RCLNO,
        PTF_PKGTP,LT_REMDS"
   6) order by SUBSTRING(PTF_PRDCD,1,4),PTF_PRDCD,PTF_LOTNO,PTF_RCLNO,PTF_PKGTP,LT_REMDS
 
Validations :
    - PTF Number Entered must be valid.
*/

public class fg_rpptf extends cl_rbase 
{									/** JTextField to display & to enter the PTF Number.*/
	private JTextField txtPTFNO;	/** JTextField to display the PTF Date.*/
	private JTextField txtPTFDT;	/** JOptionPane to display message to forward the Report through Email.*/
	private JOptionPane jopMSG;		/** String variable for generated Report File Name.*/
	private String strFILNM;		/** String variable for the ISO number of the Report.*/
	private String strISO1 = "";	/** String variable for the ISO number of the Report.*/
	private String strISO2 = "";	/** String variable for the ISO number of the Report.*/
	private String strISO3 = "";	/** Integer variable to count the number of records fetched.*/
	private int intRECCT;			/** Integer variable to count the number of classified Bags.*/
	private int intCLSPK = 0;		/** Hashtable to maintain the Product Group with Description.*/	
	private Hashtable<String,String> hstPRDGR;		/** Hashtable to maintain the Package Type with Quantity releated to the Package Type.*/
	private Hashtable<String,String> hstPKGTP;		/** Hashtable to maintain the Package Type with Description.*/
	private Hashtable<String,String> hstPKGDS;		/** Hashtable to maintain the Product Code with Description.*/
	private Hashtable<String,String> hstPRDDS;			/** FileOutputStream Object to generate the report File from the Stream of Data.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream Object to hld the report data in the Stream to generate the Report.*/
    private DataOutputStream dosREPORT; /** String varaible for PTF Number entered.*/
	private String strPTFNO ;			/** String variable to print the dotted Line in the Report.*/
	private String strDOTLN = "    --------------------------------------------------------------------------------------";		
	private String strPTFDT = "";
	
	private String strCALL = "No";
	fg_rpptf()
	{	
		super(2);
		try
		{
			strCALL = "No";
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
		
			add(new JLabel("PTF Number"),4,3,1,.8,this,'R');
			add(txtPTFNO = new TxtNumLimit(5),4,4,1,1,this,'L');
			
			add(new JLabel("PTF Date"),5,3,1,.8,this,'R');
			add(txtPTFDT = new TxtDate(),5,4,1,1,this,'L');
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
			hstPRDGR = new Hashtable<String,String>();
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,4)CODCD,CMT_CODDS from CO_CDTRN where"
				+" CMT_CGMTP = 'MST'"
				+" AND CMT_CGSTP = 'COXXPGR'"
				+" AND CMT_CCSVL = 'SG'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstPRDGR.put(nvlSTRVL(M_rstRSSET.getString("CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}			
			hstPKGTP = new Hashtable<String,String>();
			hstPKGDS = new Hashtable<String,String>();
			M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_SHRDS from CO_CDTRN where"
				+" CMT_CGMTP = 'SYS'"
				+" AND CMT_CGSTP = 'FGXXPKG'";				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),""));
					hstPKGDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));
				}
				M_rstRSSET.close();
			}
			hstPRDDS = new Hashtable<String,String>();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
	}   
	fg_rpptf(String P_strSBSCD,String P_strPTFNO, String P_strPTFDT)
	{	
		try
		{
			strCALL = "Yes";
		    M_strSBSCD = P_strSBSCD;
			cl_dat.M_cmbOPTN_pbst.setSelectedItem(cl_dat.M_OPSCN_pbst);
			strPTFNO = P_strPTFNO;
			strPTFDT = P_strPTFDT;
			M_rdbHTML.setSelected(true);		    
		    hstPRDGR = new Hashtable<String,String>();
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,4)CODCD,CMT_CODDS from CO_CDTRN where"
				+" CMT_CGMTP = 'MST'"
				+" AND CMT_CGSTP = 'COXXPGR'"
				+" AND CMT_CCSVL = 'SG'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstPRDGR.put(nvlSTRVL(M_rstRSSET.getString("CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}			
			hstPKGTP = new Hashtable<String,String>();
			hstPKGDS = new Hashtable<String,String>();
			M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_SHRDS from CO_CDTRN where"
				+" CMT_CGMTP = 'SYS'"
				+" AND CMT_CGSTP = 'FGXXPKG'";				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),""));
					hstPKGDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));
				}
				M_rstRSSET.close();
			}
			hstPRDDS = new Hashtable<String,String>();
		}
		catch(Exception L_E)
		{
		    setMSG(L_E,"fg_rpptf");    
		}

    }
	/**
	 * Super Class method overrided to enable & disable the Components.
	 * @param L_flgSTAT boolean argument to pass State of the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		txtPTFDT.setEnabled(false);
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
					txtPTFNO.requestFocus();
					setMSG("Please Enter PTF Number..",'N');							
					if(txtPTFNO.getText().trim().length()!= 5)
					{
						M_strSQLQRY = "Select distinct PTF_PTFNO,PTF_PTFDT from FG_PTFRF where PTF_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ptf_ptfno like '"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+"%' order by PTF_PTFNO desc";
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								strPTFNO = M_rstRSSET.getString("PTF_PTFNO").trim();
								txtPTFNO.setText(strPTFNO);
								java.sql.Date L_tmpDATE = M_rstRSSET.getDate("PTF_PTFDT");
								if(L_tmpDATE != null)					
									txtPTFDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							}
							M_rstRSSET.close();
						}
					}
				}
				else 
					setENBL(false);
			}
			if(M_objSOURC == txtPTFNO)
			{
				if(txtPTFNO.getText().trim().length() == 5)
				{
					/*M_strSQLQRY = "Select PTF_PTFDT from FG_PTFRF where PTF_PTFNO = '"+ txtPTFNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{	
							java.sql.Date L_tmpDATE = M_rstRSSET.getDate("PTF_PTFDT");
							if(L_tmpDATE != null)					
								txtPTFDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							cl_dat.M_btnSAVE_pbst.requestFocus();
						}
						M_rstRSSET.close();
					}*/
					txtPTFDT.setText(getPTFDT(txtPTFNO.getText().trim()));
				}
				else
					setMSG("Please Enter Valid PTF Number..",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionP");
		}
	}		
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtPTFNO)
			setMSG("Please Enter PTF Number..",'N');
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{				
				if(M_objSOURC == txtPTFNO)
				{
					M_strHLPFLD = "txtPTFNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY = "Select distinct PTF_PTFNO,PTF_PTFDT from FG_PTFRF where PTF_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ptf_ptfno like '"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+"%'  order by PTF_PTFNO desc";
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"PTF Number","PTF Date"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
		}
		else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if((M_objSOURC == txtPTFNO) &&(txtPTFNO.getText().trim().length() == 5))
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}		
		}			
	}	
	/**
	* Super class Method overrrided to execuate the F1 Help.
	*/
	public void exeHLPOK()
	{
		try
		{
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtPTFNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtPTFNO.setText(cl_dat.M_strHLPSTR_pbst);				
				txtPTFDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	* Method to generate the Report & to forward it to specified destination.
	*/
	public void exePRINT()
	{
		try
		{
			getDATA();
			if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Statement of PTF Generation"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to validate the data before execuation of the SQL Quary.
	 */
	boolean vldDATA()
	{
		try
		{
			if(strCALL.equals("No"))				
				strPTFNO = txtPTFNO.getText().trim();
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"fg_rpptf.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"fg_rpptf.doc";	

			if(strPTFNO.length() == 0)
			{
				//txtPTFNO.requestFocus();
				setMSG("Please Enter PTF Number to generate the Report..",'E');
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
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA()
	{
		try
		{
			if(!vldDATA())
				return;	
			
			intRECCT = 0; 
			String L_strTEMP = "";
			String L_strSBGRP="",L_strLOTNO="",L_strPRDCD="",L_strRCLNO="";
			String L_strOPRCD = "",L_strPKGTP="",L_strPTFCT="",L_strREMDS="";			
			String L_strPKGWT="";
			String L_strOSBGRP="";
			double L_dblCLSQT=0,L_dblTOQTY=0,L_dblTTOQTY=0; // Quantity
			int L_intTOBAG=0,L_intTTOBAG=0;    // No of Bags			
			
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Product Transfer Form</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
				
			prnHEADER();
				
			L_strOSBGRP = "";
						
			M_strSQLQRY = "Select distinct PTF_PRDCD,PR_PRDDS from FG_PTFRF,CO_PRMST where"
				+" PTF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PTF_PTFNO = '"+strPTFNO+"'"
				+" AND PTF_PRDCD = PR_PRDCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstPRDDS.put(nvlSTRVL(M_rstRSSET.getString("PTF_PRDCD"),""),nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Select SUBSTRING(PTF_PRDCD,1,4) L_SBPRD,PTF_PRDCD,isnull(PTF_OPRCD,' ') PTF_OPRCD, PTF_PTFCT,PTF_LOTNO,PTF_RCLNO,"
				+"LT_REMDS,PTF_PKGTP,sum(PTF_PTFQT) L_CLSQT from FG_PTFRF,PR_LTMST where"
				+" PTF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PTF_PTFNO = '"+strPTFNO+"'"
				+" AND PTF_CMPCD = LT_CMPCD and PTF_PRDTP = LT_PRDTP"
				+" AND PTF_LOTNO = LT_LOTNO"
				+" AND PTF_RCLNO = LT_RCLNO"
				+" group by SUBSTRING(PTF_PRDCD,1,4),PTF_PRDCD,PTF_OPRCD,PTF_PTFCT,PTF_LOTNO,PTF_RCLNO,PTF_PKGTP,LT_REMDS"
				+" order by SUBSTRING(PTF_PRDCD,1,4),PTF_PRDCD,PTF_LOTNO,PTF_RCLNO,PTF_PKGTP,LT_REMDS";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{	
					intRECCT = 1; 
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}					
					L_strSBGRP = nvlSTRVL(M_rstRSSET.getString("L_SBPRD"),"");
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("PTF_LOTNO"),"");
					L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("PTF_RCLNO"),"");
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("PTF_PRDCD"),"");
					L_strOPRCD = nvlSTRVL(M_rstRSSET.getString("PTF_OPRCD"),"");
					L_strPKGTP = nvlSTRVL(M_rstRSSET.getString("PTF_PKGTP"),"");
					L_strPTFCT = nvlSTRVL(M_rstRSSET.getString("PTF_PTFCT"),"");
					L_strREMDS = nvlSTRVL(M_rstRSSET.getString("LT_REMDS"),"");
					L_dblCLSQT = M_rstRSSET.getDouble("L_CLSQT");
					intCLSPK = 0;
					if(hstPKGTP.containsKey(L_strPKGTP))
					{
						L_strPKGWT = hstPKGTP.get(L_strPKGTP).toString();
						intCLSPK = Integer.parseInt(String.valueOf(Math.round(L_dblCLSQT/Double.parseDouble(L_strPKGWT))));
					}
					if(!L_strSBGRP.equals(L_strOSBGRP))
					{
						if(!L_strOSBGRP.equals(""))
						{
							L_dblTTOQTY += L_dblTOQTY;
							L_intTTOBAG += L_intTOBAG;
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
								prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes("\n              Sub Total :      "+padSTRING('L',String.valueOf(L_intTOBAG).toString(),14)+padSTRING('L',setNumberFormat(L_dblTOQTY,3),10));	
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							L_dblTOQTY = 0;
							L_intTOBAG = 0;
						}
						if(hstPRDGR.containsKey(L_strSBGRP))
							L_strTEMP = hstPRDGR.get(L_strSBGRP).toString();
						else 
							L_strTEMP = "";
						
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
							prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes("\n    "+L_strTEMP);					
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						
						L_strOSBGRP = L_strSBGRP;
					}
					dosREPORT.writeBytes("\n    ");
					dosREPORT.writeBytes(padSTRING('R',L_strLOTNO,10));					
					if(hstPRDDS.containsKey(L_strPRDCD))
						L_strTEMP = hstPRDDS.get(L_strPRDCD).toString();
					else 
						L_strTEMP = "-";
					dosREPORT.writeBytes(padSTRING('R',L_strTEMP,12));
					if(hstPKGDS.containsKey(L_strPKGTP))
						L_strTEMP = hstPKGDS.get(L_strPKGTP).toString();
					else 
						L_strTEMP = "-";					
					dosREPORT.writeBytes(padSTRING('L',L_strTEMP,9));//pkgwt
					
					L_dblTOQTY += L_dblCLSQT;
					L_intTOBAG += intCLSPK;
					
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(intCLSPK).toString(),10));//Bags
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCLSQT,3),10)+"   ");//Quantity					
					dosREPORT.writeBytes(padSTRING('R',L_strREMDS,32));//remds
				}
				M_rstRSSET.close();
			}
			L_dblTTOQTY += L_dblTOQTY;
			L_intTTOBAG += L_intTOBAG;
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("\n              Sub Total :        "+padSTRING('L',String.valueOf(L_intTOBAG).toString(),12)+padSTRING('L',setNumberFormat(L_dblTOQTY,3),10));
			dosREPORT.writeBytes("\n\n    Grand Total :    "+padSTRING('L',String.valueOf(L_intTTOBAG).toString(),24)+padSTRING('L',setNumberFormat(L_dblTTOQTY,3),10));	
							
			dosREPORT.writeBytes("\n"+strDOTLN);
			dosREPORT.writeBytes("\n\n\n\n\n");
			dosREPORT.writeBytes("    CHIEF EXECUTIVE (OPERATIONS)                                           HOD (QCA)");
			dosREPORT.writeBytes("\n\n    cc:ED.(Styrenics)/MHD/QCA");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n"+strDOTLN);
			
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    									///<P CLASS = \"breakhere\">
			dosREPORT.close();
			fosREPORT.close();
			
			int L_intSELOPT = jopMSG.showConfirmDialog(this,"Do you want to mail the report?","E-Mail Status",JOptionPane.YES_NO_OPTION);
			if(L_intSELOPT == 0)
			{
				cl_eml ocl_eml = new cl_eml();
				M_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='EML' AND CMT_CGSTP = 'QCXXPTF'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					String L_strEML = "";
					while(M_rstRSSET.next())
					{						
						L_strEML = M_rstRSSET.getString("CMT_CODDS");
						ocl_eml.sendfile(L_strEML,cl_dat.M_strREPSTR_pbst+"fg_rpptf.html","PTF Statement for PTF Number "+strPTFNO,"PTF Statement");
					}
					M_rstRSSET.close();
				}
				setMSG("Report has been mailed succesfully",'N');																
			}		
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
	}
	/**
	 * Method to generate Header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			if(strISO1.length() == 0)
			{
				String L_strTEMP = "";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY0("SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'ISO' AND CMT_CGSTP = 'FGXXRPT'");				
				if(L_rstRSSET !=null)
				{					
					while(L_rstRSSET.next())
					{
						L_strTEMP = L_rstRSSET.getString("CMT_CODCD").toString();
						if(L_strTEMP.equals("FG_RPPTF01"))
							strISO1= L_rstRSSET.getString("CMT_CODDS").toString();
						else if(L_strTEMP.equals("FG_RPPTF02"))
							strISO2 = L_rstRSSET.getString("CMT_CODDS").toString();
						else if(L_strTEMP.equals("FG_RPPTF03"))
							strISO3 = L_rstRSSET.getString("CMT_CODDS").toString();
					}
					L_rstRSSET.close();
				}
			}
			dosREPORT.writeBytes("\n"+padSTRING('L',"-------------------------------",strDOTLN.length()));
			dosREPORT.writeBytes("\n"+padSTRING('L',strISO1,strDOTLN.length()));
			dosREPORT.writeBytes("\n"+padSTRING('L',strISO2,strDOTLN.length()));
			dosREPORT.writeBytes("\n"+padSTRING('L',strISO3,strDOTLN.length()));
			dosREPORT.writeBytes("\n"+padSTRING('L',"-------------------------------",strDOTLN.length()));
			if(M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("</STYLE>");
				dosREPORT.writeBytes("<PRE style =\" font-size : 15 pt \">");
				dosREPORT.writeBytes("\n\n<B>"+ padSTRING('L',cl_dat.M_strCMPNM_pbst,45)+"\n");
				dosREPORT.writeBytes("</STYLE>");
				dosREPORT.writeBytes("<PRE style =\" font-size : 12 pt \">");
				dosREPORT.writeBytes(padSTRING('L',"PRODUCT TRANSFER FORM",48)+"\n\n");
				dosREPORT.writeBytes("</B></STYLE>");
				dosREPORT.writeBytes("<PRE style =\" font-size : 10 pt \">");
			}
			else 
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					prnFMTCHR(dosREPORT,M_strENH);					
					dosREPORT.writeBytes("\n\n"+ padSTRING('L',cl_dat.M_strCMPNM_pbst,37)+"\n");
					prnFMTCHR(dosREPORT,M_strNOENH);
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('L',"PRODUCT TRANSFER FORM",57)+"\n");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				}
				else
				{
					dosREPORT.writeBytes("\n\n"+ padSTRING('L',cl_dat.M_strCMPNM_pbst,62)+"\n");
					dosREPORT.writeBytes(padSTRING('L',"PRODUCT TRANSFER FORM",60)+"\n");
				}
			}
			strPTFDT = getPTFDT(strPTFNO);
			if(strCALL.equals("Yes"))				
				dosREPORT.writeBytes("\n    " +padSTRING('R',"PTF Statement on "+strPTFDT,strDOTLN.length()-25));
			else 
            {
			//	strPTFDT = txtPTFDT.getText();
                dosREPORT.writeBytes("\n    " +padSTRING('R',"PTF Statement on "+strPTFDT,strDOTLN.length()-25));
            }   
		//	dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst);
	        dosREPORT.writeBytes("Date    : "+strPTFDT);
			dosREPORT.writeBytes("\n    " +padSTRING('R',"PTF Number : "+strPTFNO,strDOTLN.length()-25));			
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO);
			
			dosREPORT.writeBytes("\n"+strDOTLN);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("\n"+"    Lot No    Grade          Pkg.Wt.     Bags      Qty.   Remarks");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n"+strDOTLN);
			cl_dat.M_intLINNO_pbst = 13;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}	
	String getPTFDT(String P_strPTFNO)
	{
	      String L_strPTFDT="";
	    try
	    {
	        M_strSQLQRY = "Select PTF_PTFDT from FG_PTFRF where PTF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PTF_PTFNO = '"+ P_strPTFNO+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{	
					java.sql.Date L_tmpDATE = M_rstRSSET.getDate("PTF_PTFDT");
					if(L_tmpDATE != null)					
						L_strPTFDT = M_fmtLCDAT.format(L_tmpDATE);
				}
				M_rstRSSET.close();
			}
			return L_strPTFDT; 
	    }
	    catch(Exception L_E)
	    {
	       setMSG(L_E,"getPTFDT"); 
	       return L_strPTFDT; 
	    }
	    
	}
	
}