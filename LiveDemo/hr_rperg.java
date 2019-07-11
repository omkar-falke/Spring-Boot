/*
System Name : Human Resurce Management System.

Program Name :  Attendance Register.
 
Source Directory : d:\source\splerp3\hr_rperg.java    
                    
Executable Directory : d:\exec\splerp3\hr_rperg.class

Purpose : This module use for display Attendance Register (Weekly & Monthly).

List of tables used:
Table Name		Primary key										Operation done
													Insert		Update	   	 Query    	Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
HR_EPMST		EP_CMPCD,EP_EMPNO                                			   /
HR_SWMST        SW_CMPCD,SW_EMPNO      														   
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on Shops & Establishment register(weekly)Report :
Column Name		Table name		Type/Size	    Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
ep_empnm		hr_epmst		Varchar(50)     Employee Name	
ep_sexfl        hr_epmst        Varchar(1)      Sex flag
ep_bthdt        hr_epmst        Date            Emp Birth Date
sw_incst		hr_swmst		TimeStamp       Standard In time
sw_outst		hr_swmst		TimeStamp       Standard Out time
sw_lvecd		hr_swmst		varchar(2)		Leave code
sw_actwk        hr_swmst		time			Actual work hours
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on Monthly attendance register as per Factory From J Report :
Column Name		Table name		Type/Size	    Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
ep_empno		hr_epmst		varchar(4)      Employee No
ep_empnm		hr_epmst		Varchar(50)     Employee Name	
sw_lvecd		hr_swmst		varchar(2)		Leave code
sw_actwk        hr_swmst		time			Actual work hours
-----------------------------------------------------------------------------------------------------------------------------------------------------

Validations :

->Data will be fetch from hr_epmst & HR_SWMST  (both table related to employee data) as per specified Period.
->Week Date & Month/Year can not be greater than Current date & Month/year resp.

Requirement :

->In shops & Establishment Register (weekly) report will be display working hours from Sunday to Saturday.
  in which also display total of weekly working hours.
->Monthly attendance Register report will be display on left & right pages (by click on left & right button).
->In Monthly attendance Register report will be display present(P)/absent(leave code or -) employee for a given month on left side page.
->On Right side display total Present ady ,Holiday day,Casual Leave ,Sick Leave ,Privilege Leave & L.W.P days of employee for given month.
->Calculated the Grand Total of Technical & Non-Technical Employees in summery report.

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Vector;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.ResultSet;


class hr_rperg extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rperg.html";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										
	private FileOutputStream F_OUT ;  /** FileOutputStream for generated report file handeling.*/
    private DataOutputStream D_OUT ;/** DataOutputStream to hold Stream of report data.*/ 
   
    private JTextField txtDPTCD,txtWKEDT,txtMONTH,txtYEAR;
    private JLabel lblDPTNM,lblWKEDT,lblMONTH;
    
    private JRadioButton  rdbREGWK,rdbREGMT;
	private ButtonGroup   btgREGST;
	
	private JRadioButton  rdbLEFT,rdbRIGHT;
	private ButtonGroup   btgREGMT;

	private SimpleDateFormat fmtHHMM = new SimpleDateFormat("HH:mm"); 
	
	String[] M_strMONTH = {"January", "February","March", "April", "May", "June", "July","August", "September", "October", "November","December"};

	String M_strENDDT="",M_strSTRDT="",M_strPRVDT="", M_strMNTNM="",M_strADDRS="",M_strDPTNM="",L_strBGCOL="#000000";
	;
	int M_intLSTDT =0;
	private boolean flgTBLDT;
	
	private Vector<String> vtrLVECD;
	private Vector<String> vtrEMPNO;
	private Vector<String> vtrWRKDT;/**Vector to store Month date**/
	private Vector<String> vtrWEKDT;/**Vector to store week date**/
	
	private Hashtable<String,String> hstLVEDS;
	private Hashtable<String,String> hstEMPDL;/**Hashtable to store employee related records **/
	private Hashtable<String,String> hstERGDL;/**Hashtable to store employee related records **/
	
	java.sql.Timestamp L_tmsINCST=null,L_tmsOUTST=null,L_tmsINCTM=null,L_tmsOUTTM=null;

	hr_rperg()		/*  Constructor   */
	{
		super(1);
		try
		{
			setMatrix(20,20);	
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			
		    add(rdbREGWK=new JRadioButton("Shops & Establishment Register (Weekly)"),4,7,1,6,this,'L');
		    add(lblWKEDT=new JLabel("Week Ending Date"),4,13,1,3,this,'L');   
		    add(txtWKEDT = new TxtDate(),4,16,1,2,this,'L');  
		    
		   
			add(rdbREGMT=new JRadioButton("Monthly Attendance Register as per Factoy act Form J"),6,7,1,7,this,'L');
			add(lblMONTH=new JLabel("Month & Year"),6,14,1,2,this,'L');   
		    add(txtMONTH = new TxtNumLimit(2),6,16,1,1,this,'L');  
		    add(txtYEAR= new TxtNumLimit(4),6,17,1,2,this,'L');  
		    
			add(rdbLEFT=new JRadioButton("LEFT"),7,7,1,2,this,'L');
			add(rdbRIGHT=new JRadioButton("RIGHT"),7,9,1,2,this,'L');
			
			add(new JLabel("Department :"),8,7,1,2,this,'L');   
			add(txtDPTCD = new TxtLimit(3),8,9,1,2,this,'L'); 
			add(lblDPTNM = new JLabel(),8,11,1,2,this,'L'); 
			
			INPVF oINPVF=new INPVF();
			txtDPTCD.setInputVerifier(oINPVF);
			txtWKEDT.setInputVerifier(oINPVF);
			txtMONTH.setInputVerifier(oINPVF);
			txtYEAR.setInputVerifier(oINPVF);
			
			btgREGMT=new ButtonGroup();
			btgREGMT.add(rdbREGWK); 
			btgREGMT.add(rdbREGMT);
			
			btgREGST=new ButtonGroup();
			btgREGST.add(rdbLEFT); 
			btgREGST.add(rdbRIGHT);
			
			lblWKEDT.setVisible(false);
			txtWKEDT.setVisible(false);
			txtMONTH.setVisible(false);
			lblMONTH.setVisible(false);
			txtYEAR.setVisible(false);
			rdbLEFT.setVisible(false);
			rdbRIGHT.setVisible(false);
		
			vtrLVECD  = new Vector<String>();
			vtrEMPNO  = new Vector<String>();
			vtrWRKDT  = new Vector<String>();
			vtrWEKDT  = new Vector<String>();
			
			M_pnlRPFMT.setVisible(true);
			M_rdbHTML.setSelected(true);
			setENBL(true);
			hstEMPDL = new Hashtable<String,String>();
			hstERGDL = new Hashtable<String,String>();
			
			/**Store Leave code description in hstLVEDS hashtable*/
			hstLVEDS = new Hashtable<String,String>();
			String L_strSQLQRY1=" SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN";
			L_strSQLQRY1+= " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXLVE' and CMT_CHP02='1'";
			L_strSQLQRY1+= " order by CMT_CODCD";
			ResultSet L_rstRSSET1= cl_dat.exeSQLQRY(L_strSQLQRY1);
			//System.out.println(">>>"+L_strSQLQRY1);
			if(L_rstRSSET1 != null)
			{
				while(L_rstRSSET1.next())
					hstLVEDS.put(nvlSTRVL(L_rstRSSET1.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),""));
				L_rstRSSET1.close();
			}
			
			/**M_strADDRS String Varible to store Company address**/
			M_strSQLQRY =" Select CP_ADR01,CP_ADR02,CP_ADR03,CP_ADR04,CP_PINCD,CP_CTYNM from CO_CPMST ";
			M_strSQLQRY+=" where CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>>M_strSQLQRY>>>>"+M_strSQLQRY);	
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				 M_strADDRS=M_rstRSSET.getString("CP_ADR01")+" "+M_rstRSSET.getString("CP_ADR02")+" "+M_rstRSSET.getString("CP_ADR03")+" "+M_rstRSSET.getString("CP_ADR04")+" "+M_rstRSSET.getString("CP_CTYNM")+" "+M_rstRSSET.getString("CP_PINCD");
				//System.out.println("M_strADDRS>>>>"+M_strADDRS);
			}	
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
					rdbREGWK.requestFocus();
				}
			}
			else if(M_objSOURC == rdbREGWK)
			{
				if(rdbREGWK.isSelected())
				{
					lblWKEDT.setVisible(true);
					txtWKEDT.setVisible(true);
					lblMONTH.setVisible(false);
					txtMONTH.setVisible(false);
					txtYEAR.setVisible(false);
					rdbLEFT.setVisible(false);
					rdbRIGHT.setVisible(false);

					txtWKEDT.requestFocus();
					M_calLOCAL.add(Calendar.DATE, 7 - M_calLOCAL.get(Calendar.DAY_OF_WEEK)-7);
					M_strPRVDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
					txtWKEDT.setText(M_strPRVDT);
					if((txtWKEDT.getText().trim().length()==0))
						setMSG ("Please enter Weekly Ending Date to generate Report..",'N');	
				}
			}
			else if(M_objSOURC == rdbREGMT)
			{
				if(rdbREGMT.isSelected())
				{
					lblMONTH.setVisible(true);
					txtMONTH.setVisible(true);
					txtYEAR.setVisible(true);
					rdbLEFT.setVisible(true);
					rdbRIGHT.setVisible(true);
					rdbLEFT.setSelected(true);
					txtWKEDT.setVisible(false);
					lblWKEDT.setVisible(false);

					txtMONTH.requestFocus();
					txtMONTH.setText(cl_dat.M_strLOGDT_pbst.substring(3,5));
					txtYEAR.setText(cl_dat.M_strLOGDT_pbst.substring(6));
					if((txtMONTH.getText().trim().length()==0))
						setMSG ("Please enter Month to generate Report..",'N');	
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
				if(M_objSOURC==txtDPTCD)		
				{
					M_strHLPFLD = "txtDPTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXDPT'";
					if(txtDPTCD.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtDPTCD.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Department code","Department Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
			}
			if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
				if(M_objSOURC == rdbREGWK)
					 txtWKEDT.requestFocus();
				else if(M_objSOURC == txtWKEDT)
					txtDPTCD.requestFocus();
				
				else if(M_objSOURC == rdbREGMT)
					txtMONTH.requestFocus();
				else if(M_objSOURC == txtMONTH)
				{
					txtYEAR.requestFocus();
					txtYEAR.setText(cl_dat.M_strLOGDT_pbst.substring(6));
				}
				else if(M_objSOURC == txtYEAR)
				{
					txtDPTCD.requestFocus();
					setMSG("Enter the Department Code or Press F1 to Select from List..",'N');	
				}
				else if(M_objSOURC == txtDPTCD)
				{
					if(txtDPTCD.getText().length()==0)
						lblDPTNM.setText("");
				   cl_dat.M_btnSAVE_pbst.requestFocus();
				}
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
				  lblDPTNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
			
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}
	/*
	 
<table width="1000" border="1" cellspacing="1">
  <tr>
    <th width="84" scope="col">&nbsp;</th>
    <th width="46" scope="col">&nbsp;</th>
    <th width="41" scope="col">&nbsp;</th>
    <th width="51" scope="col">&nbsp;</th>
    <th width="46" scope="col">&nbsp;</th>
    <th width="41" scope="col">&nbsp;</th>
    <th width="50" scope="col">&nbsp;</th>
    <th width="46" scope="col">&nbsp;</th>
    <th width="48" scope="col">&nbsp;</th>
    <th width="48" scope="col">&nbsp;</th>
    <th width="47" scope="col">&nbsp;</th>
    <th width="48" scope="col">&nbsp;</th>
    <th width="46" scope="col">&nbsp;</th>
    <th width="46" scope="col">&nbsp;</th>
    <th width="17" scope="col">&nbsp;</th>
    <th width="18" scope="col">&nbsp;</th>
    <th width="17" scope="col">&nbsp;</th>
    <th width="18" scope="col">&nbsp;</th>
    <th width="19" scope="col">&nbsp;</th>
    <th width="21" scope="col">&nbsp;</th>
    <th width="16" scope="col">&nbsp;</th>
    <th width="33" scope="col">&nbsp;</th>
    <th width="35" scope="col">&nbsp;</th>
  </tr>
  </table>
	 
	 */

	/**method to generate Shops & Establishment Register(Weekly)*/
	void genWEKDL()
	{
		try
		{
			if(!vldDATA())
				return;
			setCursor(cl_dat.M_curWTSTS_pbst);
			
			hstEMPDL.clear();
			hstERGDL.clear();
			vtrEMPNO.clear();
			vtrLVECD.clear();
			
			String strTOTHR = "00:00";
			
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			genRPHDR();
			
			M_strSQLQRY = " select ep_empno,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' ' + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  ep_empnm,ep_sexfl,DATEDIFF(year, ep_bthdt, GETDATE()) ep_bthdt,sw_incst,sw_outst,sw_inctm,sw_outtm,sw_lvecd,sw_wrkdt,isnull(sw_actwk,'00:00') sw_actwk ";
			M_strSQLQRY += " from hr_epmst,hr_swmst";
			M_strSQLQRY += " where sw_cmpcd=ep_cmpcd and sw_empno=ep_empno and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strENDDT))+"' ";
			if(txtDPTCD.getText().length()>0)
				M_strSQLQRY += " and ep_dptcd='"+txtDPTCD.getText()+"'";
			M_strSQLQRY += "order by ep_empno";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while( M_rstRSSET.next())
				{
					if(!vtrEMPNO.contains(M_rstRSSET.getString("ep_empno")))
					{
						vtrEMPNO.add(M_rstRSSET.getString("ep_empno"));
						vtrEMPNO.remove("");
					}
					if(!hstEMPDL.contains(M_rstRSSET.getString("ep_empno")))
						hstEMPDL.put(nvlSTRVL(M_rstRSSET.getString("ep_empno"),""),M_rstRSSET.getString("ep_empnm")+"!"+M_rstRSSET.getString("ep_sexfl")+"!"+M_rstRSSET.getString("ep_bthdt")+"!"+M_rstRSSET.getTimestamp("sw_incst")+"!"+M_rstRSSET.getTimestamp("sw_outst"));
					if(!hstERGDL.contains(M_rstRSSET.getString("ep_empno")))
						hstERGDL.put(M_rstRSSET.getString("ep_empno")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("sw_wrkdt")),M_rstRSSET.getTimestamp("sw_inctm")+"!"+M_rstRSSET.getTimestamp("sw_outtm")+"!"+M_rstRSSET.getString("sw_lvecd")+"!"+M_rstRSSET.getString("sw_actwk"));
				}
			}
			
			// D_OUT.writeBytes("<table width=984 border=1 cellspacing=0 bordercolor=\"#666666\">");			   
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				D_OUT.writeBytes("<tr>");
				/**Fetch employee related records to display vertically in html table*/
				if(hstEMPDL.containsKey(vtrEMPNO.get(i)))
				{
					String[] L_strEMPDL=hstEMPDL.get(vtrEMPNO.get(i)).split("!");
					D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='130' align ='left'><font size='2'>");
					D_OUT.writeBytes(L_strEMPDL[0]);
					D_OUT.writeBytes("</font></td>");
					
					if(L_strEMPDL[1].equals("1")) /**if ep_sexfl=1 */
						//D_OUT.writeBytes(padSTRING1('C',"M",null,0));
					    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='36' align ='center' ><font size='2'>M</font></td>");
					else
						//D_OUT.writeBytes(padSTRING1('C',"F",null,0)); /**if ep_sexfl=2 */
						D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='36' align ='center'><font size='2'>F</font></td>");
					
					if(L_strEMPDL[2].compareTo("20")>0) /**if employee age will be greater than 20 ,while set 'N' otherwise 'Y'*/
						//D_OUT.writeBytes(padSTRING1('C',"N",null,0));
						D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='34' align ='center' ><font size='2'>N</font></td>");
					else
						//D_OUT.writeBytes(padSTRING1('C',"Y",null,0));
						D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='34' align ='center'><font size='2'>Y</font></td>");
	
					//D_OUT.writeBytes(padSTRING1('C',L_strEMPDL[3].substring(11,16),null,0));/**Standard In time*/
					//D_OUT.writeBytes(padSTRING1('C',L_strEMPDL[4].substring(11,16),null,0));/**Standard out time*/
					//D_OUT.writeBytes(padSTRING1('C',"00:30",null,0)); /**Rest Interval(1:00 to 1:30)*/
					D_OUT.writeBytes("<td width='34' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align ='center'><font size='2'>"+L_strEMPDL[3].substring(11,16)+"</font></td>");
					D_OUT.writeBytes("<td width='36' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align ='center'><font size='2'>"+L_strEMPDL[4].substring(11,16)+"</font></td>");
					D_OUT.writeBytes("<td width='35' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align ='center'><font size='2'>00:30</font></td>");
					/*proper blank display
					D_OUT.writeBytes("<td width='44' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='42' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='42' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='45' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='44' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width=43' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='45' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='35' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='24' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='24' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='25' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='24' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='17' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='19' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='17' align ='center'><font size='2'>&nbsp;</font></td>");					
					D_OUT.writeBytes("<td width='36' align ='center'><font size='2'>&nbsp;</font></td>");
					D_OUT.writeBytes("<td width='35' align ='center'><font size='2'>&nbsp;</font></td></table>");
					*/				
				}
				
				for(int j=0;j<vtrWEKDT.size();j++)
				{
					System.out.println("vtr :"+vtrEMPNO.get(i).toString());
					/**Fetch Actual work hours of employee to display horizontally in html table until the week ending Date*/
					if(hstERGDL.containsKey(vtrEMPNO.get(i)+"|"+vtrWEKDT.get(j)))
					{
						String[] L_strERGDL=hstERGDL.get(vtrEMPNO.get(i)+"|"+vtrWEKDT.get(j)).split("!");
						if(!L_strERGDL[0].equals("null") && !L_strERGDL[1].equals("null"))
						{
							if(fmtHHMM.parse(L_strERGDL[3]).compareTo(fmtHHMM.parse("08:00"))>0)/**if Actual work hours greater than 08:000, while set hours 08:00*/
							{
								L_strERGDL[3]="08:00";
								D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='44' align='center'><font size=2>");
								D_OUT.writeBytes(L_strERGDL[3]);
								D_OUT.writeBytes("</font></td>");
							}
							else
							{
								D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='44' align='center' ><font size=2 >");
								D_OUT.writeBytes(chkZERO(L_strERGDL[3].substring(0,5)));/**display Actual work hours*/
								D_OUT.writeBytes("</font></td>");
							}
							strTOTHR=addTIME(L_strERGDL[3].substring(0,5),strTOTHR);
						}
						else
						{
							D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='42' align='center' ><font size=1 >");
							D_OUT.writeBytes(nvlSTRVL(L_strERGDL[2],"-"));/**it will be display leave code or -,while In/Out time will be blank */
							D_OUT.writeBytes("</font></td>");
						}
						if(!vtrLVECD.contains(L_strERGDL[2]))/**add leave code in Vector to display in footer*/
						{
							vtrLVECD.add(L_strERGDL[2]);
							vtrLVECD.remove("");
							vtrLVECD.remove("null");
						}
					}
					else
						D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				}
				
				//D_OUT.writeBytes(padSTRING1('C',strTOTHR,null,0));/**Ptint total of Actual work hours */
				D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width='35' align='center' ><font size=2 >");
				D_OUT.writeBytes(strTOTHR);
				D_OUT.writeBytes("</font></td>");
				
				strTOTHR="00:00";
				/*
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes(padSTRING1('C'," - ",null,0));
				D_OUT.writeBytes("</tr>");
				*/
				D_OUT.writeBytes("<td width='24' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='25' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='24' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");				
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='24' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");				
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='17' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='19' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");				
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='17' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");				
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='32' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");				
				D_OUT.writeBytes("</font></td>");
				D_OUT.writeBytes("<td width='35' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray align='center' ><font size=1 >");
				D_OUT.writeBytes("-");				
				D_OUT.writeBytes("</font></td>");			
				crtNWLIN();
			}	
			
			genRPFTR();
			endTABLE(D_OUT);
			D_OUT.close();
			F_OUT.close();
		    
			if(M_rstRSSET==null)
			{
			  M_rstRSSET.close();
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genWEKDL");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**method to generate Monthly Attendance Register as per Factoy act Form J report*/
	void genMNTDL()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(!vldDATA())
				return;
			
			hstEMPDL.clear();
			hstERGDL.clear();
			vtrEMPNO.clear();
			vtrLVECD.clear();
			
			String[] L_strERGDL=null;
			
			String L_strPRSHR = "00:00",L_strHLDHR="00:00",L_strCLLHR="00:00",L_strSLLHR="00:00",L_strPLLHR="00:00",L_strTOTHR="00:00";
			int L_intPRDAY=0,L_intHLDAY=0,L_intCLDAY=0,L_intSLDAY=0,L_intPLDAY=0,L_intWPDAY=0,L_intTOTAL=0;
			
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			genRPHDR();
			
			M_strSQLQRY = " select sw_empno,sw_wrkdt,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' ' + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  ep_empnm,ep_dptnm,ep_mmgrd,isnull(sw_prsfl,'') sw_prsfl,isnull(sw_wrkvl,0) sw_wrkvl,isnull(sw_lvecd,'') sw_lvecd ";
			M_strSQLQRY+= " from hr_swmst,hr_epmst ";
			M_strSQLQRY+= " where sw_cmpcd = ep_cmpcd and sw_empno=ep_empno and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strENDDT))+"' ";
			if(txtDPTCD.getText().length()>0)
				M_strSQLQRY += " and ep_dptcd='"+txtDPTCD.getText()+"'";
			M_strSQLQRY+= " order by sw_empno";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			if(M_rstRSSET != null )
			{
				while( M_rstRSSET.next())
				{
					//M_strDPTNM=M_rstRSSET.getString("ep_dptnm");
					if(!hstEMPDL.contains(M_rstRSSET.getString("sw_empno")))
						hstEMPDL.put(nvlSTRVL(M_rstRSSET.getString("sw_empno"),""),nvlSTRVL(M_rstRSSET.getString("ep_empnm"),"")+"!"+nvlSTRVL(M_rstRSSET.getString("ep_mmgrd"),""));
					if(!hstERGDL.contains(M_rstRSSET.getString("sw_empno")))
						hstERGDL.put(M_rstRSSET.getString("sw_empno")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("sw_wrkdt")),M_rstRSSET.getString("sw_prsfl")+"!"+M_rstRSSET.getString("sw_lvecd")+"!"+M_rstRSSET.getString("sw_wrkvl"));
					if(!vtrEMPNO.contains(M_rstRSSET.getString("sw_empno")))
					{
						vtrEMPNO.add(M_rstRSSET.getString("sw_empno"));
						vtrEMPNO.remove("");
					}
				}
			}
			
			
			/**Fetch employee no wise records in table vertically */
			for(int i=0;i<vtrEMPNO.size();i++)
			{
				//D_OUT.writeBytes("<tr bgcolor="+L_strBGCOL+">");
				//D_OUT.writeBytes("<tr><td>&nbsp;</td></tr>");
				D_OUT.writeBytes("<tr>");
				if(rdbLEFT.isSelected())
				{
					D_OUT.writeBytes(padSTRING1('C',String.valueOf(i+1),"RW",3));/**print Serial No*/
					D_OUT.writeBytes(padSTRING1('C',vtrEMPNO.get(i),"RW",3));
					if(hstEMPDL.containsKey(vtrEMPNO.get(i)))
					{
						String[] L_strEMPDL=hstEMPDL.get(vtrEMPNO.get(i)).split("!");
						// style='border-top: medium double; border-bottom: medium double'
						D_OUT.writeBytes("<td width='30%' align ='left' rowspan='3' ><b><font size='2'>");
						D_OUT.writeBytes(L_strEMPDL[0]);/**get employee name from hstEMPDL of given emp no key to display in table */
						D_OUT.writeBytes("</font></b></td>");
						D_OUT.writeBytes(padSTRING1('C',L_strEMPDL[1],"RW",3));
					}
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;","RW",3));
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;","RW",3));
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;","RW",3));
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;","RW",3));
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;","RW",3));
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;","RW",3));
					
				}
				else
				{
					D_OUT.writeBytes(padSTRING1('C',String.valueOf(i+1),null,0));/**print Serial No*/
				}
				L_intPRDAY=0;
				L_intHLDAY=0;
				L_strPRSHR="00:00";
				L_strHLDHR="00:00";
				
				L_intCLDAY=0;
				L_intSLDAY=0;
				L_intPLDAY=0;
				L_intTOTAL=0;
				L_strCLLHR="00:00";
				L_strSLLHR="00:00";
				L_strPLLHR="00:00";
				L_strTOTHR="00:00";	
				
				/**Fetch work date wise records in table horizontally */
				
				for(int j=0;j<vtrWRKDT.size();j++)
				{
					if(hstERGDL.containsKey(vtrEMPNO.get(i)+"|"+vtrWRKDT.get(j)))
					{
						L_strERGDL=hstERGDL.get(vtrEMPNO.get(i)+"|"+vtrWRKDT.get(j)).split("!");
				
						if(rdbLEFT.isSelected())
						{
							if(L_strERGDL[1].equals("WO"))
								D_OUT.writeBytes(padSTRING1('C',"|",null,0));	
							else
								//D_OUT.writeBytes(padSTRING1('C',nvlSTRVL(L_strERGDL[1],"-"),null,0));/**print leave code or -,while Actual work hours less than 03:00 */	
								D_OUT.writeBytes("<td width='5.5%' align ='center'><font size='2'>"+nvlSTRVL(L_strERGDL[1],"-")+"</font></td>");
							
						}
						if(rdbRIGHT.isSelected())
						{
							if(L_strERGDL[0].equals("P") || L_strERGDL[1].equals("TD"))
							{
								L_intPRDAY+=1;/**Calculate present day to display on right side page*/
								/**Calculate present day=present day + TD day, if Employee on Tour Duty*/
							}
							
							if(L_strERGDL[1].equals("WO") || L_strERGDL[1].equals("PH") || L_strERGDL[1].equals("RH"))
							{
								L_intHLDAY+=1; /**Calculate Holiday =WO+PH+RH*/
								//L_strHLDHR=addTIME("08:00",L_strHLDHR);
							}
							if(L_strERGDL[1].equals("CL"))
							{
								L_intCLDAY+=1; /**Calculate Casual Leave day */
							}
							if(L_strERGDL[1].equals("SL"))
							{
								L_intSLDAY+=1; /**Calculate Sick Leave day */
							}
							if(L_strERGDL[1].equals("PL"))
							{
								L_intPLDAY+=1; /**Calculate Privilege leave day */
							}
						}
						if(!vtrLVECD.contains(L_strERGDL[1]))/**add leave code in Vector to display in footer*/
						{
							vtrLVECD.add(L_strERGDL[1]);
							vtrLVECD.remove("");
							vtrLVECD.remove("null");
						}
					}
					else
					{
						if(rdbLEFT.isSelected())
						D_OUT.writeBytes(padSTRING1('C',"-",null,0));/**print -, if hstERGDl not contains key emp no & wrk date */
						
					}
				}
			
				if(rdbLEFT.isSelected())
				{
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr bgcolor="+L_strBGCOL+">");
					for(int j=0;j<vtrWRKDT.size();j++)
					{
						if(hstERGDL.containsKey(vtrEMPNO.get(i)+"|"+vtrWRKDT.get(j)))
						{
							L_strERGDL=hstERGDL.get(vtrEMPNO.get(i)+"|"+vtrWRKDT.get(j)).split("!");
							D_OUT.writeBytes(padSTRING1('C',nvlSTRVL(L_strERGDL[0],"-"),null,0));
						}
						else
							D_OUT.writeBytes(padSTRING1('C',"-",null,0));/**print -, if hstERGDl not contains key emp no & wrk date */
					}
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr bgcolor="+L_strBGCOL+">");
					for(int j=0;j<vtrWRKDT.size();j++)
					{
						if(hstERGDL.containsKey(vtrEMPNO.get(i)+"|"+vtrWRKDT.get(j)))
						{
							L_strERGDL=hstERGDL.get(vtrEMPNO.get(i)+"|"+vtrWRKDT.get(j)).split("!");
					
							if(L_strERGDL[2].equals("0.0000"))
								D_OUT.writeBytes(padSTRING1('C',"-",null,0));
							else if(L_strERGDL[2].equals("1.0000"))
								D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
							else
							{
								//D_OUT.writeBytes("<tr bgcolor="+L_strBGCOL+"><td width='4%' height='10%' align='center'><font size=1 face= Times New Roman></font>");
								//D_OUT.writeBytes(L_strERGDL[2]);
								//D_OUT.writeBytes("</td>");
								D_OUT.writeBytes(padSTRING1('C',L_strERGDL[2],null,0));
							}
							
						}
						else
							D_OUT.writeBytes(padSTRING1('C',"-",null,0));
					}
					D_OUT.writeBytes("</tr>");
				}
					
				if(rdbRIGHT.isSelected())
				{	
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_intPRDAY)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_strPRSHR)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_intHLDAY)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_strHLDHR)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_intCLDAY)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_strCLLHR)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_intSLDAY)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_strSLLHR)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_intPLDAY)),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_strPLLHR)),null,0));
					L_intTOTAL=L_intPRDAY+L_intHLDAY+L_intCLDAY+L_intSLDAY+L_intPLDAY;/**calculate total day */
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(L_intTOTAL)),null,0));/**display total day */
					D_OUT.writeBytes(padSTRING1('C',chkZERO(L_strPRSHR),null,0));
					D_OUT.writeBytes(padSTRING1('C',chkZERO(String.valueOf(M_intLSTDT-L_intTOTAL)),null,0));/**calculate L.W.P (leave without pay)**/
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
					D_OUT.writeBytes(padSTRING1('C',String.valueOf(M_intLSTDT),null,0));/**dispay working days of month*/
					D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
					D_OUT.writeBytes("<td width='15%' align ='left'><b><font size='2'>");
					D_OUT.writeBytes("&nbsp;");
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("</tr>");
				
				}
				if(L_strBGCOL.equals("#000000"))
					L_strBGCOL="#FFFFFF";
				else if(L_strBGCOL.equals("#FFFFFF"))
					L_strBGCOL="#000000";
				crtNWLIN();
			}
			
			genRPFTR();
			endTABLE(D_OUT);
			D_OUT.close();
			F_OUT.close();
		    
			if(M_rstRSSET==null)
			{
			  M_rstRSSET.close();
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genMNTDL");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**Method to creat HTML Table*/
	private void crtTBL(DataOutputStream L_DOUT,boolean P_flgBORDR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(P_flgBORDR)
				L_DOUT.writeBytes("<p><TABLE border=0 bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray  cellPadding=4 cellSpacing=0  width=\"100%\" align=center>");
			else
				L_DOUT.writeBytes("<p><TABLE border=0 bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width=\"100%\" align=center>");
			flgTBLDT=true;
		}
	}
	/**Method to end HTML Table*/
	private void endTABLE(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbHTML.isSelected())
			L_DOUT.writeBytes("</TABLE></P>");
		flgTBLDT=false;
	}
	
	
	/**Method to created html column format.
	 * 
	 */	
	protected  String padSTRING1(char P_chrPADTP,String P_strSTRVL,String P_strSTYLE,int P_intSPANN)
	{
		String P_strTRNVL = "";
		String strTXCLR= "";
		try
		{
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			strTXCLR="<font size=2 face= Times New Roman>";
		
			if(M_rdbHTML.isSelected())
			{
				if(P_chrPADTP=='C')
					P_strTRNVL="<p Align = center>"+strTXCLR+P_strSTRVL+"</P>";
				else if(P_chrPADTP=='R')
					P_strTRNVL="<p Align = left>"+strTXCLR+P_strSTRVL+"</P>";
				else if(P_chrPADTP=='L')
					P_strTRNVL="<p Align = right>"+strTXCLR+P_strSTRVL+"</P>";
				else if(P_chrPADTP=='V')//vertical
					P_strTRNVL="<p Align = center style='writing-mode: tb-rl'>"+strTXCLR+P_strSTRVL+"</P>";
				
				if(flgTBLDT)
				{
					if(P_strSTYLE=="CL")// column span
						P_strTRNVL="<td width='25%' height='10%' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 colspan="+P_intSPANN+"><b>"+P_strTRNVL+"</font></b></td>";
					else if(P_strSTYLE=="RW")//row span
						P_strTRNVL="<td width='2%'  height='10%' bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 rowspan="+P_intSPANN+"><b>"+P_strTRNVL+"</font></b></td>";
					else
					{
						if(rdbRIGHT.isSelected())
							P_strTRNVL="<td  bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width='2%' height='50'"+strTXCLR+P_strTRNVL+"</font></td>";
						else
							P_strTRNVL="<td  bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width='4%' height='10%' "+strTXCLR+P_strTRNVL+"</font></td>";
					}
						
				}
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"padSTRING");
		}
		return P_strTRNVL;
	}
	

	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{	
				if(rdbREGWK.isSelected())
				{ 
					if(cl_dat.M_intLINNO_pbst >28)
					{
						genRPFTR();
						genRPHDR();
						vtrLVECD.clear();/** clear vector to add new leave code of fetching records*/
					}	
				}
				else
				{
					if(cl_dat.M_intLINNO_pbst >9)
					{
						genRPFTR();
						genRPHDR();
						vtrLVECD.clear();/** clear vector to add new leave code of fetching records*/
					}
				}
			}
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}
	/** Method to check zero value*/
	private String chkZERO(String LP_STR)
	{
		try
		{
			if(LP_STR.equals("00:00") || LP_STR.equals("0"))
				return "-";
			else
				return LP_STR;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkZERO()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return "";
	}
	
	void genRPHDR()
	{
		try
		{
			String strEMPTY="";
			/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
			*/
			crtTBL(D_OUT,true);
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title></title>");
				D_OUT.writeBytes("<style>");
				//D_OUT.writeBytes("div#test {background-image:url(\\\\192.168.10.207\\toho\\level2\\hdr.jpg);background-repeat: no-repeat; height: 155px; width:1000 text-align: left}</style>");crtNWLIN(); 
				D_OUT.writeBytes("</style>");
				
				D_OUT.writeBytes("</HEAD><BODY><P><PRE style =\" font-size :9 pt \"></font>"); 
				
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always} </STYLE>"); 
				
			}	
			cl_dat.M_PAGENO +=1;
			/**for Shops & Establishment register Header*/
    		if(rdbREGWK.isSelected())
  		    {
    			
    			crtTBL(D_OUT,false);
    			//D_OUT.writeBytes("<br>");
				//<IMG src=\\\\192.168.10.207\\user\\exec\\splerp3\\spllogo_old.gif style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\">
    			//D_OUT.writeBytes("<tr><td width='20%' height='13' rowspan='3'><img src=file:///192.168.10.207/USER/EXEC/splerp3/spllogo_old.gif width='76' height='80' alt='spllogo.gif'></td><td width='60%' height='13' align=center><STRONG><FONT face=Arial size=2><em>Form J [Rule.20(1)]</em></FONT></STRONG></td><td width='20%'></td></tr><tr><td align=center><STRONG><FONT face=Arial size=4><em>REGISTER OF EMPLOYMENT</em></STRONG></FONT></td></tr>" +
    			//D_OUT.writeBytes("<tr><FONT face=Arial size=4>SUPREME PETROCHEM LTD</FONT><td width='60%' height='13' align=center><STRONG><FONT face=Arial size=2><em>Form J [Rule.20(1)]</em></FONT></STRONG></td><td width='20%'></td></tr><tr><td align=center><STRONG><FONT face=Arial size=4><em>REGISTER OF EMPLOYMENT</em></STRONG></FONT></td><td align='center' width='20%' height='13' rowspan='2'><IMG src=\\\\192.168.10.207\\user\\exec\\splerp3\\spllogo_old.gif style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></td></tr>" +
					//	"<tr><td align=center><FONT face=Arial size=2>(Where Opening & Ending hours are ordinarily uniform)</FONT></td></tr>" +
						//"<tr><td align=center><STRONG><FONT face=Arial size=2>FOR THE WEEK ENDING : "+M_strENDDT+"</FONT></STRONG></td></tr><tr><td></td><td><small>"+M_strADDRS+"</small></small></td></tr></table>");
					
    			
    			D_OUT.writeBytes("<table border=0 width=100% bordercolor='#CCCCCC'>"+
    			  "<tr>"+
    			    "<td width='30%'><big><strong>SUPREME&nbsp; PETROCHEM&nbsp;&nbsp; LTD</strong></big></td>"+
    			    "<td width='38%'><p align='center'><em><strong>Form&nbsp; J [Rule.20(1)]</strong></em></td>"+
    			    "<td width='32%' rowspan='3' align=center><img src=\\\\192.168.10.207\\user\\exec\\splerp3\\spllogo_old.gif width='76' height='80' alt='spllogo.gif'>"+
    			  "</td>"+
    			  "</tr>"+
    			  "<tr>"+
    			    "<td width='30%' rowspan='3'><small>"+M_strADDRS+"</small> <small>&nbsp;&nbsp; Andheri (E) </small></td>"+
    			    "<td width='38%'><p align='center'><em><big><strong>REGISTER&nbsp; OF&nbsp;&nbsp;EMPLOYMENT</strong></big></em></td>"+
    			  "</tr>"+
    			  "<tr>"+
    			    "<td width='38%'><p align='center'><small><font face='Arial'>(Where&nbsp; Opening &amp;"+
    			    "Ending hours are ordinarily uniform)</font></small></td>"+
    			  "</tr>"+
    			  "<tr>"+
    			    "<td width='38%'><p align='center'><small><strong>FOR&nbsp; THE WEEK&nbsp; ENDING :"+M_strENDDT+"</strong></small></td>"+
    			    "<td width='32%'></td>"+
    			  "</tr>"+    			  
    			"</table>");

			    //crtTBL(D_OUT,true);
			    //D_OUT.writeBytes("<div id=\"test\"></div>");			    
			    //D_OUT.writeBytes("<div id=\"test\"><br><br><br><br><br><br><pre>                          &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+M_strSTRDT+"</pre></div>");
			    String L_strSTRDT=M_strSTRDT.toString().substring(0,5),L_strSTRDT1=null;
			    D_OUT.writeBytes("<img src=\\\\192.168.10.207\\user\\exec\\splerp3\\hdr.jpg>");
			   
			    /*D_OUT.writeBytes("<div id=\"test\"><br><br><br><br><br><br><br><pre>                                  &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+L_strSTRDT);
			    M_calLOCAL.setTime(M_fmtLCDAT.parse(M_strSTRDT)); 
			    M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			    D_OUT.writeBytes("&nbsp"+L_strSTRDT1);
			    M_calLOCAL.add(Calendar.DATE,1);
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
				   
			    D_OUT.writeBytes("&nbsp"+L_strSTRDT1);
			    M_calLOCAL.add(Calendar.DATE,1);
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
				   
			    D_OUT.writeBytes("&nbsp&nbsp"+L_strSTRDT1);
			    M_calLOCAL.add(Calendar.DATE,1);
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
				   
			    D_OUT.writeBytes("&nbsp&nbsp"+L_strSTRDT1);
			    M_calLOCAL.add(Calendar.DATE,1);
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
				   
			    D_OUT.writeBytes("&nbsp&nbsp"+L_strSTRDT1);
			    		    
			    M_calLOCAL.add(Calendar.DATE,1);
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
				   
			    D_OUT.writeBytes("&nbsp&nbsp"+L_strSTRDT1);
			    D_OUT.writeBytes("</pre></div>");   
			    */
			   // String L_strSTRDT=M_strSTRDT.toString().substring(0,5),L_strSTRDT1=null;
			    //D_OUT.writeBytes("<table width=984 border=1 cellspacing=0 bordercolor='#CCCCCC'>");
			    D_OUT.writeBytes("<TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width=984>");
			    D_OUT.writeBytes("<tr>");
			    
			  //  td style="background-color:#bbd8d8; bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"128\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"34\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"32\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"32\">&nbsp;</td>");
			      D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"34\">&nbsp;</td>");
			    D_OUT.writeBytes(" <td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"33\">&nbsp;</td>");
			    M_calLOCAL.setTime(M_fmtLCDAT.parse(M_strSTRDT)); 
			    //M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			    
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"42\">"+L_strSTRDT1+"</td>");
			    M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			  
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"40\">"+L_strSTRDT1+"</td>");
			    M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			  
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"42\">"+L_strSTRDT1+"</td>");
			    M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			  
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"43\">"+L_strSTRDT1+"</td>");
			    M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			  
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"44\">"+L_strSTRDT1+"</td>");
			    M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			  
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"43\">"+L_strSTRDT1+"</td>");
			    M_calLOCAL.add(Calendar.DATE,1);   
			    L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
			    L_strSTRDT1=L_strSTRDT.toString().substring(0,5);
			  
			    D_OUT.writeBytes("<td  bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"45\">"+L_strSTRDT1+"</td>");
			    
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"35\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"24\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"25\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"24\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"24\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"17\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"19\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"17\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"36\">&nbsp;</td>");
			    D_OUT.writeBytes("<td bordercolor='#CCCCCC' borderColorDark=ghostwhite borderColorLight=gray width=\"35\">&nbsp;</td>");
			    D_OUT.writeBytes("</tr>");
			    //D_OUT.writeBytes("</table>");
			  
			    	    
			    
			    
			    /*			  
			    D_OUT.writeBytes("<tr>");
			    D_OUT.writeBytes(padSTRING1('C',"Name&nbsp;of&nbsp;&nbsp;&nbsp;Persons Employed","RW",2));
			  
				D_OUT.writeBytes(padSTRING1('V',"Whether woman"+"<br>"+"or not","RW",2));
				D_OUT.writeBytes(padSTRING1('V',"Whether young"+"<br>"+"persons& or not","RW",2));
				D_OUT.writeBytes(padSTRING1('V',"Time at which "+"<br>"+"employment commences","RW",2));
				D_OUT.writeBytes(padSTRING1('V',"Time at which "+"<br>"+"employment ceases","RW",2));
				D_OUT.writeBytes(padSTRING1('V',"Rest Interval "+"<br>"+"(1:00 to 1:30)","RW",2));
				D_OUT.writeBytes(padSTRING1('C',"HOURS WORKED ON","CL",7));
				D_OUT.writeBytes(padSTRING1('V',"Total hours worked "+"<br>"+"during the week","RW",2));
				D_OUT.writeBytes(padSTRING1('C',"Days&nbsp;on&nbsp;which&nbsp;overtime&nbsp;work is&nbsp;done&nbsp;and&nbsp;extent&nbsp;of such overtime&nbsp;on&nbsp;each&nbsp;occasion","CL",7));
				D_OUT.writeBytes(padSTRING1('V',"Extent&nbsp;of&nbsp;overtime"+"<br>"+"worked&nbsp;during&nbsp;the&nbsp;week","RW",2));
				D_OUT.writeBytes(padSTRING1('V',"Extent&nbsp;of&nbsp;overtime&nbsp;worked"+"<br>"+"Previously&nbsp;during&nbsp;the&nbsp;week","RW",2));
				D_OUT.writeBytes("</tr>");
				
				D_OUT.writeBytes("<tr>");
				
				D_OUT.writeBytes(padSTRING1('V',"Sunday "+"<br>"+M_strSTRDT,null,0));
				M_calLOCAL.setTime(M_fmtLCDAT.parse(M_strSTRDT));      
				M_calLOCAL.add(Calendar.DATE,1);
			
				D_OUT.writeBytes(padSTRING1('V',"Monday "+"<br>"+M_fmtLCDAT.format(M_calLOCAL.getTime()),null,0));
				M_calLOCAL.add(Calendar.DATE,1);
				D_OUT.writeBytes(padSTRING1('V',"Tuesday "+"<br>"+M_fmtLCDAT.format(M_calLOCAL.getTime()),null,0));
				M_calLOCAL.add(Calendar.DATE,1);
				D_OUT.writeBytes(padSTRING1('V',"Wednesday "+"<br>"+M_fmtLCDAT.format(M_calLOCAL.getTime()),null,0));
				M_calLOCAL.add(Calendar.DATE,1);
				D_OUT.writeBytes(padSTRING1('V',"Thursday "+"<br>"+M_fmtLCDAT.format(M_calLOCAL.getTime()),null,0));
				M_calLOCAL.add(Calendar.DATE,1);
				D_OUT.writeBytes(padSTRING1('V',"Friday "+"<br>"+M_fmtLCDAT.format(M_calLOCAL.getTime()),null,0));
				M_calLOCAL.add(Calendar.DATE,1);
				D_OUT.writeBytes(padSTRING1('V',"Saturday "+"<br>"+M_fmtLCDAT.format(M_calLOCAL.getTime()),null,0));
				
				D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
				D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
				D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
				D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
				D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
				D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
				D_OUT.writeBytes(padSTRING1('C',"&nbsp;",null,0));
				D_OUT.writeBytes("</tr>");
				*/
  		    }
    		/** for Monthly Attendance Register as per Factoy act Form J */
    		else if(rdbREGMT.isSelected())
  		    {
    			if(rdbLEFT.isSelected())// LEFT page Header
    			{
	    			crtTBL(D_OUT,false);
	    			D_OUT.writeBytes("<br><br><br>");
	    			D_OUT.writeBytes("<tr><td align='center' width='20%' height='13' rowspan='3'><IMG src=\\\\192.168.10.207\\user\\exec\\splerp3\\spllogo_old.gif style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></td><tr><td align=center><STRONG><FONT face=Arial size=4>REGISTER OF ADULT WORKERS</STRONG></FONT></td><td width='20%'></td></tr>" +
							"<tr><td align=center><FONT face=Arial size=2>Muster Roll and Register of Employees</FONT></td></tr>" +
							"<tr><td><small><small>"+M_strADDRS+"</small></small></td><td align=center><STRONG><FONT face=Arial size=2>(All Combined)</FONT></STRONG></td><td>Month:  "+M_strMNTNM+"</td></tr></table>");
	    			D_OUT.writeBytes("<br><br><br>");
				    crtTBL(D_OUT,true);
				    D_OUT.writeBytes("<tr>");
				    D_OUT.writeBytes(padSTRING1('C',"Sr."+"<br>"+"No.","RW",2));
					D_OUT.writeBytes(padSTRING1('C',"Emp No","RW",2));
					D_OUT.writeBytes(padSTRING1('C',"Employee Name","RW",2));
					D_OUT.writeBytes(padSTRING1('C',"Designation","RW",2));
					D_OUT.writeBytes(padSTRING1('C',"GRP Letter","RW",2));
					D_OUT.writeBytes(padSTRING1('C',"No.of Relay","CL",5));
					
					for(int n=1;n<=vtrWRKDT.size();n++)
					{
						D_OUT.writeBytes(padSTRING1('C',String.valueOf(n),"RW",2));/**print total month day*/
					}
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('C',"1",null,0));
					D_OUT.writeBytes(padSTRING1('C',"2",null,0));
					D_OUT.writeBytes(padSTRING1('C',"3",null,0));
					D_OUT.writeBytes(padSTRING1('C',"4",null,0));
					D_OUT.writeBytes(padSTRING1('C',"5",null,0));
					D_OUT.writeBytes("</tr>");
    			}
    			else// RIGHT page Header
    			{
    				crtTBL(D_OUT,false);
    				//D_OUT.writeBytes("<br>");
        			D_OUT.writeBytes("<tr><td width='45%'>&nbsp;</td><td width='15%'>&nbsp;</td><td align='left' width='10%'><FONT face=Arial size=2>Relay Group</td><td width='30%'><FONT face=Arial size=1>A 08-30 to 12-30&nbsp;&nbsp;13-00 to 17-00</font></td></tr>"+
        					"<tr><td align='center'><STRONG><FONT face=Arial size=5>"+cl_dat.M_strCMPNM_pbst+"</STRONG></FONT><td>&nbsp;</td></td>&nbsp;<td></td><td><FONT face=Arial size=1>B 11-30 to 14-30&nbsp;&nbsp;14-00 to 19-00</fobt></td></tr>" +
    						"<tr><td align='center'><FONT face=Arial size=2>From 17 and 29 prescribed under Maharashtra Factories Rules 99 and 122</FONT><td>&nbsp;</td><td>&nbsp;</td><td><FONT face=Arial size=1>C 07-30 to 11-30&nbsp;&nbsp;11-00 to 15-00</font></td></tr>" +
    						
    						"<tr><td align='center'><FONT face=Arial size=2><STRONG>E.S.I.Regulation - 32 (ALL Combined)</STRONG></FONT></td><td>&nbsp;</td><td>&nbsp;</td><td><FONT face=Arial size=1>D 15-00 to 19-00&nbsp;&nbsp;19-30 to 23-00</font></td></tr>" +
    						"<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td><FONT face=Arial size=1>E 23-00 to 03-00&nbsp;&nbsp;03-30 to 07-00</font></td></tr>" +
    						
    						"<tr><td align='center'>&nbsp;</td><td>&nbsp;</td><td align='left' width='40%' height='42' colspan=2'><FONT face=Arial size=2>Workers in Group "+"B"+" to "+ "D"+" will work in one of the shifts....</font></td></tr>" +
        					"<tr><td></td><td>&nbsp;</td><td  align='left' width='50%' colspan=2'><FONT face=Arial size=2>Month: "+M_strMNTNM+"     "+ "Working days: " +M_intLSTDT+"     "+"Dept :"+lblDPTNM.getText()+"</font></td></tr>" );
        			
    			    crtTBL(D_OUT,true);
    			    D_OUT.writeBytes("<tr>");
    			    D_OUT.writeBytes(padSTRING1('C',"Sr.No.","RW",2));
    			    D_OUT.writeBytes(padSTRING1('C',"Present","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"Holiday","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"Casual Leave","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"Sick Leave","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"Privilege Leave","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"Total","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"L.W.P","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"Total","CL",2));
    				D_OUT.writeBytes(padSTRING1('C',"REMARKS","RW",2));
    				D_OUT.writeBytes("</tr>");
    				
    				D_OUT.writeBytes("<tr>");
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Days",null,0));
    				D_OUT.writeBytes(padSTRING1('C',"Hrs",null,0));
    				D_OUT.writeBytes("</tr>");
      		    }
  		    }
			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</font></b>");		
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
			//D_OUT.writeBytes("<p><TABLE border=0 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width=\"50%\" align=left>");
			crtTBL(D_OUT,false);
			/**display leave code & description in footer*/
			if(rdbLEFT.isSelected() || rdbREGWK.isSelected())
  		    {
				 D_OUT.writeBytes("<tr>");
				for(int i=0;i<vtrLVECD.size();i++)
				{
					 D_OUT.writeBytes("<td borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width='3%' align='left'><font size=2>");
					 if(!vtrLVECD.get(i).toString().equals("WO"))
						 D_OUT.writeBytes(vtrLVECD.get(i).toString()+" - "+hstLVEDS.get(vtrLVECD.get(i)));
					 else
					 {
						 if(rdbREGWK.isSelected())
							 D_OUT.writeBytes(vtrLVECD.get(i).toString()+" - "+"Weekly Off");
						 else if(rdbLEFT.isSelected())
							 D_OUT.writeBytes("|"+" - "+"Weekly Off");
					 }
					 D_OUT.writeBytes("</font ></td>");
				}
  		    }
			
			D_OUT.writeBytes("</tr>");
			D_OUT.writeBytes("</p></TABLE>");
			 
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}
	
	boolean vldDATA()
	{
		try
		{
			if(rdbREGWK.isSelected() && txtWKEDT.getText().trim().length()== 0)
			{
			  setMSG("Enter Weekly Ending Date..",'E');
			  txtWKEDT.requestFocus();
			  return false;
			}
			else  if(rdbREGMT.isSelected())
			{
				if(txtMONTH.getText().trim().length()== 0)
				{
				  setMSG("Please Enter Month..",'E');
				  txtMONTH.requestFocus();
				  return false;
				}
				else if(txtMONTH.getText().trim().length()>0 && txtYEAR.getText().trim().length()== 0)
				{
				  setMSG("Please Enter Year..",'E');
				  txtYEAR.requestFocus();
				  return false;
				}
				else if((rdbLEFT.isSelected()==false) && (rdbRIGHT.isSelected()==false))
				{
					rdbLEFT.requestFocus();
					setMSG("Select either LEFT or RIGHT Button",'E');
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
			vtrWRKDT.clear();
			vtrWEKDT.clear();
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rperg.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rperg.doc";
		
			if(rdbREGWK.isSelected())
			{
				M_calLOCAL.setTime(M_fmtLCDAT.parse(txtWKEDT.getText())); 
				M_calLOCAL.add(Calendar.DATE, 7 - M_calLOCAL.get(Calendar.DAY_OF_WEEK));
				M_strENDDT=M_fmtLCDAT.format(M_calLOCAL.getTime());/**Week end date*/
				//System.out.println("M_strENDDT"+M_strENDDT);
				
				M_calLOCAL.add(Calendar.DATE, 7 - M_calLOCAL.get(Calendar.DAY_OF_WEEK)-6);
				M_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());/**week Start date*/
				//System.out.println("M_strSTRDT"+M_strSTRDT);
				
				/**Store Week date fron  start date to  end date in vector*/
				vtrWEKDT.add(M_strSTRDT);
				for(int i=1;i<7;i++)
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(M_strSTRDT));
					M_calLOCAL.add(Calendar.DATE,i);
					String strWRKDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
					if(!vtrWEKDT.contains(strWRKDT))
						vtrWEKDT.add(strWRKDT);
				}
				//System.out.println("vtrWEKDT"+vtrWEKDT);
				genWEKDL();
			}
				
			else if(rdbREGMT.isSelected())
			{
				M_strSTRDT="01/"+txtMONTH.getText()+"/"+txtYEAR.getText();/**Month start date*/
				M_calLOCAL.setTime(M_fmtLCDAT.parse(M_strSTRDT)); 
				M_intLSTDT = M_calLOCAL.getActualMaximum(Calendar.DATE);
				M_strENDDT=M_intLSTDT+"/"+txtMONTH.getText()+"/"+txtYEAR.getText();/**Month End date*/
				//System.out.println("M_strENDDT"+M_strENDDT);
				/**Store total Month Date in vector*/
				vtrWRKDT.add(M_strSTRDT);
				for(int i=1;i<M_intLSTDT;i++)
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(M_strSTRDT));
					M_calLOCAL.add(Calendar.DATE,i);
					String strWRKDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
					if(!vtrWRKDT.contains(strWRKDT))
						vtrWRKDT.add(strWRKDT);
				}
				
				M_strMNTNM= M_strMONTH[M_calLOCAL.get(Calendar.MONTH)]+","+M_calLOCAL.get(Calendar.YEAR);/**String Variable to store Month Name*/
				genMNTDL();
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
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
	
	
    /**
    Method to add time given in second parameter to the time in first parameter 
    and returns the result in HH:MM format
	@param P_strSTRTM String argument to pass Starting Time.
	@param P_strNEWTM String argument to pass New Time.
    */
    private String addTIME(String P_strSTRTM,String P_strNEWTM)
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
            int  L_intTOTHR = (L_intSTRMN+L_intNEWMN) / 60;
            int  L_intTOTMN = (L_intSTRMN+L_intNEWMN)- ((L_intSTRMN+L_intNEWMN)/60)*60;
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
		    setMSG(L_EX, "addTIME");
    		return "";
		}            
    }

class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
				return true;
			if(input == txtWKEDT)
			{
				if(M_fmtLCDAT.parse(txtWKEDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))> 0)
				{
					setMSG("Weekly Ending Date can not be greater than Current date time..",'E');
					return false;
				}
			}
			else if(input == txtMONTH)
			{ 
				if(txtMONTH.getText().length()==1)
					txtMONTH.setText("0"+txtMONTH.getText());
				else if(txtMONTH.getText().equals("00"))
				{
					setMSG("Enter valid Month..",'E');
					return false;
				}
				else if(txtMONTH.getText().compareTo("12")>0)
				{
					setMSG("Enter valid Month..",'E');
					return false;
				}
				else if(Integer.parseInt(txtMONTH.getText())>Integer.parseInt(cl_dat.M_strLOGDT_pbst.substring(3,5)))
				{
					setMSG("Month can not be greater than Current Month..",'E');
					return false;
				}
			}
			else if(input == txtYEAR)
			{ 
				if(Integer.parseInt(txtYEAR.getText())>Integer.parseInt(cl_dat.M_strLOGDT_pbst.substring(6)))
				{
					setMSG("Year can not be greater than Current Year..",'E');
					return false;
				}
				else if(txtYEAR.getText().length()<4)
				{
					setMSG("please Enter valid Year..",'E');
					return false;
				}
			}
			else if(input == txtDPTCD)
			{
				M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and CMT_CODCD = '"+txtDPTCD.getText()+"'";
				//System.out.println("INPVF DPTCD : "+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					txtDPTCD.setText(M_rstRSSET.getString("CMT_CODCD"));
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
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
}



