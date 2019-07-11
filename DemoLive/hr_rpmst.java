/*
System Name : Human Resurce Management System.
Program Name :  Manpower Status.
 
Source Directory : d:\source\splerp3\hr_rpmst.java                        Executable Directory : d:\exec\splerp3\hr_rpmst.class

Purpose : This module displays Manpower Status report.

List of tables used :
Table Name		Primary key							                      Operation done
													                      Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
				    
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	    TextField Name			 Type/Size	       Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtFMDAT         From date               date              From Date
txtTODAT         To date                 date              To Date   
-----------------------------------------------------------------------------------------------------------------------------------------------------
Validations :
->Enter From Date.
->Enter To Date.
->From Date can not be greater than Current date time.
->To Date can not be greater than Current date time.
->To Date can not be smaller than From date.

Requirement :
->Display Manpower report ,when click on option button of Manpower detail.
->generate Department & Designation wise Designation count.
->generate Technical & non-technical department & designation wise records & total in Manpower report.
->print current total of officers & TM and Grand total of all.
->Display Employee detail report to employees trun around details.

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import java.util.*;
import java.util.Enumeration;
import java.util.Calendar;
import java.sql.Date;

class hr_rpmst extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpmst.html";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										
	private FileOutputStream F_OUT ;  /** FileOutputStream for generated report file handeling.*/
    private DataOutputStream D_OUT ;/** DataOutputStream to hold Stream of report data.*/ 
    private String strDOTLN = "---------------------------------------------------------------------------------------------------------------------------------";
    private JTextField txtFMDAT;
    private JTextField txtTODAT;
    private String strFMDAT,strTODAT,strCRDAT;
    private String strEPREM="";
    private String strHRREM="";
   
    private boolean flgTBLDT;
    private Hashtable<String,String> hstCNTDS;// hashtable to store designation count.
    private Hashtable<String,String> hstEMPCT; // hashtable to store employee category.
    private Hashtable<String,String> hstDPTTP;  // hashtable to store Department type.
  
    private Vector<String> vtrDPTNM; 
    private Vector<String> vtrDESGN;
    private Vector<String> vtrSMDSG; /** vector to store designation for Summery data.*/
   
    private JRadioButton  rdbMANSM;
    private JRadioButton  rdbMANDL;
	private JRadioButton  rdbEMPDL;
	private ButtonGroup   btgEMPDL;
    
	String L_strDPTTP="";
	String L_strCNTDS=""; 
	String L_strEMPCT="";
	String L_strDESGN="";
	
	int L_intSMDSG=0; /**Variable for store total count of Designation in summery table.**/
	
	int L_intOFFCR=0;/**Variable for store Grand total of officers**/
	int L_intTM=0;/**Variable for store Grand total of TM**/
	int intTOTDS=0;
	int L_intCNTDS=0; /**Variable for store designation count**/
	
	
	int[] arrDESGN = new int[50];  
	int[] arrDESGN1 = new int[100];   /**array variable for Store designation total of Technical Depts.*/
	int[] arrDESGN2 = new int[100];  /**array variable for Store designation total of Non-Technical Depts.*/

	int intINDEX = 0; 
	int intINDEX1= 0; /** Integer Variable for indicate index of Technical Depts  array .*/
	int intINDEX2= 0;  /** Integer Variable for indicate index of non-Technical Depts  array .*/
	
	int L_intCNTOF = 0; 
	int L_intCNTOF1= 0; /** Integer Variable for store current total of officers in technical & non-technical depts. */
	int L_intCNTOF2= 0; 
	int L_intCNTTM = 0; /** Integer Variable for store current total of TM in technical& non-technical depts. */
	int L_intCNTTM1= 0; 
	int L_intCNTTM2= 0; 

	int L_intGRAND = 0;
	
	hr_rpmst()		/*  Constructor   */
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
			INPVF oINPVF=new INPVF();
			
			JPanel pnlPRNO = new JPanel();
			setMatrix(20,8);
			pnlPRNO.setLayout(null);
			add(new JLabel("From Date"),1,2,1,1,pnlPRNO,'L');   
		    add(txtFMDAT = new TxtDate(),1,3,1,1,pnlPRNO,'L');  
		    add(new JLabel("To Date"),1,4,1,1,pnlPRNO,'L');   
		    add(txtTODAT = new TxtDate(),1,5,1,1,pnlPRNO,'L');  
		    pnlPRNO.setBorder(BorderFactory.createTitledBorder(null,"Employee Detail",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlPRNO,6,2,2,6,this,'L');
		    
			add(rdbMANDL=new JRadioButton("Manpower Detail"),3,2,1,1.5,this,'L');
			add(rdbEMPDL=new JRadioButton("Employee Detail"),3,4,1,1.5,this,'L');
			add(rdbMANSM=new JRadioButton("Manpower Summery"),4,2,1,1.5,this,'L');
			
			btgEMPDL=new ButtonGroup();
			btgEMPDL.add(rdbMANSM); 
			btgEMPDL.add(rdbMANDL);
			btgEMPDL.add(rdbEMPDL); 
			rdbMANSM.setSelected(true);
			
			txtFMDAT.addKeyListener(this);
			txtTODAT.addKeyListener(this);
			txtFMDAT.setInputVerifier(oINPVF);
			txtTODAT.setInputVerifier(oINPVF);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
			txtFMDAT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2));
			
			vtrDPTNM  = new Vector<String>();
			vtrDESGN  = new Vector<String>();
			vtrSMDSG  = new Vector<String>();
			
			hstCNTDS  = new Hashtable<String,String>();
			hstEMPCT  = new Hashtable<String,String>();
			hstDPTTP  = new Hashtable<String,String>();
			
			M_pnlRPFMT.setVisible(true);
			M_rdbHTML.setSelected(true);
			setENBL(true);
						
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
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
					  setMSG ("Please enter Date to generate Report..",'N');	
					}
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
			if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtFMDAT)
				   txtTODAT.requestFocus();
				else if(M_objSOURC == txtTODAT)
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

		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	/**Method to generate Manpower detail report*/
	void genMANDL()
	{
		try
		{
			//if(!vldDATA())
			//	return;
			
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			String strNEW_DPTNM ="";
			String strOLD_DPTNM="";
			String strNEW_DESGN ="";
			String strOLD_DESGN="";
		
			genRPHDR();
			M_strSQLQRY = " select ep_dptcd,c.cmt_codds ep_dptnm,ep_empno,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' ' + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM,isnull(d.cmt_chp01,'xx') cmt_chp01,c.cmt_chp02,ep_empct,isnull(d.cmt_nmp01,999) cmt_nmp01 from co_cdtrn c,hr_epmst left outer join co_cdtrn d on d.cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and d.cmt_cgstp = 'HRXXGRD' and d.cmt_codcd = ep_mmgrd where c.cmt_cgmtp='SYS' and c.cmt_cgstp = 'COXXDPT' and c.cmt_codcd = ep_dptcd  and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"') and  ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'   order by ep_dptcd,d.cmt_nmp01";
			System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strNEW_DPTNM=nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),"");
					if(!strOLD_DPTNM.equals(strNEW_DPTNM))
					{
						if(M_rstRSSET.getString("cmt_chp02").equals("01"))
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_DPTNM")+" "+"[T]",""),39));
						}
						if(M_rstRSSET.getString("cmt_chp02").equals("02"))
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_DPTNM")+"[NT]",""),39));
						}
						
							strOLD_DPTNM=nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),"");
					}
					else 
				    {	
						
					   //cl_dat.M_intLINNO_pbst ++;  
					   D_OUT.writeBytes(padSTRING('R',"",39));
				    }
					
					/*strNEW_DESGN=nvlSTRVL(M_rstRSSET.getString("cmt_chp01"),"");
					if(!strOLD_DESGN.equals(strNEW_DESGN))
					{
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("cmt_chp01"),""),17));
						strOLD_DESGN=nvlSTRVL(M_rstRSSET.getString("cmt_chp01"),"");
					}
					else 
				    {	
					   cl_dat.M_intLINNO_pbst ++;  
					   D_OUT.writeBytes(padSTRING('R',"-",17));
				    }*/
				
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),""),14));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_EMPNM"),""),22));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("cmt_chp01"),""),17));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ep_empct"),""),15));
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
			setMSG(L_EX,"Manpower detail");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**method to generate Manpower detail report*/
	void genMANSM()
	{
		try
		{
			if(!vldDATA())
				return;
			
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			vtrSMDSG.clear();
			vtrDESGN.clear();
			vtrDPTNM.clear();
			hstCNTDS.clear();
			L_intSMDSG=0;
			L_intOFFCR=0;
			L_intTM=0;
			
			
			for(int i=0;i<arrDESGN1.length;i++)
				arrDESGN1[i]=0;
			for(int i=0;i<arrDESGN.length;i++)
				arrDESGN[i]=0;
			for(int i=0;i<arrDESGN2.length;i++)
				arrDESGN2[i]=0;
			genRPHDR();
			crtTBL(D_OUT,true);
			
			//M_strSQLQRY = " select c.cmt_chp02,ep_dptcd,c.cmt_codds ep_dptnm,ifnull(d.cmt_chp01,'xx') cmt_chp01,ifnull(d.cmt_nmp01,999) cmt_nmp01,ep_empct,count(*) CNT from hr_epmst, co_cdtrn c left outer join co_cdtrn d on d.cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' ";
			//M_strSQLQRY += " and d.cmt_cgstp = 'HRXXGRD' and d.cmt_codcd = ep_mmgrd where c.cmt_cgmtp='SYS' and c.cmt_cgstp = 'COXXDPT' and c.cmt_codcd = ep_dptcd  and ep_lftdt is null and ep_stsfl<>'U'";
			//M_strSQLQRY += " and ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' group by c.cmt_chp02,ep_dptcd,c.cmt_codds,d.cmt_chp01,d.cmt_nmp01,ep_empct order by d.cmt_nmp01,cmt_chp02,ep_dptcd,ep_empct";
			
			M_strSQLQRY = " select c.cmt_chp02,ep_dptcd,c.cmt_codds ep_dptnm,isnull(d.cmt_chp01,'xx') cmt_chp01,isnull(d.cmt_nmp01,999) cmt_nmp01,ep_empct,count(*) CNT from co_cdtrn c,hr_epmst left outer join co_cdtrn d on d.cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' ";
			M_strSQLQRY += " and d.cmt_cgstp = 'HRXXGRD' and d.cmt_codcd = ep_mmgrd where c.cmt_cgmtp='SYS' and c.cmt_cgstp = 'COXXDPT' and c.cmt_codcd = ep_dptcd ";
			M_strSQLQRY += " and ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"') group by c.cmt_chp02,ep_dptcd,c.cmt_codds,d.cmt_chp01,d.cmt_nmp01,ep_empct order by d.cmt_nmp01,cmt_chp02,ep_dptcd,ep_empct";
			System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while( M_rstRSSET.next())
				{
					
					if(!vtrDESGN.contains(M_rstRSSET.getString("cmt_chp01")))
						vtrDESGN.add(M_rstRSSET.getString("cmt_chp01"));
					
					if(!vtrDPTNM.contains(M_rstRSSET.getString("ep_dptnm")))
						vtrDPTNM.add(M_rstRSSET.getString("ep_dptnm"));
					
					hstCNTDS.put(M_rstRSSET.getString("ep_dptnm")+"|"+M_rstRSSET.getString("cmt_chp01"),M_rstRSSET.getString("cnt"));	
					hstEMPCT.put(M_rstRSSET.getString("ep_dptnm")+"|"+M_rstRSSET.getString("cmt_chp01"),M_rstRSSET.getString("ep_empct"));		
					hstDPTTP.put(M_rstRSSET.getString("ep_dptnm"),M_rstRSSET.getString("cmt_chp02"));
				}
			}
			
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes("<td width='10%'rowspan=2 align='center'>");
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<small>");
				D_OUT.writeBytes(padSTRING('L',"DEPARTMENT",25));
				D_OUT.writeBytes("</td>");
				
				for(int i=0;i<vtrDESGN.size();i++)
				{
					 D_OUT.writeBytes("<td width='3%'rowspan=2 align='center'>");
					 D_OUT.writeBytes("<font size=2>");
					 D_OUT.writeBytes("<b>");
					 D_OUT.writeBytes(padSTRING('L',vtrDESGN.get(i).toString(),25));
					 D_OUT.writeBytes("</font >");
					 D_OUT.writeBytes("</td>");
				}	
				D_OUT.writeBytes("<td colspan=2 align='center'>");
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<small>");
				D_OUT.writeBytes(padSTRING('L',"Current Total",15));
				D_OUT.writeBytes("</td>");
				D_OUT.writeBytes("</tr>");
				 
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes(padSTRING1('C',"OFF",5));
				D_OUT.writeBytes(padSTRING1('C',"TM",5));
				D_OUT.writeBytes("</tr>");
				D_OUT.writeBytes("</b>");
				
				rowBLANK("Technical ",false);
				//rowBLANK("",true);
				prnDATA("01","Total Tech",arrDESGN1,L_intCNTOF1,L_intCNTTM1);// call prnDATA method to print data of technical Depts.
				rowBLANK("",true);
				rowBLANK("Non-Technical ",false);
				//rowBLANK("",true);
				prnDATA("02","Total Non-Tech",arrDESGN2,L_intCNTOF2,L_intCNTTM2);// call prnDATA method to print data of non-technical Depts.
				rowBLANK("",true);
				
				//display grand total
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes(padSTRING1('L',"Grand Total",15));
			    for(int k=0;k<=intINDEX-1;k++)
				{
					  L_intGRAND=Integer.valueOf(arrDESGN1[k])+Integer.valueOf(arrDESGN2[k]);
					  D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intGRAND),15));
				}
				
				D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intOFFCR),15));
				D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intTM),15));
				D_OUT.writeBytes("</tr>");
			    D_OUT.writeBytes("</table>");
				/**generate Summery Data in html table.*/
				
				M_strSQLQRY = " select cmt_chp02,cmt_nmp02,count(*)cnt from hr_epmst,co_cdtrn  where ep_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXGRD'";
				M_strSQLQRY +=" and cmt_codcd = EP_MMGRD and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"')";
				M_strSQLQRY +=" group by cmt_chp02,cmt_nmp02 order by cmt_nmp02";
				//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				D_OUT.writeBytes("\n"); 
				D_OUT.writeBytes("\n"); 
				  crtTBL(D_OUT,false);
				  
				 D_OUT.writeBytes("<tr>");
				 D_OUT.writeBytes("<td width='6%'>");
				 D_OUT.writeBytes("<b>");
				 D_OUT.writeBytes("<font size=2>");
				 D_OUT.writeBytes(padSTRING('R',"Summery",10));
				 D_OUT.writeBytes("</td>");
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						 D_OUT.writeBytes(padSTRING1('C',M_rstRSSET.getString("cmt_chp02"),5));
						 vtrSMDSG.add(M_rstRSSET.getString("cnt"));
						 
					}
				}
				//System.out.println("vtrSMDSG"+vtrSMDSG);
				 D_OUT.writeBytes(padSTRING1('C',"TOTAL",5));
				 D_OUT.writeBytes("</tr>");
				 
				 D_OUT.writeBytes("<tr>");
				 D_OUT.writeBytes("<td>");
				 D_OUT.writeBytes("<p>&nbsp;</p>");
				 D_OUT.writeBytes("</td>");
				  
				for(int j=0;j<vtrSMDSG.size();j++)
				{
				  D_OUT.writeBytes(padSTRING1('C',vtrSMDSG.get(j),5));
				  L_intSMDSG+=Integer.valueOf( vtrSMDSG.get(j));

				}
				D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intSMDSG),5));
				D_OUT.writeBytes("</tr>");
				D_OUT.writeBytes("</table>");
				/**generate the count of joined,Resigned,Transferred & Retired employees.*/
				
				  D_OUT.writeBytes("<p><TABLE border=0 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width=\"30%\"  align=right>");
				  D_OUT.writeBytes("<b>");
				  M_strSQLQRY = " select count(*) cnt_join from hr_epmst  where ep_jondt between'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"' and ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'  and ep_stsfl<>'U'";
				  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				  D_OUT.writeBytes(padSTRING('R',"Employees Joined      :",24));
				  if(M_rstRSSET != null && M_rstRSSET.next())
				  {
					 D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("cnt_join"),10));
					 crtNWLIN();
				  }
					
				  M_strSQLQRY = " select count(*) cnt_resin from hr_epmst  where ep_lftdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"' and ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'  and ep_rsncd='01'";
				  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				  D_OUT.writeBytes(padSTRING('R',"Employees Resigned    :",24));
				  if(M_rstRSSET != null && M_rstRSSET.next())
				  {
					 D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("cnt_resin"),10));
					 crtNWLIN();
				  }
					
				  M_strSQLQRY = " select count(*) cnt_trans from hr_epmst  where ep_trfdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"' and ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and ep_stsfl<>'U'";
				  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				  D_OUT.writeBytes(padSTRING('R',"Employees Transferred :",24));
				  if(M_rstRSSET != null && M_rstRSSET.next())
				  {
					 D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("cnt_trans"),10));
					 crtNWLIN();
				  }
					
				  M_strSQLQRY = " select count(*) cnt_retir from hr_epmst  where ep_lftdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"' and ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and ep_rsncd='02'";
				  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				  D_OUT.writeBytes(padSTRING('R',"Employees Retired     :",24));
				  if(M_rstRSSET != null && M_rstRSSET.next())
				  {
					 D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("cnt_retir"),10));
					 crtNWLIN();
				  }
					D_OUT.writeBytes("</b>");
					D_OUT.writeBytes("</table>");
					
					D_OUT.writeBytes("\n\n\n\n"); 
					D_OUT.writeBytes("<p><TABLE border=0 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width=\"30%\"  align=left>");
					D_OUT.writeBytes("<b>"+padSTRING('R',"* Designations As On "+cl_dat.M_strLOGDT_pbst+"</b>",50));	
					D_OUT.writeBytes("</table>");
					
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
	
	//Method to display Technical & non-technical wise records & total.
	public void prnDATA(String LP_STRCD,String LP_DPTTP,int[] arrDESGN,int L_intCNTOF,int L_intCNTTM)
	{
		try
		{
			D_OUT.writeBytes("<tr>");
			
			for(int i=0;i<vtrDPTNM.size();i++)
			{
				 int L_intTOTOF=0,L_intTOTTM=0;
			  
			  if(hstDPTTP.containsKey(vtrDPTNM.get(i)))
				{
					L_strDPTTP=hstDPTTP.get(vtrDPTNM.get(i));
					
					if(L_strDPTTP.equals(LP_STRCD))
					{	
						intINDEX=0;
					  D_OUT.writeBytes("<td>");
					  D_OUT.writeBytes("<font size=2>");
					  D_OUT.writeBytes(padSTRING('L',vtrDPTNM.get(i).toString(),30));
					  D_OUT.writeBytes("</td>");
				
						for(int j=0;j<vtrDESGN.size();j++)
						{
							if(hstCNTDS.containsKey(vtrDPTNM.get(i)+"|"+vtrDESGN.get(j)))
							{
								L_strCNTDS=hstCNTDS.get(vtrDPTNM.get(i)+"|"+vtrDESGN.get(j));
								L_intCNTDS = Integer.valueOf(L_strCNTDS).intValue();
								intTOTDS+=L_intCNTDS;
								arrDESGN[intINDEX] += L_intCNTDS;
								D_OUT.writeBytes("<td width='3%'align='center'>");
								D_OUT.writeBytes("<small>");
								D_OUT.writeBytes(padSTRING('L',String.valueOf(L_intCNTDS),25));
								D_OUT.writeBytes("</td>");
							
								if(hstEMPCT.containsKey(vtrDPTNM.get(i)+"|"+vtrDESGN.get(j)))
								{
								    L_strEMPCT=hstEMPCT.get(vtrDPTNM.get(i)+"|"+vtrDESGN.get(j));
								    
										if(L_strEMPCT.equals("OFF"))
										{
											L_intTOTOF+= Integer.valueOf(L_strCNTDS);
												L_intCNTOF+= Integer.valueOf(L_strCNTDS);
										}
										else
										{
											L_intTOTTM+= Integer.valueOf(L_strCNTDS);
												L_intCNTTM+= Integer.valueOf(L_strCNTDS);
										}
								}
							}
							else
							{
								D_OUT.writeBytes("<td width='3%'align='center'>");
								D_OUT.writeBytes(padSTRING('L',"-",25));
								D_OUT.writeBytes("</td>");
							}					
							intINDEX++;
							
						}
					
						D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intTOTOF),15));
						D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intTOTTM),15));
						 D_OUT.writeBytes("</tr>");
					}
				}
			}
			D_OUT.writeBytes(padSTRING1('L',LP_DPTTP,15));
			
			for(int n=0;n<=intINDEX-1;n++)
			{	
				//display technical & non-technical total.
				  D_OUT.writeBytes("<td width='3%'align='center'>");
				  D_OUT.writeBytes("<font size=2>");
				  D_OUT.writeBytes("<b>");
				  D_OUT.writeBytes(padSTRING('C',String.valueOf(arrDESGN[n]),5));
				  D_OUT.writeBytes("</font>");
				  D_OUT.writeBytes("</td>");	  
			}
				D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intCNTOF),15));
				D_OUT.writeBytes(padSTRING1('C',String.valueOf(L_intCNTTM),15));
				D_OUT.writeBytes("</tr>");
				
				L_intOFFCR+=L_intCNTOF; // officers Grand total.
				L_intTM+=L_intCNTTM;   //  TM Grand Total.
				
		}
		catch(Exception E)
		{
			System.out.println("in prnDATA() : "+E);
		}
	}
	
	/**Method to creat HTML Table*/
	private void crtTBL(DataOutputStream L_DOUT,boolean P_flgBORDR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(P_flgBORDR)
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray  cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
			else
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray cellPadding=0 cellSpacing=0 width=\"60%\"  align=left>");
			flgTBLDT=true;
		}
	}
	
	
	/**Method to creat html column format.
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
			strTXCLR="<font size=2>";
			
				
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
					P_strTRNVL="<td width='3%'><b>"+strTXCLR+P_strTRNVL+"</b></td>";
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"padSTRING");
		}
		return P_strTRNVL;
	}
	
	
	/**
	 * Method to heading & blank row  in html table.
	 * */
	protected  void rowBLANK(String P_strSTRVL,boolean P_flgROWBL)
	{
		String P_strTRNVL = "";
		String strTXCLR= " ";
		try
		{
			D_OUT.writeBytes("<tr>");
			if(P_flgROWBL)
			{
				 D_OUT.writeBytes("<td>");
			  D_OUT.writeBytes("<p>&nbsp;</p>");
			  D_OUT.writeBytes("</td>");
			}	
			else
				 D_OUT.writeBytes(padSTRING1('R',P_strSTRVL,20));
				
			for(int i=0;i<vtrDESGN.size();i++)
			{
				 D_OUT.writeBytes("<td>");
				  D_OUT.writeBytes("<p>&nbsp;</p>");
				  D_OUT.writeBytes("</td>");
			}	
			 
			  D_OUT.writeBytes("<td>");
			  D_OUT.writeBytes("<p>&nbsp;</p>");
			  D_OUT.writeBytes("</td>");
			  D_OUT.writeBytes("<td>");
			  D_OUT.writeBytes("<p>&nbsp;</p>");
			  D_OUT.writeBytes("</td>");
			  D_OUT.writeBytes("</tr>");
		}catch(Exception L_EX){
			setMSG(L_EX," rowBLANK");
		}
		
	}
	
	/**Method to generate Employee detail report*/
	void genEMPDL()
	{
		if(!vldDATA())
			return;
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			
			String strEPREM ="";
			String strHRREM ="";
			String L_strHRREM_SUB="";
			String L_strEPREM_SUB="";
			
			genRPHDR();
			M_strSQLQRY = "select rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '   + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM,EP_DPTNM,EP_JONDT,EP_LFTDT,EP_MMGRD,A.rm_remds rm_eprem,B.rm_remds rm_hrrem from hr_epmst left outer join hr_rmmst A on  A.rm_docno=ep_empno and A.rm_cmpcd=ep_cmpcd  and A.rm_trntp='LE' and A.rm_trncd='ER'  left outer join hr_rmmst B on B.rm_docno=ep_empno and B.rm_cmpcd=ep_cmpcd  and B.rm_trntp='LE' and B.rm_trncd='HR' where ";
			M_strSQLQRY += " ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
			if(txtFMDAT.getText().trim().length()>0 && txtTODAT.getText().trim().length()>0)
			{
			  M_strSQLQRY +=" AND ((CONVERT(varchar,ep_jondt,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"')";
			  M_strSQLQRY +=" OR (CONVERT(varchar,ep_lftdt,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'))";
			}
			M_strSQLQRY +=" order by ep_fulnm";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					int L_intEPREM_STR=0,L_intEPREM_END=0;
					strEPREM = nvlSTRVL(M_rstRSSET.getString("rm_hrrem"),"-");
					L_strEPREM_SUB = nvlSTRVL(M_rstRSSET.getString("rm_hrrem"),"-");
					int L_intHRREM_STR=0,L_intHRREM_END=0;
					strHRREM = nvlSTRVL(M_rstRSSET.getString("rm_eprem"),"-");
					L_strHRREM_SUB = nvlSTRVL(M_rstRSSET.getString("rm_eprem"),"-");
																								 
					int intYRS1 = Integer.parseInt(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_JONDT")).substring(6,10));
					int intMTH1 = Integer.parseInt(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_JONDT")).substring(3,5));
					int intDAY1 = Integer.parseInt(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_JONDT")).substring(0,2));
			       
					int intYRS2 = 0;
					int intMTH2 = 0;
					int intDAY2 = 0;
					int intCOT = 0;
				
					if(!(M_rstRSSET.getDate("EP_LFTDT")==null))
					{	
					  intYRS2 = Integer.parseInt(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_LFTDT")).substring(6,10));
					  intMTH2 = Integer.parseInt(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_LFTDT")).substring(3,5));
					  intDAY2 = Integer.parseInt(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_LFTDT")).substring(0,2));
					}
					
					 Calendar cal1 = Calendar.getInstance();
				     Calendar cal2 = Calendar.getInstance();
					 cal1.set(intYRS1,intMTH1,intDAY1);
				     cal2.set(intYRS2,intMTH2,intDAY2);
				     long milis1 = cal1.getTimeInMillis();
				     long milis2 = cal2.getTimeInMillis(); 
				     // Calculate difference in milliseconds
				     long diff = milis2 - milis1;
				       
				        // Calculate difference in seconds
				        long diffSeconds = diff / 1000;
				       
				        // Calculate difference in minutes
				        long diffMinutes = diff / (60 * 1000);
				       
				        // Calculate difference in hours
				        long diffHours = diff / (60 * 60 * 1000);
				       
				        // Calculate difference in days
				        long diffDays = diff / (24 * 60 * 60 * 1000);
				       
				        long num_years = diff/31536000000L;
				        long num_months = (diff % 31536000000L)/2628000000L;
				        long num_days = ((diff % 31536000000L) % 2628000000L)/86400000L;
				       				       
				       //System.out.println("Number of years: " + Math.floor(num_years) + "<br>");
				       //System.out.println("Number of months: " + Math.floor(num_months) + "<br>");
				       //System.out.println("Number of days: " + Math.floor(num_days) + "<br>");
				        
				   if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("<B>");   
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_EMPNM"),"-"),15));
				   if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("</B>");	
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""),14));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_JONDT")),""),13));
					if(!(M_rstRSSET.getDate("EP_LFTDT")==null))
					{	
					D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("EP_LFTDT")),18));
					D_OUT.writeBytes(padSTRING('R',Math.floor(num_years)+":"+Math.floor(num_months)+":"+Math.floor(num_days),22));		
					}
					else
					{	
					   D_OUT.writeBytes(padSTRING('R',"-",18));
					   D_OUT.writeBytes(padSTRING('R',"-",22));
					}
					D_OUT.writeBytes(padSTRING('R',"",8));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_MMGRD"),"-"),15));
					D_OUT.writeBytes(padSTRING('R',"",15));
								
					crtNWLIN();
					  L_intEPREM_END=strEPREM.length()>90?90:strEPREM.length();
					if(!(M_rstRSSET.getString("rm_hrrem") == null))
					{	
					  D_OUT.writeBytes(padSTRING('R',"Reason for Leaving :"+strEPREM,110));
					  //System.out.println("in "+strEPREM);
					}  
					else
					  D_OUT.writeBytes(padSTRING('R',"Reason for Leaving : ",110));
			
						while(L_intEPREM_END<strEPREM.length())
						{
							if(L_intEPREM_END<strEPREM.length())
							{
								crtNWLIN();	
								D_OUT.writeBytes(padSTRING('R',"",20));
								L_intEPREM_STR=L_intEPREM_END;
								L_intEPREM_END=strEPREM.length()>L_intEPREM_END+90?L_intEPREM_END+90:strEPREM.length();
								L_strEPREM_SUB = padSTRING('R',strEPREM.substring(L_intEPREM_STR,L_intEPREM_END),110);
								//System.out.println("in EP "+L_strEPREM_SUB);
								D_OUT.writeBytes(padSTRING('R',L_strEPREM_SUB,110));
							}
						}
					crtNWLIN();
					L_intHRREM_END=strHRREM.length()>90?90:strHRREM.length();
					if(!(M_rstRSSET.getString("rm_eprem") == null))	
					D_OUT.writeBytes(padSTRING('R',"Comments by Human Resource Department :"+strHRREM,110));
					else
					D_OUT.writeBytes(padSTRING('R',"Comments by Human Resource Department : ",110));
					while(L_intHRREM_END<strHRREM.length())
					{								
					if(L_intHRREM_END<strHRREM.length())
					{	
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('R',"",30));
						L_intHRREM_STR=L_intHRREM_END;
						L_intHRREM_END=strHRREM.length()>L_intHRREM_END+90?L_intHRREM_END+90:strHRREM.length();
						L_strHRREM_SUB = padSTRING('R',strHRREM.substring(L_intEPREM_STR,L_intHRREM_END),110);
						D_OUT.writeBytes(padSTRING('R',L_strHRREM_SUB,110));						
					}  
					}
					crtNWLIN();
					D_OUT.writeBytes(padSTRING('R',"",130));
					crtNWLIN();
				}
			 }
			 genRPFTR();
			 D_OUT.writeBytes("\n");
			// D_OUT.writeBytes(strDOTLN);
	
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
				   // D_OUT.writeBytes("<P CLASS = \"breakhere\">");
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
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size :9 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	
			cl_dat.M_PAGENO +=1;
			
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,89));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		if(rdbMANSM.isSelected())
  		    {
    			D_OUT.writeBytes("\n"+padSTRING('R',"MANPOWER STATEMENT - ("+cl_dat.M_strCMPLC_pbst+") Period From : "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),89));
        		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
    			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    			crtNWLIN();
    			//D_OUT.writeBytes(padSTRING('R',"   Period : From  "+txtFMDAT.getText()+"  To  "+txtTODAT.getText(),100));
	    		//crtNWLIN();
			    prnFMTCHR(D_OUT,M_strNOBOLD);  
  		    }
    		if(rdbMANDL.isSelected())
  		    {
    			D_OUT.writeBytes("\n"+padSTRING('R',"Summery of MANPOWER STATEMENT - ("+cl_dat.M_strCMPLC_pbst+") as on : "+cl_dat.M_strLOGDT_pbst,89));
        		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
    			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    			crtNWLIN();
    			D_OUT.writeBytes(strDOTLN+"\n");
    			D_OUT.writeBytes("Department Name [Type]                 Employee No.  Employee Name         Designation      Employee Category \n");                     
    			D_OUT.writeBytes(strDOTLN);
    			crtNWLIN();
			    prnFMTCHR(D_OUT,M_strNOBOLD);  
  		    }
    		 if(rdbEMPDL.isSelected())
  		    {
	    		D_OUT.writeBytes("\n"+padSTRING('R',"MANPOWER STATEMENT - ("+cl_dat.M_strCMPLC_pbst+") - EMPLOYEES TRURN AROUND DETAILS",89));
	    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
    			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    			crtNWLIN();
	    		D_OUT.writeBytes(padSTRING('R',"   Period : From  "+txtFMDAT.getText()+"  To  "+txtTODAT.getText(),100));
	    		crtNWLIN();
			 
			    D_OUT.writeBytes(strDOTLN+"\n");
			    D_OUT.writeBytes("Name of        Department    Date of      Date of           Total Years       Remark      Grade of         Replacement \n");                     
			    D_OUT.writeBytes("Employee                     Joining      Resignation       Completed                     Employee         Planned  \n"); 
			    D_OUT.writeBytes("                                                            (YY:MM:DD)                                              \n"); 
			    D_OUT.writeBytes(strDOTLN);  
			    prnFMTCHR(D_OUT,M_strNOBOLD);
	    		crtNWLIN();
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
			strFMDAT = txtFMDAT.getText().trim();
			strTODAT = txtTODAT.getText().trim();
			strCRDAT = cl_dat.M_strLOGDT_pbst;
			if(txtFMDAT.getText().trim().length()== 0)
			{
			  setMSG("Enter From Date..",'E');
			  txtFMDAT.requestFocus();
			  return false;
			}
			else if(txtTODAT.getText().trim().length()== 0)
			{
			  setMSG("Enter To Date..",'E');
			  txtTODAT.requestFocus();
			  return false;
			 
			}
			else if(M_fmtLCDAT.parse(strFMDAT).compareTo(M_fmtLCDAT.parse(strCRDAT))> 0)
			{
				
				setMSG("From Date can not be greater than Current date time..",'E');
				return false;
				
			}
			else if(M_fmtLCDAT.parse(strTODAT).compareTo(M_fmtLCDAT.parse(strCRDAT))> 0)
			{
				setMSG("To Date can not be greater than Current date time..",'E');
				return false;
				
			}
			else if(M_fmtLCDAT.parse(strTODAT).compareTo(M_fmtLCDAT.parse(strFMDAT))< 0)
			{
				setMSG("To Date can not be smaller than From date..",'E');
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
			if(!rdbMANDL.isSelected())
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rpmst.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpmst.doc";
			if(rdbMANSM.isSelected())
				genMANSM();
			if(rdbMANDL.isSelected())
				genMANDL();
			if(rdbEMPDL.isSelected())
				genEMPDL();
			
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

		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
}



