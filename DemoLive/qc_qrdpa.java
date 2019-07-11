/*
System Name   : Management Information System
Program Name  : Datewise Product Test Analysis Query
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 
Version       : v2.0.0.0

Modificaitons 
Modified By    : 
Modified Date  : 
Modified det.  : 
Version        :

*/


import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.MouseEvent;import java.awt.event.MouseListener;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.JComponent;import javax.swing.InputVerifier;
import javax.swing.JTable.*;import java.awt.Color;import java.awt.Font;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.util.Hashtable;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Datewise Product Test Analysis Query

Purpose : to view & generate the Date wise Product Test Report.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
CO_PRMST                                         
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT     LT_PSTDT      PR_LTMST      Timestamp     Production Start Date & Time
txtTODAT     LT_PSTDT      PR_LTMST      Timestamp     Production Start Date & Time
txtLINNO     LT_LINNO      PR_LTMST      VARCHAR(2)    Production Line Number
cmbPRDTP     LT_PRDTP      PR_LTMST      VARCHAR(2)    Product Type
--------------------------------------------------------------------------------------
<B>
Logic</B> Data is taken from PR_LTMST and CO_PRMST for condiations
    1) LT_PRDCD = PR_PRDCD
    2) AND LT_PSTDT BETWEEN given date range.
    3) If Line number is given then LT_LINNO = given Line Number
    4) AND LT_PRDTP = selected product Type
    5) AND LT_CLSFL <> '8'";

<B>Validations :</B>
    - To Date must be greater than From date & less than current Date.
    - Line number must be valid.
</I> */
public class qc_qrdpa extends cl_rbase implements MouseListener 
{									/** JComboBox to display & select Product Type.*/
	private JComboBox cmbPRDTP;		/** JTextField to enter & display From Date to specify date Range.*/
	private JTextField txtFMDAT;	/** JTextField to enter & display To Date to specify date Range*/
	private JTextField txtTODAT;	/** JTextField to enter & display Line Number.*/
	private JTextField txtLINNO;	/** JLabel to display message on the Screen.*/
	private JLabel lbl1,lbl2;		/** JTable to display quality parameter values for different Lots.*/
	private cl_JTable tblLOTDL;		/** String variable for QCA Type.*/
	private String strQCATP;		/** String varaible for Product Type.*/
	private String strPRDTP;		/** String varaible for Generated Report File Name*/
	private String strFILNM;		/** Integer varaible for Row Width.*/
	private int intROWWD;			/** Integer varaible for column Width.*/
	private int intCOLWD;
										/** Integer varaible for Check Flag Columm.*/
	private final int TB_CHKFL =0;		/** Integer varaible for Lot Number Columm.*/
	private final int TB_LOTNO =1;		/** Integer varaible for RCL Number Columm.*/
	private final int TB_RCLNO =2;		/** Integer varaible for Lot Date Columm.*/
	private final int TB_LOTDT =3;		/** Integer varaible for Product Description Columm.*/
	private final int TB_PRDDS =4;		/** Integer varaible for Classification Flag Columm.*/
	private final int TB_CLSFL =5;		/** Integer varaible for DSP Quality Parameter Value Columm.*/
	private final int TB_DSP =6;		/** StringBuffer object for dynamically generated Header.*/
	private StringBuffer stbHEADR;		/** StringBuffer object for dynamicaly generated dotted Line.*/
	private StringBuffer stbDOTLN;
								/** String variable for Product Description.*/
	private String strPRDDS;	/** String variable for classification Flag.*/
	private String strCLSFL;	/** String variable for Line Number.*/
	private String strLINNO;	/** String variable for Lot Number.*/
	private String strLOTNO;	/** String variable for Reclassification Number.*/
	private String strRCLNO;	/** String variable for Lot Date.*/
	private String strLOTDT;	/** String variable for From Date & Time to secify Date Time Range.*/
	private String strFMDTM;	/** String variable for To Date & Time to secify Date Time Range.*/
	private String strTODTM;	/** String variable for Old Lot Number to manage the Lot change.*/
	private String strOLINNO;
								/** Final Integer variable for Column Count.*/    
	private final int intCOLCNT_fn = 11;/** Final Integer variable for Row Count.*/
    private final int intROWCT_fn =1000;/** Final String variable for Test Type (composite certification Test).*/
	private final String strTSTTP_fn = "0103"; 
								/**	Array of String for Quality Parameter Code*/
    private String[] arrQPRCD;  /** Integer variable for Record Count.*/
	private int intRECCNT =0;	/** Array of Integers for decimal oint possion.*/
    private int[] arrDECCT;		/** FileOutPutStream Object for generated Report File.*/
	private FileOutputStream fosREPORT;/** DataOutPutStream Object to hold report data in stream to generate the Report.*/
    private DataOutputStream dosREPORT;
	private Hashtable<String,String> hstSHRDS;
	qc_qrdpa()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Product Type"),2,1,1,0.9,this,'R');
			add(cmbPRDTP = new JComboBox(),2,2,1,1.5,this,'L');
		
			add(new JLabel("From Date"),3,1,1,.9,this,'R');
			add(txtFMDAT = new TxtDate(),3,2,1,1,this,'L');
			add(new JLabel("To Date"),3,3,1,.9,this,'R');
			add(txtTODAT = new TxtDate(),3,4,1,1,this,'L');
				
			add(new JLabel("Line Number"),3,5,1,.9,this,'R');
			add(txtLINNO = new JTextField(),3,6,1,1,this,'L');
												
			String[] L_COLHD1 = {"Select","Lot No.","RCLNO","Date","Grade","Cl.Flag","DSP","MFI","IZO","VIC","TS","EL","RSM","WI","a","b","Y1"};
			int[] L_COLSZ1 = {30,60,40,65,75,40,40,40,40,40,40,40,40,40,40,40,40};      		
			tblLOTDL = crtTBLPNL1(this,L_COLHD1,intROWCT_fn,5,1,10.3,8,L_COLSZ1,new int[]{0});
			tblLOTDL.addMouseListener(this);	
			add(lbl1 = new JLabel("Classification  Status :-  U : Unclassified    C : Classfied    P : Prov. Classfied"),17,1,1,6,this,'L');
			add(lbl2 = new JLabel("* Please click on Lot number for Classification  Details."),18,1,1,6,this,'L');
							
			lbl1.setForeground(Color.blue);
			lbl2.setForeground(Color.blue);				
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD'' and isnull(CMT_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
			txtFMDAT.requestFocus();				
			arrQPRCD = new String[intCOLCNT_fn];
			arrQPRCD = new String[intCOLCNT_fn];
			arrDECCT = new int[intCOLCNT_fn];
			M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_NMP02 from CO_CDTRN where CMT_CGMTP = 'RPT' ";
 			M_strSQLQRY += " AND CMT_CGSTP = 'QCXXPTA' ORDER BY CMT_NCSVL";				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				int i = 0;
				while(M_rstRSSET.next())
				{						
					arrQPRCD[i] = M_rstRSSET.getString("CMT_CODCD");
					if(arrQPRCD[i]!=null)
					{
						arrQPRCD[i] = arrQPRCD[i].trim();
						arrDECCT[i] = M_rstRSSET.getInt("CMT_NMP02");// decimal
					}						
					i++;
				}
			    M_rstRSSET.close();					
			}
			hstSHRDS = new Hashtable<String,String>();
			M_strSQLQRY = "Select QS_QPRCD,QS_SHRDS,QS_UOMCD,QS_TSMCD from CO_QSMST where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
					{
						//hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));
						hstSHRDS.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_SHRDS"),""));
					}
				}				
				M_rstRSSET.close();
			}	
			M_pnlRPFMT.setVisible(true);
			strQCATP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			stbHEADR = new StringBuffer();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
  public void mouseClicked(MouseEvent L_ME)
  {
        if(M_objSOURC == tblLOTDL)
        {
            for(int i=0;i<tblLOTDL.getRowCount();i++)
            {
                if(i !=tblLOTDL.getSelectedRow())
                    tblLOTDL.setValueAt(Boolean.FALSE,i,TB_CHKFL);
                else
                    tblLOTDL.setValueAt(Boolean.TRUE,i,TB_CHKFL);
            }
            cl_dat.M_btnUNDO_pbst.setEnabled(false);
            qc_qrlcl obj = new qc_qrlcl(0);             // create an object
        	cl_dat.M_pnlFRBTM_pbst.add("screen1", obj); // add it to the card layout
			cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen1"); // show this card
            obj.exeLTQRY(tblLOTDL.getValueAt(tblLOTDL.getSelectedRow(),TB_LOTNO).toString(),"00","01","01");  
        }
  }	
	public void actionPerformed(ActionEvent L_AE)
	{
	    super.actionPerformed(L_AE);       
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setENBL(true);
                tblLOTDL.setEnabled(false);		
                tblLOTDL.cmpEDITR[TB_CHKFL].setEnabled(true);		
				int L_intDAYCT = Integer.valueOf(cl_dat.M_strLOGDT_pbst.substring(0,2)).intValue();					
				txtFMDAT.setText(calDATE(cl_dat.M_strLOGDT_pbst,L_intDAYCT));
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);				
				txtFMDAT.requestFocus();
			}
			else
			{				
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == txtFMDAT)
		{	
			if(txtFMDAT.getText().trim().length()>0)
			{
				txtTODAT.requestFocus();
				setMSG("Enter Date to Specify date range..",'N');			
			}
		}		
		else if(M_objSOURC == txtTODAT)		
		{
			txtLINNO.requestFocus();
			setMSG("Please Enter Line Number OR to prass F1 to select From List..",'N');
		}
		else if(M_objSOURC == cmbPRDTP)
		{					
			strQCATP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			
			txtFMDAT.requestFocus();
			setMSG("Please Enter Date range to generate the report..",'N');				
		}
		else if(M_objSOURC == txtLINNO)
		{	
			int i=0,L_intCOUNT =0;
			ResultSet L_rstRSSET ;
			String L_strQPRVL;
			try
			{
				strFMDTM = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim()+ " 07:00"));
				strTODTM = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim()+ " 07:00"));										
				tblLOTDL.clrTABLE();
			
				M_strSQLQRY = " SELECT LT_LOTNO,LT_RCLNO,LT_CLSFL,LT_LINNO,LT_PSTDT,PR_PRDDS ";
				for(int k=0;k<arrQPRCD.length;k++)							
					M_strSQLQRY += ","+"PS_"+arrQPRCD[k].trim()+"VL";																			
				M_strSQLQRY += " from CO_PRMST,PR_LTMST LEFT OUTER JOIN QC_PSMST ON ";
				M_strSQLQRY +=" LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO AND PS_QCATP = " + "'"+ strQCATP +"'" + " AND PS_TSTTP = " + "'"+ strTSTTP_fn + "'";
				M_strSQLQRY += " AND PS_STSFL <> 'X' ";							
				M_strSQLQRY += " where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDCD = PR_PRDCD ";
				M_strSQLQRY += " AND LT_PSTDT BETWEEN '" + strFMDTM + "' AND '" + strTODTM +"'";   
				if(txtLINNO.getText().trim().length() > 0)
					M_strSQLQRY += " AND LT_LINNO = " + "'"+ txtLINNO.getText().trim()+ "'";
				M_strSQLQRY += " AND LT_PRDTP = '" + strQCATP +"'";
				M_strSQLQRY += " AND LT_CLSFL <> '8'";
				M_strSQLQRY += " ORDER BY LT_LINNO,LT_LOTNO,LT_RCLNO";				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
						strLOTNO = nvlSTRVL(M_rstRSSET.getString("LT_LOTNO"),"");
						strRCLNO = nvlSTRVL(M_rstRSSET.getString("LT_RCLNO"),"");
						strLINNO = nvlSTRVL(M_rstRSSET.getString("LT_LINNO"),"");					
						java.sql.Timestamp L_tmsTEMP = M_rstRSSET.getTimestamp("LT_PSTDT");
						if(L_tmsTEMP != null)
							strLOTDT = M_fmtLCDTM.format(L_tmsTEMP);					
						strLOTDT = strLOTDT.substring(0,10);
						strCLSFL = nvlSTRVL(M_rstRSSET.getString("LT_CLSFL"),"");
						if(!strCLSFL.equals(""))
						{
							if(strCLSFL.trim().equals("9"))
								strCLSFL ="C";
							else if(strCLSFL.trim().equals("4"))
								strCLSFL ="P";
							else 
								strCLSFL ="U";
						}
						strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");										
						if(!strLOTNO.equals(""))
						{													
							tblLOTDL.setValueAt(strLOTNO.trim(),L_intCOUNT,TB_LOTNO);
							tblLOTDL.setValueAt(strRCLNO.trim(),L_intCOUNT,TB_RCLNO);
							tblLOTDL.setValueAt(strLOTDT.trim(),L_intCOUNT,TB_LOTDT);
							tblLOTDL.setValueAt(strPRDDS.trim(),L_intCOUNT,TB_PRDDS);
							tblLOTDL.setValueAt(strCLSFL.trim(),L_intCOUNT,TB_CLSFL);
							for(int l=0;l<arrQPRCD.length;l++)
							{									
								L_strQPRVL = nvlSTRVL(M_rstRSSET.getString("PS_"+arrQPRCD[l].trim()+"VL"),"-");
								if(!L_strQPRVL.equals("-"))									
									L_strQPRVL = setNumberFormat(Double.valueOf(L_strQPRVL).doubleValue(),arrDECCT[l]);
								tblLOTDL.setValueAt(L_strQPRVL,L_intCOUNT,TB_DSP+l);
							}							
						}
						L_intCOUNT++;
					}
					intRECCNT = L_intCOUNT;
					M_rstRSSET.close();
					if(L_intCOUNT == 0)
						setMSG("No records exists with the given details..",'N');
					else
						setMSG("Query completed",'N');
				}					
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == txtLINNO");
			}			
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{ 
		super.keyPressed(L_KE);    
   		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			if(M_objSOURC == txtLINNO)
			{
				strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP =  ";
				M_strSQLQRY = M_strSQLQRY + "'SYS'" + " AND CMT_CGSTP = ";
				M_strSQLQRY = M_strSQLQRY + "'PRXXLIN'";
				M_strSQLQRY += " AND CMT_CCSVL = " + "'"+strPRDTP + "'";
				M_strHLPFLD = "txtLINNO";
				//txtLINNO.setEnabled(false);		
				String LM_ARRHDR[] = {"Line No.","Description"};
				cl_hlp(M_strSQLQRY,1,1,LM_ARRHDR,2,"CT");
			}
		}	 		
	}
	/**
	* Method to execute F1 help.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
	    if (M_strHLPFLD.equals("txtLINNO"))
        {
			txtLINNO.setText(cl_dat.M_strHLPSTR_pbst);
            //txtLINNO.setEnabled(true);
            txtLINNO.requestFocus();
         }
         cl_dat.M_flgHELPFL_pbst = false;
	}						
	/**
	* Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
	
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_qrdpa.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_qrdpa.doc";				
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Datewise Product Test Analysis Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}		
	/**
	* Method to validate Consignment number, before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{		
		try
		{
		    cl_dat.M_PAGENO = 0;
			if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter From Date, To spacify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter To date, to Spacify Date Range..",'E');
				txtTODAT.requestFocus();
				return false;
			}				
			else if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("To Date must be smaller than Current Date.. ",'E');				
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				txtTODAT.requestFocus();
				return false;
			}
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("From Date must be smaller than Current Date.. ",'E');				
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				txtTODAT.requestFocus();
				return false;
			}
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("From Date must be smaller than To Date .. ",'E');											
				txtFMDAT.requestFocus();
				return false;
			}		
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount()==0)
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
		}
		catch(Exception L_EX)	
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	/**
    *Method to fetch data from database & club it with Header in the Data Output Stream.
	*/
	private void getDATA()
	{			
		String L_strQPRVL;	
		int L_intCOUNT = 0;
		int L_intTEMP = 0;
		StringBuffer L_stbDATA = new StringBuffer();
		strOLINNO = " ";
	    try
	    {
			setCursor(cl_dat.M_curWTSTS_pbst);
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Datewise Product Analysis Test Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}	
			prnHEADER();
			
			for(int i=0;i<intRECCNT;i++)
			{
				L_stbDATA.delete(0,L_stbDATA.toString().length());								
				strLOTNO = tblLOTDL.getValueAt(i,TB_LOTNO).toString().trim(); 
				if(strLOTNO.trim().length() >2)
					strLINNO = strLOTNO.substring(0,2);
				else 
					strLINNO ="";
				strRCLNO = tblLOTDL.getValueAt(i,TB_RCLNO).toString().trim(); 
				strLOTDT = tblLOTDL.getValueAt(i,TB_LOTDT).toString().trim(); 
				strCLSFL = tblLOTDL.getValueAt(i,TB_CLSFL).toString().trim(); 
				if(strCLSFL.trim().equals("C"))
					strCLSFL =" ";				
			     strPRDDS = tblLOTDL.getValueAt(i,TB_PRDDS).toString().trim(); 
				 if(strPRDDS == null)
					 strPRDDS = "";				 
				if(strLOTNO != null)
				{
					strLOTNO = strLOTNO.trim();
					strRCLNO = strRCLNO.trim();
					strCLSFL = strCLSFL.trim();
					strPRDDS = strPRDDS.trim();
					strLOTDT = strLOTDT.trim();					
					if(!strLINNO.equals(strOLINNO))
					{
						if(cl_dat.M_intLINNO_pbst> 61)
						{													
							dosREPORT.writeBytes(stbDOTLN.toString());
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();												
						}
						dosREPORT.writeBytes("\n");						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						dosREPORT.writeBytes("Line No. : " + strLINNO);						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst += 3;												
					}
					L_intCOUNT +=1;
					strOLINNO = strLINNO;
					L_stbDATA.append(padSTRING('R',strLOTNO,9));
					L_stbDATA.append(padSTRING('R',strRCLNO+" "+strCLSFL,5));					
					L_stbDATA.append(padSTRING('R',strPRDDS,10));
					L_stbDATA.append(padSTRING('R',strLOTDT,5));					
					for(int l=0;l<arrQPRCD.length;l++)
					{	
						if(l==0)
							L_intTEMP = 5;
						else
							L_intTEMP = 7;
						
						L_strQPRVL = tblLOTDL.getValueAt(i,TB_DSP+l).toString().trim();												
						if(L_strQPRVL.equals(""))
							L_strQPRVL = "-";
						if(!L_strQPRVL.equals("-"))
							L_strQPRVL = setNumberFormat(Double.valueOf(L_strQPRVL).doubleValue(),arrDECCT[l]);						
						L_stbDATA.append(padSTRING('L',L_strQPRVL,L_intTEMP));						
					}					
					dosREPORT.writeBytes(L_stbDATA.toString());					
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
					if(cl_dat.M_intLINNO_pbst> 63)
					{													
						dosREPORT.writeBytes(stbDOTLN.toString());
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();												
					}
				}				
			}		
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			dosREPORT.writeBytes("Note: U: Unclassified Lots  P: Prov. Classified Lots ");			
			
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}	
	/**
	 * Method to Generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{			
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
			if(cl_dat.M_PAGENO == 1)
			{
				stbHEADR.delete(0,stbHEADR.toString().length());				
				stbHEADR.append(padSTRING('R'," Lot No.",9));
				stbHEADR.append(padSTRING('R',"RCL",5));
				stbHEADR.append(padSTRING('R',"Grade",10));
				stbHEADR.append(padSTRING('R',"Date",4));			
				intROWWD = 27;
				for(int j=0;j<11;j++)
				{			
					if (j == 0)
						intCOLWD = 6;
					else
						intCOLWD = 7;
        			String LM_DESC = hstSHRDS.get(arrQPRCD[j]).toString();
					//String LM_DESC = cl_dat.getPRMCOD("CMT_SHRDS","SYS","QCXXQPR",arrQPRCD[j]);
					if(LM_DESC !=null)
					{
						LM_DESC = LM_DESC.trim();
						stbHEADR.append(padSTRING('L',LM_DESC,intCOLWD));
						intROWWD += intCOLWD;
					}
				}						
				stbDOTLN = new StringBuffer("---");
				for(int i =0; i< intROWWD;i++)
					stbDOTLN.append("-");
			}		
			dosREPORT.writeBytes("\n\n\n\n\n");			 
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWWD -23));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst +"\n");
			dosREPORT.writeBytes(padSTRING('R',"Datewise Product Test Analysis",intROWWD -23));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n" );
			dosREPORT.writeBytes("From Date : "+txtFMDAT.getText().trim()+"  To Date : "+txtTODAT.getText().trim()+"\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			dosREPORT.writeBytes(stbHEADR.toString()+"\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");	
			cl_dat.M_intLINNO_pbst =12;			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}		
	/**
	*Method to Calculate From-Date, one day smaller than To-Date.
    *@param P_strTODT String argument to pass To_Date.
	*/
    private String calDATE(String P_strTODT,int P_intDATCT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strTODT));
		    M_calLOCAL.add(java.util.Calendar.DATE,- (P_intDATCT -1));																
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{	       
			setMSG(L_EX, "calDATE");
			return (cl_dat.M_strLOGDT_pbst);
        }					
	}	
}	