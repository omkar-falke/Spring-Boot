// GRIN Updation
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Number;
import java.lang.Float;
import java.text.SimpleDateFormat;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.sql.CallableStatement;
//import java.util.Date;

class mm_temdv extends cl_pbase{
	private JTextField txtEDITR;			// Table Cell Editor
	private JTextField txtGRNNO,txtGRNDT,txtAMDNO,txtAMDDT,txtACPDT,txtPORNO,txtVENCD,txtVENNM;
	private JTextField txtGINNO,txtCHLNO,txtCHLDT,txtEXCCT,txtPRDCD;
	private JTextField txtACPQT;
	private String strGRNNO,strGRNDT,strPORNO;
	private String strVENCD,strVENNM,strSBSCD,strSTRTP,strMATCD,strACPDT;
	private String strUOMCD,strCHLNO,strCHLDT,strCHLQT,strACPQT,strAMDNO,strAMDDT;
	private String strGINNO,LM_LUSBY,LM_LUPDT,strGINTP,strDELFL,strQLTFL,strTEMP,strSRLNO;
	private int intTBLRW,intTBLCL,intROWCT,intOLDRW,intINDCT;
	private Hashtable<String,String> hstMATCD = new Hashtable<String,String>();
	private Hashtable<String,String> hstITMDT = new Hashtable<String,String>();
	private Hashtable<String,String> hstMAXVL = new Hashtable<String,String>();
	private ResultSet rstRSSET ;
	private float fltPORVL =0.0f;
	inpVRFY objINPVR = new inpVRFY();	
	private cl_JTable tblGINVL;
	private CallableStatement cstMDIPR;			// mODVAT iSSUE PROCESSING
	final int TBL_CHKFL = 0;
	final int TBL_MATCD = 1;
	final int TBL_UOMCD = 2;
	final int TBL_MATDS = 3;
	final int TBL_ACPQT = 4;
	final int TBL_EXCCT = 5;
	final int TBL_EXPRD = 6;
	final int TBL_CHPNO = 7;
	final int TBL_EXCVL = 8;
	final int TBL_MODFL = 9;
	final int TBL_MODPR = 10;
	final int TBL_MODVL = 11;

	
	final String strPORGR_fn ="01";					// PO GRIN	
	final String strTRNFL_fn = "0";		// Transfer flag
//	final String strDFLBT_fn ="COMMON";			// default bach no  
	private String strYSTDT ="";
	
	mm_temdv()
	{
		super(2);
		try
		{	
			setMatrix(20,8);
			
			add(new JLabel("GRIN No."),2,1,1,1,this,'L');
			add(txtGRNNO = new TxtLimit(8),2,2,1,1,this,'L');
			
			add(new JLabel("GRIN Date"),2,3,1,1,this,'L');
			add(txtGRNDT = new TxtDate(),2,4,1,1,this,'L');
			
			add(new JLabel("Accepted Date"),2,5,1,1,this,'L');
			add(txtACPDT = new TxtDate(),2,6,1,1,this,'L');
			
			add(new JLabel("Gate In No."),2,7,1,1,this,'L');
			add(txtGINNO = new TxtLimit(8),2,8,1,1,this,'L');
		
			add(new JLabel("Amd. No."),3,1,1,1,this,'L');
			add(txtAMDNO = new TxtLimit(2),3,2,1,1,this,'L');
			
			add(new JLabel("Amd. Date"),3,3,1,1,this,'L');
			add(txtAMDDT = new TxtDate(),3,4,1,1,this,'L');
			
			add(new JLabel("Challan No."),3,5,1,1,this,'L');
			add(txtCHLNO = new TxtLimit(10),3,6,1,1,this,'L');
		
			add(new JLabel("Challan Date"),3,7,1,1,this,'L');
			add(txtCHLDT = new TxtDate(),3,8,1,1,this,'L');
		
			add(new JLabel("P.O. No."),4,1,1,1,this,'L');
			add(txtPORNO = new TxtLimit(8),4,2,1,1,this,'L');
		
			add(new JLabel("Supplier"),4,3,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),4,4,1,1,this,'L');
			add(txtVENNM = new TxtLimit(5),4,5,1,4,this,'L');
			txtVENNM.setEnabled(false);
		
			
			String[] L_COLHD = {"","Material","UOM","Material Description","Acp.Qty.","Cat.","Prd.Code","Chp. No.","Excise ","Flag","Mod. %","Modvat Amt."};
			int[] L_COLSZ = {20,75,40,180,80,40,60,60,60,20,60,50};
			intROWCT = 150;
			tblGINVL = crtTBLPNL1(this,L_COLHD,intROWCT,6,1,10,8,L_COLSZ,new int[]{0,9});
			txtEDITR = (JTextField)tblGINVL.getCellEditor(1,1).getTableCellEditorComponent(tblGINVL,"",false,1,1);
			tblGINVL.clrTABLE();
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			strGINTP = "02";
			cstMDIPR = cl_dat.M_conSPDBA_pbst.prepareCall("{call mm_mdprc(?,?,?)}");			
			M_strSQLQRY = "Select CMT_CHP01 from CO_CDTRN where CMT_CGMTP ='SYS' and cmt_cgstp ='MMXXYRD' "+
						  " AND CMT_CODCD ='01' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
					strYSTDT = M_rstRSSET.getString("CMT_CHP01");
				M_rstRSSET.close();
			}
            System.out.println(strYSTDT);
			
		}catch(Exception E){
			setMSG(E,"constructor");
		}	
		setENBL(false);
		txtGRNNO.setEnabled(false);
		txtEXCCT = new TxtLimit(2);
		txtEXCCT.addFocusListener(this);
		txtEXCCT.addKeyListener(this);
		
		txtPRDCD = new TxtLimit(10);
		txtPRDCD.addFocusListener(this);
		txtPRDCD.addKeyListener(this);
		tblGINVL.setCellEditor(TBL_EXCCT,txtEXCCT);
		tblGINVL.setCellEditor(TBL_EXPRD,txtPRDCD);
		setMSG("Select an Option..",'N');
		cl_dat.M_flgHELPFL_pbst = false;
		cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		tblGINVL.setInputVerifier(new mm_tegraTBLINVFR());		
	
	}	
	void setENBL(boolean P_FLAG)
	{
		super.setENBL(P_FLAG);
		if(tblGINVL.isEditing())
			tblGINVL.getCellEditor().stopCellEditing();
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			txtGRNNO.setEnabled(true);
		else
		txtGRNNO.setEnabled(P_FLAG);
		txtCHLNO.setEnabled(false);
		txtCHLDT.setEnabled(false);
	    txtGRNDT.setEnabled(false);
		txtACPDT.setEnabled(false);
		txtAMDNO.setEnabled(false);
		txtAMDDT.setEnabled(false);
		txtGINNO.setEnabled(false);
		txtVENCD.setEnabled(false);
		txtVENNM.setEnabled(false);
		txtPORNO.setEnabled(false);
		tblGINVL.cmpEDITR[TBL_MATCD].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_MATDS].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_ACPQT].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_EXCVL].setEnabled(false);
		}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				strSBSCD = M_strSBSCD;
				strSTRTP = M_strSBSCD.substring(2,4);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					setENBL(true);
					txtACPDT.setText(cl_dat.M_strLOGDT_pbst);
					txtGRNNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst)){
					setENBL(true);
					txtGRNNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst)){
				setENBL(false);
				txtGRNNO.setEnabled(true);	
				}
				else{
					setMSG("Select an option",'N');
					setENBL(false);
					txtGRNNO.setEnabled(false);
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"ActionPerformed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			setMSG("",'N');
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{	
				if(L_KE.getSource().equals(txtGRNNO))		// GRIN No
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtGRNNO";
					strSTRTP = M_strSBSCD.substring(2,4);
					String L_ARRHDR[] = {"GRIN No.","GRIN Date","Modvat Tag"};
					M_strSQLQRY = "Select distinct GR_GRNNO,GR_ACPDT,GR_MODFL from MM_GRMST";
					M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						M_strSQLQRY += " and gr_acpdt is not null ";
					if(txtGRNNO.getText().trim().length() >0)
						M_strSQLQRY += " and gr_grnno like '"+txtGRNNO.getText().trim() +"%'";
					M_strSQLQRY += " and isnull(GR_STSFL,' ') <>'X' order by GR_GRNNO desc";
					System.out.println(M_strSQLQRY);
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
				}
				else if(M_objSOURC == txtEXCCT)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtEXCCT";
					String L_ARRHDR[] = {"Code","Material Type"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
									
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtPRDCD)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPRDCD";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "Select EX_PRDCD,EX_PRDDS from MM_EXPRD";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(L_KE.getSource().equals(txtGRNNO))
				{			
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					strGRNNO = txtGRNNO.getText().trim();
					clrCOMP();
					if(exeGETREC(strSTRTP,strGRNNO)){
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))	
						setENBL(false);
					else setENBL(true);
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(L_KE.getSource().equals(txtGRNDT))
				{			
						strGRNDT = txtGRNDT.getText().trim();
					if(M_fmtLCDAT.parse(strGRNDT).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
							setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
							
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"keyPressed");
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtGRNNO")){				// GRIN No
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				//strSTRTP = String.valueOf(cmbSTRTP.getSelectedItem()).trim().substring(0,2);
				clrCOMP();
				txtGRNNO.setText(cl_dat.M_strHLPSTR_pbst);
				if(exeGETREC(strSTRTP,cl_dat.M_strHLPSTR_pbst)){
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))	
					setENBL(false);
				else setENBL(true);
					
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_strHLPFLD.equals("txtEXCCT")){			// Excise Material Category
				txtEXCCT.setText(cl_dat.M_strHLPSTR_pbst);
			
			}
			else if(M_strHLPFLD.equals("txtPRDCD")){
				txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
			
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"exeHLPOK");
		}
	}
	
	// Method to check whether all necessary data has been entered
	boolean vldDATA()
	{
		try
		{
			if(tblGINVL.isEditing())
				tblGINVL.getCellEditor().stopCellEditing();
			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(tblGINVL.getValueAt(i,TBL_MODFL).toString().equals("true"))
					{
						if(tblGINVL.getValueAt(i,TBL_EXPRD).toString().length() == 0)
						{
							setMSG("Enter the Excise product code..",'E');
							return false;
						}
						else if(tblGINVL.getValueAt(i,TBL_CHPNO).toString().length() == 0)
						{
							setMSG("Enter the Chapter No..",'E');
							return false;
						}
						else if(tblGINVL.getValueAt(i,TBL_MODVL).toString().length() == 0)
						{
							setMSG("Enter the Modvat amount..",'E');
							return false;
						}
						else if(Double.parseDouble(tblGINVL.getValueAt(i,TBL_MODVL).toString()) == 0)
						{
							setMSG("Enter the Modvat amount..",'E');
							return false;
						}
						else
						{
							String L_strEXCVL = tblGINVL.getValueAt(i,TBL_EXCVL).toString();
							strTEMP = tblGINVL.getValueAt(i,TBL_MODVL).toString();
							if(strTEMP.length()>0)
							{
								// Check released temporarily API HBP
								/*if(Double.parseDouble(L_strEXCVL) >0)
								{
									if( Double.parseDouble(strTEMP) > Double.parseDouble(L_strEXCVL))
									{
										setMSG("Modvat amount can not exceed the Excise Value..",'E');
										return false;
									}
								}
								else
								{*/
									if(Double.parseDouble(strTEMP) > Double.parseDouble(hstMAXVL.get(tblGINVL.getValueAt(i,TBL_MATCD).toString()).toString()))
									{
										setMSG("Modvat amount can not exceed the Item Value..",'E');
										return false;
									}
								//}
							} 
						}
					}
				}
			}
			//return chkTBLDATA();
		}
		catch(Exception e){
			setMSG(e,"vldDATA ");
			return false;
		}
		return true;
	}
	private boolean exeGETREC(String LP_STRTP,String LP_GRNNO)
	{
		boolean L_FIRST = true;
		java.sql.Date L_datTMPDT;
		String L_strBILVL ="";
		String L_strPORVL ="";
		try
		{
			System.out.println("exeGETREC");
			String L_strMATCD;
			int i = 0;
			if(hstITMDT !=null)
				hstITMDT.clear();
			if(tblGINVL.isEditing())
				tblGINVL.getCellEditor().stopCellEditing();
			tblGINVL.setColumnSelectionInterval(0,0);
			tblGINVL.setRowSelectionInterval(0,0);
		//	tblGINVL.select(0,0);
			M_strSQLQRY = "Select GR_GRNNO,GR_AMDNO,gr_porno,gr_acpdt,GR_GRNDT,GR_AMDDT,GR_PORNO,GR_VENCD,GR_VENNM,";
			M_strSQLQRY += " GR_GINNO,GR_CHLNO,GR_CHLDT,GR_MATCD,ST_UOMCD,ST_MATDS";
			M_strSQLQRY += " ,GR_ACPQT,GR_EXCVL ,GR_MODVL,GR_MODFL,GR_PORVL,GR_BILVL,GR_EXCCT ";
			M_strSQLQRY += " ,GR_EXPRD,GR_CHPNO,GR_EXCCT";
			M_strSQLQRY += " from MM_GRMST,MM_STMST where GR_STRTP = ST_STRTP AND GR_MATCD =  ST_MATCD AND GR_CMPCD = ST_CMPCD ";
			M_strSQLQRY += " and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + LP_STRTP + "'";
			M_strSQLQRY += " and GR_GRNNO = '" + LP_GRNNO + "'";
//			M_strSQLQRY += " and GR_BATNO = '" + strDFLBT_fn + "'";
			M_strSQLQRY += " and isnull(GR_STSFL,'') <> 'X'";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			while(M_rstRSSET.next())
			{
				if(L_FIRST){
					txtGRNNO.setText(LP_GRNNO);
					L_datTMPDT = M_rstRSSET.getDate("GR_ACPDT");
					
					if(L_datTMPDT !=null)
					{
						txtACPDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						
					}
					else
					{
						txtACPDT.setText("");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						{
							setMSG("GRIN has not been Accepted,Modvat tagging not allowed..",'E');
							return false;
						}
                      	txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_AMDNO"),""));
						L_datTMPDT = M_rstRSSET.getDate("GR_AMDDT");
						if(L_datTMPDT !=null)
							txtAMDDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						else
							txtAMDDT.setText("");
					}
                    if(M_fmtLCDAT.parse(strYSTDT).compareTo(M_fmtLCDAT.parse(M_fmtLCDAT.format(L_datTMPDT)))>0)
    				{
    					// Year start date is greater than GRIN acceptance date, prv year GRIN
    					setMSG("GRIN is Accepted in previous year, Modvat Tagging is  not allowed..",'E');
    					return false;
    				}
					
					// Commented on 02/01/2006 on test basis.
					
		            /*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
					{
						if(M_rstRSSET.getDouble("GR_BILVL") > 0)
                        {                     
                            setMSG("Bill is Passed,Modvat tagging not allowed..",'E');
						    return false;
                        }
					}*/
					L_datTMPDT = M_rstRSSET.getDate("GR_GRNDT");
					if(L_datTMPDT !=null)
						txtGRNDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					else
						txtGRNDT.setText("");
					txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""));
					txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""));
					txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""));
					txtGINNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),""));
					txtCHLNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),""));
					
					L_datTMPDT = M_rstRSSET.getDate("GR_CHLDT");
					if(L_datTMPDT !=null)
						txtCHLDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					else
						txtCHLDT.setText("");
					L_FIRST = false;
				}
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
				tblGINVL.setValueAt(L_strMATCD,i,TBL_MATCD);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),i,TBL_UOMCD);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),i,TBL_MATDS);
				strACPQT = nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0");
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_EXCCT"),""),i,TBL_EXCCT);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_EXCVL"),"0"),i,TBL_EXCVL);
				
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0"),i,TBL_MODVL);
				L_strBILVL = nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0");
				L_strPORVL = nvlSTRVL(M_rstRSSET.getString("GR_PORVL"),"0");
				if(Double.parseDouble(L_strBILVL) >0)
					hstMAXVL.put(L_strMATCD,L_strBILVL);
				else
					hstMAXVL.put(L_strMATCD,L_strPORVL);
				if(nvlSTRVL(M_rstRSSET.getString("GR_MODFL"),"").toString().equals("Y"))
					tblGINVL.setValueAt(Boolean.TRUE,i,TBL_MODFL);
				else
					tblGINVL.setValueAt(Boolean.FALSE,i,TBL_MODFL);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_CHPNO"),""),i,TBL_CHPNO);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_EXPRD"),""),i,TBL_EXPRD);
				hstITMDT.put(L_strMATCD,strACPQT);
				tblGINVL.setValueAt(strACPQT,i,TBL_ACPQT);
				tblGINVL.setValueAt(Boolean.TRUE,i,TBL_CHKFL);
				i++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		/*	M_strSQLQRY ="SELECT RM_REMDS from MM_RMMST where RM_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY +=" AND RM_STRTP ='"+strSTRTP+"'";
			M_strSQLQRY +=" AND RM_DOCTP  ='GR'";
			M_strSQLQRY +=" AND RM_TRNTP  ='ACP'";
			M_strSQLQRY +=" AND RM_DOCNO  ='"+txtGRNNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			System.out.println("7");
			if(M_rstRSSET !=null)
				if(M_rstRSSET.next())
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));*/
		}catch(Exception e){
			setMSG(e,"exeGETREC");
		}
		if(L_FIRST)
			setMSG("Record could not be found..",'E');
		else
			setMSG("",'E');
		return !L_FIRST;
	}
	void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(!vldDATA())
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			for(int i=0;i<intROWCT;i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					strMATCD = String.valueOf(tblGINVL.getValueAt(i,TBL_MATCD));
					strGRNNO = txtGRNNO.getText().trim();
					M_strSQLQRY = "Update MM_GRMST set ";
						if(tblGINVL.getValueAt(i,TBL_MODFL).toString().equals("true"))
						{
							M_strSQLQRY += "GR_MODFL = 'Y',";
						    M_strSQLQRY += "GR_CHPNO = '" + tblGINVL.getValueAt(i,TBL_CHPNO).toString() + "',";
							M_strSQLQRY += "GR_EXPRD = '" + tblGINVL.getValueAt(i,TBL_EXPRD).toString() + "',";
							M_strSQLQRY += "GR_MODVL = " + tblGINVL.getValueAt(i,TBL_MODVL).toString() + ",";
						}
						else
						{
							M_strSQLQRY += "GR_MODFL = 'N',GR_CHPNO ='',GR_EXPRD ='',GR_MODVL =0,";
							
						}
						M_strSQLQRY += getUSGDTL("GR",'U',null);
						M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
						M_strSQLQRY += " and GR_GRNNO = '" + strGRNNO + "'";
						M_strSQLQRY += " and GR_MATCD = '" + strMATCD + "'";
//						M_strSQLQRY += " and GR_BATNO = '" + strDFLBT_fn + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Data Saved successfully..",'N');
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					cstMDIPR.setString(1,cl_dat.M_strCMPCD_pbst);
					cstMDIPR.setString(2,M_strSBSCD.substring(2,4));
					cstMDIPR.setString(3,strGRNNO);
					cstMDIPR.executeUpdate();
					clrCOMP();
				}
				else
				{
					setMSG("Error in Modvat processing..",'E');
				}
				
			}
			else
			{
				setMSG("Error in saving Data ..",'E');
			}
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	// Method to return Material Description
	private String getMATDS(String LP_MATCD){
		String L_MATDS = "";
		try{
			M_strSQLQRY = "Select ST_MATDS from MM_STMST";
			M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = '" + LP_MATCD + "'";
			M_strSQLQRY += " AND ST_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET.next())
				L_MATDS = M_rstRSSET.getString("ST_MATDS");
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"getMATDS");
		}	
		return L_MATDS;
	}
	
class inpVRFY extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
			{	
				return true;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"verify");
		}
	return true;	
	}
}
private class mm_tegraTBLINVFR extends TableInputVerifier
{
	public boolean verify(int P_intROWID,int P_intCOLID)
	{
		try
		{
			if(P_intCOLID==TBL_MODFL)
			{
				if(tblGINVL.getValueAt(P_intROWID,TBL_MODFL).toString().equals("false"))
					tblGINVL.setValueAt("0.00",P_intROWID,TBL_MODVL);
			}
			if(P_intCOLID==TBL_MODPR)
			{
				strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_MODPR).toString();
				if(strTEMP.length() >0)
				{
					if(Double.parseDouble(strTEMP) > 100)
					{
						setMSG("Modvat % can not be greater than 100 %..",'E');
						return false;
					}
					else
					{
						if(tblGINVL.getValueAt(P_intROWID,TBL_EXCVL).toString().length() >0)
						{
							double L_dblMODVL =  Double.parseDouble(tblGINVL.getValueAt(P_intROWID,TBL_EXCVL).toString()) *Double.parseDouble(tblGINVL.getValueAt(P_intROWID,TBL_MODPR).toString())/100;
							tblGINVL.setValueAt(setNumberFormat(L_dblMODVL,2),P_intROWID,TBL_MODVL);
						}
					}
				}
			}
			if(P_intCOLID==TBL_MODVL)
			{
				String L_strEXCVL = tblGINVL.getValueAt(P_intROWID,TBL_EXCVL).toString();
			    strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_MODVL).toString();
				if(strTEMP.length()>0)
				{
					/*if(Double.parseDouble(L_strEXCVL) >0) 
					{
						if( Double.parseDouble(strTEMP) > Double.parseDouble(L_strEXCVL))
						{
							setMSG("Modvat amount can not exceed the Excise Value..",'E');
							return false;
						}
					}
					else
					{*/
						if(Double.parseDouble(strTEMP) > Double.parseDouble(hstMAXVL.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString()))
						{
							setMSG("Modvat amount can not exceed the Item Value..",'E');
							return false;
						}
					//}
				} 
			}
			if(P_intCOLID==TBL_EXPRD)
			{
				M_strSQLQRY = "Select EX_PRDDS from MM_EXPRD";
				M_strSQLQRY += " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EX_PRDCD = '" + tblGINVL.getValueAt(P_intROWID,TBL_EXPRD).toString() + "'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					setMSG(" ",'N');
					return true;
				}
				else
				{
					setMSG("Invalid Product Code..",'E');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"verify");
			return false;
		}
		return true;
	}
}
}	
