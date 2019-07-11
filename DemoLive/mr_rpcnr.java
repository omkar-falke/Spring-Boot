/*
System Name   : Marketing System
Program Name  : Credit Note Register 
Author        : Mr V.M.Bhaurkar

Purpose : This program is used to generate & print Credit Note Register.

List of Table used :
-----------------------------------------------------------------------------------------------------------
Table Name      Primary Key														Operation done
																				Insert  Upd  Query  Del
-----------------------------------------------------------------------------------------------------------
MR_IVTRN		IVT_MKTTP,IVT_INVNO,IVT_PRDCD,IVT_PKGTP							  -      -     #     -
MR_PTTRN		PT_MKTTP,PT_INVNO,PT_PRDCD,PT_PKGTP,PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_SRLNO  -     #     -    
CO_PTMST		PT_PRTTP,PT_PRTCD												  -      -     #     -
-----------------------------------------------------------------------------------------------------------
List of field on the Report :
Sr No		Detail					Column Name				Table			Remark
1			Customer Name			PT_PRTNM				CO_PTMST
2			Inv No					PT_INVNO				MR_PTTRN
3			Inv Date				IVT_INVDT				MR_IVTRN
4			Grade					IVT_PRDDS				MR_IVTRN
5			Inv Qty					PT_INVQT				MR_PTTRN
6			Amount					PT_PGRVL				MR_PTTRN
7			Indent No				IVT_INDNO				MR_IVTRN
8			Rate					PT_TRNRT				MR_PTTRN
9			Credit Note Number		PT_DOCRF				MR_PTTRN
10			Acc.Ref.No.				PT_ACCRF				MR_PTTRN

<I>
<B>Query : </B>
For generating Report Data is taken FROM 

		CO_PTMST  : Customer Name from (PT_PRTNM)
		CO_CDTRN  : Destination code are kept in SYS/MRXXPTT 
        MR_PTTRN  : All the record show in report
        MR_IVTRN  : Invoice Date And Invoice No. is Taken from this table.

M_strSQLQRY = "PT_INVNO,PT_INVQT,PT_PGRVL,PT_TRNRT,PT_DOCRF,PT_ACCRF,IVT_INVDT,IVT_PRDDS,IVT_INDNO,PT_PRTNM ";
		   +=" from MR_PTTRN,MR_IVTRN,CO_PTMST where ";				
		   1) PT_MKTTP = IVT_MKTTP and PT_INVNO = IVT_INVNO 
		   2) and PT_PRDCD = IVT_PRDCD and PT_PKGTP = IVT_PKGTP 
		   3) and PT_PRTTP = PT_PRTTP and PT_PRTCD = PT_PRTCD 
		   4) and PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"' ";
		   5) and PT_STSFL <> 'X' and PT_CRDTP ='"+strCRDTP+"' 
		   6) order by PT_CRDTP,PT_PRTNM,IVT_PRDDS 

<B>Validations & Other Information:</B>    
</I>

*/

import java.sql.Date;import java.sql.ResultSet;import java.awt.event.*;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComboBox;import java.util.Hashtable;import java.util.Enumeration;
import java.util.StringTokenizer;

class mr_rpcnr extends cl_rbase
{
	private JComboBox cmbRPTOP;	   /** JComboBox to display & select Report.*/
	private JTextField txtFMDAT;   /** JtextField to display & enter Date to generate the Report.*/
	private JTextField txtTODAT;   /** String Variable for date.*/
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;
	private JTextField txtPRTTP;
	private JTextField txtDSRNM;
	
	private JTextField txtOTHCR;
	
	private String strFILNM;			 /** String Variable for generated Report file Name.*/ 
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
	
	private String strPRTNM;		/**String variable for party name */
	private String strPRDDS;		/**String variable for grade */
	private String strINVQT;        /**String variable for Invoice Quentity */
	private String strCRDVL;		/**String variable for Amount */
	
	private String strZONCD;        
	private String strPRDCT;        
	private String strPZONCD;		
	private String strPPRCT;		
	
	
	private String strPRDNM;		/** *********  String variable for reference party name ***********/
	
	private String strPPTNM;		/**String variable for reference party name */
	private double dblINVQT;		/**double variable for Indent no */
	private double dblCRDVL;		/**double variable for credit Amount */
	
	private double dblZONQT;		/**double variable for Indent no */
	private double dblZONVL;		/**double variable for credit Amount */
	
	private double dblPRDQT;		/**double variable for Indent no */
	private double dblPRDVL;		/**double variable for credit Amount */
	
	private double dblHSTQT;		/**double variable for Indent no */
	private double dblHSTVL;		/**double variable for credit Amount */
	
	private int intPRTSR =0;
	private int intPRCSR =0;
	private int intZONSR =0;
	private int intRECCT;		    /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private String strSPACE = " ";
	Date L_datTEMP;					/**variable for date */
	private String strDOTLN = "----------------------------------------------------------------------------------------------------------";
	private Hashtable<String,String> hstZONCD = new Hashtable<String,String>(10);
	private Hashtable<String,String> hstPRDDS = new Hashtable<String,String>(30);
	mr_rpcnr()
	{
		super(2);
		
		try
		{
			setMatrix(20,8);
			cmbRPTOP = new JComboBox();
			String L_strCODCD="";
			String L_strCODDS="";
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT'";
			M_strSQLQRY +=" AND  SUBSTRING(CMT_CODCD,1,1) in ('0','3') And isnull(CMT_STSFL,'')<> 'X' Order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{	 
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbRPTOP.addItem(L_strCODCD+" "+L_strCODDS);
				}
				M_rstRSSET.close();
			 }
		    M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='MRXXZON'";
		    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{	 
				while(M_rstRSSET.next())
				{
				    hstZONCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}
			 M_strSQLQRY = "SELECT SUBSTRING(CMT_CODCD,1,4) CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='MST' AND CMT_CGSTP ='COXXPGR' AND CMT_CCSVL ='SG'";
		    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{	 
				while(M_rstRSSET.next())
				{
				    hstPRDDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}
		
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Credit Note Type"),3,2,1,2,this,'L');
			add(cmbRPTOP,3,3,1,2,this,'L');

			add(new JLabel("Category "),3,5,1,1,this,'R');
			add(txtOTHCR = new TxtLimit(2),3,6,1,1,this,'L');
			
			add(new JLabel("From Date "),4,2,1,2,this,'L');
			add(txtFMDAT = new TxtDate(),4,3,1,1.5,this,'L');
			add(new JLabel("To Date "),5,2,1,2,this,'L');
			add(txtTODAT = new TxtDate(),5,3,1,1.5,this,'L');
			
			add(new JLabel("Party Type"),6,2,1,1,this,'L');
			add(txtPRTTP=new TxtLimit(5),6,3,1,1,this,'L');
			add(new JLabel("Party Code"),7,2,1,1,this,'L');
			add(txtPRTCD=new TxtLimit(5),7,3,1,1,this,'L');
			add(new JLabel("Party Name"),8,2,1,1,this,'L');
			add(txtPRTNM=new TxtLimit(30),8,3,1,3,this,'L');
			

			M_pnlRPFMT.setVisible(true);
			setENBL(false);	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		 txtOTHCR.setEnabled(false);	
		txtPRTNM.setEnabled(false);
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				//txtPRTTP.requestFocus();
				txtFMDAT.requestFocus();
				setMSG("Please Enter Date to generate the Report..",'N');
			}
			
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		if(M_objSOURC == cmbRPTOP)
		{
			txtPRTTP.setText("");
			txtPRTCD.setText("");
			txtPRTNM.setText("");
			if(cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("09"))
			{
	            txtOTHCR.setEnabled(true);		    
			}
			else
			{
			    txtOTHCR.setEnabled(false);		    
			    txtOTHCR.setText("");
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
				
				if(M_objSOURC == txtOTHCR)
    			{
    				M_strHLPFLD = "txtOTHCR";
    				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXOCN' And CMT_CODCD like '0%' AND CMT_CODCD<> '0Z' ";					
					//System.out.println(" OTHCR  "+M_strSQLQRY);
    				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
    			}
				if(M_objSOURC==txtPRTTP)
				{
					M_strHLPFLD="txtDSRCD";
					M_strSQLQRY="Select distinct PT_PRTTP from CO_PTMST where ";
					if((cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals("01"))
					{
						M_strSQLQRY += " PT_PRTTP in('C') ";
					}
					else if((cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals("02")||(cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2).equals("03"))
					{
						M_strSQLQRY += " PT_PRTTP in('D')";
					}
					else
						M_strSQLQRY += " PT_PRTTP in('C','D')";
					
					M_strSQLQRY += " AND isnull(PT_STSFL,'')<> 'X'";
					
					if(txtPRTTP.getText().trim().length()>0)
					{
						M_strSQLQRY += "AND PT_PRTTP LIKE '"+txtPRTTP.getText().trim().toUpperCase()+"%'"; 
					}
						
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Type"},1,"CT");
				}
				if(M_objSOURC == txtPRTCD)
				{
					M_strHLPFLD = "txtPRTCD";
					M_strSQLQRY="Select distinct A.PT_PRTCD,A.PT_PRTNM from CO_PTMST A,MR_PTTRN B  where B.PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and  A.PT_PRTTP='"+txtPRTTP.getText().trim().toUpperCase()+"'  AND A.PT_PRTTP=B.PT_PRTTP AND A.PT_PRTCD=B.PT_PRTCD  AND isnull(A.PT_STSFL,'')<> 'X'";
					if(txtPRTCD.getText().trim().length()>0)
					{
						M_strSQLQRY += "AND A.PT_PRTCD LIKE '"+txtPRTCD.getText().trim().toUpperCase()+"%'"; 
					}
					M_strSQLQRY +=" and B.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' ";
					if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09")) // Other Credit Note
						M_strSQLQRY += " AND B.PT_CRDTP='"+(cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2)+"'";
					else
					{
						if(txtOTHCR.getText().trim().length()==2)
							M_strSQLQRY +=" AND B.PT_CRDTP = '"+txtOTHCR.getText().trim()+"' ";
						else
							M_strSQLQRY +=" AND B.PT_CRDTP between '0A' AND '0Z'";
					}
					
					M_strSQLQRY += " order by  A.PT_PRTNM";
					//System.out.println("PArty Help "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name"},2,"CT");
				}
			}
			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == M_cmbDESTN)
				{
					cmbRPTOP.requestFocus();
				}
				if(M_objSOURC == cmbRPTOP)
				{
					if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09")) 
						txtFMDAT.requestFocus();
					else
						txtOTHCR.requestFocus();
					
				}
				if(M_objSOURC == txtOTHCR)
				{
					txtOTHCR.setText(txtOTHCR.getText().toUpperCase());
					txtFMDAT.requestFocus();
				}
				if(M_objSOURC == txtPRTTP)
				{
					txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());	
					txtPRTCD.requestFocus();
				}
				if(M_objSOURC == txtPRTCD)
				{
					txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());	
					if(txtPRTCD.getText().trim().length()==5)
					{
						M_strSQLQRY="Select distinct A.PT_PRTCD,A.PT_PRTNM from CO_PTMST A,MR_PTTRN B  where  B.PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and A.PT_PRTTP='"+txtPRTTP.getText().trim().toUpperCase()+"'  AND A.PT_PRTTP=B.PT_PRTTP AND A.PT_PRTCD=B.PT_PRTCD  AND isnull(A.PT_STSFL,'')<> 'X'";
						if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09")) // Other Credit Note
							M_strSQLQRY += " AND B.PT_CRDTP='"+(cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2)+"'";
						else
						{
							if(txtOTHCR.getText().trim().length()==2)
								M_strSQLQRY +=" AND B.PT_CRDTP = '"+txtOTHCR.getText().trim()+"' ";
							else
								M_strSQLQRY +=" AND B.PT_CRDTP between '0A' AND '0Z'";
						}
					//	M_strSQLQRY += " AND B.PT_CRDTP='"+(cmbRPTOP.getSelectedItem().toString().trim()).substring(0,2)+"'";
						M_strSQLQRY += " AND B.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' ";
						M_strSQLQRY += " AND A.PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'"; 
					
						//System.out.println("PARTY "+M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY); 
						
						if(M_rstRSSET!=null &&M_rstRSSET.next())
						{
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  "));
							//txtFMDAT.requestFocus();
							cl_dat.M_btnSAVE_pbst.requestFocus();
						}
						else
						{
							setMSG("Enter Proper Party Code1 Or Press F1 For Help..",'E');
							txtPRTNM.setText("");
							txtPRTCD.requestFocus();
						}				
						if(M_rstRSSET != null)
							M_rstRSSET.close();
					}
					else if(txtPRTCD.getText().trim().length()>0 && txtPRTCD.getText().trim().length()<5)
					{
						setMSG("Enter Proper Party Code2 Or Press F1 For Help..",'E');
						txtPRTNM.setText("");
						cl_dat.M_btnSAVE_pbst.requestFocus();
						//txtPRTCD.requestFocus();
					}
					else
					{
						txtPRTNM.setText("");
						txtPRTCD.setText("");
						//txtFMDAT.requestFocus();
						cl_dat.M_btnSAVE_pbst.requestFocus();
					
					}
				}
				if(M_objSOURC == txtFMDAT)
				{
					txtTODAT.requestFocus();
					setMSG("Enter Date",'N');
				}
				if(M_objSOURC == txtTODAT)
				{
					//cl_dat.M_btnSAVE_pbst.requestFocus();
					txtPRTTP.setText("");
					txtPRTCD.setText("");
					txtPRTNM.setText("");
					txtPRTTP.requestFocus();
				}
				setMSG("",'N');
			}
		}
		catch(Exception E)
		{
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD == "txtOTHCR")
			{
				txtOTHCR.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtDSRCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtPRTTP.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");				
				txtPRTCD.setText(L_STRTKN1.nextToken());
				txtPRTNM.setText(L_STRTKN1.nextToken());
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
		}
	}
	/**User friendly messagees	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(L_FE.getSource().equals(txtFMDAT))
			{
                setMSG("Enter  Date in format dd/mm/yyyy",'N');
			}
			if(L_FE.getSource().equals(txtTODAT))
			{
                setMSG("Enter  Date in format dd/mm/yyyy",'N');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"TEIND.FocusGained"+M_objSOURC);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Method to fetch data from Database & club it with Header & Footer.
	*/
	public void getDATA()
	{		
		cl_dat.M_PAGENO = 1;
		cl_dat.M_intLINNO_pbst =0;
		double L_dblPIVQT=0.0;
		double L_dblPCDVL=0.0;
		dblINVQT = 0;
		dblCRDVL = 0;
		dblZONQT = 0;
		dblZONVL = 0;
		dblHSTQT = 0 ;
		dblHSTVL = 0; 
		dblPRDQT = 0; // 05/08/2006
		dblPRDVL =0;    // 05/08/2006
		setCursor(cl_dat.M_curWTSTS_pbst);
		
		String L_strFMDAT = txtFMDAT.getText().trim();
       	String L_strTODAT = txtTODAT.getText().trim();
		Hashtable<String,Double> hstPRQTY = new Hashtable<String,Double>(30);
		Hashtable<String,Double> hstPRVAL = new Hashtable<String,Double>(30);
		String L_strCRDTP =cmbRPTOP.getSelectedItem().toString().trim();
		String strCRDTP =  L_strCRDTP.substring(0,2);
		try
	    {	        
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Register</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			prnHEADER();
			M_strSQLQRY = "select A.PT_INVNO PT_INVNO,A.PT_INVQT PT_INVQT,A.PT_PGRVL PT_PGRVL,A.PT_TRNRT PT_TRNRT,A.PT_DOCRF PT_DOCRF,A.PT_ACCRF PT_ACCRF,IVT_ZONCD,IVT_INVDT,IVT_BYRCD,IVT_PRDCD,IVT_PRDDS,IVT_INDNO,B.PT_PRTNM PT_PRTNM ";
			M_strSQLQRY +=" from MR_PTTRN A ,CO_PTMST B";
			/*if(txtOTHCR.getText().trim().equals("0Z"))
			{
				M_strSQLQRY +="	left outer join MR_IVTRN ON A.PT_INVNO||A.PT_MKTTP||A.PT_PRDCD||A.PT_PKGTP = IVT_INVNO||IVT_MKTTP||IVT_PRDCD||IVT_PKGTP ";
				M_strSQLQRY +="	where A.PT_PRTTP = B.PT_PRTTP ";	
			}
			else 
			{*/
			
			M_strSQLQRY +=",MR_IVTRN where  ";
			M_strSQLQRY +=" A.PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and A.PT_CMPCD = IVT_CMPCD and A.PT_MKTTP = IVT_MKTTP and A.PT_INVNO = IVT_INVNO ";
			M_strSQLQRY +=" and A.PT_PRDCD = IVT_PRDCD and A.PT_PKGTP = IVT_PKGTP and A.PT_PRTTP = B.PT_PRTTP ";
			
			
			M_strSQLQRY +="  and A.PT_PRTCD = B.PT_PRTCD ";
			M_strSQLQRY +=" and A.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"' ";
			if(txtPRTCD.getText().trim().length()>0)
			{
				M_strSQLQRY +=" AND A.PT_PRTTP='"+txtPRTTP.getText().trim()+"'";
				M_strSQLQRY +=" AND A.PT_PRTCD='"+txtPRTCD.getText().trim()+"'";
			}
			M_strSQLQRY +="  and A.PT_STSFL <> 'X' ";
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09")) // other than distributor comission
				M_strSQLQRY +="  and A.PT_CRDTP ='"+strCRDTP+"' " ;
			else 
			{
				if(txtOTHCR.getText().trim().length()==2 )
					M_strSQLQRY +=" AND  A.PT_CRDTP = '"+txtOTHCR.getText().trim()+"' ";
				else
					M_strSQLQRY +=" AND  A.PT_CRDTP between '0A' AND '0Y'";
			}
			M_strSQLQRY +=" ORDER BY IVT_ZONCD desc,B.PT_PRTNM,IVT_PRDCD ";
			System.out.println("QUERY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			strPPTNM ="";
			strPZONCD ="";
			strPPRCT ="";
			if(M_rstRSSET != null)
			{ 
			    intPRCSR =0;
			    intPRTSR =0;
			    intZONSR =0;
				//System.out.println("IN MAIN");
				while(M_rstRSSET.next())
				{
					if(cl_dat.M_intLINNO_pbst>60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN+"\n");
						cl_dat.M_intLINNO_pbst = 0;
						
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						cl_dat.M_PAGENO ++;
						prnHEADER();
					}
					strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					strZONCD = nvlSTRVL(M_rstRSSET.getString("IVT_ZONCD"),"");
					strPRDCT = nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),"").substring(0,4);
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"");
					strPRDNM = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
					strINVQT = nvlSTRVL(M_rstRSSET.getString("PT_INVQT"),"");
					strCRDVL = nvlSTRVL(M_rstRSSET.getString("PT_PGRVL"),"");
					//System.out.println("TOtal 10");
					if(!strPZONCD.equals(strZONCD))
					{
					  	if(intRECCT>0)
						{
						    dosREPORT.writeBytes("\n\n");
						    cl_dat.M_intLINNO_pbst += 2;
						     if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
		        	    	   prnFMTCHR(dosREPORT,M_strBOLD);
		        	    	 if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("<b>");
					        if(intPRCSR >1)
 					        {
            				  	dosREPORT.writeBytes(padSTRING('L'," Prd.  Total : ",34));
            					dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDQT,3)+"",""),16));
            					dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDVL,2)+"",""),12)+"\n");
            					cl_dat.M_intLINNO_pbst += 1;
            		        }
 					        dblPRDQT = 0;
            				dblPRDVL = 0;
            				
            				strPPRCT = strPRDCT;
						    // Party Total
						//    dosREPORT.writeBytes("\n\n");
						 	/// shifeted from below
						 	// Print summary of product sub category total here
						    if(hstPRQTY !=null)
						    {
						        String L_strKEY ="";
						        String L_strPRDDS ="";
						        Enumeration enm = hstPRQTY.keys();
						        while(enm.hasMoreElements())
						        {
						            L_strKEY = enm.nextElement().toString();
						            if(hstPRDDS.containsKey(L_strKEY))
						                L_strPRDDS = hstPRDDS.get(L_strKEY).toString();
						            else
						                L_strPRDDS ="";
						            dosREPORT.writeBytes(padSTRING('L', L_strPRDDS+" Total : ",34));
									//System.out.println("TOtal 1");
        							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstPRQTY.get(L_strKEY)).doubleValue(),3),16));
        							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstPRVAL.get(L_strKEY)).doubleValue(),2).toString(),12)+"\n");
                                }
						        
						    }
						    hstPRQTY.clear();	
						    hstPRVAL.clear();	
						    /// shifeted from below
						 	if(intPRTSR >1)
        					{	  
							    dosREPORT.writeBytes(padSTRING('L'," Party Total : ",34));
							    dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblINVQT,3)+"",""),16));
    							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblCRDVL,2)+"",""),12)+"\n");
    							cl_dat.M_intLINNO_pbst += 1;
                            }							
							dblINVQT = 0;
							dblCRDVL = 0;
							
							// Zone Total
						    //dosREPORT.writeBytes("\n\n");
						    if(intZONSR >1)
        					{	  
    							dosREPORT.writeBytes(padSTRING('L'," Zone  Total : ",34));
    							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblZONQT,3)+"",""),16));
    							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblZONVL,2)+"",""),12)+"\n");
    							cl_dat.M_intLINNO_pbst += 1;
        					}
        			   	   if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
		        	   	    prnFMTCHR(dosREPORT,M_strNOBOLD);
		        	   	    
		        	   	    if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("</b>");
					
							dblZONQT = 0;
							dblZONVL = 0;
						   /* 
						    // Print summary of product sub category total here
						    if(hstPRQTY !=null)
						    {
						        String L_strKEY ="";
						        String L_strPRDDS ="";
						        Enumeration enm = hstPRQTY.keys();
						        while(enm.hasMoreElements())
						        {
						            L_strKEY = enm.nextElement().toString();
						            if(hstPRDDS.containsKey(L_strKEY))
						                L_strPRDDS = hstPRDDS.get(L_strKEY).toString();
						            else
						                L_strPRDDS ="";
						            dosREPORT.writeBytes(padSTRING('L', L_strPRDDS+" Total : ",34));
									//System.out.println("TOtal 1");
        							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstPRQTY.get(L_strKEY)).doubleValue(),3),16));
        							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstPRVAL.get(L_strKEY)).doubleValue(),2).toString(),12)+"\n");
                                }
						        
						    }
						    hstPRQTY.clear();	
						    hstPRVAL.clear();	*/
						}
				  	    if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strBOLD);
		        	   	if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						//System.out.println("TOtal 11");
						if(hstZONCD.containsKey(strZONCD))
							dosREPORT.writeBytes(padSTRING('R',hstZONCD.get(strZONCD).toString()+"",40)+"\n");
						else
							dosREPORT.writeBytes(padSTRING('R',strZONCD,40)+"\n");
							
						//System.out.println("TOtal 111");
					    if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
					    if(M_rdbHTML.isSelected())
				            dosREPORT.writeBytes("</b>");
						cl_dat.M_intLINNO_pbst +=1;
						strPZONCD = strZONCD;
					    dosREPORT.writeBytes(padSTRING('R',strPRTNM+"",40)+"\n");
					   
    					cl_dat.M_intLINNO_pbst +=1;
					    intPRCSR =0;
					    intPRTSR =0;
					    intZONSR =0;
					}
					else
					{
    					if(!strPPTNM.equals(strPRTNM))
    					{
    					   	if(intRECCT>0)
    						{
								dosREPORT.writeBytes("\n\n");
        					    cl_dat.M_intLINNO_pbst += 2;
        					    //if(!strPPRCT.equals(strPRDCT))
        					    //{
        					    if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
									prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<b>");
        					        if(intPRCSR >1)
        						   	{
            						  	dosREPORT.writeBytes(padSTRING('L'," Prd.  Total : ",34));
            							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDQT,3)+"",""),16));
            							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDVL,2)+"",""),12)+"\n");
            						   	cl_dat.M_intLINNO_pbst += 1;
        						   	}
        							dblPRDQT = 0;
        							dblPRDVL = 0;
        							strPPRCT = strPRDCT;
        					    //}   						    
    						   // dosREPORT.writeBytes("\n\n");
    						    if(intPRTSR >1)
        					    {
									dosREPORT.writeBytes(padSTRING('L'," Party Total : ",34));
        							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblINVQT,3)+"",""),16));
        							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblCRDVL,2)+"",""),12)+"\n");
        							cl_dat.M_intLINNO_pbst += 1;
    							}
    							dblINVQT = 0;
    							dblCRDVL = 0;
    							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</b>");
    						}
    				       	dosREPORT.writeBytes(padSTRING('R',strPRTNM+"",40)+"\n");
    				
    						cl_dat.M_intLINNO_pbst +=1;
    						intPRTSR =0;
    						intPRCSR =0;
    					}
					}
                    if(!strPPRCT.equals(strPRDCT))
					{
						if(intRECCT>0)
						{
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>"); 
						    if(intPRCSR >1)
						    {
    						    dosREPORT.writeBytes("\n");
    							dosREPORT.writeBytes(padSTRING('L'," Prd   Total : ",34));
    							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDQT,3)+"",""),16));
    							dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDVL,2)+"",""),12)+"\n");
    							cl_dat.M_intLINNO_pbst += 2;
						    }
						  	dblPRDQT = 0;
							dblPRDVL = 0;
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
		        	    	 if(M_rdbHTML.isSelected())
				                dosREPORT.writeBytes("</b>");
					
						}
						strPPRCT = strPRDCT;
						intPRCSR =0;
					}
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_INVNO"),""),12));
					L_datTEMP = M_rstRSSET.getDate("IVT_INVDT");
					if(L_datTEMP !=null)
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),12));
					else
						dosREPORT.writeBytes(padSTRING('R'," ",12));
					dosREPORT.writeBytes(padSTRING('R',strPRDDS+"",18));
					dosREPORT.writeBytes(padSTRING('L',strINVQT+"",8));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strCRDVL),2),12));
					dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
					dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PT_TRNRT"),"0")),0),7));
					dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
					dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_DOCRF"),""),10));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ACCRF"),""),10));
					strPPTNM = strPRTNM;
									
					L_dblPIVQT = Double.parseDouble(strINVQT);
					dblINVQT = dblINVQT + L_dblPIVQT;
                    dblZONQT = dblZONQT + L_dblPIVQT;
                    dblPRDQT = dblPRDQT + L_dblPIVQT;
                    
					L_dblPCDVL = Double.parseDouble(strCRDVL);
					dblCRDVL = dblCRDVL + L_dblPCDVL;
					dblZONVL = dblZONVL + L_dblPCDVL;
					dblPRDVL = dblPRDVL + L_dblPCDVL;
					if(hstPRQTY.containsKey((String)strPRDCT))
					{
					    dblHSTQT = Double.parseDouble(hstPRQTY.get(strPRDCT).toString());
					    hstPRQTY.put(strPRDCT,new Double(dblHSTQT+L_dblPIVQT));
					}
					else
				    	hstPRQTY.put(strPRDCT,new Double(L_dblPIVQT));
					if(hstPRVAL.containsKey((String)strPRDCT))
					{
					    dblHSTVL = Double.parseDouble(hstPRVAL.get(strPRDCT).toString());
					    hstPRVAL.put(strPRDCT,new Double(dblHSTVL+L_dblPCDVL));
					}
					else
				    	hstPRVAL.put(strPRDCT,new Double(L_dblPCDVL));
				
				
					intRECCT++;
					intPRCSR++;
			    	intPRTSR++;
				    intZONSR++;
			
				}
				dosREPORT.writeBytes("\n\n");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<b>");
				if(intPRCSR >1)
			    {
				    dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('L'," Prd   Total : ",34));
					dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDQT,3)+"",""),16));
					dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblPRDVL,2)+"",""),12)+"\n");
					cl_dat.M_intLINNO_pbst += 2;
			    }
			    
				if(hstPRQTY !=null)
			    {
			        String L_strKEY ="";
			        String L_strPRDDS ="";
			        Enumeration enm = hstPRQTY.keys();
			        while(enm.hasMoreElements())
			        {
						L_strKEY = enm.nextElement().toString();
			            if(hstPRDDS.containsKey(L_strKEY))
			                L_strPRDDS = hstPRDDS.get(L_strKEY).toString();
			            else
			                L_strPRDDS ="";
            
			            //dosREPORT.writeBytes(padSTRING('L',hstPRDDS.get(L_strKEY).toString() +" Total : ",34));
						dosREPORT.writeBytes(padSTRING('L',L_strPRDDS+" Total : ",34));
					    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstPRQTY.get(L_strKEY)).doubleValue(),3),16));
        			    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstPRVAL.get(L_strKEY)).doubleValue(),2).toString(),12)+"\n");
                    }
			    }
			    hstPRQTY.clear();	
			    hstPRVAL.clear();	
			    if(intPRTSR >1)
				{
					dosREPORT.writeBytes(padSTRING('L'," Party Total : ",34));
    				dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblINVQT,3)+"",""),16));
    				dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblCRDVL,2)+"",""),12)+"\n");
    				cl_dat.M_intLINNO_pbst += 1;
    			}
    			if(intZONSR >1)
				{	  
					dosREPORT.writeBytes(padSTRING('L'," Zone  Total : ",34));
					dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblZONQT,3)+"",""),16));
					dosREPORT.writeBytes(padSTRING('L',""+nvlSTRVL(setNumberFormat(dblZONVL,2)+"",""),12)+"\n");
					cl_dat.M_intLINNO_pbst += 1;
				}	
				 if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
		           	   prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
				   dosREPORT.writeBytes("</b>");
				dblINVQT = 0;
				dblCRDVL = 0;
				dblZONQT = 0;
				dblZONVL = 0;
				dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			}
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);					
			}			
			if(fosREPORT !=null)    
			    fosREPORT.close();
			if(dosREPORT !=null)        
			    dosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length()==0)
			{
				txtFMDAT.requestFocus();
				setMSG("Date can't be Blank..",'E');
				return false;
			}
			if(txtTODAT.getText().trim().length()==0)
			{
				txtFMDAT.requestFocus();
				setMSG("Date can't be Blank..",'E');
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("From Date can not be greater than TO Date's date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(txtPRTTP.getText().trim().length()>0 && txtPRTCD.getText().trim().length()!=5)
			{
				setMSG("Please Enter proper Party Code or Press f1 for Help ",'E');
				txtPRTCD.requestFocus();
				return false;
			}
			if(txtOTHCR.getText().trim().equals("0Z"))
			{
				setMSG("Please Enter proper Category or Press f1 for Help ",'E');
				txtOTHCR.requestFocus();
				return false;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	/**
	 * Method to generate the header of the Report.
	 */
	void prnHEADER()
	{
		try
		{
			String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
			String strCRDTP1 = L_strCRDTP.substring(2,L_strCRDTP.length()).toString().trim();
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-22));
			//dosREPORT.writeBytes(padSTRING('R',"Credit Note Register",strDOTLN.length()-22));
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(padSTRING('R',"Date    :"+cl_dat.M_strLOGDT_pbst,22));		
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(strCRDTP1+"","")+" Register from : "+txtFMDAT.getText().trim()+""+" To "+txtTODAT.getText().trim()+"",strDOTLN.length()-22));
			dosREPORT.writeBytes(padSTRING('R',"Page No :"+cl_dat.M_PAGENO,22));				
			
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			dosREPORT.writeBytes("Party Name ");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Invoice No. Invoice Dt  grade              Invoice      Amount  Ind no      Rate   Doc.No    Acc.Ref. ",strDOTLN.length()));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," Qty                          Rs/MT ",strDOTLN.length()-25));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst += 8;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER ");
		}
	}
	
	/**
	 * Method to generate the Report & send to the selected Destination.
	*/
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpcnr.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpcnr.doc";
			getDATA();
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
}
			
	