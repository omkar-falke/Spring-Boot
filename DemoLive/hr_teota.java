/*
System Name   : Labortory Information Management System
Program Name  : Lot Master Enter (Batch Wise)
Program Desc. :
Author        : A.M.Jain
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;import java.awt.event.MouseEvent;import java.awt.Dimension;
import java.util.Calendar;import java.text.SimpleDateFormat;
public class hr_teota extends cl_pbase
{
	JTextField txtDPTCD;
	JTextField txtEMPNO;
	JTextField txtSTRDT;
	JTextField txtENDDT;		
	JLabel lblEMPNM,lblDPTNM;
		
	private final int TB_CHKFL=0;
	private final int TB_WRKDT=1;
	private final int TB_EMPCT=2;
	private final int TB_EMPNO=3;
	private final int TB_EMPNM=4;
	private final int TB_SHFCD=5;
	private final int TB_OVTWK=6;
	private final int TB_OVTHR=7;
	private final int TB_AUTHC=8;
	private final int TB_OVTBY=9;
	private final int TB_INCTM=10;
	private final int TB_OUTTM=11;
	private final int TB_VEWAT=12;
	private final int TB_ACTWK=13;
	private final int TB_POTTM=14;
	private final int TB_OOTTM=15;
	private  JRadioButton rdbOTAUT;
	private  JRadioButton rdbCOAUT;
	ButtonGroup btgCOTAU;
	boolean flgALLDPT;
	cl_JTable tblTEOTA,tblTEOTA1;
	
	JTextField txtWRKDT;
	JTextField txtEMPCT;
	JTextField txtEMPNO1;
	JTextField txtEMPNM;
	JTextField txtSHFCD;
	JTextField txtINCTM;
	JTextField txtOUTTM;
	JTextField txtACTWK;
	JTextField txtEXPER;
	JTextField txtOVTWK;
	JTextField txtOVTHR;
	JTextField txtOVTBY;

	JCheckBox chkVEWAT;
	JCheckBox chkAUTHC;

	java.util.Date datWRKDT_2;		// two day Previous
	java.util.Date datWRKDT_1;		// Previous day
	java.util.Date datWRKDT1;		// Current Working day
	java.util.Date datWRKDT2;		// Next Day
	java.util.Date datWRKDT3;		// Next Day Ending date
	java.util.Date datWRKDT;		// Working date to be considered depending on In/Out time
	
	hr_teota()
	{
		super(1);
		setMatrix(20,20);
		try
		{
			add(rdbOTAUT=new JRadioButton("Over Time"),1,1,1,2,this,'L');
			add(rdbCOAUT=new JRadioButton("COMP Off"),1,3,1,2,this,'L');
			btgCOTAU=new ButtonGroup();
			btgCOTAU.add(rdbOTAUT);btgCOTAU.add(rdbCOAUT);
			
			add(new JLabel("Dept "),2,1,1,2,this,'L');     
			add(txtDPTCD=new TxtLimit(3),2,3,1,2,this,'L');
			add(lblDPTNM=new JLabel(),2,5,1,2,this,'L');

			add(new JLabel("Emp.No. "),2,7,1,2,this,'L');     
			add(txtEMPNO=new TxtLimit(4),2,9,1,2,this,'L');
			add(lblEMPNM=new JLabel(),2,11,1,2,this,'L');
			
			add(new JLabel("Start Date "),3,1,1,2,this,'L');     
			add(txtSTRDT=new TxtDate(),3,3,1,2,this,'L');
			
			add(new JLabel("End Date "),3,7,1,2,this,'L');
			add(txtENDDT=new TxtDate(),3,9,1,2,this,'L');
			
			String[] L_strTBLHD1 = {" ","Work Dt","Cat","EpNo","Emp Name","Shft","Extra Work","OT/CO Hrs.","Auth","Auth By","InTm","OutTm","View","WRKD Hrs","Exit(P)","Exit(0)"};
			int[] L_intCOLSZ1 ={10,80,30,50,120,30,70,70,40,40,60,60,40,60,60,60};
			tblTEOTA = crtTBLPNL1(this,L_strTBLHD1,1000,5,1,7,20,L_intCOLSZ1,new int[]{0,TB_VEWAT,TB_AUTHC});						add(new JLabel("Autorized Records : "),12,1,1,3,this,'L');			
			tblTEOTA1 = crtTBLPNL1(this,L_strTBLHD1,1000,13,1,5,20,L_intCOLSZ1,new int[]{0,TB_VEWAT,TB_AUTHC});			/*tblTEOTA.setCellEditor(TB_WRKDT,txtWRKDT=new TxtDate());
			tblTEOTA.setCellEditor(TB_EMPCT,txtEMPCT=new TxtLimit(1));			tblTEOTA.setCellEditor(TB_EMPNO,txtEMPNO1=new TxtNumLimit(4));
			tblTEOTA.setCellEditor(TB_EMPNM,txtEMPNM=new TxtLimit(20));
			tblTEOTA.setCellEditor(TB_SHFCD,txtSHFCD=new TxtLimit(1));
			tblTEOTA.setCellEditor(TB_INCTM,txtINCTM=new TxtTime());
			tblTEOTA.setCellEditor(TB_OUTTM,txtOUTTM=new TxtTime());
			tblTEOTA.setCellEditor(TB_ACTWK,txtACTWK=new TxtTime());
			tblTEOTA.setCellEditor(TB_POTTM,txtPOTTM=new TxtTime());
			tblTEOTA.setCellEditor(TB_OOTTM,txtAOTTM=new TxtTime());
			tblTEOTA.setCellEditor(TB_OVTWK,txtOVTWK=new TxtTime());*/
			tblTEOTA.setCellEditor(TB_AUTHC,chkAUTHC=new JCheckBox());
			tblTEOTA.setCellEditor(TB_VEWAT,chkVEWAT=new JCheckBox());
			tblTEOTA.setCellEditor(TB_OVTHR,txtOVTHR=new TxtTime());
			tblTEOTA.setCellEditor(TB_OVTBY,txtOVTBY=new TxtLimit(3));
			
			((JTextField)tblTEOTA.cmpEDITR[TB_WRKDT]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_EMPCT]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_EMPNO]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_EMPNM]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_SHFCD]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_INCTM]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_OUTTM]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_ACTWK]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_POTTM]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_OOTTM]).setEditable(false);
			((JTextField)tblTEOTA.cmpEDITR[TB_OVTWK]).setEditable(false);
			
			((JCheckBox) tblTEOTA.cmpEDITR[TB_VEWAT]).addMouseListener(this);
			((JCheckBox) tblTEOTA.cmpEDITR[TB_AUTHC]).addMouseListener(this);
			INPVF oINPVF=new INPVF();
			txtDPTCD.setInputVerifier(oINPVF);
			txtEMPNO.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
   void setENBL(boolean P_flgENBFL)
   {
		super.setENBL(P_flgENBFL);
   }

	public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed(L_KE);
	    if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{
				if(M_objSOURC==txtDPTCD)
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
					
					M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT'";        			
					
					//M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and cmt_codcd in ";
					//M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=US_EMPCD)";        			
					
        		    //M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
					//M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "+ (flgALLDPT ? "" : " and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select substr(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and substr(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"') and substr(EP_HRSBS,1,2)='"+M_strSBSCD.substring(0,2)+"')");					
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				if(M_objSOURC==txtEMPNO)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
        			String L_ARRHDR[] = {"Employee No","Name"};
        			//M_strSQLQRY = "select EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_DPTCD = '"+txtDPTCD.getText()+"' and EP_EMPNO in (select substr(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and substr(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"') and ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
					M_strSQLQRY = "select EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'" +(txtDPTCD.getText().length()>0 ? "AND EP_DPTCD = '"+txtDPTCD.getText()+"' ":"") +" and ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
					//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
			}
			catch(NullPointerException L_NPE)
			{
			    setMSG("Help not available",'N');                            
			}
		  }
		if(L_KE.getKeyCode() == L_KE.VK_ENTER )
        {
			if(M_objSOURC==txtDPTCD)
				txtEMPNO.requestFocus();
			if(M_objSOURC==txtEMPNO)
				txtSTRDT.requestFocus();
			else if(M_objSOURC==txtSTRDT)
				txtENDDT.requestFocus();
			else if(M_objSOURC==txtENDDT)
				tblTEOTA.requestFocus();
		}
	}
	
	public void exeHLPOK()
	{
		super.exeHLPOK();
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
			lblEMPNM.setText(L_STRTKN.nextToken());
		}
		cl_dat.M_flgHELPFL_pbst = false;
    }

	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			if (M_objSOURC==chkVEWAT)
			{					tblTEOTA.setValueAt(new Boolean(false),tblTEOTA.getSelectedRow(),TB_VEWAT); 				
				if(tblTEOTA.getValueAt(tblTEOTA.getSelectedRow(),TB_EMPNO).toString().length()==4)
					dspVEWAT(tblTEOTA.getSelectedRow());
			}
			if (M_objSOURC==chkAUTHC)
			{					if(((JCheckBox) tblTEOTA.cmpEDITR[TB_AUTHC]).isSelected())
				{						if(!vldTEOTA(tblTEOTA.getSelectedRow()))
					{
						tblTEOTA.setValueAt(new Boolean(false),tblTEOTA.getSelectedRow(),TB_AUTHC);
						return;
					}
					tblTEOTA.setValueAt(new Boolean(true),tblTEOTA.getSelectedRow(),TB_CHKFL); 
					tblTEOTA.setValueAt(cl_dat.M_strUSRCD_pbst,tblTEOTA.getSelectedRow(),TB_OVTBY); 
				}
				else if(!((JCheckBox) tblTEOTA.cmpEDITR[TB_AUTHC]).isSelected())
				{
					tblTEOTA.setValueAt(new Boolean(false),tblTEOTA.getSelectedRow(),TB_CHKFL); 
					tblTEOTA.setValueAt("",tblTEOTA.getSelectedRow(),TB_OVTBY); 
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}	
	
	private boolean vldTEOTA(int P_intROWNO)
	{
		try
		{
		   setMSG("",'N');
		   if(tblTEOTA.getValueAt(P_intROWNO,TB_OVTHR).toString().length()<5)
		   {
			   setMSG("Please Enter OT/CO Hrs. in the Table for "+tblTEOTA.getValueAt(P_intROWNO,TB_EMPNM).toString(),'E');
			   return false;   
		   }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTEEXR()");
		}
		return true;			
	}	
	
	/**
	 * Displays Attendance Punching details for verification
	 *
	 */
	private void dspVEWAT(int LP_ROWID)
	{
		try
		{
			SimpleDateFormat fmtLCDTM_L=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	/**	For Date in Locale format "dd/MM/yyyy" */
			int TB1_CHKFL = 0;
			int TB1_WRKDT = 1;
			int TB1_SRLNO = 2;
			int TB1_INCTM = 3;
			int TB1_INCDL = 4;
			int TB1_OUTTM = 5;
			int TB1_OUTDL = 6;
				
			JLabel lblEMPNM;
			cl_JTable tblVEWAT; 
			JPanel pnlVEWAT;
			
			pnlVEWAT=new JPanel(null);
			String[] L_staCOLHD = {"Chk","Date","S.No.","In Time","X","Out Time","X"};
			int[] L_inaCOLSZ = {20,80,30,100,20,100,20};
			add(lblEMPNM = new JLabel(""),1,1,1,4,pnlVEWAT,'L');
			tblVEWAT = crtTBLPNL1(pnlVEWAT,L_staCOLHD,50,2,1,7,10,L_inaCOLSZ,new int[]{0,4,6});
			
			//**setWRKDTX(txtWRKDT.getText().toString().trim());
			setWRKDTX(tblTEOTA.getValueAt(tblTEOTA.getSelectedRow(),TB_WRKDT).toString());
			String L_strWHRSTR_AL1 = " SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and  swt_wrkdt between '"+M_fmtDBDAT.format(datWRKDT_2)+"' and '"+M_fmtDBDAT.format(datWRKDT3)+"' and swt_stsfl not in ('X') and SWT_EMPNO = '"+tblTEOTA.getValueAt(LP_ROWID,TB_EMPNO).toString()+"'";		
			M_strSQLQRY = "select swt_wrkdt,swt_srlno,swt_inctm,swt_outtm,ifNull(swt_incfl,'') swt_incfl,ifNull(swt_outfl,'') swt_outfl from hr_swtrn where "+L_strWHRSTR_AL1+" order by swt_wrkdt,swt_srlno,swt_inctm,swt_outtm";
			

			//System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			java.util.Date L_datWRKDT = null,L_datWRKDT_OLD = null;
			java.sql.Timestamp L_tmsINCTM = null,L_tmsOUTTM = null;
			java.sql.Time L_timWRKTM = null;
			
			tblVEWAT.clrTABLE();
			inlTBLEDIT(tblTEOTA);
			inlTBLEDIT(tblVEWAT);
			int i =0;
			boolean L_flgIN = false;
			String L_strSRLNO = "1";
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				while (true)
				{
					tblVEWAT.setValueAt(M_fmtLCDAT.format(L_rstRSSET.getDate("SWT_WRKDT")),i,TB1_WRKDT);
					tblVEWAT.setValueAt(L_rstRSSET.getString("SWT_SRLNO"),i,TB1_SRLNO);
					if(L_rstRSSET.getTimestamp("SWT_INCTM")!=null)
					{
						tblVEWAT.setValueAt(fmtLCDTM_L.format(L_rstRSSET.getTimestamp("SWT_INCTM")) ,i,TB1_INCTM);
						if(L_rstRSSET.getString("SWT_INCFL").equals("X"))
							tblVEWAT.setValueAt(new Boolean(true),i,TB1_INCDL);
					}
					if(L_rstRSSET.getTimestamp("SWT_OUTTM")!=null)
					{
						tblVEWAT.setValueAt(fmtLCDTM_L.format(L_rstRSSET.getTimestamp("SWT_OUTTM")) ,i,TB1_OUTTM);
						if(L_rstRSSET.getString("SWT_OUTFL").equals("X"))
							tblVEWAT.setValueAt(new Boolean(true),i,TB1_OUTDL);
					}
					if(!L_rstRSSET.next())
						break;
					i++;
				}
			}

			lblEMPNM.setText(tblTEOTA.getValueAt(LP_ROWID,TB_EMPNO).toString()+"  "+tblTEOTA.getValueAt(LP_ROWID,TB_EMPNM).toString());
			pnlVEWAT.setSize(200,100);
			pnlVEWAT.setPreferredSize(new Dimension(600,200));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVEWAT,"Punching details",JOptionPane.OK_CANCEL_OPTION);
			/*if(L_intOPTN == 0)
			{
				String strWHRSTR="";
				for(i=0;i<tblVEWAT.getRowCount();i++)
				{
					if(tblVEWAT.getValueAt(i,TB1_INCTM).toString().length()>0)
					{
						strWHRSTR =" SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SWT_EMPNO='"+tblTEOTA.getValueAt(LP_ROWID,TB_EMPNO).toString()+"'";
						strWHRSTR+=" and SWT_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblVEWAT.getValueAt(i,TB1_WRKDT).toString()))+"'";											
						M_strSQLQRY = " Update HR_SWTRN set";
						if(tblVEWAT.getValueAt(i,TB1_INCDL).toString().equals("true"))
    						M_strSQLQRY +=" SWT_INCFL='X'";
						else
							M_strSQLQRY +=" SWT_INCFL=''";
						M_strSQLQRY +=" where "+strWHRSTR;
						M_strSQLQRY +=" and SWT_INCTM='"+M_fmtDBDTM.format(fmtLCDTM_L.parse(tblVEWAT.getValueAt(i,TB1_INCTM).toString()))+"'";
						//System.out.println("M_strSQLQRY1>>"+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					if(tblVEWAT.getValueAt(i,TB1_OUTTM).toString().length()>0)
					{
						strWHRSTR =" SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SWT_EMPNO='"+tblTEOTA.getValueAt(LP_ROWID,TB_EMPNO).toString()+"'";
						strWHRSTR+=" and SWT_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblVEWAT.getValueAt(i,TB1_WRKDT).toString()))+"'";					
						M_strSQLQRY = " Update HR_SWTRN set";
    					if(tblVEWAT.getValueAt(i,TB1_OUTDL).toString().equals("true"))
							M_strSQLQRY +=" SWT_OUTFL='X'";
						else
							M_strSQLQRY +=" SWT_OUTFL=''";
						M_strSQLQRY +=" where "+strWHRSTR;
						M_strSQLQRY +=" and SWT_OUTTM='"+M_fmtDBDTM.format(fmtLCDTM_L.parse(tblVEWAT.getValueAt(i,TB1_OUTTM).toString()))+"'";
						//System.out.println("M_strSQLQRY2>>"+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				if(cl_dat.exeDBCMT("exeSAVE"))
					setMSG("",'N');
				else
				    setMSG("Error in updating data..",'E');
			}*/
		}

		catch (Exception L_EX)
		{
			setMSG("Error in dspVEWAT : "+L_EX,'E');
		}
	}	

	void setWRKDTX(String LP_WRKDT)
	{
		try
		{
			datWRKDT1 = M_fmtLCDAT.parse(LP_WRKDT);
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,-2);    
			datWRKDT_2 = M_calLOCAL.getTime();

			datWRKDT1 = M_fmtLCDAT.parse(LP_WRKDT);
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,-1);    
			datWRKDT_1 = M_calLOCAL.getTime();
				
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,+1);    
			datWRKDT2 = M_calLOCAL.getTime();
				
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,+2);
			datWRKDT3 = M_calLOCAL.getTime();
		}
		catch(Exception L_EX){
			setMSG(L_EX,"setWRKDTX");
		}
	}
	
	void getDATA()
	{
	    try
	    {
			if(!(rdbOTAUT.isSelected() || rdbCOAUT.isSelected()))
			{
				setMSG("Please select option for COMP OFF or OT",'E');
				return;
			}
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Data,Please wait....",'N');
			tblTEOTA.clrTABLE();
			inlTBLEDIT(tblTEOTA);

			int L_intCNT=0,L_intCNT1=0;
			M_strSQLQRY = " select SW_WRKDT,SW_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM,ifNull(EP_EMPCT,'') EP_EMPCT,ifNull(SW_WRKSH,'') SW_WRKSH,SW_OVTWK,SW_INCTM,SW_OUTTM,SW_ACTWK,SW_POTTM,SW_OOTTM,ifNull(SW_OVTFL,'') SW_OVTFL,SW_OVTHR,ifNull(SW_OVTBY,'') SW_OVTBY";
			M_strSQLQRY +=" from hr_swmst,hr_epmst";
			M_strSQLQRY +=" where ep_empno=sw_empno and ep_cmpcd=sw_cmpcd and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
			if(txtDPTCD.getText().length()>0)
				M_strSQLQRY +=" and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
			if(txtEMPNO.getText().length()>0)
				M_strSQLQRY +=" and ep_empno='"+txtEMPNO.getText().trim()+"'";
			if(rdbOTAUT.isSelected())
				M_strSQLQRY +=" and sw_ovtwk>'00:00:30' and ep_empct in ('T','W')";
			else if(rdbCOAUT.isSelected())
				M_strSQLQRY +=" and sw_ovtwk>'05:59:00' and ((ep_empct='O') OR (ep_empct in ('T','W') AND sw_lvecd='WO'))";
			//if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			//	M_strSQLQRY +=" and ifNull(sw_ovtfl,'') = ''";
			M_strSQLQRY +=" order by sw_wrkdt,sw_empno";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("SW_OVTFL").equals("C") || M_rstRSSET.getString("SW_OVTFL").equals("O"))
					{
						setTBLDT(tblTEOTA1,M_rstRSSET,L_intCNT1);
						tblTEOTA1.setValueAt(new Boolean(true),L_intCNT1,TB_AUTHC);
						tblTEOTA1.setValueAt(M_rstRSSET.getTime("SW_OVTHR").toString().substring(0,5),L_intCNT1,TB_OVTHR);
						tblTEOTA1.setValueAt(M_rstRSSET.getString("SW_OVTBY").toString(),L_intCNT1,TB_OVTBY);
						L_intCNT1++;
					}
					else
					{
						setTBLDT(tblTEOTA,M_rstRSSET,L_intCNT);
						L_intCNT++;						
					}
				}
			}
			else
				setMSG("No Data Found..",'E');
			setMSG("Fetching Data Completed....",'N');
   	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getDATA");
	    }
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	

	private void setTBLDT(cl_JTable LP_TBL,ResultSet LP_RSSET,int LP_CNT)
	{
		try
		{
			LP_TBL.setValueAt(M_fmtLCDAT.format(LP_RSSET.getDate("SW_WRKDT")),LP_CNT,TB_WRKDT);
			LP_TBL.setValueAt(LP_RSSET.getString("EP_EMPCT"),LP_CNT,TB_EMPCT);
			LP_TBL.setValueAt(LP_RSSET.getString("SW_EMPNO"),LP_CNT,TB_EMPNO);
			LP_TBL.setValueAt(LP_RSSET.getString("EP_EMPNM"),LP_CNT,TB_EMPNM);
			LP_TBL.setValueAt(LP_RSSET.getString("SW_WRKSH"),LP_CNT,TB_SHFCD);
			if(LP_RSSET.getString("SW_OVTWK")!=null)
			{
				LP_TBL.setValueAt(LP_RSSET.getTime("SW_OVTWK").toString().substring(0,5),LP_CNT,TB_OVTWK);
				LP_TBL.setValueAt(exeRNDOF(LP_RSSET.getTime("SW_OVTWK").toString().substring(0,5)),LP_CNT,TB_OVTHR);
			}
			if(LP_RSSET.getString("SW_INCTM")!=null)
				LP_TBL.setValueAt(LP_RSSET.getTime("SW_INCTM").toString().substring(0,5),LP_CNT,TB_INCTM);
			if(LP_RSSET.getString("SW_OUTTM")!=null)
				LP_TBL.setValueAt(LP_RSSET.getTime("SW_OUTTM").toString().substring(0,5),LP_CNT,TB_OUTTM);
			if(LP_RSSET.getString("SW_ACTWK")!=null)
				LP_TBL.setValueAt(LP_RSSET.getTime("SW_ACTWK").toString().substring(0,5),LP_CNT,TB_ACTWK);
			if(LP_RSSET.getString("SW_POTTM")!=null)
				LP_TBL.setValueAt(LP_RSSET.getTime("SW_POTTM").toString(),LP_CNT,TB_POTTM);
			if(LP_RSSET.getString("SW_OOTTM")!=null)
				LP_TBL.setValueAt(LP_RSSET.getTime("SW_OOTTM").toString(),LP_CNT,TB_OOTTM);
		}
		catch(Exception E)
		{	
			setMSG(E,"exeRNDOF");
		}
	}

	/** round off the time in multiples of 15 e.g 13:27 ==> 13:25
	 */
	private String exeRNDOF(String LP_STRTM)
	{
		String strMIN="";
		try
		{
			int intMIN = Integer.parseInt(LP_STRTM.substring(3,5));
			int intEXTRA = intMIN % 15;
			intMIN = intMIN - intEXTRA;
			if(String.valueOf(intMIN).length()==1)
				strMIN = LP_STRTM.substring(0,3)+"0"+String.valueOf(intMIN);
			else
				strMIN = LP_STRTM.substring(0,3)+String.valueOf(intMIN);
		}
		catch(Exception E)
		{	
			setMSG(E,"exeRNDOF");
		}
		return strMIN;
	}

	/** Initializing table editing before poulating/capturing data
	 */
	private void inlTBLEDIT(JTable P_tblTBLNM)
	{
		if(!P_tblTBLNM.isEditing())
			return;
		P_tblTBLNM.getCellEditor().stopCellEditing();
		P_tblTBLNM.setRowSelectionInterval(0,0);
		P_tblTBLNM.setColumnSelectionInterval(0,0);
	}
	
	void exeSAVE()
	{
	    try
	    {
			for(int P_intROWNO=0;P_intROWNO<tblTEOTA.getRowCount();P_intROWNO++)
			{
				if(tblTEOTA.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
				{
			        if(!vldTEOTA(P_intROWNO))
		            return;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				for(int P_intROWNO=0;P_intROWNO<tblTEOTA.getRowCount();P_intROWNO++)
				{
					if(tblTEOTA.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY = " update HR_SWMST set ";
						M_strSQLQRY+= " SW_OVTHR = '"+tblTEOTA.getValueAt(P_intROWNO,TB_OVTHR).toString()+"',";	
						if(rdbCOAUT.isSelected())
							M_strSQLQRY+= " SW_OVTFL = 'C',";
						if(rdbOTAUT.isSelected())
							M_strSQLQRY+= " SW_OVTFL = 'O',";
						M_strSQLQRY+= " SW_OVTBY = '"+tblTEOTA.getValueAt(P_intROWNO,TB_OVTBY).toString().toUpperCase()+"'";
						M_strSQLQRY+= " where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO = '"+tblTEOTA.getValueAt(P_intROWNO,TB_EMPNO)+"' and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEOTA.getValueAt(P_intROWNO,TB_WRKDT).toString()))+"' ";
						cl_dat.M_flgLCUPD_pbst = true;
						System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				tblTEOTA.clrTABLE();			
				//clrCOMP();
				setMSG("record updated successfully",'N');
			}
			else
			{
			   	JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
			    setMSG("Error in updating data..",'E');
			}
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"exeSAVE");
	    }
	}
	public void actionPerformed(ActionEvent L_AE)
    {
        super.actionPerformed(L_AE);
		if(txtEMPNO.getText().length()==0)
			lblEMPNM.setText("");	
		if(txtDPTCD.getText().length()==0)
			lblDPTNM.setText("");	
		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{	
			rdbOTAUT.setEnabled(true);
			rdbCOAUT.setEnabled(true);
			txtEMPNO.setEnabled(true);
			txtDPTCD.setEnabled(true);
			txtSTRDT.setEnabled(true);
			txtENDDT.setEnabled(true);
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
				
				if(input == txtDPTCD)
				{
					M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and CMT_CODCD= '"+txtDPTCD.getText().toString().trim()+"'";
					
					//M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and CMT_CODCD= '"+txtDPTCD.getText().toString().trim()+"' and cmt_codcd in ";
					//M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=US_EMPCD)";

					//M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
					//M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' and cmt_codcd = '"+txtDPTCD.getText().trim()+"' "+(flgALLDPT ? "" : " and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select substr(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and substr(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"') and EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"')");					
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblDPTNM.setText(M_rstRSSET.getString("CMT_SHRDS"));
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
				}	
				if(input == txtEMPNO)
				{
					M_strSQLQRY = "select EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_EMPNO='"+txtEMPNO.getText()+"' AND EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'" +(txtDPTCD.getText().length()>0 ? "AND EP_DPTCD = '"+txtDPTCD.getText()+"' ":"") +" and ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Employee No.",'E');
						return false;
					}
				}	
				if(input == txtSTRDT)
				{
					if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("From date can not be greater than Today's date..",'E');
						txtSTRDT.requestFocus();
						return false;
					}
					txtENDDT.setText(txtSTRDT.getText());
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
					getDATA();
				}
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"Input Verifier");		
			}
			return true;
		}
	}		

}
