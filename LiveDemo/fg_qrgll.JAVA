/*
System Name   : Finished Goods Inventory Management System
Program Name  : Grade,LotNo & Locationwise Details
Program Desc. : 1)Gives detail about the Grade,LotNo & Location within the Stock Master.
				2)Gives Detail regarding Loading Advice No. as per the Lotno ,Grade or 
				  Location entered.
Author        : Mr. Zaheer Alam Khan
Date          : 04 September 2006
Version       : FIMS 2.0.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/


import java.sql.ResultSet;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JTable;import java.util.Hashtable;import javax.swing.JComboBox;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;import java.util.Date;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import javax.swing.JPanel;import java.awt.Color;
import javax.swing.JButton;


/**<pre>
<b>System Name :</b> Finished Goods Inventory Management System.
 
<b>Program Name :</b>Grade,LotNo & Locationwise Details

<b>Purpose :</b> Gives Detail regarding Loading Advice No. as per the Lotno ,Grade or 
				  Location entered.

List of tables used :FG_STMST,CO_PRMST,PR_LTMST,FG_ISTRN
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
FG_STMST    ST_WRHTP,ST_PRDTP,ST_LOTNO,                             #
            ST_RCLNO,ST_PKGTP,ST_MNLCD                    
           

FG_ISTRN    IST_WRHTP,IST_ISSTP,IST_ISSNO,                          #
            IST_PRDTP,IST_LOTNO,IST_RCLNO,
            IST_PRDCD,IST_PKGTP,IST_MNLCD
PR_LTMST    LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
CO_PRMST    PR_PRDCD                                                #
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table Name      Type/Size     Description
--------------------------------------------------------------------------------
txtFMGRD    PR_PRDDS       FG_STMST       Varchar        From Grade
txtTOGRD    PR_PRDDS       FG_STMST       Varchar        To Grade
txtFMLOT    ST_LOTNO       FG_STMST       Varchar        From Lot Number
txtTOLOT    ST_LOTNO       FG_STMST       Varchar        To Lot Number
txtFMLOC    ST_MNLCD       FG_STMST       Varchar        From Location
txtTOLOC    ST_MNLCD       FG_STMST       Varchar        To Location
txtPKGTP    ST_PKGTP       FG_STMST       Varchar        Package Type

--------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description               Display Columns            Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtFMGRD    Product Code                      PR_PRDDS                   FG_STMST
txtTOGRD    Product Code                      PR_PRDDS                   FG_STMST
txtFMLOT    Lot Number                        ST_LOTNO                   FG_STMST
txtTOLOT    Lot Number                        ST_LOTNO                   FG_STMST
txtFMLOC    Location                          ST_MNLCD                   FG_STMST
txtFMLOC    Location                          ST_MNLCD                   FG_STMST
txtPKGTP    Package Type And Description      ST_PKGTP                   FG_STMST
-----------------------------------------------------------------------------------------------------------------------------------------------------
<I>
<B>Conditions Give in Query:</b>
            1) For Table 1 data is taken from  FG_STMST,CO_PRMST,PR_LTMST between the 
               given Grade ,Location,Lot Number and specific Package type on this condition
        
            1.a)ST_PRDCD=PR_PRDCD.
            1.b)and ST_PRDTP=LT_PRDTP.
            1.c)and ST_LOTNO=LT_LOTNO.
            1.d)and ST_RCLNO=LT_RCLNO.
            1.f)and ST_STKQT+ST_UCLQT) > 0. 
            
            2) For Table 2 data is taken from  FG_ISTRN,CO_PRMST,PR_LTMST between the 
               given Grade ,Location,Lot Number and specific Package type on this condition
       
            2.a)IST_PRDCD=PR_PRDCD.
            2.b)and IST_STSFL <> 'X'.
            

</I>

 */


public class fg_qrgll extends cl_rbase
{                                      /** JtextField For To Grade     */ 
	private JTextField txtTOGRD;        /** JtextField For From Grade   */
	private JTextField txtFMGRD;       /** JtextField For To Lot Number */ 
	private JTextField txtTOLOT;       /** JtextField For From Lot Number*/ 
	private JTextField txtFMLOT;      /**  JTextField for To Location    */
	private JTextField txtTOLOC;      /**  JTextField for from Location    */
	private JTextField txtFMLOC;      /**  JTextField for Package Type */
	private JTextField txtPKGTP;      /** JButton for Execute The Report */
	private JButton btnRUN;           /** Jtable For show the Record   */
	private cl_JTable TBL_STKDTL;     /** Jtable For show the Record    */ 
	private cl_JTable TBL_ISUDTL;
	
										 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ; /** Integer Variable to count Page Number */
	private int intPAGENO=0;		
	
	
	String strGRDBT1 = "";
	String strLOTBT1 = "";
	String strLOCBT1 = "";
	String strPKGTP1 = "";
	String strORDBY1 = "";
	String strORDBY2 = "";
	String strLOTBT2 = "";
	String strLOCBT2 = "";
	String strPKGTP2 = "";
	String strGRDBT2 = "";
	private final int TB1_GRADE=1;
	private final int TB1_RESFL=2;
	private final int TB1_LOTNO=3;
	private final int TB1_RCLNO=4;
	private final int TB1_STKQT=5;
	private final int TB1_UCLQT=6;
	private final int TB1_MNLCD=7;
	private final int TB1_PKGTP=8;
	private final int TB1_RETQT=9;
	private final int TB1_STSFL=10;
	private final int TB1_PSTDT=11;
	
	
	private final int TB2_ISSNO=1;
	private final int TB2_LOTNO=2;
	private final int TB2_RCLNO=3;
	private final int TB2_MNLCD=4;
	private final int TB2_PKGTP=5;
	private final int TB2_ISSQT=6;
	private final int TB2_AUTDT=7;
	private final int TB2_STSFL=8;
	
	private String strISODC1="";      /**String  variable for ISO value          */
	private String strISODC2="";      /**String  variable for ISO value          */
	private String strISODC3="";      /**String  variable for ISO value          */
	private Hashtable<String,String> hstGENCD=new Hashtable<String,String>(); /** Hash Table for storing ISO Variable   */
	private Hashtable<String,String> hstPKGTP=new Hashtable<String,String>();  /** Hashtable for Storing Package Type And value */
	
	fg_qrgll()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			hstPKGTP.clear();
			hstGENCD.clear();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Starting From"),2,3,1,1,this,'L');
			add(new JLabel("Ending To"),2,4,1,1,this,'L');
			add(new JLabel("Package Type"),2,5,1,1,this,'L');
			add(new JLabel("Grade"),3,2,1,1,this,'L');
			add(txtFMGRD = new TxtLimit(40),3,3,1,1,this,'R');

			add(txtTOGRD = new TxtLimit(40),3,4,1,1,this,'R');
			add(txtPKGTP = new TxtLimit(2),3,5,1,1,this,'R');
			
			add(new JLabel("Lot No"),4,2,1,1,this,'L');
			add(txtFMLOT = new TxtLimit(8),4,3,1,1,this,'R');

			add(txtTOLOT = new TxtLimit(8),4,4,1,1,this,'R');
			
			add(new JLabel("Location"),5,2,1,1,this,'L');
			add(txtFMLOC = new TxtLimit(5),5,3,1,1,this,'R');

			add(txtTOLOC = new TxtLimit(5),5,4,1,1,this,'R');
			
			add(btnRUN = new JButton("Run"),5,6,1,1,this,'L');
			
			String[] L_staSTKDL = {"Select","Grades","Quality Hold Flag","Lot No","Rcl NO","Classified Qty","Unclassified Qty","Main Loc.","Pkg. Type","Reten Qty", "Status","Prd.Date"};
			int[] L_intCOLSZ = {20,100,30,90,70,80,100,70,70,80,60,100};
			TBL_STKDTL = crtTBLPNL1(this,L_staSTKDL,5000,6,1,5.8,7.9,L_intCOLSZ,new int[]{0});
		
			String[] L_staISUDL = {"Select","Loding Advice","Lot No.","Rcl. No","Main Loc","Pkg.Type","Issue Qty","Issue Date","status"};
			int[] L_intCOLSZ1 = {20,90,90,90,90,90,90,90,90};
			TBL_ISUDTL = crtTBLPNL1(this,L_staISUDL,20000,13,1,5.8,7.9,L_intCOLSZ1,new int[]{0});
			
			M_strSQLQRY =  " Select CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ( 'ISOFGXXRPT','SYSFGXXPKG')";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	

			if(M_rstRSSET != null)   
			{
		    	while(M_rstRSSET.next())
        		{  
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("FGXXPKG"))
						hstPKGTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					else
						hstGENCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
			}
			strISODC1 = hstGENCD.get("FG_RPRCM01").toString();
			strISODC2 = hstGENCD.get("FG_RPRCM02").toString();
			strISODC3 = hstGENCD.get("FG_RPRCM03").toString();
						
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constractor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == btnRUN)
			{
				
				TBL_STKDTL.clrTABLE();
				TBL_ISUDTL.clrTABLE();
				getQRSTR();
				
				showTABL1();
				
				showTABL2();
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"actionPerformed");
			
		}
	}
	/**
	 * Super class Method inherited to enable & disable the Components.
	 * @param P_flgSTATE boolean variable to pass state for the Components.
	 */
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		TBL_STKDTL.setEnabled(false);
		TBL_ISUDTL.setEnabled(false);
	}
	
	/**
		* Method to print, display report as per selection, 
		* This function is called from Display Button
	*/
	public void exePRINT()
	{
		try
		{
			intRECCT=0;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
			{
				if(M_rdbHTML.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst +M_txtDESTN.getText().trim()+".html";
				else if(M_rdbTEXT.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + M_txtDESTN.getText().trim()+".doc";
			}
			else
			{
				if(M_rdbHTML.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst +"fg_qrgll.html";
				else if(M_rdbTEXT.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + "fg_qrgll.doc";
			}
				
			getDATA();
			dosREPORT.close();
			fosREPORT.close();
			setMSG("Report Completed.......",'N');
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
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
				//System.out.println("IN E MAIL = "+strFILNM);
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{					
					//System.out.println("File Name  = "+strFILNM);
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Details"," ");
					//System.out.println("IN E MAIL Address =  "+M_cmbDESTN.getItemAt(i).toString().trim());
					
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					//System.out.println(" AFter IN E MAIL = "+ M_cmbDESTN.getSelectedItem());
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	
	/**
	 *  
	*/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			getQRSTR();
			if(M_objSOURC == txtFMGRD)
			{
				M_strHLPFLD  = "txtFMGRD";
				M_strSQLQRY  =  "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_PRDCD=ST_PRDCD ";
				M_strSQLQRY +=   strLOTBT1 + strLOCBT1;
				
				if(txtFMGRD.getText().trim().length() > 0)
					M_strSQLQRY += " AND PR_PRDDS LIKE '"+txtFMGRD.getText().trim()+"%'";
				//System.out.println( "M_strSQLQRY =  "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade"},1,"CT");
			}
						
			if(M_objSOURC == txtTOGRD)
			{
				M_strHLPFLD  = "txtTOGRD";
				M_strSQLQRY  =  "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_PRDCD=ST_PRDCD";
				M_strSQLQRY +=  " AND PR_PRDDS >='"+txtFMGRD.getText().trim()+"'";
				
				//System.out.println( "M_strSQLQRY to =  "+M_strSQLQRY);
				if(txtTOGRD.getText().trim().length() > 0)
					M_strSQLQRY += " AND PR_PRDDS LIKE '"+txtTOGRD.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Grade"},1,"CT");
			}
			
			
			if(M_objSOURC == txtFMLOT)
			{
				M_strHLPFLD  = "txtFMLOT";
				M_strSQLQRY = "Select distinct ST_LOTNO from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD ";
				M_strSQLQRY += strGRDBT1 + strLOCBT1 + strPKGTP1;
				//System.out.println( "M_strSQLQRY =  "+M_strSQLQRY);
				if(txtFMLOT.getText().trim().length() > 0)
					M_strSQLQRY += " AND ST_LOTNO LIKE '"+txtFMLOT.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No."},1,"CT");
			}
			
			if(M_objSOURC == txtTOLOT)
			{
				M_strHLPFLD  = "txtTOLOT";
				M_strSQLQRY = "Select distinct ST_LOTNO from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD";
				M_strSQLQRY +=  " and ST_LOTNO  >= '"+txtFMLOT.getText().trim()+"'";
				M_strSQLQRY += strGRDBT1 + strLOCBT1 + strPKGTP1;
				//System.out.println( "M_strSQLQRY =  "+M_strSQLQRY);
				if(txtTOLOT.getText().trim().length() > 0)
					M_strSQLQRY += " AND ST_LOTNO LIKE '"+txtTOLOT.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No."},1,"CT");
			}
			
			if(M_objSOURC == txtFMLOC)
			{
				M_strHLPFLD  = "txtFMLOC";
				M_strSQLQRY = "Select distinct ST_MNLCD from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD ";
				M_strSQLQRY +=  strGRDBT1 + strLOTBT1 + strPKGTP1;
				if(txtFMLOC.getText().trim().length() > 0)
					M_strSQLQRY += " AND ST_MNLCD LIKE '"+txtFMLOC.getText().trim()+"%'";
				//System.out.println( "M_strSQLQRY  FMLOC=  "+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Location"},1,"CT");
			}
			
			if(M_objSOURC == txtTOLOC)
			{
				M_strHLPFLD  = "txtTOLOC";
				M_strSQLQRY = "Select distinct ST_MNLCD from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD ";
				M_strSQLQRY +=  " and ST_MNLCD  >= '"+txtFMLOC.getText().trim()+"' ";
				M_strSQLQRY +=  strGRDBT1 + strLOTBT1 + strPKGTP1;
				//System.out.println( "M_strSQLQRY TOLOC =  "+M_strSQLQRY);
				if(txtTOLOC.getText().trim().length() > 0)
					M_strSQLQRY += " AND ST_MNLCD LIKE '"+txtTOLOC.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Location"},1,"CT");
			}
			if(M_objSOURC == txtPKGTP)
			{
				M_strHLPFLD  = "txtPKGTP";
				M_strSQLQRY = "Select distinct ST_PKGTP,CMT_CODDS from FG_STMST,CO_PRMST,CO_CDTRN where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD";
				M_strSQLQRY += " and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and cmt_codcd=st_pkgtp";
				M_strSQLQRY += strGRDBT1 + strLOTBT1 + strLOCBT1;
				if(txtPKGTP.getText().trim().length() > 0)
					M_strSQLQRY += " AND ST_PKGTP LIKE '"+txtPKGTP.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Package Type.","Description"},2,"CT");
			
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				getQRSTR();
				
				if(M_objSOURC == txtFMGRD)
				{
					if(txtFMGRD.getText().trim().length()==0)
						txtPKGTP.requestFocus();

					else
					{
						txtFMGRD.setText(txtFMGRD.getText().trim());
						M_strSQLQRY  =  "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_PRDCD=ST_PRDCD  AND PR_PRDDS='"+txtFMGRD.getText().trim()+"'";
						M_strSQLQRY +=   strLOTBT1 + strLOCBT1;
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET !=null && M_rstRSSET.next())
						{
							txtTOGRD.setText(txtFMGRD.getText().trim());
							txtTOGRD.requestFocus();
						}
						else
						{
							setMSG("Invalid Grade ",'E');
							txtFMGRD.requestFocus();
						}
					}
				}
				if(M_objSOURC == txtTOGRD)
				{
					txtTOGRD.setText(txtTOGRD.getText().trim());
					M_strSQLQRY  =  "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PR_PRDCD=ST_PRDCD  AND PR_PRDDS='"+txtTOGRD.getText().trim()+"'";
					M_strSQLQRY +=   strLOTBT1 + strLOCBT1;
					M_strSQLQRY +=  " AND PR_PRDDS >='"+txtFMGRD.getText().trim()+"'";
					
					//System.out.println( "M_strSQLQRY to =  "+M_strSQLQRY);
					
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null && M_rstRSSET.next())
					{
						txtPKGTP.requestFocus();

					}
					else
					{
						setMSG("Invalid Grade ",'E');
						txtTOGRD.requestFocus();
					}
					
				}
				if(M_objSOURC == txtPKGTP)
				{
					if(txtPKGTP.getText().trim().length()==0)
						txtFMLOT.requestFocus();
					else
					{
						M_strSQLQRY = "Select ST_PKGTP,CMT_CODDS from FG_STMST,CO_PRMST,CO_CDTRN where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD";
						M_strSQLQRY += " and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and cmt_codcd=st_pkgtp and ST_PKGTP='"+txtPKGTP.getText().trim()+"'  ";
						M_strSQLQRY += strGRDBT1 + strLOTBT1 + strLOCBT1;
						//System.out.println( "M_strSQLQRY pkgtp =  "+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET !=null && M_rstRSSET.next())
						{
							txtFMLOT.requestFocus();
						}
						else
						{
							setMSG("Invalid Package Type ",'E');
							txtPKGTP.requestFocus();
						}
					}
					
				}
				if(M_objSOURC == txtFMLOT)
				{
					if(txtFMLOT.getText().trim().length()==0)
						txtFMLOC.requestFocus();

					else
					{
						M_strSQLQRY = "Select ST_LOTNO from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD  AND ST_LOTNO='"+txtFMLOT.getText().trim()+"' ";
						M_strSQLQRY += strGRDBT1 + strLOCBT1 + strPKGTP1;
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET !=null && M_rstRSSET.next())
						{
							txtTOLOT.setText(txtFMLOT.getText().trim());
							txtTOLOT.requestFocus();
						}
						else
						{
							setMSG("Invalid Lot Number ",'E');
							txtFMLOT.requestFocus();
						}
					}
				}
				if(M_objSOURC == txtTOLOT)
				{
					M_strSQLQRY = "Select distinct ST_LOTNO from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD AND ST_LOTNO='"+txtTOLOT.getText().trim()+"' ";
					M_strSQLQRY +=  " and ST_LOTNO  >= '"+txtFMLOT.getText().trim()+"'";
					M_strSQLQRY += strGRDBT1 + strLOCBT1 + strPKGTP1;
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null && M_rstRSSET.next())
					{
						txtFMLOC.requestFocus();
					}
					else
					{
						setMSG("Invalid Lot Number ",'E');
						txtTOLOT.requestFocus();
					}
					
					
				}
				if(M_objSOURC == txtFMLOC)
				{
					if(txtFMLOC.getText().trim().length()==0)
						btnRUN.requestFocus();

					else
					{
						txtFMLOC.setText(txtFMLOC.getText().trim());
						M_strSQLQRY = "Select  ST_MNLCD from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD  AND ST_MNLCD= '"+txtFMLOC.getText().trim()+"' ";
						M_strSQLQRY +=  strGRDBT1 + strLOTBT1 + strPKGTP1;
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET !=null && M_rstRSSET.next())
						{
							txtTOLOC.setText(txtFMLOC.getText().trim());
							txtTOLOC.requestFocus();
						}
						else
						{
							setMSG("Invalid Location ",'E');
							txtFMLOC.requestFocus();
						}
					}
					
					
				}
				if(M_objSOURC==txtTOLOC)
				{
					txtTOLOC.setText(txtTOLOC.getText().trim());
					M_strSQLQRY = "Select  ST_MNLCD from FG_STMST,CO_PRMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD AND ST_MNLCD= '"+txtTOLOC.getText().trim()+"'  ";
					M_strSQLQRY +=  " and ST_MNLCD  >= '"+txtFMLOC.getText().trim()+"' ";
					M_strSQLQRY +=  strGRDBT1 + strLOTBT1 + strPKGTP1;
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null && M_rstRSSET.next())
					{
						btnRUN.requestFocus();
					}
					else
					{
						setMSG("Invalid Location ",'E');
						txtTOLOC.requestFocus();
					}
					
				}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
			}
			catch(Exception e)
			{
				setMSG(e,"ENTER_KEY Press");
			}
			
		}
	}
	
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMGRD")
		{
			txtFMGRD.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOGRD.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtTOGRD")
		{
			txtTOGRD.setText(cl_dat.M_strHLPSTR_pbst);
			//txtTOGRD.setText(cl_dat.M_strHLPSTR_pbst);
		}
		
		if(M_strHLPFLD == "txtFMLOT")
		{
			txtFMLOT.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOLOT.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtTOLOT")
		{
			txtTOLOT.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtFMLOC")
		{
			txtFMLOC.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOLOC.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtTOLOC")
		{
			txtTOLOC.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtPKGTP")
		{
			txtPKGTP.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
	/**
	 *Method to fetch Data from Database and start creation of the output file.
	*/
	
	private void getDATA()
	{
		
		try
		{
			getQRSTR();
			boolean flgRESFL = false;
			double L_dblCLFQT=0;
			double L_dblUCLQT=0;
			double L_dblTOTCL=0;
			double L_dblTOTUN=0;
			double L_dblGRTOT=0;
			double L_dblCLGRT=0;
			double L_dblUNGRT=0;
			int L_intLOCCT=0;
	
			String L_strSTSFL="",L_strREMDS="";
			String L_strPKGTP="",L_strPKGTP1="";
			String L_strPRDCD="",L_strPRDCD1="";
			
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
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>UnClassified Stock Statement</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();	//gets the Header of the report		
			
			M_strSQLQRY = "Select PR_PRDDS,isnull(LT_RESFL,'') LT_RESFL,ST_LOTNO,ST_RCLNO,ST_MNLCD,ST_STKQT,ST_UCLQT,ST_PKGTP,ST_REMDS,ST_PRDCD,ST_STSFL from FG_STMST,CO_PRMST,PR_LTMST";
			M_strSQLQRY += " where ST_PRDCD=PR_PRDCD and ST_CMPCD=LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (ST_STKQT+ST_UCLQT) > 0";
			M_strSQLQRY += strGRDBT1 + strLOTBT1 + strLOCBT1 + strPKGTP1 + " '";//+ strORDBY1;
			M_strSQLQRY = M_strSQLQRY.substring(0,M_strSQLQRY.length()-1).trim();
			//System.out.println("Getdata = "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strPRDCD=nvlSTRVL(M_rstRSSET.getString("ST_PRDCD"),"");
					
					if(cl_dat.M_intLINNO_pbst >= 62)
					{
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");		
						cl_dat.M_intLINNO_pbst = 0;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER(); //gets the Header of the report
					}
					
					if(!L_strPRDCD.equals(L_strPRDCD1))
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes(padSTRING('R'," ",5));	
						if(intRECCT==0)
						{
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""),30));	
						}
						else
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R'," ",5));
							dosREPORT.writeBytes(padSTRING('R',"Grade Total :",14));	
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTCL,3),14));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTUN,3),13));
							dosREPORT.writeBytes("\n\n"); 
							cl_dat.M_intLINNO_pbst +=3;
							dosREPORT.writeBytes(padSTRING('R'," ",5));	
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""),30));	
							L_dblTOTUN=0;
							L_dblTOTCL=0;							
						}						
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						dosREPORT.writeBytes("\n");   
						cl_dat.M_intLINNO_pbst +=1;
					}
					if(M_rstRSSET.getString("LT_RESFL").equals("Q"))
					{
						dosREPORT.writeBytes(padSTRING('R',"*",5));	
						flgRESFL=true;
					}
					else if(M_rstRSSET.getString("LT_RESFL").equals("H"))
					{
						dosREPORT.writeBytes(padSTRING('R',"#",5));	
						flgRESFL=true;
					}
					else
						dosREPORT.writeBytes(padSTRING('R'," ",5));	
						
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_LOTNO"),""),12));	
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MNLCD"),""),6));
					
					L_dblCLFQT=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCLFQT,3),10));
					L_dblUCLQT=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ST_UCLQT"),"0"));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblUCLQT,3),13));
					 dosREPORT.writeBytes(padSTRING('R'," ",3));	
					L_strPKGTP=nvlSTRVL(M_rstRSSET.getString("ST_PKGTP"),"");	
					if(hstPKGTP.containsKey(L_strPKGTP))
					   dosREPORT.writeBytes(padSTRING('R',hstPKGTP.get(L_strPKGTP).toString(),15));	
					else
						 dosREPORT.writeBytes(padSTRING('R'," ",15));
					L_strREMDS = M_rstRSSET.getString("ST_REMDS");
					L_strSTSFL = M_rstRSSET.getString("ST_STSFL");
                    L_strREMDS = L_strSTSFL.equals("2") ? L_strREMDS+" Sales Return" : L_strREMDS;

					dosREPORT.writeBytes(padSTRING('R',L_strREMDS,25));	
					
					L_strPRDCD1=L_strPRDCD;
					L_dblTOTCL += L_dblCLFQT;
					L_dblTOTUN += L_dblUCLQT;
					
					L_dblCLGRT +=L_dblCLFQT;
					L_dblUNGRT +=L_dblUCLQT;
					
					dosREPORT.writeBytes("\n");  
					cl_dat.M_intLINNO_pbst +=1;
					intRECCT++;
				}
				if(intRECCT>0)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("\n");  
					dosREPORT.writeBytes(padSTRING('R'," ",5));	
					dosREPORT.writeBytes(padSTRING('R',"Grade Total :",14));	
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTCL,3),14));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTUN,3),13));
					dosREPORT.writeBytes("\n\n");  
					dosREPORT.writeBytes(padSTRING('R'," ",5));	
					dosREPORT.writeBytes(padSTRING('R',"Grand Total :",14));	
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblCLGRT,3),14));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblUNGRT,3),13));
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
				}
				if(flgRESFL==true)
				{
					dosREPORT.writeBytes("\n\n");  
					dosREPORT.writeBytes(padSTRING('R'," [*] or [#] : (Quality Hold for Despatch) ",50));	
				}
			}
			   
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			
			//if(dosREPORT !=null)
			//	dosREPORT.close();
			//if(fosREPORT !=null)
			//	fosREPORT.close();
			
			
		}
		catch(Exception L_EX)
		{
		
			setMSG(L_EX  + " GetDATA",'E');
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
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",66));
			dosREPORT.writeBytes("------------------------------\n");
			dosREPORT.writeBytes(padSTRING('L'," ",66));
			dosREPORT.writeBytes(strISODC1+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",66));
			dosREPORT.writeBytes(strISODC2+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",66));
			dosREPORT.writeBytes(strISODC3+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",66));
			dosREPORT.writeBytes("------------------------------\n");
			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,66));
			dosREPORT.writeBytes("Report Date :"+ cl_dat.M_strLOGDT_pbst + "\n");
			dosREPORT.writeBytes(padSTRING('R',"Stock Detail",66));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("     Grade \n");      
			dosREPORT.writeBytes("     Lot No.     Loc    Clsfd.Qty  Unclsfd.Qty   Pkg.Category   Remark            \n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");		
			
			
			cl_dat.M_intLINNO_pbst += 12;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
	
	/**
	 *Method to Assign value for variable, which used as Between Grade, Lot Number, Location And Package Type .
	*/
	private void getQRSTR()
	{
		try
		{
			String L_strFMGRD,L_strTOGRD,L_strFMLOT,L_strTOLOT,L_strFMLOC,L_strTOLOC;
			String L_strPKGTP;
			strGRDBT1 = "";
			strLOTBT1 = "";
			strLOCBT1 = "";
			strPKGTP1 = "";
			strGRDBT2 = "";
			strLOTBT2 = "";
			strLOCBT2 = "";
			strPKGTP2 = "";
			strORDBY2 = " order by ";
			strORDBY1 = " order by ";
			L_strFMGRD = txtFMGRD.getText().toString().trim();
			L_strTOGRD = txtTOGRD.getText().toString().trim();
			L_strFMLOT = txtFMLOT.getText().toString().trim();
			L_strTOLOT = txtTOLOT.getText().toString().trim();
			L_strFMLOC = txtFMLOC.getText().toString().trim();
			L_strTOLOC = txtTOLOC.getText().toString().trim();
			L_strPKGTP = txtPKGTP.getText().toString().trim();
			if(!L_strFMGRD.equals("") && !L_strTOGRD.equals(""))
			{
				strGRDBT1 = " and PR_PRDDS between '"+L_strFMGRD+"' and '"+L_strTOGRD+"'";
				strORDBY1 += "ST_PRDCD,LT_PSTDT asc,ST_LOTNO,ST_RCLNO,";
				strGRDBT2 = " and PR_PRDDS between '"+L_strFMGRD+"' and '"+L_strTOGRD+"'";
				strORDBY2 += "IST_PRDCD,";
			}
			if(!L_strFMLOT.equals("") && !L_strTOLOT.equals(""))
			{
				strLOTBT1 = " and ST_LOTNO between '"+L_strFMLOT+"' and '"+L_strTOLOT+"'";
				strORDBY1 += "LT_PSTDT asc,ST_LOTNO,ST_RCLNO,";
				strLOTBT2 = " and IST_LOTNO between '"+L_strFMLOT+"' and '"+L_strTOLOT+"'";
				strORDBY2 += "IST_LOTNO,IST_RCLNO,";
			}
			if(!L_strFMLOC.equals("") && !L_strTOLOC.equals(""))
			{
				strLOCBT1 = " and ST_MNLCD between '"+L_strFMLOC+"' and '"+L_strTOLOC+"'";
				strORDBY1 += "ST_MNLCD,";
				strLOCBT2 = " and IST_MNLCD between '"+L_strFMLOC+"' and '"+L_strTOLOC+"'";
				strORDBY2 += "IST_MNLCD,";
			}
			if(!L_strPKGTP.equals(""))
			{
				strPKGTP1 = " and ST_PKGTP = '"+L_strPKGTP+"'";
				strORDBY1 += "ST_PKGTP,";
				strPKGTP2 = " and IST_PKGTP = '"+L_strPKGTP+"'";
				strORDBY2 += "IST_PKGTP,";
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getGRDSTR");
		}
	}
	/**
	 *Method to fetch Data from Database and Show the Data in Upper table.
	*/
	private void showTABL1()
	{
		try
		{
			int i=0;
			M_strSQLQRY = "Select PR_PRDDS,isnull(LT_RESFL,'') LT_RESFL,ST_LOTNO,ST_RCLNO,ST_STKQT,ST_UCLQT,ST_MNLCD,ST_PKGTP,LT_RETQT,ST_STSFL,ST_PRDCD,CONVERT(varchar,LT_PSTDT,103) LT_PSTDT from FG_STMST,CO_PRMST,PR_LTMST";
			M_strSQLQRY +=" where ST_PRDCD=PR_PRDCD and ST_CMPCD=LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (ST_STKQT+ST_UCLQT) > 0";
			M_strSQLQRY += strGRDBT1 + strLOTBT1 + strLOCBT1 + strPKGTP1 + "'" ;//+ strORDBY1;//shubham
			M_strSQLQRY = M_strSQLQRY.substring(0,M_strSQLQRY.length()-1).trim();
			//System.out.println(" showTABL1 = "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("PR_PRDDS"),i,TB1_GRADE);
					if(M_rstRSSET.getString("LT_RESFL").equals("Q"))
						TBL_STKDTL.setValueAt("*",i,TB1_RESFL);
					else if(M_rstRSSET.getString("LT_RESFL").equals("H"))
						TBL_STKDTL.setValueAt("#",i,TB1_RESFL);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("ST_LOTNO"),i,TB1_LOTNO);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("ST_RCLNO"),i,TB1_RCLNO);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("ST_STKQT"),i,TB1_STKQT);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("ST_UCLQT"),i,TB1_UCLQT);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("ST_MNLCD"),i,TB1_MNLCD);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("ST_PKGTP"),i,TB1_PKGTP);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("LT_RETQT"),i,TB1_RETQT);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("ST_STSFL"),i,TB1_STSFL);
					TBL_STKDTL.setValueAt(M_rstRSSET.getString("LT_PSTDT"),i,TB1_PSTDT);
					i++;
				}
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"showTABL1");
		}
	}	
	/**
	 *Method to fetch Data from Database and Show the Data in Lower table.
	*/
	private void showTABL2()
	{
		try
		{
			java.sql.Date L_datTMPDT;
			int i=0;
			M_strSQLQRY = "Select distinct IST_ISSNO,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGTP,IST_ISSQT,IST_AUTDT,IST_STSFL,";
			M_strSQLQRY += "IST_PRDCD from FG_ISTRN,CO_PRMST where IST_PRDCD=PR_PRDCD and IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_STSFL <> 'X' ";
			M_strSQLQRY += strGRDBT2 + strLOTBT2 + strLOCBT2 + strPKGTP2 + strORDBY2;
			M_strSQLQRY = M_strSQLQRY.substring(0,M_strSQLQRY.length()-1).trim();
			//System.out.println(" showTABL2 = "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					TBL_ISUDTL.setValueAt(M_rstRSSET.getString("IST_ISSNO"),i,TB2_ISSNO);
					TBL_ISUDTL.setValueAt(M_rstRSSET.getString("IST_LOTNO"),i,TB2_LOTNO);
					TBL_ISUDTL.setValueAt(M_rstRSSET.getString("IST_RCLNO"),i,TB2_RCLNO);
					TBL_ISUDTL.setValueAt(M_rstRSSET.getString("IST_MNLCD"),i,TB2_MNLCD);
					TBL_ISUDTL.setValueAt(M_rstRSSET.getString("IST_PKGTP"),i,TB2_PKGTP);
					TBL_ISUDTL.setValueAt(M_rstRSSET.getString("IST_ISSQT"),i,TB2_ISSQT);
					L_datTMPDT = M_rstRSSET.getDate("IST_AUTDT");
					if(L_datTMPDT !=null)
					{
						TBL_ISUDTL.setValueAt(M_fmtLCDAT.format(L_datTMPDT),i,TB2_AUTDT);
					}
					else
						TBL_ISUDTL.setValueAt("",i,TB2_AUTDT);
					TBL_ISUDTL.setValueAt(M_rstRSSET.getString("IST_STSFL"),i,TB2_STSFL);
					i++;
					//intRWCNT++;
				}
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"showTABL2");
		}
	}
}

