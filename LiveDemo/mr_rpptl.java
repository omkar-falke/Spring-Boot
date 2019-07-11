/*
System Name   : Marketing System
Program Name  : 
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 10/07/2006"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :25/07/2006
Modified det.  :
Version        :
*/

import java.sql.ResultSet;import java.util.Date;
import java.awt.event.KeyEvent;import javax.swing.JComponent;import javax.swing.JComboBox; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import java.util.Hashtable;import java.util.StringTokenizer;

class mr_rpptl extends cl_rbase
{			
	private JComboBox cmbPRTTP;
	private JCheckBox chbBALVL;
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;		 /** JTextField to  enter From Date */
	private JTextField txtFMDAT;		 /** JtextField to  enter To Date */
	private JTextField txtTODAT;		 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 120;      // report width
	private int intPAGENO=0;
	private ResultSet L_rstRSSET ;
	private String strDOTLN = "---------------------------------------------------------------------------------------------";
	public mr_rpptl()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			String L_strCODCD="";
			cmbPRTTP=new JComboBox();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXPRT'";
			M_strSQLQRY += "And CMT_CODCD in('C','D')";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODCD =L_strCODCD+" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbPRTTP.addItem(L_strCODCD);
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			add(new JLabel("Party Type"),2,3,1,1,this,'L');
			add(cmbPRTTP,2,4,1,2,this,'L');
			add(new JLabel("Party Code"),3,3,1,1,this,'L');
			add(txtPRTCD=new TxtLimit(5),3,4,1,1,this,'L');
			add(new JLabel("Party Name"),4,3,1,1,this,'L');
			add(txtPRTNM=new TxtLimit(30),4,4,1,3,this,'L');
			add(new JLabel("From Date"),5,3,1,1,this,'L');
			add(txtFMDAT=new TxtDate(),5,4,1,1,this,'L');
			add(new JLabel("To Date"),6,3,1,1,this,'L');
			add(txtTODAT=new TxtDate(),6,4,1,1,this,'L');
            add(chbBALVL = new JCheckBox("Show Running balance"),7,3,1,2,this,'L');
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
			if(txtPRTCD.getText().trim().length()<5)
			{
				setMSG("Please Enter Proper Party Code  or Press f1 for Help..",'E');
				txtPRTCD.requestFocus();
				return false;
			}
			if(txtPRTCD.getText().trim().length()!=0)
			{
				txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());				
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
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("To Date can not be Less than From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}		
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
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
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD = "txtPRTCD";
				M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN";
				M_strSQLQRY += " where  PT_PRTTP='"+(cmbPRTTP.getSelectedItem().toString()).substring(0,1)+"' ";
				M_strSQLQRY += " AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD  AND isnull(PT_STSFL,'')<> 'X' ";
				if(txtPRTCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtPRTCD.getText().trim().toUpperCase()+"%'"; 
				}
				M_strSQLQRY += " Order by PT_PRTNM ";
				//System.out.println("HELP = "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT");
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			if(M_objSOURC==cmbPRTTP)
			{
				txtPRTCD.requestFocus();
			}
			if(M_objSOURC == txtPRTCD)
			{
				txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());	
				if(txtPRTCD.getText().trim().length() ==5)
				{
					try
					{
						M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN  where  PT_PRTTP='"+(cmbPRTTP.getSelectedItem().toString()).substring(0,1)+"' AND PL_PRTCD=PT_PRTCD AND PL_PRTCD=PT_PRTCD  AND isnull(PT_STSFL,'')<> 'X'";
						M_strSQLQRY += "AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'"; 
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
						if(M_rstRSSET!=null &&M_rstRSSET.next())
						{
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  "));
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
				else
				{
					txtPRTNM.setText("");
				}
				txtFMDAT.requestFocus();
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
		setMSG("",'N');
		if(M_strHLPFLD.equals("txtPRTCD"))
		{
			StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
			txtPRTCD.setText(L_STRTKN1.nextToken());
			txtPRTNM.setText(L_STRTKN1.nextToken());
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpptl.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpptl.doc";
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Party Ledger"," ");
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
			boolean flgTEMP=true;
			double L_dblBALVL =0;
			double L_dblPRTBL=0.0;
			double L_dblDEBIT=0.0;
			double L_dblCREDT=0.0;
			String L_strDOCVL="";
			double L_dblDOCVL=0;
			int intSEQNO=0;
			String L_strNXDAT="";
			String L_strPRDAT="";
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO =1;
			String L_strDOCTP="";
			String L_strPRTBL="";
			String L_strBALFL="";
			String L_strDOCDS ="";
			String L_strFMDAT="";
			String L_strYSTDT="";
			java.sql.Date L_datTMPDT =null;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Product Specification Sheet</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			L_strFMDAT=txtFMDAT.getText().trim();
			
			M_strSQLQRY="Select PT_YOPVL PRTBL,PT_YOPFL BALFL,PT_YSTDT from CO_PTMST where PT_PRTTP ='"+(cmbPRTTP.getSelectedItem().toString()).substring(0,1)+"'";
			M_strSQLQRY +=" AND PT_PRTCD='"+txtPRTCD.getText().trim().toUpperCase()+"' and isnull(PT_STSFL,'')<> 'X'"; 
						
			//System.out.println("\n CO_PTMST = "+M_strSQLQRY+"\n");
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				L_strPRTBL=nvlSTRVL(M_rstRSSET.getString("PRTBL"),"0.0");
				//System.out.println("DATE2 ="+L_strYSTDT);
				L_strBALFL=nvlSTRVL(M_rstRSSET.getString("BALFL"),"");
				L_datTMPDT=M_rstRSSET.getDate("PT_YSTDT");
				if(L_datTMPDT!=null)
				{
					L_strYSTDT=M_fmtLCDAT.format(L_datTMPDT);
				}
				//System.out.println("PRTBL ="+L_strPRTBL);
				//System.out.println("DATE ="+L_strYSTDT);
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		//	System.out.println("DATE 1 ="+L_datTMPDT);
			if (L_datTMPDT==null)
			{
				L_strFMDAT=L_strFMDAT;
			}
			else if((M_fmtLCDAT.parse(L_strFMDAT).compareTo(M_fmtLCDAT.parse(L_strYSTDT))<=0))
			{
				L_strFMDAT=L_strYSTDT;
				flgTEMP=false;
			}
			if(flgTEMP)
			{
				M_strSQLQRY ="Select PL_SEQNO from MR_PLTRN where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_PRTTP='"+(cmbPRTTP.getSelectedItem().toString()).substring(0,1)+"'";
				M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim().toUpperCase()+"' AND PL_DOCDT >='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+ "' and isnull(PL_STSFL,'')<> 'X' order by PL_SEQNO"; 
				//System.out.println("Seq No QUERY = "+ M_strSQLQRY);
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null && M_rstRSSET.next())
				{
					intSEQNO=M_rstRSSET.getInt("PL_SEQNO");
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				
				if(intSEQNO>1)
				{
					intSEQNO=intSEQNO-1;
					M_strSQLQRY="Select PL_BALVL PRTBL,PL_BALFL  BALFL from MR_PLTRN where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_PRTTP ='"+(cmbPRTTP.getSelectedItem().toString()).substring(0,1)+"' AND PL_SEQNO ="+intSEQNO+"";
					M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim().toUpperCase()+"'  AND isnull(PL_STSFL,'')<> 'X'"; 
					//System.out.println("\n Query PARTY BAL PLTRN  = "+M_strSQLQRY);
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						L_strPRTBL=nvlSTRVL(M_rstRSSET.getString("PRTBL"),"0.0");
						L_strBALFL=nvlSTRVL(M_rstRSSET.getString("BALFL"),"");
					}
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
				
			prnHEADER();
			dosREPORT.writeBytes(padSTRING('R',"Opening Balance : ",29));
			if(L_strBALFL.equals("DB"))
			{				
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strPRTBL),0),15));
				L_dblDEBIT+=Double.parseDouble(L_strPRTBL);
			}
			if(L_strBALFL.equals("CR"))
			{
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strPRTBL),0),35));
				L_dblCREDT +=Double.parseDouble(L_strPRTBL);
			}
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst +=1;
			
			//L_dblBALVL = Double.parseDouble(L_strPRTBL);
						
			M_strSQLQRY="select PL_DOCDT,PL_DOCNO,PL_SEQNO,PL_DOCVL,PL_BALVL,PL_BALFL,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP from MR_PLTRN ";
			M_strSQLQRY +="where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_PRTTP ='"+(cmbPRTTP.getSelectedItem().toString()).substring(0,1)+"'";
			M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim().toUpperCase()+"' ";
			M_strSQLQRY +=" AND PL_DOCDT BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'  ";
			M_strSQLQRY +="and isnull(PL_STSFL,'')<> 'X' order by PL_SEQNO"; 
			
			//System.out.println(M_strSQLQRY);
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				if(cl_dat.M_intLINNO_pbst >60)
				{
				   	dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO +=1;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}					
				
				L_datTMPDT =M_rstRSSET.getDate("PL_DOCDT");
				L_strNXDAT=M_fmtLCDAT.format(L_datTMPDT).toString();
				if(L_datTMPDT !=null)
				{
					if(!L_strPRDAT.equals(L_strNXDAT))
					{
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst +=1;
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),17));	
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",17));	
				
				}
				else
				dosREPORT.writeBytes(padSTRING('R'," ",17));	
					
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PL_DOCNO"),""),9));	
				
				L_strDOCTP=nvlSTRVL(M_rstRSSET.getString("PL_DOCTP"),"");
				if(L_strDOCTP.equals("0"))
				    L_strDOCDS = "CRN";
				else if(L_strDOCTP.equals("1"))
				    L_strDOCDS = "RCT"; 
				else if(L_strDOCTP.equals("2"))
				    L_strDOCDS = "INV";
				else if(L_strDOCTP.equals("3"))
				    L_strDOCDS = "DBN";
				else if(L_strDOCTP.equals("4"))
				    L_strDOCDS = "PMT";

				dosREPORT.writeBytes(L_strDOCDS);
				L_strDOCVL=nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),"0");
				
				if(L_strDOCTP.equals("2")||L_strDOCTP.equals("3")||L_strDOCTP.equals("4"))
				{
					L_dblDOCVL=Double.parseDouble(L_strDOCVL);
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDOCVL,0),15));
					//L_dblBALVL = L_dblBALVL + L_dblDOCVL;
					L_dblDEBIT +=L_dblDOCVL;
					
					dosREPORT.writeBytes(padSTRING('R'," ",20));
				}
				else
				{
					dosREPORT.writeBytes(padSTRING('R'," ",18));
					L_dblDOCVL=Double.parseDouble(L_strDOCVL);
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDOCVL,0),17));
					//L_dblBALVL = L_dblBALVL -L_dblDOCVL;
					L_dblCREDT += L_dblDOCVL;
				}
				
				L_dblPRTBL=M_rstRSSET.getDouble("PL_BALVL");
				if(chbBALVL.isSelected())
				{
    				if(L_dblCREDT<L_dblDEBIT)
						L_dblBALVL=L_dblDEBIT-L_dblCREDT;
					else
						L_dblBALVL=L_dblCREDT-L_dblDEBIT;
					
					dosREPORT.writeBytes(padSTRING('R'," ",10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTBL,0)+" "+M_rstRSSET.getString("PL_BALFL"),15));   
					if(L_dblPRTBL!=L_dblBALVL)
						dosREPORT.writeBytes(padSTRING('L',"*",2)); 
				}
				//L_strPRTBL=M_rstRSSET.getString("PL_BALVL");
				L_strBALFL=nvlSTRVL(M_rstRSSET.getString("PL_BALFL"),"");
				dosREPORT.writeBytes("\n");
				L_strPRDAT=L_strNXDAT;
				cl_dat.M_intLINNO_pbst +=1;
		    	intRECCT++;
			}
			dosREPORT.writeBytes("\n");
			
			
			
			dosREPORT.writeBytes(padSTRING('R',"Closing Balance : ",29));
			if(L_strBALFL.equals("CR"))
			{				
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTBL,0),15));
				//dosREPORT.writeBytes(padSTRING('L',L_strBALFL,3));
				//L_dblDEBIT +=Double.parseDouble(L_strPRTBL);
				L_dblDEBIT +=L_dblPRTBL;
				
				
				
			}
			if(L_strBALFL.equals("DB"))
			{
			//	dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strPRTBL),0),35));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTBL,0),35));
				//dosREPORT.writeBytes(padSTRING('L',L_strBALFL,3));
				//L_dblCREDT +=Double.parseDouble(L_strPRTBL);
				L_dblCREDT +=L_dblPRTBL;
				
			}
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
			dosREPORT.writeBytes(padSTRING('R'," ",20));
			dosREPORT.writeBytes(padSTRING('L',"Total :",9));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDEBIT,0),15));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCREDT,0),20));
			
			
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
			if(M_rstRSSET != null)
				M_rstRSSET.close();
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
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Party Ledger",strDOTLN.length())+"\n");
			  dosREPORT.writeBytes(padSTRING('R',cmbPRTTP.getSelectedItem().toString().substring(2, cmbPRTTP.getSelectedItem().toString().length())     +":"+txtPRTNM.getText().trim()+" ("+txtPRTCD.getText().trim()+")",strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Report Date:"+(cl_dat.M_strLOGDT_pbst),22)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Date Range :"+txtFMDAT.getText().trim()+" to "+ txtTODAT.getText().trim(),strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Page No    :"+cl_dat.M_PAGENO ,22)+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
		
			
			cl_dat.M_intLINNO_pbst +=4;
			
			dosREPORT.writeBytes(strDOTLN +"\n");	
			if(chbBALVL.isSelected())
			    dosREPORT.writeBytes("Date             Document Ref          Debit              Credit                  Balance   ");
			else
			    dosREPORT.writeBytes("Date             Document Ref          Debit              Credit   ");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst += 3;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
			
		}
	}
	
}