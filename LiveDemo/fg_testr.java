// Date 29/12/2006

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;import java.awt.Color;
import java.awt.Dimension;
import java.sql.ResultSet;import javax.swing.JPanel;import javax.swing.JTabbedPane;
import java.sql.CallableStatement;import javax.swing.JComboBox;import java.util.Vector;
import java.util.Enumeration;import javax.swing.JButton;
import javax.swing.JOptionPane;import java.util.Calendar;
import java.util.logging.*;

class fg_testr extends cl_pbase
{
	private JLabel lblAMDNO,lblAMDDT,lblPRTDS,lblAUTBY,lblAUTDT;
	private JTextField txtRESTP,txtRESNO,txtRESDT,txtSTRDT,txtENDDT;
	private JTextField txtPRDTP,txtPRTTP,txtGRPCD,txtREMDS;
    private JTextField txtLOTNO,txtRCLNO,txtRESQT,/*txtEUSCD,*/txtPRDDS,txtPRDCD;	
	private JTextField txtREQBY,txtREQDT;
	private JCheckBox chkCHKFL;
	
	private cl_JTable tblGRDTL;  
	private String strPRDTP="01"; 
	private String strMKTTP="01"; 
	private String strLOTNO="";
	private String strRCLNO="";
	private String strSTSFL="";
	
	private Vector<String> vtrSTKQT;
	
	private final int TBL_CHKFL=0;
	private final int TBL_PRDDS=1;
	private final int TBL_LOTNO=2;
	private final int TBL_RCLNO=3;
	private final int TBL_RESQT=4;
	private final int TBL_STKQT=5;
	private final int TBL_RUNTO=6;
	//private final int TBL_EUSCD=x;
	//private final int TBL_EUSDS=x;
	private final int TBL_REMDS=7;
	private final int TBL_PRDCD=8;
	private JPanel pnlRESEV;
	private String strYREND = "31/03/2009";
    private String strYRDGT;
    private String strWHRSTR;
	CallableStatement cstLTMST_STR;	// Stored procedure for setting res.qty in LTMST
	private String strOGLIST = "('511195','511295','521195','521295','519595','529595')";

	private boolean flgAUTRN = false;
	private boolean flgCHK_EXIST = false;
	private boolean flgFIRSTREC = true;
	private boolean flgOFFGRD = false;
	
	private Hashtable<String,String> hstRESQT;
	
	private JRadioButton rdbAUTH_Y;
	private JRadioButton rdbAUTH_N;	
	
    // Column number variables for Reserved LOts Display Table
	final int TB7_GRPNM=1;
    final int TB7_LOTNO=2;
    final int TB7_AVLQT=3;
    final int TB7_RESQT=4;
    final int TB7_RDSQT=5; 
    final int TB7_BALQT=6;
    final int TB7_STRDT=7;
    final int TB7_ENDDT=8;
    final int TB7_RESNO=9;


    // Column number variables for Quality Hold Lots Display Table
	final int TB8_PRDDS=1;
    final int TB8_LOTNO=2;
    final int TB8_RCLNO=3;
    final int TB8_REMDS=4;
    final int TB8_STKQT=5;
	
	String strWRHTP="01";
	String strTRNTP="SR";
	private int intRWCNT=0;
	private boolean flgAMDFL=false;
	private JLabel lblOFFGRD;
	
	String strENDDT;
	
	FileHandler f;
	Logger loger;
	boolean flgREMRK=false;
    private JCheckBox chkRESEV;
    private JLabel lblGRAD;
    private cl_JTable tblRESEV;
    private cl_JTable tblQLTHLD;
    private JButton btnPRINT;
    private JTextField txtACTS;
    private JTextField txtRESS;
    private JTextField txtRESQ;
    private JTextField txtQDIS;
	public fg_testr(int intOFFGRD)
	{
		super(2);
		setMatrix(20,8);
		try
		{
			if(intOFFGRD==1)
				  flgOFFGRD = true;
			dspENTSCR();
		}
		
		catch(Exception L_E)
		{
			setMSG(L_E,"fg_testr(int)");
		}
	}

	public fg_testr()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			flgOFFGRD = false;
			dspENTSCR();
		}
		
		catch(Exception L_E)
		{
			setMSG(L_E,"fg_testr()");
		}
	}
	
/*	public fg_testr()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			//ResultSet rs= cl_dat.exeSQLQRY1("Select * from MR_SRATR");
			
			vtrSTKQT=new Vector<String>(2);
			hstRESQT = new  Hashtable<String,String>();
			add(new JLabel("Res. Type"),1,2,1,1,this,'L');
			add(txtRESTP = new TxtLimit(2),2,2,1,0.25,this,'L');
			add(new JLabel("Res. No."),1,3,1,1,this,'L');
			add(txtRESNO = new TxtLimit(8),2,3,1,0.75,this,'L');
			add(new JLabel("Res. Date"),1,4,1,1,this,'L');
			add(txtRESDT = new TxtDate(),2,4,1,0.75,this,'L');
			
			add(new JLabel("Amd.No & Date"),1,5,1,1,this,'L');
			add(lblAMDNO = new JLabel(""),2,5,1,0.20,this,'L');
			//add(new JLabel("Amd. Date"),1,5,1,1,this,'L');
			add(lblAMDDT = new JLabel(""),2,5,1,0.80,this,'R');
			
			add(new JLabel("Start Date"),3,2,1,1,this,'L');
			add(txtSTRDT = new TxtDate(),4,2,1,1,this,'L');
			add(new JLabel("End Date"),3,3,1,1,this,'L');
			add(txtENDDT = new TxtDate(),4,3,1,1,this,'L');
			
			add(new JLabel("Req.By"),3,4,1,1,this,'L');
			add(txtREQBY=new TxtLimit(5),4,4,1,0.50,this,'L');
			add(new JLabel("Date"),3,5,1,1,this,'L');
			add(txtREQDT=new TxtDate(),4,5,1,1,this,'L');
			
			add(new JLabel("Auth.By"),3,6,1,1,this,'L');
			add(lblAUTBY=new JLabel(""),4,6,1,0.50,this,'L');
			add(new JLabel("Date"),3,7,1,1,this,'L');
			
			
			add(lblOFFGRD = new JLabel(""),3,8,1,2,this,'L');
			
			
			add(lblAUTDT=new JLabel(""),4,7,1,1,this,'L');
			
			add(new JLabel("Prd. Type"),5,2,1,1,this,'L');
			add(txtPRDTP = new TxtLimit(2),6,2,1,0.25,this,'L');
			
			add(new JLabel("Prt.Type & Code"),5,3,1,2,this,'L');
			add(txtPRTTP = new TxtLimit(1),6,3,1,.2,this,'L');
			add(txtGRPCD = new TxtLimit(5),6,3,1,.7,this,'R');
			//add(new JLabel("Name"),5,5,1,1,this,'L');
			add(lblPRTDS = new JLabel(""),6,4,1,4,this,'L');
			
			add(new JLabel("Remark"),7,2,1,1,this,'L');
			add(txtREMDS = new TxtLimit(200),7,3,1,5,this,'L');
			add(rdbAUTH_Y=new JRadioButton("Authorise"),2,6,1,1.5,this,'L');
			add(rdbAUTH_N=new JRadioButton("DoNot Authorise"),2,8,1,1.5,this,'R');
			ButtonGroup btgAUTH=new ButtonGroup();
			btgAUTH.add(rdbAUTH_Y);btgAUTH.add(rdbAUTH_N);
			
			//String[] L_strTBLHD1 = {"FL","Grade Desc","Lot No","Rcl.No.","Auth Qty","U/Res.Stk","Running Total","Code","Description","Remark(Quality Related)","Prd.Code"};
			String[] L_strTBLHD1 = {"FL","Grade Desc","Lot No","Rcl.No.","Auth Qty","U/Res.Stk","Running Total","Remark(Quality Related)","Prd.Code"};
			int[] L_intCOLSZ1 = {20,100,70,40,80,80,80,140,5};
			tblGRDTL = crtTBLPNL1(this,L_strTBLHD1,50,9,2,9,6.7,L_intCOLSZ1,new int[]{0});
			add(chkRESEV=new JCheckBox("View Res. Status"),18,2,1,1.5,this,'L');//adding the 24-05-2007
			tblGRDTL.setInputVerifier(new TBLINPVF());
			tblGRDTL.addKeyListener(this);
			tblGRDTL.setCellEditor(TBL_CHKFL,chkCHKFL = new JCheckBox());
			tblGRDTL.setCellEditor(TBL_LOTNO,txtLOTNO = new TxtLimit(8));
			tblGRDTL.setCellEditor(TBL_RCLNO,txtRCLNO = new TxtLimit(2));
			tblGRDTL.setCellEditor(TBL_RESQT,txtRESQT = new TxtLimit(10));
			//tblGRDTL.setCellEditor(TBL_EUSCD,txtEUSCD = new TxtLimit(2));
			tblGRDTL.setCellEditor(TBL_PRDDS,txtPRDDS = new TxtLimit(15));
			tblGRDTL.setCellEditor(TBL_PRDCD,txtPRDCD = new TxtLimit(10));
			
			chkCHKFL.addKeyListener(this);
			
			txtPRDDS.addKeyListener(this);
			txtPRDDS.addFocusListener(this);
			
			txtLOTNO.addKeyListener(this);
			txtLOTNO.addFocusListener(this);

			txtRCLNO.addKeyListener(this);
			txtRCLNO.addFocusListener(this);
			
			txtRESQT.addKeyListener(this);
			txtRESQT.addFocusListener(this);
					
			//txtEUSCD.addKeyListener(this);
			//txtEUSCD.addFocusListener(this);
			
			INPVF objFGIPV=new INPVF();
		
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objFGIPV);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
				}
			}
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "9" : "8";
			
			lblOFFGRD.setText(flgOFFGRD ? "Off Grade Reservation" : "General Reservation");
			setENBL(false);
			
			
			loger=Logger.getLogger("fg_testr");
			//loger.log("Severe Testing");
			f=new FileHandler("/fg_testr.log");
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"Constructor");
		}
	}
	
*/
	
public void dspENTSCR()	
{
		try
		{
			//ResultSet rs= cl_dat.exeSQLQRY1("Select * from MR_SRATR");
			vtrSTKQT=new Vector<String>(2);
			hstRESQT = new  Hashtable<String,String>();
			add(new JLabel("Res. Type"),1,2,1,1,this,'L');
			add(txtRESTP = new TxtLimit(2),2,2,1,0.25,this,'L');
			add(new JLabel("Res. No."),1,3,1,1,this,'L');
			add(txtRESNO = new TxtLimit(8),2,3,1,0.75,this,'L');
			add(new JLabel("Res. Date"),1,4,1,1,this,'L');
			add(txtRESDT = new TxtDate(),2,4,1,0.75,this,'L');
			
			add(new JLabel("Amd.No & Date"),1,5,1,1,this,'L');
			add(lblAMDNO = new JLabel(""),2,5,1,0.20,this,'L');
			//add(new JLabel("Amd. Date"),1,5,1,1,this,'L');
			add(lblAMDDT = new JLabel(""),2,5,1,0.80,this,'R');
			
			add(new JLabel("Start Date"),3,2,1,1,this,'L');
			add(txtSTRDT = new TxtDate(),4,2,1,1,this,'L');
			add(new JLabel("End Date"),3,3,1,1,this,'L');
			add(txtENDDT = new TxtDate(),4,3,1,1,this,'L');
			
			add(new JLabel("Req.By"),3,4,1,1,this,'L');
			add(txtREQBY=new TxtLimit(5),4,4,1,0.50,this,'L');
			add(new JLabel("Date"),3,5,1,1,this,'L');
			add(txtREQDT=new TxtDate(),4,5,1,1,this,'L');
			
			add(new JLabel("Auth.By"),3,6,1,1,this,'L');
			add(lblAUTBY=new JLabel(""),4,6,1,0.50,this,'L');
			add(new JLabel("Date"),3,7,1,1,this,'L');
			
			
			add(lblOFFGRD = new JLabel(""),3,8,1,2,this,'L');
			
			
			add(lblAUTDT=new JLabel(""),4,7,1,1,this,'L');
			
			add(new JLabel("Prd. Type"),5,2,1,1,this,'L');
			add(txtPRDTP = new TxtLimit(2),6,2,1,0.25,this,'L');
			
			add(new JLabel("Prt.Type & Code"),5,3,1,2,this,'L');
			add(txtPRTTP = new TxtLimit(1),6,3,1,.2,this,'L');
			add(txtGRPCD = new TxtLimit(5),6,3,1,.7,this,'R');
			//add(new JLabel("Name"),5,5,1,1,this,'L');
			add(lblPRTDS = new JLabel(""),6,4,1,4,this,'L');
			
			add(new JLabel("Remark"),7,2,1,1,this,'L');
			add(txtREMDS = new TxtLimit(200),7,3,1,5,this,'L');
			add(rdbAUTH_Y=new JRadioButton("Authorise"),2,6,1,1.5,this,'L');
			add(rdbAUTH_N=new JRadioButton("DoNot Authorise"),2,8,1,1.5,this,'R');
			ButtonGroup btgAUTH=new ButtonGroup();
			btgAUTH.add(rdbAUTH_Y);btgAUTH.add(rdbAUTH_N);
			
			//String[] L_strTBLHD1 = {"FL","Grade Desc","Lot No","Rcl.No.","Auth Qty","U/Res.Stk","Running Total","Code","Description","Remark(Quality Related)","Prd.Code"};
			String[] L_strTBLHD1 = {"FL","Grade Desc","Lot No","Rcl.No.","Auth Qty","U/Res.Stk","Running Total","Remark(Quality Related)","Prd.Code"};
			int[] L_intCOLSZ1 = {20,100,70,40,80,80,80,140,5};
			tblGRDTL = crtTBLPNL1(this,L_strTBLHD1,50,9,2,9,6.7,L_intCOLSZ1,new int[]{0});
			add(chkRESEV=new JCheckBox("View Res. Status"),18,2,1,1.5,this,'L');//adding the 24-05-2007
			tblGRDTL.setInputVerifier(new TBLINPVF());
			tblGRDTL.addKeyListener(this);
			tblGRDTL.setCellEditor(TBL_CHKFL,chkCHKFL = new JCheckBox());
			tblGRDTL.setCellEditor(TBL_LOTNO,txtLOTNO = new TxtLimit(8));
			tblGRDTL.setCellEditor(TBL_RCLNO,txtRCLNO = new TxtLimit(2));
			tblGRDTL.setCellEditor(TBL_RESQT,txtRESQT = new TxtLimit(10));
			//tblGRDTL.setCellEditor(TBL_EUSCD,txtEUSCD = new TxtLimit(2));
			tblGRDTL.setCellEditor(TBL_PRDDS,txtPRDDS = new TxtLimit(15));
			tblGRDTL.setCellEditor(TBL_PRDCD,txtPRDCD = new TxtLimit(10));
			
			chkCHKFL.addKeyListener(this);
			
			txtPRDDS.addKeyListener(this);
			txtPRDDS.addFocusListener(this);
			
			txtLOTNO.addKeyListener(this);
			txtLOTNO.addFocusListener(this);

			txtRCLNO.addKeyListener(this);
			txtRCLNO.addFocusListener(this);
			
			txtRESQT.addKeyListener(this);
			txtRESQT.addFocusListener(this);
					
			//txtEUSCD.addKeyListener(this);
			//txtEUSCD.addFocusListener(this);
			
			INPVF objFGIPV=new INPVF();
		
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objFGIPV);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
				}
			}
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "0" : "9";
			
			lblOFFGRD.setText(flgOFFGRD ? "Off Grade Reservation" : "General Reservation");
			setENBL(false);
			
			
			loger=Logger.getLogger("fg_testr");
			//loger.log("Severe Testing");
			f=new FileHandler("/fg_testr.log");
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"dspENTSCR");
		}
}
	
	/**
	 * Super Class metdhod overrided to inhance its funcationality, to enable disable 
	 * components according to requriement.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		
		//txtPRTDS.setEnabled(false);
		//txtAUTBY.setEnabled(false);

		//txtAUTDT.setEnabled(false);
		//txtREQBY.setEnabled(false);
	//	txtREQDT.setEnabled(false);
		//tblGRDTL.cmpEDITR[TBL_PRDDS].setEnabled(false);
		
		tblGRDTL.cmpEDITR[TBL_STKQT].setEnabled(false);
		//tblGRDTL.cmpEDITR[TBL_EUSDS].setEnabled(false);
		tblGRDTL.cmpEDITR[TBL_REMDS].setEnabled(false);
		tblGRDTL.cmpEDITR[TBL_PRDCD].setEnabled(false);
		tblGRDTL.cmpEDITR[TBL_RUNTO].setEnabled(false);
		tblGRDTL.cmpEDITR[TBL_RCLNO].setEnabled(false);		
	} 	/**
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		
		try
		{
		    
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(M_objSOURC==cl_dat.M_cmbOPTN_pbst&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{
				flgAUTRN = false;
				if(M_staUSRRT[0][M_intAE_AUTFL].equals("Y"))		
				    flgAUTRN=true;
				txtRESTP.setEnabled(true);
				txtRESNO.setEnabled(true);
				//SETTING Authorisation button and resp. radiobuttons' status
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					if(flgAUTRN)
					{
						rdbAUTH_Y.setVisible(true);
						rdbAUTH_N.setVisible(true);
						rdbAUTH_Y.setSelected(false);
						rdbAUTH_N.setSelected(false);
					}
				}
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						tblGRDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);	
					}
					txtRESNO.setEnabled(true);
					txtRESTP.setEnabled(true);
					txtREQBY.setText(cl_dat.M_strUSRCD_pbst);
					txtRESTP.requestFocus();
					
					txtRESDT.setEnabled(false);
					txtSTRDT.setEnabled(false);
					txtENDDT.setEnabled(false);
					txtREQBY.setEnabled(false);
					txtREQDT.setEnabled(false);
					
					//txtAUTDT.setEnabled(false);
					//txtAUTDT.setEnabled(false);
					txtPRDTP.setEnabled(false);
					txtPRTTP.setEnabled(false);
					txtGRPCD.setEnabled(false);
				}
				
				else 
				{
					txtRESNO.setEnabled(false);
					txtPRTTP.setText("C");
					txtRESTP.setText("01");
					txtRESDT.setText(cl_dat.M_strLOGDT_pbst);
					txtRESDT.setEnabled(true);
					txtSTRDT.setEnabled(true);
					txtSTRDT.setText(cl_dat.M_strLOGDT_pbst);
					
					strENDDT = txtSTRDT.getText().trim();
					M_calLOCAL.setTime(M_fmtLCDAT.parse(strENDDT));  
					M_calLOCAL.add(Calendar.MONTH,+1);               
					strENDDT = "01/"+M_fmtLCDAT.format(M_calLOCAL.getTime()).substring(3);
					M_calLOCAL.setTime(M_fmtLCDAT.parse(strENDDT));  
					M_calLOCAL.add(Calendar.DATE,-1);     
					strENDDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
					txtENDDT.setText(strENDDT);
					if(txtREQBY.getText().trim().length() == 0)
						txtREQBY.setText(cl_dat.M_strUSRCD_pbst);
					if(txtPRDTP.getText().trim().length() == 0)
						txtPRDTP.setText("01");
					
					//txtAUTBY.setText(cl_dat.M_strUSRCD_pbst);
					//txtAUTDT.setText(cl_dat.M_strLOGDT_pbst);
					
					//strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime()); 
					
					txtENDDT.setEnabled(true);
					lblAMDNO.setText("00");
					txtREQBY.setEnabled(true);
					txtREQDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREQDT.setEnabled(true);
					//txtAUTDT.setEnabled(true);
					//txtAUTDT.setEnabled(true);
					txtPRDTP.setEnabled(true);
					//txtPRTTP.setEnabled(true);
					txtGRPCD.setEnabled(true);
				}			
			}
			
			
			if(M_objSOURC==chkRESEV)
			{
				if(chkRESEV.isSelected())
				{
					chkRESEV(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDCD).toString());
				}
				
			}
			if(M_objSOURC==btnPRINT)
			{
			    dispPRINT();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"actionPerformed");
		}
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC==txtLOTNO) 
			{
				setMSG("Press F1 to select Lot No & then press Enter.",'N');
				if(tblGRDTL.getSelectedRow()<intRWCNT)
					((JTextField)tblGRDTL.cmpEDITR[TBL_LOTNO]).setEditable(false);
				else
					((JTextField)tblGRDTL.cmpEDITR[TBL_LOTNO]).setEditable(true);
			}
			if(M_objSOURC==txtPRDDS) 
			{
				setMSG("Press F1 to select Lot No & then press Enter.",'N');
				if(tblGRDTL.getSelectedRow()<intRWCNT)
					((JTextField)tblGRDTL.cmpEDITR[TBL_PRDDS]).setEditable(false);
				else
					((JTextField)tblGRDTL.cmpEDITR[TBL_PRDDS]).setEditable(true);
			}
		}
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode()==L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtRESTP)
				{
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and";
				    M_strSQLQRY +=" CMT_CGSTP = 'FGXXSTR'  AND CMT_CODCD='"+txtRESTP.getText().trim()+"' ";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
							txtRESNO.requestFocus();
						else
							txtSTRDT.requestFocus();
					}
					else
					{
						setMSG("Invalid Reservation Type ",'E');
						txtRESTP.requestFocus();
					}
				}
				if(M_objSOURC==txtRESNO)
				{
					getDATA();
					//lblAMDNO.requestFocus();
				}
				if(M_objSOURC==txtSTRDT)
				{
					txtENDDT.requestFocus();
				}
				if(M_objSOURC==txtENDDT)
				{
					txtREQBY.requestFocus();
				}
				if(M_objSOURC==txtREQBY)
				{
					txtREQDT.requestFocus();
				}
				if(M_objSOURC==txtREQDT)
				{
					txtPRDTP.requestFocus();
				}
				/*if(M_objSOURC==txtAUTBY)
				{
					txtAUTDT.requestFocus();
				}
				if(M_objSOURC==txtAUTDT)
				{
					txtPRDTP.requestFocus();
				}*/
				if(M_objSOURC==txtPRDTP)
				{
					//txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
					txtGRPCD.requestFocus();
				}
				
				//if(M_objSOURC==txtPRTTP)
				//{
				//	txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
				//	txtGRPCD.requestFocus();
				//}
				if(M_objSOURC==txtGRPCD)
				{
					txtREMDS.requestFocus();
				}
				if(M_objSOURC==txtREMDS)
				{
					tblGRDTL.setRowSelectionInterval(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedRow());		
					tblGRDTL.setColumnSelectionInterval(TBL_PRDDS,TBL_PRDDS);		
					tblGRDTL.editCellAt(tblGRDTL.getSelectedRow(),TBL_PRDDS);
					tblGRDTL.cmpEDITR[TBL_PRDDS].requestFocus();
				}
			}
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC==txtRESTP)
				{
					M_strHLPFLD = "txtRESTP";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and";
				    M_strSQLQRY +=" CMT_CGSTP = 'FGXXSTR'  order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[] {"Code","Description"},2,"CT");
				}
				/*if(M_objSOURC==txtPRTTP)
				{
					M_strHLPFLD = "txtPRTTP";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
				    M_strSQLQRY +=" CMT_CGSTP = 'COXXPRT'  order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[] {"Code","Description"},2,"CT");
				}*/
				if(M_objSOURC==txtPRDTP)
				{
					M_strHLPFLD = "txtPRDTP";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
				    M_strSQLQRY +=" CMT_CGSTP = 'COXXPRD'  order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,2,1,new String[] {"Code","Description"},2,"CT");
				}
				if(M_objSOURC==txtRESNO)
				{
					M_strHLPFLD = "txtRESNO";
					M_strSQLQRY = "Select distinct SR_RESNO,min(PT_PRTNM) PT_PRTNM,PR_PRDDS from CO_PRMST,FG_SRTRN left outer join CO_PTMST on PT_PRTTP='C' and PT_GRPCD = SR_GRPCD where SR_PRDCD=PR_PRDCD and SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_RESTP='"+txtRESTP.getText().trim()+"'  ";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						M_strSQLQRY +="and isnull(SR_STSFL,'')<>'X' " ;
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						M_strSQLQRY +="and SR_STSFL not in('1','X') ";
					
					M_strSQLQRY +=" group by SR_RESNO,PR_PRDDS order by SR_RESNO desc" ;
					//System.out.println("txtRESNO = "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[] {"Res. No","Customer","Grade"},3,"CT");
				}
				if(M_objSOURC==txtGRPCD)
				{
					M_strHLPFLD="txtGRPCD";
					M_strSQLQRY="Select distinct isnull(PT_GRPCD,'XXXXX') PT_GRPCD,PT_PRTNM,PT_CTYNM,CMT_CODDS from CO_PTMST left outer join CO_CDTRN on CMT_CGMTP='MST' and CMT_CGSTP='COXXSTA' and CMT_CODCD=PT_STACD where  PT_PRTTP='"+txtPRTTP.getText().trim()+"' and PT_CNTCD = '001' AND isnull(PT_STSFL,'')<> 'X'";
					if(txtGRPCD.getText().trim().length()>0)
					{	
						M_strSQLQRY += "AND PT_GRPCD LIKE '"+txtGRPCD.getText().trim().toUpperCase()+"%'"; 
					}
					M_strSQLQRY +=" Order by PT_PRTNM";
					
					System.out.println("M_strSQLQRY = "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code", "Party Name","City","State"},4,"CT");
				}
				if(M_objSOURC==txtPRDDS)
				{
					M_strHLPFLD = "txtPRDDS";
					M_strSQLQRY="select ST_PRDCD,PR_PRDDS,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) ST_STKQT from FG_STMST, CO_PRMST where ST_PRDCD=PR_PRDCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ST_PRDTP = '"+txtPRDTP.getText()+"' and SUBSTRING(pr_prdcd,5,2) = '95' AND (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)>0) ";
					if(flgOFFGRD)
						M_strSQLQRY += " and SUBSTRING(PR_PRDCD,1,6) in "+strOGLIST;
					if(txtPRDDS.getText().trim().length()>0)
					{
						M_strSQLQRY += " AND PR_PRDDS like '"+txtPRDDS.getText().trim().toUpperCase()+ "%'";
					}
					M_strSQLQRY += " group by ST_PRDCD,PR_PRDDS order by st_prdcd";
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[] {"Code","Description","Stock"},3,"CT");
					
				}

				/*if(M_objSOURC==txtEUSCD)
				{
					M_strHLPFLD = "txtEUSCD";
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and";
				    M_strSQLQRY +=" CMT_CGSTP = 'MR00EUS'  order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,2,1,new String[] {"Code","Description"},2,"CT");
				}*/
				if(M_objSOURC==txtLOTNO)
				{
					M_strHLPFLD = "txtLOTNO";
					String L_strPRDCD=tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDCD).toString();
					//System.out.println("L_strPRDCD = "+L_strPRDCD);
					//New Query Change By SRD  // Date 18-01-07 11.00 am
					/*
						In this Query there are taken average of lt_resqt+lt_rdsqt+lt_hldqt+lt_retqt ";
					    because this query add record more then one time.
					*/
					cstLTMST_STR = cl_dat.M_conSPDBA_pbst.prepareCall("{ call setLTMST_STR(?,?,?)}");
					cstLTMST_STR.setString(1,cl_dat.M_strCMPCD_pbst);
					cstLTMST_STR.setString(2,L_strPRDCD);
					cstLTMST_STR.setString(3,txtPRDTP.getText());
					cstLTMST_STR.executeUpdate();
					
					M_strSQLQRY = " select lt_lotno,max(lt_rclno) lt_rclno,lt_prdcd,pr_prdds,lt_remds,";
					M_strSQLQRY += "round(sum(isnull(st_stkqt,0)-isnull(st_aloqt,0))-avg((isnull(lt_resqt,0)-isnull(lt_ralqt,0)-isnull(lt_rdsqt,0))+isnull(lt_hldqt,0)+isnull(lt_retqt,0)),3) lt_ursqt, ";
					M_strSQLQRY += " lt_clsfl  from  co_prmst,pr_ltmst left outer join ";
					M_strSQLQRY += " fg_stmst on lt_prdtp = st_prdtp and lt_lotno = st_lotno and  ";
					M_strSQLQRY += " lt_rclno = st_rclno where lt_prdcd = pr_prdcd  and LT_PRDTP='"+txtPRDTP.getText()+"'   ";
					M_strSQLQRY += " and lt_clsfl='9' and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDCD='"+L_strPRDCD+"' and upper(isnull(lt_resfl,'X')) not in ('Q','H')  group by lt_lotno,lt_prdcd,pr_prdds,lt_remds,lt_clsfl  ";
					M_strSQLQRY += " having (sum(isnull(st_stkqt,0)-isnull(st_aloqt,0))-avg((isnull(lt_resqt,0)-isnull(lt_ralqt,0)-isnull(lt_rdsqt,0))+isnull(lt_hldqt,0)";
					M_strSQLQRY += " +isnull(lt_retqt,0)))>0  order by lt_ursqt desc ";
					
					
					System.out.println(" Lot Help "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"LOT NO.","Rcl.No.","Prd Code","Discription","Remark","Bal Qty","Cls flag"},7,"CT");
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtRESTP"))
			{
				txtRESTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtRESNO"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtRESNO.setText(L_STRTKN1.nextToken());
				txtRESDT.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtPRDTP"))
			{
				txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			/*if(M_strHLPFLD.equals("txtPRTTP"))
			{
				txtPRTTP.setText(cl_dat.M_strHLPSTR_pbst);
			}*/
			if(M_strHLPFLD.equals("txtGRPCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");					
				txtGRPCD.setText(L_STRTKN1.nextToken());
				lblPRTDS.setText(L_STRTKN1.nextToken()+" "+L_STRTKN1.nextToken()+" "+L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtPRDDS"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());	
				tblGRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim(),tblGRDTL.getSelectedRow(),TBL_PRDCD);
			}
			if(M_strHLPFLD.equals("txtLOTNO"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtLOTNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblGRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblGRDTL.getSelectedRow(),TBL_RCLNO);
				tblGRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblGRDTL.getSelectedRow(),TBL_PRDCD);
				tblGRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),tblGRDTL.getSelectedRow(),TBL_PRDDS);
				tblGRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim(),tblGRDTL.getSelectedRow(),TBL_REMDS);
				tblGRDTL.setValueAt(setNumberFormat(Double.parseDouble(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)).trim()),3),tblGRDTL.getSelectedRow(),TBL_STKQT);
				tblGRDTL.setValueAt(setNumberFormat(Double.parseDouble(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)).trim()),3),tblGRDTL.getSelectedRow(),TBL_RESQT);
			}
			/*if(M_strHLPFLD.equals("txtEUSCD"))
			{
//				txtEUSCD.setText(cl_dat.M_strHLPSTR_pbst);
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtEUSCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblGRDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblGRDTL.getSelectedRow(),TBL_EUSDS);
			}*/
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	public boolean vldDATA()
	{
		String strTEMP="";
		if(tblGRDTL.isEditing())
			tblGRDTL.getCellEditor().stopCellEditing();
		tblGRDTL.setRowSelectionInterval(0,0);
		tblGRDTL.setColumnSelectionInterval(0,0);
		
		for(int i=0;i<tblGRDTL.getRowCount();i++)
    	{
			if(tblGRDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
    		{
				strTEMP = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_PRDDS).toString(),"");
    			if(strTEMP.length() == 0)
    			{
    				setMSG("Product Code Can not be Blank..",'E');
					return false;
    			}
				strTEMP = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_LOTNO).toString(),"");
    			if(strTEMP.length() ==0)
    			{
    				setMSG("Lot Number can't be Blank..",'E');
					return false;
    			}
				strTEMP = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_RCLNO).toString(),"");
    			if(strTEMP.length() ==0)
    			{
    				setMSG("Re Classification  Number can't be Blank..",'E');
					return false;
    			}
			}
		}
		return true;
	} 	/**
	 * Displays Reservation status for current day 
	 *
	*/
	/*
	private void chkRESEV()
	{
	   try
	   {
	   
	    if(chkRESEV.isSelected()==false)
			return;
		Hashtable hstGRPNM = new Hashtable();
	    
	    this.setCursor(cl_dat.M_curWTSTS_pbst);
	    if(pnlRESEV==null)
		{
	        
	        pnlRESEV=new JPanel(null);
	        add(new JLabel("Grade"),1,1,1,1,pnlRESEV,'L');
	        add(lblGRAD=new JLabel(),1,2,1,2,pnlRESEV,'L');
	        
	        //add(btnPRINT=new JButton("Print"),1,4,1,0.8,pnlRESEV,'R');
	        lblGRAD.setText(nvlSTRVL(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDDS).toString(),""));
	        String[] L_staCOLHD = {"FL","Customer","Lot.No","Act.Stk","Res.Stk","Qty.Res","Qty.Dsp","From","To","Res.No"};
			int[] L_inaCOLSZ = {20,250,65,60,60,60,60,60,60,60};
			tblRESEV = crtTBLPNL1(pnlRESEV,L_staCOLHD,500,2,1,12,10,L_inaCOLSZ,new int[]{0});
	        
		}
			hstGRPNM.clear();

			M_strSQLQRY ="select distinct sr_grpcd,min(pt_prtnm) pt_prtnm from fg_srtrn  left outer join co_ptmst on pt_prttp='C' and pt_grpcd=sr_grpcd where sr_prdcd='"+tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDCD).toString()+"'  and (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0 and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'  group by  sr_grpcd order by sr_grpcd";
																																																																					  
			ResultSet M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		    //System.out.println(M_strSQLQRY);
		    if(!M_rstRSSET.next() || M_rstRSSET==null)
			{
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
		
			while (true)
			{	 
			    hstGRPNM.put(getRSTVAL(M_rstRSSET,"sr_grpcd","C"),getRSTVAL(M_rstRSSET,"pt_prtnm","C"));
				    
			    if(!M_rstRSSET.next())
					break;
			}
			M_rstRSSET.close() ;

		
	        tblRESEV.clrTABLE();
			
			
	        M_strSQLQRY = "select sr_grpcd,st_lotno,sum(isnull(st_stkqt,0)) st_stkqt,(isnull(sr_resqt,0)-isnull(sr_rdsqt,0)) sr_stkqt,isnull(sr_resqt,0) sr_resqt,isnull(sr_rdsqt,0) sr_rdsqt,";
	        M_strSQLQRY += "sr_strdt,sr_enddt,sr_resno from fg_stmst ,fg_srtrn  where  st_lotno=sr_lotno  and st_prdcd=sr_prdcd  and (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0  ";
	        M_strSQLQRY += " and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"' and isnull(st_stkqt,0)>0  and st_prdcd='"+tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDCD).toString()+"'  group by  sr_resno,st_lotno,sr_grpcd,isnull(sr_resqt,0) ,isnull(sr_rdsqt,0), ";
	        M_strSQLQRY += " (isnull(sr_resqt,0)-isnull(sr_rdsqt,0)) ,sr_strdt,sr_enddt order by sr_grpcd,st_lotno"; 			
	           //M_strSQLQRY ="select sr_grpcd,st_lotno,sum(isnull(st_stkqt,0)) st_stkqt,(isnull(sr_resqt,0)-isnull(sr_rdsqt,0)) sr_stkqt,isnull(sr_resqt,0) sr_resqt,isnull(sr_rdsqt,0)";
	           //M_strSQLQRY +=" sr_rdsqt,sr_strdt,sr_enddt,sr_resno from fg_stmst  left outer join fg_srtrn  on  st_lotno=sr_lotno  and st_prdcd=sr_prdcd  ";
	           //M_strSQLQRY += " and (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0 and sr_enddt> '05/24/2007' where  isnull(st_stkqt,0)>0  and st_prdcd='5111951610'  group by  ";
	           //M_strSQLQRY += " sr_resno,st_lotno,sr_grpcd,isnull(sr_resqt,0) ,isnull(sr_rdsqt,0),(isnull(sr_resqt,0)-isnull(sr_rdsqt,0)) ,sr_strdt,sr_enddt order by sr_grpcd,st_lotno";
			    System.out.println(M_strSQLQRY);
	           M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    if(!M_rstRSSET.next() || M_rstRSSET==null)
				{
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				int i =0;
				while (true)
				{	
					if(hstGRPNM.containsKey(getRSTVAL(M_rstRSSET,"sr_grpcd","C")))
					    tblRESEV.setValueAt(hstGRPNM.get(getRSTVAL(M_rstRSSET,"sr_grpcd","C")).toString(),i,TB1_GRPNM);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_lotno","C"),i,TB1_LOTNO);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_stkqt","C"),i,TB1_STKQT);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_stkqt","C"),i,TB1_SRKQT);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_resqt","C"),i,TB1_RESQT);
				    
				    
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_rdsqt","C"),i,TB1_RDSQT);
				    
				    tblRESEV.setValueAt(" " +getRSTVAL(M_rstRSSET,"sr_strdt","C"),i,TB1_STRDT);
				    tblRESEV.setValueAt(" " +getRSTVAL(M_rstRSSET,"sr_enddt","C"),i,TB1_ENDDT);
				   
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_resno","C"),i,TB1_RESNO);
				    i++;
				    
				    if(!M_rstRSSET.next())
						break;
				}
				M_rstRSSET.close() ;
		        M_strSQLQRY = "select st_lotno,sum(isnull(st_stkqt,0)) st_stkqt from fg_stmst  where isnull(st_stkqt,0)>0  and st_prdcd='"+tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDCD).toString()+"'  and st_lotno not in (select sr_lotno from fg_srtrn  where   (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0 and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"' and sr_prdcd='"+tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDCD).toString()+"' )    group by   st_lotno order by st_lotno";
	            M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    
			    if(!M_rstRSSET.next() || M_rstRSSET==null)
				{
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				while (true)
				{	
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_lotno","C"),i,TB1_LOTNO);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_stkqt","C"),i,TB1_STKQT);
				    i++;
				    
				    if(!M_rstRSSET.next())
						break;
				}
				M_rstRSSET.close() ;
	    
	    
	    pnlRESEV.setSize(300,700);
	    pnlRESEV.setPreferredSize(new Dimension(800,450));
		//pnlRETCP.setPreferredSize(new Dimension(700,250));
		//JOptionPane.showMessageDialog(pnlRESEV,"Reservation Status","Reservation Status",JOptionPane.INFORMATION_MESSAGE);					
		int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlRESEV," Reseveration status",JOptionPane.OK_CANCEL_OPTION);
		//int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDSPST,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	   }catch(Exception L_EX)
	   {
	       setMSG(L_EX,"Resveration status");
	   }
	   
	    
	}
	*/
	
	private void dispPRINT()
	{
	    
	    
	    
	    
	}
	
	void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(!vldDATA())
				return;
			if(!chkRESNO())
				return;
			
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				cl_dat.M_flgLCUPD_pbst = true;
            	if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))&&(!strSTSFL.equals("0")))
            	{
            		String L_strSQLQRY  ="Insert into FG_SRTAM Select * from FG_SRTRN where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_RESTP = '"+txtRESTP.getText() +"' AND SR_RESNO = '"+txtRESNO.getText()+"'";
            		cl_dat.exeSQLUPD(L_strSQLQRY,"" );
                }
				saveRMMST();
				flgFIRSTREC=true;
				for(int i=0;i<tblGRDTL.getRowCount() ;i++)
				{
					if (!tblGRDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						continue;
					if (((tblGRDTL.getValueAt(i,TBL_LOTNO)).toString()).length()!=8)
						continue;
					if(tblGRDTL.getValueAt(i,TBL_STKQT).toString().length()==0)
						continue;
					saveSRTRN(i);
				}
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgFIRSTREC)
			{
				JOptionPane.showMessageDialog(this,"Please Note Down Res.Number\n"+txtRESNO.getText().toString(),"",JOptionPane.ERROR_MESSAGE);
				M_strSQLQRY="update co_cdtrn set cmt_ccsvl='"+txtRESNO.getText().substring(3)+"',CMT_CHP01='Y' where cmt_cgmtp='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='FGXXSTR'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
		flgFIRSTREC=false;
			//String L_strDOCNO=genDOCNO();
			//updDOCNO(L_strDOCNO);
				
			if(cl_dat.M_flgLCUPD_pbst)
			{
				setMSG("Data saved successfully",'N');
				//JOptionPane.showMessageDialog(this,"Please, Note down the Res No. " + txtRESNO.getText().trim() ,"RES No.",JOptionPane.INFORMATION_MESSAGE);
				//cl_dat.M_btnSAVE_pbst.setEnabled(true);
			}
			else
			{
				setMSG("Error in saving data..",'E');
			}
				
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					clrCOMP_1();
				}
			}
			else
			{
				setMSG("Error in saving data..",'E');
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
		setCursor(cl_dat.M_curDFSTS_pbst);
		
	}
	
	/**
	 * Setting default values in screen components, after initializing
	 */
	private void clrCOMP_1()
	{
		try
		{
			String L_strRESNO = txtRESNO.getText().trim();
			String L_strSTRDT = txtSTRDT.getText().trim();			
			String L_strENDDT = txtENDDT.getText().trim();			
			String L_strREQDT = txtREQDT.getText().trim();			
			clrCOMP();
			txtRESNO.setText(L_strRESNO);
			txtSTRDT.setText(L_strSTRDT);
			txtENDDT.setText(L_strENDDT);
			txtREQDT.setText(L_strREQDT);
			txtRESTP.setText("01");
			txtRESDT.setText(cl_dat.M_strLOGDT_pbst);
			txtPRTTP.setText("C");
		}
		catch(Exception L_E)
			{setMSG(L_E,"clrCOMP_1");}
	}
	
	/**
	 * Function For Generate  DOC Number for 
	*/
	private String genDOCNO()
	{
		String L_strDOCTP="",L_strDOCNO  = "",  L_strCODCD = "", L_strCCSVL = "0",L_CHP02="";// for DOC
		L_strDOCTP=txtRESTP.getText().trim();
	
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,isnull(CMT_CHP02,'') CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXSTR'  and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP +"'  and  isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP02.trim().length() >0)
					{
						setMSG("dataBase IN USE",'E');
						M_rstRSSET.close();
						return null;
					}
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP02 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'FGXXSTR'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP+ "'";
			cl_dat.M_flgLCUPD_pbst = true;
			//cl_dat.exeSQLUPD(M_strSQLQRY," ");
			if(cl_dat.exeDBCMT("genDOCNO"))
			{
				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
				for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
					L_strDOCNO += "0";
				L_strCCSVL = L_strDOCNO + L_strCCSVL;										
				L_strDOCNO = L_strCODCD + L_strCCSVL;
				txtRESNO.setText(L_strDOCNO);
			}
			else 
				return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genDOCNO");
			//setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_strDOCNO;
	}	
	
    
	private void updDOCNO(String P_strDOCNO)
	{
		try
		{
			String L_strDOCTP="";
			L_strDOCTP=txtRESTP.getText().trim();
						
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP02 ='',CMT_CCSVL = '" + P_strDOCNO.substring(3,8) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP ='FGXXSTR'";
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR1_pbst.substring(3) + L_strDOCTP+"'";			
			//System.out.println("updDOCNO = "+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeDOCNO ");
			//setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	
	
	
	public void delTRANS()
	{
		try
		{
			String L_strLOTNO="";
			String L_strRCLNO="";
			String L_strRESQT="";
			for(int i=0;i<tblGRDTL.getRowCount();i++)
			{
				if(tblGRDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_strLOTNO = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_LOTNO).toString(),"");
					L_strRCLNO = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_RCLNO).toString(),"");
					L_strRESQT = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_RESQT).toString(),"");
					
					M_strSQLQRY="UPDATE FG_SRTRN SET SR_STSFL = 'X', ";
					M_strSQLQRY +="SR_TRNFL = '0', ";
					M_strSQLQRY +="SR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="SR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
					M_strSQLQRY += " WHERE SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_RESTP = '" +txtRESTP.getText().trim() + "'";
					M_strSQLQRY += " AND SR_RESNO = '" +txtRESNO.getText().trim() + "'";
					M_strSQLQRY += " AND SR_PRDTP = '" +txtPRDTP.getText().trim() + "'";
					M_strSQLQRY += " AND SR_LOTNO = '" + L_strLOTNO + "'";
					M_strSQLQRY += " AND SR_RCLNO = '" + L_strRCLNO + "' ";
					//System.out.println("delTRANS = "+M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				setMSG("Data Deleted  successfully",'N');
			}
			else
			{
				setMSG("Error in Deletion",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"delTRANS");
		}
	}
	
	public void autTRANS()
	{
		try
		{
			String L_strLOTNO="";
			String L_strRCLNO="";
			String L_strRESQT="";
			for(int i=0;i<tblGRDTL.getRowCount();i++)
			{
				if(tblGRDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_strLOTNO = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_LOTNO).toString(),"");
					L_strRCLNO = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_RCLNO).toString(),"");
					L_strRESQT = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_RESQT).toString(),"");
					
					M_strSQLQRY="UPDATE FG_SRTRN SET SR_STSFL = '1', ";
					M_strSQLQRY +="SR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="SR_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
					M_strSQLQRY += " WHERE SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_RESTP = '" +txtRESTP.getText().trim() + "'";
					M_strSQLQRY += " AND SR_RESNO = '" +txtRESNO.getText().trim() + "'";
					M_strSQLQRY += " AND SR_PRDTP = '" +txtPRDTP.getText().trim() + "'";
					M_strSQLQRY += " AND SR_LOTNO = '" + L_strLOTNO + "'";
					M_strSQLQRY += " AND SR_RCLNO = '" + L_strRCLNO + "' ";
					//System.out.println("autTRANS = "+M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				setMSG("Data Deleted  successfully",'N');
			}
			else
			{
				setMSG("Error in Deletion",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"delTRANS");
		}
	}

	
	
	/**
	 * Method to perform  check of the Remark In the Data Base, Before updating 
	 *new data in the data base.
	*/
	
	private boolean chkREMRK()
	{
		ResultSet L_rstRSSET;
		String L_strSQLQRY="";
		flgREMRK=false;
		try
		{
			L_strSQLQRY="SELECT RM_REMDS from FG_RMMST ";
			L_strSQLQRY += "where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP = '"+ strWRHTP + "'";
			L_strSQLQRY += " AND RM_TRNTP = '" + strTRNTP + "' ";
			L_strSQLQRY += " AND RM_DOCTP = '" + txtRESTP.getText().trim() + "' ";
			L_strSQLQRY += " AND RM_DOCNO = '" + txtRESNO.getText().trim() + "' and  isnull(RM_STSFL,'') <> 'X'";
			L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY );
			if(L_rstRSSET!=null)
			{
				if(L_rstRSSET.next())
				{
					flgREMRK= true;
				}
				else
					flgREMRK=false;
			}
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"chkREMRK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return flgREMRK;
	}

	
	public void getDATA()
	{
		try
		{
			int i=0;
			double L_dblRUNQT;
			String L_strGRPCD="";
			intRWCNT=0;
			vtrSTKQT.clear();
			//String L_strEUSCD="";
			String L_strPRDCD="";
			String L_strLOTNO="";
			String L_strRCLNO="";
			
			
			if(tblGRDTL.isEditing())
				tblGRDTL.getCellEditor().stopCellEditing();
			tblGRDTL.setRowSelectionInterval(0,0);
			tblGRDTL.setColumnSelectionInterval(0,0);
			//String L_strPRDCD1="";
			ResultSet L_rstRSSET=null;
			M_strSQLQRY="Select SR_RESDT,SR_AMDNO,SR_AMDDT,SR_STRDT,SR_ENDDT,";
			M_strSQLQRY+="SR_REQBY,SR_REQDT,SR_AUTBY,SR_AUTDT,SR_PRDTP,SR_PRTTP,SR_GRPCD, ";
			M_strSQLQRY+=" SR_LOTNO,SR_RCLNO,SR_PRDCD,SR_RESQT/*,SR_EUSCD*/,PR_PRDDS,SR_STSFL";
			M_strSQLQRY+=" from FG_SRTRN,CO_PRMST where PR_PRDCD=SR_PRDCD AND SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_RESTP='"+txtRESTP.getText().trim()+"' AND SR_RESNO='"+txtRESNO.getText().trim()+"'";
			M_strSQLQRY+=" AND isnull(SR_STSFL,'')<>'X'";
			if(flgOFFGRD)
				M_strSQLQRY += " and SUBSTRING(SR_PRDCD,1,6) in "+strOGLIST;
			M_strSQLQRY+=" order By SR_PRDCD,SR_LOTNO";
			
			System.out.println("Select = "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			hstRESQT.clear();
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					txtRESDT.setText(getRSTVAL(M_rstRSSET,"SR_RESDT","D"));
					
					flgAMDFL = false;
					//System.out.println("SR_STSFL"+getRSTVAL(M_rstRSSET,"SR_STSFL","C").trim());
					if (!nvlSTRVL(getRSTVAL(M_rstRSSET,"SR_STSFL","C").trim(),"0").equals("0"))
						flgAMDFL = true;
					//System.out.println(" flgAMDFL = "+flgAMDFL);
					String L_strINAMDNO = nvlSTRVL(getRSTVAL(M_rstRSSET,"SR_AMDNO","C").trim(),"00");
					String L_strINAMDDT = getRSTVAL(M_rstRSSET,"SR_AMDDT","D");
					//System.out.println(" flgAMDFL1 = "+flgAMDFL);
					
					//System.out.println(" L_strINAMDNO = "+L_strINAMDNO);
					
					if (flgAMDFL)
					{
						L_strINAMDNO = (Integer.parseInt(L_strINAMDNO)<9 ? "0" : "")+String.valueOf(Integer.parseInt(L_strINAMDNO)+1);
						L_strINAMDDT = cl_dat.M_strLOGDT_pbst;
					}
						
					lblAMDNO.setText(L_strINAMDNO);
					lblAMDDT.setText(L_strINAMDDT);
			
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				    {
						//tblGRDTL.setValueAt( new Boolean(true),i,TBL_CHKFL);
						tblGRDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
					}
					lblAMDNO.setText(getRSTVAL(M_rstRSSET,"SR_AMDNO","C"));
					lblAMDDT.setText(getRSTVAL(M_rstRSSET,"SR_AMDDT","D"));
					txtSTRDT.setText(getRSTVAL(M_rstRSSET,"SR_STRDT","D"));
					txtENDDT.setText(getRSTVAL(M_rstRSSET,"SR_ENDDT","D"));
					txtREQBY.setText(getRSTVAL(M_rstRSSET,"SR_REQBY","C"));
					txtREQDT.setText(getRSTVAL(M_rstRSSET,"SR_REQDT","D"));
					lblAUTBY.setText(getRSTVAL(M_rstRSSET,"SR_AUTBY","C"));
					lblAUTDT.setText(getRSTVAL(M_rstRSSET,"SR_AUTDT","D"));
					txtPRDTP.setText(getRSTVAL(M_rstRSSET,"SR_PRDTP","C"));
					//txtPRTTP.setText(getRSTVAL(M_rstRSSET,"SR_PRTTP","C"));
					txtPRTTP.setText("C");
					L_strGRPCD=getRSTVAL(M_rstRSSET,"SR_GRPCD","C");
					txtGRPCD.setText(L_strGRPCD);
					tblGRDTL.setValueAt(getRSTVAL(M_rstRSSET,"PR_PRDDS","C"),i,TBL_PRDDS);
					L_strLOTNO=getRSTVAL(M_rstRSSET,"SR_LOTNO","C");
					tblGRDTL.setValueAt(L_strLOTNO,i,TBL_LOTNO);
					L_strRCLNO =getRSTVAL(M_rstRSSET,"SR_RCLNO","C"); 
					tblGRDTL.setValueAt(L_strRCLNO,i,TBL_RCLNO);
					tblGRDTL.setValueAt(getRSTVAL(M_rstRSSET,"SR_RESQT","C"),i,TBL_RESQT);
					hstRESQT.put(L_strLOTNO+L_strRCLNO,getRSTVAL(M_rstRSSET,"SR_RESQT","C"));
					tblGRDTL.setValueAt(getRSTVAL(M_rstRSSET,"SR_PRDCD","C"),i,TBL_PRDCD);
					L_strPRDCD= getRSTVAL(M_rstRSSET,"SR_PRDCD","C");
					tblGRDTL.setValueAt(L_strPRDCD,i,TBL_PRDCD);
					tblGRDTL.setValueAt(getRUNQT(L_strPRDCD,i),i,TBL_RUNTO);
					getPRTNM(L_strGRPCD);
					
					vtrSTKQT.add(strPRDTP+L_strLOTNO+L_strRCLNO);
					
					i++;
					intRWCNT++;
				}
			}
			
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			
			if(intRWCNT>0)
				getTOTQT();
			
			else
			{
				setMSG("NO Data Found ",'E');
				return;
			}
				
			M_strSQLQRY= "Select RM_REMDS from FG_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP='"+strWRHTP+"' AND RM_TRNTP='"+strTRNTP+"'";
			M_strSQLQRY += " AND RM_DOCTP='"+txtRESTP.getText().trim()+"' AND RM_DOCNO='"+txtRESNO.getText().trim()+"'	";
			//System.out.println("REMARK = "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				txtREMDS.setText(getRSTVAL(M_rstRSSET,"RM_REMDS","C"));
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	*/
	private String getRSTVAL(ResultSet P_rstRSSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
			if (LP_FLDTP.equals("C"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? P_rstRSSET.getString(LP_FLDNM).toString() : "";
			//return P_rstRSSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString()," ")) : "";
			else if (LP_FLDTP.equals("N"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString(),"0") : "0";
			else if (LP_FLDTP.equals("D"))
				return P_rstRSSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(P_rstRSSET.getDate(LP_FLDNM)) : "";
			else if (LP_FLDTP.equals("T"))
				return P_rstRSSET.getTimestamp(LP_FLDNM) != null ? M_fmtLCDTM.format(P_rstRSSET.getTimestamp(LP_FLDNM)) : "";
			 //   return M_fmtLCDTM.parse(P_rstRSSET.getString(LP_FLDNM)));
			else 
				return " ";
		}
		catch (Exception L_EX)
		{
			//setMSG(L_EX,"getRSTVAL");
			
			f.publish((new LogRecord(Level.SEVERE,"msghshshshshsh "+ L_EX)));
			loger.info("Exception11");
		}
		return " ";
	} 
	
	public void getTOTQT()
	{
		String L_strSQLQRY="";
		try
		{
			for(int j=0;j<vtrSTKQT.size();j++)
			{	//L_strSQLQRY="'";
				L_strSQLQRY +="'"+(String)vtrSTKQT.get(j);
				L_strSQLQRY +="',";
				//M_strSQLQRY += L_strSQLQRY+" And ";
			}
			int i=0;
			//System.out.println("L_strSQLQRY = "+L_strSQLQRY);
			L_strSQLQRY=L_strSQLQRY.substring(0,L_strSQLQRY.length()-1);
			M_strSQLQRY = "Select LT_LOTNO,LT_RESQT,sum(ST_STKQT) st_stkqt,isnull(LT_RESQT,0) LT_RESQT,isnull(LT_RDSQT,0) LT_RDSQT from FG_STMST,PR_LTMST where ST_PRDTP=LT_PRDTP AND ST_LOTNO=LT_LOTNO ";
			M_strSQLQRY += " AND ST_RCLNO=LT_RCLNO AND ST_CMPCD=LT_CMPCD AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP + ST_LOTNO + ST_RCLNO in ("+L_strSQLQRY+")  and upper(isnull(lt_resfl,'X')) not in ('Q','H') ";
			if(flgOFFGRD)
				M_strSQLQRY += " and SUBSTRING(LT_PRDCD,1,6) in "+strOGLIST;
			M_strSQLQRY += " group By LT_LOTNO,LT_RESQT,isnull(LT_RESQT,0),isnull(LT_RDSQT,0) order by LT_LOTNO";
			
			//System.out.println("getTOTQT = "+M_strSQLQRY);
			ResultSet L_rstRSSET2=cl_dat.exeSQLQRY(M_strSQLQRY);
			while(L_rstRSSET2.next())
			{
				double L_dblSTKQT = Double.parseDouble(getRSTVAL(L_rstRSSET2,"ST_STKQT","N"));
				double L_dblRESQT = Double.parseDouble(getRSTVAL(L_rstRSSET2,"LT_RESQT","N"));
				double L_dblRDSQT = Double.parseDouble(getRSTVAL(L_rstRSSET2,"LT_RDSQT","N"));
				double L_dblAVLQT = L_dblSTKQT - (L_dblRESQT-L_dblRDSQT);
				if(L_dblAVLQT < 0)
					L_dblAVLQT = 0.000;
				tblGRDTL.setValueAt(setNumberFormat(L_dblAVLQT,3),i,TBL_STKQT);
				i++;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTOTQT");
		}
	}
	
	/** Input Verifier
 */	
	private class INPVF extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtRESNO)
				{
					M_strSQLQRY = "Select SR_RESNO from FG_SRTRN where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_RESNO = '"+txtRESNO.getText()+"'";
					if(flgOFFGRD)
						M_strSQLQRY += " and SUBSTRING(SR_PRDCD,1,6) in "+strOGLIST;
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						M_rstRSSET.close();
						return true;
					}
					else
					{
						setMSG("Invalid Reservation Number",'E');
						return false;
					}
				}
				
				if(input==txtREQDT)
				{
					if(M_fmtLCDAT.parse(txtREQDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtSTRDT.getText().trim()))>0)
					{
						setMSG("Start Date can not be Less than Requested  Date ..",'E');
						//txtFMDAT.requestFocus();
						return false;
					}	
				}
				if(input==txtENDDT)
				{
					if(M_fmtLCDAT.parse(txtSTRDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText().trim()))>0)
					{
						setMSG("Start Date can not be Less than End Date ..",'E');
						//txtFMDAT.requestFocus();
						return false;
					}	
				}
				/*if(input==txtPRTTP)
				{
					M_strSQLQRY = "Select PT_PRTTP from CO_PTMST where PT_PRTTP = '"+txtPRTTP.getText().toUpperCase()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						M_rstRSSET.close();
						return true;
					}
					else
					{
						setMSG("Invalid Party Typer",'E');
						return false;
					}
				}*/
				if(input==txtGRPCD)
				{
					M_strSQLQRY = "Select distinct PT_GRPCD,PT_PRTNM,PT_CTYNM,CMT_CODDS from CO_PTMST left outer join CO_CDTRN on CMT_CGMTP='MST' and CMT_CGSTP='COXXSTA' and CMT_CODCD=PT_STACD where   PT_PRTTP = '"+txtPRTTP.getText().toUpperCase()+"' AND PT_PRTCD='"+ txtGRPCD.getText().trim()+"' AND isnull(PT_STSFL,'')<> 'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						lblPRTDS.setText(M_rstRSSET.getString("PT_PRTNM")+" "+M_rstRSSET.getString("PT_CTYNM")+" "+M_rstRSSET.getString("CMT_CODDS"));
						M_rstRSSET.close();
						return true;
					}
					else
					{
						setMSG("Invalid Party Code ",'E');
						return false;
					}
				}
			}
			
			catch (Exception e)
			{
				setMSG(e,"INPVF");
				return false;
			}
			return true;
		}
	}
	
	/**
	 */
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				if(getSource()==tblGRDTL)
				{
					if(P_intCOLID==TBL_RESQT)
					{ 
						String L_strRESQT ="";
						String L_strSTKQT ="";
						String L_strPRDCD ="";
						
						double L_dblRESQT=0.0;
						double L_dblSTKQT=0.0;
						double L_dblDSPQT=0.0;
						       //L_dblDSPQT
						L_strRESQT=((JTextField)tblGRDTL.cmpEDITR[TBL_RESQT]).getText();
						L_strSTKQT = nvlSTRVL(tblGRDTL.getValueAt(P_intROWID,TBL_STKQT).toString().trim(),"0");
						L_strPRDCD = tblGRDTL.getValueAt(P_intROWID,TBL_PRDCD).toString().trim();
						L_dblRESQT=Double.parseDouble(L_strRESQT);
						L_dblSTKQT=Double.parseDouble(L_strSTKQT);
						String L_strRUNQT=getRUNQT(L_strPRDCD,P_intROWID);
						tblGRDTL.setValueAt(L_strRUNQT, P_intROWID,TBL_RUNTO);
						double L_dblRESQT1 = 0.000;
						if(hstRESQT.containsKey(tblGRDTL.getValueAt(P_intROWID,TBL_LOTNO).toString().trim()+tblGRDTL.getValueAt(P_intROWID,TBL_RCLNO).toString().trim()))
							L_dblRESQT1 = Double.parseDouble(hstRESQT.get(tblGRDTL.getValueAt(P_intROWID,TBL_LOTNO).toString().trim()+tblGRDTL.getValueAt(P_intROWID,TBL_RCLNO).toString().trim()).toString());
						if(L_dblSTKQT+L_dblRESQT1 < L_dblRESQT)
						{
							//JOptionPane.showMessageDialog(this,"Reserved Quantity should not exceed : "+setNumberFormat(L_dblSTKQT+L_dblRESQT1,3)+ " \n (Previous Entry = "+setNumberFormat(L_dblRESQT1,3)+"   Current Unreserved Stock = "+setNumberFormat(L_dblSTKQT,3)+")","",JOptionPane.ERROR_MESSAGE);
							setMSG("Reserved Quantity should not exceed : "+setNumberFormat(L_dblSTKQT+L_dblRESQT1,3)+ " \n (Previous Entry = "+setNumberFormat(L_dblRESQT1,3)+"   Current Unreserved Stock = "+setNumberFormat(L_dblSTKQT,3)+")",'E');
							return false;

						}
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						{
							L_dblDSPQT = Double.parseDouble(setNumberFormat(Double.parseDouble(getDSPQT(P_intROWID)),3));
							//L_dblDSPQT =Double.parseDouble( setNumberFormat(Double.parseDouble("9.9"),3);
							
							//System.out.println("L_dblDSPQT = "+L_dblDSPQT);							
							if(L_dblRESQT<L_dblDSPQT)
							{
								//JOptionPane.showMessageDialog(this,"Qyantity : "+L_dblDSPQT +" already despatched agaist Reservation ","",JOptionPane.ERROR_MESSAGE);
								setMSG("Qyantity : "+L_dblDSPQT +" already despatched agaist Reservation ",'E');
								return false;								
							}
						}
						if(tblGRDTL.getValueAt(P_intROWID+1,TBL_PRDDS).toString().trim().equals(""))
						{
							tblGRDTL.setValueAt(tblGRDTL.getValueAt(P_intROWID,TBL_PRDDS).toString().trim(),P_intROWID+1,TBL_PRDDS);
							tblGRDTL.setValueAt(tblGRDTL.getValueAt(P_intROWID,TBL_PRDCD).toString().trim(),P_intROWID+1,TBL_PRDCD);
						}
						
					}
					if(P_intCOLID==TBL_LOTNO)
					{ 
						
						String L_strLOTNO =((JTextField)tblGRDTL.cmpEDITR[TBL_LOTNO]).getText();
						if(L_strLOTNO.length()==0)
							return true;
						if(L_strLOTNO.length()!=8)
							return false;
						M_strSQLQRY = " select lt_lotno,max(lt_rclno) lt_rclno,lt_prdcd,pr_prdds,lt_remds,";
						M_strSQLQRY += "round(sum(isnull(st_stkqt,0)-isnull(st_aloqt,0))-avg((isnull(lt_resqt,0)-isnull(lt_ralqt,0)-isnull(lt_rdsqt,0))+isnull(lt_hldqt,0)+isnull(lt_retqt,0)),3) lt_ursqt, ";
						M_strSQLQRY += " lt_clsfl  from  co_prmst,pr_ltmst left outer join ";
						M_strSQLQRY += " fg_stmst on lt_prdtp = st_prdtp and lt_lotno = st_lotno ";
						M_strSQLQRY += " and lt_rclno = st_rclno where lt_prdcd = pr_prdcd  and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP='"+txtPRDTP.getText()+"' and  LT_LOTNO = '"+L_strLOTNO+"' ";
						if(flgOFFGRD)
							M_strSQLQRY += " and SUBSTRING(LT_PRDCD,1,6) in "+strOGLIST;
						M_strSQLQRY += " and lt_clsfl='9'  and upper(isnull(lt_resfl,'X')) not in ('Q','H')  group by lt_lotno,lt_prdcd,pr_prdds,lt_remds,lt_clsfl  ";
						M_strSQLQRY += " having (sum(isnull(st_stkqt,0)-isnull(st_aloqt,0)) - avg((isnull(lt_resqt,0)-isnull(lt_ralqt,0)-isnull(lt_rdsqt,0))+isnull(lt_hldqt,0)";
						M_strSQLQRY += " +isnull(lt_retqt,0)))>0 order by lt_ursqt desc ";
						//M_strSQLQRY = "Select LT_LOTNO, max(LT_RCLNO) LT_RCLNO from PR_LTMST where LT_PRDTP = '"+txtPRDTP.getText()+"' AND LT_LOTNO = '"+ L_strLOTNO + "' group by LT_LOTNO";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET.next())
						{
							if(tblGRDTL.getValueAt(P_intROWID,TBL_RCLNO).toString().length()==0)
							{
								tblGRDTL.setValueAt(M_rstRSSET.getString("LT_RCLNO"),P_intROWID,TBL_RCLNO);
								tblGRDTL.setValueAt(M_rstRSSET.getString("LT_PRDCD"),P_intROWID,TBL_PRDCD);
								tblGRDTL.setValueAt(M_rstRSSET.getString("PR_PRDDS"),P_intROWID,TBL_PRDDS);
								tblGRDTL.setValueAt(M_rstRSSET.getString("LT_REMDS"),P_intROWID,TBL_REMDS);
								tblGRDTL.setValueAt(setNumberFormat(M_rstRSSET.getDouble("LT_URSQT"),3),P_intROWID,TBL_STKQT);
								tblGRDTL.setValueAt(setNumberFormat(M_rstRSSET.getDouble("LT_URSQT"),3),P_intROWID,TBL_RESQT);
							}
							M_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Lot No.",'E');
							return false;
						}

					}
					
					if(P_intCOLID==TBL_PRDCD)
					{
						String L_strPRDDS = tblGRDTL.getValueAt(P_intROWID,TBL_PRDDS).toString().trim();
						//String L_strEUSCD = tblGRDTL.getValueAt(P_intROWID,TBL_EUSCD).toString().trim();
						//String L_strEUSDS = tblGRDTL.getValueAt(P_intROWID,TBL_EUSDS).toString().trim();
						String L_strPRDCD = tblGRDTL.getValueAt(P_intROWID,TBL_PRDCD).toString().trim();
						if(tblGRDTL.getValueAt(P_intROWID+1,TBL_PRDDS).toString().trim().length()==0)
							tblGRDTL.setValueAt(L_strPRDDS, P_intROWID+1,TBL_PRDDS);
						
						//if(tblGRDTL.getValueAt(P_intROWID+1,TBL_EUSCD).toString().trim().length()==0)
						//	tblGRDTL.setValueAt(L_strEUSCD, P_intROWID+1,TBL_EUSCD); 
						
						//if(tblGRDTL.getValueAt(P_intROWID+1,TBL_EUSDS).toString().trim().length()==0)
						//	tblGRDTL.setValueAt(L_strEUSDS, P_intROWID+1,TBL_EUSDS);
						
						if(tblGRDTL.getValueAt(P_intROWID+1,TBL_PRDCD).toString().trim().length()==0)
							tblGRDTL.setValueAt(L_strPRDCD, P_intROWID+1,TBL_PRDCD);
						
						M_strSQLQRY="select ST_PRDCD,PR_PRDDS,count(*) ST_RECCT,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) ST_STKQT from FG_STMST,CO_PRMST where ST_PRDCD=PR_PRDCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)>0) ";
						if(flgOFFGRD)
							M_strSQLQRY += " and SUBSTRING(PR_PRDCD,1,6) in "+strOGLIST;
						M_strSQLQRY += " group by ST_PRDCD,PR_PRDDS";
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET != null && L_rstRSSET.next())
							if(L_rstRSSET.getInt("ST_RECCT")>0)
								return true;
						return false;
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"TableInputVerifier");
			}
			return true;
		}
	}


	private String getRUNQT(String P_strPRDCD,int P_intROWID1)
	{
		String L_strPRDCD = "";
		String L_strLOTQT = "";
		try
		{
			double L_dblRUNQT = 0;
			for(int i = 0;i <=P_intROWID1;i++)
			{
				L_strPRDCD = tblGRDTL.getValueAt(i,TBL_PRDCD).toString().trim();
				//L_strRCLNO = tblGRDTL.getValueAt(i,TB2_RCLNO).toString().trim();
				if(L_strPRDCD.equals(P_strPRDCD))
				{
					L_strLOTQT = nvlSTRVL(tblGRDTL.getValueAt(i,TBL_RESQT).toString().trim(),"0"); 
					L_dblRUNQT = L_dblRUNQT + Double.parseDouble(L_strLOTQT);
				}
			}
			/*if(L_dblRUNQT != 0)
				L_dblRUNQT = L_dblRUNQT - LM_DLTQT;*/
			return setNumberFormat(L_dblRUNQT,3);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRUNQT");
		}
		return "0";
	}

	public void getPRTNM(String P_strGRPCD)
	{
		try
		{
			M_strSQLQRY="Select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTCD = '"+P_strGRPCD.toUpperCase()+"'" ;
			ResultSet L_rstRSSET1=cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET1!=null && L_rstRSSET1.next())
			{
				lblPRTDS.setText(L_rstRSSET1.getString("PT_PRTNM"));
				L_rstRSSET1.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"GetPRTNM");
		}
	}
	public String getDSPQT(int P_intROWID)
	{
		String L_strDSPQT="0";
		try
		{
			
			String L_strLOTNO=nvlSTRVL(tblGRDTL.getValueAt(P_intROWID,TBL_LOTNO).toString().trim(),"");
			String L_strRCLNO=nvlSTRVL(tblGRDTL.getValueAt(P_intROWID,TBL_RCLNO).toString().trim(),""); 		
			M_strSQLQRY="Select SR_DSPQT from FG_SRTRN where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_PRDTP='"+txtPRDTP.getText().trim()+"' And SR_LOTNO='"+L_strLOTNO+"' AND SR_RCLNO='"+L_strRCLNO +"' ";
			M_strSQLQRY+=" AND isnull(SR_STSFL,'')<>'X'";
			//System.out.println("getDSPQT = "+M_strSQLQRY );
			ResultSet L_rstRSSET1=cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET1!=null && L_rstRSSET1.next())
			{
				L_strDSPQT=nvlSTRVL(L_rstRSSET1.getString("SR_DSPQT"),"0");
				L_rstRSSET1.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDSPQT");
		}
		return L_strDSPQT;
		
	}


	



/** Saving record in fg_srtrn
 */
private void saveSRTRN(int LP_ROWNO)
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
           return;
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if (rdbAUTH_Y.isSelected())
			{lblAUTDT.setText(cl_dat.M_strLOGDT_pbst); lblAUTBY.setText(cl_dat.M_strUSRCD_pbst);}
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		
		strWHRSTR = "  SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_RESTP = '" +txtRESTP.getText().trim() + "'"
					+ " AND SR_RESNO = '" +txtRESNO.getText().trim() + "'"
					+ " AND SR_PRDTP = '" +txtPRDTP.getText().trim() + "'"
					+ " AND SR_LOTNO = '" + tblGRDTL.getValueAt(LP_ROWNO,TBL_LOTNO).toString() + "'"
					+ " AND SR_RCLNO = '" + tblGRDTL.getValueAt(LP_ROWNO,TBL_RCLNO).toString() + "' ";
		flgCHK_EXIST =  chkEXIST("FG_SRTRN", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in FG_SRTRN");
				return;
		}
		String L_strSTSFL = rdbAUTH_Y.isSelected() ? "1" : "0";
		if(Double.parseDouble(tblGRDTL.getValueAt(LP_ROWNO,TBL_RESQT).toString())==0.000)
			L_strSTSFL = "X";
			
		inlTBLEDIT(tblGRDTL);
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into FG_SRTRN (SR_CMPCD,SR_RESTP,SR_RESNO,SR_PRDTP,SR_LOTNO,SR_RCLNO,SR_PRDCD,SR_PRTTP,SR_GRPCD,SR_STRDT,SR_ENDDT,SR_REQBY,SR_REQDT,SR_AUTBY,SR_AUTDT,SR_MKTTP,SR_SBSCD,SR_RESDT,SR_RESQT,SR_AMDNO,SR_AMDDT,SR_TRNFL,SR_LUSBY,SR_LUPDT,SR_STSFL) values ("
			+setINSSTR("SR_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("SR_RESTP",txtRESTP.getText().trim(),"C")
			+setINSSTR("SR_RESNO",txtRESNO.getText().trim(),"C")
			+setINSSTR("SR_PRDTP",txtPRDTP.getText().trim(),"C")
			+setINSSTR("SR_LOTNO",tblGRDTL.getValueAt(LP_ROWNO,TBL_LOTNO).toString(),"C")
			+setINSSTR("SR_RCLNO",tblGRDTL.getValueAt(LP_ROWNO,TBL_RCLNO).toString(),"C")
			+setINSSTR("SR_PRDCD",tblGRDTL.getValueAt(LP_ROWNO,TBL_PRDCD).toString(),"C")
			+setINSSTR("SR_PRTTP",txtPRTTP.getText().trim(),"C")
			+setINSSTR("SR_GRPCD",txtGRPCD.getText().trim(),"C")
			+setINSSTR("SR_STRDT",txtSTRDT.getText().trim(),"D")
			+setINSSTR("SR_ENDDT",txtENDDT.getText().trim(),"D")
			+setINSSTR("SR_REQBY",txtREQBY.getText().trim(),"C")
			+setINSSTR("SR_REQDT",txtREQDT.getText().trim(),"D")
			+setINSSTR("SR_AUTBY",lblAUTBY.getText().trim(),"C")
			+setINSSTR("SR_AUTDT",lblAUTDT.getText().trim(),"D")
			+setINSSTR("SR_MKTTP",strMKTTP,"C")
			+setINSSTR("SR_SBSCD",M_strSBSCD,"C")
			+setINSSTR("SR_RESDT",txtRESDT.getText().trim(),"D")
			+setINSSTR("SR_RESQT",tblGRDTL.getValueAt(LP_ROWNO,TBL_RESQT).toString(),"N")
			+setINSSTR("SR_AMDNO",lblAMDNO.getText().trim(),"C")
			+setINSSTR("SR_AMDDT",lblAMDDT.getText(),"D")
			+setINSSTR("SR_TRNFL","0","C")
			+setINSSTR("SR_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("SR_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'"+L_strSTSFL+"')";   //+setINSSTR("SR_STSFL",'0',"C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update  FG_SRTRN set "
			+setUPDSTR("SR_PRDCD",tblGRDTL.getValueAt(LP_ROWNO,TBL_PRDCD).toString(),"C")
			+setUPDSTR("SR_PRTTP",txtPRTTP.getText().trim(),"C")
			+setUPDSTR("SR_GRPCD",txtGRPCD.getText().trim(),"C")
			+setUPDSTR("SR_STRDT",txtSTRDT.getText().trim(),"D")
			+setUPDSTR("SR_ENDDT",txtENDDT.getText().trim(),"D")
			+setUPDSTR("SR_REQBY",txtREQBY.getText().trim(),"C")
			+setUPDSTR("SR_REQDT",txtREQDT.getText().trim(),"D")
			+setUPDSTR("SR_AUTBY",lblAUTBY.getText().trim(),"C")
			+setUPDSTR("SR_AUTDT",lblAUTDT.getText().trim(),"D")
			+setUPDSTR("SR_MKTTP",strMKTTP,"C")
			+setUPDSTR("SR_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("SR_RESDT",txtRESDT.getText().trim(),"D")
			+setUPDSTR("SR_RESQT",tblGRDTL.getValueAt(LP_ROWNO,TBL_RESQT).toString(),"N")
			+setUPDSTR("SR_AMDNO",lblAMDNO.getText().trim(),"C")
			+setUPDSTR("SR_AMDDT",lblAMDDT.getText(),"D")
			+setUPDSTR("SR_TRNFL","0","C")
			+setUPDSTR("SR_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("SR_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"SR_STSFL = '"+L_strSTSFL+"' where " + strWHRSTR;  //+setUPDSTR("SR_STSFL","0","C")
		}
		//System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveRMMST : "+L_EX,'E');}
} 

/** Saving record in remark master
 */
private void saveRMMST()
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
           return;
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(!(txtREMDS.getText().length()>0))
			return;
		
		strWHRSTR = " RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP = '"+strWRHTP+"' and "
					+ "RM_TRNTP = '"+strTRNTP+"' and "
					+ "RM_DOCTP = '"+txtRESTP.getText().trim()+"' and "
					+ "RM_DOCNO = '"+txtRESNO.getText().trim()+"'";
		flgCHK_EXIST =  chkEXIST("FG_RMMST", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in FG_RMMST");
				return;
		}
		
		if(!flgCHK_EXIST)
		{
		
			M_strSQLQRY="Insert into FG_RMMST (RM_CMPCD,RM_WRHTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT,RM_STSFL) values ("
			+setINSSTR("RM_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("RM_WRHTP",strWRHTP,"C")
			+setINSSTR("RM_TRNTP",strTRNTP,"C")
			+setINSSTR("RM_DOCTP",txtRESTP.getText().trim(),"C")
			+setINSSTR("RM_DOCNO",txtRESNO.getText().trim(),"C")
			+setINSSTR("RM_REMDS",delQuote(txtREMDS.getText()),"C")
			+setINSSTR("RM_TRNFL","0","C")
			+setINSSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("RM_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')";   //+setUPDSTR("RM_STSFL",'0',"C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update  FG_RMMST set "
			+setUPDSTR("RM_REMDS",delQuote(txtREMDS.getText()),"C")
			+setUPDSTR("RM_TRNFL","0","C")
			+setUPDSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("RM_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"RM_STSFL = '0' where " + strWHRSTR;  //+setUPDSTR("RM_STSFL","0","C")
		}
		//System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveRMMST : "+L_EX,'E');}
}
	

/** Checking key in table for record existance
 */
private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
{
	boolean L_flgCHKFL = false;
	try
	{
		M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
		//System.out.println(M_strSQLQRY);
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if (L_rstRSSET != null && L_rstRSSET.next())
		{
			L_flgCHKFL = true;
			L_rstRSSET.close();
		}
	}
	catch (Exception L_EX)	
	{setMSG("Error in chkEXIST : "+L_EX,'E');}
	return L_flgCHKFL;
} 
private boolean chkRESNO()
{
	try
	{
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			return true;
		M_rstRSSET=cl_dat.exeSQLQRY("Select * from co_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXSTR' and cmt_CODCD='"+strYRDGT+txtRESTP.getText()+"'");
		System.out.println(strYRDGT+txtRESTP.getText());
		if(M_rstRSSET==null || (!M_rstRSSET.next()))
			{setMSG("Reservation series not found ..",'E'); cl_dat.M_flgLCUPD_pbst = false; return false;}
		String L_strRESNO=null;
		if(getRSTVAL(M_rstRSSET,"CMT_CHP01","C").equals("N"))
				{setMSG("Res. Series is in Use. Please retry after some time ..",'E'); 	return false;}
		cl_dat.exeSQLUPD("Update co_cdtrn set cmt_CHP01='N' where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXSTR' and cmt_CODCD='"+strYRDGT+txtRESTP.getText()+"'","setLCLUPD");
		L_strRESNO=Integer.toString(Integer.parseInt(getRSTVAL(M_rstRSSET,"CMT_CCSVL","N"))+1);
		txtRESNO.setText(L_strRESNO);
		if(L_strRESNO.length()<5)
		{
			for(int i=0;i<=5;i++)
			{
				txtRESNO.setText("0"+txtRESNO.getText());
				if(txtRESNO.getText().length()==5)
					break;
			}
		}
		L_strRESNO = txtRESNO.getText();
		txtRESNO.setText(getRSTVAL(M_rstRSSET,"CMT_CODCD","C")+L_strRESNO);
	}
    catch(Exception L_EX)
		{setMSG(L_EX,"chkRESNO"); return false;}
	return true;
}

	/** Initializing table editing before poulating/capturing data
	 */
	private void inlTBLEDIT(JTable P_tblTBLNM)
	{
		if(!P_tblTBLNM.isEditing())
			return;
        if(P_tblTBLNM.isEditing())
		    P_tblTBLNM.getCellEditor().stopCellEditing();
		P_tblTBLNM.setRowSelectionInterval(0,0);
		P_tblTBLNM.setColumnSelectionInterval(0,0);
	}


	/**  To set default status during intial display
	 */	
	private void setDFTSTS()
	{
		rdbAUTH_Y.setSelected(false);
		rdbAUTH_N.setSelected(false);
		rdbAUTH_Y.setVisible(false);
		rdbAUTH_N.setVisible(false);
		if(flgAUTRN)
		{
		    rdbAUTH_Y.setVisible(true);
		    rdbAUTH_N.setVisible(true);
		}
	}	


	/** Generating string for Insertion Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 */
	private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
	try {
		//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
		if (LP_FLDTP.equals("C"))
			 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
	 	else if (LP_FLDTP.equals("N"))
	         return   nvlSTRVL(LP_FLDVL,"0") + ",";
	 	else if (LP_FLDTP.equals("D"))
			 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
		else if (LP_FLDTP.equals("T"))
			 return   (LP_FLDVL.length()>10) ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
		else return " ";
	        }
	    catch (Exception L_EX) 
		{setMSG("Error in setINSSTR : "+L_EX,'E');}
	return " ";
	}
		



	/** Generating string for Updation Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 */
	private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 
	{
		try 
		{
			//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
			if (LP_FLDTP.equals("C"))
				 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
		 	else if (LP_FLDTP.equals("N"))
		         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
		 	else if (LP_FLDTP.equals("D"))
				 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
			else if (LP_FLDTP.equals("T"))
				 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
			else return " ";
		}
		catch (Exception L_EX) 
			{setMSG("Error in setUPDSTR : "+L_EX,'E');}
		return " ";
	}




	/**
	 * Displays Reservation status for current day 
	 *
	*/
	private void chkRESEV(String LP_PRDCD)
	{
	   try
	   {
	       String L_strGRAD="";
	   
	    if(chkRESEV.isSelected()==false)
			return;
	    
		Hashtable<String,String> hstGRPNM = new Hashtable<String,String>();
	    this.setCursor(cl_dat.M_curWTSTS_pbst);
	    if(pnlRESEV==null)
		{
	        
	        pnlRESEV=new JPanel(null);
	        add(new JLabel("Grade"),1,1,1,1,pnlRESEV,'L');
	        add(lblGRAD=new JLabel(),1,2,1,2,pnlRESEV,'L');
	        
	        //add(btnPRINT=new JButton("Print"),1,4,1,0.8,pnlRESEV,'R');
	        lblGRAD.setText(nvlSTRVL(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TBL_PRDDS).toString(),""));
	        String[] L_staCOLHD_RES = {"FL","Customer","Lot.No","Avl.Stk","Qty.Res","Qty.Dsp","Bal.Res","From","To","Res.No"};
			int[] L_inaCOLSZ_RES = {20,250,65,60,60,60,60,60,60,60};
			tblRESEV = crtTBLPNL1(pnlRESEV,L_staCOLHD_RES,500,2,1,4,8,L_inaCOLSZ_RES,new int[]{0});

			add(new JLabel("Quality Hold for Despatch"),7,1,1,3,pnlRESEV,'L');
			String[] L_staCOLHD_QLH = {"FL","Grade","Lot.No","Rcl.No.","Remark","Stock"};
			int[] L_inaCOLSZ_QLH = {20,150,80,20,250,80};
			tblQLTHLD = crtTBLPNL1(pnlRESEV,L_staCOLHD_QLH,100,8,1,12,8,L_inaCOLSZ_QLH,new int[]{0});
	        
		}
			hstGRPNM.clear();
			boolean L_flgEOF = false;
			M_strSQLQRY ="select distinct sr_grpcd,min(pt_prtnm) pt_prtnm from fg_srtrn  left outer join co_ptmst on pt_prttp='C' and pt_grpcd=sr_grpcd where sr_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sr_prdcd='"+LP_PRDCD+"'  and (isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0)))>0 and sr_enddt >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'  group by  sr_grpcd order by sr_grpcd";
																																																																					  
			ResultSet M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		    System.out.println(M_strSQLQRY);
			L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
			while (!L_flgEOF)
			{	 
			    hstGRPNM.put(getRSTVAL(M_rstRSSET,"sr_grpcd","C"),getRSTVAL(M_rstRSSET,"pt_prtnm","C"));
				    
			    L_flgEOF = (!M_rstRSSET.next()) ? true : false;
			}
			M_rstRSSET.close() ;

		
	        tblRESEV.clrTABLE();
			
			
	        M_strSQLQRY = "select sr_grpcd,st_lotno,sum(isnull(st_stkqt,0)) st_stkqt,(isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0))) sr_balqt,isnull(sr_resqt,0) sr_resqt,(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0)) sr_rdsqt,";
	        M_strSQLQRY += "sr_strdt,sr_enddt,sr_resno from fg_stmst ,fg_srtrn  where  st_lotno=sr_lotno  and st_prdcd=sr_prdcd and st_cmpcd=sr_cmpcd and st_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sr_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0)))>0  ";
	        M_strSQLQRY += " and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"' and isnull(st_stkqt,0)>0  and st_prdcd='"+LP_PRDCD+"'";
			if(flgOFFGRD)
				M_strSQLQRY += " and SUBSTRING(SR_PRDCD,1,6) in "+strOGLIST;
			M_strSQLQRY += " group by  sr_resno,st_lotno,sr_grpcd,isnull(sr_resqt,0) ,(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0)), ";
	        M_strSQLQRY += " (isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0))) ,sr_strdt,sr_enddt order by sr_grpcd,st_lotno"; 			
	           M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    System.out.println(M_strSQLQRY);
				int i =0;
				L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
				while (!L_flgEOF)
				{	
					if(hstGRPNM.containsKey(getRSTVAL(M_rstRSSET,"sr_grpcd","C")))
					    tblRESEV.setValueAt(hstGRPNM.get(getRSTVAL(M_rstRSSET,"sr_grpcd","C")).toString(),i,TB7_GRPNM);
					double L_dblAVLQT = Double.parseDouble(getRSTVAL(M_rstRSSET,"st_stkqt","C"))-Double.parseDouble(getRSTVAL(M_rstRSSET,"sr_balqt","C"));
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_lotno","C"),i,TB7_LOTNO);
				    tblRESEV.setValueAt(setNumberFormat(L_dblAVLQT,3),i,TB7_AVLQT);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_resqt","C"),i,TB7_RESQT);
				    
				    
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_rdsqt","C"),i,TB7_RDSQT);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_balqt","C"),i,TB7_BALQT);
				    
				    tblRESEV.setValueAt(" " +getRSTVAL(M_rstRSSET,"sr_strdt","C"),i,TB7_STRDT);
				    tblRESEV.setValueAt(" " +getRSTVAL(M_rstRSSET,"sr_enddt","C"),i,TB7_ENDDT);
				   
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_resno","C"),i,TB7_RESNO);
				    i++;
				    
					L_flgEOF = (!M_rstRSSET.next()) ? true : false;
				}
				M_rstRSSET.close() ;
		        M_strSQLQRY = "select st_lotno,sum(isnull(st_stkqt,0)) st_stkqt from fg_stmst  where st_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stkqt,0)>0  and st_prdcd='"+LP_PRDCD+"'  and st_lotno not in (select sr_lotno from fg_srtrn  where sr_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0)))>0 and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"' and sr_prdcd='"+LP_PRDCD+"' ) ";
				if(flgOFFGRD)
					M_strSQLQRY += " and SUBSTRING(ST_PRDCD,1,6) in "+strOGLIST;
				M_strSQLQRY += " group by   st_lotno order by st_lotno";
	            M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    
				L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
				while (!L_flgEOF)
				{	
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_lotno","C"),i,TB7_LOTNO);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_stkqt","C"),i,TB7_AVLQT);
				    i++;
				    
					L_flgEOF = (!M_rstRSSET.next()) ? true : false;
				}
				M_rstRSSET.close() ;

		
	        tblQLTHLD.clrTABLE();
			
			
	        M_strSQLQRY = "select pr_prdds,lt_lotno,lt_rclno,lt_remds,sum(isnull(st_stkqt,0)) st_stkqt from pr_ltmst,co_prmst,fg_stmst where lt_prdtp=st_prdtp and lt_cmpcd=st_cmpcd and lt_lotno=st_lotno and lt_prdcd = pr_prdcd  and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stkqt,0)>0 and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(lt_resfl,'X'))  in ('Q','H')";
			if(flgOFFGRD)
				M_strSQLQRY += " and SUBSTRING(PR_PRDCD,1,6) in "+strOGLIST;
			M_strSQLQRY += " group by pr_prdds,lt_lotno,lt_rclno,lt_remds order by pr_prdds,lt_lotno,lt_rclno";

			
	           M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    System.out.println(M_strSQLQRY);
			    if(!M_rstRSSET.next() || M_rstRSSET==null)
				i =0;
				L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
				while (!L_flgEOF)
				{	
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"PR_PRDDS","C"),i,TB8_PRDDS);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"LT_LOTNO","C"),i,TB8_LOTNO);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"LT_RCLNO","C"),i,TB8_RCLNO);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"LT_REMDS","C"),i,TB8_REMDS);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"ST_STKQT","C"),i,TB8_STKQT);
				    
				    
				    i++;
				    
					L_flgEOF = (!M_rstRSSET.next()) ? true : false;
				}
				M_rstRSSET.close() ;
	    
	    pnlRESEV.setSize(300,700);
	    pnlRESEV.setPreferredSize(new Dimension(800,450));
		//pnlRETCP.setPreferredSize(new Dimension(700,250));
		//JOptionPane.showMessageDialog(pnlRESEV,"Reservation Status","Reservation Status",JOptionPane.INFORMATION_MESSAGE);					
		int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlRESEV," Reseveration status",JOptionPane.OK_CANCEL_OPTION);
		//int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDSPST,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	   }catch(Exception L_EX)
	   {
	       setMSG(L_EX,"Resveration status");
	   }
	   
	    
	}

	
	/** Deleting Single Quotes(') from a specified string  (for Saving to Database)
	*/
      private String delQuote(String LP_STRVL)
        {
        String L_STRVL = LP_STRVL;
        String L_RETSTR="";
        StringTokenizer L_STRTKN;
        try
          {
            if(LP_STRVL==null)
               return L_STRVL;
            else if (LP_STRVL.length()==0)
               return L_STRVL;
            int L_STRLEN = LP_STRVL.length();
            int L_QOTLCN = 0;
            L_RETSTR = "";
            L_STRTKN = new StringTokenizer(L_STRVL,"'");
            while(L_STRTKN.hasMoreTokens())
            {
                 L_RETSTR +=  L_STRTKN.nextToken();
            }         
          }
          catch(Exception ex)
          {
			  setMSG(ex,"in delQuote");
          }
          return(L_RETSTR);
        }
	
	
}

