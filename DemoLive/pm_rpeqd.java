import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Font;
import java.util.StringTokenizer;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.InputVerifier;
import java.sql.ResultSet;
import java.util.Hashtable;
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.util.HSSFColor;

class pm_rpeqd extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"pm_rpeqd.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    private JTextField txtTAGNO,txtTAGDS,txtEQPID,txtEQPDS;
	private JTextField txtPLNCD,txtPLNDS;
	private JTextField txtARACD,txtARADS;
	private JTextField txtCRICD,txtCRIDS;
	private JTextField txtEQMTP,txtEQSTP,txtEQMTP_DS,txtEQSTP_DS;
	private JTextField txtDPTCD,txtDPTNM;
    private String strDOTLN = "-------------------------------------------------------------------------------------------------------------------------------";
    private JComboBox cmbORDBY;
    private JButton btnGENRP;
    private JTabbedPane tbpMAIN;
    private JPanel pnlDTLRPT,pnlSUMRPT;
    
    Hashtable<String,String[]> hstCDTRN;
	Hashtable<String,String[]> hstCTMST;
	
	private int intCDTRN_TOT = 2;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;	
   
    private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;
	
	private  cl_JTable tblEQPDL,tblEQPSM; 
	private INPVF oINPVF;
	
    int TB1_CHKFL = 0; 				
    int TB1_EQPID = 1;          
	int TB1_EMSTP = 2;              
	int TB1_PACCD = 3;             
	int TB1_DRGNO = 4;              
	int TB1_MFRCD = 5;            
	int TB1_PORNO = 6;              
	int TB1_HRSRN = 7;             
	
	int TB2_CHKFL = 0; 				
    int TB2_TAGNO = 1;            
    int TB2_TAGDS = 2;             
	int TB2_EQPID = 3;         
	int TB2_EQPDS= 4;  
	
	/*HSSFRow hssfROW;
	HSSFCell hssfCELL,hssfCELL1,hssfCELL2,hssfCELL3,hssfCELL4,hssfCELL5,hssfCELL6,hssfCELL7,hssfCELL8,hssfCELL9,hssfCELL10,hssfCELL11,hssfCELL12,hssfCELL13,hssfCELL14,hssfCELL15,hssfCELL16,hssfCELL17,hssfCELL18;
	HSSFCellStyle cellSTYLE,cellSTYLE1;
    HSSFFont hssfFONT;
    HSSFWorkbook wrkBOOK ;
    HSSFSheet hssSHEET;*/
	int intROWNO=0,intROWNO1=0;
	
	private JCheckBox chkEXCEL;
	pm_rpeqd()		/*  Constructor   */
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
			
			
			add(new JLabel("Tag No :"),1,1,1,2,this,'L');
			add(txtTAGNO = new TxtLimit(15),1,3,1,2,this,'L');
			add(txtTAGDS = new TxtLimit(50),1,5,1,4,this,'L');
		
			add(new JLabel("Equipment Id :"),1,10,1,2,this,'L');
			add(txtEQPID = new TxtLimit(15),1,12,1,2,this,'L');
			add(txtEQPDS = new TxtLimit(50),1,14,1,4,this,'L');
			
			add(new JLabel("Eqpt Type:"),2,1,1,2,this,'L');  
			add(txtEQMTP = new TxtLimit(3),2,3,1,2,this,'L'); 
			add(txtEQMTP_DS = new TxtLimit(50),2,5,1,4,this,'L'); 
			
			add(new JLabel("Sub Type:"),2,10,1,2,this,'L');  
			add(txtEQSTP = new TxtLimit(3),2,12,1,2,this,'L');
			add(txtEQSTP_DS = new TxtLimit(50),2,14,1,4,this,'L');
			
			add(new JLabel("Plant Code :"),3,1,1,2,this,'L');
			add(txtPLNCD = new TxtLimit(3),3,3,1,2,this,'L');
			add(txtPLNDS = new TxtLimit(10),3,5,1,4,this,'L');
					
			add(new JLabel("Area Code :"),3,10,1,2,this,'L');
			add(txtARACD = new TxtLimit(3),3,12,1,2,this,'L');
			add(txtARADS = new TxtLimit(10),3,14,1,4,this,'L');
			
			add(new JLabel("Criticality :"),4,1,1,2,this,'L');
			add(txtCRICD = new TxtLimit(1),4,3,1,2,this,'L');
			add(txtCRIDS = new TxtLimit(5),4,5,1,4,this,'L');
			
			add(new JLabel("Department:"),4,10,1,2,this,'L');  
			add(txtDPTCD = new TxtLimit(3),4,12,1,2,this,'L');
			add(txtDPTNM = new TxtLimit(15),4,14,1,4,this,'L');
						
			add(new JLabel("Order By"),5,1,1,2,this,'L');
    		add(cmbORDBY = new JComboBox(),5,3,1,4,this,'L');
    		cmbORDBY.addItem("Tag No");
			cmbORDBY.addItem("Equipment Id");
			cmbORDBY.addItem("Eqpt Main Type");
			cmbORDBY.addItem("Eqpt Sub Type");
			cmbORDBY.addItem("Plant code");
			cmbORDBY.addItem("Area code");
			cmbORDBY.addItem("Department");
			cmbORDBY.addItem("Manufacture code");
			cmbORDBY.addItem("Material Group");
			cmbORDBY.addItem("P.O. No");
			cmbORDBY.addItem("Installed Date");
			
			add(btnGENRP= new JButton("Generate"),5,14,1,2,this,'L');
			
			add(chkEXCEL=new JCheckBox("Excel"),5,17,1,2,this,'L');
			
			tbpMAIN = new JTabbedPane();
			pnlDTLRPT = new JPanel(null);
			pnlSUMRPT = new JPanel(null);
			
			String[] L_strTBLHD = {"","Eqpt Id/Description/Dept","Eqpt Type/Description/Sub Type/Description","Plant/Area/Criticality","Drawing No/Installed Date/Material Gr./Description","Mfr Code/Nmae","P.O No/P.O.Date/P.O.Value","Hrs Run/Runlog Ind/Interchange Id/Mint cost"};
    		int[] L_intCOLSZ = {10,200,200,150,100,100,150,150};
    		tblEQPDL= crtTBLPNL1(pnlDTLRPT,L_strTBLHD,16000,1,1,10,19,L_intCOLSZ,new int[]{0});
    		
    		String[] L_strTBLHD1 = {"","Tag No","Desription","Equipment Id","Description"};
    		int[] L_intCOLSZ1 = {10,90,350,90,350};
    		tblEQPSM= crtTBLPNL1(pnlSUMRPT,L_strTBLHD1,3000,1,1,10,19,L_intCOLSZ1,new int[]{0});
			
    		tbpMAIN.addTab("Detail",pnlDTLRPT);
			tbpMAIN.addTab("Summary",pnlSUMRPT);
			
			add(tbpMAIN,7,1,13,19,this,'L');
    		
			txtTAGNO.setInputVerifier(oINPVF);
			txtEQPID.setInputVerifier(oINPVF);
			txtEQMTP.setInputVerifier(oINPVF);
			txtEQSTP.setInputVerifier(oINPVF);
			txtPLNCD.setInputVerifier(oINPVF);
			txtARACD.setInputVerifier(oINPVF);
			txtCRICD.setInputVerifier(oINPVF);
			txtDPTCD.setInputVerifier(oINPVF);
			
		    M_pnlRPFMT.setVisible(true);
			setENBL(true);
			
			hstCDTRN = new Hashtable<String,String[]>();
			hstCDTRN.clear();
			
			crtCDTRN("'MSTPMXXPLN','MSTPMXXARA','MSTPMXXCRT', 'MSTPMXXEQT','MSTPMXXEST','SYSCOXXDPT','MSTPMXXRSN','MSTPMXXFLR'","",hstCDTRN);
		
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
				txtTAGDS.setEnabled(false);
				txtEQPDS.setEnabled(false);
				txtEQMTP_DS.setEnabled(false);
				txtEQSTP_DS.setEnabled(false);
				txtPLNDS.setEnabled(false);
				txtARADS.setEnabled(false);
				txtCRIDS.setEnabled(false);
				txtDPTNM.setEnabled(false);
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtTAGNO.requestFocus();
				}
			}
			else if(M_objSOURC ==btnGENRP)
			{
				getDATA();
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	/** Method to request Focus in all TextField of component,when press ENTER & Display help screen when press F1 **/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtTAGNO)
				{
					txtTAGNO.setText(txtTAGNO.getText().toUpperCase());
					txtEQPID.requestFocus();
					setMSG("Enter Equipment Id or Press F1 to Select from List..",'N');
					if(txtTAGNO.getText().length()==0)
						txtTAGDS.setText("");
				}
				else if(M_objSOURC == txtEQPID)
				{
					txtEQPID.setText(txtEQPID.getText().toUpperCase());
					if(txtEQPID.getText().length()==0)
						txtEQPDS.setText("");
					txtEQMTP.requestFocus();
					setMSG("Enter Equipment main type or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtEQMTP)
				{
					txtEQMTP.setText(txtEQMTP.getText().toUpperCase());
					if(txtEQMTP.getText().length()==0)
						txtEQMTP_DS.setText("");
					txtEQSTP.requestFocus();
					setMSG("Enter Equipment Sub Type or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtEQSTP)
				{
					txtEQSTP.setText(txtEQSTP.getText().toUpperCase());
					if(txtEQSTP.getText().length()==0)
						txtEQSTP_DS.setText("");
					txtPLNCD.requestFocus();
					setMSG("Enter Plant code or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtPLNCD)
				{
					txtPLNCD.setText(txtPLNCD.getText().toUpperCase());
					if(txtPLNCD.getText().length()==0)
						txtPLNDS.setText("");
					txtARACD.requestFocus();
					setMSG("Enter Area code or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtARACD)
				{
					txtARACD.setText(txtARACD.getText().toUpperCase());
					if(txtARACD.getText().length()==0)
						txtARADS.setText("");
					txtCRICD.requestFocus();
					setMSG("Enter Criticality code or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtCRICD)
				{
					txtCRICD.setText(txtCRICD.getText().toUpperCase());
					if(txtCRICD.getText().length()==0)
						txtCRIDS.setText("");
					txtDPTCD.requestFocus();
					setMSG("Enter Dept code or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtDPTCD)
				{
					txtDPTCD.setText(txtDPTCD.getText().toUpperCase());
					if(txtDPTCD.getText().length()==0)
						txtDPTNM.setText("");
					cmbORDBY.requestFocus();
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
				if(M_objSOURC == txtTAGNO )
				{
					M_strHLPFLD = "txtTAGNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT distinct TG_TAGNO,TG_TAGDS from PM_TGMST";
					M_strSQLQRY+= " where TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
					if(txtTAGNO.getText().length()>0)				
						M_strSQLQRY+= " AND TG_TAGNO like '"+txtTAGNO.getText().trim()+"%'";
					M_strSQLQRY+= " order by TG_TAGNO ";
					//System.out.println("txtTAGNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Tag No","Tag Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtEQPID)
				{
						M_strHLPFLD = "txtEQPID";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
					
						M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS from PM_EQMST";
						M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EQ_STSFL,'')<>'X'";
						if(txtTAGNO.getText().length()>0)		
							M_strSQLQRY+= " AND EQ_TAGNO='"+txtTAGNO.getText()+"'";
						if(txtEQPID.getText().length()>0)				
							M_strSQLQRY+= " AND EQ_EQPID like '"+txtEQPID.getText().trim()+"%'";
						M_strSQLQRY+= " order by EQ_EQPID";
						//System.out.println("EQPT_ID>>"+M_strSQLQRY);
					
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Id","Equipment Description"},2,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
				}

				else if(M_objSOURC == txtARACD)
				{
					M_strHLPFLD = "txtARACD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXARA' AND isnull(CMT_STSFL,'')<>'X' ";
					if(txtARACD.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtARACD.getText().trim()+"%'";
					M_strSQLQRY+= " order by cmt_codcd";
					//System.out.println("txtARACD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Area Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				else if(M_objSOURC == txtPLNCD)
				{
					M_strHLPFLD = "txtPLNCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXPLN' AND isnull(CMT_STSFL,'')<>'X'";
					if(txtPLNCD.getText().length()>0)				
						M_strSQLQRY+=  " AND cmt_codcd like '"+txtPLNCD.getText().trim()+"%'";
					M_strSQLQRY+= " order by cmt_codcd";
					//System.out.println("txtPLNCD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Plant Code","Description"},2,"CT");
				//	cl_hlp(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN),2,1,new String[]{"Plant Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
					 
				}	
				else if(M_objSOURC == txtCRICD)
				{
					M_strHLPFLD = "txtCRICD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXCRT' AND isnull(CMT_STSFL,'')<>'X'";
					if(txtCRICD.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtCRICD.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Criticality Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				else if(M_objSOURC == txtEQMTP)
				{
					M_strHLPFLD = "txtEQMTP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXEQT' AND isnull(CMT_STSFL,'')<>'X' ";
					if(txtEQMTP.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtEQMTP.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Type","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				else if(M_objSOURC == txtEQSTP)
				{
					M_strHLPFLD = "txtEQSTP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXEST' AND isnull(CMT_STSFL,'')<>'X'";
					if(txtEQSTP.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtEQSTP.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Sub Type","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC==txtDPTCD)		
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
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"keypressed");		
		}		
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{
			if(M_strHLPFLD.equals("txtEQPID"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEQPID.setText(L_STRTKN.nextToken());
				txtEQPDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtTAGNO"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtTAGNO.setText(L_STRTKN.nextToken());
				 txtTAGDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtPLNCD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtPLNCD.setText(L_STRTKN.nextToken());
				 txtPLNDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtARACD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtARACD.setText(L_STRTKN.nextToken());
				 txtARADS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtCRICD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtCRICD.setText(L_STRTKN.nextToken());
				 txtCRIDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtEQMTP"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtEQMTP.setText(L_STRTKN.nextToken());
				 txtEQMTP_DS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtEQSTP"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtEQSTP.setText(L_STRTKN.nextToken());
				 txtEQSTP_DS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtDPTCD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtDPTCD.setText(L_STRTKN.nextToken());
				 txtDPTNM.setText(L_STRTKN.nextToken());
			}
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}
	/**
	 * Method to get Data from pm_eqmst to display Equipment detail records */
	private void getDATA() 
	{
		try
		{ 
			tblEQPDL.clrTABLE();
			inlTBLEDIT(tblEQPDL);
			tblEQPSM.clrTABLE();
			inlTBLEDIT(tblEQPSM);
			
			String L_strTAGNO_NEW="",L_strTAGNO_OLD="";
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			intROWNO=0;
			intROWNO1=0;
			setMSG("Fetching Records ...",'N');
			String L_strDPTCD="",L_strMFRCD="",L_strEQMTP="",L_strEQSTP="",L_strMATGR="",L_strMATDS="";
		
			M_strSQLQRY= " SELECT EQ_TAGNO,EQ_EQPID,EQ_EQPDS,EQ_EQMTP,EQ_EQSTP,EQ_PLNCD,EQ_ARACD,EQ_DPTCD,EQ_DRGNO,EQ_MATGR,EQ_MFRCD,EQ_RNIND,EQ_ICHCD,EQ_PORNO,EQ_PORDT,EQ_PORVL,EQ_HRSRN,EQ_INSDT,EQ_MNTVL,TG_TAGDS,TG_CRICD";
			M_strSQLQRY+= " FROM PM_EQMST,PM_TGMST";
			M_strSQLQRY+= " where EQ_TAGNO=TG_TAGNO AND EQ_CMPCD=TG_CMPCD AND EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EQ_STSFL,'')<>'X'";
			if(txtTAGNO.getText().length()>0)
				M_strSQLQRY+= " and EQ_TAGNO='"+txtTAGNO.getText()+"'";
			if(txtEQPID.getText().length()>0)
				M_strSQLQRY+= " and EQ_EQPID='"+txtEQPID.getText()+"'";
			if(txtEQMTP.getText().length()>0)
				M_strSQLQRY+= " and EQ_EQMTP='"+txtEQMTP.getText()+"'";
			if(txtEQSTP.getText().length()>0)
				M_strSQLQRY+= " and EQ_EQSTP='"+txtEQSTP.getText()+"'";
			if(txtPLNCD.getText().length()>0)
				M_strSQLQRY+= " and EQ_PLNCD='"+txtPLNCD.getText()+"'";
			if(txtARACD.getText().length()>0)
				M_strSQLQRY+= " and EQ_ARACD='"+txtARACD.getText()+"'";
			if(txtCRICD.getText().length()>0)
				M_strSQLQRY+= " and TG_CRICD='"+txtCRICD.getText()+"'";
			if(txtDPTCD.getText().length()>0)
				M_strSQLQRY+= " and EQ_DPTCD='"+txtDPTCD.getText()+"'";
			
			if(cmbORDBY.getSelectedItem().toString().equals("Tag No"))
				M_strSQLQRY+=" order by EQ_TAGNO"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Equipment Id"))
				M_strSQLQRY+=" order by EQ_EQPID"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Eqpt Main Type"))
				M_strSQLQRY+=" order by EQ_EQMTP"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Eqpt Sub Type"))
				M_strSQLQRY+=" order by EQ_EQSTP";
			else if(cmbORDBY.getSelectedItem().toString().equals("Plant Code"))
				M_strSQLQRY+=" order by EQ_PLNCD"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Area Code"))
				M_strSQLQRY+=" order by EQ_ARACD"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Department"))
				M_strSQLQRY+=" order by EQ_DPTCD"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Manufacture Code"))
				M_strSQLQRY+=" order by EQ_MFRCD"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Material Group"))
				M_strSQLQRY+=" order by EQ_MATGR"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("P.O. No"))
				M_strSQLQRY+=" order by EQ_PORNO"; 
			else if(cmbORDBY.getSelectedItem().toString().equals("Installed Date"))
				M_strSQLQRY+=" order by EQ_INSDT"; 
			
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strEQMTP=nvlSTRVL(M_rstRSSET.getString("EQ_EQMTP"),"");
					L_strEQSTP=nvlSTRVL(M_rstRSSET.getString("EQ_EQSTP"),"");
					L_strDPTCD=nvlSTRVL(M_rstRSSET.getString("EQ_DPTCD"),"");
					L_strMATGR=nvlSTRVL(M_rstRSSET.getString("EQ_MATGR"),"");
					L_strMFRCD=nvlSTRVL(M_rstRSSET.getString("EQ_MFRCD"),"");
					
					L_strTAGNO_NEW =M_rstRSSET.getString("EQ_TAGNO");
					
					if(!L_strTAGNO_NEW.equals(L_strTAGNO_OLD))
					{
						L_strTAGNO_OLD = L_strTAGNO_NEW;
						
						intROWNO+=1;
						tblEQPDL.setValueAt("Tag No:"+L_strTAGNO_NEW+":"+M_rstRSSET.getString("TG_TAGDS"),intROWNO,TB1_EQPID);
						tblEQPDL.setCellColor(intROWNO,TB1_EQPID,Color.red);
						
						tblEQPSM.setValueAt(M_rstRSSET.getString("EQ_TAGNO"),intROWNO1,TB2_TAGNO);
						tblEQPSM.setValueAt(M_rstRSSET.getString("TG_TAGDS"),intROWNO1,TB2_TAGDS);
					}
					tblEQPSM.setValueAt(M_rstRSSET.getString("EQ_EQPID"),intROWNO1,TB2_EQPID);
					tblEQPSM.setValueAt(M_rstRSSET.getString("EQ_EQPDS"),intROWNO1,TB2_EQPDS);
					
					intROWNO+=1;
					tblEQPDL.setValueAt(M_rstRSSET.getString("EQ_EQPID"),intROWNO,TB1_EQPID);
					tblEQPDL.setCellColor(intROWNO,TB1_EQPID,Color.blue);
					tblEQPDL.setValueAt(L_strEQMTP,intROWNO,TB1_EMSTP);
					tblEQPDL.setValueAt(!M_rstRSSET.getString("EQ_PLNCD").equals("")?"("+M_rstRSSET.getString("EQ_PLNCD")+")"+getCDTRN("MSTPMXXPLN"+M_rstRSSET.getString("EQ_PLNCD").toString().trim(),"CMT_CODDS",hstCDTRN):"-",intROWNO,TB1_PACCD);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_DRGNO"),"-"),intROWNO,TB1_DRGNO);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_MFRCD"),"-"),intROWNO,TB1_MFRCD);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_PORNO"),"-"),intROWNO,TB1_PORNO);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_HRSRN"),"-"),intROWNO,TB1_HRSRN);
					
					intROWNO+=1;
					tblEQPDL.setValueAt(M_rstRSSET.getString("EQ_EQPDS"),intROWNO,TB1_EQPID);
					tblEQPDL.setValueAt(L_strEQMTP.length()>0?getCDTRN("MSTPMXXEQT"+L_strEQMTP,"CMT_CODDS",hstCDTRN):"-",intROWNO,TB1_EMSTP);
					tblEQPDL.setValueAt(!M_rstRSSET.getString("EQ_ARACD").equals("")?"("+M_rstRSSET.getString("EQ_ARACD")+")"+getCDTRN("MSTPMXXARA"+M_rstRSSET.getString("EQ_ARACD").toString().trim(),"CMT_CODDS",hstCDTRN):"-",intROWNO,TB1_PACCD);
					tblEQPDL.setValueAt(!(M_rstRSSET.getDate("EQ_INSDT")==null)?M_fmtLCDAT.format(M_rstRSSET.getDate("EQ_INSDT")):"-",intROWNO,TB1_DRGNO);
					tblEQPDL.setValueAt(!(M_rstRSSET.getDate("EQ_PORDT")==null)?M_fmtLCDAT.format(M_rstRSSET.getDate("EQ_PORDT")):"-",intROWNO,TB1_PORNO);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_RNIND"),"-"),intROWNO,TB1_HRSRN);
					
					intROWNO+=1;
					tblEQPDL.setValueAt(L_strDPTCD.length()>0?getCDTRN("SYSCOXXDPT"+L_strDPTCD,"CMT_CODDS",hstCDTRN):"-",intROWNO,TB1_EQPID);
					tblEQPDL.setValueAt(nvlSTRVL(L_strEQSTP,"-"),intROWNO,TB1_EMSTP);
					tblEQPDL.setValueAt(!M_rstRSSET.getString("TG_CRICD").equals("")?"("+M_rstRSSET.getString("TG_CRICD")+")"+getCDTRN("MSTPMXXCRT"+M_rstRSSET.getString("TG_CRICD").toString().trim(),"CMT_CODDS",hstCDTRN):"-",intROWNO,TB1_PACCD);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_MATGR"),"-"),intROWNO,TB1_DRGNO);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_PORVL"),"-"),intROWNO,TB1_PORNO);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_ICHCD"),"-"),intROWNO,TB1_HRSRN);
					
					intROWNO+=1;
					tblEQPDL.setValueAt(L_strEQSTP.length()>0?getCDTRN("MSTPMXXEST"+L_strEQSTP,"CMT_CODDS",hstCDTRN):"-",intROWNO,TB1_EMSTP);
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_MNTVL"),"-"),intROWNO,TB1_HRSRN);
					
					intROWNO+=1;
					intROWNO1++;
				}
			}
			
			M_rstRSSET.close();
			if(tbpMAIN.getSelectedIndex()==0 && intROWNO==0)
				setMSG("No Data Found..",'E');
			else if(tbpMAIN.getSelectedIndex()==1 && intROWNO1==0)
				setMSG("No Data Found..",'E');
			
			/*if(L_strMATGR.length()>0)
            {
	            String L_strSQLQRY1= "select ct_matds from co_ctmst where  substr(ct_matcd,3,8) = '0000000A' and isnull(CT_STSFL,'') <> 'X' ";
	            L_strSQLQRY1+= " AND ct_matcd='"+L_strMATGR+"' ";
	            ResultSet L_rstRSSET1= cl_dat.exeSQLQRY(L_strSQLQRY1);
				//System.out.println(">>>"+L_strSQLQRY1);
				if(L_rstRSSET1 != null && L_rstRSSET1.next())
				{
					tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ct_matds"),""),intROWNO,TB1_DRGNO);
					L_rstRSSET1.close();
				}
            }*/
           
         /*
            if(L_strMFRCD.length()>0)
            {
	            String L_strSQLQRY= "Select PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST where PT_PRTTP='S' ";
	            L_strSQLQRY+= " AND PT_PRTCD='"+L_strMFRCD+"' ";
	            ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY); 
				if(L_rstRSSET != null)
				{
					while( L_rstRSSET.next())
					{
						txtMFRNM.setText(nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""));
						txtADR01.setText(nvlSTRVL(L_rstRSSET.getString("PT_ADR01"),""));
						txtCTYNM.setText(nvlSTRVL(L_rstRSSET.getString("PT_CTYNM"),""));
					}
				}
				 L_rstRSSET.close();
            }*/
			 
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA()"); 
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >64)
				{
				    D_OUT.writeBytes("<P CLASS = \"breakhere\">");
					genRPFTR();
					genRPHDR();
				}
			}
			else
			{
				 if(tbpMAIN.getSelectedIndex()==0)
				 {
					if(cl_dat.M_intLINNO_pbst >38)
					{		
						genRPFTR();
						genRPHDR();			
					}	
				 }
				 else if(tbpMAIN.getSelectedIndex()==1)
				 {
					if(cl_dat.M_intLINNO_pbst >60)
					{		
						genRPFTR();
						genRPHDR();			
					}	
				 }
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
				D_OUT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	
			if(chkEXCEL.isSelected())
	    	{
	    		D_OUT.writeBytes("Tag/Equipment Detail"+"\n");
	    		D_OUT.writeBytes("Date: "+cl_dat.M_strLOGDT_pbst+"\n\n");
			    if(txtTAGNO.getText().length()>0)
			    	D_OUT.writeBytes("Tag No: "+txtTAGNO.getText()+" : "+txtTAGDS.getText());
				if(txtEQPID.getText().length()>0)
					D_OUT.writeBytes("\n"+"Eqpt Id: "+txtEQPID.getText()+" : "+txtEQPDS.getText());
				if(txtEQMTP.getText().length()>0)
					D_OUT.writeBytes("\n"+"Eqpt Type: "+txtEQMTP.getText()+" : "+txtEQMTP_DS.getText());
				if(txtEQSTP.getText().length()>0)
					D_OUT.writeBytes("\n"+"Eqpt Sub Type: "+txtEQSTP.getText()+" : "+txtEQSTP_DS.getText());
				if(txtPLNCD.getText().length()>0)
					D_OUT.writeBytes("\n"+"Plant Code: "+txtPLNCD.getText()+" : "+txtPLNDS.getText());
				if(txtARACD.getText().length()>0)
					D_OUT.writeBytes("\n"+"Area Code: "+txtARACD.getText()+" : "+txtARADS.getText());
				if(txtCRICD.getText().length()>0)
					D_OUT.writeBytes("\n"+"Criticality Code: "+txtCRICD.getText()+" : "+txtCRIDS.getText());
				if(txtDPTCD.getText().length()>0)
					D_OUT.writeBytes("\n"+"Department: "+txtDPTCD.getText()+" : "+txtDPTNM.getText()+"\n");
			    
	    	}
			else
			{
				cl_dat.M_PAGENO +=1;
				crtNWLIN();
	    		prnFMTCHR(D_OUT,M_strBOLD);
	    		D_OUT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-17));
	    		D_OUT.writeBytes("Date: "+cl_dat.M_strLOGDT_pbst+"\n");
	    		D_OUT.writeBytes(padSTRING('R',"Tag/Equipment Detail",strDOTLN.length()-17));
			    D_OUT.writeBytes("Page No: "+cl_dat.M_PAGENO+"\n");
			    if(txtTAGNO.getText().length()>0)
			    	D_OUT.writeBytes(padSTRING('R',"Tag No: "+txtTAGNO.getText()+" : "+txtTAGDS.getText(),55));
				if(txtEQPID.getText().length()>0)
					D_OUT.writeBytes("\n"+padSTRING('R',"Eqpt Id: "+txtEQPID.getText()+" : "+txtEQPDS.getText(),55));
				if(txtEQMTP.getText().length()>0)
					D_OUT.writeBytes("\n"+padSTRING('R',"Eqpt Type: "+txtEQMTP.getText()+" : "+txtEQMTP_DS.getText(),55));
				if(txtEQSTP.getText().length()>0)
					D_OUT.writeBytes("\n"+padSTRING('R',"Eqpt Sub Type: "+txtEQSTP.getText()+" : "+txtEQSTP_DS.getText(),55));
				if(txtPLNCD.getText().length()>0)
					D_OUT.writeBytes("\n"+padSTRING('R',"Plant Code: "+txtPLNCD.getText()+" : "+txtPLNDS.getText(),35));
				if(txtARACD.getText().length()>0)
					D_OUT.writeBytes("\n"+padSTRING('R',"Area Code: "+txtARACD.getText()+" : "+txtARADS.getText(),35));
				if(txtCRICD.getText().length()>0)
					D_OUT.writeBytes("\n"+padSTRING('R',"Criticality Code: "+txtCRICD.getText()+" : "+txtCRIDS.getText(),35));
				if(txtDPTCD.getText().length()>0)
					D_OUT.writeBytes("\n"+padSTRING('R',"Department: "+txtDPTCD.getText()+" : "+txtDPTNM.getText(),40));
			    crtNWLIN();
				
			    prnFMTCHR(D_OUT,M_strNOBOLD);
	    	}
			
		   
		    if(tbpMAIN.getSelectedIndex()==0)
			{
		    	if(chkEXCEL.isSelected())
		    	{
		    		 D_OUT.writeBytes("Eqpt Id\tEqpt Type\tPlant\tDrawing No\tMfr Code\tP.O. No\tHrs Run\n");
		    		 D_OUT.writeBytes("Description\tDescription\tArea\tInstalled Date\t\tP.O. Date\tRunlog Ind\n");
		    		 D_OUT.writeBytes("Department\tSub Type\tCriticality\tMaterial Gr.\t\tP.O. Value\tInterchange Id\n");
		    		 D_OUT.writeBytes("\tDescription\t\t\t\t\tMint Cost\n");
		    	}
		    	else
		    	{
			    	D_OUT.writeBytes(strDOTLN+"\n");
				    D_OUT.writeBytes("Eqpt Id                       Eqpt Type               Plant                     Drawing No      Mfr Code P.O. No    Hrs Run     \n");                     
				    D_OUT.writeBytes("Description                   Description             Area                      Installed Date           P.O. Date  Runlog Ind  \n");  
				    D_OUT.writeBytes("Department                    Sub Type                Criticality               Material Gr.             P.O. Value InterchngId \n");                       
				    D_OUT.writeBytes("                              Description                                                                           Mint Cost   \n");
				    D_OUT.writeBytes(strDOTLN); 
		    	}
		    	
			}
		    else if(tbpMAIN.getSelectedIndex()==1)
		    {
		    	if(chkEXCEL.isSelected())
			    	D_OUT.writeBytes("Tag No\tTag Description\tEqpt Id\tEqpt Description\n");
		    	else
		    	{
		    		D_OUT.writeBytes("-----------------------------------------------------------------------------------------------\n");
			    	D_OUT.writeBytes("Tag No         Tag Description               Eqpt Id             Eqpt Description              \n");
			    	D_OUT.writeBytes("-----------------------------------------------------------------------------------------------\n");
		    	}
		    }
		   
    		crtNWLIN();
			
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
			if(chkEXCEL.isSelected())
				 D_OUT.writeBytes("\n");  
			else
			{
			  crtNWLIN();
			  if(tbpMAIN.getSelectedIndex()==0)
				  D_OUT.writeBytes(strDOTLN);  
			  else if(tbpMAIN.getSelectedIndex()==1)
				  D_OUT.writeBytes("------------------------------------------------------------------------------------------------");
			  crtNWLIN();
			}
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			
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
			
		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA()");
		}
		return true;
	}
	
	void genRPTFL()
	{
		try
		{
		/*
			
			if(M_rdbTEXT.isSelected() && chkEXCEL.isSelected())
			{
				F_OUT=new FileOutputStream(strRPFNM);
				 wrkBOOK = new HSSFWorkbook();
				 hssSHEET = wrkBOOK.createSheet();
				 cellSTYLE = wrkBOOK.createCellStyle();
				 cellSTYLE1 = wrkBOOK.createCellStyle();
		
				 hssfCELL = crtHSSFCell(hssSHEET, "Equipment Details", 2,4);
				 hssfFONT = crtHSSFFont(new Font("Tahoma",Font.BOLD,15));
				 hssfFONT.setColor(HSSFColor.BLUE.index);
				 cellSTYLE.setFont(hssfFONT);   
				 hssfCELL.setCellStyle(cellSTYLE);
				
				 crtHSSFCell(hssSHEET, "Eqpt Id",4,1);
				 crtHSSFCell(hssSHEET, "Eqpt Description",4,2);
				 crtHSSFCell(hssSHEET, "Eqpt Type", 4,3);
				 crtHSSFCell(hssSHEET, "Sub Type",4,4);
				 crtHSSFCell(hssSHEET, "Plant",4,5);
				 crtHSSFCell(hssSHEET, "Area",4,6);
				 crtHSSFCell(hssSHEET, "Department",4,7);
				 crtHSSFCell(hssSHEET, "Drawing No",4,8);
				 crtHSSFCell(hssSHEET, "Installed Date",4,9);
				 crtHSSFCell(hssSHEET, "Material Gr.",4,10);
				 crtHSSFCell(hssSHEET, "Mfr Code",4,11);
				 crtHSSFCell(hssSHEET, "P.O. No",4,12);
				 crtHSSFCell(hssSHEET, "P.O. Date",4,13);
				 crtHSSFCell(hssSHEET, "P.O. Value",4,14);
				 crtHSSFCell(hssSHEET, "Hrs Run",4,15);
				 crtHSSFCell(hssSHEET, "Runlog Ind",4,16);
				 crtHSSFCell(hssSHEET, "Interchange Id",4,17);
				 crtHSSFCell(hssSHEET, "Mint Cost",4,18);
	   
			
				 if(tbpMAIN.getSelectedIndex()==0)
				 	exeEXCEL(tblEQPDL,hssSHEET);
				 else if(tbpMAIN.getSelectedIndex()==1)
				 	exeEXCEL(tblEQPSM,hssSHEET);
					
				 wrkBOOK.write(F_OUT);
				 F_OUT.close();
			}
			else
			{*/
				F_OUT=new FileOutputStream(strRPFNM);
				D_OUT=new DataOutputStream(F_OUT); 
				genRPHDR();	
				
				
				
				if(tbpMAIN.getSelectedIndex()==0)
				{
					for(int i=0;i<intROWNO;i++)
					{
						if(chkEXCEL.isSelected())
						{
							D_OUT.writeBytes(tblEQPDL.getValueAt(i,TB1_EQPID).toString()+"\t");
							D_OUT.writeBytes(tblEQPDL.getValueAt(i,TB1_EMSTP).toString()+"\t");
							D_OUT.writeBytes(tblEQPDL.getValueAt(i,TB1_PACCD).toString()+"\t");
							D_OUT.writeBytes(tblEQPDL.getValueAt(i,TB1_DRGNO).toString()+"\t");
							D_OUT.writeBytes(tblEQPDL.getValueAt(i,TB1_MFRCD).toString()+"\t");
							D_OUT.writeBytes(tblEQPDL.getValueAt(i,TB1_PORNO).toString()+"\t");
							D_OUT.writeBytes(tblEQPDL.getValueAt(i,TB1_HRSRN).toString()+"\n");
						}
						else
						{
							D_OUT.writeBytes(padSTRING('R',tblEQPDL.getValueAt(i,TB1_EQPID).toString(),30));
							D_OUT.writeBytes(padSTRING('R',tblEQPDL.getValueAt(i,TB1_EMSTP).toString(),23));
							D_OUT.writeBytes(padSTRING('R',tblEQPDL.getValueAt(i,TB1_PACCD).toString(),27));
							D_OUT.writeBytes(padSTRING('R',tblEQPDL.getValueAt(i,TB1_DRGNO).toString(),16));
							D_OUT.writeBytes(padSTRING('R',tblEQPDL.getValueAt(i,TB1_MFRCD).toString(),9));
							D_OUT.writeBytes(padSTRING('R',tblEQPDL.getValueAt(i,TB1_PORNO).toString(),12));
							D_OUT.writeBytes(padSTRING('R',tblEQPDL.getValueAt(i,TB1_HRSRN).toString(),10));
							crtNWLIN();
						}
						
					}
				}
				else if(tbpMAIN.getSelectedIndex()==1)
				{
					for(int i=0;i<intROWNO1;i++)
					{
						if(chkEXCEL.isSelected())
						{
							D_OUT.writeBytes(tblEQPSM.getValueAt(i,TB2_TAGNO).toString()+"\t");
							D_OUT.writeBytes(tblEQPSM.getValueAt(i,TB2_TAGDS).toString()+"\t");
							D_OUT.writeBytes(tblEQPSM.getValueAt(i,TB2_EQPID).toString()+"\t");
							D_OUT.writeBytes(tblEQPSM.getValueAt(i,TB2_EQPDS).toString()+"\n");
						}
						else
						{
							D_OUT.writeBytes(padSTRING('R',tblEQPSM.getValueAt(i,TB2_TAGNO).toString(),15));
							D_OUT.writeBytes(padSTRING('R',tblEQPSM.getValueAt(i,TB2_TAGDS).toString(),30));
							D_OUT.writeBytes(padSTRING('R',tblEQPSM.getValueAt(i,TB2_EQPID).toString(),20));
							D_OUT.writeBytes(padSTRING('R',tblEQPSM.getValueAt(i,TB2_EQPDS).toString(),30));
							crtNWLIN();
						}
						
					}
				}
				 genRPFTR();
				 
				if(M_rdbHTML.isSelected())
				{	
				  D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");   
				}
				D_OUT.close();
				F_OUT.close();
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genRPTFL");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
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
			     strRPFNM = strRPLOC + "pm_rpeqd.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "pm_rpeqd.doc";
			
			if(chkEXCEL.isSelected())
				strRPFNM = strRPLOC + "pm_rpeqd.xls";
			genRPTFL();
			
			if(chkEXCEL.isSelected())
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\excel.exe "+strRPFNM);
				
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if (M_rdbTEXT.isSelected())
					{
					    doPRINT(strRPFNM);
					}	
					else if (M_rdbHTML.isSelected())
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
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	void inlTBLEDIT(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}

	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param   LP_FLDNM        Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
		if (LP_FLDTP.equals("C"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
			//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
		else if (LP_FLDTP.equals("N"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
		else if (LP_FLDTP.equals("D"))
			return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
		else if (LP_FLDTP.equals("T"))
		    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
		else 
			return " ";
		}
		catch (Exception L_EX)
		{setMSG(L_EX,"getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);}
	return " ";
	} 
	
	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	 */
     private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
    {
		String L_strSQLQRY = "";
        try
        {
	        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")   "+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
		  //System.out.println(L_strSQLQRY);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
	            //setMSG("Records not found in CO_CDTRN",'E');
                return;
            }
            while(true)
            {
                    strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                    strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                    strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                    String[] staCDTRN = new String[intCDTRN_TOT];
                    staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                    staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                 
                    LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
                   // System.out.println("LP_HSTNM"+LP_HSTNM);
			
                    if (!L_rstRSSET.next())
                            break;
            }
            L_rstRSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtCDTRN");
        }
	}

	
	/** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
    private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
    {
	//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
    try
    {
			if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
				{setMSG(LP_CDTRN_KEY+" not found in CO_CDTRN hash table",'E'); return " ";}
            if (LP_FLDNM.equals("CMT_CODCD"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
            else if (LP_FLDNM.equals("CMT_CODDS"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
       
    }
	catch (Exception L_EX)
	{
		setMSG(L_EX,"getCDTRN");
	}
    return "";
    }
	
   
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
				return true;
			
			if(input == txtTAGNO)
			{	
				try
				{
					if(txtTAGNO.getText().length()>0)
					{
						M_strSQLQRY=" SELECT distinct TG_TAGNO,TG_TAGDS from PM_TGMST where TG_TAGNO='"+txtTAGNO.getText().toUpperCase()+"'";
						M_strSQLQRY+= " AND TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
						//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							txtTAGNO.setText(txtTAGNO.getText().toUpperCase());
							txtTAGDS.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGDS"),""));
						}
						else
						{
							setMSG("Enter valid Tag No ",'E');
							txtTAGNO.requestFocus();
							return false;
						}
						M_rstRSSET.close();
					}
				}
				catch(Exception e)
				{
					setMSG(e,"error in Tag No InputVerifier  ");
				}
			}
		
			else if(input == txtEQPID)
			{	
				try
				{
					if(txtEQPID.getText().length()>0)
					{
						M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS from PM_EQMST  ";
						M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EQ_STSFL,'')<>'X' AND EQ_EQPID ='"+txtEQPID.getText().toUpperCase()+"' ";
						
						//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							txtEQPID.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQPID"),""));
							txtEQPDS.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQPDS"),""));
						}
						else
						{	
							setMSG("Enter valid Equipment Id..",'E');
							txtEQPID.requestFocus();
							return false;
						}
						M_rstRSSET.close();
					}
				}
				catch(Exception e)
				{
					setMSG(e,"error in Equipment id  InputVerifier ");
				}
			}

			else if(input == txtEQMTP)
			{	
				try
				{
					if(txtEQMTP.getText().length()>0)
					{
						if(!hstCDTRN.containsKey("MSTPMXXEQT"+txtEQMTP.getText().toString().toUpperCase()))
						{
							setMSG("Enter Valid Equipment Main Type ",'E');
							return false;
						}
						else
							txtEQMTP_DS.setText(getCDTRN("MSTPMXXEQT"+txtEQMTP.getText().toString().toUpperCase(),"CMT_CODDS",hstCDTRN));
					}
				}
				catch(Exception e)
				{
					setMSG(e,"error txtEQMTP InputVerifier ");
				}
			}
			
			else if(input == txtEQSTP)
			{	
				try
				{
					if(txtEQSTP.getText().length()>0)
					{
						if(!hstCDTRN.containsKey("MSTPMXXEST"+txtEQSTP.getText().toString().toUpperCase()))
						{
							setMSG("Enter Valid Equipment Sub Type ",'E');
							return false;
						}
						else
							txtEQSTP_DS.setText(getCDTRN("MSTPMXXEST"+txtEQSTP.getText().toString().toUpperCase(),"CMT_CODDS",hstCDTRN));	
					}	
				}
				catch(Exception e)
				{
					setMSG(e,"error in  Equipment Sub Type InputVerifier");
				}
			}
			
			else if(input == txtPLNCD)
			{	
				try{
					if(!hstCDTRN.containsKey("MSTPMXXPLN"+txtPLNCD.getText().toString().trim()))
					{
						setMSG("Enter Valid  Plant Code ",'E');
						return false;
					}
					else
						txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				}
				catch(Exception e)
				{
					setMSG(e,"error in Plant Code InputVerifier ");
				}
			}
			else if(input == txtARACD)
			{	
				try{
					if(!hstCDTRN.containsKey("MSTPMXXARA"+txtARACD.getText().toString().trim()))
					{
						setMSG("Enter Valid Area Code ",'E');
						return false;
					}
					else
						txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				}
				catch(Exception e)
				{
					setMSG(e,"error in Area Code InputVerifier");
				}
			}
			else if(input == txtCRICD)
			{	
				try{
					
					if(txtCRICD.getText().length()>0)
					{
						if(!hstCDTRN.containsKey("MSTPMXXCRT"+txtCRICD.getText().toString().trim()))
						{
							setMSG("Enter Valid Criticality Code ",'E');
							return false;
						}
						else
							txtCRIDS.setText(getCDTRN("MSTPMXXCRT"+txtCRICD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
					}
				}
				catch(Exception e)
				{
					setMSG(e,"error in Criticality Code InputVerifier");
				}

			}
			else if(input == txtDPTCD)
			{ 
				if(txtDPTCD.getText().length()>0)
				{
					if(!hstCDTRN.containsKey("SYSCOXXDPT"+txtDPTCD.getText().toString().trim()))
					{
						setMSG("Enter Valid Department Code ",'E');
						return false;
					}
					else
						txtDPTNM.setText(getCDTRN("SYSCOXXDPT"+txtDPTCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
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



