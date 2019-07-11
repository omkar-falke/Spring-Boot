/*

	This module is developed to give attendance data in SAP formatas given below.
	
	------------------------------------------------------------------------------
	Per.no_new	per.no_old	Terminal_ID	Time_Event 	Clock-in_Date	Clock-in_Time
	------------------------------------------------------------------------------
	
	data is fetched from spldata.hr_sltrn1 table.
	
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.Color;
import java.lang.Number;
import java.util.Date;
import java.util.Enumeration;
import java.awt.Font;
import java.util.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.util.Date;

public class hr_pncdt1 extends JFrame implements ActionListener
{
	JPanel pnlATTEN=new JPanel();
	JButton btnSTART = new JButton("REPORT");
	//JButton btnACCEPT = new JButton("ACCEPT");
	JButton btnEXIT = new JButton("EXIT");
	JLabel lblSTRDT = new JLabel("Start Date-Time");
	JLabel lblENDDT = new JLabel("End Date-Time");
	//JTextField txtWEIGHT = new JTextField();
	JTextField txtSTRDT = new TxtDate();
	JTextField txtSTRTM = new TxtTime();
	JTextField txtENDDT = new TxtDate();
	JTextField txtENDTM = new TxtTime();
	private FileOutputStream F_OUT ;
    private DataOutputStream D_OUT ;
    //String strRPFNM = "c:\\reports\\Punching.txt";
	String strRPFNM = "c:\\reports\\Punchwrk.txt";

    //variables for DB connection
	   
	   String LP_SYSLC = "01";
	   //String LP_DTBLB = "r3e04data"; // sandbox
	   //String LP_DTBLB = "r3e03data";   // developement
	   //String LP_DTBLB = "r3e02data"; // Quality
	   //String LP_DTBLB = "r3e01data"; // Production
	   String LP_DTBLB = "spldata"; // domino
	   
	   String LP_DTBUS = "fims";
	   String LP_DTBPW = "";
	   String L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
	   
	   String L_STRURL = "jdbc:as400://SPLWS01/";  //Domino(192.168.10.131)
	   //String L_STRURL = "jdbc:as400://SPLBWPRD/"; //Sandbox(192.168.10.133)
	   //String L_STRURL = "jdbc:as400://SPLDEVQA/";   //Developement & quality(192.168.10.134)
	   //String L_STRURL = "jdbc:as400://SPLR3PRD/";   //Production(192.168.10.132)
	   
	   Connection LP_CONDTB=null;
	   Statement stmTVTDL;
	private SimpleDateFormat fmtDBDTM = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss"); 
	private SimpleDateFormat fmtLCDTM = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	private SimpleDateFormat fmtDBDAT = new SimpleDateFormat("MM/dd/yyyy");	/**	For Date in Locale format "dd/MM/yyyy" */
	private SimpleDateFormat fmtLCDAT = new SimpleDateFormat("dd/MM/yyyy");	/**	For Date in Locale format "dd/MM/yyyy" */
	static String strCMPCD = "";
	          
	public hr_pncdt1()
	{
	   if(!strCMPCD.equals("XX"))
	   {
		   //super("Employee_Information");  
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   setSize(1300,100);
		   pnlATTEN.setLayout(null);
		   
		   lblSTRDT.setBounds(200,20,100,30);
	       txtSTRDT.setBounds(300,20,100,30);
	       txtSTRTM.setBounds(400,20,50,30);
	       
	       lblENDDT.setBounds(500,20,100,30);
	       txtENDDT.setBounds(600,20,100,30);
	       txtENDTM.setBounds(700,20,50,30);
	       
	       btnSTART.setBounds(800,20,100,30);
		   btnEXIT.setBounds(950,20,75,30);
	
	//       lblWEIGHT.setBounds(300,20,100,30);
	
	//       btnACCEPT.setBounds(550,20,100,30);
		   
	//	   txtWEIGHT.setEnabled(false);
	       pnlATTEN.add(btnSTART);
	//       pnlATTEN.add(lblWEIGHT);
	//       pnlATTEN.add(txtWEIGHT);
	       pnlATTEN.add(lblSTRDT);
	       pnlATTEN.add(txtSTRDT);
	       pnlATTEN.add(txtSTRTM);
	       pnlATTEN.add(lblENDDT);
	       pnlATTEN.add(txtENDDT);
	       pnlATTEN.add(txtENDTM);
	//       pnlATTEN.add(btnACCEPT);
		   pnlATTEN.add(btnEXIT);
		   
	       getContentPane().add(pnlATTEN);
	       btnSTART.addActionListener(this);
	//       btnACCEPT.addActionListener(this);
	       btnEXIT.addActionListener(this);
	       this.setTitle("Punching Machine Data Download");
	       setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
	       setVisible(true);
	       conOPEN();
	   }
	   else if(strCMPCD.equals("XX"))
	   {
		   conOPEN();
		   getREPORT();
	   }
       txtSTRDT.setText("06/06/2010");
	   txtSTRTM.setText("07:00");
	   txtENDDT.setText("08/06/2010");
	   txtENDTM.setText("20:00");
		
	}

	
	public void actionPerformed(ActionEvent e)
	{
		  try
		  {
		   	if(e.getSource()== btnSTART)
			{
				if(txtSTRDT.getText().length()==0)
				{
					txtSTRDT.requestFocus();
					return;
				}
				else if(txtSTRTM.getText().length()==0)
				{
					txtSTRTM.requestFocus();
					return;
				}
				else if(txtENDDT.getText().length()==0)
				{
					txtENDDT.requestFocus();
					return;
				}
				else if(txtENDTM.getText().length()==0)
				{
					txtENDTM.requestFocus();
					return;
				}
		   		//System.out.println("Inside Action");
				getREPORT();
		    }
		    if(e.getSource() == btnEXIT)
			{
				conCLOSE();
				System.exit(0);
			}
		  } 
		  catch(Exception ioe)
	       {
		   	System.out.println("inside ActionPerformed :"+ioe);
		  }
	 }

  public static void main(String[] args) 
  {
  		if(args.length == 0)
  		    System.out.println("Please give CMD argument as company code");
        else if(args.length == 1)
        {
	        strCMPCD = args[0];
	    	hr_pncdt1 pm=new hr_pncdt1();
	    }
  }
  
  
   public void conOPEN()
   {
	   try
	   {
		   Class.forName(L_STRDRV);
		   L_STRURL = L_STRURL + LP_DTBLB;
		   LP_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);
		   //if(LM_CONDTB == null)
			//	return null;
			LP_CONDTB.setAutoCommit(false);
	
			SQLWarning L_STRWRN = LP_CONDTB.getWarnings();
			if ( L_STRWRN != null )
				System.out.println("Warning in setCONDTB : "+L_STRWRN);
			System.out.println("LP_CONDTB>>"+LP_CONDTB);
			stmTVTDL = LP_CONDTB.createStatement();
		}
		catch(Exception E)
		{
			JOptionPane.showMessageDialog(this,"Error in Server Connection...","Error",JOptionPane.INFORMATION_MESSAGE);
			System.out.println("conOPEN : "+E);	
		}
   }


   public void conCLOSE()
   {
	    try
	    {
			if(LP_CONDTB != null)
			{
				LP_CONDTB.commit();
				stmTVTDL.close();
				LP_CONDTB.close();
			}
		}
		catch(Exception E)
		{
			System.out.println("conCLOSE : "+E);	
		}
   }

   public void getREPORT()
   {
   		try
   		{
   			String strSTRDTM="",strENDDTM="";		// two day Previous
	   			
   			if(!strCMPCD.equals("XX"))
   			{
	   			strSTRDTM = fmtDBDTM.format(fmtLCDTM.parse(txtSTRDT.getText()+" "+txtSTRTM.getText()+":00"));
	   			strENDDTM = fmtDBDTM.format(fmtLCDTM.parse(txtENDDT.getText()+" "+txtENDTM.getText()+":00"));
			}   			
   			
   			F_OUT=new FileOutputStream(strRPFNM,true);
			D_OUT=new DataOutputStream(F_OUT); 
	   		String L_strTEMP = " SELECT SLT_EMPCD,SLT_EMPNO,SLT_TRMID,SLT_INOCD,date(SLT_PNCTM) DATE,time(SLT_PNCTM) TIME from HR_SLTRN1";
	   		       L_strTEMP +=" WHERE length(slt_empcd) = 8";
	   		       if(strCMPCD.equals("XX"))
	   		       		L_strTEMP +=" and  SLT_STSFL = '1'";
	   		       else if(strCMPCD.equals("YY"))
	   		       		L_strTEMP +=" and SLT_PNCTM between '"+strSTRDTM+"' and '"+strENDDTM+"'";
	   		       else 		
	   		       		L_strTEMP +=" and SLT_CMPCD = '"+strCMPCD+"' and SLT_PNCTM between '"+strSTRDTM+"' and '"+strENDDTM+"'";
	   		       L_strTEMP +=" order by SLT_PNCTM";
	        String L_strDATE;
	        ResultSet L_rstRSSET = stmTVTDL.executeQuery(L_strTEMP);
	        System.out.println("L_strTEMP>>"+L_strTEMP);
	        if(L_rstRSSET !=null)
	        while(L_rstRSSET.next())
	        {
	            D_OUT.writeBytes(L_rstRSSET.getString("SLT_EMPCD"));
				D_OUT.writeBytes("#");
				D_OUT.writeBytes(L_rstRSSET.getString("SLT_EMPNO"));
				D_OUT.writeBytes("#");
				D_OUT.writeBytes(L_rstRSSET.getString("SLT_TRMID"));
				D_OUT.writeBytes("#");
				D_OUT.writeBytes(L_rstRSSET.getString("SLT_INOCD"));
				D_OUT.writeBytes("#");
				D_OUT.writeBytes(fmtLCDAT.format(L_rstRSSET.getDate("DATE")));
				D_OUT.writeBytes("#");
				D_OUT.writeBytes(L_rstRSSET.getString("TIME").substring(0,8));
				D_OUT.writeBytes("#\n");
	        }	
			if(L_rstRSSET==null)
			{
				L_rstRSSET.close();
			}
			
			if(strCMPCD.equals("XX"))
	   		{
		   		String strSQLQRY = " UPDATE HR_SLTRN1 SET SLT_STSFL = '2' WHERE SLT_STSFL = '1'";
				System.out.println(strSQLQRY);	 
				stmTVTDL.executeUpdate(strSQLQRY);
				LP_CONDTB.commit();
			}

			D_OUT.close();
			F_OUT.close();
		    //Runtime r = Runtime.getRuntime();
			//Process p = null;
			//p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
			System.out.println("File Appended Successfully");
			//JOptionPane.showMessageDialog(null,"File Appended Successfully", "Verify", JOptionPane.INFORMATION_MESSAGE); 	
				
	    }
	    catch(Exception E)
	    {
	    	System.out.println("inside getREPORT() : "+E);
	    }    
   }
}  // End of class hr_pncdt1.
