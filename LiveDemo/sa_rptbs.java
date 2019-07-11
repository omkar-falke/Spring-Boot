/*
System Name    : System Administration System
Program Name   : Table structure details
Program Desc.  : Report for spldata database tables structure Details
Author         : Mrs.Dipti S Shinde
Date           : 18th July 2005
Version        : v2.0.0
Modificaitons  : 
Modified By    :
Modified Date  : 
Modified det.  :
Version        :
*/
import java.sql.*;
import javax.swing.*;
import java.awt.event.ActionEvent; 
import java.awt.event.MouseEvent;
import java.io.FileOutputStream; 
import java.io.DataOutputStream; 
import java.util.Vector;

//----------------------------------------------------------------
/**<pre>
System Name : System Admin.
 
Program Name :Table Structure printing for spldata related tables       

Purpose : This program will generate a Report for Table Structure details
          1)Single:-Table structures will be printed in single File.
          2)Seperate:-Structures will be printed in Different files
            (Name same as the name of Table).

List of tables used :
Table Name      Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
 spldata - all tables are used.                                         *
-------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name   Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
lstTBLFR - this list shows all tables from spldata
lstTBLTO - this list shows all or selected tables from lstTBLFR.         
rdbALL   - Option for taking all tables to lstTBLTO.
rdbSPEFC - Option for selecting tables from lstTBLFR to lstTBLTO. 
rdbSINGL - table Structure to be generated in single file.
rdbSEPRT - table structure to be generated in separate file.                             
-------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 
all spldata table used.

<B>Conditions Give in Query:</B>
   A simple select query is fired for each table in List 2 and Result Set Meta data is taken.
   Table structure is printed by taking details from resultset meta data.
<B>Validations :</B>
  - with out selected table name from list report will not generate.
</I> */
/*----------------------------------------------------------------------------------------------------------------------------------------*/
public class sa_rptbs extends cl_rbase
{                                                           /** String for geting value From Selected List Vlaue */
	private String strSELVAL="";                            /** String for generating file from table name */
	private String strTBLFL="";                             /** Datametadata for fetching Table name from main spldata */
	private DatabaseMetaData dmdTBLNM;                      /** redio button for all table name taken from lstTBLFR to lstTBLTO */
	private JRadioButton rdbALL;                            /** redio button for selected table name by user taken from lstTBLFR to lstTBLTO */
	private JRadioButton rdbSPEFC;                          /** redio button for generating report in single file */
	private JRadioButton rdbSINGL;                          /** redio button for generating report in separate file */
	private JRadioButton rdbSEPRT;                          /** lstTBLFR list for getting all Table name from spldata */
	private JList lstTBLFR;                                 /** lstTBLTO list for getting selected table name from list lstTBLFR */
	private JList lstTBLTO;                                 /** vector for value of table name */
	private Vector<String> vtrTBLNM = new Vector<String>();                 /** vector for temparary values of table name */
	private Vector<String> vtrTEMPF = new Vector<String>();                 /** buttons for addind table name and remove table name */
	private JButton btnADD,btnREMOVE;                       /** check box for page break on report table wise */
	private JCheckBox chbPGBRK;                             /** Button group for rdbSINGL and rdbSEPRT file generation */
	private ButtonGroup btgFILGN;                           /** Button group for rdbALL and rdbSPEFC in Selection of Table Structures */
	private ButtonGroup btgSELTN;                           /** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	                    /** Data output Stream for generating Report File.*/
	private DataOutputStream dosREPORT ;                    /** String for generating table name */
	private String strFILNM="";                             /** integer for reccord count in data base */           
	private int intRECCT=0;                                 /** list model for lstTABLTO */
	private ListModel lstMODEL;                             /** label for file generation option on screen */
	private JLabel lblFILEG;                                /** label for table Structure label on Screen */
	private JLabel lblTABLE;								/** Text box for report file name to be printed through DOS mode */
	private JTextField txtFILNM;							/** Check box for listing of File Name and Key Constraints */
	private JCheckBox chbSUMFL;							
	private java.util.Hashtable<String,String> hstCOMNT = new java.util.Hashtable<String,String>();
	sa_rptbs()
	  {  
		 super(2);
		 setCursor(cl_dat.M_curWTSTS_pbst);
		 try
		 {
			setMatrix(20,6);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			String L_strTBLTP[] = new String[]{"TABLE"};
			strFILNM ="";
			dmdTBLNM = cl_dat.M_conSPDBA_pbst.getMetaData();
			ResultSet rstTBLNM = dmdTBLNM.getTables("SPLWS01","spldata",null,L_strTBLTP);
			while(rstTBLNM.next())
			{
				strFILNM = nvlSTRVL(rstTBLNM.getString("TABLE_NAME"),"");
				vtrTBLNM.addElement(strFILNM);
			}
			rstTBLNM.close();
			
			btgFILGN = new ButtonGroup();
			btgSELTN = new ButtonGroup();
			add(lblTABLE=new JLabel("Table Structure"),2,2,1,2,this,'L');
			add(rdbALL =  new JRadioButton("ALL",true),2,3,1,1,this,'R');
			add(rdbSPEFC =  new JRadioButton("Specific",false),2,4,1,1,this,'R');
			add(new JLabel("File Name"),1,5,1,1,this,'L');
			add(txtFILNM =  new JTextField(),2,5,1,1,this,'L');
			add(chbSUMFL =  new JCheckBox("File Name List"),4,5,1,1,this,'L');
			btgSELTN.add(rdbSPEFC);
			btgSELTN.add(rdbALL);
			
			add(lblFILEG=new JLabel("File Genaration"),3,2,1,2,this,'L');
			add(rdbSINGL = new JRadioButton("Single file",true),3,3,1,1,this,'R');
			add(rdbSEPRT =  new JRadioButton("Separate",false),3,4,1,1,this,'R');
			btgFILGN.add(rdbSINGL);
			btgFILGN.add(rdbSEPRT);
			add(chbPGBRK = new JCheckBox("set Page Break",false),3,5,1,1,this,'L');
			
			lstTBLFR = new JList();
			//lstTBLFR = new JList(vtrTBLNM);
			lstTBLTO = new JList();
			add(btnADD = new JButton("ADD"),6,3,1,0.5,this,'R');
			add(btnREMOVE = new JButton("DEL"),7,3,1,0.5,this,'R');
		
			JScrollPane jspTBLFR = new JScrollPane(lstTBLFR);
			JScrollPane jspTBLTO = new JScrollPane(lstTBLTO);
			add(jspTBLFR,5,2,12,1.5,this,'L');
			add(jspTBLTO,5,4,12,1.5,this,'L');
			
			lstTBLFR.addMouseListener(this);
			lstTBLTO.addMouseListener(this);
			btnADD.addMouseListener(this);
			btnREMOVE.addMouseListener(this);
			
			M_pnlRPFMT.setVisible(true);		
	 		setENBL(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_EX)
			{
				System.out.println("Inside Constructor"+L_EX);
			}
		}
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
		void setENBL(boolean L_flgSTAT)
		{
			super.setENBL(L_flgSTAT);	
			{
			 lstTBLFR.setEnabled(L_flgSTAT);
			 lstTBLTO.setEnabled(L_flgSTAT);
			 rdbSEPRT.setEnabled(L_flgSTAT);
			 rdbALL.setEnabled(L_flgSTAT);
			 rdbSPEFC.setEnabled(L_flgSTAT);
			 rdbSINGL.setEnabled(L_flgSTAT);
			 chbPGBRK.setEnabled(L_flgSTAT);
			 chbSUMFL.setEnabled(L_flgSTAT);
			 lblFILEG.setEnabled(L_flgSTAT);
			 lblTABLE.setEnabled(L_flgSTAT);
			 txtFILNM.setEnabled(L_flgSTAT);
			}
		}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			{
				setMSG("Select an Option..",'E');
				clrCOMP();
				setENBL(false);
				cl_dat.M_cmbOPTN_pbst.requestFocus();
			}			
			else
			{
				setENBL(true);
				lstTBLFR.setListData(vtrTBLNM);
				rdbSPEFC.requestFocus();
				setMSG(" select from List..",'N');
			}
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			setMSG("please select the Structure Format you want ALL,Specific",'N');
			rdbSPEFC.requestFocus();
		}
		if(M_objSOURC ==rdbSPEFC)
		{
			setMSG("select the Table Structure type",'N');
			rdbSINGL.requestFocus();
			//lstTBLTO.setListData(new Vector());
			//vtrTEMPF.removeAllElements();
		}
		if(M_objSOURC ==rdbSINGL)
		{
			setMSG("select the file generation type",'N');
			rdbSEPRT.requestFocus();
		}
		if(M_objSOURC ==rdbSEPRT)
		{
			setMSG("select the file generation type",'N');
			chbPGBRK.requestFocus();
		}
		if(M_objSOURC ==lstTBLFR)
		{
			setMSG("ADD the record",'N');
			btnADD.requestFocus();
		}
    	 if(M_objSOURC == rdbALL)
		{
			lstTBLTO.setListData(vtrTBLNM);
			vtrTEMPF.removeAllElements();
		}
		 
		if(M_objSOURC == btnADD)
		{
			if(rdbSPEFC.isSelected())
			{
				strSELVAL = lstTBLFR.getSelectedValue().toString().trim();
				vtrTEMPF.remove(strSELVAL);
				vtrTEMPF.addElement(strSELVAL);
				lstTBLTO.setListData(vtrTEMPF);
			}
		}
	}
	public void mouseClicked(MouseEvent L_ME)
	{
	    
		if(M_objSOURC == lstTBLFR)
		{
			if(rdbSPEFC.isSelected())
			{
				if(L_ME.getClickCount() == 2)
				{
					strSELVAL = lstTBLFR.getSelectedValue().toString().trim();
					vtrTEMPF.remove(strSELVAL);
					vtrTEMPF.addElement(strSELVAL);
					lstTBLTO.setListData(vtrTEMPF);
				}
			}
		}
		if(M_objSOURC == btnREMOVE)
		{
			if(rdbSPEFC.isSelected())
			{
			    lstTBLTO.setListData(new Vector());
				vtrTEMPF.removeAllElements();
				//vtrTEMPF.addElement(lstTBLTO.getSelectedValue().toString().trim());
				//vtrTEMPF.removeAllElements();
				//vtrTEMPF.removeElement(lstTBLTO.getSelectedValue());
				//vtrTEMPF.removeElementAt(lstTBLTO.getSelectedValue());
			}
		}
	}
	
	
	/**
	Method to validate  DATA before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			System.out.println("x "+lstTBLTO.getComponentCount());
			System.out.println("x "+vtrTEMPF.size());
		 /*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			 String temp="";
				for(int i = 0; i < lstTBLTO.setListData(i); i++)
				{
					temp=lstMODEL.getElementAt(i).toString().trim();
					
					
				}
				if(temp.toString().length()==0)
					{
					
						setMSG("you r not selected list",'E');
						return false;
					}
			}*/
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
		return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	
	/**
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;		
	    try
		{
			cl_dat.M_PAGENO =0;
			cl_dat.M_intLINNO_pbst = 0;
			String L_strFILNM = txtFILNM.getText().length()>0 ? txtFILNM.getText() : "sa_rptbs";
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst +L_strFILNM +".html";				
			else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst +L_strFILNM +".doc";
			if(txtFILNM.getText().length()==0)
			{
				getDATA(strFILNM);
				if(intRECCT ==0)
				{
					setMSG("Data not found..",'E');
					return;
				}
				if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	if(!rdbSEPRT.isSelected())
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
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			   if(!rdbSEPRT.isSelected())
			   {
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					if(M_rdbHTML.isSelected())
					   p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
					else
    					p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			   }
			}	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				if(!rdbSEPRT.isSelected())
				{
					cl_eml ocl_eml = new cl_eml();				    
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Table structure "," ");
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					}
				}
			 }
		}
		catch(Exception L_EX)
		{
		setMSG(L_EX, "exeprint");
		}
	}
	
	/**
	 */
	private void getDATA(String P_strFILNM)
	{
		try
		{
			intRECCT = 0;
			lstMODEL = lstTBLTO.getModel();
			if(rdbSINGL.isSelected())
			//strTBLFL = cl_dat.M_strREPSTR_pbst+ P_strFILNM;
			strTBLFL = P_strFILNM;
			fosREPORT = new FileOutputStream(strTBLFL);
			dosREPORT = new DataOutputStream(fosREPORT);
			ResultSetMetaData rmdCOLMN;
			ResultSet rstPRMKY;
			String L_strCOLNM,L_strCOLTP;
			String L_strPRMKY;
			String L_strTEMPS;
			String L_strSEPRT;
			
			int L_COLNL;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Table Structuer</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				 
			}
			String L_strTEMP ="";
			for(int i = 0; i < lstMODEL.getSize(); i++)
			{
				P_strFILNM = lstMODEL.getElementAt(i).toString().trim();
				if(i ==0)
				{
				    L_strTEMP = "IN('"+P_strFILNM+"'";
				}
				else
				    L_strTEMP += ",'"+P_strFILNM+"'";
			}	
			L_strTEMP += ")"; 
			M_strSQLQRY = "Select TABLE_NAME,TABLE_TEXT from SYSTABLES WHERE TABLE_NAME "+ L_strTEMP;
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			    while(M_rstRSSET.next())
			    {
			        hstCOMNT.put(nvlSTRVL(M_rstRSSET.getString("TABLE_NAME"),""),nvlSTRVL(M_rstRSSET.getString("TABLE_TEXT"),"-"));
			    }
			    M_rstRSSET.close();
			}

			M_strSQLQRY = "Select SYSTEM_TABLE_NAME,SYSTEM_COLUMN_NAME,COLUMN_TEXT from SYSCOLUMNS WHERE SYSTEM_TABLE_NAME "+ L_strTEMP;
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			    while(M_rstRSSET.next())
			    {
			        hstCOMNT.put(nvlSTRVL(M_rstRSSET.getString("SYSTEM_TABLE_NAME"),"")+"|"+nvlSTRVL(M_rstRSSET.getString("SYSTEM_COLUMN_NAME"),""),nvlSTRVL(M_rstRSSET.getString("COLUMN_TEXT"),"-"));
			    }
			    M_rstRSSET.close();
			}
			for(int i = 0; i < lstMODEL.getSize(); i++)
			{
				P_strFILNM = lstMODEL.getElementAt(i).toString().trim();
					
				if(rdbSEPRT.isSelected())
				{
					if(M_rdbHTML.isSelected())
					{
						strTBLFL = cl_dat.M_strREPSTR_pbst+ P_strFILNM+".html";
						dosREPORT.writeBytes("<HTML><HEAD><Title>Table Structuer</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
						dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				 
					}
					if(M_rdbTEXT.isSelected())
						strTBLFL = cl_dat.M_strREPSTR_pbst+ P_strFILNM+".doc";
						fosREPORT = new FileOutputStream(strTBLFL);
						dosREPORT = new DataOutputStream(fosREPORT);
						setMSG("file has been created in  C:\\reports  by table name",'N');
						System.out.println("reccord "+intRECCT);
						intRECCT++;
					/*if(M_rdbHTML.isSelected())
					{
					    dosREPORT.writeBytes("<HTML><HEAD><Title>Table Structuer</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
						dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				 
					}*/
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					prnFMTCHR(dosREPORT,M_strNOCPI17);
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strCPI12);
				}
				if(M_rdbHTML.isSelected())
				{
					dosREPORT.writeBytes("<HTML><HEAD><Title> Table Structure</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
					dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				 
				}	
			
				if(chbSUMFL.isSelected())
					prnSUMSTR(P_strFILNM);
				else 
					prnDTLSTR(P_strFILNM);
				if((M_rdbTEXT.isSelected()) && (chbPGBRK.isSelected()) && (!chbSUMFL.isSelected()))
				{
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strEJT);				
				}			
				if(M_rdbHTML.isSelected())
				{
				 dosREPORT.writeBytes("<P CLASS = \"breakhere\">");    
				}
					setCursor(cl_dat.M_curDFSTS_pbst);
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			if(rdbSINGL.isSelected())
				strTBLFL = cl_dat.M_strREPSTR_pbst+ P_strFILNM;
				//strTBLFL =P_strFILNM;
				
			if(dosREPORT!=null)	
				dosREPORT.close();
			if(fosREPORT!=null)	
				fosREPORT.close();
		}
		catch(Exception L_EX)
		{
		setMSG(L_EX,"getdata");
		}
	}


	/**
	 */
	private void prnDTLSTR(String P_strFILNM)
	{
		try
		{
				strTBLFL = P_strFILNM;
				ResultSetMetaData rmdCOLMN;
				ResultSet rstPRMKY;
				String L_strCOLNM,L_strCOLTP;
				String L_strPRMKY;
				String L_strTEMPS;
				String L_strSEPRT;
				int L_COLNL;
				intRECCT++;
				setMSG("Structure printing for " + P_strFILNM + " in progress..",'N');
				M_strSQLQRY = "Select * from "+ P_strFILNM;
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);		
					if(M_rstRSSET != null)
					{
						rmdCOLMN = M_rstRSSET.getMetaData();
						int cnt = rmdCOLMN.getColumnCount();
							dosREPORT.writeBytes(padSTRING('R',"Create table "+P_strFILNM+" ( ",50));
							if(hstCOMNT.containsKey(P_strFILNM))
							  dosREPORT.writeBytes("-- "+hstCOMNT.get(P_strFILNM).toString());
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst+=1;
							for(int k = 1;k <= cnt;k++)
							{
								L_strCOLNM = rmdCOLMN.getColumnName(k);
								L_strCOLTP = rmdCOLMN.getColumnTypeName(k);
								L_COLNL = rmdCOLMN.isNullable(k);
								L_strTEMPS = L_strCOLNM+"     "+L_strCOLTP;
								if(L_strCOLTP.equals("VARCHAR"))
								{
									L_strTEMPS += "("+rmdCOLMN.getColumnDisplaySize(k)+")";
									if(L_COLNL == 0)
										L_strTEMPS += "   not null";
								}
								else if(L_strCOLTP.equals("DATE"))
								{
								}
								else if(L_strCOLTP.equals("TIMESTAMP"))
								{
								}
								else if(L_strCOLTP.equals("DECIMAL"))
								{
									L_strTEMPS += "("+rmdCOLMN.getPrecision(k);
									if(rmdCOLMN.getScale(k) > 0)
									{
										L_strTEMPS += ","+rmdCOLMN.getScale(k)+")";
									}
									else
										L_strTEMPS += ")";
									if(L_COLNL == 0)
										L_strTEMPS += "   not null default 0";
								}
								L_strTEMPS += ",";
								L_strTEMPS = padSTRING('R',L_strTEMPS,50);
								if(hstCOMNT.containsKey(P_strFILNM+"|"+L_strCOLNM))
								    L_strTEMPS += "-- "+hstCOMNT.get(P_strFILNM+"|"+L_strCOLNM).toString();
								
								dosREPORT.writeBytes(L_strTEMPS+"\n");
								cl_dat.M_intLINNO_pbst+=1;
							}
							dmdTBLNM = cl_dat.M_conSPDBA_pbst.getMetaData();
							rstPRMKY = dmdTBLNM.getPrimaryKeys("SPLWS01","spldata",P_strFILNM);
							L_strPRMKY = " Constraint "+P_strFILNM+" Primary Key (";
							L_strSEPRT = "";
							while(rstPRMKY.next())
							{
								L_strPRMKY += L_strSEPRT + rstPRMKY.getString("COLUMN_NAME");
								L_strSEPRT = ",";
							}
							rstPRMKY.close();
							L_strPRMKY += "))";
							dosREPORT.writeBytes(L_strPRMKY+"\n");
							cl_dat.M_intLINNO_pbst+=1;
							if(chbPGBRK.isSelected())
							{
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("------------------------------------------------------------------------------------\n");
								cl_dat.M_intLINNO_pbst+=2;
								
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))						
								{
									prnFMTCHR(dosREPORT,M_strCPI10);
									prnFMTCHR(dosREPORT,M_strEJT);
								}
							}
							if(!chbPGBRK.isSelected())
							 {
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("------------------------------------------------------------------------------------\n");
								cl_dat.M_intLINNO_pbst+=2;
							 }
							if(cl_dat.M_intLINNO_pbst> 63)
							{
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("------------------------------------------------------------------------------------\n");
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>"); 
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))						
								{
									prnFMTCHR(dosREPORT,M_strCPI10);
									prnFMTCHR(dosREPORT,M_strEJT);
								}
								cl_dat.M_intLINNO_pbst = 0;
							}
							if(rdbSEPRT.isSelected())
							{
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
						
								if(dosREPORT!=null)	
									dosREPORT.close();
								if(fosREPORT!=null)	
									fosREPORT.close();
							}
					}
					M_rstRSSET.close();
					setMSG("Report generation completed",'N');		
		}
		catch(Exception L_EX)
		{
		setMSG(L_EX,"prnDTLSTR");
		}
	}


	/**
	 */
	private void prnSUMSTR(String P_strFILNM)
	{
		try
		{
				strTBLFL = P_strFILNM;
				ResultSetMetaData rmdCOLMN;
				ResultSet rstPRMKY;
				String L_strCOLNM,L_strCOLTP;
				String L_strPRMKY;
				String L_strTEMPS;
				String L_strSEPRT;
				int L_COLNL;
				intRECCT++;
				setMSG("Table List  for " + P_strFILNM + " in progress..",'N');
				M_strSQLQRY = "Select * from "+ P_strFILNM;
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);		
					if(M_rstRSSET != null)
					{
						rmdCOLMN = M_rstRSSET.getMetaData();
						int cnt = rmdCOLMN.getColumnCount();
							dosREPORT.writeBytes(padSTRING('R',P_strFILNM,20));
							if(hstCOMNT.containsKey(P_strFILNM))
							  dosREPORT.writeBytes(hstCOMNT.get(P_strFILNM).toString());
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst+=1;
							dmdTBLNM = cl_dat.M_conSPDBA_pbst.getMetaData();
							rstPRMKY = dmdTBLNM.getPrimaryKeys("SPLWS01","spldata",P_strFILNM);
							L_strPRMKY = padSTRING('R',"--------	",20)+"Constraint "+P_strFILNM+" Primary Key (";
							L_strSEPRT = "";
							while(rstPRMKY.next())
							{
								L_strPRMKY += L_strSEPRT + rstPRMKY.getString("COLUMN_NAME");
								L_strSEPRT = ",";
							}
							rstPRMKY.close();
							L_strPRMKY += "))";
							dosREPORT.writeBytes(L_strPRMKY+"\n");
							cl_dat.M_intLINNO_pbst+=1;
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst+=1;
							if(cl_dat.M_intLINNO_pbst> 63)
							{
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes("------------------------------------------------------------------------------------\n");
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>"); 
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))						
								{
									//prnFMTCHR(dosREPORT,M_strCPI10);
									prnFMTCHR(dosREPORT,M_strEJT);
								}
								cl_dat.M_intLINNO_pbst = 0;
							}
					}
					M_rstRSSET.close();
					setMSG("Report generation completed",'N');		
		}
		catch(Exception L_EX)
		{
		setMSG(L_EX,"prnSUMSTR");
		}
	}

}
