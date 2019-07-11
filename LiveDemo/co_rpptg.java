/*
System Name   : Common Master Records
Program Name  : Party type grouping
Program Desc. : Party Group code.
Author        : Mrs.Dipti.S.Shinde.
Date          : 18th Aug 2005
System        : Master maintenance
Version       : MMS v2.0.0
Modificaitons : 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/
/**
<b>Program Name :</b>  Grouping parties report

<b>Purpose :</b> This program will generate a Report for Group wise parties.
			
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
CO_PTMST        PT_PRTCD,PT_PRTTP                             #       
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		 Type/Size       Description
----------------------------------------------------------------------------------------------------------
 txtPRTTP    PT_PRTTP       CO_PTMST          varchar(1)     Party type
 txtGRPCD    PT_GRPCD       CO_PTMST          varchar(5)     Group code                         
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description     Display Columns       Table Name
----------------------------------------------------------------------------------------------------------
txtPRTTP        Party code             PT_PRTCD             CO_PTMST
txtGRPCD        Group code             PT_GRPCD             CO_PTMST             
----------------------------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
   - Party type wise report generate.
   - All report generate.
   - Specific Group wise report generate.  
<B>Validations :</B>
    - 
	-  
</I> */

import java.sql.ResultSet;
import javax.swing.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import java.util.Date; 
public class co_rpptg extends cl_rbase
{                                       /**Text field for Party type*/
	private JTextField txtPRTTP;        /**Text field for Group code*/
	private JTextField txtGRPCD;        /**RedioButton for All Report */
	private JRadioButton rdbGRPAL;      /**RedioButton for Specific Groupwise report */
	private JRadioButton rdbGRPSP;      /**Button group fro rdbGRPAL and rdbGRPSP */
	private ButtonGroup bgrGRPCD;       /** String for generated Report File Name*/
	private String strFILNM;            /** Counter for record counting */ 
	private int intRECCT=0;				/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	/** flag to identify group record printing */
	boolean flgGRPREC = false;         

	co_rpptg()
	{
	    super(1);
	    try
	    {	
			setCursor(cl_dat.M_curWTSTS_pbst);
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			bgrGRPCD = new ButtonGroup();
			add(new JLabel("Party Type: "),2,2,1,1,this,'R');
			add(txtPRTTP = new JTextField(),2,3,1,1,this,'L');
			add(new JLabel("Group Code: "),3,2,1,1,this,'R');
			add(rdbGRPAL= new JRadioButton ("ALL",true),3,3,1,1,this,'L');
			add(rdbGRPSP= new JRadioButton ("SPECIFIC",false),3,4,1,1,this,'L');
			add(txtGRPCD = new JTextField(),3,5,1,1,this,'L');
			bgrGRPCD.add(rdbGRPAL);
			bgrGRPCD.add(rdbGRPSP);
			
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{  
		super.setENBL(L_flgSTAT);	
		txtGRPCD.setEnabled(false);
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
				setMSG("Select an Option..",'E');
				clrCOMP();
				cl_dat.M_cmbOPTN_pbst.requestFocus();
			}	
			else
			{				
				if((txtPRTTP.getText().trim()).length()==0)						
					clrCOMP();
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				setENBL(true);
				txtPRTTP.requestFocus();
				setMSG("please select the option from table and for location F1 press",'N');
			}
		}
		if(M_objSOURC==txtPRTTP)
		{
			rdbGRPAL.requestFocus();
		}
		if(M_objSOURC==rdbGRPAL)
		{
			txtGRPCD.setEnabled(false);
			txtGRPCD.setText("");
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		if(rdbGRPSP.isSelected())
		{
			txtGRPCD.setEnabled(true);
		}
		if(M_objSOURC==rdbGRPSP)
		{
			txtGRPCD.requestFocus();
		}
		if(M_objSOURC==txtGRPCD)
		{
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
		if((L_KE.getKeyCode() == L_KE.VK_F1))
		{
			txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
			this.setCursor(cl_dat.M_curWTSTS_pbst);
    	    				
				if(M_objSOURC == txtGRPCD)
				{
					txtGRPCD.setText(txtGRPCD.getText().trim().toUpperCase());
					M_strHLPFLD = "txtGRPCD";
					
					M_strSQLQRY=" select PT_GRPCD,PT_PRTNM from CO_PTMST where PT_GRPCD <>'XXXXX' AND PT_PRTTP= '"+txtPRTTP.getText().trim()+"' " ;
					if(txtGRPCD.getText().length() > 0)
						M_strSQLQRY += " and PT_GRPCD like '"+txtGRPCD.getText().trim()+"%'";
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Group code","Name"},2,"CT");
				}	
				if(M_objSOURC == txtPRTTP)
				{
					txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
					M_strHLPFLD = "txtPRTTP";
					M_strSQLQRY=" SELECT  cmt_codcd,cmt_codds from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPRT' and cmt_codcd='C'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Party type","Description"},2,"CT");
					System.out.println("Party  "+M_strSQLQRY);
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtGRPCD")
		{
			txtGRPCD.setText(cl_dat.M_strHLPSTR_pbst);	
			txtGRPCD.setEnabled(true);
			txtGRPCD.requestFocus();
		}
		if(M_strHLPFLD == "txtPRTTP")
		{
			txtPRTTP.setText(cl_dat.M_strHLPSTR_pbst);							
			
		}
	}
	void exePRINT()
	{
		//if (!vldDATA())
			//return;		
		try
		   {
			   if(M_rdbHTML.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + "co_rpptg.html";				
			   else if(M_rdbTEXT.isSelected())
				    strFILNM = cl_dat.M_strREPSTR_pbst + "co_rpptg.doc";				
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;	
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"group wise parties printing"," ");
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Grouped parties </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
			prnHEADER();
			intRECCT = 0;
			
			String L_strGRPCD,L_strPRTCD,L_strPRTNM,L_strADR01,L_strADR02,L_strADR03,L_strADR04;			
			
			M_strSQLQRY=" select distinct PT_GRPCD,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_ZONCD,PT_PRTNM from CO_PTMST where PT_PRTTP= '"+txtPRTTP.getText().trim()+"'";
			if(rdbGRPAL.isSelected())
			{
				M_strSQLQRY+=" and PT_GRPCD <>'XXXXX' ";
			}
			else if(rdbGRPSP.isSelected())
			{
				M_strSQLQRY += " AND PT_GRPCD ='"+txtGRPCD.getText().trim()+"'";
			}
			M_strSQLQRY+= " AND isnull(PT_STSFL,' ') <> 'X' order by PT_GRPCD , PT_PRTCD ";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println("getdata "+M_strSQLQRY);
			String L_strGRPCD1 = "zzzzz";
			if(M_rstRSSET ==null || !M_rstRSSET.next())
				return;
			L_strPRTCD = "";
			L_strPRTNM = "";
			L_strADR01 = "";
			L_strADR02 = "";
			L_strADR03 = "";
			L_strADR03 = "";
						
			while(true)
			{
				L_strGRPCD = nvlSTRVL(M_rstRSSET.getString("PT_GRPCD"),"");
				if(!L_strGRPCD.equals(L_strGRPCD1))
					{L_strGRPCD1 = L_strGRPCD; exePRTGRP(L_strGRPCD);}
				L_strPRTCD = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
				L_strPRTNM = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),42);
				L_strADR01 = nvlSTRVL(M_rstRSSET.getString("PT_ADR01")," ").trim();
				L_strADR02 = nvlSTRVL(M_rstRSSET.getString("PT_ADR02")," ").trim();
				L_strADR03 = nvlSTRVL(M_rstRSSET.getString("PT_ADR03")," ").trim();
				L_strADR04 = nvlSTRVL(M_rstRSSET.getString("PT_ADR04")," ").trim();
				String L_strADDSTR = L_strADR01+L_strADR02+L_strADR03+L_strADR04;
				if(L_strGRPCD.equals(L_strPRTCD))
					if(M_rstRSSET.next()) continue; else break; //if Group code equals to party then not to show.. 
				exeRECPRT(L_strGRPCD,L_strPRTCD,L_strPRTNM,L_strADDSTR);
				if(!M_rstRSSET.next())
					break;
				
				if(cl_dat.M_intLINNO_pbst> 63)
				{						
					dosREPORT.writeBytes("\n--------------------------------------------------------------------------------------------------------");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					{
						dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------");			
						prnFMTCHR(dosREPORT,M_strEJT);
					}
					prnHEADER();
				}
			}
			dosREPORT.writeBytes("\n-------------------------------------------------------------------------------------------------------------");
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
 	/** Method for printing party record */
   private void exeRECPRT(String LP_GRPCD,String LP_PRTCD,String LP_PRTNM,String LP_ADDSTR)
	{
		try
		{
				if(flgGRPREC)
					{dosREPORT.writeBytes("\n\n");	cl_dat.M_intLINNO_pbst +=2;}
				int L_intGRPFIL1 = (flgGRPREC ? 0 : 7);//for party pading
				int L_intGRPFIL2 = (flgGRPREC ? 7 : 0);//for group pading
				dosREPORT.writeBytes("\n");	
				cl_dat.M_intLINNO_pbst +=1;
				dosREPORT.writeBytes(padSTRING('R',"",L_intGRPFIL1));
				dosREPORT.writeBytes(padSTRING('R',LP_PRTCD,7));
				dosREPORT.writeBytes(padSTRING('R',LP_PRTNM,42));
				dosREPORT.writeBytes(padSTRING('R',"",L_intGRPFIL2));
				int L_ADDRLEN = LP_ADDSTR.length();
				if (L_ADDRLEN <= 45)
					{
					  dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR,45)+"\n\n");
					  cl_dat.M_intLINNO_pbst +=2; return;
					}
				if (L_ADDRLEN <= 45+45)
					{
					  dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(0,45),45)+"\n");
					  dosREPORT.writeBytes(padSTRING('R'," ",7+7+42)); dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(45,L_ADDRLEN),45)+"\n\n");
					  cl_dat.M_intLINNO_pbst +=3; return;
					}
				if (L_ADDRLEN <= 45+45+45)
					{
					  dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(0,45),45)+"\n");
					  dosREPORT.writeBytes(padSTRING('R'," ",7+7+42)); dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(45,45+45),45)+"\n");
					  dosREPORT.writeBytes(padSTRING('R'," ",7+7+42)); dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(45+45,L_ADDRLEN)+"\n\n",45));
					  cl_dat.M_intLINNO_pbst +=4; return;
					}
				if (L_ADDRLEN <= 45+45+45+45)
					{
					  dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(0,45),45)+"\n");
					  dosREPORT.writeBytes(padSTRING('R'," ",7+7+42)); dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(45,45+45),45)+"\n");
					  dosREPORT.writeBytes(padSTRING('R'," ",7+7+42)); dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(45+45,45+45+45),45)+"\n");
					  dosREPORT.writeBytes(padSTRING('R'," ",7+7+42)); dosREPORT.writeBytes(padSTRING('R',LP_ADDSTR.substring(45+45+45,L_ADDRLEN),45)+"\n\n");
					  cl_dat.M_intLINNO_pbst +=5; return;
					}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeRECPRT");
		}					
   }
   
/** Method for group record printing
 */
   private void exePRTGRP(String LP_GRPCD)
   {
	try
	{
		M_strSQLQRY=" select PT_GRPCD,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_ZONCD,PT_PRTNM from CO_PTMST where PT_PRTTP= '"+txtPRTTP.getText().trim()+"' and pt_prtcd = '"+LP_GRPCD+"' and pt_grpcd = '"+LP_GRPCD+"'";
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		System.out.println("getdata "+M_strSQLQRY);
		
		if(!L_rstRSSET.next())
			return;
		String L1_strPRTNM = padSTRING('R',nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""),42);
		String L1_strADR01 = nvlSTRVL(M_rstRSSET.getString("PT_ADR01")," ").trim();
		String L1_strADR02 = nvlSTRVL(M_rstRSSET.getString("PT_ADR02")," ").trim();
		String L1_strADR03 = nvlSTRVL(M_rstRSSET.getString("PT_ADR03")," ").trim();
		String L1_strADR04 = nvlSTRVL(M_rstRSSET.getString("PT_ADR04")," ").trim();
		String L1_strADDSTR = L1_strADR01+L1_strADR02+L1_strADR03+L1_strADR04;
		flgGRPREC = true;
		exeRECPRT(LP_GRPCD,LP_GRPCD,L1_strPRTNM,L1_strADDSTR);
		flgGRPREC = false;
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"exePRTGRP");
	}
}
    /**
	Method to validate DATA before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return false;
	}
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,75));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");	
			if(rdbGRPAL.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Party grouping report ",75));
			else if(rdbGRPSP.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Group '"+txtGRPCD.getText().trim()+"'  " ,75));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");			
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------\n");						
			dosREPORT.writeBytes("Group  Party  Party Name                                Address                                         \n");             					
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst+= 5;					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}

}//main------------------