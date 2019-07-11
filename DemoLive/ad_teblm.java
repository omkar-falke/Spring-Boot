/*
System Name : Administration System.
Program Name : Bill Transactions Entry
Source Directory : f:\source\splerp2\hr_telve..java                        
Executable Directory : f:\exec\splerp2\hr_telve.class

List of tables used :
Table Name		Primary key							Operation done
													Insert	Update	   Query    Delete	
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - 
AD_BLMST		BL_CMPCD,BL_BILTP,BL_EMPNO,			/		/		    /	     /
				BL_BILNO,BL_SPRCD,BL_TRNDT   
-------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name		Column Name		Table name		Type/Size		Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - 
cmbBILTP		BL_BILTP		AD_BLMST		Varchar(4)		Transaction Type
txtPAYDT		BL_PAYDT		AD_BLMST		Date			Transaction Date
txtSPRCD		BL_SPRCD		AD_BLMST		Varchar(30)  	Service Provider
txtEMPNO		BL_EMPNO		AD_BLMST		Varchar(5)  	Employee No
txtEMPNM 		BL_EMPNM		AD_BLMST		Varchar(50) 	Employee Name
txtDESGN		BL_DESGN		AD_BLMST		varchar(10)  	Employee Designation
txtMMGRD		BL_MMGRD		AD_BLMST		Varchar(10)  	Employee Grade
txtDPTCD		BL_DPTCD		AD_BLMST		Varchar(3)  	Department code
txtDPTNM		BL_DPTNM		AD_BLMST		Varchar(20)  	Department Name
txtSTRDT		BL_STRDT		AD_BLMST		Date			From Date
txtENDDT		BL_ENDDT		AD_BLMST		Date			To Date
txtBILDT		BL_BILDT		AD_BLMST		Date			Bill Date
txtBILNO 		BL_BILNO		AD_BLMST		Varchar(15) 	Bill No
txtBILAM		BL_BILAM		AD_BLMST		decimal(12,2)	Bill  Amount
txtEPHRF		BL_EPHRF		AD_BLMST		decimal(12)		Phone no
txtDSTQT		BL_DSTQT		AD_BLMST		decimal(6,0)	Travel Distance
txtSRCLC 		BL_SRCLC		AD_BLMST		Varchar(15)		From Source
txtDSTLC 		BL_DSTLC		AD_BLMST		Varchar(15)		To Destination
txtTRNQT		BL_TRNQT		AD_BLMST		decimal(5)		Quantity
txtUOMCD		BL_UOMCD		AD_BLMST		Varchar(3,0)	Unit of Measurement
txtTGBQT		BL_TGBQT		AD_BLMST		decimal(6,0)	Traffic in GB
txtESTVL		BL_ESTVL		AD_BLMST		decimal(10,0)	Estimated Cost
----------------------------------------------------------------------------------------
List of fields with help facility : 
Field Name	Display Description		Display Columns		Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - 
txtEMPNO	Employee No				 	EP_EMPNO			HR_EPMST
txtEMPNM 	Employee Name				EP_EMPNM			HR_EPMST
txtDESGN	Employee Designation		EP_DESGN			HR_EPMST
txtMMGRD	Employee Grade				EP_MMGRD			HR_EPMST
txtDPTCD	Department code				EP_DPTCD		    HR_EPMST
txtDPTNM	Department Name				EP_DPTNM			HR_EPMST
----------------------------------------------------------------------------------------
B>Validations & Other Information:</B> 
  	- Transaction date should not be greater than current date.
 	- From Date must be Smaller Than Or Equal to To Date.
 	- To Date must not be Grater Than Todays Date.
    - To Date must be greater than Or Equal to From Date.
    - Before saving Bill transactions,it should be validated.
   
</I> 
Requirements:
	- While fetching the emp no in emp no text box,it will be fetch from hp_epmst table.
	- After selecting specified Employee Number from employee master and it will be displayed in Employee number Text Box.
	- While saving the entries,it will be saved in AD_BLMST table.
	- if user enter invalid data,then set message for valid entry for transaction date,from date,to date & emp no.
	- User will not make changes bill number column & bill date column as desired & Employee data.
	- While fetching the UOM code in text box ,it will be fetch from co_cdtrn table.
	- While entering all Bill transactions data,click on save button to save data in respective database table.
	- User can delete & modify selected data .
	- Bill no,bill date,bill type,service provider,emp no are protected from modification. 

*/
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.JTable;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java .util.Hashtable;
class ad_teblm extends cl_pbase implements KeyListener, FocusListener
{								 
	private JTextField txtTRNDT; 
	private JTextField txtTRNNO;
	private JTextField txtEMPNO;
	private JTextField txtEMPNM; 
	private JTextField txtDESGN;
	private JTextField txtMMGRD;
	private JTextField txtDPTCD;
	private JTextField txtDPTNM;
	
	private JTextField txtSTRDT; 
	private JTextField txtENDDT;
	private JTextField txtSPRCD; 
	private JTextField txtBILNO;
	private JTextField txtBILDT;
	private JTextField txtBILAM;
	private JTextField txtESTVL;
	
	private JTextField txtCHQNO; 
	private JTextField txtCHQDT;
	private JTextField txtBFWDT; 
	private JTextField txtCRCDT;
	private JTextField txtCDSDT;
	private JTextField txtREMDS;
	
	private JTextField txtEPHRF; 
	private JTextField txtDSTQT;
	private JTextField txtSRCLC; 
	private JTextField txtDSTLC;
	private JTextField txtTRNQT;
	private JTextField txtUOMCD;
	private JTextField txtTGBQT;
	
	private JLabel lblEPHRF; 
	private JLabel lblDSTQT;
	private JLabel lblSRCLC; 
	private JLabel lblDSTLC;
	private JLabel lblTRNQT;
	private JLabel lblUOMCD;
	private JLabel lblTGBQT;
	
	private JComboBox cmbBILTP;
	private JComboBox cmbBILTP1;
	private Vector<String> vtrBILTP;
	private INPVF oINPVF;
	
	public ad_teblm()
	{
    	super(2);
    	try
    	{
    		setMatrix(20,8);
    		
    		add(new JLabel("Main Bill Type"),3,3,1,1.4,this,'R');
    		add(cmbBILTP = new JComboBox(),3,4,1,1.5,this,'L');
    		add(new JLabel("Sub Bill Type"),3,6,1,1.4,this,'R');
    		add(cmbBILTP1 = new JComboBox(),3,7,1,1.5,this,'L');
    		
    		/*add(new JLabel("Transaction No"),4,3,1,1.4,this,'R'); 
    		add(txtTRNNO = new TxtLimit(8),4,4,1,1,this,'L');
    		add(new JLabel("Transaction Date"),4,6,1,1.4,this,'R'); 
    		add(txtTRNDT = new TxtDate(),4,7,1,1,this,'L');*/
    		add(new JLabel("Transaction Date"),4,3,1,1.4,this,'R'); 
    		add(txtTRNDT = new TxtDate(),4,4,1,1,this,'L');
    		add(new JLabel("Transaction No"),4,6,1,1.4,this,'R'); 
    		add(txtTRNNO = new TxtLimit(8),4,7,1,1,this,'L');
		
    		vtrBILTP  = new Vector<String>();
		
    		/*add(new JLabel("Employee Number"),5,3,1,1.4,this,'R');
    		add(txtEMPNO = new TxtLimit(5),5,4,1,1,this,'L');
    		add(new JLabel("Employee Name"),5,6,1,1.4,this,'R');
    		add(txtEMPNM = new TxtLimit(50),5,7,1,1,this,'L');
		
    		add(new JLabel("Employee Grade"),6,3,1,1.4,this,'R');
    		add(txtMMGRD = new TxtLimit(10),6,4,1,1,this,'L');
    		add(new JLabel("Employee Designation"),6,6,1,1.4,this,'R');
    		add(txtDESGN = new TxtLimit(10),6,7,1,1,this,'L'); 
		
    		add(new JLabel("Department Code"),7,3,1,1.4,this,'R');
    		add(txtDPTCD = new TxtLimit(3),7,4,1,1,this,'L');
    		add(new JLabel("Department Name"),7,6,1,1.4,this,'R');
    		add(txtDPTNM = new TxtLimit(20),7,7,1,1,this,'L');*/
    		
    		
    		add(new JLabel("Department Code"),5,3,1,1.4,this,'R');
    		add(txtDPTCD = new TxtLimit(3),5,4,1,1,this,'L');
    		add(new JLabel("Department Name"),5,6,1,1.4,this,'R');
    		add(txtDPTNM = new TxtLimit(20),5,7,1,1.8,this,'L');
    		
    		add(new JLabel("Employee Number"),6,3,1,1.4,this,'R');
    		add(txtEMPNO = new TxtLimit(5),6,4,1,1,this,'L');
    		add(new JLabel("Employee Name"),6,6,1,1.4,this,'R');
    		add(txtEMPNM = new TxtLimit(50),6,7,1,1.8,this,'L');
		
    		add(new JLabel("Employee Grade"),7,3,1,1.4,this,'R');
    		add(txtMMGRD = new TxtLimit(10),7,4,1,1,this,'L');
    		add(new JLabel("Employee Designation"),7,6,1,1.4,this,'R');
    		add(txtDESGN = new TxtLimit(10),7,7,1,1,this,'L');
    		
    		add(new JLabel("From Date"),8,3,1,1.4,this,'R');
    		add(txtSTRDT  = new TxtDate(),8,4,1,1,this,'L');
    		add(new JLabel("To Date"),8,6,1,1.4,this,'R');
    		add(txtENDDT = new TxtDate(),8,7,1,1,this,'L');
    		
    		add(new JLabel("Service Provider"),9,3,1,1.4,this,'R');
    		add(txtSPRCD = new TxtLimit(20),9,4,1,1,this,'L');
    		add(new JLabel("Bill Date"),9,6,1,1.4,this,'R');
    		add(txtBILDT= new TxtDate(),9,7,1,1,this,'L');
    		
    		add(new JLabel("Bill No"),10,3,1,1.4,this,'R');
    		add(txtBILNO = new TxtLimit(10),10,4,1,1,this,'L'); 
    		add(new JLabel("Bill Amount"),10,6,1,1.4,this,'R');
    		add(txtBILAM = new TxtNumLimit(10.2),10,7,1,1,this,'L');
    		
    		add(new JLabel("Bill Pass Amount"),11,3,1,1.4,this,'R');
    		add(txtESTVL = new TxtNumLimit(10.2),11,4,1,1,this,'L');
    		add(new JLabel("Bill Forward Date"),11,6,1,1.4,this,'R');
    		add(txtBFWDT = new TxtDate(),11,7,1,1,this,'L');
    		
    		add(new JLabel("Cheque Receive Date"),12,3,1,1.4,this,'R');
    		add(txtCRCDT = new TxtDate(),12,4,1,1,this,'L');
    		add(new JLabel("Cheque No"),12,6,1,1.4,this,'R');
    		add(txtCHQNO = new TxtLimit(15),12,7,1,1,this,'L');
    		
    		add(new JLabel("Cheque Date"),13,3,1,1.4,this,'R');
    		add(txtCHQDT = new TxtDate(),13,4,1,1,this,'L');
    		add(new JLabel("Cheque Dispatch Date"),13,6,1,1.4,this,'R');
    		add(txtCDSDT = new TxtDate(),13,7,1,1,this,'L');
    		
    		add(new JLabel("Remark"),14,3,1,1.4,this,'R');
    		add(txtREMDS= new TxtLimit(25),14,4,1,4,this,'L');
    		
    		add(lblEPHRF=new JLabel("Phone No"),15,3,1,1.4,this,'R');
    		add(txtEPHRF = new TxtLimit(12),15,4,1,1,this,'L');
    		add(lblTGBQT=new JLabel("Traffic(GB)"),15,6,1,1.4,this,'R');
    		add(txtTGBQT = new TxtNumLimit(6.0),15,7,1,1,this,'L');
    		
    		add(lblSRCLC=new JLabel("Source"),15,3,1,1.4,this,'R');
    		add(txtSRCLC = new TxtLimit(15),15,4,1,1,this,'L');
    		add(lblDSTLC=new JLabel("Destination"),15,6,1,1.4,this,'R');
    		add(txtDSTLC = new TxtLimit(15),15,7,1,1,this,'L');
    		
    		add(lblTRNQT=new JLabel("Quantity"),15,3,1,1.4,this,'R');
    		add(txtTRNQT = new TxtNumLimit(5.3),15,4,1,1,this,'L');
    		add(lblUOMCD=new JLabel("UOM"),15,6,1,1.4,this,'R');
    		add(txtUOMCD = new TxtLimit(3),15,7,1,1,this,'L');
    		
    		add(lblDSTQT=new JLabel("Travel Distance"),16,3,1,1.4,this,'R');
    		add(txtDSTQT = new TxtNumLimit(6.0),16,4,1,1,this,'L');
    		
    		oINPVF=new INPVF();
    		txtTRNNO.setInputVerifier(oINPVF);
    		txtDPTCD.setInputVerifier(oINPVF);
    		txtEMPNO.setInputVerifier(oINPVF);
    		txtTRNDT.setInputVerifier(oINPVF);
    		txtUOMCD.setInputVerifier(oINPVF);
    		txtSTRDT.setInputVerifier(oINPVF);
    		txtENDDT.setInputVerifier(oINPVF);
    		//txtSPRCD.setInputVerifier(oINPVF);
    		//txtBILNO.setInputVerifier(oINPVF);
    		
    		//txtEMPNO.addKeyListener(this);
    		txtTRNDT.addKeyListener(this);
    		//txtTRNNO.addKeyListener(this);
    		txtSTRDT.addKeyListener(this);
    		txtENDDT.addKeyListener(this);
    		txtSPRCD.addKeyListener(this);
    		txtBILDT.addKeyListener(this);
    		txtBILAM.addKeyListener(this);
    		txtESTVL.addKeyListener(this);
    		txtEPHRF.addKeyListener(this); 
    		txtTGBQT.addKeyListener(this);
    		txtDSTQT.addKeyListener(this);
    		txtSRCLC.addKeyListener(this);
    		txtDSTLC.addKeyListener(this);
    		txtTRNQT.addKeyListener(this);
    		txtUOMCD.addFocusListener(this);
    		
    		setENBL(false); 
    		setVisible(true);
    		
    		M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='ADXXBTP'";
    		//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
    		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    		if(M_rstRSSET != null)
    		{
    			while( M_rstRSSET.next())
    			{
    				//System.out.println(M_rstRSSET.getString("cmt_codcd"));
    				M_rstRSSET.getString("cmt_codds");				
					vtrBILTP.add(M_rstRSSET.getString("cmt_codcd")+"|"+M_rstRSSET.getString("cmt_codds"));
    			}
    		}
    		for(int i=0;i<vtrBILTP.size();i++)
    		{
    			if(vtrBILTP.get(i).toString().substring(2,4).equals("00"))
    				cmbBILTP.addItem(vtrBILTP.get(i).toString().substring(5));
    		}
    	}
    	catch(Exception EXE)
    	{
    		System.out.println("inside constructor"+EXE);
    	}
	}
    /**
	 * Super Class Method overrided to enable & disable the Components.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);		 
		txtEMPNM.setEnabled(false);	
		txtMMGRD.setEnabled(false);
		txtDESGN.setEnabled(false);
		txtDPTNM.setEnabled(false);
		txtTRNDT.setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{ 
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					txtTRNDT.requestFocus();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						if(txtTRNDT.getText().trim().length()==0)
							txtTRNDT.setText(cl_dat.M_strLOGDT_pbst);
					}
					else
						txtTRNDT.setText("");
				}
				else
					setENBL(false);
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					txtTRNNO.setEnabled(true);
					txtTRNNO.requestFocus();
					setMSG(" For Modification first Enter Transaction No. or Press F1 to Select form List..",'N');
				}
				else
				{
					txtTRNNO.setEnabled(false);
					txtDPTCD.requestFocus();
					setMSG("Enter the Department Code or Press F1 to Select form List..",'N');					
				}
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					clrCOMP();
					txtTRNDT.setEnabled(false);
					txtEMPNM.setEnabled(false); 
					txtEMPNO.setEnabled(false);
					txtDESGN.setEnabled(false);
					txtMMGRD.setEnabled(false);
					txtDPTCD.setEnabled(false);
					txtDPTNM.setEnabled(false);
					txtSTRDT.setEnabled(false); 
					txtENDDT.setEnabled(false);
					txtSPRCD.setEnabled(false); 
					txtBILDT.setEnabled(false);
					txtBILNO.setEnabled(false);
					txtBILAM.setEnabled(false);
					txtESTVL.setEnabled(false);
					txtCHQNO.setEnabled(false); 
					txtCHQDT.setEnabled(false);
					txtBFWDT.setEnabled(false); 
					txtCRCDT.setEnabled(false);
					txtCDSDT.setEnabled(false);
					txtREMDS.setEnabled(false);
					txtEPHRF.setEnabled(false); 
					txtDSTQT.setEnabled(false);
					txtSRCLC.setEnabled(false); 
					txtDSTLC.setEnabled(false);
					txtTRNQT.setEnabled(false);
					txtUOMCD.setEnabled(false);
					txtTGBQT.setEnabled(false);
				}
			}
			
			else if(M_objSOURC == cmbBILTP) 
			{
				String L_strBILTP=cmbBILTP.getSelectedItem().toString();//store main combo option in string
				//System.out.println("L_strBILTP>>"+L_strBILTP);
				for(int i=0;i<vtrBILTP.size();i++)
				{
					if(vtrBILTP.get(i).toString().substring(5).equals(L_strBILTP))//compare vector to this  main combo option
					{	
						String L_strBILCD = vtrBILTP.get(i).toString().substring(0,2);						
						for (int k=cmbBILTP1.getItemCount()-1;k>=0;k--)
						{
							cmbBILTP1.removeItemAt(k);//remove  previous cmbBILTP1 item 
						}
						for(int j=0;j<vtrBILTP.size();j++)
						{
							//System.out.println("L_strBILCD>>"+L_strBILCD);
							if(vtrBILTP.get(j).toString().substring(0,2).equals(L_strBILCD) && !vtrBILTP.get(j).toString().substring(2,4).equals("00"))	
								cmbBILTP1.addItem(vtrBILTP.get(j).toString().substring(5));
						}
					}
				}
				if(cmbBILTP.getSelectedIndex()==0)
				{
					lblEPHRF.setVisible(true);
					txtEPHRF.setVisible(true);
					lblTGBQT.setVisible(true);
					txtTGBQT.setVisible(true);
					lblSRCLC.setVisible(false);
					txtSRCLC.setVisible(false);
					lblDSTLC.setVisible(false);
					txtDSTLC.setVisible(false);
					lblDSTQT.setVisible(false);
					txtDSTQT.setVisible(false);
					lblTRNQT.setVisible(false);
					txtTRNQT.setVisible(false);
					lblUOMCD.setVisible(false);
					txtUOMCD.setVisible(false);
				}
				if(cmbBILTP.getSelectedIndex()==1)
				{
					lblSRCLC.setVisible(true);
					txtSRCLC.setVisible(true);
					lblDSTLC.setVisible(true);
					txtDSTLC.setVisible(true);
					lblDSTQT.setVisible(true);
					txtDSTQT.setVisible(true);
					lblEPHRF.setVisible(false);
					txtEPHRF.setVisible(false);
					lblTGBQT.setVisible(false);
					txtTGBQT.setVisible(false);
					lblTRNQT.setVisible(false);
					txtTRNQT.setVisible(false);
					lblUOMCD.setVisible(false);
					txtUOMCD.setVisible(false);
				}
				if(cmbBILTP.getSelectedIndex()==2)
				{
					lblEPHRF.setVisible(false);
					txtEPHRF.setVisible(false);
					lblTGBQT.setVisible(false);
					txtTGBQT.setVisible(false);
					lblSRCLC.setVisible(false);
					txtSRCLC.setVisible(false);
					lblDSTLC.setVisible(false);
					txtDSTLC.setVisible(false);
					lblDSTQT.setVisible(false);
					txtDSTQT.setVisible(false);
					lblTRNQT.setVisible(true);
					txtTRNQT.setVisible(true);
					lblUOMCD.setVisible(true);
					txtUOMCD.setVisible(true);
				}
				if(cmbBILTP.getSelectedIndex()==3)
				{
					lblTRNQT.setVisible(false);
					txtTRNQT.setVisible(false);
					lblUOMCD.setVisible(false);
					txtUOMCD.setVisible(false);
					lblEPHRF.setVisible(false);
					txtEPHRF.setVisible(false);
					lblTGBQT.setVisible(false);
					txtTGBQT.setVisible(false);
					lblSRCLC.setVisible(false);
					txtSRCLC.setVisible(false);
					lblDSTLC.setVisible(false);
					txtDSTLC.setVisible(false);
					lblDSTQT.setVisible(false);
					txtDSTQT.setVisible(false);
				}	
				
				if(cmbBILTP.getSelectedIndex()>=0)
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
							txtTRNNO.requestFocus();
					else
						txtDPTCD.requestFocus();
					clrCOMP();
					
				}
			}                                     
			else if(M_objSOURC == cmbBILTP1) 
			{
				if(cmbBILTP1.getSelectedIndex()>=0)
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						txtTRNNO.requestFocus();
					else
						txtDPTCD.requestFocus();
				clrCOMP();
				
			}
			else if(M_objSOURC == txtEMPNO) 
			{
				if(txtEMPNO.getText().length()==0)
				{
					txtEMPNM.setText("");
					txtDESGN.setText("");
					txtMMGRD.setText("");
				}
			}
				
		}
		catch(Exception L_EX) 
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == txtTRNDT)			
			setMSG("Enter Transaction Date..",'N');	
		else if(M_objSOURC == txtDPTCD)			
			setMSG("Enter the Department Code or Press F1 to Select form List..",'N');
		else if(M_objSOURC == txtEMPNO)			
			setMSG("Enter the Employee Number or Press F1 to Select form List..",'N');
		else if(M_objSOURC == txtSTRDT)			
			setMSG("Enter From Date..",'N');			
		else if(M_objSOURC == txtENDDT)			
			setMSG("Enter To Date..",'N');
		else if(M_objSOURC == txtUOMCD)
			setMSG("Enter the UOM or Press F1 to Select form List..",'N');
		else if(M_objSOURC == txtTRNNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				setMSG(" For Modification first Enter the Transaction No. or Press F1 to Select form List..",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG(" For Deletion first Enter the Transaction No. or Press F1 to Select form List..",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				setMSG(" For Enquiry first Enter the Transaction No. or Press F1 to Select form List..",'N');
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
				if(M_objSOURC==txtDPTCD)		
				{
					M_strHLPFLD = "txtDPTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXDPT'";
					//if(txtDPTCD.getText().length() >0)
					//	M_strSQLQRY += " AND CMT_CODCD like '"+txtDPTCD.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Department code","Department Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
				else if(M_objSOURC==txtEMPNO)		
				{
					
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
						M_strHLPFLD = "txtEMPNO";
						String L_ARRHDR[] = {"Employee No","Employee Name"};
	    				M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' '+ EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
	    				if(txtDPTCD.getText().length()>0)
	    					M_strSQLQRY += " and EP_DPTCD='"+txtDPTCD.getText()+"'";
	    				M_strSQLQRY += " order by ep_empno";
	    				//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
	    				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
	    				setCursor(cl_dat.M_curDFSTS_pbst);
	    			
    			}
				else if(M_objSOURC==txtUOMCD)		
				{
					if(txtTRNQT.getText().trim().length() == 0)
					{
						setMSG("Please Enter the Quantity first..",'E');
						txtTRNQT.requestFocus();
						return;
					}
				    	cl_dat.M_flgHELPFL_pbst = true;
				    	setCursor(cl_dat.M_curWTSTS_pbst);
				    	M_strHLPFLD = "txtUOMCD";
						String L_ARRHDR[] = {"UOM Code"," UOM Description"};
	    				M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where  CMT_CGMTP ='MST' AND CMT_CGSTP = 'COXXUOM' AND isnull(CMT_STSFL,'')<>'X' ORDER BY CMT_CODCD";
						//System.out.println(">>>>txtUOMCD>>>>"+M_strSQLQRY);
	    				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
	    				setCursor(cl_dat.M_curDFSTS_pbst);
	    		}
				else if(M_objSOURC == txtTRNNO)
				{
						M_strHLPFLD = "txtTRNNO";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
						M_strSQLQRY=" SELECT BL_TRNNO,BL_PAYDT,BL_BILNO,BL_BILDT from AD_BLMST where isnull(BL_STSFL,'')<>'X'";
						for(int i=0;i<vtrBILTP.size();i++)
						{
						 if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP1.getSelectedItem().toString()))
						  {
							 M_strSQLQRY += " AND BL_BILTP like '"+vtrBILTP.get(i).toString().substring(0,4)+"%'";
						  }
						}
						if(txtTRNNO.getText().length() >0)				
							M_strSQLQRY += " AND BL_TRNNO like '"+txtTRNNO.getText().trim()+"%'";
						M_strSQLQRY += " order by BL_TRNNO";
						cl_hlp(M_strSQLQRY,3,1,new String[]{"Transaction No","Transaction Date","Bill NO","Bill Date"},4,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
				}	
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtTRNDT)
				 {
					txtTRNNO.requestFocus();
					setMSG("Enter the Transaction Number or Press F1 to Select form List..",'N');
				 }
				else if(M_objSOURC == txtTRNNO)
				 {
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))&& txtTRNNO.getText().trim().length() == 8)
						getDATA();
					else
					{
						txtDPTCD.requestFocus();
						setMSG("Enter the Department Code or Press F1 to Select form List..",'N');
					}
				}
				
				else if(M_objSOURC ==txtDPTCD)
				{
					txtEMPNO.requestFocus();
					setMSG("Enter the Employee Number or Press F1 to Select form List..",'N');
				}
				else if(M_objSOURC == txtEMPNO)
				{
					//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && txtEMPNO.getText().length()>0)
						//getDATA1();
					//if(txtEMPNO.getText().length()==0 && txtEMPNO.getText().length()>0)
						txtSTRDT.requestFocus();
						setMSG("Enter the From Date.",'N');
				}
				else if(M_objSOURC == txtSTRDT)
				{
					txtENDDT.requestFocus();
					setMSG("Enter To Date.",'N');
				}
				else if(M_objSOURC == txtENDDT)
				{
					txtSPRCD.requestFocus();
					setMSG("Enter the Service Provider.",'N');
				}
				else if(M_objSOURC == txtSPRCD)
				{
					txtBILDT.requestFocus();
					setMSG("Enter the Bill Date.",'N');
				}
				else if(M_objSOURC == txtBILDT)
				{
					txtBILNO.requestFocus();
					setMSG("Enter Bill No.",'N');
				}
				else if(M_objSOURC == txtBILNO)
				{
					txtBILAM.requestFocus();
					setMSG("Enter the Bill Amount.",'N');
				}
				else if(M_objSOURC == txtBILAM)
				{
					txtESTVL.requestFocus();
					setMSG("Enter the Bill Pass Amount.",'N');
				}
				else if(M_objSOURC == txtESTVL)
				{
					txtBFWDT.requestFocus();
					setMSG("Enter the Bill Forward Date to Acct.",'N');
				}
				else if(M_objSOURC == txtBFWDT)
				{
					txtCRCDT.requestFocus();
					setMSG("Enter the Cheque Receive Date from Acct.",'N');
				}
				else if(M_objSOURC == txtCRCDT)
				{
					txtCHQNO.requestFocus();
					setMSG("Enter Cheque No.",'N');
				}
				else if(M_objSOURC == txtCHQNO)
				{
					txtCHQDT.requestFocus();
					setMSG("Enter the Cheque Date.",'N');
				}
				else if(M_objSOURC == txtCHQDT)
				{
					txtCDSDT.requestFocus();
					setMSG("Enter Cheque Dispatch Date.",'N');
				}
				else if(M_objSOURC == txtCDSDT)
				{
					txtREMDS.requestFocus();
					setMSG("Enter the Remark.",'N');
				}
				else if(M_objSOURC == txtREMDS)
				{
					if(cmbBILTP.getSelectedIndex()==0)
					{
						txtEPHRF.requestFocus();
						setMSG("Enter the Phone No.",'N');
					}
					if(cmbBILTP.getSelectedIndex()==1)
					{
						txtSRCLC.requestFocus();
						setMSG("Enter the From Source.",'N');
					}
					if(cmbBILTP.getSelectedIndex()==2)
					{
						txtTRNQT.requestFocus();
						setMSG("Enter the Quantity.",'N');
					}
					if(cmbBILTP.getSelectedIndex()==3)
						cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else if(M_objSOURC == txtEPHRF)
				{
					txtTGBQT.requestFocus();
					setMSG("Enter the Traffic in GB.",'N');
				}
				else if(M_objSOURC == txtTGBQT)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else if(M_objSOURC == txtSRCLC)
				{
					txtDSTLC.requestFocus();
					setMSG("Enter To Destination.",'N');
				}
				else if(M_objSOURC == txtDSTLC)
				{
					txtDSTQT.requestFocus();
					setMSG("Enter the Travel Distance.",'N');
				}
				else if(M_objSOURC == txtDSTQT)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else if(M_objSOURC == txtTRNQT)
				{
					txtUOMCD.requestFocus();
					setMSG("Enter the UOM or Press F1 to Select form List..",'N');
				}
				else if(M_objSOURC == txtUOMCD)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keypressed"); 
		}
	} 
	/**
	 * Method for set data from Help Screen to textfield of Employee Number & Employee Name.*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
			      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				  txtDPTCD.setText(L_STRTKN.nextToken());
				  txtDPTNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
			else if(M_strHLPFLD.equals("txtEMPNO"))
			{
				  StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			      txtEMPNO.setText(L_STRTKN.nextToken());
				  txtEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
			else if(M_strHLPFLD.equals("txtUOMCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtUOMCD.setText(L_STRTKN.nextToken().replace('|',' '));
			}
			else if(M_strHLPFLD.equals("txtTRNNO"))
			{
			  	txtTRNNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtBILNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}
	
	/**
	 * Method to get Data from HR_EPMST  table in text box*/
	private void getDATA1() 
	{
		try
		{ 
			M_strSQLQRY= "SELECT EP_DESGN,EP_DPTCD,EP_DPTNM,EP_MMGRD from HR_EPMST";
			M_strSQLQRY+= " where  EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '"+txtEMPNO.getText()+"' AND ep_lftdt is null";
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					txtDESGN.setText(M_rstRSSET.getString("EP_DESGN"));
					txtMMGRD.setText(M_rstRSSET.getString("EP_MMGRD"));
					txtDPTCD.setText(M_rstRSSET.getString("EP_DPTCD"));
					txtDPTNM.setText(M_rstRSSET.getString("EP_DPTNM"));
				}
			}
            M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA1()"); 
		}
	}
	/**
	 * Method to get Data from ad_blmst table in text box*/
	private void getDATA() 
	{
		try
		{ 
			M_strSQLQRY= "SELECT BL_DPTCD,BL_DPTNM,BL_EMPNO,BL_EMPNM,BL_DESGN,BL_MMGRD,BL_PAYDT,BL_STRDT,BL_ENDDT,BL_BILDT,BL_SPRCD,BL_BILNO,BL_BILAM,BL_EPHRF,BL_DSTQT,";
			M_strSQLQRY+= "BL_SRCLC,BL_DSTLC,BL_TRNQT,BL_UOMCD,BL_TGBQT,BL_ESTVL,BL_REMDS,BL_CHQNO,BL_CHQDT,BL_BFWDT,BL_CRCDT,BL_CDSDT from AD_BLMST ";
		
			for(int i=0;i<vtrBILTP.size();i++)
			{
			 if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP1.getSelectedItem().toString()))
			  {
				  M_strSQLQRY += " where BL_BILTP='"+vtrBILTP.get(i).toString().substring(0,4)+"'";
			  }
			}
			M_strSQLQRY+= " AND isnull(BL_STSFL,'')<>'X' AND BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BL_TRNNO = '"+txtTRNNO.getText()+"'";
			//M_strSQLQRY+= " AND BL_PAYDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTRNDT.getText().toString()))+"' ";
			
			//System.out.println(">>>select2>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					txtEMPNO.setText(M_rstRSSET.getString("BL_EMPNO"));
					txtEMPNM.setText(M_rstRSSET.getString("BL_EMPNM"));
					txtDPTCD.setText(M_rstRSSET.getString("BL_DPTCD"));
					txtDPTNM.setText(M_rstRSSET.getString("BL_DPTNM"));
					txtDESGN.setText(M_rstRSSET.getString("BL_DESGN"));
				    txtMMGRD.setText(M_rstRSSET.getString("BL_MMGRD"));
					
				    
				    if(!(M_rstRSSET.getDate("BL_PAYDT")==null))
						txtTRNDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_PAYDT")));
					else
						txtTRNDT.setText("");
					if(!(M_rstRSSET.getDate("BL_STRDT")==null))
						txtSTRDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_STRDT")));
					else
						txtSTRDT.setText("");
					if(!(M_rstRSSET.getDate("BL_ENDDT")==null))
						txtENDDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_ENDDT")));
					else
						txtENDDT.setText("");
					if(!(M_rstRSSET.getDate("BL_BILDT")==null))
						txtBILDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_BILDT")));
					else
						txtBILDT.setText("");
					txtSPRCD.setText(nvlSTRVL(M_rstRSSET.getString("BL_SPRCD"),""));
					//txtBILNO.setText(nvlSTRVL(M_rstRSSET.getString("BL_BILNO"),""));
					txtBILAM.setText(nvlSTRVL(M_rstRSSET.getString("BL_BILAM"),""));
					txtEPHRF.setText(nvlSTRVL(M_rstRSSET.getString("BL_EPHRF"),""));
				    txtDSTQT.setText(nvlSTRVL(M_rstRSSET.getString("BL_DSTQT"),""));
					txtSRCLC.setText(nvlSTRVL(M_rstRSSET.getString("BL_SRCLC"),""));
					txtDSTLC.setText(nvlSTRVL(M_rstRSSET.getString("BL_DSTLC"),""));
					txtTRNQT.setText(nvlSTRVL(M_rstRSSET.getString("BL_TRNQT"),""));
					txtUOMCD.setText(nvlSTRVL(M_rstRSSET.getString("BL_UOMCD"),""));
					txtTGBQT.setText(nvlSTRVL(M_rstRSSET.getString("BL_TGBQT"),""));
					txtESTVL.setText(nvlSTRVL(M_rstRSSET.getString("BL_ESTVL"),""));
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("BL_REMDS"),""));
					txtCHQNO.setText(nvlSTRVL(M_rstRSSET.getString("BL_CHQNO"),""));
					
					if(!(M_rstRSSET.getDate("BL_CHQDT")==null))
						txtCHQDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_CHQDT")));
					else
						txtCHQDT.setText("");
					if(!(M_rstRSSET.getDate("BL_BFWDT")==null))
						txtBFWDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_CHQDT")));
					else
						txtBFWDT.setText("");
					if(!(M_rstRSSET.getDate("BL_CRCDT")==null))
						txtCRCDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_CRCDT")));
					else
						txtCRCDT.setText("");
					if(!(M_rstRSSET.getDate("BL_CDSDT")==null))
						txtCDSDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("BL_CDSDT")));
					else
						txtCDSDT.setText("");
				}
			}
            M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA()"); 
		}
	}
	
	/**
	 * Method to clear data*/
	void clrCOMP()
	{
		try
		{
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
				txtTRNDT.setText(""); 
			else
				txtTRNDT.setText(cl_dat.M_strLOGDT_pbst);
			txtTRNNO.setText(""); 
			txtSPRCD.setText(""); 
			txtEMPNO.setText("");
			txtEMPNM.setText("");
			txtDESGN.setText("");
			txtMMGRD.setText("");
			txtDPTCD.setText("");
			txtDPTNM.setText("");
			txtSTRDT.setText(""); 
			txtENDDT.setText("");
			txtBILNO.setText("");
			txtBILAM.setText("");
			txtBILDT.setText("");
			txtESTVL.setText("");
			txtEPHRF.setText("");
			txtDSTQT.setText(""); 
			txtSRCLC.setText("");
			txtDSTLC.setText("");
			txtTRNQT.setText("");
			txtTGBQT.setText("");
			txtUOMCD.setText("");
			txtREMDS.setText(""); 
			txtCHQNO.setText("");
			txtCHQDT.setText("");
			txtBFWDT.setText("");
			txtCRCDT.setText("");
			txtCDSDT.setText("");
			
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
	/**	this method checks for blank values in the tblBLENT
	*/	

	/** method to validate data  */
	boolean vldDATA()
	{
		try
		{
			/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			if(txtTRNDT.getText().trim().length() ==0)
    		{
				txtTRNDT.requestFocus();
    			setMSG("Enter the Transaction Date",'E');
    			return false;
    		}
			/*if(txtTRNNO.getText().trim().length() ==0)
    		{
				txtTRNNO.requestFocus();
    			setMSG("Enter the Transaction Number",'E');
    			return false;
    		}*/
			if(txtDPTCD.getText().trim().length() ==0)
    		{
				txtDPTCD.requestFocus();
    			setMSG("Enter the Department Code",'E');
    			return false;
    		}
			if(txtSTRDT.getText().trim().length() ==0)
    		{
				txtSTRDT.requestFocus();
    			setMSG("Enter From Date",'E');
    			return false;
    		}
			if(txtENDDT.getText().trim().length() ==0)
    		{
				txtENDDT.requestFocus();
    			setMSG("Enter To Date",'E');
    			return false;
    		}
			if(txtSPRCD.getText().trim().length() ==0)
    		{
				txtSPRCD.requestFocus();
    			setMSG("Enter the Service Provider",'E');
    			return false;
    		}
			if(txtBILNO.getText().trim().length() ==0)
    		{
				txtBILNO.requestFocus();
    			setMSG("Enter the Bill No",'E');
    			return false;
    		}
			if(txtBILDT.getText().trim().length() ==0)
    		{
				txtBILDT.requestFocus();
    			setMSG("Enter the Bill Date",'E');
    			return false;
    		}
			if(txtBILAM.getText().trim().length() ==0)
    		{
				txtBILAM.requestFocus();
    			setMSG("Enter the Bill Amount",'E');
    			return false;
    		}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is vldDATA");
			return false;
		}	
		return true;
	}	
    
	/**
	 * Method to Save Records*/
	void exeSAVE()
	{
		
		try
		{
			if(!vldDATA()) 
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
		  	{
				exeADDREC();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
				exeMODREC();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
				exeDELREC();
			}
			
			if(cl_dat.exeDBCMT("exeSAVE")) 
			{
				clrCOMP();
			}
		}
		catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeSAVE"); 
	    }
	} 
	/**
	 *  On Save Button click data is inserted into the respective tables */
	private void exeADDREC()
	{ 
		try
		{
			//this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			String L_strBILTP="";
			String strPAYNO="";
			int L_intTSTNO =0;
			
			String L_strTEMP1 = "";		
			for(int i=0;i<vtrBILTP.size();i++) 
			{
			  if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP1.getSelectedItem().toString()))
			  {
				  L_strBILTP= vtrBILTP.get(i).toString().substring(0,2);
				  //System.out.println("INSIDE3" +L_strBILTP);
			  }
			}
			
			M_strSQLQRY = "Select CMT_CCSVL from CO_CDTRN where CMT_CGMTP ='D02' AND CMT_CGSTP ='ADXXBIL' AND CMT_CODCD='"+cl_dat.M_strFNNYR_pbst.substring(3,4)+""+L_strBILTP+"' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println("INSIDE"+M_strSQLQRY);
			
			if(M_rstRSSET!= null)
			{				
				if(M_rstRSSET.next())				
					L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),""); 		
				//System.out.println("INSIDE1"+L_strTEMP1);
			 M_rstRSSET.close();
			}						
			L_intTSTNO = Integer.valueOf(L_strTEMP1).intValue()+1;
			//L_intTSTNO = 00000+1;
			//System.out.println("INSIDE2"+L_intTSTNO );
			strPAYNO = cl_dat.M_strFNNYR_pbst.substring(3,4)+L_strBILTP
			+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
			+String.valueOf(L_intTSTNO);	
			//System.out.println("INSIDE4"+strPAYNO );
			txtTRNNO.setText(strPAYNO);			
			
		  M_strSQLQRY =" insert into AD_BLMST (BL_TRNNO,BL_BILTP,BL_PAYDT,BL_DPTCD,BL_DPTNM,BL_EMPNO,BL_EMPNM,BL_DESGN,BL_MMGRD,BL_STRDT,BL_ENDDT,BL_SPRCD,BL_BILDT,BL_BILNO,BL_BILAM,BL_EPHRF,BL_DSTQT,BL_SRCLC,BL_DSTLC,BL_TRNQT,BL_UOMCD,BL_TGBQT,BL_ESTVL,BL_CHQNO,BL_CHQDT,BL_BFWDT,BL_CRCDT,BL_CDSDT,BL_REMDS,BL_STSFL,BL_LUSBY,BL_LUPDT,BL_TRNFL,BL_CMPCD)";
		  M_strSQLQRY += " values(";
		  M_strSQLQRY += "'"+ strPAYNO +"',";
		  for(int i=0;i<vtrBILTP.size();i++) 
			{
			  if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP1.getSelectedItem().toString()))
			  {
				  M_strSQLQRY += "'"+vtrBILTP.get(i).toString().substring(0,4)+"',";
			  }
			}
		  if(txtTRNDT.getText().toString().length()>0)
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTRNDT.getText()))+"',";
		  else 
			  M_strSQLQRY += "null,"; 
		  
		  M_strSQLQRY += "'"+ txtDPTCD.getText()+"',";
		  M_strSQLQRY += "'"+txtDPTNM.getText()+"',";
		  M_strSQLQRY += "'"+txtEMPNO.getText()+"',"; 
		  M_strSQLQRY += "'"+txtEMPNM.getText()+"',";
		  M_strSQLQRY += "'"+txtDESGN.getText()+"',";
		  M_strSQLQRY += "'"+txtMMGRD.getText()+"',";
		  if(txtSTRDT.getText().toString().length()>0)
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"',";
		  else 
			  M_strSQLQRY += "null,"; 
		  if(txtENDDT.getText().toString().length()>0)
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"',";
		  else 
			  M_strSQLQRY += "null,"; 
		  M_strSQLQRY += "'"+txtSPRCD.getText().toString()+"',";
		  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBILDT.getText()))+"',";
	      M_strSQLQRY += "'"+txtBILNO.getText().toString()+"',";
		 
	      if(txtBILAM.getText().toString().length()>0)
	    	  M_strSQLQRY += txtBILAM.getText().toString()+",";
	      else 
			  M_strSQLQRY += "0,"; 
	      
	      M_strSQLQRY += "'"+txtEPHRF.getText()+"',";
	      
	      if(txtDSTQT.getText().toString().length()>0)
	    	  M_strSQLQRY += txtDSTQT.getText().toString()+",";
	      else
			  M_strSQLQRY += "0,";
	    
	      M_strSQLQRY += "'"+txtSRCLC.getText().toString()+"',";
		  M_strSQLQRY += "'"+txtDSTLC.getText().toString()+"',";
		  
		  if(txtTRNQT.getText().toString().length()>0)
			  M_strSQLQRY += txtTRNQT.getText().toString()+",";
		  else
			  M_strSQLQRY += "0,";
		  
		   M_strSQLQRY += "'"+txtUOMCD.getText().toString()+"',";
		 
		  if(txtTGBQT.getText().toString().length()>0)
			  M_strSQLQRY += txtTGBQT.getText().toString()+",";
		  else
			  M_strSQLQRY += "0,";
		  
		  if(txtESTVL.getText().toString().length()>0)
			  M_strSQLQRY += txtESTVL.getText().toString()+",";
		  else
			  M_strSQLQRY += "0,";
		  
		  
		  M_strSQLQRY += "'"+txtCHQNO.getText().toString()+"',";
		  if(txtCHQDT.getText().toString().length()>0)
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHQDT.getText().toString()))+"',";
		  else
			  M_strSQLQRY += "null,";
		  if(txtBFWDT.getText().toString().length()>0)
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBFWDT.getText().toString()))+"',";
		  else
			  M_strSQLQRY += "null,";
		  if(txtCRCDT.getText().toString().length()>0)
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCRCDT.getText().toString()))+"',";
		  else
			  M_strSQLQRY += "null,";
		  if(txtCDSDT.getText().toString().length()>0)
			  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCDSDT.getText().toString()))+"',";
		  else
			  M_strSQLQRY += "null,";
		  M_strSQLQRY += "'"+txtREMDS.getText().toString()+"',";
		  
		  
		  M_strSQLQRY += "'1',";
		  M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
		  M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
		  M_strSQLQRY += "'1',";
		  M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"')";
		  //System.out.println(">>>Insert>>"+ M_strSQLQRY );
		  //cl_dat.M_flgLCUPD_pbst = true;
		  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		  
		  if(cl_dat.M_flgLCUPD_pbst == true)
			{
			  //JOptionPane.showInternalMessageDialog(this, "Please Note the Transaction Number\n"+strPAYNO,"","information",JOptionPane.INFORMATION_MESSAGE);
			  JOptionPane.showMessageDialog(this,"Please Note the Transaction Number\n"+strPAYNO,"",JOptionPane.ERROR_MESSAGE);
				M_strSQLQRY = "Update CO_CDTRN set CMT_CCSVL ='"+ strPAYNO.substring(3) +"' where CMT_CGMTP ='D02' AND CMT_CGSTP ='ADXXBIL' AND CMT_CODCD='"+cl_dat.M_strFNNYR_pbst.substring(3,4)+L_strBILTP+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
		  setCursor(cl_dat.M_curDFSTS_pbst);
		}
		
		catch(Exception L_EX)
		{
			 setMSG(L_EX,"exeADDREC()"); 
		}
	}
	/** Method to update Records*/
	private void exeMODREC() 
	{
	    try
	    {
	    	M_strSQLQRY = " Update AD_BLMST set";
	    	if(!txtSTRDT.getText().toString().equals(""))
	    		M_strSQLQRY += " BL_STRDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString()))+"',";
	    	if(!txtENDDT.getText().toString().equals(""))
	    		M_strSQLQRY += " BL_ENDDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString()))+"',";
	    	if(!txtBILDT.getText().toString().equals(""))
	    		M_strSQLQRY += " BL_BILDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBILDT.getText().toString()))+"',";
	    	M_strSQLQRY += " BL_DPTCD='"+txtDPTCD.getText().toString()+"',";
	    	M_strSQLQRY += " BL_EMPNO='"+txtEMPNO.getText().toString()+"',";
	    	M_strSQLQRY += " BL_BILNO='"+txtBILNO.getText().toString()+"',";
	    	M_strSQLQRY += " BL_SPRCD='"+txtSPRCD.getText().toString()+"',";
	    	M_strSQLQRY += " BL_EPHRF='"+txtEPHRF.getText().toString()+"',";
			M_strSQLQRY += " BL_SRCLC='"+txtSRCLC.getText().toString()+"',";
			M_strSQLQRY += " BL_DSTLC='"+txtDSTLC.getText().toString()+"',";
			M_strSQLQRY += " BL_UOMCD='"+txtUOMCD.getText().toString()+"',";
			 for(int i=0;i<vtrBILTP.size();i++) 
				{
				  if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP1.getSelectedItem().toString()))
				  {
					  M_strSQLQRY +=" BL_BILTP='"+vtrBILTP.get(i).toString().substring(0,4)+"',";
				  }
				}
			
			if(txtBILAM.getText().toString().length()>0)
				M_strSQLQRY += " BL_BILAM="+txtBILAM.getText().toString()+",";
			else 
				M_strSQLQRY += " BL_BILAM=0,"; 
			
			if(txtDSTQT.getText().toString().length()>0)
				M_strSQLQRY +=" BL_DSTQT="+ txtDSTQT.getText().toString()+",";
			else
		    	M_strSQLQRY += " BL_DSTQT=0,";
		    
			if(txtTRNQT.getText().toString().length()>0)
				M_strSQLQRY += " BL_TRNQT="+txtTRNQT.getText().toString()+",";
			else
				M_strSQLQRY += " BL_TRNQT=0,";
			
			if(txtTGBQT.getText().toString().length()>0)
				M_strSQLQRY +=" BL_TGBQT="+ txtTGBQT.getText().toString()+",";
			else 
				M_strSQLQRY += " BL_TGBQT=0,";  
			
		    if(txtESTVL.getText().toString().length()>0)
		    	M_strSQLQRY +=" BL_ESTVL="+ txtESTVL.getText().toString()+" ,";
			else 
				M_strSQLQRY += " BL_ESTVL=0,"; 
		    
		    M_strSQLQRY += " BL_CHQNO='"+txtCHQNO.getText().toString()+"',";
		    if(!txtCHQDT.getText().toString().equals(""))
		    	M_strSQLQRY += " BL_CHQDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHQDT.getText().toString()))+"',";
		    if(!txtBFWDT.getText().toString().equals(""))
		    	M_strSQLQRY += " BL_BFWDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBFWDT.getText().toString()))+"',";
		    if(!txtCRCDT.getText().toString().equals(""))
		    	M_strSQLQRY += " BL_CRCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCRCDT.getText().toString()))+"',";
		    if(!txtCDSDT.getText().toString().equals(""))
		    	M_strSQLQRY += " BL_CDSDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCDSDT.getText().toString()))+"',";
			
		    M_strSQLQRY += " BL_REMDS='"+txtREMDS.getText().toString()+"'";
			
			M_strSQLQRY +=" where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY +=" AND BL_TRNNO='"+txtTRNNO.getText().toString()+"'";
			M_strSQLQRY+= " AND BL_PAYDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTRNDT.getText().toString()))+"' ";
			
		//System.out.println(">>>update>>"+M_strSQLQRY);  
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		 }
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODREC()");
	    }
	} 
/**
 * Delete Records From Table*/
	private void exeDELREC() 
	{ 
	  try
	  {
		  	M_strSQLQRY = "UPDATE AD_BLMST SET";	
			M_strSQLQRY +="	BL_STSFL='X'";	
			M_strSQLQRY +=" where BL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY +=" AND BL_TRNNO='"+txtTRNNO.getText().toString()+"'";
			M_strSQLQRY+= " AND BL_PAYDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTRNDT.getText().toString()))+"' ";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			//System.out.println(">>>Delete>>"+M_strSQLQRY);
	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELREC()");		
	  }
	}
	/** Validate Data by user*/
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				//if(((JTextField)input).getText().length() == 0)
					//return true;
				
				
				if(input == txtTRNDT)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					if(txtTRNDT.getText().trim().length() ==0)
		    		{
						txtTRNDT.requestFocus();
		    			setMSG("Enter the Transaction Date",'E');
		    			return false;
		    		}
					else if(M_fmtLCDAT.parse(txtTRNDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Transaction Date Should Not be greater than Current Date..",'E');
						txtTRNDT.requestFocus();
						return false;
					}
				}
				
				
				
				if(((JTextField)input).getText().length() == 0)
					return true;
				
				if(input == txtDPTCD)
				{
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and CMT_CODCD = '"+txtDPTCD.getText()+"'";
					//System.out.println("INPVF DPTCD : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
		               // txtDPTCD.setText(M_rstRSSET.getString("CMT_CODCD"));
		                  txtDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
						setMSG("",'N');
					}
				    else
					{
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
					
				}
				if(input == txtTRNNO)
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						/*if(txtTRNNO.getText().trim().length() ==0)
			    		{
							txtTRNNO.requestFocus();
			    			setMSG("Enter the Transaction Number",'E');
			    			return false;
			    		}*/
						M_strSQLQRY=" SELECT BL_TRNNO,BL_PAYDT,BL_BILNO,BL_BILDT from AD_BLMST where isnull(BL_STSFL,'')<>'X' ";
						for(int i=0;i<vtrBILTP.size();i++)
						{
						 if(vtrBILTP.get(i).toString().substring(5).equals(cmbBILTP1.getSelectedItem().toString()))
						  {
							 M_strSQLQRY += " AND BL_BILTP like '"+vtrBILTP.get(i).toString().substring(0,4)+"%'";
						  }
						}
						M_strSQLQRY += "and BL_TRNNO = '"+txtTRNNO.getText()+"'";
							//System.out.println("INPVF DPTCD : "+M_strSQLQRY);
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
		                        txtTRNNO.setText(M_rstRSSET.getString("BL_TRNNO"));
		                        txtBILNO.setText(M_rstRSSET.getString("BL_BILNO"));
								setMSG("",'N');
							}
							else
							{
								setMSG("Enter Valid Transaction Number",'E');
								return false;
							}
					}
				}
				if(input == txtEMPNO)
				{
					
					M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and ep_empno = '"+txtEMPNO.getText()+"'";
					if(txtDPTCD.getText().length()>0)
    					M_strSQLQRY += " and EP_DPTCD='"+txtDPTCD.getText()+"'";
					//System.out.println("INPVF EMPNO : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
						setMSG("",'N');
						getDATA1();
					}	
					else
					{
						setMSG("Enter Valid Employee Code",'E');
						return false;
					}
				}
				
				
				if(input == txtSTRDT)
			    {
					if(M_fmtLCDAT.parse(txtSTRDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("From Date Should Not Be Grater Than Todays Date..",'E');
						txtSTRDT.requestFocus();
						return false;
					}
			    }
				if(input == txtENDDT)
			    {
					if(M_fmtLCDAT.parse(txtENDDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("To Date Should Not Be Grater Than Todays Date..",'E');
						txtENDDT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtENDDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtSTRDT.getText().trim()))<0)
					{
						setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
						txtENDDT.requestFocus();
						return false;
					}
			    }
		    	
		    	if(input == txtUOMCD)
		    	{
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' AND CMT_CGSTP='COXXUOM' AND CMT_CODCD='"+txtUOMCD.getText().toString()+"' AND isnull(CMT_STSFL,'')<>'X' order by CMT_CODCD";
					//System.out.println("INPVF UOM : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(!M_rstRSSET.next() || M_rstRSSET==null)
					{
						setMSG("Enter Valid UOM",'E');
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
}
	
	
	