/*
System Name		: Materials Management System
Program Name	: Stock Transfer Note Report 
Author			: Mr.Zaheer Khan

Modified Date	: 10/05/2006
Version			: v2.0.0
*/
import java.sql.ResultSet;import java.util.Date;
import java.awt.event.KeyEvent;import javax.swing.JComponent; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;
/**<pre>
System Name  : Material Management System.
 
Program Name : Stock Transfer Note Report 

Purpose : Program to generate Stock Transfer Note Report for given range of STN No.
List of STN report is also generated. STN value is also printed on STN List.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_STNTR       SA_MMSBS,SA_STRTP,SA_STNNO,SA_MATCD                     #
MM_STPRC       ST_MMSBS,ST_STRTP,ST_MATCD                              #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtFMSTN    STN_STNNO       MM_STNTR      VARCHAR(8)     STN Number
txtTOSTN    STN_STNNO       MM_STNTR      VARCHAR(8)     STN Number
cmbSTNTP    CMT_CODCD		CO_CDTRN	  varchar(2)     STN Type
cmbSTNTP    CMT_CODDS		CO_CDTRN	  varchar(20)    STN Type description
--------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
            
   Report Data is taken from tables MM_STNTR and MM_STMST and CO_CTMST for given condition     
		if STN type is 02 "Transfer from Work Order to Stores" then data will came on this condition
			1) STN_TOMAT = CT_MATCD
			2) AND STN_STNTP=specified STN type 
			3) AND STN_FRSTR = specified Store type 
			4) AND STN_STNNO in the range of given SNT Nuberr
			5) AND isnull(STN_STSFL,' ') <> 'X'

		if STN Type is stocks Transfer or transfer from stores to Work Order then data will came  on this condition
			1) STN_FRMAT  = STP_MATCD
			2) AND STN_TOMAT = CT_MATCD
			3) AND STN_STNTP=specified STN type 
			4) AND STN_FRSTR = specified Store type 
			5) AND STN_STNNO in the range of given range
			6) AND isnull(STN_STSFL,' ') <> 'X'

	Remark description is taken from MM_RMMST 
      1) RM_TRNTP = 'ST' 
      2) AND RM_DOCTP = 'ST' 
      3) AND RM_DOCNO = given STN number
      4) AND isnull(RM_STSFL,'') <> 'X'

<I>
<B>Validations :</B>
   - STN Number Specified must be valid 
</I> */

class mm_rpstn extends cl_rbase
{									/** JcomboBox to display & to select Report Type.*/
	private JComboBox cmbRPTOP;		/** JcomboBox to display & to select STN  Type.*/
	private JComboBox cmbSTNTP;		/** JTextField to display & to enter STN Number.*/
	private JTextField txtFMSTN;	/** JTextField to display & to enter STN Number.*/
	private JTextField txtTOSTN;	/** String variable for Store Type.*/	
	private String strSTRNM;		/** String variable for STN Number.*/
	private String strSTNNO;		/** String variable for Remark Description.*/	
	private String strREMDS;		/** Boolean Varaible to manage the fetching of data only Once.*/
	private boolean flgFIRST;		/** Double Variable to calculate the STN Value.*/
	private double dblSTNVL=0;		 /** Integer variable for Serial Number.*/	
	private int intSRLNO;			 /** FileOutputStream to generate the Report file from the stream of data.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream to generate the stream of Reposrt Data.*/
	private DataOutputStream dosREPORT;	/** String variable for generated Report file Name.*/
	private String strFILNM;	        /**hashTable to maintain the code and description from the CO_CDTRN  table                        	         */
	private Hashtable<String,String> hstCDTRN;        /**String variable for regular Transfer  */
	private String strRGULR="01";      /** String variable for transfer from work order to stores  */
	private String strFRWOR="02";		/** String variable for transfer from  stores to work order  */
	private String strTOWOR="03";
	
	public mm_rpstn()
	{
		super(2);
		try
		{
			String L_strTEMP="";
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			strSTRNM = "";
			strSTNNO = "";	
			cmbSTNTP = new JComboBox();
			cmbRPTOP = new JComboBox();
			hstCDTRN=new Hashtable<String,String>();
			cmbRPTOP.addItem("STN List");
			cmbRPTOP.addItem("STN Note");
			
			setMatrix(20,8);
			add(cmbRPTOP,3,5,1,1.5,this,'L');	
			add(new JLabel("Report Type"),3,4,1,1,this,'R');
			add(cmbRPTOP,3,5,1,1.5,this,'L');
			add(new JLabel("STN Type"),4,4,1,1,this,'R');
			
			M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and  CMT_CGSTP = 'MMXXSTN' and isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					L_strTEMP  =  nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  ");
					L_strTEMP +=  "   ";
					L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbSTNTP.addItem(L_strTEMP);	
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			add(cmbSTNTP,4,5,1,1.5,this,'L');
			add(new JLabel("From STN No."),5,4,1,1,this,'R');
			add(txtFMSTN = new TxtNumLimit(8.0),5,5,1,1,this,'R');
			add(new JLabel("To STN No."),6,4,1,1,this,'R');
			add(txtTOSTN = new TxtNumLimit(8.0),6,5,1,1,this,'R');
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where  CMT_CGMTP ='SYS'  AND CMT_CGSTP = 'MMXXSST' and isnull(CMT_STSFL,'') <> 'X' "  ;
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					hstCDTRN.put((nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")),(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"")));	
				}
				M_rstRSSET.close();
			}
								
			
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In Constructor");
		}
	}
	public mm_rpstn(String P_strSBSCD)
	{
		M_strSBSCD = P_strSBSCD;
		M_rdbTEXT.setSelected(true);
		//System.out.println("IN REPORT");
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))||(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst)))
			{
				setENBL(true);
				M_cmbDESTN.requestFocus();
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);
				cmbRPTOP.requestFocus();
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
			{
				cmbRPTOP.requestFocus();
			}
			else
				M_cmbDESTN.requestFocus();
		}

		if(M_objSOURC == cmbRPTOP)
		{
			txtFMSTN.setText("");
			txtTOSTN.setText("");
			setMSG("Select Report Type..",'N');
		}
		if(M_objSOURC == cmbSTNTP)
		{
			txtFMSTN.setText("");
			txtTOSTN.setText("");
			setMSG("Select STN Type",'N');
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strSTNTP=cmbSTNTP.getSelectedItem().toString().trim().substring(0,2);
			if(M_objSOURC == txtFMSTN)
			{
				M_strHLPFLD = "txtFMSTN";
				String L_ARRHDR[]={"STN NO.","STN DATE"};
				M_strSQLQRY = "Select distinct STN_STNNO,STN_STNDT from MM_STNTR where STN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STN_STNTP='"+L_strSTNTP +"' AND STN_FRSTR='" + M_strSBSCD.substring(2,4) +"'";
				if(txtFMSTN.getText().trim().length() >0)
					M_strSQLQRY += " and STN_STNNO like '" + txtFMSTN.getText().trim() + "%'";
				M_strSQLQRY += "  AND isnull(STN_STSFL,'')<> 'X'  Order by STN_STNNO";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"STN No.","STN Date"},2,"CT");
			}
			if(M_objSOURC == txtTOSTN)
			{
				M_strHLPFLD = "txtTOSTN";
				String L_ARRHDR[]={"STN NO.","STN DATE"};
				M_strSQLQRY = "Select distinct (STN_STNNO),STN_STNDT from MM_STNTR where STN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STN_STNTP='"+L_strSTNTP +"' AND  STN_FRSTR='" + M_strSBSCD.substring(2,4) +"'";
				if(txtTOSTN.getText().trim().length() >0)
					M_strSQLQRY += " and STN_STNNO like '" + txtTOSTN.getText().trim() + "%'";
				M_strSQLQRY += " AND isnull(STN_STSFL,'')<> 'X'  Order by STN_STNNO";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"STN No.","STN Date"},2,"CT");

			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbRPTOP)
			{
				cmbSTNTP.requestFocus();
				setMSG("Select STN Type",'N');
			}

			if(M_objSOURC == cmbSTNTP)
			{
				txtFMSTN.requestFocus();
				setMSG("Enter From STN Number",'N');
			}

			if(M_objSOURC == txtFMSTN )
			{
				if(txtFMSTN.getText().length()>0)
				{
					txtTOSTN.setEnabled(true);
					if(txtTOSTN.getText().length() == 0)
					{
						txtTOSTN.setText(txtFMSTN.getText().trim());
						txtTOSTN.select(0,txtTOSTN.getText().length());
						setMSG("Enter To STN Number",'N');
					}
				}
				else
				{
					txtTOSTN.setText("");
					txtTOSTN.setEnabled(false);
				}
			}
			if(M_objSOURC == txtTOSTN )
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	/**
	 * Super Class method overrided to enable & disable the components.
	 */
	public void setENBL(boolean ACTION)
	{
		super.setENBL(ACTION);
	}
	/**
	 * Super Class method overrided to execuate the F1 help for selected Field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMSTN")
		{
			txtFMSTN.setText(cl_dat.M_strHLPSTR_pbst);
			if(txtTOSTN.getText().trim().length() == 0)
				txtTOSTN.setText(txtFMSTN.getText().trim());
			txtTOSTN.setEnabled(true);
			txtTOSTN.requestFocus();
		}
		if(M_strHLPFLD == "txtTOSTN")
		{
			txtTOSTN.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}	
	/**
	 * Method to fetch data from the database & to club it with header & footer of the report.
	 */
	void exePRINT()
	{
		try
		{
			int L_intSELID =0;
			if(!vldDATA())
				return;			
			
			if(cmbRPTOP.getSelectedItem().toString().equals("STN List"))
				L_intSELID = 0;
			else 
				L_intSELID = 1;
			
			getDATA(txtFMSTN.getText().trim(),txtTOSTN.getText().trim(),L_intSELID);
				
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
			    {    
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
			   	cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Transfer Note"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
	/**
	 * Method to validate the inputs before execuation of the SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(M_rdbHTML.isSelected())
				 strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpstn.html";
			if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpstn.doc";
			if(txtFMSTN.getText().trim().length() == 0)
			{
				setMSG("Enter From STN Number..",'E');
				txtFMSTN.requestFocus();
				return false;
			}
			if(txtTOSTN.getText().trim().length() == 0)
			{
				setMSG("Enter To STN Number..",'E');
				txtTOSTN.requestFocus();
				return false;
			}
			else if( Integer.parseInt(txtTOSTN.getText().trim().toString()) < Integer.parseInt(txtFMSTN.getText().trim().toString()) )
			{
				setMSG("To STN Number Should Be Greater Than Or Equal To From STN Number..",'E');
				txtTOSTN.requestFocus();
				return false;
			}
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'E');
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
			setMSG(L_EX,"Error In vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer
	*/
	public void getDATA(String P_strFMSTN,String P_strTOSTN,int P_intRPTOP) throws Exception
	{
		try
		{
			String L_strSPACE="";
			String L_strSQLQRY;
			ResultSet L_rstRSSET;
			String L_strSTNVL ="";
			String L_strTOSTR="";
			String L_strSTRCD="";
			Date tmpDATE;
			String L_strSTNTP="";
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generating Report..",'N');
			
			strSTRNM = cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString();
			L_strSTNTP=cmbSTNTP.getSelectedItem().toString().trim().substring(0,2);
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 1;
			flgFIRST = false;
			strSTNNO ="";
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Stock Transfer Note </title> ");
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE></HEAD>"); 
				dosREPORT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}	
			if(L_strSTNTP.equals(strFRWOR))
			{
				M_strSQLQRY = "Select STN_MMSBS,STN_FRSTR,STN_TOSTR,STN_STNNO,STN_STNDT,STN_STNTP,STN_WORNO,STN_FRMAT,STN_TOMAT,STN_TRNQT,STN_STNRT,STN_FRLOC,STN_TOLOC,STN_PREBY,STN_RECBY,STN_APRBY,(CT_MATDS)STN_FRDES,(CT_UOMCD)STN_UOMCD,CT_MATDS,CT_UOMCD ";
				M_strSQLQRY = "Select STN_MMSBS,STN_FRSTR,STN_WORNO,STN_TOSTR,STN_STNNO,STN_STNDT,STN_FRMAT,STN_TOMAT,STN_TRNQT,STN_STNRT,STN_FRLOC,STN_TOLOC,(CT_MATDS)STN_FRDES,(CT_UOMCD)STN_UOMCD,CT_MATDS,CT_UOMCD ";
				M_strSQLQRY +=" from MM_STNTR,CO_CTMST";
				M_strSQLQRY += " where STN_FRMAT = CT_MATCD AND STN_TOMAT=CT_MATCD AND STN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STN_FRSTR = '" + M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += "  and STN_STNTP='"+L_strSTNTP+"' and STN_STNNO BETWEEN  '" + P_strFMSTN + "' AND '" +P_strTOSTN +"'";
				M_strSQLQRY += " and isnull(STN_STSFL,'') <> 'X' order by STN_STNNO";
			}
			else
			{
				M_strSQLQRY = "Select STN_MMSBS,STN_FRSTR,STN_WORNO,STN_TOSTR,STN_STNNO,STN_STNDT,STN_FRMAT,STN_TOMAT,STN_TRNQT,STN_STNRT,STN_FRLOC,STN_TOLOC,(STP_MATDS)STN_FRDES,(STP_UOMCD)STN_UOMCD,CT_MATDS,CT_UOMCD ";
				M_strSQLQRY +=" from MM_STNTR,MM_STPRC,CO_CTMST";
				M_strSQLQRY += " where STN_CMPCD=STP_CMPCD and ltrim(str(STN_FRSTR,20,0)) = STP_STRTP AND STN_FRMAT = STP_MATCD AND STN_TOMAT=CT_MATCD AND STN_FRSTR = '" + M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += "  and STN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STN_STNTP='"+L_strSTNTP+"' and STN_STNNO BETWEEN  '" + P_strFMSTN + "' AND '" +P_strTOSTN +"'";
				M_strSQLQRY += " and isnull(STN_STSFL,'') <> 'X' order by STN_STNNO";
			}
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(P_intRPTOP == 1) // STN Note
			{
				prnSTNNT(P_strFMSTN,P_strTOSTN);
			}
			else                // STN List
			{
				prnHEADER(P_strFMSTN,P_strTOSTN,P_intRPTOP);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						if(cl_dat.M_intLINNO_pbst >60)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("--------------------------------------------------------------------------------------------\n");
							cl_dat.M_intLINNO_pbst = 0;
							cl_dat.M_PAGENO += 1;
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								prnHEADER(P_strFMSTN,P_strTOSTN,P_intRPTOP);
						}
						if(!strSTNNO.equals(nvlSTRVL(M_rstRSSET.getString("STN_STNNO"),"")))
						{
							strREMDS = "";
							if(flgFIRST)
							{
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst++;
								L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'ST' AND "
									+"RM_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'ST' AND RM_DOCNO = '"+strSTNNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
								System.out.println(L_strSQLQRY);	
								L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
								if(L_rstRSSET!=null)
								{
									if(L_rstRSSET.next())
										strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
								}
								if(L_rstRSSET!=null)
									L_rstRSSET.close();
								dosREPORT.writeBytes(padSTRING('L',"Total  : "+setNumberFormat(dblSTNVL,2),92));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst++;
								dblSTNVL =0;	
								if(strREMDS.length() > 87)
								{
									dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS.substring(0,87),96));
									dosREPORT.writeBytes("\n");
									dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
									cl_dat.M_intLINNO_pbst++;
								}
								else
									dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS,96));
									dosREPORT.writeBytes("\n");
									dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
									dosREPORT.writeBytes("\n");
									cl_dat.M_intLINNO_pbst += 2;
							}
							flgFIRST = true;
							
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 1;
							
							dosREPORT.writeBytes(padSTRING('R',"",4));
							//strSTRCD=nvlSTRVL(M_rstRSSET.getString("STN_TOSTR"),"");
							
							if(L_strSTNTP.equals(strRGULR))
							{
								dosREPORT.writeBytes(padSTRING('R',"Stores From : "+strSTRNM,28));
								dosREPORT.writeBytes(padSTRING('R',"To : ",5));
								L_strSTRCD=nvlSTRVL(M_rstRSSET.getString("STN_TOSTR"),"");
								L_strTOSTR=hstCDTRN.get(L_strSTRCD).toString();
								dosREPORT.writeBytes(padSTRING('R',L_strTOSTR,27));
							}
							else if(L_strSTNTP.equals(strFRWOR))
							{
								String L_strWORNO=nvlSTRVL(M_rstRSSET.getString("STN_WORNO"),"");
								dosREPORT.writeBytes(padSTRING('R',"Work Order : "+L_strWORNO,28));
								dosREPORT.writeBytes(padSTRING('R',"To Store : ",11));
								L_strSTRCD=nvlSTRVL(M_rstRSSET.getString("STN_TOSTR"),"");
					
								L_strTOSTR=(String)hstCDTRN.get(L_strSTRCD);
								dosREPORT.writeBytes(padSTRING('R',L_strTOSTR,21));
							}
					        else if(L_strSTNTP.equals(strTOWOR))
							{
								dosREPORT.writeBytes(padSTRING('R',"Stores From : "+strSTRNM,28));
								dosREPORT.writeBytes(padSTRING('R',"To Work Order: ",15));
								L_strSTRCD=nvlSTRVL(M_rstRSSET.getString("STN_TOSTR"),"");
								String L_strWORNO=nvlSTRVL(M_rstRSSET.getString("STN_WORNO"),"");
								dosREPORT.writeBytes(padSTRING('R',L_strWORNO,17));
							}

							dosREPORT.writeBytes(padSTRING('R',M_strSBSCD.substring(2,4)+"/"+nvlSTRVL(M_rstRSSET.getString("STN_STNNO"),""),18));
							tmpDATE = M_rstRSSET.getDate("STN_STNDT");
							if(tmpDATE != null)
								dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(tmpDATE),15));
							else
								dosREPORT.writeBytes(padSTRING('R',"",15));
							strSTNNO = nvlSTRVL(M_rstRSSET.getString("STN_STNNO"),"");
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 1;
							intSRLNO = 1;
						}
						dosREPORT.writeBytes("\n");
						if(L_strSTNTP.equals(strRGULR))
						{
							dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRMAT"),""),11));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_UOMCD"),""),3));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRDES"),""),46));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRLOC"),""),7));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STN_TRNQT"),""),9));
							L_strSTNVL = nvlSTRVL(M_rstRSSET.getString("STN_STNRT"),"0");
							dosREPORT.writeBytes(padSTRING('L',L_strSTNVL,12));
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',String.valueOf(L_strSPACE),4));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_TOMAT"),""),11));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),3));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),46));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_TOLOC"),""),7));
						
							dosREPORT.writeBytes("\n");
							dblSTNVL +=  Double.parseDouble(L_strSTNVL);
							cl_dat.M_intLINNO_pbst +=2;
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRMAT"),""),11));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_UOMCD"),""),3));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRDES"),""),46));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRLOC"),""),7));
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STN_TRNQT"),""),9));
							L_strSTNVL = nvlSTRVL(M_rstRSSET.getString("STN_STNRT"),"0");
							dosREPORT.writeBytes(padSTRING('L',L_strSTNVL,12));	
							dblSTNVL +=  Double.parseDouble(L_strSTNVL);
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 1;
						}
						cl_dat.M_intLINNO_pbst += 1;
						intSRLNO++;
					}
					L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'ST' AND RM_STRTP ='"
					+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'ST' AND RM_DOCNO = '"+strSTNNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
					System.out.println(L_strSQLQRY);
					L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET!=null)
					{
						if(L_rstRSSET.next())
							strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
					}
					if(L_rstRSSET!=null)
						L_rstRSSET.close();
					dosREPORT.writeBytes("\n");
			        dosREPORT.writeBytes(padSTRING('L',"Total  : "+setNumberFormat(dblSTNVL,2),92));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=2;
					dblSTNVL =0;	
					if(strREMDS.length() > 87)
					{
						dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS.substring(0,87),96));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
						cl_dat.M_intLINNO_pbst++;
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS,96));	
					if(M_rstRSSET!=null)
						M_rstRSSET.close();
				}
				else
				{
					setMSG("Record Not Found..",'E');
				}
			}
			if(P_intRPTOP == 0)
			{
				dosREPORT.writeBytes("\n--------------------------------------------------------------------------------------------\n");
				cl_dat.M_intLINNO_pbst += 2;			
				setMSG("Report completed.. ",'N');			
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	private void prnHEADER(String P_strFMSTN,String P_strTOSTN,int P_intRPTOP)
	{
		try
		{
			String L_strTOSTR="";
			String L_strSTNTP="";
			java.util.Date tmpDATE;			
			strSTRNM = cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString();
			L_strSTNTP=cmbSTNTP.getSelectedItem().toString();
			L_strSTNTP=L_strSTNTP.substring(2,L_strSTNTP.length());
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",96));
			dosREPORT.writeBytes("\n");
			if(P_intRPTOP == 0)
			{
				dosREPORT.writeBytes(padSTRING('R',"Stock Transfer List From : "+P_strFMSTN+" To "+P_strTOSTN,68));
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</b>");
			}
			else
			{
				dosREPORT.writeBytes(padSTRING('R',"Stock Transfer Note ",75));
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</b>");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',"STN Type     : "+L_strSTNTP.trim(),77));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',"STN No./Date : "+M_strSBSCD.substring(2,4)+"/"+nvlSTRVL(M_rstRSSET.getString("STN_STNNO"),""),28));
				cl_dat.M_intLINNO_pbst += 2;
			}
			if(P_intRPTOP == 0)
				dosREPORT.writeBytes(padSTRING('R',"Report Date : "+cl_dat.M_strLOGDT_pbst,25));
			else
			{
				String L_strSTRCD=nvlSTRVL(M_rstRSSET.getString("STN_TOSTR"),"");
				L_strTOSTR=(String)hstCDTRN.get(L_strSTRCD);
				
				tmpDATE = M_rstRSSET.getDate("STN_STNDT");
				if(tmpDATE != null)
					dosREPORT.writeBytes(padSTRING('R',"  "+M_fmtLCDAT.format(tmpDATE),40));
				else
					dosREPORT.writeBytes(padSTRING('R',"  "+cl_dat.M_strLOGDT_pbst,44));
				dosREPORT.writeBytes(padSTRING('R',"Report Date : "+cl_dat.M_strLOGDT_pbst,25));
			}
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 2;
			if(P_intRPTOP == 0)
			{
				dosREPORT.writeBytes(padSTRING('R',"STN Type :"+L_strSTNTP,68));
				dosREPORT.writeBytes(padSTRING('R',"Page No     : "+cl_dat.M_PAGENO,25));
			}
			else
			{
				if(cmbSTNTP.getSelectedItem().toString().substring(0,2).equals(strRGULR))
				{
					dosREPORT.writeBytes(padSTRING('R',"Stores From  : "+strSTRNM,28));
					dosREPORT.writeBytes(padSTRING('R',"To "+L_strTOSTR,40));
				}
				if(cmbSTNTP.getSelectedItem().toString().substring(0,2).equals(strFRWOR))
				{
					dosREPORT.writeBytes(padSTRING('R',"From W/O     : "+nvlSTRVL(M_rstRSSET.getString("STN_WORNO"),""),28));
					dosREPORT.writeBytes(padSTRING('R',"To Store :"+L_strTOSTR,40));
				}
				if(cmbSTNTP.getSelectedItem().toString().substring(0,2).equals(strTOWOR))
				{
					dosREPORT.writeBytes(padSTRING('R',"Stores From  : "+strSTRNM,28));
					dosREPORT.writeBytes(padSTRING('R',"To Work Order "+nvlSTRVL(M_rstRSSET.getString("STN_WORNO"),""),40));
				}
				dosREPORT.writeBytes(padSTRING('R',"Page No     : "+cl_dat.M_PAGENO,21));
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			if(P_intRPTOP == 0)
			{
				dosREPORT.writeBytes("                                                                STN Number          STN Date     ");
				dosREPORT.writeBytes("\n");
				if(cmbSTNTP.getSelectedItem().toString().substring(0,2).equals(strRGULR))
				{
					dosREPORT.writeBytes("Sr. From Mat Code  UOM  Description                             FR Locn  TRN.Qty       Value");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("No. To Mat Code    UOM  Description                             To Locn       ");
					cl_dat.M_intLINNO_pbst += 1;
				}
				else
				{
					dosREPORT.writeBytes("Sr. Material Code  UOM  Description                             Location TRN.Qty       Value");
				}
				cl_dat.M_intLINNO_pbst += 3;
			}
			else
			{
				if(cmbSTNTP.getSelectedItem().toString().substring(0,2).equals(strRGULR))
				{
					dosREPORT.writeBytes("Sr. From Mat Code  UOM    Description                                   Fr Locn      Trn.Qty");
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("No. To Mat Code    UOM    Description                                   TO Locn           ");
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 2;
				}
				else
				{
					dosREPORT.writeBytes("Sr. Material Code  UOM    Description                                   Location     Trn.Qty");
				}
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");

			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
			cl_dat.M_intLINNO_pbst += 3;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPHDR");
		}
	}
	/**
	 * Metod to fetch STN Details & to include it in the Report.
	*/	
	 public void prnSTNNT(String P_strFMSTN,String P_strTOSTN)
	{
		try
		{
			String L_strSQLQRY ="";
			
			if(M_rstRSSET != null)
			{
				flgFIRST = false;
				strREMDS = "";
				String L_strSPACE="";
				while(M_rstRSSET.next())
				{
					if(cl_dat.M_intLINNO_pbst >62)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER(P_strFMSTN,P_strTOSTN,1);
					}
					if(!strSTNNO.equals(nvlSTRVL(M_rstRSSET.getString("STN_STNNO"),"")))
					{
						if(flgFIRST)
						{
							strREMDS = "";
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
							L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'ST' AND RM_STRTP ='"
								+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'ST' AND RM_DOCNO = '"+strSTNNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
							ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
							if(L_rstRSSET!=null)						
							{
								if(L_rstRSSET.next())
									strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
							}
							if(L_rstRSSET!=null)				
								L_rstRSSET.close();
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;
							if(strREMDS.length() > 87)
							{
								dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS.substring(0,87),96));
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
								cl_dat.M_intLINNO_pbst++;
							}
							else
								dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS,96));		
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=2;
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						}
						flgFIRST = true;
						prnHEADER(P_strFMSTN,P_strTOSTN,1);
						strSTNNO = nvlSTRVL(M_rstRSSET.getString("STN_STNNO"),"");
						intSRLNO = 1;
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO = 1;
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
					if(cmbSTNTP.getSelectedItem().toString().substring(0,2).equals(strRGULR))
					{
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRMAT"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRDES"),""),54));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRLOC"),""),8));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STN_TRNQT"),""),12));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',String.valueOf(L_strSPACE),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_TOMAT"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),54));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_TOLOC"),""),8));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 2;
					}					
					else
					{
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRMAT"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRDES"),""),54));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STN_FRLOC"),""),8));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STN_TRNQT"),""),12));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
					}
					cl_dat.M_intLINNO_pbst += 1;
					intSRLNO++;
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
				L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'ST' AND RM_STRTP ='"
					+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'ST' AND RM_DOCNO = '"+strSTNNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET!=null)							
				{
					strREMDS = "";
					if(L_rstRSSET.next())
						strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
				}
				if(L_rstRSSET!=null)							
					L_rstRSSET.close();
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 2;
				if(strREMDS.length() > 87)
				{
					dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS.substring(0,87),96));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
					cl_dat.M_intLINNO_pbst++;
				}
				else
					dosREPORT.writeBytes(padSTRING('R',"Remarks : "+strREMDS,96));		
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
					prnFMTCHR(dosREPORT,M_strEJT);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnSTNNT");
		}
	}
}	

	
	
	
	
	
