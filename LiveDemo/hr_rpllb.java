import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;

class hr_rpllb extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"new_rep.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	hr_rpllb()		/*  Constructor   */
	{
		super(2);
		try
		{
			//System.out.println("hiiiiiiiiii");
			setMatrix(20,20);			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
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
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			
			D_OUT.writeBytes(" ________________________________________________________________________________________________________________________________________");crtNWLIN();
			D_OUT.writeBytes("|        |        |            |          |               |         |         |         | P.F. Amount| Total Amt|                        |");	crtNWLIN();
			D_OUT.writeBytes("|Present | Holiday|Casual Leave|Sick Leave|Privilege Leave|  Total  |  L.W.P  |  Total  |  of Cont.  |  of Cont.|      REMARKS           |");crtNWLIN();			
			D_OUT.writeBytes("|________|________|____________|__________|_______________|_________|_________|_________|____________|__________|________________________|");crtNWLIN();
			D_OUT.writeBytes("|Days|hrs|Days|hrs| Days |hrs  |Days |hrs | Days  | hrs   |Days |hrs|Days |hrs|Days |hrs|Days |hrs   |Days |hrs |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                                             
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			D_OUT.writeBytes("|    |   |    |   |      |     |     |    |       |       |     |   |     |   |     |   |     |      |     |    |                        |");crtNWLIN();
			D_OUT.writeBytes("|____|___|____|___|______|_____|_____|____|_______|_______|_____|___|_____|___|_____|___|_____|______|_____|____|________________________|");crtNWLIN();                                                                                    
			
			
			
			
			
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
				D_OUT.writeBytes("<HTML><HEAD><Title>Attendance Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',","+ cl_dat.M_strCMPLC_pbst,10));
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
			     strRPFNM = strRPLOC + "new_rep.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "new_rep.doc";
				
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



