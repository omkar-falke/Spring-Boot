/*
System Name    : Finished Goods Inventory Management System
Program Name   : Gradewise Residence Details
Program Desc.  : 1)Gives detail regarding Gradewise Aging Statement.
Author         : Mr. Deepal N. Mehta
Date           : 4th July 2002
Version        : FIMS 1.0
Modificaitons  : 
Modified By    : Zaheer Khan
Modified Date  : 31-10-2006
Modified det.  : 
Version        : 

*/

import java.sql.ResultSet;import java.util.Date;import java.util.Calendar;
import java.awt.event.KeyEvent;import java.awt.Color;import java.awt.Component;
import javax.swing.JComponent;import javax.swing.JComboBox; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JCheckBox;
import java.util.Hashtable;import java.util.StringTokenizer;import javax.swing.JPanel;
import javax.swing.JButton;import javax.swing.JTable;import javax.swing.table.*;

public class fg_qrgra extends cl_rbase
{
	JTextField txtGRDFR,txtGRDTO,txtQRDAT,txtNOFDS;
	JCheckBox chkLOTWS;	
	String strFILNM = cl_dat.M_strREPSTR_pbst;
	String strFILNM1 = ""; 
	private JPanel pnlDETAL;
										 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	
	cl_JTable tblDETAL;
	TableColumn TCL_PRDDS,TCL_les3M,TCL_3to6M,TCL_6to9M,TCL_9to12,TCL_grt12,TCL_TOTQT;
	Hashtable<String,String> LM_HSTST;
	private JButton btnRUN;  
	
	final int TB_PRDDS = 0;
	final int TB_les3M = 1;
    final int TB_3to6M = 2;
	final int TB_6to9M = 3;
	final int TB_9to12 = 4;
	final int TB_grt12 = 5;
	final int TB_TOTQT = 6;
	
	int LM_LMRGN=0;
	String strGRDFR = ""; String strGRDTO="";String strQRDAT="";String strQRDAT1="";
	String strPRMGR = ""; String strPRSGR = ""; String strSGRDS = ""; String strMGRDS = ""; String strMONTH = ""; String strLOTNO="";
	String strPRDDS,strPRDCD,strDAY,strDATE;
		
	String strLOTNO1 = "";
	String strPRMGR1 = "";
	String strPRDCD1 = "";
	String strPRSGR1 = "";
	
	String strLOTNO2 = "";
	String strPRMGR2 = "";
	String strPRDCD2 = "";
	String strPRSGR2 = "";
	
	String strTOTLTQT = "";
	String strTOTPRQT = "";
	String strTOTSGQT = "";
	String strTOTMGQT = "";
	String strTOTGTQT = "";
	
	String staLOTQT[];
	String staPRDQT[];
	String staSGRQT[];
	String staMGRQT[];
	String staGRTOT[];
	
	StringBuffer LM_PRNSTR;
	String[] staTBLHD = {"Grade","< 3 months","3 - 6 months","6 - 9 months","9 - 12 months","> 12 months","Total Qty."};
	int[] intCOLSZ = {140,100,100,100,100,100,100};
	
	int intCOUNT = 0;
	int l = 0,intCNT = 0;
	int intPEROD = 0;
	int intRWCNT = 0;
	int intQRYYR = 0;
	int intQRYMM = 0;
	int intQRYDY = 0;
	int intDBSYR = 0;
	int intDBSMM = 0;
	int intDBSDY = 0;
	int intRETRN = 0;
	
	double dblSTKQT = 0;
	boolean flgQRYFL=true;	                     /** Hashtable for storing different Main Product Types*/
	private Hashtable<String,String> hstMNGRP = new Hashtable<String,String>();/** Hashtable for storing different Sub Product Types*/
	private Hashtable<String,String> hstSBGRP = new Hashtable<String,String>();
	
	fg_qrgra()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Starting From"),3,2,1,1,this,'L');
			add(txtGRDFR = new TxtLimit(40),3,3,1,2,this,'L');
			add(new JLabel("Ending To"),4,2,1,1,this,'L');
			add(txtGRDTO = new TxtLimit(40),4,3,1,2,this,'L');
		
			add(new JLabel("Date"),5,2,1,1,this,'L');
			add(txtQRDAT = new TxtDate(),5,3,1,1,this,'C');
			add(chkLOTWS = new JCheckBox("Lotwise"),5,4,1,1,this,'L');
			add(btnRUN = new JButton("Run"),5,6,1,1,this,'L');
			
			add(txtNOFDS = new TxtNumLimit(4),6,3,1,0.5,this,'L');
			add(new JLabel(" Days And Above"),6,4,1,1,this,'L');
			
			tblDETAL = crtTBLPNL1(this,staTBLHD,30000,7,1,10,7.9,intCOLSZ,new int[]{});
			M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ( 'ISOFGXXRPT','M"+cl_dat.M_strCMPCD_pbst+"COXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL')";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	

			if(M_rstRSSET != null)   
			{
				while(M_rstRSSET.next())
				{   
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("MG"))
						hstMNGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,2),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("SG"))
						hstSBGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,4),M_rstRSSET.getString("CMT_CODDS"));
					
				}
				M_rstRSSET.close();
			}
			
			setENBL(false);
			chkLOTWS.setSelected(false);
		}
		catch(Exception E_LX)
		{
			setMSG(E_LX,"Constructor");
		}
	}
	private void crtLINE(int P_intCNT)
	{
		String strln = "";
		try
		{
			for(int i=1;i<=P_intCNT;i++)
			{
				strln += "-";
			}
			dosREPORT.writeBytes(padSTRING('L'," ",LM_LMRGN));
			dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtLINE ");
		}
	}
	
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		try
		{
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));       // Convert Into Local Date Format
			M_calLOCAL.add(Calendar.DATE,-1);                     // Increase Date from +1 with Locked Date
			strQRDAT1 = M_fmtLCDAT.format(M_calLOCAL.getTime());
			txtQRDAT.setText(strQRDAT1);
			txtNOFDS.setText("0");
		}
		catch(Exception E)
		{
			setMSG(E,"setENBL");
			
		}
		
	}
		
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtGRDFR)
			{
				M_strHLPFLD = "txtGRDFR";
				M_strSQLQRY = "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_PRDCD=ST_PRDCD";
				if(txtGRDFR.getText().trim().length()>0)
				{
					M_strSQLQRY += " AND PR_PRDDS LIKE '"+txtGRDFR.getText().trim()+"%'"; 
				}
				M_strSQLQRY += " order by  PR_PRDDS";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade"},1,"CT");
			}
			if(M_objSOURC == txtGRDTO)
			{
				M_strHLPFLD = "txtGRDTO";
				M_strSQLQRY = "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_PRDCD=ST_PRDCD";
				if(txtGRDTO.getText().trim().length()>0)
				{
					M_strSQLQRY += " AND PR_PRDDS LIKE '"+txtGRDTO.getText().trim()+"%'"; 
				}
				M_strSQLQRY += " order by  PR_PRDDS";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade"},1,"CT");
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtGRDFR)
			{
				txtGRDTO.setText(txtGRDFR.getText().trim());
				txtGRDTO.requestFocus();
			}
			if(M_objSOURC== txtGRDTO)
			{
				txtQRDAT.requestFocus();
				setMSG("Enter To Date",'N');
			}
			if(M_objSOURC == txtQRDAT )
			{
				txtNOFDS.requestFocus();
			}
			if(M_objSOURC == txtNOFDS)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}   
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtGRDFR"))
			{
				txtGRDFR.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtGRDTO"))
			{
				txtGRDTO.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");			
		}
	}
		
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(L_AE.getSource().equals(btnRUN))
			{
				if(!vldDATA())
					return;
				pnlDETAL  = new JPanel(null);
				setCursor(cl_dat.M_curWTSTS_pbst);
				
				setMSG("Data Fetching in Progress... ",'N');
				tblDETAL.clrTABLE();
				strQRDAT = txtQRDAT.getText().toString().trim();
				
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strQRDAT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
				strQRDAT1 = M_fmtLCDAT.format(M_calLOCAL.getTime());  
				
				strGRDFR = txtGRDFR.getText().toString();
				strGRDTO = txtGRDTO.getText().toString();
				strQRDAT = txtQRDAT.getText().toString();
				
				try
				{
					/*M_strSQLQRY = "Select count(*) from CO_PRMST,FG_STMST where ST_PRDCD=PR_PRDCD and (ST_DOSQT+ST_DOUQT) > 0";
					M_strSQLQRY += " and ST_RCTDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+ "'  ";
					if(!strGRDFR.equals("") && !strGRDTO.equals(""))
						M_strSQLQRY +="   and PR_PRDDS between '"+strGRDFR.trim()+"' and '"+strGRDTO.trim()+"'";
					*/
					//changed on 15/04/2008
					
					M_strSQLQRY = "Select count(*) from CO_PRMST,FG_STMST,PR_LTMST where ";
					M_strSQLQRY += "ST_PRDTP = LT_PRDTP and ST_LOTNO = LT_LOTNO and ST_RCLNO = LT_RCLNO and ST_CMPCD = LT_CMPCD and ST_PRDCD=PR_PRDCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (ST_DOSQT+ST_DOUQT) > 0";
					M_strSQLQRY += " and CONVERT(varchar,LT_PSTDT,101) <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+ "'  ";
					if(!strGRDFR.equals("") && !strGRDTO.equals(""))
						M_strSQLQRY +="   and PR_PRDDS between '"+strGRDFR.trim()+"' and '"+strGRDTO.trim()+"'";
					
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					//System.out.println(M_strSQLQRY);
					while(M_rstRSSET.next())
					{
						intCNT = M_rstRSSET.getInt(1);
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"getDTLRW");
				}
				if(intCNT>0)
				{
					flgQRYFL = true;
					intCNT = intCNT * 2;
					TCL_PRDDS = tblDETAL.getColumn(tblDETAL.getColumnName(TB_PRDDS));    
					TCL_les3M = tblDETAL.getColumn(tblDETAL.getColumnName(TB_les3M));    
					TCL_3to6M = tblDETAL.getColumn(tblDETAL.getColumnName(TB_3to6M));    
					TCL_6to9M = tblDETAL.getColumn(tblDETAL.getColumnName(TB_6to9M));    
					TCL_9to12 = tblDETAL.getColumn(tblDETAL.getColumnName(TB_9to12));    
					TCL_grt12 = tblDETAL.getColumn(tblDETAL.getColumnName(TB_grt12));    
					TCL_TOTQT = tblDETAL.getColumn(tblDETAL.getColumnName(TB_TOTQT));    
					tblDETAL.clrTABLE();
					if(tblDETAL.isEditing())
						tblDETAL.getCellEditor().stopCellEditing();
					tblDETAL.setRowSelectionInterval(0,0);
					tblDETAL.setColumnSelectionInterval(0,0);
					
					getALLREC(strQRDAT,flgQRYFL);
					TCL_PRDDS.setCellRenderer(new RowRenderer1());
					TCL_les3M.setCellRenderer(new RowRenderer());
					TCL_3to6M.setCellRenderer(new RowRenderer());
					TCL_6to9M.setCellRenderer(new RowRenderer());
					TCL_9to12.setCellRenderer(new RowRenderer());
					TCL_grt12.setCellRenderer(new RowRenderer());
					TCL_TOTQT.setCellRenderer(new RowRenderer());					
				}
				else
					setMSG("No Data Found... ",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
		
	public boolean vldDATA()
	{
		/*if(txtGRDFR.getText().trim().length()==0)
		{
			setMSG("Enter From Grade ",'E');
			txtGRDFR.requestFocus();
			return false;
		}
		if(txtGRDTO.getText().trim().length()==0)
		{
			setMSG("Enter From Grade ",'E');
			txtGRDTO.requestFocus();
			return false;
		}*/
		if(txtNOFDS.getText().trim().length()==0)
		{
			setMSG("Enter No. Of Days ",'E');
			txtNOFDS.requestFocus();
			return false;
		}
		return true;
	}
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
			    doPRINT(strFILNM1);		
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM1); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM1); 
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	
	public void getALLREC(String P_strQRDAT,boolean P_flgQRYFL)  // fetches all the records
	{
		try
		{
			strQRDAT = P_strQRDAT;
			//System.out.println("strQRDAT : "+strQRDAT);
            
			M_calLOCAL.setTime(M_fmtLCDAT.parse(strQRDAT));       // Convert Into Local Date Format
			M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
			strQRDAT1 = M_fmtLCDAT.format(M_calLOCAL.getTime());  
			
			flgQRYFL = P_flgQRYFL;
			LM_HSTST = new Hashtable<String,String>();
			intRWCNT = 0;
			strDAY = P_strQRDAT.substring(0,2).toString().trim();
			strMONTH = P_strQRDAT.substring(3,5).toString().trim();
			strFILNM1 = strFILNM.concat("fgga"+strDAY+strMONTH+".doc"); 
			//System.out.println("strFILNM1 : "+strFILNM1);
			fosREPORT = new FileOutputStream(strFILNM1);
			dosREPORT = new DataOutputStream(fosREPORT);
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 69;
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strCPI17);
			intCOUNT = 5;
			
			staLOTQT = new String[10];
			staPRDQT = new String[10];
			staSGRQT = new String[10];
			staMGRQT = new String[10];
			staGRTOT = new String[10];
			
			strTOTLTQT = "0";
			strTOTPRQT = "0";
			strTOTSGQT = "0";
			strTOTMGQT = "0";
			strTOTGTQT = "0";
			
			strLOTNO1 = "";  // Previous Lot No.
			strPRMGR1 = "";  // Previous Product Type
			strPRDCD1 = "";  // Previous Product Description
			strPRSGR1 = "";  // Previous Product Sub Type
			
			strLOTNO2 = "";  // Previous Lot No.
			strPRMGR2 = "";  // Previous Product Type
			strPRDCD2 = "";  // Previous Product Description
			strPRSGR2 = "";  // Previous Product Sub Type
			
			strLOTNO = "";			
			strPRMGR = "";
			strPRSGR = "";
			strPRDCD = "";
			intPEROD =0;
			dblSTKQT = 0;
			for(l = 1;l <= intCOUNT;l++)
			{
				staLOTQT[l] = "0";
				staPRDQT[l] = "0";
				staSGRQT[l] = "0";
				staMGRQT[l] = "0";
				staGRTOT[l] = "0";
			}
			boolean L_flgFIRST = true;
			boolean L_flgEOF = false;
			int L_intCCSVL = 0;
			tblDETAL.clrTABLE();
			if(chkLOTWS.isSelected())
			{
				/*M_strSQLQRY = "Select SUBSTRING(ST_PRDCD,1,2) LM_PRDTP,SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,ST_PRDCD,ST_LOTNO,ST_RCTDT,";
				M_strSQLQRY += "sum(ST_DOSQT+ST_DOUQT) L_STKQT from CO_PRMST,FG_STMST where ST_PRDCD=PR_PRDCD and (ST_DOSQT+ST_DOUQT) > 0";
				M_strSQLQRY += " and ST_RCTDT <='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"' ";
				if(Integer.parseInt(txtNOFDS.getText())>0)
					M_strSQLQRY += " and days('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"')-days(st_rctdt)>"+txtNOFDS.getText();
				if(!strGRDFR.equals("") && !strGRDTO.equals(""))
					M_strSQLQRY +="  and PR_PRDDS between '"+strGRDFR.trim()+"' and '"+strGRDTO.trim()+"'";
			
				M_strSQLQRY += " group by SUBSTRING(ST_PRDCD,1,2),SUBSTRING(ST_PRDCD,1,4),ST_PRDCD,ST_LOTNO,ST_RCTDT";
				M_strSQLQRY += " order by LM_PRDTP,LM_SUBPD,ST_PRDCD,ST_LOTNO,ST_RCTDT";
				*/
				
				//changed on 15/04/2008
				M_strSQLQRY = "Select SUBSTRING(ST_PRDCD,1,2) LM_PRDTP,SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,ST_PRDCD,ST_LOTNO,LT_PSTDT,";
				M_strSQLQRY += "sum(ST_DOSQT+ST_DOUQT) L_STKQT from CO_PRMST,FG_STMST,PR_LTMST where ";
				M_strSQLQRY += "ST_PRDCD=PR_PRDCD and ST_CMPCD=LT_CMPCD and ST_PRDTP = LT_PRDTP and ST_LOTNO = LT_LOTNO and ST_RCLNO = LT_RCLNO and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (ST_DOSQT+ST_DOUQT) > 0";
				M_strSQLQRY += " and CONVERT(varchar,LT_PSTDT,101) <='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"' ";
				if(Integer.parseInt(txtNOFDS.getText())>0)
					M_strSQLQRY += " and days('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"')-days(lt_pstdt)>"+txtNOFDS.getText();
				if(!strGRDFR.equals("") && !strGRDTO.equals(""))
					M_strSQLQRY +="  and PR_PRDDS between '"+strGRDFR.trim()+"' and '"+strGRDTO.trim()+"'";
			
				M_strSQLQRY += " group by SUBSTRING(ST_PRDCD,1,2),SUBSTRING(ST_PRDCD,1,4),ST_PRDCD,ST_LOTNO,LT_PSTDT";
				M_strSQLQRY += " order by LM_PRDTP,LM_SUBPD,ST_PRDCD,ST_LOTNO,LT_PSTDT";
				
				
				//System.out.println("chkLOTWS = "+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					//System.out.println(">>>>>>>>>>>>"+M_fmtLCDAT.format(M_rstRSSET.getDate("LT_PSTDT")));
					strDATE = M_fmtLCDAT.format(M_rstRSSET.getDate("LT_PSTDT"));
					strPRDCD2 = M_rstRSSET.getString("ST_PRDCD").trim();
					strPRSGR2 = M_rstRSSET.getString("LM_SUBPD").trim();
					strPRMGR2 = M_rstRSSET.getString("LM_PRDTP").trim();
					strLOTNO2 = M_rstRSSET.getString("ST_LOTNO").trim();
					dblSTKQT = M_rstRSSET.getDouble("L_STKQT");
					while(!L_flgEOF)
					{
						strPRDCD = strPRDCD2;
						strPRSGR = strPRSGR2;
						strPRMGR = strPRMGR2;
						strLOTNO = strLOTNO2;
						if(cl_dat.M_intLINNO_pbst >= 68)
						{
							cl_dat.M_intLINNO_pbst = 0;
							cl_dat.M_PAGENO += 1;
							prnHEADER(); //gets the Header of the report
						}
						if(L_flgFIRST)
						{
							strPRDCD1 = strPRDCD;
							strPRSGR1 = strPRSGR;
							strPRMGR1 = strPRMGR;
							strLOTNO1 = strLOTNO;
							L_flgFIRST = false;
						}
						prnGRPHDR("MG",4);
						strPRMGR1 = strPRMGR;
						while((strPRMGR).equals(strPRMGR1) && !L_flgEOF)
						{
							prnGRPHDR("SG",6);
							strPRMGR = strPRMGR2;
							strPRMGR1 = strPRMGR;
							while((strPRMGR+strPRSGR).equals(strPRMGR1+strPRSGR1) && !L_flgEOF)
							{
								prnGRPHDR("PR",8);
								strPRMGR = strPRMGR2;
								strPRSGR = strPRSGR2;
								strPRMGR1 = strPRMGR;
								strPRSGR1 = strPRSGR;
								while((strPRMGR+strPRSGR+strPRDCD).equals(strPRMGR1+strPRSGR1+strPRDCD1) && !L_flgEOF)
								{
									strPRMGR = strPRMGR2;
									strPRSGR = strPRSGR2;
									strPRMGR1 = strPRMGR;
									strPRSGR1 = strPRSGR;
									strPRDCD = strPRDCD2;
									strPRDCD1 = strPRDCD;
									while((strPRMGR+strPRSGR+strPRDCD+strLOTNO).equals(strPRMGR1+strPRSGR1+strPRDCD1+strLOTNO1) && !L_flgEOF)
									{
										intPEROD = calPERIOD(strDATE,strQRDAT);  //strQRDAT-strDATE
										L_intCCSVL = 0;
										
										if(intPEROD > 365)
											L_intCCSVL = 5;
										else if(intPEROD > 270)
											L_intCCSVL = 4;
										else if(intPEROD > 180)
											L_intCCSVL = 3;
										else if(intPEROD > 90)
											L_intCCSVL = 2;
										else
											L_intCCSVL = 1;
										
										staLOTQT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staLOTQT[L_intCCSVL])+dblSTKQT,3).trim();
										staPRDQT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staPRDQT[L_intCCSVL])+dblSTKQT,3).trim();
										staSGRQT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staSGRQT[L_intCCSVL])+dblSTKQT,3).trim();
										staMGRQT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staMGRQT[L_intCCSVL])+dblSTKQT,3).trim();
										staGRTOT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staGRTOT[L_intCCSVL])+dblSTKQT,3).trim();
											
										strTOTLTQT = setNumberFormat(Double.parseDouble(strTOTLTQT)+dblSTKQT,3).trim();
										strTOTPRQT = setNumberFormat(Double.parseDouble(strTOTPRQT)+dblSTKQT,3).trim();
										strTOTSGQT = setNumberFormat(Double.parseDouble(strTOTSGQT)+dblSTKQT,3).trim();
										strTOTMGQT = setNumberFormat(Double.parseDouble(strTOTMGQT)+dblSTKQT,3).trim();
										strTOTGTQT = setNumberFormat(Double.parseDouble(strTOTGTQT)+dblSTKQT,3).trim();
										if (!M_rstRSSET.next())
										{
											L_flgEOF = true;
											break;
										}
										strPRMGR2 = M_rstRSSET.getString("LM_PRDTP").trim();
										strPRSGR2 = M_rstRSSET.getString("LM_SUBPD").trim();
										strPRDCD2 = M_rstRSSET.getString("ST_PRDCD").trim();
										strLOTNO2 = M_rstRSSET.getString("ST_LOTNO").trim();
										
										dblSTKQT = M_rstRSSET.getDouble("L_STKQT");
										strDATE = M_fmtLCDAT.format(M_rstRSSET.getDate("LT_PSTDT"));
										intPEROD = calPERIOD(strDATE,strQRDAT);
									
										strPRMGR = strPRMGR2;
										strPRSGR = strPRSGR2;
										strPRDCD = strPRDCD2;
										strLOTNO = strLOTNO2;
									}
									prnGRPTOT("LT",strTOTLTQT,"N");
									intGRPTOT("LT");
								}
								prnGRPTOT("PR",strTOTPRQT,"B");
								intGRPTOT("PR");
							}
							prnGRPTOT("SG",strTOTSGQT,"B");
							intGRPTOT("SG");
						}
						prnGRPTOT("MG",strTOTMGQT,"B");
						intGRPTOT("MG");
					}
					prnFOOTR();
					setMSG("",'N');
				}
				else
					setMSG("No Record exists.",'N');
			}
			else
			{
				/*
				M_strSQLQRY = "Select SUBSTRING(ST_PRDCD,1,2) LM_PRDTP,SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,ST_PRDCD,ST_RCTDT,";
				M_strSQLQRY += "sum(ST_DOSQT+ST_DOUQT) L_STKQT from CO_PRMST,FG_STMST where ST_PRDCD=PR_PRDCD and (ST_DOSQT+ST_DOUQT) > 0";
				M_strSQLQRY += " and ST_RCTDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"' ";
				if(Integer.parseInt(txtNOFDS.getText())>0)
					M_strSQLQRY += " and days('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"')-days(st_rctdt)>"+txtNOFDS.getText();
				if(!strGRDFR.equals("") && !strGRDTO.equals(""))
					M_strSQLQRY +="  and PR_PRDDS between '"+strGRDFR.trim()+"' and '"+strGRDTO.trim()+"'";
				
				M_strSQLQRY += " group by  SUBSTRING(ST_PRDCD,1,2),SUBSTRING(ST_PRDCD,1,4),ST_PRDCD,ST_RCTDT";
				M_strSQLQRY += " order by  LM_PRDTP,LM_SUBPD,ST_PRDCD,ST_RCTDT";
				*/
				//Changed On 15/04/2008
				
				M_strSQLQRY = "Select SUBSTRING(ST_PRDCD,1,2) LM_PRDTP,SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,ST_PRDCD,LT_PSTDT,";
				M_strSQLQRY += "sum(ST_DOSQT+ST_DOUQT) L_STKQT from CO_PRMST,FG_STMST,PR_LTMST where ";
				M_strSQLQRY += " ST_PRDTP = LT_PRDTP and ST_LOTNO = LT_LOTNO and ST_CMPCD=LT_CMPCD and ST_RCLNO = LT_RCLNO and ST_PRDCD=PR_PRDCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (ST_DOSQT+ST_DOUQT) > 0";
				M_strSQLQRY += " and CONVERT(varchar,LT_PSTDT,101) <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"' ";
				if(Integer.parseInt(txtNOFDS.getText())>0)
					M_strSQLQRY += " and days('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strQRDAT))+"')-days(LT_PSTDT)>"+txtNOFDS.getText();
				if(!strGRDFR.equals("") && !strGRDTO.equals(""))
					M_strSQLQRY +="  and PR_PRDDS between '"+strGRDFR.trim()+"' and '"+strGRDTO.trim()+"'";
				
				M_strSQLQRY += " group by  SUBSTRING(ST_PRDCD,1,2),SUBSTRING(ST_PRDCD,1,4),ST_PRDCD,LT_PSTDT";
				M_strSQLQRY += " order by  LM_PRDTP,LM_SUBPD,ST_PRDCD,LT_PSTDT";
				
				//System.out.println("NOchk = "+M_strSQLQRY);
				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					strDATE = M_fmtLCDAT.format(M_rstRSSET.getDate("LT_PSTDT"));
					strPRDCD2 = M_rstRSSET.getString("ST_PRDCD").trim();
					strPRSGR2 = M_rstRSSET.getString("LM_SUBPD").trim();
					strPRMGR2 = M_rstRSSET.getString("LM_PRDTP").trim();
					dblSTKQT = M_rstRSSET.getDouble("L_STKQT");
					while(!L_flgEOF)
					{
						strPRDCD = strPRDCD2;
						strPRSGR = strPRSGR2;
						strPRMGR = strPRMGR2;
						if(cl_dat.M_intLINNO_pbst >= 68)
						{
							cl_dat.M_intLINNO_pbst = 0;
							cl_dat.M_PAGENO += 1;
							prnHEADER(); //gets the Header of the report
						}
						if(L_flgFIRST)
						{
							strPRDCD1 = strPRDCD;
							strPRSGR1 = strPRSGR;
							strPRMGR1 = strPRMGR;
							L_flgFIRST = false;
						}
						prnGRPHDR("MG",4);
						strPRMGR1 = strPRMGR;
						while((strPRMGR).equals(strPRMGR1) && !L_flgEOF)
						{
							prnGRPHDR("SG",6);
							strPRMGR = strPRMGR2;
							strPRMGR1 = strPRMGR;
							while((strPRMGR+strPRSGR).equals(strPRMGR1+strPRSGR1) && !L_flgEOF)
							{
								strPRMGR = strPRMGR2;
								strPRSGR = strPRSGR2;
								strPRMGR1 = strPRMGR;
								strPRSGR1 = strPRSGR;
								while((strPRMGR+strPRSGR+strPRDCD).equals(strPRMGR1+strPRSGR1+strPRDCD1) && !L_flgEOF)
								{
									intPEROD = calPERIOD(strDATE,strQRDAT);  //strQRDAT-strDATE
									L_intCCSVL = 0;
								
									if(intPEROD > 365)
										L_intCCSVL = 5;
									else if(intPEROD > 270)
										L_intCCSVL = 4;
									else if(intPEROD > 180)
										L_intCCSVL = 3;
									else if(intPEROD > 90)
										L_intCCSVL = 2;
									else
										L_intCCSVL = 1;
									
									staPRDQT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staPRDQT[L_intCCSVL])+dblSTKQT,3).trim();
									staSGRQT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staSGRQT[L_intCCSVL])+dblSTKQT,3).trim();
									staMGRQT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staMGRQT[L_intCCSVL])+dblSTKQT,3).trim();
									staGRTOT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staGRTOT[L_intCCSVL])+dblSTKQT,3).trim();
											
									strTOTPRQT = setNumberFormat(Double.parseDouble(strTOTPRQT)+dblSTKQT,3).trim();
									strTOTSGQT = setNumberFormat(Double.parseDouble(strTOTSGQT)+dblSTKQT,3).trim();
									strTOTMGQT = setNumberFormat(Double.parseDouble(strTOTMGQT)+dblSTKQT,3).trim();
									strTOTGTQT = setNumberFormat(Double.parseDouble(strTOTGTQT)+dblSTKQT,3).trim();
									if (!M_rstRSSET.next())
									{
										L_flgEOF = true;
										break;
									}
									
									strPRMGR2 = M_rstRSSET.getString("LM_PRDTP").trim();
									strPRSGR2 = M_rstRSSET.getString("LM_SUBPD").trim();
									strPRDCD2 = M_rstRSSET.getString("ST_PRDCD").trim();
										
									dblSTKQT = M_rstRSSET.getDouble("L_STKQT");
									strDATE = M_fmtLCDAT.format(M_rstRSSET.getDate("LT_PSTDT"));
									intPEROD = calPERIOD(strDATE,strQRDAT);
										
									strPRMGR = strPRMGR2;
									strPRSGR = strPRSGR2;
									strPRDCD = strPRDCD2;
								}
								prnGRPTOT("PR",strTOTPRQT,"N");
								intGRPTOT("PR");
							}
							prnGRPTOT("SG",strTOTSGQT,"B");
							intGRPTOT("SG");
						}
						prnGRPTOT("MG",strTOTMGQT,"B");
						intGRPTOT("MG");
					}
					prnFOOTR();
					setMSG("",'N');
				}
				else
					setMSG("No Record exists.",'N');
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	
	private int calPERIOD(String LP_DBSDAT,String LP_QRYDAT)
	{
		intRETRN = 0;
		try
		{
			intDBSDY = Integer.parseInt(LP_DBSDAT.substring(0,2).toString().trim());
			intDBSMM = Integer.parseInt(LP_DBSDAT.substring(3,5).toString().trim());
			intDBSYR = Integer.parseInt(LP_DBSDAT.substring(6,10).toString().trim());
			intQRYDY = Integer.parseInt(LP_QRYDAT.substring(0,2).toString().trim());
			intQRYMM = Integer.parseInt(LP_QRYDAT.substring(3,5).toString().trim());
			intQRYYR = Integer.parseInt(LP_QRYDAT.substring(6,10).toString().trim());
			intRETRN = ((intQRYYR-intDBSYR)*365) + ((intQRYMM-intDBSMM)*30) + (intQRYDY-intDBSDY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calPERIOD");
		}
		return intRETRN;
	}
	
	private void prnFOOTR()
	{
		try
		{
			if(cl_dat.M_intLINNO_pbst >= 61)
				prnFMTCHR(dosREPORT,M_strEJT);
			dosREPORT.writeBytes ("\n");
			prnGRPTOT("GT",strTOTGTQT,"B");
			dosREPORT.writeBytes ("\n");
			crtLINE(136);
			for(int i = 1;i <= 5;i++)
				dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",20));//margin
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",40));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY  ",20));
			dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)  ",35));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes ("\n");
			crtLINE(136);
			cl_dat.M_intLINNO_pbst += 9;
			prnFMTCHR(dosREPORT,M_strEJT);				
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			fosREPORT.close();
			dosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
		
	private void prnGRPHDR(String LP_GRPCT, int LP_MRGVL)
	{
		try
		{
			String L_GRPDS = "";
			if (LP_GRPCT.equals("PR"))
				L_GRPDS = getPRDDS(strPRDCD1);
			else if (LP_GRPCT.equals("MG"))
				L_GRPDS=hstMNGRP.get(strPRMGR1).toString();
			
			else if (LP_GRPCT.equals("SG"))
				L_GRPDS=hstSBGRP.get(strPRSGR1).toString();
				
			if(flgQRYFL)
			{
				LM_HSTST.put(String.valueOf(intRWCNT),L_GRPDS);
				tblDETAL.setValueAt(L_GRPDS,intRWCNT,TB_PRDDS);
				intRWCNT++;
			}
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",LP_MRGVL));
			dosREPORT.writeBytes(padSTRING('R',L_GRPDS,(28-LP_MRGVL)));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 2;
			if(cl_dat.M_intLINNO_pbst >= 68)
			{
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				crtLINE(136);
				prnFMTCHR(dosREPORT,M_strEJT);				
				prnHEADER(); //gets the Header of the report
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPHDR");
		}
	}
	
	private void prnGRPTOT(String LP_GRPCT,String LP_TOTXXQT,String LP_BLDFL)
	{
		try
		{
			LM_PRNSTR = new StringBuffer("");
			if (LP_GRPCT.equals("LT"))
			{
				dosREPORT.writeBytes(padSTRING('R',"",12));
				LM_PRNSTR.append(padSTRING('R',strLOTNO1,16));
				if(flgQRYFL)
					tblDETAL.setValueAt(strLOTNO1,intRWCNT,TB_PRDDS);
				for(int l=1;l<=intCOUNT;l++)
				{
					LM_PRNSTR.append(padSTRING('L',getDASH(staLOTQT[l]),15));
					if(flgQRYFL)
						tblDETAL.setValueAt(getDASH(staLOTQT[l]),intRWCNT,l);
				}
			}
			else if (LP_GRPCT.equals("PR"))
			{
				strPRDDS = getPRDDS(strPRDCD1);
				dosREPORT.writeBytes(padSTRING('R',"",10));
				LM_PRNSTR.append(padSTRING('R',strPRDDS,18));
				if(flgQRYFL)
				{
					if(chkLOTWS.isSelected())
						LM_HSTST.put(String.valueOf(intRWCNT),"x");
					tblDETAL.setValueAt(strPRDDS,intRWCNT,TB_PRDDS);
				}
				for(int l=1;l<=intCOUNT;l++)
				{
					LM_PRNSTR.append(padSTRING('L',getDASH(staPRDQT[l]),15));
					if(flgQRYFL)
						tblDETAL.setValueAt(getDASH(staPRDQT[l]),intRWCNT,l);
				}
			}
			else if (LP_GRPCT.equals("SG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				strSGRDS=hstSBGRP.get(strPRSGR1).toString();
				
				dosREPORT.writeBytes(padSTRING('R',"",6));
				if(flgQRYFL)
				{
					LM_HSTST.put(String.valueOf(intRWCNT),"x");
					tblDETAL.setValueAt("TOTAL " + strSGRDS,intRWCNT,TB_PRDDS);
				}
				LM_PRNSTR.append(padSTRING('R',"TOTAL " + strSGRDS,22));
				for(int l=1;l<=intCOUNT;l++)
				{
					LM_PRNSTR.append(padSTRING('L',getDASH(staSGRQT[l]),15));
					if(flgQRYFL)
						tblDETAL.setValueAt(getDASH(staSGRQT[l]),intRWCNT,l);
				}
			}
			else if (LP_GRPCT.equals("MG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				strMGRDS=hstMNGRP.get(strPRMGR1).toString();
				
				dosREPORT.writeBytes(padSTRING('R',"",4));
				if(flgQRYFL)
				{
					LM_HSTST.put(String.valueOf(intRWCNT),"x");
					tblDETAL.setValueAt("TOTAL " + strMGRDS,intRWCNT,TB_PRDDS);
				}
				LM_PRNSTR.append(padSTRING('R',"TOTAL " + strMGRDS,24));
				for(int l=1;l<=intCOUNT;l++)
				{
					LM_PRNSTR.append(padSTRING('L',getDASH(staMGRQT[l]),15));
					if(flgQRYFL)
						tblDETAL.setValueAt(getDASH(staMGRQT[l]),intRWCNT,l);
				}
			}
			else if (LP_GRPCT.equals("GT"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				dosREPORT.writeBytes(padSTRING('R',"",4));
				if(flgQRYFL)
				{
					LM_HSTST.put(String.valueOf(intRWCNT),"x");
					tblDETAL.setValueAt("Grand Total",intRWCNT,TB_PRDDS);
				}
				LM_PRNSTR.append(padSTRING('R',"GRAND TOTAL",24));
				for(int l=1;l<=intCOUNT;l++)
				{
					LM_PRNSTR.append(padSTRING('L',getDASH(staGRTOT[l]),15));
					if(flgQRYFL)
						tblDETAL.setValueAt(getDASH(staGRTOT[l]),intRWCNT,l);
				}
			}
			LM_PRNSTR.append(padSTRING('L',getDASH(LP_TOTXXQT),18));
			if(flgQRYFL)
			{
				tblDETAL.setValueAt(getDASH(LP_TOTXXQT),intRWCNT,l);
				intRWCNT++;
			}
			if (LP_BLDFL.equals("B"))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(LM_PRNSTR.toString());
			if (LP_BLDFL.equals("B"))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			
			if(cl_dat.M_intLINNO_pbst >= 68)
			{
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				crtLINE(136);
				prnFMTCHR(dosREPORT,M_strEJT);				
				prnHEADER(); //gets the Header of the report
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPTOT");
			System.out.println("Exception: "+LM_PRNSTR.toString());
		}
	}
	
	private String getDASH(String LP_DSHSTR)
	{
		try
		{
			if(LP_DSHSTR == null || LP_DSHSTR.equals("0"))
				LP_DSHSTR = "-";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDASH");
		}
		return LP_DSHSTR;
	}
		
	private void intGRPTOT(String LP_GRPCT)
	{
		try
		{
			if (LP_GRPCT.equals("LT"))
			{
				for(l = 1;l <= intCOUNT;l++)
					staLOTQT[l] = "0";
				strTOTLTQT = "0";
				strLOTNO1 = strLOTNO;
			}
			else if (LP_GRPCT.equals("PR"))
			{
				for(l = 1;l <= intCOUNT;l++)
					staPRDQT[l] = "0";
				strTOTPRQT = "0";
				strPRDCD1 = strPRDCD;
			}
			else if (LP_GRPCT.equals("SG"))
			{
				for(l = 1;l <= intCOUNT;l++)
					staSGRQT[l] = "0";
				strTOTSGQT = "0";
				strPRSGR1 = strPRSGR;
			}
			else if (LP_GRPCT.equals("MG"))
			{
				for(l = 1;l <= intCOUNT;l++)
					staMGRQT[l] = "0";
				strTOTMGQT = "0";
				strPRMGR1 = strPRMGR;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"intGRPTOT");
		}
	}
	
	/**
	 * @return void
	 * Prints the Header of the report when the report is displayed or printed
	 * for the first time.
	*/
	private void prnHEADER()    //gets the Header of the Report
	{
		try
		{
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
			dosREPORT.writeBytes(padSTRING('L',"Report Date :"+cl_dat.M_txtCLKDT_pbst.getText(),111));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Gradewise Finished Goods Inventory Aging Summary"+(Integer.parseInt(txtNOFDS.getText()) > 0 ? " ( "+txtNOFDS.getText()+" Days And Above )":"" )+" as on "+strQRDAT1+" at 07.00 Hrs.",110));
            dosREPORT.writeBytes(padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,17));
			dosREPORT.writeBytes("\n");
			crtLINE(136);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R'," ",10));
			dosREPORT.writeBytes(padSTRING('R',"Grade",18));
			
			dosREPORT.writeBytes(padSTRING('L',"Below 90",15));
			dosREPORT.writeBytes(padSTRING('L',"Below 180",15));
			dosREPORT.writeBytes(padSTRING('L',"Below 270",15));
			dosREPORT.writeBytes(padSTRING('L',"Below 365",15));
			dosREPORT.writeBytes(padSTRING('L',"365 & above",15));
			dosREPORT.writeBytes(padSTRING('L',"Total Qty.",18));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R'," ",28));
			dosREPORT.writeBytes(padSTRING('L',"Days",15));
			
			dosREPORT.writeBytes(padSTRING('L',"Days",15));
			dosREPORT.writeBytes(padSTRING('L',"Days",15));
			dosREPORT.writeBytes(padSTRING('L',"Days",15));
			dosREPORT.writeBytes(padSTRING('L',"Days",15));
			dosREPORT.writeBytes("\n");
			crtLINE(136);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 8;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHeader");
		}
	}

	class RowRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isselected,boolean hasFocus,int row,int col)
		{
			if(LM_HSTST.get(String.valueOf(row))  != null)
				setForeground(Color.blue);
			else
				setForeground(Color.black);
			int cellValue = (value instanceof Number) ? ((Number)value).intValue() : 0;
			setText((value == null) ? "" : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);
			return super.getTableCellRendererComponent(table,value,isselected,hasFocus,row,col);
		}
	}
	
	class RowRenderer1 extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isselected,boolean hasFocus,int row,int col)
		{
			if(LM_HSTST.get(String.valueOf(row))  != null)
				setForeground(Color.blue);
			else
				setForeground(Color.black);
		    return super.getTableCellRendererComponent(table,value,isselected,hasFocus,row,col);
		}
	}
	
	
	public String getPRDDS(String P_strPRDCD)    //To get Product Grade i.e SH3001
	{
		String L_strPRDDS = "";
		try
		{
			String LM_QUERY = "Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+P_strPRDCD+"'";
			ResultSet LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY);
			while(LM_CTRLST.next())
			{
				L_strPRDDS = LM_CTRLST.getString("PR_PRDDS");
			}
			LM_CTRLST.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDDS");
		}
		if(L_strPRDDS == null)
			L_strPRDDS = "";
		return L_strPRDDS;
	}
	
}