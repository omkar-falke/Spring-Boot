import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;
/*
System Name    : Marketing System.
Program Name   : Customer Permit Form Query / Report (available forms) 
Program Desc.  : This module displays/prints detail about available permit form status 
				 according to the input combinations specified by user. 
Author         : Mr.Aawaj Jain
Date           : 2007
Version        : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :

System Name    : Marketing System.
 
Program Name   : Customer Permit form query/report.

Purpose        : This module displays/prints detail about available permit form status 
				  according to the input combinations specified by user.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MR_PFTRN       PFT_PRTTP,PFT_PRTCD,PFT_FRMTP,PFT_FRMNO                 #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD						   #	
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtPRTTP    PFT_PRTTP       MR_PFTRN      VARCHAR(1)     Party Type
txtPRTCD    PFT_PRTCD		MR_PFTRN	  VARCHAR(5)	 Party Code
txtFRMTP	PFT_FRMTP		MR_PFTRN	  VARCHAR(1)	 Form Type
txtLOCCD    Variable		-			  VARCHAR(1)	 Location 
txtFMDAT    PFT_XXXDT	    MM_PFTRN	  DATE		 	 From Date 	
txtTODAT    PFT_XXXDT	    MM_PFTRN	  DATE			 To Date
--------------------------------------------------------------------------------------

List of Fields with help facility
Field Name  Display Description         Display Columns           Table Name
---------------------------------------------------------------------------------------------
txtPRTTP    Party Type,Description      cmt_codcd,cmt_codds		  CO_CDTRN-MST/COXXPRT
txtPRTCD	Party Code,Party Name		pt_prtcd,pt_prtnm		  CO_PTMST
txtFRMTP	Form Type,Description		cmt_codcd,cmt_codds		  CO_CDTRN-SYS/MRXXFTP		
---------------------------------------------------------------------------------------------



Validations :
    - To date must be greater then From data & Smaller than Current date.
    - If Party Code is not specified then record for all parties will be fetched display.
	- If form type is not specified record for all form type will be fetched.
	- If location is not specified , records at all locations will be fetched and from and to date validations will not be applied
	- Report will be generated in text/HTML format for generating hard copy, when print option is selected.	
 */

class mr_rppfa extends cl_rbase 
{

	private JTextField txtPRTTP;			
	private JTextField txtPRTCD;
	private JTextField txtFRMTP;
	private JTextField txtLOCCD;
	private JTextField txtFMDAT;			
	private JTextField txtTODAT;
	private JLabel lblPRTNM;
	private	String PFTXXXDT;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mr_rppfa.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    		
	mr_rppfa()		/*  Constructor   */
	{
		super(2);
		try
		{
			//System.out.println("hiiiiiiiiii");
			lblPRTNM=new JLabel("");
			
			add(new JLabel("Party Type          "),5,3,1,1,this,'L');
			add(txtPRTTP= new TxtLimit(1),5,4,1,1,this,'L');
						
			add(new JLabel("Party Code          "),6,3,1,1,this,'L');
			add(txtPRTCD= new TxtLimit(5),6,4,1,1,this,'L');
			add(lblPRTNM,6,5,1,8,this,'L');     
			
			add(new JLabel("Location            "),7,3,1,1,this,'L');
			add(txtLOCCD= new TxtLimit(3),7,4,1,1,this,'L');
			
			add(new JLabel("Form Type           "),8,3,1,1,this,'L');
			add(txtFRMTP= new TxtLimit(2),8,4,1,1,this,'L');
			
			add(new JLabel("From Date           "),9,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),9,4,1,1,this,'L');
						
			add(new JLabel("To Date              "),10,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),10,4,1,1,this,'L');
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtPRTTP.setInputVerifier(oINPVF);
			txtFMDAT.setInputVerifier(oINPVF);
			txtTODAT.setInputVerifier(oINPVF);
			txtLOCCD.setInputVerifier(oINPVF);
			txtPRTCD.setInputVerifier(oINPVF);
			txtFRMTP.setInputVerifier(oINPVF);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);

		if(txtPRTCD.getText().length()==0)
		{
			lblPRTNM.setText("");		
		}	
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
				M_cmbDESTN.requestFocus(); 
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst)){
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst)){
				setENBL(true);
			}
		}	
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC==txtPRTTP)
				{
					M_strHLPFLD="txtPRTTP";
					M_strSQLQRY=" Select distinct MR_PFTRN.pft_prttp,CO_CDTRN.cmt_codds from MR_PFTRN,CO_CDTRN where";
					M_strSQLQRY+=" CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT' and MR_PFTRN.PFT_PRTTP=CO_CDTRN.cmt_codcd and MR_PFTRN.PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' order by pft_prttp";
					//System.out.println("....."+M_rstRSSET);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Type", "Description"},2,"CT",new int[]{107,400});
				}	
				if(M_objSOURC == txtPRTCD)
				{
					M_strHLPFLD="txtPRTCD";
			
					M_strSQLQRY="Select distinct mr_pftrn.pft_prtcd,co_ptmst.pt_prtnm from MR_PFTRN,CO_PTMST  where ";
					M_strSQLQRY+="MR_PFTRN.PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_PFTRN.PFT_PRTTP = '"+txtPRTTP.getText()+"' and ";
					M_strSQLQRY+="PFT_PRTCD like '"+txtPRTCD.getText().trim().toUpperCase()+"%' and PFT_PRTTP=pt_prttp and PFT_PRTCD=PT_PRTCD";
					M_strSQLQRY+= " ";//" order by co_ptmst.prtnm ";
					//System.out.println(">>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT",new int[]{107,400});
				}	
				if(M_objSOURC==txtFRMTP)
				{
					M_strHLPFLD="txtFRMTP";
					M_strSQLQRY="Select distinct cmt_codcd,cmt_codds from CO_CDTRN where  CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MRXXFTP'";
					M_strSQLQRY += "  and CMT_CODCD in (select PFT_FRMTP from MR_PFTRN where ";
					if(txtPRTCD.getText().length()>0)
						M_strSQLQRY += " PFT_PRTCD= '"+txtPRTCD.getText()+"' and ";
					M_strSQLQRY += " PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP= '"+txtPRTTP.getText()+"' )";
					//System.out.println(">>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Form Type", "Description"},2,"CT",new int[]{107,400});
				}	
			}	
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtPRTTP && txtPRTTP.getText().length()>0)
					txtPRTCD.requestFocus();
				if(M_objSOURC==txtPRTCD)
					txtLOCCD.requestFocus();
				if(M_objSOURC==txtLOCCD)
				{
					txtLOCCD.getText().toUpperCase();
					txtFRMTP.requestFocus();
				}
				if(M_objSOURC==txtFRMTP)
					txtFMDAT.requestFocus();
				if(M_objSOURC == txtFMDAT)
				{
					txtTODAT.requestFocus();
				}
				if(M_objSOURC == txtTODAT)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
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
			//System.out.println("in exeHLPOK");
			if(M_strHLPFLD.equals("txtPRTTP"))
			{
				txtPRTTP.setText(cl_dat.M_strHLPSTR_pbst);
			}	
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			}	
			if(M_strHLPFLD.equals("txtFRMTP"))
			{
				txtFRMTP.setText(cl_dat.M_strHLPSTR_pbst);
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
			genRPHDR();
					
			M_strSQLQRY="select PFT_FRMTP,PFT_PRTCD,PFT_FRMNO,PFT_ISSDT,PFT_DLHDT,PFT_CMSDT,PFT_NGTDT,pt_prtnm from MR_PFTRN,CO_PTMST ";
			M_strSQLQRY+=" where pft_prttp=pt_prttp and pft_prtcd=pt_prtcd and isnull(PFT_STSFL,'')<>'X' and";
			M_strSQLQRY+=" PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP='"+txtPRTTP.getText().trim()+"' ";
					
			
			if(txtPRTCD.getText().length()>0)
			{	
				M_strSQLQRY+=" and PFT_PRTCD='"+txtPRTCD.getText()+"'"; 
			}
			if(txtFRMTP.getText().length()>0)
			{	
				M_strSQLQRY+=" and PFT_FRMTP='"+txtFRMTP.getText().trim()+"'"; 
			}
			if(txtLOCCD.getText().length() > 0)
		    {
				if(txtLOCCD.getText().equals("DLH"))
				{
					PFTXXXDT="PFT_DLHDT";
				}	
				if(txtLOCCD.getText().equals("CMS"))
				{
					PFTXXXDT="PFT_CMSDT";
				}	
				if(txtLOCCD.getText().equals("NGT"))
				{
					PFTXXXDT="PFT_NGTDT";
				}	
				if((txtFMDAT.getText().length() >0)&&(txtTODAT.getText().length() >0))
					M_strSQLQRY+=" and CONVERT(varchar,"+PFTXXXDT+",101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
			}  
			M_strSQLQRY+=" order by PFT_PRTTP,PFT_PRTCD"; 
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	
			//System.out.println(">>>>"+M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PFT_FRMTP"),11));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PFT_FRMNO"),10));
					D_OUT.writeBytes(padSTRING('R',txtPRTTP.getText()+"/"+M_rstRSSET.getString("PFT_PRTCD"),12));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTNM"),33));
					if(M_rstRSSET.getDate("PFT_ISSDT")==null)
						D_OUT.writeBytes(padSTRING('R',"",12));
					else
						D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_ISSDT")).toString(),12));
					
					if(txtLOCCD.getText().equals("DLH"))
						{
							D_OUT.writeBytes(padSTRING('R',"DLH",10));
							if(M_rstRSSET.getDate("PFT_DLHDT")==null)
								D_OUT.writeBytes(padSTRING('R',"",12));
							else	
								D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_DLHDT")).toString(),12));
						}	
					
					if(txtLOCCD.getText().equals("CMS"))
						{
							D_OUT.writeBytes(padSTRING('R',"CMS",10));
							if(M_rstRSSET.getDate("PFT_CMSDT")==null)
								D_OUT.writeBytes(padSTRING('R',"",12));
							else	
								D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_CMSDT")).toString(),12));
						}
					
					if(txtLOCCD.getText().equals("NGT"))
						{	
							D_OUT.writeBytes(padSTRING('R',"NGT",10));
							if(M_rstRSSET.getDate("PFT_NGTDT")==null)
								D_OUT.writeBytes(padSTRING('R',"",12));
							else	
								D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_NGTDT")).toString(),12));
						}	
					
					if(txtLOCCD.getText().equals("")) 
					{
						//System.out.println("inside blank");	
						if(M_rstRSSET.getDate("PFT_NGTDT")!=null)
						{	
							D_OUT.writeBytes(padSTRING('R',"NGT",10));
							D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_NGTDT")).toString(),12));
						}	
						
						else if(M_rstRSSET.getDate("PFT_CMSDT")!=null)
						{	
							D_OUT.writeBytes(padSTRING('R',"CMS",10));
							D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_CMSDT")).toString(),12));
						}	
						
						else if(M_rstRSSET.getDate("PFT_DLHDT")!=null)
						{	
							D_OUT.writeBytes(padSTRING('R',"DLH",10));
							D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_DLHDT")).toString(),12));
						}
					}	
					
					
					crtNWLIN();
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
			if(cl_dat.M_intLINNO_pbst >60)
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
				D_OUT.writeBytes("<HTML><HEAD><Title>Permit Form Status(Available Forms)</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
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
    		//cl_dat.M_intLINNO_pbst+=1;
    		D_OUT.writeBytes(padSTRING('R',"Permit Form Status(Available Forms)  ",50));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		crtNWLIN();
			
			if(txtPRTCD.getText().length()>0)
			{
				D_OUT.writeBytes(padSTRING('R',"Party Code   : "+txtPRTCD.getText().toString(),48));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Party Name   : "+lblPRTNM.getText(),48));
				crtNWLIN();
			}	
			if(txtLOCCD.getText().length()>0)
			{
				D_OUT.writeBytes(padSTRING('R',"Location     : "+txtLOCCD.getText().toString(),48));
				crtNWLIN();
			}	
			if(txtFMDAT.getText().length()>0 && txtTODAT.getText().length()>0)
			{	
				D_OUT.writeBytes(padSTRING('R',"Period From    "+txtFMDAT.getText().toString()+"   To  "+txtTODAT.getText().toString(),48));
				crtNWLIN();
			}
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Form Type  Form No.  Party Code  Party Name                       Issue Date  Location  Receipt Date");
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
			crtNWLIN();
			crtNWLIN();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
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
			if(txtPRTTP.getText().length()!= 1)
			{
				setMSG("Enter Party Type",'E');
				txtPRTTP.setEnabled(true);
				txtPRTTP.requestFocus();
				return false;
			}
			if(txtLOCCD.getText().length()>0)
			{	
				if(txtFMDAT.getText().length()==0)
				{
					txtFMDAT.setEnabled(true);
					setMSG("Enter From Date",'E');
					txtFMDAT.requestFocus();
					return false;
				}	
				if(txtTODAT.getText().length()==0)
				{
					txtTODAT.setEnabled(true);
					setMSG("Enter To Date",'E');
					txtTODAT.requestFocus();
					return false;
				}	
			}	
			return true;
		}
		catch(Exception L_VLD)
		{
			return false;
		}
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
			     strRPFNM = strRPLOC + "mr_rppfa.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "mr_rppfa.doc";
				
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
			
			if(((JTextField)input).getText().length() == 0)
					return true;
			
			if(input == txtPRTTP)
			{
					M_strSQLQRY=" Select distinct pft_prttp from mr_pftrn where";
					M_strSQLQRY+=" PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pft_prttp='"+txtPRTTP.getText().trim().toUpperCase()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
									
					//System.out.println(">>>>"+M_strSQLQRY);
					//System.out.println(">>>>"+M_rstRSSET);
					
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
						
					}	
					else
					{
						setMSG("Enter Valid Party Type",'E');
						return false;
					}
			}	
			if(input==txtPRTCD)
			{
				if(txtPRTCD.getText().length()<5)
				{	
					setMSG("Enter Valid Party Code",'E');
					return false;
				}
				else
				{
					M_strSQLQRY="Select distinct pft_prtcd,pt_prtnm from MR_PFTRN,co_ptmst where pft_prttp=pt_prttp and pft_prtcd=pt_prtcd and ";
					M_strSQLQRY+="PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP = '"+txtPRTTP.getText()+"' and ";
					M_strSQLQRY+="PFT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"' ";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());	
						lblPRTNM.setText(M_rstRSSET.getString("pt_prtnm"));
					}	
					else
					{
						setMSG("Enter Valid Party Code",'E');
						return false;
					}
				}
			}
			if(input==txtLOCCD)
			{
				txtLOCCD.setText(txtLOCCD.getText().trim().toUpperCase());
				if(!txtLOCCD.getText().trim().toUpperCase().equals("") && !txtLOCCD.getText().trim().toUpperCase().equals("DLH")
				   && !txtLOCCD.getText().trim().toUpperCase().equals("CMS") && !txtLOCCD.getText().trim().toUpperCase().equals("NGT"))   
				{
					setMSG("Enter valid Location code",'E');
					return false;
				}	
			}	
			if(input==txtFRMTP)
			{
					M_strSQLQRY="Select distinct mr_pftrn.pft_frmtp,co_cdtrn.cmt_codds from CO_CDTRN,mr_pftrn where  ";
					M_strSQLQRY+="mr_pftrn.pft_frmtp=CO_CDTRN.cmt_codcd and MR_PFTRN.PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_PFTRN.PFT_FRMTP = '"+txtFRMTP.getText().trim().toUpperCase()+"'";
					
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtFRMTP.setText(txtFRMTP.getText().trim().toUpperCase());
					}	
					else
					{
						setMSG("Enter Valid Form Type",'E');
						return false;
					}
				}	
			if(input == txtFMDAT)
			{
				if(M_fmtLCDAT.parse(txtFMDAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From date can not be greater than Today's date..",'E');
					return false;
				}
				//txtTODAT.requestFocus();
			}
			if(input == txtTODAT)
			{
				if(M_fmtLCDAT.parse(txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("TO date can not be greater than Today's date..",'E');
					return false;
				}
				else if(M_fmtLCDAT.parse(txtFMDAT.getText()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
					return false;
				}
				
				//cl_dat.M_btnSAVE_pbst.requestFocus();
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



