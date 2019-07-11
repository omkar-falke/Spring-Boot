/*
System Name   : Marketing System
Program Name  : Payment Details Report
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 10/06/2006"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :13/06/2006
Modified det.  :
Version        :
*/
import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import javax.swing.JPanel;import java.util.Hashtable;import java.util.StringTokenizer;

/**
System Name : Marketing System.
 
Program Name : Payment Details Report

Purpose : This program generates Report for Payment Details .

List of tables used :
Table Name              Primary key                                             Operation done
                                                                   Insert   Update   Query   Delete	
---------------------------------------------------------------------------------------------------
MR_PRTRN       PR_CMPCD,PR_PRTTP,PR_PRTCD,PR_DOCTP,
               PR_DOCNO,PR_SRLNO                                                       #
CO_PTMST       PT_PRTTP,PT_PRTCD                                                       #
MR_PATRN       PA_CMPCD,PA_PRTTP,PA_PRTCD,PA_RCTTP,
               PA_CRDNO,PA_DBTTP,PA_DBTNO                                              #
MR_PLTRN       PA_CMPCD,PL_PRTTP,PL_GRPCD,PL_DOCTP,PL_DOCNO                            #
---------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT   PR_DOCDT                  MR_PRTRN           Date         Document date
txtTODAT   PR_DOCDT                  MR_PRTRN           Date         Document date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
    For Receipt Details Report Receipt details is Taken from  MR_PRTRN 
        Condition: 1) PR_DOCDT ="Given date range by user
                   2) And ifnull(PR_STSFL,'') <> 'X' 

    For Receipt Details Report Adjustment details is Taken from  MR_PATRN,MR_PLTRN
	     Condition:1) pa_cmpcd = pl_cmpcd
                   2) and pa_prttp = pl_prttp 
				   3) and PA_PRTCD = pl_grpcd 
                   4) and pa_dbttp = pl_doctp
                   5) and PA_DBTNO = pl_docno
                   6) and pa_cmpcd||pa_prttp||PA_PRTCD||PA_CRDNO  in(select distinct pr_cmpcd||pr_prttp||pr_grpcd||pr_docno"
                   7) And PR_DOCDT = date range Given by user
			       8) And ifnull(PA_STSFL,'') <> 'X' 
			       9) order by pa_prttp,pa_grpcd,PA_CRDNO ";
*/

class mr_rppmt extends cl_rbase
{										 /** JTextField to  enter From Date */
	private JTextField txtFMDAT;		 /** JtextField to  enter To Date */
	private JTextField txtTODAT;		 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 150;          // report width
	private ResultSet L_rstRSSET ;       // Variable for result Set
	private String strADJGRPCD="";       //variable for Adjusted Group Code  
	private String strNXTGRPCD;          //Veriable for next Group Code
	private String strLSTGRPCD;          //Variable for last Group Code
	private String strLSTDOCNO;          //Variable for last Document Number
	private String strNXTDOCNO;          // Variable for  Document number
		
	private String strDOCNO;
	private boolean flgRSSET=false;
	private Hashtable<String,String> hstDOCDT=new Hashtable<String,String>();    //HashTable To store Document Date 
	private double dblRCTTOT=0.00;                 // variable for Receipt Amount Total
	private double dblADJTOT=0.00;                 //Variable for Adjustment Total
	private int intPAGENO=0;                       //Variable for Page No count
	private Hashtable<String,String> hstPARTY = new Hashtable<String,String>();  //Hash Table for Party code And party Name
	private final String strDDRFT="02";
	private final String strCHEQE="01";
	private final String strPORDR="03";
	private final String strINVIC="21";
	private final String strADPAY="11";
	private final String strCREDT="13";
	private final String strAGNLC="12";
	
	
	
	mr_rppmt()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
				
			add(new JLabel("From Date "),2,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),2,4,1,1,this,'L');
			add(new JLabel("TO Date "),3,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),3,4,1,1,this,'L');
					
			M_strSQLQRY="Select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_DSRCD from CO_PTMST ";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET!=null)
			{
				String L_strPRTNM="";
				while(M_rstRSSET.next())
				{
					hstPARTY.put(nvlSTRVL(M_rstRSSET.getString("PT_PRTTP"),"  ") +""+nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"  "),nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  ")+"|"+nvlSTRVL(M_rstRSSET.getString("PT_DSRCD"),"  "));
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
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
		super.actionPerformed(L_AE);		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				setMSG("Enter From Date..",'N');
				txtFMDAT.requestFocus();	
			}
			if(M_objSOURC == txtFMDAT)
			{
				setMSG("Enter To Date..",'N');
				txtTODAT.requestFocus();
			}
			if(M_objSOURC == txtTODAT)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	public boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length()==0)
			{
				setMSG("Please Enter From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length()==0)
			{
				setMSG("Please Enter To Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("To Date can not be Less than From Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}				
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rppmt.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rppmt.doc";
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Payment Register"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	*/
	public void getDATA()
	{		
		try
		{
			cl_dat.M_intLINNO_pbst =0;
			String L_strSQLQRY="";
			intPAGENO=1;
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
					
			L_strSQLQRY ="select PR_PRTCD,PR_DOCTP,PR_MOPCD,PR_CHQNO,PR_CHQDT,PR_RCTVL,PR_BNKCD,PR_DOCNO,PR_DOCDT,PT_PRTNM,PT_PRTCD,PT_PRTTP from ";
            L_strSQLQRY +="mr_prtrn,CO_PTMST where PR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'  ";
			L_strSQLQRY +="	AND PR_BNKCD=PT_PRTCD AND PT_PRTTP='B' ";
			L_strSQLQRY +="	AND isnull(PR_STSFL,'') <> 'X' ORDER BY PR_PRTTP,PR_PRTCD,PR_DOCNO ";
			 
			L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			
			M_strSQLQRY ="select PA_PRTCD,PA_DBTTP,PA_CRDNO,PA_DBTNO,PA_ADJVL,PL_DOCVL,PL_DOCDT from mr_patrn,mr_pltrn,mr_prtrn where pa_cmpcd = pl_cmpcd ";
			M_strSQLQRY +=" and PA_PRTTP=PL_PRTTP and PA_PRTCD = PL_PRTCD and PA_DBTTP=PL_DOCTP and PA_DBTNO =PL_DOCNO";
			M_strSQLQRY +=" and pa_cmpcd = pr_cmpcd and pa_prttp=pr_prttp and PA_PRTCD=pr_prtcd and PA_CRDNO= pr_docno ";
			M_strSQLQRY +=" and  PR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND isnull(PA_STSFL,'') <> 'X'  order by pa_prttp,PA_PRTCD,PA_CRDNO ";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			   prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Product Specification Sheet</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			prnHEADER();			
			dspRCTDATA();
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			   	prnFMTCHR(dosREPORT,M_strCPI12);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setMSG("",'N'); 
	}
	
	private void dspRCTDATA()
	{
		try
		{
			//intRECCT=2;
			hstDOCDT.clear();
			String L_strCHQDT="";
			strLSTGRPCD="";
			strNXTGRPCD="";
			int L_intGRPCD=0;
			int L_intFRSCNT=0;
			StringTokenizer L_STRTKN;
			String L_strPRTNM="";
			String L_strDSRCD="";
			
			double L_dblRCTVL=0.0;
			double L_dblADJVL=0.0;
			double L_dblINVVL=0.0;
			String L_strDOCTP="";
			String L_strMOPCD="";
			String L_strDBTTP="";
			java.sql.Date jdtTEMP=null;
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				flgRSSET=true;
			}
			
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					if(cl_dat.M_intLINNO_pbst >60)
					{
						intPAGENO++;
						dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
					}
				
					strNXTGRPCD=nvlSTRVL(L_rstRSSET.getString("PR_PRTCD"),"");
					strNXTDOCNO=nvlSTRVL(L_rstRSSET.getString("PR_DOCNO"),"");
					
					if(!strLSTGRPCD.equals(strNXTGRPCD)||!strLSTDOCNO.equals(strNXTDOCNO))
					{
						if(intRECCT>0)
						{
							while(flgRSSET)
							{
								strADJGRPCD=M_rstRSSET.getString("PA_PRTCD");
								strDOCNO=nvlSTRVL(M_rstRSSET.getString("PA_CRDNO"),"");
								if(strDOCNO.equals(strLSTDOCNO))
								{
									dosREPORT.writeBytes(padSTRING('R'," ",2));
									L_strDBTTP=nvlSTRVL(M_rstRSSET.getString("pa_dbttp"),"");
									if(L_strDBTTP.equals(strINVIC))
									{
										L_strDBTTP="Inv";
									}
									dosREPORT.writeBytes(padSTRING('R',L_strDBTTP,9));
									dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PA_DBTNO"),""),14));
									jdtTEMP = M_rstRSSET.getDate("PL_DOCDT");
									if(jdtTEMP !=null)
									{		
										L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
									}
									else
									{
										L_strCHQDT="";
									}
									dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_strCHQDT,""),11));
									dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),12));
									L_dblINVVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),""));
									dosREPORT.writeBytes(padSTRING('R',"("+setNumberFormat(L_dblINVVL,2)+")",14));
									L_dblADJVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("pa_adjvl"),""));
									dblADJTOT += L_dblADJVL;
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblADJVL,2),55));
									dosREPORT.writeBytes(padSTRING('L',strDOCNO,20));
									dosREPORT.writeBytes(padSTRING('L',nvlSTRVL((String)hstDOCDT.get(strDOCNO),""),13));
									dosREPORT.writeBytes("\n");
									cl_dat.M_intLINNO_pbst+=1;
									L_intFRSCNT=1;
								}
								else
								{
									if(strADJGRPCD.equals(strLSTGRPCD))
									{
										break;
									}
									dosREPORT.writeBytes("\n");
									dosREPORT.writeBytes(crtLINE(intRPTWD-74,"- ")+"\n");
									dosREPORT.writeBytes(padSTRING('R'," ",82));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblRCTTOT,2),16));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblADJTOT,2),19));
									dosREPORT.writeBytes(padSTRING('L',"Bal :",17));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblRCTTOT-dblADJTOT,2),11));
									dosREPORT.writeBytes("\n");
									dosREPORT.writeBytes(crtLINE(intRPTWD-74,"- ")+"\n");
									dblADJTOT=0.0;
									//dblINVTOT=0.0;
									dblRCTTOT=0.0;
									L_intGRPCD=1;
									cl_dat.M_intLINNO_pbst +=4;
									break;
								}
								if(!M_rstRSSET.next())
								{
									flgRSSET=false;
									break;
								}
							}//end of Inner While Loop
						}// End Of Inner If Loop
						else
						{
					
							
							dosREPORT.writeBytes(padSTRING('R'," ",2));
							if(hstPARTY.containsKey("C"+strNXTGRPCD))
							{
								L_strPRTNM=hstPARTY.get("C"+strNXTGRPCD).toString();
								L_STRTKN=new StringTokenizer(L_strPRTNM,"|");
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_STRTKN.nextToken(),""),36));
								L_strDSRCD=L_STRTKN.nextToken();
								L_strPRTNM=hstPARTY.get("D"+L_strDSRCD).toString();
								L_STRTKN=new StringTokenizer(L_strPRTNM,"|");
								dosREPORT.writeBytes(padSTRING('R',"(Dist Ref : "+nvlSTRVL(L_STRTKN.nextToken()+")",""),45));
							}
							dosREPORT.writeBytes("\n\n");
							
							dosREPORT.writeBytes(padSTRING('R'," ",2));
							L_strDOCTP=nvlSTRVL(L_rstRSSET.getString("PR_DOCTP"),"");
							if(L_strDOCTP.equals(strCREDT))
							{
								L_strDOCTP="Crd";
							}
							else if(L_strDOCTP.equals(strAGNLC))
							{
								L_strDOCTP="LC";
							}
							else if(L_strDOCTP.equals(strADPAY))
							{
								L_strDOCTP="Adv";
							}
							dosREPORT.writeBytes(padSTRING('R',L_strDOCTP,9));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PR_CHQNO"),""),14));
							jdtTEMP = L_rstRSSET.getDate("PR_CHQDT");
					
							if(jdtTEMP !=null)
							{		
								L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
							}
							else
							{
								L_strCHQDT="";
							}
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_strCHQDT,""),11));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),2));
							L_strMOPCD=nvlSTRVL(L_rstRSSET.getString("PR_MOPCD"),"");
							if(L_strMOPCD.equals(strCHEQE))
							{
								L_strMOPCD="Chq";
							}
							else if(L_strMOPCD.equals(strDDRFT))
							{
								L_strMOPCD="DD";
							}
							if(L_strMOPCD.equals(strPORDR))
							{
								L_strMOPCD="PO";
							}
							dosREPORT.writeBytes(padSTRING('R',L_strMOPCD,10));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""),36));
							L_dblRCTVL = Double.parseDouble(nvlSTRVL(L_rstRSSET.getString("PR_RCTVL"),""));
							dblRCTTOT = L_dblRCTVL;
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCTVL,2),14));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),30));
							dosREPORT.writeBytes(padSTRING('L',strNXTDOCNO,9));
							jdtTEMP = L_rstRSSET.getDate("PR_DOCDT");
							if(jdtTEMP !=null)
							{		
								L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
							}
							else
							{
								L_strCHQDT="";
							}
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_strCHQDT,""),13));
							hstDOCDT.put(strNXTDOCNO,L_strCHQDT);
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=3;
							intRECCT++;
						}
					}// End Of Outer If Loop
					else
					{		
						
						dosREPORT.writeBytes(padSTRING('R'," ",2));
						L_strDOCTP=nvlSTRVL(L_rstRSSET.getString("PR_DOCTP"),"");
		
						if(L_strDOCTP.equals(strCREDT))
						{
							L_strDOCTP="Crd";
						}
						else if(L_strDOCTP.equals(strAGNLC))
						{
							L_strDOCTP="LC";
						}
						else if(L_strDOCTP.equals(strADPAY))
						{
							L_strDOCTP="Adv";
						}
						dosREPORT.writeBytes(padSTRING('R',L_strDOCTP,9));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PR_CHQNO"),""),14));
						jdtTEMP = L_rstRSSET.getDate("PR_CHQDT");
					
						if(jdtTEMP !=null)
						{		
							L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
						}
						else
						{
							L_strCHQDT="";
						}
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_strCHQDT,""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),2));
						L_strMOPCD=nvlSTRVL(L_rstRSSET.getString("PR_MOPCD"),"");
						if(L_strMOPCD.equals(strCHEQE))
						{
							L_strMOPCD="Chq";
						}
						else if(L_strMOPCD.equals(strDDRFT))
						{
							L_strMOPCD="DD";
						}
						if(L_strMOPCD.equals(strPORDR))
						{
							L_strMOPCD="Chq1";
						}
						dosREPORT.writeBytes(padSTRING('R',L_strMOPCD,10));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""),36));
						L_dblRCTVL = Double.parseDouble(nvlSTRVL(L_rstRSSET.getString("PR_RCTVL"),""));
						dblRCTTOT += L_dblRCTVL;
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCTVL,2),14));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),30));
						dosREPORT.writeBytes(padSTRING('L',strNXTDOCNO,9));
						jdtTEMP = L_rstRSSET.getDate("PR_DOCDT");
						if(jdtTEMP !=null)
						{		
							L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
						}
						else
						{
							L_strCHQDT="";
						}
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_strCHQDT,""),13));
						hstDOCDT.put(strNXTDOCNO,L_strCHQDT);
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst +=1;
						L_intFRSCNT=0;
						L_intGRPCD=0;
						
					}
					if(L_intFRSCNT==1)
					{
						if(L_intGRPCD==1)
						{
							dosREPORT.writeBytes(padSTRING('R'," ",2));
							if(hstPARTY.containsKey("C"+strNXTGRPCD))
							{
								L_strPRTNM=hstPARTY.get("C"+strNXTGRPCD).toString();
								L_STRTKN=new StringTokenizer(L_strPRTNM,"|");
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_STRTKN.nextToken(),""),36));
								L_strDSRCD=L_STRTKN.nextToken();
								L_strPRTNM=hstPARTY.get("D"+L_strDSRCD).toString();
								L_STRTKN=new StringTokenizer(L_strPRTNM,"|");
								dosREPORT.writeBytes(padSTRING('R',"(Dist Ref : "+nvlSTRVL(L_STRTKN.nextToken()+")",""),45));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst+=1;
							}
						}
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R'," ",2));
						L_strDOCTP=nvlSTRVL(L_rstRSSET.getString("PR_DOCTP"),"");
						if(L_strDOCTP.equals(strCREDT))
						{
							L_strDOCTP="Crd";
						}
						else if(L_strDOCTP.equals(strAGNLC))
						{
							L_strDOCTP="LC";
						}
						else if(L_strDOCTP.equals(strADPAY))
						{
							L_strDOCTP="Adv";
						}
						dosREPORT.writeBytes(padSTRING('R',L_strDOCTP,9));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PR_CHQNO"),""),14));
						jdtTEMP = L_rstRSSET.getDate("PR_CHQDT");
						if(jdtTEMP !=null)
						{		
							L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
						}
						else
						{
							L_strCHQDT="";
						}
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_strCHQDT,""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),2));
						L_strMOPCD=nvlSTRVL(L_rstRSSET.getString("PR_MOPCD"),"");
						if(L_strMOPCD.equals(strCHEQE))
						{
							L_strMOPCD="Chq";
						}
						else if(L_strMOPCD.equals(strDDRFT))
						{
							L_strMOPCD="DD";
						}
						if(L_strMOPCD.equals(strPORDR))
						{
							L_strMOPCD="PO";
						}
						dosREPORT.writeBytes(padSTRING('R',L_strMOPCD,10));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""),36));
						L_dblRCTVL = Double.parseDouble(nvlSTRVL(L_rstRSSET.getString("PR_RCTVL"),""));
						dblRCTTOT += L_dblRCTVL;
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCTVL,2),14));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),30));
						dosREPORT.writeBytes(padSTRING('L',strNXTDOCNO,9));
						jdtTEMP = L_rstRSSET.getDate("PR_DOCDT");
						if(jdtTEMP !=null)
						{		
							L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
						}
						else
						{
							L_strCHQDT="";
						}
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_strCHQDT,""),13));
						hstDOCDT.put(strNXTDOCNO,L_strCHQDT);
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst +=2;
						intRECCT++;
						L_intFRSCNT=0;
						L_intGRPCD=0;
					}
					strLSTGRPCD=strNXTGRPCD;
					strLSTDOCNO=strNXTDOCNO;
					//intRECCT++;
				}//End Of outer While Loop
			}
			while(flgRSSET)
			{
				strADJGRPCD=M_rstRSSET.getString("PA_PRTCD");
				strDOCNO=nvlSTRVL(M_rstRSSET.getString("PA_CRDNO"),"");
				if(strDOCNO.equals(strLSTDOCNO))
				{
					dosREPORT.writeBytes(padSTRING('R'," ",2));
					L_strDBTTP=nvlSTRVL(M_rstRSSET.getString("pa_dbttp"),"");
					if(L_strDBTTP.equals(strINVIC))
					{
						L_strDBTTP="Inv";
					}
					dosREPORT.writeBytes(padSTRING('R',L_strDBTTP,9));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PA_DBTNO"),""),14));
					
					jdtTEMP = M_rstRSSET.getDate("PL_DOCDT");
					if(jdtTEMP !=null)
					{		
						L_strCHQDT=M_fmtLCDAT.format(jdtTEMP);
					}
					else
					{
						L_strCHQDT="";
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_strCHQDT,""),11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(" "," "),12));
					L_dblINVVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),""));
					dosREPORT.writeBytes(padSTRING('R',"("+setNumberFormat(L_dblINVVL,2)+")",14));
					L_dblADJVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("pa_adjvl"),""));
					dblADJTOT += L_dblADJVL;
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblADJVL,2),55));
					
					dosREPORT.writeBytes(padSTRING('L',strDOCNO,20));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL((String)hstDOCDT.get(strDOCNO),""),13));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
				else
				{
					break;
				}
				if(!M_rstRSSET.next())
					break;
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(crtLINE(intRPTWD-74,"- ")+"\n");
			dosREPORT.writeBytes(padSTRING('R'," ",82));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblRCTTOT,2),16));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblADJTOT,2),19));
			dosREPORT.writeBytes(padSTRING('L',"Bal :",17));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblRCTTOT-dblADJTOT,2),11));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(crtLINE(intRPTWD-74,"- ")+"\n");
			dblADJTOT=0.0;
			//dblINVTOT=0.0;
			dblRCTTOT=0.0;
			//L_intGRPCD=1;
			cl_dat.M_intLINNO_pbst +=4;
			
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);					
			}			
			if(fosREPORT !=null)    
			    fosREPORT.close();
			if(dosREPORT !=null)        
			    dosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspRCTDATA");
		}
		setMSG("",'N'); 
	}
	/**
	 * Method to generate the header of the Report.
	*/
	void prnHEADER()
	{
		java.sql.Date jdtTEMP=null;
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intRPTWD-25)+padSTRING('L',"Report Date : "+cl_dat.M_txtCLKDT_pbst.getText(),25)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Payment Details From "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),intRPTWD-24)+padSTRING('R',"Page No.    : "+intPAGENO,21)+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n");
			dosREPORT.writeBytes("  Doc.Tp   Doc.No.       Doc.Date     Pmt.Mode  Description                            Credit Amt.         Debit Amt.            Trn.No.    Date "+"\n");
			dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n");
			cl_dat.M_intLINNO_pbst +=5;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
	}
	/**
	 *
	 *Method to create lines that are used in the Reports
	*/
     private String crtLINE(int P_strCNT,String P_strLINCHR)
     {
		String strln = "";
		try
		{
			for(int i=1;i<=P_strCNT;i++)   
				strln += P_strLINCHR;
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
        return strln;
	}
}