/*
System Name   : Management Information System
Program Name  : Gradewise Product Test Analysis Query.
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 22 July 2005
Version       : LIMS v2.0.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.MouseEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.JComponent;import javax.swing.InputVerifier;
import javax.swing.JTable.*;
import java.awt.Color;import java.awt.Font;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.util.Hashtable;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Gradewise Product Test Analysis Query.

Purpose : Query Screen to view & generate the gradewise Product Test Report.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
CO_PRMST 
QC_PSMST       PS_QCATP,PS_TSTTP,PS_LOTNO,PS_RCLNO,PS_TSTNO,PSTSTDT    #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFRDAT     LT_PSTDT      PR_LTMST      Timestamp     Production Start Date & Time
txtTODAT     LT_PSTDT      PR_LTMST      Timestamp     Production Start Date & Time
txtPRDCD     LT_TPRCD      PR_LTMST      VARCHAR(12)   Production Code
cmbPRDDS     PR_PRDDS      CO_PRMST      VARCHAR(20)   Product Description
--------------------------------------------------------------------------------------
<B>
Logic</B> Data is taken from PR_LTMST and QC_PSMST for condiations
    1) PS_QCATP = Given QCA type
    2) AND PS_TSTTP = Given test Type
    3) AND PS_STSFL status Flag not equal to 'X' 
    4) AND PS_RCLNO = given reclassification number.
    5) AND PS_LOTNO = LT_LOTNO AND PS_RCLNO = LT_RCLNO
    6) AND LT_RUNNO = given Run Number.
    7) AND LT_TPRCD = given Transporter Code.
    8) AND LT_PSTDT BETWEEN given Date Range.

<B>Validations :</B>
    - To Date must be greater than From date & less than current Date.
    - Line number must be valid.
</I> */
public class qc_qrgpa extends cl_rbase
{									/** JTextField to enter & display Product Code.*/  
	private JTextField txtPRDCD;	/** JTextField to enter & display Product Description.*/
	private JTextField txtPRDDS;	/** JTextField to enter & display From Date.*/
	private JTextField txtFRDAT;	/** JTextField to enter & display To Date.*/
	private JTextField txtTODAT;	/** JComboBox to select & display Product Type.*/
	private JComboBox cmbPRDTP;		/** JTable to display Run Details.*/
	private cl_JTable tblRUNDL;		/** JTable to display Lot Details.*/
	private cl_JTable tblLOTDL;   	/** JLabel to display Lot Count.*/
	private JLabel lblLOTCT;		/** JLabel to display Baged Quantity.*/
	private JLabel lblBAGQT;		/** JLabel to display Different Messages.*/
	private JLabel lbl1,lbl2,lbl3,lbl4,lbl5; 
   								/**	String variable for File Name.*/
	private String strFILNM;	/**	Integer variable for Row Count.*/
	private int intROWCT;		/**	Integer variable for Row Width.*/
	private int intROWWD;		/**	String variable for Product Code.*/
	private String strPRDCD;	/**	String variable for old Product Code.*/
	private String strOPRDCD="";/**	String variable for Run Number.*/
	private String strRUNNO;	/**	String variable for old Run.*/
	private String strORUNNO="";	/**	String variable for QCA Type.*/
	private String strQCATP;		/**	String variable for Product Type.*/
	private String strPRDTP;	
									/**	StringBuffer object to generate the Dotted line Dynamically.*/
	private StringBuffer stbDOTLN;	/**	StringBuffer object to generate the Header Dynamically.*/
	private StringBuffer stbHEADR;                        
									/** Array of String for Quality Parameter Code.*/
	private String[] arrQPRCD;      /** Array of Double for average value of Quality Parameters.*/
	private double[] arrAVRGE;		/** Array of integer to specify the possion of decimal point.*/
	private int[] arrCLDEC;			/** Double variable to count Bagged Quantity.*/
	private double dblBAGQT =0;		/** Integer variable for Quality Parameter Count.*/
	private int intQPRCT =11;		/** Integer variable for Column Width.*/
	private int intCOLWD;			/** String variable for initial Reclassification Number.*/
	private String intINTRCL ="00"; /** String variable for Transporter Code.*/
	private String strTPRCD;		/** String variable for Product Description.*/
	private String strPRDDS = "";   /** String variable for composite certification Test.*/
    private String strTSTTP = "0103";/** String Variable for Classification Flag.*/
	private String strCLSFL;		/** String Variable for Line Number.*/
	private String strLINNO;		/** String Variable for Lot Number.*/
	private String strLOTNO;		/** String Variable for Lot Date.*/
	private String strLOTDT;		/** String Variable for From Date */
	private String strFMDAT;		/** String Variable for To Date.*/
	private String strTODAT;		/** Integer Variable for Details Count.*/
	private int intDTLCNT=0;		/** Integer variable for Previous Detail count.*/	  	
	private int intPRDTCT =0;		
									/** Final Integer variable to represent Check Flag Column for Run Details Table.*/
	final int TB1_CHKFL =0;			/** Final Integer variable to represent Run Number Columnfor Run Details Table.*/
	final int TB1_RUNNO =1;			/** Final Integer variable to represent Line Number Columnfor Run Details Table.*/
	final int TB1_LINNO =2;			/** Final Integer variable to represent Check Flag Column of Lot Details Column.*/
	final int TB2_CHKFL =0;			/** Final Integer variable to represent Lot Number Column of Lot Details Column.*/
	final int TB2_LOTNO =1;			/** Final Integer variable to represent Lot Date Column of Lot Details Column.*/
	final int TB2_LOTDT =2;			/** Final Integer variable to represent Grade Column of Lot Details Column.*/
	final int TB2_GRADE =3;			/** Final Integer variable to represent Classification Flag Column of Lot Details Column.*/
	final int TB2_CLSFL =4;			
											/** FileOutputStream object to generate the Report File form Data Stream.*/
	private FileOutputStream fosREPORT;		/** DataOutputStream object to generate Data Stream for the Report.*/
	private DataOutputStream dosREPORT;		/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();	/** Table Input Verifier to manage the data fetchingfor selected Lot Number.*/
	private TableInputVerifier TBLINPVF;
	private TBLINPVF objTBLVRF;
	private Hashtable<String,String> hstSHRDS,hstUOMCD;
	qc_qrgpa()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Product Type"),2,1,1,.9,this,'R');
			add(cmbPRDTP = new JComboBox(),2,2,1,1.5,this,'L');
			add(new JLabel("From Date"),2,4,1,.8,this,'R');
			add(txtFRDAT = new TxtDate(),2,5,1,1,this,'L');			
			add(new JLabel("To Date"),2,6,1,.6,this,'R');
			add(txtTODAT = new TxtDate(),2,7,1,1,this,'L');
			
			add(new JLabel("Product Code"),3,1,1,.9,this,'R');
			add(txtPRDCD = new JTextField(),3,2,1,1.5,this,'L');
			add(new JLabel("Description"),3,4,1,.8,this,'R');
			add(txtPRDDS = new JTextField(),3,5,1,3,this,'L');	
									
			add(new JLabel("Run Details"),4,1,1,.9,this,'L');
			String[] L_COLHD = {"Select","Run No.","Line No.","DSP","MFI","IZO","VIC","TS","EL","RSM","WI","a","b","Y1"};
			int[] L_COLSZ = {40,80,80,50,50,50,50,50,50,50,50,50,50,50};      		
			tblRUNDL = crtTBLPNL1(this,L_COLHD,500,5,1,4.6,8,L_COLSZ,new int[]{0});	
			
			add(lbl1 = new JLabel("Please  Click  on  Run  number,  to  get  Lot  Details."),10,1,1,4,this,'L');
			add(lbl2 = new JLabel("Number  of  Lots  in  Run  :- "),10,6,1,2,this,'L');
			add(lblLOTCT=new JLabel("00"),10,8,1,1,this,'L');			
			lbl1.setFont(new Font("Times New Roman", Font.PLAIN,14));
			lbl1.setForeground(Color.blue);
			lbl2.setForeground(Color.blue);
			lblLOTCT.setForeground(Color.blue);
			
			add(new JLabel("Lot Details"),11,1,1,.9,this,'L');
			add(lbl3 = new JLabel("Total  Bagged  Quantity    :- "),11,6,1,2,this,'L');
			add(lblBAGQT = new JLabel("00"),11,8,1,1,this,'L');
			lbl3.setForeground(Color.blue);
			lblBAGQT.setForeground(Color.blue);
			
			String[] L_COLHD1 = {"Select","Lot No.","Date","Grade","Cl.Flag","DSP","MFI","IZO","VIC","TS","EL","RSM","WI","a","b","Y1"};
			int[] L_COLSZ1 = {40,70,80,80,40,40,40,40,40,40,40,40,40,40,40,40};      		
			tblLOTDL = crtTBLPNL1(this,L_COLHD1,500,12,1,4.6,8,L_COLSZ1,new int[]{0});	
			tblLOTDL.addMouseListener(this);
			add(lbl4 = new JLabel("Classification  Status :-  U : Unclassified    C : Classfied    P : Prov. Classfied"),17,1,1,6,this,'L');
			add(lbl5 = new JLabel("* Please click on Lot number for Classification  Details."),18,1,1,6,this,'L');			
			
			lbl5.setFont(new Font("Times New Roman", Font.ITALIC,14));
			lbl4.setForeground(Color.blue);
			lbl5.setForeground(Color.blue);
			
		    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}		
			hstSHRDS = new Hashtable<String,String>();
			hstUOMCD = new Hashtable<String,String>();
			M_strSQLQRY = "Select QS_QPRCD,QS_SHRDS,QS_UOMCD,QS_TSMCD from CO_QSMST where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
					{
						hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));
						hstSHRDS.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_SHRDS"),""));
					}
				}				
				M_rstRSSET.close();
			}	
			txtFRDAT.setInputVerifier(objINPVR);
			txtTODAT.setInputVerifier(objINPVR);
			txtPRDCD.setInputVerifier(objINPVR);
			objTBLVRF = new TBLINPVF();
			tblRUNDL.setInputVerifier(objTBLVRF);
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}       
	}
	/**
	 * Super class method override to inhance its functionality according to Requriment.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		if(P_flgSTAT == false)
			clrCOMP();
		tblRUNDL.setEnabled(false);
		tblRUNDL.cmpEDITR[TB1_CHKFL].setEnabled(true);
		tblLOTDL.setEnabled(false);
        txtPRDDS.setEnabled(false);
		tblLOTDL.cmpEDITR[TB2_CHKFL].setEnabled(true);
	}
	public void mouseClicked(MouseEvent L_ME)
    {
        if(M_objSOURC == tblLOTDL)
        {
            for(int i=0;i<tblLOTDL.getRowCount();i++)
            {
                if(i !=tblLOTDL.getSelectedRow())
                    tblLOTDL.setValueAt(Boolean.FALSE,i,TB2_CHKFL);
                else
                    tblLOTDL.setValueAt(Boolean.TRUE,i,TB2_CHKFL);
            }
            cl_dat.M_btnUNDO_pbst.setEnabled(false);
            qc_qrlcl obj = new qc_qrlcl(0);             // create an object
        	cl_dat.M_pnlFRBTM_pbst.add("screen1", obj); // add it to the card layout
			cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen1"); // show this card
            obj.exeLTQRY(tblLOTDL.getValueAt(tblLOTDL.getSelectedRow(),TB2_LOTNO).toString(),"00","01","01");  
        }
    }
	public void actionPerformed(ActionEvent L_AE)
    {
        super.actionPerformed(L_AE);   
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
			    setMSG("Please Select the Product Type..",'N');				
				//setMSG("Please Enter Date range to generate the report..",'N');				
				setENBL(true);				
				int L_intDAYCT = Integer.valueOf(cl_dat.M_strLOGDT_pbst.substring(0,2)).intValue();					
				txtFRDAT.setText(calDATE(cl_dat.M_strLOGDT_pbst,L_intDAYCT));
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);				
				cmbPRDTP.requestFocus();
			}
			else
			{				
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == cmbPRDTP)
		{				
			txtPRDCD.setText("");
			txtPRDDS.setText("");
			tblRUNDL.clrTABLE();
			tblLOTDL.clrTABLE();
			txtFRDAT.requestFocus();
			setMSG("Please Enter From Date To Specify Date Range..",'N');
		}
		else if(M_objSOURC == txtFRDAT)
		{						
			txtPRDCD.setText("");
			txtPRDDS.setText("");
			tblRUNDL.clrTABLE();
			tblLOTDL.clrTABLE();
			txtTODAT.requestFocus();
			setMSG("Enter Date to Specify date range..",'N');			
		}
		else if(M_objSOURC == txtTODAT)		
		{					
			txtPRDCD.setText("");
			txtPRDDS.setText("");
			tblRUNDL.clrTABLE();
			tblLOTDL.clrTABLE();
			txtPRDCD.requestFocus();
			setMSG("Please Enter Product Code OR Press F1 To Select From List..",'N');		
		}	
		else if(M_objSOURC == txtPRDCD)
		{
			if(txtPRDCD.getText().trim().length()>0)
			{				
				tblRUNDL.requestFocus();
				setMSG("Select Run number to View lot Details..",'N');
				this.setCursor(cl_dat.M_curWTSTS_pbst);				
				tblRUNDL.clrTABLE();
				tblLOTDL.clrTABLE();
				tblRUNDL.setRowSelectionInterval(0,0);
				tblRUNDL.setColumnSelectionInterval(0,0);
				tblLOTDL.setRowSelectionInterval(0,0);
				tblLOTDL.setColumnSelectionInterval(0,0);
				getQPRDL();				
				getRUNDTL();				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else			
				setMSG("Please Enter Product Code to get details..",'N');			
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{						
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;									
		}						
    }	
	public void keyPressed(KeyEvent L_KE)
    {
		super.keyPressed(L_KE);
      	if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{												
				cl_dat.M_flgHELPFL_pbst = true;								
				if(L_KE.getSource().equals(txtPRDCD))
				{
					strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
					strQCATP = cmbPRDTP.getSelectedItem().toString().substring(0,2);					
					if(txtFRDAT.getText().trim().length()>0)
						strFMDAT = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFRDAT.getText().trim()+" "+"07:00"))+"'";
					if(txtTODAT.getText().trim().length()>0)
						strTODAT ="'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" "+"07:00"))+"'";					
					cl_dat.M_flgHELPFL_pbst = true;		
					M_strHLPFLD = "txtPRDCD";					
					String L_arrHDR[] ={"Product Code","Grade"};
					
				    //M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_STSFL <> 'X' order by PR_PRDDS";
					M_strSQLQRY = "SELECT distinct LT_TPRCD,PR_PRDDS from PR_LTMST,CO_PRMST";
					M_strSQLQRY += " Where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(LT_STSFL,'')<>'X' AND LT_PRDTP ='"+ strPRDTP +"'";
					
					if((txtFRDAT.getText().trim().length()>0) &&(txtTODAT.getText().trim().length()>0))
						M_strSQLQRY += " AND LT_PSTDT BETWEEN " + strFMDAT.trim() + " AND " + strTODAT.trim() ;					
					
					else if(txtFRDAT.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_PSTDT > "+ strFMDAT.trim();
					else if(txtTODAT.getText().trim().length() > 0)
						M_strSQLQRY += " AND LT_PSTDT < "+ strTODAT.trim();					
					M_strSQLQRY += " AND ltrim(str(LT_TPRCD,20,0)) = PR_PRDCD";
					M_strSQLQRY += " ORDER BY LT_TPRCD";									    					
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");										
				}
			}
			catch(Exception L_EX)
			{	
				setMSG(L_EX,"F1 Help");                            
			}      
        }
    	if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
            if(M_objSOURC == cmbPRDTP)
            {
                txtFRDAT.requestFocus();
                setMSG("Please Specify Date Value in Date Range..",'N');
            }
            
        }
	}			
	/**
	*Method to Calculate From-Date, one day smaller than To-Date.
    *@param P_strTODT String argument to pass To_Date.
    *@param P_intDATCT Integer variable to pass the day count to calculate the req. date.
	*/
    private String calDATE(String P_strTODT,int P_intDATCT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strTODT));
		    M_calLOCAL.add(java.util.Calendar.DATE,- (P_intDATCT -1));																
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{	       
			setMSG(L_EX, "calDATE");
			return (cl_dat.M_strLOGDT_pbst);
        }					
	}
	/**
	 * Methode to get the Quality Paramater List Width in number of charectors.
	 */
	private boolean getQPRDL()
	{		
		ResultSet L_rstRSSET ;
		try
		{
	   		arrQPRCD = new String[intQPRCT]; 
		    arrCLDEC = new int[intQPRCT]; 
			arrAVRGE = new double[intQPRCT]; 			
			M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_NMP02 from CO_CDTRN where CMT_CGMTP = 'RPT' ";
			M_strSQLQRY += " AND CMT_CGSTP = 'QCXXPTA'" ;
			M_strSQLQRY += " ORDER BY CMT_NCSVL";			
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET != null)		
			{ 
				int i=0;
				while(L_rstRSSET.next())
				{
					arrQPRCD[i] = L_rstRSSET.getString("CMT_CODCD");
					if(arrQPRCD[i]!=null)
					arrCLDEC[i] = L_rstRSSET.getInt("CMT_NMP02");// decimal
					i++;					
				}				
			    L_rstRSSET.close();
			}		
		 }		
		 catch(Exception L_EX)
		 {
			 setMSG(L_EX,"getQPRDL");
			 return false;
		 }
		 return true;
	}
	/**
	 * Method to get the Run Details 
	 */
	private void getRUNDTL()
	{
		ResultSet L_rstRSSET ;
		String L_strQPRVL;	
		strTPRCD = txtPRDCD.getText().trim();		
		try
		{
			int i=0,L_intCOUNT =0;
			intROWCT =0;		
			strFMDAT ="";
			strTODAT ="";
			if(txtFRDAT.getText().trim().length()>0)
			    strFMDAT = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFRDAT.getText().trim()+" "+"07:00"))+"'";
			if(txtTODAT.getText().trim().length()>0)
				strTODAT ="'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" "+"07:00"))+"'";					

			M_strSQLQRY = "SELECT distinct LT_RUNNO,LT_LINNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_TPRCD = ";
			M_strSQLQRY += "'"+ txtPRDCD.getText().trim()+"'";
			if((txtFRDAT.getText().trim().length()>0)&&(txtTODAT.getText().trim().length()>0))
                M_strSQLQRY += " AND LT_PSTDT BETWEEN " + strFMDAT.trim() + " AND " + strTODAT.trim() ;			
            else if(txtFRDAT.getText().trim().length()>0)
                M_strSQLQRY += " AND LT_PSTDT > " + strFMDAT.trim();
            else if(txtTODAT.getText().trim().length()>0)
                M_strSQLQRY += " AND LT_PSTDT < " + strTODAT.trim() ;   
			M_strSQLQRY += " AND LT_CLSFL <> '8'";
			M_strSQLQRY += " AND LT_RCLNO = '" + intINTRCL + "'";
			M_strSQLQRY += " AND isnull(LT_RUNNO,'') <>''";
			M_strSQLQRY += " ORDER BY LT_RUNNO";		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{				
					strRUNNO = M_rstRSSET.getString("LT_RUNNO");
					if(strRUNNO == null)
						strRUNNO ="";
					tblRUNDL.setValueAt(strRUNNO,intROWCT,TB1_RUNNO);
					strLINNO = M_rstRSSET.getString("LT_LINNO");
					if(strLINNO == null)
						strLINNO ="";
					tblRUNDL.setValueAt(strLINNO,intROWCT,TB1_LINNO);
					if(strRUNNO != null)
					{						
						L_intCOUNT +=1;
						M_strSQLQRY = "Select ";
						int k=0;
						M_strSQLQRY += "PS_"+arrQPRCD[k].trim()+"VL";
						for(k=1;k<arrQPRCD.length;k++)						
							M_strSQLQRY += ","+"PS_"+arrQPRCD[k].trim()+"VL";						
						M_strSQLQRY +=" FROM QC_PSMST where ";
						M_strSQLQRY +=" PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = " + "'"+strQCATP + "'" + " AND PS_TSTTP = " + "'"+ strTSTTP + "'";
						M_strSQLQRY += " AND isnull(PS_STSFL,'') <> 'X' AND PS_RCLNO = '"+intINTRCL +"'";
						M_strSQLQRY += " AND PS_LOTNO IN(select LT_LOTNO from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_runno ='"+ strRUNNO.trim() + "' AND LT_RCLNO = '" +intINTRCL +"'";
						M_strSQLQRY += " AND LT_TPRCD = '"+strTPRCD+"')";						
						L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						int L_intLOTCT =0;
						for(int j=0;j<arrAVRGE.length;j++)						
							arrAVRGE[j] =0;						
						if(L_rstRSSET !=null)
						{
							while(L_rstRSSET.next())
							{
								for(int l=0;l<arrQPRCD.length;l++)
								{
									L_strQPRVL = "-";
									L_strQPRVL = L_rstRSSET.getString("PS_"+arrQPRCD[l].trim()+"VL");
									if(L_strQPRVL == null)
										L_strQPRVL = "-";
									if((L_strQPRVL !=null)&&(!L_strQPRVL.equals("-")))								  
										arrAVRGE[l] = arrAVRGE[l] + Double.valueOf(L_strQPRVL).doubleValue(); 									
								}
								L_intLOTCT +=1;
							}
							L_rstRSSET.close();
						}
						for(int l=0;l<arrQPRCD.length;l++)						
							tblRUNDL.setValueAt(setNumberFormat(arrAVRGE[l]/L_intLOTCT,arrCLDEC[l]+1),intROWCT,l+3);						
					}
					intROWCT++;
				}
			}			
		}		
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRUNDTL");
		}
	}
	/**
	 * Method to execute the F1 help for Product Code.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
        if (M_strHLPFLD.equals("txtPRDCD"))
        {
			txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
            txtPRDDS.setEnabled(false);          
        }
        cl_dat.M_flgHELPFL_pbst = false;
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_qrgpa.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_qrgpa.doc";				
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			/*if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}*/
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Product Run Query & Analysis Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}		
	/**
	* Method to validate Consignment number, before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{		
		if(txtPRDCD.getText().length()==0)			
		{
			setMSG("Please Enter Product Code OR Press F1 To Select From List.. ",'E');
			txtPRDCD.requestFocus();			
			return false;
		}
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount()==0)
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
    *Method to fetch data from MM_GRMST,CO_CTMST & MM_BETRN tables & club it with Header &
    *footer in Data Output Stream.
	*/
	private void getDATA()
	{				
		ResultSet L_rstRSSET;
		strOPRDCD="";strPRDCD="";
		strORUNNO="";strRUNNO="";			
		String L_strQPRVL;
		strTPRCD = txtPRDCD.getText().trim();			
		int i = 0,L_intROWCT = 0,L_intCOUNT =0;	
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Gradewise product Analysis Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}	
			prnHEADER();
			
			//getLOTRNG();			
			for(i=0;i<((intROWWD -16)/7);i++) /// to initialize the array having any no of quality parameters
				arrAVRGE[i] = 0.0;
			
			M_strSQLQRY = " SELECT LT_LOTNO,LT_CLSFL,LT_RUNNO,LT_LINNO,LT_RESFL,LT_PSTDT,LT_PRDCD from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_TPRCD = ";
			M_strSQLQRY += "'"+ strTPRCD+"'";
			//M_strSQLQRY += " AND LT_PSTDT BETWEEN " + strFMDAT.trim() + " AND " + strTODAT.trim() ;	
			if((txtFRDAT.getText().trim().length()>0)&&(txtTODAT.getText().trim().length()>0))
                M_strSQLQRY += " AND LT_PSTDT BETWEEN " + strFMDAT.trim() + " AND " + strTODAT.trim() ;			
            else if(txtFRDAT.getText().trim().length()>0)
                M_strSQLQRY += " AND LT_PSTDT > " + strFMDAT.trim();
            else if(txtTODAT.getText().trim().length()>0)
                M_strSQLQRY += " AND LT_PSTDT < " + strTODAT.trim() ;   
			//
			M_strSQLQRY += " AND LT_CLSFL <> '8'";
			M_strSQLQRY += " AND LT_RCLNO = '" +intINTRCL +"'";
            M_strSQLQRY += " AND isnull(LT_RUNNO,'') <>''";
			M_strSQLQRY += " ORDER BY LT_RUNNO,LT_PRDCD,LT_LOTNO";
				
			StringBuffer L_PRNLIN = new StringBuffer();			
		
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{								
				while(M_rstRSSET.next())
				{					
					strLOTNO = nvlSTRVL(M_rstRSSET.getString("LT_LOTNO"),"");
					strRUNNO = nvlSTRVL(M_rstRSSET.getString("LT_RUNNO"),"");
					strLINNO = nvlSTRVL(M_rstRSSET.getString("LT_LINNO"),"");																
					strCLSFL = nvlSTRVL(M_rstRSSET.getString("LT_CLSFL"),"");
					strPRDCD = nvlSTRVL(M_rstRSSET.getString("LT_PRDCD"),"");
					
					if(strCLSFL.trim().equals("9"))
						strCLSFL =" ";
					else if(strCLSFL.trim().equals("4"))
						strCLSFL ="P";
					else 
						strCLSFL ="U";					
					java.sql.Timestamp L_tmsTEMP = M_rstRSSET.getTimestamp("LT_PSTDT");
					if(L_tmsTEMP != null)					
						strLOTDT = M_fmtLCDTM.format(L_tmsTEMP);				
					strLOTDT = strLOTDT.substring(0,10);																															
					
					if(strLOTNO.equals(""))				
						return;																				
					if(!strRUNNO.equals(strORUNNO))
					{	
						if(cl_dat.M_intLINNO_pbst> 63)
						{													
							dosREPORT.writeBytes(stbDOTLN.toString());
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();												
						}
						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						if(L_intCOUNT > 0)
						{
							dosREPORT.writeBytes("\n"+padSTRING('R',"Average : ",17));						
							for(i=0;i<((intROWWD -16)/7);i++)
							{
								dosREPORT.writeBytes(padSTRING('L',setNumberFormat(arrAVRGE[i]/L_intROWCT,arrCLDEC[i]),7));
								arrAVRGE[i] = 0;
							}
							dosREPORT.writeBytes("\n\n");
							cl_dat.M_intLINNO_pbst  +=3;
							L_intROWCT = 0;
						}											
						dosREPORT.writeBytes(padSTRING('R',"Run No : "+ strRUNNO,21)+ padSTRING('R',"Line No. : " +strLINNO,17));
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						strORUNNO = strRUNNO;
						strOPRDCD="";
					}										
					if(!strPRDCD.equals(strOPRDCD))
					{
						if(cl_dat.M_intLINNO_pbst> 63)
						{													
							dosREPORT.writeBytes(stbDOTLN.toString());
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();												
						}						
						M_strSQLQRY = "Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+ strPRDCD +"'";				
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
								strPRDDS = L_rstRSSET.getString("PR_PRDDS");					
							L_rstRSSET.close();
						}												
						dosREPORT.writeBytes("\n"+"Grade : " + padSTRING('R',strPRDDS,12));						
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst += 3;												
						strOPRDCD = strPRDCD;
					}
					L_PRNLIN.delete(0,L_PRNLIN.toString().length());
					L_PRNLIN.append(padSTRING('R',strLOTNO+" "+strCLSFL,11));
					L_PRNLIN.append(padSTRING('R',strLOTDT,5));					
					
					M_strSQLQRY = "Select ";
					int k=0;
					M_strSQLQRY += "PS_"+arrQPRCD[k].trim()+"VL";
					for(k=1;k<arrQPRCD.length;k++)				
						M_strSQLQRY += ","+"PS_"+arrQPRCD[k].trim()+"VL";					
					M_strSQLQRY +=" FROM QC_PSMST where ";
					M_strSQLQRY +=" PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = " + "'"+strQCATP + "'" + " AND PS_TSTTP = " + "'"+ strTSTTP + "'";
					M_strSQLQRY += " AND PS_LOTNO ="+ "'"+strLOTNO.trim() + "'";
					M_strSQLQRY += " AND PS_RCLNO ="+ "'"+intINTRCL.trim() + "'";
					M_strSQLQRY += " AND isnull(PS_STSFL,'') <> 'X' ";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET != null)
				    {
						if(L_rstRSSET.next())
						{
							for(int l=0;l<arrQPRCD.length;l++)
							{
								L_strQPRVL = "-";
								L_strQPRVL = L_rstRSSET.getString("PS_"+arrQPRCD[l].trim()+"VL");
								if(L_strQPRVL == null)
									L_strQPRVL = "-";
								if((L_strQPRVL !=null)&&(!L_strQPRVL.equals("-")))					    
									arrAVRGE[l] = arrAVRGE[l] + Double.valueOf(L_strQPRVL).doubleValue(); 						
								if(l ==0)
									L_PRNLIN.append(padSTRING('L',L_strQPRVL,8));
								else
									L_PRNLIN.append(padSTRING('L',L_strQPRVL,7));								
							}							
							dosREPORT.writeBytes(L_PRNLIN.toString()+"\n");							
							cl_dat.M_intLINNO_pbst += 1;												
							if(cl_dat.M_intLINNO_pbst> 63)
							{													
								dosREPORT.writeBytes(stbDOTLN.toString());
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								prnHEADER();												
							}
						}						
						L_rstRSSET.close();						
					}
					L_intROWCT +=1;
					L_intCOUNT +=1;						
				}
				M_rstRSSET.close();								
			}						
			if(L_intCOUNT >0)
			{
				if(cl_dat.M_intLINNO_pbst> 63)
				{													
					dosREPORT.writeBytes(stbDOTLN.toString());
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					prnHEADER();										
				}
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<b>");
				dosREPORT.writeBytes("\n"+padSTRING('R',"Average : ",17));
				for(i=0;i<11;i++)
				{					
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(arrAVRGE[i]/L_intROWCT,arrCLDEC[i]),7));					
					arrAVRGE[i] = 0;
				}				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</b>");
				L_intROWCT =0;
				dosREPORT.writeBytes("\n\n");
				dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
				cl_dat.M_intLINNO_pbst += 4;
				dosREPORT.writeBytes("Note: U: Unclassified Lots  P: Prov. Classified Lots ");	
			}
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}	
	/**
	 * Method to Generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			intCOLWD = 7;
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			stbHEADR = new StringBuffer(padSTRING('R'," Lot No.",11));
			stbHEADR.append(padSTRING('R',"Date",6));
			intROWWD = 16;
			String L_strSHRDS ="",L_strUOMCD ="";
			for(int j=0;j<intQPRCT;j++)
			{
				//String LM_DESC = cl_dat.getPRMCOD("CMT_SHRDS","SYS","QCXXQPR",arrQPRCD[j]);
				L_strSHRDS = hstSHRDS.get(arrQPRCD[j]).toString();
				if(L_strSHRDS !=null)
				{
					stbHEADR.append(padSTRING('L',L_strSHRDS.trim(),intCOLWD));
					intROWWD += intCOLWD;
				}
			}			
			stbHEADR.append("\n"+padSTRING('R'," ",17));
			for(int j=0;j<intQPRCT;j++)
			{
				//String LM_UOM = cl_dat.getPRMCOD("CMT_CCSVL","SYS","QCXXQPR",arrQPRCD[j]);
				L_strUOMCD = hstSHRDS.get(arrQPRCD[j]).toString();
				if(L_strUOMCD !=null)
				{
					if((j==0)||(j==1))
					{
						stbHEADR.append(padSTRING('R',L_strUOMCD.trim(),9));
					}
					else if(j==5)
					{
						stbHEADR.append("  "+ padSTRING('R',L_strUOMCD.trim(),5));
					}
					else
						stbHEADR.append(padSTRING('R',L_strUOMCD.trim(),7));
				}
			}			
			stbDOTLN = new StringBuffer("---");
			for(int i =0; i< intROWWD;i++)
				stbDOTLN.append("-");
			
			dosREPORT.writeBytes("\n\n\n\n\n");			 
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWWD -23));
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst +"\n");
			dosREPORT.writeBytes(padSTRING('R',"Gradewise Product Test Analysis",intROWWD -23));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n" );
			dosREPORT.writeBytes("From Date : "+txtFRDAT.getText().trim()+"  To Date : "+txtTODAT.getText().trim()+"\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");
			dosREPORT.writeBytes(stbHEADR.toString()+"\n");
			dosREPORT.writeBytes(stbDOTLN.toString()+"\n");	
			cl_dat.M_intLINNO_pbst =12;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}		
	/**
	 * Method to get the lot Details for selected lot number & to di
	 */
	void getLOTDL()
	{		
		String L_strQPRVL,L_strPRDCD;
		double L_dblBAGQT =0;					
		int L_intLOTCT =0;
		dblBAGQT =0;
		try
		{							
			setMSG("Fetching the Lot Details..",'N');
			this.setCursor(cl_dat.M_curWTSTS_pbst);		  			
			tblLOTDL.clrTABLE();
			tblLOTDL.setRowSelectionInterval(0,0);
			tblLOTDL.setColumnSelectionInterval(0,0);
			for(int i = 0; i<tblRUNDL.getRowCount();i++)
			{
				tblRUNDL.setValueAt(new Boolean(false),i,TB1_CHKFL);
				tblRUNDL.setValueAt(new Boolean(true),tblRUNDL.getSelectedRow(),TB1_CHKFL);	
				if(tblRUNDL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
				{																			
					strTPRCD = txtPRDCD.getText().trim();
					M_strSQLQRY = "Select LT_LOTNO,LT_PSTDT,LT_CLSFL,LT_PRDCD,LT_BAGQT, ";
					int k=0;
					M_strSQLQRY += "PS_"+arrQPRCD[k].trim()+"VL";
					for(k=1;k<arrQPRCD.length;k++)						
						M_strSQLQRY += ","+"PS_"+arrQPRCD[k].trim()+"VL";						
					M_strSQLQRY +=" FROM QC_PSMST,PR_LTMST where ";
					M_strSQLQRY +=" PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = " + "'"+strQCATP + "'";
					M_strSQLQRY +=" AND PS_TSTTP = " + "'"+ strTSTTP + "'";
					M_strSQLQRY +=" AND isnull(PS_STSFL,'') <> 'X' ";
					M_strSQLQRY +=" AND PS_RCLNO = '"+intINTRCL +"'";
					M_strSQLQRY +=" AND PS_LOTNO = LT_LOTNO AND PS_RCLNO = LT_RCLNO";
					M_strSQLQRY +=" AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_RUNNO = '"+strRUNNO +"'"; 
					M_strSQLQRY +=" AND LT_TPRCD = '"+ strTPRCD.trim() +"'";
				//	M_strSQLQRY +=" AND LT_PSTDT BETWEEN " + strFMDAT.trim() + " AND " + strTODAT.trim() ;
    				if((txtFRDAT.getText().trim().length()>0)&&(txtTODAT.getText().trim().length()>0))
                        M_strSQLQRY += " AND LT_PSTDT BETWEEN " + strFMDAT.trim() + " AND " + strTODAT.trim() ;			
                    else if(txtFRDAT.getText().trim().length()>0)
                       M_strSQLQRY += " AND LT_PSTDT > " + strFMDAT.trim();
                    else if(txtTODAT.getText().trim().length()>0)
                        M_strSQLQRY += " AND LT_PSTDT < " + strTODAT.trim() ;   
			
					java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,   ResultSet.CONCUR_UPDATABLE);
					M_rstRSSET = L_stat.executeQuery(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						intDTLCNT=0;
						while(M_rstRSSET.next())
							intDTLCNT++;
						M_rstRSSET.beforeFirst();									
					}
					if(intPRDTCT < intDTLCNT)							
						intPRDTCT = intDTLCNT;// previous row count in detail table					
					while(M_rstRSSET.next())
					{
						L_strPRDCD =" ";
						L_dblBAGQT =0;
						for(int l=0;l<arrQPRCD.length;l++)
						{
							L_strQPRVL = "-";
							strLOTNO = M_rstRSSET.getString("LT_LOTNO");					
							java.sql.Timestamp L_tmsTEMP = M_rstRSSET.getTimestamp("LT_PSTDT");
							L_dblBAGQT = M_rstRSSET.getDouble("LT_BAGQT");					
								 dblBAGQT += L_dblBAGQT;
							if(L_tmsTEMP !=null)					
								strLOTDT = M_fmtLCDTM.format(L_tmsTEMP).substring(0,10);					
							else
								strLOTDT = " ";
							strCLSFL = M_rstRSSET.getString("LT_CLSFL");
							if(strCLSFL !=null)
							if(strCLSFL.trim().equals("9"))
								strCLSFL ="C";
							else if(strCLSFL.trim().equals("4"))
								strCLSFL ="P";
							else 
								strCLSFL ="U";
							L_strPRDCD = M_rstRSSET.getString("LT_PRDCD");
							L_strQPRVL = M_rstRSSET.getString("PS_"+arrQPRCD[l].trim()+"VL");
							if(L_strQPRVL == null)
								L_strQPRVL = "-";
							tblLOTDL.setValueAt(strLOTNO,L_intLOTCT,TB2_LOTNO);
							tblLOTDL.setValueAt(strLOTDT,L_intLOTCT,TB2_LOTDT);
														
							M_strSQLQRY = "Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+ L_strPRDCD +"'";
							ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							if(L_rstRSSET != null)
							{
								if(L_rstRSSET.next())
									strPRDDS = L_rstRSSET.getString("PR_PRDDS");
								L_rstRSSET.close();
							}
							tblLOTDL.setValueAt(strPRDDS,L_intLOTCT,TB2_GRADE);
									
							tblLOTDL.setValueAt(strCLSFL,L_intLOTCT,TB2_CLSFL);
							tblLOTDL.setValueAt(L_strQPRVL,L_intLOTCT,l+5);
						}
						L_intLOTCT +=1;
					}
					lblLOTCT.setText(String.valueOf(L_intLOTCT));
					lblBAGQT.setText(setNumberFormat(dblBAGQT,3));

					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"M_objSOURC == tblRUNDL.cmpEDITR[1]");
		}		
	}	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if((input == txtFRDAT) && (txtFRDAT.getText().trim().length() == 10))
				{				
					if(M_fmtLCDTM.parse(txtFRDAT.getText().trim()+" "+"07:00").compareTo(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))>0)
					{	
						setMSG("From date must be smaller than current Date..",'E');						
						return false;
					}																	
					if(txtTODAT.getText().trim().length()>0)
					{
						if(M_fmtLCDTM.parse(txtFRDAT.getText().trim()+" "+"07:00").compareTo(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" "+"07:00"))>0)
						{
							setMSG("From Date must be smaller than To Date..",'E');						
							return false;
						}
					}
				}
				if((input == txtTODAT) && (txtTODAT.getText().trim().length() == 10))
				{				
					if(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" "+"07:00").compareTo(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))>0)
					{	
						setMSG("To Date must be smaller than current Date..",'E');						
						return false;
					}																	
					if(txtFRDAT.getText().trim().length()>0)
					{
						if(M_fmtLCDTM.parse(txtFRDAT.getText().trim()+" "+"07:00").compareTo(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" "+"07:00"))>0)
						{
							setMSG("From Date must be smaller than To Date..",'E');						
							return false;
						}
					}
				}	
				if((input == txtPRDCD) && (txtPRDCD.getText().trim().length() == 10))
				{
					if(txtFRDAT.getText().trim().length()>0)
						strFMDAT = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFRDAT.getText().trim()+" "+"07:00"))+"'";
					if(txtTODAT.getText().trim().length()>0)
						strTODAT ="'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim()+" "+"07:00"))+"'";					
					cl_dat.M_flgHELPFL_pbst = true;					
				    //M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_STSFL <> 'X' order by PR_PRDDS  ";
					
					M_strSQLQRY = "SELECT distinct LT_TPRCD,PR_PRDDS from PR_LTMST,CO_PRMST";
					M_strSQLQRY += " Where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(LT_STSFL,'')<>'X'";										
					
					if((txtFRDAT.getText().trim().length()>0) &&(txtTODAT.getText().trim().length()>0))
						M_strSQLQRY += " AND LT_PSTDT BETWEEN " + strFMDAT.trim() + " AND " + strTODAT.trim() ;					
					
					else if(txtFRDAT.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_PSTDT > "+ strFMDAT.trim();
					
					else if(txtTODAT.getText().trim().length() < 0)
						M_strSQLQRY += " AND LT_PSTDT < "+ strTODAT.trim();					
					M_strSQLQRY += " AND ltrim(str(LT_TPRCD,20,0)) = PR_PRDCD";
					M_strSQLQRY += " AND LT_TPRCD ='"+txtPRDCD.getText().trim() +"'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						    txtPRDDS.setText(L_rstRSSET.getString("PR_PRDDS"));						   						
						else
						{
							setMSG("Specified Material is not produced in the given Date Range..",'E');							
							L_rstRSSET.close();
							return false;
						}
						L_rstRSSET.close();
					}	
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			return true;
		}
	}
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try  
			{
				if((P_intCOLID == TB1_CHKFL) ||(P_intCOLID == TB1_RUNNO))
				{
					setMSG("",'N');
					int L_intROWNO = tblRUNDL.getSelectedRow();						
					for (int i =0; i<tblRUNDL.getRowCount();i++)
						tblRUNDL.setValueAt(new Boolean(false),i,0);
					tblRUNDL.setValueAt(new Boolean(true),L_intROWNO,0);
					strRUNNO = tblRUNDL.getValueAt(L_intROWNO,1).toString();
					if (strRUNNO.trim().length() == 0)
					{
						for (int i =0; i<tblRUNDL.getRowCount();i++)
							tblRUNDL.setValueAt(new Boolean(false),i,0);
						setMSG("Please Select the Row where Run Number is not blank..",'E');
						return false;
					}
					getLOTDL();
					
					return true;
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"table verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}	
}	