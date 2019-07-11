/*
System Name   : Material Management System
Program Name  : Product Specification Sheet 
Program Desc. : 
Author        : 
Date          : 07/10/2005"
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :16/03/06
Modified det.  :
Version        :
*/
import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import javax.swing.JPanel;

/**<pre>
System Name : Material Management System.
 
Program Name : Product Specification Sheet

Purpose : This program generates Report for Product Specification Sheet .

List of tables used :
Table Name              Primary key                                             Operation done
                                                                   Insert   Update   Query   Delete	
---------------------------------------------------------------------------------------------------
CO_QPMST   QP_QCATP,QP_TSTTP,QP_PRDCD,QP_QPRCD,QP_STRDT,QP_SRLNO                        #
CO_PRMST                    PR_PRDCD													#
CO_PTMST				    PT_PRTTP,PT_PRTCD											#
---------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtPRDCD   PR_PRDCD                  CO_PRMST           varchar(10)  Product code
txtPRDDS   PR_PRDDS                  CO_PRMST           varchar(50)  Product Description 
txtPRTCD   QP_SRLNO                  CO_QPMST           varchar(5)   Party code
txtPRTNM   PT_PRTNM                  CO_PTMST           varchar(50)  Party Name
txtFRDAT   QP_STRDT                  CO_QPMST			Data         Start date
txtTODAT   QP_ENDDT					 CO_QPMST           Date         End date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
    For Product Specification Sheet Report Product Type  is Taken from  CO_CDTRN 
         1) CMT_CGMTP ='MST'
         2) AND CMT_CGSTP = 'COXXPRD'COXXPRD''
         3) AND isnull(CMT_STSFL,'') <> 'X'

For product Speciation sheet Report  Product Code And Description is taken from CO_PRMST
"Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_PRDTP ='"+L_strPRDTP+"' AND isnull(PR_PRDCD,'') <>'X'";
       1) PR_PRDTP = Selected Product Type
       2) AND isnull (PR_PRDCD,'') <> 'X'

For generating Report Data is taken FROM CO_QPMST 

    Condition : 1)QP_PRDCD = given producd Code
                2)AND isnull(QP_PRDCD,'') <> 'X'  
    if Current Specification is Selected

		Condition:  1) And QP_SRLNO=Given party Code
			        2) AND QP_ENDDT is null ";

	if Range is selected
		Condition: 1) And QP_SRLNO=Given party Code
			       2) AND QP_STRDT=Given Start Date
                   3) AND QP_ENDDT= is not null
			
<B>Validations & Other Information:</B>    
     -  Product code specifies must be valid
     -  Date Should be valid
     -  From date must  be less then To Date
</I> */

class qc_rpspc extends cl_rbase
{								/** JComboBox to display & Select Product Type.*/
	private JComboBox cmbPRDTP;	    
	private ButtonGroup btgSPECF;    /**JRedioButton to  Current Specification Report.*/
	private JRadioButton rdbCURSP;	 /**JRedioButton to  Range Specified  Report.*/
	private JRadioButton rdbRNGSP;	 /**JRadioButton to Display all Record */
	private JRadioButton rdbHSTSP;	 /** JtextField to  enter From Date */
	private JTextField txtFMDAT;     /** JtextField to  enter To Date */
	private JTextField txtTODAT;     /** JtextField to  enter Product Code to generate the Report.*/  
	private JTextField txtPRDCD;     /** JtextField for Showing the description of Product.*/
	private JTextField txtPRDDS;     /** JtextField for Showing the code of Party*/
	private JTextField txtPRTCD;     /** JtextField for Showing the Name of party*/
	private JTextField txtPRTNM;     /**JLabel For Showing from Date  */
	private JLabel lblFRDAT;         /**JLabel for Showing To Date    */
	private JLabel lblTODAT;		 /** String variable for Specification date*/
	private String strSPDAT;		 /** String variable for Product Code.*/
	private String strPRTCD;		 /** String Variable for generated Report file Name.*/ 
	private String strFILNM;	     /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;		     /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ; 
	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	qc_rpspc()
	{
		super(2);
		try
		{
			cmbPRDTP = new JComboBox();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='MST'"
				+ " AND CMT_CGSTP = 'COXXPRD' AND isnull(CMT_STSFL,'')<> 'X' "  ;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET!=null)
			{
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP  =  nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  ");
					L_strTEMP +=  "   ";
					L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbPRDTP.addItem(L_strTEMP);	
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			setMatrix(20,8);
			btgSPECF=new ButtonGroup();
			add(new JLabel("Product Type"),4,3,1,1,this,'L');
			add(cmbPRDTP,4,4,1,1.5,this,'L');
			add(new JLabel("Product Code "),5,3,1,1,this,'L');
			add(txtPRDCD = new TxtLimit(10),5,4,1,1.5,this,'L');
			add(new JLabel("Description "),6,3,1,1,this,'L');
			add(txtPRDDS = new TxtLimit(30),6,4,1,1.5,this,'L');
			add(new JLabel("Party Code "),7,3,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(30),7,4,1,1.5,this,'L');
			add(new JLabel("Party Name "),8,3,1,1,this,'L'); 
			add(txtPRTNM = new TxtLimit(30),8,4,1,1.5,this,'L');
			add(rdbCURSP=new JRadioButton("Current Spec",true),9,3,1,2,this,'L');
			add(rdbRNGSP=new JRadioButton("Range"),9,5,1,1,this,'L');
			add(rdbHSTSP=new JRadioButton("History"),9,6,1,1,this,'L');
			btgSPECF.add(rdbCURSP);
			btgSPECF.add(rdbRNGSP);
			btgSPECF.add(rdbHSTSP);
			add(lblFRDAT = new JLabel("From Date "),10,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),10,4,1,1,this,'L');
			add(lblTODAT = new JLabel("TO Date "),10,5,1,1,this,'L');
			add(txtTODAT = new TxtDate(),10,6,1,1,this,'L');
			lblFRDAT.setVisible(false);
			lblTODAT.setVisible(false);
			txtFMDAT.setVisible(false);
			txtTODAT.setVisible(false);
			
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
			if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)	
			{
				if(rdbRNGSP.isSelected())
				{
					rdbCURSP.setSelected(true);
				}
			}
		}	
		if(M_objSOURC==cmbPRDTP)
		{
			setMSG("Select Product Type..",'N');
			txtPRDCD.setText("");
			txtPRDDS.setText("");
			txtPRTCD.setText("00000");
			txtPRTNM.setText("");
		}
		if(rdbHSTSP.isSelected())
		{
			txtPRTCD.setEnabled(false);
			txtPRTNM.setEnabled(false);
		}
		else
		{
			txtPRTCD.setEnabled(true);
		}	
		if(rdbRNGSP.isSelected())
		{
		    lblFRDAT.setVisible(true);
			lblTODAT.setVisible(true);
			txtFMDAT.setVisible(true);
			txtTODAT.setVisible(true);			
		}
		else
		{
			lblFRDAT.setVisible(false);
			lblTODAT.setVisible(false);
			txtFMDAT.setVisible(false);
			txtTODAT.setVisible(false);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		intRECCT=0;
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode()==L_KE.VK_F1)
		{
			if(M_objSOURC == txtPRDCD)
			{
				M_strHLPFLD = "txtPRDCD";
				String L_strPRDTP =(String)cmbPRDTP.getSelectedItem();
				L_strPRDTP=L_strPRDTP.substring(0,2);
				
				M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_PRDTP ='"+L_strPRDTP+"' AND isnull(PR_PRDCD,'') <>'X'";
				if(txtPRDCD.getText().trim().length()>0)
					M_strSQLQRY += " AND PR_PRDCD like  '"+txtPRDCD.getText().trim() +"%'";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Prod Code","Prod Desc"},2,"CT");
			}
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD="txtPRTCD";
				
				M_strSQLQRY="Select distinct QP_SRLNO from CO_QPMST where QP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND length(QP_SRLNO)= 5 AND QP_PRDCD= '"+txtPRDCD.getText().trim()+"'";
				
				if(txtPRTCD.getText().trim().length()>0)
				{
					M_strSQLQRY += "AND QP_SRLNO LIKE '"+txtPRTCD.getText().trim()+"%'"; 
				}
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Party Code"},1,"CT");
			}
			if(M_objSOURC==txtFMDAT)
			{
				M_strHLPFLD="txtFMDAT";
				M_strSQLQRY="Select distinct QP_STRDT,QP_ENDDT from CO_QPMST where QP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QP_PRDCD='"+txtPRDCD.getText().trim()+ "' AND QP_SRLNO ='" +txtPRTCD.getText().trim()+"' AND QP_ENDDT is not null ";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Start Date", "End Date"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				setMSG("Select Product Type..",'N');
				cmbPRDTP.requestFocus();	
			}
			if(M_objSOURC == cmbPRDTP)
			{
				txtPRDCD.setText("");
				txtPRDDS.setText("");
				setMSG("Enter Product Code..",'N');
				txtPRDCD.requestFocus();
			}
			if(M_objSOURC == txtPRDCD)
			{
				setMSG("Enter Party Code..",'N');
				if(txtPRDCD.getText().trim().length() != 10)
				{
					setMSG("Enter Product Code Or Press F1 For Help..",'E');
					txtPRDCD.requestFocus();
					return ;
				}
				if(txtPRDCD.getText().trim().length() == 10)
				{
					try
					{
						M_strSQLQRY="Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+txtPRDCD.getText()+"' AND isnull(PR_STSFL,'')<> 'X'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
						if(M_rstRSSET!=null)
						{
							txtPRDDS.setEnabled(false);
							if(M_rstRSSET.next())
							{
								String L_strPRDDS= nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"  ");
								txtPRDDS.setText(L_strPRDDS);	
							}
							else
							{
								setMSG("Enter Proper Product Code Or Press F1 For Help..",'E');
								txtPRDCD.requestFocus();
							}				
							if(M_rstRSSET != null)
								M_rstRSSET.close();
						}
						
						if(rdbHSTSP.isSelected())
							cl_dat.M_btnSAVE_pbst.requestFocus();
						else						
						txtPRTCD.requestFocus();
					}
					catch(Exception E_KP)
					{
						setMSG(E_KP,"getKEPRESS");
					}
				}
				else
				{
					txtPRDCD.requestFocus();
					setMSG("Enter Product Code",'N');
				}
			}
			if(M_objSOURC==txtPRTCD)
			{
				setMSG("Enter Party Name..",'N');
				txtFMDAT.setText("");
				txtTODAT.setText("");			
				if(txtPRTCD.getText().trim().length()!=5)
				{
					setMSG("Enter Party Code Or Press F1 For Help..",'E');
					txtPRTCD.requestFocus();
					return ;
				}
				else
				{
					try
					{
						txtPRTNM.setText("");
						int i=0;
						M_strSQLQRY="select PT_PRTNM  from CO_PTMST where PT_PRTCD='"+txtPRTCD.getText().trim()+"' AND PT_PRTTP='C' AND isnull(PT_STSFL,'')<> 'X'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET!=null)
						{
							if(M_rstRSSET.next())
							{
								txtPRTNM.setText(M_rstRSSET.getString("PT_PRTNM"));
							}
						}
						if(M_rstRSSET!=null)
							M_rstRSSET.close();
					}
					catch(Exception E)
					{
						setMSG(E," GetPRTNM");
					}
					if(rdbRNGSP.isSelected())
					{
						setMSG("Enter Start Date..",'N');
						txtFMDAT.requestFocus();
					}
					else
						cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
			if(M_objSOURC==txtPRTNM)
			{
				if(rdbRNGSP.isSelected())
				{
					setMSG("Enter Start Date..",'N');
					txtFMDAT.requestFocus();
				}
				else
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			if(M_objSOURC==txtFMDAT)
			{
				setMSG("Enter End Date..",'N');
				txtTODAT.requestFocus();
			}
			if(M_objSOURC==txtTODAT)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}		
		}
	}
	/**
	 * Supper Class method overrided to execuate the F1 Help for selected Component.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPRDCD")
		{
			txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtPRTCD")
		{
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtFMDAT")
		{
			txtFMDAT.setText(cl_dat.M_strHLPSTR_pbst);
			txtTODAT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
	}	
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
			if(txtPRDCD.getText().trim().length() != 10)
			{
				setMSG("Enter Product Code Or Press F1 For Help..",'E');
				txtPRDCD.requestFocus();
				return false;
			}
			if(txtPRDDS.getText().trim().length()==0)
			{
				setMSG("Enter Product Description Or Press F1 For Help..",'E');
				txtPRDCD.requestFocus();
				return false;
			}
			if(txtPRTCD.getText().trim().length()==0)
			{
				setMSG("Please Enter Party Code or press F1 for Help..",'E');
				txtPRTCD.requestFocus();
				return false;
			}
			if(rdbRNGSP.isSelected())
			{
				if(txtFMDAT.getText().trim().length()==0)
				{
					setMSG("Please Enter From Date or press F1 for Help..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				if(txtTODAT.getText().trim().length()==0)
				{
					setMSG("Please Enter To Date or press F1 for Help..",'E');
					txtTODAT.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date can not be greater than today's date..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
				{
					setMSG("To Date can not be Less than From Date ..",'E');
					txtTODAT.requestFocus();
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
 * Method to generate the Report & send to the selected Destination.
*/
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rpspc.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpspc.doc";
			getDATA();
			fosREPORT.close();
			dosREPORT.close();
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,cmbPRDTP.getSelectedItem().toString()," ");				
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
	 * Method to fetch data from the database & to club it with header & footer.
	*/
	public void getDATA()
	{		
		try
		{
			java.sql.Date jdtTEMP=null;
			intRECCT = 0;		
			cl_dat.M_PAGENO = 1;
			String L_strSTRDT="";
			String L_strENDDT="";
			String L_strPRTDT="";
			String L_strPRTCD="";
			ResultSet L_rstRSSET;
			String L_strSQLQRY="";
			String L_strPRTNM="";
			cl_dat.M_intLINNO_pbst = 0;
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Product Specification Sheet</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			prnHEADER();
			M_strSQLQRY = "SELECT distinct QP_QPRCD,QP_UOMDS,QP_QPRDS,QP_TSMDS,QP_NPFVL,QP_NPTVL,QP_STRDT,QP_ENDDT,QP_SRLNO,QP_ORDBY FROM CO_QPMST WHERE  QP_PRDCD ='"+txtPRDCD.getText()+"' ";
			if(rdbCURSP.isSelected())
			{
				M_strSQLQRY +=" And QP_SRLNO='"+txtPRTCD.getText().trim()+"' AND QP_ENDDT is null ";
			}
			else if(rdbRNGSP.isSelected())
			{
				String L_strFMDAT = txtFMDAT.getText().trim();
				String L_strTODAT = txtTODAT.getText().trim();
				M_strSQLQRY += " And QP_SRLNO='"+txtPRTCD.getText().trim()+"' AND QP_STRDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+ "' AND QP_ENDDT is not null";
			}
			M_strSQLQRY += " AND isnull(QP_PRDCD,'') <> 'X' order by QP_STRDT,QP_SRLNO,QP_ORDBY ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				intRECCT = 0;
				double L_dblNPFVL;
				double L_dblNPTVL;
				String L_intSPECF="";
				String L_strNPFVL="";
				String L_strNPTVL="";
				int L_intNPFVL;
				int L_intNPTVL;
				while(M_rstRSSET.next())
				{								
					if(cl_dat.M_intLINNO_pbst >56)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO +=1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}					
					L_strNPFVL=nvlSTRVL(M_rstRSSET.getString("QP_NPFVL"),"null");//M_rstRSSET.getString("QP_NPFVL");
					L_strNPTVL=nvlSTRVL(M_rstRSSET.getString("QP_NPTVL"),"null");//M_rstRSSET.getString("QP_NPTVL");
					if(L_strNPFVL=="null")
					{
						L_strNPFVL="null";
						L_intSPECF="-";
					}
					if(L_strNPTVL=="null")
					{
						L_strNPTVL="null";
						L_intSPECF="-";
					}
					if(L_strNPFVL=="null" && L_strNPTVL!="null")
				    {
						L_dblNPFVL=Double.parseDouble(L_strNPFVL);
						L_intNPFVL=(int)L_dblNPFVL;
						L_intSPECF =L_intNPFVL+" - __";
					}
					if(L_strNPFVL!="null" && L_strNPTVL=="null")
				    {
						L_dblNPTVL=Double.parseDouble(L_strNPTVL);
						L_intNPTVL=(int)L_dblNPTVL;
						L_intSPECF ="__ - "+ L_intNPTVL;
					}
					if(L_strNPFVL!="null" && L_strNPTVL!="null")
					{
						 L_dblNPFVL=Double.parseDouble(L_strNPFVL);
						 L_dblNPTVL=Double.parseDouble(L_strNPTVL);
						 L_intNPFVL=(int)L_dblNPFVL;
						 L_intNPTVL=(int)L_dblNPTVL;
						 if(L_intNPFVL==0 && L_intNPTVL!=0)
							 L_intSPECF=L_intNPTVL+" max";
						 if(L_intNPFVL!=0 && L_intNPTVL==0)
							 L_intSPECF =L_intNPFVL+" min";
						 if(L_intNPFVL!=0 && L_intNPTVL!=0)
						  	 L_intSPECF =L_intNPFVL+" - "+ L_intNPTVL;
					}
					if(rdbHSTSP.isSelected())
					{ 
						jdtTEMP=M_rstRSSET.getDate("QP_STRDT");
						L_strPRTCD=M_rstRSSET.getString("QP_SRLNO");						
						if(jdtTEMP !=null)
						{		
							L_strSTRDT=M_fmtLCDAT.format(jdtTEMP);
						}
						else
						{
							L_strSTRDT="";
						}
						jdtTEMP=M_rstRSSET.getDate("QP_ENDDT");
						if(jdtTEMP !=null)
						{		
							L_strENDDT=M_fmtLCDAT.format(jdtTEMP);
						}
						else
						{
							L_strENDDT=" Till Now";
						}
						if(intRECCT==0)
						{
							L_strSQLQRY="SELECT PT_PRTNM FROM CO_PTMST WHERE PT_PRTCD='"+L_strPRTCD+"' AND PT_PRTTP='C' AND isnull(PT_STSFL,'')<> 'X'";
							L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
							if(L_rstRSSET!=null)
							{
								if(L_rstRSSET.next())
								{
									L_strPRTNM = L_rstRSSET.getString("PT_PRTNM");
								}
							}
							if(L_rstRSSET!=null)
								L_rstRSSET.close();
							dosREPORT.writeBytes(padSTRING('R',"Specification from "+L_strSTRDT+" - "+L_strENDDT+" Party:"+L_strPRTCD+" "+L_strPRTNM,strDOTLN.length())+"\n");
							dosREPORT.writeBytes( strDOTLN +"\n");	
							cl_dat.M_intLINNO_pbst +=2;
							strSPDAT=L_strSTRDT;
							strPRTCD=L_strPRTCD;
							L_strPRTNM="";
						}						
						else
						if((M_fmtLCDAT.parse(L_strSTRDT).compareTo(M_fmtLCDAT.parse(strSPDAT))>0)||(!(strPRTCD.equals(L_strPRTCD))))
						{
							dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
							L_strSQLQRY="SELECT PT_PRTNM FROM CO_PTMST WHERE PT_PRTCD='"+L_strPRTCD+"' AND PT_PRTTP='C' AND isnull(PT_STSFL,'')<> 'X'";
							L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
							if(L_rstRSSET!=null)
							{
								if(L_rstRSSET.next())
								{
									L_strPRTNM = L_rstRSSET.getString("PT_PRTNM");
								}
							}
							if(L_rstRSSET!=null)
								L_rstRSSET.close();
							dosREPORT.writeBytes(padSTRING('R',"Specification from "+L_strSTRDT+" - "+L_strENDDT+" Party :"+L_strPRTCD+" "+L_strPRTNM,strDOTLN.length())+"\n");
							dosREPORT.writeBytes( strDOTLN +"\n");	
							cl_dat.M_intLINNO_pbst +=4;
							strSPDAT=L_strSTRDT;
							strPRTCD=L_strPRTCD;
							L_strPRTNM="";
						}
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),""),10));	//Parameter
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("QP_UOMDS"),"-"),15));	//Unit of Measurement
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("QP_QPRDS"),"-"),38));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("QP_TSMDS"),"-"),19));
		    		dosREPORT.writeBytes(padSTRING('R',L_intSPECF,12));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst ++;
					intRECCT = 1;
				}
				dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
				cl_dat.M_intLINNO_pbst +=2;
			}
			fosREPORT.close();
			dosREPORT.close();
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setMSG("",'N'); 
	}
	/**
	 * Method to generate the header of the Report.
	*/
	void prnHEADER()
	{
		java.sql.Date jdtTEMP=null;
		try
		{
			String L_strFRDAT="";
			String L_strTODAT="";
			setCursor(cl_dat.M_curWTSTS_pbst);
			ResultSet L_rstRSSET;
			String L_strSQLQRY="";
			String L_strPRDTP=(String)cmbPRDTP.getSelectedItem();
			L_strPRDTP=L_strPRDTP.substring(2,L_strPRDTP.length());
			if(rdbCURSP.isSelected())
		   {
				L_strSQLQRY = "SELECT QP_STRDT FROM CO_QPMST WHERE  QP_PRDCD ='"+txtPRDCD.getText()+"' AND  QP_SRLNO ='" +txtPRTCD.getText().trim()+"'  AND QP_ENDDT is null ";			
				L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET!=null)
				{
					if(L_rstRSSET.next())
					{
						jdtTEMP = L_rstRSSET.getDate("QP_STRDT");
					}
					if(jdtTEMP !=null)
					{		
						L_strFRDAT=M_fmtLCDAT.format(jdtTEMP);
						L_strTODAT = "Till Now";
					}
					else
					{
						L_strFRDAT="";
					}
				}
				if(L_rstRSSET!=null)
					L_rstRSSET.close();
			}
		   if(rdbHSTSP.isSelected())
		   {
				L_strSQLQRY = "SELECT min(QP_STRDT) QP_STRDT  FROM CO_QPMST WHERE  QP_PRDCD ='"+txtPRDCD.getText()+"' AND  QP_SRLNO ='" +txtPRTCD.getText().trim()+"' ";			
				L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET!=null)
				{
					if(L_rstRSSET.next())
					{
						jdtTEMP = L_rstRSSET.getDate("QP_STRDT");
					}
					if(jdtTEMP !=null)
					{		
						L_strFRDAT=M_fmtLCDAT.format(jdtTEMP);
						L_strTODAT = "Till Now";
					}
					else
					{
						L_strFRDAT="";
					}
				}
				if(L_rstRSSET!=null)
					L_rstRSSET.close();
			}
			if(rdbRNGSP.isSelected())
			{
				L_strFRDAT=txtFMDAT.getText().trim();
				L_strTODAT=txtTODAT.getText().trim();
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('C',"SUPREME PETROCHEM LTD.",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('C',"PRODUCT SPECIFICATION SHEET",strDOTLN.length())+"\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes(padSTRING('R',"Product Type :"+L_strPRDTP.trim(),strDOTLN.length()-21)+"\n");
		    dosREPORT.writeBytes(padSTRING('R',"Product Code :"+txtPRDCD.getText().trim(),strDOTLN.length()-23));
			dosREPORT.writeBytes(padSTRING('R',"Report Date:"+(cl_dat.M_strLOGDT_pbst),strDOTLN.length()-23)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Grade        :"+txtPRDDS.getText().trim(),strDOTLN.length()-23));
			dosREPORT.writeBytes(padSTRING('R',"Page No    :"+cl_dat.M_PAGENO ,strDOTLN.length()-23)+"\n");
			if(!rdbHSTSP.isSelected())
			{
				dosREPORT.writeBytes(padSTRING('R',"Valid From   :"+L_strFRDAT+"-"+L_strTODAT,strDOTLN.length()-23)+"\n");
				if(!((txtPRTCD.getText()).equals("00000")))
				{
					dosREPORT.writeBytes(padSTRING('R',"Party Code   :"+txtPRTCD.getText().trim() , strDOTLN.length()-23)+"\n");
					dosREPORT.writeBytes(padSTRING('R',"Party Name   :"+ txtPRTNM.getText().trim(), strDOTLN.length()-23)+"\n");
					cl_dat.M_intLINNO_pbst +=2;
				}
				cl_dat.M_intLINNO_pbst +=1;
			}
			dosREPORT.writeBytes(strDOTLN +"\n");	
			dosREPORT.writeBytes("Parameter UoM            Description                           Test Method         Specifications   ");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst += 8;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtPRDDS.setEnabled(false);
		txtPRTNM.setEnabled(false);
		txtPRTCD.setText("00000");
		txtPRTNM.setText("Default");
	}
}