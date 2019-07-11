import java.util.Calendar;import java.util.Vector;import java.util.StringTokenizer;import java.util.Hashtable;import java.util.Enumeration;
import javax.swing.event.MenuEvent;import java.io.File;
import javax.swing.event.MenuListener;
import java.awt.Color;import java.awt.Font;import java.awt.Toolkit;import java.awt.CardLayout;import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;import java.sql.DriverManager;import java.sql.SQLWarning;import java.sql.ResultSet;
import javax.swing.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.awt.*;

/**
 * This class creates a basic frame with login screen for the application.
 * Individual forms will be added on to it as JPanels.
 * Also, basic functional buttons and status bar are created here.
 * @author AAP
 * @version 2.0
 * Latest Revision details :
 *		Conversion from java1.2 to java1.4.1
 */
public class fr_log extends JFrame implements ActionListener,FocusListener,ItemListener,KeyListener,MouseListener,SwingConstants,Runnable,MenuListener
{
	/** Refernce to current form being displayed (other than login or set password screen)	 */
	private cl_pbase ocl_pbase;/**Thread for updating hit count in database*/
	private Thread thrHITCNT;/**Thread for DB Connection*/
	private Thread thrCONNECT;/** For DB location to which u r currently connected	 */
	//private Thread thrCMPCD;/** For Company code to be inserted in combo box	 */
	//private String strCURLC;
	private static String strRPDIR=""	;
	private JLabel lblMODUL;/** Program code of current form */
	private String strPRGCD;/** Program Type of current form */
	private String strPRGTP;/** DataBase details     */
    private static String	strEXEDRV_st = "",
							//cl_dat.M_strUSRCT_pbst  = "",
							//strSYSLC_st  = "01",
							//strSYSLC0_st = "01",
							strCONTP_st = "";
							/** For getting the printer list	 */
	private String	M_strSPLBA_pb_st = "",
					M_strSPUSA_pb_st = "",
					M_strSPPWA_pb_st = "",
					M_strSPDNA_pb_st = "",
					M_strSPDPA_pb_st = "";/** Global Miscellaneous String variable */
	private String strACTCMD,strSQLQRY,strSYSCD;/** For User - Rights hashtable String array indexing */
	private final int	intSYSCD_fn=0,//SYSCD
								intPRGDS_fn=1,//PROGRAM DESCRIPTION
								intSYSNM_fn=2,//SYSTEM NAME
								intPRGTP_fn=3,//PROGRAM TYPE
								intVERNO_fn=4,//VERSION NO.
								intPRGLC_fn=8;//DB LOCATION FOR THE PROGRAM
	/**Variable for Visible menulength	 */
	private final int intMNULN_fn=20;/**Counter for failed login attempts	 */
	private int intLGCNT_fn;/** Vector for SBS access rights details	 */
	private Vector<String>	vtrPRGCD,vtrSBSCD,vtrSHRDS,vtrCHP01,vtrCHP02,vtrADDFL,vtrMODFL,vtrDELFL,vtrENQFL,vtrAUTFL;/** Hashtable for form details	 */
	private Hashtable<String,String[]> hstUSRRT;/** Result set object	 */
	private ResultSet rstRSSET;/** Standard application, menu bar*/
	private JMenuBar mbrMNUBR;/** Main menu	 */
	
	private JMenu mnuTEFG;
	private JMenu mnuTEHR;
	private JMenu mnuTEAD;
	private JMenu mnuTEPM;
	private JMenu mnuTEMR;
	private JMenu mnuTECO;
	private JMenu mnuTEMM;
	private JMenu mnuTEPR;
	private JMenu mnuTEQC;
	private JMenu mnuTESA;
	

	private JMenu mnuMEFG;
	private JMenu mnuMEHR;
	private JMenu mnuMEAD;
	private JMenu mnuMEPM;
	private JMenu mnuMEMR;
	private JMenu mnuMECO;
	private JMenu mnuMEMM;
	private JMenu mnuMEPR;
	private JMenu mnuMEQC;
	private JMenu mnuMESA;
	

	
	private JMenu mnuRPFG;
	private JMenu mnuRPHR;
	private JMenu mnuRPAD;
	private JMenu mnuRPPM;
	private JMenu mnuRPMR;
	private JMenu mnuRPCO;
	private JMenu mnuRPMM;
	private JMenu mnuRPPR;
	private JMenu mnuRPQC;
	private JMenu mnuRPSA;
	
	
	private JMenu mnuQRFG;
	private JMenu mnuQRHR;
	private JMenu mnuQRAD;
	private JMenu mnuQRPM;
	private JMenu mnuQRMR;
	private JMenu mnuQRCO;
	private JMenu mnuQRMM;
	private JMenu mnuQRPR;
	private JMenu mnuQRQC;
	private JMenu mnuQRSA;

	
	private JMenu mnuHKFG;
	private JMenu mnuHKHR;
	private JMenu mnuHKAD;
	private JMenu mnuHKPM;
	private JMenu mnuHKMR;
	private JMenu mnuHKCO;
	private JMenu mnuHKMM;
	private JMenu mnuHKPR;
	private JMenu mnuHKQC;
	private JMenu mnuHKSA;
	
	
	
	private JMenu mnuTMRSRC,mnuMASTR,mnuTRANS,mnuREPRT,mnuHSKPG,mnuQUERY,mnuEXIT;/**Menus to be used as separators in main menu	 */

	private JMenu mnuSEPTR1,mnuSEPTR2,mnuSEPTR3,mnuSEPTR4,mnuSEPTR5;/**Menuitem for scrolling effect	 */
	private JMenuItem mitTMRSRC,mitNWITM,mitMSTUP,mitMSTDN,mitTRNUP,mitTRNDN,mitRPTUP,mitRPTUP1,mitRPTDN,mitRPTDN1,mitOTHUP,mitOTHDN,mitHSKUP,mitHSKDN;/**Font of "login" Text in login screen	 */
	private Font fntLOGIN = new Font("Arial",Font.BOLD,20);/** SPLLOGO image	 */
	private Image imgSPL;/** SPLLOGO image icon	 */
	private ImageIcon imiSPL;/** Menu panel	 */
	private JPanel pnlMENU=new JPanel(null);/** Timer for menu scrolling	 */
	private javax.swing.Timer tmrMNUUP,tmrMNUDN;
	private java.util.Timer tmrCLOCK;/** Current DB date	 */
	private java.util.Date datCURDT;/** Locale Timestamp format	 */
	private SimpleDateFormat	fmtLCLTM=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");/** DB Timestamp format	 */
	private SimpleDateFormat	fmtDBSTM=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");/** Global calendar object	 */
	private Calendar calCLOCK=Calendar.getInstance();
	private JButton btnINTRN;
//	private Thread thrTEMP;
	/**
	 * Constructs the object<br>Uses separate thread for DB connection. <BR>Creates empty menu
	 */
	fr_log()
	{
		try
		{
			//CHECKING FOR WINDOWS LOG IN
			File ofile = new File(strEXEDRV_st.substring(0,1)+":\\exec\\splerp2");
			//System.out.println("strEXEDRV_st : "+strEXEDRV_st.substring(0,1)+":\\exec\\splerp2");
			/*if(!ofile.exists())//
			{
				JOptionPane.showMessageDialog(this, "Error : Not Logged in to Windows\n Application will terminate", "Error in start-up", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}*/
			//CHECKING FOR REPORTS DIRECTORY
			if(strRPDIR.length() >= 2)
			{
				cl_dat.M_strREPSTR_pbst = strRPDIR+"\\reports\\";
				ofile = new File(cl_dat.M_strREPSTR_pbst);
			}
			else
			{
				cl_dat.M_strREPSTR_pbst = "c:\\reports\\";
				ofile = new File(cl_dat.M_strREPSTR_pbst);
			}
			//System.out.println(cl_dat.M_strREPSTR_pbst);
			if(!ofile.exists());
			{
				ofile.mkdir();
			}
			
		cl_dat.M_dimSCRN_pbst = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(cl_dat.M_dimSCRN_pbst.width,cl_dat.M_dimSCRN_pbst.height);
		imgSPL = this.getToolkit().createImage("\\\\192.168.203.39\\user\\exec\\splerp3\\spllogo_old.gif");
		imgSPL=imgSPL.getScaledInstance(90,90,Image.SCALE_SMOOTH);
		//fferedImage imgSPL1=new BufferedImage(75, 80,BufferedImage.TYPE_INT_ARGB );
//int a=0;
//while(! (imgSPL1.getGraphics().drawImage(imgSPL, 0, 0, Color.lightGray, null)&&a++<100));
//imgSPL1.flush();
		this.setIconImage(imgSPL);
		//this.show();	deprecated in 1.6
		this.setVisible(true);
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		tmrMNUUP=new javax.swing.Timer(300,this);tmrMNUDN=new javax.swing.Timer(300,this);tmrCLOCK=new java.util.Timer();
		getContentPane().setLayout(null);
		cl_dat.M_dblWIDTH=(cl_dat.M_dimSCRN_pbst.width/800.00);
		cl_dat.M_dblHIGHT=((cl_dat.M_dimSCRN_pbst.height)/600.00);
		thrCONNECT=new Thread(this);//thread for db connection.
		thrCONNECT.start();
		cl_dat.M_thrPRNLS_pbst=new Thread(this);//thread for printer connection list.
		cl_dat.M_thrPRNLS_pbst.start();
	// ADDING TOP PANEL FOR BUTTONS ***********
		cl_dat.M_pnlFRTOP_pbst=new JPanel(null);
		JPanel p_int2=new JPanel(null);
		lblMODUL=new JLabel("");
		cl_dat.M_pnlFRTOP_pbst.add(lblMODUL);
		lblMODUL.setBounds(0,(new Double(3*cl_dat.M_dblWIDTH).intValue()),(new Double(176*cl_dat.M_dblWIDTH).intValue()),(new Double(28*cl_dat.M_dblHIGHT).intValue()));
		lblMODUL.setBorder(BorderFactory.createLineBorder(Color.gray,1));
	//*****CREATING TOP PANEL OF BUTTONS
		int LP_WID = 90;
		int LP_HGT = 22;
		int LP_XAXIS=180;
		int LP_YAXIS=3;
		cl_dat.M_cmbOPTN_pbst = crtCMB(p_int2,cl_dat.M_OPSEL_pbst,10,3,140,LP_HGT);
	  	cl_dat.M_cmbOPTN_pbst.addItemListener(this);
        cl_dat.M_cmbOPTN_pbst.addFocusListener(this);
        cl_dat.M_cmbOPTN_pbst.addKeyListener(this);
	    cl_dat.M_btnSAVE_pbst = crtBTN(p_int2,"Save",LP_XAXIS,LP_YAXIS,LP_WID,LP_HGT);
        cl_dat.M_btnUNDO_pbst = crtBTN(p_int2,"Undo",LP_XAXIS+LP_WID+15,LP_YAXIS,LP_WID,LP_HGT);
        cl_dat.M_btnHELP_pbst = crtBTN(p_int2,"Help",LP_XAXIS+2*LP_WID+30,LP_YAXIS,LP_WID,LP_HGT);
        cl_dat.M_btnEXIT_pbst = crtBTN(p_int2,"Exit",LP_XAXIS+3*LP_WID+45,LP_YAXIS,LP_WID,LP_HGT);
		cl_dat.M_cmbCMPCD_pbst.setActionCommand("cl_dat.M_cmbCMPCD_pbst");
		cl_dat.M_btnEXIT_pbst.setActionCommand("cl_dat.M_btnEXIT_pbst");
		cl_dat.M_btnSAVE_pbst.setActionCommand("cl_dat.M_btnSAVE_pbst");
		cl_dat.M_btnUNDO_pbst.setActionCommand("cl_dat.M_btnUNDO_pbst");
		cl_dat.M_btnHELP_pbst.setActionCommand("cl_dat.M_btnHELP_pbst");
		cl_dat.M_btnSAVE_pbst.setToolTipText(" Saves a Record ");
		cl_dat.M_btnUNDO_pbst.setToolTipText(" Reverts back without saving ");
		cl_dat.M_btnHELP_pbst.setToolTipText(" Help about the current Screen ");
		cl_dat.M_btnEXIT_pbst.setToolTipText(" Exits From the Current Screen "	);
		setENLEXT();
	//******* BUTTON PANEL CREATED
		p_int2.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
		cl_dat.M_pnlFRTOP_pbst.add(p_int2);
		p_int2.setBounds((new Double(180*cl_dat.M_dblWIDTH).intValue()),new Double(3*cl_dat.M_dblHIGHT).intValue(),cl_dat.M_dimSCRN_pbst.width-(new Double(175*cl_dat.M_dblWIDTH).intValue()),(new Double(28*cl_dat.M_dblHIGHT).intValue()));
		cl_dat.M_pnlFRTOP_pbst.setVisible(false);
		getContentPane().add(cl_dat.M_pnlFRTOP_pbst);
		cl_dat.M_pnlFRTOP_pbst.setBounds(0,(new Double( 28*cl_dat.M_dblHIGHT).intValue()),cl_dat.M_dimSCRN_pbst.width,(new Double(32*cl_dat.M_dblHIGHT).intValue()));
		getContentPane().add(pnlMENU);
		pnlMENU.setBounds(0,0,cl_dat.M_dimSCRN_pbst.width,new Double(25*cl_dat.M_dblHIGHT).intValue());
		pnlMENU.setVisible(true);
	//FORMING PANEL FOR SUBSYSTEM CODES
		cl_dat.M_pnlSBSCD_pbst=new JPanel(null);
		cl_dat.M_pnlSBSCD_pbst.setBorder(BorderFactory.createLineBorder(Color.black));
		cl_dat.M_pnlSBSCD_pbst.setBounds(0,0,cl_dat.M_dimSCRN_pbst.width,new Double(27*cl_dat.M_dblHIGHT).intValue());
		getContentPane().add(cl_dat.M_pnlSBSCD_pbst);
		cl_dat.M_pnlSBSCD_pbst.setVisible(false);
	//ADDING BOTTOM PANEL FOR MESSAGE ***************STATUSBAR
		JPanel main_btm=new JPanel(null);
		cl_dat.M_txtCLKDT_pbst=new JTextField();cl_dat.M_txtCLKTM_pbst=new JTextField();
		cl_dat.M_txtCLKDT_pbst=crtTXT(main_btm,0,800-200,18,100,18);
		cl_dat.M_txtCLKTM_pbst=crtTXT(main_btm,0,800-100,18,100,18);
		cl_dat.M_txtCLKDT_pbst.setForeground(Color.blue);cl_dat.M_txtCLKDT_pbst.setBackground(this.getBackground() );
		cl_dat.M_txtCLKTM_pbst.setForeground(Color.blue);cl_dat.M_txtCLKTM_pbst.setBackground(this.getBackground());
		cl_dat.M_txtCLKDT_pbst.setFont(new Font("Comic Sans MS",Font.PLAIN,12));
		cl_dat.M_txtCLKTM_pbst.setFont(new Font("Comic Sans MS",Font.PLAIN,12));
		cl_dat.M_txtCLKTM_pbst.setEditable(false);
		cl_dat.M_txtCLKDT_pbst.setEditable(false);
		JLabel lb_des=crtLBL(main_btm,"Description",0,0,100,18);
		lb_des.setBorder(BorderFactory.createLineBorder(Color.gray));
		cl_dat.M_txtDESC_pbst=crtTXT(main_btm,0,100,0,800-300,18);
		cl_dat.M_txtSTAT_pbst=crtTXT(main_btm,JTextField.LEFT,0,18,800-200,18);
		cl_dat.M_txtUSER_pbst=crtTXT(main_btm,0,800-200,0,100,18);
		cl_dat.M_txtDATE_pbst=crtTXT(main_btm,0,800-100,0,100,18);
		cl_dat.M_txtDMSG_pbst=crtTXT(main_btm,0,0,18,800-300,18);
		cl_dat.M_txtUSER_pbst.setBackground(this.getBackground());
		cl_dat.M_txtDMSG_pbst.setBackground(this.getBackground());
		cl_dat.M_txtSTAT_pbst.setBackground(this.getBackground());
		cl_dat.M_txtDATE_pbst.setBackground(this.getBackground());
		main_btm.setVisible(false);
		getContentPane().add(main_btm);
		main_btm.setBounds(0,cl_dat.M_dimSCRN_pbst.height-new Double(200*cl_dat.M_dblHIGHT).intValue(),cl_dat.M_dimSCRN_pbst.width,new Double(40*cl_dat.M_dblHIGHT).intValue());
	//ADDING CENTER PANEL (DEFAULT LOGIN)**********
	//MAKING LOGIN SCREEN ******************
		cl_dat.M_pnlLOGIN_pbst=new JPanel(null);
		cl_dat.M_pnlLOGIN_pbst.setBounds(0,130,new Double(800*cl_dat.M_dblWIDTH).intValue(),new Double(300*cl_dat.M_dblHIGHT).intValue());
		cl_dat.M_crdCENTR_pbst = new CardLayout();
		cl_dat.M_pnlFRBTM_pbst=new JPanel(cl_dat.M_crdCENTR_pbst);
		cl_dat.M_pnlFRBTM_pbst.setBorder(BorderFactory.createBevelBorder(0));
		imiSPL=new ImageIcon(imgSPL);
		//// chnged on july 04 . changed Y-posn of image from 100 to 60.
		JLabel lblLOGIN = crtLBL(cl_dat.M_pnlLOGIN_pbst,"LOGIN",387,60,100,40);
		lblLOGIN.setFont(fntLOGIN);
		lblLOGIN.setForeground(Color.blue);
		JLabel lblIMGIC = new JLabel(imiSPL);
		
		//// chnged on july 04 . changed Y-posn of image from 145 to 100.
		lblIMGIC.setBounds(new Double(370*cl_dat.M_dblWIDTH).intValue(),new Double(100*cl_dat.M_dblHIGHT).intValue(),new Double(100*cl_dat.M_dblWIDTH).intValue(),new Double(100*cl_dat.M_dblHIGHT).intValue());
		cl_dat.M_pnlLOGIN_pbst.add(lblIMGIC);
		
		////ADDED on july 04 to add functionality of company combo box.
		JLabel lblUSRNM = crtLBL(cl_dat.M_pnlLOGIN_pbst,"User Name",325,230,75,20);
		cl_dat.M_txtUSRCD_pbst = crtTXT(cl_dat.M_pnlLOGIN_pbst,JTextField.LEFT,410,230,120,20);
		JLabel lblPASWD = crtLBL(cl_dat.M_pnlLOGIN_pbst,"Password",325,260,75,20);
		cl_dat.M_txtPASWD_pbst = new JPasswordField(10);
		cl_dat.M_txtPASWD_pbst.setBounds(new Double(410*cl_dat.M_dblWIDTH).intValue(),new Double(260*cl_dat.M_dblHIGHT).intValue(),new Double(120*cl_dat.M_dblWIDTH).intValue(),new Double(20*cl_dat.M_dblHIGHT).intValue());
		cl_dat.M_pnlLOGIN_pbst.add(cl_dat.M_txtPASWD_pbst);
		
		JLabel lblCMPCD = crtLBL(cl_dat.M_pnlLOGIN_pbst,"Location",325,290,75,20);
		cl_dat.M_cmbCMPCD_pbst = crtCMB(cl_dat.M_pnlLOGIN_pbst,cl_dat.M_OPSEL_pbst,410,290,120,20);
		
		JLabel lblCURDT = crtLBL(cl_dat.M_pnlLOGIN_pbst,"Current Date",325,320,75,20);
		cl_dat.M_txtCURDT_pbst =new TxtDate();
		cl_dat.M_txtCURDT_pbst.addFocusListener(this);
		cl_dat.M_txtCURDT_pbst.setBounds(new Double(410*cl_dat.M_dblWIDTH).intValue(),new Double(320*cl_dat.M_dblHIGHT).intValue(),new Double(120*cl_dat.M_dblWIDTH).intValue(),new Double(20*cl_dat.M_dblHIGHT).intValue());
		cl_dat.M_txtCURDT_pbst.addActionListener(this);
		cl_dat.M_pnlLOGIN_pbst.add(cl_dat.M_txtCURDT_pbst);
		cl_dat.M_btnEXT_pbst = crtBTN(cl_dat.M_pnlLOGIN_pbst,"Exit",325,350,75,20);
		cl_dat.M_btnSETPW_pbst = crtBTN(cl_dat.M_pnlLOGIN_pbst,"Set Password",410,350,120,20);
		btnINTRN = crtBTN(cl_dat.M_pnlLOGIN_pbst,"Intimation",540,350,75,20);
		cl_dat.M_pnlFRBTM_pbst.add("login",cl_dat.M_pnlLOGIN_pbst);
		cl_dat.M_pnlFRBTM_pbst.add("screen",new JPanel());
		this.setSize(cl_dat.M_dimSCRN_pbst.width,cl_dat.M_dimSCRN_pbst.height);
		cl_dat.M_pnlFRBTM_pbst.setBounds(0,new Double(60*cl_dat.M_dblHIGHT).intValue(),cl_dat.M_dimSCRN_pbst.width,cl_dat.M_dimSCRN_pbst.height-new Double(125*cl_dat.M_dblHIGHT).intValue());
		getContentPane().add(cl_dat.M_pnlFRBTM_pbst );
		getContentPane().add(main_btm);
		main_btm.setBounds(0,cl_dat.M_dimSCRN_pbst.height-new Double(65*cl_dat.M_dblHIGHT).intValue(),cl_dat.M_dimSCRN_pbst.width,new Double(37*cl_dat.M_dblHIGHT).intValue());
		cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"login");
		cl_dat.M_pnlFRTOP_pbst.setVisible(false);
	// Creating Status Bar at bottom of screen
		cl_dat.M_txtSTAT_pbst.setText("");
		cl_dat.M_txtUSER_pbst.setText(cl_dat.M_strUSRCD_pbst);
		cl_dat.M_txtDATE_pbst.setText(cl_dat.M_strLOGDT_pbst);
		cl_dat.M_txtSTAT_pbst.setEditable(false);
	    cl_dat.M_txtUSER_pbst.setEditable(false);
        cl_dat.M_txtDATE_pbst.setEditable(false);
		cl_dat.M_txtUSER_pbst.setEnabled(false);
        cl_dat.M_txtDATE_pbst.setEnabled(false);
		cl_dat.M_txtSTAT_pbst.setEnabled(false);
		cl_dat.M_txtDMSG_pbst.setText("Description ");
		cl_dat.M_txtDMSG_pbst.setFont(new Font("Times new Roman",Font.BOLD,11));
		cl_dat.M_txtDMSG_pbst.setEditable(false);
		cl_dat.M_txtDESC_pbst.setEditable(false);
		cl_dat.M_txtDMSG_pbst.setEnabled(false);
		cl_dat.M_txtDESC_pbst.setEnabled(false);
		cl_dat.M_txtDESC_pbst.setFont(new Font("Comic Sans MS",Font.BOLD,10));
		cl_dat.M_txtSTAT_pbst.setFont(new Font("Lucida Console",Font.BOLD,12));
		lblMODUL.setFont(new Font("Times New Roman",Font.BOLD,12));
		// Status Bar crated
	//SET DEFAULT STATUS OF COMPONENTS
	    //cl_dat.M_strSYSLC_pbst0 = strSYSLC0_st;
        //cl_dat.M_strSYSLC_pbst  = strSYSLC_st;
		cl_dat.M_txtUSRCD_pbst.setEnabled(true);
        cl_dat.M_txtPASWD_pbst.setEnabled(false);
        cl_dat.M_cmbCMPCD_pbst.setEnabled(false);
		cl_dat.M_txtCURDT_pbst.setEnabled(false);
      	cl_dat.M_btnSETPW_pbst.setEnabled(false);
		//btnINTRN.setEnabled(false);
		btnINTRN.setVisible(false);
        cl_dat.M_txtPASWD_pbst.setText("");
		cl_dat.M_txtUSRCD_pbst.requestFocus();
		this.setTitle("Supreme Petrochem Ltd.");
		lblMODUL.setText("");
	//ADDING MENU
		mbrMNUBR=new JMenuBar();
		pnlMENU.add(mbrMNUBR);
		mbrMNUBR.setBounds(0,0,cl_dat.M_dimSCRN_pbst.width,new Double(25*cl_dat.M_dblHIGHT).intValue());
	//addDEFMNU();
		mnuMASTR=crtMNU("       ",0);
		mnuSEPTR1=crtMNU("       ",0);
		mnuTRANS=crtMNU("            ",0);
		mnuSEPTR2=crtMNU("       ",0);
		mnuREPRT=crtMNU("       ",0);
		mnuSEPTR3=crtMNU("       ",0);
		mnuQUERY=crtMNU("      ",0);
		mnuSEPTR4=crtMNU("       ",0);
		mnuHSKPG=crtMNU("        ",0);
		mnuSEPTR5=crtMNU("       ",0);
		mnuEXIT=crtMNU("    ",0);

		mnuTEFG=crtSUBMNU("   ",0);
		mnuTEHR=crtSUBMNU("   ",0);
		mnuTEAD=crtSUBMNU("   ",0);
		mnuTEPM=crtSUBMNU("   ",0);
		mnuTEMR=crtSUBMNU("   ",0);
		mnuTECO=crtSUBMNU("   ",0);
		mnuTEMM=crtSUBMNU("   ",0);
		mnuTEPR=crtSUBMNU("   ",0);
		mnuTEQC=crtSUBMNU("   ",0);
		mnuTESA=crtSUBMNU("   ",0);
	
		mnuMEFG=crtSUBMNU("   ",0);
		mnuMEHR=crtSUBMNU("   ",0);
		mnuMEAD=crtSUBMNU("   ",0);
		mnuMEPM=crtSUBMNU("   ",0);
		mnuMEMR=crtSUBMNU("   ",0);
		mnuMECO=crtSUBMNU("   ",0);
		mnuMEMM=crtSUBMNU("   ",0);
		mnuMEPR=crtSUBMNU("   ",0);
		mnuMEQC=crtSUBMNU("   ",0);
		mnuMESA=crtSUBMNU("   ",0);
				
	
		mnuRPFG=crtSUBMNU("   ",0);
		mnuRPHR=crtSUBMNU("   ",0);
		mnuRPAD=crtSUBMNU("   ",0);
		mnuRPPM=crtSUBMNU("   ",0);
		mnuRPMR=crtSUBMNU("   ",0);
		mnuRPCO=crtSUBMNU("   ",0);
		mnuRPMM=crtSUBMNU("   ",0);
		mnuRPPR=crtSUBMNU("   ",0);
		mnuRPQC=crtSUBMNU("   ",0);
		mnuRPSA=crtSUBMNU("   ",0);
				
	
		mnuQRFG=crtSUBMNU("   ",0);
		mnuQRHR=crtSUBMNU("   ",0);
		mnuQRAD=crtSUBMNU("   ",0);
		mnuQRPM=crtSUBMNU("   ",0);
		mnuQRMR=crtSUBMNU("   ",0);
		mnuQRCO=crtSUBMNU("   ",0);
		mnuQRMM=crtSUBMNU("   ",0);
		mnuQRPR=crtSUBMNU("   ",0);
		mnuQRQC=crtSUBMNU("   ",0);
		mnuQRSA=crtSUBMNU("   ",0);
				
	
		mnuHKFG=crtSUBMNU("   ",0);
		mnuHKHR=crtSUBMNU("   ",0);
		mnuHKAD=crtSUBMNU("   ",0);
		mnuHKPM=crtSUBMNU("   ",0);
		mnuHKMR=crtSUBMNU("   ",0);
		mnuHKCO=crtSUBMNU("   ",0);
		mnuHKMM=crtSUBMNU("   ",0);
		mnuHKPR=crtSUBMNU("   ",0);
		mnuHKQC=crtSUBMNU("   ",0);
		mnuHKSA=crtSUBMNU("   ",0);
		
		
		mnuSEPTR1.setEnabled (false);
		mnuSEPTR2.setEnabled (false);
		mnuSEPTR3.setEnabled (false);
		mnuSEPTR4.setEnabled (false);
		mnuSEPTR5.setEnabled (false);
		
		//MENU ADDED
	//Adding Listeners
		cl_dat.M_cmbCMPCD_pbst.addFocusListener(this);
		cl_dat.M_cmbCMPCD_pbst.addKeyListener(this);
		cl_dat.M_cmbCMPCD_pbst.addActionListener(this);
		cl_dat.M_cmbCMPCD_pbst.setActionCommand("cl_dat.M_cmbCMPCD_pbst");
		
		cl_dat.M_txtUSRCD_pbst.setActionCommand("txtUSR");
		cl_dat.M_txtPASWD_pbst.addFocusListener(this);
		cl_dat.M_txtPASWD_pbst.addKeyListener(this);
		cl_dat.M_txtPASWD_pbst.addActionListener(this);
		cl_dat.M_txtPASWD_pbst.setActionCommand("cl_dat.M_txtPASWD_pbst");
		cl_dat.M_txtCURDT_pbst.setActionCommand("cl_dat.M_txtCURDT_pbst");
		cl_dat.M_btnEXT_pbst.setActionCommand("cl_dat.M_btnEXT_pbst");
		cl_dat.M_btnSETPW_pbst.setActionCommand("cl_dat.M_btnSETPW_pbst");
		btnINTRN.setActionCommand("btnINTRN");
//		if(t!=null)
//			t.join();
		main_btm.setVisible(true);
		getContentPane().validate();
		//****update(getGraphics());
		//DISABLING CLOSING FROM WINDOW ICON ON TOP RIGHT CORNER
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
	//STARTING TIMER FOR IDLE TIME
		cl_dat.M_tmrIDLTM_pbst=new javax.swing.Timer(1800000,this);
		cl_dat.M_tmrIDLTM_pbst.start();
	    if(strCONTP_st.equalsIgnoreCase("TST"))
        {
			cl_dat.M_flgTSTDT_pbst = true;
			setMSG("You are entering into Test database zone",'N');
			getContentPane().validate();
			update(getGraphics());
		}
		cl_dat.M_txtCLKDT_pbst.setFocusable(false);
		cl_dat.M_txtCLKTM_pbst.setFocusable(false);
		cl_dat.M_txtDESC_pbst.setFocusable(false);
		cl_dat.M_txtDMSG_pbst.setFocusable(false);
		cl_dat.M_txtSTAT_pbst.setFocusable(false);
		hstUSRRT=new Hashtable<String,String[]>(10,0.2f);
		vtrPRGCD=new Vector<String>(10,2);
		vtrSBSCD=new Vector<String>(10,2);
		vtrSHRDS=new Vector<String>(10,2);
		vtrCHP01=new Vector<String>(10,2);
		vtrCHP02=new Vector<String>(10,2);
		vtrADDFL=new Vector<String>(10,2);
		vtrMODFL=new Vector<String>(10,2);
		vtrDELFL=new Vector<String>(10,2);
		vtrENQFL=new Vector<String>(10,2);
		vtrAUTFL=new Vector<String>(10,2);//ADDED ON 02/04/2004 BY AAP FOR AUTHORISATION OPTION
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	
		
		}catch (Exception e)
		{setMSG(e,"FR_LOG.Constructor");}
	}
	/** Thread.Run()<br><br><BR>
	 * <b>TASKS :</B><BR>
	 * &nbsp&nbsp&nbspSource=thrHITCNT : call setHITCNT() to update hit count<BR>
	 * &nbsp&nbsp&nbsp else : set connection to DB	 */
	public void run()
	{//UPDATE FOR  TRANSFER OF FOLLOWING VARIABLES TO LOCAL SCOPE
		try
		{
//			if(Thread.currentThread()==thrTEMP)
//				System.out.println("in thr");
			if(Thread.currentThread()==thrHITCNT)
			{
				setHITCNT();//Updating Hit Count at Back end.
			}
			else if(Thread.currentThread()==cl_dat.M_thrPRNLS_pbst)
			{
				cl_dat.getPRSRV();
			}
			//else if(Thread.currentThread()==thrCMPCD)
			//{
			//	setCMB_CMPCD();
			//}
			else//Code For DB Connection starts
			{
				//if(strEXEDRV_st.length() > 0)
				  //  strWRKDR = strEXEDRV_st.trim()+":\\";
				cl_dat.M_flgTSTDT_pbst = false;
				M_strSPUSA_pb_st = "FIMS";
				M_strSPPWA_pb_st = "FIMS";
				M_strSPLBA_pb_st = "SPLDATA";
				M_strSPDNA_pb_st = "SPLDATA";
				if(strCONTP_st.equalsIgnoreCase("TST"))
				{
					cl_dat.M_flgTSTDT_pbst = true;
					System.out.println("You are entering into Test database zone");
					M_strSPLBA_pb_st = "SPLTEST";
					M_strSPDNA_pb_st = "SPLTEST";
				}
                                if(strCONTP_st.equalsIgnoreCase("HST"))
				{
                                        System.out.println("Connecting to History Database");
                                        M_strSPLBA_pb_st = "SPLHIST";
                                        M_strSPDNA_pb_st = "SPLHIST";
				}
				//setCONACT(strSYSLC0_st,M_strSPLBA_pb_st,M_strSPUSA_pb_st,M_strSPPWA_pb_st);
				setCONACT("02",M_strSPLBA_pb_st,M_strSPUSA_pb_st,M_strSPPWA_pb_st);
				//strCURLC=strSYSLC0_st;

			}
		}catch(Exception e)
		{setMSG(e,"Connection to DB");}
	}
	/**Entry point for application<BR><BR><BR>Varifies command line aurgument and set parameters accordingly. 	 */
	public static void main(String args[])
    {
		try{
		if (args.length > 0)
        {
            cl_dat.M_strUSRCT_pbst = args[0];
            strEXEDRV_st = args[1].substring(0,1);
	    	strRPDIR =args[1].substring(1);
			if(args.length > 2)
               strCONTP_st = args[2];
			fr_log ofr_log = new fr_log();
			//ofr_log.show();	deprecated in 1.6
			ofr_log.setVisible(true);
			
		}
        else
			System.out.println("Enter parameters");
		}catch(Exception e)
		{System.out.println("Error in main : "+e);}
	}
	
	/** Populating Compny code in Comb Box
	 */
	public void setCMB_CMPCD()
	{
		try
		{
			//if(cl_dat.M_strUSRCD_pbst.length()==0)
			//	return;
			for (int i=cl_dat.M_cmbCMPCD_pbst.getItemCount()-1;i>0;i--)
				cl_dat.M_cmbCMPCD_pbst.removeItemAt(i);
			//System.out.println("cl_dat.M_cmbCMPCD_pbst.getItemCount()>>"+cl_dat.M_cmbCMPCD_pbst.getItemCount());
			//ADDED on 4 july to add functionality for company code and company name.
			String L_strSQLQRY = " SELECT CP_CMPCD,CP_CMPNM FROM CO_CPMST where CP_CMPCD in (select ust_cmpcd from sa_ustrn where ust_usrct = '"+cl_dat.M_strUSRCT_pbst+"' and ust_usrcd = '"+cl_dat.M_txtUSRCD_pbst.getText().toUpperCase()+"') order by CP_CMPCD";
			//System.out.println(L_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			if(L_rstRSSET !=null)
			{
			    while(L_rstRSSET.next())
			    {
					L_rstRSSET.getString("CP_CMPCD");
					cl_dat.M_cmbCMPCD_pbst.addItem(L_rstRSSET.getString("CP_CMPCD")+" "+L_rstRSSET.getString("CP_CMPNM"));
				}
			}	
			else setMSG("No Data Found in CO_CPMST",'E'); 
				
			//cl_dat.M_strCMPCD_pbst ="01";
			//cl_dat.M_strCMPNM_pbst = "Supreme Petrochem Ltd.";
		}catch(Exception e)
		{System.out.println("Error in setCMBCMPCD() : "+e);}
	}

	/**
	 * Establishees connection with DB and creates statement objects
	 *
	 * @param LP_SYSLC String of length 2 for System Location
	 * @param LP_XXLBX String for library name
	 * @param LP_XXUSX String for User name
	 * @param LP_XXPWX String for Password */
	public  void setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX)
    {
		try
        {
          cl_dat.M_conSPDBA_pbst = this.setCONDTB(LP_SYSLC,LP_XXLBX, LP_XXUSX, LP_XXPWX);
          if(cl_dat.M_conSPDBA_pbst != null)
          {
              cl_dat.M_conSPDBA_pbst.rollback();
		  	  cl_dat.M_stmSPDBA_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst1 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst2 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst3 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
		  }
	    }
        catch(Exception L_EX)
        {
			System.out.println("Error in setCONTACT"+L_EX);
		}
	}
	/** Establishes connection to DB.
	 * Called internally from
	 * {@link  fr_log#setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX) }
	 */
	private  Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
        {
		Connection LM_CONDTB=null;
		try
                {       String L_STRURL = "", L_STRDRV = "";
						if(LP_SYSLC.equals("01"))
                        {
                                L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                                L_STRURL = "jdbc:as400://192.168.203.7/";
                                Class.forName(L_STRDRV);
                        }
                        else if(LP_SYSLC.equals("02"))
                        {
                                int port = 50000;
                                // LP_DTBUS = "SPLDATA";
								// LP_DTBPW = "SPLDATA";
								LP_DTBUS= "naman";
								LP_DTBPW="Biniar@1234";
								
								L_STRURL="jdbc:sqlserver://192.168.0.100:1433;databasename=";

								LP_DTBLB ="SPLDATA_Temp;";
                               // L_STRURL = "jdbc:db2://" + "splhos01" + ":" + 1433 + "/" ;
                                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                        }
						else if(LP_SYSLC.equals("03"))
                        {
                                L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                                L_STRURL = "jdbc:as400://MUMAS2/";
                                Class.forName(L_STRDRV);
						}
						System.out.println(LP_DTBLB);
                        L_STRURL = L_STRURL + LP_DTBLB;
                        LM_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);

                        if(LM_CONDTB == null)
				return null;
                        LM_CONDTB.setAutoCommit(false);

                        SQLWarning L_STRWRN = LM_CONDTB.getWarnings();
			if ( L_STRWRN != null )
			   System.out.println("Warning in setCONDTB : "+L_STRWRN);
                        return LM_CONDTB;
		}
                catch(java.lang.Exception L_EX)
                {
			System.out.println("setCONDTB" + L_EX.toString());
			return null;
		}

    }
/**To remove Separators from a menu <p>
 * Removes separators at all positions in the menu
 * @param SRCMNU : JMenu from which, separators are to be removed.
 * <p>Called internally from @see fr_log#actionPerformed(ActionEvent L_AE)
 */
	private void removeSeparator(JMenu SRCMNU)
	{
		for(int i=0;i<SRCMNU.getItemCount();i++)
			if(!SRCMNU.isMenuComponent(SRCMNU.getItem(i)))
				SRCMNU.remove(i);
	}
/**To add Separators to a menu <p>
 * Adds separators at all positions in the menu where System Code i.e. first two letters ofthe Program code are getting changed.
 * @param SRCMNU : JMenu to which, separators are to be added.
 * <p>Called internally from @see fr_log#actionPerformed(ActionEvent L_AE)
 */
	private void addSeparator(JMenu SRCMNU)
	{
		for(int i=0;i<SRCMNU.getItemCount()-1;i++)
			if(SRCMNU.isMenuComponent(SRCMNU.getItem(i))&&SRCMNU.isMenuComponent(SRCMNU.getItem(i+1))&&!SRCMNU.getItem(i).getText().substring(0,2).equals(SRCMNU.getItem(i+1).getText().substring(0,2)))
				SRCMNU.insertSeparator(++i);
		SRCMNU.updateUI();//SRCMNU.doClick();
	}
	/**<b>TASKS :</B><br>
	 * <br>&nbsp&nbsp&nbspSource=cl_dat.M_tmrIDLTM_pbst : Closes application after disconnecting DB<br>
	 * <br>&nbsp&nbsp&nbspSource=tmrMNUUP : Scrolls menu upword
	 * <br>&nbsp&nbsp&nbspSource=tmrMNUDN : Scrolls menu downword
	 * <br>&nbsp&nbsp&nbspSource=cl_dat.M_btnEXIT_pbst : Exit from current screen, show login screen, Hide button pakket, Show Menu, Hide SBS combos
	 * <br>&nbsp&nbsp&nbspSource=cl_dat.M_btnHELP_pbst : Launch Internet explorer with hoe help page
	 * <br>&nbsp&nbsp&nbspSource=txtUSR : convert USRCD to caps, Disable it, set focus to pasword
	 * <br>&nbsp&nbsp&nbspSource=cl_dat.M_txtPASWD_pbst : Validate user by calling chkusrvld()
	 * <br>&nbsp&nbsp&nbspSource=cl_dat.M_btnEXT_pbst : Close application after disconnecting DB
	 * <br>&nbsp&nbsp&nbspSource=cl_dat.M_btnSETPW_pbst : Open set Password screen
	 * <br>&nbsp&nbsp&nbspELSE : Consider that, menuitem is clicked and show corresponding screen by calling crtSCN(), Start thread for hit count updation.
	 */
   public void actionPerformed(ActionEvent L_AE)
   {
	   this.setCursor(cl_dat.M_curWTSTS_pbst);
		try
		{
			if(cl_dat.M_tmrIDLTM_pbst!=null)
				cl_dat.M_tmrIDLTM_pbst.restart();
			if(L_AE.getSource()==cl_dat.M_tmrIDLTM_pbst)
			{
				System.out.println(calCLOCK.getTime().toString());
				clsAPPCON();
				System.exit(0);
			}
		//*******CODE FOR SCROLLABLE MENU
		//TO CHANGE LENGTH OF MENU, SET VALUE OF intMNULN_fn IN DECLARATION.
			else if(L_AE.getSource()==tmrMNUUP)//SCROLLING UP
			{
				removeSeparator(mnuTMRSRC);
				for(int i=mnuTMRSRC.getItemCount()-2;i>intMNULN_fn-2;i--)
				{
					try
					{
						if(mnuTMRSRC.getItem(i)!=null)
							if(mnuTMRSRC.getItem(i).isVisible())
							{
								for(int z=1;z<intMNULN_fn-1;z++)
									mnuTMRSRC.getItem(i-z).setVisible(true);
								mnuTMRSRC.getItem(i).setVisible(false);
								mnuTMRSRC.getItem(mnuTMRSRC.getItemCount()-1).setEnabled(true);
								if(mnuTMRSRC.getItem(1).isVisible())
								{//Disable Up scroll and stop timer if first menuitem is show.
									mnuTMRSRC.getItem(0).setEnabled(false);
									tmrMNUUP.stop();
								}
								addSeparator(mnuTMRSRC);
								break;
							}
					}catch(Exception e)
					{
						System.out.println(e);
						removeSeparator(mnuTMRSRC);
						addSeparator(mnuTMRSRC);
					}
				}
				addSeparator(mnuTMRSRC);
			}
			else if(L_AE.getSource()==tmrMNUDN)//SCROLLING DOWN
			{
				removeSeparator(mnuTMRSRC);
				for(int i=1;i<mnuTMRSRC.getItemCount()-intMNULN_fn+1;i++)
				{
				 	try
					{
						if(mnuTMRSRC.getItem(i)!=null)
							if(mnuTMRSRC.getItem(i).isVisible())
							{
								for(int z=1;z<intMNULN_fn-1;z++)
									mnuTMRSRC.getItem(z+i).setVisible(true);
								mnuTMRSRC.getItem(i).setVisible(false);
								mnuTMRSRC.getItem(0).setEnabled(true);
								if(mnuTMRSRC.getItem(mnuTMRSRC.getItemCount()-2).isVisible())
								{//Disable Down scroll and stop timer if last menuitem is show.
									mnuTMRSRC.getItem(mnuTMRSRC.getItemCount()-1).setEnabled(false);
									tmrMNUDN.stop();
								}
								addSeparator(mnuTMRSRC);
								break;
							}
					}catch(Exception e)
					{
						System.out.println(e);
						removeSeparator(mnuTMRSRC);
						addSeparator(mnuTMRSRC);
					}
				}
				addSeparator(mnuTMRSRC);
			}
			else
			{
				tmrMNUUP.stop();tmrMNUDN.stop();//STOP TIMERS FOR SCROLLING OF MENU.(CLICKING ON UP/DOWN MENU KEEPS TIMERS RUNNING.
				String L_ACTCMD =  L_AE.getActionCommand();
				String L_LEFT02 = L_AE.getActionCommand().substring(0,2);
				if(L_ACTCMD.equals("cl_dat.M_btnEXIT_pbst")){
					this.setTitle(cl_dat.M_strCMPNM_pbst);
					cl_dat.M_pnlFRTOP_pbst.setVisible(false);
					cl_dat.M_pnlSBSCD_pbst.setVisible(false);
					pnlMENU.setVisible(true);
					cl_dat.M_pnlSBSCD_pbst=new JPanel(null);
					cl_dat.M_cmbSBSL1_pbst=null;cl_dat.M_cmbSBSL2_pbst=null;cl_dat.M_cmbSBSL3_pbst=null;
					for (int i=cl_dat.M_cmbOPTN_pbst.getItemCount()-1;i>0;i--)
						cl_dat.M_cmbOPTN_pbst.removeItemAt(i);
					if(cl_dat.M_tmrUPUSG_st!=null)
						cl_dat.M_tmrUPUSG_st.join();
					cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"login");
					this.getContentPane().validate();
					this.update(this.getGraphics());
					cl_dat.M_btnEXT_pbst.requestFocus();
					setMSG("Press space bar to exit ..",'N');
				}
				else if(L_ACTCMD.equals("cl_dat.M_btnHELP_pbst"))
				{
					try{
						cl_dat.M_strURL_pbst = "//10.0.0.1/htmfiles/"+strPRGCD +".htm";
						Process p = null;
						Runtime r = Runtime.getRuntime();
						p  = r.exec("c:\\program files\\internet explorer\\iexplore.exe "+cl_dat.M_strURL_pbst);
					}catch(Exception L_EX)
					{
						setMSG(L_EX,"actionPerformed()");
					}
				}
			 else if(L_ACTCMD.equals("txtUSR"))
	         {
				cl_dat.M_txtPASWD_pbst.setEnabled(true);
				cl_dat.M_txtUSRCD_pbst.setEnabled(false);
				cl_dat.M_txtPASWD_pbst.setText("");
				cl_dat.M_txtPASWD_pbst.requestFocus();
				cl_dat.M_strUSRCD_pbst = cl_dat.M_txtUSRCD_pbst.getText().toUpperCase();
				cl_dat.M_txtUSRCD_pbst.setText(cl_dat.M_strUSRCD_pbst);
				//thrCMPCD=new Thread(this);//thread for company code combo box.
				//thrCMPCD.setPriority(10);
				//thrCMPCD.start();
			}
			else if(L_ACTCMD.equals("cl_dat.M_txtPASWD_pbst"))
			{
				if(thrCONNECT!=null)
					thrCONNECT.join();
				//if(!strCURLC.equals(strSYSLC0_st))
				//{
					//setCONACT(strSYSLC0_st,M_strSPLBA_pb_st,M_strSPUSA_pb_st,M_strSPPWA_pb_st);
					//setCONACT("01",M_strSPLBA_pb_st,M_strSPUSA_pb_st,M_strSPPWA_pb_st);
					//strCURLC=strSYSLC0_st;
				//}
				setCMB_CMPCD();				
				chkUSRVLD0();
			}
			else if(L_ACTCMD.equals("cl_dat.M_cmbCMPCD_pbst"))	//Company COMBO.	
			{
			   cl_dat.M_strCMPCD_pbst = cl_dat.M_cmbCMPCD_pbst.getSelectedItem().toString().substring(0,2);
			   cl_dat.M_strCMPCD_EXT_pbst = cl_dat.M_strCMPCD_pbst;
			   cl_dat.M_strCMPNM_pbst = "SUPREME PETROCHEM LTD";
			   cl_dat.M_strCMPLC_pbst = cl_dat.M_cmbCMPCD_pbst.getSelectedItem().toString().substring(3);
			   //cl_dat.M_cmbCMPCD_pbst.setEnabled(false);				
				chkUSRVLD1();
			   cl_dat.M_txtCURDT_pbst.setEnabled(true);
			   cl_dat.M_txtCURDT_pbst.requestFocus();
			}	
			else if(L_ACTCMD.equals("cl_dat.M_txtCURDT_pbst"))
	        {
				if(cl_dat.M_cmbCMPCD_pbst.getSelectedItem().equals("Select an Option"))	
				{	
					setMSG("Select Location First..",'E');
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}	
				if(cl_dat.M_txtCURDT_pbst.vldDATE()!=null)
				{//Keep txtCURDT enabled if date is invalid and dont create menu
					cl_dat.M_txtCURDT_pbst.transferFocus();
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				cl_dat.M_strLOGDT_pbst = cl_dat.M_txtCURDT_pbst.getText();
				crtMENU();
				pnlMENU.setVisible(true);
				cl_dat.M_txtUSER_pbst.setText(cl_dat.M_txtUSRCD_pbst.getText());
				cl_dat.M_txtDATE_pbst.setText(cl_dat.M_txtCURDT_pbst.getText());
				cl_dat.M_txtCURDT_pbst.setEnabled(false);
				// cal the hastable generation after this
				crtHSTMKT();
                cl_dat.crtHST();
			}
			else if(L_ACTCMD.equals("cl_dat.M_btnEXT_pbst"))
	        {
			   clsAPPCON();
			   System.exit(0);
			}
			else if(L_ACTCMD.equals("btnINTRN"))
	        {
				if(cl_dat.M_strEMPNO_pbst.length()==4)
				{
					co_rpint oco_rpint = new co_rpint(cl_dat.M_strEMPNO_pbst,cl_dat.M_txtCURDT_pbst.getText());
				}
			}
			else if(L_ACTCMD.equals("cl_dat.M_btnSETPW_pbst"))
	        {//SHOWING CHANGE PASSWORD SCREEN
				fr_pwd obj = new fr_pwd();
				cl_dat.M_pnlFRBTM_pbst.add("screen", obj);
			cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen");
			}
	        else if(L_LEFT02.equals("FG") ||L_LEFT02.equals("QC")|| L_LEFT02.equals("CO") || L_LEFT02.equals("MS") || L_LEFT02.equals("PR") || L_LEFT02.equals("MM") || L_LEFT02.equals("MR") || L_LEFT02.equals("SA") || L_LEFT02.equals("QA")||L_LEFT02.equals("SA")||L_LEFT02.equals("HR")||L_LEFT02.equals("PR")||L_LEFT02.equals("MR")||L_LEFT02.equals("AD")||L_LEFT02.equals("PM"))
	        {
					strACTCMD = L_AE.getActionCommand();
			     
					//CODE TO BLOCK ISSUE ENTRY FOR USERS OTHER THAN STORES 
					//used on 01/04/2005 
				/*
                    // To block both the Issue and Indent entry	uncomment the line written below,for issue only, uncomment next line 
                   	//if(strACTCMD.equalsIgnoreCase("mm_teist") || strACTCMD.equalsIgnoreCase("mm_teind"))
					//    if(strACTCMD.equalsIgnoreCase("mm_teist"))
						{
							System.out.println("block");
							ResultSet L_rstRSSET=cl_dat.exeSQLQRY0("Select * from SA_USTRN where UST_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and UST_SYSCD='MM'");
							System.out.println("Select * from SA_USTRN where UST_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and UST_SYSCD='MM'");
							if(L_rstRSSET.next())
							// Access to VVD Only uncomment the line below
							//    if(!L_rstRSSET.getString("UST_USRTP").equalsIgnoreCase("MM003"))
							// Access to VVD and RSP uncomment the line below
							//  if((!L_rstRSSET.getString("UST_USRTP").equalsIgnoreCase("MM003"))&&(!L_rstRSSET.getString("UST_USRTP").equalsIgnoreCase("MM002")))
								{
								    System.out.println("block 1");
									setCursor(cl_dat.M_curDFSTS_pbst);
									JOptionPane.showMessageDialog(this,"Access Blocked for Year Ending ..","Blocked",JOptionPane.INFORMATION_MESSAGE);
									setMSG("Access Blocked For Year Ending ..",'E');
									return;
								}
						}
				*/		
					//END OF ACCESS BLOCKING for Year End 
					
					pnlMENU.setVisible(false);
					if(strACTCMD.substring(3).equals("XXEXT"))
	                {
						//strCURLC="";
	                    exeCLRMNU();
						tmrCLOCK.cancel();
						cl_dat.M_pnlSBSCD_pbst.setVisible(false);
						pnlMENU.setVisible(true);
						cl_dat.M_pnlSBSCD_pbst=new JPanel(null);
						cl_dat.M_cmbSBSL1_pbst=null;cl_dat.M_cmbSBSL2_pbst=null;cl_dat.M_cmbSBSL3_pbst=null;
						this.setTitle(cl_dat.M_strCMPNM_pbst);
						this.getContentPane().validate();
						this.update(this.getGraphics());

						cl_dat.M_txtUSRCD_pbst.setEnabled(true);
						cl_dat.M_txtUSRCD_pbst.setText("");
						cl_dat.M_cmbCMPCD_pbst.setSelectedIndex(0);
						cl_dat.M_cmbCMPCD_pbst.setEnabled(false);
						cl_dat.M_txtCURDT_pbst.setEnabled(false);
						cl_dat.M_btnSETPW_pbst.setEnabled(false);
						//btnINTRN.setEnabled(false);
						btnINTRN.setVisible(false);
						cl_dat.M_txtCURDT_pbst.setText("");
						cl_dat.M_txtUSRCD_pbst.requestFocus();
					}
					else
	                {
						((JMenuItem)L_AE.getSource()).setBorder(BorderFactory.createBevelBorder(0,this.getBackground(),this.getBackground(),this.getBackground(),this.getBackground()));
						((JMenuItem)L_AE.getSource()).setForeground(Color.black);
						setMSG(L_LEFT02,'N');
						int L_CURMTH = Integer.valueOf(cl_dat.M_strLOGDT_pbst.substring(3,5)).intValue();
						int L_CURYER = Integer.valueOf(cl_dat.M_strLOGDT_pbst.substring(8,10)).intValue();
						if ( L_CURMTH >= 1 && L_CURMTH <= 6){
						    String L_SECYER = (L_CURYER - 1) + " ";
						    String L_CURYR =  L_CURYER + " ";
						    if (L_SECYER.trim().length() == 1)
								L_SECYER = "0" + L_SECYER.trim();
						    if (L_CURYR.trim().length() == 1)
						        L_CURYR = "0" + L_CURYR.trim();
						    cl_dat.M_strFNNYR_pbst = L_SECYER.trim() + L_CURYR.trim();
						}
						else
						{
							String L_SECYER = (L_CURYER + 1) + " ";
						    String L_CURYR =  L_CURYER + " ";
						    if (L_SECYER.trim().length() == 1)
						            L_SECYER = "0" + L_SECYER.trim();
						    if (L_CURYR.trim().length() == 1)
						            L_CURYR = "0" + L_CURYR.trim();
						    cl_dat.M_strFNNYR_pbst = L_CURYR.trim() + L_SECYER.trim();
						}
						if ( L_CURMTH >= 1 && L_CURMTH <= 3){
						    String L_SECYER = (L_CURYER-1) + " ";
						    String L_CURYR =  L_CURYER + " ";
						    if (L_SECYER.trim().length() == 1)
								L_SECYER = "0" + L_SECYER.trim();
						    if (L_CURYR.trim().length() == 1)
						        L_CURYR = "0" + L_CURYR.trim();
						    cl_dat.M_strFNNYR1_pbst = L_SECYER.trim() + L_CURYR.trim();
						}
						else
						{
							String L_SECYER = (L_CURYER+1 ) + " ";
						    String L_CURYR =  L_CURYER + " ";
						    if (L_SECYER.trim().length() == 1)
						            L_SECYER = "0" + L_SECYER.trim();
						    if (L_CURYR.trim().length() == 1)
						            L_CURYR = "0" + L_CURYR.trim();
						    cl_dat.M_strFNNYR1_pbst = L_CURYR.trim() + L_SECYER.trim();
						}
						System.out.println("cl_dat.M_strFNNYR1_pbst "+cl_dat.M_strFNNYR1_pbst);
						String[]L_STRTMP=(String[])hstUSRRT.get(strACTCMD);
						this.setTitle(cl_dat.M_strCMPNM_pbst+"          "+L_STRTMP[intSYSNM_fn]);
						if (!exeSPTRN(strACTCMD))
					        return;
						strPRGCD=strACTCMD;
						/*from pbase*/			
						lblMODUL.setText(" ("+strPRGCD+") : " + L_STRTMP[intPRGDS_fn]+ "   " + L_STRTMP[intVERNO_fn]);
						//if(L_STRTMP[intPRGLC_fn].equals(""))
						//{
							//if(!strCURLC.equals(strSYSLC0_st))
							//{
								//setCONACT("01",M_strSPLBA_pb_st,M_strSPUSA_pb_st,M_strSPPWA_pb_st);
								//strCURLC=strSYSLC0_st;
							//}
						//}
						//else if(!L_STRTMP[intPRGLC_fn].equals(strCURLC))
						//{
						//	setCONACT(L_STRTMP[intPRGLC_fn],"SPLDATA",M_strSPUSA_pb_st,M_strSPPWA_pb_st);
						//	strCURLC=L_STRTMP[intPRGLC_fn];
						//}
						strSYSCD=L_STRTMP[intSYSCD_fn];
						thrHITCNT=new Thread(this);//Starting thread for Hit Count
						thrHITCNT.start();
						//SHIFTED FROM CL_MNU
						Enumeration L_ELMNTS=vtrPRGCD.elements();
						int i=0,j=0;
						while(L_ELMNTS.hasMoreElements())
							if(L_ELMNTS.nextElement().toString().equals(strACTCMD))
								i++;
						String[][] L_staSBSRT=new String[i][9];	
						i=0;
						L_ELMNTS=vtrPRGCD.elements();
						while(L_ELMNTS.hasMoreElements())
						{
							if(L_ELMNTS.nextElement().toString().equals(strACTCMD))
							{

								//System.out.println("SBSCD "+vtrSBSCD.elementAt(i).toString());
								//System.out.println("SHRDS "+vtrSHRDS.elementAt(i).toString());
								//System.out.println("chp01 "+vtrCHP01.elementAt(i).toString());
								//System.out.println("chp02 "+vtrCHP02.elementAt(i).toString());
								//System.out.println("addfl "+vtrADDFL.elementAt(i).toString());
								//System.out.println("modfl "+vtrMODFL.elementAt(i).toString());
								//System.out.println("delfl "+vtrDELFL.elementAt(i).toString());
								//System.out.println("enqfl "+vtrENQFL.elementAt(i).toString());
								//System.out.println("autfl "+vtrAUTFL.elementAt(i).toString());//ADDED BY AAP ON 02/04.2004 FOR AUTHORISATION OPTION
								
								
								L_staSBSRT[j][0]=vtrSBSCD.elementAt(i).toString();
								L_staSBSRT[j][1]=vtrSHRDS.elementAt(i).toString();
								L_staSBSRT[j][2]=vtrCHP01.elementAt(i).toString();
								L_staSBSRT[j][3]=vtrCHP02.elementAt(i).toString();
								L_staSBSRT[j][4]=vtrADDFL.elementAt(i).toString();
								L_staSBSRT[j][5]=vtrMODFL.elementAt(i).toString();
								L_staSBSRT[j][6]=vtrDELFL.elementAt(i).toString();
								L_staSBSRT[j][7]=vtrENQFL.elementAt(i).toString();
								L_staSBSRT[j][8]=vtrAUTFL.elementAt(i).toString();//ADDED BY AAP ON 02/04.2004 FOR AUTHORISATION OPTION

								j++;
							}
							i++;
						}
						//SHIFTED FROM CL_MNU - END--------
						if(ocl_pbase!=null)//added on 16/03/2004 to remove refernce to old screen.
						{
							cl_dat.M_pnlFRBTM_pbst.remove(ocl_pbase);
							ocl_pbase=null;
						}

//						ocl_mnu.MNUACT(strACTCMD,L_STRTMP[intSYSCD_fn],L_staSBSRT);
						// Comment here for copying
				//		if(strACTCMD.equalsIgnoreCase("mm_teind"))
				//			ocl_pbase=(cl_pbase)new mm_tegrt();
				//	 if(strACTCMD.equalsIgnoreCase("mm_rpind"))
				//			ocl_pbase=(cl_pbase)new mm_rpdar();
				//	 else	
						 // end Comment  for copying
						    System.out.println("class name "+strACTCMD.toLowerCase());
							ocl_pbase=(cl_pbase)Class.forName(strACTCMD.toLowerCase()).newInstance();
						crtSCN(L_staSBSRT);
						getContentPane().validate();
						update(getGraphics());
						if(cl_dat.M_cmbSBSL1_pbst.getItemCount()>2)
						{
							cl_dat.M_cmbSBSL1_pbst.requestFocus();
							cl_dat.M_cmbSBSL1_pbst.showPopup();
						}
						else if(cl_dat.M_cmbSBSL2_pbst!=null)
							if(cl_dat.M_cmbSBSL2_pbst.getItemCount()>2)
							{

								cl_dat.M_cmbSBSL2_pbst.requestFocus();
								cl_dat.M_cmbSBSL2_pbst.showPopup();
							}
					}
				}
			}
		}catch(Exception L_EX)
		{
			System.out.println("ActionCommand fr_log: "+L_EX);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
 	}

   
   
   /**
    * Tasks :
    * Reset System Ilde Time Timer
    * if source=
    * {@link cl_dat#M_btnEXT_pbst} : Close application
    * if source=
    *{ @link cl_dat#M_btnSETPW_pbst} : Open screen to change password
    */
   public void keyPressed(KeyEvent L_KE)
   {
	 if(cl_dat.M_tmrIDLTM_pbst!=null)
		cl_dat.M_tmrIDLTM_pbst.restart();
//		
//			if(L_KE.getSource().equals(cl_dat.M_btnEXT_pbst))
//			{
//				System.out.println("BtnExit KEYP");
//				clsAPPCON();
//			}
	 if(L_KE.getKeyCode() == L_KE.VK_ENTER)
	 {
		 if(L_KE.getSource().equals(cl_dat.M_btnSETPW_pbst)){
				fr_pwd obj = new fr_pwd();
				cl_dat.M_pnlFRBTM_pbst.add("screen", obj);
				cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen");
			}
		}
	}
	/**
	 * Tasks :<p>
	 *		Change Borders/Forecolor of Buttons & menus</p>
	 * <p>Set massages for value to be entered in respective field </p>
	 *
	 */
	public void focusGained(FocusEvent L_FE)
    {
		try{
			JComponent M_objSOURC=(JComponent)L_FE.getSource();
			if(M_objSOURC.getClass().toString().equals("class javax.swing.JButton")||M_objSOURC.getClass().toString().equals("class javax.swing.JMenu")||M_objSOURC.getClass().toString().equals("class javax.swing.JMenuItem"))
			{
				M_objSOURC.setForeground(Color.blue );
				if(M_objSOURC.isEnabled())M_objSOURC.setBorder(BorderFactory.createBevelBorder(0));
			}
			if(L_FE.getSource().equals(cl_dat.M_txtUSRCD_pbst)){
				setMSG("Please Enter User Name..",'N');
		}
		else if(L_FE.getSource().equals(cl_dat.M_txtPASWD_pbst))
		   setMSG("Enter your Password ..",'N');
		else if(L_FE.getSource().equals(cl_dat.M_txtCURDT_pbst))
           setMSG("Enter Today's date ..",'N');
		else if(M_objSOURC.equals(cl_dat.M_cmbOPTN_pbst)&&cl_dat.M_cmbOPTN_pbst.getItemCount()>0){
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
				setMSG("Please Select an Option ",'N');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG("Adds a Record ",'N');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				setMSG("Modifies a Record ",'N');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Deletes a Record ",'N');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				setMSG("Enquires about a Record ",'N');
			//ADDED ON 02/04/2004 BY AAP FOR AUTHORISATION OPTION
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				setMSG("Authorizes a Request ",'N');
		}
		else if(M_objSOURC.equals(cl_dat.M_btnSAVE_pbst)&&!cl_dat.M_txtSTAT_pbst.getForeground().equals(Color.red))
		{
			if(cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("SAVE")||cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("CHANGE"))
				setMSG(" Saves a Record ",'N');
			else if(cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("DELETE"))
				setMSG(" Deletes a Record ",'N');
			else
				setMSG(" Executes the report ",'N');
		}
        else if(M_objSOURC.equals(cl_dat.M_btnHELP_pbst)&&!cl_dat.M_txtSTAT_pbst.getForeground().equals(Color.red))
                setMSG(" Help about the current Screen ",'N');
        else if(M_objSOURC.equals(cl_dat.M_btnEXIT_pbst)&&!cl_dat.M_txtSTAT_pbst.getForeground().equals(Color.red))
                setMSG(" Exits From the Current Screen ",'N');
		}catch(Exception e)
		{System.out.println("Error in fr_log Focus gained : "+e);}
	}
	/** Tasks : set Border and Forecolor for Buttons and menus
	 */
	public void focusLost(FocusEvent e)
	{
		JComponent L_SRCOBJ=(JComponent)e.getSource();
		L_SRCOBJ.setForeground(Color.black);
		if(L_SRCOBJ.getClass().toString().equals("class javax.swing.JMenu")||L_SRCOBJ.getClass().toString().equals("class javax.swing.JMenuItem"))
		{
			if(L_SRCOBJ.isEnabled()) L_SRCOBJ.setBorder(BorderFactory.createBevelBorder(0,this.getBackground(),this.getBackground(),this.getBackground(),this.getBackground()));
		}
		else
			if(L_SRCOBJ.isEnabled()) L_SRCOBJ.setBorder(BorderFactory.createLineBorder(Color.darkGray));

	}
	public void menuCanceled(MenuEvent e)
	{}
	/**Set default border for Menu	 */
	public void menuDeselected(MenuEvent e)
	{
		((JComponent) e.getSource()).setBorder(BorderFactory.createBevelBorder(0,this.getBackground(),this.getBackground(),this.getBackground(),this.getBackground()));
	}
	/**Set bevel border for Menu	 */
	public void menuSelected(MenuEvent e)
	{
		((JComponent) e.getSource()).setBorder(BorderFactory.createBevelBorder(0));
	}

//TO CLOSE CONNECTION AND TEMINATE APPLICATION
	/**Romve connection to DB <p>close application</p>
	 */
    private void clsAPPCON()
	{
		try
		{
			if(cl_dat.M_conSPDBA_pbst != null){
		        cl_dat.M_conSPDBA_pbst.rollback();
				cl_dat.M_conSPDBA_pbst.close();
			}
		}catch(Exception L_EX){
			System.out.println("Error in clsAPPCON : "+L_EX);
		}
	}
	/**Collect System Details from hashtable hstUSRRT	 */
	private boolean exeSPTRN(String LP_PRGCD){//COLLECTS SYSTEM DETAILS FROM BACKEND
	try{
		String[] L_STRTMP=(String[])hstUSRRT.get(LP_PRGCD);
		//strSYSLC_st = L_STRTMP[5];
        cl_dat.M_strYSTDT_pbst =new StringBuffer(L_STRTMP[6]).toString();
        cl_dat.M_strYEDDT_pbst =new StringBuffer(L_STRTMP[7]).toString();
        cl_dat.M_strEINFL_pbst =new StringBuffer(L_STRTMP[9]).toString();
	}
	catch(Exception L_E)
            {
		System.out.println("Error in exeSPTRN....."+L_E.toString());
	}
    return true;
	}
	/**Create Menu<p>Add Mouse, Menu & Focus Listener</P>	 */
	private JMenu crtMNU(String LP_MNUNAM, int LP_ACCKEY){
		JMenu L_MNU = new JMenu(LP_MNUNAM);
		L_MNU.setMnemonic(LP_ACCKEY);
		L_MNU.addMouseListener(this);
		L_MNU.addMenuListener(this);L_MNU.addFocusListener(this);
		mbrMNUBR.add(L_MNU);
		return L_MNU;
	}

	
	/**Create Menu<p>Add Mouse, Menu & Focus Listener</P>	 */
	private JMenu crtSUBMNU(String LP_MNUNAM, int LP_ACCKEY){
		JMenu L_MNU = new JMenu(LP_MNUNAM);
		L_MNU.setMnemonic(LP_ACCKEY);
		L_MNU.addMouseListener(this);
		L_MNU.addMenuListener(this);L_MNU.addFocusListener(this);
		//mbrSBMNUBR.add(L_MNU);
		return L_MNU;
	}
	
	/**Create Menuitem<p>Add Mouse, Menu & Focus Listener</P>	 */
	 private void crtMNUITM(JMenu L_MNUNM,String LP_ITMNAM,String LP_ACTCMD,char LP_ACCKEY){
		mitNWITM = new JMenuItem(LP_ITMNAM);		
		mitNWITM.setMnemonic(LP_ACCKEY);
		mitNWITM.setActionCommand(LP_ACTCMD);
		mitNWITM.addActionListener(this);
		mitNWITM.addMouseListener(this);
		L_MNUNM.add(mitNWITM);
    }
	/**Creates Menu for the user 	 */
	private void crtMENU(){
		mnuMASTR.setText("Masters");
		mnuMASTR.setMnemonic(KeyEvent.VK_M);
		mnuTRANS.setText("Transactions");
		mnuTRANS.setMnemonic(KeyEvent.VK_T);
		mnuREPRT.setText("Reports");
		mnuREPRT.setMnemonic(KeyEvent.VK_R);
		mnuQUERY.setText("Queries");
		mnuQUERY.setMnemonic(KeyEvent.VK_Q);
		mnuHSKPG.setText("Others");
		mnuHSKPG.setMnemonic(KeyEvent.VK_O);
		mnuEXIT.setText("Exit");
		mnuEXIT.setMnemonic(KeyEvent.VK_X);

		
		
		chkADDMNU();
		
		
		
		crtMNUITM(mnuEXIT,"Exit","FG_XXEXT",'X');
		String[] L_STRTMP=new String[1];
		String key="",L_MSTOLDCD="",mitTRNOLDCD="",mitRPTOLDCD="",mitHSKOLDCD="",mitOTHOLDCD="";
		boolean flag=true;
		for(int i=0;i<vtrPRGCD.size();i++)//ADDING MENU ITEMS
		{
			flag=true;
			L_STRTMP=(String[])hstUSRRT.get(vtrPRGCD.elementAt(i));
			key=vtrPRGCD.elementAt(i).toString();
			for(int j=0;j<i;j++)//CHECKING FOR REPEATED PRGCD
			{
				if(vtrPRGCD.elementAt(j).equals(vtrPRGCD.elementAt(i)))
				{
					flag=false;
					break;
				}
			}
			if(flag)
			{
			if(L_STRTMP[intPRGTP_fn].equals("ME"))
			{
				crtMNUITM1(L_STRTMP,key,mnuMEFG,"FG");
				crtMNUITM1(L_STRTMP,key,mnuMEHR,"HR");
				crtMNUITM1(L_STRTMP,key,mnuMEAD,"AD");
				crtMNUITM1(L_STRTMP,key,mnuMEPM,"PM");
				crtMNUITM1(L_STRTMP,key,mnuMEMR,"MR");
				crtMNUITM1(L_STRTMP,key,mnuMECO,"CO");
				crtMNUITM1(L_STRTMP,key,mnuMEMM,"MM");
				crtMNUITM1(L_STRTMP,key,mnuMEPR,"PR");
				crtMNUITM1(L_STRTMP,key,mnuMEQC,"QC");
				crtMNUITM1(L_STRTMP,key,mnuMESA,"SA");
				//crtMNUITM(mnuMASTR,L_STRTMP[intSYSCD_fn]+" : "+L_STRTMP[intPRGDS_fn],key,L_STRTMP[intPRGDS_fn].charAt(0));
				//** ADDING "UP" OPTION IF No. OF MENUITEMS IS MORE
				//if(mnuMASTR.getItemCount()>intMNULN_fn&&mitMSTUP==null)
				//{
				//	mnuMASTR.insert(mitMSTUP=new JMenuItem("UP"),0);
				//	mitMSTUP.addMouseListener(this);
				//	mitMSTUP.setEnabled(false);
				//	mitMSTUP.setForeground(Color.blue );
				//	mitMSTUP.setBorder(BorderFactory.createBevelBorder(0));
				//}
				L_MSTOLDCD=L_STRTMP[intSYSCD_fn];
			}
			else if(L_STRTMP[intPRGTP_fn].equals("TE"))

			{
				crtMNUITM1(L_STRTMP,key,mnuTEFG,"FG");
				crtMNUITM1(L_STRTMP,key,mnuTEHR,"HR");
				crtMNUITM1(L_STRTMP,key,mnuTEAD,"AD");
				crtMNUITM1(L_STRTMP,key,mnuTEPM,"PM");
				crtMNUITM1(L_STRTMP,key,mnuTEMR,"MR");
				crtMNUITM1(L_STRTMP,key,mnuTECO,"CO");
				crtMNUITM1(L_STRTMP,key,mnuTEMM,"MM");
				crtMNUITM1(L_STRTMP,key,mnuTEPR,"PR");
				crtMNUITM1(L_STRTMP,key,mnuTEQC,"QC");
				crtMNUITM1(L_STRTMP,key,mnuTESA,"SA");
				if(mnuTEMM.getItemCount()>intMNULN_fn&&mitTRNUP==null)
				{
					mnuTEMM.insert(mitTRNUP=new JMenuItem("UP"),0);
					mitTRNUP.addMouseListener(this);
					mitTRNUP.setEnabled(false);
					mitTRNUP.setForeground(Color.blue );
					mitTRNUP.setBorder(BorderFactory.createBevelBorder(0));
				}
				mitTRNOLDCD=L_STRTMP[intSYSCD_fn];
			}
			else if(L_STRTMP[intPRGTP_fn].equals("RP"))
			{
				crtMNUITM1(L_STRTMP,key,mnuRPFG,"FG");
				crtMNUITM1(L_STRTMP,key,mnuRPHR,"HR");
				crtMNUITM1(L_STRTMP,key,mnuRPAD,"AD");
				crtMNUITM1(L_STRTMP,key,mnuRPPM,"PM");
				crtMNUITM1(L_STRTMP,key,mnuRPMR,"MR");
				crtMNUITM1(L_STRTMP,key,mnuRPCO,"CO");
				crtMNUITM1(L_STRTMP,key,mnuRPMM,"MM");
				crtMNUITM1(L_STRTMP,key,mnuRPPR,"PR");
				crtMNUITM1(L_STRTMP,key,mnuRPQC,"QC");
				crtMNUITM1(L_STRTMP,key,mnuRPSA,"SA");
				//** ADDING "UP" OPTION IF No. OF MENUITEMS IS MORE
				if(mnuRPMM.getItemCount()>intMNULN_fn&&mitRPTUP==null)
				{
					mnuRPMM.insert(mitRPTUP=new JMenuItem("UP"),0);
					mitRPTUP.addMouseListener(this);
					mitRPTUP.setEnabled(false);
					mitRPTUP.setForeground(Color.blue );
					mitRPTUP.setBorder(BorderFactory.createBevelBorder(0));
				}
				if(mnuRPMR.getItemCount()>intMNULN_fn&&mitRPTUP1==null)
				{
					mnuRPMR.insert(mitRPTUP1=new JMenuItem("UP"),0);
					mitRPTUP1.addMouseListener(this);
					mitRPTUP1.setEnabled(false);
					mitRPTUP1.setForeground(Color.blue );
					mitRPTUP1.setBorder(BorderFactory.createBevelBorder(0));
				}
				mitRPTOLDCD=L_STRTMP[intSYSCD_fn];
			}
			else if(L_STRTMP[intPRGTP_fn].equals("HK"))
			{

				crtMNUITM1(L_STRTMP,key,mnuHKFG,"FG");
				crtMNUITM1(L_STRTMP,key,mnuHKHR,"HR");
				crtMNUITM1(L_STRTMP,key,mnuHKAD,"AD");
				crtMNUITM1(L_STRTMP,key,mnuHKPM,"PM");
				crtMNUITM1(L_STRTMP,key,mnuHKMR,"MR");
				crtMNUITM1(L_STRTMP,key,mnuHKCO,"CO");
				crtMNUITM1(L_STRTMP,key,mnuHKMM,"MM");
				crtMNUITM1(L_STRTMP,key,mnuHKPR,"PR");
				crtMNUITM1(L_STRTMP,key,mnuHKQC,"QC");
				crtMNUITM1(L_STRTMP,key,mnuHKSA,"SA");
				//crtMNUITM(mnuHSKPG,L_STRTMP[intSYSCD_fn]+" : "+L_STRTMP[intPRGDS_fn],key,L_STRTMP[intPRGDS_fn].charAt(0));
				//** ADDING "UP" OPTION IF No. OF MENUITEMS IS MORE
				//if(mnuHSKPG.getItemCount()>intMNULN_fn&&mitHSKUP==null)
				//{
				//	mnuHSKPG.insert(mitHSKUP=new JMenuItem("UP"),0);
				//	mitHSKUP.addMouseListener(this);
				//	mitHSKUP.setEnabled(false);
				//	mitHSKUP.setForeground(Color.blue );
				//	mitHSKUP.setBorder(BorderFactory.createBevelBorder(0));
				//}
				mitHSKOLDCD=L_STRTMP[intSYSCD_fn];
			}
			else
			{
				crtMNUITM1(L_STRTMP,key,mnuQRFG,"FG");
				crtMNUITM1(L_STRTMP,key,mnuQRHR,"HR");
				crtMNUITM1(L_STRTMP,key,mnuQRAD,"AD");
				crtMNUITM1(L_STRTMP,key,mnuQRPM,"PM");
				crtMNUITM1(L_STRTMP,key,mnuQRMR,"MR");
				crtMNUITM1(L_STRTMP,key,mnuQRCO,"CO");
				crtMNUITM1(L_STRTMP,key,mnuQRMM,"MM");
				crtMNUITM1(L_STRTMP,key,mnuQRQC,"QC");
				crtMNUITM1(L_STRTMP,key,mnuQRPR,"PR");
				crtMNUITM1(L_STRTMP,key,mnuQRSA,"SA");
				//crtMNUITM(mnuQUERY,L_STRTMP[intSYSCD_fn]+" : "+L_STRTMP[intPRGDS_fn],key,L_STRTMP[intPRGDS_fn].charAt(0));
				//** ADDING "UP" OPTION IF No. OF MENUITEMS IS MORE
				//if(mnuQUERY.getItemCount()>intMNULN_fn&&mitOTHUP==null)
				//{
				//	mnuQUERY.insert(mitOTHUP=new JMenuItem("UP"),0);
				//	mitOTHUP.addMouseListener(this);
				//	mitOTHUP.setEnabled(false);
				//	mitOTHUP.setForeground(Color.blue );
				//	mitOTHUP.setBorder(BorderFactory.createBevelBorder(0));
				//}
				mitOTHOLDCD=L_STRTMP[intSYSCD_fn];
			}
			}
		}
		//if(mitMSTUP!=null)//IF MENU COUNT > MENU LENGTH, ADD DOWN MENU AND HIDE ADDITIONAL MENU
		//{
		//	mnuMASTR.add(mitMSTDN=new JMenuItem("Down"));
		//	mitMSTDN.addMouseListener(this);
		//	mitMSTDN.setForeground(Color.blue );
		//	mitMSTDN.setBorder(BorderFactory.createBevelBorder(0));
		//	for(int i=1;i<mnuMASTR.getItemCount()-1;i++)
		//	{
		//		if(mnuMASTR.isMenuComponent(mnuMASTR.getItem(i))&&i>intMNULN_fn-2)
		//			mnuMASTR.getItem(i).setVisible(false);
		//		mnuMASTR.updateUI();
		//	}
		//}
		//addSeparator(mnuMASTR);//ADD SYSTEM WISE SEPARATORS
		if(mitTRNUP!=null)//IF MENU COUNT > MENU LENGTH, ADD DOWN MENU AND HIDE ADDITIONAL MENU
		{
			mnuTEMM.add(mitTRNDN=new JMenuItem("Down"));
			mitTRNDN.addMouseListener(this);
			mitTRNDN.setForeground(Color.blue );
			mitTRNDN.setBorder(BorderFactory.createBevelBorder(0));
			for(int i=1;i<mnuTEMM.getItemCount()-1;i++)
			{
				if(mnuTEMM.isMenuComponent(mnuTEMM.getItem(i))&&i>intMNULN_fn-2)
					mnuTEMM.getItem(i).setVisible(false);
			}
		}
		addSeparator(mnuTEMM);//ADD SYSTEM WISE SEPARATORS
		if(mitRPTUP!=null)//IF MENU COUNT > MENU LENGTH, ADD DOWN MENU AND HIDE ADDITIONAL MENU
		{
			mnuRPMM.add(mitRPTDN=new JMenuItem("Down"));
			mitRPTDN.addMouseListener(this);
			mitRPTDN.setForeground(Color.blue );
			mitRPTDN.setBorder(BorderFactory.createBevelBorder(0));
			for(int i=1;i<mnuRPMM.getItemCount()-1;i++)
			{
				if(mnuRPMM.isMenuComponent(mnuRPMM.getItem(i))&&i>intMNULN_fn-2)
					mnuRPMM.getItem(i).setVisible(false);
			}
		}
		addSeparator(mnuRPMM);//ADD SYSTEM WISE SEPARATORS

		if(mitRPTUP1!=null)//IF MENU COUNT > MENU LENGTH, ADD DOWN MENU AND HIDE ADDITIONAL MENU
		{
			mnuRPMR.add(mitRPTDN1=new JMenuItem("Down"));
			mitRPTDN1.addMouseListener(this);
			mitRPTDN1.setForeground(Color.blue );
			mitRPTDN1.setBorder(BorderFactory.createBevelBorder(0));
			for(int i=1;i<mnuRPMR.getItemCount()-1;i++)
			{
				if(mnuRPMR.isMenuComponent(mnuRPMR.getItem(i))&&i>intMNULN_fn-2)
					mnuRPMR.getItem(i).setVisible(false);
			}
		}
		addSeparator(mnuRPMR);//ADD SYSTEM WISE SEPARATORS

		//if(mitOTHUP!=null)//IF MENU COUNT > MENU LENGTH, ADD DOWN MENU AND HIDE ADDITIONAL MENU
		//{
		//	mnuQUERY.add(mitOTHDN=new JMenuItem("Down"));
		//	mitOTHDN.addMouseListener(this);
		//	mitOTHDN.setForeground(Color.blue );
		//	mitOTHDN.setBorder(BorderFactory.createBevelBorder(0));
		//	for(int i=1;i<mnuQUERY.getItemCount()-1;i++)
		//	{
		//		if(mnuQUERY.isMenuComponent(mnuQUERY.getItem(i))&&i>intMNULN_fn-2)
		//			mnuQUERY.getItem(i).setVisible(false);
		//	}
		//}
		//****addSeparator(mnuQUERY);//ADD SYSTEM WISE SEPARATORS
		//if(mitHSKUP!=null)//IF MENU COUNT > MENU LENGTH, ADD DOWN MENU AND HIDE ADDITIONAL MENU
		//{
		//	mnuHSKPG.add(mitHSKDN=new JMenuItem("Down"));
		//	mitHSKDN.addMouseListener(this);
		//	mitHSKDN.setForeground(Color.blue );
		//	mitHSKDN.setBorder(BorderFactory.createBevelBorder(0));
		//	for(int i=1;i<mnuHSKPG.getItemCount()-1;i++)
		//	{
		//		if(mnuHSKPG.isMenuComponent(mnuHSKPG.getItem(i))&&i>intMNULN_fn-2)
		//			mnuHSKPG.getItem(i).setVisible(false);
		//	}
		//}
		//addSeparator(mnuHSKPG);//ADD SYSTEM WISE SEPARATORS
		mnuMASTR.setVisible(true);
		mnuTRANS.setVisible(true);
		mnuREPRT.setVisible(true);
		mnuHSKPG.setVisible(true);
		mnuQUERY.setVisible(true);
		mnuEXIT.setVisible(true);
		mnuMASTR.requestFocus();
		mnuMASTR.doClick();//SHOW MENU POPED UP
		setMSG("Select a System to work with ..",'N');
	}
	
	private void chkADDMNU()
	{
		try
		{
			String L_strSYSCD = "", L_strPRGTP = "";
			String L_strSQLQRY = "select distinct PP_PRGTP,PP_SYSCD  from sa_ppmst where pp_prgcd in (select ppr_prgcd from sa_pprtr where ppr_usrtp in (select ust_usrtp from sa_ustrn where ust_usrct = '"+cl_dat.M_strUSRCT_pbst+"' and ust_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ust_usrcd='"+cl_dat.M_strUSRCD_pbst+"')) order by pp_prgtp,pp_syscd";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET.next() && L_rstRSSET != null)
			{
				while(true)
				{
					if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("FG"))
					 	setMNUTXT(mnuTRANS,mnuTEFG,"FG");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("HR"))
						setMNUTXT(mnuTRANS,mnuTEHR,"HR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("AD"))
						setMNUTXT(mnuTRANS,mnuTEAD,"AD");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("PM"))
						setMNUTXT(mnuTRANS,mnuTEPM,"PM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("MR"))
						setMNUTXT(mnuTRANS,mnuTEMR,"MR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("CO"))
						setMNUTXT(mnuTRANS,mnuTECO,"CO");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("MM"))
						setMNUTXT(mnuTRANS,mnuTEMM,"MM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("PR"))
						setMNUTXT(mnuTRANS,mnuTEPR,"PR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("QC"))
						setMNUTXT(mnuTRANS,mnuTEQC,"QC");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("TE") && L_rstRSSET.getString("PP_SYSCD").equals("SA"))
						setMNUTXT(mnuTRANS,mnuTESA,"SA");
					
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("FG"))
					 setMNUTXT(mnuMASTR,mnuMEFG,"FG");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("HR"))
						setMNUTXT(mnuMASTR,mnuMEHR,"HR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("AD"))
						setMNUTXT(mnuMASTR,mnuMEAD,"AD");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("PM"))
						setMNUTXT(mnuMASTR,mnuMEPM,"PM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("MR"))
						setMNUTXT(mnuMASTR,mnuMEMR,"MR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("CO"))
						setMNUTXT(mnuMASTR,mnuMECO,"CO");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("MM"))
						setMNUTXT(mnuMASTR,mnuMEMM,"MM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("PR"))
						setMNUTXT(mnuMASTR,mnuMEPR,"PR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("QC"))
						setMNUTXT(mnuMASTR,mnuMEQC,"QC");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("ME") && L_rstRSSET.getString("PP_SYSCD").equals("SA"))
						setMNUTXT(mnuMASTR,mnuMESA,"SA");
					
					
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("FG"))
					 setMNUTXT(mnuREPRT,mnuRPFG,"FG");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("HR"))
						setMNUTXT(mnuREPRT,mnuRPHR,"HR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("AD"))
						setMNUTXT(mnuREPRT,mnuRPAD,"AD");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("PM"))
						setMNUTXT(mnuREPRT,mnuRPPM,"PM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("MR"))
						setMNUTXT(mnuREPRT,mnuRPMR,"MR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("CO"))
						setMNUTXT(mnuREPRT,mnuRPCO,"CO");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("MM"))
						setMNUTXT(mnuREPRT,mnuRPMM,"MM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("PR"))
						setMNUTXT(mnuREPRT,mnuRPPR,"PR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("QC"))
						setMNUTXT(mnuREPRT,mnuRPQC,"QC");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("RP") && L_rstRSSET.getString("PP_SYSCD").equals("SA"))
						setMNUTXT(mnuREPRT,mnuRPSA,"SA");
					
					
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("FG"))
					 setMNUTXT(mnuQUERY,mnuQRFG,"FG");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("HR"))
						setMNUTXT(mnuQUERY,mnuQRHR,"HR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("AD"))
						setMNUTXT(mnuQUERY,mnuQRAD,"AD");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("PM"))
						setMNUTXT(mnuQUERY,mnuQRPM,"PM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("MR"))
						setMNUTXT(mnuQUERY,mnuQRMR,"MR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("CO"))
						setMNUTXT(mnuQUERY,mnuQRCO,"CO");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("MM"))
						setMNUTXT(mnuQUERY,mnuQRMM,"MM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("PR"))
						setMNUTXT(mnuQUERY,mnuQRPR,"PR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("QC"))
						setMNUTXT(mnuQUERY,mnuQRQC,"QC");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("QR") && L_rstRSSET.getString("PP_SYSCD").equals("SA"))
						setMNUTXT(mnuQUERY,mnuQRSA,"SA");
					
					
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("FG"))
					 setMNUTXT(mnuHSKPG,mnuHKFG,"FG");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("HR"))
						setMNUTXT(mnuHSKPG,mnuHKHR,"HR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("AD"))
						setMNUTXT(mnuHSKPG,mnuHKAD,"AD");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("PM"))
						setMNUTXT(mnuHSKPG,mnuHKPM,"PM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("MR"))
						setMNUTXT(mnuHSKPG,mnuHKMR,"MR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("CO"))
						setMNUTXT(mnuHSKPG,mnuHKCO,"CO");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("MM"))
						setMNUTXT(mnuHSKPG,mnuHKMM,"MM");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("PR"))
						setMNUTXT(mnuHSKPG,mnuHKPR,"PR");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("QC"))
						setMNUTXT(mnuHSKPG,mnuHKQC,"QC");
					else if(L_rstRSSET.getString("PP_PRGTP").equals("HK") && L_rstRSSET.getString("PP_SYSCD").equals("SA"))
						setMNUTXT(mnuHSKPG,mnuHKSA,"SA");
					
					
					
					
					if(!L_rstRSSET.next())
						break;
				}
			}
			
		}catch(Exception L_SE)
        {
		   System.out.println("Error in chkADDMNU: "+L_SE.toString());
		}
	}			

	
	/**
	 */
	private void crtMNUITM1(String[] LP_STRTMP,String LP_KEY,JMenu LP_MNUNM,String LP_SYSCD)
	{
		try
		{
        		if(!LP_STRTMP[intSYSCD_fn].toString().trim().equals(LP_SYSCD))
				return;
			crtMNUITM(LP_MNUNM,LP_STRTMP[intSYSCD_fn]+" : "+LP_STRTMP[intPRGDS_fn],LP_KEY,LP_STRTMP[intPRGDS_fn].charAt(0));
			//if(LP_MNUNM.getItemCount()>intMNULN_fn&&LP_mitXXXUP==null)
			//{
			//	LP_MNUNM.insert(LP_mitXXXUP=new JMenuItem("UP"),0);
			//	LP_MNUNM.addMouseListener(this);
			//	LP_MNUNM.setEnabled(false);
			//	LP_MNUNM.setForeground(Color.blue );
			//	LP_MNUNM.setBorder(BorderFactory.createBevelBorder(0));
			//}
		}catch(Exception L_SE)
        {
		   System.out.println("Error in crtMNUITM1: "+L_SE.toString());
		}
}
	
	private void setMNUTXT(JMenu LP_MNUMM, JMenu LP_MNUSB,String LP_SYSCD)
	{
		LP_MNUMM.add(LP_MNUSB);
		if(LP_SYSCD.equals("MM"))
			LP_MNUSB.setText("Material Management");
		else if(LP_SYSCD.equals("CO"))
			LP_MNUSB.setText("Masters");
		else if(LP_SYSCD.equals("HR"))
			LP_MNUSB.setText("Human Resources");
		else if(LP_SYSCD.equals("AD"))
			LP_MNUSB.setText("Administration");
		else if(LP_SYSCD.equals("PM"))
			LP_MNUSB.setText("Plant Maintenance");
		else if(LP_SYSCD.equals("QC"))
			LP_MNUSB.setText("Quality Control");
		else if(LP_SYSCD.equals("PR"))
			LP_MNUSB.setText("Production");
		else if(LP_SYSCD.equals("MR"))
			LP_MNUSB.setText("Marketing");
		else if(LP_SYSCD.equals("FG"))
			LP_MNUSB.setText("Finished Goods");
		else if(LP_SYSCD.equals("SA"))
			LP_MNUSB.setText("System Administration");
	}
	
	/**Removes content of the menu	 */
	private void exeCLRMNU(){
		mnuMASTR.removeAll();
		mnuTRANS.removeAll();
		mnuREPRT.removeAll();
		mnuHSKPG.removeAll();
		mnuQUERY.removeAll();
		mnuEXIT.removeAll();

		mnuMASTR.setVisible(false);
		mnuTRANS.setVisible(false);
		mnuREPRT.setVisible(false);
		mnuHSKPG.setVisible(false);
		mnuQUERY.setVisible(false);
		mnuEXIT.setVisible(false);
		
		mnuTEFG.removeAll();
		mnuTEHR.removeAll();
		mnuTEMR.removeAll();
		mnuTECO.removeAll();
		mnuTEMM.removeAll();
		mnuTEPR.removeAll();
		mnuTEQC.removeAll();
		mnuTESA.removeAll();
	
		mnuMEFG.removeAll();
		mnuMEHR.removeAll();
		mnuMEMR.removeAll();
		mnuMECO.removeAll();
		mnuMEMM.removeAll();
		mnuMEPR.removeAll();
		mnuMEQC.removeAll();
		mnuMESA.removeAll();
				
	
		mnuRPFG.removeAll();
		mnuRPHR.removeAll();
		mnuRPMR.removeAll();
		mnuRPCO.removeAll();
		mnuRPMM.removeAll();
		mnuRPPR.removeAll();
		mnuRPQC.removeAll();
		mnuRPSA.removeAll();
				
	
		mnuQRFG.removeAll();
		mnuQRHR.removeAll();
		mnuQRMR.removeAll();
		mnuQRCO.removeAll();
		mnuQRMM.removeAll();
		mnuQRPR.removeAll();
		mnuQRQC.removeAll();
		mnuQRSA.removeAll();
				
		mnuHKFG.removeAll();
		mnuHKHR.removeAll();
		mnuHKMR.removeAll();
		mnuHKCO.removeAll();
		mnuHKMM.removeAll();
		mnuHKPR.removeAll();
		mnuHKQC.removeAll();
		mnuHKSA.removeAll();
			
		mitMSTDN=null;mitTRNDN=null;mitRPTDN=null;mitRPTDN1=null;mitHSKDN=null;mitOTHDN=null;
		mitMSTUP=null;mitTRNUP=null;mitRPTUP=null;mitRPTUP1=null;mitHSKUP=null;mitOTHUP=null;
	}


	/**Validates user and user rights
	   * <P>Puts data in respective Hasshtable & Vectors for future references</P>
	   * <P>If contineous three failed attempts are made to login, disables the user and stores IP,name of system, user name, date and time</P>
	   * <p>If user name is same as user code, forces user to set password other than user code</p>
	   */
	private void chkUSRVLD0()
        {
		SimpleDateFormat fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");

		setMSG("Validating User .. Please wait ..",'N');
		fr_pwd obj=null;
		// Validates the UserName Password and return User Code for a valid user otherwise it returns 0
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			//System.out.println("strPRGCD "+strPRGCD);
			cl_dat.M_strUSRPW_pbst=cl_ProcessPassword.encode(new String(cl_dat.M_txtPASWD_pbst.getPassword()));

			strSQLQRY = "select US_USRPW,ISNULL(US_EMPCD,'') US_EMPCD,US_PWMFL,US_STSFL,UST_USRTP,US_EMLRF,current_timestamp curtm,GETDATE() us_curdt,cast(GETDAte() as time) us_curtm FROM SA_USMST,SA_USTRN "
			+" where  US_usrct = '"+cl_dat.M_strUSRCT_pbst+"' and US_USRCT = UST_USRCT and UST_USRCD=US_USRCD and US_USRCD='"+cl_dat.M_txtUSRCD_pbst.getText().toUpperCase() + "'";
            //System.out.println(strSQLQRY);
			//if(rstRSSET!=null)
			//	rstRSSET.close();
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
			boolean flag=true,fl_STPWD=true,L_flgUSRCD=false;
			if(L_rstRSSET!=null)
			{
				//ADDED BY APP 20/10/2004 for SYSTEM YEAR DATES CORRECT REPRESENTATION
				//flag to check whether user name is correct
				cl_dat.M_strUSRTP_pbst ="";
				cl_dat.M_strEMPNO_pbst = "";
                while(L_rstRSSET.next())
				{
					if(flag)
					{L_flgUSRCD=true;
						//tmrCLOCK= new java.util.Timer();
						//tmrCLOCK.scheduleAtFixedRate(new cl_clock(), 1l,1000l);
						datCURDT=fmtDBSTM.parse(L_rstRSSET.getString("curtm").substring(0,19));
						calCLOCK.setTime(datCURDT);
						if(nvlSTRVL(L_rstRSSET.getString("US_STSFL"),"").equals("D"))
						{
							setMSG("USER IS DISABLED.. Please contact System Administrator.",'E');
							return;
						}
						cl_dat.M_strEMPNO_pbst = L_rstRSSET.getString("US_EMPCD").trim();
						if(!L_rstRSSET.getString("US_USRPW").trim().equalsIgnoreCase(cl_dat.M_strUSRPW_pbst.trim()))
						//if(!L_rstRSSET.getString("US_USRPW").trim().equalsIgnoreCase("ZY"))
						{
							intLGCNT_fn++;
							if(intLGCNT_fn==3)
							{//DISABLE USER AFTER THREE UNSUCCESSFUL ATTEMPTS
								JOptionPane.showInternalMessageDialog(this.getContentPane(),"Wrong Password provided. User is disabled.\nPlease contact System Administrator before next login","Application Terminating ..",JOptionPane.ERROR_MESSAGE);
								strSQLQRY="UPDATE SA_USMST SET US_STSFL='D', "
									+"US_DSBID='LOGIN:"+System.getProperty("user.name")
									+";IP:"+InetAddress.getLocalHost().getHostAddress()
									+";PC:"+InetAddress.getLocalHost().getHostName()
									+";DT:"+L_rstRSSET.getString("us_curdt")
									+";TM:"+L_rstRSSET.getString("us_curtm")
									+ "' WHERE US_usrct = '"+cl_dat.M_strUSRCT_pbst+"' and US_USRCD='"+cl_dat.M_txtUSRCD_pbst.getText().toUpperCase()+"'";
								cl_dat.exeSQLUPD(strSQLQRY ,"setLCLUPD");
								cl_dat.exeDBCMT("chkUSRVLD0");
								System.out.println("login attempts expired");
								clsAPPCON();//CLOSE APPLICATION
								System.exit(0);
							}
							else
							{
								setMSG("Wrong Password..  Try again ..",'E');
								cl_dat.M_txtPASWD_pbst.requestFocus();
								break;
							}
						}
						else
						{
							if(flag&&(!nvlSTRVL(L_rstRSSET.getString("US_STSFL"),"").equals("D"))&&L_rstRSSET.getString("US_USRPW").equals(cl_ProcessPassword.encode(cl_dat.M_txtUSRCD_pbst.getText().toUpperCase())))
							{
								fl_STPWD=false;
								obj = new fr_pwd();
								setMSG("User must change Password in first login ..",'N');
								cl_dat.M_pnlFRBTM_pbst.add("screen", obj);
								cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen");
								getContentPane().validate();
								update(getGraphics());
							}
							intLGCNT_fn=0;//INTIALISING LOGIN ATTEMPT COUNT
							cl_dat.M_strSYSEML_pbst = L_rstRSSET.getString("US_EMLRF");
							cl_dat.M_strLOGDT_pbst = fmtLCLTM.format(L_rstRSSET.getDate("us_curdt")).substring(0,10);
							cl_dat.M_txtCURDT_pbst.setText(cl_dat.M_strLOGDT_pbst);
							cl_dat.M_txtPASWD_pbst.setEnabled(false);
							cl_dat.M_btnSETPW_pbst.setEnabled(true);
							//btnINTRN.setEnabled(false);
							btnINTRN.setVisible(false);
							cl_dat.M_txtUSER_pbst.setText(cl_dat.M_txtUSRCD_pbst.getText());
							cl_dat.M_txtDATE_pbst.setText(cl_dat.M_strLOGDT_pbst);
							getContentPane().validate();
							//if(thrCMPCD!=null)
							//	thrCMPCD.join();
							if(obj==null)
							{
								if(cl_dat.M_cmbCMPCD_pbst.getItemCount()==2)
								{
									cl_dat.M_cmbCMPCD_pbst.setSelectedIndex(1);
									cl_dat.M_txtCURDT_pbst.requestFocus();
								}
								else
								{	
									cl_dat.M_cmbCMPCD_pbst.setEnabled(true);
									cl_dat.M_cmbCMPCD_pbst.requestFocus();
								}	
								//cl_dat.M_txtCURDT_pbst.requestFocus();
							}
						}
					}
					flag=false;
					// Added on 20 oct for maintaining user type
					//if(cl_dat.M_strUSRTP_pbst.indexOf(rstRSSET.getString("UST_USRTP")) < 0)
					//{    
                    //    if(cl_dat.M_strUSRTP_pbst.length() ==0)
                    //        cl_dat.M_strUSRTP_pbst +=rstRSSET.getString("UST_USRTP");
                    //    else
                    //    cl_dat.M_strUSRTP_pbst +="_"+rstRSSET.getString("UST_USRTP");
                    //}
				}
			}
			else
			{
				setMSG("Wrong UserName.",'E');
				cl_dat.M_txtUSRCD_pbst.setEnabled(true);
				cl_dat.M_txtUSRCD_pbst.requestFocus();
			}
			L_rstRSSET.close();
			String L_STRTMP = "";
			L_rstRSSET=cl_dat.exeSQLQRY1("select current_timestamp from sa_spmst");
			if(L_rstRSSET.next())
			{
				datCURDT=fmtDBSTM.parse(L_rstRSSET.getString(1).substring(0,19));
				calCLOCK.setTime(datCURDT);
				L_STRTMP=fmtLCLTM.format(calCLOCK.getTime());
				cl_dat.M_txtCLKDT_pbst.setText(L_STRTMP.substring(0,10));
				cl_dat.M_txtCLKTM_pbst.setText(L_STRTMP.substring(10,15));
			}
			L_rstRSSET.close();
			if(!L_flgUSRCD)
			{
				setMSG("Wrong UserName.",'E');
				cl_dat.M_txtUSRCD_pbst.setEnabled(true);
				cl_dat.M_txtUSRCD_pbst.requestFocus();
			}
			L_rstRSSET.close();
		}catch(Exception L_SE)
                {
		   System.out.println("Error in chkUSRVLD0 : "+L_SE.toString());
		}
	}
	
	
	
	/**Validates user and user rights
	   * <P>Puts data in respective Hasshtable & Vectors for future references</P>
	   * <P>If contineous three failed attempts are made to login, disables the user and stores IP,name of system, user name, date and time</P>
	   * <p>If user name is same as user code, forces user to set password other than user code</p>
	   */
	private void chkUSRVLD1()
        {
		SimpleDateFormat fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");

		setMSG("Validating User .. Please wait ..",'N');
		fr_pwd obj=null;
		// Validates the UserName Password and return User Code for a valid user otherwise it returns 0
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			//System.out.println("strPRGCD "+strPRGCD);
			cl_dat.M_strUSRPW_pbst=cl_ProcessPassword.encode(new String(cl_dat.M_txtPASWD_pbst.getPassword()));

			strSQLQRY = "(select US_USRPW,US_PWMFL,US_STSFL,UST_USRTP,US_EMLRF,current_timestamp curtm,GETDATE() us_curdt,cast(GETDAte() as time) us_curtm,PPR_SYSCD,PPR_SBSCD,PPR_PRGCD,SP_SYSNM,SP_SYSLC,SP_DTBLB,  SP_YSTDT, SP_YENDT,PP_PRGDS,PP_VERNO,PP_PRGLC,PP_CNVFL,PP_EINFL,CMT_SHRDS,ISNULL(CMT_CHP01,' ') CMT_CHP01,ISNULL(CMT_CHP02,' ') CMT_CHP02,PPR_USRTP,PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,PPR_AUTFL,PP_PRGTP,SP_SYSCD,ISNULL(PP_ORDBY,PP_PRGCD) PP_PRGCD FROM SA_SPMST,SA_PPMST,SA_USMST,SA_USTRN,SA_PPRTR,CO_CDTRN "
			+" where PP_SYSCD=SP_SYSCD and PPR_PRGCD=PP_PRGCD and US_usrct = '"+cl_dat.M_strUSRCT_pbst+"' and US_USRCT = UST_USRCT and UST_USRCD=US_USRCD and PPR_USRTP=UST_USRTP and UST_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and CMT_CODCD=PPR_SBSCD and CMT_CGMTP = 'MST' and CMT_CGSTP='COXXSBS' and US_USRCD='"+cl_dat.M_txtUSRCD_pbst.getText().toUpperCase() + "')  order by SP_SYSCD,PP_PRGCD ";
			//+" union (select US_USRPW,US_PWMFL,US_STSFL,UST_USRTP,US_EMLRF,current_timestamp curtm,GETDATE() us_curdt,current_time us_curtm,PPR_SYSCD,PPR_SBSCD,PPR_PRGCD,SP_SYSNM,SP_SYSLC,SP_DTBLB,  SP_YSTDT, SP_YENDT,PP_PRGDS,PP_VERNO,PP_PRGLC,PP_CNVFL,PP_EINFL,CMT_SHRDS,ISNULL(CMT_CHP01,' ') CMT_CHP01,ISNULL(CMT_CHP02,' ') CMT_CHP02,PPR_USRTP,PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,PPR_AUTFL,PP_PRGTP,SP_SYSCD,ISNULL(PP_ORDBY,PP_PRGCD) PP_PRGCD FROM SA_SPMST,SA_PPMST,SA_USMST,SA_USTRN,SA_PPRTR,CO_CDTRN "
			//+" where PP_SYSCD=SP_SYSCD and PPR_PRGCD=PP_PRGCD and UST_USRCD=US_USRCD and PPR_USRTP=UST_USRTP and CMT_CODCD=PPR_SBSCD and CMT_CGMTP = 'MST' and CMT_CGSTP='COXXSBS' and US_USRCD='"+cl_dat.M_txtUSRCD_pbst.getText().toUpperCase() + "'  and ISNULL(PP_CNVFL,'X')='Y') order by SP_SYSCD,PP_PRGCD ";

			//strSQLQRY = "select US_USRPW,US_PWMFL,US_STSFL,UST_USRTP,US_EMLRF,current_timestamp curtm,GETDATE() us_curdt,current_time us_curtm,PPR_SYSCD,PPR_SBSCD,PPR_PRGCD,SP_SYSNM,SP_SYSLC,SP_DTBLB,  SP_YSTDT, SP_YENDT,PP_PRGDS,PP_VERNO,PP_PRGLC,CMT_SHRDS,CMT_CHP01,CMT_CHP02,PPR_USRTP,PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,PPR_AUTFL,PP_PRGTP,SP_SYSCD,ISNULL(PP_ORDBY,PP_PRGCD) PP_PRGCD FROM SA_SPMST,SA_PPMST,SA_USMST,SA_USTRN,SA_PPRTR,CO_CDTRN "
			//+" where PP_SYSCD=SP_SYSCD and PPR_PRGCD=PP_PRGCD and UST_USRCD=US_USRCD and PPR_USRTP=UST_USRTP and CMT_CODCD=PPR_SBSCD and CMT_CGMTP = 'SYS' and CMT_CGSTP='COXXSBS' and US_USRCD='"+cl_dat.M_txtUSRCD_pbst.getText().toUpperCase() + "'  order by SP_SYSCD,PP_PRGCD ";
            //System.out.println(strSQLQRY);
			if(rstRSSET!=null)
				rstRSSET.close();
			rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			boolean flag=true,fl_STPWD=true,L_flgUSRCD=false;
	        exeCLRMNU();
			hstUSRRT.clear();
			vtrPRGCD.clear();
			vtrSBSCD.clear();
			vtrSHRDS.clear();
			vtrCHP01.clear();
			vtrCHP02.clear();
			vtrADDFL.clear();
			vtrMODFL.clear();
			vtrDELFL.clear();
			vtrENQFL.clear();
			vtrAUTFL.clear();
			if(rstRSSET!=null)
			{
				//hstUSRRT=new Hashtable<String,String[]>(10,0.2f);
				//vtrPRGCD=new Vector<String>(10,2);
				//vtrSBSCD=new Vector<String>(10,2);
				//vtrSHRDS=new Vector<String>(10,2);
				//vtrCHP01=new Vector<String>(10,2);
				//vtrCHP02=new Vector<String>(10,2);
				//vtrADDFL=new Vector<String>(10,2);
				//vtrMODFL=new Vector<String>(10,2);
				//vtrDELFL=new Vector<String>(10,2);
				//vtrENQFL=new Vector<String>(10,2);
				//vtrAUTFL=new Vector<String>(10,2);//ADDED ON 02/04/2004 BY AAP FOR AUTHORISATION OPTION
				//ADDED BY APP 20/10/2004 for SYSTEM YEAR DATES CORRECT REPRESENTATION
				//flag to check whether user name is correct
				cl_dat.M_strUSRTP_pbst ="";
                while(rstRSSET.next())
				{
					if(flag)
					{L_flgUSRCD=true;
						tmrCLOCK= new java.util.Timer();
						tmrCLOCK.scheduleAtFixedRate(new cl_clock(), 1l,1000l);
						datCURDT=fmtDBSTM.parse(rstRSSET.getString("curtm").substring(0,19));
						calCLOCK.setTime(datCURDT);
						if(nvlSTRVL(rstRSSET.getString("US_STSFL"),"").equals("D"))
						{
							setMSG("USER IS DISABLED.. Please contact System Administrator.",'E');
							return;
						}
						if(!rstRSSET.getString("US_USRPW").trim().equalsIgnoreCase(cl_dat.M_strUSRPW_pbst.trim()))
						//if(!rstRSSET.getString("US_USRPW").trim().equalsIgnoreCase("ZY"))
						{
							intLGCNT_fn++;
							if(intLGCNT_fn==3)
							{//DISABLE USER AFTER THREE UNSUCCESSFUL ATTEMPTS
								JOptionPane.showInternalMessageDialog(this.getContentPane(),"Wrong Password provided. User is disabled.\nPlease contact System Administrator before next login","Application Terminating ..",JOptionPane.ERROR_MESSAGE);
								strSQLQRY="UPDATE SA_USMST SET US_STSFL='D', "
									+"US_DSBID='LOGIN:"+System.getProperty("user.name")
									+";IP:"+InetAddress.getLocalHost().getHostAddress()
									+";PC:"+InetAddress.getLocalHost().getHostName()
									+";DT:"+rstRSSET.getString("us_curdt")
									+";TM:"+rstRSSET.getString("us_curtm")
									+ "' WHERE us_usrct = '"+cl_dat.M_strUSRCT_pbst+"' and US_USRCD='"+cl_dat.M_txtUSRCD_pbst.getText().toUpperCase()+"'";
								cl_dat.exeSQLUPD(strSQLQRY ,"setLCLUPD");
								cl_dat.exeDBCMT("chkUSRVLD1");
								System.out.println("login attempts expired");
								clsAPPCON();//CLOSE APPLICATION
								System.exit(0);
							}
							else
							{
								setMSG("Wrong Password..  Try again ..",'E');
								cl_dat.M_txtPASWD_pbst.requestFocus();
								break;
							}
						}
						else
						{
							if(flag&&(!nvlSTRVL(rstRSSET.getString("US_STSFL"),"").equals("D"))&&rstRSSET.getString("US_USRPW").equals(cl_ProcessPassword.encode(cl_dat.M_txtUSRCD_pbst.getText().toUpperCase())))
							{
								fl_STPWD=false;
								obj = new fr_pwd();
								setMSG("User must change Password in first login ..",'N');
								cl_dat.M_pnlFRBTM_pbst.add("screen", obj);
								cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen");
								getContentPane().validate();
								update(getGraphics());
							}
							intLGCNT_fn=0;//INTIALISING LOGIN ATTEMPT COUNT
							cl_dat.M_strSYSEML_pbst = rstRSSET.getString("US_EMLRF");
							cl_dat.M_strLOGDT_pbst = fmtLCLTM.format(rstRSSET.getDate("us_curdt")).substring(0,10);
							cl_dat.M_txtCURDT_pbst.setText(cl_dat.M_strLOGDT_pbst);
							cl_dat.M_txtPASWD_pbst.setEnabled(false);
							cl_dat.M_btnSETPW_pbst.setEnabled(true);
							//btnINTRN.setEnabled(true);
							if(chkINTRN())
								btnINTRN.setVisible(true);
							cl_dat.M_txtUSER_pbst.setText(cl_dat.M_txtUSRCD_pbst.getText());
							cl_dat.M_txtDATE_pbst.setText(cl_dat.M_strLOGDT_pbst);
							getContentPane().validate();
							//if(thrCMPCD!=null)
							//	thrCMPCD.join();
							if(obj==null)
							{
								if(cl_dat.M_cmbCMPCD_pbst.getItemCount()==2)
								{
									cl_dat.M_cmbCMPCD_pbst.setSelectedIndex(1);
									cl_dat.M_txtCURDT_pbst.requestFocus();
								}
								else
								{	
									cl_dat.M_cmbCMPCD_pbst.setEnabled(true);
									cl_dat.M_cmbCMPCD_pbst.requestFocus();
								}	
								//cl_dat.M_txtCURDT_pbst.requestFocus();
							}
						}
					}
					flag=false;
					if(!hstUSRRT.containsKey(rstRSSET.getString("PPR_PRGCD")))
						hstUSRRT.put(rstRSSET.getString("PPR_PRGCD"),new String[]{rstRSSET.getString("PPR_SYSCD"),
								 nvlSTRVL(rstRSSET.getString("PP_PRGDS"),""),//1
								 nvlSTRVL(rstRSSET.getString("SP_SYSNM"),""),//2
								 rstRSSET.getString("PP_PRGTP"),//3
								 nvlSTRVL(rstRSSET.getString("PP_VERNO"),""),//4
								 nvlSTRVL(rstRSSET.getString("SP_SYSLC"),""),//5
								 nvlSTRVL(fmtLCDAT.format(rstRSSET.getDate("SP_YSTDT")),""),//6
								 nvlSTRVL(fmtLCDAT.format(rstRSSET.getDate("SP_YENDT")),""),//7
								 nvlSTRVL(rstRSSET.getString("PP_PRGLC"),""),//8
								 nvlSTRVL(rstRSSET.getString("PP_EINFL"),"")});//9
						
					vtrPRGCD.add(rstRSSET.getString("PPR_PRGCD"));
					vtrSBSCD.add(rstRSSET.getString("PPR_SBSCD"));
					vtrSHRDS.add(rstRSSET.getString("CMT_SHRDS"));
					vtrCHP01.add(rstRSSET.getString("CMT_CHP01"));
					vtrCHP02.add(rstRSSET.getString("CMT_CHP02"));
					vtrADDFL.add(rstRSSET.getString("PPR_ADDFL"));
					vtrMODFL.add(rstRSSET.getString("PPR_MODFL"));
					vtrDELFL.add(rstRSSET.getString("PPR_DELFL"));
					vtrENQFL.add(rstRSSET.getString("PPR_ENQFL"));
					vtrAUTFL.add(nvlSTRVL(rstRSSET.getString("PPR_AUTFL"),"N"));//ADDED ON 02/04/2004 BY AAP FOR AUTHORISTION OPTION
					// Added on 20 oct for maintaining user type
					if(cl_dat.M_strUSRTP_pbst.indexOf(rstRSSET.getString("UST_USRTP")) < 0)
					{    
                        if(cl_dat.M_strUSRTP_pbst.length() ==0)
                            cl_dat.M_strUSRTP_pbst +=rstRSSET.getString("UST_USRTP");
                        else
                        cl_dat.M_strUSRTP_pbst +="_"+rstRSSET.getString("UST_USRTP");
                    }
				}
			}
			else
			{
				setMSG("Wrong UserName.",'E');
				cl_dat.M_txtUSRCD_pbst.setEnabled(true);
				cl_dat.M_txtUSRCD_pbst.requestFocus();
			}
			if(!L_flgUSRCD)
			{
				setMSG("Wrong UserName.",'E');
				cl_dat.M_txtUSRCD_pbst.setEnabled(true);
				cl_dat.M_txtUSRCD_pbst.requestFocus();
			}
			rstRSSET.close();
		}catch(Exception L_SE)
                {
		   System.out.println("Error in chkUSRVLD1 : "+L_SE.toString());
		}
	}
	
//************	methods copied from cl_frm on 26/03/2003 by aap

	public void	mouseClicked(MouseEvent e)
	{}
	/**<B>TASKS : </B><br>
	 * Set Forecolor as BLUE & Border as Bevel border for the source.
	 * if Source is any of UP/DOWN Menuitem : Start tmrMNUUP/tmrMNUDN resp,Copy refence to current Menu selected to mnuTMRSCR
	 */
	 public void mouseEntered(MouseEvent e)
	 {
		JComponent L_SRCOBJ=(JComponent)e.getSource();
		L_SRCOBJ.setForeground(Color.blue );
		if(L_SRCOBJ.isEnabled())L_SRCOBJ.setBorder(BorderFactory.createBevelBorder(0));
	//CODE TO START SCROLLING OF MENUS *****************************
		
		if(L_SRCOBJ==mitTRNUP&&mitTRNUP.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuTEMM;
		}
		else if(L_SRCOBJ==mitRPTDN&&mitRPTDN.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuRPMM;
		}
		else if(L_SRCOBJ==mitTRNDN&&mitTRNDN.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuTEMM;
		}
		else if(L_SRCOBJ==mitRPTUP&&mitRPTUP.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuRPMM;
		}
		else if(L_SRCOBJ==mitRPTUP1&&mitRPTUP1.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuRPMR;
		}
		else if(L_SRCOBJ==mitRPTDN1&&mitRPTDN1.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuRPMR;
		}



		/*if(L_SRCOBJ==mitMSTUP&&mitMSTUP.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuMASTR;
		}
		else if(L_SRCOBJ==mitMSTDN&&mitMSTDN.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuMASTR;
		}
		else if(L_SRCOBJ==mitTRNUP&&mitTRNUP.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuTRANS;
		}
		else if(L_SRCOBJ==mitTRNDN&&mitTRNDN.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuTRANS;
		}
		else if(L_SRCOBJ==mitRPTUP&&mitRPTUP.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuREPRT;
		}
		else if(L_SRCOBJ==mitRPTDN&&mitRPTDN.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuREPRT;
		}
		else if(L_SRCOBJ==mitOTHUP&&mitOTHUP.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuQUERY;
		}
		else if(L_SRCOBJ==mitOTHDN&&mitOTHDN.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuQUERY;
		}
		else if(L_SRCOBJ==mitHSKUP&&mitHSKUP.isEnabled())
		{
			tmrMNUUP.start();tmrMNUUP.setRepeats(true);
			mnuTMRSRC=mnuHSKPG;
		}
		else if(L_SRCOBJ==mitHSKDN&&mitHSKDN.isEnabled())
		{
			tmrMNUDN.start();tmrMNUDN.setRepeats(true);
			mnuTMRSRC=mnuHSKPG;
		}*/
	//END OF CODE TO START SCROLLING OF MENUS *****************************
	 }
	/**<B>TASKS : </b><br>
	 * &nbsp&nbsp&nbsp&nbspSource = MenuItems UP/DOWN : Stop respective Timer
	 * &nbsp&nbsp&nbsp&nbspSource = Any other MenuItem : Remove Border
	 * &nbsp&nbsp&nbsp&nbspSource = Button : set line Border
	 */
	public void mouseExited(MouseEvent e)
	{
		JComponent L_SRCOBJ=(JComponent)e.getSource();
	//CODE TO STOP SCROLLING OF MENUS *****************************
		


		if(L_SRCOBJ==mitMSTUP)
		{
			tmrMNUUP.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitMSTDN)
		{
			tmrMNUDN.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitTRNUP)
		{
			tmrMNUUP.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitTRNDN)
		{
			tmrMNUDN.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitRPTUP)
		{
			tmrMNUUP.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitRPTDN)
		{
			tmrMNUDN.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitRPTUP1)
		{
			tmrMNUUP.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitRPTDN1)
		{
			tmrMNUDN.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitOTHUP)
		{
			tmrMNUUP.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitOTHDN)
		{
			tmrMNUDN.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitHSKUP)
		{
			tmrMNUUP.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
		else if(L_SRCOBJ==mitHSKDN)
		{
			tmrMNUDN.stop();
			mnuTMRSRC=null;
			mitTMRSRC=null;
		}
	//END OF CODE TO STOP SCROLLING OF MENUS *****************************
		else
		{//CHANGING BORDER AND FORECOLOR OF BUTTONS AND MENUS
			if(!((JComponent)e.getSource()).hasFocus())
			{
				L_SRCOBJ.setForeground(Color.black);
				if(L_SRCOBJ.getClass().toString().equals("class javax.swing.JMenu")||L_SRCOBJ.getClass().toString().equals("class javax.swing.JMenuItem"))
				{
					if(L_SRCOBJ.isEnabled()) L_SRCOBJ.setBorder(BorderFactory.createBevelBorder(0,this.getBackground(),this.getBackground(),this.getBackground(),this.getBackground()));
				}
				else
					if(L_SRCOBJ.isEnabled()) L_SRCOBJ.setBorder(BorderFactory.createLineBorder(Color.darkGray));
			}
		}
	}
	/**Restart System Idle Time Timer	 */
	public void mousePressed(MouseEvent e)
	{
		cl_dat.M_tmrIDLTM_pbst.restart();//IDLE TIME TIMER
	}
	public  void mouseReleased(MouseEvent e)
	{}
	/**Call exeITMEVT()	 */
	public void itemStateChanged(ItemEvent L_IE){
		if(((JComboBox)L_IE.getSource()).equals(cl_dat.M_cmbOPTN_pbst))
			exeITMEVT();
	}
	/** Actions related to cmbOPTN selection change	 */
	private void exeITMEVT(){
		if(strPRGTP.equalsIgnoreCase("RP")||strPRGTP.equalsIgnoreCase("QR"))
			cl_dat.M_btnSAVE_pbst.setText("Run");
		if(cl_dat.M_btnSAVE_pbst.getText().equalsIgnoreCase("Run"))
		{
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPFIL_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
				cl_dat.M_btnSAVE_pbst.setText("Execute");
	            setMSG(" Prints Reoprt to a file ",'N');
	            setDSLEXT();
			}

	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPRSL_pbst)){
	//          cl_dat.M_FLGOPT = 'C';
	            setENLEXT();
				cl_dat.M_btnSAVE_pbst.setText(" Run ");
        	}
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPEML_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
				cl_dat.M_btnSAVE_pbst.setText("Send");
	            setDSLEXT();
	            setMSG(" Mails a Report ",'N');
			}
	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPSCN_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
				cl_dat.M_btnSAVE_pbst.setText("Display");
	            setDSLEXT();
	            setMSG(" Displays Report on Screen ",'N');
			}
	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPPRN_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
				cl_dat.M_btnSAVE_pbst.setText("Print");
	            setDSLEXT();
	            setMSG(" Prints Report on Selected Printer ",'N');
			}
	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPFAX_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
				cl_dat.M_btnSAVE_pbst.setText("Fax");
	            setDSLEXT();
	            setMSG(" Prints Report on Selected Printer ",'N');
			}
		}
		else
		{
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
	            setMSG(" Adds a New Record ",'N');
	            cl_dat.M_btnSAVE_pbst.setText("Save");
	            setDSLEXT();
			}

	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPSEL_pbst)){
	//            cl_dat.M_FLGOPT = 'C';
	            setENLEXT();
				cl_dat.M_btnSAVE_pbst.setText(" Save ");
	        }
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPDEL_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
	            setDSLEXT();
	            cl_dat.M_btnSAVE_pbst.setText("Delete");
	            setMSG(" Deletes a Record ",'N');
			}
	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
	            setDSLEXT();
	            cl_dat.M_btnSAVE_pbst.setText("Change");
	            setMSG(" Modifies a Record ",'N');
			}
			//ADDED ON 02/04/2004 BY AAP FOR AUTHORISATION OPTION
	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPAUT_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
	            setDSLEXT();
	            cl_dat.M_btnSAVE_pbst.setText("Authorize");
	            setMSG(" Authorizes a request ",'N');
			}
	        if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPENQ_pbst)){
	            cl_dat.M_cmbOPTN_pbst.setEnabled(true);
	            cl_dat.M_btnSAVE_pbst.setText("Save");
	            setMSG(" Enquires about a Record ",'N');
	//            cl_dat.M_FLGOPT = 'E';
	//			cl_dat.M_ENQFL = 'T';
		        setDSLEXT();
				cl_dat.M_btnSAVE_pbst.setEnabled(false);
			}
		}
	}
	/**Enable exit button and related work
	 */
	private  void setENLEXT(){              // set ENABLE EXIT
	    cl_dat.M_btnEXIT_pbst.setEnabled(true);
        cl_dat.M_btnSAVE_pbst.setEnabled(false);
        cl_dat.M_btnUNDO_pbst.setEnabled(false);
        cl_dat.M_cmbOPTN_pbst.setEnabled(true);
	}
/**Disable exit button and related work
	 */
	private void setDSLEXT(){             // set DISABLE EXIT
	    cl_dat.M_btnEXIT_pbst.setEnabled(false);
        cl_dat.M_btnSAVE_pbst.setEnabled(true);
        cl_dat.M_btnUNDO_pbst.setEnabled(true);
	}
	/**Display message to user at bottom area of application
	 * <p>If message type=normal, forrecolor = blue</p>
	 * <p>If message type=error, forrecolor = red</p>
	 */
    private void setMSG(String LP_STRMSG,char LP_MSGTP){
		System.out.println(LP_STRMSG);
		try{
		cl_dat.M_txtSTAT_pbst.setEnabled(true);
		if(LP_MSGTP == 'N')
			cl_dat.M_txtSTAT_pbst.setForeground(Color.blue);
		else if(LP_MSGTP == 'E')
			cl_dat.M_txtSTAT_pbst.setForeground(Color.red);
		cl_dat.M_txtSTAT_pbst.setText(" "+LP_STRMSG);
		cl_dat.M_txtSTAT_pbst.paintImmediately(cl_dat.M_txtSTAT_pbst.getVisibleRect());
		}catch(Exception e)
		{System.out.println("Error in setMSG : "+e);}
	}

    // Returns new JButton
	public JButton crtBTN(JPanel LP_PANEL,String LP_LABEL,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT){
        JButton btnNEW = new JButton(LP_LABEL);
		LP_XPOS=(new Double(LP_XPOS*cl_dat.M_dblWIDTH).intValue());
		LP_YPOS=(new Double(LP_YPOS*cl_dat.M_dblHIGHT).intValue());
		LP_WIDTH=(new Double(LP_WIDTH*cl_dat.M_dblWIDTH).intValue());
		LP_HEIGHT=(new Double(LP_HEIGHT*cl_dat.M_dblHIGHT).intValue());
		
		btnNEW.setSize(LP_WIDTH,LP_HEIGHT);
		btnNEW.setLocation(LP_XPOS,LP_YPOS);
		btnNEW.addActionListener(this);
		btnNEW.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		btnNEW.addMouseListener(this);
		btnNEW.addKeyListener(this);
		btnNEW.addFocusListener(this);
        LP_PANEL.add(btnNEW);
		return btnNEW;
	}

	// Returns new JTextField
	public JTextField crtTXT(JPanel LP_PANEL,int LP_ALIGN,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT)
	{
		LP_XPOS=(new Double(LP_XPOS*cl_dat.M_dblWIDTH).intValue());
		LP_YPOS=(new Double(LP_YPOS*cl_dat.M_dblHIGHT).intValue());
		LP_WIDTH=(new Double(LP_WIDTH*cl_dat.M_dblWIDTH).intValue());
		LP_HEIGHT=(new Double(LP_HEIGHT*cl_dat.M_dblHIGHT).intValue());
    	JTextField txtNEW = new JTextField();
		txtNEW.setLocation(LP_XPOS,LP_YPOS);
		txtNEW.setSize(LP_WIDTH,LP_HEIGHT);
		txtNEW.setHorizontalAlignment(LP_ALIGN);
		txtNEW.addFocusListener(this);
		txtNEW.addActionListener(this);
		txtNEW.addKeyListener(this);
		LP_PANEL.add(txtNEW);
		return txtNEW;
	}

	// Returns new JLabel
	public JLabel crtLBL(JPanel LP_PANEL,String LP_LABEL,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT)
	{
		LP_XPOS=(new Double(LP_XPOS*cl_dat.M_dblWIDTH).intValue());
		LP_YPOS=(new Double(LP_YPOS*cl_dat.M_dblHIGHT).intValue());
		LP_WIDTH=(new Double(LP_WIDTH*cl_dat.M_dblWIDTH).intValue());
		LP_HEIGHT=(new Double(LP_HEIGHT*cl_dat.M_dblHIGHT).intValue());
   		JLabel lblNEW = new JLabel(LP_LABEL);
		lblNEW.setLocation(LP_XPOS,LP_YPOS);
		lblNEW.setSize(LP_WIDTH,LP_HEIGHT);
	    LP_PANEL.add(lblNEW);
		return lblNEW;
	}

	// Returns new JComboBox
    public JComboBox crtCMB(JPanel LP_PANEL,String LP_ITEM,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT){
    	JComboBox cmbNEW = new JComboBox();
		LP_XPOS=(new Double(LP_XPOS*cl_dat.M_dblWIDTH).intValue());
		LP_YPOS=(new Double(LP_YPOS*cl_dat.M_dblHIGHT).intValue());
		LP_WIDTH=(new Double(LP_WIDTH*cl_dat.M_dblWIDTH).intValue());
		LP_HEIGHT=(new Double(LP_HEIGHT*cl_dat.M_dblHIGHT).intValue());
		cmbNEW.addItem(LP_ITEM);
		cmbNEW.setLocation(LP_XPOS,LP_YPOS);
		cmbNEW.setSize(LP_WIDTH,LP_HEIGHT);
		LP_PANEL.add(cmbNEW);
		return cmbNEW;
    }
	public void keyTyped(KeyEvent L_KTE){}
    public void keyReleased(KeyEvent L_KRE){}

	public void mouseUp(MouseEvent L_ME){}
	/**Check String for null value
	 * @param LP_VARVL String to be checked for null
	 * @param LP_DEFVL String to be returned if LP_VARVL is null
	 * @return Original String if it was not null, default String if original straing was null
	 * <p><b>Usage :</b>Ex. 1 : String s=null;</p>
	 *	<P>						nvlSTRVL(s,"STRING") returns "STRING"</p>
	 *	<P>				Ex. 2 : String s1="ABC";</p>
	 *	<P>						nvlSTRVL(s1,"STRING") returns "ABC"	</p>
	 */
	private String nvlSTRVL(String LP_VARVL, String LP_DEFVL){
		try{
                        if (LP_VARVL != null) {
                           if (LP_VARVL.trim().length() > 0) {
				LP_VARVL = LP_VARVL.trim();
                                } // if (LP_VARVL.trim().length() > 0)
                           else {
				LP_VARVL = LP_DEFVL;
                           } // if (LP_VARVL.trim().length() > 0)
                        } // if (LP_VARVL != null)
                        else {
				LP_VARVL = LP_DEFVL;
                        } // if (LP_VARVL != null) {
                }catch (Exception L_EX){
			setMSG(L_EX,"nvlSTRVL");
		}
		return LP_VARVL;
	}
	/**Display message to user at bottom area of application. Over-ridden to display Error generated by JVM
	 * <br>If message type=normal, forrecolor = blue
	 * <br>If message type=error, forrecolor = red
	 * @param P_expEXPTNP Refernce to Exception reported by JVM
	 * @param P_strMTHNM String giving details of the method in which Exception is reported
	 * <br><br><hr><B>Usage</B>
	 * <br>setMSG(e,"My Method")
	 * <br>Displays message : "Error in saving/retreiving data"
	 * <br>Displays "Error in My Method "+details of error on dos screen
	 */
	private  void setMSG(Exception P_expEXPTNP,String P_strMTHNM){
		if(P_expEXPTNP.getClass().toString().equals("class java.sql.SQLException"))
		{
			setMSG("Error in saving/retreiving data",'E');
			System.out.println("Error in "+P_strMTHNM+" :"+P_expEXPTNP);
		}
	}

	/** Update Hit Count at Back-end	 */
	private void setHITCNT()
	{
		try
		{
			String L_STRSQL="UPDATE SA_PPMST SET "
				+"PP_HITCT=PP_HITCT+1,"
							+"PP_LHTBY='"+(cl_dat.M_strUSRCD_pbst.trim().length()>3 ? cl_dat.M_strUSRCD_pbst.substring(0,3) : cl_dat.M_strUSRCD_pbst)+"',"
				+"PP_LHTDT=GETDATE() "
				+"WHERE PP_SYSCD='"+strSYSCD+"' AND "
				+" PP_PRGCD='"+strPRGCD+"'";
			cl_dat.exeSQLUPD(L_STRSQL,"setLCLUPD");
			cl_dat.exeDBCMT("setHITCNT");
		}catch (Exception e)
		{System.out.println("Error in hit count Updation .."+e);}
	}
	/**Internal class in fr_log to update current date and time	 */
	private class cl_clock extends java.util.TimerTask
	{
		public void run()
		{
			try{
			calCLOCK.add(Calendar.SECOND,1);
			String L_STRTMP=fmtLCLTM.format(calCLOCK.getTime());
			cl_dat.M_txtCLKDT_pbst.setText(L_STRTMP.substring(0,10));
			cl_dat.M_txtCLKTM_pbst.setText(L_STRTMP.substring(10,16));

			if(calCLOCK.get(Calendar.SECOND)<2&&calCLOCK.get(Calendar.MINUTE)<1)
			{

				rstRSSET=cl_dat.exeSQLQRY("select current_timestamp from sa_spmst");
				if(rstRSSET.next())
				{
					datCURDT=fmtDBSTM.parse(rstRSSET.getString(1).substring(0,19));
					calCLOCK.setTime(datCURDT);
					L_STRTMP=fmtLCLTM.format(calCLOCK.getTime());
					cl_dat.M_txtCLKDT_pbst.setText(L_STRTMP.substring(0,10));
					cl_dat.M_txtCLKTM_pbst.setText(L_STRTMP.substring(10,15));
				}
			}
			}catch(Exception e)
			{System.out.println(e);}
		}
	}
	/**Method to create screen for a form.
	 *
	 * <br><br><br>Displays SBS and button panel. Transfers Program and System codes to form class
	 * <br>Sets components in SBS Combos by calling setUSRRT(String[][]) in cl_pbase
	 * <br>Waits for Hit count updation thread to complete
	 * @param P_staSBSRT String array containing details of the user rights
	 */
	private void crtSCN(String[][] P_staSBSRT)
	{
		try
		{
			
		String[] L_staPRGDT=(String[])hstUSRRT.get(strPRGCD);
		strPRGTP=L_staPRGDT[intPRGTP_fn];
		if(L_staPRGDT[intPRGTP_fn].equals("RP")||L_staPRGDT[intPRGTP_fn].equals("QR"))
			cl_dat.M_btnSAVE_pbst.setText("Run");
		else
			cl_dat.M_btnSAVE_pbst.setText("Save");
		cl_dat.M_pnlFRTOP_pbst.setVisible(true);
		getContentPane().add(cl_dat.M_pnlSBSCD_pbst);//DO NOT REMOVE
		cl_dat.M_pnlSBSCD_pbst.setBorder(BorderFactory.createLineBorder(Color.gray));
		cl_dat.M_pnlSBSCD_pbst.setBounds(0,0,cl_dat.M_dimSCRN_pbst.width,new Double(28*cl_dat.M_dblHIGHT).intValue());
		cl_dat.M_pnlSBSCD_pbst.setVisible(true);
		cl_dat.M_pnlFRBTM_pbst.add("screen", ocl_pbase);
		ocl_pbase.M_strPRGCD_fr=strACTCMD;
		ocl_pbase.M_strSYSCD_fr=strSYSCD;
		if(thrHITCNT!=null)
			thrHITCNT.join();
		//if(cl_dat.thrPRNLS!=null)
		//	cl_dat.thrPRNLS.join();
	
		//System.out.println("cl_dat.M_strEINFL_pbst "+cl_dat.M_strEINFL_pbst);
		//System.out.println("P_staSBSRT[x][x] "+P_staSBSRT.toString());
		cl_dat.M_crdCENTR_pbst.show(cl_dat.M_pnlFRBTM_pbst,"screen");
		ocl_pbase.setUSRRT(P_staSBSRT);
		setCursor(cl_dat.M_curDFSTS_pbst);
		}catch(Exception e)
		{System.out.println("Error in crtSCN :"+e);}
	}
/*
	CODE FOR WINDOW RESIZING
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentResized(ComponentEvent e) {}//System.out.println("yes");//cl_dat.M_dimSCRN_pbst=getSize();
//													cl_dat.M_dblWIDTH=(cl_dat.M_dimSCRN_pbst.width/800.00);
//		cl_dat.M_dblHIGHT=((cl_dat.M_dimSCRN_pbst.height)/600.00);
//													p_int2.setBounds((new Double(180*cl_dat.M_dblWIDTH).intValue()),new Double(3*cl_dat.M_dblHIGHT).intValue(),cl_dat.M_dimSCRN_pbst.width-(new Double(175*cl_dat.M_dblWIDTH).intValue()),(new Double(28*cl_dat.M_dblHIGHT).intValue()));
//													invalidate();repaint();}
	public void componentShown(ComponentEvent e) {System.out.println("shwn");}
*/

private void crtHSTMKT()
{
    try
    {
        String L_strPHSKEY ="",L_strHSKEY="";
        String[] L_staVALUE = new String[]{"","","","","","",""};
        int L_intCNT =0;
        cl_dat.M_hstMKTCD_pbst = new Hashtable<String,String[]>();
        // String L_strSQLQRY = "SELECT * FROM CO_CDTRN WHERE CMT_CGMTP||CMT_CGSTP IN("+
        //"'SYSMRXXDTP','SYSMRXXPMM','SYSMRXXRGN','SYSMR00SAL','SYSMR00ZON','SYSMRXXDLT','SYSMRXXPTT','SYSMRXXLAU','SYSFGXXPKG') order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
        String L_strSQLQRY = "SELECT * FROM CO_CDTRN A WHERE A.CMT_CGMTP + A.CMT_CGSTP IN "+
		"(SELECT B.CMT_CODCD FROM CO_CDTRN B WHERE B.CMT_CGMTP='CAT' AND B.CMT_CGSTP = 'COXXHST' AND ISNULL(CMT_STSFL,' ') <> 'X') order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
        //System.out.println("call to crtHSTMKT"+L_strSQLQRY);
        if(L_rstRSSET !=null)
        {
            while(L_rstRSSET.next())
            {
                L_strHSKEY = L_rstRSSET.getString("CMT_CGMTP")+L_rstRSSET.getString("CMT_CGSTP")+nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");
                if(!L_strHSKEY.equals(L_strPHSKEY))
                {
                    L_staVALUE = new String[8];
                    L_staVALUE[cl_dat.M_intCODDS_pbst] = L_rstRSSET.getString("CMT_CODDS");
                    L_staVALUE[cl_dat.M_intSHRDS_pbst] = L_rstRSSET.getString("CMT_SHRDS");
                    L_staVALUE[cl_dat.M_intCHP01_pbst] = L_rstRSSET.getString("CMT_CHP01");
                    L_staVALUE[cl_dat.M_intCHP02_pbst] = L_rstRSSET.getString("CMT_CHP02");
                    L_staVALUE[cl_dat.M_intCCSVL_pbst] = L_rstRSSET.getString("CMT_CCSVL");
                    L_staVALUE[cl_dat.M_intNMP01_pbst] = L_rstRSSET.getString("CMT_NMP01");
                    L_staVALUE[cl_dat.M_intNMP02_pbst] = L_rstRSSET.getString("CMT_NMP02");
                    L_staVALUE[cl_dat.M_intNCSVL_pbst] = L_rstRSSET.getString("CMT_NCSVL");
                    cl_dat.M_hstMKTCD_pbst.put(L_strHSKEY,L_staVALUE);
                    L_intCNT++;
                }
            }
            if(L_intCNT >0)
                cl_dat.M_hstMKTCD_pbst.put(L_strHSKEY,L_staVALUE);
            System.out.println(cl_dat.M_hstMKTCD_pbst.size());    
         L_rstRSSET.close();    
        }
        
        
    }
    catch(Exception L_E)
    {
       System.out.println("crtHSTMKT "+L_E.toString()); 
    }
}

	private boolean chkINTRN()
	{
		try
		{
			String L_strSQLQRY = "SELECT count(*) CNT FROM CO_INTRN WHERE IN_EMPNO = '"+cl_dat.M_strEMPNO_pbst+"'";
			//System.out.println(L_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(L_strSQLQRY);
			if(L_rstRSSET !=null && L_rstRSSET.next())
			{
				if(L_rstRSSET.getInt("CNT")>0)
				{
					L_rstRSSET.close();
					return true;
				}
				L_rstRSSET.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in chkINTRN() : "+e);
		}
		return false;
	}

}
/*
QUERY TO SELECT UER RIGHTS.
select US_USRPW,US_PWMFL,UST_USRTP,US_EMLRF,
GETDATE() us_curdt,PPR_SYSCD,PPR_SBSCD,
PPR_PRGCD,SP_SYSNM,SP_SYSLC,SP_DTBLB,
SP_YSTDT,SP_YENDT,PP_PRGDS,PP_VERNO,PP_PRGLC
CMT_SHRDS,CMT_CHP01,CMT_CHP02,PPR_USRTP,
PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,
PP_PRGTP FROM spldata/SA_USMST,spldata/SA_PPRTR,
spldata/SA_USTRN,spldata/SA_PPMST,spldata/SA_SPMST,
spldata/CO_CDTRN where US_USRCD = 'HBP'
AND UST_USRCD=US_USRCD and PPR_USRTP=UST_USRTP
and CMT_CODCD=PPR_SBSCD and CMT_CGMTP='SYS'
and CMT_CGSTP='COXXSBS' and PPR_PRGCD=PP_PRGCD
and PP_SYSCD=SP_SYSCD order by ppr_syscd,pp_prgtp
*/
