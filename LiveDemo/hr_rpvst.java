/*
System Name   : Human Resource Management System
Program Name  : Visitors Status Report.
Program Desc. : Visitors type vise information report.
Author        : Mrs.Dipti.S.Shinde.
Date          : 03rd Jan 2006
System        : Report Program
Version       : MMS v2.0.0
Modificaitons :  
Modified By   :
Modified Date :
Modified det. :
Version       : 
*/
/**
<P><PRE style = font-size : 10 pt >

<b>System Name :</b> Common Master Records.
 
<b>Program Name :</b> Visitors status report.

<b>Purpose :</b> This Report program shows Total no of Visitors 
                 Contractors presen in works area.this head counter program 
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
HR_VSTRN       VS_VSPNO,VS_VSTDT                             #
----------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		 Type/Size       Description
----------------------------------------------------------------------------------------------------------
tblVSTDTL  VS_VSTNM         HR_VSTRN         VARCHAR(35)     Visitor Name   
tblCNTDTL  VS_VSPNO                          VARCHAR(8)      Visitor Pass Number
           VS_PERVS                          VARCHAR(15)     Person visited
           VS_VSTCT                          DECIMAL(3)      Visitor Counter         
----------------------------------------------------------------------------------------------------------
*/
import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JRadioButton;import javax.swing.JComponent;import javax.swing.InputVerifier;
import javax.swing.JCheckBox;import javax.swing.JPanel;import javax.swing.ButtonGroup;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import javax.swing.DefaultCellEditor;import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.util.StringTokenizer;import java.util.Hashtable;

public class hr_rpvst extends cl_rbase
{                                           /**Label for Full Report*/            
	private JLabel lblFLRPT;                /**Label for Visitor Categary Wise Report*/  
	private JLabel lblVSTTP;                /**Label for From Date Report*/  
	private JLabel lblFMDAT;
	private JLabel lblTODAT;
	private JLabel lblREPOP;
	private JLabel lblVSTNM;
	private JLabel lblTOVST;
	private JCheckBox chkCURNT;
	private JTextField txtVSTNM;
	private JTextField txtTOVST;
	private JTextField txtVSTTP;            /**TextField for From Date*/
	private JTextField txtFMDAT;            /**TextField for To Date*/
	private JTextField txtTODAT;            /**RedioButton for Full Report */
	private JRadioButton rdbFLRPT;          /**RedioButton for Visitor Categary Wise */
	private JRadioButton rdbVSTTP;          /**Button group for Visitor's Report */
	private ButtonGroup bgrVSTCD;           /** String for generated Report File Name and Categary Description */
	private String strFILNM,L_strVSCDS="";  /** Counter for record counting */ 
	private int intRECCT=0;				    /** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;  	/** Data output Stream for generating Report File.*/
	private DataOutputStream dosREPORT;	    /** flag to identify group record printing */
	boolean flgGRPREC = false; 
	
	Hashtable<String,String> hstVSTCT = new Hashtable<String,String>();
	hr_rpvst()
	{       
		super(2);
	    try  
	    {	
			setCursor(cl_dat.M_curWTSTS_pbst);
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    bgrVSTCD = new ButtonGroup();
			add(lblREPOP = new JLabel("Report option"),3,3,1,2,this,'L');
			add(rdbVSTTP= new JRadioButton ("Categary ",true),3,4,1,1,this,'L');
			add(rdbFLRPT= new JRadioButton ("Full Report",false),3,5,1,1,this,'L');
			add(lblVSTTP =new JLabel("Type"),4,3,1,1,this,'L');
			add(txtVSTTP = new JTextField(),4,4,1,1,this,'L');
			add(chkCURNT = new JCheckBox("Current Visitor Status"),4,5,1,1.7,this,'L');
			add(lblFMDAT = new JLabel("From Date"),5,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),5,4,1,1,this,'L');
			add(lblTODAT = new JLabel("To Date"),6,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),6,4,1,1,this,'L');
			
			add(lblVSTNM = new JLabel("Visitor Name"),7,3,1,1,this,'L');
			add(txtVSTNM = new TxtLimit(40),7,4,1,1,this,'L');
			
			add(lblTOVST = new JLabel("Person Visited"),8,3,1,1,this,'L');
			add(txtTOVST = new TxtLimit(40),8,4,1,1,this,'L');
			
			//txtVSTTP.addKeyListener(this);
			bgrVSTCD.add(rdbFLRPT);
			bgrVSTCD.add(rdbVSTTP);
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
						
			String L_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			L_strSQLQRY += " where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'HRXXVCT' AND CMT_STSFL <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())			
					hstVSTCT.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));				
				M_rstRSSET.close();
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}   
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}
	}
	
	/**
	 * Super class method overrided to enhance ist functionality.
	 * @param L_flgSTAT boolean variable to pass State fo the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		{	
			txtVSTTP.setEnabled(L_flgSTAT);	
			txtFMDAT.setEnabled(L_flgSTAT);
			txtTODAT.setEnabled(L_flgSTAT);
			lblFMDAT.setEnabled(L_flgSTAT);
			lblTODAT.setEnabled(L_flgSTAT);
			lblREPOP.setEnabled(L_flgSTAT);
			lblVSTTP.setEnabled(L_flgSTAT);
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
	     super.actionPerformed(L_AE);
		 try
		 {
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{ 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setMSG("Please Select Option through F1..",'N');
					setENBL(true);
					txtVSTTP.requestFocus();
					txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
				    txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == rdbFLRPT)
			{
				txtVSTTP.setText("");
				txtVSTTP.setEnabled(false);
				txtFMDAT.requestFocus();
			}
			else if(M_objSOURC == rdbVSTTP)
			{
				txtVSTTP.setEnabled(true);
				txtVSTTP.requestFocus();
			}
			else if(M_objSOURC == txtVSTTP)
			{
				txtFMDAT.requestFocus();
				setMSG("Enter a From Date..",'N');
			}
			else if(M_objSOURC == txtFMDAT)
			{
				txtTODAT.requestFocus();
				setMSG("Enter a TO Date..",'N');
			}
			else if(M_objSOURC == txtTODAT)
			{
				txtVSTNM.requestFocus();
				setMSG("Press F1 to select the name of visitor..",'N');
			}
			else if(M_objSOURC == txtVSTNM)
			{
				txtTOVST.requestFocus();
				setMSG("Press F1 to select the name of person visited ..",'N');
			}
			else if(M_objSOURC == txtTOVST)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else if(M_objSOURC == chkCURNT)
			{
				if(chkCURNT.isSelected())
				{
					txtFMDAT.setEnabled(false);
					txtTODAT.setEnabled(false);
					txtTOVST.setText("");
					txtVSTNM.setText("");
					txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
				    txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else
				{
					txtFMDAT.setEnabled(true);
					txtTODAT.setEnabled(true);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}	
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {			
    	    try
    	  	{
				if(M_objSOURC == txtVSTTP)
				{
					if(rdbVSTTP.isSelected())
					{
						setCursor(cl_dat.M_curWTSTS_pbst);
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtVSTTP";
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
						M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXVCT' and CMT_CHP01='01' ";
						if(txtVSTTP.getText().trim().length()>0)
							M_strSQLQRY += " and CMT_CODCD like '"+ txtVSTTP.getText().trim()+"%' "; 
						M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Visitor Type","Categary"},2,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				if(M_objSOURC == txtVSTNM)
				{
					setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVSTNM";
					M_strSQLQRY = "Select DISTINCT VS_VSTNM from HR_VSTRN ";
				   if(txtVSTTP.getText().length() >0)
					M_strSQLQRY += " where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSCAT ='"+txtVSTTP.getText().trim()+"'";
				   	if(!chkCURNT.isSelected())//for specified date range report..
						M_strSQLQRY+= " AND VS_VSTDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
				   M_strSQLQRY += " order by VS_VSTNM ";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Visitor Name"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtTOVST)
				{
					setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTOVST";
					M_strSQLQRY = "Select DISTINCT VS_PERVS from HR_VSTRN where";
					if(txtVSTTP.getText().length() >0)
					M_strSQLQRY += "  VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSCAT ='"+txtVSTTP.getText().trim()+"'";
				   	if(!chkCURNT.isSelected())//for specified date range report..
						M_strSQLQRY+= " AND VS_VSTDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"'";
					if(txtVSTNM.getText().length() >0)
						M_strSQLQRY += " and VS_VSTNM ='"+txtVSTNM.getText().trim()+"'";
					M_strSQLQRY += " order by VS_PERVS ";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Person Visited"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}

				//M_strSQLQRY = "Select VS_VSPNO,VS_VSTNM,VS_VSTDT from HR_VSTRN ";
    		}
    	    catch(Exception L_EX)
    	    {
    		    setMSG(L_EX ," F1 help..");    	
				setCursor(cl_dat.M_curDFSTS_pbst);
			}    	    
		}
	}
	/**
	 * Super Class method overrided to execuate the F1 Help
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtVSTTP")
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtVSTTP.setText(cl_dat.M_strHLPSTR_pbst);							
		}		
		if(M_strHLPFLD == "txtVSTNM")
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtVSTNM.setText(cl_dat.M_strHLPSTR_pbst);							
		}
		if(M_strHLPFLD == "txtTOVST")
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTOVST.setText(cl_dat.M_strHLPSTR_pbst);							
		}
	}
	/**
	 * Method to generate the Report & to forward it to Specified Destination.
	 */
	void exePRINT()
	{
		try
		{
			if (!vldDATA())
			return;	
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "hr_rpvst.html";				
			else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "hr_rpvst.doc";				
			
			getDATA();
			if(intRECCT ==0)
			{
				setMSG("Data not found ..",'E');
				return;
			}
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Visitors Status Printing"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}					
	}
	/**
	 * Method to fetch Data from the Database & to cloub it with Header & footer of the Report.
	 */
	private void getDATA()
	{
		try  
		{   
			String L_strPRVCAT="",L_strVSTTP="",L_strVSTNM="",L_strVSORG="",L_strVSTCT="",L_strPURPS="",L_strVSPNO="",L_strBDGNO="",L_strVSITM="",L_strCLRBY="";
			String L_strVSOCT="",L_strSHFCD="",L_strVSARA="",L_strVEHNO="",L_strPRTTP="",L_strPRTCD="",L_strCRDNM="",L_strVSCAT="",L_strPERVS="";
			String L_strTFMDAT="",L_strTTODAT="",L_strSRLNO="";
			int L_intNMLEN=0,L_intORGLN=0,L_intSRLNO=0;
		//	vtrVSCAT.removeAllElements();
			int L_intVSCNT=1;
			intRECCT = 0;
			setCursor(cl_dat.M_curWTSTS_pbst);
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Visitors Status Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				 
			}
			prnHEADER();
			int L_ROWNO = 0;
			int L_ROWNO2 = 0;
			String L_strTEMP="",L_strVSTCT1="",L_strSQLQRY="",L_strCATRY="",L_strVSICT;
			ResultSet L_rstRSSET;
			int L_intTEMP=0,L_intTEMP1=0;
			java.sql.Timestamp L_tmpTIME;
			java.sql.Date L_tmpDATE;
			L_strTTODAT=M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			L_strTFMDAT=M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			//main query
			M_strSQLQRY ="Select * from HR_VSTRN where";
			if(rdbVSTTP.isSelected())//visiter type wise report..
				M_strSQLQRY+= " VS_VSCAT= '"+txtVSTTP.getText()+"' and";
			if(chkCURNT.isSelected())//current report
				M_strSQLQRY+= " VS_VSTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' and isnull(VS_STSFL,' ') <> 'X' and VS_VSOTM IS NULL  ";
			if(!chkCURNT.isSelected())//for specified date range report..
				M_strSQLQRY+= " VS_VSTDT BETWEEN '"+L_strTFMDAT+"' and '"+L_strTTODAT+"'";
			if(txtVSTNM.getText().length() >0)
				M_strSQLQRY+= " and VS_VSTNM= '"+txtVSTNM.getText()+"'";
			if(txtTOVST.getText().length() >0)
				M_strSQLQRY+= " and VS_PERVS= '"+txtTOVST.getText()+"'";
			
			M_strSQLQRY+= " and VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,'')<> 'X' ORDER BY VS_VSCAT";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
//			System.out.println(" Query "+M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					//for date converting ..
					L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
					if (L_tmpTIME != null)
					{
						L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
						if(chkCURNT.isSelected())
							L_strTEMP = L_strTEMP.substring(11);
						L_strTEMP = padSTRING('R',L_strTEMP,16);
					}
					L_strVSTTP = nvlSTRVL(M_rstRSSET.getString("VS_VSTTP"),"-");
					L_strVSTNM = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),"-"),35);
					L_strVSORG = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),"-"),35);
					L_strVSICT = nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),"1");
					L_strVSTCT = nvlSTRVL(M_rstRSSET.getString("VS_VSTCT"),"1");
					L_strVSOCT = nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),"");
					L_strSHFCD = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_SHFCD"),"-"),5);
					L_strVEHNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_VEHNO"),"-"),15);
					L_strVSARA = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_VSARA"),"-"),4);
					L_strPURPS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_PURPS"),"-"),50);
					L_strVSPNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),"-"),16);
					L_strPERVS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),"-"),25);
					L_strCLRBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("VS_CLRBY"),"-"),5);
					L_strVSCAT = nvlSTRVL(M_rstRSSET.getString("VS_VSCAT"),"");					
					//to get vs categary description through categary code...					
					if(hstVSTCT.containsKey(L_strVSCAT))
						L_strVSCDS = hstVSTCT.get(L_strVSCAT).toString();
					else 
						L_strVSCDS = L_strVSCAT;
					//for one time printing categary typr with description...
					if(!L_strVSCAT.equals(L_strPRVCAT))
					{
						if(L_intVSCNT >=1)
						{
							dosREPORT.writeBytes(L_strVSCDS+" "+L_strVSCAT+"\n");
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 2;
						}
						L_strPRVCAT = L_strVSCAT;
						L_intVSCNT=1;
					}
					else
					{
						L_intVSCNT++;
					}
					
					dosREPORT.writeBytes(L_strVSTNM+"  "+L_strVSPNO+"  "+L_strPERVS+"  "+L_strSHFCD+"  "+L_strVEHNO+"  "+L_strVSARA+"  "+L_strCLRBY+"  "+padSTRING('L',L_strVSICT,3)+"\n");
					dosREPORT.writeBytes(L_strVSORG+"  "+L_strTEMP+"  "+L_strPURPS.substring(0,25)+"\n");
					dosREPORT.writeBytes(padSTRING('R',"",55)+L_strPURPS.substring(25));					
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=3;
					L_intTEMP = Integer.parseInt(L_strVSICT);
					L_intTEMP1 = L_intTEMP1+L_intTEMP;
									
					if(cl_dat.M_intLINNO_pbst> 63)
					{						
						dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------\n");						
						cl_dat.M_intLINNO_pbst ++;
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						{
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						prnHEADER();
					}
					intRECCT++;
				}
				M_rstRSSET.close();
				dosREPORT.writeBytes("total "+String.valueOf(L_intTEMP1));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst +=1;
			}
			L_strVSTCT1="";
			L_intTEMP=0;
			L_intTEMP1=0;
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------\n");						
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}
			fosREPORT.close();
			dosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
		}
	}
	/**
	 * Method to validate the Inputs before execuation of the SQL Queries.
	 */
	boolean vldDATA()
	{
		try
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;				
			if(rdbVSTTP.isSelected())
			{					
				if(txtVSTTP.getText().length() ==0)
				{
					setMSG("Please Enter the Visitor Type..",'E');
					return false;
				}
			}
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("From Date should not be Greater than current Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("To Date should not be Greater than current Date Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Greater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
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
	 * Method to generate the header part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,103));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			if(!chkCURNT.isSelected())
				dosREPORT.writeBytes(padSTRING('R'," Visitors status at SPL Site from "+txtFMDAT.getText().toString()+" to "+txtTODAT.getText().toString(),103));
			if(chkCURNT.isSelected())
				dosREPORT.writeBytes(padSTRING('R'," Visitors status at SPL Site as on "+ cl_dat.M_strLOGDT_pbst,103));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");			
			if(rdbVSTTP.isSelected())
			{
				dosREPORT.writeBytes("Visitor of "+L_strVSCDS+" Status \n");
				cl_dat.M_intLINNO_pbst+= 1;
			}
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------\n");						
			dosREPORT.writeBytes("Visitor Name                         Pass No           Person To Visit            Shift  Vehicle          Area  Clear  No of   \n");             					
			dosREPORT.writeBytes("Representing                         In Time           Purpose                                                  By     Persons \n");
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------\n");						
			cl_dat.M_intLINNO_pbst+= 6;					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}