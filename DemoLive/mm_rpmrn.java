/*
System Name   : Material Management System
Program Name  : List Of Material Return Note
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 19/10/2005
Version       : MMS v2.0.0
*/
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.sql.ResultSet;import java.util.Hashtable;import java.sql.Date;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;

/**<pre>
System Name : Material Management System.
 
Program Name : List Of Material Return Note

Purpose : Program for List Of Material Return Note for given Date/MRN Range .

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_MRMST       MR_MMSBS,MR_STRTP,MR_MRNNO,MR_MATCD                     #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
cmbRPTOP    To Specify Report Option as Material Return Note or List of MRN
txtFMMRN    MR_MRNNO                MM_MRMST           VARCHAR(8)    MRN Number
txtTOMRN    MR_MRNNO                MM_MRMST           VARCHAR(8)    MRN Number    
txtFMDAT    MR_MRNDT                MM_MRMST           Date          MRN Date
txtTODAT    MR_MRNDT                MM_MRMST           Date          MRN Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from MM_MRMST for condiations :-
    1) MR_STRTP = Specified Store Type.
  if Material Return Note		
    2) AND MR_MRNNO between given range of MRN Number.
  if List Of MRN Selected			
	3) AND MR_MRNDT between given range of dates.
    4) AND ifnull(MR_STSFL,' ') <> 'X' 

<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then current Date.
    - MRN numbre entered Must be valid.
</I> */
class mm_rpmrn extends cl_rbase
{									/** JComboBox to specify Report Option Type.*/
	private JComboBox cmbRPTOP;		/** JTextField to display to enter From MRN Number.*/
	private JTextField txtFMMRN;	/** JTextField to display * to enter To MRN Number.*/
	private JTextField txtTOMRN;	/** JTextField to display to enter From Date.*/
	private JTextField txtFMDAT;	/** JTextField to display & to enter To Date.*/
	private JTextField txtTODAT;	/** String variable for Department Code.*/	
	private String strDPTCD;		/** String variable for MRN Number.*/
	private String strMRNNO;		/** String variable for Remark description.*/
	private String strREMDS;		/** String variable for Temp MRN Number.*/
	private String strMRNNO1;		/** String variable for Material code.*/
	private String strMATCD;		/** String variable for Material description.*/
	private String strMATDS;		/** String variable for Unit of Measurment Code.*/
	private String strUOMCD;		/** String varaible for Location Code.*/
	private String strLOCCD;		/** String variable for ISO Document Number.*/
	private String strISO1;			/** String variable for ISO Document Number.*/
	private String strISO2;			/** String variable for ISO Document Number.*/
	private String strISO3;			/** Integer variable for iteam count.*/
	private int intITEMCT;			/** Flag to print status.*/
	private boolean flgPRINT;		/** String variable for generated Report File Name.*/
	private String strFILNM;		/** Integer variable to count the number of records fetched, to block the Report if no data found.*/
	private int intRECCT ;			/** Hashtable Object to hold the department code & associated Description.*/
	private Hashtable<String,String> hstDPTCD;		/** FileOutputStream to generate the Report file from the stream of data.*/
	private FileOutputStream fosREPORT ;/** DataOutputStream Object to generate to hold the Stream of Report Data.*/   
    private DataOutputStream dosREPORT ;/** String variable to print Dotted line in the Report.*/
	private String strDOTLN = "------------------------------------------------------------------------------------------------";
	private String strPRTCD,strPRTNM;
	private JTextField txtPRTCD,txtPRTNM;
	private String strPREBY,strPREDT,strFRWBY,strFRWDT,strAPRBY,strAPRDT,strAUTBY,strAUTDT;
    public mm_rpmrn()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);	
			
			add(new JLabel("Report Option"),4,4,1,1,this,'L');
			add(cmbRPTOP = new JComboBox(),4,5,1,1.5,this,'L');
			add(new JLabel("From MRN No."),5,4,1,1,this,'L');
			add(txtFMMRN = new TxtNumLimit(8.0),5,5,1,1.5,this,'L');
			add(new JLabel("To MRN No."),6,4,1,1,this,'L');
			add(txtTOMRN = new TxtNumLimit(8.0),6,5,1,1.5,this,'L');
			add(new JLabel("From Date"),7,4,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),7,5,1,1.5,this,'L');
			add(new JLabel("To Date."),8,4,1,1,this,'L');
			add(txtTODAT = new TxtDate(),8,5,1,1.5,this,'L');
			add(new JLabel("Contractor"),9,4,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(5),9,5,1,1,this,'L');
			add(txtPRTNM = new TxtLimit(40),9,6,1,3,this,'L');
			cmbRPTOP.addItem("Material Return Note");
			cmbRPTOP.addItem("List Of MRN.");
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
			hstDPTCD = new Hashtable<String,String>(10,0.8f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
			+"CMT_CGSTP = 'COXXDPT'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())				
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));				
				M_rstRSSET.close();
			}									
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor ..");
		}
	}
	mm_rpmrn(String P_strSBSCD)
	{
		try
		{
			M_strSBSCD = P_strSBSCD;
            M_rdbTEXT.setSelected(true);
			hstDPTCD = new Hashtable<String,String>(10,0.8f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'COXXDPT'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())				
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));				
				M_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
	}
	/**
	 * Super class method overrided to enable & disable the Components.
	 */
	public void setENBL(boolean ACTION)
	{
		super.setENBL(ACTION);
		if(ACTION == false)
			return;
		if(cmbRPTOP.getSelectedIndex() == 0)
		{
			txtFMMRN.setEnabled(true);
			txtTOMRN.setEnabled(true);
			txtFMDAT.setEnabled(false);
			txtTODAT.setEnabled(false);
			txtPRTCD.setEnabled(false);
		}
		else
		{
			txtFMMRN.setEnabled(false);
			txtTOMRN.setEnabled(false);
			txtFMDAT.setEnabled(true);
			txtTODAT.setEnabled(true);
			txtPRTCD.setEnabled(true);
		}
		txtPRTNM.setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				setENBL(true);
				txtFMMRN.requestFocus();				
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == cmbRPTOP)
		{
			try
			{
				setENBL(true);
				txtFMMRN.setText("");
				txtTOMRN.setText("");
				txtFMDAT.setText("");
				txtTODAT.setText("");
				txtPRTCD.setText("");
				txtPRTNM.setText("");
				if(cmbRPTOP.getSelectedIndex() == 0)
					txtFMMRN.requestFocus();
				else
				{
					txtFMDAT.requestFocus();			
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == cmbRPTOP");
			}
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtFMMRN)
			{
				M_strHLPFLD = "txtFMMRN";
				M_strSQLQRY = "SELECT DISTINCT MR_MRNNO,MR_DPTCD FROM MM_MRMST WHERE ";
				M_strSQLQRY += " MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,' ') <> 'X' AND MR_STRTP ='";
				M_strSQLQRY += M_strSBSCD.substring(2,4) +"'";
				if(txtFMMRN.getText().length()>0)
					M_strSQLQRY += " AND MR_MRNNO like '"+txtFMMRN.getText()+"%'";
				if(txtTOMRN.getText().length() == 8)
					M_strSQLQRY += " AND MR_MRNNO <= '"+txtTOMRN.getText()+"'";				
				M_strSQLQRY += " ORDER BY MR_MRNNO DESC";
				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"MRN Number","Dept Code"},2,"CT");
			}
			if(M_objSOURC == txtTOMRN)
			{
				M_strHLPFLD = "txtTOMRN";
				M_strSQLQRY = "SELECT DISTINCT MR_MRNNO,MR_DPTCD FROM MM_MRMST WHERE";
				M_strSQLQRY += " MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,' ') <> 'X' AND MR_STRTP ='";
				M_strSQLQRY += M_strSBSCD.substring(2,4) +"'";							
				if(txtTOMRN.getText().length()>0)
					M_strSQLQRY += " AND MR_MRNNO like '"+txtTOMRN.getText()+"%'";
				if(txtFMMRN.getText().length() == 8)
					M_strSQLQRY += " AND MR_MRNNO >= '"+txtFMMRN.getText()+"'";				
				M_strSQLQRY += " ORDER BY MR_MRNNO DESC";
				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"MRN Number","Dept Code"},2,"CT");
			}
			else if(M_objSOURC == txtPRTCD)				// Usage Type
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPRTCD";
				String L_ARRHDR[] = {"Party Code","Party Name"};
				M_strSQLQRY = "Select MR_PRTCD,PT_PRTNM from MM_MRMST,CO_PTMST";
				M_strSQLQRY += " where PT_PRTTP = 'S' AND MR_PRTCD = PT_PRTCD AND MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP ='";
				M_strSQLQRY += M_strSBSCD.substring(2,4)+"'";
				if(txtPRTCD.getText().trim().length() >0)
					M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText()+"%'";	
				M_strSQLQRY += " Order By PT_PRTCD";
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMMRN)
			{				
				txtTOMRN.requestFocus();
			}
			else if(M_objSOURC == txtFMDAT)
			{				
				txtTODAT.requestFocus();
			}
			else if(M_objSOURC == txtTODAT)
			{				
				txtPRTCD.requestFocus();
			}
			else if(M_objSOURC == txtPRTCD)
			{				
			    cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if(M_objSOURC == txtTOMRN)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC == txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();			
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == cmbRPTOP)
			setMSG("Select Report Option ..",'N');
		if(M_objSOURC == txtFMMRN)
			setMSG("Enter From MRN Number Or press 'F1' For Help ..",'N');
		if(M_objSOURC == txtTOMRN)
			setMSG("Enter To MRN Number Or Press 'F1' For Help ..",'N');
		if(M_objSOURC == txtFMDAT)
			setMSG("Enter From Date ..",'N');
		if(M_objSOURC == txtTODAT)
			setMSG("Enter To Date ..",'N');
	}	
	/**
	 * Super Class method to to execuate the F1 Help for selectedf Field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtFMMRN"))
		{
			txtFMMRN.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD.equals("txtTOMRN"))
		{
			txtTOMRN.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		else if(M_strHLPFLD.equals("txtPRTCD"))
		{
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
	}	
	/**
	 * Method to generate the Report & to forward it to specified Destination
	 */
	void exePRINT()
	{
		try
		{
			genREPORT(txtFMMRN.getText().trim(),txtTOMRN.getText().trim(),cmbRPTOP.getSelectedIndex(),txtPRTCD.getText().trim());
			
			if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
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
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"List Of Material Return Note"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to fetch data from the data base & to club it with header & footer.
	 */
	public void genREPORT(String P_strFMMRN,String P_strTOMRN,int P_intRPTOP,String P_strPRTCD)
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			intRECCT = 0;			
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpmrn.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpmrn.doc";

            M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
				+ " AND CMT_CGSTP = 'MMXXRPT' AND isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET != null)
			{
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(L_strTEMP.equals("MM_RPMRN01"))
						strISO1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					else if(L_strTEMP.equals("MM_RPMRN02"))
						strISO2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					else if(L_strTEMP.equals("MM_RPMRN03"))
						strISO3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");					
				}
				M_rstRSSET.close();
			}					
			intITEMCT = 0;
			strMRNNO = "";
			strDPTCD = ""; strMATDS = ""; strUOMCD = ""; strLOCCD = "";
			ResultSet L_rstRSSET;
			
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>List Of Material Return Note</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			M_strSQLQRY = "SELECT MR_MRNNO,MR_MRNDT,MR_MATCD,MR_ISSNO,MR_RETQT,MR_DPTCD,MR_MRNRT,MR_ORGCD,MR_PRTTP,MR_PRTCD,MR_PREBY,MR_FRWBY,MR_FRWDT,MR_APRBY,MR_STSFL,MR_APRDT,MR_AUTBY,MR_AUTDT,PT_PRTNM "
				+" FROM MM_MRMST LEFT OUTER JOIN CO_PTMST ON MR_PRTTP = PT_PRTTP and MR_PRTCD = PT_PRTCD AND isnull(PT_STSFL,'') <>'X' WHERE MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND MR_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";		
			if(P_intRPTOP == 0)
				M_strSQLQRY += "AND MR_MRNNO BETWEEN '"+P_strFMMRN+"' AND '"+P_strTOMRN+"' ";
			else
				M_strSQLQRY +=" AND MR_MRNDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
			if(P_strPRTCD.length() >0)
			    M_strSQLQRY +=" AND MR_PRTCD ='"+P_strPRTCD+"'";
			M_strSQLQRY +=" AND isnull(MR_STSFL,' ') <> 'X' ORDER BY MR_DPTCD,MR_PRTCD,MR_MRNNO,MR_MATCD ";
			//System.out.println(M_strSQLQRY);									
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(P_intRPTOP == 1)
				prnHEADER();
			if(M_rstRSSET != null)
			{
			//	System.out.println(1);
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					//System.out.println(2);
					strMRNNO1 = nvlSTRVL(M_rstRSSET.getString("MR_MRNNO"),"");
					if(M_rstRSSET.getString("MR_MATCD").equals(M_rstRSSET.getString("MR_ORGCD")))
						strMATCD = nvlSTRVL(M_rstRSSET.getString("MR_MATCD"),"");
					else
						strMATCD = nvlSTRVL(M_rstRSSET.getString("MR_ORGCD"),"");
					strPRTCD = 	nvlSTRVL(M_rstRSSET.getString("MR_PRTCD"),"");
					strPRTNM =  nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					//M_strSQLQRY = "SELECT CT_MATDS,CT_UOMCD,ST_LOCCD FROM CO_CTMST LEFT OUTER JOIN MM_STMST ON ST_MATCD = CT_MATCD "
					//	+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CT_CODTP = 'CD' AND CT_MATCD = '"+strMATCD+"'";
					
					M_strSQLQRY ="Select ST_LOCCD from MM_STMST where ST_MATCD = '"+strMATCD+"' and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET!= null && L_rstRSSET.next())
					{
						strLOCCD = nvlSTRVL(L_rstRSSET.getString("ST_LOCCD"),"");
					}
					else
						strLOCCD = "";
					
					M_strSQLQRY ="Select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST where ct_codtp ='CD' and CT_MATCD = '"+strMATCD+"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET!= null && L_rstRSSET.next())
					{
						strMATDS = nvlSTRVL(L_rstRSSET.getString("CT_MATDS"),"");
						strUOMCD = nvlSTRVL(L_rstRSSET.getString("CT_UOMCD"),"");
						//strLOCCD = nvlSTRVL(L_rstRSSET.getString("ST_LOCCD"),"");
					}
						
					if(P_intRPTOP == 0)
					{
						if(!strMRNNO.equals(nvlSTRVL(M_rstRSSET.getString("MR_MRNNO"),"")))
						{
							if(strMRNNO.equals(""))
								prnNOTHD();
							else
							{
								prnFOOTER();								
								prnNOTHD();
							}
							strMRNNO = nvlSTRVL(M_rstRSSET.getString("MR_MRNNO"),"");
						}
					}
					if(cl_dat.M_intLINNO_pbst >59)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						
						if(P_intRPTOP == 1)
							strMRNNO = "";
						strDPTCD = "";
						if(P_intRPTOP == 0)
							prnNOTHD();
						else
							prnHEADER();
					}
					if(P_intRPTOP == 0)
					{
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MR_MATCD"),""),26));
						dosREPORT.writeBytes(padSTRING('R',strUOMCD,12));
						dosREPORT.writeBytes(padSTRING('R',strLOCCD,14));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MR_ISSNO"),""),12));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MR_RETQT"),"0.0"),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MR_MRNRT"),"0.0"),17));
					}
					else
					{
						if(!(strDPTCD+strPRTCD).equals(nvlSTRVL(M_rstRSSET.getString("MR_DPTCD"),"")+nvlSTRVL(M_rstRSSET.getString("MR_PRTCD"),"")))
						{
							strDPTCD = nvlSTRVL(M_rstRSSET.getString("MR_DPTCD"),"");
							dosREPORT.writeBytes(padSTRING('R',"Department : "+hstDPTCD.get(strDPTCD).toString(),45));
							dosREPORT.writeBytes(strPRTCD+" "+strPRTNM);
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 1;
						}
						if(!strMRNNO.equals(nvlSTRVL(M_rstRSSET.getString("MR_MRNNO"),"")))
						{
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MR_MRNNO"),""),11));
							strMRNNO = nvlSTRVL(M_rstRSSET.getString("MR_MRNNO"),"");
							flgPRINT = true;
						}
						else
							dosREPORT.writeBytes(padSTRING('R',"",11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MR_MATCD"),""),61));
					}
					dosREPORT.writeBytes("\n");
					if(P_intRPTOP == 0)
						dosREPORT.writeBytes(padSTRING('R',strMATDS,61)); 
					else
					{
						if(flgPRINT)
						{
							if(M_rstRSSET.getDate("MR_MRNDT") != null)
								dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("MR_MRNDT")),11));
							else
								dosREPORT.writeBytes(padSTRING('R',"",11));
							flgPRINT = false;
						}
						else
							dosREPORT.writeBytes(padSTRING('R',"",11));
						dosREPORT.writeBytes(padSTRING('R',strMATDS,61));
						dosREPORT.writeBytes(padSTRING('R',strUOMCD,4));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MR_RETQT"),""),10));
						dosREPORT.writeBytes(padSTRING('L',String.valueOf(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("MR_RETQT"),"0.0")) * Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("MR_MRNRT"),"0.0"))),11));
					}
					dosREPORT.writeBytes("\n\n");
					intITEMCT++;
					cl_dat.M_intLINNO_pbst += 3;
				}
			}
			if(P_intRPTOP == 0)
				prnFOOTER();
			else
			{
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes("Total No Of Items : "+intITEMCT);
			}			
			strDPTCD = "";
			intITEMCT = 0;
			strMRNNO = "";
			strDPTCD = "";
			
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			setMSG("Report completed.. ",'N');			
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG("",'N');
		}
	}
	/**
	 * Method to validate the input given before execuation of the SQL query.
	 */
	boolean vldDATA()
	{
		try
		{						
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				if(txtFMDAT.getText().trim().length() == 0)
				{
					setMSG("Enter From Date..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date Should Be Grater Than Today..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				else if(txtTODAT.getText().trim().length() == 0)
				{
					setMSG("Enter To Date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date Should Not Be Grater Than Today..",'E');
					txtTODAT.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
				{
					setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
			}
			else
			{
				if(txtFMMRN.getText().trim().length() == 0)
				{
					setMSG("Enter From MRN Number ..",'E');
					txtFMMRN.requestFocus();
					return false;
				}
				if(txtTOMRN.getText().trim().length() == 0)
				{
					setMSG("Enter To MRN Number ..",'E');
					txtTOMRN.requestFocus();
					return false;
				}
				if(Integer.parseInt(txtFMMRN.getText()) > Integer.parseInt(txtTOMRN.getText()))
				{
					setMSG("To MRN Number Grater Than Or Equal To From MRN Number..",'E');
					txtFMMRN.requestFocus();
					return false;
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
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/**
	 * Metod to generate Header Part of the Report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n");			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())			
				dosREPORT.writeBytes("<b>");			
			dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"List Of Material Return From "+txtFMDAT.getText()+" To "+txtTODAT.getText(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");						
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("MRN No.    Material Code                                               UOM    Quantity     Value"+"\n");			
			dosREPORT.writeBytes("MRN Dt.    Material Description                                                                 "+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())			
				dosREPORT.writeBytes("</b>");			
			cl_dat.M_intLINNO_pbst = 8;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to generate the Header part for MRN Note.
	 */
	public void prnNOTHD()
	{
		try
		{		
			strDPTCD = nvlSTRVL(M_rstRSSET.getString("MR_DPTCD"),"");
			strPREDT =" ";strAPRDT =" ";strFRWDT =" ";strAUTDT =" ";
			dosREPORT.writeBytes("\n"+padSTRING('L',"-------------------------------",strDOTLN.length())+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO1,strDOTLN.length())+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO2,strDOTLN.length())+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO3,strDOTLN.length())+"\n");			
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length())+"\n");			
			dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst +"\n");			
			dosREPORT.writeBytes("Material Return Note" +"\n");			
		//	dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+M_strSBSCD.substring(2,4),strDOTLN.length()-21));
			dosREPORT.writeBytes("MRN No. : "+nvlSTRVL(M_rstRSSET.getString("MR_MRNNO"),"")+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Department  : "+hstDPTCD.get(strDPTCD),strDOTLN.length()-21));
			if(M_rstRSSET.getDate("MR_MRNDT") != null)
				dosREPORT.writeBytes(padSTRING('R',"MRN Dt. : "+M_fmtLCDAT.format(M_rstRSSET.getDate("MR_MRNDT")),20));
			else
				dosREPORT.writeBytes(padSTRING('R',"MRN Dt. : ",20));			
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Contractor  : "+nvlSTRVL(M_rstRSSET.getString("MR_PRTCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Material Code            UOM          Location      Issue Number       Ret Qty.        Mat. Rate"+"\n");			
			dosREPORT.writeBytes("Material Description                                                                            "+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");
			strPREBY = nvlSTRVL(M_rstRSSET.getString("MR_PREBY"),"");
			strFRWBY = nvlSTRVL(M_rstRSSET.getString("MR_FRWBY"),"");
			if(!nvlSTRVL(M_rstRSSET.getString("MR_STSFL"),"").equals("3"))
			{
				strAPRBY = nvlSTRVL(M_rstRSSET.getString("MR_APRBY"),"");
				if(M_rstRSSET.getDate("MR_APRDT") != null)
				strAPRDT = M_fmtLCDAT.format(M_rstRSSET.getDate("MR_APRDT"));
			}
			else
			{
				strAPRBY ="";
				strAPRDT ="";
			}
			strAUTBY = nvlSTRVL(M_rstRSSET.getString("MR_AUTBY"),"");
			
			//if(M_rstRSSET.getDate("MR_PREDT") != null)
			//	strPREDT = M_fmtLCDAT.format(M_rstRSSET.getDate("MR_PREDT"));
			if(M_rstRSSET.getDate("MR_FRWDT") != null)
				strFRWDT = M_fmtLCDAT.format(M_rstRSSET.getDate("MR_FRWDT"));
			
			if(M_rstRSSET.getDate("MR_AUTDT") != null)
				strAUTDT = M_fmtLCDAT.format(M_rstRSSET.getDate("MR_AUTDT"));
			
			cl_dat.M_intLINNO_pbst = 15;
		}
		catch(Exception L_IO)
		{
			setMSG(L_IO,"getRPHDR,File I/O ");
		}
	}
	/**
	* Method to generate the footer of the Report.
	*/	
	public void prnFOOTER()
	{
		try
		{
			M_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"RM_TRNTP = 'MR' AND RM_DOCNO = '"+strMRNNO1+"' AND "
				+"isnull(RM_STSFL,' ') <> 'X' ";
				System.out.println(M_strSQLQRY);
			ResultSet L_rstREMRK = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstREMRK != null)
			{
				if(L_rstREMRK.next())
					strREMDS = nvlSTRVL(L_rstREMRK.getString("RM_REMDS"),"");
				else
					strREMDS = "";
				L_rstREMRK.close();
			}			
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Remarks : "+strREMDS);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Prepared By         Forwarded By          Approved By          Accepted By       Stores Officer");
			dosREPORT.writeBytes("\n\n\n");
			dosREPORT.writeBytes(padSTRING('R',strPREBY,20)+padSTRING('R',strFRWBY,22)+padSTRING('R',strAPRBY,20));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Dt : "+strPREDT,20)+padSTRING('R',"Dt : "+strFRWDT,22)+padSTRING('R',"Dt : "+ strAPRDT,21)+padSTRING('R',"Dt : ",18)+padSTRING('R',"Dt : "+ strAUTDT,20));
			dosREPORT.writeBytes("\n");
		//	dosREPORT.writeBytes("Dt.                  Dt.                   Dt.                  Dt.               Dt.           ");
		//	dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strEJT);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPFTR");
		}
	}
}