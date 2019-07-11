/*
System Name   : Material Management System
Program Name  : GRIN Forwarding Memo
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 10/10/2005
Version       : MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.KeyEvent;import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
/**<pre>
System Name : Material Management System.
 
Program Name : GRIN Forwarding Memo

Purpose : This program generates Report for GRIN Forwarding Memo for given date Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    GR_ACPDT       MM_GRMST      Date          Accepted Date
txtTODAT    GR_ACPDT       MM_GRMST      Date          Accepted Date   
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
    Data is taken from MM_GRMST for condiations :- 
   1) GR_STRTP = Selected Store Type
   2) AND GR_ACPDT Between given date range
   3) AND ifnull(GR_STSFL,'') = '2' 
   4) AND ifnull(GR_ACPQT,0) > 0
       
<B>Validations & Other Information:</B>
    - To date must be greater than from date & smaller than current date.
</I> */
class mm_rpgrf extends cl_rbase
{									/** JTextField to enter & to displey From Date.*/
	private JTextField txtFMDAT;	/** JTextField to enter & to diaplay To Date.*/
	private JTextField txtTODAT;	/** JTextField to enter & to display Receivers Name.*/
	private JTextField txtRECNM;	/** String Variable For Stores Type.*/
	private String strSTRTP;		/** String Variable For Store Name.*/
	private String strSTRNM;		/** String Variable for generated report File Name.*/
	private String strFILNM;		/** Integer vatiable to count number of Records fetched & to assign a serial number*/	
	private int intSRLNO;				/** FileOutputStream Object to generate the Report file from the stream of data.*/	
	private FileOutputStream fosREPORT; /** DataOutputStream Object to hold the stream of data to generate the Report file.*/
    private DataOutputStream dosREPORT;
	private String strDOTLN = "------------------------------------------------------------------------------------------------";
	private int intRECCT;
	mm_rpgrf()
	{
		super(2);
		setMatrix(20,8);		
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		M_vtrSCCOMP.remove(M_txtTODAT);

		add(new JLabel("From Date "),3,4,1,1,this,'L');
		add(txtFMDAT = new TxtDate(),3,5,1,1,this,'L');
		add(new JLabel("To Date "),4,4,1,1,this,'L');
		add(txtTODAT = new TxtDate(),4,5,1,1,this,'L');
		add(new JLabel("Receiver's Name"),5,4,1,1.2,this,'L');
		add(txtRECNM = new TxtLimit(50),5,5,1,3,this,'L');
		setENBL(false);
		M_pnlRPFMT.setVisible(true);
		intSRLNO = 1;
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);		
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					strSTRTP = M_strSBSCD.substring(2,4);
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
					setENBL(true);				
				}
				else
					setENBL(false);		
			}
			if(M_objSOURC ==  cl_dat.M_btnSAVE_pbst)
			{
				intSRLNO = 1;
				cl_dat.M_PAGENO = 0;
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);	
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC == txtFMDAT)
			{
				M_strHLPFLD = "txtFMDAT";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_ACPDT FROM MM_GRMST WHERE "
					+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND isnull(GR_STSFL,'') <> 'X' order by GR_ACPDT";
				cl_hlp(M_strSQLQRY,1,2,new String[]{"GRIN Number","GRIN Date"},2,"CT");
			}
			if(M_objSOURC == txtTODAT)
			{
				M_strHLPFLD = "txtTODAT";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_ACPDT FROM MM_GRMST WHERE "
					+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND isnull(GR_STSFL,'') <> 'X' order by GR_ACPDT";
				cl_hlp(M_strSQLQRY,1,2,new String[]{"GRIN Number","GRIN Date"},2,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		if(L_KE.getKeyChar() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
				{
					txtTODAT.requestFocus();
					setMSG("Enter To Date Or Press F1 For Help..",'N');
				}
				else
					setMSG("To Date cannot be null ..",'E');
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
				{
					txtRECNM.requestFocus();
					setMSG("Enter Receivers Name..",'N');
				}
				else
					setMSG("From Date cannot be null ..",'E');
			}
			if(M_objSOURC == txtRECNM)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}				
	}
	/**
	 * Super class method overrided to execuate the F1 help for the selected field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMDAT")
		{
			txtFMDAT.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD == "txtTODAT")
		{
			txtTODAT.setText(cl_dat.M_strHLPSTR_pbst);			
		}
	}
	/**
	 * Method to generate the report file & to send it to the selected gestination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpgrf.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgrf.doc";
			intRECCT = 0;
			
			getDATA();
			
			if(dosREPORT != null)
				dosREPORT.close();
			if(fosREPORT != null)
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"GRIN Forwarding Memo"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}
	}
	/**
	 * Method to fetch data from database & to club it with header & footer.
	 */
	public void getDATA()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generating Report..Wait For Some Time...",'N');
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>GRIN Forwarding Memo</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			String L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			String L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			prnHEADER();
			M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_VENCD,GR_VENNM FROM MM_GRMST WHERE "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND GR_ACPDT BETWEEN '"+L_strFMDAT+"' AND "
				+"'"+L_strTODAT+"' AND isnull(GR_STSFL,'') = '2' AND "
				+"isnull(GR_ACPQT,0) > 0  ORDER BY GR_GRNNO ASC ";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes(strDOTLN+"\n");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),10));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),15));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),45));
					dosREPORT.writeBytes("\n");
					intSRLNO++;
					cl_dat.M_intLINNO_pbst += 1;
				}
			}
			prnFOOTER();
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/** Method TO Print Header Of The Report
	 */
	public void prnHEADER()
	{
		strSTRTP = M_strSBSCD.substring(2,4);
		try
		{
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY3("SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSST' AND CMT_CODCD = '"+strSTRTP+"'");
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
					strSTRNM = L_rstRSSET.getString("CMT_CODDS").toString();
				L_rstRSSET.close();
			}
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,96)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"GRIN FORWARDING MEMO ",76));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst +"\n\n");			
			dosREPORT.writeBytes("From  :  Material Handeling Division    To  :  Account Dipertment"+"\n\n");
			dosREPORT.writeBytes("Period : "+txtFMDAT.getText().trim().toString()+" To "+txtTODAT.getText().trim().toString()+"\n");			
			dosREPORT.writeBytes("Please receive the following GRINs :              Attn. :"+txtRECNM.getText().trim().toString()+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,76));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");
			dosREPORT.writeBytes("Sr No.    GRIN Number    Vendor Name                                       Bill Number"+"\n");		
			dosREPORT.writeBytes(strDOTLN+"\n");			
			cl_dat.M_intLINNO_pbst = 12;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**Method to Print The Footer Of The Report
	 */
	public void prnFOOTER()
	{
		try
		{
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Kindly Acknowledge,"+"\n\n");			
			dosREPORT.writeBytes("Regards,"+"\n\n");			
			dosREPORT.writeBytes("Authorised Signatory"+"\n\n\n");			
			dosREPORT.writeBytes("Mat. Handling Div."+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTER");
		}
	}
	/**
	 * Method To validate inputs before execuation of the SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() == 0)
			{
				txtFMDAT.requestFocus();
				setMSG("Enter Date Or Press F1 For Help..",'E');
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				txtTODAT.requestFocus();
				setMSG("Enter Date Or Press F1 For Help..",'E');
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim())) < 0)
			{
				txtTODAT.requestFocus();
				setMSG("To Date Should Be Grater Than Or Equal TO From Date..",'E');
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
}