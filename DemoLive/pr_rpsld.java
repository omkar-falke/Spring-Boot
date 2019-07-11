import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.Color;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.sql.ResultSet;import java.text.SimpleDateFormat;
import java.util.Calendar;import javax.swing.border.*;import javax.swing.JPanel;
import java.sql.*;

class pr_rpsld extends cl_rbase 
{

	private JTextField txtPRDTP,txtPRDDS;			
	private JTextField txtSTRDT,txtSTRTM;			
	private JTextField txtENDDT,txtENDTM;
	private JLabel lblTIME;
	
	//private SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("dd/MM/yyyy"); 	
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"pr_rpsld.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    private JPanel pnlMAIN;   
    
    private JCheckBox chkEXCEL;
	pr_rpsld()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,20);			
			pnlMAIN = new JPanel(null);
			
			add(new JLabel("Product Type"),1,1,1,2,pnlMAIN,'L');
			add(txtPRDTP= new TxtLimit(2),1,3,1,2,pnlMAIN,'L');
			add(txtPRDDS= new TxtLimit(20),1,5,1,3,pnlMAIN,'L');
			
			add(new JLabel("From Date"),2,1,1,2,pnlMAIN,'L');
			add(txtSTRDT = new TxtDate(),2,3,1,2,pnlMAIN,'L');
			add(txtSTRTM = new TxtTime(),2,5,1,1,pnlMAIN,'L');
						
			add(new JLabel("To Date"),3,1,1,2,pnlMAIN,'L');
			add(txtENDDT = new TxtDate(),3,3,1,2,pnlMAIN,'L');
			add(txtENDTM = new TxtTime(),3,5,1,1,pnlMAIN,'L');
			
			add(chkEXCEL=new JCheckBox("Excel"),3,14,1,2,this,'L'); 
			//add(chkEXCEL=new JCheckBox("Excel"),3,1,1,2,M_pnlRPFMT,'L'); 
			pnlMAIN.setBorder(new EtchedBorder(Color.black,Color.lightGray));
			add(pnlMAIN,4,7,5,9,this,'L');
			
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtPRDTP.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			
			M_pnlRPFMT.setVisible(true);	
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
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					txtPRDTP.requestFocus();
					txtPRDDS.setEnabled(false);
					if((txtSTRDT.getText().trim().length()==0) ||(txtENDDT.getText().trim().length()==0))
					{
						txtSTRDT.setText("01/"+cl_dat.M_strLOGDT_pbst.substring(3));
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(Calendar.DATE,-1);
						txtENDDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						//txtSTRDT.setText("01/"+txtENDDT.getText().substring(3));
						txtSTRTM.setText("06:00");
						txtENDTM.setText("06:00");
					}
				}
				else
					setENBL(false);
			}
			
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
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        	{						
				if(M_objSOURC == txtPRDTP)
				{
				   cl_dat.M_flgHELPFL_pbst = true;
		    	   M_strHLPFLD = "txtPRDTP";
		    	   String L_ARRHDR[] = {"Product Type","Description"};
		    	   M_strSQLQRY = " select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
		    	   M_strSQLQRY+= " where CMT_CGMTP='MST' AND CMT_CGSTP='COXXPRD' AND isnull(CMT_STSFL,'')<>'X'";
		    	   if(txtPRDTP.getText().length()>0)				
		    		   M_strSQLQRY+= " AND cmt_codcd like '"+txtPRDTP.getText().trim()+"%'";
				   M_strSQLQRY += " order by cmt_codcd";
				   System.out.println("txtPRDTP"+M_strSQLQRY);
		    	   cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");  
					      
				}
			}	
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
        	{
				if(M_objSOURC == txtPRDTP)
				{
					if(txtPRDTP.getText().length()==0)
						txtPRDDS.setText("");
				
					txtSTRDT.requestFocus();
					setMSG("Enter the From Date..",'N');	
				}
				else if(M_objSOURC == txtSTRDT)
				{
					if(txtSTRDT.getText().length()==0)
						txtSTRTM.setText("");
					txtSTRTM.requestFocus();
					setMSG("Enter the From Date Time..",'N');	
					
				}
				else if(M_objSOURC == txtSTRTM)
				{
					txtENDDT.requestFocus();
					setMSG("Enter the To Date ..",'N');	
				}
				else if(M_objSOURC == txtENDDT)
				{
					if(txtENDDT.getText().length()==0)
						txtENDTM.setText("");
					txtENDTM.requestFocus();
					setMSG("Enter the To Date Time..",'N');	
				}
				else if(M_objSOURC == txtENDTM)
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
			cl_dat.M_flgHELPFL_pbst = false;		
			if(M_strHLPFLD == "txtPRDTP")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtPRDTP.setText(L_STRTKN.nextToken());
				txtPRDDS.setText(L_STRTKN.nextToken());
			}
		}
		catch(Exception L_EX)
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
			float fltTOQTY=0;
			java.sql.Timestamp L_tmsPENTM=null;
			String strNEW_PENDT="",strOLD_PENDT="";
			genRPHDR();
			
			M_strSQLQRY =" select lt_prdtp,lt_linno,lt_pendt,lt_cprcd,pr_prdds,lt_lotno,max(lt_rclno),isnull(sum(rct_rctqt),0) rct_rctqt from pr_ltmst left outer join fg_rctrn  on lt_cmpcd=rct_cmpcd and lt_prdtp=rct_prdtp and lt_lotno=rct_lotno and lt_rclno = rct_rclno and rct_stsfl='2' and rct_rcttp in ('10','15','51') left outer join co_prmst on ltrim(str(lt_cprcd,20,0))=pr_prdcd ";
			M_strSQLQRY +=" where lt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and lt_prdtp='"+txtPRDTP.getText()+"'  and lt_pendt is not null";
			M_strSQLQRY +=" and lt_pendt between '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSTRDT.getText().trim()+" "+txtSTRTM.getText().trim()))+"' and '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtENDDT.getText().trim()+" "+txtENDTM.getText().trim()))+"'";
			M_strSQLQRY +=" and lt_lotno + lt_rclno not in (select rct_lotno + rct_rclno from fg_rctrn where  rct_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and rct_prdtp='"+txtPRDTP.getText()+"'  and  rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().trim()))+"' and rct_rcttp <> '51' and rct_lotno in (select rct_lotno from fg_rctrn where  rct_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and rct_prdtp='"+txtPRDTP.getText()+"'  and rct_rctdt is not null and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().trim()))+"' and rct_rcttp = '51'))";
			M_strSQLQRY +=" group by lt_prdtp,lt_linno,lt_pendt,lt_cprcd,pr_prdds,lt_lotno order by lt_linno,lt_pendt";
			System.out.println(M_strSQLQRY);
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_tmsPENTM=M_rstRSSET.getTimestamp("lt_pendt");
					strNEW_PENDT=M_fmtLCDTM.format(L_tmsPENTM).substring(0,10);
					if(!chkEXCEL.isSelected())
					{
						if(!strOLD_PENDT.equals(strNEW_PENDT) && !strOLD_PENDT.equals(""))
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('L',"   "+setNumberFormat(fltTOQTY,3),87));
							fltTOQTY=0;
						}
						crtNWLIN();
				
					}
				    if(!strOLD_PENDT.equals(strNEW_PENDT))
					{
						if(chkEXCEL.isSelected())
						{
							D_OUT.writeBytes("\n");
							D_OUT.writeBytes(M_fmtLCDTM.format(L_tmsPENTM).substring(0,10)+"\t");
						}
						else
						{
							D_OUT.writeBytes(padSTRING('R',M_fmtLCDTM.format(L_tmsPENTM).substring(0,10),14));
						}
					}
					else
					{
						if(chkEXCEL.isSelected())
							D_OUT.writeBytes(""+"\t");	
						else
							D_OUT.writeBytes(padSTRING('R',"",14));		
					}
								
					if(chkEXCEL.isSelected())
					{
						D_OUT.writeBytes(M_fmtLCDTM.format(L_tmsPENTM).substring(11,16)+"\t");
						D_OUT.writeBytes(nvlSTRVL(M_rstRSSET.getString("lt_lotno"),"-")+"\t");
						D_OUT.writeBytes(nvlSTRVL(M_rstRSSET.getString("pr_prdds"),"-")+"\t");
						D_OUT.writeBytes(nvlSTRVL(M_rstRSSET.getString("rct_rctqt"),"-")+"\n");
					}
					else
					{
						D_OUT.writeBytes(padSTRING('R',M_fmtLCDTM.format(L_tmsPENTM).substring(11,16),15));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("lt_lotno"),""),10));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("pr_prdds"),""),18));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("rct_rctqt"),""),15));
						fltTOQTY+=M_rstRSSET.getFloat("rct_rctqt");
					}
					
					strOLD_PENDT=M_fmtLCDTM.format(L_tmsPENTM).substring(0,10);
					//strOLD_PENDT=M_fmtLCDAT.format(M_rstRSSET.getTimestamp("lt_pendt"));
				}
					crtNWLIN();
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
			if(chkEXCEL.isSelected())
				return;
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
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
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
				D_OUT.writeBytes("<HTML><HEAD></HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");   
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
				
    		if(chkEXCEL.isSelected())
    		{
	    		/*D_OUT.writeBytes(cl_dat.M_strCMPNM_pbst+"\t");
	    		D_OUT.writeBytes("Report Date: "+"\t");
			    D_OUT.writeBytes(cl_dat.M_strLOGDT_pbst+"\t");
	    		crtNWLIN();
	    		
	    		prnFMTCHR(D_OUT,M_strNOBOLD);
	    		D_OUT.writeBytes("Sequential Lot Detail  "+"\t");
	    		D_OUT.writeBytes("Page No    : "+"\t");
				D_OUT.writeBytes(cl_dat.M_PAGENO+"\t");
				crtNWLIN();
				D_OUT.writeBytes("Product Type : "+txtPRDDS.getText()+"  Period : From  "+txtSTRDT.getText()+" "+txtSTRTM.getText()+" To  "+txtENDDT.getText()+" "+txtENDTM.getText()+"\t");
				*/
    			D_OUT.writeBytes("Date"+"\t");
    			D_OUT.writeBytes("Time"+"\t");
    			D_OUT.writeBytes("Lot No"+"\t");
    			D_OUT.writeBytes("Grade"+"\t");
    			D_OUT.writeBytes("Quantity"+"\n");
				
    		}
			else
			{
				crtNWLIN();
	    		prnFMTCHR(D_OUT,M_strBOLD);
				D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,37));
	    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
			    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
	    		crtNWLIN();
	    		
	    		prnFMTCHR(D_OUT,M_strNOBOLD);
	    		D_OUT.writeBytes(padSTRING('R',"Sequential Lot Detail  ",37));
	    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
				D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Product Type : "+txtPRDDS.getText()+"  Period : From  "+txtSTRDT.getText()+" "+txtSTRTM.getText()+" To  "+txtENDDT.getText()+" "+txtENDTM.getText(),100));
				crtNWLIN();
				D_OUT.writeBytes("---------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes("Date          Time           Lot No.                Grade       Quantity          Total");
				crtNWLIN();
				D_OUT.writeBytes("---------------------------------------------------------------------------------------");
				crtNWLIN();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
			setCursor(cl_dat.M_curDFSTS_pbst);
			
		}
	}

	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			if(chkEXCEL.isSelected())
				D_OUT.writeBytes(""+"\t");
			else
			{
				D_OUT.writeBytes(padSTRING('L',"",65));
				crtNWLIN();
				D_OUT.writeBytes("---------------------------------------------------------------------------------------");
				crtNWLIN();
			}
			prnFMTCHR(D_OUT,M_strNOCPI17);
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

	boolean vldDATA()
	{
		try
		{
			if(txtPRDTP.getText().length()==0)
			{
				txtPRDTP.requestFocus();
				setMSG("Please Enter Product Type",'E');
				return false;
			}	
			if(txtSTRTM.getText().length()==0)
			{
				txtSTRTM.requestFocus();
				setMSG("Please Enter From Date Time",'E');
				return false;
			}	
			if(txtENDTM.getText().length()==0)
			{
				txtENDTM.requestFocus();
				setMSG("Please Enter To Date Time",'E');
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
			     strRPFNM = strRPLOC + "pr_rpsld.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "pr_rpsld.doc";
			if(chkEXCEL.isSelected())
			     strRPFNM = strRPLOC + "pr_rpsld.xls";
			
			
	    		genRPTFL();
				
			if(chkEXCEL.isSelected())
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
				//p  = r.exec("c:\\windows\\excel.exe "+strRPFNM);
				p  = r.exec("D:\\Program Files (x86)\\Microsoft Office\\Office12\\excel.exe "+strRPFNM);
				

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
						//if(chkEXCEL.isSelected())
						//	p  = r.exec("c:\\windows\\excel.exe "+strRPFNM); 
						//else
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
		//if(((JTextField)input).getText().length() == 0)
			//	return true;
		   
			if(input == txtPRDTP)
			{
				if(txtPRDTP.getText().length()==0)
				{
					txtPRDTP.requestFocus();
					setMSG("Please Enter Product Type",'E');
					return false;
				}	
				
				M_strSQLQRY = " select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
		        M_strSQLQRY+= " where CMT_CGMTP='MST' AND CMT_CGSTP='COXXPRD' AND isnull(CMT_STSFL,'')<>'X'";
		    	M_strSQLQRY+= " AND cmt_codcd = '"+txtPRDTP.getText().trim()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null && M_rstRSSET.next())
				{
					txtPRDTP.setText(txtPRDTP.getText());
					txtPRDDS.setText(M_rstRSSET.getString("CMT_CODDS"));
				}	
				else 
				{
					setMSG("Enter Valid Product Type..",'E');
					return false;
				}	
			}
			if(input == txtSTRDT)
			{
				if(txtSTRDT.getText().length()==0)
				{
					txtSTRDT.requestFocus();
					setMSG("Please Enter From Date",'E');
					return false;
				}	
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From date can not be greater than Today's date..",'E');
					return false;
				}
			}
			if(input == txtENDDT)
			{
				if(txtENDDT.getText().length()==0)
				{
					txtENDDT.requestFocus();
					setMSG("Please Enter To Date",'E');
					return false;
				}	
				if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("To date can not be greater than Today's date..",'E');
					return false;
				}
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
				{
					setMSG("From Date can not be greater than To Date..",'E');
					return false;
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



