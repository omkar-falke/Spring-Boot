import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;

import java.util.Date;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.util.Vector;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;import java.util.Calendar;
import java.text.SimpleDateFormat;import javax.swing.*;import javax.swing.border.*;	

import java.sql.ResultSetMetaData;
import java.util.*;
class hr_rplcd extends cl_rbase 
{

	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rplcd.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;		/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;			/** DataOutputStream to hold Stream of report data.*/   
	private DataOutputStream D_OUT ;
	private JButton b; 
	private JPanel p;
	private JTextField t,txtEMPNO;
	private JRadioButton rbLVEDATE,rbLVECODE;
	private ButtonGroup bg;
	Hashtable<String, String> ht;
	String pllast,sllast,rhlast,cllast;
	String x=null;  //string to take care that if L_CNT=0 then dont display pl,cl,rh,sl for leavecode
	float pl,cl,rh,sl,sumlc;///sumlc for sum of leave count from lve_lveqt float values
	Container c;
	hr_rplcd()		/*  Constructor   */
	{
		super(2);
//		*********************************************
		add(new JLabel("Employee No.  "),6,3,1,2,this,'L');
		add((txtEMPNO=new JTextField(10)),6,4,1,1,this,'L');
		txtEMPNO.setEnabled(false);

		add((rbLVEDATE=new JRadioButton("Order by Leave Date")),8,4,1,2,this,'L');
		rbLVEDATE.setSelected(true);
		rbLVEDATE.setEnabled(false);
		add((rbLVECODE=new JRadioButton("Order by Leave Type")),9,4,1,2,this,'L');	
		rbLVEDATE.setEnabled(false);
		bg=new ButtonGroup();
		bg.add(rbLVEDATE);
		bg.add(rbLVECODE);
		
		ht = new Hashtable<String, String>();
		ht.put("0", "Applied");
		ht.put("1", "Recommended");
		ht.put("2", "Sanctioned");
		ht.put("3", "Confirmed BY HRD");
		ht.put("8", "Held For Discussion");
		ht.put("9", "Waiting For Special Sanction");
		ht.put("X", "Cancelled");
		//for tblTELVE
		
//		********************************************

		try
		{
			setMatrix(20,20);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);

			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			M_pnlRPFMT.setVisible(true);			

		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		//**********************************************
		if(L_AE.getSource()==cl_dat.M_btnSAVE_pbst)
		{
			System.out.println("display clicked");
		}
		//**********************************************
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbCMPCD_pbst.getSelectedIndex()>0)
			{
				txtEMPNO.requestFocus();
			}
			
			
			
		}
	
		
	}

	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);

		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{

				if(M_objSOURC==txtEMPNO)
				{	// JOptionPane.showMessageDialog(this, "F1 pressed");			 
					//cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtEMPNO";

					M_strSQLQRY="select EP_EMPNO,EP_FULNM from HR_EPMST";
					// M_rstRSSET=cl_dat.exeSQLQRY("select EP_EMPNO from HR_EPMST");
					cl_hlp(M_strSQLQRY,1,1,new String[]{"EMP NO","EMP NAME"},2,"CT");//2=twocolumn,CT=center

				}
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
		{if(M_strHLPFLD=="txtEMPNO")
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
														  
			//cl_dat.M_flgHELPFL_pbst = true;
			txtEMPNO.setText(L_STRTKN.nextToken());
		}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL()
	{
		try
		{
			String L_strOLDGRPCD="";
			String L_strNEWGRPCD="";
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 

			genRPHDR();
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
			setMSG(L_EX,"genRPTFL");
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
						
			int L_CNT=0;
			
			
			M_strSQLQRY="select EP_EMPNO,EP_FSTNM,EP_MDLNM,EP_LSTNM,EP_YOPCL,EP_YOPSL,EP_YOPPL,EP_YOPRH,EP_YOPDT from HR_EPMST where EP_EMPNO="+txtEMPNO.getText();			
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			//go to first record
			M_rstRSSET.next();
			cl=M_rstRSSET.getInt("EP_YOPCL");
			sl=M_rstRSSET.getInt("EP_YOPSL");
			pl=M_rstRSSET.getInt("EP_YOPPL");
			rh=M_rstRSSET.getInt("EP_YOPRH");
			String strYOPDT=M_fmtDBDAT.format(M_rstRSSET.getDate("EP_YOPDT"));
			
			////////////for displaying emp name , emp no., lve srno////////////////////////////
			String name=M_rstRSSET.getString("EP_FSTNM");
			name=name+" "+M_rstRSSET.getString("EP_MDLNM");
			name=name+" "+M_rstRSSET.getString("EP_LSTNM");
			//D_OUT.writeBytes(padSTRING('R',name[0]+" "+name[1]+" "+name[2],30));			
			if(M_rdbHTML.isSelected())D_OUT.writeBytes("<b>");

			D_OUT.writeBytes(padSTRING('C',"NAME :",10));
			D_OUT.writeBytes(padSTRING('R',name,50));

			if(M_rdbHTML.isSelected())  //for gap in html
				D_OUT.writeBytes("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

			D_OUT.writeBytes(padSTRING('C',"EMP NO :",10));					
			D_OUT.writeBytes(padSTRING('C',M_rstRSSET.getString("EP_EMPNO"),10));
			D_OUT.writeBytes(padSTRING('R',"",20));

			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");


			D_OUT.writeBytes(padSTRING('L',"SR. NO. :",15));
			
			String M_strSQLQRY2="select LVT_SRLNO from HR_LVTRN";
			ResultSet M_rstRSSET2=cl_dat.exeSQLQRY1(M_strSQLQRY2);
			M_rstRSSET2.next();			
			D_OUT.writeBytes(padSTRING('C',M_rstRSSET2.getString("LVT_SRLNO"),10));
			M_rstRSSET2.close();
			crtNWLIN();
			crtNWLIN();crtNWLIN();

			
			if(M_rdbTEXT.isSelected()) /////  header when text selected    ////////
			{
				D_OUT.writeBytes(" ______________________________________________________________________________________________________________________________________");crtNWLIN();
				D_OUT.writeBytes("|                                |                       |         |                       |    |    |                        |        |");crtNWLIN();	
				D_OUT.writeBytes("|          LEAVE  DETAILS        |       REASON          |APPLICANT|       REMARKS         |RECM|SANC|    LEAVE BALANCE       |HRD     |");crtNWLIN();			
				D_OUT.writeBytes("|________________________________|                       |         |                       |BY  |BY  |________________________|CONFMTN.|");crtNWLIN();
				D_OUT.writeBytes("|APPL |FROM | TO  |DAYS  |TYPE OF|                       |         |                       |    |    |     |     |     |      |        |");crtNWLIN();
				D_OUT.writeBytes("|ON   |     |     |      |LEAVE  |                       |         |                       |    |    |C/L  |S/L  |RH   |P/L   |        |");crtNWLIN();
				D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
				D_OUT.writeBytes("|                 Year opening balance as on : "+padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("EP_YOPDT")),54)+"|"+padSTRING('L',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPCL")),5)+"|"+padSTRING('L',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPSL")),5)+"|"+padSTRING('L',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPRH")),5)+"|"+padSTRING('L',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPPL")),5)+" |        |");crtNWLIN();
				D_OUT.writeBytes("|____________________________________________________________________________________________________|_____|_____|_____|______|________|");crtNWLIN();
			}
			
			if(M_rdbHTML.isSelected())/////  header when html selected    ////////
			{
					D_OUT.writeBytes("<br><br>");
					D_OUT.writeBytes("<table border=2 width=260 cellspacing=0 bordercolor='black'>");
					D_OUT.writeBytes("<tr><td align='center' colspan=5><b>LEAVE DETAILS  </td><td colspan=4 align='center'><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;REASON&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><b>APPLICANT</td><td><b>REMARKS</td><td align='center'colspan=1><b>RECM BY</td><td><b>SANC BY</td><td align='center' colspan=4><b>LEAVE BALANCE</td><td><b>HRD CONFMTN.</td></tr>");
					D_OUT.writeBytes("<tr><td><b>APPLIED ON</td><td><b>FROM</td><td><b>TO</td><td><b>DAYS</td><td colspan=1><b>TYPE OF LEAVE</td><td colspan=4>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td><b>C/L</td><td><b>S/L</td><td><b>RH</td><td><b>P/L</td><td>&nbsp;</td></tr>");
					//D_OUT.writeBytes("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td><b>&nbsp;</td><td><b>C/L</td><td><b>S/L</td><td><b>RH</td><td><b>PL</td><td><b>DEPT</td>");
					//D_OUT.writeBytes("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
					D_OUT.writeBytes("<tr><td colspan=4>&nbsp;</td><td align='center' colspan=7>Year opening balance as on : "+padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("EP_YOPDT")),63)+"</td><td colspan=2>&nbsp</td><td align='center'>"+padSTRING('R',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPCL")),5)+"</td><td align='center'>"+padSTRING('R',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPSL")),5)+"</td><td align='center'>"+padSTRING('R',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPRH")),5)+"</td><td align='center'>"+padSTRING('R',""+Float.parseFloat(M_rstRSSET.getString("EP_YOPPL")),5)+"<td>&nbsp;</td>");crtNWLIN();

			}
			
			M_rstRSSET.close();  //after displaying year opening balance close the resultset
			
			//String L_strSELSTR=" LVT_APPDT,LVT_LVEDT,LVT_LVECD,isnull(LVT_LVEQT,0) LVT_LVEQT,isnull(LVT_RSNDS,'') LVT_RSNDS,isnull(LVT_RCMBY,'') LVT_RCMBY,isnull(LVT_SCNBY,'') LVT_SCNBY,isnull(LVT_STSFL,'') LVT_STSFL";
			String L_strSELSTR="EP_YTDCL,EP_YTDSL,EP_YTDPL,EP_YTDRH,LVT_LVEQT,LVT_APPDT,LVT_REFDT,LVT_LVEDT,LVT_LVECD,isnull(LVT_LVEQT,0) LVT_LVEQT,isnull(LVT_RSNDS,'') LVT_RSNDS,isnull(LVT_RCMBY,'') LVT_RCMBY,isnull(LVT_SCNBY,'') LVT_SCNBY,isnull(LVT_STSFL,'') LVT_STSFL";
			String L_strWHRSTR=" LVT_EMPNO='"+txtEMPNO.getText()+"' "+" and LVT_EMPNO=EP_EMPNO AND LVT_LVEDT>='"+strYOPDT+"'";//and isnull(LVT_STSFL,'')<>'X'"; 

			M_strSQLQRY=" Select "+L_strSELSTR+" from HR_LVTRN,HR_EPMST";
			M_strSQLQRY+=" where "+L_strWHRSTR+"";
			if(rbLVEDATE.isSelected())
			M_strSQLQRY+=" order by LVT_LVEDT,LVT_APPDT,LVT_LVECD";
			if(rbLVECODE.isSelected())		
			M_strSQLQRY+=" order by LVT_LVECD,LVT_APPDT,LVT_LVEDT";
				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			
			//start with first record
			if(M_rstRSSET.next() || M_rstRSSET !=null)
			{
				//JOptionPane.showMessageDialog(this,M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")).substring(0,6)+M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")).substring(8, 10));
				String L_strLVECD,L_strRSNDS,L_strRCMBY,L_strSCNBY;
				float L_fltLVEQT=0.0f;
				java.util.Date L_datAPPDT,L_datREFDT,L_datSTRDT,L_datENDDT;
				String L_strSTSFL,dtapplied,dtstart,dtend;
				L_datAPPDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
				
				//
				L_datREFDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT")));
				L_strLVECD=M_rstRSSET.getString("LVT_LVECD");
				L_datSTRDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
				L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
				L_fltLVEQT=0.0f;	//initially we dont want the lve qty calculation done in else part	   
				L_strRSNDS=M_rstRSSET.getString("LVT_RSNDS");		   
				L_strRCMBY=M_rstRSSET.getString("LVT_RCMBY");		   
				L_strSCNBY=M_rstRSSET.getString("LVT_SCNBY");		   
				L_strSTSFL=M_rstRSSET.getString("LVT_STSFL");
									
				
				//looping from second record onwards
				while(!M_rstRSSET.isAfterLast())
				{  
					
					//////if any mismatch except date/////////////////////
					if(!L_datREFDT.equals(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT"))))
							|| !L_datAPPDT.equals(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT"))))
							|| !L_strSTSFL.equals(M_rstRSSET.getString("LVT_STSFL"))
							|| !L_strLVECD.equals(M_rstRSSET.getString("LVT_LVECD")))
					{
						dtapplied=M_fmtLCDAT.format(L_datAPPDT);
						dtstart=M_fmtLCDAT.format(L_datSTRDT);				
						dtend=M_fmtLCDAT.format(L_datENDDT);
						
							if(L_strSTSFL.equalsIgnoreCase("2") || L_strSTSFL.equalsIgnoreCase("3") )
							{	
								if(L_strLVECD.equalsIgnoreCase("PL"))
								pl=pl-L_fltLVEQT;
								if(L_strLVECD.equalsIgnoreCase("SL"))
								sl=sl-L_fltLVEQT;
								if(L_strLVECD.equalsIgnoreCase("CL"))
								cl=cl-L_fltLVEQT;
								if(L_strLVECD.equalsIgnoreCase("RH"))
								rh=rh-L_fltLVEQT;
							}
						
						/////////////////////print the record from the temp variables(previous record)////////					
						if(rbLVEDATE.isSelected()&&M_rdbTEXT.isSelected())
						{
							D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('L',cl+"",5)+ "|"+padSTRING('L',sl+"",5)+"|"+padSTRING('L',rh+"",5)+"|"+padSTRING('L',pl+"",5)+" |        |");crtNWLIN();
							D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
							
						}
						if(rbLVEDATE.isSelected()&&M_rdbHTML.isSelected())
						{
							//D_OUT.writeBytes("|"+ dtapplied.substring(0,6)+dtapplied.substring(8,10)+"|"+dtstart.substring(0,6)+dtstart.substring(8,10)+ "|"+dtend.substring(0,6)+dtend.substring(8,10)+"|"+ padSTRING('L',""+L_CNT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',(int)cl+"",3)+ "|"+padSTRING('R',(int)sl+"",3)+"|"+padSTRING('R',(int)rh+"",3)+"|"+padSTRING('R',(int)pl+"",3)+" |        |");crtNWLIN();
							D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td align='center'>"+cl+"</td><td align='center'>"+sl+"</td><td align='center'>"+rh+"</td><td align='center'>"+pl+"</td><td>&nbsp;</td></tr>");
						}
							
						
						if(rbLVECODE.isSelected()&&M_rdbTEXT.isSelected())
						{
							if(L_strLVECD.equalsIgnoreCase("RH"))
								{
								x=rh+"";
								D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',"",5)+ "|"+padSTRING('R',"",5)+"|"+padSTRING('L',x+"",5)+"|"+padSTRING('R',"",5)+" |        |");crtNWLIN();
								D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
								}
							if(L_strLVECD.equalsIgnoreCase("CL"))
								{
								x=cl+"";
								D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('L',x+"",5)+ "|"+padSTRING('R',"",5)+"|"+padSTRING('L',"",5)+"|"+padSTRING('R',"",5)+" |        |");crtNWLIN();
								D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
								}
							if(L_strLVECD.equalsIgnoreCase("SL"))
								{
								x=sl+"";
								D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',"",5)+ "|"+padSTRING('L',x+"",5)+"|"+padSTRING('L',"",5)+"|"+padSTRING('R',"",5)+" |        |");crtNWLIN();
								D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
								}
							if(L_strLVECD.equalsIgnoreCase("PL"))
								{
								 x=pl+"";
								D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',"",5)+ "|"+padSTRING('R',"",5)+"|"+padSTRING('L',"",5)+"|"+padSTRING('L',x+"",5)+" |        |");crtNWLIN();
								D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
								}
						}					
						if(rbLVECODE.isSelected()&&M_rdbHTML.isSelected())
						{
							if(L_strLVECD.equalsIgnoreCase("RH"))
							{
								
									x=rh+"";
									D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td>&nbsp;</td><td>&nbsp;</td><td align='center'>"+x+"</td><td>&nbsp;</td><td>&nbsp;</td></tr>");

								
							}
							if(L_strLVECD.equalsIgnoreCase("CL"))
							{								
								
									x=cl+"";
									D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+ "</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td align='center'>"+x+"</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");

								
							}
							if(L_strLVECD.equalsIgnoreCase("SL"))
							{
								
									x=sl+"";
									D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td>&nbsp;</td><td>"+x+"</td><td align='center'>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");

							}

							if(L_strLVECD.equalsIgnoreCase("PL"))
							{
								
									x=pl+"";
									D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td>&nbsp;</td><td>&nbsp;</td><td align='center'>&nbsp;</td><td>"+x+"</td><td>&nbsp;</td></tr>");

								

							}

						}


						
						/////////////////////store the current record///////////////////////////////
						L_datAPPDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
						L_datREFDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT")));
						L_strLVECD=M_rstRSSET.getString("LVT_LVECD");
						L_datSTRDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
						L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
						L_fltLVEQT=M_rstRSSET.getFloat("LVT_LVEQT");		   
						L_strRSNDS=M_rstRSSET.getString("LVT_RSNDS");		   
						L_strRCMBY=M_rstRSSET.getString("LVT_RCMBY");		   
						L_strSCNBY=M_rstRSSET.getString("LVT_SCNBY");		   
						L_strSTSFL=M_rstRSSET.getString("LVT_STSFL");					
						
						if((L_strSTSFL.equalsIgnoreCase("2")||L_strSTSFL.equalsIgnoreCase("3")))//for authorization
							{
							L_CNT=1;  
							}
						else 
							{L_CNT=0;						
							L_strSCNBY="";
							}
					
					}
					else //////////////if only date mismatch///////////////////
					{
						L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));
						L_fltLVEQT+=M_rstRSSET.getFloat("LVT_LVEQT");
						
						if(L_strSTSFL.equalsIgnoreCase("2")||L_strSTSFL.equalsIgnoreCase("3"))
						{
							L_CNT++;
						}
						else
							
						{
							L_strSCNBY="";
						}
					}
					M_rstRSSET.next();
					
				}// end of while
				
				////////////////////////////print the last record/////////////////////////////////////////
				{  
					dtapplied=M_fmtLCDAT.format(L_datAPPDT);
					dtstart=M_fmtLCDAT.format(L_datSTRDT);				
					dtend=M_fmtLCDAT.format(L_datENDDT);
					if(L_strSTSFL.equalsIgnoreCase("2")||L_strSTSFL.equalsIgnoreCase("3"))
					{
							if(L_strLVECD.equalsIgnoreCase("PL"))
							pl=pl-L_fltLVEQT;
							if(L_strLVECD.equalsIgnoreCase("SL"))
							sl=sl-L_fltLVEQT;
							if(L_strLVECD.equalsIgnoreCase("CL"))
							cl=cl-L_fltLVEQT;
							if(L_strLVECD.equalsIgnoreCase("RH"))
							rh=rh-L_fltLVEQT;				
						
					}
					else
					{
						L_strSCNBY="";
					}	
					   
					if(rbLVEDATE.isSelected()&&M_rdbTEXT.isSelected())
					{
						D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('L',cl+"",5)+ "|"+padSTRING('L',sl+"",5)+"|"+padSTRING('L',rh+"",5)+"|"+padSTRING('L',pl+"",5)+" |        |");crtNWLIN();
						D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
						
					}
					
					if(rbLVEDATE.isSelected()&&M_rdbHTML.isSelected())
					{
						//D_OUT.writeBytes("|"+ dtapplied.substring(0,6)+dtapplied.substring(8,10)+"|"+dtstart.substring(0,6)+dtstart.substring(8,10)+ "|"+dtend.substring(0,6)+dtend.substring(8,10)+"|"+ padSTRING('L',""+L_CNT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',(int)cl+"",3)+ "|"+padSTRING('R',(int)sl+"",3)+"|"+padSTRING('R',(int)rh+"",3)+"|"+padSTRING('R',(int)pl+"",3)+" |        |");crtNWLIN();
						D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td align='center'>"+cl+"</td><td align='center'>"+sl+"</td><td align='center'>"+rh+"</td><td align='center'>"+pl+"</td><td>&nbsp;</td></tr>");
					}
						
					
					if(rbLVECODE.isSelected()&&M_rdbTEXT.isSelected())
					{
						if(L_strLVECD.equalsIgnoreCase("RH"))
							{
							x=rh+"";
							D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',"",5)+ "|"+padSTRING('R',"",5)+"|"+padSTRING('L',x+"",5)+"|"+padSTRING('R',"",5)+" |        |");crtNWLIN();
							D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
							}
						if(L_strLVECD.equalsIgnoreCase("CL"))
							{
							x=cl+"";
							D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('L',x+"",5)+ "|"+padSTRING('R',"",5)+"|"+padSTRING('L',"",5)+"|"+padSTRING('R',"",5)+" |        |");crtNWLIN();
							D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
							}
						if(L_strLVECD.equalsIgnoreCase("SL"))
							{
							x=sl+"";
							D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',"",5)+ "|"+padSTRING('L',x+"",5)+"|"+padSTRING('L',"",5)+"|"+padSTRING('R',"",5)+" |        |");crtNWLIN();
							D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
							}
						if(L_strLVECD.equalsIgnoreCase("PL"))
							{
							 x=pl+"";
							D_OUT.writeBytes("|"+ dtapplied.substring(0,5)+"|"+dtstart.substring(0,5)+"|"+dtend.substring(0,5)+"|"+ padSTRING('L',""+L_fltLVEQT,6)+ "|  "+ L_strLVECD+ "   |"+ padSTRING('R',L_strRSNDS,23)+"|         |"+padSTRING('R',ht.get(L_strSTSFL),23)+"|"+ padSTRING('R',L_strRCMBY.toString(),4)+"|"+ padSTRING('R',L_strSCNBY.toString(),4)+"|"+padSTRING('R',"",5)+ "|"+padSTRING('R',"",5)+"|"+padSTRING('L',"",5)+"|"+padSTRING('L',x+"",5)+" |        |");crtNWLIN();
							D_OUT.writeBytes("|_____|_____|_____|______|_______|_______________________|_________|_______________________|____|____|_____|_____|_____|______|________|");crtNWLIN();
							}
					}					
					if(rbLVECODE.isSelected()&&M_rdbHTML.isSelected())
					{
						if(L_strLVECD.equalsIgnoreCase("RH"))
						{
							
								x=rh+"";
								D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td>&nbsp;</td><td>&nbsp;</td><td align='center'>"+x+"</td><td>&nbsp;</td><td>&nbsp;</td></tr>");

							
						}
						if(L_strLVECD.equalsIgnoreCase("CL"))
						{								
							
								x=cl+"";
								D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+ "</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td align='center'>"+x+"</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");

							
						}
						if(L_strLVECD.equalsIgnoreCase("SL"))
						{
							
								x=sl+"";
								D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td>&nbsp;</td><td>"+x+"</td><td align='center'>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");

						}

						if(L_strLVECD.equalsIgnoreCase("PL"))
						{
							
								x=pl+"";
								D_OUT.writeBytes("<tr><td>"+ dtapplied.substring(0,5)+"</td><td>"+dtstart.substring(0,5)+"</td><td>"+dtend.substring(0,5)+"</td><td align='center'>"+L_fltLVEQT+ "</td><td>"+ L_strLVECD+ "</td><td colspan=4>"+ padSTRING('R',L_strRSNDS,23)+"</td><td>&nbsp;</td><td>"+ht.get(L_strSTSFL)+"</td><td>"+L_strRCMBY+"</td><td>"+L_strSCNBY+"</td><td>&nbsp;</td><td>&nbsp;</td><td align='center'>&nbsp;</td><td>"+x+"</td><td>&nbsp;</td></tr>");

							

						}

					}


					cllast=cl+"";pllast=pl+"";sllast=sl+"";rhlast=rh+"";
					
					M_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
					M_rstRSSET.next();
					//compare the float values with value stored in database if mismatch *
					if(!M_rstRSSET.getString("EP_YTDCL").trim().equalsIgnoreCase(cllast))
					 cllast=cl+"*";
					else
					 cllast=cl+"";
					if(!M_rstRSSET.getString("EP_YTDSL").trim().equalsIgnoreCase(sllast))
						sllast=sl+"*";
					else sllast=sl+"";	
					if(!M_rstRSSET.getString("EP_YTDPL").trim().equalsIgnoreCase(pllast))
						pllast=pl+"*";
					else pllast=pl+"";	
					if(!M_rstRSSET.getString("EP_YTDRH").trim().equalsIgnoreCase(rhlast))
						rhlast=rh+"*";
					else rhlast=rh+"";							
					
					if(M_rdbTEXT.isSelected())
					{
						
						D_OUT.writeBytes("|                      closing balance as on : "+padSTRING('R',M_fmtLCDAT.format(new Date()),54)+"|"+padSTRING('L',cllast,5)+"|"+padSTRING('L',sllast,5)+"|"+padSTRING('L',rhlast,5)+"|"+padSTRING('L',pllast,5)+" |        |");crtNWLIN();
						D_OUT.writeBytes("|____________________________________________________________________________________________________|_____|_____|_____|______|________|");crtNWLIN();
						
					}
					if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("<tr><td colspan=4>&nbsp;</td><td colspan=7 align='center'>"+" closing balance as on : "+padSTRING('R',M_fmtLCDAT.format(new Date()),63)+"</td><td colspan=2>&nbsp;</td><td align='center'>"+cllast+"</td><td align='center'>"+sllast+"</td><td align='center'>"+rhlast+"</td><td align='center'>"+pllast+"</td><td>&nbsp;</td></tr>");crtNWLIN();

				
				}			 
				
			}
		} //end of try

		catch(Exception L_EX)
		{
			setMSG(L_EX.getMessage(),'R'); 
		}
	}

	/** generates report footer*/
	 
	void genRPFTR()
	{
		try
		{
			//initialising variables for next print
			cl_dat.M_intLINNO_pbst = 0;	

			//D_OUT.writeBytes(padSTRING('R',"",65));
			//crtNWLIN();
			D_OUT.writeBytes("\n");
			{
			//	D_OUT.writeBytes("|________|________|________|______|_______|_______________________|_________|_______________________|____|____|___|___|___|____|________|");crtNWLIN();
			}

			//D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}	

	boolean vldDATA()
	{
		try
		{

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
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			if(M_rdbHTML.isSelected())
				strRPFNM = strRPLOC + "hr_rplcd.html";
			if(M_rdbTEXT.isSelected())
				strRPFNM = strRPLOC + "hr_rplcd.doc";

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
					p  = r.exec("doPRINT(strRPFNM)"); 
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
			/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}*/
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

			}
			catch(Exception L_E)
			{
				setMSG(L_E,"INPVF");
			}
			return true;
		}
	}
}



