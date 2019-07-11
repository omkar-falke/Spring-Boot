//24/04/2006

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import javax.swing.JPanel;import javax.swing.JButton;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable;
import javax.swing.JComponent;import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;
import java.sql.ResultSet;

/**
<P><PRE style = font-size : 10 pt >
<b>System Name :</b> Sytem Admin.
 
<b>Program Name :</b> Pending Job Stock transfer Nodes.

<b>Purpose :</b> Program to generate Stock Transfer Note.Here we canbe able add new Records, Modify existing Records and to Delete unwanted records.
			
List of tables used :
List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_STNTR       SA_MMSBS,SA_STRTP,SA_STNNO,SA_MATCD    #      #         #       #
MM_STPRC       ST_MMSBS,ST_STRTP,ST_MATCD                              #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
MM_RMMST       RM_TRNTP,RM_DOCNO                      #		 #		   #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                 #         #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		 Type/Size       Description
----------------------------------------------------------------------------------------------------------
txtSTNTP    STN_STNTP       MM_STNTR         varchar(2)      STN Type
txtSTNNO    STN_STNNO       MM_STNTR         varchar(8)      Stn Number 
txtWWORNO   STN_WORNO       MM_STNTR         varchar(15)     Work Order Number
txtPREBY    STN_PREBY       MM_STNTR         varchar(3)      Prepared By
txtRECBY    STN_RECBY       MM_STNTR         varchar(3)      Recommand By
txtAPRBY    STN_APRBY       MM_STNTR         varchar(3)      Approved By
txtREMDS    BL_REMDS        MM_RMMST         VARCHAR(200)    Remark
cmbSTORE CMT_CODCD,CMT_CODDS CO_CDTRN                        Code and Description
----------------------------------------------------------------------------------------------------------

For Stores Type:</b>
     Table used  : 1)CO_CDTRN                             
     Condition   : 1) CMT_CGMTP = 'SYS' 
                   2) AND CMT_CGSTP = 'MMXXSST' 
                  
<B>For STN Type:</B>
     Table used  : 1)CO_CDTRN   
Conditation      : 1) CMT_CGMTP='SYS' 
				 : 2)AND CMT_CGSTP = 'MMXXSTN' 
                 : 3)AND isnull(CMT_STSFL,'') <> 'X'";

<B>For STN Number:</B>
     Table used  : 1)MM_STNTR                             
     Condition   : 1) MM_STNNO = Selected STN Number
				   2) STN_FRSTR= M_strSBSCD.substring(2,4) (Sub-System Code)
                   3) And isnull(MM_STSFL,'') <> 'X'";


<b>For STN details:</b>(Table Displayed on Screen)
     Tables used : 1)MM_STNTR
                   2)MM_STPRC
                   3)CO_CTMST                        
     If STN Type is '02' transfer From Work Order to Stores then 
	     Condition : 1) STN_FRSTR = STP_STRTP
	                   2) AND STN_FRMAT = CT_MATCD 
		               3) AND STN_TOMAT=CT_MATCD
			           4) AND STN_FRSTR =M_strSBSCD.substring(2,4) (sub System Code)
				       5) AND STN_STNNO = Given SNT Number
				       6) AND isnull(STN_STSFL,'') <> 'X'

	If STN Type is '01' or '03' then 
		 Condition : 1) STN_FRSTR = STP_STRTP
			           2) AND STN_FRMAT = STP_MATCD 
				       3) AND STN_TOMAT=CT_MATCD
					   4) AND STN_FRSTR =M_strSBSCD.substring(2,4) (sub System Code)
					   5) AND STN_STNNO = Given SNT Number
					   6) AND isnull(STN_STSFL,'') <> 'X'

for Deletion:
    - STN number must be valid.
for Enquiry:
    - STN number must be valid.
    - Report Printing for given STN number details is allowed.
for Authorization
    - STN Number must be Valid
    - AND STN_STSFL not in ('2','X')

Validations :
	* 1)Transfer Quantity Should be present in Stocks
	* 2)SNT type should be valid
	* 3)Item Code should be Valid
*/

public class mm_testn extends cl_pbase
{									/** JTextField for STN Number*/
	private JTextField txtSTNNO;	/**JTextField for STN Date	 */
	private JTextField txtSTNDT;    /**JTextfield for remark	                                 */
	private JTextField txtREMDS;    /**JTextField for Mapping with From Item code of table   */
	private JTextField txtFRMAT;    /**JTextField for Mapping with To Item code of table    */
	private JTextField txtTOMAT;    /**JTextField for STN Type   */
	private JTextField txtSTNTP;    /**JTextField for work Order Number  */
	private JTextField txtWORNO;    /**JTextField for Prepared By     */
	private JTextField txtPREBY;    /**JTextField for Recommended By  */
	private JTextField txtRECBY;    /**JTextField for Approved By */
	private JTextField txtAPRBY;    /**JButton for Print the report	                                 */
	private JButton btnPRNT;        
	private cl_JTable tblSTNDT;     
	private JCheckBox chkCHKFL ;           /** HashTable for storing the Transfer Quantity  */
	private Hashtable<String,String>  hstITMDT = new Hashtable<String,String>(); /**	JCombo for Selecting the Store Type */  
	private JComboBox cmbSTORE;			/** Object of table Input Verifier*/
	private mm_teSTNTBLINVFR objTBLVRF; 
	private final int TBL_CHKFL =0;
	private final int TBL_FRMAT =1;
	private final int TBL_FRLOC =2;
	private final int TBL_MATDS =3;
	private final int TBL_UOMCD =4;
	private final int TBL_TRNQT =5;
	private final int TBL_TRNRT =6;
	private final int TBL_TOMAT =7;
	private final int TBL_TOLOC =8;
	private final int TBL_MATDS2=9;
	private final int TBL_TOUOM =10;
	private final String strDOCTP_fn ="80";	
	private String strSTNNO,strREMDS,strSTNDT,strTEMP;
	final String strTRNTP_fn = "ST";
	private int intRWCNT;
	boolean flgREMRK;
	private String strRGULR="01";
	private String strFRWOR="02";
	private String strTOWOR="03";
	mm_testn()
	{
		super(2);
		try
		{
			hstITMDT.clear();
			setMatrix(20,8);
			add(btnPRNT = new JButton("Print"),1,8,1,1,this,'L');
			
			add(new JLabel("STN Type."),2,1,1,1,this,'L');
			add(txtSTNTP = new TxtLimit(8),2,2,1,1,this,'L');
			
			add(new JLabel("STN No."),2,3,1,1,this,'L');
			add(txtSTNNO = new TxtLimit(8),2,4,1,1,this,'L');
			add(new JLabel("STN Date"),2,5,1,1,this,'L');
			add(txtSTNDT = new TxtDate(),2,6,1,1,this,'L');
			add(new JLabel("To Store"),3,1,1,1,this,'L');
			cmbSTORE = new JComboBox();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
				+ " AND CMT_CGSTP = 'MMXXSST'  AND isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET!=null)
			{
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP  =  nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"  ");
					L_strTEMP +=  "   ";
					L_strTEMP +=  nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbSTORE.addItem(L_strTEMP);	
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			add(cmbSTORE,3,2,1,1.5,this,'L');
			add(new JLabel("Work Order."),3,5,1,1,this,'L');
			add(txtWORNO = new TxtLimit(15),3,6,1,1,this,'L');
			add(new JLabel("Prepared By"),4,1,1,1,this,'L');
			add(txtPREBY = new TxtLimit(3),4,2,1,1,this,'L');
			add(new JLabel("Recomm. By"),4,3,1,1,this,'L');
			add(txtRECBY = new TxtLimit(3),4,4,1,1,this,'L');
			add(new JLabel("Approved By"),4,5,1,1,this,'L');
			add(txtAPRBY = new TxtLimit(3),4,6,1,1,this,'L');
			
			String[] L_strTBLHD = {" ","From Item Code","From Loc","Description","UOM ","Trns.Qty","Trns.Rate","TO Item Code","To Loc","Desc","UOM"};
			int[] L_intCOLSZ = {15,80,80,200,60,70,70,80,80,200,60};
			tblSTNDT = crtTBLPNL1(this,L_strTBLHD,100,6,1,10,7.9,L_intCOLSZ,new int[]{0});
			add(new JLabel("Remarks"),17,1,1,1,this,'L');
			add(txtREMDS=new TxtLimit(200),17,2,1,7,this,'L');
			txtFRMAT = new TxtLimit(10);
			txtTOMAT = new TxtLimit(10);
			txtFRMAT.addFocusListener(this);
			txtFRMAT.addKeyListener(this);
			txtTOMAT.addFocusListener(this);
			txtTOMAT.addKeyListener(this);
			tblSTNDT.setCellEditor(TBL_FRMAT,txtFRMAT);
			tblSTNDT.setCellEditor(TBL_TOMAT,txtTOMAT);
			tblSTNDT.setCellEditor(TBL_CHKFL,chkCHKFL = new JCheckBox());
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_teSTNTBLINVFR();
			tblSTNDT.setInputVerifier(objTBLVRF);	
			setENBL(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC==txtFRMAT) 
			{
				setMSG("Press F1 to select Item Code & then press Enter.",'N');
				if(tblSTNDT.getSelectedRow()<intRWCNT)
					((JTextField)tblSTNDT.cmpEDITR[TBL_FRMAT]).setEditable(false);
				else
					((JTextField)tblSTNDT.cmpEDITR[TBL_FRMAT]).setEditable(true);
			}
			if(M_objSOURC==txtTOMAT) 
			{
				setMSG("Press F1 to select Item Code & then press Enter.",'N');
				if(tblSTNDT.getSelectedRow()<intRWCNT)
					((JTextField)tblSTNDT.cmpEDITR[TBL_TOMAT]).setEditable(false);
				else
					((JTextField)tblSTNDT.cmpEDITR[TBL_TOMAT]).setEditable(true);
			}
		}
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == btnPRNT)
		{
			try
			{
				strSTNNO = txtSTNNO.getText().trim();
				if(strSTNNO.length() !=8)
				{
					setMSG("STN number is not Valid..",'E');
					return;
				}
				if(strSTNNO.length() ==8)
				{
					mm_rpstn objSTNRP  = new mm_rpstn(M_strSBSCD);
					objSTNRP.getDATA(strSTNNO,strSTNNO,1);
					JComboBox L_cmbLOCAL = objSTNRP.getPRNLS();
					objSTNRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpstn.doc",L_cmbLOCAL.getSelectedIndex());
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"btnPRINT");
			}
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();	
			txtPREBY.setText(cl_dat.M_strUSRCD_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
				setENBL(false);
			else
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					txtSTNNO.setEnabled(true);
				    txtRECBY.setEnabled(false);
					txtAPRBY.setEnabled(false);
					cmbSTORE.setEnabled(false);
					txtSTNNO.requestFocus();
				}
				else 
				{
					txtSTNDT.setText(cl_dat.M_strLOGDT_pbst);
					txtSTNTP.setEnabled(true);	
					txtWORNO.setEnabled(true);	
					txtRECBY.setEnabled(true);
					txtAPRBY.setEnabled(true);
					txtSTNNO.setEnabled(false);
					txtSTNTP.requestFocus();
				}
			}
		}
		if(M_objSOURC == txtSTNNO)
		{
			String L_strSTNNO = txtSTNNO.getText().trim();
			if(txtSTNNO.getText().trim().length()==0)
			{
				setMSG("STN No. can't be Zero Press f1 for help ",'N');
				txtSTNNO.requestFocus();
				return;
			}
			else if(txtSTNNO.getText().trim().length()!=8)
			{
				setMSG("Invalid STN No, Press f1 for help ",'N');
				txtSTNNO.requestFocus();
				return;
			}
			else
				getDATA(M_strSBSCD.substring(2,4),L_strSTNNO);			
		}	
	}
	/**
	 * Super Class metdhod overrided to inhance its funcationality, to enable disable 
	 * components according to requriement.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		btnPRNT.setEnabled(false);
		txtSTNNO.setEnabled(false);
		txtSTNDT.setEnabled(false);
		txtSTNTP.setEnabled(false);		
		txtWORNO.setEnabled(false);
		txtPREBY.setEnabled(false);	
		tblSTNDT.cmpEDITR[TBL_MATDS].setEnabled(false);
		tblSTNDT.cmpEDITR[TBL_FRLOC].setEnabled(false);
		tblSTNDT.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblSTNDT.cmpEDITR[TBL_TRNRT].setEnabled(false);
		tblSTNDT.cmpEDITR[TBL_MATDS2].setEnabled(false);
		tblSTNDT.cmpEDITR[TBL_TOLOC].setEnabled(false);
		tblSTNDT.cmpEDITR[TBL_TOUOM].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			btnPRNT.setEnabled(true);
			txtSTNNO.setEnabled(true);
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
		{
			tblSTNDT.setEnabled(false);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int[] L_inaCOLSZ = new int[]{100,200,100,100};
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC==txtSTNNO)
			{
				M_strHLPFLD="txtSTNNO";
				String L_ARRHDR[]={"STN NO.","STN DATE"};
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{	
					M_strSQLQRY = "Select distinct STN_STNNO ,STN_STNDT from MM_STNTR where STN_FRSTR='" + M_strSBSCD.substring(2,4) +"' AND STN_STSFL='0' and  isnull(STN_STSFL,'') <> 'X'";
				}
				else
				{
					M_strSQLQRY = "Select distinct STN_STNNO ,STN_STNDT from MM_STNTR where STN_FRSTR='" + M_strSBSCD.substring(2,4) +"'  AND STN_STSFL <>'X'  and  isnull(STN_STSFL,'') <> 'X'";
				}
					if(txtSTNNO.getText().trim().length() >0)
						M_strSQLQRY += " and STN_STNNO like '" + txtSTNNO.getText().trim() + "%' order by STN_STNNO desc ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"STN No.","STN Date"},2,"CT");
			}
			if(M_objSOURC==txtSTNTP)
			{
				M_strHLPFLD="txtSTNTP";
				M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and  CMT_CGSTP = 'MMXXSTN'  and  isnull(CMT_STSFL,'') <> 'X'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"STN Type.","Description"},2,"CT");
			}
			if(M_objSOURC ==txtFRMAT)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtFRMAT";
				String L_ARRHDR[] = {"Item Code","Description","UOM","Trns. Rate"};
				if(txtSTNTP.getText().equals(strFRWOR))
				{
					M_strSQLQRY="Select CT_MATCD,CT_MATDS,CT_UOMCD,CT_PPORT from CO_CTMST";
					M_strSQLQRY += " Where CT_CODTP='CD'";
					if(txtFRMAT.getText().trim().length() >0)
						M_strSQLQRY += " AND CT_MATCD like '"+txtFRMAT.getText().trim() +"%'";
					M_strSQLQRY += " and  isnull(CT_STSFL,'') <> 'X' Order by CT_MATCD ";
				}
				else
				{
					M_strSQLQRY="Select STP_MATCD,STP_MATDS,STP_UOMCD,STP_YCLRT from MM_STPRC where STP_STRTP='"+M_strSBSCD.substring(2,4) +"' and  isnull(STP_YCSQT,0)>0";
					if(txtFRMAT.getText().trim().length() >0)
						M_strSQLQRY += " and STP_MATCD like '"+txtFRMAT.getText().trim() +"%'";
					M_strSQLQRY += "  and  isnull(STP_STSFL,'') <> 'X' Order by STP_MATCD ";
				}
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,4,"CT",L_inaCOLSZ);
			}
			if(M_objSOURC ==txtTOMAT)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtTOMAT";
				String L_ARRHDR1[] = {"Item Code","Description","UOM"};
				if(txtSTNTP.getText().equals(strTOWOR))
				{
					M_strSQLQRY="Select STP_MATCD,STP_MATDS,STP_UOMCD from MM_STPRC where STP_STRTP='"+M_strSBSCD.substring(2,4) +"'";
					if(txtTOMAT.getText().trim().length() >0)
						M_strSQLQRY += " and STP_MATCD like '"+txtTOMAT.getText().trim() +"%'";
					M_strSQLQRY += " and  isnull(STP_STSFL,'') <> 'X' Order by STP_MATCD ";
				}
				else
				{
					M_strSQLQRY="Select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST";
					M_strSQLQRY += " Where CT_CODTP='CD'";
					if(txtTOMAT.getText().trim().length() >0)
						M_strSQLQRY += " AND CT_MATCD like '"+txtTOMAT.getText().trim() +"%'";
					M_strSQLQRY += " and  isnull(CT_STSFL,'') <> 'X'  Order by CT_MATCD ";
				}
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR1,3,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC == txtSTNNO)
				{					
					if(txtSTNNO.getText().trim().length()==0)
					{
						setMSG("STN No. can't be Zero Press f1 for help ",'N');
						txtSTNNO.requestFocus();
						return;
					}
					if(txtSTNNO.getText().trim().length()!=8)
					{
						setMSG("Please Enter Correct STN No. or Press f1 for help ",'N');
						txtSTNNO.requestFocus();
					}
				}
				if(M_objSOURC==txtSTNTP)
				{
					if((txtSTNTP.getText().equals(strRGULR))||(txtSTNTP.getText().equals(strFRWOR))||(txtSTNTP.getText().equals(strTOWOR)))
					{
						if(txtSTNTP.getText().equals(strRGULR))
						{
							txtWORNO.setEnabled(false);
							cmbSTORE.setEnabled(true);
							cmbSTORE.requestFocus();
						}
						else
						{
							cmbSTORE.setEnabled(false);
							for(int j=0;j<cmbSTORE.getItemCount();j++)
							{
								if(M_strSBSCD.substring(2,4).equals((cmbSTORE.getItemAt(j).toString()).substring(0,2)))
								{
									cmbSTORE.setSelectedIndex(j);
								}
							}
							txtWORNO.setEnabled(true);
							txtWORNO.requestFocus();
						}
					}
					else
					{
						setMSG("Invalid STN Type Press F1 for Help ",'E');
						txtSTNTP.requestFocus();
					}
				}
				if(M_objSOURC==txtWORNO)
				{
					intRWCNT=0;
					if(txtWORNO.getText().trim().length()==0)
					{
						setMSG("Please Enter Work Order Number",'E');
						txtWORNO.requestFocus();
					}
					else
						txtRECBY.requestFocus();
				}
				if(M_objSOURC==cmbSTORE)
				{
					if(txtSTNTP.getText().equals(strRGULR))
						txtRECBY.requestFocus();
					else
						txtWORNO.requestFocus();
				}
				if(M_objSOURC==txtRECBY)
				{
					txtAPRBY.requestFocus();
				}
				if(M_objSOURC==txtAPRBY)
				{
					tblSTNDT.setRowSelectionInterval(tblSTNDT.getSelectedRow(),tblSTNDT.getSelectedRow());		
					tblSTNDT.setColumnSelectionInterval(TBL_FRMAT,TBL_FRMAT);		
					tblSTNDT.editCellAt(tblSTNDT.getSelectedRow(),TBL_FRMAT);
					tblSTNDT.cmpEDITR[TBL_FRMAT].requestFocus();
				}
			}
			catch(Exception L_EK)
			{
				setMSG(L_EK,"Key Evelnt");
			}
		}
	}
	/**
	 * Method to execute F1 help for the selected field.
	*/
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtSTNNO"))
			{
				txtSTNNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtSTNTP"))
			{
				txtSTNTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtFRMAT"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				String L_strMATCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim();
				txtFRMAT.setText(L_strMATCD);
				M_strSQLQRY="Select ST_LOCCD from MM_STMST where ST_MATCD= '"+L_strMATCD+ "' And ST_STRTP='" + M_strSBSCD.substring(2,4) +"'and  isnull(ST_STSFL,'') <> 'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						tblSTNDT.setValueAt(M_rstRSSET.getString("ST_LOCCD"),tblSTNDT.getSelectedRow(),TBL_FRLOC);
					}
					M_rstRSSET.close();
				}
				if(tblSTNDT.isEditing())
					tblSTNDT.getCellEditor().stopCellEditing();
				tblSTNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblSTNDT.getSelectedRow(),TBL_MATDS);
				tblSTNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblSTNDT.getSelectedRow(),TBL_UOMCD);
				tblSTNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim(),tblSTNDT.getSelectedRow(),TBL_TRNRT);
				tblSTNDT.cmpEDITR[TBL_TRNQT].requestFocus();
				tblSTNDT.cmpEDITR[TBL_TRNQT].requestFocus();
				if(objTBLVRF.verify(tblSTNDT.getSelectedRow(),TBL_FRMAT))
				{
					tblSTNDT.editCellAt(tblSTNDT.getSelectedRow(),TBL_FRMAT);
					tblSTNDT.cmpEDITR[TBL_TRNQT].requestFocus();
				}
			}
			if(M_strHLPFLD.equals("txtTOMAT"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				String L_strMATCD=  String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim();
				txtTOMAT.setText(L_strMATCD);	
				M_strSQLQRY="Select ST_LOCCD from MM_STMST where ST_MATCD= '"+L_strMATCD+ "' And ST_STRTP='" + M_strSBSCD.substring(2,4) +"' and  isnull(ST_STSFL,'') <> 'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						tblSTNDT.setValueAt(M_rstRSSET.getString("ST_LOCCD"),tblSTNDT.getSelectedRow(),TBL_TOLOC);
					}
					if(M_rstRSSET !=null)
						M_rstRSSET.close();
				}
				tblSTNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblSTNDT.getSelectedRow(),TBL_MATDS2);
				tblSTNDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblSTNDT.getSelectedRow(),TBL_TOUOM);
				tblSTNDT.cmpEDITR[TBL_FRMAT].requestFocus();
				txtFRMAT.requestFocus();
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**
	 * Method to fetch data from the database for Specified STN Number.
	*/
	private boolean getDATA(String P_strSTRTP,String P_strSTNNO)
	{
		boolean L_FIRST = true;
		java.sql.Date L_datTMPDT;
		String L_strTOSTR="";
		String L_strDPTCD="";
		String L_strSTNTP="";
		String L_strSQLQRY="";
		ResultSet L_rstRSSET;
		intRWCNT=0;
		int i=0;
		clrCOMP();		
		try
		{
			L_strSQLQRY = "Select STN_STNTP from MM_STNTR WHERE STN_FRSTR = '" + M_strSBSCD.substring(2,4) + "' and STN_STNNO = '" + P_strSTNNO + "' and  isnull(STN_STSFL,'') <> 'X'";
			L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);	
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					L_strSTNTP=L_rstRSSET.getString("STN_STNTP");
				}
				if(L_rstRSSET!=null)
					L_rstRSSET.close();
			}
			if(L_strSTNTP.equals(strFRWOR))
			{
				M_strSQLQRY = "Select STN_MMSBS,STN_FRSTR,STN_TOSTR,STN_STNNO,STN_STNDT,STN_STNTP,STN_WORNO,STN_FRMAT,STN_TOMAT,STN_TRNQT,STN_STNRT,STN_FRLOC,STN_TOLOC,STN_PREBY,STN_RECBY,STN_APRBY,(CT_MATDS)STN_FRDES,(CT_UOMCD)STN_UOMCD,CT_MATDS,CT_UOMCD ";
				M_strSQLQRY +=" from MM_STNTR,CO_CTMST";
				M_strSQLQRY += " where STN_FRMAT = CT_MATCD AND STN_TOMAT=CT_MATCD AND STN_FRSTR = '" + M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += " and STN_STNNO = '" + P_strSTNNO + "' and  isnull(STN_STSFL,'') <> 'X'";
			}
			else
			{
				M_strSQLQRY = "Select STN_MMSBS,STN_FRSTR,STN_TOSTR,STN_STNNO,STN_STNDT,STN_STNTP,STN_WORNO,STN_FRMAT,STN_TOMAT,STN_TRNQT,STN_STNRT,STN_FRLOC,STN_TOLOC,STN_PREBY,STN_RECBY,STN_APRBY,(STP_MATDS)STN_FRDES,(STP_UOMCD)STN_UOMCD,CT_MATDS,CT_UOMCD ";
				M_strSQLQRY +=" from MM_STNTR,MM_STPRC,CO_CTMST";
				M_strSQLQRY += " where STN_FRSTR = STP_STRTP AND STN_FRMAT = STP_MATCD AND STN_TOMAT=CT_MATCD AND STN_FRSTR = '" + M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += " and STN_STNNO = '" + P_strSTNNO + "'  and  isnull(STN_STSFL,'') <> 'X'";
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
			{
				M_strSQLQRY +="	and STN_STSFL = '0'";
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			{
				M_strSQLQRY +="	and STN_STSFL <> 'X'";
			}
            else
				M_strSQLQRY +="	and STN_STSFL not in('2','X')";
			M_strSQLQRY += " order by STN_FRMAT";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			
			if(tblSTNDT.isEditing())
			tblSTNDT.getCellEditor().stopCellEditing();
			tblSTNDT.setRowSelectionInterval(0,0);
			tblSTNDT.setColumnSelectionInterval(0,0);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					if(L_FIRST)
					{
						txtSTNNO.setText(P_strSTNNO);
						L_datTMPDT = M_rstRSSET.getDate("STN_STNDT");
						if(L_datTMPDT !=null)
						{
							txtSTNDT.setText(M_fmtLCDAT.format(L_datTMPDT));
							strSTNDT = txtSTNDT.getText().trim();
						}
						else
							txtSTNDT.setText("");
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				    {
						tblSTNDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
					}
					txtSTNTP.setText(nvlSTRVL(M_rstRSSET.getString("STN_STNTP"),""));
					txtWORNO.setText(nvlSTRVL(M_rstRSSET.getString("STN_WORNO"),""));
					txtPREBY.setText(nvlSTRVL(M_rstRSSET.getString("STN_PREBY"),""));
					txtRECBY.setText(nvlSTRVL(M_rstRSSET.getString("STN_RECBY"),""));
					txtAPRBY.setText(nvlSTRVL(M_rstRSSET.getString("STN_APRBY"),""));
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_FRMAT"),i,TBL_FRMAT);
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_FRLOC"),i,TBL_FRLOC);
					L_strTOSTR=M_rstRSSET.getString("STN_TOSTR");
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_TOMAT"),i,TBL_TOMAT);
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_TRNQT"),i,TBL_TRNQT);
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_STNRT"),i,TBL_TRNRT);
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_FRDES"),i,TBL_MATDS);
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_UOMCD"),i,TBL_UOMCD);
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_TOLOC"),i,TBL_TOLOC);
					tblSTNDT.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,TBL_MATDS2);
					tblSTNDT.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,TBL_TOUOM);
					i++;
					intRWCNT++;
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			for(int j=0;j<cmbSTORE.getItemCount();j++)
			{
				if(L_strTOSTR.equals((cmbSTORE.getItemAt(j).toString()).substring(0,2)))
				{
					cmbSTORE.setSelectedIndex(j);
				}
			}
			M_strSQLQRY = "Select RM_REMDS,RM_STSFL from MM_RMMST ";
			M_strSQLQRY += " where RM_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
			M_strSQLQRY += " and RM_DOCNO = '" + txtSTNNO.getText().trim() + "' and  isnull(RM_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(!nvlSTRVL(M_rstRSSET.getString("RM_STSFL"),"").equals("X"))
						txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
					else
						txtREMDS.setText("test");
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception E)
		{
			setMSG(E ,"GetData");
		}
		return !L_FIRST;
	}
	
	/**
	 * Method to update the MM_STNTR AND Remarks if 
	 * new record inserted  then insert new record or updated previous.	
	*/
	private void exeMODSTN()
	{
		try
		{
			String L_strTOMAT="";
			String L_strFRMAT="";
			String L_strTOSTR=(String)cmbSTORE.getSelectedItem();
			L_strTOSTR=L_strTOSTR.substring(0,2);
			for(int i=0;i<tblSTNDT.getRowCount();i++)
			{
				if(tblSTNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_strFRMAT = nvlSTRVL(tblSTNDT.getValueAt(i,TBL_FRMAT).toString(),"");
					L_strTOMAT = nvlSTRVL(tblSTNDT.getValueAt(i,TBL_TOMAT).toString(),"");
					String L_strSTNNO=txtSTNNO.getText().trim();
					if(i<intRWCNT)
					{
						M_strSQLQRY="UPDATE MM_STNTR SET STN_TRNQT= "+tblSTNDT.getValueAt(i,TBL_TRNQT).toString() +" , ";
						M_strSQLQRY +="STN_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
						M_strSQLQRY +="STN_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
						M_strSQLQRY += " WHERE STN_MMSBS = '"+ M_strSBSCD+ "'";
						M_strSQLQRY +=" AND STN_FRSTR='"+M_strSBSCD.substring(2,4)+"'";
						M_strSQLQRY += " AND STN_STNNO = '" + L_strSTNNO + "'";
						M_strSQLQRY += " AND STN_FRMAT = '" + L_strFRMAT + "'";
						M_strSQLQRY += " AND STN_TOMAT = '" + L_strTOMAT + "'";
					}
					else
					{
						M_strSQLQRY="Insert into MM_STNTR(STN_MMSBS,STN_FRSTR,STN_TOSTR,STN_STNNO,STN_STNDT,STN_FRMAT,STN_TOMAT,STN_TRNQT,STN_STNRT,STN_FRLOC,STN_TOLOC,STN_STNTP,STN_WORNO,STN_PREBY,STN_RECBY,STN_APRBY,STN_STSFL,STN_TRNFL,STN_LUSBY,STN_LUPDT ) values ( ";
						M_strSQLQRY+="'"+M_strSBSCD+"',";
						M_strSQLQRY+="'"+M_strSBSCD.substring(2,4) +"',";
						M_strSQLQRY+="'"+L_strTOSTR +"',";
						M_strSQLQRY+="'"+txtSTNNO.getText().trim() +"',";
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTNDT))+ "',";
						M_strSQLQRY+="'"+tblSTNDT.getValueAt(i,TBL_FRMAT).toString() +"',";
						M_strSQLQRY+="'"+tblSTNDT.getValueAt(i,TBL_TOMAT).toString() +"',";
						M_strSQLQRY+= tblSTNDT.getValueAt(i,TBL_TRNQT).toString() +",";
						M_strSQLQRY+= tblSTNDT.getValueAt(i,TBL_TRNRT).toString() +",";
						M_strSQLQRY +="'"+tblSTNDT.getValueAt(i,TBL_FRLOC).toString() +"',";
						M_strSQLQRY +="'"+tblSTNDT.getValueAt(i,TBL_TOLOC).toString() +"',";
						M_strSQLQRY+="'"+txtSTNTP.getText().trim() +"',";
						M_strSQLQRY+="'"+txtWORNO.getText().trim() +"',";
						M_strSQLQRY+="'"+txtPREBY.getText().trim() +"',";
						M_strSQLQRY+="'"+txtRECBY.getText().trim() +"',";
						M_strSQLQRY+="'"+txtAPRBY.getText().trim() +"',";
						M_strSQLQRY += getUSGDTL("ST",'I',"0")+")";
					}
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			flgREMRK=chkREMRK();
			if(flgREMRK)
			{
				M_strSQLQRY="UPDATE  MM_RMMST  SET RM_REMDS= '"+txtREMDS.getText().trim()+"' , ";
				M_strSQLQRY += getUSGDTL("RM",'U',"");
				M_strSQLQRY += "where RM_STRTP = '"+ M_strSBSCD.substring(2,4) + "'";
				M_strSQLQRY += " AND RM_TRNTP = '" + strTRNTP_fn + "' ";
				M_strSQLQRY += " AND RM_DOCTP = '" + strTRNTP_fn + "' ";
				M_strSQLQRY += " AND RM_DOCNO = '" + txtSTNNO.getText().trim() + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			else
			{
				strREMDS=txtREMDS.getText().trim();
				if(strREMDS.length()>0)
				{
					M_strSQLQRY="Insert into MM_RMMST (RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS,RM_MMSBS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT )";
					M_strSQLQRY +=" Values (";
					M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
					M_strSQLQRY += "'" + strTRNTP_fn + "',";
					M_strSQLQRY += "'" + strTRNTP_fn + "',";
					M_strSQLQRY += "'" + txtSTNNO.getText().trim() + "',";
					M_strSQLQRY += "'" + strREMDS + "',";
					M_strSQLQRY += "'" + M_strSBSCD +"',";
					M_strSQLQRY += getUSGDTL("RM",'I',"0")+")";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeMOD"))
				{
					setMSG("Data Update successfully",'N');
				}
			}
			else
			{
				setMSG("Error in Updating",'E');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeMODSTN ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**
	 * Method to perform  check of the Remark In the Data Base, Before updating 
	 *new data in the data base.
	*/
	
	private boolean chkREMRK()
	{
		ResultSet L_rstRSSET;
		String L_strSQLQRY="";
		flgREMRK=false;
		try
		{
			L_strSQLQRY="SELECT RM_REMDS from MM_RMMST ";
			L_strSQLQRY += "where RM_STRTP = '"+ M_strSBSCD.substring(2,4) + "'";
			L_strSQLQRY += " AND RM_TRNTP = '" + strTRNTP_fn + "' ";
			L_strSQLQRY += " AND RM_DOCTP = '" + strTRNTP_fn + "' ";
			L_strSQLQRY += " AND RM_DOCNO = '" + txtSTNNO.getText().trim() + "' and  isnull(RM_STSFL,'') <> 'X'";
			L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY );
			if(L_rstRSSET!=null)
			{
				if(L_rstRSSET.next())
				{
					flgREMRK= true;
				}
				else
					flgREMRK=false;
			}
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"chkREMRK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return flgREMRK;
	}
	
	/**
	 * Method to perform validy check of the Data entered, Before inserting 
	 *new data in the data base.
	*/
	
	boolean vldDATA()
	{
		if(txtSTNDT.getText().trim().length()==0)
    	{
    		setMSG("STN Date can not be blank..",'E');
    		return false;
    	}
		if(txtRECBY.getText().trim().length() ==0)
    	{
    		setMSG("Enter the initial of Recommending authority..",'E');
    		return false;
    	}
    	if(txtAPRBY.getText().trim().length() ==0)
    	{
    		setMSG("Approved by can not be blank..",'E');
    		return false;
    	}
		for(int i=0;i<tblSTNDT.getRowCount();i++)
    	{
			if(tblSTNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
    		{
				strTEMP = nvlSTRVL(tblSTNDT.getValueAt(i,TBL_FRMAT).toString(),"");
    			if(strTEMP.length() == 0)
    			{
    				setMSG("item Code can not be blank..",'E');
					return false;
    			}
    			strTEMP = tblSTNDT.getValueAt(i,TBL_TRNQT).toString();
    			if(strTEMP.length() == 0)
    			{
    				setMSG("STN Qty. can not be blank..",'E');
    				return false;
    			}
    			if(Double.parseDouble(tblSTNDT.getValueAt(i,TBL_TRNQT).toString())==0)
    			{
    				setMSG("Qty. can not be zero..",'E');
    				return false;
    			}
    			strTEMP = tblSTNDT.getValueAt(i,TBL_TRNRT).toString();
    			if(strTEMP.length() == 0)
    			{
    				setMSG("STN Rate can not be blank..",'E');
    				return false;
    			}
    			if(Double.parseDouble(strTEMP)==0)
    			{
    				setMSG("Rate can not be zero..",'E');
    				return false;
    			}
				strTEMP = nvlSTRVL(tblSTNDT.getValueAt(i,TBL_TOMAT).toString(),"");
				if(strTEMP.length()==0)
				{
					setMSG("Item Code Can't be Zero",'E');
					return false;
				}    	
			}
			if(tblSTNDT.isEditing())
    		{
    			if(tblSTNDT.getValueAt(tblSTNDT.getSelectedRow(),tblSTNDT.getSelectedColumn()).toString().length()>0)
    			{
    				mm_teSTNTBLINVFR obj=new mm_teSTNTBLINVFR();
    				obj.setSource(tblSTNDT);
    				if(obj.verify(tblSTNDT.getSelectedRow(),tblSTNDT.getSelectedColumn()))
    					tblSTNDT.getCellEditor().stopCellEditing();
    				else
    					return false;
    			}
    		}
		}
		return true;
	}		
	/**
	 *  Method to update the last Issue No.in the CO_CDTRN
	 *private void exeSTNNO(String P_strSTNNO)
	*/
	private void exeSTNNO()
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + txtSTNNO.getText().trim().substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP ='MM"+M_strSBSCD.substring(2,4)+"STN'";
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn +"'";			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeSTNNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
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
			if(!vldDATA())
			{
				return;
			}
			if(hstITMDT !=null)
				hstITMDT.clear();
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				chkAUTHO();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				delSTNNO();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				exeMODSTN();
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				strSTNDT = cl_dat.M_strLOGDT_pbst;
				strSTNNO = genSTNNO();
				if(strSTNNO !=null)
				{
					String L_strTOSTR=(String)cmbSTORE.getSelectedItem();
					L_strTOSTR=L_strTOSTR.substring(0,2);
					for(int i=0;i<tblSTNDT.getRowCount();i++)
					{
						if(tblSTNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							M_strSQLQRY="Insert into MM_STNTR(STN_MMSBS,STN_FRSTR,STN_TOSTR,STN_STNNO,STN_STNDT,STN_FRMAT,STN_TOMAT,STN_TRNQT,STN_STNRT,STN_FRLOC,STN_TOLOC,STN_STNTP,STN_WORNO,STN_PREBY,STN_RECBY,STN_APRBY,STN_STSFL,STN_TRNFL,STN_LUSBY,STN_LUPDT ) values ( ";
						    M_strSQLQRY+="'"+M_strSBSCD+"',";
						    M_strSQLQRY+="'"+M_strSBSCD.substring(2,4) +"',";
						    M_strSQLQRY+="'"+L_strTOSTR +"',";
						    M_strSQLQRY+="'"+txtSTNNO.getText().trim() +"',";
						    M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strSTNDT))+ "',";
						    M_strSQLQRY+="'"+tblSTNDT.getValueAt(i,TBL_FRMAT).toString() +"',";
						    M_strSQLQRY+="'"+tblSTNDT.getValueAt(i,TBL_TOMAT).toString() +"',";
						    M_strSQLQRY+= tblSTNDT.getValueAt(i,TBL_TRNQT).toString() +",";
						    M_strSQLQRY+= tblSTNDT.getValueAt(i,TBL_TRNRT).toString() +",";
						    M_strSQLQRY +="'"+tblSTNDT.getValueAt(i,TBL_FRLOC).toString() +"',";
							M_strSQLQRY +="'"+tblSTNDT.getValueAt(i,TBL_TOLOC).toString() +"',";
							M_strSQLQRY+="'"+txtSTNTP.getText().trim() +"',";
							M_strSQLQRY+="'"+txtWORNO.getText().trim() +"',";
							M_strSQLQRY+="'"+txtPREBY.getText().trim() +"',";
							M_strSQLQRY+="'"+txtRECBY.getText().trim() +"',";
							M_strSQLQRY+="'"+txtAPRBY.getText().trim() +"',";
							M_strSQLQRY += getUSGDTL("ST",'I',"0")+")";
						    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}		
					exeSTNNO();
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					strREMDS=txtREMDS.getText().trim();
					if(strREMDS.length()>0)
					{
						M_strSQLQRY="Insert into MM_RMMST (RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS,RM_MMSBS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT )";
						M_strSQLQRY +=" Values (";
						M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
						M_strSQLQRY += "'" + strTRNTP_fn + "',";
						M_strSQLQRY += "'" + strTRNTP_fn + "',";
						M_strSQLQRY += "'" + txtSTNNO.getText().trim() + "',";
						M_strSQLQRY += "'" + strREMDS + "',";
						M_strSQLQRY += "'" + M_strSBSCD +"',";
						M_strSQLQRY += getUSGDTL("RM",'I',"0")+")";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Data saved successfully",'N');
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							strSTNNO = txtSTNNO.getText().trim();
							JOptionPane.showMessageDialog(this,"Please, Note down the STN No. " + strSTNNO,"STN No.",JOptionPane.INFORMATION_MESSAGE);
							btnPRNT.setEnabled(true);
							clrCOMP();	
						}
						txtSTNNO.setText(strSTNNO);
						txtSTNDT.setText(cl_dat.M_strLOGDT_pbst);
						cl_dat.M_btnSAVE_pbst.setEnabled(false);
					}
				}
				else
				{
					setMSG("Error in saving",'E');
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	 	setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Function For Generate  STN NO 
	*/
	private String genSTNNO()
	{
		strSTNNO ="";
		String L_strSTNNO  = "",  L_strCODCD = "", L_strCCSVL = "",L_CHP01="";// for STN;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"STN' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn +"'  and  isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP01.trim().length() >0)
					{
						setMSG("dataBase IN USE",'E');
						M_rstRSSET.close();
						return null;
					}
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"STN'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + "'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY," ");
			if(cl_dat.exeDBCMT("genSTNNO"))
			{
				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
				for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
					L_strSTNNO += "0";
				L_strCCSVL = L_strSTNNO + L_strCCSVL;
				L_strSTNNO = L_strCODCD + L_strCCSVL;
				txtSTNNO.setText(L_strSTNNO);
				strSTNNO = L_strSTNNO;
			}
			else 
				return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genSTNNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_strSTNNO;
	}	
	/**
	 * Method for Authorization
	*/
	private void chkAUTHO()
	{
		try
		{
			if(txtSTNTP.getText().trim().equals(strFRWOR))
			{
					String L_strSTNNO=txtSTNNO.getText().trim();
					M_strSQLQRY  ="update MM_STNTR SET ";
					M_strSQLQRY +="STN_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +="STN_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
					M_strSQLQRY +="STN_STSFL= '2'";
					M_strSQLQRY  += " WHERE STN_FRSTR = '" + M_strSBSCD.substring(2,4) + "'";
					M_strSQLQRY += " and STN_STNNO = '" + L_strSTNNO + "'";
					M_strSQLQRY += " and isnull(STN_STSFL,'') <> 'X'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
			else
			{
				if(chkQUANT(tblSTNDT,TBL_FRMAT,TBL_TRNQT))
				{				
					String L_strSTNNO=txtSTNNO.getText().trim();
					M_strSQLQRY  ="update MM_STNTR SET ";
					M_strSQLQRY +="STN_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +="STN_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
					M_strSQLQRY +="STN_STSFL= '2'";
					M_strSQLQRY  += " WHERE STN_FRSTR = '" + M_strSBSCD.substring(2,4) + "'";
					M_strSQLQRY += " and STN_STNNO = '" + L_strSTNNO + "'";
					M_strSQLQRY += " and isnull(STN_STSFL,'') <> 'X'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeAUTH"))
				{
					setMSG("Data saved successfully",'N');
				}
			}
			else
			{
				setMSG("Error In Saving Data",'E');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"chkAUTHO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 *  Method to delete STN Number
	*/
	private void delSTNNO()
	{
		int L_intSELROW =0;
		String L_strTOMAT="";
		String L_strFRMAT="";
		try
		{
			for(int i=0;i<tblSTNDT.getRowCount();i++)
			{
				if(tblSTNDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELROW++;
					L_strFRMAT = nvlSTRVL(tblSTNDT.getValueAt(i,TBL_FRMAT).toString(),"");
					L_strTOMAT = nvlSTRVL(tblSTNDT.getValueAt(i,TBL_TOMAT).toString(),"");
					String L_strSTNNO=txtSTNNO.getText().trim();
					M_strSQLQRY  ="update MM_STNTR SET ";
					M_strSQLQRY +="STN_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"', ";
					M_strSQLQRY +="STN_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"', ";
					M_strSQLQRY +="STN_STSFL= 'X'";
					M_strSQLQRY += " WHERE STN_MMSBS = '"+ M_strSBSCD+ "'";
					M_strSQLQRY +=" AND STN_FRSTR='"+M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND STN_STNNO = '" + L_strSTNNO + "'";
					M_strSQLQRY += " AND STN_FRMAT = '" + L_strFRMAT + "'";
					M_strSQLQRY += " AND STN_TOMAT = '" + L_strTOMAT + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("delSTNNO"))
				{
					setMSG("Data Deleted successfully",'N');
				}
			}
			else
			{
				setMSG("Error In Deleting Data",'E');
			}
			if(L_intSELROW ==0)
			{
				setMSG("No rows selcted",'E');
				return;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"delSTNNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**
	 * Input Verifier Class 
	*/
	private class mm_teSTNTBLINVFR extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID>0)
				{
					if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))&&(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					{
						return true;
					}
					if(P_intCOLID!=TBL_CHKFL)
						if(((JTextField)tblSTNDT.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
							return true;
				}
				if(P_intCOLID==TBL_FRMAT)
				{
					strTEMP = tblSTNDT.getValueAt(P_intROWID,TBL_FRMAT).toString();
					if(strTEMP.length()>0&& strTEMP.length()<10)
					{
						setMSG("Invalid FROM  Item Code",'E');
						return false;
					}
					if(!vldFRMAT(strTEMP,P_intROWID))
					{
						return false;
					}
				}
				if(P_intCOLID==TBL_TRNQT)
				{			
					if(txtSTNTP.getText().trim().equals(strFRWOR))
						return true;
					else
					{
						if(!chkQUANT(tblSTNDT,TBL_FRMAT,P_intCOLID))
						{
							return false;
						}
					}
				}	
				if(P_intCOLID==TBL_TOMAT)
				{
					String L_strTOMAT =((JTextField)tblSTNDT.cmpEDITR[TBL_TOMAT]).getText();
					String L_strFRMAT =tblSTNDT.getValueAt(P_intROWID,TBL_FRMAT).toString();
					if(txtSTNTP.getText().trim().equals(strRGULR))
					{
						if(M_strSBSCD.substring(2,4).equals((cmbSTORE.getSelectedItem().toString()).substring(0,2)) && L_strTOMAT.equals(L_strFRMAT))
						{
							setMSG("TO Item Code & TO Store Can't same From Item Code & From Store ",'E');
							return false;
						}
					}
					if(!txtSTNTP.getText().trim().equals(strRGULR))
					{
						if(!L_strTOMAT.equals(L_strFRMAT))
						{
							setMSG("TO Item Code Should be Same  From Item Code ",'E');
							return false;
						}					
					}
					if(L_strTOMAT.length()>0&& L_strTOMAT.length()<10)
					{
						setMSG("Invalid TO Item Code",'E');
						return false;
					}
					if(!vldTOMAT(L_strTOMAT,P_intROWID))
					{
						return false;
					}
				}
			}				
			catch(Exception e)
			{
				setMSG(e,"InputVerifier");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}
	/**
	 * Method for validation of To Item Code
	*/
	private boolean vldTOMAT(String P_strMATCD,int P_intROWID)
	{
		String L_strSQLQRY="";
		ResultSet L_rstRSSET;
		try
		{
			if(txtSTNTP.getText().equals(strTOWOR))
			{
				M_strSQLQRY="Select STP_MATCD,(STP_MATDS)STN_TODES,(STP_UOMCD)STN_TOUOM from MM_STPRC where STP_MATCD = '" + P_strMATCD + "' and  isnull(STP_STSFL,'') <> 'X'";
			}
			else
			{		
				M_strSQLQRY="Select CT_MATCD,(CT_MATDS)STN_TODES,(CT_UOMCD)STN_TOUOM from CO_CTMST";
				M_strSQLQRY += " Where CT_MATCD = '" + P_strMATCD + "' and  isnull(CT_STSFL,'') <> 'X'";
			}
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_TODES"),tblSTNDT.getSelectedRow(),TBL_MATDS2);
					tblSTNDT.setValueAt(M_rstRSSET.getString("STN_TOUOM"),tblSTNDT.getSelectedRow(),TBL_TOUOM);
					L_strSQLQRY="Select ST_LOCCD from MM_STMST where ST_MATCD= '"+P_strMATCD+ "' And ST_STRTP='" + M_strSBSCD.substring(2,4) +"' and  isnull(ST_STSFL,'') <> 'X'";
					L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY );
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							tblSTNDT.setValueAt(L_rstRSSET.getString("ST_LOCCD"),tblSTNDT.getSelectedRow(),TBL_TOLOC);
						}
						if(L_rstRSSET !=null)
							L_rstRSSET.close();
					}
					M_rstRSSET.close();
					return true;
				}
				else
				{
					setMSG("Invalid to  Material Code.Press F1 for help",'E');
					M_rstRSSET.close();
					return false;
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"vldTOMAT");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	/**
	 * Method for validation of From Item Code
	*/
	private boolean vldFRMAT(String P_strMATCD,int P_intROWID)
	{
		String L_strSQLQRY="";
		ResultSet L_rstRSSET;
		try
		{
			if(txtSTNTP.getText().equals(strFRWOR))
			{
				M_strSQLQRY="Select CT_MATCD,(CT_MATDS)STN_FRDES,(CT_UOMCD)STN_FRUOM,(CT_PPORT)STN_STNRT  from CO_CTMST";
				M_strSQLQRY += " Where CT_MATCD = '" + P_strMATCD + "' and  isnull(CT_STSFL,'') <> 'X'";
			}
			else
			{
				M_strSQLQRY="Select STP_MATCD,(STP_MATDS)STN_FRDES,(STP_UOMCD)STN_FRUOM,(STP_YCLRT)STN_STNRT from MM_STPRC where  STP_MATCD = '" + P_strMATCD + "' AND STP_STRTP='"+M_strSBSCD.substring(2,4) +"' and  isnull(STP_YCSQT,0)>0";
			}
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{					
				tblSTNDT.setValueAt(M_rstRSSET.getString("STN_FRDES"),tblSTNDT.getSelectedRow(),TBL_MATDS);
				tblSTNDT.setValueAt(M_rstRSSET.getString("STN_FRUOM"),tblSTNDT.getSelectedRow(),TBL_UOMCD);
				tblSTNDT.setValueAt(M_rstRSSET.getString("STN_STNRT"),tblSTNDT.getSelectedRow(),TBL_TRNRT);
				
					L_strSQLQRY="Select ST_LOCCD from MM_STMST where ST_MATCD= '"+P_strMATCD+ "' And ST_STRTP='" + M_strSBSCD.substring(2,4) +"' and  isnull(ST_STSFL,'') <> 'X'";
					L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY );
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							tblSTNDT.setValueAt(L_rstRSSET.getString("ST_LOCCD"),tblSTNDT.getSelectedRow(),TBL_FRLOC);
						}
						L_rstRSSET.close();
					}
					M_rstRSSET.close();			
					return true;
				}
			}
			setMSG("Invalid Material Code.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"vldFRMAT");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	/**
	 * Method for Checking the Enter Quantity
	*/
	private boolean chkQUANT(JTable LP_TBLNM,int LP_intFRMAT,int LP_intTRNQT)
	{
		hstITMDT.clear();
		double L_dblTRNQT=0;
		double L_dblTOTQT=0;
		String L_strTOTQT="";
		String L_strFRMAT=new String();
		String L_strTRNQT=new String();
		for(int i=0 ;i<=LP_TBLNM.getRowCount();i++)
		{
			if(LP_TBLNM.getValueAt(i,LP_intTRNQT).toString().length()==0)
				break;
			L_strFRMAT=LP_TBLNM.getValueAt(i,LP_intFRMAT).toString();
			L_strTRNQT=LP_TBLNM.getValueAt(i,LP_intTRNQT).toString();
			if(hstITMDT.containsKey(L_strFRMAT))
			{
				L_strTOTQT=(String)hstITMDT.get(L_strFRMAT);
				L_dblTOTQT = Double.parseDouble(L_strTOTQT);
				L_dblTOTQT +=Double.parseDouble(L_strTRNQT);
				L_strTRNQT=String.valueOf(L_dblTOTQT);
				hstITMDT.remove(L_strFRMAT);
				hstITMDT.put(L_strFRMAT,L_strTRNQT);					
			}
			else
			{
				hstITMDT.put(L_strFRMAT,L_strTRNQT);
			}
			L_strTRNQT=(String)hstITMDT.get(L_strFRMAT);
			L_dblTRNQT=Double.parseDouble(L_strTRNQT);
			try
			{
				M_strSQLQRY="Select ST_STKQT from MM_STMST where ST_MATCD= '"+L_strFRMAT+ "' And ST_STKQT >="+L_dblTRNQT+"";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
					}
					else
					{
						setMSG("Transfer Quantity from Item "+ L_strFRMAT + " is more than Available Quantity ",'E');
						cl_dat.M_flgLCUPD_pbst=false;
						return false;
					}
					M_rstRSSET.close();
				}
				else
				{
					setMSG("Enter Quantity in Item "+ L_strFRMAT + " is more than Available Quantity ",'E');
					cl_dat.M_flgLCUPD_pbst=false;
					return false;
				}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
			}
			catch(Exception e)
			{
				setMSG(e,"chkQUANT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
			return true;
	}
}