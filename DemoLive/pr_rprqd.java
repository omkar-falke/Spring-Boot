import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;

class pr_rprqd extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"pr_rprqd.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    private JTextField txtDOCNO;
    private String strDOTLN = "-----------------------------------------------------------------------------------------------------------";
    private JTextField txtFMDAT;
    private JTextField txtTODAT;
    private String strFMDAT,strTODAT,strCRDAT;
    
	pr_rprqd()		/*  Constructor   */
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
			
			JPanel pnlPRNO = new JPanel();
			setMatrix(20,8);
			pnlPRNO.setLayout(null);
			add(new JLabel("From Date"),1,2,1,1,pnlPRNO,'L');   
		    add(txtFMDAT = new TxtDate(),1,3,1,1,pnlPRNO,'L');  
		    add(new JLabel("To Date"),1,4,1,1,pnlPRNO,'L');   
		    add(txtTODAT = new TxtDate(),1,5,1,1,pnlPRNO,'L');  
		    pnlPRNO.setBorder(BorderFactory.createTitledBorder(null,"PRODUCTION REQUEST",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlPRNO,6,2,2,6,this,'L');
		    
			txtFMDAT.addKeyListener(this);
			txtTODAT.addKeyListener(this);
			txtFMDAT.setInputVerifier(oINPVF);
			txtTODAT.setInputVerifier(oINPVF);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
			txtFMDAT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2));
		    
		    M_pnlRPFMT.setVisible(true);
			setENBL(true);
			
			
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
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
					  setMSG ("Please enter Date to generate Report..",'N');	
					}
			  }
			}
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
			if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtFMDAT)
				   txtTODAT.requestFocus();
				else if(M_objSOURC == txtTODAT)
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

		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL()
	{
		if(!vldDATA())
			return;
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			 int L_ROWNO1=0;
			 String strTOTQT = "0";
			 String strTOTRQT = "0";
			 
			 double L_dblTOTQT = 0;
			 String strTOAMT;
			
			genRPHDR();	
			
			M_strSQLQRY = "select rq_docno,rq_docdt, pt_prtnm,rq_reqby,pr_prdds,rq_reqqt,rq_reqdt,rq_prdtp,rq_stkqt,rq_uclqt,rq_resqt,rq_qlhqt,rq_tarqt from pr_rqtrn,co_ptmst,co_prmst,pr_rmmst where rq_cmpcd = rm_cmpcd and pt_prtcd = rq_prtcd and rq_prdtp = rm_prdtp and rq_docno = rm_docno and ltrim(str(rq_prdcd,20,0))=pr_prdcd and rm_trntp='RQ' and rq_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and pt_prttp ='C'";
			M_strSQLQRY +=" AND CONVERT(varchar,rq_docdt,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
    		M_strSQLQRY +=" order by rq_docno,rq_docdt";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(">>>M_strSQLQRY>>"+  M_strSQLQRY);
			  
			 if(M_rstRSSET != null)
			 {	  
				while(M_rstRSSET.next())
				{	
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("rq_docno"),""),10));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("rq_docdt")),""),13));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("rq_reqby"),""),8));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("pr_prdds"),""),20));
					D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("rq_reqqt"),""),12));
					D_OUT.writeBytes(padSTRING('R',"",5));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("pt_prtnm"),""),28));
					D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("rq_reqdt")),""),10));
					strTOAMT = setNumberFormat(M_rstRSSET.getDouble("rq_reqqt"),3).toString();
					L_dblTOTQT += Double.parseDouble(strTOAMT);
					crtNWLIN();
					L_ROWNO1++;
				}
				
			 }
			 genRPFTR();
			 strTOTQT = setNumberFormat(L_dblTOTQT,3);
			 strTOTRQT = String.valueOf(L_ROWNO1++);
			 D_OUT.writeBytes("\n" + padSTRING('L',"Total Request : ",15) + padSTRING('R',strTOTRQT,10));
			 D_OUT.writeBytes(padSTRING('L',"Total Quantity : ",17) + padSTRING('L',strTOTQT,12));
			 D_OUT.writeBytes("\n");
			 D_OUT.writeBytes(strDOTLN);
			 
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
				if(cl_dat.M_intLINNO_pbst >70)
				{
				    D_OUT.writeBytes("<P CLASS = \"breakhere\">");
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

			cl_dat.M_PAGENO +=1;
			
			 String strFMDAT = txtFMDAT.getText();
			 String strTODAT = txtTODAT.getText();
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-17));
    		D_OUT.writeBytes("Date: "+cl_dat.M_strLOGDT_pbst+"\n");
    		D_OUT.writeBytes(padSTRING('R',"Production Request Register Period From:"+strFMDAT+" To:"+strTODAT,strDOTLN.length()-17));
		    D_OUT.writeBytes("Page No: "+cl_dat.M_PAGENO+"\n");
		    crtNWLIN();
		    prnFMTCHR(D_OUT,M_strNOBOLD);
		    D_OUT.writeBytes(strDOTLN+"\n");
		    D_OUT.writeBytes("PR        PR           Request Grade                    Request     Customer                    Despatch \n");                     
		    D_OUT.writeBytes("NO.       Date         By                              Quantity                                 Date      \n");                       
		    D_OUT.writeBytes(strDOTLN);  
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
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			//if(M_rdbHTML.isSelected())
				//D_OUT.writeBytes("<P CLASS = \"breakhere\">");
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
			strFMDAT = txtFMDAT.getText().trim();
			strTODAT = txtTODAT.getText().trim();
			strCRDAT = cl_dat.M_strLOGDT_pbst;
			if(txtFMDAT.getText().trim().length()== 0)
			{
			  setMSG("Enter From Date..",'E');
			  txtFMDAT.requestFocus();
			  return false;
			}
			
			else if(txtTODAT.getText().trim().length()== 0)
			{
			  setMSG("Enter To Date..",'E');
			  txtTODAT.requestFocus();
			  return false;
			 
			}
			else if(M_fmtLCDAT.parse(strFMDAT).compareTo(M_fmtLCDAT.parse(strCRDAT))> 0)
			{
				
				setMSG("From Date can not be greater than Current date time..",'E');
				return false;
				
			}
			else if(M_fmtLCDAT.parse(strTODAT).compareTo(M_fmtLCDAT.parse(strCRDAT))> 0)
			{
				setMSG("To Date can not be greater than Current date time..",'E');
				return false;
				
			}
			else if(M_fmtLCDAT.parse(strTODAT).compareTo(M_fmtLCDAT.parse(strFMDAT))< 0)
			{
				setMSG("To Date can not be smaller than From date..",'E');
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
			     strRPFNM = strRPLOC + "pr_rprqd.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "pr_rprqd.doc";
			
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
			/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}*/
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



