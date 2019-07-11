
/*
System Name   : Marketing System
Program Name  : Payment OutStanding
Program Desc. : 
Author        : Mr. Zaheer Khan
Date          : 21/07/2006"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :26/07/2006
Modified det.  :
Version        :
*/

import java.sql.ResultSet;import java.util.Hashtable;
import java.awt.event.KeyEvent;import javax.swing.JComponent;
import javax.swing.JComboBox; import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.sql.Date;
import java.util.*;
						

class mr_rppmo extends cl_rbase
{			
	private JComboBox cmbRPTTP;
	private JComboBox cmbDSTTP;
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;		 
	private JTextField txtDSRCD;		 
	private JTextField txtDSRNM;
	private JTextField txtTODAT;
	private JTextField txtODDAY;
	private ButtonGroup btgRPTTP;    
	private JRadioButton rdbSUMRY;	 
	private JRadioButton rdbDETAL;	      /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			   /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				   /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ;   /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ; // report width (Detail)
	private int intRPTDWD = 102;            // report width (Summary)
	private int intRPTSWD = 158;
	private String strPPRTY="";
	private String strNPRTY="";
	private String strNCNSD="";
	private String strPCNSD="";
	private double dblBALAM=0;
	private double dblTOTAL=0;
	private int intDEBIT=0;
	private int intCREDT=0;
	private int intODDAY=0;
	private String strTODAT;
	private Hashtable<String,String> hstDSRCD;
	public mr_rppmo()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			cmbRPTTP = new JComboBox();
			cmbDSTTP = new JComboBox();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			btgRPTTP=new ButtonGroup();
					
			add(rdbDETAL=new JRadioButton("Details Report",true),2,3,1,2,this,'L');
			add(rdbSUMRY=new JRadioButton("Summary Report"),2,5,1,2,this,'L');
			
			btgRPTTP.add(rdbDETAL);
			btgRPTTP.add(rdbSUMRY);
			
			cmbRPTTP.addItem("Outstanding payment");
			cmbRPTTP.addItem("Overdue payment");
			
			
			add(new JLabel("Report Type"),3,3,1,1,this,'L');
			add(cmbRPTTP,3,4,1,2,this,'L');
			add(new JLabel("Dist Type"),4,3,1,1,this,'L');
			cmbDSTTP.addItem("D");
			cmbDSTTP.addItem("G");
			add(cmbDSTTP,4,4,1,1,this,'L');
			add(new JLabel("Dist Code"),5,3,1,1,this,'L');
			add(txtDSRCD=new TxtLimit(5),5,4,1,1,this,'L');
			add(new JLabel("Dist Name"),6,3,1,1,this,'L');
			add(txtDSRNM=new TxtLimit(30),6,4,1,3,this,'L');
			add(new JLabel("Party Code"),7,3,1,1,this,'L');
			add(txtPRTCD=new TxtLimit(5),7,4,1,1,this,'L');
			add(new JLabel("Party Name"),8,3,1,1,this,'L');
			add(txtPRTNM=new TxtLimit(30),8,4,1,3,this,'L');

			add(new JLabel("Overdue on"),9,3,1,1,this,'L');
			add(txtTODAT=new TxtDate(),9,4,1,1,this,'L');

			add(new JLabel("Overdue by(days)"),10,3,1,1,this,'L');
			add(txtODDAY=new TxtNumLimit(3),10,4,1,1,this,'L');
			
			hstDSRCD = new Hashtable<String,String>(50);
			M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP in('C','G')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstDSRCD = new Hashtable<String,String>(50);
			if(M_rstRSSET !=null)
			{
			    while(M_rstRSSET.next())
			        hstDSRCD.put(M_rstRSSET.getString("PT_PRTCD"),M_rstRSSET.getString("PT_PRTNM"));
			}
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
		txtDSRNM.setEnabled(false);
		txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText());
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))||(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst)))
			{
				M_cmbDESTN.requestFocus();
			}
		}	
		if(rdbSUMRY.isSelected())
		{
			txtPRTCD.setText("");
			txtPRTNM.setText("");
			txtPRTCD.setEnabled(false);
		}
		else
			txtPRTCD.setEnabled(true);

		if(M_objSOURC==cmbRPTTP)
		{
			txtDSRCD.setText("");
			txtDSRNM.setText("");
			txtPRTCD.setText("");
			txtPRTNM.setText("");
		}
		
	}
	
	public boolean vldDATA()
	{
		try
		{
			if(rdbDETAL.isSelected())
			{	
				if(txtDSRCD.getText().trim().length()<5)
				{
					setMSG("Please Enter  Distributor Code OR press f1 for help..",'E');
					txtDSRCD.requestFocus();
					return false;
				}
			}
			if(rdbSUMRY.isSelected())
			{
				if(txtODDAY.getText().trim().length()==0)
				{
					setMSG("Please Enter  OverDue Days",'E');
					txtODDAY.requestFocus();
					return false;
					
				}
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
			if(M_objSOURC==txtDSRCD)
			{
				M_strHLPFLD="txtDSRCD";
				M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where ";
				M_strSQLQRY += " PT_PRTTP='"+cmbDSTTP.getSelectedItem().toString().substring(0,1)+"' AND isnull(PT_STSFL,'')<> 'X'";
				if(txtDSRCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtDSRCD.getText().trim().toUpperCase()+"%'"; 
				}
					M_strSQLQRY +="Order by PT_PRTNM";
					//System.out.println("Distributer "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Dist Code","Dist Name"},2,"CT");
			}
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD = "txtPRTCD";
				M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD  AND isnull(PT_STSFL,'')<> 'X' and PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
				if(cmbDSTTP.getSelectedItem().toString().substring(0,1).equals("D"))
				{
					if(txtDSRCD.getText().trim().length()==5)
					{
						if(!txtDSRCD.getText().trim().substring(1,5).equals("8888"))
						{
							M_strSQLQRY += " AND PT_DSRCD='"+txtDSRCD.getText().trim()+"'";					
						}
					}
				}
				else
				{
					if(txtDSRCD.getText().trim().length()==5)
					{
						if(!txtDSRCD.getText().trim().substring(1,5).equals("8888"))
						{
							M_strSQLQRY += " AND PT_CNSRF='"+txtDSRCD.getText().trim()+"'";					
						}
					}
					
				}
				if(txtPRTCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtPRTCD.getText().trim().toUpperCase()+"%'"; 
				}
				M_strSQLQRY += " order by  PT_PRTNM";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT");
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC==txtDSRCD)
				{
					txtDSRCD.setText(txtDSRCD.getText().trim().toUpperCase());	
					if(txtDSRCD.getText().trim().length()==5)
					{
						M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where ";
						M_strSQLQRY += " PT_PRTTP='"+cmbDSTTP.getSelectedItem().toString().substring(0,1)+"' AND isnull(PT_STSFL,'')<> 'X'";
						M_strSQLQRY += "AND PT_PRTCD = '"+txtDSRCD.getText().trim().toUpperCase()+"'"; 
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
						if(M_rstRSSET!=null &&M_rstRSSET.next())
						{
							txtDSRNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  "));
							if(rdbSUMRY.isSelected())
								txtTODAT.requestFocus();
								//cl_dat.M_btnSAVE_pbst.requestFocus();							
							else
								txtPRTCD.requestFocus();
						}
						else
						{
							setMSG("Enter Proper Distributer Code Or Press F1 For Help..",'E');
							txtDSRNM.setText("");
							txtDSRCD.requestFocus();
						}				
						if(M_rstRSSET != null)
							M_rstRSSET.close();
					}
					else if(txtDSRCD.getText().trim().length()>0 && txtDSRCD.getText().trim().length()<5)
					{
						setMSG("Enter Proper Distributer Code Or Press F1 For Help..",'E');
						txtDSRNM.setText("");
						txtDSRCD.requestFocus();
					}				
				}
				if(M_objSOURC == txtPRTCD)
				{
					txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());	
					if(txtPRTCD.getText().trim().length()==5)
					{
						M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD  AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PT_STSFL,'')<> 'X'";
						M_strSQLQRY += "AND PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'"; 
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
						if(M_rstRSSET!=null &&M_rstRSSET.next())
						{
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  "));
							txtTODAT.requestFocus();
							//cl_dat.M_btnSAVE_pbst.requestFocus();
						}
						else
						{
							setMSG("Enter Proper Party Code Or Press F1 For Help..",'E');
							txtPRTNM.setText("");
							txtPRTCD.requestFocus();
						}				
						if(M_rstRSSET != null)
							M_rstRSSET.close();
					}
					else if(txtPRTCD.getText().trim().length()>0 && txtPRTCD.getText().trim().length()<5)
					{
						setMSG("Enter Proper Party Code Or Press F1 For Help..",'E');
						txtPRTNM.setText("");
						txtPRTCD.requestFocus();
					}
					else
					{
						txtPRTNM.setText("");
						txtPRTCD.setText("");
						txtTODAT.requestFocus();
					}
				}
				if(M_objSOURC == txtTODAT)
				{	
					strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()));
					txtODDAY.requestFocus();
					//cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				if(M_objSOURC == txtODDAY)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			catch(Exception E_KP)
			{
				setMSG(E_KP,"getKEPRESS");
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtDSRCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtDSRCD.setText(L_STRTKN1.nextToken());
				txtDSRNM.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtPRTCD.setText(L_STRTKN1.nextToken());
				txtPRTNM.setText(L_STRTKN1.nextToken());
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rppmo.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rppmo.doc";
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
			//cl_dat.M_PAGENO =1;
						
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Payment OutStanding </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			if(rdbDETAL.isSelected())
			{
				getDETAL();
			}
			if(rdbSUMRY.isSelected())
			{
				getSUMRY();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setMSG("",'N'); 
	}
	
	private void getDETAL()
	{
		try
		{
			Date L_datTMPDT;
			double L_dblBALAM=0.0;
			String L_strDOCDS="";
			intRECCT=0;
			double L_dblDEBIT=0.0;
			double L_dblCREDT=0.0;
			int intPRCNT=0;
			strNPRTY="";
			strPPRTY="";
			dblBALAM=0.0;
			dblTOTAL=0.0;
			intCREDT=0;
			intDEBIT=0;
			String L_strDOCTP="";
			String L_strBALAM="";
			cl_dat.M_PAGENO=0;	
			//boolean L_flgRSSET=false;
			//04/08/2006
						
			String L_strDATE = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()));
			//System.out.println("AFter Conversion "+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText())));
			
			
			/*
			
SELECT PL_PRTTP,PL_PRTCD,PT_PRTNM,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DO
CVL,(DAYS(Date('09/27/2006'))-DAYS(PL_DUEDT)) PENDAYS,  (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) 
BALAM, PL_ACCRF 
			
			FROM MR_PLTRN,CO_PTMST 
			
		WHERE (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) >0 AND PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND  (PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE  '3%') 
			
AND PL_PRTTP||PL_PRTCD in(select PT_PRTTP||PT_PRTCD from CO_PTMST where (PT_PRTTP='C' AND PT_DSRCD='D0006')OR (PT_PRTTP='D' AND PT_PRTCD='D0006' ))
			
			Union SELECT  PL_PRTTP,PL_PRTCD,PT_PRTNM,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DO CVL,(DAYS(date('09/27/2006'))-DAYS(PL_DUEDT)) PENDAYS,  (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0))  BALAM, PL_ACCRF FROM MR_PLTRN,CO_PTMST WHERE (isnull(PL_DOCVL,0) -  isnull(PL_ADJVL,0)) >0 AND PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND  (PL_DOCTP LIKE  '0%' OR  PL_DOCTP LIKE  '1%') AND PL_PRTTP||PL_PRTCD in(select PT_PRTTP||PT_PRTCD from CO_PTMST  where (PT_PRTTP='C' AND PT_DSRCD='D0006') or (PT_PRTTP='D' AND PT_PRTCD='D0006' )) ORDER BY  PL_PRTCD asc, PL_DOCTP desc
			*/
			
			//M_strSQLQRY ="SELECT PL_PRTCD,PT_PRTNM,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DOCVL,(DAYS(Date('"+L_strDATE+"'))-DAYS(PL_DUEDT)) PENDAYS, ";
			//M_strSQLQRY +=" (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) BALAM,PL_ACCRF";
			if(cmbDSTTP.getSelectedItem().toString().substring(0,1).equals("D"))
			{
				M_strSQLQRY ="SELECT PL_PRTTP,PL_PRTCD,PT_PRTNM,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DOCVL,";
				M_strSQLQRY +=" (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT)) PENDAYS,  (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) BALAM, PL_ACCRF";
				M_strSQLQRY +="  FROM MR_PLTRN,CO_PTMST WHERE (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) >0 AND PL_PRTTP='C' and PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND ";
				M_strSQLQRY +=" PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE  '3%')";
				if(cmbRPTTP.getSelectedIndex()==1)
				{
					M_strSQLQRY +="AND (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT))>0 ";
				}
				if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
					M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
				if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
					M_strSQLQRY +="AND PL_PRTTP + PL_PRTCD in(select PT_PRTTP + PT_PRTCD from CO_PTMST where (PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')OR (PT_PRTTP='D' AND PT_PRTCD='"+txtDSRCD.getText().trim()+"' ))";
			
				//M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
				//M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
			
				M_strSQLQRY += "Union ";
				M_strSQLQRY +="SELECT PL_PRTTP,PL_PRTCD,PT_PRTNM,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DOCVL,";
				M_strSQLQRY +=" (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT)) PENDAYS,  (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) BALAM, PL_ACCRF";
				M_strSQLQRY +="  FROM MR_PLTRN,CO_PTMST WHERE (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) >0 AND PL_PRTTP='C' and PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND ";
				M_strSQLQRY +=" PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (PL_DOCTP LIKE  '0%' OR PL_DOCTP LIKE  '1%')";
				if(cmbRPTTP.getSelectedIndex()==1)
				{
					M_strSQLQRY +="AND (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT))>0 ";
				}
				if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
					M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
				if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
					M_strSQLQRY +="AND PL_PRTTP + PL_PRTCD in(select PT_PRTTP + PT_PRTCD from CO_PTMST where (PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')OR (PT_PRTTP='D' AND PT_PRTCD='"+txtDSRCD.getText().trim()+"' ))";
				M_strSQLQRY +=" ORDER BY PL_PRTTP asc, PL_PRTCD asc, PL_DOCTP desc";
				System.out.println(M_strSQLQRY);
			}
			else
			{
				
				
				M_strSQLQRY ="SELECT PL_PRTTP,PL_PRTCD,PL_CNSCD,PT_PRTNM,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DOCVL,";
				M_strSQLQRY +=" (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT)) PENDAYS,  (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) BALAM, PL_ACCRF";
				M_strSQLQRY +="  FROM MR_PLTRN,CO_PTMST WHERE (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) >0 AND PL_PRTTP IN('C','"+cmbDSTTP.getSelectedItem().toString().substring(0,1)+"') and PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND ";
				M_strSQLQRY +=" PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE  '3%')";
				if(cmbRPTTP.getSelectedIndex()==1)
				{
					M_strSQLQRY +="AND (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT))>0 ";
				}
				if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
					M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
				if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
					M_strSQLQRY +="AND PL_PRTTP + PL_PRTCD in(select PT_PRTTP + PT_PRTCD from CO_PTMST where (PT_PRTTP='C' AND PT_CNSRF='"+txtDSRCD.getText().trim()+"')OR (PT_PRTTP='G' AND PT_PRTCD='"+txtDSRCD.getText().trim()+"' ))";
				//if(cmbDSTTP.getSelectedItem().toString().substring(0,1).equals("D"))
				//	M_strSQLQRY +=" (PT_PRTTP='D' AND PT_PRTCD='"+txtDSRCD.getText().trim()+"' ))";
				//else
				//	M_strSQLQRY +=" (PT_PRTTP='G' AND PT_CNSRF='"+txtDSRCD.getText().trim()+"' ))";
				
			
				//M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
				//M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
			
				M_strSQLQRY += "Union ";
				M_strSQLQRY +="SELECT PL_PRTTP,PL_PRTCD,PL_CNSCD,PT_PRTNM,SUBSTRING(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DOCVL,";
				M_strSQLQRY +=" (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT)) PENDAYS,  (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) BALAM, PL_ACCRF";
				M_strSQLQRY +="  FROM MR_PLTRN,CO_PTMST WHERE (isnull(PL_DOCVL,0) - isnull(PL_ADJVL,0)) >0 AND PL_PRTTP IN('C','"+cmbDSTTP.getSelectedItem().toString().substring(0,1)+"') and PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND ";
				M_strSQLQRY +=" PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (PL_DOCTP LIKE  '0%' OR PL_DOCTP LIKE  '1%')";
				if(cmbRPTTP.getSelectedIndex()==1)
				{
					M_strSQLQRY +="AND (DAY(CONVERT(varchar,'"+L_strDATE+"',101))-DAY(PL_DUEDT))>0 ";
				}
				if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
					M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
				if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
				{
					M_strSQLQRY +="AND PL_PRTTP + PL_PRTCD in(select PT_PRTTP + PT_PRTCD from CO_PTMST where (PT_PRTTP='C' AND PT_CNSRF='"+txtDSRCD.getText().trim()+"')OR (PT_PRTTP='G' AND PT_PRTCD='"+txtDSRCD.getText().trim()+"' ))";
				
				}
				M_strSQLQRY +=" ORDER BY PL_PRTTP asc, PL_PRTCD asc,PL_CNSCD  asc, PL_DOCTP desc";
				System.out.println(M_strSQLQRY);
				//System.out.println("GGGG =   ");
			}
			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			prnHEADER();
			while(M_rstRSSET.next())
			{
				if(cl_dat.M_intLINNO_pbst >64)
				{
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(crtLINE(intRPTDWD,"-")+"\n");
					cl_dat.M_intLINNO_pbst = 0;
					//cl_dat.M_PAGENO +=1;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				strNPRTY=nvlSTRVL(M_rstRSSET.getString("PL_PRTCD"),"");
				if(cmbDSTTP.getSelectedItem().toString().substring(0,1).equals("G"))
				{
					strNCNSD=nvlSTRVL(M_rstRSSET.getString("PL_CNSCD"),"");
					//System.out.println(" strNCNSD "+strNCNSD);
				}
				
				L_strDOCTP=nvlSTRVL(M_rstRSSET.getString("PL_DOCTP"),"");
				if((!L_strDOCTP.equals("2")&& !L_strDOCTP.equals("3"))||(!strNPRTY.equals(strPPRTY)))
				{
					if(intDEBIT>0)
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						
						dosREPORT.writeBytes(padSTRING('L',"Total :",71));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDEBIT,0),15));
						dblTOTAL+=L_dblDEBIT;
						
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						dosREPORT.writeBytes("\n");	
						cl_dat.M_intLINNO_pbst += 1;
						intDEBIT=0;
						intCREDT=0;
					}
				}
				if((!L_strDOCTP.equals("0")&& !L_strDOCTP.equals("1"))||(!strNPRTY.equals(strPPRTY)))
				{
					if(intCREDT>0)
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						dosREPORT.writeBytes(padSTRING('L',"Total Unadjusted :",71));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCREDT,0),15));
						dosREPORT.writeBytes("\n");
						dblTOTAL -=L_dblCREDT;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						cl_dat.M_intLINNO_pbst += 1;
						intCREDT=0;
						intDEBIT=0;
					}
				}
				
				
				if(cmbDSTTP.getSelectedItem().toString().substring(0,1).equals("G"))
				{
					if(!strNPRTY.equals(strPPRTY)||!strNCNSD.equals(strPCNSD))
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						if(intRECCT>0)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('L',"Balance Amount :",71));
							dblBALAM=L_dblDEBIT-L_dblCREDT;
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblBALAM,0),15));
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("\n");
							intCREDT=0;
							intDEBIT=0;
							cl_dat.M_intLINNO_pbst += 3;
						}
						if(intRECCT==0)
						{
							dosREPORT.writeBytes(padSTRING('R',txtDSRNM.getText().trim()+" ("+txtDSRCD.getText().trim()+")",60));
							dosREPORT.writeBytes("\n");
						}
							
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTNM")+" ("+M_rstRSSET.getString("PL_PRTTP")+"/"+strNPRTY+")",60));
						if(strNCNSD.length()==6)
						{
							if(hstDSRCD.containsKey(strNCNSD.substring(1,6)))
								dosREPORT.writeBytes(padSTRING('R',hstDSRCD.get(strNCNSD.substring(1,6)).toString()+" "+strNCNSD+")",44));
							else
								dosREPORT.writeBytes(padSTRING('R'," "+strNCNSD+")",44));
						}
			
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
					
						dosREPORT.writeBytes("\n\n");
						dblBALAM=0.0;
						L_dblBALAM=0;
						L_dblCREDT=0;
						L_dblDEBIT=0;
						intPRCNT++;
						cl_dat.M_intLINNO_pbst += 2;
						strPCNSD=strNCNSD;
					}

				}
				else
				{
					if(!strNPRTY.equals(strPPRTY))
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						if(intRECCT>0)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('L',"Balance Amount :",71));
							dblBALAM=L_dblDEBIT-L_dblCREDT;
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblBALAM,0),15));
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("\n");
							intCREDT=0;
							intDEBIT=0;
							cl_dat.M_intLINNO_pbst += 3;
						}
						if(intRECCT==0)
						{
							dosREPORT.writeBytes(padSTRING('R',txtDSRNM.getText().trim()+" ("+txtDSRCD.getText().trim()+")",60));
							dosREPORT.writeBytes("\n");
						}
						
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTNM")+" ("+M_rstRSSET.getString("PL_PRTTP")+"/"+strNPRTY+")",60));
			
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
					
						dosREPORT.writeBytes("\n\n");
						dblBALAM=0.0;
						L_dblBALAM=0;
						L_dblCREDT=0;
						L_dblDEBIT=0;
						intPRCNT++;
						cl_dat.M_intLINNO_pbst += 2;
					}
				}		
				L_dblBALAM=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("BALAM"),"0"));

				if(L_strDOCTP.equals("2"))
				{
					 L_strDOCDS = "Inv";
					 L_dblDEBIT +=L_dblBALAM;
					 intDEBIT++;
				}
				else if(L_strDOCTP.equals("3"))
				{
					 L_strDOCDS = "DB(I)";
					  L_dblDEBIT +=L_dblBALAM;
					  intDEBIT++;
				}
				if(L_strDOCTP.equals("0"))
				{
				    L_strDOCDS = "CRN";
					L_dblCREDT +=L_dblBALAM;
					intCREDT++;
				}
				else if(L_strDOCTP.equals("1"))
				{
				    L_strDOCDS = "RCT"; 
					L_dblCREDT +=L_dblBALAM;
					intCREDT++;
				}
				dosREPORT.writeBytes(padSTRING('R',L_strDOCDS,7));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PL_DOCNO"),""),12));
				L_datTMPDT =M_rstRSSET.getDate("PL_DOCDT");
				//System.out.println("DAte "+L_datTMPDT);
				if(L_datTMPDT !=null)
				{
					dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),13));	
				}
				else
				dosREPORT.writeBytes(padSTRING('R'," ",13));	
				L_datTMPDT =M_rstRSSET.getDate("PL_DUEDT");
				if(L_datTMPDT !=null)
				{
					dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),13));	
				}
				else
				dosREPORT.writeBytes(padSTRING('R'," ",13));	
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PENDAYS"),""),11));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),"0")),0),15));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBALAM,0),15));
				dosREPORT.writeBytes(padSTRING('R'," ",6));	
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PL_ACCRF"),""),15));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				intRECCT++;
				strPPRTY=strNPRTY;
			}
			dosREPORT.writeBytes("\n");	
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			
			if(intDEBIT>0)
			{
				dosREPORT.writeBytes(padSTRING('L',"Total :",71));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDEBIT,0),15));
				dblTOTAL+=L_dblDEBIT;
			}
			
			if(intCREDT>0)
			{
				dosREPORT.writeBytes(padSTRING('L',"Total Unadjusted :",71));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCREDT,0),15));
				dblTOTAL-=L_dblCREDT;
			}
			dosREPORT.writeBytes("\n");
			dblBALAM=L_dblDEBIT-L_dblCREDT;
			dosREPORT.writeBytes(padSTRING('L',"Balance Amount :",71));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblBALAM,0),15));
			dosREPORT.writeBytes("\n\n");
			if(intPRCNT>1)
			{
				dosREPORT.writeBytes(padSTRING('L'," Net Balance ",67));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblTOTAL,0),19));
				dosREPORT.writeBytes("\n");
			}
			dosREPORT.writeBytes("\n"+crtLINE(intRPTDWD,"-")+"\n");
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
	
	/** FOR SUMMARY REPORT (BY SATYA) */
	
	private void getSUMRY()
	{ 	    		
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		String L_strPRTYOPFL = "";
		String L_strPPRTYOPFL = "";
		String L_strDOCTP = "";
		String L_strDOCTP1 = "";
		String L_strDOCNO = "";
		String L_strDUEDT = "";
		String L_strDUEDT1 = "";
		int L_intPRTCNT = 1;
		double L_dblDOCVL = 0;
		double L_dblADJVL = 0;
		//double L_dblYOPVL = 0;
		double L_dblBALVL = 0;

		double L_dblPRTOSVL = 0;
		double L_dblPRTDBVL = 0;
		double L_dblPRTCRVL = 0;
		double L_dblPRTODVL = 0;
		double L_dblPRTPRODVL = 0;
		double L_dblPRTYOPVL = 0;
		double L_dblPPRTYOPVL = 0;
		
		double L_dblDSROSVL = 0;
		double L_dblDSRDBVL = 0;
		double L_dblDSRCRVL = 0;
		double L_dblDSRODVL = 0;
		double L_dblDSRPRODVL = 0;
		double L_dblDSRYOPVL = 0;

		double L_dblZONOSVL = 0;
		double L_dblZONDBVL = 0;
		double L_dblZONCRVL = 0;
		double L_dblZONODVL = 0;
		double L_dblZONPRODVL = 0;
		double L_dblZONYOPVL = 0;
		
		intODDAY = Integer.parseInt(txtODDAY.getText());

		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPPRTNM = new String();
		String L_strPDSRNM = new String();
		String L_strPZONDS = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCPRTNM = new String();
		String L_strCDSRNM = new String();
		String L_strCZONDS = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strPRTNM = new String();
		String L_strDSRNM = new String();
		String L_strZONDS = new String();
		
		try
	    {	
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Statement </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();
			setMSG("Report Generation is in Progress.......",'N');

			//M_strSQLQRY = " SELECT  B.PT_ZONCD,C.CMT_CODDS ZONDS,A.PT_DSRCD,B.PT_PRTNM DSRNM,PL_PRTCD,A.PT_PRTNM PRTNM,PL_DOCTP,";
			//M_strSQLQRY+= " PL_DOCNO,D.CMT_CHP01 DOCTP1, A.PT_YOPVL PRTYOPVL,A.PT_YOPFL PRTYOPFL,PL_DOCVL,isnull(PL_ADJVL,0) PL_ADJVL,PL_DOCVL-isnull(PL_ADJVL,0) BALVL,PL_DUEDT ";
			//M_strSQLQRY+= "	FROM MR_PLTRN, CO_PTMST A,CO_PTMST B,CO_CDTRN C, ";
			//M_strSQLQRY+= " CO_CDTRN D WHERE PL_PRTTP=A.PT_PRTTP AND  PL_PRTCD=A.PT_PRTCD AND B.PT_PRTTP='D' ";
			//M_strSQLQRY+= " AND A.PT_DSRCD=B.PT_PRTCD  AND PL_STSFL != 'X'  AND A.PT_DSRCD!= 'X8888'  AND ";
			//M_strSQLQRY+= " C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MRXXZON' AND B.PT_ZONCD=C.CMT_CODCD  AND D.CMT_CGMTP='DOC'";
			//M_strSQLQRY+= " AND D.CMT_CGSTP='MRXXPTT' AND SUBSTRING(D.CMT_CODCD,2,2)=PL_DOCTP ";
			//M_strSQLQRY+= " AND ((PL_DOCVL-isnull(PL_ADJVL,0)) > 0 OR A.PT_YOPVL > 0) AND (PL_DOCDT >= '01/07/2006' OR SUBSTRING(PL_DOCNO,4,5)='77777')";
			//M_strSQLQRY+= " AND isnull(PL_ADJVL,0) <= PL_DOCVL AND LENGTH(TRIM(isnull(PL_DOCNO,''))) = 8" ; 

			M_strSQLQRY = " SELECT  B.PT_ZONCD,C.CMT_CODDS ZONDS,A.PT_DSRCD,B.PT_PRTNM DSRNM,PL_PRTCD,A.PT_PRTNM PRTNM,PL_DOCTP,SUBSTRING(PL_DOCNO,2,2) PL_DOCTP2,";
			M_strSQLQRY+= " PL_DOCNO,D.CMT_CHP01 DOCTP1, A.PT_YOPVL PRTYOPVL,A.PT_YOPFL PRTYOPFL,PL_DOCVL,isnull(PL_ADJVL,0) PL_ADJVL,PL_DOCVL-isnull(PL_ADJVL,0) BALVL,PL_DUEDT ";
			M_strSQLQRY+= " FROM MR_PLTRN, CO_PTMST A,CO_PTMST B,CO_CDTRN C, ";
			M_strSQLQRY+= " CO_CDTRN D WHERE PL_PRTTP = 'C' and PL_PRTTP=A.PT_PRTTP AND  PL_PRTCD=A.PT_PRTCD AND B.PT_PRTTP='D' ";
			M_strSQLQRY+= " AND A.PT_DSRCD=B.PT_PRTCD  AND PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_STSFL != 'X'  AND  PL_SBSCD NOT IN ('111200') AND ";
			M_strSQLQRY+= " C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MRXXZON' AND B.PT_ZONCD=C.CMT_CODCD  AND (D.CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' OR D.CMT_CGMTP = 'SYS')";
			M_strSQLQRY+= " AND ((D.CMT_CGSTP='MRXXPTT' and SUBSTRING(D.CMT_CODCD,1,1) ='"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+"' AND SUBSTRING(D.CMT_CODCD,2,2)=PL_DOCTP) OR (D.CMT_CGSTP='MRXXOCN' AND D.CMT_CODCD=PL_DOCTP))";
			M_strSQLQRY+= " AND ((PL_DOCVL-isnull(PL_ADJVL,0)) > 0 OR A.PT_YOPVL > 0) AND (PL_DOCDT >= '01/07/2006' OR SUBSTRING(PL_DOCNO,4,5)='77777')";
			M_strSQLQRY+= " AND isnull(PL_ADJVL,0) <= PL_DOCVL AND len(rtrim(ltrim(isnull(PL_DOCNO,'')))) = 8" ; 
			
			
			if(txtDSRCD.getText().equals(""))
				M_strSQLQRY+= " ";
			else
				M_strSQLQRY+= " AND A.PT_DSRCD='"+txtDSRCD.getText()+"' ";	
			
			M_strSQLQRY+= " ORDER BY C.CMT_CODDS,B.PT_PRTNM,A.PT_PRTNM,PL_DOCTP ";   
			
			//System.out.println(M_strSQLQRY);			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					L_strZONDS = nvlSTRVL(M_rstRSSET.getString("ZONDS"),"");
					L_strDSRNM = nvlSTRVL(M_rstRSSET.getString("DSRNM"),"");
					L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("PRTNM"),"");
					L_strDOCTP = nvlSTRVL(M_rstRSSET.getString("PL_DOCTP"),"");
					L_strDOCTP1 = nvlSTRVL(M_rstRSSET.getString("DOCTP1"),"");
					if (L_strDOCTP1.equals("DB") && L_strDOCTP.equals("21"))
					{	
						L_strDUEDT =  nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("PL_DUEDT")),"");
						M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strDUEDT)); 
					    M_calLOCAL.add(Calendar.DATE,+intODDAY); 
						L_strDUEDT1 = M_fmtLCDAT.format(M_calLOCAL.getTime()); 
					}	
					L_strDOCNO = nvlSTRVL(M_rstRSSET.getString("PL_DOCNO"),"");
					L_strPRTYOPFL = nvlSTRVL(M_rstRSSET.getString("PRTYOPFL"),"");
	   				L_dblDOCVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),"0"));
	   				L_dblADJVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_ADJVL"),"0"));
	   				L_dblBALVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("BALVL"),"0"));
	   				L_dblPRTYOPVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PRTYOPVL"),"0"));

					if(L_flg1STSFL)
					{
						L_strPZONDS = L_strZONDS;
						L_strCZONDS = L_strZONDS;
						L_strPDSRNM = L_strDSRNM;
						L_strCDSRNM = L_strDSRNM;
						L_strPPRTNM = L_strPRTNM;
						L_strCPRTNM = L_strPRTNM;
						L_flg1STSFL = false;
					}

					while(!L_flgEOF)		
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes(L_strZONDS+"\n\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
						cl_dat.M_intLINNO_pbst+= 2;
						L_strZONDS = L_strCZONDS;
						L_strPZONDS = L_strZONDS;

						while(L_strZONDS.equals(L_strPZONDS) && !L_flgEOF)
						{
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes(L_strDSRNM+"\n\n");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
							cl_dat.M_intLINNO_pbst+= 2;

							L_strDSRNM = L_strCDSRNM;
							L_strPDSRNM = L_strDSRNM;

							while((L_strZONDS+L_strDSRNM).equals(L_strPZONDS+L_strPDSRNM) && !L_flgEOF)
							{
								dosREPORT.writeBytes(padSTRING('R',L_strPRTNM,40));
								L_strDSRNM = L_strCDSRNM;
								L_strPDSRNM = L_strDSRNM;
								
								while((L_strZONDS+L_strDSRNM+L_strPRTNM).equals(L_strPZONDS+L_strPDSRNM+L_strPPRTNM) && !L_flgEOF)
								{
									if (!L_strDOCNO.substring(3,8).toString().equals("77777"))
									{
										if (L_strDOCTP1.equals("DB") && L_strDOCTP.equals("21"))
										{	
											//if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText())) >= 0 && M_fmtLCDAT.parse(L_strDUEDT).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))<= 0)
											//if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) >= 0 && M_fmtLCDAT.parse(L_strDUEDT).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))<= 0)
											//CHANGES MADE ON 16/10/2007 TO SHOW CORRECT "AS ON DATE OVERDUE" FIGURE
											if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0 && M_fmtLCDAT.parse(L_strDUEDT).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))<= 0)
											{
												L_dblPRTPRODVL+= L_dblBALVL;
												L_dblPRTOSVL+= L_dblBALVL;

												L_dblDSRPRODVL+= L_dblBALVL;
												L_dblDSROSVL+= L_dblBALVL;

												L_dblZONPRODVL+= L_dblBALVL;
												L_dblZONOSVL+= L_dblBALVL;
											}	
											//else if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText())) > 0 && M_fmtLCDAT.parse(L_strDUEDT).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))> 0)
											else if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0 && M_fmtLCDAT.parse(L_strDUEDT).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))> 0)
											{
												L_dblPRTOSVL+= L_dblBALVL;
												//L_dblPRTDBVL+=L_dblBALVL;

												L_dblDSROSVL+= L_dblBALVL;
												L_dblZONOSVL+= L_dblBALVL;
											}	
											//else if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText())) < 0)	
											//else if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) < 0)	
											//CHANGES MADE ON 16/10/2007 TO SHOW CORRECT "AS ON DATE OVERDUE" FIGURE
											else if (M_fmtLCDAT.parse(L_strDUEDT1).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) <= 0)	
											{
												L_dblPRTDBVL+= L_dblBALVL;
												L_dblPRTOSVL+= L_dblBALVL;
												
												L_dblDSRDBVL+= L_dblBALVL;
												L_dblDSROSVL+= L_dblBALVL;

												L_dblZONDBVL+= L_dblBALVL;
												L_dblZONOSVL+= L_dblBALVL;
											}
										}
										else if (L_strDOCTP1.equals("DB") && L_strDOCTP != "21")
										{
											L_dblPRTOSVL+= L_dblBALVL;
											L_dblPRTDBVL+= L_dblBALVL;
										
											L_dblDSROSVL+= L_dblBALVL;
											L_dblDSRDBVL+= L_dblBALVL;
										
											L_dblZONOSVL+= L_dblBALVL;
											L_dblZONDBVL+= L_dblBALVL;
										}
										else if(L_strDOCTP1.equals("CR"))
										{	
											L_dblPRTCRVL+= L_dblBALVL;
											L_dblPRTOSVL-= L_dblBALVL;

											L_dblDSRCRVL+= L_dblBALVL;
											L_dblDSROSVL-= L_dblBALVL;

											L_dblZONCRVL+= L_dblBALVL;
											L_dblZONOSVL-= L_dblBALVL;
										}
									}	
									
									if (L_strDOCNO.substring(3,8).toString().equals("77777"))
									{
										if(L_strDOCTP1.equals("DB"))
										{
											L_dblPRTOSVL+= L_dblBALVL;
											L_dblPRTDBVL+= L_dblBALVL;

											L_dblDSROSVL+= L_dblBALVL;
											L_dblDSRDBVL+= L_dblBALVL;
											L_dblDSRYOPVL+= L_dblDOCVL;
											
											L_dblZONOSVL+= L_dblBALVL;
											L_dblZONDBVL+= L_dblBALVL;
											L_dblZONYOPVL+= L_dblDOCVL;

											L_dblPPRTYOPVL = L_dblDOCVL;
											L_strPPRTYOPFL = L_strDOCTP1;
										}	
										else
										{
											L_dblPRTCRVL+= L_dblBALVL;
											L_dblDSRCRVL+= L_dblBALVL;
											L_dblZONCRVL+= L_dblBALVL;
											L_dblDSRYOPVL-= L_dblDOCVL;
											L_dblZONYOPVL-= L_dblDOCVL;

											L_dblPRTOSVL-= L_dblBALVL;
											L_dblDSROSVL-= L_dblBALVL;
											L_dblZONOSVL-= L_dblBALVL;

											L_dblPPRTYOPVL = L_dblDOCVL;
											L_strPPRTYOPFL = L_strDOCTP1;
										}	
									}	
									intRECCT++;
																		
									if(!M_rstRSSET.next())
									{
										L_flgEOF = true;
										break;
									}
									
									L_strCZONDS = nvlSTRVL(M_rstRSSET.getString("ZONDS"),"");
									L_strCDSRNM = nvlSTRVL(M_rstRSSET.getString("DSRNM"),"");
									L_strCPRTNM = nvlSTRVL(M_rstRSSET.getString("PRTNM"),"");
									L_strDOCTP = nvlSTRVL(M_rstRSSET.getString("PL_DOCTP"),"");
									L_strDOCTP1 = nvlSTRVL(M_rstRSSET.getString("DOCTP1"),"");
									if (L_strDOCTP1.equals("DB") && L_strDOCTP.equals("21"))
									{	
										L_strDUEDT =  nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("PL_DUEDT")),"");
										M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strDUEDT)); 
										M_calLOCAL.add(Calendar.DATE,+intODDAY); 
										L_strDUEDT1 = M_fmtLCDAT.format(M_calLOCAL.getTime()); 
									}
									L_strDOCNO = nvlSTRVL(M_rstRSSET.getString("PL_DOCNO"),"");
									L_strPRTYOPFL = nvlSTRVL(M_rstRSSET.getString("PRTYOPFL"),"");
	   								L_dblDOCVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),"0"));
	   								L_dblADJVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_ADJVL"),"0"));
	   								L_dblBALVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("BALVL"),"0"));
	   								L_dblPRTYOPVL = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PRTYOPVL"),"0"));	
									
									L_strZONDS = L_strCZONDS;
									L_strDSRNM = L_strCDSRNM;
									L_strPRTNM = L_strCPRTNM;
									L_intPRTCNT+= 1;
								}//	while(L_strZONDS+L_strDSRNM+L_strPRTNM).equals(L_strPZONDS+L_strPDSRNM+L_strPPRTNM) && !L_flgEOF)
									L_dblPRTODVL = L_dblPRTDBVL - L_dblPRTCRVL;
									dosREPORT.writeBytes(padSTRING('L',"",3));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPPRTYOPVL,2),12));
									dosREPORT.writeBytes(padSTRING('L',L_strPPRTYOPFL,6));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTOSVL,2),12));
									dosREPORT.writeBytes(padSTRING('L',"",3));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTDBVL,2),12));
									dosREPORT.writeBytes(padSTRING('L',"",2));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTCRVL,2),12));
									dosREPORT.writeBytes(padSTRING('L',"",2));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTODVL,2),12));
									dosREPORT.writeBytes(padSTRING('L',"",2));
									dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPRTPRODVL,2),12));
									dosREPORT.writeBytes("\n");
				 					cl_dat.M_intLINNO_pbst+= 1;
									
									if(cl_dat.M_intLINNO_pbst >= 64)
									{	
										dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------");		
										if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
											prnFMTCHR(dosREPORT,M_strEJT);
										if(M_rdbHTML.isSelected())
											dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
										prnHEADER();
									dosREPORT.writeBytes("\n");
				 					cl_dat.M_intLINNO_pbst+= 1;
									}
									L_strPPRTNM = L_strPRTNM;
									L_dblPRTOSVL = 0;
									L_dblPRTDBVL = 0;
									L_dblPRTCRVL = 0;
									L_dblPRTPRODVL = 0;
									L_dblPPRTYOPVL = 0;
									L_strPPRTYOPFL = "";
									L_intPRTCNT	= 1;
							}//while(L_strZONDS+L_strDSRNM).equals(L_strPZONDS+L_strPDSRNM) && !L_flgEOF)
								L_dblDSRODVL = L_dblDSRDBVL - L_dblDSRCRVL;
								dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
								dosREPORT.writeBytes(padSTRING('R',L_strPDSRNM+" Total",42));
								dosREPORT.writeBytes(padSTRING('L',"",1));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDSRYOPVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",6));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDSROSVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",3));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDSRDBVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",2));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDSRCRVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",2));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDSRODVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",2));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDSRPRODVL,2),12));
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			 					cl_dat.M_intLINNO_pbst+= 3;
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
								L_dblDSROSVL = 0;
								if(cl_dat.M_intLINNO_pbst >= 64)
								{	
									dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
									prnHEADER();
								}
								L_dblDSRDBVL = 0;
								L_dblDSRCRVL = 0;
								L_dblDSRPRODVL = 0;
								L_dblDSRYOPVL = 0;
								L_strPDSRNM = L_strDSRNM;
						}//while(L_strZONDS).equals(L_strPZONDS) && !flgEOF)
								L_dblZONODVL = L_dblZONDBVL - L_dblZONCRVL;
								dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
								dosREPORT.writeBytes(padSTRING('R',L_strPZONDS+" Total",42));
								dosREPORT.writeBytes(padSTRING('L',"",1));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblZONYOPVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",6));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblZONOSVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",3));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblZONDBVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",2));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblZONCRVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",2));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblZONODVL,2),12));
								dosREPORT.writeBytes(padSTRING('L',"",2));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblZONPRODVL,2),12));
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			 					cl_dat.M_intLINNO_pbst+= 3;
							L_strPZONDS = L_strZONDS;
							L_dblZONOSVL = 0;
							L_dblZONDBVL = 0;
							L_dblZONCRVL = 0;
							L_dblZONYOPVL = 0;
							L_dblZONPRODVL = 0;
		}		
		}		
		prnFOOTR();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
		    if(M_rstRSSET != null)
				M_rstRSSET.close();						
			else
    		{							
				setMSG("No Data Found..",'E');
				return ;
			}
			if(intRECCT == 0)
			{
				setMSG("No Data Found for the given selection..",'E');
				return ;
			}
		}
		}
	 	catch(Exception L_EX)
		{		    
			setMSG(L_EX+" getDATA",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}

	/**void prnHEADER()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd",intRPTWD-25)+padSTRING('L',"Report Date : "+cl_dat.M_txtCLKDT_pbst.getText(),25)+"\n");
			if(rdbDETAL.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',cmbRPTTP.getSelectedItem().toString()+" as on : "+txtTODAT.getText().trim(),intRPTWD-24)+padSTRING('R',"Page No.    : "+cl_dat.M_PAGENO ,21)+"\n");
			}
			else if(rdbSUMRY.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',cmbRPTTP.getSelectedItem().toString()+" Summary as on : "+txtTODAT.getText().trim(),intRPTWD-24)+padSTRING('R',"Page No.    : "+cl_dat.M_PAGENO ,21)+"\n");
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n"); 
			cl_dat.M_intLINNO_pbst +=1;
		  	if(rdbDETAL.isSelected())
			{
				dosREPORT.writeBytes("Buyer Name                          \n");
				dosREPORT.writeBytes("Type   Doc. No     Doc.Date     Due Date    pending Days        Inv.Amt        Bal.Amt      Acc.Ref   ");
			}
			else if(rdbSUMRY.isSelected())
			{
			
				dosREPORT.writeBytes("Distributer Name   \n");
				dosREPORT.writeBytes("    Buyer                                          OutStanding          Unadjusted           Balance ");
			}  

			dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n"); 
			
			cl_dat.M_intLINNO_pbst += 8;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER ");
		}
	}*/
	
	private void prnHEADER()
	{  
	    try
	    {
		  	if(rdbDETAL.isSelected())
			{
				cl_dat.M_PAGENO +=1;
				cl_dat.M_intLINNO_pbst=0;
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,78));
				dosREPORT.writeBytes(padSTRING('R',"Report Date :" + cl_dat.M_strLOGDT_pbst,24) + "\n");			
				dosREPORT.writeBytes(padSTRING('R',cmbRPTTP.getSelectedItem().toString()+" Detail Report",78));			
				dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------\n");			
				dosREPORT.writeBytes("Buyer Name                          \n");
				dosREPORT.writeBytes("Type   Doc. No     Doc.Date     Due Date    pending Days        Inv.Amt        Bal.Amt      Acc.Ref   \n");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------\n");			
				cl_dat.M_intLINNO_pbst += 7;
			}
			else if(rdbSUMRY.isSelected())
			{
				cl_dat.M_PAGENO +=1;
				cl_dat.M_intLINNO_pbst=0;
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,123));
				dosREPORT.writeBytes(padSTRING('R',"Report Date :" + cl_dat.M_strLOGDT_pbst.trim(),24) + "\n");			
				dosREPORT.writeBytes(padSTRING('R',cmbRPTTP.getSelectedItem().toString()+" Summary Report as on "+txtTODAT.getText(),123));			
				dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
				dosREPORT.writeBytes("Zone \n");
				dosREPORT.writeBytes("Distributor                                                                   Overdue\n");
				dosREPORT.writeBytes("Buyer Name                                 YOP Balance & Flag   Total O/S     D/B Balance   C/R Balance   Total O/D     Projected O/D \n");
				dosREPORT.writeBytes("                                                                              As on Date    As on Date    As on Date    Next "+txtODDAY.getText()+" Days"+" \n");  
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");			
				cl_dat.M_intLINNO_pbst += 9;
			}  
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}	


private void prnFOOTR()
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
	    {   
			prnFMTCHR(dosREPORT,M_strEJT);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
		}	
	}
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnFOOTR",'E');
		}	
}

/**private void prnFOOTR()
{
	try
	{
		if(cl_dat.M_intLINNO_pbst >= 64)
		{
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strEJT);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
			prnHEADER();
		}
		dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes ("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('L'," ",10));//margin
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",20));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY  ",25));
			dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)  ",35));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 7;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		    {   
				prnFMTCHR(dosREPORT,M_strEJT);
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}	
	}
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnFOOTR",'E');
		}	
}*/

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
			setMSG(L_EX,"crtLINE");
		}
        return strln;
	}
	
}

