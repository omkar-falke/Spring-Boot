/*
System Name   : Material Management System
Program Name  : Tanker unloading Note
Program Desc. : User selects the Gate-In No. & Gate-In Type
Author        : Mrs.Dipti.S.Shinde.
Date          : 23rd Sep 2005
System        : MMS
Version       : MMS v2.0.0
Modificaitons : Query and report printing 
Modified By   : 
Modified Date : 
Modified det. : 
Version       : MMS v2.0.0
*/
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import java.util.Date; 

/** <pre>
<b>Program Name :</b> Tanker Unloading Note printing.

<b>Purpose :</b> Tanker unloading Note is printed for Tankers only.This is printed
at the time of Gross weighment at weighbridge and sent to Tankfarm operator along
with the tanker.Unloading clearance details by control room are printed on this note.
Unloading details are written by tankfarm op. on this note and are updated in 
system at the time of tare weighment. 
			
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
MM_WBTRN       WB_DOCTP,WB_MATCD                              #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		  Table name		    Type/Size       Description
----------------------------------------------------------------------------------------------------------
 txtDOCNO    WB_DOCNO          MM_WBTRN              varchar(8)     Gate-In Number
 cmbDOCTP    CO_CODCD,WB_DOCTP CO_CDTRN              varchar(2)     Gate-In Type                 
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description           Display Columns       Table Name
----------------------------------------------------------------------------------------------------------
txtDOCNO    Doc. number (Gate-In No.)    WB_DOCNO               MM_WBTRN
----------------------------------------------------------------------------------------------------------

Validations : Gate in number should not be blank and should be valid.
*/
public class mm_rptun extends cl_rbase 
{                                        /** String for file name */
	private String strFILNM;             /** File output Stream for generating file.*/
	private FileOutputStream fosREPORT;	 /** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	 /** Integer for record counting */                                     		
	private int intRECCT=0;	             /** TextField for gate entry number */
	private JTextField txtDOCNO;         /** comboBox for entry type */
	private JComboBox cmbDOCTP;	 
	private String strDOCNO ="";        
	mm_rptun()
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
			add(new JLabel("Gate in type"),2,2,1,1,this,'L');
			add(cmbDOCTP = new JComboBox(),2,3,1,1.5,this,'L');
					
			add(new JLabel("Gate in number"),4,2,1,1,this,'R');
			add(txtDOCNO = new JTextField(),4,3,1,1.5,this,'L');	
			
			cmbDOCTP.addItem("01 Tanker");
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}
		
	}
	mm_rptun(String P_strDOCNO)
	{
	    strDOCNO = P_strDOCNO;
        M_rdbTEXT.setSelected(true);
	}
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{  
		super.setENBL(L_flgSTAT);	
	}
	/** Method for action performing 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE); 	
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			{
				setMSG("Select an Option..",'N');
				cl_dat.M_cmbOPTN_pbst.requestFocus();
			}			
			else
			{				
				setENBL(true);
				cmbDOCTP.requestFocus();
				setMSG("select from List..",'N');
			}
		}
		if(M_objSOURC==cmbDOCTP)
		{
			txtDOCNO.setText("");
			txtDOCNO.requestFocus();
		}
		if(M_objSOURC==txtDOCNO)
		{
			setMSG("Press F1 to get data",'N');
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
			String L_strGATTP =cmbDOCTP.getSelectedItem().toString().substring(0,2);
			setCursor(cl_dat.M_curWTSTS_pbst);
    	    try
    	  	{				
				if(M_objSOURC== txtDOCNO)
				{
					M_strHLPFLD = "txtDOCNO";
					M_strSQLQRY = "SELECT DISTINCT WB_DOCNO,WB_LRYNO from MM_WBTRN "+
				      "where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP ='"+L_strGATTP+"'";
					//M_strSQLQRY +=" and WB_STSFL NOT IN('9','X') ";
                   // M_strSQLQRY +=" and ifnull(WB_QLLRF,'')<>'' ";

					if(txtDOCNO.getText().length() >0)
					{
						M_strSQLQRY += "and WB_DOCNO like '"+ txtDOCNO.getText().trim()+"%'"; 
					}
                   // System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"gate in number","Lorry number"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			catch(Exception L_EX)
			{   
				setMSG(L_EX,"f1 key event");
			}
		}
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtDOCNO")
         {
			txtDOCNO.setText(cl_dat.M_strHLPSTR_pbst);	
			txtDOCNO.requestFocus();
		 }
	}
	void exePRINT()
	{
		if(!vldDATA())
			return;		
		try
		   {
			strDOCNO = txtDOCNO.getText().trim();
			getDATA();
			//if(intRECCT ==0)
			//{
			//	setMSG("Data not found ..",'E');
			//	return;
			//}
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Tanker Unloading Note"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}					
	}
	 void getDATA()
	{
		try
		{
		    if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rptun.html";				
		   else 
			    strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rptun.doc";				
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;	

			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			setMSG("Report Generation in Process.......",'N');
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Tanker Routing Slip </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
			String L_strDOCNO="",L_strTNKNO="",L_strLRYNO="",L_strMATDS="",L_strWINBY="",L_strUCLBY ="",L_strULDBY="";
			M_strSQLQRY ="Select WB_TNKNO,WB_WINBY,WB_DOCNO,WB_LRYNO,WB_MATDS,WB_UCLBY,WB_ULDBY,WB_CHLQT,RM_REMDS from MM_WBTRN LEFT OUTER JOIN MM_RMMST ON ";
			 M_strSQLQRY += " RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_REMTP ='ULC' AND RM_CMPCD = WB_CMPCD AND RM_DOCNO = WB_DOCNO AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(RM_STSFL,'') <>'X' where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCNO ='"+strDOCNO+"' and WB_DOCTP='01' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strDOCNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO")," ");
					L_strTNKNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_TNKNO")," "),10);
					L_strLRYNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_LRYNO")," "),15);
					L_strMATDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_MATDS")," "),45);
					L_strWINBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_WINBY")," "),3);
					L_strUCLBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_UCLBY")," "),3);
					L_strULDBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_ULDBY")," "),3);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
				
					dosREPORT.writeBytes(padSTRING('L'," Tanker Unloading Note",55)+"\n");

					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD); 
	
					dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,71));
					dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");	
					dosREPORT.writeBytes(" Security NO : ");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					
					dosREPORT.writeBytes(padSTRING('R',L_strDOCNO,60));
					dosREPORT.writeBytes("Challan Qty : "+setNumberFormat(M_rstRSSET.getDouble("WB_CHLQT"),3)+"\n");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD); 
	
				//	dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");	
					dosREPORT.writeBytes(" Please Unload Tanker No : ");
					
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					
					dosREPORT.writeBytes(L_strLRYNO.trim() );
					
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD); 
	
					dosREPORT.writeBytes("  Containing Material : ");
					
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
						
					dosREPORT.writeBytes(L_strMATDS.trim()+" \n");
					
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD); 
	
					dosREPORT.writeBytes(" in Tank No  : ");
					
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					
					dosREPORT.writeBytes(L_strTNKNO.trim()); 
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD); 
	
					dosREPORT.writeBytes("  after following Checks.                               \n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");	
					String L_strPRDRM ="";
    				M_strSQLQRY ="select * "; 
    				M_strSQLQRY +="from MM_RMMST";
    			    M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_REMTP ='ULC' AND RM_DOCNO = '"+L_strDOCNO+"'";
                    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
    				if(M_rstRSSET !=null)
    				{ 
    					if(M_rstRSSET.next())
    					   L_strPRDRM = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
    				}
    				dosREPORT.writeBytes(" Control Room Instructions : "+padSTRING('R',L_strPRDRM,66)+"\n");	
    				if(L_strPRDRM.length() >66)
    				    dosREPORT.writeBytes(" "+L_strPRDRM.substring(66)+"\n");	
				    dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");	
				
					dosREPORT.writeBytes(" 1. Tanker IN                                          : Date :            Time :              \n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(" 2. Bottom Sample Checked for Smell,Colour and Water   : Yes / No                              \n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(" 3. Line Out Checked to Tank                           : Yes / No                              \n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(" 4. Earthing Provided To Tanker                        : Yes / No                              \n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(" 5. Unloading Started AT                               : Date :            Time :              \n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(" 6. Unloading Completed AT                             : Date :            Time :              \n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(" 7. Tanker Checked From Top & Is Empty                 : Yes / No                              \n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(" 8. Tanker Sent Out                                    : Date :            Time :              \n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");	
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("   ( "+L_strWINBY+" )"+"                          (  "+L_strUCLBY +"  ) "+"                                  ( "+L_strULDBY +" )  \n");	
				//	dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("  Weigh Bridge                    Control Room                                  Tank farm    \n");	
					setMSG("Report completed.. ",'N');
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
					{
						prnFMTCHR(dosREPORT,M_strCPI10);
						prnFMTCHR(dosREPORT,M_strEJT);				
					}			
					if(M_rdbHTML.isSelected())
					{
					    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
					}
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	Method to validate DATA before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{
			if(txtDOCNO.getText().trim().length()== 0)
			{
				setMSG("please enter the number",'E');
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			
		}
		return true;
	}
}