import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;
/*
System Name    : Marketing System.
Program Name   : Customer Permit Form Query / Report (available forms) 
Program Desc.  : This module displays/prints detail about available permit form status 
				 according to the input combinations specified by user. 
Author         : Mr.Aawaj Jain
Date           : 2007
Version        : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :

System Name    : Marketing System.
 
Program Name   : Customer Permit form query/report.

Purpose        : This module displays/prints detail about available permit form status 
				  according to the input combinations specified by user.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MR_PFTRN       PFT_PRTTP,PFT_PRTCD,PFT_FRMTP,PFT_FRMNO                 #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD						   #	
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtPRTTP    PFT_PRTTP       MR_PFTRN      VARCHAR(1)     Party Type
txtPRTCD    PFT_PRTCD		MR_PFTRN	  VARCHAR(5)	 Party Code
txtFRMTP	PFT_FRMTP		MR_PFTRN	  VARCHAR(1)	 Form Type
txtLOCCD    Variable		-			  VARCHAR(1)	 Location 
txtSTRDT    PFT_XXXDT	    MM_PFTRN	  DATE		 	 From Date 	
txtENDDT    PFT_XXXDT	    MM_PFTRN	  DATE			 To Date
--------------------------------------------------------------------------------------

List of Fields with help facility
Field Name  Display Description         Display Columns           Table Name
---------------------------------------------------------------------------------------------
txtPRTTP    Party Type,Description      cmt_codcd,cmt_codds		  CO_CDTRN-MST/COXXPRT
txtPRTCD	Party Code,Party Name		pt_prtcd,pt_prtnm		  CO_PTMST
txtFRMTP	Form Type,Description		cmt_codcd,cmt_codds		  CO_CDTRN-SYS/MRXXFTP		
---------------------------------------------------------------------------------------------



Validations :
    - To date must be greater then From data & Smaller than Current date.
    - If Party Code is not specified then record for all parties will be fetched display.
	- If form type is not specified record for all form type will be fetched.
	- If location is not specified , records at all locations will be fetched and from and to date validations will not be applied
	- Report will be generated in text/HTML format for generating hard copy, when print option is selected.	
 */

class hr_rpadt extends cl_rbase 
{

	private JTextField txtDPTCD;			
	private JTextField txtEMPNO;
	private JTextField txtSTRDT;			
	private JTextField txtENDDT;
	private JTextField txtTMLIM;
	private JLabel lblDPTNM;
	private JLabel lblEMPNM;
	private JLabel lblTMLIM;
	private JComboBox cmbRPTYP;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpadt.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    private static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	
	private SimpleDateFormat fmtDBDATTM_YMD = new SimpleDateFormat("yyyy-MM-dd"); 
	private SimpleDateFormat fmtHHMMSS = new SimpleDateFormat("HH:mm:ss"); 
	private SimpleDateFormat fmtHHMM = new SimpleDateFormat("HH:mm"); 
	java.util.Date datWRKDT0;		// Previous day
	java.util.Date datWRKDT1;		// Current Working day
	java.util.Date datWRKDT2;		// Next Day
	java.util.Date datWRKDT3;		// Next Day Ending date
	java.util.Date datWRKDT;		// Working date to be considered depending on In/Out time
	boolean flgALLDPT;
	
	hr_rpadt()		/*  Constructor   */
	{
		super(2);
		try
		{
			//System.out.println("hiiiiiiiiii");
			setMatrix(20,20);			
			flgALLDPT = false;
			if(cl_dat.M_strUSRCD_pbst.equals("SKS") || cl_dat.M_strUSRCD_pbst.equals("S_J") || cl_dat.M_strUSRCD_pbst.equals("PDP") || cl_dat.M_strUSRCD_pbst.equals("RPN") || cl_dat.M_strUSRCD_pbst.equals("SDD"))
				flgALLDPT = true;
			add(new JLabel("Report Type"),5,3,1,2,this,'L');
			add(cmbRPTYP = new JComboBox(),5,5,1,6,this,'L');			
			
			add(new JLabel("Department"),6,3,1,2,this,'L');
			add(txtDPTCD= new TxtLimit(3),6,5,1,2,this,'L');
			add(lblDPTNM=new JLabel(),6,7,1,8,this,'L');     
			
			add(new JLabel("Employee"),7,3,1,2,this,'L');
			add(txtEMPNO= new TxtLimit(4),7,5,1,2,this,'L');
			add(lblEMPNM=new JLabel(),7,7,1,8,this,'L');     
			
			add(new JLabel("Form Date"),8,3,1,2,this,'L');
			add(txtSTRDT= new TxtDate(),8,5,1,2,this,'L');
			
			add(new JLabel("To Date"),9,3,1,2,this,'L');
			add(txtENDDT = new TxtDate(),9,5,1,2,this,'L');
		
			add(lblTMLIM = new JLabel("Time Limit"),10,3,1,2,this,'L');
			add(txtTMLIM = new TxtTime(),10,5,1,2,this,'L');
			txtTMLIM.setVisible(false);
			lblTMLIM.setVisible(false);
			
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtDPTCD.setInputVerifier(oINPVF);
			txtEMPNO.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);			

			cmbRPTYP.addItem("Select Report Type");
			cmbRPTYP.addItem("Absentee  Report");
			cmbRPTYP.addItem("Presentee Report");
			cmbRPTYP.addItem("Daily Perfomance Report");
			cmbRPTYP.addItem("Late Comming Report");
			cmbRPTYP.addItem("Early Going Report");
			cmbRPTYP.addItem("Weekly Off Performance Report");
			cmbRPTYP.addItem("Sanctioned Leave Performance Report");
			cmbRPTYP.addItem("Exception Report");
			cmbRPTYP.addItem("Less Working (below Entered Hrs)");
			//cmbRPTYP.addItem("Over Time Report");
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");
			if(txtEMPNO.getText().length()==0)
				lblEMPNM.setText("");
			if(M_objSOURC == cmbRPTYP)
			{
				if(cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)"))
				{
				   txtTMLIM.setVisible(true);
				   lblTMLIM.setVisible(true);
				}
				else
				{
				   txtTMLIM.setVisible(false);
				   lblTMLIM.setVisible(false);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        	{						
										//help for Department Code
        		if(M_objSOURC==txtDPTCD)		
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
					//M_strSQLQRY=" Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
					//M_strSQLQRY+=" and CMT_STSFL <> 'X'";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "+ (flgALLDPT ? "" : " and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO ='"+cl_dat.M_strEMPNO_pbst+"')");
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
        								//help for Employee Category
				if(M_objSOURC==txtEMPNO)		
        		{
					if(!flgALLDPT && txtDPTCD.getText().length()==0)
					{
						setMSG("Please Enter Department Code first...",'E');
						txtDPTCD.requestFocus();
						txtEMPNO.setText("");
						return;
					}
						
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
        			String L_ARRHDR[] = {"Code","Category"};
        			M_strSQLQRY = "select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U'";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY += " and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
					M_strSQLQRY += " order by EP_EMPNO";
					//System.out.println(">>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
        	{						
        		if(M_objSOURC==txtDPTCD)		
					txtEMPNO.requestFocus();
        		if(M_objSOURC==txtEMPNO)		
					txtSTRDT.requestFocus();
        		if(M_objSOURC==txtSTRDT)		
					txtENDDT.requestFocus();
				if(M_objSOURC==txtENDDT)		
				{
					if(cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)"))
						txtTMLIM.requestFocus();
					else
						cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				if(M_objSOURC==txtTMLIM)		
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is KeyPressed");
		}	
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDPTCD.setText(L_STRTKN.nextToken());
				lblDPTNM.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNO.setText(L_STRTKN.nextToken());
				lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL()
	{
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			String strOLD_WRKDT="";
			String strNEW_WRKDT="";
			
			String strOLD_DPTCD="";
			String strNEW_DPTCD="";
			
			String strOLD_EMPCT="";
			
			setMSG("Printing Report..",'N');
			////report For Absentee
			if(cmbRPTYP.getSelectedItem().toString().equals("Absentee  Report"))
			{
				M_strSQLQRY  =" SELECT  DISTINCT SW_INCTM,SW_OUTTM,isnull(SW_ACTWK,'00:00') SW_ACTWK,SW_WRKDT,SW_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,isnull(EP_EMPCT,'') EP_EMPCT,EP_DPTCD,EP_DPTNM,SW_WRKSH,isnull(SW_LVECD,'') SW_LVECD";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U' AND EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO and ((SW_INCTM is null or SW_OUTTM is null) or (sw_actwk is not null and isnull(sw_actwk,'00:00') between '00:01' and '05:00')) AND SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				//M_strSQLQRY +=" AND (isnull(SW_LVECD,'') = '' or SW_LVECD <> 'WO')";
				M_strSQLQRY +="  and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_EMPNO";

				System.out.println("Query Absentee  Report>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						
							if(!strOLD_WRKDT.equals(strNEW_WRKDT))
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
								strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
								strOLD_DPTCD="";
								crtNWLIN();
							}
							if(!strOLD_DPTCD.equals(strNEW_DPTCD))
							{
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
								strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							}
							else D_OUT.writeBytes(padSTRING('R',"",19)); 
							
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),4));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),6));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),20));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
							
							if(M_rstRSSET.getString("SW_INCTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
							else D_OUT.writeBytes(padSTRING('R',"-",10));
							
							if(M_rstRSSET.getString("SW_OUTTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
							else D_OUT.writeBytes(padSTRING('R',"-",11));

							if(!M_rstRSSET.getString("SW_LVECD").equals(""))
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_LVECD"),8));
							else D_OUT.writeBytes(padSTRING('R',"-",8));
							
							if(fmtHHMMSS.parse(M_rstRSSET.getString("sw_actwk")).compareTo(fmtHHMMSS.parse("05:00:00"))<0 && fmtHHMMSS.parse(M_rstRSSET.getString("sw_actwk")).compareTo(fmtHHMMSS.parse("03:00:00"))>0)
								D_OUT.writeBytes(padSTRING('R',"half day",11));
							crtNWLIN();
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}
			
			////report For Presentee
			if(cmbRPTYP.getSelectedItem().toString().equals("Presentee Report") || cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)") || cmbRPTYP.getSelectedItem().toString().equals("Daily Perfomance Report"))
			{
				String L_str0830 = "";
				if(cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)"))
					L_str0830 = " and SW_ACTWK < '"+txtTMLIM.getText().trim()+"' ";
					//L_str0830 = " and SW_ACTWK < '08.30.00' ";
																
				M_strSQLQRY  =" SELECT  DISTINCT SW_INCTM,SW_OUTTM,isnull(SW_ACTWK,'00:00') SW_ACTWK,SW_INCST,SW_OUTST,SW_WRKDT,SW_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,isnull(EP_EMPCT,'') EP_EMPCT,EP_DPTCD,EP_DPTNM,SW_WRKSH,isnull(SW_LVECD,'') SW_LVECD";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U' AND EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO AND SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				if(!cmbRPTYP.getSelectedItem().toString().equals("Daily Perfomance Report"))
				{
					M_strSQLQRY +=" and (SW_INCTM is not null and SW_OUTTM is not null)";
					M_strSQLQRY +=" AND isnull(SW_LVECD,'') = '' "+L_str0830;
				}
				M_strSQLQRY +=" and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_EMPNO";
				
				System.out.println("Query Presentee Report>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						
							if(!strOLD_WRKDT.equals(strNEW_WRKDT))
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
								strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
								strOLD_DPTCD="";
								crtNWLIN();
							}
							if(!strOLD_DPTCD.equals(strNEW_DPTCD))
							{
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
								strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							}
							else D_OUT.writeBytes(padSTRING('R',"",19)); 
							
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),4));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),6));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),20));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
							
							if(M_rstRSSET.getTime("SW_INCTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
							else
								D_OUT.writeBytes(padSTRING('R',"-",10));
							
							if(M_rstRSSET.getTime("SW_OUTTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
							else 
								D_OUT.writeBytes(padSTRING('R',"-",11));
							
							if(!M_rstRSSET.getString("SW_LVECD").equals(""))
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_LVECD"),8));
							else D_OUT.writeBytes(padSTRING('R',"-",8));
							
							if(M_rstRSSET.getString("SW_ACTWK")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_ACTWK").substring(0,5),8));
							else D_OUT.writeBytes(padSTRING('R',"-",8));
							
							String L_strSHFHR = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")));
							if(M_rstRSSET.getString("SW_ACTWK")!=null)
							{
								if(fmtHHMM.parse(M_rstRSSET.getString("SW_ACTWK").substring(0,5)).compareTo(fmtHHMM.parse(L_strSHFHR))<0)
									D_OUT.writeBytes(padSTRING('R',subTIME(L_strSHFHR,M_rstRSSET.getString("SW_ACTWK").substring(0,5)),8));
								else
									D_OUT.writeBytes(padSTRING('R',"-",8));
								if(fmtHHMM.parse(M_rstRSSET.getString("SW_ACTWK").substring(0,5)).compareTo(fmtHHMM.parse(L_strSHFHR))>0)
									D_OUT.writeBytes(padSTRING('R',subTIME(M_rstRSSET.getString("SW_ACTWK").substring(0,5),L_strSHFHR),8));
								else
									D_OUT.writeBytes(padSTRING('R',"-",8));
							}
							else D_OUT.writeBytes(padSTRING('R',"-",16));
							
							if(fmtHHMMSS.parse(M_rstRSSET.getString("sw_actwk")).compareTo(fmtHHMMSS.parse("05:00:00"))<0 && fmtHHMMSS.parse(M_rstRSSET.getString("sw_actwk")).compareTo(fmtHHMMSS.parse("03:00:00"))>0)
								D_OUT.writeBytes(padSTRING('R',"half day",17));
							else if(fmtHHMMSS.parse(M_rstRSSET.getString("sw_actwk")).compareTo(fmtHHMMSS.parse("00:00:00"))!=0
									&& fmtHHMMSS.parse(M_rstRSSET.getString("sw_actwk")).compareTo(fmtHHMMSS.parse("03:00:00"))<0)
								D_OUT.writeBytes(padSTRING('R',"Wrked Hrs < 3 Hrs",17));
							crtNWLIN();
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}
			
			////report For Late Comming Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Late Comming Report"))
			{
				String L_strLTEBY="";
				M_strSQLQRY  =" SELECT  DISTINCT SW_INCTM,SW_OUTTM,SW_INCST,isnull(SW_ACTWK,'00:00') SW_ACTWK,SW_WRKDT,SW_EMPNO,EP_LSTNM +' '+ SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,isnull(EP_EMPCT,'') EP_EMPCT,EP_DPTCD,EP_DPTNM,SW_WRKSH,isnull(SW_LVECD,'') SW_LVECD";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U' AND EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO and (SW_INCTM is not null and SW_OUTTM is not null) AND SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				//M_strSQLQRY +=" AND isnull(SW_LVECD,'') = '' ";
				M_strSQLQRY +=" and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_EMPNO";
				
				System.out.println("Query Late Comming Report>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						L_strLTEBY="";
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");

						if(M_rstRSSET.getTimestamp("SW_INCST").compareTo(M_rstRSSET.getTimestamp("SW_INCTM"))<0)
  							L_strLTEBY = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")));						
						if(!L_strLTEBY.equals("") && fmtHHMM.parse(L_strLTEBY).compareTo(fmtHHMM.parse("00:15"))>0)
						{		
							if(!strOLD_WRKDT.equals(strNEW_WRKDT))
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
								strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
								strOLD_DPTCD="";
								crtNWLIN();
							}
							if(!strOLD_DPTCD.equals(strNEW_DPTCD))
							{
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
								strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							}
							else D_OUT.writeBytes(padSTRING('R',"",19)); 
							
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),4));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),6));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),20));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
							
							if(M_rstRSSET.getTime("SW_INCTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
							else
								D_OUT.writeBytes(padSTRING('R',"-",10));
							
							if(M_rstRSSET.getTime("SW_OUTTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
							else 
								D_OUT.writeBytes(padSTRING('R',"-",11));
							
							D_OUT.writeBytes(padSTRING('R',L_strLTEBY,15));
							crtNWLIN();
						}	
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}

			////report For Early Going Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Early Going Report"))
			{
				String L_strERLBY="";
				M_strSQLQRY  =" SELECT  DISTINCT SW_INCTM,SW_OUTTM,SW_OUTST,isnull(SW_ACTWK,'00:00') SW_ACTWK,SW_WRKDT,SW_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,isnull(EP_EMPCT,'') EP_EMPCT,EP_DPTCD,EP_DPTNM,SW_WRKSH,isnull(SW_LVECD,'') SW_LVECD";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U' AND EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO and (SW_INCTM is not null and SW_OUTTM is not null) AND SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				//M_strSQLQRY +=" AND isnull(SW_LVECD,'') = '' ";
				M_strSQLQRY +=" and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_EMPNO";
				
				System.out.println("Early going Report>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						L_strERLBY="";
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						
						if(M_rstRSSET.getTimestamp("SW_OUTTM").compareTo(M_rstRSSET.getTimestamp("SW_OUTST"))<0)
  							L_strERLBY = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTTM")));
						if(!L_strERLBY.equals("") && fmtHHMM.parse(L_strERLBY).compareTo(fmtHHMM.parse("00:15"))>0)
						{	
							if(!strOLD_WRKDT.equals(strNEW_WRKDT))
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
								strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
								strOLD_DPTCD="";
								crtNWLIN();
							}
							if(!strOLD_DPTCD.equals(strNEW_DPTCD))
							{
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
								strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							}
							else D_OUT.writeBytes(padSTRING('R',"",19)); 
							
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),4));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),6));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),20));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
							
							if(M_rstRSSET.getTime("SW_INCTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
							else 
								D_OUT.writeBytes(padSTRING('R',"-",10));
							
							if(M_rstRSSET.getTime("SW_OUTTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
							else 
								D_OUT.writeBytes(padSTRING('R',"-",11));
							
							D_OUT.writeBytes(padSTRING('R',L_strERLBY,15));
							crtNWLIN();
						}	
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}
			
			////report For Weekly Off Performance
			if(cmbRPTYP.getSelectedItem().toString().equals("Weekly Off Performance Report"))
			{
				String L_strERLBY="";
				M_strSQLQRY  =" SELECT  DISTINCT SW_INCTM,SW_OUTTM,SW_WRKDT,SW_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,isnull(EP_EMPCT,'') EP_EMPCT,EP_DPTCD,EP_DPTNM,SW_WRKSH,isnull(SW_LVECD,'') SW_LVECD";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U' AND EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO and (SW_INCTM is not null or SW_OUTTM is not null) AND SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				M_strSQLQRY +=" AND isnull(SW_LVECD,'') = 'WO'";
				M_strSQLQRY +="  and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_EMPNO";
				
				System.out.println("Weekly Off Performance>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						L_strERLBY="";
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						if(!strOLD_WRKDT.equals(strNEW_WRKDT))
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
							strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
							strOLD_DPTCD="";
							crtNWLIN();
						}
						if(!strOLD_DPTCD.equals(strNEW_DPTCD))
						{
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
							strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						}
						else D_OUT.writeBytes(padSTRING('R',"",19)); 
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),4));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),6));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),20));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
						
						if(M_rstRSSET.getTime("SW_INCTM")!=null)
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
						else 
							D_OUT.writeBytes(padSTRING('R',"-",10));
						
						if(M_rstRSSET.getTime("SW_OUTTM")!=null)
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
						else 
							D_OUT.writeBytes(padSTRING('R',"-",11));
						crtNWLIN();
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}
			
			////report For Sanctioned Leave Performance
			if(cmbRPTYP.getSelectedItem().toString().equals("Sanctioned Leave Performance Report"))
			{
				String L_strERLBY="";
				M_strSQLQRY  =" SELECT  DISTINCT SW_INCTM,SW_OUTTM,SW_WRKDT,SW_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,isnull(EP_EMPCT,'') EP_EMPCT,EP_DPTCD,EP_DPTNM,SW_WRKSH,isnull(SW_LVECD,'') SW_LVECD";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U' AND EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO and (SW_INCTM is not null or SW_OUTTM is not null) AND SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				M_strSQLQRY +=" AND isnull(SW_LVECD,'') not in('WO','')";
				M_strSQLQRY +=" and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_EMPNO";
				
				System.out.println("Sanctioned Leave Performance>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						L_strERLBY="";
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						
						if(!strOLD_WRKDT.equals(strNEW_WRKDT))
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
							strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
							strOLD_DPTCD="";
							crtNWLIN();
						}
						if(!strOLD_DPTCD.equals(strNEW_DPTCD))
						{
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
							strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						}
						else D_OUT.writeBytes(padSTRING('R',"",19)); 
							
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),4));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),6));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),20));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
						if(M_rstRSSET.getTime("SW_INCTM")!=null)
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
						else 
							D_OUT.writeBytes(padSTRING('R',"-",10));
						
						if(M_rstRSSET.getTime("SW_OUTTM")!=null)
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
						else 
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
						
						if(!M_rstRSSET.getString("SW_LVECD").equals(""))
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_LVECD"),8));
						else D_OUT.writeBytes(padSTRING('R',"",8)); 
						crtNWLIN();
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}			
			
			////Exception Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Exception Report"))
			{
				String L_strEXPFL="00";
				String L_strINCST="",L_strOUTST="";
				boolean flgCHGTM;				
				String L_strCHGTM="";////var to calculate diff between standard in time and actual intime.				
				M_strSQLQRY  =" SELECT  DISTINCT SW_WRKDT,SW_EMPNO,SW_SHRWK,SW_ACTWK,EP_EMPCT,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,EP_DPTCD,EP_DPTNM,SW_WRKSH,SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,SW_EXCFL,isnull(SW_LVECD,' ') SW_LVECD,SW_STSFL";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  EP_STSFL <> 'U' AND EP_CMPCD = SW_CMPCD AND EP_EMPNO=SW_EMPNO AND  SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				M_strSQLQRY +=" and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') ";				
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_EMPNO";
				
				System.out.println("Query Exception Report>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				
			
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						flgCHGTM=false;
						L_strEXPFL="00";
						L_strCHGTM="";						

						////to check whether punching time matches with assg. shift
						if(M_rstRSSET.getTimestamp("SW_INCTM") != null)
						{	
							if(M_rstRSSET.getTimestamp("SW_INCTM").compareTo(M_rstRSSET.getTimestamp("SW_INCST"))>0)
								L_strCHGTM = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")));
							else if(M_rstRSSET.getTimestamp("SW_INCST").compareTo(M_rstRSSET.getTimestamp("SW_INCTM"))>0)
								L_strCHGTM = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")));
						}	
						if(!L_strCHGTM.equals("") && fmtHHMM.parse(L_strCHGTM).compareTo(fmtHHMM.parse("01:00"))>0)
			 				flgCHGTM=true;
							
						////////////////////////////////////////////////////////////
				
						if(M_rstRSSET.getString("SW_STSFL").equals("9"))/// Authorised record
							L_strEXPFL="00";
						else if(M_rstRSSET.getString("SW_STSFL").equals("9"))/// Authorised record
							L_strEXPFL="00";
						else if(M_rstRSSET.getString("SW_INCTM") == null && M_rstRSSET.getString("SW_OUTTM") == null && M_rstRSSET.getString("SW_LVECD").equals("WO") && !M_rstRSSET.getString("SW_STSFL").equals("2"))/// Valid weekly off
							L_strEXPFL="00";
						else if(M_rstRSSET.getString("SW_INCTM")==null)///in punching missing
							L_strEXPFL="01";
						else if(M_rstRSSET.getString("SW_OUTTM")==null)///out punching missing
							L_strEXPFL="02";
						else if(flgCHGTM)////Punching time does not match with assigned Shift
							L_strEXPFL="03";
						else if((M_rstRSSET.getString("SW_INCTM")!=null || M_rstRSSET.getString("SW_OUTTM")!=null) && M_rstRSSET.getString("SW_LVECD").equals("WO"))///working on weekly off
							L_strEXPFL="04";
						else if((M_rstRSSET.getString("SW_INCTM")!=null || M_rstRSSET.getString("SW_OUTTM")!=null) && (M_rstRSSET.getString("SW_LVECD").equals("CL") || M_rstRSSET.getString("SW_LVECD").equals("PL") || M_rstRSSET.getString("SW_LVECD").equals("SL")))///working on sanc leave
							L_strEXPFL="05";
						else if((M_rstRSSET.getString("SW_INCTM")!=null || M_rstRSSET.getString("SW_OUTTM")!=null) && M_rstRSSET.getString("SW_LVECD").equals("PH"))///working on RH
							L_strEXPFL="06";
						else if(fmtHHMMSS.parse(M_rstRSSET.getString("SW_SHRWK")).compareTo(fmtHHMMSS.parse("00:15:00"))>0)////Partial working
							L_strEXPFL="10";
						else if((M_rstRSSET.getString("SW_INCTM")==null && M_rstRSSET.getString("SW_OUTTM")==null) && (!M_rstRSSET.getString("SW_LVECD").equals("WO") && M_rstRSSET.getString("SW_LVECD").length()==2 && !M_rstRSSET.getString("SW_STSFL").equals("9")))///Leave Confirmation
							L_strEXPFL="88";
						else if(M_rstRSSET.getString("SW_WRKSH").equals("") || M_rstRSSET.getString("SW_WRKSH").equals("X") || M_rstRSSET.getString("SW_STSFL").equals("2")) /// Working shift not defined
							L_strEXPFL="99";
						else if((M_rstRSSET.getString("SW_INCTM") == null || M_rstRSSET.getString("SW_OUTTM") == null) && !M_rstRSSET.getString("SW_LVECD").equals("WO"))/// Absent Employees
							L_strEXPFL="99";
						
						if(!L_strEXPFL.equals("00"))
						{
							strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
							strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							if(!strOLD_WRKDT.equals(strNEW_WRKDT))
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
								strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
								strOLD_DPTCD="";
								crtNWLIN();
							}
							if(!strOLD_DPTCD.equals(strNEW_DPTCD))
							{
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
								strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							}
							else D_OUT.writeBytes(padSTRING('R',"",19)); 
							//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
							//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),3));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),5));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),22));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
							if(M_rstRSSET.getString("SW_INCTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
							else
								D_OUT.writeBytes(padSTRING('R',"",10));

							if(M_rstRSSET.getString("SW_OUTTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
							else
								D_OUT.writeBytes(padSTRING('R',"",11));
							
							
							if(M_rstRSSET.getString("SW_ACTWK")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_ACTWK").substring(0,5),8));
							else D_OUT.writeBytes(padSTRING('R',"-",8));
							
							String L_strSHFHR = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")));
							if(M_rstRSSET.getString("SW_ACTWK")!=null)
							{
								if(fmtHHMM.parse(M_rstRSSET.getString("SW_ACTWK").substring(0,5)).compareTo(fmtHHMM.parse(L_strSHFHR))<0)
									D_OUT.writeBytes(padSTRING('R',subTIME(L_strSHFHR,M_rstRSSET.getString("SW_ACTWK").substring(0,5)),8));
								else
									D_OUT.writeBytes(padSTRING('R',"-",8));
								if(fmtHHMM.parse(M_rstRSSET.getString("SW_ACTWK").substring(0,5)).compareTo(fmtHHMM.parse(L_strSHFHR))>0)
									D_OUT.writeBytes(padSTRING('R',subTIME(M_rstRSSET.getString("SW_ACTWK").substring(0,5),L_strSHFHR),8));
								else
									D_OUT.writeBytes(padSTRING('R',"-",8));
							}
							else D_OUT.writeBytes(padSTRING('R',"-",16));
							
							
							
							D_OUT.writeBytes(padSTRING('R',L_strEXPFL,12));

							crtNWLIN();
						}
						L_strEXPFL="00";
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			
				////display Exception Flag and its meaning
				M_strSQLQRY  = " Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HR01EXC'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODCD"),5));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("CMT_CODDS"),50));
					}	
				}				
			}				
			
			////report For OverTime

			if(cmbRPTYP.getSelectedItem().toString().equals("Over Time Report"))
			{
				M_strSQLQRY  =" SELECT  DISTINCT SW_WRKDT,SW_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,EP_DPTCD,EP_DPTNM,SW_WRKSH,SW_INCTM,SW_OUTTM,SW_SHRWK,SW_EXTWK,SW_OVTWK,SW_INCST,SW_OUTST";
				M_strSQLQRY +=" FROM HR_EPMST,HR_SWMST";
				M_strSQLQRY +=" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U' AND EP_CMPCD = SW_CMPCD AND EP_EMPNO=SW_EMPNO AND SUBSTRING(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' AND SW_WRKDT  between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPNM";
				
				System.out.println("Query Over Time Report>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");						
						if(M_rstRSSET.getString("SW_OVTWK")!=null && Integer.parseInt(M_rstRSSET.getString("SW_OVTWK").substring(0,2))>=1)
						{	
							if(!strOLD_WRKDT.equals(strNEW_WRKDT))
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
								strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
								strOLD_DPTCD="";
								crtNWLIN();
							}
							if(!strOLD_DPTCD.equals(strNEW_DPTCD))
							{
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
								strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							}
							else D_OUT.writeBytes(padSTRING('R',"",19)); 
							//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
							//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),5));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),16));

							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),5));
							if(M_rstRSSET.getString("SW_INCTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),8));
							else
								D_OUT.writeBytes(padSTRING('R',"",8));

							if(M_rstRSSET.getString("SW_OUTTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
							else
								D_OUT.writeBytes(padSTRING('R',"",11));
							
							
							String L_strEXTWK1="";
							String L_strEXTWK2="";
							if(M_rstRSSET.getTimestamp("SW_INCTM") != null  && M_rstRSSET.getTimestamp("SW_INCST") != null)
							{
								if(M_rstRSSET.getTimestamp("SW_INCTM").compareTo(M_rstRSSET.getTimestamp("SW_INCST"))<0)
									L_strEXTWK1 = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")));
								D_OUT.writeBytes(padSTRING('R',L_strEXTWK1,9));
							}	
							else D_OUT.writeBytes(padSTRING('R',"",9));
							if(M_rstRSSET.getTimestamp("SW_OUTTM") != null  && M_rstRSSET.getTimestamp("SW_OUTST") != null)
							{
								if(M_rstRSSET.getTimestamp("SW_OUTTM").compareTo(M_rstRSSET.getTimestamp("SW_OUTST"))>0)
  									L_strEXTWK2 = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTTM")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")));
								D_OUT.writeBytes(padSTRING('R',L_strEXTWK2,10));
							}							
							else D_OUT.writeBytes(padSTRING('R',"",10));							
							
							if(M_rstRSSET.getString("SW_OVTWK")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OVTWK").substring(0,5),9));
							else
								D_OUT.writeBytes(padSTRING('R',"",9));

							
							crtNWLIN();
						}	
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}				
			crtNWLIN();
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			if(M_rstRSSET==null)
			{
				M_rstRSSET.close();
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

    /**
    Method to add time given in second parameter to the time in first parameter 
    and returns the result in HH:MM format
	@param P_strSTRTM String argument to pass Starting Time.
	@param P_strNEWTM String argument to pass New Time.
    */
    private String subTIME(String P_strSTRTM,String P_strNEWTM)
    {
	    String L_strRETSTR = "";
		try
        {
            if (P_strSTRTM.equals(""))  P_strSTRTM = "00:00";
            if (P_strNEWTM.equals(""))  P_strNEWTM = "00:00";            
            int  L_intSTRLN = P_strSTRTM.trim().length();
            int  L_intNEWLN = P_strNEWTM.trim().length();
            int  L_intSTRCL = P_strSTRTM.indexOf(":");
            int  L_intNEWCL = P_strNEWTM.indexOf(":");                
            int  L_intSTRMN = Integer.parseInt(P_strSTRTM.substring(0,L_intSTRCL))*60+Integer.parseInt(P_strSTRTM.substring(L_intSTRCL+1,L_intSTRLN));
            int  L_intNEWMN = Integer.parseInt(P_strNEWTM.substring(0,L_intNEWCL))*60+Integer.parseInt(P_strNEWTM.substring(L_intNEWCL+1,L_intNEWLN));
            int  L_intTOTHR = (L_intSTRMN-L_intNEWMN) / 60;
            int  L_intTOTMN = (L_intSTRMN-L_intNEWMN)- ((L_intSTRMN-L_intNEWMN)/60)*60;
            String L_strTOTHR1 = "0000"+String.valueOf(L_intTOTHR).trim();
            String L_strTOTMN1 = "0000"+String.valueOf(L_intTOTMN).trim();                
            int L_intLENHR = L_strTOTHR1.length();
            int L_intLENMN = L_strTOTMN1.length();
            int L_intTOTCL;
            if (L_intTOTHR < 100)  
                L_intTOTCL = 2;
             else if (L_intTOTHR < 1000)
                    L_intTOTCL = 3;
                else 
                    L_intTOTCL = 4;
           L_strRETSTR = L_strTOTHR1.substring(L_intLENHR-L_intTOTCL,L_intLENHR)+":"+L_strTOTMN1.substring(L_intLENMN-2,L_intLENMN);                
           return  L_strRETSTR;
		}
        catch(Exception L_EX)
        {
		    setMSG(L_EX, "subTIME");
    		return "";
		}            
    }		
	/**
    Method to Calculate the differance two Date & Time.
	@param P_strFINTM String argument to Final Time.
	@param P_strINITM String argument to Initial Time.
    */
	public String calTIME(String P_strFINTM,String P_strINITM)
	{
		String L_strDIFTM = "";
		try
		{
			int L_intYRS,L_intMTH,L_intDAY,L_intHRS,L_intMIN;
			int L_intYRS1,L_intMTH1,L_intDAY1,L_intHRS1,L_intMIN1;
			int L_intYRS2,L_intMTH2,L_intDAY2,L_intHRS2,L_intMIN2;
			String L_strHOUR,L_strMINT;			
			if(P_strFINTM.equals("") || P_strINITM.equals(""))
				return L_strDIFTM;			
			// Seperating year,month,day,hour & minute from Final time
			L_intYRS1 = Integer.parseInt(P_strFINTM.substring(6,10));
			L_intMTH1 = Integer.parseInt(P_strFINTM.substring(3,5));
			L_intDAY1 = Integer.parseInt(P_strFINTM.substring(0,2));
			L_intHRS1 = Integer.parseInt(P_strFINTM.substring(11,13));
			L_intMIN1 = Integer.parseInt(P_strFINTM.substring(14));			
			// Seperating year,month,day,hour & minute from Initial time
			L_intYRS2 = Integer.parseInt(P_strINITM.substring(6,10));
			L_intMTH2 = Integer.parseInt(P_strINITM.substring(3,5));
			L_intDAY2 = Integer.parseInt(P_strINITM.substring(0,2));
			L_intHRS2 = Integer.parseInt(P_strINITM.substring(11,13));
			L_intMIN2 = Integer.parseInt(P_strINITM.substring(14));			
			L_intMIN = L_intMIN1 - L_intMIN2;
			L_intHRS = L_intHRS1 - L_intHRS2;			
			// Checking for leap year
			if(L_intYRS1%4 == 0)
				arrDAYS[1] = "29";
			else
				arrDAYS[1] = "28";			
			// Final date is of next month
			if(L_intMTH1 > L_intMTH2)
			{
				for(int i = L_intMTH2;i < L_intMTH1;i++)
					L_intDAY1 += Integer.parseInt(arrDAYS[i-1]);
			}			
			L_intDAY = L_intDAY1 - L_intDAY2;			
			if(L_intMIN < 0)
			{
				L_intMIN += 60;
				L_intHRS--;
			}
			if(L_intHRS < 0)
			{
				L_intHRS += 24;
				L_intDAY--;
			}
			if(L_intDAY > 0)
				L_intHRS += L_intDAY*24;			
			L_strHOUR = String.valueOf(L_intHRS);
			L_strMINT = String.valueOf(L_intMIN);
			if(L_strHOUR.length() < 2)
				L_strHOUR = "0" + L_strHOUR;
			if(L_strMINT.length() < 2)
				L_strMINT = "0" + L_strMINT;
			L_strDIFTM = L_strHOUR + ":" + L_strMINT;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calTIME");
		}
		//System.out.println("para 1 : "+P_strFINTM+"Para 2 : "+P_strINITM+"  Ret.Val : "+L_strDIFTM);
		return L_strDIFTM;
	}	
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >70)
				{
					genRPFTR();
					genRPHDR();
				}
			}	
			else if(cl_dat.M_intLINNO_pbst >60)
			{		
				genRPFTR();
				genRPHDR();			
			}			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

	void genRPHDR()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
			
				
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title>Attendance Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();

			////for Absentee Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Absentee  Report"))
				crtHDRFOR("Absentee Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   Leave   Remarks");	
			////for Presentee Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Presentee Report") || cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)"))
				crtHDRFOR("Presentee Report "+(cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)") ? " (below Entered Hrs working)" : "")+" From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   Leave   ActWrk  LessWrk ExtWrk   Remarks");
			if(cmbRPTYP.getSelectedItem().toString().equals("Daily Perfomance Report"))
				crtHDRFOR("Daily Perfomance Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   Leave   ActWrk  LessWrk ExtWrk   Remarks");
			////for Late Comming Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Late Comming Report"))
				crtHDRFOR("Late Comming Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   Late Comming   Remarks");
			////for Early Going Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Early Going Report"))
				crtHDRFOR("Early Going Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   Early Going   Remarks");
			////Weekly Off Performance Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Weekly Off Performance Report"))
				crtHDRFOR("Weekly Off Performance Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   Remarks");			
			////Sanctioned Leave Performance Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Sanctioned Leave Performance Report"))
				crtHDRFOR("Sanctioned Leave Performance Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   Leave   Remarks");
			////Exception Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Exception Report"))
				crtHDRFOR("Exception Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   ActWrk  LessWrk ExtWrk  Exception   Remark");	
			////for OverTime Report
			if(cmbRPTYP.getSelectedItem().toString().equals("Over Time Report"))
				crtHDRFOR("OverTime Report From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code & Name   Employee No & Name   Shft In-Time Out-Time   EarlyBy   LateBy   OVT HRS  Remark ");	
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			if(cmbRPTYP.getSelectedItem().toString().equals("Presentee Report") || cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)") ||
			   cmbRPTYP.getSelectedItem().toString().equals("Exception Report") || cmbRPTYP.getSelectedItem().toString().equals("Daily Perfomance Report"))
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------");
			else
				D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}	

	/**method that creates header for selected index
	 */
	private void crtHDRFOR(String LP_HDRR00, String LP_HDRKY)
	{
		try
		{
    			D_OUT.writeBytes(padSTRING('R',LP_HDRR00,75));
    			D_OUT.writeBytes(padSTRING('L',"Page No    : ",15));
				D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    			prnFMTCHR(D_OUT,M_strNOBOLD);

				crtNWLIN();
    			crtNWLIN();
				if(cmbRPTYP.getSelectedItem().toString().equals("Presentee Report") || cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)") ||
				   cmbRPTYP.getSelectedItem().toString().equals("Exception Report") || cmbRPTYP.getSelectedItem().toString().equals("Daily Perfomance Report"))
					D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------");
				else
					D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes(LP_HDRKY);
				crtNWLIN();
				if(cmbRPTYP.getSelectedItem().toString().equals("Presentee Report") || cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)") ||
				   cmbRPTYP.getSelectedItem().toString().equals("Exception Report") || cmbRPTYP.getSelectedItem().toString().equals("Daily Perfomance Report"))
					D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------");
				else
					D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
				crtNWLIN();
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"crtHDRFOR");
		}	
	}	

	
	boolean vldDATA()
	{
		try
		{
			if(!flgALLDPT && txtDPTCD.getText().length()==0)
			{
				txtDPTCD.requestFocus();
				setMSG("Please Enter Department...",'E');
				return false;
			}
			if(txtSTRDT.getText().length()<10)
			{
				txtSTRDT.requestFocus();
				setMSG("Please Enter From Date",'E');
				return false;
			}	
			if(txtENDDT.getText().length()<10)
			{
				txtENDDT.requestFocus();
				setMSG("Please Enter To Date",'E');
				return false;
			}		
			if(cmbRPTYP.getSelectedIndex()==0)
			{
				cmbRPTYP.requestFocus();
				setMSG("Please Enter Type Of Report to be Display",'E');
				return false;
			}	
			if(cmbRPTYP.getSelectedItem().toString().equals("Less Working (below Entered Hrs)"))
			{
				if(txtTMLIM.getText().length()==0)
				{
					setMSG("Please Enter Time Limt to..",'E');
					txtTMLIM.requestFocus();
					return false;
				}
			}	
			
		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA()");
		}
		return true;
	}
	
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rpadt.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpadt.doc";
				
			genRPTFL();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				*/	
				}	
				else 
			    {    
					Runtime r = Runtime.getRuntime();
					Process p = null;					    
					p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
			    if(M_rdbHTML.isSelected())
			        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			    else
			        p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				
			}
			/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}*/
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		
	}
	
	
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
				return true;
				
			if(input == txtDPTCD)
			{
				//M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
				//M_strSQLQRY+=" and CMT_STSFL <> 'X' and CMT_CODCD='"+txtDPTCD.getText().trim()+"'";
				
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CODCD='"+txtDPTCD.getText().trim()+"' and CMT_CGSTP = 'COXXDPT' "+ (flgALLDPT ? "" : " and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO ='"+cl_dat.M_strEMPNO_pbst+"')");
					
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Department Code",'E');
					return false;
				}			
			}	
			if(input == txtEMPNO)
			{
				if(!flgALLDPT && txtDPTCD.getText().length()==0)
				{
					setMSG("Please Enter Department Code first...",'E');
					txtDPTCD.requestFocus();
					txtEMPNO.setText("");
					return false;
				}
				M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM + ' '+ SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText().trim()+"' and EP_STSFL <> 'U' ";
				if(txtDPTCD.getText().length()>0)
					M_strSQLQRY +=" and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
				//System.out.println("<<<<"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Employee Code",'E');
					return false;
				}			
			}	
			if(input == txtSTRDT)
			{
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
				{
					setMSG("Date can not be greater than todays date..",'E');
					return false;
				}
			}
			if(input == txtENDDT)
			{
				if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
				{
					setMSG("Date can not be greater than todays date..",'E');
					return false;
				}
				else if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText())) > 0)
				{
					setMSG("From Date can not be greater than To date..",'E');
					return false;
				}
			}	
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
}



