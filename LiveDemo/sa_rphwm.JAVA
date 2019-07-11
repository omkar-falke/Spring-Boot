/*
System Name    : System Administration
Program Name   : Hardware master
Program Desc.  : List Of Hardware.
Author         : Mrs.Dipti S Shinde
Date           : 22nd June 05
Version        : v2.0.0
Modificaitons  : 
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
import javax.swing.JTable;
import java.util.Hashtable;
import javax.swing.JComponent;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

//----------------------------------------------------------------
/**<pre>
System Name : Material Management System.
 
Program Name : List of Hardware wise Report       

Purpose : This program will generate a Report for Hardware Details.

List of tables used :
Table Name      Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
HW_LCMST         LC_LOCCD                                              #
HW_MCMST         MC_SRLNO,MCMACNM,MC_LOCCD                             #
HW_PRMST         PR_SRLNO                                              #
-------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name   Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
               All           HW_LCMST                   Location Master                                
               All           HW_MCMST                   Machine Master                                  #
               All           HW_PRMST                   Printer Master
                                
-------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)HW_LCMST
                            2)HW_MCMST
                            3)HW_PRMST
                        
<B>Conditions Give in Query:</b>
    Data is taken from HW_LCMST,HW_MCMST,HW_PRMST for given condition from input box.
    All Table Details Are Taken from Backend on the Screen with dyanamic query.
       
<B>Validations :</B>
    - Two option is over there 1.Full Report,2.Selection wise.
    - First option Full Report user can fetch Full data.
    - Second option of Selection wise user can Create Report as per Section of Database through Table.
</I> */

/*-------------------------------------------------------------------*/
public class sa_rphwm extends cl_rbase
{									    /*** String for generated Report File Name */                   
	private String strFILNM;			/** Integer for counting the records fetched from DB.*/	                     
	private int intRECCT=0;				/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	/**String for ISODOC1 */	                                       		
	private String strISODOC1;			/**String for ISODOC1 */
	private String strISODOC2;			/**String for ISODOC1 */
	private String strISODOC3;			/**Combobox for select Type to generate Report value.*/
	private JComboBox cmbLOCDS;			/**Lable name on Screen */
	private JLabel llbLOCDS;			/**this table for Machine Master */
	private cl_JTable tblMACIN;         /**this table for Printer master */
	private cl_JTable tblPRNTR;         /**this table for Location Master */
	private cl_JTable tblLOCDS;
	private boolean flgFIRST = true;
	private boolean flgFIRST1 = true;   /**This StringBuffer for Creating a dynamic line */
	private StringBuffer stbDOTLN =new StringBuffer();/**Lable for Table */
	private	JLabel lblMACIN;            /**Lable for Table*/
	private JLabel lblPRNTR,lblDATA;            /**Lable for Table*/
	private	JLabel lblLOCDS,lblSYSTP;            /**this for Getting Actual Database(fieldName,Size) from Table */
	private ResultSetMetaData rmdDATA;  /**Check Flag for Table Select option */
	private final int TBL_CHKFL = 0;    /** Value for Table Field option */
	private final int TBL_FIELD = 1;    /** Value for Table Size option */
	private final int TBL_CSIZE = 2;    /**StringBuffer for Creating dynamic Query */
	private StringBuffer stbQUERY;	    /**Vector for a Field Name of all table */
	private Vector<Object> vtrNAME = new Vector<Object>();/**HashTable for MashineMaster,LocationMaster,PrinerMaster */
	private Hashtable<String,String> hstSIZE = new Hashtable<String,String>();/**String for a getting Field Name form ResultSetMetaDAta */
	private String strNAME = "";        /**integer for getting Size of Column */
	private int intSIZE = 0;            /**integer for getting Column Counter */
	private int intCOLCT =0;            /**integer for getting Total Size of ReportAnd String */
	private int intTOTSZ =0;            /**String for a getting Field Name from Vector */
	private String strFIELDN ="";       /**String for a getting Size from Vector */
	private String strFSIZE ="";        /**integer for Counting a Row */
	private int intROCNT =0;            /**StirnTokenizer for Getting one By one Header String*/
	private StringTokenizer sttDATA;    /**StringBuffer for getting Total Header String and Padstring Size */
	private StringBuffer stbHEAD = new StringBuffer();  /**String Array for Counting Total Width*/
	private String arrCOLWD[];
	private JTextField txtDATA,txtSYSTP;         /**TextField for Location Details*/
	private String strTOTSZ ="";        /**String for Total size of string*/
	private int intTOTSIZ =0;           /**Integer Variable for total Size in vaildation */
	private String strLOCDS = "";   
	private JRadioButton rdbALL,rdbLOC,rdbPER,rdbMAC;
	private ButtonGroup btgSET;
	private int cnt;/**String for Dynamic Location details in Header */
	sa_rphwm()
	{      
	    super(2);
	    try 
		{	
			setCursor(cl_dat.M_curWTSTS_pbst);
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);

			add(lblMACIN = new JLabel("Machine Details"),5,2,1,2,this,'L'); 
			String[] L_strCOLHD = {"Select","FieldName","Size"};
      		int[] L_intCOLSZ = {50,150,60};	    				
			tblMACIN = crtTBLPNL1(this,L_strCOLHD,50,5,2,14,3,L_intCOLSZ,new int[]{0});
			
			add(lblPRNTR = new JLabel("Peripheral Details"),5,5,1,2,this,'L');

			int[] L_intCOLSZ1 = {50,150,60};
			tblPRNTR = crtTBLPNL1(this,L_strCOLHD,50,5,5,5,3,L_intCOLSZ1,new int[]{0});
			
			add(lblLOCDS = new JLabel("Location Details"),11,5,1,2,this,'L');
			//String[] L_strCOLHD2 = {"Select","FieldName","Size"};
      		int[] L_intCOLSZ2 = {50,150,60};	    				
			tblLOCDS = crtTBLPNL1(this,L_strCOLHD,50,11,5,8,3,L_intCOLSZ2,new int[]{0});
			
			stbDOTLN = new StringBuffer("--");
			
			
			add(llbLOCDS = new JLabel("option"),2,3,1,1,this,'L');
			add(cmbLOCDS = new JComboBox(),2,4,1,1,this,'L');
			
			btgSET = new ButtonGroup();
			
			add(rdbALL = new JRadioButton ( "ALL Tables",false),3,2,1,1,this,'R');
			add(rdbMAC = new JRadioButton ( "Machine",false),3,3,1,1,this,'R');
			add(rdbPER = new JRadioButton ( "peripheral",false),3,4,1,1,this,'R');
			add(rdbLOC = new JRadioButton ( "Location",true),3,5,1,1,this,'R');
					//btgOPTON.add(rdbMAC);	 
			btgSET.add(rdbALL);
			btgSET.add(rdbLOC);
			btgSET.add(rdbPER);
			btgSET.add(rdbMAC);
			add(lblDATA = new JLabel("Speci Loc"),4,2,1,1,this,'L');
			add(txtDATA =new JTextField(),4,3,1,1,this,'L');
			add(lblSYSTP = new JLabel("System type"),2,1,1,1,this,'L');
			add(txtSYSTP =new JTextField(),2,2,1,1,this,'L');
			cmbLOCDS.addItem("Selection Wise");
			cmbLOCDS.addItem("Full Report");
			stbQUERY= new StringBuffer();
			txtDATA.addKeyListener(this);
			txtSYSTP.addKeyListener(this);
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
			setCursor(cl_dat.M_curDFSTS_pbst);			
		}   
	    catch(Exception L_EX)
		{   
			setMSG(L_EX+" GUI Designing",'E');
		}	
    }
	/**     
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		{
				
			tblMACIN.cmpEDITR[TBL_FIELD].setEnabled(false);
			tblPRNTR.cmpEDITR[TBL_FIELD].setEnabled(false);
			tblLOCDS.cmpEDITR[TBL_FIELD].setEnabled(false);
			tblMACIN.cmpEDITR[TBL_CSIZE].setEnabled(false);
			tblPRNTR.cmpEDITR[TBL_CSIZE].setEnabled(false);
			tblLOCDS.cmpEDITR[TBL_CSIZE].setEnabled(false);
		}
	}
	/** Method for action performing.. 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{ 
		super.actionPerformed(L_AE);
		try{
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			{
				setMSG("Select an Option..",'E');
				//clrCOMP();
				cl_dat.M_cmbOPTN_pbst.requestFocus();
			}			
			else
			{				
				//if((txtSYSTP.getText().trim()).length()==0)						
				//	clrCOMP();				
				setENBL(true);
				txtSYSTP.requestFocus();
				setMSG("Enter System Type & press Enter Key OR Press F1 to select from List..",'N');
			}
		}
		if(M_objSOURC==txtSYSTP)
		{
			if(txtSYSTP.getText().trim().length()==0)
			{
				setMSG("System type is complasary field",'E');
				//return;//break;
			}
			else
			{
				cmbLOCDS.requestFocus(); 
			}
		}
		
	 	if(rdbALL.isSelected())
		{		
			tblMACIN.setVisible(true); 
			tblPRNTR.setVisible(true);
			tblLOCDS.setVisible(true);
			lblMACIN.setVisible(true);
			lblPRNTR.setVisible(true);
			lblLOCDS.setVisible(true);
			txtDATA.setEnabled(true);
			txtDATA.setVisible(true);
			lblDATA.setVisible(true);
			tblMACIN.setEnabled(true);
			tblPRNTR.setEnabled(true);	
			tblLOCDS.setEnabled(true);
	
			int L_intROWCT = 0;
				
		/*STAT1 = cl_dat.M_conSPDBA_pbst.createStatement(
                          ResultSet.TYPE_SCROLL_INSENSITIVE,
                          ResultSet.CONCUR_UPDATABLE);*/
			M_rstRSSET = cl_dat.exeSQLQRY("select * from HW_MCMST");
			rmdDATA = M_rstRSSET.getMetaData();	
			intCOLCT = 0;
			intCOLCT = rmdDATA.getColumnCount();
			intSIZE = 0;
			strNAME = "";	
						
			if(tblMACIN.isEditing())//for clear the editing tabel.
				tblMACIN.getCellEditor().stopCellEditing();
			tblMACIN.setRowSelectionInterval(0,0);
			tblMACIN.setColumnSelectionInterval(0,0);
			if(tblPRNTR.isEditing())
				tblPRNTR.getCellEditor().stopCellEditing();
			tblPRNTR.setRowSelectionInterval(0,0);
			tblPRNTR.setColumnSelectionInterval(0,0);
			if(tblLOCDS.isEditing())
				tblLOCDS.getCellEditor().stopCellEditing();
			tblLOCDS.setRowSelectionInterval(0,0);
			tblLOCDS.setColumnSelectionInterval(0,0);
					
			for(int i=1;i<=intCOLCT;i++)
			{				
				strNAME = rmdDATA.getColumnName(i);
				intSIZE = rmdDATA.getColumnDisplaySize(i);	
				tblMACIN.setValueAt(strNAME,i-1,TBL_FIELD);
				tblMACIN.setValueAt(String.valueOf(intSIZE).toString(),i-1,TBL_CSIZE);
				hstSIZE.put(strNAME,String.valueOf(intSIZE));
			}
			M_rstRSSET.close();
						
			M_rstRSSET = cl_dat.exeSQLQRY("select * from HW_PRMST");
			rmdDATA = M_rstRSSET.getMetaData();
			intCOLCT = 0;
			intCOLCT = rmdDATA.getColumnCount();
			intSIZE = 0;
			strNAME = "";
						
			if(tblMACIN.isEditing())
				tblMACIN.getCellEditor().stopCellEditing();
			tblMACIN.setRowSelectionInterval(0,0);
			tblMACIN.setColumnSelectionInterval(0,0);
			if(tblPRNTR.isEditing())
				tblPRNTR.getCellEditor().stopCellEditing();
			tblPRNTR.setRowSelectionInterval(0,0);
			tblPRNTR.setColumnSelectionInterval(0,0);
			if(tblLOCDS.isEditing())
				tblLOCDS.getCellEditor().stopCellEditing();
			tblLOCDS.setRowSelectionInterval(0,0);
			tblLOCDS.setColumnSelectionInterval(0,0);
								
			for( int j=1;j<=intCOLCT;j++)
			{				
				strNAME = rmdDATA.getColumnName(j);
				intSIZE = rmdDATA.getColumnDisplaySize(j);				
				tblPRNTR.setValueAt(strNAME,j-1,TBL_FIELD);
				tblPRNTR.setValueAt(String.valueOf(intSIZE).toString(),j-1,TBL_CSIZE);
				hstSIZE.put(strNAME,String.valueOf(intSIZE));
							
			}
			M_rstRSSET.close();
						
			M_rstRSSET = cl_dat.exeSQLQRY("select * from HW_LCMST");
			rmdDATA = M_rstRSSET.getMetaData();
			intCOLCT = 0;
			intCOLCT = rmdDATA.getColumnCount();
			strNAME = "";	
						
			if(tblMACIN.isEditing())
				tblMACIN.getCellEditor().stopCellEditing();
			tblMACIN.setRowSelectionInterval(0,0);
			tblMACIN.setColumnSelectionInterval(0,0);
			if(tblPRNTR.isEditing())
				tblPRNTR.getCellEditor().stopCellEditing();
			tblPRNTR.setRowSelectionInterval(0,0);
			tblPRNTR.setColumnSelectionInterval(0,0);
			if(tblLOCDS.isEditing())
				tblLOCDS.getCellEditor().stopCellEditing();
			tblLOCDS.setRowSelectionInterval(0,0);
			tblLOCDS.setColumnSelectionInterval(0,0);
						
			for( int k=1;k<=intCOLCT;k++)
			{				
				strNAME = rmdDATA.getColumnName(k);
				intSIZE = rmdDATA.getColumnDisplaySize(k);				
				tblLOCDS.setValueAt(strNAME,k-1,TBL_FIELD);
				tblLOCDS.setValueAt(String.valueOf(intSIZE).toString(),k-1,TBL_CSIZE);
				hstSIZE.put(strNAME,String.valueOf(intSIZE));
							
			}
			M_rstRSSET.close();											
				
		}	
		
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;			
			vtrNAME.removeAllElements();
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			cmbLOCDS.requestFocus();
			setMSG("please select the option from table and for location F1 press",'N');
		}
		if(cmbLOCDS.getSelectedItem().equals("Full Report"))
		 {
			tblMACIN.setEnabled(false);
			tblPRNTR.setEnabled(false);	
			tblLOCDS.setEnabled(false);
			setMSG("Press Display for Full Report",'N');
			tblMACIN.setVisible(false); 
			tblPRNTR.setVisible(false);
			tblLOCDS.setVisible(false);
			lblMACIN.setVisible(false);
			lblPRNTR.setVisible(false);
			lblLOCDS.setVisible(false);
			txtDATA.setVisible(false);
			lblDATA.setVisible(false);
			cl_dat.M_btnSAVE_pbst.requestFocus();
	     
		}
		if(cmbLOCDS.getSelectedItem().equals("Selection Wise"))
		{
			rdbALL.requestFocus();
		}
		if(rdbPER.isSelected())
		{
			tblMACIN.setVisible(false); 
			tblLOCDS.setVisible(false); 
			tblPRNTR.setVisible(true); 	
			txtDATA.setVisible(false);
			lblDATA.setVisible(false);
		}
		if(rdbLOC.isSelected())
		{
			tblMACIN.setVisible(false); 
			tblPRNTR.setVisible(false); 
			tblLOCDS.setVisible(true); 
			txtDATA.setVisible(false);
			lblDATA.setVisible(false);
		}
		if(rdbMAC.isSelected())
		{
			tblPRNTR.setVisible(false); 
			tblLOCDS.setVisible(false); 
			tblMACIN.setVisible(true); 	
			txtDATA.setVisible(false);
			lblDATA.setVisible(false);
		}
		setMSG("Please Select a option through Table",'N');
		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX ," Action performed..");    	
			setCursor(cl_dat.M_curDFSTS_pbst);
		}    	    
		this.setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
			txtDATA.setText(txtDATA.getText().trim().toUpperCase());
			setCursor(cl_dat.M_curWTSTS_pbst);
    	    try
    	  	{				
				//if(cmbLOCDS.getSelectedItem().equals("Selection Wise"))
				if(M_objSOURC== txtSYSTP)
				{
				M_strHLPFLD = "txtSYSTP";
				M_strSQLQRY=" SELECT substring(CMT_CODCD,3,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP = 'COXXSBS' and CMT_CODCD like 'SA%' ";
				if(txtSYSTP.getText().length() >0)
				{
					M_strSQLQRY += " and substring(CMT_CODCD,3,2) like '"+txtSYSTP.getText().trim()+"%'";
				}
				cl_hlp(M_strSQLQRY,1,1,new String[]{"System code","description"},2,"CT");
				System.out.println("dept "+M_strSQLQRY);
				setCursor(cl_dat.M_curDFSTS_pbst);
				}
		
				if(M_objSOURC==txtDATA)
				{
					M_strHLPFLD = "txtDATA";	
					M_strSQLQRY=" SELECT  LC_LOCCD,LC_LOCDS from HW_LCMST where ifnull(LC_STSFL,' ') <> 'X' order by LC_LOCCD";
					if(txtDATA.getText().trim().length()>0)
						M_strSQLQRY += "where LC_LOCCD like '"+ txtDATA.getText().trim()+"%'"; 
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Location","desc"},2,"CT");										
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
    		}
	   	    catch(Exception L_EX)
    	    {
    		    setMSG(L_EX ," F1 help....");   
				System.out.println("fffffff   ");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}    	    
 	    }
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC==txtDATA)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			if(cmbLOCDS.getSelectedItem().equals("Selection Wise"))
			{
				txtDATA.requestFocus();
				setMSG("please select the options.",'N');
			}
		}
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtSYSTP")
         {
			txtSYSTP.setText(cl_dat.M_strHLPSTR_pbst);	
			txtSYSTP.requestFocus();
		 }
	 
		if(M_strHLPFLD == "txtDATA")
		{
			txtDATA.setText(cl_dat.M_strHLPSTR_pbst);							
			if (txtDATA.getText().length()<=8)		
			  cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
		setCursor(cl_dat.M_curDFSTS_pbst);	
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
				strFILNM = cl_dat.M_strREPSTR_pbst +"sa_rphwm.html";				
			else if(M_rdbTEXT.isSelected())
			strFILNM = cl_dat.M_strREPSTR_pbst + "sa_rphwm.doc";
			
			if(tblMACIN.isEditing())
				tblMACIN.getCellEditor().stopCellEditing();
			tblMACIN.setRowSelectionInterval(0,0);
			tblMACIN.setColumnSelectionInterval(0,0);
			if(tblPRNTR.isEditing())
				tblPRNTR.getCellEditor().stopCellEditing();
			tblPRNTR.setRowSelectionInterval(0,0);
			tblPRNTR.setColumnSelectionInterval(0,0);
			if(tblLOCDS.isEditing())
				tblLOCDS.getCellEditor().stopCellEditing();
			tblLOCDS.setRowSelectionInterval(0,0);
			tblLOCDS.setColumnSelectionInterval(0,0);
			
				getDATA();
				//if(intRECCT ==0)
				//{
				//	setMSG("Data not found ..",'E');
					//return;
				//}
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Hardware wise "," ");
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
		String L_strSRLNO="",L_strMACNM="",L_strMDLNO="",L_strCPUDS="",L_strRAMDS="",L_strHDDDS="",L_strPRNDS="",L_strPRSRL="",L_strIPADD="",L_strPORNO="",L_strPRLCCD="",L_strLOCCD="",L_strPRLOC="",L_strPRSRLNO="",L_strEXPDT="";			
		int L_intHWCNT=1;	
		int L_intFINAL=0;
		intRECCT = 0;	
		try
		{
			//MC_SYSTP = '"+txtMAINL.getText().trim()+"'
			if(stbQUERY.length() >0)
			stbQUERY.delete(0,stbQUERY.length());
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> List of Hardware Installation </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				 
			}						
			prnHEADER();
			System.out.println("total size for get"+intTOTSZ);	
			
			if(cmbLOCDS.getSelectedItem().equals("Selection Wise"))
			{
					String L_strDATA ="";
					String L_strTEMP ="";
					if(rdbPER.isSelected())
					{
						M_strSQLQRY = "select "+ stbQUERY.toString()+" ";
						M_strSQLQRY += " from HW_PRMST where ifnull(PR_STSFL,' ') <> 'X' and MC_SYSTP = '"+txtSYSTP.getText().trim()+"'";
					}
					if(rdbLOC.isSelected())
					{
						M_strSQLQRY = "select "+ stbQUERY.toString()+" ";
						M_strSQLQRY += " from HW_LCMST where ifnull(LC_STSFL,' ') <> 'X' and MC_SYSTP = '"+txtSYSTP.getText().trim()+"'";
					}
					if(rdbMAC.isSelected())
					{
						M_strSQLQRY = "select "+ stbQUERY.toString()+" ";
						M_strSQLQRY += " from HW_MCMST where ifnull(MC_STSFL,' ') <> 'X' and MC_SYSTP = '"+txtSYSTP.getText().trim()+"'";
					}
					if(rdbALL.isSelected()) 
					{
						M_strSQLQRY = "select "+ stbQUERY.toString()+" ";
						M_strSQLQRY += "from HW_LCMST,HW_MCMST left outer join HW_PRMST on PR_SYSTP=LC_SYSTP and PR_LOCCD=LC_LOCCD  and ifnull(PR_STSFL,' ') <> 'X'  WHERE  MC_SYSTP=LC_SYSTP and MC_LOCCD = LC_LOCCD and ifnull(LC_STSFL,' ') <> 'X' and ";
						M_strSQLQRY += "ifnull(MC_STSFL,' ') <> 'X' and MC_SYSTP = '"+txtSYSTP.getText().trim()+"'";
	
						M_strSQLQRY +=" order by MC_LOCCD";
						if(txtDATA.getText().trim().length()>0)
						{		
							M_strSQLQRY += " and LC_LOCCD ='"+txtDATA.getText().trim()+"'";
						}
					}
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
					System.out.println(" dynamic query "+M_strSQLQRY);
					
				if(M_rstRSSET!=null)
				{
						cnt=0;
					while(M_rstRSSET.next())
					{
							sttDATA = new StringTokenizer(stbQUERY.toString(),",");
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							
							while(sttDATA.hasMoreTokens())
							{
								L_strTEMP =sttDATA.nextToken();	
								L_strDATA =nvlSTRVL(M_rstRSSET.getString(L_strTEMP)," ");
								dosREPORT.writeBytes(padSTRING('R',L_strDATA, Integer.parseInt((String)hstSIZE.get(L_strTEMP))));
							}
								cnt++;
								cl_dat.M_intLINNO_pbst += 1;
						if(cl_dat.M_intLINNO_pbst> 63)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(stbDOTLN.toString());
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))						
							{
								prnFMTCHR(dosREPORT,M_strEJT);
							}
							prnHEADER();
						}
					}
					M_rstRSSET.close();
					
					dosREPORT.writeBytes("\n\n");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('R',"Total "+cnt,intTOTSZ));
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					cl_dat.M_intLINNO_pbst +=2;
				}
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst +=1;
					
				dosREPORT.writeBytes(stbDOTLN.toString());
				setMSG("Report generation is Completed",'N');
			}
			if(cmbLOCDS.getSelectedItem().equals("Full Report"))
			{				
				M_strSQLQRY = "select MC_SRLNO,MC_MACNM,LC_IPADD,MC_MDLNO,MC_CPUDS,MC_RAMDS,MC_HDDDS,PR_PRFDS,PR_SRLNO,MC_LOCCD,MC_EXPDT";
				M_strSQLQRY += " from HW_LCMST,HW_MCMST left outer join HW_PRMST on PR_SYSTP=LC_SYSTP and PR_LOCCD=LC_LOCCD  and ifnull(PR_STSFL,' ') <> 'X' WHERE MC_SYSTP=LC_SYSTP and MC_LOCCD = LC_LOCCD and ifnull(LC_STSFL,' ') <> 'X' and ";
				M_strSQLQRY += " ifnull(MC_STSFL,' ') <> 'X' and MC_SYSTP = '"+txtSYSTP.getText().trim()+"'";
	
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				System.out.println("main query "+ M_strSQLQRY);	
				if(M_rstRSSET !=null)
				{	
					L_strPRLCCD ="";
					L_strPRSRLNO= "";				
					while(M_rstRSSET.next())
					{					
						intRECCT = 1;
						L_strSRLNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_SRLNO")," "),22);
						L_strMACNM = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_MACNM")," "),18);
						L_strIPADD = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("LC_IPADD")," "),12);
						L_strMDLNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_MDLNO")," "),20);
						L_strCPUDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_CPUDS")," "),17);
						L_strRAMDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_RAMDS")," "),8);
						L_strHDDDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_HDDDS")," "),11);
						L_strPRNDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PR_PRFDS")," "),22);
						L_strPRSRL = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PR_SRLNO")," "),12);
						L_strLOCCD = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_LOCCD"),"-"),10);
						L_strEXPDT = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MC_EXPDT")," "),4);
				  
						/*if(L_strLOCCD.length() >0)
							L_strLOCCD = L_strLOCCD.substring(0,2);
						if(!L_strLOCCD.equals(L_strPRLOC))
						{
							if(L_intHWCNT >1)
							{
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
									dosREPORT.writeBytes(padSTRING('R',"Total " ,7)+L_intHWCNT +"\n");
									dosREPORT.writeBytes("\n");
									cl_dat.M_intLINNO_pbst += 2;
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								L_intFINAL=L_intHWCNT;
							}									
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							if(L_strLOCCD.equals("HO"))
							{					 
								if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<B>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes("Head Office");
								if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("</B>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strNOBOLD);					
							}
							else if(L_strLOCCD.equals("WR"))
							{					 
								if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<B>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes("Works");
								if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("</B>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strNOBOLD);					
							}
							L_strPRLOC =L_strLOCCD;						
							dosREPORT.writeBytes("\n\n"); 
							cl_dat.M_intLINNO_pbst +=2;
							L_intHWCNT =1;
						}
						else
						{
							L_intHWCNT++;
						}*/
						dosREPORT.writeBytes(L_strLOCCD+L_strSRLNO+L_strMACNM+L_strIPADD+L_strMDLNO+L_strCPUDS+L_strRAMDS+L_strHDDDS+L_strPRNDS+L_strPRSRL+L_strEXPDT);
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst +=1;					
						if(cl_dat.M_intLINNO_pbst> 63)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n");
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))						
							{
								prnFMTCHR(dosREPORT,M_strEJT);
							}							
							prnHEADER();
						}
					}
				/*	if(L_intHWCNT >1)
					{
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(padSTRING('R',"Total " ,7)+L_intHWCNT +"\n");
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 2;
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						L_intFINAL=L_intFINAL+L_intHWCNT;
							dosREPORT.writeBytes("Final total    "+L_intFINAL);
							dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						L_intFINAL=0;
						intRECCT++;
					}*/
					M_rstRSSET.close();
					}
					dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------- ");
					setMSG("Report completed.. ",'N');
					}
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
				setMSG(L_EX,"getDATA");
			}
		}
	/**
	 * Method to validate  DATA before execution, Check for blank and wrong Input. 
	*/
	boolean vldDATA()
	{
		try
		{	int x=0,y=0,z=0;
			strISODOC1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","COXXLIC","DOC1");
			strISODOC2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","COXXLIC","DOC2");
			strISODOC3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","COXXLIC","DOC3");
				
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(!cmbLOCDS.getSelectedItem().equals("Full Report"))
			{					
				if(txtDATA.getText().length() ==0)
				{
					setMSG("If you want a report location wise then enter data with help(F1 key)and select the option of Table",'N');
					
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
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(tblMACIN.isEditing())
					tblMACIN.getCellEditor().stopCellEditing();
					tblMACIN.setRowSelectionInterval(0,0);
					tblMACIN.setColumnSelectionInterval(0,0);
				if(tblPRNTR.isEditing())
					tblPRNTR.getCellEditor().stopCellEditing();
					tblPRNTR.setRowSelectionInterval(0,0);
					tblPRNTR.setColumnSelectionInterval(0,0);
				if(tblLOCDS.isEditing())
					tblLOCDS.getCellEditor().stopCellEditing();
					tblLOCDS.setRowSelectionInterval(0,0);
					tblLOCDS.setColumnSelectionInterval(0,0);
			}
			//if(rdbALL.isSelected())
			if(cmbLOCDS.getSelectedItem().equals("Selection Wise"))
			{		
				strTOTSZ="";
				int i=0;
				intROCNT=tblMACIN.getRowCount();
				for(i=0;i<intROCNT;i++)
				{
					if(tblMACIN.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						strTOTSZ=tblMACIN.getValueAt(i,TBL_CSIZE).toString().trim();
						 x+=Integer.parseInt(strTOTSZ);
					}
				}
				intROCNT=tblPRNTR.getRowCount();
				for( i=0;i<intROCNT;i++)
				{
					if(tblPRNTR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						strTOTSZ=tblPRNTR.getValueAt(i,TBL_CSIZE).toString().trim();							
						y+=Integer.parseInt(strTOTSZ);
				
					}
				}
				intROCNT=tblLOCDS.getRowCount();
				for( i=0;i<intROCNT;i++)
				{
					if(tblLOCDS.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						strTOTSZ=tblLOCDS.getValueAt(i,TBL_CSIZE).toString().trim();						
						z+=Integer.parseInt (strTOTSZ);
					}
				}
				intTOTSIZ =x+y+z;
				intTOTSZ =intTOTSIZ;
				if(intTOTSZ>230)
				{
					setMSG("please select in limit<230",'E');
					return false;
				}
			}
			if(txtSYSTP.getText().trim().length()==0)
			{
				setMSG("System type is complasary field",'E');
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
			
			if(cmbLOCDS.getSelectedItem().equals("Full Report"))
			{
				dosREPORT.writeBytes("\n" + padSTRING('L',"-------------------------",163));
				dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC1,163));
				dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC2,163));
				dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC3,163));
				dosREPORT.writeBytes("\n" + padSTRING('L',"-------------------------",163) + "\n");
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,139));
				dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
				dosREPORT.writeBytes(padSTRING('R',"List of Hardware Installations ",139));
				dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");			
				dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");											
				dosREPORT.writeBytes("Location  Machine Serial        Machine Name      IP Address  Model NO.           CPU              RAM     Hard Disc  Printer               Printer Srl   EXPdate  \n");
				dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"); 
				cl_dat.M_intLINNO_pbst = 11;
				
			}
			//if(rdbALL.isSelected())
			if(cmbLOCDS.getSelectedItem().equals("Selection Wise"))
			{		
				if(!cmbLOCDS.getSelectedItem().equals("Full Report"))
				{
				int j = 0;
				arrCOLWD = new String[intROCNT];
				int i= 0;
				int z = 0;
				strFIELDN ="";
				strFSIZE ="";
				intROCNT=0;
				intROCNT=tblMACIN.getRowCount();
				
				if(cl_dat.M_PAGENO ==1)
				{
					intTOTSZ =0;
					if(stbHEAD.length() >0)
					stbHEAD.delete(0,stbHEAD.length());
					for(i=0;i<intROCNT;i++)
					{ 
						if(tblMACIN.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(j != 0)
							stbQUERY.append(",");
							stbQUERY.append(tblMACIN.getValueAt(i,TBL_FIELD).toString().trim());						
							j++;
							vtrNAME.addElement(tblMACIN.getValueAt(i,TBL_FIELD));
						}
					}
					intROCNT=tblPRNTR.getRowCount();
					for(i=0;i<intROCNT;i++)
					{
						if(tblPRNTR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(j != 0)
							stbQUERY.append(",");
							stbQUERY.append(tblPRNTR.getValueAt(i,TBL_FIELD).toString().trim());						
							j++;
							vtrNAME.addElement(tblPRNTR.getValueAt(i,TBL_FIELD));
						}
					}
					intROCNT=tblLOCDS.getRowCount();
					for(i=0;i<intROCNT;i++)
					{
						if(tblLOCDS.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(j != 0)
							stbQUERY.append(",");
							stbQUERY.append(tblLOCDS.getValueAt(i,TBL_FIELD).toString().trim());						
							j++;							
							vtrNAME.addElement(tblLOCDS.getValueAt(i,TBL_FIELD));
						}
					}
					for( z=0;z<vtrNAME.size();z++)
					{
						strFIELDN = vtrNAME.elementAt(z).toString().trim();
						if(hstSIZE.containsKey((String)strFIELDN))
						{
						  strFSIZE =(String)hstSIZE.get(strFIELDN);
						  intTOTSZ+= Integer.parseInt(strFSIZE);
						  //System.out.println("Field name :"+strFIELDN+" size :"+strFSIZE );
						  stbHEAD.append(padSTRING('R',strFIELDN,Integer.parseInt(strFSIZE)));
						  }
					}
					stbDOTLN.delete(0,stbDOTLN.length());
					if(intTOTSZ<80)
					{
						intTOTSZ = 100;
					}
					for(int L_intDOTCT =0;L_intDOTCT<intTOTSZ;L_intDOTCT++)
					{
						stbDOTLN.append("-");					
					}					
				}
				if(intTOTSZ<80)
					intTOTSZ = 100;
				dosREPORT.writeBytes("\n" + padSTRING('L',"-------------------------",+intTOTSZ));
				dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC1,+intTOTSZ));
				dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC2,+intTOTSZ));
				dosREPORT.writeBytes("\n" + padSTRING('L',strISODOC3,+intTOTSZ));
				dosREPORT.writeBytes("\n" + padSTRING('L',"-------------------------",+intTOTSZ)+"\n");
				dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intTOTSZ-24));
				dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");	
				if(txtDATA.getText().length()>0)
				{
					if(txtDATA.getText().equals("WR"))
					{
						strLOCDS ="Works";
						if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(padSTRING('R',"List of Hardware Installations for "+strLOCDS,intTOTSZ-24));
						if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					}
					if(txtDATA.getText().equals("HO"))
					{
						strLOCDS ="Head Office";
						if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(padSTRING('R',"List of Hardware Installations for "+strLOCDS,intTOTSZ-24));
						if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					}
				}
				else if(txtDATA.getText().length()==0)
				dosREPORT.writeBytes(padSTRING('R',"List of Hardware Installations ",intTOTSZ-24));
				dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
				dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
				dosREPORT.writeBytes(stbHEAD.toString()+"\n");
				dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
				cl_dat.M_intLINNO_pbst = 11;	
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}	
}