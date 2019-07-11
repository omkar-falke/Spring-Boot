/**/
 
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.awt.event.*;
import java.awt.Color;
//import cl_dat;
//import cl_cust;
import java.text.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;


public class hr_vwhdc extends JFrame implements ActionListener
{
     
	String strSQLQRY;
	ResultSet rstRSSET;
	String strSQLQRY1;
	ResultSet rstRSSET1;
	
	JLabel lblMSG,lblTRPCT,lblVSTTP,lblCURDT,lblEMPCT,lblVSTCT,lblTOTAL,lblCNTCT,lblTOTCT,lblOUTCT,lblDETAL; 
	JLabel lblEMPCT1,lblCNTCT1,lblVSTCT1,lblTOTCT1,lblOUTCT1,lblTRPCT1; 	
	JLabel lblSUMCT,lblDITCT,lblSUMCT1,lblDITCT1,lblSUMCT2,lblDITCT2,lblSUMCT3,lblDITCT3;
	
	JTable tblEMPDL,tblEMPSM;    /** table for a Employee details */ 
	JTable tblVSTDL,tblVSTSM;    /** table for Visitor Details */
	JTable tblCNTDL,tblCNTSM;	/** table Contractors Details */
	JTable tblTRPDL,tblTRPSM;
	JTable tblOEMPDL;    /** table for a Employee details */ 
	JTable tblOVSTDL;    /** table for Visitor Details */
	JTable tblOCNTDL;
	JTable tblOUTDL;
	JTable tblTOTDL;
		 
	JTabbedPane jtpMANTAB; 
	JPanel pnlVSTDL;   /** Panel foe Visitors Table */      
	JPanel pnlCNTDL;   /** Panel foe Contractors Table */   
	JPanel pnlEMPDL;  /** Panel foe Employee Table */
	JPanel pnlTRPDL;
	JPanel pnlOUTDL;
	JPanel pnlDSPREC;
	JPanel pnlHEDCT;
	 //JCheckBox chkINTM;
	JWindow wndMAINWD,wndDSPTBL;
	JDialog wndMAINDL;
	JButton btnREFSH, btnEXIT, btnDTLON, btnDTLOF;
	
	JRadioButton rdbDTLON,rdbDTLOF;
	  ButtonGroup grpDTLOPT;
	  
	  hr_vwhdc_RightTableCellRenderer rightRENDERER;
	  TableColumnModel tcm;
	
	 Hashtable<String,String> hstDOCTP;
	
	Cursor curWTSTS = new Cursor(Cursor.WAIT_CURSOR);
	Cursor curDFSTS = new Cursor(Cursor.DEFAULT_CURSOR);
	static String M_strCMPCD_pbst = "";
	static String M_strINCHK_pbst = "";
    static String M_strCMPNM_prst = "";
 
	 final int TB1_EDEPT =0; 
	 final int TB1_EMPNO =1;
	 final int TB1_EMPNM =2;
	 final int TB1_PNCTM =3;
	 final int TB1_PNOTM =4;
	                                /** Final value for Column of Visitors table */
	 final int TB2_VSTNM =0;
	 final int TB2_VSTCT =1;
	 final int TB2_PERVS =2;
	 final int TB2_VSITM =3;
	 final int TB2_VSOTM =4;
	                                /** Final value for Column of Contractors table */
	 final int TB3_CNTNM =0;
	 final int TB3_CNTCT =1;
	 final int TB3_CNITM =2;
	 final int TB3_CNOTM =3;
	
	 final int TB4_STRTP =0;
	 final int TB4_TPRDS =1;
	 final int TB4_COUNT =2;
	 final int TB4_GINDT =3;
	 final int TB4_GOTDT =4;
	 
	 final int TB6_DESCD =0;
	 final int TB6_HEDCT =1;

	 
	 int L_intTOTCT=0,L_intHEDCT=0,L_intVSTCT=0,L_intCNTCT=0,L_intTRPCT=0;
	 int L_intEMP=0;
	 int sl_time = 200;
	 String LM_ACTlbl,LM_DATFMT;
	 int L_ROWNO=0,L_ROWNO1=0, L_ROWNO2=0,L_ROWNO3=0,L_ROWNO4=0;
	SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");

  hr_vwhdc()
  {
		opnDBCON();
		rightRENDERER=new hr_vwhdc_RightTableCellRenderer();
	    getDSPMSG();
	  	getSUMDL();
		getHEDDL();
		getOUTDL();
		getLVEDL();
		
		//if(M_strINCHK_pbst.equalsIgnoreCase("IN"))
			//getINDL();
  }

  private void opnDBCON(){
	  try{
		//cl_dat.ocl_dat.M_conSPDBA_pbst = cl_dat.ocl_dat.setCONDTB("01","spldata","FIMS","FIMS");
		setCONACT("01","spldata","FIMS","FIMS");		
		if(cl_dat.M_conSPDBA_pbst != null){
			cl_dat.M_stmSPDBA_pbst = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			cl_dat.M_stmSPDBQ_pbst  = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			cl_dat.M_stmSPDBQ_pbst1 = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			}
		
	  }catch(Exception L_EX){
		  System.out.println("opnDBCON: "+L_EX);
	  }
  }
  
  private void clsDBCON(){
	  try{
		if(cl_dat.M_conSPDBA_pbst != null){
			cl_dat.M_conSPDBA_pbst.commit();
			cl_dat.M_stmSPDBA_pbst.close();
			cl_dat.M_stmSPDBQ_pbst.close();
			cl_dat.M_stmSPDBQ_pbst1.close();
			cl_dat.M_conSPDBA_pbst.close();
		}
	  }catch(Exception L_EX){
		  System.out.println("clsDBCON: "+L_EX);
	  }
  }
  
  public static void main(String args[])
	{
		if (args.length > 0)
      {
          M_strCMPCD_pbst = args[0];
		}
	    M_strINCHK_pbst = "";
		if (args.length > 1)
	       M_strINCHK_pbst = args[1];
	   hr_vwhdc ohr_vwhdc = new hr_vwhdc();
      //ofg_vwdsp.show();
     ohr_vwhdc.setVisible(true);
	}
  
  public Statement chkCONSTM(Connection LP_CONVAL,String LP_QRYTP){
	try{
		if (LP_CONVAL != null){
			if(LP_QRYTP.equals("Q"))	
				return LP_CONVAL.createStatement();
			else if(LP_QRYTP.equals(""))
				return LP_CONVAL.createStatement();
			}
		}catch(Exception L_EX){}
			return null;			
		}
  
	private String setDATE(java.util.Date oDT)
    {
		String strCRDTM="";
		SimpleDateFormat L_fmtLCDTM=new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		try
		{
			String L_strSQLQRY = "SELECT CMT_CHP01 from CO_CDTRN WHERE CMT_CGMTP ='S"+M_strCMPCD_pbst+"' AND CMT_CGSTP ='HRXXDTR' AND CMT_CODCD ='01'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET!=null && L_rstRSSET.next())
				//strCRDTM = M_fmtLCDTM.format(L_fmtLCDTM.parse(L_rstRSSET.getString("cmt_chp01")));
				strCRDTM = L_rstRSSET.getString("cmt_chp01");
			return strCRDTM;
		}
		catch(Exception E)
		{
			System.out.println("setDate() : "+E);
		}
		return M_fmtLCDTM.format(oDT);
	}
  
	private String setDATFMT(String LP_DATSTR){
		if(LP_DATSTR != null){
			if(LP_DATSTR.trim().length() == 8){
				String L_CHRDT = LP_DATSTR.substring(0,2)+"/"+LP_DATSTR.substring(3,5)+"/"+"20"+LP_DATSTR.substring(6,LP_DATSTR.length());
				return L_CHRDT;
			}else
				return "";
		}else
			return "";
	}
	

 public void actionPerformed(ActionEvent L_AE){
        LM_ACTlbl = L_AE.getActionCommand();
		if(L_AE.getSource().equals("+"))
          sl_time -= 10;
        if(L_AE.getSource().equals("-"))
          sl_time += 10;
     
	}
/**method to display component on screen*/
  private void getDSPMSG(){
	  try{
				
			pnlDSPREC = new JPanel();
			pnlDSPREC.removeAll();
			pnlDSPREC.setLayout(null);
			
			pnlVSTDL = new JPanel();
			pnlCNTDL = new JPanel();
			pnlEMPDL = new JPanel();
			pnlTRPDL = new JPanel();
			//pnlOUTDL = new JPanel();
			pnlHEDCT = new JPanel();
			jtpMANTAB = new JTabbedPane();
			lblSUMCT = new JLabel("Head Count Summery");
			lblDITCT = new JLabel("Head Count Detail");
			lblSUMCT1 = new JLabel("Head Count Summery");
			lblDITCT1 = new JLabel("Head Count Detail");
			lblSUMCT2 = new JLabel("Head Count Summery");
			lblDITCT2 = new JLabel("Head Count Detail");
			lblSUMCT3 = new JLabel("Head Count Summery");
			lblDITCT3 = new JLabel("Head Count Detail");
			
			
			
			/*String[] L_strCOLHD0 = {"Time","Employees","Visitors","Contractors","Count from Vehicle","Total"};
			int[] L_intCOLSZ0 = {40,90,90,90,170,90};	    				
			tblTOTDL = crtTBLPNL1(pnlDSPREC,L_strCOLHD0,3,4,0,500,108,L_intCOLSZ0);	*/	
			
			pnlEMPDL.setLayout(null);
			String[] L_strCOLHD = {"Department","Employee No.","Employee Name","In Time","Out Time"};
			int[] L_intCOLSZ = {100,100,200,125,125};	    				
			tblEMPDL = crtTBLPNL1(pnlEMPDL,L_strCOLHD,400,1,230,780,400,L_intCOLSZ);	
			//tblTELVE = crtTBLPNL1(pnlHSTRY,L_strTBLHD3,100,1,1,8,15,L_intCOLSZ3,new int[]{0});
		    tblEMPDL.setSelectionForeground(Color.blue);
			jtpMANTAB.add(pnlEMPDL);
		
			String[] L_strCOLHD5 = {"Description","Head Count"};
			int[] L_intCOLSZ5 = {100,40};	    				
			tblEMPSM = crtTBLPNL1(pnlEMPDL,L_strCOLHD5,200,1,15,450,200,L_intCOLSZ5);
			tcm=tblEMPSM.getColumnModel();
			tcm.getColumn(TB6_HEDCT).setCellRenderer(rightRENDERER);
			
			tblVSTSM = crtTBLPNL1(pnlVSTDL,L_strCOLHD5,8,1,15,450,200,L_intCOLSZ5);
			tcm=tblVSTSM.getColumnModel();
			tcm.getColumn(TB6_HEDCT).setCellRenderer(rightRENDERER);
			
			tblCNTSM = crtTBLPNL1(pnlCNTDL,L_strCOLHD5,100,1,15,450,200,L_intCOLSZ5);
			tcm=tblCNTSM.getColumnModel();
			tcm.getColumn(TB6_HEDCT).setCellRenderer(rightRENDERER);
			
			tblTRPSM = crtTBLPNL1(pnlTRPDL,L_strCOLHD5,100,1,15,450,200,L_intCOLSZ5);
			tcm=tblTRPSM.getColumnModel();
			tcm.getColumn(TB6_HEDCT).setCellRenderer(rightRENDERER);
			
			tblEMPSM.setSelectionForeground(Color.blue);
			jtpMANTAB.add(pnlEMPDL);
			
			pnlVSTDL.setLayout(null);
			String[] L_strCOLHD1 = {"Visitor Name","No of Persons","To See a person","In Time","Out Time"};
			int[] L_intCOLSZ1 = {200,80,130,125,125};	
			tblVSTDL = crtTBLPNL1(pnlVSTDL,L_strCOLHD1,200,1,230,780,400,L_intCOLSZ1);
			 tcm=tblVSTDL.getColumnModel();
			tcm.getColumn(TB2_VSTCT).setCellRenderer(rightRENDERER);
			
			tblVSTDL.setSelectionForeground(Color.blue);
			jtpMANTAB.add(pnlVSTDL);
			
			pnlCNTDL.setLayout(null);
			String[] L_strCOLHD2 = {"Contractor Name","No of Persons","In Tim","Out Time"};
	  		int[] L_intCOLSZ2 = {225,100,200,200};	    				
			tblCNTDL = crtTBLPNL1(pnlCNTDL,L_strCOLHD2,200,1,230,780,400,L_intCOLSZ2);
			 tcm=tblCNTDL.getColumnModel();
			tcm.getColumn(TB3_CNTCT).setCellRenderer(rightRENDERER);
			tblCNTDL.setSelectionForeground(Color.blue);
			jtpMANTAB.add(pnlCNTDL);
		
			pnlTRPDL.setLayout(null);
			//lblTRPCT.setForeground(Color.blue);
			String[] L_strCOLHD3 = {"Category","Transporter","No of Persons","In Time","Out Time"};
	  		int[] L_intCOLSZ3 = {100,125,100,200,200};	    				
			tblTRPDL = crtTBLPNL1(pnlTRPDL,L_strCOLHD3,200,1,230,780,400,L_intCOLSZ3);
			 tcm=tblTRPDL.getColumnModel();
			tcm.getColumn(TB4_COUNT).setCellRenderer(rightRENDERER);
			tblTRPDL.setSelectionForeground(Color.blue);
			jtpMANTAB.add(pnlTRPDL);
			
			pnlHEDCT.setBorder(new EtchedBorder(Color.gray,Color.lightGray)); 
			
			Font f = new Font("TimesRoman",Font.BOLD,12);
			Font f1 = new Font("TimesRoman",Font.PLAIN,16);
			lblEMPCT = new JLabel("Employees ");
			lblEMPCT1 = new JLabel();			
			lblVSTCT = new JLabel("Visitors ");
			lblVSTCT1 = new JLabel();		
			lblCNTCT = new JLabel("Contractors ");
			lblCNTCT1 = new JLabel();
			lblTRPCT = new JLabel("Count from Vehicle ");
			lblTRPCT1 = new JLabel();
			lblTOTCT = new JLabel("Total ");
			lblTOTCT1 = new JLabel();
			
			lblDETAL = new JLabel("Head Count :");						
			lblDETAL.setForeground(Color.blue);
			lblDETAL.setBounds(0,0,80,20);
			
			lblSUMCT.setForeground(Color.blue);
			lblSUMCT.setFont(f);
			lblDITCT.setForeground(Color.blue);
			lblDITCT.setFont(f);
			lblSUMCT.setBounds(10,0,500,15);
			lblDITCT.setBounds(10,215,500,15);
			
			lblSUMCT1.setForeground(Color.blue);
			lblSUMCT1.setFont(f);
			lblDITCT1.setForeground(Color.blue);
			lblDITCT1.setFont(f);
			lblSUMCT1.setBounds(10,0,500,15);
			lblDITCT1.setBounds(10,215,500,15);
			
			lblSUMCT2.setForeground(Color.blue);
			lblSUMCT2.setFont(f);
			lblDITCT2.setForeground(Color.blue);
			lblDITCT2.setFont(f);
			lblSUMCT2.setBounds(10,0,500,15);
			lblDITCT2.setBounds(10,215,500,15);
			
			lblSUMCT3.setForeground(Color.blue);
			lblSUMCT3.setFont(f);
			lblDITCT3.setForeground(Color.blue);
			lblDITCT3.setFont(f);
			lblSUMCT3.setBounds(10,0,500,15);
			lblDITCT3.setBounds(10,215,500,15);
			
			btnREFSH= new JButton("Refresh");
			btnEXIT= new JButton("Exit");
			btnDTLON= new JButton("Detail On");
			btnDTLOF= new JButton("Detail Off");
			rdbDTLON = new JRadioButton("Detail On",false);
			rdbDTLOF = new JRadioButton("Detail Off",false);
			
			grpDTLOPT = new ButtonGroup();
			grpDTLOPT.add(rdbDTLON);
			grpDTLOPT.add(rdbDTLOF);
			rdbDTLON.setSize(100,20);
            rdbDTLON.setLocation(730,30);
			rdbDTLOF.setSize(100,20);
            rdbDTLOF.setLocation(830,30);
			/*btnREFSH.setBounds(545,10,85,20);
			btnEXIT.setBounds(635,10,90,20);*/
			
			btnREFSH.setBounds(545,10,85,20);
			//btnDTLON.setBounds(635,10,90,20);
			//btnDTLOF.setBounds(730,10,90,20);
			btnEXIT.setBounds(635,10,90,20);
			
			lblEMPCT.setForeground(Color.blue);
			lblEMPCT.setFont(f);
			lblEMPCT1.setForeground(Color.black);
			lblEMPCT1.setFont(f1);
			lblVSTCT.setForeground(Color.blue);
			lblVSTCT.setFont(f);
			lblVSTCT1.setForeground(Color.black);
			lblVSTCT1.setFont(f1);
			lblCNTCT.setForeground(Color.blue);
			lblCNTCT.setFont(f);
			lblCNTCT1.setForeground(Color.black);
			lblCNTCT1.setFont(f1);
			lblTRPCT.setForeground(Color.blue);
			lblTRPCT.setFont(f);
			lblTRPCT1.setForeground(Color.black);
			lblTRPCT1.setFont(f1);
			lblTOTCT.setForeground(Color.blue);
			lblTOTCT.setFont(f);
			lblTOTCT1.setForeground(Color.black);
			lblTOTCT1.setFont(f1);
			
            
            pnlHEDCT.add(lblEMPCT);
            pnlHEDCT.add(new JLabel("              ")); 
            pnlHEDCT.add(lblVSTCT);
            pnlHEDCT.add(new JLabel("              "));
            pnlHEDCT.add(lblCNTCT);
            pnlHEDCT.add(new JLabel("              "));
            pnlHEDCT.add(lblTRPCT);
            pnlHEDCT.add(new JLabel("              "));
            pnlHEDCT.add(lblTOTCT);
           // pnlHEDCT.add(new JLabel("           "));
            
            pnlHEDCT.add(lblEMPCT1);
            pnlHEDCT.add(new JLabel("                            "));
            pnlHEDCT.add(lblVSTCT1);
            pnlHEDCT.add(new JLabel("                            "));
            pnlHEDCT.add(lblCNTCT1);
            pnlHEDCT.add(new JLabel("                  "));
            pnlHEDCT.add(new JLabel("                  "));
            pnlHEDCT.add(lblTRPCT1);
            pnlHEDCT.add(new JLabel("                            "));
            pnlHEDCT.add(lblTOTCT1);
           
			pnlEMPDL.add(lblSUMCT);
			pnlEMPDL.add(lblSUMCT);
			pnlEMPDL.add(lblDITCT);
			pnlVSTDL.add(lblSUMCT1);
			pnlVSTDL.add(lblDITCT1);
			pnlCNTDL.add(lblSUMCT2);
			pnlCNTDL.add(lblDITCT2);
			pnlTRPDL.add(lblSUMCT3);
			pnlTRPDL.add(lblDITCT3);
			
			pnlDSPREC.add(btnREFSH);
			pnlDSPREC.add(btnEXIT);
			pnlDSPREC.add(rdbDTLON);
			pnlDSPREC.add(rdbDTLOF);
			//pnlDSPREC.add(btnDTLON);
			//pnlDSPREC.add(btnDTLOF);
			
			pnlHEDCT.setSize(540,50);
			pnlHEDCT.setLocation(0,12);
			pnlDSPREC.add(pnlHEDCT);
			
			jtpMANTAB.setSize(780,500);
			jtpMANTAB.setLocation(20,90);
			//jtpMANTAB.addMouseListener(this);
			jtpMANTAB.addTab("Employees ",pnlEMPDL);
			jtpMANTAB.addTab("Visitors",pnlVSTDL);
			jtpMANTAB.addTab("Contractors",pnlCNTDL);
			jtpMANTAB.addTab("Count from Vehicle",pnlTRPDL);
			//jtpMANTAB.addTab("Gate Out Persons",pnlOUTDL);
			pnlDSPREC.add(jtpMANTAB);
			
			btnREFSH.setEnabled(true);
			btnEXIT.setEnabled(true);
			rdbDTLON.setVisible(false);
			rdbDTLOF.setVisible(false);
			jtpMANTAB.setVisible(false);
			if(M_strINCHK_pbst.equalsIgnoreCase("HC")||M_strINCHK_pbst.equalsIgnoreCase("IN"))
			{
				rdbDTLON.setVisible(true);
				rdbDTLOF.setVisible(true);
				jtpMANTAB.setVisible(true);
			}
			
			wndMAINWD = new JWindow(this);
			wndMAINDL = new JDialog(this);
			LM_DATFMT = "DMY";
			opnDBCON();

			try{
				String L_strSQLQRY = " SELECT CP_CMPNM FROM CO_CPMST where CP_CMPCD ='"+M_strCMPCD_pbst+"'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET !=null && L_rstRSSET.next())
					M_strCMPNM_prst=L_rstRSSET.getString("CP_CMPNM");
			}catch(Exception E){System.out.println(E+":CO_CPMST");}
			
			wndMAINDL.setTitle("Supreme Petrochem Limited" + "  ("+M_strCMPNM_prst+")                       "+ " Head Count Status as on "+setDATE(new java.util.Date())+" Hrs");
			wndMAINWD = new JWindow(wndMAINDL);
			wndMAINDL.setSize(950,100);
			//wndMAINDL.setBounds(0,0,950,120);
			wndMAINDL.getContentPane().add(pnlDSPREC);
			wndMAINDL.toFront();
			//wndMAINDL.show();
			wndMAINDL.setVisible(true);
			//chkINTM.setVisible(false);
			try{
				hstDOCTP = new Hashtable<String,String>();
				String L_strSQLQRY= "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXWBT' AND ifnull(CMT_STSFL,'')<>'X'";
				ResultSet L_rstRSSET= cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET != null)
				{
					while(L_rstRSSET.next())
						hstDOCTP.put(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
					L_rstRSSET.close();
				}	
			}catch(Exception Ex){System.out.println(Ex+":CO_CDTRN");}
			btnEXIT.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				//t1.t.stop();
				//t2.t.stop();
				//t1.t = null;
				//t2.t = null;
				//clsDBCON();
				setVisible(false);
				dispose();
				System.exit(0);
				
			}});
			btnREFSH.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				opnDBCON();
				refrsh();
				clrTABLE(tblEMPSM);
				clrTABLE(tblEMPDL);
				clrTABLE(tblVSTSM);
				clrTABLE(tblVSTDL);
				clrTABLE(tblCNTSM);
				clrTABLE(tblCNTDL);
				clrTABLE(tblTRPSM);
				clrTABLE(tblTRPDL);
				
				 L_ROWNO=0;
				 L_ROWNO1=0;
				 L_ROWNO2=0;
				 L_ROWNO3=0;
				 L_ROWNO4=0;
				getSUMDL();
				getHEDDL();
				getOUTDL();
				getLVEDL();
			}});
			rdbDTLON.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				opnDBCON();
			    wndMAINDL.setSize(950,700);
				wndMAINDL.validate();
				rdbDTLOF.setEnabled(true);
			}});
			rdbDTLOF.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				
					pnlDSPREC.remove(tblEMPDL);
					pnlDSPREC.remove(tblVSTDL);
					pnlDSPREC.remove(tblCNTDL);
					pnlDSPREC.remove(tblTRPDL);
					pnlDSPREC.updateUI();
				
				wndMAINDL.setSize(950,120);
				rdbDTLOF.setEnabled(false);
			}});
			
			
			addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//t1.t.stop();
				//t2.t.stop();
				//t1.t = null;
				//t2.t = null;
				
				setVisible(false);
				dispose();
				System.exit(0);
			 }});
	  }catch(Exception L_EX){
		  System.out.println("hr_vwhdc: "+L_EX);
	  }
  }

  public String nvlSTRVL(String LP_VARVL, String LP_DEFVL){
	try{
		if (LP_VARVL != null)
			LP_VARVL = LP_VARVL;
		else
			LP_VARVL = LP_DEFVL;
	}catch (Exception L_EX){
		System.out.println("nvl "+L_EX);
	}
	return LP_VARVL;
}
 

 /**Cretaes JTable on the passed Panel
 */
 public JTable crtTBLPNL1(JPanel LP_TBLPNL,String[] LP_COLHD,int LP_ROWCNT,int LP_XPOS,int LP_YPOS,int LP_WID,int LP_HGT,int[] LP_ARRGSZ){ 
	 try{
		 cl_tab2 L_TBLOBJ1; 
		 JPanel pnlTAB1 = new JPanel();
		 Object[][] L_TBLDT1;
		 L_TBLDT1 = crtTBLDAT(LP_ROWCNT,LP_COLHD.length); // Creating the Object Data
		 L_TBLOBJ1 = new cl_tab2(L_TBLDT1,LP_COLHD);
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
 
  public Object[][] crtTBLDAT(int LP_ROWCNT,int LP_COLCNT){
		int i = 0;
	    Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];;
		for(int j = 0;j < LP_ROWCNT;j++){
			//if(cl_dat.M_CHKTBL){
			//	i = 1;
			//	L_TBLDT[j][0] = new Boolean(false);
			//}else
				i = 0;
			for( int k = i;k < LP_COLCNT;k++){
				L_TBLDT[j][k] = "";
			}
		}
		//cl_dat.M_CHKTBL = true;
		return L_TBLDT;
  }
  
  /**Clears contents of table	 */
	public void clrTABLE(JTable P_TBL)
	{
		for(int i=0;i<P_TBL.getColumnCount();i++)
		{
			for(int j=0;j<P_TBL.getRowCount();j++)
			{
				P_TBL.setValueAt("",j,i);
			}
		}
	}
	/**method to display head count summery*/
  private void getSUMDL()
	{        
		try  
		{   
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strVSTTP = "";
			String L_strCRNDT = "current_date";
			
			int L_ROWNO=0,L_ROWNO1=0, L_ROWNO2=0,L_ROWNO3=0,L_intVSTCT=0,L_intCNTCT=0,L_intTRPCT=0,L_intCOUNT=0,L_intTEMP=0;
			
			//String L_strHDCT="",L_strLRYNO="",L_strVSTNM="",L_strTOTCT="",L_strTEMP="",L_strVSTCT1="",L_strEMPNM,L_strFULNM,L_strDPTNM="",L_strPRDPT="", L_strEMPNO="";
			
			L_intCNTCT = 0;
			L_intCOUNT = 0;
			String strMSG = "";
			String L_strDOCDS="";
			String L_strDOCTP="",L_strODOCTP="";;
			String strOLD_DPTNM="";
			StringTokenizer L_stnTOKEN;			
			java.sql.Timestamp L_tmpTIME;
			java.sql.Timestamp L_tmpTIME1;
			java.sql.Date L_tmpDATE;
			
				strSQLQRY ="Select cmt_codds,sum(ifnull(vs_vsict,0))-sum(ifnull(vs_vsoct,0)) vs_vstct from HR_VSTRN,co_cdtrn where vs_cmpcd = '"+M_strCMPCD_pbst+"' and cmt_cgmtp='SYS' and cmt_cgstp = 'HRXXVCT' and cmt_codcd = vs_vstct  AND ifnull(VS_STSFL,' ') = '6' and VS_VSTTP ='01' group by cmt_codds order by cmt_codds";
				//System.out.println(strSQLQRY);
				rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
				if(rstRSSET !=null)
				{
					while(rstRSSET.next())
					{
						
						
						tblVSTSM.setValueAt(nvlSTRVL(rstRSSET.getString("cmt_codds"),""),L_ROWNO1,TB6_DESCD);
						L_intTEMP = Integer.valueOf(nvlSTRVL(rstRSSET.getString("vs_vstct"),"0")).intValue();
						tblVSTSM.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO1,TB6_HEDCT);
						L_intVSTCT += L_intTEMP;
						L_ROWNO1++;
					}
					rstRSSET.close();
					tblVSTSM.setValueAt("Total  :"+L_intVSTCT,L_ROWNO1,TB6_HEDCT);
					
				}
			
			strSQLQRY ="Select vs_vstnm,sum(ifnull(vs_vsict,0))-sum(ifnull(vs_vsoct,0))vs_cntct from HR_VSTRN  where vs_cmpcd='"+M_strCMPCD_pbst+"' and ifnull(VS_STSFL,' ') <> 'X'  and VS_VSOTM IS NULL and VS_VSTTP ='02' group by vs_vstnm order by vs_vstnm";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET !=null)
			{
				while(rstRSSET.next())
				{
					tblCNTSM.setValueAt(nvlSTRVL(rstRSSET.getString("vs_vstnm"),""),L_ROWNO2,TB6_DESCD);
        			L_intTEMP = Integer.valueOf(nvlSTRVL(rstRSSET.getString("vs_cntct"),"")).intValue();
					tblCNTSM.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO2,TB6_HEDCT);
					L_intCNTCT += L_intTEMP;
        			L_ROWNO2 ++;
				}
    	    rstRSSET.close();
    	    tblCNTSM.setValueAt("Total  :"+L_intCNTCT,L_ROWNO2,TB3_CNTCT);
    	    }
			
			strSQLQRY ="select wb_tprds,sum(ifnull(wb_vsict,0))-sum(ifnull(wb_vsoct,0)) wb_trpct from mm_wbtrn where WB_CMPCD='"+M_strCMPCD_pbst+"' AND ifnull(WB_STSFL,'')<> 'X' AND WB_STSFL <>'9'  and date(wb_gindt)<=current_date group by wb_tprds order by wb_tprds";
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			//System.out.println(strSQLQRY);
			if(rstRSSET != null)
			{
				while(rstRSSET.next())
				{
					tblTRPSM.setValueAt(nvlSTRVL(rstRSSET.getString("WB_TPRDS"),""),L_ROWNO3,TB6_DESCD);
					L_intTEMP = Integer.valueOf(nvlSTRVL(rstRSSET.getString("wb_trpct"),"")).intValue();
					tblTRPSM.setValueAt(String.valueOf(L_intTEMP),L_ROWNO3,TB6_HEDCT);
					L_intTRPCT += L_intTEMP;
					L_ROWNO3++;
				}
				rstRSSET.close();	
				tblTRPSM.setValueAt("Total  :"+L_intTRPCT,L_ROWNO3,TB6_HEDCT);
			}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	catch(Exception L_E)
	{
		System.out.println("getSUMDL :"+L_E);
	}
}
  /**method to display head count Detail*/
  private void getHEDDL()
	{        
		try  
		{   
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strVSTTP = "";
			String L_strCRNDT = "current_date";
			
			int L_intVSTCT=0,L_intCNTCT=0,L_intTRPCT=0,L_intCOUNT=0,L_intTEMP=0,L_intHEDCT=0;
			String L_intEMPCT="",L_strEDEPT="",L_strVSTNM="",L_strTOTCT="",L_strTEMP="",L_strVSTCT1="",L_strEMPNM,L_strDPTNM="",L_strPRDPT="", L_strEMPNO="",L_strHEDCT="";
		
			L_intCNTCT = 0;
			L_intCOUNT = 0;
			String strMSG = "";
			String L_strDOCDS="";
			String L_strDOCTP="",L_strODOCTP="";;
			String strOLD_DPTNM="";
			StringTokenizer L_stnTOKEN;			
			java.sql.Timestamp L_tmpTIME;
			java.sql.Timestamp L_tmpTIME1;
			java.sql.Date L_tmpDATE;
			
			strSQLQRY ="Select EP_DPTNM,COUNT(*) EP_HDCNT from HR_EPMST  where EP_CMPCD='"+M_strCMPCD_pbst+"' AND ifnull(EP_INOST,'') in ( 'I') AND EP_LFTDT is null and ifnull(EP_STSFL,'') <>'X' and date(EP_PNCTM) = current_date group by ep_dptnm order by ep_dptnm";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			String L_strDPTNM1="";
			if(rstRSSET !=null)
			{
				while(rstRSSET.next())
				{
					L_strDPTNM=nvlSTRVL(rstRSSET.getString("EP_DPTNM"),"");
					tblEMPSM.setValueAt( L_strDPTNM,L_ROWNO4,TB6_DESCD);	
					L_intEMP = Integer.valueOf(nvlSTRVL(rstRSSET.getString("EP_HDCNT"),"0")).intValue();			
					tblEMPSM.setValueAt(String.valueOf(L_intEMP),L_ROWNO4,TB6_HEDCT);
					
					L_intHEDCT += L_intEMP;
					L_ROWNO4 ++;
				}
				rstRSSET.close();
				tblEMPSM.setValueAt("Total :"+L_intHEDCT,L_ROWNO4,TB6_HEDCT);
				 
			}
						
			strSQLQRY ="Select EP_EMPNO,ifnull(EP_FSTNM,' ')||' '||substr(ifnull(EP_MDLNM,' '),1,1)||'. '||ifnull(EP_LSTNM,' ') EP_EMPNM,EP_DPTNM,EP_PNCTM,EP_DPTCD from HR_EPMST where EP_CMPCD='"+M_strCMPCD_pbst+"' AND ifnull(EP_INOST,'') in ( 'I') AND ifnull(EP_STSFL,'') <>'X' and ep_lftdt is null and date(EP_PNCTM) = current_date  order by EP_DPTNM,EP_EMPNO";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			//tblEMPDL.setValueAt("Head Count",L_ROWNO,TB1_EDEPT);
			if(rstRSSET !=null)
			{
				
				while(rstRSSET.next())
				{
					
						L_intCOUNT =0;
					    L_strEMPNM =nvlSTRVL(rstRSSET.getString("EP_EMPNM"),"");
					    L_strDPTNM = nvlSTRVL(rstRSSET.getString("EP_DPTNM"),"");
					    L_strEMPNO = nvlSTRVL(rstRSSET.getString("EP_EMPNO"),"");
					 	//L_strEMPNM ="";					
					 	/*L_stnTOKEN = new StringTokenizer(L_strFULNM,"|");
		         		while(L_stnTOKEN.hasMoreTokens())
		           		{
		            	    L_strTEMP = L_stnTOKEN.nextToken();
		            	    L_intCOUNT++;
		            	    if(L_intCOUNT ==1)
		            	        L_strEMPNM =  L_strTEMP.substring(0,1) +". ";
		            	    else if(L_intCOUNT ==2)
		            	        L_strEMPNM +=  L_strTEMP.substring(0,1) +". ";
		            	    else
		            	    {
		            	        L_strEMPNM +=  L_strTEMP;
		            	        L_intCOUNT =0;
		            	     }
		            	}*/
		         		tblEMPDL.setValueAt(L_strEMPNO,L_ROWNO,TB1_EMPNO);
						tblEMPDL.setValueAt(L_strEMPNM,L_ROWNO,TB1_EMPNM);
						
						if(!L_strDPTNM.equals(L_strPRDPT))
						{
							tblEMPDL.setValueAt(L_strDPTNM,L_ROWNO,TB1_EDEPT);
							  //tblEMPDL.setCellcolor(L_ROWNO,TB1_EDEPT,Color.blue);
						    L_strPRDPT = L_strDPTNM;
						}
				//L_tmpTIME = rstRSSET.getTimestamp("swt_inctm");
						L_tmpTIME = rstRSSET.getTimestamp("EP_PNCTM");
						L_strTEMP="";
						if(L_tmpTIME != null && M_strINCHK_pbst.equalsIgnoreCase("IN"))
						{
							L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
							tblEMPDL.setValueAt(L_strTEMP,L_ROWNO,TB1_PNCTM);
						}
						
					/*L_tmpTIME = rstRSSET.getTimestamp("swt_outtm");
						//L_tmpTIME = rstRSSET.getTimestamp("EP_PNCTM");
						L_strTEMP="";
						if(L_tmpTIME != null && M_strINCHK_pbst.equalsIgnoreCase("IN"))
						{
							L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
							tblEMPDL.setValueAt(L_strTEMP,L_ROWNO,TB1_PNOTM);
						}*/
						
						L_intTEMP =L_ROWNO;
						L_ROWNO ++;
				}
				rstRSSET.close();
				tblEMPDL.setValueAt("Head Count  :"+L_intHEDCT,L_ROWNO+1,TB1_EMPNM);
				//tblTOTDL.setValueAt(String.valueOf(L_intHEDCT),2,TB0_EMPCT);
				lblEMPCT1.setText(String.valueOf(L_intHEDCT));
				//System.out.println("inside2"+L_intHEDCT);
				L_ROWNO +=5;
				
			}
			 
			strSQLQRY ="Select vs_vstnm,vs_pervs,vs_vsitm,VS_VSOCT,VS_VSICT from HR_VSTRN where VS_CMPCD='"+M_strCMPCD_pbst+"' AND ifnull(VS_STSFL,' ') = '6' and VS_VSTTP ='01'";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET !=null)
			{
				while(rstRSSET.next())
				{
					tblVSTDL.setValueAt(nvlSTRVL(rstRSSET.getString("VS_VSTNM"),""),L_ROWNO1,TB2_VSTNM);				 		
			 		tblVSTDL.setValueAt(nvlSTRVL(rstRSSET.getString("VS_PERVS"),""),L_ROWNO1,TB2_PERVS);
					L_tmpTIME = rstRSSET.getTimestamp("VS_VSITM");
					L_strTEMP="";		
					if(L_tmpTIME != null && M_strINCHK_pbst.equalsIgnoreCase("IN"))
					{
							L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
							tblVSTDL.setValueAt(L_strTEMP,L_ROWNO1,TB2_VSITM);
					}
					
					L_intTEMP = Integer.valueOf(nvlSTRVL(rstRSSET.getString("VS_VSICT"),"0")).intValue() - Integer.valueOf(nvlSTRVL(rstRSSET.getString("VS_VSOCT"),"0")).intValue();
					tblVSTDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO1,TB2_VSTCT);
					L_intVSTCT += L_intTEMP;
					L_ROWNO1++;
				}
				rstRSSET.close();
				//tblTOTDL.setValueAt(String.valueOf(L_intVSTCT),2,TB0_VSTCT);
				tblVSTDL.setValueAt("Head Count  :"+L_intVSTCT,L_ROWNO1+1,TB2_VSTCT);
				lblVSTCT1.setText(String.valueOf(L_intVSTCT));
				L_ROWNO1+=5;
			}
			strSQLQRY ="Select  vs_vstnm,vs_pervs,vs_vsitm,vs_vsict,vs_vsoct from HR_VSTRN  where VS_CMPCD='"+M_strCMPCD_pbst+"' AND ifnull(VS_STSFL,' ') <> 'X'  and VS_VSOTM IS NULL and VS_VSTTP ='02'";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET !=null)
			{
				while(rstRSSET.next())
				{
					tblCNTDL.setValueAt(nvlSTRVL(rstRSSET.getString("VS_VSTNM"),""),L_ROWNO2,TB3_CNTNM);
        			L_tmpTIME = rstRSSET.getTimestamp("VS_VSITM");
        			L_strTEMP="";
					if(L_tmpTIME != null && M_strINCHK_pbst.equalsIgnoreCase("IN"))
	        			{
	        				L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
	        				tblCNTDL.setValueAt(L_strTEMP,L_ROWNO2,TB3_CNITM);
	        			}
					
        			L_intTEMP = Integer.valueOf(nvlSTRVL(rstRSSET.getString("VS_VSICT"),"0")).intValue() - Integer.valueOf(nvlSTRVL(rstRSSET.getString("VS_VSOCT"),"0")).intValue();						
					tblCNTDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO2,TB3_CNTCT);
					L_intCNTCT += L_intTEMP;
        			L_ROWNO2 ++;
    		}
    	    rstRSSET.close();
    	    tblCNTDL.setValueAt("Head Count  :"+String.valueOf(L_intCNTCT).toString(),L_ROWNO2+1,TB3_CNTCT);
    	   // tblTOTDL.setValueAt(String.valueOf(L_intCNTCT),2,TB0_CNTCT);
    	    lblCNTCT1.setText(String.valueOf(L_intCNTCT));
    	    L_ROWNO2+=5;
			}
			
			strSQLQRY ="select wb_vsict,wb_vsoct,wb_tprds,wb_doctp,wb_gindt,wb_gotdt from mm_wbtrn where WB_CMPCD='"+M_strCMPCD_pbst+"' AND ifnull(WB_STSFL,'')<> 'X' AND WB_STSFL <>'9'  and date(wb_gindt)<=current_date";
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			//System.out.println(strSQLQRY);
			if(rstRSSET != null)
			{
				while(rstRSSET.next())
				{
					L_strDOCTP = nvlSTRVL(rstRSSET.getString("WB_DOCTP"),"");
					if(!L_strDOCTP.equals(L_strODOCTP))	
					{	
						if(hstDOCTP.containsKey(L_strDOCTP))
							L_strDOCDS = hstDOCTP.get(L_strDOCTP).toString();
							tblTRPDL.setValueAt(L_strDOCDS,L_ROWNO3,TB4_STRTP);	
						
							//if(!L_strODOCTP.equals(""))							
							//lblTRPCT.setText(lblTRPCT.getText()+"   "+hstDOCTP.get(L_strODOCTP).toString()+" : "+L_intCOUNT);
						 	
						L_strODOCTP = L_strDOCTP;
						
					}
					
					tblTRPDL.setValueAt(nvlSTRVL(rstRSSET.getString("WB_TPRDS"),""),L_ROWNO3,TB4_TPRDS);
					java.sql.Timestamp L_tsmTEMP = rstRSSET.getTimestamp("WB_GINDT");
					if(L_tsmTEMP != null && M_strINCHK_pbst.equalsIgnoreCase("IN"))
						tblTRPDL.setValueAt(M_fmtLCDTM.format(L_tsmTEMP),L_ROWNO3,TB4_GINDT);
					
					L_intTEMP = rstRSSET.getInt("WB_VSICT") - rstRSSET.getInt("WB_VSOCT");					
					tblTRPDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO3,TB4_COUNT);
					L_intTRPCT += L_intTEMP;
					L_ROWNO3++;
				}
				rstRSSET.close();				
				tblTRPDL.setValueAt("Head Count :"+String.valueOf(L_intTRPCT).toString(),L_ROWNO3+1,TB4_COUNT);
				//tblTOTDL.setValueAt(String.valueOf(L_intTRPCT),2,TB0_TRPCT);
				lblTRPCT1.setText(String.valueOf(L_intTRPCT));
				L_ROWNO3+=5; 
			}
			L_intHEDCT=Integer.valueOf(lblEMPCT1.getText().toString()).intValue()+Integer.valueOf(lblVSTCT1.getText().toString()).intValue()+Integer.valueOf(lblCNTCT1.getText().toString()).intValue()+Integer.valueOf(lblTRPCT1.getText().toString()).intValue();
			//tblTOTDL.setValueAt(String.valueOf(L_intHDCT).toString(),2,TB0_GRTOT);
			lblTOTCT1.setText(String.valueOf(L_intHEDCT));
		}
		catch(Exception L_E)
		{
			System.out.println("getHEDDL :"+L_E);
		}
	}

  /**method to display out time in particular table*/
 
 private void getOUTDL()
	{        
		try  
		{   
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strVSTTP = "";
			String L_strCRNDT = "current_date";
			
			int L_intVSTCT2=0,L_intCNTCT=0,L_intCNTCT2=0,L_intTRPCT2=0,L_intCOUNT=0,L_intTEMP=0,L_intOUTCT=0;
			String L_strHEDCT1="",L_strEDEPT="",L_strTEMP="",L_strEMPNM,L_strDPTNM="",L_strPRDPT="", L_strEMPNO="";
			
			L_intCOUNT = 0;
			String strMSG = "";
			String L_strDOCDS="";
			String L_strDOCTP="",L_strODOCTP="";;
			String strOLD_EMPNM="";
			StringTokenizer L_stnTOKEN;			
			java.sql.Timestamp L_tmpTIME;
			java.sql.Timestamp L_tmpTIME1;
			java.sql.Date L_tmpDATE;
					
			
			strSQLQRY =" Select swt_empno,ifnull(EP_FSTNM,' ')||' '||substr(ifnull(EP_MDLNM,' '),1,1)||'. '||ifnull(EP_LSTNM,' ') EP_EMPNM,EP_DPTNM,swt_inctm,swt_outtm from HR_EPMST,hr_swtrn ";
			strSQLQRY +="where EP_CMPCD='"+M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_cmpcd = swt_cmpcd and ep_empno = swt_empno ";
			strSQLQRY +="and((swt_outtm is null and days(date(swt_inctm))= days(current_date)-1) or (swt_outtm is not null and date(swt_outtm)= current_date)) and ifnull(EP_INOST,'') in ( 'O') AND ifnull(EP_STSFL,'') <>'X'";
			strSQLQRY +="and date(EP_PNCTM)  = current_date order by EP_DPTNM,swt_EMPNO,ifnull(swt_inctm,swt_outtm)";

			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET !=null)
			{
				tblEMPDL.setValueAt("OUT Persons",L_ROWNO-2,TB1_EDEPT);
				while(rstRSSET.next())
				{
						L_intCOUNT =0;
					    L_strEMPNM =nvlSTRVL(rstRSSET.getString("EP_EMPNM"),"");
					if(!strOLD_EMPNM.equals(L_strEMPNM))
	        		{	
	        			  
					    L_strDPTNM = nvlSTRVL(rstRSSET.getString("EP_DPTNM"),"");
					    L_strEMPNO = nvlSTRVL(rstRSSET.getString("swt_EMPNO"),"");
					 	//L_strEMPNM ="";					
					 	/*L_stnTOKEN = new StringTokenizer(L_strFULNM,"|");
		         		while(L_stnTOKEN.hasMoreTokens())
		           		{
		            	    L_strTEMP = L_stnTOKEN.nextToken();
		            	    L_intCOUNT++;
		            	    if(L_intCOUNT ==1)
		            	        L_strEMPNM =  L_strTEMP.substring(0,1) +". ";
		            	    else if(L_intCOUNT ==2)
		            	        L_strEMPNM +=  L_strTEMP.substring(0,1) +". ";
		            	    else
		            	    {
		            	        L_strEMPNM +=  L_strTEMP;
		            	        L_intCOUNT =0;
		            	     }
		            	}*/
		         		tblEMPDL.setValueAt(L_strEMPNO,L_ROWNO,TB1_EMPNO);
						tblEMPDL.setValueAt(L_strEMPNM,L_ROWNO,TB1_EMPNM);
						strOLD_EMPNM=rstRSSET.getString("EP_EMPNM");
	        		}
						if(!L_strDPTNM.equals(L_strPRDPT))
						{
						    tblEMPDL.setValueAt(L_strDPTNM,L_ROWNO,TB1_EDEPT);
						   // tblEMPDL.setCellcolor(L_ROWNO,TB1_EDEPT,Color.blue);
						  L_strPRDPT = L_strDPTNM;
						}
						L_tmpTIME = rstRSSET.getTimestamp("swt_inctm");
						//L_tmpTIME = rstRSSET.getTimestamp("EP_PNCTM");
						L_strTEMP="";
							if (L_tmpTIME != null)
							{
								L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
								tblEMPDL.setValueAt(L_strTEMP,L_ROWNO,TB1_PNCTM);
							}
						L_tmpTIME = rstRSSET.getTimestamp("swt_outtm");
						//L_tmpTIME = rstRSSET.getTimestamp("EP_PNCTM");
						L_strTEMP="";
							if (L_tmpTIME != null)
							{
								L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
								tblEMPDL.setValueAt(L_strTEMP,L_ROWNO,TB1_PNOTM);
									
							}
						L_intTEMP =L_ROWNO;
						L_ROWNO ++;
					}
				rstRSSET.close();
				
			}
			
			strSQLQRY ="Select COUNT(*) EP_OTCNT from HR_EPMST where EP_CMPCD='"+M_strCMPCD_pbst+"' and ep_lftdt is null AND ifnull(EP_INOST,'') in ( 'O') AND ifnull(EP_STSFL,'') <>'X' and date(EP_PNCTM) = current_date";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET !=null)
			{
				while(rstRSSET.next())
				{
					L_strHEDCT1=nvlSTRVL(rstRSSET.getString("EP_OTCNT"),"");
					//tblTOTDL.setValueAt(L_strTEMP,1,TB0_EMPCT);
					tblEMPDL.setValueAt("OUT    :"+nvlSTRVL(rstRSSET.getString("EP_OTCNT"),""),L_ROWNO+1,TB1_EMPNM);
					if(M_strINCHK_pbst.equalsIgnoreCase("OUT"))
						lblEMPCT1.setText(nvlSTRVL(rstRSSET.getString("EP_OTCNT"),""));
				}
				rstRSSET.close();
				L_ROWNO+=5;
			}
			
			strSQLQRY ="select vs_vspno,vs_vstnm,vs_pervs,vs_vsotm,vs_vsitm,vs_vsict,vs_vsoct from hr_vstrn where vs_cmpcd = '"+M_strCMPCD_pbst+"' and date(vs_vsitm)<=current_date and date(vs_vsotm)=current_date  and vs_vsttp = '01' and vs_vsotm is not null";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET !=null)
			{
				tblVSTDL.setValueAt("  OUT Persons",L_ROWNO1-2,TB2_VSTNM);
				while(rstRSSET.next())
				{
					tblVSTDL.setValueAt(nvlSTRVL(rstRSSET.getString("VS_VSTNM"),""),L_ROWNO1,TB2_VSTNM);				 		
			 		tblVSTDL.setValueAt(nvlSTRVL(rstRSSET.getString("VS_PERVS"),""),L_ROWNO1,TB2_PERVS);
					L_tmpTIME = rstRSSET.getTimestamp("VS_VSOTM");
					L_strTEMP="";					
					if (L_tmpTIME != null)
					{
						L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
						tblVSTDL.setValueAt(L_strTEMP,L_ROWNO1,TB2_VSOTM);
					}
					L_tmpTIME = rstRSSET.getTimestamp("VS_VSITM");
					L_strTEMP="";					
					if (L_tmpTIME != null)
					{
						L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
						tblVSTDL.setValueAt(L_strTEMP,L_ROWNO1,TB2_VSITM);
					}
					L_intTEMP = Integer.valueOf(nvlSTRVL(rstRSSET.getString("VS_VSOCT"),"0")).intValue();						
					tblVSTDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO1,TB2_VSTCT);
					L_intVSTCT2 += L_intTEMP;
					L_ROWNO1++;
				}
			rstRSSET.close();
			//tblTOTDL.setValueAt(String.valueOf(L_intVSTCT2),1,TB0_VSTCT);
			tblVSTDL.setValueAt("OUT   :"+L_intVSTCT2,L_ROWNO1+1,TB2_VSTCT);
			if(M_strINCHK_pbst.equalsIgnoreCase("OUT"))
				lblVSTCT1.setText(String.valueOf(L_intVSTCT2));
			
			}
			
			strSQLQRY ="select vs_vstnm,vs_pervs,vs_vsotm,vs_vsitm,vs_vsict,vs_vsoct from hr_vstrn where  date(vs_vsotm)=current_date  and vs_cmpcd = '"+M_strCMPCD_pbst+"' and vs_vsttp = '02' and vs_vsotm is not null and ifnull(VS_STSFL,' ') <> 'X'";
			//System.out.println(strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET !=null)
			{
				tblCNTDL.setValueAt("  OUT Persons",L_ROWNO2-2,TB3_CNTNM);
				while(rstRSSET.next())
				{
					tblCNTDL.setValueAt(nvlSTRVL(rstRSSET.getString("VS_VSTNM"),""),L_ROWNO2,TB3_CNTNM);
        			L_tmpTIME = rstRSSET.getTimestamp("VS_VSOTM");
        			L_strTEMP="";
        			if (L_tmpTIME != null)
        			{
        				L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
        				tblCNTDL.setValueAt(L_strTEMP,L_ROWNO2,TB3_CNOTM);
        			}
        			L_tmpTIME = rstRSSET.getTimestamp("VS_VSITM");
        			L_strTEMP="";
        			if (L_tmpTIME != null)
        			{
        				L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
        				tblCNTDL.setValueAt(L_strTEMP,L_ROWNO2,TB3_CNITM);
        			}
        			L_intTEMP =Integer.valueOf(nvlSTRVL(rstRSSET.getString("VS_VSOCT"),"0")).intValue();						
					tblCNTDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO2,TB3_CNTCT);
					L_intCNTCT2 += L_intTEMP;
        			L_ROWNO2 ++;
    		}
    	    rstRSSET.close();
    	    tblCNTDL.setValueAt("OUT    :"+String.valueOf(L_intCNTCT2).toString(),L_ROWNO2+1,TB3_CNTCT);
    	    if(M_strINCHK_pbst.equalsIgnoreCase("OUT"))
    	    	lblCNTCT1.setText(String.valueOf(L_intCNTCT2));
    	   // tblTOTDL.setValueAt(String.valueOf(L_intCNTCT2),1,TB0_CNTCT);
    	   }

			strSQLQRY ="select wb_vsict,wb_vsoct,wb_tprds,wb_doctp,wb_gindt,wb_gotdt from mm_wbtrn where WB_CMPCD='"+M_strCMPCD_pbst+"' AND date(wb_gindt)<=current_date and date(wb_gotdt)=current_date and ifnull(WB_STSFL,'')<> 'X' AND WB_STSFL ='9'" ;
			rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			//System.out.println(strSQLQRY);
			if(rstRSSET != null)
			{
				tblTRPDL.setValueAt("  OUT Persons",L_ROWNO3-2,TB4_STRTP);
				while(rstRSSET.next())
				{
					L_strDOCTP = nvlSTRVL(rstRSSET.getString("WB_DOCTP"),"");
					if(!L_strDOCTP.equals(L_strODOCTP))	
					{	
						if(hstDOCTP.containsKey(L_strDOCTP))
							L_strDOCDS = hstDOCTP.get(L_strDOCTP).toString();
							tblTRPDL.setValueAt(L_strDOCDS,L_ROWNO3,TB4_STRTP);	
						
							//if(!L_strODOCTP.equals(""))							
							//lblTRPCT.setText(lblTRPCT.getText()+"   "+hstDOCTP.get(L_strODOCTP).toString()+" : "+L_intCOUNT);
						 	
						L_strODOCTP = L_strDOCTP;
						L_intCOUNT = 0;
					}
					
					tblTRPDL.setValueAt(nvlSTRVL(rstRSSET.getString("WB_TPRDS"),""),L_ROWNO3,TB4_TPRDS);
					java.sql.Timestamp L_tsmTEMP = rstRSSET.getTimestamp("WB_GOTDT");
					if(L_tsmTEMP != null)
						tblTRPDL.setValueAt(M_fmtLCDTM.format(L_tsmTEMP),L_ROWNO3,TB4_GOTDT);
					L_tsmTEMP = rstRSSET.getTimestamp("WB_GINDT");
					if(L_tsmTEMP != null)
						tblTRPDL.setValueAt(M_fmtLCDTM.format(L_tsmTEMP),L_ROWNO3,TB4_GINDT);
					
					L_intTEMP = rstRSSET.getInt("WB_VSOCT");					
					L_intCOUNT += L_intTEMP;
					L_intTRPCT2 += L_intTEMP;
					tblTRPDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO3,TB4_COUNT);
					L_ROWNO3++;
				}
				rstRSSET.close();				
				tblTRPDL.setValueAt("OUT    :"+String.valueOf(L_intTRPCT2).toString(),L_ROWNO3+1,TB4_COUNT);
				//tblTOTDL.setValueAt(String.valueOf(L_intTRPCT2),1,TB0_TRPCT);
				/*if(M_strINCHK_pbst.equalsIgnoreCase("OUT"))
					lblTRPCT1.setText(String.valueOf(L_intTRPCT2));
				 L_intOUTCT=Integer.valueOf(lblEMPCT1.getText().toString()).intValue()+Integer.valueOf(lblVSTCT1.getText().toString()).intValue()+Integer.valueOf(lblCNTCT1.getText().toString()).intValue()+Integer.valueOf(lblTRPCT1.getText().toString()).intValue();
				if(M_strINCHK_pbst.equalsIgnoreCase("OUT"))
					lblTOTCT1.setText(String.valueOf(L_intOUTCT));*/
				
			}
			//L_intINCT=Integer.valueOf(L_strHEDCT1.toString()).intValue()+L_intHEDCT;
			//if(M_strINCHK_pbst.equalsIgnoreCase("IN"))
				//lblEMPCT1.setText(String.valueOf(L_intINCT));
			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			System.out.println("getOUTDL :"+L_E);
		}
	}
 /**method to display leave Detail*/
 private void getLVEDL()
	{        
		try  
		{   
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strTEMP = "";
			String L_strDOCDS="";
			String L_strCOUNT="",L_strVALUE="",L_strSTSFL="",L_strLVECD="",L_strEMPNO="",L_strEMPNM="",L_strDOCTP="",L_strODOCTP="",L_strDPTNM="",L_strPRDPT="";
			String strOLD_EMPNM="";
			StringTokenizer L_stnTOKEN;		
			
			int L_intLVTCT=0,L_intCOUNT=0,L_intTEMP=0;
			java.sql.Timestamp L_tmpTIME;
			java.sql.Timestamp L_tmpTIME1;
			java.sql.Date L_tmpDATE;
			
			
			 strSQLQRY =" select lvt_empno,ifnull(EP_FSTNM,' ')||' '||substr(ifnull(EP_MDLNM,' '),1,1)||'. '||ifnull(EP_LSTNM,' ') EP_EMPNM,ep_dptnm,lvt_stsfl,lvt_lvecd,min(lvt_lvedt) min_lvedt,max(lvt_lvedt) max_lvedt from hr_lvtrn,hr_epmst where lvt_cmpcd='"+M_strCMPCD_pbst+"' and lvt_cmpcd= ep_cmpcd and lvt_empno=ep_empno and  ep_lftdt is null and lvt_empno || char(lvt_refdt) in (select lvt_empno || char(lvt_refdt) from hr_lvtrn where lvt_cmpcd='"+M_strCMPCD_pbst+"'  and LVT_LVECD <> 'PE' and date(lvt_lvedt)= current_date) group by lvt_empno,ep_fstnm,ep_mdlnm,ep_lstnm,ep_dptnm,lvt_stsfl,lvt_lvecd order by EP_DPTNM,lvt_EMPNO";
			 //System.out.println("strSQLQRY"+strSQLQRY);
			 rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			 	if(rstRSSET !=null)
				{
			 		tblEMPDL.setValueAt("  Employyes on Leave",L_ROWNO-2,TB1_EDEPT);
			 		tblEMPDL.setValueAt("  From Date",L_ROWNO-1,TB1_PNCTM);
			 		tblEMPDL.setValueAt("  To Date",L_ROWNO-1,TB1_PNOTM);
					while(rstRSSET.next())
					{
							L_intCOUNT =0;
					    L_strEMPNM =nvlSTRVL(rstRSSET.getString("EP_EMPNM"),"");
					    L_strDPTNM = nvlSTRVL(rstRSSET.getString("EP_DPTNM"),"");
					    L_strEMPNO = nvlSTRVL(rstRSSET.getString("lvt_empno"),"");
					    L_intLVTCT+=1;
					    
					    L_strSTSFL= nvlSTRVL(rstRSSET.getString("lvt_stsfl"),"");
					    L_strLVECD= nvlSTRVL(rstRSSET.getString("lvt_lvecd"),"");
					    if(L_strSTSFL.equals("01"))
							L_strVALUE="*";
						else
							L_strVALUE="";
					   // System.out.println("L_strSTSFL"+L_strSTSFL);
						
						 	//L_strEMPNM ="";					
						 	/*L_stnTOKEN = new StringTokenizer(L_strFULNM,"|");
			         		while(L_stnTOKEN.hasMoreTokens())
			           		{
			            	    L_strTEMP = L_stnTOKEN.nextToken();
			            	    L_intCOUNT++;
			            	    if(L_intCOUNT ==1)
			            	        L_strEMPNM =  L_strTEMP.substring(0,1) +". ";
			            	    else if(L_intCOUNT ==2)
			            	        L_strEMPNM +=  L_strTEMP.substring(0,1) +". ";
			            	    else
			            	    {
			            	        L_strEMPNM +=  L_strTEMP;
			            	        L_intCOUNT =0;
			            	     }
			            	}*/
			         		tblEMPDL.setValueAt(L_strEMPNO,L_ROWNO,TB1_EMPNO);
							tblEMPDL.setValueAt(L_strEMPNM+" "+"("+L_strLVECD+")"+L_strVALUE,L_ROWNO,TB1_EMPNM);
							
						if(!L_strDPTNM.equals(L_strPRDPT))
						{
						    tblEMPDL.setValueAt(L_strDPTNM,L_ROWNO,TB1_EDEPT);
						   // tblEMPDL.setCellcolor(L_ROWNO,TB1_EDEPT,Color.blue);
						  L_strPRDPT = L_strDPTNM;
						}
						L_tmpTIME = rstRSSET.getTimestamp("min_lvedt");
						
						//L_tmpTIME = rstRSSET.getTimestamp("EP_PNCTM");
						L_strTEMP="";
							if (L_tmpTIME != null)
							{
								L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
								tblEMPDL.setValueAt(L_strTEMP,L_ROWNO,TB1_PNCTM);
								
							}
						L_tmpTIME = rstRSSET.getTimestamp("max_lvedt");
						//L_tmpTIME = rstRSSET.getTimestamp("EP_PNCTM");
						L_strTEMP="";
							if (L_tmpTIME != null)
							{
								L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
								tblEMPDL.setValueAt(L_strTEMP,L_ROWNO,TB1_PNOTM);
									
							}
						L_intTEMP =L_ROWNO;
						L_ROWNO ++;
					}
				rstRSSET.close();
				 
				tblEMPDL.setValueAt("Employees on Leave   :"+String.valueOf(L_intLVTCT).toString(),L_ROWNO+1,TB1_EMPNM);
			}
				setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			System.out.println("getLVEDL :"+L_E);
		}
	}

 /**method to display refresh label*/
 private void refrsh(){    
        try{
      
        	lblEMPCT1.setText("");
        	lblVSTCT1.setText("");
        	lblCNTCT1.setText("");
        	lblTRPCT1.setText("");
        	lblTOTCT1.setText("");
        	
		}catch (Exception L_EX) {
			System.out.println("refrsh :"+L_EX);
		}
	}

/**
 */
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
                {       String L_STRURL = "", L_STRDRV = "";
						if(LP_SYSLC.equals("01"))
                        {
                                L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                                L_STRURL = "jdbc:as400://SPLWS01/";
                                Class.forName(L_STRDRV);
                        }
                        else if(LP_SYSLC.equals("02"))
                        {
                                int port = 50000;
                                LP_DTBUS = "SPLDATA";
                                LP_DTBPW = "SPLDATA";

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
class hr_vwhdc_RightTableCellRenderer extends DefaultTableCellRenderer {    
	  protected  hr_vwhdc_RightTableCellRenderer()
	  {   
	    setHorizontalAlignment(JLabel.RIGHT);  
	    } 	
}