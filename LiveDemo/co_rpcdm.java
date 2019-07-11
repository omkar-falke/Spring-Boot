/*
Program Name  : Code Master Listing Report
Program Desc. : Report for taking the Code Master Details
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 16th May 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.InputVerifier;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.util.StringTokenizer;

/**<pre>
System Name : Report for taking the Code Master Details.
 
Program Name : Code Master Listing Report

Purpose : This program will generate a Report for the details of the given 
Main Group & subGroup range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDMST      CM_CGMTP,CM_CGSTP                                        #
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD                            #	     
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name             Column Name  Table name  Type/Size    Description
--------------------------------------------------------------------------------------
txtFRMTP & txtTOMTP    CMT_CGMST    CO_CDTRN    VARCHAR(3)   Main Group
txtFRSTP & txtTOSTP    CMT_CMSTP    CO_CDTRN    VARCHAR(7)   Sub Group
--------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)CO_CDTRN
                        2)CO_CDMST
                        
<B>Conditions Give in Query:</b>
    Data is taken from CO_CDMST & CO_CDTRN for,
        1) CMT_CGMTP = CM_CGMTP 
        2) AND CMT_CGSTP = CM_CGSTP
     for Rang selection(if Complete main Group & subgroup range is given.)
        3) AND CM_CGMTP||CM_CGSTP between given range.
     for main Group Range(if From Main Group & to Main Group is given.)
        4) AND CM_CGMTP between given Main Group range.
     for given Main Group & Sub Group(if only From Main & sub Group is given.)
        5) AND CM_CGMTP = given Main Group.
        6) AND CM_CGSTP = given Sub Group.

<B>Validations :</B>
   - Given Main Group & Sub Group must be valid.   
</I> */

public class co_rpcdm extends cl_rbase
{									/** JTestField to display & accept Main Group.*/
	private JTextField txtFRMTP;	/** JTestField to display & accept Subgroup.*/
	private JTextField txtTOMTP;	/** JTestField to display & accept Main Group.*/
	private JTextField txtFRSTP;	/** JTestField to display & accept Subgroup.*/
	private JTextField txtTOSTP;	
									/**	JCheckBox to select custom Report.*/
	private JCheckBox chkSELCT;		/** Jpanel to place all the Components requried for custom Report.*/
	private JPanel pnlTABLE;		/** JComboBox to display & to Specify the Fontsize for the Report.*/
	private JComboBox cmbFNTSZ;		/** JComboBox to display & to Specify the Page Size for the Report.*/
	private JComboBox cmbPAGSZ;		/** JTable to display & select the columns requried in the Report.*/
	private cl_JTable tblITMDT;
	
	private ResultSetMetaData rsmDATA;		/** Integer variable to count the numer of records fetched.*/
	private int intRECCT;					/** String variable for generated report file name.*/
	private String strFILNM;				/** FileOutPutStream to generate the report file from DataSteam.*/
	private FileOutputStream fosREPORT;		/** DataOutPutStream to store & append data to generate the report file.*/
    private DataOutputStream dosREPORT;	
											/**	Final integer to specify the check flag Column.*/
	private final int TBL_SELCT = 0;		/**	Final integer to specify the Column Name Column.*/
	private final int TBL_CNAME = 1;		/**	Final integer to specify the Column Width Column.*/
	private final int TBL_CSIZE = 2;	
	private INPVF objINPVR = new INPVF();	
								/** Integer variable for number of charector in row for selected Page Size.*/
	private int intROWLN;		/** String Variable for dynamically generated Table Header.*/
	private String strTHEAD="";	/** StringBuffer object for selected Column list.*/
	private StringBuffer stbCOLLS;/** StringBuffer object dynamicaly generated Dotted Line.*/
	private StringBuffer stbDOTLN = new StringBuffer("-");/** Integer variable for number of lines in selected page.*/
	private int intNOLIN;/** Integer variable for number of charectores in selected page.*/
	private int intNOCHR;/** String Variable for selected Font size*/
	private String strFNTSZ="";		
	private int arrCOLWD[]; /** Array of integers for Selected Column Width.*/
	co_rpcdm()
	{		
		super(1);
		try
		{	
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);	
									
			add(new JLabel("Main Type"),3,4,1,.8,this,'R');
			add(new JLabel("Sub Type"),3,5,1,.8,this,'R');
		    add(new JLabel("From"),4,3,1,.5,this,'R');
			add(txtFRMTP = new TxtLimit(3),4,4,1,1,this,'L');
			add(txtFRSTP = new TxtLimit(7),4,5,1,1,this,'L');
			add(new JLabel("To"),5,3,1,.5,this,'R');
			add(txtTOMTP = new TxtLimit(3),5,4,1,1,this,'L');
			add(txtTOSTP = new TxtLimit(7),5,5,1,1,this,'L');	
			
			add(chkSELCT = new JCheckBox("Custom Report"),6,4,1,1.5,this,'L');					
			
			pnlTABLE= new JPanel(null);
			add(new JLabel("Font"),1,1,1,.8,pnlTABLE,'R');
			add(cmbFNTSZ = new JComboBox(),1,2,1,1.7,pnlTABLE,'L');						
			
			add(new JLabel("Page Size"),2,1,1,.8,pnlTABLE,'R');
			add(cmbPAGSZ = new JComboBox(),2,2,1,1.7,pnlTABLE,'L');
									
			String[] L_COLHD = {"Select","Column Name","Width"};
      		int[] L_COLSZ = {50,120,60};	    				
			tblITMDT = crtTBLPNL1(pnlTABLE,L_COLHD,30,3,1,8.3,2.7,L_COLSZ,new int[]{0});				
			
			add(pnlTABLE,7,6,12,4,this,'R');
			
			txtFRMTP.setInputVerifier(objINPVR);
			txtTOMTP.setInputVerifier(objINPVR);
			txtFRSTP.setInputVerifier(objINPVR);
			txtTOSTP.setInputVerifier(objINPVR);
						
			cmbPAGSZ.addItem("Plain A4");
			cmbPAGSZ.addItem("Fanfold 210mm - 12in");
			cmbPAGSZ.addItem("Fanfold 358mm - 12in");
								
			M_pnlRPFMT.setVisible(true);
			
			pnlTABLE.setVisible(false);
			tblITMDT.cmpEDITR[TBL_CNAME].setEnabled(false);
			tblITMDT.cmpEDITR[TBL_CSIZE].setEnabled(false);
			setENBL(false);
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
		if(false==L_flgSTAT)
		{
			clrCOMP();
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{		
		super.actionPerformed(L_AE); 
		try
		{
			if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
			{				
				cl_dat.M_PAGENO=0;
				cl_dat.M_intLINNO_pbst=0;				
			}		
			if(M_objSOURC == chkSELCT)
			{
				if(chkSELCT.isSelected())
				{
					pnlTABLE.setVisible(true);
					try
					{
						M_rstRSSET = cl_dat.exeSQLQRY("select * from CO_CDTRN");
						rsmDATA = M_rstRSSET.getMetaData();									
						int k = 0;
						for(int i=3;i<=rsmDATA.getColumnCount();i++)
						{								
							tblITMDT.setValueAt(rsmDATA.getColumnName(i),i-3,TBL_CNAME);
							tblITMDT.setValueAt(String.valueOf(rsmDATA.getColumnDisplaySize(i)).toString(),i-3,TBL_CSIZE);							
						}
						M_rstRSSET.close();																												
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"Displaying data in the table");
					}
					if(M_rdbHTML.isSelected())
					{
						cmbFNTSZ.removeAllItems();
						cmbFNTSZ.addItem("Size 8");
						cmbFNTSZ.addItem("Size 9");
						cmbFNTSZ.addItem("Size 10");
					}		
					else
					{			
						cmbFNTSZ.removeAllItems();
						cmbFNTSZ.addItem("CPI10");
						cmbFNTSZ.addItem("CPI12");
						cmbFNTSZ.addItem("CPI15");
						cmbFNTSZ.addItem("CPI17");					
					}
				}
				else
				{
					pnlTABLE.setVisible(false);
					strFNTSZ="";
				}
			}
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{					
				if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)	
				{
					cl_dat.M_cmbOPTN_pbst.requestFocus();				
					setENBL(false);				
				}
				else
				{
					setENBL(true);
					setMSG("Please Enter Main Group to Specify Range..",'N');				
					txtFRMTP.requestFocus();
				}				
			}		
			if(M_objSOURC == M_rdbHTML)
			{
				if(chkSELCT.isSelected())
				{
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("Size 8");
					cmbFNTSZ.addItem("Size 9");
					cmbFNTSZ.addItem("Size 10");
				}
			}
			if(M_objSOURC == M_rdbTEXT)
			{
				if(chkSELCT.isSelected())
				{
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("CPI10");
					cmbFNTSZ.addItem("CPI12");
					cmbFNTSZ.addItem("CPI15");
					cmbFNTSZ.addItem("CPI17");					
				}
			}		
				
			if(M_objSOURC == cmbFNTSZ)		
				strFNTSZ = cmbFNTSZ.getSelectedItem().toString().trim();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionP");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    		
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {			
			if(M_objSOURC == txtFRMTP)
			{
				txtFRSTP.setText("");
 				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{									
    				M_strSQLQRY = "Select distinct CM_CGMTP from CO_CDMST";
					//M_strSQLQRY += "  where SUBSTRING(CM_CGSTP,1,2)='"+cl_dat.ocl_dat.M_SPSYSCD+"'";					
					if((txtTOMTP.getText().trim().length()!=0) || (txtFRMTP.getText().trim().length()!=0))
						M_strSQLQRY +=	" where";	
					if (txtFRMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP LIKE '" +txtFRMTP.getText().trim() + "%'";
					if((txtTOMTP.getText().trim().length()!=0) && (txtFRMTP.getText().trim().length()!=0))
						M_strSQLQRY +=	" and";
					if (txtTOMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP <= '" +txtTOMTP.getText().trim() + "'";
					M_strSQLQRY += " order by CM_CGMTP";				
    				M_strHLPFLD = "txtFRMTP";					
    				cl_hlp(M_strSQLQRY,1,1,new String[]{"Main Group"},1,"CT");    			
    			}
    			catch(Exception L_EX)
    			{
    			    setMSG(L_EX ," F1 help..");    		    
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
    			setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(M_objSOURC==txtFRSTP)
			{
				if(txtFRMTP.getText().trim().length()==0)
				{
					setMSG("Please Enter From Main Group First..",'E');
					return;
				}
				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{									
    				M_strSQLQRY = "Select distinct CM_CGSTP,CM_CGRDS from CO_CDMST";
					M_strSQLQRY +=	" where CM_CGMTP = '" +txtFRMTP.getText().trim() + "'";
					//M_strSQLQRY += "  where SUBSTRING(CM_CGSTP,1,2)='"+cl_dat.ocl_dat.M_SPSYSCD+"'";					
					if(txtFRSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP LIKE '" +txtFRSTP.getText().trim() + "%'";
					if(txtTOSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP <= '" +txtTOSTP.getText().trim() + "'";
					M_strSQLQRY += " order by CM_CGSTP";				
    				M_strHLPFLD = "txtFRSTP";					
    				cl_hlp(M_strSQLQRY,2,1,new String[]{"Sub Group","Description"},2,"CT");    			
    			}
    			catch(Exception L_EX)
    			{
    			    setMSG(L_EX ," F1 help..");    		    
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
    			setCursor(cl_dat.M_curDFSTS_pbst);
			}			
			if(M_objSOURC==txtTOMTP)
			{
				txtTOSTP.setText("");
				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{									
    				M_strSQLQRY = "Select distinct CM_CGMTP from CO_CDMST";
					if((txtTOMTP.getText().trim().length()!=0) || (txtFRMTP.getText().trim().length()!=0))
						M_strSQLQRY +=	" where";					
					if (txtTOMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP LIKE '" +txtTOMTP.getText().trim() + "%'";
					if((txtTOMTP.getText().length()!=0) && (txtFRMTP.getText().length()!=0))
						M_strSQLQRY +=	" AND ";
					if (txtFRMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP >= '" +txtFRMTP.getText().trim() + "'";
					M_strSQLQRY += " order by CM_CGMTP";				
    				M_strHLPFLD = "txtTOMTP";					
    				cl_hlp(M_strSQLQRY,1,1,new String[]{"Main Group"},1,"CT");    			
    			}
    			catch(Exception L_EX)
    			{
    			    setMSG(L_EX ," F1 help..");    		    
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
    			setCursor(cl_dat.M_curDFSTS_pbst);
			}						
			if(M_objSOURC==txtTOSTP)
			{
				if(txtTOMTP.getText().trim().length()==0)
				{
					setMSG("Please Enter To Main Group First..",'E');
					return;
				}
				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{									
    				M_strSQLQRY = "Select distinct CM_CGSTP,CM_CGRDS from CO_CDMST";
					M_strSQLQRY +=	" where CM_CGMTP = '" +txtTOMTP.getText().trim() + "'";
					//M_strSQLQRY += "  where SUBSTRING(CM_CGSTP,1,2)='"+cl_dat.ocl_dat.M_SPSYSCD+"'";					
					if(txtTOSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP LIKE '" +txtTOSTP.getText().trim() + "%'";
					if(txtFRSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP >= '" +txtFRSTP.getText().trim() + "'";
					M_strSQLQRY += " order by CM_CGSTP";				
    				M_strHLPFLD = "txtTOSTP";					
    				cl_hlp(M_strSQLQRY,2,1,new String[]{"Sub Group","Description"},2,"CT");
    			}
    			catch(Exception L_EX)
    			{
    			    setMSG(L_EX ," F1 help..");    		    
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
    			setCursor(cl_dat.M_curDFSTS_pbst);
			}			
 	    }
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
 	    {			
			if(M_objSOURC == txtFRMTP)
			{
				if(txtFRMTP.getText().trim().length()>0)
				{
					txtFRSTP.requestFocus();
					setMSG("Enter MainGroup Or Press F1 to select from list..",'N');
				}
				else
					setMSG("Main Group cannot be blank, Press F1 to select from list..",'E');
			}			
			if(M_objSOURC == txtFRSTP)
			{
				txtFRSTP.requestFocus();
				setMSG("Enter Sub Group Or Press F1 to select from list..",'N');
			}
			if(M_objSOURC == txtTOMTP)
			{				
				txtTOSTP.requestFocus();
				setMSG("Enter Main Group Or Press F1 to select from list..",'N');
			}
			if(M_objSOURC == txtTOSTP)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();				
			}
		}
	}
	/**
	Method for execution of help for Main Group & Sub Group.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFRMTP")
		{
			txtFRMTP.setText(cl_dat.M_strHLPSTR_pbst);							
			if (cl_dat.M_strHLPSTR_pbst.length() !=0)			
			    txtFRSTP.requestFocus();
		}		
		if(M_strHLPFLD == "txtFRSTP")
		{
			txtFRSTP.setText(cl_dat.M_strHLPSTR_pbst);							
			if (cl_dat.M_strHLPSTR_pbst.length() !=0)			
			    txtTOMTP.requestFocus();
		}		
		if(M_strHLPFLD == "txtTOMTP")
		{
			txtTOMTP.setText(cl_dat.M_strHLPSTR_pbst);							
			if (cl_dat.M_strHLPSTR_pbst.length() !=0)			
			    txtTOSTP.requestFocus();
		}		
		if(M_strHLPFLD == "txtTOSTP")
		{
			txtTOSTP.setText(cl_dat.M_strHLPSTR_pbst);							
			if (cl_dat.M_strHLPSTR_pbst.length() !=0)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{				
		if (!vldDATA())									
			return;						
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"co_rpcdm.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "co_rpcdm.doc";													
			getDATA();			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM," Codes Master Listing"," ");
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
    *Method to fetch data from tables CO_CDMST and CO_CDTRN & to club it with Header
    *DataOutputStream.
	*/
	private void getDATA()
	{ 				
		String L_strCODDS,L_strCGRDS="",L_strCCSVL,L_strSHRDS,L_strCHP01,L_strCHP02,L_strNMP01,L_strNMP02,L_strCODCD;		
		String L_strNCSVL,L_strFRMTP,L_strFRSTP,L_strTOMTP,L_strTOSTP;	
		String L_strCGSTP="",L_strOCGSTP="";
		String L_strCGMTP="",L_strOCGMTP="";				
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);												
				if(strFNTSZ.equals("CPI10"))				
					prnFMTCHR(dosREPORT,M_strCPI10);																
				else if(strFNTSZ.equals("CPI12"))
					prnFMTCHR(dosREPORT,M_strCPI12);					
			    else if(strFNTSZ.equals("CPI15"))
				{
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI17);
				}
				else
				{
					prnFMTCHR(dosREPORT,M_strCPI12);
					prnFMTCHR(dosREPORT,M_strCPI17);
				}																
			}
			if(M_rdbHTML.isSelected())
			{
				strFNTSZ = strFNTSZ.substring(strFNTSZ.length()-2).trim();									
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Code Master Listing </title> </HEAD> <BODY><P><PRE style =\" font-size :"+ strFNTSZ +"pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}			
			prnHEADER();			
						
			L_strFRMTP = txtFRMTP.getText().trim();
			L_strTOMTP = txtTOMTP.getText().trim();	
			L_strFRSTP = txtFRSTP.getText().trim();	
			L_strTOSTP = txtTOSTP.getText().trim();	
				
			String LM_ADDSTR = "";
			if(L_strFRMTP.length() > 0 && L_strFRSTP.length() > 0 && L_strTOMTP.length() > 0 && L_strTOSTP.length() > 0)			
				LM_ADDSTR = " AND CM_CGMTP||CM_CGSTP between '"+L_strFRMTP+L_strFRSTP+"' AND '"+L_strTOMTP+L_strTOSTP+"'";							
			else if(L_strFRMTP.length() > 0 && L_strFRSTP.length() > 0 && L_strTOMTP.length() > 0)
			{
				LM_ADDSTR = " AND CM_CGMTP between '"+L_strFRMTP+"' AND '"+L_strTOMTP+"' AND";
				LM_ADDSTR += " CM_CGSTP > '"+L_strFRSTP+"'";
			}
			else if(L_strFRMTP.length() > 0 && L_strTOMTP.length() > 0)		
				LM_ADDSTR = " AND CM_CGMTP between '"+L_strFRMTP+"' AND '"+L_strTOMTP+"'";							
			else if(L_strFRMTP.length() > 0 && L_strFRSTP.length() > 0)			
				LM_ADDSTR = " AND CM_CGMTP = '"+L_strFRMTP+"' AND CM_CGSTP = '"+L_strFRSTP+"'";							
			else if(L_strFRMTP.length() > 0)
				LM_ADDSTR = " AND CMT_CGMTP = '"+L_strFRMTP+"'";			
			if(chkSELCT.isSelected())
			{				
				M_strSQLQRY = " select CM_CGMTP,CM_CGSTP,CM_CGRDS,"+ stbCOLLS.toString() +" from CO_CDMST,CO_CDTRN where";
				M_strSQLQRY += " CMT_CGMTP = CM_CGMTP AND CMT_CGSTP = CM_CGSTP";				
				M_strSQLQRY += LM_ADDSTR;					
				M_strSQLQRY += " ORDER BY CMT_CGMTP,CMT_CGSTP,CMT_CODCD";
			}
			else
			{
				M_strSQLQRY = "Select CM_CGMTP,CM_CGSTP,CM_CGRDS,CMT_CODCD,CMT_CODDS,CMT_SHRDS,CMT_CHP01,";
				M_strSQLQRY += " CMT_CHP02,CMT_NMP01,CMT_NMP02,CMT_NCSVL,CMT_CCSVL from CO_CDMST,CO_CDTRN where";
				M_strSQLQRY += " CMT_CGMTP = CM_CGMTP AND CMT_CGSTP = CM_CGSTP";				
				M_strSQLQRY += LM_ADDSTR;					
				M_strSQLQRY += " ORDER BY CMT_CGMTP,CMT_CGSTP,CMT_CODCD";
			}
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);					
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
				{						
					if(chkSELCT.isSelected())
					{
						L_strCGMTP = M_rstRSSET.getString("CM_CGMTP");				
						L_strCGSTP = M_rstRSSET.getString("CM_CGSTP");	
						L_strCGRDS = nvlSTRVL(M_rstRSSET.getString("CM_CGRDS"),"");
						if((L_strCGMTP.equals(L_strOCGMTP))&&(L_strCGSTP.equals(L_strOCGSTP)))
						{
							StringTokenizer stkTEMP = new StringTokenizer(stbCOLLS.toString(),",");
							int i=0;
							while(stkTEMP.hasMoreTokens())
							{							
								String strTEMP = stkTEMP.nextToken();
								String strDATA = nvlSTRVL(M_rstRSSET.getString(strTEMP),"-");						
								dosREPORT.writeBytes(padSTRING('R',strDATA,arrCOLWD[i]));																				
								i++;
							}
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
						}
						else
						{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");						
							dosREPORT.writeBytes("\n"+padSTRING('R',L_strCGMTP,12)+padSTRING('R',L_strCGSTP,10)+L_strCGRDS +"\n");					
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b>");											
							L_strOCGMTP = L_strCGMTP;
							L_strOCGSTP = L_strCGSTP;	
							cl_dat.M_intLINNO_pbst +=2;
						}					
						if(cl_dat.M_intLINNO_pbst > intNOLIN)
						{					
							dosREPORT.writeBytes (stbDOTLN.toString());			
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
						}		
					}
					else
					{									
						L_strCGMTP = M_rstRSSET.getString("CM_CGMTP");				
						L_strCGSTP = M_rstRSSET.getString("CM_CGSTP");					
						L_strCGRDS = nvlSTRVL(M_rstRSSET.getString("CM_CGRDS"),"-");
						L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"-");
						L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"-");
						L_strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"-");
						L_strCHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"-");
						L_strCHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"-");
						L_strNMP01 = nvlSTRVL(M_rstRSSET.getString("CMT_NMP01"),"-");
						L_strNMP02 = nvlSTRVL(M_rstRSSET.getString("CMT_NMP02"),"-");
						L_strNCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),"-");
						L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"-");
					
					
						if(cl_dat.M_intLINNO_pbst> intNOLIN)
						{					
							dosREPORT.writeBytes ("---------------------------------------------------------------------------------------------------------------");			
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
						}				
						if((L_strCGMTP.equals(L_strOCGMTP))&&(L_strCGSTP.equals(L_strOCGSTP)))
						{
							if(L_strCODCD.length()>24)
								L_strCODCD = L_strCODCD.substring(0,10);
							dosREPORT.writeBytes(padSTRING('R',L_strCODCD,12));
							
							if(L_strCODDS.length()>24)
								L_strCODDS=L_strCODDS.substring(0,24);
							dosREPORT.writeBytes(padSTRING('R',L_strCODDS,25));
							dosREPORT.writeBytes(padSTRING('R',L_strSHRDS,16));
							
							if(L_strCHP01.length()>8)
								L_strCHP01=L_strCHP01.substring(0,8);						
							dosREPORT.writeBytes(padSTRING('R',L_strCHP01,9));
							if(L_strCHP02.length()>8)
								L_strCHP02=L_strCHP02.substring(0,8);
							dosREPORT.writeBytes(padSTRING('R',L_strCHP02,9));
							if(L_strNMP01.length()>8)
								L_strNMP01=L_strNMP01.substring(0,8);
							dosREPORT.writeBytes(padSTRING('L',L_strNMP01,9));
							if(L_strNMP02.length()>8)
								L_strNMP02=L_strNMP02.substring(0,8);
							dosREPORT.writeBytes(padSTRING('L',L_strNMP02,9));
							if(L_strNCSVL.length()>8)
								L_strNCSVL=L_strNCSVL.substring(0,8);
							dosREPORT.writeBytes(padSTRING('L',L_strNCSVL,9));
							dosREPORT.writeBytes("  "+ L_strCCSVL + "\n");
							cl_dat.M_intLINNO_pbst +=1;
						}
						else
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");						
							dosREPORT.writeBytes("\n"+L_strCGMTP+"  "+L_strCGSTP+"    "+L_strCGRDS +"\n");					
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b>");						
							cl_dat.M_intLINNO_pbst += 2;
							L_strOCGMTP = L_strCGMTP;
							L_strOCGSTP = L_strCGSTP;
						}																														
						intRECCT=1;
					}
					intRECCT=1;
				}											
				dosREPORT.writeBytes (stbDOTLN.toString());
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
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	Method to validate data entered, before execution of query & to check for blank and wrong Input.
	*/
	boolean vldDATA()
	{		
		if(chkSELCT.isSelected())
		{			
			if(cmbPAGSZ.getSelectedItem().equals("Plain A4"))
			{					
				if(strFNTSZ.equals("CPI10"))
				{					
					intNOLIN = 65;
					intNOCHR = 70;
				}	
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 85;
				}				
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 65;
					intNOCHR = 110;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 65;
					intNOCHR = 125;
				}	
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 110;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 100;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 100;
				}
			}				
			if(cmbPAGSZ.getSelectedItem().equals("Fanfold 210mm - 12in"))
			{
				if(strFNTSZ.equals("CPI10"))
				{
					intNOLIN = 65;
					intNOCHR = 85;
				}	
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 110;
				}				
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 70;
					intNOCHR = 130;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 70;
					intNOCHR = 150;
				}	
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 130;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 120;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 110;
				}
			}			
			if(cmbPAGSZ.getSelectedItem().equals("Fanfold 358mm - 12in"))
			{
				if(strFNTSZ.equals("CPI10"))
				{
					intNOLIN = 65;
					intNOCHR = 135;
				}	
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 165;
				}				
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 70;
					intNOCHR = 220;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 70;
					intNOCHR = 230;
				}	
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 200;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 180;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 160;
				}
			}
		}		
		else
		{
			intNOLIN = 65;
			intNOCHR = 110;
		}					
		intROWLN = 0;		
		if(chkSELCT.isSelected())
		{
			int L_intCOUNT = 0;
			strTHEAD="";		
			stbCOLLS = new  StringBuffer(); 				
			if(stbDOTLN != null)
				stbDOTLN.delete(0,stbDOTLN.length());		
			arrCOLWD= new int[30];				
			intROWLN = 0;
			for(int i=0; i< tblITMDT.getRowCount();i++)
			{			
				if(tblITMDT.getValueAt(i,TBL_SELCT).toString().equals("true"))
				{				
					String strNAME = tblITMDT.getValueAt(i,TBL_CNAME).toString();
					String strSIZE = tblITMDT.getValueAt(i,TBL_CSIZE).toString();
					
					if(Integer.parseInt(strSIZE)<10)
						strSIZE = "10";								
					
					intROWLN += Integer.parseInt(strSIZE)+1;
					strTHEAD += padSTRING('R',strNAME,Integer.parseInt(strSIZE)+1);								
					if(L_intCOUNT !=0)
						stbCOLLS.append(",");								
					stbCOLLS.append(strNAME);				
					arrCOLWD[L_intCOUNT] = Integer.parseInt(strSIZE)+1;											
					L_intCOUNT++;					
				}
			}		
			if(intROWLN < 60)
				intROWLN=60;
			for(int i=0;i<intROWLN;i++)
				stbDOTLN.append("-");
			if(L_intCOUNT==0)
			{
				setMSG("No column is selected, Please select column/s..",'E');
				return false;
			}
			
			if(cmbPAGSZ.getSelectedItem().equals("Fanfold 358mm - 12in"))
			{
				if(intNOCHR <intROWLN)
				{
					setMSG("Report cannot be fit into "+cmbPAGSZ.getSelectedItem().toString() +" page Please Select less No. of Columns..",'E');
					return false;
				}
			}
			else
			{
				if(intNOCHR <intROWLN)
				{
					setMSG("Report cannot be fit into "+cmbPAGSZ.getSelectedItem().toString() +" page Please Select Larger Size Paper..",'E');
					return false;
				}
			}					
		}		
		if(txtFRMTP.getText().length()!=3)			
		{
			setMSG("Please Enter valid Main Group OR Press F1 key to select From list.. ",'E');
			txtFRMTP.requestFocus();			
			return false;
		}
		if((txtFRSTP.getText().length() > 0)&& (txtFRSTP.getText().length()!= 7))
		{
			setMSG("Please enter valid sub group OR Press F1 key to select From list.. ",'E');
			txtFRSTP.requestFocus();			
			return false;
		}	
		if((txtTOMTP.getText().length() > 0)&& (txtTOMTP.getText().length()!= 3))
		{
			setMSG("Please enter valid main group OR Press F1 key to select From list.. ",'E');
			txtTOMTP.requestFocus();			
			return false;
		}	
		if((txtTOSTP.getText().length() > 0)&& (txtTOSTP.getText().length()!= 7))
		{
			setMSG("Please enter valid main group OR Press F1 key to select From list.. ",'E');
			txtTOSTP.requestFocus();			
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
			if(!chkSELCT.isSelected())
				intROWLN=110;
			dosREPORT.writeBytes("\n\n\n\n\n");			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWLN-24));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			dosREPORT.writeBytes(padSTRING('R',"Codes Master Listing",intROWLN-24));
			dosREPORT.writeBytes("Page No.    : " + cl_dat.M_PAGENO + "\n");						
			if(chkSELCT.isSelected())
			{
				dosREPORT.writeBytes(stbDOTLN.toString()+"\n");			
				dosREPORT.writeBytes(padSTRING('R',"Main Group",12) + padSTRING('R',"Sub Group",10)+"Description"+"\n");
				dosREPORT.writeBytes(strTHEAD+"\n");
				dosREPORT.writeBytes(stbDOTLN.toString()+"\n");			
			}
			else
			{
				dosREPORT.writeBytes ("---------------------------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes ("Main/Subgroup & Description \n");
				dosREPORT.writeBytes ("Code        Description              Sh.Des.         Ch.Par1  Ch.Par2     N.Par1   N.Par2  N.Const  C.Const\n");
				dosREPORT.writeBytes ("---------------------------------------------------------------------------------------------------------------\n");
			}
			cl_dat.M_intLINNO_pbst = 11;
		}		
		catch(Exception L_XE)
		{
			setMSG(L_XE,"prnHEADER");
		}
	}		
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if((input == txtFRMTP) && (txtFRMTP.getText().trim().length() == 3))
				{														
					M_strSQLQRY = "Select distinct CM_CGMTP from CO_CDMST";					
					if((txtTOMTP.getText().trim().length()!=0) || (txtFRMTP.getText().trim().length()!=0))
						M_strSQLQRY +=	" where";	
					if (txtFRMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP ='" +txtFRMTP.getText().trim() + "'";
					if((txtTOMTP.getText().trim().length()!=0) && (txtFRMTP.getText().trim().length()!=0))
						M_strSQLQRY +=	" and";
					if (txtTOMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP < '" +txtTOMTP.getText().trim() + "'";					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Main Group..",'E');
							L_rstRSSET.close();
							return false;
						}
					}
				}
				if((input == txtTOMTP) && (txtTOMTP.getText().trim().length() == 3))
				{														
					M_strSQLQRY = "Select distinct CM_CGMTP from CO_CDMST";
					if((txtTOMTP.getText().trim().length()!=0) || (txtFRMTP.getText().trim().length()!=0))
						M_strSQLQRY +=	" where";					
					if (txtTOMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP LIKE '" +txtTOMTP.getText().trim() + "%'";
					if((txtTOMTP.getText().length()!=0) && (txtFRMTP.getText().length()!=0))
						M_strSQLQRY +=	" AND ";
					if (txtFRMTP.getText().length()!=0)
						M_strSQLQRY +=	" CM_CGMTP >= '" +txtFRMTP.getText().trim() + "'";
										
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Main Group..",'E');
							L_rstRSSET.close();
							return false;
						}
					}
				}
				if((input == txtFRSTP) && (txtFRSTP.getText().trim().length() == 7))
				{														
					if(txtFRMTP.getText().trim().length()==0)
					{
						setMSG("Please Enter From Main Group First..",'E');
						return false;
					}
					setCursor(cl_dat.M_curWTSTS_pbst);    												
    				M_strSQLQRY = "Select distinct CM_CGSTP,CM_CGRDS from CO_CDMST";
					M_strSQLQRY +=	" where CM_CGMTP = '" +txtFRMTP.getText().trim() + "'";					
					if(txtFRSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP LIKE '" +txtFRSTP.getText().trim() + "%'";
					if(txtTOSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP < '" +txtTOSTP.getText().trim() + "'";					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Sub Group..",'E');
							L_rstRSSET.close();
							return false;
						}
					}
				}
				if((input == txtTOSTP) && (txtTOSTP.getText().trim().length() == 7))
				{					
					if(txtFRMTP.getText().trim().length()==0) 
					{
						setMSG("Please Enter To Main Group First..",'E');
						return false;
					}				    											
    				M_strSQLQRY = "Select CM_CGSTP,CM_CGRDS from CO_CDMST";
					M_strSQLQRY +=	" where CM_CGMTP = '" +txtTOMTP.getText().trim() + "'";					
					if(txtTOSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP LIKE '" +txtTOSTP.getText().trim() + "%'";
					if(txtFRSTP.getText().length()!=0)
						M_strSQLQRY +=	" AND CM_CGSTP > '" +txtFRSTP.getText().trim() + "'";					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Sub Group..",'E');
							L_rstRSSET.close();
							return false;
						}
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}					
}		