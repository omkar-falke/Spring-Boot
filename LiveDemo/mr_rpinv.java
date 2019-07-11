/*
System Name    : System Administration
Program Name   : Invoice Master.
Program Desc.  : Despatch invoice Details
Author         : Mrs.Dipti S Shinde
Date           : 24th June 05
Version        : v2.0.0
Modificaitons  : 
Modified By    :
Modified Date  : 
Modified det.  :
Version        :
*/
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.util.StringTokenizer;

//----------------------------------------------------------------
/**<pre>
System Name : Material Management System.
 
Program Name : Despatch Invoice Report       

Purpose : This program will generate a Report on Despatch Invoice Details.

List of tables used :
Table Name      Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
Co_CDTRN        CMT_CODCD                                              #
-------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
 IVT_INVNO                    MR_IVTRN    VARCHAR/40   Invoice Number
 IVT_INVDT                       -         
 IVT_ASSVL                       -         
      
-------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)MR_IVTRN 2)CO_CDTRN
                        
<B>Conditions Give in Query:</b>
    Data is taken from MR_IVTRN for given condition from input box
        
<B>Validations :</B>
    - Date should be Valid
    - From date should be smaller than To date.
</I> */

/*-------------------------------------------------------------------*/
public class mr_rpinv extends cl_rbase
{  										
									     /*** String for generated Report File Name*/                   
	private String strFILNM;			/** Integer for counting the records fetched from DB.*/	                     
	private int intRECCT=0;				/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	 /**Combobox for select Type to generate Report value.*/
    private JComboBox cmbDESTP;	/**TextField for from date*/	
	private JTextField txtFRMDT;/**TextField for To date */
	private JTextField txtTODAT;/**Strings for acsepting data from Dtabase to String */
	private String L_strCGMTP="",L_strTOTSTR="",L_strCODCD="",L_strCODDS="";		
	private String L_strDTPCD="",L_strASSVL="",L_strINVDT="",L_strINVNO="",L_strFRMDT,L_strTODAT,L_strLRNO="",L_strINVQT="";	
	private java.sql.Date L_TMPDT;
	private int intCNT,L_intINVQT=0;
	mr_rpinv()
	{
	    super(1);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Despatch Type"),4,4,1,1,this,'L');
			add(cmbDESTP = new JComboBox(),4,5,1,1.5,this,'L');
			
			add(new JLabel("From Date"),5,4,1,1,this,'R');
			add(txtFRMDT = new TxtDate(),5,5,1,1.5,this,'L');	
			
			add(new JLabel("To Date"),6,4,1,1,this,'R');
			add(txtTODAT = new TxtDate(),6,5,1,1.5,this,'L');	
			
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP ='MRXXDTP' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{	
			while(M_rstRSSET.next())
				{									
					cmbDESTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
			    }
           M_rstRSSET.close();
			}
			
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX+" GUI Designing",'E');
		}	
    }
     /**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		
	}
	/** Method for action performing 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE);
		
		if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPRSL_pbst))
			{
				txtFRMDT.setText("");
				txtTODAT.setText("");
			}
			else
			{
				if(txtFRMDT.getText().length()==0)
				txtFRMDT.setText(cl_dat.M_strLOGDT_pbst);
				if(txtTODAT.getText().length()==0)
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
			
			}
		}
		if(M_objSOURC == cmbDESTP)
		{
			setENBL(true);
			setMSG("Enter From Date",'N');
			txtFRMDT.requestFocus();
		}
		if(M_objSOURC == txtFRMDT)
		{
			setMSG("Enter To Date",'N');
			txtTODAT.requestFocus();
		}
		if(M_objSOURC == txtTODAT)
		{
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			cmbDESTP.requestFocus();
			setMSG("please select an option",'N');
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if((L_KE.getKeyCode() == L_KE.VK_F1))
 	    {
			 //setCursor(cl_dat.M_curWTSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC==txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			if(M_objSOURC ==cmbDESTP)
			{
				txtFRMDT.requestFocus();
				setMSG("please enter the date ",'N');
				setCursor(cl_dat.M_curWTSTS_pbst);
			}
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
	}	/**
	* Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;		
		try
		   {
			   if(M_rdbHTML.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpinv.html";				
			   else if(M_rdbTEXT.isSelected())
				   strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpinv.doc";				
				
			getDATA();
			if(intRECCT ==0)
			{
				setMSG("Data not found ..",'E');
				return;
			}
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Invoice No wise"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}					
	}	
	/** Method for Getting All Records from Table. */
    private void getDATA()
	{
		try
		{
			double L_intTOTAS;
			L_strDTPCD="";
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> List of Despatch Invoice Details</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}				
			prnHEADER();
			
			L_strDTPCD =cmbDESTP.getSelectedItem().toString().substring(0,2);
			L_strFRMDT =M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRMDT.getText().trim()));
			L_strTODAT =M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			
			M_strSQLQRY = "SELECT IVT_INVNO,IVT_LR_NO,CONVERT(varchar,IVT_INVDT,101)L_INVDT,sum(IVT_ASSVL) L_ASSVL,sum(IVT_INVQT)L_INVQT from MR_IVTRN "+
						  "where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_STSFL,'')<>'X' and isnull(IVT_INVNO,'') <> '' and "+
						  "CONVERT(varchar,IVT_INVDT,101) between " +"'" +L_strFRMDT +"'"+
						  "AND '" +L_strTODAT+ "' AND IVT_DTPCD ='"+L_strDTPCD+"' group by IVT_INVNO,IVT_LR_NO,CONVERT(varchar,ivt_invdt,101) order by IVT_INVNO,IVT_LR_NO ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			String L_strTMPDT ="";
			if(M_rstRSSET !=null)
			{	
				intCNT =0;
				L_intTOTAS =0;
				
				while(M_rstRSSET.next())
				{
					L_strINVNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"-"),15);				
					L_strASSVL = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_ASSVL"),"-"),15);
					L_strINVQT = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_INVQT"),"-"),20);
					L_strLRNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_LR_NO"),"-"),16);
					L_intTOTAS+=Double.parseDouble(L_strASSVL);
					L_TMPDT = M_rstRSSET.getDate("L_INVDT");
					if(L_TMPDT!=null)
						 L_strTMPDT =M_fmtLCDAT.format(L_TMPDT);
						
					dosREPORT.writeBytes(L_strINVNO+L_strTMPDT+"     "+L_strINVQT+"      "+L_strLRNO+L_strASSVL);
					
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					
					if(cl_dat.M_intLINNO_pbst> 63)
					{						
						dosREPORT.writeBytes("\n-----------------------------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						{
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						prnHEADER();
					}
					intRECCT++;
					intCNT++;
					}
				M_rstRSSET.close();
				dosREPORT.writeBytes("\n-----------------------------------------------------------------------------------------------------");
						setMSG("Report completed.. ",'N');
				if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
					//dosREPORT.writeBytes("\nTotal Invoices : "+intCNT+"                          Assessable Value : "+L_intTOTAS);
					dosREPORT.writeBytes("\nTotal Invoices : "+intCNT);
					dosREPORT.writeBytes(padSTRING('L',"Assessable Value : "+setNumberFormat(L_intTOTAS,2),68));
					cl_dat.M_intLINNO_pbst = 2;
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
				}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
        catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
	/**
	Method to validate DATE and DATA before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
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
			if(txtFRMDT.getText().length()==0)
			{
				setMSG("Enter From Date.. ",'E');
				txtFRMDT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().length()==0)
			{
				setMSG("Enter To Date.. ",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFRMDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("date Should not be gretter than Today's Date",'E');
				txtFRMDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("date Should not be gretter than Today's Date",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFRMDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("From date Should not be gretter than To Date",'E');
				txtFRMDT.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
		
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
			L_strFRMDT =txtFRMDT.getText();
			L_strTODAT =txtTODAT.getText();
			L_strTOTSTR =cmbDESTP.getSelectedItem().toString().substring(2);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,76));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Invoice Details for the Period From " +L_strFRMDT+ " TO " +L_strTODAT,76));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
			if(M_rdbHTML.isSelected())// for html
				dosREPORT.writeBytes("<B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("Despatch Type : "+L_strTOTSTR+"\n");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------\n");						
			dosREPORT.writeBytes("Invoice No.    Invoice Date            Invoice Qty.     LR No.         Assessable Value              \n");					
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst = 8;					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}		
}
