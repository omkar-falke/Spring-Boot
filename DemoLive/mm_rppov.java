/*
System Name   : Material Management System
Program Name  : Vendor wise P.O Value
Program Desc. : 
Author        : Narender K. Virdi
Date          : 06/04/2004
Version       : MMS 2.0
*/
import java.awt.event.KeyEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.sql.Date;
import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.util.Hashtable;
/**<pre>
System Name : Material Management System.
 
Program Name :  Vendor wise P.O Value

Purpose : This report displays Currency ,Vendor and P.O. series wise
          sum of purchase order values. If minimum value is given then only
          those records are displayed where sum is greater than the given value. 

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD            #	     
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name    Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtFMDAT      PO_PORDT       MM_POMST      Date           P.O. Date
txtTODAT      PO_PORDT       MM_POMST      Date           P.O. Date
txtMINVL  SUM(PO_ITVAL)      MM_POMST      Decimal(10,2)  Total Value
--------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b>1) MM_POMST
                        2) CO_PTMST
<B>Conditions Give in Query:</b>
    Data is taken from MM_POMST and CO_PTMST for condiations
      1) PO_VENCD = PT_PRTCD 
      2) AND PT_PRTTP ='S' 
      3) AND ifnull(PO_STSFL,'') <>'X' 
      4) AND PO_PORDT BETWEEN given dates
      5) AND PO_STRTP = selected store type
      6) GROUP BY  PO_CURCD,PO_VENCD,PT_PRTNM,substring(PO_PORNO,1,3)
    if Minimum Value is given then 
      7) HAVING SUM(PO_ITVAL) greater then given min. Value.

<B>Validations & Other Information:</B>
    - To Date must be smaller than current date & greater than From date.
    - If Minimum limit for total value is given then only records having more value are displayed.
</I> */
class mm_rppov extends cl_rbase
{											/** Text Field For From Department Code*/
	private JTextField txtFMDAT;			/** Text Field For To Department code*/
	private JTextField txtTODAT;			/**	Text Field for vendor code */
	private JTextField txtMINVL;			/** String variable for generated report file Name.*/
	private String strFILNM;				/** FileOutputStream Object to generate the Report file from Stream of data.*/    	 
	private FileOutputStream fosREPORT ;	/** DataOutputStream Object to hold & generate the Stream of data.*/
    private DataOutputStream dosREPORT ;	/** Integer variable to generate the */
	private int intRECCT = 0;
	private String strDOTLN = "-----------------------------------------------------------------------------------------------";
	public mm_rppov()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			setMatrix(20,8);
			add(new JLabel("From Date"),3,4,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),3,5,1,1,this,'L');
			add(new JLabel("To Date"),4,4,1,1,this,'L');
			add(txtTODAT = new TxtDate(),4,5,1,1,this,'L');
			add(new JLabel("Min Value Limit"),5,4,1,1,this,'L');
			add(txtMINVL = new TxtNumLimit(12.2),5,5,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Cconstructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			try
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtFMDAT.requestFocus();
					if(txtTODAT.getText().trim().length()==0)
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);				
					if(txtFMDAT.getText().trim().length()==0)
					{
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);       				
						txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
					}
					setMSG("Please Enter Date to generate report..",'N');
					setENBL(true);
				}
				else
					setENBL(false);
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"ActionPerformed");
			}
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;				
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)
			{
				txtTODAT.requestFocus();
				setMSG("Enter the Date ..",'N');
			}
			else if(M_objSOURC == txtTODAT)
			{
				txtMINVL.requestFocus();
				setMSG("Enter Minimum value of P.O., if required...",'N');				
			}
			else if(M_objSOURC == txtMINVL)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
	
		try
		{
			//intRECCT = 0;
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rppov.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rppov.doc";				
			getDATA();			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Vendor wise P.O Value"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}	
	/**Method call On Save Button
	 */
	public void getDATA()
	{	
		java.util.Hashtable<String,String> L_hstCODDS = new Hashtable<String,String>();		
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strDPTCD = "";	//Local String for department name
			String L_strPORNO = "";	//local string for p.o. number
			String L_strCURCD ="";
			String L_strPCURCD ="";
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Purchase Order Value </title> ");
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE></HEAD>"); 
				dosREPORT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_SHRDS FROM CO_CDTRN WHERE "
				+"CMT_CGMTP = 'DOC' AND CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"POR'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_hstCODDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_SHRDS"));
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
				+"CMT_CGMTP = 'MST' AND CMT_CGSTP = 'COXXCUR'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_hstCODDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}
			//SQL string for given input
			M_strSQLQRY =  "SELECT PO_CURCD,PO_VENCD,PT_PRTNM,substring(PO_PORNO,1,3) L_POSRL,SUM(PO_ITVAL/PO_EXGRT) L_TOTVL "
				+"FROM MM_POMST,CO_PTMST WHERE PO_VENCD = PT_PRTCD AND PT_PRTTP ='S' AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'') <>'X' and PO_PORDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"'"
				+ " AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' "
				+ " AND PO_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
			M_strSQLQRY +=  " GROUP BY  PO_CURCD,PO_VENCD,PT_PRTNM,substring(PO_PORNO,1,3) ";
			if(txtMINVL.getText().length() >0)
				M_strSQLQRY +=" HAVING 	SUM(PO_ITVAL) >"+ txtMINVL.getText().trim();
			M_strSQLQRY += " ORDER BY PO_CURCD,PO_VENCD,PT_PRTNM,substring(PO_PORNO,1,3)";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			intRECCT = 0;
			if(M_rstRSSET != null)
			{
				prnHEADER();
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					L_strCURCD = nvlSTRVL(M_rstRSSET.getString("PO_CURCD"),""); 
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						prnHEADER();
					}
					if(!L_strCURCD.equals(L_strPCURCD))
					{
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						if(L_hstCODDS.containsKey((String)L_strCURCD))
						dosREPORT.writeBytes(padSTRING('R',L_hstCODDS.get(L_strCURCD).toString(),15));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						L_strPCURCD = L_strCURCD;
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst += 2;
					}
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						L_strDPTCD = "";
						L_strPORNO = "";
						prnHEADER();
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),53));
					if(L_hstCODDS.containsKey((String)nvlSTRVL(M_rstRSSET.getString("L_POSRL"),"")))
						dosREPORT.writeBytes(padSTRING('R',L_hstCODDS.get(nvlSTRVL(M_rstRSSET.getString("L_POSRL"),"")).toString(),8));
					//dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_TOTVL"),""),12));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("L_TOTVL"),2),12));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
					//intRECCT ++;
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n"+strDOTLN);			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strEJT);
				}
			}
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);		
	}
	/**method to validate data before generate the report this method is called before every report generation	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
			{
				setMSG("From Date Can not be greater than Today's Date..",'E');
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
			{
				setMSG("To Date Can not be greater than Today's Date..",'E');
				return false;
			}
			else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("From Date Can not be greater than To Date..",'E');
				return false;
			}			
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount()==0)
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
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	/**Method To Print Report Header
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");			
			dosREPORT.writeBytes("\n\n"+cl_dat.M_strCMPNM_pbst);			
			String L_strDESC ="Vendor Wise P.O. Value for the period between "+txtFMDAT.getText() +" and "+txtTODAT.getText();
			dosREPORT.writeBytes("\n"+padSTRING('R',L_strDESC,strDOTLN.length()-21));
			dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));			
			dosREPORT.writeBytes("\n"+padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes(padSTRING('R',"Page No : "+cl_dat.M_PAGENO,20));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");			
			dosREPORT.writeBytes("\n"+strDOTLN);
			dosREPORT.writeBytes("\n"+"Currency" +"\n");						
			dosREPORT.writeBytes("Vendor Name                                          Series    Total Value(in Currency)\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 10;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
}
