import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.sql.*;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.BorderFactory;
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

System Name	   : Marketing System.
 
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
	- If from & to date is not secified , records irrespective of despatch dates range will be fetched for display.
	- Report will be generated in text/HTML format for generating hard copy, when print option is selected.	
 */

class mr_rppfd extends cl_rbase 
{

	private JTextField txtPRTTP;			
	private JTextField txtPRTCD;
	private JTextField txtFRMTP;
	private JTextField txtFMDAT;			
	private JTextField txtTODAT;
	private JLabel lblPRTNM;
	private	String PFTXXXDT;
	private	String strSTSDS;
	private	String strSTSDT;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mr_rppfd.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    
    private JRadioButton  rdbISS;
	private JRadioButton  rdbDLH;
	private JRadioButton  rdbCMS;
	private JRadioButton  rdbNGT;
	private JRadioButton  rdbINV;
	private JRadioButton  rdbALL;
	private ButtonGroup   btgSTATS;
	
	private JComboBox cmbORDBY;
	
	private JRadioButton  rdbSUMRP;
	private JRadioButton  rdbDTLRP;
	private ButtonGroup   btgPFDTL;
	
	private int intRECCT;	
	
	private JPanel M_pnlSTATS;
   // private int intLINNO = 0;
		
	mr_rppfd()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,8);
			//System.out.println("hiiiiiiiiii");
			
			
			lblPRTNM=new JLabel("");
			
			add(new JLabel("Party Type          "),5,3,1,1,this,'L');
			add(txtPRTTP= new TxtLimit(1),5,4,1,1,this,'L');
						
			add(new JLabel("Party Code          "),6,3,1,1,this,'L');
			add(txtPRTCD= new TxtLimit(5),6,4,1,1,this,'L');
			add(lblPRTNM,6,5,1,8,this,'L');     
			
			add(new JLabel("Form Type           "),7,3,1,1,this,'L');
			add(txtFRMTP= new TxtLimit(2),7,4,1,1,this,'L');
			
			add(new JLabel("From Date           "),8,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),8,4,1,1,this,'L');
						
			add(new JLabel("To Date              "),9,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),9,4,1,1,this,'L');
			
			M_pnlSTATS= new JPanel(null);
			
			//M_pnlSTATS.setBorder(new EtchedBorder(Color.gray,Color.lightGray)); 
			add(rdbISS=new JRadioButton("Issued"),1,1,1,1,M_pnlSTATS,'R');
			add(rdbDLH=new JRadioButton("Delhi"),2,1,1,1,M_pnlSTATS,'R');
			add(rdbCMS=new JRadioButton("CMS"),3,1,1,1,M_pnlSTATS,'R'); 
			add(rdbNGT=new JRadioButton("Nagothane"),4,1,1,1,M_pnlSTATS,'R');
			add(rdbINV=new JRadioButton("Invoiced"),5,1,1,1,M_pnlSTATS,'R');
			add(rdbALL=new JRadioButton("All"),6,1,1,1,M_pnlSTATS,'R');
			btgSTATS=new ButtonGroup();
			btgSTATS.add(rdbISS); 
			btgSTATS.add(rdbDLH); 
			btgSTATS.add(rdbCMS); 
			btgSTATS.add(rdbNGT); 
			btgSTATS.add(rdbINV);
			btgSTATS.add(rdbALL);
			M_pnlSTATS.setBorder(BorderFactory.createTitledBorder("Status / Scope"));
			add(M_pnlSTATS,4,7,7,1,this,'R');
			 
			
			add(rdbSUMRP=new JRadioButton("Summery"),11,3,1,1,this,'L');
			add(rdbDTLRP=new JRadioButton("Detail"),11,4,1,1,this,'L');
			btgPFDTL=new ButtonGroup();
			btgPFDTL.add(rdbSUMRP); 
			btgPFDTL.add(rdbDTLRP);
			
			
			add(new JLabel("Order By"),4,3,1,1,this,'R');
    		add(cmbORDBY = new JComboBox(),4,4,1,2,this,'L');
    		cmbORDBY.addItem("Select");
			cmbORDBY.addItem("Form Type");
			cmbORDBY.addItem("Form No");
			//cmbORDBY.addItem("Party Code");
			cmbORDBY.addItem("Party Name");
			//cmbORDBY.addItem("Status");
			
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			rdbSUMRP.setSelected(true);
			rdbALL.setSelected(true);
			
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
			txtPRTCD.setInputVerifier(oINPVF);
			txtFRMTP.setInputVerifier(oINPVF);
			txtPRTTP.setText("C");
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
				/*if(M_objSOURC == txtPRTCD)
				{
					M_strHLPFLD="txtPRTCD";
			
					M_strSQLQRY="Select distinct mr_pftrn.pft_prtcd,co_ptmst.pt_prtnm from MR_PFTRN,CO_PTMST  where ";
					M_strSQLQRY+="MR_PFTRN.PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_PFTRN.PFT_PRTTP = '"+txtPRTTP.getText()+"' and ";
					M_strSQLQRY+="PFT_PRTCD like '"+txtPRTCD.getText().trim().toUpperCase()+"%' and PFT_PRTTP=pt_prttp and PFT_PRTCD=PT_PRTCD";
					M_strSQLQRY+= " order by co_ptmst.prtnm ";
					System.out.println(">>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT",new int[]{107,400});
				}*/	
				if(M_objSOURC == txtPRTCD)
				{
					M_strHLPFLD="txtPRTCD";
			
					M_strSQLQRY="Select distinct pft_prtcd,pt_prtnm from MR_PFTRN,CO_PTMST  where ";
					M_strSQLQRY+=" PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP = '"+txtPRTTP.getText()+"' and ";
					M_strSQLQRY+="PFT_PRTCD like '"+txtPRTCD.getText().trim().toUpperCase()+"%' and PFT_PRTTP=pt_prttp and PFT_PRTCD=PT_PRTCD";
					M_strSQLQRY+= " order by pt_prtnm ";
					System.out.println(">>>>>"+M_strSQLQRY);
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
					txtFRMTP.requestFocus();
				if(M_objSOURC==txtFRMTP)
					txtFMDAT.requestFocus();
				if(M_objSOURC == txtFMDAT)
					txtTODAT.requestFocus();
				if(M_objSOURC == txtTODAT)
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
				
			M_strSQLQRY="select PFT_FRMTP,PFT_PRTCD,PFT_FRMNO,PFT_INVNO,PFT_LRYNO,PFT_INVDT,pt_prtnm,cmt_codds pt_dstds,pft_issdt,pft_dlhdt,pft_cmsdt,pft_ngtdt from MR_PFTRN,CO_PTMST ";
			M_strSQLQRY+=" left outer join co_cdtrn on cmt_cgmtp='SYS' and cmt_cgstp='COXXDST' and cmt_codcd = isnull(PT_DSTCD,'') where pft_prttp=pt_prttp and pft_prtcd=pt_prtcd and PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PFT_STSFL,'')<>'X' and";
			M_strSQLQRY+=" PFT_PRTTP='"+txtPRTTP.getText().trim()+"' ";
			
					
			
			if(txtPRTCD.getText().length()>0)
			{	
				M_strSQLQRY+=" and PFT_PRTCD='"+txtPRTCD.getText()+"'"; 
			}
			if(txtFRMTP.getText().length()>0)
			{	
				M_strSQLQRY+=" and PFT_FRMTP='"+txtFRMTP.getText().trim()+"'"; 
			}
			if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
		    {
				M_strSQLQRY+=" and CONVERT(varchar,PFT_ISSDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
			}  
			
			if(rdbISS.isSelected())
			{
				M_strSQLQRY += " and PFT_ISSDT is not null and PFT_DLHDT is null and PFT_CMSDT is null and PFT_NGTDT is null and PFT_INVDT is null";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_ISSDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbDLH.isSelected())
			{
				M_strSQLQRY += " and PFT_DLHDT is not null and PFT_CMSDT is null and PFT_NGTDT is null and PFT_INVDT is null";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_DLHDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbCMS.isSelected())
			{
				M_strSQLQRY += " and PFT_CMSDT is not  null and PFT_NGTDT is null and PFT_INVDT is null";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_CMSDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbNGT.isSelected())
			{
				M_strSQLQRY += " and PFT_NGTDT is not null and PFT_INVDT is null";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_NGTDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbINV.isSelected())
			{
				M_strSQLQRY += " and PFT_INVDT is not null";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_INVDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			
			if(cmbORDBY.getSelectedIndex() == 0)
				M_strSQLQRY+=" order by PFT_PRTTP"; 
			else if(cmbORDBY.getSelectedIndex() == 1)
				M_strSQLQRY+=" order by PFT_FRMTP"; 
			else if(cmbORDBY.getSelectedIndex() == 2)
				M_strSQLQRY+=" order by PFT_FRMNO"; 
			//else if(cmbORDBY.getSelectedIndex() == 3)
			//	M_strSQLQRY+=" order by PFT_PRTCD"; 
			else if(cmbORDBY.getSelectedIndex() == 3)
				M_strSQLQRY+=" order by PT_PRTNM"; 
			
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			strSTSDS = "";
			strSTSDT = "";
	
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PFT_FRMTP"),11));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PFT_FRMNO"),10));
					D_OUT.writeBytes(padSTRING('R',txtPRTTP.getText()+"/"+M_rstRSSET.getString("PFT_PRTCD"),12));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTNM")+" - "+M_rstRSSET.getString("pt_dstds"),46));
					
					if(M_rstRSSET.getString("PFT_INVNO")==null)
						D_OUT.writeBytes(padSTRING('R',"",8));
					else
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PFT_INVNO"),8));
					if(M_rstRSSET.getString("PFT_LRYNO")==null)
						D_OUT.writeBytes(padSTRING('R',"",13));
					else	
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PFT_LRYNO"),13));
					/*if(M_rstRSSET.getDate("PFT_INVDT")==null)
						D_OUT.writeBytes(padSTRING('R',"",15));
					else	
						D_OUT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_INVDT")).toString(),15));*/
					
					getSTSVL(M_rstRSSET);
					D_OUT.writeBytes(padSTRING('R',strSTSDS,13));
					D_OUT.writeBytes(padSTRING('R',strSTSDT,10));
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
	
	/** Returning Status Description and Status date for the record in resultset
	 */
	void getSTSVL(ResultSet LP_RSSET)
	{
		try
		{
			strSTSDS = ""; strSTSDT = "";
			if(LP_RSSET.getString("PFT_ISSDT") != null && LP_RSSET.getString("PFT_DLHDT") == null && LP_RSSET.getString("PFT_CMSDT") == null && LP_RSSET.getString("PFT_NGTDT") == null && LP_RSSET.getString("PFT_INVDT") == null)
				{strSTSDS = "Issued"; strSTSDT = M_fmtLCDAT.format(LP_RSSET.getDate("PFT_ISSDT"));}
			else if(LP_RSSET.getString("PFT_DLHDT") != null && LP_RSSET.getString("PFT_CMSDT") == null && LP_RSSET.getString("PFT_NGTDT") == null && LP_RSSET.getString("PFT_INVDT") == null)
				{strSTSDS = "At Delhi"; strSTSDT = M_fmtLCDAT.format(LP_RSSET.getDate("PFT_DLHDT"));}
			else if(LP_RSSET.getString("PFT_CMSDT") != null && LP_RSSET.getString("PFT_NGTDT") == null && LP_RSSET.getString("PFT_INVDT") == null)
				{strSTSDS = "At CMS"; strSTSDT = M_fmtLCDAT.format(LP_RSSET.getDate("PFT_CMSDT"));}
			else if(LP_RSSET.getString("PFT_NGTDT") != null && LP_RSSET.getString("PFT_INVDT") == null)
				{strSTSDS = "At Nagothane"; strSTSDT = M_fmtLCDAT.format(LP_RSSET.getDate("PFT_NGTDT"));}
			else if(LP_RSSET.getString("PFT_INVDT") != null)
				{strSTSDS = "Invoiced"; strSTSDT = M_fmtLCDAT.format(LP_RSSET.getDate("PFT_INVDT"));}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTSVL");
		}
	}

	
	void genSUMRPT()
	{
		try
		{
			String L_strSTATS="",L_strTEMP="";
			String L_strFRMTP="",L_strOFRMTP="";
			String L_strPRTCD="",L_strOPRTCD="";
			int L_intFRM=0,L_intTFRM=0;
			float fltNOFRM=0;
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			M_strSQLQRY=" select PFT_FRMTP,PFT_PRTCD,pt_prtnm, count(*) cnt from MR_PFTRN,CO_PTMST  where pft_prttp=pt_prttp and pft_prtcd=pt_prtcd and PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
			M_strSQLQRY+=" and PFT_PRTTP='"+txtPRTTP.getText().trim()+"'";
			if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
		    {
				M_strSQLQRY+=" and CONVERT(varchar,PFT_ISSDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
			} 
			
			if(rdbISS.isSelected())
			{
				M_strSQLQRY+=" and PFT_ISSDT is not null and PFT_DLHDT is null and PFT_CMSDT is null and PFT_NGTDT is null and PFT_INVDT is null ";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_ISSDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbDLH.isSelected())
			{
				M_strSQLQRY+=" and PFT_DLHDT is not null and PFT_CMSDT is null and PFT_NGTDT is null and PFT_INVDT is null ";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_DLHDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbCMS.isSelected())
			{
				M_strSQLQRY+=" and  PFT_CMSDT is not null and PFT_NGTDT is null and PFT_INVDT is null ";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_CMSDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbNGT.isSelected())
			{
				M_strSQLQRY+=" and  PFT_NGTDT is not null and PFT_INVDT is null ";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_NGTDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			else if(rdbINV.isSelected())
			{
				M_strSQLQRY+=" and  PFT_INVDT is not null ";
				if(txtFMDAT.getText().length() > 0 && txtTODAT.getText().length() > 0)
			    {
					M_strSQLQRY+=" and CONVERT(varchar,PFT_INVDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";	
				} 
			}
			 
			M_strSQLQRY+=" group by PFT_FRMTP,PFT_PRTCD,pt_prtnm order by pft_frmtp,pft_prtcd";
			
			//M_strSQLQRY+=" order by PFT_PRTTP"; 
			//System.out.println("data"+M_strSQLQRY);     
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())  
				{ 
					if(M_rstRSSET.getFloat("cnt")>0)
					{	
						fltNOFRM+=M_rstRSSET.getFloat("cnt");
						
					}	
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PFT_FRMTP"),12));	
						//cl_dat.M_intLINNO_pbst ++;
						D_OUT.writeBytes(padSTRING('R',txtPRTTP.getText()+"/"+M_rstRSSET.getString("PFT_PRTCD"),20));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTNM"),55));
						D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("cnt"),10));
						
						crtNWLIN();
				}
				D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Total",15));			
				D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltNOFRM,0),82));
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
			setMSG(L_EX,"genSUMRPT()");
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
			    D_OUT.writeBytes("<HTML><HEAD><Title>Permit Form Status(Despatched Forms)</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,70));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",43));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		D_OUT.writeBytes(padSTRING('R',"Permit Form Status(Despatched Forms)  ",40));
    		if(rdbISS.isSelected())
				D_OUT.writeBytes(padSTRING('R',"Status at Issued",30));
			if(rdbDLH.isSelected())
				D_OUT.writeBytes(padSTRING('R',"Status at Delhi",30));
			if(rdbCMS.isSelected())
				D_OUT.writeBytes(padSTRING('R',"Status at CMS",30));
			if(rdbNGT.isSelected())
				D_OUT.writeBytes(padSTRING('R',"Status at Nagothane",30));
			if(rdbINV.isSelected())
				D_OUT.writeBytes(padSTRING('R',"Status at Invoiced",30));
			if(rdbALL.isSelected())
				D_OUT.writeBytes(padSTRING('R',"Status at All",30));
				
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",43));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		
    		crtNWLIN();
    		//cl_dat.M_intLINNO_pbst+=1;
    		crtNWLIN();
			if(rdbDTLRP.isSelected())
			{
				if(txtPRTCD.getText().length()>0)
				{
					D_OUT.writeBytes(padSTRING('R',"Party Code   : "+txtPRTCD.getText().toString(),48));
					crtNWLIN();
					D_OUT.writeBytes(padSTRING('R',"Party Name   : "+lblPRTNM.getText(),48));
					//crtNWLIN();
				}
				
				if(txtFMDAT.getText().length()>0 && txtTODAT.getText().length()>0)
				{	
					D_OUT.writeBytes(padSTRING('R',"Period From    "+txtFMDAT.getText().toString()+"   To  "+txtTODAT.getText().toString(),48));
					crtNWLIN();
				}
					crtNWLIN();
					crtNWLIN();
					D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------");
					crtNWLIN();
					D_OUT.writeBytes("From Type  Form No.  Party Code  Party Name - Destination                      Inv no  Lorry No     Status       Date      ");
					crtNWLIN();
					D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------");
					crtNWLIN();
					crtNWLIN();
			}
			if(rdbSUMRP.isSelected())
			{
				
				if(txtFMDAT.getText().length()>0 && txtTODAT.getText().length()>0)
				{	
					D_OUT.writeBytes(padSTRING('R',"Period From    "+txtFMDAT.getText().toString()+"   To  "+txtTODAT.getText().toString(),48));
					crtNWLIN();
				}
					crtNWLIN();
					crtNWLIN();
					D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------");
					crtNWLIN();
					D_OUT.writeBytes("Form Type   Party Code          Party Name                                             No of Form                         ");
					crtNWLIN();
					D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------");
					crtNWLIN();
					crtNWLIN();
			}
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
						D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------");
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
			if(txtTODAT.getText().length()==0)
			{
				if(txtFMDAT.getText().length()>0)
				{
					txtFMDAT.setEnabled(true);
					setMSG("Enter To Date",'E');
					txtFMDAT.requestFocus();
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
			     strRPFNM = strRPLOC + "mr_rppfd.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "mr_rppfd.doc";
			if(rdbDTLRP.isSelected())
				genRPTFL();
			if(rdbSUMRP.isSelected())
				genSUMRPT();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
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
			if(input == txtPRTTP)
			{
					
					if(txtPRTTP.getText().length() < 1)
						return true;				
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
				if(txtPRTCD.getText().length()<5 && txtPRTCD.getText().length()>0)
				{	
					setMSG("Enter Valid Party Code",'E');
					return false;
				}
				else if(txtPRTCD.getText().length()!=0)
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
			if(input==txtFRMTP)
			{
				
				if(txtFRMTP.getText().length()!=0)
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
			}	
			if(input == txtFMDAT)
			{
				if(txtFMDAT.getText().length()>0 && M_fmtLCDAT.parse(txtFMDAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From date can not be greater than Today's date..",'E');
					return false;
				}
				//txtTODAT.requestFocus();
			}
			if(input == txtTODAT)
			{
				if(txtTODAT.getText().length()>0 && M_fmtLCDAT.parse(txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("TO date can not be greater than Today's date..",'E');
					return false;
				}
				else if(txtTODAT.getText().length()>0 && M_fmtLCDAT.parse(txtFMDAT.getText()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText()))>0)
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



