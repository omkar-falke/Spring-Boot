import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.ResultSet;import java.text.SimpleDateFormat;
import java.util.Calendar;import javax.swing.border.*;import javax.swing.JPanel;
import java.sql.*;
/*

 */

class pr_rplbd extends cl_rbase 
{

	private JTextField txtPRDCD;			
	private JTextField txtSTRDT;			
	private JTextField txtENDDT;
	private JTextField txtTRNST;
	private JLabel lblPRDDS;
	private JLabel lblYOPST;
	private JLabel lblTRNST;
	private SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("dd/MM/yyyy"); 	
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"pr_rplbd.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private JPanel pnlMAIN;    		
	
	private JRadioButton  rdbSUMRY;
	private JRadioButton  rdbDTAIL;
	private ButtonGroup   btgSUDTL;
	
	private CallableStatement cstWKLBD;
	private String strCMPCD="01";
	
	private float fltTRNST=0;
	private String strTRNST="";
	private String strYOPST="";
	private String strSQLQRY1;
	ResultSet rstRSSET1;

	pr_rplbd()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,20);			
			pnlMAIN = new JPanel(null);

			add(new JLabel("    From Date"),1,1,1,2,pnlMAIN,'L');
			add(txtSTRDT = new TxtDate(),1,3,1,2,pnlMAIN,'L');
						
			add(new JLabel("    To Date"),2,1,1,2,pnlMAIN,'L');
			add(txtENDDT = new TxtDate(),2,3,1,2,pnlMAIN,'L');
			
			
			add(new JLabel("    Grade"),3,1,1,2,pnlMAIN,'L');
			add(txtPRDCD= new TxtLimit(10),3,3,1,2,pnlMAIN,'L');
			add(lblPRDDS= new JLabel(),3,5,1,5,pnlMAIN,'L');
			add(lblYOPST= new JLabel(),3,6,1,5,pnlMAIN,'L');
			
			add(lblTRNST=new JLabel("    Transaction Opening Stock"),4,1,1,4,pnlMAIN,'L');
			add(txtTRNST = new TxtNumLimit(10.3),4,5,1,2,pnlMAIN,'L');
						
			pnlMAIN.setBorder(new EtchedBorder(Color.black,Color.lightGray));
			add(pnlMAIN,5,7,5,9,this,'L');
			
			add(rdbSUMRY=new JRadioButton("Summary"),1,7,1,2.5,pnlMAIN,'L');
			add(rdbDTAIL=new JRadioButton("Detail"),2,7,1,2,pnlMAIN,'L');
			btgSUDTL=new ButtonGroup();
			btgSUDTL.add(rdbSUMRY); 
			btgSUDTL.add(rdbDTAIL);           
			rdbSUMRY.setSelected(true);
			
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtPRDCD.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);	
			
			cstWKLBD = cl_dat.M_conSPDBA_pbst.prepareCall("{call crtPR_WKLBD(?,?,?,?)}");
			
			txtTRNST.setVisible(false);
		    lblTRNST.setVisible(false);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(txtPRDCD.getText().length()==0)
				lblPRDDS.setText("");
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && txtSTRDT.getText().length()==0)
			{
				M_calLOCAL.setTime(M_fmtLCDAT.parse("01/"+cl_dat.M_strLOGDT_pbst.substring(3)));
				M_calLOCAL.add(Calendar.DATE,-1);
				txtENDDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				txtSTRDT.setText("01/"+txtENDDT.getText().substring(3));
			}
			if(rdbDTAIL.isSelected())
			{
				txtTRNST.setVisible(true);
			    lblTRNST.setVisible(true);
			}
			else
			{
				txtTRNST.setVisible(false);
			    lblTRNST.setVisible(false);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is actionPerformed()");
		}		
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        	{						
				if(M_objSOURC == txtPRDCD)
				{
					//M_strSQLQRY = " select pr_prdcd,pr_prdds from co_prmst where pr_prdcd in (select lt_prdcd from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_lotno in (select rct_lotno from fg_rctrn where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp in ('10','15') and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' )) order by pr_prdcd";
                    M_strSQLQRY = "select distinct pr_prdcd,pr_prdds from co_prmst,pr_ltmst,fg_rctrn  where pr_prdcd=ltrim(str(lt_prdcd,20,0)) and lt_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_cmpcd=rct_cmpcd and lt_prdtp=rct_prdtp and lt_lotno=rct_lotno and lt_rclno = rct_rclno and  rct_rcttp in ('10','15') and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' order by pr_prdcd";
					//System.out.println(M_strSQLQRY);
					M_strHLPFLD = "txtPRDCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Grade Code","Description"},2,"CT");        
				}
			}	
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
        	{
				if(M_objSOURC == txtSTRDT)
					txtENDDT.requestFocus();
				if(M_objSOURC == txtENDDT)
					txtPRDCD.requestFocus();
				if(M_objSOURC == txtPRDCD)
					txtTRNST.requestFocus();
				if(M_objSOURC == txtTRNST)
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
			cl_dat.M_flgHELPFL_pbst = false;		
			if(M_strHLPFLD == "txtPRDCD")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtPRDCD.setText(L_STRTKN.nextToken());
				lblPRDDS.setText(L_STRTKN.nextToken());
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
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			float fltTOTBG=0,fltTOTDS=0;
			int intNOFBG=0,intNOFDS=0;
			M_strSQLQRY =" Select wk_lotno,isnull(wk_pbgqt,0) wk_pbgqt,isnull(wk_pdsqt,0) wk_pdsqt,isnull(wk_obgqt,0) wk_obgqt,isnull(wk_odsqt,0) wk_odsqt,isnull(wk_stkqt,0) wk_stkqt";
			M_strSQLQRY+=" from pr_wklbd";
			M_strSQLQRY+=" order by wk_lotno";			
			System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					System.out.println(">>>>>>>>>>>>"+M_rstRSSET.getString("wk_lotno"));
					//System.out.println(">>>>>>>>>>>>"+M_rstRSSET.getFloat("wk_pbgqt"));
					//System.out.println(">>>>>>>>>>>>"+M_rstRSSET.getFloat("wk_pdsqt"));
					if(M_rstRSSET.getFloat("wk_pbgqt")>0)
					{	
						fltTOTBG+=M_rstRSSET.getFloat("wk_pbgqt");
						intNOFBG++;
					}	
					if(M_rstRSSET.getFloat("wk_pdsqt")>0)
					{	
						fltTOTDS+=M_rstRSSET.getFloat("wk_pdsqt");
						intNOFDS++;
					}	
					
					D_OUT.writeBytes(padSTRING('R',chkZERO(M_rstRSSET.getString("wk_lotno")),15));			
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("wk_pbgqt")),16));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("wk_pdsqt")),20));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("wk_obgqt")),16));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("wk_odsqt")),18));
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("wk_stkqt")),15));
					crtNWLIN();
				}	
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Total",15));			
				D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltTOTBG,3),16));
				D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltTOTDS,3),20));				
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"No Of Lots",15));			
				D_OUT.writeBytes(padSTRING('L',""+intNOFBG,16));
				D_OUT.writeBytes(padSTRING('L',""+intNOFDS,20));
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
	String prnSTR(float P_fltVAL)
	{
		if(P_fltVAL == 0)
			return "-";
		else
			return String.valueOf(P_fltVAL).toString();
	}
	void genRPTFL1()
	{
		try
		{
			String strSQLQRY2;
			ResultSet rstRSSET2;
			
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			float fltTORCT=0,fltTOISS=0;
			int intNOFRCT=0,intNOFISS=0;
			float fltTORECPT=0,fltTOISSUE=0;
			float fltRCTDT=0;
			float fltISSDT=0;
			float fltTOSTQ=0;
			
			float fltRECPT=0;
			float fltISSUE=0;
			float fltTRNST=0;
			
			String strNEW_DOCDT="";
			String strOLD_DOCDT="";
			String strNEW_DOCDT1="";
			String strOLD_DOCDT1="";
			
			strSQLQRY2 =" Select wk_lotno,wk_docdt,isnull(wk_rctqt,0) wk_rctqt,isnull(wk_issqt,0) wk_issqt";
			strSQLQRY2+=" from pr_wklbd1";
			strSQLQRY2+=" order by wk_docdt,wk_lotno";
			/*strSQLQRY2 =" Select wk_lotno,date(wk_docdt)wk_docdt,isnull(wk_rctqt,0) wk_rctqt,isnull(wk_issqt,0) wk_issqt,sum(st_dosqt+st_douqt) st_curstq";
			strSQLQRY2+=" from pr_wklbd1 left outer join fg_stmst on wk_prdcd=st_prdcd and wk_cmpcd=st_cmpcd";
			strSQLQRY2+=" order by wk_docdt,wk_lotno";*/
			//System.out.println(strSQLQRY2);
			rstRSSET2=cl_dat.exeSQLQRY2(strSQLQRY2);
			fltTRNST=Float.parseFloat(strTRNST);
			if(rstRSSET2 != null)
			{
				while(rstRSSET2.next())
				{
					
					if(rstRSSET2.getFloat("wk_rctqt")>0)
					{	
						fltTORCT+=rstRSSET2.getFloat("wk_rctqt");
					}	
					if(rstRSSET2.getFloat("wk_issqt")>0)
					{	
						fltTOISS+=rstRSSET2.getFloat("wk_issqt");
					}
					fltRECPT=rstRSSET2.getFloat("wk_rctqt");
					fltISSUE=rstRSSET2.getFloat("wk_issqt");
					///fltTRNST=Float.parseFloat(strTRNST);
					
					fltTRNST+=fltRECPT-fltISSUE;
					
					strNEW_DOCDT=M_fmtLCDAT.format(rstRSSET2.getDate("wk_docdt"));
					if(!strOLD_DOCDT.equals(strNEW_DOCDT) && !strOLD_DOCDT.equals(""))
					{
						crtNWLIN();
						
						if(M_rdbHTML.isSelected())
							D_OUT.writeBytes("<b>");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(D_OUT,M_strBOLD);
						D_OUT.writeBytes(padSTRING('L',"",19));
						D_OUT.writeBytes(padSTRING('R',"Total",9));			
						D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltRCTDT,3),23));
						D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltISSDT,3),18));
						//D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltTOSTQ,3),28));
					
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(D_OUT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							D_OUT.writeBytes("</b>");
											
						fltISSDT=0;
						fltRCTDT=0;
						//fltTOSTQ=0;
					}
					strOLD_DOCDT=M_fmtLCDAT.format(rstRSSET2.getDate("wk_docdt"));
					crtNWLIN();
					
					strNEW_DOCDT1=rstRSSET2.getString("wk_docdt");
					if(!strOLD_DOCDT1.equals(strNEW_DOCDT1)) 
					{
						D_OUT.writeBytes(padSTRING('R',chkZERO(rstRSSET2.getString("wk_docdt")),19));
						strOLD_DOCDT1=rstRSSET2.getString("wk_docdt");
					} 
					else
						D_OUT.writeBytes(padSTRING('R'," ",19));
					
					D_OUT.writeBytes(padSTRING('R',chkZERO(rstRSSET2.getString("wk_lotno")),9));
					D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET2.getString("wk_rctqt")),23));
					D_OUT.writeBytes(padSTRING('L',chkZERO(rstRSSET2.getString("wk_issqt")),17));
					D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltTRNST,3),29));
					
					fltRCTDT+=rstRSSET2.getFloat("wk_rctqt");
					fltISSDT+=rstRSSET2.getFloat("wk_issqt");
					
					//fltTRNST+=fltRECPT-fltISSUE;
					//fltTOSTQ+=fltTRNST;	
				}
				crtNWLIN();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					prnFMTCHR(D_OUT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("<b>");
				D_OUT.writeBytes(padSTRING('L',"",19));
				D_OUT.writeBytes(padSTRING('R',"Total",9));			
				D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltRCTDT,3),23));
				D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltISSDT,3),18));
			//	D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltTOSTQ,3),28));
			
				crtNWLIN();
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
					crtNWLIN();
					D_OUT.writeBytes(padSTRING('R',"Total",34));			
					D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltTORCT,3),17));
					D_OUT.writeBytes(padSTRING('L',""+setNumberFormat(fltTOISS,3),18));		
					
					strSQLQRY2=" Select sum(st_dosqt+st_douqt) st_curstq from fg_stmst";
					strSQLQRY2+=" where st_prdcd='"+txtPRDCD.getText().toString()+"' and st_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
					//System.out.println(strSQLQRY2);
	    			rstRSSET2=cl_dat.exeSQLQRY2(strSQLQRY2);
	    			if(rstRSSET2 != null)
	    			{
	    				while(rstRSSET2.next())
	    				{
	    					D_OUT.writeBytes(padSTRING('R',"",2));
	    					D_OUT.writeBytes(padSTRING('R',"Current Stock :",19));
	 					   	D_OUT.writeBytes(padSTRING('R',chkZERO(rstRSSET2.getString("st_curstq")),9));
	 					  
	 					   	if(!(""+fltTOSTQ).equals(rstRSSET2.getString("st_curstq")))
	 							D_OUT.writeBytes(padSTRING('L',"*",1));
	 						
	 						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		 							prnFMTCHR(D_OUT,M_strNOBOLD);
		 					if(M_rdbHTML.isSelected())
		 							D_OUT.writeBytes("</b>");
	    				}
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
			setMSG(L_EX,"Report1");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

	private String chkZERO(String LP_STR)
	{
		try
		{
			if(LP_STR.equals("0.000"))
				return "-";
			else
				return LP_STR;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkZERO()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return "";
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
				//D_OUT.writeBytes("<HTML><HEAD><Title>Lotwise Bagging Despatch Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<HTML><HEAD></HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");   
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
    		
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		
    		
			// get Locking date from CO_CDTRN
    			String L_strSQLQRY =" Select CMT_CCSVL from CO_CDTRN where";
    			L_strSQLQRY+=" CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXREF' and CMT_CODCD='DOCDT' ";
    			ResultSet L_rstRSSET=cl_dat.exeSQLQRY1(L_strSQLQRY);
    			//System.out.println(">>>>chkLOCKINGDATE>>>>"+M_strSQLQRY);
    			java.util.Date datLOCDT=null;
    			if(L_rstRSSET.next() && L_rstRSSET !=null)
    				datLOCDT=M_fmtLCDAT.parse(L_rstRSSET.getString("CMT_CCSVL"));
    			else
    				setMSG("LOCKING date does not exist ",'E');			

    			M_calLOCAL.setTime(datLOCDT);      
    			M_calLOCAL.add(Calendar.DATE,+1);    
    			datLOCDT = M_calLOCAL.getTime();
    			//lblDSPDT.setText("As On "+fmtYYYYMMDD.format(datLOCDT)+" 7:00 hrs");
    		
    		if(rdbSUMRY.isSelected()) 
        	{
    			D_OUT.writeBytes(padSTRING('R',"Lotwise Bagging Despatch Summary  ",50));
        		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
    			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    			crtNWLIN();
    			D_OUT.writeBytes(padSTRING('R',"Grade : "+lblPRDDS.getText()+"         Period : From  "+txtSTRDT.getText()+"  To  "+txtENDDT.getText(),100));

    			crtNWLIN();
    			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
    			crtNWLIN();
    			D_OUT.writeBytes("Lot No.                 Bagging            Despatch           Total             Total    Stock As On");
    			crtNWLIN();
    			D_OUT.writeBytes("                     During the          During The         Bagging          Despatch    "+fmtYYYYMMDD.format(datLOCDT));
    			crtNWLIN();
    			D_OUT.writeBytes("                         Period              Period                                         7:00 hrs");
    			crtNWLIN();
    			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
    			crtNWLIN();
			
    		}
    		if(rdbDTAIL.isSelected()) 
    		{
	    		
    			D_OUT.writeBytes(padSTRING('R'," Datewise,Lotwise Stock ledger",50));
        		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
    			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    			crtNWLIN();
    			D_OUT.writeBytes(padSTRING('R',"Grade : "+lblPRDDS.getText()+"         Period : From  "+txtSTRDT.getText()+"  To  "+txtENDDT.getText(),100));

    			
				crtNWLIN();
	    		D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
				crtNWLIN();
				D_OUT.writeBytes("Date               Lot No.                  Receipt             Issue                    Stock As On");
				crtNWLIN();
    			D_OUT.writeBytes("                                                                                      "+fmtYYYYMMDD.format(datLOCDT));
				crtNWLIN();
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------");
				crtNWLIN();
				 D_OUT.writeBytes(padSTRING('L',"",70));
                D_OUT.writeBytes(padSTRING('R',"Year Opening Stock : ",21));
                D_OUT.writeBytes(padSTRING('R',chkZERO(strYOPST),35));
                crtNWLIN();
    			strTRNST=txtTRNST.getText().toString();
    			
    			 D_OUT.writeBytes(padSTRING('L',"",63));
    			 D_OUT.writeBytes(padSTRING('R',"Transcation Opening Stock : ",28));
    			 D_OUT.writeBytes(padSTRING('R',strTRNST,21)+"\n");
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
			setCursor(cl_dat.M_curDFSTS_pbst);
			
		}
	}

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
			setMSG(L_IOE,"Report Footer");
		}
	}	

	boolean vldDATA()
	{
		try
		{
			if(txtPRDCD.getText().length()==0)
			{
				txtPRDCD.requestFocus();
				setMSG("Please Enter Grade Code",'E');
				return false;
			}	
			if(txtSTRDT.getText().length()==0)
			{
				txtSTRDT.requestFocus();
				setMSG("Please Enter From Date",'E');
				return false;
			}	
			if(txtENDDT.getText().length()==0)
			{
				txtENDDT.requestFocus();
				setMSG("Please Enter To Date",'E');
				return false;
			}	
			if(txtTRNST.getText().length()==0 && rdbDTAIL.isSelected())
			{
				txtTRNST.requestFocus();
				setMSG("Please Enter Transaction Opening Stock",'E');
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
			cl_dat.M_flgLCUPD_pbst = true;
			if(rdbSUMRY.isSelected()) 
			M_strSQLQRY = "delete from pr_wklbd";
			if(rdbDTAIL.isSelected())  
			M_strSQLQRY = "delete from pr_wklbd1";
			//System.out.println(">>>M_strSQLQRY>>>"+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			if(cl_dat.exeDBCMT("exeUPDTBLS"))
				setMSG("Generating Report ... ",'N');
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "pr_rplbd.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "pr_rplbd.doc";
			if(rdbSUMRY.isSelected()) 
			{
				exeUPD_WRKTBL();
	    		genRPTFL();
			}
			if(rdbDTAIL.isSelected()) 
			{
				cstWKLBD.setString(1,strCMPCD);
				cstWKLBD.setString(2,txtPRDCD.getText().trim());
				cstWKLBD.setString(3,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().trim())));
				cstWKLBD.setString(4,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().trim())));
				//System.out.println("BEFORE update");
				cstWKLBD.executeUpdate();
				//System.out.println("after update");
				cl_dat.exeDBCMT("after procedure");
				//System.out.println("procedure call complete");
				
				genRPTFL1();
			}
			
			
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
	
	
	public void exeUPD_WRKTBL()
	{
		try
		{
			//Bagging for the period 
			setMSG("Fetching Bagging For The Period..",'N');
			M_strSQLQRY =" ";
			M_strSQLQRY="select rct_lotno rpt_lotno,sum(rct_rctqt) rpt_lotqt from fg_rctrn,pr_ltmst where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and rct_cmpcd=lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno=lt_lotno and rct_rclno=lt_rclno and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_prdcd = '"+txtPRDCD.getText()+"'  and rct_rcttp in ('10','15') group by rct_lotno";
			//M_strSQLQRY="select rct_lotno rpt_lotno,sum(rct_rctqt) rpt_lotqt from fg_rctrn where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and rct_cmpcd||rct_prdtp||rct_lotno||rct_rclno  in (select lt_cmpcd||lt_prdtp||lt_lotno||lt_rclno  from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_prdcd = '"+txtPRDCD.getText()+"')  and rct_rcttp in ('10','15') group by rct_lotno";
			System.out.println(">>>> Bagging for the period >>>>"+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			putPRWKLBD(M_rstRSSET,"WK_PBGQT");
		
			//Despatch For the period 
			setMSG("Fetching Despatches For the period..",'N');
			M_strSQLQRY =" ";
			M_strSQLQRY="select ist_lotno rpt_lotno,sum(ist_issqt) rpt_lotqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_issdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and ist_prdcd = '"+txtPRDCD.getText()+"'  and ist_isstp in ('10') group by ist_lotno";
			//M_strSQLQRY="select ist_lotno rpt_lotno,sum(ist_issqt) rpt_lotqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_issdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and ist_prdcd = '"+txtPRDCD.getText()+"'  and ist_isstp in ('10') group by ist_lotno";
			System.out.println(">>>> Despatch For the period >>>>"+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			putPRWKLBD(M_rstRSSET,"WK_PDSQT");
			
			// Total Bagging
			setMSG("Fetching Total Bagging For The Period..",'N');
			M_strSQLQRY =" ";
			M_strSQLQRY="select rct_lotno rpt_lotno ,sum(rct_rctqt) rpt_lotqt from fg_rctrn,pr_ltmst where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_cmpcd=lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno=lt_lotno and rct_rclno=lt_rclno  and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_prdcd = '"+txtPRDCD.getText()+"'  and   (rct_prdtp + rct_lotno + rct_rclno  in (select b.rct_prdtp + b.rct_lotno + b.rct_rclno from fg_rctrn b where  b.RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND b.rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'  and b.rct_rcttp in ('10','15')) or   rct_prdtp + rct_lotno + rct_rclno in (select c.ist_prdtp + c.ist_lotno + c.ist_rclno from fg_istrn c where  c.IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND c.ist_issdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'  and c.ist_isstp in ('10','21') ))  and rct_rcttp in ('10','15') group by rct_lotno order by rct_lotno";
			//M_strSQLQRY="select rct_lotno rpt_lotno ,sum(rct_rctqt) rpt_lotqt from fg_rctrn where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_cmpcd||rct_prdtp||rct_lotno||rct_rclno  in (select lt_cmpcd||lt_prdtp||lt_lotno||lt_rclno  from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_prdcd = '"+txtPRDCD.getText()+"')  and   (rct_prdtp||rct_lotno||rct_rclno  in (select b.rct_prdtp||b.rct_lotno||b.rct_rclno from fg_rctrn b where  b.RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND b.rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'  and b.rct_rcttp in ('10','15')) or   rct_prdtp||rct_lotno||rct_rclno in (select c.ist_prdtp||c.ist_lotno||c.ist_rclno from fg_istrn c where  c.IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND c.ist_issdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'  and c.ist_isstp in ('10','21') ))  and rct_rcttp in ('10','15') group by rct_lotno order by rct_lotno";
			System.out.println(">>>> Total Bagging >>>>"+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			putPRWKLBD(M_rstRSSET,"WK_OBGQT");
			
			// Total Despatches
			setMSG("Fetching Total Despatches For The Period..",'N');
			M_strSQLQRY =" ";
			M_strSQLQRY="select ist_lotno  rpt_lotno,sum(ist_issqt)  rpt_lotqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_prdcd = '"+txtPRDCD.getText()+"'   and  (ist_cmpcd + ist_prdtp + ist_lotno + ist_rclno  in (select b.rct_cmpcd + b.rct_prdtp + b.rct_lotno + b.rct_rclno from fg_rctrn b where b.RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND b.rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and b.rct_rcttp in ('10','15')) or   ist_prdtp + ist_lotno + ist_rclno in (select c.ist_prdtp + c.ist_lotno + c.ist_rclno from fg_istrn c where c.IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  c.ist_issdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'  and c.ist_isstp in ('10','21') ))  and ist_isstp in ('10','21') group by ist_lotno order by ist_lotno";
			System.out.println(">>>> Total Despatches >>>>"+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			putPRWKLBD(M_rstRSSET,"WK_ODSQT");
			
			// Stock
			setMSG("Stock..",'N');
			M_strSQLQRY =" ";
			M_strSQLQRY="select st_lotno rpt_lotno, sum(st_dosqt+st_douqt)  rpt_lotqt from fg_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  (st_cmpcd + st_prdtp + st_lotno + st_rclno  in (select b.rct_cmpcd + b.rct_prdtp + b.rct_lotno + b.rct_rclno from fg_rctrn b where b.RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND b.rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'  and b.rct_rcttp in ('10','15')) or   st_prdtp + st_lotno + st_rclno in (select c.ist_prdtp + c.ist_lotno + c.ist_rclno from fg_istrn c where c.IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND c.ist_issdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and c.ist_isstp in ('10','21') ))  and st_prdcd = '"+txtPRDCD.getText()+"'  and st_dosqt+st_douqt>0 group by st_lotno order by st_lotno";
			//M_strSQLQRY="select st_lotno rpt_lotno, sum(st_dosqt+st_douqt)  rpt_lotqt from fg_stmst,fg_ltmst left outer join fg_rctrn on st_cmpcd=rct_cmpcd and st_prdtp=rct_prdtp and st_lotno=rct_lotno and st_rclno=rct_rclno and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND b.rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'  and b.rct_rcttp in ('10','15') where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'   and st_dosqt+st_douqt>0 group by st_lotno order by st_lotno";
			System.out.println(">>>> Stock >>>>"+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			putPRWKLBD(M_rstRSSET,"WK_STKQT");
			
			cl_dat.exeDBCMT("exeSAVE");
		}	
		catch(Exception L_E)
		{
			setMSG(L_E,"exeUPDTBLS()");
		}
	}		
	
	
	private void putPRWKLBD(ResultSet LP_RSSET, String LP_FLDNM)
	{
		try
		{
			String strSQLQRY1="";
			while(LP_RSSET.next() && LP_RSSET !=null)
			{
				if(getCOUNT(LP_RSSET.getString("RPT_LOTNO"))==0)
				{
					strSQLQRY1 =" Insert into PR_WKLBD(WK_LOTNO,"+LP_FLDNM+") values(";
					strSQLQRY1+="'"+LP_RSSET.getString("RPT_LOTNO")+"',"; 
					strSQLQRY1+="'"+LP_RSSET.getString("RPT_LOTQT")+"')";
					//System.out.println(">>>>insert>>>>"+strSQLQRY1);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");												
				}	
				else
				{
					strSQLQRY1 =" Update PR_WKLBD set "+LP_FLDNM+"=isnull("+LP_RSSET.getString("RPT_LOTQT")+",0) where WK_LOTNO='"+LP_RSSET.getString("RPT_LOTNO")+"'";	
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(strSQLQRY1,"setLCLUPD");								
					//System.out.println(">>>>update>>>>"+strSQLQRY1);
				}	
			}
	
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"putFGDSWRK");
		}
	}
	
	
	public int getCOUNT(String lp_lotno)
	{
		try
		{
			String strSQLQRY;
			ResultSet rstRSSET;
		
			strSQLQRY =" Select count(*) CNT";
			strSQLQRY+=" from PR_WKLBD";
			strSQLQRY+=" where  WK_LOTNO='"+lp_lotno+"'";
			//strSQLQRY+=" where WK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WK_LOTNO='"+lp_lotno+"'";
			//System.out.println(">>>>count>>>>"+strSQLQRY);
			rstRSSET=cl_dat.exeSQLQRY1(strSQLQRY);
			if(rstRSSET.next() && rstRSSET !=null)
			{
				return rstRSSET.getInt("CNT");
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getCOUNT()");
		}
		return -1;
	}
	
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
				return true;
		   
			if(input == txtPRDCD)
			{
				//M_strSQLQRY =" select pr_prdcd,pr_prdds from co_prmst where pr_prdcd = '"+txtPRDCD.getText()+"' and pr_prdcd in (select lt_prdcd from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_lotno in (select rct_lotno from fg_rctrn where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp in ('10','15') and rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' )) order by pr_prdcd";
                M_strSQLQRY = "select distinct pr_prdcd,pr_prdds from co_prmst  where pr_prdcd='"+txtPRDCD.getText()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null && M_rstRSSET.next())
				{
					lblPRDDS.setText(M_rstRSSET.getString("pr_prdds"));
					if(rdbDTAIL.isSelected())
					{
						strSQLQRY1 =" Select sum(op_yosqt) op_yosqt from fg_opstk where op_prdcd='"+txtPRDCD.getText().toString()+"' and op_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
						//System.out.println(strSQLQRY1);
						rstRSSET1=cl_dat.exeSQLQRY1(strSQLQRY1);
						if(rstRSSET1 != null)
						{
							while(rstRSSET1.next())
							{
								strYOPST=rstRSSET1.getString("op_yosqt");
							}
						}
						lblYOPST.setText("  Year Opening Stock : "+strYOPST);
						lblYOPST.setForeground(Color.blue);
					}
				}	
				else 
				{
					setMSG("No Data Found..",'E');
					return false;
				}	
			}
			if(input == txtSTRDT)
			{
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From date can not be greater than Today's date..",'E');
					return false;
				}
			}
			if(input == txtENDDT)
			{
				if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("TO date can not be greater than Today's date..",'E');
					return false;
				}
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
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



