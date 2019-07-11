/*
System Name   : Common Master records
Program Name  : Party List  
Program Desc. : Report to generate the hard copy document for various parties for given criteria.
Author        : Mr S.R.Mehesare
Date          : 02.05.2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.util.StringTokenizer;
/**<pre>
System Name : Common Master Records
 
Program Name : Party List  

Purpose : Report to generate the hard copy document for various parties for given criteria.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
cmbPRTTP    PT_PRTTP       CO_PTMST      VARCHAR(1)     Party Type
txtDATAX    PT_PRTCD       CO_PTMST      VARCHAR(5)     Party Code 
txtDATDS    PT_PRTNM       CO_PTMST      VARCHAR(40)    Party Name
txtFMXXX    PT_PRTCD       CO_PTMST      VARCHAR(5)     Party Code
txtTOXXX    PT_PRTCD       CO_PTMST      VARCHAR(5)     Party Code  
--------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
      For Party Type
          Data is taken from CO_CDTRN for CMT_CGSTP ='MSTCOXXPRT'.
      For Party Zone wise
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


public class co_rpptm extends cl_rbase
{  	
									/** JTextField to accept & display Party Code to Specify the Range.*/	
	private JTextField txtFMXXX;	/** JTextField to accept & display Party Code to Specify the Range.*/	
	private JTextField txtTOXXX;	/** JTextField to accept & display selected field related Code Code.*/	
	private JTextField txtDATAX;	/** JTextField to display discription for given Code.*/	
	private JTextField txtDATDS;	/** JComboBox to display & to allow to select Party Type.*/
	private JComboBox cmbPRTTP;		
	private JLabel lblCRITE;
	private JLabel lblORDER;	
	private JLabel lblSPECI;		/** JComboBox to Display & allow, to select Specification.*/	
	private JComboBox cmbSPECI;		/** JComboBox to Display & allow, to select Order By Condition.*/
	private JComboBox cmbORDER;		/** JComboBox to Display & allow, to select Specification Crieteria.*/
	private JComboBox cmbCRITE;
	private boolean flgFIRST= true;		/** Integer variable to get the length so as to get description of Condition field.*/
	private int intLENGTH;				/** String for generated Report File Name*/	                     
	private String strFILNM;			/** Integer for counting the records fetched from DB.*/	                     
	private int intRECCT = 0;				/** String variable for Party Type Description.*/
	private String strPRTDS;				/** File Output Stream for generated report file Handling.*/
	private FileOutputStream fosREPORT;		/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;		/** String variable to Print the header of the Report.*/
	private String strDATDS;				/** String variable to Print the header of the Report.*/
	private String strHEDER="";					
	co_rpptm()
	{
	    super(1);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Report Option"),4,2,1,1,this,'L');
			add(cmbSPECI = new JComboBox(),4,3,1,2,this,'L');															
			add(lblCRITE = new JLabel("Condition"),4,5,1,.7,this,'R');
			add(cmbCRITE = new JComboBox(),4,6,1,2,this,'L');
			 
			add(new JLabel("Party Type"),5,2,1,1,this,'R');
			add(cmbPRTTP= new JComboBox(),5,3,1,2,this,'L');			
			add(lblORDER = new JLabel("Order By"),5,5,1,.7,this,'R');
			add(cmbORDER = new JComboBox(),5,6,1,2,this,'L');
			
			add(lblSPECI = new JLabel("Specify"),6,2,1,1,this,'L');
			add(txtDATAX= new TxtLimit(15),6,3,1,2,this,'L');
			add(txtDATDS= new TxtLimit(40),6,5,1,3,this,'L');
			add(txtFMXXX = new TxtLimit(5),6,3,1,1,this,'L');			 			
			add(txtTOXXX = new TxtLimit(5),6,4,1,1,this,'L');																
			
			cmbSPECI.addItem("Specific Condition");
			cmbSPECI.addItem("Party Range");
			cmbSPECI.addItem("All Parties");
			
			cmbCRITE.addItem("Distributor wise");
			cmbCRITE.addItem("Cons.Stk.wise");
			cmbCRITE.addItem("Zone wise");
			cmbCRITE.addItem("State wise");
			cmbCRITE.addItem("Country wise");
			cmbCRITE.addItem("City wise");
			cmbCRITE.addItem("Transpoter wise");
			cmbCRITE.addItem("Category wise");
			cmbCRITE.addItem("Party-Name Like");
			
			cmbORDER.addItem("Party Name");
			cmbORDER.addItem("Party Code");			
			try
			{
				M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT'";							
				M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);					
				if(M_rstRSSET !=null)				
				{									
					while(M_rstRSSET.next())
					{									
						cmbPRTTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));					
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Constructor");
			}
			intLENGTH =((String)(cmbPRTTP.getSelectedItem()).toString()).length();
			strPRTDS=((String)cmbPRTTP.getSelectedItem()).substring(2,intLENGTH);
			M_pnlRPFMT.setVisible(true);							 										
			flgFIRST = true;
			setENBL(false);			
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
    }
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 * @param L_flgSTAT boolean argument to pass the enable State wise for the components.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);						
		if (flgFIRST== true)
		{
			flgFIRST= false;
			return;
		}		
		M_pnlRPFMT.setEnabled(true);
		M_rdbTEXT.setEnabled(true);
		M_rdbHTML.setEnabled(true);
		txtDATDS.setEnabled(false);
		cmbORDER.setEnabled(true);		
		lblSPECI.setVisible(true);
		txtFMXXX.setText("");
		txtTOXXX.setText("");
		txtFMXXX.setVisible(true);
		txtTOXXX.setVisible(true);
		cmbCRITE.setEnabled(false);	
		cmbSPECI.setEnabled(true);
		txtDATAX.setVisible(false);
		cmbPRTTP.setEnabled(true);
		txtDATDS.setVisible(false);
		lblCRITE.setEnabled(false);
		lblSPECI.setEnabled(true);
		
		if((cmbSPECI.getSelectedItem()).equals("Specific Condition"))
		{			
			cmbCRITE.setVisible(true);
			txtDATAX.setVisible(true);			
			txtDATDS.setVisible(true);
			lblCRITE.setVisible(true);			
			lblSPECI.setText("Specify");
			txtFMXXX.setVisible(false);
			txtTOXXX.setVisible(false);
			cmbCRITE.setEnabled(true);			
			txtDATAX.setEnabled(true);	
			lblSPECI.setVisible(true);
			txtDATDS.setVisible(true);
			lblCRITE.setEnabled(true);
		}		
		else if((cmbSPECI.getSelectedItem()).equals("Party Range"))
		{		
			lblCRITE.setEnabled(false);						
			lblSPECI.setText("Party Range");								
			txtFMXXX.setEnabled(true);
			txtTOXXX.setEnabled(true);																																						
		}
		else if((cmbSPECI.getSelectedItem()).equals("All Parties"))
		{					
			lblSPECI.setEnabled(false);
			txtTOXXX.setEnabled(false);			
			txtFMXXX.setEnabled(false);			
		}								
	}	
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE);		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			{				
				flgFIRST= true;							
				setMSG("Select an Option..",'N');												
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				setENBL(false);
			}			
			else
			{					
				setENBL(false);				
				cmbSPECI.requestFocus();				
				setMSG("Enter Party Type & press Enter Key OR Press F1 to select from List..",'N');
			}
		}		
		if(M_objSOURC ==cmbSPECI)
		{					
			txtDATAX.setText("");
			txtDATDS.setText("");			
			if((cmbSPECI.getSelectedItem()).equals("Specific Condition"))			
				cmbORDER.requestFocus();										
			else if((cmbSPECI.getSelectedItem()).equals("Party Range"))//Range
			{								
				txtFMXXX.setText("");
				txtTOXXX.setText("");				
				txtFMXXX.requestFocus();
			}
			else if((cmbSPECI.getSelectedItem()).equals("All Parties"))
				cmbORDER.requestFocus();			
			setENBL(false);
		}
		
		if(M_objSOURC == cmbCRITE)
		{	
			StringTokenizer st = new StringTokenizer((String)cmbCRITE.getSelectedItem().toString()," ");
			strHEDER = st.nextToken();
			
			if((cmbSPECI.getSelectedItem()).equals("Specific Condition"))
			{					
				txtDATAX.setText("");
				txtDATDS.setText("");				
				cmbORDER.requestFocus();
				setMSG("Select Order by Condition..",'N');				
			}			
		}				
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;				
		}
		if(M_objSOURC == cmbPRTTP)
		{			
			intLENGTH =((String)(cmbPRTTP.getSelectedItem()).toString()).length();
			strPRTDS=((String)cmbPRTTP.getSelectedItem()).substring(2,intLENGTH);			
			if((cmbSPECI.getSelectedItem()).equals("Specific Condition"))
			{
				txtDATAX.setText("");
				txtDATDS.setText("");				
				cmbCRITE.requestFocus();
				setMSG("Select Perticular Condition to generate the Report ",'N');
			}
			if((cmbSPECI.getSelectedItem()).equals("Party Range"))//Range
			{								
				txtFMXXX.setText("");
				txtTOXXX.setText("");
				txtFMXXX.requestFocus();
				setMSG("Enter Party Code to specify Party Code Range..",'N');				
			}
			else if((cmbSPECI.getSelectedItem()).equals("All Parties"))
			{
				cmbORDER.requestFocus();
				setMSG("Select Order by Condition..",'N');
			}						
		}		
		if(M_objSOURC == cmbORDER)
		{
			if((cmbSPECI.getSelectedItem()).equals("Specific Condition")) //Specific
			{
				txtDATAX.requestFocus();
				setMSG("Specify "+ (String)cmbCRITE.getSelectedItem().toString()+"Code to generate the Report..",'N');
			}
			else
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("Press Enter Key To generate The Report..",'N');
			}
		}
		if(M_objSOURC == txtFMXXX)
		{
			if((txtFMXXX.getText().trim()).length()==0)
			{
				txtTOXXX.requestFocus();
				setMSG("Enter Party Code, OR Press F1 to specify Party Code Range.. ",'N');
			}
			else
			{
				txtFMXXX.requestFocus();
				setMSG("Enter Party Code, OR Press F1 to specify Party Code Range.. ",'E');
			}				
		}
		if(M_objSOURC == txtTOXXX)
		{
			if((txtTOXXX.getText().trim()).length()==0)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("Press Enter Key OR press Left Click to generate the Report.. ",'N');
			}
			else
			{
				txtTOXXX.requestFocus();
				setMSG("Enter Party Code, OR Press F1 to specify Party Code Range.. ",'E');
			}
		}
		if(M_objSOURC == txtDATAX) 
		{			
			cl_dat.M_btnSAVE_pbst.requestFocus();
			setMSG("Press Enter Key Togenerate Report..",'N');			
		}				
		setCursor(cl_dat.M_curDFSTS_pbst);
	}					
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {						
			if(M_objSOURC == txtDATAX) 
			{			
				txtDATDS.setText("");				
				txtDATAX.setText((txtDATAX.getText().trim()).toUpperCase());
				M_strHLPFLD = "txtDATAX";				
				if(((cmbCRITE.getSelectedItem()).equals("Distributor wise")) || (cmbCRITE.getSelectedItem()).equals("Transpoter wise") || (cmbCRITE.getSelectedItem()).equals("Cons.Stk.wise")) //Disteributer / Zone wise
				{
					M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where";
					if((cmbCRITE.getSelectedItem()).equals("Distributor wise"))
						M_strSQLQRY += " PT_PRTTP ='D'";				
					else if((cmbCRITE.getSelectedItem()).equals("Transpoter wise"))
				 		M_strSQLQRY += " PT_PRTTP ='T'";
					else if((cmbCRITE.getSelectedItem()).equals("Cons.Stk.wise"))
				 		M_strSQLQRY += " PT_PRTTP ='G'";
					if((txtDATAX.getText().trim()).length()>0)
						M_strSQLQRY += " AND PT_PRTCD like'"+ txtDATAX.getText().trim()+"%'";
					M_strSQLQRY += " AND isnull(PT_STSFL,'') <> 'X' order by PT_PRTCD";					
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Description"},2,"CT");
				}													
				if((cmbCRITE.getSelectedItem()).equals("Zone wise"))
				{
					M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP||CMT_CGSTP = 'SYSMR00ZON'";													
					if((txtDATAX.getText().trim()).length()>0)
						M_strSQLQRY += " and CMT_CODCD like'"+ txtDATAX.getText().trim()+"%'";
					M_strSQLQRY += " AND CMT_STSFL <> 'X' order by CMT_CODCD";					
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Zone Code","Description"},2,"CT");
				}
				else if((cmbCRITE.getSelectedItem()).equals("State wise"))
				{					
					M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP||CMT_CGSTP = 'MSTCOXXSTA'";					
					if((txtDATAX.getText().trim()).length()>0)
						M_strSQLQRY += " and CMT_CODCD like'"+ txtDATAX.getText().trim()+"%'";					
					M_strSQLQRY += " AND CMT_STSFL <> 'X' order by CMT_CODCD";													
					cl_hlp(M_strSQLQRY,2,1,new String[]{"State Code","Description"},2,"CT");
				}
				else if((cmbCRITE.getSelectedItem()).equals("Country wise"))
				{					
					M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP||CMT_CGSTP = 'MSTCOXXCNT'";							
					if((txtDATAX.getText().trim()).length()>0)
						M_strSQLQRY += " and CMT_CODCD like'"+ txtDATAX.getText().trim()+"%'";
					M_strSQLQRY += " AND CMT_STSFL <> 'X' order by CMT_CODCD";										
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Country Code","Description"},2,"CT");
				}
				else if((cmbCRITE.getSelectedItem()).equals("City wise"))
				{
					M_strSQLQRY = "select distinct PT_CTYNM from CO_PTMST where PT_PRTTP ='"+ ((String)(cmbPRTTP.getSelectedItem()).toString()).substring(0,1) +"'";			
					if((txtDATAX.getText().trim()).length()>0)
						M_strSQLQRY += " AND PT_CTYNM like'"+ txtDATAX.getText().trim()+"%'";					
					M_strSQLQRY += " AND isnull(PT_STSFL,'') <> 'X' order by PT_CTYNM";										
					cl_hlp(M_strSQLQRY,1,1,new String[]{"City Name"},1,"CT");
				}				
				else if((cmbCRITE.getSelectedItem()).equals("Category wise"))
				{
					M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP||CMT_CGSTP = 'SYSCOXXPCT'";					
					if((txtDATAX.getText().trim()).length()>0)
						M_strSQLQRY += " and CMT_CODCD like'"+ txtDATAX.getText().trim()+"%'";
					M_strSQLQRY += " AND CMT_STSFL <> 'X' order by CMT_CODCD";													
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Category Code","Description"},2,"CT");
				}				
				if (cmbCRITE.getSelectedItem().equals("Party-Name Like"))
				{
					M_strSQLQRY = "select distinct PT_PRTNM from CO_PTMST where PT_PRTTP ='"+ ((String)(cmbPRTTP.getSelectedItem()).toString()).substring(0,1) +"'";			
					if((txtDATAX.getText().trim()).length()>0)
						M_strSQLQRY += " AND PT_PRTNM like'"+ txtDATAX.getText().trim()+"%'";					
					M_strSQLQRY += " AND isnull(PT_STSFL,'') <> 'X' order by PT_PRTNM";															
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Party Name"},1,"CT");
				}
			}
			if(M_objSOURC == txtFMXXX) 
			{				
				M_strHLPFLD = "txtFMXXX";
				M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where";								
				M_strSQLQRY += " PT_PRTTP ='"+((String)(cmbPRTTP.getSelectedItem()).toString()).substring(0,1)+"' AND";
				if((txtDATAX.getText().trim()).length()>0)
					M_strSQLQRY += " PT_PRTCD like'"+ txtDATAX.getText().trim()+"%' AND";				
				if((txtTOXXX.getText().trim()).length()>0)
					M_strSQLQRY += " PT_PRTCD <'"+ txtTOXXX.getText().trim()+"' AND";				
				M_strSQLQRY += " isnull(PT_STSFL,'') <> 'X' order by PT_PRTCD";																
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT");				
			}
			if(M_objSOURC == txtTOXXX) 
			{				
				M_strHLPFLD = "txtTOXXX";
				M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where";				
				M_strSQLQRY += " PT_PRTTP ='"+((String)(cmbPRTTP.getSelectedItem()).toString()).substring(0,1)+"' AND";
				if((txtDATAX.getText().trim()).length()>0)
					M_strSQLQRY += " PT_PRTCD like'"+ txtDATAX.getText().trim()+"%' AND";				
				if((txtFMXXX.getText().trim()).length()>0)
					M_strSQLQRY += " PT_PRTCD >'"+ txtFMXXX.getText().trim()+"' AND";				
				M_strSQLQRY += " isnull(PT_STSFL,'') <> 'X' order by PT_PRTCD";								
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT");				
			}
			
		}				
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();		
		if(M_strHLPFLD == "txtDATAX")
		{
			if((cmbCRITE.getSelectedItem()).equals("City wise") || (cmbCRITE.getSelectedItem()).equals("Party-Name Like"))
			{
				txtDATAX.setText(cl_dat.M_strHLPSTR_pbst);
				strHEDER =cl_dat.M_strHLPSTR_pbst;
				return;
			}
			txtDATAX.setText(cl_dat.M_strHLPSTR_pbst);
			strDATDS=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
			txtDATDS.setText(strDATDS);
		}
		if(M_strHLPFLD == "txtTOXXX")
			txtTOXXX.setText(cl_dat.M_strHLPSTR_pbst);		
		if(M_strHLPFLD == "txtFMXXX")		
			txtFMXXX.setText(cl_dat.M_strHLPSTR_pbst);		
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
		{							
			return;
		}
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"co_rpptm.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "co_rpptm.doc";				
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Party List"," ");
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
		String L_strDATA;
		String L_strTELNO;
		intRECCT=0;
		ResultSet M_rstRSSET1;
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Party List</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}			
			prnHEADER();			
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,";
			M_strSQLQRY += "PT_CONNM,PT_TEL01,PT_TEL02,PT_FAXNO,PT_STXNO,PT_CSTNO,PT_EMLMR,";
			M_strSQLQRY += "PT_ECCNO,PT_ITPNO,PT_EXCNO,PT_RNGDS,PT_DIVDS,PT_CLLDS,PT_TSTFL,";
			M_strSQLQRY += "PT_TRNCD,PT_TINNO,PT_VATNO,PT_DSRCD,PT_DSTCD,PT_MOBNO";			
			M_strSQLQRY += " from CO_PTMST";
			M_strSQLQRY += " where PT_PRTTP ='"+((String)(cmbPRTTP.getSelectedItem()).toString()).substring(0,1)+"'";			
			M_strSQLQRY += " and isnull(PT_STSFL,'') <> 'X'";									
			if(((cmbSPECI.getSelectedItem()).equals("Specific Condition")) && (txtDATAX.getText().trim().length()>0))
			{
				if((cmbCRITE.getSelectedItem()).equals("Distributor wise"))
					M_strSQLQRY += " and PT_DSRCD ='"+ txtDATAX.getText().trim()+"'";										
				else if((cmbCRITE.getSelectedItem()).equals("Cons.Stk.wise"))
					M_strSQLQRY += " and PT_DSRCD ='"+ txtDATAX.getText().trim()+"'";										
				else if((cmbCRITE.getSelectedItem()).equals("Zone wise"))
					M_strSQLQRY += " and PT_ZONCD ='"+ txtDATAX.getText().trim()+"'";			
				else if((cmbCRITE.getSelectedItem()).equals("State wise"))
					M_strSQLQRY += " and PT_STACD ='"+ txtDATAX.getText().trim()+"'";			
				else if((cmbCRITE.getSelectedItem()).equals("Country wise"))
					M_strSQLQRY += " and PT_CNTCD ='"+ txtDATAX.getText().trim()+"'";			
				else if((cmbCRITE.getSelectedItem()).equals("City wise"))
					M_strSQLQRY += " and PT_CTYNM ='"+ txtDATAX.getText().trim()+"'";			
				else if((cmbCRITE.getSelectedItem()).equals("Transpoter wise"))
					M_strSQLQRY += " and PT_TRNCD ='"+ txtDATAX.getText().trim()+"'";			
				else if((cmbCRITE.getSelectedItem()).equals("Category wise"))
					M_strSQLQRY += " and PT_DEFCD ='"+ txtDATAX.getText().trim()+"'";											
				else
					M_strSQLQRY += " AND PT_PRTNM like '%"+ txtDATAX.getText().trim().toUpperCase()+"%'";
			}						
			else if((cmbSPECI.getSelectedItem()).equals("Party Range"))			
				M_strSQLQRY += " and PT_PRTCD between '"+ txtFMXXX.getText().trim()+"' AND '"+ txtTOXXX.getText().trim()+"'";			
			
			if((cmbORDER.getSelectedItem()).equals("Party Name"))
				M_strSQLQRY += " order by PT_PRTNM";									
			else
				M_strSQLQRY += " order by PT_PRTCD";
			System.out.println (M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)				
			{									
				while(M_rstRSSET.next())
				{									
					dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_PRTCD"),7));					
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"-"),42));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_STXNO"),"-"),22));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ITPNO"),"-"),32));	
					dosREPORT.writeBytes(cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXDST",nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"-"))+"\n");

					dosREPORT.writeBytes("       "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),"-"),42));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_CSTNO"),"-"),22));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_EXCNO"),"-"),32));
					dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("PT_MOBNO"),"-")+"\n");						
					
					dosREPORT.writeBytes("       "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),"-"),42));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ECCNO"),"-"),22));
					dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("PT_RNGDS"),"-")+"\n");
										
					dosREPORT.writeBytes("       "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),"-"),42));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_TINNO"),"-"),22));
					
					L_strDATA = nvlSTRVL(M_rstRSSET.getString("PT_TRNCD"),"");
					if(L_strDATA.length()!=0)
					{						
						try
						{
							M_strSQLQRY = "Select PT_PRTNM from CO_PTMST";					
							M_strSQLQRY += " where PT_PRTTP ='T'";			
							M_strSQLQRY += " AND PT_PRTCD ='"+ L_strDATA +"'";			
							M_strSQLQRY += " AND isnull(PT_STSFL,'') <> 'X'";																
							M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);					
							if(M_rstRSSET1 !=null)				
							{									
								if(M_rstRSSET1.next())
								{									
									L_strDATA = nvlSTRVL(M_rstRSSET1.getString("PT_PRTNM"),"");
								}
								M_rstRSSET1.close();								
							}						
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"get Transporter Name");						
						}
					}
					else
						L_strDATA="-";
										
					dosREPORT.writeBytes(L_strDATA + "\n");											
					dosREPORT.writeBytes("       "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),"-"),42));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_VATNO"),"-"),22)+"\n");
					dosREPORT.writeBytes("       "+padSTRING('R',"-",42));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_DIVDS"),"-"),22));					
					dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("PT_CLLDS"),"-")+"\n");
															
					dosREPORT.writeBytes("       "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_CONNM"),"-"),42));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_FAXNO"),"-"),22));					
					
					L_strTELNO="";
					L_strDATA="";
					L_strTELNO = nvlSTRVL(M_rstRSSET.getString("PT_TEL01"),"-");
					L_strDATA = nvlSTRVL(M_rstRSSET.getString("PT_TEL02"),"-");
					dosREPORT.writeBytes(padSTRING('R',L_strTELNO+" "+L_strDATA,32));
					
					L_strDATA="";
					L_strDATA=nvlSTRVL(M_rstRSSET.getString("PT_TSTFL"),"");
					if((L_strDATA.equals("y"))||(L_strDATA.equals("Y")))
						L_strDATA="YES";
					else
						L_strDATA="NO";
						
					dosREPORT.writeBytes(L_strDATA + "\n");									
					
					dosREPORT.writeBytes("       "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_EMLMR"),"-"),42)+"\n\n");					
						
					//dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------\n");
					cl_dat.M_intLINNO_pbst = cl_dat.M_intLINNO_pbst + 9;				
					if((cl_dat.M_intLINNO_pbst > 62) && ((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) || (M_rdbHTML.isSelected())))
					{						
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();					
					}	
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
		txtDATAX.setText(txtDATAX.getText().trim().toUpperCase());
		if((cmbSPECI.getSelectedItem()).equals("Specific Condition"))
		{	
			if((txtDATAX.getText().trim()).length()==0)
			{
				txtDATAX.requestFocus();
				setMSG("Enter "+(String)cmbCRITE.getSelectedItem().toString()+" Code Or Press F1 Key to Select from List..",'E');
				return false;
			}
		}
		if((cmbSPECI.getSelectedItem()).equals("Party Range"))	
		{						
			if((txtFMXXX.getText().trim()).length()==0)
			{
				txtFMXXX.requestFocus();
				setMSG("Enter Party code To Specify Party Code Range..",'E');				
				return false;
			}
			if((txtTOXXX.getText().trim()).length()==0)
			{
				txtTOXXX.requestFocus();
				setMSG("Enter Party code To Specify Party Code Range..",'E');				
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
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,86));										
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			
			if((cmbSPECI.getSelectedItem()).equals("Specific Condition"))
				dosREPORT.writeBytes(padSTRING('R',strPRTDS +" List for " + strHEDER +" : "+ txtDATDS.getText().trim() ,86));	
				
			if((cmbSPECI.getSelectedItem()).equals("Party Range"))
				dosREPORT.writeBytes(padSTRING('R',strPRTDS +" List for the Party Range : " + txtFMXXX.getText().trim()+", "+txtTOXXX.getText().trim(),86));					
			
			else if((cmbSPECI.getSelectedItem()).equals("All Parties"))
				dosREPORT.writeBytes(padSTRING('R',"List of All "+strPRTDS +"s",86));	
					
			dosREPORT.writeBytes("Page No.    : " + cl_dat.M_PAGENO + "\n");
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------\n");									
			dosREPORT.writeBytes("Code   Name                                      S.T. No.              IT PAN No.                      Destination\n");
			dosREPORT.writeBytes("       Address                                   C.S.T. No.            Excise No.                      Mobile No.\n");
			dosREPORT.writeBytes("                                                 E.C.C. No.            Range \n");
			dosREPORT.writeBytes("                                                 CST TIN No.           Transpoter \n");  								 
			dosREPORT.writeBytes("                                                 VAT TIN No.           \n");  								 
			dosREPORT.writeBytes("                                                 Division              Collectorate\n");
			dosREPORT.writeBytes("       Contact Person                            Fax No.               Tel.No1, Tel.No2                T.Cert.\n"); 
			dosREPORT.writeBytes("       Email Id \n");
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------\n\n");
			cl_dat.M_intLINNO_pbst=14;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
}

		
		

/*
			rdbSPECI = new JRadioButton("Specific  ",true);
			rdbRANGE = new JRadioButton("Range     ");
			rdbALLXX = new JRadioButton("All       ");
			
			pnlPANL2 = new JPanel();							
			bgrRANGE = new ButtonGroup();
			bgrRANGE.add(rdbSPECI);			
			bgrRANGE.add(rdbRANGE);
			bgrRANGE.add(rdbALLXX);
			add(rdbSPECI,1,2,1,1,pnlPANL2,'L');
			add(rdbRANGE,1,4,1,1,pnlPANL2,'L');											
			add(rdbALLXX,1,6,1,1,pnlPANL2,'L');
			add(pnlPANL2,4,3,2,4.2,this,'L');
						
			add(lblCRITE = new JLabel("Condition"),6,2,1,1,this,'L');
			add(cmbCRITE = new JComboBox(),6,3,1,1.3,this,'R');
			add(lblSPECI = new JLabel("Specify"),6,4,1,1,this,'R');
			add(txtDATAX= new TxtLimit(15),6,5,1,1.5,this,'R');
			add(txtDATDS= new TxtLimit(40),6,6,1,2.5,this,'L');
			
			add(new JLabel("Party Type"),7,2,1,1,this,'L');
			add(cmbPRTTP= new JComboBox(),7,3,1,1.3,this,'R');
			add(lblPRTNM = new JLabel("Party Name"),7,5,1,1,this,'L');
			add(txtPRTNM = new JTextField(),7,6,1,3,this,'L');
			
			add(lblORDER = new JLabel("Order By"),8,3,1,1,this,'L');
			add(cmbORDER = new JComboBox(),8,4,1,1.5,this,'L');
			
			*/
/*if(M_objSOURC == txtPRTNM) 
			{				
				M_strHLPFLD = "txtPRTNM";
				M_strSQLQRY = "select distinct PT_PRTNM from CO_PTMST where";				
				M_strSQLQRY += " PT_PRTTP ='"+((String)(cmbPRTTP.getSelectedItem()).toString()).substring(0,1)+"'";
				if(txtDATAX.getText().trim().length()>0)
				{
					if((cmbCRITE.getSelectedItem()).equals("Distributor wise")) 
						M_strSQLQRY += " AND PT_DSRCD ='";
					else if((cmbCRITE.getSelectedItem()).equals("Transpoter wise"))
						M_strSQLQRY += " AND PT_TRNCD ='";
					else if((cmbCRITE.getSelectedItem()).equals("Zone wise"))
						M_strSQLQRY += " AND PT_ZONCD ='";
					else if((cmbCRITE.getSelectedItem()).equals("State wise"))
						M_strSQLQRY += " AND PT_STACD ='";
					else if((cmbCRITE.getSelectedItem()).equals("Country wise"))
						M_strSQLQRY += " AND PT_CNTCD ='";
					else if((cmbCRITE.getSelectedItem()).equals("City wise"))
						M_strSQLQRY += " AND PT_CTYNM ='";
					else if((cmbCRITE.getSelectedItem()).equals("Category wise"))
						M_strSQLQRY += " AND PT_CODCD ='";
					M_strSQLQRY += txtDATAX.getText().trim()+"'";
				}
				M_strSQLQRY += " order by PT_PRTNM ";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Party Name"},1,"CT");
			}*/