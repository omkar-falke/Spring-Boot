/*
System Name		: Materials Management System
Program Name	: Transaction Count From / To Date
Author			: Mr.S.R.Mehesare

Modified Date	: 13/10/2005
Version			: v2.0.0
*/
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.sql.ResultSet;import java.util.Date;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;import java.awt.event.KeyEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : Transaction Count From / To Date

Purpose : This program generates Report for Transaction Count for given date Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              # 
MM_ISMST       IS_STRTP,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_TAGNO,IS_BATNO   #
MM_MRMST       MR_MMSBS,MR_STRTP,MR_MRNNO,MR_MATCD                     #
MM_SAMST       SA_MMSBS,SA_STRTP,SA_SANNO,SA_MATCD                     #
MM_GPTRN       GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MATCD                     #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT   IN_INDDT        MM_INMST      Date          Indent Date
txtTODAT   IN_INDDT        MM_INMST      Date          Indent Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   For INDENTs, Data is taken from MM_INMST for condiations :- 
      1) IN_INDDT between to given From Date & To Date 
      2) AND IN_STRTP = selected Store Type
      3) AND isnull(IN_STSFL,' ') <> 'X'
   For GRINs, Data is taken from MM_GRMST,MM_STMST and CO_CDTRN for condiations :- 
      1) ST_MATCD = GR_MATCD 
      2) AND CMT_CODCD = ST_MATTP 
      3) AND CMT_CGMTP = 'SYS' 
      4) AND CMT_CGSTP = 'MMXXMTP' 
      5) AND isnull(GR_STSFL,' ') <> 'X' 
      6) AND isnull(ST_STSFL,' ') <> 'X' 
      7) AND isnull(CMT_STSFL,'') <> 'X'
      8) AND GR_GRNDT between given Date Range.
      9) AND GR_STRTP = selected Store Type
  For ISSUEs Data is taken from MM_ISMST,MM_STMST and CO_CDTRN for condiations :- 
      1) ST_MATCD = IS_MATCD 
      2) AND CMT_CODCD = ST_MATTP 
      3) AND CMT_CGMTP = 'SYS' 
      4) AND CMT_CGSTP = 'MMXXMTP' 
      5) AND isnull(IS_STSFL,' ') <> 'X'
      6) AND isnull(ST_STSFL,' ') <> 'X'
      7) AND isnull(CMT_STSFL,'') <> 'X' 
      8) AND IS_ISSDT betweengiven Date Range.
      9) AND IS_STRTP = selected Store Type.
  For MRNs Data is taken from MM_MRMST,MM_STMST and CO_CDTRN for condiations :- 			 
      1) ST_MATCD = MR_MATCD 
      2) AND CMT_CODCD = ST_MATTP 
      3) AND CMT_CGMTP = 'SYS' 
      4) AND CMT_CGSTP = 'MMXXMTP' 
      5) AND isnull(MR_STSFL,' ') <> 'X' 
      6) AND isnull(ST_STSFL,' ') <> 'X' 
      7) AND isnull(CMT_STSFL,'') <> 'X' 
      8) AND MR_MRNDT between given Date Range.
      9) AND MR_STRTP = selected Store Type.
  For SAN RECEIPTs Data is taken from MM_SAMST for condiations :-
      1) SA_SANQT > 0 
      2) AND isnull(SA_STSFL,' ') <> 'X' 
      3) AND SA_SANDT between given From Date & To Date.
      4) AND SA_STRTP = Selected Store Type.
  For SAN ISSUEs Data is taken from MM_SAMST for condiations :-
      1) SA_SANQT < 0 
      2) AND isnull(SA_STSFL,' ') <> 'X' 
      3) AND SA_SANDT between given From Date & TO Date
      4) AND SA_STRTP = selected Store Type.
  For GATE PASSes Data is taken from MM_GPTRN and CO_CDTRN for condiations :-
      1) SUBSTR(CMT_CODCD,2,3) = GP_MGPTP 
      2) AND GP_STRTP = selected Store Type
      3) AND CMT_CGMTP = 'DOC' 
      4) AND CMT_CGSTP = 'MMXXMGP' 
      5) AND GP_MGPDT between given From Date & To Date
      6) AND isnull(GP_STSFL,' ') <> 'X'
  For CATALOGUEs COUNT(DISTINCT CT_MATCD) is taken from table CO_CTMST.:-			
					
 For STOCK MASTER
    Items as on given date Data is taken from MM_STMST for condiatation :-
      1) isnull(ST_STSFL,' ') <> 'X'
    Addition Modification data is taken from MM_STMST
      1) isnull(ST_STSFL,' ') <> 'X' 
      2) AND ST_LUPDT between given From Date & To Date.
      3) AND isnull(ST_STKQT,0) > 0 
      4) AND isnull(ST_STSFL,' ') <> 'X'
    Number of Items on stock is taken from MM_STMST for condition :-
      1) isnull(ST_STKQT,0) > 0 
      2) AND isnull(ST_STSFL,' ') <> 'X' ";

<B>Validations & Other Information:</B>    
    - To date must be greater than from date & smaller than current date.
</I> */
class mm_rptrc extends cl_rbase
{									/** JTextField to display & to enter From date to spacify date Range to generate Report.*/	
	private JTextField txtFMDAT;	/** JTextField to display & to enter To date to spacify date Range to generate Report.*/	
	private JTextField txtTODAT;	/** FileOutputStream Object to generate the Report from stream of Data.*/
	private FileOutputStream fosREPORT;/** DataOutputStream Object to hold the Data to generate the Report.*/
	private DataOutputStream dosREPORT;	/** String variable for generated Report File Name.*/	
	private String strFILNM;			/** Integer variable to count the number of records fetched tobolck the Report if no data found.*/
	private int intRECCT = 0;
	private String strDOTLN = "------------------------------------------------------------------------------------------------";		
	public mm_rptrc()
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
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() ==10)
					txtTODAT.requestFocus();
			}
			else if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() ==10)
					cl_dat.M_btnSAVE_pbst.requestFocus();	
			}			
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtFMDAT)
			setMSG("Enter From Date..",'N');
		if(M_objSOURC == txtTODAT)
			setMSG("Enter To To Date..",'N');
	}
	/**
	* Method to validate the inputs before execuation of the SQL Query. 
	*/	
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
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
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
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/**
	 * Method to generate the Report File & to forward it to selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rptrc.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rptrc.doc";			
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Transaction Count From / To Date"," ");
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
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA()
	{
		try
		{
			intRECCT = 0;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Transaction Count From / To Date</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}		
			
			prnHEADER();
														
			M_strSQLQRY = "SELECT COUNT(DISTINCT IN_INDNO) INDNO,COUNT(DISTINCT IN_MATCD) MATCD,"
				+"SUBSTRING(IN_MMSBS,1,2) MMSBS FROM MM_INMST WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND "
				+"IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(IN_STSFL,' ') <> 'X' "
				+"GROUP BY SUBSTRING(IN_MMSBS,1,2)";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\nINDENTS\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					if(Integer.parseInt(M_rstRSSET.getString("MMSBS")) == 01)
					{
						dosREPORT.writeBytes(padSTRING('R',"",5));
						dosREPORT.writeBytes(padSTRING('R',"WORKS",41));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("INDNO"),"0.0"),20));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MATCD"),""),30));
					}
					if(Integer.parseInt(M_rstRSSET.getString("MMSBS")) == 02)
					{
						dosREPORT.writeBytes(padSTRING('R',"",5));
						dosREPORT.writeBytes(padSTRING('R',"H.O.",41));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("INDNO"),"0.0"),20));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MATCD"),""),30));
					}
				}
			}				
			/**GRIN  QUERY 			 */
			M_strSQLQRY = "SELECT ST_MATTP,CMT_CODDS,COUNT(DISTINCT GR_GRNNO) GRNNO,"
				+"COUNT(DISTINCT GR_MATCD) MATCD FROM MM_GRMST,MM_STMST,CO_CDTRN WHERE "
				+"ST_CMPCD=GR_CMPCD and ST_MATCD = GR_MATCD AND CMT_CODCD = ST_MATTP AND CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'MMXXMTP' AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,' ') <> 'X' AND "
				+"isnull(ST_STSFL,' ') <> 'X' AND isnull(CMT_STSFL,'') <> 'X'  AND "
				+"GR_GRNDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND "
				+"GR_STRTP = '"+M_strSBSCD.substring(2,4)+"'GROUP BY ST_MATTP,CMT_CODDS "
				+"ORDER BY ST_MATTP";
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\nGRIN\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODDS"),41));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("GRNNO"),20));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}
			//ISSUE QUERY 			 
			M_strSQLQRY = "SELECT ST_MATTP,CMT_CODDS,COUNT(DISTINCT IS_ISSNO) ISSNO,"
				+"COUNT(DISTINCT IS_MATCD) MATCD FROM MM_ISMST,MM_STMST,CO_CDTRN WHERE "
				+"ST_CMPCD=IS_CMPCD AND ST_MATCD = IS_MATCD AND CMT_CODCD = ST_MATTP AND CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'MMXXMTP' AND IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IS_STSFL,' ') <> 'X' AND "
				+"isnull(ST_STSFL,' ') <> 'X' AND isnull(CMT_STSFL,'') <> 'X' AND "
				+"IS_ISSDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND " 
				+"IS_STRTP = '"+M_strSBSCD.substring(2,4)+"'GROUP BY ST_MATTP,CMT_CODDS "
				+"ORDER BY ST_MATTP";
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\nISSUE\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODDS"),41));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("ISSNO"),20));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}
			//MRN  QUERY		 
			M_strSQLQRY = "SELECT ST_MATTP,CMT_CODDS,COUNT(DISTINCT MR_MRNNO) MRNNO,"
				+"COUNT(DISTINCT MR_MATCD) MATCD FROM MM_MRMST,MM_STMST,CO_CDTRN WHERE "
				+"ST_CMPCD=MR_CMPCD and ST_MATCD = MR_MATCD AND CMT_CODCD = ST_MATTP AND CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'MMXXMTP' AND MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,' ') <> 'X' AND "
				+"isnull(ST_STSFL,' ') <> 'X' AND isnull(CMT_STSFL,'') <> 'X' AND MR_MRNDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND " 
				+"MR_STRTP = '"+M_strSBSCD.substring(2,4)+"'GROUP BY ST_MATTP,CMT_CODDS "
				+"ORDER BY ST_MATTP";
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\nMRN\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODDS"),41));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MRNNO"),20));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}
			//SAN RECEIPT  QUERY		 
			M_strSQLQRY = "SELECT COUNT(DISTINCT SA_SANNO) SANNO,COUNT(DISTINCT SA_MATCD) MATCD "
				+"FROM MM_SAMST WHERE SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_SANQT > 0 AND isnull(SA_STSFL,' ') <> 'X' AND "
				+"SA_SANDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND "
				+"SA_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\nSAN\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',"Receipt ",41));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("SANNO"),20));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}
			//SAN ISSUE  QUERY		 
			M_strSQLQRY = "SELECT COUNT(DISTINCT SA_SANNO) SANNO,COUNT(DISTINCT SA_MATCD) MATCD "
				+"FROM MM_SAMST WHERE SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_SANQT < 0 AND isnull(SA_STSFL,' ') <> 'X' AND "
				+"SA_SANDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND "
				+"SA_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',"Issue  ",41));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("SANNO"),20));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}
			//GATE PASS QUERY		
			M_strSQLQRY="SELECT CMT_CODDS,COUNT(DISTINCT GP_MGPNO) MGPNO,COUNT(DISTINCT GP_MATCD) MATCD"
				+" FROM MM_GPTRN,CO_CDTRN WHERE SUBSTRING(CMT_CODCD,2,3) = GP_MGPTP AND "
				+"GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND CMT_CGMTP = 'DOC' AND "
				+"CMT_CGSTP = 'MMXXMGP' AND GP_MGPDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND "
				+"isnull(GP_STSFL,' ') <> 'X' GROUP BY CMT_CODDS " ;
					
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\nGATE PASS\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODDS"),41));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MGPNO"),20));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}
			//CATALOGUE QUERY		
			M_strSQLQRY = "SELECT COUNT(DISTINCT CT_MATCD) MATCD FROM CO_CTMST";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\nCATALOGUE MASTER\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',"Items As On "+cl_dat.M_strLOGDT_pbst,61));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}	
			//STOCK MASTER QUERY	
			M_strSQLQRY = "SELECT COUNT(DISTINCT ST_MATCD) MATCD FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,' ') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\nSTOCK MASTER\n");
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',"Items As On "+cl_dat.M_strLOGDT_pbst,61));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}	
			//Addition Modification 
			M_strSQLQRY = "SELECT COUNT(ST_MATCD) MATCD FROM MM_STMST WHERE "
				+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,' ') <> 'X' AND ST_LUPDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND "
				+"isnull(ST_STKQT,0) > 0 AND isnull(ST_STSFL,' ') <> 'X' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',"Addition / Modification For Given Period ",61));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}	
			//NO OF ITEMS ON STOCK
			M_strSQLQRY = "SELECT COUNT(ST_MATCD) MATCD FROM MM_STMST WHERE "
				+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STKQT,0) > 0 AND isnull(ST_STSFL,' ') <> 'X' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					intRECCT = 1;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',"No Of Items In Stock ",61));
					dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("MATCD"),30));
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n" + strDOTLN);
			setMSG("Report generation Completed..",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			dosREPORT.close();
			fosREPORT.close();	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * Method to generate the header part of the Report.
	 */
	public void prnHEADER()
	{
		try
		{			
			cl_dat.M_PAGENO = 1 ;
			dosREPORT.writeBytes("\n"+cl_dat.M_strCMPNM_pbst +"\n");
			dosREPORT.writeBytes(padSTRING('R',"Transaction Count From "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date     : "+cl_dat.M_strLOGDT_pbst +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No. : "+cl_dat.M_PAGENO +"\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");			
			dosREPORT.writeBytes("Transaction Type                              No. Of Tranasactions                   No.Of Items"+"\n");			
			dosREPORT.writeBytes(strDOTLN);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}