/*

*/
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.sql.ResultSet;

class sa_tehwm extends cl_pbase
{			
	private JTextField txtSYSTP; 
	private JTextField txtMCHCT;
	private JTextField txtMCHDS;
	private JTextField txtMACNM;
	private JTextField txtSRLNO;
	//private JTextField txtVRFDT;
	private JTextField txtMNTNO,txtMNTDS;
	
	private JComboBox cmbSFTCT,cmbLICTP;
	private JTabbedPane jtpMANTAB;   
	private JPanel pnlINTSW,pnlHWMNT;   
	private  cl_JTable tblSFTDL,tblHWMNT;
	
	private INPVF oINPVF;
	private TBLINPVF objTBLVRF;
	
	Hashtable<String,String> hstSFTCT;
	Hashtable<String,String> hstSFTCT_DS;
	Hashtable<String,String> hstLICTP_DS;
	Hashtable<String,String> hstLICTP;
	Hashtable<String,String> hstLICDS;
	Hashtable<String,String> hstSFTDS;
	
	 private Vector<String> vtrLICNO; 
	
	int TB1_CHKFL = 0; 	JCheckBox chkCHKFL;
	int TB1_SFTCT = 1;  JTextField txtSFTCT;
	int TB1_SFTCD = 2;  JTextField txtSFTCD;
	int TB1_SFTNM = 3;  JTextField txtSFTNM; 
	int TB1_SWSRL = 4;  JTextField txtSWSRL; 
	int TB1_LICNO = 5;  JTextField txtLICNO; 
	int TB1_LICTP = 6;  JTextField txtLICTP; 
	int TB1_LICDS = 7;  JTextField txtLICDS; 
	int TB1_VRFDT = 8;  JTextField txtVRFDT;  
	
	String L_strSTSFL="";
	
	public sa_tehwm()
	{
    	super(2);
    	try
    	{
    		setMatrix(20,20);
    		cmbSFTCT = new JComboBox();
    		cmbSFTCT.addItem("Select");
    		//cmbLICTP = new JComboBox();
    		
    		pnlINTSW = new JPanel();
			pnlHWMNT = new JPanel();
			pnlINTSW.setLayout(null);
			pnlHWMNT.setLayout(null);
		
			jtpMANTAB=new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			
			jtpMANTAB.add(pnlINTSW,"Installed Software");
			//jtpMANTAB.add(pnlHWMNT,"H/W Maintenance");
			
    		add(new JLabel("System Type:"),2,3,1,2,this,'L');
		    add(txtSYSTP = new TxtLimit(2),2,5,1,2,this,'L');  
    		
    		add(new JLabel("Machine Category:"),2,7,1,4,this,'L');
    		add(txtMCHCT = new TxtLimit(2),2,10,1,1,this,'L'); 
    		add(txtMCHDS = new TxtLimit(20),2,11,1,4,this,'L'); 
    		
		    add(new JLabel("Machine Name:"),3,3,1,2,this,'L');
		    add(txtMACNM = new TxtLimit(30),3,5,1,2,this,'L'); 
		    
		    add(new JLabel("Machine Srl No:"),3,7,1,3,this,'L');
		    add(txtSRLNO = new TxtLimit(30),3,10,1,5,this,'L'); 
		   
		  //  cmbLICTP.addActionListener(this);
			//cmbLICTP.addItem("Select");
		    vtrLICNO  = new Vector<String>();
		    
			hstLICTP = new Hashtable<String,String>();
			hstLICTP_DS = new Hashtable<String,String>();
			String L_strSQLQRY= "SELECT CMT_CODCD,CMT_SHRDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='SAXXLTP'";
			ResultSet L_rstRSSET= cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					hstLICTP.put(L_rstRSSET.getString("CMT_SHRDS"),L_rstRSSET.getString("CMT_CODCD"));
					hstLICTP_DS.put(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET.getString("CMT_SHRDS"),""));
				}
				L_rstRSSET.close();
			}
    		
		   // add(new JLabel("Verify Date:"),3,15,1,2,this,'L');
		   // add(txtVRFDT = new TxtDate(),3,17,1,2,this,'L');  
		    
		    String[] L_strTBLHD = {"","Software Category","S/W Code","Software Name","Serial No","License No","Lic.Type","Lic.Purpose","Verify Date"};
    		int[] L_intCOLSZ = {10,120,50,300,50,150,50,90,60};
    		tblSFTDL= crtTBLPNL1(pnlINTSW,L_strTBLHD,100,1,1,11,17,L_intCOLSZ,new int[]{0});
    		//tblSFTDL.addActionListener(this);
    		tblSFTDL.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
    		tblSFTDL.setCellEditor(TB1_SFTCD,txtSFTCD=new TxtLimit(3));
    		tblSFTDL.setCellEditor(TB1_LICTP,txtLICTP=new TxtLimit(20));
    		tblSFTDL.setCellEditor(TB1_LICNO,txtLICNO=new TxtLimit(40));
    		tblSFTDL.setCellEditor(TB1_LICDS,txtLICDS=new TxtLimit(50));
    		tblSFTDL.setCellEditor(TB1_VRFDT,txtVRFDT=new TxtDate());
    		tblSFTDL.setCellEditor(TB1_SFTCT,cmbSFTCT);
    	//	tblSFTDL.setCellEditor(TB1_LICTP,cmbLICTP);
    		
    		
    		/*add(new JLabel("Maintenance No:"),1,1,1,3,pnlHWMNT,'L');
 		    add(txtMNTNO = new TxtLimit(2),1,4,1,2,pnlHWMNT,'L'); 
 		    add(txtMNTDS = new TxtLimit(30),1,6,1,5,pnlHWMNT,'L'); 
    		
    		String[] L_strTBLHD1 = {"","Step No","Job Description","Desired Observation"};
    		int[] L_intCOLSZ1= {10,80,400,300};
    		tblHWMNT= crtTBLPNL1(pnlHWMNT,L_strTBLHD1,100,2,1,10,16,L_intCOLSZ1,new int[]{0});
    		
    		tblHWMNT.setCellEditor(TB2_CHKFL,chkCHKFL=new JCheckBox());
    		tblHWMNT.setCellEditor(TB2_STPNO,txtSTPNO=new TxtLimit(3));
    		tblHWMNT.setCellEditor(TB2_JOBDS,txtJOBDS=new TxtLimit(50));
    		tblHWMNT.setCellEditor(TB2_DSOBS,txtDSOBS=new TxtLimit(50));*/
    		
    		add(jtpMANTAB,5,2,13,18,this,'L');
    		
			oINPVF=new INPVF();
			txtMACNM.setInputVerifier(oINPVF);
			txtMCHCT.setInputVerifier(oINPVF);
			
			objTBLVRF = new TBLINPVF();
			tblSFTDL.setInputVerifier(objTBLVRF);
			txtSFTCD.addKeyListener(this);
			txtLICNO.addKeyListener(this);
			
			cmbSFTCT.addActionListener(this);
			((JComboBox) tblSFTDL.cmpEDITR[TB1_SFTCT]).addMouseListener(this);
			
    		hstSFTCT = new Hashtable<String,String>();
    		hstSFTCT_DS = new Hashtable<String,String>();
			String L_strSQLQRY1= "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='SAXXSCT'";
			ResultSet L_rstRSSET1= cl_dat.exeSQLQRY1(L_strSQLQRY1);
			if(L_rstRSSET1 != null)
			{
				while(L_rstRSSET1.next())
				{
					hstSFTCT.put(nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),""),nvlSTRVL(L_rstRSSET1.getString("CMT_CODCD"),""));
					hstSFTCT_DS.put(L_rstRSSET1.getString("CMT_CODCD"),nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),""));
					cmbSFTCT.addItem(L_rstRSSET1.getString("CMT_CODDS"));
				}
				L_rstRSSET1.close();
			}
			
			
			hstSFTDS = new Hashtable<String,String>();
			String L_strSQLQRY3= " SELECT SW_SFTCT,SW_SFTCD,SW_SRLNO,SW_LICTP,SW_SFTDS from SA_SWMST";
            ResultSet L_rstRSSET3= cl_dat.exeSQLQRY1(L_strSQLQRY3);
			if(L_rstRSSET3 != null)
			{
				while(L_rstRSSET3.next())
				{
					hstSFTDS.put(L_rstRSSET3.getString("SW_SFTCT")+"|"+L_rstRSSET3.getString("SW_SFTCD")+"|"+L_rstRSSET3.getString("SW_SRLNO"),nvlSTRVL(L_rstRSSET3.getString("SW_SFTDS"),""));
				
				}
				L_rstRSSET3.close();
			}
			
			hstLICDS = new Hashtable<String,String>();
			String L_strSQLQRY4= " SELECT SWT_LICNO,SWT_LICDS from SA_SWTRN WHERE SWT_LICDS<>''";
            ResultSet L_rstRSSET4= cl_dat.exeSQLQRY1(L_strSQLQRY4);
			if(L_rstRSSET4 != null)
			{
				while(L_rstRSSET4.next())
				{
					hstLICDS.put(L_rstRSSET4.getString("SWT_LICNO"),nvlSTRVL(L_rstRSSET4.getString("SWT_LICDS"),""));
				
				}
				L_rstRSSET4.close();
			}
    	}
    	catch(Exception EXE)
    	{
    		System.out.println("inside constructor"+EXE);
    	}
	}
    
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{ 
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				tblSFTDL.cmpEDITR[TB1_SFTNM].setEnabled(false);
				tblSFTDL.cmpEDITR[TB1_SWSRL].setEnabled(false);
				tblSFTDL.cmpEDITR[TB1_LICDS].setEnabled(false);
				tblSFTDL.cmpEDITR[TB1_LICTP].setEnabled(false);
				txtMCHDS.setEnabled(false);
				txtSRLNO.setEnabled(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtSYSTP.requestFocus();
					txtSYSTP.setText(M_strSBSCD.substring(0,2));
					//txtVRFDT.setText(cl_dat.M_strLOGDT_pbst);
					setENBL(true);
				}
				else
					setENBL(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					tblSFTDL.setEnabled(false);
					tblSFTDL.cmpEDITR[TB1_CHKFL].setEnabled(true);
				}
				else
				{
					tblSFTDL.setEnabled(true);
				}	
				
			}
			if(M_objSOURC == cmbSFTCT)
			{
				if(cmbSFTCT.getSelectedIndex()>0)
				{
					tblSFTDL.setValueAt(new Boolean(true),tblSFTDL.getSelectedRow(),TB1_CHKFL);
					tblSFTDL.setValueAt(cl_dat.M_strLOGDT_pbst,tblSFTDL.getSelectedRow(),TB1_VRFDT);
				}
			}
		}
		catch(Exception L_EX) 
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	
	 public void mouseReleased(MouseEvent L_KE)
	 {
  	    super.mouseReleased(L_KE);
		try
		{ 	
			 if(L_KE.getSource().equals(cmbSFTCT))
			 {
				if(cmbSFTCT.getSelectedIndex()>0)
				{
					tblSFTDL.setValueAt(new Boolean(true),tblSFTDL.getSelectedRow(),TB1_CHKFL);
					tblSFTDL.setValueAt(cl_dat.M_strLOGDT_pbst,tblSFTDL.getSelectedRow(),TB1_VRFDT);
				}
				tblSFTDL.setValueAt("",tblSFTDL.getSelectedRow(),TB1_SFTCD);
				tblSFTDL.setValueAt("",tblSFTDL.getSelectedRow(),TB1_SFTNM);
				tblSFTDL.setValueAt("",tblSFTDL.getSelectedRow(),TB1_SWSRL);
				tblSFTDL.setValueAt("",tblSFTDL.getSelectedRow(),TB1_LICNO);
			 }
		}
		catch(Exception L_EX) 
		{
			setMSG(L_EX,"mouseReleased");
		}
	}
	
	/** Method to request Focus in all TextField of component,when press ENTER **/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
				if(M_objSOURC == txtSFTCD)
				{
					M_strHLPFLD = "txtSFTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					M_strSQLQRY = " SELECT distinct SW_SFTCD,SW_SFTDS,SW_SRLNO from SA_SWMST where ifnull(SW_STSFL,' ') <> 'X'";
					//SW_SYSTP='"+txtSYSTP.getText().toString()+"' AND
					if(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCT).toString().length()>0 || !tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCT).toString().equals("Select"))
						M_strSQLQRY += " and SW_SFTCT='"+hstSFTCT.get(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCT).toString())+"' ";
					if(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCD).toString().length() >0)
						M_strSQLQRY += " and SW_SFTCD like '"+tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCD).toString()+"%'";
					M_strSQLQRY += " order by SW_SFTCD";
					//System.out.println("txtDOCNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Software Code","Description","Serial No"},3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
						
				}
				else if(M_objSOURC== txtLICNO)
				{									
					M_strHLPFLD = "txtLICNO";
					
					M_strSQLQRY = " SELECT SWT_LICNO,SWT_LICTP,SWT_LICDS from SA_SWTRN where ifnull(SWT_STSFL,' ') <> 'X'";
					//if(txtSYSTP.getText().length()>0)
					//	M_strSQLQRY += " and SWT_SYSTP='"+txtSYSTP.getText().toString()+"'";
					if(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCT).toString().length()>0 || !tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCT).toString().equals("Select"))
						M_strSQLQRY += " and SWT_SFTCT='"+hstSFTCT.get(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCT).toString())+"' ";
					if(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCD).toString().length() >0)
						M_strSQLQRY += " and SWT_SFTCD ='"+tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SFTCD).toString()+"'";
					if(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SWSRL).toString().length() >0)
						M_strSQLQRY += " and SWT_SRLNO ='"+tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_SWSRL).toString()+"'";
					//if(!tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_LICTP).toString().equals("Select") || tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_LICTP).toString().length() >0)
					//	M_strSQLQRY += " and SWT_LICTP ='"+hstLICTP.get(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_LICTP).toString())+"'";
					if(tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_LICNO).toString().length() >0)
						M_strSQLQRY += " and SWT_LICNO like '"+tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_LICNO).toString()+"%'";
					M_strSQLQRY += " order by SWT_LICNO";
					//System.out.println("txtDOCNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Licenses No","Lic.Type","Lic.Purpose"},3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC== txtMCHCT)
				{									
					M_strHLPFLD = "txtMCHCT";
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='HWXXMCH' and IFNULL(CMT_CODCD,'')<>'03'";
					if(txtMCHCT.getText().length() >0)			
						M_strSQLQRY += " and CMT_CODCD like '"+ txtMCHCT.getText().trim()+"%'";	
					M_strSQLQRY += " order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"MCH Categary","Description"},2,"CT");			
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC== txtMACNM)
				{									
					M_strHLPFLD = "txtMACNM";
					M_strSQLQRY=" SELECT MC_MACNM,MC_SRLNO from HW_MCMST where MC_STSFL='1'" ;
					if(txtSYSTP.getText().length() >0)		
						M_strSQLQRY += " and MC_SYSTP='"+txtSYSTP.getText().trim()+"' ";
					if(txtMCHCT.getText().length() >0)		
						M_strSQLQRY += " AND MC_MCHCT='"+txtMCHCT.getText().trim()+"' ";
					if(txtMACNM.getText().length() >0)			
						M_strSQLQRY += " and MC_MACNM like '"+ txtMACNM.getText().trim()+"%'";		
					M_strSQLQRY += " order by MC_MACNM";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Machine Name","Machine Serial No"},2,"CT");			
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtSYSTP)
				{
					inlTBLEDIT(tblSFTDL);
		  			tblSFTDL.clrTABLE();
					txtMCHCT.setText("");
					txtMCHDS.setText("");
					txtMACNM.setText("");
					txtSRLNO.setText("");
					txtMCHCT.requestFocus();
					setMSG("Enter Machine Category..",'N');
				}
				else if(M_objSOURC == txtMCHCT)
				{
					inlTBLEDIT(tblSFTDL);
		  			tblSFTDL.clrTABLE();
					txtMACNM.setText("");
					txtSRLNO.setText("");
					if(txtMCHCT.getText().length()==0)
						txtMCHDS.setText("");
					else
					{
						txtMACNM.requestFocus();
						setMSG("Enter Machine Serial No..",'N');
					}
				}
				else if(M_objSOURC == txtMACNM)
				{
					if(txtMACNM.getText().length()==0)
						txtSRLNO.setText("");
					else
					{
						txtMACNM.setText(txtMACNM.getText().toUpperCase());
						txtMACNM.setText(txtMACNM.getText().replace("'","`"));
						inlTBLEDIT(tblSFTDL);
			  			tblSFTDL.clrTABLE();
						//txtVRFDT.requestFocus();
						//setMSG("Enter the Verify Date..",'N');
						tblSFTDL.requestFocus();
						setMSG("Select Software Category..",'N');
					}
				}
			/*	else if(M_objSOURC ==txtVRFDT)
				{
					tblSFTDL.requestFocus();
					setMSG("Enter Software Code..",'N');
				}*/
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keypressed"); 
		}
	} 
	/**
	 * Method for set data from Help Screen to textfield */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtSFTCD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtSFTCD.setText(L_STRTKN.nextToken());
				 tblSFTDL.setValueAt(L_STRTKN.nextToken(),tblSFTDL.getSelectedRow(),TB1_SFTNM);
				 tblSFTDL.setValueAt(L_STRTKN.nextToken(),tblSFTDL.getSelectedRow(),TB1_SWSRL);
			}
			else if(M_strHLPFLD.equals("txtLICNO"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtLICNO.setText(L_STRTKN.nextToken());
				 tblSFTDL.setValueAt(hstLICTP_DS.get(L_STRTKN.nextToken()),tblSFTDL.getSelectedRow(),TB1_LICTP);
				 tblSFTDL.setValueAt(L_STRTKN.nextToken(),tblSFTDL.getSelectedRow(),TB1_LICDS);
			}
			else if(M_strHLPFLD.equals("txtMCHCT"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtMCHCT.setText(L_STRTKN.nextToken());
				 txtMCHDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtMACNM"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtMACNM.setText(L_STRTKN.nextToken());
				 txtSRLNO.setText(L_STRTKN.nextToken());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}
	boolean vldDATA()
	{
		try
		{
			if(txtSYSTP.getText().trim().length() ==0)
	    	{
				txtSYSTP.requestFocus();
	    		setMSG("Enter the System Type",'E');
	    		return false;
	    	}
			else if(txtMCHCT.getText().trim().length() ==0)
	    	{
				txtMCHCT.requestFocus();
	    		setMSG("Enter the Machine Category",'E');
	    		return false;
	    	}
			else if(txtMACNM.getText().trim().length() ==0)
	    	{
				txtMACNM.requestFocus();
	    		setMSG("Enter Machine Name",'E');
	    		return false;
	    	}
			/*else if(txtVRFDT.getText().trim().length() ==0)
	    	{
				txtVRFDT.requestFocus();
	    		setMSG("Enter Verify Date",'E');
	    		return false;
	    	}*/
			
			boolean L_flgCHKFL= false;
	    	for(int k=0;k<tblSFTDL.getRowCount();k++)
			{
				if( tblSFTDL.getValueAt(k,TB1_CHKFL).toString().equals("true"))
				{
					L_flgCHKFL= true;
				}
			}
			
			if(L_flgCHKFL== false)
			{
				setMSG("Please Select Atleast one row..",'E');				
				return false;
			}	
			for(int P_intROWNO=0;P_intROWNO<tblSFTDL.getRowCount();P_intROWNO++)
			{
				if(tblSFTDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					if(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString().equals("Select"))
					{
						setMSG("Please Select Valid Software Category for Software Code "+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString()+"..",'E');
						return false;
						
					}
					else if(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString().length()==0)
					{
			    		setMSG("Select Software Category..",'E');
			    		return false;
					}
					else if(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString().length()==0)
					{
			    		setMSG("Enter Software Code..",'E');
			    		return false;
					}
					else if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().length()==0)
					{
						if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().length()>0)
						{
							setMSG("Enter License Type for License No "+tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString()+"..",'E');
							return false;
						}
					}
					else if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().length()==0)
					{
						if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().length()>0)
						{
				    		setMSG("Select License No..",'E');
				    		return false;
						}
					}
					else if(tblSFTDL.getValueAt(P_intROWNO,TB1_VRFDT).toString().length()==0)
					{
			    		setMSG("Enter Verify Date..",'E');
			    		return false;
					}
				}
	    	}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATA()");		
		}
		return true;
	}
	/**
	 * Method to Save Records*/
	void exeSAVE()
	{
		int P_intROWNO=0;
		
		try
		{
			if(!vldDATA()) 
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)
			|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		  	{
				for(P_intROWNO=0;P_intROWNO<tblSFTDL.getRowCount();P_intROWNO++)
				{
					if(tblSFTDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeADDREC(P_intROWNO);
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
				
				for(P_intROWNO=0;P_intROWNO<tblSFTDL.getRowCount();P_intROWNO++)
				{
					if(tblSFTDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeDELREC(P_intROWNO); 
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG(" Data Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					setMSG(" Data Modified Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
				clrCOMP();
				txtSYSTP.setText(M_strSBSCD.substring(0,2));
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				   setMSG("Error in Modified Data details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'E');
			}
		}
		catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeSAVE"); 
	    }
	} 
	
	/**
	 *  On Save Button click data is inserted into SA_SWMST table.
	 * 
	 */
	private void exeADDREC(int P_intROWNO)
	{ 
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			
			String strSQLQRY=" select count(*) from HW_MCTRN ";
			strSQLQRY+=" where MCT_SFTCT = '"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"' AND MCT_SFTCD = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"' AND MCT_SWSRL = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"' AND MCT_LICNO = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"' ";
			strSQLQRY+=" AND MCT_MCHCT='"+txtMCHCT.getText().toString()+"' AND MCT_SRLNO='"+txtSRLNO.getText().toString()+"' and IFNULL(MCT_STSFL,'')<>'X'";
			
			ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			//System.out.println(">>>Count>>"+strSQLQRY);
			if(rstRSSET.next() && rstRSSET!= null)
			{
				if(rstRSSET.getInt(1)>0)
				{
					M_strSQLQRY = " Update HW_MCTRN set";
			    
					M_strSQLQRY += " MCT_VRFDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblSFTDL.getValueAt(P_intROWNO,TB1_VRFDT).toString()))+"',";
					if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().length()>0 && !tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().equals("Select"))
						M_strSQLQRY +=" MCT_LICTP = '"+hstLICTP.get(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString())+"',";
					M_strSQLQRY +=" MCT_STSFL = '1',";
					M_strSQLQRY +=" MCT_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +=" MCT_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY +=" where MCT_MCHCT= '"+txtMCHCT.getText()+"'";
					M_strSQLQRY +=" AND MCT_SRLNO= '"+txtSRLNO.getText().toString()+"'";
					M_strSQLQRY +=" AND MCT_SYSTP = '"+txtSYSTP.getText().toString()+"' ";
					M_strSQLQRY +=" AND MCT_SFTCT = '"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"' ";
					M_strSQLQRY +=" AND MCT_SFTCD = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"' ";
					M_strSQLQRY +=" AND MCT_SWSRL = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"' ";
					M_strSQLQRY +=" AND MCT_LICNO= '"+tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"' ";
					
					//System.out.println(">>>update>>"+M_strSQLQRY);  
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
					  M_strSQLQRY =" insert into HW_MCTRN(MCT_SYSTP,MCT_MCHCT,MCT_SRLNO,MCT_VRFDT,MCT_SFTCT,MCT_SFTCD,MCT_SWSRL,MCT_LICTP,MCT_LICNO,MCT_STSFL,MCT_LUSBY,MCT_LUPDT)"
				         +" VALUES('"+txtSYSTP.getText().toString()+"',";
				      M_strSQLQRY += "'"+txtMCHCT.getText()+"',";
					  M_strSQLQRY += "'"+txtSRLNO.getText()+"',";	
					  if(tblSFTDL.getValueAt(P_intROWNO,TB1_VRFDT).toString().length()>0)
						  M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(tblSFTDL.getValueAt(P_intROWNO,TB1_VRFDT).toString()))+"',";
					  else 
						  M_strSQLQRY += "null,"; 
					  M_strSQLQRY += "'"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"',";
					  M_strSQLQRY += "'"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"',";
					  M_strSQLQRY += "'"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"',";
					  if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().length()>0 && !tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().equals("Select"))
						  M_strSQLQRY += "'"+hstLICTP.get(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().trim())+"',";
					  else
						  M_strSQLQRY += " 'XX',";
					  M_strSQLQRY += "'"+tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"',";
					 
					  M_strSQLQRY += " '1',";
					  M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
					  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
					
					  //System.out.println(">>>Insert>>"+ M_strSQLQRY );
					  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					  //if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().length()>0 && tblSFTDL.getValueAt(P_intROWNO,TB1_LICDS).toString().length()>0)
					//	  hstLICDS.put(tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim(),tblSFTDL.getValueAt(P_intROWNO,TB1_LICDS).toString().trim());
			
				}
			}
			if(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().length()>0 && tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().length()>0)
			{
				String strSQLQRY1=" select count(*) UTLQT from HW_MCTRN";
				strSQLQRY1+=" where MCT_SFTCT = '"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"' AND MCT_SFTCD = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"' AND MCT_SWSRL = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"' AND MCT_LICTP= '"+hstLICTP.get(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString().trim())+"' ";
				ResultSet rstRSSET1 = cl_dat.exeSQLQRY(strSQLQRY1);
				//System.out.println(">>>Count>>"+strSQLQRY1);
				if(rstRSSET1.next() && rstRSSET1!= null)
				{
					if(rstRSSET1.getInt(1)>0)
					{
						M_strSQLQRY = " Update SA_SWMST set";
						M_strSQLQRY +=" SW_UTLQT = '"+rstRSSET1.getString("UTLQT")+"'";
						M_strSQLQRY +=" where SW_SFTCT = '"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"' ";
						M_strSQLQRY +=" AND SW_SFTCD = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"' ";
						M_strSQLQRY +=" AND SW_SRLNO= '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"'";
						M_strSQLQRY +=" AND SW_LICTP= '"+hstLICTP.get(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString())+"'";
					//	System.out.println(">>>update>>"+M_strSQLQRY);  
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						
					}
				}
				String strSQLQRY2=" select count(*) UTLQT from HW_MCTRN ";
				strSQLQRY2+=" where MCT_SFTCT = '"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"' AND MCT_SFTCD = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"' AND MCT_SWSRL = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"' AND MCT_LICNO= '"+tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"' ";
				ResultSet rstRSSET2 = cl_dat.exeSQLQRY(strSQLQRY2);
				//System.out.println(">>>Count>>"+strSQLQRY2);
				if(rstRSSET2.next() && rstRSSET2!= null)
				{
					if(rstRSSET2.getInt(1)>0)
					{
						M_strSQLQRY = " Update SA_SWTRN set";
						M_strSQLQRY +=" SWT_UTLQT = '"+rstRSSET2.getString("UTLQT")+"'";
						//M_strSQLQRY +="  SWT_SYSTP = '"+txtSYSTP.getText().toString()+"' ";
						M_strSQLQRY +=" where SWT_SFTCT = '"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"' ";
						M_strSQLQRY +=" AND SWT_SFTCD = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"' ";
						M_strSQLQRY +=" AND SWT_SRLNO= '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"'";
						M_strSQLQRY +=" AND SWT_LICNO= '"+tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString()+"'";
					//	M_strSQLQRY +=" AND SWT_LICTP= '"+hstLICTP.get(tblSFTDL.getValueAt(P_intROWNO,TB1_LICTP).toString())+"'";
						
						//System.out.println(">>>update1>>"+M_strSQLQRY);  
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			 setCursor(cl_dat.M_curDFSTS_pbst);
			 setMSG(L_EX,"exeADDREC()"); 
		}
	}
	
	/**
	 * Delete Records From SA_SWMST Table*/
		private void exeDELREC(int P_intROWNO) 
		{ 
		  try
		  {
			  	M_strSQLQRY = "UPDATE HW_MCTRN SET";	
				M_strSQLQRY +="	MCT_STSFL='X'";	
				M_strSQLQRY +=" where MCT_MCHCT='"+txtMCHCT.getText()+"'";
				M_strSQLQRY +=" AND MCT_SRLNO='"+txtSRLNO.getText().toString()+"'";
				M_strSQLQRY +=" AND MCT_SYSTP = '"+txtSYSTP.getText().toString()+"' ";
				M_strSQLQRY +=" AND MCT_SFTCT = '"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCT).toString())+"' ";
				M_strSQLQRY +=" AND MCT_SFTCD = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SFTCD).toString()+"' ";
				M_strSQLQRY +=" AND MCT_SWSRL = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_SWSRL).toString()+"' ";
				M_strSQLQRY +=" AND MCT_LICNO = '"+tblSFTDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"' ";
				
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				//System.out.println(">>>Delete>>"+M_strSQLQRY);
		  }
		  catch(Exception L_EX)
		  {
		     setMSG(L_EX,"exeDELREC()");		
		  }
		}
	
	  /**
	  * Method to setdata in respective text box from SA_SWMST & SA_SWTRN database table*/
	  	private void getDATA() 
	  	{
	  		try
	  		{ 
	  			this.setCursor(cl_dat.M_curWTSTS_pbst);
	  			inlTBLEDIT(tblSFTDL);
	  			tblSFTDL.clrTABLE();
				setMSG("Fetching Records ...",'N');
				vtrLICNO.clear();
	  			int L_CNT=0;
	  			String L_strLICNO="",L_strSFTCT="",L_strSFTCD="",L_strSRLNO="";
	  			M_strSQLQRY= "select distinct MCT_SYSTP,MCT_SFTCT,MCT_SFTCD,MCT_SWSRL,MCT_LICTP,MCT_LICNO,MCT_VRFDT,SW_LICTP,SW_SFTDS";
	  			M_strSQLQRY+= " from HW_MCTRN,SA_SWMST";
	  			M_strSQLQRY+= " where MCT_SFTCT=SW_SFTCT AND MCT_SFTCD=SW_SFTCD AND MCT_SWSRL=SW_SRLNO AND MCT_LICTP=SW_LICTP";
	  			M_strSQLQRY+= " AND MCT_STSFL<>'X' and MCT_SYSTP='"+txtSYSTP.getText().toString()+"' AND MCT_MCHCT = '"+txtMCHCT.getText().trim()+"' AND MCT_SRLNO = '"+txtSRLNO.getText().trim()+"'";
	  			//M_strSQLQRY+= " AND MCT_LICNO=SWT_LICNO";
	  			M_strSQLQRY+= " order by MCT_SFTCT,MCT_SFTCD,MCT_SWSRL,MCT_VRFDT";
	  			
	  			//System.out.println(">>>select>>"+ M_strSQLQRY );
	  			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
	  			if(M_rstRSSET != null )
	  			{
	  				while( M_rstRSSET.next())
	  				{
	  					L_strLICNO=M_rstRSSET.getString("MCT_LICNO");
	  					L_strSFTCT=M_rstRSSET.getString("MCT_SFTCT");
	  					L_strSFTCD=M_rstRSSET.getString("MCT_SFTCD");
	  					L_strSRLNO=M_rstRSSET.getString("MCT_SWSRL");
	  					if(hstSFTCT_DS.containsKey(M_rstRSSET.getString("MCT_SFTCT")))
	  						tblSFTDL.setValueAt(hstSFTCT_DS.get(M_rstRSSET.getString("MCT_SFTCT")),L_CNT,TB1_SFTCT);
		  				tblSFTDL.setValueAt(M_rstRSSET.getString("MCT_SFTCD"),L_CNT,TB1_SFTCD);
		  				tblSFTDL.setValueAt(M_rstRSSET.getString("SW_SFTDS"),L_CNT,TB1_SFTNM);
		  				tblSFTDL.setValueAt(M_rstRSSET.getString("MCT_SWSRL"),L_CNT,TB1_SWSRL);
		  				tblSFTDL.setValueAt(M_rstRSSET.getString("MCT_LICNO"),L_CNT,TB1_LICNO);
		  				tblSFTDL.setValueAt(hstLICTP_DS.get(M_rstRSSET.getString("MCT_LICTP")),L_CNT,TB1_LICTP);
		  				tblSFTDL.setValueAt(hstLICDS.get(M_rstRSSET.getString("MCT_LICNO")),L_CNT,TB1_LICDS);
		  				if(!(M_rstRSSET.getDate("MCT_VRFDT")==null))
		  					tblSFTDL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("MCT_VRFDT")),L_CNT,TB1_VRFDT);
		  				if(L_strLICNO.length()>0)
		  					vtrLICNO.add(M_rstRSSET.getString("MCT_LICNO"));
		  				L_CNT++;
	  				}
	  			}
	          
	            M_rstRSSET.close();
	            if(L_CNT==0)
	  				setMSG("No Data Found ...",'E');
	        }	
	  		catch(Exception L_EX)
	  		{
	  			this.setCursor(cl_dat.M_curDFSTS_pbst);
	  			setMSG(L_EX,"getDATA()"); 
	  		}
	  	}
	  	void inlTBLEDIT(cl_JTable tblTABLE)
		{
			if(tblTABLE.isEditing())
				tblTABLE.getCellEditor().stopCellEditing();
			tblTABLE.setRowSelectionInterval(0,0);
			tblTABLE.setColumnSelectionInterval(0,0);
		}
	  	
		
	/** Validate Data by user*/
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;
			   
				if(input == txtMCHCT)
				{
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='HWXXMCH' ";
					M_strSQLQRY += " and CMT_CODCD ='"+ txtMCHCT.getText().trim()+"'";			
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null && M_rstRSSET.next())
					{
						txtMCHCT.setText(M_rstRSSET.getString("CMT_CODCD"));
						txtMCHDS.setText(M_rstRSSET.getString("CMT_CODDS"));
					}	
					else 
					{
						setMSG("Enter Valid Machine Category..",'E');
						return false;
					}	
				}
				else if(input == txtMACNM)
				{
					M_strSQLQRY=" SELECT MC_SRLNO,MC_MACNM from HW_MCMST where MC_STSFL='1' and MC_SYSTP ='"+ txtSYSTP.getText().trim()+"' and MC_MCHCT ='"+ txtMCHCT.getText().trim()+"'";
					M_strSQLQRY += "  AND MC_MACNM='"+ txtMACNM.getText().trim()+"' ";			
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null && M_rstRSSET.next())
					{
						txtMACNM.setText(M_rstRSSET.getString("MC_MACNM"));
						txtSRLNO.setText(M_rstRSSET.getString("MC_SRLNO"));

						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							getDATA();
					}	
					else 
					{
						setMSG("Enter Valid Machine Name..",'E');
						return false;
					}	
				}
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"class INPVF");		
			}
			return true;
		}
	}
	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
		   	try
		   	{
		   		if(getSource()==tblSFTDL)
			    {
		   			if(tblSFTDL.getValueAt(P_intROWID,P_intCOLID).toString().length()== 0)
						return true;
					if(P_intCOLID == TB1_SFTCD)
					{
						if(tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString().length()== 0 || tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString().equals("Select"))
						{
							setMSG("Please Select Valid Software Category ",'E');
							return false;
						}
						if(tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString().length()>0  || !tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString().equals("Select"))
						{
							M_strSQLQRY=" SELECT SW_SFTCD,SW_SFTDS,SW_SRLNO from SA_SWMST ";
							M_strSQLQRY +=" WHERE ifnull(SW_STSFL,'')<>'X' and SW_SFTCD='"+tblSFTDL.getValueAt(P_intROWID,P_intCOLID).toString().trim()+"' ";
							//AND SW_SYSTP='"+txtSYSTP.getText().toString()+"'";
							M_strSQLQRY +=" AND SW_SFTCT='"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString())+"'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								tblSFTDL.setValueAt(M_rstRSSET.getString("SW_SFTCD"),P_intROWID,TB1_SFTCD);
								//tblSFTDL.setValueAt(M_rstRSSET.getString("SW_SFTDS"),P_intROWID,TB1_SFTNM);
								//tblSFTDL.setValueAt(M_rstRSSET.getString("SW_SRLNO"),P_intROWID,TB1_SWSRL);
								setMSG("",'N');
							}
							else
							{
								setMSG("Enter Valid Software Code ",'E');
								return false;
							}
							M_rstRSSET.close();
						}
					}
					if(P_intCOLID == TB1_LICNO)
					{
						M_strSQLQRY=" SELECT SWT_LICTP,SWT_LICNO,SWT_LICDS FROM SA_SWTRN where ifnull(SWT_STSFL,'')<>'X' ";
						if(tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString().length()>0 || !tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString().equals("Select"))
							M_strSQLQRY += " and SWT_SFTCT='"+hstSFTCT.get(tblSFTDL.getValueAt(P_intROWID,TB1_SFTCT).toString())+"' ";
						if(tblSFTDL.getValueAt(P_intROWID,TB1_SFTCD).toString().length() >0)
							M_strSQLQRY += " and SWT_SFTCD ='"+tblSFTDL.getValueAt(P_intROWID,TB1_SFTCD).toString()+"'";
						if(tblSFTDL.getValueAt(P_intROWID,TB1_SWSRL).toString().length() >0)
							M_strSQLQRY += " and SWT_SRLNO ='"+tblSFTDL.getValueAt(P_intROWID,TB1_SWSRL).toString()+"'";
						//if(!tblSFTDL.getValueAt(tblSFTDL.getSelectedRow(),TB1_LICTP).toString().equals("Select") || tblSFTDL.getValueAt(P_intROWID,TB1_LICTP).toString().length() >0)
						//	M_strSQLQRY += " and SWT_LICTP ='"+hstLICTP.get(tblSFTDL.getValueAt(P_intROWID,TB1_LICTP).toString())+"'";
						M_strSQLQRY += " and SWT_LICNO ='"+tblSFTDL.getValueAt(P_intROWID,TB1_LICNO).toString().toUpperCase()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null && M_rstRSSET.next())
						{
							tblSFTDL.setValueAt(M_rstRSSET.getString("SWT_LICNO"),P_intROWID,TB1_LICNO);	
							tblSFTDL.setValueAt(hstLICTP_DS.get(M_rstRSSET.getString("SWT_LICTP")),P_intROWID,TB1_LICTP);
							tblSFTDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SWT_LICDS"),""),P_intROWID,TB1_LICDS);
							setMSG("",'N');
						}	
						else 
						{
							setMSG("Enter Valid License No..",'E');
							return false;
						}	
						
					}
			    }
		   	}
		   	catch(Exception E_VR)
			{
				setMSG(E_VR,"class INPVF");		
			}
			return true;
		}
    }
}
	