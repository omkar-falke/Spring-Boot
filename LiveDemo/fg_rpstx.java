/*
System Name   : Finished Goods Inventory Management System
Program Name  : Stock Statement Report
Program Desc. : User selects the Main Location & accordingly Stock Statement is displayed on the Screen.
				Stock Statement Report can be Printed,E-Mailed & a File can be created.
Author        : Mr. Zaheer Khan
Date          : 17.08.2006
Version       : FIMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/



import java.sql.ResultSet;import java.util.Date;import javax.swing.JCheckBox;
import java.awt.event.KeyEvent;import javax.swing.JComponent;import javax.swing.JComboBox;
import java.io.FileOutputStream;import java.io.DataOutputStream;import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.util.Calendar;


class fg_rpstx extends cl_rbase
{			
	private ButtonGroup btgRPTST;    
	private JRadioButton rdbCURNT;	 
	private JRadioButton rdbDYOPN;	
	private ButtonGroup btgPRDCT;    
	private JRadioButton rdbSPECF;	 
	private JRadioButton rdbALLCT;		/** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intPAGENO=0;					 /** Hashtable for storing different Receipt Types */  
	private Hashtable<String,String> hstRCTTP = new Hashtable<String,String>();/** Hashtable for storing different Main Product Types*/
	private Hashtable<String,String> hstMNGRP = new Hashtable<String,String>();/** Hashtable for storing different Sub Product Types*/
	private Hashtable<String,String> hstSBGRP = new Hashtable<String,String>();/** Hashtable for storing different Package Types*/
	private Hashtable<String,String> hstPKGTP = new Hashtable<String,String>();
	private Hashtable<String,String> hstRESQT = new Hashtable<String,String>();
	private Hashtable<String,String> hstGENCD = new Hashtable<String,String>();
	private Hashtable<String,String> hstPRINT = new Hashtable<String,String>();
	private Hashtable<String,String> hstQLHQT = new Hashtable<String,String>();
	private String strISODC1,strISODC2,strISODC3;	
	private cl_JTable tblENTTB1;	
	private JTextField txtCODE,txtDISCP,txtPRDTP;
	private JCheckBox chkCHKFL;
	private String staMNLCD[];
	private String staMNLDS[];
	private int LM_COUNT=0,l=0;
	private String strSTRCNT="";
	private String strDUMST="";
	private String strRESQT = "0";
	private String strREFDT = "";
	private boolean flgXPSFL = true;
	int j=0;
	int i=0;
	
	
	String LM_PRDTP,strPRSGR,LM_SUBPD,strCCSVL,strRESNO;
    String strPRDCD,strPRMGR,LM_CLSQT,strSALRT,strSTSFL;
	
	String strSTKVR = "";
	
	double dblDSTQT = 0;
	String strTPRQT = "";
	String strTPKQT = "";
	String strTSGQT = "";
	String strTMGQT = "";
	String strTSTQT = "";
	String strTGTQT = "";
	
	String strSLRPRQT = "";
	String strSLRPKQT = "";
	String strSLRSGQT = "";
	String strSLRMGQT = "";
	String strSLRSTQT = "";
	String strSLRGTQT = "";
	
	String strRESPRQT = "";
	String strRESPKQT = "";
	String strRESSGQT = "";
	String strRESMGQT = "";
	String strRESSTQT = "";
	String strRESGTQT = "";
	
	String strQLHPRQT = "0";
	String strQLHPKQT = "0";
	String strQLHSGQT = "0";
	String strQLHMGQT = "0";
	String strQLHSTQT = "0";
	String strQLHGTQT = "0";

	String strCMTPRQT = "0";   // Cubic Meters
	String strCMTPKQT = "0";
	String strCMTSGQT = "0";
	String strCMTMGQT = "0";
	String strCMTSTQT = "0";
	String strCMTGTQT = "0";

	String strPCSPRQT = "0";   // No.of Pieces
	String strPCSPKQT = "0";
	String strPCSSGQT = "0";
	String strPCSMGQT = "0";
	String strPCSSTQT = "0";
	String strPCSGTQT = "0";

	String strMTTPRQT = "0";   // Metric Tonnes
	String strMTTPKQT = "0";
	String strMTTSGQT = "0";
	String strMTTMGQT = "0";
	String strMTTSTQT = "0";
	String strMTTGTQT = "0";

	String strPRMGR1 = "";
	String strPRDCD1 = "";
	String strPRDDS1 = "";
	String strPKGTP1 = "";
	String strPRSGR1 = "";
    //String strPKGTP1 = "";
	String strSTSFL1 = "";
	
	String strPRMGR2 = "";
	String strPRDCD2 = "";
	String strPRDDS2 = "";
	String strPKGTP2 = "";
	String strPRSGR2 = "";
    //String strPKGTP2 = "";
	String strSTSFL2 = "";
	
	String strCLSTR = "";
	String strPDSTR = "";
	String strLCSTR = "";

	
	//String LM_PRDSTR = "";
	//String LM_LGRMNL = "";   //String for capturing Location Code from Location Group
	
	
	String staPRDQT[];
    String staPKGQT[];
	String staSBGQT[];
	String staMNGQT[];
	String staSTSQT[];
	String staGRTOT[];
	String staSTCNT[];
	String strPRDDS="";
	String strPKGTP="";
	int intFIRST=0;
	int intLAST=0;
	int intCOUNT=0;
			
	public fg_rpstx()
	{
		super(2);
		setMatrix(20,8);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			String L_strCODCD="";
			
			btgRPTST=new ButtonGroup();		
			add(new JLabel("Stock Statment of"),3,1,1,1.3,this,'L');
			add(rdbCURNT=new JRadioButton("Current Stock",true),3,3,1,1,this,'L');
			add(rdbDYOPN=new JRadioButton("Day Opening Stock"),3,4,1,2,this,'L');

			btgPRDCT=new ButtonGroup();		
			add(new JLabel("Product Category"),5,1,1,1.8,this,'L');
			add(rdbSPECF=new JRadioButton("Specific"),5,3,1,1,this,'L');
			add(rdbALLCT=new JRadioButton("All",true),5,4,1,1,this,'L');
		
			add(txtPRDTP = new TxtLimit(2),5,5,1,1,this,'L');
			//add(new txtPRDTP("Product Category"),5,1,1,1.8,this,'L');
			btgRPTST.add(rdbCURNT);
			btgRPTST.add(rdbDYOPN);
			
			btgPRDCT.add(rdbALLCT);
			btgPRDCT.add(rdbSPECF);
			
			tblENTTB1=crtTBLPNL1(this,new String[]{"Status","Main Loc","Discription"},10,6,3,5,3,new int[]{50,65,150,},new int[]{0});
			
			tblENTTB1.addFocusListener(this);
			tblENTTB1.addKeyListener(this);
			tblENTTB1.setCellEditor(0,chkCHKFL = new JCheckBox());
			tblENTTB1.setCellEditor(1,txtCODE = new TxtLimit(2));
			tblENTTB1.setCellEditor(2,txtDISCP = new TxtLimit(20));

			getMNLCD();			
			M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ( 'ISOFGXXRPT','M"+cl_dat.M_strCMPCD_pbst+"COXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL','STSPRNNPRD')";
			M_strSQLQRY += " and  isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			
			if(M_rstRSSET != null)   
			{
		    	while(M_rstRSSET.next())
        		{   
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("MG"))
						hstMNGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,2),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("SG"))
						hstSBGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,4),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("FGXXPKG"))
						hstPKGTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("PRNNPRD"))
						hstRCTTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					else
						hstGENCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
			}
			strISODC1 = hstGENCD.get("FG_RPRCM01").toString();
			strISODC2 = hstGENCD.get("FG_RPRCM02").toString();
			strISODC3 = hstGENCD.get("FG_RPRCM03").toString();
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		txtPRDTP.setEnabled(false);
	}
	private void getMNLCD()
	{ 
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS,CMT_CHP01 FROM CO_CDTRN WHERE CMT_CGMTP = 'S"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " AND CMT_CGSTP = 'FGXXLGR' and CMT_CODCD in ('04','06') ORDER BY CMT_CODCD";
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			//M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			i=0;
			//System.out.println("M_strSQLQRY = "+i);
			while(M_rstRSSET.next())
			{
			    //System.out.println("I = "+i);
				tblENTTB1.setValueAt(new Boolean(true),i,0);
				tblENTTB1.setValueAt(String.valueOf(M_rstRSSET.getString("CMT_CODCD")),i,1);
				//tblENTTB1.setValueAt("D",i,i+1);
				//System.out.println("Code = "+M_rstRSSET.getString("CMT_CODCD"));
				tblENTTB1.setValueAt(String.valueOf(M_rstRSSET.getString("CMT_SHRDS")),i,2);
				i++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMNLCD");	
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
		if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				getMNLCD();				
			}
		}
		if(M_objSOURC==rdbSPECF)
			txtPRDTP.setEnabled(true);
		else if(M_objSOURC==rdbALLCT)
		{	
			txtPRDTP.setText("");
			txtPRDTP.setEnabled(false);
		}	
	}
	public boolean vldDATA()
	{
		try
		{
						
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPRDTP)
			{
				//txtMNPRD.setText("");
				M_strHLPFLD  = "txtPRDTP";
				M_strSQLQRY  = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXPRD'";
				M_strSQLQRY+=  " AND  isnull(CMT_STSFL,'') <> 'X'";
				//System.out.println( "M_strSQLQRY =  "+M_strSQLQRY);
				if(txtPRDTP.getText().trim().length() > 0)
					M_strSQLQRY += " AND CMT_CODCD LIKE '"+txtPRDTP.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Product Category","Product Description"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtPRDTP)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPRDTP")
		{
			txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst);
			//txtMNPRD.requestFocus();
		}
	}
	
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			//System.out.println("rec 1 = "+intRECCT);
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rpstx.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpstx.doc";
			getDATA();
		
			/*
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			   	prnFMTCHR(dosREPORT,M_strCPI12);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}
			*/
			dosREPORT.close();
			fosREPORT.close();
			
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				{
					doPRINT(strFILNM);
				}
				else 
		        {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{					
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Statements"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	private void getDATA()
	{
		
		try
		{
			j=0;
			/* Variables Declared to store PREVIOUS values of Resultset **/
			double L_dblPKGQT=0;
			double L_dblTOTQT=0;
			double L_dblLOTQT=0;
			double L_dblPRDQT=0;
			double L_dblGRTOT=0;
			int L_intLOCCT=0;
			hstPRINT.clear();
			
			staPRDQT = new String[11];
            staPKGQT = new String[11];
			staSBGQT = new String[11];
			staMNGQT = new String[11];
			staSTSQT = new String[11];
			staGRTOT = new String[11];
			staSTCNT = new String[11];
			
			LM_COUNT=0;
			l=0;
			String L_strPRDTP="",L_strPRDTP1="";
			String L_strSUBPD="",L_strSUBPD1="";
			String L_strTPRCD="",L_strTPRCD1="";
			String L_strPKGTP="",L_strPKGTP1="";
			String L_strSTSFL="",L_strSTSFL1="";
						
			strTPRQT = "0";
			strTPKQT = "0";
			strTSGQT = "0";
			strTMGQT = "0";
			strTSTQT = "0";
			strTGTQT = "0";
			
			strSLRPRQT = "0";
			strSLRPKQT = "0";
			strSLRSGQT = "0";
			strSLRMGQT = "0";
			strSLRSTQT = "0";
			strSLRGTQT = "0";
			
			strRESPRQT = "0";
			strRESPKQT = "0";
			strRESSGQT = "0";
			strRESMGQT = "0";
			strRESSTQT = "0";
			strRESGTQT = "0";
			
			strQLHPRQT = "0";
			strQLHPKQT = "0";
			strQLHSGQT = "0";
			strQLHMGQT = "0";
			strQLHSTQT = "0";
			strRESGTQT = "0";

			strCMTPRQT = "0";   // Cubic Meters
			strCMTPKQT = "0";
			strCMTSGQT = "0";
			strCMTMGQT = "0";
			strCMTSTQT = "0";
			strCMTGTQT = "0";

			strPCSPRQT = "0";   // No.of Pieces
			strPCSPKQT = "0";
			strPCSSGQT = "0";
			strPCSMGQT = "0";
			strPCSSTQT = "0";
			strPCSGTQT = "0";

			strMTTPRQT = "0";   // Metric Tonnes
			strMTTPKQT = "0";
			strMTTSGQT = "0";
			strMTTMGQT = "0";
			strMTTSTQT = "0";
			strMTTGTQT = "0";

			
			strSTKVR = "";
			strCLSTR = "";
			strPDSTR = "";
			strLCSTR = "";
			//LM_PRDSTR = "";
			
			strPRMGR1 = "";  // Previous Product Type
			strPRDCD1 = "";  // Previous Product Description
			strPRSGR1 = "";  // Previous Product Sub Type
            strPKGTP1 = "";  // Previous Package Category
			strSTSFL1 = "";  // Previous Status Flag
			
			strPRMGR2 = "";  // Previous Product Type
			strPRDCD2 = "";  // Previous Product Description
			strPRDDS2 = "";
			strPKGTP2 = "";
			strPRSGR2 = "";  // Previous Product Sub Type
            //strPKGTP2 = "";  // Previous Package Category
			strSTSFL2 = "";  // Previous Status Flag
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				//System.out.println("prnFMTCHR");
			   prnFMTCHR(dosREPORT,M_strCPI17);
				//System.out.println("prnFMTCHR After");
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Stock Statements Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
			staMNLCD = new String[10];
			staMNLDS = new String[10];
			
			for(i=0; i<tblENTTB1.getRowCount();i++)
			{
				
				if(tblENTTB1.getValueAt(i,0).toString().trim().equals("true"))
				{
					staMNLCD[j] = tblENTTB1.getValueAt(i,1).toString().trim();
					staMNLDS[j] = tblENTTB1.getValueAt(i,2).toString().trim();
					//System.out.println("LM_MNLDSC["+j+"]"+LM_MNLDSC[j]);
					staSTCNT[j] = tblENTTB1.getValueAt(i,1).toString().trim();
					hstPRINT.put(tblENTTB1.getValueAt(i,1).toString().trim(),"VALUE");
					//System.out.println(staMNLDS[j]  +"  "+ j +" = "+tblENTTB1.getValueAt(i,1).toString().trim());
					getLGRMNL(staMNLCD[j]);
					LM_COUNT++;
					//intCOUNT++;
				}
			}
			
			intFIRST=Integer.parseInt(staSTCNT[0]);
			intLAST=Integer.parseInt(staSTCNT[LM_COUNT-1]);
			//System.out.println("intLAST "+intLAST);
			//System.out.println("LM_COUNT1 "+LM_COUNT);
			
			if(LM_COUNT<intLAST && LM_COUNT==1)
			{
				intLAST=intFIRST;
				//System.out.println("intLAST = "+intLAST);
			}
			else if(LM_COUNT+2==intLAST)
				intLAST=LM_COUNT+2;	
			else if (LM_COUNT+1==intLAST)
				intLAST=LM_COUNT+1;	
			
			//System.out.println("intLAST2  ="+intLAST);
			//System.out.println("LM_COUNT2 =  "+LM_COUNT);
			
			if(rdbSPECF.isSelected())
			{
				//LM_PRTYP = txtPRDTP.getText().toString().trim();
				strPDSTR = " and ST_PRDTP='"+txtPRDTP.getText().toString().trim()+"'";
			}
			
			if(rdbCURNT.isSelected())
			{
				strSTKVR = "ST_STKQT";
			}
			else if(rdbDYOPN.isSelected())
			{
				strSTKVR = "ST_DOSQT";
			}
			
			/**
			 * The following for loop assigns selected Main Location to the String Array.
			 * i.e LM_MNLCOD[] & LM_MNLDSC[]
			 */
			//System.out.println("002");
			
			//System.out.println("003");
			strPRMGR = "";
			strPRSGR = "";
			strPKGTP = "";
			strPRDCD = "";
			strPRDDS = "";
			strCCSVL = "";
			//System.out.println("LM_COUNT = "+LM_COUNT);
			intCOUNT=10;
			for(l = 0;l <= intCOUNT;l++)
			{
				staPRDQT[l] = "0";
                staPKGQT[l] = "0";
				staSBGQT[l] = "0";
				staMNGQT[l] = "0";
				staSTSQT[l] = "0";
				staGRTOT[l] = "0";
			}
			//System.out.println("004");
			//String LM_STRCNT ="('1','2','3' ";
			setMSG("Report Generation is in Progress.......",'N');		
			int k = strSTRCNT.length();
			strSTRCNT = strSTRCNT.substring(0,k-1).toString().trim();
			strSTRCNT = strSTRCNT + ")";
			strLCSTR =  " and  SUBSTRING(ST_MNLCD,1,1) in "+strSTRCNT+"";
			crtQLHQT();

			String strCLSTR =  " (ST_CPRCD is not null and ST_CPRCD <> ' ') and "+strSTKVR+" > 0";

			crtHSTRESQT();
			getREFDT();
			
			double[] dbaXPSDT = new double[2]; 
			double dblMTCQT = 0.000;
			double dblMTTQT = 0.000;
			String L_strMTCQT0 = "",L_strMTTQT0 = "";
			double L_dblRCTPK = 0;
			
			
			M_strSQLQRY = "Select  SUBSTRING(ST_PRDCD,1,2) LM_PRDTP, SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,PR_STSFL,";
            M_strSQLQRY += "A.CMT_CCSVL,ST_PKGTP,ST_PRDCD,PR_PRDDS,sum("+strSTKVR+") LM_CLSQT,ST_STSFL,ST_RESNO,B.CMT_NCSVL NCSVL";
			M_strSQLQRY += " from FG_STMST,CO_CDTRN A,CO_CDTRN B,CO_PRMST where B.CMT_CGMTP='SYS' and B.CMT_CGSTP='FGXXPKG' AND ST_PKGTP = B.CMT_CODCD AND A.CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and A.CMT_CGSTP='FGXXMNL' and";
			M_strSQLQRY += "  SUBSTRING(ST_MNLCD,1,1)=A.CMT_CODCD and  SUBSTRING(ST_MNLCD,1,1) in ('P','S') and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD and " + strCLSTR + strPDSTR + strLCSTR ; 
			M_strSQLQRY +=  " and  SUBSTRING(st_prdcd,1,2) "+ (flgXPSFL ?  "=" : "<>") + "'SX'";
			//M_strSQLQRY = "Select  SUBSTRING(ST_PRDCD,1,2) LM_PRDTP, SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,PR_STSFL,";
            //M_strSQLQRY += "CMT_CCSVL,ST_PKGTP,ST_PRDCD,PR_PRDDS,sum("+strSTKVR+") LM_CLSQT,ST_STSFL,ST_RESNO";
			//M_strSQLQRY += " from FG_STMST,CO_CDTRN,CO_PRMST where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXMNL' and";
			//M_strSQLQRY += "  SUBSTRING(ST_MNLCD,1,1)=CMT_CODCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD and " + strCLSTR + strPDSTR + strLCSTR ; 
			
            String L_strDBGRP = "  SUBSTRING(ST_PRDCD,1,2), SUBSTRING(ST_PRDCD,1,4),PR_STSFL,ST_PRDCD,ST_PKGTP,PR_PRDDS,A.CMT_CCSVL,ST_STSFL,ST_RESNO,B.CMT_NCSVL";
            String L_strDBORD = " LM_PRDTP,LM_SUBPD,PR_STSFL,ST_PRDCD,ST_PKGTP,CMT_CCSVL,ST_STSFL,ST_RESNO";
		//	String L_GRPSTR = getGRPSTR("SP",L_DTBSTR,L_DBSSTR);
			
			prnHEADER();
			
			M_strSQLQRY += " group by " + L_strDBGRP;
			M_strSQLQRY += " order by " + L_strDBORD;
			System.out.println(M_strSQLQRY);		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println("005");
			boolean L_1STFL = true;
			i = 0;
			boolean LM_EOF = false;
			M_rstRSSET.next();
			
			while(!LM_EOF)
			{
				strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").trim();
				strPRDCD = nvlSTRVL(M_rstRSSET.getString("ST_PRDCD"),"").trim();
				strSTSFL = nvlSTRVL(M_rstRSSET.getString("PR_STSFL"),"").trim();
                strPKGTP = nvlSTRVL(M_rstRSSET.getString("ST_PKGTP"),"").trim();
				strPRSGR = nvlSTRVL(M_rstRSSET.getString("LM_SUBPD"),"").trim();
				strPRMGR = nvlSTRVL(M_rstRSSET.getString("LM_PRDTP"),"").trim();
				strSALRT = nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"").trim();
				strRESNO = nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"").trim();
				dblDSTQT = M_rstRSSET.getDouble("LM_CLSQT");
				strRESQT="0";
				
				strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"").trim();
				
				//System.out.println("strCCSVL= "+strCCSVL);
				if(cl_dat.M_intLINNO_pbst >= 68)
				{
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO += 1;
					prnHEADER(); //gets the Header of the report
					//getTBLHDR(); // gets the Header of the Table
				}
				if(L_1STFL)
				{
					strPRDCD1 = strPRDCD;
					strPRDDS1 = strPRDDS;
					strPKGTP1 = strPKGTP;
					strPRSGR1 = strPRSGR;
					strPRMGR1 = strPRMGR;
					strSTSFL1 = strSTSFL;
					
					strPRDCD2 = strPRDCD;
					strPRDDS2 = strPRDDS;
					strPKGTP2 = strPKGTP;
					strPRSGR2 = strPRSGR;
					strPRMGR2 = strPRMGR;
					strSTSFL2 = strSTSFL;
					L_1STFL = false;
				}
				//prnGRPHDR("MG",4);
				//System.out.println("Main Loc = "+hstMNGRP.get(strPRMGR1).toString());
				dosREPORT.writeBytes(padSTRING('R',"",2));
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				
				dosREPORT.writeBytes(padSTRING('R',hstMNGRP.get(strPRMGR1).toString(),71));
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				strPRMGR1 = strPRMGR;
				while((strPRMGR).equals(strPRMGR1) && !LM_EOF)
				{
					
					
					//prnGRPHDR("SG",6);
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst +=1;
					dosREPORT.writeBytes(padSTRING('R',"",6));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					//System.out.println("Main Loc12 = "+hstSBGRP.get(strPRSGR1).toString());
					dosREPORT.writeBytes(padSTRING('R',hstSBGRP.get(strPRSGR1).toString(),71));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					strPRMGR = strPRMGR2;
					strPRMGR1 = strPRMGR;
					while((strPRMGR+strPRSGR).equals(strPRMGR1+strPRSGR1) && !LM_EOF)
					{
						//prnGRPHDR("ST",8);
						dosREPORT.writeBytes ("\n");
						cl_dat.M_intLINNO_pbst +=1;
						dosREPORT.writeBytes(padSTRING('R',"",8));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						//System.out.println("STSFL = "+hstRCTTP.get(strSTSFL1).toString());
						dosREPORT.writeBytes(padSTRING('R',hstRCTTP.get(strSTSFL1).toString(),57));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						strPRMGR = strPRMGR2;
						strPRSGR = strPRSGR2;
						strPRMGR1 = strPRMGR;
						strPRSGR1 = strPRSGR;
						while((strPRMGR+strPRSGR+strSTSFL).equals(strPRMGR1+strPRSGR1+strSTSFL1) && !LM_EOF)
						{
						//	prnGRPHDR("PK",12);
						//	dosREPORT.writeBytes ("\n");
							cl_dat.M_intLINNO_pbst +=1;
							dosREPORT.writeBytes(padSTRING('R',"",10));
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							//System.out.println("PKGTP = "+hstPKGTP.get(strPKGTP1).toString());
							//dosREPORT.writeBytes(padSTRING('R',hstPKGTP.get(strPKGTP1).toString(),71));
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
							dosREPORT.writeBytes ("\n");
							cl_dat.M_intLINNO_pbst +=1;
							
							strPRMGR = strPRMGR2;
							strPRSGR = strPRSGR2;
							strSTSFL = strSTSFL2;
							strPRMGR1 = strPRMGR;
							strPRSGR1 = strPRSGR;
							strSTSFL1 = strSTSFL;
                            while((strPRMGR+strPRSGR+strSTSFL+strPRDCD).equals(strPRMGR1+strPRSGR1+strSTSFL1+strPRDCD1) && !LM_EOF)
							{
								strPRMGR = strPRMGR2;
								strPRSGR = strPRSGR2;
								strSTSFL = strSTSFL2;
								strPRDCD = strPRDCD2;
								strPRMGR1 = strPRMGR;
								strPRSGR1 = strPRSGR;
								strSTSFL1 = strSTSFL;
								strPRDCD1 = strPRDCD;
                                while((strPRMGR+strPRSGR+strSTSFL+strPRDCD+strPKGTP).equals(strPRMGR1+strPRSGR1+strSTSFL1+strPRDCD1+strPKGTP1) && !LM_EOF)
								{
									
									
									int L_CCSVL = Integer.parseInt(strCCSVL);
									//System.out.println("006" +L_CCSVL);
									//System.out.println(" ddjj"+Double.parseDouble(LM_PRDCDQT[L_CCSVL])+dblDSTQT);
									
									staPRDQT[L_CCSVL] = setNumberFormat(Double.parseDouble(staPRDQT[L_CCSVL])+dblDSTQT,3);
                                    staPKGQT[L_CCSVL] = setNumberFormat(Double.parseDouble(staPKGQT[L_CCSVL])+dblDSTQT,3);
									staSBGQT[L_CCSVL] = setNumberFormat(Double.parseDouble(staSBGQT[L_CCSVL])+dblDSTQT,3);
									staMNGQT[L_CCSVL] = setNumberFormat(Double.parseDouble(staMNGQT[L_CCSVL])+dblDSTQT,3);
									staSTSQT[L_CCSVL] = setNumberFormat(Double.parseDouble(staSTSQT[L_CCSVL])+dblDSTQT,3);
									staGRTOT[L_CCSVL] = setNumberFormat(Double.parseDouble(staGRTOT[L_CCSVL])+dblDSTQT,3);
									//System.out.println("00622 = " +L_CCSVL);
									if (strSALRT.equals("2"))
									{
										strSLRPRQT = setNumberFormat(Double.parseDouble(strSLRPRQT)+dblDSTQT,3);
										strSLRPKQT = setNumberFormat(Double.parseDouble(strSLRPKQT)+dblDSTQT,3);
										strSLRSGQT = setNumberFormat(Double.parseDouble(strSLRSGQT)+dblDSTQT,3);
										strSLRMGQT = setNumberFormat(Double.parseDouble(strSLRMGQT)+dblDSTQT,3);
										strSLRSTQT = setNumberFormat(Double.parseDouble(strSLRSTQT)+dblDSTQT,3);
										strSLRGTQT = setNumberFormat(Double.parseDouble(strSLRGTQT)+dblDSTQT,3);
									}
									//if (strRESNO.length() > 0)
									//{
									//	strRESPRQT = setNumberFormat(Double.parseDouble(strRESPRQT)+dblDSTQT,3);
									//	strRESPKQT = setNumberFormat(Double.parseDouble(strRESPKQT)+dblDSTQT,3);
									//	strRESSGQT = setNumberFormat(Double.parseDouble(strRESSGQT)+dblDSTQT,3);
									//	strRESMGQT = setNumberFormat(Double.parseDouble(strRESMGQT)+dblDSTQT,3);
									//	strRESSTQT = setNumberFormat(Double.parseDouble(strRESSTQT)+dblDSTQT,3);
									//	strRESGTQT = setNumberFormat(Double.parseDouble(strRESGTQT)+dblDSTQT,3);
									//}
									strTPRQT = setNumberFormat(Double.parseDouble(strTPRQT)+dblDSTQT,3);
									strTPKQT = setNumberFormat(Double.parseDouble(strTPKQT)+dblDSTQT,3);
									strTSGQT = setNumberFormat(Double.parseDouble(strTSGQT)+dblDSTQT,3);
									strTMGQT = setNumberFormat(Double.parseDouble(strTMGQT)+dblDSTQT,3);
									strTSTQT = setNumberFormat(Double.parseDouble(strTSTQT)+dblDSTQT,3);
									strTGTQT = setNumberFormat(Double.parseDouble(strTGTQT)+dblDSTQT,3);
									strCMTPKQT = "0";
									strPCSPKQT = "0";
									strMTTPKQT = "0";
									
									/////XPS details
									L_dblRCTPK = M_rstRSSET.getDouble("LM_CLSQT")/M_rstRSSET.getDouble("NCSVL");
									strPCSPKQT = setNumberFormat(L_dblRCTPK,0);
									L_strMTCQT0 = "";L_strMTTQT0 = "";
									System.out.println(">>"+M_rstRSSET.getString("LM_PRDTP"));
									if(M_rstRSSET.getString("LM_PRDTP").equals("SX") && !M_rstRSSET.getString("ST_PKGTP").equals("99"))
									{
										dbaXPSDT[0]=0; dbaXPSDT[1]=0;
										dbaXPSDT = getXPSDT(L_dblRCTPK,M_rstRSSET.getString("ST_PRDCD"),M_rstRSSET.getString("ST_PKGTP"));
										//System.out.println("out>>"+dbaXPSDT[0]+"|"+dbaXPSDT[1]);
										strCMTPKQT = setNumberFormat(dbaXPSDT[0],3);
										strMTTPKQT = setNumberFormat(dbaXPSDT[1],3);
										System.out.println("L_strMTCQT0>>"+L_strMTCQT0);
										System.out.println("L_strMTTQT0>>"+L_strMTTQT0);
									}
									////////////////////////////////////////////////////////////
									
									if (!M_rstRSSET.next())
									{
										LM_EOF = true;
										break;
									}
									strPRMGR2 = nvlSTRVL(M_rstRSSET.getString("LM_PRDTP"),"").trim();
									strPRSGR2 = nvlSTRVL(M_rstRSSET.getString("LM_SUBPD"),"").trim();
									strPRDCD2 = nvlSTRVL(M_rstRSSET.getString("ST_PRDCD"),"").trim();
									strPRDDS2 = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"").trim();
                                    strPKGTP2 = nvlSTRVL(M_rstRSSET.getString("ST_PKGTP"),"").trim();
									strSTSFL2 = nvlSTRVL(M_rstRSSET.getString("PR_STSFL"),"").trim();
									if(hstRESQT.containsKey(strPRDCD))
									{
										
									   strRESPKQT = setNumberFormat((Double.parseDouble(hstRESQT.get(strPRDCD).toString()) > dblDSTQT ? dblDSTQT : Double.parseDouble(hstRESQT.get(strPRDCD).toString())),3);
									}
									
									if(strSTSFL2.equals("4"))
										strSTSFL2 = "3";
									
									dblDSTQT = M_rstRSSET.getDouble("LM_CLSQT");
									strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").trim();
									strSALRT = nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"").trim();
									strRESNO = nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"").trim();
									//strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"").trim();
									strPRDCD = strPRDCD2;
									strPRDDS = strPRDDS2;
                                    strPKGTP = strPKGTP2;
									strPRSGR = strPRSGR2;
									strPRMGR = strPRMGR2;
									strSTSFL = strSTSFL2;
									intRECCT++;
								}
								
								strQLHPKQT = "0";
								if(hstQLHQT.containsKey(strPRMGR1+strPRSGR1+strSTSFL1+strPRDCD1))
								   strQLHPKQT = hstQLHQT.get(strPRMGR1+strPRSGR1+strSTSFL1+strPRDCD1).toString();
								if (Double.parseDouble(strQLHPKQT) > 0)
								{
									strQLHPRQT = setNumberFormat(Double.parseDouble(strQLHPRQT)+Double.parseDouble(strQLHPKQT),3);
									strQLHSGQT = setNumberFormat(Double.parseDouble(strQLHSGQT)+Double.parseDouble(strQLHPKQT),3);
									strQLHMGQT = setNumberFormat(Double.parseDouble(strQLHMGQT)+Double.parseDouble(strQLHPKQT),3);
									strQLHSTQT = setNumberFormat(Double.parseDouble(strQLHSTQT)+Double.parseDouble(strQLHPKQT),3);
									strQLHGTQT = setNumberFormat(Double.parseDouble(strQLHGTQT)+Double.parseDouble(strQLHPKQT),3);
								}
								if (Double.parseDouble(strRESPKQT) > 0)
								{
									strRESPRQT = setNumberFormat(Double.parseDouble(strRESPRQT)+Double.parseDouble(strRESPKQT),3);
									strRESSGQT = setNumberFormat(Double.parseDouble(strRESSGQT)+Double.parseDouble(strRESPKQT),3);
									strRESMGQT = setNumberFormat(Double.parseDouble(strRESMGQT)+Double.parseDouble(strRESPKQT),3);
									strRESSTQT = setNumberFormat(Double.parseDouble(strRESSTQT)+Double.parseDouble(strRESPKQT),3);
									strRESGTQT = setNumberFormat(Double.parseDouble(strRESGTQT)+Double.parseDouble(strRESPKQT),3);
								}
								if (Double.parseDouble(strCMTPKQT) > 0)
								{
									strCMTPRQT = setNumberFormat(Double.parseDouble(strCMTPRQT)+Double.parseDouble(strCMTPKQT),3);
									strCMTSGQT = setNumberFormat(Double.parseDouble(strCMTSGQT)+Double.parseDouble(strCMTPKQT),3);
									strCMTMGQT = setNumberFormat(Double.parseDouble(strCMTMGQT)+Double.parseDouble(strCMTPKQT),3);
									strCMTSTQT = setNumberFormat(Double.parseDouble(strCMTSTQT)+Double.parseDouble(strCMTPKQT),3);
									strCMTGTQT = setNumberFormat(Double.parseDouble(strCMTGTQT)+Double.parseDouble(strCMTPKQT),3);
								}
								if (Double.parseDouble(strMTTPKQT) > 0)
								{
									strMTTPRQT = setNumberFormat(Double.parseDouble(strMTTPRQT)+Double.parseDouble(strMTTPKQT),3);
									strMTTSGQT = setNumberFormat(Double.parseDouble(strMTTSGQT)+Double.parseDouble(strMTTPKQT),3);
									strMTTMGQT = setNumberFormat(Double.parseDouble(strMTTMGQT)+Double.parseDouble(strMTTPKQT),3);
									strMTTSTQT = setNumberFormat(Double.parseDouble(strMTTSTQT)+Double.parseDouble(strMTTPKQT),3);
									strMTTGTQT = setNumberFormat(Double.parseDouble(strMTTGTQT)+Double.parseDouble(strMTTPKQT),3);
								}
								if (Double.parseDouble(strPCSPKQT) > 0)
								{
									strPCSPRQT = setNumberFormat(Double.parseDouble(strPCSPRQT)+Double.parseDouble(strPCSPKQT),0);
									strPCSSGQT = setNumberFormat(Double.parseDouble(strPCSSGQT)+Double.parseDouble(strPCSPKQT),0);
									strPCSMGQT = setNumberFormat(Double.parseDouble(strPCSMGQT)+Double.parseDouble(strPCSPKQT),0);
									strPCSSTQT = setNumberFormat(Double.parseDouble(strPCSSTQT)+Double.parseDouble(strPCSPKQT),0);
									strPCSGTQT = setNumberFormat(Double.parseDouble(strPCSGTQT)+Double.parseDouble(strPCSPKQT),0);
								}
								prnGRPTOT("PK",strSLRPKQT,strTPKQT,strRESPKQT,strQLHPKQT,strCMTPKQT, strMTTPKQT,strPCSPKQT,"N");
								intGRPTOT("PK");
							}
							prnGRPTOT("PR",strSLRPRQT,strTPRQT,strRESPRQT,strQLHPRQT,strCMTPRQT, strMTTPRQT,strPCSPRQT,"B");
							intGRPTOT("PR");
						}
						prnGRPTOT("ST",strSLRSTQT,strTSTQT,strRESSTQT,strQLHSTQT,strCMTSTQT, strMTTSTQT,strPCSSTQT,"B");
						intGRPTOT("ST");
					}
					prnGRPTOT("SG",strSLRSGQT,strTSGQT,strRESSGQT,strQLHSGQT,strCMTSGQT, strMTTSGQT,strPCSSGQT,"B");
					intGRPTOT("SG");
				}
				prnGRPTOT("MG",strSLRMGQT,strTMGQT,strRESMGQT,strQLHMGQT,strCMTMGQT, strMTTMGQT,strPCSMGQT,"B");
				intGRPTOT("MG");
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst +=1;
			prnGRPTOT("GT",strSLRGTQT,strTGTQT,strRESGTQT,strQLHGTQT,strCMTGTQT, strMTTGTQT,strPCSGTQT,"B");
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst +=1;
			
			prnFOOTR();
			
			//if(dosREPORT !=null)
			//	dosREPORT.close();
			//if(fosREPORT !=null)
			//	fosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX  + " GetDATA",'E');
		}
	}
	
	
	/**
	 * Creating hash table with Gradewise Reserved Quantity
	 */
	private void crtHSTRESQT()
	{
		try
		{
			hstRESQT.clear();
			M_strSQLQRY = "Select sr_prdcd, sum( isnull(sr_resqt,0)- isnull(sr_dspqt,0)) sr_resqt from fg_srtrn where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ( isnull(sr_resqt,0)- isnull(sr_dspqt,0))>0 and sr_stsfl<>'X' and  SUBSTRING(ltrim(str(sr_prdcd,20,0)),1,2) "+ (flgXPSFL ?  "=" : "<>") + "'SX' group by sr_prdcd";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET==null || !M_rstRSSET.next())
				return;
			while (true)
			{
				hstRESQT.put(M_rstRSSET.getString("SR_PRDCD").toString(),M_rstRSSET.getString("SR_RESQT").toString());
				//System.out.println(M_rstRSSET.getString("SR_PRDCD").toString()+" : "+M_rstRSSET.getString("SR_RESQT").toString());
				if(!M_rstRSSET.next())
					break;
			}
			M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX  + " crtHSTRESQT",'E');
		}
	}
	
	private void prnGRPTOT(String LP_GRPCT,String LP_SLRXXQT,String LP_TOTXXQT,String LP_RESXXQT,String LP_QLHXXQT,String LP_CMTXXQT,String LP_MTTXXQT,String LP_PCSXXQT,String LP_BLDFL)
	{
		try
		{
			/*if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				System.out.println("prnFMTCHR");
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
				System.out.println("prnFMTCHR After");
			  
			}*/
			String L_strPRT="";
			String L_GRPDS = "";
			//LM_PRNSTR = new StringBuffer("");
			if (LP_BLDFL.equals("B"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
			}
			if (LP_GRPCT.equals("PK"))
			{
				//L_GRPDS = cl_cust.ocl_cust.getPRDDS(strPRDCD1);
				dosREPORT.writeBytes(padSTRING('R',"",10));
				dosREPORT.writeBytes(padSTRING('R',strPRDDS1,15));
				dosREPORT.writeBytes(padSTRING('R',hstPKGTP.get(strPKGTP1).toString(),15));
				for(int l=intFIRST;l<=intLAST;l++)
				{
					L_strPRT="0"+l;
				//	System.out.println( "L_strPRT= "+L_strPRT);
					if(hstPRINT.containsKey(L_strPRT))
						
						dosREPORT.writeBytes(padSTRING('L',getDASH(staPKGQT[l]),12));
				}
			}
			else if (LP_GRPCT.equals("PR"))
			{
                                //dosREPORT.writeBytes ("\n");
                                //cl_dat.M_intLINNO_pbst+=1;
                //L_GRPDS = cl_dat.ocl_dat.getPRMCOD("CMT_SHRDS","SYS","FGXXPKG",strPKGTP1);
				//System.out.println("PKTTP ="+hstPKGTP.get(strPKGTP1).toString());
                dosREPORT.writeBytes(padSTRING('R',"",10));
                dosREPORT.writeBytes(padSTRING('R',"TOTAL " + strPRDDS1,30));
				for(int l=intFIRST;l<=intLAST;l++)
				{
					L_strPRT="0"+l;
				//	System.out.println( "L_strPRT= "+L_strPRT);
					if(hstPRINT.containsKey(L_strPRT))
						dosREPORT.writeBytes(padSTRING('L',getDASH(staPRDQT[l]),12));
				}
			}
			else if (LP_GRPCT.equals("ST"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
			//	L_GRPDS = cl_dat.getPRMCOD("cmt_codds","STS","MRXXSTS",strSTSFL1);
                dosREPORT.writeBytes(padSTRING('R',"",8));
                dosREPORT.writeBytes(padSTRING('R',"TOTAL " +hstRCTTP.get(strSTSFL1).toString(),32));
				for(int l=intFIRST;l<=intLAST;l++)
				{
					L_strPRT="0"+l;
				//	System.out.println( "L_strPRT= "+L_strPRT);
					if(hstPRINT.containsKey(L_strPRT))
						dosREPORT.writeBytes(padSTRING('L',getDASH(staSTSQT[l]),12));
				}
			}
			else if (LP_GRPCT.equals("SG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
			//	L_GRPDS = cl_cust.ocl_cust.getSBPRD(strPRSGR1);
				dosREPORT.writeBytes(padSTRING('R',"",6));
				dosREPORT.writeBytes(padSTRING('R',"TOTAL " + hstSBGRP.get(strPRSGR1).toString(),34));
				for(int l=intFIRST;l<=intLAST;l++)
				{
					L_strPRT="0"+l;
				//	System.out.println( "L_strPRT= "+L_strPRT);
					if(hstPRINT.containsKey(L_strPRT))
						dosREPORT.writeBytes(padSTRING('L',getDASH(staSBGQT[l]),12));
				}
			}
			else if (LP_GRPCT.equals("MG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				//System.out.println("MAIN GROUP ="+hstMNGRP.get(strPRMGR1).toString());
				//dosREPORT.writeBytes(padSTRING('R',hstMNGRP.get(strPRMGR1).toString(),71));
				//L_GRPDS = cl_cust.ocl_cust.getMNPRD(strPRMGR1);
				dosREPORT.writeBytes(padSTRING('R',"",2));
				dosREPORT.writeBytes(padSTRING('R',"TOTAL " + hstMNGRP.get(strPRMGR1).toString(),38));
				for(int l=intFIRST;l<=intLAST;l++)
				{
					L_strPRT="0"+l;
					//System.out.println( "L_strPRT= "+L_strPRT);
					if(hstPRINT.containsKey(L_strPRT))
					{
						//System.out.println( "IN MG" +L_strPRT);
						dosREPORT.writeBytes(padSTRING('L',getDASH(staMNGQT[l]),12));
					}
				}
				
			}
			else if (LP_GRPCT.equals("GT"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				dosREPORT.writeBytes(padSTRING('R',"",2));
				dosREPORT.writeBytes(padSTRING('R',"GRAND TOTAL",38));
				for(int l=intFIRST;l<=intLAST;l++)
				{
					L_strPRT="0"+l;
				//	System.out.println( "L_strPRT= "+L_strPRT);
					if(hstPRINT.containsKey(L_strPRT))
						dosREPORT.writeBytes(padSTRING('L',getDASH(staGRTOT[l]),12));
				}
			}
			dosREPORT.writeBytes(padSTRING('L',getDASH(LP_TOTXXQT),12));
			dosREPORT.writeBytes(padSTRING('L',getDASH(LP_SLRXXQT),12));
			dosREPORT.writeBytes(padSTRING('L',getDASH(LP_RESXXQT),10));
			dosREPORT.writeBytes(padSTRING('L',getDASH(LP_QLHXXQT),10));
			dosREPORT.writeBytes(padSTRING('L',getDASH(LP_CMTXXQT),10));
			dosREPORT.writeBytes(padSTRING('L',getDASH(LP_MTTXXQT),10));
			dosREPORT.writeBytes(padSTRING('L',getDASH(LP_PCSXXQT),10));
			if (LP_BLDFL.equals("B"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				
			}
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			if(cl_dat.M_intLINNO_pbst >= 64)
			{
				cl_dat.M_intLINNO_pbst = 0;
				//cl_dat.ocl_dat.M_PAGENO += 1;
				dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------\n");		
				//cl_dat.M_intLINNO_pbst += 1;
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
					prnFMTCHR(dosREPORT,M_strEJT);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				prnHEADER(); //gets the Header of the report
			}
		}
		catch(Exception L_EX)
		{
		//	showEXMSG(L_EX,"prnGRPTOT","");
		//	System.out.println("Exception: "+LM_PRNSTR.toString());
		}
	}
	
	public double[] getXPSDT(double LP_PKGQT, String LP_PRDCD,String LP_PKGTP)
	{
		double[] LP_dbaXPSDT = new double[2];
		try
		{
			//LP_staXPSDT[0] = LP_PRDCD.substring(2,4);
			//System.out.println("PRDCD>>"+LP_PRDCD.substring(2,4));
			String L_strSQLQRY = "select cmt_nmp01,cmt_nmp02,cmt_chp02 from spldata.co_cdtrn where cmt_cgstp='FGXXPKG' and cmt_codcd='"+LP_PKGTP+"'";
			//System.out.println(L_strSQLQRY);
			java.sql.ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(L_strSQLQRY);
			double L_dblMTCQT = 0.000;
			double L_dblMTTQT = 0.000;
			if(L_rstRSSET != null)
			{	
				if(L_rstRSSET.next())
				{
					//LP_staXPSDT[1] = L_rstRSSET.getString("cmt_nmp01");
					//LP_staXPSDT[2] = L_rstRSSET.getString("cmt_nmp02");
					//LP_staXPSDT[3] = L_rstRSSET.getString("cmt_chp02");
					//System.out.println("1.."+L_rstRSSET.getString("cmt_nmp01"));
					//System.out.println("2.."+L_rstRSSET.getString("cmt_nmp02"));	
					//System.out.println("3.."+L_rstRSSET.getString("cmt_chp02"));	
					LP_dbaXPSDT[0] = LP_PKGQT * ((Double.parseDouble(L_rstRSSET.getString("cmt_chp02")) *Double.parseDouble(L_rstRSSET.getString("cmt_nmp01")) * Double.parseDouble(L_rstRSSET.getString("cmt_nmp02")))/(1000*1000*1000));
					LP_dbaXPSDT[1] = (Double.parseDouble(LP_PRDCD.substring(2,4))*LP_dbaXPSDT[0])/1000;
				}
			}
		}
		catch(Exception L_EX)
	    {
			setMSG(L_EX  + " getXPSDT",'E');
		}
		return LP_dbaXPSDT;
	}

	
	private String getDASH(String LP_DSHSTR)
	{
		try
		{
			if(LP_DSHSTR == null || LP_DSHSTR.equals("0"))
				LP_DSHSTR = "-";
		}
		catch(Exception L_EX)
		{
			//showEXMSG(L_EX,"getDASH","");
		}
		return LP_DSHSTR;
	}
	
	private void intGRPTOT(String LP_GRPCT)
	{
		try
		{
			if (LP_GRPCT.equals("PK"))
			{
				for(l = intFIRST;l <= intLAST;l++)
                    staPKGQT[l] = "0";
				strSLRPKQT = "0";
				strRESPKQT = "0";
				strQLHPKQT = "0";
				strCMTPKQT = "0";
				strMTTPKQT = "0";
				strPCSPKQT = "0";
				strTPKQT = "0";
                strPKGTP1 = strPKGTP;
				//strPRDCD1 = strPRDCD;
				//strPRDDS1 = strPRDDS;
				//strSTSFL1 = strSTSFL;
				//strPRSGR1 = strPRSGR;
				//strPRMGR1 = strPRMGR;
			}
			else if (LP_GRPCT.equals("PR"))
			{
				for(l=intFIRST;l<= intLAST;l++)
					staPRDQT[l] = "0";
				strSLRPRQT = "0";
				strRESPRQT = "0";
				strQLHPRQT = "0";
				strCMTPRQT = "0";
				strMTTPRQT = "0";
				strPCSPRQT = "0";
				strTPRQT = "0";
				strPRDCD1 = strPRDCD;
				strPRDDS1 = strPRDDS;
				//strSTSFL1 = strSTSFL;
				//strPRSGR1 = strPRSGR;
				//strPRMGR1 = strPRMGR;
			}
			else if (LP_GRPCT.equals("ST"))
			{
				for(l = intFIRST;l <= intLAST;l++)
					staSTSQT[l] = "0";
				strSLRSTQT = "0";
				strRESSTQT = "0";
				strQLHSTQT = "0";
				strCMTSTQT = "0";
				strMTTSTQT = "0";
				strPCSSTQT = "0";
				strTSTQT = "0";
				strSTSFL1 = strSTSFL;
				//strPRSGR1 = strPRSGR;
				//strPRMGR1 = strPRMGR;
			}
			else if (LP_GRPCT.equals("SG"))
			{
				for(l = intFIRST;l <= intLAST;l++)
					staSBGQT[l] = "0";
				strSLRSGQT = "0";
				strRESSGQT = "0";
				strQLHSGQT = "0";
				strCMTSGQT = "0";
				strMTTSGQT = "0";
				strPCSSGQT = "0";
				strTSGQT = "0";
				strPRSGR1 = strPRSGR;
				//strPRMGR1 = strPRMGR;
			}
			else if (LP_GRPCT.equals("MG"))
			{
				for(l = intFIRST;l <= intLAST;l++)
					staMNGQT[l] = "0";
				strSLRMGQT = "0";
				strRESMGQT = "0";
				strQLHMGQT = "0";
				strCMTMGQT = "0";
				strMTTMGQT = "0";
				strPCSMGQT = "0";
				strTMGQT = "0";
				strPRMGR1 = strPRMGR;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"intGRPTOT");
		}
	}
	
	private void getLGRMNL(String LP_LGRMNL)    //To get Main Product Code i.e Polystyrene
	{
		try
		{
			ResultSet M_rstRSSET1;
			String L_MNLCD = "";
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXMNL' and CMT_CCSVL='"+LP_LGRMNL+"'";
			M_rstRSSET  = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				L_MNLCD   = M_rstRSSET.getString("CMT_CODCD");
				strSTRCNT = getSTRCNT(L_MNLCD); //method for String of Main Locations
				//System.out.println(LM_STRCNT);
			}
			M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
	}
	
	/**
	 * @return String
	 * @param (String MNLCD)
	 * method used to form String within the "in" statement i.e in('A','B')
	*/
	private String getSTRCNT(String LP_MNLCD)
	{ 
		if(j == 0)								
			strDUMST = "(";
		strDUMST = strDUMST + "'" + LP_MNLCD + "'" + ",";
		j++;
		return strDUMST;
	}
	
	/**
	  Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",100));
			dosREPORT.writeBytes("------------------------------\n");
			dosREPORT.writeBytes(padSTRING('L'," ",100));
			dosREPORT.writeBytes(strISODC1+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",100));
			dosREPORT.writeBytes(strISODC2+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",100));
			dosREPORT.writeBytes(strISODC3+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",100));
			dosREPORT.writeBytes("------------------------------\n");
			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst ,100));
			dosREPORT.writeBytes("Report Date:"+ cl_dat.M_strLOGDT_pbst + "\n");	
			
			dosREPORT.writeBytes(padSTRING('R',"Stock Statement as on "+(rdbDYOPN.isSelected() ? strREFDT+" at 07:00 Hrs" : cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText()+" Hrs"),100));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------\n");
			
			dosREPORT.writeBytes(padSTRING('R'," ",12));
			dosREPORT.writeBytes(padSTRING('R',"Product & Size",28));
			//System.out.println("LM_COUNT = "+LM_COUNT);
			for(i=0;i<LM_COUNT;i++)
				dosREPORT.writeBytes(padSTRING('L',staMNLDS[i],12));
			dosREPORT.writeBytes(padSTRING('L',"Tot.Sq.Mts",12));
			dosREPORT.writeBytes(padSTRING('L',"S/Rtn",12));
			dosREPORT.writeBytes(padSTRING('L',"Res.Qty",10));
			dosREPORT.writeBytes(padSTRING('L',"Q/Hold",10));
			dosREPORT.writeBytes(padSTRING('L',"Cub.Mtrs",10));
			dosREPORT.writeBytes(padSTRING('L',"M.Tons",10));
			dosREPORT.writeBytes(padSTRING('L',"Pcs",10));
			dosREPORT.writeBytes("\n");
			//crtLINE(136);
			//dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------\n");		
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst += 11;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
	/**
  	 *Method to Generate the Page Footer of the Report.
	*/
	private void prnFOOTR()
	{
		try
		{
			if(cl_dat.M_intLINNO_pbst >= 64)
			{
				dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------");		
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
				prnHEADER();
			}
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes ("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('L'," ",10));//margin
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",40));
			dosREPORT.writeBytes(padSTRING('R',"CHECKED BY  ",40));
			dosREPORT.writeBytes(padSTRING('R',"H.O.D (MHD)  ",40));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------\n");
			
			dosREPORT.writeBytes(" System generated report, hence signature not required \n");
			cl_dat.M_intLINNO_pbst += 8;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		    {   
				prnFMTCHR(dosREPORT,M_strEJT);
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}	
		}
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnFOOTR",'E');
		}	
	}
	

/**
 * Function for getting Reference Date
*/	
	public void getREFDT()//get reference date
	{
		try
		{
			Date L_strTEMP=null;
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				L_rstRSSET.close();
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
				strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				//System.out.println("REFDT = "+strREFDT);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
		}
	}
	
	/**
	 */
	private void crtQLHQT()
	{
		try
		{
			hstQLHQT.clear();
			//String strCLSTR =  " (ST_CPRCD is not null and ST_CPRCD <> ' ') and "+strSTKVR+" > 0 and st_CMPCD||st_prdtp||st_lotno||st_rclno in (select lt_CMPCD||lt_prdtp||lt_lotno||lt_rclno from pr_ltmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_resfl in ('Q','H'))";
			String strCLSTR =  " (ST_CPRCD is not null and ST_CPRCD <> ' ') and "+strSTKVR+" > 0 and st_CMPCD= lt_cmpcd and st_prdtp=lt_prdtp and st_lotno=lt_lotno and st_rclno=lt_rclno and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_resfl in ('Q','H') ";
			//System.out.println("strCLSTR : "+strCLSTR);

			crtHSTRESQT();
			
			M_strSQLQRY = "Select  SUBSTRING(ST_PRDCD,1,2) LM_PRDTP, SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,PR_STSFL,";
            M_strSQLQRY += "ST_PKGTP,ST_PRDCD,sum("+strSTKVR+") LM_CLSQT";
			M_strSQLQRY += " from FG_STMST,CO_PRMST,PR_LTMST where ";
			M_strSQLQRY += " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDCD=PR_PRDCD and " + strCLSTR + strPDSTR + strLCSTR ; 
			M_strSQLQRY +=  " and  SUBSTRING(st_prdcd,1,2) "+ (flgXPSFL ?  "=" : "<>") + "'SX'";
            String L_strDBGRP = "  SUBSTRING(ST_PRDCD,1,2), SUBSTRING(ST_PRDCD,1,4),PR_STSFL,ST_PKGTP,ST_PRDCD";
            String L_strDBORD = " LM_PRDTP,LM_SUBPD,PR_STSFL,ST_PKGTP,ST_PRDCD";
		//	String L_GRPSTR = getGRPSTR("SP",L_DTBSTR,L_DBSSTR);
			//prnHEADER();
			
			M_strSQLQRY += " group by " + L_strDBGRP;
			M_strSQLQRY += " order by " + L_strDBORD;
			//System.out.println(M_strSQLQRY);		
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET==null || !L_rstRSSET.next())
				return;
			while(true)
			{
				//System.out.println("Putting : "+L_rstRSSET.getString("LM_PRDTP")+L_rstRSSET.getString("LM_SUBPD")+L_rstRSSET.getString("PR_STSFL")+L_rstRSSET.getString("ST_PKGTP")+L_rstRSSET.getString("ST_PRDCD"));
				hstQLHQT.put(L_rstRSSET.getString("LM_PRDTP")+L_rstRSSET.getString("LM_SUBPD")+L_rstRSSET.getString("PR_STSFL")+L_rstRSSET.getString("ST_PKGTP")+L_rstRSSET.getString("ST_PRDCD"),L_rstRSSET.getString("LM_CLSQT"));
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
		}
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + "crtQLHQT",'E');
		}	
	}
}
