/*
System Name   : Marketing System
Program Name  : 
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 10/07/2006"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :25/07/2006
Modified det.  :
Version        :
*/

import java.sql.ResultSet;import java.util.Date;
import java.awt.event.KeyEvent;import javax.swing.JComponent;import javax.swing.JComboBox; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import java.util.Hashtable;import java.util.StringTokenizer;

class mr_rpivt extends cl_rbase
{			
	private JComboBox cmbPRTTP;
	private JCheckBox chbBALVL;
	private JTextField txtINVNO;
	private JTextField txtPRTNM;		 /** JTextField to  enter From Date */
	private JTextField txtFMDAT;		 /** JtextField to  enter To Date */
	private JTextField txtTODAT;		 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 120;      // report width
	private int intPAGENO=0;
	private ResultSet L_rstRSSET ;
	private String strINVNO="";
	private String strPINVNO="";
	private String strDOTLN = "---------------------------------------------------------------------------------------------";
	public mr_rpivt()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			String L_strCODCD="";
			add(new JLabel("Invoice Number "),4,3,1,1,this,'L');
			add(txtFMDAT=new TxtLimit(8),4,4,1,1,this,'L');
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
		txtPRTNM.setEnabled(false);
		txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
	}
	
	public boolean vldDATA()
	{
		try
		{
			
			if(txtINVNO.getText().trim().length()!=8)
			{
				setMSG("Please Enter Invoice Number ..",'E');
				txtINVNO.requestFocus();
				return false;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtINVNO)
			{
				String L_ARRHDR[] ={"Invoice No.","Invoice Date"};
				M_strSQLQRY = "Select distinct IVT_INVNO,IVT_INVDT from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_STSFL,'')<>'X' ";  // DOT_STSFL in  ('A','H','1') AND ";
				if(txtINVNO.getText().trim().length() >0)
					M_strSQLQRY += " AND  IVT_INVNO like '"+ txtINVNO.getText().trim() + "%' ";
				M_strSQLQRY += " order by IVT_INVNO desc";

				//System.out.println("HELP = "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtINVNO )
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		setMSG("",'N');
		if(M_strHLPFLD.equals("txtINVNO"))
		{
			StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
			txtINVNO.setText(L_STRTKN1.nextToken());
			
		}
	}
	
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpinv.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpinv.doc";
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Invoice Entry Report"," ");
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
			
			java.sql.Date L_datTMPDT =null;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Invoice Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			M_strSQLQRY ="Select IVT_INVNO,IVT_INVDT,IVT_PORNO,IVT_PORDT,IVT_DSTDS,";
			M_strSQLQRY +="IVT_LR_NO,IVT_LD_DT,IVT_PRDDS,IVT_INVQT,IVT_INVPK,";
			M_strSQLQRY +="IVT_INVRT,IVT_ASSVL,IVT_INVVL,INV_TRPCD,INV_LRYNO,IVT_SALTP,";
			M_strSQLQRY +="PT_PRTNM,PT_ECCNO,PT_TINNO,PT_ADR01,PT_ADR02,PT_ADR03 ";
			M_strSQLQRY +=" from MR_IVTRN,CO_PTMST where PT_PRTCD=IVT_CNSCD AND PT_PRTTP='C' AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO='"+txtINVNO.getText().trim()+"'";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(cl_dat.M_intLINNO_pbst >60)
					{
				   		dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO +=1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}		
					
					
					
					dosREPORT.writeBytes(padSTRING('R',"",40));	
					strINVNO=nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
					if(!strINVNO.equals(strPINVNO))
					{
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),9));
						dosREPORT.writeBytes("\n");
					
						dosREPORT.writeBytes(padSTRING('R',"",40));	
					
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),""),9));	
						dosREPORT.writeBytes("\n\n\n\n\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),35));	
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PORNO"),""),10));	
					
						L_datTMPDT =M_rstRSSET.getDate("IVT_PORDT");
						//System.out.println("DAte "+L_datTMPDT);
						if(L_datTMPDT !=null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),13));	
						else
							dosREPORT.writeBytes(padSTRING('R'," ",13));	
					
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""),35));	
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_DSTDS"),""),25));	
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADD02"),""),35));	
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_TONNO"),""),25));	
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADD03"),""),35));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_LR_NO"),""),10));	
						L_datTMPDT =M_rstRSSET.getDate("IVT_LR_DT");
						//System.out.println("DAte "+L_datTMPDT);
						if(L_datTMPDT !=null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),13));	
						else
							dosREPORT.writeBytes(padSTRING('R'," ",13));		

						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",35));	
						//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_DOCNO"),""),10));	
					
						
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",35));	
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ECCNO"),""),20));	
						dosREPORT.writeBytes("\n");
					}
					else
					{
						prnMIDLE();
					}
					
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	
	
	public void prnMIDLE()
	{
		try
		{
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),25));	
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),10));	
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVPK"),""),10));	
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVRT"),""),10));	
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),""),12));	
			dosREPORT.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFooter");
		}
		
	}
	
	public void prnFOTER()
	{
		
		try
		{
			dosREPORT.writeBytes("\n");
		
			dosREPORT.writeBytes(padSTRING('L',"",42));	
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),""),40));
		
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),""),40));	
		
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),""),40));	
		
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),""),10));	
				
			dosREPORT.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFooter");
		}
		}
	/**
	 * Method to generate the header of the Report.
	*/
	void prnHEADER()
	{
		try
		{
			
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
		}
	}
}