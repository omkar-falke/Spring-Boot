/*
	System Name    : HRMS
	 
	Program Name   : Leave Processing Authorities Master.

	Purpose        : Allows to add,modify or delete Reccomending Authorities and 
					 Sanctioning Authorities for employees. 

	List of tables used :
	Table Name     Primary key                                 Operation done
															Insert   Update   Query   Delete	
	--------------------------------------------------------------------------------------
	CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD				#		#	   #	   #
	HR_EPMST	   EP_EMPNO													   #	
	--------------------------------------------------------------------------------------

	List of  fields accepted/displayed on screen :
	Field Name  Column Name    Table name    Type/Size      Description
	--------------------------------------------------------------------------------------
	txtDPTCD    cmt_codcd       CO_CDTRN	  VARCHAR(3)     Dept Code
	txtEMPNO	ep_empno		HR_EPMST	  VARCHAR(4)	 Employee No.
	rdbRCMBY	-				-			  -				 Records of Recommending auth to be fetched
	rdbSSNBY	-				-			  -				 Records of sanc auth to be fetched
	txtEXAUT    cmt_codcd       CO_CDTRN	  VARCHAR(4)     TextField that displays Existing Authority.
	chkGRMOD	-				-			  -				 CheckBox that is used for Group Modification  	 
	txtNWAUT    cmt_codcd       CO_CDTRN	  VARCHAR(4)     TextField that displays New Authority that to be replace with existing authority.
	chkAPPLY	-				-			  -				 If chkGRMOD is selected then this check box will be set visible.
  															 this will change all the entries of tables existing authority with new authority 
	
	for tblMELRS
	txtTB_DPTCD cmt_codcd       CO_CDTRN	  VARCHAR(3)     Dept Code
	txtTB_DPTNM cmt_shrds       CO_CDTRN	  VARCHAR()      Dept Name
	txtTB_EMPNO	ep_empno		HR_EPMST	  VARCHAR(4)	 Employee No.	
	txtTB_EMPNM	ep_empnm		HR_EPMST	  VARCHAR(4)	 Employee Name	
	txtTB_EXAUT cmt_codcd       CO_CDTRN	  VARCHAR(4)     TextField that displays Existing Authority.
	--------------------------------------------------------------------------------------

	List of Fields with help facility
	Field Name  Display Description         Display Columns           Table Name
	---------------------------------------------------------------------------------------------
	txtDPTCD    Dept Code,Description       cmt_codcd,cmt_codds		  CO_CDTRN-SYS/COXXDPT
	txtEMPNO	Employee code,Description	ep_empno				  HR_EPMST
	txtEXAUT    Employee Code,Description   cmt_codcd,cmt_codds		  CO_CDTRN-AUT/HRssLRC/HRssLAU ss='01' for works etc. 
                                                                      value = substr(cmt_codcd,6,4)
	txtNWAUT	Employee code,Description	ep_empno				  HR_EPMST
	txtTB_EXAUT Employee code,Description	ep_empno				  HR_EPMST
	---------------------------------------------------------------------------------------------

	Validations :
	->	If Dept Code is given then display all the employees of that department
	->	If both Dept code and Emp no is not given then display all employees
	->	If Recommending Authority is given then display employees with given recommending authority.
	->	If Sanc Authority is given then display employees with given Sanc authority.
	->  User can add , modify or delete the records as required.
    ->  User can Specify Dept.Code, Employee Code, Recommending Authority, Sanctioning Autority for displaying list of employees in Specified scope.
        If any of the above mentioned entity is left blank, all records of that entity are considered for fetching details.
	->  if chkAPPLY is not selected then addn or modification is done.
			while adding data if key is already present with cmt_stsfl = 'X'
			then update record
			otherwise insert record.
    ->  to delete Authority set cmt_stsfl = 'X'.
	->  if chkAPPLY is selected then only Modification can be done.
	 		if New authority for the employee is already exist in the CO_CDTRN 
			then 1st del Existing Authority and then modify New Authority
	 		otherwise update authority to the new authority directly
	-> if 'new for existing authority' check box is clicked
		then all the employees with existing auth as reccomending/sanctioning is copied to the new auth. 
*/

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.awt.Color;
import java.sql.ResultSet;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.sql.CallableStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Color;
import javax.swing.border.*;

class hr_melrs extends cl_pbase implements KeyListener
{							/** TextField for Dept Code */
	private JTextField txtDPTCD;	/** TextField for Emp No */
	private JTextField txtEMPNO;	/** TextField for Recommending Auth. */
	private JTextField txtRCMBY;	/** TextField for Sanctioning Auth. */
    private JTextField txtSSNBY;	/** Text Field if the group modificaton checkbox is checked*/	
	private JTextField txtNWAUT;	/** Text Field for the existing Authority*/	
	private JTextField txtEXAUT;	
	private JTextField txtFMAUT;
	private JTextField txtTOAUT;
	private JLabel lblDPTNM;
	private JLabel lblEMPNM;
	private JLabel lblEXAUT;
	private JLabel lblFMAUT;
	private JLabel lblTOAUT;
	private JLabel lblNWAUT1;
	private JLabel lblNWAUT2;
	private cl_JTable tblMELRS;
	private JLabel lblHEADN;
	private JCheckBox chkGRMOD;
	private JCheckBox chkAPPLY;
	private JCheckBox chkCPAUT;
	private JCheckBox chkCHALL;
	private JButton btnDISPL;
	private ButtonGroup btgAUTHR;
	private JRadioButton rdbRCMBY;
	private JRadioButton rdbSSNBY;
    private TBLINPVF objTBLVRF; 
	private INPVF oINPVF;
	private int intCNT=0;
	private String strEMPNO_LOGIN;
	private boolean flgALLDPT = false;
	JPanel pnlCPAUT;
	JButton btnCPAUT;
	
	//for tblMELRS
	private int TB_CHKFL = 0;										
	private int TB_DPTCD = 1;                    JTextField txtTB_DPTCD;
    private int TB_DPTNM = 2;                    JTextField txtTB_DPTNM;	
    private int TB_EMPNO = 3;                    JTextField txtTB_EMPNO;
	private int TB_EMPNM = 4;                    JTextField txtTB_EMPNM;
	private int TB_EXAUT = 5;                    JTextField txtTB_EXAUT;
	private int TB_AUTNM = 6;                    JTextField txtTB_AUTNM;
	/* Constructor */	
	hr_melrs()
	{
	    super(2);
		try
		{
		    setMatrix(20,20);
	            
			objTBLVRF = new TBLINPVF();
			oINPVF=new INPVF();
			
			btgAUTHR=new ButtonGroup();
			add(rdbRCMBY = new JRadioButton("RCM Authority"),2,7,1,3,this,'L');
			add(rdbSSNBY = new JRadioButton("SNC Authority"),3,7,1,3,this,'L');
			btgAUTHR.add(rdbRCMBY);btgAUTHR.add(rdbSSNBY);

			add(new JLabel("Dept Code"),4,7,1,2,this,'L');
			add(txtDPTCD = new TxtLimit(3),4,9,1,2,this,'L');
			add(lblDPTNM = new JLabel(),4,11,1,3,this,'L');

			add(chkCPAUT = new JCheckBox("New for Existing Authority"),3,14,1,4,this,'L');
			pnlCPAUT = new JPanel(null);
			add(new JLabel("   Existing"),1,1,1,2,pnlCPAUT,'L');
			add(txtFMAUT = new TxtLimit(5),1,3,1,2,pnlCPAUT,'L');
			add(lblFMAUT = new JLabel(),1,5,1,1,pnlCPAUT,'L');
			add(new JLabel("     New"),2,1,1,2,pnlCPAUT,'L');
			add(txtTOAUT = new TxtLimit(5),2,3,1,2,pnlCPAUT,'L');
			add(lblTOAUT = new JLabel(),2,5,1,1,pnlCPAUT,'L');
			add(btnCPAUT = new JButton("Copy"),3,2,1,2,pnlCPAUT,'L');
			add(pnlCPAUT,4,14,4,6,this,'L');
			pnlCPAUT.setBorder(new EtchedBorder(Color.black,Color.lightGray));
			pnlCPAUT.setVisible(false);
			chkCPAUT.setEnabled(false);
			
			add(new JLabel("Emp No"),5,7,1,2,this,'L');
			add(txtEMPNO = new TxtLimit(4),5,9,1,2,this,'L');
			add(lblEMPNM = new JLabel(),5,11,1,3,this,'L');
			
			add(new JLabel("Authority"),6,7,1,2,this,'L');
			add(txtEXAUT = new TxtLimit(5),6,9,1,2,this,'L');
			add(lblEXAUT = new JLabel(),6,11,1,3,this,'L');

			add(btnDISPL = new JButton("Display"),7,9,1,2,this,'L');
			
			add(chkGRMOD= new JCheckBox("Transfer Authority"),9,5,1,3,this,'L');
			add(lblNWAUT1=new JLabel("Enter New Authority"),9,8,1,3,this,'L');
			add(txtNWAUT= new JTextField(),9,11,1,1,this,'L');
            add(lblNWAUT2=new JLabel(""),9,12,1,2,this,'L');
			add(chkAPPLY= new JCheckBox("Apply"),9,14,1,3,this,'L');
			
			rdbRCMBY.setSelected(true);
			add(lblHEADN = new JLabel(),10,9,1,4,this,'L');
			lblHEADN.setText("Recommending Authority");
			lblHEADN.setForeground(Color.blue);
			
			add(chkCHALL = new JCheckBox("Select All"),10,5,1,3,this,'L');
			chkCHALL.setVisible(false);
			//CREATING TABLE 
            String[] L_strTBLHD1 = {"FL","Dept Code","Dept Name","Emp No","Emp Name","Authority","Name"};
			int[] L_intCOLSZ1 = {20,80,80,80,120,80,80};
			tblMELRS = crtTBLPNL1(this,L_strTBLHD1,500,11,5,7,12,L_intCOLSZ1,new int[]{0});
			
			tblMELRS.addKeyListener(this);
			tblMELRS.setCellEditor(TB_DPTCD,txtTB_DPTCD = new TxtNumLimit(3));
			tblMELRS.setCellEditor(TB_DPTNM,txtTB_DPTNM = new TxtLimit(15));
			tblMELRS.setCellEditor(TB_EMPNO,txtTB_EMPNO = new TxtNumLimit(4));
			tblMELRS.setCellEditor(TB_EMPNM,txtTB_EMPNM = new TxtLimit(15));
			tblMELRS.setCellEditor(TB_EXAUT,txtTB_EXAUT = new TxtNumLimit(4));
			tblMELRS.setCellEditor(TB_AUTNM,txtTB_AUTNM = new TxtLimit(3));
			
			btnDISPL.addKeyListener(this);
			txtEMPNO.setInputVerifier(oINPVF);
			txtDPTCD.setInputVerifier(oINPVF);
			txtEXAUT.setInputVerifier(oINPVF);
			txtNWAUT.setInputVerifier(oINPVF);
			txtFMAUT.setInputVerifier(oINPVF);
			txtTOAUT.setInputVerifier(oINPVF);
			tblMELRS.setInputVerifier(objTBLVRF);
			txtTB_DPTCD.addKeyListener(this);
			txtTB_EMPNO.addKeyListener(this);
			txtTB_EXAUT.addKeyListener(this);
			txtNWAUT.setVisible(false);
			lblNWAUT1.setVisible(false);
			chkGRMOD.setVisible(false);
			chkAPPLY.setVisible(false);
			setENBL(false);
			strEMPNO_LOGIN = getEMPNO(cl_dat.M_strUSRCD_pbst);
			flgALLDPT = cl_dat.M_strUSRCD_pbst.equals("SYS") ? true : false;
	   	}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"Constructor");
	    }
	}       // end of constructor
	
	/* super class Method overrided to enhance its functionality, to enable & disable components 
       according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{   
        super.setENBL(L_flgSTAT);
		
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{

		}
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}
	
	void clrCOMP()
	{
		super.clrCOMP();
		lblDPTNM.setText("");
		lblEMPNM.setText("");
		lblEXAUT.setText("");
		lblNWAUT2.setText("");		
	}
	

	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			String L_strNewval1,L_strNewval2;
			try
			{
				if(getSource()==tblMELRS)
			    {
					if(tblMELRS.getValueAt(P_intROWID,P_intCOLID).toString().length()==0)
						return true;

					if(P_intCOLID == TB_DPTCD)
    			    {
						M_strSQLQRY = " Select CMT_SHRDS from CO_CDTRN";
        				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' and CMT_CODCD='"+tblMELRS.getValueAt(P_intROWID,P_intCOLID).toString().trim()+"'";
						if(txtDPTCD.getText().length()==3)
							M_strSQLQRY += " and CMT_CODCD='"+txtDPTCD.getText()+"'";
						if(txtEMPNO.getText().length()==4)
							M_strSQLQRY += " and CMT_CODCD in(select EP_DPTCD from HR_EPMST where EP_EMPNO='"+txtEMPNO.getText()+"')";
						
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							tblMELRS.setValueAt(M_rstRSSET.getString("cmt_shrds"),P_intROWID,TB_DPTNM);
							setMSG("",'N');
						}	
						else
						{
							setMSG("Enter Valid Department Code",'E');
							return false;
						}
					}
					else if(P_intCOLID == TB_EMPNO)
					{
						M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) +'.' EP_EMPNM from HR_EPMST";
						M_strSQLQRY+= " where SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2)='"+M_strSBSCD.substring(0,2)+"'  and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and ep_empno = '"+tblMELRS.getValueAt(P_intROWID,P_intCOLID).toString().trim()+"'";
						if(tblMELRS.getValueAt(P_intROWID,TB_DPTCD).toString().length()==3)
							M_strSQLQRY+= " and ep_dptcd='"+tblMELRS.getValueAt(P_intROWID,TB_DPTCD).toString()+"'";
						if(txtEMPNO.getText().length()==4)
							M_strSQLQRY+= " and ep_empno='"+txtEMPNO.getText()+"'";	

						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							tblMELRS.setValueAt(M_rstRSSET.getString("EP_EMPNM"),P_intROWID,TB_EMPNM);
							setMSG("",'N');
						}	
						else
						{
							setMSG("Enter Valid Employee Code",'E');
							return false;
						}
					}	
					if(P_intCOLID == TB_EXAUT)
					{
						L_strNewval1=tblMELRS.getValueAt(P_intROWID,TB_EXAUT).toString();
						for(int i=0;i<P_intROWID;i++)
						if(tblMELRS.getValueAt(i,TB_EXAUT).toString().length()>0 && i!=P_intROWID)
						{
							if(tblMELRS.getValueAt(P_intROWID,TB_EMPNO).toString().equals(tblMELRS.getValueAt(i,TB_EMPNO).toString()))
							   if(tblMELRS.getValueAt(P_intROWID,TB_EXAUT).toString().equals(tblMELRS.getValueAt(i,TB_EXAUT).toString())) 
								{
									setMSG("Authority For The Employee Already Exist..",'E');
									return false;
								}
						}

						/*if(rdbRCMBY.isSelected())
						{	
							M_strSQLQRY = "Select EP_SHRNM,EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_EMPNO = '"+tblMELRS.getValueAt(P_intROWID,TB_EXAUT).toString()+"' and EP_EMPNO in (select substr(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR01LRC')";
						}
						else
						{
							M_strSQLQRY = "Select EP_SHRNM,EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_EMPNO = '"+tblMELRS.getValueAt(P_intROWID,TB_EXAUT).toString()+"' and EP_EMPNO in (select substr(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR01LSN')";
						}
						if(tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString().length()==3)
							M_strSQLQRY+= " and ep_dptcd='"+tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString()+"'";						
						*/
		
						M_strSQLQRY = " select distinct EP_EMPNO,EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
						M_strSQLQRY+= " where EP_EMPNO = '"+tblMELRS.getValueAt(P_intROWID,TB_EXAUT).toString()+"' and SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2)='"+M_strSBSCD.substring(0,2)+"'  and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
						
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							tblMELRS.setValueAt(M_rstRSSET.getString("EP_SHRNM"),P_intROWID,TB_AUTNM);
							setMSG("",'N');
						}	
						else
						{
							setMSG("Enter Valid Existing Authority",'E');
							return false;
						}
					}	
				}	
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"class TBLINPVF");
			}
			return true;
		}
    }
	
	/** Validate data entered by user, Format all text and calculate salary */
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;				

				if(input==txtDPTCD)
				{
					M_strSQLQRY = "Select CMT_SHRDS from CO_CDTRN ";
        			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' and CMT_CODCD='"+txtDPTCD.getText()+"'";
					if(!flgALLDPT)
						M_strSQLQRY += " and  cmt_codcd in (select distinct EP_DPTCD from HR_EPMST where EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_codcd like '_____"+strEMPNO_LOGIN+"' and cmt_stsfl<>'X'  and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN')))";

					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblDPTNM.setText(M_rstRSSET.getString("CMT_SHRDS"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
				}
				else if(input == txtEMPNO)
				{
					M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
					M_strSQLQRY+= " where SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2)='"+M_strSBSCD.substring(0,2)+"'  and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and ep_empno = '"+txtEMPNO.getText()+"'";
					
					String L_strADDCHK =  " and EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_codcd like '_____"+strEMPNO_LOGIN+"' and cmt_stsfl <> 'X'";
					if(txtDPTCD.getText().length()==3)
						L_strADDCHK +=" and cmt_chp01='"+txtDPTCD.getText()+"'";
					if(rdbRCMBY.isSelected())
						L_strADDCHK +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC')";
					else
						L_strADDCHK +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN')";
					M_strSQLQRY+= flgALLDPT ? "" : L_strADDCHK;
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Employee Code",'E');
						return false;
					}
				}
				else if(input == txtEXAUT)
				{
					M_strSQLQRY = "Select EP_EMPNO,EP_SHRNM from HR_EPMST where EP_EMPNO = '"+txtEXAUT.getText()+"' and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_stsfl <> 'X' ";
					if(txtDPTCD.getText().length()==3)
						M_strSQLQRY +=" and cmt_chp01='"+txtDPTCD.getText()+"'";
					if(txtEMPNO.getText().length()==4)
						M_strSQLQRY +=" and SUBSTRING(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'";
					if(rdbRCMBY.isSelected())
						 M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC')";
					else
						 M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN')";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtEXAUT.setText(txtEXAUT.getText());
						lblEXAUT.setText(M_rstRSSET.getString("EP_SHRNM"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Existing Authority",'E');
						return false;
					}
				}
				else if(input == txtFMAUT)
				{
					M_strSQLQRY = "Select EP_EMPNO,EP_SHRNM from HR_EPMST where EP_EMPNO = '"+txtFMAUT.getText()+"' and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_stsfl <> 'X' ";
					/*if(txtDPTCD.getText().length()==3)
						M_strSQLQRY +=" and cmt_chp01='"+txtDPTCD.getText()+"'";
					if(txtEMPNO.getText().length()==4)
						M_strSQLQRY +=" and substr(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'";
					*/
					if(rdbRCMBY.isSelected())
						 M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC')";
					else
						 M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN')";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblFMAUT.setText(M_rstRSSET.getString("EP_SHRNM"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Existing Authority",'E');
						return false;
					}
				}
				else if(input == txtTOAUT)
				{
					M_strSQLQRY = " select distinct EP_EMPNO,EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
					M_strSQLQRY+= " where SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2)='"+M_strSBSCD.substring(0,2)+"'  and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and EP_EMPNO='"+txtTOAUT.getText()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblTOAUT.setText(M_rstRSSET.getString("EP_SHRNM"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Authority",'E');
						return false;
					}
				}
				else if(input == txtNWAUT)
				{
					M_strSQLQRY = " select distinct EP_EMPNO,EP_SHRNM,EP_LSTNM + ' '+ SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
					M_strSQLQRY+= " where SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2)='"+M_strSBSCD.substring(0,2)+"'  and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and EP_EMPNO='"+txtNWAUT.getText()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblNWAUT2.setText(M_rstRSSET.getString("EP_SHRNM"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Authority",'E');
						return false;
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
	
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			super.mouseReleased(L_ME);
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}
		
	private  boolean vldHSTRY(int P_intROWNO)
	{
		try
		{

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldHSTRY");
		}
		return true;
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
		    if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
        		if(M_objSOURC==txtDPTCD)		
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
        		    M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
        			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' ";
					if(flgALLDPT)
						M_strSQLQRY += " and  cmt_codcd in (select distinct EP_DPTCD from HR_EPMST where EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_codcd like '_____"+strEMPNO_LOGIN+"' and cmt_stsfl<>'X'  and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN')))";
					
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
        								//help for Employee Category
				else if(M_objSOURC==txtEMPNO)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
        			String L_ARRHDR[] = {"Code","Category"};
					M_strHLPFLD = "txtEMPNO";
					M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
					M_strSQLQRY+= " where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
					if(!flgALLDPT)
					{
						M_strSQLQRY += " and EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_codcd like '_____"+strEMPNO_LOGIN+"' and cmt_stsfl<> 'X'";
						if(txtDPTCD.getText().length()==3)
							M_strSQLQRY +=" and cmt_chp01='"+txtDPTCD.getText()+"'";
						if(rdbRCMBY.isSelected())
							M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC')";
						else
							M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN')";
					}
					
					/*if(txtDPTCD.getText().length()==3)
						M_strSQLQRY+= " and ep_dptcd='"+txtDPTCD.getText()+"'";*/
					M_strSQLQRY+= "order by ep_empnm";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Employee No","Employee Name"},2,"CT",new int[]{107,400});
					
        		}
				else if(M_objSOURC==txtEXAUT)
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEXAUT";
        			String L_ARRHDR[] = {"Code","Short Name","Full Name"};
        			M_strSQLQRY = "Select EP_EMPNO,EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_stsfl<>'X'";
					if(txtDPTCD.getText().length()==3)
						M_strSQLQRY +=" and cmt_chp01='"+txtDPTCD.getText()+"'";
					if(txtEMPNO.getText().length()==4)
						M_strSQLQRY +=" and SUBSTRING(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'";
					if(rdbRCMBY.isSelected())
						M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC')";
					else
						M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN')";
					M_strSQLQRY +=" order by ep_empnm";
					/*if(txtDPTCD.getText().length()==3)
						M_strSQLQRY += " and ep_dptcd='"+txtDPTCD.getText()+"'";*/
        			cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
        		}
				else if(M_objSOURC==txtFMAUT)
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtFMAUT";
        			String L_ARRHDR[] = {"Code","Short Name","Full Name"};
        			M_strSQLQRY = "Select EP_EMPNO,EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_stsfl<>'X'";
					/*if(txtDPTCD.getText().length()==3)
						M_strSQLQRY +=" and cmt_chp01='"+txtDPTCD.getText()+"'";
					if(txtEMPNO.getText().length()==4)
						M_strSQLQRY +=" and substr(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'";
					*/
					if(rdbRCMBY.isSelected())
						M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC')";
					else
						M_strSQLQRY +=" and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN')";
					M_strSQLQRY +=" order by ep_empnm";
        			cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
        		}
				else if(M_objSOURC==txtTOAUT)
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtTOAUT";
        			String L_ARRHDR[] = {"Code","Short Name","Full Name"};
					M_strSQLQRY = " select distinct EP_EMPNO,EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
					M_strSQLQRY+= " where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
					M_strSQLQRY +=" order by ep_empnm";
        			cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
        		}
				
				else if(M_objSOURC==txtNWAUT)
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtNWAUT";
        			String L_ARRHDR[] = {"Code","Short Name","Full Name"};
					M_strSQLQRY = " select distinct EP_EMPNO,EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
					M_strSQLQRY+= " where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
					M_strSQLQRY +=" order by ep_empnm";
        			cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
        		}

				else if(M_objSOURC==txtSSNBY)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtSSNBY";
        			String L_ARRHDR[] = {"Code","Name"};
					M_strSQLQRY = " Select EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"'"+(flgALLDPT ? "" : " and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' and cmt_codcd like '"+txtEMPNO.getText()+"%'))");
					M_strSQLQRY +=" order by ep_empnm";
					//System.out.println(">>>>AUTBY>>>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				else if(M_objSOURC == tblMELRS.cmpEDITR[TB_DPTCD])
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtTBDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
        		    M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
        			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' ";
					if(txtDPTCD.getText().length()==3)
						M_strSQLQRY += " and CMT_CODCD='"+txtDPTCD.getText()+"'";
					if(txtEMPNO.getText().length()==4)
						M_strSQLQRY += " and CMT_CODCD in(select EP_DPTCD from HR_EPMST where EP_EMPNO='"+txtEMPNO.getText()+"')";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				else if(M_objSOURC== tblMELRS.cmpEDITR[TB_EMPNO])		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtTBEMPNO";
        			String L_ARRHDR[] = {"Code","Category"};
					
					//M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST";
					//M_strSQLQRY+= " where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
					//if(txtEMPNO.getText().length()==4)
					//	M_strSQLQRY+= " and ep_empno='"+txtEMPNO.getText()+"'";	
					//if(tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString().length()==3)
					//	M_strSQLQRY+= " and ep_dptcd='"+tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString()+"'";						
					//M_strSQLQRY+= "order by ep_empno";
					
					M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,EP_MMGRD from HR_EPMST";
					M_strSQLQRY+= " where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
					if(txtEMPNO.getText().length()==4)
						M_strSQLQRY+= " and ep_empno='"+txtEMPNO.getText()+"'";	
					if(tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString().length()==3)
						M_strSQLQRY+= " and ep_dptcd='"+tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString()+"'";
					M_strSQLQRY+= " and ep_empno not in(select SUBSTRING(cmt_codcd,1,4) from co_cdtrn where cmt_cgmtp='A01' and "+(rdbRCMBY.isSelected()?"cmt_cgstp='HR"+cl_dat.M_strCMPCD_pbst+"LRC'":"cmt_cgstp='HR"+cl_dat.M_strCMPCD_pbst+"LSN'") +(txtEXAUT.getText().length()>0?" and SUBSTRING(cmt_codcd,6,9)='"+txtEXAUT.getText()+"'":"")+")";
					M_strSQLQRY+= "order by ep_empnm";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Employee No","Employee Name","Grade"},3,"CT",new int[]{90,200,200});
        		}
				else if(M_objSOURC==tblMELRS.cmpEDITR[TB_EXAUT])
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtTBEXAUT";
        			String L_ARRHDR[] = {"Code","Short Name","Full Name"};
        			/*if(rdbRCMBY.isSelected())
					{	
						M_strSQLQRY = "Select EP_EMPNO,EP_SHRNM from HR_EPMST where EP_EMPNO in (select substr(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR01LRC')";
					}
					else
					{
						M_strSQLQRY = "Select EP_EMPNO,EP_SHRNM from HR_EPMST where EP_EMPNO in (select substr(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR01LSN')";
					}	
					*/
					M_strSQLQRY = " select distinct EP_EMPNO,EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST";
					M_strSQLQRY+= " where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by EP_EMPNM ";
					
					/*if(tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString().length()==3)
						M_strSQLQRY+= " and ep_dptcd='"+tblMELRS.getValueAt(tblMELRS.getSelectedRow(),TB_DPTCD).toString()+"'";*/
        			cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
        		}
			}
			else if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
				if(M_objSOURC==rdbRCMBY)
					rdbSSNBY.requestFocus();
				else if(M_objSOURC==rdbSSNBY)
					txtDPTCD.requestFocus();
				else if(M_objSOURC==txtDPTCD)
					txtEMPNO.requestFocus();
				else if(M_objSOURC==txtEMPNO)
					txtEXAUT.requestFocus();
				else if(M_objSOURC==txtEXAUT)
					btnDISPL.requestFocus();
				else if(M_objSOURC==txtNWAUT)
					chkAPPLY.requestFocus();
				
				else if(M_objSOURC==txtFMAUT)
					txtTOAUT.requestFocus();
				else if(M_objSOURC==txtTOAUT)
					btnCPAUT.requestFocus();
    	    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	/* method for Help*/	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD == "txtDPTCD")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDPTCD.setText(L_STRTKN1.nextToken());
				lblDPTNM.setText(L_STRTKN1.nextToken());
			}	
			else if(M_strHLPFLD == "txtEMPNO")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtEMPNO.setText(L_STRTKN1.nextToken());
				lblEMPNM.setText(L_STRTKN1.nextToken());
			}	
			else if(M_strHLPFLD == "txtEXAUT")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtEXAUT.setText(L_STRTKN1.nextToken());
				lblEXAUT.setText(L_STRTKN1.nextToken());
			}
			else if(M_strHLPFLD == "txtFMAUT")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtFMAUT.setText(L_STRTKN1.nextToken());
				lblFMAUT.setText(L_STRTKN1.nextToken());
			}
			else if(M_strHLPFLD == "txtTOAUT")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtTOAUT.setText(L_STRTKN1.nextToken());
				lblTOAUT.setText(L_STRTKN1.nextToken());
			}
			else if(M_strHLPFLD == "txtNWAUT")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtNWAUT.setText(L_STRTKN1.nextToken());
				lblNWAUT2.setText(L_STRTKN1.nextToken());
			}			
			else if(M_strHLPFLD == "txtTBDPTCD")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtTB_DPTCD.setText(L_STRTKN1.nextToken());
				//tblMELRS.setValueAt(L_STRTKN1.nextToken(),tblMELRS.getSelectedRow(),TB_DPTCD);
				tblMELRS.setValueAt(L_STRTKN1.nextToken(),tblMELRS.getSelectedRow(),TB_DPTNM);
				tblMELRS.setValueAt(new Boolean(true),tblMELRS.getSelectedRow(),TB_CHKFL);
			}
			else if(M_strHLPFLD == "txtTBEMPNO")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtTB_EMPNO.setText(L_STRTKN1.nextToken());
				//tblMELRS.setValueAt(L_STRTKN1.nextToken(),tblMELRS.getSelectedRow(),TB_EMPNO);
				tblMELRS.setValueAt(L_STRTKN1.nextToken(),tblMELRS.getSelectedRow(),TB_EMPNM);
			}
			else if(M_strHLPFLD == "txtTBEXAUT")
			{	
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtTB_EXAUT.setText(L_STRTKN1.nextToken());
				//tblMELRS.setValueAt(L_STRTKN1.nextToken(),tblMELRS.getSelectedRow(),TB_EXAUT);
				tblMELRS.setValueAt(L_STRTKN1.nextToken(),tblMELRS.getSelectedRow(),TB_AUTNM);
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}
	
	/** Fetching Employee code for specified user login
	 */ 
	private String getEMPNO(String LP_USRCD)
	{   
        String L_strRETVL = "";
		try
		{
	
				M_strSQLQRY = "select US_EMPCD from SA_USMST where US_USRCD='"+LP_USRCD+"'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					return L_strRETVL;
				L_strRETVL = L_rstRSSET.getString("US_EMPCD");
		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"getEMPNO");
	    }
		return L_strRETVL;
	}      
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");
			if(txtEMPNO.getText().length()==0)
				lblEMPNM.setText("");
			if(txtEXAUT.getText().length()==0)
				lblEXAUT.setText("");
			if(txtNWAUT.getText().length()==0)
				lblNWAUT2.setText("");
			if(txtFMAUT.getText().length()==0)
				lblFMAUT.setText("");
			if(txtTOAUT.getText().length()==0)
				lblTOAUT.setText("");
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				setENBL(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{	
					setENBL(true);
					/*((JTextField)tblMELRS.cmpEDITR[TB_DPTNM]).setEditable(false);
					((JTextField)tblMELRS.cmpEDITR[TB_EMPNM]).setEditable(false);
					((JTextField)tblMELRS.cmpEDITR[TB_AUTNM]).setEditable(false);*/
					chkGRMOD.setVisible(true);
					chkCPAUT.setEnabled(true);
					chkCHALL.setVisible(false);
				}	
				else
				{	
					chkGRMOD.setVisible(false);
					chkAPPLY.setVisible(false);
					txtNWAUT.setVisible(false);
					lblNWAUT1.setVisible(false);	
					lblNWAUT2.setVisible(false);
				}	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{	
					setENBL(true);
					chkCPAUT.setEnabled(false);
					pnlCPAUT.setVisible(false);
					tblMELRS.setEnabled(false);
					chkCHALL.setVisible(true);
					((JCheckBox)tblMELRS.cmpEDITR[TB_CHKFL]).setEnabled(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{	
					setENBL(true);
					chkCPAUT.setEnabled(false);
					pnlCPAUT.setVisible(false);
					tblMELRS.setEnabled(false);
					chkCHALL.setVisible(false);
				}

			}	
			if(M_objSOURC == chkGRMOD)
			{
				((JCheckBox)tblMELRS.cmpEDITR[TB_CHKFL]).setEnabled(true);
				if(chkGRMOD.isSelected())
				{	
					if(txtEXAUT.getText().length()!=4)
						setMSG("Please Enter Existing Authority",'E');
					else
					{	
						txtNWAUT.setVisible(true);
						lblNWAUT1.setVisible(true);
						lblNWAUT2.setVisible(true);
						chkAPPLY.setVisible(true);
						chkAPPLY.setSelected(false);
					}
				}	
				else
				{	
					txtNWAUT.setVisible(false);
					lblNWAUT1.setVisible(false);	
					lblNWAUT2.setVisible(false);
					chkAPPLY.setVisible(false);
				}	
			}
			if(M_objSOURC == chkCPAUT)
			{
				if(chkCPAUT.isSelected())
					pnlCPAUT.setVisible(true);
				else 
					pnlCPAUT.setVisible(false);
			}
			if(M_objSOURC == chkCHALL)
			{
				if(chkCHALL.isSelected())
				{	
					for(int i=0;i<tblMELRS.getRowCount();i++)
						if(tblMELRS.getValueAt(i,TB_DPTCD).toString().length()>0)
						{	
							tblMELRS.setValueAt(new Boolean(true),i,TB_CHKFL);
						}	
				}	
				else 
				{	
					for(int i=0;i<tblMELRS.getRowCount();i++)
						if(tblMELRS.getValueAt(i,TB_DPTCD).toString().length()>0)
						{	
							tblMELRS.setValueAt(new Boolean(false),i,TB_CHKFL);
						}	
				}	
			}	
			if(M_objSOURC == chkAPPLY)
			{
				((JCheckBox)tblMELRS.cmpEDITR[TB_CHKFL]).setEnabled(true);
				if(chkAPPLY.isSelected())
				{
					if(txtEXAUT.getText().length()<4 || txtNWAUT.getText().length()<4)
					{
						setMSG("Please Enter Existing Authority and New Authority",'E');
						//return false;
					}	
					else
					{
						((JCheckBox)tblMELRS.cmpEDITR[TB_CHKFL]).setEnabled(false);
						for(int i=0;i<tblMELRS.getRowCount();i++)
							if(tblMELRS.getValueAt(i,TB_DPTCD).toString().length()>0)
							{	
								tblMELRS.setValueAt(txtNWAUT.getText(),i,TB_EXAUT);
								tblMELRS.setValueAt(lblNWAUT2.getText(),i,TB_AUTNM);
								tblMELRS.setValueAt(new Boolean(true),i,TB_CHKFL);
							}	
						
					}	
				}	
			}	
			if(M_objSOURC == rdbRCMBY)
			{	
				clrCOMP();
				lblHEADN.setText("Recommending Authority");
				tblMELRS.clrTABLE();
			}	
			if(M_objSOURC == rdbSSNBY)
			{	
				clrCOMP();
				lblHEADN.setText("Sanctioning Authority");
				tblMELRS.clrTABLE();
			}	
			if(M_objSOURC == btnDISPL)
			{
				getDATA();
			}
			if(M_objSOURC == btnCPAUT)
			{
				cpyAUTHR();
			}	
		}	
	    catch(Exception L_EA)
	    {
	        setMSG(L_EA,"Action Performed");
	    }
	}
			   
	void clrEDITR(cl_JTable tblTABLE)
	{
		tblTABLE.clrTABLE();
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}	
		
		
	/** the employees with existing Sanctioning/recommending authority are coppied to new authority.
	 *  if authority for that employee does not exist then record is inserted.
	 */
	private void cpyAUTHR()
	{
		try
		{
			if(!vldCPAUT())
				return;
			String strSQLQRY="",strSQLQRY1="";
			ResultSet rstRSSET;
			//M_strSQLQRY = "select cmt_chp01 dptcd, cmt_chp02 dptds, substr(cmt_codcd,1,4) empno, cmt_codds empnm,substr(cmt_codcd,6,4) autno, cmt_ccsvl autnm from co_cdtrn ";
			M_strSQLQRY = "select cmt_cgmtp,cmt_cgstp,SUBSTRING(cmt_codcd,1,4) empno,SUBSTRING(cmt_codcd,6,4) autno,cmt_codds,cmt_shrds,cmt_chp01,cmt_chp02,cmt_ccsvl,cmt_trnfl,cmt_stsfl,cmt_lusby,cmt_lupdt from co_cdtrn ";
			M_strSQLQRY +=" where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' and CMT_STSFL <> 'X'"; 
			M_strSQLQRY += rdbSSNBY.isSelected() ? " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' " : " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' ";
			M_strSQLQRY += " and SUBSTRING(cmt_codcd,6,4) = '"+txtFMAUT.getText()+"' ";
			M_strSQLQRY += " order by cmt_chp01,cmt_chp02,cmt_codcd";
			//System.out.println(">>M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					strSQLQRY = " select count(*) CNT from CO_CDTRN";
					strSQLQRY +=" where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' "; 
					strSQLQRY += rdbSSNBY.isSelected() ? " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' " : " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' ";
					strSQLQRY += "and cmt_codcd = '"+M_rstRSSET.getString("empno")+"_"+txtTOAUT.getText().trim()+"'";
					rstRSSET=cl_dat.exeSQLQRY1(strSQLQRY);
					//System.out.println(strSQLQRY);
					if(rstRSSET!=null && rstRSSET.next())
					{
						if(rstRSSET.getInt("CNT")>0)
						{	
							System.out.println("Record Already Exist");
						}
						else
						{	
							strSQLQRY1 = " insert into CO_CDTRN(cmt_cgmtp,cmt_cgstp,cmt_codcd,cmt_codds,cmt_shrds,cmt_chp01,cmt_chp02,cmt_ccsvl,cmt_trnfl,cmt_stsfl,cmt_lusby,cmt_lupdt) ";
							strSQLQRY1 += " values (";
							strSQLQRY1 += "'"+M_rstRSSET.getString("cmt_cgmtp")+"',";
							strSQLQRY1 += "'"+M_rstRSSET.getString("cmt_cgstp")+"',";
							strSQLQRY1 += "'"+M_rstRSSET.getString("empno")+"_"+txtTOAUT.getText().trim()+"',";
							strSQLQRY1 += "'"+M_rstRSSET.getString("cmt_codds")+"',";
							strSQLQRY1 += "'"+M_rstRSSET.getString("cmt_shrds")+"',";
							strSQLQRY1 += "'"+M_rstRSSET.getString("cmt_chp01")+"',";
							strSQLQRY1 += "'"+M_rstRSSET.getString("cmt_chp02")+"',";
							strSQLQRY1 += "'"+lblTOAUT.getText().trim()+"',";
							strSQLQRY1 += getUSGDTL("CMT_",'I',"0")+")";
							//System.out.println(">>>>insert>>>>"+strSQLQRY1);
							cl_dat.M_flgLCUPD_pbst = true;
							cl_dat.exeSQLUPD(strSQLQRY1 ,"setLCLUPD");
						}
					}
					else
					{
						setMSG("Error in exeADDREC()",'E');
					}					
				}	  
			}
			cl_dat.M_flgLCUPD_pbst = true;
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
			   	JOptionPane.showMessageDialog(this,"Record Copied Successfully ","Error",JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
			   	JOptionPane.showMessageDialog(this,"Error in Copying data ","Error",JOptionPane.INFORMATION_MESSAGE);
			    setMSG("Error in updating data..",'E');
			}
			
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"cpyAUTHR()");
	    }
			
	}	
	
	
	private void getDATA()
	{
		try
		{
			int L_CNT=0;
			intCNT=0;
			clrEDITR(tblMELRS);
			M_strSQLQRY = "select cmt_chp01 dptcd, cmt_chp02 dptds, SUBSTRING(cmt_codcd,1,4) empno, cmt_codds empnm,SUBSTRING(cmt_codcd,6,4) autno, cmt_ccsvl autnm from co_cdtrn ";
			M_strSQLQRY +=" where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' and CMT_STSFL <> 'X'"; 
			M_strSQLQRY += rdbSSNBY.isSelected() ? " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' " : " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' ";
			if(txtDPTCD.getText().length()==3)
				M_strSQLQRY += " and cmt_chp01 = '"+txtDPTCD.getText()+"' ";
			if(txtEMPNO.getText().length()==4)
				M_strSQLQRY += " and SUBSTRING(cmt_codcd,1,4) = '"+txtEMPNO.getText()+"' ";
			if(txtEXAUT.getText().length()==4)
				M_strSQLQRY += " and SUBSTRING(cmt_codcd,6,4) = '"+txtEXAUT.getText()+"' ";
				//M_strSQLQRY += " and substr(cmt_codcd,6,4) = '"+txtEXAUT.getText()+"' ";
			M_strSQLQRY += "  order by cmt_chp01,cmt_chp02,cmt_codcd";
			//System.out.println(">>M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					tblMELRS.setValueAt(M_rstRSSET.getString("dptcd"),L_CNT,TB_DPTCD);
					tblMELRS.setValueAt(M_rstRSSET.getString("dptds"),L_CNT,TB_DPTNM);
					tblMELRS.setValueAt(M_rstRSSET.getString("empno"),L_CNT,TB_EMPNO);
					tblMELRS.setValueAt(M_rstRSSET.getString("empnm"),L_CNT,TB_EMPNM);
					tblMELRS.setValueAt(M_rstRSSET.getString("autno"),L_CNT,TB_EXAUT);
					tblMELRS.setValueAt(M_rstRSSET.getString("autnm"),L_CNT,TB_AUTNM);
					L_CNT++;
					intCNT++;
				}	  
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getDATA()");
	    }
			
	}	
	
    	
	/* method to save data   */
	void exeSAVE()
    {
		int P_intROWNO;
       try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			
			for(P_intROWNO=0;P_intROWNO<tblMELRS.getRowCount();P_intROWNO++)
			{
				if(tblMELRS.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
				{
					if(!vldDATA(P_intROWNO))
					{
						return;
					}
				}
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
			{
			    for(P_intROWNO=0;P_intROWNO<tblMELRS.getRowCount();P_intROWNO++)
				{
					if(tblMELRS.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
					{
						exeINSREC(P_intROWNO);
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
			    for(P_intROWNO=0;P_intROWNO<tblMELRS.getRowCount();P_intROWNO++)
				{
					if(tblMELRS.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
					{
						exeDELREC(P_intROWNO);
					}
				}
			}

			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				clrCOMP();
				tblMELRS.clrTABLE();			
				setMSG("record updated successfully",'N');
			}
			else
			{
			   	JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
			    setMSG("Error in updating data..",'E');
			}

        }
        catch(Exception L_EX)
        {
           setMSG(L_EX,"exeSAVE"); 
        }
    }
    
	private boolean vldCPAUT()
	{
		try
		{
			if(txtFMAUT.getText().length()<4)
			{
				setMSG("Enter Authority",'E');
				txtFMAUT.requestFocus();
				return false;
			}	
			if(txtTOAUT.getText().length()<4)
			{
				setMSG("Enter Authority",'E');
				txtTOAUT.requestFocus();
				return false;
			}				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA()");
		}
		return true;
	}							 
	private  boolean vldDATA(int P_intROWNO)
	{
		try
		{
		   if(tblMELRS.getValueAt(P_intROWNO,TB_DPTCD).toString().equals(""))
		   {
				setMSG("Please Enter Department Code in the Table",'E');
				return false;
		   }
		   if(tblMELRS.getValueAt(P_intROWNO,TB_DPTNM).toString().equals(""))
		   {
				setMSG("Please Enter Department Name in the Table",'E');
				return false;
		   }
   		   if(tblMELRS.getValueAt(P_intROWNO,TB_EMPNO).toString().equals(""))
		   {
				setMSG("Please Enter Employee No in the Table",'E');
				return false;
		   }
		   if(tblMELRS.getValueAt(P_intROWNO,TB_EMPNM).toString().equals(""))
		   {
				setMSG("Please Enter Employee Name in the Table",'E');
				return false;
		   }
		   if(tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString().equals(""))
		   {
				setMSG("Please Enter Code Of Authority in the Table",'E');
				return false;
		   }
		   if(tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString().equals(""))
		   {
				setMSG("Please Enter Name Of Authority in the Table",'E');
				return false;
		   }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA()");
		}
		return true;
	}

	
    /* On Save Button click data is inserted or updated into the respective tables */
	/** if chkAPPLY is selected then only Modification can be done.
	 *		if New authority for the employee is already exist in the CO_CDTRN then 1st del Existing Authority and then modify New Authority
	 *		otherwise update authority to the new authority directly
	 *  if chkAPPLY is not selected then addn or modification is done.
	 */
	private void exeINSREC(int P_intROWNO)
	{ 
	  try
	  {
		  if(chkGRMOD.isSelected() && chkAPPLY.isSelected())
		  {
				M_strSQLQRY = " select count(*) CNT from CO_CDTRN";
				M_strSQLQRY +=" where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' "; 
				M_strSQLQRY += rdbSSNBY.isSelected() ? " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' " : " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' ";
				M_strSQLQRY += "and cmt_codcd = '"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNO).toString()+"_"+tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString()+"'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				if(M_rstRSSET!=null && M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt("CNT")>0)
					{	
						System.out.println("*********inside COMMON MOD (key value exist)*************");
						exeDELREC(P_intROWNO);
						exeMODREC(P_intROWNO,tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString());			  
					}
					else
					{	
						System.out.println("*********inside COMMON MOD (key value does not exist)*************");
						exeMODREC(P_intROWNO,txtEXAUT.getText().trim());			  
					}	
				}
				else
				{
					setMSG("Error in exeADDREC()",'E');
				}
		  }
		  else
		  {			
				M_strSQLQRY = " select count(*) CNT from CO_CDTRN";
				M_strSQLQRY +=" where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' "; 
				M_strSQLQRY += rdbSSNBY.isSelected() ? " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' " : " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' ";
				M_strSQLQRY += "and cmt_codcd = '"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNO).toString()+"_"+tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString()+"'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				if(M_rstRSSET!=null && M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt("CNT")>0)
					{	
						//System.out.println("*********inside MOD*************");
						exeMODREC(P_intROWNO,tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString());
					}
					else
					{	
						//System.out.println("*********inside ADD*************");
						exeADDREC(P_intROWNO);
					}
				}
				else
				{
					setMSG("Error in exeADDREC()",'E');
				}
		  }	
	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeINSREC()");		
	  }
	}

	private void exeADDREC(int P_intROWNO)
	{
		try
		{
			String[] strARRAY=new String[4];
			M_strSQLQRY = " insert into CO_CDTRN(cmt_cgmtp,cmt_cgstp,cmt_codcd,cmt_codds,cmt_shrds,cmt_chp01,cmt_chp02,cmt_ccsvl,cmt_trnfl,cmt_stsfl,cmt_lusby,cmt_lupdt) ";
			M_strSQLQRY += " values (";
			M_strSQLQRY += "'A"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += rdbSSNBY.isSelected() ? "'HR"+cl_dat.M_strCMPCD_pbst+"LSN'," : "'HR"+cl_dat.M_strCMPCD_pbst+"LRC',";
			M_strSQLQRY += "'"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNO).toString()+"_"+tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString()+"',";
			M_strSQLQRY += "'"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNM).toString()+"',";
			strARRAY=tblMELRS.getValueAt(P_intROWNO,TB_EMPNM).toString().split(" ");	//to enter Initial of EMP as a short desc
			M_strSQLQRY += "'"+strARRAY[1].charAt(0)+strARRAY[1].charAt(2)+strARRAY[0].charAt(0)+"',";
			M_strSQLQRY += "'"+tblMELRS.getValueAt(P_intROWNO,TB_DPTCD).toString()+"',";
			M_strSQLQRY += "'"+tblMELRS.getValueAt(P_intROWNO,TB_DPTNM).toString()+"',";
			M_strSQLQRY += "'"+tblMELRS.getValueAt(P_intROWNO,TB_AUTNM).toString()+"',";
			M_strSQLQRY += getUSGDTL("CMT_",'I',"0")+")";
			//System.out.println(">>>>insert>>>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception L_EX)
		{
		   setMSG(L_EX,"exeADDREC()");		
		}	
	}
	
	private void exeMODREC(int P_intROWNO,String strAUTNO)
	{
	  try
	  {
		  M_strSQLQRY = " update CO_CDTRN set";
		  M_strSQLQRY +=" cmt_codcd = '"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNO).toString()+"_"+tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString()+"',";
		  M_strSQLQRY +=" cmt_chp01	= '"+tblMELRS.getValueAt(P_intROWNO,TB_DPTCD).toString()+"',";
		  M_strSQLQRY +=" cmt_chp02	= '"+tblMELRS.getValueAt(P_intROWNO,TB_DPTNM).toString()+"',";
		  M_strSQLQRY +=" cmt_codds	= '"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNM).toString()+"',";
		  M_strSQLQRY +=" cmt_ccsvl	= '"+tblMELRS.getValueAt(P_intROWNO,TB_AUTNM).toString()+"',";
		  M_strSQLQRY +=" cmt_stsfl	= '0',";
		  M_strSQLQRY +=" cmt_lusby = '"+cl_dat.M_strUSRCD_pbst+"',";
		  M_strSQLQRY +=" cmt_lupdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
		  M_strSQLQRY +=" where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' "; 
		  M_strSQLQRY += rdbSSNBY.isSelected() ? " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' " : " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' ";
		  M_strSQLQRY += "and cmt_codcd = '"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNO).toString()+"_"+strAUTNO+"'";
		  //System.out.println(">>>>update>>>>"+M_strSQLQRY);
		  cl_dat.M_flgLCUPD_pbst = true;
		  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
      }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeMODREC()");		
	  }

	}	
	
	
	private void exeDELREC(int P_intROWNO)
	{ 
	  try
	  {
		  M_strSQLQRY = " update CO_CDTRN set CMT_STSFL = 'X' ";
		  M_strSQLQRY +=" where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' "; 
		  M_strSQLQRY += rdbSSNBY.isSelected() ? " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LSN' " : " and CMT_CGSTP = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' ";
		  M_strSQLQRY += "and cmt_codcd = '"+tblMELRS.getValueAt(P_intROWNO,TB_EMPNO).toString()+"_"+tblMELRS.getValueAt(P_intROWNO,TB_EXAUT).toString()+"'";
		  //System.out.println(">>>>delete>>>>"+M_strSQLQRY);
		  cl_dat.M_flgLCUPD_pbst = true;
		  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
 	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELREC");		
	  }
	}  
	
	private void exeDELREC1(int P_intROWNO)
	{ 
	  try
	  {
 	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELREC1");		
	  }
	}  
}

