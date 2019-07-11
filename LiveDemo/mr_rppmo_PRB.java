
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

class mr_rppmo extends cl_rbase
{			
	private JComboBox cmbRPTTP;
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;		 
	private JTextField txtDSRCD;		 
	private JTextField txtDSRNM;
	private JTextField txtTODAT;		 
	private ButtonGroup btgRPTTP;    
	private JRadioButton rdbSUMRY;	 
	private JRadioButton rdbDETAL;	      /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			   /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				   /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ;   /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intRPTWD = 102;            // report width
	
	private String strPPRTY="";
	private String strNPRTY="";
	private double dblBALAM=0;
	private double dblTOTAL=0;
	private int intDEBIT=0;
	private int intCREDT=0;
	
	public mr_rppmo()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			cmbRPTTP = new JComboBox();
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
			add(new JLabel("Dist Code"),4,3,1,1,this,'L');
			add(txtDSRCD=new TxtLimit(5),4,4,1,1,this,'L');
			add(new JLabel("Dist Name"),5,3,1,1,this,'L');
			add(txtDSRNM=new TxtLimit(30),5,4,1,3,this,'L');
			add(new JLabel("Party Code"),6,3,1,1,this,'L');
			add(txtPRTCD=new TxtLimit(5),6,4,1,1,this,'L');
			add(new JLabel("Party Name"),7,3,1,1,this,'L');
			add(txtPRTNM=new TxtLimit(30),7,4,1,3,this,'L');

			add(new JLabel("As on Date"),8,3,1,1,this,'L');
			add(txtTODAT=new TxtDate(),8,4,1,1,this,'L');

			
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
			if(txtDSRCD.getText().trim().length()<5)
			{
				setMSG("Please Enter  Distributer Code or press f1 for help..",'E');
				txtDSRCD.requestFocus();
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
			if(M_objSOURC==txtDSRCD)
			{
				M_strHLPFLD="txtDSRCD";
				M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where  PT_PRTTP='D' AND ifnull(PT_STSFL,'')<> 'X'";
				if(txtDSRCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND PT_PRTCD LIKE '"+txtDSRCD.getText().trim().toUpperCase()+"%'"; 
				}
					M_strSQLQRY +="Order by PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Dist Code","Dist Name"},2,"CT");
			}
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD = "txtPRTCD";
				M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD  AND ifnull(PT_STSFL,'')<> 'X'";
				
				if(txtDSRCD.getText().trim().length()==5)
				{
					if(!txtDSRCD.getText().trim().substring(1,5).equals("8888"))
					{
						M_strSQLQRY += " AND PT_DSRCD='"+txtDSRCD.getText().trim()+"'";					
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
						M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where  PT_PRTTP='D' AND ifnull(PT_STSFL,'')<> 'X'";
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
						M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN  where  PT_PRTTP='C'  AND PL_PRTTP=PT_PRTTP AND PL_PRTCD=PT_PRTCD  AND ifnull(PT_STSFL,'')<> 'X'";
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
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				
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
			cl_dat.M_PAGENO =1;
						
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Product Specification Sheet</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
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
			//boolean L_flgRSSET=false;
			//04/08/2006
						
			String L_strDATE = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()));
			//System.out.println("AFter Conversion "+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText())));
			
			
			M_strSQLQRY ="SELECT PL_PRTCD,PT_PRTNM,substr(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DOCVL,(DAYS(Date('"+L_strDATE+"'))-DAYS(PL_DUEDT)) PENDAYS, ";
			M_strSQLQRY +=" (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) BALAM,";
			M_strSQLQRY +=" PL_ACCRF FROM MR_PLTRN,CO_PTMST WHERE (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND ";
			M_strSQLQRY +=" (PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE  '3%')";
			if(cmbRPTTP.getSelectedIndex()==1)
			{
				M_strSQLQRY +="AND (DAYS(date('"+L_strDATE+"'))-DAYS(PL_DUEDT))>0 ";
			}
			if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
				M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
			if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
				M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
			M_strSQLQRY += "Union ";
			M_strSQLQRY +="SELECT PL_PRTCD,PT_PRTNM,substr(PL_DOCTP,1,1)PL_DOCTP,PL_DOCNO,PL_DOCDT,PL_DUEDT,PL_PAYDT,PL_DOCVL,(DAYS(date('"+L_strDATE+"'))-DAYS(PL_DUEDT)) PENDAYS, ";
			M_strSQLQRY +=" (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) BALAM,";
			M_strSQLQRY +=" PL_ACCRF FROM MR_PLTRN,CO_PTMST WHERE (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND ";
			M_strSQLQRY +=" (PL_DOCTP LIKE  '0%' OR PL_DOCTP LIKE  '1%')";   
			if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
				M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
			if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
				M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
			M_strSQLQRY +=" ORDER BY PL_PRTCD asc, PL_DOCTP desc";
			
			//END
			
			//System.out.println("SQLQRY DETAILS  = "+M_strSQLQRY);
						
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			prnHEADER();
			while(M_rstRSSET.next())
			{
				if(cl_dat.M_intLINNO_pbst >64)
				{
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n");
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO +=1;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				strNPRTY=nvlSTRVL(M_rstRSSET.getString("PL_PRTCD"),"");
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
						dosREPORT.writeBytes(padSTRING('R',txtDSRNM.getText().trim()+" ("+txtDSRCD.getText().trim()+")",44));
						dosREPORT.writeBytes("\n");
					}
	
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTNM")+" ("+strNPRTY+")",44));
			
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
	
	private void getSUMRY()
	{
		String L_strDOCTP="";
		
		double L_dblOUTST=0.0;
		double L_dblUNADJ=0.0;
		double L_dblTOTUN=0.0;
		double L_dblTOTOS=0.0;
		double L_dblBALNC=0.0;
		int L_intDSPBL=0;
		strNPRTY="";
		strPPRTY="";
		intRECCT=0;
		try
		{
			String L_strDATE = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()));
			
			M_strSQLQRY="SELECT PL_PRTCD,PT_PRTNM,'OS', sum( (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) )BALAM FROM MR_PLTRN,CO_PTMST";
			M_strSQLQRY +=" WHERE (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 AND PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD ";
			M_strSQLQRY +=" AND  (PL_DOCTP LIKE  '2%' OR PL_DOCTP LIKE  '3%')";
			if(cmbRPTTP.getSelectedIndex()==1)
			{
				M_strSQLQRY +="AND (DAYS(Date('"+L_strDATE+"'))-DAYS(PL_DUEDT))>0 ";
			}
			if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
				M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
			if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
				M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
		
			M_strSQLQRY += "group by PL_PRTCD,PT_PRTNM,'OS' union ";
		
			M_strSQLQRY +=" SELECT PL_PRTCD,PT_PRTNM,'UN', sum( (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) )BALAM ";
			M_strSQLQRY +=" FROM MR_PLTRN,CO_PTMST WHERE (ifnull(PL_DOCVL,0) - ifnull(PL_ADJVL,0)) >0 ";
			M_strSQLQRY +=" AND PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD AND  (PL_DOCTP LIKE  '0%' OR PL_DOCTP LIKE  '1%') ";
			if((txtDSRCD.getText().trim().length()==0||txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==5))
				M_strSQLQRY +=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
			if((txtDSRCD.getText().trim().length()==5)&&(txtPRTCD.getText().trim().length()==0))
				M_strSQLQRY +=" AND PL_PRTCD in(select PT_PRTCD from CO_PTMST where PT_PRTTP='C' AND PT_DSRCD='"+txtDSRCD.getText().trim()+"')";
		
			M_strSQLQRY += " group by PL_PRTCD,PT_PRTNM,'UN'";
			 
		
			//System.out.println("SQLQRY ="+M_strSQLQRY);
				
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				
			prnHEADER();
			int L_int=0;
			while(M_rstRSSET.next())
			{
				strNPRTY=nvlSTRVL(M_rstRSSET.getString("PL_PRTCD"),"");
								
				if(cl_dat.M_intLINNO_pbst >65)
				{
					if(!strNPRTY.equals(strPPRTY))
					{
						if(L_int==1)
						{
							dosREPORT.writeBytes(padSTRING('L'," ",20));
						}
						L_dblBALNC=L_dblOUTST-L_dblUNADJ;
						
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBALNC,0),18));
						L_intDSPBL=2;
						
					}
					else
					{
						dosREPORT.writeBytes(padSTRING('R'," ",5));
						L_dblUNADJ=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("BALAM"),"0"));
						L_dblTOTUN +=L_dblUNADJ;
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblUNADJ,0),15));
						
						L_dblBALNC=L_dblOUTST-L_dblUNADJ;
						
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBALNC,0),18));
						L_intDSPBL=2;
					}
					
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(crtLINE(intRPTWD,"-")+"\n");
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO +=1;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				if(!strNPRTY.equals(strPPRTY))
				{
					
					if(intRECCT==0)
					{
						dosREPORT.writeBytes(padSTRING('R',txtDSRNM.getText().trim()+" ("+txtDSRCD.getText().trim()+")",44));
					}
					if(intRECCT>0)
					{
						if(L_int==1)
						{
							dosREPORT.writeBytes(padSTRING('L'," ",20));
						}
						L_dblBALNC=L_dblOUTST-L_dblUNADJ;
						if(L_intDSPBL==2)
							dosREPORT.writeBytes(padSTRING('L'," ",18));
						else
						{
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBALNC,0),18));
							dosREPORT.writeBytes("\n");
						}
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R'," ",4));
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTNM")+" ("+strNPRTY+")",43));
					L_dblUNADJ=0;
					L_dblOUTST=0;
					L_int=0;
					cl_dat.M_intLINNO_pbst +=2;
					L_intDSPBL=0;
				}
				
				L_strDOCTP=M_rstRSSET.getString(3);
				if(L_strDOCTP.equals("OS"))
				{
					L_dblOUTST=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("BALAM"),"0"));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblOUTST,0),15));
					
					L_dblTOTOS +=L_dblOUTST;
					L_int=1;
					
					
				}
				else if(L_strDOCTP.equals("UN"))
				{
					if(L_intDSPBL==2)
					{
						dosREPORT.writeBytes(padSTRING('L'," ",15));
					}
					else
					{
						
						if(L_int==0)
						{
							dosREPORT.writeBytes(padSTRING('L'," ",15));
						}
						dosREPORT.writeBytes(padSTRING('R'," ",5));
						L_dblUNADJ=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("BALAM"),"0"));
						L_dblTOTUN +=L_dblUNADJ;
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblUNADJ,0),15));
						L_int=0;
					}
				}
				intRECCT++;
				strPPRTY=strNPRTY;	
			}
			
			if(L_int==1)
			{
				dosREPORT.writeBytes(padSTRING('L'," ",20));
			}
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat((L_dblOUTST-L_dblUNADJ),0),18));
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes(padSTRING('L',"Total :",44));
			dosREPORT.writeBytes(padSTRING('R'," ",3));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTOS,0),15));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTUN,0),20));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat((L_dblTOTOS-L_dblTOTUN),0),18));
			dosREPORT.writeBytes("\n"+crtLINE(intRPTWD,"-")+"\n");
			
		}
		catch(Exception E)
		{
			setMSG(E,"GetSUMRY");
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
			setMSG(L_EX,"crtLINE");
		}
        return strln;
	}
	
}