import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;import java.util.Calendar;	
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.CallableStatement;import java.sql.ResultSet;
import java.util.Calendar;	


class fg_rpdcl extends cl_rbase 
{

	private JTextField txtDATE;
	private JTextField txtPRDCT;
	private JTextField txtPRDCT1;
	private JTextField txtQLTCT;

	private JLabel lblPRDDS;
	private JLabel lblPRDDS1;
	private JLabel lblQLTCT;
	
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"fg_rpdcl.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private String strTXCLR="<font Color=black>";/**Global string for page break in HTML	 */
	private String strPGBRK="<P CLASS = \"breakhere\"></PRE>";/** String array for report column headers */
	private boolean flgTBLDT;
	private float totPRMCLOQT=0,totPRMCLTQT=0,totPRMDDOQT=0,totPRMDEOQT=0,totPRMDCOQT=0,totPRMDDTQT=0,totPRMDETQT=0,totPRMDCTQT=0,totPRMDSRQT=0;
	private float totNPRMCLOQT=0,totNPRMCLTQT=0,totNPRMDDOQT=0,totNPRMDEOQT=0,totNPRMDCOQT=0,totNPRMDDTQT=0,totNPRMDETQT=0,totNPRMDCTQT=0,totNPRMDSRQT=0;
	private float totSUBCLOQT=0,totSUBCLTQT=0,totSUBDDOQT=0,totSUBDEOQT=0,totSUBDCOQT=0,totSUBDDTQT=0,totSUBDETQT=0,totSUBDCTQT=0,totSUBDSRQT=0;
	private float totCLOQT=0,totCLTQT=0,totDDOQT=0,totDEOQT=0,totDCOQT=0,totDDTQT=0,totDETQT=0,totDCTQT=0,totDSRQT=0;

	CallableStatement cstHKDCC;
	fg_rpdcl()		/*  Constructor   */
	{
		super(2);
		try
		{
			M_intPAGNO=0;
			setMatrix(20,20);
			
			add(new JLabel("Enter Date"),5,6,1,3,this,'L');
			add(txtDATE= new TxtDate(),5,9,1,2,this,'L');
			
			add(new JLabel("Main Product Category"),6,6,1,3,this,'L');
			add(txtPRDCT = new TxtLimit(10),6,9,1,2,this,'L');
			add(lblPRDDS = new JLabel(),6,11,1,2,this,'L');
			
			add(new JLabel("Sub Product Category"),7,6,1,3,this,'L');
			add(txtPRDCT1 = new TxtLimit(10),7,9,1,2,this,'L');
			add(lblPRDDS1 = new JLabel(),7,11,1,2,this,'L');
			
			add(new JLabel("Quality (0/1)"),8,6,1,3,this,'L');
			add(txtQLTCT = new TxtNumLimit(1),8,9,1,2,this,'L');
			add(lblQLTCT = new JLabel(),8,11,1,2,this,'L');
			
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtDATE.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);			
			M_rdbHTML.setSelected(true);
			txtPRDCT.setInputVerifier(oINPVF);
			txtPRDCT1.setInputVerifier(oINPVF);
			txtQLTCT.setInputVerifier(oINPVF);
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
			if(txtPRDCT.getText().length()==0)
				lblPRDDS.setText("");
			if(txtPRDCT1.getText().length()==0)
				lblPRDDS1.setText("");
			if(txtQLTCT.getText().length()==0)
				lblQLTCT.setText("");
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{	
				if(txtDATE.getText().length()<10)
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));      
					M_calLOCAL.add(Calendar.DATE,-1);    
					txtDATE.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				}
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
        		if(M_objSOURC==txtPRDCT)		
        		{
					M_strHLPFLD = "txtPRDCT";
					M_strSQLQRY = "Select SUBSTRING(cmt_codcd,1,2),cmt_shrds from CO_CDTRN where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPGR' and SUBSTRING(cmt_codcd,3,2)='00'";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Product Category","Product Desc."},2,"CT");        		
				}
				if(M_objSOURC==txtPRDCT1)		
        		{
					if(txtPRDCT.getText().length() > 0)
					{	
						M_strHLPFLD = "txtPRDCT1";
						M_strSQLQRY = "Select SUBSTRING(cmt_codcd,1,4),cmt_shrds from CO_CDTRN where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPGR' and SUBSTRING(cmt_codcd,3,2)<>'00' and SUBSTRING(cmt_codcd,1,2)='"+txtPRDCT.getText().substring(0,2)+"'";
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Product Category","Product Desc."},2,"CT");        		
					}
					else 
						setMSG("Please select Main Product Category",'E');
				}
				
			}	
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
        	{
				if(M_objSOURC==txtDATE)
					txtPRDCT.requestFocus();
				else if(M_objSOURC==txtPRDCT)
					txtPRDCT1.requestFocus();
				else if(M_objSOURC==txtPRDCT1)
					txtQLTCT.requestFocus();
				else if(M_objSOURC==txtQLTCT)
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
			
			if(M_strHLPFLD.equals("txtPRDCT"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
				txtPRDCT.setText(L_STRTKN.nextToken().substring(0,2));
				lblPRDDS.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtPRDCT1"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
				txtPRDCT1.setText(L_STRTKN.nextToken().substring(0,4));
				lblPRDDS1.setText(L_STRTKN.nextToken());
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
			genRPHDR();
			dspREPRT();
			crtNWLIN();
			//genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	private void dspREPRT() 
	{
		try
		{
			String strSQLQRY;
			ResultSet rstRSSET;
			String L_strNEWGRPCD="",L_strOLDGRPCD="",L_strNEWQLTCT="",L_strOLDQLTCT="",L_strSUBCATDS="",L_strCATDS="";
			String L_strNEWGRPCT="",L_strOLDGRPCT="";
			setMSG("Printing Report...",'N');
			cstHKDCC = cl_dat.M_conSPDBA_pbst.prepareCall("{ call crtFG_WKDCL(?,?)}");
			cstHKDCC.setString(1,cl_dat.M_strCMPCD_pbst);
			cstHKDCC.setString(2,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDATE.getText())));
			cstHKDCC.executeUpdate();

			M_strSQLQRY =" Select WK_PRDCT,WK_QLTCT,WK_PRDCD,WK_PRDDS,";
			M_strSQLQRY+=" isnull(WK_CLOQT,0) WK_CLOQT,isnull(WK_CLTQT,0) WK_CLTQT,isnull(WK_DDOQT,0) WK_DDOQT,isnull(WK_DEOQT,0) WK_DEOQT,isnull(WK_DCOQT,0) WK_DCOQT,isnull(WK_DDTQT,0) WK_DDTQT,isnull(WK_DDTQT,0) WK_DDTQT,isnull(WK_DETQT,0) WK_DETQT,isnull(WK_DCTQT,0) WK_DCTQT,isnull(WK_DSRQT,0) WK_DSRQT ";
			M_strSQLQRY+=" from fg_wkdcl ";
			if(txtPRDCT.getText().length()>0)
				M_strSQLQRY+=" where WK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(WK_PRDCT,1,2)='"+txtPRDCT.getText().substring(0,2)+"'";
			if(txtPRDCT1.getText().length()>0)
				M_strSQLQRY+=" and WK_PRDCT='"+txtPRDCT1.getText().substring(0,4)+"'";
			if(txtQLTCT.getText().length()>0)
			{	
				if(txtPRDCT.getText().length()>0)
					M_strSQLQRY+=" and WK_QLTCT='"+txtQLTCT.getText()+"'";	
				else
					M_strSQLQRY+=" where WK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WK_QLTCT='"+txtQLTCT.getText()+"'";
			}	
			M_strSQLQRY+=" order by wk_prdct,wk_qltct,wk_prdcd";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strNEWGRPCD=M_rstRSSET.getString("WK_PRDCT").substring(0,4);
					L_strNEWQLTCT=M_rstRSSET.getString("WK_QLTCT");
					L_strNEWGRPCT=M_rstRSSET.getString("WK_PRDCT").substring(0,2);

					//prints total. 	
					if(!L_strNEWGRPCD.equals(L_strOLDGRPCD) || !L_strNEWQLTCT.equals(L_strOLDQLTCT))
					{
						setTXCLR(D_OUT,Color.orange);
						if(!L_strOLDQLTCT.equals(""))
							if(Integer.parseInt(L_strOLDQLTCT) == 0)
								prnTOTAL(0);
							else 
								prnTOTAL(1); 		
					}
					
					setTXCLR(D_OUT,Color.magenta);
					if(!L_strNEWGRPCD.equals(L_strOLDGRPCD))
					{
						if(!L_strOLDGRPCD.equals(""))
							prnTOTAL1(L_strSUBCATDS);
					}	
					
					setTXCLR(D_OUT,Color.red);
					if(!L_strNEWGRPCT.equals(L_strOLDGRPCT))
					{
						if(!L_strOLDGRPCT.equals(""))
							prnTOTAL2(L_strCATDS);
					}	

					//displays product cat as polystyrene or sp compunds
					//if 1st 2 digits of product cat are not same
					if(!L_strNEWGRPCT.equals(L_strOLDGRPCT))
					{
						setTXCLR(D_OUT,Color.red);
						strSQLQRY =" select CMT_SHRDS from co_cdtrn where cmt_cgstp = 'COXXPGR' and cmt_codcd like '%"+L_strNEWGRPCT+"%' order by cmt_codcd";
						rstRSSET = cl_dat.exeSQLQRY1(strSQLQRY);
						if(rstRSSET.next() && rstRSSET!=null)
						{	
							D_OUT.writeBytes(padSTRING('R',rstRSSET.getString("CMT_SHRDS"),10));
							L_strCATDS=rstRSSET.getString("CMT_SHRDS");
						}	
						else
							setMSG("Error: no data found in COXXPGR",'E');
					}

					
					//displays sub product cat as GPPS or HIPS
					//if 1st 4 digits of product cat are not same
					if(!L_strNEWGRPCD.equals(L_strOLDGRPCD) || !L_strNEWQLTCT.equals(L_strOLDQLTCT))
					{
						setTXCLR(D_OUT,Color.magenta);
						if(L_strNEWGRPCT.equals(L_strOLDGRPCT))
							D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						L_strSUBCATDS=getCATDS(M_rstRSSET.getString("WK_PRDCT"));

						if(L_strSUBCATDS.length()>10)
							D_OUT.writeBytes(padSTRING('R',L_strSUBCATDS.substring(0,10),10));
						else
							D_OUT.writeBytes(padSTRING('R',L_strSUBCATDS,10));
						setTXCLR(D_OUT,Color.orange);
						//if(Integer.parseInt(M_rstRSSET.getString("WK_QLTCT"))==0)
						if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
							D_OUT.writeBytes(padSTRING('R',"PRIME",15));
						else
							D_OUT.writeBytes(padSTRING('R',"N.PRIME",15));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
						crtNWLIN();
					}

				
					L_strOLDGRPCT=M_rstRSSET.getString("WK_PRDCT").substring(0,2);
					L_strOLDGRPCD=M_rstRSSET.getString("WK_PRDCT").substring(0,4);
					L_strOLDQLTCT=M_rstRSSET.getString("WK_QLTCT");
					
					setTXCLR(D_OUT,Color.green);
					D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
					D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
					if(M_rstRSSET.getString("WK_PRDDS").length()>12)
						D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("WK_PRDDS").substring(0,12),15));
					else D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("WK_PRDDS"),15)); 
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_CLOQT")),10));
					
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMCLOQT+=Float.parseFloat(M_rstRSSET.getString("WK_CLOQT"));
					else
						totNPRMCLOQT+=Float.parseFloat(M_rstRSSET.getString("WK_CLOQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_CLTQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMCLTQT+=Float.parseFloat(M_rstRSSET.getString("WK_CLTQT"));
					else
						totNPRMCLTQT+=Float.parseFloat(M_rstRSSET.getString("WK_CLTQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_DDOQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMDDOQT+=Float.parseFloat(M_rstRSSET.getString("WK_DDOQT"));
					else
						totNPRMDDOQT+=Float.parseFloat(M_rstRSSET.getString("WK_DDOQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_DEOQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMDEOQT+=Float.parseFloat(M_rstRSSET.getString("WK_DEOQT"));
					else
						totNPRMDEOQT+=Float.parseFloat(M_rstRSSET.getString("WK_DEOQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_DCOQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMDCOQT+=Float.parseFloat(M_rstRSSET.getString("WK_DCOQT"));
					else
						totNPRMDCOQT+=Float.parseFloat(M_rstRSSET.getString("WK_DCOQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_DDTQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMDDTQT+=Float.parseFloat(M_rstRSSET.getString("WK_DDTQT"));
					else
						totNPRMDDTQT+=Float.parseFloat(M_rstRSSET.getString("WK_DDTQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_DETQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMDETQT+=Float.parseFloat(M_rstRSSET.getString("WK_DETQT"));
					else
						totNPRMDETQT+=Float.parseFloat(M_rstRSSET.getString("WK_DETQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_DCTQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMDCTQT+=Float.parseFloat(M_rstRSSET.getString("WK_DCTQT"));
					else
						totNPRMDCTQT+=Float.parseFloat(M_rstRSSET.getString("WK_DCTQT"));
					
					D_OUT.writeBytes(padSTRING('L',chkZERO(M_rstRSSET.getString("WK_DSRQT")),10));
					if(M_rstRSSET.getString("WK_QLTCT").equals("0"))
						totPRMDSRQT+=Float.parseFloat(M_rstRSSET.getString("WK_DSRQT"));
					else
						totNPRMDSRQT+=Float.parseFloat(M_rstRSSET.getString("WK_DSRQT"));
					
					crtNWLIN();
				}
				setTXCLR(D_OUT,Color.orange);
				if(Integer.parseInt(L_strOLDQLTCT) == 0)
					prnTOTAL(0);
				else 
					prnTOTAL(1); 		
				setTXCLR(D_OUT,Color.magenta);
				prnTOTAL1(L_strSUBCATDS);
				setTXCLR(D_OUT,Color.red);
				prnTOTAL2(L_strCATDS);
			}
			else
				setMSG("No Data Found in dspREPRT()",'E');
		}
		catch(Exception e)
		{
		    setMSG(e,"dspREPRT()");
		}
	}
	
	/**prints total . if LP_QLTCT=0 prime else non prime
	 */
	private void prnTOTAL(int LP_QLTCT)
	{
		try
		{
			if(LP_QLTCT == 0)
			{
				D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
				D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
				D_OUT.writeBytes(padSTRING('L',"TOTAL",15));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMCLOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMCLTQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMDDOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMDEOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMDCOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMDDTQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMDETQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMDCTQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totPRMDSRQT,3).toString()),10));
			}	
			else
			{
				D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
				D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
				D_OUT.writeBytes(padSTRING('L',"TOTAL",15));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMCLOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMCLTQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMDDOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMDEOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMDCOQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMDDTQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMDETQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMDCTQT,3).toString()),10));
				D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totNPRMDSRQT,3).toString()),10));

				totSUBCLOQT+=totPRMCLOQT+totNPRMCLOQT;
				totSUBCLTQT+=totPRMCLTQT+totNPRMCLTQT;
				totSUBDDOQT+=totPRMDDOQT+totNPRMDDOQT;
				totSUBDEOQT+=totPRMDEOQT+totNPRMDEOQT;
				totSUBDCOQT+=totPRMDCOQT+totNPRMDCOQT;
				totSUBDDTQT+=totPRMDDTQT+totNPRMDDTQT;
				totSUBDETQT+=totPRMDETQT+totNPRMDETQT;
				totSUBDCTQT+=totPRMDCTQT+totNPRMDCTQT;
				totSUBDSRQT+=totPRMDSRQT+totNPRMDSRQT;				
				
				totCLOQT+=totPRMCLOQT+totNPRMCLOQT;
				totCLTQT+=totPRMCLTQT+totNPRMCLTQT;
				totDDOQT+=totPRMDDOQT+totNPRMDDOQT;
				totDEOQT+=totPRMDEOQT+totNPRMDEOQT;
				totDCOQT+=totPRMDCOQT+totNPRMDCOQT;
				totDDTQT+=totPRMDDTQT+totNPRMDDTQT;
				totDETQT+=totPRMDETQT+totNPRMDETQT;
				totDCTQT+=totPRMDCTQT+totNPRMDCTQT;
				totDSRQT+=totPRMDSRQT+totNPRMDSRQT;				

				totPRMCLOQT=0;totPRMCLTQT=0;totPRMDDOQT=0;totPRMDEOQT=0;totPRMDCOQT=0;totPRMDDTQT=0;totPRMDETQT=0;totPRMDCTQT=0;totPRMDSRQT=0;
				totNPRMCLOQT=0;totNPRMCLTQT=0;totNPRMDDOQT=0;totNPRMDEOQT=0;totNPRMDCOQT=0;totNPRMDDTQT=0;totNPRMDETQT=0;totNPRMDCTQT=0;totNPRMDSRQT=0;
			}				
			crtNWLIN();
		}
		catch(Exception E)
		{
			System.out.println("Error In prnTOTAL()"+E);
		}	
	}	

	private void prnTOTAL1(String LP_SUBCATDS)
	{
		try
		{
			D_OUT.writeBytes(padSTRING('R',"&nbsp",10));
			D_OUT.writeBytes(padSTRING('L',LP_SUBCATDS+" TOT",10));
			D_OUT.writeBytes(padSTRING('L',"&nbsp",15));			
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBCLOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBCLTQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBDDOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBDEOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBDCOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBDDTQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBDETQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBDCTQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totSUBDSRQT,3).toString()),10));
			crtNWLIN();	
			totSUBCLOQT=0;totSUBCLTQT=0;totSUBDDOQT=0;totSUBDEOQT=0;totSUBDCOQT=0;totSUBDDTQT=0;totSUBDETQT=0;totSUBDCTQT=0;totSUBDSRQT=0;
		}
		catch(Exception E)
		{
			System.out.println("Error In prnTOTAL()"+E);
		}	
	}	

	private void prnTOTAL2(String LP_CATDS)
	{
		try
		{
			D_OUT.writeBytes(padSTRING('L',LP_CATDS+" TOT",10));
			D_OUT.writeBytes(padSTRING('L',"&nbsp",10));			
			D_OUT.writeBytes(padSTRING('R',"&nbsp",10));			
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totCLOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totCLTQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totDDOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totDEOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totDCOQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totDDTQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totDETQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totDCTQT,3).toString()),10));
			D_OUT.writeBytes(padSTRING('L',chkZERO(setNumberFormat(totDSRQT,3).toString()),10));
			crtNWLIN();
			totCLOQT=0;totCLTQT=0;totDDOQT=0;totDEOQT=0;totDCOQT=0;totDDTQT=0;totDETQT=0;totDCTQT=0;totDSRQT=0;
		}
		catch(Exception E)
		{
			System.out.println("Error In prnTOTAL()"+E);
		}	
	}	
	
	/** returns short descreption for corresponding GRPCD
	 */
	private String getCATDS(String LP_CODCD)
	{
		try
		{
			ResultSet rstRSSET1;
			String strSQLQRY1;
			strSQLQRY1 =" Select CMT_SHRDS from CO_CDTRN ";
			strSQLQRY1+=" where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPGR' and SUBSTRING(CMT_CODCD,1,4)='"+LP_CODCD+"'";
			rstRSSET1=cl_dat.exeSQLQRY2(strSQLQRY1);
			if(rstRSSET1.next() && rstRSSET1!=null)
			{
				return rstRSSET1.getString("CMT_SHRDS");
			}	
				
		}
		catch(Exception L_EX)
		{
		 setMSG(L_EX,"getCATDS()"); 
		}
		return "";
	}		
	
	/** if argument string ="0.000" reurns "-"
	 */
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
			if(M_rdbTEXT.isSelected())
			   D_OUT.writeBytes("\n");
			else
			{
				if(flgTBLDT)
					D_OUT.writeBytes("</TD></TR><TR>");
				else
					D_OUT.writeBytes("</P><P>");
			}
			cl_dat.M_intLINNO_pbst++;
			if(cl_dat.M_intLINNO_pbst>23)
			{
				prnFMTCHR(D_OUT,M_strEJT);
				cl_dat.M_intLINNO_pbst=1;
				genRPHDR();
	//			prnRPFTR();
			}
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

		/**Method to set Text color in HTML file<br>Has no effect in text format	 */
	void setTXCLR(DataOutputStream L_DOUT,Color P_clrCOLOR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(P_clrCOLOR.equals(Color.red))
				strTXCLR="<font Color=red size=2>";
			else if(P_clrCOLOR.equals(Color.black))
				strTXCLR="<font Color=black size=2>";
			else if(P_clrCOLOR.equals(Color.blue))
				strTXCLR="<font Color=blue size=2>";
			else if(P_clrCOLOR.equals(Color.magenta))
				strTXCLR="<font Color=purple size=2>";
			else if(P_clrCOLOR.equals(Color.green))
				strTXCLR="<font Color=green size=2>";
			else if(P_clrCOLOR.equals(Color.orange))
				strTXCLR="<font Color=orange size=2>";
			else if(P_clrCOLOR.equals(Color.yellow))
				strTXCLR="<font Color=yellow size=2>";
		}
		else if(M_rdbTEXT.isSelected())
		{
			if(P_clrCOLOR.equals(Color.red))
				prnFMTCHR(L_DOUT,M_strBOLD);
			else if(P_clrCOLOR.equals(Color.black))
				prnFMTCHR(L_DOUT,M_strNOBOLD);
			else if(P_clrCOLOR.equals(Color.blue))
				prnFMTCHR(L_DOUT,M_strBOLD);
			else if(P_clrCOLOR.equals(Color.magenta))
				prnFMTCHR(L_DOUT,M_strBOLD);
			else if(P_clrCOLOR.equals(Color.green))
				prnFMTCHR(L_DOUT,M_strBOLD);
		}
	}

	private void crtTBL(DataOutputStream L_DOUT,boolean P_flgBORDR) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(P_flgBORDR)
				L_DOUT.writeBytes("<p><TABLE border=1 borderColor=white borderColorDark=white borderColorLight=gray cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
			else
				L_DOUT.writeBytes("<p><TABLE border=0 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gostwhite cellPadding=0 cellSpacing=0 width=\"100%\"  align=center>");
			flgTBLDT=true;
		}
	}

	void crtHRLIN(DataOutputStream L_DOUT,String P_strLNCHR,int P_intCHRCT) throws Exception
	{
		if(M_rdbHTML.isSelected())
		{
			if(!flgTBLDT)
				L_DOUT.writeBytes("<HR>");
		}
		else
			for(int i=0;i<P_intCHRCT;i++)
				L_DOUT.writeBytes(P_strLNCHR);
		crtNWLIN();
	}	
	
	protected  String padSTRING(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		try
		{
			String L_STRSP = " ";
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			if(P_intPADLN <= L_STRLN && M_rdbTEXT.isSelected())
			{
				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			if(M_rdbHTML.isSelected())
			{
				if(P_chrPADTP=='C')
					P_strTRNVL="<p Align = center>"+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				else if(P_chrPADTP=='R')
					P_strTRNVL="<p Align = left>"+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				else if(P_chrPADTP=='L')
					P_strTRNVL="<p Align = right>"+strTXCLR+P_strSTRVL+"</font>"+"</P>";
				if(flgTBLDT)
					P_strTRNVL="<td>"+strTXCLR+P_strTRNVL+"</font>"+"</td>";
				return P_strTRNVL;
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
	
	
	void genRPHDR()
	{
		try
		{
			
			
			M_calLOCAL.setTime(M_fmtLCDAT.parse(txtDATE.getText()));      
			M_calLOCAL.add(Calendar.DATE,+1);    
			String L_strRPTDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<HTML><HEAD><STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE></HEAD>");
				D_OUT.writeBytes("<BODY bgColor=ghostwhite><P><HR><TABLE border=0 cellPadding=0 cellSpacing=0  width=\"100%\"> <TR><TD><IMG src=\"\\\\192.168.10.207\\user\\exec\\splerp2\\spllogo_old.gif\" style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></TD><TD><P align=left><STRONG><FONT face=Arial size=2>SUPREME PETROCHEM LTD</FONT></STRONG></P><STRONG><FONT face=Arial size=2><p align=left> STATEMENT OF DESPATCH & CLASSIFICATION FOR THE DAY ENDING AT 07:00 HRS ON "+L_strRPTDT+"</font></STRONG></TD><TD><p><FONT face=Arial size=2>Date : "+cl_dat.M_txtCLKDT_pbst.getText()+"</p><p><FONT face=Arial size=2>Page No. : "+Integer.toString(++M_intPAGNO)+"</P><TD> </TR></TABLE><HR><FONT face=\"Comic Sans MS\" >");
			}
			else
			{
				prnFMTCHR(D_OUT, M_strCPI10);
				prnFMTCHR(D_OUT, M_strBOLD);	
				D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
				D_OUT.writeBytes(padSTRING('R',"Date : "+cl_dat.M_txtCLKDT_pbst.getText(),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"STATEMENT OF CLASSIFICATION & DESPATCH FOR THE DAY ENDING AT 07:00 HRS ON "+L_strRPTDT,50));
				D_OUT.writeBytes(padSTRING('R',"Date : "+Integer.toString(++M_intPAGNO),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',""+txtDATE.getText(),30));
				crtHRLIN(D_OUT,"-",80);
				prnFMTCHR(D_OUT,M_strCPI17);
			}
			crtTBL(D_OUT,true);
			setTXCLR(D_OUT,Color.blue);
			D_OUT.writeBytes(padSTRING('R',"CAT",10));
			D_OUT.writeBytes(padSTRING('R',"SUB_CAT___",15));
			D_OUT.writeBytes(padSTRING('R',"GRADES_______",15));
			D_OUT.writeBytes(padSTRING('L',"___CLSFD",10));
			D_OUT.writeBytes(padSTRING('L',"___CLSFD",10));
			D_OUT.writeBytes(padSTRING('L',"<-----",10));
			D_OUT.writeBytes(padSTRING('L',"DSP ON DT",10));
			D_OUT.writeBytes(padSTRING('L',"----->",10));			
			D_OUT.writeBytes(padSTRING('L',"<-----",10));
			D_OUT.writeBytes(padSTRING('L',"DSP TO DT",10));
			D_OUT.writeBytes(padSTRING('L',"----->",10));
			D_OUT.writeBytes(padSTRING('L',"SALES_RT",10));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L',"&nbsp",10));
			D_OUT.writeBytes(padSTRING('L',"&nbsp",10));
			D_OUT.writeBytes(padSTRING('L',"&nbsp",15));
			D_OUT.writeBytes(padSTRING('L',"ON DT",10));
			D_OUT.writeBytes(padSTRING('L',"TO DT",10));
			D_OUT.writeBytes(padSTRING('L',"DOMESTIC",10));
			D_OUT.writeBytes(padSTRING('L',"__EXPORT",10));
			D_OUT.writeBytes(padSTRING('L',"_CAPTIVE",10));
			D_OUT.writeBytes(padSTRING('L',"DOMESTIC",10));
			D_OUT.writeBytes(padSTRING('L',"__EXPORT",10));
			D_OUT.writeBytes(padSTRING('L',"_CAPTIVE",10));
			D_OUT.writeBytes(padSTRING('L',"&nbsp",10));
			crtNWLIN();
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			M_intPAGNO=0;		
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

		protected  void prnFMTCHR(DataOutputStream L_DOUT,String L_FMTSTR){
		try{
			if(L_FMTSTR.equals(M_strCPI10))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<FONT Size = 6>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("P");
				}
			}
			if(L_FMTSTR.equals(M_strCPI12))
			{
				intCOLCT=90;
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<FONT  Size = 5>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("M");
				}
			}
			if(L_FMTSTR.equals(M_strCPI17))
			{
				intCOLCT=145;
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<FONT Size = 4>");
				else
					L_DOUT.writeChar(15);
			}
			if(L_FMTSTR.equals(M_strNOCPI17))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("</FONT>");
				else
					L_DOUT.writeChar(18);
			}
			if(L_FMTSTR.equals(M_strBOLD))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<STRONG>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("G");
				}
			}
			if(L_FMTSTR.equals(M_strNOBOLD))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("</STRONG>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("H");
				}
			}
			if(L_FMTSTR.equals(M_strENH))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("<FONT Size = 5><STRONG>");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("W1");
				}
			}
			if(L_FMTSTR.equals(M_strNOENH))
			{
				if(M_rdbHTML.isSelected())
					L_DOUT.writeBytes("</STRONG></FONT >");
				else
				{
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("W0");
					L_DOUT.writeChar(27);
					L_DOUT.writeBytes("F");
				}
			}
			if(L_FMTSTR.equals(M_strEJT))
			{
				if(M_rdbHTML.isSelected())
				{
					try{
							endTABLE(D_OUT);
							D_OUT.writeBytes(strPGBRK);
					}catch(Exception e)
					{setMSG(e,"Child.prnFMTCHR");}
				}
				else
					L_DOUT.writeChar(12);
			}
		}catch(IOException L_EX){
			setMSG(L_EX,"prnFMTCHR");
		}
	}

	private void endTABLE(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbHTML.isSelected())
			L_DOUT.writeBytes("</TR></TABLE></P>");
		flgTBLDT=false;
	}

	boolean vldDATA()
	{
		try
		{
			if(txtDATE.getText().length()<10)
			{
				txtDATE.requestFocus();
				setMSG("Please Enter Date",'E');
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
			M_intPAGNO=0;
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "fg_rpdcl.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "fg_rpdcl.doc";
				
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
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		M_rdbTEXT.setEnabled(false);		
	}
	
	
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
					return true;
				
			if(input == txtPRDCT)
			{
				if(txtPRDCT.getText().length() < 2)
				{
					setMSG("Enter Valid Product Category",'E');
					return false;
				}	
				M_strSQLQRY = "Select cmt_codcd,cmt_shrds from CO_CDTRN where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPGR' and SUBSTRING(cmt_codcd,3,2)='00' and SUBSTRING(cmt_codcd,1,2)='"+txtPRDCT.getText()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblPRDDS.setText(M_rstRSSET.getString("CMT_SHRDS"));
					setMSG("",'N');
					return true;
				}	
				else
				{
					setMSG("Enter Valid Product Category",'E');
					return false;
				}
			}	
			if(input == txtPRDCT1)
			{
				if(txtPRDCT.getText().length() < 2)
				{
					setMSG("Please select Main Product Category",'E');
					return false;
				}
				if(txtPRDCT1.getText().length() < 4)
				{
					setMSG("Enter Valid Product Category",'E');
					return false;
				}
				M_strSQLQRY = "Select cmt_codcd,cmt_shrds from CO_CDTRN where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPGR' and SUBSTRING(cmt_codcd,3,2)<>'00' and SUBSTRING(cmt_codcd,1,2)='"+txtPRDCT.getText().substring(0,2)+"' and SUBSTRING(cmt_codcd,1,4)='"+txtPRDCT1.getText()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblPRDDS1.setText(M_rstRSSET.getString("CMT_SHRDS"));
					setMSG("",'N');
					return true;
				}	
				else
				{
					setMSG("Enter Valid Product Category",'E');
					return false;
				}
			}	
			if(input == txtQLTCT)
			{
				if(txtQLTCT.getText().equals("0"))
					lblQLTCT.setText("PRIME");
				else if(txtQLTCT.getText().equals("1"))
					lblQLTCT.setText("NON PRIME");
				else
				{
					setMSG("Enter Valid Quality",'E');
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



