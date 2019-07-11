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




import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;
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
txtTODAT   IVT_INVDT				 MR_IVTR1           Date         Invoice date
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
         6) AND ifnull(IVT_INVNO,'') <>''
         7) AND ifnull(ivt_stsfl,'') <>'X' ";
	     8) ORDER BY IVT_CNSCD,IVT_INVNO ";

    For Loction Wise Summary Data is taken from MR_IVTR1 and CO_PTMST on This condition.
               
         1) PT_PRTTP ='C'
         2) AND PT_PRTCD = IVT_CNSCD 
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0
         5) AND date(IVT_INVDT) BETWEEN txtFMDAT.getText() AND txtTODAT.getText()
         6) AND ifnull(ivt_invno,'') <>''
         7) AND ifnull(ivt_stsfl,'') <>'X' ";
		 8) GROUP BY IVT_DSRCD,IVT_CNSCD,PT_PRTNM,PT_DSTCD,IVT_PRDDS ";
         9) ORDER BY IVT_CNSCD,IVT_PRDDS";
   
    For Grade wise Summary Data is taken from MR_IVTR1 Amd CO_PTMST on This Condition
         1) PT_PRTTP ='C'
         2) AND PT_PRTCD = IVT_CNSCD
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0
         5) AND date(IVT_INVDT) BETWEEN txtFMDAT.getText() AND txtTODAT.getText()
         6) AND ifnull(ivt_invno,'') <>''";
         7) AND ifnull(ivt_stsfl,'') <>'X'
         8) GROUP BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
		 9) ORDER BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
   

<B>Validations & Other Information:</B>    
     -  Date Should be valid
     -  From date must  be less then To Date
</I> */


class mr_rpsal extends cl_rbase
{	
	private JRadioButton rdbOVRAL;       /**RadioButton For Detail Report    */
	private JRadioButton rdbSPECF;       /**RadioButton For Detail Report    */							
	private JRadioButton rdbDETAL;       /**RadioButton For Detail Report    */
  //private JRadioButton rdbLOCTN;       /**RadioButton For Location wise Summary Report */
  //private JRadioButton rdbGRADE;       /** RadioButton For Grade wise Summary Report */
	private ButtonGroup btgRPTTP;         /**ButtonGroup Variable to Add RadioButton    */
	private JTextField txtFMDAT;         /** JtextField to display & enter Date to generate the Report.*/
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
	private JLabel lblCNSCD;       /**JLabel to display message on the Screen.*/
	private JTextField txtCNSCD;   /** JtextField to display & enter Consignment Stockist Code the Report.*/
	private JTextField txtCNSNM;   /** JtextField to display & enter Consignment Stockist Name.*/		
	private String strDOTLN = "----------------------------------------------------------------------------------------------------------------------";
	private double dblINVVL=0;
	mr_rpsal()
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
			btgRPTTP=new ButtonGroup();
		
			setMatrix(20,8);
			add(rdbOVRAL = new JRadioButton("Over All",true),4,3,1,1,this,'L');
			add(rdbSPECF = new JRadioButton("Specific Consignment Stockist"),4,5,1,2.5,this,'L');
			
			add(lblCNSCD = new JLabel("Cons. Stk."),5,3,1,1,this,'L');
			add(txtCNSCD = new TxtLimit(5),5,4,1,1,this,'L');
			add(txtCNSNM = new TxtLimit(30),5,5,1,3,this,'L');
			
			
			btgRPTTP.add(rdbOVRAL);
			btgRPTTP.add(rdbSPECF);
		
			add(new JLabel("From Date "),6,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),6,4,1,1,this,'L');
			
			add(new JLabel("To Date "),7,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),7,4,1,1,this,'L');
			
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

			lblCNSCD.setVisible(false);
			txtCNSCD.setVisible(false);
			txtCNSNM.setVisible(false);
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
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
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
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)		
			cl_dat.M_PAGENO = 0;		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
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
		
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC == txtCNSCD)
				{
					M_strSQLQRY="Select PT_PRTNM from CO_PTMST where  PT_PRTTP='G' AND PT_PRTCD='"+txtCNSCD.getText().trim().toUpperCase()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						txtCNSNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
						txtCNSCD.setText(txtCNSCD.getText().toUpperCase());
						txtFMDAT.requestFocus();
						M_rstRSSET.close();
					}
					else
					{
						txtCNSNM.setText("");
						setMSG("Please Enter Correct Code or Press F1 for Help..",'E');
						txtCNSCD.requestFocus();
					}
				}
				if(M_objSOURC == txtFMDAT)
				{
					if(txtFMDAT.getText().trim().length() == 10)
						txtTODAT.requestFocus();
					else
					{
						txtFMDAT.requestFocus();
						setMSG("Enter Date",'N');
					}
				}
			
			if(M_objSOURC == txtTODAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
					
				else
				{
					txtFMDAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			setMSG("",'N');
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"VK_ENTER");
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
			
			if(txtFMDAT.getText().trim().length()==0)
			{
				setMSG("Please Enter From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length()==0)
			{
				setMSG("Please Enter To Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("To Date can not be Less than From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}		
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpsal.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpsal.doc";
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Transferr"," ");
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
	    String strDSRCD = "";
		strCNSCD ="";
	    String strPDSRCD = "";
		strPCNSCD ="";
		strPINVNO="";
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
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Consignment Stockist</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
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
			//	dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
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
		double L_dblEXGRT=0.0;
		double L_dblQTGRT=0.0;
		double L_dblBSGRT=0.0;
		double L_dblCSGRT=0.0;
		
		double L_dblQTTOT=0.0;
		double L_dblBSTOT=0.0;
		double L_dblEXTOT=0.0;
		double L_dblCSTOT=0.0;
		double L_dblTOTAL=0.0;
		
		String L_strINVQT="";
		String L_strBASIC="";
		String L_strEXICE="";
		String L_strCESS="";

		try
		{
			M_strSQLQRY = "SELECT IVT_DSRCD,IVT_INVNO,PT_PRTNM,PT_DSTCD,IVT_INVDT,IVT_PRDDS,IVT_INVQT,IVT_INVRT,IVT_ASSVL,IVT_EXCVL, ";
			M_strSQLQRY+= "isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0) IVT_EDHVL,IVT_NETVL,IVT_INVVL,IVT_CSIRF FROM MR_IVTRN,CO_PTMST WHERE PT_PRTTP ='G' ";
			M_strSQLQRY +=" AND PT_PRTCD = IVT_DSRCD AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='14' and ivt_invqt >0 ";
			M_strSQLQRY +=" and isnull(IVT_INVNO,'') <>'' and isnull(ivt_stsfl,'') <>'X' and ivt_SBSCD1 in "+M_strSBSLS;
			M_strSQLQRY += " and CONVERT(varchar,IVT_INVDT,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
			if(rdbSPECF.isSelected())
				M_strSQLQRY += " and IVT_DSRTP ='G' AND IVT_DSRCD ='"+txtCNSCD.getText().trim() +"'";	
			M_strSQLQRY += " ORDER BY IVT_DSRCD,IVT_INVNO ";
			
		/*
			M_strSQLQRY = "SELECT IVT_INVNO,IVT_INVDT,IVT_PRDDS,IVT_INVQT,IVT_INVRT,IVT_ASSVL,IVT_EXCVL, ";
			M_strSQLQRY+= "IVT_EDCVL,IVT_NETVL,IVT_INVVL FROM MR_IVTR1 WHERE  ";
			M_strSQLQRY +=" IVT_SALTP ='04' and ivt_invqt >0 ";
			M_strSQLQRY +=" and ifnull(IVT_INVNO,'') <>'' and ifnull(ivt_stsfl,'') <>'X'   and ivt_sbscd in "+M_strSBSLS;
			M_strSQLQRY += " and date(IVT_INVDT) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
			M_strSQLQRY += " ORDER BY IVT_INVNO ";
		*/
			System.out.println(" M_strSQLQRY"+M_strSQLQRY);
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
					strINVNO=nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
					strCNSCD=nvlSTRVL(M_rstRSSET.getString("IVT_DSRCD"),"");
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
								//dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+strINVVL,108));
								dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+setNumberFormat(dblINVVL,0),117));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst +=1;
								
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
								
							}
							cl_dat.M_intLINNO_pbst +=1;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");   
							if(L_intCNSCT>1)
							{
								dosREPORT.writeBytes(padSTRING('L'," Total : "+setNumberFormat(L_dblQTTOT,3),56));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBSTOT,0),26));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblEXTOT,0),12));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCSTOT,0),9));

								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL,0),14));
								dosREPORT.writeBytes("\n\n");
								cl_dat.M_intLINNO_pbst +=2;
								L_dblQTTOT=0.0;
								L_dblBSTOT=0.0;
								L_dblEXTOT=0.0;
								L_dblCSTOT=0.0;
								L_dblTOTAL=0.0;
								L_intCNSCT=0;
							}
							
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"("+strCNSCD+")",46));
							//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"("+strCNSCD+")",46));
							if(hstDSTCD.containsKey((String)nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")))
								dosREPORT.writeBytes(padSTRING('R',"("+hstDSTCD.get(nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")).toString()+")",15));	//DMR Number
							else
								dosREPORT.writeBytes(padSTRING('R',"("+M_rstRSSET.getString("PT_DSTCD")+")",15));
							
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							L_intRECCT=0;
							dosREPORT.writeBytes(padSTRING('R',strINVNO,10));
							strINVVL=nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"");
							dblINVVL=M_rstRSSET.getDouble("IVT_INVVL");
							L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
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
								
								//dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+strINVVL,97));
								dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+setNumberFormat(dblINVVL,0),117));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst +=1;
								
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
								
							}
							
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							L_intRECCT=0;
							dosREPORT.writeBytes(padSTRING('R',strINVNO,10));
							strINVVL=nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"");
							dblINVVL=M_rstRSSET.getDouble("IVT_INVVL");
							L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
						}
						else
							dosREPORT.writeBytes(padSTRING('R',"",10));
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IVT_INVDT")).toString(),""),13));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_CSIRF"),""),10));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),16));
					//dosREPORT.writeBytes(padSTRING('R',"",2));
					L_strINVQT=nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"");
					L_strBASIC=nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),"");
					L_strEXICE=nvlSTRVL(M_rstRSSET.getString("IVT_EXCVL"),"");
					L_strCESS=nvlSTRVL(M_rstRSSET.getString("IVT_EDHVL"),"");
					
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_INVQT"),3),7));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_INVRT"),0),12));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_ASSVL"),0),14));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_EXCVL"),0),12));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_EDHVL"),0),9));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_NETVL"),0),14));
					dosREPORT.writeBytes(padSTRING('L'," ",3));
//					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_CSIRF"),""),8));
					
					L_dblQTTOT+=Double.parseDouble(nvlSTRVL(L_strINVQT,"0"));
					L_dblBSTOT+=Double.parseDouble(nvlSTRVL(L_strBASIC,"0"));
					L_dblEXTOT+=Double.parseDouble(nvlSTRVL(L_strEXICE,"0"));
					L_dblCSTOT+=M_rstRSSET.getDouble("IVT_EDHVL");
					
					L_dblQTGRT+=Double.parseDouble(nvlSTRVL(L_strINVQT,"0"));
					L_dblBSGRT+=Double.parseDouble(nvlSTRVL(L_strBASIC,"0"));
					L_dblEXGRT+=Double.parseDouble(nvlSTRVL(L_strEXICE,"0"));
					L_dblCSGRT+=M_rstRSSET.getDouble("IVT_EDHVL");
					
					L_dblGRTOT+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_NETVL"),"0"));
					//L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
					
					
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
					
					//dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+strINVVL,97));
					dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+setNumberFormat(dblINVVL,0),117));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
				if(L_intCNSCT>1)
				{
					dosREPORT.writeBytes(padSTRING('L'," Total : "+setNumberFormat(L_dblQTTOT,3),56));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBSTOT,0),26));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblEXTOT,0),12));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCSTOT,0),9));

					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL,0),14));					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					L_dblTOTAL=0.0;
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes(padSTRING('L',"Grand Total : "+setNumberFormat(L_dblQTGRT,3),56));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBSGRT,0),26));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblEXGRT,0),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCSGRT,0),9));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGRTOT,0),14));
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
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");	
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Consignment Stockist Sales Report from "+txtFMDAT.getText()+" To "+txtTODAT.getText(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");		
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			 if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");	
			dosREPORT.writeBytes(strDOTLN +"\n");
			
			dosREPORT.writeBytes("Consignment Stockist                          Location                                                        \n");
			dosREPORT.writeBytes("Inv.No    Inv. Date    Inv.Ref   Grade Name     Inv Qty.     Rate/MT     Basic Val      Excise     CESS    Tot Amount   ");
			cl_dat.M_intLINNO_pbst = 1;
			
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	
	
}