/*
System Name   : Finished Goods Inventory Management System
Program Name  : Gradewise Reciept Detail Report
Program Desc. : Gradewise Receipt Detail Report

Author        : Mr S.R.Tawde
Date          : 11.06.2005
Version       : FIMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.DataOutputStream; 
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.sql.ResultSet;
/**<pre>
<b>System Name :</b> Finished Goods Inventory Management System.
 
<b>Program Name :</b> Receipt Summary Report

<b>Purpose :</b> This module gives Gradewise Receipt Detail Report between given dates.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
FG_LWMST    LW_WHRTP,LW_RCTTP,LW_RCTNO,LW_PRDTP,                    #
            LW_LOTNO,LW_RCLNO,LW_STRTM
PR_LTMST    LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
CO_PRMST    PR_PRDCD                                                #
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table Name      Type/Size     Description
--------------------------------------------------------------------------------
txtFRMDT    LW_RCTDT       FG_LWMST        Date          From Date
txtTORDT    LW_RCTDT       FG_LWMST        Date          From Date
txtMNPRD    CMT_CODCD                      Varchar       Main Product Category
txtSHFCD    LW_SHFCD       FG_LWMST        Varchar       Shift Code
txtTREMDS   -------------- Remark to be printed on report ----------------------
rdbMNPSP    --------------Select for specific product type----------------------
rdbMNPAL    ----------------Select for all product type-------------------------
rdbSHFSP    ---------------- Select for specific shift--------------------------
rdbSHFAL    ------------------- Select for all shift----------------------------
--------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description			      Display Columns			Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtMNPRD    Main Product category,            CMT_CODCD,CMT_CODDS       CO_CDTRN
            Description 
txtSHFCD    Shift Code,Description            CMT_CODCD,CMT_CODDS       CO_CDTRN
-----------------------------------------------------------------------------------------------------------------------------------------------------
<I>
<B>Conditions Give in Query:</b>
             Data is taken from FG_LWMST,PR_LTMST,CO_PRMST between the given date range 
             and depending upon the selection of Main Product Category and Sift Code.
             Grouping is done by Receipt Date,Shift Code,Main Product Category(viz.PS,SPS,MB etc.),
             SubProduct Category (viz.GPPS,HIPS,etc.), and Lot No. wise.
             1)RCT_PRDTP = LT_PRDTP.
             2)and RCT_LOTNO = LT_LOTNO.
             3)and RCT_RCLNO=LT_RCLNO.
             4)and LT_PRDCD=PR_PRDCD.
             5)and RCT_RCTTP in ('10','15','21','30'). 
             6)and RCT_STSFL not in ('X')
             7)Group by LW_RCTDT,LW_SHFCD,SUBSTRING(PR_PRDCD,1,2),SUBSTRING(PR_PRDCD,1,4),LW_LOTNO,LW_RCLNO,
			   PR_PRDDS,LT_CYLNO,LW_STRCT,LW_ENDCT,LW_MISCT,LW_PKGTP,LW_MNLCD,LW_UCLTG,LW_REMDS
</I>
Validations :
//'ISOFGXXRPT','MSTCOXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL			 
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
    3) Shift Code validation from CO_CDTRN.('MST','COXXSHF')
    4) Main Product Category.('MST','COXXPGR',CMT_CCSVL='MG')
    5) SubProduct Category.('MST','COXXPGR',CMT_CCSVL='SG') 
 */
public class fg_rprcm extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;		/** JTextField to accept Main Product Category.*/
	private JTextField txtMNPRD;        /** JTextField to accept Shift Code.*/
	private JTextField txtRCTTP;
	private JTextField txtSHFCD;		/** JTextField to accept online Remark just to print on the report.*/
	private JTextField txtTREMDS;		/** Integer to store No.of packages.*/
	private int intRCTPK = 0;
	
	private JRadioButton rdbSHFSP,rdbSHFAL,rdbMNPSP,rdbMNPAL,rdbRECAL,rdbRECSP;
	private ButtonGroup bgrSHFCD,bgrMNPRD,bgrRCTTP;	
	private String strHLPFLD,strISODC1,strISODC2,strISODC3;	
	
	private JLabel lblRCTTP, lblMNPRD,lblSHFCD;
	
											/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;	
												 /** Hashtable for storing different Main Product Types*/
	private Hashtable<String,String[]> hstCDTRN;			// Details of all codes used in program
	private Hashtable<String,String> hstCODDS;			// Code No. from Code Description

	/** Array elements for records picked up from Code Transactoion */
	private int intCDTRN_TOT = 10;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
    private int intAE_CMT_MODLS = 9;
	
	/** Variables for Code Transaction Table
	 */
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	
	//private Hashtable hstMNGRP = new Hashtable();/** Hashtable for storing different Sub Product Types*/
	//private Hashtable hstSBGRP = new Hashtable();/** Hashtable for storing Packge,shift,iso and cylo codes*/
	//private Hashtable hstGENCD = new Hashtable();	                                              
	
	private INPVF objINPVF = new INPVF();
	
	/**
	 *1.Screen Designing
	 *2.Hashtable is created using CO_CDTRN for maintaining various types of codes alongwith
	 *  their descriptions.
	 */
	fg_rprcm()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			bgrMNPRD=new ButtonGroup();			
			add(new JLabel("From Date"),5,2,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,3,1,1,this,'L');			
			add(new JLabel("To Date"),5,4,1,1.5,this,'L');
			add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');
			
			JPanel pnlSHFCD = new JPanel();
			setMatrix(20,8);
			pnlSHFCD.setLayout(null);
			bgrSHFCD=new ButtonGroup();
			add(rdbSHFAL = new JRadioButton("All",true),1,2,1,1,pnlSHFCD,'L');
			add(rdbSHFSP = new JRadioButton("Specific",false),1,3,1,1,pnlSHFCD,'L');
			add(txtSHFCD = new TxtLimit(1),1,4,1,1,pnlSHFCD,'L');
			add(lblSHFCD = new JLabel(),1,5,1,3,pnlSHFCD,'L');
			bgrSHFCD.add(rdbSHFSP);
			bgrSHFCD.add(rdbSHFAL);
			pnlSHFCD.setBorder(BorderFactory.createTitledBorder(null,"Shift",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlSHFCD,6,2,2,6,this,'L');
			
			JPanel pnlMNPRD = new JPanel();
			setMatrix(20,8);
			pnlMNPRD.setLayout(null);
			bgrMNPRD=new ButtonGroup();
			add(rdbMNPAL = new JRadioButton("All",true),1,2,1,1,pnlMNPRD,'L');
			add(rdbMNPSP = new JRadioButton("Specific",false),1,3,1,1,pnlMNPRD,'L');
			add(txtMNPRD = new TxtLimit(2),1,4,1,1,pnlMNPRD,'L');
			add(lblMNPRD = new JLabel(),1,5,1,3,pnlMNPRD,'L');
			bgrMNPRD.add(rdbMNPSP);
			bgrMNPRD.add(rdbMNPAL);
			pnlMNPRD.setBorder(BorderFactory.createTitledBorder(null,"Product Category",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlMNPRD,8,2,2,6,this,'L');
			
			JPanel pnlRCTTP = new JPanel();
			setMatrix(20,8);
			pnlRCTTP.setLayout(null);
			bgrRCTTP=new ButtonGroup();
			add(rdbRECAL = new JRadioButton("All",true),1,2,1,1,pnlRCTTP,'L');
			add(rdbRECSP = new JRadioButton("Specific",false),1,3,1,1,pnlRCTTP,'L');
			add(txtRCTTP = new TxtLimit(2),1,4,1,1,pnlRCTTP,'L');
			add(lblRCTTP = new JLabel(),1,5,1,3,pnlRCTTP,'L');
			bgrRCTTP.add(rdbRECAL);
			bgrRCTTP.add(rdbRECSP);
			pnlRCTTP.setBorder(BorderFactory.createTitledBorder(null,"Receipt Type",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlRCTTP,10,2,2,6,this,'L');
			
					
			add(new JLabel ("Remarks: "),14,2,1,1,this,'L');
			add(txtTREMDS = new TxtLimit(200),14,3,1,5,this,'L');

			/** Registering the components with inputverifier */
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtSHFCD.setInputVerifier(objINPVF);
			txtMNPRD.setInputVerifier(objINPVF);
			txtRCTTP.setInputVerifier(objINPVF);

			hstCDTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			
			hstCDTRN.clear();
			hstCODDS.clear();
			crtCDTRN("'ISOFGXXRPT','M"+cl_dat.M_strCMPCD_pbst+"COXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL'","",hstCDTRN);
			
			//M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			//M_strSQLQRY += " where CMT_CGMTP||CMT_CGSTP in ( 'ISOFGXXRPT','MSTCOXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL')";
			//M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            //M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	

			//if(M_rstRSSET != null)   
			//{
		    //	while(M_rstRSSET.next())
        	//	{   
			//		if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("MG"))
			//			hstMNGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,2),M_rstRSSET.getString("CMT_CODDS"));
			//		else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("SG"))
			//			hstSBGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,4),M_rstRSSET.getString("CMT_CODDS"));
			//		else
			//			hstGENCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        	//	}
        	//	M_rstRSSET.close();
			//}
			strISODC1 = getCDTRN("ISOFGXXRPTFG_RPRCM01","CMT_CODDS",hstCDTRN);
			strISODC2 = getCDTRN("ISOFGXXRPTFG_RPRCM02","CMT_CODDS",hstCDTRN);
			strISODC3 = getCDTRN("ISOFGXXRPTFG_RPRCM03","CMT_CODDS",hstCDTRN);
			M_pnlRPFMT.setVisible(true);			
			setENBL(false);	
			
			txtMNPRD.addActionListener(this);
			txtRCTTP.addActionListener(this);
			txtSHFCD.addActionListener(this);
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
	}			
    /**
     *Super class method overide to enhance its funcationality 
     *For populating FROM DATE AND TO DATE with required default values 
     *We can use this for Enabling or Disabling other components on the screen as per our
     *requirement based on option selected ie. ADDITION,MODIFICATION,DELETION etc.
     * @param L_flgSTAT boolean argument to pass state of the component.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtTODAT.setEnabled(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);		
		txtSHFCD.setEnabled(false);
		txtMNPRD.setEnabled(false);
		txtRCTTP.setEnabled(false);
		if (L_flgSTAT==true )
		{
			if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
			{					 
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));																					
       				txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));				            														
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" setENBL",'E');
				}
			}
			else 
			{
				if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				{
					txtTODAT.setText("");
					txtFMDAT.setText("");			
				}
			}
			if(rdbSHFSP.isSelected())
			{
				txtSHFCD.setEnabled(L_flgSTAT);
			} 
			if(rdbMNPSP.isSelected())
			{
				txtMNPRD.setEnabled(L_flgSTAT);
			}
			if(rdbRECSP.isSelected())
			{
				txtRCTTP.setEnabled(L_flgSTAT);
			}
			
		}
	}		

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
		
	  try
	  {
		if(M_objSOURC == rdbSHFSP)
			txtSHFCD.setEnabled(true);
		else if(M_objSOURC == rdbSHFAL)
		{	
			txtSHFCD.setText("");
			txtSHFCD.setEnabled(false);
		}	
		if(M_objSOURC == rdbMNPSP)
			txtMNPRD.setEnabled(true);
		else if(M_objSOURC == rdbMNPAL)
		{	
			txtMNPRD.setText("");
			txtMNPRD.setEnabled(false);
		}
		
		if(M_objSOURC == rdbRECSP)
			txtRCTTP.setEnabled(true);
		else if(M_objSOURC == rdbRECAL)
		{	
			txtRCTTP.setText("");
			txtRCTTP.setEnabled(false);	
		}
		if(M_objSOURC == txtRCTTP)
		{
			if(txtRCTTP.getText().length() == 0)
			{
			  lblRCTTP.setText("");
			}
		}
		
		if(M_objSOURC == txtMNPRD)
		{
			if(txtMNPRD.getText().length() == 0)
			{
			  lblMNPRD.setText("");
			}
		}
		
		if(M_objSOURC == txtSHFCD)
		{
			if(txtSHFCD.getText().length() == 0)
			{
			  lblSHFCD.setText("");
			}
		}
			
		if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(M_cmbDESTN.getItemCount() > 1)
				{
					M_cmbDESTN.setEnabled (true);
				}
				else if(M_cmbDESTN.getItemCount() == 1)
				{
					setMSG("No Printer Attached to the System ..",'E');
				}
			}
		}
	  }	
		catch(Exception L_EX)
		  {
			setMSG(L_EX,"actionPerformed");
		  }
		   setMSG("",'N');
	}	 
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)			
				txtTODAT.requestFocus();
			else if(M_objSOURC == txtTODAT)			
				txtSHFCD.requestFocus();
			if(M_objSOURC == txtSHFCD)
				txtMNPRD.requestFocus();
			if(M_objSOURC == txtMNPRD)
				txtRCTTP.requestFocus();
			if(M_objSOURC == txtRCTTP)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		
		/**Method to display list of Shift Codes and Main Product Codes in help window 
		when F1 is Pressed.*/
		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtSHFCD)
				{
					M_strHLPFLD = "txtSHFCD";
					M_strSQLQRY =  "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='M"+cl_dat.M_strCMPCD_pbst+"' ";
					M_strSQLQRY+=  "and CMT_CGSTP='COXXSHF'";
					M_strSQLQRY+=  "and isnull(CMT_STSFL,'')<> 'X'";
					if(txtSHFCD.getText().trim().length() > 0)
						M_strSQLQRY += " AND CMT_CODCD LIKE '"+txtSHFCD.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Shift Code","Shift Description"},2,"CT");
				}
				if(M_objSOURC == txtMNPRD)
				{
					txtMNPRD.setText("");
					M_strHLPFLD  = "txtMNPRD";
					M_strSQLQRY  = "select left(CMT_CODCD,2),CMT_CODDS from CO_CDTRN WHERE ";
					M_strSQLQRY+=  "CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and CMT_CCSVL='MG' and ";
					M_strSQLQRY+=  "isnull(CMT_STSFL,'') <> 'X'";
					if(txtMNPRD.getText().trim().length() > 0)
						M_strSQLQRY += " AND CMT_CODCD LIKE '"+txtMNPRD.getText().trim()+"%'";
				
					System.out.println(">>>>MNPRD>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Product Category","Product Description"},2,"CT");
					
					
				}
				
				if(M_objSOURC == txtRCTTP)
				{
					txtRCTTP.setText("");
					M_strHLPFLD  = "txtRCTTP";
					M_strSQLQRY  = "select cmt_codcd,cmt_codds from co_cdtrn where ";
					M_strSQLQRY+=  "cmt_cgstp='FGXXRTP'  and cmt_codcd in ('10','15','21','22','30') order by cmt_codcd ";
					System.out.println(">>>>RCTTP>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Receipt Type","Receipt Description"},2,"CT");
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
				
				
			}
			catch(Exception L_E)
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"Key Pressed F1");
			}
		}
	}		

		public void exeHLPOK()
		{
		super.exeHLPOK();
			if(M_strHLPFLD == "txtSHFCD")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtSHFCD.setText(L_STRTKN.nextToken());
				lblSHFCD.setText(L_STRTKN.nextToken());
				txtMNPRD.requestFocus();
			}
			if(M_strHLPFLD == "txtMNPRD")
			{
			   StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			   txtMNPRD.setText(L_STRTKN.nextToken());
			   lblMNPRD.setText(L_STRTKN.nextToken());
			   txtRCTTP.requestFocus();
			}
			if(M_strHLPFLD == "txtRCTTP")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtRCTTP.setText(L_STRTKN.nextToken());
				lblRCTTP.setText(L_STRTKN.nextToken());
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	
	/**
	*Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
							
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rprcm.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rprcm.doc";				
			getDATA();				
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(intRECCT >0)
				    if (M_rdbTEXT.isSelected())
				        doPRINT(strFILNM);
				    else 
                    {    
		                Runtime r = Runtime.getRuntime();
					    Process p = null;								
				        p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
				        setMSG("For Printing Select File Menu, then Print  ..",'N');
				    }    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
				 if(intRECCT >0)
				 {						
				     if(M_rdbHTML.isSelected())
				        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
				     else
    				    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
	    		  }
 				  else
					  setMSG("No data found, Please check Date Range OR Product Category OR Shift Code..",'E');				    
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
		    {
			    cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Gradewise Receipt Detail Report"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	/**
	*Method to validate data in the input components on the screen, before execution like
    *validation of Dates, availability of the printers etc.
    *Check for blank input
	*/
	boolean vldDATA()
	{
		try
		{	
			if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Specify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter valid To-Date To Specify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}					
			else if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtTODAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" vldDATA",'E');
				}	
			}	
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("Please Note that From-Date must be Greater than To-Date .. ",'E');								
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtFMDAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX + " vldData", 'E');
				}																
			}							
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{				
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{				
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}			
			return true;
		}
		catch(Exception L_EX)
		{
			return false;
		}
	}
	/**
	*Method to fetch Data from Database and start creation of the output file.
	*/
	private void getDATA()
	{ 	    		
		String L_strFMDAT,L_strTODAT,L_strPRDCT="",L_strSHFCT="";
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblBAGQT = 0;
		int L_intBAGPK = 0;
		
		/** Variables declared for storing receipt quantity and packages at different levels
		 */
		double L_dblDTRQT = 0;
		int L_intDTPQT    = 0;
		double L_dblSHRQT = 0;
		int L_intSHPQT    = 0;
		double L_dblMNRQT = 0;
		int L_intMNPQT    = 0;
		double L_dblSBRQT = 0;
		int L_intSBPQT    = 0;
		double L_dblGTRQT = 0;
		int L_intGTPQT    = 0;
		
		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPRCTDT = new String();
		String L_strPSHFCD = new String();
		String L_strPMNPRD = new String();
		String L_strPSBPRD = new String();
		String L_strPLOTNO = new String();
		String L_strPRCLNO = new String();
		String L_strPPRDDS = new String();
		String L_strPPKGTP = new String();
		String L_strPCYLNO = new String();
		String L_strPSTRCT = new String();
		String L_strPENDCT = new String();
		String L_strPMISCT = new String();
		String L_strPMNLCD = new String();
		String L_strPREMDS = new String();
		String L_strPUCLTG = new String();

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCRCTDT = new String();
		String L_strCSHFCD = new String();
		String L_strCMNPRD = new String();
		String L_strCSBPRD = new String();
		String L_strCLOTNO = new String();
		String L_strCRCLNO = new String();
		String L_strCPRDDS = new String();
		String L_strCPKGTP = new String();
		String L_strCCYLNO = new String();
		String L_strCSTRCT = new String();
		String L_strCENDCT = new String();
		String L_strCMISCT = new String();
		String L_strCMNLCD = new String();
		String L_strCREMDS = new String();
		String L_strCUCLTG = new String();

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strRCTDT = new String();
		String L_strSHFCD = new String();
		String L_strMNPRD = new String();
		String L_strSBPRD = new String();
		String L_strLOTNO = new String();
		String L_strRCLNO = new String();
		String L_strPRDDS = new String();
		String L_strPKGTP = new String();
		String L_strCYLNO = new String();
		String L_strSTRCT = new String();
		String L_strENDCT = new String();
		String L_strMISCT = new String();
		String L_strMNLCD = new String();
		String L_strREMDS = new String();
		String L_strUCLTG = new String();

		/** Variables declared for storing current and previous code description
	 */	
		String L_strSHFDS="",L_strMPRDS="",L_strSPRDS="",L_strPKGDS="",L_strCYLDS="";
		String L_strPSHFDS="",L_strPMPRDS="",L_strPSPRDS="";
			
		try
	    {	
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				//prnFMTCHR(dosREPORT,M_strCPI12);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Gradewise Receipt Detail Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 7 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();			
    		
			L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			setMSG("Report Generation is in Progress.......",'N');			   			
			String L_DTBSTR1 = "RCT_LOTNO,RCT_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,4),LT_CYLNO,0,0,RCT_RCTDT,";
			L_DTBSTR1  += " RCT_SHFCD ,0,RCT_PKGTP,RCT_MNLCD,'--','--'";
			
			String L_DTBSTR2 = " LW_RCTDT,LW_SHFCD,L_SBPRD,LW_LOTNO,LW_RCLNO,PR_PRDDS,LT_CYLNO,LW_STRCT,LW_ENDCT,";
			L_DTBSTR2 += " LW_MISCT,LW_PKGTP,LW_MNLCD,LW_REMDS,LW_UCLTG";
						
			M_strSQLQRY = " Select LW_LOTNO,LW_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,2) L_MNPRD,SUBSTRING(PR_PRDCD,1,4) L_SBPRD,LT_CYLNO,LW_STRCT,LW_ENDCT,LW_RCTDT,";
			M_strSQLQRY+= " LW_SHFCD,LW_MISCT,LW_PKGTP,LW_MNLCD,LW_REMDS,LW_UCLTG,sum(LW_BAGQT) L_dblBAGQT,sum(LW_BAGPK) L_intBAGPK";
			M_strSQLQRY+= " from FG_LWMST,PR_LTMST,CO_PRMST where LW_CMPCD = LT_CMPCD and LW_PRDTP=LT_PRDTP and LW_LOTNO=LT_LOTNO";
			M_strSQLQRY+= " and LW_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD and LW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LW_RCTDT between '"+L_strFMDAT+"'";
			M_strSQLQRY+= " and '"+L_strTODAT+"' and LW_STSFL not in ('X') "+ (txtRCTTP.getText().trim().length()==2 ? " and LW_RCTTP = '"+txtRCTTP.getText()+"'" : "");
			if(rdbMNPSP.isSelected())
				M_strSQLQRY+=  " and left(PR_PRDCD,2)='"+txtMNPRD.getText()+"' ";
			if(rdbSHFSP.isSelected())
				M_strSQLQRY+=  " and LW_SHFCD='"+txtSHFCD.getText()+"' ";
			M_strSQLQRY+= " group by LW_RCTDT,LW_SHFCD,SUBSTRING(PR_PRDCD,1,2),SUBSTRING(PR_PRDCD,1,4),LW_LOTNO,LW_RCLNO,";
			M_strSQLQRY+= "	PR_PRDDS,LT_CYLNO,LW_STRCT,LW_ENDCT,LW_MISCT,LW_PKGTP,LW_MNLCD,LW_UCLTG,LW_REMDS";
 			M_strSQLQRY+= " union Select RCT_LOTNO LW_LOTNO,RCT_RCLNO LW_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,2) L_MNPRD,SUBSTRING(PR_PRDCD,1,4) L_SBPRD,LT_CYLNO,0 LW_STRCT,0 LW_ENDCT,RCT_RCTDT LW_RCTDT,";
			M_strSQLQRY+= " RCT_SHFCD LW_SHFCD,0 LW_MISCT,RCT_PKGTP LW_PKGTP,RCT_MNLCD LW_MNLCD,'--' LW_REMDS,'--' LW_UCLTG,sum(RCT_RCTQT) L_BAGQT,sum(RCT_RCTPK) L_BAGPK";
			M_strSQLQRY+= " from FG_RCTRN,PR_LTMST,CO_PRMST where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTTP in ('21','22','23','30') and RCT_CMPCD=LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO";
			M_strSQLQRY+= " and RCT_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD "+(txtRCTTP.getText().trim().length()==2 ? " and RCT_RCTTP = '"+txtRCTTP.getText()+"'" : "")+" and RCT_RCTDT between '"+L_strFMDAT+"'";
			M_strSQLQRY+= " and '"+L_strTODAT+"' and RCT_STSFL not in ('X') ";
			if(rdbMNPSP.isSelected())
				M_strSQLQRY+=  " and left(PR_PRDCD,2)='"+txtMNPRD.getText()+"' ";
			if(rdbSHFSP.isSelected())
				M_strSQLQRY+=  " and RCT_SHFCD='"+txtSHFCD.getText()+"' ";
			M_strSQLQRY+= " group by RCT_LOTNO,RCT_RCLNO ,PR_PRDDS,SUBSTRING(PR_PRDCD,1,2),SUBSTRING(PR_PRDCD,1,4) ,LT_CYLNO,RCT_RCTDT, RCT_SHFCD ,RCT_PKGTP ,RCT_MNLCD";
			M_strSQLQRY+= " order by LW_RCTDT,LW_SHFCD,L_MNPRD,L_SBPRD,LW_LOTNO,LW_RCLNO,PR_PRDDS,LT_CYLNO,LW_STRCT,LW_ENDCT,LW_MISCT,LW_PKGTP,LW_MNLCD,LW_UCLTG,LW_REMDS";
            System.out.println(M_strSQLQRY);
				
			/*M_strSQLQRY += " union Select RCT_LOTNO LW_LOTNO,RCT_RCLNO LW_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,4) L_SBPRD,LT_CYLNO,0 LW_STRCT,0 LW_ENDCT,RCT_RCTDT LW_RCTDT,";
			M_strSQLQRY += " RCT_SHFCD LW_SHFCD,0 LW_MISCT,RCT_PKGTP LW_PKGTP,RCT_MNLCD LW_MNLCD,'--' LW_REMDS,'--' LW_UCLTG,sum(RCT_RCTQT) L_BAGQT,sum(RCT_RCTPK) L_BAGPK";
			M_strSQLQRY += " from FG_RCTRN,PR_LTMST,CO_PRMST where RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO";
			M_strSQLQRY += " and RCT_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD and RCT_RCTDT between "+L_strFMDAT;
			M_strSQLQRY += " and "+L_strTODAT+" and RCT_STSFL not in ('X') and RCT_RCTTP in ('21','22','23') ";
			if(rdbMNPSP.isSelected())
				M_strSQLQRY+=  " and left(PR_PRDCD,2)='"+txtMNPRD.getText()+"' ";
			if(rdbSHFSP.isSelected())
				M_strSQLQRY+=  " and RCT_SHFCD='"+txtSHFCD.getText()+"' ";
			//M_strSQLQRY += LM_PRDSTR1;
			//M_strSQLQRY += LM_SHTSTR1;
			M_strSQLQRY += " group by " + L_DTBSTR1;
			M_strSQLQRY += " order by " + L_DTBSTR2;
		/*	
			if(rdbPRDSP.isSelected())
			{
				LM_PRDSTR = " and LW_PRDTP='"+LM_PRDTP+"' "; 
			    LM_PRDSTR1 = " and RCT_PRDTP='"+LM_PRDTP+"' ";
			}
			if(rdbSHFSP.isSelected())
			{
				LM_SHTSTR = " and LW_SHFCD='"+LM_SHIFT+"' ";
			    LM_SHTSTR1 = " and RCT_SHFCD='"+LM_SHIFT+"' ";
			}
			//String L_DTBSTR = " LW_RCTDT,LW_SHFCD,SUBSTRING(PR_PRDCD,1,4),LW_LOTNO,LW_RCLNO,PR_PRDDS,LT_CYLNO,LW_STRCT,";
			//L_DTBSTR += "LW_ENDCT,LW_MISCT,LW_PKGTP,LW_MNLCD,LW_UCLTG,LW_REMDS ";

			String L_DTBSTR = "LW_LOTNO,LW_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,4),LT_CYLNO,LW_STRCT,LW_ENDCT,LW_RCTDT,";
			L_DTBSTR += "LW_SHFCD,LW_MISCT,LW_PKGTP,LW_MNLCD,LW_REMDS,LW_UCLTG";		
		
			String L_DTBSTR1 = "RCT_LOTNO,RCT_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,4),LT_CYLNO,0,0,RCT_RCTDT,";
			L_DTBSTR1  += " RCT_SHFCD ,0,RCT_PKGTP,RCT_MNLCD,'--','--'";
			
			String L_DTBSTR2 = " LW_RCTDT,LW_SHFCD,L_SBPRD,LW_LOTNO,LW_RCLNO,PR_PRDDS,LT_CYLNO,LW_STRCT,LW_ENDCT,";
			L_DTBSTR2 += " LW_MISCT,LW_PKGTP,LW_MNLCD,LW_REMDS,LW_UCLTG";
			
			LM_STRSQL = "Select LW_LOTNO,LW_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,4) L_SBPRD,LT_CYLNO,LW_STRCT,LW_ENDCT,LW_RCTDT,";
			LM_STRSQL += "LW_SHFCD,LW_MISCT,LW_PKGTP,LW_MNLCD,LW_REMDS,LW_UCLTG,sum(LW_BAGQT) L_BAGQT,sum(LW_BAGPK) L_BAGPK";
			LM_STRSQL += " from FG_LWMST,PR_LTMST,CO_PRMST where LW_PRDTP=LT_PRDTP and LW_LOTNO=LT_LOTNO";
			LM_STRSQL += " and LW_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD and LW_RCTDT between "+cc_dattm.occ_dattm.setDBSDT(LM_FRMDT);
			LM_STRSQL += " and "+cc_dattm.occ_dattm.setDBSDT(LM_TODAT)+" and LW_STSFL not in ('X') ";
			LM_STRSQL += LM_PRDSTR;
			LM_STRSQL += LM_SHTSTR;
			LM_STRSQL += " group by " + L_DTBSTR;
			//LM_STRSQL += " order by " + L_DTBSTR;
			LM_STRSQL += " union Select RCT_LOTNO LW_LOTNO,RCT_RCLNO LW_RCLNO,PR_PRDDS,SUBSTRING(PR_PRDCD,1,4) L_SBPRD,LT_CYLNO,0 LW_STRCT,0 LW_ENDCT,RCT_RCTDT LW_RCTDT,";
			LM_STRSQL += " RCT_SHFCD LW_SHFCD,0 LW_MISCT,RCT_PKGTP LW_PKGTP,RCT_MNLCD LW_MNLCD,'--' LW_REMDS,'--' LW_UCLTG,sum(RCT_RCTQT) L_BAGQT,sum(RCT_RCTPK) L_BAGPK";
			LM_STRSQL += " from FG_RCTRN,PR_LTMST,CO_PRMST where RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO";
			LM_STRSQL += " and RCT_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD and RCT_RCTDT between "+cc_dattm.occ_dattm.setDBSDT(LM_FRMDT);
			LM_STRSQL += " and "+cc_dattm.occ_dattm.setDBSDT(LM_TODAT)+" and RCT_STSFL not in ('X') and RCT_RCTTP in ('21','22','23') ";
			LM_STRSQL += LM_PRDSTR1;
			LM_STRSQL += LM_SHTSTR1;
			LM_STRSQL += " group by " + L_DTBSTR1;
			LM_STRSQL += " order by " + L_DTBSTR2;
		*/	
			//System.out.println(" Query "+M_strSQLQRY);
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);

			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					while(!L_flgEOF)
					{
						System.out.println("Lot No: "+nvlSTRVL(M_rstRSSET.getString("LW_LOTNO"),""));	
						L_strRCTDT = nvlSTRVL(M_rstRSSET.getString("LW_RCTDT"),"");
						System.out.println("001");
						L_strSHFCD = nvlSTRVL(M_rstRSSET.getString("LW_SHFCD"),"");
						L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("LW_LOTNO"),"");
						System.out.println("002");
						L_strMNPRD = nvlSTRVL(M_rstRSSET.getString("L_MNPRD"),"");
						L_strSBPRD = nvlSTRVL(M_rstRSSET.getString("L_SBPRD"),"");
						System.out.println("003");
						L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
						L_strPKGTP = nvlSTRVL(M_rstRSSET.getString("LW_PKGTP"),"");
						System.out.println("004");
						L_strCYLNO = nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),"");
						L_strSTRCT = nvlSTRVL(M_rstRSSET.getString("LW_STRCT"),"");
						System.out.println("005");
						L_strENDCT = nvlSTRVL(M_rstRSSET.getString("LW_ENDCT"),"");
						L_strMISCT = nvlSTRVL(M_rstRSSET.getString("LW_MISCT"),"");
						System.out.println("006");
						L_strMNLCD = nvlSTRVL(M_rstRSSET.getString("LW_MNLCD"),"");
						L_strREMDS = nvlSTRVL(M_rstRSSET.getString("LW_REMDS"),"");
						System.out.println("007");
						L_strUCLTG = nvlSTRVL(M_rstRSSET.getString("LW_UCLTG"),"");
    					L_dblBAGQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_dblBAGQT"),"0"));
						System.out.println("008");
    					L_intBAGPK = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("L_intBAGPK"),"0"));
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
						}
						if(L_flg1STSFL)
						{
							System.out.println("IN FLAG");
							L_strPRCTDT = L_strRCTDT;
							System.out.println("009");
							L_strPMNPRD = L_strMNPRD;
							L_strMPRDS  = getCDTRN("MSTCOXXPGR"+L_strMNPRD+"0000000A","CMT_CODDS",hstCDTRN);  //hstMNGRP.get(L_strMNPRD).toString();
							System.out.println("010");
							L_strPMPRDS = L_strMPRDS;
							L_strPSBPRD = L_strSBPRD;
							System.out.println("011");
							L_strSPRDS  = getCDTRN("MSTCOXXPGR"+L_strSBPRD+"00000A","CMT_CODDS",hstCDTRN); //hstSBGRP.get(L_strSBPRD).toString();
							L_strPSPRDS = L_strSPRDS;
							System.out.println("012");
							L_strPSHFCD = L_strSHFCD;
							L_strSHFDS  = getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHFCD,"CMT_CODDS",hstCDTRN);  //hstGENCD.get(L_strSHFCD).toString();
							System.out.println("013");
							L_strPSHFDS = L_strSHFDS;
							L_strPPRDDS = L_strPRDDS;
							System.out.println("014");
							L_strPPKGTP = L_strPKGTP;
							L_strPKGDS  = getCDTRN("SYSFGXXPKG"+L_strPKGTP,"CMT_CODDS",hstCDTRN);  //hstGENCD.get(L_strPKGTP).toString();
							System.out.println("015");
							L_strPLOTNO = L_strLOTNO;
							L_strPRCLNO = L_strRCLNO;
							System.out.println("016");
							L_strPCYLNO = L_strCYLNO;
							L_strCYLDS  = getCDTRN("SYSPRXXCYL"+L_strCYLNO,"CMT_CODDS",hstCDTRN);  //hstGENCD.get(L_strCYLNO).toString();
							System.out.println("017");
							L_strPSTRCT = L_strSTRCT;
							L_strPENDCT = L_strENDCT;
							System.out.println("018");
							L_strPMISCT = L_strMISCT;
							L_strPMNLCD = L_strMNLCD;
							System.out.println("019");
							L_strPUCLTG = L_strUCLTG;
							L_strPREMDS = L_strREMDS;
							System.out.println("020");
							
							L_strCRCTDT = L_strRCTDT;
							System.out.println("021");
							L_strCSHFCD = L_strSHFCD;
							L_strCMNPRD = L_strMNPRD;
							System.out.println("022");
							L_strCSBPRD = L_strSBPRD;
							L_strCPRDDS = L_strPRDDS;
							System.out.println("023");
							L_strCPKGTP = L_strPKGTP;
							L_strCRCLNO = L_strRCLNO;
							System.out.println("024");
							L_strCCYLNO = L_strCYLNO; 
							L_strCSTRCT = L_strSTRCT;
							System.out.println("025");
							L_strCENDCT = L_strENDCT;
							L_strCMISCT = L_strMISCT;
							System.out.println("026");
							L_strCMNLCD = L_strMNLCD;
							L_strCREMDS = L_strREMDS;
							System.out.println("027");
							L_strCUCLTG = L_strUCLTG;
							L_strCLOTNO = L_strLOTNO;
						
							L_flg1STSFL = false;
						}
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes(L_strRCTDT+"\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
   						cl_dat.M_intLINNO_pbst+= 1;
					    System.out.println("In first While");
						while(!L_flgEOF && L_strRCTDT.equals(L_strPRCTDT))
						{
							if(cl_dat.M_intLINNO_pbst >= 64)
							{	
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes(padSTRING('L',"",4));
							dosREPORT.writeBytes(L_strSHFDS+"\n");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
   							cl_dat.M_intLINNO_pbst+= 1;
							L_strPSHFCD = L_strSHFCD;
							System.out.println("Second While");
							while(!L_flgEOF && (L_strRCTDT+L_strSHFCD).equals(L_strPRCTDT+L_strPSHFCD))					
							{
								if(cl_dat.M_intLINNO_pbst >= 64)
								{	
									dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
									prnHEADER();
								}
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");
								dosREPORT.writeBytes(padSTRING('L',"",8));
								dosREPORT.writeBytes(L_strMPRDS+"\n");
	   							cl_dat.M_intLINNO_pbst+= 1;
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");
								L_strSHFCD = L_strCSHFCD;
								L_strPSHFCD = L_strSHFCD;
								while(!L_flgEOF && (L_strRCTDT+L_strSHFCD+L_strMNPRD).equals(L_strPRCTDT+L_strPSHFCD+L_strPMNPRD))
								{
									if(cl_dat.M_intLINNO_pbst >= 64)
									{	
										dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
										if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
											prnFMTCHR(dosREPORT,M_strEJT);
										if(M_rdbHTML.isSelected())
											dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
										prnHEADER();
									}
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strBOLD);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<B>");
									dosREPORT.writeBytes(padSTRING('L',"",12));
									dosREPORT.writeBytes(L_strSPRDS+"\n");
		   							cl_dat.M_intLINNO_pbst+= 1;
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("</B>");
									L_strSHFCD  = L_strCSHFCD;
									L_strMNPRD  = L_strCMNPRD;
									L_strPSHFCD = L_strSHFCD;
									L_strPMNPRD = L_strMNPRD;
									//System.out.println("Third While");
									while(!L_flgEOF && (L_strRCTDT+L_strSHFCD+L_strMNPRD+L_strSBPRD).equals(L_strPRCTDT+L_strPSHFCD+L_strPMNPRD+L_strPSBPRD))
									{
										L_strSHFCD  = L_strCSHFCD;
										L_strMNPRD  = L_strCMNPRD;
										L_strSBPRD  = L_strCSBPRD;
										L_strPSHFCD = L_strSHFCD;
										L_strPMNPRD = L_strMNPRD;
										L_strPSBPRD = L_strSBPRD;
										while(!L_flgEOF && (L_strRCTDT+L_strSHFCD+L_strMNPRD+L_strSBPRD+L_strLOTNO+L_strRCLNO+L_strPRDDS+L_strCYLNO+L_strSTRCT+L_strENDCT+L_strMISCT+L_strMNLCD+L_strUCLTG+L_strREMDS).equals(L_strPRCTDT+L_strPSHFCD+L_strPMNPRD+L_strPSBPRD+L_strPLOTNO+L_strPRCLNO+L_strPPRDDS+L_strPCYLNO+L_strPSTRCT+L_strPENDCT+L_strPMISCT+L_strPMNLCD+L_strPUCLTG+L_strPREMDS))
										{
											intRECCT++;								
											dosREPORT.writeBytes(padSTRING('L',"",16));	
											dosREPORT.writeBytes(padSTRING('R',L_strLOTNO,10));
											dosREPORT.writeBytes(padSTRING('R',L_strPRDDS,15));
											dosREPORT.writeBytes(padSTRING('R',L_strCYLDS,8));
											dosREPORT.writeBytes(padSTRING('R',L_strSTRCT,8));
											dosREPORT.writeBytes(padSTRING('R',L_strENDCT,8));
											dosREPORT.writeBytes(padSTRING('R',L_strMISCT,8));
											dosREPORT.writeBytes(padSTRING('R',L_strPKGDS,7));
											dosREPORT.writeBytes(padSTRING('L',"",2));
											dosREPORT.writeBytes(padSTRING('R',L_strMNLCD,8));
											dosREPORT.writeBytes(padSTRING('R',L_strUCLTG,8));
											dosREPORT.writeBytes(padSTRING('R',L_strREMDS,15));
				    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBAGQT,3),14));
											dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intBAGPK,0),10));
											dosREPORT.writeBytes("\n");
						   					cl_dat.M_intLINNO_pbst+= 1;
											if(cl_dat.M_intLINNO_pbst >= 64)
											{	
												dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
												if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
													prnFMTCHR(dosREPORT,M_strEJT);
												if(M_rdbHTML.isSelected())
													dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
												prnHEADER();
											}
											L_dblDTRQT+=  L_dblBAGQT;
											L_intDTPQT+=  L_intBAGPK;
											L_dblSHRQT+=  L_dblBAGQT;
											L_intSHPQT+=  L_intBAGPK;
											L_dblMNRQT+=  L_dblBAGQT;
											L_intMNPQT+=  L_intBAGPK;
											L_dblSBRQT+=  L_dblBAGQT;
											L_intSBPQT+=  L_intBAGPK;
											L_dblGTRQT+=  L_dblBAGQT;
											L_intGTPQT+=  L_intBAGPK;
											if(!M_rstRSSET.next())
											{
												L_flgEOF = true;
												break;
											}
								            L_strCRCTDT = nvlSTRVL(M_rstRSSET.getString("LW_RCTDT"),"");
											L_strCSHFCD = nvlSTRVL(M_rstRSSET.getString("LW_SHFCD"),"");
											//System.out.println("SHIFT"+L_strCSHFCD);
											//if(hstGENCD.containsKey(L_strCSHFCD))
												L_strSHFDS  = getCDTRN("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strCSHFCD,"CMT_CODDS",hstCDTRN);  //hstGENCD.get(L_strCSHFCD).toString();
											//else
											//{
											//	L_strSHFDS=L_strCSHFCD;
												//System.out.println("L_strCSHFCD "+L_strCSHFCD);
											//}
										
											L_strCMNPRD = nvlSTRVL(M_rstRSSET.getString("L_MNPRD"),"");
											//if(hstMNGRP.containsKey(L_strCMNPRD))
												L_strMPRDS  = getCDTRN("MSTCOXXPGR"+L_strCMNPRD+"0000000A","CMT_CODDS",hstCDTRN); //hstMNGRP.get(L_strCMNPRD).toString();
											//else
											//{
											//	L_strMPRDS = L_strCMNPRD;
												//System.out.println("L_strCMNPRD "+L_strCMNPRD);
											
											//}
									
											L_strCSBPRD = nvlSTRVL(M_rstRSSET.getString("L_SBPRD"),"");
											//if(hstSBGRP.containsKey(L_strCSBPRD))
												L_strSPRDS  = getCDTRN("MSTCOXXPGR"+L_strCSBPRD+"00000A","CMT_CODDS",hstCDTRN);  //hstSBGRP.get(L_strCSBPRD).toString();
											//else
											//{
											//	L_strSPRDS=L_strCSBPRD;
												//System.out.println("L_strCSBPRD "+L_strCSBPRD);
											
											//}
									
											L_strCLOTNO = nvlSTRVL(M_rstRSSET.getString("LW_LOTNO"),"");
											L_strCPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
											L_strCPKGTP = nvlSTRVL(M_rstRSSET.getString("LW_PKGTP"),"");
									
											//if(hstGENCD.containsKey(L_strCPKGTP))
												L_strPKGDS = getCDTRN("SYSFGXXPKG"+L_strCPKGTP,"CMT_CODDS",hstCDTRN);  //hstGENCD.get(L_strCPKGTP).toString();
											//else
											//{
											//	L_strPKGDS=L_strCPKGTP;
												//System.out.println("L_strCPKGTP "+L_strCPKGTP);
											//}
									
											L_strCCYLNO = nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),"");
											//if(hstGENCD.containsKey(L_strCCYLNO))
												L_strCYLDS = getCDTRN("SYSPRXXCYL"+L_strCCYLNO,"CMT_CODDS",hstCDTRN); //hstGENCD.get(L_strCCYLNO).toString();
											//else
											//{
											//	L_strCYLDS =L_strCCYLNO;
												//System.out.println("L_strCCYLNO "+L_strCCYLNO);
											//}
									
											L_strCSTRCT = nvlSTRVL(M_rstRSSET.getString("LW_STRCT"),"");
											L_strCENDCT = nvlSTRVL(M_rstRSSET.getString("LW_ENDCT"),"");
											L_strCMISCT = nvlSTRVL(M_rstRSSET.getString("LW_MISCT"),"");
											L_strCMNLCD = nvlSTRVL(M_rstRSSET.getString("LW_MNLCD"),"");
											L_strCREMDS = nvlSTRVL(M_rstRSSET.getString("LW_REMDS"),"");
											L_strCUCLTG = nvlSTRVL(M_rstRSSET.getString("LW_UCLTG"),"");
    										L_dblBAGQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_dblBAGQT"),"0"));
    										L_intBAGPK = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("L_intBAGPK"),"0"));
									
											L_strRCTDT = L_strCRCTDT;
											L_strSHFCD = L_strCSHFCD;
											L_strMNPRD = L_strCMNPRD;
											L_strSBPRD = L_strCSBPRD;
											L_strLOTNO = L_strCLOTNO;
											L_strRCLNO = L_strCRCLNO;
											L_strPRDDS = L_strCPRDDS;
											L_strRCLNO = L_strCRCLNO;
											L_strCYLNO = L_strCCYLNO;
											L_strSTRCT = L_strCSTRCT;
											L_strENDCT = L_strCENDCT;
											L_strMISCT = L_strCMISCT;
											L_strPKGTP = L_strCPKGTP;
											L_strMNLCD = L_strCMNLCD;
											L_strREMDS = L_strCREMDS;
											L_strUCLTG = L_strCUCLTG;
										}//	while((L_strRCTDT+L_strSHFCD+L_strMNPRD+L_strSBPRD+LM_LOTNO+LM_RCLNO+LM_PRDDS+LM_CYLNO+LM_STRCT+LM_ENDCT+LM_MISCT+LM_MNLCD+LM_UCLTG+LM_REMDS).equals(LM_RCTDT1+LM_SHFCD1+LM_SBGRP1+LM_LOTNO1+LM_RCLNO1+LM_PRDDS1+LM_CYLNO1+LM_STRCT1+LM_ENDCT1+LM_MISCT1+LM_MNLCD1+LM_UCLTG1+LM_REMDS1) && !L_EOF)
										L_strPLOTNO = L_strLOTNO;
										L_strPRCLNO = L_strRCLNO;
										L_strPPRDDS = L_strPRDDS;
										L_strPCYLNO = L_strCYLNO;
										L_strPSTRCT = L_strSTRCT;
										L_strPENDCT = L_strENDCT;
										L_strPMISCT = L_strMISCT;
										L_strPMNLCD = L_strMNLCD;
										L_strPUCLTG = L_strUCLTG;
										L_strPREMDS = L_strREMDS;
									}//while((L_strRCTDT+L_strSHFCD+L_strMNPRD+L_strSBPRD)	
									dosREPORT.writeBytes(padSTRING('L',"",12));
									if(cl_dat.M_intLINNO_pbst >= 64)
									{	
										dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
										if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
											prnFMTCHR(dosREPORT,M_strEJT);
										if(M_rdbHTML.isSelected())
											dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
										prnHEADER();
									}
									dosREPORT.writeBytes("\n");
				   					cl_dat.M_intLINNO_pbst+= 1;
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strBOLD);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<B>");
									dosREPORT.writeBytes(padSTRING('L',"",12));
									dosREPORT.writeBytes(padSTRING('R',"Total "+L_strPSPRDS,101));
		    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSBRQT,3),14));
		    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intSBPQT,0),10)+"\n\n");
									cl_dat.M_intLINNO_pbst+= 2;
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strNOBOLD);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("</B>");
									L_dblSBRQT = 0;
									L_intSBPQT = 0;
									L_strPSBPRD = L_strSBPRD;
									L_strPSPRDS = L_strSPRDS;
								}//while((L_strRCTDT+L_strSHFCD+L_strMNPRD)	
								if(cl_dat.M_intLINNO_pbst >= 64)
								{	
									dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
									prnHEADER();
								}
								dosREPORT.writeBytes("\n");
				   				cl_dat.M_intLINNO_pbst+= 1;
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");
								dosREPORT.writeBytes(padSTRING('L',"",8));
								dosREPORT.writeBytes(padSTRING('R',"Total "+L_strPMPRDS,105));
		    					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblMNRQT,3),14));
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intMNPQT,0),10)+"\n");
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");
				   				cl_dat.M_intLINNO_pbst+= 2;
								L_dblMNRQT = 0;
								L_intMNPQT = 0;
								L_strPMNPRD = L_strMNPRD;
								L_strPMPRDS = L_strMPRDS;
							}//while((L_strRCTDT+L_strSHFCD)	
							if(cl_dat.M_intLINNO_pbst >= 64)
							{	
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst+= 1;
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes(padSTRING('L',"",4));
							dosREPORT.writeBytes(padSTRING('R',"Total for "+L_strPSHFDS,109));
		    				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSHRQT,3),14));
		    				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intSHPQT,0),10)+"\n\n");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
				   			cl_dat.M_intLINNO_pbst+= 2;
							L_dblSHRQT = 0;
							L_intSHPQT = 0;
							L_strPSHFCD = L_strSHFCD;
							L_strPSHFDS = L_strSHFDS;
						}//while((L_strRCTDT)
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
						}
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst+= 1;
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes(padSTRING('R',"Total for "+L_strRCTDT,113));
					  	dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDTRQT,3),14));
		    			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intDTPQT,0),10)+"\n\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						cl_dat.M_intLINNO_pbst+= 2;
						L_dblDTRQT = 0;
						L_intDTPQT = 0;
						L_strPRCTDT = L_strRCTDT;
					}//while(eof())
				}//End If
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Grand Total ",113));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTRQT,3),14));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intGTPQT,0),10)+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				cl_dat.M_intLINNO_pbst+= 1;
				L_dblGTRQT = 0;
				L_intGTPQT = 0;
			    prnFOOTR();
				if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
				if(M_rstRSSET != null)
					M_rstRSSET.close();						
				else
    			{							
					setMSG("No Data Found..",'E');
					return ;
				}
				if(intRECCT == 0)
				{
					setMSG("No Data Found for the given selection..",'E');
					return ;
				}
			}	
		}	
	
	 	catch(Exception L_EX)
		{		    
			setMSG(L_EX+" getDATA",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}
	/**
	Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",114));
			dosREPORT.writeBytes("------------------------------\n");
			dosREPORT.writeBytes(padSTRING('L'," ",114));
			dosREPORT.writeBytes(strISODC1+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",114));
			dosREPORT.writeBytes(strISODC2+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",114));
			dosREPORT.writeBytes(strISODC3+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",114));
			dosREPORT.writeBytes("------------------------------\n");
			String L_strADDHDR = "";
			L_strADDHDR = L_strADDHDR + (lblRCTTP.getText().trim().length()>0 ? "   Rct.Type:"+lblRCTTP.getText().trim()+"  " : "");
			L_strADDHDR = L_strADDHDR + (lblMNPRD.getText().trim().length()>0 ? "   Prd.Cat.:"+lblMNPRD.getText().trim()+"  " : "");
			L_strADDHDR = L_strADDHDR + (lblSHFCD.getText().trim().length()>0 ? "   Shift:"+lblSHFCD.getText().trim()+"  " : "");
			L_strADDHDR = L_strADDHDR.trim().length()>0 ? "  ("+L_strADDHDR+") " : L_strADDHDR;
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst ,120));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Gradewise Receipt Detail Report"+L_strADDHDR+"  between " +txtFMDAT.getText().trim()+ " and " + txtTODAT.getText().trim(),120));
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("Rcpt.Date   \n");		    			
			//dosREPORT.writeBytes("    Shift :" +lblSHFCD.getText().trim()+ "\n");
			//dosREPORT.writeBytes("        Finished Goods Category  \n");
			dosREPORT.writeBytes("            Product Category :" +lblMNPRD.getText().trim()+ "\n");
			dosREPORT.writeBytes("                Lot No.   Prov.          Silo    Start   End     Miss    Package  Main    UCL     Remarks                  Qty.      Bags       \n");
			dosREPORT.writeBytes("                          Grade          No.     Count   Count   Count   Type     Loc.    Tag No.                                               \n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 16;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
	/**
  	 *Method to Generate the Page Footer of the Report.
	*/
	private void prnFOOTR()
	{
		try
		{
			if(cl_dat.M_intLINNO_pbst >= 64)
			{
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
				prnHEADER();
			}
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes ("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('L'," ",10));//margin
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",55));
			dosREPORT.writeBytes(padSTRING('R',"CHECKED BY  ",55));
			dosREPORT.writeBytes(padSTRING('R',"H.O.D (MHD)  ",55));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes("\n System generated report, hence signature not required \n");
			cl_dat.M_intLINNO_pbst += 7;
			if(!txtTREMDS.getText().equals(""))
			{	
				dosREPORT.writeBytes("Remark :");
				if(txtTREMDS.getText().length() > 120)
				{	
					dosREPORT.writeBytes(padSTRING('L',txtTREMDS.getText().substring(0,120),120)+"\n");
					dosREPORT.writeBytes(txtTREMDS.getText().substring(120));
				}	
				else
					dosREPORT.writeBytes(txtTREMDS.getText());	
			}	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{   
				prnFMTCHR(dosREPORT,M_strEJT);
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX  + " prnFOOTR",'E');
		}	
	}

	class INPVF extends InputVerifier
	{
		String L_strSHIFT="",L_strPRDCT="";
	  public boolean verify(JComponent Jcomp)
	  {
		 try
		 {
			if(((JTextField)Jcomp).getText().length() == 0)
				return true;
			if(Jcomp == txtSHFCD)
			{
				L_strSHIFT = txtSHFCD.getText().toString().trim().toUpperCase();
				txtSHFCD.setText(L_strSHIFT);
				if(!hstCDTRN.containsKey("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHIFT))
					{setMSG("Invalid Shift Code",'E'); return false;}
			}
			if(Jcomp == txtMNPRD)
			{
				L_strPRDCT = txtMNPRD.getText().toString().trim().toUpperCase();
				if(!hstCDTRN.containsKey("MSTCOXXPGR"+L_strPRDCT+"0000000A"))
					{setMSG("Invalid Product Category",'E'); return false;}
			}
			if(Jcomp == txtSHFCD)
			{
				M_strSQLQRY =  "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='M"+cl_dat.M_strCMPCD_pbst+"' ";
				M_strSQLQRY+=  "and CMT_CGSTP='COXXSHF'";
				M_strSQLQRY+=  "and isnull(CMT_STSFL,'')<> 'X'";
				M_strSQLQRY += " AND CMT_CODCD = '"+txtSHFCD.getText().trim()+"'";	
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				   
				   if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						System.out.println("inside inpvf");
					   lblSHFCD.setText(M_rstRSSET.getString("CMT_CODDS"));
					   setMSG("",'N');
					   System.out.println("inside inpvf1");
					}
					else
					{
						lblSHFCD.setText("");
						setMSG("Enter Valid Shift Code",'E');
						return false;
					}
				
			}
						
			if(Jcomp == txtMNPRD)
			{
			   M_strSQLQRY  = "select left(CMT_CODCD,2),CMT_CODDS from CO_CDTRN WHERE ";
			   M_strSQLQRY+=  "CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and CMT_CCSVL='MG'";
			   M_strSQLQRY += " AND left(CMT_CODCD,2)= '"+txtMNPRD.getText()+"'";
			   M_strSQLQRY+=  " AND isnull(CMT_STSFL,'') <> 'X'";
			   M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			   //System.out.println("inside INPVF txtMNPRD "+M_strSQLQRY);
			   
			   if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					
				   lblMNPRD.setText(M_rstRSSET.getString("CMT_CODDS"));
				   setMSG("",'N');
				   
				}
				else
				{
					lblMNPRD.setText("");
					setMSG("Enter Valid Product Categary",'E');
					return false;
				}
			   
			}
			  
			if(Jcomp == txtRCTTP)
			{
				M_strSQLQRY  = "select cmt_codcd,cmt_codds from co_cdtrn where ";
				M_strSQLQRY+=  "cmt_cgstp='FGXXRTP'  and cmt_codcd = '"+txtRCTTP.getText()+"'"+" order by cmt_codcd ";
				System.out.println(">>>>RCTTP>>>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
			
				   lblRCTTP.setText(M_rstRSSET.getString("cmt_codds"));
				   setMSG("",'N');
				  
				}
				else
				{
					lblRCTTP.setText("");
					setMSG("Enter Valid Receipt Type",'E');
					return false;
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



		/** One time data capturing for specified codes from CO_CDTRN
		 * into the Hash Table
		 */
         private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
        {
			String L_strSQLQRY = "";
            try
            {
		        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")   "+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
			    System.out.println(L_strSQLQRY);
		        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
                if(L_rstRSSET == null || !L_rstRSSET.next())
                {
		            //setMSG("Records not found in CO_CDTRN",'E');
                    return;
                }
                while(true)
                {
                        strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                        strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                        strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                        String[] staCDTRN = new String[intCDTRN_TOT];
                        staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                        staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                        staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
                        staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
                        staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
                        staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
                        staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
                        staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
                        staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
                        staCDTRN[intAE_CMT_MODLS] = getRSTVAL(L_rstRSSET,"CMT_MODLS","C");
                        LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
				//if(strCGSTP.equals("HRXXLVE"))
				//	System.out.println("Adding : "+strCGMTP+strCGSTP+strCODCD);
				hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
                        if (!L_rstRSSET.next())
                                break;
                }
                L_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtCDTRN");
            }
		}



		 
		 
		 
		/** Picking up Specified Codes Transaction related details from Hash Table
		 * <B> for Specified Code Transaction key
		 * @param LP_CDTRN_KEY	Code Transaction key
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
        {
		//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
        try
        {
				if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
					{setMSG(LP_CDTRN_KEY+" not found in CO_CDTRN hash table",'E'); return " ";}
                if (LP_FLDNM.equals("CMT_CODCD"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
                else if (LP_FLDNM.equals("CMT_CODDS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
                else if (LP_FLDNM.equals("CMT_SHRDS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
                else if (LP_FLDNM.equals("CMT_CHP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
                else if (LP_FLDNM.equals("CMT_CHP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
                else if (LP_FLDNM.equals("CMT_NMP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
                else if (LP_FLDNM.equals("CMT_NMP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
                else if (LP_FLDNM.equals("CMT_NCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
                else if (LP_FLDNM.equals("CMT_CCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
                else if (LP_FLDNM.equals("CMT_MODLS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_MODLS];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return "";
        }
		 

		
		
		
/** Method for returning values from Result Set
 * <br> with respective verifications against various data types
 * @param	LP_RSLSET		Result set name
 * @param       LP_FLDNM                Name of the field for which data is to be extracted
 * @param	LP_FLDTP		Data Type of the field
 */
private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
{
	//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
    try
    {
	if (LP_FLDTP.equals("C"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
		//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
	else if (LP_FLDTP.equals("N"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
	else if (LP_FLDTP.equals("D"))
		return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
	else if (LP_FLDTP.equals("T"))
	    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
	else 
		return " ";
	}
	catch (Exception L_EX)
	{setMSG(L_EX,"getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);}
return " ";
} 



}
