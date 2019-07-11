/*
System Name   : Marketing System
Program Name  : Account Reference Posting
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 21/07/2006"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :28/07/2006
Modified det.  :
Version        :
*/

import java.sql.ResultSet;import java.util.Date;import java.util.Hashtable;
import java.awt.event.KeyEvent;import javax.swing.JComponent;
import javax.swing.JComboBox; import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;


/*
<PRE>
System Name   : Marketing System
Program Name  : Account Reference Posting
Modify Date   : 31-07-2006
Purpose : This program is used to generate & print Account Status of Credit Note.

List of Table used :
-----------------------------------------------------------------------------------------------------------------
Table Name      Primary Key														             Operation done
																				         Insert  Upd  Query  Del
-----------------------------------------------------------------------------------------------------------------
MR_PTTRN		PT_CMPCD,PT_INVNO,PT_PRDCD,PT_PKGTP,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_SRLNO	  -      -     #     -
MR_PLTRN		PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO                              -      -     #     -    
CO_PTMST		PT_PRTTP,PT_PRTCD												          -      -     #     -
-----------------------------------------------------------------------------------------------------------------
In this program there are two option 
	1) Pending for account Reference Posting
    2) Pending for Document Generation

    For Pending for Account Reference Posting generating Report Data is taken FROM 
    1)  MR_PLTRN
    2)  CO_PTMST
Condition : 1)PT_PRTTP=PL_PRTTP
            2)AND  PT_PRTCD=PL_PRTCD
            3)PL_DOCTP like '0%'
            4)and ifnull(pl_a

    For Pending for Document Generation generating Report Data is taken FROM 
    1)  MR_PTTRN A
    2)  CO_PTMST B
Condition : 1) A.PT_PRTTP = B.PT_PRTTP
            2) AND A.PT_PRTCD = B.PT_PRTCD
            3) AND  A.PT_DOCRF ='00000000'
            4) GROUP BY A.PT_CRDTP,A.PT_DOCRF,A.PT_PRTCD,B.PT_PRTNM
</Pre>
*/

class mr_rparu extends cl_rbase
{										   /** ComboBox for selection of report type */
	private JComboBox cmbRPTTP;            /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			   /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				   /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ;   /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 102;            // report width
	public mr_rparu()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			cmbRPTTP = new JComboBox();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			cmbRPTTP.addItem("Pending for Account Ref Posting");
			cmbRPTTP.addItem("Pending for Document Generation");
			add(new JLabel("Report Type"),3,3,1,1,this,'L');
			add(cmbRPTTP,3,4,1,2.5,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
	}
	public void exePRINT()
	{
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rparu.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rparu.doc";
			getDATA();
			fosREPORT.close();
			dosREPORT.close();
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Credit Note Status"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	private void getDATA()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO =1;
			java.util.Date L_datTMPDT=null;
			String L_strNXDOC="";
			String L_strPRDOC="";
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Credit Note Status</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			if(cmbRPTTP.getSelectedIndex()==0)
			{
				M_strSQLQRY ="SELECT PL_DOCTP,PL_DOCNO,PL_PRTCD,PT_PRTNM,PL_DOCVL,PL_DOCDT ";
			  	M_strSQLQRY +=" FROM MR_PLTRN,CO_PTMST where PT_PRTTP=PL_PRTTP AND  PT_PRTCD=PL_PRTCD AND PL_DOCTP like '0%' and isnull(pl_accrf,'') = '' "; 
				M_strSQLQRY +="Order by PL_DOCTP,PL_DOCNO ";
				/*
					M_strSQLQRY ="SELECT PL_DOCTP,PL_DOCNO,PL_PRTCD,PT_PRTNM,PL_DOCVL,PL_DOCDT,CMT_CODDS ";
					M_strSQLQRY +=" FROM MR_PLTRN,CO_PTMST,CO_CDTRN where CMT_CGMTP= 'SYS' AND CMT_CGSTP='MRXXPTT'";
					M_strSQLQRY +=" AND  CMT_CODCD= PL_DOCTP AND PT_PRTTP=PL_PRTTP AND  PT_PRTCD=PL_PRTCD";
					M_strSQLQRY +=" AND PL_DOCTP like '0%' and ifnull(pl_accrf,'') = '' ";
					M_strSQLQRY +="Order by PL_DOCTP,PL_DOCNO";
				*/
			}
			if(cmbRPTTP.getSelectedIndex()==1)
			{
				M_strSQLQRY ="SELECT A.PT_CRDTP,A.PT_DOCRF,A.PT_PRTCD,B.PT_PRTNM,SUM(PT_PNTVL)PNTVL ";
				M_strSQLQRY += " FROM MR_PTTRN A, CO_PTMST B WHERE  ";
				M_strSQLQRY += " A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD AND  A.PT_DOCRF ='00000000' ";
				M_strSQLQRY += " GROUP BY A.PT_CRDTP,A.PT_DOCRF,A.PT_PRTCD,B.PT_PRTNM ORDER BY A.PT_CRDTP,A.PT_DOCRF,A.PT_PRTCD,B.PT_PRTNM";
			}
			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			prnHEADER();
			while(M_rstRSSET.next())
			{
				if(cl_dat.M_intLINNO_pbst >64)
				{
					dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); 
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO +=1;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}	
				L_strNXDOC=nvlSTRVL(M_rstRSSET.getString(1),"");
			//	System.out.println(L_strNXDOC);
				if(!L_strNXDOC.equals(L_strPRDOC))
				{
					if(intRECCT>0)
					{
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst +=1;
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(getCODVL("SYSMRXXPTT"+L_strNXDOC,cl_dat.M_intCODDS_pbst),""),52));	
				
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst +=2;
				}
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString(2),""),12));	
				if(cmbRPTTP.getSelectedIndex()==0)
				{
					L_datTMPDT =M_rstRSSET.getDate("PL_DOCDT");
					if(L_datTMPDT !=null)
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),15));	
					else
						dosREPORT.writeBytes(padSTRING('R'," ",15));	
				}
				else
					dosREPORT.writeBytes(padSTRING('R'," -",15));	
				
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString(3),""),9));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString(4),""),40));	
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString(5),"0")),0),14));	
				dosREPORT.writeBytes("\n");
				intRECCT++;
				L_strPRDOC=L_strNXDOC;
				cl_dat.M_intLINNO_pbst +=1;
			}
			dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); 
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))			
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);					
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setMSG("",'N'); 
	}
	/**
	 * Method to generate the header of the Report.
	*/
	void prnHEADER()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd.",intRPTWD-25)+padSTRING('L',"Report Date : "+cl_dat.M_txtCLKDT_pbst.getText(),25)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"List Of Transactions "+cmbRPTTP.getSelectedItem().toString(),intRPTWD-24)+padSTRING('R',"Page No.    : "+cl_dat.M_PAGENO ,21)+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); 
		  	dosREPORT.writeBytes("Doc. No     Doc.Date       Code     Name                                            Amount   ");
			dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n"); 
			cl_dat.M_intLINNO_pbst +=5;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER ");
		}
	}
	private String crtLINE(int P_strCNT,String P_strLINCHR)
    {
		String strln = "";
		try
		{
			for(int i=1;i<=P_strCNT;i++)   
				strln += P_strLINCHR;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtLINE");
		}
        return strln;
	}
}