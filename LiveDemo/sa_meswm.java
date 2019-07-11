/*
System Name : Software master System.
 

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
import javax.swing.JOptionPane;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import java.util.Hashtable;
import java.util.StringTokenizer;
//import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;

import java.sql.ResultSet;

class sa_meswm extends cl_pbase
{	
	private JTextField txtSFTCT; 
	private JTextField txtSFTCT_DS; 
	private JTextField txtSFTCD; 
	private JTextField txtSFTDS;
	private JTextField txtSRLNO;
	private JTextField txtPROQT; 
	private JTextField txtUTLQT;

	private JCheckBox chkSTSFL,chkMACNM;
	private JComboBox cmbSFTCT,cmbLICTP;
	
	private JRadioButton  rdbSTSYS;
	private JRadioButton rdbSTSNO;
	private ButtonGroup  btgSTSFL;

	
	private  cl_JTable tblLICDL;
	private JPanel pnlMACNM;
	private INPVF oINPVF;
	private TBLINPVF objTBLVRF;
	
	Hashtable<String,String> hstSFTCT;
	Hashtable<String,String> hstLICTP;
	Hashtable<String,String> hstLICTP_DS;
	Hashtable<String,String> hstSFTCD;
	Hashtable<String,String> hstSRLNO;
	Hashtable<String,String> hstVENDS;
	
	int TB1_CHKFL = 0; 	JCheckBox chkCHKFL;
	int TB1_SYSTP = 1;  JTextField txtSYSTP;
	int TB1_VENCD = 2;  JTextField txtVENCD;
	int TB1_VENNM = 3;  JTextField txtVENNM; 
	int TB1_LICDT = 4;  JTextField txtLICDT;   
	//int TB1_LICTP = 4;  JTextField txtLICTP;           
	int TB1_LICNO = 5; 	JTextField txtLICNO;
	int TB1_VEWAT = 6;   JCheckBox chkVEWAT;
	int TB1_LICQT = 7;  JTextField txtLICQT;
	int TB1_UTLQT = 8;  JTextField txtUTLVL;
	int TB1_LICDS = 9;  JTextField txtLICDS;
	String L_strSTSFL="",L_strLTQRY="";
	
	public sa_meswm()
	{
    	super(2);
    	try
    	{
    		setMatrix(20,20);
    		cmbLICTP = new JComboBox();
    		
    		//add(new JLabel("System Type:"),2,3,1,2,this,'L');
		   // add(txtSYSTP = new TxtLimit(2),2,5,1,2,this,'L'); 
		   
		    add(new JLabel("Software Category:"),2,3,1,3,this,'L');
		    add(cmbSFTCT = new JComboBox(),2,7,1,5,this,'L');
		  
    		add(new JLabel("Software Code:"),3,3,1,2,this,'L');
		    add(txtSFTCD = new TxtLimit(3),3,5,1,2,this,'L');  
		    add(txtSFTDS = new TxtLimit(40),3,7,1,8,this,'L');  
		    
		    add(new JLabel("SRL No:"),4,3,1,2,this,'L');
		    add(txtSRLNO = new TxtLimit(2),4,5,1,2,this,'L');  
		    
		    add(new JLabel("Usage Status"),5,3,1,2,this,'L');
		    add(rdbSTSYS=new JRadioButton("Yes"),5,5,1,2,this,'L');
			add(rdbSTSNO=new JRadioButton("No"),5,7,1,2,this,'L');
			btgSTSFL=new ButtonGroup();
			btgSTSFL.add(rdbSTSYS); 
			btgSTSFL.add(rdbSTSNO);  
			
			add(new JLabel("License Type:"),6,3,1,2,this,'L');
			add(cmbLICTP = new JComboBox(),6,5,1,5,this,'L');
			
			cmbLICTP.addItem("Select");
			hstLICTP = new Hashtable<String,String>();
			hstLICTP_DS = new Hashtable<String,String>();
			String L_strSQLQRY2= "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='SAXXLTP'";
			ResultSet L_rstRSSET2= cl_dat.exeSQLQRY1(L_strSQLQRY2);
			if(L_rstRSSET2 != null)
			{
				while(L_rstRSSET2.next())
				{
					//hstLICTP.put("Select","XX");
					hstLICTP.put(L_rstRSSET2.getString("CMT_CODDS"),L_rstRSSET2.getString("CMT_CODCD"));
					hstLICTP_DS.put(nvlSTRVL(L_rstRSSET2.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET2.getString("CMT_CODDS"),""));
					cmbLICTP.addItem(L_rstRSSET2.getString("CMT_CODDS"));
				}
				L_rstRSSET2.close();
			}
			cmbLICTP.addActionListener(this);
			
		    add(new JLabel("No.of License Procured:"),6,10,1,4,this,'L');
		    add(txtPROQT= new TxtNumLimit(4),6,13,1,2,this,'L'); 
		    
		    add(new JLabel("No.of License Utilized:"),6,15,1,3,this,'L');
		    add(txtUTLQT = new TxtNumLimit(4),6,18,1,2,this,'L'); 
		    
			add(chkMACNM=new JCheckBox("Installed S/W On"),4,15,1,3,this,'L'); 
		    //add(btnSWREP= new JButton("Report"),6,15,1,2,this,'L');
			
		    String[] L_strTBLHD = {"","Sys Type","Vendor Code","Vendor Name","License Date","License No","View","License Qty","Utility Qty","Lic.Purpose"};
    		int[] L_intCOLSZ = {10,20,80,100,80,340,20,75,75,100};
    		tblLICDL= crtTBLPNL1(this,L_strTBLHD,100,8,3,10,17,L_intCOLSZ,new int[]{0,TB1_VEWAT});
    		
    		tblLICDL.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
    		tblLICDL.setCellEditor(TB1_SYSTP,txtSYSTP=new TxtLimit(2));
    		tblLICDL.setCellEditor(TB1_VENCD,txtVENCD=new TxtLimit(5));
    		tblLICDL.setCellEditor(TB1_LICDT,txtLICDT = new TxtDate());  
    		tblLICDL.setCellEditor(TB1_LICNO,txtLICNO=new TxtLimit(40));
    		tblLICDL.setCellEditor(TB1_LICQT,txtLICQT=new TxtNumLimit(4));
    		tblLICDL.setCellEditor(TB1_UTLQT,txtUTLVL=new TxtNumLimit(4));
    		tblLICDL.setCellEditor(TB1_LICDS,txtLICDS=new TxtLimit(50));
    		tblLICDL.setCellEditor(TB1_VEWAT,chkVEWAT = new JCheckBox());
    		//tblLICDL.setCellEditor(TB1_LICTP,cmbLICTP);
    		txtVENCD.addKeyListener(this);
    		txtLICNO.addMouseListener(this);
    		
			oINPVF=new INPVF();
			txtSFTCD.setInputVerifier(oINPVF);
			txtSRLNO.setInputVerifier(oINPVF);
			
			objTBLVRF = new TBLINPVF();
    		tblLICDL.setInputVerifier(objTBLVRF);
    		   
    		((JCheckBox) tblLICDL.cmpEDITR[TB1_VEWAT]).addMouseListener(this);
    		chkMACNM.addMouseListener(this);
    		chkMACNM.setEnabled(false);
    			
			hstSFTCT = new Hashtable<String,String>();
			String L_strSQLQRY1= "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='SAXXSCT'  ";
			ResultSet L_rstRSSET1= cl_dat.exeSQLQRY1(L_strSQLQRY1);
			if(L_rstRSSET1 != null)
			{
				while(L_rstRSSET1.next())
				{
					hstSFTCT.put(nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),""),nvlSTRVL(L_rstRSSET1.getString("CMT_CODCD"),""));
					cmbSFTCT.addItem(L_rstRSSET1.getString("CMT_CODDS"));
				}
				L_rstRSSET1.close();
			}
			
			hstVENDS = new Hashtable<String,String>();
			String L_strSQLQRY4=" SELECT PT_PRTCD,PT_PRTNM from CO_PTMST ";
			L_strSQLQRY4 += "WHERE PT_PRTTP='S' AND ifnull(PT_STSFL,'')<>'X'";
			ResultSet L_rstRSSET4= cl_dat.exeSQLQRY1(L_strSQLQRY4);
			if(L_rstRSSET4 != null)
			{
				while(L_rstRSSET4.next())
				{
					hstVENDS.put(L_rstRSSET4.getString("PT_PRTCD"),nvlSTRVL(L_rstRSSET4.getString("PT_PRTNM"),""));
				}
				L_rstRSSET4.close();
			}
			
			hstSFTCD = new Hashtable<String,String>();
			hstSRLNO = new Hashtable<String,String>();
			String L_strSQLQRY3 = " SELECT distinct SW_SFTCD,SW_SFTCT,SW_SFTDS,SW_SRLNO from SA_SWMST where ifnull(SW_STSFL,' ') <> 'X' ";
			//and SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' AND SW_SFTCD='"+txtSFTCD.getText().toString()+"'";
			ResultSet L_rstRSSET3= cl_dat.exeSQLQRY1(L_strSQLQRY3);
			if(L_rstRSSET3 != null)
			{
				while(L_rstRSSET3.next())
				{
					hstSFTCD.put(L_rstRSSET3.getString("SW_SFTCT")+"|"+L_rstRSSET3.getString("SW_SFTCD"),nvlSTRVL(L_rstRSSET3.getString("SW_SFTDS"),"")+"!"+nvlSTRVL(L_rstRSSET3.getString("SW_SRLNO"),""));
					hstSRLNO.put(L_rstRSSET3.getString("SW_SFTCT")+"|"+L_rstRSSET3.getString("SW_SFTCD")+"|"+L_rstRSSET3.getString("SW_SRLNO"),nvlSTRVL(L_rstRSSET3.getString("SW_SFTDS"),""));
				}
				L_rstRSSET3.close();
			}
			rdbSTSYS.setSelected(true);
			
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
				tblLICDL.cmpEDITR[TB1_VENNM].setEnabled(false);
				tblLICDL.cmpEDITR[TB1_UTLQT].setEnabled(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					//txtSYSTP.setText(M_strSBSCD.substring(0,2));
					//txtSYSTP.requestFocus();
					setENBL(true);
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						txtSFTDS.setEnabled(false);
						txtPROQT.setEnabled(false);
						txtUTLQT.setEnabled(false);
						rdbSTSYS.setEnabled(false);
						rdbSTSNO.setEnabled(false);
						tblLICDL.setEnabled(false);
						tblLICDL.cmpEDITR[TB1_CHKFL].setEnabled(true);
					}
					else
					{
						txtSFTDS.setEnabled(true);
						txtPROQT.setEnabled(true);
						txtUTLQT.setEnabled(true);
						rdbSTSYS.setEnabled(true);
						rdbSTSNO.setEnabled(true);
						tblLICDL.setEnabled(true);
					}
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						chkMACNM.setEnabled(true);
					else
						chkMACNM.setEnabled(false);
				}
				else
					setENBL(false);
				
			}
			else if(M_objSOURC==cmbSFTCT)
			{
				if(cmbSFTCT.getSelectedIndex()>=0)
				{
					txtSFTCD.setText(""); 
					txtSFTDS.setText(""); 
					txtSRLNO.setText(""); 
					clrCOMP1();
				}
				if(cmbSFTCT.getSelectedItem().toString().equals("Communication") || cmbSFTCT.getSelectedItem().toString().equals("Freewares") || cmbSFTCT.getSelectedItem().toString().equals("Miscellaneous"))
				{
					cmbLICTP.setEnabled(false);
					txtPROQT.setEnabled(false);
					txtUTLQT.setEnabled(false);
				}
				else
				{
					cmbLICTP.setEnabled(true);
					txtPROQT.setEnabled(true);
					txtUTLQT.setEnabled(true);
				}
			}
			else if(M_objSOURC ==cmbLICTP)
			{
				if(cmbLICTP.getSelectedIndex()>0)
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						clrCOMP1();
						getSWMST();
						getSWTRN();
					}
					txtPROQT.setEnabled(true);
					txtUTLQT.setEnabled(true);
				}
				else
				{
					txtPROQT.setEnabled(false);
					txtUTLQT.setEnabled(false);
				}
			}
			
			else if(M_objSOURC == rdbSTSNO)
				txtUTLQT.setText("0");
		}
		catch(Exception L_EX) 
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			if(M_objSOURC==chkVEWAT)
			{	
				if(chkVEWAT.isSelected())
				{
					if(tblLICDL.getValueAt(tblLICDL.getSelectedRow(),TB1_LICNO).toString().length()>0)
						dspMACNM();
				}
				
			}
			else if (M_objSOURC==chkMACNM)
			{
				if(chkMACNM.isSelected())
				{
					dspMACNM();
				}
			}
			else if (M_objSOURC==txtLICNO)
				tblLICDL.setValueAt(M_strSBSCD.substring(0,2),tblLICDL.getSelectedRow(),TB1_SYSTP);
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
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
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strHLPFLD = "txtSFTCD";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
						
						M_strSQLQRY = " SELECT distinct SW_SFTCD,SW_SFTDS,SW_SRLNO from SA_SWMST where ifnull(SW_STSFL,' ') <> 'X'";
						M_strSQLQRY += " AND SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
						if(txtSFTCD.getText().length() >0)
							M_strSQLQRY += " AND SW_SFTCD like '"+txtSFTCD.getText().trim()+"%'";
						M_strSQLQRY += " order by SW_SFTCD,SW_SRLNO";
						//System.out.println("txtSFTCD>>"+M_strSQLQRY);
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Software Code","Description","Serial No"},3,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
					}	
				}
				else if(M_objSOURC==txtVENCD)	
	    		{
					  cl_dat.M_flgHELPFL_pbst = true;	
					  M_strHLPFLD = "txtVENCD";	
		    		  String L_ARRHDR[] = {"Vendor Code","Vendor Name","Vendor Address","Vendor City "};
		    		  M_strSQLQRY=" SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST ";
					  M_strSQLQRY += "WHERE PT_PRTTP='S' AND ifnull(PT_STSFL,'')<>'X'";
					  if(txtVENCD.getText().toString().length() >0)
						  M_strSQLQRY += " AND PT_PRTCD like '"+txtVENCD.getText().toString().trim().toUpperCase()+"%'";
					  M_strSQLQRY += " order by PT_PRTCD";
					  //System.out.println("txtVENCD f1>>"+M_strSQLQRY);
		    		  cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,4,"CT");
	    		}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				/*if(M_objSOURC == txtSYSTP)
				{
					cmbSFTCT.requestFocus();
					setMSG("Please Select Software Category....",'N');
				}
				else*/
				if(M_objSOURC == cmbSFTCT)
				{
					txtSFTCD.requestFocus();
					setMSG("Enter Software Code or Press F1 to Select from List....",'N');
				}
				else if(M_objSOURC == txtSFTCD)
				{
					if(txtSFTCD.getText().length()==0)
					{
						txtSFTDS.setText(""); 
						txtSRLNO.setText(""); 
						clrCOMP1();
					}
					else
					{
						txtSFTCD.setText(txtSFTCD.getText().toUpperCase());
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							txtSFTDS.requestFocus();
							setMSG("Enter Software Description..",'N');
						}
						else
							txtSRLNO.requestFocus();
						
					}
				}
				else if(M_objSOURC == txtSFTDS)
				{
					if(txtSFTDS.getText().length()>0)
					{                                                                                                                            
						txtSFTDS.setText(txtSFTDS.getText().replace("'","`"));
						txtSRLNO.requestFocus();
						setMSG("Enter Serial No..",'N');
					}
				}
				else if(M_objSOURC == txtSRLNO)
				{
					if(txtSRLNO.getText().length()>0)
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							clrCOMP1();
							cmbLICTP.setSelectedIndex(0);
							getSWMST();
							getSWTRN();
						}
						txtSRLNO.setText(txtSRLNO.getText().toUpperCase());
						txtPROQT.requestFocus();
						setMSG("Enter No.of License Procured..",'N');
					}
				}
				else if(M_objSOURC ==txtPROQT)
				{
					txtUTLQT.requestFocus();
					setMSG("Enter No.of Lisense Utilized..",'N');
				}
				else if(M_objSOURC ==txtUTLQT)
				{
					tblLICDL.requestFocus();
					setMSG("Enter Vendor Code or Press F1 to Select from List....",'N');
				}
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
				 txtSFTDS.setText(L_STRTKN.nextToken());
				 txtSRLNO.setText(L_STRTKN.nextToken());
				 txtSFTCD.setEnabled(false);
				 txtSRLNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtVENCD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtVENCD.setText(L_STRTKN.nextToken());
				 tblLICDL.setValueAt(L_STRTKN.nextToken(),tblLICDL.getSelectedRow(),TB1_VENNM);
				
			}
			 txtSFTCD.setEnabled(true);
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
			/*if(txtSYSTP.getText().trim().length() ==0)
	    	{
				txtSYSTP.requestFocus();
	    		setMSG("Enter System Type",'E');
	    		return false;
	    	}*/
			if(txtSFTCD.getText().trim().length() ==0)
	    	{
				txtSFTCD.requestFocus();
	    		setMSG("Enter Software Code ",'E');
	    		return false;
	    	}
			else if(txtSFTDS.getText().trim().length() ==0)
	    	{
				txtSFTDS.requestFocus();
	    		setMSG("Enter Software Description",'E');
	    		return false;
	    	}
			else if(txtSRLNO.getText().trim().length() ==0)
	    	{
				txtSRLNO.requestFocus();
	    		setMSG("Enter Serial No",'E');
	    		return false;
	    	}
			else if((rdbSTSYS.isSelected()==false) && (rdbSTSNO.isSelected()==false))
			{
				rdbSTSYS.requestFocus();
				setMSG("Select either Yes or No Usage Status",'E');
				return false;
			}
			else if(!cmbSFTCT.getSelectedItem().toString().equals("Speciality Software") || !cmbSFTCT.getSelectedItem().toString().equals("Communication") || cmbSFTCT.getSelectedItem().toString().equals("Freewares") || cmbSFTCT.getSelectedItem().toString().equals("Miscellaneous"))
			{
				if(cmbLICTP.getSelectedItem().toString().equals("Select"))
				{
					cmbLICTP.requestFocus();
					setMSG("Select Valid License Type",'E');
					return false;
				}
			}
				
			boolean L_flgCHKFL= false;
	    	for(int k=0;k<tblLICDL.getRowCount();k++)
			{
				if( tblLICDL.getValueAt(k,TB1_CHKFL).toString().equals("true"))
				{
					L_flgCHKFL= true;
				}
			}
			
			if(L_flgCHKFL== false)
			{
				if(tblLICDL.getValueAt(tblLICDL.getSelectedRow(),TB1_VENCD).toString().length()>0)
				{
					setMSG("Please Select Atleast one row..",'E');				
					return false;
				}
			}	
			for(int P_intROWNO=0;P_intROWNO<tblLICDL.getRowCount();P_intROWNO++)
			{
				if(tblLICDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					/*if(tblLICDL.getValueAt(P_intROWNO,TB1_LICTP).toString().equals("Select"))
					{
						setMSG("Please Select Valid License Type for Software Code "+txtSFTCD.getText()+"..",'E');
						return false;
					}*/
					/*else if(tblLICDL.getValueAt(P_intROWNO,TB1_VENCD).toString().length()==0)
					{
			    		setMSG("Please Enter Vendor Code or Press F1 to Select from List..",'E');
			    		return false;
					}*/
					if(tblLICDL.getValueAt(P_intROWNO,TB1_SYSTP).toString().length()==0)
					{
			    		setMSG("Enter Company Code..",'E');
			    		return false;
					}
					else if(tblLICDL.getValueAt(P_intROWNO,TB1_LICQT).toString().length()==0)
					{
			    		setMSG("Enter License Quantity..",'E');
			    		return false;
					}
					else if(tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString().length()==0)
					{
			    		setMSG("Enter License No..",'E');
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
		if(rdbSTSYS.isSelected())
			L_strSTSFL = "Y";
		else if(rdbSTSNO.isSelected())
			L_strSTSFL = "N";
		try
		{
			int L_intLICQT=0;
			if(!vldDATA()) 
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		  	{
				exeADDREC();
				for(P_intROWNO=0;P_intROWNO<tblLICDL.getRowCount();P_intROWNO++)
				{
					if(tblLICDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeADDLIC(P_intROWNO); 
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		  	{
				exeMODREC();
				for(P_intROWNO=0;P_intROWNO<tblLICDL.getRowCount();P_intROWNO++)
				{
					if(tblLICDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeADDLIC(P_intROWNO); 
						exeMODMCT(P_intROWNO);
					}
				}
				if(tblLICDL.getValueAt(tblLICDL.getSelectedRow(),TB1_LICNO).toString().length()==0)
					exeMODMCT(tblLICDL.getSelectedRow());
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
				if(tblLICDL.getValueAt(tblLICDL.getSelectedRow(),TB1_LICNO).toString().length()==0)
					exeDELREC();
				for(P_intROWNO=0;P_intROWNO<tblLICDL.getRowCount();P_intROWNO++)
				{
					if(tblLICDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeDELLIC(P_intROWNO); 
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
				//txtSYSTP.setText(M_strSBSCD.substring(0,2));
				//txtSYSTP.requestFocus();
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
	private void exeADDREC()
	{ 
		try
		{
			  this.setCursor(cl_dat.M_curWTSTS_pbst);
			  cl_dat.M_flgLCUPD_pbst = true;
			  M_strSQLQRY =" insert into SA_SWMST(SW_SFTCT,SW_SFTCD,SW_SFTDS,SW_SRLNO,SW_LICTP,SW_PROQT,SW_UTLQT,SW_STSFL,SW_LUSBY,SW_LUPDT)"
		         +" VALUES('"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"',";
		      M_strSQLQRY += "'"+txtSFTCD.getText().toString()+"',";
			  M_strSQLQRY += "'"+txtSFTDS.getText().toString()+"',";	
			  M_strSQLQRY += "'"+txtSRLNO.getText().toString()+"',";
			  
			  if(cmbLICTP.getSelectedIndex()>0)
				  M_strSQLQRY += "'"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"',";
			  else
				  M_strSQLQRY += "'XX',";
			  
			  if(txtPROQT.getText().toString().length()>0)
				  M_strSQLQRY += "'"+txtPROQT.getText().toString()+"',";
			  else
				  M_strSQLQRY += "0,";
			  if(txtUTLQT.getText().toString().length()>0)
				  M_strSQLQRY += "'"+txtUTLQT.getText().toString()+"',";
			  else
				  M_strSQLQRY += "0,";
			  M_strSQLQRY += "'"+L_strSTSFL+ "',";
			  M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
			 
			  //System.out.println(">>>Insert>>"+ M_strSQLQRY );
			  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			  hstSFTCD.put(hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"|"+txtSFTCD.getText().toString(),txtSFTDS.getText().toString()+"!"+nvlSTRVL(txtSRLNO.getText().toString(),""));
			  hstSRLNO.put(hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"|"+txtSFTCD.getText().toString()+"|"+txtSRLNO.getText().toString(),txtSFTDS.getText().toString());
			  setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			 cl_dat.exeDBCMT("exeADDREC");
			 setMSG(L_EX,"exeADDREC()"); 
		}
	}
	
	
	/** Method to update Records in SA_SWMST table.
*/ 
	private void exeMODREC() 
	{
	    try
	    {
	    	this.setCursor(cl_dat.M_curWTSTS_pbst);
		    String strSQLQRY=" select count(*) from SA_SWMST ";
			strSQLQRY+=" where SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' AND SW_SFTCD='"+txtSFTCD.getText().toString()+"' AND SW_SRLNO='"+txtSRLNO.getText().toString()+"'";
			strSQLQRY += " and ifnull(SW_STSFL,'')<>'X'";
			if(cmbLICTP.getSelectedIndex()>0)
				strSQLQRY += " AND SW_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
			else
				strSQLQRY += " AND SW_LICTP='XX'";
			ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			//System.out.println(">>>Count2>>"+strSQLQRY);
			if(rstRSSET.next() && rstRSSET!= null)
			{
				if(rstRSSET.getInt(1)>0)
				{
			    	this.setCursor(cl_dat.M_curWTSTS_pbst);
			    	cl_dat.M_flgLCUPD_pbst = true;
					M_strSQLQRY = " Update SA_SWMST set";
			    	M_strSQLQRY += " SW_SFTDS='"+txtSFTDS.getText().toString().trim()+"',";
			    	M_strSQLQRY +=" SW_STSFL='"+L_strSTSFL+"',";
			    	if(txtPROQT.getText().toString().length()>0)
			    		M_strSQLQRY += " SW_PROQT='"+txtPROQT.getText()+"',";
			    	else
			    		M_strSQLQRY += " SW_PROQT=0,";
			    
					if(txtUTLQT.getText().toString().length()>0)
			    		M_strSQLQRY += " SW_UTLQT='"+txtUTLQT.getText()+"',";
					else
			    		M_strSQLQRY += "SW_UTLQT=0,";
					M_strSQLQRY +=" SW_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +=" SW_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY +=" where SW_SFTCT= '"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
					M_strSQLQRY +=" AND SW_SFTCD= '"+txtSFTCD.getText()+"'";
					M_strSQLQRY +=" AND SW_SRLNO= '"+txtSRLNO.getText().toString()+"'";
					if(cmbLICTP.getSelectedIndex()>0)
						M_strSQLQRY +=" AND SW_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
					else
						strSQLQRY += " AND SW_LICTP='XX'";
					//M_strSQLQRY +="  AND SW_SYSTP = '"+txtSYSTP.getText().toString()+"' ";
					//System.out.println(">>>update>>"+M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
				}
				else
				{
					exeADDREC();
				}
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		 }
	    catch(Exception L_EX)
	    {
	    	
	        setMSG(L_EX,"exeMODREC()");
	    }
	}
	
	/**
	 * Delete Records From SA_SWMST Table*/
		private void exeMODMCT(int P_intROWNO) 
		{ 
		  try
		  {
			  	String strSQLQRY=" select count(*) from HW_MCTRN ";
				//strSQLQRY+=" where MCT_SYSTP='"+tblLICDL.getValueAt(P_intROWNO,TB1_SYSTP).toString().trim()+"' ";
				strSQLQRY+=" where MCT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' AND MCT_SFTCD='"+txtSFTCD.getText().toString()+"'";
				strSQLQRY+="  and ifnull(MCT_STSFL,'')<>'X' AND MCT_SWSRL='"+txtSRLNO.getText().toString()+"'";
				strSQLQRY += " AND MCT_LICNO='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"' ";
				
				ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count2>>"+strSQLQRY);
				if(rstRSSET.next() && rstRSSET!= null)
				{
					if(rstRSSET.getInt(1)>0)
					{
					  	M_strSQLQRY = "UPDATE HW_MCTRN SET";	
					  	if(cmbLICTP.getSelectedIndex()>0)
					  		M_strSQLQRY +="	MCT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
					  	else
					  		M_strSQLQRY +="	MCT_LICTP='XX'";
						M_strSQLQRY +=" where MCT_SFTCT = '"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' ";
						M_strSQLQRY +=" AND MCT_SFTCD = '"+txtSFTCD.getText()+"' ";
						M_strSQLQRY +=" AND MCT_SWSRL = '"+txtSRLNO.getText().toString()+"' ";
						M_strSQLQRY +=" AND MCT_LICNO='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"' ";
						
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						//System.out.println(">>>Delete>>"+M_strSQLQRY);
					}
				}
		  }
		  catch(Exception L_EX)
		  {
		     setMSG(L_EX,"exeMODMCT()");		
		  }
		}
	
	/**
	 * Delete Records From SA_SWMST Table*/
		private void exeDELREC() 
		{ 
		  try
		  {
			  	M_strSQLQRY = "UPDATE SA_SWMST SET";	
				M_strSQLQRY +="	SW_STSFL='X'";	
				M_strSQLQRY +=" where SW_SFTCT= '"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' ";
				M_strSQLQRY +=" AND SW_SFTCD='"+txtSFTCD.getText()+"'";
				M_strSQLQRY +=" AND SW_SRLNO='"+txtSRLNO.getText().toString()+"'";
				M_strSQLQRY +=" AND SW_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"',";
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				//System.out.println(">>>Delete>>"+M_strSQLQRY);
		  }
		  catch(Exception L_EX)
		  {
		     setMSG(L_EX,"exeDELREC()");		
		  }
		}
	
	 /** Method to insert records in SA_SWTRN(SOFTWARE TRANSACTION) table.  */
	  private void exeADDLIC(int P_intROWNO)
	  { 
		  try
		  {
			  	this.setCursor(cl_dat.M_curWTSTS_pbst);
			    String strSQLQRY=" select count(*) from SA_SWTRN ";
				strSQLQRY+=" where SWT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' AND SWT_SFTCD='"+txtSFTCD.getText().toString()+"' AND SWT_SRLNO='"+txtSRLNO.getText().toString()+"'";
				strSQLQRY += " AND SWT_LICNO='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"' and ifnull(SWT_STSFL,'')<>'X'";
				
				ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count2>>"+strSQLQRY);
				if(rstRSSET.next() && rstRSSET!= null)
				{
					if(rstRSSET.getInt(1)>0)
					{
						M_strSQLQRY = " Update SA_SWTRN set";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_VENCD).toString().length()>0)
							M_strSQLQRY +=" SWT_VENCD='"+tblLICDL.getValueAt(P_intROWNO,TB1_VENCD).toString()+"',";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_LICDS).toString().length()>0)
							M_strSQLQRY +=" SWT_LICDS='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICDS).toString()+"',";
						//if(cmbLICTP.getSelectedIndex()>0)
				    	M_strSQLQRY +=" SWT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"',";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_LICDT).toString().length()>0)
							M_strSQLQRY += " SWT_LICDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblLICDL.getValueAt(P_intROWNO,TB1_LICDT).toString()))+"',";
						M_strSQLQRY += " SWT_SYSTP= '"+tblLICDL.getValueAt(P_intROWNO,TB1_SYSTP).toString().trim()+"',";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString().length()>0)
							M_strSQLQRY += " SWT_LICNO='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString()+"',";
						
						if(tblLICDL.getValueAt(P_intROWNO,TB1_UTLQT).toString().length()>0)
							M_strSQLQRY += " SWT_UTLQT='"+tblLICDL.getValueAt(P_intROWNO,TB1_UTLQT).toString()+"',";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_LICQT).toString().length()>0)
							M_strSQLQRY += " SWT_LICQT='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICQT).toString()+"',";
						
						M_strSQLQRY += " SWT_STSFL = '"+L_strSTSFL+"',";
						M_strSQLQRY += " SWT_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += " SWT_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						//M_strSQLQRY += " where SWT_SYSTP= '"+tblLICDL.getValueAt(P_intROWNO,TB1_SYSTP).toString().trim()+"'";
						M_strSQLQRY += " where SWT_SFTCD= '"+txtSFTCD.getText().toString()+"'";
						M_strSQLQRY += " AND SWT_SRLNO='"+txtSRLNO.getText().toString()+"'";
						M_strSQLQRY += " AND SWT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString().length()>0)
							M_strSQLQRY += " AND SWT_LICNO='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString()+"'";
						//M_strSQLQRY += " AND SWT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
						//System.out.println(">>>update2>>"+M_strSQLQRY);  
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else
					{
				  		cl_dat.M_flgLCUPD_pbst = true;
				  		M_strSQLQRY =" insert into SA_SWTRN(SWT_SYSTP,SWT_SFTCT,SWT_SFTCD,SWT_SRLNO,SWT_VENCD,SWT_LICDT,SWT_LICTP,SWT_LICNO,SWT_LICQT,SWT_UTLQT,SWT_LICDS,SWT_STSFL,SWT_LUSBY,SWT_LUPDT)";
						M_strSQLQRY += " values (";
						M_strSQLQRY += "'"+tblLICDL.getValueAt(P_intROWNO,TB1_SYSTP).toString().trim()+"',";
						
						M_strSQLQRY += "'"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"',";
						
						M_strSQLQRY += "'"+txtSFTCD.getText().toString()+"',";
						
				  	    M_strSQLQRY += "'"+txtSRLNO.getText().toString()+"',";
				  	 
				  	    M_strSQLQRY += "'"+tblLICDL.getValueAt(P_intROWNO,TB1_VENCD).toString()+"',";
				  	  
						if(tblLICDL.getValueAt(P_intROWNO,TB1_LICDT).toString().length()>0)
							M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(tblLICDL.getValueAt(P_intROWNO,TB1_LICDT).toString()))+"',";
						else 
							  M_strSQLQRY += "null,"; 
						//if(cmbLICTP.getSelectedIndex()>0)
						M_strSQLQRY += "'"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"',";
				    	
						M_strSQLQRY += "'"+tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString().trim()+"',";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_LICQT).toString().length()>0)
							M_strSQLQRY += "'"+tblLICDL.getValueAt(P_intROWNO,TB1_LICQT).toString()+"',";
						else
							M_strSQLQRY += "0,";
						if(tblLICDL.getValueAt(P_intROWNO,TB1_UTLQT).toString().length()>0)
							M_strSQLQRY += "'"+tblLICDL.getValueAt(P_intROWNO,TB1_UTLQT).toString()+"',";
						else
							M_strSQLQRY += "0,";
					
						M_strSQLQRY += "'"+tblLICDL.getValueAt(P_intROWNO,TB1_LICDS).toString()+"',";
						
						M_strSQLQRY += "'"+L_strSTSFL+"',";
					
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						//System.out.println("insert2"+M_strSQLQRY);
						
					}
				}
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false;
			  cl_dat.exeDBCMT("exeADDLIC");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDLIC()");
		  }
	  }
	  
  /**
	 * Delete Records From SA_SWTRN Table*/
		private void exeDELLIC(int P_intROWNO) 
		{ 
			try
			{
			    M_strSQLQRY = "UPDATE SA_SWTRN SET";	
				M_strSQLQRY +=" SWT_STSFL='X'";	
				M_strSQLQRY +=" where SWT_SFTCD='"+txtSFTCD.getText()+"'";
				M_strSQLQRY +=" AND SWT_SRLNO='"+txtSRLNO.getText().toString()+"'";
				//M_strSQLQRY += " AND SWT_SYSTP = '"+tblLICDL.getValueAt(P_intROWNO,TB1_SYSTP).toString().trim()+"' ";
				M_strSQLQRY += " AND SWT_SFTCT = '"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
				M_strSQLQRY += " AND SWT_LICNO='"+tblLICDL.getValueAt(P_intROWNO,TB1_LICNO).toString()+"'";
				M_strSQLQRY += " AND SWT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				//System.out.println(">>>Delete2>>"+M_strSQLQRY);
				
			}
			catch(Exception L_EX)
			{
		     setMSG(L_EX,"exeDELLIC()");		
			}
		}
  /**
  * Method to setdata in respective text box from SA_SWMST & SA_SWTRN database table*/
  	private void getSWMST() 
  	{
  		try
  		{ 
  			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Records ...",'N');
		
  			M_strSQLQRY= " SELECT distinct SW_SFTDS,SW_STSFL,SW_PROQT,SW_UTLQT,SW_LICTP";
  			M_strSQLQRY+= " from SA_SWMST";
  			M_strSQLQRY+= " where ifnull(SW_STSFL,'')<>'X'";
  			M_strSQLQRY+= " and SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' AND SW_SFTCD = '"+txtSFTCD.getText()+"' AND SW_SRLNO = '"+txtSRLNO.getText()+"'";
  			//M_strSQLQRY+= L_strLTQRY;
  			if(cmbLICTP.getSelectedIndex()>0)
				M_strSQLQRY+= " and SW_LICTP = '"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
			else
  				M_strSQLQRY+= " and SW_LICTP='XX'";
  			//System.out.println(">>>select>>"+ M_strSQLQRY );
  			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY); 
  			if(M_rstRSSET != null )
  			{
  				while(M_rstRSSET.next())
  				{
  					txtSFTDS.setText(nvlSTRVL(M_rstRSSET.getString("SW_SFTDS"),""));
	  				
	  				if(nvlSTRVL(M_rstRSSET.getString("SW_STSFL"),"").equals("Y"))
	  					rdbSTSYS.setSelected(true);
					else
						rdbSTSYS.setSelected(false);
	  			
					if(nvlSTRVL(M_rstRSSET.getString("SW_STSFL"),"").equals("N"))
					{
						rdbSTSNO.setSelected(true);
						rdbSTSYS.setSelected(false);
					}
					else
						rdbSTSNO.setSelected(false);
					
					txtPROQT.setText(nvlSTRVL(M_rstRSSET.getString("SW_PROQT"),""));
	  				txtUTLQT.setText(nvlSTRVL(M_rstRSSET.getString("SW_UTLQT"),""));
	  				
	  				if(!M_rstRSSET.getString("SW_LICTP").equals("XX"))
	  					cmbLICTP.setSelectedItem(hstLICTP_DS.get(M_rstRSSET.getString("SW_LICTP")));
					else
						cmbLICTP.setSelectedIndex(0);
  				}
  			}
            M_rstRSSET.close();
           //if(L_CNT==0)
        	//setMSG("No Data Found in S/W Transaction..",'N'); 
            this.setCursor(cl_dat.M_curDFSTS_pbst);
        }	
  		catch(Exception L_EX)
  		{
  			
  			setMSG(L_EX,"getSWMST()"); 
  		}
  	}
	private void getSWTRN() 
  	{
  		try
  		{ 
  			this.setCursor(cl_dat.M_curWTSTS_pbst);
  			inlTBLEDIT(tblLICDL);
  			tblLICDL.clrTABLE();
  			setMSG("Fetching Records ...",'N');
			int L_CNT=0;
			//M_strSQLQRY= " SELECT distinct SW_SFTDS,SW_STSFL,SW_PROQT,SW_UTLQT,SWT_VENCD,SWT_LICDT,SWT_LICTP,SWT_LICNO,SWT_LICQT,SWT_STSFL,PT_PRTNM";
			//M_strSQLQRY+= " from SA_SWMST,SA_SWTRN,CO_PTMST";
			//M_strSQLQRY+= " where SW_SYSTP=SWT_SYSTP AND SW_SFTCT=SWT_SFTCT AND SW_SFTCD=SWT_SFTCD and SW_SRLNO=SWT_SRLNO and ifnull(SWT_STSFL,'')<>'X' AND SWT_VENCD=PT_PRTCD AND PT_PRTTP='S' AND ifnull(PT_STSFL,'')<>'X'";
			//M_strSQLQRY+= " and SW_SYSTP='"+txtSYSTP.getText().toString()+"' and SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' AND SW_SFTCD = '"+txtSFTCD.getText()+"' AND SW_SRLNO = '"+txtSRLNO.getText()+"'";
  			String M_strSQLQRY1= " SELECT SWT_SYSTP,SWT_VENCD,SWT_LICDT,SWT_LICTP,SWT_LICNO,SWT_LICQT,SWT_UTLQT,SWT_LICDS,SWT_STSFL";
			M_strSQLQRY1+= " from SA_SWTRN";
			M_strSQLQRY1+= " where ifnull(SWT_STSFL,'')<>'X'";
			M_strSQLQRY1+= " and SWT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
			M_strSQLQRY1+= " AND SWT_SFTCD = '"+txtSFTCD.getText()+"' AND SWT_SRLNO = '"+txtSRLNO.getText()+"'";
			if(cmbLICTP.getSelectedIndex()>0)
				M_strSQLQRY1+= " and SWT_LICTP = '"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
			else
  				M_strSQLQRY1+= " and SWT_LICTP='XX'";
  		
			//System.out.println(">>>select11>>"+ M_strSQLQRY1 );
			ResultSet M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY1); 
			if(M_rstRSSET1 != null )
			{
				while(M_rstRSSET1.next())
				{
					tblLICDL.setValueAt(M_rstRSSET1.getString("SWT_SYSTP"),L_CNT,TB1_SYSTP);
	  				tblLICDL.setValueAt(M_rstRSSET1.getString("SWT_VENCD"),L_CNT,TB1_VENCD);
	  				if(hstVENDS.containsKey(M_rstRSSET1.getString("SWT_VENCD")))
	  					tblLICDL.setValueAt(hstVENDS.get(M_rstRSSET1.getString("SWT_VENCD")),L_CNT,TB1_VENNM);
	  				
	  				if(!(M_rstRSSET1.getDate("SWT_LICDT")==null))
	  					tblLICDL.setValueAt(M_fmtLCDAT.format(M_rstRSSET1.getDate("SWT_LICDT")),L_CNT,TB1_LICDT);
	  				//tblLICDL.setValueAt(hstLICTP_DS.get(M_rstRSSET.getString("SWT_LICTP")),L_CNT,TB1_LICTP);
	  				tblLICDL.setValueAt(nvlSTRVL(M_rstRSSET1.getString("SWT_LICNO"),""),L_CNT,TB1_LICNO);
	  				tblLICDL.setValueAt(nvlSTRVL(M_rstRSSET1.getString("SWT_LICQT"),"0"),L_CNT,TB1_LICQT);
	  				tblLICDL.setValueAt(nvlSTRVL(M_rstRSSET1.getString("SWT_UTLQT"),"0"),L_CNT,TB1_UTLQT);
	  				tblLICDL.setValueAt(nvlSTRVL(M_rstRSSET1.getString("SWT_LICDS"),""),L_CNT,TB1_LICDS);
	  			
	  				L_CNT++;
				}
			}
			M_rstRSSET1.close();
			 this.setCursor(cl_dat.M_curDFSTS_pbst);
  		 }	
  		catch(Exception L_EX)
  		{
  			
  			setMSG(L_EX,"getSWTRN()"); 
  		}
  	}
  	void inlTBLEDIT(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}
  	
  	/**method to clear component after click on save button &  select option **/
	void clrCOMP1()
	{
		try
		{
			rdbSTSYS.setSelected(true);
			rdbSTSNO.setSelected(false);
			txtPROQT.setText("");
			txtUTLQT.setText("");
  			inlTBLEDIT(tblLICDL);
  			tblLICDL.clrTABLE();
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP1()");			
		}	
	}

	

	/**Method to display Machine Name On which installed Given Software**/
	private void dspMACNM()
	{
		cl_JTable tblMACNM;
		JLabel lblSFTDS;
		JPanel pnlMACNM;
		 int TB2_CHKFL = 0;
		 int TB2_MACNM = 1;
		 int TB2_SRLNO = 2;
		 int TB2_LOCCD = 3;
		 int TB2_VRFDT = 4;
		
		try
		{
			 
			if(txtSFTCD.getText().trim().length() ==0)
	    	{
				txtSFTCD.requestFocus();
	    		setMSG("Enter Software Code",'E');
	    		return ;
	    	}
			else if(txtSRLNO.getText().trim().length() ==0)
	    	{
				txtSRLNO.requestFocus();
	    		setMSG("Enter Serial No..",'E');
	    		return ;
	    	}
			
			//if(pnlMACNM==null)
			{
				pnlMACNM=new JPanel(null);
				String[] L_staCOLHD1 = {"","Machine Name","Serial No","Location","Verify Date"};
				int[] L_inaCOLSZ1 = {10,150,200,150,90};
				add(lblSFTDS = new JLabel(""),1,1,1,10,pnlMACNM,'L');
				tblMACNM = crtTBLPNL1(pnlMACNM,L_staCOLHD1,100,2,1,7,11,L_inaCOLSZ1,new int[]{0});
			}
			
			M_strSQLQRY = "select DISTINCT MCT_MCHCT,MCT_SRLNO,MCT_VRFDT,MC_MACNM,LC_LOCDS from HW_MCTRN,HW_MCMST,HW_LCMST ";
			M_strSQLQRY+= " where MCT_MCHCT=MC_MCHCT AND MCT_SRLNO=MC_SRLNO AND MC_LOCCD=LC_LOCCD";
			M_strSQLQRY+= " AND MCT_SYSTP=LC_SYSTP and MCT_STSFL=MC_STSFL and MCT_STSFL='1' AND MCT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"' AND MCT_SFTCD='"+txtSFTCD.getText().toString()+"'";
			M_strSQLQRY +=" and MCT_SWSRL='"+txtSRLNO.getText().toString()+"' AND  MCT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem()).toString()+"' ";
			if(chkVEWAT.isSelected())
				M_strSQLQRY +=" and MCT_LICNO='"+tblLICDL.getValueAt(tblLICDL.getSelectedRow(),TB1_LICNO).toString()+"'";
			M_strSQLQRY +=" order by MCT_MCHCT,MC_MACNM";
			System.out.println("dspMACNM>>>>"+M_strSQLQRY);
			ResultSet L_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);
			tblMACNM.clrTABLE();
			inlTBLEDIT(tblMACNM);
			int i =0;
			if(L_rstRSSET1 != null)
			{
				while (L_rstRSSET1.next())
				{
					tblMACNM.setValueAt(nvlSTRVL(L_rstRSSET1.getString("MC_MACNM"),""),i,TB2_MACNM);
					tblMACNM.setValueAt(nvlSTRVL(L_rstRSSET1.getString("MCT_SRLNO"),""),i,TB2_SRLNO);
					tblMACNM.setValueAt(nvlSTRVL(L_rstRSSET1.getString("LC_LOCDS"),""),i,TB2_LOCCD);
					if(L_rstRSSET1.getDate("MCT_VRFDT")!=null)
						tblMACNM.setValueAt(M_fmtLCDAT.format(L_rstRSSET1.getDate("MCT_VRFDT")),i,TB2_VRFDT);
					i++;	
				}
				
				L_rstRSSET1.close();
			}
			
			 if(chkVEWAT.isSelected())
				lblSFTDS.setText(tblLICDL.getValueAt(tblLICDL.getSelectedRow(),TB1_LICNO).toString()+" License No On");
			 else if(txtSFTDS.getText().toString().length()>0)
				lblSFTDS.setText("Installed "+txtSFTDS.getText().toString()+" On");

			pnlMACNM.setSize(300,200);
			pnlMACNM.setPreferredSize(new Dimension(600,300));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlMACNM,"Machines Name",JOptionPane.OK_CANCEL_OPTION);
			if( L_intOPTN==0 || L_intOPTN==2 || L_intOPTN==-1)
				chkMACNM.setSelected(false);
		
		}
		catch (Exception L_EX)
		{
			//L_EX.printStackTrace();
			setMSG("Error in dspMACNM : "+L_EX,'E');
		}
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
				
				if(input == txtSFTCD)
				{
					if(txtSFTCD.getText().length()>0)
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							if(hstSFTCD.containsKey(hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"|"+txtSFTCD.getText()))	
							{
								String[] L_strSFTCD;
								L_strSFTCD=hstSFTCD.get(hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"|"+txtSFTCD.getText()).split("!");
								
								txtSFTCD.setText(txtSFTCD.getText());
								txtSFTDS.setText(L_strSFTCD[0]);
								txtSRLNO.setText(L_strSFTCD[1]);
							}
							else 
							{
								setMSG("Enter Valid Software Code",'E');
								return false;
							}
						}
					}
				}
				if(input == txtSRLNO)
				{
					if(txtSRLNO.getText().length()>0)
					{
						if(hstSRLNO.containsKey(hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"|"+txtSFTCD.getText()+"|"+txtSRLNO.getText()))    
						{
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							{
								setMSG("This Records already exist..",'E');
								txtSRLNO.requestFocus();
								return false;
							}
							else
								txtSFTDS.setText(hstSRLNO.get(hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"|"+txtSFTCD.getText()+"|"+txtSRLNO.getText()));   
						}
						else 
						{
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							{
								setMSG("Enter Valid Serial No ..",'E');
								txtSRLNO.requestFocus();
								return false;
							}
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
	
	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
		   	try
		   	{
		   		if(getSource()==tblLICDL)
			    {
		   			if(tblLICDL.getValueAt(P_intROWID,P_intCOLID).toString().length()== 0)
						return true;
					if(P_intCOLID == TB1_VENCD)
					{
						if(tblLICDL.getValueAt(P_intROWID,P_intCOLID).toString().length()>0)
						{	
							M_strSQLQRY=" SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST ";
							M_strSQLQRY += "WHERE PT_PRTTP='S' AND ifnull(PT_STSFL,'')<>'X' and PT_PRTCD='"+tblLICDL.getValueAt(P_intROWID,P_intCOLID).toString().trim().toUpperCase()+"'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								tblLICDL.setValueAt(M_rstRSSET.getString("PT_PRTCD"),P_intROWID,TB1_VENCD);
								tblLICDL.setValueAt(M_rstRSSET.getString("PT_PRTNM"),P_intROWID,TB1_VENNM);
								setMSG("",'N');
							}
							else
							{
								setMSG("Enter Valid Vendor Code ",'E');
								return false;
							}
							M_rstRSSET.close();
						}
						else
							tblLICDL.setValueAt("",P_intROWID,TB1_VENNM);
					}
					else if(P_intCOLID == TB1_LICNO)
    			    {
						if(tblLICDL.getValueAt(P_intROWID,TB1_LICNO).toString().length()>0)
						{
							tblLICDL.setValueAt(tblLICDL.getValueAt(P_intROWID,TB1_LICNO).toString().toUpperCase(),P_intROWID,TB1_LICNO);
							tblLICDL.setValueAt(tblLICDL.getValueAt(P_intROWID,TB1_LICNO).toString().replace("'","`"),P_intROWID,TB1_LICNO);
							tblLICDL.setValueAt("0",P_intROWID,TB1_LICQT);
						}
    			    }
				
					else if(P_intCOLID == TB1_LICDT)
    			    {
						if(tblLICDL.getValueAt(P_intROWID,TB1_LICDT).toString().length()>0)
						{
							if(M_fmtLCDAT.parse(tblLICDL.getValueAt(P_intROWID,TB1_LICDT).toString().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
							{
								setMSG("License Date Should Not be greater than Current Date..",'E');
								return false;
							}
						}
    			    }
					else if(P_intCOLID == TB1_LICDS)
    			    {
						if(tblLICDL.getValueAt(P_intROWID,TB1_LICDS).toString().length()>0)
						{
							tblLICDL.setValueAt(tblLICDL.getValueAt(P_intROWID,TB1_LICDS).toString().toUpperCase(),P_intROWID,TB1_LICDS);
							tblLICDL.setValueAt(tblLICDL.getValueAt(P_intROWID,TB1_LICDS).toString().replace("'","`"),P_intROWID,TB1_LICDS);
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
	
	
	