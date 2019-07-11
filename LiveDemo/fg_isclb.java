/*
System Name   : Finished Goods Inventory Management System
Program Name  : Issue Callback Provision
Program Desc. : Issues being Authorized are Released for changes within the Loading Advice.
Author        : Mr. Deepal N. Mehta
Date          : 10th July 2001
Version       : FIMS 1.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class fg_isclb extends cl_rbase implements MouseListener
{
	JTextField txtTRNTP,txtISSNO;
	JOptionPane LM_OPTNPN;
	JRadioButton rdbDRISS,rdbIDISS;
	JComboBox cmbREFCALL;
	JLabel lblTRANT,lblISSUE;
	ButtonGroup chkGRP;
	//JCheckBox chkREFRCT,chkREFISS;
	JButton btnRUN,btnCLEAR;
	String LM_ACTTXT,strTRNTP,strISSNO,strCODDS,strLOTNO,strRCLNO,strMNLCD,strCODCD,strPRODCD;
	String strPRDCD,strISAQT,strPRDTP,strPRDDS,strPKGTP,strPKGCT,strISSTP,strWRHTP,strISSQT;
	String LM_SQLSTR="";
	//String LM_YRDGT = cl_dat.ocl_dat.M_FNANYR.substring(3,4);
	int intCOUNT; //used to determine whether the records are present or not within the Marketing database table
	int LM_UPDCTR =0;
	int LM_UPDCNT = 0; //used as an counter to determine whether the records are present or not within the Marketing Database Table.
	int LM_SMIPK; //used to determine the total sum of issue packages while printing the D.O
	int i,j,k;
	boolean LM_RMTCHK; //Remote Updation Flag
	boolean flgDIRFL = false; //Direct Flag
	ResultSet LM_RSLSET;							
	
	/**Constructor for the form<br>
	 * to create the Buttons and radioButton 
	 * 
	 */
	fg_isclb()
	{
		super(2);
		try{	
			
			LM_OPTNPN = new JOptionPane();
			chkGRP = new ButtonGroup();
			
			add(rdbDRISS =new JRadioButton("Direct Issue",true),3,4,1,1,this,'L');
			add(rdbIDISS =new JRadioButton("Indirect Issue",false),4,4,1,1.5, this,'L');
			add(cmbREFCALL=new JComboBox(),2,2,1,1.2,this,'L');
			cmbREFCALL.addItem("Catagoery");
			cmbREFCALL.addItem("Recall");
			cmbREFCALL.addItem("Refresh");
			
			chkGRP.add(rdbDRISS);
			chkGRP.add(rdbIDISS);
			add(txtTRNTP=new JTextField(),5,4,1,1,this,'L');	
			add(txtISSNO=new JTextField(),6,4,1,1,this,'L');
			add(lblTRANT=new JLabel("Tran. Type"),5,3,1,1,this,'L');
			add(lblISSUE= new JLabel("Issue No."),6,3,1,1,this,'L');
			add(btnRUN=new JButton("RUN"),2,3,1,0.7,this,'R');
			add(btnCLEAR=new JButton("CLEAR"),6,6,1,1,this,'R');
			
			//add(chkREFRCT = new JCheckBox("Refresh Receipt",false),3,6,1,1.5,this,'R');
			//add(chkREFISS = new JCheckBox("Refresh Issue",false),4,6,1,1.5,this,'R');
			
			//exeINTSTA('D');
			rdbDRISS.setVisible( false);
			rdbIDISS.setVisible( false);
			btnCLEAR.setVisible(false);
			 rdbDRISS.setEnabled(false);
	         rdbIDISS.setEnabled( false);
	         txtTRNTP.setEnabled( false);
	         txtISSNO.setEnabled(false );
	         btnRUN.setEnabled( false);
	      //   btnCLEAR.setEnabled( false);
	         //chkREFRCT.setEnabled( false);
	         //chkREFISS.setEnabled( false);
	         cmbREFCALL.setEnabled( false);
	         rdbDRISS.addMouseListener( this);
	         rdbIDISS.addMouseListener(this);
	         cmbREFCALL.addMouseListener(this);
	         //chkREFRCT.addMouseListener(this);
	         //chkREFISS.addMouseListener(this);
	         
	         cl_dat.M_flgHELPFL_pbst = false;
			
	}catch(Exception L_EX)
	{
			setMSG(L_EX," Constructor fg_isclb");
	}	
}
	/**@void  This Function for set the buttons and text fields enabled and disbled  
	 * */
	void setENBL(boolean L_flgSTAT)
	{   
         super.setENBL(L_flgSTAT);
         rdbDRISS.setEnabled(false);
         rdbIDISS.setEnabled(false);
         txtTRNTP.setEnabled(false);
         txtISSNO.setEnabled(false);
         btnRUN.setEnabled( false);
         btnCLEAR.setEnabled(true);
         //chkREFRCT.setEnabled( true);
         //chkREFISS.setEnabled(true);
         cmbREFCALL.setEnabled( false);
         if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
		       
		        rdbDRISS.setEnabled(false);
		        rdbIDISS.setEnabled(false);
		         txtTRNTP.setEnabled(false);
		         txtISSNO.setEnabled(false );
		         btnRUN.setEnabled(false);
		         btnCLEAR.setEnabled( false);
		         //chkREFRCT.setEnabled( false);
		         //chkREFISS.setEnabled( false);
		          txtTRNTP.setVisible(true);
		          txtISSNO.setVisible(true);
		          rdbIDISS.setVisible(false);
		          rdbDRISS.setVisible(false);
			      txtTRNTP.setVisible(true);
			     txtISSNO.setVisible(true );
			     //chkREFRCT.setVisible( true);
		         //chkREFISS.setVisible( true);
		         lblTRANT.setVisible( true);
		         lblISSUE.setVisible( true); 
		         cmbREFCALL.setEnabled( true);
		         
		         
			}
        
	
		
         
        
	}
	/*private void exeINTSTA(char LP_DSBFL)
	{ //Clears all the data
		txtTRNTP.setText("");
		txtISSNO.setText("");
		if(LP_DSBFL=='D')
			{
			btnRUN.setEnabled(false);
			txtTRNTP.setEnabled(false);
			}
		}*/
	/** We perform the actions on the buttons
	 * */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		
		try
		{
		    if(M_objSOURC ==cmbREFCALL)
			{    
		   
				
		  	
					if(cmbREFCALL.getSelectedItem().toString().equals("Recall"))
					{
					    
					    setMSG(" You Selected Recall ",'N'); 
					     rdbDRISS.setVisible(false);
				         rdbIDISS.setVisible(false);
				         btnCLEAR.setEnabled( true);
				         //chkREFRCT.setVisible( false);
				         //chkREFISS.setVisible( false);
				         txtTRNTP.setVisible(true);
					     txtISSNO.setVisible(true );
					     lblTRANT.setVisible( true);
				         lblISSUE.setVisible( true);
				         rdbDRISS.setEnabled(false);
				         rdbIDISS.setEnabled(false);
				         txtTRNTP.setEnabled(true);
				         txtISSNO.setEnabled(true);
				         txtTRNTP.requestFocus();
					    
					}
					if(cmbREFCALL.getSelectedItem().toString().equals("Refresh"))
					{
					    setMSG(" You Selected Refresh",'N');
					    rdbDRISS.setVisible(false);
					    rdbIDISS.setVisible( false);
					    btnCLEAR.setVisible(false);
					    btnRUN.setEnabled(true);
					    txtTRNTP.setVisible(false);
				         txtISSNO.setVisible(false );
				         //chkREFRCT.setVisible(true);
				         //chkREFISS.setVisible(true);
				         //chkREFRCT.setEnabled(true);
				         
				         //chkREFISS.setEnabled( true);
				         txtTRNTP.setEnabled(false);
				         txtISSNO.setEnabled(false);
				         
				         lblTRANT.setVisible( false);
				         lblISSUE.setVisible( false);
				         
					   
					    
					}
					
				}	
		
					
			if(M_objSOURC == btnRUN)
			{
			    if(cmbREFCALL.getSelectedItem().toString().equals("Recall"))
				{
			        strISSNO = txtISSNO.getText().toString().trim();		
					strTRNTP = txtTRNTP.getText().toString().trim();		
					cl_dat.M_flgLCUPD_pbst = true;
					LM_RMTCHK = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					if(flgDIRFL)
				        exeAUTRLS();
					else
						exeIDRLS();
				}
			    if(cmbREFCALL.getSelectedItem().toString().equals("Refresh"))
				{
			    
			        exeRefresh();
				}    
					
			}
			if(M_objSOURC== btnCLEAR)
			{
			    txtISSNO.setText("");
			    txtTRNTP.setText("");
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Action Performed");
		}
		
	 }
	/**
	 * This function Used for the ExeRefresh Refresh Receipt & Refresh Issue 
	 *
	 */
	private void exeRefresh()
	{
	    this.setCursor(cl_dat.M_curWTSTS_pbst);
		getDSPMSG("Before");
		cl_dat.M_flgLCUPD_pbst = true;
		setMSG("Decreasing Stock Master",'N');
		decSTMST();
		setMSG("Updating Stock Master",'N');
		exeSTMST();
		updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXRCT");	
		updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS");
		/*if(chkREFRCT.isSelected())
		{
			setMSG("Refreshing Receipt Masters.",'N');
			
		}
		if(chkREFISS.isSelected())
		{
			setMSG("Refreshing Issue Masters.",'N');
			
		}*/
		getDSPMSG("After");
		if(cl_dat.M_flgLCUPD_pbst)
			setMSG("Data refreshed successfully",'N');
		else
			setMSG("Data not refreshed successfully",'E');
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	
	}
	/**This Keypressed is used for the perform the enter and f1 operations on TextFields
	 * */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed( L_KE);
		try
		{
			if((L_KE.getKeyCode() ==9 ) ||(L_KE.getKeyCode() == KeyEvent.VK_ENTER) )
			{
				if(M_objSOURC== txtTRNTP)
				{
					vldTRNTP();
					
				}
				if(M_objSOURC== txtISSNO)
				{
					vldISSNO();
					
					
					
				}
				
			}
			if(L_KE.getKeyCode() ==KeyEvent.VK_F1)
			{
				if(M_objSOURC== txtTRNTP)
				{
				    flgDIRFL=true;
					if(flgDIRFL)
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXMKT'";
					 // else
						//M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'FGXXITP' and CMT_CCSVL = '1'";
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTRNTP";
					cl_hlp(M_strSQLQRY,1,1,new String[]{ "Transaction Type.","Description"},2,"CT");
				}
				if(M_objSOURC ==txtISSNO)
				{
					if(flgDIRFL)
			            M_strSQLQRY = "Select distinct IVT_LADNO,IVT_LADDT from MR_IVTRN where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP='"+txtTRNTP.getText().toString().trim() +"' and IVT_STSFL in ('A','L','P','2') and isnull(ivt_ladqt,0)>0 and ivt_loddt is not null and isnull(IVT_INVNO,' ') = ' ' order by IVT_LADNO desc";
					else
						M_strSQLQRY = "Select distinct IST_ISSNO,IST_AUTDT from FG_ISTRN where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_ISSTP='"+txtTRNTP.getText() .toString().trim()+"' and IST_STSFL='2' order by IST_ISSNO desc";
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtISSNO";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Transaction No.","Date"},2,"CT");
				}
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Keypressed");
		}
	}
	/**This function used for the To Take the values into the DB display the Help Screen
	 * To perform actions on pressing enter or clicking on button "OK" 
	 * in help window Must be re-defined in chlid classes, with call to super at first. 
	 */
	
	 void exeHLPOK()
	 {
		   
		   super.exeHLPOK();
		   try
		   {
			 //  cl_dat.M_flgHELPFL_pbst = false;
			   if(M_strHLPFLD=="txtTRNTP")
			   {
			   	txtTRNTP.setText( cl_dat.M_strHLPSTR_pbst);
			   	btnRUN.setEnabled( false);
			   	
			   }
			   if(M_strHLPFLD=="txtISSNO")
			   {
			   	txtISSNO.setText( cl_dat.M_strHLPSTR_pbst);
			   	btnRUN.setEnabled(false) ;
			   }
		   }catch(Exception L_EX)
		   {
		   	setMSG(L_EX,"exeHLPOK");
		   }
	 }	
	 /** This function for the Mouse performons on RadioButtons*/
	 public void mouseClicked(MouseEvent L_ME)
	 {
	     //exeINTSTA('E');
	    
	     if(cmbREFCALL.getSelectedItem().toString().equals("Recall"))
		 {  
			//     if(M_objSOURC== rdbDRISS)
			     txtTRNTP.requestFocus();
	         		flgDIRFL = true;
					txtTRNTP.setEnabled(true);
					txtISSNO.setEnabled( true);
					btnRUN.setEnabled(false);
					
					
			     //}
			     /*if(M_objSOURC==rdbIDISS)
			     {
				    flgDIRFL = false;
					txtTRNTP.setEnabled(true);
					txtISSNO.setEnabled( true);
					btnRUN.setEnabled(false);
					txtTRNTP.requestFocus();
			     }*/
		 }
	     if(cmbREFCALL.getSelectedItem().toString().equals("Refresh"))
		 {
	         //if(chkREFRCT.isSelected())
	 		   btnRUN.setEnabled(true);
	         //if(chkREFISS.isSelected())
	 		  //btnRUN.setEnabled(true);    
	 		 
	         
		 }
	     
	}
/**
 * 	validates Market Type
 */
	private void vldTRNTP()
	{
		if(exeVDTRTP())
		{
			setMSG("Valid Market Type",'N');
			txtISSNO.requestFocus();		
		}else
		{
		setMSG("InValid Market Type",'E');
		txtTRNTP.requestFocus();
		}
	}
/**
 * 	validates Market Type with the database
 */
	private boolean exeVDTRTP() 
	{
		try{
			
			if(flgDIRFL)
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXMKT' and CMT_CODCD='"+txtTRNTP.getText().toString().trim()+"'";
			else
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'FGXXITP' and CMT_CCSVL = '1' and CMT_CODCD='"+txtTRNTP.getText().toString().trim()+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
					return true;
			}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"exeVDTRTP");
		}
		return false;
	}
	
	/**
	 * @return void
	 * Validates the Issue Number & makes the JTable enable to fetch the data.
	 * getALLREC() fetches the data into the Grade JTable for Direct Issues
	 * & getIDISRC() fetches the data into the Lot JTable for InDirect issues. 
	 */
	private void vldISSNO() //validates Issue number
	{
		if(exeVDISNO())
		{
			setMSG("Valid Issue No.",'N');
			btnRUN.setEnabled(true);
			btnRUN.requestFocus();
		}else
		{
		setMSG("InValid Issue No.",'E');
		txtISSNO.requestFocus();
		}
	}
	private boolean exeVDISNO() //validates Issue Number with the database
	{
		try
		{
			strTRNTP = txtTRNTP.getText().toString().trim();
			strISSNO = txtISSNO.getText().toString().trim();
			if(flgDIRFL)
                M_strSQLQRY = "Select distinct IVT_LADNO from MR_IVTRN where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP='"+txtTRNTP.getText().toString().trim()+"' and IVT_STSFL in ('A','L','P','2') and isnull(ivt_ladqt,0)>0 and ivt_loddt is not null and isnull(IVT_INVNO,' ') = ' ' and IVT_LADNO='"+txtISSNO.getText().toString().trim()+"'";
			else
                M_strSQLQRY = "Select distinct IST_ISSNO,IST_AUTDT from FG_ISTRN where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_ISSTP='"+txtTRNTP.getText().toString().trim()+"' and IST_STSFL='2' and IST_ISSNO='"+txtISSNO.getText().toString().trim()+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
					return true;
			}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"exeVDISNO");
		}
		return false;
	}
	/**
	 * @return void
	 * On Save Button click, Authorization procedure starts from here 
	 *  i.e data are inserted or updated into respective Tables
	 * For Direct Issues, Tables updated are FG_ISTRN,FG_STMST,FG_LCMST,PR_LTMST
	 * CO_PRMST,MR_IVTRN.
	 * For InDirect Issues, Tables updated are FG_ISTRN,FG_STMST
	 */
	private void exeAUTRLS()
	{ 
	  try
	  {
		setMSG("Updating Invoice Transaction table",'N');
		updIVTRN();
		//setMSG("Updating D.O. Delivery table",'N');
		//updDODEL();
		exeIDRLS();
	  }catch(Exception L_EX)
	  {
			setMSG(L_EX,"exeAUTRLS");
	  }
	}
	/**
	 * @return void
	 * On Save Button click, Authorization procedure starts from here 
	 *  i.e data are inserted or updated into respective Tables
	 * For Direct Issues, Tables updated are FG_ISTRN,FG_STMST,FG_LCMST,PR_LTMST
	 * CO_PRMST,MR_IVTRN.
	 * For InDirect Issues, Tables updated are FG_ISTRN,FG_STMST
	 */
	private void exeIDRLS()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				setMSG("Updating FG_ISTRN",'N');
				addISTRN();
				if(cl_dat.M_flgLCUPD_pbst)
				{
					cl_dat.exeDBCMT("exeSAVE");
				    this.setCursor(cl_dat.M_curDFSTS_pbst);
					LM_OPTNPN.showMessageDialog(this,"Loading Advice No. has been released Successfully.","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
					//exeINTSTA('D');
					setMSG(" ",'N');
				}
				else
				{
				 
				    this.setCursor(cl_dat.M_curDFSTS_pbst);
				LM_OPTNPN.showMessageDialog(this,"Loading Advice No. has not been released.","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
				setMSG("Release Failed .....",'E');
				}
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"exeIDRLS");
			}
		}
	}	
		
	
/**
 * 	Modifying Invoice Transaction i.e MR_IVTRN
 */
	private void updIVTRN()
	{ 
		try{
			M_strSQLQRY  = "Update mr_ivtrn set";
			M_strSQLQRY += " IVT_LADQT = 0,";
			M_strSQLQRY += " IVT_LODDT = null,";
			M_strSQLQRY += " IVT_INVQT = 0,";
			M_strSQLQRY += " IVT_INVPK = 0,";
			M_strSQLQRY += " IVT_STSFL = 'A',";
			M_strSQLQRY += " IVT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += " IVT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP = '"+txtTRNTP.getText() .toString() .trim()+"'";
			M_strSQLQRY += " and IVT_LADNO = '"+txtISSNO.getText().toString().trim()  +"'";
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			//System.out.println("update Ivtrn table :"+M_strSQLQRY);
			
		}catch(Exception L_EX){
			setMSG(L_EX,"updIVTRN");
		}
	}
	/**
	 * Inserting or Modifying Issue Transaction
	  */
	private void addISTRN()
	{
		
		if(cl_dat.M_flgLCUPD_pbst)
		{	
			try
			{
				M_strSQLQRY = "Select IST_WRHTP,IST_ISSTP,IST_PRDCD,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PKGTP,IST_PKGCT,IST_MNLCD,IST_ISSQT from FG_ISTRN where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_ISSNO='"+txtISSNO.getText().toString().trim() +"' and IST_STSFL='2'";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				while(M_rstRSSET.next())
				{
					strWRHTP = M_rstRSSET.getString("IST_WRHTP");
					strISSTP = M_rstRSSET.getString("IST_ISSTP");
					strPRDCD = M_rstRSSET.getString("IST_PRDCD");
					strPRDTP = M_rstRSSET.getString("IST_PRDTP");
					strLOTNO = M_rstRSSET.getString("IST_LOTNO");
					strRCLNO = M_rstRSSET.getString("IST_RCLNO");
					strPKGTP = M_rstRSSET.getString("IST_PKGTP");
					strMNLCD = M_rstRSSET.getString("IST_MNLCD");
					strPKGCT = M_rstRSSET.getString("IST_PKGCT");
					strISSQT = M_rstRSSET.getString("IST_ISSQT");
					//System.out.println(strISSQT);
					setMSG("Updating FG_LCMST",'N');
				//	chkLCMST();
					setMSG("Updating FG_STMST",'N'); 
					//chkSTMST();
					setMSG("Updating FG_ISTRN",'N');
					//System.out.println("updating");
					updISTRN();
					setMSG("Updating CO_PRMST",'N');
					chkPRMST();
					setMSG("Updating PR_LTMST",'N');
					chkLTMST();
					}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();	
			}catch(Exception L_EX)
			{
			setMSG(L_EX,"addISTRN");
			}
		}
	}
	/**
	 * 
	 *Modifies Issue Transaction i.e FG_ISTRN
	 */
	private void updISTRN() 
	{
		try
		{
			M_strSQLQRY = "Update FG_ISTRN set ";
			M_strSQLQRY += "IST_STSFL = '1',";
			M_strSQLQRY += "IST_TRNFL = '0',";
			M_strSQLQRY += "IST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "IST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst ))+"'";
			M_strSQLQRY += " where ist_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ist_wrhtp = '"+strWRHTP+"'";
			M_strSQLQRY += " and ist_isstp = '"+strISSTP+"'";
			M_strSQLQRY += " and ist_issno = '"+txtISSNO.getText().toString() .trim() +"'";
			M_strSQLQRY += " and ist_prdcd = '"+strPRDCD+"'";
			M_strSQLQRY += " and ist_prdtp = '"+strPRDTP+"'";
			M_strSQLQRY += " and ist_lotno = '"+strLOTNO+"'";
			M_strSQLQRY += " and ist_rclno = '"+strRCLNO+"'";
			M_strSQLQRY += " and ist_pkgtp = '"+strPKGTP+"'";
			M_strSQLQRY += " and ist_mnlcd = '"+strMNLCD+"'";
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			//System.out.println("update fgistrn table :"+M_strSQLQRY);
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"updISTRN");
		}
	}
	/**
	 * check the primary master table on pr_prdcd
	 *
	 */
	private void chkPRMST()
	{
		try
		{
			M_strSQLQRY = "Select count(*) from co_prmst";
			M_strSQLQRY += " where pr_prdcd = '"+strPRDCD+"'";
			if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
				updPRMST();
			else{
				setMSG("Record does not exist in CO_PRMST",'E');
				cl_dat.M_flgLCUPD_pbst = false;
				}
			//System.out.println(M_strSQLQRY);
	}catch(Exception L_EX){
			setMSG(L_EX,"chkPRMST");
		}
	}
//	Updating Classified Stk Qty. and/or Reserved Qty.
    //into the Product Master i.e CO_PRMST
	/**
	 * 
	 */
	private void updPRMST()
	{ 
		try
		{
			M_strSQLQRY = "Update CO_PRMST set ";
			M_strSQLQRY += "PR_CSTQT = PR_CSTQT + "+strISSQT+",";
			M_strSQLQRY += "PR_TRNFL = '0',";
			M_strSQLQRY += "PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "PR_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where pr_prdcd = '"+strPRDCD+"'";
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			//System.out.println("update COprmt table :"+M_strSQLQRY);
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"updPRMST");
		}
	}
	/**
	 * Check the master table pr_ltmst
	 *
	 */
	private void chkLTMST(){
		try{
			M_strSQLQRY = "Select count(*) from pr_ltmst";
			M_strSQLQRY += " where lt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and lt_prdtp = '"+strPRDTP+"'";
			M_strSQLQRY += " and lt_lotno = '"+strLOTNO+"'";
			M_strSQLQRY += " and lt_rclno = '"+strRCLNO+"'";
			//System.out.println("select prltmst :"+M_strSQLQRY);
			if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
				updLTMST();
			else
			{
				setMSG("Record does not exist in PR_LTMST",'E');
				//cl_dat.M_flgLCUPD_pbst = false;
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"chkLTMST");
		}
	}
/**
 * 	Updating Despatch Qty. into the Lot Master i.e PR_LTMST
 */
	
	private void updLTMST()  
	{
		try{
			M_strSQLQRY = "Update PR_LTMST set ";
			M_strSQLQRY += "LT_DSPQT = LT_DSPQT - "+strISSQT+",";
			M_strSQLQRY += "LT_TRNFL = '0',";
			M_strSQLQRY += "LT_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "LT_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst ))+"'";
			M_strSQLQRY += " where lt_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and lt_prdtp = '"+strPRDTP+"'";
			M_strSQLQRY += " and lt_lotno = '"+strLOTNO+"'";
			M_strSQLQRY += " and lt_rclno = '"+strRCLNO+"'";
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			//System.out.println("update pr_ltmst table :"+M_strSQLQRY);
			
		}catch(Exception L_EX){
			setMSG(L_EX,"setLCLUPD");
		}
	}
	private void chkLCMST()
	{
		try
		{
			M_strSQLQRY = "Select count(*) from fg_lcmst";
			M_strSQLQRY += " where lc_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and lc_wrhtp = '"+strWRHTP+"'";
			M_strSQLQRY += " and lc_mnlcd = '"+strMNLCD+"'";
			if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
				updLCMST();
			else{
				setMSG("Record does not exist in FG_LCMST",'E');
				cl_dat.M_flgLCUPD_pbst = false;
				}
			//System.out.println(M_strSQLQRY);
	}catch(Exception L_EX){
			setMSG(L_EX,"chkLCMST");
		}
	}
	
/**
 * 	Updating Stock Qty. into the Location Master i.e FG_LCMST
 */
	private void updLCMST(){ 
		try{
				M_strSQLQRY = "Update FG_LCMST set ";
				M_strSQLQRY += "LC_STKQT = LC_STKQT + "+strISSQT+",";
				M_strSQLQRY += "LC_TRNFL = '0',";
				M_strSQLQRY += "LC_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "LC_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY += " where lc_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and lc_wrhtp = '"+strWRHTP+"'";
				M_strSQLQRY += " and lc_mnlcd = '"+strMNLCD+"'";
				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				//System.out.println(M_strSQLQRY);
				
		}catch(Exception L_EX){
			setMSG(L_EX,"updLCMST");
		}
	}
	/** This is check Master fg_stmst
	 * */
	private void chkSTMST()
	{
		try
		{
			M_strSQLQRY = "Select count(*) from fg_stmst";
			M_strSQLQRY += " where st_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and st_wrhtp = '"+strWRHTP+"'";
			M_strSQLQRY += " and st_prdtp = '"+strPRDTP+"'";
			M_strSQLQRY += " and st_lotno = '"+strLOTNO+"'";
			M_strSQLQRY += " and st_rclno = '"+strRCLNO+"'";
			M_strSQLQRY += " and st_pkgtp = '"+strPKGTP+"'";
			M_strSQLQRY += " and st_mnlcd = '"+strMNLCD+"'";
			if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
				updSTMST();
			else{
				setMSG("Record does not exist in FG_STMST",'E');
				cl_dat.M_flgLCUPD_pbst = false;
				}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkSTMST");
		}
	}
/**
 * 	updating Stock Master i.e FG_STMST
 */
	private void updSTMST()
	{ 
		try
		{
			M_strSQLQRY = "Update FG_STMST set ";
			M_strSQLQRY += "ST_STKQT = ST_STKQT + "+strISSQT+",";
			M_strSQLQRY += "ST_ALOQT = ST_ALOQT + "+strISSQT+",";
			M_strSQLQRY += "ST_TRNFL = '0',";
			M_strSQLQRY += "ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where st_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and st_wrhtp = '"+strWRHTP+"'";
			M_strSQLQRY += " and st_prdtp = '"+strPRDTP+"'";
			M_strSQLQRY += " and st_lotno = '"+strLOTNO+"'";
			M_strSQLQRY += " and st_rclno = '"+strRCLNO+"'";
			M_strSQLQRY += " and st_pkgtp = '"+strPKGTP+"'";
			M_strSQLQRY+= " and st_mnlcd = '"+strMNLCD+"'";
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
		}catch(Exception L_EX){
			setMSG(L_EX,"updSTMST");
		}
	}
	
	/**
	 * @return void
	 * This method reduces the Allocated Qty. within the Stock Master to zero.
	 */
	private void decSTMST(){
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
			    M_strSQLQRY = "Update FG_STMST set ";
			    M_strSQLQRY += "ST_ALOQT = 0";
				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				if(cl_dat.M_flgLCUPD_pbst)
				{
					cl_dat.exeDBCMT("exeSAVE");
					
				}else
				{
				
				}
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"This is decrement Statck Master");
			}
		}
	}
	/**
	 * This function Used for the getThe Values from the DB for Refresh 
	
	 */
	private void exeSTMST()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				M_strSQLQRY = "Select IST_WRHTP,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGTP,sum(IST_ISSQT) LM_SUMQT";
				M_strSQLQRY += " from FG_ISTRN where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_STSFL = '1' and isnull(IST_ISSQT,0)>0 ";
				M_strSQLQRY += " group by IST_WRHTP,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PKGTP,IST_MNLCD";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				while(M_rstRSSET.next())
				{
					
				    strWRHTP = M_rstRSSET.getString("IST_WRHTP");
					strPRDTP = M_rstRSSET.getString("IST_PRDTP");
					strLOTNO = M_rstRSSET.getString("IST_LOTNO");
					strRCLNO = M_rstRSSET.getString("IST_RCLNO");
					strMNLCD = M_rstRSSET.getString("IST_MNLCD");
					strPKGTP = M_rstRSSET.getString("IST_PKGTP");
					strISAQT = M_rstRSSET.getString("LM_SUMQT");
				    
					
					
					updSTMST1();
					}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"exeSTMST");
			}
		}
	}


/**
 * @return void
 * Updates Allocated Qty. within Stock Master with the Allocated Qty. retrieved from the 
 * Issue Allocated Transaction. 
 */
 private void updSTMST1()
 {
     if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				
				
		
							M_strSQLQRY = "Update FG_STMST set ";
							M_strSQLQRY += "ST_ALOQT = "+strISAQT+",";
							M_strSQLQRY += "ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY += "ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
							M_strSQLQRY += " where ST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ST_WRHTP = '"+strWRHTP+"'";
							M_strSQLQRY += " and ST_PRDTP = '"+strPRDTP+"'";
							M_strSQLQRY += " and ST_LOTNO = '"+strLOTNO+"'";
							M_strSQLQRY += " and ST_RCLNO = '"+strRCLNO+"'";
							M_strSQLQRY += " and ST_PKGTP = '"+strPKGTP+"'";
							M_strSQLQRY += " and ST_MNLCD = '"+strMNLCD+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							//System.out.println("updStamst1:"+M_strSQLQRY);
							if(cl_dat.M_flgLCUPD_pbst)
							{
								cl_dat.exeDBCMT("exeSAVE");
								
							}
							else
							{
							
							}
					
				
		
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"upd Stock Master in Refresh ");
			    
			}
		}	
	}
/**
 * 
 * @param LP_CGMTP "D"+cl_dat.M_strCMPCD_pbst      Updated In CO_CDTRN Table for FEFRESH Receipt & Refresh Issue
 * @param LP_CGSTP  "FGXXRCT"
 */
private void updCDTRN(String LP_CGMTP,String LP_CGSTP)
{
	if(cl_dat.M_flgLCUPD_pbst)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += "CMT_CHP01 = '',";
			M_strSQLQRY += "CMT_CHP02 = ''";
			M_strSQLQRY+= " where CMT_CGMTP = '"+LP_CGMTP+"'";
			M_strSQLQRY += " and CMT_CGSTP = '"+LP_CGSTP+"'";
		    //System.out.println(M_strSQLQRY); 
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			if(cl_dat.M_flgLCUPD_pbst)
			{
				cl_dat.exeDBCMT("exesave");
				
		}else{
			setMSG("Refresh Failed",'E');			}
		}catch(Exception L_EX){
			setMSG(L_EX,"updCDTRN");
		}
	}
}

/**
 * @return void
 * Displays successful message if Allocated Qtys. in both the table matches with each other
 * i.e fg_stmst & fg_istrn else displays unsuccessful message.
 */
private void getDSPMSG(String LP_STATS)
{
	try
	{
		String L_STKQT = "";
		String L_ISSQT = "";
		M_strSQLQRY = "Select sum(ST_ALOQT) LM_SUMQT from FG_STMST";
		//System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		//System.out.println("This is Stack Quantity"+M_strSQLQRY);
		while(M_rstRSSET.next())
		{
			L_STKQT = M_rstRSSET.getString("LM_SUMQT");
		}
		if(M_rstRSSET != null)
			M_rstRSSET.close();
		M_strSQLQRY = "Select sum(IST_ISSQT) LM_SUMQT from FG_ISTRN where IST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IST_STSFL='1'";
		//System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		//System.out.println("This is ISSUE Quantity"+M_strSQLQRY);
		while(M_rstRSSET.next())
		{
			L_ISSQT = M_rstRSSET.getString("LM_SUMQT");
		}
		if(M_rstRSSET != null)
			M_rstRSSET.close();
		if(L_STKQT == null || L_STKQT.equals(""))
			L_STKQT = "0.000";
		if(L_ISSQT == null || L_ISSQT.equals(""))
			L_ISSQT = "0.000";
	LM_OPTNPN.showMessageDialog(this,"Allocation Status "+ LP_STATS +" Refresh i.e Stock Allocated Qty="+L_STKQT+" and Issue Allocated Qty="+L_ISSQT+"","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
	
	}catch(Exception L_EX){
		setMSG(L_EX,"getDSPMSG");
		}
	}
	
	
	
	

}

	

	
	
	
	
	
