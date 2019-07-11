import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;

class hr_rpapf extends cl_rbase 
{

	private JTextField txtEMPNO;
	private JLabel lblEMPNM;
	private String strMMGRD="";
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpapf.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	SimpleDateFormat datDDMMYYYY=new SimpleDateFormat("dd/MM/yyyy");
	String[] staMRTLS_fn=new String[] {"Select","Unmarried","Married","Widow","Widower","Divorcee"};	
		
	hr_rpapf()		/*  Constructor   */
	{
		super(2);
		try
		{
			//System.out.println("hiiiiiiiiii");
			setMatrix(20,20);			
			
			add(new JLabel("Employee No."),6,9,1,2,this,'L');
			add(txtEMPNO= new TxtLimit(4),6,11,1,2,this,'L');
			add(lblEMPNM=new JLabel(),6,13,1,8,this,'L');     
			
			
			setENBL(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtEMPNO.setInputVerifier(oINPVF);
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
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
				M_rdbHTML.setSelected(true);
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
										//help for Department Code
				if(M_objSOURC==txtEMPNO)
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
        			String L_ARRHDR[] = {"Code","Category"};
        			M_strSQLQRY = "select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null AND EP_STSFL <> 'X'";
					M_strSQLQRY += " order by EP_EMPNO";
					System.out.println(">>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
        	{						
        		if(M_objSOURC==txtEMPNO)		
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
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNO.setText(L_STRTKN.nextToken());
				lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
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
			setMSG("Printing Report..",'N');
			
			M_strSQLQRY = "select * from HR_EPMST where EP_EMPNO = '"+txtEMPNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				
				prnFMTCHR(D_OUT,M_strBOLD);
				D_OUT.writeBytes(padSTRING('R',"PERSONAL DATA",50));
    			prnFMTCHR(D_OUT,M_strNOBOLD);
				crtNWLIN();
				D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
				crtNWLIN();
				String [] staADRPR = null; 
				String [] staADRTP = null; 
				staADRPR = M_rstRSSET.getString("EP_ADRPR").replace('|','~').split("~");
				staADRTP = M_rstRSSET.getString("EP_ADRTP").replace('|','~').split("~");
				D_OUT.writeBytes(padSTRING('R',"Full Name in block letters  :",30));
				D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_FULNM").replace('|',' ').toUpperCase(),50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Fathers Name : ",20));
				D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_LSTNM").toUpperCase(),50));
				crtNWLIN();
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Present Address : ",50));
				D_OUT.writeBytes(padSTRING('R',"",3));
				D_OUT.writeBytes(padSTRING('R',"Permanant Address : ",50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',staADRTP[0],50));
				D_OUT.writeBytes(padSTRING('R',"",3));
				D_OUT.writeBytes(padSTRING('R',staADRPR[0],50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',staADRTP[1],50));
				D_OUT.writeBytes(padSTRING('R',"",3));
				D_OUT.writeBytes(padSTRING('R',staADRPR[1],50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',staADRTP[2],50));
				D_OUT.writeBytes(padSTRING('R',"",3));
				D_OUT.writeBytes(padSTRING('R',staADRPR[2],50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',staADRTP[3],50));
				D_OUT.writeBytes(padSTRING('R',"",3));
				D_OUT.writeBytes(padSTRING('R',staADRPR[3],50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',staADRTP[4],50));
				D_OUT.writeBytes(padSTRING('R',"",3));
				D_OUT.writeBytes(padSTRING('R',staADRPR[4],50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',staADRTP[5],50));
				D_OUT.writeBytes(padSTRING('R',"",3));
				D_OUT.writeBytes(padSTRING('R',staADRPR[5],50));
				crtNWLIN();
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Tel No",20));
				D_OUT.writeBytes(padSTRING('R'," : "+staADRPR[6],50));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"E-mail",20));
				D_OUT.writeBytes(padSTRING('R'," : "+staADRPR[7],50));
				crtNWLIN();
				crtNWLIN();
				//genRPHDR_PD();
				D_OUT.writeBytes(padSTRING('R',"Department",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_DPTNM"),30));
				D_OUT.writeBytes(padSTRING('R',"Qualification",20));
				String [] staQUALN = null; 
				staQUALN = M_rstRSSET.getString("EP_QUALN").replace('|','~').split("~");
				D_OUT.writeBytes(padSTRING('R'," : "+staQUALN[0]+" "+staQUALN[1],30));
				crtNWLIN();
				
				D_OUT.writeBytes(padSTRING('R',"Designation",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_DESGN"),30));
				D_OUT.writeBytes(padSTRING('R',"Grade",20));
				strMMGRD=M_rstRSSET.getString("EP_MMGRD");
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_MMGRD"),30));
				crtNWLIN();
				
				D_OUT.writeBytes(padSTRING('R',"Basic",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_BASAL"),30));
				D_OUT.writeBytes(padSTRING('R',"Personal Pay",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_PPSAL"),30));
				crtNWLIN();
				
				D_OUT.writeBytes(padSTRING('R',"PAN No.",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_PANNO"),30));
				D_OUT.writeBytes(padSTRING('R',"Bank Account",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_ACTNO"),30));
				crtNWLIN();

				D_OUT.writeBytes(padSTRING('R',"Identification Mark",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_IDNMK"),30));
				D_OUT.writeBytes(padSTRING('R',"Physical Disability",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_PHDSB"),30));
				crtNWLIN();
				
				int intINDEX=Integer.parseInt(M_rstRSSET.getString("EP_MRTST"));
				D_OUT.writeBytes(padSTRING('R',"Marital Status",20));
				D_OUT.writeBytes(padSTRING('R'," : "+staMRTLS_fn[intINDEX],30));
				D_OUT.writeBytes(padSTRING('R',"Blood Group",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_BLDGP"),30));
				crtNWLIN();
				
				D_OUT.writeBytes(padSTRING('R',"Date Of Birth",20));
				D_OUT.writeBytes(padSTRING('R'," : "+datDDMMYYYY.format(M_rstRSSET.getDate("EP_BTHDT")),30));
				D_OUT.writeBytes(padSTRING('R',"Place Of Birth",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_BTHPL"),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Nationality",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_NATNL"),30));
				D_OUT.writeBytes(padSTRING('R',"Religion",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("EP_RELGN"),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Refferences",20));
				String [] staREFRN = null; 
				staREFRN = M_rstRSSET.getString("EP_REFRN").replace('|','~').split("~");
				D_OUT.writeBytes(padSTRING('R'," : "+staREFRN[0]+","+staREFRN[1],50));
				crtNWLIN();
				D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
				crtNWLIN();
				crtNWLIN();
			}
			
			prnFMTCHR(D_OUT,M_strBOLD);
			D_OUT.writeBytes(padSTRING('R',"SALARY DETAILS",50));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			M_strSQLQRY=" select * from HR_GRMST where GR_GRDCD = '"+strMMGRD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null && M_rstRSSET.next())
			{
				D_OUT.writeBytes(padSTRING('R',"Special Allowance",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_SPALW"),30));
				D_OUT.writeBytes(padSTRING('R',"Lunch Subsidy",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_LNSUB"),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Conveyance",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_CONVY"),30));
				D_OUT.writeBytes(padSTRING('R',"Child Edu Allowance",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_CHEDN"),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Medical Allowance",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_MDALW"),30));
				D_OUT.writeBytes(padSTRING('R',"L.T.A. %",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_LTALW"),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"PF %",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_PFALW"),30));
				D_OUT.writeBytes(padSTRING('R',"Super Annuation %",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_SAALW"),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"H.R.A. %",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_HRALW"),30));
				D_OUT.writeBytes(padSTRING('R',"D.A.",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_DNALW"),30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"Work Allowance",20));
				D_OUT.writeBytes(padSTRING('R'," : "+M_rstRSSET.getString("GR_WKALW"),30));
				crtNWLIN();
			}
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			crtNWLIN();
			
			
			prnFMTCHR(D_OUT,M_strBOLD);
			D_OUT.writeBytes(padSTRING('R',"EDUCATIONAL QUALIFICATION",50));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
			genRPHDR_QL();
			M_strSQLQRY=" select * from HR_QLMST where QL_EMPNO = '"+txtEMPNO.getText()+"' order by QL_PSGYR";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("QL_CATGR"),14));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("QL_PSGYR"),10));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("QL_COLLG"),29));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("QL_CATDS"),24));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("QL_PERCT"),10));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("QL_CLASS"),10));
					crtNWLIN();
				}
			}
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strBOLD);
			D_OUT.writeBytes(padSTRING('R',"FAMILY BACKGROUND",50));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
			genRPHDR_FL();
			M_strSQLQRY=" select * from HR_FLMST where FL_EMPNO = '"+txtEMPNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("FL_MEMNM"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("FL_RELDS"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("FL_QUALN"),16));
					if(M_rstRSSET.getDate("FL_BTHDT")==null)
						D_OUT.writeBytes(padSTRING('R',"",16));
					else
						D_OUT.writeBytes(padSTRING('R',datDDMMYYYY.format(M_rstRSSET.getDate("FL_BTHDT")),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("FL_OCCPN"),16));
					crtNWLIN();
				}
			}
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strBOLD);
			D_OUT.writeBytes(padSTRING('R',"LANGUAGE DETAILS",50));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
			genRPHDR_LG();
			M_strSQLQRY="  select * from HR_LGMST where LG_EMPNO = '"+txtEMPNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("LG_LNGDS"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("LG_SPKFL"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("LG_REDFL"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("LG_WRTFL"),16));
					crtNWLIN();
				}
			}
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();			
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strBOLD);
			D_OUT.writeBytes(padSTRING('R',"EMPLOYMENT HISTORY",50));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
			genRPHDR_HS();
			M_strSQLQRY="  select EP_STRDT,EP_ENDDT,EP_CMPNM,EP_DESGN from HR_EPHST where EP_EMPNO = '"+txtEMPNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_STRDT"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_ENDDT"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_CMPNM"),16));
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DESGN"),16));
					crtNWLIN();
				}
			}
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
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
				D_OUT.writeBytes("<HTML><HEAD><Title>Employee Information</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
			D_OUT.writeBytes(padSTRING('R',"Employee Information",50));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();			
    		crtNWLIN();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPHDR_QL()
	{
		try
		{
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Examination   Year Of   School,College,University    Main                    Total     Class");
			crtNWLIN();
			D_OUT.writeBytes("Passed        Passing   Institute attended           Subjects                % Marks   Obtained");
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header Quali");
		}
	}
	
	void genRPHDR_PD()
	{
		try
		{
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Date Of Birth   Place Of Birth   Nationality   Religion       Marital Status");
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header Quali");
		}
	}

	void genRPHDR_HS()
	{
		try
		{
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("From            To              Company Name    Designation     ");
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header Quali");
		}
	}
	
	void genRPHDR_FL()
	{
		try
		{
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Name            Relation        Qualification   Birth Date      Occupation");
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header Quali");
		}
	}

	void genRPHDR_LG()
	{
		try
		{
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Language        Speak           Read            Write");
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
			crtNWLIN();
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header Quali");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("-------------------------------------------------------------------------------------------------------");
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
			if(txtEMPNO.getText().length()<4)
			{
				txtEMPNO.requestFocus();
				setMSG("Please Enter From Employee No.",'E');
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
			     strRPFNM = strRPLOC + "hr_rpapf.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpapf.doc";
				
			genRPTFL();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				*/	
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
			if(((JTextField)input).getText().length() == 0)
				return true;
				
			if(input == txtEMPNO)
			{
				M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText().trim()+"' and EP_LFTDT is null and EP_STSFL <> 'X' ";
				//System.out.println("<<<<"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Employee Code",'E');
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



