/*
System Name		: Materials Management System
Program Name	: Nuemerical Stock Ledger
Author			: Mr.S.R.Mehesare

Modified Date	: 20/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;import java.awt.Color; import java.sql.ResultSet;

/**<pre>
System Name : Material Management System.
 
Program Name : Nuemerical Stock Ledger.

Purpose : Program for Nuemerical Stock Ledger Report for given Date & Dept. Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
MM_ISMST       IS_STRTP,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_TAGNO,IS_BATNO   #
MM_MRMST       MR_MMSBS,MR_STRTP,MR_MRNNO,MR_MATCD                     #
MM_SAMST       SA_MMSBS,SA_STRTP,SA_SANNO,SA_MATCD                     #
MM_STPRC       STP_MMSBS,STP_STRTP,STP_MATCD                           #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    
txtTODAT    
txtFMMCD
txtTOMCD
txtFMMDS
txtTOMDS
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
To generate the Report data is taken from the follwing tables for specified Condiations
 & inserted it in the temporary table :-

  1) From table MM_GRMST for :-
     1) GR_ACPDT between specified Date Range
     2) AND GR_MATCD between 
     3) AND isnull(GR_STSFL,'')='2'
					
  2) From table MM_ISMST & CO_CDTRN for :-
     1) date(IS_AUTDT) between Specified Date Range
     2) AND IS_MATCD between  Spcified Material Code Range.
     3) AND isnull(IS_STSFL,'')='2' 
     4) AND CMT_CGMTP='SYS' 
     5) AND CMT_CGSTP='COXXDPT' 
     6) AND CMT_CODCD=IS_DPTCD
					
   3) From table MM_MRMST for :-
     1) MR_MRNDT between given Date Range
     2) AND MR_MATCD between specified range of Material Code 
     3) AND isnull(MR_STSFL,'')='2'

   4) From Table MM_SAMST for :-
     1) SA_SANDT between given date range
     2) AND SA_MATCD between specified Range Of Material Code
     3) AND SA_SANQT > 0 
     4) AND isnull(SA_STSFL,'')<>'X'

   5) From table MM_SAMST for :-
     1) SA_SANDT between specified Date Range
     2) AND SA_MATCD between Specified Material Code Range
     3) AND SA_SANQT < 0 
     4) AND isnull(SA_STSFL,'')<>'X'

  Report Data is taken from MM_STPRC for condiations :-
     1) STP_STRTP = pecifie Store Type
     2) AND STP_MATCD between given date range.
  Along with this data is also taken from the Temporary table for :-
     1) TT_STRTP = Specified Store Type.
     2) AND TT_MATCD Specified Material Code Range.

<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then Current Date.
    - Department Codes entered must be valid.
</I> */


class mm_rpnsl extends cl_rbase
{										/** JTextField to Specify & to display To Date.*/
	private JTextField txtTODAT; 		/** JTextField to Specify & to display From Date.*/
	private JTextField txtFMDAT;		/** JTextField to specify From Material Code.*/
	private JTextField txtFMMCD;		/** JTextField to specify To Material Code.*/
	private JTextField txtTOMCD;		/** JTextField to display From Material Description.*/
	private JTextField txtFMMDS;		/** JTextField to display To Material Description.*/
	private JTextField txtTOMDS;		/** String variable for generated Report File Name.*/
	private String strFILNM;			/** Integer variable to count the Number of records fetched to block the Report if no data.*/
	private int intRECCT;				/** DataOutputstream Object to generate & hold the stream of data.*/
	private DataOutputStream dosREPORT;	/** FileOutputStream Object to generate the report file from Stream of Data.*/	
	private FileOutputStream fosREPORT; /** String variable t print Cotted line in the Report.*/
	private String strDOTLN = "---------------------------------------------------------------------------------------------------";
	
	mm_rpnsl()
	{
		super(2);
		try
		{			
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);					
			add(new JLabel("From Date"),4,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),4,4,1,1,this,'L');
			add(new JLabel("To Date"),5,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),5,4,1,1,this,'L');
		
			add(new JLabel("From Mat. Code"),6,3,1,2,this,'L');
			add(txtFMMCD = new TxtNumLimit(10.0),6,4,1,1,this,'L');
			add(txtFMMDS = new JTextField(),6,5,1,4,this,'L');
			add(new JLabel("To Mat. Code"),7,3,1,2,this,'L');
			add(txtTOMCD = new TxtNumLimit(10.0),7,4,1,1,this,'L');
			add(txtTOMDS = new JTextField(),7,5,1,4,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	/**
	 * Super Class method overrided to Enable & Disable the Components.
	 */
	void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(P_flgSTATE);
		if(P_flgSTATE == true)
		{
			txtFMMDS.setEnabled(false);
			txtTOMDS.setEnabled(false);
		}
	}
	public void actionPerformed(ActionEvent P_AE)
	{
		try
		{
			super.actionPerformed(P_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst )
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtFMDAT.requestFocus();
					setENBL(true);
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() > 0)			
					txtTODAT.requestFocus();
				else 
					setMSG("To Date cannot be Blank..",'E');
			}
			else if(M_objSOURC == txtTODAT )
			{
				if(txtTODAT.getText().trim().length() > 0)			
					txtFMMCD.requestFocus();
				else 
					setMSG("From Date cannot be Blank..",'E');
			}
			else if(M_objSOURC == txtFMMCD)
			{
				if(txtFMMCD.getText().trim().length() == 10)
				{
					M_strSQLQRY ="Select CT_MATDS from CO_CTMST where CT_CODTP='CD'";
					M_strSQLQRY += " AND CT_MATCD ='"+txtFMMCD.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							txtFMMDS.setText(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
						M_rstRSSET.close();
					}
					txtTOMCD.requestFocus();
				}
				else
					setMSG("Invalid Material Code, Press F1 to select From List..",'E');
			}
			else if(M_objSOURC == txtTOMCD )
			{
				if(txtTOMCD.getText().trim().length() == 10)
				{
					M_strSQLQRY ="Select CT_MATDS from CO_CTMST where CT_CODTP='CD'";
					M_strSQLQRY += " AND CT_MATCD ='"+txtTOMCD.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							txtTOMDS.setText(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
						M_rstRSSET.close();
					}
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else
					setMSG("Invalid Material Code, Press F1 to select From List..",'E');			
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == txtFMDAT)			
			setMSG("Please Enter From Date to specify Date Range..",'N');
		else if(M_objSOURC == txtTODAT)			
			setMSG("Please Enter To Date to specify Date Range..",'N');
		else if(M_objSOURC == txtFMMCD)
			setMSG("Please Enter From Material code to specify Range selection..",'N');
		else if(M_objSOURC == txtTOMCD)
			setMSG("Please Enter To Material code to specify Range selection..",'N');
	}	
	public void keyPressed(KeyEvent P_KE)
	{
		super.keyPressed(P_KE);
		try
		{			
			if(P_KE.getKeyCode() == P_KE.VK_F1)
			{
				cl_dat.M_flgHELPFL_pbst = true; 				
				setCursor(cl_dat. M_curWTSTS_pbst);
				if(M_objSOURC == txtFMMCD)
				{
					M_strHLPFLD = "txtFMMCD";
					M_strSQLQRY ="Select CT_MATCD,CT_MATDS from CO_CTMST where CT_CODTP='CD'";
					if(txtFMMCD.getText().trim().length()>0)
						M_strSQLQRY += " AND CT_MATCD like '"+txtFMMCD.getText()+"%'";
					if(txtTOMCD.getText().trim().length() == 10)
						M_strSQLQRY += " AND CT_MATCD <= '"+txtTOMCD.getText()+"'";
					M_strSQLQRY += " AND isnull(CT_STSFL,'')<>'X' order by CT_MATCD";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");
				}
				else if(M_objSOURC == txtTOMCD)
				{
					M_strHLPFLD = "txtTOMCD";
					M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST where CT_CODTP='CD'";
					if(txtTOMCD.getText().length()> 0)
						M_strSQLQRY += " AND CT_MATCD like '"+txtTOMCD.getText()+"%'";
					if(txtFMMCD.getText().length() == 10)
						M_strSQLQRY += " AND CT_MATCD >= '"+txtFMMCD.getText()+"'";
					M_strSQLQRY += " AND isnull(CT_STSFL,'')<>'X' order by CT_MATCD";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");					
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"VK_F1");
		}
	}
	/**
	 * Super Class method overrided to execuate the F1 Help for Selected Field.
	 */
	void exeHLPOK()
	{		
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
		if(M_strHLPFLD == "txtFMMCD")
		{
			txtFMMCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMMDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD == "txtTOMCD")
		{
			txtTOMCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOMDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpnsl.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpnsl.doc";
			
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Nuemerical Stock Ledger"," ");
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
	 * Method to validate the inputs before execuation of the SQL Query.
	 */
	boolean vldDATA() 
	{
		try
		{
			if(txtFMDAT.getText().length() == 0)
			{
				setMSG("Please Enter From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().length() == 0)
			{
				setMSG("Please Enter To Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText()))<0)
			{
				setMSG("To date cannot be less than From Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(txtTOMCD.getText().length() == 0)
			{
				setMSG("Please Enter To Material Code ..",'E');
				txtTOMCD.requestFocus();
				return false;
			}
			if(txtFMMCD.getText().length() == 0)
			{
				setMSG("Please Enter From Material Code ..",'E');
				txtFMMCD.requestFocus();
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
			setMSG("Invalid Data ..",'E');
			return false;
		}
		return true;
	}
	
	/**
	 * Method to generate NUMERAICAL LEDGER REPORT
	 * 
	 * Method to generate NUMERAICAL LEDGER REPORT
	 * 
	 * <p> Generates report for given material code and duration as : <br>
	 * Create temporary table named as : USRCD+HH+MM e.g. SYS1710 is table created with SYS login at 1710hrs.<br>
	 * Populates temporary table with data for given period from MM_GRMST, MM_ISMST, MM_SAMST, MM_MRMST and assigns document type to the transaction.<br>
	 * Collects data from MM_STPRC for month opening and closing values<br>
	 * Collects data from Temporary table for transaction details for the month <br>
	 * Drop the temporary table after report is generated<br><br>
	 * Disply Material code wise transaction detail sorted by transaction date in sequence of : GRIN, MRN, +SAN, -SAN, MIN<br>
	 * Display opening and closing stock for the material code
	 */
	void getDATA()
	{
		if(!vldDATA())
			return;
		boolean L_flgNODATA=false;
		try
		{
			intRECCT = 0;
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO=0;		
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Nuemerical Stock Ledger</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			String L_strTBLNM=null;
			ResultSet L_rstRSSET=null;
			try
			{
				//GENERATING TEMP. TABLE NAME AS : USRCD+HH+MM
					//L_strTBLNM="TT_"+cl_dat.M_strUSRCD_pbst+cl_dat.M_txtCLKTM_pbst.getText().trim().substring(0,2)+cl_dat.M_txtCLKTM_pbst.getText().trim().substring(3,5);				
					L_strTBLNM="MM_TTNSL";				
					//M_strSQLQRY="Create table "+L_strTBLNM
					//	+"  (	TT_CMPCD varchar(2),"
					//	+"		TT_STRTP varchar(2),"
					//	+"		TT_MATCD varchar(10),"
					//	+"		TT_DOCTP varchar(1),"
					//	+"		TT_DOCNO varchar(10),"
					//	+"		TT_DOCRF varchar(20),"
					//	+"		TT_DOCQT Decimal(12,3),"
					//	+"		TT_DOCVL Decimal(12,2),"
					//	+"		TT_DOCDT Date,"
					//	+"		TT_TAGNO varchar(12))";		
						//+"		constraint pk_"+L_strTBLNM+" primary key (TT_STRTP,TT_MATCD,TT_DOCTP,TT_DOCNO,TT_DOCRF,TT_TAGNO))";
					cl_dat.M_flgLCUPD_pbst=true;
					cl_dat.exeDBCMT("trfDATA");
					M_strSQLQRY = "delete from "+L_strTBLNM;
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					cl_dat.exeDBCMT("trfDATA");
				//TRANSFER DATA FROM LIVE TABLES TO TEMP. TABLE
					cl_dat.M_flgLCUPD_pbst=true;
					setMSG("Collecting GRIN data ..",'N');
					M_strSQLQRY = "Insert into "+L_strTBLNM+" (Select GR_CMPCD,GR_STRTP,GR_MATCD,'1',GR_GRNNO,GR_BATNO,gr_ACPQT,gr_BILVL,GR_ACPDT,'' from MM_GRMST where GR_ACPDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))
						+"' and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_MATCD between '"+txtFMMCD.getText()+"' and '"+txtTOMCD.getText()+"' and isnull(GR_STSFL,'')='2')";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					cl_dat.exeDBCMT("trfDATA");
					
					setMSG("Collecting MIN data ..",'N');
					M_strSQLQRY = "Insert into "+L_strTBLNM+" (Select IS_CMPCD,IS_STRTP,IS_MATCD,'4',IS_ISSNO,CMT_SHRDS ,IS_ISSQT,IS_ISSVL,CONVERT(varchar,IS_AUTDT,101),IS_TAGNO from MM_ISMST,CO_CDTRN where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,IS_AUTDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))
						+"' and IS_MATCD between '"+txtFMMCD.getText()+"' and '"+txtTOMCD.getText()+"' and isnull(IS_STSFL,'')='2' and CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT' and CMT_CODCD=IS_DPTCD )";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					cl_dat.exeDBCMT("trfDATA");

					setMSG("Collecting MRN data ..",'N');
					M_strSQLQRY = "Insert into "+L_strTBLNM+" (Select MR_CMPCD,MR_STRTP,MR_MATCD,'2',MR_MRNNO,MR_MRNNO,MR_RETQT,MR_MRNVL,MR_MRNDT,MR_TAGNO from MM_MRMST where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_MRNDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))
						+"' and MR_MATCD between '"+txtFMMCD.getText()+"' and '"+txtTOMCD.getText()+"' and isnull(MR_STSFL,'')='2')";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					cl_dat.exeDBCMT("trfDATA");

					setMSG("Collecting SAN+ data ..",'N');
					M_strSQLQRY = "Insert into "+L_strTBLNM+" (Select SA_CMPCD,SA_STRTP,SA_MATCD,'3',SA_SANNO,SA_SANNO,SA_SANQT,SA_SANVL,SA_SANDT,''  from MM_SAMST where SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_SANDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))
						+"' and SA_MATCD between '"+txtFMMCD.getText()+"' and '"+txtTOMCD.getText()+"' and SA_SANQT>0 and isnull(SA_STSFL,'')<>'X')";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					cl_dat.exeDBCMT("trfDATA");
					
					setMSG("Collecting SAN- data ..",'N');
					M_strSQLQRY = "Insert into "+L_strTBLNM+" (Select SA_CMPCD,SA_STRTP,SA_MATCD,'5',SA_SANNO,SA_SANNO,SA_SANQT,SA_SANVL,SA_SANDT,''  from MM_SAMST where SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_SANDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))
						+"' and SA_MATCD between '"+txtFMMCD.getText()+"' and '"+txtTOMCD.getText()+"' and SA_SANQT<0 and isnull(SA_STSFL,'')<>'X')";
					//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					cl_dat.exeDBCMT("trfDATA");

					setMSG("Formatting data. Please wait ..",'N');
					cl_dat.exeDBCMT("trfDATA");
					//dosREPORT=new DataOutputStream(L_fosREPORT);
				//COLLECTING DATA IN RESULT SETS
					M_strSQLQRY = "Select count(*) from "+L_strTBLNM+" where TT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TT_strtp='"+M_strSBSCD.substring(2,4)+"'  and TT_MATCD between '"+txtFMMCD.getText()+"' and  '"+txtTOMCD.getText()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							if(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString(1),"0")) == 0)
							{
								setMSG("No data found ..",'E');
								M_rstRSSET.close();
								return;						
							}
							M_rstRSSET.close();
						//M_rstRSSET = null;
						}
					}
					L_rstRSSET=cl_dat.exeSQLQRY0("Select STP_MATCD MATCD, STP_MATDS MATDS, STP_MOSQT OPSQT, STP_MOSVL OPSVL, STP_MCSQT CLSQT, STP_MCSVL CLSVL from MM_STPRC where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_strtp='"+M_strSBSCD.substring(2,4)+"'  and STP_MATCD between '"+txtFMMCD.getText()+"' and '"+txtTOMCD.getText()+"' order by STP_MATCD");
					M_rstRSSET=cl_dat.exeSQLQRY1("Select TT_DOCNO DOCNO, TT_DOCDT DOCDT, TT_DOCTP DOCTP,TT_DOCRF DOCRF, TT_MATCD MATCD, TT_DOCQT DOCQT, TT_DOCVL DOCVL from "+L_strTBLNM+" where TT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TT_strtp='"+M_strSBSCD.substring(2,4)+"'  and TT_MATCD between '"+txtFMMCD.getText()+"' and '"+txtTOMCD.getText()+"' order by TT_MATCD,TT_DOCTP");
					if(M_rstRSSET==null)
					{
						setMSG("No data found ..",'E');
						L_flgNODATA=true;
						return;
					}
					String[] L_staTRNTP=new String[]{"GRIN","MRN","+SAN","MIN","SAN"};//TO PRINT TRANSACTION TYPE WRT DOCTP
					String L_strBLANK="  ";
					prnHEADER();
					L_rstRSSET.next();
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					//prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strBOLD);
					
					dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATCD"),"")+"  :   ");
					dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATDS"),""));
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);					
					dosREPORT.writeBytes("\n"+L_strBLANK+padSTRING('R',"Opening Balance : ",24));
					cl_dat.M_intLINNO_pbst++;
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSQT"),""),10));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSVL"),""),10)+"\n");
					cl_dat.M_intLINNO_pbst++;
					
					double L_dblYCSQT=0,L_dblYCSVL=0;//FOR YEAR CLOSING STOCK AND VALUE OF LAST MATERIAL CODE
					while(M_rstRSSET.next())
					{
						intRECCT = 1;
						L_strBLANK="  ";
						if(cl_dat.M_intLINNO_pbst>60)
						{
							dosREPORT.writeBytes("\n"+strDOTLN);					
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
						}
						while( !M_rstRSSET.getString("MATCD").equals(L_rstRSSET.getString("MATCD")))
						{//DISPLAY CLOSING BALANCE FOR CURRENT AND OPENING BALANCE FOR NEXT MATCD
							//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							//	prnFMTCHR(dosREPORT,M_strCPI10);
							dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Closing Balance : ",24));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("CLSQT"),""),20));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("CLSVL"),""),20)+"\n");
							cl_dat.M_intLINNO_pbst++;
							//crtHRLIN(40,"- ");
							dosREPORT.writeBytes("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "+"\n");
							cl_dat.M_intLINNO_pbst++;
							if(!L_rstRSSET.next())
								break;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATCD"),"")+"  :   ");
							dosREPORT.writeBytes(nvlSTRVL(L_rstRSSET.getString("MATDS"),"")+"\n");
							cl_dat.M_intLINNO_pbst++;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Opening Balance : ",24));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSQT"),""),20));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("OPSVL"),""),20)+"\n");
							cl_dat.M_intLINNO_pbst++;
							L_dblYCSQT=L_rstRSSET.getDouble("CLSQT");
							L_dblYCSVL=L_rstRSSET.getDouble("CLSVL");
						}
					//DISPLAY TRANSACTION DETAILS
						L_strBLANK="     ";
						dosREPORT.writeBytes(L_strBLANK+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("DOCDT"),""),11));
						dosREPORT.writeBytes(padSTRING('R',L_staTRNTP[M_rstRSSET.getInt("DOCTP")-1],7));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCNO"),""),12));
						if(M_rstRSSET.getString("DOCTP").equals("4"))
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCRF"),"").trim(),8));
						else
							dosREPORT.writeBytes(padSTRING('L',"",8));
						if(M_rstRSSET.getFloat("DOCVL")>0.0f&&!(L_staTRNTP[M_rstRSSET.getInt("DOCTP")-1].equals("MIN")))
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCQT"),""),15));
						else
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCQT"),""),40));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("DOCVL"),""),15)+"\n");
						cl_dat.M_intLINNO_pbst++;
					}
				//TOTAL FOR LAST MATERIAL CODE
					//prnFMTCHR(dosREPORT,M_strCPI10);
					dosREPORT.writeBytes(L_strBLANK+padSTRING('R',"Closing Balance : ",24));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSQT,3),20));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYCSVL,2),20)+"\n");
					cl_dat.M_intLINNO_pbst++;
					//crtHRLIN(50,"- ");
					dosREPORT.writeBytes("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "+"\n");
					cl_dat.M_intLINNO_pbst++;
					setMSG("Report completed.. ",'N');			
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strEJT);				
					}			
					if(M_rdbHTML.isSelected())			
					    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
					//dosREPORT.close();
					//fosREPORT.close();
					setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"getDATA");
			}
			finally
			{
				if(dosREPORT != null)
				{
					try
					{//CLOSE RESULT SETS AND DELETE TEMP. TABLE IF CREATED
						if(M_rstRSSET!=null)
								M_rstRSSET.close();
						if(L_rstRSSET!=null)
							L_rstRSSET.close();
						M_rstRSSET = null;
						L_rstRSSET = null;
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeDBCMT("Drop Table");
						//cl_dat.exeSQLUPD("Drop table "+L_strTBLNM+" cascade","");
						//setMSG("Table Deleted ..",'N');
						//cl_dat.exeDBCMT("");
						dosREPORT.close();
						dosREPORT=null;
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"getDATA");						
					}
					setCursor(cl_dat.M_curDFSTS_pbst);						
				}
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");			
		}
	}				
	/**
	 * Method to generate the header part of the report.
	 */
	private void prnHEADER() throws Exception
	{		
		
		dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-21 )+"Date     : "+cl_dat.M_txtCLKDT_pbst.getText()+"\n");		
		dosREPORT.writeBytes("Numerical Stock Ledger for the period from "+txtFMDAT.getText()+" to "+txtTODAT.getText()+"\n");		
		dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),strDOTLN.length()-21)+ "Page No. : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");
		dosREPORT.writeBytes(strDOTLN+"\n");
		dosREPORT.writeBytes("     "+padSTRING('R',"Trn. Date",11));
		dosREPORT.writeBytes(padSTRING('R',"Type",7));
		dosREPORT.writeBytes(padSTRING('R',"Ref. No.",12));
		dosREPORT.writeBytes(padSTRING('R',"Dept.",8));
		dosREPORT.writeBytes(padSTRING('R',"Qty.",15));
		dosREPORT.writeBytes(padSTRING('R',"Value",15));
		dosREPORT.writeBytes(padSTRING('R',"Qty.",15));
		dosREPORT.writeBytes(padSTRING('R',"Value",15)+"\n");
		dosREPORT.writeBytes(strDOTLN+"\n");
		cl_dat.M_intLINNO_pbst = 7;		
	}

}