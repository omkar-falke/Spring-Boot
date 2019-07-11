import java.awt.Color;import java.awt.Component;import java.awt.Font;import java.awt.GridLayout;import java.awt.Container;import java.awt.Dimension;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.util.Vector;import java.util.StringTokenizer;import java.util.Hashtable;import java.util.Calendar;
import java.awt.event.*;import java.util.Enumeration;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.text.NumberFormat;
/**
 * This class is base class for all classes which create different forms in the system.<br>
 * Provides generalised methods for child classes<br>
 * Defines call to pre-defined methods<br>
 * Provides customised Layout Manager which takes care of changes in screen resolution.<br>
 * <p><b>BASIC FUNCTIONALITIES PROVIED FOR CHILD CLASS ARE AS FOLLOWS :</B><br>
 * <FONT color=purple><STRONG><BLOCKQUOTE style="MARGIN-RIGHT: 0px">  <P align=left><FONT face="LotusWP Icon">/&nbsp;&nbsp;&nbsp;</FONT>   </STRONG></FONT>Enable/Disable all components of screen as well as Button Pallet depending on selection in cmbOPTN<br><FONT face="LotusWP Icon"><FONT   color=purple><STRONG>/</STRONG></FONT>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Subsystem   selection route and user rights deifinition<BR><FONT   face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Enable/Disable   all components of screen as well as Button Pallet depending on selection in   cmbOPTN<BR><FONT face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT>&nbsp;</STRONG>&nbsp;&nbsp;</FONT>&nbsp;Call to exeSAVE()   in Press of btn SAVE if Entry form; Otherwise, call exePRINT() for   reports<BR><FONT face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Call to exeHLP()   and basic task to be done int his method.<BR><FONT   face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;System idle time   tracking<BR><FONT face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Generilised   methods for clearing screen (clrCOMP()) and setting state of Components on   screen(setENBL())<BR><FONT face="LotusWP Icon"><STRONG><FONT   color=purple>/</FONT></STRONG>&nbsp;&nbsp;&nbsp;</FONT>&nbsp;Update Useage at   back end when user comes out of the screen </P></BLOCKQUOTE> 
 * @version 2.0.0
 * @author AAP
 */
public class cl_pbase extends JPanel implements WindowListener,ActionListener,FocusListener,KeyListener,MouseListener,DocumentListener,SwingConstants,ItemListener, Runnable
{
	/** Global variable for Report  printing*/
	protected final String	M_strCPI10 ="CPI10",
							M_strCPI12 ="CPI12",
							M_strCPI17 ="CPI17",
							M_strNOCPI17 ="NOCPI17",
							M_strENH ="ENH",
							M_strNOENH ="NOENH",
							M_strBOLD ="BOLD",
							M_strNOBOLD ="NOBOLD",
							M_strEJT ="EJT",
							M_strPRNNM = "Printer",
							M_strSCRNM = "Screen",
							M_strFILNM = "File",
							M_strEMLNM = "E-Mail";
	String M_strPRGCD_fr,M_strSYSCD_fr;	/**Flags to remember operations carried out by the user. To update SA_PPUTR	 */
	private String strFLADD="'F'",strFLMOD="'F'",strFLDEL="'F'",strFLENQ="'F'",strFLRPT="'F'";	/** SybSystem code equivallent to current selection of SBS comboboxes at the top of the screen	 Ideally, should be equal to concatination of selected indices of subsystem combos.*/
	protected String M_strSBSCD;	/** Global variable for SQL Query String	 */
	protected String M_strSQLQRY;	/** Global variable for name of the field in string format on which help action is fired*/
	protected String M_strHLPFLD;	/** Global variable for ResutSet*/
	protected ResultSet M_rstRSSET;	/** Global variable for vertical gap to be left at the top of the screen while laying the components*/
	protected int M_intVSTRT = 10;	/** Global vector  to store components on the screen. Used to retrieve list of component present in clrCOMP() & setENBL()*/
	protected Vector M_vtrSCCOMP;	/** Global variable for ratio of currnet screen resolution to standard resolution i e  800 x 600	 */
	protected double M_dblWIDTH,M_dblHIGHT;	/** Global variable for gap between two columns*/
	protected static int M_intHRGAP=8;	/** Global variable for gap between two columns in std. resolution*/
	protected static int M_intHRGAP_st=8;	/** Global variable for gap between teo rows*/
	protected static int M_intVRGAP=5;	/** Global variable for gap between teo rows in std. resolution*/
	protected static int M_intVRGAP_st=5;	/** Global variable for Height of a row*/
	protected static int M_intROWHT;	/** Global variable for Width of a column*/
	protected static int M_intCOLWD;	/** Global variable for number of rows on the screen*/
	protected static int M_intROWCT=20;	/** Global variable for number of columns on the screen*/
	protected static int M_intCOLCT=4;	/** Global variable for refernce to source object in event handlers.	*/
	protected Object M_objSOURC;	/** Global flag for Error message is being displayed on screen*/
	protected boolean M_flgERROR;	/** Global variable for SubSystem wise User rights in current screen*/
	protected String[][]	M_staUSRRT;	/**	For Date in DB format "MM/dd/yyyy */
	protected SimpleDateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");	/**	For Timestamp in DB format "yyyy-MM-dd-HH.mm" */
	protected SimpleDateFormat M_fmtDBDTM=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	protected SimpleDateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");	/**	For Timestamp in Locale format "dd/MM/yyyy HH:mm" */
	protected SimpleDateFormat M_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");	/** For Calender in Locale format	Required for calculations /comparision of data/datetime values */
	protected Calendar M_calLOCAL=Calendar.getInstance();/** Array for column widths in help panel. Initialised in cl_hlp and set to null at the end. */
	private int[] inaHLPSZ;/**Scroll pane for help table */
	private JScrollPane jspHLPTB;
	/** Default constructor<br><b>Not be called any where</b> 	 */
	public cl_pbase()//DEFAULT CONSTRUCTOR, NEVER CALLED
	{
		M_dblWIDTH=cl_dat.M_dblWIDTH;
		M_dblHIGHT=cl_dat.M_dblHIGHT;
		this.setLayout(null);
		M_vtrSCCOMP=new Vector(20,10);
//		addLSTN();
	}
	/** Creates a screen with SubSystem ComboBoxes at the top ofthe screen
	 * @param P_intSBSCT number of comboboxes required. Usually, same as that of SubSystem levels available in the System 	 */
	public cl_pbase(int P_intSBSCT)
	{
		try
		{
			setSBSCD(P_intSBSCT);//GET COMBO-BOXES FOR SUBSYSTEM ACCESS
			M_dblWIDTH=cl_dat.M_dblWIDTH;//RATIO OF STANDARD to ACTUAL SCREEN WIDTH
			M_dblHIGHT=cl_dat.M_dblHIGHT;//RATIO OF STANDARD to ACTUAL SCREEN HEIGHT
			this.setLayout(null);
			addLSTN();//ADD LISTENERS TO COMPONENTS FROM FR_LOG
			M_vtrSCCOMP=new Vector(20,10);//VECTOR FOR SCREEN COMPONENTS
		}catch(Exception e)
		{setMSG(e,"in c-Pbase constructor");}
	}
	/** Adds listener to global components as Option combo and all buttons	 */
	void addLSTN()//METHODTO ADD LISTENERS
	{
	 	cl_dat.M_btnUNDO_pbst.addActionListener (this);
		cl_dat.M_btnSAVE_pbst.addActionListener (this);
		cl_dat.M_btnUNDO_pbst.addKeyListener (this);
		cl_dat.M_cmbOPTN_pbst.addKeyListener (this);
		cl_dat.M_cmbOPTN_pbst.addActionListener (this);
		cl_dat.M_btnSAVE_pbst.addKeyListener (this);
		cl_dat.M_cmbOPTN_pbst.addItemListener (this);
		cl_dat.M_btnEXIT_pbst.addActionListener(this);
		M_fmtDBDAT.setLenient(false);
		M_fmtDBDTM.setLenient(false);
		M_fmtLCDAT.setLenient(false);
		M_fmtLCDTM.setLenient(false);
		M_calLOCAL.setLenient(false);
		setDefaultGap();
	}
	/**Starts thread to update usage	 */
	public void run()
	{
		if(Thread.currentThread()==cl_dat.M_tmrUPUSG_st)
			updUSG();
	}
//TO BE REVIEWED********************************(KEPT API SRD, 27/08/03)
/*	protected void showEXMSG(Exception P_expEXPTN, String P_strMTHNM, String P_strSETFL){
		System.out.println(P_strMTHNM+" : "+P_expEXPTN.toString());
		setMSG(P_strMTHNM+" : "+P_expEXPTN.toString(),'E');
		if (P_strSETFL.equals("setLCLUPD"))
			cl_dat.M_flgLCUPD_pbst = false;
	}*/
	/**
	 * Creates and lays button pallet for a report screen
	 * 
	 * @param P_pnlPARNT Panel to which buttons are to 	  be added ('this' in all cases)
	 * @param P_intXAXIS X-co-ordinate for laying
	 * @param P_intYAXIS Y-co-ordinate for laying
	 * @param P_chrCOMTP Component type i. e. 'C' - Combobox, 'B' - Button Panel, ' ' - Both
	 * @param P_chrCMBTP Combobox Selection type i.e. 'F' - File, 'E' - E-Mail, ' ' - Both
	 */
	protected void setRPTBTN(JPanel P_pnlPARNT,int P_intXAXIS,int P_intYAXIS,char P_chrCOMTP,char P_chrCMBTP){              // Create Report BUTTON PANEL
		int L_intWIDTH = 90;
		int L_intHIGHT = 20;
		String L_strFILNM = "";
		String L_strEMLID = "";
		String L_strLBLNM = "";
		P_intXAXIS=P_intXAXIS*M_intCOLWD;
		switch(P_chrCOMTP){
			case 'C' :
				crtLBL(P_pnlPARNT,"Destination :",P_intXAXIS,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_cmbDESTN_pbst = crtCMB(P_pnlPARNT,M_strSCRNM,P_intXAXIS+L_intWIDTH,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_cmbDESTN_pbst.addItem(M_strPRNNM);
				switch(P_chrCMBTP){
					case 'F' :
						L_strFILNM = M_strFILNM;
						L_strLBLNM = "File Name :";
						break;
					case 'E' :
						L_strEMLID = M_strEMLNM;
						L_strLBLNM = "E-Mail ID :";
						break;
					default :
						L_strFILNM = M_strFILNM;
						L_strEMLID = M_strEMLNM;
						L_strLBLNM = "File/E-Mail :";
						break;
				}
				if(L_strFILNM.length() > 0)
					cl_dat.M_cmbDESTN_pbst.addItem(L_strFILNM);
				if(L_strEMLID.length() > 0)
					cl_dat.M_cmbDESTN_pbst.addItem(L_strEMLID);
				crtLBL(P_pnlPARNT,L_strLBLNM,P_intXAXIS,P_intYAXIS+(2*L_intHIGHT+5),L_intWIDTH,L_intHIGHT);
				cl_dat.M_txtDESTN_pbst = crtTXT(P_pnlPARNT,LEFT,P_intXAXIS+L_intWIDTH,P_intYAXIS+(2*L_intHIGHT+5),L_intWIDTH,L_intHIGHT);
				cl_dat.M_txtDESTN_pbst.addKeyListener(this);
				break;
			case 'B' :
				cl_dat.M_btnRUN_pbst = crtBTN(P_pnlPARNT,"Run",P_intXAXIS,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnEXT_pbst = crtBTN(P_pnlPARNT,"Exit",P_intXAXIS+L_intWIDTH,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnHLP_pbst = crtBTN(P_pnlPARNT,"Help",P_intXAXIS+2*L_intWIDTH,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnCLR_pbst = crtBTN(P_pnlPARNT,"Clear",P_intXAXIS+3*L_intWIDTH,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnPRN_pbst = crtBTN(P_pnlPARNT,"Print",P_intXAXIS+4*L_intWIDTH,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnRUN_pbst.addFocusListener(this);
				cl_dat.M_btnRUN_pbst.addKeyListener(this);
				cl_dat.M_btnRUN_pbst.setActionCommand("cl_dat.M_btnRUN_pbst");
				cl_dat.M_btnRUN_pbst.setToolTipText(" Executes a Record ");
//				cl_dat.M_btnEXT_pbst.addKeyListener(this);
				cl_dat.M_btnEXT_pbst.addFocusListener(this);
				cl_dat.M_btnEXT_pbst.setActionCommand("cl_dat.M_btnEXT_pbst");
//				cl_dat.M_btnEXT_pbst.setToolTipText(cl_dat.M_MSGEXT);
				cl_dat.M_btnHLP_pbst.addFocusListener(this);
				cl_dat.M_btnHLP_pbst.addKeyListener(this);
				cl_dat.M_btnHLP_pbst.setActionCommand("cl_dat.M_btnHELP_pbst");
				cl_dat.M_btnCLR_pbst.addFocusListener(this);
				cl_dat.M_btnCLR_pbst.addKeyListener(this);
				cl_dat.M_btnCLR_pbst.setActionCommand("cl_dat.M_btnCLR_pbst");
				cl_dat.M_btnCLR_pbst.setToolTipText(" Clears Component contents ");
				cl_dat.M_btnPRN_pbst.addFocusListener(this);
				cl_dat.M_btnPRN_pbst.addKeyListener(this);
				cl_dat.M_btnPRN_pbst.setActionCommand("cl_dat.M_btnPRN_pbst");
				cl_dat.M_btnPRN_pbst.setToolTipText(" Clears Component contents ");
				break;
			default :
				crtLBL(P_pnlPARNT,"Destination :",P_intXAXIS,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_cmbDESTN_pbst = crtCMB(P_pnlPARNT,M_strSCRNM,P_intXAXIS+L_intWIDTH,P_intYAXIS,L_intWIDTH,L_intHIGHT);
				cl_dat.M_cmbDESTN_pbst.addItem(M_strPRNNM);
				switch(P_chrCMBTP){
					case 'F' :
						L_strFILNM = M_strFILNM;
						L_strLBLNM = "File Name :";
						break;
					case 'E' :
						L_strEMLID = M_strEMLNM;
						L_strLBLNM = "E-Mail ID :";
						break;
					default :
						L_strFILNM = M_strFILNM;
						L_strEMLID = M_strEMLNM;
						L_strLBLNM = "File/E-Mail :";
						break;
				}
				if(L_strFILNM.length() > 0)
					cl_dat.M_cmbDESTN_pbst.addItem(L_strFILNM);
				if(L_strEMLID.length() > 0)
					cl_dat.M_cmbDESTN_pbst.addItem(L_strEMLID);
				crtLBL(P_pnlPARNT,L_strLBLNM,P_intXAXIS,P_intYAXIS+(2*L_intHIGHT+5),L_intWIDTH,L_intHIGHT);
				cl_dat.M_txtDESTN_pbst = crtTXT(P_pnlPARNT,LEFT,P_intXAXIS+L_intWIDTH,P_intYAXIS+(2*L_intHIGHT+5),L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnRUN_pbst = crtBTN(P_pnlPARNT,"Run",P_intXAXIS,P_intYAXIS+(3*L_intHIGHT+20),L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnEXT_pbst = crtBTN(P_pnlPARNT,"Exit",P_intXAXIS+L_intWIDTH,P_intYAXIS+(3*L_intHIGHT+20),L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnHLP_pbst = crtBTN(P_pnlPARNT,"Help",P_intXAXIS+2*L_intWIDTH,P_intYAXIS+(3*L_intHIGHT+20),L_intWIDTH,L_intHIGHT);
				cl_dat.M_btnRUN_pbst.addFocusListener(this);
				cl_dat.M_btnHLP_pbst.addFocusListener(this);
				cl_dat.M_btnEXT_pbst.addFocusListener(this);
				cl_dat.M_btnRUN_pbst.setActionCommand("cl_dat.M_btnRUN_pbst");
				cl_dat.M_btnEXT_pbst.setActionCommand("cl_dat.M_btnEXT_pbst");
				cl_dat.M_btnHLP_pbst.setActionCommand("cl_dat.M_btnHELP_pbst");
				cl_dat.M_txtDESTN_pbst.addKeyListener(this);
				cl_dat.M_btnRUN_pbst.setToolTipText(" Executes a Record ");
//				cl_dat.M_btnHLP_pbst.setToolTipText(cl_dat.M_MSGHLP);
//				cl_dat.M_btnEXT_pbst.setToolTipText(cl_dat.M_MSGEXT);
				break;
		}
	}
	// Returns new JButton
	//********************************************************
	protected JButton crtBTN(JPanel P_pnlPARNT,String P_strLABEL,int P_intXPOSN,int P_intYPOSN,int P_intWIDTH,int P_intHIGHT){
		P_intXPOSN=new Double(P_intXPOSN*M_dblWIDTH).intValue();
		P_intYPOSN=new Double(P_intYPOSN*M_dblHIGHT).intValue();
		P_intWIDTH=new Double(P_intWIDTH*M_dblWIDTH).intValue();
		P_intHIGHT=new Double(P_intHIGHT*M_dblHIGHT).intValue();
		JButton btnNEW = new JButton(P_strLABEL);
		btnNEW.setFont(new Font("Times new Roman",Font.BOLD,new Double(14.0*M_dblWIDTH).intValue()));
		btnNEW.setSize(P_intWIDTH,P_intHIGHT);
		btnNEW.setLocation(P_intXPOSN,P_intYPOSN);
		btnNEW.addActionListener(this);
        P_pnlPARNT.add(btnNEW);
		return btnNEW;
	}
	// Returns new JLabel
	//*******************************************************
	protected JLabel crtLBL(JPanel P_pnlPARNT,String P_strLABEL,int P_intXPOSN,int P_intYPOSN,int P_intWIDTH,int P_intHIGHT){
		P_intXPOSN=new Double(P_intXPOSN*M_dblWIDTH).intValue();
		P_intYPOSN=new Double(P_intYPOSN*M_dblHIGHT).intValue();
		P_intWIDTH=new Double(P_intWIDTH*M_dblWIDTH).intValue();
		P_intHIGHT=new Double(P_intHIGHT*M_dblHIGHT).intValue();
		JLabel L_lblNWLBL = new JLabel(P_strLABEL);
		L_lblNWLBL.setFont(new Font("Arial",Font.PLAIN,new Double(14.0*M_dblWIDTH).intValue()));
		L_lblNWLBL.setLocation(P_intXPOSN,P_intYPOSN);
		L_lblNWLBL.setSize(P_intWIDTH,P_intHIGHT);
	    P_pnlPARNT.add(L_lblNWLBL);
		return L_lblNWLBL;
	}
		// Returns new JTextField
	//************************************************
	protected  JTextField crtTXT(JPanel P_pnlPARNT,int LP_ALIGN,int P_intXPOSN,int P_intYPOSN,int P_intWIDTH,int P_intHIGHT){
		P_intXPOSN=new Double(P_intXPOSN*M_dblWIDTH).intValue();
		P_intYPOSN=new Double(P_intYPOSN*M_dblHIGHT).intValue();
		P_intWIDTH=new Double(P_intWIDTH*M_dblWIDTH).intValue();
		P_intHIGHT=new Double(P_intHIGHT*M_dblHIGHT).intValue();
    	JTextField L_txtNWTXT = new JTextField();
		L_txtNWTXT.setFont(new Font("Arial",Font.PLAIN,new Double(13.0*M_dblWIDTH).intValue()));
		L_txtNWTXT.setLocation(P_intXPOSN,P_intYPOSN);
		L_txtNWTXT.setSize(P_intWIDTH,P_intHIGHT);
		L_txtNWTXT.setHorizontalAlignment(LP_ALIGN);
		P_pnlPARNT.add(L_txtNWTXT);
		return L_txtNWTXT;
	}
	// Returns new JComboBox
    protected  JComboBox crtCMB(JPanel P_pnlPARNT,String LP_ITEM,int P_intXPOSN,int P_intYPOSN,int P_intWIDTH,int P_intHIGHT){
    	P_intXPOSN=new Double(P_intXPOSN*M_dblWIDTH).intValue();
		P_intYPOSN=new Double(P_intYPOSN*M_dblHIGHT).intValue();
		P_intWIDTH=new Double(P_intWIDTH*M_dblWIDTH).intValue();
		P_intHIGHT=new Double(P_intHIGHT*M_dblHIGHT).intValue();
		JComboBox cmbNEW = new JComboBox();
		cmbNEW.setFont(new Font("Times new Roman",Font.BOLD,new Double(14.0*M_dblWIDTH).intValue()));
		cmbNEW.addItem(LP_ITEM);
		cmbNEW.setLocation(P_intXPOSN,P_intYPOSN);
		cmbNEW.setSize(P_intWIDTH,P_intHIGHT);
		P_pnlPARNT.add(cmbNEW);
		return cmbNEW;
    }
	/**To Disable Exit Button. called internally on item selection change of cmbOPTN if selected index >0	 */
	private void setDSLEXT()
	{// set DISABLE EXIT
	    cl_dat.M_btnEXIT_pbst.setEnabled(false);
        cl_dat.M_btnSAVE_pbst.setEnabled(true);
        cl_dat.M_btnUNDO_pbst.setEnabled(true);
	}
	/**
	 * Display message to user at bottom area of application
	 * <p>If message type=normal, forrecolor = blue; 
	 * <p>If message type=error, forrecolor = red
	 * 
	 * @param P_strMESSG Message String to be displayed
	 * @param P_chrMSGTP Character for message type. 'N' : Normal, 'E' : Error
	 */
    protected  void setMSG(String P_strMESSG,char P_chrMSGTP)
	{
    	cl_dat.M_txtSTAT_pbst.setEnabled(true);
		if(P_chrMSGTP == 'N')
		{
			if(M_flgERROR)
			{
				M_flgERROR=false;
				return;
			}
			cl_dat.M_txtSTAT_pbst.setForeground(Color.blue);
		}
		else if(P_chrMSGTP == 'E')
		{
			cl_dat.M_txtSTAT_pbst.setForeground(Color.red);
			System.out.println(P_strMESSG);
			M_flgERROR=true;
		}
		cl_dat.M_txtSTAT_pbst.setText(" "+P_strMESSG);
		cl_dat.M_txtSTAT_pbst.paintImmediately(cl_dat.M_txtSTAT_pbst.getVisibleRect());
	}
	/**
	 * Display message to user at bottom area of application
	 * Over-ridden to display Error generated by JVM
	 * 
	 * <p>If message type=normal, forrecolor = blue; 
	 * <p>If message type=error, forrecolor = red
	 * 
	 * @param P_expEXPTNP Refernce to Exception reported by JVM
	 * @param P_strMTHNM Method name in which error is generated
	 * 
	 * <br><br><hr><B>Usage</B>
	  <br>setMSG(e,"My Method")
	  <br>If Error type = SQLError, Displays message : "Error in saving/retreiving data"
	  <br>Displays "Error in My Method "+details of error on dos screen
	 */
	protected  void setMSG(Exception P_expEXPTNP,String P_strMTHNM){
		if(P_expEXPTNP.getClass().toString().equals("class java.sql.SQLException"))
		{
			setMSG("Error in saving/retreiving data",'E');
			M_flgERROR=true;
		}
		System.out.println("Error in "+P_strMTHNM+" :"+P_expEXPTNP);
	}
	/**Copy refernace of source to M_objSOURC	 */
	public void focusLost(FocusEvent L_FE)
	{		M_objSOURC=L_FE.getSource();	}
	
	public void focusGained(FocusEvent L_FE)
	{
		M_objSOURC=L_FE.getSource();
		if(M_objSOURC.equals(cl_dat.M_tblHELP_pbst))
                cl_dat.M_txtHLPPOS_pbst.requestFocus();
    }

	/**
	 * <b>TASKS :</B><br>
	 * &nbsp&nbsp&nbsp Reset M_tmrIDLTM_pbst (System idle time timer)<br>
	 * &nbsp&nbsp&nbsp Source=
	 * {@link cl_dat#M_cmbOPTN_pbst} : Disable SBS Combos; Clear contents on screen; Set Component states(Enable/Disable) as per option selected.<br>
	 * &nbsp&nbsp&nbsp Source=
	 * {@link cl_dat#M_btnEXIT_pbst} : Remove Action/key listners to Button Pallet; Set SubSystem Combos to null;<br>
	 * &nbsp&nbsp&nbsp Source=
	 * {@link cl_dat#M_btnHLPOK_pbst}||{@link cl_dat#M_lstHELP_pbst}||{@link cl_dat#M_txtHLPPOS_pbst}||LM_HLLPTBL : call exeHPLOK()<br>
	 * &nbsp&nbsp&nbsp Source=
	 * {@link cl_dat#M_btnSAVE_pbst} : call exeSAVE()<br>
	 * &nbsp&nbsp&nbsp Source=
	 * {@link cl_dat#M_btnUNDO_pbst} : call clrCOMP()
	 */
	public  void actionPerformed(ActionEvent L_AE)
	{
		cl_dat.M_tmrIDLTM_pbst.restart();//RESETTING SYSTEM IDLE TIME COUNTER
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			M_objSOURC=L_AE.getSource();
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				cl_dat.M_cmbSBSL1_pbst.setEnabled(false);//DISABLEING SBS COMBOS
				if(cl_dat.M_cmbSBSL2_pbst!=null)
					cl_dat.M_cmbSBSL2_pbst.setEnabled(false);
				if(cl_dat.M_cmbSBSL3_pbst!=null)
					cl_dat.M_cmbSBSL3_pbst.setEnabled(false);
				cl_dat.M_pnlSBSCD_pbst.setEnabled(false);//SBS COMBOS DISABLED
				cl_dat.M_txtUSER_pbst.setText(cl_dat.M_strUSRCD_pbst);
				cl_dat.M_txtDATE_pbst.setText(cl_dat.M_strLOGDT_pbst);
				String L_STRTMP=(String)cl_dat.M_cmbOPTN_pbst.getSelectedItem();
				if(L_STRTMP.equals(cl_dat.M_OPSEL_pbst)||L_STRTMP.equals(cl_dat.M_OPRSL_pbst))//CODE FOR ITEM "Select an Option"
				{
					clrCOMP();
					setENBL(false);
					cl_dat.M_cmbSBSL1_pbst.setEnabled(true);//ENABLING SBS COMBOS
					if(cl_dat.M_cmbSBSL2_pbst!=null)
						cl_dat.M_cmbSBSL2_pbst.setEnabled(true);
					if(cl_dat.M_cmbSBSL3_pbst!=null)
						cl_dat.M_cmbSBSL3_pbst.setEnabled(true);
					cl_dat.M_pnlSBSCD_pbst.setEnabled(true);
				}
				else if((L_STRTMP).equals(cl_dat.M_OPADD_pbst))
				{//Set Addition flag
					clrCOMP();
					setENBL(true);
					strFLADD="'T'";
				}
				else if((L_STRTMP).equals(cl_dat.M_OPDEL_pbst))
				{//set Deletion flag
				    clrCOMP();
					setENBL(false);
					strFLMOD="'T'";
				}
				else if((L_STRTMP).equals(cl_dat.M_OPMOD_pbst))
				{
				    clrCOMP();
					setENBL(true);
					strFLDEL="'T'";
				}
				else if((L_STRTMP).equals(cl_dat.M_OPENQ_pbst))
				{
				    setENBL(false);
					strFLENQ="'T'";
				}
				else
				{
					setENBL(true);
					strFLRPT="'T'";
				}
			}
			if(M_objSOURC==cl_dat.M_btnEXIT_pbst)
			{
				cl_dat.M_tmrUPUSG_st=new Thread(this);//Start thread for usage updation
				cl_dat.M_tmrUPUSG_st.start();
				cl_dat.M_cmbSBSL1_pbst=null;cl_dat.M_cmbSBSL2_pbst=null;cl_dat.M_cmbSBSL3_pbst=null;//Distroy subsystem combos
				cl_dat.M_btnUNDO_pbst.removeActionListener (this);//Unregister all Listeners
				cl_dat.M_cmbOPTN_pbst.removeActionListener (this);
				cl_dat.M_btnSAVE_pbst.removeActionListener (this);
				cl_dat.M_btnUNDO_pbst.removeKeyListener (this);
				cl_dat.M_cmbOPTN_pbst.removeKeyListener (this);
				cl_dat.M_btnSAVE_pbst.removeKeyListener (this);
				cl_dat.M_cmbOPTN_pbst.removeKeyListener (this);
				cl_dat.M_btnEXIT_pbst.removeActionListener (this);
			}
			else if (	M_objSOURC==cl_dat.M_btnHLPOK_pbst //CALL exeHLPOK
					 || M_objSOURC==cl_dat.M_lstHELP_pbst
					 || M_objSOURC==cl_dat.M_txtHLPPOS_pbst
					 || M_objSOURC==cl_dat.M_tblHELP_pbst
					 || L_AE.getActionCommand().equals("help"))
				exeHLPOK();
			else if(M_objSOURC==cl_dat.M_btnSAVE_pbst)
			{
				String L_strTEMP=cl_dat.M_btnSAVE_pbst.getText();
				if(L_strTEMP.equalsIgnoreCase("Save")||L_strTEMP.equalsIgnoreCase("Change")||L_strTEMP.equalsIgnoreCase("Delete")||L_strTEMP.equalsIgnoreCase("Authorize"))
				{
					exeSAVE();
					
				}
				else
					exePRINT();
			}
			else if(M_objSOURC==cl_dat.M_btnUNDO_pbst)
				clrCOMP();
		}catch(Exception e)
		{			setMSG(e,"cl_pbase.actionPreformed : ");		}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	/**Sets Subsystem rights
	 * <br>Checks if next Subsystem level exists, If yes, poulates corresponding combo and transfers focus<br>Else, populates cmbOPTN and transfers focus to it
	 */
	public void itemStateChanged(ItemEvent L_IE)
	{
		M_objSOURC=L_IE.getSource();
		if(M_objSOURC==cl_dat.M_cmbSBSL1_pbst)//SBS LEVEL -1 SELECTION CHANGED
		{
			if(cl_dat.M_cmbSBSL2_pbst!=null)//IF LEVEL - 2 EXISTS
			{//POPULATING SBS LEVE - 2 COMBO
				cl_dat.M_cmbSBSL2_pbst.hidePopup();
				cl_dat.M_cmbSBSL2_pbst.removeAllItems();
				cl_dat.M_cmbSBSL2_pbst.addItem("Select");
				for(int i=0;i<M_staUSRRT.length&&M_staUSRRT[i][0]!=null;i++)
				{
					if(M_staUSRRT[i][1].equals(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString()))
					{
						boolean flag=true;
						for(int z=0;z<cl_dat.M_cmbSBSL2_pbst.getItemCount();z++)
							if(M_staUSRRT[i][2].equals(cl_dat.M_cmbSBSL2_pbst.getItemAt(z).toString()))
							{
								flag=false;
								break;
							}
						if(flag)
							cl_dat.M_cmbSBSL2_pbst.addItem(M_staUSRRT[i][2]);
					}
				}//ALL ITEMS ADDED IN CMB2
				if(cl_dat.M_cmbSBSL2_pbst.getItemCount()==2)
					cl_dat.M_cmbSBSL2_pbst.setSelectedIndex(1);
				else
				{
					cl_dat.M_cmbSBSL2_pbst.requestFocus();
					cl_dat.M_cmbSBSL2_pbst.showPopup();
				}
			}
			else//IF LEVEL - 2 DOESN'T EXIST
			{
				for(int i=0;i<M_staUSRRT.length&&M_staUSRRT[i][0]!=null;i++)
				{
					cl_dat.M_cmbOPTN_pbst.removeAllItems();
					if(M_staUSRRT[i][1].equals(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString()))
					{
						M_strSBSCD=M_staUSRRT[i][0].substring(2);
						if(cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Run")
							||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Send")
							||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Display")
							||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Print"))
						{
							if(cl_dat.M_cmbSBSL1_pbst.getSelectedIndex()!=0)
								setOPTN(-1);
						}
						else
						{
							cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPSEL_pbst);
							setOPTN(i);
						}
						break;
					}
				}
			}
		}
		else if(M_objSOURC==cl_dat.M_cmbSBSL2_pbst)//SBS LEVEL - 2 SELECTION CHANGED
		{
			if(cl_dat.M_cmbSBSL3_pbst!=null)//IF LEVEL - 3 EXISTS
			{//POPULATING LEVEL - 3 COMBO
				cl_dat.M_cmbSBSL3_pbst.hidePopup();
				cl_dat.M_cmbSBSL3_pbst.removeAllItems();
				cl_dat.M_cmbSBSL3_pbst.addItem("Select");
				for(int i=0;i<M_staUSRRT.length&&M_staUSRRT[i][0]!=null;i++)
				{
					if(M_staUSRRT[i][1].equals(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString())&&M_staUSRRT[i][2].equals(cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString()))
					{
						boolean flag=true;
						for(int z=0;z<cl_dat.M_cmbSBSL3_pbst.getItemCount();z++)
							if(M_staUSRRT[i][3].equals(cl_dat.M_cmbSBSL3_pbst.getItemAt(z).toString()))
							{
								flag=false;
								break;
							}
						if(flag)
							cl_dat.M_cmbSBSL3_pbst.addItem(M_staUSRRT[i][3]);
					}
				}
				if(cl_dat.M_cmbSBSL3_pbst.getItemCount()==2)
				{
					cl_dat.M_cmbSBSL3_pbst.setSelectedIndex(1);
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					cl_dat.M_cmbOPTN_pbst.showPopup();
				}
				else if(cl_dat.M_cmbSBSL3_pbst.getItemCount()>2)
				{
					cl_dat.M_cmbSBSL3_pbst.requestFocus();
					cl_dat.M_cmbSBSL3_pbst.showPopup();
				}
			}
			else//LEVEL - 3 DOESN'T EXIST
			{//POPULATING OPTION COMBO
				for(int i=0;i<M_staUSRRT.length&&M_staUSRRT[i][0]!=null;i++)
				{
					cl_dat.M_cmbOPTN_pbst.removeAllItems();
					if(M_staUSRRT[i][1].equals(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString())&&M_staUSRRT[i][2].equals(cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString()))
					{
						M_strSBSCD=M_staUSRRT[i][0].substring(2);
						if(cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Run")
							||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Send")
							||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Display")
							||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Print"))
						{
							if(cl_dat.M_cmbSBSL2_pbst.getSelectedIndex()!=0)
								setOPTN(-1);
						}
						else
						{
							cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPSEL_pbst);
							setOPTN(i);
						}
						break;
					}
				}
			}
		}
		else if(M_objSOURC==cl_dat.M_cmbSBSL3_pbst)//LEVEL - 3 SELECTION CHANGED
		{//POPULATING OPTION COMBO
			for(int i=0;i<M_staUSRRT.length&&M_staUSRRT[i][0]!=null;i++)
			{
					cl_dat.M_cmbOPTN_pbst.removeAllItems();
				if(M_staUSRRT[i][1].equals(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString())&&M_staUSRRT[i][2].equals(cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString())&&M_staUSRRT[i][3].equals(cl_dat.M_cmbSBSL3_pbst.getSelectedItem().toString()))
				{
					M_strSBSCD=M_staUSRRT[i][0].substring(2);
					if(cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Run")
						||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Send")
						||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Display")
						||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Print"))
					{
						if(cl_dat.M_cmbSBSL3_pbst.getSelectedIndex()!=0)
							setOPTN(-1);
					}
					else
					{
						cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPSEL_pbst);
						setOPTN(i);
					}
					break;
				}
			}
			if(cl_dat.M_cmbSBSL3_pbst.getItemCount()>2)
			{
				cl_dat.M_cmbSBSL3_pbst.requestFocus();
				cl_dat.M_cmbSBSL3_pbst.showPopup();
			}
			else
			{
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				cl_dat.M_cmbOPTN_pbst.showPopup();
			}
		}
	}
	/**METHOD TO POPULATE OPTION COMBO. (FOR SBS CODE RELATED ONLY)
	 * if Aurgument is -1, considers current screen as report & puts related items.<br> Else, takes rights from i'th element from SBSRT array <br>called internally on selection change of Subsystem ComboBoxes*/
	private void setOPTN(int i)
	{
		if(i==-1)
		{
			cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPRSL_pbst);
			cl_dat.M_btnSAVE_pbst.setText("Run");
			cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPPRN_pbst);
			cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPEML_pbst);
			cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPFIL_pbst);
			cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPSCN_pbst);
		}
		else
		{
			if(M_staUSRRT[i][4].equalsIgnoreCase("Y"))
				cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPADD_pbst);
			if(M_staUSRRT[i][5].equalsIgnoreCase("Y"))
				cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPMOD_pbst);
			if(M_staUSRRT[i][6].equalsIgnoreCase("Y"))
				cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPDEL_pbst);
			if(M_staUSRRT[i][7].equalsIgnoreCase("Y"))
				cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPENQ_pbst);
			//ADDED ON 02/04/2004 BY AAP FOR AUTHORISATION OPTION
			if(M_staUSRRT[i][8].equalsIgnoreCase("Y"))
				cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPAUT_pbst);
		}
		cl_dat.M_cmbOPTN_pbst.setEnabled(true);
		cl_dat.M_cmbOPTN_pbst.requestFocus();
		cl_dat.M_cmbOPTN_pbst.showPopup();
	}

	
	/**
	 * Creates help panel with customised column width.
	 * 
	 * Creates help panel with customised column width.
	 * 
	 * <p>Simply stores refernce of the int array to inaHLPSZ and calls default method
	 * 
	 * @param P_objSQLQRY Object of SQL query in string or Help datain the form of Object Array or Hashtable of Object Array or vector of Object Array
	 * @param P_intCLSCH Column index on which smart search is to be carried out
	 * @param P_intCLRTN Column index whose respective value is to be returned when user presses enteror clicks on button "OK"
	 * @param P_staHEADR String Array for Column headers
	 * @param P_intNUMCL number of columns
	 * @param P_strHLPOS Position of the help window on screen
	 * @param P_inaHLPSZ Int array for width of the columns.
	 * 
	 * <br><hr><b>USAGE</B><br>
	 * cl_hlp("Select C1,C2,C3,C4 from TBL1",1,2,new String[]{"COL1","COL2","COL3","COL4"},3,"CT",new int[]{10,20,30,40})<br>
	 * &nbsp&nbsp&nbsp Displays help screen with three columns at center of screen<br>
	 * &nbsp&nbsp&nbsp Column headers 'COL1','COL2','COL3','COL4' with Column widths 10,20,30,40 resp.<br>
	 * &nbsp&nbsp&nbsp Incremental search on COL1, 
	 * &nbsp&nbsp&nbsp Copies COL2 value to cl_dat.M_strRTNVL_pbst on exeHLPOK
	 * &nbsp&nbsp&nbsp Copies COL1|COL2|COL3 to cl_dat.M_strHLPSTR_pbst on exeHLPOK<hr>
	 * <p>Restrications :<br>Length of P_staHEADR should be equals to length of P_intNUMCL & length of <br>
	 * Value of P_strHLPOS should be one of <br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "TR" : Top Right<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "TL" : Top Left<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "BR" : Bottom Right<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "BL" : Bottom Left<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "CT" : Center<br>
	 */
	protected  void cl_hlp(String P_strSQLQRY,int P_intCLSCH,int P_intCLRTN,String[] P_staHEADR,int P_intNUMCL,String P_strHLPOS,int[] P_inaHLPSZ)	{		inaHLPSZ=P_inaHLPSZ;
		cl_hlp(P_strSQLQRY,P_intCLSCH,P_intCLRTN,P_staHEADR,P_intNUMCL,P_strHLPOS);	}

	/**
	 * Creates help panel with Default column width (i.e. equal col width)
	 * 
	 * <p><b>Provision of passing Help data in Vector and two dimensional Object Array is provided but kept commented API SRD.<br>
	 * At present, Help data can be passed thro' a hashtable containing Object Array of each a row in help table as element of the hashtable </B>
	 * 
	 * @param P_objSQLQRY Object of SQL query in string or Help datain the form of Object Array or Hashtable of Object Array or vector of Object Array
	 * @param P_intCLSCH Column index on which smart search is to be carried out
	 * @param P_intCLRTN Column index whose respective value is to be returned when user presses enteror clicks on button "OK"
	 * @param P_staHEADR String Array for Column headers
	 * @param P_intNUMCL number of columns
	 * @param P_strHLPOS Position of the help window on screen
	 * 
	 * <br><hr><b>USAGE</B><br>
	 * cl_hlp("Select C1,C2,C3,C4 from TBL1",1,2,new String[]{"COL1","COL2","COL3","COL4"},3,"CT")<br>
	 * &nbsp&nbsp&nbsp Displays help screen with three columns at center of screen<br>
	 * &nbsp&nbsp&nbsp Column headers 'COL1','COL2','COL3','COL4' with equal Column widths resp.<br>
	 * &nbsp&nbsp&nbsp Incremental search on COL1, 
	 * &nbsp&nbsp&nbsp Copies COL2 value to cl_dat.M_strRTNVL_pbst on exeHLPOK
	 * &nbsp&nbsp&nbsp Copies COL1|COL2|COL3 to cl_dat.M_strHLPSTR_pbst on exeHLPOK<hr>
	 * 
	 * cl_hlp(hst,1,2,new String[]{"COL1","COL2","COL3","COL4"},3,"CT")<br>
	 * Where hst is hashtable defines as : 
	 * hst=new Hashtable();
	 * hst.put(KEY1,ObjectArray1);hst.put(KEY2,ObjectArray2); and so on..<br>
	 * &nbsp&nbsp&nbsp ObjectArray1,ObjectArray2 .. should be of length = length of String array for titles<br>
	 * &nbsp&nbsp&nbsp Displays help screen with columns equal to ObjectArray.length at center of screen<br>
	 * &nbsp&nbsp&nbsp Table will contain values passed into each ObjestArray in hst as seperate rows<br>
	 * &nbsp&nbsp&nbsp Column headers 'COL1','COL2','COL3','COL4' with equal Column widths resp.<br>
	 * &nbsp&nbsp&nbsp Incremental search on COL1, 
	 * &nbsp&nbsp&nbsp Copies COL2 value to cl_dat.M_strRTNVL_pbst on exeHLPOK
	 * &nbsp&nbsp&nbsp Copies COL1|COL2|COL3 to cl_dat.M_strHLPSTR_pbst on exeHLPOK<hr>
	 * <p>Restrications :<br>
	 * Value of P_strHLPOS should be one of <br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "TR" : Top Right<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "TL" : Top Left<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "BR" : Bottom Right<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "BL" : Bottom Left<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "CT" : Center<br>
	 * Value of filter criteria entered by user (Like predicate in caseof query) will not appear when data is passed
	 */
	protected  void cl_hlp(Object P_objSQLQRY,int P_intCLSCH,int P_intCLRTN,String[] P_staHEADR,int P_intNUMCL,String P_strHLPOS)
	{
		try
		{
			System.out.println("ok1");
			String L_strLIKEVL=null;//String to store value of like predicate 17/04/2004, AAP
			String P_strSQLQRY=null;
			if(P_strSQLQRY instanceof String)
			{
				P_strSQLQRY=(String )P_objSQLQRY;
				if(P_strSQLQRY.toLowerCase().indexOf("like")!=-1)
				{//Code to rerieve value of like predicate, if exists 17/04/2004, AAP
					L_strLIKEVL=P_strSQLQRY.substring(P_strSQLQRY.toLowerCase().indexOf("like")+4);
					L_strLIKEVL=L_strLIKEVL.replace('\'',' ').trim();
					L_strLIKEVL=new StringTokenizer(L_strLIKEVL,"%").nextToken();
				}
			}
			cl_dat.M_SPLSCH_pbst = P_intCLSCH;
			cl_dat.M_intSPLRTN_pbst = P_intCLRTN;
			cl_dat.M_flgCHKTB_pbst = false;
			cl_dat.M_pnlHELP_pbst = new JPanel();
			cl_dat.M_lblHLPHDG_pbst = new JLabel();
			cl_dat.M_pnlHELP_pbst.setLayout(new GridLayout(2,0,0,-60));
			cl_dat.M_pnlHELP_pbst.setSize(700,500);
			cl_dat.M_pnlPOSBTN_pbst.setLayout(null);
			cl_dat.M_pnlPOSBTN_pbst.removeAll();
			cl_dat.M_pnlDYNTBL_pbst = crtHLPPNL(P_objSQLQRY,P_intNUMCL,P_staHEADR,0,0,550,145);
			if(cl_dat.M_pnlDYNTBL_pbst==null)
				return;
			cl_dat.M_pnlHELP_pbst.add(cl_dat.M_pnlDYNTBL_pbst);
			cl_dat.M_pnlDYNTBL_pbst.setEnabled(false);
			cl_dat.M_lblHELP_pbst = new JLabel("Search :");
			cl_dat.M_lblHELP_pbst.setBounds(32,80,70,20);
			cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_lblHELP_pbst);
			cl_dat.M_txtHLPPOS_pbst = new JTextField();
//			cl_dat.M_txtHLPPOS_pbst.addKeyListner(this);
			cl_dat.M_txtHLPPOS_pbst.setBounds(100,80,250,25);
			cl_dat.M_txtHLPPOS_pbst.getDocument().addDocumentListener(this);
			cl_dat.M_txtHLPPOS_pbst.addActionListener(this);
			cl_dat.M_txtHLPPOS_pbst.addKeyListener(this);
			cl_dat.M_txtHLPPOS_pbst.addFocusListener(this);
			cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_txtHLPPOS_pbst);
			cl_dat.M_btnHLPOK_pbst = new JButton("Ok");
	  		cl_dat.M_btnHLPOK_pbst.setBounds(425,80,60,25);
			cl_dat.M_btnHLPOK_pbst.addActionListener(this);
			cl_dat.M_btnHLPOK_pbst.addMouseListener(this);
			cl_dat.M_btnHLPOK_pbst.setActionCommand("help");
			cl_dat.M_btnHLPOK_pbst.addKeyListener(this);
			cl_dat.M_pnlPOSBTN_pbst.add(cl_dat.M_btnHLPOK_pbst);
			cl_dat.M_pnlHELP_pbst.add(cl_dat.M_pnlPOSBTN_pbst);
			cl_dat.M_pnlHELP_pbst.setVisible(true);
			cl_dat.M_wndHLP_pbst = new JDialog();
			cl_dat.M_wndHLP_pbst.getContentPane().add(cl_dat.M_pnlHELP_pbst);
			if(P_strHLPOS.equals("TL")){
				cl_dat.M_intXPOS_pbst = 10;
				cl_dat.M_intYPOS_pbst = 35;
			}
			else if(P_strHLPOS.equals("TR")){
				cl_dat.M_intXPOS_pbst = 150;
				cl_dat.M_intYPOS_pbst = 35;
			}
			else if(P_strHLPOS.equals("BL")){
				cl_dat.M_intXPOS_pbst = 10;
				cl_dat.M_intYPOS_pbst = 300;
			}
			else if(P_strHLPOS.equals("BR")){
				cl_dat.M_intXPOS_pbst = 150;
				cl_dat.M_intYPOS_pbst = 300;
			}
			else if(P_strHLPOS.equals("CT")){
				cl_dat.M_intXPOS_pbst = 100;
				cl_dat.M_intYPOS_pbst = 160;
			}
			cl_dat.M_wndHLP_pbst.setBounds(cl_dat.M_intXPOS_pbst,cl_dat.M_intYPOS_pbst,600,230);
			cl_dat.M_wndHLP_pbst.toFront();
			cl_dat.M_wndHLP_pbst.show();
			cl_dat.M_wndPOS_pbst = new JWindow(cl_dat.M_wndHLP_pbst);
			cl_dat.M_wndHLP_pbst.setTitle(padSTRING('L',"Help Screen",70));
			cl_dat.M_txtHLPPOS_pbst.requestFocus();
			if(L_strLIKEVL!=null)//Display like preodcate value if exists 17/04/2004, AAP
				cl_dat.M_txtHLPPOS_pbst.setText(L_strLIKEVL);
			inaHLPSZ=null;
		}catch(Exception e)
		{setMSG(e,"cl_hlp");
		 setMSG("No. data Found ..",'E');
		 inaHLPSZ=null;}
	}

/**
 * Creates a panel for help
 * 
 * <b>Called internally from cl_hlp</b>
 * <br>dimenstion and location of window are determined from aurguments in cl_hlp()
 * <br>Detects the type of first aurgument, converts it into Object Array or fies query to DB and constructs the help table
 * 
 * @param P_objSQLQRY Object of SQL query in string or Help datain the form of Object Array or Hashtable of Object Array or vector of Object Array
 * @param P_intNUMCL number of columns
 * @param LP_COLHD String Array for Column headers
 * @param P_intXPOSN X-co-ordinatte for placing the help window
 * @param P_intYPOSN Y-co-ordinatte for placing the help window
 * @param L_intWIDTH Width of Help window
 * @param L_intHIGHT Height of Help window
 */
	protected  JPanel crtHLPPNL(Object P_objSQLQRY,int P_intNUMCL,String[] LP_COLHD,int P_intXPOSN,int P_intYPOSN,int L_intWIDTH,int L_intHIGHT){
		JPanel pnlTAB1 = new JPanel();
		try{
			cl_dat.M_hstHELP_pbst = new Hashtable();
			cl_dat.M_intCOUNT_pbst = 0;
			String L_STRHLP = "";
			String L_COLVAL = "",LM_SPLTP = "";
			float L_FLTVL;			cl_dat.M_rstHELP_pbst=null;
			String P_strSQLQRY="";
			Object[][] objDATA=null;
			if(P_objSQLQRY instanceof String)
			{//If First aurgument is string, fire query to database and create help table
				P_strSQLQRY=(String)P_objSQLQRY;
				cl_dat.M_rstHELP_pbst = cl_dat.exeSQLQRY(P_strSQLQRY);			
				cl_dat.M_rmdRSMHLP_pbst = cl_dat.M_rstHELP_pbst.getMetaData();			
				Vector vtrDATA=new Vector(10,5);			
				if(cl_dat.M_rstHELP_pbst !=null){
					while (cl_dat.M_rstHELP_pbst.next()){
						cl_dat.M_staTBDAT_pbst = new String[P_intNUMCL];
						for(int j = 0;j < P_intNUMCL;j++)
						{
							LM_SPLTP = cl_dat.M_rmdRSMHLP_pbst.getColumnTypeName(j+1);						if(!LM_SPLTP.equals("DATE")){
								if(LM_SPLTP.equals("Float")){
									L_FLTVL = cl_dat.M_rstHELP_pbst.getFloat(j+1);
									L_COLVAL = String.valueOf(L_FLTVL);
								}else
									L_COLVAL = cl_dat.M_rstHELP_pbst.getString(j+1);
								if(LM_SPLTP.equals("TIMESTAMP")){
									if(L_COLVAL != null)
										L_COLVAL = M_fmtLCDTM.format(cl_dat.M_rstHELP_pbst.getTimestamp(j+1));
								}
							}
							else if(LM_SPLTP.equals("DATE")){
								java.util.Date LM_STRDT = (cl_dat.M_rstHELP_pbst.getDate(j+1));
								if(LM_STRDT != null)
									L_COLVAL = M_fmtLCDAT.format(cl_dat.M_rstHELP_pbst.getDate(j+1));
								else
									L_COLVAL="";
							}
							if(L_COLVAL == null)
								L_COLVAL = "";
							cl_dat.M_staTBDAT_pbst[j] = nvlSTRVL(L_COLVAL,"").trim().replace('|',' ');
							L_STRHLP += nvlSTRVL(L_COLVAL,"").trim().replace('|',' ') + "|";
						}
						cl_dat.M_hstHELP_pbst.put(String.valueOf(cl_dat.M_intCOUNT_pbst),L_STRHLP);
						cl_dat.M_intCOUNT_pbst++;
						L_STRHLP = "";
						vtrDATA.addElement(cl_dat.M_staTBDAT_pbst);
					}
				}
				else			
				{
					cl_dat.M_flgHELPFL_pbst = false;
					cl_dat.M_strHLPSTR_pbst = "";
				}
				objDATA=new Object[vtrDATA.size()][cl_dat.M_staTBDAT_pbst.length];
				for(int i=0;i<vtrDATA.size();i++)
					objDATA[i]=(Object[])vtrDATA.elementAt(i);
				if(cl_dat.M_rstHELP_pbst != null)
					cl_dat.M_rstHELP_pbst.close();			

			}
		//CODE TO USE DATA IN OBJECT ARRAY
		//ADDED BY AAP 04/05/2004, TESTED & KEPT COMMENTED API SRD
/*			else if(P_objSQLQRY instanceof Object[])
			{//If First aurgument is Object[][], use that array and create help table
				objDATA=(Object[][])P_objSQLQRY;
				String L_strTEMP="";
				for(int i=0;i<objDATA.length;i++)
				{
					for(int j=0;j<objDATA[i].length;j++)
						L_strTEMP+=objDATA[i][j].toString()+"|";
					cl_dat.M_hstHELP_pbst.put(Integer.toString(i),L_strTEMP);
					L_strTEMP="";
				}
			}
			
*/			
		//CODE TO CONVERT DATA IN HASHTABLE TO OBJECT ARRAY
		//ADDED BY AAP 04/05/2004, 
			else if(P_objSQLQRY instanceof Hashtable)
			{//If First aurgument is Hashtable, use elements in hashtable as Object arrays, construct two dimensional Object array and create help table
				objDATA=new Object[((Hashtable)P_objSQLQRY).size()][];
				Enumeration L_enmTEMP=((Hashtable)P_objSQLQRY).elements();
				int i=0;
				Object L_objTEMP=null;
				while(L_enmTEMP.hasMoreElements())
				{
					L_objTEMP=L_enmTEMP.nextElement();
					if(! (L_objTEMP instanceof Object[]))
						throw new Exception ("Illegal Hashtable format ");
					objDATA[i++]=(Object[])L_objTEMP;
				}
				String L_strTEMP="";
				for(i=0;i<objDATA.length;i++)
				{
					for(int j=0;j<objDATA[i].length;j++)
						L_strTEMP+=objDATA[i][j].toString()+"|";
					cl_dat.M_hstHELP_pbst.put(Integer.toString(i),L_strTEMP);
					L_strTEMP="";
				}
			}
		//CODE TO CONVERT DATA IN VECTOR TO OBJECT ARRAY
		//ADDED BY AAP 04/05/2004, TESTED & KEPT COMMENTED API SRD
/*			else if(P_objSQLQRY instanceof Vector)
			{//If First aurgument is Vector, use elements in vector as Object arrays, construct two dimensional Object array and create help table
				objDATA=new Object[((Vector)P_objSQLQRY).size()][];
				for(int i=0;i<((Vector)P_objSQLQRY).size();i++)
				{
					if(! ( ((Vector)P_objSQLQRY).elementAt(i) instanceof Object[]))
						throw new Exception ("Illegal Vector format ");
					objDATA[i]=(Object[])((Vector)P_objSQLQRY).elementAt(i);
				}
				String L_strTEMP="";
				for(int i=0;i<objDATA.length;i++)
				{
					for(int j=0;j<objDATA[i].length;j++)
						L_strTEMP+=objDATA[i][j].toString()+"|";
					cl_dat.M_hstHELP_pbst.put(Integer.toString(i),L_strTEMP);
					L_strTEMP="";
				}
			}
*/			
			if(objDATA.length==0){
				setMSG("No Data Found ..",'E');
				return null;	
			}
			 pnlTAB1.removeAll();
			 cl_dat.M_tblHELP_pbst = new JTable(objDATA,LP_COLHD);
			 int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			 int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			 jspHLPTB = new JScrollPane(cl_dat.M_tblHELP_pbst,v1,h1);			 
			 jspHLPTB.setPreferredSize(new Dimension(L_intWIDTH-25,L_intHIGHT-25));
			 jspHLPTB.setLocation(0,10);			 
			 if(inaHLPSZ==null)
				 setCOLWDT(cl_dat.M_tblHELP_pbst,LP_COLHD);			 
			 else{
				 setCOLWDT(cl_dat.M_tblHELP_pbst,LP_COLHD,inaHLPSZ);
				 cl_dat.M_tblHELP_pbst.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			 }			 
			 pnlTAB1.add(jspHLPTB);
			 pnlTAB1.setSize(L_intWIDTH,L_intHIGHT);
			 pnlTAB1.setLocation(P_intXPOSN,P_intYPOSN);
			 pnlTAB1.setVisible(true);
			 cl_dat.M_tblHELP_pbst.setRowSelectionInterval(0,0);
		}catch(Exception L_EX)		{
			setMSG(L_EX,"crtHLPPNL");			setMSG("No data Found ..",'E');
		}
		cl_dat.M_tblHELP_pbst.addKeyListener(this);
		cl_dat.M_tblHELP_pbst.addFocusListener(this);
		cl_dat.M_tblHELP_pbst.addMouseListener(this);
		return pnlTAB1;
	}

	/*
	protected  JPanel crtHLPPNL(Object P_objSQLQRY,int P_intNUMCL,String[] LP_COLHD,int P_intXPOSN,int P_intYPOSN,int L_intWIDTH,int L_intHIGHT){
		JPanel pnlTAB1 = new JPanel();
		try{
			cl_dat.M_hstHELP_pbst = new Hashtable();
			cl_dat.M_intCOUNT_pbst = 0;
			String L_STRHLP = "";
			String L_COLVAL = "",LM_SPLTP = "";
			float L_FLTVL;			cl_dat.M_rstHELP_pbst=null;
			String P_strSQLQRY="";
			if(P_objSQLQRY instanceof String)
				P_strSQLQRY=(String)P_objSQLQRY;
			else if(P_objSQLQRY instanceof Object[])
				System.out.println("In object Array");
			cl_dat.M_rstHELP_pbst = cl_dat.exeSQLQRY(P_strSQLQRY);			
			cl_dat.M_rmdRSMHLP_pbst = cl_dat.M_rstHELP_pbst.getMetaData();			
			Vector vtrDATA=new Vector(10,5);			
			if(cl_dat.M_rstHELP_pbst !=null){
				while (cl_dat.M_rstHELP_pbst.next()){
					cl_dat.M_staTBDAT_pbst = new String[P_intNUMCL];
					for(int j = 0;j < P_intNUMCL;j++)
					{
						LM_SPLTP = cl_dat.M_rmdRSMHLP_pbst.getColumnTypeName(j+1);						if(!LM_SPLTP.equals("DATE")){
							if(LM_SPLTP.equals("Float")){
								L_FLTVL = cl_dat.M_rstHELP_pbst.getFloat(j+1);
								L_COLVAL = String.valueOf(L_FLTVL);
							}else
								L_COLVAL = cl_dat.M_rstHELP_pbst.getString(j+1);
							if(LM_SPLTP.equals("TIMESTAMP")){
								if(L_COLVAL != null)
									L_COLVAL = M_fmtLCDTM.format(cl_dat.M_rstHELP_pbst.getTimestamp(j+1));
							}
						}
						else if(LM_SPLTP.equals("DATE")){
							java.util.Date LM_STRDT = (cl_dat.M_rstHELP_pbst.getDate(j+1));
							if(LM_STRDT != null)
								L_COLVAL = M_fmtLCDAT.format(cl_dat.M_rstHELP_pbst.getDate(j+1));
							else
								L_COLVAL="";
						}
						if(L_COLVAL == null)
							L_COLVAL = "";
						cl_dat.M_staTBDAT_pbst[j] = nvlSTRVL(L_COLVAL,"").trim().replace('|',' ');
						L_STRHLP += nvlSTRVL(L_COLVAL,"").trim().replace('|',' ') + "|";
					}
					cl_dat.M_hstHELP_pbst.put(String.valueOf(cl_dat.M_intCOUNT_pbst),L_STRHLP);
					cl_dat.M_intCOUNT_pbst++;
					L_STRHLP = "";
					vtrDATA.addElement(cl_dat.M_staTBDAT_pbst);
				}
			}
			else			
			{
				cl_dat.M_flgHELPFL_pbst = false;
				cl_dat.M_strHLPSTR_pbst = "";
			}
			Object[][] objDATA=new Object[vtrDATA.size()][cl_dat.M_staTBDAT_pbst.length];
			for(int i=0;i<vtrDATA.size();i++)
				objDATA[i]=(Object[])vtrDATA.elementAt(i);
			if(cl_dat.M_rstHELP_pbst != null)
				cl_dat.M_rstHELP_pbst.close();			
			if(objDATA.length==0){
				setMSG("No Data Found ..",'E');
				return null;	
			}
			 pnlTAB1.removeAll();
			 cl_dat.M_tblHELP_pbst = new JTable(objDATA,LP_COLHD);
			 int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			 int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			 jspHLPTB = new JScrollPane(cl_dat.M_tblHELP_pbst,v1,h1);			 
			 jspHLPTB.setPreferredSize(new Dimension(L_intWIDTH-25,L_intHIGHT-25));
			 jspHLPTB.setLocation(0,10);			 
			 if(inaHLPSZ==null)
				 setCOLWDT(cl_dat.M_tblHELP_pbst,LP_COLHD);			 
			 else{
				 setCOLWDT(cl_dat.M_tblHELP_pbst,LP_COLHD,inaHLPSZ);
				 cl_dat.M_tblHELP_pbst.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			 }			 
			 pnlTAB1.add(jspHLPTB);
			 pnlTAB1.setSize(L_intWIDTH,L_intHIGHT);
			 pnlTAB1.setLocation(P_intXPOSN,P_intYPOSN);
			 pnlTAB1.setVisible(true);
			 cl_dat.M_tblHELP_pbst.setRowSelectionInterval(0,0);
		}catch(Exception L_EX)		{
			setMSG(L_EX,"crtHLPPNL");			setMSG("No data Found ..",'E');
		}
		cl_dat.M_tblHELP_pbst.addKeyListener(this);
		cl_dat.M_tblHELP_pbst.addFocusListener(this);
		cl_dat.M_tblHELP_pbst.addMouseListener(this);
		return pnlTAB1;
	}
*/
	public void changedUpdate(DocumentEvent L_DE){}
	public void insertUpdate(DocumentEvent L_DE){
		incSRCH();
	}
	public void removeUpdate(DocumentEvent L_DE){
		incSRCH();
	}
	/**Increamental sreach in help panel <br><b> Called internally from cl_hlp()</b>
	 */
	protected  void incSRCH(){
		cl_dat.M_intCNTCHR_pbst = cl_dat.M_txtHLPPOS_pbst.getText().length(); // No. of chars in text fiels
		cl_dat.M_intCTITM_pbst = cl_dat.M_tblHELP_pbst.getRowCount(); // No. of rows
		String L_STRSCH = " ";
		for(int i=0;i<cl_dat.M_intCTITM_pbst;i++){
			L_STRSCH = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(i,cl_dat.M_SPLSCH_pbst-1));
			if(cl_dat.M_intCNTCHR_pbst<= L_STRSCH.trim().length()){
				if(L_STRSCH.trim().substring(0,cl_dat.M_intCNTCHR_pbst).equalsIgnoreCase(cl_dat.M_txtHLPPOS_pbst.getText())){
					cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(i,cl_dat.M_SPLSCH_pbst-1,true));
					cl_dat.M_tblHELP_pbst.setRowSelectionInterval(i,i);
					cl_dat.M_strHLPSTR_pbst = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(i,cl_dat.M_intSPLRTN_pbst-1));
					break;
				}
			}
			else{
				L_STRSCH = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(i,cl_dat.M_SPLSCH_pbst-1));
				if(cl_dat.M_intCNTCHR_pbst<= L_STRSCH.trim().length()){
					if(L_STRSCH.trim().substring(0,cl_dat.M_intCNTCHR_pbst).equalsIgnoreCase(cl_dat.M_txtHLPPOS_pbst.getText())){
						cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(i,cl_dat.M_SPLSCH_pbst-1,true));
						cl_dat.M_tblHELP_pbst.setRowSelectionInterval(i,i);
						cl_dat.M_strHLPSTR_pbst = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(i,cl_dat.M_intSPLRTN_pbst-1));
						break;
					}
				}
			}
		}
	}
	/**Get String to be returned from help panel	 */
	protected  String getRTNSTR()
	{
		String P_strTRNVL = "";
		P_strTRNVL = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),cl_dat.M_intSPLRTN_pbst-1)).trim();
		return P_strTRNVL;
	}
	/**To perform actions on pressing enter or clicking on button "OK" in help window	
	 * 
	 * <p>Must be re-defined in chlid classes, with call to super at first.
	 * Stores values in cl_dat.strRTNVL,cl_dat.M_strHLPSTR,cl_dat.M_strHELP and distroyes help window 
	 */
	void exeHLPOK()
	{
		try
		{
 			cl_dat.M_strRTNVL_pbst = getRTNSTR();
			cl_dat.M_strHLPSTR_pbst = cl_dat.M_strRTNVL_pbst;
			cl_dat.M_strHELP_pbst = getSTRHLP();
		}catch(Exception e)
		{			System.out.println("Error in exeHLPOK(Parent) : " + e.toString());		}
		cl_dat.M_pnlHELP_pbst.removeAll();
		cl_dat.M_wndHLP_pbst.dispose();
		cl_dat.M_wndPOS_pbst.dispose();
	}
	protected  String getSTRHLP()
	{
		String L_STRHLP = "";
		L_STRHLP = cl_dat.M_hstHELP_pbst.get(String.valueOf(cl_dat.M_tblHELP_pbst.getSelectedRow())).toString().trim();
		return L_STRHLP;
	}
    public void textValueChanged(TextEvent L_TE){}
	public void keyTyped(KeyEvent L_KTE)
	{		M_objSOURC=L_KTE.getSource();	}
	
	/**
	  <b>TASKS :</B><br>
	  &nbsp&nbsp&nbsp Copy Source Refernce to M_objSOURC & Reset System Idle time timer at every event.<br>
	  &nbsp&nbsp&nbsp if '*' is pressed, clear contents of screen
	  &nbsp&nbsp&nbsp Source=M_cmbSBSL1_pbst || M_cmbSBSL2_pbst || M_cmbSBSL3_pbst : Focus transfer on arrow keys.<br>
	  &nbsp&nbsp&nbsp Source=M_btnEXIT_pbst : Remove Action/key listners to Button Pallet; Set SubSystem Combos to null;<br>
	  &nbsp&nbsp&nbsp Source=M_btnHLPOK_pbst || M_lstHELP_pbst || M_txtHLPPOS_pbst || LM_HLLPTBL : <br>
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp call to exeHPLOK() on enter <br>
	  &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp dispose on escape  <br>
	  &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp up,down,PgUp,PgDn,Home,End nevigation of help screen  on resp. buttons <br>
	  */
	public void keyPressed(KeyEvent L_KPE)
	{
		cl_dat.M_tmrIDLTM_pbst.restart();//RESTTING SYSTEM IDLE TIME COUNTER
		M_objSOURC=L_KPE.getSource();
		char L_chrSOURC=L_KPE.getKeyChar();
		int L_intCDSRC=L_KPE.getKeyCode();
		setMSG("",'N');
		if(M_objSOURC==cl_dat.M_cmbSBSL1_pbst)//SBS LEVEL - 1 COMBO
		{
			if(L_intCDSRC==L_KPE.VK_RIGHT||L_intCDSRC==L_KPE.VK_ENTER)
			{//TO SHOW LEVEL - 2 COMBO
				if(cl_dat.M_cmbSBSL2_pbst!=null)
				{
					cl_dat.M_cmbSBSL2_pbst.requestFocus();
					cl_dat.M_cmbSBSL2_pbst.showPopup();
				}
			}
		}
		else if(M_objSOURC==cl_dat.M_cmbSBSL2_pbst)
		{
			if(L_intCDSRC==L_KPE.VK_RIGHT||L_intCDSRC==L_KPE.VK_ENTER)
			{
				if(cl_dat.M_cmbSBSL3_pbst!=null)
				{//SHOW LEVEL - 3 COMBO
					cl_dat.M_cmbSBSL3_pbst.requestFocus();
					cl_dat.M_cmbSBSL3_pbst.showPopup();
				}
				else
				{//POPULATE OPTION COMBO
					cl_dat.M_cmbOPTN_pbst.removeAllItems();
					cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPSEL_pbst);
					for(int i=0;i<M_staUSRRT.length&&M_staUSRRT[i][0]!=null;i++)
					{
						if(M_staUSRRT[i][1].equals(cl_dat.M_cmbSBSL1_pbst.getSelectedItem().toString())&&M_staUSRRT[i][2].equals(cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString()))
						{
							setOPTN(i);
							cl_dat.M_cmbOPTN_pbst.requestFocus();
							cl_dat.M_cmbOPTN_pbst.showPopup();
							break;
						}
					}
				}
			}
			else if(L_intCDSRC==L_KPE.VK_LEFT)
			{//SHOW LEVEL - 1 COMBO
				cl_dat.M_cmbSBSL1_pbst.requestFocus();
				cl_dat.M_cmbSBSL1_pbst.showPopup();
			}
		}
		else if(M_objSOURC==cl_dat.M_cmbSBSL3_pbst)
		{
			if(L_intCDSRC==L_KPE.VK_ENTER)
			{//SHOW OPTION COMBO
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				cl_dat.M_cmbOPTN_pbst.showPopup();
			}
			else if(L_intCDSRC==L_KPE.VK_LEFT)
			{//SHOW LEVEL - 2 COMBO
				cl_dat.M_cmbSBSL2_pbst.requestFocus();
				cl_dat.M_cmbSBSL2_pbst.showPopup();
			}
		}
		if(L_chrSOURC=='*')
		{
			cl_dat.M_btnEXIT_pbst.setEnabled(true);
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			cl_dat.M_btnUNDO_pbst.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
				cl_dat.M_cmbOPTN_pbst.setSelectedIndex(0);
			setENBL(false);
			clrCOMP();
			cl_dat.M_btnEXIT_pbst.requestFocus();
		}
		if((L_intCDSRC== L_KPE.VK_ENTER)&&(M_objSOURC.equals(cl_dat.M_txtHLPPOS_pbst)))
    		exeHLPOK();
		if((L_intCDSRC== L_KPE.VK_ESCAPE&&cl_dat.M_wndHLP_pbst!=null)){
			cl_dat.M_pnlHELP_pbst.removeAll();
			cl_dat.M_wndHLP_pbst.dispose();
			cl_dat.M_wndPOS_pbst.dispose();
	    }
		if((L_intCDSRC== L_KPE.VK_DOWN)&&(M_objSOURC.equals(cl_dat.M_txtHLPPOS_pbst))){
    		if(!(cl_dat.M_tblHELP_pbst.getSelectedRow() == (cl_dat.M_tblHELP_pbst.getRowCount()-1))){
				cl_dat.M_tblHELP_pbst.setRowSelectionInterval(cl_dat.M_tblHELP_pbst.getSelectedRow()+1,cl_dat.M_tblHELP_pbst.getSelectedRow()+1);
				cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow(),cl_dat.M_SPLSCH_pbst-1,true));
			}
		}
//FOR PAGE SCORLL DOWN. ADDED ON 03/04/2004 BY AAP
		if((L_intCDSRC== L_KPE.VK_PAGE_DOWN)&&(M_objSOURC.equals(cl_dat.M_txtHLPPOS_pbst))){
    		if(!(cl_dat.M_tblHELP_pbst.getSelectedRow() > (cl_dat.M_tblHELP_pbst.getRowCount()-5))){
				cl_dat.M_tblHELP_pbst.setRowSelectionInterval(cl_dat.M_tblHELP_pbst.getSelectedRow()+5,cl_dat.M_tblHELP_pbst.getSelectedRow()+5);
				cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow(),cl_dat.M_SPLSCH_pbst-1,true));
			}
			else if(!(cl_dat.M_tblHELP_pbst.getSelectedRow() == (cl_dat.M_tblHELP_pbst.getRowCount()-1))){
				cl_dat.M_tblHELP_pbst.setRowSelectionInterval(cl_dat.M_tblHELP_pbst.getSelectedRow()+1,cl_dat.M_tblHELP_pbst.getSelectedRow()+1);
				cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow(),cl_dat.M_SPLSCH_pbst-1,true));
			}
		}
//FOR PAGE SCORLL UP. ADDED ON 03/04/2004 BY AAP		
		if((L_intCDSRC== L_KPE.VK_PAGE_UP)&&(M_objSOURC.equals(cl_dat.M_txtHLPPOS_pbst))){
			if(cl_dat.M_tblHELP_pbst.getSelectedRow()>5){
				cl_dat.M_tblHELP_pbst.setRowSelectionInterval(cl_dat.M_tblHELP_pbst.getSelectedRow()-5,cl_dat.M_tblHELP_pbst.getSelectedRow()-5);
				cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow(),cl_dat.M_SPLSCH_pbst,true));
			}
			else if(cl_dat.M_tblHELP_pbst.getSelectedRow()!=0)
			{
				cl_dat.M_tblHELP_pbst.setRowSelectionInterval(cl_dat.M_tblHELP_pbst.getSelectedRow()-1,cl_dat.M_tblHELP_pbst.getSelectedRow()-1);
				cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow()-1,cl_dat.M_SPLSCH_pbst-1,true));
			}
		}
		if((L_intCDSRC== L_KPE.VK_UP)&&(M_objSOURC.equals(cl_dat.M_txtHLPPOS_pbst))&&(cl_dat.M_tblHELP_pbst.getSelectedRow()!=0)){
    		cl_dat.M_tblHELP_pbst.setRowSelectionInterval(cl_dat.M_tblHELP_pbst.getSelectedRow()-1,cl_dat.M_tblHELP_pbst.getSelectedRow()-1);
			cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow()-1,cl_dat.M_SPLSCH_pbst-1,true));
		}
		if((L_intCDSRC== L_KPE.VK_END)&&(M_objSOURC.equals(cl_dat.M_txtHLPPOS_pbst))&&(cl_dat.M_tblHELP_pbst.getSelectedRow()!=cl_dat.M_tblHELP_pbst.getRowCount()-1)){
    		cl_dat.M_tblHELP_pbst.setRowSelectionInterval(cl_dat.M_tblHELP_pbst.getRowCount()-1,cl_dat.M_tblHELP_pbst.getRowCount()-1);
			cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow(),cl_dat.M_SPLSCH_pbst,true));
		}
		if((L_intCDSRC== L_KPE.VK_HOME)&&(M_objSOURC.equals(cl_dat.M_txtHLPPOS_pbst))&&(cl_dat.M_tblHELP_pbst.getSelectedRow()!=0)){
    		cl_dat.M_tblHELP_pbst.setRowSelectionInterval(0,0);
			cl_dat.M_tblHELP_pbst.scrollRectToVisible(cl_dat.M_tblHELP_pbst.getCellRect(cl_dat.M_tblHELP_pbst.getSelectedRow(),cl_dat.M_SPLSCH_pbst,true));
		}
		if((L_intCDSRC==9)&&(M_objSOURC.equals(cl_dat.M_wndHLP_pbst)))
			cl_dat.M_btnHLPOK_pbst.requestFocus();
	}
	public void keyReleased(KeyEvent L_KRE)
	{
		M_objSOURC=L_KRE.getSource();
	}

	/**
	 * Method to make the string as aurgument of desired length
	 * 
	 * Method to make the string as aurgument of desired length
	 * 
	 * <p>Appends or prepends blank spaces as per the direction given if String length is less than required, other wise, returns substring of that size.
	 * 
	 * @param P_chrPADTP Type of Padding 
	 * @param P_strSTRVL String to be modified
	 * @param P_intCLRTN Final length of the string required
	 * @return String of P_intCLRTN length.
	 * 
	 * <br><hr><b>USAGE</B><br>
	 * padSTRING('L',"MyString",10);<br>
	 * &nbsp&nbsp&nbsp Returns : "  MyString" <br>
	 * padSTRING('L',"MyString",5);<br>
	 * &nbsp&nbsp&nbsp Returns : "MyStr"<br>
	 * padSTRING('R',"MyString",10);<br>
	 * &nbsp&nbsp&nbsp Returns : "MyString  "<br>
	 * padSTRING('R',"MyString",5);<br>
	 * &nbsp&nbsp&nbsp Returns : "MyStr"<br>
	 * padSTRING('C',"MyString",10);<br>
	 * &nbsp&nbsp&nbsp Returns : " MyString "<br>
	 * padSTRING('C',"MyString",5);<br>
	 * &nbsp&nbsp&nbsp Returns : "MyStr"<br>
	 * <hr>
	 * <p><b>Restrications :</B><br>
	 * Value of P_chrPADTP should be one of <br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "C" : Center<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "L" : Left<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "R" : Right<br>
	 */
	protected  String padSTRING(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		try
		{
			String L_STRSP = " ";
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			if(P_intPADLN <= L_STRLN)
			{
				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			int L_STRDF = P_intPADLN - L_STRLN;
			StringBuffer L_STRBUF;
			switch(P_chrPADTP)
			{
				case 'C':
					L_STRDF = L_STRDF / 2;
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
					break;
				case 'R':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  P_strSTRVL+L_STRBUF ;
					break;
				case 'L':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL ;
					break;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"padSTRING");
		}
		return P_strTRNVL;
	}
	/**
	 * <b>TASKS : </b><br>
	 * Reset System idle time timer at every click<br>
	 * Copy refernce of soure to M_objSOURC
	 */
	public void mouseClicked(MouseEvent L_ME)
	{
		cl_dat.M_tmrIDLTM_pbst.restart();
		M_objSOURC=L_ME.getSource();
	}
	/**	  <b>TASKS : </b><br>	  Copy refernce of soure to M_objSOURC	 */
	public void mouseEntered(MouseEvent L_ME)
	{
		M_objSOURC=L_ME.getSource();
	}
	/** <b>TASKS : </b><br> Copy refernce of soure to M_objSOURC	 */
	public void mouseExited(MouseEvent L_ME)
	{
		M_objSOURC=L_ME.getSource();
	}
	/** <b>TASKS : </b><br> Copy refernce of soure to M_objSOURC	 */
	public void mousePressed(MouseEvent L_ME)
	{
		M_objSOURC=L_ME.getSource();
	}
	/** <b>TASKS : </b><br> Copy refernce of soure to M_objSOURC	 */
	public void mouseReleased(MouseEvent L_ME)
	{
		M_objSOURC=L_ME.getSource();
	}
	/** <b>TASKS : </b><br> Copy refernce of soure to M_objSOURC */
	public void mouseUp(MouseEvent L_ME)
	{
		M_objSOURC=L_ME.getSource();
	}
	// Method to convert number into proper decimal format (16.07.2002)
	/**
	 * Method is obsolute as setNumberFormat(double,int) is defined
	 * @deprecated since revision 2.0.0
	 */
	protected  String setFMT(String LP_STR,int LP_LEN){
		int L_DOTPOS;
		String L_AFTERDEC;
		String LP_DEC = "";
		String L_MINUS = "";
		for(int i=0;i<LP_LEN;i++)
			LP_DEC += "0";
		LP_STR = LP_STR.trim();
		L_DOTPOS = LP_STR.indexOf(".");
		if(L_DOTPOS != -1){
			L_AFTERDEC = LP_STR.substring(L_DOTPOS + 1) + LP_DEC;
			L_AFTERDEC = String.valueOf(Math.round(Double.parseDouble("0." + L_AFTERDEC.substring(LP_LEN)))/Math.pow(10,LP_LEN));
			if(LP_STR.charAt(0) == '-'){
				L_MINUS = "-";
				LP_STR = (String.valueOf(Math.abs(Double.parseDouble(LP_STR)) + Double.parseDouble(L_AFTERDEC))+ LP_DEC).substring(0,L_DOTPOS + LP_LEN);
			}
			else
				LP_STR = (String.valueOf(Math.abs(Double.parseDouble(LP_STR)) + Double.parseDouble(L_AFTERDEC))+ LP_DEC).substring(0,L_DOTPOS + LP_LEN + 1);
		}
		else{			// if no dot is present
			LP_STR += "." + LP_DEC;
		}
		return (L_MINUS + LP_STR).trim();
	}
	/**
	 * Method to write printing format characters in .doc file
	 * 
	 *  Method to write printing format characters in .doc file

	 * <p>Writes value of the respective predefined string using the output stream received as aurgument
	 * 
	 * @param L_OUT DataOutputStream with which, format character is to be wirtten
	 * @param L_FMTSTR Name of the pre-defined string constant for format character
	 * 
	 * <br><hr><b>USAGE</B><br>
	 * prnFMTCHR(L_OUT,M_strBOLD)<br>
	 * &nbsp&nbsp&nbsp Writes BOLD format character to DataOutputStream L_OUT<br>
	 * <p>Restrications :<br>
	 * Value of L_FMTSTR should be one of <br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strCPI10 : CPI10<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strCPI12 : CPI12<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strCPI17 : CPI17<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strNOCPI17 : Clear CPI17<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strBOLD : BOLD<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strNOBOLD : clear BOLD<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strENH : Enhanced Text (for titles)<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strNOENH : clear Enhanced Text<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp M_strEJT : Page Eject<br>
	 */
	protected  void prnFMTCHR(DataOutputStream L_DOUT,String L_FMTSTR){
		try{
			if(L_FMTSTR.equals(M_strCPI10))
			{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("P");
			}
			if(L_FMTSTR.equals(M_strCPI12))
			{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("M");
			}
			if(L_FMTSTR.equals(M_strCPI17))
			{
					L_DOUT.writeChar(15);
			}
			if(L_FMTSTR.equals(M_strNOCPI17))
			{
					L_DOUT.writeChar(18);
			}
			if(L_FMTSTR.equals(M_strBOLD))
			{
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("G");
				}
			}
			if(L_FMTSTR.equals(M_strNOBOLD))
			{
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("H");
				}
			}
			if(L_FMTSTR.equals(M_strENH))
			{
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("W1");
				}
			}
			if(L_FMTSTR.equals(M_strNOENH))
			{
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("W0");
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("F");
				}
			}
			if(L_FMTSTR.equals(M_strEJT))
			{
					L_DOUT.writeChar(12);
			}
		}catch(IOException L_EX){
			setMSG(L_EX,"prnFMTCHR");
		}
	}
	//ADDED BY AAP 26/05/03
	/**
	 * Method to create a table with scroll pane on given panel 
	 * 
	 * Method to create a table with scroll pane on given panel 

	 * <p> Creates object of cl_JTBL with given column heading, width with check boxes at specified column indix/indices and no. off rows. Sets size of the table and adds it to the said panel<br>
	 * Adds refernce of the table into M_vtrSCCOMP<br>
	 * Adds 'this' as key/focus listener for editor components of the table.<br>
	 * 
	 * @param LP_TBLPNL Panel on which table object is to be layed. May be 'this' when called from child classes.<br> Cannot be null.
	 * @param LP_COLHD String array for column headers <br> Cannot be null.
	 * @param LP_ROWCNT No. of rows in the table 
	 * @param P_ROW Row index of LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param P_COL Column index of LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param P_WETX No. Rows the table should occupy on LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param P_WETY No. Columns the table should occupy on LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param LP_ARRGSZ Integer array specifiing gutter size (width ) of the columns <br> Cannot be null.
	 * @param LP_CHKCOL Ineger array containing indices of columns where Check boxes are to be displayed. Can be null if check box is not required.
	 * @return An object of {@link cl_JTBL}
	 *	
	 * <br><hr><b>USAGE</B><br>
	 * crtTBLPNL(this,new String[]{"COL1","COL2"},10,1,2,3,4,new int[]{50,100},new int[]{0})<br>
	 * &nbsp&nbsp&nbsp Will create an object of cl_JTBL and place it in child class screen as :<br>
	 * &nbsp&nbsp&nbsp Two Columns with titles "COL1" & "COL2"  and width equivalent to 50 and 100 pixels resp.<br>
	 * &nbsp&nbsp&nbsp 10 rows in the table<br>
	 * &nbsp&nbsp&nbsp Possition the table on first row, at second column.<br>
	 * &nbsp&nbsp&nbsp Table hight equal to hight of 3 rows.<br>
	 * &nbsp&nbsp&nbsp Table width equal to hight of 4 columns.<br>
	 * &nbsp&nbsp&nbsp Column "COL1" will contain check-boxess in it.<br>
	 * <p><b>RESTRICTIONS :</b><br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp Length of LP_COLHD,LP_ARRGSZ should be equal.<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp Max. value for any element in LP_CHKCOL should not be greater than LP_COLHD.length-1<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp If new TableCellEditor, other than default is specified in child class, key/focus listeners are to be registered subseqently.
	 */
	protected  cl_JTBL crtTBLPNL(JPanel LP_TBLPNL,String[] LP_COLHD,int LP_ROWCNT,int P_ROW,int P_COL,double P_WETX,double P_WETY,int[] LP_ARRGSZ,int LP_CHKCOL[])
	{
		try
		{
			int LP_COLCNT=LP_COLHD.length;
			Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];
			for(int j = 0;j < LP_ROWCNT;j++)
			{
				for(int i=0;i<LP_COLCNT;i++)
				{
					L_TBLDT[j][i]="";
					for(int k=0;k<LP_CHKCOL.length;k++)
					{
						if(i==LP_CHKCOL[k])
							L_TBLDT[j][i]=new Boolean(false);
					}
				}
			}
			cl_dat.M_flgCHKTB_pbst = true;
			cl_JTBL L_TBL1 = new cl_JTBL(L_TBLDT,LP_COLHD);
			L_TBL1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			M_vtrSCCOMP.add(L_TBL1);//ADDED TO COMPONENT VECTOR
			int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			setCOLWDT(L_TBL1,LP_COLHD,LP_ARRGSZ);
			JScrollPane jspTBL1 = new JScrollPane(L_TBL1,v1,h1);
			add(jspTBL1,P_ROW,P_COL,P_WETX,P_WETY,LP_TBLPNL,'L');
			for(int i=0;i<((cl_JTBL)L_TBL1).cmpEDITR.length;i++)
			{
				((JComponent)((cl_JTBL)L_TBL1).cmpEDITR[i]).addKeyListener(this);
				((JComponent)((cl_JTBL)L_TBL1).cmpEDITR[i]).addFocusListener(this);
			}
			return L_TBL1;
		}catch(Exception L_EX){
			System.out.println("crtTBLPNL: "+L_EX);
		}
		return null;
	}
	/**
	 * Method to create a table using cl_JTable with scroll pane on given panel 
	 * 
	 * Method to create a table using cl_JTable with scroll pane on given panel 

	 * <p> Creates object of cl_JTable with given column heading, width with check boxes at specified column indix/indices and no. off rows. Sets size of the table and adds it to the said panel<br>
	 * Adds refernce of the table into M_vtrSCCOMP<br>
	 * Adds 'this' as key/focus listener for editor components of the table.<br>
	 * 
	 * @param LP_TBLPNL Panel on which table object is to be layed. May be 'this' when called from child classes.<br> Cannot be null.
	 * @param LP_COLHD String array for column headers <br> Cannot be null.
	 * @param LP_ROWCNT No. of rows in the table 
	 * @param P_ROW Row index of LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param P_COL Column index of LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param P_WETX No. Rows the table should occupy on LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param P_WETY No. Columns the table should occupy on LP_TBLPNL, for position of the table on LP_TBLPNL
	 * @param LP_ARRGSZ Integer array specifiing gutter size (width ) of the columns <br> Cannot be null.
	 * @param LP_CHKCOL Ineger array containing indices of columns where Check boxes are to be displayed. Can be null if check box is not required.
	 * @return An object of {@link cl_JTable}
	 * 
	 * <br><hr><b>USAGE</B><br>
	 * crtTBLPNL(this,new String[]{"COL1","COL2"},10,1,2,3,4,new int[]{50,100},new int[]{0})<br>
	 * &nbsp&nbsp&nbsp Will create an object of cl_JTBL and place it in child class screen as :<br>
	 * &nbsp&nbsp&nbsp Two Columns with titles "COL1" & "COL2"  and width equivalent to 50 and 100 pixels resp.<br>
	 * &nbsp&nbsp&nbsp 10 rows in the table<br>
	 * &nbsp&nbsp&nbsp Possition the table on first row, at second column.<br>
	 * &nbsp&nbsp&nbsp Table hight equal to hight of 3 rows.<br>
	 * &nbsp&nbsp&nbsp Table width equal to hight of 4 columns.<br>
	 * &nbsp&nbsp&nbsp Column "COL1" will contain check-boxess in it.<br>
	 * <p><b>RESTRICTIONS :</b><br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp Length of LP_COLHD,LP_ARRGSZ should be equal.<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp Max. value for any element in LP_CHKCOL should not be greater than LP_COLHD.length-1<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp If new TableCellEditor, other than default is specified in child class, key/focus listeners are to be registered subseqently.
	 */
	protected  cl_JTable crtTBLPNL1(JPanel LP_TBLPNL,String[] LP_COLHD,int LP_ROWCNT,int P_ROW,int P_COL,double P_WETX,double P_WETY,int[] LP_ARRGSZ,int LP_CHKCOL[])
	{
		try
		{
			int LP_COLCNT=LP_COLHD.length;
			Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];
			for(int j = 0;j < LP_ROWCNT;j++)
			{
				for(int i=0;i<LP_COLCNT;i++)
				{
					L_TBLDT[j][i]="";
					for(int k=0;k<LP_CHKCOL.length;k++)
					{
						if(i==LP_CHKCOL[k])
							L_TBLDT[j][i]=new Boolean(false);
					}
				}
			}
			cl_dat.M_flgCHKTB_pbst = true;
			cl_JTable L_TBL1 = new cl_JTable(L_TBLDT,LP_COLHD);
			L_TBL1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			M_vtrSCCOMP.add(L_TBL1);//ADDED TO COMPONENT VECTOR
			int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			setCOLWDT(L_TBL1,LP_COLHD,LP_ARRGSZ);
			JScrollPane jspTBL1 = new JScrollPane(L_TBL1,v1,h1);
			add(jspTBL1,P_ROW,P_COL,P_WETX,P_WETY,LP_TBLPNL,'L');
			for(int i=0;i<((cl_JTable)L_TBL1).cmpEDITR.length;i++)
			{
				((JComponent)((cl_JTable)L_TBL1).cmpEDITR[i]).addKeyListener(this);
				((JComponent)((cl_JTable)L_TBL1).cmpEDITR[i]).addFocusListener(this);
			}
			return L_TBL1;
		}catch(Exception L_EX){
			System.out.println("crtTBLPNL: "+L_EX);
		}
		return null;
	}
    //MOTHOD WILL NOT BE REQUIRED IF CL_JTBL IS USED.
	//OR, OTHERWISE, MAKE USE OF CONSTRUCTOR (OBJECT[][],OBJECT[]) IN CLASS JTable
	//Creates dynamic JTable with checked column
	//This will create checkboxes starting from last column
	//if checkbox is required in the first column then 0 is passed as LP_CHKCOL parameter
/*	public JTable crtDFLTBL(String[] LP_COLHD,int LP_ROWCNT,int P_intXPOSN,int P_intYPOSN,int L_intWIDTH,int L_intHIGHT,int LP_XWNDPS,int LP_YWNDPS,int LP_WNDWD,int LP_WNDHT,int[] LP_ARRGSZ,int LP_CHKCOL){
		try{
			JPanel pnlTAB1 = new JPanel();
			Object[][] L_TBLDT1 = crtTBLDAT(LP_ROWCNT,LP_COLHD.length,LP_CHKCOL); // Creating the Object Data
			cl_tab2 L_TBLOBJ1 = new cl_tab2(L_TBLDT1,LP_COLHD);
			JTable L_TBL1 = new JTable(L_TBLOBJ1);
			L_TBL1.setBackground(new Color(213,213,255));
			int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			JScrollPane jspTBL1 = new JScrollPane(L_TBL1,v1,h1);
			jspTBL1.setPreferredSize(new Dimension(L_intWIDTH-25,L_intHIGHT-25));
			jspTBL1.setLocation(0,100);
			pnlTAB1.removeAll();
			setCOLWDT(L_TBL1,LP_COLHD,LP_ARRGSZ);
			pnlTAB1.add(jspTBL1);
			pnlTAB1.setSize(L_intWIDTH,L_intHIGHT);
			pnlTAB1.setLocation(P_intXPOSN,P_intYPOSN);
			cl_dat.M_wndPOS_pbst = new JWindow();
			cl_dat.M_wndPOS_pbst.setBounds(LP_XWNDPS,LP_YWNDPS,LP_WNDWD,LP_WNDHT);
			cl_dat.M_wndPOS_pbst.getContentPane().add(pnlTAB1);
			cl_dat.M_wndPOS_pbst.toFront();
			cl_dat.M_wndPOS_pbst.show();
			cl_dat.M_flgCHKTB_pbst = false;
			return L_TBL1;
		}catch(Exception L_EX){
			System.out.println("crtDFLTBL: "+L_EX);
		}
		return null;
	}
*/
	//Creates Dynamic table using Abstract Table Model
	//Data is fetched directly into table when query is passed
	protected  JTable crtDYNTBL1(String P_strSQLQRY,String[] LP_COLHD,int P_intXPOSN,int P_intYPOSN,int L_intWIDTH,int L_intHIGHT,int LP_XWNDPS,int LP_YWNDPS,int LP_WNDWD,int LP_WNDHT){
		JPanel pnlTAB1 = new JPanel();
		try{
			pnlTAB1.removeAll();
			int L_NUMCOL = LP_COLHD.length;
			String L_COLVAL = "",LM_SPLTP = "";
			float L_FLTVL;
			ResultSet M_rstHELP_pbst = cl_dat.exeSQLQRY(P_strSQLQRY);
			ResultSetMetaData M_rmdRSMHLP_pbst = M_rstHELP_pbst.getMetaData();
			Vector vtrDATA=new Vector(10,5);
			while (M_rstHELP_pbst.next()){
				String[] L_DATA = new String[L_NUMCOL];
				for(int j = 0;j < L_NUMCOL;j++){
					LM_SPLTP = M_rmdRSMHLP_pbst.getColumnTypeName(j+1);
					if(!LM_SPLTP.equals("DATE")){
						if(LM_SPLTP.equals("Float")){
							L_FLTVL = (M_rstHELP_pbst.getFloat(j+1));
							L_COLVAL = String.valueOf(L_FLTVL);
						}
						else
							L_COLVAL = (M_rstHELP_pbst.getString(j+1));
						if(L_COLVAL== null)
							L_COLVAL = "";
					}
					else if(LM_SPLTP.equals("TIMESTAMP"))
						L_COLVAL = M_fmtLCDTM.format(cl_dat.M_rstHELP_pbst.getDate(j+1));
					else if(LM_SPLTP.equals("DATE")){
						java.util.Date LM_STRDT = (cl_dat.M_rstHELP_pbst.getDate(j+1));
						if(LM_STRDT !=null)
							L_COLVAL = M_fmtLCDAT.format(cl_dat.M_rstHELP_pbst.getDate(j+1));
						 else
							 L_COLVAL="";
					}
					L_DATA[j] = L_COLVAL;
				}
				vtrDATA.addElement(L_DATA);
			}
			String[][] L_OBJDATA=new String[vtrDATA.size()][L_NUMCOL];
			for(int i=0;i<vtrDATA.size();i++)
				L_OBJDATA[i]=(String[])vtrDATA.elementAt(i);
			cl_dat.M_tblHELP_pbst = new JTable(L_OBJDATA,LP_COLHD);
			cl_dat.M_tblHELP_pbst.setBackground(new Color(213,213,255));
			int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			JScrollPane jspTBL1 = new JScrollPane(cl_dat.M_tblHELP_pbst,v1,h1);
			jspTBL1.setPreferredSize(new Dimension(L_intWIDTH-25,L_intHIGHT-25));
			jspTBL1.setLocation(0,10);
			setCOLWDT(cl_dat.M_tblHELP_pbst,LP_COLHD);
			pnlTAB1.add(jspTBL1);
			pnlTAB1.setSize(L_intWIDTH,L_intHIGHT);
			pnlTAB1.setLocation(P_intXPOSN,P_intYPOSN);
			pnlTAB1.setVisible(true);
		}catch(Exception L_EX){
			setMSG(L_EX,"crtDYNTBL1");
		}
		cl_dat.M_tblHELP_pbst.addKeyListener(this);
		cl_dat.M_tblHELP_pbst.addFocusListener(this);
		cl_dat.M_tblHELP_pbst.addMouseListener(this);
		return cl_dat.M_tblHELP_pbst;
	}
	protected   void setENLEXT(){              // set ENABLE EXIT
	    cl_dat.M_btnEXIT_pbst.setEnabled(true);
        cl_dat.M_btnSAVE_pbst.setEnabled(false);
        cl_dat.M_btnUNDO_pbst.setEnabled(false);
        cl_dat.M_btnSAVE_pbst.setText("Save");
        cl_dat.M_cmbOPTN_pbst.setEnabled(true);
	}
//AAP 10/09/2003
	//COMMENTED ON 10/09/2003 AS SEPARATE METHOD WILL NOT BE REQUIRED **************
	//SINCE ABOVE METHOD IS MODIFIED TO MAKE USE OF CL_JTBL*************************
	//This table is created when 2 dynamic table are required in 1 form
	//Creates Dynamic table using Abstract Table Model
	//Data is fetched directly into table when query is passed
/*	public JTable crtDYNTBL2(String P_strSQLQRY,String[] LP_COLHD,int P_intXPOSN,int P_intYPOSN,int L_intWIDTH,int L_intHIGHT,int LP_XWNDPS,int LP_YWNDPS,int LP_WNDWD,int LP_WNDHT){
		JPanel pnlTAB1 = new JPanel();
		try{
			pnlTAB1.removeAll();
			int L_NUMCOL = LP_COLHD.length;
			cl_dat.LM_TBMDL2 = new cl_tbl2();
			String L_COLVAL = "",LM_SPLTP = "";
			float L_FLTVL;
			cl_dat.M_rstHELP_pbst = cl_dat.exeSQLQRY(P_strSQLQRY);
			cl_dat.M_rmdRSMHLP_pbst = cl_dat.M_rstHELP_pbst.getMetaData();
			cl_tbl2.ocl_tbl2.LM_TBLHDR = LP_COLHD;
			cl_tbl2.ocl_tbl2.LM_COLCNT = L_NUMCOL;
			while (cl_dat.M_rstHELP_pbst.next()){
				String[] L_DATA = new String[L_NUMCOL];
				for(int j = 0;j < L_NUMCOL;j++){
					LM_SPLTP = cl_dat.M_rmdRSMHLP_pbst.getColumnTypeName(j+1);
					if(!LM_SPLTP.equals("DATE")){
						if(LM_SPLTP.equals("Float")){
							L_FLTVL = (cl_dat.M_rstHELP_pbst.getFloat(j+1));
							L_COLVAL = String.valueOf(L_FLTVL);
						}
						else
							L_COLVAL = (cl_dat.M_rstHELP_pbst.getString(j+1));
						if(L_COLVAL== null)
							L_COLVAL = "";
					}
					else if(LM_SPLTP.equals("TIMESTAMP"))
						L_COLVAL = cc_dattm.occ_dattm.setDBSTM(L_COLVAL);
					else if(LM_SPLTP.equals("DATE")){
						java.util.Date LM_STRDT = (cl_dat.M_rstHELP_pbst.getDate(j+1));
						if(LM_STRDT !=null)
							L_COLVAL = cc_dattm.occ_dattm.setDATE("DMY",LM_STRDT);
						else
							L_COLVAL="";
					}
					L_DATA[j] = L_COLVAL;
				}
				cl_tbl2.ocl_tbl2.M_staTBDAT_pbst.addElement(L_DATA);
			}
			cl_dat.LM_TBMDL2.fireTableChanged(null);
			if(cl_dat.M_rstHELP_pbst != null)
				cl_dat.M_rstHELP_pbst.close();
			cl_dat.M_tblHELP_pbst = new JTable(cl_dat.LM_TBMDL2);
			int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			JScrollPane jspTBL1 = new JScrollPane(cl_dat.M_tblHELP_pbst,v1,h1);
			jspTBL1.setPreferredSize(new Dimension(L_intWIDTH-25,L_intHIGHT-25));
			jspTBL1.setLocation(0,10);
			setCOLWDT(cl_dat.M_tblHELP_pbst,LP_COLHD);
			pnlTAB1.add(jspTBL1);
			pnlTAB1.setSize(L_intWIDTH,L_intHIGHT);
			pnlTAB1.setLocation(P_intXPOSN,P_intYPOSN);
			pnlTAB1.setVisible(true);
			cl_dat.M_wndPOS_pbst = new JWindow();
			cl_dat.M_wndPOS_pbst.setBounds(LP_XWNDPS,LP_YWNDPS,LP_WNDWD,LP_WNDHT);
			cl_dat.M_wndPOS_pbst.getContentPane().add(pnlTAB1);
			cl_dat.M_wndPOS_pbst.toFront();
			cl_dat.M_wndPOS_pbst.show();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"crtDYNTBL2","");
		}
		cl_dat.M_tblHELP_pbst.addKeyListener(this);
		cl_dat.M_tblHELP_pbst.addFocusListener(this);
		cl_dat.M_tblHELP_pbst.addMouseListener(this);
		return cl_dat.M_tblHELP_pbst;
	}
 */
	protected  void setCOLWDT(JTable LP_TBLNM,String[] LP_TBLHDG,int[] L_intWIDTH){
		for(int i=0;i<LP_TBLHDG.length;i++){
			TableColumn L_TBLCOL = LP_TBLNM.getColumn(LP_TBLHDG[i]);
			if(L_intWIDTH[i] !=0)
				L_TBLCOL.setPreferredWidth(L_intWIDTH[i]);
		}
	}
	protected void setCOLWDT(JTable LP_TBLNM,String[] LP_TBLHDG){
		for(int i=0;i<LP_TBLHDG.length;i++){
			TableColumn L_TBLCOL = LP_TBLNM.getColumn(LP_TBLHDG[i]);
		}
	}
	/**
	 * Check String for null value
	 * 
	 * Check String for null value
	 * 
	 * @param LP_VARVL String to be checked for null
	 * @param LP_DEFVL String to be returned if LP_VARVL is null
	 * @return Original String if it was not null, default String if original straing was null
	 * 
	 * <br><hr><b>Usage :</b><br>Ex. 1 : String s=null;
	 * <br>		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp				nvlSTRVL(s,"STRING") returns "STRING"
	 * <br>				Ex. 2 : String s1="ABC";
	 * <br>		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp				nvlSTRVL(s1,"STRING") returns "ABC"
	 */
	protected String nvlSTRVL(String LP_VARVL, String LP_DEFVL)
	{
		try
		{
			if (LP_VARVL != null)
			{
				if (LP_VARVL.trim().length() > 0)
				{
					LP_VARVL = LP_VARVL.trim();
                } // if (LP_VARVL.trim().length() > 0)
                else
				{
					LP_VARVL = LP_DEFVL;
                } // if (LP_VARVL.trim().length() > 0)
             } // if (LP_VARVL != null)
             else
			 {
				LP_VARVL = LP_DEFVL;
             } // if (LP_VARVL != null) {
        }catch (Exception L_EX)
		{
			setMSG(L_EX,"nvlSTRVL");
		}
			return LP_VARVL;
	}
	/**
	 * Method to convert String into double
	 * 
	 * Method to convert String into double
	 * 
	 * @param LP_VARCL String to be converted to number
	 * @return 'Zero' if string is not a number otherwise, double value of the string
	 */
	protected double nvlNUMDB(String LP_VARVL){
		double LP_RETVL = 0;
		try
		{
			if(LP_VARVL.length() > 0)
				LP_RETVL = Double.parseDouble(LP_VARVL);
		}catch (Exception L_EX)
		{
			setMSG(L_EX,"nvlNUMDB");
		}
		return LP_RETVL;
	}
	/**
	 * Method to convert String into integer(premitive)
	 * 
	 * Method to convert String into integer(premitive)
	 * 
	 * @param LP_VARCL String to be converted to number
	 * @return 'Zero' if string is not a number otherwise, integer(premitive) value of the string
	 */
	protected int nvlNUMIN(String LP_VARVL){
		int LP_RETVL = 0;
		try{
			if(LP_VARVL != null)
				LP_RETVL = Integer.parseInt(LP_VARVL);
		}catch (Exception L_EX){
			setMSG(L_EX,"nvlNUMIN");
		}
		return LP_RETVL;
	}
	/**
	 * Method to convert String into float
	 * 
	 * Method to convert String into float
	 * 
	 * @param LP_VARCL String to be converted to number
	 * @return 'Zero' if string is not a number otherwise, float value of the string
	 */
	protected float nvlNUMFL(String LP_VARVL){
		float LP_RETVL = 0;
		try{
			if(LP_VARVL != null)
				LP_RETVL = Float.parseFloat(LP_VARVL);
		}catch (Exception L_EX){
			setMSG(L_EX,"nvlNUMFL");
		}
		return LP_RETVL;
	}
    protected void exeRPTPRN(String LP_RPTNM){
		String LM_RESFIN = cl_dat.M_strREPSTR_pbst;
        String LM_RESSTR = "co_rpprn.bat "+LM_RESFIN.trim().concat("\\"+LP_RPTNM);
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try{
            p  = r.exec(LM_RESSTR);
           }catch(IOException L_EX){
           System.out.println("Error.exescrn.......... "+L_EX);
             }
        }
	/**Checks ifthe said directory exists otherwise, creates new one with absolute path
	 */
	protected void chkDIR(String LP_STRNM)
	{
		File ofile = new File(LP_STRNM);
		if(!ofile.exists());
		{
			setMSG("Directory does not exist,it will be created",'N');
			ofile.mkdir();
		}
	}
	public void windowActivated(WindowEvent w){updateUI();repaint();System.out.println("Act");}
	public void windowDeactivated(WindowEvent w){System.out.println("deact");}
	public void windowClosing(WindowEvent w){System.out.println("clo");}
	public void windowClosed(WindowEvent w){System.out.println("clsd");}
	public void windowDeiconified(WindowEvent w){updateUI();}
	public void windowOpened(WindowEvent w){System.out.println("open");}
	public void windowIconified(WindowEvent w){}
	/**Method to set state of all components on the screen. <b>This method is valid only if components are layed using customised lay-out manager</b>
	 * @param L_STAT State to be set for all compenents (true or false)
	 * <br>To give additional functionality, over-ride in child classes by giving call to parent method using "super.setENBL()" as first line of code.
	 *
	 */
	void setENBL(boolean L_STAT)
	{
		for(int i=0;i<M_vtrSCCOMP.size();i++)
		{
			Object obj=M_vtrSCCOMP.get(i);
			if((!(obj.getClass().toString().equals("class javax.swing.JLabel")))&&(!(obj.getClass().toString().equals("class javax.swing.JTabbedPane"))))
				((Component)obj).setEnabled(L_STAT);
		}
	}
	/**Method to clear contents of all components on the screen. <b>This method is valid only if components are layed using customised lay-out manager</b>
	 * <br>To give additional functionality, over-ride in child classes by giving call to parent method using "super.clrCOMP()" as first line of code.
	 */
	void clrCOMP()
	{
		cl_dat.M_txtUSER_pbst.setText(cl_dat.M_strUSRCD_pbst);
		cl_dat.M_txtDATE_pbst.setText((cl_dat.M_strLOGDT_pbst));
		String L_STRTMP="";
		for(int i=0;i<M_vtrSCCOMP.size();i++)
		{
			Object component=M_vtrSCCOMP.get(i);
			((Component)component).removeFocusListener(this);
			L_STRTMP=component.getClass().toString();
			if(component instanceof JTextField)
			{
				((JTextField)component).setText("");
			}
			else if(component instanceof AbstractButton)
			{
				((AbstractButton)component).setSelected(false);
			}
			else if(L_STRTMP.equals("class javax.swing.JScrollPane"))
			{//MODIFIED ON 12/05/04 BY AAP TO REMOVE CLASSCAST EXCEPTION IN CASE OF TEXT AREA
				try
				{
					Component L_cmpTEMP=((JScrollPane)component).getViewport().getView();
					if(L_cmpTEMP instanceof JTable)
					{
						if(L_cmpTEMP!=null)
						{
							if(L_cmpTEMP instanceof cl_JTBL)
								((cl_JTBL)L_cmpTEMP).clrTABLE();
							else
								((cl_JTable)L_cmpTEMP).clrTABLE();
						}
					}
					else if(L_cmpTEMP instanceof JTextArea)
					{
						((JTextArea)L_cmpTEMP).setText("");
					}
					else if(L_cmpTEMP instanceof JList)
					{
						//NO ACTION IN CASE OF JLIST
					}
					
				
				}catch(ClassCastException e)
				{
					setMSG(e,"cl_pbase.clrCOMP : \nInvalid component detected in JScrollPane\n");
				}
			}
			else if(L_STRTMP.equals("class javax.swing.JTabbedPane"))
			{
				((JTabbedPane)component).setSelectedIndex(0);
			}
			else if(L_STRTMP.equals("class javax.swing.JComboBox"))
			{
				if(((JComboBox)component).getItemCount()>0)
				{
					((JComboBox)component).removeActionListener(this);
					((JComboBox)component).removeItemListener(this);
					((JComboBox)component).setSelectedIndex(0);
					((JComboBox)component).addItemListener(this);
					((JComboBox)component).addActionListener(this);
				}
			}
			((Component)component).addFocusListener(this);
		}
	}
	/** To change lay-out Matrix. Call to this method will not change lay-out of components added earlier.
	 * @param L_ROWS Number of rows
	 * @param L_COLS Number of columns
	 * <br><br><hr><b>Usage :</B><br>&nbsp&nbsp&nbsp&nbsp setMatrix(20,4) will devide the screen into 20x4=80 cells with current horizontal and vertical gaps in between.
	 */
	protected void setMatrix(int L_ROWS,int L_COLS)
	{
		M_intROWCT=L_ROWS;
		M_intCOLCT=L_COLS;
		M_intVSTRT=new Double(10*M_dblHIGHT).intValue();
		M_intVRGAP=new Double(M_intVRGAP_st*M_dblHIGHT).intValue();
		M_intHRGAP=new Double(M_intHRGAP_st*M_dblWIDTH).intValue();
		M_intCOLWD=(cl_dat.M_dimSCRN_pbst.width-M_intHRGAP*L_COLS-new Double(20*M_dblWIDTH).intValue())/L_COLS;
//		M_intROWHT=(cl_dat.M_dimSCRN_pbst.height-M_intVRGAP*L_ROWS-new Double((65+10)*M_dblHIGHT).intValue())/L_ROWS;
			M_intROWHT=new Double((cl_dat.M_dimSCRN_pbst.height-M_intVRGAP*L_ROWS-((65+10+6)*M_dblHIGHT))/L_ROWS).intValue();
	}
	/**Sets gap between two rows
	 * @param L_HGAP Gap between two rows in pixels
	 */
	protected void setVGAP(int L_VGAP)
	{
		M_intVRGAP_st=L_VGAP;
		setMatrix(M_intROWCT,M_intCOLCT);
	}
	/**Sets gap between two columns
	 * @param L_HGAP Gap between two columns in pixels
	 */
	protected void setHGAP(int L_HGAP)
	{
		M_intHRGAP_st=L_HGAP;
		setMatrix(M_intROWCT,M_intCOLCT);
	}
	/**Sets Horizontal as well as Vertical gaps to default
	 */
	protected void setDefaultGap()
	{
		M_intHRGAP_st=8;
		M_intVRGAP_st=5;
		setMatrix(M_intROWCT,M_intCOLCT);
	}
	/**
	 * Method to lay component by using customised lay-out manager
	 * 
	 * Method to lay component by using customised lay-out manager
	 * 
	 * @param c Component to be added
	 * @param row Row index at which Component is to be added
	 * @param col Column index at which component is to be added
	 * @param weightx Number of rows component should occupy
	 * @param p1 Parent Container
	 * @param Aline Allignment 'L' : Left; 'R' : Right;
	 * <br><br><hr><b>Usage :</b><br>&nbsp&nbsp&nbsp&nbsp&nbsp add(MyCompoment,1,2,3,4,MyContainer,'L');<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp Will Lay MyComponent in Container MyContainer at second column in first row, <br>&nbsp&nbsp&nbsp&nbsp&nbspLeft alligned and size ofthe component will be 3 rows high and 4 columns wide
	 */
	protected void add(Component c,int row,int col,double weighty,double weightx,Container p1,char Aline)
	{
		int width=0,height=0;
		width=((weightx>1) ? (new Double((M_intCOLWD*weightx)+(M_intHRGAP)*(weightx-1)).intValue()) : (new Double(M_intCOLWD*weightx).intValue()));
		height=((weighty>1) ? (new Double((M_intROWHT+M_intVRGAP)*weighty).intValue()) : (new Double(M_intROWHT*weighty).intValue()));
		if(Aline=='L'||Aline=='l')
		{
			if(p1==this)
				c.setBounds((col-1)*(M_intCOLWD+M_intHRGAP)+10,(row-1)*(M_intVRGAP+M_intROWHT)+M_intVSTRT,width,height);
			else
				c.setBounds((col-1)*(M_intCOLWD+M_intHRGAP)+3,(row-1)*(M_intVRGAP+M_intROWHT)+M_intVSTRT*2,width,height);
		}
		else
		{
			if(p1==this)
				c.setBounds(new Double((col-1)*(M_intCOLWD+M_intHRGAP)+10+(1-weightx)*M_intCOLWD).intValue(),(row-1)*(M_intVRGAP+M_intROWHT)+M_intVSTRT,width,height);
			else
				c.setBounds(new Double((col-1)*(M_intCOLWD+M_intHRGAP)+5+(1-weightx)*M_intCOLWD).intValue(),(row-1)*(M_intVRGAP+M_intROWHT)+M_intVSTRT*2,width-M_intHRGAP,height);
		}
		c.addKeyListener(this);
		p1.add(c);
		String L_STRTMP=c.getClass().toString();
/*		if(L_STRTMP.equals("class TxtLimit")
		   ||L_STRTMP.equals("class TxtNumLimit")
		   ||L_STRTMP.equals("class TxtNonNumLimit")
		   ||L_STRTMP.equals("class TxtNum")
		   ||L_STRTMP.equals("class TxtDate")
		   ||L_STRTMP.equals("class javax.swing.JTextField")
		   ||L_STRTMP.equals("class TxtNonNum"))
*/
		if(c instanceof JTextField)
		{
			((JTextField)c).addActionListener(this);
			((JTextField)c).addFocusListener(this);
		}
/*		else if(L_STRTMP.equals("class javax.swing.JButton")
			||L_STRTMP.equals("class javax.swing.JCheckBox")
			||L_STRTMP.equals("class javax.swing.JRadioButton"))
*/
		else if(c instanceof AbstractButton)
		{
			((AbstractButton)c).addActionListener(this);
			((AbstractButton)c).addFocusListener(this);
		}
		else if(L_STRTMP.equals("class javax.swing.JComboBox"))
		{
			((JComboBox)c).addActionListener(this);
			c.addFocusListener(this);
		}
		M_vtrSCCOMP.add(c);
	}
	/** Method to Print Report. called by default To be overridden in child classes
	 * <br> If not Over-ridden, gives error message by using {@link cl_pbase#setMSG(String P_strMESSG,char P_chrMSGTP)}*/
	void exePRINT()
	{
		setMSG("Method exePRINT not defined in child class",'E');
	}
	/** Method to save/update/delete data. called by default To be overridden in child classes
	 * <br> If not Over-ridden, gives error message by using {@link cl_pbase#setMSG(String P_strMESSG,char P_chrMSGTP)}*/
	void exeSAVE()
	{
		setMSG("Method exeSAVE not defined in child class",'E');
	}
/**Method to validate data. Empty definition given. to be over-ridden in child classes
 */
	boolean vldDATA()
	{
		setMSG("Method vldDATA not defined in child class",'E');
		return false;
	}
	//To be removed after SimpleDateFormat is used
	protected String fmtDAT(java.util.Date d)
	{
		String s1=d.toString();
		StringTokenizer st1=new StringTokenizer(s1,"-");
		String dd[]=new String[3];
		int i=0;
		while(st1.hasMoreTokens())
		{
			dd[i++]=st1.nextToken();
		}
		return ((dd[2]+"/"+dd[1]+"/"+dd[0]));
	}
	/**
	 * Similar to method substring() in java package
	 * 
	 * Similar to method substring() in java package
	 * 
	 * <p>Eliminates StringIndexOutOfBoundsException.
	 * 
	 * @param P_strSTRVL String to be manpulated
	 * @param LP_FRPOS From position
	 * @param LP_TOPOS To position
	 * @return SubString of P_strSTRVL from LP_FMPOS to LP_TOPOS posisiton, zero based indexing
	 */
	protected String getSUBSTR(String P_strSTRVL,int LP_FRPOS,int LP_TOPOS){
		try{
			if (P_strSTRVL.length() == 0)
				P_strSTRVL =  "";
			else if(P_strSTRVL.length() <= LP_FRPOS)		// Added on 24.12.2002
				P_strSTRVL =  "";
			else if(P_strSTRVL.length() >= LP_TOPOS)
				P_strSTRVL = P_strSTRVL.substring(LP_FRPOS,LP_TOPOS).trim();
			else
				P_strSTRVL = P_strSTRVL.substring(LP_FRPOS,P_strSTRVL.length()).trim();
		}catch (Exception L_EX){
			setMSG(L_EX,"getSUBSTR");
		}
		return P_strSTRVL;
	}
	/**
	 * Method to add SubSystem Combo's at the top
	 * 
	 * Method to add SubSystem Combo's at the top
	 * 
	 * <p> Called from constructor
	 * throws IllegalAurgumentException, if Aurgument is other thatn 1,2 or 3
	 * Creates Combos, adds Item/Key listeners and lays on screen
	 * 
	 * @param L_SBSLVL Number of Combos to be added
	 * 
	 */
	void setSBSCD(int L_SBSLVL)
	{
		try{
			switch (L_SBSLVL)//L_SBSLVL : TO BE PASSED AS PARAMETER DURING CALL TO SUPER CONSTRUCTOR
		{
		case 3 : //THREE LEVELS ARE AVAILABLE FOR SBS CODE
			{
				cl_dat.M_cmbSBSL3_pbst=new JComboBox();
				cl_dat.M_cmbSBSL3_pbst.setForeground(new Color(63,91,167));
				cl_dat.M_pnlSBSCD_pbst.add(cl_dat.M_cmbSBSL3_pbst);
				cl_dat.M_cmbSBSL3_pbst.setBounds(new Double(5*cl_dat.M_dblWIDTH).intValue()+2*(cl_dat.M_dimSCRN_pbst.width/3),2,cl_dat.M_dimSCRN_pbst.width/3-5,new Double(25*cl_dat.M_dblWIDTH).intValue());
				cl_dat.M_cmbSBSL3_pbst.addItemListener(this);cl_dat.M_cmbSBSL3_pbst.addKeyListener(this);
				cl_dat.M_cmbSBSL3_pbst.setAlignmentX(0.5f);
			}
		case 2 : //TWO LEVELS ARE VAILABLE FOR SBS CODE
			{
				cl_dat.M_cmbSBSL2_pbst=new JComboBox();
				cl_dat.M_cmbSBSL2_pbst.setForeground(new Color(63,91,167));
				cl_dat.M_pnlSBSCD_pbst.add(cl_dat.M_cmbSBSL2_pbst);
				cl_dat.M_cmbSBSL2_pbst.setBounds(new Double(5*cl_dat.M_dblWIDTH).intValue()+cl_dat.M_dimSCRN_pbst.width/3,2,cl_dat.M_dimSCRN_pbst.width/3-5,new Double(21*cl_dat.M_dblWIDTH).intValue());
				cl_dat.M_cmbSBSL2_pbst.addItemListener(this);cl_dat.M_cmbSBSL2_pbst.addKeyListener(this);
			}
		case 1 : //ONLY ONE LEVEL IS AVAILABLE FOR SBS CODE
			{
				cl_dat.M_cmbSBSL1_pbst=new JComboBox();
				cl_dat.M_cmbSBSL1_pbst.setForeground(new Color(63,91,167));
				cl_dat.M_pnlSBSCD_pbst.add(cl_dat.M_cmbSBSL1_pbst);
				cl_dat.M_cmbSBSL1_pbst.setBounds(new Double(5*cl_dat.M_dblWIDTH).intValue(),2,cl_dat.M_dimSCRN_pbst.width/3-5,new Double(21*cl_dat.M_dblWIDTH).intValue());
				cl_dat.M_cmbSBSL1_pbst.addItemListener(this);cl_dat.M_cmbSBSL1_pbst.addKeyListener(this);
				cl_dat.M_pnlSBSCD_pbst.revalidate();
				updateUI();
				break;
			}
		default : //WRING AURGUMENT FOUND
			{
				throw (new Exception("Illegal Aurgument .. VALUE SHOULD BE 1 OR 2 OR 3"));
			}
		}
		}catch(Exception e)
		{
			System.out.println("Error in setSBSCD : "+e);
		}
	}
	/**
	 * To set user rigths in the current screen
	 * 
	 * To set user rigths in the current screen
	 * 
	 * <p>This method is called only once from cl_mnu. <br> This will populate the SusSystem Combo Boxes According to String array passed as argument
	 * <br> If Subsystem Combo's are not available, M_cmbOPTN_pbst will be populated.
	 * 
	 * @param LP_USRRT Two dimentional String array giving details of access rights.
	 */
	public void setUSRRT(String[][] LP_USRRT)
	{
		//PPR_PRGCD : PPR_SYSCD,PPR_SBSCD,CMT_SHRDS,CMT_CHP01,CMT_CHP02,PPR_USRTP,PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,PP_PRGDS,SP_SYSNM,PP_PRGTP,PP_VERNO,SP_SYSLC,SP_DTBLB,SP_YSTDT,SP_YENDT
		try
		{
			if(cl_dat.M_cmbSBSL1_pbst!=null)
			{
				M_staUSRRT=LP_USRRT;
				cl_dat.M_cmbSBSL1_pbst.removeAllItems();
				cl_dat.M_cmbSBSL1_pbst.addItem("Select");
				for(int i=0;i<LP_USRRT.length&&LP_USRRT[i][0]!=null;i++)
				{//SCANNING SBS RIGHTS
					if(LP_USRRT[i][1]!=null&&cl_dat.M_cmbSBSL1_pbst!=null)
					{
						boolean flag=true;
						for(int z=0;z<cl_dat.M_cmbSBSL1_pbst.getItemCount();z++)
							if(LP_USRRT[i][1].equals(cl_dat.M_cmbSBSL1_pbst.getItemAt(z).toString()))
							{//CHECKING IF ITEM IS ALREADY PRESENT IN LEVEL - 1 COMBO
								flag=false;
								break;
							}
						if(flag)
						{//ADDING ITEM TO LEVEL - 1 COMBO
							cl_dat.M_cmbSBSL1_pbst.addItem(LP_USRRT[i][1]);
						}
					}
				}//ALL ITEMS ADDED IN CMB1
				if(cl_dat.M_cmbSBSL1_pbst.getItemCount()==2)
				{
					cl_dat.M_cmbSBSL1_pbst.setSelectedIndex(1);
					if(cl_dat.M_cmbSBSL2_pbst==null)
					{//SHOW CMBOPTN
						cl_dat.M_cmbOPTN_pbst.requestFocus();
						cl_dat.M_cmbOPTN_pbst.showPopup();
					}
					else if(cl_dat.M_cmbSBSL2_pbst.getItemCount()>2)
					{//SHOW LVL 2 COMBO
						cl_dat.M_cmbSBSL2_pbst.requestFocus();
						cl_dat.M_cmbSBSL2_pbst.showPopup();
					}
					else
					{
						cl_dat.M_cmbOPTN_pbst.requestFocus();
						cl_dat.M_cmbOPTN_pbst.showPopup();
					}
				}
				else
				{//SHOW LEVEL - 1 COMBO
					cl_dat.M_cmbSBSL1_pbst.requestFocus();
					cl_dat.M_cmbSBSL1_pbst.showPopup();
				}
			}
			else
			{//FOR SYSTEM WITHOUT SBS CODES. NOT TO BE USED. CODED FOR FUTURE MODIFICATION, IF REQUIRED
				cl_dat.M_cmbOPTN_pbst.removeAllItems();
				M_strSBSCD=M_staUSRRT[0][0].substring(2);
				if(cl_dat.M_btnSAVE_pbst.getText().equals("Run"))
				{
					cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPRSL_pbst);
					cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPFIL_pbst);
					cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPEML_pbst);
					cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPSCN_pbst);
				}
				else
				{
					cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPSEL_pbst);
					if(LP_USRRT[0][4].equals("Y"))
						cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPADD_pbst);
					if(LP_USRRT[0][5].equals("Y"))
						cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPMOD_pbst);
					if(LP_USRRT[0][6].equals("Y"))
						cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPDEL_pbst);
					if(LP_USRRT[0][7].equals("Y"))
						cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPENQ_pbst);
					//ADDED ON 02/04/2004 BY AAP FOR AUTHORISATION OPTION
					if(LP_USRRT[0][8].equals("Y"))
						cl_dat.M_cmbOPTN_pbst.addItem(cl_dat.M_OPAUT_pbst);
				}
				cl_dat.M_cmbOPTN_pbst.requestFocus();
			}
		}catch(Exception e)
		{System.out.println("Error in setUSRRT : "+e);}
	}
	//METHOD DHIFTED FROM cc_dattm DURING REV. Aug'2003
	/**
	 * Method to calculate the difference between two timestamps
	 * 
	 * Method to calculate the difference between two timestamps
	 * 
	 * <p>If First value is greater than second, +ve is returned. Else, difference is termed as -ve.
	 * <br>If Time part is ommited, "00:00" is appended and then differnece is calculated.
	 * <br>If Date part is ommited, "01/01/2000" is prepended and then differnece is calculated.
	 * 
	 * @param LP_FINTM Final Timestamp in "dd/MM/yyyy HH:mm" OR "dd/MM/yyyy" OR "HH:mm"
	 * @param LP_INITM Initial Timestamp in "dd/MM/yyyy HH:mm" OR "dd/MM/yyyy" OR "HH:mm"
	 * @return String in format "d:H:M"
	 * 
	 * <p><br><b>Exception</b> : "Aurgument format mismatch" if length of two strings are not equal.<br>
	 * <hr>
	 * <b>Usage :</B><br>&nbsp&nbsp&nbsp&nbsp&nbsp Call : calTIME("01/02/2003 10:00","01/01/2003 05:00")
	  <br> &nbsp&nbsp&nbsp&nbsp&nbsp Return : "31:5:0"<br>
	  <br>&nbsp&nbsp&nbsp&nbsp&nbsp Call : calTIME("01/01/2003 05:30","01/02/2003 10:00")
	  <br> &nbsp&nbsp&nbsp&nbsp&nbsp Return : "-31:4:30"<br>
	  <br>&nbsp&nbsp&nbsp&nbsp&nbsp Call : calTIME("01/01/2003","01/02/2003")
	  <br> &nbsp&nbsp&nbsp&nbsp&nbsp Return : "-31:0:0"<br>
	  <br>&nbsp&nbsp&nbsp&nbsp&nbsp Call : calTIME("12:00","01:00")
	  <br> &nbsp&nbsp&nbsp&nbsp&nbsp Return : "0:11:0"
	 */
	protected String calTIME(String LP_FINTM,String LP_INITM){
		String L_RTSTR = "";
		try{
			if(LP_FINTM.length()!=LP_INITM.length())
				throw (new Exception("Aurgument Format Mismatch"));
			if(LP_FINTM.length()==10)//Convert Aurguments from Date to Timestamp.
			{
				LP_FINTM=LP_FINTM+" 00:00";
				LP_INITM=LP_INITM+" 00:00";
			}
			if(LP_FINTM.length()==5)//Convert Aurguments from Time to Timestamp.
			{
				LP_FINTM="01/01/2000 "+LP_FINTM;
				LP_INITM="01/01/2000 "+LP_INITM;
			}
			M_calLOCAL.setTime(M_fmtLCDTM.parse(LP_FINTM));
			long L_FINLG=M_calLOCAL.getTimeInMillis();
			M_calLOCAL.setTime(M_fmtLCDTM.parse(LP_INITM));
			long L_INILG=M_calLOCAL.getTimeInMillis();
			long L_DAYS=0l,L_HRS=0l,L_MIN=0l;
			L_DAYS=(L_FINLG-L_INILG)/(24l*60l*60l*1000l);
			L_HRS=((L_FINLG-L_INILG)/(60l*60l*1000l))-L_DAYS*24l;
			L_MIN=((L_FINLG-L_INILG)/(60l*1000l))-(L_DAYS*24l+L_HRS)*60l;
			if(L_DAYS<0l||L_HRS<0l||L_MIN<0l)
				L_RTSTR="-";
			L_RTSTR+=Long.toString(Math.abs(L_DAYS))+":"+Long.toString(Math.abs(L_HRS))+":"+Long.toString(Math.abs(L_MIN));
			return(L_RTSTR);
	}catch(Exception e){
			setMSG(e,"calTIME");
		}
		return L_RTSTR;
	}
		/** 
		 * Update Usage of current form in current session of user, including operations carried out by the user
		 * 
		 * <P>Called after btnEIXT is pressed
		 */
	private void updUSG()
	{
		try
		{
			M_strSQLQRY="UPDATE SA_PPUTR SET "
				+" PPU_LGOTM=current_timestamp, "
				+"PPU_ADDFL="+strFLADD
				+",PPU_MODFL="+strFLMOD
				+",PPU_DELFL="+strFLDEL
				+",PPU_ENQFL="+strFLENQ
				+",PPU_RPTFL="+strFLRPT
				+" WHERE "
				+" PPU_PRGCD='"+M_strPRGCD_fr+"' AND "
				+" PPU_USGBY='"+cl_dat.M_strUSRCD_pbst+"' AND "
				+" PPU_USGNO= (select max(PPU_USGNO) from sa_pputr where PPU_PRGCD='"+M_strPRGCD_fr+"' AND PPU_USGBY='"+cl_dat.M_strUSRCD_pbst+"')";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			if(!cl_dat.exeDBCMT("updUSG"))
				setMSG("Error in updating usage",'E');
		}catch (Exception e)
		{
			System.out.println("Error in updUSG : "+e);
			setMSG("Error in updating usage",'E');
		}
	}
	/**
	 * Method to set Number format
	 * 
	 * Method to set Number format
	 * 
	 * <p>This method makes use of NumberFormat class defined by Java. This will round up the additional digits (if present)<br>
	 * By default, java format includes commas in string representation, which is removed by using stringTokenizer
	 * 
	 * @param P_dblNUMBR Number to be formatted. Being the highest capacity primitive type of number, inmplicite casting takes place if aurgument is int or float
	 * @param P_intFRCTN Number of digits required after formatting. <br>Can be zeero to convert a number into integer (primitive).
	 * 
	 * <hr>
	 * <p><b>USAGE : </B>
	 * setNumberFormat(1.123456,5) returns : 1.12346<br>
	 * setNumberFormat(1.123456,4) returns : 1.1235<br>
	 * setNumberFormat(1.123456,3) returns : 1.123<br>
	 * setNumberFormat(1.1,0) returns : 1
	 * setNumberFormat(1.6,0) returns : 2
	 */
	protected String setNumberFormat(double P_dblNUMBR,int P_intFRCTN)
	{
		NumberFormat L_fmtNUMFM=NumberFormat.getNumberInstance();
		L_fmtNUMFM.setMaximumFractionDigits(P_intFRCTN);
		L_fmtNUMFM.setMinimumFractionDigits(P_intFRCTN);
		String L_strTEMP=(L_fmtNUMFM.format(P_dblNUMBR));
		StringTokenizer L_stkSTTKN=new StringTokenizer(L_strTEMP,",");
		L_strTEMP="";
		while(L_stkSTTKN.hasMoreTokens())
		{
			L_strTEMP+=L_stkSTTKN.nextToken();
		}
		return L_strTEMP;
	}


	/**
	 * Constructs part of string for TRNFL,STSFL,LUSBY,LUPDT columns value updation.
	 */
	protected String getUSGDTL(String P_strTBLNM,char P_chrQRYTP,String P_strSTSFL)
	{
		try
		{
			if(P_chrQRYTP=='I'||P_chrQRYTP=='i')
			{
				String L_strRTNVL="";
				if(P_strSTSFL==null)
					P_strSTSFL="";
				L_strRTNVL="'0','"//TRNFL    Varchar(1),
					+P_strSTSFL+"',"//STSFL    Varchar(1),
					+"'"+cl_dat.M_strUSRCD_pbst+"',"//LUSBY    Varchar(3),
					+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
				return L_strRTNVL;
			}
			else if(P_chrQRYTP=='U'||P_chrQRYTP=='u')
			{
				String L_strRTNVL="";
				
				if(P_strSTSFL==null)
				{
					L_strRTNVL=P_strTBLNM+"_TRNFL='"+"0"+"',"//TRNFL    Varchar(1),
						+P_strTBLNM+"_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//LUSBY    Varchar(3),
						+P_strTBLNM+"_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
				}	
				else
				{
					L_strRTNVL=P_strTBLNM+"_TRNFL='"+"0"+"',"//TRNFL    Varchar(1),
						+P_strTBLNM+"_STSFL='"+P_strSTSFL+"',"//STSFL    Varchar(1),
						+P_strTBLNM+"_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//LUSBY    Varchar(3),
						+P_strTBLNM+"_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
				}
				return L_strRTNVL;
			}
		}catch(Exception e)
		{
			setMSG(e,"getUSGDTL");
			return null;
		}
		return null;
	}
}