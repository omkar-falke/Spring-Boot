/*
System Name   : Production System
Program Name  : 
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 14/10/2006
Version       : PR v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.sql.ResultSet;import java.util.Date;import java.sql.Timestamp;
import java.awt.event.KeyEvent;import javax.swing.JComponent;import javax.swing.JComboBox; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import java.util.Hashtable;import java.util.StringTokenizer;

class pr_rpbat extends cl_rbase
{			
	
	private JTextField txtFMDAT;
	private JTextField txtTODAT;
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 120;      // report width
	private int intPAGENO=0;
	private ResultSet L_rstRSSET ;
	String strPRTNM="";
	private String strDOTLN = "---------------------------------------------------------------------------";
	String strTODAT=cl_dat.M_strLOGDT_pbst;
	String strLINNO="";
	String strSTRDT="";
	String strENDDT="";
	String strPSTDT="";
	String strPSTTM="";
	String strPENDT="";
	String strPENTM="";
	
	public pr_rpbat()
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
			
			add(new JLabel("From Date"),5,3,1,1,this,'L');
			add(txtFMDAT=new TxtDate(),5,4,1,1,this,'L');
			add(new JLabel("To Date"),6,3,1,1,this,'L');
			add(txtTODAT=new TxtDate(),6,4,1,1,this,'L');
			
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
		txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
		txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
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
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT )
			{
				txtTODAT.requestFocus();
			}
			
			if(M_objSOURC == txtTODAT )
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			cl_dat.M_PAGENO =1;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"pr_rpbat.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "pr_rpbat.doc";
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
			ResultSet L_rstRSSET;
			String L_strPRTTP="";
			String L_strPRTCD="";
			String L_strFMDAT="";
			String L_strPRDQT="";
			String L_strPSTDT="";
			String L_strPENDT="";
			Timestamp L_TMPTM=null;
			double L_dblPRDQT=0;
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO =0;		
			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Datewise Production Details </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			int i=0;
			
			/*
			M_strSQLQRY=" Select CT_MATDS,CT_UOMCD,sum(LTR_ISSQT)LTR_ISSQT  from PR_LTRDT,CO_CTMST,pr_btmst where LTR_MATCD=CT_MATCD ";
			M_strSQLQRY +="AND LTR_BATNO=bt_batno and  date(bt_bstdt) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'    group by CT_MATDS,CT_UOMCD";
			*/
			
			M_strSQLQRY = " Select CT_MATCD,CT_MATDS,CT_UOMCD,sum(LTR_ISSQT)LTR_ISSQT ";
			M_strSQLQRY +=" from PR_LTRDT,CO_CTMST where LTR_MATCD=CT_MATCD AND";
			M_strSQLQRY +=" LTR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LTR_BATNO in(select distinct bt_batno from pr_btmst where ";
			M_strSQLQRY +=" BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,bt_bstdt,101)BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"')";
			M_strSQLQRY +=" group by CT_MATCD,CT_MATDS,CT_UOMCD ";

			
//			System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			prnHEADER();
			i=0;
			L_dblPRDQT=0;
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(cl_dat.M_intLINNO_pbst >64)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN+"\n");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO +=1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CT_MATDS"),40));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CT_UOMCD"),15));
					//System.out.println("L_dblPRDQT " + M_rstRSSET.getString("CT_UOMCD"));
					L_strPRDQT= nvlSTRVL(M_rstRSSET.getString("LTR_ISSQT"),"0");
					L_dblPRDQT +=Double.parseDouble(L_strPRDQT);
					//System.out.println("L_dblPRDQT "+L_dblPRDQT);
					dosREPORT.writeBytes(padSTRING('L',L_strPRDQT,10));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					i++;
					intRECCT++;
				}
				dosREPORT.writeBytes(strDOTLN+"\n");
				M_rstRSSET.close();
			}
		}
		catch(Exception E)
		{
			setMSG(E,"getDATA");
		}
	}

	void prnHEADER()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
		
			dosREPORT.writeBytes(padSTRING('R',""+cl_dat.M_strCMPNM_pbst,strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Report Date:"+(cl_dat.M_strLOGDT_pbst),22)+"\n");
			dosREPORT.writeBytes(padSTRING('R'," ",strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Page No    :"+cl_dat.M_PAGENO ,22)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Raw Material Consumption For Period  : "+txtFMDAT.getText().trim() +" To "+txtTODAT.getText().trim()+" ",strDOTLN.length()-2)+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");		
			
			cl_dat.M_intLINNO_pbst +=1;
			
			dosREPORT.writeBytes(strDOTLN +"\n");	
			dosREPORT.writeBytes("Description                             UOM              Quantity    ");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst += 3;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
			
		}
	}
}