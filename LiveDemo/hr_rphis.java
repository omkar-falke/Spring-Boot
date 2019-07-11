import java.awt.event.ActionEvent;import java.awt.event.MouseEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;import java.sql.CallableStatement;

class hr_rphis extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rphis.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private JTextField txtSTRDT;			
	private JTextField txtENDDT;
	private JRadioButton rdbGRDHS;
	private JRadioButton rdbEMPHS;
	private JCheckBox chkSELAL;
	private JCheckBox chkPROCS;
	private ButtonGroup btgHSTRY;/**  */
	private  cl_JTable tblGRADE;         /** Panel for Grade Details (TB2) */
	private int TB_CHKFL = 0;
	private int TB_GRDCD = 1;
	CallableStatement updHR_EHTRN;
	CallableStatement updHR_GRHST;
	int intROW = 0;
	
	hr_rphis()		/*  Constructor   */
	{
		super(2);
		try
		{
			//System.out.println("hiiiiiiiiii");
			setMatrix(20,20);	
			
			add(new JLabel("Form Date"),2,3,1,2,this,'L');
			add(txtSTRDT= new TxtDate(),2,5,1,2,this,'L');
			
			add(new JLabel("To Date"),3,3,1,2,this,'L');
			add(txtENDDT = new TxtDate(),3,5,1,2,this,'L');

			add(rdbGRDHS=new JRadioButton("Grade History"),4,3,1,3,this,'L');
			add(rdbEMPHS=new JRadioButton("Employee History"),4,6,1,3,this,'L');
			btgHSTRY=new ButtonGroup();
			add(chkPROCS=new JCheckBox("Process"),4,9,1,3,this,'L');
			chkPROCS.setVisible(false);
			btgHSTRY.add(rdbGRDHS);btgHSTRY.add(rdbEMPHS);
				
			add(chkSELAL=new JCheckBox("Select All"),5,3,1,3,this,'L');
            //CREATING TABLE FOR GRADE HISTORY
            String[] L_strTBLHD1 = {"FL","Grade"};
			int[] L_intCOLSZ1 = {20,200};
			tblGRADE = crtTBLPNL1(this,L_strTBLHD1,600,6,3,5,5,L_intCOLSZ1,new int[]{0});

			
			M_strSQLQRY="select cmt_codcd from co_cdtrn where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXGRD'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				tblGRADE.setValueAt(M_rstRSSET.getString("cmt_codcd"),intROW,TB_GRDCD);
				//tblGRADE.setValueAt(new Boolean(true),intROW,TB_CHKFL);
				intROW++;
			}
			M_rstRSSET.close();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_pnlRPFMT.setVisible(true);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			chkSELAL.addMouseListener(this);
			chkPROCS.addMouseListener(this);
			txtENDDT.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}

	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			if(L_ME.getSource() == chkSELAL)
			{
				if(chkSELAL.isSelected())
				{
					for(int i=0;i<intROW;i++)
						tblGRADE.setValueAt(new Boolean(true),i,TB_CHKFL);
				}
				else if(!chkSELAL.isSelected())
				{
					for(int i=0;i<intROW;i++)
						tblGRADE.setValueAt(new Boolean(false),i,TB_CHKFL);
				}
			}
			else if(L_ME.getSource() == chkPROCS)
			{
				if(chkPROCS.isSelected())
				{
					if(!vldDATA())
					{
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
						return;
					}					
					int intSTRYR = Integer.parseInt(txtSTRDT.getText().substring(6,10));
					int intENDYR = Integer.parseInt(txtENDDT.getText().substring(6,10));
					int intCURYR = Integer.parseInt(cl_dat.M_strLOGDT_pbst.substring(6,10));
			
					if((intSTRYR<intCURYR-1) || (intENDYR<intCURYR-1))
					{
						txtSTRDT.requestFocus();
						setMSG("Start Date and End Date Must be greater than or equal to previous year",'E');
						return;
					}
					for(int i=0;i<tblGRADE.getRowCount();i++)
					{
						if(tblGRADE.getValueAt(i,TB_CHKFL).toString().equals("true"))
						{
							this.setCursor(cl_dat.M_curWTSTS_pbst);
							if(rdbGRDHS.isSelected())
							{
								updHR_GRHST = cl_dat.M_conSPDBA_pbst.prepareCall("call updHR_GRHST(?,?,?,?)");
								updHR_GRHST.setString(1,cl_dat.M_strCMPCD_pbst);
								updHR_GRHST.setString(2,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText())));
								updHR_GRHST.setString(3,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText())));
								updHR_GRHST.setString(4,tblGRADE.getValueAt(i,TB_GRDCD).toString().trim());
								updHR_GRHST.executeUpdate();
							}
							else if(rdbEMPHS.isSelected())
							{
								updHR_EHTRN = cl_dat.M_conSPDBA_pbst.prepareCall("call updHR_EHTRN(?,?,?,?)");
								updHR_EHTRN.setString(1,cl_dat.M_strCMPCD_pbst);
								updHR_EHTRN.setString(2,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText())));
								updHR_EHTRN.setString(3,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText())));
								updHR_EHTRN.setString(4,tblGRADE.getValueAt(i,TB_GRDCD).toString().trim());
								updHR_EHTRN.executeUpdate();
							}
							this.setCursor(cl_dat.M_curDFSTS_pbst);
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
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
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
        	{						
        		if(M_objSOURC==txtSTRDT)		
					txtENDDT.requestFocus();
				if(M_objSOURC==txtENDDT)		
					rdbGRDHS.requestFocus();
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
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			String L_strGRDCD="";
			for(int i=0;i<tblGRADE.getRowCount();i++)
			{
				if(tblGRADE.getValueAt(i,TB_CHKFL).toString().equals("true"))
				{
					if(!L_strGRDCD.equals(""))
						L_strGRDCD+=",";
					L_strGRDCD += "'"+tblGRADE.getValueAt(i,TB_GRDCD).toString().trim()+"'";
				}
			}
			if(rdbGRDHS.isSelected())
			{
				M_strSQLQRY  = " Select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,";
				M_strSQLQRY += " GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,";
				M_strSQLQRY += " GR_WKALW,GR_WSALW,GR_EMPCT from HR_GRHST";
				M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(GR_STSFL,'') <> 'X'"; 
				M_strSQLQRY += " and GR_STRDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and GR_ENDDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
				M_strSQLQRY += " and GR_GRDCD in ("+L_strGRDCD+")";
				M_strSQLQRY += " order by GR_GRDCD,GR_STRDT";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					//System.out.println("RS NOT NULL");
					while(M_rstRSSET.next())
					{
					  D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRDCD"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_SPALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_LNSUB"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_CONVY"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_CHEDN"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_MDALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_LTALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_PFALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_SAALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_HRALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_DNALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_WKALW"),"").trim(),10));
					  D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_WSALW"),"").trim(),10));
					  crtNWLIN();
					}
				}
			}
			if(rdbEMPHS.isSelected())
			{
				M_strSQLQRY = " Select EH_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM,EP_DPTNM,EH_DESGN,EH_MMGRD,EH_BASAL,";
				M_strSQLQRY +=" EH_DNALW,EH_PPSAL,EH_GRSAL,EH_CTCVL,EH_TELAL,EH_VHMAL,EH_EPLOC,EH_QUALN from HR_EHTRN,HR_EPMST";
				M_strSQLQRY +=" where EP_CMPCD=EH_CMPCD and EP_EMPNO=EH_EMPNO and EH_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and EH_STRDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and EH_ENDDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
				M_strSQLQRY +=" and EH_MMGRD in ("+L_strGRDCD+")";
				M_strSQLQRY += " order by EH_MMGRD";
				//System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY); 
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EH_EMPNO"),""),5));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_EMPNM"),""),14));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""),8));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EH_DESGN"),""),8));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EH_MMGRD"),""),5));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("EH_BASAL"),""),8));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("EH_DNALW"),""),10));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("EH_PPSAL"),""),7));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("EH_GRSAL"),""),8));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("EH_CTCVL"),""),12));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("EH_TELAL"),""),10));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("EH_VHMAL"),""),12));
						D_OUT.writeBytes(padSTRING('R',"",3));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EH_EPLOC"),""),7));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EH_QUALN"),""),9));
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
				D_OUT.writeBytes("<HTML><HEAD><Title>History Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
			
			D_OUT.writeBytes(padSTRING('R',(rdbGRDHS.isSelected()?"Grade":"Employee") +" History From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),50));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		crtNWLIN();
			
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			if(rdbGRDHS.isSelected())
				D_OUT.writeBytes("Grade      Spc.Allw. Lunch-Sub     conv.  Chld.Alw.  Medical       LTA  PF Allow  GR_SAALW       HRA        DA  GR_WKALW  GR_WSALW");
			else if(rdbEMPHS.isSelected())
				D_OUT.writeBytes("Emp No   Name      Dept    Desig   Grade   Basic        DA  PPSAL   Gross         CTC  Tel Alw.   Veh. alw.   Locn   Qualfn");
			crtNWLIN();
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
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
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------------------------");
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
			boolean flgSELCT=false;
			for(int i=0;i<tblGRADE.getRowCount();i++)
			{
				if(tblGRADE.getValueAt(i,TB_CHKFL).toString().equals("true"))
				{
					flgSELCT = true;
				}
			}
			if(!flgSELCT)
			{
				setMSG("Please Select Grade Code in the table",'E');
				return false;
			}
			if(txtSTRDT.getText().length()<10)
			{
				txtSTRDT.requestFocus();
				setMSG("Please Enter From Date",'E');
				return false;
			}	
			if(txtENDDT.getText().length()<10)
			{
				txtENDDT.requestFocus();
				setMSG("Please Enter To Date",'E');
				return false;
			}		
			if(!rdbEMPHS.isSelected() && !rdbGRDHS.isSelected())
			{
				setMSG("Please Select Type Of Report (Grade History/Employee History)",'E');
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
			     strRPFNM = strRPLOC + "hr_rphis.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rphis.doc";
				
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
			if(input == txtSTRDT)
			{
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From date can not be greater than Today's date..",'E');
					txtSTRDT.requestFocus();
					return false;
				}
			}
			if(input == txtENDDT)
			{
				if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("TO date can not be greater than Today's date..",'E');
					return false;
				}
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
					return false;
				}
				int intSTRYR = Integer.parseInt(txtSTRDT.getText().substring(6,10));
				int intENDYR = Integer.parseInt(txtENDDT.getText().substring(6,10));
				int intCURYR = Integer.parseInt(cl_dat.M_strLOGDT_pbst.substring(6,10));
				
				if((intSTRYR<intCURYR-1) || (intENDYR<intCURYR-1))
				{
					chkPROCS.setVisible(false);
				}
				else
					chkPROCS.setVisible(true);
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



