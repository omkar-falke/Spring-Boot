/**  System Job status display banner
 */

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.text.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.InputVerifier;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox; import javax.swing.JLabel;
import javax.swing.table.DefaultTableColumnModel;
import java.util.Timer;
import java.util.Calendar; 


public class co_vwjob extends JFrame implements ActionListener,KeyListener,ItemListener
{  
  
    ResultSet LM_RSLSET, L_RSLSET,L_RSLSET1,L_RSLSET2,L_RSLSET3,L_RSLSET4;
    
    co_vwjob_RightTableCellRenderer rightRENDERER;
    TableColumnModel tcm;
  
    protected SimpleDateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");	/**	For Timestamp in DB format "yyyy-MM-dd-HH.mm" */
	protected SimpleDateFormat M_fmtDBDTM=new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	protected SimpleDateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");	/**	For Timestamp in Locale format "dd/MM/yyyy HH:mm" */
	protected SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");	/** For Calender in Locale format	Required for calculations /comparision of data/datetime values */
   
    
	Hashtable<String,String> hstGRPCD = new Hashtable<String,String>();
	Hashtable<String,String> hstCODDS = new Hashtable<String,String>();
	Hashtable<String,String> hstCODD1 = new Hashtable<String,String>();
	Hashtable<String,String> hstOJTCT = new Hashtable<String,String>();
	
	
	JLabel lblSYSCD1,lblJOBDS1,lblREPDT1,lblPLNDY1,lblJOBCT1,lblSRLNO1,lblPRTNO1,lblPLNDT1,lblSTRDT1,lblENDDT1,lblACTDY1,lblSTSDS,lblREPBY,lblREMDS,lblPLNDL,lblCOMDL,lblSYSCD,lblREPTM;
	JLabel lblJOBASS;
	JLabel lblFMDAT;
	JLabel lblTODAT;
    JLabel lblPENJOB,lblCOMJOB,lblPENJOB1,lblCOMJOB1,lblONJOB,lblONJOB1;
    JLabel lblJOBTP,lblJOBOR,lblDVCNM,lblREPDP,lblSTRTM,lblALCTO,lblENDTM,lblCPAFL,lblOJTFL,lblOJTCT,lblMDLNO,lblHWSRL,lblSFTCD,lblSFVER,lblPRGCD;
  
    
    String LM_strSQL, L_strSQLQRY, L_strSQLQRY1,L_strSQLQRY2,L_strSQLQRY3,L_strSQLQRY4;	
    String msg1 = "Collecting Information ...";
	String strSRLNO;
    String LM_STRSQL = "";
    String LM_ACTTXT,LM_DSPQT,LM_LDQTY,LM_PRVDT,LM_REPQT,LM_CFWQT_R,LM_CFWQT_L,LM_DATFMT,LM_MTDSP,LM_TODAT;
    String strWHERE;
    String strFLAG="";/** String variable for selected Radio_button.*/
    
    protected boolean M_flgERROR;
    boolean flgCHK_EXIST;
    
    JComboBox cmbSYSCD;
   	JComboBox cmbJOBTP;
   	JComboBox cmbJOBOR;
	JComboBox cmbDVCNM;
	
	
	JTextField txtACTDY1;
	JTextField txtENDDT1;
	JTextField txtSTRDT1;
	JTextField txtPLNDT1;
	JTextField txtPRTNO1;
	JTextField txtSRLNO1;
	JTextField txtPLNDY1;
	JTextField txtREPDT1;
	JTextField txtJOBDS1;
	JTextField txtGRPCD;
	JTextField txtREPDP;
	JTextField txtREPTM;
	JTextField txtSTRTM;
	JTextField txtALCTO;
	JTextField txtENDTM;
	JTextField txtMDLNO;
	JTextField txtHWSRL;
	JTextField txtSFTCD;
	JTextField txtSFVER;
	JTextField txtPRGCD;
    JTextArea txtSTSDS;
	JTextField txtREPBY;
	JTextField txtREMDS;
	JTextField txtFMDAT;
	JTextField txtTODAT;
	JTextField txtJOBASS;	
	
	JCheckBox chkCPAFL;
	JCheckBox chkOJTFL;	
	
    JWindow wndMAINWD,wndDSPTBL;
    JDialog wndMAINDL;
    
    JTable tblPENJOB,tblCOMJOB,tblSUMRY,tblONJOB,tblJOPTPA,tblSRLNO;
    
    JPanel pnlDSPREC,pnlPENJOB,pnlCOMJOB, pnlSUMRY,pnlONJOB,pnlUPDIN,pnlJSRLNO;
    JPanel pnlJOPTPA,pnlPOP_PENJOB;
 
    JTabbedPane jtpMANTAB;
    
    JButton btnREFSH, btnEXIT, btnDTLON, btnDTLOF, btnSAVE;
    JRadioButton rdbUPDAT,rdbINSET;
    JRadioButton rdbACCT,rdbNACCT,rdbALL;
    ButtonGroup grpSELOPT;
    ButtonGroup grpRDBUT;
    
    private INPVF oINPVF;
  
    private JComboBox cmbJOBCT,cmbOJTCT;
  
    Cursor curWTSTS = new Cursor(Cursor.WAIT_CURSOR);
    Cursor curDFSTS = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private static String M_strCMPCD_prst = "01";
    private static String M_strCMPNM_prst = "";
     	  		   
    /** Pending Jobs table */
    final int TB1_CHKFL =0;     JCheckBox chkPENJOB;
    final int TB1_SYSCD =1;   
    final int TB1_JOBDS =2;                              
  	final int TB1_REPDT =3;   
	final int TB1_PLNDY =4;   
	final int TB1_JOBCT =5;   
	final int TB1_SRLNO =6;   
	final int TB1_PRTNO =7;   
	final int TB1_ALCTO =8;   
	                                                                               
	/** Completed Jobs table */
	final int TB2_CHKFL =0;       JCheckBox chkCOMJOB;
	final int TB2_SYSCD =1;    
    final int TB2_JOBDS =2; 
    final int TB2_STRDT =3;
    final int TB2_ENDDT =4;
    final int TB2_ACTDY =5;
   	final int TB2_PLNDY =6;
   	final int TB2_VERIT =7;
	final int TB2_JOBCT =8;    
	final int TB2_SRLNO =9;    
	final int TB2_PRTNO =10;  
	final int TB2_ALCTO =11;
	final int TB2_CPAFL =12;        
	final int TB2_OJTFL =13;      
	
	/** Summary table */
	final int TB3_CHKFL =0;  
	final int TB3_SYSCD =1;
	final int TB3_PA =2;
	final int TB3_PB=3;
	final int TB3_PC =4;
	final int TB3_TOTP =5;
	final int TB3_RA =6;
	final int TB3_RB =7;
	final int TB3_RC =8;
	final int TB3_TOTR =9;
	final int TB3_TOPR =10;
	final int TB3_CA =11;
	final int TB3_CB =12;
	final int TB3_CC =13;
	final int TB3_TOTC =14;
	final int TB3_PRCA =15;
	final int TB3_PRCB =16;
	final int TB3_PRCC =17;
	final int TB3_TOPRC =18;
	
	/** Ongoinging Jobs table */  
	final int TB4_CHKFL =0;       JCheckBox chkONJOB;
    final int TB4_SYSCD =1;  
    final int TB4_JOBDS =2;  
    final int TB4_REPDT =3; 
	final int TB4_PLNDY =4;   
	final int TB4_JOBCT =5;   
	final int TB4_SRLNO =6;   
	final int TB4_PRTNO =7; 
	final int TB4_ALCTO =8; 
	 
	final int TB5_CHKFL =0;  /** Help for Dept. No.*/ 
	final int TB5_CODCD =1; 
	final int TB5_SHRDS =2; 
	 
	 
	final int TB6_CHKFL =0;  /** Help for Serial No.*/
	final int TB6_SRLNO =1; 
	final int TB6_JOBDS =2; 
	 	 
  co_vwjob()
  {  
	  try
	  {  
		addLSTN();   
	  	opnDBCON();
	  	rightRENDERER=new co_vwjob_RightTableCellRenderer();
	  	getDSPMSG();
	  	btnDTLON.setEnabled(false);
	  	getDATA();
	  	getPENJOB();
	  	jtpMANTAB.setVisible(false);
	  	btnDTLON.setEnabled(true);
		setENBL(false);
		oINPVF=new INPVF();
		txtREPDP.setInputVerifier(oINPVF);
		txtREPBY.setInputVerifier(oINPVF);
		
	  }
	  catch(Exception L_EX)
	  {
		  System.out.println("constructor "+L_EX);
	  }
	  
  }
  
//METHOD TO ADD LISTENERS
  void addLSTN()
  { 
    M_fmtDBDAT.setLenient(false);
	M_fmtDBDTM.setLenient(false);
	M_fmtLCDAT.setLenient(false);
	M_fmtLCDTM.setLenient(false);
  }
  
  void setENBL(boolean L_flgSTAT)
  {
	//super.setENBL(L_flgSTAT);
	if(L_flgSTAT == false)
	txtGRPCD.setText(hstGRPCD.get(cmbSYSCD.getSelectedItem().toString()).toString());
	txtGRPCD.setEnabled(false);
  }	
  
  private void opnDBCON()
  {
	try
	{			
	setCONACT("01","spldata","FIMS","FIMS");		
		if(cl_dat.M_conSPDBA_pbst != null)
		{
			cl_dat.M_stmSPDBA_pbst = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			cl_dat.M_stmSPDBQ_pbst  = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			cl_dat.M_stmSPDBQ_pbst1 = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
		}	
	}
	catch(Exception L_EX)
	{
		System.out.println("opnDBCON: "+L_EX);
	}
  }
  
  private void clsDBCON()
  {
	try
	{
		if(cl_dat.M_conSPDBA_pbst != null)
		{
			cl_dat.M_conSPDBA_pbst.commit();
			cl_dat.M_stmSPDBA_pbst.close();
			cl_dat.M_stmSPDBQ_pbst.close();
			cl_dat.M_stmSPDBQ_pbst1.close();
			cl_dat.M_conSPDBA_pbst.close();
		}
	}
	catch(Exception L_EX)
	{
		System.out.println("clsDBCON: "+L_EX);
	}
  }
  
  
  public static void main(String args[])
  {
	if (args.length > 0)
	{
		M_strCMPCD_prst = args[0];
	}
	co_vwjob oco_vwjob = new co_vwjob();
	oco_vwjob.setVisible(true);
  }
  
  public Statement chkCONSTM(Connection LP_CONVAL,String LP_QRYTP)
  {
	try
	{
		if(LP_CONVAL != null)
		{
			if(LP_QRYTP.equals("Q"))	
			return LP_CONVAL.createStatement();
			else if(LP_QRYTP.equals(""))
			return LP_CONVAL.createStatement();
		}
	}
	catch(Exception L_EX)
	{
	}
	return null;			
  }
	  
  private String setDATE(java.util.Date oDT)
  {
	  String strCURDT;
	  SimpleDateFormat dtFORM;
        if (LM_DATFMT.trim().equals("DMY"))
            dtFORM = new SimpleDateFormat("dd/MM/yyyy");
        else 
            dtFORM = new SimpleDateFormat("MM/dd/yyyy");
      strCURDT = dtFORM.format(oDT);
      LM_DATFMT = " ";
	  return strCURDT;
  }
 		
  public void actionPerformed(ActionEvent L_AE)
  {
	LM_ACTTXT = L_AE.getActionCommand();  
	try 
	{ 
		if(L_AE.getSource()== cmbSYSCD)
		{
			String L_strGRPCD = hstGRPCD.get(cmbSYSCD.getSelectedItem().toString()).toString();				
			txtGRPCD.setText(L_strGRPCD);
					
			   if(L_strGRPCD.equals("HARDWARE"))
			   {
				cmbDVCNM.setEnabled(true);
				txtMDLNO.setEnabled(true);
				txtHWSRL.setEnabled(true);
			   }
			   else
			   {
				cmbDVCNM.setEnabled(false);
				txtMDLNO.setEnabled(false);
				txtHWSRL.setEnabled(false);
			   }
		}
		if(L_AE.getSource() == chkOJTFL) //On Job Training CheckBox
		{
			if(chkOJTFL.isSelected())
				cmbOJTCT.setEnabled(true);
			else
				cmbOJTCT.setEnabled(false);
		}
        if(L_AE.getSource() == rdbACCT) //Accountable
		{
			  if(rdbACCT.isSelected())
	          {	
				strFLAG="'A'";  
			  }
		}
        if(L_AE.getSource() == rdbNACCT) //NonAccountable
		{
			  if(rdbNACCT.isSelected())
			  {	
				strFLAG="'N'";
			  }
		}
        if(L_AE.getSource() == rdbALL) //All
		{
			  if(rdbALL.isSelected())
			  {	
				strFLAG="'A','N'";
			  }
		}	        
     }
     catch(Exception L_EX)
     {
		System.out.println("actionPerformed"+L_EX);			
	 }
		                        
	}
  private void getDSPMSG()
  {
		try
		{		  
			LM_DATFMT = "DMY";
			String L_strTODAT = setDATE(new java.util.Date());		  		  
			pnlDSPREC = new JPanel();
			pnlDSPREC.removeAll();
			pnlDSPREC.setLayout(null);			
						 
			rdbUPDAT = new JRadioButton("Update");
			rdbINSET = new JRadioButton("Insert");
			grpRDBUT = new ButtonGroup();
			grpRDBUT.add(rdbUPDAT);
			grpRDBUT.add(rdbINSET);
						
			jtpMANTAB = new JTabbedPane();
			pnlPENJOB = new JPanel();
			pnlCOMJOB = new JPanel();
			pnlSUMRY = new JPanel();
			pnlONJOB = new JPanel();
			pnlUPDIN = new JPanel();
											
			lblPENJOB = new JLabel("Pending Jobs");
			lblPENJOB1 = new JLabel();
			lblCOMJOB = new JLabel("Completed Jobs");
			lblCOMJOB1 = new JLabel();
			lblONJOB = new JLabel("OnGoing Jobs");
			lblONJOB1 = new JLabel();
						
			txtFMDAT = new TxtDate();
			txtTODAT = new TxtDate();
			txtJOBASS = new TxtLimit(3);
						
			lblFMDAT = new JLabel("From Date");
			lblTODAT = new JLabel("To Date");
			lblJOBASS = new JLabel("Job Assign");
			
		
								
			pnlPENJOB.setLayout(null);	
			String[] LM_TBLHD = {"","Sys","Job Description","Rep.Date","pln.Days","Cat.","Job SNO","Priority","Job Allocated"};
			int[] LM_COLSZ = {1,5,450,35,10,5,50,5,10};
			tblPENJOB = crtTBLPNL1(pnlPENJOB,LM_TBLHD,1000,1,9,900,400,LM_COLSZ,new int[]{0});	
			tblPENJOB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jtpMANTAB.add(pnlPENJOB);
					 		  
			pnlCOMJOB.setLayout(null);	
			String[] LM_TBLHD1 = {"","Sys","Job Description","Start Date","End Date","Actual","pln.Days","Deviation","Cat.","Job SNO","Priority","Job Allocated","CPAFL","OJTFL"};
			int[] LM_COLSZ1 = {1,5,250,35,35,10,10,10,5,50,5,10,1,1};
			tblCOMJOB = crtTBLPNL1(pnlCOMJOB,LM_TBLHD1,1000,1,9,900,400,LM_COLSZ1,new int[]{0,9,10});
			tblCOMJOB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tcm=tblCOMJOB.getColumnModel();
			tcm.getColumn(TB2_ACTDY).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB2_PLNDY).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB2_VERIT).setCellRenderer(rightRENDERER);
			jtpMANTAB.add(pnlCOMJOB);
			
					 	
			pnlSUMRY.setLayout(null);			
			String[] LM_TBLHD2 = {"","Sys","P(A)","P(B)","P(C)","TotalP","R(A)","R(B)","R(C)","TotalR","Total Pendings","C(A)","C(B)","C(C)","TotalC","(P+R-C)(A)","(P+R-C)(B)","(P+R-C)(C)","Total(P+R-C)"};
			int[] LM_COLSZ2 = {1,20,20,20,20,40,20,20,20,40,40,20,20,20,40,20,20,20,40};	
			tblSUMRY = crtTBLPNL1(pnlSUMRY,LM_TBLHD2,1000,1,9,900,400,LM_COLSZ2,new int[]{0});
			tcm=tblSUMRY.getColumnModel();
			tcm.getColumn(TB3_PA).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_PB).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_PC).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_TOTP).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_RA).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_RB).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_RC).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_RB).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_TOTR).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_TOPR).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_CA).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_CB).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_CC).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_TOTC).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_PRCA).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_PRCB).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_PRCC).setCellRenderer(rightRENDERER);
			tcm.getColumn(TB3_TOPRC).setCellRenderer(rightRENDERER);
			jtpMANTAB.add(pnlSUMRY);
			
					  
			pnlONJOB.setLayout(null);	
			String[] LM_TBLHD3 = {"","Sys","Job Description","Rep. Date","pln.Days","Cat.","Job SNO","Priority","Job Allocated"};
			int[] LM_COLSZ3 = {1,5,450,35,10,5,50,5,10};
			tblONJOB = crtTBLPNL1(pnlONJOB,LM_TBLHD3,1000,1,9,900,400,LM_COLSZ3,new int[]{0});	
			tblONJOB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jtpMANTAB.add(pnlONJOB);
					   
			pnlUPDIN.setLayout(null);	
			jtpMANTAB.add(pnlUPDIN);	
					  			
			Font f = new Font("TimesRoman",Font.PLAIN,15);
						
			btnREFSH= new JButton("Refresh");
			btnEXIT= new JButton("Exit");
			btnDTLON= new JButton("Detail On");
			btnDTLOF= new JButton("Detail Off");
			btnSAVE= new JButton("Save");	
						
			rdbACCT = new JRadioButton("Accountable",true);
			rdbNACCT = new JRadioButton("NonAccountable",false);
			rdbALL = new JRadioButton("All",false);
	            
            grpSELOPT = new ButtonGroup();
			grpSELOPT.add(rdbACCT);
			grpSELOPT.add(rdbNACCT);
			grpSELOPT.add(rdbALL);
			rdbUPDAT.setBounds(100,30,150,20);
			rdbINSET.setBounds(0,30,70,20);
			rdbACCT .setSize(100,20);
			rdbACCT .setLocation(400,30);
			rdbNACCT.setSize(130,20);
			rdbNACCT.setLocation(505,30);
			rdbALL.setSize(100,20);
			rdbALL.setLocation(640,30);
            

			lblPENJOB.setForeground(Color.blue);
			lblPENJOB.setFont(f);
			lblCOMJOB.setForeground(Color.blue);
			lblCOMJOB.setFont(f);
			lblPENJOB1.setForeground(Color.black);
			lblPENJOB1.setFont(f);
			lblCOMJOB1.setForeground(Color.black);
			lblCOMJOB1.setFont(f);
			lblONJOB.setForeground(Color.blue);
			lblONJOB.setFont(f);
			lblONJOB1.setForeground(Color.black);
			lblONJOB1.setFont(f);
			lblFMDAT.setForeground(Color.blue);
			lblFMDAT.setFont(f);
			lblTODAT.setForeground(Color.blue);
			lblTODAT.setFont(f);
			
			
			lblPENJOB.setBounds(0,0,500,20);
			lblPENJOB1.setBounds(110,0,80,20);
			lblCOMJOB.setBounds(200,0,500,20);
			lblCOMJOB1.setBounds(320,0,80,20);
			lblONJOB.setBounds(370,0,500,20);
			lblONJOB1.setBounds(490,0,80,20);
			lblFMDAT.setBounds(0,50,500,20);
			lblTODAT.setBounds(190,50,500,20);
			lblJOBASS.setBounds(350,50,500,20);
			
			
			txtFMDAT.setBounds(80,50,90,20);
			txtTODAT.setBounds(250,50,90,20);
			txtJOBASS.setBounds(420,50,80,20);
			btnSAVE.setBounds(510,50,70,20);
			btnREFSH.setBounds(590,50,80,20);
			btnDTLON.setBounds(680,50,85,20);
			btnDTLOF.setBounds(770,50,90,20);
			btnEXIT.setBounds(870,50,60,20);
			
			
			jtpMANTAB.setBounds(20,95,900,900);
			jtpMANTAB.addTab("Pending Jobs ",pnlPENJOB);
			jtpMANTAB.addTab("Completed Jobs ",pnlCOMJOB);
			jtpMANTAB.addTab("Summary",pnlSUMRY);
			jtpMANTAB.addTab("Ongoing Jobs",pnlONJOB);
			jtpMANTAB.addTab("Update/Insert",pnlUPDIN);
			pnlDSPREC.add(jtpMANTAB);	  
			
			pnlDSPREC.add(rdbUPDAT);
			pnlDSPREC.add(rdbINSET);
			pnlDSPREC.add(rdbACCT);
			pnlDSPREC.add(rdbNACCT);
			pnlDSPREC.add(rdbALL);
			pnlDSPREC.add(lblPENJOB);
			pnlDSPREC.add(lblCOMJOB);
			pnlDSPREC.add(lblPENJOB1);
			pnlDSPREC.add(lblCOMJOB1);
			pnlDSPREC.add(lblCOMJOB);
			pnlDSPREC.add(lblONJOB);
			pnlDSPREC.add(lblONJOB1);
			pnlDSPREC.add(btnSAVE);
			pnlDSPREC.add(lblJOBASS);
			pnlDSPREC.add(txtJOBASS);
			
			pnlDSPREC.add(btnREFSH);
			pnlDSPREC.add(btnEXIT);
			pnlDSPREC.add(btnDTLON);
			pnlDSPREC.add(btnDTLOF);
			pnlDSPREC.add(lblFMDAT);
			pnlDSPREC.add(lblTODAT);
			pnlDSPREC.add(txtFMDAT);
			pnlDSPREC.add(txtTODAT);
						
			
			btnREFSH.setEnabled(true);
			btnDTLOF.setEnabled(false);
			btnEXIT.setEnabled(true);
			
			wndMAINWD = new JWindow(this);
			wndMAINDL = new JDialog(this);
			LM_DATFMT = "DMY";
			opnDBCON();
			
			rdbUPDAT.setEnabled(false);
			rdbINSET.setEnabled(false);
			
			txtFMDAT.setText("01"+"/"+L_strTODAT.substring(3,5)+"/"+L_strTODAT.substring(6,10));
			txtTODAT.setText(L_strTODAT);
			txtJOBASS.addKeyListener(this);
			txtFMDAT.addKeyListener(this);
			txtTODAT.addKeyListener(this);
				
		 try
		 {
			L_strSQLQRY = " SELECT CP_CMPNM FROM CO_CPMST where CP_CMPCD ='"+M_strCMPCD_prst+"'";
			L_RSLSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_RSLSET !=null && L_RSLSET.next())
			M_strCMPNM_prst=L_RSLSET.getString("CP_CMPNM");
		 }
	     catch(Exception E)
	     {
		    System.out.println(E+":CO_CPMST");
		 }
			
			wndMAINDL.setTitle("Supreme Petrochem Limited" + "  ("+M_strCMPNM_prst+")                       "+ " System Job Details as on "+setDATE(new java.util.Date()));
			wndMAINWD = new JWindow(wndMAINDL);
			wndMAINDL.setBounds(0,0,950,120);
			wndMAINDL.getContentPane().add(pnlDSPREC);
			wndMAINDL.toFront();
			wndMAINDL.setVisible(true);
			btnEXIT.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				
				setVisible(false);
				dispose();
				System.exit(0);
			}});
			btnREFSH.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				opnDBCON();
				refrsh();
				clrCOMP();
				getDATA();
			}});
			btnDTLON.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				opnDBCON();
				refrsh();
				getDATA();
				wndMAINDL.setSize(950,1000);
				wndMAINDL.validate();
				btnDTLOF.setEnabled(true);
				rdbUPDAT.setEnabled(true);
				rdbINSET.setEnabled(true);
				jtpMANTAB.setVisible(true);
				btnDTLON.setEnabled(false);
				
			}});
			btnDTLOF.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				refrsh();
				clrCOMP();
				pnlDSPREC.remove(tblPENJOB);
				pnlDSPREC.remove(tblCOMJOB);
				pnlDSPREC.remove(tblSUMRY);
				pnlDSPREC.remove(tblONJOB);
				pnlDSPREC.remove(pnlPOP_PENJOB);
				pnlDSPREC.updateUI();
				wndMAINDL.setSize(950,120);
				rdbUPDAT.setEnabled(false);
				rdbINSET.setEnabled(false);
				jtpMANTAB.setVisible(false);
				btnDTLOF.setEnabled(false);
				btnDTLON.setEnabled(true);
			}});
			btnSAVE.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent L_AE) {
					getJOBDATA();
					
				}});
				
				rdbUPDAT.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent L_AE) {
			          if(rdbUPDAT.isSelected())
					  {	
			        	 txtSRLNO1.setEnabled(true); 
			        	 clrCOMP();
						 setTEXTFIELD();
					  }	
		
					}});
				
				rdbINSET.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent L_AE) {
			          if(rdbINSET.isSelected())
					  {	
			        	  txtSRLNO1.setEnabled(false);
					  }	
				}});   
													
			addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
				System.exit(0);
			 }});
	  }
	  catch(Exception L_EX)
	  {
		  System.out.println("Refresh: "+L_EX.getMessage());
	  }
  }
    
  public String nvlSTRVL(String LP_VARVL, String LP_DEFVL)
  {
	  try
	  {
		if (LP_VARVL != null)
			LP_VARVL = LP_VARVL;
		else
			LP_VARVL = LP_DEFVL;
	  }
	  catch (Exception L_EX)
	  {
		System.out.println("nvl "+L_EX);
	  }
	  return LP_VARVL;
  }
  
  private void refrsh()
  { 
      try
      {
    	  	   	   
    	  for(int L_ROWNO3=0; L_ROWNO3<tblSUMRY.getRowCount(); L_ROWNO3++)
      	  {
    		tblSUMRY.setValueAt(new Boolean(false),L_ROWNO3,TB3_CHKFL);
	      	tblSUMRY.setValueAt("",L_ROWNO3,TB3_SYSCD);	
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_PA);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_PB);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_PC);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_TOTP);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_RA);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_RB);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_RC);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_TOTR);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_TOPR);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_CA);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_CB);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_CC);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_TOTC);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_PRCA);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_PRCB);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_PRCC);
		    tblSUMRY.setValueAt("",L_ROWNO3,TB3_TOPRC);
	     }
      	 for(int L_ROWNO1=0; L_ROWNO1<tblPENJOB.getRowCount(); L_ROWNO1++)
      	 {
      		tblPENJOB.setValueAt(new Boolean(false),L_ROWNO1,TB1_CHKFL);
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_SYSCD);	
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_JOBDS);
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_REPDT);
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_PLNDY);
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_JOBCT);
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_SRLNO);
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_PRTNO);
      		tblPENJOB.setValueAt("",L_ROWNO1,TB1_ALCTO);
    	 }
      	 for(int L_ROWNO2=0; L_ROWNO2<tblCOMJOB.getRowCount(); L_ROWNO2++)
      	 {
      		tblCOMJOB.setValueAt(new Boolean(false),L_ROWNO2,TB2_CHKFL);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_SYSCD);	
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_JOBDS);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_ENDDT);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_PLNDY);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_JOBCT);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_SRLNO);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_PRTNO);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_ALCTO);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_STRDT);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_ACTDY);
      		tblCOMJOB.setValueAt("",L_ROWNO2,TB2_VERIT);
      		tblCOMJOB.setValueAt(new Boolean(false),L_ROWNO2,TB2_CPAFL);
      		tblCOMJOB.setValueAt(new Boolean(false),L_ROWNO2,TB2_OJTFL);	
	     }
      	 for(int L_ROWNO4=0; L_ROWNO4<tblONJOB.getRowCount(); L_ROWNO4++)
      	 {
      		tblONJOB.setValueAt(new Boolean(false),L_ROWNO4,TB4_CHKFL);
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_SYSCD);	
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_JOBDS);
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_REPDT);
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_PLNDY);
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_JOBCT);
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_SRLNO);
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_PRTNO);
      		tblONJOB.setValueAt("",L_ROWNO4,TB4_ALCTO);
	     }
      	
      }
      catch (Exception L_EX) 
      {
		System.out.println("refrsh :"+L_EX);
	  }
  }
  
  /**
	 * Method to fetch data from the database for Specified Serial Number.
	 */
	private void getSRLDATA()
	{
	   try
	   { 
			String M_strSQLQRY = "";
			ResultSet M_rstRSSET;
			int L_intROWNO =0,L_intREPDP=0,L_dblPLNDY=0;
			java.sql.Date L_tmpDATE;
			String L_strTEMP="",L_strSTRTM="",L_strENDTM="";
			java.sql.Timestamp L_tmpTIME;
			int L_intSRLNO=0;
            
			
			M_strSQLQRY  =" Select * from SA_JBMST "
				         +" where JB_SRLNO = '" + txtSRLNO1.getText()+"'";
		
			M_rstRSSET =  cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{					
					cmbSYSCD.removeActionListener(this);
					cmbJOBTP.removeActionListener(this);
					cmbJOBCT.removeActionListener(this);
					cmbOJTCT.removeActionListener(this);
										
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_SYSCD"),"").toString();
					cmbSYSCD.setSelectedItem(L_strTEMP +" "+hstCODD1.get(L_strTEMP));
					txtGRPCD.setText(hstGRPCD.get(L_strTEMP +" "+hstCODD1.get(L_strTEMP).toString()).toString());
					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBTP"),"");
					cmbJOBTP.setSelectedItem(L_strTEMP+" "+hstCODDS.get(L_strTEMP).toString());
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBOR"),"");
					cmbJOBOR.setSelectedItem(L_strTEMP+" "+hstCODDS.get(L_strTEMP).toString());
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_JOBCT"),"");
					cmbJOBCT.setSelectedItem(L_strTEMP+" "+hstCODDS.get(L_strTEMP).toString());
					if(nvlSTRVL(M_rstRSSET.getString("JB_OJTFL"),"").equals("Y"))
					{	
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("JB_OJTCT"),"");
						cmbOJTCT.setSelectedItem(L_strTEMP+" "+hstOJTCT.get(L_strTEMP).toString());
						cmbOJTCT.setEnabled(true);
					}
					else
						cmbOJTCT.setEnabled(false);
					//cmbGRPCD.addActionListener(this);
					cmbSYSCD.addActionListener(this);
					cmbJOBTP.addActionListener(this);
					cmbJOBCT.addActionListener(this);
					cmbOJTCT.addActionListener(this);
					
					txtREPBY.setText(nvlSTRVL(M_rstRSSET.getString("JB_REPBY"),""));
					txtREPDP.setText(nvlSTRVL(M_rstRSSET.getString("JB_REPDP"),""));
					L_tmpTIME = M_rstRSSET.getTimestamp("JB_REPDT");
		 			if (L_tmpTIME != null)
					{
						L_strSTRTM = M_fmtLCDTM.format(L_tmpTIME);
						txtREPDT1.setText(L_strSTRTM.substring(0,10));
						if(L_strSTRTM.toString().length()>=11)
						txtREPTM.setText(L_strSTRTM.substring(11));
					}
										
					txtJOBDS1.setText(nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),""));
					cmbDVCNM.setSelectedItem(nvlSTRVL(M_rstRSSET.getString("JB_DVCNM"),""));
					txtMDLNO.setText(nvlSTRVL(M_rstRSSET.getString("JB_MDLNO"),""));
					txtHWSRL.setText(nvlSTRVL(M_rstRSSET.getString("JB_HWSRL"),""));
					txtSFTCD.setText(nvlSTRVL(M_rstRSSET.getString("JB_SFTCD"),""));
					txtSFVER.setText(nvlSTRVL(M_rstRSSET.getString("JB_SWVER"),""));
					txtPRGCD.setText(nvlSTRVL(M_rstRSSET.getString("JB_PRGCD"),""));
					
					L_tmpDATE = M_rstRSSET.getDate("JB_PLNDT");					
					if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
					else
						L_strTEMP="";
					txtPLNDT1.setText(L_strTEMP);
					txtPLNDY1.setText(nvlSTRVL(M_rstRSSET.getString("JB_PLNDY"),""));
					
					L_tmpTIME = M_rstRSSET.getTimestamp("JB_STRDT");
		 			if (L_tmpTIME != null)
					{
						L_strSTRTM = M_fmtLCDTM.format(L_tmpTIME);
						txtSTRDT1.setText(L_strSTRTM.substring(0,10));
						if(L_strSTRTM.toString().length()>=11)
							txtSTRTM.setText(L_strSTRTM.substring(11));
					}
					txtALCTO.setText(nvlSTRVL(M_rstRSSET.getString("JB_ALCTO"),""));
					L_tmpTIME = M_rstRSSET.getTimestamp("JB_ENDDT");
		 			if (L_tmpTIME != null)
					{
						L_strENDTM = M_fmtLCDTM.format(L_tmpTIME);
						txtENDDT1.setText(L_strENDTM.substring(0,10));
						if(L_strENDTM.toString().length()>=11)
							txtENDTM.setText(L_strENDTM.substring(11));
					}					
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("JB_REMDS"),""));
					txtPRTNO1.setText(nvlSTRVL(M_rstRSSET.getString("JB_PRTNO"),""));
					if(nvlSTRVL(M_rstRSSET.getString("JB_CPAFL"),"").equals("Y"))
							chkCPAFL.setSelected(true);
					if(nvlSTRVL(M_rstRSSET.getString("JB_OJTFL"),"").equals("Y"))
							chkOJTFL.setSelected(true);
					txtSTSDS.setText(nvlSTRVL(M_rstRSSET.getString("JB_STSDS"),""));
				}
				M_rstRSSET.close();
			}
	   }
	   catch(Exception L_E)
	   {
		 System.out.println("getdata"+L_E);
	   }
  }
    
  private void getPENJOB()
  {
		try
		{
			
			String M_strSQLQRY = "";
			ResultSet M_rstRSSET;
			String L_strSTSFL = "0";
			LM_DATFMT = "MDY";
			String L_strTODAT = setDATE(new java.util.Date());
			
			if(pnlPOP_PENJOB==null)
			{
				pnlPOP_PENJOB=new JPanel(null);
  
				Font f = new Font("TimesRoman",Font.PLAIN,15);
					
				cmbSYSCD = new JComboBox();				
				txtGRPCD = new JTextField();
				txtJOBDS1 = new TxtLimit(100);
				txtREPDT1 = new TxtDate();
				txtPLNDY1 =  new TxtNumLimit(5.2);
				cmbJOBCT = new JComboBox();
				txtSRLNO1 =  new TxtLimit(8);
				txtPRTNO1 =  new TxtNumLimit(2);
				txtPLNDT1 = new TxtDate();
				txtSTRDT1 = new TxtDate();
				txtENDDT1 = new TxtDate();
				txtACTDY1 = new JTextField();
				txtSTSDS = new JTextArea();	
				txtREPBY = new TxtLimit(3);
				txtREMDS = new TxtLimit(100);
				
				cmbJOBTP = new JComboBox();
				cmbJOBOR = new JComboBox();
				cmbDVCNM = new JComboBox();
				txtREPDP = new TxtNumLimit(3);
				txtREPTM = new TxtTime();
				txtSTRTM = new TxtTime();
				txtALCTO = new TxtLimit(3);
				txtENDTM = new TxtTime();
				chkCPAFL = new JCheckBox();
				chkOJTFL = new JCheckBox();
				cmbOJTCT = new JComboBox();
				txtMDLNO = new TxtLimit(20);
				txtHWSRL = new TxtLimit(30);
				txtSFTCD = new TxtLimit(15);
				txtSFVER = new TxtLimit(15);
				txtPRGCD = new TxtLimit(10);
			
				
			
				lblSYSCD = new JLabel("System Code");  							
				lblJOBDS1 = new JLabel("Job Description");							
				lblREPDT1 = new JLabel("Date Time");								
				lblPLNDY1 = new JLabel("Pln. Days");							
				lblJOBCT1 = new JLabel("Categary");	             
				lblSRLNO1 = new JLabel("Serial No");				
				lblPRTNO1 = new JLabel("Priority No(10-90)");			
				lblPLNDT1 = new JLabel("Plan Date");						
				lblSTRDT1 = new JLabel("Start Date ");								
				lblENDDT1 = new JLabel("End Date ");								
				lblACTDY1 = new JLabel("Actual Days ");
				lblSTSDS = new JLabel("Status Description");
				lblREPBY = new JLabel("Reported By");
				lblREMDS = new JLabel("Remark Description");
				
				
				lblJOBTP = new JLabel("Job Type");
				lblJOBOR = new JLabel("Job Origin");
				lblDVCNM = new JLabel("H/W Device");
				lblREPDP = new JLabel("Rep.Dept.");
				lblSTRTM = new JLabel("Start Time");
				lblALCTO = new JLabel("Allocated To");
				lblENDTM = new JLabel("End Time");
				lblCPAFL = new JLabel("CAPA Flag");
				lblOJTFL = new JLabel("OJT Flag");
				lblOJTCT = new JLabel("Training");
				lblMDLNO = new JLabel("Model No.");
				lblHWSRL = new JLabel("Device SR.NO.");
				lblSFTCD = new JLabel("S/W Name");
				lblSFVER = new JLabel("S/W Version");
				lblPRGCD = new JLabel("ERP Module");
				lblPLNDL = new JLabel("Planning  Detail");	
				lblCOMDL = new JLabel("Complaint Registration Detail");	
				lblREPTM = new JLabel("Rep.Time");		
				
				lblSYSCD.setForeground(Color.black);
				lblSYSCD.setFont(f);
				lblJOBDS1.setForeground(Color.black);
				lblJOBDS1.setFont(f);
				lblREPDT1.setForeground(Color.black);
				lblREPDT1.setFont(f);
				lblPLNDY1.setForeground(Color.black);
				lblPLNDY1.setFont(f);
				lblJOBCT1.setForeground(Color.black);
				lblJOBCT1.setFont(f);
				lblSRLNO1.setForeground(Color.black);
				lblSRLNO1.setFont(f);
				lblPRTNO1.setForeground(Color.black);
				lblPRTNO1.setFont(f);
				lblPLNDT1.setForeground(Color.black);
				lblPLNDT1.setFont(f);
				lblSTRDT1.setForeground(Color.black);
				lblSTRDT1.setFont(f);
				lblENDDT1.setForeground(Color.black);
				lblENDDT1.setFont(f);
				lblACTDY1.setForeground(Color.black);
				lblACTDY1.setFont(f);
				lblSTSDS.setForeground(Color.black);
				lblSTSDS.setFont(f);
				lblREPBY.setForeground(Color.black);
				lblREPBY.setFont(f);
				lblREMDS.setForeground(Color.black);
				lblREMDS.setFont(f);
										
				lblREPTM.setForeground(Color.black);
				lblREPTM.setFont(f);
				lblJOBTP.setForeground(Color.black);
				lblJOBTP.setFont(f);
				
				lblJOBOR.setForeground(Color.black);
				lblJOBOR.setFont(f);
				lblDVCNM.setForeground(Color.black);
				lblDVCNM.setFont(f);
				lblREPDP.setForeground(Color.black);
				lblREPDP.setFont(f);
				lblSTRTM.setForeground(Color.black);
				lblSTRTM.setFont(f);
				lblALCTO.setForeground(Color.black);
				lblALCTO.setFont(f);
				lblENDTM.setForeground(Color.black);
				lblENDTM.setFont(f);
				lblCPAFL.setForeground(Color.black);
				lblCPAFL.setFont(f);
				lblOJTFL.setForeground(Color.black);
				lblOJTFL.setFont(f);
				lblOJTCT.setForeground(Color.black);
				lblOJTCT.setFont(f);
				lblMDLNO.setForeground(Color.black);
				lblMDLNO.setFont(f);
				lblHWSRL.setForeground(Color.black);
				lblHWSRL.setFont(f);
				lblSFTCD.setForeground(Color.black);
				lblSFTCD.setFont(f);
				lblSFVER.setForeground(Color.black);
				lblSFVER.setFont(f);
				lblPRGCD.setForeground(Color.black);
				lblPRGCD.setFont(f);
				lblPLNDL.setForeground(Color.blue);
				lblPLNDL.setFont(f);
				lblCOMDL.setForeground(Color.blue);
				lblCOMDL.setFont(f);
						
				lblCOMDL.setBounds(50,30,250,20);
				lblSYSCD.setBounds(50,60,90,20);
				lblJOBOR.setBounds(50,90,80,20);
				lblREPBY.setBounds(50,120,90,20);
				lblJOBDS1.setBounds(50,150,120,20);		
				lblPLNDL.setBounds(50,180,200,20);
				lblPLNDT1.setBounds(50,210,90,20);
				lblSTRDT1.setBounds(50,240,90,20);
				lblENDDT1.setBounds(50,270,90,20);
				lblREMDS.setBounds(50,300,120,20);
				lblPRTNO1.setBounds(50,330,120,20);
				lblDVCNM.setBounds(50,360,90,20);
				lblHWSRL.setBounds(50,390,200,20);
				lblSFTCD.setBounds(50,420,200,20);
				lblPRGCD.setBounds(460,420,200,20);
				lblSTSDS.setBounds(50,480,500,20);
				
				cmbSYSCD.setBounds(150,60,150,20);
				cmbJOBOR.setBounds(150,90,110,20);
				txtREPBY.setBounds(150,120,90,20);
				txtJOBDS1.setBounds(170,150,450,20);
				txtPLNDT1.setBounds(150,210,90,20);
				txtSTRDT1.setBounds(150,240,90,20);
				txtENDDT1.setBounds(150,270,90,20);
				txtREMDS.setBounds(190,300,500,20);
				txtPRTNO1.setBounds(190,330,60,20);
				cmbDVCNM.setBounds(150,360,110,20);
				txtHWSRL.setBounds(150,390,90,20);
				txtSFTCD.setBounds(150,420,90,20);
				txtPRGCD.setBounds(550,420,150,20);
				
				JScrollPane scrollPane=new JScrollPane(txtSTSDS);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	 			scrollPane.setBounds(180,480,700,100);
				
				
				lblJOBCT1.setBounds(280,90,80,20);
				lblREPDP.setBounds(280,120,70,20);
				
				
				lblPLNDY1.setBounds(250,210,90,20);
				lblSTRTM.setBounds(250,240,90,20);
				lblENDTM.setBounds(250,270,90,20);
				lblCPAFL.setBounds(300,330,70,20);
				
				txtGRPCD.setBounds(350,60,80,20);
				cmbJOBCT.setBounds(350,90,80,20);
				txtREPDP.setBounds(350,120,90,20);	
				
				txtPLNDY1.setBounds(350,210,90,20);
				txtSTRTM.setBounds(350,240,90,20);
				txtENDTM.setBounds(350,270,90,20);
				chkCPAFL.setBounds(270,330,20,20);
				
				txtMDLNO.setBounds(350,360,90,20);
				txtSFVER.setBounds(350,420,90,20);
				
				
				lblMDLNO.setBounds(270,360,90,20);
				lblSFVER.setBounds(250,420,90,20);
				
				lblJOBTP.setBounds(450,60,80,20);
				lblSRLNO1.setBounds(450,90,90,20);
				lblREPDT1.setBounds(450,120,90,20);
				
				lblREPTM.setBounds(600,120,90,20);
				lblALCTO.setBounds(450,240,90,20);
				lblACTDY1.setBounds(450,270,90,20);
				lblOJTFL.setBounds(420,330,70,20);
				lblOJTCT.setBounds(510,330,90,20);
				cmbOJTCT.setBounds(580,330,300,20);
				
				cmbJOBTP.setBounds(550,60,120,20);
				txtSRLNO1.setBounds(550,90,90,20);
				txtREPDT1.setBounds(550,120,90,20);
				txtREPTM.setBounds(660,120,90,20);
				txtALCTO.setBounds(550,240,90,20);
				txtACTDY1.setBounds(550,270,90,20);
				chkOJTFL.setBounds(400,330,20,20);
				
				txtJOBDS1.addKeyListener(this);
				txtPLNDT1.addKeyListener(this);
				txtSRLNO1.addKeyListener(this);
				txtENDDT1.addKeyListener(this);
				txtREMDS.addKeyListener(this);
				txtPRTNO1.addKeyListener(this);
				txtSFTCD.addKeyListener(this);
				txtPRGCD.addKeyListener(this);
				txtREPBY.addKeyListener(this);
				txtPLNDY1.addKeyListener(this);
				txtENDTM.addKeyListener(this);
				txtMDLNO.addKeyListener(this);
				txtSFVER.addKeyListener(this);
				txtSTSDS.addKeyListener(this);
				txtREPDP.addKeyListener(this);
				txtSTRDT1.addKeyListener(this);
				txtACTDY1.addKeyListener(this);
				txtHWSRL.addKeyListener(this);
				cmbJOBCT.addKeyListener(this);
				txtREPDT1.addKeyListener(this);
				txtREPTM.addKeyListener(this);
				txtSTRTM.addKeyListener(this);
				txtALCTO.addKeyListener(this);
				chkOJTFL.addKeyListener(this);
				cmbOJTCT.addKeyListener(this);
				chkCPAFL.addKeyListener(this);
				//chkCHKFL.addActionListener(this);
				txtSRLNO1.addActionListener(this);
				chkOJTFL.addActionListener(this);
				cmbOJTCT.setEnabled(false);
				M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
					        +" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXGRP'"
					        +" AND isnull(CMT_STSFL,'')<>'X'";			
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					hstCODD1.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					M_rstRSSET.close();
				}
												
				M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
					        +" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXJOB'"
					        +" AND isnull(CMT_STSFL,'')<>'X'";			
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					String L_strCODCD="",L_strCODDS="";
					while(M_rstRSSET.next())
					{
							L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
							L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							hstCODDS.put(L_strCODCD,L_strCODDS);
							if(L_strCODCD.substring(0,1).equals("1"))
								cmbJOBTP.addItem(L_strCODCD+" "+L_strCODDS);
							else if(L_strCODCD.substring(0,1).equals("2"))
								cmbJOBOR.addItem(L_strCODCD+" "+L_strCODDS);					
							else if(L_strCODCD.substring(0,1).equals("4"))
								cmbDVCNM.addItem(L_strCODCD+" "+L_strCODDS);
							else 
								cmbJOBCT.addItem(L_strCODCD+" "+L_strCODDS);
					 }
					 M_rstRSSET.close();
				  }
					
					cmbSYSCD.removeActionListener(this);
					M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS,CMT_CHP01 from CO_CDTRN where"
						        +" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXGRP'"
						        +" AND isnull(CMT_STSFL,'')<>'X' order by CMT_CODCD";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					
					if(M_rstRSSET != null)
					{
					   String L_strCODCD="",L_strCODDS="";	
					   while(M_rstRSSET.next())
					   {
							L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							cmbSYSCD.addItem(L_strCODCD);
							hstGRPCD.put(L_strCODCD,nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),""));
					   }
					   M_rstRSSET.close();
					}
					   cmbSYSCD.addActionListener(this);
					   
					   
					cmbOJTCT.removeActionListener(this);
					M_strSQLQRY = " SELECT CMT_CODCD,CMT_CODDS,CMT_CHP01 from CO_CDTRN where"
								        +" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'SAXXCMP'"
								        +" AND isnull(CMT_STSFL,'')<>'X' order by CMT_CODCD";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					//System.out.println("training"+M_strSQLQRY);
						
					if(M_rstRSSET != null)
					{
						 String L_strCODCD="",L_strCODDS="";	
						 while(M_rstRSSET.next())
						 {
							  L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							  cmbOJTCT.addItem(L_strCODCD);
							  hstOJTCT.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						 }
						 M_rstRSSET.close();
					}
					cmbOJTCT.addActionListener(this);
						
				pnlPOP_PENJOB.add(lblSYSCD);
				pnlPOP_PENJOB.add(cmbSYSCD);
				pnlPOP_PENJOB.add(txtGRPCD);
				pnlPOP_PENJOB.add(lblJOBDS1);
				pnlPOP_PENJOB.add(txtJOBDS1);
				pnlPOP_PENJOB.add(lblREPDT1);
				pnlPOP_PENJOB.add(txtREPDT1);
				pnlPOP_PENJOB.add(lblPLNDY1);
				pnlPOP_PENJOB.add(txtPLNDY1);
				pnlPOP_PENJOB.add(lblJOBCT1);
				pnlPOP_PENJOB.add(cmbJOBCT);
				pnlPOP_PENJOB.add(lblSRLNO1);
				pnlPOP_PENJOB.add(txtSRLNO1);
				pnlPOP_PENJOB.add(lblPRTNO1);
				pnlPOP_PENJOB.add(txtPRTNO1);
				pnlPOP_PENJOB.add(lblPLNDT1);
				pnlPOP_PENJOB.add(txtPLNDT1);
				pnlPOP_PENJOB.add(lblSTRDT1);
				pnlPOP_PENJOB.add(txtSTRDT1);
				pnlPOP_PENJOB.add(lblENDDT1);
				pnlPOP_PENJOB.add(txtENDDT1);
				pnlPOP_PENJOB.add(lblACTDY1);
				pnlPOP_PENJOB.add(txtACTDY1);
				pnlPOP_PENJOB.add(lblSTSDS);
				pnlPOP_PENJOB.add(scrollPane);
				pnlPOP_PENJOB.add(lblREPBY);
				pnlPOP_PENJOB.add(txtREPBY);
				pnlPOP_PENJOB.add(lblREMDS);
				pnlPOP_PENJOB.add(txtREMDS);
				pnlPOP_PENJOB.add(lblPRGCD);
				pnlPOP_PENJOB.add(txtPRGCD);
				pnlPOP_PENJOB.add(lblCOMDL);
				pnlPOP_PENJOB.add(lblPLNDL);
				
				
				pnlPOP_PENJOB.add(lblJOBTP);
				pnlPOP_PENJOB.add(cmbJOBTP);
				pnlPOP_PENJOB.add(lblJOBOR);
				pnlPOP_PENJOB.add(cmbJOBOR);
				pnlPOP_PENJOB.add(lblDVCNM);
				pnlPOP_PENJOB.add(cmbDVCNM);
				pnlPOP_PENJOB.add(lblREPDP);
				pnlPOP_PENJOB.add(txtREPDP);
				pnlPOP_PENJOB.add(txtREPTM);
				pnlPOP_PENJOB.add(lblSTRTM);
				pnlPOP_PENJOB.add(txtSTRTM);
				pnlPOP_PENJOB.add(lblALCTO);
				pnlPOP_PENJOB.add(txtALCTO);
				pnlPOP_PENJOB.add(lblENDTM);
				pnlPOP_PENJOB.add(txtENDTM);
				pnlPOP_PENJOB.add(lblCPAFL);
				pnlPOP_PENJOB.add(chkCPAFL);
				pnlPOP_PENJOB.add(lblOJTFL);
				pnlPOP_PENJOB.add(chkOJTFL);
				pnlPOP_PENJOB.add(lblOJTCT);
				pnlPOP_PENJOB.add(cmbOJTCT);
				pnlPOP_PENJOB.add(lblMDLNO);
				pnlPOP_PENJOB.add(txtMDLNO);
				pnlPOP_PENJOB.add(lblHWSRL);
				pnlPOP_PENJOB.add(txtHWSRL);
				pnlPOP_PENJOB.add(lblSFTCD);
				pnlPOP_PENJOB.add(txtSFTCD);
				pnlPOP_PENJOB.add(lblSFVER);
				pnlPOP_PENJOB.add(txtSFVER);
				pnlPOP_PENJOB.add(lblACTDY1);
				pnlPOP_PENJOB.add(txtACTDY1);
																
			}
			pnlPOP_PENJOB.setSize(1000,600);
			pnlPOP_PENJOB.setPreferredSize(new Dimension(1000,600));	
			pnlUPDIN.add(pnlPOP_PENJOB);
		}
		catch (Exception L_EX)
		{
			System.out.println("getPENJOB :"); 	
		}
  }
 		
  private void getJOBDATA()
  {
	    try
	    {
		    String M_strSQLQRY = "";
			ResultSet M_rstRSSET;
			String L_strSTSFL = "0";
			LM_DATFMT = "DMY";
			String L_strTODAT = setDATE(new java.util.Date());
		     
			if (!vldDATA())
		    return;	
			
			if(rdbINSET.isSelected()==true)
			{				
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgLCUPD_pbst = true;
					String L_strTEMP1 = "";	
					
					int L_CURMTH = Integer.valueOf(L_strTODAT.substring(3,5)).intValue();
					int L_CURYER = Integer.valueOf(L_strTODAT.substring(8,10)).intValue();
					if ( L_CURMTH >= 1 && L_CURMTH <= 6)
					{
					    String L_SECYER = (L_CURYER - 1) + " ";
					    String L_CURYR =  L_CURYER + " ";
					    if (L_SECYER.trim().length() == 1)
							L_SECYER = "0" + L_SECYER.trim();
					    if (L_CURYR.trim().length() == 1)
					        L_CURYR = "0" + L_CURYR.trim();
					    cl_dat.M_strFNNYR_pbst = L_SECYER.trim() + L_CURYR.trim();
					}
					else
					{
						String L_SECYER = (L_CURYER + 1) + " ";
					    String L_CURYR =  L_CURYER + " ";
					    if (L_SECYER.trim().length() == 1)
					            L_SECYER = "0" + L_SECYER.trim();
					    if (L_CURYR.trim().length() == 1)
					            L_CURYR = "0" + L_CURYR.trim();
					    cl_dat.M_strFNNYR_pbst = L_CURYR.trim() + L_SECYER.trim();
					}
				
					M_strSQLQRY = "Select CMT_CCSVL from CO_CDTRN where CMT_CGMTP ='D"+M_strCMPCD_prst+"' AND CMT_CGSTP ='COXXJOB' AND isnull(CMT_STSFL,'')<>'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					
					if(M_rstRSSET!= null)
					{
						if(M_rstRSSET.next())				
							L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),""); 				
						M_rstRSSET.close();
					}						
					int L_intTSTNO = Integer.valueOf(L_strTEMP1).intValue()+1;
					//System.out.println("getJOB:"+L_intTSTNO);
					strSRLNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + txtGRPCD.getText().substring(0,2).toUpperCase()
						+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
						+ String.valueOf(L_intTSTNO);					
					txtSRLNO1.setText(strSRLNO);
								
					if((txtSTRDT1.getText().length()>0)&&(txtENDDT1.getText().length()>0))
						L_strSTSFL = "9";
					else
						L_strSTSFL = "0";
									
					M_strSQLQRY = "INSERT INTO SA_JBMST(JB_GRPCD,JB_SYSCD,JB_JOBTP,JB_JOBOR,JB_JOBCT,"
								+"JB_SRLNO,JB_REPBY,JB_REPDP,JB_REPDT,JB_JOBDS,JB_DVCNM,JB_MDLNO,JB_HWSRL,"
								+"JB_SFTCD,JB_SWVER,JB_PRGCD,JB_PLNDT,JB_PLNDY,JB_STRDT,JB_ALCTO,JB_ENDDT,JB_REMDS,JB_PRTNO,JB_CPAFL,JB_OJTFL,JB_OJTCT,JB_STSDS,"
								+"JB_TRNFL,JB_STSFL,JB_LUSBY,JB_LUPDT)"
								+" VALUES('" + txtGRPCD.getText().toString().substring(0,2).trim().toUpperCase()+"',"
								+"'"+ cmbSYSCD.getSelectedItem().toString().substring(0,2)+"',"
								+"'"+ cmbJOBTP.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
								+"'"+ cmbJOBOR.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
								+"'"+cmbJOBCT.getSelectedItem().toString().substring(0,1).toUpperCase() +"',"
								+"'"+ strSRLNO +"',"
								+"'"+ txtREPBY.getText().trim().toUpperCase() +"',"
								+"'"+ txtREPDP.getText().trim() +"',";
					if(txtREPDT1.getText().length()>0)
						M_strSQLQRY +=	"'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtREPDT1.getText().trim()+" "+txtREPTM.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";
										
					M_strSQLQRY += "'" + txtJOBDS1.getText().trim() +"',";
					if(txtGRPCD.getText().toString().equals("HARDWARE"))
						M_strSQLQRY += "'"+ cmbDVCNM.getSelectedItem().toString() +"',";
					else 
						M_strSQLQRY += "'',";
					
					M_strSQLQRY += "'"+ txtMDLNO.getText().trim() +"',"
								+"'"+ txtHWSRL.getText().trim() +"',"
								+"'"+ txtSFTCD.getText().trim() +"',"
								+"'"+ txtSFVER.getText().trim() +"',"
								+"'"+ txtPRGCD.getText().trim() +"',";					
					if(txtPLNDT1.getText().length()>0)
						M_strSQLQRY +=	"'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPLNDT1.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";					
					if(txtPLNDY1.getText().length()>0)
						M_strSQLQRY += txtPLNDY1.getText()+",";
					else
						M_strSQLQRY += 0 +",";															
					if(txtSTRDT1.getText().length()>0)
						M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSTRDT1.getText().trim()+" "+txtSTRTM.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";
					M_strSQLQRY += "'"+ txtALCTO.getText().trim().toUpperCase()+"',";
					if(txtENDDT1.getText().length()>0)
						M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtENDDT1.getText().trim()+" "+txtENDTM.getText().trim()))+"',";
					else
						M_strSQLQRY += null +",";
					M_strSQLQRY += "'" + txtREMDS.getText().trim()+"',";
					M_strSQLQRY += "'" + txtPRTNO1.getText().trim()+"',";
					M_strSQLQRY += chkCPAFL.isSelected() ? "'Y'," : "'N',";
					M_strSQLQRY += chkOJTFL.isSelected() ? "'Y'," : "'N',";
					if(chkOJTFL.isSelected())
						M_strSQLQRY +="'"+cmbOJTCT.getSelectedItem().toString().substring(0,2)+"',";
					M_strSQLQRY += "'" + txtSTSDS.getText().trim()+"',"
					+"'0',"	//TRNFL					
					+"'"+ L_strSTSFL+"',"	//STSFL
					+"'sys',"
					+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"')";
               		//System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					setCursor(cl_dat.M_curDFSTS_pbst);	
					// Update the Serial Number 
					if(cl_dat.M_flgLCUPD_pbst == true)
					{
						JOptionPane.showMessageDialog(this,"Please Note the Job Number\n"+strSRLNO,"",JOptionPane.ERROR_MESSAGE);
						M_strSQLQRY = "Update CO_CDTRN set CMT_CCSVL ='"+ strSRLNO.substring(3) +"' where CMT_CGMTP ='D"+M_strCMPCD_prst+"' AND CMT_CGSTP ='COXXJOB'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						//System.out.println(M_strSQLQRY);
					}
					
				}
								
				if(rdbUPDAT.isSelected()==true)
				{
				  					
			    	if((txtSTRDT1.getText().length()>0)&&(txtENDDT1.getText().length()>0))
						L_strSTSFL = "9";
			    	
					else
						L_strSTSFL = "0";
								    	
			    	L_strSQLQRY ="UPDATE SA_JBMST SET "						
						+" JB_SYSCD = '"+cmbSYSCD.getSelectedItem().toString().substring(0,2)+"',"
						+" JB_JOBTP = '"+ cmbJOBTP.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
						+" JB_JOBOR = '"+ cmbJOBOR.getSelectedItem().toString().substring(0,2).toUpperCase() +"',"
						+" JB_JOBCT = '"+cmbJOBCT.getSelectedItem().toString().substring(0,1).toUpperCase() +"',"						
						+" JB_REPBY = '"+ txtREPBY.getText().trim() +"',"
						+" JB_REPDP = "+ txtREPDP.getText().trim() +",";
			    
					if(txtREPDT1.getText().length()>0)
					L_strSQLQRY += "JB_REPDT ='" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtREPDT1.getText().trim()+" "+txtREPTM.getText().trim()))+"',";
					L_strSQLQRY +=" JB_JOBDS = '" + txtJOBDS1.getText().trim() +"',";
					L_strSQLQRY +=" JB_DVCNM = '"+ cmbDVCNM.getSelectedItem().toString().substring(0,2).toUpperCase()  +"',";
					L_strSQLQRY += " JB_MDLNO = '"+ txtMDLNO.getText().trim() +"',"
						+" JB_HWSRL = '"+ txtHWSRL.getText().trim() +"',"
						+" JB_SFTCD = '"+ txtSFTCD.getText().trim() +"',"
						+" JB_SWVER = '"+ txtSFVER.getText().trim() +"',"
						+" JB_PRGCD = '"+ txtPRGCD.getText().trim() +"',";
				 
					if(txtPLNDT1.getText().length()>0)
						L_strSQLQRY += "JB_PLNDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPLNDT1.getText().trim()))+"',";
					else
						L_strSQLQRY += "JB_PLNDT = "+ null +",";					
					if(txtPLNDY1.getText().length()>0)
						L_strSQLQRY +=" JB_PLNDY = "+ txtPLNDY1.getText()+",";
					else
						L_strSQLQRY +=" JB_PLNDY = "+ 0+",";															
					if(txtSTRDT1.getText().length()>0)
						L_strSQLQRY += "JB_STRDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSTRDT1.getText().trim()+" "+txtSTRTM.getText().trim()))+"',";
					else
						L_strSQLQRY += "JB_STRDT = "+ null +",";
					L_strSQLQRY += " JB_ALCTO ='"+ txtALCTO.getText().trim().toUpperCase()+"',";
					if(txtENDDT1.getText().length()>0)
						L_strSQLQRY += "JB_ENDDT = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtENDDT1.getText().trim()+" "+txtENDTM.getText().trim()))+"',";
					else
						L_strSQLQRY +="JB_ENDDT = "+ null +",";
					L_strSQLQRY += "JB_REMDS = '" + txtREMDS.getText().trim()+"',";
					L_strSQLQRY += "JB_PRTNO = '" + txtPRTNO1.getText().trim()+"',";
					L_strSQLQRY += chkCPAFL.isSelected() ? "JB_CPAFL = 'Y'," : "JB_CPAFL = 'N',";
					L_strSQLQRY += chkOJTFL.isSelected() ? "JB_OJTFL = 'Y'," : "JB_OJTFL = 'N',";
					if(chkOJTFL.isSelected())
						L_strSQLQRY +=" JB_OJTCT = '"+cmbOJTCT.getSelectedItem().toString().substring(0,2)+"',";
					L_strSQLQRY += "JB_STSDS = '" + txtSTSDS.getText().trim()+"',"
					+" JB_TRNFL = '0',"	//TRNFL
					+" JB_STSFL = '"+ L_strSTSFL+ "',"	//STSFL
					+" JB_LUSBY = 'sys',"
					+" JB_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'"
					+" WHERE JB_SRLNO = '"+txtSRLNO1.getText().trim()+"'";						
					
					cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
					//System.out.println("inside update"+L_strSQLQRY);
							   
				}
				if(cl_dat.exeDBCMT("Save")) 
				{
					clrCOMP();
					//System.out.println("Record save successfully");
				}
				else
				{
					System.out.println("Error in  saving data");
				}
        }					      
		catch (Exception L_EX)
		{
			System.out.println("inside"+L_EX); 
		}
  }
  
  
  class INPVF extends InputVerifier 
  {
	public boolean verify(JComponent input)
	{
		try
		{		  
				String L_strSQLQRY = " ";
				ResultSet L_RSLSET;	
		
				if(input == txtREPBY)
				{
				   L_strSQLQRY = " SELECT EP_DPTCD from HR_EPMST where"
					            +" EP_SHRNM ='"+txtREPBY.getText().trim().toUpperCase()+"' and ep_stsfl<>'U' and ep_lftdt is null";
				   //System.out.println("INPVF txtREPBY : "+L_strSQLQRY);
				   L_RSLSET = cl_dat.exeSQLQRY(L_strSQLQRY);
					
				   if(L_RSLSET!=null && L_RSLSET.next())
				   {
						txtREPBY.setText(txtREPBY.getText().trim().toUpperCase());
						txtREPDP.setText(L_RSLSET.getString("EP_DPTCD"));
				   }
				   else
				   {
					   txtREPBY.requestFocus();
					   JOptionPane.showMessageDialog(null,"Enter Valid Name","MESSAGE", JOptionPane.ERROR_MESSAGE);
				   }
				}
				if(input == txtREPDP)
				{
					L_strSQLQRY = " select count(*) CNT from HR_EPMST where";
					L_strSQLQRY +=" ep_stsfl<>'U' and ep_lftdt is null";
					L_strSQLQRY +=" AND EP_SHRNM = '"+txtREPBY.getText()+"'";

					//System.out.println("INPVF txtREPDP : "+L_strSQLQRY);
				    L_RSLSET = cl_dat.exeSQLQRY(L_strSQLQRY);
					if(L_RSLSET.next() && L_RSLSET!=null)
					{
						if(L_RSLSET.getInt("CNT")==0)
						{
						   txtREPDP.setText("");	
						   JOptionPane.showMessageDialog(null,"Enter Valid Department code","MESSAGE", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
		  }
		  catch(Exception E_VR)
		  {
			System.out.println("class INPVF");		
		  }
		  return true;
	 }
  }
  
  boolean vldDATA()
  { 
	 try
	 {
			LM_DATFMT = "DMY";
			String L_strTODAT = setDATE(new java.util.Date());
			String strTIME = "";
			String L_strTIME = setTIME(strTIME);
			
			 if(rdbUPDAT.isSelected()== false && rdbINSET.isSelected()== false)
			 {
				JOptionPane.showMessageDialog(null,"Pls select Update or Insert radio buttons","MESSAGE", JOptionPane.ERROR_MESSAGE);
				rdbUPDAT.requestFocus();
				rdbINSET.requestFocus();
				return false;
			 }
					    
			 if(txtSTRDT1.getText().trim().length() != 0)
			 {			    
				if(txtSTRTM.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null,"Start Time can not be blank..","MESSAGE", JOptionPane.ERROR_MESSAGE);
					txtSTRTM.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtSTRDT1.getText().trim()).compareTo(M_fmtLCDAT.parse(L_strTODAT))>0)
				{
					JOptionPane.showMessageDialog(null,"Start Date  can not be greater than current Date ...","MESSAGE", JOptionPane.ERROR_MESSAGE);
					txtSTRDT1.requestFocus();
					return false;
				}
				
			  }	
						
			  if(txtREPDT1.getText().trim().length() == 0)
			  {
				   JOptionPane.showMessageDialog(null,"Reported Date can not be blank..","MESSAGE", JOptionPane.ERROR_MESSAGE);
				   txtSTRTM.requestFocus();
				   return false;	
			  }
			  if(txtREPTM.getText().trim().length() == 0)
			  {
					JOptionPane.showMessageDialog(null,"Reported Time can not be blank..","MESSAGE", JOptionPane.ERROR_MESSAGE);
					txtSTRTM.requestFocus();
					return false;
			  }
			  if(M_fmtLCDAT.parse(txtREPDT1.getText().trim()).compareTo(M_fmtLCDAT.parse(L_strTODAT))>0)
			  {
					JOptionPane.showMessageDialog(null,"Reported Date  can not be greater than current Date ...","MESSAGE", JOptionPane.ERROR_MESSAGE);
					txtSTRDT1.requestFocus();
					return false;
			  }
			  if(txtREPDP.getText().trim().length() == 0)
			  {
				  	JOptionPane.showMessageDialog(null,"Reported Dept. can not be blank..","MESSAGE", JOptionPane.ERROR_MESSAGE);	
			  }
			  if(txtREPBY.getText().trim().length() == 0)
			  {
				  	JOptionPane.showMessageDialog(null,"Reported By can not be blank..","MESSAGE", JOptionPane.ERROR_MESSAGE);	
			  }
			  if(txtENDDT1.getText().trim().length() != 0)
			  {
				  	if(txtENDTM.getText().trim().length() == 0)
				  	{
				  		JOptionPane.showMessageDialog(null,"End Time can not be blank...","MESSAGE", JOptionPane.ERROR_MESSAGE);
				  		txtSTRTM.requestFocus();
				  		return false;
				  	}
				  	if(M_fmtLCDAT.parse(txtENDDT1.getText().trim()).compareTo(M_fmtLCDAT.parse(L_strTODAT))>0)
				  	{
				  		JOptionPane.showMessageDialog(null,"End Date can not be greater than current Date...","MESSAGE", JOptionPane.ERROR_MESSAGE);
				  		txtENDDT1.requestFocus();
				  		return false;
				  	}
			  }
			  if((txtSTRDT1.getText().trim().length() != 0) && (txtENDDT1.getText().trim().length() != 0))
			  {
				  	if(M_fmtLCDTM.parse(txtSTRDT1.getText().trim()+" "+txtSTRTM.getText().trim()).compareTo(M_fmtLCDTM.parse(txtENDDT1.getText().trim()+" "+txtENDTM.getText().trim()))>0)
				  	{
				  		JOptionPane.showMessageDialog(null,"End Date Time must be greater than Start Date Time ...","MESSAGE", JOptionPane.ERROR_MESSAGE);
				  		txtENDDT1.requestFocus();
				  		return false;
				  	}
			}
			if(txtPRTNO1.getText().trim().length() == 0)
			{
					JOptionPane.showMessageDialog(null,"Please Enter Priority No...","MESSAGE", JOptionPane.ERROR_MESSAGE);
					txtPRTNO1.requestFocus();				
					return false;				
			}	
		    if(txtPLNDY1.getText().length() == 0)
			{
					JOptionPane.showMessageDialog(null,"Planned Days Cannot be Blank....","MESSAGE", JOptionPane.ERROR_MESSAGE);
					txtPLNDY1.requestFocus();
					return false;				
			}
			if(txtJOBDS1.getText().length() == 0)
			{
					JOptionPane.showMessageDialog(null,"Job Description Cannot be Blank...","MESSAGE", JOptionPane.ERROR_MESSAGE);
					return false;				
			}
		    if(txtPLNDY1.getText().length() != 0)
			{
				double L_dblPLNDY = Double.valueOf(txtPLNDY1.getText()).doubleValue();
				if((L_dblPLNDY >= 3) && (cmbJOBCT.getSelectedIndex() != 0))
				{
					JOptionPane.showMessageDialog(null,"Please Select Appropriate Job Category according to Planned Days..","MESSAGE", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			    if((L_dblPLNDY < 3)&&(L_dblPLNDY > 1) && (cmbJOBCT.getSelectedIndex() != 1))
				{
					JOptionPane.showMessageDialog(null,"Please Select Appropriate Job Category according to Planned Days..","MESSAGE", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				if((L_dblPLNDY < 1) && (cmbJOBCT.getSelectedIndex() != 2))
				{
					JOptionPane.showMessageDialog(null,"Please Select Appropriate Job Category according to Planned Days...","MESSAGE", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		    
	  }   
	  catch(Exception x)
	  {
		  return false;
	  }
	  return true;
  }
  
  private String setTIME(String strTIME )
  {
	 String strMINUTE;
	 String strHOUR;
     Calendar oTIME = Calendar.getInstance(); 
     strTIME =  String.valueOf(oTIME.get(Calendar.HOUR_OF_DAY)+ ":" + oTIME.get(Calendar.MINUTE)); 
     //System.out.println("min"+oTIME.get(Calendar.MINUTE));
     int  intMIN = oTIME.get(Calendar.MINUTE); 
     int  intHOUR = oTIME.get(Calendar.HOUR_OF_DAY);
     if(intMIN < 10 && intHOUR < 10)
     {	 
    	 strMINUTE = String.valueOf("0"+oTIME.get(Calendar.MINUTE));
    	 strHOUR = String.valueOf("0"+oTIME.get(Calendar.HOUR_OF_DAY));
    	 strTIME =  String.valueOf(strHOUR+ ":" + strMINUTE); 
    	 return  strTIME;
     } 
     if(intMIN < 10)
     {	 
    	 strMINUTE = String.valueOf("0"+oTIME.get(Calendar.MINUTE));
    	 strTIME =  String.valueOf(oTIME.get(Calendar.HOUR_OF_DAY)+ ":" + strMINUTE); 
    	 return  strTIME;
     } 
     if(intHOUR < 10)
     {	 
	     strHOUR = String.valueOf("0"+oTIME.get(Calendar.HOUR_OF_DAY));
		 strTIME =  String.valueOf(strHOUR+ ":" +oTIME.get(Calendar.MINUTE)); 
		 return  strTIME;
     }
     return  strTIME;
  }
   
  public void keyPressed(KeyEvent L_KE)
  { 
	    LM_DATFMT = "DMY";
		String L_strTODAT = setDATE(new java.util.Date());
		String strTIME = "";
		String L_strTIME = setTIME(strTIME);
	  
		try
		{
		    if (L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
		    	
		    	if(L_KE.getSource()== txtFMDAT)
		    		txtTODAT.requestFocus();
		    	if(L_KE.getSource()== txtTODAT)
		    		txtJOBASS.requestFocus();
		    	
		    	if(L_KE.getSource()==txtJOBASS)
		    	{
		    		txtJOBASS.setText(txtJOBASS.getText().toUpperCase());
		    		refrsh();
					getDATA();
					wndMAINDL.setSize(950,1000);
					wndMAINDL.validate();
					btnDTLOF.setEnabled(true);
					rdbUPDAT.setEnabled(true);
					rdbINSET.setEnabled(true);
					jtpMANTAB.setVisible(true);
					btnDTLON.setEnabled(false);
		    	}
				if(L_KE.getSource()==txtSRLNO1)
				{
					getSRLDATA();
					txtREPBY.requestFocus();
				}
			    if(L_KE.getSource() == txtREPBY)
					txtREPDP.requestFocus();
			    	
				if(L_KE.getSource() == txtREPDP)
				{
					txtREPDT1.requestFocus();
					if(txtREPDT1.getText().length()==0)
					txtREPDT1.setText(L_strTODAT);	
				}
				if(L_KE.getSource() == txtREPDT1)
				{
					txtREPTM.requestFocus();
					if(txtREPTM.getText().length()==0)
					txtREPTM.setText(L_strTIME);
				}
			    if(L_KE.getSource() == txtREPTM)
				{
					txtJOBDS1.requestFocus();		
				}				
					
				if(L_KE.getSource() == txtJOBDS1)
				{
					txtPLNDT1.requestFocus();
					if(txtPLNDT1.getText().length()==0)
					txtPLNDT1.setText(L_strTODAT);					
				}
			    if(L_KE.getSource() == cmbDVCNM)
				{
					txtMDLNO.requestFocus();
						
				}
				if(L_KE.getSource() == txtMDLNO)
				{
					txtHWSRL.requestFocus();	
				}
				if(L_KE.getSource() == txtHWSRL)
				{
					txtSFTCD.requestFocus();	
				}
				if(L_KE.getSource() == txtSFTCD)
				{
					txtSFVER.requestFocus();
				}
				if(L_KE.getSource() == txtSFVER)
				{
					txtPRGCD.requestFocus();
				}
				if(L_KE.getSource() == txtPRGCD)
				{
					txtSTSDS.requestFocus();
				}				
				if(L_KE.getSource() == txtPLNDT1)
				{
					txtPLNDY1.requestFocus();					
				}
				if(L_KE.getSource() == txtPLNDY1)
				{
					txtSTRDT1.requestFocus();
					if(txtSTRDT1.getText().length()==0)
					txtSTRDT1.setText(L_strTODAT); 	
				}
				if(L_KE.getSource() == txtSTRDT1)
				{
					txtSTRTM.requestFocus();
					if(txtSTRTM.getText().length()==0)
					txtSTRTM.setText(L_strTIME);
				}
				if(L_KE.getSource() == txtSTRTM)
				{
					txtALCTO.requestFocus();	 	
				}
				if(L_KE.getSource() == txtALCTO)
				{
					txtALCTO.setText(txtALCTO.getText().toUpperCase());
					txtENDDT1.requestFocus();
					if(txtENDDT1.getText().length()==0)
					txtENDDT1.setText(L_strTODAT);	
				}
				if(L_KE.getSource() == txtENDDT1)
				{
					txtENDTM.requestFocus();
					if(txtENDTM.getText().length()==0)
					txtENDTM.setText(L_strTIME);
				}
				if(L_KE.getSource() == txtENDTM)
				{
					int intYRS1 = Integer.parseInt(txtSTRDT1.getText().trim().substring(6,10));
					int intMTH1 = Integer.parseInt(txtSTRDT1.getText().trim().substring(3,5));
					int intDAY1 = Integer.parseInt(txtSTRDT1.getText().trim().substring(0,2));
			       
					int intYRS2 = 0;
					int intMTH2 = 0;
					int intDAY2 = 0;
				
					if(!(txtENDDT1.getText() == null))
					{	
					  intYRS2 = Integer.parseInt(txtENDDT1.getText().trim().substring(6,10));
					  intMTH2 = Integer.parseInt(txtENDDT1.getText().trim().substring(3,5));
					  intDAY2 = Integer.parseInt(txtENDDT1.getText().trim().substring(0,2));
					}
			
					 Calendar cal1 = Calendar.getInstance();
				     Calendar cal2 = Calendar.getInstance();
					 cal1.set(intYRS1,intMTH1,intDAY1);
				     cal2.set(intYRS2,intMTH2,intDAY2);
				     long milis1 = cal1.getTimeInMillis();
				     long milis2 = cal2.getTimeInMillis(); 
				     // Calculate difference in milliseconds
				     long diff = milis2 - milis1;
				       
				     // Calculate difference in days
				     long diffDays = diff / (24 * 60 * 60 * 1000);
				     System.out.println("days"+diffDays);
				  			       
				     txtACTDY1.setText(String.valueOf(Math.floor(diffDays)));
					 txtACTDY1.requestFocus();	 		
				}
				if(L_KE.getSource() == txtACTDY1)
				{	
					txtREMDS.requestFocus();							
				}
				if(L_KE.getSource() == txtREMDS)
				{	
					txtPRTNO1.requestFocus();
				}
				if(L_KE.getSource() == txtPRTNO1)
				{	
					chkCPAFL.requestFocus();						
				}
				if(L_KE.getSource() == chkCPAFL)
				{	
					chkOJTFL.requestFocus();
				}
				if(L_KE.getSource() == chkOJTFL)
				{	
					if(txtGRPCD.getText().equals("HARDWARE"))
					{
						cmbDVCNM.requestFocus();						
					}
					else
					{
						txtSFTCD.requestFocus();
					}
				}						
			}
		    if (L_KE.getKeyCode()== L_KE.VK_F1)
		    {
				if(L_KE.getSource() == txtREPDP)
				{ 				  
					String M_strSQLQRY = "";
					ResultSet M_rstRSSET;
					if(pnlJOPTPA == null)
					{	  
						pnlJOPTPA = new JPanel();
					    int L_ROWNO5=0;
					    pnlJOPTPA.setLayout(null);	
					    String[] LM_TBLHD5 = {"","Dept.","Dept.Name"};
					    int[] LM_COLSZ5 = {5,40,120};
				        tblJOPTPA = crtTBLPNL1(pnlJOPTPA,LM_TBLHD5,500,1,9,200,200,LM_COLSZ5,new int[]{0});			
				        M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
					    M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
					    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					    // System.out.println(">>>>DPTCD>>>>"+M_strSQLQRY);
					 	if(M_rstRSSET != null)   
					    {  
					        				 
					    	while(M_rstRSSET.next())
							{
					    		 tblJOPTPA.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),L_ROWNO5,TB5_CODCD);
					    		 tblJOPTPA.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""),L_ROWNO5,TB5_SHRDS);
					    		 L_ROWNO5++;
							}
					    	M_rstRSSET.close();
					    }
					 	setCursor(cl_dat.M_curWTSTS_pbst); 
    				}    
					pnlJOPTPA.setSize(300,300);
					pnlJOPTPA.setPreferredSize(new Dimension(300,300));
					int L_intOPTN=JOptionPane.showConfirmDialog( this, pnlJOPTPA,"Help For Dept.",JOptionPane.OK_CANCEL_OPTION);
					for( int P_intROWNO=0;P_intROWNO<tblJOPTPA.getRowCount();P_intROWNO++)
					{
						if(tblJOPTPA.getValueAt(P_intROWNO,TB5_CHKFL).toString().equals("true"))
						{	
							txtREPDP.setText(String.valueOf(tblJOPTPA.getValueAt(tblJOPTPA.getSelectedRow(),TB5_CODCD)).trim());
						}
					}  
										  
				}
			    if(L_KE.getSource() == txtSRLNO1)
				{
			      
			    	  String M_strSQLQRY = "";
					  ResultSet M_rstRSSET;
					  					  					 
					  if(txtGRPCD.getText().length()== 0)
					  {
						JOptionPane.showMessageDialog(null,"Please select System code","MESSAGE", JOptionPane.ERROR_MESSAGE); 
						return;
					  }
					
											
					  pnlJSRLNO = new JPanel();
					  int L_ROWNO6=0;
					 
					 
					  pnlJSRLNO.setLayout(null);
					  String[] LM_TBLHD6 = {"","SRL.NO.","Job Description"};				
					  int[] LM_COLSZ6 = {5,200,400};		
					  tblSRLNO = crtTBLPNL1(pnlJSRLNO,LM_TBLHD6,1000,1,9,300,200,LM_COLSZ6,new int[]{0});
				
					  M_strSQLQRY=" SELECT distinct JB_SRLNO,JB_JOBDS from SA_JBMST "
							+" where isnull(JB_STSFL,'')<>'X'";

					  if(txtSRLNO1.getText().length() >0)				
							M_strSQLQRY += " AND JB_SRLNO like '"+txtSRLNO1.getText().trim()+"%'";	
					  else
						  M_strSQLQRY += " AND JB_SRLNO like '_"+ txtGRPCD.getText().substring(0,2).toUpperCase()+"%'";	
					  M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					 // System.out.println(">>>>SRLNO>>>>"+M_strSQLQRY);
					
					 		if(M_rstRSSET != null)   
					        { 
					 			
					        	 
					    		while(M_rstRSSET.next())
								{
					    		
					    			 tblSRLNO.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_SRLNO"),""),L_ROWNO6,TB6_SRLNO);
					    			 tblSRLNO.setValueAt(nvlSTRVL(M_rstRSSET.getString("JB_JOBDS"),""),L_ROWNO6,TB6_JOBDS);
					    			 L_ROWNO6++;
					    		}
					    		M_rstRSSET.close();
					        }
					 		setCursor(cl_dat.M_curWTSTS_pbst); 
					 		 
					  					  		 		
					         pnlJSRLNO.setSize(400,300);
					         pnlJSRLNO.setPreferredSize(new Dimension(400,300));
							int L_intOPTN1 =JOptionPane.showConfirmDialog( this,pnlJSRLNO,"Help For Serial No.",JOptionPane.OK_CANCEL_OPTION);
						
						for( int P_intROWNO=0;P_intROWNO<tblSRLNO.getRowCount();P_intROWNO++)
					    {
					      if(tblSRLNO.getValueAt(P_intROWNO,TB6_CHKFL).toString().equals("true"))
					      {
							txtSRLNO1.setText(String.valueOf(tblSRLNO.getValueAt(tblSRLNO.getSelectedRow(),TB6_SRLNO)).trim());
					      }
					    }				
				} 	  
		  }
		}
		catch(Exception L_EX)
		{
			System.out.println("VK_ENTER"+L_EX);			
		}
  }
  public void itemStateChanged(ItemEvent ex)
  {
    try
    	{
 	   	
    	} 
    catch(Exception exe)
    {
     	System.out.println("inside itemStateChanged:"+exe);
    }
  }
  public void keyTyped(KeyEvent event)
  {
  	return;
  }
  public void keyReleased(KeyEvent event)
  {
  	return;
  }
   
  void clrCOMP()
  {
		try
		{
			txtJOBDS1.setText("");
			txtREPDT1.setText("");
			txtPLNDY1.setText("");
			txtSRLNO1.setText("");
			txtPRTNO1.setText("");
			txtPLNDT1.setText(""); 
			txtPLNDY1.setText("");
			txtSTRDT1.setText("");
			txtENDDT1.setText("");
			txtREPDP.setText("");
			txtREPBY.setText("");
			txtSTSDS.setText("");
			txtSTRTM.setText("");
			txtENDTM.setText("");
			txtREPTM.setText("");
			txtALCTO.setText("");
			txtACTDY1.setText("");
			txtREMDS.setText("");
			txtSFTCD.setText("");
			txtSFVER.setText("");
			txtSTSDS.setText("");
			txtPRGCD.setText("");
			chkOJTFL.setSelected(false);
			chkCPAFL.setSelected(false);
			cmbOJTCT.setEnabled(false);
			
		}	
		catch(Exception E)
		{
			System.out.println("clrCOMP :");		
		}	
  }
  private void setTEXTFIELD()   //DISPLAY WINDOW FOR ENTERING VEHICLE REJECTION
  {
	  try
	  {		
			if(jtpMANTAB.getSelectedIndex() == 0)
			{	
			
				for(int P_intROWNO=0;P_intROWNO<tblPENJOB.getRowCount();P_intROWNO++)
			    {
				    if(tblPENJOB.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				    {
					
						txtJOBDS1.setText(String.valueOf(tblPENJOB.getValueAt(tblPENJOB.getSelectedRow(),TB1_JOBDS)).trim());
						txtREPDT1.setText(String.valueOf(tblPENJOB.getValueAt(tblPENJOB.getSelectedRow(),TB1_REPDT)).trim().substring(0,10));
						txtPLNDY1.setText(String.valueOf(tblPENJOB.getValueAt(tblPENJOB.getSelectedRow(),TB1_PLNDY)).trim());
						txtSRLNO1.setText(String.valueOf(tblPENJOB.getValueAt(tblPENJOB.getSelectedRow(),TB1_SRLNO)).trim());
						txtPRTNO1.setText(String.valueOf(tblPENJOB.getValueAt(tblPENJOB.getSelectedRow(),TB1_PRTNO)).trim());
				    }
				}
			}
			
			if(jtpMANTAB.getSelectedIndex() == 3)
			{
				
				for(int P_intROWNO=0;P_intROWNO<tblONJOB.getRowCount();P_intROWNO++)
				{
					 if(tblONJOB.getValueAt(P_intROWNO,TB4_CHKFL).toString().equals("true"))
					 {	
					
						txtJOBDS1.setText(String.valueOf(tblONJOB.getValueAt(tblONJOB.getSelectedRow(),TB4_JOBDS)).trim());
						txtREPDT1.setText(String.valueOf(tblONJOB.getValueAt(tblONJOB.getSelectedRow(),TB4_REPDT)).trim().substring(0,8));
						txtPLNDY1.setText(String.valueOf(tblONJOB.getValueAt(tblONJOB.getSelectedRow(),TB4_PLNDY)).trim());
						txtSRLNO1.setText(String.valueOf(tblONJOB.getValueAt(tblONJOB.getSelectedRow(),TB4_SRLNO)).trim());
						txtPRTNO1.setText(String.valueOf(tblONJOB.getValueAt(tblONJOB.getSelectedRow(),TB4_PRTNO)).trim());
					 }
				} 
			}
			if(jtpMANTAB.getSelectedIndex() == 1)
			{
				for(int P_intROWNO=0;P_intROWNO<tblCOMJOB.getRowCount();P_intROWNO++)
				{
					 if(tblCOMJOB.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
					 {	
						txtJOBDS1.setText(String.valueOf(tblCOMJOB.getValueAt(tblCOMJOB.getSelectedRow(),TB2_JOBDS)).trim());
						txtENDDT1.setText(String.valueOf(tblCOMJOB.getValueAt(tblCOMJOB.getSelectedRow(),TB2_ENDDT)).trim().substring(0,8));
						txtPLNDY1.setText(String.valueOf(tblCOMJOB.getValueAt(tblCOMJOB.getSelectedRow(),TB2_PLNDY)).trim());
						txtSRLNO1.setText(String.valueOf(tblCOMJOB.getValueAt(tblCOMJOB.getSelectedRow(),TB2_SRLNO)).trim());
						txtPRTNO1.setText(String.valueOf(tblCOMJOB.getValueAt(tblCOMJOB.getSelectedRow(),TB2_PRTNO)).trim());
						chkCPAFL.setSelected(new Boolean(String.valueOf(tblCOMJOB.getValueAt(tblCOMJOB.getSelectedRow(),TB2_CPAFL)).trim()));
						chkOJTFL.setSelected(new Boolean(String.valueOf(tblCOMJOB.getValueAt(tblCOMJOB.getSelectedRow(),TB2_OJTFL)).trim()));
					 }
				} 
			}
														
		}
		catch (Exception L_EX)
		{
			System.out.println("setTEXTFIELD :"+L_EX);
		}
  }
  private void getDATA()
  {        
	 try  
	 {		
			if(rdbACCT.isSelected())
	        {	
				strFLAG="'A'";  
			}
			if(rdbNACCT.isSelected())
			{	
				strFLAG="'N'";
			}
			if(rdbALL.isSelected())
			{	
				strFLAG="'A','N'";
			}
			  			
		    String strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			String strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim())); 
		    
		    java.sql.Date L_datTEMP;
		    java.sql.Timestamp L_tmsTEMP;  
		    java.sql.Timestamp L_tmpTIME;
            int L_ROWNO1=0, L_ROWNO2=0,L_ROWNO3=0,L_ROWNO4=0;
            
		    
		    int L_intCOUNT = 0;
		    int L_intCOUNT1 = 0;
			int L_intCOMCOT= 0;
			
		    						
			String L_strTOTL = "Total";
			String L_strSTATS="",L_strTEMP="", L_strENDDT="",L_strREPDTM="";
		
			
			String L_strSYSCD="",L_strOSYSCD="";
			int L_intA1=0,L_intB1=0,L_intC1=0;
			int L_intTA1=0,L_intTB1=0,L_intTC1=0;
			int L_intA2=0,L_intB2=0,L_intC2=0;
			int L_intTA2=0,L_intTB2=0,L_intTC2=0;
			int L_intA3=0,L_intB3=0,L_intC3=0;
			int L_intTA3=0,L_intTB3=0,L_intTC3=0;
			int L_intP=0,L_intR=0,L_intC=0;
		    int L_intTP=0,L_intTR=0,L_intTC=0;
		   
			String L_strSQL = "";
			
			double L_dblTDAY = 0;
			//double L_intTOTPL= setNumberFormat(L_dblTDAY,2);
		
						 	
		     /*Pending Jobs**/
			  
					L_strSQLQRY1 ="Select JB_SYSCD,JB_JOBDS,JB_REPBY,JB_REPDT,JB_PLNDY,JB_JOBCT,JB_STRDT,JB_ENDDT,JB_SRLNO,JB_PRTNO,JB_ALCTO from SA_JBMST where isnull(JB_STSFL,'')<>'X'"
						+" AND DATE(JB_REPDT)<='"+strTODAT+"'AND JB_ENDDT IS NULL AND JB_STRDT IS NULL AND JB_JOBTP <> '13'"+"AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN ("+strFLAG+"))";							
					if(txtJOBASS.getText().trim().length() == 3)
						L_strSQLQRY1 +=" AND JB_ALCTO ='"+ txtJOBASS.getText().trim().toUpperCase()+"'";
					L_strSQLQRY1 +=" order by JB_SYSCD,JB_PRTNO ";
					//System.out.println("Pen " +L_strSQLQRY1);
					L_RSLSET1 = cl_dat.exeSQLQRY1(L_strSQLQRY1);
				 			     
			        if(L_RSLSET1 != null)   
			        {  				 
			    		while(L_RSLSET1.next())
						{
			    			
			    			L_tmpTIME = L_RSLSET1.getTimestamp("JB_REPDT");
				 			if (L_tmpTIME != null)
							{
							   L_strREPDTM = M_fmtLCDAT.format(L_tmpTIME);
							}	
								
			    			tblPENJOB.setValueAt(nvlSTRVL(L_RSLSET1.getString("JB_SYSCD"),""),L_ROWNO1,TB1_SYSCD);
			    			tblPENJOB.setValueAt(nvlSTRVL(L_RSLSET1.getString("JB_JOBDS"),""),L_ROWNO1,TB1_JOBDS);
			       			tblPENJOB.setValueAt(nvlSTRVL(L_strREPDTM ,""),L_ROWNO1,TB1_REPDT);
			    			tblPENJOB.setValueAt(nvlSTRVL(L_RSLSET1.getString("JB_PLNDY"),""),L_ROWNO1,TB1_PLNDY);	
			    		    tblPENJOB.setValueAt(nvlSTRVL(L_RSLSET1.getString("JB_JOBCT"),""),L_ROWNO1,TB1_JOBCT);
			    			tblPENJOB.setValueAt(nvlSTRVL(L_RSLSET1.getString("JB_SRLNO"),""),L_ROWNO1,TB1_SRLNO);
			    			tblPENJOB.setValueAt(nvlSTRVL(L_RSLSET1.getString("JB_PRTNO"),""),L_ROWNO1,TB1_PRTNO);
			    			tblPENJOB.setValueAt(nvlSTRVL(L_RSLSET1.getString("JB_ALCTO"),""),L_ROWNO1,TB1_ALCTO);
			    			
			    		    L_intCOUNT++;
			    			L_ROWNO1++;
			    		
			    			tblPENJOB.setValueAt("Total Pending Jobs : ",L_ROWNO1,TB1_SYSCD);
			    			tblPENJOB.setValueAt(L_intCOUNT,L_ROWNO1,TB1_JOBDS);
			    				
			    		}
			    		  L_RSLSET1.close();
			    		  lblPENJOB1.setText(String.valueOf(L_intCOUNT));
			        }
			        
			        
			        /*Ongoing Jobs**/
			        
			        L_strSQLQRY4 ="Select JB_SYSCD,JB_JOBDS,JB_REPBY,date(JB_REPDT)REPDT,JB_PLNDY,JB_JOBCT,JB_STRDT,JB_ENDDT,JB_SRLNO,JB_PRTNO,JB_ALCTO from SA_JBMST where isnull(JB_STSFL,'')<>'X'"
						+" AND DATE(JB_REPDT) <= '"+ strTODAT +"'AND JB_ENDDT IS NULL "+"AND JB_STRDT IS NOT NULL AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN ("+strFLAG+"))";							
			        if(txtJOBASS.getText().trim().length() == 3)
			        	L_strSQLQRY4 +=" AND JB_ALCTO ='"+ txtJOBASS.getText().trim().toUpperCase()+"'";
			        L_strSQLQRY4 +=" order by JB_SYSCD,JB_PRTNO ";
			        //System.out.println("Ongoing " +L_strSQLQRY4);
					L_RSLSET4 = cl_dat.exeSQLQRY1(L_strSQLQRY4);
								 			     
			        if(L_RSLSET4 != null)   
			        {  				 
			    		while(L_RSLSET4.next())
						{
			    			tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("JB_SYSCD"),""),L_ROWNO4,TB4_SYSCD);
			    			tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("JB_JOBDS"),""),L_ROWNO4,TB4_JOBDS);
			       			tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("REPDT"),""),L_ROWNO4,TB4_REPDT);
			    			tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("JB_PLNDY"),""),L_ROWNO4,TB4_PLNDY);	
			    	  		tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("JB_JOBCT"),""),L_ROWNO4,TB4_JOBCT);
			    			tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("JB_SRLNO"),""),L_ROWNO4,TB4_SRLNO);
			    			tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("JB_PRTNO"),""),L_ROWNO4,TB4_PRTNO);
			    			tblONJOB.setValueAt(nvlSTRVL(L_RSLSET4.getString("JB_ALCTO"),""),L_ROWNO4,TB4_ALCTO);
			    			
			    		    L_intCOUNT1++;
			    			L_ROWNO4++;
			    			tblONJOB.setValueAt("Total Ongoing Jobs : ",L_ROWNO4,TB4_SYSCD);
			    			tblONJOB.setValueAt(L_intCOUNT1,L_ROWNO4,TB4_JOBDS);
			    		}
			   			  L_RSLSET4.close();
			    		  lblONJOB1.setText(String.valueOf(L_intCOUNT1));
			        }
			        
		     /* Completed Jobs**/
			      
			    	 L_strSQLQRY2 = "Select JB_SYSCD,JB_JOBDS,JB_REPBY,date(JB_ENDDT) ENDDT,date(JB_STRDT) STRDT,JB_ENDDT,JB_STRDT,JB_PLNDY,JB_JOBCT,(DAYS(DATE(JB_ENDDT)) - DAYS(DATE(JB_STRDT)))L_DAYS,JB_SRLNO,JB_PRTNO,JB_REPDT,JB_ALCTO,JB_CPAFL,JB_OJTFL from SA_JBMST"
			 			+" where isnull(JB_STSFL,'')<>'X'"+" AND DATE(JB_REPDT)<='"+ strTODAT +"' AND DATE(JB_ENDDT) BETWEEN '"+ strFMDAT  +"' AND '"+ strTODAT +"'AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN ("+strFLAG+"))";
			    	 if(txtJOBASS.getText().trim().length() == 3)
							L_strSQLQRY2 +=" AND JB_ALCTO ='"+ txtJOBASS.getText().trim().toUpperCase()+"'";
			    	 L_strSQLQRY2 +="  order by JB_SYSCD,ENDDT ";
			    	//System.out.println("Com " +L_strSQLQRY2);
			    	 L_RSLSET2 = cl_dat.exeSQLQRY1(L_strSQLQRY2);
			       
			    	 
			        if(L_RSLSET2 != null)
					{
			      	    while(L_RSLSET2.next())
						{
			      	    	
			      	    	int intISDOT = L_RSLSET2.getString("JB_PLNDY").indexOf(".");
							int intDAY1 = Integer.parseInt(L_RSLSET2.getString("JB_PLNDY").toString().substring(0,intISDOT));
							
							int intDAY2 = 0;
							int intCOT = 0;
						
							if(!(L_RSLSET2.getString("L_DAYS")==null))
							{	
							  intDAY2 = Integer.parseInt(L_RSLSET2.getString("L_DAYS"))+1;
							}
					
							int intVERIA = (intDAY2 - intDAY1);
						    //System.out.println("Number of days: " +intVERIA + "<br>");
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_SYSCD"),""),L_ROWNO2,TB2_SYSCD);
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_JOBDS"),""),L_ROWNO2,TB2_JOBDS);
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_STRDT"),""),L_ROWNO2,TB2_STRDT);
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_ENDDT"),""),L_ROWNO2,TB2_ENDDT);
			    			tblCOMJOB.setValueAt(nvlSTRVL(String.valueOf(intDAY2),""),L_ROWNO2,TB2_ACTDY);
			   			    tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_PLNDY"),""),L_ROWNO2,TB2_PLNDY);
			   			    tblCOMJOB.setValueAt(nvlSTRVL(String.valueOf(intVERIA),""),L_ROWNO2,TB2_VERIT);
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_JOBCT"),""),L_ROWNO2,TB2_JOBCT);
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_SRLNO"),""),L_ROWNO2,TB2_SRLNO);
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_PRTNO"),""),L_ROWNO2,TB2_PRTNO);
			    			tblCOMJOB.setValueAt(nvlSTRVL(L_RSLSET2.getString("JB_ALCTO"),""),L_ROWNO2,TB2_ALCTO);
			    			if(nvlSTRVL(L_RSLSET2.getString("JB_CPAFL"),"").equals("Y"))
			    			tblCOMJOB.setValueAt(new Boolean(true),L_ROWNO2,TB2_CPAFL);
						    if(nvlSTRVL(L_RSLSET2.getString("JB_OJTFL"),"").equals("Y"))
						    tblCOMJOB.setValueAt(new Boolean(true),L_ROWNO2,TB2_OJTFL);
						    
			    			L_intCOMCOT++;
			    			L_ROWNO2++;
			    			tblCOMJOB.setValueAt("Total Jobs Completed : ",L_ROWNO2,TB2_SYSCD);
			    			tblCOMJOB.setValueAt(L_intCOMCOT,L_ROWNO2,TB2_JOBDS);
						 }
													
						L_RSLSET2.close();
						lblCOMJOB1.setText(String.valueOf(L_intCOMCOT));
						
					}
			       			 			 			
				/*Summary Report **/
			        
			        
			        
			        L_strSQL = " AND JB_JOBTP <> '13'";
				   			        	
					L_strSQLQRY3 = "SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'P' L_STATS FROM SA_JBMST WHERE DATE(JB_REPDT) <'"+ strFMDAT +"' AND ((JB_ENDDT is null) or (date(JB_ENDDT) > '"+ strFMDAT +"')) AND isnull(JB_STSFL,'')<>'X'" +L_strSQL +" AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN ("+strFLAG+"))"
				 		+" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'P1' L_STATS FROM SA_JBMST WHERE DATE(JB_REPDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND Date(JB_ENDDT) is null AND isnull(JB_STSFL,'')<>'X'"+ L_strSQL+" AND JB_SYSCD IN(SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN ("+strFLAG+"))"// Pending, reported in the date Range						 
				 		+" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'R' L_STATS FROM SA_JBMST WHERE DATE(JB_REPDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND isnull(JB_STSFL,'')<>'X'" +L_strSQL+" AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN("+strFLAG+"))"//Reported in the Date  Date Range			
				 		+" UNION SELECT JB_SRLNO,JB_SYSCD,JB_JOBCT,DATE(JB_REPDT) L_REPDT,DATE(JB_STRDT) L_STRDT,DATE(JB_ENDDT) L_ENDDT,'C' L_STATS FROM SA_JBMST WHERE DATE(JB_ENDDT) BETWEEN '"+ strFMDAT +"' AND '"+ strTODAT +"' AND isnull(JB_STSFL,'')<>'X'"+L_strSQL +" AND JB_SYSCD IN (SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP='SYS' and CMT_CGSTP='COXXGRP'AND CMT_CHP02 IN("+strFLAG+"))";		
					 						
					L_strSQLQRY3 +=" ORDER BY JB_SYSCD";
					 
					//System.out.println("Sum "+L_strSQLQRY3);
			    	L_RSLSET3 = cl_dat.exeSQLQRY1(L_strSQLQRY3);	
			        		  			        
			    	 				    
			    	while(L_RSLSET3.next())
			    	{
						L_strSYSCD = nvlSTRVL(L_RSLSET3.getString("JB_SYSCD"),"");
			    			if(!L_strSYSCD.equals(L_strOSYSCD))
							{
								if(!L_strOSYSCD.equals(""))
								{
									
									L_intTA1 += L_intA1;
									L_intTB1 += L_intB1;
									L_intTC1 += L_intC1;
									L_intTA2 += L_intA2;
									L_intTB2 += L_intB2;
									L_intTC2 += L_intC2;
									L_intTA3 += L_intA3;
									L_intTB3 += L_intB3;
									L_intTC3 += L_intC3;
									
									tblSUMRY.setValueAt(L_strOSYSCD,L_ROWNO3,TB3_SYSCD);
									tblSUMRY.setValueAt(String.valueOf(L_intA1).toString(),L_ROWNO3,TB3_PA);
									tblSUMRY.setValueAt(String.valueOf(L_intB1).toString(),L_ROWNO3,TB3_PB);
									tblSUMRY.setValueAt(String.valueOf(L_intC1).toString(),L_ROWNO3,TB3_PC);
							
									
									L_intP = L_intA1+L_intB1+L_intC1;
									L_intTP += L_intP;
									tblSUMRY.setValueAt(String.valueOf(L_intP).toString(),L_ROWNO3,TB3_TOTP);
									tblSUMRY.setValueAt(String.valueOf(L_intA2).toString(),L_ROWNO3,TB3_RA);
									tblSUMRY.setValueAt(String.valueOf(L_intB2).toString(),L_ROWNO3,TB3_RB);
									tblSUMRY.setValueAt(String.valueOf(L_intC2).toString(),L_ROWNO3,TB3_RC);
								
									
									L_intR = L_intA2+L_intB2+L_intC2;
									L_intTR += L_intR;
									tblSUMRY.setValueAt(String.valueOf(L_intR).toString(),L_ROWNO3,TB3_TOTR);
									tblSUMRY.setValueAt(String.valueOf(L_intP+L_intR).toString(),L_ROWNO3,TB3_TOPR);
								
									
									tblSUMRY.setValueAt(String.valueOf(L_intA3).toString(),L_ROWNO3,TB3_CA);
									tblSUMRY.setValueAt(String.valueOf(L_intB3).toString(),L_ROWNO3,TB3_CB);
									tblSUMRY.setValueAt(String.valueOf(L_intC3).toString(),L_ROWNO3,TB3_CC);
									L_intC = L_intA3+L_intB3+L_intC3;
									L_intTC += L_intC;
									tblSUMRY.setValueAt(String.valueOf(L_intC).toString(),L_ROWNO3,TB3_TOTC);
						
									
									tblSUMRY.setValueAt(String.valueOf(L_intA1+L_intA2-L_intA3).toString(),L_ROWNO3,TB3_PRCA);
									tblSUMRY.setValueAt(String.valueOf(L_intB1+L_intB2-L_intB3).toString(),L_ROWNO3,TB3_PRCB);
									tblSUMRY.setValueAt(String.valueOf(L_intC1+L_intC2-L_intC3).toString(),L_ROWNO3,TB3_PRCC);
									tblSUMRY.setValueAt(String.valueOf(L_intP+L_intR-L_intC).toString(),L_ROWNO3,TB3_TOPRC);
									L_ROWNO3++; 
									  								
																										
								}
								L_intA1=0;L_intB1=0;L_intC1=0;
								L_intA2=0;L_intB2=0;L_intC2=0;
								L_intA3=0;L_intB3=0;L_intC3=0;
								L_strOSYSCD = L_strSYSCD;
						   }
						   L_strSTATS = L_RSLSET3.getString("L_STATS");
						   if((L_strSTATS.equals("P"))||(L_strSTATS.equals("P1")))// pending before From Date.
						   {
								L_strTEMP = L_RSLSET3.getString("JB_JOBCT");
								if(L_strTEMP.equals("A"))
									L_intA1++;
								else if(L_strTEMP.equals("B"))
									L_intB1++;
								else if(L_strTEMP.equals("C"))
									L_intC1++;
							}
							else if(L_strSTATS.equals("R"))// Jobs Reported in the Given Date Range
							{
								L_strTEMP = L_RSLSET3.getString("JB_JOBCT");
								if(L_strTEMP.equals("A"))
									L_intA2++;
								else if(L_strTEMP.equals("B"))
									L_intB2++;
								else if(L_strTEMP.equals("C"))
									L_intC2++;	
							}
							else if(L_strSTATS.equals("C"))//Completed in the given date range.
							{
								L_strTEMP = L_RSLSET3.getString("JB_JOBCT");
								if(L_strTEMP.equals("A"))
									L_intA3++;
								else if(L_strTEMP.equals("B"))
									L_intB3++;
								else if(L_strTEMP.equals("C"))
									L_intC3++;
							}
				    }
			    	 
			        if(L_RSLSET3 != null)
				    {	
			    	  L_RSLSET3.close(); 			
				    }
			             
			        tblSUMRY.setValueAt(L_strSYSCD,L_ROWNO3,TB3_SYSCD);
					tblSUMRY.setValueAt(String.valueOf(L_intA1).toString(),L_ROWNO3,TB3_PA);
					tblSUMRY.setValueAt(String.valueOf(L_intB1).toString(),L_ROWNO3,TB3_PB);
					tblSUMRY.setValueAt(String.valueOf(L_intC1).toString(),L_ROWNO3,TB3_PC);
					
					L_intP = L_intA1+L_intB1+L_intC1;
					L_intTP += L_intP;
					tblSUMRY.setValueAt(String.valueOf(L_intP).toString(),L_ROWNO3,TB3_TOTP);
					tblSUMRY.setValueAt(String.valueOf(L_intA2).toString(),L_ROWNO3,TB3_RA);
					tblSUMRY.setValueAt(String.valueOf(L_intB2).toString(),L_ROWNO3,TB3_RB);
					tblSUMRY.setValueAt(String.valueOf(L_intC2).toString(),L_ROWNO3,TB3_RC);
					
					
					L_intR = L_intA2+L_intB2+L_intC2;
					L_intTR += L_intR;
					tblSUMRY.setValueAt(String.valueOf(L_intR).toString(),L_ROWNO3,TB3_TOTR);
					tblSUMRY.setValueAt(String.valueOf(L_intP+L_intR).toString(),L_ROWNO3,TB3_TOPR);
						
					tblSUMRY.setValueAt(String.valueOf(L_intA3).toString(),L_ROWNO3,TB3_CA);
					tblSUMRY.setValueAt(String.valueOf(L_intB3).toString(),L_ROWNO3,TB3_CB);
					tblSUMRY.setValueAt(String.valueOf(L_intC3).toString(),L_ROWNO3,TB3_CC);
					L_intC = L_intA3+L_intB3+L_intC3;
					L_intTC += L_intC;
					tblSUMRY.setValueAt(nvlSTRVL(String.valueOf(L_intC).toString(),""),L_ROWNO3,TB3_TOTC);
					
					tblSUMRY.setValueAt(String.valueOf(L_intA1+L_intA2-L_intA3).toString(),L_ROWNO3,TB3_PRCA);
					tblSUMRY.setValueAt(String.valueOf(L_intB1+L_intB2-L_intB3).toString(),L_ROWNO3,TB3_PRCB);
					tblSUMRY.setValueAt(String.valueOf(L_intC1+L_intC2-L_intC3).toString(),L_ROWNO3,TB3_PRCC);
					tblSUMRY.setValueAt(String.valueOf(L_intP+L_intR-L_intC).toString(),L_ROWNO3,TB3_TOPRC);
					L_ROWNO3++; 
					tblSUMRY.setValueAt(L_strTOTL,L_ROWNO3,TB3_SYSCD);
					
				
					int L_intSUMA=0,L_intSUMB=0,L_intSUMC=0,L_intSUMT=0,L_intTEMP =0;
					int L_intTOTALP=0;
					L_intTEMP = L_intA1+L_intTA1+L_intB1+L_intTB1+L_intC1+L_intTC1;
					L_intSUMA = L_intTEMP;
					L_intTOTALP = L_intTEMP;
					tblSUMRY.setValueAt(String.valueOf(L_intA1+L_intTA1).toString(),L_ROWNO3,TB3_PA);
					tblSUMRY.setValueAt(String.valueOf(L_intB1+L_intTB1).toString(),L_ROWNO3,TB3_PB);
					tblSUMRY.setValueAt(String.valueOf(L_intC1+L_intTC1).toString(),L_ROWNO3,TB3_PC);				
					tblSUMRY.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO3,TB3_TOTP);
										
					L_intTEMP = L_intA2+L_intTA2+L_intB2+L_intTB2+L_intC2+L_intTC2;
					L_intSUMA += L_intTEMP;			
					L_intTOTALP += L_intTEMP;
					tblSUMRY.setValueAt(String.valueOf(L_intA2+L_intTA2).toString(),L_ROWNO3,TB3_RA);
					tblSUMRY.setValueAt(String.valueOf(L_intB2+L_intTB2).toString(),L_ROWNO3,TB3_RB);
					tblSUMRY.setValueAt(String.valueOf(L_intC2+L_intTC2).toString(),L_ROWNO3,TB3_RC);		
					tblSUMRY.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO3,TB3_TOTR);

					L_intTEMP = L_intA3+L_intTA3+L_intB3+L_intTB3+L_intC3+L_intTC3;
					L_intSUMA -= L_intTEMP;
					tblSUMRY.setValueAt(String.valueOf(L_intTOTALP).toString(),L_ROWNO3,TB3_TOPR);
					tblSUMRY.setValueAt(String.valueOf(L_intA3+L_intTA3).toString(),L_ROWNO3,TB3_CA);
					tblSUMRY.setValueAt(String.valueOf(L_intB3+L_intTB3).toString(),L_ROWNO3,TB3_CB);
					tblSUMRY.setValueAt(String.valueOf(L_intC3+L_intTC3).toString(),L_ROWNO3,TB3_CC);			
					tblSUMRY.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO3,TB3_TOTC);
										
					tblSUMRY.setValueAt(String.valueOf(L_intA1+L_intTA1+L_intA2+L_intTA2-(L_intA3+L_intTA3)).toString(),L_ROWNO3,TB3_PRCA);
					tblSUMRY.setValueAt(String.valueOf(L_intB1+L_intTB1+L_intB2+L_intTB2-(L_intB3+L_intTB3)).toString(),L_ROWNO3,TB3_PRCB);
					tblSUMRY.setValueAt(String.valueOf(L_intC1+L_intTC1+L_intC2+L_intTC2-(L_intC3+L_intTC3)).toString(),L_ROWNO3,TB3_PRCC);
					tblSUMRY.setValueAt(String.valueOf(L_intSUMA).toString(),L_ROWNO3,TB3_TOPRC);
					L_ROWNO3++;   
				    setCursor(cl_dat.M_curDFSTS_pbst);	
		  					   											
		 }
	     catch(Exception L_EX)
		 {
			System.out.println("getDATA "+L_EX);
		 }
  }
  /*
 *Cretaes JTable on the passed Panel
 */
 public JTable crtTBLPNL1(JPanel LP_TBLPNL,String[] LP_COLHD,int LP_ROWCNT,int LP_XPOS,int LP_YPOS,int LP_WID,int LP_HGT,int[] LP_ARRGSZ,int LP_CHKCOL[]){ 
	 try{
		 cl_tab2 L_TBLOBJ1; 
		 JPanel pnlTAB1 = new JPanel();
		
		// Object[][] L_TBLDT1;
		 //L_TBLDT1 = crtTBLDAT(LP_ROWCNT,LP_COLHD.length); // Creating the Object Data
		 
		 int LP_COLCNT=LP_COLHD.length;
		 Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];
		 L_TBLDT = crtTBLDAT(LP_ROWCNT,LP_COLHD.length);
		 for(int j = 0;j < LP_ROWCNT;j++)
		 {
				for(int i=0;i<LP_COLCNT;i++)
				{
					L_TBLDT[j][i]="";
					for(int k=0;k<LP_CHKCOL.length;k++)
					{
						if(i==LP_CHKCOL[k])
							L_TBLDT[j][i]=new Boolean(false);
					}
					
				}
		 }
		 L_TBLOBJ1 = new cl_tab2(L_TBLDT,LP_COLHD);
		 JTable L_TBL1 = new JTable(L_TBLOBJ1); 
		 L_TBL1.setBackground(new Color(213,213,255));
		 int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
		 int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		 JScrollPane jspTBL1 = new JScrollPane(L_TBL1,v1,h1);
		 jspTBL1.setPreferredSize(new Dimension(LP_WID-25,LP_HGT-25));
		 jspTBL1.setLocation(0,100);
		 pnlTAB1.removeAll();
		 setCOLWDT(L_TBL1,LP_COLHD,LP_ARRGSZ);
		 pnlTAB1.add(jspTBL1);
		 pnlTAB1.setSize(LP_WID,LP_HGT);
		 pnlTAB1.setLocation(LP_XPOS,LP_YPOS);
		 LP_TBLPNL.add(pnlTAB1);
		 LP_TBLPNL.updateUI();
		return L_TBL1;
	  }catch(Exception L_EX){
		 System.out.println("crtTBLPNL1 "+L_EX);
	 }
	return null;
 }
  
  public void setCOLWDT(JTable LP_TBLNM,String[] LP_TBLHDG,int[] LP_WID){
	for(int i=0;i<LP_TBLHDG.length;i++){
		TableColumn L_TBLCOL = LP_TBLNM.getColumn(LP_TBLHDG[i]);
		if(LP_WID[i] !=0)
			L_TBLCOL.setPreferredWidth(LP_WID[i]);
		}
	}
  
  
  public Object[][] crtTBLDAT(int LP_ROWCNT,int LP_COLCNT)
  {
		int i = 0;
	    Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];;
		for(int j = 0;j < LP_ROWCNT;j++){
			i = 0;
			for( int k = i;k < LP_COLCNT;k++){
				L_TBLDT[j][k] = "";
			}
		}
			return L_TBLDT;
  }
  
  public  void setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX)
  {
		try
        {
          cl_dat.M_conSPDBA_pbst = this.setCONDTB(LP_SYSLC,LP_XXLBX, LP_XXUSX, LP_XXPWX);
          if(cl_dat.M_conSPDBA_pbst != null)
          {
              cl_dat.M_conSPDBA_pbst.rollback();
		  	  cl_dat.M_stmSPDBA_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
		  }
	    }
        catch(Exception L_EX)
        {
			System.out.println("Error in setCONTACT"+L_EX);
		}
   }
	/** Establishes connection to DB.
	 * Called internally from
	 * {@link  fr_log#setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX) }
	 */
   private  Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
   {
		Connection LM_CONDTB=null;
		try
        {       
			String L_STRURL = "", L_STRDRV = "";
						if(LP_SYSLC.equals("01"))
                        {
                                L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                                L_STRURL = "jdbc:as400://SPLWS01/";
                                Class.forName(L_STRDRV);
                        }
                        else if(LP_SYSLC.equals("02"))
                        {
                                int port = 50000;
                                LP_DTBUS = "spldata";
                                LP_DTBPW = "spldata";

                                L_STRURL = "jdbc:db2://" + "splhos01" + ":" + 50000 + "/" ;
                                Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
                        }
						else if(LP_SYSLC.equals("03"))
                        {
                                L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                                L_STRURL = "jdbc:as400://MUMAS2/";
                                Class.forName(L_STRDRV);
                        }
                        L_STRURL = L_STRURL + LP_DTBLB;
                        LM_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);

                        if(LM_CONDTB == null)
				return null;
                        LM_CONDTB.setAutoCommit(false);

                        SQLWarning L_STRWRN = LM_CONDTB.getWarnings();
			if ( L_STRWRN != null )
			   System.out.println("Warning in setCONDTB : "+L_STRWRN);
                        return LM_CONDTB;
		}
        catch(java.lang.Exception L_EX)
        {
			System.out.println("setCONDTB" + L_EX.toString());
			return null;
		}
    }
}
class  co_vwjob_RightTableCellRenderer extends DefaultTableCellRenderer 
{    
		  protected  co_vwjob_RightTableCellRenderer() 
		  {   
			  setHorizontalAlignment(JLabel.RIGHT); 
		  }         
}   
