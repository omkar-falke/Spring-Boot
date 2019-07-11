/*
System Name   : Material Management System
Program Name  : Material Tag Pentring
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 18/10/2005
Version       : MMS v2.0.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import javax.swing.JComboBox;import java.io.FileOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.sql.ResultSet;import java.sql.Date;import java.io.DataOutputStream;
import javax.swing.JTextField;import javax.swing.JCheckBox;import javax.swing.JLabel;
import java.util.Hashtable;
/**<pre>
<B>System Name :</B> Material Management System.
 
<B>Program Name :</B> Material Tag Printing

<B>Purpose :</B> This Program is used for Material Tags / Bin card printing for a given range of
GRIN / MRN /SAN number or material code. 
various criteria.

List of tables used :
Table Name    Primary key                            Operation done
                                          Insert   Update   Query   Delete	
-----------------------------------------------------------------------------
MM_GRMST      GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO    #
MM_SAMST      SA_MMSBS,SA_STRTP,SA_SANNO,SA_MATCD             #
MM_STMST      ST_MMSBS,ST_STRTP,ST_MATCD                      #
MM_MRMST      MR_MMSBS,MR_STRTP,MR_MRNNO,MR_MATCD             #
-----------------------------------------------------------------------------

List of  fields entered/displayed on screen :
Field Name  Column Name        Table name   Type/Size      Description
-----------------------------------------------------------------------------
cmbTAGOP    to specify the criteria for Material Tag pirinting as SAN,GRIN,
          MRN & Material Code
txtFMCOD    GR_GRNNO
txtTOCOD    GR_GRNNO
chbBINCD
-----------------------------------------------------------------------------
<I>
<B>Conditions Give in Query:</B>     
   Report data is taken from MM_STMST AND (MM_GRMST or MM_MRMST or MM_SAMST)     
			
<b>Validations :</b>
    - Material codes entered for range selection must be valid.
*/

class mm_rpmtg extends cl_rbase
{									/** JComboBox to display & to select Report Option.*/
	private JComboBox cmbTAGOP;		/** JTextField to display & to enter Material Code.*/
	private JTextField txtFMCOD;	/** JTextField to display & to enter Material Code.*/
	private JTextField txtTOCOD;	/** JCheckBox to Specify Bin Card.*/
	private JCheckBox chbBINCD;		/** String variable for Store Type Code.*/
	private String strSTRTP;		/** String variable for Store Type Description.*/
	private String strSTRNM;		/** String variable for Material Code.*/	
	private String strMATCD1;		/** String variable for Location Code.*/
	private String strLOCCD1;		/** String variable for Material description.*/
	private String strMATDS1;		/** String variable for Quantity.*/
	private String strRECQT1;		/** String variable for Unit of measurement.*/
	private String strUOMCD1;		/** String variable for GRIN Number..*/
	private String strGRNNO1;		/** String variable for GRIN Date.*/
	private String strGRNDT1;		/** String variable for Ventor Name.*/
	private String strVENNM1;		/** String variable for Minimum Stock Lavel.*/
	private String strMINLV1;		/** String variable for Maximum Stock Lavel.*/
	private String strMAXLV1;		/** String variable for Rol.*/
	private String strRORLV1;		/** String variable for Rol Quantity.*/
	private String strRORQT1;		/** String variable for Material Code.*/
	private String strMATCD2;		/** String variable for Location code*/
	private String strLOCCD2;		/** String variable for Material Description.*/
	private String strMATDS2;		/** String variable for Quantity.*/
	private String strRECQT2;		/** String variable for Unit of Measurement.*/
	private String strUOMCD2;		/** String variable for GRIN Number.*/
	private String strGRNNO2;		/** String variable for GRIN Date.*/
	private String strGRNDT2;		/** String variable for Vendor Name.*/
	private String strVENNM2;		/** String variable for Minimum Stock Lavel.*/
	private String strMINLV2;		/** String variable for Maximum Stock Lavel.*/
	private String strMAXLV2;		/** String variable for Rol.*/
	private String strRORLV2;		/** String variable for Rol Quantity.*/
	private String strRORQT2;		/** String variable for Temporary Material Description.*/
	private String strTMPMATDS1="";	/** String variable for Temporary Material Description.*/
	private String strTMPMATDS2="";	/** Charector variable for Print Flag.*/
	private char chrPRNFL;			/** Integer varaible to ount the number of records fetched to block the Report if no data Found.*/
	private int intRECCT;			/** String variable for generated Report File Name.*/
	private String strFILNM;		/** Hashtable to hold the Store Type Code & associated description.*/
	private Hashtable<String,String> hstSTRTP;		/** FileOutputStream to generate the report file from stream of data.*/
	private FileOutputStream fosREPORT;/** DataOutputStream to hold report Data in theStream of data.*/
    private DataOutputStream dosREPORT;
	
	mm_rpmtg()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);													
			add(new JLabel("Option"),3,4,1,1,this,'L');
			add(cmbTAGOP = new JComboBox(),3,5,1,1.2,this,'L');
			add(new JLabel("From"),4,4,1,1.2,this,'L');
			add(txtFMCOD = new TxtNumLimit(10.0),4,5,1,1,this,'L');
			add(new JLabel("To"),5,4,1,1.2,this,'L');
			add(txtTOCOD = new TxtNumLimit(10.0),5,5,1,1,this,'L');
			add(new JLabel("Report Option"),6,4,1,1,this,'L');
			add(chbBINCD = new JCheckBox("Bin Card"),6,5,1,1,this,'L');
			cmbTAGOP.addItem("GRIN Wise");
			cmbTAGOP.addItem("MRN Wise");
			cmbTAGOP.addItem("SAN Wise");
			cmbTAGOP.addItem("Material Wise");
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		
			hstSTRTP = new Hashtable<String,String>();
			M_strSQLQRY="SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP='SYS' "
				+"AND CMT_CGSTP='MMXXSST'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			String strTEMP ="";
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!strTEMP.equals(""))					
						hstSTRTP.put(strTEMP,nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		setMSG("",'N');
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				setENBL(true);
				txtFMCOD.requestFocus();
				setMSG("Please Enter Material Code To generate the Report..",'N');
			}
			else
				setENBL(false);
		}		
		if(M_objSOURC == cmbTAGOP)
		{
			txtFMCOD.setText("");
			txtTOCOD.setText("");
			chbBINCD.setSelected(false);
			txtFMCOD.requestFocus();
			if(cmbTAGOP.getSelectedIndex() == 0)
				setMSG("Enter GRIN Number Or Press F1 For Help..",'N');
			else if(cmbTAGOP.getSelectedIndex() == 1)
				setMSG("Enter MRN Number Or Press F1 For Help..",'N');
			else if(cmbTAGOP.getSelectedIndex() == 2)
				setMSG("Enter SAN Number Or Press F1 For Help..",'N');
			else
				setMSG("Enter Material Code Or Press F1 For Help..",'N');
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_intLINNO_pbst = 0;
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbTAGOP)
			{
				txtFMCOD.requestFocus();
				if(cmbTAGOP.getSelectedIndex() == 0)
					setMSG("Enter GRIN Number Or Press F1 For Help..",'N');
				else if(cmbTAGOP.getSelectedIndex() == 1)
					setMSG("Enter MRN Number Or Press F1 For Help..",'N');
				else if(cmbTAGOP.getSelectedIndex() == 2)
					setMSG("Enter SAN Number Or Press F1 For Help..",'N');
				else
					setMSG("Enter Material Code Or Press F1 For Help..",'N');
			}
			if(M_objSOURC == txtFMCOD)
			{
				if(txtFMCOD.getText().trim().length() > 0)
				{
					txtTOCOD.setText(txtFMCOD.getText());
					txtTOCOD.requestFocus();
					if(cmbTAGOP.getSelectedIndex() == 0)
						setMSG("Enter GRIN Number Or Press F1 For Help..",'N');
					else if(cmbTAGOP.getSelectedIndex() == 1)
						setMSG("Enter MRN Number Or Press F1 For Help..",'N');
					else if(cmbTAGOP.getSelectedIndex() == 2)
						setMSG("Enter SAN Number Or Press F1 For Help..",'N');
					else
						setMSG("Enter Material Code Or Press F1 For Help..",'N');
				}
				else
				{
					txtFMCOD.requestFocus();
					if(cmbTAGOP.getSelectedIndex() == 0)
						setMSG("Enter GRIN Number Or Press F1 For Help..",'N');
					else if(cmbTAGOP.getSelectedIndex() == 1)
						setMSG("Enter MRN Number Or Press F1 For Help..",'N');
					else if(cmbTAGOP.getSelectedIndex() == 2)
						setMSG("Enter SAN Number Or Press F1 For Help..",'N');
					else
						setMSG("Enter Material Code Or Press F1 For Help..",'N');	}
			}
			if(M_objSOURC == txtTOCOD)
			{
				if(txtTOCOD.getText().trim().length() > 0)
				{
					if(Long.parseLong(txtTOCOD.getText().trim()) < Long.parseLong(txtFMCOD.getText().trim())){
						txtTOCOD.requestFocus();
						setMSG("To Code Should Be Greater Than Or Equal To From Code..",'E');
					}
					else
					{
						chbBINCD.requestFocus();
						setMSG("Click To Print Bin Card..",'N');
					}
				}
				else
				{
					txtTOCOD.requestFocus();
					if(cmbTAGOP.getSelectedIndex() == 0)
						setMSG("Enter GRIN Number Or Press F1 For Help..",'N');
					else if(cmbTAGOP.getSelectedIndex() == 1)
						setMSG("Enter MRN Number Or Press F1 For Help..",'N');
					else if(cmbTAGOP.getSelectedIndex() == 2)
						setMSG("Enter SAN Number Or Press F1 For Help..",'N');
					else
						setMSG("Enter Material Code Or Press F1 For Help..",'N');
				}
			}
			if(M_objSOURC == chbBINCD)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			strSTRTP = M_strSBSCD.substring(2,4);
			if(M_objSOURC == txtFMCOD)
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strHLPFLD = "txtFMCOD";
				if(cmbTAGOP.getSelectedIndex() == 0)//If GRIN Wise Option Selected
				{									
					if(txtFMCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST WHERE "
							+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND "
							+"GR_GRNNO LIKE '"+txtFMCOD.getText().trim()+"%' AND "
							+"isnull(GR_STSFL,'') <> 'X' ORDER BY GR_GRNNO ASC";
					else
						M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST WHERE "
							+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND isnull(GR_STSFL,'') <> 'X' "
							+"ORDER BY GR_GRNNO ASC";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Number","GRIN Date"},2,"CT");
				}
				else if(cmbTAGOP.getSelectedIndex() == 1)	//If MRN Wise Option Selected
				{
					if(txtFMCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT MR_MRNNO,MR_MRNDT FROM MM_MRMST WHERE "
						+"MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP = '"+strSTRTP+"' AND "
						+"MR_MRNNO LIKE '"+txtFMCOD.getText().trim()+"%' AND "
						+"isnull(MR_STSFL,'') <> 'X' ORDER BY MR_MRNNO DESC";
					else
						M_strSQLQRY = "SELECT DISTINCT MR_MRNNO,MR_MRNDT FROM MM_MRMST WHERE "
						+"MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP = '"+strSTRTP+"' AND isnull(MR_STSFL,'') <> 'X' "
						+"ORDER BY MR_MRNNO DESC";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"MRN Number","MRN Date"},2,"CT");
				}
				else if(cmbTAGOP.getSelectedIndex() == 2)	//If SAN Wise Option Selected
				{
					if(txtFMCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT SA_SANNO,SA_SANDT FROM MM_SAMST WHERE "
						+"SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_STRTP = '"+strSTRTP+"' AND "
						+"SA_SANNO LIKE '"+txtFMCOD.getText().trim()+"%' AND "
						+"isnull(SA_STSFL,'') <> 'X' ORDER BY SA_SANNO DESC";
					else
						M_strSQLQRY = "SELECT DISTINCT SA_SANNO,SA_SANDT FROM MM_SAMST WHERE "
						+"SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_STRTP = '"+strSTRTP+"' AND isnull(SA_STSFL,'') <> 'X' "
						+"ORDER BY SA_SANNO DESC";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"SAN Number","SAN Date"},2,"CT");
				}
				else				//If Material Wise Option Selected
				{
					if(txtFMCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD "
						+"FROM MM_STMST WHERE ST_STRTP = '"+strSTRTP+"' AND "
						+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD LIKE '"+txtFMCOD.getText().trim()+"%' AND "
						+"isnull(ST_STSFL,'')<> 'X' ORDER BY ST_MATCD ASC";
					else
						M_strSQLQRY = "SELECT DISTINCT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD "
						+"FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND "
						+"isnull(ST_STSFL,'')<> 'X' ORDER BY ST_MATCD ASC";
					
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Material Code","Description"},2,"CT");
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(M_objSOURC == txtTOCOD)
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strHLPFLD = "txtTOCOD";
				if(cmbTAGOP.getSelectedIndex() == 0)	//If GRIN Wise Option Selected
				{
					if(txtTOCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST WHERE "
							+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND "
							+"GR_GRNNO LIKE '"+txtTOCOD.getText().trim()+"%' AND "
							+"isnull(GR_STSFL,'') <> 'X' ORDER BY GR_GRNNO ASC";
					else		
						M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST WHERE "
							+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND isnull(GR_STSFL,'') <> 'X' "
							+"ORDER BY GR_GRNNO ASC";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Number","GRIN Date"},2,"CT");
				}
				else if(cmbTAGOP.getSelectedIndex() == 1)	//If MRN Wise Option Selected
				{
					if(txtTOCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT MR_MRNNO,MR_MRNDT FROM MM_MRMST WHERE "
							+"MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP = '"+strSTRTP+"' AND "
							+"MR_MRNNO LIKE '"+txtTOCOD.getText().trim()+"%' AND "
							+"isnull(MR_STSFL,'') <> 'X' ORDER BY MR_MRNNO DESC";
					else
						M_strSQLQRY = "SELECT DISTINCT MR_MRNNO,MR_MRNDT FROM MM_MRMST WHERE "
							+"MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP = '"+strSTRTP+"' AND isnull(MR_STSFL,'') <> 'X' "
							+"ORDER BY MR_MRNNO DESC";
					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"MRN Number","MRN Date"},2,"CT");
				}
				else if(cmbTAGOP.getSelectedIndex() == 2)	//If SAN Wise Option Selected
				{
					if(txtTOCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT SA_SANNO,SA_SANDT FROM MM_SAMST WHERE "
							+"SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_STRTP = '"+strSTRTP+"' AND "
							+"SA_SANNO LIKE '"+txtTOCOD.getText().trim()+"%' AND "
							+"isnull(SA_STSFL,'') <> 'X' ORDER BY SA_SANNO DESC";
					else
						M_strSQLQRY = "SELECT DISTINCT SA_SANNO,SA_SANDT FROM MM_SAMST WHERE "
							+"SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_STRTP = '"+strSTRTP+"' AND isnull(SA_STSFL,'') <> 'X' "
							+"ORDER BY SA_SANNO DESC";
					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"SAN Number","SAN Date"},2,"CT");
				}
				else
				{		//If Material Wise Option Selected
					if(txtTOCOD.getText().trim().length() > 0)
						M_strSQLQRY = "SELECT DISTINCT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD "
							+"FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND "
							+"ST_MATCD LIKE '"+txtTOCOD.getText().trim()+"%' AND "
							+"isnull(ST_STSFL,'')<> 'X' ORDER BY ST_MATCD ASC";
					else
						M_strSQLQRY = "SELECT DISTINCT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD "
							+"FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND "
							+"isnull(ST_STSFL,'')<> 'X' ORDER BY ST_MATCD ASC";
					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
	}
	/**
	 * Super class method overrided to execuate the F1 help for selected Field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMCOD")
		{
			txtFMCOD.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMCOD.requestFocus();
		}
		if(M_strHLPFLD == "txtTOCOD")
		{
			txtTOCOD.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOCOD.requestFocus();
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
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpmtg.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpmtg.doc";
			if(cmbTAGOP.getSelectedIndex() == 3)
			{
				if(chbBINCD.isSelected())				
					getRPBIN();				
				else
					getRPMAT();
			}
			else
			{
				if(chbBINCD.isSelected())				
					getRPBIN();				
				else
					getRPGRN();
			}
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Material Tag / Bin Card"," ");
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
	 * Method to validate the Inputs before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		if(Float.parseFloat(txtTOCOD.getText().trim()) < Float.parseFloat(txtFMCOD.getText().trim()))
		{
			txtTOCOD.requestFocus();
			setMSG("To Code Should Be Greater Than Or Equal TO From Code..",'E');
			return false;
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if(M_cmbDESTN.getItemCount() ==0)
			{
				setMSG("Please select E-mail Id by using the F1 list ..",'E');
				return false;
			}
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
	/*
	* Method for Material Tag Printing Form GRIN To GRIN Number
	*/
	public void getRPGRN()
	{
		try
		{
			ResultSet L_rstRSSET;
			java.sql.Date jdtTEMP;
			strSTRTP = M_strSBSCD.substring(2,4);
			setCursor(cl_dat.M_curWTSTS_pbst);
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Material Tag Printing </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			if(hstSTRTP.containsKey(strSTRTP))
				strSTRNM = hstSTRTP.get(strSTRTP).toString();
			
			if(cmbTAGOP.getSelectedIndex() == 0)
			{		 
				M_strSQLQRY = "SELECT GR_MATCD,GR_RECQT,GR_GRNNO,GR_GRNDT,GR_VENNM,"
				+"ST_MATDS,ST_UOMCD,ST_LOCCD "
				+"FROM MM_GRMST,MM_STMST WHERE ST_CMPCD = GR_CMPCD and ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND "
				+"GR_GRNNO BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
				+"'"+txtTOCOD.getText().trim()+"' AND "
				+"isnull(GR_STSFL,'') <> 'X' AND isnull(ST_STSFL,'') <> 'X' "
				+"ORDER BY GR_GRNNO,GR_MATCD ";
			}
			else if(cmbTAGOP.getSelectedIndex() == 1)
			{		 
				// Modified on 18/02/2005 as before MRN auth. all codes printing was not
				// appearing, left outer join introduced
				M_strSQLQRY = "SELECT MR_MATCD GR_MATCD,MR_RETQT GR_RECQT,MR_MRNNO GR_GRNNO,MR_MRNDT GR_GRNDT,MR_DPTCD GR_VENNM,"
				+"ST_MATDS,ST_UOMCD,ST_LOCCD "
				+"FROM MM_MRMST LEFT OUTER JOIN MM_STMST ON ST_CMPCD=MR_CMPCD AND ST_STRTP = MR_STRTP AND ST_MATCD = MR_MATCD AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' WHERE "
				+"MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP = '"+strSTRTP+"' AND "
				+"MR_MRNNO BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
				+"'"+txtTOCOD.getText().trim()+"' AND "
				+"isnull(MR_STSFL,'') <> 'X'  "
				+"ORDER BY MR_MRNNO,MR_MATCD ";
			}
			else if(cmbTAGOP.getSelectedIndex() == 2)
			{		 
				M_strSQLQRY = "SELECT SA_MATCD GR_MATCD,SA_SANQT GR_RECQT,SA_SANNO GR_GRNNO,SA_SANDT GR_GRNDT,'' GR_VENNM,"
				+"ST_MATDS,ST_UOMCD,ST_LOCCD "
				+"FROM MM_SAMST,MM_STMST WHERE ST_CMPCD=SA_CMPCD and ST_STRTP = SA_STRTP AND ST_MATCD = SA_MATCD AND "
				+"SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_STRTP = '"+strSTRTP+"' AND "
				+"SA_SANNO BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
				+"'"+txtTOCOD.getText().trim()+"' AND "
				+"isnull(SA_STSFL,'') <> 'X' AND isnull(ST_STSFL,'') <> 'X' "
				+"ORDER BY SA_SANNO,SA_MATCD ";
			}
						
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
			intRECCT=0;
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strMATCD1 = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
					strLOCCD1 = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
					strMATDS1 = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
					strRECQT1 = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"");
					strUOMCD1 = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
					strGRNNO1 = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
						
					jdtTEMP = M_rstRSSET.getDate("GR_GRNDT");
					if(jdtTEMP != null)
						strGRNDT1 = M_fmtLCDAT.format(jdtTEMP);
					else
						strGRNDT1 = "";     
						
					strVENNM1 = nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"");
						
					if(strMATDS1.length() ==0)
					{
						// For MRN , to get desc of reconditined codes before auth
						if(cmbTAGOP.getSelectedIndex() == 1)
						{
							M_strSQLQRY = "Select MR_ORGCD,ST_MATDS,ST_UOMCD,ST_LOCCD "
    							+" FROM MM_MRMST,MM_STMST WHERE MR_CMPCD=ST_CMPCD and MR_ORGCD = ST_MATCD "
								+" AND MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STSFL <>'X' AND ST_STSFL <>'X' AND MR_STRTP ='"
								+ M_strSBSCD.substring(2,4) +"' AND MR_MRNNO ='"
								+ strGRNNO1 +"' AND MR_MATCD ='"
								+ strMATCD1+"'";
							L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
							if(L_rstRSSET !=null)
							{
								if(L_rstRSSET.next())
								{
									strLOCCD1 = nvlSTRVL(L_rstRSSET.getString("ST_LOCCD"),"");
									strMATDS1 = nvlSTRVL(L_rstRSSET.getString("ST_MATDS"),"");
									strUOMCD1 = nvlSTRVL(L_rstRSSET.getString("ST_UOMCD"),"");
															
								}
								L_rstRSSET.close();
							}
						}
					}
					if(M_rstRSSET.next())
					{
						chrPRNFL = 'Y';
						strMATCD2 = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
						strLOCCD2 = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
						strMATDS2 = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
						strRECQT2 = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"");
						strUOMCD2 = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
						strGRNNO2 = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
						
						jdtTEMP = M_rstRSSET.getDate("GR_GRNDT");
						if(jdtTEMP != null)
							strGRNDT2 = M_fmtLCDAT.format(jdtTEMP);
						else
							strGRNDT2 = "";     
						
						strVENNM2 = nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"");
						if(strMATDS2.length() ==0)
						{
							// For MRN
							if(cmbTAGOP.getSelectedIndex() == 1)
							{
								M_strSQLQRY = "Select MR_ORGCD,ST_MATDS,ST_UOMCD,ST_LOCCD "
    								+" FROM MM_MRMST,MM_STMST WHERE MR_CMPCD=ST_CMPCD and MR_ORGCD = ST_MATCD "
									+" AND MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STSFL <>'X' AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STSFL <>'X' AND MR_STRTP ='"
									+ M_strSBSCD.substring(2,4) +"' AND MR_MRNNO ='"
									+ strGRNNO2 +"' AND MR_MATCD ='"
									+ strMATCD2+"'";
								L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
								if(L_rstRSSET !=null)
								{
									if(L_rstRSSET.next())
									{
										strLOCCD2 = nvlSTRVL(L_rstRSSET.getString("ST_LOCCD"),"");
										strMATDS2 = nvlSTRVL(L_rstRSSET.getString("ST_MATDS"),"");
										strUOMCD2 = nvlSTRVL(L_rstRSSET.getString("ST_UOMCD"),"");
																	
									}
									L_rstRSSET.close();
								}
							}
						}
					}
					else
					{
						chrPRNFL = 'N';
						strMATCD2 = "";
						strLOCCD2 = "";
						strMATDS2 = "";
						strRECQT2 = "";
						strUOMCD2 = "";
						strGRNNO2 = "";
						strGRNDT2 = "";
						strVENNM2 = "";
					}
					dosREPORT.writeBytes("\n");	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");	
					dosREPORT.writeBytes(padSTRING('R',"SPL MATERIAL TAG",17));
					dosREPORT.writeBytes(padSTRING('L',strSTRNM,26));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"SPL MATERIAL TAG",17));
						dosREPORT.writeBytes(padSTRING('L',strSTRNM,26));
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");
					dosREPORT.writeBytes("\n\n");
					dosREPORT.writeBytes(padSTRING('R',"Mat. Code : "+strMATCD1,24));
					dosREPORT.writeBytes(padSTRING('L',"Location : "+strLOCCD1,19));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"Mat. Code : "+strMATCD2,24));
						dosREPORT.writeBytes(padSTRING('L',"Location : "+strLOCCD2,19));
					}
					dosREPORT.writeBytes("\n");
					/////
					if(strMATDS1.length() > 42)
					{
						dosREPORT.writeBytes(padSTRING('R',strMATDS1.substring(0,42),43));
						strTMPMATDS1 = strMATDS1.substring(42,strMATDS1.length());
					}
					else
						dosREPORT.writeBytes(padSTRING('R',strMATDS1,43));
						
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						if(strMATDS2.length() > 42)
						{
							dosREPORT.writeBytes(padSTRING('R',strMATDS2.substring(0,42),43));
							strTMPMATDS2 = strMATDS2.substring(42,strMATDS2.length());
						}
						else						
							dosREPORT.writeBytes(padSTRING('R',strMATDS2,43));						
					}
					dosREPORT.writeBytes("\n");
					if(strTMPMATDS1.length() > 0)
						dosREPORT.writeBytes(padSTRING('R',strTMPMATDS1,43));
					else
						dosREPORT.writeBytes(padSTRING('R',"",43));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(strTMPMATDS2.length() > 0)
						dosREPORT.writeBytes(padSTRING('R',strTMPMATDS2,43));
					else
						dosREPORT.writeBytes(padSTRING('R',"",43));
					dosREPORT.writeBytes("\n");
					/////
					dosREPORT.writeBytes(padSTRING('R',"Quantity  : "+strRECQT1,24));
					dosREPORT.writeBytes(padSTRING('L',"UOM : "+strUOMCD1,19));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"Quantity  : "+strRECQT2,24));
						dosREPORT.writeBytes(padSTRING('L',"UOM : "+strUOMCD2,19));
					}
					dosREPORT.writeBytes("\n");
						
					if(cmbTAGOP.getSelectedIndex() == 0)
						dosREPORT.writeBytes(padSTRING('R',"GRIN No.  : "+strGRNNO1,24));
					else if(cmbTAGOP.getSelectedIndex() == 1)
						dosREPORT.writeBytes(padSTRING('R',"MRN No.  : "+strGRNNO1,24));
					else if(cmbTAGOP.getSelectedIndex() == 2)
						dosREPORT.writeBytes(padSTRING('R',"SAN No.  : "+strGRNNO1,24));
						
					dosREPORT.writeBytes(padSTRING('L',"Date : "+strGRNDT1,19));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						if(cmbTAGOP.getSelectedIndex() == 0)
							dosREPORT.writeBytes(padSTRING('R',"GRIN No.  : "+strGRNNO2,24));
						else if(cmbTAGOP.getSelectedIndex() == 1)
							dosREPORT.writeBytes(padSTRING('R',"MRN No.  : "+strGRNNO2,24));
						else if(cmbTAGOP.getSelectedIndex() == 2)
							dosREPORT.writeBytes(padSTRING('R',"SAN No.  : "+strGRNNO2,24));
						dosREPORT.writeBytes(padSTRING('L',"Date : "+strGRNDT2,19));
					}
					dosREPORT.writeBytes("\n");
					if(cmbTAGOP.getSelectedIndex() == 0)
						dosREPORT.writeBytes(padSTRING('R',"Sup : "+strVENNM1,43));
					else
						dosREPORT.writeBytes(padSTRING('R',"",43));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						if(cmbTAGOP.getSelectedIndex() == 0)
							dosREPORT.writeBytes(padSTRING('R',"Sup : "+strVENNM2,43));
						else
							dosREPORT.writeBytes(padSTRING('R',"",43));
					}
					dosREPORT.writeBytes("\n\n\n\n");						
					cl_dat.M_intLINNO_pbst += 12;
					strTMPMATDS1 = "";
					strTMPMATDS2 = "";
					intRECCT++;
				}
			}			
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);												
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    				
			}			
			dosREPORT.close();
			fosREPORT.close();	
			setMSG("Report generated Successfully..",'N');
			chrPRNFL = 'X';			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPTFL");
		}
	}
	/*
	* Method for Printlng Material Tag for given range of Material Code.
	*/
	public void getRPMAT()
	{
		try
		{
			java.sql.Date jdtTEMP;
			strSTRTP = M_strSBSCD.substring(2,4);
			setCursor(cl_dat.M_curWTSTS_pbst);
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Material Tag Printing </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}						
			intRECCT=0;
			
			if(hstSTRTP.containsKey(strSTRTP))
				strSTRNM = hstSTRTP.get(strSTRTP).toString();			
			
			M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD "
				+"FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND "
				+"ST_MATCD BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
				+"'"+txtTOCOD.getText().trim()+"' AND isnull(ST_STSFL,'') <> 'X' "
				+"ORDER BY ST_MATCD ";						
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strMATCD1 = nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),"");
					strLOCCD1 = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
					strMATDS1 = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
					strUOMCD1 = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
						
					if(M_rstRSSET.next())
					{
						chrPRNFL = 'Y';
						strMATCD2 = nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),"");
						strLOCCD2 = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
						strMATDS2 = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
						strUOMCD2 = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
							
					}
					else
					{
						chrPRNFL = 'N';
						strMATCD2 = "";
						strLOCCD2 = "";
						strMATDS2 = "";
						strUOMCD2 = "";
					}
					dosREPORT.writeBytes("\n");	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");
					dosREPORT.writeBytes(padSTRING('R',"SPL MATERIAL TAG",17));
					dosREPORT.writeBytes(padSTRING('L',strSTRNM,26));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"SPL MATERIAL TAG",17));
						dosREPORT.writeBytes(padSTRING('L',strSTRNM,26));
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");
					dosREPORT.writeBytes("\n\n");
					dosREPORT.writeBytes(padSTRING('R',"Mat. Code : "+strMATCD1,24));
					dosREPORT.writeBytes(padSTRING('L',"Location : "+strLOCCD1,19));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"Mat. Code : "+strMATCD2,24));
						dosREPORT.writeBytes(padSTRING('L',"Location : "+strLOCCD2,19));
					}
					dosREPORT.writeBytes("\n");
					/*
					dosREPORT.writeBytes(padSTRING('R',strMATDS1,43));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
						dosREPORT.writeBytes(padSTRING('R',strMATDS2,43));
					dosREPORT.writeBytes("\n\n");
					*/
					//////
					if(strMATDS1.length() > 42)
					{
						dosREPORT.writeBytes(padSTRING('R',strMATDS1.substring(0,42),43));
						strTMPMATDS1 = strMATDS1.substring(42,strMATDS1.length());
					}
					else
						dosREPORT.writeBytes(padSTRING('R',strMATDS1,43));
						
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						if(strMATDS2.length() > 42)
						{
							dosREPORT.writeBytes(padSTRING('R',strMATDS2.substring(0,42),43));
							strTMPMATDS2 = strMATDS2.substring(42,strMATDS2.length());
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('R',strMATDS2,43));
						}
					}
					dosREPORT.writeBytes("\n");
					if(strTMPMATDS1.length() > 0)
						dosREPORT.writeBytes(padSTRING('R',strTMPMATDS1,43));
					else
						dosREPORT.writeBytes(padSTRING('R',"",43));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(strTMPMATDS2.length() > 0)
						dosREPORT.writeBytes(padSTRING('R',strTMPMATDS2,43));
					else
						dosREPORT.writeBytes(padSTRING('R',"",43));
					dosREPORT.writeBytes("\n");
					//////
					dosREPORT.writeBytes(padSTRING('R',"Quantity  : ",24));
					dosREPORT.writeBytes(padSTRING('L',"UOM : "+strUOMCD1,19));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"Quantity  : ",24));
						dosREPORT.writeBytes(padSTRING('L',"UOM : "+strUOMCD2,19));
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"GRIN/SAN/MRN : ",24));
					dosREPORT.writeBytes(padSTRING('R',"Date : ",19));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"GRIN/SAN/MRN : ",24));
						dosREPORT.writeBytes(padSTRING('R',"Date : ",19));
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("\n\n\n\n");
						
					cl_dat.M_intLINNO_pbst += 12;
						
					strTMPMATDS1 = "";
					strTMPMATDS2 = "";
					intRECCT++;
				}
			}			
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);												
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			}			
			dosREPORT.close();
			fosREPORT.close();	
			chrPRNFL = 'X';			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPTFL");
		}
	}
	/*
	* Method For Bin Tag println For From GRIN - To GRIN And From Material Code - To Material Code
	*/
	public void getRPBIN()
	{
		try
		{
			java.sql.Date jdtTEMP;
			strSTRTP = M_strSBSCD.substring(2,4);
			setCursor(cl_dat.M_curWTSTS_pbst);
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Material Tag Printing </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			
			if(hstSTRTP.containsKey(strSTRTP))
				strSTRNM = hstSTRTP.get(strSTRTP).toString();
			
			if(cmbTAGOP.getSelectedIndex() == 0)	//Query For From - To GRIN Number
				M_strSQLQRY = "SELECT GR_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,"
					+"ST_RORLV,ST_RORQT FROM MM_GRMST,MM_STMST WHERE ST_CMPCD=GR_CMPCD and ST_STRTP =GR_STRTP AND "
					+"ST_MATCD = GR_MATCD AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND "
					+"GR_GRNNO BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
					+"'"+txtTOCOD.getText().trim()+"' AND "
					+"isnull(GR_STSFL,'') <> 'X' AND isnull(ST_STSFL,'') <> 'X' "
					+"ORDER BY GR_GRNNO,GR_MATCD ";
			else if(cmbTAGOP.getSelectedIndex() == 1)	//Query For From - To MRN Number
				M_strSQLQRY = "SELECT MR_MATCD GR_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,"
					+"ST_RORLV,ST_RORQT FROM MM_MRMST,MM_STMST WHERE ST_CMPCD=MR_CMPCD and ST_STRTP =MR_STRTP AND "
					+"ST_MATCD = MR_MATCD AND MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP = '"+strSTRTP+"' AND "
					+"MR_MRNNO BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
					+"'"+txtTOCOD.getText().trim()+"' AND "
					+"isnull(MR_STSFL,'') <> 'X' AND isnull(ST_STSFL,'') <> 'X' "
					+"ORDER BY MR_MRNNO,MR_MATCD ";
			else if(cmbTAGOP.getSelectedIndex() == 2)	//Query For From - To SAN Number
				M_strSQLQRY = "SELECT SA_MATCD GR_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,"
					+"ST_RORLV,ST_RORQT FROM MM_SAMST,MM_STMST WHERE ST_CMPCD=SA_CMPCD and ST_STRTP =SA_STRTP AND "
					+"ST_MATCD = SA_MATCD AND SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_STRTP = '"+strSTRTP+"' AND "
					+"SA_SANNO BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
					+"'"+txtTOCOD.getText().trim()+"' AND "
					+"isnull(SA_STSFL,'') <> 'X' AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' "
					+"ORDER BY SA_SANNO,SA_MATCD ";
			else	//Query For From - To Material Code
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_MINLV,ST_MAXLV,"
					+"ST_RORLV,ST_RORQT FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND "
					+"ST_MATCD BETWEEN '"+txtFMCOD.getText().trim()+"' AND "
					+"'"+txtTOCOD.getText().trim()+"' AND isnull(ST_STSFL,'') <> 'X' "
					+"ORDER BY ST_MATCD ";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				intRECCT =0;
				while(M_rstRSSET.next())
				{
					if(cmbTAGOP.getSelectedIndex() != 3)
						strMATCD1 = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
					else
						strMATCD1 = nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),"");
					strLOCCD1 = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
					strMATDS1 = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
					strUOMCD1 = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
					strMINLV1 = nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"");
					strMAXLV1 = nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),"");
					strRORLV1 = nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),"");
					strRORQT1 = nvlSTRVL(M_rstRSSET.getString("ST_RORQT"),"");
					if(M_rstRSSET.next()){
						chrPRNFL = 'Y';
						if(cmbTAGOP.getSelectedIndex() == 3)
							strMATCD2 = nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),"");
						else
							strMATCD2 = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
						strLOCCD2 = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
						strMATDS2 = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
						strUOMCD2 = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
						strMINLV2 = nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"");
						strMAXLV2 = nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),"");
						strRORLV2 = nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),"");
						strRORQT2 = nvlSTRVL(M_rstRSSET.getString("ST_RORQT"),"");
					}
					else
					{
						chrPRNFL = 'N';
						strMATCD2 = "";
						strLOCCD2 = "";
						strMATDS2 = "";
						strUOMCD2 = "";
						strMINLV2 = "";
						strMAXLV2 = "";
						strRORLV2 = "";
						strRORQT2 = "";
					}
					dosREPORT.writeBytes("\n");	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");
					dosREPORT.writeBytes(padSTRING('R',"SPL BIN CARD",17));
					dosREPORT.writeBytes(padSTRING('L',strSTRNM,26));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y'){
						dosREPORT.writeBytes(padSTRING('R',"SPL BIN CARD",17));
						dosREPORT.writeBytes(padSTRING('L',strSTRNM,26));
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");
					dosREPORT.writeBytes("\n\n");
					dosREPORT.writeBytes(padSTRING('R',"Mat. Code : "+strMATCD1,24));
					dosREPORT.writeBytes(padSTRING('L',"Location : "+strLOCCD1,19));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"Mat. Code : "+strMATCD2,24));
						dosREPORT.writeBytes(padSTRING('L',"Location : "+strLOCCD2,19));
					}
					dosREPORT.writeBytes("\n");
					/*
					dosREPORT.writeBytes(padSTRING('R',strMATDS1,43));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
						dosREPORT.writeBytes(padSTRING('R',strMATDS2,43));
					dosREPORT.writeBytes("\n\n");
					*/
					////////
					if(strMATDS1.length() > 42)
					{
						dosREPORT.writeBytes(padSTRING('R',strMATDS1.substring(0,42),43));
						strTMPMATDS1 = strMATDS1.substring(42,strMATDS1.length());
					}
					else
						dosREPORT.writeBytes(padSTRING('R',strMATDS1,43));
						
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						if(strMATDS2.length() > 42)
						{
							dosREPORT.writeBytes(padSTRING('R',strMATDS2.substring(0,42),43));
							strTMPMATDS2 = strMATDS2.substring(42,strMATDS2.length());
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('R',strMATDS2,43));
						}
					}
					dosREPORT.writeBytes("\n");
					if(strTMPMATDS1.length() > 0)
						dosREPORT.writeBytes(padSTRING('R',strTMPMATDS1,43));
					else
						dosREPORT.writeBytes(padSTRING('R',"",43));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(strTMPMATDS2.length() > 0)
						dosREPORT.writeBytes(padSTRING('R',strTMPMATDS2,43));
					else
						dosREPORT.writeBytes(padSTRING('R',"",43));
					dosREPORT.writeBytes("\n");
					///////
					dosREPORT.writeBytes(padSTRING('R',"Min Stk : "+strMINLV1,18));
					dosREPORT.writeBytes(padSTRING('R',"ROL : "+strRORLV1,16));
					dosREPORT.writeBytes(padSTRING('L',"UOM : "+strUOMCD1,9));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"Min Stk : "+strMINLV2,18));
						dosREPORT.writeBytes(padSTRING('R',"ROL : "+strRORLV2,16));
						dosREPORT.writeBytes(padSTRING('L',"UOM : "+strUOMCD2,9));
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"Max Stk : "+strMAXLV1,18));
					dosREPORT.writeBytes(padSTRING('R',"ROQ : "+strRORQT1,16));
					dosREPORT.writeBytes(padSTRING('L',"",9));
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(chrPRNFL == 'Y')
					{
						dosREPORT.writeBytes(padSTRING('R',"Max Stk : "+strMAXLV2,18));
						dosREPORT.writeBytes(padSTRING('R',"ROQ : "+strRORQT2,16));
						dosREPORT.writeBytes(padSTRING('L',"",9));
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("\n\n\n\n");
						
					cl_dat.M_intLINNO_pbst += 12;
						
					strTMPMATDS1 = "";
					strTMPMATDS2 = "";
					intRECCT++;
				}
			}
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);												
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			}			
			dosREPORT.close();
			fosREPORT.close();	
			setMSG("Report generated Successfully..",'N');
			chrPRNFL = 'X';			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPTFL");
		}
	}
}