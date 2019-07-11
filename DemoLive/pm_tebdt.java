/*
 * 
System Name:Plant Maintenance.
Program Name:Equipment- Base Detail-Tag

Purpose : This module used for accepting the tag Detail .
 
Source Directory: f:\source\splerp3\pm_tebdt.java                         
Executable Directory: F:\exec\splerp3\pm_tebdt.class
List of tables used:
Table Name		Primary key					                    Operation done
								                      Insert   Update	   Query    Delete	
---------------------------------------------------------------------------------------------
PM_TGMST	    TG_TAGNO,TG_CMPCD           			/		/			/		  /
CO_CDTRN        CMT_CGMTP,CMT_CGSTP,CMT_CODCD 								/
CO_PTMST        PT_PRTTP,PT_PRTCD        	                    	        /        
-------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on screen:
Field Name		Column Name			    Table name		      Type/Size	       Description
-------------------------------------------------------------------------------------------------------
txtTAGNO		cmt_codcd/TG_TAGNO	    PM_TGMST 	          Varchar(15)	   Tag No.
txtTAGDS		TG_TAGDS	            PM_TGMST         	  Varchar(50)	   Tag Desp.	
txtPLNCD        cmt_codcd/TG_PLNCD      CO_CDTRN/PM_TGMST     Varchar(3)       Plant Code
txtPLNDS        cmt_codds       		CO_CDTRN              Varchar(50)      Plant Description
txtARACD        cmt_codcd/TG_ARACD      CO_CDTRN/PM_TGMST     VARCHAR(3)       Area Code
txtARADS        cmt_codds               CO_CDTRN              VARCHAR(50)      Area Description
txtCRICD        cmt_codcd/TG_CRICD      CO_CDTRN/PM_TGMST     VARCHAR(2)       Criticality
txtCRIDS        cmt_codds               CO_CDTRN              VARCHAR(50)      Criticality Description
txtINSDT        TG_INSDT                PM_TGMST              Date             Installed Date      
txtDRGNO        TG_DRGNO                PM_TGMST              VARCHAR(20)      Drawing No.
txtPORNO        TG_PORNO                PM_TGMST              VARCHAR(15)      Purchase Order No.
txtPORDT        TG_PORDT                PM_TGMST              Date             Purchase Order Date
txtPORVL        TG_PORVL                PM_TGMST              DECIMAL(12)      Purchase Value
txtMFRCD        TG_MFRCD                PM_TGMST/CO_PTMST     VARCHAR(5)       Manufracturer Code
txtMFNAM        PT_PRTNM                CO_PTMST              VARCHAR(40)      Manufracturer Name
txtMFADD        PT_ADR01                CO_PTMST              VARCHAR(40)      Manufracturer Address
txtMFCTY        PT_CTYNM                CO_PTMST              VARCHAR(15)      Manufracturer City 
txtVENCD        TG_PRTCD                PM_TGMST/CO_PTMST     VARCHAR(5)       Vendor Code        
txtVENAM        PT_PRTNM                CO_PTMST              VARCHAR(40)      Vendor Name
txtVEADD        PT_ADR01                CO_PTMST              VARCHAR(40)      Vendor Address
txtVECTY        PT_CTYNM                CO_PTMST              VARCHAR(15)      Vendor City

Table tblEQPDL:

txtEQPID	    EQ_EQPID			    PM_EQMST		      VARCHAR(15)       Equipment ID
txtEQPDS        EQ_EQPDS      		    PM_EQMST		      VARCHAR(50)	    Description

----------------------------------------------------------------------------------------------------------

List of fields with help facility: 
Field Name	 Display Description		     		Display Columns			  	Table Name
-----------------------------------------------------------------------------------------------------------------------
txtTAGNO	 Tag No, Description             		TG_TAGNO,TG_TAGDS 		   	PM_TGMST
txtPLNCD	 Plant Code, Description                CMT_CODCD,CMT_CODDS		    CO_CDTRN/MST/PMXXPLN
txtARACD	 Area Code, Description                 CMT_CODCD,CMT_CODDS		    CO_CDTRN/MST/PMXXARA
txtCRICD	 Criticality Code, Description          CMT_CODCD,CMT_CODDS      	CO_CDTRN/MST/PMXXCRT
txtMFRCD	 Mfr Code, Name, Address,City           PT_PRTCD,PT_PRTNM, 			CO_PTMST/PT_PRTTP=S
txtVENCD											PT_ADR01,PT_CTYNM		    
------------------------------------------------------------------------------------------------------------------

Validations :
While saving the data:-
1]TAG No. is compulsory.
2]TAG Description is compulsory.
3]Plant Code is compulsory.
4]Area Code is compulsory. 
5]Criticality is compulsory.
6]Installed Date is compulsory.
7]Drawing No. is compulsory.
8]Purchase Order No. is compulsory.
9]Purchase Order Date is compulsory.
10]Purchase Order Value is compulsory.
11]Manufracturer Code is compulsory.
12]Vendor Code is compulsory.


Other Requirement:
1]If user entered Equipment Tag No. then all data is fetch related to that Equipment Tag No.
2]In Enquiry & Deletion option all fields are set to false except Tag No.
3]In Equipment Detail tab all Equipment IDs and Description related to that Tag No. is Displayed.

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import javax.swing.table.*;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;
import javax.swing.*;

public class pm_tebdt extends cl_pbase 
{	
  JTextField txtTAGNO,txtTAGDS,txtPLNCD,txtPLNDS,txtARACD,txtARADS,txtCRICD,txtCRIDS,txtINSDT,txtDRGNO,txtPORNO,txtPORDT,txtPORVL,txtMFRCD,txtVENCD;
  JTextField txtMFRNM,txtMFADD,txtMFCTY,txtVENAM,txtVEADD,txtVECTY,txtMATGR,txtMATDS;
  
  private JTabbedPane jtpMANTAB;          
  private JPanel pnlTAGDL,pnlEQPDL; 
  private  cl_JTable tblEQPDL;
  private INPVF oINPVF;
  
  int TB1_CHKFL = 0; 			  JCheckBox chkCHKFL;
  int TB1_EQPID = 1;              JTextField txtEQPID;
  int TB1_EQPDS = 2;              JTextField txtEQPDS;
  
  private Hashtable<String,String[]> hstCDTRN;
  private Hashtable<String,String[]> hstPTMST;			// Distributor details
  
  /** Array elements for records picked up from Code Transactoion */
  private int intCDTRN_TOT = 2;			
  private int intAE_CMT_CODCD = 0;		
  private int intAE_CMT_CODDS = 1;	
  

  /** Array elements for Distributor details */
  private int intPTMST_TOT = 3;
  private int intAE_PT_PRTNM = 0;						
  private int intAE_PT_ADR01 = 1;
  private int intAE_PT_CTYNM = 2;
  
  private String strPRTCD;
  
  /** Variables for Code Transaction Table
	 */
  private String strCGMTP;		
  private String strCGSTP;		
  private String strCODCD;
  
     
  pm_tebdt()
  {
	super(2);
	
	try
	{
		setMatrix(20,20);	
		
		pnlTAGDL = new JPanel();
		pnlEQPDL = new JPanel();
		pnlTAGDL.setLayout(null);
		pnlEQPDL.setLayout(null);
		jtpMANTAB=new JTabbedPane();
		jtpMANTAB.add(pnlTAGDL,"Tag Detail");
		jtpMANTAB.add(pnlEQPDL,"Equipments Detail");
		
		
		add(new JLabel("Tag No."),2,2,1,3,this,'L');  
		add(txtTAGNO = new TxtLimit(15),3,2,1,2,this,'L');
		add(new JLabel("Tag Description"),2,4,1,3,this,'L');
		add(txtTAGDS = new TxtLimit(50),3,4,1,4,this,'L'); 
		add(new JLabel("Plant"),2,8,1,2,this,'L');  
		add(txtPLNCD = new TxtNumLimit(3),3,8,1,1,this,'L');
		add(txtPLNDS = new TxtLimit(15),3,9,1,3,this,'L');
		add(new JLabel("Area"),2,12,1,2,this,'L');  
		add(txtARACD = new TxtNumLimit(3),3,12,1,1,this,'L');  
		add(txtARADS = new TxtLimit(15),3,13,1,4,this,'L');
		add(new JLabel("Criticality"),2,17,1,3,this,'L');  
		add(txtCRICD = new TxtLimit(2),3,17,1,1,this,'L'); 
		add(txtCRIDS = new TxtLimit(15),3,18,1,3,this,'L');
		
		add(new JLabel("Installed Date:"),1,2,1,3,pnlTAGDL,'L');  
		add(txtINSDT = new TxtDate(),1,4,1,3,pnlTAGDL,'L');
		add(new JLabel("Drawing No.:"),1,7,1,3,pnlTAGDL,'L');  
		add(txtDRGNO = new TxtLimit(20),1,9,1,3,pnlTAGDL,'L');
		//add(new JLabel("Material Group:"),1,12,1,3,pnlTAGDL,'L');  
		//add(txtMATGR = new TxtLimit(10),1,14,1,2,pnlTAGDL,'L');
		//add(txtMATDS = new TxtLimit(60),1,16,1,5,pnlTAGDL,'L');
		
		add(new JLabel("P.O.No.:"),3,2,1,3,pnlTAGDL,'L');  
		add(txtPORNO = new TxtLimit(15),3,4,1,3,pnlTAGDL,'L');
		add(new JLabel("P.O. Date:"),3,7,1,3,pnlTAGDL,'L');  
		add(txtPORDT = new TxtDate(),3,9,1,3,pnlTAGDL,'L');
		add(new JLabel("P.O.Value:"),3,12,1,3,pnlTAGDL,'L');  
		add(txtPORVL = new TxtNumLimit(12),3,14,1,4,pnlTAGDL,'L');
		
		
		add(new JLabel("Manufactured Details"),5,2,1,3,pnlTAGDL,'L');  
		add(new JLabel("Code:"),6,2,1,2,pnlTAGDL,'L');  
		add(txtMFRCD = new TxtLimit(50),6,3,1,3,pnlTAGDL,'L');
		add(txtMFRNM = new TxtLimit(50),7,3,1,6,pnlTAGDL,'L');
		add(txtMFADD = new TxtLimit(50),8,3,1,6,pnlTAGDL,'L');
		add(txtMFCTY = new TxtLimit(50),9,3,1,6,pnlTAGDL,'L');
		
		
		add(new JLabel("Vendor Details"),5,10,1,3,pnlTAGDL,'L');  
		add(new JLabel("Code:"),6,10,1,2,pnlTAGDL,'L');  
		add(txtVENCD = new TxtLimit(50),6,11,1,3,pnlTAGDL,'L');
		add(txtVENAM = new TxtLimit(50),7,11,1,6,pnlTAGDL,'L');
		add(txtVEADD = new TxtLimit(50),8,11,1,6,pnlTAGDL,'L');
		add(txtVECTY = new TxtLimit(50),9,11,1,6,pnlTAGDL,'L');
		
		String[] L_strTBLHD = {"","Equipment ID","Equipment Description "};
		int[] L_intCOLSZ = {10,200,350};
		tblEQPDL= crtTBLPNL1(pnlEQPDL,L_strTBLHD,200,3,2,6,12,L_intCOLSZ,new int[]{0});
		tblEQPDL.setEnabled(false);
		add(jtpMANTAB,5,2,13,19,this,'L');
		
		oINPVF=new INPVF();
		txtTAGNO.setInputVerifier(oINPVF);
		txtPLNCD.setInputVerifier(oINPVF);
		txtARACD.setInputVerifier(oINPVF);
		txtCRICD.setInputVerifier(oINPVF);
		txtINSDT.setInputVerifier(oINPVF);
		txtPORNO.setInputVerifier(oINPVF);
		txtPORDT.setInputVerifier(oINPVF);
		txtPORVL.setInputVerifier(oINPVF);
		txtMFRCD.setInputVerifier(oINPVF);
		txtVENCD.setInputVerifier(oINPVF);
	//	txtMATGR.setInputVerifier(oINPVF);
		
	    
		setENBL(false);
		
		hstCDTRN = new Hashtable<String,String[]>();
		hstPTMST = new Hashtable<String,String[]>();
		hstCDTRN.clear();
		crtCDTRN("'MSTPMXXPLN','MSTPMXXARA','MSTPMXXCRT'","",hstCDTRN);
		crtPTMST();
		
	} 
	catch(Exception L_EX)
	{
	    setMSG(L_EX,"Constructor");
	}
  }
  public void actionPerformed(ActionEvent L_AE)
  {
	super.actionPerformed(L_AE);
	    try
	    {
	    	
	      if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) 
	      {
	    	  txtTAGNO.setText("");
	  		  clrCOMP();
	    	  txtTAGNO.requestFocus();
	    	  jtpMANTAB.setEnabledAt(1,false);
	    	  txtPLNDS.setEnabled(false);
	    	  txtARADS.setEnabled(false);	
	    	  txtCRIDS.setEnabled(false);

		  	  txtMFRNM.setEnabled(false);
		  	  txtVENAM.setEnabled(false);
		  	  txtMFADD.setEnabled(false);
		  	  txtVEADD.setEnabled(false);
		  	  txtMFCTY.setEnabled(false);
		  	  txtVECTY.setEnabled(false);
		  	  //txtMATDS.setEnabled(false);
	    	
	    	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)
	    	|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
	    	{
	    		txtTAGNO.setEnabled(true);
	    		txtTAGDS.setEnabled(false);
	    		txtPLNCD.setEnabled(false);
	    		txtARACD.setEnabled(false);	
	    		txtCRICD.setEnabled(false);
	    		
	    		txtINSDT.setEnabled(false);
	    		txtDRGNO.setEnabled(false);
	    		txtPORNO.setEnabled(false);
	    		txtPORDT.setEnabled(false);
	    		txtPORVL.setEnabled(false);
	    		txtMFRCD.setEnabled(false);
	    		txtVENCD.setEnabled(false);
	    		tblEQPDL.setEnabled(false);
	    	}
	        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)
	        ||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))			
	    	{
	        	txtTAGNO.setEnabled(true);
	    		txtTAGDS.setEnabled(true);
	    		txtPLNCD.setEnabled(true);
	    		txtARACD.setEnabled(true);	
	    		txtCRICD.setEnabled(true);
	    		txtINSDT.setEnabled(true);
	    		txtDRGNO.setEnabled(true);
	    		txtPORNO.setEnabled(true);
	    		txtPORDT.setEnabled(true);
	    		txtPORVL.setEnabled(true);
	    		txtMFRCD.setEnabled(true);
	    		txtVENCD.setEnabled(true);
	    		tblEQPDL.setEnabled(false);
	        }
	        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
	        	jtpMANTAB.setEnabledAt(1,true);
	      }  
	    }
	    catch(Exception L_EX)
	    {
	      setMSG(L_EX,"action Perform");
	    }
  }
  

  public void keyPressed(KeyEvent L_KE)
  {
    super.keyPressed(L_KE);
	try
	{
		if(L_KE.getKeyCode() == L_KE.VK_F1 )
    	{						
			if(M_objSOURC==txtTAGNO && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtTAGNO";
    		  String L_ARRHDR[] = {"Tag No.","Description"};
    		  M_strSQLQRY = "select distinct TG_TAGNO,TG_TAGDS from PM_TGMST"; 
    		  M_strSQLQRY+= " where TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
    		  if(txtTAGNO.getText().length() >0)
  			  M_strSQLQRY += " and TG_TAGNO like '"+txtTAGNO.getText().trim()+"%'";
    		  M_strSQLQRY += " order by TG_TAGNO";
    		  //System.out.println("equipmentF1"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    		}
			
			else if(M_objSOURC==txtPLNCD)	
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtPLNCD";
    		  String L_ARRHDR[] = {"Plant Code","Description"};
    		  M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn";
			  M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXPLN' AND isnull(CMT_STSFL,'')<>'X'";
			  if(txtPLNCD.getText().length()>0)				
			  M_strSQLQRY+=  " AND cmt_codcd like '"+txtPLNCD.getText().trim()+"%'";
			  M_strSQLQRY+= " order by cmt_codcd";
    		  //System.out.println("plantf1"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    		}
			
			else if(M_objSOURC==txtARACD)	
    		{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtARACD";
				String L_ARRHDR[] = {"Area Code","Description"};
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
				M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXARA' AND isnull(CMT_STSFL,'')<>'X' ";
				if(txtARACD.getText().length()>0)				
				M_strSQLQRY+= " AND cmt_codcd like '"+txtARACD.getText().trim()+"%'";
				M_strSQLQRY+= " order by cmt_codcd";
				//System.out.println("txtARACD f1>>"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
    		}
			
			else if(M_objSOURC==txtCRICD)	
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtCRICD";
    		  String L_ARRHDR[] = {"Criticality Code","Criticality Description"};
    		  M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn";
			  M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXCRT' AND isnull(CMT_STSFL,'')<>'X'";
			  if(txtCRICD.getText().length()>0)				
			  M_strSQLQRY+=  " AND cmt_codcd like '"+txtCRICD.getText().trim()+"%'";
			  M_strSQLQRY+= " order by cmt_codcd";
			  //System.out.println("txtcriCD f1>>"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    		}
			/*else if(M_objSOURC==txtMATGR)		
			{
				M_strHLPFLD = "txtMATGR";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" select ct_matcd,ct_matds from co_ctmst where  substr(ct_matcd,3,8) = '0000000A' and isnull(CT_STSFL,'') <> 'X'";
				if(txtMATGR.getText().length() >0)
					M_strSQLQRY += " AND ct_matcd like '"+txtMATGR.getText().trim()+"%'";
				M_strSQLQRY += " order by ct_matcd";
				//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Material Group","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}*/
			else if(M_objSOURC==txtPORNO)		
			{
				M_strHLPFLD = "txtPORNO";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" select po_strtp,po_porno,po_pordt,po_porqt,po_porrt,po_porvl from mm_pomst where po_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
				//M_strSQLQRY += " AND po_matcd ='"+txtMATGR.getText().trim()+"'";
				if(txtPORNO.getText().length() >0)
					M_strSQLQRY += " AND po_porno like '"+txtPORNO.getText().trim()+"%'";
				M_strSQLQRY += " order by po_pordt desc";
				//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Store Type","P.O.No","P.O.Date","P.O.Qty","P.O.Rate","P.O.Value"},6,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC==txtMFRCD)	
    		{
				M_strHLPFLD = "txtMFRCD";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST ";
				M_strSQLQRY += "WHERE PT_PRTTP='S' AND isnull(PT_STSFL,'')<>'X'";
				if(txtMFRCD.getText().length() >0)
				M_strSQLQRY += " AND PT_PRTCD like '"+txtMFRCD.getText().trim()+"%'";
				M_strSQLQRY += " order by PT_PRTCD";
				System.out.println("txtMFRCD f1>>"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Mfr.code","Mfr.Name","Mfr.Address","Mfr.City"},4,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
    		}
			
			else if(M_objSOURC==txtVENCD)	
    		{
			  cl_dat.M_flgHELPFL_pbst = true;	
			  M_strHLPFLD = "txtVENCD";	
    		  String L_ARRHDR[] = {"Vendor Code","Vendor Name","Vendor Address","Vendor City "};
    		  M_strSQLQRY=" SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST ";
			  M_strSQLQRY += "WHERE PT_PRTTP='S' AND isnull(PT_STSFL,'')<>'X'";
			  if(txtVENCD.getText().length() >0)
			  M_strSQLQRY += " AND PT_PRTCD like '"+txtVENCD.getText().trim()+"%'";
			  M_strSQLQRY += " order by PT_PRTCD";
			  //System.out.println("txtVENCD f1>>"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
    		}
    	}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER )
	    {				
			if(M_objSOURC==txtTAGNO)
			{
			  if(txtTAGNO.getText().length()==0)
				txtTAGDS.setText("");
			  else
			  {
				  txtTAGNO.setText(txtTAGNO.getText().replace("'","`"));
				  txtTAGNO.setText(txtTAGNO.getText().toUpperCase());
			  }
			  txtTAGDS.requestFocus();
			  setMSG("Enter the Tag description..",'N');
		    }		
			else if(M_objSOURC == txtTAGDS)
			{
				if(txtTAGDS.getText().length()>0)
				{
					txtTAGDS.setText(txtTAGDS.getText().replace("'","`"));
					txtTAGDS.setText(txtTAGDS.getText().toUpperCase());
					
				}
					
				txtPLNCD.requestFocus();
				setMSG("Enter Plant code or Press F1 to Select from List..",'N');
			}
			else if(M_objSOURC == txtPLNCD)
			{
				if(txtPLNCD.getText().length()==0)
					txtPLNDS.setText("");
				txtARACD.requestFocus();
				setMSG("Enter Area code or Press F1 to Select from List..",'N');	
			}
			else if(M_objSOURC == txtARACD)
			{
				if(txtARACD.getText().length()==0)
					txtARADS.setText("");
				txtCRICD.requestFocus();
				setMSG("Enter Criticality code or Press F1 to Select from List..",'N');
			}
			else if(M_objSOURC == txtCRICD)
			{
				if(txtCRICD.getText().length()==0)
					txtCRIDS.setText("");
				txtINSDT.requestFocus();
				setMSG("Enter Installed Date",'N');		
			}
			
			else if(M_objSOURC == txtINSDT)
			{
				txtDRGNO.requestFocus();
				setMSG("Enter Drawing Number",'N');
			}
			else if(M_objSOURC == txtDRGNO)
			{
			    if(txtDRGNO.getText().length()>0)
			    {
			    	txtDRGNO.setText(txtDRGNO.getText().replace("'","`"));
			        txtDRGNO.setText(txtDRGNO.getText().toUpperCase());
			    }
			    	
				//txtMATGR.requestFocus();
				//setMSG("Enter Material group",'N');	
				
				txtPORNO.requestFocus();
				setMSG("Enter Purchase Order Number",'N');	
			}
		/*	else if(M_objSOURC == txtMATGR)
			{
				txtPORNO.requestFocus();
				setMSG("Enter Purchase Order Number",'N');	
			}*/
			else if(M_objSOURC == txtPORNO)
			{
				 if(txtPORNO.getText().length()>0)
				 {
					 txtPORNO.setText(txtPORNO.getText().replace("'","`"));
					 txtPORNO.setText(txtPORNO.getText().toUpperCase());
				 }
				 
				txtPORDT.requestFocus();
				setMSG("Enter Purchase Order Date",'N');
			}
			else if(M_objSOURC == txtPORDT)
			{
				txtPORVL.requestFocus();
				setMSG("Enter Purchase Order Value",'N');
			}
			else if(M_objSOURC == txtPORVL)
			{
				txtMFRCD.requestFocus();
				setMSG("Enter Manufracturer Code or Press F1 to Select from List..",'N');
			}
			else if(M_objSOURC == txtMFRCD)
			{
				if(txtMFRCD.getText().length()==0)
				{
					txtMFRNM.setText("");
					txtMFCTY.setText("");
					txtMFADD.setText("");
				}
				txtVENCD.requestFocus();
				setMSG("Enter Vendor Code",'N');	
			}
			else if(M_objSOURC == txtVENCD)
			{
				if(txtVENCD.getText().length()==0)
				{
					txtVENAM.setText("");
					txtVECTY.setText("");
					txtVEADD.setText("");
				}
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}	
	    }	
	}  
   	catch(NullPointerException L_NPE)
	{
	  setMSG("keyPressed",'E');  
	  setCursor(cl_dat.M_curDFSTS_pbst);
	}
  }
  
  void exeHLPOK()
  {
 	try
 	{
 	  super.exeHLPOK();
 	    if(M_strHLPFLD.equals("txtTAGNO"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtTAGNO.setText(L_STRTKN.nextToken());
			 txtTAGDS.setText(L_STRTKN.nextToken());
		}
 	    else if(M_strHLPFLD.equals("txtPLNCD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtPLNCD.setText(L_STRTKN.nextToken());
			 txtPLNDS.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtARACD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtARACD.setText(L_STRTKN.nextToken());
			 txtARADS.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtCRICD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtCRICD.setText(L_STRTKN.nextToken());
			 txtCRIDS.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtMFRCD"))
		{
			 String[] strMFRDT = null;
			 strMFRDT = cl_dat.M_strHELP_pbst.replace('|','~').split("~");
			 txtMFRCD.setText(strMFRDT[0]);
			 txtMFRNM.setText(strMFRDT[1]);
			 txtMFADD.setText(strMFRDT[2]);
			 if(strMFRDT.length>3 && strMFRDT[3].length()>1 )
				 txtMFCTY.setText(strMFRDT[3]); 
		}
		else if(M_strHLPFLD.equals("txtVENCD"))
		{
			 String[] strVENDT = null;
			 strVENDT = cl_dat.M_strHELP_pbst.replace('|','~').split("~");
			 txtVENCD.setText(strVENDT[0]);
			 txtVENAM.setText(strVENDT[1]);
			 txtVEADD.setText(strVENDT[2]);
			 if(strVENDT.length>3 && strVENDT[3].length()>1 )
				 txtVECTY.setText(strVENDT[3]); 	 
		}
		/*else if(M_strHLPFLD.equals("txtMATGR"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtMATGR.setText(L_STRTKN.nextToken());
			 txtMATDS.setText(L_STRTKN.nextToken());
		}*/
		else if(M_strHLPFLD.equals("txtPORNO"))
		{
			String [] strPODTA = null;
			strPODTA = cl_dat.M_strHELP_pbst.replace('|','~').split("~");
			txtPORNO.setText(strPODTA[1]);
			txtPORDT.setText(strPODTA[2]);
			txtPORVL.setText(strPODTA[5]);
		}
 	}
 	catch(Exception L_EX)
 	{
 		setMSG(L_EX,"exeHLPOK"); 
 	}
  }
  void clrEDITR(cl_JTable tblTABLE)
  {
	  if(tblTABLE.isEditing())
	  tblTABLE.getCellEditor().stopCellEditing();
	  tblTABLE.setRowSelectionInterval(0,0);
	  tblTABLE.setColumnSelectionInterval(0,0);	
  }
         
  void getDATA()
  {
	  String L_strMFECD ="";
	  String L_strVENCD ="";
	  try
	  {
	     // clrCOMP();
		  M_strSQLQRY = "SELECT TG_TAGDS,TG_PLNCD,TG_ARACD,TG_CRICD,TG_INSDT,TG_DRGNO,TG_PORNO,TG_PORDT,TG_PORVL,TG_MFRCD,TG_PRTCD"; 
		  M_strSQLQRY+=	" FROM PM_TGMST";
		  M_strSQLQRY+=	" where TG_TAGNO ='"+txtTAGNO.getText()+"'";
		  M_strSQLQRY+= " AND isnull(TG_STSFL,'')<>'X'"; 
		  M_strSQLQRY+= " AND TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
		  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		  //System.out.println("getData"+M_strSQLQRY);
		  
		  if(M_rstRSSET !=null)                           
		  { 
			 while(M_rstRSSET.next())
			 {
				 txtTAGDS.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGDS"),""));
				 txtPLNCD.setText(nvlSTRVL(M_rstRSSET.getString("TG_PLNCD"),""));
				 txtARACD.setText(nvlSTRVL(M_rstRSSET.getString("TG_ARACD"),""));		
				 txtCRICD.setText(nvlSTRVL(M_rstRSSET.getString("TG_CRICD"),""));
				
				 txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				 txtARADS.setText(getCDTRN("MSTPMXXARA"+M_rstRSSET.getString("TG_ARACD").trim(),"CMT_CODDS",hstCDTRN));	
				 txtCRIDS.setText(getCDTRN("MSTPMXXCRT"+M_rstRSSET.getString("TG_CRICD").trim(),"CMT_CODDS",hstCDTRN));
				 
				 if(!(M_rstRSSET.getDate("TG_INSDT")==null))
					 txtINSDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("TG_INSDT")));
				 
				 txtDRGNO.setText(nvlSTRVL(M_rstRSSET.getString("TG_DRGNO"),""));
				 
				 txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("TG_PORNO"),""));
				 if(!(M_rstRSSET.getDate("TG_PORDT")==null))
					 txtPORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("TG_PORDT")));
				 txtPORVL.setText(nvlSTRVL(M_rstRSSET.getString("TG_PORVL"),""));
				 
				 txtMFRCD.setText(nvlSTRVL(M_rstRSSET.getString("TG_MFRCD"),""));
				 txtMFRNM.setText(getPTMST(txtMFRCD.getText(),"PT_PRTNM"));
				 txtMFADD.setText(getPTMST(txtMFRCD.getText(),"PT_ADR01"));
				 txtMFCTY.setText(getPTMST(txtMFRCD.getText(),"PT_CTYNM"));
				 
				 txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("TG_PRTCD"),""));
				 txtVENAM.setText(getPTMST(txtVENCD.getText(),"PT_PRTNM"));
				 txtVEADD.setText(getPTMST(txtVENCD.getText(),"PT_ADR01"));
				 txtVECTY.setText(getPTMST(txtVENCD.getText(),"PT_CTYNM")); 
				 
		     }
			 if(M_rstRSSET != null)
			 {	
				M_rstRSSET.close();			
			 }
		 } 
	  }	
	  catch(Exception L_E)
	  {
		setMSG(L_E,"getDATA");
	  }
  }
  
  void getEQPDL()
  {
	  try
	  {
		  int L_CNT=0;
		  M_strSQLQRY = "select EQ_TAGNO,EQ_EQPID,EQ_EQPDS from PM_EQMST where";
		  M_strSQLQRY+= " EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and EQ_TAGNO ='"+txtTAGNO.getText()+"'";
		  M_strSQLQRY+= " And isnull(EQ_STSFL,'')<>'X'"; 
		  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		  //System.out.println("getEQPDL"+M_strSQLQRY);
		  if(M_rstRSSET !=null)                           
		  { 
			 while(M_rstRSSET.next())
			 {
				 tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_EQPID"),""),L_CNT,TB1_EQPID);
				 tblEQPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EQ_EQPDS"),""),L_CNT,TB1_EQPDS);
				 L_CNT++;
		     }	
		  }
		  M_rstRSSET.close();		
	  }	
	  catch(Exception L_E)
	  {
		setMSG(L_E,"getEQPDL");
	  }
  }
   
 	
  /** Method to validate data  */
  boolean vldDATA()
  {
	try
	{
		 
		if(txtTAGNO.getText().trim().length() ==0)
		{
			txtTAGNO.requestFocus();
			setMSG("Enter the TAG No.",'E');
			return false;
		}
		else if(txtTAGDS.getText().trim().length() ==0)
		{
			txtTAGDS.requestFocus();
			setMSG("Enter the TAG Description",'E');
			return false;
		}
		else if(txtPLNCD.getText().trim().length() ==0)
		{
			txtPLNCD.requestFocus();
			setMSG("Enter Plant Code",'E');
			return false;
		}
		else if(txtARACD.getText().trim().length() ==0)
		{
			txtARACD.requestFocus();
			setMSG("Enter Area Code",'E');
			return false;
		}
		else if(txtCRICD.getText().trim().length() ==0)
		{
			txtCRICD.requestFocus();
			setMSG("Enter Criticality",'E');
			return false;
		}
		else if(txtINSDT.getText().trim().length() ==0)
		{
			txtINSDT.requestFocus();
			setMSG("Enter Installed Date",'E');
			return false;
		}
		else if(txtINSDT.getText().length() >0 && txtPORDT.getText().length() >0)
		{
			if(M_fmtLCDAT.parse(txtINSDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtPORDT.getText().toString()))<0)
			{
				setMSG("Installed date date can't be smaller than Purchase order",'E');
				txtPORDT.requestFocus();
				return false;
			}
		}
		else if(txtDRGNO.getText().trim().length() ==0)
		{
			txtDRGNO.requestFocus();
			setMSG("Enter Drawing No.",'E');
			return false;
		}
		else if(txtPORNO.getText().trim().length() ==0)
		{
			txtPORNO.requestFocus();
			setMSG("Enter Purchase Order No.",'E');
			return false;
		}
		else if(txtPORDT.getText().trim().length() ==0)
		{
			txtPORDT.requestFocus();
			setMSG("Enter Purchase Order Date",'E');
			return false;
		}
		else if(txtPORVL.getText().trim().length() ==0)
		{
			txtPORVL.requestFocus();
			setMSG("Enter Purchase Order Value",'E');
			return false;
		}
		else if(txtMFRCD.getText().trim().length() ==0)
		{
			txtMFRCD.requestFocus();
			setMSG("Enter Manufracturer Code",'E');
			return false;
		}
		else if(txtVENCD.getText().trim().length() ==0)
		{
			txtVENCD.requestFocus();
			setMSG("Enter Vendor Code",'E');
			return false;
		}
	}	  
  	catch(Exception L_EX)
  	{
  	  setMSG(L_EX,"This is vldDATA");
  	}	
  	return true;
  }
  
  void exeSAVE()
  {
    try
    {
	    if(!vldDATA())
	  	{
		  cl_dat.M_btnSAVE_pbst.setEnabled(true);
		  return;
	  	} 
        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
  	    {
          exeMODREC();	
  	    }
    	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
  	    {
    	  exeDELREC();	
  	    }    	   	        			
     	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
        {
     	  exeADDREC(); 
        }	
     	if(cl_dat.exeDBCMT("exeSAVE"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG(" Data Saved Successfully..",'N'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				setMSG(" Data Modified Successfully..",'N'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Data Deleted Successsfully ..",'N');
			txtTAGNO.setText("");
     		clrCOMP();
		}
		else
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG("Error in saving details..",'E'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			   setMSG("Error in Modified Data details..",'E'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Error in Deleting data..",'E');
		}
     }
   	 catch(Exception L_E)
	 {
   		 	setMSG(L_E,"exeSAVE");
	 }
  }
  
  /** Method to insert data  */
  private void exeADDREC()
  { 
	  try
	  {
		 
		M_strSQLQRY =" insert into PM_TGMST(TG_TAGNO,TG_TAGDS,TG_PLNCD,TG_ARACD,TG_CRICD,TG_INSDT,TG_DRGNO,TG_PORNO,TG_PORDT,TG_PORVL,TG_MFRCD,TG_PRTCD,TG_LUPDT,TG_LUSBY,TG_STSFL,TG_TRNFL,TG_CMPCD,TG_SBSCD)";
		M_strSQLQRY += " values (";
		M_strSQLQRY += "'"+txtTAGNO.getText().toString()+"',";
		M_strSQLQRY += "'"+txtTAGDS.getText().toString().trim().toUpperCase()+"',";
		M_strSQLQRY += "'"+txtPLNCD.getText().toString()+"',";
		M_strSQLQRY += "'"+txtARACD.getText().toString()+"',";
		M_strSQLQRY += "'"+txtCRICD.getText().toString()+"',";
		if(txtINSDT.getText().length()>0)
		M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINSDT.getText()))+"',";
		if(txtDRGNO.getText().length()>0)
		M_strSQLQRY += "'"+txtDRGNO.getText().toString()+"',";
		if(txtPORNO.getText().length()>0)
		M_strSQLQRY += "'"+txtPORNO.getText().toString()+"',";
		if(txtPORDT.getText().length()>0)
		M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPORDT.getText()))+"',";
		if(txtPORVL.getText().length()>0)
		M_strSQLQRY += "'"+txtPORVL.getText().toString()+"',";
		if(txtMFRCD.getText().length()>0)
		M_strSQLQRY += "'"+txtMFRCD.getText().toString()+"',";
		if(txtVENCD.getText().length()>0)
		M_strSQLQRY += "'"+txtVENCD.getText().toString()+"',";
		M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
		M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
		M_strSQLQRY += "'1',";
		M_strSQLQRY += "'0',";
		M_strSQLQRY += "'01',";
		M_strSQLQRY += "'"+M_strSBSCD+"')";
		cl_dat.M_flgLCUPD_pbst = true;
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		//System.out.println("insert"+M_strSQLQRY);
	  }
	  catch(Exception L_EX)
	  {
		  cl_dat.M_flgLCUPD_pbst=false;
		  setMSG(L_EX,"exeADDREC()"); 
	  }
  }
  /**Method to modify pm_tgmst records**/
  private void exeMODREC()
  {
	  try
	  {
		M_strSQLQRY = " Update PM_TGMST set";
		M_strSQLQRY +=" TG_TAGDS ='"+txtTAGDS.getText().toString().trim().toUpperCase()+"',";
		M_strSQLQRY +=" TG_PLNCD ='"+txtPLNCD.getText()+"',";
		M_strSQLQRY +=" TG_ARACD ='"+txtARACD.getText()+"',";
		M_strSQLQRY +=" TG_CRICD ='"+txtCRICD.getText()+"',";
		M_strSQLQRY +=" TG_INSDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINSDT.getText()))+"',";
		M_strSQLQRY +=" TG_DRGNO ='"+txtDRGNO.getText()+"',";
		M_strSQLQRY +=" TG_PORNO ='"+txtPORNO.getText()+"',";
		M_strSQLQRY +=" TG_PORDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPORDT.getText()))+"',";
		M_strSQLQRY +=" TG_PORVL ='"+txtPORVL.getText()+"',";
		M_strSQLQRY +=" TG_MFRCD ='"+txtMFRCD.getText()+"',";
		M_strSQLQRY +=" TG_PRTCD ='"+txtVENCD.getText()+"',";
		M_strSQLQRY +=" TG_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
		M_strSQLQRY +=" TG_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
		M_strSQLQRY +=" where TG_TAGNO ='"+txtTAGNO.getText()+"'";
		M_strSQLQRY +=" and TG_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
		//System.out.println(">>>update>>"+M_strSQLQRY);
		cl_dat.M_flgLCUPD_pbst = true;
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	  }
	  catch(Exception L_EX)
	  {
		  	setMSG(L_EX,"exeMODREC()");
	  }
  }
 
  /** Method to delete data  */
  private void exeDELREC()
  {
	  try
	  {
		M_strSQLQRY="update PM_TGMST set";
		M_strSQLQRY+=" TG_STSFL = 'X'";
		M_strSQLQRY+=" where TG_TAGNO ='"+txtTAGNO.getText()+"'";
		M_strSQLQRY+=" and TG_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
		cl_dat.M_flgLCUPD_pbst = true;
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
		//System.out.println(">>>delete>>"+M_strSQLQRY);
	  }  
	  catch(Exception L_EX)
	  {
		  setMSG(L_EX,"exeDELREC()");		
	  }
  }
  
 
 
	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
		if (LP_FLDTP.equals("C"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
			//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
		else if (LP_FLDTP.equals("N"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
		else if (LP_FLDTP.equals("D"))
			return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
		else if (LP_FLDTP.equals("T"))
		    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
		else 
			return " ";
		}
		catch (Exception L_EX)
		{setMSG(L_EX,"getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);}
	return " ";
	} 
	
	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	 */
     private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
     {
		String L_strSQLQRY = "";
        try
        {
	        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")   "+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
		  //System.out.println(L_strSQLQRY);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
	            //setMSG("Records not found in CO_CDTRN",'E');
                return;
            }
            while(true)
            {
                    strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                    strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                    strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                    String[] staCDTRN = new String[intCDTRN_TOT];
                    staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                    staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                 
                    LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
                    if (!L_rstRSSET.next())
                            break;
            }
            L_rstRSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtCDTRN");
        }
	}

	

	/** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
   private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
   {
	//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
	   try
	   {
				if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
					{setMSG(LP_CDTRN_KEY+" not found in CO_CDTRN hash table",'E'); return " ";}
	           if (LP_FLDNM.equals("CMT_CODCD"))
	                   return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
	           else if (LP_FLDNM.equals("CMT_CODDS"))
	                   return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
	      
	   }
	   catch (Exception L_EX)
	   {
			setMSG(L_EX,"getCDTRN");
	   }
	   return "";
   }
   
   

	/** One time data capturing for Distributors
	 * into the Hash Table
	 */
	 private void crtPTMST()
	 {
		String L_strSQLQRY = "";
	    try
	    {
	        hstPTMST.clear();
			L_strSQLQRY= "Select PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM FROM CO_PTMST where";
			L_strSQLQRY+= " PT_PRTTP='S' AND isnull(PT_STSFL,'')<>'X'";
			L_strSQLQRY+= " order by PT_PRTCD";
			//System.out.println("PTMST"+L_strSQLQRY);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
                 setMSG("Distributors not found in CO_PTMST",'E');
                  return;
	        }
	        while(true)
	        {
	            strPRTCD = getRSTVAL(L_rstRSSET,"PT_PRTCD","C");
	            String[] staPTMST = new String[intPTMST_TOT];
	            staPTMST[intAE_PT_PRTNM] = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
	            staPTMST[intAE_PT_ADR01] = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
	            staPTMST[intAE_PT_CTYNM] = getRSTVAL(L_rstRSSET,"PT_CTYNM","C");
	            hstPTMST.put(strPRTCD,staPTMST);
	                       
	            if(!L_rstRSSET.next())
	            		break;
	        }
	            L_rstRSSET.close();
	     }
	     catch(Exception L_EX)
	     {
	        setMSG(L_EX,"crtPTMST");
	     }
	     return;
	 }
	
	 /** Picking up Distributor Details
		 * @param LP_PRTCD		Party Code 
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
     private String getPTMST(String LP_PRTCD, String LP_FLDNM)
     {
	     String L_RETSTR = "";
	     try
	     {
	             String[] staPTMST = (String[])hstPTMST.get(LP_PRTCD);
	             if (LP_FLDNM.equals("PT_PRTNM"))
	                     L_RETSTR = staPTMST[intAE_PT_PRTNM];
	             else if (LP_FLDNM.equals("PT_ADR01"))
	                     L_RETSTR = staPTMST[intAE_PT_ADR01];
	             else if (LP_FLDNM.equals("PT_CTYNM"))
	                     L_RETSTR = staPTMST[intAE_PT_CTYNM];    
	     }
		 catch (Exception L_EX)
		 {
				setMSG(L_EX,"getPTMST");
		 }
		 return L_RETSTR;
     }

  
  /**
	 * Method to clear data*/
	void clrCOMP()
	{
		try
		{
			txtTAGDS.setText("");
			txtPLNCD.setText("");
			txtARACD.setText("");
			txtCRICD.setText("");
			txtPLNDS.setText("");
			txtARADS.setText("");
			txtCRIDS.setText("");
			txtINSDT.setText("");
			txtDRGNO.setText("");
			txtPORNO.setText(""); 
			txtPORDT.setText("");
			txtPORVL.setText("");
			txtMFRCD.setText("");
			txtVENCD.setText("");
			txtMFADD.setText("");
			txtVEADD.setText("");
			txtMFRNM.setText("");
			txtVENAM.setText("");
			txtMFCTY.setText("");
			txtVECTY.setText("");
			tblEQPDL.clrTABLE();
			clrEDITR(tblEQPDL);
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
	
	/**Verify to enter valid data***/
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{    
			    if(input == txtTAGNO)
				{	
					try
					{
						String L_strTAGNO="";
						clrCOMP();
						if(txtTAGNO.getText().length()==0)
						{
							setMSG("Enter Tag No..",'E');
							txtTAGNO.requestFocus();
							return false;
						}
						
						String strTAGNO =txtTAGNO.getText().toString();
						M_strSQLQRY=" SELECT TG_TAGNO,TG_TAGDS,TG_PLNCD,TG_ARACD,TG_CRICD from PM_TGMST where TG_TAGNO='"+txtTAGNO.getText().toUpperCase()+"'";
						M_strSQLQRY+= " AND TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
						//System.out.println("INPVF TAGNO"+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							  L_strTAGNO=nvlSTRVL(M_rstRSSET.getString("TG_TAGNO"),"");
							  if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							  {	 
								txtTAGNO.setText(L_strTAGNO);
								txtTAGDS.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGDS"),""));
								txtPLNCD.setText(nvlSTRVL(M_rstRSSET.getString("TG_PLNCD"),""));
								txtARACD.setText(nvlSTRVL(M_rstRSSET.getString("TG_ARACD"),""));
								txtCRICD.setText(nvlSTRVL(M_rstRSSET.getString("TG_CRICD"),""));
							
								getDATA();
								
							  }
							  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
								  getEQPDL();
						}
						else
						{
							  if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							  {	  
								setMSG("Enter valid Tag No ",'E');
								txtTAGNO.requestFocus();
								return false;
							  }	
						}
						M_rstRSSET.close();
						
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{	  
							if(strTAGNO.equals(L_strTAGNO))
							{
								setMSG("This Tag No. Already exist",'E');
								txtTAGNO.requestFocus();
								return false;
							}
						}		
					}	
					catch(Exception e)
					{
						setMSG(e,"error in Tag No InputVerifier  ");
					}
				}
				    
				if(((JTextField)input).getText().length() == 0)
						return true;
				
				else if(input == txtPLNCD)
				{	
					try
					{
						txtPLNCD.setText(txtPLNCD.getText().toUpperCase());
						if(!hstCDTRN.containsKey("MSTPMXXPLN"+txtPLNCD.getText().toString().trim().toUpperCase()))
						{
							setMSG("Enter valid Plant Code ",'E');
							return false;	
						}
						else
							txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim().toUpperCase(),"CMT_CODDS",hstCDTRN));
					}
					catch(Exception e)
					{
						setMSG(e,"error in Plant Cod InputVerifier ");
					}

				}
				else if(input == txtARACD)
				{	
					try
					{
						txtARACD.setText(txtARACD.getText().toUpperCase());
						if(!hstCDTRN.containsKey("MSTPMXXARA"+txtARACD.getText().toString().trim().toUpperCase()))
						{	
							setMSG("Enter valid Area Code ",'E');
						    return false;
						}   
						else
							txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim().toUpperCase(),"CMT_CODDS",hstCDTRN));
							
					}
					catch(Exception e)
					{
						setMSG(e,"error in Area Code InputVerifier");
					}

				}
				else if(input == txtCRICD)
				{	
					try
					{
						txtCRICD.setText(txtCRICD.getText().toUpperCase());
						if(!hstCDTRN.containsKey("MSTPMXXCRT"+txtCRICD.getText().toString().trim().toUpperCase()))
						{
							setMSG("Enter valid Criticality Code ",'E');
							return false;
						}
						else
							txtCRIDS.setText(getCDTRN("MSTPMXXCRT"+txtCRICD.getText().toString().trim().toUpperCase(),"CMT_CODDS",hstCDTRN));
					}
					catch(Exception e)
					{
						setMSG(e,"error in Criticality Code InputVerifier");
					}

				}
				else if(input == txtINSDT)
				{	
					if(txtINSDT.getText().toString().length()== 0)
					{
						setMSG("Enter Installed Date..",'E');
						txtINSDT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtINSDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("Installed Date can't be greater than current date..",'E');
						txtINSDT.requestFocus();
						return false;
					}
				}     
				else if(input == txtPORDT)
				{	
					if(txtPORDT.getText().toString().length()== 0)
					{
						setMSG("Enter Purchase Order Date..",'E');
						txtPORDT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtPORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("Purchase order date can't be greater than current date",'E');
						txtPORDT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtPORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtINSDT.getText().toString()))>0)
					{
						setMSG("Purchase order date can't be greater than Installed date",'E');
						txtPORDT.requestFocus();
						return false;
					}
				}                
				else if(input == txtMFRCD)
				{	
					try
					{
						if(!hstPTMST.containsKey(txtMFRCD.getText().toString().trim()))
						{
							setMSG("Enter valid Mfr.Code ",'E');
							return false;
						}
						else
						{
							txtMFRNM.setText(getPTMST(txtMFRCD.getText().toString().trim(),"PT_PRTNM"));
							txtMFADD.setText(getPTMST(txtMFRCD.getText().toString().trim(),"PT_ADR01"));
							txtMFCTY.setText(getPTMST(txtMFRCD.getText().toString().trim(),"PT_CTYNM"));
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Mfr.Code InputVerifier");
					}
				}
				else if(input == txtVENCD)
				{	
					try
					{
						if(!hstPTMST.containsKey(txtVENCD.getText().toString().trim()))
						{
							setMSG("Enter valid Vendor Code ",'E');
							return false;
						}
						else
						{
							txtVENAM.setText(getPTMST(txtVENCD.getText().toString().trim(),"PT_PRTNM"));
							txtVEADD.setText(getPTMST(txtVENCD.getText().toString().trim(),"PT_ADR01"));
							txtVECTY.setText(getPTMST(txtVENCD.getText().toString().trim(),"PT_CTYNM"));
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Mfr.Code InputVerifier");
					}
				}   
				/*else if(input == txtMATGR)
				{	
					try{
						if(txtMATGR.getText().length()>0)
						{
							M_strSQLQRY=" select ct_matcd,ct_matds from co_ctmst where  substr(ct_matcd,3,8) = '0000000A' and isnull(CT_STSFL,'') <> 'X'";
							M_strSQLQRY += " AND ct_matcd ='"+txtMATGR.getText().toUpperCase()+"'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtMATGR.setText(txtMATGR.getText());
								txtMATDS.setText(nvlSTRVL(M_rstRSSET.getString("ct_matds"),""));	
							}
							else
							{
								setMSG("Enter valid Material Group ",'E');
								return false;
							}
							M_rstRSSET.close();
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Material Group InputVerifier");
					}
				}*/
				else if(input == txtPORNO)
		    	{
					/*if(txtMATGR.getText().length()==0)
					{
						setMSG("Enter Material Group..",'E');
						txtMATGR.requestFocus();
						return false;
					}*/
					if(txtPORNO.getText().length()>0)
					{
						M_strSQLQRY=" select po_strtp,po_porno,po_pordt,po_porqt,po_porrt,po_porvl from mm_pomst where po_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' AND po_porno = '"+txtPORNO.getText().trim()+"'";
						//M_strSQLQRY += " AND po_matcd ='"+txtMATGR.getText().trim()+"'";
						//System.out.println("INPVF  P.O.NO : "+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							txtPORNO.setText(txtPORNO.getText());
							txtPORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("po_pordt")));
							txtPORVL.setText(nvlSTRVL(M_rstRSSET.getString("po_porvl"),""));
						}
						else
						{
							setMSG("Enter valid P.O.No..",'E');
							return false;
						}
						M_rstRSSET.close();
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
 