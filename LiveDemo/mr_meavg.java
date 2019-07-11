import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
//import cl_cust;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.util.*;


public class mr_meavg extends cl_rbase implements MouseListener
{
        JButton btnREFRESH, btnREFR1, btnEXIT1;
        JButton btnSPLRT;    // Button for setting Speciality & Master batch rates automatically
        JRadioButton rdbNEWENT,rdbOLDENT;
        ButtonGroup grpENT,grpCAT;

       // JTable tblCATTBL, LM_PRDTBL;
	    JOptionPane LM_OPTNPN;
        JTextField txtEDITR;
		cl_JTable tblCATTBL,tblPRDTBL;

//	cl_cust ocl_cust = new cl_cust();


        String strSCAVG;
        String strSHAVG;
        String strPRDCD;
        String strPRDDS;
        String strAVGRT;
        String strAVGBY;
		//String strPRDCD;
        String strAVGDT;


	    int intTB1_CHKFL = 0;
        int intTB1_PRDCD = 1;
        int intTB1_PRDDS = 2;
        int intTB1_AVGRT = 3;
        int intTB1_AVGBY = 4;
        int intTB1_AVGDT = 5;


        int intTB2_CHKFL = 0;
        int intTB2_PRDCD = 1;
        int intTB2_PRDDS = 2;
        int intTB2_AVGRT = 3;
        int intTB2_AVGBY = 4;
        int intTB2_AVGDT = 5;
		int intCNTRW=0;

        int LM_SCOL, LM_SROW, LM_PRVVL, LM_CURVL;
        ResultSet L1_RSLSET;
        boolean LM_NEWENT = false, LM_CAT_PS = false;

        PreparedStatement PS01_dspCATTBL;
        PreparedStatement PS01_dspPRDTBL;
        //PreparedStatement PS01_exeSAVECAT;
        //PreparedStatement PS01_exeSAVEPRD;
        //PreparedStatement PS02_exeSAVEPRD;
		CallableStatement cstSPLRT;	// Stored procedure for Auto updation of Speciality Rates
		
		
/**Constructor for the form<br>
 * to create the Buttons and radioButton and Dynamic table
 * 
 */
        mr_meavg()
        {
           
			super(1);
			try
			{
				cl_dat.M_flgLCUPD_pbst = true;
		    setMatrix(20,20);
			grpENT=new ButtonGroup();
            grpCAT=new ButtonGroup();
			
			add(rdbNEWENT = new JRadioButton("New Entry",false),1,4,1,2.5,this,'L');
            add(rdbOLDENT = new JRadioButton("Modification",true),1,7,1,2.5,this,'L');
			grpENT.add(rdbNEWENT);
			grpENT.add(rdbOLDENT);
			
			add(btnREFRESH=new JButton("Refresh"),1,10,1,2.5,this,'L');
			add(btnSPLRT = new JButton("SPS-MB Auto-Update"),2,10,1,3,this,'L');
			
						
			String[] L_staSTKHD = {"Status","Code","Categry","Rs./MT","Entered By","Entered on"};/**This is for create the coloums */
       	    int[] L_intCOLSZ = {50,75,125,75,75,65};
		    tblCATTBL = crtTBLPNL1(this,L_staSTKHD,100,3,6,5,12.5 ,L_intCOLSZ,new int[]{0});
			//tblCATTBL.setInputVerifier(new TBLINPVF());
			tblCATTBL.addMouseListener(this);
			
			tblCATTBL.setRowSelectionInterval(0,0);
			tblCATTBL.setInputVerifier(new INPVF());
			INPVF oINPVF =new INPVF();
			
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier( oINPVF);
				}
				else
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}
			
			tblCATTBL.addKeyListener(this);
			
			
			String[] L_PRDHD = {"Status","Code","Grade","Rs./MT","Entered By","Entered on"};
            int[] L_COLSZ = {50,100,150,100,100,80};
            tblPRDTBL = crtTBLPNL1(this,L_PRDHD,1000,9,1,10,15.5,L_COLSZ,new int[]{0});
            tblPRDTBL.addMouseListener(this);
			rdbNEWENT.setEnabled(false);
			 btnREFRESH.setEnabled(false);
			 btnSPLRT.setEnabled(false);
			 tblCATTBL.setEnabled(false);
			 tblPRDTBL .setEnabled(false);
			 rdbOLDENT.setEnabled(false);
		
			 PS01_dspCATTBL  = cl_dat.M_conSPDBA_pbst.prepareStatement("select CMT_CODCD,CMT_CODDS,CMT_NCSVL,CMT_LUSBY,CMT_LUPDT from co_cdtrn where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and SUBSTRING(cmt_codcd,1,4) in ('5111','5112') and SUBSTRING(cmt_codcd,5,6)='00000A' order by cmt_codcd ");
             
			 PS01_dspPRDTBL  = cl_dat.M_conSPDBA_pbst.prepareStatement("select PR_PRDCD,PR_PRDDS,PR_AVGRT,PR_AVGBY,PR_AVGDT from co_prmst where SUBSTRING(pr_prdcd,1,2) in ('51','52','54') order by pr_prdcd"); 
			 //PS01_exeSAVECAT = cl_dat.M_conSPDBA_pbst.prepareStatement("update co_cdtrn set cmt_ncsvl = ?, cmt_lusby = ?, cmt_lupdt = ? where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and cmt_codcd = ?");
             //PS02_exeSAVEPRD = cl_dat.M_conSPDBA_pbst.prepareStatement("update co_prmst set pr_avgrt = ?, pr_avgby = ?, pr_avgdt = ?, pr_lusby = ?, pr_lupdt = ? where pr_prdcd = ?"); 
			
			
			
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"MR_MEAVG");
			}	
            
            
              									
									
			
            
         
        }
		void setENBL(boolean L_flgSTAT)
		{
         super.setENBL(L_flgSTAT);
		/* rdbNEWENT.setEnabled(true);
		 btnREFRESH.setEnabled(true);
		 tblCATTBL.setEnabled(true);
		  tblPRDTBL .setEnabled(true);
		  rdbOLDENT.setEnabled(true);*/
		 
		}
		/**@return void This function is FromDate & ToDate TextFields and Labels also removed
		 */
		void removeENBL(boolean L_flgSTAT)
		{
        
           M_txtFMDAT.setVisible(false);
	       M_txtTODAT.setVisible(false);
	       M_lblFMDAT.setVisible(false);
		   M_lblTODAT.setVisible(false);		
		    
		}
		
		
		
		/**Focus Listener is used for we have enter into the table on the textfield dislay some message
		 * 
		 */
		public void focusGained(FocusEvent L_FE)
		{
			super.focusGained(L_FE);
			try
			{   //tblCATTBL	tblPRDTBL 
				if(M_objSOURC == ((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]))
				{
					setMSG("Enter Product Code..........",'N');
					if(tblCATTBL.getSelectedRow()<intCNTRW)
					{	
						 ((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]).setEditable(false);
						 
					}	 
					
					else
						 	((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]).setEditable(true);
				}
				if(M_objSOURC == ((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]))
				{
					setMSG("Enter the Product Catagery....",'N');
					if(tblCATTBL.getSelectedRow()<intCNTRW)
						((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]).setEditable(false);
					else
						((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]).setEditable(true);
				}	
																	   
				 if(M_objSOURC == ((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGRT]))
				 {	 
				 
					setMSG("Enter the Average Rate",'N');
					 if(tblCATTBL.getSelectedRow()<intCNTRW)
							((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGRT]).setEditable(false);
					else
				    	 	((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGRT]).setEditable(true);
				 }	
				 if(M_objSOURC == ((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGBY]))
				 {	 
				     setMSG("Enter the AverageBY......",'N');
						if(tblCATTBL.getSelectedRow()<intCNTRW)
							 ((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGBY]).setEditable(false);
						else
							 ((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGBY]).setEditable(true);
				 
				 }
				 
				 if(M_objSOURC == ((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]))
				 {	 
					 setMSG("Enter Average Dage..........",'N');
						if(tblCATTBL.getSelectedRow()<intCNTRW)
							 ((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]).setEditable(false);
						else
							 ((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]).setEditable(true);
				 }		
				 
				 if(M_objSOURC == ((JTextField)tblPRDTBL.cmpEDITR[intTB2_PRDCD]))
				 {	
					setMSG(" Product Table.. Enter the Product Code",'N');
					
					 if(tblPRDTBL .getSelectedRow()<intCNTRW)
						 ((JTextField)tblPRDTBL.cmpEDITR[intTB2_PRDCD]).setEditable(false);
					else
						 ((JTextField)tblPRDTBL.cmpEDITR[intTB2_PRDCD]).setEditable(true);
				 }	
				 if(M_objSOURC == ((JTextField)tblPRDTBL.cmpEDITR[intTB2_PRDDS]))
				 {	 
					 setMSG(" Product Table.. Enter the Product Catagoery",'N');
					if(tblPRDTBL .getSelectedRow()<intCNTRW)
						  ((JTextField)tblPRDTBL.cmpEDITR[intTB2_PRDDS]).setEditable(false);
					else
						 ((JTextField)tblPRDTBL.cmpEDITR[intTB2_PRDDS]).setEditable(true);
				 }	
				 if(M_objSOURC == ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGRT]))
				 {	 
					 setMSG(" Product Table.. Enter the Average Rate",'N');
						if(tblPRDTBL .getSelectedRow()<intCNTRW)
						        ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGRT]).setEditable(false);
						else
								 ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGRT]).setEditable(true);					 
				 }		

				 if(M_objSOURC == ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGBY]))
				 {
					 setMSG(" Product Table.. Enter the Average By",'N');
    					if(tblPRDTBL .getSelectedRow()<intCNTRW)
								((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGBY]).setEditable(false);
						else
						       ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGBY]).setEditable(true);					 
				 }		
																			
																
				 if(M_objSOURC == ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGDT]))
				 {	 
					 setMSG(" Product Table.. Enter the Average Date",'N');
						if(tblPRDTBL .getSelectedRow()<intCNTRW)
							 ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGDT]).setEditable(false);
						else
							 ((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGDT]).setEditable(true);
				 }		
							 
				 
				
							
					
				
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"This is focusGained");
			}	
		}
			
			/**<b>TASKS :</b><br>
			 * we have to implement on the events on Refresh Button & RadioButon any RadioButton Clicked some action performed
*/	 
		public void actionPerformed(ActionEvent L_AE)
		{
				super.actionPerformed(L_AE);
				try
				{
		
					if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) /** Combo Opotion Remove the From Date & To Date 
					                                         */
					removeENBL(false);
					
					if(M_objSOURC == btnREFRESH)
					{
						 getCATTBL1();
					}	
					if(M_objSOURC == btnSPLRT)
					{
						cstSPLRT = cl_dat.M_conSPDBA_pbst.prepareCall("{call setPRMST_AVGRT(?,?)}");
						cstSPLRT.setString(1,cl_dat.M_strCMPCD_pbst);
						cstSPLRT.setString(2,"52");
						cstSPLRT.executeUpdate();
						cstSPLRT.setString(1,cl_dat.M_strCMPCD_pbst);
						cstSPLRT.setString(2,"54");						
						cstSPLRT.executeUpdate();
						//getCATTBL1();
					}	
		
		
		
					if(M_objSOURC== rdbNEWENT)
					{
						
					   if(rdbNEWENT.isSelected())
					   {
					        LM_NEWENT = true;
							//tblCATTBL.clrTABLE();
						    //tblPRDTBL.clrTABLE();
							//clrPRDTBL();clrCATTBL();
					        getCATTBL();
					        getPRDTBL();
					    }
					 }
					if(M_objSOURC==  rdbOLDENT)
					{
					    if(rdbOLDENT.isSelected())
					     {
					           LM_NEWENT = false;
							  // clrPRDTBL();clrCATTBL();
							  // tblCATTBL.clrTABLE();
							   //tblPRDTBL.clrTABLE();
					           getCATTBL();
					           getPRDTBL();
					     }
					}	
        

					}catch(Exception L_EX)
					{
						setMSG(L_EX,"Actionperformed");
					}	
		}
		

		
		
		/**we have to perform the Enter operation on the table
		 */
		public void keyPressed(KeyEvent L_KE)
        {
          try
          {
				super.keyPressed(L_KE);
														
				
				 if(tblCATTBL.isEditing())
				{
					((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]).setEditable(false);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]).setEditable(false);
					((JTextField)tblCATTBL.cmpEDITR[intTB2_AVGBY]).setEditable(false);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]).setEditable(false);
					
				 }
				/*  for(int i=0;i<tblCATTBL.getRowCount();i++)
				  {
	        
	        
						if(nvlSTRVL(tblCATTBL.getValueAt(i,intTB1_PRDCD).toString(),"").equals(""))
						{
							
							((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]).setEditable(true);
							((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]).setEditable(true);
							((JTextField)tblCATTBL.cmpEDITR[intTB2_AVGBY]).setEditable(true);
							((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]).setEditable(true);
						}
			  }	*/
					
					
				
				
				if(tblPRDTBL.isEditing())
				{
					((JTextField)tblPRDTBL.cmpEDITR[intTB1_PRDCD]).setEditable(false);
					((JTextField)tblPRDTBL.cmpEDITR[intTB1_PRDDS]).setEditable(false);
					((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGBY]).setEditable(false);
					((JTextField)tblPRDTBL.cmpEDITR[intTB1_AVGDT]).setEditable(false);
					
				}   
				
				
						if (L_KE.getKeyCode() == 106)
		 		        {
					
		    				//cl_dat.ocl_dat.M_FLGOPT = 'C';
							cl_dat.M_cmbOPTN_pbst.setSelectedIndex(0);
			    			cl_dat.M_cmbOPTN_pbst.requestFocus();
					    }
				        if(L_KE.getKeyCode() == 9 || L_KE.getKeyCode() == L_KE.VK_ENTER)
				        {
				        
					            
				                if( M_objSOURC==tblCATTBL)
				                {
									
				
									
				                        if(tblCATTBL.getSelectedColumn() == intTB1_AVGRT)
				                                tblCATTBL.setValueAt(new Boolean(true),tblCATTBL.getSelectedRow(),intTB1_CHKFL);
										
				                }
				                else if(L_KE.getSource().equals(tblPRDTBL))
				                {
				                        if(tblPRDTBL.getSelectedColumn() == intTB2_AVGRT)
				                                tblPRDTBL.setValueAt(new Boolean(true),tblPRDTBL.getSelectedRow(),intTB2_CHKFL);
                        }
                }
          }
                catch (Exception L_EX)
                {
                        setMSG(L_EX,"keyPressed");
                }
        }
		
		/** This Mouse Event was used for the not editing the table fields 
		  */
		public void mouseReleased(MouseEvent L_ME)
		{
			if(tblCATTBL.isEditing() )
			{
				
					((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]).setEditable(false);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]).setEditable(false);
					((JTextField)tblCATTBL.cmpEDITR[intTB2_AVGBY]).setEditable(false);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]).setEditable(false);
					
						
			}
			/*for(int i=0;i<tblCATTBL.getRowCount();i++)
			{
	        
	        
				if(nvlSTRVL(tblCATTBL.getValueAt(i,intTB1_PRDCD).toString(),"").equals(""))
				{
			
					((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]).setEditable(true);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]).setEditable(true);
					((JTextField)tblCATTBL.cmpEDITR[intTB2_AVGBY]).setEditable(true);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]).setEditable(true);
				}
			}	*/
					
				
		  		
				
			
		   if((tblPRDTBL.isEditing()))
		   {
			   ((JTextField)tblPRDTBL.cmpEDITR[intTB1_PRDCD]).setEditable(false);
				((JTextField)tblPRDTBL.cmpEDITR[intTB1_PRDDS]).setEditable(false);
				((JTextField)tblPRDTBL.cmpEDITR[intTB2_AVGBY]).setEditable(false);
				((JTextField)tblPRDTBL.cmpEDITR[intTB1_AVGDT]).setEditable(false);
					
		   }
		  
					
					
				
		
}
		public void mouseClicked(MouseEvent L_ME)
        {
		}


        public void mouseEntered(MouseEvent L_ME)
        {
			/*if(!(tblCATTBL.isEditing()))
		    {   
		   			((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDCD]).setEditable(true);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_PRDDS]).setEditable(true);
					((JTextField)tblCATTBL.cmpEDITR[intTB2_AVGBY]).setEditable(true);
					((JTextField)tblCATTBL.cmpEDITR[intTB1_AVGDT]).setEditable(true);
		   }*/		
				
		
		}

        public void mouseExited(MouseEvent L_ME)
        {
		}

        public void mousePressed(MouseEvent L_ME)
        {
		}

       

/**Get the Records tblCATTBL  on the table from DB
 */
		private void getCATTBL()
		{
			try
			{
			     int i = 0;
			    
			     M_rstRSSET = PS01_dspCATTBL.executeQuery();

					if ((!M_rstRSSET.next()) || (M_rstRSSET == null))
			             {
			                     setMSG("Product category not defined in Codes Transaction",'E');
			                     return;
			             }
					while (true)
			             {
			                   tblCATTBL.setValueAt(getRSTVAL(M_rstRSSET,"CMT_CODCD","C"),i,intTB1_PRDCD);
			                   tblCATTBL.setValueAt(getRSTVAL(M_rstRSSET,"CMT_CODDS","C"),i,intTB1_PRDDS);
			                   tblCATTBL.setValueAt(LM_NEWENT ? "0.00" : setNumberFormat(Double.parseDouble(getRSTVAL(M_rstRSSET,"CMT_NCSVL","C")),0),i,intTB1_AVGRT);
			                   tblCATTBL.setValueAt(getRSTVAL(M_rstRSSET,"CMT_LUSBY","C"),i,intTB1_AVGBY);
			                   tblCATTBL.setValueAt(getRSTVAL(M_rstRSSET,"CMT_LUPDT","D"),i,intTB1_AVGDT);
			                   i += 1;
			                 if (!M_rstRSSET.next()) {break;}
			             }
			   M_rstRSSET.close();
			}
				catch (Exception L_EX)
				        {
				                setMSG(L_EX,"getCATTBL");
				        }
      }
		
		
		
		
		
		
		
/**get The records on the table from DB
 */		
		
private void getPRDTBL()
{
   int i;
   try
   {


                M_rstRSSET = PS01_dspPRDTBL.executeQuery();
		if ((!M_rstRSSET.next()) || (M_rstRSSET == null))
                {
                        setMSG("Records not found in Product Master",'E');
                        return;
                }
                i = 0;
                while (true)
                {
                      tblPRDTBL.setValueAt(getRSTVAL(M_rstRSSET,"PR_PRDCD","C"),i,intTB2_PRDCD);
                      tblPRDTBL.setValueAt(getRSTVAL(M_rstRSSET,"PR_PRDDS","C"),i,intTB2_PRDDS);
                      tblPRDTBL.setValueAt(LM_NEWENT ? "0.00" : setNumberFormat(Double.parseDouble(getRSTVAL(M_rstRSSET,"PR_AVGRT","C")),0),i,intTB2_AVGRT);
                      tblPRDTBL.setValueAt(getRSTVAL(M_rstRSSET,"PR_AVGBY","C"),i,intTB2_AVGBY);
                      tblPRDTBL.setValueAt(getRSTVAL(M_rstRSSET,"PR_AVGDT","D"),i,intTB2_AVGDT);
                      i += 1;
                      if (!M_rstRSSET.next()) {break;}
                }
                M_rstRSSET.close();
      }

      catch (Exception L_EX)
      {
                    setMSG(L_EX,"getPRDTBL");
      }
}


/**Clear the table name is CATTBL
 */

 private void clrCATTBL()
 {
        try
          {
                tblCATTBL.getCellEditor(1,1).cancelCellEditing();
                tblCATTBL.getCellEditor(1,0).cancelCellEditing();
                for(int i = 0;i < tblCATTBL.getRowCount();i++)
                {
                        for(int j = 0;j<(tblCATTBL.getColumnCount()-1);j++)
                        {
                                if(tblCATTBL.getValueAt(i,intTB1_CHKFL).equals(new Boolean(true)))
                                {
                                       tblCATTBL.setValueAt(new Boolean(false),i,intTB1_CHKFL);
                                }
                                tblCATTBL.setValueAt("",i,j+1);
			}
		}
          }
          catch (Exception L_EX)
          {
                setMSG(L_EX,"clrCATTBL");
          }
        }
 /**Clear the Table Product Table
  */
  private void clrPRDTBL()
        {
          try
            {
                tblPRDTBL.getCellEditor(1,1).cancelCellEditing();
                tblPRDTBL.getCellEditor(1,0).cancelCellEditing();
                for(int i = 0;i < tblPRDTBL.getRowCount();i++)
                {
                        for(int j = 0;j<(tblPRDTBL.getColumnCount()-1);j++)
                        {
                                if(tblPRDTBL.getValueAt(i,intTB1_CHKFL).equals(new Boolean(true)))
                                {
                                       tblPRDTBL.setValueAt(new Boolean(false),i,intTB1_CHKFL);
                                }
                                tblPRDTBL.setValueAt("",i,j+1);
			}
		}
             }
		catch (Exception L_EX)
                {
                        setMSG(L_EX,"clrPRDTBL");
                }
        }
  
  /**
   * This function is used for Click the Authorised Button Save the records into the DB 
   */
  void exeSAVE()
    {
       try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			
		//	if(!vldDATA())
		//	{
		//		return;
		//	}
			//setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
            {
                setCursor(cl_dat.M_curWTSTS_pbst);
				
				getCATTBL1();
				exeSAVECAT();
				exeSAVEPRD();
             
            }
			
	   }catch(Exception L_EX)
	   {
		   setMSG(L_EX,"exeSAVE");
	   }   
  }  
  
  /*boolean vldDATA()
  {
	  
			if(tblDGRTBL.isEditing())
			{
				if(tblDGRTBL.getValueAt(tblDGRTBL.getSelectedRow(),tblDGRTBL.getSelectedColumn()).toString().length()>0)
				{
				TBLINPVF obj=new TBLINPVF();
				obj.setSource(tblDGRTBL);
				if(obj.verify(tblDGRTBL.getSelectedRow(),tblDGRTBL.getSelectedColumn()))
					tblDGRTBL.getCellEditor().stopCellEditing();
				else
					return false;
				}
			}	
  }		*/
  
  /**@return is void we have get the records for the DB any changes are having inthe some logic
   *  we have to performed that logic alos defiend on the function
   */
	private void getCATTBL1()
	{
			int i;
			try
			  {
			    strSCAVG="0.00";
			    strSHAVG="0.00";
			    i=0;
			    while (true)
			    {
			           strPRDCD = tblCATTBL.getValueAt(i,intTB1_PRDCD).toString().trim();
			            if (strPRDCD.length()==0)
			             {
			                  break;
			             }
			            if(tblCATTBL.getValueAt(i,intTB1_CHKFL).equals(new Boolean(true)))
			             {
			                if(strPRDCD.equals("511100000A"))
			                        strSCAVG = tblCATTBL.getValueAt(i,intTB1_AVGRT).toString().trim();
			                if(strPRDCD.equals("511200000A"))
			                        strSHAVG = tblCATTBL.getValueAt(i,intTB1_AVGRT).toString().trim();
			             }
			             i += 1;
			    }

			//  GPPS Prime   price PMT :  x
			//  HIPS Prime   price PMT :  y


			//  Gradewise Distribution

			//  SCXXX       (prime GPPS)                        :  x
			//  SCXXXB      (B grade & other Non-prime)         :  x-500

			//  SHXXX       (prime HIPS)                        :  y
			//  SHXXXB      (B grade & other Non-prime)         :  y-500

			//  SCOG        5111951610                          :  x-3000
			//  SHOG        5112951610                          :  y-3000

			//  SCOGXY      5111951640                          :  x-4000
			//  SCOGX       5111951620                          :  x-4000
			//  SCOGY       5111951630                          :  x-4000

			//  PSOG        5195951610                          :  x-10000

			//  SCP         5111951680                          :  x+11000
			//  SCP         5211951680                          :  x+11000

			//  PSFS        5197951180                          :  x-15000
			//  PSFS(SPS)   5297951180                          :  x-15000



			//  SPS (GPPS)    5211xxxxxx                          :  x+6000
			//  SPS (HIPS)    5212xxxxxx                          :  y+6000

				
			//	Other SPS Categories

			//  SPS category				Classified	Prd.Code		Avg.Price
			//								under	    Structure		Structure
				
			//  Toughened PS				GPPS		5213xxxxxx		x+6000
			//  Flame retardant PS			HIPS		5214xxxxxx		y+6000
			//  Modified PS					GPPS+HIPS	5215xxxxxx		((x+y)/2)+6000
			//  High heat PS				HIPS		5221xxxxxx		y+6000
			//  GPPS based master batch		GPPS+HIPS	5231xxxxxx		((x+y)/2)+6000
			//  HIPS based master batch		HIPS		5232xxxxxx		y+6000
			//  PS based master batch		GPPS+HIPS	5233xxxxxx		((x+y)/2)+6000
			//  PS PE Alloy					HIPS		5251xxxxxx		y+6000
			//  Flame retardant PS			GPPS+HIPS	5264xxxxxx		((x+y)/2)+6000
			//  Others						GPPS+HIPS	5281xxxxxx		((x+y)/2)+6000
			//  Experimental grade			GPPS+HIPS	5290xxxxxx		((x+y)/2)+6000
			//  Off grade					OG XY		5295xxxxxx		(x+6000) - 4000
			//  Floor sweep					Rs.20		5297xxxxxx		20000
			//  Scrap						Rs.20		5298xxxxxx		20000
				
			//  SPS (others)  5213xxxxxx Toughened PS             :  ((x+y)/2)+6000
			//  SPS (others)  5214xxxxxx Flame Retardant PS       :  ((x+y)/2)+6000
			//  SPS (others)  5215xxxxxx Modified PS              :  ((x+y)/2)+6000
			//  SPS (others)  5221xxxxxx High heat PS             :  ((x+y)/2)+6000
			//  ***********************************************************************

			//  SP2157 9150 5212409150                          :  y-1000
			//  SP2157 9200 5212409200                          :  y-1000
			//  SP2157 9210 5212409210                          :  y-1000
			//  SP2157 W    5212951910                          :  y-1000


			//  SP6403 9910 5215039910                          :  y+6000
			//  SP6403 8060 5215038060                          :  y+6000
			//  SP6403 0000 5215030000                          :  y+6000
			//  SP6403 1640 5215031640                          :  y+6000



			        boolean L_UPDCHK;
			        double L_SCAVG   = Double.parseDouble(strSCAVG);
			        String L_SCXXX   = strSCAVG;
			        String L_SCXXXB  = setNumberFormat((L_SCAVG-500),0);
			        String L_SCOG    = setNumberFormat((L_SCAVG-3000),0);
			        String L_SCOGXY  = setNumberFormat((L_SCAVG-4000),0);
			        String L_PSOG    = setNumberFormat((L_SCAVG-10000),0);
			        String L_PSFS    = setNumberFormat((L_SCAVG-15000),0);
			        String L_SCP     = setNumberFormat((L_SCAVG+11000),0);
			        String L_SPSGPPS = setNumberFormat((L_SCAVG+6000),0);
			        String L_5213    = setNumberFormat((L_SCAVG+6000),0);
			        String L_5295    = setNumberFormat((L_SCAVG+6000-4000),0);
			        String L_5297    = setNumberFormat((20000),0);
			        String L_5298    = setNumberFormat((20000),0);
					

			        double L_SHAVG   = Double.parseDouble(strSHAVG);
			        String L_SHXXX   = strSHAVG;
			        String L_SHXXXB  = setNumberFormat((L_SHAVG-500),0);
			        String L_SHOG    = setNumberFormat((L_SHAVG-3000),0);
			        String L_SPSHIPS = setNumberFormat((L_SHAVG+6000),0);
			        String L_5214    = setNumberFormat((L_SHAVG+6000),0);
			        String L_5221    = setNumberFormat((L_SHAVG+6000),0);
			        String L_5232    = setNumberFormat((L_SHAVG+6000),0);
			        String L_5251    = setNumberFormat((L_SHAVG+6000),0);

			        //String L_SPSXXXX = setFMT("RND",String.valueOf(((L_SCAVG+L_SHAVG)/2)+6000),0);
			        String L_5215    = setNumberFormat((((L_SCAVG+L_SHAVG)/2)+6000),0);
			        String L_5231    = setNumberFormat((((L_SCAVG+L_SHAVG)/2)+6000),0);
			        String L_5233    = setNumberFormat((((L_SCAVG+L_SHAVG)/2)+6000),0);

					
			        String L_5264    = setNumberFormat((((L_SCAVG+L_SHAVG)/2)+6000),0);
			        String L_5281    = setNumberFormat((((L_SCAVG+L_SHAVG)/2)+6000),0);
			        String L_5290    = setNumberFormat((((L_SCAVG+L_SHAVG)/2)+6000),0);
					
			        //String L_SP2157X = setFMT("RND",String.valueOf(L_SHAVG-1000),0);
			        //String L_SP6403X = setFMT("RND",String.valueOf(L_SHAVG+6000),0);



			    i=0;
			    while (true)
			    {
			            strPRDCD = tblPRDTBL.getValueAt(i,intTB2_PRDCD).toString().trim();
			            if (strPRDCD.length()==0)
			             {
			                  break;
			             }
			                L_UPDCHK = false;
			                if (L_SCAVG>0 && L_SHAVG>0)
			                {
			                        if(strPRDCD.equals("5111951610"))
			                        {
			                                tblPRDTBL.setValueAt(L_SCOG,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.equals("5111951640") || strPRDCD.equals("5111951620") || strPRDCD.equals("5111951630"))
			                        {
			                                tblPRDTBL.setValueAt(L_SCOGXY,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.equals("5195951610"))
			                        {
			                                tblPRDTBL.setValueAt(L_PSOG,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.equals("5111951680") || strPRDCD.equals("5212951680"))
			                        {
			                                tblPRDTBL.setValueAt(L_SCP,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.equals("5197951180") || strPRDCD.equals("5297951180"))
			                        {
			                                tblPRDTBL.setValueAt(L_PSFS,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5111") && strPRDCD.substring(6,7).equals("0"))
			                        {
			                                tblPRDTBL.setValueAt(L_SCXXX,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5111") && strPRDCD.substring(6,7).equals("1"))
			                        {
			                                tblPRDTBL.setValueAt(L_SCXXXB,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.equals("5112951610"))
			                        {
			                                tblPRDTBL.setValueAt(L_SHOG,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        //if(strPRDCD.equals("5215039910") || strPRDCD.equals("5215038060") || strPRDCD.equals("5215030000") || strPRDCD.equals("5215031640"))
			                        //{
			                        //        LM_PRDTBL.setValueAt(L_SP6403X,i,intTB2_AVGRT);
			                        //        L_UPDCHK = true;
			                        //}
			                        //if(strPRDCD.equals("5212409150") || strPRDCD.equals("5212409200") || strPRDCD.equals("5212409210") || strPRDCD.equals("5212951910"))
			                        //{
			                        //        LM_PRDTBL.setValueAt(L_SP2157X,i,intTB2_AVGRT);
			                        //        L_UPDCHK = true;
			                        //}
			                        else if(strPRDCD.substring(0,4).equals("5112") && strPRDCD.substring(6,7).equals("0"))
			                        {
			                                tblPRDTBL.setValueAt(L_SHXXX,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5112") && strPRDCD.substring(6,7).equals("1"))
			                        {
			                                tblPRDTBL.setValueAt(L_SHXXXB,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if((strPRDCD.substring(0,4).equals("5212")) && (!strPRDCD.equals("5212951680")))
			                        {
			                                tblPRDTBL.setValueAt(L_SPSHIPS,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5211"))
			                        {
			                                tblPRDTBL.setValueAt(L_SPSGPPS,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5213"))
			                        {
			                                tblPRDTBL.setValueAt(L_5213,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5214"))
			                        {
			                                tblPRDTBL.setValueAt(L_5214,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5215"))
			                        {
			                                tblPRDTBL.setValueAt(L_5215,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5221"))
			                        {
			                                tblPRDTBL.setValueAt(L_5221,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5231"))
			                        {
			                                tblPRDTBL.setValueAt(L_5231,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5232"))
			                        {
			                                tblPRDTBL.setValueAt(L_5232,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5233"))
			                        {
			                                tblPRDTBL.setValueAt(L_5233,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5251"))
			                        {
			                                tblPRDTBL.setValueAt(L_5251,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5264"))
			                        {
			                                tblPRDTBL.setValueAt(L_5264,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5281"))
			                        {
			                                tblPRDTBL.setValueAt(L_5281,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5290"))
			                        {
			                                tblPRDTBL.setValueAt(L_5290,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5295"))
			                        {
			                                tblPRDTBL.setValueAt(L_5295,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5297"))
			                        {
			                                tblPRDTBL.setValueAt(L_5297,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                        else if(strPRDCD.substring(0,4).equals("5298"))
			                        {
			                                tblPRDTBL.setValueAt(L_5298,i,intTB2_AVGRT);
			                                L_UPDCHK = true;
			                        }
			                }



			                if(L_UPDCHK)
			                {
			                        tblPRDTBL.setValueAt(cl_dat.M_strUSRCD_pbst,i,intTB2_AVGBY);
			                        tblPRDTBL.setValueAt(cl_dat.M_strLOGDT_pbst,i,intTB2_AVGDT);
			                        tblPRDTBL.setValueAt(new Boolean(true),i,intTB1_CHKFL);
			                }
                i += 1;
        }
  }
  catch (Exception L_EX)
  {
        setMSG(L_EX,"getCATTBL1");
  }

}
  
  
 /**We have to retrive the records on the table or Save records into the DB or any changes are 
  * have also saving the DB
  */ 
  private void exeSAVECAT()
{
  int i;
  try
  {
    i=0;
    while (true)
    {
            strPRDCD = tblCATTBL.getValueAt(i,intTB1_PRDCD).toString().trim();
            if (strPRDCD.length()==0)
             {
                  break;
             }
            if(tblCATTBL.getValueAt(i,intTB1_CHKFL).equals(new Boolean(true)))
             {
                strAVGRT = tblCATTBL.getValueAt(i,intTB1_AVGRT).toString().trim();
                strAVGBY = cl_dat.M_strUSRCD_pbst;
                strAVGDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
                //PS01_exeSAVECAT.setString(1,strAVGRT);
                //PS01_exeSAVECAT.setString(2,strAVGBY);
                //PS01_exeSAVECAT.setString(3,strAVGDT);
                //PS01_exeSAVECAT.setString(4,strPRDCD);
                //PS01_exeSAVECAT.executeUpdate();
				
				M_strSQLQRY = "update co_cdtrn set cmt_ncsvl = '"+strAVGRT+"', cmt_lusby = '"+strAVGBY+"', cmt_lupdt = '"+strAVGDT+"' where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and cmt_codcd = '"+strPRDCD+"'";
				//System.out.println("M_strSQLQRY1>>"+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
                cl_dat.exeDBCMT("exeSAVE");
             }
             i += 1;
    }
  }

  catch (Exception L_EX)
  {
        setMSG(L_EX,"exeSAVECAT");
  }
}

/**Save the records into the DB from Product Table
 */
private void exeSAVEPRD()
{
  int i;
  try
  {
    i=0;
    while (true)
    {
            strPRDCD = tblPRDTBL.getValueAt(i,intTB2_PRDCD).toString().trim();
            if (strPRDCD.length()==0)
             {
                  break;
             }
            if(tblPRDTBL.getValueAt(i,intTB2_CHKFL).equals(new Boolean(true)))
             {
                strAVGRT = tblPRDTBL.getValueAt(i,intTB2_AVGRT).toString().trim();
                strAVGBY = cl_dat.M_strUSRCD_pbst;
                strAVGDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));

				//PS02_exeSAVEPRD.setString(1,tblPRDTBL.getValueAt(i,intTB2_AVGRT).toString().trim());
                //PS02_exeSAVEPRD.setString(2,strAVGBY);
                //PS02_exeSAVEPRD.setString(3,strAVGDT);
                //PS02_exeSAVEPRD.setString(4,strAVGBY);
                //PS02_exeSAVEPRD.setString(5,strAVGDT);
                //PS02_exeSAVEPRD.setString(6,strPRDCD);
                //PS02_exeSAVEPRD.executeUpdate();
				
				M_strSQLQRY = "update co_prmst set pr_avgrt = '"+tblPRDTBL.getValueAt(i,intTB2_AVGRT).toString().trim()+"', pr_avgby = '"+strAVGBY+"', pr_avgdt = '"+strAVGDT+"', pr_lusby = '"+strAVGBY+"', pr_lupdt = '"+strAVGDT+"' where pr_prdcd = '"+strPRDCD+"'";
				//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
                cl_dat.exeDBCMT("exeSAVE");
             }
             i += 1;
    }
  }

  catch (Exception L_EX)
  {
        setMSG(L_EX,"exeSAVEPRD");
  }
}




		
		/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param   LP_FLDNM        Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
		private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP)
        {
		String L_RETVL = "";
                try
                {
                        if (LP_FLDTP.equals("C"))
                        {
                            return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString() ,"") : "";    
							
                        }
                        else if (LP_FLDTP.equals("D"))
                        {
							return LP_RSLSET.getString(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)):"";
                        }
                        else if (LP_FLDTP.equals("T"))
                        {
							return LP_RSLSET.getString(LP_FLDNM) != null ? M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM))):"";
                        }
                }
                catch (Exception L_EX)
                {
                        setMSG(L_EX,"getRSTVAL");
                }
                return "";
        }

		private class INPVF extends InputVerifier
		{		
			
			public boolean verify(JComponent input)
			{
				try
				{
					
					if(input instanceof JTextField)
						if(((JTextField)input).getText().length()==0)
							return true;
				}catch(Exception L_EX)
				{
					setMSG(L_EX,"This is input verfier");
					return false;
				}	
				return true;
			}
		}	
		
/*	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				//System.out.println("This is kdkldklf");
				if(getSource()==tblCATTBL)
				{
				  if(P_intCOLID == intTB1_PRDCD)
				   	{
						if(tblCATTBL.getValueAt(P_intCOLID,intTB1_PRDCD).toString().length() > 0)
						{
						
									//setValueAt(tblCATTBL.getSelectedRow(),tblCATTBL.getSelectedColumn());
									tblCATTBL.setRowSelectionInterval(0,0);
									tblCATTBL.setColumnSelectionInterval(0,0);
			
									tblCATTBL.getCellEditor().stopCellEditing();
						}	
					}	
				   
				}
				/*if(tblCATTBL.isEditing())
				{
					if(tblCATTBL.getValueAt(tblCATTBL.getSelectedRow(),tblCATTBL.getSelectedColumn()).toString().length()>0)
					{
						TBLINPVF obj=new TBLINPVF();
						obj.setSource(tblCATTBL);
						if(obj.verify(tblCATTBL.getSelectedRow(),tblCATTBL.getSelectedColumn()))
							tblCATTBL.getCellEditor().stopCellEditing();
						else
							return false;
					}
				}	*
			}catch(Exception L_EX)
			{
				
			}	
			return true;
		}
	}	*/
		
		
		
		
		
                
                
          }
		