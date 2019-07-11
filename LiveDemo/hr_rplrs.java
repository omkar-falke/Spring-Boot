/* 						DOCUMENTATION  FOR  LEAVE RECOMMENDING AND SANCTIONING AUTHORITY
 						------------------------------------------------------------------
 1) This program gives you report for leave recommending and sanctioning authorities
    where the user can select the option to show the report order wise with respect to 
           	a)Dept Name
			b)Emp No
			c)Emp Name
			d)Recm Auth
			e)Sanc Auth
 2) All the empno and empnm is taken from hr_epmst and stored in the hash table in the constructor
 3) The table used called	tblVIEW  is used only for display purpose so it is disabled using setENBL(false)
    only the first column is enabled
 4) which ever row the user checks true that rows are only send for report
 5) by default the option overwrite is selected and all the data displayed in tblVIEW are cleared first then written	 
 6) when the user selects append option records are added to the table without removing the previous record
 7) query is executed on selection of combo option and click of DISPLAY button by calling getDATA()
 8) F1 is given for txtDPTCD and txtEMPCD
 */
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class hr_rplrs extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rplrs.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
	private DataOutputStream D_OUT ;
	private JTextField txtEMPCD;
	private JTextField txtDPTCD;
	private JCheckBox chkSELALL;
	private JRadioButton rdbAPPEND,rdbOVERWRT;
	ButtonGroup bg;
	private JLabel lblEMPNM,lblDPTNM;
	private JComboBox cmbORDBY;
	private JButton btnDISPLAY,btnPRINT;
	private int flgEMPCD;
	private int flgDPTCD;
	Hashtable<String,String> hstRCSNNM=new Hashtable<String,String>(); //for getting name of recm and sanc authority from emp eno
   
	private  cl_JTable tblVIEW;
	int TB1_CHKFL = 0; 				
	int TB1_DPTNM = 1;              	
	int TB1_EMPNO = 2;             
	int TB1_EMPNM = 3;             
	int TB1_DESGN = 4;	
	int TB1_RCAUT = 5; 				
	int TB1_SNAUT = 6;      
	int TB1_EMAIL = 7;      

	int intRWCNT=0; //no of row in the table


	hr_rplrs()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,8);	
			add(new JLabel("Employee No"),3,2,1,1,this,'L');
			add(txtEMPCD = new TxtLimit(4),3,3,1,1,this,'L');			
			add(lblEMPNM = new JLabel(),3,4,2,2,this,'L');
			

			add(new JLabel("Department No"),4,2,1,1,this,'L');
			add(txtDPTCD = new TxtLimit(4),4,3,1,1,this,'L');			
			add(lblDPTNM = new JLabel(),4,4,2,2,this,'L');

			add(new JLabel("Order By"),5,2,1,1,this,'L');
			add(cmbORDBY=new JComboBox(new String[]{"Select Option","Dept Name","Emp No","Emp Name","Recm Auth","Sanc Auth"}),5,3,1,1,this,'L');	

			//add(btnPRINT=new JButton("PRINT"),5,7,1,1,this,'L');

			add(chkSELALL=new JCheckBox("Select All"),6,2,1,1,this,'L');			
			add(rdbAPPEND=new JRadioButton("Append"),6,3,1,1,this,'L'); 
			add(rdbOVERWRT=new JRadioButton("Overwrite"),6,4,1,1,this,'L');	
			rdbOVERWRT.setSelected(true);
			bg=new ButtonGroup();
			bg.add(rdbAPPEND);
			bg.add(rdbOVERWRT);;
			
			
			add(btnDISPLAY=new JButton("Display"),6,5,1,1,this,'L');
			
			String[] L_strTBLHD = {"","Department","Employee No","Employee Name","Designation ","Recommending Authority","Sanctioning Auth","Email"};
			int[] L_intCOLSZ = {10,100,100,100,100,100,100,100};
			tblVIEW= crtTBLPNL1(this,L_strTBLHD,800,7,2,10,6,L_intCOLSZ,new int[]{0});//10 and 5 are tbl ht and width wrt matrix
			tblVIEW.setEnabled(false);
			tblVIEW.cmpEDITR[TB1_CHKFL].setEnabled(true); //to enable only the first column						

			M_strSQLQRY ="select ep_empno,ep_shrnm ep_empnm from HR_EPMST where isnull(EP_STSFL,'')<>'X'";
			M_strSQLQRY += " AND EP_LFTDT is NULL ";
				
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{	
				while(M_rstRSSET.next())
				{
					hstRCSNNM.put(M_rstRSSET.getString("ep_empno"), nvlSTRVL(M_rstRSSET.getString("ep_empnm"),""));
					//System.out.println("hsh>>> : "+hstRCSNNM.get(M_rstRSSET.getString("ep_empno")));
					
				}
				M_rstRSSET.close();
			}
			else
			{
				System.out.println(">>>> :null in hash");
			}
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			M_pnlRPFMT.setVisible(true);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			INPVF oINPVF=new INPVF();
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}

	
	public void clrCOMP()
	{
		super.clrCOMP();
		M_pnlRPFMT.setVisible(true);
		M_txtFMDAT.setVisible(false);
		M_txtTODAT.setVisible(false);
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				
			    
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtEMPCD.requestFocus();
					setENBL(true);				
					tblVIEW.setEnabled(false);
					tblVIEW.cmpEDITR[TB1_CHKFL].setEnabled(true); //to enable only the first column						

				}
				else
					setENBL(false);
			}

			if(M_objSOURC==cmbORDBY)
			{
				rdbOVERWRT.setSelected(true);
				tblVIEW.setEnabled(false);
				tblVIEW.cmpEDITR[TB1_CHKFL].setEnabled(true);
			    
				if(cmbORDBY.getSelectedIndex()>0)
				{
					
					//System.out.print("inside my combo");
					if(txtDPTCD.getText().trim().length()==0)
						flgDPTCD=0; //nothing
					if(txtDPTCD.getText().trim().length()>0)
						flgDPTCD=1; //something in text
					if(txtEMPCD.getText().trim().length()==0)
						flgEMPCD=0; //nothing
					if(txtEMPCD.getText().trim().length()>0)
						flgEMPCD=1; //something in text
					getDATA(flgDPTCD,flgEMPCD);
				}


			}

			if(M_objSOURC==rdbOVERWRT)
			{
				if(rdbOVERWRT.isSelected())
				{

					tblVIEW.clrTABLE();
				}

			}
			if(M_objSOURC==chkSELALL)
			{
				//System.out.print("intRWCNT"+intRWCNT);
				if(chkSELALL.isSelected())
					for(int i=0;i<intRWCNT;i++)  //marking check  in the first column of entire table
					{
						if(tblVIEW.getValueAt(i,TB1_EMPNM).toString().trim().length()>0)
						{
							tblVIEW.setValueAt(new Boolean(true),i,TB1_CHKFL);


						}
					}
				if(!chkSELALL.isSelected()) //removing the check mark from the first column of entire table
				{
					for(int i=0;i<intRWCNT;i++)
					{
						if(tblVIEW.getValueAt(i,TB1_EMPNM).toString().trim().length()>0)
						{
							tblVIEW.setValueAt(new Boolean(false),i,TB1_CHKFL);


						}
					}

				}

			}

			if(M_objSOURC==btnDISPLAY)
			{
				if(txtDPTCD.getText().trim().length()==0)
					flgDPTCD=0; //nothing
				if(txtDPTCD.getText().trim().length()>0)
					flgDPTCD=1; //something in text
				if(txtEMPCD.getText().trim().length()==0)
					flgEMPCD=0; //nothing
				if(txtEMPCD.getText().trim().length()>0)
					flgEMPCD=1; //something in text
						    
				getDATA(flgDPTCD,flgEMPCD);
			}
			/*  for giving print option 
				if(M_objSOURC==btnPRINT)
				{
					System.out.println("printer combo clicked");
					JComboBox L_cmbLOCAL = getPRNLS();				
					//doPRINT(cl_dat.M_strREPSTR_pbst+"hr_rplrs.doc",L_cmbLOCAL.getSelectedIndex());
					
				}	
			*/		
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is actionPerformed()");
		}		
	}

	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);

		try
		{
			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
			{
				if(M_objSOURC == txtEMPCD)
					txtDPTCD.requestFocus();
				if(M_objSOURC == txtDPTCD)
					cmbORDBY.requestFocus();
			}
			else if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
				if(M_objSOURC==txtEMPCD)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strHLPFLD = "txtEMPCD";
					String L_ARRHDR[] = {"Employee No","Employee Name"};
					M_strSQLQRY= "select a.ep_empno,a.ep_fstnm + ' ' + SUBSTRING(a.ep_mdlnm,1,1) + '. ' + a.ep_lstnm ep_empnm,a.ep_dptcd,a.ep_dptnm,a.ep_desgn,SUBSTRING(cmt_codcd,6,4) ep_xxxcd, SUBSTRING(b.ep_fstnm,1,1) + '. ' + SUBSTRING(b.ep_mdlnm,1,1) + '. '+ b.ep_lstnm ep_xxxnm"; 
					M_strSQLQRY+= " from hr_epmst a left outer join co_cdtrn on cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HR"+cl_dat.M_strCMPCD_pbst+"LRC' and SUBSTRING(cmt_codcd,1,4) =ltrim(str(a.ep_empno,20,0))";
					M_strSQLQRY+= " left outer join hr_epmst b on SUBSTRING(cmt_codcd,6,4)=ltrim(str(b.ep_empno,20,0)) where a.ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
					if(txtEMPCD.getText().trim().length()>0)
						M_strSQLQRY+= " and a.ep_empno LIKE  '"+txtEMPCD.getText().trim()+"%'";

					M_strSQLQRY+= " and  a.ep_lftdt is null and a.ep_stsfl<>'X' order by a.ep_dptcd,a.ep_empno";
					//System.out.println(">>>>>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");  //L_ARRHDR will tell how many col u want if 2 string means 2 col 4 indicates u are using in exeHLPOK to get 4 token using SQL query
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				
				if(M_objSOURC==txtDPTCD)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strHLPFLD = "txtDPTCD";
					String L_ARRHDR[] = {"Department No","Department Name"};
					M_strSQLQRY= "select distinct ep_dptcd,ep_dptnm"; 
					M_strSQLQRY+= " from hr_epmst";
					M_strSQLQRY+= " where ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
					if(txtEMPCD.getText().trim().length()>0)
						M_strSQLQRY+= " and ep_empno LIKE  '"+txtEMPCD.getText().trim()+"%'";

					M_strSQLQRY+= " and  ep_lftdt is null and ep_stsfl<>'X' order by ep_dptcd";
					//System.out.println(">>>>>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");  //L_ARRHDR will tell how many col u want if 2 string means 2 col 4 indicates u are using in exeHLPOK to get 4 token
					setCursor(cl_dat.M_curDFSTS_pbst);
				}				
				
				
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
			if(M_strHLPFLD.equals("txtEMPCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");			      
				txtEMPCD.setText(L_STRTKN.nextToken());			
				lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
				txtDPTCD.setText(L_STRTKN.nextToken());
				lblDPTNM.setText(L_STRTKN.nextToken());

			}
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");			      
				txtDPTCD.setText(L_STRTKN.nextToken());
				lblDPTNM.setText(L_STRTKN.nextToken());

			}

		}
		catch(Exception E_VR)
		{
			E_VR.printStackTrace();
			setMSG(E_VR,"exeHLPOK()");		
		}
	}

	void genRPTFL()
	{
		try
		{
			int L_CNTDPT=0;
			int L_CNT=0;
			//String L_tmpDPT="firstdpt";//initialized for knowing first  dpt name in table 
			//String L_tmpENO="firsteno";
			//String L_tmpRCM="firstrcm";
			//String L_tmpSCN="firstscn";
			
			String L_strSRTFLD="firstvalue";//initialized for knowing first value in table 
			int L_intSRTCOL= 0;//Sort Column 
			switch(cmbORDBY.getSelectedIndex())
			{
			    case 1: L_intSRTCOL=TB1_DPTNM;break;	
				case 2: L_intSRTCOL=TB1_EMPNO;break;					
				case 3: L_intSRTCOL=TB1_EMPNM;break;					
				case 4: L_intSRTCOL=TB1_RCAUT;break;
				case 5: L_intSRTCOL=TB1_SNAUT;break;
				case 0: L_intSRTCOL=TB1_DPTNM;break;			
			}
			
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();			
			
			//System.out.println("intRWCNT :"+intRWCNT);
			if(M_rdbTEXT.isSelected())
			{
				prnFMTCHR(D_OUT,M_strNOCPI17);  //first always make NOCPI17
				
				prnFMTCHR(D_OUT,M_strCPI12);
			}
			
			//////////////to print the report//////////////////////////
			for(int i=0;i<intRWCNT;i++)
			{		
				
				crtNWLIN();	
				if((tblVIEW.getValueAt(i,0).toString().trim()).equals("true"))
				{
					// put blank then row as per combo option selected
					if(!tblVIEW.getValueAt(i,L_intSRTCOL).toString().trim().equals(L_strSRTFLD.trim()))//if previous row value is not equal to current row value 
						{
							crtNWLIN();
							L_strSRTFLD=tblVIEW.getValueAt(i,L_intSRTCOL).toString().trim();//update the tmp variable with current row value
						}
					D_OUT.writeBytes(padSTRING('R',tblVIEW.getValueAt(i,1).toString(),15)+padSTRING('R',tblVIEW.getValueAt(i,2).toString(),10)+padSTRING('R',tblVIEW.getValueAt(i,3).toString(),25)+padSTRING('R',tblVIEW.getValueAt(i,4).toString(),15)+padSTRING('R',tblVIEW.getValueAt(i,5).toString(),10)+padSTRING('R',tblVIEW.getValueAt(i,6).toString(),10)+padSTRING('R',tblVIEW.getValueAt(i,7).toString(),25)) ;
				}
			}			
			//////////////////report ends/////////////////////////////////
			
			//prnFMTCHR(D_OUT,M_strNOCPI12);
			if(M_rdbTEXT.isSelected())			
			{
				prnFMTCHR(D_OUT,M_strCPI10);	//Reset it to CPI10
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
			L_EX.printStackTrace();
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
				if(cl_dat.M_intLINNO_pbst >70)
				{
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
		{	if(M_rdbTEXT.isSelected())
			{
				prnFMTCHR(D_OUT,M_strNOCPI17);  //first always make NOCPI17
				
				prnFMTCHR(D_OUT,M_strCPI12);
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				//prnFMTCHR(D_OUT,M_strNOCPI17);
				//prnFMTCHR(D_OUT,M_strCPI10);							
				//prnFMTCHR(D_OUT,M_strBOLD);
				//prnFMTCHR(D_OUT,M_strNOENH);
			}	
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title>Attendance Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 14 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;

			crtNWLIN();
			prnFMTCHR(D_OUT,M_strBOLD);
			//prnFMTCHR(D_OUT,M_strCPI12);			
			D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst+"("+cl_dat.M_strCMPLC_pbst+")",70));			
			D_OUT.writeBytes(padSTRING('L',"Report Date : "+cl_dat.M_strLOGDT_pbst,30));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('R',"Leave Recommending Authority Report",70));
			D_OUT.writeBytes(padSTRING('L',"Page No : "+cl_dat.M_PAGENO,30));
			crtNWLIN();
					
			crtNWLIN();	
			D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------");
			crtNWLIN();	crtNWLIN();				
			D_OUT.writeBytes(padSTRING('R',"DEPARTMENT",15)+padSTRING('R',"EMP NO",10)+padSTRING('R',"EMP NAME",25)+padSTRING('R',"DESGN",15)+padSTRING('R',"RCM AUT ",10)+padSTRING('R'," SAN AUT",10)+padSTRING('R',"EMAIL ",25)) ;
			crtNWLIN();	
			D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------");
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
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOBOLD);
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
    //gets the data from txtDPTCD txtEMPNO in query form if no data in field then that column is not in query
	String getINPUT(int flgDPTCD,int flgEMPCD)
	{
		String L_strINPUT="";
		if((flgDPTCD==1)&&(flgEMPCD==1))
		{
			L_strINPUT=" and a.ep_dptcd='"+txtDPTCD.getText().trim()+"' and a.ep_empno='"+txtEMPCD.getText().trim()+"'";

		}
		else if((flgDPTCD==1)&&(flgEMPCD==0))
		{
			L_strINPUT=" and a.ep_dptcd='"+txtDPTCD.getText().trim()+"'";	

		}
		else if((flgDPTCD==0)&&(flgEMPCD==1))
		{
			L_strINPUT=" and a.ep_empno='"+txtEMPCD.getText().trim()+"'";				
		}
		else if((flgDPTCD==0)&&(flgEMPCD==0))
		{
			L_strINPUT="";
		}
		return L_strINPUT;
	}

	private void getDATA(int flgDPTCD,int flgEMPCD) 
	{
		try
		{ 
			String L_strORDBY="";
			String L_strINPUT="";  // field to select from text field
			if(rdbOVERWRT.isSelected())  // if clearall selected then start from begining
			{
				intRWCNT=0;  //intialized to 0 because other class might had changed it before
				tblVIEW.clrTABLE();
				tblVIEW.cmpEDITR[TB1_CHKFL].setEnabled(true); //to enable only the first column						

			}
	
			//if clear not selected and nothing in first row of table then start writing from first row
			if((rdbAPPEND.isSelected()==true) && (tblVIEW.getValueAt(0,TB1_DPTNM).toString().length()==0))  // if clear all selected then start from begining
			{
				intRWCNT=0;  //intialized to 0 because other class might had changed it before
				tblVIEW.clrTABLE();
				tblVIEW.cmpEDITR[TB1_CHKFL].setEnabled(true); //to enable only the first column						

			}
			
			//System.out.println("cmbORDBY.getSelectedIndex()"+cmbORDBY.getSelectedIndex());
			/* combo option
			 1 -->Dept Name
			 2-->Emp No
			 3-->Emp Name
			 4-->Recm Auth
			 5-->Sanc Auth
			 */
			L_strINPUT=getINPUT(flgDPTCD,flgEMPCD);
			
			//System.out.println("L_strINPUT : "+L_strINPUT);
			
			switch(cmbORDBY.getSelectedIndex())
			{
			    case 1: L_strORDBY=" ep_dptcd,ep_desgn,ep_empno";break;	
				case 2: L_strORDBY=" ep_empno";break;					
				case 3: L_strORDBY=" ep_empnm";break;					
				case 4: L_strORDBY=" ep_empcd_r,ep_desgn,ep_empno";break;
				case 5: L_strORDBY=" ep_empcd_s,ep_desgn,ep_empno";break;
				case 0: L_strORDBY=" ep_dptcd,ep_desgn,ep_empno";break;			
			}
			// L_strORDBY=(cmbORDBY.getSelectedItem().toString().trim().equalsIgnoreCase("Select Option")?"a.ep_dptcd,,a.ep_empno":cmbORDBY.getSelectedItem().toString().trim());

			//System.out.println("L_strORDBY"+L_strORDBY);		
     /*
			M_strSQLQRY= "select a.ep_dptcd ep_dptcd,a.ep_dptnm ep_dptnm,a.ep_empno ep_empno,a.ep_desgn ep_desgn,a.ep_fstnm||' '||SUBSTRING(a.ep_mdlnm,1,1)||'. '||a.ep_lstnm ep_empnm,SUBSTRING(cmt_codcd,6,4) ep_xxxcd, SUBSTRING(b.ep_fstnm,1,1)||'. '||SUBSTRING(b.ep_mdlnm,1,1)||'. '||b.ep_lstnm ep_recnm"; 
			M_strSQLQRY+= " from hr_epmst a left outer join co_cdtrn on cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HR"+cl_dat.M_strCMPCD_pbst+"LRC' and SUBSTRING(cmt_codcd,1,4) = a.ep_empno";
			M_strSQLQRY+= " left outer join hr_epmst b on SUBSTRING(cmt_codcd,6,4)=b.ep_empno where a.ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'"+L_strINPUT;
			M_strSQLQRY+= " and  a.ep_lftdt is null and a.ep_stsfl<>'X' order by "+L_strORDBY;
	*/
			
	/*   old query
		select a.ep_dptcd,a.ep_dptnm,a.ep_empno,a.ep_desgn,a.ep_fstnm||' '||SUBSTRING(a.ep_mdlnm,1,1)||'. '||a.ep_lstnm ep_empnm,SUBSTRING(cmt_codcd,6,4) ep_xxxcd, SUBSTRING(b.ep_fstnm,1,1)||'. '||SUBSTRING(b.ep_mdlnm,1,1)||'. '||b.ep_lstnm ep_xxxnm from spldata.hr_epmst a left outer join spldata.co_cdtrn on cmt_cgmtp='A01' and cmt_cgstp='HR01LRC' and SUBSTRING(cmt_codcd,1,4) = a.ep_empno left outer join spldata.hr_epmst b on  
 		SUBSTRING(cmt_codcd,6,4)=b.ep_empno where a.ep_cmpcd='01' and  a.ep_lftdt is null and a.ep_stsfl<>'X' order by a.ep_dptcd,a.ep_empno;
		
	*/
			
			M_strSQLQRY= "select a.ep_dptcd ep_dptcd,a.ep_dptnm ep_dptnm,a.ep_empno ep_empno,a.ep_desgn ep_desgn,a.ep_fstnm + ' ' + SUBSTRING(a.ep_mdlnm,1,1) + '. ' + a.ep_lstnm ep_empnm,SUBSTRING(b.cmt_codcd,6,4) ep_empcd_r,SUBSTRING(c.cmt_codcd,6,4) ep_empcd_s,ep_emlrf from hr_epmst a ";
			M_strSQLQRY+= " left outer join  co_cdtrn b on b.cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and b.cmt_cgstp='HR"+cl_dat.M_strCMPCD_pbst+"LRC' and SUBSTRING(b.cmt_codcd,1,4) = a.ep_empno  left outer join co_cdtrn c on c.cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and c.cmt_cgstp='HR"+cl_dat.M_strCMPCD_pbst+"LSN' and SUBSTRING(c.cmt_codcd,1,4) = a.ep_empno"; 
			M_strSQLQRY+= " where  a.ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'"+L_strINPUT +" and  a.ep_lftdt is null and a.ep_stsfl<>'X'   order by "+L_strORDBY;
		
			
	/*  new query
	 select a.ep_dptcd,a.ep_dptnm,a.ep_empno,a.ep_desgn,a.ep_fstnm||' '||SUBSTRING(a.ep_mdlnm,1,1)||'. '||a.ep_lstnm ep_empnm,
	  SUBSTRING(b.cmt_codcd,6,4) ep_empcd_r,SUBSTRING(c.cmt_codcd,6,4) ep_empcd_s from spldata.hr_epmst a 
	  left outer join spldata.co_cdtrn b  on b.cmt_cgmtp='A01' and b.cmt_cgstp='HR01LRC' and SUBSTRING(b.cmt_codcd,1,4) = a.ep_empno 
	  left outer join spldata.co_cdtrn c on c.cmt_cgmtp='A01' and c.cmt_cgstp='HR01LSN' and SUBSTRING(c.cmt_codcd,1,4) = a.ep_empno 
	  where a.ep_cmpcd='01' and  a.ep_lftdt is null and a.ep_stsfl<>'X'   order by a.ep_dptcd,a.ep_empno;

	 */
			System.out.println(M_strSQLQRY);

			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 			
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(chkSELALL.isSelected())
						tblVIEW.setValueAt(true,intRWCNT,TB1_CHKFL);

					tblVIEW.setValueAt(nvlSTRVL(M_rstRSSET.getString("ep_dptnm"),""),intRWCNT,TB1_DPTNM);	
					tblVIEW.setValueAt(nvlSTRVL(M_rstRSSET.getString("ep_empno"),""),intRWCNT,TB1_EMPNO);	
					tblVIEW.setValueAt(nvlSTRVL(M_rstRSSET.getString("ep_empnm"),""),intRWCNT,TB1_EMPNM);	
					tblVIEW.setValueAt(nvlSTRVL(M_rstRSSET.getString("ep_desgn"),""),intRWCNT,TB1_DESGN);
				    if(M_rstRSSET.getString("ep_empcd_r")!=null)
						if(hstRCSNNM.containsKey(M_rstRSSET.getString("ep_empcd_r")))
						tblVIEW.setValueAt(hstRCSNNM.get(M_rstRSSET.getString("ep_empcd_r")),intRWCNT,TB1_RCAUT);	
						else
						tblVIEW.setValueAt("",intRWCNT,TB1_RCAUT);	
				    else
					tblVIEW.setValueAt("",intRWCNT,TB1_RCAUT);	
				    
				    if(M_rstRSSET.getString("ep_empcd_s")!=null)						
						if(hstRCSNNM.containsKey(M_rstRSSET.getString("ep_empcd_s")))						
						tblVIEW.setValueAt(nvlSTRVL(hstRCSNNM.get(M_rstRSSET.getString("ep_empcd_s")),""),intRWCNT,TB1_SNAUT);						
						else
						tblVIEW.setValueAt("",intRWCNT,TB1_SNAUT);	
				    else
					tblVIEW.setValueAt("",intRWCNT,TB1_SNAUT);	
					
					//tblVIEW.setValueAt(nvlSTRVL(M_rstRSSET.getString("ep_empcd_s"),""),intRWCNT,TB1_SNAUT);						
				    tblVIEW.setValueAt(nvlSTRVL(M_rstRSSET.getString("ep_emlrf"),""),intRWCNT,TB1_EMAIL);
				    
					intRWCNT++;
				}
				
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			
			setMSG(L_EX,"getDATA()"); 
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
	

	void exePRINT()
	{
		try
		{
			int flgHTML=0;  //to know whether html option selected
			if(M_rdbHTML.isSelected())flgHTML=1;
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;

			if(M_rdbHTML.isSelected())
				strRPFNM = strRPLOC + "hr_rplrs.html";
			if(M_rdbTEXT.isSelected())
				strRPFNM = strRPLOC + "hr_rplrs.doc";

			genRPTFL();

			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
					doPRINT(strRPFNM);
				
					/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
					 */	
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



