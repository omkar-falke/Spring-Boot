/*
System Name   : Material Management System
Program Name  : Catalouge Master Query
Program Desc. : 
Author        : A.T.Chaudhari
Date          : 08/04/2004
Version       : MMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.Vector;import java.util.Hashtable;import java.util.StringTokenizer;

class mm_qrctm extends cl_pbase
{
	private JTextField txtCATCD,txtCATDS,txtCRFCD,txtMFPRN;
	private JTextField txtMATCD,txtMATDS,txtUOMCD,txtPRTNO,txtMATRF,txtLVLNO;
	private JButton btnPREV,btnNEXT,btnFIRST,btnLAST;
	private cl_JTable tblMATDS;
	private JPanel pnlMATDS;
	ResultSet rstRSTMP;
	//***ADDED BY AAP 31/05/04
	private String strPRTFL,strMATCD;/** To hold base details from CTMST. Holds in tabular format in the sequence of resultset columns*/
	private String [][] staQRDAT;/** To hold materails code wise CTTRN details. Format : Key - MAterial code; Value - Vector of Strings in format LINNO|DESCRIPTION|PRINTFLAG */
	private Hashtable<String,Object> hstQRDAT;/** Variable to track current record being displayed */
	private int intROWID=-1;
	private int intRECCT=-1;
	///***
	
	mm_qrctm()
	{
		super(2);
		pnlMATDS = new JPanel(null);
		pnlMATDS.setBorder(new LineBorder(Color.lightGray,2));
		txtCATCD = new TxtNumLimit(10.0);
		txtCATDS = new TxtLimit(60);
		txtCRFCD = new TxtLimit(10);
		txtMFPRN = new TxtLimit(15);
		
		txtMATCD = new TxtNumLimit(10.0);
		txtMATDS = new TxtLimit(60);
		txtMATRF = new TxtLimit(20);
		txtLVLNO = new TxtLimit(2);
		txtPRTNO = new TxtLimit(20);
		txtUOMCD = new TxtLimit(10);
		
		btnPREV = new JButton("<");
		btnNEXT = new JButton(">");
				
		setMatrix(20,8);
		
		add(new JLabel("Catalog Code "),2,2,1,1.5,this,'L');
		add(txtCATCD,2,3,1,1,this,'L');
		
		add(new JLabel("Partial Description "),2,4,1,2,this,'L');
		add(txtCATDS,2,7,1,2.75,this,'R');
		
		add(new JLabel("Cross Ref. Code "),3,2,1,2,this,'L');
		add(txtCRFCD,3,3,1,1,this,'L');

		add(new JLabel("Manufacturer Part No "),3,4,1,2,this,'L');
		add(txtMFPRN,3,6,1,1.66,this,'R');
		
		add(new JLabel("Catalog Code"),1,2,1,1,pnlMATDS,'L');
		add(txtMATCD,1,3,1,1,pnlMATDS,'L');
		add(new JLabel("Description"),1,4,1,1,pnlMATDS,'L');
		add(txtMATDS,1,5,1,3.3,pnlMATDS,'L');
		
		add(new JLabel("Cross Ref.  "),2,2,1,1,pnlMATDS,'L');
		add(txtMATRF,2,3,1,1,pnlMATDS,'L');
		
		add(new JLabel("Level No  "),2,4,1,1,pnlMATDS,'L');
		add(txtLVLNO,2,5,1,1,pnlMATDS,'L');
		
		add(new JLabel("UOM  "),3,2,1,1,pnlMATDS,'L');
		add(txtUOMCD,3,3,1,1,pnlMATDS,'L');
		
		add(new JLabel("Mfg. Part No.  "),3,4,1,1,pnlMATDS,'L');
		add(txtPRTNO,3,5,1,2,pnlMATDS,'L');
		
			
		tblMATDS = crtTBLPNL1(pnlMATDS,new String[]{"","Line No.","Description","Print Flag"},20,4,1,6.5,7.9,new int[]{20,70,560,70},new int[]{0,3});
		add(btnFIRST=new JButton("<<"),12,3,1,0.8,pnlMATDS,'L');
		add(btnPREV,12,4,1,0.8,pnlMATDS,'L');
		add(btnNEXT,12,5,1,0.8,pnlMATDS,'L');
		add(btnLAST=new JButton(">>"),12,6,1,0.8,pnlMATDS,'L');
		
		add(pnlMATDS,5,1,14,8,this,'L');
		
		txtCATCD.setInputVerifier(new INPVF());
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);	
				txtCATCD.requestFocus();
			}
			else 
				setMSG("Invalid Option ..",'E');	
		}
		if(M_objSOURC == btnPREV)
		{
			String L_strTEMP=staQRDAT[intROWID][0];
			for(int i=intROWID;i>=0;i--)
				if(!L_strTEMP.equals(staQRDAT[i][0]))
				{
					intRECCT--;
					dspDATA(i);
					break;
				}
//			dspDATA(--intROWID);
		}
		if(M_objSOURC == btnNEXT)
		{
			String L_strTEMP=staQRDAT[intROWID][0];
			for(int i=intROWID;i<staQRDAT.length;i++)
				if(!L_strTEMP.equals(staQRDAT[i][0]))
				{
					intRECCT++;
					dspDATA(i);
					break;
				}
//			dspDATA(++intROWID);
		}
		else if(M_objSOURC == btnFIRST)
		{
			intRECCT=1;
			dspDATA(0);
		}
		else if(M_objSOURC == btnLAST)
		{
			intRECCT=hstQRDAT.size();
			dspDATA(staQRDAT.length-1);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtCATCD)
			{
				setCursor(cl_dat. M_curWTSTS_pbst);
				M_strHLPFLD = "txtCATCD";
				M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_CODTP = 'CD' AND CT_MATCD LIKE '"+txtCATCD.getText()+"%' ORDER BY CT_MATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");
				setCursor(cl_dat. M_curDFSTS_pbst);
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtCATCD)
				if(txtCATCD.getText().trim().length() > 0)
				{
					txtCATDS.requestFocus();
					setMSG("Enter Short Description.. ",'N');
				}
			else
			{
				txtCATCD.requestFocus();
				setMSG("Enter Material Code ",'E');
			}
			if(M_objSOURC == txtCATDS)
			{
				if(txtCATDS.getText().length() != 0)
				{
					txtCATDS.setText(txtCATDS.getText().toString().toUpperCase());
				}
				txtCRFCD.requestFocus();
				setMSG("Enter Cross Ref. Code... ",'N');
			}
			if(M_objSOURC == txtCRFCD)
			{
				txtMFPRN.requestFocus();
				setMSG("Enter Manifacturing Part No. ",'N');
			}
			if(M_objSOURC == txtMFPRN)
			{
				//getDATA();
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			setMSG("",'N');
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtCATCD")
		{
			txtCATCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtCATCD.requestFocus();
		}
	}
	/**
	 * Method to get data from back end which fits in user defined criteria
	 * 
	 * Method to get data from back end which fits in user defined criteria
	 * 
	 * <p>Adds filter to the qry for MATCD and/or MATDS and/or PARTIAL DESCRIPTION.<br>
	 * Puts the data into staQRDAT string array. size of the array is determined using ResultSet MEtadata<br>
	 * Puts data in the same sequence as that of resultset.<br>
	 * If no data is found, displays msg and returns. Otherwise, displays first record.
	 */
	void exePRINT()
	{
		try
		{
			setCursor(cl_dat. M_curWTSTS_pbst);
			txtCATDS.setText(txtCATDS.getText().toUpperCase());
//IMPORTANT : DO NOT CHANGE SEQUESNCE OF THE COLUMNS IN THE QRY as RECORD DISPLAY USES COLUMN INDEX INSTEAD OF DESCRIPTION IN dspDATA() method
			M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS,CT_UOMCD,CT_PRTNO,CT_MATRF,CTT_LVLNO,CTT_LINNO,CTT_MATDS,"
				+"CTT_PRTFL FROM CO_CTMST,CO_CTTRN WHERE "
				+"CT_MATCD = CTT_MATCD AND CT_CODTP = CTT_CODTP AND CT_CODTP IN ('CD','CR') ";
			if(txtCATCD.getText().trim().length() != 0)
				M_strSQLQRY +=" AND CT_MATCD LIKE '"+txtCATCD.getText()+"%' ";
			if(txtCATDS.getText().trim().length() != 0)
				M_strSQLQRY +=" AND CT_MATDS LIKE '%"+txtCATDS.getText()+"%' ";
			if(txtCRFCD.getText().trim().length() != 0)
				M_strSQLQRY +=" AND CT_MATRF LIKE '"+txtCRFCD.getText()+"%' ";
			if(txtMFPRN.getText().trim().length() != 0)
				M_strSQLQRY +=" AND CT_PRTNO LIKE '"+txtMFPRN.getText()+"%' ";
			M_strSQLQRY +="ORDER BY CT_MATCD,CTT_LINNO ";
			
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
																			   ResultSet.CONCUR_UPDATABLE);
			System.out.println(M_strSQLQRY);
			rstRSTMP = L_stat.executeQuery(M_strSQLQRY);
			if(rstRSTMP != null)
			{
				ResultSetMetaData L_rmtTEMP= rstRSTMP.getMetaData();
				int L_intROWCT=0;
				int L_intCOLCT=L_rmtTEMP.getColumnCount();
				String L_strMATCD="";
				int L_intROW=0;
				while(rstRSTMP.next())
					L_intROWCT++;
				//L_intROWCT--;//decrement by one for end of resultset row. (After last)
				if(L_intROWCT<=0)
				{//If result set is empty, return
					tblMATDS.clrTABLE();
					setMSG("No data Found ..",'E');
					setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				staQRDAT=new String[L_intROWCT][L_intCOLCT];//Array for result set data
				hstQRDAT=new Hashtable<String,Object>(20,0.8f);//Hash table for CTT data
				Vector<String> L_vtrTEMP=new Vector<String>(10,2);//Vector for CTT data of a material code
				rstRSTMP.beforeFirst();
				while(rstRSTMP.next())
				{//put data in string array and hashtable
//					if(!L_strMATCD.equals(rstRSTMP.getString("CT_MATCD")))
					{
						for (int i=0;i<L_intCOLCT;i++)
						{//Check for datatype DATE
							
							if(L_rmtTEMP.getColumnType(i+1)==91)
							{
								if(rstRSTMP.getDate(i+1)!=null)
									staQRDAT[L_intROW][i]=rstRSTMP.getDate(i+1).toString();
							}
							else
								staQRDAT[L_intROW][i]=rstRSTMP.getString(i+1);
						}
						
					}
					L_strMATCD=rstRSTMP.getString("CT_MATCD");
					if(L_intROW>0)
					{
						if(!staQRDAT[L_intROW-1][0].equals(rstRSTMP.getString("CT_MATCD")))
						{//if details of a material code are over, put vector in hast table adn create new one
							hstQRDAT.put(staQRDAT[L_intROW-1][0],L_vtrTEMP);
							L_vtrTEMP=new Vector<String>(10,2);
						}
					}
					L_vtrTEMP.addElement(rstRSTMP.getString("CTT_LINNO")+"|"+rstRSTMP.getString("CTT_MATDS")+"|"+rstRSTMP.getString("CTT_PRTFL"));
					L_intROW++;
				}
				hstQRDAT.put(staQRDAT[L_intROW-1][0],L_vtrTEMP);//For last material code
				intRECCT=1;
				dspDATA(0);//Display first record
				setMSG("Press 'NEXT' Or 'PREV' To View Records",'N');
			}
			setCursor(cl_dat. M_curDFSTS_pbst);
		}
		catch(Exception L_SQLE)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_SQLE,"exePRINT");
		}
	}
	/**
	 * Method to display record at a given index of staQRDAT.
	 * 
	 * Method to display record at a given index of staQRDAT.
	 * 
	 * <p>Sets state of the navigation buttons and displays data cinsidering that, sequence of values in staQRDAT is appropriate.
	 * 
	 * @param P_intROWID Index of the row in staQRDAT to be displayed
	 */
	private void dspDATA(int P_intROWID)
	{
		try
		{
			if(staQRDAT==null)
			{//DATA ARRAY NOT INITIALISED
				setMSG("No data found ..",'E');
				return;
			}
			if(!hstQRDAT.containsKey(staQRDAT[P_intROWID][0]))
			{///LINE DESCRIPTION VECTOR NOT FOUND
				setMSG("No data found ..",'E');
				return;
			}
			intROWID=P_intROWID;//copy parameter in global veriable
			//set button states and focus component
			btnFIRST.setEnabled(intRECCT >1);
			btnPREV.setEnabled(intRECCT >1);
			btnLAST.setEnabled(intRECCT <hstQRDAT.size());
			btnNEXT.setEnabled(intRECCT <hstQRDAT.size());
			if(!(intRECCT <hstQRDAT.size()))
				btnPREV.requestFocus();
			else if(!(intRECCT >1))
				btnNEXT.grabFocus();
			updateUI();
			//Display data from string arrya staQRDAT
			txtMATCD.setText(nvlSTRVL(staQRDAT[P_intROWID][0],""));
			txtMATDS.setText(nvlSTRVL(staQRDAT[P_intROWID][1],"")); 
			txtUOMCD.setText(nvlSTRVL(staQRDAT[P_intROWID][2],""));
			txtPRTNO.setText(nvlSTRVL(staQRDAT[P_intROWID][3],""));
			txtMATRF.setText(nvlSTRVL(staQRDAT[P_intROWID][4],""));
			txtLVLNO.setText(nvlSTRVL(staQRDAT[P_intROWID][5],""));
			
			
			//Display data from vector in hashtable in the JTable
			tblMATDS.clrTABLE();
			Vector L_vtrTEMP=(Vector)hstQRDAT.get(staQRDAT[P_intROWID][0]);
			StringTokenizer L_stkTEMP=null;
			for(int i=0;i<L_vtrTEMP.size();i++)
			{//Retrieve data from vector & display in table using string tokenizer
				L_stkTEMP=new StringTokenizer((String)L_vtrTEMP.elementAt(i),"|");
				tblMATDS.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,1);
				tblMATDS.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,2);
				if(L_stkTEMP.nextToken().equalsIgnoreCase("Y"))
					tblMATDS.setValueAt(Boolean.TRUE,i,3);
				else
					tblMATDS.setValueAt(Boolean.FALSE,i,3);
			}
			setMSG("Record "+Integer.toString(intRECCT)+" of "+Integer.toString(hstQRDAT.size()),'N');
		}catch(Exception e)
		{setMSG(e,"Child.dspDATA");}
	}
	void setENBL(boolean flgENBL)
	{
		txtCATCD.setEnabled(flgENBL);txtCATDS.setEnabled(flgENBL);txtCRFCD.setEnabled(flgENBL);
		txtMFPRN.setEnabled(flgENBL);
		tblMATDS.setEnabled(false);txtMATCD.setEnabled(false);txtMATDS.setEnabled(false);
		txtMATRF.setEnabled(false);txtLVLNO.setEnabled(false);txtPRTNO.setEnabled(false);
		txtUOMCD.setEnabled(false);btnPREV.setEnabled(false);btnNEXT.setEnabled(false);
		btnFIRST.setEnabled(false);btnLAST.setEnabled(false);
	}
	/**
	 * Method to clear Components
	 * 
	 * Method to clear Components
	 * 
	 * <p>Overridden to erase earlier search data in staing array and hashtable
	 */
	void clrCOMP()
	{
		super.clrCOMP();
		staQRDAT=null;
		hstQRDAT=null;
	}
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(input == txtCATCD)
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select count(*) from CO_CTMST where CT_MATCD like '"+((JTextField)input).getText()+"%'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								setMSG("Invalid Input ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Input ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Input ",'E');
						return false;
					}
				}
				return true;
			}
			catch(SQLException L_SQL)
			{
				setMSG(L_SQL,"verify");
				return false;
			}
		}
	}	
}
