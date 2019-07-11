
/*
System Name   : Human Resource Management System
Program Name  : Head Count Query
Program Desc. : total No of Employee,Visitors,Contractors displays. 
Author        : Mrs.Dipti.S.Shinde.
Date          : 28th Oct 2005
System        : Query Program.
Version       : MMS v2.0.0
Modificaitons : 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/
import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTabbedPane;import javax.swing.JLabel;import javax.swing.JTable;
import javax.swing.JTextField;import javax.swing.JPanel;
import java.awt.Color;import java.util.StringTokenizer;import java.util.Hashtable;

/**
<P><PRE style = font-size : 10 pt >

<b>System Name :</b> Common Master Records
 
<b>Program Name :</b> Head Counter Query.

<b>Purpose :</b> This Query program shows Total no of Employees,visitors 
                 Contractors presen in works area.this head counter program 
       	
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
HR_EPMST       EP_EMPNO                                      #      
HR_VSTRN       VS_VSPNO,VS_VSTDT                             #
----------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		 Type/Size       Description
----------------------------------------------------------------------------------------------------------
tblVSTDL  VS_VSTNM         HR_VSTRN         VARCHAR(35)     Visitor Name   
& tblCNTDL VS_VSPNO                         VARCHAR(8)      Visitor Pass Number
            VS_PERVS                         VARCHAR(15)     Person visited
            VS_VSTCT                         DECIMAL(3)      Visitor Counter
----------------------------------------------------------------------------------------------------------

*/
public class hr_qrhdc extends cl_pbase 
{                                   /** Label Displays on the Screen */
	private JLabel lblVSTTP,lblCURDT,lblEMPCT,lblVSTCT,lblTOTAL,lblCNTCT,lblTOTCT,lblDETAL;
	                                /** TextField for Current Date */
	private JTextField txtCURDT;    /** TextField for Employee Counter  */
	private JTextField txtEMPCT;    /** TextField for Visitor Counter */
	private JTextField txtVSTCT;    /** TextField for Contractors Counter */
	private JTextField txtCNTCT;    /** TextField for Total counting of In Employee */
	private JTextField txtTOTCT; 
	private JTextField txtTRPCT;
	                                /** table for a Employee details */
	private cl_JTable tblEMPDL;    /** table for Visitor Details */
	private cl_JTable tblVSTDL;    /** table Contractors Details */
	private cl_JTable tblCNTDL;
	private cl_JTable tblTRPDL;    
	/** Main Jtabbed pane*/
	private JTabbedPane jtpMANTAB;  /** Panel foe Visitors Table */
	private JPanel pnlVSTDL;       /** Panel foe Contractors Table */
	private JPanel pnlCNTDL;       /** Panel foe Employee Table */
	private JPanel pnlEMPDL; 
	private JPanel pnlTRPDL;
	private JLabel lblTRPCT;
									/** Final value for column of Employee's table */	
	private final int TB1_CHKFL =0; 
	private final int TB1_EDEPT =1; 
	private final int TB1_EMPNO =2;
	private final int TB1_EMPNM =3;
	private final int TB1_PNCTM =4;
	                                /** Final value for Column of Visitors table */
	private final int TB2_CHKFL =0;
	private final int TB2_VSTNM =1;
	private final int TB2_VSTCT =2;
	private final int TB2_PERVS =3;
	private final int TB2_VSITM =4;
	                                /** Final value for Column of Contractors table */
	private final int TB3_CHKFL =0;
	private final int TB3_CNTNM =1;
	private final int TB3_CNTCT =2;
	private final int TB3_CNITM =3;
	
	private final int TB4_CHKFL =0;
	private final int TB4_STRTP =1;
	private final int TB4_TPRDS =2;
	private final int TB4_COUNT =3;
	private final int TB4_GINDT =4;
	private Hashtable<String,String> hstDOCTP;
	
	
	hr_qrhdc()    
	{
		super(2);
		try
		{
			setMatrix(20,8);
			
			//add(lblDETAL = new JLabel(" Head Count "),1,1,1,2,this,'L');
				
			add(lblEMPCT = new JLabel("Total Employees"),3,2,1,1.5,this,'L');
			add(txtEMPCT = new JTextField(),3,3,1,.7,this,'R');			
			add(lblVSTCT = new JLabel("Visitors"),3,4,1,.8,this,'R');
			add(txtVSTCT = new JTextField(),3,5,1,1,this,'L');			
			add(lblCNTCT = new JLabel("Contractors"),3,6,1,0.9,this,'R');
			add(txtCNTCT = new JTextField(),3,7,1,1,this,'L');
			
			add(lblEMPCT = new JLabel("Count from Vehicle"),4,2,1,2,this,'L');
			add(txtTRPCT = new JTextField(),4,3,1,.7,this,'R');
			add(lblTOTCT = new JLabel("Grand Total"),4,4,1,.8,this,'R');
			add(txtTOTCT = new JTextField(),4,5,1,1,this,'L');
			
			add(lblDETAL = new JLabel("Head Count Details"),5,2,1,2,this,'L');						
			lblDETAL.setForeground(Color.blue);

			pnlVSTDL = new JPanel();
			pnlCNTDL = new JPanel();
			pnlEMPDL = new JPanel();
			pnlTRPDL = new JPanel();
			
			jtpMANTAB = new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
		
			pnlEMPDL.setLayout(null);
			String[] L_strCOLHD = {"Select","Department","Employee No.","Employee Name","In Time"};
			int[] L_intCOLSZ = {30,100,100,200,125};	    				
			tblEMPDL = crtTBLPNL1(pnlEMPDL,L_strCOLHD,200,1,1,9.7,6,L_intCOLSZ,new int[]{0});			
			add(pnlEMPDL,1,1,10,7,jtpMANTAB,'L');

			pnlVSTDL.setLayout(null);
			String[] L_strCOLHD1 = {"Select","Visitor Name","No of Persons","To See a person","In Time"};
			int[] L_intCOLSZ1 = {30,200,70,130,125};	
			tblVSTDL = crtTBLPNL1(pnlVSTDL,L_strCOLHD1,200,1,1,9.7,6,L_intCOLSZ1,new int[]{0});			
			add(pnlVSTDL,1,1,10,6,jtpMANTAB,'L');
			
			pnlCNTDL.setLayout(null);
			String[] L_strCOLHD2 = {"Select","Contractor Name","No of Persons","In Time"};
      		int[] L_intCOLSZ2 = {30,225,100,200};	    				
			tblCNTDL = crtTBLPNL1(pnlCNTDL,L_strCOLHD2,200,1,1,9.7,6,L_intCOLSZ2,new int[]{0});
			add(pnlCNTDL,1,1,10,7,jtpMANTAB,'L');
		
			pnlTRPDL.setLayout(null);
			add(lblTRPCT = new JLabel(),1,1,1,6,pnlTRPDL,'L');
			lblTRPCT.setForeground(Color.blue);
			String[] L_strCOLHD3 = {"Select","Category","Transporter","No of Persons","In Time"};
      		int[] L_intCOLSZ3 = {30,100,125,100,200};	    				
			tblTRPDL = crtTBLPNL1(pnlTRPDL,L_strCOLHD3,200,2,1,8.7,6,L_intCOLSZ3,new int[]{0});
			add(pnlTRPDL,2,1,9,7,jtpMANTAB,'L');
				
			jtpMANTAB.addTab("Employees ",pnlEMPDL);
			jtpMANTAB.addTab("Visitors",pnlVSTDL);
			jtpMANTAB.addTab("Contractors",pnlCNTDL);
			jtpMANTAB.addTab("Count from Vehicle",pnlTRPDL);
			
			add(jtpMANTAB,6,2,12,6.1,this,'L');
		//	cstEMPCT = cl_dat.M_conSPDBA_pbst.prepareCall("{call hr_ephct()}");
			/*DefaultTableCellRenderer LM_CELRND = new DefaultTableCellRenderer();
		    LM_CELRND.setHorizontalAlignment(JLabel.LEFT);
		    tblEMPDL.getColumn(tblEMPDL.getColumnName(TB1_EDEPT)).setCellRenderer(LM_CELRND);
		    tblEMPDL.getColumn(tblEMPDL.getColumnName(TB1_EMPNO)).setCellRenderer(LM_CELRND);*/
			setENBL(false);
			hstDOCTP = new Hashtable<String,String>();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXWBT' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())			
					hstDOCTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}			
		}   
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}   
	} 
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable GUI components.	 
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		{
			tblEMPDL.setEnabled(false);
			tblVSTDL.setEnabled(false);
			tblTRPDL.setEnabled(false);
			tblCNTDL.setEnabled(false);
			txtEMPCT.setEnabled(false);
			txtVSTCT.setEnabled(false);
			txtCNTCT.setEnabled(false);
			txtTOTCT.setEnabled(false);
			txtTRPCT.setEnabled(false);
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
	     super.actionPerformed(L_AE);
		 try
		 {
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				setENBL(true);
				tblEMPDL.clrTABLE();
				tblCNTDL.clrTABLE();
				tblVSTDL.clrTABLE();
				tblTRPDL.clrTABLE();
				inlTBLEDIT(tblEMPDL);
				inlTBLEDIT(tblVSTDL);
				inlTBLEDIT(tblCNTDL);
				inlTBLEDIT(tblTRPDL);
				lblTRPCT.setText("");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
					getDATA();
			}					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}	
	}
	void exePRINT()
	{
		System.out.println("exePRINT");
		tblEMPDL.clrTABLE();
		tblCNTDL.clrTABLE();
		tblVSTDL.clrTABLE();
		tblTRPDL.clrTABLE();
		inlTBLEDIT(tblEMPDL);
		inlTBLEDIT(tblVSTDL);
		inlTBLEDIT(tblCNTDL);
		inlTBLEDIT(tblTRPDL);
		lblTRPCT.setText("");
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			getDATA();
	}	
	/**
	 * Method to fetch data from the Data base & to dispay it in the corresponding tables.
	 */
	private void getDATA()
	{        
		try  
		{    
			setCursor(cl_dat.M_curWTSTS_pbst);
			
			String L_strVSTTP = "";
			int L_ROWNO=0,L_ROWNO2=0,L_ROWNO1=0,L_intVSTCT=0,L_intCNTCT=0,L_intCOUNT=0,L_intTEMP=0;
			String L_strTEMP="",L_strVSTCT1="",L_strEMPNM,L_strFULNM,L_strDPTNM="",L_strPRDPT="";
			
			StringTokenizer L_stnTOKEN;			
			java.sql.Timestamp L_tmpTIME;
			java.sql.Date L_tmpDATE;
			
			M_strSQLQRY ="Select EP_EMPNO,EP_FULNM,EP_DPTNM,EP_PNCTM from HR_EPMST "
				+" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EP_INOST,'')= 'I'"
				+" AND isnull(EP_STSFL,'') <>'X' order by EP_DPTCD,EP_EMPNO";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
				    L_intCOUNT =0;
				    L_strFULNM =nvlSTRVL(M_rstRSSET.getString("EP_FULNM"),"");
				    L_strDPTNM = nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),"");
				 	L_strEMPNM ="";					
				 	L_stnTOKEN = new StringTokenizer(L_strFULNM,"|");
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
	            	}
	                tblEMPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),""),L_ROWNO,TB1_EMPNO);
					tblEMPDL.setValueAt(L_strEMPNM,L_ROWNO,TB1_EMPNM);
					if(!L_strDPTNM.equals(L_strPRDPT))
					{
					    tblEMPDL.setValueAt(L_strDPTNM,L_ROWNO,TB1_EDEPT);
					    tblEMPDL.setCellColor(L_ROWNO,TB1_EDEPT,Color.blue);
					    L_strPRDPT = L_strDPTNM;
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("EP_PNCTM");
					L_strTEMP="";
					if (L_tmpTIME != null)
					{
						L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
						tblEMPDL.setValueAt(L_strTEMP,L_ROWNO,TB1_PNCTM);
					}
					L_intTEMP =L_ROWNO;
					L_ROWNO ++;
				}
				M_rstRSSET.close();
				txtEMPCT.setText(String.valueOf(L_ROWNO));
			}
			
			M_strSQLQRY ="Select * from HR_VSTRN "
//				+"where isnull(VS_STSFL,' ') <> 'X'"
				+"where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,' ') = '6'"
				+" AND VS_VSOTM IS NULL order by VS_VSTTP";		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strVSTTP = nvlSTRVL(M_rstRSSET.getString("VS_VSTTP"),"");
					if(L_strVSTTP.equals("01"))
					{
				 		tblVSTDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO2,TB2_VSTNM);				 		
				 		tblVSTDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""),L_ROWNO2,TB2_PERVS);
						L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
						L_strTEMP="";					
						if (L_tmpTIME != null)
						{
							L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
							tblVSTDL.setValueAt(L_strTEMP,L_ROWNO2,TB2_VSITM);
						}				
						L_intTEMP = Integer.valueOf(nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),"0")).intValue() - Integer.valueOf(nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),"0")).intValue();
						tblVSTDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO2,TB2_VSTCT);						
						L_intVSTCT += L_intTEMP;
						L_ROWNO2 ++;
					}
					else if(L_strVSTTP.equals("02"))
					{
						tblCNTDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO1,TB3_CNTNM);						
						L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
						L_strTEMP="";
						if (L_tmpTIME != null)
						{
							L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
							tblCNTDL.setValueAt(L_strTEMP,L_ROWNO1,TB3_CNITM);
						}						
						L_intTEMP = Integer.valueOf(nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),"0")).intValue() - Integer.valueOf(nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),"0")).intValue();						
						tblCNTDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO1,TB3_CNTCT);
						L_intCNTCT += L_intTEMP;
						L_ROWNO1 ++;
					}
				}
				M_rstRSSET.close();
				txtVSTCT.setText(String.valueOf(L_intVSTCT));
				txtCNTCT.setText(String.valueOf(L_intCNTCT));				
			}
			L_intTEMP=0;
			L_intTEMP=Integer.parseInt(txtEMPCT.getText().toString())+Integer.parseInt(txtVSTCT.getText().toString())+Integer.parseInt(txtCNTCT.getText().toString());
				
			
			//transporter peoples details.
			
			M_strSQLQRY = "Select WB_TPRDS,WB_DOCTP,WB_GINDT,WB_VSICT,WB_VSOCT from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(WB_STSFL,'')<> 'X' AND WB_STSFL <>'9' order by WB_DOCTP,WB_GINDT";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				L_ROWNO = 0;
				L_intCNTCT = 0;
				L_intCOUNT = 0;
				String strMSG = "";
				String L_strDOCDS="";
				String L_strDOCTP="",L_strODOCTP="";;
				while(M_rstRSSET.next())
				{
					L_strDOCTP = nvlSTRVL(M_rstRSSET.getString("WB_DOCTP"),"");
					if(!L_strDOCTP.equals(L_strODOCTP))	
					{						
						if(hstDOCTP.containsKey(L_strDOCTP))
							L_strDOCDS = hstDOCTP.get(L_strDOCTP).toString();
						tblTRPDL.setValueAt(L_strDOCDS,L_ROWNO,TB4_STRTP);												
						if(!L_strODOCTP.equals(""))							
							lblTRPCT.setText(lblTRPCT.getText()+"   "+hstDOCTP.get(L_strODOCTP).toString()+" : "+L_intCOUNT);
						L_strODOCTP = L_strDOCTP;
						L_intCOUNT = 0;
					}
					tblTRPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),""),L_ROWNO,TB4_TPRDS);
					java.sql.Timestamp L_tsmTEMP = M_rstRSSET.getTimestamp("WB_GINDT");
					if(L_tsmTEMP != null)
						tblTRPDL.setValueAt(M_fmtLCDTM.format(L_tsmTEMP),L_ROWNO,TB4_GINDT);
							
					L_intTEMP = M_rstRSSET.getInt("WB_VSICT") - M_rstRSSET.getInt("WB_VSOCT");					
					L_intCOUNT += L_intTEMP;
					L_intCNTCT += L_intTEMP;
					tblTRPDL.setValueAt(String.valueOf(L_intTEMP).toString(),L_ROWNO,TB4_COUNT);
					L_ROWNO++;
				}				   
				M_rstRSSET.close();				
				//lblTRPCT.setText(strMSG);
				lblTRPCT.setText("Head Count form Vehicles "+lblTRPCT.getText()+"  "+hstDOCTP.get(L_strDOCTP).toString()+" : "+L_intCOUNT);
				txtTRPCT.setText(String.valueOf(L_intCNTCT).toString());
				txtTOTCT.setText(String.valueOf(L_intCNTCT + Integer.valueOf(txtEMPCT.getText().trim()).intValue()+Integer.valueOf(txtVSTCT.getText().trim()).intValue()+Integer.valueOf(txtCNTCT.getText().trim()).intValue()).toString());
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
		}
	}
	/** 
	 * Method for stopcell editing of tables 
	 */
	private void inlTBLEDIT(JTable P_tblTBLNM)
	{
		if(!P_tblTBLNM.isEditing())
			return;
		P_tblTBLNM.getCellEditor().stopCellEditing();
		P_tblTBLNM.setRowSelectionInterval(0,0);
		P_tblTBLNM.setColumnSelectionInterval(0,0);
	}
		
}
