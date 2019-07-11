import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.undo.*;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.ResultSet;
import javax.swing.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Cursor;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>  Customer Order Booking Entry</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                  Form for Customer Order                  Entry by Regional Offices.       (To be extended to Distributors in future)&nbsp; </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Marketing System Enhancement Proposal by      Mr. SRD      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\mr_teind.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_teind.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>10/11/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD></TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD></TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD></TD>    <TD> </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD></TD>    <TD></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class fg_tesra extends cl_pbase implements ChangeListener//,PopupMenuListener
{
	
								/**Flag to indicate that ammendment is to be raised during modification	 */
	private boolean flgAMDFL;	/**Flag to remember whether user has autothorisation rights */
	private boolean flgAUTRN;	/**Flag to indicate whether remark was added previously */
	private boolean flgREGRM;	/**String for order no. */
	private boolean flgAUTRM;	/**String for order no. */
	private boolean flgBKGRM;
	
								/**String for order no. */

	
										/**Hash tables  */
	private Hashtable hstCDTRN;			// Details of all codes used in program
	private Hashtable hstCODDS;			// Code decription as key  & Code as a value 
	private Hashtable hstPRMST;			// Details of product master
	private Hashtable hstPTMST;			// Details of product master
	private Hashtable hstSTMST;			// Details of stock master related fields
	private Hashtable hstRSTRN;
	private Hashtable hstLTMST;

	
	private Vector vtrPRTNM;			// Party Name Vector
	private JList lstPRTNM;
	
	
	private fg_rpsra objRPSRA;
	
	private Object[] arrHSTKEY;		// Object array for getting hash table key in sorted order
	
								/**Button for indent authorisation only in modification	 */
	private TxtLimit txtWRHTP;
	private TxtLimit txtRESTP;
	private TxtLimit txtRESNO;
	private TxtDate txtRESDT;
	private TxtDate txtREXDT;
	private TxtLimit txtREQBY;
	private TxtLimit txtAUTBY;
	private TxtLimit txtPRDCD;
	private TxtLimit txtPKGTP;
	private TxtLimit txtGRPCD;
	private TxtLimit txtRESDS;
	private TxtNumLimit txtRESQT;
	private TxtNumLimit txtISSQT;
	private TxtNumLimit txtBALQT;
	
	private JCheckBox chkADDFL;
	private JCheckBox chkDEDFL;

	private JButton btnPRINT;
	//private JButton btnREVERT;
	
    private JLabel lblPRDDS;
    private JLabel lblPKGDS;
	
	
	private cl_JTable tblSTMST;		// Allocation Entry Table
	private cl_JTable tblRSTRN;		// Reservation Status Display
	private JTabbedPane tbpMAIN;
	
	private JPanel pnlALLCN;		// Allocation Entry Panel
	private JPanel pnlRESST;		// Reservation Status panel
	
	
	/**Panel for new Deemed Export file no. entry	 */	
											/** Table column for Allocation Table */
	private int intTB1_CHKFL = 0;
	private int intTB1_LOTNO = 1;
	private int intTB1_RCLNO = 2;
	private int intTB1_MNLCD = 3;
	private int intTB1_STKQT = 4;
	private int intTB1_RESQT = 5;
	private int intTB1_AVLQT = 6;
	private int intTB1_ADDFL = 7;
	private int intTB1_ADJQT = 8;
	private int intTB1_DEDFL = 9;
	private int intTB1_REMDS = 10;
	private int intTB1_LOTDS = 11;
	

											/** Table column for Reservation Qty. Table */
	//private int intTB2_CHKFL = 0;
	private int intTB2_LOTNO = 0;
	private int intTB2_RCLNO = 1;
	private int intTB2_STKQT = 2;
	private int intTB2_RESQT = 3;
	private int intTB2_CURQT = 4;
	private int intTB2_ISSQT = 5;
	private int intTB2_BALQT = 6;
	
	
											/** Array elements for records picked up from Code Transactoion */
	private int intCDTRN_TOT = 9;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;

	
	private int intSTMST_TOT = 9;			
    private int intAE_ST_LOTNO = 0;
    private int intAE_ST_RCLNO = 1;
    private int intAE_ST_MNLCD = 2;
    private int intAE_ST_STKQT = 3;
    private int intAE_ST_RESQT = 4;
    private int intAE_ST_AVLQT = 5;
    private int intAE_ST_ADJQT = 6;
    private int intAE_ST_REMDS = 7;
    private int intAE_ST_LOTDS = 8;
	

	private int intLTMST_TOT = 3;			
    private int intAE_LT_LOTNO = 0;
    private int intAE_LT_RCLNO = 1;
    private int intAE_LT_REMDS = 2;
	
	
	
	private int intRSTRN_TOT = 7;			
    private int intAE_RST_LOTNO = 0;
    private int intAE_RST_RCLNO = 1;
    private int intAE_RST_STKQT = 2;
    private int intAE_RST_RESQT = 3;
    private int intAE_RST_REFQT = 4;
    private int intAE_RST_CURQT = 5;
    private int intAE_RST_ISSQT = 6;
	
	
										/** Array elements for Product Master details */
	private int intPRMST_TOT = 3;
    private int intAE_PR_PRDCD = 0;		
    private int intAE_PR_PRDDS = 1;		
    private int intAE_PR_AVGRT = 2;		
	
	
										/** Variables for Code Transaction Table
										*/
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		

	private String strPRDCD;		
	private String strPRDTP;		
	private String strLOTNO;		
	private String strRCLNO;		
	private String strMNLCD;		
	private String strPKGTP;		
	private String strWHRSTMST;		

	//private String strLOTNO_NEW;
	//private String strRCLNO_NEW;
	private double dblLOTTOT	= 0.000;
	private double dblGRDTOT_RES	= 0.000;
	private double dblGRDTOT_ISS	= 0.000;

	private double dblSTKQT_XXX = 0.000;
	private double dblRESQT_OLD = 0.000;
	private double dblAVLQT_OLD = 0.000;
	private double dblADJQT_OLD = 0.000;
	private double dblRESQT = 0.000;
	private double dblAVLQT = 0.000;
	private double dblADJQT = 0.000;
	private double dblRESQT_NEW = 0.000;
	private double dblAVLQT_NEW = 0.000;
	private double dblADJQT_NEW = 0.000;
	private double dblADJQT_ADD = 0.000;

	private double dblSTKQT_LOT = 0.000;
	private double dblSTKQT_LOT_AFTER = 0.000;
	private double dblRESQT_LOT_AFTER = 0.000;
	
	private String strWRHTP="01";					// Product Type
	private String strPRTTP="C";					// Party Type
    private String strYREND = "31/03/2004";			// Year Ending date
    private String strYRDGT ="";					// Year digit for Doc.Numbering
    private String strWHRSTR ="";					// where condition for update statement
    private boolean flgCHK_EXIST;					// flag for checking existence of a record in database
    private boolean flgFIRST_ADD = true;				
    private boolean flgEXPIRED_RES = false;				
	
	private TBLINPVF oTBLINPVF;

	//******************************************************
	private cl_JTable tblODRSV;
	private JPanel pnlODRSV;
	private int intTB0_CHKFL = 0;
	private int intTB0_RESNO = 1;
	private int intTB0_RESDT = 2;
	private int intTB0_PRDDS = 3;
	private int intTB0_PKGTP = 4;
	private int intTB0_GRPCD = 5;
	private int intTB0_RESQT = 6;
	private int intTB0_ISSQT = 7;
	private int intTB0_BALQT = 8;
	private int intTB0_REXDT = 9;
	private int intTB0_REVFL = 10;
	private int intTB0_EXTFL = 11;
	private int intTB0_NEWDT = 12;
	//*****************************************************	
	
	
	/**Constructor for the form<br>
	 * Retrieves market type/delivery type/payment type and transport type  options from CO_CDTRN and populates respective combos.<br>
	 * Starts thread for retrieving Distributor details along with curren year series for INDNO
	 */
	fg_tesra()
	{
		super(2);
		try
		{
			//objRPSRA = new fg_rpsra();
			hstCDTRN = new Hashtable();
			hstCODDS = new Hashtable();
			hstPRMST = new Hashtable();
			hstPTMST = new Hashtable();
			hstSTMST = new Hashtable();
			hstLTMST = new Hashtable();
			hstRSTRN = new Hashtable();

			
			vtrPRTNM = new Vector();
			lstPRTNM = new JList();

			lblPRDDS = new JLabel("---");
			lblPKGDS = new JLabel("---");


			//**************************************
			pnlODRSV = new JPanel(null);
			tblODRSV = crtTBLPNL1(pnlODRSV,new String[]{"","Res. No.","Res. Dt.","Grade","Pkg. Type","Party Cd.","Quantity","Issue Qty.","Bal Qty.","Expry Dt.","Relese","Extend","New Dt."},100,2,1,9,8,new int[]{20,60,75,80,25,50,85,80,80,80,25,25,80},new int[]{0,10,11});
			tblODRSV.setCellEditor(intTB0_NEWDT,new TxtDate());
			//**************************************
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "5" : "4";

			hstCDTRN.clear();
            hstCODDS.clear();
			crtCDTRN("'MSTCOXXCUR','STSMRXXIND','SYSMRXXDTP', 'SYSMRXXPMT','SYSMR01MOT','SYSMR00EUS','SYSCOXXTAX','SYSCOXXDST','SYSFGXXPKG','SYSCOXXAMT','SYSMR00SAL','SYSFGXXQLT'","",hstCDTRN);
			crtPRMST();

			setMatrix(20,12);
			add(new JLabel("W/H Type / Res.Type"),1,1,1,2,this,'L');
			add(txtWRHTP=new TxtLimit(2),2,1,1,0.5,this,'L');
			add(txtRESTP=new TxtLimit(2),2,1,1,0.5,this,'R');

			add(new JLabel("Res.No."),1,3,1,1,this,'L');
			add(txtRESNO=new TxtLimit(8),2,3,1,1.25,this,'L');

			add(new JLabel("Res.Date"),1,5,1,1.25,this,'L');
			add(txtRESDT=new TxtDate(),2,5,1,1.25,this,'L');

			add(new JLabel("Exp.Date"),1,7,1,1.25,this,'L');
			add(txtREXDT=new TxtDate(),2,7,1,1.25,this,'L');


			add(new JLabel("Req.By"),1,9,1,1,this,'L');
			add(new JLabel("Auth.By"),1,10,1,1,this,'L');
			add(txtREQBY=new TxtLimit(3),2,9,1,0.75,this,'L');
			add(txtAUTBY=new TxtLimit(3),2,10,1,0.75,this,'L');

			
			add(new JLabel("Prd.Code"),3,1,1,1.35,this,'L');
			add(txtPRDCD=new TxtLimit(10),4,1,1,1.35,this,'L');
			add(lblPRDDS,5,1,1,2,this,'L');

			add(new JLabel("Pkg.Type"),3,3,1,1,this,'L');
			add(txtPKGTP=new TxtLimit(2),4,3,1,0.5,this,'L');
			add(lblPKGDS,5,3,1,2,this,'L');

			add(new JLabel("Grp.Code"),3,4,1,1,this,'L');
			add(txtGRPCD=new TxtLimit(5),4,4,1,0.75,this,'L');

			add(new JLabel("Remark"),3,5,1,4,this,'L');
			add(txtRESDS=new TxtLimit(50),4,5,1,4,this,'L');

			add(btnPRINT = new JButton("Print"),5,5,1,1.25,this,'L');
			//add(btnREVERT = new JButton("Revert"),5,7,1,1.25,this,'L');

			add(new JScrollPane(lstPRTNM),3,9,2,4,this,'L');
			
			//System.out.println("001");

			//CREATING ALLOCATION PANEL AS FIRST TABBED PANE		
			pnlALLCN=new JPanel(null);

			add(new JLabel("Total:"),6,7,1,1,this,'L');
			add(new JLabel("Res."),5,8,1,1,this,'R');
			add(new JLabel("Desp."),5,9,1,1,this,'R');
			add(new JLabel("Bal."),5,10,1,1,this,'R');
			add(txtRESQT=new TxtNumLimit(7.3),6,8,1,1,this,'R');
			add(txtISSQT=new TxtNumLimit(7.3),6,9,1,1,this,'R');
			add(txtBALQT=new TxtNumLimit(7.3),6,10,1,1,this,'R');
			
			
			
			//System.out.println("002");
			JTextField txtTB1_LOTNO=new JTextField();
			JTextField txtTB1_RCLNO=new JTextField();
			TxtNumLimit txtTB1_ADJQT=new TxtNumLimit(7.3);
			tblSTMST=crtTBLPNL1(pnlALLCN,new String[]{"FL","Lot No.","RCl.No","Locn","Stock","Res.Qty","Avl.Qty.","+","(+/-)","-","Stk.Remark","Lot Remark"},200,2,1,8,12,new int[]{10,80,25,60,60,60,60,10,60,10,150,150},new int[]{0,7,9});
			tblSTMST.setCellEditor(intTB1_LOTNO,txtTB1_LOTNO);
			tblSTMST.setCellEditor(intTB1_RCLNO,txtTB1_RCLNO);
			tblSTMST.setCellEditor(intTB1_MNLCD,new TxtLimit(5));
			tblSTMST.setCellEditor(intTB1_STKQT,new TxtNumLimit(7.3));
			tblSTMST.setCellEditor(intTB1_RESQT,new TxtNumLimit(7.3));
			tblSTMST.setCellEditor(intTB1_AVLQT,new TxtNumLimit(7.3));
			tblSTMST.setCellEditor(intTB1_ADDFL,chkADDFL = new JCheckBox());
			tblSTMST.setCellEditor(intTB1_ADJQT, txtTB1_ADJQT);
			tblSTMST.setCellEditor(intTB1_DEDFL,chkDEDFL = new JCheckBox());
			tblSTMST.setCellEditor(intTB1_REMDS,new JTextField());
			tblSTMST.setCellEditor(intTB1_LOTDS,new JTextField());

			((TxtNumLimit) tblSTMST.cmpEDITR[intTB1_ADJQT]).addFocusListener(this);
			((TxtNumLimit) tblSTMST.cmpEDITR[intTB1_ADJQT]).addKeyListener(this);

			((JCheckBox) tblSTMST.cmpEDITR[intTB1_ADDFL]).addFocusListener(this);
			((JCheckBox) tblSTMST.cmpEDITR[intTB1_ADDFL]).addMouseListener(this);

			((JCheckBox) tblSTMST.cmpEDITR[intTB1_DEDFL]).addFocusListener(this);
			((JCheckBox) tblSTMST.cmpEDITR[intTB1_DEDFL]).addMouseListener(this);
			tblODRSV.addMouseListener(this);
			
			
			tblSTMST.setInputVerifier(oTBLINPVF=new TBLINPVF());
			txtTB1_LOTNO.addFocusListener(this);
			txtTB1_LOTNO.addKeyListener(this);
			txtTB1_RCLNO.addFocusListener(this);
			txtTB1_RCLNO.addKeyListener(this);
			
			//System.out.println("003");

			//CREATING RESERVATION STATUS PANEL AS SECOND TABBED PANE		
			pnlRESST=new JPanel(null);
			tblRSTRN=crtTBLPNL1(pnlRESST,new String[]{"Lot No.","RCL","Stk.Qty.","Res.Qty.","Cur.Resn.","Despatched","Balance"},100,2,1,8,12,new int[]{120,25,100,100,100,100,100},new int[]{});
			tblRSTRN.setCellEditor(intTB2_LOTNO,new TxtLimit(8));
			tblRSTRN.setCellEditor(intTB2_RCLNO,new TxtLimit(2));
			tblRSTRN.setCellEditor(intTB2_STKQT,new TxtNumLimit(7.3));
			tblRSTRN.setCellEditor(intTB2_RESQT,new TxtNumLimit(7.3));
			tblRSTRN.setCellEditor(intTB2_CURQT,new TxtNumLimit(7.3));
			tblRSTRN.setCellEditor(intTB2_ISSQT,new TxtNumLimit(7.3));
			tblRSTRN.setCellEditor(intTB2_BALQT,new TxtNumLimit(7.3));
			tblRSTRN.setInputVerifier(new TBLINPVF());

			//System.out.println("004");
			//ADDING ALL PANELS IN TABBED PANE			
			tbpMAIN=new JTabbedPane();
			tbpMAIN.add("Lot Allocation",pnlALLCN);
			tbpMAIN.add("Reservation Status",pnlRESST);
			tbpMAIN.add("Expired Reservations",pnlODRSV);
			tbpMAIN.addChangeListener(this);

			//System.out.println("005");
			//ADDING TABBED PANE TO SCREEN			
			add(tbpMAIN,6,1,12,14,this,'L');

			
			INPVF oINPVF =new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(oINPVF);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
					{
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
					}
				}
				else
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			setMatrix(20,6);
		}catch (Exception e)
		{setMSG(e,"Child.Constructor");}
	}


	/** Action to be executed after selecting a code using F1
	 */
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			cl_dat.M_wndHLP_pbst=null;
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtPRDCD"))
			{
				txtPRDCD.setText(L_STRTKN.nextToken());
				lblPRDDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtPKGTP"))
			{
				txtPKGTP.setText(L_STRTKN.nextToken());
				lblPKGDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtGRPCD"))
			{
				txtGRPCD.setText(L_STRTKN.nextToken());
				txtRESDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtRESNO"))
			{
				txtRESNO.setText(L_STRTKN.nextToken());
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"in Child.exeHLPOK");
		}
	}
	
	
	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		super.clrCOMP();
		if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
		{
			inlTBLEDIT(tblSTMST);
			inlTBLEDIT(tblRSTRN);
			if(txtWRHTP.getText().length()==0)
				{txtWRHTP.setText("01");txtWRHTP.requestFocus();}
			if(txtRESTP.getText().length()==0)
				txtRESTP.setText("01");
			if(txtRESDT.getText().length()==0)
				txtRESDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
			//if(txtREXDT.getText().length()==0)
			//	txtREXDT.setText(setMONEND(txtRESDT.getText()));
			if(txtREQBY.getText().length()==0)
				txtREQBY.setText(cl_dat.M_strUSRCD_pbst);
			if(txtAUTBY.getText().length()==0)
				txtAUTBY.setText(cl_dat.M_strUSRCD_pbst);
			txtRESQT.setDisabledTextColor(Color.blue);
			txtISSQT.setDisabledTextColor(Color.blue);
			txtBALQT.setDisabledTextColor(Color.blue);
			vtrPRTNM.clear();
			lstPRTNM.setListData(vtrPRTNM);
			

		}
	}
	
	/** Returns Last Day of the month
	 */	
	private String setMONEND(String LP_CURDT)
	{
		String L_strRETSTR="";
		try
		{
			M_calLOCAL.setTime(M_fmtLCDAT.parse(LP_CURDT));
			M_calLOCAL.add(Calendar.MONTH,+1);
			M_calLOCAL.set(Calendar.DATE,1);
			M_calLOCAL.add(Calendar.DATE,-1);
			L_strRETSTR = M_fmtLCDAT.format(M_calLOCAL.getTime());
		}
		catch(Exception L_EX){setMSG(L_EX, "setMONEND");}
		return L_strRETSTR;
	}


	/** Restoring default Key Values after clearing components 
	 * on the entry screen
	 */
	private void clrCOMP_1()
	{
		try
		{
			String L_strRESNO = txtRESNO.getText();
			clrCOMP();
			txtRESNO.setText(L_strRESNO);
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP_1");}
	}


	
	
	/** Reverting the reservation by initializing Res.Qty. in MR_RSTRN to zero
	 * and reducing Res.Qty in stock master
	 */
	private void exeREVERT(String LP_RESNO)
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			double L_dblRESQT_L = 0.000;	// Res.qty for the Lot
			double L_dblRESQT_M = 0.000;	// Res.qty for Lot + Location
			//double L_dblRESQT_RUN = 0.000;	// Running Balance Qty. for the Lot
			String L_strSQLQRY = "select RST_LOTNO,RST_RCLNO,RST_RESQT,RST_ISSQT from MR_RSTRN where RST_RESNO = '"+LP_RESNO+"' and isnull(RST_RESQT,0)-isnull(RST_ISSQT,0)>0  order by RST_LOTNO,RST_RCLNO";
			ResultSet L_rstRSSET1 = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(!L_rstRSSET1.next() || L_rstRSSET1==null)
				return;

			L_strSQLQRY = "update MR_RSTRN set RST_RESQT = isnull(RST_ISSQT,0), RST_STSFL = 'X' where RST_RESNO = '"+LP_RESNO+"'";
			System.out.println(L_strSQLQRY);
			cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
			while(true)
			{
				L_dblRESQT_L = Double.parseDouble(getRSTVAL(L_rstRSSET1,"RST_RESQT","N"))-Double.parseDouble(getRSTVAL(L_rstRSSET1,"RST_ISSQT","N"));
				L_strSQLQRY = "select ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_MNLCD,ST_RESQT from FG_STMST where ST_LOTNO = '"+getRSTVAL(L_rstRSSET1,"RST_LOTNO","N")+"' and ST_RCLNO = '"+getRSTVAL(L_rstRSSET1,"RST_RCLNO","N")+"' and isnull(ST_RESQT,0)>0 order by ST_LOTNO, ST_RCLNO, ST_PKGTP, ST_MNLCD";
				ResultSet L_rstRSSET2 = cl_dat.exeSQLQRY2(L_strSQLQRY);
				if(!L_rstRSSET2.next() || L_rstRSSET2==null)
					{if(!L_rstRSSET1.next()) break; else continue;}
				while(true)
				{
					L_dblRESQT_M = 	Double.parseDouble(getRSTVAL(L_rstRSSET2,"ST_RESQT","N"));
					L_dblRESQT_M = L_dblRESQT_L < L_dblRESQT_M ? L_dblRESQT_L : L_dblRESQT_M;
					L_strSQLQRY = "update FG_STMST set ST_RESQT = ST_RESQT - "+setNumberFormat(L_dblRESQT_M,3)+" where ST_LOTNO = '"+getRSTVAL(L_rstRSSET2,"ST_LOTNO","C")+"' and  ST_RCLNO = '"+getRSTVAL(L_rstRSSET2,"ST_RCLNO","C")+"' and ST_PKGTP = '"+getRSTVAL(L_rstRSSET2,"ST_PKGTP","C")+"' and ST_MNLCD = '"+getRSTVAL(L_rstRSSET2,"ST_MNLCD","C")+"'";
					System.out.println(L_strSQLQRY);
					cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
					L_dblRESQT_L = L_dblRESQT_L - L_dblRESQT_M;
					if(!L_rstRSSET2.next())
						break;
				}
				L_rstRSSET2.close();
				if(!L_rstRSSET1.next())
					break;
			}
			L_rstRSSET1.close();
			if(cl_dat.exeDBCMT("exeREVERT"))
				{setMSG(LP_RESNO+" reverted successfully",'N');}
		}

		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
			cl_dat.M_flgLCUPD_pbst=false;
			cl_dat.exeDBCMT("exeREVERT");
		}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	

	
	/** Replacing Expiry date in Res.Record with new Expiry Date
	 */
	private void updREXDT(String LP_RESNO, String LP_REXDT)
	{
		try
		{
			System.out.println("update MR_RSTRN set RST_REXDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_REXDT))+"' where RST_RESNO = '"+LP_RESNO+"'");
			cl_dat.exeSQLUPD("update MR_RSTRN set RST_REXDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_REXDT))+"' where RST_RESNO = '"+LP_RESNO+"'","");
			if(cl_dat.exeDBCMT("updREXDT"))
				{setMSG(LP_RESNO+" date extended",'N');}
	}
		catch(Exception L_EX){setMSG(L_EX,"updREXDT");}
	}
	
	
	
	
	/**
	 */
	void dspPRTNM()
	{
		try
		{
			vtrPRTNM.clear();
			M_strSQLQRY="select PT_PRTNM from CO_PTMST where PT_GRPCD ='"+txtGRPCD.getText()+"' and PT_PRTTP='C'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
				{vtrPRTNM.addElement(M_rstRSSET.getString("PT_PRTNM"));}
			lstPRTNM.setListData(vtrPRTNM);
		}
		catch(Exception L_EX){setMSG(L_EX,"dspPRTNM");}
	}
	

	/**
	 */
	boolean vldDATA()
	{
		try
		{
		}catch (Exception e)
		{
			setMSG(e,"vldDATA");
			return false;
		}
		return true;
	}
	

	
	/**  Expired Reservation saving 
	 */
	void exeSAVE_EXP()
	{
		try
		{
			for(int i=0;i<tblODRSV.getRowCount();i++)
			{
				if(tblODRSV.getValueAt(i,intTB0_RESNO).toString().length()==0)
					break;
				if(tblODRSV.getValueAt(i,intTB0_REVFL).toString().equalsIgnoreCase("true"))
					exeREVERT(tblODRSV.getValueAt(i,intTB0_RESNO).toString());
				else if(tblODRSV.getValueAt(i,intTB0_EXTFL).toString().equalsIgnoreCase("true"))
					updREXDT(tblODRSV.getValueAt(i,intTB0_RESNO).toString(),tblODRSV.getValueAt(i,intTB0_NEWDT).toString());
			}
		}

		catch(Exception e)
		{
			setMSG(e,"exeSAVE_EXP");
			cl_dat.M_flgLCUPD_pbst=false;
			cl_dat.exeDBCMT("exeSAVE");
		}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	
	
	
	
	/**  Validating & Saving Data 
	 */
	void exeSAVE()
	{
		try
		{
			this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);
			if(flgEXPIRED_RES)
				exeSAVE_EXP();
			if(!vldDATA())
				return;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)&& !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				return;
			if(!chkRESNO())
				return;
			
			updateUI();
			cl_dat.M_flgLCUPD_pbst = true;
			inlTBLEDIT(tblSTMST);
			//saveRSMST();
			flgFIRST_ADD = true;
			for(int i=0;i<tblSTMST.getRowCount()&&tblSTMST.getValueAt(i,intTB1_LOTNO).toString().length()==8;i++)
			{
				if(tblSTMST.getValueAt(i,intTB1_CHKFL).toString().equals("true")  && tblSTMST.getValueAt(i,intTB1_ADJQT).toString().length()>0)
					saveSTMST(i);
			}

			for(int i=0;i<tblRSTRN.getRowCount()&&tblRSTRN.getValueAt(i,intTB2_LOTNO).toString().length()==8;i++)
			{
					{saveRSTRN(i);}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				cl_dat.exeSQLUPD("Update co_cdtrn set cmt_CHP01='Y' where CMT_CGMTP='DOC' and CMT_CGSTP='MRXXRES' and cmt_CODCD='"+strYRDGT+strWRHTP+"'","");
			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				btnPRINT.setEnabled(false);
				//btnREVERT.setEnabled(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					setMSG("Record saved successfully",'N');
					btnPRINT.setEnabled(true);
					//btnREVERT.setEnabled(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					JOptionPane.showMessageDialog(this,"Deleted/Cancelled Successfully");
				clrCOMP_1();
			}
			else
				setMSG((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) ? "Addition " : cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) ? "Modification/Authorisation" : cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "Deletion" : "")+" Operation not Successful",'E');
		}

		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
			cl_dat.M_flgLCUPD_pbst=false;
			cl_dat.exeDBCMT("exeSAVE");
		}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	

	
	
	
/**
 */	
private void saveRSTRN(int LP_ROWNO)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;

		strWHRSTR =  "RST_WRHTP = '" +strWRHTP+"' and "
					 +"RST_RESTP = '" +txtRESTP.getText()+"' and "
					 +"RST_RESNO = '" +txtRESNO.getText()+"' and "
					 +"RST_PRDTP = '" +strPRDTP+"' and "
					 +"RST_LOTNO = '" +tblRSTRN.getValueAt(LP_ROWNO,intTB2_LOTNO).toString()+"' and "
					 +"RST_RCLNO = '" +tblRSTRN.getValueAt(LP_ROWNO,intTB2_RCLNO).toString()+"'";
		flgCHK_EXIST =  chkEXIST("MR_RSTRN", strWHRSTR);

		//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		//{
		//		JOptionPane.showMessageDialog(this,"Record alreay exists in MR_RSTRNT");
		//		cl_dat.M_flgLCUPD_pbst=false;
		//		return;
		//}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="insert into MR_RSTRN (RST_WRHTP, RST_RESTP, RST_RESNO, RST_PRDTP, RST_LOTNO, RST_RCLNO, RST_PRDCD, RST_PKGTP,  RST_PRTTP, RST_GRPCD, RST_RESQT, RST_RESDT, RST_REXDT, RST_REQBY, RST_AUTBY, RST_RESDS, RST_ISSQT, RST_STSFL,   RST_LUSBY, RST_LUPDT, RST_TRNFL) values ("
			+setINSSTR("RST_WRHTP",strWRHTP,"C")
			+setINSSTR("RST_RESTP",txtRESTP.getText(),"C")
			+setINSSTR("RST_RESNO",txtRESNO.getText(),"C")
			+setINSSTR("RST_PRDTP",strPRDTP,"C")
			+setINSSTR("RST_LOTNO",tblRSTRN.getValueAt(LP_ROWNO,intTB2_LOTNO).toString(),"C")
			+setINSSTR("RST_RCLNO",tblRSTRN.getValueAt(LP_ROWNO,intTB2_RCLNO).toString(),"C")
			+setINSSTR("RST_PRDCD",txtPRDCD.getText(),"C")
			+setINSSTR("RST_PKGTP",txtPKGTP.getText(),"C")
			+setINSSTR("RST_PRTTP",strPRTTP,"C")
			+setINSSTR("RST_GRPCD",txtGRPCD.getText(),"C")
			+setINSSTR("RST_RESQT",tblRSTRN.getValueAt(LP_ROWNO,intTB2_CURQT).toString(),"N")
			+setINSSTR("RST_RESDT",txtRESDT.getText(),"D")
			+setINSSTR("RST_REXDT",txtREXDT.getText(),"D")
			+setINSSTR("RST_REQBY",txtREQBY.getText(),"C")
			+setINSSTR("RST_AUTBY",txtAUTBY.getText(),"C")
						+setINSSTR("RST_RESDS",(txtRESDS.getText().length()>20) ? txtRESDS.getText().substring(0,20) : txtRESDS.getText(),"C")
			+setINSSTR("RST_ISSQT","0.000","N")
			+setINSSTR("RST_STSFL","1","C")
			+setINSSTR("RST_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("RST_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
			+ "'0')";		//setINSSTR("RS_TRNFL","0","C")
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && flgFIRST_ADD)
				{cl_dat.exeSQLUPD("Update CO_CDTRN set CMT_CCSVL='"+txtRESNO.getText().substring(3,8)+"' where CMT_CGMTP='DOC' and CMT_CGSTP = 'MRXXRES'" ,"setLCLUPD"); flgFIRST_ADD = false;}
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update MR_RSTRN set "
			+setUPDSTR("RST_PRDCD",txtPRDCD.getText(),"C")
			+setUPDSTR("RST_PKGTP",txtPKGTP.getText(),"C")
			+setUPDSTR("RST_PRTTP",strPRTTP,"C")
			+setUPDSTR("RST_GRPCD",txtGRPCD.getText(),"C")
			+setUPDSTR("RST_RESQT",tblRSTRN.getValueAt(LP_ROWNO,intTB2_CURQT).toString(),"N")
			+setUPDSTR("RST_RESDT",txtRESDT.getText(),"D")
			+setUPDSTR("RST_REXDT",txtREXDT.getText(),"D")
			+setUPDSTR("RST_REQBY",txtREQBY.getText(),"C")
			+setUPDSTR("RST_AUTBY",txtAUTBY.getText(),"C")
			+setUPDSTR("RST_RESDS",(txtRESDS.getText().length()>20) ? txtRESDS.getText().substring(0,20) : txtRESDS.getText(),"C")
			+setUPDSTR("RST_STSFL","1","C")
			+setUPDSTR("RST_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("RST_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
			+ "RST_TRNFL = '0' "		//setUPDSTR("RST_TRNFL","0","C")
			+" where "+strWHRSTR;
		}
		//System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveRSTRN : "+L_EX,'E');}
}



/**
 */	
private void saveSTMST(int LP_ROWNO)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;

		strWHRSTR =  "ST_WRHTP = '" +strWRHTP+"' and "
					 +"ST_PRDTP = '" +strPRDTP+"' and "
					 +"ST_LOTNO = '" +tblSTMST.getValueAt(LP_ROWNO,intTB1_LOTNO).toString()+"' and "
					 +"ST_RCLNO = '" +tblSTMST.getValueAt(LP_ROWNO,intTB1_RCLNO).toString()+"' and "
					 +"ST_PKGTP = '" +txtPKGTP.getText()+"' and "
					 +"ST_MNLCD = '" +tblSTMST.getValueAt(LP_ROWNO,intTB1_MNLCD).toString()+"'";
		flgCHK_EXIST =  chkEXIST("FG_STMST", strWHRSTR);

		if(!flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record not found in Stock Master for "+ strWHRSTR);
				cl_dat.M_flgLCUPD_pbst=false;
				return;
		}
		
		M_strSQLQRY="update FG_STMST set "
		+setUPDSTR("ST_RESQT","isnull(ST_RESQT,0.000)+"+tblSTMST.getValueAt(LP_ROWNO,intTB1_ADJQT).toString(),"N")
		+ "ST_TRNFL = '0' "		//setUPDSTR("RST_TRNFL","0","C")
		+" where "+strWHRSTR;
		//System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveSTMST : "+L_EX,'E');}
}

/**
 */
private boolean chkRESNO()
{
	try
	{
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			return true;
		M_rstRSSET=cl_dat.exeSQLQRY("Select * from co_CDTRN where CMT_CGMTP='DOC' and CMT_CGSTP='MRXXRES' and cmt_CODCD='"+strYRDGT+strWRHTP+"'");
		//System.out.println("Select * from co_CDTRN where CMT_CGMTP='DOC' and CMT_CGSTP='MRXXRES' and cmt_CODCD='"+strYRDGT+strWRHTP+"'");
		if(M_rstRSSET==null || (!M_rstRSSET.next()))
			{setMSG("Res.Series not found ..",'E'); cl_dat.M_flgLCUPD_pbst = false; return false;}
		String L_strRESNO=null;
		//if(getRSTVAL(M_rstRSSET,"CMT_CHP01","C").equals("N"))
		//	{setMSG("Res. Series is in Use. Please retry after some time ..",'E'); 	return false;}
		//cl_dat.exeSQLUPD("Update co_cdtrn set cmt_CHP01='N' where CMT_CGMTP='DOC' and CMT_CGSTP='MRXXRES' and cmt_CODCD='"+strYRDGT+strWRHTP+"'","setLCLUPD");
		L_strRESNO=Integer.toString(Integer.parseInt(getRSTVAL(M_rstRSSET,"CMT_CCSVL","N"))+1);
		txtRESNO.setText(L_strRESNO);
		if(L_strRESNO.length()<5)
		{
			for(int i=0;i<=5;i++)
			{
				txtRESNO.setText("0"+txtRESNO.getText());
				if(txtRESNO.getText().length()==5)
					break;
			}
		}
		L_strRESNO = txtRESNO.getText();
		txtRESNO.setText(getRSTVAL(M_rstRSSET,"CMT_CODCD","C")+L_strRESNO);
	}
    catch(Exception L_EX)
		{setMSG(L_EX,"chkRESNO"); return false;}
	return true;
}



	/**<b>TASKS : </B><br>
	 * Source = cmbMKTTP : start thread for collecting tax details	 */
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
		}catch(Exception e){setMSG(e,"Child.FocusLost");}
	}
	

/*	
	public void mouseReleased(MouseEvent L_ME)
	{
		super.mouseReleased(L_ME);
		try
		{
			if(M_objSOURC == tblODRSV)
			{
				if(tblODRSV.getSelectedColumn() == intTB0_EXTFL)
				{
					if(tblODRSV.getValueAt(tblODRSV.getSelectedRow(),intTB0_EXTFL).toString().equals("true"))
					{
						String L_strNEWDT = setMONEND(txtRESDT.getText());
						tblODRSV.setValueAt(Boolean.FALSE,tblODRSV.getSelectedRow(),intTB0_REVFL);
						tblODRSV.setValueAt(L_strNEWDT,tblODRSV.getSelectedRow(),intTB0_NEWDT);
					}
				}
				if(tblODRSV.getSelectedColumn() == intTB0_REVFL)
				{
					if(tblODRSV.getValueAt(tblODRSV.getSelectedRow(),intTB0_REVFL).toString().equals("true"))
					{
						tblODRSV.setValueAt("",tblODRSV.getSelectedRow(),intTB0_NEWDT);
						tblODRSV.setValueAt(Boolean.FALSE,tblODRSV.getSelectedRow(),intTB0_EXTFL);
					}
				}
				if(tblODRSV.getSelectedColumn() == intTB0_REVFL || tblODRSV.getSelectedColumn() == intTB0_EXTFL)
				{
					if(tblODRSV.getValueAt(tblODRSV.getSelectedRow(),intTB0_EXTFL).toString().equals("true") || tblODRSV.getValueAt(tblODRSV.getSelectedRow(),intTB0_REVFL).toString().equals("true"))
						tblODRSV.setValueAt(Boolean.TRUE,tblODRSV.getSelectedRow(),0);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseReleased");
		}
	}

*/	
	
	
	/**
	 */
	public void mousePressed(MouseEvent L_ME)
	{
		super.mousePressed(L_ME);
		try
		{
			if (M_objSOURC==chkADDFL || M_objSOURC==chkDEDFL)
				oTBLINPVF.setSource(tblSTMST);
			//if (M_objSOURC==chkADDFL || M_objSOURC==chkDEDFL)
			//	oTBLINPVF.setSource(tblSTMST);
		}
		catch(Exception e){setMSG(e,"mousePressed");}
	}

	
	/**
	 * <b>TASKS : </b><br>
	 * Reset System idle time timer at every click<br>
	 * Copy refernce of soure to M_objSOURC
	 */
	public void mouseClicked(MouseEvent L_ME)
	{
		super.mouseClicked(L_ME);
		try
		{
		}
		catch(Exception L_EX){setMSG(L_EX,"mouseClicked");}
	}

	
	/**<b>TASKS :</b><br>
	 * &nbsp&nbsp&nbspSource=cmbPMTCD/cmbDTPCD : Show/hide related details' fields depending on item selected<br>&nbsp&nbsp&nbsp&nbsp&nbspSelection Index used for this. There fore, change in display order should br taken care
	 * &nbsp&nbsp&nbspSource=cmbINDNO : Display respective Distributor details in address and ORDNO text Fields<br>	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				btnPRINT.setEnabled(true);
				//btnREVERT.setEnabled(true);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{getODRSV(); btnPRINT.setEnabled(false);  chkRESNO();}
			}
			else if(M_objSOURC==btnPRINT)
			{
				objRPSRA.txtRESNO.setText(txtRESNO.getText());
				objRPSRA.flgOUTPRN = true;
				objRPSRA.M_strSBSCD=M_strSBSCD;
				objRPSRA.M_rdbHTML.setSelected(true);
				objRPSRA.exePRINT();
			}
		}
		catch(Exception L_EX){setMSG(L_EX,"actionPerformed");}
	
	}
	

	
	/**
	 */
	void setENBL(boolean L_STAT)
	{
		try
		{
			super.setENBL(L_STAT);
			txtWRHTP.setEnabled(true);
			txtRESTP.setEnabled(true);
			txtRESNO.setEnabled(true);
			txtRESDT.setEnabled(true);
			txtREXDT.setEnabled(true);
			txtREQBY.setEnabled(true);
			txtAUTBY.setEnabled(true);
			txtPRDCD.setEnabled(true);
			txtPKGTP.setEnabled(true);
			txtGRPCD.setEnabled(true);
			txtRESDS.setEnabled(true);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				txtRESNO.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) || !L_STAT)
			{
				txtRESDT.setEnabled(false);
				txtREXDT.setEnabled(false);
				txtREQBY.setEnabled(false);
				txtAUTBY.setEnabled(false);
				txtPRDCD.setEnabled(false);
				txtPKGTP.setEnabled(false);
				txtGRPCD.setEnabled(false);
				txtRESDS.setEnabled(false);
			}
			tblODRSV.setEnabled(false);
			tblODRSV.cmpEDITR[intTB0_CHKFL].setEnabled(true);
			tblODRSV.cmpEDITR[intTB0_REVFL].setEnabled(true);
			tblODRSV.cmpEDITR[intTB0_EXTFL].setEnabled(true);
			tblODRSV.cmpEDITR[intTB0_NEWDT].setEnabled(true);
		}
		catch(Exception L_EX) {	setMSG(L_EX,"setENBL");}
}
	
	
	/**<b>TASKS : </b><br>
	 * &nbsp&nbsp&nbspSource = txtPRDCD : HELP by buyer code(F2) as well as buyer name(F1)<br>
	 * &nbsp&nbsp&nbspSource = txtCONCD : HELP by consignee code(F2) as well as consignee name(F1)<br> 
	 * &nbsp&nbsp&nbspFocus navigation
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
		int L_intKEYCD=L_KE.getKeyCode();
		try
		{
			if(L_intKEYCD==L_KE.VK_LEFT&&L_KE.isControlDown())
				tbpMAIN.setSelectedIndex(tbpMAIN.getSelectedIndex()%tbpMAIN.getTabCount()-1);
			else if(L_intKEYCD==L_KE.VK_RIGHT&&L_KE.isControlDown())
				tbpMAIN.setSelectedIndex(tbpMAIN.getSelectedIndex()%tbpMAIN.getTabCount()+1);
			if(L_intKEYCD==L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtWRHTP)
					{txtRESTP.requestFocus();}
				else if(M_objSOURC==txtRESTP)
					{(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)? txtRESDT : txtRESNO).requestFocus();}
				else if(M_objSOURC==txtRESNO)
					{getRESDTL(); setALLCN(); txtRESDT.requestFocus();}
				else if(M_objSOURC==txtRESDT)
					{txtREXDT.requestFocus();}
				else if(M_objSOURC==txtRESDT)
					{txtREXDT.requestFocus();}
				else if(M_objSOURC==txtREXDT)
					{txtREQBY.requestFocus();}
				else if(M_objSOURC==txtREQBY)isnull
					{txtAUTBY.requestFocus();}
				else if(M_objSOURC==txtAUTBY)
					{txtPRDCD.requestFocus();}
				else if(M_objSOURC==txtPRDCD)
					{txtPKGTP.requestFocus();}
				else if(M_objSOURC==txtPKGTP)
					{txtGRPCD.requestFocus();}
				else if(M_objSOURC==txtGRPCD)
					{dspPRTNM(); txtRESDS.requestFocus();}
				else if(M_objSOURC==txtRESDS)
					{tblSTMST.requestFocus();}
			}

			if(L_intKEYCD==L_KE.VK_F1)
			{isnull
				if(M_objSOURC==txtPRDCD)
				{
					M_strHLPFLD = "txtPRDCD";
					cl_hlp("select PR_PRDCD, PR_PRDDS from CO_PRMST where SUBSTRING(PR_PRDCD,1,2) in ('51','52','53') and PR_PRDCD in (select ST_PRDCD from fg_stmst where ifnull(st_stkqt,0)>0) ORDER BY PR_PRDCD" ,2,1,new String[] {"Code","Grade"},2 ,"CT");
				}
				else if(M_objSOURC==txtRESNO)
				{
					M_strHLPFLD = "txtRESNO";
					cl_hlp("select distinct RST_RESNO,RST_RESDS from MR_RSTRN where RST_STSFL<>'X' order by RST_RESNO" ,2,1,new String[] {"Code","Grade"},2 ,"CT");
				}
				else if(M_objSOURC==txtPKGTP)
				{
					String strSQLQRY="select CMT_CODCD, CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXPKG' ORDER BY CMT_CODCD";
					M_strHLPFLD = "txtPKGTP";
					cl_hlp(strSQLQRY ,2,1,new String[] {"Code","Descr."},2 ,"CT");
				}
				else if(M_objSOURC==txtGRPCD)
				{
					String strSQisnullselect distinct PT_GRPCD, PT_PRTNM from CO_PTMST where PT_PRTTP='C' and length(ifnull(pt_grpcd,''))=5  and pt_grpcd in (select ivt_byrcd from mr_ivtrn where ivt_prdcd = '"+txtPRDCD.getText()+"') ORDER BY PT_GRPCD";
					M_strHLPFLD = "txtGRPCD";
					cl_hlp(strSQLQRY ,1,2,new String[] {"Code","Grade"},2 ,"CT");
				}
			}
		}catch(Exception e)	{setMSG(e,"Child.KeyPressed");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
/**
 */
private boolean setALLCN()
{
	String L_strSQLQRY = "";
    try
    {
		if (txtPRDCD.getText().length()!=10 || txtPKGTP.getText().length()!=2)
			return false;
		strPRDTP = txtPRDCD.getText().substring(0,2).equals("51") ? "01" : "02";
		strWHRSTMST = "ifnull(ST_STKQT,0)>0 and ST_PRDCD='"+txtPRDCD.getText()+"' and ST_PKGTP = '"+txtPKGTP.getText()+"' and ST_PRDTP = '"+strPRDTP+"'";

		crtLTMST();
		if(!crtSTMST())
			return false;
		crtRSTRN();

		inlTBLEDIT(tblSTMST);
		inlTBLEDIT(tblRSTRN);
		dspSTMST();
		dspLTMST();
		setRSTRN();
		dspRSTRN();
	}
	catch (Exception e){setMSG(e,"setALLCN");}
	return true;
}


/**
 */
private void getRESDTL()
{
	String L_strSQLQRY = "";
    try
    {
       L_strSQLQRY = "select RST_RESDT, RST_REXDT, RST_REQBY, RST_AUTBY, RST_PRDCD, RST_PKGTP, RST_GRPCD, RST_RESDS from MR_RSTRN where rst_wrhtp = '"+txtWRHTP.getText()+"' and rst_restp = '"+txtRESTP.getText()+"' and rst_resno = '"+txtRESNO.getText()+"'";
       ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);

		if (!L_rstRSSET.next() || L_rstRSSET == null)
			return;

		txtRESDT.setText(getRSTVAL(L_rstRSSET,"RST_RESDT","D"));
		txtREXDT.setText(getRSTVAL(L_rstRSSET,"RST_REXDT","D"));
		txtREQBY.setText(getRSTVAL(L_rstRSSET,"RST_REQBY","C"));
		txtAUTBY.setText(getRSTVAL(L_rstRSSET,"RST_AUTBY","C"));
		txtPRDCD.setText(getRSTVAL(L_rstRSSET,"RST_PRDCD","C"));
		txtPKGTP.setText(getRSTVAL(L_rstRSSET,"RST_PKGTP","C"));
		txtGRPCD.setText(getRSTVAL(L_rstRSSET,"RST_GRPCD","C"));
		txtRESDS.setText(getRSTVAL(L_rstRSSET,"RST_RESDS","C"));
		lblPRDDS.setText(getPRMST(txtPRDCD.getText(),"PR_PRDDS"));
		lblPKGDS.setText(getCDTRN("SYSFGXXPKG"+txtPKGTP.getText(),"CMT_CODDS",hstCDTRN));
		dspPRTNM();
	}
	catch (Exception e){setMSG(e,"getRESDTL");return;}
}




	/**
	 */
	private void dspSTMST()
	{
	    try
	    {
		double L_dblSTKQT = 0.000;
		double L_dblRESQT = 0.000;
		setHST_ARR(hstSTMST);
		for(int i=0;i<arrHSTKEY.length;i++)
		{
				L_dblSTKQT = Double.parseDouble(getSTMST(arrHSTKEY[i].toString(),"ST_STKQT"));
				L_dblRESQT = Double.parseDouble(getSTMST(arrHSTKEY[i].toString(),"ST_RESQT"));
				tblSTMST.setValueAt(getSTMST(arrHSTKEY[i].toString(),"ST_LOTNO"),i,intTB1_LOTNO);
				tblSTMST.setValueAt(getSTMST(arrHSTKEY[i].toString(),"ST_RCLNO"),i,intTB1_RCLNO);
				tblSTMST.setValueAt(getSTMST(arrHSTKEY[i].toString(),"ST_MNLCD"),i,intTB1_MNLCD);
				tblSTMST.setValueAt(getSTMST(arrHSTKEY[i].toString(),"ST_STKQT"),i,intTB1_STKQT);
				tblSTMST.setValueAt(getSTMST(arrHSTKEY[i].toString(),"ST_RESQT"),i,intTB1_RESQT);
				tblSTMST.setValueAt(setNumberFormat(L_dblSTKQT-L_dblRESQT,3),i,intTB1_AVLQT);
				tblSTMST.setValueAt(getSTMST(arrHSTKEY[i].toString(),"ST_REMDS"),i,intTB1_REMDS);
				//tblSTMST.setValueAt(getSTMST(arrHSTKEY[i].toString(),"ST_LOTDS"),i,intTB1_LOTDS);
		}
		}
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"dspSTMST");
	    }
	}


	
	/**
	 */
	private void dspLTMST()
	{
	    try
	    {
			for(int i=0;i<tblSTMST.getRowCount();i++)
			{
				if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().length()!=8)
					break;
				tblSTMST.setValueAt(getLTMST(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString(),"LT_REMDS"),i,intTB1_LOTDS);
			}
		}
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"dspLTMST");
	    }
	}
	

	/**
	 */
	private void setRSTRN()
	{
	    try
	    {
			String L_strLOTKEY = "";
			for(int i=0;i<tblSTMST.getRowCount();i++)
			{
				if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().length()!=8)
					break;
				if(!L_strLOTKEY.equals(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()))
				{
					setRSTRN1(tblSTMST.getValueAt(i,intTB1_LOTNO).toString(),tblSTMST.getValueAt(i,intTB1_RCLNO).toString());
					L_strLOTKEY = tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString();
				}
			}
		}
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"setRSTRN");
	    }
	}
	

	/**
	 */
	private void setRSTRN1(String LP_LOTNO, String LP_RCLNO)
	{
	    try
	    {
			//System.out.println("setRSTRN1 : "+LP_LOTNO+"/"+LP_RCLNO);

			double L_dblSTKQT = 0.000;
			double L_dblRESQT = 0.000;
			double L_dblCURQT = (getRSTRN(LP_LOTNO+LP_RCLNO, "RST_REFQT").length()>0) ? Double.parseDouble(getRSTRN(LP_LOTNO+LP_RCLNO, "RST_REFQT")) : 0.000;
			for(int i=0;i<tblSTMST.getRowCount();i++)
			{
				if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().length()!=8)
					break;
				if(!(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()).equals(LP_LOTNO+LP_RCLNO))
					continue;
				//System.out.println(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()+"/"+LP_LOTNO+LP_RCLNO);
				putSTMST(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()+tblSTMST.getValueAt(i,intTB1_MNLCD).toString(), "ST_RESQT", tblSTMST.getValueAt(i,intTB1_RESQT).toString());
				putSTMST(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()+tblSTMST.getValueAt(i,intTB1_MNLCD).toString(), "ST_AVLQT", tblSTMST.getValueAt(i,intTB1_AVLQT).toString());
				if(tblSTMST.getValueAt(i,intTB1_ADJQT).toString().length()>0)  putSTMST(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()+tblSTMST.getValueAt(i,intTB1_MNLCD).toString(), "ST_ADJQT", tblSTMST.getValueAt(i,intTB1_ADJQT).toString());
				//System.out.println(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()+tblSTMST.getValueAt(i,intTB1_MNLCD).toString()+"/"+tblSTMST.getValueAt(i,intTB1_ADJQT).toString());

				L_dblSTKQT += (tblSTMST.getValueAt(i,intTB1_STKQT).toString().length()>0) ? Double.parseDouble(tblSTMST.getValueAt(i,intTB1_STKQT).toString()) : 0.000;
				L_dblRESQT += (tblSTMST.getValueAt(i,intTB1_RESQT).toString().length()>0) ? Double.parseDouble(tblSTMST.getValueAt(i,intTB1_RESQT).toString()) : 0.000;
				L_dblCURQT += (tblSTMST.getValueAt(i,intTB1_ADJQT).toString().length()>0) ? Double.parseDouble(tblSTMST.getValueAt(i,intTB1_ADJQT).toString()) : 0.000;
				//System.out.println("setNumberFormat(L_dblSTKQT,3) : "+setNumberFormat(L_dblSTKQT,3));
			}
			if(!hstRSTRN.containsKey(LP_LOTNO+LP_RCLNO) && L_dblCURQT>0)  // adding record to Reservation Transaction hash table
			{
				//System.out.println("adding : "+LP_LOTNO+LP_RCLNO);
				String[] staRSTRN = new String[intRSTRN_TOT];
				staRSTRN[intAE_RST_LOTNO] = LP_LOTNO;
				staRSTRN[intAE_RST_RCLNO] = LP_RCLNO;
				staRSTRN[intAE_RST_STKQT] = setNumberFormat(L_dblSTKQT,3);
				staRSTRN[intAE_RST_RESQT] = setNumberFormat(L_dblRESQT,3);
				staRSTRN[intAE_RST_REFQT] = "0.000";
				staRSTRN[intAE_RST_CURQT] = setNumberFormat(L_dblCURQT,3);
				staRSTRN[intAE_RST_ISSQT] = "0.000";
				hstRSTRN.put(LP_LOTNO+LP_RCLNO,staRSTRN);
			}
			putRSTRN(LP_LOTNO+LP_RCLNO,"RST_STKQT",setNumberFormat(L_dblSTKQT,3));
			putRSTRN(LP_LOTNO+LP_RCLNO,"RST_RESQT",setNumberFormat(L_dblRESQT,3));
			putRSTRN(LP_LOTNO+LP_RCLNO,"RST_CURQT",setNumberFormat(L_dblCURQT,3));
		}
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"setRSTRN1");
	    }
	}
	
	
	
	/**
	 */
	private void dspRSTRN()
	{
	    try
	    {
			dblGRDTOT_RES = 0.000;
			dblGRDTOT_ISS = 0.000;
			double L_dblCURQT = 0.000;
			double L_dblISSQT = 0.000;
			tblRSTRN.clrTABLE();
			setHST_ARR(hstRSTRN);
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				tblRSTRN.setValueAt(arrHSTKEY[i].toString().substring(0,8),i,intTB2_LOTNO);
				tblRSTRN.setValueAt(arrHSTKEY[i].toString().substring(8,10),i,intTB2_RCLNO);
				tblRSTRN.setValueAt(getRSTRN(arrHSTKEY[i].toString(),"RST_STKQT"),i,intTB2_STKQT);
				tblRSTRN.setValueAt(getRSTRN(arrHSTKEY[i].toString(),"RST_RESQT"),i,intTB2_RESQT);
				
				L_dblCURQT = Double.parseDouble(getRSTRN(arrHSTKEY[i].toString(),"RST_CURQT"));
				L_dblISSQT = Double.parseDouble(getRSTRN(arrHSTKEY[i].toString(),"RST_ISSQT"));
				
				tblRSTRN.setValueAt(setNumberFormat(L_dblCURQT,3),i,intTB2_CURQT);
				tblRSTRN.setValueAt(setNumberFormat(L_dblISSQT,3),i,intTB2_ISSQT);
				tblRSTRN.setValueAt(setNumberFormat(L_dblCURQT-L_dblISSQT,3),i,intTB2_BALQT);
				dblGRDTOT_RES += L_dblCURQT;
				dblGRDTOT_ISS += L_dblISSQT;
			}
		txtRESQT.setText(setNumberFormat(dblGRDTOT_RES,3));
		txtISSQT.setText(setNumberFormat(dblGRDTOT_ISS,3));
		txtBALQT.setText(setNumberFormat(dblGRDTOT_RES-dblGRDTOT_ISS,3));
		}
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"dspRSTRN");
	    }
	}
	
	
	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	 */
	 private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable LP_HSTNM)
	{
		String L_strSQLQRY = "";
	    try
	    {
	        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp||cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
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
	                staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
	                staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
	                staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
	                staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
	                staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
	                staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
	                staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
	                LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
					hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
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
	

	/**
	 */
	 private String getSTMST(String LP_STMST_KEY, String LP_FLDNM)
    {
    try
    {
		//System.out.println("getSTMST : "+LP_STMST_KEY+"/"+LP_FLDNM);
			if(!hstSTMST.containsKey(LP_STMST_KEY))
				return "";
            if (LP_FLDNM.equals("ST_LOTNO"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_LOTNO];
            else if (LP_FLDNM.equals("ST_RCLNO"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_RCLNO];
            else if (LP_FLDNM.equals("ST_MNLCD"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_MNLCD];
            else if (LP_FLDNM.equals("ST_STKQT"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_STKQT];
            else if (LP_FLDNM.equals("ST_RESQT"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_RESQT];
            else if (LP_FLDNM.equals("ST_AVLQT"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_AVLQT];
            else if (LP_FLDNM.equals("ST_ADJQT"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_ADJQT];
            else if (LP_FLDNM.equals("ST_REMDS"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_REMDS];
            else if (LP_FLDNM.equals("ST_LOTDS"))
                    return ((String[])hstSTMST.get(LP_STMST_KEY))[intAE_ST_LOTDS];
    }
	catch (Exception L_EX)
	{
		setMSG(L_EX,"getSTMST");
	}
    return "";
    }
	 

	 
	/**
	 */
	 private String getRSTRN(String LP_RSTRN_KEY, String LP_FLDNM)
    {
	//System.out.println("getSTMST : "+LP_STMST_KEY+"/"+LP_FLDNM);
    try
    {
		//System.out.println("getRSTRN : "+LP_RSTRN_KEY+"/"+LP_FLDNM);
		if(!hstRSTRN.containsKey(LP_RSTRN_KEY))
			return "";
        if (LP_FLDNM.equals("RST_STKQT"))
                return ((String[])hstRSTRN.get(LP_RSTRN_KEY))[intAE_RST_STKQT];
        else if (LP_FLDNM.equals("RST_RESQT"))
                return ((String[])hstRSTRN.get(LP_RSTRN_KEY))[intAE_RST_RESQT];
        else if (LP_FLDNM.equals("RST_REFQT"))
                return ((String[])hstRSTRN.get(LP_RSTRN_KEY))[intAE_RST_REFQT];
        else if (LP_FLDNM.equals("RST_CURQT"))
                return ((String[])hstRSTRN.get(LP_RSTRN_KEY))[intAE_RST_CURQT];
        else if (LP_FLDNM.equals("RST_ISSQT"))
                return ((String[])hstRSTRN.get(LP_RSTRN_KEY))[intAE_RST_ISSQT];
    }
	catch (Exception L_EX)
	{
		setMSG(L_EX,"getRSTRN");
	}
    return "";
    }
	 
	 
	 
	 
	/**
	 */
	 private String getLTMST(String LP_LTMST_KEY, String LP_FLDNM)
    {
	//System.out.println("getLTMST : "+LP_STMST_KEY+"/"+LP_FLDNM);
    try
    {
			//System.out.println(LP_LTMST_KEY+"/"+LP_FLDNM);
		if(!hstLTMST.containsKey(LP_LTMST_KEY))
			return "";
        if (LP_FLDNM.equals("LT_REMDS"))
                return ((String[])hstLTMST.get(LP_LTMST_KEY))[intAE_LT_REMDS].toString();
    }
	catch (Exception L_EX)
	{
		setMSG(L_EX,"getLTMST");
	}
    return "";
    }
	 
	 
	 
	/**
	 */
	 private void putSTMST(String LP_STMST_KEY, String LP_FLDNM, String LP_FLDVL)
    {
	//System.out.println("putSTMST : "+LP_STMST_KEY+"/"+LP_FLDNM+"/"+LP_FLDVL);
    try
    {
			if(!hstSTMST.containsKey(LP_STMST_KEY))
				return;
			String[] L_staSTMST = (String[])hstSTMST.get(LP_STMST_KEY);
            if (LP_FLDNM.equals("ST_STKQT"))
				L_staSTMST[intAE_ST_STKQT]= LP_FLDVL;
            else if (LP_FLDNM.equals("ST_RESQT"))
				L_staSTMST[intAE_ST_RESQT]= LP_FLDVL;
            else if (LP_FLDNM.equals("ST_AVLQT"))
				L_staSTMST[intAE_ST_AVLQT]= LP_FLDVL;
            else if (LP_FLDNM.equals("ST_ADJQT"))
				L_staSTMST[intAE_ST_ADJQT]= LP_FLDVL;
            else if (LP_FLDNM.equals("ST_REMDS"))
				L_staSTMST[intAE_ST_REMDS]= LP_FLDVL;
            else if (LP_FLDNM.equals("ST_LOTDS"))
				L_staSTMST[intAE_ST_LOTDS]= LP_FLDVL;
			hstSTMST.put(LP_STMST_KEY,L_staSTMST);
    }
	catch (Exception L_EX)
	{
		setMSG(L_EX,"putSTMST");
	}
    }
	 

	 
	/**
	 */
	 private void putRSTRN(String LP_RSTRN_KEY, String LP_FLDNM, String LP_FLDVL)
    {
    try
    {
		//System.out.println("putRSTRN : "+LP_RSTRN_KEY+"/"+LP_FLDNM+"/"+LP_FLDVL);
			if(!hstRSTRN.containsKey(LP_RSTRN_KEY))
				return;
			String[] L_staRSTRN = (String[])hstRSTRN.get(LP_RSTRN_KEY);
            if (LP_FLDNM.equals("RST_STKQT"))
				L_staRSTRN[intAE_RST_STKQT]= LP_FLDVL;
            else if (LP_FLDNM.equals("RST_RESQT"))
				L_staRSTRN[intAE_RST_RESQT]= LP_FLDVL;
            else if (LP_FLDNM.equals("RST_CURQT"))
				L_staRSTRN[intAE_RST_CURQT]= LP_FLDVL;
            else if (LP_FLDNM.equals("RST_ISSQT"))
				L_staRSTRN[intAE_RST_ISSQT]= LP_FLDVL;
			hstRSTRN.put(LP_RSTRN_KEY,L_staRSTRN);
    }
	catch (Exception L_EX)
	{
		setMSG(L_EX,"putRSTRN");
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
			return "";
        if (LP_FLDNM.equals("CMT_CODCD"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
        else if (LP_FLDNM.equals("CMT_CODDS"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
        else if (LP_FLDNM.equals("CMT_SHRDS"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
        else if (LP_FLDNM.equals("CMT_CHP01"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
        else if (LP_FLDNM.equals("CMT_CHP02"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
        else if (LP_FLDNM.equals("CMT_NMP01"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
        else if (LP_FLDNM.equals("CMT_NMP02"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
        else if (LP_FLDNM.equals("CMT_NCSVL"))
                return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
        else if (LP_FLDNM.equals("CMT_CCSVL"))
                risnull((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
}
catch (Exception L_EX)isnullisnullisnullisnull
{
	setMSG(L_EX,"getCDTRN");
}
return "";
}
		 
		 


/** One time data capturing for Stock Master details
*	into the Hash Table
*/
private boolean crtSTMST()
{
	String L_strSQLQRY = "";
    try
    {
		strPRDTP = txtPRDCD.getText().substring(0,2).equals("51") ? "01" : "02";
		strWHRSTMST = "ifnull(ST_STKQT,0)>0 and ST_PRDCD='"+txtPRDCD.getText()+"' and ST_PKGTP = '"+txtPKGTP.getText()+"' and ST_PRDTP = '"+strPRDTP+"'";
		hstSTMST.clear();
		L_strSQLQRY = "select ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_MNLCD,(ifnull(ST_STKQT,0)-ifnull(ST_ALOQT,0)) ST_STKQT,ifnull(ST_RESQT,0) ST_RESQT,ST_REMDS from FG_STMST where "+strWHRSTMST+"  and ifnull(st_stkqt,0)>0.025 order by ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_MNLCD";
		//System.out.println(L_strSQLQRY);
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
		 if(L_rstRSSET == null || !L_rstRSSET.next())
		 {
			tblRSTRN.clrTABLE();
			tblSTMST.clrTABLE();
		    setMSG("Stock Records not found in FG_STMST",'E');
		    return false;
		 }
		 while(true)
		 {
		         strLOTNO = getRSTVAL(L_rstRSSET,"ST_LOTNO","C");
		         strRCLNO = getRSTVAL(L_rstRSSET,"ST_RCLNO","C");
		         strMNLCD = getRSTVAL(L_rstRSSET,"ST_MNLCD","C");

				 String[] staSTMST = new String[intSTMST_TOT];
		         staSTMST[intAE_ST_LOTNO] = getRSTVAL(L_rstRSSET,"ST_LOTNO","C");
		         staSTMST[intAE_ST_RCLNO] = getRSTVAL(L_rstRSSET,"ST_RCLNO","C");
		         staSTMST[intAE_ST_MNLCD] = getRSTVAL(L_rstRSSET,"ST_MNLCD","C");
		         staSTMST[intAE_ST_STKQT] = getRSTVAL(L_rstRSSET,"ST_STKQT","N");
		         staSTMST[intAE_ST_RESQT] = getRSTVAL(L_rstRSSET,"ST_RESQT","N");
		         staSTMST[intAE_ST_ADJQT] = "";
		         staSTMST[intAE_ST_REMDS] = getRSTVAL(L_rstRSSET,"ST_REMDS","C");
		         staSTMST[intAE_ST_LOTDS] = "";
		         hstSTMST.put(strLOTNO+strRCLNO+strMNLCD,staSTMST);
		         if (!L_rstRSSET.next())
		                 break;
		 }
		L_rstRSSET.close();
    }
    catch(Exception L_EX)
    {
           setMSG(L_EX,"crtSTMST");
    }
return true;
}



/** One time data capturing for Lot Master details
*	into the Hash Table
*/
private void crtLTMST()
{
	String L_strSQLQRY = "";
    try
    {
		hstLTMST.clear();
		L_strSQLQRY = "select LT_LOTNO,LT_RCLNO,LT_REMDS from PR_LTMST where LT_PRDTP = '"+strPRDTP+"' and LT_LOTNO in (select ST_LOTNO from FG_STMST where "+strWHRSTMST+") order by LT_LOTNO,LT_RCLNO";
		//System.out.println(L_strSQLQRY);
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
		 if(L_rstRSSET == null || !L_rstRSSET.next())
		 {
		      setMSG("Lot Records not found in PR_LTMST",'E');
		       return;
		 }
		 while(true)
		 {
		         strLOTNO = getRSTVAL(L_rstRSSET,"LT_LOTNO","C");
		         strRCLNO = getRSTVAL(L_rstRSSET,"LT_RCLNO","C");

				 String[] staLTMST = new String[intLTMST_TOT];
		         staLTMST[intAE_LT_LOTNO] = getRSTVAL(L_rstRSSET,"LT_LOTNO","C");
		         staLTMST[intAE_LT_RCLNO] = getRSTVAL(L_rstRSSET,"LT_RCLNO","C");
		         staLTMST[intAE_LT_REMDS] = getRSTVAL(L_rstRSSET,"LT_REMDS","N");
		         hstLTMST.put(strLOTNO+strRCLNO,staLTMST);
		         if (!L_rstRSSET.next())
		                 break;
		 }
		L_rstRSSET.close();
    }
    catch(Exception L_EX)isnullisnull
    {
           setMSG(L_EX,"crtLTMST");
    }
return;
}



/** One time data capturing for Res.Transaction details
*	into the Hash Table
*/
private void crtRSTRN()
{
	hstRSTRN.clear();
	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		return;
	String L_strSQLQRY = "";
    try
    {
		strPRDTP = txtPRDCD.getText().substring(0,2).equals("51") ? "01" : "02";
		L_strSQLQRY = "select  RST_LOTNO, RST_RCLNO, ifnull(RST_RESQT,0.000) RST_RESQT,ifnull(RST_ISSQT,0.000) RST_ISSQT from MR_RSTRN where RST_WRHTP = '"+strWRHTP+"' and RST_RESTP = '"+txtRESTP.getText()+"' and RST_RESNO = '"+txtRESNO.getText()+"' and RST_PRDTP='"+strPRDTP+"' order by RST_LOTNO, RST_RCLNO";
		//System.out.println(L_strSQLQRY);
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
		 if(L_rstRSSET == null || !L_rstRSSET.next())
		 {
		      setMSG("Stock Records not found in mr_rstrn",'E');
		       return;
		 }
		 while(true)
		 {
		         strLOTNO = getRSTVAL(L_rstRSSET,"RST_LOTNO","C");
		         strRCLNO = getRSTVAL(L_rstRSSET,"RST_RCLNO","C");
			if(Double.parseDouble(getRSTVAL(L_rstRSSET,"RST_RESQT","N"))>0)
			{
				 String[] staRSTRN = new String[intRSTRN_TOT];
		         staRSTRN[intAE_RST_LOTNO] = getRSTVAL(L_rstRSSET,"RST_LOTNO","C");
		         staRSTRN[intAE_RST_RCLNO] = getRSTVAL(L_rstRSSET,"RST_RCLNO","C");
		         staRSTRN[intAE_RST_STKQT] = "";
		         staRSTRN[intAE_RST_RESQT] = "";
		         staRSTRN[intAE_RST_REFQT] = getRSTVAL(L_rstRSSET,"RST_RESQT","N");
		         staRSTRN[intAE_RST_CURQT] = getRSTVAL(L_rstRSSET,"RST_RESQT","N");
		         staRSTRN[intAE_RST_ISSQT] = getRSTVAL(L_rstRSSET,"RST_ISSQT","N");
		         hstRSTRN.put(strLOTNO+strRCLNO,staRSTRN);
			}
		    if (!L_rstRSSET.next())
		            break;
		 }
		L_rstRSSET.close();
    }
    catch(Exception L_EX)
    {
           setMSG(L_EX,"crtRSTRN");
    }
return;
}
 
		 
/** One time data capturing for Product Master
*	into the Hash Table
*/
 private void crtPRMST()
{
	String L_strSQLQRY = "";
    try
    {
        hstPRMST.clear();
        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl <> 'X' and SUBSTRING(pr_prdcd,1,2) in ('51','52','53')";
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
        if(L_rstRSSET == null || !L_rstRSSET.next())
        {
             setMSG("Product Records not found in CO_PRMST",'E');
              return;
        }
        while(true)
        {
                strPRDCD = getRSTVAL(L_rstRSSET,"PR_PRDCD","C");
                String[] staPRMST = new String[intPRMST_TOT];
                staPRMST[intAE_PR_PRDDS] = getRSTVAL(L_rstRSSET,"PR_PRDDS","C");
                staPRMST[intAE_PR_AVGRT] = getRSTVAL(L_rstRSSET,"PR_AVGRT","N");
                hstPRMST.put(strPRDCD,staPRMST);
                if (!L_rstRSSET.next())
                        break;
        }
        L_rstRSSET.close();
    }
    catch(Exception L_EX)
    {
           setMSG(L_EX,"crtPRMST");
    }
return;
}

 
	/** Picking up Product Master Details
	 * @param LP_PRDCD		Product Code 
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
	private String getPRMST(String LP_PRDCD, String LP_FLDNM)
	{
		String L_RETSTR = "";
		try
		{
			if(!hstPRMST.containsKey(LP_PRDCD))
				return L_RETSTR;
		    String[] staPRMST = (String[])hstPRMST.get(LP_PRDCD);
		    if (LP_FLDNM.equals("PR_PRDDS"))
		            L_RETSTR = staPRMST[intAE_PR_PRDDS];
		    else if (LP_FLDNM.equals("PR_AVGRT"))
		            L_RETSTR = staPRMST[intAE_PR_AVGRT];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPRMST");
		}
		return L_RETSTR;
	}
 
 
/**
 */
private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
{
	//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
    try
    {
	if (LP_FLDTP.equals("C"))
		return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
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
	{System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);setMSG(L_EX,"getRSTVAL");}
return " ";
} 

/**To disable Combo Selection change if combo is not having focus if source is not cmbOPTN<br>
 * if source is cmbOPTN, set component states as per selection of cmbOPTN, nevigate focus to cmbMKTTP */	
public void itemStateChanged(ItemEvent L_IE)
{
	super.itemStateChanged(L_IE);
	try
	{
		M_objSOURC=L_IE.getSource();
		if(L_IE.getStateChange()!=1)
			return;
				
			
	}catch(Exception e){setMSG(e,"Child.itemStateChanged");}
}

	/**Prevents user from entering tax details before entering grade wise details	  <br>Displays list of applicable taxes in Grade - wise tax tab. i.e. taxes having zero value in common tax table*/
	public void stateChanged(ChangeEvent L_CE)
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
			return;
	}


	/**
	 */
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(getSource()==tblSTMST)
				{
					if(P_intCOLID==intTB1_ADDFL || P_intCOLID==intTB1_DEDFL || P_intCOLID==intTB1_ADJQT)
					{
						inlTBLEDIT(tblSTMST);
						btnPRINT.setEnabled(false);
						dblSTKQT_XXX = 0.000;
						dblRESQT_OLD = 0.000;
						dblAVLQT_OLD = 0.000;
						dblADJQT_OLD = 0.000;
						dblRESQT = 0.000;
						dblAVLQT = 0.000;
						dblADJQT = 0.000;
						dblRESQT_NEW = 0.000;
						dblAVLQT_NEW = 0.000;
						dblADJQT_NEW = 0.000;
						dblADJQT_ADD = 0.000;

						String L_strSTMST_KEY = tblSTMST.getValueAt(P_intROWID,intTB1_LOTNO).toString()+tblSTMST.getValueAt(P_intROWID,intTB1_RCLNO).toString()+tblSTMST.getValueAt(P_intROWID,intTB1_MNLCD).toString();
						dblSTKQT_XXX = (getSTMST(L_strSTMST_KEY,"ST_STKQT").length()>0) ? Double.parseDouble(getSTMST(L_strSTMST_KEY,"ST_STKQT")) : 0.000;
						dblRESQT_OLD = (getSTMST(L_strSTMST_KEY,"ST_RESQT").length()>0) ? Double.parseDouble(getSTMST(L_strSTMST_KEY,"ST_RESQT")) : 0.000;
						dblAVLQT_OLD = dblSTKQT_XXX - dblRESQT_OLD;
						dblADJQT_OLD = (getSTMST(L_strSTMST_KEY,"ST_ADJQT").length()>0) ? Double.parseDouble(getSTMST(L_strSTMST_KEY,"ST_ADJQT")) : 0.000;

						dblRESQT = Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(P_intROWID,intTB1_RESQT).toString(),"0.000"));
						dblAVLQT = dblSTKQT_XXX - dblRESQT;
						dblADJQT = Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(P_intROWID,intTB1_ADJQT).toString(),"0.000"));
						if(P_intCOLID==intTB1_ADJQT) dblADJQT = Double.parseDouble(nvlSTRVL(((JTextField)tblSTMST.cmpEDITR[intTB1_ADJQT]).getText(),"0.000"));
						dblADJQT_ADD = dblADJQT-dblADJQT_OLD;

						//dblADJQT = Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(P_intROWID,intTB1_ADJQT).toString(),"0.000"));
						//if(tblSTMST.getValueAt(P_intROWID,intTB1_ADJQT).toString().length()>0) L_dblADJQT_OLD = Double.parseDouble(tblSTMST.getValueAt(P_intROWID,intTB1_ADJQT).toString());
						//if(tblSTMST.getValueAt(P_intROWID,intTB1_RESQT).toString().length()>0) L_dblRESQT = Double.parseDouble(tblSTMST.getValueAt(P_intROWID,intTB1_RESQT).toString());
						//if(tblSTMST.getValueAt(P_intROWID,intTB1_AVLQT).toString().length()>0) L_dblAVLQT = Double.parseDouble(tblSTMST.getValueAt(P_intROWID,intTB1_AVLQT).toString());
						//L_dblSTKQT = (getSTMST(L_strSTMST_KEY,"ST_STKQT").length()>0) ? Double.parseDouble(getSTMST(L_strSTMST_KEY,"ST_STKQT")) : 0.000;
						//L_dblRESQT = (getSTMST(L_strSTMST_KEY,"ST_RESQT").length()>0) ? Double.parseDouble(getSTMST(L_strSTMST_KEY,"ST_RESQT")) : 0.000;

						if(P_intCOLID==intTB1_ADDFL &&  chkADDFL.isSelected() && dblAVLQT>0)
								setTB1_QTY(P_intROWID,dblAVLQT,"A",0.000,"R",dblAVLQT,"A","T","F");
						else if(P_intCOLID==intTB1_DEDFL && chkDEDFL.isSelected() && dblRESQT>0)
								setTB1_QTY(P_intROWID,0.000,"R",dblRESQT,"A",dblRESQT*-1,"A","F","T");
						else if(P_intCOLID==intTB1_ADJQT)
							setTB1_QTY(P_intROWID,dblADJQT_ADD,"A",dblADJQT_ADD*-1,"A",0.000,"A","F","F");

						dblRESQT_NEW = Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(P_intROWID,intTB1_RESQT).toString(),"0.000"));
						dblAVLQT_NEW = dblSTKQT_XXX - dblRESQT_NEW;
						dblADJQT_NEW = Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(P_intROWID,intTB1_ADJQT).toString(),"0.000"));
						if(P_intCOLID==intTB1_ADJQT) dblADJQT_NEW = Double.parseDouble(nvlSTRVL(((JTextField)tblSTMST.cmpEDITR[intTB1_ADJQT]).getText(),"0.000"));
						tblSTMST.setValueAt(Boolean.TRUE,P_intROWID,intTB1_CHKFL);

						setSTKQT_LOT(tblSTMST.getValueAt(P_intROWID,intTB1_LOTNO).toString(),tblSTMST.getValueAt(P_intROWID,intTB1_RCLNO).toString());
						boolean L_flgREVERT = false;
						if((dblRESQT_OLD+dblAVLQT_OLD)!=(dblRESQT_NEW+dblAVLQT_NEW))
							{setMSG("RESQT_OLD+AVLQT_OLD != RESQT_NEW+AVLQT_NEW  "+setNumberFormat(dblRESQT_OLD+dblAVLQT_OLD,3)+"/"+setNumberFormat(dblRESQT_NEW+dblAVLQT_NEW,3),'E'); L_flgREVERT = true;}
						if(dblRESQT_LOT_AFTER<0)
							{setMSG("New Res.Qty (Lot) : "+setNumberFormat(dblRESQT_LOT_AFTER,3),'E'); L_flgREVERT = true;}
						if(dblRESQT_NEW<0)
							{setMSG("New Res.Qty : "+setNumberFormat(dblRESQT_NEW,3),'E'); L_flgREVERT = true;}
						if(dblAVLQT_NEW<0)
							{setMSG("New Avl.Qty : "+setNumberFormat(dblAVLQT_NEW,3),'E'); L_flgREVERT = true;}
						if(dblSTKQT_LOT!=dblSTKQT_LOT_AFTER)
							{setMSG("dblSTKQT_LOT!=dblSTKQT_LOT_AFTER : "+setNumberFormat(dblSTKQT_LOT,3)+"/"+setNumberFormat(dblSTKQT_LOT_AFTER,3),'E'); L_flgREVERT = true;}
						
						if(L_flgREVERT)
						{
							setTB1_QTY(P_intROWID,dblRESQT_OLD,"R",dblAVLQT_OLD,"R",dblADJQT_OLD,"R","F","F");
							tblSTMST.setValueAt(Boolean.FALSE,P_intROWID,intTB1_CHKFL);
							setMSG("Posting not Successful",'E');
						}

						//System.out.println("RESQT : "+dblRESQT_OLD+" / "+dblRESQT_NEW);
						//System.out.println("AVLQT : "+dblAVLQT_OLD+" / "+dblAVLQT_NEW);
						//System.out.println("ADJQT : "+dblADJQT_OLD+" / "+dblADJQT_NEW);

						setRSTRN1(tblSTMST.getValueAt(P_intROWID,intTB1_LOTNO).toString(),tblSTMST.getValueAt(P_intROWID,intTB1_RCLNO).toString());
						dspRSTRN();
							
							//if(dblGRDTOT < 0 && dblGRDTOT <= dblLOTTOT && dblGRDTOT <= dblRESQT)
							//	{setMSG("Reducing this Qty. makes Grade Total = "+dblGRDTOT,'E'); tblSTMST.setValueAt("0.000",P_intROWID,intTB1_ADJQT); setLOTTOT(P_intROWID); return false;} 
							//if(dblLOTTOT < 0 && dblLOTTOT <= dblGRDTOT && dblLOTTOT <= dblRESQT)
							//	{setMSG("Reducing this Qty. makes Lot Total = "+dblLOTTOT,'E'); tblSTMST.setValueAt("0.000",P_intROWID,intTB1_ADJQT); setLOTTOT(P_intROWID); return false;}   
							//if(dblRESQT < 0 && dblRESQT <= dblGRDTOT &&  dblRESQT <= dblLOTTOT)
							//	{setMSG("Reducing this Qty. makes Overall Res.for Lot = "+dblRESQT,'E'); tblSTMST.setValueAt("0.000",P_intROWID,intTB1_ADJQT); setLOTTOT(P_intROWID); return false;} 
						
					}
				}
/*				if(getSource()==tblODRSV)
				{
					if(P_intCOLID==intTB0_EXTFL)
					{
						if(tblODRSV.getValueAt(P_intROWID,intTB0_EXTFL).toString().equals("true"))
						{
							String L_strNEWDT = setMONEND(txtRESDT.getText());
							tblODRSV.setValueAt(Boolean.FALSE,P_intROWID,intTB0_REVFL);
							tblODRSV.setValueAt(L_strNEWDT,P_intROWID,intTB0_NEWDT);
						}
					}
					if(P_intCOLID == intTB0_REVFL)
					{
						if(tblODRSV.getValueAt(P_intROWID,intTB0_REVFL).toString().equals("true"))
						{
							tblODRSV.setValueAt("",P_intROWID,intTB0_NEWDT);
							tblODRSV.setValueAt(Boolean.FALSE,P_intROWID,intTB0_EXTFL);
						}
					}
					if(P_intCOLID == intTB0_REVFL || P_intCOLID == intTB0_EXTFL)
					{
						if(tblODRSV.getValueAt(P_intROWID,intTB0_EXTFL).toString().equals("true") || tblODRSV.getValueAt(tblODRSV.getSelectedRow(),intTB0_REVFL).toString().equals("true"))
							tblODRSV.setValueAt(Boolean.TRUE,P_intROWID,0);
					}
				}*/
				
				setMSG("",'N');
			}
			catch(Exception e)
			{
				setMSG(e,"TableInputVerifier");
				setMSG("Invalid Data ..",'E');				
				return false;
			}
		return true;
		}
	}

/** Procedure to verify (Res.Qty+Avl.Qty) before Updation
 *                      & (Res.Qty+Avl.Qty) after updation  match with each other
 */	
private void setSTKQT_LOT(String LP_LOTNO, String LP_RCLNO)
{
	try
	{
		dblSTKQT_LOT = 0.000;
		dblSTKQT_LOT_AFTER = 0.000;
		dblRESQT_LOT_AFTER = 0.000;
		
		double L_dblADJQT_LOT_AFTER = 0.000;

		for(int i=0;i<tblSTMST.getRowCount();i++)
		{
			//if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().length()==0)
			//	break;
			if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().equals(LP_LOTNO) && tblSTMST.getValueAt(i,intTB1_RCLNO).toString().equals(LP_RCLNO))
			{
				//System.out.println(LP_LOTNO+"/"+LP_RCLNO);
				String L_strSTMST_KEY = tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()+tblSTMST.getValueAt(i,intTB1_MNLCD).toString();
				dblSTKQT_LOT += Double.parseDouble(getSTMST(L_strSTMST_KEY,"ST_STKQT").length()==0 ? "0.000" : getSTMST(L_strSTMST_KEY,"ST_STKQT"));
				dblSTKQT_LOT_AFTER +=	Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(i,intTB1_RESQT).toString(),"0.000")) + Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(i,intTB1_AVLQT).toString(),"0.000"));
				L_dblADJQT_LOT_AFTER +=	Double.parseDouble(nvlSTRVL(tblSTMST.getValueAt(i,intTB1_ADJQT).toString(),"0.000")) ;
			}
		}
		dblRESQT_LOT_AFTER = Double.parseDouble(getRSTRN(LP_LOTNO+LP_RCLNO,"RST_REFQT").length()==0 ? "0.000" : getRSTRN(LP_LOTNO+LP_RCLNO,"RST_REFQT"))+L_dblADJQT_LOT_AFTER;
		dblSTKQT_LOT = Double.parseDouble(setNumberFormat(dblSTKQT_LOT,3));
		dblSTKQT_LOT_AFTER = Double.parseDouble(setNumberFormat(dblSTKQT_LOT_AFTER,3));
	}
	catch(Exception L_EX){setMSG(L_EX,"setSTKQT_LOT");}
}
	
/** Adding (incrementing) Reservation, Available & Adjustment Qty and Add/Ded flag
 * for affected row of the table tblSTMST
 */
private void setTB1_QTY(int LP_ROWID,double LP_RESQT,String LP_RESFL, double LP_AVLQT,String LP_AVLFL, double LP_ADJQT,String LP_ADJFL, String LP_ADDFL, String LP_DEDFL)
{
	try
	{
		double L_dblFLDQT = 0.000;
		if(LP_RESFL.equalsIgnoreCase("R"))    // replace
			tblSTMST.setValueAt(setNumberFormat(LP_RESQT,3),LP_ROWID,intTB1_RESQT);
		else if(LP_RESQT!=0)  // add to existing qty.
			tblSTMST.setValueAt(setNumberFormat(dblRESQT+LP_RESQT,3),LP_ROWID,intTB1_RESQT);
		
		if(LP_AVLFL.equalsIgnoreCase("R"))    // replace
			tblSTMST.setValueAt(setNumberFormat(LP_AVLQT,3),LP_ROWID,intTB1_AVLQT);
		else if(LP_AVLQT!=0)  // add to existing qty.
			tblSTMST.setValueAt(setNumberFormat(dblAVLQT+LP_AVLQT,3),LP_ROWID,intTB1_AVLQT);

		if(LP_ADJFL.equalsIgnoreCase("R"))    // replace
			tblSTMST.setValueAt(setNumberFormat(LP_ADJQT,3),LP_ROWID,intTB1_ADJQT);
		else if(LP_ADJQT!=0)	 // add to existing qty.
			tblSTMST.setValueAt(setNumberFormat(dblADJQT+LP_ADJQT,3),LP_ROWID,intTB1_ADJQT);
		tblSTMST.setValueAt(Boolean.FALSE,LP_ROWID,intTB1_ADDFL);
		tblSTMST.setValueAt(Boolean.FALSE,LP_ROWID,intTB1_DEDFL);
		if(LP_ADDFL.equalsIgnoreCase("T"))
			tblSTMST.setValueAt(Boolean.TRUE,LP_ROWID,intTB1_ADDFL);
		if(LP_DEDFL.equalsIgnoreCase("T"))
			tblSTMST.setValueAt(Boolean.TRUE,LP_ROWID,intTB1_DEDFL);
	}
	catch(Exception L_EX){setMSG(L_EX,"setTB1_QTY");}
}
	
	
	
/**
 */	
private class INPVF extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			boolean L_flgRETFL = true;
			try
			{
				if(input==txtPRDCD)
				{
					L_flgRETFL = false;
					if(hstPRMST.containsKey(txtPRDCD.getText()))
					   L_flgRETFL = true;
					lblPRDDS.setText(getPRMST(txtPRDCD.getText(),"PR_PRDDS"));
				}
				else if(input==txtPKGTP)
				{
					L_flgRETFL = false;
					if(hstCDTRN.containsKey("SYSFGXXPKG"+txtPKGTP.getText()))
					   L_flgRETFL = true;
					lblPKGDS.setText(getCDTRN("SYSFGXXPKG"+txtPKGTP.getText(),"CMT_CODDS",hstCDTRN));

					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						if(!setALLCN())
							{L_flgRETFL = false; setMSG("Product/Stock details not available",'E');}
					}
				}
				else if(input==txtGRPCD)
				{
					if(!chkGROUP("C",txtGRPCD.getText(),"Invalid Group Code","Y"))		 return false;
					dspPRTNM();
				}
				else if(input==txtRESDT)
				{
					if (txtRESDT.getText().length()==10)
						txtREXDT.setText(setMONEND(txtRESDT.getText()));
				}
				else if(input==txtRESNO)
				{
					L_flgRETFL = false;
					if(txtRESNO.getText().length()==10)
						if(setALLCN())
							L_flgRETFL = true;
				}
			}catch (Exception e){setMSG(e,"INPVF");return false;}
		return true;
		}
	}




/** Validating Group code
 */
private boolean chkGROUP(String LP_PRTTP, String LP_GRPCD, String LP_MSGDS, String LP_CMPFL)
{
	boolean L_RETFL = false;
	try
	{
	if(LP_GRPCD.length()>0)
	{
		ResultSet L_rstRSSET=cl_dat.exeSQLQRY1("Select * from co_ptmst where pt_prttp = '"+LP_PRTTP+"' and pt_grpcd = '"+LP_GRPCD+"'");
					if(L_rstRSSET!=null)
					{
						L_rstRSSET.close();
						L_RETFL = true;
					}
	}
	else if (LP_CMPFL.equalsIgnoreCase("N"))
		L_RETFL = true;
	}
	catch (Exception L_EX)
	{}
	return L_RETFL;
}

/** Generating string for Insertion Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try {
	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
	if (LP_FLDTP.equals("C"))
		 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
 	else if (LP_FLDTP.equals("N"))
         return   nvlSTRVL(LP_FLDVL,"0") + ",";
 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else if (LP_FLDTP.equals("T"))
		 return   (LP_FLDVL.length()>10) ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else return " ";
        }
    catch (Exception L_EX) 
	{setMSG("Error in setINSSTR : "+L_EX,'E');}
return " ";
}
		



/** Generating string for Updation Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try {
	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
	if (LP_FLDTP.equals("C"))
		 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
 	else if (LP_FLDTP.equals("N"))
         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
	else if (LP_FLDTP.equals("T"))
		 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
	else return " ";
        }
    catch (Exception L_EX) 
	{setMSG("Error in setUPDSTR : "+L_EX,'E');}
return " ";
}





/** Initializing table editing before poulating/capturing data
 */
private void inlTBLEDIT(JTable P_tblTBLNM)
{
	if(!P_tblTBLNM.isEditing())
		return;
	P_tblTBLNM.getCellEditor().stopCellEditing();
	P_tblTBLNM.setRowSelectionInterval(0,0);
	P_tblTBLNM.setColumnSelectionInterval(0,0);
			
}

private void setHST_ARR(Hashtable LP_HSTNM)
{
	try
	{
		arrHSTKEY = LP_HSTNM.keySet().toArray();
		Arrays.sort(arrHSTKEY);
	}
	catch(Exception e){setMSG(e,"setHST_ARR");}
}


/** Checking key in table for record existance
 */
private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
{
	boolean L_flgCHKFL = false;
	try
	{
		M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if (L_rstRSSET != null && L_rstRSSET.next())
		{
			L_flgCHKFL = true;
			L_rstRSSET.close();
		}
	}
	catch (Exception L_EX)	
	{setMSG("Error in chkEXIST : "+L_EX,'E');}
	return L_flgCHKFL;
}


//************************************************************
	private void getODRSV()
	{
		try
		{
			flgEXPIRED_RES = false;
			M_strSQLQRY = "SELECT RST_RESNO,RST_RESDT,RST_PRDCD,RST_PKGTP,RST_GRPCD,RST_REXDT,"
				+"PR_PRDDS,sum(RST_RESQT) RST_RESQT,sum(RST_ISSQT) RST_ISSQT FROM MR_RSTRN,"
				+"CO_PRMST WHERE PR_PRDCD=RST_PRDCD AND RST_WRHTP='"+txtWRHTP.getText().trim()+"' "
				+"AND RST_REXDT < CURRENT_DATE GROUP BY RST_RESNO,RST_RESDT,RST_PRDCD,RST_PKGTP,"
				+"RST_GRPCD,RST_REXDT,PR_PRDDS ORDER BY RST_RESNO,RST_PRDCD ";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);

			if(tblODRSV.isEditing())
				tblODRSV.getCellEditor().stopCellEditing();
			
			if(M_rstRSSET != null)
			{
				int i = 0;
				while(M_rstRSSET.next())
				{
					tblODRSV.setValueAt(nvlSTRVL(M_rstRSSET.getString("RST_RESNO"),""),i,intTB0_RESNO);
					if(M_rstRSSET.getDate("RST_RESDT") != null)
						tblODRSV.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("RST_RESDT")),i,intTB0_RESDT);
					tblODRSV.setValueAt(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""),i,intTB0_PRDDS);
					tblODRSV.setValueAt(nvlSTRVL(M_rstRSSET.getString("RST_PKGTP"),""),i,intTB0_PKGTP);
					tblODRSV.setValueAt(nvlSTRVL(M_rstRSSET.getString("RST_GRPCD"),""),i,intTB0_GRPCD);
					tblODRSV.setValueAt(nvlSTRVL(M_rstRSSET.getString("RST_RESQT"),""),i,intTB0_RESQT);
					tblODRSV.setValueAt(nvlSTRVL(M_rstRSSET.getString("RST_ISSQT"),"0.0"),i,intTB0_ISSQT);
					tblODRSV.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("RST_RESQT"),"0.0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("RST_ISSQT"),"0.0")),3),i,intTB0_BALQT);
					if(M_rstRSSET.getDate("RST_REXDT") != null)
						tblODRSV.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("RST_REXDT")),i,intTB0_REXDT);
					i++;
				}
				if(i>0)
				{flgEXPIRED_RES = true; setENBL(false);}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getODRSV");
		}
	}

//************************************************************


}


/**************************************************************************************
This code makes use of Lotwise Approval & Sale Type Marking from PR_LTMTR
Currently Lotwise Marking & Approvals are stored directlly in ST_REMDS & dsplaed

	private Hashtable hstLTMTR_APR;		// Details of Lot marking details - Approval
	private Hashtable hstLTMTR_MRK;		// Details of Lot marking details - Sales Type marking
 
	private int intTB1_APRFL = 11;
	private int intTB1_MRKFL = 12;	//"FL","Lot No.","Locn","Stock","Res.Qty","Avl.Qty.","+","(+/-)","-","Qlty.Remark","Approvals","Lot Marking		

	hstLTMTR_APR = new Hashtable();
	hstLTMTR_MRK = new Hashtable();

	tblSTMST.setCellEditor(intTB1_APRFL,new JTextField());
	tblSTMST.setCellEditor(intTB1_MRKFL,new JTextField());

	crtLTMTR_APR();
	crtLTMTR_MRK();
	dspLTMTR_APR();
	dspLTMTR_MRK();



	private void dspLTMTR_APR()
	{
	    try
	    {
		for(int i=0;i<tblSTMST.getRowCount();i++)
		{
			if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().length()==0)
				break;
			if(hstLTMTR_APR.containsKey(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()))
			   tblSTMST.setValueAt(hstLTMTR_APR.get(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()).toString(),i,intTB1_APRFL);
		}
		}
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"dspLTMTR_APR");
	    }
	}


private void crtLTMTR_MRK()
{
	String L_strSQLQRY = "";
    try
    {
        hstLTMTR_MRK.clear();
        L_strSQLQRY = "select LTM_LOTNO,LTM_RCLNO,SUBSTRING(LTM_LTMCD,5,2) LTM_SALTP from pr_ltmtr where  ltm_ltmtp='02' and ltm_lotno||ltm_rclno in (select st_lotno||st_rclno from fg_stmst where "+strWHRSTMST+") order by ltm_lotno, ltm_rclno,SUBSTRING(ltm_ltmcd,5,2)";
		//System.out.println(L_strSQLQRY);
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
        if(L_rstRSSET == null || !L_rstRSSET.next())
        {
             setMSG("Lot marking not found in PR_LTMTR",'E');
              return;
        }
		String L_strLOTNO_OLD = getRSTVAL(L_rstRSSET,"LTM_LOTNO","C");
		String L_strRCLNO_OLD = getRSTVAL(L_rstRSSET,"LTM_RCLNO","C");
		String L_strPRTNM_MRK = "";
        while(true)
        {
			if(L_strLOTNO_OLD.equals(getRSTVAL(L_rstRSSET,"LTM_LOTNO","C")) && L_strRCLNO_OLD.equals(getRSTVAL(L_rstRSSET,"LTM_RCLNO","C")))
			{
				L_strPRTNM_MRK += getCDTRN("SYSMR00SAL"+getRSTVAL(L_rstRSSET,"LTM_SALTP","C"),"CMT_CODDS",hstCDTRN)+" / ";
				if (!L_rstRSSET.next()) break; else continue;
			}
			System.out.println("L_strPRTNM_MRK0 : "+L_strPRTNM_MRK+" : "+L_strPRTNM_MRK.length());
			if(L_strPRTNM_MRK.length()>3);
				hstLTMTR_MRK.put(L_strLOTNO_OLD+L_strRCLNO_OLD,L_strPRTNM_MRK.substring(0,L_strPRTNM_MRK.length()-3));
			L_strPRTNM_MRK = "";
			L_strLOTNO_OLD = getRSTVAL(L_rstRSSET,"LTM_LOTNO","C");
			L_strRCLNO_OLD = getRSTVAL(L_rstRSSET,"LTM_RCLNO","C");
        }
		System.out.println("L_strPRTNM_MRK : "+L_strPRTNM_MRK+" : "+L_strPRTNM_MRK.length());
		if(L_strPRTNM_MRK.length()>3);
			hstLTMTR_MRK.put(L_strLOTNO_OLD+L_strRCLNO_OLD,L_strPRTNM_MRK.substring(0,L_strPRTNM_MRK.length()-2));
        L_rstRSSET.close();
    }
    catch(Exception L_EX)
    {
           setMSG(L_EX,"crtLTMTR_MRK");
    }
return;
}


private void crtLTMTR_APR()
{
	String L_strSQLQRY = "";
    try
    {
        hstLTMTR_APR.clear();
        L_strSQLQRY = "select LTM_LOTNO,LTM_RCLNO,SUBSTRING(LTM_LTMCD,2,5) LTM_PRTCD, PT_PRTNM LTM_PRTNM from pr_ltmtr,co_ptmst where pt_prttp='C' and ltm_ltmtp='01' and SUBSTRING(ltm_ltmcd,1,1)='C' and pt_prtcd = SUBSTRING(ltm_ltmcd,2,5) and ltm_lotno||ltm_rclno in (select st_lotno||st_rclno from fg_stmst where "+strWHRSTMST+") order by ltm_lotno, ltm_rclno, SUBSTRING(ltm_ltmcd,2,5)";
		//System.out.println(L_strSQLQRY);
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
        if(L_rstRSSET == null || !L_rstRSSET.next())
        {
             setMSG("Party Records not found in PR_LTMTR",'E');
              return;
        }
		String L_strLOTNO_OLD = getRSTVAL(L_rstRSSET,"LTM_LOTNO","C");
		String L_strRCLNO_OLD = getRSTVAL(L_rstRSSET,"LTM_RCLNO","C");
		String L_strPRTNM_APR = "";
        while(true)
        {
			if(L_strLOTNO_OLD.equals(getRSTVAL(L_rstRSSET,"LTM_LOTNO","C")) && L_strRCLNO_OLD.equals(getRSTVAL(L_rstRSSET,"LTM_RCLNO","C")))
			{
				L_strPRTNM_APR += " "+getRSTVAL(L_rstRSSET,"LTM_PRTNM","C").substring(0,6)+" / ";
				if (!L_rstRSSET.next()) break; else continue;
			}
			if(L_strPRTNM_APR.length()>3)
				hstLTMTR_APR.put(L_strLOTNO_OLD+L_strLOTNO_OLD,L_strPRTNM_APR.substring(0,L_strPRTNM_APR.length()-3));
			L_strPRTNM_APR = "";
			L_strLOTNO_OLD = getRSTVAL(L_rstRSSET,"LTM_LOTNO","C");
			L_strRCLNO_OLD = getRSTVAL(L_rstRSSET,"LTM_RCLNO","C");
        }
		if(L_strPRTNM_APR.length()>3)
			hstLTMTR_APR.put(L_strLOTNO_OLD+L_strRCLNO_OLD,L_strPRTNM_APR.substring(0,L_strPRTNM_APR.length()-3));
        L_rstRSSET.close();
    }
    catch(Exception L_EX)
    {
           setMSG(L_EX,"crtLTMTR_APR");
    }
return;
}



	private void dspLTMTR_MRK()
	{
	    try
	    {
		for(int i=0;i<tblSTMST.getRowCount();i++)
		{
			//System.out.println(tblSTMST.getValueAt(i,intTB1_LOTNO).toString());
			if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().length()==0)
				break;
			if(hstLTMTR_MRK.containsKey(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()))
			   tblSTMST.setValueAt(hstLTMTR_MRK.get(tblSTMST.getValueAt(i,intTB1_LOTNO).toString()+tblSTMST.getValueAt(i,intTB1_RCLNO).toString()).toString(),i,intTB1_MRKFL);
		}
		}
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"dspLTMTR_MRK");
	    }
	}




	private void setLOTTOT(int LP_ROWID)
	{
		dblLOTTOT = 0.000;
		strLOTNO_NEW = tblSTMST.getValueAt(LP_ROWID,intTB1_LOTNO).toString();
		strRCLNO_NEW = tblSTMST.getValueAt(LP_ROWID,intTB1_RCLNO).toString();
		for(int i=0;i<tblSTMST.getRowCount();i++)
		{
			if(tblSTMST.getValueAt(i,intTB1_STKQT).toString().length()==0)
				break;
			if(tblSTMST.getValueAt(i,intTB1_ADJQT).toString().length()>0)
			{
				if(tblSTMST.getValueAt(i,intTB1_LOTNO).toString().equals(strLOTNO_NEW) && tblSTMST.getValueAt(i,intTB1_RCLNO).toString().equals(strRCLNO_NEW))
					dblLOTTOT += Double.parseDouble(tblSTMST.getValueAt(i,intTB1_ADJQT).toString());
			}
		}
		dblCURQT_REF=0.000; if(hstCURQT_REF.containsKey(strLOTNO_NEW+strRCLNO_NEW)) dblCURQT_REF = Double.parseDouble(hstCURQT_REF.get(strLOTNO_NEW+strRCLNO_NEW).toString());
		
		//if(hstCURQT.containsKey(strLOTNO_NEW+strRCLNO_NEW))
		//	hstCURQT.put(strLOTNO_NEW+strRCLNO_NEW,setNumberFormat(dblCURQT_REF+dblLOTTOT,3));
		//else if(dblCURQT_REF+dblLOTTOT>0)
		//	hstCURQT.put(strLOTNO_NEW+strRCLNO_NEW,setNumberFormat(dblCURQT_REF+dblLOTTOT,3));

		dblRESQT_REF = 0.000; if(hstRESQT_REF.containsKey(strLOTNO_NEW+strRCLNO_NEW)) dblRESQT_REF = Double.parseDouble(hstRESQT_REF.get(strLOTNO_NEW+strRCLNO_NEW).toString());
		hstRESQT.put(strLOTNO_NEW+strRCLNO_NEW,setNumberFormat(dblRESQT_REF+dblLOTTOT,3));
		//if(dblRESQT_REF+dblLOTTOT>0)
		//	hstRESQT.put(strLOTNO_NEW+strRCLNO_NEW,setNumberFormat(dblRESQT_REF+dblLOTTOT,3));
		//else if(hstRESQT.containsKey(strLOTNO_NEW+strRCLNO_NEW))
		//	hstRESQT.put(strLOTNO_NEW+strRCLNO_NEW,setNumberFormat(dblRESQT_REF+dblLOTTOT,3));
		dspRSTRN();
	}






*/	

