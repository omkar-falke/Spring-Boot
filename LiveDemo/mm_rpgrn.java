/*
System Name   : Material Management System
Program Name  : GRIN Note
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 15/10/2005
Version       : MMS v2.0.0
*/
import java.util.Date;import java.sql.ResultSet;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : GRIN Note

Purpose : This program generates Report for GRIN Note for given GRIN Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMGRN    GR_GRNNO       MM_GRMST      VARCHAR(8)    GRIN number
txtTOGRN    GR_GRNNO       MM_GRMST      VARCHAR(8)    GRIN number
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   Data is taken from MM_GRMST and MM_STMST for condiations :-       
     1) GR_STRTP = ST_STRTP 
     2) AND GR_MATCD = ST_MATCD 
     3) AND ifnull(ST_STSFL,' ') <>'X' 
     4) AND ifnull(GR_STSFL,' ') <>'X' 
     5) AND GR_STRTP = selected store Type
     6) AND GR_GRNNO between given range of GRIN
		
<B>Validations & Other Information:</B>    
    - GRIN Number specified must be valid.
</I> */
class mm_rpgrn extends cl_rbase 
{									/** JTextField to display to enter GRIN Number.*/
	private JTextField txtFMGRN;	/** JTextField to display to enter GRIN Number.*/
	private JTextField txtTOGRN;	/** String variable for generated Report File Name.*/
	private String strFILNM;		/** Integer variable for serial number.*/
	private int intSRLNO;			/** Integer variable to count the number of records fetched to block the Report if no data found.*/
	private int intRECCT;			/** String variable for GRIN number.*/
	private String strGRNNO;		/** String variable for GRIN Date.*/
	private String strGRNDT;		/** String variable for Purchase Order Number..*/
	private String strPORNO;		/** String variable for Gate In Number.*/
	private String strGINNO;		/** String variable for Remark Description.*/
	private String strREMDS;		/** String variable for Consignment Number..*/
	private String strCNSNO;		/** String variable for Bill of Entry Number.*/
	private String strBOENO;		/** String variable for Challan Number.*/
	private String strCHLNO;		/** String variable for Challan Date.*/
	private String strCHLDT;		/** String variable for Challan Quantity.*/
	private String strCHLQT;		/** String variable for Vendor Code.*/
	private String strVENCD;		/** String variable for Vendor Name.*/
	private String strVENNM;		/** String variable for Lorray Number.*/
	private String strLRYNO;		/** String variable for Transporter Code.*/
	private String strTRNCD;		/** String variable for Transporter Name.*/
	private String strTRNNM;		/** String variable for Accepoted Quantity.*/
	private String strACPQT;		/** String variable for Rejected Quantity..*/
	private String strRECQT;		/** String variable for GRIN Date.*/
	private String strREJQT;		/** String variable for Execise Category.*/
	private String strEXCCT="";		/** String variable for Material Description.*/
	private String strMATDS;		/** String variable for Unit of Measurement Code.*/
	private String strUOMCD;		/** String variable for Location Code.*/
	private String strLOCCD;		/** String variable for previous GRIN Number.*/	
	private String strPGRNO="";		/** String variables for ISO Document Number.*/
	private String strISO1,strISO2,strISO3;	/** FileOutputStream Object to generate the Report file from data stream */
	private FileOutputStream fosREPORT ;	/** DataoutputStream Object to hold the report data in stream to generate the Report.*/
    private DataOutputStream dosREPORT ;	/** String variable to print dotted line in the Report.*/
    private String strDOTLN = "------------------------------------------------------------------------------------------------";
	mm_rpgrn()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);								
			add(new JLabel("FROM GRIN"),4,4,1,1,this,'L');
			add(txtFMGRN = new TxtNumLimit(8.0),4,5,1,1,this,'L');
			add(new JLabel("TO GRIN"),5,4,1,1,this,'L');
			add(txtTOGRN = new TxtNumLimit(8.0),5,5,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	mm_rpgrn(String P_strSBSCD)
	{
		M_strSBSCD = P_strSBSCD;
		M_rdbTEXT.setSelected(true);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC ==cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				txtFMGRN.setEnabled(true);
				txtTOGRN.setEnabled(true);
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				txtFMGRN.setEnabled(true);
				txtTOGRN.setEnabled(true);
				txtFMGRN.requestFocus();
			}
			if(M_cmbDESTN.getSelectedIndex() >=1)
			{
				txtFMGRN.requestFocus();
			}
		}
		else if(M_objSOURC ==txtFMGRN)
		{
			txtTOGRN.setText(txtFMGRN.getText().trim());
			txtTOGRN.requestFocus();
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC == txtFMGRN)
			{
				M_strHLPFLD = "txtFMGRN";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT,GR_GRNTP FROM MM_GRMST "
					+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtFMGRN.getText().trim().length() >0)
					M_strSQLQRY +=" AND GR_GRNNO like '"+txtFMGRN.getText().trim() +"%'";
				M_strSQLQRY += "ORDER BY GR_GRNNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN No.","GRIN DATE","GRIN Type"},3,"CT");
			}
			if(M_objSOURC == txtTOGRN)
			{
				M_strHLPFLD = "txtTOGRN";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT,GR_GRNTP FROM MM_GRMST "
					+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtTOGRN.getText().trim().length() >0)
					M_strSQLQRY +=" AND GR_GRNNO like '"+txtTOGRN.getText().trim() +"%'";
				M_strSQLQRY +=" AND GR_GRNNO > '"+txtFMGRN.getText().trim()+"' ORDER BY GR_GRNNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN No.","GRIN DATE","GRIN Type"},3,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMGRN)
			{
				if(txtFMGRN.getText().toString().length() != 0)
					txtTOGRN.requestFocus();
			}
			if(M_objSOURC == txtTOGRN)
			{
				if(txtFMGRN.getText().toString().length() != 0 && txtTOGRN.getText().toString().length() != 0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	/**
	 *Supper class method overrided to execuate the F1 Help for Selected Field.
	 */
	public void exeHLPOK()
	{
		if(M_strHLPFLD.equals("txtFMGRN"))
		{
			super.exeHLPOK();
			txtFMGRN.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOGRN.setText(txtFMGRN.getText().trim());
			txtTOGRN.select(0,txtTOGRN.getText().trim().length());
			txtTOGRN.requestFocus();
		}
		if(M_strHLPFLD.equals("txtTOGRN"))
		{
			super.exeHLPOK();
			txtTOGRN.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	/**
	 * Method to fetch data from the database & club it with header & footer.
	 */
	void getDATA(String P_strFMGRN,String P_strTOGRN)
	{
		try
		{	
			String strMATCD,strBATNO,strPMATCD ="";
			strPGRNO="";
			intRECCT =0;			
			java.sql.Date tmpDATE;	
			java.sql.Date tmpCHLDT;	
			ResultSet L_rstRSSET;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generating Report..Wait For Some Time...",'N');
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpgrn.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpgrn.doc";			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
	        if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
			{
         	    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>GRIN Note</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}		
			
			M_strSQLQRY = "SELECT GR_GRNNO,GR_GRNDT,GR_MATCD,GR_PORNO,GR_GINNO,GR_BATNO,"
			+"GR_CNSNO,GR_BOENO,GR_CHLNO,GR_CHLDT,GR_CHLQT,GR_VENCD,GR_VENNM,GR_LRYNO,"
			+"GR_TRNCD,GR_TRNNM,GR_ACPQT,GR_RECQT,GR_REJQT,GR_EXCCT,ST_MATDS,ST_UOMCD,ST_LOCCD "
			+"FROM MM_GRMST,MM_STMST "
			+"WHERE GR_CMPCD= ST_CMPCD and GR_STRTP = ST_STRTP AND GR_MATCD = ST_MATCD AND "
			+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,' ') <>'X' and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,' ') <>'X' AND "		 
			+ "GR_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND "
			+"GR_GRNNO BETWEEN '"+P_strFMGRN+"' "
			+"AND '"+P_strTOGRN +"' ORDER BY GR_GRNNO,GR_MATCD";
		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
					tmpDATE = M_rstRSSET.getDate("GR_GRNDT");
					if(tmpDATE != null)
						strGRNDT = M_fmtLCDAT.format(tmpDATE);
					else
						strGRNDT = "";
					strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
					strPORNO = nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),"");
					strBATNO = nvlSTRVL(M_rstRSSET.getString("GR_BATNO"),"");
					strGINNO = nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),"");
					strCNSNO = nvlSTRVL(M_rstRSSET.getString("GR_CNSNO"),"");
					strBOENO = nvlSTRVL(M_rstRSSET.getString("GR_BOENO"),"");
					strCHLNO = nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"");
					tmpCHLDT=M_rstRSSET.getDate("GR_CHLDT");
					if(tmpCHLDT != null)
						strCHLDT = M_fmtLCDAT.format(tmpCHLDT);
					else
						strCHLDT = "";
					strCHLQT = nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0.0");
					strVENCD = nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"");
					strVENNM = nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"");
					strLRYNO = nvlSTRVL(M_rstRSSET.getString("GR_LRYNO"),"");
					strTRNCD = nvlSTRVL(M_rstRSSET.getString("GR_TRNCD"),"");
					strTRNNM = nvlSTRVL(M_rstRSSET.getString("GR_TRNNM"),"");
					strACPQT = nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.0");
					strRECQT = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0.0");
					strREJQT = nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0.0");
					strMATDS = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
					strUOMCD = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
					strLOCCD = nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"");
					strEXCCT = nvlSTRVL(M_rstRSSET.getString("GR_EXCCT"),"");					
					if(cl_dat.M_intLINNO_pbst > 60)
					{						
						dosREPORT.writeBytes(strDOTLN);
						dosREPORT.writeBytes("\n");
						if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER1();
					}
					if(!strGRNNO.equals(strPGRNO))
					{
						strREMDS ="";
						strPMATCD ="";
						cl_dat.M_PAGENO = 0;
						intSRLNO = 1;
						M_strSQLQRY ="SELECT * FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
						M_strSQLQRY +=" AND RM_DOCTP ='GR' AND RM_REMTP ='ACP' AND RM_DOCNO ='"+strGRNNO.trim() +"'";
						
						L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
						if(L_rstRSSET !=null)
						{
							if(L_rstRSSET.next())
								strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),""); 
							L_rstRSSET.close();
						}
						if(strPGRNO.length() >0)
						{	
							prnFOOTER();
							if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");						
						}
						strPGRNO = strGRNNO;
						prnHEADER();
					}					
					
					if(!strMATCD.equals(strPMATCD))
					{
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						if(cl_dat.M_intLINNO_pbst > 60)
						{
							dosREPORT.writeBytes("\n"+strDOTLN);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER1();
						}
						dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
						dosREPORT.writeBytes(padSTRING('R',strMATCD,11));
						dosREPORT.writeBytes(padSTRING('R',strUOMCD,5));
						dosREPORT.writeBytes(padSTRING('R',strMATDS,70));
						dosREPORT.writeBytes("\n");
						strPMATCD =	strMATCD ;
						cl_dat.M_intLINNO_pbst += 1;
						if(cl_dat.M_intLINNO_pbst > 60)
						{
							dosREPORT.writeBytes("\n"+strDOTLN);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER1();
						}
						intSRLNO++;					

					}
					dosREPORT.writeBytes(padSTRING('R',"",20));
//					dosREPORT.writeBytes(padSTRING('R',"",3));
					dosREPORT.writeBytes(padSTRING('R',strBATNO,15));
					dosREPORT.writeBytes(padSTRING('R',strLOCCD,6));
					dosREPORT.writeBytes(padSTRING('L',strCHLQT,12));
					dosREPORT.writeBytes(padSTRING('L',strRECQT,15));
					if((Float.parseFloat(strACPQT)<=0)&&(Float.parseFloat(strREJQT)<=0))
						dosREPORT.writeBytes(padSTRING('L',strRECQT,15));
					else
						dosREPORT.writeBytes(padSTRING('L',strACPQT,15));
					dosREPORT.writeBytes(padSTRING('L',strREJQT,12));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER1();
					}
					
					intRECCT++;
			
				}								
				M_rstRSSET.close();				
				if(intRECCT >0)
				{
					prnFOOTER();
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strEJT);
					}
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				}
			}
			setMSG("Report generation Completed..",'N');
		/*	if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
		*/
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
	 * Method to generate the header of the Report.
	 */
	void prnHEADER()
	{
		try
		{			
			cl_dat.M_PAGENO++;
			cl_dat.M_intLINNO_pbst = 0;
			if(cl_dat.M_PAGENO == 1)
			{						
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
					+ " AND CMT_CGSTP ='MMXXRPT' AND isnull(CMT_STSFL,'') <> 'X'";
				ResultSet L_rstRSSET1 = cl_dat.exeSQLQRY3(M_strSQLQRY);        
				if(L_rstRSSET1 != null)
				{
					String L_strTEMP="";
					while(L_rstRSSET1.next())
					{
						L_strTEMP = nvlSTRVL(L_rstRSSET1.getString("CMT_CODCD"),"");
						if(L_strTEMP.equals("MM_RPGRIN1"))
							strISO1 = nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPGRIN2"))
							strISO2 = nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPGRIN3"))
							strISO3 = nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),"");					
							
					}
					L_rstRSSET1.close();
				}	
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',"DOCUMENT REF : "+strISO1,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISO2,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISO3,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length())+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strBOLD);
				prnFMTCHR(dosREPORT,M_strENH);
			}
			if(M_rdbHTML.isSelected())
			{				
				dosREPORT.writeBytes("<H3>");
			}
			dosREPORT.writeBytes("\n \n");
			cl_dat.M_intLINNO_pbst += 7;
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<CENTER>");
			else
				dosREPORT.writeBytes(padSTRING('L'," ",13));	
			dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst);
			dosREPORT.writeBytes("\n \n");
			if(M_rdbTEXT.isSelected())
			{	
				dosREPORT.writeBytes(padSTRING('L'," ",10));
			}
			dosREPORT.writeBytes("Goods Receipt Cum Inspection Note ");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</CENTER>");
			
			dosREPORT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOENH);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</H1>");
			dosREPORT.writeBytes("\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+M_strSBSCD.substring(2,4)+" "+cl_dat.getPRMCOD("cMT_CODDS","MST","COXXSST",M_strSBSCD.substring(2,4)),40));
			dosREPORT.writeBytes(padSTRING('L'," ",23));
			dosREPORT.writeBytes("GRIN No.           : "+M_strSBSCD.substring(2,4)+"/"+strGRNNO);			//NEW
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Supplier    : "+strVENCD+" "+strVENNM,63));
			dosREPORT.writeBytes("GRIN Date          : "+strGRNDT);			//NEW
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Challan No. : "+strCHLNO,40));
			dosREPORT.writeBytes(padSTRING('L'," ",23));
			dosREPORT.writeBytes("Accepted Date      : ");					//NEW
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Challan Dt. : "+strCHLDT,40));
			dosREPORT.writeBytes(padSTRING('L'," ",23));
			dosREPORT.writeBytes("Gate In No.        : "+strGINNO);			//NEW
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 8;
			dosREPORT.writeBytes (padSTRING('R',"Category    : "+cl_dat.getPRMCOD("CMT_CODDS","SYS","MMXXMAT",strEXCCT),40));		
			dosREPORT.writeBytes (padSTRING('L'," ",23));
			dosREPORT.writeBytes("Purchase Order No. : "+M_strSBSCD.substring(2,4)+"/"+strPORNO);		//NEW
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes (padSTRING('R',"Transporter : "+strTRNNM,40));
			dosREPORT.writeBytes (padSTRING('L'," ",23));
			dosREPORT.writeBytes ("Consignment No.    : "+strCNSNO);	//NEW
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes (padSTRING('R',"Vehicle No. : "+strLRYNO,40));
			dosREPORT.writeBytes(padSTRING('L',"",23));					//NEW
			dosREPORT.writeBytes ("Bill Of Entry No.  : "+strBOENO);	//NEW
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"",63));
			dosREPORT.writeBytes("Page Number        : "+cl_dat.M_PAGENO);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 4;
			dosREPORT.writeBytes(strDOTLN);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("Sr. Item Code  UOM  Item Description");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("No.                 Batch No.      LOC    Challan Qty   Receipt Qty.  Accepted Qty.   Rej. Qty.");

			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN);
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			cl_dat.M_intLINNO_pbst += 1;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
		
	}
	/**
	 * Method to generate the footer part of the report.
	 */
	void prnFOOTER()
	{
		try
		{
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Remarks : ");
			if(strREMDS.length() >97)
			{
				dosREPORT.writeBytes(strREMDS.substring(0,95)+"-");
				dosREPORT.writeBytes ("\n");			
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(strREMDS.substring(95));
			}
			else
				dosREPORT.writeBytes(strREMDS);			
			
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Prepared By",24)+padSTRING('R',"Checked By",24)+padSTRING('R',"Inspected By",24)+padSTRING('R',"Approved By",24)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"",24)+"\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('R',"DT : ",24)+padSTRING('R',"DT : ",24)+padSTRING('R',"DT : ",24)+padSTRING('R',"DT : ",24));			
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst =10;
			cl_dat.M_intLINNO_pbst=0;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTER");
		}
	}
	/**
	 * Method to validate the Inputs before execuation of SQL Query.
	 */
	public boolean vldDATA()
	{
        cl_dat.M_PAGENO = 0;
		cl_dat.M_intLINNO_pbst = 0;
		intSRLNO = 1;
		intRECCT = 1;
		strPGRNO ="";

		if(txtFMGRN.getText().trim().length() < 8)
		{
			setMSG("Enter Valid GRIN Number Or Press 'F1' For Help..",'E');
			txtFMGRN.requestFocus();
			return false;
		}
		if(txtTOGRN.getText().trim().length() < 8)
		{
			setMSG("Enter Valid GRIN Number Or Press 'F1' For Help..",'E');
			txtTOGRN.requestFocus();
			return false;
		}
		if(Integer.parseInt(txtFMGRN.getText().trim()) > Integer.parseInt(txtTOGRN.getText().trim()))
		{
			txtTOGRN.requestFocus();
			setMSG("TO GRIN Number Should Be Grater Or Equal To From GRIN Number..",'E');
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
		return true;
	}
	/**
	 * Method to generate report & forward it to specified Destination.
	 */
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
				return;
			getDATA(txtFMGRN.getText(),txtTOGRN.getText());
			if(dosREPORT != null)
				dosREPORT.close();
			if(fosREPORT != null)
				fosREPORT.close();
			/*if(intRECCT == 0)
			{
				setMSG("No data found..",'E');
				return;
			}*/
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"GRIN Note"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	/**
	 * Method to generate the table header of the report.
	 */
	void prnHEADER1()
	{
		try
		{
			cl_dat.M_PAGENO++;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"GRIN No.     : "+M_strSBSCD.substring(2,4)+"/"+strGRNNO,40));			//NEW
			dosREPORT.writeBytes(padSTRING('L',"Page Number        : "+cl_dat.M_PAGENO,56));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Sr.   Item Code  UOM   LOC    Challan Qty    Receipt Qty.   Accepted Qty.   Rejected Qty");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("No.   Item Desc.");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN);
			dosREPORT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			cl_dat.M_intLINNO_pbst = 6;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"prnHEADER1");
		}
	}
}