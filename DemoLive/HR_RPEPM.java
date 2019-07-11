/*
System Name   : Material Management System
Program Name  : List Of Daily Material received
Program Desc. : 
Author        : 
Date          : 28/03/2006
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :30/03/06
Modified det.  :
Version        :
*/
import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.JButton;import java.util.Vector;import java.util.StringTokenizer;
import javax.swing.JList;import java.util.regex.Pattern;import javax.swing.JTable;
import javax.swing.JScrollPane;import javax.swing.JCheckBox;import javax.swing.JPanel;import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;



//import java.sql.ResultSetMetaData;
/**<pre>
System Name : Material Management System.
 
Program Name : List Of ng GRIN

Purpose : This program generates Report for Product Specification Sheet .

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_QPMST       WB_DOCTP,WB_DOCNO,WB_SRLNO                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtPRDCD   PR_PRDCD                  CO_PRMST            Limit        Gate-In Date
txtPRDDS   PR_PRDDS                  CO_PRMST            Limit
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
     For Product Specification Sheet of the Perticuler Product 
   //For List Of ending DMR Report Data is taken from MM_WBTRN for condiations :- 
        1) ifnull(WB_DOCRF,' ') = ' ' 
        2) AND ifnull(WB_DOCTP,' ') = '02' 
        3) AND WB_GINDT IS NOT null 
        4) AND ifnull(WB_STSFL,'') <> 'X'

For Daily Material List Report Data id taken from MM_WBTRN for :-
        1) WB_DOCTP = '02' 
        2) AND date(WB_GINDT) =is between the Form Date to ToDate
        3) AND ifnull(WB_STSFL,'') <> 'X'

<B>Validations & Other Information:</B>    
    - For Daily Material List Gate in Date Entered must be valid.
</I> */

class hr_rpepm extends cl_rbase
{
	private JTextField txtHRVAL ;
	private JTextField txtTOVAL;
	private JComboBox cmbHRCON;		/** JComboBox to display & Select Condition.*/
	private JComboBox cmbFNTSZ;		/** JComboBox to display & to Specify the Page Size for the Report.*/
	private JComboBox cmbPAGSZ;		/** JTable to display & select the columns requried in the Report.*/
	private JList lstORDBY;			/** JList for Display The Order By  */
	private JList lstDISPL;			/** JList for Display The Selected criteria  */
	private JList lstCOLDS;          /** JList for Criteria & Column Description */
	private String strVDATE;         /** String Variable for Valid Date.*/ 
	
	//private Vector<String> vtrORDBY;         /** Vector for Checking Duplicate Element Display List   */
	private Vector<String> vtrCHECK;         /** Vector for Checking Duplicate Element Display List   */
	private Vector<String> vtrHRCRT;         /** Vector for Diplay Criteria   */
	private Vector<String> vtrCOLDS;         /** Vector for Column Description   */
	private Vector<String> vtrDESPL;         /** Vector for Display selected element in List  */
	private Vector<String> vtrSQLQRY;        /** Vector to store element for SQL Query  */
	private JButton btnADD;
	private JButton btnDEL;
	private JLabel lblVALUE;
	private JLabel lblFRVAL;
	private JLabel lblTOVAL;
	private StringBuffer stbDOTLN = new StringBuffer("-");
	private StringBuffer stbCOLLS;
	private String strTHEAD="";			/** String Variable for dynamically generated Table Header.*/
	private int intROWLN;				/** Integer variable for number of charector in row for selected Page Size.*/
	private int arrCOLWD[];				/** Array of integers for Selected Column Width.*/
	private JScrollPane srpORDBY;       /**JScrollPane for Displaying the Colunm Discription */
	private JScrollPane srpCOLDS;       /**JScrollPane for Displaying the Colunm Discription */
	private JScrollPane srpDISPL;		/**JScrollPane for Displaying the selected Criteria */
	private int intCOUNT;				/**Integer variable for increment the count in loop */
	private JCheckBox chkSELCT;			/**	JCheckBox to select custom Report.*/
	private JPanel pnlTABLE;			/** Jpanel to place all the Components requried for custom Report.*/
	private cl_JTable tblITMDT;			/** JTable to display & select the columns requried in the Report.*/
	private final int TBL_SELCT = 0;	/**	Final integer to specify the check flag Column.*/
	private final int TBL_CTEXT = 1;	/**	Final integer to specify the Column Name Column.*/
	private final int TBL_CSIZE = 2;	/**	Final integer to specify the Column TYPE Column.*/
	private final int TBL_CTYPE = 3;    /**	Final integer to specify the Column TYPE Column.*/
	private final int TBL_CNAME = 4;	/**	Final integer to specify the Column TYPE Column.*/
	private int intNOLIN;				/** Integer variable for number of lines in selected page.*/
	private int intNOCHR;				/** Integer variable for number of charectores in selected page.*/
	private String strFNTSZ="";			/** String Variable for selected Font size*/
	private int intFLAG=0;              /** Integer Varaiable for using Flag*/	                                     
	private String strFILNM;	         /** String Variable for generated Report file Name.*/ 
	private int intRECCT;		         /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	int TB_CHKFL = 0;					  JCheckBox chkCHKFL;
	private Hashtable<String,String> hstITMDT;
	
	hr_rpepm()
	{
		super(1);
		setMatrix(20,9);
		//vtrORDBY=new Vector<String>(1);
		vtrCHECK=new Vector<String>(1);
		vtrDESPL=new Vector<String>(1);
		vtrSQLQRY=new Vector<String>(1);
		stbCOLLS = new  StringBuffer(); 	
		hstITMDT= new Hashtable<String,String>();	
		try
		{
			vtrHRCRT=new Vector<String>(5,1);
			vtrCOLDS=new Vector<String>(5,1);
			lstCOLDS=new JList();
			M_strSQLQRY = "Select CMT_CODDS,CMT_SHRDS,CMT_CHP01,CMT_NMP01 from CO_CDTRN where CMT_CGMTP ='SYS'"
				+ " AND CMT_CGSTP = 'HRXXCRT'" ;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
			if(M_rstRSSET!=null)
			{
				String L_strTEMP1="";
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP1=  nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"-");
					L_strTEMP = L_strTEMP1;
					L_strTEMP +=  "|";
					L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"-");
					L_strTEMP +=  "|";
					L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"-");
					L_strTEMP +=  "|";
					L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("CMT_NMP01"),"-");
					vtrCOLDS.add(L_strTEMP1);
					vtrHRCRT.add(L_strTEMP);					
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
		for(int k=0;k<vtrCOLDS.size();k++)
		{
			lstCOLDS.setListData(vtrCOLDS);
		}
		cmbHRCON =new JComboBox();
		btnADD =new  JButton("Add");
		btnDEL =new  JButton("Del");
		//DefaultListModel model = new DefaultListModel();
		lstORDBY=new JList();
		lstORDBY.setName("s");
		hr_rpepm_list arrayListHandler = new hr_rpepm_list();
		lstORDBY.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        lstORDBY.setTransferHandler(arrayListHandler);
        lstORDBY.setDragEnabled(true);

		lstDISPL=new JList();
		pnlTABLE= new JPanel(null);
		add(chkSELCT = new JCheckBox("Custom Report"),1,6,1,1.5,this,'L');					
		String[] L_COLHD = {"Select","Fields Name","Width","data Type","Column Name"};
      	int[] L_COLSZ = {30,250,10,10,10};	    				
		tblITMDT = crtTBLPNL1(pnlTABLE,L_COLHD,100,2,1,6,2.7,L_COLSZ,new int[]{0});	
		add(new JLabel("Order"),2,5,1,2,pnlTABLE,'L');
		add(lstORDBY,3,5,5,2,pnlTABLE,'L');
		srpORDBY = new JScrollPane(lstORDBY);
		add(srpORDBY,3,5,5,2,pnlTABLE,'L');
		
		//add(new JLabel("Font"),2,4,1,1,pnlTABLE,'R');
		add(cmbFNTSZ = new JComboBox(),2,5,1,1.7,pnlTABLE,'L');
		//add(new JLabel("Page Size"),4,4,1,1,pnlTABLE,'R');
		add(cmbPAGSZ = new JComboBox(),4,5,1,1.7,pnlTABLE,'L');
				
		cmbPAGSZ.addItem("Plain A4");
		cmbPAGSZ.addItem("Fanfold 210mm - 12in");
		cmbPAGSZ.addItem("Fanfold 358mm - 12in");
				
		add(pnlTABLE,1,7,8,6.55,this,'R');
		add(new JLabel("Criteria"),9,2,1,1,this,'R');
		add(new JLabel("Condition"),11,4,1,1,this,'L');
		add(lblVALUE = new JLabel("Value"),11,5,1,1,this,'L');
		add(lblFRVAL = new JLabel("Value From"),11,5,1,1,this,'L');
		add(lblTOVAL=new JLabel("TO "),13,5,1,1,this,'L');
		add(lstCOLDS,10,2,3,2,this,'L');
		srpCOLDS = new JScrollPane(lstCOLDS);
		add(srpCOLDS,10,2,7,2,this,'L');
		cmbHRCON.addItem("Select Condition");
		cmbHRCON.addItem("=");
		cmbHRCON.addItem(">");
		cmbHRCON.addItem("<");
		cmbHRCON.addItem("Between");
		add(cmbHRCON,12,4,1,1,this,'L');
		add(txtHRVAL = new TxtLimit(20),12,5,1,1,this,'L');
		add(txtTOVAL = new TxtLimit(20),14,5,1,1,this,'L');
		add(btnADD,12,6,1,1,this,'L');
		add(btnDEL,13,6,1,1,this,'L');
		add(lstDISPL,10,7,10,2,this,'L');
		srpDISPL = new JScrollPane(lstDISPL);
		add(srpDISPL,10,7,7,2,this,'L');
		M_pnlRPFMT.setVisible(true);
		lblTOVAL.setVisible(false);
		lblFRVAL.setVisible(false);
		txtTOVAL.setVisible(false);
		pnlTABLE.setVisible(false);
		tblITMDT.setCellEditor(TB_CHKFL,chkCHKFL=new JCheckBox());		
		//tblITMDT.addKeyListener(this);
		//tblITMDT.addMouseListener(this);
		((JCheckBox) tblITMDT.cmpEDITR[TB_CHKFL]).addMouseListener(this);
		//((JCheckBox) tblITMDT.cmpEDITR[TB_CHKFL]).addKeyListener(this);
		
		tblITMDT.cmpEDITR[TBL_CNAME].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_CSIZE].setEnabled(false);
		setENBL(false);	

		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		
		cmbPAGSZ.setVisible(false);
		cmbFNTSZ.setVisible(false);
	}	 
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		if(false==L_flgSTAT)
		{
			clrCOMP();
		}
	}	
	
	public void mousePressed(MouseEvent L_ME)
	{
		super.mousePressed(L_ME);
		try
		{
			
		}
		catch(Exception e){setMSG(e,"mousePressed");}
	}
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			if(M_objSOURC==chkCHKFL)
			{	
				if(chkCHKFL.isSelected())
				{
					javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
					for(int i=0 ; i<this.lstORDBY.getModel().getSize() ; i++ )
					{
						listModel.addElement(this.lstORDBY.getModel().getElementAt(i));
					}
					listModel.addElement(tblITMDT.getValueAt(tblITMDT.getSelectedRow(),1).toString());
					this.lstORDBY.setModel(listModel);
					//vtrORDBY.addElement(tblITMDT.getValueAt(tblITMDT.getSelectedRow(),4).toString());
				}
				if(!chkCHKFL.isSelected())
				{
					javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
					for(int i=0 ; i<this.lstORDBY.getModel().getSize() ; i++ )
					{
						listModel.addElement(this.lstORDBY.getModel().getElementAt(i));
					}
					listModel.removeElement(tblITMDT.getValueAt(tblITMDT.getSelectedRow(),1).toString());
					this.lstORDBY.setModel(listModel);
					//vtrORDBY.removeElement(tblITMDT.getValueAt(tblITMDT.getSelectedRow(),4).toString());
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}	
	
	public void actionPerformed(ActionEvent L_AE)
	{
		intCOUNT=0;
		cl_dat.M_intLINNO_pbst=0;
		super.actionPerformed(L_AE);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
			M_pnlRPFMT.setVisible(false);
		else
			M_pnlRPFMT.setVisible(true);	
		
		if(M_objSOURC == chkSELCT)
		{
			if(chkSELCT.isSelected())
			{
				pnlTABLE.setVisible(true);
				try
				{
					M_strSQLQRY="Select column_name,length,data_type,column_text from SYSColumns where Table_name='HR_EPMST' and column_text is not null";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
					int i=0;
					hstITMDT.clear();
					if(M_rstRSSET!=null)
					{
						while(M_rstRSSET.next())
						{
							tblITMDT.setValueAt(M_rstRSSET.getString("column_text"),i,TBL_CTEXT);
							tblITMDT.setValueAt(M_rstRSSET.getString("length"),i,TBL_CSIZE);
							tblITMDT.setValueAt(M_rstRSSET.getString("data_type"),i,TBL_CTYPE);
							tblITMDT.setValueAt(M_rstRSSET.getString("column_name"),i,TBL_CNAME);
							hstITMDT.put(M_rstRSSET.getString("column_text"),M_rstRSSET.getString("column_name"));
							i++;
						}
					}
					if(M_rstRSSET!=null)
						M_rstRSSET.close();
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"Displaying data in the table");
				}
				if(M_rdbHTML.isSelected())
				{
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("Size 8");
					cmbFNTSZ.addItem("Size 9");
					cmbFNTSZ.addItem("Size 10");
				}		
				if(M_rdbTEXT.isSelected())
				{			
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("CPI10");
					cmbFNTSZ.addItem("CPI12");
					cmbFNTSZ.addItem("CPI15");
					cmbFNTSZ.addItem("CPI17");					
				}
			}
			else
			{
				pnlTABLE.setVisible(false);
				strFNTSZ="";
			}
		}
		if(M_objSOURC == cmbFNTSZ)		
		strFNTSZ = cmbFNTSZ.getSelectedItem().toString().trim();
		try
		{
			if(M_objSOURC == M_rdbHTML)
			{
				if(chkSELCT.isSelected())
				{
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("Size 8");
					cmbFNTSZ.addItem("Size 9");
					cmbFNTSZ.addItem("Size 10");
				}
			}
			if(M_objSOURC == M_rdbTEXT)
			{
				if(chkSELCT.isSelected())
				{
					cmbFNTSZ.removeAllItems();
					cmbFNTSZ.addItem("CPI10");
					cmbFNTSZ.addItem("CPI12");
					cmbFNTSZ.addItem("CPI15");
					cmbFNTSZ.addItem("CPI17");					
				}
			}		
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{					
				if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)	
				{
					cl_dat.M_cmbOPTN_pbst.requestFocus();				
					setENBL(false);				
				}
				else
				{
					setENBL(true);
					setMSG("Please select Criteria from List..",'N');				
					lstCOLDS.requestFocus();
				}				
			}		
			if(M_objSOURC==cmbHRCON)
			{
				setMSG("Pease select Condition ",'N');
				if(cmbHRCON.getSelectedIndex()==4)
				{
					lblTOVAL.setVisible(true);
					lblFRVAL.setVisible(true);
					txtTOVAL.setVisible(true);
				}	
				else
				{
					lblFRVAL.setVisible(false);
					lblTOVAL.setVisible(false);
					txtTOVAL.setVisible(false);
				}		
			}
			if(M_objSOURC == btnADD)
			{
				addDATA();
			}
			if(M_objSOURC == btnDEL)
			{
				if(lstCOLDS.isSelectionEmpty())
				{
					setMSG("Please Select The Condition To Remove ",'E');
				}
				else
				{
					vtrDESPL.removeElementAt(lstDISPL.getSelectedIndex());
					vtrSQLQRY.removeElementAt(lstDISPL.getSelectedIndex());
					vtrCHECK.removeElementAt(lstDISPL.getSelectedIndex());
					setENBL(true);
					for(int k=0;k<vtrDESPL.size();k++)
					{
						lstDISPL.setListData(vtrDESPL);
					}
					if(chkSELCT.isSelected())
					{
						setENBL(false);		
						pnlTABLE.setVisible(true);
						try
						{
							M_strSQLQRY="Select column_name,length,data_type,column_text from SYSColumns where Table_name='HR_EPMST'";
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
							int i=0;
							if(M_rstRSSET!=null)
							{
								while(M_rstRSSET.next())
								{
									tblITMDT.setValueAt(M_rstRSSET.getString("column_text"),i,TBL_CTEXT);
									tblITMDT.setValueAt(M_rstRSSET.getString("length"),i,TBL_CSIZE);
									tblITMDT.setValueAt(M_rstRSSET.getString("data_type"),i,TBL_CTYPE);
									tblITMDT.setValueAt(M_rstRSSET.getString("column_name"),i,TBL_CNAME);
									i++;
								}
							}
							if(M_rstRSSET!=null)
								M_rstRSSET.close();
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"Displaying data in the table");
						}
					//	for(int i=0;i<tblITMDT.getRowCount();i++)
					//	{
							//tblITMDT.setSelected(true);
					//	}
						chkSELCT.setSelected(true);
					}
					else
					{
						setENBL(false);
					}
					setENBL(true);
				}
			}
		}			
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	/**
	 *
	 */
	public void addDATA()
	{
		try
		{
			if(lstCOLDS.isSelectionEmpty())
			{
				setMSG("Please Select The Criteria ",'N');
			}
			else if(cmbHRCON.getSelectedIndex()==0)
			{
				setMSG("Please Select The Condition ",'N');
				cmbHRCON.requestFocus();
			}
			else if(txtHRVAL.getText().trim().length() == 0)
			{
				setMSG("Please Enter Proper Value ",'N');
				txtHRVAL.requestFocus();
			}
			else
			{					
				intFLAG=0;
				String L_strCRITR=(String)lstCOLDS.getSelectedValue();
				for(int k=0;k<vtrCHECK.size();k++)
				{
					if(L_strCRITR.equals(vtrCHECK.elementAt(k).toString()))
				   {
						intFLAG=1;
						setMSG(L_strCRITR+ " is Already selected Please select another criteria ",'E'); 
						lstCOLDS.requestFocus();
					}
					else
					{
					}
				}
				if(intFLAG==1)
				{				
					return;					
				}
				else
				{					
					String L_strFMVAL="";
					String L_strTOVAL="";
					String L_strTEMP="";
					String L_strTEMP1="";
					String L_strVTRVL=(String)vtrHRCRT.elementAt(lstCOLDS.getSelectedIndex());
					String staSPTVL[]=new String[4];
    				StringTokenizer sttSPTVL1=new StringTokenizer(L_strVTRVL,"|");
					while(sttSPTVL1.hasMoreTokens())
					{  
						staSPTVL[intCOUNT]=sttSPTVL1.nextToken();
						intCOUNT++;
					}
					if(cmbHRCON.getSelectedIndex()==4)
					{	
						L_strFMVAL = txtHRVAL.getText().trim();
						L_strTOVAL= txtTOVAL.getText().trim();
						if(staSPTVL[2].equals("C") || staSPTVL[2].equals("D"))
						{
							if(staSPTVL[2].equals("D"))
							{
								if(!vldDATE(L_strFMVAL))
								{
									txtHRVAL.requestFocus();
									return ;
								}
								else if(!vldDATE(L_strTOVAL))
								{
									txtTOVAL.requestFocus();
									return ;
								}
								else
								{
									String strTODAT;
									strVDATE=M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMVAL));
									strTODAT=M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTOVAL));
									
									if(M_fmtLCDAT.parse(L_strFMVAL).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
									{
										setMSG("Date can not be greater than today's date..",'E');
										txtHRVAL.requestFocus();
									}
									else if(M_fmtLCDAT.parse(L_strTOVAL).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
									{
										setMSG("Date can not be greater than today's date..",'E');
										txtTOVAL.requestFocus();
									}
									else if(M_fmtLCDAT.parse(L_strFMVAL).compareTo(M_fmtLCDAT.parse(L_strTOVAL))>0)
									{
										setMSG("To Date can not be Less than From Date ..",'E');
										txtTOVAL.requestFocus();
										return;
									}
									else
									{
										L_strTEMP= L_strCRITR+ " " +cmbHRCON.getSelectedItem().toString()+" '"+L_strFMVAL+"'";
										L_strTEMP+=" And "+L_strTOVAL;
										vtrDESPL.add(L_strTEMP);
										L_strTEMP1=staSPTVL[1]+" "+cmbHRCON.getSelectedItem().toString()+"'"+strVDATE+"'";
										L_strTEMP1+=" And '"+strTODAT+"'";
										vtrSQLQRY.add(L_strTEMP1);
										vtrCHECK.add(L_strCRITR);											
										cmbHRCON.setSelectedIndex(0);
										txtTOVAL.setText("");
									}
								}
							}
							else
							{
								if(lstCOLDS.getSelectedValue().toString().equals("Dept. Code"))
								{
									if(!vldDEPT(L_strFMVAL))
								   {
										txtHRVAL.requestFocus();
										intFLAG=1;
									}
									else if(!vldDEPT(L_strTOVAL))
									{
										txtTOVAL.requestFocus();
										intFLAG=1;
									}
									else
									{
										intFLAG=0;
									}
								}
								if(intFLAG==1)
								{
									return;
								}
								else
								{
									L_strTEMP= L_strCRITR+" " +cmbHRCON.getSelectedItem().toString()+" '"+txtHRVAL.getText()+"'";
									L_strTEMP+=" And "+L_strTOVAL;
									vtrDESPL.add(L_strTEMP);
									L_strTEMP1=staSPTVL[1]+" "+cmbHRCON.getSelectedItem().toString()+"'"+txtHRVAL.getText()+"'";
									L_strTEMP1+= " And '"+ L_strTOVAL+"'";
									vtrSQLQRY.add(L_strTEMP1);
									vtrCHECK.add(L_strCRITR);
									txtTOVAL.setText("");
								}
							}
						}
						if(staSPTVL[2].equals("N"))
						{
							L_strTEMP=L_strCRITR +" "+cmbHRCON.getSelectedItem().toString()+" "+txtHRVAL.getText();
							L_strTEMP+= "And "+ L_strTOVAL;
							vtrDESPL.add(L_strTEMP);
							L_strTEMP1=staSPTVL[1]+" "+ cmbHRCON.getSelectedItem().toString()+txtHRVAL.getText();
							L_strTEMP1+= "And "+ L_strTOVAL;
							vtrSQLQRY.add(L_strTEMP1);
							vtrCHECK.add(L_strCRITR);
							cmbHRCON.setSelectedIndex(0);
						}
					}
					else if(staSPTVL[2].equals("C") || staSPTVL[2].equals("D"))
					{
						L_strFMVAL = txtHRVAL.getText().trim();
						if(staSPTVL[2].equals("D"))
						{
							if(!vldDATE(L_strFMVAL))
							{
								txtHRVAL.requestFocus();
								return ;
							}							
							else
							{
								strVDATE=M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMVAL));
								if(M_fmtLCDAT.parse(L_strFMVAL).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
								{
									setMSG("Date can not be greater than today's date..",'E');
									txtHRVAL.requestFocus();
								}
								else
								{
									L_strTEMP= L_strCRITR+" " +cmbHRCON.getSelectedItem().toString()+" '"+L_strFMVAL+"'";
									vtrDESPL.add(L_strTEMP);
									L_strTEMP1=staSPTVL[1]+" "+cmbHRCON.getSelectedItem().toString()+" '"+strVDATE+"'";
									vtrSQLQRY.add(L_strTEMP1);
									vtrCHECK.add(L_strCRITR);											
									cmbHRCON.setSelectedIndex(0);
								}
							}
						}
						else
						{
							if(lstCOLDS.getSelectedValue().toString().equals("Dept. Code"))
							{
								if(!vldDEPT(L_strFMVAL))
							   {
									intFLAG=1;
								}
								else
								{
									intFLAG=0;
								}
							}
							if(intFLAG==1)
							{
								return;
							}	
							else
							{
								L_strTEMP= L_strCRITR+" " +cmbHRCON.getSelectedItem().toString()+" '"+txtHRVAL.getText()+"'";
								vtrDESPL.add(L_strTEMP);
								L_strTEMP1=staSPTVL[1]+cmbHRCON.getSelectedItem().toString()+"'"+txtHRVAL.getText()+"'";
								vtrSQLQRY.add(L_strTEMP1);
								vtrCHECK.add(L_strCRITR);
							}
						}
					}
					if(staSPTVL[2].equals("N"))
					{
						L_strTEMP=L_strCRITR +" "+cmbHRCON.getSelectedItem().toString()+" "+txtHRVAL.getText();
						vtrDESPL.add(L_strTEMP);
						L_strTEMP1=staSPTVL[1]+cmbHRCON.getSelectedItem().toString()+txtHRVAL.getText();
						vtrSQLQRY.add(L_strTEMP1);
						vtrCHECK.add(L_strCRITR);
						cmbHRCON.setSelectedIndex(0);
					}
				}
				for(int k=0;k<vtrDESPL.size();k++)
				{
					lstDISPL.setListData(vtrDESPL);
				}
				for(int i=0;i<vtrSQLQRY.size();i++)
				{
					String str=vtrSQLQRY.get(i).toString();
				}
				txtHRVAL.setText("");
				cmbHRCON.setSelectedIndex(0);
			}
		}
		catch(Exception E_BD)
		{
			setMSG(E_BD,"Between Data");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		if(L_KE.getKeyCode()==L_KE.VK_F1)
		{
			if(M_objSOURC == txtHRVAL)
			{
				M_strHLPFLD = "txtHRVAL";
				if(lstCOLDS.getSelectedValue().toString().equals("Dept. Code"))
				{
					if(txtHRVAL.getText().trim().length() >0)
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT'  and CMT_CODCD like '" + txtHRVAL.getText().trim() + "%'    ";
		   				cl_hlp(M_strSQLQRY,2,1,new String[]{"Dept Code","Dept Name"},2,"CT");
					}
					else
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT'";
		   				cl_hlp(M_strSQLQRY,2,1,new String[]{"Dept Code","Dept Name"},2,"CT");
					}						
				}
			}
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			if(M_objSOURC==lstCOLDS)
			{
				cmbHRCON.requestFocus();
				setMSG("Please Select Condition ",'N');
			}
			if(M_objSOURC==cmbHRCON)
			{	
				txtHRVAL.requestFocus();
				setMSG("Enter the Value ",'N');
				if(lstCOLDS.getSelectedValue().toString().equals("Dept. Code"))
				{
					setMSG("Please Enter Dept Code , Press f1 to select from list..",'N');
				}
			}
			if(M_objSOURC==txtHRVAL)
			{
				if(cmbHRCON.getSelectedIndex()==4)
				{
					setMSG("Enter the Value ",'N');
					txtTOVAL.requestFocus();
				}
				else
				{
					btnADD.requestFocus();
				}
			}
			if(M_objSOURC==txtTOVAL)
			{
				btnADD.requestFocus();
			}
			if(M_objSOURC==btnADD)
			{
				try
				{
					addDATA();
				}
				catch(Exception E_KE)
				{
					setMSG(E_KE,"Key Event");
				}
			}
		}
	}

	/**
	    * Method to fetch data from the database & to club it with header & footer.
	*/
	public void getDATA()
	{		
		try
		{
			ResultSet L_rstRSSET;
			String L_staNAME[]=new String[4];
			String L_strNAME="";
			Pattern L_patNAME;
			intRECCT = 0;	
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			String L_strSQLQRY="";
			M_strSQLQRY="";
			cl_dat.M_PAGENO=1;
			String L_strCGMTP="",L_strOCGMTP="";	
			if(vtrSQLQRY.isEmpty())
			{
				intFLAG=0;
			}
			else
			{
				for(int j=0;j<vtrSQLQRY.size();j++)
				{		
						L_strSQLQRY=(String)vtrSQLQRY.get(j);
						M_strSQLQRY += L_strSQLQRY+" And ";
				}
				intFLAG=1;
				M_strSQLQRY=M_strSQLQRY.substring(0,M_strSQLQRY.length()-4);
			}	
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Employee Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}	
			prnHEADER();
			String strORDBY="EP_DPTCD";
			//for(int i=0;i<vtrORDBY.size();i++)
			//	strORDBY = strORDBY+","+vtrORDBY.elementAt(i);
			ListModel model = lstORDBY.getModel();
			for(int i = 0; i < model.getSize(); i++) 
			{
				if(hstITMDT.containsKey(model.getElementAt(i)))
					strORDBY = strORDBY+","+hstITMDT.get(model.getElementAt(i)).toString();
			}
		
			if(chkSELCT.isSelected())
			{
				if(intFLAG==0)
				{
					M_strSQLQRY = "select  EP_FULNM,EP_EMPNO,"+stbCOLLS.toString() +" from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and EP_STSFL <> 'U' order by "+strORDBY ;
				}
				else
				{
					M_strSQLQRY = " select EP_FULNM,EP_EMPNO,"+stbCOLLS.toString() +" from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  AND EP_LFTDT is null and EP_STSFL <> 'U' AND " + M_strSQLQRY +"order by "+strORDBY;
				}
			}
			else
			{
				if(intFLAG==0)
				{
					M_strSQLQRY="SELECT EP_FULNM,EP_EMPNO,EP_DESGN,EP_DPTNM,EP_DPTCD,EP_EPLOC,EP_JONDT from HR_EPMST  where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and EP_STSFL <> 'U' order by "+strORDBY;
				}
				else
				{
					M_strSQLQRY="SELECT EP_FULNM,EP_EMPNO,EP_DESGN,EP_DPTNM,EP_DPTCD,EP_EPLOC,EP_JONDT from HR_EPMST  where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and EP_STSFL <> 'U' AND "+ M_strSQLQRY +"order by "+strORDBY;
				}
			}
			//System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);  
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;	
					if(cl_dat.M_intLINNO_pbst >intNOLIN )
					{						
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
							dosREPORT.writeBytes("\n\n");	
						else
							dosREPORT.writeBytes("\n"+ strDOTLN +"\n");	
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO +=1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}						
					if(chkSELCT.isSelected())
					{
						StringTokenizer stkTEMP = new StringTokenizer(stbCOLLS.toString(),",");
						int i=0;
						L_strNAME=nvlSTRVL(M_rstRSSET.getString("EP_FULNM"),"-");
						StringTokenizer sttSPTVL1=new StringTokenizer(L_strNAME,"|");
						L_strNAME="";
						while(sttSPTVL1.hasMoreTokens())
						{  
							L_strNAME+=sttSPTVL1.nextToken()+" ";
							intCOUNT++;
						}
						dosREPORT.writeBytes(padSTRING('R',L_strNAME,40));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
							dosREPORT.writeBytes("\t");	
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),"-"),8));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
							dosREPORT.writeBytes("\t");	
						while(stkTEMP.hasMoreTokens())
						{							
							String strTEMP = stkTEMP.nextToken();
							String strDATA = nvlSTRVL(M_rstRSSET.getString(strTEMP),"-");
							dosREPORT.writeBytes(padSTRING('R',strDATA,arrCOLWD[i]));	
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
								dosREPORT.writeBytes("\t");								
							i++;
						}
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst +=1;
					}
					else
					{
						L_strNAME=nvlSTRVL(M_rstRSSET.getString("EP_FULNM"),"-");
						StringTokenizer sttSPTVL1=new StringTokenizer(L_strNAME,"|");
						L_strNAME="";
						while(sttSPTVL1.hasMoreTokens())
						{  
							L_strNAME+=sttSPTVL1.nextToken()+" ";
							intCOUNT++;
						}					
						dosREPORT.writeBytes(padSTRING('R',L_strNAME,40));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
								dosREPORT.writeBytes("\t");								
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),"-"),8));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
								dosREPORT.writeBytes("\t");								
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_DESGN"),"-"),11));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
								dosREPORT.writeBytes("\t");								
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),"-"),20));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
								dosREPORT.writeBytes("\t");								
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
					}
					//r++;
				}
			}
				dosREPORT.writeBytes(stbDOTLN.toString());		
				setMSG("Report completed.. ",'N');			
			    fosREPORT.close();
			    dosREPORT.close();
			    setCursor(cl_dat.M_curDFSTS_pbst);
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	  * Method to generate the header of the Report.
	*/
	void prnHEADER()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_intLINNO_pbst=0;
			if(!chkSELCT.isSelected())
				intROWLN=110;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				dosREPORT.writeBytes("\t");	
			dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst ,50)+"\n");
			
			dosREPORT.writeBytes(padSTRING('R',"Employee Details",41));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				dosREPORT.writeBytes("\t");	
			dosREPORT.writeBytes(padSTRING('L',"Page No     : "+cl_dat.M_PAGENO,50)+"\n");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				dosREPORT.writeBytes("\n");	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");	
			for(int k=0;k<vtrDESPL.size();k++)
			{
				dosREPORT.writeBytes(padSTRING('R',""+vtrDESPL.elementAt(k),strDOTLN.length()-10)+"\n");
				cl_dat.M_intLINNO_pbst+=1;
			}
			
			if(chkSELCT.isSelected())
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				{
					dosREPORT.writeBytes("Employee Name");
					dosREPORT.writeBytes("\t");	
					dosREPORT.writeBytes("Emp No");
					dosREPORT.writeBytes("\t");
					dosREPORT.writeBytes(strTHEAD);
					dosREPORT.writeBytes("\t");
					dosREPORT.writeBytes("\n\n");
				}
				else
				{
					dosREPORT.writeBytes(stbDOTLN.toString()+"\n");			
					dosREPORT.writeBytes("Employee Name                           Emp No  ");
					dosREPORT.writeBytes(strTHEAD+"\n");
					dosREPORT.writeBytes(stbDOTLN.toString()+"\n");			
				}
			}
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				{
					dosREPORT.writeBytes("Employee Name");
					dosREPORT.writeBytes("\t");	
					dosREPORT.writeBytes("Emp No");
					dosREPORT.writeBytes("\t");
					dosREPORT.writeBytes("Desgn");
					dosREPORT.writeBytes("\t");
					dosREPORT.writeBytes("Department");
					dosREPORT.writeBytes("\t");
					dosREPORT.writeBytes("\n\n");
				}
				else
				{	
					dosREPORT.writeBytes(strDOTLN +"\n");	
					dosREPORT.writeBytes("Employee Name                           Emp No  Desgn     Department   ");
					dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
				}
			}
			cl_dat.M_intLINNO_pbst+=7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
		
/**
 * Method to generate the Report & send to the selected Destination.
 */
	public void exePRINT()
	{
		if(!vldDATA())
		{
			return;
		}
		try
		{
			intRECCT=0;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
			{	
				if(M_txtDESTN.getText().trim().length()>0)
				{	
					if(M_txtDESTN.getText().trim().charAt(M_txtDESTN.getText().trim().length()-1)==('\\'))
						strFILNM = M_txtDESTN.getText().trim() + "hr_rpepm.xls";
					else
						strFILNM = M_txtDESTN.getText().trim() + "\\hr_rpepm.xls";
				}
				else
					strFILNM = cl_dat.M_strREPSTR_pbst + "\\hr_rpepm.xls";
			}	
			else if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"hr_rpepm.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "hr_rpepm.doc";
			
			//System.out.println("strFILNM>>"+strFILNM);
			getDATA();
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}
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
			/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{					
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Employee Details"," ");				
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }*/
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
			{
				//if(M_txtDESTN.getText().length()>0)
				//	setMSG("File Copied to file:"+M_txtDESTN.getText().trim()+"",'N');
				//else
				//	setMSG("File Copied to file:c:\\reports\\hr_rpepm.doc",'N');
				 Runtime r = Runtime.getRuntime();
				 Process p = null;					
                 p  = r.exec("C:\\windows\\excel.exe " + strFILNM); 
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
/**
	 * Method to validate inputs given before execuation of SQL Query.
 */
	public boolean vldDATA()
	{
		if(chkSELCT.isSelected())
		{
			if(cmbPAGSZ.getSelectedItem().equals("Plain A4"))
			{
				if(strFNTSZ.equals("CPI10"))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
						intNOLIN = 51;
					else
						intNOLIN = 56;
					intNOCHR = 70;
				}
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 85;
				}
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 65;
					intNOCHR = 110;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 65;
					intNOCHR = 125;
				}
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 110;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 100;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 100;
				}
			}
			if(cmbPAGSZ.getSelectedItem().equals("Fanfold 210mm - 12in"))
			{
				if(strFNTSZ.equals("CPI10"))
				{
					intNOLIN = 56;
					intNOCHR = 85;
				}
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 110;
				}
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 70;
					intNOCHR = 130;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 70;
					intNOCHR = 150;
				}	
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 130;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 120;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 110;
				}
			}			
			if(cmbPAGSZ.getSelectedItem().equals("Fanfold 358mm - 12in"))
			{
				if(strFNTSZ.equals("CPI10"))
				{
					intNOLIN = 60;
					intNOCHR = 135;
				}	
				if(strFNTSZ.equals("CPI12"))
				{
					intNOLIN = 65;
					intNOCHR = 165;
				}				
				if(strFNTSZ.equals("CPI15"))
				{
					intNOLIN = 70;
					intNOCHR = 220;
				}
				if(strFNTSZ.equals("CPI17"))
				{
					intNOLIN = 70;
					intNOCHR = 230;
				}	
				if(strFNTSZ.equals("Size 8"))
				{
					intNOLIN = 85;
					intNOCHR = 200;
				}
				if(strFNTSZ.equals("Size 9"))
				{
					intNOLIN = 75;
					intNOCHR = 180;
				}
				if(strFNTSZ.equals("Size 10"))
				{
					intNOLIN = 70;
					intNOCHR = 160;
				}
			}
		}		
		else
		{
			intNOLIN = 56;
			intNOCHR = 110;
		}					
		intROWLN = 0;		
		if(chkSELCT.isSelected())
		{
			int L_intCOUNT = 0;
			strTHEAD="";		
			stbCOLLS = new  StringBuffer(); 				
			if(stbDOTLN != null)
				stbDOTLN.delete(0,stbDOTLN.length());		
			arrCOLWD= new int[30];				
			intROWLN = 55;
			for(int i=0; i< tblITMDT.getRowCount();i++)
			{			
				if(tblITMDT.getValueAt(i,TBL_SELCT).toString().equals("true"))
				{				
					String strNAME = tblITMDT.getValueAt(i,TBL_CNAME).toString();
					String strNAME1= tblITMDT.getValueAt(i,TBL_CTEXT).toString();
					String strSIZE = tblITMDT.getValueAt(i,TBL_CSIZE).toString();
					if(Integer.parseInt(strSIZE)<10)
						strSIZE = "10";								
					intROWLN += Integer.parseInt(strSIZE)+1;
					strTHEAD += padSTRING('R',strNAME1,Integer.parseInt(strSIZE)+1);								
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
						strTHEAD += "\t";
						
					if(L_intCOUNT !=0)
						stbCOLLS.append(",");								
					stbCOLLS.append(strNAME);				
					arrCOLWD[L_intCOUNT] = Integer.parseInt(strSIZE)+1;											
					L_intCOUNT++;					
				}
			}	
			if(intROWLN < 60)
				intROWLN=60;
			for(int i=0;i<intROWLN;i++)
				stbDOTLN.append("-");
			if(L_intCOUNT==0)
			{
				setMSG("No column is selected, Please select column/s..",'E');
				return false;
			}
			/**if(cmbPAGSZ.getSelectedItem().equals("Fanfold 358mm - 12in"))
			{
				if(intNOCHR <intROWLN)
				{
					setMSG("Report cannot be fit into "+cmbPAGSZ.getSelectedItem().toString() +" page Please Select less No. of Columns..",'E');
					return false;
				}
			}
			else
			{
				if(intNOCHR <intROWLN)
				{
					setMSG("Report cannot be fit into "+cmbPAGSZ.getSelectedItem().toString() +" page Please Select Larger Size Paper..",'E');
					return false;
				}
			}**/				
		}
		if(!chkSELCT.isSelected())
		{
			if(stbDOTLN != null)
				stbDOTLN.delete(0,stbDOTLN.length());	
			for(int i=0;i<98;i++)
				stbDOTLN.append("-");
		}
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount() == 0)
			{					
				setMSG("Please Select the Email/s from List through F1 Help ..",'N');
				return false;
			}
		}
		return true;
	}
	/**
	 * Method For Department Validation
	 */
	public boolean vldDEPT(String P_strDEPT)
	{
		if(P_strDEPT.length()!=3)
		{
			setMSG("Please Enter Proper Dept Code , Press f1 to select from list..",'E');
			return false;
		}
		else if(cmbHRCON.getSelectedIndex()==4)
		{	
			if((txtHRVAL.getText().trim()).compareTo(txtTOVAL.getText().trim())>0)
			{
				setMSG("From Value Can not be greater than tovalue..",'E');
				return false;
			}
			else
			{
				return true;
			}
		}		
		else
		{
			return true;
		}
	}
	/** Method for date Validation*/
	public boolean vldDATE(String P_strDATE)
	{
		try
		{	
			String L_strTEMP="";
			String L_strTEMP1="";
			String L_staTEMP[];
			String L_strDATE =P_strDATE;
			if(L_strDATE.length()!=10)
			{
				setMSG("Please Enter Proper Date Length(dd/mm/yyyy)",'E');
				return false;
			}
			else
			{
				Pattern pat=Pattern.compile("/");
				L_staTEMP=pat.split(L_strDATE);
				if(L_staTEMP.length!=3)
				{
					setMSG("Please Enter Proper Date format(dd/mm/yyyy)",'E');
					txtHRVAL.setText("");
					return false;
				}
				else if(L_staTEMP[0].length()!=2 ||L_staTEMP[1].length()!=2||L_staTEMP[2].length()!=4)
				{
					setMSG("Please Enter Proper Date ,Months & Year",'E');
					txtHRVAL.requestFocus();
					return false;
				}
				else 
				{
					int intDATE=Integer.parseInt(L_staTEMP[0]);
					int intMONTH=Integer.parseInt(L_staTEMP[1]);
					int intYEAR=Integer.parseInt(L_staTEMP[2]);
					if (intMONTH<0 || intMONTH>12)
					{
						setMSG("Please Enter Proper Month.. ",'E');
						txtHRVAL.requestFocus();
						return false;
					}
					if(intMONTH==1 ||intMONTH==3||intMONTH==5||intMONTH==7 || intMONTH==8 || intMONTH==10 || intMONTH==12)
				    {
						if(intDATE<0 || intDATE>31)
						{
							setMSG("Days for specified months can not be greater than 31..",'E');
							return false;
						}
					}
				   if(intMONTH==4 ||intMONTH==6 || intMONTH==9 || intMONTH==11)
				   {
						if(intDATE<0 || intDATE>30)
						{
							setMSG("Days for specified months can not be greater than 30..",'E');
							return false;
						}
					}
				    if(intMONTH==2)
				   {
						if(intYEAR%100 ==0)
						{
							if(intYEAR %400 == 0)
							{
								if(intDATE<0 || intDATE>29)
								{
									setMSG("Days for specified months can not be greater than 29..",'E');
									return false;
								}
							}
							else if(intYEAR %400 != 0)
							{
								if(intDATE<0 || intDATE>28)
								{
									setMSG("Days for specified months can not be greater than 28..",'E');
									return false;
								}
							}
						}
						else if(intYEAR%4==0)
						{
							if(intDATE<0 || intDATE>29)
							{
								setMSG("Days for specified months can not be greater than 29..",'E');
								return false;
							}
						}
						else 
						{
							if(intDATE<0 || intDATE>28)
							{
								setMSG("Days for specified months can not be greater than 28..",'E');
								return false;
							}
						}
					}	
					return true;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}	
	/**
	  * Supper Class method overrided to execuate the F1 Help for selected Component.
	*/
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtHRVAL")
		{
			txtHRVAL.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
}


class hr_rpepm_list extends TransferHandler {
    DataFlavor localArrayListFlavor, serialArrayListFlavor;
    String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +
                                ";class=java.util.ArrayList";
    JList source = null;
    int[] indices = null;
    int addIndex = -1; //Location where items were added
    int addCount = 0;  //Number of items added

    public hr_rpepm_list() {
        try {
            localArrayListFlavor = new DataFlavor(localArrayListType);
        } catch (ClassNotFoundException e) {
            System.out.println(
             "hr_rpepm_list: unable to create data flavor");
        }
        serialArrayListFlavor = new DataFlavor(ArrayList.class,
                                              "ArrayList");
    }

    public boolean importData(JComponent c, Transferable t) {
        JList target = null;
        ArrayList alist = null;
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }
        try {
            target = (JList)c;
            if (hasLocalArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList)t.getTransferData(localArrayListFlavor);
            } else if (hasSerialArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList)t.getTransferData(serialArrayListFlavor);
            } else {
                return false;
            }
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("importData: unsupported data flavor");
            return false;
        } catch (IOException ioe) {
            System.out.println("importData: I/O exception");
            return false;
        }

        // If you want the dialog to appear only for drags from the
        // left list and drops on the right JList.
        if(target.getName().equals("right") && target != source) {
            String message = "Do you want to drop here?";
            int retVal = JOptionPane.showConfirmDialog(target, message, "Confirm",
                                                       JOptionPane.YES_NO_OPTION,
                                                       JOptionPane.QUESTION_MESSAGE);
            //System.out.println("retVal = " + retVal);
            if(retVal == JOptionPane.NO_OPTION || retVal == JOptionPane.CLOSED_OPTION)
                return false;
        }

        //At this point we use the same code to retrieve the data
        //locally or serially.

        //We'll drop at the current selected index.
        int index = target.getSelectedIndex();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //This is interpreted as dropping the same data on itself
        //and has no effect.
        if (source.equals(target)) {
            if (indices != null && index >= indices[0] - 1 &&
                  index <= indices[indices.length - 1]) {
                indices = null;
                return true;
            }
        }

        DefaultListModel listModel = (DefaultListModel)target.getModel();
        int max = listModel.getSize();
        //System.out.printf("index = %d  max = %d%n", index, max);
        if (index < 0) {
            index = max; 
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        addCount = alist.size();
        for (int i=0; i < alist.size(); i++) {
            listModel.add(index++, alist.get(i));
        }
        return true;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
        if ((action == MOVE) && (indices != null)) {
            DefaultListModel model = (DefaultListModel)source.getModel();

            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex &&
                        indices[i] + addCount < model.getSize()) {
                        indices[i] += addCount;
                    }
                }
            }
            for (int i = indices.length -1; i >= 0; i--)
                model.remove(indices[i]);
        }
        indices = null;
        addIndex = -1;
        addCount = 0;
    }

    private boolean hasLocalArrayListFlavor(DataFlavor[] flavors) {
        if (localArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(localArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSerialArrayListFlavor(DataFlavor[] flavors) {
        if (serialArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(serialArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (hasLocalArrayListFlavor(flavors))  { return true; }
        if (hasSerialArrayListFlavor(flavors)) { return true; }
        return false;
    }

    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JList) {
            source = (JList)c;
            indices = source.getSelectedIndices();
            Object[] values = source.getSelectedValues();
            if (values == null || values.length == 0) {
                return null;
            }
            ArrayList<String> alist = new ArrayList<String>(values.length);
            for (int i = 0; i < values.length; i++) {
                Object o = values[i];
                String str = o.toString();
                if (str == null) str = "";
                alist.add(str);
            }
            return new ReportingListTransferable(alist);
        }
        return null;
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public class ReportingListTransferable implements Transferable {
        ArrayList data;

        public ReportingListTransferable(ArrayList alist) {
            data = alist;
        }

        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { localArrayListFlavor,
                                      serialArrayListFlavor };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (localArrayListFlavor.equals(flavor)) {
                return true;
            }
            if (serialArrayListFlavor.equals(flavor)) {
                return true;
            }
            return false;
        }
    }
}