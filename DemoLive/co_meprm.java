/*
System Name   : 
Program Name  : Product Code Entry Form
Program Desc. : Entry for new product codes within product master table is done through this form.
Author        : Mr. Deepal N. Mehta
Date          : 03rd July 2003
Version       : 

Modificaitons : 

Modified By    :
Modified Date  :
Modified det.  :
Version        :


               xx         xx           xx                 x          xx         x
              MANTP      SUBTP        CATGY             PRSTS      SRLNO      QLTFL

             COXXPGR     COXXPGR     PRXXFLT (MG:54)   PRXXSTS                PRXXQLT
            xx0000000A  xxxx00000A   SUBSTRING(pr_prdcd,5,2)
                                     PRXXCST (MG :SX)
            

*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class co_meprm extends cl_pbase implements ActionListener
{
	
	JButton		btnMEQPM = new JButton("Press to enter Quality Parmeter Specification"); 
	JTextField	txtPRDDS = new TxtLimit(45),
				txtPRDCD = new TxtNumLimit(10),
				txtMANTP = new TxtLimit(2),
				txtSUBTP = new TxtNumLimit(2),
				txtCATGY = new TxtNumLimit(2),
				txtPRSTS = new TxtLimit(1),
				txtSRLNO = new TxtLimit(2),
				txtMATTP = new TxtNumLimit(1),
				txtUOMCD = new TxtLimit(3);
				
	JComboBox	cmbMANTP = new JComboBox(),
				cmbSUBTP = new JComboBox(),
				cmbCATGY = new JComboBox(),
				cmbPRSTS = new JComboBox(),
				cmbSRLNO = new JComboBox(),
				cmbMATTP = new JComboBox(),
				cmbUOMCD = new JComboBox();
	JList		lstPRDCD = new JList();
	
	String strMANTP,strSUBTP,strPRDDS,strCATGY,strPRSTS,strSRLNO,strMATTP, strWHERE;
	String strSTSFL,strUOMCD,strPRDTP;
	String strPRDCD = "";
	boolean flgCHK_EXIST;	

	String	strMANTP1 = "Select Main Type",
			strSUBTP1 = "Select Sub Type",
			strCATGY1 = "Select Category",
			strPRSTS1 = "Select Series",
			strSRLNO1 = "Select Serial No.",
			strMATTP1 = "Select Material Type",
			strUOMCD1 = "Select UOM",
			strMBATDS ="";
			//strQLTFL = "Select Grade Quality";
	// Starting location of the actual code in Hash Table Key
	private int intMANLC = 0;
	private int intSUBLC = 2;
	private int intCATLC = 4;
	private int intPRSLC = 2;
	private int intSRLLC = 0;
	private int intMATLC = 2;
	private int intUOMLC = 0;
	
	//private Object[] arrHSTKEY;		// Object array for getting hash table key in sorted order
	Hashtable<String,String> hstPRDTP = new Hashtable<String,String>(),
				hstMANTP = new Hashtable<String,String>(),
				hstSUBTP = new Hashtable<String,String>(),									   
				hstCATGY = new Hashtable<String,String>(),
				hstPRSTS = new Hashtable<String,String>(),									   
				hstSRLNO = new Hashtable<String,String>(),									   
				hstMATTP = new Hashtable<String,String>(),
				hstUOMCD = new Hashtable<String,String>();
										 //hashtable will contain vector for displaying sub type
	private Hashtable<String,String[]> hstCDTRN;			// Details of all codes used in program
	private Hashtable<String,String> hstCODDS;			// Code No. from Code Description
	private Hashtable<String,String[]> hstPRCAT;			// Product Category Details
	private Hashtable<String,String[]> hstPRMST;			// Product Master Details

	Vector<String> vtrPRDLS = new Vector<String>();  

	private Vector vtrPRDTP1;
		
	private int intCDTRN_TOT = 9;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
											/** Array elements for Product Category */
	private int intPRCAT_TOT = 2;
    private int intAE_PR_PRCAT = 0;		
    private int intAE_PR_CATDS = 1;		
											/** Array elements for Product Master */
	private int intPRMST_TOT = 2;
    private int intAE_PR_PRDCD = 0;		
    private int intAE_PR_PRDDS = 1;		
										/** Variables for Code Transaction Table
										*/
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	private String strPRCAT;		
	private String strPRDCD1;		

	ResultSet L_RSLSET;
	
	
	boolean flgPRDEXS = false;	
	co_meprm()
	{
		super(2);
		try
		{
			hstCDTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstPRCAT = new Hashtable<String,String[]>();
			hstPRMST = new Hashtable<String,String[]>();

			vtrPRDTP1 = new Vector();
			setMatrix(18,6);
			add(new JLabel("Grade"),2,1,1,1,this,'L');
			add(txtPRDDS,2,2,1,1.75,this,'L');
			add(new JLabel("Available Product "),2,4,1,1,this,'L');
			add(new JLabel("Code & Grade : "),3,4,1,1,this,'L');
			
			add(new JLabel("Code"),3,1,1,1,this,'L');
			add(txtPRDCD,3,2,1,1.75,this,'L');
			
			JScrollPane jspPRDCD = new JScrollPane(lstPRDCD);
			add(jspPRDCD,1,5,14,2,this,'L');
			
			add(new JLabel("Main Type : "),4,1,1,1,this,'L');
			add(cmbMANTP,4,2,1,1.75,this,'L');
			add(txtMANTP,4,4,1,0.25,this,'L');
			
			add(new JLabel("Sub Type : "),5,1,1,1,this,'L');
			add(cmbSUBTP,5,2,1,1.75,this,'L');
			add(txtSUBTP,5,4,1,0.25,this,'L');
			add(new JLabel("Category : "),6,1,1,1,this,'L');
			add(cmbCATGY,6,2,1,1.75,this,'L');
			add(txtCATGY,6,4,1,0.25,this,'L');
			add(new JLabel("Series : "),7,1,1,1,this,'L');
			add(cmbPRSTS,7,2,1,1.75,this,'L');
			add(txtPRSTS,7,4,1,0.25,this,'L');
			add(new JLabel("Serial No. : "),8,1,1,1,this,'L');
			add(cmbSRLNO,8,2,1,1.75,this,'L');
			add(txtSRLNO,8,4,1,0.25,this,'L');
			add(new JLabel("Material Type : "),9,1,1,1,this,'L');
			add(cmbMATTP,9,2,1,1.75,this,'L');
			add(txtMATTP,9,4,1,0.25,this,'L');
			add(new JLabel("UOM : "),10,1,1,1,this,'L');
			add(cmbUOMCD,10,2,1,1.75,this,'L');
			add(txtUOMCD,10,4,1,0.25,this,'L');
			add(new JLabel("Grade Quality : "),11,1,1,1,this,'L');

			add(btnMEQPM,12,1,2,3,this,'L');

			cmbMANTP.addItem(strMANTP1);
			cmbSUBTP.addItem(strSUBTP1);
			cmbCATGY.addItem(strCATGY1);
			cmbPRSTS.addItem(strPRSTS1);
			cmbSRLNO.addItem(strSRLNO1);
			cmbMATTP.addItem(strMATTP1);
			cmbUOMCD.addItem(strUOMCD1);
		
			String L_MANTP1 = "";
			String L_SUBTP1 = "";
			crtCDTRN("'MSTPRXXQLT','MSTPRXXSTS','MSTCOXXUOM', 'MSTPRXXFLT','MSTCOXXPGR', 'MSTPRXXCST', 'MSTPRXXFSF' ","",hstCDTRN);
			crtPRCAT();
			crtPRMST();
		
			setHST_GRP("MSTCOXXPGR","MG",hstMANTP);
			setCMBVL_GRP(cmbMANTP,hstMANTP,strMANTP1,intMANLC,strMANTP,"MG");
		
			setHST_CAT("MSTCOXXUOM",hstUOMCD);
			setCMBVL_CAT(cmbUOMCD,hstUOMCD,strUOMCD1,intUOMLC,strUOMCD);
		}
		catch(Exception E)
		{
			setMSG(E, "Constructor");
			
		}	
	}


	
	/**
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			//Object L_SRC = L_AE.getSource();
			setMSG("",'N');
			if(M_objSOURC == txtPRDDS)
				dspPRDDTL();
			/*if(L_SRC == cmbOPTN || L_SRC == btnUNDO)
			{
				exeINTLCMB();
				//setCMBENBL();
				txtPRDDS.requestFocus();
			}
			
			*/
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				System.out.println("In ACTIONP ");
				txtPRDDS.requestFocus();
			}
			else if(M_objSOURC == cmbMANTP)
			{
				strMANTP = setSUBSTR(getCMBVL(cmbMANTP, hstMANTP,"MANTP"),intMANLC);
				//system.out.println("strMANTP : "+strMANTP);
				txtMANTP.setText(strMANTP);
				exeINTLTXT("MANTP");
				setHST_GRP("MSTCOXXPGR","SG",hstSUBTP);
				setCMBVL_GRP(cmbSUBTP,hstSUBTP,strSUBTP1,intSUBLC,strSUBTP,"SG");

			}
			else if(M_objSOURC == cmbSUBTP)
			{
				strSUBTP = setSUBSTR(getCMBVL(cmbSUBTP, hstSUBTP,"SUBTP"),intSUBLC);
				txtSUBTP.setText(strSUBTP);
				exeINTLTXT("SUBTP");
				if(strMANTP.equals("54"))
				{
					setHST_CAT_54("MSTPRXXFLT",hstCATGY);
					strMBATDS = getSUBCT(strMANTP+strSUBTP);
					//System.out.println(strMBATDS);
				    if(chkEXIST("CO_PRMST", "PR_PRDDS = '" +txtPRDDS.getText().trim()+" "+strMBATDS +"'" ))
					{
						//System.out.println("Already exists");
				//shubham		JOptionPane.showMessageDialog(this,"Product Code alreay exists");
					}
				}
				else if(strMANTP.equals("SX"))
				{
					setHST_CAT_SX("MSTPRXXCST",hstCATGY);
					//strMBATDS = getSUBCT(strMANTP+strSUBTP);
					//System.out.println(strMBATDS);
				    if(chkEXIST("CO_PRMST", "PR_PRDDS = '" +txtPRDDS.getText().trim()+"'" ))
					{
						//System.out.println("Already exists");
				//shubham		JOptionPane.showMessageDialog(this,"Product Code alreay exists");
					}
				}
				else
					setHST_CAT_XX(hstCATGY);
				setCMBVL_MTP1(cmbCATGY,hstCATGY,strCATGY1,intCATLC,strCATGY);
			}
			else if(M_objSOURC == cmbCATGY)
			{
				strCATGY = setSUBSTR(getCMBVL(cmbCATGY, hstCATGY,"CATGY"),intCATLC);
				txtCATGY.setText(strCATGY);
				setHST_MTP("MSTPRXXSTS",hstPRSTS);
				setCMBVL_MTP(cmbPRSTS,hstPRSTS,strPRSTS1,intPRSLC,strPRSTS);
				exeINTLTXT("CATGY");
			}
			else if(M_objSOURC == cmbPRSTS)
			{
				strPRSTS = setSUBSTR(getCMBVL(cmbPRSTS, hstPRSTS,"PRSTS"),intPRSLC);
				txtPRSTS.setText(strPRSTS);
				if(strMANTP.equals("SX"))
					setHST_CAT_SX("MSTPRXXFSF",hstSRLNO);
				else
					setHST_SRL(hstSRLNO);
				setCMBVL_CAT(cmbSRLNO,hstSRLNO,strSRLNO1,intSRLLC,strSRLNO);
				exeINTLTXT("PRSTS");
			}
			else if(M_objSOURC == cmbSRLNO)
			{
				System.out.println(new StringTokenizer(cmbSRLNO.getSelectedItem().toString(),"|").nextToken().trim());
				if(strMANTP.equals("SX"))
					strSRLNO = getCODCD("MSTPRXXFSF"+new StringTokenizer(cmbSRLNO.getSelectedItem().toString(),"|").nextToken().trim());
				else
					strSRLNO = setSUBSTR(getCMBVL(cmbSRLNO, hstSRLNO,"SRLNO"),intSRLLC);
				txtSRLNO.setText(strSRLNO);
				setHST_MTP("MSTPRXXQLT",hstMATTP);
				setCMBVL_MTP(cmbMATTP,hstMATTP,strMATTP1,intMATLC,strMATTP);
				exeINTLTXT("SRLNO");
			}
			else if(M_objSOURC == cmbMATTP)
			{
				strMATTP = setSUBSTR(getCMBVL(cmbMATTP, hstMATTP,"MATTP"),intMATLC);
				txtMATTP.setText(strMATTP);
				exeINTLTXT("MATTP");
				setCMBDFT(cmbUOMCD,"MSTCOXXUOM","MT");
			}
			else if(M_objSOURC == cmbUOMCD)
			{
				strUOMCD = setSUBSTR(getCMBVL(cmbUOMCD, hstUOMCD,"UOMCD"),intUOMLC);
				txtUOMCD.setText(strUOMCD);
				//exeINTLTXT("UOMCD");
			}
			
			//System.out.println("005d1");
			//System.out.println("005d3");
			txtPRDCD.setDisabledTextColor(Color.red);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	
	
/**
	* Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	*/
	void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			setCursor(cl_dat.M_curWTSTS_pbst);
			//System.out.println(" IN SAVE ");
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			
			//setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				String L_QLTFL = strMANTP.equals("51") ? strPRSTS : "0"; 
				strSTSFL = (L_QLTFL.equals("0") ? "2" : (L_QLTFL.equals("1") ? "3" : L_QLTFL));
				savePRMST();
				saveCTMST();
				saveCTTRN();
				
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
						setMSG("Data saved successfully",'N');
					}
				}
				else
				{
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					setMSG("Error In Saving",'E');
				}
			}
		}
		catch(Exception L_EX)
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setMSG(L_EX,"exeSave");
		}
	}
		
	/**
	 * 
	*/
	private void setCMBDSBL()
	{
		try
		{
			cmbMANTP.setEnabled(false);	
			cmbSUBTP.setEnabled(false);	
			cmbCATGY.setEnabled(false);	
			cmbPRSTS.setEnabled(false);	
			cmbSRLNO.setEnabled(false);	
			cmbMATTP.setEnabled(false);	
			cmbUOMCD.setEnabled(false);	
			
			txtPRDCD.setEnabled(false);	
			txtMANTP.setEnabled(false);	
			txtSUBTP.setEnabled(false);	
			txtCATGY.setEnabled(false);	
			txtPRSTS.setEnabled(false);	
			txtSRLNO.setEnabled(false);	
			txtMATTP.setEnabled(false);	
			txtUOMCD.setEnabled(false);	
			
			cl_dat.M_cmbOPTN_pbst.requestFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX, "setCMBDSBL");
		}	
	}


	/**
	 */
	private void setCMBENBL()
	{
		try
		{
			cmbMANTP.setEnabled(true);	
			cmbSUBTP.setEnabled(true);	
			cmbCATGY.setEnabled(true);	
			cmbPRSTS.setEnabled(true);	
			cmbSRLNO.setEnabled(true);	
			cmbMATTP.setEnabled(true);	
			cmbUOMCD.setEnabled(true);	
			//cmbQLTFL.setEnabled(true);	
			txtPRDCD.setEnabled(true);	
			txtMANTP.setEnabled(true);	
			txtSUBTP.setEnabled(true);	
			txtCATGY.setEnabled(true);	
			txtPRSTS.setEnabled(true);	
			txtSRLNO.setEnabled(true);	
			txtMATTP.setEnabled(true);	
			txtUOMCD.setEnabled(true);	
			//txtQLTFL.setEnabled(true);	
			cmbMANTP.requestFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX, "setCMBENBL");
		}	
	}
	
	
	
	
	

	/**
	 */
	private void exeINTLCMB()
	{
		try
		{
			
			//if(LP_INTTP.equals("ALL")) 
			//{
				//	strPRDCD = "";
				//	strPRDDS = "";
				//	strUOMCD = "";
			//}
			//System.out.println("001");
			String L_strPRDCD=strPRDCD;
			System.out.println("PRDCD CODE 1 : "+strPRDCD);
			txtPRDCD.setText(strPRDCD);
			txtUOMCD.setText(strUOMCD);
			strMANTP  = "";
			strSUBTP  = "";
			strCATGY  = "";
			strPRSTS  = "";
			strSRLNO  = "";
			strMANTP  = "";

						
			System.out.println("PRDCD CODE 2 : "+strPRDCD);
			
			cmbMANTP.setSelectedIndex(0);
			cmbSUBTP.setSelectedIndex(0);
			cmbCATGY.setSelectedIndex(0);
			cmbPRSTS.setSelectedIndex(0);
			cmbSRLNO.setSelectedIndex(0);
			cmbMATTP.setSelectedIndex(0);
			cmbUOMCD.setSelectedIndex(0);

			System.out.println("002");
			System.out.println("PRDCD CODE 3 : "+strPRDCD);
			System.out.println("Length = "+strPRDCD.length());
			System.out.println("flgPRDEXS = "+flgPRDEXS);
			if(L_strPRDCD.length() == 10 && flgPRDEXS==true)
			{
				System.out.println("003");
				strMANTP  = L_strPRDCD.substring(0,2);
				strSUBTP  = L_strPRDCD.substring(2,4);
				strCATGY  = L_strPRDCD.substring(4,6);
				strPRSTS  = L_strPRDCD.substring(6,7);
				strSRLNO  = L_strPRDCD.substring(7,9);
				strMATTP  = L_strPRDCD.substring(9,10);
				//System.out.println("004");
				txtMANTP.setText(strMANTP);
				txtSUBTP.setText(strSUBTP);
				txtCATGY.setText(strCATGY);
				txtPRSTS.setText(strPRSTS);
				txtSRLNO.setText(strSRLNO);
				txtMATTP.setText(strMATTP);
				//System.out.println("005");
				setCMBSEL("MANTP");
				setCMBSEL("SUBTP");
				setCMBSEL("CATGY");
				setCMBSEL("PRSTS");
				setCMBSEL("SRLNO");
				setCMBSEL("MATTP");
				setCMBDSBL();
			}
				//System.out.println("006");
			System.out.println("004");
				txtPRDDS.requestFocus();
				//this.setCursor(curDFSTS);
				setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception E)
		{
			setMSG(E, "exeINTLCMB");
		}
	}
	

	
	
/**
 */	
	private void setCMBSEL(String LP_CMBTP)
	{
		try
		{
			if(LP_CMBTP.equals("MANTP"))
			{
				setHST_GRP("MSTCOXXPGR","MG",hstMANTP);
				setCMBVL_GRP(cmbMANTP,hstMANTP,strMANTP1,intMANLC,strMANTP,"MG");
			}
			else if(LP_CMBTP.equals("UOMCD"))
			{
				setHST_CAT("MSTCOXXUOM",hstUOMCD);
				setCMBVL_CAT(cmbUOMCD,hstUOMCD,strUOMCD1,intUOMLC,strUOMCD);
			}
			else if(LP_CMBTP.equals("SUBTP"))
			{
				setHST_GRP("MSTCOXXPGR","SG",hstSUBTP);
				setCMBVL_GRP(cmbSUBTP,hstSUBTP,strSUBTP1,intSUBLC,strSUBTP,"SG");
			}
			else if(LP_CMBTP.equals("CATGY"))
			{
				if(strMANTP.equals("54"))
					setHST_CAT_54("MSTPRXXFLT",hstCATGY);
				else if(strMANTP.equals("SX"))
					setHST_CAT_SX("MSTPRXXCST",hstCATGY);
				else
					setHST_CAT_XX(hstCATGY);
				setCMBVL_MTP1(cmbCATGY,hstCATGY,strCATGY1,intCATLC,strCATGY);
			}
			else if(LP_CMBTP.equals("PRSTS"))
			{
				setHST_MTP("MSTPRXXSTS",hstPRSTS);
				setCMBVL_MTP(cmbPRSTS,hstPRSTS,strPRSTS1,intPRSLC,strPRSTS);
			}
			else if(LP_CMBTP.equals("SRLNO"))
			{
				setHST_SRL(hstSRLNO);
				setCMBVL_CAT(cmbSRLNO,hstSRLNO,strSRLNO1,intSRLLC,strSRLNO);
			}
			else if(LP_CMBTP.equals("MATTP"))
			{
				setHST_MTP("MSTPRXXQLT",hstMATTP);
				setCMBVL_MTP(cmbMATTP,hstMATTP,strMATTP1,intMATLC,strMATTP);
			}
		}
		catch(Exception E)
		{
			setMSG(E, "setCMBSEL");
		}	
	}
	
	/**
	*/
	private void exeINTLTXT(String LP_TXTCAT)
	{
		try
		{
			if(!flgPRDEXS)
			{
				if(LP_TXTCAT.equals("ALLTP"))
				{
					strMANTP="";
					strSUBTP="";
					strCATGY="";
					strPRSTS="";
					strSRLNO="";
					strMATTP="";
				}
				
				if(LP_TXTCAT.equals("MANTP"))
				{
					strSUBTP="";
					strCATGY="";
					strPRSTS="";
					strSRLNO="";
					strMATTP="";
				}
				else if(LP_TXTCAT.equals("SUBTP"))
				{
					strCATGY="";
					strPRSTS="";
					strSRLNO="";
					strMATTP="";
				}
				else if(LP_TXTCAT.equals("CATGY"))
				{
					strPRSTS="";
					strSRLNO="";
					strMATTP="";
				}
				else if(LP_TXTCAT.equals("PRSTS"))
				{
					strSRLNO="";
					strMATTP="";
				}
				else if(LP_TXTCAT.equals("SRLNO"))
				{
					strMATTP="";
				}
				else if(LP_TXTCAT.equals("MATTP"))
				{
				}
			}
			txtMANTP.setText(strMANTP);
			txtSUBTP.setText(strSUBTP);
			txtCATGY.setText(strCATGY);
			txtPRSTS.setText(strPRSTS);
			txtSRLNO.setText(strSRLNO);
			txtMATTP.setText(strMATTP);
			strPRDCD = strMANTP+strSUBTP+strCATGY+strPRSTS+strSRLNO+strMATTP;
			txtPRDCD.setText(strPRDCD);
			dspPRDLS();
		}
		catch(Exception E)
		{
			setMSG(E, "exeINTLTXT");
		}	
	}
	
	/**
	*/
	private void dspPRDLS()
	{
		try
		{
			vtrPRDLS.clear();
			Object[] arrHSTKEY = setHST_ARR(hstPRMST);
			int intPRDLN = strPRDCD.length();
			for(int i=0;i<arrHSTKEY.length;i++)
			//Enumeration enmKEYVL = hstPRMST.keys();
			//while(enmKEYVL.hasMoreElements())
			{
				Object L_KEY = arrHSTKEY[i];
				if(L_KEY.toString().substring(0,intPRDLN).equals(strPRDCD))
					vtrPRDLS.addElement(L_KEY.toString()+"  "+getPRMST(L_KEY.toString(),"PR_PRDDS"));
			}
			
			lstPRDCD.setListData(vtrPRDLS);
		}
		catch(Exception E)
		{
			setMSG(E, "dspPRDLS");
		}	
	}

	/**
	 * 
	*/
    public void focusLost(FocusEvent L_FE)
    {
        super.focusLost(L_FE);
		Object L_SRC = L_FE.getSource();
		setMSG("",'N');
		if(L_SRC == txtMANTP)
		{
			strMANTP = txtMANTP.getText();
			strPRDCD = strMANTP+strSUBTP+strCATGY+strPRSTS+strSRLNO+strMATTP;
			txtPRDCD.setText(strPRDCD);
		}
		else if(L_SRC == txtSUBTP)
		{
			strSUBTP = txtSUBTP.getText();
			strPRDCD = strMANTP+strSUBTP+strCATGY+strPRSTS+strSRLNO+strMATTP;
			txtPRDCD.setText(strPRDCD);
		}
		else if(L_SRC == txtCATGY)
		{
			strCATGY = txtCATGY.getText();
			strPRDCD = strMANTP+strSUBTP+strCATGY+strPRSTS+strSRLNO+strMATTP;
			txtPRDCD.setText(strPRDCD);
		}
		else if(L_SRC == txtPRSTS)
		{
			strPRSTS = txtPRSTS.getText();
			strPRDCD = strMANTP+strSUBTP+strCATGY+strPRSTS+strSRLNO+strMATTP;
			txtPRDCD.setText(strPRDCD);
		}
		else if(L_SRC == txtSRLNO)
		{
			strSRLNO = txtSRLNO.getText();
			strPRDCD = strMANTP+strSUBTP+strCATGY+strPRSTS+strSRLNO+strMATTP;
			txtPRDCD.setText(strPRDCD);
			if(cmbMATTP.getItemCount()<2)
			{
				setHST_MTP("MSTPRXXQLT",hstMATTP);
				setCMBVL_MTP(cmbMATTP,hstMATTP,strMATTP1,intMATLC,strMATTP);
			}
		}
		else if(L_SRC == txtMATTP)
		{
			strMATTP = txtMATTP.getText();
			strPRDCD = strMANTP+strSUBTP+strCATGY+strPRSTS+strSRLNO+strMATTP;
			txtPRDCD.setText(strPRDCD);
		}
    }


	
	/**
	 */
	private void setCMBVL_GRP(JComboBox LP_CMBNM, Hashtable LP_HSTNM, String LP_DFTVL, int LP_CODLC, String LP_CATVL, String LP_CATTP)
	{
		try
		{
			LP_CMBNM.removeActionListener(this);
			Object[] arrHSTKEY = setHST_ARR(LP_HSTNM);
			LP_CMBNM.removeAllItems();
			LP_CMBNM.addItem(LP_DFTVL);
			String L_strCMBVL = "x";
			Object L_KEY = null;
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				L_KEY = arrHSTKEY[i];
				if(!LP_CATTP.equals("MG") && !L_KEY.toString().substring(0,2).equals(strMANTP))
					continue;
				L_strCMBVL = LP_HSTNM.get(L_KEY).toString()+"| "+L_KEY.toString().substring(LP_CODLC);
					
				LP_CMBNM.addItem(L_strCMBVL);
				if(flgPRDEXS)
				{
					if (LP_CATVL.equals(L_KEY.toString().substring(LP_CODLC)))
						LP_CMBNM.setSelectedItem(L_strCMBVL);
				}
			}
				
			LP_CMBNM.addActionListener(this);
		}
		catch(Exception E)
		{
			setMSG(E, "setCMBVL_GRP");
		}	
	}

	/**
	 */
	private void setCMBVL_MTP(JComboBox LP_CMBNM, Hashtable LP_HSTNM, String LP_DFTVL, int LP_CODLC, String LP_CATVL)
	{
		try
		{
			LP_CMBNM.removeActionListener(this);
			Object[] arrHSTKEY = setHST_ARR(LP_HSTNM);
			LP_CMBNM.removeAllItems();
			LP_CMBNM.addItem(LP_DFTVL);
			Object L_KEY = null;
			String L_strCMBVL = "x";
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				L_KEY = arrHSTKEY[i];
				if(L_KEY.toString().substring(0,2).equals(strMANTP))
				{
					L_strCMBVL = LP_HSTNM.get(L_KEY).toString()+"| "+L_KEY.toString().substring(LP_CODLC);
					LP_CMBNM.addItem(L_strCMBVL);
					if(flgPRDEXS)
					{
						//System.out.println(LP_CATVL+"/"+L_KEY.toString().substring(LP_CODLC));
						if (LP_CATVL.equals(L_KEY.toString().substring(LP_CODLC)))
							LP_CMBNM.setSelectedItem(L_strCMBVL);
					}
				}
			}
			LP_CMBNM.addActionListener(this);
		}
		catch(Exception E)
		{
			setMSG(E, "setCMBVL_MTP");
		}	
	}
	
	/**
	 */
	private void setCMBVL_MTP1(JComboBox LP_CMBNM, Hashtable LP_HSTNM, String LP_DFTVL, int LP_CODLC, String LP_CATVL)
	{
		try
		{
			LP_CMBNM.removeActionListener(this);
			Object[] arrHSTKEY = setHST_ARR(LP_HSTNM);
			LP_CMBNM.removeAllItems();
			LP_CMBNM.addItem(LP_DFTVL);
			Object L_KEY = null;
			String L_strCMBVL = "x";
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				L_KEY = arrHSTKEY[i];
				if(L_KEY.toString().substring(0,4).equals(strMANTP+strSUBTP))
				{
					L_strCMBVL = LP_HSTNM.get(L_KEY).toString()+"| "+L_KEY.toString().substring(LP_CODLC);
					LP_CMBNM.addItem(L_strCMBVL);
					if(flgPRDEXS)
					{
						//System.out.println(LP_CATVL+"/"+L_KEY.toString().substring(LP_CODLC));
						if (LP_CATVL.equals(L_KEY.toString().substring(LP_CODLC)))
							LP_CMBNM.setSelectedItem(L_strCMBVL);
					}
				}
			}
			LP_CMBNM.addActionListener(this);
		}
		catch(Exception E)
		{
			setMSG(E, "setCMBVL_MTP");
		}	
	}
	
	
	/**
	 */
	private void setCMBVL_CAT(JComboBox LP_CMBNM, Hashtable LP_HSTNM, String LP_DFTVL, int LP_CODLC, String LP_CATVL)
	{
		try
		{
			LP_CMBNM.removeActionListener(this);
			Object[] arrHSTKEY = setHST_ARR(LP_HSTNM);
			LP_CMBNM.removeAllItems();
			LP_CMBNM.addItem(LP_DFTVL);
			Object L_KEY = null;
			String L_strCMBVL = "x";
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				L_KEY = arrHSTKEY[i];
				L_strCMBVL = LP_HSTNM.get(L_KEY).toString()+"| "+L_KEY.toString().substring(LP_CODLC);
				LP_CMBNM.addItem(L_strCMBVL);
				if(flgPRDEXS)
				{
					//System.out.println(LP_CATVL+"/"+L_KEY.toString().substring(LP_CODLC));
					if(LP_CATVL.equals(L_KEY.toString().substring(LP_CODLC)))
						LP_CMBNM.setSelectedItem(L_strCMBVL);
				}
			}
			LP_CMBNM.addActionListener(this);
		}
		catch(Exception E)
		{
			setMSG(E, "setCMBVL_CAT");
		}	
	}
	

	/**
	 */
	private void setCMBVL_SRL(JComboBox LP_CMBNM, Hashtable LP_HSTNM, String LP_DFTVL, int LP_CODLC, String LP_CATVL)
	{
		try
		{
			LP_CMBNM.removeActionListener(this);
			Object[] arrHSTKEY = setHST_ARR(LP_HSTNM);
			LP_CMBNM.removeAllItems();
			LP_CMBNM.addItem(LP_DFTVL);
			Object L_KEY = null;
			String L_strCMBVL = "x";
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				L_KEY = arrHSTKEY[i];
				if(L_KEY.toString().substring(0,2).equals(strMANTP))
				{
					L_strCMBVL = LP_HSTNM.get(L_KEY).toString()+"| "+L_KEY.toString().substring(LP_CODLC);
					LP_CMBNM.addItem(L_strCMBVL);
					if(flgPRDEXS)
					{
						//System.out.println(LP_CATVL+"/"+L_KEY.toString().substring(LP_CODLC));
						if(LP_CATVL.equals(L_KEY.toString().substring(LP_CODLC)))
							LP_CMBNM.setSelectedItem(L_strCMBVL);
					}
				}
			}
			LP_CMBNM.addActionListener(this);
		}
		catch(Exception E)
		{
			setMSG(E, "setCMBVL_SRL");
		}	
	}
	/**
	 * 
	*/
	private void dspPRDDTL()
	{
		try
		{
			strPRDDS = txtPRDDS.getText().trim();
			M_strSQLQRY = "Select pr_prdcd, pr_prdds, pr_uomcd,pr_stsfl";
			M_strSQLQRY += " from co_prmst where pr_prdds='"+strPRDDS+"' and isnull(pr_stsfl,'') != 'X'";
			ResultSet LM_RSLSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//this.setCursor(curWTSTS);
			setCursor(cl_dat.M_curWTSTS_pbst);
			flgPRDEXS = false;
			setCMBDSBL();
			if(!LM_RSLSET.next() || LM_RSLSET == null)
			{
				exeINTLCMB();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setCMBENBL();
				return;
			}
			flgPRDEXS = true;
			strPRDCD = nvlSTRVL(LM_RSLSET.getString("pr_prdcd"),"");
			strUOMCD = nvlSTRVL(LM_RSLSET.getString("pr_uomcd"),"");
			strPRDDS = nvlSTRVL(LM_RSLSET.getString("pr_prdds"),"");
			txtPRDCD.setText(strPRDCD);
			txtUOMCD.setText(strUOMCD);
			System.out.println("PRDCD : "+strPRDCD);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				JOptionPane.showMessageDialog(this,"Product Code alreay exists");
			}
			System.out.println("PRDCD CODE : "+strPRDCD);
			exeINTLCMB();
			cmbPRSTS.addActionListener(this);
			//this.setCursor(curDFSTS);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception E)
		{
			setMSG(E, "dspPRDDTL");
		}	
	}
	/**
	 */
	public boolean vldDATA()
	{
		boolean L_UPDREC = true;
		if(nvlSTRVL(strPRDCD,"").length() !=10)
		{
		 	setMSG("Please check the generated Product code,code should be of 10 dights..",'E');
		 	L_UPDREC = false;
		}
		else if(txtPRDDS.getText().trim().length() ==0)
		{
		    setMSG("Grade description can not be blank ..",'E');
		    L_UPDREC = false;
		}
		return L_UPDREC;
	}
	/** Saving data in Product Master (CO_PRMST)
	*/	
	private void savePRMST()
	{
		try
		{
			String L_ADDDS ="";
			cl_dat.M_flgLCUPD_pbst = true;
			String L_QLTFL = strMANTP.equals("51") ? strPRSTS : "0"; 
			String L_QLTFL1 = (L_QLTFL.equals("0") ? "2" : (L_QLTFL.equals("1") ? "3" : L_QLTFL));
			strWHERE =  "PR_PRDCD = '" +strPRDCD+"'";
			flgCHK_EXIST =  chkEXIST("CO_PRMST", strWHERE);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{   
				if(strMANTP.equals("54"))
				{
					L_ADDDS = " ";
					L_ADDDS = getSUBCT(strMANTP+strSUBTP);
					
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)  && flgCHK_EXIST)
			{
				JOptionPane.showMessageDialog(this,"Product code already exists");
				return;
			}
			strPRDTP = getCDTRN("MSTCOXXPGR"+strMANTP+"0000000A","CMT_CHP01",hstCDTRN);
			String L_STRQRY="";
			String L_strPRDDS = txtPRDDS.getText();
		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			if(strMANTP.equals("54") )
			{
				L_strPRDDS = L_strPRDDS + " "+L_ADDDS;
				if(L_strPRDDS.length() > 45)
				{
					JOptionPane.showMessageDialog(this,"Grade Desc. is too long after appending "+L_ADDDS);
				}
			
			}
			if(!flgCHK_EXIST)
			{
				L_STRQRY="Insert into co_prmst (pr_prdtp,pr_prdcd,pr_prdds,pr_uomcd,pr_trnfl,pr_stsfl,pr_lupdt,pr_lusby) values( "
				+setINSSTR("PR_PRDTP",strPRDTP,"C")
				+setINSSTR("PR_PRDCD",strPRDCD,"C")
				+setINSSTR("PR_PRDDS",L_strPRDDS,"C")
				+setINSSTR("PR_UOMCD",strUOMCD,"C")
				+setINSSTR("PR_TRNFL","0","C")
				+setINSSTR("PR_STSFL",strSTSFL,"C")
				+setINSSTR("PR_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"'"+cl_dat.M_strUSRCD_pbst+"')";
			}
			else if(flgCHK_EXIST)
			{
				L_STRQRY="update CO_PRMST set "
				+setUPDSTR("PR_PRDTP",strPRDTP,"C")
				+setUPDSTR("PR_PRDDS",txtPRDDS.getText(),"C")
				+setUPDSTR("PR_UOMCD",strUOMCD,"C")
				+setUPDSTR("PR_TRNFL","0","C")
				+setUPDSTR("PR_STSFL",strSTSFL,"C")
				+setUPDSTR("PR_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"'"
				+" where "+strWHERE;
			}
			//System.out.println(L_STRQRY);
			cl_dat.exeSQLUPD(L_STRQRY,"setLCLUPD");
			//cl_dat.ocl_dat.exeDBCMT("SP","ACT","");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					//JOptionPane.showMessageDialog(this,cl_dat.M_flgLCUPD_pbst ? "Saved Successfully" : "Record Not Saved");
				}
			}
			JOptionPane.showMessageDialog(this,cl_dat.M_flgLCUPD_pbst ? "Saved Successfully" : "Record Not Saved");
		}
		catch (Exception L_EX)
		{
			setMSG("Error in savePRMST : "+L_EX,'E');
		}
	}
	



	/** Saving data in Catalogue Master (CO_CTMST)
	 */	
	private void saveCTMST()
	{
		try
		{
			//if(!cl_dat.ocl_dat.M_LCLUPD)
			//	return;
			cl_dat.M_flgLCUPD_pbst = true;
			strWHERE =  "CT_GRPCD = '" +strPRDCD.substring(0,2)+"' and "
						+"CT_CODTP = 'CD' and "
						+"CT_MATCD = '"+strPRDCD+"'";
			flgCHK_EXIST =  chkEXIST("CO_CTMST", strWHERE);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)  && flgCHK_EXIST)
			{
					JOptionPane.showMessageDialog(this,"Record alreay exists in CO_CTMST");
					return;
			}
			//strPRDTP = getCDTRN("MSTCOXXPGR"+strMANTP+"0000000A","CMT_CHP01",hstCDTRN);
			String L_STRQRY="";
			if(!flgCHK_EXIST)
			{
				L_STRQRY="Insert into co_ctmst (ct_grpcd, ct_codtp, ct_matcd, ct_matds, ct_dsctp, ct_uomcd, ct_lvlrf, ct_stsfl, ct_trnfl, ct_lupdt, ct_lusby) values( "
				+setINSSTR("CT_GRPCD",strPRDTP.substring(0,2),"C")
				+setINSSTR("CT_CODTP","CD","C")
				+setINSSTR("CT_MATCD",strPRDCD,"C")
				+setINSSTR("CT_MATDS",txtPRDDS.getText(),"C")
				+setINSSTR("CT_DSCTP","S","C")
				+setINSSTR("CT_UOMCD",strUOMCD,"C")
				+setINSSTR("CT_LVLRF","05","C")
				+setINSSTR("CT_STSFL",strSTSFL,"C")
				+setINSSTR("CT_TRNFL","0","C")
				+setINSSTR("CT_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"'"+cl_dat.M_strUSRCD_pbst+"')";
			}
			else if(flgCHK_EXIST)
			{
				L_STRQRY="update CO_CTMST set "
				+setUPDSTR("CT_MATDS",txtPRDDS.getText(),"C")
				+setUPDSTR("CT_DSCTP","S","C")
				+setUPDSTR("CT_UOMCD",strUOMCD,"C")
				+setUPDSTR("CT_LVLRF","05","C")
				+setUPDSTR("CT_STSFL",strSTSFL,"C")
				+setUPDSTR("CT_TRNFL","0","C")
				+setUPDSTR("CT_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"'CT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+strWHERE;
			}
			//System.out.println(L_STRQRY);
			cl_dat.exeSQLUPD(L_STRQRY,"setLCLUPD");
			//cl_dat.ocl_dat.exeDBCMT("SP","ACT","");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
				}
			}
		}
		catch (Exception L_EX)
		{
			setMSG("Error in saveCTMST : "+L_EX,'E');
		}
	}

/** Saving data in Catalogue Master (CO_CTMST)
 */	
	private void saveCTTRN()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			strWHERE =  "CTT_GRPCD = '" +strPRDCD.substring(0,2)+"' and "
						+"CTT_CODTP = 'CD' and "
						+"CTT_MATCD = '"+strPRDCD+"' and "
						+"CTT_LVLNO = '05' and "
						+"CTT_LINNO = '91'";
			flgCHK_EXIST =  chkEXIST("CO_CTTRN", strWHERE);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)  && flgCHK_EXIST)
			{
					JOptionPane.showMessageDialog(this,"Record alreay exists in CO_CTTRN");
					return;
			}
			//strPRDTP = getCDTRN("MSTCOXXPGR"+strMANTP+"0000000A","CMT_CHP01",hstCDTRN);
			String L_STRQRY="";
			if(!flgCHK_EXIST)
			{
				L_STRQRY="Insert into co_cttrn (ctt_grpcd, ctt_codtp, ctt_matcd, ctt_lvlno, ctt_linno, ctt_matds, ctt_prtfl, ctt_stsfl, ctt_trnfl, ctt_lupdt, ctt_lusby) values( "
				+setINSSTR("CTT_GRPCD",strPRDTP.substring(0,2),"C")
				+setINSSTR("CTT_CODTP","CD","C")
				+setINSSTR("CTT_MATCD",strPRDCD,"C")
				+setINSSTR("CTT_LVLNO","05","C")
				+setINSSTR("CTT_LINNO","91","C")
				+setINSSTR("CTT_MATDS",txtPRDDS.getText(),"C")
				+setINSSTR("CTT_PRTFL","N","C")
				+setINSSTR("CTT_STSFL",strSTSFL,"C")
				+setINSSTR("CTT_TRNFL","0","C")
				+setINSSTR("CTT_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"'"+cl_dat.M_strUSRCD_pbst+"')";
			}
			else if(flgCHK_EXIST)
			{
				L_STRQRY="update CO_CTTRN set "
				+setINSSTR("CTT_MATDS",txtPRDDS.getText(),"C")
				+setINSSTR("CTT_PRTFL","N","C")
				+setINSSTR("CTT_STSFL",strSTSFL,"C")
				+setINSSTR("CTT_TRNFL","0","C")
				+setINSSTR("CTT_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"'CTT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+strWHERE;
			}
			//System.out.println(L_STRQRY);
			cl_dat.exeSQLUPD(L_STRQRY,"setLCLUPD");
			//cl_dat.ocl_dat.exeDBCMT("SP","ACT","");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
				}
			}
		}
		catch (Exception L_EX)
		{
			setMSG("Error in saveCTTRN : "+L_EX,'E');
		}
	}
	
	/**
	 * 
	*/
	private String setSUBSTR(String LP_STRVL, int LP_INTVL)
	{
		String L_RETVL="";
		try
		{
			if(LP_STRVL==null)
			{
				System.out.print("Null value in Code"); 
				return L_RETVL;
			}
			if(LP_STRVL.length()<LP_INTVL)
			{
				System.out.println("Length < "+LP_INTVL+" ("+LP_STRVL+")"); 
				return L_RETVL;
			}
			L_RETVL = LP_STRVL.substring(LP_INTVL);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setSUBSTR");
		}
		return L_RETVL;
	}
	/**
	 * 
	*/
	private String getCMBVL(JComboBox LP_CMBNM, Hashtable LP_HSTNM, String LP_CATNM)
	{
		String L_RETSTR= "";
		try
		{
			if(LP_HSTNM.size()==0)
				return L_RETSTR;
			if(LP_CMBNM.getSelectedIndex()==0)
				return L_RETSTR;
			Object[] arrHSTKEY = setHST_ARR(LP_HSTNM);
			String L_strCODDS = new StringTokenizer(LP_CMBNM.getSelectedItem().toString(),"|").nextToken().trim();
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				String L_strCODCD = arrHSTKEY[i].toString();
				if(LP_HSTNM.get(L_strCODCD).toString().equals(L_strCODDS))
				{
					L_RETSTR = L_strCODCD;
					break;
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex,"getCMBVL");
		}
		return L_RETSTR;
	}
	
	
	/**
	 * Picking up combo element for Main Group & Sub Group
	 */
	private void setHST_GRP(String LP_CODCT, String LP_PRDGR, Hashtable LP_HSTNM)
	{
		try
		{
			LP_HSTNM.clear();				
			Enumeration enmCODKEYS=hstCDTRN.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals(LP_CODCT))
				{
					String L_strCODCD1 = getCDTRN(L_strCODCD,"CMT_CODCD", hstCDTRN);
					String L_strCCSVL1 = getCDTRN(L_strCODCD,"CMT_CCSVL", hstCDTRN);
					if(L_strCODCD1.length()==10)
					{
						if(LP_PRDGR.equals("MG") && L_strCODCD1.substring(2,10).equals("0000000A") && L_strCCSVL1.equals("MG"))
						{
							String L_strMANTP = getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN);
							hstMANTP.put(L_strCODCD1.substring(0,2),L_strMANTP);
							//System.out.println("hstMANTP : "+L_strCODCD1.substring(0,2)+"/"+L_strMANTP);
						}
						else if(LP_PRDGR.equals("SG") && L_strCODCD1.substring(4,10).equals("00000A")  && L_strCCSVL1.equals("SG"))
						{
							String L_strSUBTP = getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN);
							hstSUBTP.put(L_strCODCD1.substring(0,4),L_strSUBTP);
							//system.out.println("hstSUBTP : "+L_strCODCD1.substring(0,4)+"/"+L_strSUBTP);
						}
						//LP_VTRNM.addElement(getCDTRN(L_strCODCD,LP_FLDNM, hstCDTRN));
						//System.out.println("adding to Vector  setCDLST_GRP : "+getCDTRN(L_strCODCD,LP_FLDNM, hstCDTRN));
					}
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex, "setHST_GRP");
		}
	}
	
	/**
	 */
	private void setHST_MTP(String LP_CODCT, Hashtable<String,String> LP_HSTNM)
	{
		try
		{
			LP_HSTNM.clear();				
			Enumeration enmCODKEYS = hstCDTRN.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals(LP_CODCT))
				{
					String L_strCODCD1 = getCDTRN(L_strCODCD,"CMT_CODCD", hstCDTRN);
					if(L_strCODCD1.substring(0,2).equals(strMANTP))
					{
						LP_HSTNM.put(L_strCODCD1.substring(0,3),getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
						//system.out.println(LP_CODCT+" : "+L_strCODCD1.substring(0,3)+"/"+getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
					}
						//System.out.println("adding to Vector  setCDLST_GRP : "+getCDTRN(L_strCODCD,LP_FLDNM, hstCDTRN));
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex,"setHST_MTP");
		}
	}
	/**
	 */
	private void setHST_CAT_54(String LP_CODCT, Hashtable<String,String> LP_HSTNM)
	{
		try
		{
			LP_HSTNM.clear();				
			Enumeration enmCODKEYS = hstCDTRN.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals(LP_CODCT))
				{
					String L_strCODCD1 = getCDTRN(L_strCODCD,"CMT_CODCD", hstCDTRN);
					LP_HSTNM.put(strMANTP+strSUBTP+L_strCODCD1,getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
						//system.out.println(LP_CODCT+" : "+L_strCODCD1+"/"+getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex,"setHST_CAT_54");
		}
	}


	private void setHST_CAT_SX(String LP_CODCT, Hashtable<String,String> LP_HSTNM)
	{
		try
		{
			LP_HSTNM.clear();				
			Enumeration enmCODKEYS = hstCDTRN.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals(LP_CODCT))
				{
					String L_strCODCD1 = getCDTRN(L_strCODCD,"CMT_CODCD", hstCDTRN);
					LP_HSTNM.put(strMANTP+strSUBTP+L_strCODCD1,getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
						//system.out.println(LP_CODCT+" : "+L_strCODCD1+"/"+getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex,"setHST_CAT_SX");
		}
	}



	
	/**
	 */
	private void setHST_CAT(String LP_CODCT, Hashtable<String,String> LP_HSTNM)
	{
		try
		{
			LP_HSTNM.clear();				
			Enumeration enmCODKEYS = hstCDTRN.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals(LP_CODCT))
				{
					String L_strCODCD1 = getCDTRN(L_strCODCD,"CMT_CODCD", hstCDTRN);
					LP_HSTNM.put(L_strCODCD1,getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
					//System.out.println(LP_CODCT+" : "+L_strCODCD1+"/"+getCDTRN(L_strCODCD,"CMT_CODDS", hstCDTRN));
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex,"setHST_CAT");
		}
	}
	
	/**
	 */
	private void setHST_CAT_XX(Hashtable<String,String> LP_HSTNM)
	{
		try
		{
			LP_HSTNM.clear();				
			Enumeration enmCODKEYS = hstPRCAT.keys();
			//system.out.println("hstPRCAT size : "+hstPRCAT.size());
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				//System.out.println("setHST_CAT_XX : "+L_strCODCD+"/"+getPRCAT(L_strCODCD,"PR_CATDS")+"/"+strMANTP+strSUBTP);
				if(L_strCODCD.substring(0,4).equals(strMANTP+strSUBTP))
				{
					LP_HSTNM.put(L_strCODCD,getPRCAT(L_strCODCD,"PR_CATDS"));
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex,"setHST_CAT_XX");
		}
	}
	

	
	/**
	 */
	private void setHST_SRL(Hashtable<String,String> LP_HSTNM)
	{
		try
		{
			LP_HSTNM.clear();				
			Enumeration enmCODKEYS = hstPRMST.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				//System.out.print(" _SRL : "+L_strCODCD.substring(0,7)+"  "+strMANTP+strSUBTP+strCATGY+strPRSTS);
				if(L_strCODCD.substring(0,7).equals(strMANTP+strSUBTP+strCATGY+strPRSTS))
				{
					//system.out.println("\n setHST_SRL : "+L_strCODCD.substring(0,7)+"  "+strMANTP+strSUBTP+strCATGY+strPRSTS);
					//system.out.println("              : "+L_strCODCD.substring(7,9)+"  "+getPRMST(L_strCODCD,"PR_PRDDS"));
					LP_HSTNM.put(L_strCODCD.substring(7,9),getPRMST(L_strCODCD,"PR_PRDDS"));
				}
			}
		}
		catch (Exception L_Ex)
		{
			setMSG(L_Ex,"setHST_SRL");
		}
	}
	
	/**
	 * 
	*/
	
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			Object L_SRC = L_KE.getSource();
			
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(L_SRC == txtPRDDS)
				{
					M_strHLPFLD="txtPRDDS";
					
					M_strSQLQRY = "Select pr_prdds,pr_prdcd from co_prmst where pr_stsfl != 'X' order by pr_prdds";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Description", "Code"},2,"CT");
				}
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}

	/**
	 */
	public String chkNULL(Object LP_VALUE)
	{
		String L_RTNSTR = "";
		try
		{
			if(LP_VALUE != null)
			{
				String L_VALUE = LP_VALUE.toString().trim();
				StringTokenizer stkVALUE = new StringTokenizer(L_VALUE," ");
				if(stkVALUE.nextToken().trim().equalsIgnoreCase("Select"))
				{
					return L_RTNSTR;
				}
				return L_VALUE;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkNULL");
		}
		return L_RTNSTR;
	}

	/**
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			
			if(M_strHLPFLD.equals("txtPRDDS"))
			{
				System.out.println("lllll");
				txtPRDDS.setText(cl_dat.M_strHLPSTR_pbst);
				//txtPRDDS.requestFocus();
			}
		}
		catch(NullPointerException N_E)
		{
			setMSG("NO Records Available",'E');
		} 
	}
	
	
	
	/**
	 */
	private String getSUBCT(String LP_SUBCT)
	{
		try
		{
			String L_STRQRY ="Select cmt_codds from co_cdtrn where cmt_cgmtp ='MST' and cmt_cgstp ='COXXPGR' and cmt_codcd ='";
			L_STRQRY += LP_SUBCT +"00000A'"+" and cmt_ccsvl ='SG'";
			L_RSLSET = cl_dat.exeSQLQRY1(L_STRQRY);
			if(L_RSLSET !=null)
				if(L_RSLSET.next())
				{
					return(nvlSTRVL(L_RSLSET.getString("cmt_codds"),""));
				}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSUBCT");
		}
		return "";
	}
	



	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	 */
     private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
    {
		String L_strSQLQRY = "";
        try
        {
            L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
			//System.out.println(L_strSQLQRY);
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 //setMSG("Records not found in CO_CDTRN",'E');
                  return;
            }
            while(true)
            {
                strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                String[] staCDTRN = new String[intCDTRN_TOT];
                staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
                staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
                staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
                staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
                staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
                staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
                staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
                LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
				hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
                if (!L_rstRSSET.next())
					break;
            }
            L_rstRSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG("crtCDTRN"+L_EX,'E');
        }
	}
	
			/**  Returning code value for specified code description
		 */
		private String getCODCD(String LP_CODDS_KEY)		
		{
			if(!hstCODDS.containsKey(LP_CODDS_KEY))
				return "";
			return hstCODDS.get(LP_CODDS_KEY).toString();
		}



	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
			if (LP_FLDTP.equals("C"))
				return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
			else if (LP_FLDTP.equals("N"))
				return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
			else 
				return " ";
		}
		catch (Exception L_EX)
		{
			setMSG("getRSTVAL"+L_EX,'E');
		}
		return " ";
	} 

	/** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
    private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
    {
		try
		{
            if (LP_FLDNM.equals("CMT_CODCD"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
            else if (LP_FLDNM.equals("CMT_CODDS"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
            else if (LP_FLDNM.equals("CMT_SHRDS"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
            else if (LP_FLDNM.equals("CMT_CHP01"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
            else if (LP_FLDNM.equals("CMT_CHP02"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
            else if (LP_FLDNM.equals("CMT_NMP01"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
            else if (LP_FLDNM.equals("CMT_NMP02"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
            else if (LP_FLDNM.equals("CMT_NCSVL"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
            else if (LP_FLDNM.equals("CMT_CCSVL"))
                    return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
        }
		catch (Exception L_EX)
		{
			setMSG("getCDTRN"+L_EX,'E');
		}
        return "";
	}


		/** Generating string for Insertion Query
		 * @param	LP_FLDNM	Field name to be inserted
		 * @param	LP_FLDVL	Content / value of the field to be inserted
		 * @param	LP_FLDTP	Type of the field to be inserted
		 */
	private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP)
	{
		try 
		{
			//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
			if (LP_FLDTP.equals("C"))
				 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
		 	else if (LP_FLDTP.equals("N"))
		         return   nvlSTRVL(LP_FLDVL,"0") + ",";
			else if (LP_FLDTP.equals("D"))
		    {
	            String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL));
	            if(L_CHKDT.length() >= 10)
                   return "'"+ L_CHKDT + "',";
	            else
                   return  "  null , ";
		    } 
			
			else return " ";
		}
		catch (Exception L_EX) 
		{
			setMSG("Error in setINSSTR : "+L_EX,'E');
		}
		return " ";
	}
				



		/** Generating string for Updation Query
		 * @param	LP_FLDNM	Field name to be inserted
		 * @param	LP_FLDVL	Content / value of the field to be inserted
		 * @param	LP_FLDTP	Type of the field to be inserted
		 */
	private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 
	{
		String L_RETSTR = "";
		try 
		{
			//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
			if (LP_FLDTP.equals("C"))
				 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
		 	else if (LP_FLDTP.equals("N"))
		         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
			else if (LP_FLDTP.equals("D"))
		    {
	            String L_CHKDT = LP_FLDVL.equals("") ? "null" : M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL));
	            if(L_CHKDT.length() >= 10)
					return LP_FLDNM + " = '" + L_CHKDT + "', ";
			    else
			        return LP_FLDNM + " = null, ";
			} 
			/*else if (LP_FLDTP.equals("T"))
			{
				String L_CHKDT = LP_FLDVL.equals("") ? "null" : cc_dattm.occ_dattm.setDTMFMT(LP_FLDVL);
			    if(L_CHKDT.length() > 10)
					return LP_FLDNM + " = '" + L_CHKDT + "',";
			    else
					return LP_FLDNM + " = null, ";
			} 
			*/
			else return " ";
		}
		catch (Exception L_EX) 
		{
			setMSG("Error in setUPDSTR : "+L_EX,'E');
		}
		return " ";
	}
				

	/** Checking key in table for record existance
	 */
	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
	{
		boolean L_flgCHKFL = false;
		try
		{
			String L_STRQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
			System.out.println(L_STRQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_STRQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				L_flgCHKFL = true;
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)	
		{
			setMSG("Error in chkEXIST : "+L_EX,'E');
		}
		return L_flgCHKFL;
	}


	/** One time data capturing for Product Category
	*	into the Hash Table
	*/
	 private void crtPRCAT()
	{
		String L_strSQLQRY = "";
	    try
	    {
	        hstPRCAT.clear();
	        L_strSQLQRY = "select SUBSTRING(pr_prdcd,1,6) pr_prcat, min(pr_prdds) pr_prdds from co_prmst group by SUBSTRING(pr_prdcd,1,6)  order by  SUBSTRING(pr_prdcd,1,6) ";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	             setMSG("Product Records not found in CO_PRMST",'E');
	              return;
	        }
	        while(true)
	        {
	            strPRCAT = getRSTVAL(L_rstRSSET,"PR_PRCAT","C");
	            String[] staPRCAT = new String[intPRCAT_TOT];
	            staPRCAT[intAE_PR_PRCAT] = strPRCAT;
	            staPRCAT[intAE_PR_CATDS] = getRSTVAL(L_rstRSSET,"PR_PRDDS","C");
	            //staPRMST[intAE_PR_AVGRT] = getRSTVAL(L_rstRSSET,"PR_AVGRT","N");
	            hstPRCAT.put(strPRCAT,staPRCAT);
	            if (!L_rstRSSET.next())
					break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
			setMSG("crtPRCAT : "+L_EX,'E');
	    }
		return;
	}


		 
	/** Picking up Product Master Details
	 * @param LP_PRDCD		Product Code 
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
	private String getPRCAT(String LP_PRCAT, String LP_FLDNM)
	{
		String L_RETSTR = "";
		try
		{
		    String[] staPRCAT = (String[])hstPRCAT.get(LP_PRCAT);
		    if (LP_FLDNM.equals("PR_PRCAT"))
				L_RETSTR = staPRCAT[intAE_PR_PRCAT];
		    else if (LP_FLDNM.equals("PR_CATDS"))
				L_RETSTR = staPRCAT[intAE_PR_CATDS];
		}
		catch (Exception L_EX)
		{
			setMSG("getPRCAT"+L_EX,'E');
		}
		return L_RETSTR;
	}
 
	/** One time data capturing for Product Master
	*	into the Hash Table
	*/
	 private void crtPRMST()
	{
		String L_strSQLQRY = "";
	    try
	    {
	        hstPRMST.clear();
	        L_strSQLQRY = "select pr_prdcd, pr_prdds from co_prmst where SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54','SX') order by pr_prdcd";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
				setMSG("Product Records not found in CO_PRMST",'E');
	            return;
	        }
	        while(true)
	        {
	            strPRDCD1 = getRSTVAL(L_rstRSSET,"PR_PRDCD","C");
	            String[] staPRMST = new String[intPRMST_TOT];
	            staPRMST[intAE_PR_PRDDS] = getRSTVAL(L_rstRSSET,"PR_PRDDS","C");
	            hstPRMST.put(strPRDCD1,staPRMST);
	            if (!L_rstRSSET.next())
					break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
			setMSG("crtPRMST : "+L_EX,'E');
	    }
	return;
	}
		 
		 
		 
		 
	/** Picking up Product Master Details
	 * @param LP_PRDCD		Product Code 
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
	private String getPRMST(String LP_PRDCD, String LP_FLDNM)
	{
		String L_RETSTR = "";
		try
		{
		    String[] staPRMST = (String[])hstPRMST.get(LP_PRDCD);
		    if (LP_FLDNM.equals("PR_PRDDS"))
				L_RETSTR = staPRMST[intAE_PR_PRDDS];
		}
		catch (Exception L_EX)
		{
			setMSG("getPRMST"+L_EX,'E');
		}
		return L_RETSTR;
	}
 
	/**  Conversion of Hash table keys to sorted array
	 *   For retrieving elements in sorted order
	 */
	private Object[] setHST_ARR(Hashtable LP_HSTNM)
	{
		Object[] arrHSTKEY1=null;
		try
		{
			arrHSTKEY1 = (Object[]) LP_HSTNM.keySet().toArray();
			Arrays.sort(arrHSTKEY1);
		}
		catch(Exception e)
		{
			setMSG("setHST_ARR "+e,'E');
		}
		return arrHSTKEY1;
	}
 



/** Setting initial/default values in Combo Box (taking code as a parameter)
 */
	private void setCMBDFT(JComboBox LP_CMBNM, String LP_CODCT, String LP_CMBVL)
	{
		try
		{
			LP_CMBNM.removeActionListener(this);
			int L_intCMBITMS = LP_CMBNM.getItemCount();
			String L_strCMBDS = getCDTRN(LP_CODCT+LP_CMBVL,"CMT_CODDS", hstCDTRN)+"| "+LP_CMBVL;
			//System.out.println(L_strCMBDS);
			for(int i=0;i<LP_CMBNM.getItemCount();i++)
			{
				//System.out.println(LP_CMBNM.getItemAt(i).toString());
				if((LP_CMBNM.getItemAt(i).toString()).equalsIgnoreCase(L_strCMBDS))
				{
					LP_CMBNM.setSelectedItem(LP_CMBNM.getItemAt(i).toString());
					LP_CMBNM.setSelectedIndex(i);
					break;
				}
			}
			LP_CMBNM.addActionListener(this);
		}
		catch(Exception e)
		{
			setMSG("setCMBDFT : "+e,'E');
		}
	}
	
	
}
