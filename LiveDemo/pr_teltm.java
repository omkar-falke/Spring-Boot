import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.File;

public class pr_teltm extends cl_pbase
{
	private JTextField txtLOTNO,txtRCLNO,txtTPRCD,txtTPRDS,txtIPRDS,txtLINNO,txtSILNO,txtSILST,txtPSTDT,txtPSTTM,txtPENDT,txtPENTM,txtPRDQT,txtPKGWT;
	private JTextField txtRUNNO,txtCLSST,txtLOTST,txtBAGQT,txtDSPQT,txtCLSTM,txtCPRDS,txtRMKDS;
	private JButton btnBGRPT;
	private String[] staHEADR = new String[2];
	private JCheckBox chkBAGCL;	
    private JCheckBox chkYES;	
    private JCheckBox chkNO;	
	private String  strDFRCL ="00",		// Initial Default reclassification no.
					strRCLNO,			// Reclassification No.
					strPRDTP,			// Product type
					strLOTNO,			// Lot Number
					strCLSFL,			// Classification flag
					strTPRCD,			// Taget product code
					strLSTNO = "",			// Last Lot no.
		            strLSTRN = "",			// Last Run No.	
		            strLINNO,			// Line No.	
		            strSILNO,			// Silo No.	
		            strLSTPR ="",		// Last Product code
		            strLSTDT ="",			// Last Lot Date
		            strCURNO,			// Current Lot no.
				    strENDTM,			// End time of lot
				    strADDTM;			// Add time of lot
	private boolean flgSLVLD = false,	// Flag for silo validation 
					flgLTCLS = false,   // flag for changing the mode,closing lot in addition 
					flgFSTLT = false,	// Flag to check first lot in the series 
					flgENDFL = false,	// Lot closing flag
					flgSRVLD = true;    // Lot series validation
	pr_rplcf opr_rplcf;
	
	pr_teltm()
	{
		super(2);
		
		
		setMatrix(14,7);
		setVGAP(15);
		add(new JLabel("Lot Number"),2,1,1,1,this,'L');
		add(txtLOTNO = new TxtNumLimit(8.0),2,2,1,0.70,this,'L');
		add(txtRCLNO = new TxtNumLimit(2.0),2,2,1,0.30,this,'R');
		add(new JLabel("Line No."),2,3,1,1,this,'L');
		add(txtLINNO = new TxtNumLimit(2.0),2,4,1,1,this,'L');
		add(new JLabel("Silo No./Status"),2,5,1,1,this,'L');
		add(txtSILNO = new TxtLimit(5),2,6,1,1,this,'L');
		add(txtSILST = new TxtLimit(5),2,7,1,1,this,'L');
		add(new JLabel("Target Grade"),3,1,1,1,this,'L');
		add(txtTPRCD = new TxtNumLimit(10.0),3,2,1,1,this,'L');
		add(txtTPRDS = new TxtLimit(45),3,3,1,1.94,this,'L');
		add(new JLabel("Start Date/Time"),3,5,1,1,this,'L');
		add(txtPSTDT = new TxtDate(),3,6,1,1,this,'L');
		add(txtPSTTM = new TxtTime(),3,7,1,1,this,'L');
		add(new JLabel("Prd. Qty."),4,1,1,1,this,'L');
		add(txtPRDQT = new TxtNumLimit(10.3),4,2,1,1,this,'L');
		add(new JLabel("Bagged Qty."),4,3,1,1,this,'L');
		add(txtBAGQT = new TxtNumLimit(10.3),4,4,1,1,this,'L');
		add(new JLabel("End Date/Time"),4,5,1,1,this,'L');
		add(txtPENDT = new TxtDate(),4,6,1,1,this,'L');
		add(txtPENTM = new TxtTime(),4,7,1,1,this,'L');
		add(new JLabel("Wt/Board (Kgs)"),5,1,1,1,this,'L');
		add(txtPKGWT = new TxtNumLimit(6.3),5,2,1,1,this,'L');
		add(chkBAGCL = new JCheckBox("Issued for Bagging"),5,5,1,2,this,'L');
		add(new JLabel("Bagging Grade"),5,3,1,1,this,'L');
		add(txtIPRDS = new TxtLimit(15),5,4,1,1,this,'L');
		add(new JLabel("Plant Abnormality"),6,1,1,1,this,'L');
        add(chkYES = new JCheckBox("YES"),6,2,1,0.5,this,'L');
        add(chkNO = new JCheckBox("NO"),6,2,1,0.5,this,'R');
		add(new JLabel("Remarks"),6,3,1,1,this,'L');
		add(txtRMKDS = new TxtLimit(200),6,4,1,4,this,'L');

		add(btnBGRPT = new JButton("Bagging Lot Control Form"),5,6,1,2,this,'L');
		JPanel L_pnlTEMP=new JPanel(null);
		setMatrix(14,4);
		add(new JLabel("Run No."),1,1,1,1,L_pnlTEMP,'L');
		add(txtRUNNO = new TxtLimit(8),1,2,1,1,L_pnlTEMP,'L');
		add(new JLabel("Lot Status"),1,3,1,1,L_pnlTEMP,'L');
		add(txtLOTST = new JTextField(),1,4,1,1,L_pnlTEMP,'L');
		add(new JLabel("Classification Status"),2,1,1,1,L_pnlTEMP,'L');
		add(txtCLSST = new JTextField(),2,2,1,1,L_pnlTEMP,'L');
		add(new JLabel("Classified Grade"),2,3,1,1,L_pnlTEMP,'L');
		add(txtCPRDS = new JTextField(),2,4,1,1,L_pnlTEMP,'L');
		add(new JLabel("Classification Date"),3,1,1,1,L_pnlTEMP,'L');
		add(txtCLSTM = new JTextField(),3,2,1,1,L_pnlTEMP,'L');
		add(new JLabel("Dispatch Qty."),3,3,1,1,L_pnlTEMP,'L');
		add(txtDSPQT = new TxtNumLimit(10.3),3,4,1,1,L_pnlTEMP,'L');
		L_pnlTEMP.setBorder(BorderFactory.createTitledBorder(" Details "));
		add(L_pnlTEMP,7,1,4,4,this,'L');
	}
   void setENBL(boolean P_flgENBFL)
   {
		super.setENBL(P_flgENBFL);
		txtLOTNO.setEnabled(true);
		txtRUNNO.setEnabled(false);
		txtCLSST.setEnabled(false);
		txtLOTST.setEnabled(false);
		txtBAGQT.setEnabled(false);
		txtDSPQT.setEnabled(false);
		txtCPRDS.setEnabled(false);
		txtCLSTM.setEnabled(false);
		txtTPRDS.setEnabled(false);
		txtIPRDS.setEnabled(false);
		btnBGRPT.setEnabled(false);
		chkBAGCL.setEnabled(false);
		chkYES.setEnabled(false);
		chkNO.setEnabled(false);
		txtRMKDS.setEnabled(false);
		txtSILST.setEnabled(false);
  }
 public void keyPressed(KeyEvent L_KE)
 {
		  super.keyPressed(L_KE);
		  if (L_KE.getKeyCode()== L_KE.VK_ESCAPE)
          {
			  if(M_objSOURC == txtLOTNO);
			  {
				flgLTCLS = false;
				cl_dat.M_flgHELPFL_pbst = false;
			  }
		  }// end of VK_Escape
		  else if (L_KE.getKeyCode()== L_KE.VK_F1)
		  {
			try
			{
				if(M_objSOURC == txtTPRCD)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					staHEADR[0] = "Product Code";
					staHEADR[1] = "Description";	
				    M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST WHERE PR_STSFL IN('1','2','3','4')";
					M_strSQLQRY += " AND SUBSTRING(PR_PRDCD,1,2) IN(select cmt_ccsvl from co_cdtrn where cmt_cgmtp ='MST' ";
					M_strSQLQRY += " AND CMT_CGSTP = 'COXXPRD' and cmt_codcd = '" + strPRDTP + "')"; 
					M_strSQLQRY += " ORDER BY PR_PRDDS";
					M_strHLPFLD = "txtTPRCD";
					cl_hlp(M_strSQLQRY ,2,1,staHEADR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtLINNO)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					strPRDTP = M_strSBSCD.substring(2,4);
					staHEADR[0] = "Line No.";
					staHEADR[1] = "Description";	
					M_strSQLQRY = "SELECT cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp =  ";
					M_strSQLQRY = M_strSQLQRY + "'SYS'" + " AND cmt_cgstp = ";
					M_strSQLQRY = M_strSQLQRY + "'PRXXLIN'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
					if(strPRDTP.equals("01"))
					    M_strSQLQRY += " AND CMT_CCSVL = " + "'"+strPRDTP + "'";
					else
					    M_strSQLQRY += " AND CMT_CCSVL <>'01' ";
					M_strHLPFLD = "txtLINNO";
					cl_hlp(M_strSQLQRY ,2,1,staHEADR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSILNO)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String []staHEADR1 = {"Code","Silo No.","Silo status"};	
					M_strSQLQRY = "SELECT cmt_codcd,cmt_codds,cmt_chp01 from co_cdtrn where cmt_cgmtp =  ";
					M_strSQLQRY += "'SYS'" + " AND cmt_cgstp = ";
					M_strSQLQRY += "'PRXXCYL'";
					M_strSQLQRY += " AND CMT_CCSVL LIKE " + "'%"+strPRDTP + "%' and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
					//**NOTE if CMT_CHP01 is 1 then Line validation is applicable else not
					if(flgSLVLD)
						M_strSQLQRY +=" and CMT_CODCD LIKE '" + txtLINNO.getText().trim()+"%'";
					M_strHLPFLD = "txtSILNO";
					cl_hlp(M_strSQLQRY ,2,1,staHEADR1,3,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtLOTNO)
				{	
					flgLTCLS = false;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						cl_dat.M_flgHELPFL_pbst = true;	
						flgLTCLS = true;
						staHEADR[0] = "Lot No.  ";
						staHEADR[1]=  "Lot Start Date";
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						M_strSQLQRY = "select max(LT_LOTNO) L_LOTNO,LT_LINNO from PR_LTMST ";
						M_strSQLQRY    += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(lt_lotno,1,2) = lt_linno";
						M_strSQLQRY    +=" and SUBSTRING(lt_lotno,4,1) = '"+cl_dat.M_strLOGDT_pbst.substring(9) +"'";
						M_strSQLQRY    +=" and lt_prdtp ='"+strPRDTP +"'group by lt_linno ";
						System.out.println(M_strSQLQRY);
						M_strHLPFLD = "txtLOTNO";
						cl_hlp(M_strSQLQRY ,1,1,staHEADR,2,"CT");
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
					else
					{
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						String[] L_ARRHDR = new String[4];
						cl_dat.M_flgHELPFL_pbst = true;	
						L_ARRHDR[0] = "Lot No.  ";
						L_ARRHDR[1]= "Rcl. No.";
						L_ARRHDR[2]= "Description.";
						L_ARRHDR[3]= "Classfication Status";	
						M_strSQLQRY = "Select LT_LOTNO,LT_RCLNO,PR_PRDDS,LT_CLSFL from PR_LTMST,CO_PRMST WHERE PR_PRDCD = ltrim(str(LT_PRDCD,20,0)) AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = "+"'"+strPRDTP+"'" + " and  LT_STSFL <> 'X' and PR_STSFL <> 'X' and LT_CLSFL <> '8' order by LT_CLSFL asc,LT_LOTNO desc";
						//M_strSQLQRY = "Select LT_LOTNO,LT_PSTDT,LT_PENDT,LT_CLSFL from PR_LTMST,CO_PRMST WHERE PR_PRDCD = LT_PRDCD AND LT_PRDTP = "+"'"+strPRDTP+"'" + " and  LT_STSFL <> 'X' and PR_STSFL <> 'X' and LT_CLSFL <> '8' order by LT_CLSFL asc,LT_LOTNO desc";
						M_strHLPFLD = "txtLOTNO";
						cl_hlp(M_strSQLQRY ,1,1,L_ARRHDR,4,"CT");
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
			}
			else if(M_objSOURC == txtRCLNO)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
						cl_dat.M_flgHELPFL_pbst = true;
					    this.setCursor(cl_dat.M_curWTSTS_pbst);
						M_strSQLQRY = " ";
					    M_strSQLQRY = "select LT_RCLNO,PR_PRDDS from PR_LTMST,CO_PRMST where LT_PRDCD = PR_PRDCD AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = ";
					    M_strSQLQRY +="'"+txtLOTNO.getText().trim()+"'";
					    M_strHLPFLD = "txtRCLNO";
					    staHEADR[0] = "RCL No ";
					    staHEADR[1] = "Grade";
						cl_hlp(M_strSQLQRY ,1,1,staHEADR,2,"CT");
						this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			}
			catch(NullPointerException L_NPE)
			{
			    setMSG("Help not available",'N');                            
			}
		  }// end of VK_F1
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtLOTNO)
		{
			strPRDTP = M_strSBSCD.substring(2,4);
			txtIPRDS.setEnabled(false);
			btnBGRPT.setEnabled(false);
			setMSG("Please enter the lot number ..",'N');
		}
		else if(M_objSOURC == txtTPRCD)
			setMSG("Enter the Target Code,F1 to select from List..",'N');
		else if(M_objSOURC == txtLINNO)
			setMSG("Enter the Line Number,F1 to select from list ..",'N');
		else if(M_objSOURC == txtSILNO)
			setMSG("Enter the Cylo Number,F1 to select from the list ..",'N');
		else if(M_objSOURC == txtPSTDT)
			setMSG("Enter the Start date of Lot ..",'N');
		else if(M_objSOURC == txtPSTTM)
			setMSG("Enter the Start Time of Lot..",'N');
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
		if(M_strHLPFLD.equals("txtLOTNO"))
			txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD.equals("txtRCLNO"))
			txtRCLNO.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD.equals("txtTPRCD"))
			txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD.equals("txtLINNO"))
			txtLINNO.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD.equals("txtSILNO"))
		{
			txtSILNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtSILST.setText((cl_dat.M_strHELP_pbst.charAt(cl_dat.M_strHELP_pbst.trim().length()-1) == 'F' ? "Full" : "Empty"));
		}
		
    }
private void exeSELREC()
{
	try
	{
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		String L_strTPRCD,L_strCPRCD,L_strPSTDT="",L_strPENDT="",L_strLOTST,L_strCLSST;
		java.sql.Timestamp L_TMPTM;
		ResultSet L_rstRSSET;
		M_strSQLQRY = "SELECT * from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_prdtp ='"+strPRDTP +"'";
		M_strSQLQRY += " and LT_LOTNO ='"+strLOTNO +"'";
		M_strSQLQRY += " and LT_RCLNO ='"+strRCLNO +"'";
		M_strSQLQRY += " and LT_STSFL <> 'X'";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
		if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				L_strTPRCD = nvlSTRVL(M_rstRSSET.getString("LT_TPRCD"),"");
				L_strCPRCD = nvlSTRVL(M_rstRSSET.getString("LT_CPRCD"),"");
				L_strCLSST = nvlSTRVL(M_rstRSSET.getString("LT_CLSFL"),"");
				strCLSFL = L_strCLSST;
				L_strLOTST = nvlSTRVL(M_rstRSSET.getString("LT_STSFL"),"");
				txtTPRCD.setText(L_strTPRCD);
				txtTPRDS.setText(nvlSTRVL(getPRDDS(L_strTPRCD),""));
				txtLINNO.setText(nvlSTRVL(M_rstRSSET.getString("LT_LINNO"),""));
				txtSILNO.setText(nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),""));
				L_TMPTM = M_rstRSSET.getTimestamp("LT_PSTDT");
				if(L_TMPTM !=null)
					L_strPSTDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("LT_PSTDT"));
				L_TMPTM = M_rstRSSET.getTimestamp("LT_PENDT");
				if(L_TMPTM !=null)
				{
					L_strPENDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("LT_PENDT"));
				}
				if(L_strPSTDT.trim().length() > 0)
				{
					txtPSTDT.setText(L_strPSTDT.substring(0,10));
					txtPSTTM.setText(L_strPSTDT.substring(11,16));
				}
				if(L_strPENDT.trim().length() > 0)
				{
					txtPENDT.setText(L_strPENDT.substring(0,10));
					txtPENTM.setText(L_strPENDT.substring(11,16));
					flgENDFL = true;
				}
				else
					flgENDFL = false;
				txtPRDQT.setText(setNumberFormat(M_rstRSSET.getDouble("LT_PRDQT"),3));
				txtPKGWT.setText(setNumberFormat(M_rstRSSET.getDouble("LT_PKGWT"),3));
				txtIPRDS.setText(nvlSTRVL(M_rstRSSET.getString("LT_IPRDS"),""));
				if(txtIPRDS.getText().trim().length() >0)
				{
					chkBAGCL.setSelected(true);
				}
				else
				{
					chkBAGCL.setSelected(false);
				}
				txtRUNNO.setText(nvlSTRVL(M_rstRSSET.getString("LT_RUNNO"),""));
				txtCPRDS.setText(nvlSTRVL(getPRDDS(L_strCPRCD),""));
				L_TMPTM = M_rstRSSET.getTimestamp("LT_CLSTM");
				if(L_TMPTM !=null)
					txtCLSTM.setText(M_fmtLCDTM.format(L_TMPTM));
				if(nvlSTRVL(M_rstRSSET.getString("LT_PTAFL"),"").equals("Y"))
				{
				    chkYES.setSelected(true);
				    M_strSQLQRY = "SELECT RM_REMDS from QC_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"
				                + strPRDTP +"' AND RM_TSTTP ='LOT' and RM_TSTNO ='"
				                + strLOTNO +"' AND isnull(RM_STSFL,'') <>'X'";
				    L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				    if(L_rstRSSET !=null)
				    {
				        if(L_rstRSSET.next())
				            txtRMKDS.setText(L_rstRSSET.getString("Rm_REMDS"));     
				        L_rstRSSET.close();    
				    }
				}
				if(nvlSTRVL(M_rstRSSET.getString("LT_PTAFL"),"").equals("N"))
				    chkNO.setSelected(true);
				txtBAGQT.setText(setNumberFormat(M_rstRSSET.getDouble("LT_BAGQT"),3));
				txtDSPQT.setText(setNumberFormat(M_rstRSSET.getDouble("LT_DSPQT"),3));
				txtCLSST.setText(nvlSTRVL(cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXCLS",L_strCLSST),""));
				txtLOTST.setText(nvlSTRVL(cl_dat.getPRMCOD("CMT_CODDS","STS","PRXXLOT",L_strLOTST),""));
				M_rstRSSET.close();
			}
		else
		{
			setMSG("Lot No. does not exist..",'E');
			txtLOTNO.requestFocus();
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	catch(Exception L_E)
	{
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		setMSG(L_E,"exeSELREC");
	}
}
	private void exeDELREC(){
		try{
			M_strSQLQRY = "UPDATE PR_LTMST set ";
			M_strSQLQRY += "LT_STSFL = 'X',";
			M_strSQLQRY += "LT_TRNFL = '0',";
			M_strSQLQRY += "LT_LUSBY = '" + cl_dat.M_strUSRCD_pbst + "',";
			M_strSQLQRY += "LT_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";		
			M_strSQLQRY += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '" + strPRDTP + "'";
			M_strSQLQRY += " and LT_LOTNO = '" + strLOTNO + "'";
			M_strSQLQRY += " and LT_RCLNO = '" + strRCLNO + "'";
			M_strSQLQRY += " and LT_PENDT is null";
			
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			if(cl_dat.exeDBCMT("exeSAVE")){
				setMSG("Lot Has been Deleted..",'N');
			}
			else{
			
				setMSG("Deletetion falied..",'E');
			}
		}catch(Exception L_E){}
	}
	
	private boolean exeMODREC(){
		try{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			
			// runno generation logic included on 12/03/03
			if(!flgFSTLT){
				getLSTRN(txtTPRCD.getText().trim(),txtLINNO.getText().trim(),txtLOTNO.getText().trim());
				getLOTDTL(strPRDTP,txtLOTNO.getText().trim());
				if(strPRDTP.equals("01"))
				{
					if(txtTPRCD.getText().trim().equals(strLSTPR.trim()))
					{
						//if(cc_dattm.occ_dattm.cmpDATTM(getPSTDTM(),strLSTDT.trim()) == 0)
						if(strLSTDT.equals(""))
							txtRUNNO.setText(txtLINNO.getText().trim()+cl_dat.M_strLOGDT_pbst.substring(9)+"00001");
						else if(M_fmtLCDTM.parse(getPSTDTM()).compareTo(M_fmtLCDTM.parse(strLSTDT.trim())) == 0)
						{
							if(strLSTRN.substring(3).equals("00000"))
							{
								strLSTRN = strLSTRN.substring(0,2)+"00001";
								txtRUNNO.setText(strLSTRN);
							}
							else
								txtRUNNO.setText(strLSTRN);
						}
						else
							txtRUNNO.setText(genRUNNO1());	   
					}
					else
						txtRUNNO.setText(genRUNNO1());
				}
			}
			else
			{
				if(strPRDTP.equals("01"))
					txtRUNNO.setText(txtLINNO.getText().trim()+cl_dat.M_strLOGDT_pbst.substring(9)+"00001");
			}
			if(!strPRDTP.equals("01"))
			{
				txtRUNNO.setText(getSPRNNO());
			}
			////
			M_strSQLQRY = "UPDATE PR_LTMST set ";
			if(txtIPRDS.getText().trim().length() >15)
				M_strSQLQRY += "LT_IPRDS = '" + txtIPRDS.getText().trim().substring(0,15) + "',";
			else
				M_strSQLQRY += "LT_IPRDS = '" + txtIPRDS.getText().trim()+ "',";
			if(!flgENDFL) // lot has not been closed
			{
				M_strSQLQRY += "LT_TPRCD = '" + txtTPRCD.getText().trim()+ "',";
				M_strSQLQRY += "LT_PRDCD = '" + txtTPRCD.getText().trim()+ "',";
			}
			M_strSQLQRY += "LT_RUNNO = '" + txtRUNNO.getText().trim()+ "',";
			M_strSQLQRY += "LT_CYLNO = '" + txtSILNO.getText().trim() + "',";
			M_strSQLQRY += "LT_PSTDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(getPSTDTM())) + "',";		
			M_strSQLQRY += "LT_PENDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(getPENDTM())) + "',";		
			M_strSQLQRY += "LT_ENDTM = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst + " " + cl_dat.getCURTIM())) + "',";		
			M_strSQLQRY += "LT_ENDBY = '" + cl_dat.M_strUSRCD_pbst + "',";
			M_strSQLQRY += "LT_PRDQT = " + txtPRDQT.getText().trim() + ",";
			M_strSQLQRY += "LT_PKGWT = " + txtPKGWT.getText().trim() + ",";
			M_strSQLQRY += "LT_TRNFL = '0',";		
			M_strSQLQRY += "LT_LUSBY = '" + cl_dat.M_strUSRCD_pbst + "',";
			M_strSQLQRY += "LT_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";		
			if(chkYES.isSelected())
			    M_strSQLQRY += ",LT_PTAFL = 'Y'";		
			if(chkNO.isSelected())
			    M_strSQLQRY += ",LT_PTAFL = 'N'";		
			M_strSQLQRY += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '" + strPRDTP + "'";
			M_strSQLQRY += " and LT_LOTNO = '" + strLOTNO + "'";
			M_strSQLQRY += " and LT_RCLNO = '" + strRCLNO + "'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			if(chkYES.isSelected())
			{
			    // Insert remark into remark master
			    M_strSQLQRY = "Insert into qc_rmmst(rm_cmpcd,rm_qcatp,rm_tsttp,rm_tstno,rm_remds,rm_trnfl,rm_stsfl,rm_lusby,rm_lupdt)values("
            	            +"'"+cl_dat.M_strCMPCD_pbst+"',"
							+"'"+strPRDTP+"',"
            	            +"'LOT',"
            	            +"'"+ txtLOTNO.getText().trim()+"',"
            	            +"'"+ txtRMKDS.getText().trim()+"',";
            M_strSQLQRY += getUSGDTL("RM",'I',"") + ")";
                cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			if(cl_dat.exeDBCMT("exeSAVE")){
				if(flgLTCLS){
					flgLTCLS = false;
					
					setMSG("Lot Has been Closed..",'N');
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return true;
				}
				else
				{
					setMSG("Lot Has been Modified..",'N');
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return true;
				}
			}
			else{
				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG("Modification falied..",'E');
				return false;
			}
			
		}catch(Exception L_E){
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_E,"exeMODREC");
			return false;
		}
	}
	private boolean exeADDREC(){
		try{
			if(getPENDTM().trim().length()>0) // if lot closing time has been given in addition runno generation will be done.
			{
				if(!flgFSTLT)
				{
					getLSTRN(txtTPRCD.getText().trim(),txtLINNO.getText().trim(),txtLOTNO.getText().trim());
					if(strPRDTP.equals("01"))
					{
						if(txtTPRCD.getText().trim().equals(strLSTPR.trim()))
						{
							if(M_fmtLCDTM.parse(getPSTDTM()).compareTo(M_fmtLCDTM.parse(strLSTDT.trim())) == 0){
								if(strLSTRN.substring(3).equals("00000"))
									strLSTRN = strLSTRN.substring(0,2)+"00001";
								txtRUNNO.setText(strLSTRN);
							}
							else
								txtRUNNO.setText(genRUNNO1());	   
						}
						else
							txtRUNNO.setText(genRUNNO1());
					}
				}
				else
				{
					if(strPRDTP.equals("01"))
						txtRUNNO.setText(txtLINNO.getText().trim()+cl_dat.M_strLOGDT_pbst.substring(9)+"00001");
				}
				if(!strPRDTP.equals("01"))
				{
					txtRUNNO.setText(getSPRNNO());
				}
			}
			M_strSQLQRY = "INSERT INTO PR_LTMST (LT_CMPCD,LT_PRDTP,LT_LOTNO,LT_RCLNO,";
			M_strSQLQRY += "LT_PRDCD,LT_RUNNO,LT_TPRCD,LT_IPRDS,LT_LINNO,";
			M_strSQLQRY += "LT_CYLNO,LT_PSTDT,LT_PENDT,";
			if(txtPRDQT.getText().trim().length()>0)
				M_strSQLQRY += "LT_PRDQT,";
			if(txtPKGWT.getText().trim().length()>0)
				M_strSQLQRY += "LT_PKGWT,";
			M_strSQLQRY += "LT_BAGQT,LT_DSPQT,LT_CLSFL,LT_STSFL,LT_TRNFL,LT_LUSBY,";
			M_strSQLQRY += "LT_LUPDT,LT_ADDTM,LT_ADDBY,LT_ENDTM,LT_ENDBY,LT_SBSCD)VALUES(";
			//cl_dat.M_STRSQL = M_strSQLQRY;
			M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
			M_strSQLQRY += "'" + strPRDTP + "',";
			M_strSQLQRY += "'" + strLOTNO.trim() + "',";
			M_strSQLQRY += "'" + strDFRCL + "',";
			M_strSQLQRY += "'" + txtTPRCD.getText().trim() + "',";
			M_strSQLQRY += "'" + nvlSTRVL(txtRUNNO.getText().trim(),"") + "',";
			M_strSQLQRY += "'" + txtTPRCD.getText().trim() + "',";
			if(txtIPRDS.getText().trim().length() >15)
				M_strSQLQRY += "'" + txtIPRDS.getText().trim().substring(0,15) + "',";			
			else
				M_strSQLQRY += "'" + txtIPRDS.getText().trim() + "',";			
			M_strSQLQRY += "'" + txtLINNO.getText().trim() + "',";
			M_strSQLQRY += "'" + txtSILNO.getText().trim() + "',";
			M_strSQLQRY += "'" +M_fmtDBDTM.format(M_fmtLCDTM.parse(getPSTDTM())) + "',";
			if(getPENDTM().length() >0)
				M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(getPENDTM())) + "',";			
			else
				M_strSQLQRY += "null,";			
			if(txtPRDQT.getText().trim().length()>0)
				M_strSQLQRY += txtPRDQT.getText().trim() + ",";
			if(txtPKGWT.getText().trim().length()>0)
				M_strSQLQRY += txtPKGWT.getText().trim() + ",";
			M_strSQLQRY += "0,";	// 0 Bagged Quantity
			M_strSQLQRY += "0,";	// 0 Dispatched Quantity
			M_strSQLQRY += "'0',";	// 0 Classification flag for fresh entry : unclassified
			M_strSQLQRY += "'0',";		// Status flag 0 for fresh entry
			M_strSQLQRY += "'0',";		// 0 trn flg
			M_strSQLQRY +=  "'" + cl_dat.M_strUSRCD_pbst + "',";
			strADDTM = cl_dat.M_strLOGDT_pbst+ " "+cl_dat.getCURTIM();
			strENDTM = cl_dat.M_strLOGDT_pbst+ " "+cl_dat.getCURTIM();
			M_strSQLQRY +=  "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "',";
			M_strSQLQRY +=  "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strADDTM)) + "',";
		
			M_strSQLQRY +=  "'" + cl_dat.M_strUSRCD_pbst + "',";
			//if(cc_dattm.occ_dattm.cmpDATTM(strADDTM,strENDTM)>0)
			if(M_fmtLCDTM.parse(strADDTM).compareTo(M_fmtLCDTM.parse(strENDTM)) == 0)
				strENDTM = strADDTM;
			M_strSQLQRY +=  "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(strENDTM)) + "',";
			M_strSQLQRY +=  "'" + cl_dat.M_strUSRCD_pbst + "',";
			M_strSQLQRY +=  "'" + M_strSBSCD + "')";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			M_strSQLQRY = "Update CO_CDTRN set cmt_chp01 ='F' where cmt_cgmtp ='SYS' and cmt_cgstp ='PRXXCYL' and cmt_codcd ='"+txtSILNO.getText().trim()+"'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeSRLSET("D"+cl_dat.M_strCMPCD_pbst,"PRXXLOT",txtLOTNO.getText().trim().substring(0,4),txtLOTNO.getText().trim().substring(4));
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Lot Has been Added ..",'N');
				clrCOMP();
				return true;
			}
			else{
				setMSG("Addition Failed ..",'E');
				return false;
			}
		}catch(Exception L_E){
			//System.out.println(M_strSQLQRY);
			setMSG(L_E,"exeSAVE");
			return false;
		}
	}
	
public void actionPerformed(ActionEvent L_AE)
{
		super.actionPerformed(L_AE);
		if(M_objSOURC == chkYES)
		{
	        if(chkYES.isSelected())
	        {
	          if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
	          {
	              txtRMKDS.setEnabled(true);	    
	              txtRMKDS.requestFocus();
	          }
	            chkNO.setSelected(false);
	        }
	        else
	            txtRMKDS.setEnabled(false);	    
		}
		else if(M_objSOURC == chkNO)
		{
		    if(chkNO.isSelected())
		    {
	            txtRMKDS.setEnabled(false);
	            txtRMKDS.setText("");	 
	            chkYES.setSelected(false);   
		    }
		}
		else if(M_objSOURC == btnBGRPT)
		{
			try
			{
				opr_rplcf=new pr_rplcf(1);
				if(opr_rplcf.crtREPT(strPRDTP,txtLOTNO.getText().trim(),txtIPRDS.getText().trim(),"EXT"))
				{
				    JComboBox L_cmbLOCAL = opr_rplcf.getPRNLS();
				    opr_rplcf.doPRINT("c:\\reports\\pr_rplcf.doc",L_cmbLOCAL.getSelectedIndex());
				//	Runtime r = Runtime.getRuntime();
				//	Process p = null;
				//	p  = r.exec("co_repprn.bat "+"c:\\reports\\pr_rplcf.doc"); 
				}
				clrCOMP();
				exeWRKSTA();
				btnBGRPT.setEnabled(false);
				txtIPRDS.setEnabled(false);
			}
			catch(Exception L_E)
			{
				System.out.println(L_E.toString());
			}
		}
		else if(M_objSOURC == cl_dat.M_cmbSBSL1_pbst)
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))	
			{
				txtLOTNO.setText("");
				setMSG(" " ,'N');
				txtRCLNO.setEnabled(true);
	    	}
			else
				setMSG("Please Select an option ..",'E');
			strPRDTP = M_strSBSCD.substring(2,4);
			if(!strPRDTP.equals("01"))
			{
				flgSLVLD = false;
				//System.out.println("SPS lots ");
				flgSRVLD = false;
			}
			else
			{
				flgSLVLD = true;
				flgSRVLD = true;
			}
		}
		else if(M_objSOURC == txtLOTNO)
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			strLOTNO = txtLOTNO.getText().trim();
			strRCLNO = txtRCLNO.getText().trim();
			strPRDTP = M_strSBSCD.substring(2,4);
			strRCLNO = strDFRCL;
			clrCOMP();
			txtLOTNO.setText(strLOTNO);
			txtRCLNO.setText(strDFRCL);
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				exeSELREC();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(!flgENDFL)
					{
						txtTPRCD.setEnabled(true);
						chkYES.setEnabled(true);
						chkNO.setEnabled(true);
					}
					else
					{
						txtTPRCD.setEnabled(false);
						chkYES.setEnabled(false);
						chkNO.setEnabled(false);
					}
					txtPRDQT.requestFocus();
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(flgLTCLS)
				{
					// closing of lot.
					exeSELREC();
					setENBL(false);
					txtPRDQT.setEnabled(true);
					if(txtTPRCD.getText().substring(0,2).equals("SX"))
						txtPKGWT.setEnabled(true);
					txtPENDT.setEnabled(false);
					txtPENTM.setEnabled(false);
				}
				else
				{
					exeWRKSTA();
					if(strLOTNO.trim().length() == 0)
					{
						setMSG("Please Enter a valid Lot Number..",'E');
						
					}
					else
					{
						M_strSQLQRY ="Select count(*) from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_prdtp ='"+strPRDTP.trim()+"'";
						M_strSQLQRY +=" and lt_lotno ='"+strLOTNO.trim()+"'";
						M_strSQLQRY +=" and lt_RCLNO ='"+strRCLNO.trim()+"'";
						if(cl_dat.getRECCNT(M_strSQLQRY)>0)
						{
							setMSG("Lot Already Exist..",'E');
							this.setCursor(cl_dat.M_curDFSTS_pbst);
						}
						else
						{
							if(vldLOTSR(strLOTNO))
							{
								if(getPRVLOT(strPRDTP,strLOTNO))
								{
									if(strCURNO ==null)
									{
									    if(!flgSRVLD)
									    {
									        setMSG("",'N');
										    txtLINNO.setEnabled(true);
										    txtLINNO.requestFocus();	
									    }
									}
									else if(!strCURNO.trim().equals(strLOTNO))
									{
										if((strPRDTP.trim().equals("01"))&& (!txtLINNO.getText().trim().equals("08"))&& (!txtLINNO.getText().trim().equals("28"))) // validation applicatble only for PS lots.
										{
										    setMSG("Invalid Lot No. in current series.New Lot No. should be : "+strCURNO,'E');
										}
										else
										{
											setMSG("",'N');
											txtLINNO.setEnabled(true);
											txtLINNO.requestFocus();		
										}
									}
									else
									{
										setMSG("",'N');
										txtLINNO.setEnabled(true);
										txtLINNO.requestFocus();		
									}
								}
							}
						}
					}
				}
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		 }
		else if(M_objSOURC == txtRCLNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				strRCLNO = txtRCLNO.getText().trim();
				exeSELREC();
			}
		}
		else if(M_objSOURC == txtIPRDS)
		{
		 	if(txtIPRDS.getText().trim().length() >0)
			{
				btnBGRPT.setEnabled(true);
				btnBGRPT.requestFocus();
			}
			else
			{
				setMSG("Bagging Grade can not be empty.. ",'E');
				btnBGRPT.setEnabled(false);
			}
		}
		else if(M_objSOURC == txtPRDQT)
		{
			txtPENDT.setEnabled(true);
			txtPENDT.requestFocus();
		}
		else if(M_objSOURC == txtTPRCD)
		{
			if(txtTPRCD.getText().trim().length() ==0)
				setMSG("Target Grade can not be blank.. ",'E');
			else
			{
				M_strSQLQRY ="Select count(*) from CO_PRMST where PR_PRDCD ='"+txtTPRCD.getText().trim()+"'";
				M_strSQLQRY +=" AND PR_PRDTP ='"+strPRDTP.trim() +"'";
				
				if(cl_dat.getRECCNT(M_strSQLQRY)>0)
				{
					setMSG("",'N');
 					txtTPRDS.setText(nvlSTRVL(getPRDDS(txtTPRCD.getText().trim()),""));
					txtPSTDT.setEnabled(true);
					txtPSTDT.requestFocus();		
				}
				else
				{
					setMSG("Invalid Target Grade,Please select the grade from F1 list.. ",'E');
				}
			}
		}
		else if(M_objSOURC == txtLINNO)
		{
			if(vldLINNO())
			{
				txtSILNO.setEnabled(true);
				txtSILNO.requestFocus();
			}
		}
		else if(M_objSOURC == txtSILNO)
		{
			if(txtSILNO.getText().trim().length() ==0)
				setMSG("Cylo Number can not be blank..",'E');
			else
			{
				setMSG("",'N');
				if(vldSILNO())
				{
					txtTPRCD.setEnabled(true);
					txtTPRCD.requestFocus();
				}
			}
		}
		else if(M_objSOURC == txtPSTDT)
		{
			try
			{
				if(M_fmtLCDAT.parse(txtPSTDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date can not be greater than today's date..",'E');
				}
				else
				{
					txtPSTTM.setEnabled(true);
					txtPSTTM.requestFocus();
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Date Comparision");
			}
		}
		else if(M_objSOURC == txtPSTTM)
		{
			try
			{
				if(M_fmtLCDTM.parse(getPSTDTM()).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
				{
					setMSG("Time can not be greater than the current time..",'E');
				}
				else
				{
					txtPRDQT.setEnabled(true);
					if(txtTPRCD.getText().substring(0,2).equals("SX"))
						txtPKGWT.setEnabled(true);
					txtPRDQT.requestFocus();
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Start time Comparision");
			}
		}
		else if(M_objSOURC == txtPENDT)
		{
			try
			{
				//if(txtPENDT.vldDATE() != null)
				//{
					if(M_fmtLCDAT.parse(txtPENDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Date can not be greater than today's date..",'E');
					}
					else if(M_fmtLCDAT.parse(txtPENDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtPENDT.getText().trim()))< 0)
					{
						setMSG("Lot End Date can not be smaler than start date..",'E');
					}
					else
					{
						txtPENTM.setEnabled(true);
						txtPENTM.requestFocus();
					}
				//}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Date Comparision");
			}
		}
		else if(M_objSOURC == txtPENTM)
		{
			try
			{
				if(M_fmtLCDTM.parse(getPENDTM()).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
				{
					setMSG("Date can not be greater than current date and time..",'E');
				}
				else
				{
					if(M_fmtLCDTM.parse(getPSTDTM()).compareTo(M_fmtLCDTM.parse(getPENDTM()))>0)
						setMSG("Lot Start Date can not be greater than end date..",'E');
					else
					    chkYES.requestFocus();
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"End Time Comparision");
			}
		}
		if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
		{
              exeHLPOK();
		}
		else if (M_objSOURC == cl_dat.M_btnUNDO_pbst)
		{
			  txtLOTNO.setText("");
			  flgLTCLS = false;
              clrCOMP();
		}
	}
	private String getPSTDTM()
	{
		if((txtPSTDT.getText().length()>0) && (txtPSTTM.getText().length()>0) )
		{
			return (txtPSTDT.getText().trim()+" "+txtPSTTM.getText().trim());
		}
		else
			return "";
	}
	private String getPENDTM()
	{
		if((txtPENDT.getText().length()>0) && (txtPENTM.getText().length()>0) )
		{
			return (txtPENDT.getText().trim()+" "+txtPENTM.getText().trim());
		}
		else
			return "";
	}
	private boolean getPRVLOT(String P_strPRDTP,String P_strLOTNO)
	{
		try
		{
			strLSTDT ="";
			java.sql.Timestamp L_TMPTM;
			flgFSTLT = false;
			strPRDTP = M_strSBSCD.substring(2,4);
			if(!strPRDTP.equals("01"))
			{
				flgSLVLD = false;
				flgSRVLD = false;
			}
			else
			{
				flgSLVLD = true;
				flgSRVLD = true;
			}
		//	M_strSQLQRY ="Select max(LT_LOTNO) L_LOTNO from PR_LTMST WHERE LT_PRDTP ='"+P_strPRDTP.trim()+"'";
		//	M_strSQLQRY +=" AND LT_LOTNO like '"+P_strLOTNO.substring(0,4)+"%'";
			M_strSQLQRY ="Select (CMT_CODCD + CMT_CCSVL) L_LOTNO from CO_CDTRN WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cGSTP ='PRXXLOT'";
			M_strSQLQRY +=" AND CMT_CODCD = '"+P_strLOTNO.substring(0,4)+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY ); 
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					setMSG("",'N');
					strLSTNO = M_rstRSSET.getString("L_LOTNO");
					if(strLSTNO !=null)
						strLSTNO = strLSTNO.trim();
					else
					{
						if(P_strLOTNO.substring(4).equals("0001"))
					    {
							strCURNO = P_strLOTNO.trim();
							setMSG("New Lot Series,Enter the lot number as "+strLOTNO.substring(0,4)+"0001",'N');
					        this.setCursor(cl_dat.M_curDFSTS_pbst);
						    flgFSTLT = true;
						     if(flgSRVLD)
						        return true;
						}
						else
						{
							setMSG("New Lot Series,Enter the lot number as "+strLOTNO.substring(0,4)+"0001",'N');
                          	this.setCursor(cl_dat.M_curDFSTS_pbst);
							//if(flgSRVLD)
							  //  return false;
						}
					}
					if(strLSTNO.trim().length() >0)
					{
						Integer L_TMP = Integer.valueOf(strLSTNO);
						int L_CURNO =L_TMP.intValue()+1;
						String L_TMPNO= String.valueOf(L_CURNO);
						 strCURNO = "";
						 for(int i=0;i<strLSTNO.trim().length()-L_TMPNO.trim().length();i++)
						{
							strCURNO +="0";
						}
						strCURNO += L_TMPNO;
						strCURNO  = strCURNO.trim();
					}
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY ="Select * from PR_LTMST WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO ='"+strLSTNO.trim()+"'";
			M_strSQLQRY +=" AND LT_RCLNO = '"+strDFRCL.trim()+"'";
			M_strSQLQRY +=" AND LT_CLSFL <> '8'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY ); 
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					setMSG("",'N');
					strLINNO = nvlSTRVL(M_rstRSSET.getString("LT_LINNO"),"");
					strSILNO = nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),"");
					strLSTPR = nvlSTRVL(M_rstRSSET.getString("LT_TPRCD"),"");
					L_TMPTM = M_rstRSSET.getTimestamp("LT_PENDT");
					if(L_TMPTM !=null)
						strLSTDT =M_fmtLCDTM.format(L_TMPTM);
					if(strPRDTP.equals("01"))
					{
						if(strLSTDT.trim().length()== 0)
						{
							setMSG("Before entering, please close the provious lot number..",'E');
							this.setCursor(cl_dat.M_curDFSTS_pbst);
							return false;
						}
					}
					txtLINNO.setText(strLINNO);
					txtTPRCD.setText(strLSTPR);
					txtTPRDS.setText(getPRDDS(txtTPRCD.getText().trim()));
				
					if(strLSTDT.trim().length() >=16)
					{
						txtPSTDT.setText(strLSTDT.substring(0,10));
						txtPSTTM.setText(strLSTDT.substring(11,16));
					}
				}
				else
				{
					setMSG("New Lot Series,Enter the lot number as "+strLOTNO.substring(0,4)+"0001",'N');
                   // System.out.println("2");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					//if(flgSRVLD)
					 //   return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getPRVLOT");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		return true;
	}
	private boolean vldLOTSR(String P_strLOTSER)
	{
		if(P_strLOTSER.length() < 8)
		{
			setMSG("Invalid length of lot number..",'E');
		}
		else
		{
			M_strSQLQRY ="select count(*) from CO_CDTRN where ";
			M_strSQLQRY +=" CMT_CGMTP ='SYS' and CMT_CGSTP ='PRXXLIN' and ";
			M_strSQLQRY +=" CMT_CODCD ='"+P_strLOTSER.substring(0,2)+"'";
			M_strSQLQRY +=" and CMT_CCSVL LIKE '%" +strPRDTP +"%'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
			if(cl_dat.getRECCNT( M_strSQLQRY)>0)
			{
				setMSG("",'N');
			}
			else
			{
				setMSG("Given Lot Series contains Invalid Line No.", 'E');
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
		}
		M_strSQLQRY = "Select count(*) from CO_CDTRN where CMT_CGMTP ='SYS' and cmt_cgstp ='PRXXPRD'";
		M_strSQLQRY +=" AND CMT_CODCD = '"+P_strLOTSER.substring(2,3)+"'";
		M_strSQLQRY +=" and CMT_CCSVL like  '%" +strPRDTP +"%'";
		if(cl_dat.getRECCNT( M_strSQLQRY)>0)
		{
				setMSG("",'N');
		}
		else
		{
			setMSG("Given Lot Series contains Invalid Digit of product code", 'E');
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		/*if(!P_strLOTSER.substring(3,4).equals(cl_dat.M_strLOGDT_pbst.substring(9)))
		{
			setMSG("Given Lot Series contains Invalid Year Digit", 'E');
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		else
		{
			setMSG("",'N');
			
		}*/
		return true;
	}
	private boolean vldLINNO()
	{
		if(txtLINNO.getText().trim().length()==0)
		{
			setMSG("Line No can not be Empty ..Enter some valid Line No ", 'E');
			txtLINNO.requestFocus();
			return false;
		}
		else if(!txtLOTNO.getText().trim().substring(0,2).equals(txtLINNO.getText().trim()))
		{
			setMSG("Line no. is not correct as per the Lot series .. ", 'E');
			txtLINNO.requestFocus();
			return false;
		}
		M_strSQLQRY ="Select count(*) from CO_CDTRN where cmt_cgmtp ='SYS' and CMT_CGSTP ='PRXXLIN' and ";
		M_strSQLQRY +=" cmt_codcd ='"+txtLINNO.getText().trim()+"'";
		M_strSQLQRY +=" and CMT_CCSVL like '%" +strPRDTP +"%'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
		if(cl_dat.getRECCNT( M_strSQLQRY)>0)
		{
			return true;
		}
		else
		{
			setMSG("Invalid Line No...Enter some valid Line No ", 'E');
			return false;
			
		}
	}
	private boolean vldSILNO()
	{
		if(txtSILNO.getText().trim().length()==0)
		{
			setMSG("Cylo No can not be Empty ..Enter some valid Cylo No ", 'E');
			return false;
		}	
		M_strSQLQRY ="Select count(*) from CO_CDTRN where cmt_cgmtp ='SYS' and CMT_CGSTP ='PRXXCYL' and ";
		M_strSQLQRY +=" cmt_codcd ='"+txtSILNO.getText().trim()+"'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
		if(flgSLVLD)
		M_strSQLQRY += " AND CMT_CODCD like '"+txtLINNO.getText().trim() + "%'";
		if(cl_dat.getRECCNT( M_strSQLQRY)>0)
			return true;
		else
		{
			setMSG("Invalid Cylo No ..Enter some valid Cylo No ", 'E');
			return false;
		}
	}

private void getLSTRN(String P_strTPRCD,String P_strLINNO,String P_strLOTNO)
{
	try
	{
		ResultSet L_rstRUNNO;
		String L_strYRDGT = cl_dat.M_strLOGDT_pbst.substring(9).trim();
		String L_strYEAR =  "01/01/"+cl_dat.M_strLOGDT_pbst.substring(6).trim();
        M_strSQLQRY = "Select max(LT_RUNNO) L_RUN from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_TPRCD = '" + P_strTPRCD.trim() + "' and LT_CLSFL <> '8' AND length(LT_RUNNO) >0 ";
		M_strSQLQRY += " AND LT_LINNO = '" + P_strLINNO.trim() +"'";
		M_strSQLQRY += " AND LT_LOTNO <> '" + P_strLOTNO.trim() +"'";
		M_strSQLQRY += " AND convert(varchar,LT_PSTDT,1) >= '"+L_strYEAR+"'";
		L_rstRUNNO = cl_dat.exeSQLQRY(M_strSQLQRY );
		if(L_rstRUNNO !=null)
		if(L_rstRUNNO.next())
		{
			strLSTRN = nvlSTRVL(L_rstRUNNO.getString("L_RUN"),"").trim();
			if(strLSTRN.trim().length() == 0)
			{
				strLSTRN =  P_strLINNO + L_strYRDGT + "00000"; 
			}
		}
		else
		{
				   strLSTRN =  P_strLINNO + L_strYRDGT + "00000"; 
		}
	}
	catch(SQLException L_SE)
	{
		setMSG(L_SE,"getLSTRN");
	}
}
private String genRUNNO1()
	{
		String L_strNWRUN = "";
		String L_strTEMP = "";
		String L_strNXTRN ="";
		try
		{	
			Integer L_INT = Integer.valueOf(strLSTRN);
			int L_intINCRN = L_INT.intValue();
			L_intINCRN = L_intINCRN + 1;
			L_strNXTRN = String.valueOf(L_intINCRN);
			for(int i=0;i<8-L_strNXTRN.length();i++)
			{
				L_strTEMP += "0";
			}
			L_strTEMP += L_strNXTRN;
			L_strNWRUN = L_strTEMP;
		}
		catch(NullPointerException L_NE)
		{
            setMSG("Null Pointer Exception",'E');
			return "";
		}
		catch(NumberFormatException L_NFE)
		{
            setMSG("Number Format Exception",'E');
			return "";
		}
		finally
		{
			L_strTEMP = null;
			L_strNXTRN = null;
		}
		return L_strNWRUN;
	}
private void getLOTDTL(String P_strPRDTP,String P_strLOTNO)
{
	String L_strSQLQR ="";
	ResultSet L_rstRSSET;
	Timestamp L_tmsTMP;
	try
	{
		int L_TMP = Integer.valueOf(P_strLOTNO).intValue();
		int L_PRVLOT = L_TMP -1;
		P_strLOTNO = String.valueOf(L_PRVLOT);
		L_strSQLQR ="Select * from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP ='"+P_strPRDTP.trim() +"' AND LT_LOTNO ='"+P_strLOTNO.trim()+"' AND LT_RCLNO ='00'";
		L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQR );	
		if(L_rstRSSET !=null)
			if(L_rstRSSET.next())
			{
				strLSTPR = nvlSTRVL(L_rstRSSET.getString("LT_TPRCD"),"");
				L_tmsTMP =L_rstRSSET.getTimestamp("LT_PENDT");
				if(L_tmsTMP !=null)
					strLSTDT = M_fmtLCDTM.format(L_tmsTMP);
				else strLSTDT ="";
				
			}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getLOTDTL");
	}
	finally
	{
		L_strSQLQR = null;
		L_rstRSSET = null;
	}
}
boolean vldLOTDT()
{
	try
	{
		if((txtLOTNO.getText().trim().length() ==0)||(txtLINNO.getText().trim().length() ==0)||(txtTPRCD.getText().trim().length() ==0)||(txtSILNO.getText().trim().length() ==0))
		{
			setMSG("Lot Number,Target Grade,Lot Start Date ,Line Number and Cylo Number can not be blank..",'E');
			return false;
		}
		else if(!txtLOTNO.getText().trim().substring(0,2).equals(txtLINNO.getText().trim()))
		{
			setMSG("Line no. is not correct as per the Lot series .. ", 'E');
			txtLINNO.requestFocus();
			return false;
		}
		else if(M_fmtLCDTM.parse(getPSTDTM()).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
		{
			setMSG("Start Date and time can not be greater than current time..",'E');
			return false;
		}
		/*else if(Double.valueOf(txtPRDQT.getText().trim()).doubleValue() < 0)
		{
			setMSG("Lot quantity can not be negative.. ",'E');
			txtPRDQT.requestFocus();
			return false;
		}*/
		else if(getPENDTM().trim().length() >0)
		{
			if(M_fmtLCDTM.parse(getPENDTM()).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
			{
			   if(strPRDTP.equals("01"))
			   {
					setMSG("End Date and time can not be greater than current time..",'E');
					txtPENDT.requestFocus();
					return false;
				}
			}
			else if(M_fmtLCDTM.parse(getPENDTM()).compareTo(M_fmtLCDTM.parse(getPSTDTM()))< 0)
			{
				setMSG("End time can not be smaller than start time of lot..",'E');
				txtPENDT.requestFocus();
				return false;
			}
			else if(txtPRDQT.getText().trim().length()==0)
			{
					setMSG("Please enter the quanity produced",'E');
					txtPRDQT.requestFocus();
					return false;
			}
			else if(Double.valueOf(txtPRDQT.getText().trim()).doubleValue() <= 0)
			{
					setMSG("Please enter the Lot quantity before closing the lot ",'E');
					txtPRDQT.requestFocus();
					return false;
			}
			if(chkYES.isSelected())
			{
			    if(txtRMKDS.getText().trim().length() ==0)
			    {
			        setMSG("Please enter the remarks for Abnormality..",'E');
				    txtRMKDS.requestFocus();
				    return false;
				}
			}
			else 
			{
			    if(!chkNO.isSelected())
			    {
			        if((strPRDTP.trim().equals("01"))&&(!txtLINNO.getText().trim().equals("08"))&&(!txtLINNO.getText().trim().equals("28")))
			        {
					    setMSG("Please Enter the plant abnormality (YES/NO)..",'E');
					    return false;
			        }
			    }
			}
		}
		/*else if((chkBAGCL.isSelected())&& (txtIPRDS.getText().length() ==0))
		{
			setMSG("Please enter the Bagging grade ..",'E');
			txtIPRDS.requestFocus();
		}*/
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"vldLOTDT");
		return false;
	}
	return true;	
}
private String getPRDDS(String P_strPRDCD)
{
	ResultSet L_rstPRDTL;
	String L_strTEMP ="";
	try
	{
	M_strSQLQRY ="SELECT PR_PRDDS FROM CO_PRMST WHERE PR_PRDCD ='"+P_strPRDCD+"'";
    L_rstPRDTL = cl_dat.exeSQLQRY(M_strSQLQRY );
	if(L_rstPRDTL !=null)
		if(L_rstPRDTL.next())
		{
			L_strTEMP = nvlSTRVL(L_rstPRDTL.getString("PR_PRDDS"),"");
		}	
	}
	catch(SQLException L_SE)
	{
		setMSG(L_SE,"getPRDDS");
	}
	return L_strTEMP;
}
void exeSAVE()
{
	if(vldLOTDT())
	{
		setMSG("",'N');
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{
			if(!flgENDFL)
			{
				setMSG("Modifying the lot details ..",'N');
				if(exeMODREC())
				{
					JOptionPane L_OPTNPN = new JOptionPane();
					int L_intOPT = L_OPTNPN.showConfirmDialog(null,"Do you want to Generate Bagging Lot Control Form ?", "Confirm",JOptionPane.YES_NO_OPTION);
					if(L_intOPT == 0)
					{
						setENBL(false);
						txtIPRDS.setEnabled(true);
						txtIPRDS.requestFocus();
						setMSG("Please enter the bagging grade..",'N');
					}
					else
					{
						txtIPRDS.setEnabled(false);
						btnBGRPT.setEnabled(false);
						clrCOMP();
					}
				}
			}
			else
				setMSG("Lot has been Closed, can not modify",'E');
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			if(flgLTCLS)
			{
				if(!flgENDFL)  // lot has not been closed
				{
					setMSG("Modifying the lot details ..",'N');
					exeMODREC();
				}
			}
			else
				exeADDREC();
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			if(strCLSFL.equals("0"))
				exeDELREC();
			else
				setMSG("Lot has been taken for further Processing,it can not be deleted",'E');
		}
	}
}
	private void exeWRKSTA()
	{
		flgLTCLS = false;
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			txtRCLNO.setEnabled(true);	
		}
		else
			txtRCLNO.setEnabled(false);
		txtLOTNO.setEnabled(true);
		txtIPRDS.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{
			txtTPRCD.setEnabled(false);
			txtLINNO.setEnabled(false);
			txtPSTDT.setEnabled(false);
			txtPSTTM.setEnabled(false);
		    txtSILNO.setEnabled(true);
			txtPRDQT.setEnabled(true);
			txtPKGWT.setEnabled(true);
		    txtPENDT.setEnabled(false);
		  	txtPENTM.setEnabled(false);
			
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			txtTPRCD.setEnabled(false);
			txtLINNO.setEnabled(false);
			txtSILNO.setEnabled(false);
			txtPSTDT.setEnabled(false);
			txtPSTTM.setEnabled(false);
			txtPENDT.setEnabled(false);
      		txtPENTM.setEnabled(false);
			txtPRDQT.setEnabled(false);
			txtPKGWT.setEnabled(false);
			txtTPRCD.setEnabled(false);
			txtLINNO.setEnabled(false);
			txtPSTDT.setEnabled(false);
			txtPSTTM.setEnabled(false);
		}
	}
	
	/*private void crtREPT()
	{
		boolean L_RECORD = false;
		int   L_SPCCNT =7;
		String L_strBAGLC ="",L_strSILDS="";
		DataOutputStream O_DOUT;
		FileOutputStream O_FOUT;
		try
		{
			setMSG("Report Creation in Progress .. please wait",'N');
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			File file = new File("c:\\reports\\pr_rplcf.doc");
			O_FOUT = new FileOutputStream(file);
        	O_DOUT = new DataOutputStream(O_FOUT);	
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strCPI12);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"",74)+cl_dat.getPRMCOD("CMT_CODDS","ISO","PRXXLCF","DOC1"));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"",74)+cl_dat.getPRMCOD("CMT_CODDS","ISO","PRXXLCF","DOC2"));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',"",74)+cl_dat.getPRMCOD("CMT_CODDS","ISO","PRXXLCF","DOC3"));
			O_DOUT.writeBytes("\n");
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strBOLD);
			O_DOUT.writeBytes(padSTRING('L'," ",10));
			O_DOUT.writeBytes(padSTRING('C',"SUPREME PETROCHEM LIMITED ",70));
			O_DOUT.writeBytes("\n\n");
			O_DOUT.writeBytes(padSTRING('L'," ",6));
			prnFMTCHR(O_DOUT,M_strCPI12);
			prnFMTCHR(O_DOUT,M_strENH);
			O_DOUT.writeBytes(padSTRING('C',"Bagging Lot Control Form",45));
			prnFMTCHR(O_DOUT,M_strNOENH);
			O_DOUT.writeBytes("\n");
			prnFMTCHR(O_DOUT,M_strNOBOLD);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("Date :"+cl_dat.M_txtCLKDT_pbst.getText());
			O_DOUT.writeBytes(padSTRING('L',"|",45));
			O_DOUT.writeBytes("To be filled and signed");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("Time of information to start bagging  :  ");
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_txtCLKTM_pbst.getText() + " Hrs.  ",19)+"|");
			O_DOUT.writeBytes("by Shift Officer");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(60,O_DOUT);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"BAGGING LOC. ",30)+": ");
			M_strSQLQRY = "SELECT CMT_CODDS,CMT_CHP02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp = 'PRXXCYL'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY +=" AND CMT_CODCD = '" +txtSILNO.getText().trim() +"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				L_strSILDS = M_rstRSSET.getString("CMT_CODDS");
				L_strBAGLC = M_rstRSSET.getString("CMT_CHP02");
			}
			O_DOUT.writeBytes(padSTRING('R',L_strBAGLC,28)+"|");
			O_DOUT.writeBytes(" ENTERED BY : "+cl_dat.M_strUSRCD_pbst);
			O_DOUT.writeBytes("\n");
				
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"SILO NO. ",30)+": ");
			O_DOUT.writeBytes(padSTRING('R',L_strSILDS.trim(),28)+"|");
			O_DOUT.writeBytes(" NAME: ");
			O_DOUT.writeBytes(getUSRNM(cl_dat.M_strUSRCD_pbst));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"LOT NO.",30)+": ");
			prnFMTCHR(O_DOUT,M_strBOLD);
			O_DOUT.writeBytes(padSTRING('R',txtLOTNO.getText().trim(),28));
			prnFMTCHR(O_DOUT,M_strNOBOLD);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"TARGET GRADE",30)+": ");
			O_DOUT.writeBytes(padSTRING('R',txtTPRDS.getText().trim(),28)+"|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"PROV. GRADE(For Bagging) ",30)+": ");
			prnFMTCHR(O_DOUT,M_strBOLD);
			O_DOUT.writeBytes(padSTRING('R',txtIPRDS.getText().trim(),28));
			prnFMTCHR(O_DOUT,M_strNOBOLD);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"APPROX QTY. (MT)",30)+": ");
			O_DOUT.writeBytes(padSTRING('R',txtPRDQT.getText().trim(),28)+"|");
			O_DOUT.writeBytes(" SIGN:");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("Checklist to be checked and filled in by Executive /T.M. ");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 1.   Right silo connected",50)+"Y/N");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes(padSTRING('L',"Signature",15));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 2.   Correct lot no. is being printed / labelled",50)+"Y/N");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 3.   Correct grade is being printed / labelled",50)+"Y/N");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 4.   Metal detection test done ",50)+"Y/N/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",5));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 5.   Metal detector reset",50)+"Y/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",7));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+5));
			O_DOUT.writeBytes(padSTRING('R',"(Incase of grade change)",50));
			O_DOUT.writeBytes(padSTRING('L',"|",6));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 6.   Stitching is O.K. ",50)+"Y/N/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",5));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 7.   Quantity bagged",42)+"________MT ");
			O_DOUT.writeBytes(padSTRING('L',"|",8));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R'," 8.   Magnetic seperator cleant ",50)+"Y/N/NA");
			O_DOUT.writeBytes(padSTRING('L',"|",5));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+5));
			O_DOUT.writeBytes(padSTRING('R',"(Incase of jumbo bagging machine)",50));
			O_DOUT.writeBytes(padSTRING('L',"|",6));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(88,O_DOUT);
			O_DOUT.writeBytes("\n");
		
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|9.  Initial",14)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			 O_DOUT.writeBytes(padSTRING('R',"10. Final ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   Sign.",8)+"|");
			O_DOUT.writeBytes(padSTRING('L',"|",18));
				
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Reading",14)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L'," ",4));
			O_DOUT.writeBytes(padSTRING('R',"Reading ",11)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   MHD",8)+"|");
			O_DOUT.writeBytes(padSTRING('L',"|",18));
				
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			crtLINE(13,O_DOUT);
			O_DOUT.writeBytes("|");
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(8,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(17,O_DOUT);
			O_DOUT.writeBytes("|");
				
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|   Total Bags",14)+"|");
			O_DOUT.writeBytes(padSTRING('L',"Nos.",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   Diff. ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L',"Nos.",15)+"|");
			O_DOUT.writeBytes(padSTRING('R',"   Qty.",8)+"|");
			O_DOUT.writeBytes(padSTRING('L',"   MT",15));
			O_DOUT.writeBytes("  |");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(88,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"11.   Sample taken by QCA",55)+"Y/N");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"12.   Silo completely emptied",55)+"Y/N");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"13.   Time of completion of bagging",77)+"________ Hrs.");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"14.   Difference between actual and estimated qty.",77)+"________ MT");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"15.   Details of packing items used",60));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(89,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Item",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Vendor ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Lot/GRIN No. ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Type ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," Quantity ",15));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"Av. wt. of|",11));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"|",15));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('L',"|",16));
			O_DOUT.writeBytes(padSTRING('R',"empty bag |",10));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			crtLINE(14,O_DOUT);
			O_DOUT.writeBytes("|");
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(10,O_DOUT);
			O_DOUT.writeBytes("|");

			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Bags",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L',"   Nos.",15));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"gms|",11));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"|",15));
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(42,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L',"   Nos.",15));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"gms|",11));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			crtLINE(14,O_DOUT);
			O_DOUT.writeBytes("|");
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(42,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|    Thread ",15)+"|");
			O_DOUT.writeBytes(padSTRING('L'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");

			O_DOUT.writeBytes("\n");
				
				
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes(padSTRING('L',"|",15));
				crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			crtLINE(15,O_DOUT);
			O_DOUT.writeBytes("|");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"|",15)+"|");
			O_DOUT.writeBytes(padSTRING('L'," ",15)+"|");
			O_DOUT.writeBytes(padSTRING('R'," ",15)+"|");

			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT+1));
			crtLINE(46,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"16.   G.W. of filled bags checked( Only for 25 Kg. packing ):- ",60));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
		//	O_DOUT.writeBytes(padr("      1)........Kgs. 2)........Kgs.3)........Kgs. 4)........Kgs. 5).......Kgs.",60));
			O_DOUT.writeBytes(padSTRING('R',"      1)........Kgs. 2)........Kgs.3)........Kgs. 4)........Kgs. 5).......Kgs.",80));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"17.   Any abnormality observed:-",60));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
				
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes(padSTRING('R',"T.M./Exe. - MHD ",37));
			O_DOUT.writeBytes(padSTRING('R',"Exe. - MHD ",37));
			O_DOUT.writeBytes(padSTRING('R',"Sr.Manager - MHD",30));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			crtLINE(90,O_DOUT);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L'," ",L_SPCCNT));
			O_DOUT.writeBytes("cc : Control Room");
			prnFMTCHR(O_DOUT,M_strEJT);
			O_DOUT.close();
			O_FOUT.close();
			M_strSQLQRY = "Update PR_LTMST set ";
			if(txtIPRDS.getText().trim().length() >15)
				M_strSQLQRY += "LT_IPRDS = '" + txtIPRDS.getText().trim().substring(0,15) + "',";
			else
				M_strSQLQRY += "LT_IPRDS = '" + txtIPRDS.getText().trim()+ "',";
			M_strSQLQRY += "LT_TRNFL = '0',";
			M_strSQLQRY += "LT_LUSBY = '" + cl_dat.M_strUSRCD_pbst + "',";
			M_strSQLQRY += "LT_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";		
			M_strSQLQRY += " where LT_PRDTP = '" + strPRDTP + "'";
			M_strSQLQRY += " and LT_LOTNO = '" + strLOTNO + "'";
			M_strSQLQRY += " and LT_RCLNO = '" + strRCLNO + "'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Report has been created",'N');
				// printing 
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("co_repprn.bat "+"c:\\reports\\pr_rplcf.doc"); 
			}
			else
			{
			
				setMSG("Report falied at saving the bagging grade..",'E');
			}
			
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		  }catch(Exception L_IO)
			{
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				System.out.println(L_IO.toString());
			}	
	}*/
	/*private void crtLINE(int LM_CNT,DataOutputStream P_DOUT)
	{
		String strLINE = "";
		try
		{
			for(int i=1;i<=LM_CNT;i++)
			{
				 strLINE += "-";
			}
		P_DOUT.writeBytes(strLINE);
		}
		catch(Exception L_EX){
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}*/
/*
private String getUSRNM(String P_strUSRCD)
{
	String L_strUSRNM = "";	
	try
	{
		M_strSQLQRY = "Select US_USRNM,current_time L_TIME from SA_USMST where US_USRCD = "+"'"+P_strUSRCD +"'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				L_strUSRNM = nvlSTRVL(M_rstRSSET.getString("US_USRNM"),"");
			}
	}
	catch(SQLException L_SE)
	{
		System.out.println(L_SE.toString());
	}
	return L_strUSRNM;
}*/
private String getSPRNNO()
{
	try
	{
		M_strSQLQRY = "Select max(RN_RUNNO)L_RUNNO from PR_RNMST where RN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RN_GRDCD = "+"'"+txtTPRCD.getText().trim() +"'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				return nvlSTRVL(M_rstRSSET.getString("L_RUNNO"),"");
			}
	}
	catch(SQLException L_SE)
	{
		System.out.println(L_SE.toString());
		return "";
	}
	return "";
}

}