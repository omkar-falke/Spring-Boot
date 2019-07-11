import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.util.Vector;
import javax.swing.InputVerifier;
import java.util.StringTokenizer;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class mm_temrn extends cl_pbase
{
	private JComboBox cmbMATTP,cmbPRTTP;
	private JComboBox cmbFRWTO;
	private JTextField txtMRNNO,txtMRNDT,txtDPTCD,txtDPTDS,txtMATCD,txtREMDS,txtISSNO,txtTAGNO,txtPREBY,txtAUTBY,txtAUTDT,txtPRTCD,txtPRTNM,txtFRWBY,txtMRNST;
	private cl_JTable tblMRNDT;
	private JButton btnFRWD;
	private JButton btnPRNT;
	private String strMRNNO,strREMDS,strMRNDT,strTEMP,strMATTP,strPISNO ="",strMRNST ="",strFRWBY="",strFRWTO="";
	private int intRECCT =0;
	final String strTRNTP_fn = "MR";
	private boolean flgRMKFL = false;
	private boolean flgFRWMR = false;
	private Hashtable<String,String>  hstITMDT = new Hashtable<String,String>();
	private Hashtable<String,String>  hstDPTCD = new Hashtable<String,String>();
	private Hashtable<String,String>  hstISSRT = new Hashtable<String,String>();
	private Hashtable<String,String>  hstWAVRT = new Hashtable<String,String>();
	private Vector<String> vtrMATCD = new Vector<String>();
	private mm_temrnTBLINVFR objTBLVRF;
	private final int TBL_CHKFL =0;
	private final int TBL_MATCD =1;
	private final int TBL_MATDS =2;
	private final int TBL_UOMCD =3;
	private final int TBL_RCNFL =4;
	private final int TBL_RETQT =5;
	private final int TBL_MRNRT =6;
	private final int TBL_ISSNO =7;
	private final int TBL_TAGNO =8;
	private final int TBL_LOCCD =9;
	private final int TBL_ORGCD =10;
	private final String strDOCTP_fn ="6";	
	private final String strPKGMT_fn ="3"; 
	private final String strRAWMT_fn ="2";
	private final String strSTRSP_fn ="1";
	private Vector<String> vtrFRWBY = new Vector<String>();
	private String strCLSNM ="";
	INPVF objINPVR = new INPVF();	
	
	
	mm_temrn()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			strCLSNM = getClass().getName();
			System.out.println(strCLSNM);
			add(btnPRNT = new JButton("Print"),1,8,1,1,this,'L');
			add(new JLabel("Material Type"),2,1,1,1,this,'L');
			add(cmbMATTP = new JComboBox(),2,2,1,2,this,'L');
			add(new JLabel("MRN No."),2,4,1,1,this,'L');
			add(txtMRNNO = new TxtLimit(8),2,5,1,1,this,'L');
			add(new JLabel("MRN Date"),2,6,1,1,this,'L');
			add(txtMRNDT = new TxtDate(),2,7,1,1,this,'L');
			add(new JLabel("Department"),3,1,1,1,this,'L');
			add(txtDPTCD=new TxtLimit(3),3,2,1,1,this,'L');
			add(txtDPTDS=new TxtLimit(40),3,3,1,5,this,'L');
			txtDPTDS.setEnabled(false);
		    add(new JLabel("Prepared By"),4,1,1,1,this,'L');
			add(txtPREBY=new TxtLimit(3),4,2,1,1,this,'L');
			add(new JLabel("Authorised By"),4,4,1,1,this,'L');
			add(txtAUTBY=new TxtLimit(3),4,5,1,1,this,'L');
			add(new JLabel("Auth. Date"),4,6,1,1,this,'L');
			add(txtAUTDT = new TxtDate(),4,7,1,1,this,'L');
			add(new JLabel("Party Type"),5,1,1,1,this,'L');
			add(cmbPRTTP = new JComboBox(),5,2,1,2,this,'L');
			add(new JLabel("Party Code"),5,4,1,1,this,'L');
			//add(new JLabel("Party Code"),5,6,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(5),5,5,1,1,this,'L');
			add(txtPRTNM = new TxtLimit(40),5,6,1,2,this,'L');
			String[] L_strTBLHD = {" ","Item Code","Description","UOM ","Recond.","Ret.Qty","Rate","Issue No.","Tag","Location","Org code"};
			int[] L_intCOLSZ = {15,80,325,40,30,50,60,60,40,40,80};
			tblMRNDT = crtTBLPNL1(this,L_strTBLHD,100,6,1,10,7.9,L_intCOLSZ,new int[]{0,4});
			add(new JLabel("Remarks"),16,1,1,1,this,'L');
			add(txtREMDS=new TxtLimit(200),16,2,1,7,this,'L');
			add(new JLabel("Forward To"),17,1,1,1,this,'L');
			add(cmbFRWTO=new JComboBox(),17,2,1,1,this,'L');
			add(btnFRWD = new JButton("Forward"),17,3,1,1.2,this,'L');
			//add(cmbAUTST=new JComboBox(),17,5,1,1.8,this,'R');
			add(txtMRNST=new TxtLimit(20),17,5,1,1.8,this,'R');
			add(new JLabel("Forwarded By"),17,7,1,1,this,'L');
			add(txtFRWBY=new TxtLimit(3),17,8,1,1,this,'L');
			btnFRWD.setEnabled(false);
			cmbFRWTO.setEnabled(false);
			txtMRNST.setEnabled(false);
			
			txtMATCD = new TxtLimit(10);
			txtMATCD.addFocusListener(this);
			txtMATCD.addKeyListener(this);
			txtISSNO = new TxtLimit(8);
			txtISSNO.addFocusListener(this);
			txtISSNO.addKeyListener(this);
			
			txtTAGNO = new TxtLimit(15);
			txtTAGNO.addFocusListener(this);
			txtTAGNO.addKeyListener(this);
			
			tblMRNDT.setCellEditor(TBL_MATCD,txtMATCD);
			tblMRNDT.setCellEditor(TBL_ISSNO,txtISSNO);
			tblMRNDT.setCellEditor(TBL_TAGNO,txtTAGNO);
			if(hstDPTCD !=null)
			hstDPTCD.clear();
			String L_strDATA;
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CGSTP,CMT_CHP01 from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP IN('MMXXMTP','COXXDPT') and isnull(cmt_stsfl,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXMTP"))
				{
					L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
					if(!nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"").equals("04"))
						cmbMATTP.addItem(L_strDATA);
				}
				else hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
				M_strSQLQRY = "SELECT CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CHP02 from CO_CDTRN where cmt_CGMTP ='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp IN('QCXXAU1','MMXXIND')";
			L_strDATA ="";
			ResultSet M_rstRSSET1 =null;
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET1 != null)	
			{
				cmbFRWTO.addItem("Forw.To");
				while(M_rstRSSET1.next()){
					L_strDATA = nvlSTRVL(M_rstRSSET1.getString("CMT_CODCD"),"");
				if(nvlSTRVL(M_rstRSSET1.getString("CMT_CGSTP"),"").equals("QCXXAU1"))
				{
					if(nvlSTRVL(M_rstRSSET1.getString("CMT_CHP02"),"").indexOf("M")>0) 
			  			cmbFRWTO.addItem(L_strDATA);
				}
				else
				{
					vtrFRWBY.addElement(L_strDATA);
				}
				}
				
			}
			/*M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='STS' and cmt_cgstp ='MMXXIND' AND CMT_CHP01 ='A"+cl_dat.M_strCMPCD_pbst+"'";
			L_strDATA ="";
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET1 != null)	
			{
				cmbAUTST.addItem("Auth. Status");
				while(M_rstRSSET1.next()){
				L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET1.getString("CMT_CODCD"),""),2) +M_rstRSSET1.getString("CMT_CODDS");
			  	cmbAUTST.addItem(L_strDATA);
				}
			}*/
			//cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_temrnTBLINVFR();
			tblMRNDT.setInputVerifier(objTBLVRF);	
			txtPRTCD.setInputVerifier(objINPVR);	
			cmbPRTTP.addItem("S Supplier");
			setENBL(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
			
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		String L_strMATTP ="";
		if(M_objSOURC == btnPRNT)
		{
			if(txtMRNNO.getText().trim().length() ==8)
				strMRNNO = txtMRNNO.getText().trim();
			if(strMRNNO.length() ==0)
			{
				setMSG("MRN number is not specified..",'E');
				return;
			}
			mm_rpmrn objMRNRP  = new mm_rpmrn(M_strSBSCD);
			objMRNRP.genREPORT(strMRNNO,strMRNNO,0,txtPRTCD.getText().trim());
			JComboBox L_cmbLOCAL = objMRNRP.getPRNLS();
			objMRNRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpmrn.doc",L_cmbLOCAL.getSelectedIndex());
				
		}
		
		else if(M_objSOURC == btnFRWD)
		{
			try
			{
				flgFRWMR  = true;
				cl_dat.M_flgLCUPD_pbst = true;
				String L_strFRWTO ="" ;
				if(cmbFRWTO.getSelectedIndex() ==0)
				{
					setMSG("Please select the Authorising person to whom indent is to be forwarded..",'E'); 
					setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				else
				{
					L_strFRWTO = cmbFRWTO.getSelectedItem().toString().trim();
				   if(strMRNST.equals("4"))
				   {
						setMSG("Already Approved",'E'); 
						setCursor(cl_dat.M_curDFSTS_pbst);
						return;
					}
				   else if(strMRNST.equals("3"))
				   {
						setMSG("Can not forward..MRN is held for Discussion..",'E'); 
						setCursor(cl_dat.M_curDFSTS_pbst);
						return;
					}
				}
				cmbFRWTO.setEnabled(false);
				btnFRWD.setEnabled(false);
				String L_strTEMP = txtMRNNO.getText().trim();
				System.out.println("L_strTEMP "+L_strTEMP);
				System.out.println("strMRNNO "+strMRNNO);
				M_strSQLQRY = "UPDATE MM_MRMST SET "+
							  " MR_FRWBY ='"+cl_dat.M_strUSRCD_pbst+"',"+
							  "MR_FRWTO ='"+L_strFRWTO+"',"+
							  "MR_FRWDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText().trim()))+"',";
				M_strSQLQRY += getUSGDTL("MR",'U',"1"); // status flag 2 is forwarded
				M_strSQLQRY += " WHERE MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_MMSBS ='"+M_strSBSCD+"' AND MR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY += " AND MR_MRNNO = '"+strMRNNO +"'";
				M_strSQLQRY += " AND isnull(MR_STSFL,'') <>'X'";
			//	M_strSQLQRY += " AND IN_AMDNO = '"+strPRAMD +"'";
			//	L_strORGST = strINDST;
				//strINDST = "2";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					exeSAVE();
				}
			//	strINDST =L_strORGST;
				if(cl_dat.exeDBCMT("Forward"))
				{
					setMSG("MRN forwarded to "+L_strFRWTO,'N');
					M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+L_strFRWTO+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					if(M_rstRSSET.next())
					{
						String L_strEML = M_rstRSSET.getString("US_EMLRF");
						cl_eml ocl_eml = new cl_eml();
						ocl_eml.sendfile(L_strEML,null,"MRN No." +L_strTEMP +" is pending for Approval ","MRN No." +L_strTEMP +" is pending for Approval ");
					}
					if(M_rstRSSET != null)
					M_rstRSSET.close();
		
				}
				else
				{
					setMSG("Error in Forwarding the MRN.. ",'E');
				}
				// enable
				cmbFRWTO.setEnabled(true);
				btnFRWD.setEnabled(true);
			}
			catch(Exception L_E)
			{
				
				setMSG(L_E,"forward ");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			flgFRWMR  = false;
		}
		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
			{
				setENBL(false);
				setMSG("Please Select an Option..",'E');
			}
			else
			{
				clrCOMP();
				/*L_strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
				for(int i=0;i<cmbMATTP.getItemCount();i++)
				{
					if(cmbMATTP.getItemAt(i).toString().substring(0,1).equals(L_strMATTP))
					{
						cmbMATTP.setSelectedIndex(i);
						break;
					}
				}*/
			
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					txtMRNNO.setEnabled(true);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						setENBL(false);
				}
				else 
				{
					txtMRNNO.setEnabled(false);
					txtMRNDT.setText(cl_dat.M_strLOGDT_pbst);
				}
				cmbMATTP.requestFocus();
			}
		}
		if(M_objSOURC == cmbMATTP)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				txtMRNDT.setText(cl_dat.M_strLOGDT_pbst);
		}
		if(M_objSOURC == txtMRNNO)
		{
			String L_strMRNNO = txtMRNNO.getText().trim();
			L_strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
			clrCOMP();
			txtMRNNO.setText(L_strMRNNO);
			for(int i=0;i<cmbMATTP.getItemCount();i++)
			{
				if(cmbMATTP.getItemAt(i).toString().substring(0,1).equals(L_strMATTP))
				{
					cmbMATTP.setSelectedIndex(i);
					break;
				}
			}
			getDATA(M_strSBSCD.substring(2,4),cmbMATTP.getSelectedItem().toString().substring(0,2),L_strMRNNO);
		}
		if(M_objSOURC == txtDPTCD)
		{
			if(hstDPTCD.containsKey((String)txtDPTCD.getText()))
				txtDPTDS.setText(hstDPTCD.get(txtDPTCD.getText()).toString());
			cmbPRTTP.requestFocus();	
		}
		if(M_objSOURC == cmbPRTTP)
		    txtPRTCD.requestFocus();
		if(M_objSOURC == txtPRTCD)
		{
	    	tblMRNDT.setRowSelectionInterval(0,0);
			tblMRNDT.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);
			tblMRNDT.editCellAt(tblMRNDT.getSelectedRow(),TBL_MATCD);
			tblMRNDT.cmpEDITR[1].requestFocus();
			setMSG("press F1 to select the item code ..",'N');
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int[] L_inaCOLSZ = new int[]{100,100,380};
		if(L_KE.getKeyCode() == L_KE.VK_F2)
		{
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCDF2";
			 	String L_ARRHDR[] = {"Item Code","UOM","Description"};
			    strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				M_strSQLQRY = "Select CT_MATCD,CT_UOMCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				if(strMATTP.equals(strPKGMT_fn))
					M_strSQLQRY += " and CT_GRPCD = '69'";
				else if(strMATTP.equals(strRAWMT_fn))
				{
					if(!M_strSBSCD.substring(2,4).equals("01"))
						M_strSQLQRY += " and CT_GRPCD = '68'";
					else
					{
						M_strSQLQRY += " and CT_GRPCD <> '68'";
					}
				}
				else if(strMATTP.equals(strSTRSP_fn))
					M_strSQLQRY += " and CT_GRPCD not IN('68','69') ";
				else
					M_strSQLQRY += " and CT_GRPCD not IN('68','69') ";
						
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '"+txtMATCD.getText().trim() +"%'";
			
				M_strSQLQRY += " Order by ct_matds ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,3,"CT",L_inaCOLSZ);
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			
			if(M_objSOURC == txtMATCD)
			{
				strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				String L_ARRHDR[] = {"Item Code","UOM","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_UOMCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				if(strMATTP.equals(strPKGMT_fn))
					M_strSQLQRY += " and CT_GRPCD = '69'";
				else if(strMATTP.equals(strRAWMT_fn))
				{
					if(!M_strSBSCD.substring(2,4).equals("01"))
						M_strSQLQRY += " and CT_GRPCD = '68'";
					else
					{
						M_strSQLQRY += " and CT_GRPCD <> '68'";
					}
				}
				else if(strMATTP.equals(strSTRSP_fn))
					M_strSQLQRY += " and CT_GRPCD not IN('68','69') ";
				else
					M_strSQLQRY += " and CT_GRPCD not IN('68','69') ";
						
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '"+txtMATCD.getText().trim() +"%'";
				
				M_strSQLQRY += " Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,3,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtDPTCD)
			{	
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtDPTCD";
				String L_ARRHDR[] = {"Department Code","Description"};
				M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' ";
				if(txtDPTCD.getText().trim().length() >0)
					M_strSQLQRY += " AND CMT_CODCD like '"+txtDPTCD.getText().trim() +"%'";
				M_strSQLQRY += " order by CMT_CODDS";
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
			}
			else if(M_objSOURC == txtMRNNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMRNNO";
				String L_ARRHDR[] = {"MRN Number","Status","Dept Code"};
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					M_strSQLQRY = "Select distinct MR_MRNNO,MR_STSFL,MR_DPTCD from MM_MRMST ";
					M_strSQLQRY += " where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,'') ='4' ";
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(strCLSNM.equals("mm_temr1"))
					{
						M_strSQLQRY = "Select distinct MR_MRNNO,MR_STSFL,MR_DPTCD from MM_MRMST ";
						M_strSQLQRY +=" where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,'') not in('5','X') ";
					}
					else
					{
						M_strSQLQRY = "Select distinct MR_MRNNO,MR_STSFL,MR_DPTCD from MM_MRMST ";
						M_strSQLQRY +=" where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,'') not in('4','5','X') ";
					}
				}
				else
				{	
					M_strSQLQRY = "Select distinct MR_MRNNO,MR_STSFL,MR_DPTCD from MM_MRMST ";
					M_strSQLQRY += " where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,'')<>'X'";
				}
				if(txtMRNNO.getText().trim().length() >0)
						M_strSQLQRY += " and MR_MRNNO like '" + txtMRNNO.getText().trim() + "%'";
				
				M_strSQLQRY += " and MR_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY += " and MR_MATTP ='"+cmbMATTP.getSelectedItem().toString().substring(0,1) +"'";
				M_strSQLQRY +=" Order by MR_MRNNO DESC ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
			}
			else if(M_objSOURC == txtISSNO)
			{
				M_strHLPFLD = "txtISSNO";
				M_strSQLQRY = "SELECT DISTINCT IS_ISSNO,IS_TAGNO,IS_ISSDT,IS_ISSRT FROM MM_ISMST WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MMSBS ='"+M_strSBSCD+"'";
				M_strSQLQRY +=" and IS_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND IS_MATCD ='"+tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD)+"' and is_dptcd ='"+txtDPTCD.getText().trim()+"'";
				M_strSQLQRY +=" Order by IS_ISSNO DESC ";
				String L_ARRHDR[] = {"Issue Number","Tag No","Issue Date","Rate"};
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
			}
			else if(M_objSOURC == txtTAGNO)
			{
				M_strHLPFLD = "txtTAGNO";
				M_strSQLQRY = "SELECT DISTINCT IS_TAGNO,IS_ISSRT FROM MM_ISMST WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MMSBS ='"+M_strSBSCD+"'";
				M_strSQLQRY +=" and IS_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND IS_MATCD ='"+tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()+"' and is_dptcd ='"+txtDPTCD.getText().trim()+"'";
				M_strSQLQRY +=" and IS_ISSNO ='"+tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_ISSNO).toString()+"'";
				M_strSQLQRY +=" Order by IS_ISSNO DESC ";
				String L_ARRHDR[] = {"Tag No","Rate"};
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
			else if(M_objSOURC == txtPRTCD)				// Usage Type
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPRTCD";
				String L_ARRHDR[] = {"Party Code","Party Name"};
				M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST";
				M_strSQLQRY += " where PT_PRTTP = '"+cmbPRTTP.getSelectedItem().toString().substring(0,1) +"'";
				if(txtPRTCD.getText().trim().length() >0)
					M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText()+"%'";	
				M_strSQLQRY += " Order By PT_PRTCD";
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbMATTP)
				cmbMATTP.transferFocus();
			else if(M_objSOURC == cmbPRTTP)
				cmbPRTTP.transferFocus();
			/*else if(M_objSOURC == txtMRNNO)
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					getDATA(M_strSBSCD.substring(2,4),cmbMATTP.getSelectedItem().toString().substring(0,2),txtMRNNO.getText().trim());
					//txtMRNNO.transferFocus();
				}
			}*/
			else if(M_objSOURC == txtMRNDT)
				txtMRNDT.transferFocus();
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtMRNNO"))
			{
				txtMRNNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtPRTCD"))
			{
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if(M_strHLPFLD.equals("txtISSNO"))
			{
				txtISSNO.setText(cl_dat.M_strHLPSTR_pbst);
				tblMRNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblMRNDT.getSelectedRow(),TBL_TAGNO);
				String L_strTEMP = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim();
				if(Double.parseDouble(L_strTEMP)>0)
				{
					tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
					//hstISSRT.put(cl_dat.M_strHLPSTR_pbst+tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString(),L_strTEMP);
					hstISSRT.put(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString(),L_strTEMP);
				}
			}
			else if(M_strHLPFLD.equals("txtTAGNO"))
			{
				txtTAGNO.setText(cl_dat.M_strHLPSTR_pbst);
				String L_strTEMP = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
				if(Double.parseDouble(L_strTEMP)>0)
				{
					tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
					//hstISSRT.put(cl_dat.M_strHLPSTR_pbst+tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString(),L_strTEMP);
					hstISSRT.put(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString(),L_strTEMP);
				}
			}
			else if(M_strHLPFLD.equals("txtDPTCD"))
			{
				txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtDPTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if((M_strHLPFLD.equals("txtMATCD"))||(M_strHLPFLD.equals("txtMATCDF2")))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				String L_strMATCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim();
				txtMATCD.setText(L_strMATCD);
			//	if(tblMRNDT.isEditing())
			//		tblMRNDT.getCellEditor().stopCellEditing();
				tblMRNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblMRNDT.getSelectedRow(),TBL_UOMCD);
				tblMRNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblMRNDT.getSelectedRow(),TBL_MATDS);
				tblMRNDT.setValueAt(L_strMATCD,tblMRNDT.getSelectedRow(),TBL_ORGCD);
				if(objTBLVRF.verify(tblMRNDT.getSelectedRow(),TBL_MATCD))
				{
					tblMRNDT.setRowSelectionInterval(tblMRNDT.getSelectedRow(),tblMRNDT.getSelectedRow());		
					tblMRNDT.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);		
					tblMRNDT.editCellAt(tblMRNDT.getEditingRow(),TBL_MATCD);
					tblMRNDT.cmpEDITR[TBL_MATCD].requestFocus();
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	private boolean getDATA(String P_strSTRTP,String P_strMRNTP,String P_strMRNNO)
	{
		boolean L_FIRST = true;
		ResultSet L_rstRSSET; 
		java.sql.Date L_datTMPDT;
		String L_strDPTCD="",L_strPRTTP;
		try
		{
			String L_TPRCD,L_CHLQT,L_RECQT;
			String L_strMATCD ="",L_strINDNO ="",L_strSTSFL="",L_strORGCD="",L_strRCNVL ="";
			String L_strMRNRT ="",L_strISSNO ="",L_strTAGNO ="";
			int i = 0;
			
			M_strSQLQRY = "Select MR_MMSBS,MR_STRTP,MR_MATTP,MR_MRNNO,MR_MRNDT,MR_DPTCD,MR_MATCD,MR_ORGCD,MR_PREBY,MR_AUTBY,MR_AUTDT,MR_FRWBY,MR_FRWTO,";
			M_strSQLQRY +=" MR_RETQT,MR_ISSNO,MR_TAGNO,MR_MRNRT,MR_RCNFL,MR_STSFL,MR_TRNFL,MR_LUSBY,MR_LUPDT,MR_PRTTP,MR_PRTCD,PT_PRTNM from MM_MRMST ";
			M_strSQLQRY += " LEFT OUTER JOIN CO_PTMST ON PT_PRTTP = MR_PRTTP AND PT_PRTCD = MR_PRTCD AND isnull(PT_STSFL,'') <>'X' where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_MATTP = '" + P_strMRNTP + "'";
			M_strSQLQRY += " and MR_MRNNO = '" + P_strMRNNO + "'";
			M_strSQLQRY += " and isnull(MR_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			strMRNNO = P_strMRNNO;
			if(hstITMDT !=null)
				hstITMDT.clear();
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(tblMRNDT.isEditing())
				tblMRNDT.getCellEditor().stopCellEditing();
			tblMRNDT.setRowSelectionInterval(0,0);
			tblMRNDT.setColumnSelectionInterval(0,0);
			intRECCT =0;
			String L_strRCNFL ="";
			if(vtrMATCD !=null)
				vtrMATCD.removeAllElements();
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				if(L_FIRST){
					txtMRNNO.setText(P_strMRNNO);
					L_datTMPDT = M_rstRSSET.getDate("MR_MRNDT");
					if(L_datTMPDT !=null)
					{
						txtMRNDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						strMRNDT = txtMRNDT.getText().trim();
					}
					else
						txtMRNDT.setText("");
					L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("MR_DPTCD"),"");
					L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("MR_STSFL"),"");
					System.out.println("L_strSTSFL"+L_strSTSFL);
					if(Integer.parseInt(L_strSTSFL) >2)
					{
						btnFRWD.setEnabled(false);
					}
					else
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						btnFRWD.setEnabled(true);
					}
					strMRNST = L_strSTSFL;
					txtDPTCD.setText(L_strDPTCD);
					txtPREBY.setText(nvlSTRVL(M_rstRSSET.getString("MR_PREBY"),""));
					txtAUTBY.setText(nvlSTRVL(M_rstRSSET.getString("MR_AUTBY"),""));
					strFRWTO = nvlSTRVL(M_rstRSSET.getString("MR_FRWTO"),"");
					strFRWBY = nvlSTRVL(M_rstRSSET.getString("MR_FRWBY"),"");
					L_datTMPDT = M_rstRSSET.getDate("MR_AUTDT");
					if(L_datTMPDT !=null)
					{
						txtAUTDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					}
					else
						txtAUTDT.setText("");
					if(L_strDPTCD.length() >0)
						txtDPTDS.setText(hstDPTCD.get(L_strDPTCD).toString());
					L_strPRTTP = nvlSTRVL(M_rstRSSET.getString("MR_PRTTP"),"");
					for(int j=0;j<cmbPRTTP.getItemCount();j++)
					{
						if(cmbPRTTP.getItemAt(j).toString().substring(0,1).equals(L_strPRTTP))
						{
							cmbPRTTP.setSelectedIndex(j);
							break;
						}
					}
					txtPRTCD.setText(nvlSTRVL(M_rstRSSET.getString("MR_PRTCD"),""));
				    txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
					txtMRNST.setText(cl_dat.getPRMCOD("CMT_CODDS","STS","MMXXMRN",strMRNST));
					if(strMRNST.equals("1")) // Forwarded
					{
						btnFRWD.setText("Re-Forward");
					}
					else
						btnFRWD.setText("Forward");
				}
				
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))		
				{
					System.out.println("L_strSTSFL "+L_strSTSFL);
					if(L_strSTSFL.equals("0"))
					{
						btnFRWD.setEnabled(true);
						cmbFRWTO.setEnabled(true);
					}
					if(strCLSNM.equals("mm_temrn"))
					if(L_strSTSFL.equals("4"))
					{
						setMSG("MRN is Approved..",'E');
						setCursor(cl_dat.M_curDFSTS_pbst);
						setENBL(false);
						return false;
					}
					if(L_strSTSFL.equals("5"))
					{
						setMSG("MRN is Authorised..",'E');
						setCursor(cl_dat.M_curDFSTS_pbst);
						setENBL(false);
						return false;
					}
					else if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
						setENBL(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))		
				{
					tblMRNDT.setValueAt(Boolean.TRUE,i,TBL_CHKFL);
				}
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("MR_MATCD"),"");
				L_strORGCD = nvlSTRVL(M_rstRSSET.getString("MR_ORGCD"),"");
				vtrMATCD.addElement(L_strMATCD);
				tblMRNDT.setValueAt(L_strMATCD,i,TBL_MATCD);
				tblMRNDT.setValueAt(L_strORGCD,i,TBL_ORGCD);
				L_strRCNFL = nvlSTRVL(M_rstRSSET.getString("MR_RCNFL"),"");
				hstITMDT.put(L_strMATCD,L_strRCNFL);
				getWAVRT(L_strMATCD,i);
			//	tblMRNDT.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,TBL_UOMCD);
			//	tblMRNDT.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,TBL_MATDS);
				tblMRNDT.setValueAt(M_rstRSSET.getString("MR_RETQT"),i,TBL_RETQT);
				L_strMRNRT = M_rstRSSET.getString("MR_MRNRT");
				tblMRNDT.setValueAt(L_strMRNRT,i,TBL_MRNRT);
				L_strISSNO = nvlSTRVL(M_rstRSSET.getString("MR_ISSNO"),"");
				L_strTAGNO = nvlSTRVL(M_rstRSSET.getString("MR_TAGNO"),"");
				tblMRNDT.setValueAt(L_strISSNO,i,TBL_ISSNO);
				tblMRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("MR_TAGNO"),""),i,TBL_TAGNO);
				if((L_strISSNO.length() >0)&&(L_strTAGNO.length() >0))
					hstISSRT.put(L_strMATCD,L_strMRNRT);
				L_strRCNVL = nvlSTRVL(M_rstRSSET.getString("MR_RCNFL"),"");
				if(L_strRCNVL.trim().length() >0)
					tblMRNDT.setValueAt(Boolean.TRUE,i,TBL_RCNFL);
				else
					tblMRNDT.setValueAt(Boolean.FALSE,i,TBL_RCNFL);
				getITMDT(L_strMATCD,L_strORGCD,i);
				i++;
			}
			txtFRWBY.setText(strFRWBY);
			cmbFRWTO.setSelectedIndex(0);
			//cmbAUTST.setSelectedIndex(0);
			for(i=0;i<cmbFRWTO.getItemCount();i++)
			{
				if(cmbFRWTO.getItemAt(i).toString().equals(strFRWTO))
				{
					cmbFRWTO.setSelectedIndex(i);
					break;
				}
			}
			/*for(i=0;i<cmbAUTST.getItemCount();i++)
			{
				if(cmbAUTST.getItemAt(i).toString().substring(0,1).equals(strMRNST))
				{
					cmbAUTST.setSelectedIndex(i);
					break;
				}
			}*/
			intRECCT =i;
				
			if(M_rstRSSET != null)
				M_rstRSSET.close();

			M_strSQLQRY = "Select RM_REMDS,RM_STSFL from MM_RMMST ";
			M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
			M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
	//		M_strSQLQRY += " and RM_DOCTP = '" + LP_ISSTP + "'";
			M_strSQLQRY += " and RM_DOCNO = '" + txtMRNNO.getText().trim() + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					flgRMKFL = true;
					if(!nvlSTRVL(M_rstRSSET.getString("RM_STSFL"),"").equals("X"))
						txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
					else
						txtREMDS.setText("");
				}
				M_rstRSSET.close();
			}
		}catch(Exception e){
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(e,"getDATA");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
		return !L_FIRST;
	}
	boolean vldDATA()
	{
		if(tblMRNDT.isEditing())
		{
			if(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),tblMRNDT.getSelectedColumn()).toString().length()>0)
			{
			mm_temrnTBLINVFR obj=new mm_temrnTBLINVFR();
			obj.setSource(tblMRNDT);
			if(obj.verify(tblMRNDT.getSelectedRow(),tblMRNDT.getSelectedColumn()))
				tblMRNDT.getCellEditor().stopCellEditing();
			else
				return false;
			}
		}
		if(txtDPTCD.getText().trim().length() ==0)
		{
			setMSG("Dept. Code can not be blank..",'E');
			return false;
		}
		else
		{
			if(!hstDPTCD.containsKey(txtDPTCD.getText().trim()))
			{
			   setMSG("Invalid Department code..",'E');
			   return false;
			}
		}
		if(txtMRNDT.getText().trim().length() ==0)
		{
			txtMRNDT.setText(cl_dat.M_strLOGDT_pbst);
		//	setMSG("MRN Date can not be blank..",'E');
		//	return false;
		}
		String L_strRCNFL ="",L_strTEMP="";
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
		for(int i=0;i<tblMRNDT.getRowCount();i++)
		{
			if(tblMRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				if(hstITMDT.get(tblMRNDT.getValueAt(i,TBL_MATCD).toString()) !=null)
				{
					L_strRCNFL = hstITMDT.get(tblMRNDT.getValueAt(i,TBL_MATCD).toString()).toString();
			       if(tblMRNDT.getValueAt(i,TBL_RCNFL).toString().equals("true"))
						L_strTEMP ="Y";
					else
						L_strTEMP ="";
					if(!L_strRCNFL.equals(L_strTEMP))
					{
					 	setMSG("Modification of Reconditioned Tag is not allowed.. ",'E');
						if(hstITMDT.get(tblMRNDT.getValueAt(i,TBL_MATCD).toString()).toString().equals("Y"))
							tblMRNDT.setValueAt(Boolean.TRUE,i,TBL_RCNFL);
						else
							tblMRNDT.setValueAt(Boolean.FALSE,i,TBL_RCNFL);
						return false;
					}
						
				}
			}
		}
		for(int i=0;i<tblMRNDT.getRowCount();i++)
		{
			if(tblMRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			{
				strTEMP = nvlSTRVL(tblMRNDT.getValueAt(i,TBL_MATCD).toString(),"");
				if(strTEMP.length() < 10)
				{
					setMSG("Invalid Item Code at Row  .."+i+1,'E');
					return false;
				}
				else
				{
					strTEMP = strTEMP.substring(9);
					if(strTEMP.equals("8"))
					{
						tblMRNDT.setValueAt("0.01",i,TBL_MRNRT);
						tblMRNDT.setValueAt(Boolean.TRUE,i,TBL_RCNFL);
					}
				}
				strTEMP = tblMRNDT.getValueAt(i,TBL_RETQT).toString();
				if(strTEMP.length() == 0)
				{
					setMSG("MRN Qty. can not be blank..",'E');
					tblMRNDT.cmpEDITR[TBL_RETQT].requestFocus();
					return false;
				}
				if(Double.parseDouble(tblMRNDT.getValueAt(i,TBL_RETQT).toString())==0)
				{
					setMSG("Qty. can not be zero..",'E');
					return false;
				}
				if(tblMRNDT.getValueAt(i,TBL_RCNFL).toString().equals("true"))
				{
					tblMRNDT.setValueAt("0.01",i,TBL_MRNRT);
				}

				strTEMP = tblMRNDT.getValueAt(i,TBL_MRNRT).toString();
				// Rate changing at the time of addition blocked on 17/08/2004, for access to all users
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
				{
						if(strTEMP.length() == 0)
						{
							setMSG("MRN Rate can not be blank..",'E');
							tblMRNDT.setValueAt("0",i,TBL_MRNRT);
							//return false;
						}
						/*if(Double.parseDouble(strTEMP)==0)
						{
							setMSG("Rate can not be zero..",'E');
							return false;
						}*/
					
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))		
				{
					if(strTEMP.length() == 0)
					{
						setMSG("MRN Rate can not be blank..",'E');
						return false;
					}
					if(Double.parseDouble(strTEMP)==0)
					{
						setMSG("Rate can not be zero..",'E');
						return false;
					}
				}
			}
		}
		return true;													 
														 
	}
	void exeSAVE()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			if(vldDATA())
			{
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))		
			{
				for(int i=0;i<tblMRNDT.getRowCount();i++)
				{
					if(tblMRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY = " UPDATE MM_MRMST SET ";
						M_strSQLQRY += "MR_AUTDT ='" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY += "MR_AUTBY ='" + cl_dat.M_strUSRCD_pbst+"',";
					    M_strSQLQRY +=" MR_DPTCD ='"+txtDPTCD.getText().trim()+"',";
						M_strSQLQRY +=" MR_ISSNO ='"+tblMRNDT.getValueAt(i,TBL_ISSNO)+"',";
						if(tblMRNDT.getValueAt(i,TBL_RCNFL).toString().equals("true"))
							M_strSQLQRY +=" MR_RCNFL ='Y',";
						else
							M_strSQLQRY +=" MR_RCNFL ='',";
					
						M_strSQLQRY +=" MR_RETQT ="+tblMRNDT.getValueAt(i,TBL_RETQT)+",";
						M_strSQLQRY +=" MR_MRNRT ="+tblMRNDT.getValueAt(i,TBL_MRNRT)+",";
						double L_MRNVL = Double.parseDouble(tblMRNDT.getValueAt(i,TBL_MRNRT).toString())*Double.parseDouble(tblMRNDT.getValueAt(i,TBL_RETQT).toString());
						M_strSQLQRY += " MR_MRNVL ="+ L_MRNVL +",";
						M_strSQLQRY += getUSGDTL("MR",'U',"5");
						M_strSQLQRY += " where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_MMSBS = '" + M_strSBSCD + "'";
						M_strSQLQRY += " and MR_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
						M_strSQLQRY += " and MR_MATTP = '" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "'";
						M_strSQLQRY += " and MR_MRNNO = '" + txtMRNNO.getText().trim() + "'";
						M_strSQLQRY += " and MR_MATCD = '" + tblMRNDT.getValueAt(i,TBL_MATCD).toString() + "'";
						M_strSQLQRY += " and isnull(MR_STSFL,'') <> 'X'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					}
				}
				
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
			{
				for(int i=0;i<tblMRNDT.getRowCount();i++)
				{
					if(tblMRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY = "UPDATE MM_MRMST SET ";
						M_strSQLQRY += getUSGDTL("MR",'U',"X");
						M_strSQLQRY += " where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_MMSBS = '" + M_strSBSCD + "'";
						M_strSQLQRY += " and MR_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
						M_strSQLQRY += " and MR_MATTP = '" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "'";
						M_strSQLQRY += " and MR_MRNNO = '" + txtMRNNO.getText().trim() + "'";
						M_strSQLQRY += " and MR_MATCD = '" + tblMRNDT.getValueAt(i,TBL_MATCD).toString() + "'";
						M_strSQLQRY += " and isnull(MR_STSFL,'') <> '2'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					}
				}
				if(flgRMKFL)							// To update existing record
				{					
					M_strSQLQRY = "Update MM_RMMST set ";
					M_strSQLQRY += getUSGDTL("RM",'U',"X");
					M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
					M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
					M_strSQLQRY += " and RM_DOCTP = '" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "'";
					M_strSQLQRY += " and RM_DOCNO = '" + txtMRNNO.getText().trim() + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
			{
				for(int i=0;i<tblMRNDT.getRowCount();i++)
				{
					if(tblMRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
					 	if(hstITMDT.containsKey(tblMRNDT.getValueAt(i,TBL_MATCD).toString()))
						{
							M_strSQLQRY = "UPDATE MM_MRMST SET ";
							M_strSQLQRY +=" MR_DPTCD ='"+txtDPTCD.getText().trim()+"',";
							M_strSQLQRY +=" MR_ISSNO ='"+tblMRNDT.getValueAt(i,TBL_ISSNO)+"',";
							if(tblMRNDT.getValueAt(i,TBL_RCNFL).toString().equals("true"))
								M_strSQLQRY +=" MR_RCNFL ='Y',";
							else
								M_strSQLQRY +=" MR_RCNFL ='',";
					
							M_strSQLQRY +=" MR_RETQT ="+tblMRNDT.getValueAt(i,TBL_RETQT)+",";
							M_strSQLQRY +=" MR_MRNRT ="+tblMRNDT.getValueAt(i,TBL_MRNRT)+",";;
							M_strSQLQRY += getUSGDTL("MR",'U',null);
							M_strSQLQRY += " where MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_MMSBS = '" + M_strSBSCD + "'";
							M_strSQLQRY += " and MR_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
							M_strSQLQRY += " and MR_MATTP = '" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "'";
							M_strSQLQRY += " and MR_MRNNO = '" + txtMRNNO.getText().trim() + "'";
							M_strSQLQRY += " and MR_MATCD = '" + tblMRNDT.getValueAt(i,TBL_MATCD).toString() + "'";
							M_strSQLQRY += " and isnull(MR_STSFL,'') <> '5'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
						else
							exeINSREC(i);
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				strMRNDT = cl_dat.M_strLOGDT_pbst;
				if(!genMRNNO(cmbMATTP.getSelectedItem().toString().substring(0,1)))
				{
					setMSG("Error in generating MRN Number..",'E');
					setCursor(cl_dat.M_curDFSTS_pbst);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
					for(int i=0;i<tblMRNDT.getRowCount();i++)
					{
						if(tblMRNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
							exeINSREC(i);
					}		
					exeMRNNO(strMRNNO,cmbMATTP.getSelectedItem().toString().substring(0,1));
				
			}
			if((cl_dat.M_flgLCUPD_pbst)&&(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
			{
				strREMDS = txtREMDS.getText().trim();
				// To check whether the remark against the memo no is exists or not
				if(flgRMKFL){
					// To update existing record
					if(strREMDS.length() > 0)
					{
						M_strSQLQRY = "Update MM_RMMST set ";
						M_strSQLQRY += "RM_REMDS = '" + strREMDS + "',";
						M_strSQLQRY += getUSGDTL("RM",'U',"");
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
						M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
						M_strSQLQRY += " and RM_DOCTP = '" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "'";
						M_strSQLQRY += " and RM_DOCNO = '" + txtMRNNO.getText().trim() + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else
					{
						// remark was present but deleted by user
						M_strSQLQRY = "Update MM_RMMST set ";
						M_strSQLQRY += getUSGDTL("RM",'U',"X");
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
						M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
						M_strSQLQRY += " and RM_DOCTP = '" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "'";
						M_strSQLQRY += " and RM_DOCNO = '" + txtMRNNO.getText().trim() + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						
					}
					
				}
				else if(strREMDS.length() > 0){								// To add new record
					M_strSQLQRY = "Insert into MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,";
					M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,";
					M_strSQLQRY += "RM_LUPDT) values (";
					M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+ "',";
					M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
					M_strSQLQRY += "'" + strTRNTP_fn + "',";
					M_strSQLQRY += "'" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "',";
					M_strSQLQRY += "'" + txtMRNNO.getText().trim() + "',";
					M_strSQLQRY += "'" + strREMDS + "',";
					 M_strSQLQRY += getUSGDTL("MR",'I',"0")+")";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					flgRMKFL = false;
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Data saved successfully",'N');
						setCursor(cl_dat.M_curDFSTS_pbst);
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							JOptionPane.showMessageDialog(this,"Please, Note down the MRN No. " + strMRNNO,"MRN No.",JOptionPane.INFORMATION_MESSAGE);
							btnPRNT.setEnabled(true);
						}
						clrCOMP();		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							if(!flgFRWMR)
							if(vtrFRWBY.contains(cl_dat.M_strUSRCD_pbst))
							{
								cmbFRWTO.setEnabled(true);
								btnFRWD.setEnabled(true);
								if(!flgFRWMR)
								setMSG("You can Forward the request now ..",'E');
								cmbFRWTO.requestFocus();
							}
							else
							{
								cmbFRWTO.setEnabled(false);
								btnFRWD.setEnabled(false);
							}
						}
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
					else
					{
						setMSG("Error in saving data..",'E');
						setCursor(cl_dat.M_curDFSTS_pbst);
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
				}
				if(hstITMDT !=null)
					hstITMDT.clear();
				if(hstISSRT !=null)
					hstISSRT.clear();
				if(hstWAVRT !=null)
					hstWAVRT.clear();
				if(vtrMATCD !=null)
					vtrMATCD.removeAllElements();

			}
			else
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			setCursor(cl_dat.M_curDFSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
		}
	}
private boolean exeINSREC(int P_intROWID)
{
	try
	{
		double L_MRNVL = Double.parseDouble(tblMRNDT.getValueAt(P_intROWID,TBL_MRNRT).toString())*Double.parseDouble(tblMRNDT.getValueAt(P_intROWID,TBL_RETQT).toString());
		String L_strRCNFL = "";
		String L_strMATCD = tblMRNDT.getValueAt(P_intROWID,TBL_MATCD).toString();
		if(tblMRNDT.getValueAt(P_intROWID,TBL_RCNFL).toString().equals("true"))
			L_strRCNFL = "Y";
		else
			L_strRCNFL = "";
		M_strSQLQRY = "Insert into MM_MRMST(MR_CMPCD,MR_MMSBS,MR_STRTP,MR_MATTP,MR_MRNNO,MR_MRNDT,MR_DPTCD,MR_MATCD,MR_ORGCD,";
		M_strSQLQRY += "MR_RETQT,MR_ISSNO,MR_TAGNO,MR_MRNRT,MR_MRNVL,MR_RCNFL,MR_PREBY,MR_PRTTP,MR_PRTCD,MR_TRNFL,MR_STSFL,MR_LUSBY,MR_LUPDT) values (";
		M_strSQLQRY += "'"+ cl_dat.M_strCMPCD_pbst + "',";;
		M_strSQLQRY += "'"+ M_strSBSCD + "',";;
		M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
		M_strSQLQRY += "'" + cmbMATTP.getSelectedItem().toString().substring(0,1) + "',";
		M_strSQLQRY += "'" + txtMRNNO.getText().trim() + "',";
		M_strSQLQRY += "'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(strMRNDT))+"',";
		M_strSQLQRY += "'" + txtDPTCD.getText().trim() + "',";
		if(L_strRCNFL.trim().length() >0)
			L_strMATCD = L_strMATCD.substring(0,9)+"8";
		M_strSQLQRY += "'" +L_strMATCD +"',";
		M_strSQLQRY += "'"+ tblMRNDT.getValueAt(P_intROWID,TBL_MATCD).toString() +"',";
		M_strSQLQRY +=  tblMRNDT.getValueAt(P_intROWID,TBL_RETQT).toString() +",";
		M_strSQLQRY += "'" + tblMRNDT.getValueAt(P_intROWID,TBL_ISSNO).toString() +"',";
		M_strSQLQRY += "'" + tblMRNDT.getValueAt(P_intROWID,TBL_TAGNO).toString() +"',";
		M_strSQLQRY +=  tblMRNDT.getValueAt(P_intROWID,TBL_MRNRT).toString() +",";
		M_strSQLQRY +=  L_MRNVL +",";
		if(tblMRNDT.getValueAt(P_intROWID,TBL_RCNFL).toString().equals("true"))
			M_strSQLQRY +=  "'Y',";
		else
			M_strSQLQRY +=  "'',";
		M_strSQLQRY += "'" +cl_dat.M_strUSRCD_pbst +"',";
		M_strSQLQRY +="'"+cmbPRTTP.getSelectedItem().toString().substring(0,1) + "',"; 
		M_strSQLQRY +="'" + txtPRTCD.getText() + "'," ;
		M_strSQLQRY += getUSGDTL("MR",'I',"0")+")";
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"exeINSREC");
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
	return true;
}
void setENBL(boolean L_flgSTAT)
{
	super.setENBL(L_flgSTAT);
	txtMRNDT.setEnabled(false);
	txtAUTDT.setEnabled(false);
	txtPREBY.setEnabled(false);
	txtAUTBY.setEnabled(false);
	txtDPTDS.setEnabled(false);
	btnPRNT.setEnabled(false);
	txtPRTNM.setEnabled(false);
	txtMRNST.setEnabled(false);
//	cmbFRWTO.setEnabled(false);
//	btnFRWD.setEnabled(false);
	txtFRWBY.setEnabled(false);
	tblMRNDT.cmpEDITR[TBL_MATDS].setEnabled(false);
	tblMRNDT.cmpEDITR[TBL_UOMCD].setEnabled(false);
	tblMRNDT.cmpEDITR[TBL_LOCCD].setEnabled(false);
	tblMRNDT.cmpEDITR[TBL_ORGCD].setEnabled(false);
	tblMRNDT.cmpEDITR[TBL_ISSNO].setEnabled(false);
	tblMRNDT.cmpEDITR[TBL_TAGNO].setEnabled(false);
	if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
	{
		cmbMATTP.setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
			tblMRNDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			txtMRNNO.setEnabled(true);
			cmbPRTTP.setEnabled(false);
			txtPRTCD.setEnabled(false);
		//	tblMRNDT.cmpEDITR[TBL_RCNFL].setEnabled(false);
		// Changed on 18/12/2004	
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			   tblMRNDT.cmpEDITR[TBL_RCNFL].setEnabled(false);
		}
		else 
		{
		    txtPRTCD.setEnabled(true);
			cmbPRTTP.setEnabled(true);
			tblMRNDT.cmpEDITR[TBL_RCNFL].setEnabled(true);
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
		{
			tblMRNDT.cmpEDITR[TBL_MRNRT].setEnabled(true);
			tblMRNDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		}
		else
			tblMRNDT.cmpEDITR[TBL_MRNRT].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))		
			btnPRNT.setEnabled(true);
	}
}
private boolean genMRNNO(String P_strMATTP)
	{
	strMRNNO ="";
		String L_MRNNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for MRN;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"MRN' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + P_strMATTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
              	if(M_rstRSSET.next())
				{
					if(L_CHP01.trim().length() >0)
					{
						M_rstRSSET.close();
						setMSG("Record in use ..",'E');
						return false;
					}
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"MRN'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + P_strMATTP + "'";			
			cl_dat.M_flgLCUPD_pbst = true;
          	cl_dat.exeSQLUPD(M_strSQLQRY," ");
            if(cl_dat.exeDBCMT("genISSNO"))
		   {
		      	L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
				for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
					L_MRNNO += "0";
			    
				L_CCSVL = L_MRNNO + L_CCSVL;
				L_MRNNO = L_CODCD + L_CCSVL;
				txtMRNNO.setText(L_MRNNO);
				strMRNNO = L_MRNNO;
			}
			else return false;;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genISSNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
		return true;
	}
	// Method to update the last Issue No.in the CO_CDTRN
	private void exeMRNNO(String P_strMRNNO,String P_strMATTP)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + P_strMRNNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"MRN'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + P_strMATTP + "'";			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeISSNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
private class mm_temrnTBLINVFR extends TableInputVerifier
{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			String L_strTEMP ="";
			try
			{
				if(P_intCOLID==TBL_MATCD)
				{
				//	if(tblMRNDT.isEditing())
				//		tblMRNDT.getCellEditor().stopCellEditing();
					strTEMP = tblMRNDT.getValueAt(P_intROWID,TBL_MATCD).toString();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						if(P_intROWID <intRECCT)
						{
							if(!strTEMP.equals(vtrMATCD.elementAt(P_intROWID).toString()))
						    {
								setMSG("Item Code can not be changed..",'E');
								tblMRNDT.setValueAt(vtrMATCD.elementAt(P_intROWID).toString(),P_intROWID,TBL_MATCD);
								return false;
							}
						}
					}
					if(strTEMP !=null)
					if(strTEMP.length()== 10)
					{
						for(int i=0;i<=P_intROWID-1;i++)
						{
							if(tblMRNDT.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
							if(tblMRNDT.getValueAt(i,TBL_MATCD).toString().trim().equals(strTEMP.trim()))
							{
								setMSG("Duplicate entry ..",'E');
								return false;
							}
						}
						if(!vldMATCD(strTEMP,P_intROWID))
						{
							return false;
						}
					}
				}
				if(P_intCOLID==TBL_MRNRT)
				{
					L_strTEMP = hstWAVRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()).toString();
					if(L_strTEMP !=null)
					{
						if(Double.parseDouble(L_strTEMP) >0)
							tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
					}
				}
				if(P_intCOLID==TBL_ISSNO)
				{
				}
				if(P_intCOLID==TBL_RCNFL)
				{
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
					{
						//Working in vlddata, not working here
						if(hstITMDT.get(tblMRNDT.getValueAt(P_intROWID,TBL_MATCD).toString()) !=null)
						{
							if(tblMRNDT.getValueAt(P_intROWID,TBL_RCNFL).toString().equals("true"))
								L_strTEMP ="Y";
							else
								L_strTEMP ="";
							if(!hstITMDT.get(tblMRNDT.getValueAt(P_intROWID,TBL_MATCD).toString()).toString().equals(L_strTEMP))
							{
					            setMSG("Modification of Reconditioned Tag is not allowed ",'E');
								if(hstITMDT.get(tblMRNDT.getValueAt(P_intROWID,TBL_MATCD).toString()).toString().equals("Y"))
									tblMRNDT.setValueAt(Boolean.TRUE,P_intROWID,TBL_RCNFL);
								else
									tblMRNDT.setValueAt(Boolean.FALSE,P_intROWID,TBL_RCNFL);
								return false;
							}
						}
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
					{
						if(hstITMDT.containsKey(tblMRNDT.getValueAt(P_intROWID,TBL_MATCD).toString()))
						   return true;
					}
					if(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_RCNFL).toString().equals("true"))
					{
						if(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString().length() >0)
						{
							tblMRNDT.setValueAt("0.01",tblMRNDT.getSelectedRow(),TBL_MRNRT);			
						}
					}
					else
					{
						if(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_ISSNO).toString().length()> 0)
						{
							L_strTEMP = null;
							//if(hstISSRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_ISSNO).toString()+tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()) != null)
							if(hstISSRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()) != null)
								L_strTEMP = hstISSRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()).toString();
							if(L_strTEMP !=null)
							{
								if(Double.parseDouble(L_strTEMP) >0)
									tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
							}
							else
							{
								L_strTEMP = hstWAVRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()).toString();
								if(L_strTEMP !=null)
								{
									//if(!L_strTEMP.equals("-1"))
										tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
								}
							}
								
						}
						else
						{
								L_strTEMP = hstWAVRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()).toString();
								if(L_strTEMP !=null)
									//if(!L_strTEMP.equals("-1"))
									tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
						
						}
					}
				}
				if(P_intCOLID==TBL_RETQT)
				{
					
					if(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_RCNFL).toString().equals("true"))
					{
						if(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString().length() >0)
						{
							tblMRNDT.setValueAt("0.01",tblMRNDT.getSelectedRow(),TBL_MRNRT);			
						}
					}
					else
					{
						if(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_ISSNO).toString().length()> 0)
						{
							L_strTEMP = null;
							//if(hstISSRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_ISSNO).toString()+tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()) != null)
							if(hstISSRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()) != null)
								L_strTEMP = hstISSRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()).toString();
							if(L_strTEMP !=null)
							{
								if(Double.parseDouble(L_strTEMP) >0)
									tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
							}
							else
							{
								L_strTEMP = hstWAVRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()).toString();
								if(L_strTEMP !=null)
								{
									//if(!L_strTEMP.equals("-1"))
										tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
								}
							}
								
						}
						else
						{
								L_strTEMP = hstWAVRT.get(tblMRNDT.getValueAt(tblMRNDT.getSelectedRow(),TBL_MATCD).toString()).toString();
								if(L_strTEMP !=null)
									//if(!L_strTEMP.equals("-1"))
									tblMRNDT.setValueAt(L_strTEMP,tblMRNDT.getSelectedRow(),TBL_MRNRT);
						
						}
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
			}
			return true;
		}
		
}
private void getWAVRT(String P_strMATCD,int P_intROWID)
{
	String L_strWAVRT="",L_strPISNO="";
	ResultSet L_rstWAVRT;
	try
	{
		M_strSQLQRY ="Select STP_WAVRT from MM_STPRC where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
		M_strSQLQRY +=" AND STP_MATCD ='"+P_strMATCD+"'";
		L_rstWAVRT = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(L_rstWAVRT !=null)
			if(L_rstWAVRT.next())
			{
			//	L_strPISNO = nvlSTRVL(M_rstRSSET.getString("ST_PISNO"),"");
			//	tblMRNDT.setValueAt(L_strPISNO,P_intROWID,TBL_ISSNO);
				L_strWAVRT = nvlSTRVL(L_rstWAVRT.getString("STP_WAVRT"),"0");
				tblMRNDT.setValueAt(L_strWAVRT,P_intROWID,TBL_MRNRT);
				hstWAVRT.put(P_strMATCD,L_strWAVRT);
			}
			else
			{
				tblMRNDT.setValueAt("0",P_intROWID,TBL_MRNRT);
				hstWAVRT.put(P_strMATCD,"0");
			}
		if(L_rstWAVRT !=null)
			L_rstWAVRT.close();
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"error");
	}
	
}
private void getITMDT(String P_strMATCD,String P_strORGCD,int P_intROWID)
{
	String L_strMATCD="";
	ResultSet L_rstRSSET;
	boolean L_flgFND = false;
	try
	{
		M_strSQLQRY ="Select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST where ct_codtp ='CD' and ct_matcd in('" +P_strMATCD +"','"+P_strORGCD+"') order by ct_matcd";
		L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if(L_rstRSSET !=null)
			while(L_rstRSSET.next())
			{
				L_strMATCD = nvlSTRVL(L_rstRSSET.getString("CT_MATCD"),"");
				if(L_strMATCD.equals(P_strMATCD))
				{
					tblMRNDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("CT_MATDS"),""),P_intROWID,TBL_MATDS);
					tblMRNDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("CT_UOMCD"),""),P_intROWID,TBL_UOMCD);
					L_flgFND = true;
				}
				else
				{
					if(!L_flgFND)
					{
						if(L_strMATCD.equals(P_strORGCD))
						{
							tblMRNDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("CT_MATDS"),""),P_intROWID,TBL_MATDS);
							tblMRNDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("CT_UOMCD"),""),P_intROWID,TBL_UOMCD);
							L_flgFND = true;
						}		
					}
				}
			}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"error");
	}
}
private boolean vldMATCD(String P_strMATCD,int P_intROWID)
{
	try
	{
		strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
		if(strMATTP.equals(strPKGMT_fn))
		{
			if(!P_strMATCD.substring(0,2).equals("69"))
			{
				setMSG("Invalid Material code for Packing category..",'E');	
				return false;
			}
		}
		else if(strMATTP.equals(strRAWMT_fn))
		{
			if(M_strSBSCD.substring(2,4).equals("01"))
			{
				setMSG("Raw Material category not allowed under Engg. stores..",'N');	
				return false;	
			}
			else
			{ 
				if(!P_strMATCD.substring(0,2).equals("68"))
				{
					setMSG("Invalid Material code for Raw Material category..",'E');	
					return false;	
				}
			}
			
		}
		else if(strMATTP.equals(strSTRSP_fn))
		{
			if((P_strMATCD.substring(0,2).equals("68"))||(P_strMATCD.substring(0,2).equals("69")))
			{
				setMSG("Invalid Material code for Stores and Spares category..",'E');	
				return false;	
			}
		}
		M_strSQLQRY = "select CT_MATDS,CT_UOMCD,CT_PISNO from CO_CTMST where";
		M_strSQLQRY += " CT_MATCD = '" + P_strMATCD + "'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
		strPISNO ="";
		if(M_rstRSSET !=null)
		if(M_rstRSSET.next())
		{
			tblMRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),P_intROWID,TBL_UOMCD);
			tblMRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),P_intROWID,TBL_MATDS);
			strPISNO = nvlSTRVL(M_rstRSSET.getString("CT_PISNO"),"");
			tblMRNDT.setValueAt(strPISNO,P_intROWID,TBL_ISSNO);
			setMSG("",'N');
			
			if(P_strMATCD.substring(9).equals("8"))
			{
				tblMRNDT.setValueAt("0.01",P_intROWID,TBL_MRNRT);
				tblMRNDT.setValueAt(Boolean.TRUE,P_intROWID,TBL_RCNFL);
				hstWAVRT.put(P_strMATCD,"0.01");
			}
			else
				getWAVRT(P_strMATCD,P_intROWID);
			M_rstRSSET.close();	
			if(strPISNO.length() >0)
			{
				M_strSQLQRY ="SELECT DISTINCT IS_MATCD,IS_TAGNO,IS_ISSRT FROM MM_ISMST WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_ISSNO ='"+strPISNO+"'"
							+ " AND IS_MATCD ='"+P_strMATCD +"' and isnull(IS_STSFL ,'') <>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						tblMRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IS_TAGNO"),""),P_intROWID,TBL_TAGNO);			
						hstISSRT.put(P_strMATCD,M_rstRSSET.getString("IS_ISSRT"));
					}
			}
			return true;
		}
		if(M_rstRSSET !=null)
			M_rstRSSET.close();	
		setMSG("Invalid Material Code.Press F1 for help",'E');
		if(M_rstRSSET != null)
			M_rstRSSET.close();
	}catch(Exception e){
		setMSG(e,"vldMATCD");
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
		return false;
}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input == txtPRTCD)
				{
					if(txtPRTCD.getText().length() != 5)
					{
						setMSG("Invalid Party Code..",'E');
						return false;
					}
					else
					{	
						M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='"+cmbPRTTP.getSelectedItem().toString().substring(0,1) +"'";
						M_strSQLQRY +=" AND PT_PRTCD ='"+txtPRTCD.getText().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET !=null)
							if(M_rstRSSET.next())
							{
								txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
							}
							else
							{
								setMSG("Invalid Party Code,Press F1 to select from List..",'E');
								return false;
							}
					}
					
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;	
		}
	}
}