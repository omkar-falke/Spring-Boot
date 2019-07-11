/**
System Name:Human Resource System.
 
Program Name: Exit Pass Authorization
Author : SSG
Purpose : This module displays Exit Pass Authorization & UnAuthorization report
 
Source Directory: f:\source\splerp3\hr_rpexa.java                         
Executable Directory: F:\exec\splerp3\hr_rpexa.class

 
List of tables used:
Table Name		Primary key											Operation done
															Insert	Update	   Query    Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
HR_EXTRN		EX_DOCNO, EX_CMPCD                             	               /         
                 
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed in Report:
Column Name		Table name		Type/Size		Description
----------------------------------------------------------------------------------------------------------------------------------------------------- EX_WRKDT        HR_EXTRN 		Date			Work Date
EX_DOCDT		HR_EXTRN 		Date  	        DOC Date
EX_DOCNO		HR_EXTRN		Varchar(8)		DOC No	
EX_EMPNO		HR_EXTRN 		Varchar(4)  	Employee No
EP_EMPNO		HR_EPMST 		Varchar(50)  	Employee Name
EX_INCTM        HR_EXTRN        TIMESTAMP       Actual Out Time
EX_OUTTM        HR_EXTRN        TIMESTAMP       Actual In Time	
EX_EINTM        HR_EXTRN        TIMESTAMP       Expected Out Time
EX_EOTTM        HR_EXTRN        TIMESTAMP       Expected In Time
SWT_INCTM       HR_SWTRN        TIMESTAMP       Out Time
SWT_OUTTM       HR_SWTRN        TIMESTAMP       In Time		
EX_AOTTM        HR_EXTRN        Time            Exit Period
EX_OFPFL		HR_EXTRN 		Varchar(1)  	Official /Personal
EX_SHFCD		HR_EXTRN 		Varchar(1)  	Wrk Shift
EX_AUTBY		HR_EXTRN 		Varchar(3)  	Auth BY
-----------------------------------------------------------------------------------------------------------------------------------------------------


List of fields with help facility: 
Field Name	Display Description		    		Display Columns							Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
TxtEMPNO	Employee No,Employee NAME			EP_EMPNO,EP_EMPNM       				HR_EPMTS
TxtDOCNO	DOC No, DOC Date ,off/per flag  	EX_DOCNO,EX_DOCDT,EX_OFPFL,
			shift,Out time,In time,Auth by		EX_SHFCD,EX_EOTTM,EX_EINTM,EX_AUTBY		HR_EXTRN
-----------------------------------------------------------------------------------------------------------------------------------------------------


Validations & Other Information:
    - Enter valid Employee No & Department.
  	- From Date must be Smaller Than Or Equal to To Date.
 	- To Date must not be Grater Than Todays Date.
    - To Date must be greater than Or Equal to From Date.
    
Requirement :
	- User can be display Date wise ,Dept wise & employee wise record .
	- If user select UnAuhorized Exit Pass option, then display exit pass entry records of stsfl '2' from hr_extrn in report.
	- If select Auhorized Exit Pass option ,it will be display  Authorized  records (i.e. stsfl='3')
	- If select All Exit Pass option while display all record of exit pass(i.e. stsfl='0','1','2','3')
	- while select Official button, it will be display only Official Exit Pass in report & select Personal button to dispaly only personal Exit Pass otherwise display all.
	- Display record in Report order by date, Dept Name,Off/Pers & Emp No. 
	
**/


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import java.util.StringTokenizer;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*

 */

class hr_rpexa extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpexa.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private JTextField txtDPTCD;
	private JTextField txtEMPNO;
	private JTextField txtSTRDT;			
	private JTextField txtENDDT;
	private JLabel lblDPTNM,lblEMPNM;
	private JComboBox cmbRPTTP;
	
	private JRadioButton  rdbOFFCL;
	private JRadioButton  rdbPRSNL;
	private JRadioButton  rdbALLEP;
	private ButtonGroup   btgOFPFL;
	private SimpleDateFormat M_fmtHHMM = new SimpleDateFormat("HH:mm"); 
	hr_rpexa()		/*  Constructor   */
	{
		super(1);
		try
		{
			setMatrix(20,20);
			
			add(new JLabel("Report Type"),3,7,1,2,this,'L');
    		add(cmbRPTTP = new JComboBox(),3,9,1,4,this,'L');
    		cmbRPTTP.addItem("UnAuthorized Exit Passes");
			cmbRPTTP.addItem("Authorized Exit Passes");
			cmbRPTTP.addItem("All Exit Passes");

			add(rdbOFFCL=new JRadioButton("Official"),4,7,1,2,this,'L');
			add(rdbPRSNL=new JRadioButton("Personal"),4,9,1,2,this,'L');
			add(rdbALLEP=new JRadioButton("All"),4,11,1,2,this,'L');
			btgOFPFL=new ButtonGroup();
			btgOFPFL.add(rdbOFFCL); 
			btgOFPFL.add(rdbPRSNL); 
			btgOFPFL.add(rdbALLEP); 
			rdbALLEP.setSelected(true);
			
			add(new JLabel("Department"),5,7,1,2,this,'L');
			add(txtDPTCD= new TxtLimit(10),5,9,1,2,this,'L');
			add(lblDPTNM= new JLabel(),5,11,1,5,this,'L');
						
			add(new JLabel("Emp No"),6,7,1,2,this,'L');
			add(txtEMPNO = new TxtLimit(4),6,9,1,2,this,'L');
			add(lblEMPNM= new JLabel(),6,11,1,5,this,'L');
			
			add(new JLabel("From Date"),7,7,1,2,this,'L');
			add(txtSTRDT = new TxtDate(),7,9,1,2,this,'L');
						
			add(new JLabel("To Date"),8,7,1,2,this,'L');
			add(txtENDDT = new TxtDate(),8,9,1,2,this,'L');
			
			setENBL(false);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtDPTCD.setInputVerifier(oINPVF);
			txtEMPNO.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			
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
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtDPTCD.requestFocus();
					setENBL(true);
					
					if((txtSTRDT.getText().trim().length()==0) ||(txtENDDT.getText().trim().length()==0))
					{
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));      
						M_calLOCAL.add(Calendar.DATE,-1);    
						txtSTRDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						txtENDDT.setText("");
						setMSG("Please enter date to generate Report..",'N');
					}
				}
				else
					setENBL(false);
			}
			if(M_objSOURC == txtDPTCD)
			{
				if(txtDPTCD.getText().length()==0)
					lblDPTNM.setText("");
			}
			if(M_objSOURC == txtEMPNO)
			{
				if(txtEMPNO.getText().length()==0)
					lblEMPNM.setText("");
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is actionPerformed()");
		}		
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{
				if(M_objSOURC==txtDPTCD)		
				{
					M_strHLPFLD = "txtDPTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXDPT' order by CMT_CODCD ";
						
					//if(txtDPTCD.getText().length() >0)
					//	M_strSQLQRY += " AND CMT_CODCD like '"+txtDPTCD.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Department code","Department Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC==txtEMPNO)		
				{
					
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
						M_strHLPFLD = "txtEMPNO";
						String L_ARRHDR[] = {"Employee No","Employee Name"};
	    				M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
	    				if(txtDPTCD.getText().length()>0)
	    					M_strSQLQRY += " and EP_DPTCD='"+txtDPTCD.getText()+"'";
	    				if(txtEMPNO.getText().length() >0)
	    					M_strSQLQRY += " AND EP_EMPNO like '"+txtEMPNO.getText().trim()+"%'";
	    				M_strSQLQRY += " order by ep_empno";
	    				//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
	    				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
	    				setCursor(cl_dat.M_curDFSTS_pbst);
	    			
				}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				 if(M_objSOURC == txtDPTCD)
				 {
					txtEMPNO.requestFocus();
					setMSG("Enter the Employee Number or Press F1 to Select form List..",'N');
				 }
				 else if(M_objSOURC == txtEMPNO)
				 {
					 txtSTRDT.requestFocus();
						setMSG("Enter the From Date..",'N');
				 }
				 else if(M_objSOURC == txtSTRDT)
				 {
					 txtENDDT.requestFocus();
					 txtENDDT.setText(txtSTRDT.getText());
						setMSG("Enter the To Date..",'N');
				 }
				 else if(M_objSOURC == txtENDDT)
					 cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is KeyPressed");
		}	
	
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == txtDPTCD)
			 {
				txtDPTCD.requestFocus();
				setMSG("Enter the Dept Code or Press F1 to Select form List..",'N');
			 }
		}	
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
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
			else if(M_strHLPFLD.equals("txtEMPNO"))
			{
				  StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			      txtEMPNO.setText(L_STRTKN.nextToken());
				  lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
		}
		catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK");
		}
	}
	
	
   /**method to generate Authorize, UnAuthorize & all Exit pass report  **/
	void genRPTFL()
	{
		try
		{
			java.sql.Timestamp L_tmsINCTM=null,L_tmsOUTTM=null,L_tmsEOTTM=null,L_tmsEINTM=null,L_tmsINCTM_SW=null,L_tmsOUTTM_SW=null;
			java.sql.Time L_tmsEXTTM=null;
		
			String strNEW_DOCDT="",strOLD_DOCDT="",strNEW_DPTNM="",strOLD_DPTNM="",L_strAOTTM="";
		 
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			
			M_strSQLQRY= " SELECT EX_DOCDT,EX_EMPNO,EX_DOCNO,EX_REMDS,EX_EOTTM,EX_EINTM,EX_OUTTM,EX_INCTM,EX_AOTTM,EX_OFPFL,EX_AUTBY,EX_SHFCD,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '  + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ,EP_DPTCD,EP_DPTNM,EP_EMPCT,SW_INCTM,SW_OUTTM";
			M_strSQLQRY+= " from HR_EXTRN,HR_EPMST,HR_SWMST ";
			M_strSQLQRY+= " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO=EX_EMPNO AND EP_CMPCD=EX_CMPCD AND SW_CMPCD = EX_CMPCD AND SW_EMPNO=EX_EMPNO AND SW_WRKDT=EX_DOCDT AND isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
			if(cmbRPTTP.getSelectedIndex() == 0) //UnAuthorized
				M_strSQLQRY+= " AND EX_STSFL ='2'";
			if(cmbRPTTP.getSelectedIndex() == 1) //Authorized
				M_strSQLQRY+= " AND EX_STSFL='3' ";
			if(cmbRPTTP.getSelectedIndex() == 2) //All
				M_strSQLQRY+= " AND isnull(ex_stsfl,'') <> 'X' ";
			if(txtDPTCD.getText().length()>0)
			{	
				M_strSQLQRY+=" and EP_DPTCD='"+txtDPTCD.getText()+"'"; 
			}
			if(txtEMPNO.getText().length()>0)
			{	
				M_strSQLQRY+=" and EX_EMPNO='"+txtEMPNO.getText().trim()+"'"; 
			}
			if(rdbOFFCL.isSelected())
				M_strSQLQRY+=" and EX_OFPFL='O'"; 
			if(rdbPRSNL.isSelected())
				M_strSQLQRY+=" and EX_OFPFL='P'"; 
			M_strSQLQRY+=" and CONVERT(varchar,EX_DOCDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";	
			M_strSQLQRY+=" order by EX_DOCDT,EP_DPTNM,EX_OFPFL,EX_EMPNO"; 
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					
					L_tmsEINTM=M_rstRSSET.getTimestamp("EX_EINTM");
					L_tmsEOTTM=M_rstRSSET.getTimestamp("EX_EOTTM");
					L_tmsINCTM=M_rstRSSET.getTimestamp("EX_INCTM");
					L_tmsOUTTM=M_rstRSSET.getTimestamp("EX_OUTTM");
					L_tmsINCTM_SW=M_rstRSSET.getTimestamp("SW_INCTM");
					L_tmsOUTTM_SW=M_rstRSSET.getTimestamp("SW_OUTTM");
					
					L_tmsEXTTM=M_rstRSSET.getTime("EX_AOTTM");
					
					strNEW_DOCDT=M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT"));
					strNEW_DPTNM=M_rstRSSET.getString("EP_DPTNM");
					if(!strOLD_DPTNM.equals(strNEW_DPTNM) || !strOLD_DOCDT.equals(strNEW_DOCDT))
					{
						if(!strOLD_DOCDT.equals(strNEW_DOCDT))
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")),12));
							strNEW_DPTNM=M_rstRSSET.getString("EP_DPTNM");
							
						}
						strOLD_DOCDT=M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT"));
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),8));
						strOLD_DPTNM=M_rstRSSET.getString("EP_DPTNM");
						
					}
					else 
						D_OUT.writeBytes(padSTRING('R',"",8));
					
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),35));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EX_OFPFL"),""),17));
					D_OUT.writeBytes(padSTRING('R',L_tmsOUTTM==null ? "-" : M_fmtLCDTM.format(L_tmsOUTTM).substring(11),8));
					D_OUT.writeBytes(padSTRING('R',L_tmsEOTTM==null ? "-" : M_fmtLCDTM.format(L_tmsEOTTM).substring(11),7));
					D_OUT.writeBytes(padSTRING('R',L_tmsINCTM_SW==null ? "-" : M_fmtLCDTM.format(L_tmsINCTM_SW).substring(11,16),7));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EX_SHFCD"),""),5));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EX_EMPNO"),""),7));
					crtNWLIN();
					D_OUT.writeBytes(padSTRING('R',"",8));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EX_REMDS"),""),35));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EX_AUTBY"),""),9));
					D_OUT.writeBytes(padSTRING('R',L_tmsEXTTM==null ? "-" : M_fmtHHMM.format(L_tmsEXTTM).substring(0,5),8));

					D_OUT.writeBytes(padSTRING('R',L_tmsINCTM==null ? "-" : M_fmtLCDTM.format(L_tmsINCTM).substring(11),8));
					D_OUT.writeBytes(padSTRING('R',L_tmsEINTM==null ? "-" : M_fmtLCDTM.format(L_tmsEINTM).substring(11),7));
					D_OUT.writeBytes(padSTRING('R',L_tmsOUTTM_SW==null ? "-" : M_fmtLCDTM.format(L_tmsOUTTM_SW).substring(11,16),7));
					
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_EMPCT"),""),5));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EX_DOCNO"),""),8));
					crtNWLIN();
					crtNWLIN();
					
				}	
				
			}	
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
	
	/**Print header in all report**/

	void genRPHDR()
	{
		try
		{
			String L_strOFPFL="";
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
				//D_OUT.writeBytes("<HTML><HEAD><Title>Lotwise Bagging Despatch Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<HTML><HEAD></HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");   
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,44));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		if(rdbOFFCL.isSelected())
    		    L_strOFPFL=" - Official";
    		if(rdbPRSNL.isSelected())
    			L_strOFPFL=" - Personal";
    		
    		if(cmbRPTTP.getSelectedIndex() == 0)
    			D_OUT.writeBytes(padSTRING('R',"UnAuthorized Exit Pass"+ L_strOFPFL,44));
    		if(cmbRPTTP.getSelectedIndex() == 1)
    			D_OUT.writeBytes(padSTRING('R',"Authorized Exit Pass"+ L_strOFPFL,44));
    		if(cmbRPTTP.getSelectedIndex() == 2)
    			D_OUT.writeBytes(padSTRING('R',"All Exit Pass"+ L_strOFPFL,44));
    		
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
			crtNWLIN();
			
			if(txtSTRDT.getText().length() > 0 && txtENDDT.getText().length()> 0)
			{
				if(txtDPTCD.getText().length()>0)
				{
					D_OUT.writeBytes(padSTRING('R',"Department :"+lblDPTNM.getText(),30));
					crtNWLIN();
				}
				if(txtEMPNO.getText().length()>0)
				{
					D_OUT.writeBytes(padSTRING('R',"Employee No :"+lblEMPNM.getText()+"( "+txtEMPNO.getText()+" )",55));
					crtNWLIN();
				}
				D_OUT.writeBytes(padSTRING('R',"From :"+txtSTRDT.getText()+" To :"+txtENDDT.getText(),50));
			}
				
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Date    Emp Name                           Off/Per Exit   Act-Out Exp-Out  InTm   Shif Emp No ");
			crtNWLIN();
			D_OUT.writeBytes("Dept    Purpose                            AuthBy  Peroid Act-In  Exp-In   OutTm  Cat  DOC No ");
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------");
			crtNWLIN();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
			setCursor(cl_dat.M_curDFSTS_pbst);
			
		}
	}
	
	
/**print Footer**/
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------");
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
	
	boolean vldDATA()
	{
		try
		{
			if(txtSTRDT.getText().length()==0)
			{
				txtSTRDT.requestFocus();
				setMSG("Please Enter From Date",'E');
				return false;
			}	
			if(txtENDDT.getText().length()==0)
			{
				txtENDDT.requestFocus();
				setMSG("Please Enter To Date",'E');
				return false;
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
			cl_dat.M_flgLCUPD_pbst = true;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rpexa.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpexa.doc";
			
	    		genRPTFL();
		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
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
				M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and CMT_CODCD = '"+txtDPTCD.getText()+"'";
				//System.out.println("INPVF DPTCD : "+M_strSQLQRY);
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
				M_rstRSSET.close();
			}
			if(input == txtEMPNO)
			{
				
				M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and ep_empno = '"+txtEMPNO.getText()+"'";
				if(txtDPTCD.getText().length()>0)
					M_strSQLQRY += " and EP_DPTCD='"+txtDPTCD.getText()+"'";
				//System.out.println("INPVF EMPNO : "+M_strSQLQRY);
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
				M_rstRSSET.close();
			}
			
			if(input == txtSTRDT)
			{
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From date can not be greater than Today's date..",'E');
					return false;
				}
			}
			if(input == txtENDDT)
			{
				if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("TO date can not be greater than Today's date..",'E');
					return false;
				}
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
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



