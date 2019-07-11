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
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.sql.ResultSet;
import javax.swing.InputVerifier;
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
public class sa_rpswl extends cl_rbase
{  										
									     /*** String for generated Report File Name*/                   
	private String strFILNM;			 /** Integer for counting the records fetched from DB.*/	                     
	private int intRECCT=0;				 /** File Output Stream for File Handleing.*/
	private FileOutputStream F_OUT;	 /** Data output Stream for generating Report File.*/
    private DataOutputStream D_OUT;	 /**String for accept Isodoc1 information */  
   
	private JComboBox cmbREPTP,cmbSFTCT,cmbMCHCT,cmbLICTP;	        /**TextField for accepting data from F1.*/	
	
	private JTextField txtSYSTP,txtSFTCD,txtSFTDS,txtSRLNO,txtLICNO,txtMACNM,txtMCHDS;
	private JLabel lblLICNO,lblSFTCD,lblMCHCD,lblSTSFL,lblLICTP;
	
	Hashtable<String,String> hstSFTCT;
	Hashtable<String,String> hstSFTCT_DS;
	Hashtable<String,String> hstMCHCT;
	Hashtable<String,String> hstLICTP;
	Hashtable<String,String> hstLICTP_DS;
	
	private JRadioButton  rdbSTSYS;
	private JRadioButton rdbSTSNO;
	private JRadioButton rdbSTSALL;
	private ButtonGroup  btgSTSFL;
	
	private JRadioButton  rdbSWLST;
	private JRadioButton rdbSWLIC;
	private ButtonGroup  btgSFTRP;
	
	private JRadioButton  rdbLICDL;
	private JRadioButton rdbLICSM;
	private ButtonGroup  btgLICRP;

	private INPVF oINPVF;

	sa_rpswl()
	{
	    super(2);
	    try
	    {			
		    setMatrix(20,20);
		    
		    cmbSFTCT = new JComboBox();
		    cmbMCHCT = new JComboBox();
		    cmbLICTP = new JComboBox();
		   
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Report Type:"),3,3,1,3,this,'L');
			add(cmbREPTP = new JComboBox(),3,6,1,4,this,'L');
			cmbREPTP.addItem("S/W on Machines");
			cmbREPTP.addItem("S/W List");
			cmbREPTP.addItem("S/W Licenses");
			
			add(new JLabel("System Type:"),3,10,1,2,this,'L');
		    add(txtSYSTP = new TxtLimit(2),3,12,1,2,this,'L');  
			
		    add(new JLabel("Machine Category:"),5,3,1,3,this,'L');
			add(cmbMCHCT = new JComboBox(),5,6,1,4,this,'L');
			
			cmbMCHCT.addItem("Select");
	 		hstMCHCT = new Hashtable<String,String>();
	 		String L_strSQLQRY2=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='HWXXMCH' and IFNULL(CMT_CODCD,'')<>'03'";
			ResultSet L_rstRSSET2= cl_dat.exeSQLQRY1(L_strSQLQRY2);
			if(L_rstRSSET2 != null)
			{
				while(L_rstRSSET2.next())
				{
					hstMCHCT.put(nvlSTRVL(L_rstRSSET2.getString("CMT_CODDS"),""),nvlSTRVL(L_rstRSSET2.getString("CMT_CODCD"),""));
					cmbMCHCT.addItem(L_rstRSSET2.getString("CMT_CODDS"));
				}
				L_rstRSSET2.close();
			}
			
			add(lblMCHCD=new JLabel("Machine Name:"),5,10,1,2,this,'L');
		    add(txtMACNM = new TxtLimit(15),5,12,1,2,this,'L');  
		    add(txtMCHDS = new TxtLimit(40),5,14,1,6,this,'L');
		    
		    add(new JLabel("Software Category:"),7,3,1,3,this,'L');
			add(cmbSFTCT = new JComboBox(),7,6,1,4,this,'L');
			
			cmbSFTCT.addItem("Select");
			hstSFTCT = new Hashtable<String,String>();
	 		hstSFTCT_DS = new Hashtable<String,String>();
			String L_strSQLQRY1= "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='SAXXSCT'  ";
			ResultSet L_rstRSSET1= cl_dat.exeSQLQRY1(L_strSQLQRY1);
			if(L_rstRSSET1 != null)
			{
				while(L_rstRSSET1.next())
				{
					hstSFTCT.put(nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),""),nvlSTRVL(L_rstRSSET1.getString("CMT_CODCD"),""));
					hstSFTCT_DS.put(nvlSTRVL(L_rstRSSET1.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET1.getString("CMT_CODDS"),""));
					cmbSFTCT.addItem(L_rstRSSET1.getString("CMT_CODDS"));
				}
				L_rstRSSET1.close();
			}
			
			add(lblSFTCD=new JLabel("Software Code:"),7,10,1,2,this,'L');
		    add(txtSFTCD = new TxtLimit(3),7,12,1,2,this,'L');  
		    add(txtSFTDS = new TxtLimit(40),7,14,1,6,this,'L');
		    add(txtSRLNO = new TxtLimit(2),7,20,1,1,this,'L');
		    
		    add(lblLICTP=new JLabel("License Type:"),9,3,1,3,this,'L');
			add(cmbLICTP = new JComboBox(),9,6,1,4,this,'L');
		    add(lblLICNO=new JLabel("License No:"),9,10,1,2,this,'L');
		    add(txtLICNO = new TxtLimit(40),9,12,1,7,this,'L');
		    
		    cmbLICTP.addItem("Select");	    
		    hstLICTP = new Hashtable<String,String>();
		    hstLICTP_DS = new Hashtable<String,String>();
			String L_strSQLQRY= "SELECT CMT_CODCD,CMT_CODDS,CMT_SHRDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP ='SAXXLTP'";
			ResultSet L_rstRSSET= cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					hstLICTP.put(L_rstRSSET.getString("CMT_CODDS"),L_rstRSSET.getString("CMT_CODCD"));
					hstLICTP_DS.put(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET.getString("CMT_SHRDS"),""));
					cmbLICTP.addItem(L_rstRSSET.getString("CMT_CODDS"));
				}
				L_rstRSSET.close();
			}
		
		    add(lblSTSFL=new JLabel("Usage Status"),11,3,1,2,this,'L');
		    add(rdbSTSYS=new JRadioButton("Yes"),11,5,1,2,this,'L');
			add(rdbSTSNO=new JRadioButton("No"),11,7,1,2,this,'L');
			add(rdbSTSALL=new JRadioButton("All"),11,9,1,2,this,'L');
			btgSTSFL=new ButtonGroup();
			btgSTSFL.add(rdbSTSYS); 
			btgSTSFL.add(rdbSTSNO); 
			btgSTSFL.add(rdbSTSALL); 
			rdbSTSYS.setSelected(true);
		    
			//rdbLICDL.setVisible(false);
			//rdbLICSM.setVisible(false);
			lblLICNO.setVisible(false);
			txtLICNO.setVisible(false);
			lblLICTP.setVisible(false);
			cmbLICTP.setVisible(false);
			lblSFTCD.setVisible(false);
			txtSFTCD.setVisible(false);
			txtSFTDS.setVisible(false);
			txtSRLNO.setVisible(false);
			
			lblMCHCD.setVisible(false);
			txtMACNM.setVisible(false);
			txtMCHDS.setVisible(false);
			
			rdbSTSYS.setVisible(false);
			rdbSTSNO.setVisible(false);
			rdbSTSALL.setVisible(false);
			lblSTSFL.setVisible(false);
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
	 		
	 		oINPVF=new INPVF();
			txtSFTCD.setInputVerifier(oINPVF);
			txtMACNM.setInputVerifier(oINPVF);
			
			
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX+"Constructor",'E');
		}	
    }
    
	/** Method for action performing 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{	
		try
		{
		    super.actionPerformed(L_AE); 
		    if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtSYSTP.setText(M_strSBSCD.substring(0,2));
					txtSYSTP.requestFocus();
					setENBL(true);
				}
				else
					setENBL(false);
			}
		    else if(M_objSOURC==cmbMCHCT)
			{
			    if(cmbREPTP.getSelectedItem().equals("S/W on Machines"))
			    {
			    	if(cmbMCHCT.getSelectedIndex()>0)
			    	{
				    	lblMCHCD.setVisible(true);
						txtMACNM.setVisible(true);
						txtMCHDS.setVisible(true);
						rdbSTSYS.setVisible(false);
						rdbSTSNO.setVisible(false);
						rdbSTSALL.setVisible(false);
						lblSTSFL.setVisible(false);
				    	txtMCHDS.setEnabled(false);
			    	}
			    }
			}
		    else if(M_objSOURC==cmbREPTP)
			{
		    	if(cmbREPTP.getSelectedIndex()>0)
			    {
		    		cmbMCHCT.setEnabled(false);
		    		lblMCHCD.setEnabled(false);
					txtMACNM.setEnabled(false);
					txtMCHDS.setEnabled(false);
					rdbSTSYS.setVisible(true);
					rdbSTSNO.setVisible(true);
					rdbSTSALL.setVisible(true);
					lblSTSFL.setVisible(true); 
			    }
		    	else
		    	{	
		    		cmbMCHCT.setEnabled(true);
		    		lblMCHCD.setEnabled(true);
					txtMACNM.setEnabled(true);
					txtMCHDS.setEnabled(true);
					rdbSTSYS.setVisible(false);
					rdbSTSNO.setVisible(false);
					rdbSTSALL.setVisible(false);
					lblSTSFL.setVisible(false); 
					lblLICNO.setVisible(true);
					txtLICNO.setVisible(true);
					cmbLICTP.setVisible(true);
					lblLICTP.setVisible(false);
		    	}
			}
		    else if(M_objSOURC==cmbSFTCT)
			{
		    	if(cmbSFTCT.getSelectedIndex()>=0)
				{
					txtSFTCD.setText(""); 
					txtSFTDS.setText(""); 
					txtSRLNO.setText(""); 
					txtLICNO.setText(""); 
					cmbLICTP.setSelectedIndex(0);
				}
				if(cmbSFTCT.getSelectedIndex()>0)
				{
					lblSFTCD.setVisible(true);
					txtSFTCD.setVisible(true);
					txtSFTDS.setVisible(true);
					txtSFTDS.setEnabled(false);
					//txtSRLNO.setEnabled(false);
					txtSRLNO.setVisible(true);
					
					lblLICNO.setVisible(true);
					txtLICNO.setVisible(true);
					cmbLICTP.setVisible(true);
					lblLICTP.setVisible(true);
				}
				else
				{
					lblSFTCD.setVisible(false);
					txtSFTCD.setVisible(false);
					txtSFTDS.setVisible(false);
					txtSRLNO.setVisible(false);
					lblLICNO.setVisible(false);
					txtLICNO.setVisible(false);
					cmbLICTP.setVisible(false);
					lblLICTP.setVisible(false);
					txtSFTCD.setText("");
					txtSFTDS.setText("");
					txtSRLNO.setText("");
					txtLICNO.setText("");
					cmbLICTP.setSelectedIndex(0);
				}
			}
		    else if(M_objSOURC==cmbLICTP)
			{
		    	if(cmbLICTP.getSelectedIndex()>=0)
				{
		    		txtLICNO.setText("");
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed"); 
		}
	}
	/** Method to request Focus in all TextField of component,when press ENTER **/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
				if(M_objSOURC== txtMACNM)
				{									
					M_strHLPFLD = "txtMACNM";
					M_strSQLQRY=" SELECT MC_MACNM,MC_SRLNO from HW_MCMST where MC_STSFL='1'" ;
					if(txtSYSTP.getText().length() >0)		
						M_strSQLQRY += " and MC_SYSTP='"+txtSYSTP.getText().trim()+"' ";
					if(cmbMCHCT.getSelectedIndex()>0)	
						M_strSQLQRY += " AND MC_MCHCT='"+hstMCHCT.get(cmbMCHCT.getSelectedItem().toString())+"' ";
					if(txtMACNM.getText().length() >0)			
						M_strSQLQRY += " and MC_MACNM like '"+txtMACNM.getText().trim()+"%'";		
					M_strSQLQRY += " order by MC_MACNM";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Machine Name","Machine Serial No"},2,"CT");			
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSFTCD)
				{
					M_strHLPFLD = "txtSFTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					M_strSQLQRY = " SELECT distinct SW_SFTCD,SW_SFTDS,SW_SRLNO from SA_SWMST where ifnull(SW_STSFL,' ') <> 'X'";
					//if(txtSYSTP.getText().length() >0)
					//	M_strSQLQRY += " and SW_SYSTP = '"+txtSYSTP.getText().trim()+"'"; 
					if(cmbSFTCT.getSelectedIndex()>0)
						M_strSQLQRY += " AND SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
					if(txtSFTCD.getText().length() >0)
						M_strSQLQRY += " AND SW_SFTCD like '"+txtSFTCD.getText().trim()+"%'";
					M_strSQLQRY += " order by SW_SFTCD";
					//System.out.println("txtSFTCD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Software Code","Description","Serial No"},3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
					
				}
				else if(M_objSOURC== txtLICNO)
				{									
					M_strHLPFLD = "txtLICNO";
					
					M_strSQLQRY = " SELECT SWT_LICNO from SA_SWTRN where SWT_SYSTP='"+txtSYSTP.getText().toString()+"' AND ifnull(SWT_STSFL,' ') <> 'X'";
					//if(txtSYSTP.getText().length() >0)
					//	M_strSQLQRY += " and SWT_SYSTP = '"+txtSYSTP.getText().trim()+"'"; 
					if(cmbSFTCT.getSelectedIndex()>0)
						M_strSQLQRY += " AND SWT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
					if(txtSFTCD.getText().length() >0)
						M_strSQLQRY += " AND SWT_SFTCD = '"+txtSFTCD.getText().trim()+"'";
					if(txtSRLNO.getText().length() >0)
						M_strSQLQRY += " AND SWT_SRLNO = '"+txtSRLNO.getText().trim()+"'";
					if(cmbLICTP.getSelectedIndex()>0)
						M_strSQLQRY += " AND SWT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
					if(txtLICNO.getText().length() >0)
						M_strSQLQRY += " and SWT_LICNO like '"+txtLICNO.getText().toString()+"%'";
					M_strSQLQRY += " order by SWT_LICNO";
					//System.out.println("txtDOCNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Licenses No"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtSYSTP)
				{
					if(cmbREPTP.getSelectedIndex()>0)
					{
						cmbSFTCT.requestFocus();
						setMSG("Select Software Category..",'N');
					}
					else
					{
						cmbMCHCT.requestFocus();
						setMSG("Select Machine Category..",'N');
					}
				}
				else if(M_objSOURC == cmbMCHCT)
				{
						txtMACNM.requestFocus();
						setMSG("Enter Machine Nmae or Press F1 to Select from List....",'N');
				}
				else if(M_objSOURC == txtMACNM)
				{
					if(txtMACNM.getText().length()==0)
						txtMCHDS.setText("");
					cmbSFTCT.requestFocus();
					setMSG("Select Software Category..",'N');
				}
				else if(M_objSOURC == cmbSFTCT)
				{
					if(cmbSFTCT.getSelectedIndex()>0)
					{
						txtSFTCD.requestFocus();
						setMSG("Enter Software Code or Press F1 to Select from List....",'N');
					}
				}
				else if(M_objSOURC == txtSFTCD)
				{
					if(txtSFTCD.getText().length()==0)
					{
						txtSFTDS.setText("");
						txtSRLNO.setText("");
						txtLICNO.setText("");
						cmbLICTP.setSelectedIndex(0);
					}
					cmbLICTP.requestFocus();
					setMSG("Select License Type..",'N');
				}
				else if(M_objSOURC == cmbLICTP)
				{
					txtLICNO.requestFocus();
					setMSG("Enter License No or Press F1 to Select from List....",'N');
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keypressed"); 
		}
	} 
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtMACNM"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtMACNM.setText(L_STRTKN.nextToken());
				 txtMCHDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtSFTCD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtSFTCD.setText(L_STRTKN.nextToken());
				 txtSFTDS.setText(L_STRTKN.nextToken());
				 txtSRLNO.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtLICNO"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtLICNO.setText(L_STRTKN.nextToken());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
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
					strFILNM = cl_dat.M_strREPSTR_pbst +"sa_rpswl.html";				
			   else if(M_rdbTEXT.isSelected())
				    strFILNM = cl_dat.M_strREPSTR_pbst + "sa_rpswl.doc";				
				cl_dat.M_PAGENO = 0;
				cl_dat.M_intLINNO_pbst = 0;	
				getDATA();
				if(intRECCT ==0)
				{
					setMSG("Data not found ..",'E');
					return;
				}
				if(D_OUT !=null)
					D_OUT.close();
				if(F_OUT !=null)
					F_OUT.close();
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
			int L_intLICQT=0;
			String L_strLICQT="";
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			String strNEW_SFTCT="", strOLD_SFTCT="",strNEW_SFTCD="",strOLD_SFTCD="",strNEW_SRLNO="",strOLD_SRLNO="",strNEW_LICNO="",strOLD_LICNO="",strNEW_LICTP="",strOLD_LICTP="";
			String L_strVENCD="",L_strSTSFL="";
			if(rdbSTSYS.isSelected())
				L_strSTSFL="Y";
			else if(rdbSTSNO.isSelected())
				L_strSTSFL="N";
			else if(rdbSTSALL.isSelected())
				L_strSTSFL="Y','N";
			
			F_OUT = new FileOutputStream(strFILNM);
			D_OUT = new DataOutputStream(F_OUT);
					
			setMSG("Report Generation in Process.......",'N');
								
			genRPHDR();
			if(cmbREPTP.getSelectedItem().equals("S/W List"))
			{
				intRECCT=0;
				M_strSQLQRY= " SELECT SW_LICTP,SW_SYSTP,SW_SFTCT,SW_SFTCD,SW_SFTDS,SW_SRLNO,SW_STSFL,SW_PROQT,SW_UTLQT";
	  			M_strSQLQRY+= " from SA_SWMST" ;
	  			M_strSQLQRY+= " where SW_STSFL in('"+L_strSTSFL+"')";
	  			//if(txtSYSTP.getText().toString().length()>0)
	  			//	M_strSQLQRY+= " and SW_SYSTP='"+txtSYSTP.getText().toString()+"'";
	  			if(cmbSFTCT.getSelectedIndex()>0)
	  				M_strSQLQRY+= " and SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
	  			if(txtSFTCD.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND SW_SFTCD = '"+txtSFTCD.getText().toString()+"'";
	  			if(txtSRLNO.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND SW_SRLNO = '"+txtSRLNO.getText()+"'";
	  			if(cmbLICTP.getSelectedIndex()>0)
					M_strSQLQRY += " AND SW_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
				M_strSQLQRY += " order by SW_SFTCT,SW_SFTCD,SW_SRLNO,SW_STSFL ";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				//System.out.println("Select>>>>"+M_strSQLQRY);
				if(M_rstRSSET !=null)
				{	
					while(M_rstRSSET.next())
					{
						strNEW_SFTCT=nvlSTRVL(M_rstRSSET.getString("SW_SFTCT"),"");
						if(!strOLD_SFTCT.equals(strNEW_SFTCT))
						{
							crtNWLIN();
							
							if(M_rdbHTML.isSelected())// for html
								D_OUT.writeBytes("<b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(D_OUT,M_strBOLD);
							D_OUT.writeBytes("\n"+padSTRING('R',hstSFTCT_DS.get(M_rstRSSET.getString("SW_SFTCT")).toString(),20));
							if(M_rdbHTML.isSelected())
								D_OUT.writeBytes("</b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(D_OUT,M_strNOBOLD);
							crtNWLIN();
							
						}
						strOLD_SFTCT=nvlSTRVL(M_rstRSSET.getString("SW_SFTCT"),"");
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_SFTCD"),3));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_SFTDS")+"("+M_rstRSSET.getString("SW_SRLNO")+")",44));
						//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_STSFL"),18));
						
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(hstLICTP_DS.get(M_rstRSSET.getString("SW_LICTP")),"-"),18));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SW_PROQT"),"-"),3));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SW_UTLQT"),"-"),16));
						
						L_intLICQT=M_rstRSSET.getInt("SW_PROQT")-M_rstRSSET.getInt("SW_UTLQT");
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(String.valueOf(L_intLICQT),"-"),11));
						
						intRECCT++;
					}
				}
				crtNWLIN();
				M_rstRSSET.close();
				
			}
			else if(cmbREPTP.getSelectedItem().equals("S/W Licenses"))
			{
				intRECCT=0;
				M_strSQLQRY= " SELECT SWT_SYSTP,SWT_SFTCT,SWT_SFTCD,SWT_SRLNO,SWT_STSFL,SWT_VENCD,SWT_LICDT,SWT_LICTP,SWT_LICNO,SWT_LICQT,SWT_LICDS,SW_SFTDS";
	  			M_strSQLQRY+= " from SA_SWTRN,SA_SWMST" ;
	  			M_strSQLQRY+= " where SWT_STSFL in('"+L_strSTSFL+"') AND SWT_SFTCT=SW_SFTCT AND SWT_SFTCD=SW_SFTCD and SWT_SRLNO=SW_SRLNO and SWT_LICTP=SW_LICTP";
	  			
	  			if(txtSYSTP.getText().toString().length()>0)
	  				M_strSQLQRY+= " and SWT_SYSTP='"+txtSYSTP.getText().toString()+"'";
	  			if(cmbSFTCT.getSelectedIndex()>0)
	  				M_strSQLQRY+= " and SWT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
	  			if(txtSFTCD.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND SWT_SFTCD = '"+txtSFTCD.getText().toString()+"'";
	  			if(txtSRLNO.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND SW_SRLNO = '"+txtSRLNO.getText()+"'";
	  			if(txtLICNO.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND SWT_LICNO = '"+txtLICNO.getText().toString()+"'";
	  			if(cmbLICTP.getSelectedIndex()>0)
					M_strSQLQRY += " AND SWT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
	  		
				M_strSQLQRY += "order by SWT_SFTCT,SWT_SFTCD,SWT_SRLNO,SWT_STSFL ";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				//System.out.println("Select>>>>"+M_strSQLQRY);
				if(M_rstRSSET !=null)
				{	
					while(M_rstRSSET.next())
					{
						L_strVENCD=nvlSTRVL(M_rstRSSET.getString("SWT_VENCD"),"");
						strNEW_SFTCT=nvlSTRVL(M_rstRSSET.getString("SWT_SFTCT"),"");
						strNEW_SFTCD=nvlSTRVL(M_rstRSSET.getString("SWT_SFTCD"),"");
						strNEW_SRLNO=nvlSTRVL(M_rstRSSET.getString("SWT_SRLNO"),"");
						strNEW_LICTP=nvlSTRVL(M_rstRSSET.getString("SWT_LICTP"),"");
						if(!strOLD_SFTCT.equals(strNEW_SFTCT))
						{
							crtNWLIN();
							
							if(M_rdbHTML.isSelected())// for html
								D_OUT.writeBytes("<b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(D_OUT,M_strBOLD);
							D_OUT.writeBytes("\n"+padSTRING('R',hstSFTCT_DS.get(M_rstRSSET.getString("SWT_SFTCT")).toString(),20));
							if(M_rdbHTML.isSelected())
								D_OUT.writeBytes("</b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(D_OUT,M_strNOBOLD);
							crtNWLIN();
						}
						crtNWLIN();
						
						if(!strOLD_SFTCT.equals(strNEW_SFTCT) || !strOLD_SFTCD.equals(strNEW_SFTCD) || !strOLD_SRLNO.equals(strNEW_SRLNO))
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SWT_SFTCD"),3));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_SFTDS")+"("+M_rstRSSET.getString("SWT_SRLNO")+")",46));
							//D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SWT_STSFL"),8));
						}
						else
						{
							D_OUT.writeBytes(padSTRING('R',"",3));
							D_OUT.writeBytes(padSTRING('R',"",46));
							//D_OUT.writeBytes(padSTRING('R',"",5));
							//D_OUT.writeBytes(padSTRING('R',"",8));
						}
						strOLD_SFTCT=nvlSTRVL(M_rstRSSET.getString("SWT_SFTCT"),"");
						strOLD_SFTCD=nvlSTRVL(M_rstRSSET.getString("SWT_SFTCD"),"");
						strOLD_SRLNO=nvlSTRVL(M_rstRSSET.getString("SWT_SRLNO"),"");
						if(!strOLD_LICTP.equals(strNEW_LICTP) )
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(hstLICTP_DS.get(M_rstRSSET.getString("SWT_LICTP")),""),9));
						else
							D_OUT.writeBytes(padSTRING('R',"",9));
						strOLD_LICTP=nvlSTRVL(M_rstRSSET.getString("SWT_LICTP"),"");
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SWT_LICNO"),""),40));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SWT_LICQT"),"-"),4));
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getDate("SWT_LICDT")!=null?M_fmtLCDAT.format(M_rstRSSET.getDate("SWT_LICDT")):"-",14));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SWT_LICDS"),"-"),35));
						
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SWT_VENCD"),"-"),10));
						intRECCT++;
					}
					/*if(L_strVENCD.length()>0)
		            {
			            String L_strSQLQRY= "Select PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST where PT_PRTTP='S' ";
			            L_strSQLQRY+= "  AND ifnull(PT_STSFL,'')<>'X' AND PT_PRTCD='"+L_strVENCD+"' ";
			            ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY); 
						if(L_rstRSSET != null &&  L_rstRSSET.next())
						{
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),9));
							
						}
						 L_rstRSSET.close();
		            }*/
				}
				M_rstRSSET.close();
			}
			else if(cmbREPTP.getSelectedItem().equals("S/W on Machines"))
			{
				intRECCT=0;
				int L_intTOTMC=1;
				String strNEW_MACNM="", strOLD_MACNM="";
				
				M_strSQLQRY = "select MCT_SRLNO,MCT_SFTCT,MCT_SFTCD,MCT_SWSRL,MCT_LICTP,MCT_LICNO,MC_MACNM,LC_LOCDS,LC_IPADD,SW_SFTDS";
				M_strSQLQRY+= " from HW_MCTRN,HW_MCMST,SA_SWMST,HW_LCMST";
				M_strSQLQRY+= " where MCT_STSFL='1' AND MCT_STSFL=MC_STSFL and MCT_SYSTP=MC_SYSTP AND MCT_MCHCT=MC_MCHCT AND MCT_SRLNO=MC_SRLNO AND MCT_SYSTP=LC_SYSTP AND MCT_LICTP=SW_LICTP AND MC_LOCCD=LC_LOCCD ";
				M_strSQLQRY+= " and MCT_SFTCT=SW_SFTCT AND MCT_SFTCD=SW_SFTCD AND MCT_SWSRL=SW_SRLNO ";
				if(txtSYSTP.getText().toString().length()>0)
	  				M_strSQLQRY+= " and MCT_SYSTP='"+txtSYSTP.getText().toString()+"'";
				if(cmbMCHCT.getSelectedIndex()>0)
	  				M_strSQLQRY+= " and MCT_MCHCT='"+hstMCHCT.get(cmbMCHCT.getSelectedItem().toString())+"'";
				if(txtMACNM.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND MC_MACNM = '"+txtMACNM.getText().toString()+"'";
	  			if(cmbSFTCT.getSelectedIndex()>0)
	  				M_strSQLQRY+= " and MCT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
	  			if(txtSFTCD.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND MCT_SFTCD = '"+txtSFTCD.getText().toString()+"'";
	  			if(txtSRLNO.getText().toString().length()>0)
	  				M_strSQLQRY+= "  AND MCT_SWSRL= '"+txtSRLNO.getText()+"'";
	  			if(cmbLICTP.getSelectedIndex()>0)
					M_strSQLQRY+= " AND MCT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
	  			if(txtLICNO.getText().toString().length()>0)
	  				M_strSQLQRY+= " AND MCT_LICNO= '"+txtLICNO.getText()+"'";
	  			M_strSQLQRY += "order by MCT_SFTCT,MCT_SFTCD,MCT_SWSRL,MCT_LICNO,MC_MACNM";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				//System.out.println("Select>>>>"+M_strSQLQRY);
				if(M_rstRSSET !=null)
				{	
					while(M_rstRSSET.next())
					{
						strNEW_SFTCT=nvlSTRVL(M_rstRSSET.getString("MCT_SFTCT"),"");
						strNEW_SFTCD=nvlSTRVL(M_rstRSSET.getString("MCT_SFTCD"),"");
						strNEW_SRLNO=nvlSTRVL(M_rstRSSET.getString("MCT_SWSRL"),"");
						strNEW_LICTP=nvlSTRVL(M_rstRSSET.getString("MCT_LICTP"),"");
						strNEW_LICNO=nvlSTRVL(M_rstRSSET.getString("MCT_LICNO"),"");
						strNEW_MACNM=nvlSTRVL(M_rstRSSET.getString("MCT_SRLNO"),"");
						if(!strOLD_SFTCT.equals(strNEW_SFTCT))
						{
							crtNWLIN();
							if(M_rdbHTML.isSelected())// for html
								D_OUT.writeBytes("<b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(D_OUT,M_strBOLD);
							D_OUT.writeBytes("\n"+padSTRING('R',hstSFTCT_DS.get(M_rstRSSET.getString("MCT_SFTCT")).toString(),20));
							if(M_rdbHTML.isSelected())
								D_OUT.writeBytes("</b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(D_OUT,M_strNOBOLD);
						
							crtNWLIN();
						}
						if(!strOLD_SFTCD.equals(strNEW_SFTCD) || !strOLD_SRLNO.equals(strNEW_SRLNO))
						{
							/*if(!strOLD_SFTCD.equals("") && cmbSFTCT.getSelectedIndex()>0)
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('L',"",72));	
								if(M_rdbHTML.isSelected())// for html
									D_OUT.writeBytes("<b>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(D_OUT,M_strBOLD);
								D_OUT.writeBytes(padSTRING('L',"Total Machines    :"+L_intTOTMC ,30));
								crtNWLIN();
								if(M_rdbHTML.isSelected())
									D_OUT.writeBytes("</b>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(D_OUT,M_strNOBOLD);
							}
							L_intTOTMC=1;*/
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("MCT_SFTCD"),3));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_SFTDS")+"("+M_rstRSSET.getString("MCT_SWSRL")+")",44));
						}
						else
						{
							D_OUT.writeBytes(padSTRING('R',"",3));
							D_OUT.writeBytes(padSTRING('R',"",44));
						}
						//if(!strOLD_LICTP.equals(strNEW_LICTP) )
						
						//else
						//		D_OUT.writeBytes(padSTRING('R',"",9));
						if(!strOLD_LICNO.equals(strNEW_LICNO) )
						{
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(hstLICTP_DS.get(M_rstRSSET.getString("MCT_LICTP")),"-"),9));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MCT_LICNO"),"-"),35));
						}
						else
						{
							D_OUT.writeBytes(padSTRING('R',"",9));
							D_OUT.writeBytes(padSTRING('R',"",35));
						}
						
						strOLD_SFTCT=nvlSTRVL(M_rstRSSET.getString("MCT_SFTCT"),"");
						strOLD_SFTCD=nvlSTRVL(M_rstRSSET.getString("MCT_SFTCD"),"");
						strOLD_SRLNO=nvlSTRVL(M_rstRSSET.getString("MCT_SWSRL"),"");
						strOLD_LICTP=nvlSTRVL(M_rstRSSET.getString("MCT_LICTP"),"");
						strOLD_LICNO=nvlSTRVL(M_rstRSSET.getString("MCT_LICNO"),"");
						
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("MC_MACNM"),15));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("LC_IPADD"),"-"),17));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("LC_LOCDS"),"-"),20));
						crtNWLIN();
						intRECCT++;
						
					}
					if(cmbSFTCT.getSelectedItem().equals("Operating System") || txtSFTCD.getText().length()>0)
					{
						D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------");
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('L',"",73));	
						if(M_rdbHTML.isSelected())// for html
							D_OUT.writeBytes("<b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(D_OUT,M_strBOLD);
						D_OUT.writeBytes(padSTRING('L',"Total Machines    :"+intRECCT ,30));
						if(M_rdbHTML.isSelected())
							D_OUT.writeBytes("</b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(D_OUT,M_strNOBOLD);
					}
				}
				M_rstRSSET.close();
			}
			genRPFTR();
			setMSG("Report completed.. ",'N');			
			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
        catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		
	}	
    boolean vldDATA()
	{
		try
		{
		}
        catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
        return true;
	}	
		
	private void genRPHDR()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
				
			if(M_rdbHTML.isSelected())
			{
				//D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size :9 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	
			cl_dat.M_PAGENO +=1;
			
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		if(cmbREPTP.getSelectedItem().equals("S/W on Machines"))
    		{
    			D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,100));
				D_OUT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");	
				if(cmbMCHCT.getSelectedIndex()==0)
					D_OUT.writeBytes(padSTRING('R',"Install Software on Machines",100));
				else
					D_OUT.writeBytes(padSTRING('R',"Install Software on "+cmbMCHCT.getSelectedItem(),100));
				D_OUT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
				if(txtSFTCD.getText().length()>0)
	    			D_OUT.writeBytes(padSTRING('R',txtSFTDS.getText().toString(),80)+"\n");
				D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------\n");						
				D_OUT.writeBytes("Software Code/Name/SRL No                      Lic.Type License No.                        Machine Name   IP Address       Location         \n");					
				D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------\n");
    		}
			else if(cmbREPTP.getSelectedItem().equals("S/W Licenses"))
			{
				
				D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,100));
				D_OUT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
				D_OUT.writeBytes(padSTRING('R',"List of Software Licences ",100));
				D_OUT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
				if(txtSFTCD.getText().length()>0)
	    			D_OUT.writeBytes(padSTRING('R',txtSFTDS.getText().toString(),80)+"\n");
				D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------------------\n");						
				D_OUT.writeBytes("Software Code/Name/SRL No    			       Lic.Type License No.                       Lic.Qty   Lic.Date      Lic.Purpose                        Vendor     \n");					
				D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			}
    		else if(cmbREPTP.getSelectedItem().equals("S/W List"))
			{
				D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,80));
				D_OUT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
				D_OUT.writeBytes(padSTRING('R',"Software List ",80));
				D_OUT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
				if(txtSFTCD.getText().length()>0)
	    			D_OUT.writeBytes(padSTRING('R',txtSFTDS.getText().toString(),80)+"\n");
				D_OUT.writeBytes("-----------------------------------------------------------------------------------------------\n");						
				D_OUT.writeBytes("Software Code/Name/SRL No  			     Lic.Type  Procured Lic 	Utilized Lic.   Bal.Lic. \n");					
				D_OUT.writeBytes("-----------------------------------------------------------------------------------------------\n");
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genRPHDR");
		}		
	}	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			
			if(cmbREPTP.getSelectedItem().equals("S/W on Machines"))
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------");
			else  if(cmbREPTP.getSelectedItem().equals("S/W Licenses"))
				D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
			else if(cmbREPTP.getSelectedItem().equals("S/W List"))
				D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
			
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >68)
				{
					genRPFTR();
					genRPHDR();
				}
			}	
			else if(cl_dat.M_intLINNO_pbst >52)
			{		
				genRPFTR();
				genRPHDR();			
			}			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

	/** Validate Data by user*/
	class INPVF extends InputVerifier 
	{
		String L_strSFTCD="",L_strSFTCT="",L_strSRLNO="";
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;
				if(input == txtMACNM)
				{
					M_strSQLQRY=" SELECT MC_SRLNO,MC_MACNM from HW_MCMST where MC_STSFL='1' and MC_SYSTP ='"+ txtSYSTP.getText().trim()+"' and MC_MCHCT ='"+hstSFTCT.get(cmbSFTCT.getSelectedItem()).toString()+"'";
					M_strSQLQRY += "  AND MC_MACNM='"+ txtMACNM.getText().trim()+"' ";	
					//System.out.println("M_strSQLQRY"+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null && M_rstRSSET.next())
					{
						txtMACNM.setText(M_rstRSSET.getString("MC_MACNM"));
						txtMCHDS.setText(M_rstRSSET.getString("MC_SRLNO"));
					}	
					else 
					{
						setMSG("Enter Valid Machine Name..",'E');
						return false;
					}	
				}
			/*	else if(input == txtSFTCD)
				{
					if(txtSFTCD.getText().length()>0)
					{
						M_strSQLQRY = " SELECT distinct SW_SFTCD,SW_SFTDS,SW_SRLNO,SW_SFTCT from SA_SWMST where ifnull(SW_STSFL,' ') <> 'X'";
						//if(txtSYSTP.getText().length() >0)
						//	M_strSQLQRY += " and SW_SYSTP = '"+txtSYSTP.getText().trim()+"'"; 
						if(cmbSFTCT.getSelectedIndex()>0)
							M_strSQLQRY += " AND SW_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
						M_strSQLQRY += " and SW_SFTCD='"+txtSFTCD.getText()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						//System.out.println("SFTCD"+M_strSQLQRY);
						if(M_rstRSSET != null && M_rstRSSET.next())
						{
							txtSFTCD.setText(nvlSTRVL(M_rstRSSET.getString("SW_SFTCD"),""));
							txtSFTDS.setText(M_rstRSSET.getString("SW_SFTDS"));
							//txtSRLNO.setText(nvlSTRVL(M_rstRSSET.getString("SW_SRLNO"),""));	
						}	
						else 
						{
							setMSG("Enter Valid Software Code..",'E');
							return false;
						}
					}
				}*/
				else if(input == txtLICNO)
				{
					if(txtLICNO.getText().length()>0)
					{
						M_strSQLQRY=" SELECT SWT_LICNO FROM SA_SWTRN where SWT_SYSTP ='"+txtSYSTP.getText().toString()+"'";
						//if(txtSYSTP.getText().length() >0)
						//	M_strSQLQRY += " and SWT_SYSTP = '"+txtSYSTP.getText().trim()+"'"; 
						if(cmbSFTCT.getSelectedIndex()>0)
							M_strSQLQRY += " AND SWT_SFTCT='"+hstSFTCT.get(cmbSFTCT.getSelectedItem().toString())+"'";
						if(txtSFTCD.getText().length() >0)
							M_strSQLQRY += " AND SWT_SFTCD = '"+txtSFTCD.getText().trim()+"'";
						if(txtSRLNO.getText().length() >0)
							M_strSQLQRY += " AND SWT_SRLNO = '"+txtSRLNO.getText().trim()+"'";
						if(cmbLICTP.getSelectedIndex()>0)
							M_strSQLQRY += " AND SWT_LICTP='"+hstLICTP.get(cmbLICTP.getSelectedItem().toString())+"'";
						M_strSQLQRY += " and SWT_LICNO ='"+txtLICNO.getText().toString()+"%'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null && M_rstRSSET.next())
						{
							txtLICNO.setText(M_rstRSSET.getString("SWT_LICNO"));
							setMSG("",'N');
						}	
						else 
						{
							setMSG("Enter Valid License No..",'E');
							return false;
						}
					}
				}
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"class INPVF");		
			}
			return true;
		}
	}
}