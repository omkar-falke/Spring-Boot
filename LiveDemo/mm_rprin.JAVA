/*
System Name   : Material Management System
Program Name  : Rejection Intimation Note
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
 
Program Name : Rejection Intimation Note.

Purpose : Program to generate for Rejection Intimation Note for given Range of GRIN.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD            #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMGRN    GR_GRNNO                MM_GRMST           VARCHAR(10)   GRIN Number.
txtTOGRN    GR_GRNNO                MM_GRMST           VARCHAR(10)   GRIN Number.
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
For Purches order Data is taken from MM_GRMST,CO_PTMST and CO_CTMST for condiations :- 
    1) PT_PRTCD = GR_VENCD 
    2) AND PT_PRTTP = 'S' 
    3) AND CT_MATCD = GR_MATCD 
    4) AND GR_STRTP = specified Store Type
    5) AND GR_REJQT >0 
    6) AND GR_GRNNO in the range of given GRIN Number.
      
<B>Validations & Other Information:</B>    
    - If department code is specified then it should be valid.
</I> */

class mm_rprin extends cl_rbase 
{									/** JTextField to display & to enter the GRIN Number.*/
	private JTextField txtFMGRN;	/** JTextField to display & to enter the GRIN Number.*/
	private JTextField txtTOGRN;	/** JRedioButton to specify Reminder Note.*/
	private JRadioButton rdbREJRE;	/** JRedioButton to specify Intimation Notice.*/
	private JRadioButton rdbREJIN;	/** Button Group to bind JRadioButtons together.*/
	private ButtonGroup btgREJCT;	/** String variable for GRIN Number.*/
	private String strGRNNO;		/** String variable for GRIN Date.*/
	private String strGRNDT;		/** String variable for Vendor Code.*/
	private String strVENCD;		/** String variable for Purchase order Number.*/
	private String strPORNO;		/** String variable for Purchase Order Date.*/
	private String strPORDT="";		/** String variable for Store Type.*/
	private String strSTRTP;		/** String variable for Challan Number.*/
	private String strCHLNO;		/** String variable for Challan Date.*/
	private String strCHLDT="";		/** String variable for Material code.*/
	private String strMATCD;		/** String variable for Requried Quantity.*/
	private String strRECQT;		/** String variable for rejected Quantity.*/
	private String strREJQT;		/** String variable for Party Name.*/
	private String strPRTNM;		/** String variable for Party Address.*/
	private String strADR01;		/** String variable for Party Address.*/
	private String strADR02;		/** String variable for Party Address.*/
	private String strADR03;		/** String variable for Party Address.*/
	private String strADR04;		/** String variable for Party Pin Number..*/
	private String strPINCD;		/** String variable for City Name.*/
	private String strCTYNM;		/** String variable for Material Description.*/
	private String strMATDS;		/** String variable for Unit of Measurement Code.*/
	private String strUOMCD;		/** String variable for Store type Description.*/
	private String strSTRNM;		/** String variable for ISO Document Number.*/
	private String strISOS1;		/** String variable for ISO Document Number.*/
	private String strISOS2;		/** String variable for ISO Document Number.*/
	private String strISOS3;		/** String variable for Previous GRIN Number.*/
	private String strTGRNO = "";	/** Integer variable for Serial Number.*/
	private int M_intSRLNO;			/** String variable for generated report file name.*/
	private String strFILNM;		/** Integer variable t count the number of records fetched to block the report if no data found.*/
	private int intRECCT;			/** FileOutputStream Object to generate report file from the stream of data.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream Object to hold the report data in the stream of data to generate the Report.*/
    private DataOutputStream dosREPORT;/** Hashtable to hold the Store Type code with description.*/
	private Hashtable<String,String> hstSTRTP;			/** String variable to Print Dotted Line in the Report.*/
	private String strDOTLN = "------------------------------------------------------------------------------------------------";
	String M_strADDRS="",M_strCONNO="";
	mm_rprin()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);																	
			add(new JLabel("From GRIN"),4,4,1,1,this,'L');
			add(txtFMGRN = new TxtLimit(8),4,5,1,1,this,'L');
			add(new JLabel("To GRIN"),5,4,1,1,this,'L');
			add(txtTOGRN = new TxtLimit(8),5,5,1,1,this,'L');
			add(new JLabel("Report Type"),6,4,1,1,this,'L');
			add(rdbREJIN = new JRadioButton("Intimation",true),6,5,1,1,this,'L');
			add(rdbREJRE = new JRadioButton("Reminder"),6,6,1,1,this,'L');
			btgREJCT = new ButtonGroup();
			btgREJCT.add(rdbREJIN);
			btgREJCT.add(rdbREJRE);
			
			txtFMGRN.setInputVerifier(new INPVF());
			txtTOGRN.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			
			hstSTRTP = new  Hashtable<String,String>();
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN "
				+"WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSST'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			String strTEMP = "";
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!strTEMP.equals(""))
						hstSTRTP.put(strTEMP,nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
			
			/**M_strADDRS String Varible to store Company address**/
			M_strSQLQRY =" Select CP_ADR01,CP_ADR02,CP_ADR03,CP_ADR04,CP_PINCD,CP_CTYNM,CP_TEL01,CP_TEL02,CP_FAXNO from CO_CPMST ";
			M_strSQLQRY+=" where CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>>M_strSQLQRY>>>>"+M_strSQLQRY);	
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				 M_strADDRS=M_rstRSSET.getString("CP_ADR01")+" "+M_rstRSSET.getString("CP_ADR02")+" "+M_rstRSSET.getString("CP_ADR03")+" "+M_rstRSSET.getString("CP_ADR04")+" "+M_rstRSSET.getString("CP_PINCD");
				 M_strCONNO="Telephones : "+M_rstRSSET.getString("CP_TEL01")+" Fax : "+M_rstRSSET.getString("CP_FAXNO");
			}
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_TRYE)
		{
			setMSG(L_TRYE,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				txtFMGRN .requestFocus();
				setMSG("Please Enter the GRIN Number..",'N');
				setENBL(true);				
			}
			else
				setENBL(false);	
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			strTGRNO="";
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		strSTRTP = M_strSBSCD.substring(2,4);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtFMGRN)
			{
				M_strHLPFLD = "txtFMGRN";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST "
					+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_REJQT,0) > 0 AND GR_STRTP = '"+strSTRTP+"' AND ";
				if(txtFMGRN.getText().trim().length() > 0)
					M_strSQLQRY += "GR_GRNNO LIKE '"+txtFMGRN.getText().trim()+"%' AND ";
				M_strSQLQRY += "isnull(GR_STSFL,'') <> 'X' ORDER BY GR_GRNNO ASC";
				
			  	cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Number","GRIN Date"},2,"CT");
			}
			if(M_objSOURC == txtTOGRN)
			{
				M_strHLPFLD = "txtTOGRN";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST "
					+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_REJQT,0) > 0 AND GR_STRTP = '"+strSTRTP+"' AND ";
				if(txtTOGRN.getText().trim().length() > 0)
					M_strSQLQRY += "GR_GRNNO LIKE '"+txtTOGRN.getText().trim()+"%' AND ";
				M_strSQLQRY += "isnull(GR_STSFL,'') <> 'X' ORDER BY GR_GRNNO ASC";
				
			  	cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Number","GRIN Date"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMGRN)
			{
				if(txtFMGRN.getText().trim().length() == 8)
				{
					txtTOGRN.setText(txtFMGRN.getText());
					txtTOGRN.requestFocus();
				}
				else
				{
					setMSG("Enter Valid GRIN Number ",'E');
					txtFMGRN.requestFocus();
				}
			}
			if(M_objSOURC == txtTOGRN)
			{
				if(txtTOGRN.getText().trim().length() == 8)
				{
					rdbREJIN.requestFocus();
				}
				else
				{
					setMSG("Enter Valid GRIN Number ",'E');
					txtTOGRN.requestFocus();
				}
			}
			if(M_objSOURC == rdbREJIN)
			{
				rdbREJRE.requestFocus();
			}
			if(M_objSOURC == rdbREJRE)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}			
		}
	}
	/**
	 * Super class method overrided to execuate the F1 help for selected field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMGRN")
		{
			txtFMGRN.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMGRN.requestFocus();
		}
		if(M_strHLPFLD == "txtTOGRN")
		{
			txtTOGRN.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOGRN.requestFocus();
		}
	}
	/**
	 * Method to generate the Report File & to forward it to specifiedaa Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rprin.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rprin.doc";			
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Rejection Intimation Note"," ");
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
	void getDATA()
	{
		try
		{
			intRECCT = 0;
			java.sql.Date datTEMP;
			ResultSet L_rstPORDT;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Rejection Intimation Note</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			strSTRTP = M_strSBSCD.substring(2,4);				

			if(hstSTRTP.containsKey(strSTRTP))
			   strSTRNM = hstSTRTP.get(strSTRTP).toString();
			M_strSQLQRY = "SELECT GR_GRNNO,GR_GRNDT,GR_PORNO,GR_CHLNO,GR_CHLDT,GR_MATCD,"
				+"GR_RECQT,GR_REJQT,GR_VENCD,"
				+"PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM,PT_PINCD,CT_MATDS,CT_UOMCD "
				+"FROM MM_GRMST,CO_PTMST,CO_CTMST "
				+"WHERE PT_PRTCD = GR_VENCD AND PT_PRTTP = 'S' AND "
				+"CT_MATCD = GR_MATCD AND "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+strSTRTP+"' AND GR_REJQT >0 AND "
				+"GR_GRNNO BETWEEN '"
				+txtFMGRN.getText().toString()+"' AND '"+txtTOGRN.getText().toString()+"' "
				+"ORDER BY GR_GRNNO ";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{					
				while(M_rstRSSET.next())
				{
					intRECCT = 1;					
					strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
					datTEMP = M_rstRSSET.getDate("GR_GRNDT");
					if(datTEMP != null)
						strGRNDT = M_fmtLCDAT.format(datTEMP);
					strPORNO = nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),"");
					strCHLNO = nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"");
					datTEMP = M_rstRSSET.getDate("GR_CHLDT");
					if(datTEMP != null)
						strCHLDT = M_fmtLCDAT.format(datTEMP);
					strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
					strRECQT = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"");
					strREJQT = nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"");
					strVENCD = nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"");
					strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					strADR01 = nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),"");
					strADR02 = nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),"");
					strADR03 = nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),"");
					strADR04 = nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),"");
					strCTYNM = nvlSTRVL(M_rstRSSET.getString("PT_CTYNM"),"");
					strPINCD = nvlSTRVL(M_rstRSSET.getString("PT_PINCD"),"");
					strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"");
					strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
					
					L_rstPORDT = cl_dat.exeSQLQRY2("Select PO_PORDT from MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_PORNO = '"+strPORNO+"'");
					if(L_rstPORDT!= null)
					{
						if(L_rstPORDT.next())
						{
							datTEMP = L_rstPORDT.getDate("PO_PORDT");
							if(datTEMP !=null)
								strPORDT = M_fmtLCDAT.format(datTEMP);
						}
						L_rstPORDT.close();
					}
					if(cl_dat.M_intLINNO_pbst > 60)
					{						
						cl_dat.M_intLINNO_pbst = 0;
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------");
						prnHEADER();
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");
					}
						
					if(!(strGRNNO).equals(strTGRNO))
					{
						if(strTGRNO != "")
						{
							prnFOOTER();
						}
						prnHEADER();						
						prnHEADER1();
						strTGRNO = strGRNNO;
						M_intSRLNO = 1;
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',String.valueOf(M_intSRLNO),5));
					dosREPORT.writeBytes(padSTRING('R',strMATCD,25));
					dosREPORT.writeBytes(padSTRING('R',strUOMCD,3));
						
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",5));
					dosREPORT.writeBytes(padSTRING('R',strMATDS,62));
					dosREPORT.writeBytes(padSTRING('L',strRECQT,13));
					dosREPORT.writeBytes(padSTRING('L',strREJQT,15));
					dosREPORT.writeBytes("\n");						
					M_intSRLNO += 1;
					cl_dat.M_intLINNO_pbst += 3;						
				}				
				M_rstRSSET.close();
				prnFOOTER();
			}
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
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	 * Method to generate the header part of the report.
	 */
	void prnHEADER()
	{
		String L_strRPORT="";
		try		
		{	
			ResultSet L_rstRSSET1;
			cl_dat.M_PAGENO++;			
			if(cl_dat.M_PAGENO == 1)
			{		
				if(rdbREJIN.isSelected())
					L_strRPORT = "MM_RPRIN";
				else
					L_strRPORT = "MM_RPRRN";
				
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
					+ " AND CMT_CGSTP ='MMXXRPT' AND isnull(CMT_STSFL,'') <> 'X'";
				L_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(L_rstRSSET1 != null)
				{
					String L_strTEMP="";
					while(L_rstRSSET1.next())
					{
						L_strTEMP = nvlSTRVL(L_rstRSSET1.getString("CMT_CODCD"),"");
						if(!L_strTEMP.equals(""))
						{
							if(L_strTEMP.equals(L_strRPORT +"01"))
								strISOS1 = nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals(L_strRPORT +"02"))
								strISOS2 = nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals(L_strRPORT +"03"))
								strISOS3 = nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),"");
						}					
					}
					L_rstRSSET1.close();
				}	
			}
						
			dosREPORT.writeBytes("\n"+padSTRING('L',"-------------------------------",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',"DOCUMENT REF  : "+strISOS1,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOS2,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOS3,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('C',M_strADDRS,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('C',M_strCONNO,strDOTLN.length())+"\n");			
			if(rdbREJRE.isSelected())
				dosREPORT.writeBytes(padSTRING('C',"REJECTION REMINDER",strDOTLN.length())+"\n");
			if(rdbREJIN.isSelected())
				dosREPORT.writeBytes(padSTRING('C',"REJECTION INTIMATION",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('C',"REGISTER A/D",strDOTLN.length())+"\n");
			dosREPORT.writeBytes("Ref : RIN NO. :"+strGRNNO+"\n");
			dosREPORT.writeBytes(padSTRING('L',"",60));
			dosREPORT.writeBytes("Date        : "+strGRNDT +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"",60));
			dosREPORT.writeBytes("Page No     : "+cl_dat.M_PAGENO+"\n");
			cl_dat.M_intLINNO_pbst = 15;				
		}
		catch(Exception L_HDE)
		{
			setMSG(L_HDE,"prnHEADER");
		}
	}		
	/**
	 * Method to generate the footer part of the Report.
	 */
	void prnFOOTER()
	{
		try
		{
			String L_strREMRK="";
			
			M_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+strSTRTP+"' AND "
				+"RM_REMTP = 'ACP' AND RM_DOCTP = 'GR' AND RM_DOCNO = '"+strGRNNO+"' AND "
				+"isnull(RM_STSFL,'') <> 'X' ";
			
			ResultSet L_rstREMRK = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstREMRK !=null)
			{
				if(L_rstREMRK.next())
				{
					L_strREMRK = nvlSTRVL(L_rstREMRK.getString("RM_REMDS"),"");
				}
				L_rstREMRK.close();
				L_rstREMRK =null;
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			
			if(L_strREMRK.length() >90)
			{
				dosREPORT.writeBytes("Remarks : "+L_strREMRK.substring(0,85));
				dosREPORT.writeBytes("\n");
				if(L_strREMRK.length() >180)	
				{
					dosREPORT.writeBytes(L_strREMRK.substring(85,180));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(L_strREMRK.substring(180));
				}
				else
					dosREPORT.writeBytes(L_strREMRK.substring(85));
			}
			else
				dosREPORT.writeBytes("Remarks : "+L_strREMRK);
			
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("You are requested to collect the above material from our factory within two weeks from the date ");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("of receipt of this letter. If you fail to collect the material within that period, we will be");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("free to dispose off the material at your risk and cost. ");
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes("Thanking You,");
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes("Yours Faithfully,");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("for Supreme Petrochem Limited ");
			dosREPORT.writeBytes("\n\n\n");
			dosREPORT.writeBytes("AUTHORISED SIGNATORY");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Material Handling Division");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("cc: Purchase/Excise");
			
			prnFMTCHR(dosREPORT,M_strEJT);
			cl_dat.M_intLINNO_pbst = 0;
		}
		catch(Exception L_RPFTR)
		{
			setMSG(L_RPFTR,"prnFOOTER");
		}
	}
	/**
	 * Method to validate the inputs before execuation of the SQl Query.
	 */
	boolean vldDATA()
	{
		if(txtFMGRN.getText().trim().length() < 8)
		{
			txtFMGRN.requestFocus();
			setMSG("Enter Valid GRIN No Or Press F1 For Help ",'E');
			return false;
		}
		if(txtTOGRN.getText().trim().length() < 8)
		{
			txtTOGRN.requestFocus();
			setMSG("Enter Valid GRIN No Or Press F1 For Help ",'E');
			return false;
		}
		if(Float.parseFloat(txtTOGRN.getText()) < Float.parseFloat(txtFMGRN.getText()))
		{
			setMSG("Value Of To GRIN Should Be Grater Than Or Equal To From GRIN ",'E');
			txtTOGRN.requestFocus();
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
				if(input == txtFMGRN || input == txtTOGRN)
				{
					if(((JTextField)input).getText().trim().length() < 8)
						return true;
					M_strSQLQRY = "SELECT COUNT(*) FROM MM_GRMST WHERE "
						+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRNNO = '"+((JTextField)input).getText()+"' AND "
						+"GR_REJQT > 0 AND GR_STRTP = '"+strSTRTP+"' AND isnull(GR_STSFL,'')<> 'X'";
					
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3(M_strSQLQRY);
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)== 0)
							{
								setMSG("Invalid GRIN Number..",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid GRIN Number..",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid GRIN Number..",'E');
						return false;
					}
				}
				return true;
			}
			catch(Exception L_SQL)
			{
				setMSG(L_SQL,"verify");
				return false;
			}
		}
	}
	/**
	 * Method to generate the Sub Header of the Report.
	 */
	void prnHEADER1()
	{
		try
		{			
			dosREPORT.writeBytes("\n"+padSTRING('R',strPRTNM,60));
			dosREPORT.writeBytes(padSTRING('R',"P.O. No     : "+strPORNO,35)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',strADR01,60));
			dosREPORT.writeBytes(padSTRING('R',"P.O. Date   : "+strPORDT,35)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',strADR02,60));
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,35)+"\n");
			cl_dat.M_intLINNO_pbst += 3;					
			dosREPORT.writeBytes(padSTRING('R',strADR03+"  "+strADR04,60)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',strPINCD,60)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',strCTYNM,60));
			cl_dat.M_intLINNO_pbst +=3;
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes(padSTRING('R',"Sir,",96)+"\n");			
			cl_dat.M_intLINNO_pbst +=3;
			if(rdbREJRE.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"We refer to the Rejection Intimation Number : "+strGRNNO,55));
				dosREPORT.writeBytes(padSTRING('R',"Date : "+strGRNDT,18));
				dosREPORT.writeBytes("by which we have "+"\n");				
				dosREPORT.writeBytes("informed you about the rejected materials. Since there has been no response from your end,"+"\n");				
				dosREPORT.writeBytes("we once again give below the details for your ready reference.");
				cl_dat.M_intLINNO_pbst += 2;
			}
			if(rdbREJIN.isSelected())
			{
				dosREPORT.writeBytes("With reference to the above purchase order, the following material supplied vide your"+"\n");				
				dosREPORT.writeBytes(padSTRING('R',"Challan No. : "+strCHLNO,25));
				dosREPORT.writeBytes(padSTRING('R',"Date : "+strCHLDT,20));
				dosREPORT.writeBytes("has been rejected by us.");
				cl_dat.M_intLINNO_pbst += 1;
			}			
			dosREPORT.writeBytes("\n-----------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("Sr.  Material Code            UOM                                  Supplied Qty.  Rejected Qty.\n");			
			dosREPORT.writeBytes("No.  Material Description      \n");			
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------");
			cl_dat.M_intLINNO_pbst += 4;
		}
		catch(Exception L_SHDR)
		{
			setMSG(L_SHDR,"genSBHDR");
		}
	}
}