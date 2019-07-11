/*
System Name   : Marketing System
Program Name  : Delivery Status Report
Purpose		  : This program is used to generate Delivery Status Report.



List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MR_IVTRN      IVT_MKTTP,IVT_INDNO					 #		  #        #     
MR_INMST      IN_MKTTP,IN_INDNO,IN_DSTCD			          #        #
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD			 #		  #		   #
CO_PRMST													  #
CO_PTMST													  #
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   For Generating the report

   Data is taken from  :-
	MR_IVTRN  : Invoice table
	MR_INMST  : Destination code-IN_DSTCD lead time for this to be taken from CO_CDTRN
	CO_CDTRN  : Destination code are kept in SYS/COXXDST and lead time is in CMT_NCSVL
	CO_PRMST  : Grade description to be taken (PR_PRDDS) for given PR_PRDCD
				PRDCD to be taken from IVT_PRDCD
	CO_PTMST  : Customer/Transporter Name from (PT_PRTNM)
				for Customer
				Where PT_PRTTP = 'C' and PT_PRTCD = value of IVT_BYRCD
				for Transporter
				Where PT_PRTTP = 'T' and PT_PRTCD = value of IVT_TRPCD
	
	Input for Report :
	Expected Date of Delivery : arrive on it by adding lead time (no. of days) in invoice date
	
    M_strSQLQRY = "SELECT IVT_INDNO,IVT_BYRCD,IVT_PRDDS,IVT_INVQT,IVT_INVNO,DATE(IVT_INVDT)INVDT,CMT_CODDS,CMT_NCSVL,IVT_TRPCD,DATE(DAYS(DATE(IVT_INVDT))+CMT_NCSVL)L_EXPDT ";
    M_strSQLQRY +=" FROM MR_IVTRN, MR_INMST,CO_CDTRN WHERE ";
      1) IVT_MKTTP = IN_MKTTP
      2) AND IVT_INDNO = IN_INDNO
      3) AND CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDST' 
      4) AND CMT_CODCD = IN_DSTCD 
	  5) AND DATE(IVT_INVDT) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
	    (for Invoice Date)
      6) AND DATE(DAYS(DATE(IVT_INVDT))+CMT_NCSVL) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
		(for Expected Delivery Date)
	  7) order by IVT_BYRCD (Customer Name)
      8) order by IVT_TRPCD (Transporter)
	  9) order by CMT_CODDS (Destination)

<B>Validations & Other Information:</B>    
</I>
*/

import java.sql.Date;import java.sql.ResultSet;import java.awt.event.FocusEvent;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.util.Hashtable;

class mr_rpdls extends cl_rbase
{
	private JTextField txtFMDAT;    /** JtextField to display & enter From Date to generate the Report.*/
	private JTextField txtTODAT;    /** JtextField to display & enter To Date to generate the Report.*/
	private String strFILNM;	    /** String Variable for generated Report file Name.*/ 
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
	
	private JRadioButton rdbINVDT;  /** JRadioButton for Invoice Date. */
	private JRadioButton rdbEXPDT;  /** JRadioButton for Expected Delivery Date. */
	private JRadioButton rdbCSTNM;  /** JRadioButton for Customer Name. */
	private JRadioButton rdbTRNSP;  /** JRadioButton for Transportor. */
	private JRadioButton rdbDESTI;  /** JRadioButton for Destination. */
	private ButtonGroup btgORDER;   /** ButtonGroup for Customer Name,Transportor,Destination */
	private ButtonGroup btgIVEDT;   /** ButtonGroup for Invoice Date,Expected Delivery Date */
	
	private String strBYRCD;		/**variable for IVT_BYRCD  */
	private String strTRPCD;		/**variable for IVT_TRPCD  */
	private String strCODCD;		/**variable for CMT_CODDS  */
	
	private String strPBYRF = "";   /**previous bayer code reference */
	private String strPTRRF = "";   /**previous transporter code reference */
	private String strPCORF = "";	/**previous destination code reference */				
	
	private String strPRTTP;        /**variable for party type */
	private String strPRTCD;        /**variable for party code */
	private String strPRTNM;        /**variable for party name */
	private String strBYRNM;        /**variable for store customer name */
	private String strTRPNM;        /**variable for store Transporter */
	
	private Hashtable<String,String> hstPTMST;		/** Hash table for storing Code */
	private String strSPACE = " ";  /**variable for define space */
	private int intRECCT;		    /** Integer Variable to count records fetched, to block report generation if data not found.*/

	
	Date L_datTEMP;					/**variable for date */
	private String strDOTLN = "-----------------------------------------------------------------------------------------------------------------------------------------------------------";
	
 mr_rpdls()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			btgORDER=new ButtonGroup();
			btgIVEDT=new ButtonGroup();

			setMatrix(20,8);
			add(new JLabel("Report On :"),3,3,1,1,this,'R');
			add(rdbINVDT=new JRadioButton("Invoice Date",true),3,4,1,1,this,'L');
			add(rdbEXPDT=new JRadioButton("Expected Delivery Date",true),3,5,1,2,this,'L');
			btgIVEDT.add(rdbINVDT);
			btgIVEDT.add(rdbEXPDT);
			add(new JLabel("From Date:"),5,3,1,1,this,'R');
			add(txtFMDAT = new TxtDate(),5,4,1,1.5,this,'L');
			add(new JLabel("To Date  :"),6,3,1,1,this,'R');
			add(txtTODAT = new TxtDate(),6,4,1,1.5,this,'L');
			
			add(new JLabel("Order By :"),8,3,1,1,this,'R');
			add(rdbCSTNM=new JRadioButton("Customer Name",true),8,4,1,2,this,'L');
			add(rdbTRNSP=new JRadioButton("Transporter",true),9,4,1,2,this,'L');
			add(rdbDESTI=new JRadioButton("Destination",true),10,4,1,2,this,'L');
			btgORDER.add(rdbCSTNM);
			btgORDER.add(rdbTRNSP);
			btgORDER.add(rdbDESTI);

			hstPTMST = new Hashtable<String,String>();
			hstPTMST.clear();
           	M_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP in ('C','T')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{	
					strPRTTP = nvlSTRVL(M_rstRSSET.getString("PT_PRTTP"),"");
					strPRTCD = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
					strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					hstPTMST.put(strPRTTP+strPRTCD,strPRTNM);
					
				}
				M_rstRSSET.close();
			}
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
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
				txtFMDAT.requestFocus();
				setMSG("Please Enter Date to generate the Report..",'N');
			}
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		
		}
	}
 
 public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
				{
					//cl_dat.M_btnSAVE_pbst.requestFocus();
					txtTODAT.requestFocus();
					setMSG("plz Enter Date",'N');
				}
				else
				{
					txtFMDAT.requestFocus();
					setMSG("plz Enter Date",'N');
				}
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
				{
					txtTODAT.requestFocus();
					setMSG("plz Enter Date",'N');
				}
			}
		}
		setMSG("",'N');
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
		java.sql.Date jdtTEMP;
		cl_dat.M_intLINNO_pbst =0;
		setCursor(cl_dat.M_curWTSTS_pbst);
		
		String L_strFMDAT = txtFMDAT.getText().trim();
		String L_strTODAT = txtTODAT.getText().trim();
		//System.out.println(L_strFMDAT);
		
		try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>"+"</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			
			prnHEADER();
			
			M_strSQLQRY = "SELECT IVT_INDNO,IVT_BYRCD,IVT_PRDDS,IVT_INVQT,IVT_INVNO,CONVERT(varchar,IVT_INVDT,103)INVDT,CMT_CODDS,CMT_NCSVL,IVT_TRPCD,CONVERT(varchar,DAY(CONVERT(varchar,IVT_INVDT,101))+CMT_NCSVL,101)L_EXPDT ";
			M_strSQLQRY +=" FROM MR_IVTRN, MR_INMST,CO_CDTRN WHERE ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_CMPCD = IN_CMPCD and IVT_MKTTP = IN_MKTTP AND IVT_INDNO = IN_INDNO ";
			M_strSQLQRY +="  AND CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDST' AND CMT_CODCD = IN_DSTCD ";
			if(rdbINVDT.isSelected())
			{
				M_strSQLQRY +="  AND CONVERT(varchar,IVT_INVDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
			}
			if(rdbEXPDT.isSelected())
			{
				M_strSQLQRY +="  AND CONVERT(varchar,DAY(CONVERT(varchar,IVT_INVDT,101))+CMT_NCSVL,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
			}
			M_strSQLQRY += " AND IVT_SBSCD1 in "+M_strSBSLS;
			if(rdbCSTNM.isSelected())
			{
				M_strSQLQRY +=" order by IVT_BYRCD";
			}
			if(rdbTRNSP.isSelected())
			{
				M_strSQLQRY +=" order by IVT_TRPCD";
			}
			if(rdbDESTI.isSelected())
			{
				M_strSQLQRY +=" order by CMT_CODDS";
			}
			
			
			// L_EXPDT,IN_DSTCD,
			//System.out.println(M_strSQLQRY);
			strPBYRF = "";
			strPTRRF = "";
			strPCORF = "";	
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{ 
				while(M_rstRSSET.next())
				{
					intRECCT=1;
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes(strDOTLN);
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst = 0;
					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
					//
						prnHEADER();
					}
					
					strBYRCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
					strTRPCD = nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),"");
					strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					strBYRNM = (String)hstPTMST.get("C"+strBYRCD);
					strTRPNM = (String)hstPTMST.get("T"+strTRPCD);
					if(rdbCSTNM.isSelected())
					{	
						if(!strPBYRF.equals(strBYRCD))
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',strBYRCD +" "+strBYRNM,40)+"\n");
							dosREPORT.writeBytes("\n");					
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),12));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),20));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),7));
							dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),12));
							L_datTEMP = M_rstRSSET.getDate("INVDT");
							if(L_datTEMP !=null)
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),14));	
							else
							{
								dosREPORT.writeBytes(padSTRING('R'," ",14));
							}
							dosREPORT.writeBytes(padSTRING('R',strTRPNM,34));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),12));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							L_datTEMP = M_rstRSSET.getDate("L_EXPDT");
							if(L_datTEMP !=null)
							    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),10)+"\n");
						    else
								dosREPORT.writeBytes(padSTRING('R'," ",10)+"\n");
							strPBYRF = strBYRCD;
							//System.out.println("BY RF:"+strPBYRF);
							//System.out.println("BY cd:"+strBYRCD);
							cl_dat.M_intLINNO_pbst += 4;
						}
						else
						{
							//System.out.println("in else");
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),12));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),20));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),7));
							dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),12));
							L_datTEMP = M_rstRSSET.getDate("INVDT");
							if(L_datTEMP !=null)
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),14));	
							else
							{
								dosREPORT.writeBytes(padSTRING('R'," ",14));
							}
							dosREPORT.writeBytes(padSTRING('R',strTRPNM,34));	
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),12));	
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							L_datTEMP = M_rstRSSET.getDate("L_EXPDT");
							if(L_datTEMP !=null)
							    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),10)+"\n");
						    else
								dosREPORT.writeBytes(padSTRING('R'," ",10)+"\n");
						
							cl_dat.M_intLINNO_pbst += 1;
						}
					}
					if(rdbTRNSP.isSelected())
					{
						if(!strPTRRF.equals(strTRPCD))
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',strTRPCD +" "+strTRPNM,35)+"\n");	
							dosREPORT.writeBytes("\n");					
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),12));
							dosREPORT.writeBytes(padSTRING('R',""+strBYRNM,34));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),20));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),7));
							dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),12));
							L_datTEMP = M_rstRSSET.getDate("INVDT");
							if(L_datTEMP !=null)
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),14));	
							else
							{
								dosREPORT.writeBytes(padSTRING('R'," ",14));
							}
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),12));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							L_datTEMP = M_rstRSSET.getDate("L_EXPDT");
							if(L_datTEMP !=null)
							    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),10)+"\n");
						    else
								dosREPORT.writeBytes(padSTRING('R'," ",10)+"\n");
													
							strPTRRF = strTRPCD;
							cl_dat.M_intLINNO_pbst += 4;
						}
						else
						{	//System.out.println("in else");
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),12));
							dosREPORT.writeBytes(padSTRING('R',""+strBYRNM+"",34));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),20));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),7));
							dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),12));
							L_datTEMP = M_rstRSSET.getDate("INVDT");
							if(L_datTEMP !=null)
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),14));	
							else
							{
								dosREPORT.writeBytes(padSTRING('R'," ",14));
							}
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),12));	
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							L_datTEMP = M_rstRSSET.getDate("L_EXPDT");
							if(L_datTEMP !=null)
							    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),10)+"\n");
						    else
								dosREPORT.writeBytes(padSTRING('R'," ",10)+"\n");
						
							cl_dat.M_intLINNO_pbst += 1;
						}
					}
					if(rdbDESTI.isSelected())
					{
						if(!strPCORF.equals(strCODCD))
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),13)+"\n");	
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),12));
							dosREPORT.writeBytes(padSTRING('R',""+strBYRNM,29));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),12));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),7));
							dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),12));
							L_datTEMP = M_rstRSSET.getDate("INVDT");
							if(L_datTEMP !=null)
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),12));	
							else
							{
								dosREPORT.writeBytes(padSTRING('R'," ",12));
							}
							dosREPORT.writeBytes(padSTRING('R',strTRPNM,29));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							L_datTEMP = M_rstRSSET.getDate("L_EXPDT");
							if(L_datTEMP !=null)
							    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),10)+"\n");
						    else
								dosREPORT.writeBytes(padSTRING('R'," ",10)+"\n");
						
							
							strPCORF = strCODCD;
							cl_dat.M_intLINNO_pbst += 4;
						}
						else
						{	//System.out.println("in else");
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),12));
							dosREPORT.writeBytes(padSTRING('R',""+strBYRNM,29));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),12));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),7));
							dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),12));
							L_datTEMP = M_rstRSSET.getDate("INVDT");
							if(L_datTEMP !=null)
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),12));	
							else
							{
								dosREPORT.writeBytes(padSTRING('R'," ",12));
							}
							dosREPORT.writeBytes(padSTRING('R',strTRPNM,29));
							dosREPORT.writeBytes(padSTRING('R',strSPACE,1));
							L_datTEMP = M_rstRSSET.getDate("L_EXPDT");
							if(L_datTEMP !=null)
							    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),""),10)+"\n");
						    else
								dosREPORT.writeBytes(padSTRING('R'," ",10)+"\n");
						
							cl_dat.M_intLINNO_pbst += 1;
						}
					}
				}
				//System.out.println("Hello");
			}
			//System.out.println("By..");
			M_rstRSSET.close();
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())				
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			
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
		    if(txtFMDAT.getText().trim().length() != 10)
			{
				setMSG("Enter the proper Date..",'E');
				return false;
			}
			if(txtTODAT.getText().trim().length() != 10)
			{
				setMSG("Enter the proper Date..",'E');
				return false;
			}
			if(txtFMDAT.getText().trim().length()==10 && txtTODAT.getText().trim().length()==10)
			{
				//System.out.println("in Second IF");
				setMSG(" ",'N');			
				if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
				{
					setMSG("From Date can not be greater than TO Date's date..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
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
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-21)+"\n");
			if(rdbCSTNM.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Customer wise Delivery Status Report",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			if(rdbTRNSP.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Transporter wise Delivery Status Report",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}	
			if(rdbDESTI.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Destination wise Delivery Status Report",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			if(rdbINVDT.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Invoice Date between : "+txtFMDAT.getText().trim()+""+" and "+txtTODAT.getText().trim()+"",strDOTLN.length()-21));
			}
			if(rdbEXPDT.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Expected Delivery Date between : "+txtFMDAT.getText().trim()+""+" and "+txtTODAT.getText().trim()+"",strDOTLN.length()-21));
			}
			dosREPORT.writeBytes(padSTRING('R',"Date   : "+cl_dat.M_strLOGDT_pbst,strDOTLN.length()-21)+"\n");
			dosREPORT.writeBytes(padSTRING('L',"Page No: "+cl_dat.M_PAGENO,strDOTLN.length()-11)+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
			if(rdbCSTNM.isSelected())
			{			
				dosREPORT.writeBytes(padSTRING('R',"Indent No   Grade                  Qty.  Invoice   Invoice Dt.   Transporter                        Destination  Exp Date     Delivery       Delta between",strDOTLN.length()-1)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			if(rdbTRNSP.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Indent No   Customer Name                      Grade                  Qty.  Invoice   Invoice Dt.   Destination  Exp Date     Delivery       Delta between",strDOTLN.length()-1)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			if(rdbDESTI.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Indent No   Customer Name                 Grade           Qty. Invoice   Invoice Dt. Transporter                   Exp Date       Delivery   Delta between",strDOTLN.length()-1)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
							
			dosREPORT.writeBytes(padSTRING('L',"Dt/Time         Exp & Act Dt ",strDOTLN.length()-1)+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
			cl_dat.M_PAGENO ++;
			//System.out.println("In header "+cl_dat.M_PAGENO);
			cl_dat.M_intLINNO_pbst += 7;

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
		//System.out.println("IN Print");
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpdls.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpdls.doc";
			
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
