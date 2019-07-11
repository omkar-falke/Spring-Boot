/*
System Name   : Marketing System
Program Name  : Lot Detail Analysis
Program Desc. : Gives customer,grade & lot no. details of despatch transaction.
Author        : Mr.S.R.Mehesare
Date          : 02/09/2005
Version       : MKT v2.0.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/
import java.sql.ResultSet;import java.sql.Timestamp;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import javax.swing.JTable;
import javax.swing.JComponent;import javax.swing.InputVerifier;import javax.swing.JButton;
import javax.swing.table.*;import javax.swing.JCheckBox;
import javax.swing.JComponent;import javax.swing.InputVerifier;import java.awt.Color;

public class mr_qrlda extends cl_rbase
{
	private JTextField txtPRDTP;
	private JTextField txtFMDAT;
	private JTextField txtTODAT;
	private JTextField txtSPPRT;
	private JTextField txtPRTDS;
	private JTextField txtSPPRD;
	private JTextField txtSPLOT;		
	private JButton btnFETCH;	
	private JLabel lbl1,lbl2,lbl3,lbl4;	
	private JLabel lblTOTPK;
	private JLabel lblTOTQT;
	private JComboBox cmbPRDTP;
	private JCheckBox chkFTSDP;
	//private JCheckBox chkPRDQT;
	//private JCheckBox chkPRTQT;
		
	private final int intROWCT_fn = 2000;	
	private cl_JTable tblITMDL;		
	
	private FileOutputStream fosREPORT;
	private DataOutputStream dosREPORT;
	
	private String strPRDTP = "";	
	private String strFMDAT;
	private String strTODAT;
	private String strFILNM;
							   
	private String strDOTLN = "---------------------------------------------------------------------------------------------------------";
	private String strHEAD1="";
//	private String strHEAD2="";
	private String strSPBUY;
	private double dblTOTQT;
	private double dblZONQT =0;
	private double dblBYRQT =0;
	private double dblPRDQT =0;
	
	private int intTOTPK;
	
	private String strZONCD="";
	private String strOZONCD="";
	private String strBYRCD="";
	private String strOBYRCD="";
	private String strPRDDS="";
	private String strOPRDDS="";
	private String strINVNO="";
	private String strOINVNO="";
	
	final private int TB_CHKFL = 0;
	final private int TB_INVNO = 1;
    final private int TB_INVDT = 2;
	final private int TB_BYRCD = 3;
    final private int TB_LOTNO = 4;	
	final private int TB_PRDDS = 5;	
	final private int TB_ZONCD = 6;		//zone Description	
	final private int TB_CNSCD = 7; 	// consignee
	final private int TB_INVQT = 8;
    final private int TB_INVPK = 9;
    final private int TB_LADNO = 10;												
		
	private Hashtable<String,String> hstZONCD;
	private Hashtable<String,String> hstPRTDS;
	private INPVF objINPVR = new INPVF();
	boolean flgFIRST= true;
	boolean flgFIRST1= true;
	
	mr_qrlda()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Prod Type"),2,2,1,.9,this,'R');
			add(cmbPRDTP = new JComboBox(),2,3,1,1.2,this,'R');			
			add(chkFTSDP = new JCheckBox("FTS Dispatches Only"),2,5,1,1.5,this,'L');	
				
			add(new JLabel("From Date"),3,2,1,.9,this,'R');
			add(txtFMDAT = new TxtDate(),3,3,1,1.2,this,'R');
			add(new JLabel("To Date"),3,4,1,.8,this,'R');
			add(txtTODAT = new TxtDate(),3,5,1,1.2,this,'L');
																
			add(new JLabel("Customer"),4,2,1,.9,this,'R');
			add(txtSPPRT = new TxtLimit(5),4,3,1,1.2,this,'R');
			add(new JLabel("Description"),4,4,1,.8,this,'R');
			add(txtPRTDS = new JTextField(),4,5,1,3,this,'L');			
																			
			add(new JLabel("Grade"),5,2,1,.9,this,'R');
			add(txtSPPRD = new TxtLimit(10),5,3,1,1.2,this,'R');
			add(new JLabel("Lot No."),5,4,1,.8,this,'R');
			add(txtSPLOT = new TxtNumLimit(8),5,5,1,1.2,this,'L');
											
			add(lbl2 = new JLabel(),6,1,1,6,this,'L');
			lbl2.setForeground(Color.blue);				
			add(btnFETCH = new JButton("Run"),6,8,1,1,this,'L');		
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD' AND isnull(CMT_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{					
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}					
			hstZONCD = new Hashtable<String,String>();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP + CMT_CGSTP = 'SYSMR00ZON'";				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{					
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						hstZONCD.put(L_strQPRCD, nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}					
			String[] L_arrCOLHD = {"","Inv. No.","Inv. Date","Customer","Lot No.","Grade","Zone","Consignee Name","Inv. Qty.","Inv. Pkg.","LA. No."}; 
			int L_arrCOLSZ[] = {10,75,60,130,75,75,50,80,60,60,70};				
			tblITMDL = crtTBLPNL1(this,L_arrCOLHD,intROWCT_fn,7,1,10.9,8,L_arrCOLSZ,new int[]{0});			
			
			//add(chkPRDQT = new JCheckBox("Grade Total"),18,2,1,1,this,'L');	
			//add(chkPRTQT = new JCheckBox("Customer Total"),18,3,1,1,this,'L');	
			
			add(lbl3 = new JLabel("Quantity : "),18,5,1,.7,this,'R');
			add(lblTOTQT = new JLabel("00"),18,6,1,1,this,'R');
			add(lbl4 = new JLabel("Packages : "),18,7,1,.8,this,'R');
			add(lblTOTPK = new JLabel("00"),18,8,1,1,this,'R');										
			lbl3.setForeground(Color.blue);
			lbl4.setForeground(Color.blue);
			lblTOTQT.setForeground(Color.blue);
			lblTOTPK.setForeground(Color.blue);
				
			txtFMDAT.setInputVerifier(objINPVR);
			txtTODAT.setInputVerifier(objINPVR);
			txtSPPRD.setInputVerifier(objINPVR);
			txtSPLOT.setInputVerifier(objINPVR);
			txtSPPRT.setInputVerifier(objINPVR);
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				flgFIRST = false;
				flgFIRST1 = false;
				setMSG("Selcet Product Type from the List..",'N');								
				txtPRTDS.setEnabled(false);
				tblITMDL.setEnabled(false);
				txtFMDAT.requestFocus();
				if(txtFMDAT.getText().trim().length() == 0)
				{					
					try
					{					
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);						
						txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
					}
					catch(Exception L_EX)
					{	       
						setMSG(L_EX, "calDATE");						
					}		
				}
				if(txtTODAT.getText().trim().length() == 0)				
					txtTODAT.setText(cl_dat.M_strLOGDT_pbst);									
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;				
		}
		if(M_objSOURC == btnFETCH)
		{
			/*String L_strTEMP= "";		
			if(L_strTEMP.trim().length()==0)
				L_strTEMP = "Lot Details Between Date "+ txtFMDAT.getText().trim() +" AND "+ txtTODAT.getText().trim();			*/			
			tblITMDL.clrTABLE();
			getDATA("TABLE");
			lbl2.setText("Lot Details for "+strHEAD1);
		}
		else if(M_objSOURC == cmbPRDTP)
		{			
			if(cmbPRDTP.getItemCount()==1)
				return;
			strPRDTP = cmbPRDTP.getSelectedItem().toString().trim().substring(0,2);			
			txtSPPRD.setText("");
			txtSPLOT.setText("");
			txtSPPRT.setText("");
			txtPRTDS.setText("");
			lblTOTQT.setText("00");
			lblTOTPK.setText("00");
			lbl2.setText("");
			if(flgFIRST == false)
				tblITMDL.clrTABLE();
			txtFMDAT.requestFocus();
			setMSG("Enter From Date to view lot details within the given Date Range..",'N');
		}									
		else if(M_objSOURC == txtFMDAT)
		{			
			txtTODAT.requestFocus();
			setMSG("Select Criteria to view lot details ..",'N');
		}
		else if(M_objSOURC == txtTODAT)
		{
			txtSPPRT.requestFocus();
			setMSG("Please Enter Customer Code to view details or Press F1 to select From List..",'N');
		}		
		if(M_objSOURC == txtSPPRT)
		{
			if(txtSPPRT.getText().trim().length()== 0)
				txtPRTDS.setText("");
			if((txtSPPRT.getText().trim().length() != 5) && (txtSPPRT.getText().trim().length() != 0))
			{
				setMSG("Invalid Customer Code, Press F1 to select from list..",'E');
				return;
			}
			txtSPPRD.requestFocus();
			setMSG("Enter Grade, if wants to view corresponding details only..",'N');													
		}								
		if(M_objSOURC == txtSPPRD)
		{
			if((txtSPPRD.getText().trim().length() != 10) && (txtSPPRD.getText().trim().length() != 0))
			{
				setMSG("Invalid Buyer Code, Press F1 to select from list..",'E');
				return;
			}
			txtSPLOT.requestFocus();
			setMSG("Enter Lot No. if wants to view corresponding details only..",'N');			
		}		
		if(M_objSOURC == txtSPLOT)
		{
			if((txtSPLOT.getText().trim().length() != 8) && (txtSPLOT.getText().trim().length() != 0))
			{
				setMSG("Invalid Lot No., Press F1 to select from list..",'E');
				return;
			}
			btnFETCH.requestFocus();
			setMSG("Press Button Run to view details in Table..",'N');
		}		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		if(L_KE.getKeyCode() == KeyEvent.VK_F1)
		{
			try
			{
				strFMDAT ="";strTODAT ="";String L_strDATE ="";
				if((M_objSOURC == txtSPPRT)||(M_objSOURC == txtSPPRD)||(M_objSOURC == txtSPLOT))
				{
					if(txtFMDAT.getText().trim().length()>0)
						strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					else 
						strFMDAT = "";
					if(txtTODAT.getText().trim().length()>0)
						strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
					else 
						strTODAT = "";
					if((strFMDAT.length()>0)&&(strTODAT.length()>0))										
						L_strDATE =" AND CONVERT(varchar,IVT_INVDT,101) between '"+strFMDAT+"' AND '"+strTODAT+"' ";
					else if(strFMDAT.length()>0)
						L_strDATE =" AND CONVERT(varchar,IVT_INVDT,101) >='"+ strFMDAT +"' ";
					else if(strTODAT.length()>0)
						L_strDATE =" AND CONVERT(varchar,IVT_INVDT,101) =<'"+ strTODAT +"' ";
				}
					
				if(M_objSOURC == txtSPPRT)
				{
					if(((strFMDAT.length() ==0 ) || (strTODAT.length() ==0 )) &&(txtSPPRT.getText().trim().length()==0))
					{
						txtPRTDS.setText("");
						setMSG("Please Enter a First Charector of Customer Code..",'E');
						return;						
					}
					if(txtSPPRT.getText().trim().length()>0)
						txtSPPRT.setText(txtSPPRT.getText().trim().toUpperCase());					
					txtPRTDS.setText("");
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPPRT";					
					String L_arrHDR[] = {"Party Code","Party Description"};	
					M_strSQLQRY = "Select distinct IVT_BYRCD,PT_PRTNM from CO_PTMST,MR_IVTRN,FG_ISTRN where"
					+" PT_PRTCD = IVT_BYRCD AND PT_PRTTP='C' AND IVT_CMPCD=IST_CMPCD and IVT_MKTTP = IST_MKTTP AND"
					+" IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO"
					+ L_strDATE
					+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP ='"+ cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'";
					if(chkFTSDP.isSelected())
						M_strSQLQRY += " AND IVT_SALTP = '05'";
					if(txtSPPRD.getText().trim().length() == 10)						
						M_strSQLQRY +=" AND IVT_PRDCD = '"+ txtSPPRD.getText().trim() +"'";
					if(txtSPLOT.getText().trim().length() == 8)
						M_strSQLQRY +=" AND IST_LOTNO ='"+ txtSPLOT.getText().trim() +"'";					
					if(txtSPPRT.getText().trim().length()>0)
						M_strSQLQRY +=" AND IVT_BYRCD like '"+ txtSPPRT.getText().trim() +"%'";
					
					M_strSQLQRY +=" order by IVT_BYRCD";
//					System.out.println("Customer "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");
				}											
				else if(M_objSOURC == txtSPPRD)
				{					
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPPRD";							
					String L_arrHDR[] = {"Product Code","Product Description"};
					M_strSQLQRY = "Select distinct IVT_PRDCD,PR_PRDDS from MR_IVTRN,FG_ISTRN,CO_PRMST where"
					+" IVT_MKTTP = IST_MKTTP AND IVT_CMPCD=IST_CMPCD and IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO"
					+" AND IVT_PRDCD = IST_PRDCD AND IST_PRDCD = PR_PRDCD"+ L_strDATE
					+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP ='"+ cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'";
					if(chkFTSDP.isSelected())
						M_strSQLQRY += " AND IVT_SALTP = '05'";
					if(txtSPPRT.getText().trim().length() == 5)					
						M_strSQLQRY +=" AND IVT_BYRCD = '"+ txtSPPRT.getText().trim() +"'";
					if(txtSPLOT.getText().trim().length() == 8)
						M_strSQLQRY +=" AND IST_LOTNO = '"+ txtSPLOT.getText().trim() +"'";						
					if(txtSPPRD.getText().trim().length() >0)						
						M_strSQLQRY +=" AND IVT_PRDCD like '"+ txtSPPRD.getText().trim() +"%'";									
					M_strSQLQRY +=" order by IVT_PRDCD";
//					System.out.println("PRODUCT "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");																		
				}				
				else if(M_objSOURC == txtSPLOT)
				{										
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPLOT";				
					String L_arrHDR[] = {"Lot No.","Grade"};					
					M_strSQLQRY = "Select distinct IST_LOTNO,PR_PRDDS from MR_IVTRN,FG_ISTRN,CO_PRMST where"
					+" IVT_CMPCD=IST_CMPCD and IVT_MKTTP = IST_MKTTP AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO"
					+" AND IVT_PRDCD = IST_PRDCD AND IST_PRDCD = PR_PRDCD"+ L_strDATE
					+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP ='"+ cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'";
					if(chkFTSDP.isSelected())
						M_strSQLQRY += " AND IVT_SALTP = '05'";
					if(txtSPPRT.getText().trim().length() == 5)					
						M_strSQLQRY +=" AND IVT_BYRCD = '"+ txtSPPRT.getText().trim() +"'";
					if(txtSPPRD.getText().trim().length() == 10)						
						M_strSQLQRY +=" AND IVT_PRDCD = '"+ txtSPPRD.getText().trim() +"'";
					if(txtSPLOT.getText().trim().length()>0)					
						M_strSQLQRY +=" AND IST_LOTNO like '"+ txtSPLOT.getText().trim() +"%'";					
					M_strSQLQRY +=" order by IST_LOTNO";
//					System.out.println("LOT "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_arrHDR,2,"CT");								
				}
			}		
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
		}
	}
	/**
	 * Method to execute the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{					
			if(M_strHLPFLD.equals("txtSPPRT"))
			{
				cl_dat.M_flgHELPFL_pbst = false;				
				txtSPPRT.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtSPPRD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtSPPRD.setText(cl_dat.M_strHLPSTR_pbst);
				strSPBUY = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
			}
			else if(M_strHLPFLD.equals("txtSPLOT"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtSPLOT.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())		
			return;		
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_qrlda.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "mr_qrlda.doc";				
			getDATA("REPORT");
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Lot Details Query"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}	
	}	
	
	void getDATA(String P_strREPORT)
	{
		String L_strTEMP ="",L_strBYRCD="";		
		int L_intCOUNT = 0;
		StringBuffer L_stbDATA = new StringBuffer();
		intTOTPK = 0;
		dblTOTQT = 0;
		double L_dblTOTQT = 0;
		int L_intTOTPK = 0;
		String L_strZONCD ="",L_strBYRDS="",L_strPRDDS="";
		int intPADCH =0;
		ResultSet L_rstRSSET;
		boolean flgBYRCH =  false;	
		String L_strDATE ="";
        java.sql.Date datTEMP;
		try 
		{
			dblZONQT = 0;
			dblPRDQT = 0;
			dblBYRQT = 0;
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(txtFMDAT.getText().trim().length()>0)
				strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			else 
				strFMDAT = "";
			if(txtTODAT.getText().trim().length()>0)
				strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));			
			else 
				strTODAT = "";
			if((strFMDAT.length()>0)&&(strTODAT.length()>0))
			{
				L_strDATE =" AND CONVERT(varchar,IVT_INVDT,101) between '"+strFMDAT+"' AND '"+strTODAT+"' ";
				strHEAD1 = "Invoice Date between "+ txtFMDAT.getText().trim()+" and "+txtTODAT.getText().trim();
			}
			else if(strFMDAT.length()>0)
			{
				L_strDATE =" AND CONVERT(varchar,IVT_INVDT,101) >='"+ strFMDAT +"' ";
				strHEAD1 = "Invoice Date greater than "+ txtFMDAT.getText().trim();
			}
			else if(strTODAT.length()>0)
			{
				L_strDATE =" AND CONVERT(varchar,IVT_INVDT,101) <='"+ strTODAT +"' ";
				strHEAD1 = "Invoice Date less than "+ txtTODAT.getText().trim();
			}
			else			
				L_strDATE ="";								
			
			if(txtSPLOT.getText().trim().length()>0)
				strHEAD1 = txtSPLOT.getText().trim();
			if(txtSPPRD.getText().trim().length()>0)
				strHEAD1 = txtSPPRD.getText().trim();
			if(txtSPPRT.getText().trim().length()>0)
				strHEAD1 = txtPRTDS.getText().trim();
			
			/****************to fetch customer description, consignee description & to put in hastable Dyanamically.*******************/				
			hstPRTDS = new Hashtable<String,String>();
			M_strSQLQRY = "Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST,MR_IVTRN,FG_ISTRN where"
			+" PT_PRTCD = IVT_BYRCD AND PT_PRTTP='C' AND IVT_CMPCD=IST_CMPCD and IVT_MKTTP = IST_MKTTP AND"
			+" IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO"
			+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP ='"+ cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'";
			if(chkFTSDP.isSelected())
				M_strSQLQRY += " AND IVT_SALTP = '05'";
			if(txtSPPRT.getText().trim().length()>0)
				M_strSQLQRY += " AND IVT_BYRCD = '"+txtSPPRT.getText().toString().trim()+"'";			
			M_strSQLQRY += L_strDATE;
			
//			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strTEMP1="";
				while(M_rstRSSET.next())
				{
					L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
					if(!L_strTEMP1.equals(""))
						hstPRTDS.put(L_strTEMP1,nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
				}
				M_rstRSSET.close();
			}					
			/********************************/
					
			/*M_strSQLQRY = "Select IVT_ZONCD,IVT_INVNO,date(IVT_INVDT) datINVDT,IVT_CNSCD,IVT_BYRCD,IST_LOTNO,IVT_PRDDS,";
			M_strSQLQRY +=" SUM(IVT_INVQT) IVT_INVQT,SUM(IVT_INVPK) IVT_INVPK,IVT_LADNO,";				//IVT_CNSCD
			M_strSQLQRY +=" IVT_BYRCD from MR_IVTRN,FG_ISTRN where IVT_MKTTP = IST_MKTTP AND";		
			M_strSQLQRY +=" IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO";
			M_strSQLQRY +=" AND isnull(IST_STSFL,'') <> 'X' AND isnull(IVT_INVNO,'') <>''";*/
			M_strSQLQRY = "Select IVT_ZONCD,IVT_INVNO,CONVERT(varchar,IVT_INVDT,101) datINVDT,IVT_CNSCD,IVT_BYRCD,IST_LOTNO,IVT_PRDDS,"
			+" IVT_LADNO,IVT_BYRCD,SUM(IST_ISSQT) IVT_INVQT,SUM(IST_ISSPK) IVT_INVPK "				//IVT_CNSCD
			+" from MR_IVTRN,FG_ISTRN where IVT_CMPCD=IST_CMPCD and IVT_MKTTP = IST_MKTTP AND"
			+" IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO"
			+" AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IST_STSFL,'') <> 'X' AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_INVNO,'') <>''"
			+" AND IST_PRDTP ='"+ cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'";

			if(chkFTSDP.isSelected())
				M_strSQLQRY += " AND IVT_SALTP = '05'";
			if(txtSPPRD.getText().trim().length() == 10)							
				M_strSQLQRY +=" AND IVT_PRDCD = '"+txtSPPRD.getText().toString().trim()+"'";						 			
			if(txtSPPRT.getText().trim().length()>0)				
				M_strSQLQRY += " AND IVT_BYRCD = '"+txtSPPRT.getText().toString().trim()+"'";						
			if(txtSPLOT.getText().trim().length() == 8)
				M_strSQLQRY +=" AND IST_LOTNO ='"+ txtSPLOT.getText().trim() +"'";
			M_strSQLQRY +=  L_strDATE;
						  
			M_strSQLQRY +=" group by IVT_ZONCD,IVT_CNSCD,IVT_BYRCD,IVT_INVNO,CONVERT(varchar,IVT_INVDT,101),IST_LOTNO,";
			M_strSQLQRY +=" IVT_PRDDS,IVT_LADNO,IVT_BYRCD order by IVT_ZONCD,MR_IVTRN.IVT_BYRCD,IVT_PRDDS,IVT_INVNO";
			
		    //System.out.println("Main SQL "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(tblITMDL.isEditing())
				tblITMDL.getCellEditor().stopCellEditing();
			tblITMDL.setRowSelectionInterval(0,0);
			tblITMDL.setColumnSelectionInterval(0,0);
			
			if(M_rstRSSET != null)
			{						
				if(P_strREPORT.equals("TABLE"))
				{
					dblTOTQT = 0;
					intTOTPK = 0;
					while(M_rstRSSET.next())
					{
						if(L_intCOUNT == intROWCT_fn)
						{
							setMSG("More than 2000 Records Found Please Specify Some Specific Criteria..",'E');
							setCursor(cl_dat.M_curDFSTS_pbst);
							return;
						}
						tblITMDL.setValueAt(new Boolean(false),L_intCOUNT,TB_CHKFL);						
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),L_intCOUNT,TB_INVNO);						
						datTEMP = M_rstRSSET.getDate("datINVDT");
						if(datTEMP !=null)
						    tblITMDL.setValueAt(M_fmtLCDAT.format(datTEMP),L_intCOUNT,TB_INVDT);						
						//tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("datINVDT"),""),L_intCOUNT,TB_INVDT);						
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
						if(hstPRTDS.containsKey(L_strTEMP))
							L_strTEMP = hstPRTDS.get(L_strTEMP).toString();
						tblITMDL.setValueAt(L_strTEMP,L_intCOUNT,TB_BYRCD);
							
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),""),L_intCOUNT,TB_LOTNO);						
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),L_intCOUNT,TB_PRDDS);
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_ZONCD"),"");
						if(hstZONCD.containsKey(L_strTEMP))
							L_strTEMP = hstZONCD.get(L_strTEMP).toString();						
						tblITMDL.setValueAt(L_strTEMP,L_intCOUNT,TB_ZONCD);
							
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
						if(hstPRTDS.containsKey(L_strTEMP))
							L_strTEMP = hstPRTDS.get(L_strTEMP).toString();
						else
						{
							M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTCD = '"+ L_strTEMP.trim() +"' AND PT_PRTTP ='C'";							
							L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);							
							if(L_rstRSSET != null)
							{																
								if(L_rstRSSET.next())								
									L_strTEMP = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");								
							}
							L_rstRSSET.close();
						}
						tblITMDL.setValueAt(L_strTEMP,L_intCOUNT,TB_CNSCD);											
						
						L_dblTOTQT = M_rstRSSET.getDouble("IVT_INVQT");
						dblTOTQT += L_dblTOTQT;						
						tblITMDL.setValueAt(setNumberFormat(L_dblTOTQT,3),L_intCOUNT,TB_INVQT);						
						
						L_intTOTPK = M_rstRSSET.getInt("IVT_INVPK");
						intTOTPK += L_intTOTPK;
						tblITMDL.setValueAt(String.valueOf(L_intTOTPK).toString(),L_intCOUNT,TB_INVPK);
						
						tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),""),L_intCOUNT,TB_LADNO);
						L_intCOUNT ++;
					}
					M_rstRSSET.close();
					lblTOTQT.setText(String.valueOf(setNumberFormat(dblTOTQT,3)).toString());
					lblTOTPK.setText(String.valueOf(intTOTPK).toString());					
				}
				else if(P_strREPORT.equals("REPORT"))
				{
					dblTOTQT = 0;
					intTOTPK = 0;
					intPADCH = 0;
					fosREPORT = new FileOutputStream(strFILNM);
					dosREPORT = new DataOutputStream(fosREPORT);			
					setMSG("Report Generation in Process.......",'N');
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					{
					    prnFMTCHR(dosREPORT,M_strNOCPI17);
					    prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strCPI12);
					}
					if(M_rdbHTML.isSelected())
					{
					    dosREPORT.writeBytes("<HTML><HEAD><Title>Lot Details Query</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");
						dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
						dosREPORT.writeBytes("</STYLE>"); 
					}			
					prnHEADER();
					dblZONQT = 0;
					while(M_rstRSSET.next())
					{	
						L_stbDATA.delete(0,L_stbDATA.length());						
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_ZONCD"),"");						
						if(!strOZONCD.equals(L_strTEMP))	
						{	
							if(hstZONCD.containsKey(strOZONCD))
								L_strZONCD = hstZONCD.get(strOZONCD).toString();
							if(dblZONQT != 0.000)							
							{
								//if(chkPRDQT.isSelected())
								L_stbDATA.append("      "+ padSTRING('R',strOPRDDS,30) + padSTRING('L',"Total ",58) + padSTRING('L',setNumberFormat(dblPRDQT,3).toString(),10)+"\n");
							    L_stbDATA.append("   "+ padSTRING('R',L_strBYRDS,30)+ padSTRING('L',"Total ",61) + padSTRING('L',setNumberFormat(dblBYRQT,3).toString(),10)+"\n");
								L_stbDATA.append(padSTRING('R',L_strZONCD,30)+ padSTRING('L',"Total ",64) + padSTRING('L',setNumberFormat(dblZONQT,3).toString(),10)+"\n");
								cl_dat.M_intLINNO_pbst +=3;								
							}	
							dblZONQT = 0;
							dblPRDQT = 0;
							dblBYRQT = 0;
							if(hstZONCD.containsKey(L_strTEMP))
								L_strZONCD = hstZONCD.get(L_strTEMP).toString();
							L_stbDATA.append(padSTRING('R',L_strZONCD,8)+"\n");
							cl_dat.M_intLINNO_pbst ++;
							strOZONCD = L_strTEMP;											
						}
						else
							intPADCH = 3;
						L_strBYRCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
						if(!strOBYRCD.equals(L_strBYRCD))
						{							
							if(hstPRTDS.containsKey(strOBYRCD))
								L_strBYRDS = hstPRTDS.get(strOBYRCD).toString();							
							if(dblBYRQT != 0.000)							
							{
								//if(chkPRDQT.isSelected())
								L_stbDATA.append("      "+ padSTRING('R',strOPRDDS,30) + padSTRING('L',"Total ",58) + padSTRING('L',setNumberFormat(dblPRDQT,3).toString(),10)+"\n");
							    L_stbDATA.append("   "+ padSTRING('R',L_strBYRDS,30)+ padSTRING('L',"Total ",61) + padSTRING('L',setNumberFormat(dblBYRQT,3).toString(),10)+"\n");								
								cl_dat.M_intLINNO_pbst +=2;								
							}
							dblPRDQT = 0;
							dblBYRQT = 0;
							if(hstPRTDS.containsKey(L_strBYRCD))
								L_strBYRDS = hstPRTDS.get(L_strBYRCD).toString();
							if(L_strBYRDS.length()>30)
								L_strBYRDS = L_strBYRDS.substring(0,28);
							L_stbDATA.append("   "+padSTRING('R',L_strBYRDS,30)+"\n");
							cl_dat.M_intLINNO_pbst ++;
							strOBYRCD = L_strBYRCD;
							flgBYRCH = true;
						}
						else						
							intPADCH += 3;													
						
						L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"");
						if((!strOPRDDS.equals(L_strPRDDS)) || (flgBYRCH == true))
						{
							flgBYRCH = false;	
							if(dblPRDQT != 0.000)							
							{
								//if(chkPRDQT.isSelected())							
								L_stbDATA.append("      "+ padSTRING('R',strOPRDDS,30) + padSTRING('L',"Total ",58) + padSTRING('L',setNumberFormat(dblPRDQT,3).toString(),10)+"\n");
								cl_dat.M_intLINNO_pbst ++;								
							}
							dblPRDQT =0;
							L_stbDATA.append("      "+padSTRING('R',L_strPRDDS,12));
							strOPRDDS = L_strPRDDS;
						}
						else						
							L_stbDATA.append(padSTRING('R',"",intPADCH+12));						
						
						L_stbDATA.append(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),10));
						L_stbDATA.append(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("datINVDT"),""),10));													
						L_stbDATA.append(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),""),10));																																   
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
						if(hstPRTDS.containsKey(L_strTEMP))
							L_strTEMP = hstPRTDS.get(L_strTEMP).toString();
						else
						{
							M_strSQLQRY = "Select PT_PRTNM from CO_PTMST where PT_PRTCD = '"+ L_strTEMP.trim() +"' AND PT_PRTTP ='C'";							
							L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);							
							if(L_rstRSSET != null)
							{																
								if(L_rstRSSET.next())								
									L_strTEMP = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");								
							}
							L_rstRSSET.close();
						}
						if(L_strTEMP.length()>30)
							L_strTEMP = L_strTEMP.substring(0,28);
						L_stbDATA.append(padSTRING('R',L_strTEMP,30));
						
						L_dblTOTQT = M_rstRSSET.getDouble("IVT_INVQT");
						dblTOTQT += L_dblTOTQT;
						dblZONQT += L_dblTOTQT;	
						dblBYRQT += L_dblTOTQT;	
						dblPRDQT += L_dblTOTQT;	
						
						L_stbDATA.append(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),""),10));
						L_intTOTPK = M_rstRSSET.getInt("IVT_INVPK");
						intTOTPK += L_intTOTPK;						
						L_stbDATA.append(padSTRING('L',String.valueOf(L_intTOTPK).toString(),6));
						L_stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTQT,3),10)+"\n");												
						dosREPORT.writeBytes(L_stbDATA.toString());						
						cl_dat.M_intLINNO_pbst ++;
						if(cl_dat.M_intLINNO_pbst > 64) 
						{							
							dosREPORT.writeBytes("\n" +strDOTLN);
							cl_dat.M_intLINNO_pbst ++;
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();					
						}	
					}
					M_rstRSSET.close();					
				}				
				if(P_strREPORT.equals("REPORT"))
				{																					
					dosREPORT.writeBytes("      "+ padSTRING('R',strOPRDDS,30) + padSTRING('L',"Total ",52) + padSTRING('L',setNumberFormat(dblPRDQT,3).toString(),16)+"\n");
					if(L_strBYRDS.length()>30)
						L_strBYRDS = L_strBYRDS.substring(0,30);					
					dosREPORT.writeBytes("   "+ padSTRING('R',L_strBYRDS,30)+ padSTRING('L',"Total ",55) + padSTRING('L',setNumberFormat(dblBYRQT,3).toString(),16)+"\n");
					dosREPORT.writeBytes(padSTRING('R',L_strZONCD,30)+ padSTRING('L',"Total ",58) + padSTRING('L',setNumberFormat(dblZONQT,3).toString(),16)+"\n");
					dosREPORT.writeBytes(padSTRING('L',"Total ",88) + padSTRING('L',setNumberFormat(dblTOTQT,3),16)+"\n");					
					dosREPORT.writeBytes("\n"+strDOTLN);
					cl_dat.M_intLINNO_pbst +=3;
					setMSG("Report completed.. ",'N');
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strEJT);
					}
					if(M_rdbHTML.isSelected())						
					    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");												
					dosREPORT.close();						
					fosREPORT.close();					
				}
			}			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		finally
		{
			if(hstPRTDS != null)
				hstPRTDS = null;
		}
	}
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;										
			dosREPORT.writeBytes("\n\n\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-25));										
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			dosREPORT.writeBytes(padSTRING('R',"Lot Details for "+strHEAD1 ,strDOTLN.length()-25));
			dosREPORT.writeBytes("Page No.    : " + cl_dat.M_PAGENO + "\n");						
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Zone"+"\n");    
			dosREPORT.writeBytes("   Customer "+"\n");
			dosREPORT.writeBytes("      Grade       Inv. No.  Date      Lot No.   Consignee                       LA No.     Pkg  Inv. Qty"+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");						
			cl_dat.M_intLINNO_pbst = 11;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	Method to validate Party Type & Party Code,i.e. to check for blank and wrong Inputs.
	*/
	boolean vldDATA()
	{			
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount() == 0)
			{					
				setMSG("Please Select the Email/s from List through F1 Help ..",'N');
				return false;
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{ 
			if (M_cmbDESTN.getSelectedIndex() == 0)
			{	
				setMSG("Please Select the Printer from Printer List ..",'N');
				return false;
			}
		}		
		return true;
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if((input == txtFMDAT) && (txtFMDAT.getText().trim().length() == 10))
				{
					if(txtTODAT.getText().trim().length()==10)
					{
						if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
						{			    
							setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
							return false;
						}
					}					
					if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{			    
						setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
						return false;					
					}
				}
				if((input == txtTODAT) && (txtTODAT.getText().trim().length() == 10))
				{					
					if(txtFMDAT.getText().trim().length()==10)
					{
						if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
						{			    
							setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
							return false;
						}
					}					
					if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{			    
						setMSG("Please Enter valid Date, To Specify Date Range .. ",'E');											
						return false;
					}					
				}
				if((input == txtSPPRT) && (txtSPPRT.getText().trim().length() == 5))
				{									
					M_strSQLQRY = "select PT_PRTCD from CO_PTMST"
						+" where PT_PRTTP ='C' AND PT_PRTCD ='"+ txtSPPRT.getText().trim() +"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{								
							M_rstRSSET.close();							
							return true;
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Buyer Code, Press F1 to select from List..",'E');							
							return false;				
						}							
					}
				}
				if((input == txtSPLOT) && (txtSPLOT.getText().trim().length() == 8))
				{					
					M_strSQLQRY = "select LT_LOTNO from PR_LTMST "
						+"where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = '"+ txtSPLOT.getText().trim() +"' AND isnull(LT_STSFL,'') <> 'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{								
							M_rstRSSET.close();							
							return true;								
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Lot Number ..",'E');							
							return false;				
						}							
					}
				}
				if((input == txtSPPRD) && (txtSPPRD.getText().trim().length() == 10))
				{									
					
					M_strSQLQRY = "Select PR_PRDCD from CO_PRMST where PR_PRDCD ='"+txtSPPRD.getText().trim()+"'"
						+" AND isnull(PR_STSFL,'')<>'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{		
							M_rstRSSET.close();								
							return true;								
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Product Code, Press F1 to select from List..",'E');
							return false;				
						}				
					}
				}																													
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"INPVF");
			}
			return true;
		}
	}	
}
/*	if(((M_objSOURC == txtSPPRT) && (strSPECI.equals("Lot"))) ||(M_objSOURC == txtSPLOT))		
				{
					if(txtSPPRT.getText().trim().length()>0)
						txtSPPRT.setText(txtSPPRT.getText().trim().toUpperCase());
					if(txtFMDAT.getText().trim().length()>0)
						strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					if(txtTODAT.getText().trim().length()>0)
						strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));															
										
					M_strSQLQRY = "Select distinct IST_LOTNO,PR_PRDDS from MR_IVTRN,FG_ISTRN,CO_PRMST where "
						+" IVT_MKTTP = IST_MKTTP AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO AND IVT_PRDCD = IST_PRDCD AND IST_PRDCD = PR_PRDCD";		
					
					if((strFMDAT.length()>0)&&(strTODAT.length()>0))										
						M_strSQLQRY +=" AND date(IVT_INVDT) between '"+strFMDAT+"' AND '"+strTODAT+"' ";
					else if(strFMDAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) >'"+ strFMDAT +"' ";
					else if(strTODAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) <'"+ strTODAT +"' ";					
					if(txtSPPRD.getText().trim().length()>0)					
						M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtSPPRD.getText().trim() +"'";
				}*/
					
					/*if((strFMDAT.length()>0)&&(strTODAT.length()>0))										
						M_strSQLQRY +=" AND date(IVT_INVDT) between '"+strFMDAT+"' AND '"+strTODAT+"' ";
					else if(strFMDAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) >'"+ strFMDAT +"' ";
					else if(strTODAT.length()>0)
						M_strSQLQRY +=" AND date(IVT_INVDT) <'"+ strTODAT +"' ";	*/
					/*else if(M_objSOURC == txtSPPRD)
				{									
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPPRD";		
					String L_arrHDR[] = {"Party Code","Party Description"};					
					if(txtSPPRD.getText().trim().length()>0)
						M_strSQLQRY +=" AND IVT_BYRCD like '"+ txtSPPRD.getText().trim() +"%'";					
					M_strSQLQRY +="order by IVT_BYRCD";					
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");
				}*/