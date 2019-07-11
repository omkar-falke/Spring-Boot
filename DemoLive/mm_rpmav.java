/*
System Name   : Material Management System
Program Name  : Material Groupwise Approved Vendor List 
Program Desc. : Material Groupwise Approved Vendor List 

Author        : Mr S.R.Tawde
Date          : 19.09.2006
Version       : MARKSYS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;import javax.swing.JPanel;
import java.awt.Font;import java.awt.Color;import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;import javax.swing.InputVerifier;import javax.swing.JComponent;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
/**<pre>
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Material Groupwise Approved Vendor List

<b>Purpose :</b> This module gives Material Groupwise Approved Vendor List for given Material Groups.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
CO_CTMST
CO_PTMST
MM_AVMST
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name         Table Name         Type/Size    Description
--------------------------------------------------------------------------------
rdbWORM		-------------Select for Report without Raw Material-----------------
rdbWRM	    -------------Select for Report with Raw Material--------------------
rdbORM	    --------------Select for Report with Raw Material-------------------
--------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description			      Display Columns		  	 Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------

-----------------------------------------------------------------------------------------------------------------------------------------------------
<I>

<B>Conditions Given in Query:</b>
             Data is taken from MM_AVMST,CO_PTMST,CO_CTMST

             1)AV_GRPCD=CT_GRPCD
             2)CT_CODTP = 'MG'
             3)PT_PRTCD=AV_PRTCD
</I>
Validations :
//'ISOFGXXRPT','MSTCOXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL			 
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
    3) Despatch Type validation from CO_CDTRN.('MST','MRXXDTP')
    4) Transporter's Code from CO_PTMST('PT_PRTTP = 'T' AND 'PT_PRTCD= INPUT CODE')
 */
public class mm_rpmav extends cl_rbase
{ 							/** JPanel to Display Material Groups*/	
	private JPanel pnlRPTTP ;/** JRadioGroup for Material Group*/
	private ButtonGroup bgrRPTTP ;/** JRadiobutton for selection of Material Groups*/
	private JRadioButton rdbWORM,rdbWRM,rdbORM;

										/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;/** Report Header String*/	
	private String strRPHDR,strTRNDS;
	
	/**
	 *1.Screen Designing
	 */
	mm_rpmav()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			bgrRPTTP=new ButtonGroup();			
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);

			pnlRPTTP = new JPanel();
			setMatrix(20,8);
			pnlRPTTP.setLayout(null);
			bgrRPTTP=new ButtonGroup();
			add(rdbWORM = new JRadioButton("Without Raw Material",true),1,2,1,2,pnlRPTTP,'L');
			add(rdbWRM = new JRadioButton("With Raw Material",false),1,4,1,2,pnlRPTTP,'L');
			add(rdbORM = new JRadioButton("Only Raw Material",false),1,6,1,2,pnlRPTTP,'L');
			
			bgrRPTTP.add(rdbWORM);
			bgrRPTTP.add(rdbWRM);
			bgrRPTTP.add(rdbORM);
			pnlRPTTP.setBorder(BorderFactory.createTitledBorder(null,"Material Groups",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlRPTTP,6,2,2,7,this,'L');
			
			M_pnlRPFMT.setVisible(true);			
			setENBL(false);									
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
	}			
    /**
     *Super class method overide to enhance its funcationality 
     *We can use this for Enabling or Disabling other components on the screen as per our
     *requirement based on option selected ie. ADDITION,MODIFICATION,DELETION etc.
     * @param L_flgSTAT boolean argument to pass state of the component.
	*/
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
	}		

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
			
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
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
	}
		public void exeHLPOK()
		{
		super.exeHLPOK();
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpmav.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpmav.doc";	
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
	*Method to fetch Data from Database and start creation of the output file for Detail Report.
	*/
	private void getDATA()
	{ 	    		
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		String L_strGRP = "";
		
		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPGRPCD = new String();
		String L_strPGRPDS = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCGRPCD = new String();
		String L_strCGRPDS = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strGRPCD = new String();
		String L_strGRPDS = new String();
		String L_strPRTCD = new String();
		String L_strPRTNM = new String();
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
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Statement </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();
			setMSG("Report Generation is in Progress.......",'N');
			
			if(rdbWORM.isSelected())
				L_strGRP = " and AV_GRPCD != '68' ";
			if(rdbWRM.isSelected())
				L_strGRP = "";
			if(rdbORM.isSelected())
				L_strGRP = " and AV_GRPCD = '68' ";
			
			M_strSQLQRY = " SELECT AV_GRPCD,CT_MATDS,AV_PRTCD,PT_PRTNM FROM CO_CTMST,MM_AVMST,CO_PTMST " ;
			M_strSQLQRY+= " WHERE AV_GRPCD=CT_GRPCD AND av_grpcd not in ('47','49','60','62','85','86','95','98','99') and CT_CODTP='MG' and PT_PRTTP='S' AND PT_PRTCD=AV_PRTCD AND  (isnull(AV_STSFL,'') <>'X' and isnull(PT_STSFL,'') <>'X' and isnull(CT_STSFL,'') <>'X') " ;
			M_strSQLQRY+= L_strGRP;
			M_strSQLQRY+= " ORDER BY AV_GRPCD,PT_PRTNM " ;

			System.out.println(M_strSQLQRY);			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					
					L_strGRPCD = nvlSTRVL(M_rstRSSET.getString("AV_GRPCD"),"");
					L_strGRPDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
							dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
								dosREPORT.writeBytes(padSTRING('R',L_strGRPCD,5));
								dosREPORT.writeBytes(padSTRING('R',L_strGRPDS,50));
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
								cl_dat.M_intLINNO_pbst+= 3;
					}
					if(L_flg1STSFL){
						L_strPGRPCD = L_strGRPCD;
						L_strCGRPCD = L_strGRPCD;
						L_flg1STSFL = false;
					}

			while(!L_flgEOF)		
			{
				L_strPGRPCD = L_strGRPCD;
				L_strPGRPDS = L_strGRPDS;
				
				dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',L_strGRPCD,5));
				dosREPORT.writeBytes(padSTRING('R',L_strGRPDS,50));
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
				cl_dat.M_intLINNO_pbst+= 3;
				while(L_strGRPCD.equals(L_strPGRPCD) && !L_flgEOF)
				{
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
						prnHEADER();
						dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes(padSTRING('R',L_strGRPCD,5));
							dosREPORT.writeBytes(padSTRING('R',L_strGRPDS,50));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
							cl_dat.M_intLINNO_pbst+= 3;
					}
					
					intRECCT++;
					L_strPRTCD = nvlSTRVL(M_rstRSSET.getString("AV_PRTCD"),"");
					L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
					dosREPORT.writeBytes(padSTRING('R',L_strPRTCD,8));
					dosREPORT.writeBytes(padSTRING('R',L_strPRTNM,50));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst+= 1;

					if(!M_rstRSSET.next())
					{
						L_flgEOF = true;
						break;
					}
					L_strGRPCD = nvlSTRVL(M_rstRSSET.getString("AV_GRPCD"),"");
					L_strGRPDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
				} //while((L_strGRPCD).equals(L_strPGRPCD) && !L_EOF)	
			}//while(eof())
				dosREPORT.writeBytes("--------------------------------------------------------------------------------\n");		
		}		prnFOOTR();
									
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
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+= 15;
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
}
