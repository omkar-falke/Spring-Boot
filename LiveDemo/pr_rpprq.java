/*
System Name : Production Request System.
Program Name : Production request.
Source Directory : d:\source\splerp3\pr_rpprq.java                        Executable Directory : d:\exec\splerp3\pr_rpprq.class

Purpose : This module displays production request report.

List of tables used :
Table Name		Primary key							                      Operation done
													                      Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
pr_rmmst        rm_cmpcd,rm_trntp,rm_docno,rm_doctp,rm_prdtp
pr_rqtrn		rq_prdtp,rq_cmpcd,rq_docno					    
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	    TextField Name			 Type/Size	       Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtDOCNO        Production No.            varchar/8         Production no.
txtPRDT         Production date          date/             Production request date   

-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description		Display Columns			                                 Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
Production No.                      Production No,Production request date,Description        pr_rqtrn,CO_CDTRN

-----------------------------------------------------------------------------------------------------------------------------------------------------
Validations :
->Enter Production no.
->Enter valid Production no.

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.util.StringTokenizer;import java.util.Enumeration;

class pr_rpprq extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"pr_rpprq.doc";
	
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    private JTextField txtDOCNO;
    private JTextField txtDOCDT;
    private String strPRNO="";
    private String strDOCNO ="";
    private String strDOCDT ="";
    private String strDOTLN ="________________________________________________________________________________________________________________________";
    
    private String strDOTLN2 = "__________________________________________________________________________________________";
                               
    private String strDOTLN1 ="---------------------------------------------------------------------";
    String strPRDCD,strREQQT,strPRTNM,strREQDT,strREMDS,strSTKQT,strUCLQT,strRESQT,strQLHQT,strTARQT,strPORQT,strEMPNM;
    private INPVF oINPVF;
    private JPanel pnlPRNO;
	pr_rpprq()		/*  Constructor   */
	{
		super(1);
		try
		{
			dspCOMPS();	
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	pr_rpprq(String LP_strDOCNO,String LP_strDOCDT)		/*  Constructor   */
	{
		super(1);
		try
		{
			dspCOMPS();	
			strDOCNO=LP_strDOCNO;
			strDOCDT=LP_strDOCDT;
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	void dspCOMPS()		/*  Constructor   */
	{
		try
		{
			setMatrix(20,20);			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			
			pnlPRNO = new JPanel();
			setMatrix(20,8);
			pnlPRNO.setLayout(null);
			add(new JLabel("PR No."),1,2,1,1,pnlPRNO,'L');   
		    add(txtDOCNO = new TxtLimit(8),1,3,1,1,pnlPRNO,'L'); 
		    add(new JLabel("PR Date"),1,4,1,1,pnlPRNO,'L');   
		    add(txtDOCDT = new TxtDate(),1,5,1,1,pnlPRNO,'L');
		    pnlPRNO.setBorder(BorderFactory.createTitledBorder(null,"PRODUCTION REQUEST",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlPRNO,6,2,2,6,this,'L');
		    oINPVF=new INPVF();
			txtDOCNO.setInputVerifier(oINPVF);
			M_rdbHTML.setSelected(true);
		    M_pnlRPFMT.setVisible(false);
		   	setENBL(true);
			txtDOCDT.setEnabled(false);
			
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"dspCOMPS");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
			  if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			  {
				  txtDOCDT.setEnabled(false);
				  txtDOCNO.requestFocus();
			  }
			}
			
			if(M_objSOURC == txtDOCNO)
			{
				if(txtDOCNO.getText().length() == 0)
				{
				  txtDOCDT.setText("");
				}
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
				if(M_objSOURC == txtDOCNO)
				{
						M_strHLPFLD = "txtDOCNO";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
						
						M_strSQLQRY=" SELECT RQ_DOCNO,RQ_DOCDT,CMT_CODDS from PR_RQTRN,CO_CDTRN where isnull(RQ_STSFL,'')<>'XX' ";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)
						   ||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
							M_strSQLQRY += " and RQ_STSFL IN('00','01')";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							M_strSQLQRY += " and RQ_STSFL IN('00','01')";
					   //M_strSQLQRY += " AND RQ_PRDTP ='"+M_strSBSCD.substring(2,4)+"'";
						if(txtDOCNO.getText().length() >0)				
							M_strSQLQRY += " AND RQ_DOCNO like '"+txtDOCNO.getText().trim()+"%'";
						M_strSQLQRY += " AND CMT_CGMTP='STS' AND CMT_CGSTP='PRXXREQ' AND CMT_CODCD=RQ_STSFL";
						M_strSQLQRY += " order by RQ_DOCNO";
						//System.out.println("txtDOCNO>>"+M_strSQLQRY);
						cl_hlp(M_strSQLQRY,1,1,new String[]{"PR No","PR Date","Status"},3,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
						
				}	
			}	
			
			if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{	
				if(M_objSOURC == txtDOCNO)
				{
					txtDOCDT.requestFocus();
				}
				if(M_objSOURC == txtDOCDT)
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
			if(M_strHLPFLD.equals("txtDOCNO"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDOCNO.setText(L_STRTKN.nextToken());
				txtDOCDT.setText(L_STRTKN.nextToken());		  
			}
			
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL(String P_strPRNO,String P_strPRDT)
	{
		if(!vldDATA())
			return;
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			genRPHDR();	
			M_strSQLQRY = "select rq_docno,rq_docdt, pt_prtnm,rq_reqby,pr_prdds,rq_reqqt,rq_reqdt,rq_prdtp,rm_remds,rq_stkqt,rq_porqt,rq_uclqt,rq_resqt,rq_qlhqt,rq_tarqt,us_usrnm ";
			M_strSQLQRY += " from pr_rqtrn,co_ptmst,co_prmst,pr_rmmst,sa_usmst";
			M_strSQLQRY += " where us_usrcd= rq_lusby and rq_cmpcd = rm_cmpcd and pt_prtcd = rq_prtcd and rq_prdtp = rm_prdtp and rq_docno = rm_docno and ltrim(str(rq_prdcd,20,0))=pr_prdcd and rm_trntp='RQ' and rq_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and rq_docno='"+P_strPRNO+"' and pt_prttp ='C'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
				   strPRDCD = M_rstRSSET.getString("pr_prdds");
				   strREQQT = M_rstRSSET.getString("rq_reqqt");
				   strPRTNM = M_rstRSSET.getString("pt_prtnm");
				   strREQDT = (M_fmtLCDAT.format(M_rstRSSET.getDate("rq_reqdt")));
				   strREMDS = M_rstRSSET.getString("rm_remds");
				   strSTKQT = nvlSTRVL(M_rstRSSET.getString("rq_stkqt"),"-");
				   strPORQT = nvlSTRVL(M_rstRSSET.getString("rq_porqt"),"-");
				   strUCLQT = nvlSTRVL(M_rstRSSET.getString("rq_uclqt"),"-");
				   strRESQT = nvlSTRVL(M_rstRSSET.getString("rq_resqt"),"-");
				   strQLHQT = nvlSTRVL(M_rstRSSET.getString("rq_qlhqt"),"-");
				   strTARQT = nvlSTRVL(M_rstRSSET.getString("rq_tarqt"),"-");
				   strEMPNM = M_rstRSSET.getString("us_usrnm");
				}
			}
			 M_rstRSSET.close();
		    			 
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<HTML>");
				D_OUT.writeBytes("<head>");
				D_OUT.writeBytes("<title>");
				D_OUT.writeBytes("</title>");
				D_OUT.writeBytes("</head>");
				D_OUT.writeBytes("<body leftmargin=0 topmargin=0>");
				D_OUT.writeBytes("<p align=center>&nbsp;</p>");
				D_OUT.writeBytes("<p align=center>&nbsp;</p>");
				
				
				D_OUT.writeBytes("<table width=93%  height=1  border=2  cellspacing=0  bgcolor=#FFFF80>");
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes("<td width=229  height=1  bgcolor=#FFFFFF ><p align=center><strong><font color=#000000>Grade");
				D_OUT.writeBytes("</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>"); 
				D_OUT.writeBytes("<td width=229  height=1  bgcolor=#FFFFFF ><p align=center><strong><font color=#000000>Quantity,MT");
				D_OUT.writeBytes("</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>"); 
				D_OUT.writeBytes("<td width=229  height=1  bgcolor=#FFFFFF ><p align=center><strong><font color=#000000>Customer");
				D_OUT.writeBytes("</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>"); 
				D_OUT.writeBytes("<td width=229  height=1  bgcolor=#FFFFFF ><p align=center><strong><font color=#000000>Despatch on");
				D_OUT.writeBytes("</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>"); 
				D_OUT.writeBytes("  </small></strong></p>");
				D_OUT.writeBytes("</tr>");
				
				
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes("<td width=229  height=1  bgcolor=#FFFFFF ><p align=center><font color=#000000>&nbsp;"+strPRDCD);
				D_OUT.writeBytes("</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>"); 
				D_OUT.writeBytes("<td width=240  height=1  bgcolor=#FFFFFF><p align=center><font color=#000000>&nbsp;"+strREQQT);
				D_OUT.writeBytes("</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>"); 
				D_OUT.writeBytes("<td width=586  height=1  bgcolor=#FFFFFF><p align=center><font color=#000000>"+strPRTNM);
				D_OUT.writeBytes("</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>"); 
				D_OUT.writeBytes("<td width=243  height=1  bgcolor=#FFFFFF><p align=center><font color=#000000>"+strREQDT);
				D_OUT.writeBytes("</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("</tr>");
				
				
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes("<td width=100%  height=1  bgcolor=#FFFFFF colspan=4><p align=left><font color=#000000>&nbsp;</font><big><font face=Arial>Remark</font></big></strong></p>");
				D_OUT.writeBytes("<p align=left><strong><small><font face=Arial>"+strREMDS);

				D_OUT.writeBytes("<br>");
				D_OUT.writeBytes("</p></font></small></strong><p align=left>&nbsp;</p></td>");
				D_OUT.writeBytes("</tr>");
				
				D_OUT.writeBytes("</table>");
				
				D_OUT.writeBytes("<p>&nbsp;</p>");
				D_OUT.writeBytes("<p><small>Stock Detail As on ("+P_strPRDT+")</small></p>");
				D_OUT.writeBytes("<p>&nbsp;</p>");
				
				D_OUT.writeBytes("<table width=93%  height=1  border=2  cellspacing=0  bgcolor=#000000>");
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes("<td width=135  height=1  bgcolor=#FFFFFF><p align=center><strong><font color=#000000>Grade</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=135  height=1  bgcolor=#FFFFFF><p align=center><strong><font color=#000000>Stock qty.</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=135  height=1  bgcolor=#FFFFFF><p align=center><strong><font color=#000000>Pending Order qty.</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td bgcolor=#FFFFFF width=147><p align=center><strong><font color=#000000>Qty.on hold</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td bgcolor=#FFFFFF width=165><p align=center><strong><font color=#000000>Sales qty.</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=181  height=1  bgcolor=#FFFFFF><p align=center><strong><font color=#000000>Unclassified Qty.</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=145  height=1  bgcolor=#FFFFFF ><p align=center><strong><font color=#000000>Target Qty.</font><small><font face=Arial></font></small></strong><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("</tr>");
				
				D_OUT.writeBytes("<tr>");
				D_OUT.writeBytes("<td width=135  height=1  bgcolor=#FFFFFF><p align=center><font color=#000000>"+strPRDCD+"</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=135  height=1  bgcolor=#FFFFFF><p align=center><font color=#000000>"+strSTKQT+"</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=135  height=1  bgcolor=#FFFFFF><p align=center><font color=#000000>"+strPORQT+"</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td bgcolor=#FFFFFF width=147><p align=center><font color=#000000>"+strQLHQT+"</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td bgcolor=#FFFFFF width=165><p align=center><font color=#000000>"+strRESQT+"</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=181  height=1  bgcolor=#FFFFFF><p align=center><font color=#000000>"+strUCLQT+"</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("<td width=145  height=1  bgcolor=#FFFFFF ><p align=center><font color=#000000>"+strTARQT+"</font><small><font face=Arial></font></small><p align=center>&nbsp;</p></td>");
				D_OUT.writeBytes("</tr>");
				D_OUT.writeBytes("</table>");
				D_OUT.writeBytes("<p>&nbsp;</p>");
			}
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
					D_OUT.writeBytes("<P CLASS = \"breakhere\">");	
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
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
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
				D_OUT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size : 12 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			cl_dat.M_PAGENO +=1;
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		if(M_rdbHTML.isSelected())
			{
    		D_OUT.writeBytes("<html>");
    		D_OUT.writeBytes("<head>");
    		D_OUT.writeBytes("<title></title>");
    		D_OUT.writeBytes("</head>");
    		D_OUT.writeBytes("<body>");
			}
    		    
    		if(M_rdbHTML.isSelected())
			{
    	
    		D_OUT.writeBytes("<p align=center><font color=#000000><big>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    		D_OUT.writeBytes("<big><big>");
			}
    		
    		D_OUT.writeBytes("PRODUCTION REQUEST");
    		if(M_rdbHTML.isSelected())
			{
    		D_OUT.writeBytes("&nbsp;&nbsp;</big>&nbsp;&nbsp;</big>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    		D_OUT.writeBytes("</big></font></p>");
    		D_OUT.writeBytes("<p align=left><font color=#000000><big>&nbsp;&nbsp; </big></font></p>");
    		D_OUT.writeBytes("<p align=left><font color=#000000><big>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </big></font></p>");
    		D_OUT.writeBytes("</body>");
    		D_OUT.writeBytes("</html>");
			}
    		
    		if(M_rdbHTML.isSelected())
			{
    		D_OUT.writeBytes(strDOTLN+"\n");
    		D_OUT.writeBytes("\n");
    		D_OUT.writeBytes(padSTRING('R',"PR No. "+strDOCNO,strDOTLN.length()-74));
		    D_OUT.writeBytes("PR Date: "+strDOCDT+"\n");
		    crtNWLIN();
		    D_OUT.writeBytes(strDOTLN+"\n");
			}
		    prnFMTCHR(D_OUT,M_strNOBOLD);
		    crtNWLIN();
		    D_OUT.writeBytes("To : Shri K.V.Mujumdar CE(Operations) \n");
		    D_OUT.writeBytes("\n");
		    D_OUT.writeBytes(""+padSTRING('C',"Through : Shri N.Gopal ",strDOTLN.length()-45));
		    if(M_rdbHTML.isSelected())
			D_OUT.writeBytes("</b>");
		    D_OUT.writeBytes("\n");
		    D_OUT.writeBytes("\n");
		    D_OUT.writeBytes("\n");
		    D_OUT.writeBytes(""+padSTRING('R',"Kindly arrange to produce as detailed below :",strDOTLN.length()-18));
   			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
					
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes(""+padSTRING('R',"Kindly let us know schedule for production for reverting to customers. ",strDOTLN.length()-18));
			D_OUT.writeBytes("\n");
			crtNWLIN();
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(""+padSTRING('R',"Best Regards,",strDOTLN.length()-18));
			crtNWLIN();
			crtNWLIN();
			crtNWLIN();
			if(M_rdbHTML.isSelected())
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes(padSTRING('R',strEMPNM,strDOTLN.length()-18));
			if(M_rdbHTML.isSelected())
			D_OUT.writeBytes("</b>");
			crtNWLIN();
			if(M_rdbHTML.isSelected())
			{
			D_OUT.writeBytes("<html>");
			D_OUT.writeBytes("<head>");
			D_OUT.writeBytes("<title></title>");
			D_OUT.writeBytes("</head>");
			D_OUT.writeBytes("<body>");
			D_OUT.writeBytes("<p align=left><font color=#000000><small>");
			}
			D_OUT.writeBytes("(Electronically generated hence no signature)");
			if(M_rdbHTML.isSelected())
			{
			D_OUT.writeBytes("</small></font></p>");
			D_OUT.writeBytes("<p align=left><font color=#000000><big>&nbsp;&nbsp; </big></font></p>");
			D_OUT.writeBytes("<p align=left><font color=#000000><big>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </big></font></p>");
			D_OUT.writeBytes("</body>");
			D_OUT.writeBytes("</html>");
			}
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			
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
			
			if(strDOCNO.length()==0)
			{
			  setMSG("Pls Enter PR No. use F1 for selection of PR No.",'E');
			}
			
			if(strDOCNO.length()>0)
			{		
				M_strSQLQRY=" SELECT RQ_DOCNO,RQ_DOCDT from PR_RQTRN where isnull(RQ_STSFL,'')<>'XX' ";	
				//M_strSQLQRY += " AND RQ_PRDTP ='"+M_strSBSCD.substring(2,4)+"'";
				M_strSQLQRY += "and RQ_DOCNO = '"+strDOCNO+"'";
				//System.out.println("verify>>"+M_strSQLQRY);
			    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
			       txtDOCNO.setText(M_rstRSSET.getString("RQ_DOCNO"));
				   setMSG("",'N');
				}
				else
				{
					setMSG("Enter Valid Production Request Number",'E');
					return false;
				}
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
		strDOCNO=txtDOCNO.getText().trim();
	    strDOCDT=txtDOCDT.getText().trim();
		if(!vldDATA())
			return;
		try
		{
			exeDSPRP();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	void exeDSPRP()
	{
		try
		{
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			//String strDOCNO=txtDOCNO.getText().trim();
			//String strDOCDT=txtDOCDT.getText().trim();
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "pr_rpprq.html";
			//if(M_rdbTEXT.isSelected())
			   //  strRPFNM = strRPLOC + "pr_rpprq.doc";
			
			genRPTFL(strDOCNO,strDOCDT);
		    Runtime r = Runtime.getRuntime();
				Process p = null;
			    if(M_rdbHTML.isSelected())
			        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			   // else
			     //   p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 	
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeDSPRP");
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
					if(input == txtDOCNO)
					{		
						M_strSQLQRY=" SELECT RQ_DOCNO,RQ_DOCDT from PR_RQTRN where isnull(RQ_STSFL,'')<>'XX' ";	
						M_strSQLQRY += " AND RQ_PRDTP ='"+M_strSBSCD.substring(2,4)+"'";
						M_strSQLQRY += "and RQ_DOCNO = '"+txtDOCNO.getText()+"'";
						//System.out.println("verify>>"+M_strSQLQRY);
					    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
					        txtDOCNO.setText(M_rstRSSET.getString("RQ_DOCNO"));
					        txtDOCDT.setText((M_fmtLCDAT.format(M_rstRSSET.getDate("RQ_DOCDT"))));
							setMSG("",'N');
						}
						else
						{
							setMSG("Enter Valid Production Request Number",'E');
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



