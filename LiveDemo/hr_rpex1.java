import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.util.Calendar;import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;import java.sql.ResultSet;
import javax.swing.*;import javax.swing.border.*;
/*

System Name    : Attendance System.
 
Program Name   : Exception Report For HRD.

Purpose        : Gives information to HRD about about Exceptions for the attendance system.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MR_PFTRN       PFT_PRTTP,PFT_PRTCD,PFT_FRMTP,PFT_FRMNO                 #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD						   #	
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtSTRDT	     -              -           -            Starting Date
txtENDDT	     -              -           -            Ending Date
cmbRPTYP         -              -           -            Report Type

////	For Pending Shift Schedules
rdbSUMRY         -              -           -            Summary Report
rdbSUMRY         -              -           -            Detail Report
txtDPTCD	CMT_CODCD		CO_CDTRN		-			 Department Code
lblDPTNM	CMT_CODDS		CO_CDTRN		-			 Department Name		
--------------------------------------------------------------------------------------

List of Fields with help facility
Field Name  Display Description         Display Columns           Table Name
---------------------------------------------------------------------------------------------
////	For Pending Shift Schedules
txtDPTCD	Department Code,Description	cmt_codcd,cmt_codds		  CO_CDTRN-SYS/COXXDPT
---------------------------------------------------------------------------------------------



Validations :
	-> From date and To date must be entered.   
	-> from date must be greter than or equal to To date.

Other  Requirements :
	////for Pending Shift Schedule
	->Summary Report and  Detail Report Can be generated.
	->Summary Of report displays departments with pending shift schedule.
	->Detail Report gives detail information of the departments and names of the employees.
 */

class hr_rpex1 extends cl_rbase 
{
	private JTextField txtSTRDT;			
	private JTextField txtENDDT;
	private JTextField txtDPTCD;
	private JComboBox cmbRPTYP;
	private JButton btnPROCS;
	
	private JRadioButton rdbSUMRY;
	private JRadioButton rdbDTAIL;
	private ButtonGroup btgRPTYP;/**Dept code for detail report                 */
	private JLabel lblDPTCD;
	private JLabel lblDPTNM;/**Lable to display Label Heading                       */
	private JLabel lblDISPL;
	private JLabel lblSTRDT;
	private JLabel lblENDDT;
	private JPanel pnlRPTYP;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpex1.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;/**count for the width of the line*/
	private int cntLINE=0;
	private String strCUR_YEAR;
	protected SimpleDateFormat fmtDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
	hr_rpex1()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,20);
			
			add(cmbRPTYP = new JComboBox(),5,7,1,6,this,'L');
						
			add(lblSTRDT = new JLabel("From Date"),7,7,1,2,this,'L');
			add(txtSTRDT = new TxtDate(),7,9,1,2,this,'L');
			add(lblENDDT = new JLabel("To Date"),8,7,1,2,this,'L');
			add(txtENDDT = new TxtDate(),8,9,1,2,this,'L');
			lblSTRDT.setText("Start Date");
			lblENDDT.setText("End Date");
			add(btnPROCS = new JButton("Process"),9,8,1,2,this,'L');
			add(lblDISPL = new JLabel(),7,7,1,5,this,'L');
			lblDISPL.setForeground(Color.blue);
		
			add(pnlRPTYP=new JPanel(null),10,7,4,10,this,'L');
			pnlRPTYP.setBorder(new EtchedBorder(Color.black,Color.lightGray));
			add(rdbSUMRY = new JRadioButton("Summary"),1,2,1,2,pnlRPTYP,'L');
			add(rdbDTAIL = new JRadioButton("Detail"),2,2,1,2,pnlRPTYP,'L');
			btgRPTYP=new ButtonGroup();
			add(lblDPTCD = new JLabel(),3,2,1,3,pnlRPTYP,'L');
			add(txtDPTCD = new TxtNumLimit(3),3,5,1,1,pnlRPTYP,'L');
			add(lblDPTNM = new JLabel(),3,6,1,5,pnlRPTYP,'L');
			btgRPTYP.add(rdbSUMRY);btgRPTYP.add(rdbDTAIL);
			lblDPTCD.setText("Department Code");
			
			rdbSUMRY.setSelected(true);			
			
			setVSBLITY(false);  // sets components visibility 
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			btnPROCS.addKeyListener(this);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			txtDPTCD.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);			
			
			cmbRPTYP.addItem("Select Report Type");
			cmbRPTYP.addItem("Pending Shift Schedule Detail");			
			cmbRPTYP.addItem("Pending Year Opening Leave Balances");			
			cmbRPTYP.addItem("Leave Balance Variations");			
			cmbRPTYP.addItem("Attendance Processing");
			
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	/**	Sets visibility of components 
	 */
	public void setVSBLITY(boolean LP_FLAG)
	{
		lblDPTCD.setVisible(LP_FLAG);
		txtDPTCD.setVisible(LP_FLAG);
		lblDPTNM.setVisible(LP_FLAG);	
		txtSTRDT.setVisible(LP_FLAG);
		txtENDDT.setVisible(LP_FLAG);
		lblSTRDT.setVisible(LP_FLAG);
		lblENDDT.setVisible(LP_FLAG);
		lblDISPL.setVisible(LP_FLAG);
		btnPROCS.setVisible(LP_FLAG);
		pnlRPTYP.setVisible(LP_FLAG);	
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
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				txtSTRDT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2,10));
				txtENDDT.setText(M_calLOCAL.getActualMaximum(Calendar.DAY_OF_MONTH)+cl_dat.M_strLOGDT_pbst.substring(2,10));
				strCUR_YEAR=cl_dat.M_strLOGDT_pbst.substring(6,10);
			}	
			if(M_objSOURC == cmbRPTYP)
			{
				setVSBLITY(false);  // sets components visibility
				if(cmbRPTYP.getSelectedIndex() == 1)
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					txtSTRDT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2,10));
					txtENDDT.setText(M_calLOCAL.getActualMaximum(Calendar.DAY_OF_MONTH)+cl_dat.M_strLOGDT_pbst.substring(2,10));
					strCUR_YEAR=cl_dat.M_strLOGDT_pbst.substring(6,10);
					txtSTRDT.setVisible(true);
					txtENDDT.setVisible(true);
					lblSTRDT.setVisible(true);
					lblENDDT.setVisible(true);
					pnlRPTYP.setVisible(true);	
				}	
				if(cmbRPTYP.getSelectedIndex() == 2)
				{
					lblDISPL.setVisible(true);
					lblDISPL.setText("        For The Year "+strCUR_YEAR);
					pnlRPTYP.setVisible(true);	
				}	
				if(cmbRPTYP.getSelectedIndex() == 3)
				{
					lblDISPL.setVisible(true);
					lblDISPL.setText("        As On "+cl_dat.M_strLOGDT_pbst);
					pnlRPTYP.setVisible(true);	
				}
				if(cmbRPTYP.getSelectedIndex() == 4)
				{
					txtSTRDT.setVisible(true);
					txtENDDT.setVisible(true);
					lblSTRDT.setVisible(true);
					lblENDDT.setVisible(true);
					btnPROCS.setVisible(true);
					txtSTRDT.setText(cl_dat.M_strLOGDT_pbst);
					txtENDDT.setText(cl_dat.M_strLOGDT_pbst);
				}
				/*if(cmbRPTYP.getSelectedIndex() != 0)
				{
					rdbSUMRY.setVisible(true);
					rdbDTAIL.setVisible(true);
				}*/
			}
			if(M_objSOURC == btnPROCS)   //// button verifies data entered
			{	
				if(txtSTRDT.getText().length() < 10)
				{
					setMSG("Please Enter Start Date",'E');
					txtSTRDT.requestFocus();
				}
				else if(txtENDDT.getText().length() < 10)
				{
					setMSG("Please Enter End Date",'E');
					txtENDDT.requestFocus();
				}
				else 
				{
					hr_teexr objTEEXR = new hr_teexr();
					objTEEXR.M_strSBSCD=M_strSBSCD;
					// shubham objTEEXR.prcDATA(txtSTRDT.getText(),txtENDDT.getText());
					objTEEXR.prcDATA(txtSTRDT.getText(),txtENDDT.getText(),"","","");
				}	
			}	

			if(M_objSOURC == rdbDTAIL)
			{
				if(rdbDTAIL.isSelected())
				{	
					lblDPTCD.setVisible(true);			
					txtDPTCD.setVisible(true);						
					lblDPTNM.setVisible(true);			
				}	
			}	
			if(M_objSOURC == rdbSUMRY)
			{
				if(rdbSUMRY.isSelected())
				{	
					lblDPTNM.setVisible(false);
					lblDPTCD.setVisible(false);
					txtDPTCD.setVisible(false);
				}	
			}
			//////////////////////////////////////////////
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed()");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC == txtDPTCD)
    			{
        		    M_strHLPFLD = "txtDPTCD";	
					/*M_strSQLQRY =" select cmt_codcd,cmt_codds";
					M_strSQLQRY+=" from co_cdtrn";
					M_strSQLQRY+=" where cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd in (select ep_dptcd from hr_epmst where substr(ep_hrsbs,1,2)='01'  and ep_lftdt is null and ep_empno not in (select ss_empno from hr_sstrn where ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'))";
					M_strSQLQRY+=" order by cmt_codcd"; */
					
					M_strSQLQRY=" Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
					M_strSQLQRY+=" and CMT_STSFL <> 'X'";
			
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Dept Code", "Dept Name"},2,"CT",new int[]{107,400});
    			}
			}			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtSTRDT)
					txtENDDT.requestFocus();
				if(M_objSOURC == txtENDDT)
					cmbRPTYP.requestFocus();
				if(M_objSOURC == txtDPTCD)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is KeyPressed");
		}	
	
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDPTCD.setText(L_STRTKN.nextToken());
			    lblDPTNM.setText(L_STRTKN.nextToken());
			}			
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL()
	{
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			String L_strKEYSTR="";
			genRPHDR();
			////report for pending shift schedule
			if(cmbRPTYP.getSelectedIndex()==1)
			{
				if(rdbSUMRY.isSelected())
				{
					M_strSQLQRY =" select cmt_codcd cdfld,cmt_codds dsfld";
					M_strSQLQRY+=" from co_cdtrn";
					M_strSQLQRY+=" where cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd in (select ep_dptcd from hr_epmst where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ltrim(str(ep_hrsbs,20,0)),1,2)='01'  and ep_lftdt is null and ep_empno not in (select ss_empno from hr_sstrn where SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'))";
					M_strSQLQRY+=" order by cmt_codcd"; 
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					crtRPFOR_SUM(M_rstRSSET);
				}
				if(rdbDTAIL.isSelected())
				{
					M_strSQLQRY =" select ep_dptcd + ' ' + cmt_codds keyfld,ep_empno cdfld,ep_lstnm + ' ' + ep_fstnm dsfld1,'' dsfld2 ";
					M_strSQLQRY+=" from hr_epmst,co_cdtrn";
					M_strSQLQRY+=" where";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY+=" ep_dptcd='"+txtDPTCD.getText()+"' and";	
					M_strSQLQRY+=" EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ltrim(str(ep_hrsbs,20,0)),1,2)='01' and ep_lftdt is null and ep_empno not in (select ss_empno from hr_sstrn where SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"')";
					M_strSQLQRY+=" and cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd=ep_dptcd";
					M_strSQLQRY+=" order by ep_dptcd,ep_empno"; 
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					crtRPFOR_DTL(M_rstRSSET);
					System.out.println(">>>>"+M_strSQLQRY);
				}
			}
			////report for Pending Year Opening Leave Balances
			if(cmbRPTYP.getSelectedIndex()==2)
			{
				L_strKEYSTR="01/01/"+strCUR_YEAR;
				if(rdbSUMRY.isSelected())
				{
					M_strSQLQRY =" select ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld";
					M_strSQLQRY+=" from hr_epmst";
					M_strSQLQRY+=" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_yopdt is null or ep_yopdt < '"+L_strKEYSTR+"' and ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					crtRPFOR_SUM(M_rstRSSET);					
				}
				else
				{	
					M_strSQLQRY =" select ep_dptcd + ' ' + cmt_codds keyfld,ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld1,'CL: ' + rtrim(ltrim(char(isnull(ep_yopcl,0)))) + ' PL: ' + rtrim(ltrim(char(isnull(ep_yoppl,0)))) + ' SL: ' + rtrim(ltrim(char(isnull(ep_yopsl,0)))) + ' RH: ' + rtrim(ltrim(char(isnull( ep_yoprh,0)))) dsfld2";
					M_strSQLQRY+=" from hr_epmst,co_cdtrn";
					M_strSQLQRY+=" where";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY+=" ep_dptcd='"+txtDPTCD.getText()+"' and";	
					M_strSQLQRY+=" cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd=ep_dptcd and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_yopdt is null or ep_yopdt < '"+L_strKEYSTR+"' and ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"'";
					M_strSQLQRY+=" order by ep_dptcd,ep_empno"; 
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					crtRPFOR_DTL(M_rstRSSET);
					System.out.println(">>>>"+M_strSQLQRY);
				}
			}
			//// report for Leave Balance Variations
			if(cmbRPTYP.getSelectedIndex()==3)
			{
				if(rdbSUMRY.isSelected())
				{
					//for CL
					M_strSQLQRY =" select ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld, ep_yopcl,ep_ytdcl,(isnull(ep_yopcl,0)-isnull(ep_ytdcl,0)) ytdcl1, sum(isnull(lvt_lveqt,0)) ytdcl2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn";
					M_strSQLQRY+=" where ep_cmpcd = lvt_cmpcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='CL'  group by ep_empno , ep_lstnm + ' ' + ep_fstnm , ep_yopcl,ep_ytdcl,(isnull(ep_yopcl,0)-isnull(ep_ytdcl,0)) having  (isnull(ep_yopcl,0)-isnull(ep_ytdcl,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);
					crtRPFOR_SUM(M_rstRSSET);					
					//for SL
					M_strSQLQRY =" select ep_empno cdfld, ep_lstnm +' '+ ep_fstnm dsfld, ep_yopsl,ep_ytdsl,(isnull(ep_yopsl,0)-isnull(ep_ytdsl,0)) ytdsl1, sum(isnull(lvt_lveqt,0)) ytdsl2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn";
					M_strSQLQRY+=" where ep_cmpcd=lvt_cmpcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='SL'  group by ep_empno , ep_lstnm +' '+ ep_fstnm , ep_yopsl,ep_ytdsl,(isnull(ep_yopsl,0)-isnull(ep_ytdsl,0)) having  (isnull(ep_yopsl,0)-isnull(ep_ytdsl,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);
					crtRPFOR_SUM(M_rstRSSET);					
					//for RH
					M_strSQLQRY =" select ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld, ep_yoprh,ep_ytdrh,(isnull(ep_yoprh,0)-isnull(ep_ytdrh,0)) ytdrh1, sum(isnull(lvt_lveqt,0)) ytdrh2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn";
					M_strSQLQRY+=" where ep_cmpcd = lvt_cmpcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='RH'  group by ep_empno , ep_lstnm + ' ' + ep_fstnm , ep_yoprh,ep_ytdrh,(isnull(ep_yoprh,0)-isnull(ep_ytdrh,0)) having  (isnull(ep_yoprh,0)-isnull(ep_ytdrh,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);
					crtRPFOR_SUM(M_rstRSSET);					
					//for PL
					M_strSQLQRY =" select ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld, ep_yoppl,ep_ytdpl,(isnull(ep_yoppl,0)-isnull(ep_ytdpl,0)) ytdpl1, sum(isnull(lvt_lveqt,0)) ytdpl2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn";
					M_strSQLQRY+=" where ep_cmpcd = lvt_cmpcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='PL'  group by ep_empno , ep_lstnm +' ' + ep_fstnm , ep_yoppl,ep_ytdpl,(isnull(ep_yoppl,0)-isnull(ep_ytdpl,0)) having  (isnull(ep_yoppl,0)-isnull(ep_ytdpl,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);
					crtRPFOR_SUM(M_rstRSSET);										
					
				}
				else
				{	
					//for CL
					M_strSQLQRY =" select ep_dptcd + ' ' + cmt_codds keyfld,ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld1,'CL: ' + ep_yopcl + ' ' + ep_ytdcl + ' ' + sum(isnull(lvt_lveqt,0)) dsfld2,(isnull(ep_yopcl,0)-isnull(ep_ytdcl,0)) ytdcl1, sum(isnull(lvt_lveqt,0)) ytdcl2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn,co_cdtrn";
					M_strSQLQRY+=" where";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY+=" ep_dptcd='"+txtDPTCD.getText()+"' and";	
					M_strSQLQRY+=" cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd=ep_dptcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and ep_cmpcd = lvt_cmpcd and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='CL'  group by ep_dptcd + ' ' + cmt_codds,ep_empno , ep_lstnm + ' ' + ep_fstnm , ep_yopcl,ep_ytdcl,(isnull(ep_yopcl,0)-isnull(ep_ytdcl,0)) having  (isnull(ep_yopcl,0)-isnull(ep_ytdcl,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);	
					crtRPFOR_DTL(M_rstRSSET);					
					//for SL
					M_strSQLQRY =" select ep_dptcd + ' ' + cmt_codds keyfld,ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld1, 'SL: ' + ep_yopsl  + ' ' + ep_ytdsl + ' ' + sum(isnull(lvt_lveqt,0)) dsfld2,(isnull(ep_yopsl,0)-isnull(ep_ytdsl,0)) ytdsl1, sum(isnull(lvt_lveqt,0)) ytdsl2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn,co_cdtrn";
					M_strSQLQRY+=" where";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY+=" ep_dptcd='"+txtDPTCD.getText()+"' and";	
					M_strSQLQRY+=" cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd=ep_dptcd and ep_cmpcd = lvt_cmpcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='SL'  group by ep_dptcd + ' ' + cmt_codds,ep_empno , ep_lstnm + ' ' + ep_fstnm , ep_yopsl,ep_ytdsl,(isnull(ep_yopsl,0)-isnull(ep_ytdsl,0)) having  (isnull(ep_yopsl,0)-isnull(ep_ytdsl,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);	
					crtRPFOR_DTL(M_rstRSSET);					
					//for RH
					M_strSQLQRY =" select ep_dptcd + ' ' + cmt_codds keyfld,ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld1, 'RH: ' + ep_yoprh + ' ' + ep_ytdrh + ' ' + sum(isnull(lvt_lveqt,0)) dsfld2,(isnull(ep_yoprh,0)-isnull(ep_ytdrh,0)) ytdrh1, sum(isnull(lvt_lveqt,0)) ytdrh2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn,co_cdtrn";
					M_strSQLQRY+=" where";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY+=" ep_dptcd='"+txtDPTCD.getText()+"' and";	
					M_strSQLQRY+=" cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd=ep_dptcd and ep_cmpcd = lvt_cmpcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='RH'  group by ep_dptcd + ' ' + cmt_codds,ep_empno , ep_lstnm + ' ' + ep_fstnm , ep_yoprh,ep_ytdrh,(isnull(ep_yoprh,0)-isnull(ep_ytdrh,0)) having  (isnull(ep_yoprh,0)-isnull(ep_ytdrh,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);	
					crtRPFOR_DTL(M_rstRSSET);					
					//for PL
					M_strSQLQRY =" select ep_dptcd + ' ' + cmt_codds keyfld,ep_empno cdfld, ep_lstnm + ' ' + ep_fstnm dsfld1, ep_yoppl + ' ' + ep_ytdpl + ' ' + sum(isnull(lvt_lveqt,0)) dsfld2,(isnull(ep_yoppl,0)-isnull(ep_ytdpl,0)) ytdpl1, sum(isnull(lvt_lveqt,0)) ytdpl2";
					M_strSQLQRY+=" from hr_epmst, hr_lvtrn,co_cdtrn";
					M_strSQLQRY+=" where";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY+=" ep_dptcd='"+txtDPTCD.getText()+"' and";	
					M_strSQLQRY+=" cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd=ep_dptcd and ep_hrsbs=lvt_sbscd and ep_empno=lvt_empno and ep_cmpcd = lvt_cmpcd and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ep_lftdt is null and ep_hrsbs = '"+M_strSBSCD+"' and lvt_lvecd='PL'  group by ep_dptcd +' ' + cmt_codds,ep_empno , ep_lstnm +' ' + ep_fstnm , ep_yoppl,ep_ytdpl,(isnull(ep_yoppl,0)-isnull(ep_ytdpl,0)) having  (isnull(ep_yoppl,0)-isnull(ep_ytdpl,0))<> sum(isnull(lvt_lveqt,0))";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);			
					System.out.println(">>M_strSQLQRY"+M_strSQLQRY);	
					crtRPFOR_DTL(M_rstRSSET);					
					
				}
			}
			crtNWLIN();
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			if(M_rstRSSET==null)
			{
				M_rstRSSET.close();
			}	
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >70)
				{
					genRPFTR();
					genRPHDR();
				}
			}	
			else if(cl_dat.M_intLINNO_pbst >60)
			{		
				genRPFTR();
				genRPHDR();			
			}			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

	void genRPHDR()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
			
				
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				D_OUT.writeBytes("<HTML><HEAD><Title>Exception Report for Attendance System</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst,10));
    		crtNWLIN();
			
			////for pending shift schedule entry
			if(cmbRPTYP.getSelectedIndex()==1 && rdbSUMRY.isSelected())
				crtHDRFOR("Pending Shift Schedule From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"                                 Department Code          Department Name");	
			else if(cmbRPTYP.getSelectedIndex()==1 && rdbDTAIL.isSelected())
				crtHDRFOR("Pending Shift Schedule From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),"Dept Code   Dept Name           Empl No.   Empl Name");	
			
			////for pending Year Opening entry	
			if(cmbRPTYP.getSelectedIndex()==2 && rdbSUMRY.isSelected())
				crtHDRFOR("Pending Year Opening Leave Balances for year "+strCUR_YEAR,"                                     Employee No          Employee Name");	
			else if(cmbRPTYP.getSelectedIndex()==2 && rdbDTAIL.isSelected())
				crtHDRFOR("Pending Year Opening Leave Balances For Year "+strCUR_YEAR,"Dept Code and Dept Name         Empl No and Empl Name           CL/PL/SL/RH Balance");	

			////Leave Balance Variations
			if(cmbRPTYP.getSelectedIndex()==3 && rdbSUMRY.isSelected())
				crtHDRFOR("Leave Balance Variations As On "+cl_dat.M_strLOGDT_pbst,"                                     Employee No          Employee Name");	
			else if(cmbRPTYP.getSelectedIndex()==3 && rdbDTAIL.isSelected())
				crtHDRFOR("Leave Balance Variations as on "+cl_dat.M_strLOGDT_pbst,"Dept Code and Dept Name         Empl No and Empl Name           Leave Balance YOP/YTD/Leaves");	
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	
	
	/**method that creates report for selected index
	 */
	private void crtRPFOR_SUM(ResultSet LP_RSSET)
	{
		try
		{
			if(LP_RSSET!=null)
			{
				while(LP_RSSET.next())
				{
					D_OUT.writeBytes(padSTRING('L',LP_RSSET.getString("cdfld"),48));			
					D_OUT.writeBytes(padSTRING('L',"",10));			
					D_OUT.writeBytes(padSTRING('R',LP_RSSET.getString("dsfld"),40));			
					crtNWLIN();
				}
			}
			else
				setMSG("No Data Found",'E');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtRPFOR_SUM");
		}
	}

	
/**method that creates report for selected index
	 */
	private void crtRPFOR_DTL(ResultSet LP_RSSET)
	{
		try
		{
			String L_strOLD_KEY="";
			String L_strNEW_KEY="";
			if(LP_RSSET!=null)
			{
				while(LP_RSSET.next())
				{
					if(!LP_RSSET.getString("keyfld").equals(""))
					{	
						L_strOLD_KEY=L_strNEW_KEY;
						L_strNEW_KEY=LP_RSSET.getString("keyfld");
						if(!L_strOLD_KEY.equals(L_strNEW_KEY))      // prints common dept name and dept codes only once.
						{
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',LP_RSSET.getString("keyfld"),25));			
						}
						else
							D_OUT.writeBytes(padSTRING('L',"",25));			
					}
					D_OUT.writeBytes(padSTRING('L',LP_RSSET.getString("cdfld"),11));			
					D_OUT.writeBytes(padSTRING('L',"",3));			
					D_OUT.writeBytes(padSTRING('R',LP_RSSET.getString("dsfld1"),25));
					D_OUT.writeBytes(padSTRING('R',LP_RSSET.getString("dsfld2"),50));
					crtNWLIN();
				}
			}
			else
				setMSG("No Data Found",'E');					
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtRPFOR");
		}
	}
	/**method that creates header for selected index
	 */
	private void crtHDRFOR(String LP_HDRR00, String LP_HDRKY)
	{
		try
		{
    			D_OUT.writeBytes(padSTRING('R',LP_HDRR00,60));
    			D_OUT.writeBytes(padSTRING('L',"Page No    : ",30));
				D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    			prnFMTCHR(D_OUT,M_strNOBOLD);

				crtNWLIN();
    			crtNWLIN();
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes(LP_HDRKY);
				crtNWLIN();
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
				crtNWLIN();
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"crtHDRFOR");
		}	
	}	
	

	/**method that creates footer for selected index by cntLINE
	 */
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"genRPFTR");
		}
	}	

	boolean vldDATA()
	{
		try
		{
			if(cmbRPTYP.getSelectedIndex() == 0)
			{
				setMSG("Please Select The Report You Want To Display",'E');
				cmbRPTYP.requestFocus();
				return false;
			}	
			if(txtSTRDT.getText().length() == 0)
			{
				setMSG("Please Enter Start Date",'E');
				txtSTRDT.requestFocus();
				return false;
			}	
			if(txtENDDT.getText().length() == 0)
			{
				setMSG("Please Enter End Date",'E');
				txtENDDT.requestFocus();
				return false;
			}	
		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA()");
		}
		return true;
	}
	
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rpex1.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpex1.doc";
				
			genRPTFL();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				}	
				else 
			    {    
					Runtime r = Runtime.getRuntime();
					Process p = null;					    
					p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
			    if(M_rdbHTML.isSelected())
			        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			    else
			        p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		
	}
	
	
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
				return true;			
			if(input==txtDPTCD)
			{			
				/*M_strSQLQRY =" select cmt_codcd,cmt_codds";
				M_strSQLQRY+=" from co_cdtrn";
				M_strSQLQRY+=" where cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd in (select ep_dptcd from hr_epmst where substr(ep_hrsbs,1,2)='01'  and ep_lftdt is null and ep_empno not in (select ss_empno from hr_sstrn where ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"')) and";
				M_strSQLQRY+=" cmt_codcd='"+txtDPTCD.getText()+"'";
				M_strSQLQRY+=" order by cmt_codcd"; */
				
				M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
				M_strSQLQRY+=" and CMT_STSFL <> 'X' and CMT_CODCD='"+txtDPTCD.getText().trim()+"'";
			
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblDPTNM.setText(M_rstRSSET.getString("cmt_codds"));
					return true;
				}	
				else 
				{
					setMSG("Enter Valid Department Code",'E');
					return false;	
				}
			}	
			if(input == txtENDDT)
			{
				if(txtSTRDT.getText().length()>0 && M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
}



