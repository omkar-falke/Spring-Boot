import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.util.Vector;import java.io.File;import java.util.StringTokenizer;
/**
 * 
 * 
 * Program Name : Xport Proforma Invoice Report
 
   Purpose : This Program is used to generate Report of Xport Performa Invoice Details .


List of tables used :
Table Name     Primary key                                 Operation done
                                                            Insert   Update   Query   Delete	
---------------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD						            #
CO_PTMST       PT_PRTTP,PT_PRTCD									            #
MR_PIMST       PI_MKTTP,PI_PINNO							                    #
MR_PITRN       PIT_MKTTP,PIT_PINNO,PIT_LADNO,PIT_PRDCD                          #
MR_IVTRN       IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP                          #
--------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtMKTTP    CMT_CODCD       CO_CDTRN      Varchar(2)     product Type
txtBYRCD    IVT_BYRCD       MR_IVTRN      VARCHAR(5)     Buyer Code
txtPINNO    PI_PINNO        MR_PITRN      VARCHAR(8)     Invoice Number
--------------------------------------------------------------------------------------
 * 
 * In this report there are two optiona.
 * one for Front Side And  Another for Back side
 * 
*/

class mr_rppin extends cl_rbase
{
	String strMKTTP,strBYRCD,strCNSCD,strPINNO,strPINDT,strPLDDS,strPDSDS,strCNTFD,strCURCD;
	String strFDSDS,strADLNO,strADLDT,strMRKSH,strVSLNO,strBYRRF,strOTHRF,strPMTTM,strFSPNO;
	String strLADNO,strPRDRT,strPRDDS;
	String strCNTTP,strTSLNO,strCSLNO,strPRDCD,strPKGTP,strBYRNM,strBYAD1,strBYAD2,strBYAD3;
	String strBYAD4,strBYPIN,strCNSNM,strCNAD1,strCNAD2,strCNAD3,strCNAD4,strCNPIN,strCURDS;
	String strPRDCD1,strPKGTP1,strPRDDS1,strCNTNO1,strTSLNO1,strCSLNO1,strCNTTP1,strPRDCD2,strPKGTP2,strPRDDS2,strCNTNO2;
	String strTSLNO2,strCSLNO2,strCNTTP2,LM_HLPFLD,strCNTNO,strPRDRT1,strPRDRT2;
	String strDENOM,strDELTM,strCSHDS;
	String strPRDTP = "";
	Hashtable<String,String> hstPRDCD,hstPKGTP,hst1to100;
	private Hashtable<String,String[]> hstCNTDS;	
	//ResultSet M_rstRSSET;
	
	String LM_RESFIN = cl_dat.M_strREPSTR_pbst;
	String strLFTMRG, strLFTMRG_10, strVLINE, strVLINE1;
	String LM_RESSTR = LM_RESFIN.trim().concat("\\mr_tppin.doc"); 
	
	Vector<String> vtrCNTNO;
	Vector<String> vtrTSLNO;
	Vector<String> vtrCSLNO;
	Vector<String[]> vtrCNTDS1;
	Vector<String[]> vtrPRDDS1;
	Vector<String> vtrMNHDR;
	Vector<String[]> vtrMNHDR1;
	Vector<String> vtrLOTDTL;
	
	int intLINNO=0;//to calculate the length of a string in getSTRTKN();
	
	int intTOTSLPK,intTOALLPK,intTOCTNPK,intTOPRDPK,intDBSPK,intCOUNT;
	
	double dblTOTSLQT,dblTOALLQT,dblTOCTNQT,dblTOPRDQT,dblTOTSLVL,dblTOALLVL,dblTOCTNVL,dblTOPRDVL,dblDBSVL,dblDBSQT,dblDSCVL;
	double dblEXMPK;
	int intTOTPK;

	private int intCNTDS_TOT = 1;
    private int intAE_PIT_PRDPK = 0;
	private String strCNTNO_OLD, strPIT_CNTDS;
	
	FileOutputStream fosREPORT ;
    DataOutputStream dosREPORT ;
	private JTextField txtBYRCD;
	private JTextField txtBYRDS;
	private JTextField txtPINNO;
	private JTextField txtMKTTP;

	private ButtonGroup btgRETCPL;              // ButtonGroup for adding Radio Button
	private JRadioButton rdbPREPN;              //JRadioButton for Transaction Date Selection
	private JRadioButton rdbPLNPN;              //JRadioButton for Lot Number Selection
	
	private ButtonGroup btgSIDE;              // ButtonGroup for adding Radio Button
	private JRadioButton rdbFRSID;              //JRadioButton for Transaction Date Selection
	private JRadioButton rdbBKSID;              //JRadioButton for Lot Number Selection
	
	
	String strFILNM;
	int intRECCT=0;
			
	mr_rppin()
	{
		super(2);
		setMatrix(20,8);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		hst1to100 = new Hashtable<String,String>();
		hstCNTDS = new Hashtable<String,String[]>();
		btgRETCPL=new ButtonGroup();
		btgSIDE=new ButtonGroup();
		vtrCNTNO = new Vector<String>();
		vtrTSLNO = new Vector<String>();
		vtrCSLNO = new Vector<String>();
		vtrCNTDS1 = new Vector<String[]>();
		vtrPRDDS1 = new Vector<String[]>();
		vtrMNHDR  = new Vector<String>();
		vtrMNHDR1 = new Vector<String[]>();
		vtrLOTDTL = new Vector<String>();	
	    crtHST();
		add(rdbPREPN = new JRadioButton("PrePrinted",true),3,3,1,1,this,'L');
		add(rdbPLNPN = new JRadioButton("Plain",false),3,4,1,1,this,'L');
		add(new JLabel("Product Type"),4,3,1,1,this,'L');
		add(txtMKTTP= new TxtLimit(2),4,4,1,1,this,'L');
		add(new JLabel("Buyer"),5,3,1,1,this,'L');
		add(txtBYRCD = new TxtLimit(5),5,4,1,1,this,'L');
		add(txtBYRDS = new TxtLimit(18),5,5,1,3,this,'L');
		add(new JLabel("Invoice No"),6,3,1,1,this,'L');
		add(txtPINNO = new TxtLimit(8),6,4,1,1,this,'L');
		
		add(rdbFRSID = new JRadioButton("Front Side",true),7,3,1,1,this,'L');
		add(rdbBKSID = new JRadioButton("Back Side",false),7,4,1,1,this,'L');
		
		setENBL(false);
		btgRETCPL.add(rdbPREPN);
		btgRETCPL.add(rdbPLNPN);
		btgSIDE.add(rdbFRSID);
		btgSIDE.add(rdbBKSID);
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		txtBYRDS.setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		strPINNO=txtPINNO.getText().trim();
		strBYRCD=txtBYRCD.getText().trim();
		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
			}
			//txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			//txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)		
			cl_dat.M_PAGENO = 0;		
	}
	
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtMKTTP)
				{
					txtBYRCD.requestFocus();
				}
				if(M_objSOURC==txtBYRCD)
				{
					txtPINNO.requestFocus();
				}
				if(M_objSOURC==txtPINNO)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				
				strMKTTP=txtMKTTP.getText().trim();
				if(M_objSOURC==txtMKTTP)
				{
					M_strHLPFLD="txtMKTTP";
					
					String L_staHADER[] ={"Market Type","Description"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXMKT' ";
					if(txtMKTTP.getText().trim().length() >0)
						M_strSQLQRY += " and CMT_CODCD like '" + txtMKTTP.getText().trim() + "%' ";
					M_strSQLQRY += " order by CMT_CODCD";
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_staHADER,2,"CT"); 
					
				}
				if(M_objSOURC==txtBYRCD)
				{
					M_strHLPFLD="txtBYRCD";
					String L_staHADER[]=null;
					L_staHADER  = new String[]{"Buyer Code","Description","L.A. No.","Prof. Inv. No"};
					M_strSQLQRY = "Select distinct PI_BYRCD,PT_PRTNM,PIT_LADNO,PI_PINNO from MR_PITRN,MR_PIMST,CO_PTMST where";
					M_strSQLQRY += " PI_CMPCD = PIT_CMPCD and PI_MKTTP = PIT_MKTTP and PI_PINNO = PIT_PINNO and PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PI_MKTTP = '"+strMKTTP+"'";
					M_strSQLQRY += " and PI_BYRCD = PT_PRTCD  AND PT_PRTTP='C'";
					if(txtBYRCD.getText().trim().length()>0)
						M_strSQLQRY += " and PI_BYRCD like '" + txtBYRCD.getText().trim().toUpperCase() + "%' ";
					M_strSQLQRY += " Order by PI_PINNO desc";
				
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_staHADER,4,"CT");   
				}
				
				if(M_objSOURC==txtPINNO)
				{
					M_strHLPFLD="txtPINNO";
					String L_staHADER[] ={"Prov. Invoice No.","L.A. No."};
					
					M_strSQLQRY = "Select distinct PI_PINNO,PIT_LADNO from MR_PITRN,MR_PIMST where";
					M_strSQLQRY += " PI_CMPCD = PIT_CMPCD and PI_MKTTP = PIT_MKTTP and PI_PINNO = PIT_PINNO and";
					M_strSQLQRY += " PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PI_MKTTP = '"+strMKTTP+"' and PI_BYRCD = '"+txtBYRCD.getText().trim()+"'";
					if(txtPINNO.getText().trim().length()>0)
						M_strSQLQRY += " and PI_PINNO like '" + txtPINNO.getText().trim() + "%' ";
					M_strSQLQRY += " order by pi_pinno";
					System.out.println(" txtPINNO "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_staHADER,2,"CT");            
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"keyPressed");
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtMKTTP"))
			{
				txtMKTTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtBYRCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtBYRCD.setText(L_STRTKN1.nextToken());
				//L_STRTKN1.nextToken();
				 txtBYRDS.setText(L_STRTKN1.nextToken());
				 L_STRTKN1.nextToken();
				 txtPINNO.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtPINNO"))
			{
				txtPINNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception E)
		{
			setMSG(E," exeHLPOK ");
		}
	}
	public boolean vldDATA()
	{
		if(txtMKTTP.getText().trim().length()==0)
		{
			setMSG("Please Enter Product Type or press f1 for help.. ",'E');
			txtMKTTP.requestFocus();
			return false;
		}
		if(txtBYRCD.getText().trim().length()==0)
		{
			setMSG("Please Enter Buyer Code or press f1 for help.. ",'E');
			txtBYRCD.requestFocus();
			return false;
		}
		if(txtPINNO.getText().trim().length()==0)
		{
			setMSG("Please Enter Invoice Number or press f1 for help.. ",'E');
			txtPINNO.requestFocus();
			return false;
		}
		return true;
	}
	/**
	 * Method to generate the Report & send to the selected Destination.
	*/
	
	public void exePRINT()
	{
		//System.out.println("IN Print");
		if(!vldDATA())
			return;
		try
		{
			boolean L_flgPREPN=true;
			intRECCT=0;
			if(!rdbPREPN.isSelected())
				L_flgPREPN = false;
			String L_strBYRCD=txtBYRCD.getText().trim();
			String L_strPINNO=txtPINNO.getText().trim();
			if(rdbFRSID.isSelected())
				prnALLREC(txtMKTTP.getText().trim(),L_strBYRCD,L_strPINNO,L_flgPREPN);
			else
				prnCERREC_NEW(L_strBYRCD,L_strPINNO);
		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Transferr"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}

	public void prnALLREC(String P_strPRDTP,String P_strBYRCD,String P_strPINNO,boolean P_flgPRINT)
	{
		try
		{
			strPRDTP = P_strPRDTP;
			strFILNM=LM_RESSTR;
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			//fosREPORT = crtFILE(LM_RESSTR);
			//dosREPORT = crtDTOUTSTR(fosREPORT);	
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strCPI17);
			
			
			strBYRCD=P_strBYRCD;
			strPINNO=P_strPINNO;
			
			boolean L_flgPREPN = false;
			boolean L_EOF = false;
			boolean L_1STSFL = true;
			vtrCNTDS1.clear();
			vtrPRDDS1.clear();
			vtrMNHDR.clear();
			vtrMNHDR1.clear();
			intLINNO = 0;
			intCOUNT = 1;
			dblTOALLQT = 0;
			intTOALLPK = 0;
			dblTOALLVL = 0;
			
			dblTOTSLQT = 0;
			intTOTSLPK = 0;
			dblTOTSLVL = 0;
			
			dblTOCTNQT = 0;
			intTOCTNPK = 0;
			dblTOCTNVL = 0;
			
			dblTOPRDQT = 0;
			intTOPRDPK = 0;
			dblTOPRDVL = 0;
			
			L_flgPREPN   = P_flgPRINT;
			strVLINE  = " ";
			strVLINE1 = "  ";

			//if(!rdbPREPN.isSelected())
			//	L_flgPREPN = false;

			if(!L_flgPREPN)
			{
				strVLINE  = "|";
				strVLINE1 = "| ";
			}
			M_strSQLQRY = "Select pi_mkttp,pi_pinno,pi_pindt,pi_byrcd,pi_cnscd,pi_curcd,";
			M_strSQLQRY += "pi_adlno,pi_adldt,pi_mrksh,pi_fspno,pi_pldds,pi_pdsds,pi_fdsds,isnull(pi_dscvl,0) pi_dscvl,";
			M_strSQLQRY += "pi_cntfd,pi_othrf,pi_pmttm,pi_deltm,pi_vslno,pi_byrrf,pit_ladno,";
            M_strSQLQRY += "pit_prdcd,ivt_pkgtp,pit_prdds,pit_cntds,pit_tslno,isnull(pit_cslno,' ') pit_cslno,pit_prdqt,pit_prdpk,";
			M_strSQLQRY += "pit_prdrt,pit_prdvl,pit_cnttp from mr_pitrn,mr_pimst,mr_ivtrn where ";
            M_strSQLQRY += " PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pit_cmpcd=pi_cmpcd and pit_mkttp=pi_mkttp and pit_pinno=pi_pinno and pi_byrcd='"+strBYRCD+"'";
			M_strSQLQRY += " and pi_pinno='"+strPINNO+"' and pit_cmpcd=ivt_cmpcd and pit_mkttp=ivt_mkttp and pit_ladno = ivt_ladno and pit_prdcd = ivt_prdcd";
			M_strSQLQRY += " order by pit_cnttp,pit_prdcd,pit_cntds,pit_tslno,pit_cslno,pit_prdds";
			
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(M_rstRSSET.next())
			{
				while(!L_EOF)
				{
					strMKTTP = nvlSTRVL(M_rstRSSET.getString("pi_mkttp").toString(),"").trim();	
					strPINNO = nvlSTRVL(M_rstRSSET.getString("pi_pinno").toString(),"").trim();	
					//strPINDT = cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("pi_pindt"));
					strPINDT = nvlSTRVL(getRSTVAL(M_rstRSSET,"pi_pindt","D"),"").trim();
					strADLDT = nvlSTRVL(getRSTVAL(M_rstRSSET,"pi_adldt","D"),"").trim();	
				
					//System.out.println("1 "+strADLDT);
					//strADLDT = cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("pi_adldt"));	
					strBYRCD = nvlSTRVL(M_rstRSSET.getString("pi_byrcd").toString(),"").trim();	
					strCNSCD = nvlSTRVL(M_rstRSSET.getString("pi_cnscd").toString(),"").trim();	
					//System.out.println("2 "+strCNSCD);
					strCURCD = nvlSTRVL(M_rstRSSET.getString("pi_curcd").toString(),"").trim();	
					getCURDTL();//gets details regarding currency code
					strADLNO = nvlSTRVL(M_rstRSSET.getString("pi_adlno").toString(),"").trim();	
					strMRKSH = nvlSTRVL(M_rstRSSET.getString("pi_mrksh").toString(),"").trim();	
					strFSPNO = nvlSTRVL(M_rstRSSET.getString("pi_fspno").toString(),"").trim();	
					//System.out.println("3 "+strCNSCD);
					
					strPLDDS = nvlSTRVL(M_rstRSSET.getString("pi_pldds").toString(),"").trim();	
					strPDSDS = nvlSTRVL(M_rstRSSET.getString("pi_pdsds").toString(),"").trim();	
					strFDSDS = nvlSTRVL(M_rstRSSET.getString("pi_fdsds").toString(),"").trim();	
					
					//System.out.println("4 "+strCNSCD);
					
					strCNTFD = nvlSTRVL(M_rstRSSET.getString("pi_cntfd").toString(),"").trim();	
					strOTHRF = nvlSTRVL(M_rstRSSET.getString("pi_othrf").toString(),"").trim();	
                    strPMTTM = nvlSTRVL(M_rstRSSET.getString("pi_pmttm").toString(),"").trim(); 
					
					//System.out.println("5 "+strCNSCD);
					strDELTM = nvlSTRVL(M_rstRSSET.getString("pi_deltm").toString(),"").trim();	
					strVSLNO = nvlSTRVL(M_rstRSSET.getString("pi_vslno").toString(),"").trim();	
					strBYRRF = nvlSTRVL(M_rstRSSET.getString("pi_byrrf").toString(),"").trim();	
					
					//System.out.println("6 "+strCNSCD);
					
					strLADNO = nvlSTRVL(M_rstRSSET.getString("pit_ladno").toString(),"").trim();	
					strPRDCD = nvlSTRVL(M_rstRSSET.getString("pit_prdcd").toString(),"").trim();	
					strPKGTP = nvlSTRVL(M_rstRSSET.getString("ivt_pkgtp").toString(),"").trim();	
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("pit_prdds").toString(),"").trim();	
					strCNTNO = nvlSTRVL(M_rstRSSET.getString("pit_cntds").toString(),"").trim();	
					strTSLNO = nvlSTRVL(M_rstRSSET.getString("pit_tslno").toString(),"").trim();	
					strCSLNO = nvlSTRVL(M_rstRSSET.getString("pit_cslno").toString(),"").trim();	
					//System.out.println("7 "+strCNSCD);
					strCNTTP = nvlSTRVL(M_rstRSSET.getString("pit_cnttp").toString(),"").trim();	
					strPRDRT = nvlSTRVL(M_rstRSSET.getString("pit_prdrt").toString(),"").trim();	
					dblDBSQT = M_rstRSSET.getDouble("pit_prdqt");
					dblDSCVL = M_rstRSSET.getDouble("pi_dscvl");
					intDBSPK = M_rstRSSET.getInt("pit_prdpk");
					dblDBSVL = M_rstRSSET.getDouble("pit_prdvl");
					
					if(L_1STSFL)
					{
						strPRDCD1 = strPRDCD;	
						strPKGTP1 = strPKGTP;	
						strPRDDS1 = strPRDDS;	
						strCNTNO1 = strCNTNO;	
						strTSLNO1 = strTSLNO;	
						strCSLNO1 = strCSLNO;	
						strCNTTP1 = strCNTTP;
						strPRDRT1 = strPRDRT;
					
						strPRDCD2 = strPRDCD;	
						strPKGTP2 = strPKGTP;	
						strPRDDS2 = strPRDDS;	
						strCNTNO2 = strCNTNO;	
						strTSLNO2 = strTSLNO;
						strCSLNO2 = strCSLNO;
						strCNTTP2 = strCNTTP;
						strPRDRT2 = strPRDRT;
							
						prnHEADER(L_flgPREPN);
						L_1STSFL = false;
					}
					
					while((strCNTTP+strPRDCD+strPKGTP).equals(strCNTTP1+strPRDCD1+strPKGTP1) && !L_EOF)
					{
						strPRDCD = strPRDCD2;
						strPKGTP = strPKGTP2;
						strPRDCD1 = strPRDCD;
						strPKGTP1 = strPKGTP;
						strCNTTP = strCNTTP2;
						strCNTTP1 = strCNTTP;
						while((strCNTTP+strPRDCD+strPKGTP+strCNTNO+strTSLNO+strCSLNO).equals(strCNTTP1+strPRDCD1+strPKGTP1+strCNTNO1+strTSLNO1+strCSLNO1) && !L_EOF)
						{
							dblTOTSLQT += dblDBSQT;
							intTOTSLPK += intDBSPK;
							dblTOTSLVL += dblDBSVL;
								
							dblTOALLQT += dblDBSQT;
							intTOALLPK += intDBSPK;
							dblTOALLVL += dblDBSVL;
								
							dblTOCTNQT += dblDBSQT;
							intTOCTNPK += intDBSPK;
							dblTOCTNVL += dblDBSVL;
								
							dblTOPRDQT += dblDBSQT;
							intTOPRDPK += intDBSPK;
							dblTOPRDVL += dblDBSVL;
								
							if(!M_rstRSSET.next())
							{
								L_EOF = true;
								break;
							}
								
							strPRDCD2 = nvlSTRVL(M_rstRSSET.getString("pit_prdcd").toString(),"").trim();	
							strPKGTP2 = nvlSTRVL(M_rstRSSET.getString("ivt_pkgtp").toString(),"").trim();	
							strPRDDS2 = nvlSTRVL(M_rstRSSET.getString("pit_prdds").toString(),"").trim();	
							strCNTNO2 = nvlSTRVL(M_rstRSSET.getString("pit_cntds").toString(),"").trim();	
							strTSLNO2 = nvlSTRVL(M_rstRSSET.getString("pit_tslno").toString(),"").trim();	
							strCSLNO2 = nvlSTRVL(M_rstRSSET.getString("pit_cslno").toString(),"").trim();	
							strCNTTP2 = nvlSTRVL(M_rstRSSET.getString("pit_cnttp").toString(),"").trim();	
							strPRDRT2 = nvlSTRVL(M_rstRSSET.getString("pit_prdrt").toString(),"").trim();	
							dblDBSQT = M_rstRSSET.getDouble("pit_prdqt");
							intDBSPK = M_rstRSSET.getInt("pit_prdpk");
							dblDBSVL = M_rstRSSET.getDouble("pit_prdvl");
								
							strPRDCD = strPRDCD2;
							strPKGTP = strPKGTP2;
							strCNTTP = strCNTTP2;
							strCNTNO = strCNTNO2;
							strTSLNO = strTSLNO2;
							strCSLNO = strCSLNO2;
							strPRDDS = strPRDDS2;
							strPRDRT = strPRDRT2;
						}
						prnGRPTOT("TSL",dblTOTSLQT,intTOTSLPK,dblTOTSLVL,L_flgPREPN);
						intGRPTOT("TSL");
					}
					prnGRPTOT("PRD",dblTOPRDQT,intTOPRDPK,dblTOPRDVL,L_flgPREPN);
					intGRPTOT("PRD");
					
				}
				M_rstRSSET.close();
				
				prnFOOTR(L_flgPREPN);
				prnFMTCHR(dosREPORT,M_strEJT);
				dosREPORT.close();
				fosREPORT.close();
				setMSG(" ",'N');
			}
			else
			{
				setMSG("No record exist.",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnALLREC");
		}
	}
	
	
	private void prnHEADER(boolean P_flgPREPN)
	{
		try
		{
			strLFTMRG = "                    "+strVLINE+" ";
			strLFTMRG_10 = padSTR1('R'," ",20,"100",'A')+strVLINE+" ";
			//dosREPORT.writeBytes("\n");
            prnFMTCHR(dosREPORT,M_strBOLD);
			prnFMTCHR(dosREPORT,M_strBOLD);

			dosREPORT.writeBytes(strLFTMRG+padSTR1('L',getLINE(62)+"INVOICE"+getLINE(63),130,"171",'P')+strVLINE+"\n");   prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',"Exporter/Manufacturer",65,"171",'P')                                                              + strVLINE1+padSTR1('R',"Invoice No. & Date",34,"171",'P')                       +strVLINE1+padSTR1('R',"Exporter's Ref.",27,"171",'P')             +strVLINE+"\n");   prnFMTCHR(dosREPORT,M_strBOLD);

			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',cl_dat.M_strCMPNM_pbst,62,"171",'P')+strVLINE1);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			dosREPORT.writeBytes(padSTR1('R'," "+strPINNO+" "+strPINDT,37,"100",'A')                  +strVLINE1+padSTR1('R'," ",27,"100",'P')                           +strVLINE+"\n");   prnFMTCHR(dosREPORT,M_strNOBOLD);
			prnFMTCHR(dosREPORT,M_strCPI17);
			
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',"404, Raheja Chambers, Nariman Point,",65,"171",'P')  +strVLINE1+ padSTR1('R',"Buyer's Ref. No. & Date"+getLINE(39),63,"171",'P')                                                                          +strVLINE+"\n");
			
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',"Mumbai - 400 021.",62,"171",'P')                              +strVLINE1);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			dosREPORT.writeBytes(padSTR1('R'," "+strBYRRF,66,"100",'A')                                +strVLINE+"\n");
			prnFMTCHR(dosREPORT,M_strCPI17);
					
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',"Phones : 022-288 3517 to 20.",65,"171",'P')                   +strVLINE1+ padSTR1('R',"Other Reference(s)"+getLINE(44),63,"171",'P')        +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',"Fax : 022-288 3521",62,"171",'P')                             +strVLINE1);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			dosREPORT.writeBytes(padSTR1('R'," "+strOTHRF+" DT."+cl_dat.M_strLOGDT_pbst,66,"100",'A')         +strVLINE+"\n");
			prnFMTCHR(dosREPORT,M_strCPI17);
					
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',getLINE(130),130,"171",'P')                                                                                                                 +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',"Consignee :",65,"171",'P')                                    +strVLINE1+padSTR1('R',"Buyer (if other than Consignee)",63,"171",'P')+strVLINE+"\n");
			getBYRDTL();
			if(strBYRCD.equals(strCNSCD))
			{
				strCNSNM = "Same as Consignee";
				strCNAD1 = "";
				strCNAD2 = "";
				strCNAD3 = "";
				strCNAD4 = "";
				strCNPIN = "";
			}
			else
				getCNSDTL();
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strBYRNM,65,"171",'A')                                      +strVLINE1+padSTR1('R',strCNSNM,63,"171",'A')                                                                               +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strBYAD1,65,"171",'A')                                      +strVLINE1+padSTR1('R',strCNAD1,63,"171",'A')                                                                               +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strBYAD2,65,"171",'A')                                      +strVLINE1+padSTR1('R',strCNAD2,63,"171",'A')                                                                               +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strBYAD3,65,"171",'A')                                      +strVLINE1+padSTR1('R',strCNAD3,63,"171",'A')                                                                               +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strBYAD4,65,"171",'A')                                      +strVLINE1+padSTR1('R',strCNAD4,63,"171",'A')                                                                               +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strBYPIN,65,"171",'A')                                      +strVLINE1+padSTR1('R',strCNPIN,63,"171",'A')                                                                               +strVLINE+"\n");
			//dosREPORT.writeBytes(padSTR1('L'," ",21)+padSTR1('R',"|",65)+padSTR1('R',"|",64)+padSTR1('R',"|",1));
			//dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",65,"171",'A')                                                                                                                             +strVLINE1+padSTR1('R',"Country of Origin of goods"+getLINE(4),30,"171",'P')   +strVLINE1+padSTR1('R',"Country of Final Destination"+getLINE(2),31,"171",'P')          +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",65,"171",'A')                                                                                                                             +strVLINE1+padSTR1('R',"INDIA",30,"171",'A')                                   +strVLINE1+padSTR1('R',strFDSDS,31,"171",'A')                                           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",65,"171",'A')                                                                                                                             +strVLINE1+padSTR1('R',"Terms of Delivery and Payment"+getLINE(33),63,"171",'P')                                                  +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",65,"171",'A')                                                                                                                             +strVLINE1+padSTR1('R',strPMTTM+" "+(strDELTM.length()+strPMTTM.length()>60-strPMTTM.length() ? strDELTM.substring(0,60-strPMTTM.length()) : strDELTM),62,"171",'A')                                                         +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," Pre-Carriage by"+getLINE(14),31,"171",'P')                    +strVLINE1+padSTR1('R',"Place of Receipt by Pre Carrier",32,"171",'P')         +strVLINE1+padSTR1('R',(strDELTM.length()>60-strPMTTM.length() ? strDELTM.substring(60-strPMTTM.length(),strDELTM.length()) : " "),63,"171",'A')                         +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",31,"171",'A')                                               +strVLINE1+padSTR1('R'," ",32,"171",'A')                                       +strVLINE1+padSTR1('R'," ",63,"171",'A')                         +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',"Vessel/Flight No."+getLINE(11),31,"171",'P')                   +strVLINE1+padSTR1('R',"Port of Loading"+getLINE(17),32,"171",'P')             +strVLINE1+padSTR1('R'," ",63,"171",'P')                         +strVLINE+"\n");

			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strVSLNO,31,"171",'A')                                      +strVLINE1+padSTR1('R',strPLDDS,32,"171",'A')                                  +strVLINE1+padSTR1('R'," ",63,"171",'A')                         +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," Port of Discharge"+getLINE(12),31,"171",'P')                  +strVLINE1+padSTR1('R',"Final Destn."+getLINE(17),32,"171",'P')             +strVLINE1+padSTR1('R'," ",63,"171",'P')                         +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," "+strPDSDS,31,"171",'A')                                      +strVLINE1+padSTR1('R',strPDSDS,32,"171",'A')                                  +strVLINE1+padSTR1('R'," ",63,"171",'A')            +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R',getLINE(130),130,"171",'P')                                                                                                                 +strVLINE+"\n");

			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," Marks & Nos./",28,"171",'P')                  +padSTR1('R',"No. & Kind of Pkgs.",28,"171",'P')                  +padSTR1('R',"Description of Goods",22,"171",'P')         +strVLINE1+padSTR1('L',"     Quantity",13,"171",'P')         +strVLINE1+padSTR1('L',"         Rate",13,"171",'P')       +strVLINE1+padSTR1('L',"Amount",20,"171",'P')                      +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," Container No.",78,"171",'P')                                                                                                                                            +strVLINE1+padSTR1('L',(strPRDTP.equals("07")?" in SQM":" in MT"),11,"171",'A')         +strVLINE1+padSTR1('L',"    "+strCSHDS+(strPRDTP.equals("07")?"/SQM":"/MT"),11,"171",'A')        +strVLINE1+padSTR1('L',strCSHDS+" "+strPMTTM,16,"171",'A')         +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",78,"171",'A')                                                                                                                                                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                     +strVLINE1+padSTR1('R'," ",13,"171",'A')                   +strVLINE1+padSTR1('R'," ",20,"171",'A')                           +strVLINE+"\n");
			intLINNO += 3;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}	
	
	private void prnGRPTOT(String P_strGRPCT,double P_dblTOXXXQT,int P_intTOXXXPK,double P_dblTOXXXVL,boolean P_flgPREPN)
	{
        try
		{
			String L_strPRNSTR = "";
            if(P_strGRPCT.equals("TSL"))
			{
            	//L_strPRNSTR += strLFTMRG_10          +padSTR1('R'," ",8,"100",'A')         +padSTR1('R',strCNTNO1,22,"100",'A')              +padSTR1('R',strTSLNO1,18,"100",'A')            +padSTR1('R',strPRDDS1,16,"100",'A')                +padSTR1('L',setFMT(String.valueOf(P_dblTOXXXQT),3),14,"100",'A')+strVLINE1  +padSTR1('R'," ",13,"100",'A')            +strVLINE1+padSTR1('R'," ",13,"100",'A')           +strVLINE1+padSTR1('R'," ",20,"100",'A')           +strVLINE+"\n";
				//vtrCNTDS.addElement(L_strPRNSTR);
				//vtrCNTDS1.addElement(new String[] {strCNTNO1,strTSLNO1,strCSLNO1,strPRDDS1,setFMT(String.valueOf(P_dblTOXXXQT),3)});
				vtrCNTDS1.addElement(new String[] {strCNTNO1,strTSLNO1,strCSLNO1,strPRDDS1,setNumberFormat(P_dblTOXXXQT,3)});
				//System.out.println("Adding : "+strCNTNO1+"/"+strTSLNO1+"/"+strCSLNO1+"/"+strPRDDS1+"/"+setFMT(String.valueOf(P_dblTOXXXQT),3));
				intLINNO += 1;
			}
			else if(P_strGRPCT.equals("PRD"))
			{
				String L_strPRDDS = strPRDCD1;
				String L_strPKGDS = "Size (in MM) : "+getPRMCOD("cmt_shrds","SYS","FGXXPKG",strPKGTP1);
				if(L_strPRDDS.substring(2,4).trim().equals("13"))
					L_strPRDDS = "TOUGHENED POLYSTYRENE (SB - COPOLYMER)";
				else if(L_strPRDDS.substring(2,4).equals("11"))
					L_strPRDDS = "GENERAL PURPOSE POLYSTYRENE";
				else if(L_strPRDDS.substring(2,4).equals("12"))
					L_strPRDDS = "HIGH IMPACT POLYSTYRENE RESIN";
                //L_strPRNSTR += strLFTMRG       +padSTR1('R'," "+setFMT(String.valueOf(P_intTOXXXPK),0)+" Bags",28,"171",'A')                        +padSTR1('R',L_strPRDDS,35,"171",'A')             +padSTR1('R',"Grade "+strPRDDS1,15,"171",'A')        +strVLINE1+padSTR1('L',setFMT(String.valueOf(P_dblTOXXXQT),3),13,"171",'A')         +strVLINE1+padSTR1('L',strPRDRT1,13,"171",'A')               +strVLINE+padSTR1('L',setFMT(String.valueOf(P_dblTOXXXVL),2),21,"171",'A')             +strVLINE+"\n";
				//vtrPRDDS.addElement(L_strPRNSTR);
				//vtrPRDDS1.addElement(new String[] {setFMT(String.valueOf(P_intTOXXXPK),0)+" Bags",  L_strPRDDS,"Grade "+strPRDDS1,setFMT(String.valueOf(P_dblTOXXXQT),3),strPRDRT1,setFMT(String.valueOf(P_dblTOXXXVL),2)});
				//vtrPRDDS1.addElement(new String[] {setNumberFormat(P_intTOXXXPK,0)+" Bags",  L_strPRDDS,"Grade "+strPRDDS1,setNumberFormat(P_dblTOXXXQT,3),strPRDRT1,setNumberFormat(P_dblTOXXXVL,2)});
				vtrPRDDS1.addElement(new String[] {setNumberFormat(P_intTOXXXPK,0)+(strPRDTP.equals("07")?" Sheets":" Bags"),  (strPRDTP.equals("07")? L_strPKGDS : L_strPRDDS),"Grade "+strPRDDS1,setNumberFormat(P_dblTOXXXQT,3),strPRDRT1,setNumberFormat(P_dblTOXXXVL,2)});
				intLINNO += 1;
			}
			else if(P_strGRPCT.equals("TOT"))
			{
				
				String L_strCNTDS = getPRMCOD("cmt_codds","MST","COXXCTN",strCNTTP1);
				//L_strPRNSTR += strLFTMRG       +padSTR1('R'," "+setFMT(String.valueOf(P_intTOXXXPK),0)+" Bags",28,"171",'A')                        +padSTR1('R',String.valueOf(intCOUNT)+" x "+L_strCNTDS+" containing Polystyrene.",50,"171",'A')       +strVLINE1+padSTR1('R'," ",13,"171",'A')                                          +strVLINE1+padSTR1('R'," ",13,"171",'A')                     +strVLINE1+padSTR1('R'," ",20,"171",'A')                                             +strVLINE+"\n";
				L_strPRNSTR += strLFTMRG       +padSTR1('R'," "+setNumberFormat(P_intTOXXXPK,0)+(strPRDTP.equals("07")?" Sheets":" Bags"),28,"171",'A')                        +padSTR1('R',String.valueOf(intCOUNT)+" x "+L_strCNTDS+" containing Polystyrene.",50,"171",'A')       +strVLINE1+padSTR1('R'," ",13,"171",'A')                                          +strVLINE1+padSTR1('R'," ",13,"171",'A')                     +strVLINE1+padSTR1('R'," ",20,"171",'A')                                             +strVLINE+"\n";	
				L_strPRNSTR += strLFTMRG       +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                       +strVLINE1+padSTR1('R'," ",13,"171",'A')                                          +strVLINE1+padSTR1('R'," ",13,"171",'A')                     +strVLINE1+padSTR1('R'," ",20,"171",'A')                                             +strVLINE+"\n";
				intLINNO += 2;
				vtrMNHDR.addElement(L_strPRNSTR);
				//vtrMNHDR1.addElement(new String[] {setFMT(String.valueOf(P_intTOXXXPK),0)+" Bags",String.valueOf(intCOUNT)+" x "+L_strCNTDS+" containing Polystyrene."});
				vtrMNHDR1.addElement(new String[] {setNumberFormat(P_intTOXXXPK,0)+(strPRDTP.equals("07")?" Sheets":" Bags"),String.valueOf(" "+intCOUNT)+" x "+L_strCNTDS+" containing "});
			}
			else if(P_strGRPCT.equals("TOT1"))
			{
				
				vtrMNHDR1.addElement(new String[] {"",(strPRDTP.equals("07")?"     Extruded Polystyrene Insulation Board":"     Polystyrene")});
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPTOT");
		}
	}
	
	private void intGRPTOT(String P_strGRPCT)
	{
		try
		{
			if(P_strGRPCT.equals("TSL"))
			{
				if(!strCNTNO1.equals(strCNTNO))
					intCOUNT++;
				strCNTNO1 = strCNTNO;
				strTSLNO1 = strTSLNO;
				strCSLNO1 = strCSLNO;
				dblTOTSLQT = 0;
				intTOTSLPK = 0;
				dblTOTSLVL = 0;
			}
			else if(P_strGRPCT.equals("CNT"))
			{
				strCNTTP1 = strCNTTP;
				dblTOCTNQT = 0;
				intTOCTNPK = 0;
				dblTOCTNVL = 0;
			}
			else if(P_strGRPCT.equals("PRD"))
			{
				strPRDCD1 = strPRDCD;
				strPRDDS1 = strPRDDS;
				strPRDRT1 = strPRDRT;
				dblTOPRDQT = 0;
				intTOPRDPK = 0;
				dblTOPRDVL = 0;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"intGRPTOT");
		}
	}
	
	private void getBYRDTL()
	{
		try
		{
			ResultSet L_rstRSSET;
			M_strSQLQRY = "Select pt_prtnm,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd";
			M_strSQLQRY += " from co_ptmst where pt_prtcd='"+strBYRCD+"' and pt_prttp='C'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
			{
				strBYRNM = nvlSTRVL(L_rstRSSET.getString("pt_prtnm"),"").trim();
				strBYAD1 = nvlSTRVL(L_rstRSSET.getString("pt_adr01"),"").trim();
				strBYAD2 = nvlSTRVL(L_rstRSSET.getString("pt_adr02"),"").trim();
				strBYAD3 = nvlSTRVL(L_rstRSSET.getString("pt_adr03"),"").trim();
				strBYAD4 = nvlSTRVL(L_rstRSSET.getString("pt_adr04"),"").trim();
				strBYPIN = nvlSTRVL(L_rstRSSET.getString("pt_pincd"),"").trim();
			}
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getBYRDTL");
		}
	}
	
	private void getCNSDTL()
	{
		try
		{
			ResultSet L_rstRSSET;
			M_strSQLQRY = "Select pt_prtnm,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd";
			M_strSQLQRY += " from co_ptmst where pt_prtcd='"+strCNSCD+"' and pt_prttp='C'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
			{
				strCNSNM = nvlSTRVL(L_rstRSSET.getString("pt_prtnm"),"").trim();
				strCNAD1 = nvlSTRVL(L_rstRSSET.getString("pt_adr01"),"").trim();
				strCNAD2 = nvlSTRVL(L_rstRSSET.getString("pt_adr02"),"").trim();
				strCNAD3 = nvlSTRVL(L_rstRSSET.getString("pt_adr03"),"").trim();
				strCNAD4 = nvlSTRVL(L_rstRSSET.getString("pt_adr04"),"").trim();
				strCNPIN = nvlSTRVL(L_rstRSSET.getString("pt_pincd"),"").trim();
			}
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCNSDTL");
		}
	}
	
	/**
	 *
	 *Method to create lines that are used in the Reports
	 * 
	*/
	private String crtLINE1(int L_intCNT,String P_strLINCHR)
    {
		String L_strLINCH = "";
		if(rdbPREPN.isSelected() && P_strLINCHR.equals("-"))
			P_strLINCHR = " ";
		try
		{
			for(int i=1;i<=L_intCNT;i++)
			{
				L_strLINCH += P_strLINCHR;
			}
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
			setMSG(L_EX,"Error in Line");
		}
        return L_strLINCH;
	}
	
	
	
	/**
	 */
    private  String padSTR1(char P_chrPADTP,String P_strSTRVL,int P_intPADLN, String P_strCHRSZ, char P_chrPRPRT)
	{
		String P_strTRNVL = "";
        //int L_intPADLN = new Double(Double.parseDouble(P_strCHRSZ)*(100/171)*P_intPADLN).intValue();
        int L_intPADLN = new Double((Double.parseDouble(P_strCHRSZ)/171)*P_intPADLN).intValue();
        //int L_intPADLN = P_intPADLN;
        if (P_chrPRPRT == 'P')
        {
			if(rdbPREPN.isSelected())
				return(crtLINE1(L_intPADLN," "));
        }
		try
		{
			String L_STRSP = " ";
			//P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			if(L_intPADLN <= L_STRLN)
			{
				//P_strSTRVL = P_strSTRVL.substring(0,L_intPADLN).trim();
				P_strSTRVL = P_strSTRVL.substring(0,L_intPADLN);
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			int L_STRDF = L_intPADLN - L_STRLN;
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"padSTR1");
		}
		return P_strTRNVL;
	}
	
	
	private void prnFOOTR(boolean P_flgPREPN)
	{
		try
		{
			prnGRPTOT("TOT",dblTOALLQT,intTOALLPK,dblTOALLVL,P_flgPREPN);
			prnGRPTOT("TOT1",dblTOALLQT,intTOALLPK,dblTOALLVL,P_flgPREPN);
			int j = vtrMNHDR.size();
			String[] strMNHARR = new String[2];

			for(int i = 0; i < j+1; i++)
			{
				strMNHARR = (String[]) vtrMNHDR1.elementAt(i);
				dosREPORT.writeBytes(strLFTMRG);
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);
				dosREPORT.writeBytes(padSTR1('R'," "+strMNHARR[0],20,"120",'A'));
				prnFMTCHR(dosREPORT,M_strCPI17);
				dosREPORT.writeBytes(padSTR1('R',strMNHARR[1],58,"171",'A')       +strVLINE1+padSTR1('R'," ",13,"171",'A')                                          +strVLINE1+padSTR1('R'," ",13,"171",'A')                     +strVLINE1+padSTR1('R'," ",20,"171",'A')                                             +strVLINE+"\n");
				//dosREPORT.writeBytes(strLFTMRG       +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                       +strVLINE1+padSTR1('R'," ",13,"171",'A')                                          +strVLINE1+padSTR1('R'," ",13,"171",'A')                     +strVLINE1+padSTR1('R'," ",20,"171",'A')                                             +strVLINE+"\n");
				//dosREPORT.writeBytes(vtrMNHDR.elementAt(i).toString());
			}


			dosREPORT.writeBytes(strLFTMRG       +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                       +strVLINE1+padSTR1('R'," ",13,"171",'A')                                          +strVLINE1+padSTR1('R'," ",13,"171",'A')                     +strVLINE1+padSTR1('R'," ",20,"171",'A')                                             +strVLINE);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI12);
			dosREPORT.writeBytes(strVLINE+"\n");
			
			j = vtrPRDDS1.size();
			String[] strPRDARR = new String[6];
			for(int i = 0; i < j; i++)
			{
				strPRDARR = (String[]) vtrPRDDS1.elementAt(i);
				prnFMTCHR(dosREPORT,M_strCPI17);
                dosREPORT.writeBytes(strLFTMRG+"    ");
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);
				dosREPORT.writeBytes(padSTR1('R'," "+strPRDARR[0],24,"100",'A'));

				prnFMTCHR(dosREPORT,M_strCPI17);
				dosREPORT.writeBytes(padSTR1('R',strPRDARR[1],38,"171",'A')             +padSTR1('R',strPRDARR[2],20,"171",'A')+strVLINE1);

				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);
				dosREPORT.writeBytes(padSTR1('L'," "+strPRDARR[3],22,"100",'A')         +strVLINE1+padSTR1('L',strPRDARR[4],18,"100",'A')               +strVLINE+padSTR1('L',strPRDARR[5],18,"100",'A'));
				//prnFMTCHR(dosREPORT,M_strCPI17);
				dosREPORT.writeBytes(strVLINE+"\n");
			}

			////discount
			//dosREPORT.writeBytes(padSTR1('L'," ",20,"100",'A')         +strVLINE1+padSTR1('L',"Discount",20,"100",'A')               +strVLINE+padSTR1('L',setNumberFormat(dblDSCVL,2),20,"100",'A'));
			
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strCPI17);
			prnFMTCHR(dosREPORT,M_strBOLD);
			/*
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                                                                                                        +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R',"Container No.",20,"171",'A')                         +padSTR1('R',"Seal No.",12,"171",'A')      +padSTR1('R',"C.Excise",12,"171",'A')        +padSTR1('R',"Grade",14,"171",'A')         +padSTR1('L',"Qty(MT)",10,"171",'A')   +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R'," ",20,"171",'A')                                     +padSTR1('R',"",12,"171",'A')              +padSTR1('R',"Seal No.",12,"171",'A')        +padSTR1('R',"",14,"171",'A')              +padSTR1('L',"",10,"171",'A')          +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                                                                                                        +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R'," ",20,"171",'A')                                     +padSTR1('R'," ",12,"171",'A')             +padSTR1('R',"C.Excise",12,"171",'A')        +padSTR1('R'," ",14,"171",'A')             +padSTR1('L'," ",10,"171",'A')         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R'," ",20,"171",'A')                                     +padSTR1('R'," ",12,"171",'A')             +padSTR1('R',"Raigad",12,"171",'A')          +padSTR1('R'," ",14,"171",'A')             +padSTR1('L'," ",10,"171",'A')         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                                                                                                        +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			*/
			
			//dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R',"Container No.",20,"171",'A')                         +padSTR1('R',"Seal No.",12,"171",'A')      +padSTR1('R',"C.Excise",12,"171",'A')        +padSTR1('R',"Grade",14,"171",'A')         +padSTR1('L',"Qty(MT)",10,"171",'A')   +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R',"Less Discount",13,"171",'A'));prnFMTCHR(dosREPORT,M_strNOCPI17);prnFMTCHR(dosREPORT,M_strCPI12);dosREPORT.writeBytes(strVLINE1+padSTR1('L',setNumberFormat(dblDSCVL,2),20,"120",'A')           +strVLINE+"\n");

			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                                                                                                        +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R',"Container No.",20,"171",'A')                         +padSTR1('R',"Seal No.",12,"171",'A')      +padSTR1('R',"C.Excise",12,"171",'A')        +padSTR1('R',"Grade",14,"171",'A')         +padSTR1('L',(strPRDTP.equals("07")?"Qty(SQM)":"Qty(MT)"),10,"171",'A')   +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R'," ",20,"171",'A')                                     +padSTR1('R',"",12,"171",'A')              +padSTR1('R',"Seal No.",12,"171",'A')        +padSTR1('R',"",14,"171",'A')              +padSTR1('L',"",10,"171",'A')          +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			if(dblDSCVL==0)
			    dosREPORT.writeBytes(strLFTMRG   +padSTR1('L'," ",78,"171",'A')                                                                                                                                                                                                                                                        +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			else
			{
				prnFMTCHR(dosREPORT,M_strCPI17);
                dosREPORT.writeBytes(strLFTMRG+"    ");
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes(padSTR1('R'," ",24,"100",'A'));

				prnFMTCHR(dosREPORT,M_strCPI17);
				dosREPORT.writeBytes(padSTR1('R',"",38,"171",'A')             +padSTR1('R',"Less Discount:",20,"171",'A')+strVLINE1);

				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);
				dosREPORT.writeBytes(padSTR1('L'," ",22,"100",'A')         +strVLINE1+padSTR1('L',"",18,"100",'A')               +strVLINE+padSTR1('L',setNumberFormat(dblDSCVL,2),15,"100",'A'));
				//prnFMTCHR(dosREPORT,M_strCPI17);
				dosREPORT.writeBytes(strVLINE+"\n");
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
				prnFMTCHR(dosREPORT,M_strBOLD);
			}
						
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R'," ",20,"171",'A')                                     +padSTR1('R'," ",12,"171",'A')             +padSTR1('R',"C.Excise",12,"171",'A')        +padSTR1('R'," ",14,"171",'A')             +padSTR1('L'," ",10,"171",'A')         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",2,"171",'A')            +padSTR1('R'," ",20,"171",'A')                                     +padSTR1('R'," ",12,"171",'A')             +padSTR1('R',"Raigad",12,"171",'A')          +padSTR1('R'," ",14,"171",'A')             +padSTR1('L'," ",10,"171",'A')         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                                                                                                        +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			intLINNO += 7;
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			j = vtrCNTDS1.size();
			
			String[] strARR = new String[5];
			for(int i = 0; i < j; i++)
			{
				strARR = (String[]) vtrCNTDS1.elementAt(i);
				//System.out.println("Array : "+strARR[0]+"/"+strARR[1]+"/"+strARR[2]+"/"+strARR[3]+"/"+strARR[4]);
            	dosREPORT.writeBytes(strLFTMRG          +padSTR1('R'," ",2,"171",'A'));
				prnFMTCHR(dosREPORT,M_strCPI17);
				//prnFMTCHR(dosREPORT,M_NOCPI17);
				//prnFMTCHR(dosREPORT,M_CPI10);
				dosREPORT.writeBytes(padSTR1('R'," "+strARR[0],20,"171",'A')                           +padSTR1('R',strARR[1],12,"171",'A')       +padSTR1('R',strARR[2],12,"171",'A')         +padSTR1('R',strARR[3],14,"171",'A')        +padSTR1('L',strARR[4],10,"171",'A'));
				prnFMTCHR(dosREPORT,M_strCPI17);
				dosREPORT.writeBytes(strVLINE1 +"         " +padSTR1('R'," ",13,"171",'A')            +strVLINE1+padSTR1('R'," ",13,"171",'A')           +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
			}
			
			while(intLINNO < 23)
			{
				dosREPORT.writeBytes(strLFTMRG +padSTR1('R'," ",78,"171",'A')                                                                                                                                                                                                                                     +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",13,"171",'A')                         +strVLINE1+padSTR1('R'," ",20,"171",'A')           +strVLINE+"\n");
				intLINNO += 1;
			}
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',getLINE(130),130,"171",'P')                                                                                                                 +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R'," Amount Chargeable (in words) ",86,"171",'P')+padSTR1('L',"Total : ",14,"171",'P')+strVLINE1);

			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI12);
			//dosREPORT.writeBytes(padSTR1('L'," "+setFMT(String.valueOf(dblTOALLVL),2),22,"100",'A')   +strVLINE+"\n");
			dosREPORT.writeBytes(padSTR1('L'," "+setNumberFormat(dblTOALLVL-dblDSCVL,2),24,"100",'A')   +strVLINE+"\n");
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strCPI17);
			
			//dosREPORT.writeBytes(strLFTMRG         +padSTR1('R'," "+strPMTTM+" "+strCURDS+" "+getCURWRD(setFMT(String.valueOf(dblTOALLVL),2),strDENOM),106,"171",'A') +strVLINE + padSTR1('R',getLINE(23),23,"171",'P')     +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R'," "+strPMTTM+" "+strCURDS+" "+getCURWRD(setNumberFormat(dblTOALLVL-dblDSCVL,2).toString(),strDENOM),106,"171",'A') +strVLINE + padSTR1('R',getLINE(23),23,"171",'P')     +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('L'," ",130,"171",'P')+strVLINE+"\n");
				 
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('L'," ",130,"171",'P')+strVLINE+"\n");
			//dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  EPCG LIC NO.  : ",20,"171",'A') +padSTR1('R',cl_dat.getPRMCOD("cmt_codds","MST","MRXXOTH","PI_ECGN"),110,"171",'A')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  EPCG LICENCE NO. 033008580/2/11/00 DT. 09.05.2005",110,"171",'A') +padSTR1('R'," ",20,"171",'A')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  ADV. LIC NO.  : ",20,"171",'A') +padSTR1('R',strADLNO+"   DT."+strADLDT,110,"171",'A')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  I.E. CODE NO. : ",20,"171",'A') +padSTR1('R',cl_dat.getPRMCOD("cmt_codds","MST","MRXXOTH","PI_IENO"),110,"171",'A')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  RBI CODE NO.  : ",20,"171",'A') +padSTR1('R',cl_dat.getPRMCOD("cmt_codds","MST","MRXXOTH","PI_RBIN"),110,"171",'A')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('L'," ",130,"171",'P')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('L'," ",130,"171",'P')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('L'," ",130,"171",'P')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R'," ",84,"171",'P')+padSTR1('R',getLINE(46),46,"171",'P')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R'," ",84,"171",'P')+strVLINE1+padSTR1('L',"For "+cl_dat.M_strCMPNM_pbst,44,"171",'P')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  Declaration :",84,"171",'P')                                                                                                                                                   +strVLINE1+padSTR1('R',getLINE(44),44,"171",'P')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  We declare that this invoice shows the actual price of the goods ",84,"171",'P')                                                                                               +strVLINE1+padSTR1('R'," ",44,"171",'P')+strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',"  described and that all particulars are true and correct ",84,"171",'P')                                                                                                        +strVLINE1+padSTR1('R',"Date :"+cl_dat.M_strLOGDT_pbst,20,"171",'A')+padSTR1('L',"Authorised Signatory",24,"171",'P')            +strVLINE+"\n");
			dosREPORT.writeBytes(strLFTMRG         +padSTR1('R',getLINE(130),130,"171",'P')                                                                                                                                                                                                                                                                                       +strVLINE+"\n");
			dosREPORT.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
	
		
	private String getRSTVAL(ResultSet P_rstRSSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
			if (LP_FLDTP.equals("C"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? P_rstRSSET.getString(LP_FLDNM).toString() : "";
			//return P_rstRSSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString()," ")) : "";
			else if (LP_FLDTP.equals("N"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString(),"0") : "0";
			else if (LP_FLDTP.equals("D"))
				return P_rstRSSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(P_rstRSSET.getDate(LP_FLDNM)) : "";
			else if (LP_FLDTP.equals("T"))
				return P_rstRSSET.getTimestamp(LP_FLDNM) != null ? M_fmtLCDTM.format(P_rstRSSET.getTimestamp(LP_FLDNM)) : "";
			 //   return M_fmtLCDTM.parse(P_rstRSSET.getString(LP_FLDNM)));
			else 
				return " ";
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 

	
	public String getCURWRD(String P_strCURVL,String P_strDENOM){
      	//String to be printed
		String L_strPRNSTR = "";
		try
		{
			//Remainder of divison
			int L_REMVL = 0;
			//Quotient of divison
			int L_QUOVL = 0;
			//Getting rupees
			String L_RPSVL = P_strCURVL.substring(0,P_strCURVL.indexOf(".")).trim();
			//Converting rupees in string to integer
			int L_RUPVL = Integer.parseInt(L_RPSVL);
			//Getting paise
			String L_PAIVL = P_strCURVL.substring(P_strCURVL.lastIndexOf(".")+1,P_strCURVL.length()).trim();
			//Taking length of rupees value
			int L_ABSVL = L_RPSVL.length();
            if(L_ABSVL > 0)
			{
				//Ones
				if(L_ABSVL == 1)
				{
                    L_strPRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Tens
				else if(L_ABSVL == 2)
				{
                    L_strPRNSTR += chkZERO(hst1to100.get(L_RPSVL).toString(),"",' ');
				}
				//Hundreds
				else if(L_ABSVL == 3)
				{
					L_QUOVL = L_RUPVL/100;
					L_REMVL = L_RUPVL%100;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
				//Thousands
				else if(L_ABSVL == 4 || L_ABSVL == 5)
				{
					L_QUOVL = L_RUPVL/1000;
					L_REMVL = L_RUPVL%1000;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
                 	L_QUOVL = L_REMVL/100;
					L_REMVL = L_REMVL%100;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
                               
				}
				//Lacs
				else if(L_ABSVL == 6 || L_ABSVL == 7)
				{
					L_QUOVL = L_RUPVL/100000; //for getting lac value
					L_REMVL = L_RUPVL%100000;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Lac",'B');
					L_QUOVL = L_REMVL/1000;  //for getting thousand value
					L_REMVL = L_REMVL%1000;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
					L_QUOVL = L_REMVL/100;  //for getting hundred value
					L_REMVL = L_REMVL%100;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
				//Crores
				else if(L_ABSVL == 8 || L_ABSVL == 9)
				{
					L_QUOVL = L_RUPVL/10000000; //for getting crore value
					L_REMVL = L_RUPVL%10000000;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Crore",'B');
					L_QUOVL = L_REMVL/100000;  //for getting lac value
					L_REMVL = L_REMVL%100000;
                    L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Lac",'B');
					L_QUOVL = L_REMVL/1000;  //for getting thousand value
					L_REMVL = L_REMVL%1000;
                                        L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Thousand",'B');
					L_QUOVL = L_REMVL/100;  //for getting hundred value
					L_REMVL = L_REMVL%100;
                                        L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_QUOVL)).toString(),"Hundred",'B');
                                        L_strPRNSTR += chkZERO(hst1to100.get(String.valueOf(L_REMVL)).toString(),"and",'F');
				}
			}
			if(L_PAIVL.length() > 0 && !L_PAIVL.equals("00")){
				if(L_PAIVL.substring(0,1).equals("0"))
					L_PAIVL =L_PAIVL.substring(1);
                                L_strPRNSTR += chkZERO(hst1to100.get(L_PAIVL).toString(),"and "+P_strDENOM,'F');
			}
			L_strPRNSTR += "Only";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCURWRD");
		}
		return L_strPRNSTR;
	}

	
	
	
	/**
	 * This method gives currency details for the entered currency code
	 */
	private void getCURDTL()
	{
		try
		{
			ResultSet L_rstRSSET = null;
			String L_strSQLQRY = "Select cmt_codds,cmt_shrds,cmt_chp01 from co_cdtrn where cmt_cgmtp='MST'";
			L_strSQLQRY += " and cmt_cgstp='COXXCUR' and cmt_codcd='"+strCURCD+"'";
			L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			if(L_rstRSSET.next())
			{
				strCURDS = nvlSTRVL(L_rstRSSET.getString("cmt_codds"),"");
				strDENOM = nvlSTRVL(L_rstRSSET.getString("cmt_chp01"),"");
				strCSHDS = nvlSTRVL(L_rstRSSET.getString("cmt_shrds"),"");
			}
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCURDTL");
		}
	}
	
	/**
	 *
	 * Method to return lines that are used in the Reports
	*/
	
	private String getLINE(int LP_CNT)
	{
		String L_strLINCH = "";
		try
		{
			for(int i=1;i<=LP_CNT;i++)
			{
				L_strLINCH += "-";
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error in getLINE");
		}
		return L_strLINCH;
	}
	
	private String chkZERO(String LP_STRVL,String LP_RPSVL,char LP_PLCSTR)
	{
		String L_RTNSTR = "";
		try
		{
			if(String.valueOf(LP_STRVL).equals(null) || LP_STRVL.equalsIgnoreCase("zero"))
			{
				return "";
			}
			else
			{
				if(LP_PLCSTR == 'F')
				{
					L_RTNSTR = LP_RPSVL + " " +  LP_STRVL + " ";
				}
				else if(LP_PLCSTR == 'B')
				{
					L_RTNSTR = LP_STRVL + " " + LP_RPSVL + " ";
				}
			}
		}
		catch(Exception L_EX)
		{
          	setMSG(L_EX,"chkZERO");
		}
		return L_RTNSTR;
	}
	
	public String getPRMCOD(String LP_FLDRTN, String LP_STRMGP, String LP_STRSGP, String LP_STRCOD)
	{
		String L_strTEMP="";
		ResultSet L_rstRSSET;
		M_strSQLQRY ="select " + LP_FLDRTN ;
		M_strSQLQRY +=" from CO_CDTRN where CMT_CGMTP = "+ "'" + LP_STRMGP.trim() +"'" ;
		M_strSQLQRY +=" AND CMT_CGSTP = " + "'" + LP_STRSGP.trim() + "'";
		M_strSQLQRY +=" AND CMT_CODCD = " + "'" + LP_STRCOD.trim() + "'";
		try
		{
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET.next())
				L_strTEMP = L_rstRSSET.getString(1);
			L_rstRSSET.close();
		}
		catch(Exception L_SE)
		{
			//System.out.println("Error in getPRMCOD : "+L_SE.toString()); 
			setMSG(L_SE,"getPRMCOD");
		}
	return L_strTEMP;
	}
	
	
	
	
		/**
	 */
	public void prnCERREC_OLD(String P_strBYRCD, String P_strPINNO)
	{
		try
		{
			//LM_RESSTR = LM_RESFIN.trim().concat("\\mr_tppin1.doc"); 
			strFILNM=cl_dat.M_strREPSTR_pbst.concat("\\mr_tppin1.doc"); 
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			//fosREPORT = crtFILE(LM_RESSTR);
			//dosREPORT = crtDTOUTSTR(fosREPORT);
			strBYRCD = P_strBYRCD;
			strPINNO = P_strPINNO;
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strCPI17);
			vtrCNTNO.clear();
			vtrTSLNO.clear();
			vtrCSLNO.clear();
			vtrLOTDTL.clear();
			dblDBSVL = 0;
			crtCNTDS();
			strCNTNO_OLD = "";
			
			
			M_strSQLQRY = "Select distinct pi_mkttp,pi_pinno,pi_pindt,pi_curcd,pi_adlno,";
			M_strSQLQRY += "pi_fspno,pit_cntds,pit_tslno,isnull(pit_cslno,' ') pit_cslno,pit_prdqt,pit_prdpk,pit_prdvl,pit_cnttp,";
			M_strSQLQRY += "pi_othrf,pit_ladno,pi_adldt from mr_pitrn,mr_pimst where";
            M_strSQLQRY += " pit_cmpcd=pi_cmpcd and pit_mkttp=pi_mkttp and pit_pinno=pi_pinno and";
			M_strSQLQRY += " PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pi_byrcd='"+strBYRCD+"' and pi_pinno='"+strPINNO+"' order by pit_ladno";
			//System.out.println(M_strSQLQRY);
			boolean L_STSFL = true;
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				strMKTTP = nvlSTRVL(M_rstRSSET.getString("pi_mkttp").toString(),"").trim();	
				strPINNO = nvlSTRVL(M_rstRSSET.getString("pi_pinno").toString(),"").trim();	
				strPINDT = nvlSTRVL(getRSTVAL(M_rstRSSET,"pi_pindt","D"),"").trim();	
				strADLDT = nvlSTRVL(getRSTVAL(M_rstRSSET,"pi_adldt","D"),"").trim();	
				//strPINDT = cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("pi_pindt"));	
				//strADLDT = cc_dattm.occ_dattm.setDATE("DMY",M_rstRSSET.getDate("pi_adldt"));	
				strCURCD = nvlSTRVL(M_rstRSSET.getString("pi_curcd").toString(),"").trim();	
				strCURDS = cl_dat.getPRMCOD("cmt_codds","MST","COXXCUR",strCURCD);
				strADLNO = nvlSTRVL(M_rstRSSET.getString("pi_adlno").toString(),"").trim();	
				strFSPNO = nvlSTRVL(M_rstRSSET.getString("pi_fspno").toString(),"").trim();	
				strOTHRF = nvlSTRVL(M_rstRSSET.getString("pi_othrf").toString(),"").trim();	
				strLADNO = nvlSTRVL(M_rstRSSET.getString("pit_ladno").toString(),"").trim();	
				strCNTNO = nvlSTRVL(M_rstRSSET.getString("pit_cntds").toString(),"").trim();	
				strTSLNO = nvlSTRVL(M_rstRSSET.getString("pit_tslno").toString(),"").trim();	
				strCSLNO = nvlSTRVL(M_rstRSSET.getString("pit_cslno").toString(),"").trim();	
				intDBSPK = M_rstRSSET.getInt("pit_prdpk");
				//System.out.println("intDBSPK : "+intDBSPK);
				dblDBSVL += M_rstRSSET.getDouble("pit_prdvl");	
						
				getLOTNO();
				boolean L_EXSFL = false;
				for(int i=0;i<vtrCNTNO.size();i++)
				{
					if(vtrCNTNO.elementAt(i).toString().equals(strCNTNO))
					L_EXSFL=true;
					break;
				}
				if(!L_EXSFL)
					{vtrCNTNO.addElement(strCNTNO);	 vtrTSLNO.addElement(strTSLNO); vtrCSLNO.addElement(strCSLNO);}
			}
			vtrLOTDTL.addElement("\n");
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			prnHEADER_OLD();
			for(int i = 0; i < vtrLOTDTL.size(); i++) 
				prnMNLREC(vtrLOTDTL.elementAt(i).toString(),130,15,' ');
			prnFOOTR_OLD();
			prnFMTCHR(dosREPORT,M_strEJT);
			dosREPORT.close();
			fosREPORT.close();
			setMSG(" ",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnCERREC_OLD");
		}
	}

	
	
	/**
	 */
	public void prnCERREC_NEW(String P_strBYRCD, String P_strPINNO)
	{
		try
		{
			//LM_RESSTR = LM_RESFIN.trim().concat("\\mr_tppin1.doc"); 
			strFILNM=cl_dat.M_strREPSTR_pbst.concat("\\mr_tppin1.doc"); 
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			//fosREPORT = crtFILE(LM_RESSTR);
			//dosREPORT = crtDTOUTSTR(fosREPORT);
			strBYRCD = P_strBYRCD;
			strPINNO = P_strPINNO;
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strCPI17);
			vtrCNTNO.clear();
			vtrTSLNO.clear();
			vtrCSLNO.clear();
			vtrLOTDTL.clear();
			dblDBSVL = 0;
			crtCNTDS();
			strCNTNO_OLD = "";
			
			
			M_strSQLQRY = "Select distinct pi_mkttp,pi_pinno,pi_byrcd,pi_pindt,pi_adlno,pi_fspno,pi_othrf,pi_adldt,sum(pit_prdpk) pit_prdpk from mr_pimst,mr_pitrn where";
            M_strSQLQRY += " pi_cmpcd=pit_cmpcd and pi_mkttp=pit_mkttp and pi_pinno = pit_pinno and PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pi_byrcd='"+strBYRCD+"' and pi_pinno='"+strPINNO+"' group by pi_mkttp,pi_pinno,pi_byrcd,pi_pindt,pi_adlno,pi_fspno,pi_othrf,pi_adldt";
			System.out.println(M_strSQLQRY);
			boolean L_STSFL = true;
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			intTOTPK = 0;
			while(M_rstRSSET.next())
			{
				strPINNO = nvlSTRVL(M_rstRSSET.getString("pi_pinno").toString(),"").trim();	
				strPINDT = nvlSTRVL(getRSTVAL(M_rstRSSET,"pi_pindt","D"),"").trim();	
				strADLDT = nvlSTRVL(getRSTVAL(M_rstRSSET,"pi_adldt","D"),"").trim();	
				strBYRCD = nvlSTRVL(M_rstRSSET.getString("pi_byrcd").toString(),"").trim();	
				strADLNO = nvlSTRVL(M_rstRSSET.getString("pi_adlno").toString(),"").trim();	
				strFSPNO = nvlSTRVL(M_rstRSSET.getString("pi_fspno").toString(),"").trim();	
				strOTHRF = nvlSTRVL(M_rstRSSET.getString("pi_othrf").toString(),"").trim();	
				intTOTPK = M_rstRSSET.getInt("pit_prdpk");	
				//System.out.println("intDBSPK : "+intDBSPK);
				//dblDBSVL += M_rstRSSET.getDouble("pit_prdvl");	
						
			}
			getBYRDTL();
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			prnHEADER_NEW();
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"---------------------------------------------------------------------------------------------------------------",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"Container no.	 size	    ------ Seal no -------------                  No.of pkgs stuffed in container	   ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"		                    Seal No.	C.excise seal no.                 ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                        Central Excise Raigad.            ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"---------------------------------------------------------------------------------------------------------------",130,"171",'A')+"\n");
			M_strSQLQRY = "Select pit_cntds,pit_tslno,isnull(pit_cslno,' ') pit_cslno,pit_cnttp,sum(pit_prdqt) pit_prdqt,sum(pit_prdpk) pit_prdpk  from mr_pitrn where  PIT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pit_pinno = '"+strPINNO+"' group by pit_cntds,pit_tslno,isnull(pit_cslno,' '),pit_cnttp order by pit_cntds ";
			System.out.println(M_strSQLQRY);
			//boolean L_STSFL = true;
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				strCNTNO = nvlSTRVL(M_rstRSSET.getString("pit_cntds").toString(),"").trim();	
				strTSLNO = nvlSTRVL(M_rstRSSET.getString("pit_tslno").toString(),"").trim();	
				strCSLNO = nvlSTRVL(M_rstRSSET.getString("pit_cslno").toString(),"").trim();	
				//strPRDQT = nvlSTRVL(M_rstRSSET.getString("pit_prdqt").toString(),"").trim();	
				strCNTTP = nvlSTRVL(M_rstRSSET.getString("pit_cnttp").toString(),"").trim();
				String L_strCNTTP = strCNTTP.equals("01") ? "20 Ft." : "40 Ft.";
				String L_strPRDPK = nvlSTRVL(M_rstRSSET.getString("pit_prdpk").toString(),"").trim();	
				dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',strCNTNO+"        "+L_strCNTTP+"      "+strTSLNO+"         "+strCSLNO+"                      "+L_strPRDPK,130,"171",'A')+"\n");
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			
			
			
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"---------------------------------------------------------------------------------------------------------------",130,"171",'A')+"\n");
			prnFOOTR_NEW();
			prnFMTCHR(dosREPORT,M_strEJT);
			dosREPORT.close();
			fosREPORT.close();
			setMSG(" ",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnCERREC_NEW");
		}
	}

	
	/**
	 * 
	 * 









 


	 */
	
	
	private void prnHEADER_NEW()
	{
		try
		{
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI12);
			dosREPORT.writeBytes(padSTR1('R'," ",15,"120",'A')+padSTR1('C',"ANNEXURE C1",130,"120",'A')+"\n\n");
			
			dosREPORT.writeBytes(padSTR1('R'," ",15,"120",'A')+padSTR1('C',"OFFICE OF THE SUPERINTENDENT OF CENTRAL EXCISE",130,"120",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"120",'A')+padSTR1('C',"RANGE NAGOTHANE - I,DIVISION	: ALIBAG,COMMISSIONERATE : RAIGAD.",130,"120",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"120",'A')+padSTR1('C',"C.NO.__________  Date___________Shipping bill no.________________Date____________",130,"120",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"120",'A')+padSTR1('C',"EXAMINATION REPORT FOR FACTORY SEALED PACKAGES/CONTAINER",130,"120",'A')+"\n\n");
			prnFMTCHR(dosREPORT,M_strCPI17);
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"1.  Name of Exporter                                            :   Supreme Petrochem Ltd.,",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"2.  a) IEC No.                                                  :   0392048094.",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    b) Branch Code                                              :   ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    c) BIN (PAN based business identification number            :   AAACS7249CFT001",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"       of the exporter)                                             ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"3.  Name of the manufacturer (if different from the Exporter    :   N.A.",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"4.  Factory address                                             :   Village Amdoshi & Wangani, P.O.Patansai, Nagothane,",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                                                    Tal : Roha,Dist Raigad 402106 MAHARASHTRA.",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"5.  Date of examination                                         :   "+strPINDT,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"6.  Name & designation of the examining officer-                :   ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    Inspector/EO/PO                                                 ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"7.  Name & designation of the supervising officer                   ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    Appraiser/Superintendent                                    :   ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"8.  (a) Name of Commissionerate/Division/Range                  :   Raigad/Alibag/Nagothane -I",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    (b) Location code **                                        :   430502",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"9.  Particulars of export invoice                                   ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    (a) Export Invoice no.                                      :   "+strPINNO,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    (b) Total no. of packages                                   :   "+setNumberFormat(intTOTPK,0),130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    (c) Name & address of the consignee abroad                  :   "+strBYRNM,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                                                    "+strBYAD1,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                                                    "+strBYAD2,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                                                    "+strBYAD3,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                                                    "+strBYAD4,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                                                    "+strBYPIN,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"10. (a) Is the description of the goods , the quantity & their  :   YES",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"        value as per particulars furnished in the Export Invoice ?    ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    (b) Whether sample is drawn for being forwarded to          :   NO",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"        Port of export.                                             ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"    (c) If yes, the number of the seal of the package           :   ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"        Containing the sample                                       ",130,"171",'A')+"\n\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"11.  Central Excise/Customs seal nos.                               ",130,"171",'A')+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER_NEW");
		}
	}
	

	/**
	 */
	private void prnFOOTR_NEW()
	{
		try
		{
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," ",130,"171",'A')+"\n");
			//dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"This export is against Advance Licence Application JDGFT,Mumbai File no. "+strADLNO+"  dt. "+strADLDT,130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"FSP No. "+strFSPNO,130,"171",'A')+"\n\n\n\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"Signature of Exporter                       Signature of Inspector/                         Signature of Appraiser/Superintendent",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"                                            Examiner",130,"171",'A')+"\n");

			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR_NEW");
		}
	}
	
	
	
	/**
	 */
	private void prnHEADER_OLD()
	{
		try
		{
			dosREPORT.writeBytes(strLFTMRG   +padSTR1('C',"C E R T I F I C A T E",130,"171",'A'));
			dosREPORT.writeBytes("\n"+"\n");
			
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"INSPECTED CONTAINERS, VERIFIED EMPTY CARGO  EXAMINED IN EXPORTERS  PREMISES STUFFED AT AMDOSHI, NAGOTHANE. TAL.ROHA, DIST.RAIGAD.",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," INSPECTED LOT,  CHECKED  MARKS & NUMBER, EXAMINED  AFTER SELECTION. CHECKED  DESCRIPTION, QUANTITY, WEIGHT, VALUE AND TECHNICAL ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," CHARACTERISTICS AS PER DECLARED GRADE, WEIGHT OF EXPORT PRODUCT AS PER",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," INVOICE NO."+strPINNO+"   DT."+strPINDT+"  VALUE "+strCURDS+" "+setNumberFormat(dblDBSVL,2),130,"171",'A')+"\n");
			//dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," INPUTS RECEIVABLE UNDER ADV.LIC DECLARED WILL BE ACTUALLY USED FOR MANUFACTURER OF THE EXPORT PRODUCT. THERE IS NO EXEMPTION IN ",130,"171",'A')+"\n");
			//dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," THE I/O NORMS FOR PACKING MATERIAL  ",130,"171",'A')+"\n");
			dosREPORT.writeBytes("\n\n");

			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," SUPREME PETROCHEM LTD  has been Recertified to   ISO 9001-2000  Standard by  BUREAU VERITAS QUALITY INTERNATIONAL INC. UK  and ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"  Accrediation by UKAS Quality Management. The Certification is valid till 13th April 2008 ",130,"171",'A')+"\n");
			//dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"",130,"171",'A')+"\n");
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R'," WE ALSO  CERTIFIED THAT  THEY HAVE AVAILED CENVAT BENEFIT  UNDER CENVAT CREDIT RULES,  2004  AND  NOT AVAILED  FACILITY  UNDER ",130,"171",'A')+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"  RULE 18 AND 19(2) OF THE CENTRAL EXCISE RULES, 2002 IN RESPECT OF THE EXPORTED GOODS. ",130,"171",'A')+"\n");
			dosREPORT.writeBytes("\n");
	
			String L_strDUMST = " THIS EXPORT IS AGAINST ADVANCE LICENCE APPLICATION JDGFT, MUMBAI ";
			prnMNLREC(L_strDUMST,130,15,' ');
			
			L_strDUMST = "FILE NO. "+strADLNO+"   DT."+strADLDT+ "  FSP NO."+ strFSPNO;
			prnMNLREC(L_strDUMST,130,15,'B');
			dosREPORT.writeBytes("\n");
			
			L_strDUMST = " INSPECTED BAGS PERTAINING LOT NOS. PARTICULARS OF EXAMINATION ARE AS UNDER :";
			prnMNLREC(L_strDUMST,130,15,' ');
			dosREPORT.writeBytes("\n");
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTR1('R'," ",15,"171",'A')+padSTR1('R',"Lot No.",20,"171",'A')+padSTR1('L',"No. of Bags",22,"171",'A')+padSTR1('L',"Accertained",20,"171",'A')+padSTR1('L',"Declared",20,"171",'A')+padSTR1('L',"Variation",20,"171",'A'));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTR1('R'," ",35,"171",'A')+padSTR1('L',"OPD & EXMD",22,"171",'A')+padSTR1('L',"Gr.Wt.",20,"171",'A')+padSTR1('L',"Gr.Wt.",20,"171",'A'));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}

	
	/**
	 */
	private void getLOTNO()
	{
		try
		{
		//	System.out.println("strCNTNO"+strCNTNO);
		//	System.out.println("strCNTNO_OLD"+strCNTNO_OLD);
			
			if(strCNTNO.equals(strCNTNO_OLD))
				return;
			ResultSet L_rstRSSET;
			String L_strLOTST = "";
			String L_strDUMST = "";
			boolean L_EOF = true;
			int L_CNT = 0;
			M_strSQLQRY = "Select distinct ist_lotno,ist_pkgtp from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_issno='"+strLADNO+"'";
            M_strSQLQRY += " and ist_stsfl='2' and ist_saltp='12'";
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);			
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			L_strDUMST += padSTR1('L'," ",21,"171",'A');
			if(L_rstRSSET.next())
			{
				while(L_EOF)
				{
					strPKGTP = nvlSTRVL(L_rstRSSET.getString("ist_pkgtp"),"");
					//System.out.println(" In While strPKGTP = "+strPKGTP);			
					if(L_CNT < 2)
					{
						L_strLOTST += padSTR1('R',nvlSTRVL(L_rstRSSET.getString("ist_lotno"),""),10,"171",'A')+" ";
						L_CNT++;
					}
					if(!L_rstRSSET.next())
					{
						L_EOF = false;
						break;
					}
				}
				L_strDUMST += L_strLOTST;
				if(L_CNT == 1)
					L_strDUMST += padSTR1('R'," ",11,"171",'A');
				//double L_BAGWT = Double.parseDouble(cl_dat.getPRMCOD("cmt_chp01","SYS","FGXXPKG",strPKGTP));
				//System.out.println("strPKGTP : "+strPKGTP);
				double L_BAGWT = Double.parseDouble(getPRMCOD("cmt_chp01","SYS","FGXXPKG",strPKGTP));
				//System.out.println("L_BAGWT : "+L_BAGWT);

				//System.out.println("getCNTDS(strCNTNO) : "+getCNTDS(strCNTNO,"PIT_PRDPK"));
				dblEXMPK = Double.parseDouble(getCNTDS(strCNTNO,"PIT_PRDPK")) * 0.1;
				//System.out.println("dblEXMPK : "+dblEXMPK);
				if(dblEXMPK >= 40)
					dblEXMPK = 40;
				strCNTNO_OLD = strCNTNO;
				
				double L_EXMPK = (intDBSPK > dblEXMPK) ? dblEXMPK : intDBSPK;
				
				//String L_AGRWT = setFMT(String.valueOf(L_EXMPK * L_BAGWT),2);
				String L_AGRWT = setNumberFormat((L_EXMPK * L_BAGWT),2);
				String L_DGRWT = L_AGRWT;
				//L_strDUMST += padSTR1('L',setFMT(String.valueOf(L_EXMPK),0),20,"171",'A') + padSTR1('L',L_AGRWT,20,"171",'A')+padSTR1('L',L_DGRWT,20,"171",'A')+padSTR1('L',"Nil",20,"171",'A')+"\n";
				L_strDUMST += padSTR1('L',setNumberFormat(L_EXMPK,0),20,"171",'A') + padSTR1('L',L_AGRWT,20,"171",'A')+padSTR1('L',L_DGRWT,20,"171",'A')+padSTR1('L',"Nil",20,"171",'A')+"\n";
				vtrLOTDTL.addElement(L_strDUMST);
			}
			else
				vtrLOTDTL.addElement("\n");
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLOTNO");
		}
	}
	
	/**
	 */
	private void prnFOOTR_OLD()
	{
		try
		{
			dosREPORT.writeBytes("\n");
			String L_strDUMST = "THE ABOVE MATERIAL STUFFED IN THE ";
			prnMNLREC(L_strDUMST,130,15,' ');
			L_strDUMST = "CONTAINER NO. :";
			int i = 1;
			for(int j = 0;j < vtrCNTNO.size();j++)
			{
				L_strDUMST += "("+i+")"+vtrCNTNO.elementAt(j).toString().trim()+", ";
				i++;
			}
			prnMNLREC(L_strDUMST,130,15,'B');
			L_strDUMST = "SEAL NO. :";
			i = 1;
			for(int j = 0;j < vtrTSLNO.size();j++)
			{
				L_strDUMST += "("+i+")"+vtrTSLNO.elementAt(j).toString().trim()+", ";
				i++;
			}
			prnMNLREC(L_strDUMST,130,15,'B');
			L_strDUMST = "C.Excise Seal No.:   Central Excise Raigad.";
			i = 1;
			for(int j = 0;j < vtrCSLNO.size();j++)
			{
				L_strDUMST += "("+i+")"+vtrCSLNO.elementAt(j).toString().trim()+", ";
				i++;
			}
			prnMNLREC(L_strDUMST,130,15,'B');
			L_strDUMST = "IN A MANNER SO THAT CONTENT THEREOF";
			L_strDUMST += " CANNOT BE TAMPERED WITH WITHOUT BREAKING THE SEAL VIDE ";
			prnMNLREC(L_strDUMST,130,15,' ');
			prnMNLREC(strOTHRF+"   DT."+cl_dat.M_strLOGDT_pbst,130,15,'B');
			dosREPORT.writeBytes("\n"+"\n"+"\n"+"\n"+"\n"+"\n");
			dosREPORT.writeBytes(padSTR1('R'," ",20,"171",'A')+padSTR1('C',"SUPERINTENDENT OF CENTRAL EXCISE",50,"171",'A')+padSTR1('R'," ",20,"171",'A')+padSTR1('C',"INSPECTOR OF CENTRAL EXCISE",50,"171",'A'));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTR1('R'," ",20,"171",'A')+padSTR1('C',"RANGE NAGOTHANE - I",50,"171",'A')+padSTR1('R'," ",20,"171",'A')+padSTR1('C',"RANGE NAGOTHANE - I",50,"171",'A'));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR_OLD");
		}
	}

	
	
	
	private void prnMNLREC(String LP_STRING,int LP_ENDLMT,int LP_STRSPC,char LP_STRFMT)
	{
		try
		{
			int L_STRLMT = 0;
			if(LP_STRFMT == 'B')
				prnFMTCHR(dosREPORT,M_strBOLD);
			if((LP_STRING.length()+LP_STRSPC) > LP_ENDLMT)
			{
				do
				{
					dosREPORT.writeBytes(padSTR1('R'," ",LP_STRSPC,"171",'A')+padSTR1('R',LP_STRING.substring(L_STRLMT,LP_STRING.length()).trim(),LP_ENDLMT,"171",'A'));
					dosREPORT.writeBytes("\n");
					L_STRLMT += LP_ENDLMT;
				}
				while(LP_STRING.length() >= L_STRLMT);
			}
			else
			{
				dosREPORT.writeBytes(padSTR1('R'," ",LP_STRSPC,"171",'A')+padSTR1('R',LP_STRING,LP_ENDLMT,"171",'A'));
				dosREPORT.writeBytes("\n");
			}
			if(LP_STRFMT == 'B')
				prnFMTCHR(dosREPORT,M_strNOBOLD);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnMNLREC");
		}
	}


	
	/**
	 */
	private void prnMNLREC1(String LP_STRING,int LP_ENDLMT,int LP_STRSPC,char LP_STRFMT)
	{
		try
		{
			int L_STRLMT = 0;
			if(LP_STRFMT == 'B')
				prnFMTCHR(dosREPORT,M_strBOLD);
			if((LP_STRING.length()+LP_STRSPC) > LP_ENDLMT)
			{
				do
				{
					dosREPORT.writeBytes(padSTR1('R'," ",LP_STRSPC,"171",'A')+padSTR1('R',LP_STRING.substring(L_STRLMT,LP_STRING.length()).trim(),LP_ENDLMT,"171",'A'));
					//dosREPORT.writeBytes("\n");
					L_STRLMT += LP_ENDLMT;
				}
				while(LP_STRING.length() >= L_STRLMT);
			}
			else
			{
				dosREPORT.writeBytes(padSTR1('R'," ",LP_STRSPC,"171",'A')+padSTR1('R',LP_STRING,LP_ENDLMT,"171",'A'));
				//dosREPORT.writeBytes("\n");
			}
			if(LP_STRFMT == 'B')
				prnFMTCHR(dosREPORT,M_strNOBOLD);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnMNLREC");
		}
	}
	
	
	
	
	private void crtCNTDS()
	{
		String L_strSQLQRY = "";
		try
		{
			hstCNTDS.clear();
		    L_strSQLQRY = "select pit_cntds, sum(pit_prdpk) pit_prdpk from mr_pitrn  where  PIT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pit_pinno='"+strPINNO+"' group by pit_cntds";
			//System.out.println(L_strSQLQRY);
		    ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
		    if(L_rstRSSET == null || !L_rstRSSET.next())
		    {
				setMSG("Records not found in MR_PITRN",'E');
				return;
		    }
		    while(true)
		    {
				strPIT_CNTDS = getRSTVAL(L_rstRSSET,"PIT_CNTDS","C");
				String[] staCNTDS = new String[intCNTDS_TOT];
		        staCNTDS[intAE_PIT_PRDPK] = getRSTVAL(L_rstRSSET,"PIT_PRDPK","N");
				//System.out.println("hstCNTDS.put  : "+strPIT_CNTDS+" / "+getRSTVAL(L_rstRSSET,"PIT_PRDPK","N"));
		        hstCNTDS.put(strPIT_CNTDS,staCNTDS);
				
		        if (!L_rstRSSET.next())
		             break;
			}
		    L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtCNTDS");
		}
		return;
	}
	
	/** Picking up No.of pkgs for container
	*/
	private String getCNTDS(String LP_CNTDS, String LP_FLDNM)
	{
		String L_RETSTR = "0.00";
		try
		{
			if(!hstCNTDS.containsKey(LP_CNTDS))
				return L_RETSTR;
	        String[] staCNTDS = (String[])hstCNTDS.get(LP_CNTDS);
	        if (LP_FLDNM.equals("PIT_PRDPK"))
				L_RETSTR = staCNTDS[intAE_PIT_PRDPK];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCNTDS");
		}
		return L_RETSTR;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void crtHST()
	{
		hst1to100 = new Hashtable<String,String>();
		hst1to100.put("0","zero");hst1to100.put("1","One");hst1to100.put("2","Two");hst1to100.put("3","Three");hst1to100.put("4","Four");
		hst1to100.put("5","Five");hst1to100.put("6","Six");hst1to100.put("7","Seven");hst1to100.put("8","Eight");
		hst1to100.put("9","Nine");hst1to100.put("10","Ten");hst1to100.put("11","Eleven");hst1to100.put("12","Twelve");
		hst1to100.put("13","Thirteen");hst1to100.put("14","Fourteen");hst1to100.put("15","Fifteen");hst1to100.put("16","Sixteen");
		hst1to100.put("17","Seventeen");hst1to100.put("18","Eighteen");hst1to100.put("19","Nineteen");hst1to100.put("20","Twenty");
		hst1to100.put("21","Twenty One");hst1to100.put("22","Twenty Two");hst1to100.put("23","Twenty Three");hst1to100.put("24","Twenty Four");
		hst1to100.put("25","Twenty Five");hst1to100.put("26","Twenty Six");hst1to100.put("27","Twenty Seven");hst1to100.put("28","Twenty Eight");
		hst1to100.put("29","Twenty Nine");hst1to100.put("30","Thirty");hst1to100.put("31","Thirty One");hst1to100.put("32","Thirty Two");
		hst1to100.put("33","Thirty Three");hst1to100.put("34","Thirty Four");hst1to100.put("35","Thirty Five");hst1to100.put("36","Thirty Six");
		hst1to100.put("37","Thirty Seven");hst1to100.put("38","Thirty Eight");hst1to100.put("39","Thirty Nine");hst1to100.put("40","Fourty");
		hst1to100.put("41","Fourty One");hst1to100.put("42","Fourty Two");hst1to100.put("43","Fourty Three");hst1to100.put("44","Fourty Four");
		hst1to100.put("45","Fourty Five");hst1to100.put("46","Fourty Six");hst1to100.put("47","Fourty Seven");hst1to100.put("48","Fourty Eight");
		hst1to100.put("49","Fourty Nine");hst1to100.put("50","Fity");hst1to100.put("51","Fifty One");hst1to100.put("52","Fifty Two");
		hst1to100.put("53","Fifty Three");hst1to100.put("54","Fifty Four");hst1to100.put("55","Fifty Five");hst1to100.put("56","Fifty Six");
		hst1to100.put("57","Fifty Seven");hst1to100.put("58","Fifty Eight");hst1to100.put("59","Fifty Nine");hst1to100.put("60","Sixty");
		hst1to100.put("61","Sixty One");hst1to100.put("62","Sixty Two");hst1to100.put("63","Sixty Three");hst1to100.put("64","Sixty Four");
		hst1to100.put("65","Sixty Five");hst1to100.put("66","Sixty Six");hst1to100.put("67","Sixty Seven");hst1to100.put("68","Sixty Eight");
        hst1to100.put("69","Sixty Nine");hst1to100.put("70","Seventy");hst1to100.put("71","Seventy One");hst1to100.put("72","Seventy Two");hst1to100.put("73","Seventy Three");
		hst1to100.put("74","Seventy Four");hst1to100.put("75","Seventy Five");hst1to100.put("76","Seventy Six");hst1to100.put("77","Seventy Seven");
		hst1to100.put("78","Seventy Eight");hst1to100.put("79","Seventy Nine");hst1to100.put("80","Eighty");hst1to100.put("81","Eighty One");
		hst1to100.put("82","Eighty Two");hst1to100.put("83","Eighty Three");hst1to100.put("84","Eighty Four");hst1to100.put("85","Eighty Five");		
		hst1to100.put("86","Eighty Six");hst1to100.put("87","Eighty Seven");hst1to100.put("88","Eighty Eight");hst1to100.put("89","Eighty Nine");
		hst1to100.put("90","Ninety");hst1to100.put("91","Ninety One");hst1to100.put("92","Ninety Two");hst1to100.put("93","Ninety Three");
		hst1to100.put("94","Ninety Four");hst1to100.put("95","Ninety Five");hst1to100.put("96","Ninety Six");hst1to100.put("97","Ninety Seven");
		hst1to100.put("98","Ninety Eight");hst1to100.put("99","Ninety Nine");
	}
	
	
	
}