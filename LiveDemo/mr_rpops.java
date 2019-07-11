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
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
/**
 
<PRE>
System Name   : Marketing System
Program Name  : Party Ledger
Modify Date   : 25/07/2006

*/
class mr_rpops extends cl_rbase
{	
	private ButtonGroup btgRPTTP;
	private JRadioButton rdbDETAL;
	private JRadioButton rdbSUMRY;		/** JTextField to  enter Year Starting Date */
	private JTextField txtYSDAT;		 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private String strYSTDT="";
	//private ResultSet L_rstRSSET ;
	private String strDOTLN = "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	public mr_rpops()
	{
		super(2);
		setMatrix(20,12);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			java.sql.Date L_datTMPDT=null;
			btgRPTTP=new ButtonGroup();
			
			M_strSQLQRY="Select SP_YSTDT from SA_SPMST where  SP_SYSCD='MR'";
			//System.out.println(M_strSQLQRY);
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			
			add(rdbSUMRY=new JRadioButton("Summary",true),4,5,1,2,this,'L');
			add(rdbDETAL=new JRadioButton("Details"),4,7,1,1,this,'L');
			
			btgRPTTP.add(rdbSUMRY);
			btgRPTTP.add(rdbDETAL);		
			add(new JLabel("Year Starting Date"),5,5,1,2,this,'L');
			add(txtYSDAT=new TxtDate(),5,7,1,1.5,this,'L');
			if(M_rstRSSET.next())
			{
				L_datTMPDT =M_rstRSSET.getDate("SP_YSTDT");
				if(L_datTMPDT !=null)
				{
					strYSTDT=M_fmtLCDAT.format(L_datTMPDT).toString();
					txtYSDAT.setText(strYSTDT);
					//dosREPORT.writeBytes(padSTRING('R',L_strDATE,10));
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
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		txtYSDAT.setText(strYSTDT);
		//txtYSDAT.setText("01/04/2006");
	}
	
	public boolean vldDATA()
	{
		try
		{
			
			if(txtYSDAT.getText().trim().length()==0)
			{
				setMSG("Please Enter Year Starting Date ..",'E');
				txtYSDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtYSDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(strYSTDT.trim()))!=0)
			{
				setMSG("Enter Date Should be year Starting Date .."+strYSTDT,'E');
				txtYSDAT.requestFocus();
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
			if(M_objSOURC == txtYSDAT )
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
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpops.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpops.doc";
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Sales Details of Year Starting Stock"," ");
					
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
			boolean L_strEOF=false;
			cl_dat.M_PAGENO=1;
			cl_dat.M_intLINNO_pbst = 0;
			String L_strPRDCD1="";
			String L_strPRDCD2="";
			String L_strYSTDT;
			String L_strDATE="";
			String L_strSALTP;
			String L_strGRADE="";
			int L_intSUMRY=0;
			double L_dblASSVL=0.0;
			double L_dblINVQT=0.0;
			double L_dblLADRT=0.0;
			double L_dblINVRT=0.0;
			double L_dblBALQT=0.0;
			double L_dblDUTVL=0.0;
			double L_dblINVQT1=0.0;
			double L_dblSTKQT=0.0;
			double L_dblGTSTQ=0.0;
				
			double L_dblDOQTY=0.0;
			double L_dblDOVAL=0.0;
			double L_dblDODUT=0.0;
			double L_dblGTDOQ=0.0;
			double L_dblGTDOD=0.0;
			
			double L_dblDEQTY=0.0;
			double L_dblDEVAL=0.0;
			double L_dblDEDUT=0.0;
			double L_dblGTDEQ=0.0;
			double L_dblGTDED=0.0;
			
			double L_dblEXQTY=0.0;
			double L_dblEXVAL=0.0;
			double L_dblEXDUT=0.0;
			double L_dblGTEXQ=0.0;
			double L_dblGTEXD=0.0;
			
			double L_dblCAQTY=0.0;
			double L_dblCAVAL=0.0;
			double L_dblCADUT=0.0;
			double L_dblGTCAQ=0.0;
			double L_dblGTCAD=0.0;
			
			double L_dblTRQTY=0.0;
			double L_dblTRVAL=0.0;
			double L_dblTRDUT=0.0;
			double L_dblGTTRQ=0.0;
			double L_dblGTTRD=0.0;

			double L_dblTOTQT=0.0;
			double L_dblNETBL=0.0;
			double L_dblGTNEB=0.0;
			
			

			String L_strCURCD="";
			java.sql.Date L_datTMPDT =null;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Sales Details of Year Starting Stock</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			L_strYSTDT=txtYSDAT.getText().trim();
			/*
			M_strSQLQRY ="select  IVT_PRDDS,date(IVT_INVDT)IVT_INVDT, IVT_SALTP, IVT_INVNO,IVT_INVQT,";
			M_strSQLQRY +=" IVT_CURCD, IVT_INVRT, IVT_LADRT, isnull(IVT_ASSVL,0) IVT_ASSVL, isnull(IVT_EXCVL,0)+isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0) ";
			M_strSQLQRY +="IVT_DUTVL,ivt_prdcd, sum(isnull(OP_YOSQT,0)+isnull(OP_YOXQT,0))OP_YOSQT from MR_IVTRN,fg_opstk "; 
			M_strSQLQRY +=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND date(IVT_INVDT) >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' and IVT_CMPCD=OP_CMPCD and IVT_PRDCD = OP_PRDCD and OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(OP_YOSQT,0)+isnull(OP_YOXQT,0)>0 and ivt_stsfl <> 'X'  and isnull(IVT_INVQT,0)>0  and IVT_SALTP NOT IN('14','15')"; 
			M_strSQLQRY +="  group by ivt_prdds,date(IVT_INVDT), IVT_SALTP, IVT_INVNO,IVT_INVQT,IVT_CURCD, IVT_INVRT, IVT_LADRT, ";
			M_strSQLQRY +=" isnull(IVT_ASSVL,0), isnull(IVT_EXCVL,0)+isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0),ivt_prdcd  order by ivt_prdcd,IVT_INVDT,IVT_SALTP,IVT_INVNO";
			*/

			
			M_strSQLQRY ="select  PR_PRDDS,CONVERT(varchar,IVT_INVDT,101)IVT_INVDT, IVT_SALTP, IVT_INVNO,IVT_INVQT,";
			M_strSQLQRY +=" IVT_CURCD, IVT_INVRT, IVT_LADRT, isnull(IVT_ASSVL,0) IVT_ASSVL, isnull(IVT_EXCVL,0)+isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0) ";
			M_strSQLQRY +="IVT_DUTVL,op_prdcd, sum(isnull(OP_YOSQT,0)+isnull(OP_YOXQT,0))OP_YOSQT from co_prmst,fg_opstk left outer join MR_IVTRN on  "; 
			M_strSQLQRY +="  IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,IVT_INVDT,101) >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' and IVT_CMPCD=OP_CMPCD and IVT_PRDCD = OP_PRDCD and  ivt_stsfl <> 'X'  and isnull(IVT_INVQT,0)>0  and IVT_SALTP NOT IN('14','15') where OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and OP_PRDCD = PR_PRDCD and isnull(OP_YOSQT,0)+isnull(OP_YOXQT,0)>0 " ; 
			M_strSQLQRY +="  group by pr_prdds,CONVERT(varchar,IVT_INVDT,101), IVT_SALTP, IVT_INVNO,IVT_INVQT,IVT_CURCD, IVT_INVRT, IVT_LADRT, ";
			M_strSQLQRY +=" isnull(IVT_ASSVL,0), isnull(IVT_EXCVL,0)+isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0),op_prdcd  order by op_prdcd,IVT_INVDT,IVT_SALTP,IVT_INVNO";
			

			
			/*M_strSQLQRY ="select  PR_PRDDS,date(IVT_INVDT)IVT_INVDT, IVT_SALTP, IVT_INVNO,IVT_INVQT,";
			M_strSQLQRY +=" IVT_CURCD, IVT_INVRT, IVT_LADRT, isnull(IVT_ASSVL,0) IVT_ASSVL, isnull(IVT_EXCVL,0)+isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0) ";
			M_strSQLQRY +="IVT_DUTVL,op_prdcd, sum(isnull(OP_YOSQT,0)+isnull(OP_YOXQT,0))OP_YOSQT from fg_opstk,co_prmst left outer join MR_IVTRN on "; 
			M_strSQLQRY +="  IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  date(IVT_INVDT) >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' and IVT_CMPCD=OP_CMPCD and IVT_PRDCD = OP_PRDCD and ivt_stsfl <> 'X'  and isnull(IVT_INVQT,0)>0 where OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(OP_YOSQT,0)+isnull(OP_YOXQT,0)>0  and op_prdcd=pr_prdcd  "; 
			M_strSQLQRY +="  group by pr_prdds,date(IVT_INVDT), IVT_SALTP, IVT_INVNO,IVT_INVQT,IVT_CURCD, IVT_INVRT, IVT_LADRT, ";
			M_strSQLQRY +=" isnull(IVT_ASSVL,0), isnull(IVT_EXCVL,0)+isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0),op_prdcd  order by op_prdcd,IVT_INVDT,IVT_SALTP,IVT_INVNO";*/
			
			
			
			
			System.out.println(M_strSQLQRY);
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			prnHEADER();
			M_rstRSSET.next();
			while(!L_strEOF)
			{
				if(cl_dat.M_intLINNO_pbst >64)
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
				L_strPRDCD1=nvlSTRVL(M_rstRSSET.getString("op_prdcd"),"");
								
				if(!L_strPRDCD1.equals(L_strPRDCD2))
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");
					if(intRECCT>0)
					{
						L_dblTOTQT=Double.parseDouble(setNumberFormat(L_dblTOTQT,3));
						L_dblNETBL=L_dblSTKQT-L_dblTOTQT;
						L_dblGTNEB+=L_dblNETBL;
						//System.out.println("\n L_dblNETBL = " +L_dblNETBL);
						//System.out.println(" KQT = " +L_dblSTKQT);
						//System.out.println(" TQT = " +L_dblTOTQT);
						if(rdbDETAL.isSelected())
							dosREPORT.writeBytes(padSTRING('R',"Total :",20));
						else
						{
							if(L_intSUMRY==0)
							{
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst+= 1;
							}								
							dosREPORT.writeBytes(padSTRING('R',L_strGRADE,11));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSTKQT,3),9));	
						}
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDOQTY,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDOVAL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDODUT,0)),9));
						
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDEQTY,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDEVAL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDEDUT,0)),9));
						
						
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblEXQTY,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblEXVAL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblEXDUT,0)),9));
						
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblCAQTY,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblCAVAL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblCADUT,0)),6));

						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblTRQTY,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblTRVAL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblTRDUT,0)),6));
						
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,3),9));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblNETBL,3),9));
						
						
						L_dblDOQTY=0.0;
						L_dblDOVAL=0.0;
						L_dblDODUT=0.0;
						L_dblDEQTY=0.0;
						L_dblDEVAL=0.0;
						L_dblDEDUT=0.0;
				        L_dblEXQTY=0.0;
						L_dblEXVAL=0.0;
						L_dblEXDUT=0.0;
						L_dblCAQTY=0.0;
						L_dblCAVAL=0.0;
						L_dblCADUT=0.0;
						L_dblTRQTY=0.0;
						L_dblTRVAL=0.0;
						L_dblTRDUT=0.0;
						L_dblTOTQT=0.0;
						L_dblNETBL=0.0;
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst+= 1;
						
					}
					L_dblSTKQT= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("OP_YOSQT"),"0.0"));
					L_dblGTSTQ+=L_dblSTKQT;
					L_strGRADE=nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
					if(rdbDETAL.isSelected())
					{
						if(intRECCT>0)
						{
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst+= 1;
						}
						//L_dblSTKQT+=0.000;
						dosREPORT.writeBytes(padSTRING('R',"Grade "+L_strGRADE,40));
						dosREPORT.writeBytes(padSTRING('R',"Opening Stock "+setNumberFormat(L_dblSTKQT,3),22));	
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst+= 2;
					}
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");
					L_dblBALQT=L_dblSTKQT;
					
				}
				if(rdbDETAL.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),10));
					L_datTMPDT =M_rstRSSET.getDate("IVT_INVDT");
					if(L_datTMPDT !=null)
					{
						L_strDATE=M_fmtLCDAT.format(L_datTMPDT).toString();
						dosREPORT.writeBytes(padSTRING('R',L_strDATE,10));
					}
				
				}
				L_strSALTP=nvlSTRVL(M_rstRSSET.getString("IVT_SALTP"),"");
				L_strCURCD=nvlSTRVL(M_rstRSSET.getString("IVT_CURCD"),"");
				//System.out.println("L_strCURCD22222"+L_strCURCD);
				L_dblINVQT= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
				L_dblLADRT= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_LADRT"),"0"));
				L_dblINVRT= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVRT"),"0"));
				L_dblDUTVL= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_DUTVL"),"0"));
					
				if(!L_strCURCD.equals("01"))
					L_dblASSVL=L_dblINVQT*L_dblINVRT;
				else
					L_dblASSVL=L_dblINVQT*L_dblLADRT;
				double L_dblASSVL1=L_dblASSVL;
				//System.out.println("L_dblASSVL"+L_dblASSVL);
				L_dblINVQT1=L_dblINVQT;
				L_dblBALQT=L_dblBALQT-L_dblINVQT;
				if(L_dblBALQT<0)
				{
					L_dblINVQT1=L_dblINVQT+L_dblBALQT;
					L_dblBALQT=0;
				}
				//L_dblGTNEB+=L_dblBALQT;
				//System.out.println(L_dblGTNEB);
				if(L_dblINVQT>L_dblINVQT1)
				{
					L_dblASSVL=L_dblASSVL*L_dblINVQT1/L_dblINVQT;
					L_dblDUTVL=L_dblDUTVL*L_dblINVQT1/L_dblINVQT;
				}
				
				if(L_strSALTP.equals("01")||L_strSALTP.equals("05"))
				{
					L_dblDOQTY+=L_dblINVQT1;
					L_dblDOVAL+=L_dblASSVL;
					L_dblDODUT+=L_dblDUTVL;
					
					L_dblGTDOQ+= L_dblINVQT1;
					L_dblGTDOD+= L_dblDUTVL;
					if(rdbDETAL.isSelected())
					{
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblINVQT1,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblASSVL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDUTVL,0)),9));
						
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));
					}
				}
				if(L_strSALTP.equals("03"))
				{
					L_dblDEQTY+=L_dblINVQT1;
					L_dblDEVAL+=L_dblASSVL;
					L_dblDEDUT+=L_dblDUTVL;

					L_dblGTDEQ+= L_dblINVQT1;
					L_dblGTDED+= L_dblDUTVL;
					if(rdbDETAL.isSelected())
					{
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblINVQT1,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblASSVL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDUTVL,0)),9));
						
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));
					}
				}
				if(L_strSALTP.equals("12"))
				{
					L_dblEXQTY+=L_dblINVQT1;
					L_dblEXVAL+=L_dblASSVL;
					L_dblEXDUT+=L_dblDUTVL;

					L_dblGTEXQ+= L_dblINVQT1;
					L_dblGTEXD+= L_dblDUTVL;
					if(rdbDETAL.isSelected())
					{	
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblINVQT1,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblASSVL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDUTVL,0)),9));
					
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));
					}
				}
				if(L_strSALTP.equals("04"))
				{
					L_dblTRQTY+=L_dblINVQT1;
					L_dblTRVAL+=L_dblASSVL;
					L_dblTRDUT+=L_dblDUTVL;

					L_dblGTTRQ+= L_dblINVQT1;
					L_dblGTTRD+= L_dblDUTVL;
					if(rdbDETAL.isSelected())
					{	
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

					
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblINVQT1,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblASSVL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDUTVL,0)),9));
					}
				}
				if(L_strSALTP.equals("16"))
				{
					L_dblCAQTY+=L_dblINVQT1;
					L_dblCAVAL+=L_dblASSVL;
					L_dblCADUT+=L_dblDUTVL;

					L_dblGTCAQ+= L_dblINVQT1;
					L_dblGTCAD+= L_dblDUTVL;
					if(rdbDETAL.isSelected())
					{
						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblINVQT1,3)),9));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblASSVL,0)),10));
						dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDUTVL,0)),9));

						dosREPORT.writeBytes(padSTRING('L',"-",9));
						dosREPORT.writeBytes(padSTRING('L',"-",10));
						dosREPORT.writeBytes(padSTRING('L',"-",9));

					}
				}
				
				L_dblTOTQT+=L_dblINVQT1;
				if(rdbDETAL.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblINVQT1,3),9));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBALQT,3),9));
				}
					L_strPRDCD2=L_strPRDCD1;
				if(L_dblBALQT==0)
				{
					dosREPORT.writeBytes("\n");	
					cl_dat.M_intLINNO_pbst++;
					L_intSUMRY=1;
					while(L_strPRDCD1.equals(L_strPRDCD2) && !L_strEOF)
					{
						if (!M_rstRSSET.next())
						{
							L_strEOF = true;
							break;
						}
						L_strPRDCD1=nvlSTRVL(M_rstRSSET.getString("op_prdcd"),"");
					}
				}
				else
				{
					L_intSUMRY=0;
					if (!M_rstRSSET.next())
					{
						L_strEOF = true;
						break;
					}
				}
				if(rdbDETAL.isSelected())
				{
					dosREPORT.writeBytes("\n");	
					cl_dat.M_intLINNO_pbst++;
				}
				intRECCT++;
			}
			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			if(intRECCT>0)
			{
						
				L_dblNETBL=L_dblSTKQT-L_dblTOTQT;
				//L_dblGTNEB+=L_dblNETBL;
				if(rdbDETAL.isSelected())
				{
					dosREPORT.writeBytes("\n");	
					cl_dat.M_intLINNO_pbst++;
					dosREPORT.writeBytes(padSTRING('R',"Total :",20));
					
				}
				else
				{
					dosREPORT.writeBytes(padSTRING('R',L_strGRADE,11));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSTKQT,3),9));	
					//L_dblGTNEB+=L_dblNETBL;
					//System.out.println(L_dblGTNEB);
				}
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDOQTY,3)),9));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDOVAL,0)),10));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDODUT,0)),9));
						
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDEQTY,3)),9));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDEVAL,0)),10));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblDEDUT,0)),9));
						
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblEXQTY,3)),9));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblEXVAL,0)),10));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblEXDUT,0)),9));
						
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblCAQTY,3)),9));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblCAVAL,0)),9));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblCADUT,0)),6));
						
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblTRQTY,3)),9));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblTRVAL,0)),9));
				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblTRDUT,0)),6));

				dosREPORT.writeBytes(padSTRING('L',getDASH(setNumberFormat(L_dblTOTQT,3)),9));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblNETBL,3),9));
				
				L_dblDOQTY=0.0;
				L_dblDOVAL=0.0;
				L_dblDODUT=0.0;
				L_dblDEQTY=0.0;
				L_dblDEVAL=0.0;
				L_dblDEDUT=0.0;
			    L_dblEXQTY=0.0;
				L_dblEXVAL=0.0;
				L_dblEXDUT=0.0;
				L_dblCAQTY=0.0;
				L_dblCAVAL=0.0;
				L_dblCADUT=0.0;
				L_dblTRQTY=0.0;
				L_dblTRVAL=0.0;
				L_dblTRDUT=0.0;
				L_dblTOTQT=0.0;
				//L_dblNETBL=0.0;
				dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
						
			}
				dosREPORT.writeBytes(padSTRING('L',"",11));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTSTQ,3),9));	
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTDOQ,3),9));
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTDOD,0),9));
				//dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTDEQ,3),9));
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTDED,0),9));
				//dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTEXQ,3),9));
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTEXD,0),9));
				//dosREPORT.writeBytes(padSTRING('L',"",9));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTCAQ,3),9));
				dosREPORT.writeBytes(padSTRING('L',"",7));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTCAD,0),9));
				//dosREPORT.writeBytes(padSTRING('L',"",7));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTTRQ,3),9));
				dosREPORT.writeBytes(padSTRING('L',"",7));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTTRD,0),9));
				dosREPORT.writeBytes(padSTRING('L',"",9));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTNEB,3),9));
				dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
			
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	public String getDASH(String P_strVALUE)
	{
		if(P_strVALUE.equals(null)||P_strVALUE.equals("0.000")||P_strVALUE.equals("0"))
		{
			return "-";
		}
		else
			return P_strVALUE;
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
			//dosREPORT.writeBytes(padSTRING('R',"Sales Details",strDOTLN.length())+"\n");
			if(rdbDETAL.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Sales Details" ,strDOTLN.length()-22));
			else
				dosREPORT.writeBytes(padSTRING('R',"Sales Summary" ,strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Report Date:"+(cl_dat.M_strLOGDT_pbst),22)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Gradewise Saletypewise despatch detail from Year opening Stock of :"+txtYSDAT.getText().trim(),strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Page No    :"+cl_dat.M_PAGENO ,22)+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			//cl_dat.M_intLINNO_pbst +=4;
			if(rdbDETAL.isSelected())
			{
				dosREPORT.writeBytes(strDOTLN +"\n");	
				dosREPORT.writeBytes("Inv No.   Date       <---------Domestic-------->    <-------D/Exp--------->    <------Export--------->    <------Captive------->      <----S.Transfer---->    Total  Balance \n");
				dosREPORT.writeBytes("                          Qty    As.Val     Duty      Qty    As.Val     Duty      Qty    As.Val     Duty      Qty   As.Val  Duty       Qty    As.Val  Duty \n");
			}
			else
			{
				dosREPORT.writeBytes(strDOTLN +"\n");	
				dosREPORT.writeBytes("Grade        Ope Stk  <--------Domestic-------->    <-------D/Exp--------->    <------Export--------->    <------Captive------->      <----S.Transfer---->    Total  Balance \n");
				dosREPORT.writeBytes("                          Qty    As.Val     Duty      Qty    As.Val     Duty      Qty    As.Val     Duty      Qty   As.Val  Duty       Qty    As.Val  Duty \n");
			}
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
			cl_dat.M_intLINNO_pbst += 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
			
		}
	}
}