/*
System Name   : Lebortory Information Management System
Program Name  : Product Test Certificate
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 20 JUNE 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
import java.math.BigDecimal;
import javax.swing.JComponent;import javax.swing.InputVerifier;import javax.swing.JComboBox;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Product Test Certificate.

Purpose : Report for taking the Product Test Details for given Lots.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
QC_PSMST       PS_QCATP, PS-TSTTP,PS_LOTNO,
               PS_RCLNO,PS_TSTNO,PS_TSTDT                              #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtDATE     PS_TSTDT       QC_PSMST      DATE          Test Date 
txtRMKDS1   RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 1
txtRMKDS2   RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 2
txtRMKDS3   RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 3
txtRMKDS4   RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 4
--------------------------------------------------------------------------------------
<B>
Logic</B>
Each grade (finshed product) has some proparties. & these proparties changes as per 
new standards adopted. 
These proparties are varing from grade to grade.
System manages proparties of each grade & makes Latest propatries available.
According to the new standards, some new properties are added and some unwanted
are removed for specific grade.
Hence to generate report every time list of proparties are generated dynamically as :-
   1) Latest property list is fetched from the database associated with perticular grade .
   2) These Properties are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    "PS_"+ Vector.elementAt(i)+"VL" i.e SMT_COLVL, SMT_TOLVL
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

For Remark1,2,3 & 4 are taken from QC_RMMST for conditations
    1) RM_QCATP = given QCA Type here it is "01"
    2) AND RM_TSTTP like last three charectores of the report file name.
    3) AND RM_TSTNO = report date & time 	

<I><B>Conditions Give in Query:</b>
    Data is taken from QC_PSMST and PR_LTMST for given Test Date
       1) PS_LOTNO = LT_LOTNO 
       2) AND PS_RCLNO = LT_RCLNO 
       3) AND PS_TSTDT Between a day before login date & login date time
       4) AND PS_RCLNO = "00"
       5) AND PS_TSTTP ='0101' 
       6) AND PS_QCATP ='01'             
<B>Validations :</B>
    - Date Time must be valid. i.e.smaller than current date & Time.       
</I> */
public class qc_nwptc extends cl_rbase  
{
	
	private JTextField txtGRPCD;
	private JTextField txtFMLOT;
	private JTextField txtTOLOT;
	private JTextField txtRCLNO;
	private JComboBox cmbPRDTP; 
	private String strPRDTP="";
	///strPRDTP =M_strSBSCD.substring(2,4);;
	private String strISODOC1;
	private String strISODOC2;
	private String strISODOC3;
		
	private String strFILNM;
	private String strQCATP=" ";
	private String strLOTNO="";
	private String strRCLNO="00";
	private String strPRDCD="";
	private String strPRDDS="";
	private String strUOMDS="";
	private String strQPRCD="";
	private String strQPRDS="";
	private String strTSMDS="";
	private String strCLSDT="";
	private String strDECVL="0";
	private String strTSTCN="";
	private String strPSMST_FL = "QC_PSMST";
											/** Input varifier for master data validity Check.*/
	Hashtable<String,String> L_hstRMMST=new Hashtable<String,String>(); 
	StringBuffer L_stbDATA = new StringBuffer("");
	private INPVF objINPVR = new INPVF();
		
	
    private FileOutputStream fosREPORT ;
    private DataOutputStream dosREPORT ;
		
	private String strINTRCL ="00";
	private String strTSTTP = "0103"; // composite certification Test	
	private boolean flgSPCFL = false;	
	private boolean flgREPFL = true;
	
	private String LM_DFLSRL ="00000";
		
    private BigDecimal bgdNPFVL,bgdNPTVL;
	private String strRMKDS="",strSTSFL,strPRDGR = "",strPRGDS,strGRPPR;
	private String strTXTVL,strRSLVL,strCMPFL,strFLDNM,strGRPCD;
	private String strTXTVL_DE,strRSLVL_DE;
	private java.util.StringTokenizer LM_STRTKN;
	qc_nwptc()
	{
	  super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		   
		    add(new JLabel("Product Type"),4,3,1,1,this,'R');
			add(cmbPRDTP = new JComboBox(),4,4,1,2,this,'L');				
		    
		    add(new JLabel("From Lot No."),5,3,1,1,this,'L');
			add(txtFMLOT = new TxtNumLimit(8),5,4,1,1,this,'L');
			
			add(new JLabel("To Lot No."),5,5,1,1,this,'L');
			add(txtTOLOT = new TxtNumLimit(8),5,6,1,1,this,'L');
			
			add(new JLabel("RCL No."),6,3,1,1,this,'L');
			add(txtRCLNO = new TxtNumLimit(2),6,4,1,1,this,'L');
			
			add(new JLabel("Group Code"),6,5,1,1,this,'L');
			add(txtGRPCD = new TxtNumLimit(8),6,6,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);	
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD' AND isnull(CMT_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD + " "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'ISO'"
				+ " AND CMT_CGSTP ='QCXXPTC' and isnull(CMT_STSFL,'') <>'X' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"").equals("DOC1"))
					    strISODOC1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"").equals("DOC2"))
					    strISODOC2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"").equals("DOC3"))
					    strISODOC3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
				}
				M_rstRSSET.close();
			}
			//txtFMLOT.setInputVerifier(objINPVR);
			//txtTOLOT.setInputVerifier(objINPVR);
			//txtGRPCD.setInputVerifier(objINPVR);
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			
			//System.out.println(M_strSBSCD);
			//System.out.println(M_strSBSCD.substring(2,4));
			strQCATP = M_strSBSCD.substring(2,4);
			//System.out.println(M_rdbHTML.isSelected());
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Method super class method overrided to enable & disable components
	 * according to the requriement.
	 * @param P_flgSTAT boolean argument to pass state of the Component.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			M_rdbHTML.setSelected(true);
		if(M_objSOURC == cmbPRDTP)
		{			
		    if(cmbPRDTP.getItemCount() >0)				
			    strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			//strPSMST_FL = cmbPRDTP.getSelectedItem().toString().substring(0,2).equals("02") ? "QC_PSMST_S" : "QC_PSMST";

		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter Lot Number to specify Lot range..",'N');
				setENBL(true);				
				txtFMLOT.requestFocus();
				strQCATP =M_strSBSCD.substring(2,4);
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{		    
			if((M_objSOURC == txtFMLOT))
			{   
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtFMLOT";
				String LM_ARRHDR[] = {"Lot No." ,"Lot Start Date","Product Code"};

				M_strSQLQRY = "select LT_LOTNO,LT_PSTDT,PR_PRDDS from PR_LTMST,CO_PRMST";
				M_strSQLQRY += " WHERE LT_PRDCD = PR_PRDCD ";
				M_strSQLQRY += " AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP = '"+strPRDTP +"'"; 
				if(txtTOLOT.getText().trim().length()>0)
					M_strSQLQRY += " AND LT_LOTNO <'"+ txtTOLOT.getText().trim()+"'";
				if(txtFMLOT.getText().trim().length()>0)
					M_strSQLQRY += " AND LT_LOTNO like "+"'" + txtFMLOT.getText().trim()+"%'";
				M_strSQLQRY += " AND LT_CLSFL = '9' and LT_STSFL <> 'X' "; // for classified lots
				M_strSQLQRY += " order by LT_LOTNO";
				cl_hlp(M_strSQLQRY,1,1,LM_ARRHDR,3,"CT");
				this.setCursor(cl_dat.M_curDFSTS_pbst);
		    }
			else if((M_objSOURC == txtTOLOT))
			{   
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtTOLOT";
				String LM_ARRHDR[] = {"Lot No." ,"Lot Start Date","Product Code"};
				M_strSQLQRY = "select LT_LOTNO,LT_PSTDT,PR_PRDDS from pr_ltmst,CO_PRMST";
				M_strSQLQRY += " WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDCD = PR_PRDCD ";
				M_strSQLQRY += " AND LT_PRDTP ='"+strPRDTP +"'";
				if(txtFMLOT.getText().trim().length()>0)
					M_strSQLQRY += " AND LT_LOTNO >'"+ txtFMLOT.getText().trim()+"'";
				if(txtTOLOT.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_LOTNO like "+"'" + txtTOLOT.getText().trim()+"%'";
				M_strSQLQRY += " AND LT_CLSFL = '9' AND LT_STSFL <> 'X' AND PR_STSFL <>'4'"; // for classified lots
				M_strSQLQRY += " order by LT_LOTNO ";
				cl_hlp(M_strSQLQRY,1,1,LM_ARRHDR,3,"CT");
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if((M_objSOURC == txtRCLNO))
    		{   
			   String LM_ARRHDR[] = {"RCL No." ,"RCL Date","Product Code"};      
			   M_strSQLQRY = "select lt_rclno,LT_CLSTM,PR_PRDDS from pr_ltmst,CO_PRMST";
			   M_strSQLQRY += " Where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDCD = PR_PRDCD AND LT_CLSFL ='9' AND PR_STSFL <>'4'"; // for classified lots
	           M_strSQLQRY += " AND LT_STSFL <> 'X' and LT_CLSTM is not null";
			   M_strSQLQRY += " AND LT_PRDTP  ='" +strPRDTP.trim()+"'";
			   M_strSQLQRY += " AND LT_LOTNO  ='" +txtFMLOT.getText().trim()+"'";
			   M_strHLPFLD = "txtRCLNO";
			   if(txtFMLOT.getText().trim().equals(txtTOLOT.getText().trim()))
			   {
				   cl_dat.M_flgHELPFL_pbst = true;
				   cl_hlp(M_strSQLQRY,1,1,LM_ARRHDR,3,"CT");
			   }
    	    }		  				
			else if(M_objSOURC == txtGRPCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtGRPCD";
				String LM_ARRHDR[] ={"Group Code ","Description","Product Code"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN"
					+" where CMT_CGMTP='SYS' AND CMT_CGSTP = 'QCXXTCG'";
				cl_hlp(M_strSQLQRY,1,1,LM_ARRHDR,3,"CT");				
			}			
		}
		if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
    		if(M_objSOURC == txtFMLOT)
    		{
    			setMSG("Please Enter Lot Number to specify Lot range..",'N');
    			if(txtFMLOT.getText().trim().length()== 8)
    			{
    				txtTOLOT.requestFocus();
    				txtTOLOT.setText(txtFMLOT.getText().trim());
    				setMSG("Please Enter To Lot Number to specify Lot range..",'N');
    			}
    			else
    				setMSG("Please Enter From Lot Number ..",'N');
    		}
    		else if(M_objSOURC == txtTOLOT)
    		{
    		    setMSG("Please Enter RCL Number to generate the Report..",'N');
    			if(txtTOLOT.getText().trim().length()== 8)
    			{
    			    if(txtFMLOT.getText().trim().equals(txtTOLOT.getText().trim()))
    				{
    				    txtRCLNO.requestFocus();
    				    txtRCLNO.setText(strINTRCL);
    				    txtRCLNO.setEnabled(true);
    				    setMSG("Please Enter RCL Number to generate the Report..",'N');
    				}
    				else
    				{
    				    txtRCLNO.setEnabled(false);
    				    txtRCLNO.setText(strINTRCL);
    		            setMSG("Please Enter Group Code to generate the Report..",'N');
    			        txtGRPCD.requestFocus();		
    			    }
    			}			
    		}
    		else if(M_objSOURC == txtRCLNO)
    		{
    			setMSG("Please Enter Group Code to generate the Report..",'N');
    			txtGRPCD.requestFocus();
    		}
    		else if(M_objSOURC == txtGRPCD)
    		{
    			cl_dat.M_btnSAVE_pbst.requestFocus();
    		}
	}
	}
	/**
	Method for execution of F1 help & to enter the selected data in to Corresponding Textfield.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMLOT")
		{
			txtFMLOT.setText(cl_dat.M_strHLPSTR_pbst);
			if(txtFMLOT.getText().length()>0)	
			    txtTOLOT.requestFocus();
		}		
		if(M_strHLPFLD == "txtTOLOT")
		{
			txtTOLOT.setText(cl_dat.M_strHLPSTR_pbst);
			if(txtFMLOT.getText().trim().equals(txtTOLOT.getText().trim()))
			{
			    txtRCLNO.requestFocus();
			    txtRCLNO.setText(strINTRCL);
			    txtRCLNO.setEnabled(true);
			    setMSG("Please Enter RCL Number to generate the Report..",'N');
			}
			else
			{
			    txtRCLNO.setEnabled(false);
			    txtRCLNO.setText(strINTRCL);
	            setMSG("Please Enter Group Code to generate the Report..",'N');
		        txtGRPCD.requestFocus();		
		    }
		}
		if(M_strHLPFLD == "txtRCLNO")		
		    if(txtFMLOT.getText().trim().equals(txtTOLOT.getText().trim()))
			    txtRCLNO.setText(cl_dat.M_strHLPSTR_pbst);	
		
		if(M_strHLPFLD == "txtGRPCD")		
		{
			int L_TKNCNT =0;
    		cl_dat.M_flgHELPFL_pbst= false;
    		txtGRPCD.setEnabled(true); 
    		txtGRPCD.requestFocus(); 
    		LM_STRTKN = new java.util.StringTokenizer(cl_dat.M_strHELP_pbst+"|","|");
    		while(LM_STRTKN.hasMoreTokens())
    		{
    			L_TKNCNT +=1;
    			if(L_TKNCNT == 1)
    			{
    				LM_STRTKN.nextToken();
    			}
    			else if(L_TKNCNT == 2)
    			{
    				
    				LM_STRTKN.nextToken();
    			}
    			else if(L_TKNCNT == 3)
    			{
    				
    				strGRPPR = LM_STRTKN.nextToken().trim();
    			}
    		}
       		txtGRPCD.setText(cl_dat.M_strHLPSTR_pbst);					
		}
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_nwptc.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_nwptc.doc";
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();			
			if(flgREPFL == false)
			{
			    return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
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
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
			     else
   				    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Product Test Certificate"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exePrint.. ");
		}
	}			
    /**
    * Method to fetch requried data from database & to club it with Header 
    * & footer in the DataOutputStream.
	*/
	private void getDATA()
	{ 				
		int i = 0;
		String L_strSQLQRY="";
		ResultSet L_rstRSSET;
		String L_TMP = "";
		java.sql.Date tmpDATE	;
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	
	        flgREPFL = true;        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			
			////Serial No. and test conditions from QP_RMMST is Stored in the Hash Table
			try
			{
					L_strSQLQRY  = " select QS_QPRCD,isnull(QS_DECVL,0) QS_DECVL from co_qsmst";
					L_strSQLQRY  += " where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
					//System.out.println("L_strSQLQRY>>"+L_strSQLQRY);
					L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET!=null)
					{
						while(L_rstRSSET.next())
							L_hstRMMST.put(L_rstRSSET.getString("QS_QPRCD"),L_rstRSSET.getString("QS_DECVL"));
					}				
			}
			catch(Exception L_EX){
				setMSG(L_EX,"fetching RMMST");
			}	
			//////////////////////////////////////////////////////////////////
			
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			    prnFMTCHR(dosREPORT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
			{	
			   // dosREPORT.writeBytes("<HTML><HEAD><Title>Product Transfer Form</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
			//	dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 			
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Product Test Certificate </title> </HEAD> <BODY><P><PRE style =\"font-size : 8 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");
			//	dosREPORT.writeBytes("</STYLE>"); 
			}			
			
			if(txtRCLNO.getText().trim().length()>0)
				strRCLNO=txtRCLNO.getText().trim();
		    else strRCLNO = strINTRCL;
			M_strSQLQRY = " SELECT LT_LOTNO,PR_PRDCD,PR_PRDDS,PR_STSFL,CONVERT(varchar,LT_CLSTM,103) L_CLSDT from PR_LTMST,CO_PRMST ";
			M_strSQLQRY += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDCD = PR_PRDCD  AND LT_CLSFL = '9' AND LT_STSFL <> 'X' ";
			M_strSQLQRY += " AND LT_PRDTP ='"+strPRDTP.trim()+"'";
			M_strSQLQRY += " AND LT_LOTNO BETWEEN ";
			M_strSQLQRY += "'" + txtFMLOT.getText().trim() + "'" + " AND " + "'" + txtTOLOT.getText().trim() + "'";
			M_strSQLQRY += " AND LT_RCLNO ='"+strRCLNO+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				while(M_rstRSSET.next())
				{	
					//System.out.println("001");
					if(L_stbDATA.length()!= 0)
						L_stbDATA.delete(0,L_stbDATA.length());
					//System.out.println("002");
					strCLSDT ="";
					strLOTNO = M_rstRSSET.getString("LT_LOTNO");					
					strPRDCD = M_rstRSSET.getString("PR_PRDCD");
					strPRDDS = M_rstRSSET.getString("PR_PRDDS");					
					strSTSFL = M_rstRSSET.getString("PR_STSFL");					
					
					tmpDATE = M_rstRSSET.getDate("L_CLSDT");					
					if (tmpDATE != null)
						strCLSDT = M_fmtLCDAT.format(tmpDATE);										
					strCLSDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(strCLSDT));																
					
					L_strSQLQRY = " select * from QC_PSMST,CO_QPMST,CO_QSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QS_CMPCD = PS_CMPCD and QS_QPRCD = QP_QPRCD and PS_QCATP ="+"'"+strQCATP.trim() +"'";					
					L_strSQLQRY += " AND PS_TSTTP ='"+strTSTTP.trim() +"'";					
					L_strSQLQRY += " AND PS_LOTNO ="+"'"+strLOTNO.trim() +"'";					
					L_strSQLQRY += " AND PS_RCLNO ="+"'"+txtRCLNO.getText().trim() +"'";					
					L_strSQLQRY += " and QP_STRDT <='"+strCLSDT+"'";					
					L_strSQLQRY += " and isnull(qp_enddt,current_date) >='"+strCLSDT+"' and upper(QP_QPRCD) not in ('A__','B__','L__')";
		
					///////////////////////////////////////////////////////////////////////////
					/////// commented on 14/01/2002, as classification time should be taken as base instead of test time
					//L_strSQLQRY += " AND date(PS_TSTDT) between qp_strdt and isnull(qp_enddt,current_date)";
					/////////////////////////////////////////////////////////////////////////
					L_strSQLQRY += " AND QP_PRGFL like '%T%'";
					L_strSQLQRY += " AND QP_PRDCD = '"+strPRDCD.trim()+"'" ;							
					if((txtGRPCD.getText().trim().length() >0)&&(strGRPPR.trim().equals(strPRDCD)))
						L_strSQLQRY += " AND QP_SRLNO = '"+txtGRPCD.getText().trim() +"'" ;						
					else
						L_strSQLQRY += " AND QP_SRLNO = '"+LM_DFLSRL+"'" ;					
					L_strSQLQRY += " and QP_ORDBY IS NOT NULL ORDER BY QP_ORDBY ";
					
					//***********************************************
					//L_strSQLQRY = " select * from CO_QPMST,QC_PSMST where  QP_STRDT <='"+strCLSDT+"' and isnull(qp_enddt,current_date) >='"+strCLSDT+"' and QP_PRGFL like '%T%' AND QP_PRDCD = '"+strPRDCD.trim()+"' "
					//			  +" AND QP_SRLNO = '"+((txtGRPCD.getText().trim().length() >0)&&(strGRPPR.trim().equals(strPRDCD))? txtGRPCD.getText().trim() : LM_DFLSRL)+"'  and PS_LOTNO = '"+strLOTNO.trim() +"' "
					//	          +" AND PS_QCATP = '"+strQCATP.trim() +"' and PS_TSTTP ='"+strTSTTP.trim() +"' AND PS_RCLNO ='"+txtRCLNO.getText().trim() +"'  and QP_ORDBY IS NOT NULL ORDER BY QP_ORDBY";
					
					
					//***********************************************
					 				 
					//System.out.println(L_strSQLQRY);
					int L_intDECVL = 0;
					int L_intTSTCN_TOT = 0,L_intTSTCN_RUN = 0,L_intTSTCN_1 =0,L_intTSTCN_2 =0;
					int L_intTSMDS_STR=0,L_intTSMDS_END=0;
					String L_strTSMDS_SUB="";
					
					L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);		
					if(L_rstRSSET !=null)
					{
						while(L_rstRSSET.next()) //for(int j=0;j<LM_QPRCNT;j++)
						{	
							strUOMDS = "";L_strTSMDS_SUB="";
							L_intTSMDS_STR=0;L_intTSMDS_END=0;
							strQPRCD = nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),"");
							strUOMDS = nvlSTRVL(L_rstRSSET.getString("QP_UOMDS"),"-");
							strQPRDS = nvlSTRVL(L_rstRSSET.getString("QP_QPRDS"),"-");
							strTSMDS = nvlSTRVL(L_rstRSSET.getString("QP_TSMDS"),"-");
							L_strTSMDS_SUB = nvlSTRVL(L_rstRSSET.getString("QP_TSMDS"),"-");
							L_intTSMDS_END=strTSMDS.length()>11?11:strTSMDS.length();
							strTSTCN = nvlSTRVL(L_rstRSSET.getString("QS_TSTCN"),"-");
							L_intTSTCN_RUN = 0;
							L_intTSTCN_TOT = strTSTCN.trim().length();
							
							//strDECVL = nvlSTRVL(L_rstRSSET.getString("QS_DECVL"),"0");
							strDECVL =L_hstRMMST.containsKey(L_rstRSSET.getString("QP_QPRCD")) ? L_hstRMMST.get(L_rstRSSET.getString("QP_QPRCD")).toString() : "0";
							L_intDECVL = Integer.parseInt(strDECVL);
							
							flgSPCFL = false;
							strCMPFL ="";
							
							bgdNPFVL = null; 
							bgdNPTVL = null;
							if(!flgSPCFL)
								flgSPCFL =true;
							/*deprecated in 1.6
							bgdNPFVL = L_rstRSSET.getBigDecimal("QP_NPFVL",3);
							bgdNPTVL = L_rstRSSET.getBigDecimal("QP_NPTVL",3);
							*/

							bgdNPFVL = L_rstRSSET.getBigDecimal("QP_NPFVL");
							bgdNPTVL = L_rstRSSET.getBigDecimal("QP_NPTVL");
							strTXTVL = nvlSTRVL(L_rstRSSET.getString("QP_TXTVL"),"");	 
							strCMPFL = L_rstRSSET.getString("QP_CMPFL");	 
							
							strFLDNM = "PS_"+strQPRCD.trim()+"VL";
							strDECVL =L_hstRMMST.containsKey(strQPRCD.trim()) ? L_hstRMMST.get(L_rstRSSET.getString("QP_QPRCD")).toString() : "0";
							L_intDECVL = Integer.parseInt(strDECVL);
							if(strQPRCD.equals("SPG"))//test Parameter is Specific gravity
								strRSLVL = setNumberFormat(Double.parseDouble(L_rstRSSET.getString(strFLDNM)),3);
							else 
								strRSLVL = setNumberFormat(Double.parseDouble(L_rstRSSET.getString(strFLDNM)),L_intDECVL);
							//System.out.println("strFLDNM>>"+strFLDNM);
							//System.out.println("L_rstRSSET.getString(strFLDNM)>>"+L_rstRSSET.getString(strFLDNM));
							//System.out.println("strRSLVL>>"+strRSLVL);
							if(strQPRCD.trim().equals("MOI"))
						    {
								if(strRSLVL !=null)
									strRSLVL = setNumberFormat(Double.valueOf(strRSLVL).doubleValue(),0);
							}
							if(strRSLVL ==null)
								strRSLVL = "-";
							if(strTSMDS.trim().length() == 0)
								L_strTSMDS_SUB = padSTRING('R',"-",20);
							else
							{	
								L_strTSMDS_SUB = padSTRING('R',strTSMDS.substring(0,L_intTSMDS_END),20);
							}	
							strQPRDS = padSTRING('R',strQPRDS,40);
					        L_stbDATA.append(padSTRING('R'," ",3));     
							L_stbDATA.append("<B>");
							L_stbDATA.append(padSTRING('R',strQPRDS.toUpperCase(),35)+" ");
							L_stbDATA.append("</B>");
							//L_stbDATA.append(padSTRING('R',strTSTCN,35)+" ");
							//**L_intTSTCN_1 = L_intTSTCN_RUN;L_intTSTCN_2 = ((L_intTSTCN_RUN+35)>L_intTSTCN_TOT ? L_intTSTCN_TOT : L_intTSTCN_RUN+35);
							//System.out.println(L_intTSTCN_TOT+"  "+L_intTSTCN_1+"  "+L_intTSTCN_2);
							//L_stbDATA.append(padSTRING('R',strTSTCN.substring(L_intTSTCN_1,L_intTSTCN_2),35)+"  ");
							//**L_intTSTCN_RUN = L_intTSTCN_2;
							//if(L_hstRMMST.containsKey(L_rstRSSET.getString("QP_QPRCD")))
							//{ 
							//	L_stbDATA.append(padSTRING('R',L_hstRMMST.get(L_rstRSSET.getString("QP_QPRCD")).toString(),35)+" ");
							//}
							//else 
							//	L_stbDATA.append(padSTRING('R',"",35)+" ");
							strUOMDS = padSTRING('R',strUOMDS,15);					        
							if(strQPRCD.equals("DE_"))
								L_stbDATA.append(padSTRING('R',"",15));
							else 
								L_stbDATA.append(padSTRING('R',strUOMDS,15));
							L_stbDATA.append(padSTRING('R',L_strTSMDS_SUB,22));
							L_TMP = "";
							
							if((bgdNPFVL != null)&& (bgdNPTVL != null) && (strRSLVL != null))
							{
								if(strCMPFL.equals("Y"))
								{
									if(strRSLVL.equals("-"))
									{
										setMSG("All the result values are not available,test certificate can not be generated..",'E');
										//*//flgREPFL = false;
									}
									else
									{
										setMSG("",'N');
										if(!chkQPRRNG(bgdNPFVL.floatValue(),bgdNPTVL.floatValue(),strCMPFL,Float.valueOf(strRSLVL).floatValue()))
										{
											//*//flgREPFL = false;
										}
									}
								}
								else if((strCMPFL.equals("N"))&&(strTXTVL.length() >0))
								{
									
									if(Integer.parseInt(strRSLVL) == 1) 
										strRSLVL = strTXTVL;
									else if(Integer.parseInt(strRSLVL) == 0)
										strRSLVL = "Not "+strTXTVL;
								}
							}
							if(!strSTSFL.equals("2")) // if not a prime grade
								L_TMP = padSTRING('R',"-",15); 							
							else
							{
								if((bgdNPFVL == null)&&(bgdNPTVL == null))										
									L_TMP = padSTRING('R',"-",15);								
								else if(bgdNPFVL.floatValue() == 0.0)
								{
									if(bgdNPTVL.floatValue() == 0.0)
										L_TMP = padSTRING('R',"-",15);										
									else
									   L_TMP = padSTRING('R',setNumberFormat(bgdNPTVL.floatValue(),L_intDECVL) + " max",15);										
																
								}
								else
								{
									if(bgdNPTVL.floatValue()== 0.0)
										L_TMP =padSTRING('R',setNumberFormat(bgdNPFVL.floatValue(),L_intDECVL)+" min",15);										
									else 
									{
										if(bgdNPFVL.floatValue()== bgdNPTVL.floatValue())
											L_TMP = padSTRING('R',setNumberFormat(bgdNPFVL.floatValue(),L_intDECVL)+"",15);
										else
										{
											if(strQPRCD.equals("SPG")) //test Parameter is Specific gravity
												L_TMP = padSTRING('R',setNumberFormat(bgdNPFVL.floatValue(),3) + " - "+setNumberFormat(bgdNPTVL.floatValue(),3),15);
											else 
												L_TMP = padSTRING('R',setNumberFormat(bgdNPFVL.floatValue(),L_intDECVL) + " - "+setNumberFormat(bgdNPTVL.floatValue(),L_intDECVL),15);
										}	
									}
								}
							}					       
							if(strQPRCD.equals("DE_"))
							{	
								L_stbDATA.append(padSTRING('R',"",15));
								strTXTVL_DE = L_TMP;
							}	
							else
								L_stbDATA.append(padSTRING('R',L_TMP,15));
							
							if(strQPRCD.equals("DE_"))
							{	
								L_stbDATA.append(padSTRING('L',"",9)+"\n");
								strRSLVL_DE = strRSLVL;
							}	
							else
								L_stbDATA.append(padSTRING('L',strRSLVL,9)+"\n");
							
							////2nd line printing for test condition and  test metod
							L_stbDATA.append(padSTRING('L'," ",3));
							L_intTSTCN_1 = L_intTSTCN_RUN;L_intTSTCN_2 = ((L_intTSTCN_RUN+35)>L_intTSTCN_TOT ? L_intTSTCN_TOT : L_intTSTCN_RUN+35);
							//System.out.println(L_intTSTCN_TOT+"  "+L_intTSTCN_1+"  "+L_intTSTCN_2);
							L_stbDATA.append(padSTRING('R',strTSTCN.substring(L_intTSTCN_1,L_intTSTCN_2),35));
							L_intTSTCN_RUN = L_intTSTCN_2;
							
							if(L_intTSMDS_END<strTSMDS.length())
							{	
								L_stbDATA.append(padSTRING('R',"",16));
								L_intTSMDS_STR=L_intTSMDS_END;
								L_intTSMDS_END=strTSMDS.length()>L_intTSMDS_END+11?L_intTSMDS_END+11:strTSMDS.length();
								L_strTSMDS_SUB = padSTRING('R',strTSMDS.substring(L_intTSMDS_STR,L_intTSMDS_END),20);
								L_stbDATA.append(padSTRING('R',L_strTSMDS_SUB,22));
								
							}
							L_stbDATA.append("\n");
								
							
							//L_stbDATA.append(strTSTCN);
							while((L_intTSTCN_TOT > 0 && L_intTSTCN_TOT > L_intTSTCN_RUN) || L_intTSMDS_END<strTSMDS.length())
							{
								if(L_intTSTCN_TOT > 0 && L_intTSTCN_TOT > L_intTSTCN_RUN)
								{	
									L_stbDATA.append(padSTRING('R'," ",3));
									L_intTSTCN_1 = L_intTSTCN_RUN;L_intTSTCN_2 = ((L_intTSTCN_RUN+35)>L_intTSTCN_TOT ? L_intTSTCN_TOT : L_intTSTCN_RUN+35);
									//System.out.println(L_intTSTCN_TOT+"  "+L_intTSTCN_1+"  "+L_intTSTCN_2);
									L_stbDATA.append(padSTRING('R',strTSTCN.substring(L_intTSTCN_1,L_intTSTCN_2),35));
									L_intTSTCN_RUN = L_intTSTCN_2;
								}
								else
									L_stbDATA.append(padSTRING('R',"",38));
								if(L_intTSMDS_END<strTSMDS.length())
								{	
									L_stbDATA.append(padSTRING('R',"",16));
									L_intTSMDS_STR=L_intTSMDS_END;
									L_intTSMDS_END=strTSMDS.length()>L_intTSMDS_END+11?L_intTSMDS_END+11:strTSMDS.length();
									L_strTSMDS_SUB = padSTRING('R',strTSMDS.substring(L_intTSMDS_STR,L_intTSMDS_END),20);
									L_stbDATA.append(padSTRING('R',L_strTSMDS_SUB,22));
								}
								L_stbDATA.append("\n");
							}
							L_stbDATA.append("\n");
						}
						L_rstRSSET.close();
						exePRINTCLR(strLOTNO,txtRCLNO.getText().trim(),strCLSDT);
											
					}
					// check
					/*flgREPFL = true;										
					if(!flgREPFL)
					{
						this.setCursor(cl_dat.M_curDFSTS_pbst);
						return;// false;
					}*/					
					if(flgREPFL == false)
					{
						flgREPFL = false;
						this.setCursor(cl_dat.M_curDFSTS_pbst);
						setMSG("Specifications out of Range, can not generate test certificate",'E');					
                        //return;
					}				
					prnHEADER();				
					dosREPORT.writeBytes(L_stbDATA.toString()+"\n");
					if(M_rdbHTML.isSelected())
					{
						dosREPORT.writeBytes("   ");
						dosREPORT.writeBytes("<B><HR SIZE = \"1\" COLOR = \"BLACK\"></B>"+"\n");
					}
					else
						dosREPORT.writeBytes("   --------------------------------------------------------------------------------------------------\n");
									
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("   REMARK - ");
					if(!strSTSFL.equals("2")) // if not prime grade
          				strRMKDS = "";        // remark input has been blocked.not required         
					else
					{
						if(flgSPCFL)
							strRMKDS ="The above lot meets specified requirements.";
						else
							strRMKDS = "";
					}					
					dosREPORT.writeBytes(strRMKDS );
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					dosREPORT.writeBytes("\n\n\n\n\n\n");
					dosREPORT.writeBytes("   Note : System generated report,hence signature not required.");                
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);									
					if(M_rdbHTML.isSelected())			
						dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P>");    				
				}
				M_rstRSSET.close();
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    				
			setMSG("Report completed.. ",'N');									
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}

	/**
	 */
	void exePRINTCLR(String LP_LOTNO,String LP_RCLNO, String LP_CLSDT)
	{
		String L_strSQLQRY="";
		ResultSet L_rstRSSET;
		String L_strQPRCD,L_strQPRDS,L_strTSTCN,L_strUOMDS,L_strTSMDS,L_strRSLVL;
		int L_intDECVL = 0;
		int L_intTSTCN_TOT = 0,L_intTSTCN_RUN = 0,L_intTSTCN_1 =0,L_intTSTCN_2 =0;
		String L_TMP="";
		double dblNPFVL;
		double dblNPTVL;
		try
		{
			L_strSQLQRY = " select * from QC_PSMST,CO_QPMST,CO_QSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP ="+"'"+strQCATP.trim() +"'";					
			L_strSQLQRY += " AND QS_CMPCD = PS_CMPCD and QS_QPRCD = QP_QPRCD ";
			L_strSQLQRY += " AND PS_TSTTP ='"+strTSTTP.trim() +"'";					
			L_strSQLQRY += " AND PS_LOTNO ="+"'"+LP_LOTNO.trim() +"'";					
			L_strSQLQRY += " AND PS_RCLNO ="+"'"+ LP_RCLNO+"'";					
			L_strSQLQRY += " and QP_STRDT <='"+LP_CLSDT+"'";					
			L_strSQLQRY += " and isnull(qp_enddt,current_date) >='"+LP_CLSDT+"' and upper(QP_QPRCD) in ('A__','B__','L__')";
		
			///////////////////////////////////////////////////////////////////////////
			/////// commented on 14/01/2002, as classification time should be taken as base instead of test time
			//L_strSQLQRY += " AND date(PS_TSTDT) between qp_strdt and isnull(qp_enddt,current_date)";
			/////////////////////////////////////////////////////////////////////////
			L_strSQLQRY += " AND QP_PRGFL like '%T%'";
			L_strSQLQRY += " AND QP_PRDCD = '"+strPRDCD.trim()+"'" ;							
			if((txtGRPCD.getText().trim().length() >0)&&(strGRPPR.trim().equals(strPRDCD)))
				L_strSQLQRY += " AND QP_SRLNO = '"+txtGRPCD.getText().trim() +"'" ;						
			else
				L_strSQLQRY += " AND QP_SRLNO = '"+LM_DFLSRL+"'" ;					
			L_strSQLQRY += " and QP_ORDBY IS NOT NULL ORDER BY QP_ORDBY ";
			//System.out.println("exePRINTCLR>>"+L_strSQLQRY);
			
			L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);		
			if(L_rstRSSET !=null)
			{
				//L_stbDATA.append(padSTRING('R'," ",3));
				//L_stbDATA.append(padSTRING('R',"Colour Values",35)+"\n");
				
				
				while(L_rstRSSET.next()) //for(int j=0;j<LM_QPRCNT;j++)
				{							
					strUOMDS = "";
					strQPRCD = nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),"");
					strUOMDS = nvlSTRVL(L_rstRSSET.getString("QP_UOMDS"),"-");
					strQPRDS = nvlSTRVL(L_rstRSSET.getString("QP_QPRDS"),"-");
					strTSMDS = nvlSTRVL(L_rstRSSET.getString("QP_TSMDS"),"-");
					flgSPCFL = false;
					strCMPFL ="";
					bgdNPFVL = null; 
					bgdNPTVL = null;
					dblNPFVL = 0; 
					dblNPTVL = 0;
					if(!flgSPCFL)
						flgSPCFL =true;
					/*deprecated in 1.6
					bgdNPFVL = L_rstRSSET.getBigDecimal("QP_NPFVL",3);
					bgdNPTVL = L_rstRSSET.getBigDecimal("QP_NPTVL",3);
					*/

					dblNPFVL = L_rstRSSET.getDouble("QP_NPFVL");
					dblNPTVL = L_rstRSSET.getDouble("QP_NPTVL");
					
					bgdNPFVL = L_rstRSSET.getBigDecimal("QP_NPFVL");
					bgdNPTVL = L_rstRSSET.getBigDecimal("QP_NPTVL");
					strTXTVL = nvlSTRVL(L_rstRSSET.getString("QP_TXTVL"),"");	 
					strCMPFL = L_rstRSSET.getString("QP_CMPFL");	 
								
					strFLDNM = "PS_"+strQPRCD.trim()+"VL";
					strDECVL =L_hstRMMST.containsKey(strQPRCD.trim()) ? L_hstRMMST.get(L_rstRSSET.getString("QP_QPRCD")).toString() : "0";
					L_intDECVL = Integer.parseInt(strDECVL);
					strRSLVL = setNumberFormat(Double.parseDouble(L_rstRSSET.getString(strFLDNM)),3);
					//if(strQPRCD.trim().equals("MOI"))
				    //{
					//	if(strRSLVL !=null)
					//		strRSLVL = setNumberFormat(Double.valueOf(strRSLVL).doubleValue(),L_intDECVL);
					//}
					if(strRSLVL ==null)
						strRSLVL = "-";
					if(strTSMDS.trim().length() == 0)
						strTSMDS = padSTRING('R',"-",20);
					else
						strTSMDS = padSTRING('R',strTSMDS,20);
					strQPRDS = padSTRING('R',strQPRDS,40);
					
					L_stbDATA.append(padSTRING('R'," ",3));     
					//L_stbDATA.append(padSTRING('R',strQPRDS,35)+" ");
					L_stbDATA.append(padSTRING('R',strQPRCD+"Std",20)+" ");
					if(bgdNPFVL.floatValue()!=0 && bgdNPTVL.floatValue()!=0)
						//L_stbDATA.append(padSTRING('R',setNumberFormat((dblNPFVL+dblNPTVL)/2,L_intDECVL),15));
						L_stbDATA.append(padSTRING('R',setNumberFormat((dblNPFVL+dblNPTVL)/2,3),16));
					else
						L_stbDATA.append(padSTRING('R',"",16));

					//if(L_hstRMMST.containsKey(L_rstRSSET.getString("QP_QPRCD")+L_rstRSSET.getString("QP_QPRSL")))
					//{
					//	L_stbDATA.append(padSTRING('R',L_hstRMMST.get(L_rstRSSET.getString("QP_QPRCD")+L_rstRSSET.getString("QP_QPRSL")).toString(),35)+" ");
					//}
					//else 
					//L_stbDATA.append(padSTRING('R',"",35)+" ");
					strUOMDS = padSTRING('R',strUOMDS,15);					        
					L_stbDATA.append(padSTRING('R',strUOMDS,15));					        
					//L_stbDATA.append(padSTRING('R',strTSMDS,17));
					L_stbDATA.append(padSTRING('R',"",22));
					L_TMP = "";
									
					if((bgdNPFVL != null)&& (bgdNPTVL != null) && (strRSLVL != null))
					{
						if(strCMPFL.equals("Y"))
						{
							if(strRSLVL.equals("-"))
							{
								setMSG("All the result values are not available,test certificate can not be generated..",'E');
								//*//flgREPFL = false;
							}
							else
							{
								setMSG("",'N');
								if(!chkQPRRNG(bgdNPFVL.floatValue(),bgdNPTVL.floatValue(),strCMPFL,Float.valueOf(strRSLVL).floatValue()))
								{
									//*//flgREPFL = false;
								}
							}
						}
						else if((strCMPFL.equals("N"))&&(strTXTVL.length() >0))
						{
											
							if(Integer.parseInt(strRSLVL) == 1) 
								strRSLVL = strTXTVL;
							else if(Integer.parseInt(strRSLVL) == 0)
								strRSLVL = "Not "+strTXTVL;
						}
					}
					if(!strSTSFL.equals("2")) // if not a prime grade
						L_TMP = padSTRING('R',"-",15); 							
					else
					{
						if((bgdNPFVL == null)&&(bgdNPTVL == null))										
							L_TMP = padSTRING('R',"-",15);								
						else if(bgdNPFVL.floatValue() == 0.0)
						{
							if(bgdNPTVL.floatValue() == 0.0)
								L_TMP = padSTRING('R',"-",15);										
							else
							   L_TMP = padSTRING('R',bgdNPTVL.intValue() + " max",15);										
																		
						}
						else
						{
							if(bgdNPTVL.floatValue()== 0.0)
								L_TMP =padSTRING('R',bgdNPFVL.intValue()+" min",15);										
							else 
							{
								if(bgdNPFVL.floatValue()== bgdNPTVL.floatValue())
									L_TMP = padSTRING('R',bgdNPFVL.floatValue()+"",15);
								else
									//L_TMP = padSTRING('R',setNumberFormat(bgdNPFVL.floatValue(),L_intDECVL) + " - "+setNumberFormat(bgdNPTVL.floatValue(),L_intDECVL),15);
									L_TMP = padSTRING('R',setNumberFormat(bgdNPFVL.floatValue(),3) + "   "+setNumberFormat(bgdNPTVL.floatValue(),3),15);
							}
						}
							
					}					       
					L_stbDATA.append(padSTRING('R',L_TMP,15));					        
					L_stbDATA.append(padSTRING('L',strRSLVL,9)+"\n");
				}
				L_rstRSSET.close();
				
				////for printing DE_overall deviation//////////////////////////
				L_stbDATA.append("\n");
				L_stbDATA.append(padSTRING('R',"",3));
				L_stbDATA.append(padSTRING('R',"DE_ OverAll Deviation",72)+" ");
				L_stbDATA.append(padSTRING('R',strTXTVL_DE,15));					        
				L_stbDATA.append(padSTRING('L',strRSLVL_DE,9)+"\n");
				//////////////////////////////////////////////////////////////				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINTCLR");
		}
		
	}
	
	/**
	Method to validate Data, before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{	
		    cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;		
			if(txtFMLOT.getText().length()< 8)			
			{
			    if(txtFMLOT.getText().length() == 0)
				    setMSG("Please Enter Lot Number to generate the Report.. ",'E');
				else
				    setMSG("Invalid value of from Lot No.. ",'E');
				txtFMLOT.requestFocus();
				return false;
			}		
			else if(txtTOLOT.getText().length()< 8)			
			{
			    if(txtTOLOT.getText().length() == 0)
				    setMSG("Please Enter Lot Number to generate the Report.. ",'E');
				else
				    setMSG("Invalid value of TO Lot No.. ",'E');
				txtTOLOT.requestFocus();
				return false;
			}		
			if(txtRCLNO.getText().length()==0)			
		    {
				txtRCLNO.setText("00");
				strRCLNO = "00";				
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		String L_strPRDGR="";
		try
		{	
			
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;	
			L_strPRDGR = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",strPRDCD.substring(0,4)+"00000A");
			if(M_rdbHTML.isSelected())
			{	
			     //////
                // dosREPORT.writeBytes("<IMG Style =\"Position:Absolute;Top:5%;Left:45%\" Src =\"F:\\exec\\splerp1\\splerp1.4\\spllogo.gif\">");
	             ///////				
				dosREPORT.writeBytes("<Center><B>");
				dosREPORT.writeBytes("</STYLE>");
				dosREPORT.writeBytes("<PRE style =\" font-size : 14 pt \">");
				//dosREPORT.writeBytes("<H1>");				
				//dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n");

				
				/*dosREPORT.writeBytes("<PRE style =\" font-size : 9 pt \">");
				M_strSQLQRY = " Select CP_ADR01,CP_ADR03,CP_ADR04 from co_cpmst where CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				M_rstRSSET.next();
				dosREPORT.writeBytes(M_rstRSSET.getString("CP_ADR01")+","+M_rstRSSET.getString("CP_ADR03")+","+M_rstRSSET.getString("CP_ADR04")+","+"INDIA"+"\n");
				*/
				
				
				
				//dosREPORT.writeBytes("QUALITY CONTROL LABORATORY"+"\n");
				dosREPORT.writeBytes("<PRE style =\" font-size : 14 pt \">");
				dosREPORT.writeBytes("\n\n\n\n\n\n\n");
				dosREPORT.writeBytes("PRODUCT TEST CERTIFICATE"+"\n\n\n");
			//	dosREPORT.writeBytes("</H3>"); 
				dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("</STYLE>");
				dosREPORT.writeBytes("<PRE style =\" font-size : 8 pt \">");    
				dosREPORT.writeBytes("</Center>");				
				//dosREPORT.writeBytes(padSTRING('R',"   Grade  : "+L_strPRDGR+"  "+strPRDDS,75));
				dosREPORT.writeBytes(padSTRING('R',"   Grade  : "+strPRDDS,75));
				dosREPORT.writeBytes(padSTRING('L',strISODOC1,30)+"\n");
				
				dosREPORT.writeBytes(padSTRING('R',"Lot No.: " + strLOTNO,75));
				dosREPORT.writeBytes(padSTRING('L',strISODOC2,30)+"\n");
				
				dosREPORT.writeBytes(padSTRING('R',"Report Date : " + cl_dat.M_strLOGDT_pbst,75));
				dosREPORT.writeBytes(padSTRING('L',strISODOC3,30)+"\n");
				dosREPORT.writeBytes("   ");
				dosREPORT.writeBytes("<B><HR SIZE = \"1\" COLOR = \"BLACK\"></B>"+"\n");
				dosREPORT.writeBytes("   TEST PARAMETERS                     UNITS          TEST METHOD           SPECS            RESULTS\n");
				dosREPORT.writeBytes("   TEST CONDITIONS                                (ASTM Edition-2000) \n");
				dosREPORT.writeBytes("   "); 
				dosREPORT.writeBytes("<B><HR SIZE = \"1\" COLOR = \"BLACK\"></B>"+"\n\n");
				
			//	dosREPORT.writeBytes("<PRE style =\" font-size : 9 pt \">");    
			}
			else
			{
				dosREPORT.writeBytes("\n\n\n\n\n");
				dosREPORT.writeBytes(padSTRING('L',"------------------------------",96)+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISODOC1,96)+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISODOC2,96)+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISODOC3,96)+"\n");
				dosREPORT.writeBytes(padSTRING('L',"------------------------------",96)+"\n");
			
				dosREPORT.writeBytes("   SUPREME PETROCHEM LIMITED"+"\n");									
				dosREPORT.writeBytes("   PRODUCT TEST CERTIFICATE"+"\n\n");			
				dosREPORT.writeBytes("   Grade  : "+L_strPRDGR+"  "+strPRDDS+"\n");			
				dosREPORT.writeBytes(padSTRING('L',"   ",3));
				dosREPORT.writeBytes(padSTRING('R',"Lot No.: " + strLOTNO,72));
				dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n\n");
				dosREPORT.writeBytes("   --------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes("   TEST PARAMETERS                     UNITS          TEST METHOD      SPECS            RESULTS\n");
				dosREPORT.writeBytes("   TEST CONDITIONS                                (ASTM Edition-2000) \n");
				dosREPORT.writeBytes("   --------------------------------------------------------------------------------------------\n\n");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}		
	public boolean chkQPRRNG(float P_fltNPFVL,float P_fltNPTVL,String P_strCMPFL,float P_fltQPRVL)
	{
		// if parameter is going out of range, it will return false.
		if (P_strCMPFL.trim().equals("Y"))
		{
		   if ((P_fltNPFVL != 0) && (P_fltNPTVL == 0))
		   {
		      if (P_fltQPRVL < P_fltNPFVL)
				  return false;
		   }
		   else if ((P_fltNPFVL == 0) && (P_fltNPTVL != 0))
		   {
		      if (P_fltQPRVL > P_fltNPTVL)
		        return false;
		   }
		   else
		   {
		      if (( P_fltQPRVL < P_fltNPFVL) || (P_fltQPRVL > P_fltNPTVL))
		         return false;

		   }
									 
		}
		return true; // within range 
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if(input == txtFMLOT)
				{
					if (txtFMLOT.getText().trim().length() == 8 )
					{	
                        if(txtTOLOT.getText().trim().length()>0)
						{
							if(Integer.parseInt(txtFMLOT.getText().trim()) >Integer.parseInt(txtTOLOT.getText().trim()))
							{
								setMSG("From Lot Number must be samller then To Lot Number..",'E');
								return false;										
							}
                        }
                        M_strSQLQRY = "select LT_LOTNO,PR_STSFL from PR_LTMST,CO_PRMST where ";
                        M_strSQLQRY +=" LT_PRDCD = PR_PRDCD AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL ='9' AND PR_STSFL <>'4'";
					    M_strSQLQRY += "AND LT_PRDTP ='"+ strPRDTP+"'";																																			
						M_strSQLQRY += " AND LT_LOTNO ='"+ txtFMLOT.getText().trim()+"'";																																			
						//System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
						if (M_rstRSSET != null)
						{  
							if(!M_rstRSSET.next())
							{
								M_rstRSSET.close();
								setMSG("Invalid Lot Number, please try another ..",'E');
								return false;				
							}							
						}						
					}
				}
				if(input == txtTOLOT)
				{					
					if (txtTOLOT.getText().trim().length() == 8)
					{	
                        if(txtFMLOT.getText().trim().length()>0)
						{
							if(Integer.parseInt(txtFMLOT.getText().trim()) >Integer.parseInt(txtTOLOT.getText().trim()))
							{										
								setMSG("From Lot Number must be samller then To Lot Number..",'E');
								return false;
							}
						}
					    M_strSQLQRY = "select LT_LOTNO,PR_STSFL from PR_LTMST,CO_PRMST where ";
                        M_strSQLQRY +=" LT_PRDCD = PR_PRDCD AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CLSFL ='9' AND PR_STSFL <>'4'";
					    M_strSQLQRY += "AND LT_PRDTP ='"+ strPRDTP+"'";																																			
						M_strSQLQRY += "AND LT_LOTNO ='"+ txtTOLOT.getText().trim()+"'";																																			
						//System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
						if (M_rstRSSET != null)
						{  
							if(!M_rstRSSET.next())
							{									
								setMSG("Invalid Lot Number, please try another ..",'E');
								return false;				
							}	
							M_rstRSSET.close();
						}				
					}
				}				
				if(input == txtGRPCD)
				{
					if (txtGRPCD.getText().trim().length() > 0)
					{	
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN"
						+" where CMT_CGMTP='SYS' AND CMT_CGSTP = 'QCXXTCG' AND CMT_CODCD='"+ txtGRPCD.getText().trim()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())							
								return true;							
							else if(txtGRPCD.getText().trim().equals("00000"))
								return true;
							else
							{
								M_rstRSSET.close();
								setMSG("Invalid Group Code, please try another ..",'E');
								return false;				
							}							
						}				
					}
				}								
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}
}
