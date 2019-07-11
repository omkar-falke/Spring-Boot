import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

class hr_rpsoc extends cl_rbase 
{
	private JTextField txtDPTCD,txtDPTNM;			
	private JTextField txtEMPNO;
	private JTextField txtSTRDT;			
	private JTextField txtENDDT;
	private JLabel lblSTRDT;			
	private JLabel lblENDDT;
	private JLabel lblDPTNM;
	private JLabel lblEMPNM;
	private JLabel lblTMLIM;
	private JCheckBox chkOTSDT;
	private JRadioButton rdbWRKDT;
	private JRadioButton rdbOTSDT;
	private ButtonGroup btgDATE;
	private JComboBox cmbRPTYP;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpsoc.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private String[] staYEAR = {"","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	
	private boolean flgTBLHD;
	hr_rpsoc()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,20);			
			
			add(new JLabel("Report Type"),1,7,1,2,this,'L');
			add(cmbRPTYP = new JComboBox(),1,10,1,6,this,'L');	
			
			add(new JLabel("Department"),2,7,1,2,this,'L');
			//add(txtDPTNM=new TxtLimit(20),3,10,1,2,this,'L');  
			add(txtDPTCD= new TxtLimit(3),2,10,1,2,this,'L');
			add(lblDPTNM=new JLabel(),2,13,1,8,this,'L');    
			
			add(new JLabel("Employee"),3,7,1,2,this,'L');
			add(txtEMPNO= new TxtLimit(4),3,10,1,2,this,'L');
			add(lblEMPNM=new JLabel(),3,13,1,8,this,'L');  
			
			add(lblSTRDT = new JLabel("Form Date"),4,7,1,2,this,'L');
			add(txtSTRDT= new TxtDate(),4,10,1,2,this,'L');
			
			add(lblENDDT = new JLabel("To Date"),5,7,1,2,this,'L');
			add(txtENDDT = new TxtDate(),5,10,1,2,this,'L');
			
			cmbRPTYP.addItem("Shift Change Details");
			cmbRPTYP.addItem("Off Change Details");
		
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtDPTCD.setInputVerifier(oINPVF);
			txtEMPNO.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);	
			M_rdbHTML.setSelected(true);
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
			  if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			  {
				  txtDPTCD.requestFocus();
				  if(txtDPTCD.getText().length()==0)
				    setMSG ("Please enter Department code..",'N');	
					
			  }
			}
			
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");
			if(txtEMPNO.getText().length()==0)
				lblEMPNM.setText("");
				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        	{						
										//help for Department Code
        		if(M_objSOURC==txtDPTCD)		
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
					M_strSQLQRY=" Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
					M_strSQLQRY+=" and CMT_STSFL <> 'X'";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
        								//help for Employee Category
				if(M_objSOURC==txtEMPNO)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
        			String L_ARRHDR[] = {"Code","Category"};
        			M_strSQLQRY = "select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U'";
        			if(txtDPTCD.getText().length()>0)
						M_strSQLQRY += " and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
					M_strSQLQRY += " order by EP_EMPNO";
					//System.out.println(">>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
        	{						
        		if(M_objSOURC==txtDPTCD)		
					txtEMPNO.requestFocus();
        		if(M_objSOURC==txtEMPNO)		
					txtSTRDT.requestFocus();
        		if(M_objSOURC==txtSTRDT)		
					txtENDDT.requestFocus();
				if(M_objSOURC==txtENDDT)		
					cl_dat.M_btnSAVE_pbst.requestFocus();
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
				lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL()
	{
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			flgTBLHD=false;
			genRPHDR();
			setMSG("Printing Report..",'N');
			if(cmbRPTYP.getSelectedItem().toString().equals("Shift Change Details"))
			{
				M_strSQLQRY  =" select sw_wrkdt,ep_lstnm + ' ' + SUBSTRING(ep_fstnm,1,1) + '.' + SUBSTRING(ep_mdlnm,1,1) + '.' ep_empnm,sw_cursh,sw_wrksh ";
				M_strSQLQRY+= " from hr_swmst,hr_epmst";
				M_strSQLQRY+= " where sw_cmpcd=ep_cmpcd and sw_empno = ep_empno and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
				M_strSQLQRY +=" and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
				M_strSQLQRY+= " and sw_cursh <> sw_wrksh ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and sw_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" order by sw_wrkdt,ep_lstnm + ' ' + SUBSTRING(ep_fstnm,1,1) + '.' + SUBSTRING(ep_mdlnm,1,1) + '.'";
				
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						D_OUT.writeBytes("<tr>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_fmtLCDAT.format(M_rstRSSET.getDate("sw_wrkdt"))+"</td>");
						D_OUT.writeBytes("<td align ='left' width='5%'>"+M_rstRSSET.getString("ep_empnm")+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("sw_wrksh")+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("sw_cursh")+"</td>");
						D_OUT.writeBytes("</tr>");
						crtNWLIN();
					}
					
				}
			}
			else if(cmbRPTYP.getSelectedItem().toString().equals("Off Change Details"))
			{
				M_strSQLQRY  =" select sw_wrkdt,ep_lstnm + ' ' + SUBSTRING(ep_fstnm,1,1) + '.' + SUBSTRING(ep_mdlnm,1,1) + '.' ep_empnm,co_wrkdt,co_refdt";
				M_strSQLQRY+= " from hr_swmst,hr_cotrn,hr_epmst";
				M_strSQLQRY+= " where sw_cmpcd=ep_cmpcd and sw_empno = ep_empno and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
				M_strSQLQRY +=" and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
				M_strSQLQRY +=" and  sw_lvecd='WO' and SW_INCTM is not null and SW_CMPCD = CO_CMPCD  and sw_empno = co_empno and sw_wrkdt = co_wrkdt ";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and sw_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" order by sw_wrkdt,ep_lstnm + ' ' + SUBSTRING(ep_fstnm,1,1) + '.' + SUBSTRING(ep_mdlnm,1,1) + '.'";
				
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						
						D_OUT.writeBytes("<tr>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_fmtLCDAT.format(M_rstRSSET.getDate("sw_wrkdt"))+"</td>");
						D_OUT.writeBytes("<td align ='left' width='5%'>"+M_rstRSSET.getString("ep_empnm")+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_fmtLCDAT.format(M_rstRSSET.getDate("co_wrkdt"))+"</td>");
						if(M_rstRSSET.getDate("co_refdt")!=null)
							D_OUT.writeBytes("<td align ='center' width='2%'>"+M_fmtLCDAT.format(M_rstRSSET.getDate("co_refdt"))+"</td>");
						else
							D_OUT.writeBytes("<td align ='center' width='2%'>&nbsp;</td>");
							
						D_OUT.writeBytes("</tr>");
						crtNWLIN();
						
					}
				}
			}
		
			if(M_rstRSSET != null)
				M_rstRSSET.close();
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
				if(cl_dat.M_intLINNO_pbst >40)
				{
					genRPFTR();
					genRPHDR();
				}
			}		
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

	/**Method to creat HTML Table*/
	private void crtTBL(DataOutputStream L_DOUT,int P_intBORDR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
				L_DOUT.writeBytes("<p><TABLE border="+P_intBORDR+" borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray  cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
			
		}
	}
	
	void genRPHDR()
	{
		try
		{
			crtTBL(D_OUT,1);
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size :9 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	
			
			cl_dat.M_PAGENO +=1;
			crtTBL(D_OUT,0);
			D_OUT.writeBytes("<br>");
			
			D_OUT.writeBytes("<tr><td align='left' width='25%' rowspan='2'><IMG src=\\\\192.168.10.207\\user\\exec\\splerp3\\spllogo_old.gif style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></td><td align=center WIDTH='50%'><STRONG><FONT face=Arial size=5>"+cl_dat.M_strCMPNM_pbst+"</FONT></STRONG></td><td WIDTH='25%'>&nbsp;</td></tr>" +
							"<tr><td>&nbsp;</td><td align=right><FONT face=Arial size=2>DATE:"+cl_dat.M_strLOGDT_pbst+"</FONT></td></tr>"+
							"<tr><td align=left><STRONG><FONT face=Arial size=2>DEPARTMENT:"+lblDPTNM.getText()+"</FONT></STRONG></td><td>&nbsp;</td><td align=right><STRONG><FONT face=Arial size=2>TO:<strong><strong>HRD DIVISION</strong></strong></STRONG></FONT></td></tr>"+
							"<tr><td align=left><STRONG><FONT face=Arial size=2>Period From: "+txtSTRDT.getText()+ "</FONT></STRONG></td>");
			if(cmbRPTYP.getSelectedItem().toString().equals("Shift Change Details"))	
				D_OUT.writeBytes("<td align=center><STRONG><FONT face=Arial size=2>Shift Change Details</FONT></STRONG></td><td>&nbsp;</td></tr>");
			else if(cmbRPTYP.getSelectedItem().toString().equals("Off Change Details"))	
				D_OUT.writeBytes("<td align=center><STRONG><FONT face=Arial size=2>Off Change Details</FONT></STRONG></td><td>&nbsp;</td></tr>") ;
						
							//"<tr><td align=left><STRONG><FONT face=Arial size=2>Period From: "+txtSTRDT.getText()+ "</FONT></STRONG></td><td align=center><STRONG><FONT face=Arial size=2>Shift change/Off change details</FONT></STRONG></td><td>&nbsp;</td></tr>"+
				D_OUT.writeBytes("<tr><td align=left><STRONG><FONT face=Arial size=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To: "+txtENDDT.getText()+"</FONT></STRONG></td><td>&nbsp;</td><td>&nbsp;</td></tr>" +		
							
							" </HEAD> <BODY><P><PRE style =\" font-size :10 pt \">");    
			D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			
			crtTBL(D_OUT,1);
			D_OUT.writeBytes("<tr>");
		    D_OUT.writeBytes("<td align ='center' width='2%' ><b>Date</b></td>");
		    D_OUT.writeBytes("<td align ='center' width='5%' ><b>Emp.Name</b></td>");
			if(cmbRPTYP.getSelectedItem().toString().equals("Shift Change Details"))	
			{
				D_OUT.writeBytes("<td align ='center' width='2%' ><b>Original Shift</b></td>");
				D_OUT.writeBytes("<td align ='center' width='2%' ><b>New Shift</b></td>");
			    D_OUT.writeBytes("</tr>");
			}
			else if(cmbRPTYP.getSelectedItem().toString().equals("Off Change Details"))	
			{
				D_OUT.writeBytes("<td align ='center' width='2%' ><b>Original Off</b></td>");
				D_OUT.writeBytes("<td align ='center' width='2%' ><b>New Off</b></td>");
				D_OUT.writeBytes("</tr>");
			}
			
			
				
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes("</p></TABLE>");
  		    
			D_OUT.writeBytes(padSTRING('R',"H.O.S.("+lblDPTNM.getText()+")",65));
				
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
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
			setMSG("",'N');
			
			if(txtSTRDT.getText().length()==0)
			{
				txtSTRDT.requestFocus();
				setMSG("Please Enter From Date",'E');
				return false;
			}	
			if(txtENDDT.getText().length()==0)
			{
				txtENDDT.requestFocus();
				setMSG("Please Enter To Date",'E');
				return false;
			}		
			
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
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rpsoc.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpsoc.doc";
				
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
			if(((JTextField)input).getText().length() == 0)
				return true;
				
			if(input == txtDPTCD)
			{
				M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
				M_strSQLQRY+=" and CMT_STSFL <> 'X' and CMT_CODCD='"+txtDPTCD.getText().trim()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Department Code",'E');
					return false;
				}			
			}	
			if(input == txtEMPNO)
			{
				M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText().trim()+"' and EP_STSFL <> 'U' ";
				
				if(txtDPTCD.getText().length()>0)
					M_strSQLQRY +=" and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
				//System.out.println("<<<<"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Employee Code",'E');
					txtEMPNO.requestFocus();
					return false;
				}			
			}	
			if(input == txtSTRDT)
			{
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
				{
					setMSG("Date can not be greater than todays date..",'E');
					txtSTRDT.requestFocus();
					return false;
				}
			}
			if(input == txtENDDT)
			{
				if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
				{
					setMSG("Date can not be greater than todays date..",'E');
					txtENDDT.requestFocus();
					return false;
				}
				else if(txtSTRDT.getText().length()>0)
				{
					if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText())) > 0)
					{
						setMSG("From Date can not be greater than To date..",'E');
						txtSTRDT.requestFocus();
						return false;
					}
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



