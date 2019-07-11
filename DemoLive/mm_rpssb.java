import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;

/*

System Name     : Material Management System
Program Name    :  Material Stock Statements
Source Directory : f:\source\splerp2\mm_rpssb..java                        Executable Directory : f:\exec\splerp2\mm_rpssb.class

Purpose : Program to generate the reports for Stock Statement.



List of tables used :
 Table Name		Primary key							Operation done
														          Insert      Update	       Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
CO_CTMST		CT_GRPCD,CT_CODTP,CT_MATCD														#	
MM_GRMST		GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO									#	 
MM_STMST		ST_MMSBS,ST_STRTP,ST_MATCD			    		    							#
-----------------------------------------------------------------------------------------------------------------------------------------------------




List of  fields accepted/displayed on screen :
Field Name		Column Name					Table name			  Type/Size			  Description

- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
TxtFMGRP		SUBSTRING(ST_MATCD,1,2)        MM_STMST              VARCHAR(10)        Material Code
TxtTOGRP		SUBSTRING(ST_MATCD,1,2)        MM_STMST              VARCHAR(10)        Material Code
-----------------------------------------------------------------------------------------------------------------------------------------------------




List of fields with help facility : 
Field Name	Display Description		Display Columns			Table Name

- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
TxtFMGRP  	Group Code,Group Name		CT_GRPCD,CT_MATDS		CO_CTMST
TxtTOGRP	Group Code,Group Name		CT_GRPCD,CT_MATDS		CO_CTMST -----------------------------------------------------------------------------------------------------------------------------------------------------



Validations :

Entries for TxtFMGRP,TxtTOGRP are compulsary.
Entries for TxtFMGRP,TxtTOGRP must be valid.
Value of TxtFMGRP must be less than or Equal to value of TxtTOGRP.

*/

class mm_rpssb extends cl_rbase 
{
	private JTextField txtFMGRP;	/** JTextField to display to specify To Group.*/
	private JTextField txtTOGRP;	/** JTextField to display to specify From Sub Gr*/
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mm_rpssb.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;
	private String strSTRTP;
									/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private ButtonGroup btgRPTST;    
	private JRadioButton rdbALLITM;	 
	private JRadioButton rdbOBSITM;	
	
	mm_rpssb()		/*  Constructor   */
	{
		super(2);
		try
		{
			M_pnlRPFMT.setVisible(true);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			setMatrix(20,8);
			add(new JLabel("From Main Group"),4,4,1,2,this,'R');
			add(txtFMGRP = new TxtNumLimit(2.0),4,5,1,1,this,'L');
			add(new JLabel("To Main Group"),5,4,1,2,this,'R');
			add(txtTOGRP = new TxtNumLimit(2.0),5,5,1,1,this,'L');

			btgRPTST=new ButtonGroup();		
			add(new JLabel("Stock Statment of"),3,1,1,1.3,this,'L');
			add(rdbALLITM=new JRadioButton("All Items",true),3,3,1,1,this,'L');
			add(rdbOBSITM=new JRadioButton("Obsolete Items"),3,4,1,2,this,'L');
			btgRPTST.add(rdbALLITM);
			btgRPTST.add(rdbOBSITM);
			
			txtFMGRP.setInputVerifier(new INPVF());
			txtTOGRP.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			setENBL(false);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			setENBL(true);				
			txtFMGRP.requestFocus();
			txtFMGRP.setText("");
			txtTOGRP.setText("");
			setMSG("Enter Main Group Code Or Press 'F1' For Help",'N');
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				cl_dat.M_flgHELPFL_pbst = true; 
				setMSG("",'N');
				setCursor(cl_dat. M_curWTSTS_pbst);				
				if(M_objSOURC == txtFMGRP)
				{
					M_strHLPFLD = "txtFMGRP";
					M_strSQLQRY ="SELECT CT_GRPCD,CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='MG' ";
					if(txtFMGRP.getText().trim().length() >0)
						M_strSQLQRY += " AND CT_GRPCD like '"+txtFMGRP.getText().trim() +"%'";
					if(!M_strSBSCD.substring(2,4).equals("01"))
					{
						if(!M_strSBSCD.substring(2,4).equals("11"))
							M_strSQLQRY += " AND CT_GRPCD ='68'";
						else
							M_strSQLQRY += " AND CT_GRPCD ='95'";
					}				
					if(txtTOGRP.getText().trim().length() >0)
						M_strSQLQRY += " AND CT_GRPCD <='"+txtTOGRP.getText().trim() +"'";
					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Group Name"},2,"CT");
				}
				if(M_objSOURC == txtTOGRP)
				{
					M_strHLPFLD = "txtTOGRP";
					M_strSQLQRY ="SELECT CT_GRPCD,CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='MG' ";
					if(txtTOGRP.getText().trim().length() >0)
						M_strSQLQRY += " AND CT_GRPCD like '"+txtTOGRP.getText().trim() +"%'";				
					if(!M_strSBSCD.substring(2,4).equals("01"))
					{
						if(!M_strSBSCD.substring(2,4).equals("11"))
							M_strSQLQRY += " AND CT_GRPCD ='68'";
						else
							M_strSQLQRY += " AND CT_GRPCD ='95'";
					}
					if(txtFMGRP.getText().trim().length() >0)
						M_strSQLQRY += " AND CT_GRPCD >='"+txtFMGRP.getText().trim() +"'";				
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Group Name"},2,"CT");
				}
				if(M_rstRSSET==null)
				{
					M_rstRSSET.close();
				}	
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				setMSG("",'N');
				if(M_objSOURC == txtFMGRP)
				{
					if(txtFMGRP.getText().trim().length()>0)
						txtTOGRP.requestFocus();
					else
						setMSG("To group Code Cannot be Blank..",'E');
				}
				if(M_objSOURC == txtTOGRP)
				{
					if(txtTOGRP.getText().trim().length()>0)
						cl_dat.M_btnSAVE_pbst.requestFocus();
					else
						setMSG("From group Code Cannot be Blank..",'E');
					
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
			if(M_strHLPFLD == "txtFMGRP")
			{
				txtFMGRP.setText(cl_dat.M_strHLPSTR_pbst);
				txtTOGRP.requestFocus();
			}
			if(M_strHLPFLD == "txtTOGRP")
			{
				txtTOGRP.setText(cl_dat.M_strHLPSTR_pbst);
				cl_dat.M_btnSAVE_pbst.requestFocus();
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
			String L_STKQT,L_MATCD="";
			double L_TOTSTK=0.000;
			int L_cntMATCD=0;
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			String L_strOBSCHK = rdbOBSITM.isSelected() ? " and isnull(ST_STSFL,'') in ('9','X') "  : "";

			//M_strSQLQRY = "SELECT GR_MATCD,ST_MATDS, ST_UOMCD, ST_LOCCD,GR_BATNO,SUM(isnull(GR_ACPQT,0))-SUM(isnull(GR_ISSQT,0)) L_STKQT FROM MM_GRMST,MM_STMST "
			//+"WHERE GR_CMPCD = ST_CMPCD and GR_STRTP=ST_STRTP AND GR_MATCD=ST_MATCD AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP='"+M_strSBSCD.substring(2,4)+"' AND "
			////+"SUM(isnull(GR_ACPQT,0))-SUM(isnull(GR_ISSQT,0)) > 0 AND "
			//+"SUBSTRING(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText()+"' AND '"+txtTOGRP.getText()+"' "
			//+"GROUP BY GR_MATCD,ST_MATDS, ST_UOMCD, ST_LOCCD,GR_BATNO ORDER BY GR_MATCD ";
			M_strSQLQRY = "SELECT GR_MATCD,ST_MATDS, ST_UOMCD, ST_LOCCD,GR_BATNO,SUM(isnull(GR_ACPQT,0))-SUM(isnull(GR_ISSQT,0)) L_STKQT FROM MM_STMST "
			+" left outer join MM_GRMST on ST_CMPCD = GR_CMPCD and ST_STRTP = GR_STRTP and ST_MATCD = GR_MATCD WHERE  ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP='"+M_strSBSCD.substring(2,4)+"'"+ L_strOBSCHK
			//+"SUM(isnull(GR_ACPQT,0))-SUM(isnull(GR_ISSQT,0)) > 0 AND "
			+" and SUBSTRING(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText()+"' AND '"+txtTOGRP.getText()+"' "
			+"GROUP BY GR_MATCD,ST_MATDS, ST_UOMCD, ST_LOCCD,GR_BATNO ORDER BY GR_MATCD ";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(Float.parseFloat(M_rstRSSET.getString("L_STKQT"))!=0)
					{	
						if(!M_rstRSSET.getString("GR_MATCD").equals(L_MATCD))
						{	
							if(L_cntMATCD>1)
							{	
								prnFMTCHR(D_OUT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									D_OUT.writeBytes("<b>");
								D_OUT.writeBytes(padSTRING('L',"Total Stock     ",91));
								D_OUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(""+L_TOTSTK),3),11));
								prnFMTCHR(D_OUT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									D_OUT.writeBytes("</b>");
								crtNWLIN();			
							}
							crtNWLIN();							
							L_TOTSTK=0;
							L_cntMATCD=0;
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("GR_MATCD"),12));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_MATDS"),54));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_UOMCD"),5));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_LOCCD"),10));
						}
						else D_OUT.writeBytes(padSTRING('R',"",81));	
						L_cntMATCD++;
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("GR_BATNO"),10));
						D_OUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(M_rstRSSET.getString("L_STKQT")),3),11));
						L_TOTSTK+=Double.parseDouble(M_rstRSSET.getString("L_STKQT"));
						//strLNSUB1=nvlSTRVL(tblGRADE.getValueAt(P_intROWNO,TB1_LNSUB).toString(),"0.00");
						//strLNSUB = setNumberFormat(Double.parseDouble(strLNSUB1),2);
						L_MATCD=M_rstRSSET.getString("GR_MATCD");
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
				prnFMTCHR(D_OUT,M_strCPI12);
				//prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
			
				
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title>Material Stock Status</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
			D_OUT.writeBytes(padSTRING('R',"Material Stock Status  "+(rdbOBSITM.isSelected() ? "(Obsolete Items)" : ""),50));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('R',"Store Type   :"+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),50));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Material  Code And Description                                    UOM  Location  Batch No  Stk.On Hand");
			crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------");
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
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------");
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
			if(txtFMGRP.getText().toString().length() == 0)
			{
				setMSG("Please Enter Group Type Or Press F1 For Help..",'E');
				txtFMGRP.requestFocus();
				return false;
			}
			if(txtTOGRP.getText().toString().length() == 0)
			{
				setMSG("Please Enter Group Type Or Press F1 For Help..",'E');
				txtTOGRP.requestFocus();
				return false;
			}
			if(Integer.parseInt(txtTOGRP.getText().toString()) < Integer.parseInt(txtFMGRP.getText().toString()))
			{
				setMSG("To Group Should Be Greater Than Or Equal To From Group ",'E');
				txtTOGRP.requestFocus();
				return false;
			}
		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA");
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
			     strRPFNM = strRPLOC + "mm_rpssb.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "mm_rpssb.doc";
				
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
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
			if(input == txtFMGRP || input == txtTOGRP )
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					M_rstRSSET = cl_dat.exeSQLQRY("Select count(*) from MM_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD LIKE '"+((JTextField)input).getText()+"%' AND ST_MMSBS = '"+M_strSBSCD+"'");
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							if(M_rstRSSET.getInt(1)<= 0)
							{
								setMSG("Invalid Material Group ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Material Group ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Material Group ",'E');
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



