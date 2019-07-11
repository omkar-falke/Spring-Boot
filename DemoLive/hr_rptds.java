/*
System Name : Human Resurce Management System.
Program Name :  Tour Duty.
 
Source Directory : d:\source\splerp3\hr_rptds.java                       

Executable Directory : d:\exec\splerp3\hr_rptds.class

Purpose : This module use for Tour Duty Authority report.

List of tables used :
Table Name		Primary key							                      Operation done
													                      Insert	Update	   Query    Delete	
--------------------------------------------------------------------------------------------------------------------
HR_LVTRN		LVT_CMPCD,LVT_EMPNO,LVT_LVEDT		    										/		
HR_EPMST		ep_dptnm,ep_shrnm,ep_empno														/
--------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	    Coulmn Name			 Type/Size	        Description					Table Name
--------------------------------------------------------------------------------------------------------------------
txtEMPNO        LVT_EMPNO 		     varchar(4)         Employee code 				HR_LVTRN
lblEMPNM        EP_FULNM    	     varchar(50)        Employee Name 				HR_EPMST
txtSTRDT        LVT_REFDT		     date               Tour Duty Starting Date		HR_LVTRN

--------------------------------------------------------------------------------------------------------------------

List of fields with help facility: 
Field Name	  Display Description		    	Display Columns				Table Name
--------------------------------------------------------------------------------------------------------------------
txtEMPNO	  Employee No,Employee Name			LVT_EMPNO,EP_FULNM       	HR_LVTRN,HR_EPMST
txtSTRDT	  Employee No, Ref.Date(Tour Duty)	LVT_EMPNO,LVT_REFDT			HR_LVTRN	
--------------------------------------------------------------------------------------------------------------------

Validations :

	->Fetch records from hr_lvtrn(type of leave) & hr_epmst(Employee related) for Tour Duty Slip.
	->Enter Tour Duty Starting Date to display report as per Date.

Requirement :

	->Generated Tour Duty Slip will be printed in HTML format.
	->In Tour Duty Slip Employee related data, period for tour duty, Visiting Organization & Mode of Journey is display.
	->Generated Employees on Tour duty in help screen for Tour duty slip .
	->Tour Duty Date of corresponding Employees will be generated from hr_lvtrn.*/


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.InputVerifier;
import java.util.StringTokenizer;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

class hr_rptds extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rptds.html";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										
	private FileOutputStream F_OUT ;  /** FileOutputStream for generated report file handeling.*/
    private DataOutputStream D_OUT ;/** DataOutputStream to hold Stream of report data.*/ 
  
    private JTextField txtEMPNO;
    private JLabel lblEMPNM;
    private JTextField txtSTRDT;
  
    private boolean flgTBLDT;
    private INPVF oINPVF;
  
	hr_rptds()		/*  Constructor   */
	{
		super(1);
		try
		{
			setMatrix(20,8);	
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			add(new JLabel("Employee No."),4,3,1,1,this,'L');   
		    add(txtEMPNO = new TxtLimit(4),4,4,1,1,this,'L');
		    add(lblEMPNM = new JLabel(),4,5,1,5,this,'L');
			add(new JLabel("TD Starting Date"),5,3,1,1,this,'L');   
		    add(txtSTRDT = new TxtDate(),5,4,1,1,this,'L');  
		    
		    oINPVF=new INPVF();
    		txtEMPNO.setInputVerifier(oINPVF);
    		txtSTRDT.setInputVerifier(oINPVF);
    		
    		M_rdbHTML.setSelected(true);
			M_pnlRPFMT.setVisible(true);
			
			setENBL(false);	
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
				  setENBL(true);
					txtEMPNO.requestFocus();
					if((txtEMPNO.getText().trim().length()==0))
					{
					  setMSG ("Please Enter Employee No..",'N');	
					}
			  }
			  else
					setENBL(false);
			}
			if(M_objSOURC == txtEMPNO)
			{
				if(txtEMPNO.getText().length()==0)
					lblEMPNM.setText("");
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
			if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtEMPNO)
				   txtSTRDT.requestFocus();
				else if(M_objSOURC == txtSTRDT)
				   cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
			    if(M_objSOURC==txtEMPNO)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strHLPFLD = "txtEMPNO";
					String L_ARRHDR[] = {"Employee No","Employee Name"};
					M_strSQLQRY = " select LVT_EMPNO,EP_FULNM from hr_lvtrn,hr_epmst where ep_cmpcd=lvt_cmpcd and ep_empno= lvt_empno and isnull(ep_stsfl,'X')  <> 'U' and ep_lftdt is null ";
					M_strSQLQRY += " and lvt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and lvt_lvecd='TD' and isnull(lvt_stsfl,' ') <> 'X' and year(lvt_lvedt) =  (select year(ep_yopdt) ";
					M_strSQLQRY += " from hr_epmst where ep_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ep_empno = lvt_empno)";
					if(txtEMPNO.getText().length() >0)				
						M_strSQLQRY += " AND LVT_EMPNO like '"+txtEMPNO.getText().trim()+"%'";
					M_strSQLQRY += " group by LVT_EMPNO,EP_FULNM order by LVT_EMPNO,EP_FULNM ";
    				//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
    				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    				setCursor(cl_dat.M_curDFSTS_pbst);
    			}
			    else if(M_objSOURC==txtSTRDT)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strHLPFLD = "txtSTRDT";
					String L_ARRHDR[] = {"Employee No","Ref.Date(Tour Duty)"};
					M_strSQLQRY = " select LVT_EMPNO,convert(varchar,LVT_REFDT,103) LVT_REFDT from hr_lvtrn where lvt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and lvt_lvecd='TD' and lvt_empno='"+txtEMPNO.getText()+"' ";
					M_strSQLQRY += " and isnull(lvt_stsfl,' ') <> 'X' and year(lvt_lvedt) =  (select year(ep_yopdt) from hr_epmst where ep_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ep_empno = lvt_empno and isnull(ep_stsfl,'X')  <> 'U' and ep_lftdt is null)";
					M_strSQLQRY += " group by LVT_EMPNO,LVT_REFDT order by LVT_EMPNO,LVT_REFDT "; 
					//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
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
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
				  StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			      txtEMPNO.setText(L_STRTKN.nextToken());
				  lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
			else if(M_strHLPFLD.equals("txtSTRDT"))
			{
				  StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				  txtEMPNO.setText(L_STRTKN.nextToken());
			      txtSTRDT.setText(L_STRTKN.nextToken());
				  
			}
			
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	/**method to generate Tour Duty Slip*/
	void genRPTFL()
	{
		try
		{
			if(!vldDATA())
				return;
			String L_strEMPNO="", L_strAPPDT="",L_strDESGN="",L_strMMGRD="",L_strDPTNM="",L_strCONNO="",L_strPLCNM="";
			String L_strORGNM="",L_strRSNDS="", L_strRCMBY="",L_strSCNBY="",L_strMOJCD="",L_strLVEDT="",L_strENDDT="",L_strSHRNM="";
		
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			genRPHDR();
			
			
			M_strSQLQRY= " select lvt_empno,ep_dptnm,ep_shrnm,lvt_desgn,lvt_mmgrd,lvt_conno,lvt_lvedt,lvt_refdt,lvt_appdt,lvt_plcnm,lvt_orgnm,lvt_rsnds,lvt_mojcd,lvt_rcmby,lvt_scnby from hr_lvtrn,hr_epmst where lvt_empno=ep_empno ";
			M_strSQLQRY+= " and lvt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and lvt_empno='"+txtEMPNO.getText()+"' and lvt_lvecd='TD' ";
			M_strSQLQRY+= " and lvt_refdt='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strEMPNO=nvlSTRVL(M_rstRSSET.getString("lvt_empno"),"");
					L_strAPPDT=M_fmtLCDAT.format(M_rstRSSET.getDate("lvt_appdt"));
					L_strDESGN=nvlSTRVL(M_rstRSSET.getString("lvt_desgn"),"");
					L_strMMGRD=nvlSTRVL(M_rstRSSET.getString("lvt_mmgrd"),"");
					L_strDPTNM=nvlSTRVL(M_rstRSSET.getString("ep_dptnm"),"");
					L_strLVEDT=M_fmtLCDAT.format(M_rstRSSET.getDate("lvt_lvedt"));
					L_strCONNO=nvlSTRVL(M_rstRSSET.getString("lvt_conno"),"");
					L_strPLCNM=nvlSTRVL(M_rstRSSET.getString("lvt_plcnm"),"");
					L_strORGNM=nvlSTRVL(M_rstRSSET.getString("lvt_orgnm"),"");
					L_strRSNDS=nvlSTRVL(M_rstRSSET.getString("lvt_rsnds"),"");
					L_strRCMBY=nvlSTRVL(M_rstRSSET.getString("lvt_rcmby"),"");
					L_strSCNBY=nvlSTRVL(M_rstRSSET.getString("lvt_scnby"),"");
					L_strMOJCD=nvlSTRVL(M_rstRSSET.getString("lvt_mojcd"),"");
					L_strSHRNM=nvlSTRVL(M_rstRSSET.getString("ep_shrnm"),"");
				}
				
					L_strENDDT=L_strLVEDT;
					//System.out.println("L_strENDDT"+L_strENDDT);
				
				    D_OUT.writeBytes("<b>"+padSTRING('L',"Date of Application : "+L_strAPPDT,89)+"</b>");
				  
					crtTBL(D_OUT,true);
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('C',"Name of the Employee",30));
					D_OUT.writeBytes("<td width='70%' colspan=2>");
					D_OUT.writeBytes(padSTRING('C',lblEMPNM.getText(),50));
					D_OUT.writeBytes("</td >");
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('C',"Employee Code Number",30));
					D_OUT.writeBytes(padSTRING1('C',"Department",15));
					D_OUT.writeBytes(padSTRING1('C',"Designation / Grade",30));
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING2('C',chkZERO(L_strEMPNO),25));
					D_OUT.writeBytes(padSTRING2('C',chkZERO(L_strDPTNM),25));
					D_OUT.writeBytes(padSTRING2('C',chkZERO(L_strDESGN)+" / "+L_strMMGRD,30));
					D_OUT.writeBytes("</tr>");
				/*	D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('C',"Contact Number",25));
					D_OUT.writeBytes("<td width='70%' colspan=2>");
					D_OUT.writeBytes(padSTRING('C',chkZERO(L_strCONNO),25));
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("</tr>");*/
					endTABLE(D_OUT);
					
					crtTBL(D_OUT,true);
					D_OUT.writeBytes("<b><font face=Arial>"+padSTRING('C',"I have to undertaken an Official Tour from (Date)  "+txtSTRDT.getText()+"   to (Date)  "+L_strENDDT+"  as under:",100)+"</font></b>");
					D_OUT.writeBytes("\n\n");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('C',"PLACE (S)",10));
					D_OUT.writeBytes(padSTRING1('C',"VISITING ORGANISATION (S)",50));
					D_OUT.writeBytes(padSTRING1('C',"PURPOSE (S)",15));
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING2('C',chkZERO(L_strPLCNM),25));
					D_OUT.writeBytes(padSTRING2('C',chkZERO(L_strORGNM),25));
					D_OUT.writeBytes(padSTRING2('C',chkZERO(L_strRSNDS),25));
					D_OUT.writeBytes("</tr>");
					
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes("<td>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td height='30'>");
					D_OUT.writeBytes("<td height='30'>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("<td height='30'>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("</tr>");
					
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes("<td height='30'>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("<td height='30'>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td height='30'>");
					D_OUT.writeBytes("<td>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("</tr>");
					endTABLE(D_OUT);
					
					crtTBL(D_OUT,true);
					D_OUT.writeBytes("<b><font face=Arial>"+padSTRING('L',"MODE OF JOURNEY",60)+"</font></b>");
					D_OUT.writeBytes("\n\n");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('C',"Co/Own Vehicle",25));
					D_OUT.writeBytes(padSTRING1('C',"Bus",25));
					D_OUT.writeBytes(padSTRING1('C',"Train",25));
					D_OUT.writeBytes(padSTRING1('C',"Air",25));
					D_OUT.writeBytes(padSTRING1('C',"Hired Vehicle",25));
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					//System.out.println("L_strMOJCD"+L_strMOJCD);
					if(L_strMOJCD.length()>0)
					{
						if(L_strMOJCD.substring(0,1).equals("C"))
							D_OUT.writeBytes(padSTRING2('C',"Y",25));
						else
							D_OUT.writeBytes(padSTRING2('C',"-",25));
						if(L_strMOJCD.substring(1,2).equals("B"))
							D_OUT.writeBytes(padSTRING2('C',"Y",25));
						else
							D_OUT.writeBytes(padSTRING2('C',"-",25));
					
						if(L_strMOJCD.substring(2,3).equals("T"))
							D_OUT.writeBytes(padSTRING2('C',"Y",25));
						else
							D_OUT.writeBytes(padSTRING2('C',"-",25));
						if(L_strMOJCD.substring(3,4).equals("A"))
							D_OUT.writeBytes(padSTRING2('C',"Y",25));
						else
							D_OUT.writeBytes(padSTRING2('C',"-",25));
						if(L_strMOJCD.substring(4,5).equals("H"))
							D_OUT.writeBytes(padSTRING2('C',"Y",25));
						else
							D_OUT.writeBytes(padSTRING2('C',"-",25));
					}
					
					D_OUT.writeBytes("</tr>");
					endTABLE(D_OUT);
					
					D_OUT.writeBytes("\n");
					
					crtTBL(D_OUT,true);
					D_OUT.writeBytes("<td align='center' height='37'>");
					D_OUT.writeBytes(padSTRING('C',chkZERO(L_strSHRNM),25));
					D_OUT.writeBytes("</td >");
				    D_OUT.writeBytes("<td align='center' height='37'>");
					D_OUT.writeBytes(padSTRING('C',chkZERO(L_strRCMBY),25));
					D_OUT.writeBytes("</td >");
					D_OUT.writeBytes("<td align='center' height='37'>");
					D_OUT.writeBytes(padSTRING('C',chkZERO(L_strSCNBY),25));
					D_OUT.writeBytes("</td >");
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('C',"Applicant",25));
					D_OUT.writeBytes(padSTRING1('C',"Recommended by",25));
					D_OUT.writeBytes(padSTRING1('C',"Approved by",25));
					D_OUT.writeBytes("</tr>");
					endTABLE(D_OUT);	
					
					D_OUT.writeBytes("\n");
					crtTBL(D_OUT,true);
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING1('R',"Contact Address & Telephone No. While on Tour :-",50));
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes(padSTRING2('R',chkZERO(L_strCONNO),25));
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes("<td height='30'>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("</tr>");
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes("<td height='30'>");
					D_OUT.writeBytes("<p>&nbsp;</p>");
					D_OUT.writeBytes("</td>");
					D_OUT.writeBytes("</tr>");
					endTABLE(D_OUT);	
					D_OUT.writeBytes("\n");
					D_OUT.writeBytes("<font size='2'>"+padSTRING('L',"Computer Generated Document, Hence Signature not required",88)+"</font>");
					
			}
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
			setMSG(L_EX,"Report1");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	
	/**Method to creat HTML Table*/
	
	private void crtTBL(DataOutputStream L_DOUT,boolean P_flgBORDR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			D_OUT.writeBytes("\n"); 
			if(P_flgBORDR)
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray  cellPadding=0 cellSpacing=0  width=\"84%\" align=center>");
			else
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width=\"60%\"  align=left>");
			flgTBLDT=true;
		}
	}
	
	private void endTABLE(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbHTML.isSelected())
			L_DOUT.writeBytes("</TABLE></P>");
		flgTBLDT=false;
	}
	
	
	/**Method to create html column format.
	 * 
	 */	
	protected  String padSTRING1(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		String strTXCLR= " ";
		try
		{
			String L_STRSP = " ";
			
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
		
				strTXCLR="<font face=Arial size=2>";
			
			if(P_intPADLN <= L_STRLN && M_rdbTEXT.isSelected())
			{
				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			if(M_rdbHTML.isSelected())
			{
				if(P_chrPADTP=='C')
					P_strTRNVL="<p Align = center>"+strTXCLR+P_strSTRVL+"</P>";
				else if(P_chrPADTP=='R')
					P_strTRNVL="<p Align = left>"+strTXCLR+P_strSTRVL+"</P>";
				else if(P_chrPADTP=='L')
					P_strTRNVL="<p Align = right>"+strTXCLR+P_strSTRVL+"</P>";
				
				if(flgTBLDT)
					P_strTRNVL="<td width='20%' height='30'><b>"+strTXCLR+P_strTRNVL+"</b></td>";
				
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"padSTRING");
		}
		return P_strTRNVL;
	}
	
	/**Method to create html column format.
	 * 
	 */	
	protected  String padSTRING2(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		String strTXCLR= " ";
		try
		{
			String L_STRSP = " ";
			
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
		
				strTXCLR="<font size=3>";
			
			if(P_intPADLN <= L_STRLN && M_rdbTEXT.isSelected())
			{
				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			if(M_rdbHTML.isSelected())
			{
				if(P_chrPADTP=='C')
					P_strTRNVL="<p Align = center>"+strTXCLR+P_strSTRVL+"</P>";
				else if(P_chrPADTP=='R')
					P_strTRNVL="<p Align = left>"+strTXCLR+P_strSTRVL+"</P>";
				else if(P_chrPADTP=='L')
					P_strTRNVL="<p Align = right>"+strTXCLR+P_strSTRVL+"</P>";
				
				if(flgTBLDT)
					P_strTRNVL="<td width='20%' height='30'>"+strTXCLR+P_strTRNVL+"</td>";
				
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"padSTRING2");
		}
		return P_strTRNVL;
	}
	
	
	private String chkZERO(String LP_STR)
	{
		try
		{
			if(LP_STR.equals(""))
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
	
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >60)
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
	
	

	/**method to print report header**/
	
	void genRPHDR()
	{
		try
		{
			String L_strEMPTY="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
				
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<br><br><br>");
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title></title><p>"+ L_strEMPTY+"</p><p>"+ L_strEMPTY+"</p><P align=center><STRONG><u><FONT face=Arial size=5>"+cl_dat.M_strCMPNM_pbst+"</u></FONT></STRONG></P><P align=center><STRONG><u><FONT face=Arial size=3>"+cl_dat.M_strCMPLC_pbst+"</u></FONT></STRONG></P>" +
								"<P align=center><STRONG><u><FONT face=Arial size=3>Tour Duty Authority Slip</u></FONT></STRONG></P>" +
								//"<P align=right><STRONG><FONT face=Arial size=2>Date of Application :"+txtSTRDT.getText()+"</FONT></STRONG></P>"+
								" </HEAD> <BODY><P><PRE style =\" font-size :10 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
			
	/**method to report footer***/
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------");
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
	
	boolean vldDATA()
	{
		try
		{
			if(txtEMPNO.getText().trim().length()== 0)
			{
			  setMSG("Enter Employee No..",'E');
			  txtEMPNO.requestFocus();
			  return false;
			}
			else if(txtSTRDT.getText().trim().length()== 0)
			{
			  setMSG("Enter From Date..",'E');
			  txtSTRDT.requestFocus();
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
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rptds.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rptds.doc";
			genRPTFL();
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
			if(input == txtEMPNO)
			{
				if(txtEMPNO.getText().trim().length()== 0)
				{
				  setMSG("Enter Employee No..",'E');
				  txtEMPNO.requestFocus();
				  return false;
				}
			
				M_strSQLQRY = " select LVT_EMPNO,EP_FULNM from hr_lvtrn,hr_epmst where ep_cmpcd=lvt_cmpcd and ep_empno= lvt_empno and isnull(ep_stsfl,'X')  <> 'U' and ep_lftdt is null ";
				M_strSQLQRY += " and lvt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and lvt_lvecd='TD' and isnull(lvt_stsfl,' ') <> 'X' and year(lvt_lvedt) =  (select year(ep_yopdt) ";
				M_strSQLQRY += " from hr_epmst where ep_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ep_empno = lvt_empno) and LVT_EMPNO='"+txtEMPNO.getText()+"'";
				//System.out.println("INPVF EMPNO : "+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblEMPNM.setText(M_rstRSSET.getString("EP_FULNM").replace('|',' '));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Employee Code on Tour Duty ",'E');
					return false;
				}
				M_rstRSSET.close();
			}
			if(input == txtSTRDT)
			{
				if(txtSTRDT.getText().trim().length()== 0)
				{
				  setMSG("Enter Tour Duty Starting Date..",'E');
				  txtSTRDT.requestFocus();
				  return false;
				}
				M_strSQLQRY = " select LVT_EMPNO,LVT_REFDT from hr_lvtrn where lvt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and lvt_lvecd='TD' and lvt_empno='"+txtEMPNO.getText()+"' ";
				M_strSQLQRY += " and isnull(lvt_stsfl,' ') <> 'X' and year(lvt_lvedt) =  (select year(ep_yopdt) from hr_epmst where ep_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ep_empno = lvt_empno) and LVT_REFDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"'";
				
				//System.out.println("INPVF LVT_REFDT : "+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					txtSTRDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT")));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Tour Duty Starting Date",'E');
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



