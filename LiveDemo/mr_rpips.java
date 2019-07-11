
/*
System Name   : Marketing System
Program Name  : Invoice wise Payment Summary
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 14/06/2006"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :17/07/2006
Modified det.  :
Version        :
*/

import java.sql.ResultSet;import java.util.Date;import java.util.Hashtable;
import java.awt.event.KeyEvent;import javax.swing.JComponent; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;

class mr_rpips extends cl_rbase
{			
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;		 /** JTextField to  enter From Date */
	private JTextField txtFMDAT;		 /** JtextField to  enter To Date */
	private JTextField txtTODAT;		 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 148;      // report width
	//private int intPAGENO=0;
	private ResultSet L_rstRSSET ;
	private String strDEBIT="DB";
	private String strCREDT="CR";
	private String strINVNO="";
	private Hashtable<String,String> hstPNTVL=new Hashtable<String,String>();
	public mr_rpips()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Party Code"),2,3,1,1,this,'L');
			add(txtPRTCD=new TxtLimit(5),2,4,1,1,this,'L');
			add(new JLabel("Party Name"),3,3,1,1,this,'L');
			add(txtPRTNM=new TxtLimit(30),3,4,1,3,this,'L');
			add(new JLabel("From Date"),4,3,1,1,this,'L');
			add(txtFMDAT=new TxtDate(),4,4,1,1,this,'L');
			add(new JLabel("To Date"),5,3,1,1,this,'L');
			add(txtTODAT=new TxtDate(),5,4,1,1,this,'L');
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
			if(txtPRTCD.getText().trim().length()==0)
			{
				setMSG("Please Enter Party Code ..",'E');
				txtPRTCD.requestFocus();
				return false;
			}
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
				txtFMDAT.requestFocus();
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
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD = "txtPRTCD";
				M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD  AND isnull(PT_STSFL,'')<> 'X' AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
				if(txtPRTCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtPRTCD.getText().trim().toUpperCase()+"%'"; 
				}
				M_strSQLQRY += " order by  PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT");
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			
			if(M_objSOURC == txtPRTCD)
			{
				txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());	
				if(txtPRTCD.getText().trim().length()==5)
				{
					try
					{
						M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD  AND isnull(PT_STSFL,'')<> 'X' AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
						M_strSQLQRY += "AND PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'"; 
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
						if(M_rstRSSET!=null &&M_rstRSSET.next())
						{
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  "));
							txtFMDAT.requestFocus();
						}
						else
						{
							setMSG("Enter Proper Party Code Or Press F1 For Help..",'E');
								txtPRTCD.requestFocus();
						}				
						if(M_rstRSSET != null)
							M_rstRSSET.close();
					}
					catch(Exception E_KP)
					{
						setMSG(E_KP,"getKEPRESS");
					}
				}
				setMSG("Enter From date ",'N');
			}
			if(M_objSOURC== txtFMDAT)
			{
				txtTODAT.requestFocus();
				setMSG("Enter To Date",'N');
			}
			if(M_objSOURC == txtTODAT )
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtPRTCD.setText(L_STRTKN1.nextToken());
				txtPRTNM.setText(L_STRTKN1.nextToken());
			}
		}
		catch(Exception e)
		{
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpips.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpips.doc";
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
	
	private void getDATA()
	{
		try
		{
				
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO =1;
			java.sql.Date L_datTMPDT;
			double L_dblDOCVL=0.0;
			double L_dblTOTAL=0.0;
			double L_dblDEBIT=0.0;
			double L_dblCREDT=0.0;
			double L_dblBALVL=0.0;
			boolean L_flgRSSET=false;
			
			int L_intROWCT=0;
			String L_strDBTNO="";
			String L_strDEBIT="";
			String L_strCREDT="";
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Product Specification Sheet</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			prnHEADER();
					
			M_strSQLQRY = "SELECT PT_INVNO,PT_TRNTP,SUM(PT_PNTVL) PT_PNTVL FROM MR_PTTRN WHERE PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_PRTTP='C' AND PT_PRTCD ='"+txtPRTCD.getText().trim()+"' GROUP BY PT_INVNO,PT_TRNTP ORDER BY PT_INVNO,PT_TRNTP";
			//System.out.println("Query MR_PTTRN ="+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				hstPNTVL.put((nvlSTRVL(M_rstRSSET.getString("PT_INVNO"),"  "))+(nvlSTRVL(M_rstRSSET.getString("PT_TRNTP"),"  ")),(nvlSTRVL(M_rstRSSET.getString("PT_PNTVL"),"  ")));
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			M_strSQLQRY = "SELECT PL_DOCNO,PL_DOCDT,PL_DOCVL FROM MR_PLTRN WHERE PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_PRTTP='C' AND PL_PRTCD ='"+txtPRTCD.getText().trim()+"'";
			M_strSQLQRY +=" AND PL_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
			M_strSQLQRY +="	AND PL_DOCTP = '21' ORDER BY PL_DOCNO ";
			//System.out.println("Query MR_PLTRN ="+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
						
			M_strSQLQRY = "SELECT PR_CHQNO,PR_CHQDT,PR_DOCNO,PA_DBTNO,PA_ADJVL FROM MR_PRTRN,MR_PATRN WHERE PR_CMPCD=PA_CMPCD and PR_PRTTP =PA_PRTTP";
			M_strSQLQRY +=" AND PR_PRTCD = PA_PRTCD AND PR_DOCNO = PA_CRDNO AND PR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_PRTTP ='C' AND PR_PRTCD ='"+txtPRTCD.getText().trim()+"'" +
            " and PA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PA_DBTNO in(SELECT PL_DOCNO FROM MR_PLTRN WHERE PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_PRTTP='C' AND PL_PRTCD ='"+txtPRTCD.getText().trim()+"' AND PL_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' 	AND PL_DOCTP = '21' ) ORDER BY PA_DBTNO ";
//			System.out.println("Query MR_PRTRN & MR_PATRN ="+M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(L_rstRSSET!=null)
			{
				if(L_rstRSSET.next())
				{
					
					L_flgRSSET=true;
					//System.out.println(L_flgRSSET);
				}
			}
			
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					L_dblDOCVL=0.0;
			        L_dblTOTAL=0.0;
			        L_dblDEBIT=0.0;
			        L_dblCREDT=0.0;
			        L_dblBALVL=0.0;
					L_intROWCT=0;
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
					}
					strINVNO=nvlSTRVL(M_rstRSSET.getString("pl_docno"),"");
					dosREPORT.writeBytes(padSTRING('R',strINVNO,12));
					L_datTMPDT =M_rstRSSET.getDate("PL_DOCDT");
					if(L_datTMPDT !=null)
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),12));	
					else
						dosREPORT.writeBytes(padSTRING('R'," ",12));	
					L_dblDOCVL=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),"0"));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDOCVL,0),14));	
					if(hstPNTVL.containsKey(strINVNO+strDEBIT))
					{
						L_strDEBIT=nvlSTRVL(hstPNTVL.get(strINVNO+strDEBIT).toString(),"0");
						L_dblDEBIT=Double.parseDouble(L_strDEBIT);
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDEBIT,0),16));	
					}
					else
						dosREPORT.writeBytes(padSTRING('L',"-",16));	
					if(hstPNTVL.containsKey(strINVNO+strCREDT))
					{
						L_strCREDT=nvlSTRVL(hstPNTVL.get(strINVNO+strCREDT).toString(),"0");
						L_dblCREDT=Double.parseDouble(L_strCREDT);
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCREDT,0),16));	
					}
					else
						dosREPORT.writeBytes(padSTRING('L',"-",16));	
					L_dblTOTAL=L_dblDOCVL+L_dblDEBIT-L_dblCREDT;
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAL,0),16));	
					if(L_flgRSSET)
					{
						//System.out.println(" strINVNO = "+strINVNO);
						while(true)
						{
							L_strDBTNO=nvlSTRVL(L_rstRSSET.getString("PA_DBTNO"),"");
							//System.out.println(" L_strDBTNO = "+L_strDBTNO);
							
							if(L_strDBTNO.equals(strINVNO))
							{	
								if(L_intROWCT>0)					
								{
									dosREPORT.writeBytes(padSTRING('R'," ",86));	
								}
								dosREPORT.writeBytes(padSTRING('R'," ",6));	
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PR_DOCNO"),""),14));
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PR_CHQNO"),""),10));
								L_dblDOCVL=Double.parseDouble(nvlSTRVL(L_rstRSSET.getString("PA_ADJVL"),""));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDOCVL,0),14));
								L_dblBALVL=L_dblTOTAL-L_dblDOCVL;
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBALVL,0),18));
								dosREPORT.writeBytes("\n"); 
								L_dblTOTAL=L_dblBALVL;
								L_intROWCT++;						
								cl_dat.M_intLINNO_pbst += 1;
							}
							else 
								break;
							
							if(!L_rstRSSET.next())
							{
								L_flgRSSET=false;
								break;
							}
						}
					}
					intRECCT++;
					dosREPORT.writeBytes("\n"); 
					cl_dat.M_intLINNO_pbst += 1;
				
				}// End of Outer While loop
			}//End Of Outer If Loop
			
			
			if(L_flgRSSET)
			{
				while(true)
				{
					L_strDBTNO=nvlSTRVL(L_rstRSSET.getString("PA_DBTNO"),"");
					if(L_strDBTNO.equals(strINVNO))
					{	
						if(L_intROWCT>0)					
						{
							dosREPORT.writeBytes(padSTRING('R'," ",86));	
						}
						dosREPORT.writeBytes(padSTRING('R'," ",6));	
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PR_DOCNO"),""),14));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PR_CHQNO"),""),10));
						L_dblDOCVL=Double.parseDouble(nvlSTRVL(L_rstRSSET.getString("PA_ADJVL"),""));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDOCVL,0),14));
						L_dblBALVL=L_dblTOTAL-L_dblDOCVL;
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBALVL,0),18));
						dosREPORT.writeBytes("\n"); 
						L_dblTOTAL=L_dblBALVL;
						L_intROWCT++;						
						cl_dat.M_intLINNO_pbst += 1;
					}
					else 
						break;
					if(!L_rstRSSET.next())
					{
						L_flgRSSET=false;
							break;
					}
				}
			}
		
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
				prnFMTCHR(dosREPORT,M_strEJT);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			fosREPORT.close();
			dosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setMSG("",'N'); 
	}
	
	/**
	 * Method to generate the header of the Report.
	*/
	void prnHEADER()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intRPTWD-25)+"\n");
		
				dosREPORT.writeBytes(padSTRING('R',"Invoicewise Payment Summary",intRPTWD-25)+"\n");
		
			dosREPORT.writeBytes(padSTRING('R',"Party :"+txtPRTNM.getText().trim(),intRPTWD-25)+padSTRING('L',"Report Date : "+cl_dat.M_txtCLKDT_pbst.getText(),25)+"\n");
			
			dosREPORT.writeBytes(padSTRING('R',"Date Range : "+txtFMDAT.getText()+ " to "+ txtTODAT.getText(),intRPTWD-24)+padSTRING('R',"Page No.    : "+cl_dat.M_PAGENO ,21)+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); 
			cl_dat.M_intLINNO_pbst +=1;
			dosREPORT.writeBytes("Inv. No     Inv.Date           Inv.Amt      Debit Note     Credit Note           Total      Doc.No        Chq.No            Amount           Balance   ");
			dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n"); 
			cl_dat.M_intLINNO_pbst += 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	
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
			//System.out.println("L_EX Error in Line:"+L_EX);
			setMSG(L_EX,"crtLINE");
		}
        return strln;
	}
}