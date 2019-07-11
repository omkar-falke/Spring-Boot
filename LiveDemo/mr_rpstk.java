/*
System Name    : Marketing System
Program Name   : Stock Transfer 
Program Desc.  : 
Author         :  Zaheer Alam Khan
Date           : 06/11/2006
Version        : v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/




import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.util.StringTokenizer;
/**

System Name    : Marketing System
Program Name   : Stock Transfer Report
Purpose        : The Purpose of This report is to find out the Stock Transfer from Location wise
                 Grade wise And Invoice wise as per Date Range.
                 And Also find out how much Stock have a Consignment Stockist. 

List of tables used :
Table Name              Primary key                                             Operation done
                                                            Insert   Update   Query   Delete	
-----------------------------------------------------------------------------------------------
CO_CDTRN          CMT_CGMTP,CMT_CGSTP,CMT_CODCD                                  #
CO_PTMST			   PT_PRTTP,PT_PRTCD					                     #
MR_IVTR1          IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP                        #
-----------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT   IVT_INVDT                 MR_IVTR1			Data         Invoice Date
txtTODAT   IVT_INVDT		       MR_IVTR1           Date         Invoice date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
        In this Report There are Three radioButton.
         1)Invoice Deatils
         2)Loc. Wise Summary
         3)Grade Wise Summary

    For Invoice Details Report Data is taken from MR_IVTR1 And CO_PTMST  on This condition.

	   1) PT_PRTTP ='C' ";
         2) AND PT_PRTCD = IVT_CNSCD
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0 ";
         5) AND date(IVT_INVDT) BETWEEN '"+txtFMDAT.getText()+"' AND '"+txtTODAT.getText()+"' ";
         6) AND isnull(IVT_INVNO,'') <>''
         7) AND isnull(ivt_stsfl,'') <>'X' ";
	     8) ORDER BY IVT_CNSCD,IVT_INVNO ";

    For Loction Wise Summary Data is taken from MR_IVTR1 and CO_PTMST on This condition.
               
         1) PT_PRTTP ='C'
         2) AND PT_PRTCD = IVT_CNSCD 
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0
         5) AND date(IVT_INVDT) BETWEEN txtFMDAT.getText() AND txtTODAT.getText()
         6) AND isnull(ivt_invno,'') <>''
         7) AND isnull(ivt_stsfl,'') <>'X' ";
		 8) GROUP BY IVT_DSRCD,IVT_CNSCD,PT_PRTNM,PT_DSTCD,IVT_PRDDS ";
         9) ORDER BY IVT_CNSCD,IVT_PRDDS";
   
    For Grade wise Summary Data is taken from MR_IVTR1 Amd CO_PTMST on This Condition
         1) PT_PRTTP ='C'
         2) AND PT_PRTCD = IVT_CNSCD
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0
         5) AND date(IVT_INVDT) BETWEEN txtFMDAT.getText() AND txtTODAT.getText()
         6) AND isnull(ivt_invno,'') <>''";
         7) AND isnull(ivt_stsfl,'') <>'X'
         8) GROUP BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
		 9) ORDER BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
   

<B>Validations & Other Information:</B>    
     -  Date Should be valid
     -  From date must  be less then To Date
</I> */


class mr_rpstk extends cl_rbase
{								
//	private JRadioButton rdbDETAL;       /**RadioButton For Detail Report    */
//	private JRadioButton rdbLOCTN;       /**RadioButton For Location wise Summary Report */
//	private JRadioButton rdbGRADE;       /** RadioButton For Grade wise Summary Report */
//	private ButtonGroup btgRPTTP;         /**ButtonGroup Variable to Add RadioButton    */
//	private JTextField txtFMDAT;         /** JtextField to display & enter Date to generate the Report.*/
	private JTextField txtTODAT;         /** String Variable for date.*/
	private String strFILNM;	         /** String Variable for generated Report file Name.*/ 
	private int intRECCT;		         /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
	private Hashtable<String,String> hstDSTCD;           /** Hashtable Variable for Distination  */ 
	private Hashtable<String,String> hstDSRCD;
	private String strINVNO="";            /** String Variable for Invoice Number*/ 
	private String strPINVNO="";           /** String Variable for previous Invoice Number*/ 
	private String strINVVL="";            /** String Variable for Invoice Value*/ 
	private String strPRDDS="";            /** String Variable for Product Code*/ 
	private String strPPRDDS="";           /** String Variable for previous Product Code*/ 
	private String strCNSCD="";            /** String Variable for Consignee Code*/ 
	private String strPCNSCD="";           /** String Variable for previous Consignee Code*/ 
	
	private JRadioButton rdbOVRAL;
	private JRadioButton rdbSPECF;
	private ButtonGroup btgRPTTP;
	private JLabel lblCNSCD;       /**JLabel to display message on the Screen.*/
	private JTextField txtCNSCD;   /** JtextField to display & enter Consignment Stockist Code the Report.*/
	private JTextField txtCNSNM;   /** JtextField to display & enter Consignment Stockist Name.*/	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------------------";
	private JCheckBox chkSTK ;
	mr_rpstk()
	{
		super(1);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			btgRPTTP=new ButtonGroup();
		
			setMatrix(20,8);
			
			add(rdbOVRAL = new JRadioButton(" Over All",true),3,3,1,1,this,'L');
			add(rdbSPECF = new JRadioButton("Specific Consignment Stockist"),3,4,1,3,this,'L');
			btgRPTTP.add(rdbOVRAL);
			btgRPTTP.add(rdbSPECF);
			add(lblCNSCD = new JLabel("Cons. Stk."),4,3,1,1,this,'L');
			add(txtCNSCD = new TxtLimit(5),4,4,1,1,this,'L');
			add(txtCNSNM = new TxtLimit(30),4,5,1,3,this,'L');
			lblCNSCD.setVisible(false);
			txtCNSCD.setVisible(false);
			txtCNSNM.setVisible(false);
      		add(new JLabel("Stock As on "),7,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),7,4,1,1,this,'L');
			add(chkSTK = new JCheckBox("Show Item with Stock > 0 "),8,4,1,3,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);	
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDST' AND CMT_STSFL <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstDSTCD = new Hashtable<String,String>(50);
			if(M_rstRSSET !=null)
			{
			    while(M_rstRSSET.next())
			        hstDSTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP ='G'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstDSRCD = new Hashtable<String,String>(50);
			if(M_rstRSSET !=null)
			{
			    while(M_rstRSSET.next())
			        hstDSRCD.put(M_rstRSSET.getString("PT_PRTCD"),M_rstRSSET.getString("PT_PRTNM"));
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	void setENBL(boolean L_STAT)
	{
	    super.setENBL(L_STAT);
	    txtCNSNM.setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
				M_cmbDESTN.requestFocus();
			}
			//txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)		
			cl_dat.M_PAGENO = 0;	
		
		if(M_objSOURC==rdbOVRAL)
		{
			lblCNSCD.setVisible(false);
			txtCNSCD.setVisible(false);
			txtCNSNM.setVisible(false);
			txtCNSCD.setText("");
			txtCNSNM.setText("");
		}
		if(M_objSOURC==rdbSPECF)
		{
			
			lblCNSCD.setVisible(true);
			txtCNSCD.setVisible(true);
			txtCNSNM.setVisible(true);
			
		}
		if(M_objSOURC==txtCNSCD)
		{
			txtCNSNM.setText(hstDSRCD.get(txtCNSCD.getText()).toString());
		}

	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			/*if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					txtTODAT.requestFocus();
				else
				{
					txtFMDAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			*/
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
					
				else
				{
					txtTODAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			setMSG("",'N');
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC==txtCNSCD)
			{
				M_strHLPFLD="txtCNSCD";
				M_strSQLQRY="Select PT_PRTCD,PT_PRTNM from CO_PTMST where  PT_PRTTP='G' ";
				if(txtCNSCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtCNSCD.getText().trim().toUpperCase()+"%'"; 
				}
				M_strSQLQRY += " Order by PT_PRTCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT");
			}
		}

	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		setMSG("",'N');
		if(M_strHLPFLD == "txtCNSCD")
		{
			
			StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
			txtCNSCD.setText(L_STRTKN1.nextToken());
			txtCNSNM.setText(L_STRTKN1.nextToken());
			
		}
		
	}
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
			if(txtTODAT.getText().trim().length()==0)
			{
				setMSG("Please Enter To Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
			}	
		
			if(rdbSPECF.isSelected())
			{
				if(txtCNSCD.getText().trim().length()!=5)
				{
					setMSG("please Enter Consignee Code",'E');
					txtCNSCD.requestFocus();
					return false;
				
				}
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	
	/**
	 * Method to generate the Report & send to the selected Destination.
	 */
	public void exePRINT()
	{
		//System.out.println("IN Print");
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpstk.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpstk.doc";
			
			getDATA();
			if(dosREPORT !=null)
			    dosREPORT.close();
			if(fosREPORT !=null)
			    fosREPORT.close();
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
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
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{					
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Statement"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	
	/**
	 * Method to fetch data from Database & club it with Header & Footer.
	 */
	public void getDATA()
	{		
	    String strDSRCD = "",strCNSCD ="";
	    String strPDSRCD = "",strPCNSCD ="";
	    float L_fltLOCQT =0.0f;
	    float L_fltTOTQT =0.0f;
        float L_fltGRTOT =0.0f;
		java.sql.Date jdtTEMP;
		setCursor(cl_dat.M_curWTSTS_pbst);
		try
	    {	  
	      	fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Stock Statement</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}				
			prnHEADER();
			dspDETAL();
		    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    prnFMTCHR(dosREPORT,M_strNOBOLD);
			 if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");    
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			    fosREPORT.close();
			    dosREPORT.close();
			    setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	 * Function For detail Report
	 */
	private void dspDETAL()
	{
		int L_intRECCT=0;
		int L_intCNSCT=0;
		double L_dblGRTOT=0.0;
		double L_dblTOTAL=0.0;
		
		double L_dblGRTOT_SAL=0.0;
		double L_dblTOTAL_SAL=0.0;
		
		double L_dblGRTOT_STK=0.0;
		double L_dblTOTAL_STK=0.0;
		
		double L_dblGRTOT_STV=0.0;
		double L_dblTOTAL_STV=0.0;

		double L_dblGRTOT_EXC=0.0;
		double L_dblTOTAL_EXC=0.0;

		double L_dblGRTOT_EDU=0.0;
		double L_dblTOTAL_EDU=0.0;

		double L_dblGRTOT_ACV=0.0;
		double L_dblTOTAL_ACV=0.0;

		double L_dblGRTOT_BAS=0.0;
		double L_dblTOTAL_BAS=0.0;

		double L_dblGRTOT_INV=0.0;
		double L_dblTOTAL_INV=0.0;
		double L_dblSTKVL=0.0;
		double L_dblTOT_STV=0.0;


		try
		{
			/*M_strSQLQRY = "SELECT IVT_CNSCD,IVT_INVNO,PT_PRTNM,PT_DSTCD,IVT_INVDT,IVT_PRDDS,IVT_INVQT,IVT_SALQT,isnull(ivt_invqt,0) - isnull(IVT_SALQT,0) L_STKQT,IVT_INVRT,IVT_ASSVL,IVT_EXCVL,IVT_CVDVL, ";
			M_strSQLQRY+= "IVT_EDCVL,IVT_EHCVL,IVT_ACVVL,IVT_NETVL,IVT_INVVL FROM MR_IVTR1,CO_PTMST WHERE PT_PRTTP ='C' ";
			M_strSQLQRY +=" AND PT_PRTCD = IVT_CNSCD AND IVT_SALTP ='04' and ivt_invqt >0 ";
			M_strSQLQRY +=" and isnull(IVT_INVNO,'') <>'' and isnull(ivt_stsfl,'') <>'X' ";
			M_strSQLQRY += "and date(IVT_INVDT) <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
			M_strSQLQRY += " ORDER BY IVT_CNSCD,IVT_INVNO ";*/

		//isnull(nullif(A.IVT_EXCVL,0),A.IVT_CVDVL) - constructs checks for null or zero value or EXC and returns CVD in that case
	
            M_strSQLQRY ="SELECT A.IVT_CNSCD CNSCD,A.IVT_INVNO INVNO,PT_PRTNM ,PT_DSTCD ,A.IVT_INVDT INVDT,"
            +"A.IVT_PRDDS PRDDS,A.IVT_INVQT INVQT,A.IVT_SALQT, A.IVT_NETVL NETVL,A.IVT_INVVL INVVL,A.IVT_ASSVL ASSVL,A.IVT_INVRT INVRT,isnull(nullif(A.IVT_EXCVL,0),A.IVT_CVDVL)EXCVL,isnull(A.IVT_EDCVL,0) EDCVL,isnull(A.IVT_EHCVL,0) EHCVL, isnull(A.IVT_ACVVL,0) ACVVL,SUM(B.IVT_INVQT)L_SALQT,(isnull(A.IVT_INVQT,0) - SUM(isnull(B.IVT_INVQT,0)))L_STKQT "
            +" FROM CO_PTMST,MR_IVTR1 A LEFT OUTER JOIN MR_IVTRN B  ON B.IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND B.IVT_SALTP in ('14','15') "
            +" and CONVERT(varchar,B.IVT_INVDT,101) <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
            +" AND B.IVT_CMPCD = A.IVT_CMPCD AND B.IVT_CSIRF = A.IVT_INVNO AND B.IVT_PRDCD = A.IVT_PRDCD WHERE PT_PRTTP ='C'"
            +" AND PT_PRTCD = A.IVT_CNSCD AND A.IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.IVT_SALTP ='04' and A.ivt_invqt >0 and "
            +" isnull(A.IVT_INVNO,'') <>'' and isnull(A.ivt_stsfl,'') <>'X'and "
            +" CONVERT(varchar,A.IVT_INVDT,101) <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
          	if(rdbSPECF.isSelected())
			{
				M_strSQLQRY +=" and A.IVT_DSRCD='"+txtCNSCD.getText().trim()+"' ";
			}
            M_strSQLQRY +="  GROUP BY A.IVT_CNSCD,A.IVT_INVNO,PT_PRTNM,PT_DSTCD,A.IVT_INVDT,A.IVT_PRDDS,A.IVT_INVQT,A.IVT_SALQT,A.IVT_NETVL,A.IVT_INVVL,A.IVT_ASSVL,A.IVT_INVRT,isnull(nullif(A.IVT_EXCVL,0),A.IVT_CVDVL),A.IVT_EDCVL,A.IVT_EHCVL,A.IVT_CVDVL,A.IVT_ACVVL ";
            if(chkSTK.isSelected())
		{
			M_strSQLQRY += " Having (isnull(A.IVT_INVQT,0) - SUM(isnull(B.IVT_INVQT,0))) >0 ";
		}
		 M_strSQLQRY +=" ORDER BY A.IVT_CNSCD,A.IVT_INVNO ";
		//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{ 
				while(M_rstRSSET.next())
				{
					
					if(cl_dat.M_intLINNO_pbst >65)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);
						cl_dat.M_intLINNO_pbst = 0;
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
						    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></P>");
						prnHEADER();
					}
					strINVNO=nvlSTRVL(M_rstRSSET.getString("INVNO"),"");
					strCNSCD=nvlSTRVL(M_rstRSSET.getString("CNSCD"),"");
					if(!strCNSCD.equals(strPCNSCD))
					{
						if(!strINVNO.equals(strPINVNO))
						{
							if(L_intRECCT>1)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");   
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strINVVL),0),93));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOT_STV,0),12));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst +=1;
								
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
								L_dblTOT_STV =0;
								
							}
							cl_dat.M_intLINNO_pbst +=1;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");   
							if(L_intCNSCT>1)
							{
							    dosREPORT.writeBytes(padSTRING('L'," Total :  "+setNumberFormat(L_dblTOTAL_INV,3),20));
							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_SAL,3),10));
							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_STK,3),10));

							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_BAS,0),18));
							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_EXC,0),8));
							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_EDU,0),7));		
							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_ACV,0),8));								
							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL,0),12));
							    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_STV,0),12));

								dosREPORT.writeBytes("\n\n");
								cl_dat.M_intLINNO_pbst +=2;
								L_dblGRTOT += L_dblTOTAL;
								L_dblTOTAL=0.0;
								L_dblTOTAL_SAL=0.0;
								L_dblTOTAL_STK=0.0;
								L_dblTOTAL_STV=0.0;
								L_intCNSCT=0;
						
								L_dblTOTAL_EXC=0.0;
								L_dblTOTAL_EDU=0.0;
								L_dblTOTAL_ACV=0.0;

								L_dblTOTAL_BAS=0.0;
								L_dblTOTAL_INV=0.0;

							}
							
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"("+strCNSCD+")",46));
							//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"("+strCNSCD+")",46));
							if(hstDSTCD.containsKey((String)nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")))
								dosREPORT.writeBytes(padSTRING('R',"("+hstDSTCD.get(nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")).toString()+")",15));	//DMR Number
							else
								dosREPORT.writeBytes(padSTRING('R',"("+M_rstRSSET.getString("PT_DSTCD")+")",15));
							
						/*	if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   */
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							L_intRECCT=0;
							dosREPORT.writeBytes(padSTRING('R',strINVNO,20));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("INVDT")).toString(),""),11));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
						
							strINVVL=nvlSTRVL(M_rstRSSET.getString("INVVL"),"");
							
							L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("INVVL"),"0"));
							
							strPCNSCD=strCNSCD;
						}
						//else
						//dosREPORT.writeBytes(padSTRING('R',"",10));
					}
					else
					{
						//dosREPORT.writeBytes(padSTRING('R',"",10));
						
						if(!strINVNO.equals(strPINVNO))
						{
							if(L_intRECCT>1)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");   
								
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strINVVL),0),93));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOT_STV,0),12));

								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst +=1;
								
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
							}
							L_dblTOT_STV =0;
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							L_intRECCT=0;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");   
						

							dosREPORT.writeBytes(padSTRING('R',strINVNO,20));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("INVDT")).toString(),""),11));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
						
							strINVVL=nvlSTRVL(M_rstRSSET.getString("INVVL"),"");
							L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("INVVL"),"0"));
					
						}
						///else
						///	dosREPORT.writeBytes(padSTRING('R',"",10));
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PRDDS"),""),11));
					//dosREPORT.writeBytes(padSTRING('R',"",2));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("INVQT"),""),9));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_SALQT"),"-"),10));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_STKQT"),""),10));
					
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("INVRT"),0),8));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("ASSVL"),0),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("EXCVL"),0),8));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("EDCVL")+M_rstRSSET.getDouble("EHCVL"),0),7));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("ACVVL"),0),8));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("NETVL"),0),12));
					L_dblSTKVL = (M_rstRSSET.getDouble("NETVL") * M_rstRSSET.getDouble("L_STKQT"))/ M_rstRSSET.getDouble("INVQT");

					L_dblTOTAL_SAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_SALQT"),"0"));
					L_dblTOTAL_STK+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_STKQT"),"0"));
					L_dblTOTAL_STV+=L_dblSTKVL;

					L_dblTOTAL_BAS+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ASSVL"),"0"));
					L_dblTOTAL_EXC+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("EXCVL"),"0"));
					L_dblTOTAL_EDU+=M_rstRSSET.getDouble("EDCVL")+M_rstRSSET.getDouble("EHCVL");
					L_dblTOTAL_ACV+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ACVVL"),"0"));
					L_dblTOTAL_INV+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("INVQT"),"0"));

					L_dblGRTOT_INV +=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("INVQT"),"0"));
					L_dblGRTOT_SAL +=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_SALQT"),"0"));
					L_dblGRTOT_STK +=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_STKQT"),"0"));
					L_dblGRTOT_STV+=L_dblSTKVL;
					L_dblTOT_STV+=L_dblSTKVL;

					L_dblGRTOT_BAS+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ASSVL"),"0"));
					L_dblGRTOT_EXC+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("EXCVL"),"0"));
					L_dblGRTOT_ACV+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ACVVL"),"0"));
					L_dblGRTOT_EDU+=M_rstRSSET.getDouble("EDCVL")+M_rstRSSET.getDouble("EHCVL");
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSTKVL,0),12));

					//L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
					
					//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_EDCVL"),""),10));
					
					intRECCT=1;
					L_intRECCT++;
					L_intCNSCT++;
					strPINVNO=strINVNO;
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");   
					
				if(intRECCT>1)
				{
					
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strINVVL),0),93));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOT_STV,0),12));
					L_dblTOT_STV =0;
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
				if(L_intCNSCT>1)
				{
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strINVVL),0),93));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOT_STV,0),12));
					L_dblTOT_STV =0;
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;

					dosREPORT.writeBytes(padSTRING('L'," Total :  "+setNumberFormat(L_dblTOTAL_INV,3),20));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_SAL,3),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_STK,3),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_BAS,0),18));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_EXC,0),8));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_EDU,0),7));								
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_ACV,0),8));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL,0),12));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL_STV,0),12));

					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					L_dblGRTOT += L_dblTOTAL;
					L_dblTOTAL=0.0;
					L_dblTOTAL_SAL=0.0;
					L_dblTOTAL_STK=0.0;
    				      L_dblTOTAL_STV=0.0;

					L_dblTOTAL_EXC=0.0;
					L_dblTOTAL_EDU=0.0;
					L_dblTOTAL_ACV=0.0;
					L_dblTOTAL_BAS=0.0;
					L_dblTOTAL_INV=0.0;
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes(padSTRING('L'," Grand Total :  "+setNumberFormat(L_dblGRTOT_INV,3),20));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT_SAL,3),10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT_STK,3),10));

				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT_BAS,0),18));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT_EXC,0),8));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT_EDU,0),7));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT_ACV,0),8));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT,0),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT_STV,0),12));

			///	//dosREPORT.writeBytes(padSTRING('L'," Grand Total : "+setNumberFormat(L_dblGRTOT,3),39));
			///	//dosREPORT.writeBytes(padSTRING('L',""+setNumberFormat(L_dblGRTOT_SAL,3),12));
			///	//dosREPORT.writeBytes(padSTRING('L',""+setNumberFormat(L_dblGRTOT_STK,3),12));
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");  
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspDETAL");
		}
	}
	
	/**
	 * Method to generate the header of the Report.
	 */
	void prnHEADER()
	{
		try
		{
		  	cl_dat.M_PAGENO ++;
			String L_strTEMP ="";
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			 if(M_rdbHTML.isSelected())
				 dosREPORT.writeBytes("<B>");	
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-23));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			//dosREPORT.writeBytes(padSTRING('R',"Stock Details on Consignment Stockist as on  : "+cl_dat.M_strLOGDT_pbst,strDOTLN.length()-21));
			if(rdbOVRAL.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Closing Stock Report - Overall As on "+txtTODAT.getText(),strDOTLN.length()-23));
			}
			else
			{
				dosREPORT.writeBytes(padSTRING('R',"Closing Stock Report of M/s "+ txtCNSNM.getText().trim()+ " As on "+txtTODAT.getText(),strDOTLN.length()-23));

			}
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			 if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");	
			dosREPORT.writeBytes(strDOTLN +"\n");
		//	if(rdbDETAL.isSelected())
		//	{
				dosREPORT.writeBytes("Consignment Stockist                          Location \n");
				//dosREPORT.writeBytes("Inv.No    Inv. Date  Grade Name          Inv Qty.    Sale Qty.      Stock  Tot Amount");
				dosREPORT.writeBytes("Inv.No              Inv. Date \n");
				dosREPORT.writeBytes("Grade Name   Inv Qty. Sale Qty.    Stock Rate/MT Basic Val Exc/cvd   CESS   A.CVD  Tot Amount  Stock Value");

				cl_dat.M_intLINNO_pbst = 2;
		//	}
			//dosREPORT.writeBytes("Consignee                          Destination    Grade           Stock Trf.      Sale     Stock ");
			/*else if(rdbGRADE.isSelected())
			{
				dosREPORT.writeBytes("Grade               Consignment Stockist                    Location              Trf. Qty ");
			}
			else
				dosREPORT.writeBytes("Consignment Stockist                    Location       Grade              Trf. Qty      ");*/
			//dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	
	
}