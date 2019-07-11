/*
System Name   : System admin.
Program Name  : ISO Hardware installation details
Program Desc. : ISO Hardware installation printing.
Author        : Mrs.Dipti.S.Shinde.
Date          : 5th SEP 2005
Version       : MMS v2.0.0
Modified By   :	
Modified Date : 
Modified det. :
Version       :
*/


import java.sql.ResultSet;import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.io.FileOutputStream; import java.io.DataOutputStream; import java.util.Date; 
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
/**
<b>Program Name :</b> Iso hardware installation wise Report program.

<b>Purpose :</b> This report is for ISO documentations of Hardware installation printing.

List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
HW_MCMST        MC_SRLNO                                     #
HW_LCMST        LC_LOCCD                                     #
HW_PRMST        PR_SRLNO                                     #
CO_CDTRN        CMT_COCCD                                    #                                  
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name  Column Name       Table name      Type/Size       Description
----------------------------------------------------------------------------------------------------------
txtMAINL    (CMT_CODCD,3,2)   CO_CDTRN        VARCHAR(15)     Main Location Code as HO / Works
txtHODNM    LC_HODNM          HW_LCMST        VARCHAR(20)     Dept's Head Name.
----------------------------------------------------------------------------------------------------------

Validations :
  - Main Location Code must be valid.
  - HOD Name is only provided when details for perticular department is requried.  
*/
public class sa_rp005 extends cl_rbase 
{
	private JTextField txtMAINL;
	private JTextField txtHODNM;	
	private String L_strADR01="";
	private String L_strHODNM;
	private String L_strPACTRF="";
	private String L_strLICNO;
	private String L_strSRLNO="";
	private String L_strHODNM_PR="";
	private String L_strLOCCD_PR;
	private String L_strACTRF="";
	private String L_strCDRDS="";
	private String L_strMDLNO="";
	private String L_strMAKDS="";
	private String L_strDPTCD="";
	private String L_strMACNM="";
	private String L_strIPADD;
	private String L_strOSNAM="";
	private String L_strLOCCD="";
	private String L_strLOCDS="";
	private String L_strCPUDS="";
	private String L_strHDDDS="";
	private String L_strRAMDS="";
	private String L_strMONDS="";
	private String L_strPORNO="";
	private String L_strPRPRFDS="";
	private String L_strPRACTRF="";
	private String L_strPRMDLNO="";
	private String L_strPRSRLNO="";
	private String L_strPRPORNO="";
	private String L_strGRNNO="";
	private String L_strPRGRNNO="";
	private int L_intSRLNO = 0;
	private boolean L_flgHODCHG = false;
	private boolean L_flgLOCCHG = false;
	private boolean L_flgSRLCHK = false;
	private String strSPARE_LOC = "SYS_090";
	private String strSCRAP_LOC = "SYS_099";
	private String strFILNM;
	private int intRECCT=0;	
	private FileOutputStream fosREPORT;
    private DataOutputStream dosREPORT;
	
	private JRadioButton  rdbSTSYS;
	private JRadioButton rdbSTSNO;
	private JRadioButton rdbSTSALL;
	private ButtonGroup  btgSTSFL;
	
	private JRadioButton  rdbHWDTL;
	private JRadioButton rdbHWSUM;
	private ButtonGroup  btgHWREP;
	
	private JRadioButton  rdbMCDTL;
	private JRadioButton rdbPRDTL;
	private ButtonGroup  btgMACDL;
	sa_rp005()
	{
	    super(2);
	    try
	    {				
		    setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			add(new JLabel("System Type"),3,4,1,2,this,'L'); 
			add(txtMAINL = new JTextField(),3,5,1,1,this,'L');
			
			add(new JLabel("HOD Name"),4,4,1,2,this,'L'); 
			add(txtHODNM = new JTextField(),4,5,1,1,this,'L');

			add(new JLabel("Usage Status"),5,4,1,2,this,'L');
		    add(rdbSTSYS=new JRadioButton("Yes"),5,5,1,1,this,'L');
			add(rdbSTSNO=new JRadioButton("No"),5,6,1,1,this,'L');
			add(rdbSTSALL=new JRadioButton("All"),5,7,1,1,this,'L');
			btgSTSFL=new ButtonGroup();
			btgSTSFL.add(rdbSTSYS); 
			btgSTSFL.add(rdbSTSNO); 
			btgSTSFL.add(rdbSTSALL); 
			rdbSTSALL.setSelected(true);
			
			add(rdbHWDTL=new JRadioButton("Detail"),6,4,1,1,this,'L');
			add(rdbHWSUM=new JRadioButton("Summery"),6,5,1,1,this,'L');
			btgHWREP=new ButtonGroup();
			btgHWREP.add(rdbHWDTL); 
			btgHWREP.add(rdbHWSUM);  
			rdbHWDTL.setSelected(true);
			
			add(rdbMCDTL=new JRadioButton("Computer"),6,6,1,1,this,'L');
			add(rdbPRDTL=new JRadioButton("Peripheral"),6,7,1,2,this,'L');
			btgMACDL=new ButtonGroup();
			btgMACDL.add(rdbMCDTL); 
			btgMACDL.add(rdbPRDTL);  
			rdbMCDTL.setVisible(false);
			rdbPRDTL.setVisible(false);
			rdbMCDTL.setSelected(true);
			
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);			
		}
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE); 		
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{   
			//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)))
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{				
				txtMAINL.setText(M_strSBSCD.substring(0,2));
				txtMAINL.requestFocus();
				setMSG("Please Select Option..",'N');					
				setENBL(true);
			}
			else
			{					
				cl_dat.M_cmbOPTN_pbst.requestFocus();
				setMSG("select option..",'N');
				setENBL(false);
			}	
		}		
		if(M_objSOURC == txtMAINL)
		{
			if(txtMAINL.getText().trim().length()>0)
				txtHODNM.requestFocus();				
		}
		else if(M_objSOURC == txtHODNM)
		{			
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		else if(M_objSOURC == rdbHWSUM)
		{			
			rdbMCDTL.setVisible(true);
			rdbPRDTL.setVisible(true);
		}
		else if(M_objSOURC == rdbHWDTL)
		{			
			rdbMCDTL.setVisible(false);
			rdbPRDTL.setVisible(false);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
   		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{					
			if(M_objSOURC== txtMAINL)
			{
				M_strHLPFLD = "txtMAINL";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT substring(CMT_CODCD,3,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' and CMT_CGSTP = 'COXXSBS' and CMT_CODCD like 'SA%' and substr(CMT_CODCD,3,2)||'0000' in "+M_strSBSLS;
				if(txtMAINL.getText().length() >0)			
					M_strSQLQRY += " and substring(CMT_CODCD,3,2) like '"+txtMAINL.getText().trim()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"System code","description"},2,"CT");			
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if(M_objSOURC== txtHODNM)
			{
				M_strHLPFLD = "txtHODNM";
				cl_dat.M_flgHELPFL_pbst = true;	
				M_strSQLQRY=" SELECT distinct LC_HODNM from HW_LCMST where LC_SYSTP = '"+txtMAINL.getText().trim()+"' and ifnull(LC_STSFL,' ') <> 'X' and LC_SBSCD in "+M_strSBSLS;
				if(txtHODNM.getText().length() >0)
					M_strSQLQRY += " AND LC_HODNM like '"+ txtHODNM.getText().trim()+"%'";
				M_strSQLQRY += " order by LC_HODNM";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"HOD Name"},1,"CT");										
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
	}
	/**
	 * Method to execuate the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtMAINL")
         {
			cl_dat.M_flgHELPFL_pbst = false;	
			txtMAINL.setText(cl_dat.M_strHLPSTR_pbst);			
		 }
		if(M_strHLPFLD == "txtHODNM")
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtHODNM.setText(cl_dat.M_strHLPSTR_pbst);				
		}
	}	
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;		
		try
		{
			if(M_rdbHTML.isSelected())
			 	strFILNM = cl_dat.M_strREPSTR_pbst +"sa_rp005.html";				
			else if(M_rdbTEXT.isSelected())
			     strFILNM = cl_dat.M_strREPSTR_pbst + "sa_rp005.doc";				
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
			L_intSRLNO = 0;
			getDATA();			
			
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if(M_rdbTEXT.isSelected())
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Documention of Hardware"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}					
	}	
	/**
	 * Method to fetch data from the database & to club it with header 
	 */
	private void getDATA()
	{
		try
		{
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			String L_strSTSFL="";
			if(rdbSTSYS.isSelected())
				L_strSTSFL="1";
			else if(rdbSTSNO.isSelected())
				L_strSTSFL="0";
			else if(rdbSTSALL.isSelected())
				L_strSTSFL="1','0";
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> List of Software Licences </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
			prnHEADER();
			if(rdbHWDTL.isSelected())
			{
				intRECCT = 0;
				final int a=30;
				int x=0,tot=0;			
				M_strSQLQRY = "Select LC_LOCCD,LC_LOCDS,LC_ADR01,LC_HODNM,LC_DPTCD,LC_IPADD,MC_ACTRF,MC_MACNM,MC_CDRDS,MC_MDLNO,MC_MAKDS,MC_SRLNO,MC_OSNAM,MC_CPUDS,MC_HDDDS,MC_RAMDS,MC_MONDS,MC_PORNO,MC_GRNRF,MC_LICNO";
				M_strSQLQRY += " from HW_LCMST,HW_MCMST WHERE MC_SYSTP = '"+txtMAINL.getText().trim()+"' and MC_SYSTP=LC_SYSTP and MC_LOCCD = LC_LOCCD and ifnull(LC_STSFL,' ') <> 'X'";
				if(txtHODNM.getText().trim().length()>0)
					M_strSQLQRY += " AND LC_HODNM = '"+ txtHODNM.getText().trim()+"'";
				M_strSQLQRY += " AND MC_STSFL in('"+L_strSTSFL+"') "; /* and ifnull(MC_LOCCD,'') not in ('"+strSPARE_LOC+"','"+strSCRAP_LOC+"') */
				M_strSQLQRY += " union Select LC_LOCCD,LC_LOCDS,LC_ADR01,LC_HODNM,LC_DPTCD,LC_IPADD,' ' MC_ACTRF,' ' MC_MACNM,' ' MC_CDRDS,' ' MC_MDLNO,' ' MC_MAKDS,' ' MC_SRLNO,' ' MC_OSNAM,' ' MC_CPUDS,' ' MC_HDDDS,' ' MC_RAMDS,' ' MC_MONDS,' ' MC_PORNO,' ' MC_GRNRF,' ' MC_LICNO";
				M_strSQLQRY += " from HW_LCMST WHERE LC_SYSTP = '"+txtMAINL.getText().trim()+"' and LC_LOCCD in (select PR_LOCCD from HW_PRMST where PR_SYSTP = '"+txtMAINL.getText().trim()+"' and PR_LOCCD not in (select MC_LOCCD from HW_MCMST where MC_SYSTP = '"+txtMAINL.getText().trim()+"'))"; 
				if(txtHODNM.getText().trim().length()>0)
					M_strSQLQRY += " AND LC_HODNM = '"+ txtHODNM.getText().trim()+"'";		
				M_strSQLQRY += " order by LC_HODNM, LC_LOCCD";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				
				if(M_rstRSSET !=null)
				{		
					L_strHODNM_PR="";
					L_strLOCCD_PR="";
					L_strLOCCD="";
					while(M_rstRSSET.next())
					{
						L_strADR01 = nvlSTRVL(M_rstRSSET.getString("LC_ADR01"),"");
						L_strHODNM = nvlSTRVL(M_rstRSSET.getString("LC_HODNM"),"");
						L_strLICNO  = nvlSTRVL(M_rstRSSET.getString("MC_LICNO"),"");
						L_strMACNM = nvlSTRVL(M_rstRSSET.getString("MC_MACNM"),"");
						L_strMDLNO = nvlSTRVL(M_rstRSSET.getString("MC_MDLNO"),"");
						L_strMAKDS = nvlSTRVL(M_rstRSSET.getString("MC_MAKDS"),"");
						L_strCDRDS = nvlSTRVL(M_rstRSSET.getString("MC_CDRDS"),"");
						L_strACTRF = nvlSTRVL(M_rstRSSET.getString("MC_ACTRF"),"");
						L_strIPADD = nvlSTRVL(M_rstRSSET.getString("LC_IPADD"),"");
						//L_strMACTP = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_MACTP"),""),15);
						L_strSRLNO = nvlSTRVL(M_rstRSSET.getString("MC_SRLNO"),"");
						L_strOSNAM = nvlSTRVL(M_rstRSSET.getString("MC_OSNAM"),"");
						L_strLOCCD = nvlSTRVL(M_rstRSSET.getString("LC_LOCCD"),"");
						L_strLOCDS = nvlSTRVL(M_rstRSSET.getString("LC_LOCDS"),"");
						L_strCPUDS = nvlSTRVL(M_rstRSSET.getString("MC_CPUDS"),"");
						L_strHDDDS = nvlSTRVL(M_rstRSSET.getString("MC_HDDDS"),"");
						L_strRAMDS = nvlSTRVL(M_rstRSSET.getString("MC_RAMDS"),"");
						L_strMONDS = nvlSTRVL(M_rstRSSET.getString("MC_MONDS"),"");
						L_strPORNO = nvlSTRVL(M_rstRSSET.getString("MC_PORNO"),"");
						L_strGRNNO = nvlSTRVL(M_rstRSSET.getString("MC_GRNRF"),"");											
						//dosREPORT.writeBytes(padSTRING('R'," ",18)+"|"+L_strMACNM.trim());
						//System.out.println("L_strHODNM_PR :"+L_strHODNM_PR+"L_strHODNM :"+L_strHODNM);
						//System.out.println(L_strHODNM.substring(0,3)+" / "+L_strHODNM_PR.substring(0,3));
						L_flgHODCHG = (L_strHODNM.equals(L_strHODNM_PR)) ? false : true;
						L_flgLOCCHG = (L_strLOCCD.equals(L_strLOCCD_PR)) ? false : true;
						String L_strLOCDS1 = L_flgLOCCHG ? L_strLOCDS : "";
						if(L_flgHODCHG)  
							{
								L_intSRLNO += 1;
								dosREPORT.writeBytes("|__|__________________|______________________________|__________________________________________|\n");
								cl_dat.M_intLINNO_pbst+=1;
								L_strHODNM_PR = L_strHODNM;
							}
							else 
								exePRTREC(""        ,(L_flgLOCCHG ? "______________________________" : ".............................."),"__________________________________________");
						if(cl_dat.M_intLINNO_pbst> 63)
						{
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
						}
						
						exePRTREC("",L_strMACNM,addSTRING(L_strMAKDS,0)+addSTRING(L_strCPUDS,1)+addSTRING(L_strHDDDS,1));
						L_flgSRLCHK = L_flgHODCHG ? true : false;
						if(L_strSRLNO.trim().length()>0)
						{
							exePRTREC(L_strHODNM    ,L_strIPADD,addSTRING(L_strRAMDS,0)+addSTRING(L_strCDRDS,1));
							exePRTREC("("+L_strADR01.trim()+")"  ,addSTRING(L_strMDLNO,0)+addSTRING(L_strSRLNO,1)  ,addSTRING(L_strMONDS,0)+addSTRING("Key Board",1)+addSTRING("Mouse",1));
							exePRTREC(""  ,"",addSTRING((L_strPORNO.trim().length()>0 ? "PO :" : "")+L_strPORNO,0)+addSTRING((L_strGRNNO.trim().length()>0 ? "  GRIN :" : "")+L_strGRNNO,1));
							exePRTREC(""    ,L_strOSNAM,L_strACTRF);
							exePRTREC(""        ,"",L_strLICNO);
						}
						if(cl_dat.M_intLINNO_pbst> 63)
						{
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
						}
						if(L_flgLOCCHG)
							{exePRFREC(L_strLOCCD, L_flgLOCCHG); L_strLOCCD_PR = L_strLOCCD;}
						//if(L_strPRPRFDS.toString().trim().length()>0)
						//{
						//	exePRTREC(""        ,(L_strLOCCD.equalsIgnoreCase(strSPARE_LOC) ? "______________________________" : ""),(L_strLOCCD.equalsIgnoreCase(strSPARE_LOC) ? "__________________________________________" : "............................................."));
						//	exePRTREC(""        ,"",addSTRING(L_strPRPRFDS,0)+addSTRING("SRL "+L_strPRSRLNO,1));
						//	exePRTREC(""        ,"",addSTRING((L_strPRPORNO.trim().length()>0 ? "PO :" : "")+L_strPRPORNO,0)+addSTRING((L_strPRGRNNO.trim().length()>0 ? "GRIN :" : "")+L_strPRGRNNO,1));
						//}
						//if (!L_flgHODCHG)
						L_flgHODCHG = false;
						L_flgLOCCHG = false;
						if(cl_dat.M_intLINNO_pbst> 63)
						{	
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
						}
						intRECCT++;
					}
					M_rstRSSET.close();
				}	
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			}
			else if(rdbHWSUM.isSelected())
			{
				String strNEW_MCHCT="",strOLD_MCHCT="";
				intRECCT=0;
				
				if(rdbMCDTL.isSelected())
				{
					M_strSQLQRY= " SELECT MC_MCHCT,MC_MDLNO,MC_SRLNO,MC_MACNM,MC_STSFL,LC_IPADD,LC_LOCDS,CMT_CODDS";
		  			M_strSQLQRY+= " from HW_MCMST,HW_LCMST,CO_CDTRN" ;
		  			M_strSQLQRY+= " where MC_SYSTP=LC_SYSTP AND MC_LOCCD=LC_LOCCD AND MC_STSFL in('"+L_strSTSFL+"')";
		  			if(txtMAINL.getText().toString().length()>0)
		  				M_strSQLQRY+= " and MC_SYSTP='"+txtMAINL.getText().toString()+"'";
		  			if(txtHODNM.getText().trim().length()>0)
						M_strSQLQRY += " AND LC_HODNM = '"+ txtHODNM.getText().trim()+"'";
		  			M_strSQLQRY += "and MC_MCHCT=CMT_CODCD AND CMT_CGMTP ='SYS' and CMT_CGSTP ='HWXXMCH'";
					M_strSQLQRY += "order by MC_MCHCT,MC_MACNM,MC_STSFL ";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
					System.out.println("Select>>>>"+M_strSQLQRY);
					if(M_rstRSSET !=null)
					{	
						while(M_rstRSSET.next())
						{
							strNEW_MCHCT=nvlSTRVL(M_rstRSSET.getString("MC_MCHCT"),"");
							
							if(!strOLD_MCHCT.equals(strNEW_MCHCT))
							{
								crtNWLIN();
								
								if(M_rdbHTML.isSelected())// for html
									dosREPORT.writeBytes("<b>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes("\n"+padSTRING('R',M_rstRSSET.getString("CMT_CODDS").toString(),30));
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</b>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								crtNWLIN();
								
							}
							strOLD_MCHCT=nvlSTRVL(M_rstRSSET.getString("MC_MCHCT"),"");
							crtNWLIN();
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_MACNM"),""),21));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_SRLNO"),"")+" / "+nvlSTRVL(M_rstRSSET.getString("MC_MDLNO"),""),31));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("LC_IPADD"),""),18));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("LC_LOCDS"),""),25));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MC_STSFL"),""),3));
						
							intRECCT++;
						}
					}
					M_rstRSSET.close();
					genRPFTR();
				}
				else if(rdbPRDTL.isSelected())
				{
					M_strSQLQRY= " SELECT PR_PRFCT,PR_MDLNO,PR_LOCCD,PR_SRLNO,PR_STSFL,LC_IPADD,LC_LOCDS,CMT_CODDS";
		  			M_strSQLQRY+= " from HW_PRMST,HW_LCMST,CO_CDTRN" ;
		  			M_strSQLQRY+= " where PR_SYSTP=LC_SYSTP AND PR_LOCCD=LC_LOCCD AND PR_STSFL in('"+L_strSTSFL+"')";
		  			if(txtMAINL.getText().toString().length()>0)
		  				M_strSQLQRY+= " and PR_SYSTP='"+txtMAINL.getText().toString()+"'";
		  			if(txtHODNM.getText().trim().length()>0)
						M_strSQLQRY += " AND LC_HODNM = '"+ txtHODNM.getText().trim()+"'";
		  			M_strSQLQRY += "and PR_PRFCT=CMT_CODCD AND CMT_CGMTP ='SYS' and CMT_CGSTP ='HWXXPRF'";
					M_strSQLQRY += "order by PR_PRFCT,PR_SRLNO,PR_STSFL ";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
					System.out.println("Select>>>>"+M_strSQLQRY);
					if(M_rstRSSET !=null)
					{	
						while(M_rstRSSET.next())
						{
							strNEW_MCHCT=nvlSTRVL(M_rstRSSET.getString("PR_PRFCT"),"");
							
							if(!strOLD_MCHCT.equals(strNEW_MCHCT))
							{
								crtNWLIN();
								
								if(M_rdbHTML.isSelected())// for html
									dosREPORT.writeBytes("<b>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes("\n"+padSTRING('R',M_rstRSSET.getString("CMT_CODDS").toString(),30));
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</b>");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								crtNWLIN();
								
							}
							strOLD_MCHCT=nvlSTRVL(M_rstRSSET.getString("PR_PRFCT"),"");
							crtNWLIN();
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PR_SRLNO"),"")+" / "+nvlSTRVL(M_rstRSSET.getString("PR_MDLNO"),""),37));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("LC_IPADD"),""),18));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("LC_LOCDS"),""),27));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PR_STSFL"),""),3));
						
							intRECCT++;
						}
					}
					M_rstRSSET.close();
					genRPFTR();
				}
			}
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			  	prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}
		setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
   }

void genRPFTR()
{
	try
	{
		cl_dat.M_intLINNO_pbst = 0;	//new test
		dosREPORT.writeBytes(padSTRING('L',"",65));
		crtNWLIN();
		dosREPORT.writeBytes("---------------------------------------------------------------------------------------------");
		crtNWLIN();
		prnFMTCHR(dosREPORT,M_strNOCPI17);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			prnFMTCHR(dosREPORT,M_strEJT);			
		if(M_rdbHTML.isSelected())
			dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
	}
	catch(Exception L_IOE)
	{
		setMSG(L_IOE,"Report Footer");
	}
}	
private void crtNWLIN() 
{
	try
	{
		dosREPORT.writeBytes("\n");
		cl_dat.M_intLINNO_pbst++;
		if(M_rdbHTML.isSelected())
		{
			if(cl_dat.M_intLINNO_pbst >55)
			{
				genRPFTR();
				prnHEADER();
			}
		}	
		else if(cl_dat.M_intLINNO_pbst >55)
		{		
			genRPFTR();
			prnHEADER();			
		}			
	}
	catch(Exception e)
	{
	    setMSG(e,"Chlid.crtNWLIN");
	}
}

	/**
	* Method to add Printing peripheral details (printer detail) in the Report.
	*/   
   private void exePRFREC(String LP_LOCCD, boolean LP_LOCCHG)
   {
	   try
	   {
		   if(LP_LOCCD.equalsIgnoreCase(strSPARE_LOC) && LP_LOCCHG == false)
			   return;
			String L_strSQLQRY = "Select PR_ACTRF,PR_PRFDS,PR_ACTRF,PR_MDLNO,PR_SRLNO,PR_PORNO,PR_GRNNO from HW_PRMST where PR_SYSTP = '"+txtMAINL.getText().trim()+"' and ifnull(PR_STSFL,' ') <> 'X'  and PR_LOCCD = '"+LP_LOCCD+"'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(L_strSQLQRY);
			
			String L_strLOCDS1 = L_flgLOCCHG ? L_strLOCDS : "";
			if(L_rstRSSET == null || !L_rstRSSET.next())
				return;
			while(true)
			{
				L_strPACTRF = nvlSTRVL(L_rstRSSET.getString("PR_ACTRF"),"");
				L_strPRPRFDS  = nvlSTRVL(L_rstRSSET.getString("PR_PRFDS"),"");
				L_strPRMDLNO  = nvlSTRVL(L_rstRSSET.getString("PR_MDLNO"),"");
				L_strPRSRLNO = nvlSTRVL(L_rstRSSET.getString("PR_SRLNO"),"");
				L_strPRPORNO = nvlSTRVL(L_rstRSSET.getString("PR_PORNO"),"");
				L_strPRGRNNO = nvlSTRVL(L_rstRSSET.getString("PR_GRNNO"),"");
				L_strPRACTRF = nvlSTRVL(L_rstRSSET.getString("PR_ACTRF"),"");

				exePRTREC(""        ,"",".............................................");
				exePRTREC(""   ,"("+L_strLOCDS1+")",addSTRING(L_strPRPRFDS+" "+L_strPRMDLNO,0));
				exePRTREC(""        ,"",addSTRING((L_strPRPORNO.trim().length()>0 ? "PO :" : "")+L_strPRPORNO,0)+addSTRING((L_strPRGRNNO.trim().length()>0 ? "GRIN :" : "")+L_strPRGRNNO,1));
				exePRTREC(""        ,"",addSTRING("SRL : "+L_strPRSRLNO,0)+addSTRING(L_strPRACTRF.trim(),1));
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
	   }
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRFREC");
		}
   }
   /** 
    * method to add or club more than one hardware parameter in a single string
    */
   private String addSTRING(String LP_STRVL, int LP_SEQNO)
   {
	   try
	   {
			if(LP_STRVL.trim().length()==0)
				   return "";
			if(LP_SEQNO == 0)
				   return LP_STRVL.trim();
			if(LP_SEQNO>0)
				   return " / "+LP_STRVL.trim();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"addSTRING");
		}
	   return "";
   }      
   /** 
    * Common method for printing detail record
    */
   private void exePRTREC(String LP_MCDET0,String LP_MCDET1,String LP_MCDET2)
   {
	   
	   try
	   {
		    //System.out.println(LP_LOCCD+"   "+LP_MCDET1+"  "+LP_MCDET2);
			if(LP_MCDET0.length()==0 && LP_MCDET1.length()==0 && LP_MCDET2.length()==0)
				return;
			LP_MCDET0 = L_flgHODCHG ? LP_MCDET0 : "";
			 dosREPORT.writeBytes("|"+(L_flgSRLCHK ? padSTRING('L',setNumberFormat(L_intSRLNO,0),2) : "  ")+"|"+padSTRING('R',LP_MCDET0,18)+"|"+padSTRING('R',LP_MCDET1,30)+"|"+padSTRING('R',LP_MCDET2,42)+"| \n");
			 cl_dat.M_intLINNO_pbst+=1;
			 L_flgSRLCHK = false;
	   }
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRTREC");
		}
   }
   /**
	Method to validate DATA before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{
			if(txtMAINL.getText().trim().length() == 0)
			{
				setMSG("Location Code Cannnot be Blank ..",'E');
				return false;
			}
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}

	/**
	 * Method to generate the header part of the report.
	 */
	private void prnHEADER()
	{
		try
		{			
			cl_dat.M_PAGENO += 1;
			if(rdbHWDTL.isSelected())
			{
				ResultSet L_rstRSSET = null;
				String L_strISSNO="",L_strISSDT="",L_strREVNO="",L_strREVDT="";
				M_strSQLQRY="SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'HWXXISO' and CMT_CODCD like '005_%' order by CMT_CODCD";
				L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					setMSG("ISO detail not found for 'D"+cl_dat.M_strCMPCD_pbst+"FGXXISO_005",'E');
				while (true)
				{
					if(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"").substring(4,6).equals("01"))
						L_strISSNO = padSTRING('R',nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""),10);
					if(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"").substring(4,6).equals("02"))
						L_strISSDT = padSTRING('R',nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""),10);
					if(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"").substring(4,6).equals("03"))
						L_strREVNO = padSTRING('R',nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""),10);
					if(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"").substring(4,6).equals("04"))
						L_strREVDT = padSTRING('R',nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""),10);
					if(!L_rstRSSET.next())
						break;
				}
				L_rstRSSET.close();
					
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				prnFMTCHR(dosREPORT,M_strCPI12);
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("________________________________________________________________________________________________\n");
				dosREPORT.writeBytes("|   SPL/SYS/QR/005    |         SUPREME PETROCHEM LIMITED          |Page No:"+ String.valueOf(cl_dat.M_PAGENO) +"                   |\n");
				dosREPORT.writeBytes("|_____________________|____________________________________________|____________________________|\n"); 
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes("|ISSUE NO. |"+L_strISSNO+"|                                            |Prepared by:                |\n");
				dosREPORT.writeBytes("|__________|__________|                                            |____________________________|\n");
				dosREPORT.writeBytes("|Issue Date|"+L_strISSDT+"|       LIST OF HARDWARE INSTALLATIONS       |DM(SYS)         |           |\n");
				dosREPORT.writeBytes("|__________|__________|                                            |________________|___________|\n");
				dosREPORT.writeBytes("|Rev .NO   |"+L_strREVNO+"|                                            |Approved by                 |\n");
				dosREPORT.writeBytes("|__________|__________|____________________________________________|____________________________|\n");
				dosREPORT.writeBytes("|Rev. Date |"+L_strREVDT+"|Issued to : Nil                             |HOD(SYS)        |           |\n");
				dosREPORT.writeBytes("|__________|__________|____________________________________________|________________|___________|\n");	 
				dosREPORT.writeBytes("|Sr| Dept.Name /      |M/c.Name /                    |Machine Configuration                     |\n");	
				dosREPORT.writeBytes("|No| Co-Ordinator /   |IP Address /                  |                                          |\n");  
				dosREPORT.writeBytes("|  | System           |M/c Model/ Sr.no/             |                                          |\n");	
				dosREPORT.writeBytes("|  | Location         |Operating Sytem               |                                          |\n");
				dosREPORT.writeBytes("|__|__________________|______________________________|__________________________________________|\n");				
				cl_dat.M_intLINNO_pbst = 16;
			}
			else if(rdbHWSUM.isSelected())
			{
				crtNWLIN();
				if(rdbMCDTL.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,72));
					dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
					dosREPORT.writeBytes(padSTRING('R',"Summery of installed/uninstalled Machines ",72));
					dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
					if(txtHODNM.getText().length()>0)
		    			dosREPORT.writeBytes(padSTRING('R',txtHODNM.getText().toString(),80)+"\n");
					dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");						
					dosREPORT.writeBytes("Machine Name         Serial No/Model No             IP Address        Location            Status\n");					
					dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");
				}
				else if(rdbPRDTL.isSelected())
				{
					dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,67));
					dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
					dosREPORT.writeBytes(padSTRING('R',"Summery of installed/uninstalled Peripheral ",67));
					dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
					if(txtHODNM.getText().length()>0)
		    			dosREPORT.writeBytes(padSTRING('R',txtHODNM.getText().toString(),80)+"\n");
					dosREPORT.writeBytes("-------------------------------------------------------------------------------------------\n");						
					dosREPORT.writeBytes("Serial No/Model No                   IP Address        Location              Usages Status\n");					
					dosREPORT.writeBytes("-------------------------------------------------------------------------------------------\n");
				}
			}

			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}

