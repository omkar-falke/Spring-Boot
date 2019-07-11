/*
System Name   : Material Management System
Program Name  : List Of Pending GRIN
Program Desc. : 
Author        : Mr S.R.Mehesare
Date          : 14/10/2005
Version       : MMS V2.0.0

*/

import java.sql.ResultSet;import java.util.Hashtable;
import javax.swing.InputVerifier;import java.sql.Date;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;import javax.swing.JComboBox;import javax.swing.JComponent;
import java.awt.event.KeyEvent;import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;


/**<pre>
System Name : Material Management System.
 
Program Name : List Of GRIN Pending for Inspection

Purpose : This program generates Report for List Of GRIN which are pending for 
inspection.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD            #
MM_WBTRN       WB_DOCTP,WB_DOCNO,WB_SRLNO                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtDPTCD   PO_DPTCD/IN_DPTCD        MM_GRMST/MM_POMST  VARCHAR(3)    Department Code
txtNUMDY   CURRENT_DATE-GR_GRNDT    MM_GRMST           DATE          GRIN Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
For Purches order Data is taken from MM_GRMST,MM_POMST and MM_WBTRN for condiations :- 
      1) PO_STRTP = GR_STRTP 
      2) AND PO_PORNO = GR_PORNO 
      3) AND GR_STRTP = selected store Type 
      4) AND WB_DOCTP = '02' 
      5) AND WB_DOCNO = GR_GINNO 
    if number of pending days is given
      6) AND (days(CURRENT_DATE) - days(GR_GRNDT)) > given number of pending days
      7) AND ifnull(GR_STSFL,'') <> 'X' 
      8) AND GR_ACPDT is null
    if Department code is given
      9) AND PO_DPTCD = given Department Code.				
			
For cash & replacement Material Data is taken from MM_GRMST,MM_INMST and MM_WBTRN for condiations :-
      1) IN_STRTP = GR_STRTP 
      2) AND IN_INDNO = GR_PORNO 
      3) AND GR_STRTP = selected Store Type.
      4) AND WB_DOCTP = '02' 
      5) AND WB_DOCNO = GR_GINNO 
    if number of painding days are given
      6) AND (days(CURRENT_DATE) - days(GR_GRNDT)) > given number of painding days
      7) AND ifnull(GR_STSFL,'') <> 'X' 
      8) AND GR_ACPDT is null 
    if department code is specified
      9) AND IN_DPTCD = given Department Code

<B>Validations & Other Information:</B>    
    - If department code is specified then it should be valid.
</I> */
class mm_rppin extends cl_rbase
{									/** JTextField to display & to enter Number Of Days*/
	private JTextField txtNUMDY;	/** JTextField to diaplay & to enter Department Code.*/
	private JTextField txtDPTCD;	/** JTextField to diaplay & to enter Department Name.*/	
	private JTextField txtDPTNM;	/** JComboBox to display & to user to select GRIN Type.*/
	private JComboBox cmbGRNTP;	
								/** String variable for Store Type.*/	
	private String strSTRTP;	/** String variable for Store Name.*/	
	private String strSTRNM="";	/** String variable for GRIN Number.*/
	private String strGRNNO;	/** String variable GRIN Date.*/
	private String strGRNDT;	/** String variable DMR Number.*/
	private String strDMRNO;	/** String variable Material Code.*/
	private String strMATCD;	/** String variable Material Description.*/
	private String strMATDS;	/** String variable Received Quantity.*/
	private String strRECQT;	/** String variable P.O. Number.*/
	private String strPORNO;	/** String variable Vender Code.*/
	private String strVENCD;	/** String variable Vender Name.*/
	private String strVENNM;	/** String variable Number Of Days.*/
	private String strNODAY;	/** String variable Pending Days.*/
	private String strPENDY;	/** String variable Department Code.*/
	private String strDPTCD;	/** String variable Departmetn Name.*/
	private String strDPTNM;	/** String Variable for generated Report file Name.*/
	private String strFILNM;	/** Integer variable to count the number of records fetched to block the report if data not found.*/
	private int intRECCT = 0;	/**	Hashtable Object to hold store type & associated description.*/
	private Hashtable<String,String> hstSTRTP;	/**	Hashtable Object to hold deparatment code & associated description.*/
	private Hashtable<String,String> hstDPTNM;	/** FileOutputStream Object to generate the Report files from stream of data.*/
	private FileOutputStream fosREPORT ;/** DataOutputStream Object to hold stream of data to generate the Report.*/
    private DataOutputStream dosREPORT ;
	private String strDOTLN = "------------------------------------------------------------------------------------------------------------------------------------------------------";
	
	mm_rppin()
	{		
		super(2);
		try
		{
			setMatrix(20,8);		
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
					
			add(new JLabel("GRIN Type "),4,3,1,1,this,'L');
			add(cmbGRNTP = new JComboBox(),4,4,1,1.5,this,'L');
			add(new JLabel("Pending From"),5,3,1,1,this,'L');
			add(txtNUMDY = new TxtNumLimit(3.0),5,4,1,1,this,'L');
			add(new JLabel("(Days)"),5,5,1,1,this,'L');
			add(new JLabel("Department "),6,3,1,1,this,'L');			
			add(txtDPTCD = new TxtNumLimit(3.0),6,4,1,1,this,'L');
			add(txtDPTNM = new JTextField(),6,5,1,2,this,'L');
																							
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
				+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXGRN' AND "
				+"isnull(CMT_STSFL,' ') <>'X' AND CMT_CODCD NOT IN('04','47','48')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())				
					cmbGRNTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));				
				M_rstRSSET.close();
			}
			hstSTRTP = new Hashtable<String,String>();
			String strTEMP="";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1("SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSST'");
			if(L_rstRSSET !=null)
			{				
				while(L_rstRSSET.next())
				{
					strTEMP = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD").toString(),"");				
					if(!strTEMP.equals(""))					
						hstSTRTP.put(strTEMP,L_rstRSSET.getString("CMT_CODDS").toString());					
				}
				L_rstRSSET.close();				
			}			
			hstDPTNM = new Hashtable<String,String>();
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT'";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{				
				while(L_rstRSSET.next())
				{
					strTEMP = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD").toString(),"");				
					if(!strTEMP.equals(""))					
						hstDPTNM.put(strTEMP,L_rstRSSET.getString("CMT_CODDS").toString());					
				}
				L_rstRSSET.close();				
			}			
			txtDPTCD.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"GRN Combo");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				txtNUMDY .requestFocus();
				setMSG("Please Enter the Number to specify number of pending days..",'N');
				setENBL(true);
				txtDPTNM.setEnabled(false);
			}
			else
				setENBL(false);	
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;	
		}
		else if(M_objSOURC == txtDPTCD)
		{
			try
			{
				if((txtDPTCD.getText().trim().length() == 3) ||(txtDPTCD.getText().trim().length() == 0))
				{
					txtDPTNM.setText("");
					if((txtDPTCD.getText().trim().length() == 3))
					{
						M_strSQLQRY = "SELECT CMT_CODDS FROM CO_CDTRN WHERE"
						+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT'"
						+" AND isnull(CMT_STSFL,' ') <>'X'"									
						+" AND CMT_CODCD = '"+txtDPTCD.getText().trim()+"'";					
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
								txtDPTNM.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
							M_rstRSSET.close();
						}
					}
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else
				{
					txtDPTNM.setText("");
					setMSG("Invalid Department code, press F1 to select from List..",'N');
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_ENTER");
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode()== L_KE.VK_ENTER)
		{	
			if(M_objSOURC == cmbGRNTP)
			{
				txtNUMDY.requestFocus();
				setMSG("Please Enter the Number to specify number of pending days..",'N');
			}
			else if(M_objSOURC == txtNUMDY)
			{
				txtDPTCD.requestFocus();
				setMSG("Please Enter Department Code Or Press F1 to select from List..",'N');
			}			
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtDPTCD)
			{
				txtDPTNM.setText("");
				M_strHLPFLD = "txtDPTCD";
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE"
					+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT'"
					+" AND isnull(cmt_stsfl,' ') <>'X'";			
				if(txtDPTCD.getText().trim().length() > 0)					
					M_strSQLQRY += "AND  CMT_CODCD LIKE '"+txtDPTCD.getText().trim()+"%'";
				 M_strSQLQRY += "ORDER BY CMT_CODDS";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code","Name"},2,"CT");
			}
		}
	}
	/**
	 * Supper class method overrided to execuate the F1 help for the selected field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtDPTCD"))
		{
			txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtDPTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
		}
	}
	/**
	 * Method to generate the Report File & to send at selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rppin.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rppin.doc";
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"List Of Pending GRIN"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to fetch data from the database & club it with header & footer.
	 */
	public void getDATA()
	{
		try
		{
			strSTRTP = M_strSBSCD.substring(2,4);
			strNODAY = txtNUMDY.getText().trim().toString();
			java.sql.Date jdtTEMP;
			String L_strGRNNO = "";
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>List Of Pending GRIN</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}	
			//// Store type is taken from hashtable
			if(hstSTRTP.containsKey(strSTRTP))
				strSTRNM = hstSTRTP.get(strSTRTP).toString();			
			
			prnHEADER();
			
			if(cmbGRNTP.getSelectedIndex() == 0)
			{
				M_strSQLQRY = "SELECT distinct GR_GRNNO,GR_GRNDT,GR_GINNO,GR_PORNO,GR_VENCD,"
					+"GR_VENNM,(day(getdate()) - day(GR_GRNDT)) PDAY,PO_DPTCD,WB_MATDS,WB_CHLQT "
					+"FROM MM_GRMST,MM_POMST,MM_WBTRN "
					+"WHERE PO_CMPCD=GR_CMPCD and PO_STRTP = GR_STRTP AND PO_PORNO = GR_PORNO AND "
					+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '02' AND WB_CMPCD=GR_CMPCD AND WB_DOCNO = GR_GINNO AND ";
				if(txtNUMDY.getText().trim().length() > 0)
					M_strSQLQRY += "(day(getdate()) - day(GR_GRNDT)) > "+txtNUMDY.getText().trim()+" AND ";
				M_strSQLQRY += "isnull(GR_STSFL,'') <> 'X' AND GR_ACPDT is null ";
				if(txtDPTCD.getText().trim().length()==3)
					M_strSQLQRY += " AND PO_DPTCD = '"+txtDPTCD.getText().trim()+"' ";
				M_strSQLQRY += "ORDER BY PDAY DESC";
			}
			else
			{
				M_strSQLQRY = "SELECT distinct GR_GRNNO,GR_GRNDT,GR_GINNO,GR_PORNO,GR_VENCD,IN_INDNO,"
					+"GR_VENNM,(day(getdate()) - day(GR_GRNDT)) PDAY,IN_DPTCD,WB_MATDS,WB_CHLQT "
					+"FROM MM_GRMST,MM_INMST,MM_WBTRN "
					+"WHERE IN_CMPCD=GR_CMPCD and IN_STRTP = GR_STRTP AND IN_INDNO = GR_PORNO AND "
					+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '02' AND WB_CMPCD=GR_CMPCD and WB_DOCNO = GR_GINNO AND ";
				if(txtNUMDY.getText().trim().length() > 0)
					M_strSQLQRY += "(day(getdate()) - day(GR_GRNDT)) > "+txtNUMDY.getText().trim()+" AND ";
				M_strSQLQRY += "isnull(GR_STSFL,'') <> 'X' AND GR_ACPDT is null ";
				if(txtDPTCD.getText().trim().length()==3)
					M_strSQLQRY += " AND IN_DPTCD = '"+txtDPTCD.getText().trim()+"' ";
				M_strSQLQRY += "ORDER BY PDAY DESC";
			}
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			strGRNDT = "";			
		
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
					jdtTEMP = M_rstRSSET.getDate("GR_GRNDT");
					if(jdtTEMP != null)
						strGRNDT = M_fmtLCDAT.format(jdtTEMP);
					strDMRNO = nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),"");
					strMATDS = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"");
					strRECQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0");
					if(cmbGRNTP.getSelectedIndex() == 0)
						strPORNO = nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),"");
					else
						strPORNO = nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),"");
					strVENCD = nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"");
					strVENNM = nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"");
					strPENDY = nvlSTRVL(M_rstRSSET.getString("PDAY"),"");
					if(cmbGRNTP.getSelectedIndex() == 0)
						strDPTCD = nvlSTRVL(M_rstRSSET.getString("PO_DPTCD"),"");
					else
						strDPTCD = nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"");
					/////// Department Name is taken from hashtable
					if(hstDPTNM.containsKey(strDPTCD))
					   strDPTNM = hstDPTNM.get(strDPTCD).toString();
					else
						strDPTNM = "";
					
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					if(!strGRNNO.equals(L_strGRNNO))
					{
						dosREPORT.writeBytes(padSTRING('R',strGRNNO,15));
						dosREPORT.writeBytes(padSTRING('R',strDMRNO,15));
						dosREPORT.writeBytes(padSTRING('L',strRECQT,16));
						dosREPORT.writeBytes(padSTRING('L',strPORNO,12));
						dosREPORT.writeBytes(padSTRING('R',"",8));
						dosREPORT.writeBytes(padSTRING('R',strVENCD,35));
						dosREPORT.writeBytes(padSTRING('L',strPENDY,14));
						dosREPORT.writeBytes(padSTRING('R',"",5));
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",120));
					dosREPORT.writeBytes(padSTRING('R',strDPTCD,30));
						
					dosREPORT.writeBytes("\n");
					if(!strGRNNO.equals(L_strGRNNO))
					{
						dosREPORT.writeBytes(padSTRING('R',strGRNDT,15));
						dosREPORT.writeBytes(padSTRING('R',strMATDS,51));
						dosREPORT.writeBytes(padSTRING('R',strVENNM,54));
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",120));
					dosREPORT.writeBytes(padSTRING('R',strDPTNM,31));
					L_strGRNNO = strGRNNO;
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst += 3;						
				}
			}					
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN);		
			setMSG("Report generation Completed..",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");				
			}												
			dosREPORT.close();
			fosREPORT.close();			
			setCursor(cl_dat.M_curDFSTS_pbst);	
		}
		catch(Exception L_RP)
		{
			setMSG(L_RP,"getDATA");
		}
	}
	/**
	 * Method to generate the header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO ++;
			strSTRTP = M_strSBSCD.substring(2,4);
			dosREPORT.writeBytes("\n" + cl_dat.M_strCMPNM_pbst+"\n");			
			if(cmbGRNTP.getSelectedIndex() == 1)
				dosREPORT.writeBytes(padSTRING('R',"PENDING INSPECTION REPORT (For Cash P.O.) AS ON "+cl_dat.M_strLOGDT_pbst,strDOTLN.length()-21));
			else
				dosREPORT.writeBytes(padSTRING('R',"PENDING INSPECTION REPORT AS ON "+cl_dat.M_strLOGDT_pbst,strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");						
			dosREPORT.writeBytes(strDOTLN +"\n");			
			if(cmbGRNTP.getSelectedIndex() == 0)
				dosREPORT.writeBytes("GRIN No.       Gate In No.    Receipt Quantity   P. O. No.        Vender Code                                  Days     Department Code");
			else
				dosREPORT.writeBytes("GRIN No.       Gate In No.    Receipt Quantity   Indent No        Vender Code                                  Days     Department Code");			
			dosREPORT.writeBytes("\n"+"GRIN Date      Description                                        Vender Name                               Pending     Department Name" +"\n");		
			dosREPORT.writeBytes(strDOTLN +"\n\n");			
			cl_dat.M_intLINNO_pbst = 9;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	/**
	 * Method to validate the Inputs before execuation of the SQL Queries.
	 */
	boolean vldDATA()
	{
		if((txtDPTCD.getText().trim().length() != 0) &&(txtDPTCD.getText().trim().length() != 3))
		{
			txtDPTCD.requestFocus();
			setMSG("Invalid Department code, Press 'F1' to select from List..",'E');
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
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length()==0)
						return true;
				if(input == txtDPTCD)
				{
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
						+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' and "
						+"isnull(cmt_stsfl,' ') <>'X' and CMT_CODCD='"+txtDPTCD.getText()+"'";
					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
					
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							txtDPTNM.setText(L_rstRSSET.getString("CMT_CODDS"));
							L_rstRSSET.close();
							return true;
						}	
						else
						{
							setMSG("Invalid Department Code ..",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Department Code ..",'E');
						return false;
					}
						
				}
				return true;
			}
			catch (Exception L_EX)
			{
				setMSG(L_EX,"InputVerifier");
				return false;
			}
		}
	}
}