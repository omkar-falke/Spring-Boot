/*
System Name    : System Administration
Program Name   : Software master
Program Desc.  : List Of Software Licences
Author         : Mrs.Dipti S Shinde
Date           : 3rd June 05
Version        : v2.0.0
Modificaitons  : 
Modified By    :
Modified Date  : 
Modified det.  :
Version        :
*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.util.StringTokenizer;

//----------------------------------------------------------------
/**<pre>
System Name : Material Management System.
 
Program Name : List of Software Licences wise Report       

Purpose : This program will generate a Report for Software Licences Details.

List of tables used :
Table Name      Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_SWMST        SW_SFTCD,SW_SRLNO                                      #
-------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
 txtDATA     SW_SFTNM         CO_SWMST    VARCHAR/40    Software name
             SW_LICTP         CO_SWMST    VARCHAR/10    Software Licence Type
             SW_LOCDS         CO_SWMST    VARCHAR/15    Software Location
      
-------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)CO_SWMST
                        
<B>Conditions Give in Query:</b>
   - Licences Type wise Query
   - Location wise Query
   - Software Name wise Query    
<B>Validations :</B>
    - if Full Report is selected then txtDATA option should be blank.
	- if other option is selected then txtDATA should have value. 
</I> */

/*-------------------------------------------------------------------*/
public class sa_rpswm extends cl_rbase
{  										
									     /*** String for generated Report File Name*/                   
	private String strFILNM;			 /** Integer for counting the records fetched from DB.*/	                     
	private int intRECCT=0;				 /** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	 /** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	 /**String for accept Isodoc1 information */                                     		
	private String strISODOC1;           /**String for accept Isodoc2 information. */
	private String strISODOC2;          /**String for accept Isodosc3 Information. */
	private String strISODOC3;          /**Combobox for select Type to generate Report value.*/
	private JComboBox cmbLOCDS;	        /**TextField for accepting data from F1.*/	
	private JTextField txtDATA;         /** Lable for Location Text.*/
	private JLabel lblLOCDS;            /** Lable for Specification Text */
	private JLabel lblSPECI;
	
	sa_rpswm()
	{
	    super(2);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(lblLOCDS = new JLabel("Report option"),4,3,1,1,this,'L');
			add(cmbLOCDS = new JComboBox(),4,4,1,2,this,'L');
			
			add(lblSPECI = new JLabel("Value"),6,3,1,1,this,'R');
			add(txtDATA = new JTextField(),6,4,1,2,this,'L');			
			
			cmbLOCDS.addItem("Full Report");
			cmbLOCDS.addItem("S/W code wise");
			cmbLOCDS.addItem("Lic Type wise");
			cmbLOCDS.addItem("Location wise");
								
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX+" GUI Designing",'E');
		}	
    }
     /**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		if(cmbLOCDS.getSelectedItem().equals("Full Report"))
		{
			txtDATA.setEnabled(false);
			txtDATA.setVisible(false);
			lblSPECI.setVisible(false);
		}
		else
		{
			txtDATA.setEnabled(true);
			txtDATA.setVisible(true);
			lblSPECI.setVisible(true);
			txtDATA.requestFocus();
		}
	}
	/** Method for action performing 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE); 		
		if(M_objSOURC == cmbLOCDS)
		{
			txtDATA.setText("");
			setENBL(true);
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			cmbLOCDS.requestFocus();
			setMSG("please select an option",'N');
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if((L_KE.getKeyCode() == L_KE.VK_F1)&&(M_objSOURC == txtDATA))
 	    {
			txtDATA.setText(txtDATA.getText().trim().toUpperCase());
			setCursor(cl_dat.M_curWTSTS_pbst);
    	    try
    	  	{					
				if(cmbLOCDS.getSelectedItem().equals("Location wise"))
				{
					M_strSQLQRY = " Select distinct SW_LOCDS from CO_SWMST WHERE ";					
					if(txtDATA.getText().trim().length()>0)
					M_strSQLQRY += " SW_LOCDS like '"+ txtDATA.getText().trim()+"%' AND "; 
					M_strSQLQRY += " IFNULL(SW_STSFL,'') <>'X' order by SW_LOCDS "; 			
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Location wise"},1,"CT");
				}				
				else if(cmbLOCDS.getSelectedItem().equals("S/W code wise"))
				{
					M_strSQLQRY =  " Select distinct SW_SFTCD,SW_LICTP from CO_SWMST WHERE ";				   
					if(txtDATA.getText().trim().length()>0)
						M_strSQLQRY += " SW_SFTCD like '"+ txtDATA.getText().trim()+"%' AND " ; 
					M_strSQLQRY += " IFNULL(SW_STSFL,'') <>'X' order by SW_SFTCD,SW_LICTP ";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Software code ","Licence Type"},2,"CT");
				}				
				else if(cmbLOCDS.getSelectedItem().equals("Lic Type wise"))
				{
					M_strSQLQRY =  " Select distinct SW_LICTP from CO_SWMST WHERE ";	
					if(txtDATA.getText().trim().length()>0)
						M_strSQLQRY += " SW_LICTP like '"+ txtDATA.getText().trim()+"%' AND "; 
					M_strSQLQRY += " IFNULL(SW_STSFL,'') <>'X' order by SW_LICTP ";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Licence Type"},1,"CT");
				}																																	    			
    			M_strHLPFLD = "txtDATA";			           						    		 	
				setCursor(cl_dat.M_curDFSTS_pbst);
    		}
    	    catch(Exception L_EX)
    	    {
    		    setMSG(L_EX ," F1 help..");    	
				setCursor(cl_dat.M_curDFSTS_pbst);
			}    	    
 	    }
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC==txtDATA)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			if(M_objSOURC ==cmbLOCDS)
			{
				txtDATA.requestFocus();
				setMSG("please enter the text or press F1 to select the text",'N');
			}
		}
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtDATA")
		{
			txtDATA.setText(cl_dat.M_strHLPSTR_pbst);							
			if (txtDATA.getText().length()<=8)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
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
					strFILNM = cl_dat.M_strREPSTR_pbst +"sa_rpswm.html";				
			   else if(M_rdbTEXT.isSelected())
				    strFILNM = cl_dat.M_strREPSTR_pbst + "sa_rpswm.doc";				
				cl_dat.M_PAGENO = 0;
				cl_dat.M_intLINNO_pbst = 0;	
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
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Software Licenece wise"," ");
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					}
				}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}					
	}	
	/** Method for Getting All Records from Table. */
    private void getDATA()
	{
		try
		{
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> List of Software Licences </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}						
			prnHEADER();
			String L_strSFTNM,L_strLICNO,L_strLICTP,L_strUSRNO,L_strLOCDS,L_strVERNO,L_strREMDS,L_strPRLCDS,L_strPRSWNM,L_strSFTCD,L_strPRSWCD;			
			int L_intSWCNT=1;
			//System.out.println("query");
			M_strSQLQRY = "select SW_SFTNM,SW_VERNO,SW_LICNO,SW_LICTP,SW_USRNO,SW_LOCDS,SW_SFTCD ,";
			M_strSQLQRY += " SW_REMDS from CO_SWMST where IFNULL(SW_STSFL,'') <>'X'  ";
			
			if(txtDATA.getText().trim().length()>0)
			{		
				if(cmbLOCDS.getSelectedItem().equals("Location wise"))
				{
					M_strSQLQRY += "  AND  SW_LOCDS ='"+txtDATA.getText().trim()+"'";
				}
				else if(cmbLOCDS.getSelectedItem().equals("S/W code wise"))	
				{	 
					M_strSQLQRY += "  AND  SW_SFTCD ='"+txtDATA.getText().trim()+"'";
				}					   					
				else if(cmbLOCDS.getSelectedItem().equals("Lic Type wise"))
				{
					M_strSQLQRY += "  AND  SW_LICTP ='"+txtDATA.getText().trim()+"'";
				}																									
			}
			M_strSQLQRY += "  order by SW_LOCDS ,SW_LICTP ,SW_SFTCD ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{	
				L_strLOCDS ="";
				L_strPRLCDS ="";
				L_strPRSWNM ="";
				L_strPRSWCD ="";
				while(M_rstRSSET.next())
				{
					L_strSFTNM = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SW_SFTNM"),""),40);
					L_strVERNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_VERNO"),""),10);
					L_strLICNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_LICNO"),""),40);
					L_strLICTP = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SW_LICTP"),""),10);
					L_strUSRNO = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SW_USRNO"),""),15);
					L_strLOCDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_LOCDS"),""),15);
					L_strREMDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_REMDS"),""),30);
					L_strSFTCD = nvlSTRVL(M_rstRSSET.getString("SW_SFTCD"),"");
					if(!L_strLOCDS.equals(L_strPRLCDS))
					{						
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes("\n");	
						cl_dat.M_intLINNO_pbst ++;
						dosREPORT.writeBytes(L_strLOCDS);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n");						
						cl_dat.M_intLINNO_pbst ++;
						L_strPRLCDS = L_strLOCDS;	
					}
					// counting the same location name and print a total of them at one time.
					if(!L_strSFTCD.equals(L_strPRSWCD))
					{
						if(L_intSWCNT >1)
						{
							if(M_rdbHTML.isSelected())// for html
								dosREPORT.writeBytes("<B>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes(padSTRING('R',"Total " + L_strPRSWNM ,35)+L_intSWCNT +"\n");
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst += 2;
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
						}
						L_strSFTNM = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_SFTNM"),""),35);
						L_strSFTCD = nvlSTRVL(M_rstRSSET.getString("SW_SFTCD"),"");
						L_strPRSWNM = L_strSFTNM;	
						L_strPRSWCD = L_strSFTCD;
						L_intSWCNT =1;
					}
					else
					{
						L_intSWCNT++;
						L_strSFTNM =padSTRING('R',"",35);
					}
					dosREPORT.writeBytes(L_strSFTNM+L_strLICNO+L_strLICTP+L_strUSRNO+"      "+L_strREMDS);
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst ++;
					System.out.println("linecount "+cl_dat.M_intLINNO_pbst);
					if(cl_dat.M_intLINNO_pbst> 63)
					{						
						dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------------------------------------");
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
				if(cmbLOCDS.getSelectedItem().toString().equals("S/W code wise"))
				{
					if(L_intSWCNT >1)
					{
						if(M_rdbHTML.isSelected())// for html
							dosREPORT.writeBytes("<B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes(padSTRING('R',"Total " + L_strPRSWNM,78)+L_intSWCNT +"\n");
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 2;
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
					}
				}
			}
			dosREPORT.writeBytes("\n----------------------------------------------------------------------------------------------------------------------------------");
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
			setCursor(cl_dat.M_curDFSTS_pbst);
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
			strISODOC1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","COXXLIC","DOC1");
			strISODOC2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","COXXLIC","DOC2");
			strISODOC3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","COXXLIC","DOC3");
				
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(!cmbLOCDS.getSelectedItem().equals("Full Report"))
			{					
				if(txtDATA.getText().length() ==0)
				{
					setMSG("Please Enter the Value with help(F1 key) ..",'E');
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
		
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
			dosREPORT.writeBytes("\n" + padSTRING('L',"----------------------------",130));
			dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC1,130));
			dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC2,130));
			dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC3,130));
			dosREPORT.writeBytes("\n" + padSTRING('L',"----------------------------",130) + "\n");			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,106));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"List of Software Licences ",106));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");			
			dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");						
			dosREPORT.writeBytes("Software Name                      Licence No.                                Lic.Type      No.Of users   Remark                  \n");					
			dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst = 11;					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}		
}