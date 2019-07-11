/*
SET TRNFL 0 upto co_cttrn1
 	Class for data replication
 
	AUTHOR : AAP
	VERSION : v2.0.0
	DATE : 19/05/2004
	Last Modified	: 20/05/2004
	Documented On	: 12/06/2004
 */
import java.awt.Color;import java.awt.Component;import java.awt.Container;import java.awt.Toolkit;import java.awt.Font;
import java.sql.*;import java.awt.event.*;import javax.swing.*;import java.awt.Cursor;
import java.util.Vector;import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.DataOutputStream;import java.io.File;import java.io.FileOutputStream;
/**<BODY><P><FONT size=4><STRONG>Program Description :</STRONG></FONT>  </P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT></P><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>Application&nbsp;Name</TD>    <TD>DATA REPLICATION MODULE&nbsp; </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>      Data Rplciation.</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                 Replcates data from one databse to another. By       using connection made to                                 the location, names of the                                 collections are retrieved dynamically and depending on                  the source and destination       selected, tables in said collections are replicated. </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>        Existing Data      Replication module&nbsp;      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\co_datrp.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\co_darp.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>19/05/2004&nbsp; </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>&nbsp;&nbsp;        </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>&nbsp;</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>&nbsp;</TD></TR></TABLE><P><FONT color=purple><STRONG>   </STRONG></FONT>&nbsp;<FONT color=purple><STRONG>     Features : </STRONG></FONT></P><UL>  <LI>This class makes use of cl_pbase as component layout   tool and hence all methods in cl_pbase are available for the application   <LI>As cl_pbase is contained in it, application takes   care of change in screen resolution  <LI>By default, application continues running unless and until inttrupted by   user.   <LI>By pressing on STOP button, replication can be haulted immediately.  <LI>By pressing on EXIT button, application will   terminate after completion of current replication cycle, if in progress.  <LI>This application makes use of MetaData, getSchema to retrieve DB details   dynamically and prepared statement</LI></UL></BODY> */
class co_datrp extends JFrame implements ActionListener, FocusListener, Runnable
{
	/** DataOuputSteam to write to log file	 */
	private DataOutputStream dosREPORT;/** Thread for data replication <p> Separate thread facilitates UI updation on while replication is taking place. */
	private DataOutputStream dosERROR;/** Thread for data replication <p> Separate thread facilitates UI updation on while replication is taking place. */
	private Thread thrDATRP;/** Object of cl_pbase to accomodate all the components. <p>All components in the screen are added to this. Use of cl_pbase facilitates use of methods and veriables declared in cl_pbase  */
	private cl_pbase ocl_pbase;/** Combo to display list of replication source databases. <p> Retrieved dynamically when source location is seleced by user */
	private cl_Combo cmbDBSFM;/** Combo to display list of replication destination databases. <p> Retrieved dynamically when source location is seleced by user */
	private cl_Combo cmbDBSTO;/** Combo to display list of replication sources. <p> hard coded in a final string array */
	private cl_Combo cmbREPFM;/** Combo to display list of replication destinations. <p> hard coded in a final string array */
	private cl_Combo cmbREPTO;/**Button to start replication process */
	private JButton btnSTART;/**Button to stop replication process. Replication process will pause at the point where it is */
	private JButton btnSTOP;/**Button to stop replication process. Replication process will end after completion of present replication cycle and program will terminate */
	private JButton btnEXIT;/**Radiobutton to indicate activity at replication source */
	private JRadioButton rdbREPFM;/**Radiobutton to indicate activity at replication destination */
	private JRadioButton rdbREPTO;/**Check box for detailed log */
	private JCheckBox chbLGDTL;/**Connection object to  replication source */
	private Connection cncREPFM;/**Connection object to  replication destination */
	private Connection cncREPTO;/**Label to display status at replication source */
	private JLabel lblREPFM;/**Label to display status at replication destination */
	private JLabel lblREPTO;/**Label to display replication cycle count */
	private JLabel lblREPCT;/**Replication cycle count*/
	private int intREPCT;/**Vector to store names of tables available at replication source database */
	private Vector vtrTBLFM;/**Vector to store primary keys of tables available at replication source database */
	private Vector vtrTBLPK;/**Vector to store names of tables available at replication destination database */
	private Vector vtrTBLTO;/**Hashtable to store primary key columns of tables available at replication source database */
	private Hashtable hstTRNFL;/**flag to indicate whether to continue replication cycles */
	private boolean flgREPLICATE=true;/**flag to indicate whether to continue replcation */
	private boolean flgPAUSE=false;/**Flag for detailed log */
	private boolean flgWRTLG=true;/**Replication starting time in millis */
	private long lngSTTIM;/**String for table being transfered*/
	private String strTBLNM;/**Final String array to store replication locations' description, access code and schema names */
	private final String[] staLCDSC_fn=new String[]{"SITE-NGT","H.O.-MUM"},
		staLCTCP_fn=new String[]{"01","02"},
		staLCSNM_fn=new String[]{"SPLWS01","S103VNCM"};
	private Hashtable hstINSERT=new Hashtable(50,0.75f);
	private Hashtable hstUPDATE=new Hashtable(50,0.75f);
	private Hashtable hstTRFQR=new Hashtable(50,0.75f);
	/**
	 * To construct the screen
	 * 
	 * To construct the screen
	 * 
	 * <p>Lays Components on the screen using layout manager of cl_pbase. Populates cmbREPFM & cmbREPTO using final string arrays. Keeps SITE-NGT selected as replication source<br>Removes listeners to cl_pbase, adds to this <br>Creates log file and handler to write to the log
	 */
	co_datrp()
	{
		super("Data Replication");
		try
		{
			lngSTTIM=System.currentTimeMillis();//Class instantiation time
			cl_dat.M_dimSCRN_pbst = Toolkit.getDefaultToolkit().getScreenSize();
			cl_dat.M_dblWIDTH=(cl_dat.M_dimSCRN_pbst.width/800.00);
			cl_dat.M_dblHIGHT=((cl_dat.M_dimSCRN_pbst.height)/600.00);
			ocl_pbase=new cl_pbase();
			thrDATRP=new Thread(this);
			thrDATRP.setPriority(Thread.MAX_PRIORITY);
			ocl_pbase.M_calLOCAL=Calendar.getInstance();
			ocl_pbase.M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			ocl_pbase.setMatrix(20,7);
			JLabel lblTITLE=new JLabel("DATA REPLICATION PROGRAM");
			ocl_pbase.add(lblTITLE,3,3,1,3,ocl_pbase,'L');
			lblTITLE.setFont(new Font("Arial",Font.BOLD,20));
			lblTITLE.setForeground(Color.blue);
			ocl_pbase.add(new JLabel("Replication From"),6,2,1,1,ocl_pbase,'L');
			ocl_pbase.add(cmbREPFM=new cl_Combo(),6,3,1,1,ocl_pbase,'L');
			ocl_pbase.add(new JLabel("Replicate To"),6,5,1,1,ocl_pbase,'L');
			ocl_pbase.add(cmbREPTO=new cl_Combo(),6,6,1,1,ocl_pbase,'L');
			ocl_pbase.add(new JLabel("From Library"),8,2,1,1,ocl_pbase,'L');
			ocl_pbase.add(cmbDBSFM=new cl_Combo(),8,3,1,1,ocl_pbase,'L');
			ocl_pbase.add(new JLabel("To Library"),8,5,1,1,ocl_pbase,'L');
			ocl_pbase.add(cmbDBSTO=new cl_Combo(),8,6,1,1,ocl_pbase,'L');
			cmbDBSFM.removeKeyListener(ocl_pbase);
			ocl_pbase.add(btnSTART=new JButton("Start"),11,2,1,1,ocl_pbase,'L');
			ocl_pbase.add(btnSTOP=new JButton("Stop"),11,4,1,1,ocl_pbase,'L');
			ocl_pbase.add(btnEXIT=new JButton("Exit"),11,6,1,1,ocl_pbase,'L');
			ocl_pbase.add(chbLGDTL=new JCheckBox("Detailed Log"),16,2,1,1,ocl_pbase,'L');
			ocl_pbase.add(new JLabel("Source Status"),18,1,1,0.8,ocl_pbase,'L');
			ocl_pbase.add(rdbREPFM=new JRadioButton(""),18,1,1,0.2,ocl_pbase,'R');
			ocl_pbase.add(new JLabel("Destn. Status"),20,1,1,0.8,ocl_pbase,'L');
			ocl_pbase.add(rdbREPTO=new JRadioButton(""),20,1,1,0.2,ocl_pbase,'R');
			ocl_pbase.add(new JLabel("Cycle Count : "),22,1,1,0.8,ocl_pbase,'L');
			ButtonGroup btg=new ButtonGroup();
			btg.add(rdbREPFM);btg.add(rdbREPTO);
			lblREPFM=new JLabel("");
			lblREPFM.setBorder(BorderFactory.createLineBorder(Color.black));
			lblREPFM.setForeground(Color.blue);
			ocl_pbase.add(lblREPFM,18,2,1,6,ocl_pbase,'L');
			lblREPTO=new JLabel("");
			lblREPTO.setBorder(BorderFactory.createLineBorder(Color.black));
			lblREPTO.setForeground(Color.blue);
			ocl_pbase.add(lblREPTO,20,2,1,6,ocl_pbase,'L');
			lblREPCT=new JLabel("");
			lblREPCT.setBorder(BorderFactory.createLineBorder(Color.black));
			lblREPCT.setForeground(Color.blue);
			ocl_pbase.add(lblREPCT,22,2,1,6,ocl_pbase,'L');
			Container L_ctrTHIS=getContentPane();
			L_ctrTHIS.add(ocl_pbase);
		//ADDING LOCATION TO REPLICATION DESTINATION COMBOS
			if(staLCDSC_fn.length!=staLCTCP_fn.length)
				throw new Exception("Location Description and ID mismatch");
			cmbREPFM.addItem("Select","");
			cmbREPTO.addItem("Select","");
			for(int i=0;i<staLCDSC_fn.length;i++)
			{
				cmbREPFM.addItem(staLCDSC_fn[i],staLCTCP_fn[i]);
				cmbREPTO.addItem(staLCDSC_fn[i],staLCTCP_fn[i]);
			}
			setSize(cl_dat.M_dimSCRN_pbst.width,cl_dat.M_dimSCRN_pbst.height);
			show();
		//To close application with close button at top right corner
			addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent L_WE) {
				try
				{
					if(L_WE.getNewState()==WindowEvent.WINDOW_CLOSING)
						clsAPPCON();
					setMSG(lblREPFM,"           -------- END ---------",'N');
				}catch(Exception e)
				{
					setMSG(lblREPFM,e.toString(),'E');
				}
			 }});
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Component L_cmpTEMP=null;
		//Unregister listners of cl_pbase and register local listeners
			for(int i=0;i<ocl_pbase.M_vtrSCCOMP.size();i++)
			{
				L_cmpTEMP=(Component)ocl_pbase.M_vtrSCCOMP.elementAt(i);
				L_cmpTEMP.removeKeyListener(ocl_pbase);
				if(L_cmpTEMP instanceof JTextField)				{					((JTextField)L_cmpTEMP).removeActionListener(ocl_pbase);					((JTextField)L_cmpTEMP).removeFocusListener(ocl_pbase);
					((JTextField)L_cmpTEMP).addActionListener(this);					((JTextField)L_cmpTEMP).addFocusListener(this);				}				else if(L_cmpTEMP instanceof AbstractButton)				{					((AbstractButton)L_cmpTEMP).removeActionListener(ocl_pbase);					((AbstractButton)L_cmpTEMP).removeFocusListener(ocl_pbase);
					((AbstractButton)L_cmpTEMP).addActionListener(this);					((AbstractButton)L_cmpTEMP).addFocusListener(this);				}				else if(L_cmpTEMP instanceof JComboBox)				{					((JComboBox)L_cmpTEMP).removeActionListener(ocl_pbase);					L_cmpTEMP.removeFocusListener(ocl_pbase);
					((JComboBox)L_cmpTEMP).addActionListener(this);					L_cmpTEMP.addFocusListener(this);				}			}
			L_cmpTEMP=null;
		//CREATING LOG FILE	
			StringTokenizer L_stkTEMP=new StringTokenizer(ocl_pbase.M_fmtLCDAT.format(ocl_pbase.M_calLOCAL.getTime()),"/");
			String L_strFILNM=L_stkTEMP.nextToken();
			L_strFILNM+=L_stkTEMP.nextToken();
			File filREPORT=new File("c:\\reports\\datrp"+L_strFILNM+"t.log");
			FileOutputStream filOUT=new FileOutputStream(filREPORT);
			dosREPORT=new DataOutputStream(filOUT);
			dosREPORT.writeBytes("Data Replcication instantiated At : "+ocl_pbase.M_fmtLCDTM.format(ocl_pbase.M_calLOCAL.getTime())+"\n");
			File filERROR=new File("c:\\reports\\datrp"+L_strFILNM+"_ERR.log");
			FileOutputStream filERROUT=new FileOutputStream(filERROR);
			dosERROR=new DataOutputStream(filERROUT);
			dosERROR.writeBytes("Data Replcication instantiated At : "+ocl_pbase.M_fmtLCDTM.format(ocl_pbase.M_calLOCAL.getTime())+"\n");

			cmbREPFM.setSelectedItem(staLCDSC_fn[0]);
			L_stkTEMP=null;
			L_strFILNM=null;
			System.gc();
			setENBL(true);
		}
		catch(Exception e)
		{
		 setMSG(lblREPFM,e.toString(),'E');}
	}
	/**
	 * Implementation of Run() in Thread
	 * 
	 * Implementation of Run() in Thread
	 * 
	 * <p>Starts and continues replcation untill flgREPLICATE is true
	 */
	public void run()
	{
		try
		{
			if(Thread.currentThread()==thrDATRP)
				while(flgREPLICATE && !flgPAUSE)
				{System.out.println("Starting next cycle. Free Memory : "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
				 //RE-Establish connections
					if(cncREPFM != null)
					{
						cncREPFM.clearWarnings();
						cncREPFM.close();
					}
					if(cncREPTO != null)
					{
						cncREPTO.clearWarnings();
						cncREPTO.close();
					}
					cncREPFM = null;
					cncREPTO = null;
					System.gc();
					cncREPFM=setCONDTB(cmbREPFM.getITMCD().toString(),cmbDBSFM.getSelectedItem().toString(),"FIMS","FIMS");
					cncREPTO=setCONDTB(cmbREPTO.getITMCD().toString(),cmbDBSTO.getSelectedItem().toString(),"FIMS","FIMS");
					if(cncREPFM == null)
					{//Sleep for 5 min. and try again
						setMSG(lblREPFM,"FAILED to connect to source .. Will retry after 5 minutes ..",'E');
						Thread.currentThread().sleep(300000);
						continue;
//						return;
					}
					else
						setMSG(lblREPFM,"Connectted to Source .. ",'N');
					if(cncREPTO == null)
					{//Sleep for 5 min. and try again
						setMSG(lblREPTO,"FAILED to connect to Destination .. Will retry after 5 minutes ..",'E');
						Thread.currentThread().sleep(300000);
						continue;
//						return;
					}
					else
						setMSG(lblREPTO,"Connected to Destination ..",'N');
					System.gc();
					chkTABLES();
				}
		}catch(Exception e)
		{
			setMSG(lblREPFM,e.toString(),'E');
		}
	}
	/**
	 * Method to Display Message to user
	 * 
	 * Method to Display Message to user
	 * 
	 * <p>Sets foreground of the label where msg. is to be displayed and displays the message<br>Writes the message to log if flgWRTLG is true
	 * 
	 * @param P_lblMESG Label where message is to be displayed
	 * @param P_strMESG Message to be displayed
	 * @param P_chrMSGTP Message type, <br>'N' : Normal, Otherwise Error
	 */
	private void setMSG(JLabel P_lblMESG,String P_strMESG,char P_chrMSGTP)
	{
		try
		{
			if(P_lblMESG == lblREPFM)
				rdbREPFM.setSelected(true);
			else
				rdbREPTO.setSelected(true);
			if(P_chrMSGTP == 'N')
			{
				P_lblMESG.setForeground(Color.blue);
				P_lblMESG.setText(P_strMESG);
				if(flgWRTLG)
					dosREPORT.writeBytes(P_strMESG+"   at "+ocl_pbase.M_fmtLCDTM.format(new java.util.Date(System.currentTimeMillis()))+"\n");
			}
			else
			{
				P_lblMESG.setForeground(Color.red);
				P_lblMESG.setText(P_strMESG);
				if(flgWRTLG)
					dosERROR.writeBytes(strTBLNM+" : " +P_strMESG+"   at "+ocl_pbase.M_fmtLCDTM.format(new java.util.Date(System.currentTimeMillis()))+"\n");
			}
			dosREPORT.flush();
			dosERROR.flush();
			P_strMESG=null;
			System.gc();
		}
		catch (Exception e)
		{
			setMSG(lblREPFM,e.toString(),'E');
		}
	}
	/** 
	 * Establishes connection to DB.
	 * 
	 * Establishes connection to DB.
	 * 
	 * "01" : SITE,
	 * <br>"02" : H.O. MUMBAI<br>
	 * "03" : reserved for new location, if any
	 */
	private  Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
    {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
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
				L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
				L_STRURL = "jdbc:as400://SPLHOS01/";
				Class.forName(L_STRDRV);
/*				int port = 50000;
				LP_DTBUS = "SPLDATA";
				LP_DTBPW = "SPLDATA";

				L_STRURL = "jdbc:db2://" + "splhos01" + ":" + 50000 + "/" ;
				Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
*/			}
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
			return LM_CONDTB;
		}
		catch(java.lang.Exception L_EX)
		{
			setMSG(lblREPTO,"Error while connecting to DB : "+L_EX,'E');
			return null;
		}
		finally{this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));}
	}
/*	public void keyTyped(KeyEvent L_KE)
	{
		try
		{
			Object L_objSOURC=L_KE.getSource();
		}catch(Exception e)
		{System.out.println("Error in keyTyped\n"+e);}
	}
*/	

	/**	 * Method to close the application	 * 	 * Method to close the application	 * 	 * <p>sets flgREPLICATE to false so that, replication will hault after current cycle. Waits for replication thread to complete. if connection to source database is live, rollbacks and closes the connection.<br>If connection to destination is live, commits and closes the connection.<br>Writes names and reasons of tables not updated to log file	 */	private void clsAPPCON() throws Exception	{		flgREPLICATE=false;		hide();		if(thrDATRP.isAlive())			thrDATRP.join();		setMSG(lblREPFM,"Disconnecting DataBase ",'N');		if(cncREPFM!=null)		{			cncREPFM.rollback();			cncREPFM.close();		}		if(cncREPTO!=null)		{			cncREPTO.commit();			cncREPTO.close();		}		setMSG(lblREPFM,"Disconnected ",'N');		dosERROR.writeBytes("Tables not transfered : \n");		String L_strTBLNA=" TABLE NOT AVAIALBE AT DESTINATION,\n ",L_strPKEYNA=" PRIMARY KEY NOT AVAIALBE,\n ";
		if(vtrTBLFM!=null)			for(int i=0;i<vtrTBLFM.size();i++)			{				if(!vtrTBLTO.contains(vtrTBLFM.elementAt(i)))					dosERROR.writeBytes(vtrTBLFM.elementAt(i).toString()+L_strTBLNA);				else if(vtrTBLPK.size()>i)
				{
					if(((Object[])vtrTBLPK.elementAt(i)).length == 0)						dosERROR.writeBytes(vtrTBLFM.elementAt(i).toString()+L_strPKEYNA);
				}			}		ocl_pbase.M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");		dosREPORT.writeBytes("\nTOTAL TIME ELAPSED : "+ocl_pbase.calTIME(ocl_pbase.M_fmtLCDTM.format(new java.util.Date(System.currentTimeMillis())),ocl_pbase.M_fmtLCDTM.format(new java.util.Date(lngSTTIM))));		dosERROR.flush();
		dosREPORT.flush();		dosERROR.close();
		dosREPORT.close();	}

	/**	 * Method to validate the data	 * 	 * Method to validate the data	 * 	 * <P>Checks whether surce and destinations are not same<br>if data is being replicated to SPLDATA at site, confirms with user	 */	private boolean vldDATA()	{		if(cmbREPFM.getSelectedItem().equals(cmbREPTO.getSelectedItem())		   &&cmbDBSFM.getSelectedItem().equals(cmbDBSTO.getSelectedItem()))		{			setMSG(lblREPFM,"Source and Destination cannot be same ..",'E');			return false;		}		if(cmbREPTO.getITMCD().equals("01"))		{	//			JOptionPane.showConfirmDialog(this,"You are replicating to SPLDATA at SITE\nDo you wish to continue ?","CONFIRM",JOptionPane.YES_NO_OPTION);			if(0 != JOptionPane.showConfirmDialog(this,"You are replicating to SPLDATA at SITE\nDo you wish to continue ?","CONFIRM",JOptionPane.YES_NO_OPTION))				return false;		}		return true;	}
	
	private void setENBL(boolean P_flgSTATE)
	{
		cmbREPFM.setEnabled(P_flgSTATE);cmbREPTO.setEnabled(P_flgSTATE);
		cmbDBSFM.setEnabled(P_flgSTATE);cmbDBSTO.setEnabled(P_flgSTATE);
		btnSTART.setEnabled(P_flgSTATE);btnSTOP.setEnabled(!P_flgSTATE);
		btnEXIT.setEnabled(!P_flgSTATE);
	}

	/**
	 * <b>TASKS :</B><BR>
	 * btnSTART : Start replication if replication thread is not active<br>
	 * btnSTOP : Set flgPAUSE flag to true so that, replication will hault after current row.
	 * btnEXIT : Close application after completion of current replication cycle and then exit the system<br>
	 * cmbREPFM : If selected index is not zero, Connect to the location, retieve the name of the collections and populate Database from combo.<br>
	 * cmbREPTO : If selected index is not zero, Connect to the location, retieve the name of the collections and populate Database from combo.<br>
	 * cmbDBSFM : If Selected Index > 0, Connect to the database. Retieve the list of tables and populate vtrTBLFM. Initialise vector for primary keys (vtrTBLPK). Populate hstTRNFL with name of transfer flag columns<br>
	 * cmbDBSTO : Connect to the database and populate vtrTBLTO with list of tables.
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			Object L_objSOURC=L_AE.getSource();
			if(L_objSOURC == btnSTART && !thrDATRP.isAlive())
			{//START REPLICATION
				flgPAUSE=false;
				if(vldDATA())
				{
					
//					while(flgREPLICATE && !flgPAUSE)
					{
//						System.gc();
//						thrDATRP=new Thread(this);
//						thrDATRP.setPriority(7);
						setENBL(false);
						thrDATRP.start();
					}
					
				}
				
			}
			else if(L_objSOURC==btnSTOP)//SET PAUSE FLAG TO TRUE
				flgPAUSE=true;
			else if(L_objSOURC==btnEXIT)
			{//CLOSE APPLICATION & EXIT
				clsAPPCON();
				System.exit(0);
			}
			else if(L_objSOURC==cmbREPFM)
			{
				if(cmbREPFM.getSelectedIndex()>0)
				{//CONNECTING TO DATABASE
					cncREPFM=setCONDTB(cmbREPFM.getITMCD().toString(),"SPLDATA","FIMS","FIMS");
					if(cncREPFM!=null)
						setMSG(lblREPFM,"Connected to "+cmbREPFM.getSelectedItem(),'N');
					else
					{
						setMSG(lblREPFM,"Connection FAILED to "+cmbREPFM.getSelectedItem(),'E');
						return;
					}
					DatabaseMetaData L_dmtTEMP=cncREPFM.getMetaData();
					ResultSet L_rstDBMTD=L_dmtTEMP.getSchemas();//LIST OF COLLECTIONS
					if(L_rstDBMTD!=null)
					{
						cmbDBSFM.removeAllItems();
						cmbDBSFM.addItem("Select");
						while(L_rstDBMTD.next())//POLULAATE CMBDSBFM
							if(L_rstDBMTD.getString(1).substring(0,3).equalsIgnoreCase("SPL"))
								cmbDBSFM.addItem(L_rstDBMTD.getString(1));
						L_rstDBMTD.close();
					}
				}
			}
			else if(L_objSOURC==cmbREPTO)
			{
				if(cmbREPTO.getSelectedIndex()>0)
				{
					cncREPTO=setCONDTB(cmbREPTO.getITMCD().toString(),"SPLDATA","FIMS","FIMS");
					if(cncREPTO!=null)
						setMSG(lblREPTO,"Connected to "+cmbREPTO.getSelectedItem(),'N');
					else
					{
						setMSG(lblREPTO,"Connection FAILED to "+cmbREPTO.getSelectedItem(),'E');
						return;
					}
					DatabaseMetaData L_dmtTEMP=cncREPTO.getMetaData();
					ResultSet L_rstDBMTD=L_dmtTEMP.getSchemas();
					if(L_rstDBMTD!=null)
					{
						cmbDBSTO.removeAllItems();
						cmbDBSTO.addItem("Select");
						while(L_rstDBMTD.next())
							if(L_rstDBMTD.getString(1).substring(0,3).equalsIgnoreCase("SPL") && !(cmbREPTO.getITMCD().equals("01") && L_rstDBMTD.getString(1).equalsIgnoreCase("SPLDATA")))
								cmbDBSTO.addItem(L_rstDBMTD.getString(1));
						L_rstDBMTD.close();
					}
				}
			}
			else if(L_objSOURC==cmbDBSFM)
			{
				if(cmbDBSFM.getSelectedIndex()>0)
				{//CONNECT TO THE DATABASE
					cncREPFM=setCONDTB(cmbREPFM.getITMCD().toString(),cmbDBSFM.getSelectedItem().toString(),"FIMS","FIMS");
					if(cncREPFM!=null)
						setMSG(lblREPFM,"Connected to "+cmbDBSFM.getSelectedItem() +" AT "+cmbREPFM.getSelectedItem(),'N');
					else
					{
						setMSG(lblREPFM,"Connected FAILED to "+cmbDBSFM.getSelectedItem() +" AT "+cmbREPFM.getSelectedItem(),'E');
						return;
					}
					
				//RETRIEVING LIST OF TABLES AT SOURCE
					//ResultSet L_rstRSSET=cncREPFM.getMetaData().getTables(staLCSNM_fn[cmbREPFM.getSelectedIndex()-1],cmbDBSFM.getSelectedItem().toString(),null,new String[]{"TABLE","VIEW"});
					ResultSet L_rstRSSET=cncREPFM.createStatement().executeQuery("Select Distinct TABLE_NAME, COLUMN_NAME from SYSCOLUMNS where COLUMN_NAME like '%TRNFL'");
					if(L_rstRSSET!=null)
					{
						setMSG(lblREPFM,"Retrieving List of Tables, pl. wait ..",'N');
						vtrTBLFM=new Vector(10,5);vtrTBLPK=new Vector(10,5);
						hstTRNFL=new Hashtable(25,0.75f);
						while(L_rstRSSET.next())
						{//POPULATE VTRTBLFM AND HSTTRNFL
							vtrTBLFM.addElement(L_rstRSSET.getString("TABLE_NAME"));
							hstTRNFL.put(L_rstRSSET.getString("TABLE_NAME"),L_rstRSSET.getString("COLUMN_NAME"));
						}
						L_rstRSSET.close();
						setMSG(lblREPFM,"List of Tables prepared",'N');
					}
				}
			}
			else if(L_objSOURC == cmbDBSTO)
			{
				if(cmbDBSTO.getSelectedIndex()>0)
				{//CONNECT TO THE DATABASE
					cncREPTO=setCONDTB(cmbREPTO.getITMCD().toString(),cmbDBSTO.getSelectedItem().toString(),"FIMS","FIMS");
					if(cncREPTO!=null)
						setMSG(lblREPTO,"Connected to "+cmbDBSTO.getSelectedItem()+" AT "+cmbREPTO.getSelectedItem(),'N');
					else
					{
						setMSG(lblREPTO,"Connected FAILED to "+cmbDBSTO.getSelectedItem()+" AT "+cmbREPTO.getSelectedItem(),'E');
						return;
					}
				//RETIRVING LIST OF TABLES AT DESTINATION
					ResultSet L_rstRSSET=cncREPTO.getMetaData().getCatalogs();
					L_rstRSSET=cncREPTO.getMetaData().getTables(staLCSNM_fn[cmbREPTO.getSelectedIndex()-1],cmbDBSTO.getSelectedItem().toString(),null,new String[]{"TABLE","VIEW"});
					if(L_rstRSSET!=null)
					{
						vtrTBLTO=new Vector(10,5);
						while(L_rstRSSET.next())
							vtrTBLTO.addElement(L_rstRSSET.getString(3));
						L_rstRSSET.close();
					}
				}
			}
		}catch(Exception e)
		{setMSG(lblREPFM,"Error in Action Performed "+e,'E');}
	}

	/**	 * Method to check validity of table for replication	 * 	 * Method to check validity of table for replication	 * 	 * <p> Retrieves list of primary keys and puts in a vector.<BR>if table is not available at destination, constructs query and created the table at destination.<br>If table at source has primary key, calls replication other wise contiuues	 */	private void chkTABLES()	{		lblREPCT.setText(Integer.toString(++intREPCT));		int i=0;
		Vector L_vtrTEMP=new Vector(3,2);		Vector L_vtrFMCOL=new Vector(3,2);		Vector L_vtrTOCOL=new Vector(3,2);
		ResultSet L_rstRSSET=null;
		try		{			rdbREPTO.setVisible(true);rdbREPFM.setVisible(true);
			for (i=0;i<vtrTBLFM.size()&&!flgPAUSE;i++)			{				
				strTBLNM=(String)vtrTBLFM.elementAt(i);				if(intREPCT<2)
				{
			//RETRIEVING KEYS OF TABLE					L_rstRSSET=cncREPFM.getMetaData().getBestRowIdentifier("SPLWS01",cmbDBSFM.getSelectedItem().toString(),vtrTBLFM.elementAt(i).toString(),0,true);
										L_vtrTEMP=new Vector(3,2);					while(L_rstRSSET.next())					{						if(vtrTBLFM.elementAt(i).toString().equals("HR_FLMST"))							System.out.println(L_rstRSSET.getString(2));
						L_vtrTEMP.addElement(L_rstRSSET.getString(2));
					}					vtrTBLPK.addElement(L_vtrTEMP.toArray());					if(((Object[])vtrTBLPK.elementAt(i)).length>0 && vtrTBLTO.contains(vtrTBLFM.elementAt(i)))					{						L_vtrFMCOL=new Vector(3,2);						L_vtrTOCOL=new Vector(3,2);					//RETRIEVE STRUCTURE AT SOURCE AND PUT IN VECTOR						L_rstRSSET=cncREPFM.createStatement().executeQuery("Select * from SYSCOLUMNS where TABLE_NAME='"+vtrTBLFM.elementAt(i)+"' order by COLUMN_NAME");
						while(L_rstRSSET.next())
							L_vtrFMCOL.addElement(L_rstRSSET.getString("COLUMN_NAME")+"|"+(L_rstRSSET.getString("DATA_TYPE").equalsIgnoreCase("TIMESTMP") ? "TIMESTAMP" : L_rstRSSET.getString("DATA_TYPE"))+"|("+L_rstRSSET.getString("LENGTH")+","+L_rstRSSET.getString("NUMERIC_SCALE")+")");					//RETRIEVE STRUCTURE AT DESTN AND PUT IN VECTOR
						L_rstRSSET=cncREPTO.createStatement().executeQuery("Select * from SYSCOLUMNS where TABLE_NAME='"+vtrTBLFM.elementAt(i)+"'");
						while(L_rstRSSET.next())
							L_vtrTOCOL.addElement(L_rstRSSET.getString("COLUMN_NAME")+"|"+(L_rstRSSET.getString("DATA_TYPE").equalsIgnoreCase("TIMESTMP") ? "TIMESTAMP" : L_rstRSSET.getString("DATA_TYPE"))+"|("+L_rstRSSET.getString("LENGTH")+","+L_rstRSSET.getString("NUMERIC_SCALE")+")");						for(int j=0;j<L_vtrFMCOL.size();j++)							if(L_vtrTOCOL.contains(L_vtrFMCOL.elementAt(j)))
								L_vtrFMCOL.removeElement(L_vtrFMCOL.elementAt(j--));
						if(L_vtrFMCOL.size()>0)						{							cncREPTO.createStatement().execute("Drop table "+vtrTBLFM.elementAt(i)+" cascade");							cncREPTO.commit();							cncREPTO.clearWarnings();
							vtrTBLTO.removeElement(vtrTBLFM.elementAt(i));
						}					}				//CHCKE TABLE PRESENT AT DESTINATION, OTHERWISE, CREATE IT					if(!vtrTBLTO.contains(vtrTBLFM.elementAt(i)))//&&(vtrTBLFM.elementAt(i).toString().substring(0,2).equalsIgnoreCase("MR") || vtrTBLFM.elementAt(i).toString().equalsIgnoreCase("MR_INTRN")))					{
						cncREPFM.createStatement().executeUpdate("update "+vtrTBLFM.elementAt(i)+" set "+hstTRNFL.get(vtrTBLFM.elementAt(i))+" ='0'");						cncREPFM.commit();						String L_strQUERY="create table "+vtrTBLFM.elementAt(i) +" (";						L_rstRSSET=cncREPFM.createStatement().executeQuery("Select * from SYSCOLUMNS where TABLE_NAME='"+vtrTBLFM.elementAt(i)+"' order by ORDINAL_POSITION");						if(L_rstRSSET!=null)						{							while(L_rstRSSET.next())							{
								L_strQUERY+=L_rstRSSET.getString("COLUMN_NAME")+" "+(L_rstRSSET.getString("DATA_TYPE").equalsIgnoreCase("TIMESTMP") ? "TIMESTAMP" : L_rstRSSET.getString("DATA_TYPE"));								if(!(L_rstRSSET.getString("DATA_TYPE").trim().equalsIgnoreCase("DATE") || L_rstRSSET.getString("DATA_TYPE").trim().equalsIgnoreCase("TIMESTMP")))								{									L_strQUERY+="("+L_rstRSSET.getString("LENGTH");									if(ocl_pbase.nvlSTRVL(L_rstRSSET.getString("NUMERIC_SCALE"),"").length()>0)										L_strQUERY+=","+L_rstRSSET.getString("NUMERIC_SCALE");									L_strQUERY+=")";								}								if(L_rstRSSET.getString("IS_NULLABLE").equals("N"))									L_strQUERY+=" NOT NULL ";								if(L_rstRSSET.getString("HAS_DEFAULT").equals("Y"))								{									if(L_rstRSSET.getString("COLUMN_DEFAULT")==null)										L_strQUERY+=" DEFAULT NULL";									else										L_strQUERY+=" DEFAULT "+ocl_pbase.nvlSTRVL(L_rstRSSET.getString("COLUMN_DEFAULT"),"");								}								L_strQUERY+=", \n  ";							}							//FOR PRIMARY KEY CREATION							if(((Object[])vtrTBLPK.elementAt(i)).length>0)							{								L_strQUERY+=" CONSTRAINT ";//+vtrTBLFM.elementAt(i);								ResultSet L_rstPMKEY=cncREPFM.createStatement().executeQuery("Select * from SYSKEYCST where TABLE_NAME='"+vtrTBLFM.elementAt(i)+"' order by ORDINAL_POSITION");								if(L_rstPMKEY!=null)								{									boolean L_flgFIRST=true;									while(L_rstPMKEY.next())									{										if(L_flgFIRST)										{
											L_strQUERY+=ocl_pbase.nvlSTRVL(L_rstPMKEY.getString("CONSTRAINT_NAME"),"") +" PRIMARY KEY(";											L_flgFIRST=false;										}//										L_strQUERY+=L_rstPMKEY.getString("COLUMN_NAME")+",";									if(vtrTBLFM.elementAt(i).equals("HR_FLMST"))
												System.out.println(L_rstPMKEY.getString("COLUMN_NAME"));
									}
									Object [] L_obaTBLPK=(Object[])vtrTBLPK.elementAt(i);									for(int z=0;z<L_obaTBLPK.length;z++)										L_strQUERY+=L_obaTBLPK[z].toString()+",";
									L_strQUERY=L_strQUERY.substring(0,L_strQUERY.length()-1)+"), \n  ";								}							}//CONSTRAINT MM_BILTR PRIMARY KEY(BIL_BILTP,BIL_DOCNO,BIL_DOCRF,BIL_MATCD))

							System.out.println(L_strQUERY.substring(0,L_strQUERY.length()-5)+")");							cncREPTO.createStatement().executeUpdate(L_strQUERY.substring(0,L_strQUERY.length()-5)+")");							cncREPTO.commit();cncREPTO.clearWarnings();							setMSG(lblREPTO,"Table created : "+vtrTBLFM.elementAt(i),'N');						}					}					
				}				
/* TO TEST MR_INMST & MR_INTNR REPLICATION, UNCOMMENT NEXT LINE COMMENT NEXT TO IT */				 				if(((Object[])vtrTBLPK.elementAt(i)).length>0)// && (vtrTBLFM.elementAt(i).toString().substring(0,2).equalsIgnoreCase("MR") || vtrTBLFM.elementAt(i).toString().equalsIgnoreCase("MR_INTRN")))
	//			if(((Object[])vtrTBLPK.elementAt(i)).length>0)					trfTBLDAT(i);			}	//			setMSG(lblREPFM,"Replication Completed ..",'N');			setMSG(lblREPTO,"Replication Completed .. Cycle no. :"+intREPCT,'N');			rdbREPTO.setVisible(false);rdbREPFM.setVisible(false);
		}catch(Exception e)		{			setMSG(lblREPFM,e.toString(),'E');
		}
		finally
		{			L_vtrTEMP=null;
			L_rstRSSET=null;		}	}

	/**	 * Method to replicate data of a table	 * 	 * Method to replicate data of a table	 * 	 * <p>Constructs qury for trnfl=0, fires it at source. by using MetaData of the resultset obtained, constructs Prepares statments for inserting as well as updating the values. <br>Tries to insert the values in the resultset. On DeplicateKey error, updates it<br>Writes to log no of rows inserted and updated	 */	private void trfTBLDAT(int P_intINDEX) //throws Exception	{		ResultSet L_rstRSSET=null;//Declare at top to facilitate closing in final section of try		String L_strLINDS="";////Declare at top to facilitate printing in final section of try
		String L_strTBLNM=vtrTBLFM.elementAt(P_intINDEX).toString();//Name of the table		String L_strINSERT=null,//INSERT QURY for destination				L_strUPDATE=null,
				L_strTRNFL=null,				L_strQUERY=null;
		ResultSetMetaData L_rmtTEMP=null;
		String[] L_staDATTP=null;
		Object[] L_obaTBLPK=null;
		PreparedStatement L_pstINSERT=null;
		PreparedStatement L_pstUPDATE=null;
		PreparedStatement L_pstTRNFL=null;		try		{			int L_intINSERT=0,//COUNTER FOR NO. Of ROWS inserted				L_intUPDATE=0;//COUNTER FOR NO. Of ROWS updated			L_strTBLNM=vtrTBLFM.elementAt(P_intINDEX).toString();//Name of the table						L_strQUERY="Select * from "+L_strTBLNM;//SELECT QURY for source
			//ADDING TRANSFER FLAG FILTER				if(vtrTBLTO.contains(vtrTBLFM.elementAt(P_intINDEX)))//If table is created during this replication, select all records					L_strQUERY+=" where "+hstTRNFL.get(L_strTBLNM)+"='0'";				setMSG(lblREPFM,"Collecting data to replicate from "+L_strTBLNM,'N');				L_rstRSSET=cncREPFM.createStatement().executeQuery(L_strQUERY);				L_rmtTEMP=L_rstRSSET.getMetaData();//For column names				L_staDATTP=new String[L_rmtTEMP.getColumnCount()];//ARRAY FOR COLUMN NAMES				for(int i=0;i<L_rmtTEMP.getColumnCount();i++)				{//Adding column names to qry and data types in string array			//		L_strINSERT+=L_rmtTEMP.getColumnName(i+1)+",";			//		L_strUPDATE+=L_rmtTEMP.getColumnName(i+1)+" = ?,";					L_staDATTP[i]=L_rmtTEMP.getColumnTypeName(i+1);				}
				if(hstINSERT.containsKey(L_strTBLNM) && false)			{
				L_pstINSERT=(PreparedStatement)hstINSERT.get(L_strTBLNM);				L_pstUPDATE=(PreparedStatement)hstUPDATE.get(L_strTBLNM);
				L_pstTRNFL=(PreparedStatement)hstTRFQR.get(L_strTBLNM);			}			else
			{
				L_strINSERT="insert into "+L_strTBLNM+" (";//INSERT QURY for destination				L_strUPDATE="Update "+L_strTBLNM+" set ";//UPDATE QURY for destination
				L_strTRNFL="Update "+L_strTBLNM+" set "+hstTRNFL.get(L_strTBLNM)+"='1' where ";//TRNFL at SOURCE 				L_strQUERY="Select * from "+L_strTBLNM;//SELECT QURY for source
			//ADDING TRANSFER FLAG FILTER				if(vtrTBLTO.contains(vtrTBLFM.elementAt(P_intINDEX)))//If table is created during this replication, select all records					L_strQUERY+=" where "+hstTRNFL.get(L_strTBLNM)+"='0'";				setMSG(lblREPFM,"Collecting data to replicate from "+L_strTBLNM,'N');				L_rstRSSET=cncREPFM.createStatement().executeQuery(L_strQUERY);				L_rmtTEMP=L_rstRSSET.getMetaData();//For column names				L_staDATTP=new String[L_rmtTEMP.getColumnCount()];//ARRAY FOR COLUMN NAMES				for(int i=0;i<L_rmtTEMP.getColumnCount();i++)				{//Adding column names to qry and data types in string array					L_strINSERT+=L_rmtTEMP.getColumnName(i+1)+",";					L_strUPDATE+=L_rmtTEMP.getColumnName(i+1)+" = ?,";					L_staDATTP[i]=L_rmtTEMP.getColumnTypeName(i+1);				}				L_strINSERT=L_strINSERT.substring(0,L_strINSERT.length()-1)+") values (";				for(int i=0;i<L_rmtTEMP.getColumnCount();i++)					L_strINSERT+="?,";//INSERT QURY Appended with question marks.				L_strINSERT=L_strINSERT.substring(0,L_strINSERT.length()-1)+")";				L_obaTBLPK=(Object[])vtrTBLPK.elementAt(P_intINDEX);//ARRAY OF PRIMARY KEY COLUMNS
			//ADDING PRIMARY KEY IN WHERE PART OF UPDATAE AND TRNFL QRY				if(L_obaTBLPK.length>0)				{					L_strUPDATE=L_strUPDATE.substring(0,L_strUPDATE.length()-1)+" where ";					for(int i=0;i<L_obaTBLPK.length;i++)					{
						L_strUPDATE+=L_obaTBLPK[i].toString()+" = ? and ";						L_strTRNFL+=L_obaTBLPK[i].toString()+" = ? and ";
					}					L_strUPDATE=L_strUPDATE.substring(0,L_strUPDATE.length()-4);
					L_strTRNFL=L_strTRNFL.substring(0,L_strTRNFL.length()-4);				}				L_pstINSERT=cncREPTO.prepareStatement(L_strINSERT);				L_pstUPDATE=cncREPTO.prepareStatement(L_strUPDATE);
				L_pstTRNFL=cncREPFM.prepareStatement(L_strTRNFL);
//				hstINSERT.put(L_strTBLNM,L_pstINSERT);
//				hstUPDATE.put(L_strTBLNM,L_pstUPDATE);
//				hstTRFQR.put(L_strTBLNM,L_pstTRNFL);
			}
			int L_intCOLID=0;			setMSG(lblREPFM,"Completed",'N');			if(L_rstRSSET!=null)			{				setMSG(lblREPTO,"Data transfer for "+L_strTBLNM+" in progress",'N');				while(L_rstRSSET.next()&& !flgPAUSE)//Check flag for btnSTOP				{					try					{						L_intCOLID++;						for(int i=0;i<L_rmtTEMP.getColumnCount();i++)						{							if(L_staDATTP[i].equalsIgnoreCase("VARCHAR"))							{								L_pstINSERT.setString(i+1,L_rstRSSET.getString(i+1));								L_pstUPDATE.setString(i+1,L_rstRSSET.getString(i+1));							}
							else if(L_staDATTP[i].equalsIgnoreCase("CHAR"))							{								L_pstINSERT.setString(i+1,L_rstRSSET.getString(i+1));								L_pstUPDATE.setString(i+1,L_rstRSSET.getString(i+1));							}							else if(L_staDATTP[i].equalsIgnoreCase("DATE"))							{								L_pstINSERT.setDate(i+1,L_rstRSSET.getDate(i+1));								L_pstUPDATE.setDate(i+1,L_rstRSSET.getDate(i+1));							}							else if(L_staDATTP[i].equalsIgnoreCase("TIMESTAMP"))							{								L_pstINSERT.setTimestamp(i+1,L_rstRSSET.getTimestamp(i+1));								L_pstUPDATE.setTimestamp(i+1,L_rstRSSET.getTimestamp(i+1));							}							else if(L_staDATTP[i].equalsIgnoreCase("DECIMAL"))							{								L_pstINSERT.setDouble(i+1,L_rstRSSET.getDouble(i+1));								L_pstUPDATE.setDouble(i+1,L_rstRSSET.getDouble(i+1));							}
							else if(L_staDATTP[i].equalsIgnoreCase("NUMERIC"))							{								L_pstINSERT.setDouble(i+1,L_rstRSSET.getDouble(i+1));								L_pstUPDATE.setDouble(i+1,L_rstRSSET.getDouble(i+1));							}
							else 
							{								setMSG(lblREPFM,"Data Type Not found : "+L_staDATTP[i],'E');								continue;
							}						}						L_obaTBLPK=(Object[])vtrTBLPK.elementAt(P_intINDEX);						L_strLINDS="Transfering ";						for(int i=0;i<L_obaTBLPK.length;i++)						{//SETTING VALUES FOR PRIMARY KEY FILTER IN TRNFL & UPDATE QRY							int j=0;							for(j=0;j<L_rmtTEMP.getColumnCount();j++)								if(L_obaTBLPK[i].equals(L_rmtTEMP.getColumnName(j+1)))									break;							if(L_staDATTP[j].equalsIgnoreCase("VARCHAR"))							{
								L_pstUPDATE.setString(L_rmtTEMP.getColumnCount()+i+1,L_rstRSSET.getString(j+1));								L_pstTRNFL.setString(i+1,L_rstRSSET.getString(j+1));
							}							else if(L_staDATTP[j].equalsIgnoreCase("DATE"))							{
								L_pstUPDATE.setDate(L_rmtTEMP.getColumnCount()+i+1,L_rstRSSET.getDate(j+1));
								L_pstTRNFL.setDate(i+1,L_rstRSSET.getDate(j+1));
							}							else if(L_staDATTP[j].equalsIgnoreCase("TIMESTAMP"))							{
								L_pstUPDATE.setTimestamp(L_rmtTEMP.getColumnCount()+i+1,L_rstRSSET.getTimestamp(j+1));								L_pstTRNFL.setTimestamp(i+1,L_rstRSSET.getTimestamp(j+1));
							}							else if(L_staDATTP[j].equalsIgnoreCase("DECIMAL")||L_staDATTP[j].equalsIgnoreCase("NUMERIC"))							{
								L_pstUPDATE.setDouble(L_rmtTEMP.getColumnCount()+i+1,L_rstRSSET.getDouble(j+1));
								L_pstTRNFL.setDouble(i+1,L_rstRSSET.getDouble(j+1));
							}
							else
							{								setMSG(lblREPFM,"Data Type Not found : "+L_staDATTP[j],'E');								continue;
							}							L_strLINDS+=L_rstRSSET.getString(j+1)+"|";//Line description to display						}						if(!chbLGDTL.isSelected())//AVOID WRITNG TO LOG FILE if DETAILED LOG I NOT SELECTED							flgWRTLG=false;						setMSG(lblREPTO,L_strLINDS.substring(0,L_strLINDS.length()-1)+"  in "+L_strTBLNM,'N');						flgWRTLG=true;//ENABLE WRITNG TO LOG FILE						try						{
							L_pstINSERT.execute();//TRY TO INSERT
							cncREPTO.commit();
							L_pstTRNFL.execute();//SET TRNFL AT SOURCE							cncREPFM.commit();
							L_pstINSERT.clearParameters();							cncREPTO.clearWarnings();							L_pstTRNFL.clearParameters();							cncREPFM.clearWarnings();							L_intINSERT++;						}						catch(SQLException e)						{							if(e.getErrorCode() == -803)							{								L_pstUPDATE.addBatch();//IF DUPLICATE KEY ERROR IN INSERT, UPDATE
								L_pstTRNFL.execute();//SET TRNFL AT SOURCE								cncREPFM.commit();
								L_pstTRNFL.clearParameters();								cncREPFM.clearWarnings();								L_intUPDATE++;							}							else							{
								setMSG(lblREPTO,L_strINSERT+" : "+e,'E');								break;
							}						}					}					catch(Exception e)					{						setMSG(lblREPTO,L_strINSERT+"\n"+e,'E');						break;					}				}				L_rstRSSET.close();				if(L_intUPDATE>0)					L_pstUPDATE.executeBatch();				cncREPTO.commit();				cncREPTO.clearWarnings();			}			setMSG(lblREPTO,"Data transfer for "+L_strTBLNM+" completed ( Inserts : "+Integer.toString(L_intINSERT)+" Updates : "+Integer.toString(L_intUPDATE)+" )",'N');			//cncREPFM.createStatement().executeUpdate("Update "+L_strTBLNM+" set "+hstTRNFL.get(L_strTBLNM)+" ='1' where "+hstTRNFL.get(L_strTBLNM)+"='0'");		}		catch(Exception e)		{
			setMSG(lblREPTO,"Error while Transfering "+L_strLINDS+" in "+vtrTBLFM.elementAt(P_intINDEX)+e.toString(),'E');		}		finally		{
		
			L_strLINDS=null;////Declare at top to facilitate printing in final section of try
			L_strTBLNM=vtrTBLFM.elementAt(P_intINDEX).toString();//Name of the table			L_strINSERT=null;//INSERT QURY for destination					L_strUPDATE=null;
					L_strTRNFL=null;					L_strQUERY=null;
			L_rmtTEMP=null;
			L_staDATTP=null;
			L_obaTBLPK=null;
//			cncREPTO.clearWarnings();cncREPFM.clearWarnings();			try			{	//			L_pstINSERT.close();
	//			L_pstUPDATE.close();
	//			L_pstTRNFL.close();				L_rstRSSET.close();			}catch(Exception e)			{				setMSG(lblREPFM,e.toString(),'E');			}
			L_rstRSSET=null;//Declare at top to facilitate closing in final section of try	//		L_pstINSERT=null;
	//		L_pstUPDATE=null;
	//		L_pstTRNFL=null;
			System.gc();		}	}

	public void focusGained(FocusEvent L_FE)
	{
		try
		{
			Object L_objSOURC=L_FE.getSource();
		}catch(Exception e)
		{System.out.println("Error in foucsGained\n"+e);}
	}
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
			Object L_objSOURC=L_FE.getSource();
		}catch(Exception e)
		{System.out.println("Error in foucsLost\n"+e);}
	}
	
	
	public static void main(String[]a)
	{
		co_datrp ocl_datrp=new co_datrp();
	}
}