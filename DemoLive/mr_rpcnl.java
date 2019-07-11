/*
System Name   : Marketing System
Program Name  : Credit Note List Printing 
Modify Date   : 24-08-2006
Purpose : This program is used to generate & print Credit Note List Details.
List of Table used :
-----------------------------------------------------------------------------------------------------------
Table Name      Primary Key														Operation done
																				Insert  Upd  Query  Del
-----------------------------------------------------------------------------------------------------------
MR_PLTRN		PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO					  -      -     #     -
CO_PTMST		PT_PRTTP,PT_PRTCD												  -      -     #     -
-----------------------------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   For Generating the report:
		M_strSQLQRY="Select PL_PRTTP,PL_PRTCD,PL_DOCNO,PL_DOCVL,PL_ACCRF,PL_ACCDT,PT_PRTNM from MR_PLTRN,CO_PTMST where PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD  ";
			conditions
					1) if selected item code from Report is not 09 "means author credit notes " then
							 AND PL_DOCTP ='"+cmbRPTOP.getSelectedItem().toString().substring(0,2)
			           else
					  	if other credit notes code is given then
					       AND PL_DOCTP = '"+txtOTHCT.getText().trim()
			        	else
					       AND PL_DOCTP between '0A' AND '0Z'";
			
			         2)if date wise Report is selected then
						AND PL_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))
        	
        			   else if Document wise Report is Selected then
        					AND PL_DOCNO between '"+txtFMDOC.getText().trim()+"' AND '"+txtTODOC.getText().trim()
        
					3)if party code is given then
					if((cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("02"))||(cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("03")))
						 AND PL_PRTTP='D' AND PL_PRTCD = '"+txtPRTCD.getText().trim()
					else
						 AND PL_PRTTP='C' AND PL_PRTCD = '"+txtPRTCD.getText().trim()
			       order by PL_PRTTP,PL_PRTCD,PL_DOCNO ";
</I>
*/
import java.sql.Date;import java.sql.ResultSet;import java.awt.event.*;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import javax.swing.JComboBox;import java.util.StringTokenizer;
class mr_rpcnl extends cl_rbase
{
	private ButtonGroup btgRPTTP;    
	private JRadioButton rdbDATWS;	 
	private JRadioButton rdbDOCWS;
	private JComboBox cmbRPTOP;	   /** JComboBox to display & select Report.*/
	private JLabel lblFMDAT;       /** JLabel to display message on the Screen.*/
	private JLabel lblTODAT;       /**JLabel to display message on the Screen.*/
	private JLabel lblPRTCD;       /** JLabel for Party Code message.*/
	private JLabel lblPRTNM;       /** JLabel for displaying Party Name.*/
	private JTextField txtFMDAT;   /** JtextField to display & enter Date to generate the Report.*/
	private JTextField txtTODAT;   /** String Variable for date.*/
	private JTextField txtPRTCD;   /** JtextField to specify Party Code*/
	private JTextField txtFMDOC;   /** JtextField to  form Doc No.*/
	private JTextField txtTODOC;   /** JtextField to  To Doc No.*/
	private JLabel lblFMDOC;       /** JLabel to display message on the Screen.*/
	private JLabel lblTODOC;       /** JLabel to display message on the Screen.*/
	private JTextField txtOTHCT;
	private String strFMDAT;		/** String Variable for date.*/ 
	private String strTODAT;		/** String Variable for date.*/
	
	private String strFILNM;	    /** String Variable for generated Report file Name.*/ 
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
		
	private int intRECCT;		    /** Integer Variable to count records fetched, to block report generation if data not found.*/
	double dblDOCVL =0;
	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------------";
	mr_rpcnl()
	{
		super(2);
		cmbRPTOP = new JComboBox();
		try
		{
			String L_strCODCD="";
			String L_strCODDS="";
			ResultSet L_rstRSSET;
			btgRPTTP=new ButtonGroup();
					
			add(rdbDATWS=new JRadioButton("Date Wise",true),2,2,1,2,this,'L');
			add(rdbDOCWS=new JRadioButton("Doc No Wise"),2,4,1,2,this,'L');
			btgRPTTP.add(rdbDATWS);
			btgRPTTP.add(rdbDOCWS);
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT' AND SUBSTRING(CMT_CODCD,1,1) in ('0') order by CMT_CODCD ";
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODDS = nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"");
					cmbRPTOP.addItem(L_strCODCD+" "+L_strCODDS);
				}
			}
			if(L_rstRSSET != null)
			L_rstRSSET.close();
				
			setMatrix(20,8);
			add(new JLabel("Credit Note Type"),4,2,1,2,this,'L');
			add(cmbRPTOP,4,3,1,2,this,'L');
			add(new JLabel("Category"),4,5,1,1,this,'L');
	    	add(txtOTHCT=new TxtLimit(2),4,6,1,1,this,'L');
	
			add(lblFMDAT = new JLabel("From Date "),5,2,1,2,this,'L');
			add(txtFMDAT = new TxtDate(),5,3,1,1.5,this,'L');
			add(lblTODAT = new JLabel("To Date "),6,2,1,2,this,'L');
			add(txtTODAT = new TxtDate(),6,3,1,1.5,this,'L');
			
			add(lblFMDOC = new JLabel("From Doc No"),5,2,1,2,this,'L');
			add(txtFMDOC = new TxtLimit(8),5,3,1,1.5,this,'L');
			add(lblTODOC = new JLabel("To Doc No"),6,2,1,2,this,'L');
			add(txtTODOC = new TxtLimit(8),6,3,1,1.5,this,'L');
			
			add(lblPRTCD = new JLabel("Party "),7,2,1,2,this,'L');
			add(txtPRTCD = new TxtLimit(5),7,3,1,0.5,this,'L');
			add(lblPRTNM = new JLabel(""),7,4,1,3,this,'L');
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			lblFMDOC.setVisible(false);
			txtFMDOC.setVisible(false);
			lblTODOC.setVisible(false);
			txtTODOC.setVisible(false);
			txtOTHCT.setEnabled(false);
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
		txtOTHCT.setEnabled(false);		    
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==rdbDATWS)
		{
			txtFMDOC.setText("");
			txtTODOC.setText("");
			lblPRTNM.setText("");
			txtPRTCD.setText("");
			lblFMDOC.setVisible(false);
			txtFMDOC.setVisible(false);
			lblTODOC.setVisible(false);
			txtTODOC.setVisible(false);
			
			lblFMDAT.setVisible(true);
			txtFMDAT.setVisible(true);
			lblTODAT.setVisible(true);
			txtTODAT.setVisible(true);
		}
		if(M_objSOURC==rdbDOCWS)
		{
			lblPRTNM.setText("");
			txtPRTCD.setText("");
			lblFMDAT.setVisible(false);
			txtFMDAT.setVisible(false);
			lblTODAT.setVisible(false);
			txtTODAT.setVisible(false);
			lblFMDOC.setVisible(true);
			txtFMDOC.setVisible(true);
			lblTODOC.setVisible(true);
			txtTODOC.setVisible(true);
		}
		if(M_objSOURC==cmbRPTOP)
		{
			txtFMDOC.setText("");
			txtTODOC.setText("");
			txtPRTCD.setText("");
			lblPRTNM.setText("");
			if(cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("09"))
			{
	            txtOTHCT.setEnabled(true);		    
			}
			else
			{
			    txtOTHCT.setEnabled(false);		    
			    txtOTHCT.setText("");
			}
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				txtFMDAT.requestFocus();
				setMSG("Please Enter Date to generate the Report..",'N');
			}
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
	}
		
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == KeyEvent.VK_ENTER)
			{
				if(M_objSOURC == cmbRPTOP)
				{
					txtFMDAT.requestFocus();
				}
				if(M_objSOURC == txtOTHCT)
				{
					txtOTHCT.setText(txtOTHCT.getText().toUpperCase());
					txtFMDAT.requestFocus();
				}
				if(M_objSOURC == txtFMDAT)
				{
					if(txtFMDAT.getText().trim().length() == 10)
					{
						txtTODAT.requestFocus();
						setMSG("Enter Date",'N');
					}
					else
					{
						txtFMDAT.requestFocus();
						setMSG("Enter Date",'N');
					}
				}
				if(M_objSOURC == txtTODAT)
				{
					if(txtTODAT.getText().trim().length() == 10)
					{
                        txtPRTCD.setText("");
                        lblPRTNM.setText("");
                        txtPRTCD.requestFocus();
					}
					else
					{
						txtTODAT.requestFocus();
						setMSG("Enter Date",'N');
					}
				}
				if(M_objSOURC==txtFMDOC)
				{
					txtTODOC.requestFocus();
				}
				if(M_objSOURC==txtTODOC)
				{
                    txtPRTCD.setText("");
                    lblPRTNM.setText("");
					txtPRTCD.requestFocus();
				}
				if(M_objSOURC == txtPRTCD)
				{
					if(txtPRTCD.getText().trim().length()<5)
						lblPRTNM.setText("");
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				setMSG("",'N');
			}
	 		else if(L_KE.getKeyCode() == L_KE.VK_F1)
 			{						
				if(M_objSOURC == txtPRTCD) 
				{			
					txtPRTCD.setText((txtPRTCD.getText().trim()).toUpperCase());
					String L_strFMDAT = txtFMDAT.getText();
					String L_strTODAT = txtTODAT.getText();
					String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
					String strCRDTP = L_strCRDTP.substring(0,2);
					//if((strCRDTP.equals("09"))&&(txtOTHCT.getText().length() >0))
					  //  strCRDTP = txtOTHCT.getText().trim();
					//System.out.println("strCRDTP "+strCRDTP +"\n");
					M_strHLPFLD = "txtPRTCD";				
					if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
					    M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP = 'D' ";
					else if(strCRDTP.equals("09"))
						M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_PLTRN where PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD   ";
					else
					    M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP = 'C' ";
					if(rdbDATWS.isSelected())
					{
						if(L_strFMDAT.length()==10 && L_strTODAT.length()==10)
						{	
			  				if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
			    				M_strSQLQRY += " and PT_PRTCD in (select PL_PRTCD from MR_PLTRN where PL_DOCTP = '"+strCRDTP+"' and PL_PRTTP = 'D' and PL_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"') ";
							else if(strCRDTP.equals("09"))
							{
								if(txtOTHCT.getText().length() ==0)
								{
									M_strSQLQRY +="	AND PT_PRTTP + PT_PRTCD in (select PL_PRTTP + PL_PRTCD from MR_PLTRN  where  PL_DOCTP between '0A' AND '0Z'  and PL_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"') ";
								}
								else
									M_strSQLQRY +="	AND PT_PRTTP + PT_PRTCD in (select PL_PRTTP + PL_PRTCD from MR_PLTRN  where  PL_DOCTP = '"+txtOTHCT.getText().trim()+"'  and PL_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"') ";
							}
							else
			    				M_strSQLQRY += " and PT_PRTCD in (select PL_PRTCD from MR_PLTRN  where PL_DOCTP = '"+strCRDTP+"' and PL_PRTTP = 'C' and PL_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"') ";
						}
					}
					else if(rdbDOCWS.isSelected())
					{
						if(txtFMDOC.getText().trim().length()==8 && txtTODOC.getText().trim().length()==8)
						{
							if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
							    M_strSQLQRY += " and PT_PRTCD in (select PL_PRTCD from MR_PLTRN  where PL_DOCTP = '"+strCRDTP+"' and PL_PRTTP = 'D' and PL_DOCNO between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"') ";
							else if(strCRDTP.equals("09"))
							{
								if(txtOTHCT.getText().length() ==0)
									M_strSQLQRY += " and PT_PRTCD in (select PL_PRTCD from MR_PLTRN  where PL_DOCTP between '0A' AND '0Z' and PL_DOCNO between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"') ";
								else 
									M_strSQLQRY += " and PT_PRTCD in (select PL_PRTCD from MR_PLTRN  where PL_DOCTP ='"+strCRDTP+"'  and PL_DOCNO between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"') ";
							}
							else
								M_strSQLQRY += " and PT_PRTCD in (select PL_PRTCD from MR_PLTRN  where PL_DOCTP = '"+strCRDTP+"' and PL_PRTTP = 'C' and PL_DOCNO between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"') ";
						}
					}
					if(txtPRTCD.getText().trim().length()>0)
					{
						M_strSQLQRY +=" AND PT_PRTCD like '"+txtPRTCD.getText().trim()+"%'";
					}
					M_strSQLQRY += " order by PT_PRTNM";
					//System.out.println(" Party1  "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Party Name"},2,"CT");
				}
				if(M_objSOURC == txtOTHCT)
    			{
    				M_strHLPFLD = "txtOTHCT";
    				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXOCN' And CMT_CODCD like '0%'";					
					//System.out.println(" OTHCR  "+M_strSQLQRY);
    				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
    			}
				if(M_objSOURC == txtFMDOC) 
				{
					M_strHLPFLD = "txtFMDOC";		
					String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
					String strCRDTP = L_strCRDTP.substring(0,2);
					if((strCRDTP.equals("09"))&&(txtOTHCT.getText().length() >0))
					    strCRDTP = txtOTHCT.getText().trim();
					if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
					{
    					M_strSQLQRY = "select distinct PL_DOCNO,PT_PRTNM from MR_PLTRN ,CO_PTMST  where PT_PRTTP = PL_PRTTP AND PT_PRTCD = PL_PRTCD AND PL_DOCTP = '"+strCRDTP+"'";
    					if(txtFMDOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND PL_DOCNO like '"+txtFMDOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by PL_DOCNO";
						//System.out.println(" docno DIST "+M_strSQLQRY);
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Distributor"},2,"CT");
					}
					else 
					{
    					M_strSQLQRY = "select distinct PL_DOCNO,PL_DOCDT from MR_PLTRN where ";
						if(strCRDTP.equals("09")&& (txtOTHCT.getText().length() ==0))
							M_strSQLQRY +="	PL_DOCTP between '0A' AND '0Z' ";
						else						
							M_strSQLQRY +=" PL_DOCTP = '"+strCRDTP+"' ";
						if(txtFMDOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND PL_DOCNO like '"+txtFMDOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by PL_DOCNO";
						//System.out.println(" docno "+M_strSQLQRY);
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date"},2,"CT");
					}
				}
				if(M_objSOURC == txtTODOC) 
				{
					M_strHLPFLD = "txtTODOC";
					String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
					String strCRDTP = L_strCRDTP.substring(0,2);
					if((strCRDTP.equals("09"))&&(txtOTHCT.getText().length() >0))
					    strCRDTP = txtOTHCT.getText().trim();
					if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
					{
    					M_strSQLQRY = "select distinct PL_DOCNO,PT_PRTNM from MR_PLTRN ,CO_PTMST  where PT_PRTTP = PL_PRTTP AND PT_PRTCD = PL_PRTCD AND PL_DOCTP = '"+strCRDTP+"'";
    					if(txtTODOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND PL_DOCNO like '"+txtTODOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by PL_DOCNO";
						//System.out.println(" TODOCno  "+M_strSQLQRY);
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Distributor"},2,"CT");
					}
					else 
					{
    					M_strSQLQRY = "select distinct PL_DOCNO,PL_DOCDT from MR_PLTRN where ";
						if(strCRDTP.equals("09")&& (txtOTHCT.getText().length() ==0))
							M_strSQLQRY +="	PL_DOCTP between '0A' AND '0Z' ";
						else						
							M_strSQLQRY +=" PL_DOCTP = '"+strCRDTP+"' ";
    					if(txtTODOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND PL_DOCNO like '"+txtTODOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by PL_DOCNO";
						//System.out.println(" TODOC NO1  "+M_strSQLQRY);
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date"},2,"CT");
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
   /**
	Method for execution of help for Memo Number Field.
   */
	void exeHLPOK()
	{
		super.exeHLPOK();		
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD == "txtPRTCD")
		{
			txtPRTCD.setText(L_STRTKN.nextToken());
			lblPRTNM.setText(L_STRTKN.nextToken());
		}
		if(M_strHLPFLD == "txtFMDOC")
		{
			txtFMDOC.setText(L_STRTKN.nextToken());
		}
		if(M_strHLPFLD == "txtOTHCT")
		{
			txtOTHCT.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtTODOC")
		{
			txtTODOC.setText(L_STRTKN.nextToken());
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpcnl.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpcnl.doc";
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
			
	/**
	 * Method to fetch data from Database & club it with Header & Footer.
	*/
	public void getDATA()
	{		
		String L_strPRTTP="";
		String L_strPRTCD="";
		String L_strDOCNO="";
		String L_strPRTCD1="";
		String L_strREMDS="";
		double L_dblDOCVL=0;	
		cl_dat.M_PAGENO=1;
		java.sql.Date L_datTMPDT;
		cl_dat.M_intLINNO_pbst =0;
		setCursor(cl_dat.M_curWTSTS_pbst);
		String L_strFMDAT = txtFMDAT.getText().trim();
       	String L_strTODAT = txtTODAT.getText().trim();
		try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			  	prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Credit Note List</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			M_strSQLQRY="Select PL_PRTTP,PL_PRTCD,PL_DOCNO,PL_DOCVL,PL_ACCRF,PL_ACCDT,PT_PRTNM from MR_PLTRN,CO_PTMST where PL_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PT_PRTTP=PL_PRTTP AND PT_PRTCD=PL_PRTCD  ";
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09")) // other than distributor comission
			{
				M_strSQLQRY +=" AND PL_DOCTP ='"+cmbRPTOP.getSelectedItem().toString().substring(0,2)+"'";
			}
			else
			{
				if(txtOTHCT.getText().trim().length()==2)
					M_strSQLQRY +=" AND PL_DOCTP = '"+txtOTHCT.getText().trim()+"' ";
				else
					M_strSQLQRY +=" AND PL_DOCTP between '0A' AND '0Z'";
			}
			if(rdbDATWS.isSelected())
        	{
        		M_strSQLQRY +=" AND PL_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
        	}
        	if(rdbDOCWS.isSelected())
        	{
        		M_strSQLQRY +=" AND PL_DOCNO between '"+txtFMDOC.getText().trim()+"' AND '"+txtTODOC.getText().trim()+"'";
        	}
			if(txtPRTCD.getText().trim().length()==5)
			{
				if((cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("02"))||(cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("03")))
					M_strSQLQRY += " AND PL_PRTTP='D' AND PL_PRTCD = '"+txtPRTCD.getText().trim()+"' ";
				else if(cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("09"))
					M_strSQLQRY += " AND PL_PRTCD = '"+txtPRTCD.getText().trim()+"' ";
				else
					M_strSQLQRY += " AND PL_PRTTP='C' AND PL_PRTCD = '"+txtPRTCD.getText().trim()+"' ";
			}
			 
			M_strSQLQRY += " order by PL_PRTTP,PL_PRTCD,PL_DOCNO ";
			prnHEADER();
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
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
				L_strPRTTP=nvlSTRVL(M_rstRSSET.getString("PL_PRTTP"),"");
				L_strPRTCD=nvlSTRVL(M_rstRSSET.getString("PL_PRTCD"),"");
				if(!L_strPRTCD.equals(L_strPRTCD1))
				{
					if(intRECCT>0)
					{
						dosREPORT.writeBytes( strDOTLN +"\n");
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblDOCVL,2),74));	
						dosREPORT.writeBytes("\n"+strDOTLN +"\n");
						dblDOCVL=0;
						cl_dat.M_intLINNO_pbst += 3;
					}
				}
				dosREPORT.writeBytes(padSTRING('R'," ",3));
				dosREPORT.writeBytes(padSTRING('R',L_strPRTTP,3));
				dosREPORT.writeBytes(padSTRING('R',L_strPRTCD,7));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),39));
				L_strDOCNO=nvlSTRVL(M_rstRSSET.getString("PL_DOCNO"),"");
				dosREPORT.writeBytes(padSTRING('R',L_strDOCNO,10));	
				L_dblDOCVL=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PL_DOCVL"),"0"));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDOCVL,2),12));	
				dosREPORT.writeBytes(padSTRING('R'," ",3));
				dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PL_ACCRF"),""),13));	
				L_datTMPDT =M_rstRSSET.getDate("PL_ACCDT");
				if(L_datTMPDT !=null)
					dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(L_datTMPDT).toString(),10));	
				else
					dosREPORT.writeBytes(padSTRING('R'," ",10));	
				dblDOCVL +=L_dblDOCVL;
				L_strPRTCD1=L_strPRTCD;
				L_strREMDS=getRMKDS(L_strDOCNO);
				if(L_strREMDS.length()>0)
				{
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R'," ",3));
					dosREPORT.writeBytes(padSTRING('R',L_strREMDS,120));	
					cl_dat.M_intLINNO_pbst +=1;
				}
					
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
					
				intRECCT++;
			}	
			if(intRECCT>0)
			{
				dosREPORT.writeBytes(strDOTLN +"\n");
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblDOCVL,2),75));	
				dosREPORT.writeBytes("\n"+strDOTLN +"\n");
				cl_dat.M_intLINNO_pbst += 3;
				dblDOCVL=0;
			}
			setMSG("Report completed.. ",'N');			
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
			setMSG(L_EX,"getDATA");
		}
	}
	
	private String getRMKDS(String P_strDOCNO)
	{
	    String L_strREMDS ="";
	    try
	    {
	        M_strSQLQRY = "Select RM_REMDS,RM_STSFL from MR_RMMST ";
			M_strSQLQRY += " where  RM_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RM_TRNTP = 'CR'";
       		M_strSQLQRY += " and RM_DOCNO = '" + P_strDOCNO + "' and  isnull(RM_STSFL,'') <> 'X'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(L_rstRSSET.next())
				{
					if(!nvlSTRVL(L_rstRSSET.getString("RM_STSFL"),"").equals("X"))
						L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
					else
						L_strREMDS ="";
				}
				L_rstRSSET.close();
			}
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getRMKDS");
	    }
	    return L_strREMDS;
	}
	/**
	 * Method to generate the header of the Report.
    **/
	void prnHEADER()
	{
		try
		{
			String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
			String strCRDTP1 = L_strCRDTP.substring(2,L_strCRDTP.length()).toString().trim();
			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-21)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Credit Note List",strDOTLN.length()-19));
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(padSTRING('R',"Date    :"+cl_dat.M_strLOGDT_pbst,20));		
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',strCRDTP1,strCRDTP1.length()+2));
			if(rdbDATWS.isSelected())
        		dosREPORT.writeBytes(padSTRING('R',"For Date Range :"+txtFMDAT.getText().trim()+ " To "+txtTODAT.getText().trim(),(strDOTLN.length()-strCRDTP1.length()-21)));
        	if(rdbDOCWS.isSelected())
        		dosREPORT.writeBytes(padSTRING('R',"For Doc. No :"+txtFMDOC.getText().trim()+ " To "+txtTODOC.getText().trim(),(strDOTLN.length()-strCRDTP1.length()-21)));
        	
			dosREPORT.writeBytes(padSTRING('R',"Page No :"+cl_dat.M_PAGENO,20));				
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst +=3;
			dosREPORT.writeBytes(strDOTLN +"\n");	
			dosREPORT.writeBytes("   Code      Name                                    Doc. No     Amount Rs.   Acc.Ref.No   Acc.ref Date \n");
			dosREPORT.writeBytes("   Remark   ");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst += 4;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER");
			
		}
	}

	
	
	
	
	
	
	
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
			if(txtPRTCD.getText().trim().length()>0 && txtPRTCD.getText().trim().length()<5)
			{
				setMSG("Invalid Party Code Press f1 for help ..",'E');
				txtPRTCD.requestFocus();
				return false;
			}
			if(rdbDATWS.isSelected())
			{
				if(txtFMDAT.getText().trim().length() != 10)
				{
					setMSG("Enter the proper Date..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				if(txtTODAT.getText().trim().length() != 10)
				{
					setMSG("Enter the proper Date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
			
				if(txtFMDAT.getText().trim().length()==10 && txtTODAT.getText().trim().length()==10)
				{
					setMSG(" ",'N');			
					if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
					{
						setMSG("From Date can not be greater than TO Date's date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
				}
				if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date can not be greater than today's date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
			}
			if(rdbDOCWS.isSelected())
			{
				if(txtFMDOC.getText().trim().length()<8)
				{
					setMSG("Please Enter proper Doc. Number or Press f1 for Help..",'E');
					txtFMDOC.requestFocus();
					return false;
				}
				if(txtTODOC.getText().trim().length()<8)
				{
					setMSG("Please Enter proper Doc. Number or Press f1 for Help..",'E');
					txtTODOC.requestFocus();
					return false;
				}
				if( Integer.parseInt(txtTODOC.getText().trim().toString()) < Integer.parseInt(txtFMDOC.getText().trim().toString()) )
				{
					setMSG("To DOC Number Should Be Greater Or Equal To From DOC Number..",'E');
					txtTODOC.requestFocus();
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
	
}