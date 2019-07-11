/*
System Name   : Common Master records
Program Name  : Customer Enrolment Form  
Program Desc. : Report to generate the hard copy document for various party details.
Author        : Mr S.R.Mehesare
Date          : 26.04.2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.sql.*;
import java.awt.*;
import java.util.Hashtable;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JList;
import javax.swing.JButton;
/**<pre>
System Name : Common Master Records
 
Program Name : Customer Enrollment Form.

Purpose : To generate the hard copy document for details of various parties related to SPL.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtPRTTP    PT_PRTTP       CO_PTMST      VARCHAR(1)		Party Type
txtPRTCD    PT_PRTCD       CO_PTMST      VARCHAR(5)     Party Code 
txtPRTNM    PT_PRTNM       CO_PTMST      VARCHAR(40)    Party Name
--------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
      For Party Type
          Data is taken from CO_CDTRN for CMT_CGSTP ='MSTCOXXPRT'.
      For Party Zone
          Data is taken from CO_CDTRN for CMT_CGSTP ='SYSMRXXZON'. 
      For Party Code
          Data is taken from table CO_PTMST for given condition
             1) PT_PRTTP = Given Party Type.
             2) AND PT_PRTCD = given Part code.
      Report Data is Taken from CO_PTMST for given condition as
			 1) PT_PRTTP = Given Party Type.
             2) AND PT_PRTCD = given Part code.
<I>
<B>Validations :</B>
    - Party Type must be valid, i.e. must be present in the DataBase.
    - Party Code must be valid, i.e. must be present in the DataBase.
</I> */


public class co_rppte extends cl_rbase
{  									/** JTextField to accept & display Party Type.*/
	private JTextField txtPRTTP;	/** JTextField to display Party Code.*/
	private JTextField txtPRTCD;	/** JTextField to display Party Name.*/
	private JTextField txtPRTNM;	/** JList to display Selected Part Codes.*/
	//private JList lstFPTCD;
	//private JList lstTPTCD;
	//private JButton btnMVRTA;	
	//private JButton btnMVRT1;
//	private JButton btnMVLTA;
//	private JButton btnMVLT1;
	
										/** String for generated Report File Name*/	                     
	private String strFILNM;			/** Integer for counting the records fetched from DB.*/	                     
	private int intRECCT=0;				/** String variable for Party Type Description.*/
	private String strPRTDS;			/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	
	private ResultSet M_rstRSSET1;		/** Hash Table to hold Zone Code, Description & Party Code, Description.*/	
	private Hashtable<String,String> hstCODDS;			/** Filter flag to be activated when transporter enrolment is selected */
	private boolean flgTRPFLT = false;
	co_rppte()
	{
	    super(1);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Party Type"),4,4,1,.8,this,'L');
			add(txtPRTTP= new TxtLimit(1),4,5,1,.5,this,'L');	                                                               		
			add(new JLabel("Party Code"),5,4,1,.8,this,'L');
			add(txtPRTCD= new TxtLimit(5),5,5,1,1,this,'L');
			add(new JLabel("Party Name"),6,4,1,.8,this,'L');
			add(txtPRTNM= new JTextField(40),6,5,1,2.5,this,'L');
			
			/*add(lstFPTCD= new JList(),4,3,4,1.5,this,'R');
			add(btnMVRT1= new JButton(">"),4,4,1,.7,this,'L');
			add(btnMVRTA= new JButton(">>"),5,4,1,.7,this,'L');
			add(btnMVLT1= new JButton("<"),6,4,1,.7,this,'L');
			add(btnMVLTA= new JButton("<<"),7,4,1,.7,this,'L');
			add(lstTPTCD= new JList(),4,7,3,2,this,'L');*/
			
			hstCODDS = new Hashtable<String,String>();
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ('SYSMR00ZON','MSTCOXXPRT','SYSCOXXPCT','SYSCOXXDST')";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)    			        		
			{
				while(M_rstRSSET.next())
				{
					hstCODDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));					
				}				
				M_rstRSSET.close();
			}			
			M_pnlRPFMT.setVisible(true);							 		
			txtPRTCD.setEnabled(false);
			txtPRTTP.setEnabled(false);
			txtPRTNM.setEnabled(false);
			txtPRTTP.requestFocus();
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
    }

	/**
	 * Printing blank string, if not applicable
	 */
	private void exeBLKPRT(String LP_STRVL)
	{
		try
		{
			if(flgTRPFLT==true)
				dosREPORT.writeBytes(padSTRING('R',"  ",LP_STRVL.length()));
			else
				dosREPORT.writeBytes(LP_STRVL);
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX," exeBLKRPT");
		}	
	}	


	
	/**
	 * Skipping printing part if not applicable
	 * */
	void exeSKIPPRT(String LP_STRVL)
	{
		try
		{
			if(flgTRPFLT==true)
				return;
			dosREPORT.writeBytes(LP_STRVL);			
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX," exeSKIPPRT");
		}	
	}	
	
	
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);				
		txtPRTTP.setEnabled(L_flgSTAT);
		txtPRTCD.setEnabled(L_flgSTAT);						
		txtPRTNM.setEnabled(false);
	}	
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
				if(((txtPRTTP.getText().trim()).length()==0)||((txtPRTCD.getText().trim()).length()!=0)||((txtPRTNM.getText().trim()).length()!=0))				
				{
					txtPRTTP.setText("");
					txtPRTCD.setText("");
					txtPRTNM.setText("");
				}
				setENBL(true);
				txtPRTTP.requestFocus();
				setMSG("Enter Party Type & press Enter Key OR Press F1 to select from List..",'N');
			}
		}
		if(M_objSOURC == txtPRTTP)
		{
			txtPRTCD.setText("");
			txtPRTNM.setText("");
			if((txtPRTTP.getText().trim()).length()==0)
			{
				setMSG("Please Enter Party Type & press Enter Key OR Press F1 to Select from List..",'N');
				return;
			}				
			if(hstCODDS.containsKey(txtPRTTP.getText().trim()))
			{
				strPRTDS= hstCODDS.get(txtPRTTP.getText().trim()).toString();				
				txtPRTCD.requestFocus();
				setMSG("Insert Party code & Press enter key, OR press F1 to select from list..",'N');
			}
			else
				setMSG("Invalid Party Code, press F1 to select from list..",'E');
		}
	    if(M_objSOURC == txtPRTCD)
		{	
			txtPRTCD.setText(txtPRTCD.getText().toUpperCase());
			setCursor(cl_dat.M_curWTSTS_pbst);
			if((txtPRTTP.getText().trim()).length()==0)
			{
				setMSG("Please Enter Party Type..",'E');
				return;
			}
			if((txtPRTCD.getText().trim()).length()!=5)
			{	
				txtPRTCD.setText("");
				txtPRTNM.setText("");
				setMSG("Enter party code And Press Enter Key. OR Press F1 To Select From List..",'N');
				return;
			}
			try
			{
				M_strSQLQRY = " Select PT_PRTCD,PT_PRTNM from CO_PTMST";
				M_strSQLQRY += " where PT_PRTTP = '" + txtPRTTP.getText().trim()+ "'"; 											
				M_strSQLQRY += " AND PT_PRTCD ='" + txtPRTCD.getText().trim() +"'";					
				M_strSQLQRY += " AND isnull(PT_STSFL,'') <> 'X' order by PT_PRTCD"; 					
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);									
				if(M_rstRSSET != null)    			        		
				{
					if (M_rstRSSET.next())
					{
						txtPRTCD.setText(M_rstRSSET.getString("PT_PRTCD"));
						txtPRTNM.setText(M_rstRSSET.getString("PT_PRTNM"));
					}
					else
					{
						setMSG("Invalid Party Code..",'E');	
						txtPRTCD.requestFocus();
						return;
					}
					M_rstRSSET.close();
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}								
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Consignment No..");					
			}	
		}						
		setCursor(cl_dat.M_curDFSTS_pbst);
	}					
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
			setCursor(cl_dat.M_curWTSTS_pbst);
 	        if(M_objSOURC == txtPRTCD)
			{				
				if((txtPRTTP.getText().trim()).length()==0)
				{
					setMSG("Please Enter Party Type..",'E');
					setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
    			try
    	  		{						
					M_strSQLQRY = " Select PT_PRTCD,PT_PRTNM from CO_PTMST";
					M_strSQLQRY += " where PT_PRTTP = '"+ txtPRTTP.getText().trim()+ "'"; 											
					if (txtPRTCD.getText().length()!=0)
						M_strSQLQRY += " AND PT_PRTCD like'"+ (txtPRTCD.getText().trim()).toUpperCase() +"%' ";					
					M_strSQLQRY += " and isnull(pt_stsfl,'') <>'X' order by PT_PRTCD"; 					    				
    				M_strHLPFLD = "txtPRTCD";			           											
    				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT");    								
    			}
			    catch(Exception L_EX)
    			{
    				setMSG(L_EX ," F1 help..");    		    
    			}    	
 			}			
			if(M_objSOURC == txtPRTTP)
			{			
				txtPRTCD.setText("");
				txtPRTNM.setText("");
    			try
    	  		{						
					M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT'";
					if (txtPRTTP.getText().length()!=0)
						M_strSQLQRY += " AND CMT_CODCD ='"+ (txtPRTTP.getText().trim()).toUpperCase() +"'";	
					M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";											
    				M_strHLPFLD = "txtPRTTP";			           											
    				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Type","Description"},2,"CT");    								
    			}
				catch(Exception L_EX)
    			{
    				setMSG(L_EX ," F1 help..");    		    
    			}				
 			}
		setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPRTTP")
		{			
			txtPRTTP.setText(cl_dat.M_strHLPSTR_pbst);				
			strPRTDS=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();						
		}		
		if(M_strHLPFLD == "txtPRTCD")
		{			
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);				
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
			if((txtPRTNM.getText().trim()).length()>0)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
		{				
			setMSG("Please Enter Consignment Number Or Press F1 for Help..",'N');
			return;
		}
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"co_rppte.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "co_rppte.doc";				
			getALLREC();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,strPRTDS+" Enrollment Form"," ");
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
    *Method to fetch data from CO_PTMST table & club it with Header in Data Output Stream.
	*/
	private void getALLREC()
	{ 					
		String L_strTSTFL;
		String L_strDATA,L_strDATE="",L_strDATE1="",L_strDATE2="";
		intRECCT=0;
		java.sql.Date L_datTEMP;
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
		{
			flgTRPFLT = txtPRTTP.getText().equalsIgnoreCase("T") ? true : false;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Customer Enrollment Form </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    									
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}			
			prnHEADER();			
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM,";
			M_strSQLQRY += "PT_PINCD,PT_CONNM,PT_TEL01,PT_TEL02,PT_FAXNO,PT_STXNO,PT_STXDT,PT_CSTNO,";
			M_strSQLQRY += "PT_CSTDT,PT_ECCNO,PT_ITPNO,PT_EXCNO,PT_RNGDS,PT_DIVDS,PT_CLLDS,PT_TSTFL,";
			M_strSQLQRY += "PT_ZONCD,PT_TRNCD,PT_TINNO,PT_TINDT,PT_VATNO,PT_VATDT,PT_DSRCD,PT_EMLMR,PT_LUSBY,PT_LUPDT,PT_DSTCD,";			
			M_strSQLQRY += "PT_GRPCD,PT_MOBNO,PT_COWEB,PT_EMLPR,PT_INFFL,PT_DEFCT,PT_EMLRF,PT_EVLDT,PT_EMLAC";
			M_strSQLQRY += " from CO_PTMST";
			M_strSQLQRY += " where PT_PRTTP ='"+ txtPRTTP.getText().trim()+"'";			
			M_strSQLQRY += " and PT_PRTCD ='"+ txtPRTCD.getText().trim()+"'";			
			M_strSQLQRY += " and isnull(PT_STSFL,'') <> 'X'";									
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(M_rstRSSET !=null)				
			{									
				if(M_rstRSSET.next())
				{
					dosREPORT.writeBytes(padSTRING('R',strPRTDS+" Code",16)+"  : " + padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),""),80));
					dosREPORT.writeBytes("\n\n");										
					dosREPORT.writeBytes(padSTRING('R',strPRTDS+" Name",16)+"  : " + nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"\n\n");
					dosREPORT.writeBytes(padSTRING('R',strPRTDS+" Address",16)+"  : " + nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),"")+"\n\n");
					dosREPORT.writeBytes("                  : "+nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),"")+"\n\n");
					dosREPORT.writeBytes("                  : "+nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),"")+"\n\n");
					dosREPORT.writeBytes("                  : "+nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),"")+"\n\n");
					dosREPORT.writeBytes(padSTRING('R',"City Name         : "+nvlSTRVL(M_rstRSSET.getString("PT_CTYNM"),""),35));
					dosREPORT.writeBytes(padSTRING('R',"Pin Code  : "+nvlSTRVL(M_rstRSSET.getString("PT_PINCD"),""),22));
					
					L_strDATA = nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"");
					if(hstCODDS.containsKey((String)L_strDATA))
						L_strDATA= hstCODDS.get(L_strDATA).toString();					
					dosREPORT.writeBytes("Destination     : "+L_strDATA);					
					dosREPORT.writeBytes("\n\n");
					
					
					
					L_strDATA="";
					L_strDATA = nvlSTRVL(M_rstRSSET.getString("PT_TRNCD"),"");
					exeSKIPPRT(padSTRING('R',"Transporter       : "+L_strDATA,57));
					exeBLKPRT("Group Code      : "+nvlSTRVL(M_rstRSSET.getString("PT_GRPCD"),"")); dosREPORT.writeBytes("\n\n");
					if(L_strDATA.length()!=0)					   					
						exeSKIPPRT("  "+getPRTNM("T",L_strDATA));						 									
					///exeSKIPPRT("\n\n");
					//exeSKIPPRT("\n");
					
					L_strDATA="";
					exeBLKPRT("Distributor Name  : ");
					L_strDATA = nvlSTRVL(M_rstRSSET.getString("PT_DSRCD"),"");
					if(L_strDATA.length()>0)					   					
						exeBLKPRT(padSTRING('R',getPRTNM("D",L_strDATA),37));
					else
						exeBLKPRT(padSTRING('R',"",37));
					exeSKIPPRT("\n\n");
					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("CONTACT INFORMATION"+"\n\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);	
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					
					dosREPORT.writeBytes(padSTRING('R',"Contact Person    : "+nvlSTRVL(M_rstRSSET.getString("PT_CONNM"),""),57));
					dosREPORT.writeBytes("Mobile No.      : "+nvlSTRVL(M_rstRSSET.getString("PT_MOBNO"),"")+"\n\n");
					
					dosREPORT.writeBytes(padSTRING('R',"Email Id          : "+nvlSTRVL(M_rstRSSET.getString("PT_EMLRF"),""),57));
					dosREPORT.writeBytes("Co.Web Site     : "+nvlSTRVL(M_rstRSSET.getString("PT_COWEB"),"")+"\n\n");
					
					dosREPORT.writeBytes(padSTRING('R',"Email Id Pur.     : "+nvlSTRVL(M_rstRSSET.getString("PT_EMLPR"),""),57));
					dosREPORT.writeBytes("Email Id Mkt.   : "+nvlSTRVL(M_rstRSSET.getString("PT_EMLMR"),"")+"\n\n");
					
					dosREPORT.writeBytes(padSTRING('R',"Email Id A/c.     : "+nvlSTRVL(M_rstRSSET.getString("PT_EMLAC"),""),57));
					dosREPORT.writeBytes("Fax Number      : "+nvlSTRVL(M_rstRSSET.getString("PT_FAXNO"),"")+"\n\n");	
					
					dosREPORT.writeBytes(padSTRING('R',"Telephone No.1    : "+nvlSTRVL(M_rstRSSET.getString("PT_TEL01"),""),57));					
					dosREPORT.writeBytes("Telephone No.2  : "+nvlSTRVL(M_rstRSSET.getString("PT_TEL02"),"")+"\n\n");						
					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);	
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("DETAIL INFORMATION\n\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);	
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					L_strDATE1 ="";															
					L_datTEMP = M_rstRSSET.getDate("PT_STXDT");
					if(L_datTEMP!= null)
						L_strDATE1=M_fmtLCDAT.format(L_datTEMP);
					exeSKIPPRT(padSTRING('R',"S.T.No./WEF       : "+nvlSTRVL(M_rstRSSET.getString("PT_STXNO"),""),57));					
					L_strDATE2 ="";
					L_datTEMP = M_rstRSSET.getDate("PT_VATDT");
					if(L_datTEMP!= null)
						L_strDATE2=M_fmtLCDAT.format(L_datTEMP);
					exeSKIPPRT("VAT No./WEF     : "+nvlSTRVL(M_rstRSSET.getString("PT_VATNO"),"")+"\n");
					exeSKIPPRT(padSTRING('R',"                    "+L_strDATE1,57)+"                  "+L_strDATE2+"\n\n");
					
					//L_datTEMP = M_rstRSSET.getDate("PT_TINDT");
					//if(L_datTEMP!= null)
					//	L_strDATE=M_fmtLCDAT.format(L_datTEMP);
					//exeSKIPPRT("TIN No./WEF     : "+nvlSTRVL(M_rstRSSET.getString("PT_TINNO"),"")+"   "+L_strDATE+"\n\n");
					L_strDATE1 ="";
					L_datTEMP = M_rstRSSET.getDate("PT_CSTDT");
					if(L_datTEMP!= null)
						L_strDATE1=M_fmtLCDAT.format(L_datTEMP);
					exeSKIPPRT(padSTRING('R',"C.S.T. No./WEF    : "+nvlSTRVL(M_rstRSSET.getString("PT_CSTNO"),""),57));
					L_strDATE2 ="";
					L_datTEMP = M_rstRSSET.getDate("PT_TINDT");
					if(L_datTEMP!= null)
						L_strDATE2=M_fmtLCDAT.format(L_datTEMP);
					exeSKIPPRT("TIN No./WEF     : "+nvlSTRVL(M_rstRSSET.getString("PT_TINNO"),"")+"\n");
					exeSKIPPRT(padSTRING('R',"                    "+L_strDATE1,57)+"                  "+L_strDATE2+"\n\n");
					
					exeSKIPPRT(padSTRING('R',"Division          : "+nvlSTRVL(M_rstRSSET.getString("PT_DIVDS"),""),57));
					exeSKIPPRT("E.C.C. Number   : "+nvlSTRVL(M_rstRSSET.getString("PT_ECCNO"),"")+"\n\n");										
					
					dosREPORT.writeBytes(padSTRING('R',"IT Pan Number     : "+nvlSTRVL(M_rstRSSET.getString("PT_ITPNO"),""),57));					
					exeBLKPRT("Collectorate    : "+nvlSTRVL(M_rstRSSET.getString("PT_CLLDS"),""));dosREPORT.writeBytes("\n\n");	
					
					exeSKIPPRT(padSTRING('R',"Excise Number     : "+nvlSTRVL(M_rstRSSET.getString("PT_EXCNO"),""),57));					
					exeSKIPPRT("Range           : "+nvlSTRVL(M_rstRSSET.getString("PT_RNGDS"),"")+"\n\n");					
					
					L_strDATA="";
					L_strDATA = nvlSTRVL(M_rstRSSET.getString("PT_ZONCD"),"");					
					if(hstCODDS.containsKey((String)L_strDATA))					
						L_strDATA = hstCODDS.get(L_strDATA).toString();															
					dosREPORT.writeBytes(padSTRING('R',"Zone              : "+ L_strDATA,57));																								
					exeBLKPRT("Ind/Fgn         : "+nvlSTRVL(M_rstRSSET.getString("PT_INFFL"),""));dosREPORT.writeBytes("\n\n");
					
					L_strDATA="";
					L_strDATA=nvlSTRVL(M_rstRSSET.getString("PT_TSTFL"),"");
					if((L_strDATA.equals("y"))||(L_strDATA.equals("Y")))
						L_strDATA="YES";
					else
						L_strDATA="NO";
						
					exeSKIPPRT("Test Cert.        : " + L_strDATA +"\n\n");																																	
					
					L_strDATA = nvlSTRVL(M_rstRSSET.getString("PT_DEFCT"),"");					
					if(hstCODDS.containsKey((String)L_strDATA))					
						L_strDATA = hstCODDS.get(L_strDATA).toString();															
					exeSKIPPRT("Category          : "+L_strDATA );dosREPORT.writeBytes("\n");
					
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");
					dosREPORT.writeBytes(padSTRING('R',"Entered By : "+nvlSTRVL(M_rstRSSET.getString("PT_LUSBY"),""),35));					
					L_strDATE ="";
					L_datTEMP = M_rstRSSET.getDate("PT_LUPDT");
					if(L_datTEMP!= null)
						L_strDATE=M_fmtLCDAT.format(L_datTEMP);
					dosREPORT.writeBytes(padSTRING('R',"Date : "+L_strDATE,25));					
					dosREPORT.writeBytes(padSTRING('R',"Verified By : ",35) + "\n\n");
					dosREPORT.writeBytes(padSTRING('R',"Faxed to "+(flgTRPFLT ? "Transporter" : "Distributer")+" on : ",60));
					dosREPORT.writeBytes(padSTRING('R',"By : ",35)+"\n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");					
					intRECCT = 1;
				}
				M_rstRSSET.close();
			}
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
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getALLREC");
		}
	}
	/**
	Method to validate Party Type & Party Code,i.e. to check for blank and wrong Inputs.
	*/
	boolean vldDATA()
	{
		if(txtPRTTP.getText().length()==0)			
		{
			setMSG("Please Enter Party Type and Press Enter Key.. ",'E');
			txtPRTTP.requestFocus();			
			return false;
		}
		if(txtPRTCD.getText().length()==0)			
		{
			setMSG("Please Enter Party Code and Press Enter Key OR Press F1 to select from list.. ",'N');
			txtPRTCD.requestFocus();			
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
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{ 
			if (M_cmbDESTN.getSelectedIndex() == 0)
			{	
				setMSG("Please Select the Printer from Printer List ..",'N');
				return false;
			}
		}
		if(hstCODDS.containsKey(txtPRTTP.getText().trim()))
			strPRTDS= hstCODDS.get(txtPRTTP.getText().trim()).toString();				
		return true;
	}
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;										
			dosREPORT.writeBytes("\n\n");			
			if(M_rdbTEXT.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R'," ",35));				
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,38)+"\n\n");							
				dosREPORT.writeBytes(padSTRING('R'," ",36));			
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);			
				dosREPORT.writeBytes(padSTRING('R',strPRTDS.toUpperCase()+" ENROLLMENT FORM",38)+"\n");															
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);			
				dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,94));
			}
			else
			{									
				dosREPORT.writeBytes("<b><CENTER><PRE style =\" font-size : 15 pt \">");
				dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n\n");							
				dosREPORT.writeBytes("</B></CENTER>");				
				dosREPORT.writeBytes("<B><CENTER>");
				dosREPORT.writeBytes(strPRTDS.toUpperCase() + " ENROLLMENT FORM"+"\n");				
				dosREPORT.writeBytes("</B></CENTER></FONT></PRE>");				
				dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,94) );																
				dosREPORT.writeBytes("<PRE style =\" font-size : 10 pt \">");
			}
			dosREPORT.writeBytes("\n-----------------------------------------------------------------------------------------------\n");												
			
				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	 * Method to get the description of the Transporter & Destributer for corresponding code.
	 * @param P_strPRTTP String argument to pass the party Type.
	 * @param P_strPRTCD String argument to pass the party code.
	 */
	private String getPRTNM(String P_strPRTTP, String P_strPRTCD)
	{
		try
		{
			M_strSQLQRY = "Select PT_PRTNM from CO_PTMST";					
			M_strSQLQRY += " where PT_PRTTP ='"+ P_strPRTTP +"'";			
			M_strSQLQRY += " AND PT_PRTCD ='"+ P_strPRTCD +"'";			
			M_strSQLQRY += " AND isnull(PT_STSFL,'') <> 'X'";									
			M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);					
			if(M_rstRSSET1 !=null)				
			{									
				if(M_rstRSSET1.next())
				{
					return nvlSTRVL(M_rstRSSET1.getString("PT_PRTNM"),"");
				}
				M_rstRSSET1.close();
			}
			return "";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRTNM");
			return "";
		}
	}
}
		