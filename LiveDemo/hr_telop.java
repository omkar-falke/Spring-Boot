/*

*/

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;import java.awt.Color;
import java.sql.ResultSet;import javax.swing.JPanel;import javax.swing.JTabbedPane;
import java.sql.CallableStatement;import javax.swing.JComboBox;
import javax.swing.JOptionPane;import java.awt.event.MouseEvent;
import javax.swing.border.*;import java.util.Calendar;

class hr_telop extends cl_pbase
{
	JTextField txtEMPNO;                 
    JTextField txtEMPNM;                 
	JTextField txtDPTCD;                 
    JTextField txtMMGRD;                 
    JTextField txtDESGN;                 
    JTextField txtYOPDT;                 
    JTextField txtYOPCL;                 
	JTextField txtYOPPL;                 
	JTextField txtYOPRH;
	JTextField txtYOPSL;                 
	JTextField txtYTDCL;                 
	JTextField txtYTDPL;                 
	JTextField txtYTDRH;
	JTextField txtYTDSL;                 
	JCheckBox  chkCPCUR;
	
	JLabel lblDPTNM,lblLVEOB,lblLVECB;
    private TBLINPVF objTBLVRF; 
	private INPVF oINPVF;
	

	/* Constructor */	
	hr_telop()
	{
	    super(2);
		try
		{
		    setMatrix(20,20);
	            
            //***** ADDING GRADE DETAILS*****//
			objTBLVRF = new TBLINPVF();
			oINPVF=new INPVF();
			add(new JLabel("Emp No."),2,2,1,2,this,'L');
			add(txtEMPNO = new TxtLimit(5),2,4,1,2,this,'L');
			
			add(new JLabel("Employee Name"),2,7,1,3,this,'L');
			add(txtEMPNM = new TxtLimit(20),2,10,1,3,this,'L');
			
			add(new JLabel("Department"),2,14,1,2,this,'L');
			add(txtDPTCD = new TxtLimit(3),2,16,1,2,this,'L');
			add(lblDPTNM=new JLabel(),2,18,1,3,this,'L');
			
			add(new JLabel("Grade"),3,2,1,2,this,'L');
			add(txtMMGRD = new TxtLimit(5),3,4,1,2,this,'L');
			
			add(new JLabel("Designation"),3,7,1,2,this,'L');
			add(txtDESGN = new TxtLimit(10),3,10,1,3,this,'L');
			
			add(lblLVEOB = new JLabel("Leave Opening Balance "),5,4,1,4,this,'L');
			add(lblLVECB = new JLabel("Current Balance "),5,11,1,4,this,'L');
			lblLVEOB.setForeground(Color.BLUE);    
			lblLVECB.setForeground(Color.BLUE);    
			//lblDPTNM.setBorder(new EtchedBorder(Color.black,Color.lightGray));
			
			add(new JLabel("Year Opening Date"),7,4,1,3,this,'L');
			add(txtYOPDT = new TxtDate(),7,7,1,2,this,'L');
			add(chkCPCUR = new JCheckBox("Copy To Current Balance"),7,11,1,4,this,'L');
			
			add(new JLabel("Casual Leave"),8,4,1,3,this,'L');
			add(txtYOPCL = new TxtNumLimit(5.1),8,7,1,2,this,'L');
			add(txtYTDCL = new TxtNumLimit(5.1),8,11,1,2,this,'L');
			
			add(new JLabel("Sick Leave"),9,4,1,3,this,'L');
			add(txtYOPSL = new TxtNumLimit(5.1),9,7,1,2,this,'L');
			add(txtYTDSL = new TxtNumLimit(5.1),9,11,1,2,this,'L');
			
			add(new JLabel("Priviledge Leave"),10,4,1,3,this,'L');
			add(txtYOPPL = new TxtNumLimit(5.1),10,7,1,2,this,'L');
			add(txtYTDPL = new TxtNumLimit(5.1),10,11,1,2,this,'L');
			
			add(new JLabel("Restricted Holiday"),11,4,1,3,this,'L');
			add(txtYOPRH = new TxtNumLimit(5.1),11,7,1,2,this,'L');
			add(txtYTDRH = new TxtNumLimit(5.1),11,11,1,2,this,'L');
			
			txtEMPNO.setInputVerifier(oINPVF);
			txtYOPDT.setInputVerifier(oINPVF);			
			setENBL(false);
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
		txtEMPNM.setEnabled(false);
		txtDPTCD.setEnabled(false);
		txtMMGRD.setEnabled(false);
		txtDESGN.setEnabled(false);
		txtYTDCL.setEnabled(false);
		txtYTDSL.setEnabled(false);
		txtYTDPL.setEnabled(false);
		txtYTDRH.setEnabled(false);
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
	}
	

	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			String L_strNewval1,L_strNewval2;
			try
			{
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

				if(input==txtEMPNO)
				{
					M_strSQLQRY = "Select distinct EP_EMPNO from HR_EPMST where ";
					M_strSQLQRY +="EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and EP_EMPNO = '"+txtEMPNO.getText()+"'";
        			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						getDATA();
						return true;
					}	
					else 
					{
						setMSG("Enter Valid Employee No.",'E');
						return false;	
					}
				}
				if(input == txtYOPDT)
				{
					if(M_fmtLCDAT.parse(txtYOPDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("Date must be less than or equal to todays date..",'E');
						return false;
					}
					return true;
				}	
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				
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
		
	boolean vldDATA()
	{
		try
		{
			if(txtYOPDT.getText().trim().length()==0)
    		{
				txtYOPDT.requestFocus();
    			setMSG("Enter Year Opening Date",'E');
    			return false;
    		}
			if(txtYOPCL.getText().trim().length()==0)
    		{
				txtYOPCL.requestFocus();
    			setMSG("Enter Casual Leave",'E');
    			return false;
    		}
			if(txtYOPSL.getText().trim().length()==0)
    		{
				txtYOPSL.requestFocus();
    			setMSG("Enter Sick Leave",'E');
    			return false;
    		}
			if(txtYOPPL.getText().trim().length()==0)
    		{
				txtYOPPL.requestFocus();
    			setMSG("Enter Priviledge Leave",'E');
    			return false;
    		}
			if(txtYOPRH.getText().trim().length()==0)
    		{
				txtYOPRH.requestFocus();
    			setMSG("Enter Restricted Holidays",'E');
    			return false;
    		}
		}
		catch(Exception L_EX)
		 {
			setMSG(L_EX,"This is vldDATA()");
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
					if(M_objSOURC == txtEMPNO)
    				{
        			    M_strHLPFLD = "txtEMPNO";	
        			    M_strSQLQRY = "Select distinct EP_EMPNO,EP_FULNM from HR_EPMST where ep_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' ";
						M_strSQLQRY +=" order by EP_EMPNO";
        				cl_hlp(M_strSQLQRY,2,1,new String[]{"Employee No.", "Employee Name"},2,"CT",new int[]{107,400});
    				}
			}
			else if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
   				if(M_objSOURC==txtEMPNO)
					txtYOPDT.requestFocus();
				if(M_objSOURC==txtYOPDT)
					txtYOPCL.requestFocus();
				if(M_objSOURC==txtYOPCL)
					txtYOPSL.requestFocus();
				if(M_objSOURC==txtYOPSL)
					txtYOPPL.requestFocus();
				if(M_objSOURC==txtYOPPL)
					txtYOPRH.requestFocus();
				if(M_objSOURC==txtYOPRH)
					cl_dat.M_btnSAVE_pbst.requestFocus();
    	    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	public void keyReleased(KeyEvent L_KE)
	{
		super.keyReleased(L_KE);
		try
		{
		    if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
   				if(M_objSOURC==txtYOPDT)
					txtYOPDT.select(0,txtYOPDT.getText().length());
				else if(M_objSOURC==txtYOPCL)
					txtYOPCL.select(0,txtYOPCL.getText().length());
				else if(M_objSOURC==txtYOPPL)
					txtYOPPL.select(0,txtYOPPL.getText().length());
				else if(M_objSOURC==txtYOPRH)
					txtYOPRH.select(0,txtYOPRH.getText().length());
				else if(M_objSOURC==txtYOPSL)
					txtYOPSL.select(0,txtYOPSL.getText().length());
    	    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}	
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");

			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{	
				clrCOMP();
				chkCPCUR.setSelected(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)
				|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)  )
				{
					setENBL(false);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					setENBL(false);
					txtEMPNO.setEnabled(true);
				}
			}	
		}	
	    catch(Exception L_EA)
	    {
	        setMSG(L_EA,"Action Performed");
	    }
	}
			   
	
	/* method for Help*/	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD == "txtEMPNO")
			{
				txtEMPNO.setText(cl_dat.M_strHLPSTR_pbst);
			}	

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}
	
	/* method to populat the textfields when user enters the EMPNO.*/
	public void getDATA()
    {
        try
        {
			M_strSQLQRY =" Select rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '  + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM,EP_DPTCD,EP_MMGRD,";
			M_strSQLQRY+=" EP_DESGN,EP_YOPDT,isnull(EP_YOPCL,0) EP_YOPCL,";
			M_strSQLQRY+=" isnull(EP_YTDCL,0) EP_YTDCL,isnull(EP_YOPPL,0) EP_YOPPL,isnull(EP_YTDPL,0) EP_YTDPL,isnull(EP_YOPRH,0) EP_YOPRH,";
			M_strSQLQRY+=" isnull(EP_YTDRH,0) EP_YTDRH,isnull(EP_YOPSL,0) EP_YOPSL,isnull(EP_YTDSL,0) EP_YTDSL from";
			M_strSQLQRY+=" HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and EP_EMPNO='"+txtEMPNO.getText()+"'";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			
			try{
			if(M_rstRSSET.next() && M_rstRSSET!=null)
			{
				txtEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
				txtDPTCD.setText(M_rstRSSET.getString("EP_DPTCD"));
				txtMMGRD.setText(M_rstRSSET.getString("EP_MMGRD"));
				txtDESGN.setText(M_rstRSSET.getString("EP_DESGN"));
				if(M_rstRSSET.getString("EP_YOPDT")==null)
					txtYOPDT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2,10));
				else
					txtYOPDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_YOPDT")));
				txtYOPCL.setText(M_rstRSSET.getString("EP_YOPCL"));
				txtYOPSL.setText(M_rstRSSET.getString("EP_YOPSL"));
				txtYOPPL.setText(M_rstRSSET.getString("EP_YOPPL"));
				txtYOPRH.setText(M_rstRSSET.getString("EP_YOPRH"));
				txtYTDCL.setText(M_rstRSSET.getString("EP_YTDCL"));
				txtYTDSL.setText(M_rstRSSET.getString("EP_YTDSL"));
				txtYTDPL.setText(M_rstRSSET.getString("EP_YTDPL"));
				txtYTDRH.setText(M_rstRSSET.getString("EP_YTDRH"));
			}	
			else
				setMSG("No Records Found",'E');
			}catch(Exception L_EX){setMSG(L_EX,"inside1");}
			try{
			M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
			M_strSQLQRY+=" and CMT_STSFL <> 'X' and CMT_CODCD='"+txtDPTCD.getText().trim()+"'";
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET!=null)
			{
				lblDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
			}
			else
				setMSG("No Records Found",'E');
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();

			}catch(Exception L_EX){setMSG(L_EX,"inside2");}
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"getDATA");
        }
    }
    
	/* method to save data   */
	void exeSAVE()
    {
		int P_intROWNO;
        try
		{
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_flgLCUPD_pbst = true;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
				exeMODREC();
				exeUPD_SSTRN();
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				clrCOMP();
				chkCPCUR.setSelected(false);
				setENBL(true);
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
    
    /* On Save Button click data is inserted or updated into the respective tables */
	private void exeMODREC()
	{ 
	  try
	  {
		  M_strSQLQRY =" Update HR_EPMST set";
		  M_strSQLQRY+=" EP_YOPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtYOPDT.getText()))+"',"; 
		  M_strSQLQRY+=" EP_YOPCL='"+txtYOPCL.getText()+"',"; 
		  M_strSQLQRY+=" EP_YOPSL='"+txtYOPSL.getText()+"',"; 
		  M_strSQLQRY+=" EP_YOPPL='"+txtYOPPL.getText()+"',"; 
		  M_strSQLQRY+=" EP_YOPRH='"+txtYOPRH.getText()+"'"; 
		  M_strSQLQRY+=" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText()+"'";
		  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		  
		  if(chkCPCUR.isSelected())
		  {
				M_strSQLQRY =" Update HR_EPMST set";
				M_strSQLQRY+=" EP_YTDCL='"+txtYOPCL.getText()+"',"; 
				M_strSQLQRY+=" EP_YTDSL='"+txtYOPSL.getText()+"',"; 
				M_strSQLQRY+=" EP_YTDPL='"+txtYOPPL.getText()+"',"; 
				M_strSQLQRY+=" EP_YTDRH='"+txtYOPRH.getText()+"'"; 
				M_strSQLQRY+=" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		  }		  
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeMODREC");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeADDREC"); 
      }
	}
	
    /* if General Shift Flag in HR_EPMST  == "Y" then shift 
	is automatically updated as general in HR_SSTRN */
	
	private void exeUPD_SSTRN()
	{ 
	  try
	  {
		  setMSG("Updating Shift Shedule For the Employee..",'N');
		  String L_strSTRDT,L_strENDDT;
		  java.util.Date L_datSTRDT,L_datENDDT,L_datTMPDT;
		  String strSQLQRY,strSQLQRY1;
		  ResultSet rstRSSET,rstRSSET1;
		  java.util.Vector<Object> vtrDATE = new java.util.Vector<Object>();
		  int intDAYWK;
		  strSQLQRY= " select isnull(EP_GENFL,'') EP_GENFL from HR_EPMST ";
		  strSQLQRY+=" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and EP_EMPNO='"+txtEMPNO.getText()+"'";
		  rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
		  if(rstRSSET == null || !rstRSSET.next())
			  return;
		  if(rstRSSET.getString("EP_GENFL").equals("Y"))
		  {
				L_strSTRDT =  "01/01/"+cl_dat.M_strLOGDT_pbst.substring(6).trim();
				L_strENDDT =  "31/12/"+cl_dat.M_strLOGDT_pbst.substring(6).trim();
				L_datSTRDT = M_fmtLCDAT.parse(L_strSTRDT);
				L_datENDDT = M_fmtLCDAT.parse(L_strENDDT);
				L_datTMPDT = L_datSTRDT;
				
				strSQLQRY1 = " select SS_WRKDT from HR_SSTRN";
				strSQLQRY1+= " where SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SS_EMPNO='"+txtEMPNO.getText()+"'";
				strSQLQRY1+= " and SS_WRKDT between '"+M_fmtDBDAT.format(L_datSTRDT)+"' and '"+M_fmtDBDAT.format(L_datENDDT)+"'";
				rstRSSET1 = cl_dat.exeSQLQRY(strSQLQRY1);
				if(rstRSSET1 != null)
				{
					while(rstRSSET1.next())
						if(!vtrDATE.contains(rstRSSET1.getDate("SS_WRKDT")))
							vtrDATE.addElement(rstRSSET1.getDate("SS_WRKDT"));
				}	
				
				while(L_datTMPDT.compareTo(L_datENDDT)<=0)
				{
					if(!vtrDATE.contains(L_datTMPDT))
					{
						M_calLOCAL.setTime(L_datTMPDT);
						intDAYWK = M_calLOCAL.get(M_calLOCAL.DAY_OF_WEEK);//gets day of the week. if its sunday intDAYWK=1 
							
						M_strSQLQRY =" INSERT INTO HR_SSTRN(ss_cmpcd,ss_sbscd,ss_EMPNO,ss_WRKDT,ss_LUSBY,ss_LUPDT,ss_ORGSH, ss_CURSH, ss_LVECD, ss_STSFL,ss_trnfl)";
						M_strSQLQRY +=" Values(";
						M_strSQLQRY += " '"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += " '"+M_strSBSCD+"',";
						M_strSQLQRY += " '"+txtEMPNO.getText()+"',";
						M_strSQLQRY += " '"+M_fmtDBDAT.format(L_datTMPDT)+"',";
						M_strSQLQRY += " '"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += " '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"',";
						if(intDAYWK == 1)
						{	
							M_strSQLQRY += " 'O',";
							M_strSQLQRY += " 'O',";
						}
						else
						{	
							M_strSQLQRY += " 'G',";
							M_strSQLQRY += " 'G',";
						}
						if(intDAYWK == 1)
							M_strSQLQRY += " 'WO',";
						else
							M_strSQLQRY += "'',";
												
						M_strSQLQRY += " '1',";
						M_strSQLQRY += " '0')";
						
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						//System.out.println(M_strSQLQRY);
						cl_dat.M_flgLCUPD_pbst =true;
					}	
					M_calLOCAL.setTime(L_datTMPDT);      
					M_calLOCAL.add(Calendar.DATE,+1);    
					L_datTMPDT = M_calLOCAL.getTime();
				}	  
		  }	  
			 
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeUPD_SSTRN");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeUPD_SSTRN"); 
      }
	}
	
}

