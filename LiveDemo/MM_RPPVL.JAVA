/*
System Name		: Materials Management System
Program Name	: Probable Vendor List
Author			: Mr.S.R.Mehesare
Modified Date	: 22/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JRadioButton;import javax.swing.JPanel;
import javax.swing.ButtonGroup;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;import javax.swing.JComponent;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;import java.util.Vector;import java.util.Hashtable;
import java.awt.Color; import java.sql.ResultSet;

/**<pre>
System Name : Material Management System.
 
Program Name : Probable Vendor List

Purpose : Program to generate the Report for Probable Vendor List for given Indent Number.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                               #
CO_PTMST       PT_PRTTP,PT_PRTCD                                        #
CO_TXDOC       TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD             #
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD             #
MM_INMST       IN_MMSBS,IN_STRTP,IN_INDNO,IN_MATCD                      #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtINDNO    IN_INDNO                MM_INMST           VARCHAR(8)    Indent Number.
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from MM_POMST,CO_PTMST,CO_TXDOC and CO_CTMST for condiations :-
    1) TX_SYSCD='MM'
    2) AND TX_DOCTP = 'POR' 
    3) AND TX_SBSCD = PO_MMSBS
    4) AND TX_DOCNO=PO_PORNO 
    5) AND TX_PRDCD like 'XXXX%'
    6) AND PT_PRTTP = 'S' 
    7) AND PT_PRTCD = PO_VENCD
    8) AND PO_MATCD in (Select distinct IN_MATCD from MM_INMST where 
                      1) IN_INDNO = Specified Indent Number.
                      2) AND PO_MATCD = CT_MATCD
<B>Validations & Other Information:</B>    
    - Indent Number Specified Must be valid.
   Report is Available in following Catagories :
    - PS - Prime 
    - PS - Non Prime
    - SPS - All catagories
    - Articles of PS
    - For the day
    - For the month
    - For all regions
    - For all zones in a region
    - For a specific zone
      This Report makes use of HTML format. Methods in cl_rbase are overridden for the same
      Product catagory wise seggregation is done dynamically using 'COXXPGR' series in CO_CDTRN.
</I> */

class mm_rppvl extends cl_rbase
{										/** JTextField to specify & display IndentNumber.*/
	private JTextField txtINDNO;		/** JRadioButton to specify Vendor-wise report.*/
	private JRadioButton rdbVDRWS;		/** JRedioButton to specify P.O. wise Report.*/
	private JRadioButton rdbPORWS;		/** DataOutputStream to generate & hold the report data in the stream.*/
	private DataOutputStream dosREPORT; /** FileoutputStream to generate report File from the Stream of data.*/
	private FileOutputStream fosREPORT;	/** Vector Object to store material codes in an indent */
	private Vector<String> vtrMATCD;			/** Vector Object to store Tax catagory descriptions */
	private Vector<String> vtrTAXCD;			/** HashTable to hold the Tax Category code & associated description.*/
	private Hashtable<String,String> hstTAXCT;			/** Flga to indicate that, help screen is being called. disables input verifier in that case */
	private boolean flgHELP;			/** String varaible to specify the Name for generated report file Name. */
	private String strFILNM;            /** Integer variable to count the number of records fetched to block the report if no data found.*/
	private int intRECCT;				/** String variable to append the Dotted Line in the Report.*/
	private String strDOTLN = "----------------------------------------------------------------------------------------------------------------------------------------------------------";
	mm_rppvl()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_lblTODAT);			
			add(new JLabel("Indent No."),2,1,1,1,this,'L');
			add(txtINDNO=new TxtNumLimit(8.0),2,2,1,1,this,'L');
			JPanel L_pnlTEMP=new JPanel(null);
			add(rdbVDRWS=new JRadioButton("Vendor-wise",true),1,1,1,1,L_pnlTEMP,'L');
			add(rdbPORWS=new JRadioButton("P.O.-wise"),1,2,1,1,L_pnlTEMP,'L');
			ButtonGroup L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbPORWS);L_btgTEMP.add(rdbVDRWS);
			L_pnlTEMP.setBorder(BorderFactory.createTitledBorder("Find by"));
			add(L_pnlTEMP,2,4,2,2.1,this,'L');
			//RETREIVING TAX CODE DESCRIPTIONS AND PUTTING IN A HASHTABLE
			M_rstRSSET=cl_dat.exeSQLQRY0("SELECT * FROM CO_CDTRN WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='COXXTAX' AND CMT_CHP01 = '01'");
			if(M_rstRSSET!=null)
			{
				hstTAXCT=new Hashtable<String,String>(10,0.8f);
				while(M_rstRSSET.next())				
					hstTAXCT.put(M_rstRSSET.getString("CMT_CODCD"),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));				
				M_rstRSSET.close();
			}			
			M_rstRSSET = cl_dat.exeSQLQRY0("SELECT CMT_CODCD FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP = 'COXXTAX' AND CMT_CHP01 = '01'");
			if(M_rstRSSET != null)//add tax code to vector 
			{
				vtrTAXCD = new Vector<String>(10,5);
				while(M_rstRSSET.next())				
					vtrTAXCD.add(M_rstRSSET.getString("CMT_CODCD"));				
				M_rstRSSET.close();
			}			
			INPVF oINPVF =new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(oINPVF);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
					{
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
					}
				}
				else
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	public void actionPerformed(ActionEvent P_AE)
	{
		super.actionPerformed(P_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				txtINDNO.requestFocus();
			else if(M_objSOURC==txtINDNO)
				rdbVDRWS.requestFocus();
			else if(M_objSOURC==rdbVDRWS || M_objSOURC==rdbPORWS)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}	
	public void keyPressed(KeyEvent P_KE)
	{
		super.keyPressed(P_KE);
		try
		{
			if(M_objSOURC==txtINDNO && P_KE.getKeyCode()==P_KE.VK_F1)
			{
				flgHELP=true;
				M_strHLPFLD = "txtINDNO";
				M_strSQLQRY = "SELECT DISTINCT IN_INDNO,IN_INDDT FROM MM_INMST WHERE "
					+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS='"+M_strSBSCD+"' ";
				if(txtINDNO.getText().trim().length() > 0)
					M_strSQLQRY += "AND IN_INDNO LIKE '"+txtINDNO.getText().trim()+"%' ";
				M_strSQLQRY += "ORDER BY IN_INDDT DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[] {"Indent Number","Indent Date"},2 ,"CT");
			}
			else if(P_KE.getKeyCode()==P_KE.VK_ENTER)
				((JComponent)M_objSOURC).transferFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
/**
 * Super class method overrided to execuate the F1 Help ofr Selected Field.
 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			flgHELP=false;
			if(M_strHLPFLD == "txtINDNO")
				txtINDNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Method to generate the Report & to forward it to specified Destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rppvl.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rppvl.doc";
			
			getDATA();
			
			if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Probable Vendor List"," ");
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
	 * Method to to ferch data & to club it with header & footer of the report.
	 */
	@SuppressWarnings("unchecked") void getDATA()
	{
		try
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;	
			intRECCT = 0;
			String L_strPORNO = "",L_strSQLQRY = "";
			ResultSet L_rstRSSET;			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			
			if(rdbVDRWS.isSelected())
			{
				M_strSQLQRY="Select * from MM_POMST,CO_PTMST,CO_TXDOC,CO_CTMST where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD='MM'";
				M_strSQLQRY += " AND TX_DOCTP = 'POR' AND TX_SBSCD = PO_MMSBS";
				M_strSQLQRY += " AND TX_DOCNO=PO_PORNO AND TX_PRDCD like 'XXXX%'";
				M_strSQLQRY += " AND PT_PRTTP = 'S' AND PT_PRTCD = PO_VENCD";
				M_strSQLQRY += " AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MATCD in (Select distinct IN_MATCD from MM_INMST where ";
				M_strSQLQRY += " IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDNO='"+txtINDNO.getText()+"') AND PO_MATCD = CT_MATCD order by PO_MATCD,PO_PORDT  desc";
				M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					Vector[] L_vtaVENCD=new Vector[vtrMATCD.size()];
					for (int i=0;i<L_vtaVENCD.length;i++)
						L_vtaVENCD[i]= new Vector<String>(3,2);
					//Array to store recordcount for each material code, with thre vendors for each material vode and latest three PO's of each vendor
					int[][] L_inaCOUNT=new int[vtrMATCD.size()][3];					
					String L_strMATCD="",L_strMATDS="",L_strVENCD="",L_strPRTNM="";
 					while(M_rstRSSET.next())
					{
						intRECCT = 1;
						if((cl_dat.M_intLINNO_pbst==0 && cl_dat.M_PAGENO ==0) || cl_dat.M_intLINNO_pbst > 55)
						{
							if(cl_dat.M_PAGENO > 0)
							{
								dosREPORT.writeBytes("\n"+strDOTLN);					
								cl_dat.M_intLINNO_pbst = 1;
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");						
							}							
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							{
							    prnFMTCHR(dosREPORT,M_strNOCPI17);
							    prnFMTCHR(dosREPORT,M_strCPI10);
								prnFMTCHR(dosREPORT,M_strCPI17);
								prnFMTCHR(dosREPORT,M_strBOLD);
							}
							if(M_rdbHTML.isSelected())
							{
							    dosREPORT.writeBytes("<HTML><HEAD><Title>Probable Vendor List</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
								dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE><B>"); 
							}
							dosREPORT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd.",100)+"Page : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");
							dosREPORT.writeBytes(padSTRING('R',"Probable Vendor List For Indent No. : "+txtINDNO.getText()+" as on "+cl_dat.M_strLOGDT_pbst+" (Vendorwise Previous Supply Details)",120)+"\n");						
							dosREPORT.writeBytes(strDOTLN +"\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(padSTRING('R',"Material Code",45)+padSTRING('R',"Vendor Code",45)+padSTRING('R',"P. Order",12)+padSTRING('R',"P. Order",12)+padSTRING('R',"Rate",12)+"Tax Details"+"\n");						
							dosREPORT.writeBytes(padSTRING('R',"Description",45)+padSTRING('R',"Name",45)+padSTRING('R',"Number",12)+padSTRING('R',"Date",12)+padSTRING('R',"in Rs.",12)+"& Payment Term"+"\n");
							dosREPORT.writeBytes(strDOTLN +"\n");
							cl_dat.M_intLINNO_pbst++;							
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							else if(M_rdbHTML.isSelected())						
							    dosREPORT.writeBytes("</B>");															
						}
						
						if(L_vtaVENCD[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))].size()>2)//Three vendors for the material code are alreay found
							continue;						
						if(L_vtaVENCD[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))].contains(M_rstRSSET.getString("PO_VENCD")))//Three PO's for this vendor are already found
						   if(L_inaCOUNT[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))][L_vtaVENCD[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))].indexOf(M_rstRSSET.getString("PO_VENCD"))]>2)
							   continue;						
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst++;						
						if(!L_strMATCD.equals(M_rstRSSET.getString("PO_MATCD")))//Display MATCD
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),""),45)+"\n");
						else
							dosREPORT.writeBytes(padSTRING('R',"",45)+"\n");											
						if((!L_strVENCD.equals(M_rstRSSET.getString("PO_VENCD"))||!L_strMATCD.equals(M_rstRSSET.getString("PO_MATCD"))))//DIsplay VENCD
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),""),45));
						else
							dosREPORT.writeBytes(padSTRING('R',"",45));
						//Display PO details
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORDT"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),""),12));
					/*	//Display Exise tax
						if(nvlSTRVL(M_rstRSSET.getString("TX_EXCFL"),"") != "")
						{
							prnTAXDS("EXC");
						}*/
						
						if(!L_strMATCD.equals(M_rstRSSET.getString("CT_MATDS")))
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),45)+"\n");
						else
							dosREPORT.writeBytes(padSTRING('R',"",45)+"\n");					
						cl_dat.M_intLINNO_pbst++;
						if(!L_strPRTNM.equals(M_rstRSSET.getString("PT_PRTNM"))||!L_strMATCD.equals(M_rstRSSET.getString("PO_MATCD")))//Display VENDOR NAMSE
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),45));
						else
							dosREPORT.writeBytes(padSTRING('R',"",45));
						
				
						//Add vendor in vector array and set PO count for the same
						if(L_vtaVENCD[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))].size()<4)
							if(!L_vtaVENCD[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))].contains(M_rstRSSET.getString("PO_VENCD")))
								L_vtaVENCD[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))].addElement(M_rstRSSET.getString("PO_VENCD"));
						L_inaCOUNT[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))][L_vtaVENCD[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))].indexOf(M_rstRSSET.getString("PO_VENCD"))]++;						
							
						if(nvlSTRVL(M_rstRSSET.getString("TX_EXCFL"),"") != "")//Display Exise tax
							prnTAXDS("EXC");					
						if(nvlSTRVL(M_rstRSSET.getString("TX_CSTFL"),"") != "")
						{
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst++;							
							dosREPORT.writeBytes(padSTRING('R',"",45+45+12+12+12));//Display Sales TAx
							if(nvlSTRVL(M_rstRSSET.getString("TX_CSTFL"),"").equals("P"))
								dosREPORT.writeBytes("CST.  : "+nvlSTRVL(M_rstRSSET.getString("TX_CSTVL"),"0.0")+" %");
							else if(nvlSTRVL(M_rstRSSET.getString("TX_CSTFL"),"").equals("A"))
								dosREPORT.writeBytes("CST.  : "+nvlSTRVL(M_rstRSSET.getString("TX_CSTVL"),"0.0")+" Rs");
							else if(nvlSTRVL(M_rstRSSET.getString("TX_CSTFL"),"").equals("I"))
								dosREPORT.writeBytes(hstTAXCT.get("CST")+" INCLUDED");
							else if(nvlSTRVL(M_rstRSSET.getString("TX_CSTFL"),"").equals("X"))
								dosREPORT.writeBytes(hstTAXCT.get("CST")+" AT ACTUALS");
							else
								dosREPORT.writeBytes(hstTAXCT.get("CST")+" N.A.");
						}						
						if(nvlSTRVL(M_rstRSSET.getString("TX_DSBFL"),"") != "")//Display Discount On Basic Amount details
							prnTAXDS("DSB");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_PNFFL"),"") != "")//Display Packaging and forwarding details
							prnTAXDS("PNF");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_STXFL"),"") != "")//Display Local Sales tax details
							prnTAXDS("STX");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_OCTFL"),"") != "")//Display Octroi charges details
							prnTAXDS("OCT");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_FRTFL"),"") != "")//Display Octroi charges details
							prnTAXDS("FRT");						
						if(nvlSTRVL(M_rstRSSET.getString("TX_INSFL"),"") != "")//Display Insurence details
							prnTAXDS("INS");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CDSFL"),"") != "")//Display Cash Discounts details
							prnTAXDS("CDS");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_INCFL"),"") != "")//Display Installation Charges details
							prnTAXDS("INC");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_ENCFL"),"") != "")//Display Erection and comm charges details
							prnTAXDS("ENC");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_FNIFL"),"") != "")//Display Fright and insuernce details
							prnTAXDS("FNI");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CDUFL"),"") != "")//Display Custom duty details
							prnTAXDS("CDU");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CLRFL"),"") != "")//Display Clearing charges details
							prnTAXDS("CLR");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_VATFL"),"") != "")//Display Value added taxes details
							prnTAXDS("VAT");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_SCHFL"),"") != "")//Display Sur charges details
							prnTAXDS("SCH");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CVDFL"),"") != "")//Display Counter veilling duty details
							prnTAXDS("CVD");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_WCTFL"),"") != "")//Display Work contract tax details
							prnTAXDS("WCT");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_RSTFL"),"") != "")//Display RST details
							prnTAXDS("RST");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_EDCFL"),"") != "")//Display Eduacation chess details
							prnTAXDS("EDC");
						////for FOB 
						L_strPORNO = nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),"");
						L_strSQLQRY = "SELECT TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL FROM CO_TXSPC "
							+"WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP = 'POR' AND TXT_DOCNO = '"+L_strPORNO+"' "
							+"AND TXT_PRDCD LIKE 'XXXXX%'";
						L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						//System.out.println(L_strSQLQRY+"<>"+L_rstRSSET);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								if(nvlSTRVL(L_rstRSSET.getString("TXT_CODFL"),"") != "")
								{
									dosREPORT.writeBytes("\n");
									dosREPORT.writeBytes(padSTRING('R',"",45+45+12+12+12));
									if(L_rstRSSET.getString("TXT_CODFL").equals("P"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODCD")+".    : "+L_rstRSSET.getString("TXT_CODVL")+"%");
									else if(L_rstRSSET.getString("TXT_CODFL").equals("A"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODCD")+".    : "+L_rstRSSET.getString("TXT_CODVL")+"Rs");
									else if(L_rstRSSET.getString("TXT_CODFL").equals("X"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODDS")+" AT ACTUALS");
									else if(L_rstRSSET.getString("TXT_CODFL").equals("I"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODDS")+" INCLUDED");
								}
							}
						}
						L_rstRSSET.close();
						////
						//Display Payment Terms
						if(nvlSTRVL(M_rstRSSET.getString("PO_CRDDY"),"") != "")
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',"",45+45+12+12+12));
							dosREPORT.writeBytes("P-Term  : "+M_rstRSSET.getString("PO_CRDDY")+" DAYS");
						}
						//Store current values in strings
						L_strMATCD=M_rstRSSET.getString("PO_MATCD");
						L_strMATDS=M_rstRSSET.getString("PO_MATDS");
						L_strVENCD=M_rstRSSET.getString("PO_VENCD");
						L_strPRTNM=M_rstRSSET.getString("PT_PRTNM");					
					}
				}
			}		
			else //P.O. wise Report
			{
				M_strSQLQRY="Select * from MM_POMST,CO_PTMST, CO_TXDOC,CO_CTMST where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD='MM' and TX_DOCTP='POR' and TX_SBSCD=PO_MMSBS and TX_DOCNO=PO_PORNO and TX_PRDCD like 'XXXX%' and PT_PRTTP='S' and PT_PRTCD=PO_VENCD and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MATCD in (Select distinct IN_MATCD from MM_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDNO='"+txtINDNO.getText()+"') and PO_MATCD = CT_MATCD order by PO_MATCD,PO_PORDT desc";
				M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
				
					int[] L_inaCOUNT=new int[vtrMATCD.size()];
					//To store values of previous records
					String L_strMATCD="",L_strMATDS="",L_strVENCD="",L_strPRTNM="";
					while(M_rstRSSET.next())
					{
						intRECCT = 1;
						if((cl_dat.M_intLINNO_pbst==0 && cl_dat.M_PAGENO ==0)||cl_dat.M_intLINNO_pbst>55)
						{
							if(cl_dat.M_PAGENO > 0)
							{
								dosREPORT.writeBytes("\n"+strDOTLN);					
								cl_dat.M_intLINNO_pbst = 1;
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");						
							}							
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							{
							    prnFMTCHR(dosREPORT,M_strNOCPI17);
							    prnFMTCHR(dosREPORT,M_strCPI10);
								prnFMTCHR(dosREPORT,M_strCPI17);
								prnFMTCHR(dosREPORT,M_strBOLD);
							}
							if(M_rdbHTML.isSelected())
							{
							    dosREPORT.writeBytes("<HTML><HEAD><Title>Probable Vendor List</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
								dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE><B>"); 
							}
							dosREPORT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd.",100)+"Page : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");
							dosREPORT.writeBytes(padSTRING('R',"Probable Vendor List For Indent No. : "+txtINDNO.getText()+" as on "+cl_dat.M_strLOGDT_pbst+" (P.O.wise Previous Supply Details)",120)+"     "+cl_dat.M_txtCLKDT_pbst.getText()+"\n");
							dosREPORT.writeBytes(strDOTLN +"\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(padSTRING('R',"Material Code",45)+padSTRING('R',"Vendor Code",45)+padSTRING('R',"P. Order",12)+padSTRING('R',"P. Order",12)+padSTRING('R',"Rate",12)+"Tax Details"+"\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(padSTRING('R',"Description",45)+padSTRING('R',"Name",45)+padSTRING('R',"Number",12)+padSTRING('R',"Date",12)+padSTRING('R',"in Rs.",12)+"& Payment Term"+"\n");
							cl_dat.M_intLINNO_pbst++;
							dosREPORT.writeBytes(strDOTLN +"\n");
							cl_dat.M_intLINNO_pbst++;
							prnFMTCHR(dosREPORT,M_strNOBOLD);							
						}
						//Three PO's for the material code are alreay found
						if(L_inaCOUNT[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))]>2)
							continue;
						
						dosREPORT.writeBytes("\n");//Display MATCD
						if(!L_strMATCD.equals(M_rstRSSET.getString("PO_MATCD")))
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),""),45));
						else
							dosREPORT.writeBytes(padSTRING('R',"",45));
						dosREPORT.writeBytes("\n");
						//DIsplay VENCD
						if((!L_strVENCD.equals(M_rstRSSET.getString("PO_VENCD"))||!L_strMATCD.equals(M_rstRSSET.getString("PO_MATCD"))))
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),""),45));
						else
							dosREPORT.writeBytes(padSTRING('R',"",45));
						//Display PO details
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORDT"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),""),12));
					/*	//Display Exise tax
						if(nvlSTRVL(M_rstRSSET.getString("TX_EXCFL"),"") != "")
						{
							crtNWLIN();
							prnTAXDS("EXC");
						}*/
						
						//Display MATDS	
						if(!L_strMATCD.equals(M_rstRSSET.getString("CT_MATDS")))
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),45));
						else
							dosREPORT.writeBytes(padSTRING('R',"",45));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst++;						
						if(!L_strPRTNM.equals(M_rstRSSET.getString("PT_PRTNM"))||!L_strMATCD.equals(M_rstRSSET.getString("PO_MATCD")))//Display VENDOR NAMSE
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),45));
						else
							dosREPORT.writeBytes(padSTRING('R',"",45));						
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst++;
						
						if(nvlSTRVL(M_rstRSSET.getString("TX_EXCFL"),"") != "")//Display Exise tax
							prnTAXDS("EXC");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CSTFL"),"") != "")//Display Sales Tax
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',"",45+45+12+12+12));
							if(M_rstRSSET.getString("TX_CSTFL").equals("P"))
								dosREPORT.writeBytes("CST.  : "+M_rstRSSET.getString("TX_CSTVL")+"%");
							else if(M_rstRSSET.getString("TX_CSTFL").equals("A"))
								dosREPORT.writeBytes("CST.  : "+M_rstRSSET.getString("TX_CSTVL")+"Rs");
							else if(M_rstRSSET.getString("TX_CSTFL").equals("I"))
								dosREPORT.writeBytes(hstTAXCT.get("CST")+" INCLUDED");
							else if(M_rstRSSET.getString("TX_CSTFL").equals("X"))
								dosREPORT.writeBytes(hstTAXCT.get("CST")+" AT ACTUALS");
							else
								dosREPORT.writeBytes(hstTAXCT.get("CST")+" N.A.");
						}						
						L_inaCOUNT[vtrMATCD.indexOf(M_rstRSSET.getString("PO_MATCD"))]++;//set PO count for the same
						if(nvlSTRVL(M_rstRSSET.getString("TX_FRTFL"),"") != "")//Display Frieght details
							prnTAXDS("FRT");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_DSBFL"),"") != "")//Display DSB details
							prnTAXDS("DSB");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_PNFFL"),"") != "")//Display PNF details
							prnTAXDS("PNF");						
						if(nvlSTRVL(M_rstRSSET.getString("TX_STXFL"),"") != "")//Display PNF details
							prnTAXDS("STX");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_OCTFL"),"") != "")//Display OCT details
							prnTAXDS("OCT");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_INSFL"),"") != "")//Display INS details
							prnTAXDS("INS");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CDSFL"),"") != "")//Display CDS details
							prnTAXDS("CDS");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_INCFL"),"") != "")//Display INC details
							prnTAXDS("INC");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_ENCFL"),"") != "")//Display ENC details
							prnTAXDS("ENC");						
						if(nvlSTRVL(M_rstRSSET.getString("TX_FNIFL"),"") != "")//Display FNI details
							prnTAXDS("FNI");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CDUFL"),"") != "")//Display CDU details
							prnTAXDS("CDU");						
						if(nvlSTRVL(M_rstRSSET.getString("TX_CLRFL"),"") != "")//Display CLR details
							prnTAXDS("CLR");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_VATFL"),"") != "")//Display VAT details
							prnTAXDS("VAT");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_SCHFL"),"") != "")//Display SCH details
							prnTAXDS("SCH");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_CVDFL"),"") != "")//Display CVD details
							prnTAXDS("CVD");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_WCTFL"),"") != "")//Display WCT details
							prnTAXDS("WCT");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_RSTFL"),"") != "")//Display Rst Details 
							prnTAXDS("RST");											
						if(nvlSTRVL(M_rstRSSET.getString("TX_EDCFL"),"") != "")//Display EDC details
							prnTAXDS("EDC");
						
						L_strPORNO = nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),"");
						L_strSQLQRY = "SELECT TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL FROM CO_TXSPC "
							+"WHERE  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD ='MM' AND TXT_DOCTP = 'POR' AND TXT_DOCNO = '"+L_strPORNO+"' "
							+"AND TXT_PRDCD LIKE 'XXXXX%'";
						L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);					
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								if(nvlSTRVL(L_rstRSSET.getString("TXT_CODFL"),"") != "")
								{
									dosREPORT.writeBytes("\n");
									dosREPORT.writeBytes(padSTRING('R',"",45+45+12+12+12));
									if(L_rstRSSET.getString("TXT_CODFL").equals("P"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODCD")+".   : "+L_rstRSSET.getString("TXT_CODVL")+"%");
									else if(L_rstRSSET.getString("TXT_CODFL").equals("A"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODCD")+".   : "+L_rstRSSET.getString("TXT_CODVL")+"Rs");
									else if(L_rstRSSET.getString("TXT_CODFL").equals("X"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODDS")+" AT ACTUALS");
									else if(L_rstRSSET.getString("TXT_CODFL").equals("I"))
										dosREPORT.writeBytes(L_rstRSSET.getString("TXT_CODDS")+" INCLUDED");
								}
							}
						}
						L_rstRSSET.close();											
						if(nvlSTRVL(M_rstRSSET.getString("PO_CRDDY"),"") != "")//Display Payment Terms
						{						
							dosREPORT.writeBytes("\n"+padSTRING('R',"",45+45+12+12+12));
							dosREPORT.writeBytes("P-Term  : "+M_rstRSSET.getString("PO_CRDDY")+" DAYS");
						}					
						L_strMATCD=M_rstRSSET.getString("PO_MATCD");
						L_strMATDS=M_rstRSSET.getString("PO_MATDS");
						L_strVENCD=M_rstRSSET.getString("PO_VENCD");
						L_strPRTNM=M_rstRSSET.getString("PT_PRTNM");					
					}
				}
				M_rstRSSET.close();
			}			
			dosREPORT.writeBytes("\n"+strDOTLN);			
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	 * Method to validate the data before execuation of the SQL Quary.
	 */
	boolean vldDATA()
	{
		try
		{
			cl_dat.M_PAGENO = 0;
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
	 * Method to print Tax Details
	 * @param strTAXDS String argument pass Tax Description
	 */
	public void prnTAXDS(String strTAXDS)
	{
		try
		{
			dosREPORT.writeBytes("\n"+padSTRING('R',"",45+45+12+12+12));
			if(M_rstRSSET.getString("TX_"+strTAXDS+"FL").equals("P"))
				dosREPORT.writeBytes(strTAXDS+".  : "+M_rstRSSET.getString("TX_"+strTAXDS+"VL")+"%");
			else if(M_rstRSSET.getString("TX_"+strTAXDS+"FL").equals("A"))
				dosREPORT.writeBytes(strTAXDS+".  : "+M_rstRSSET.getString("TX_"+strTAXDS+"VL")+" Rs");
			else if(M_rstRSSET.getString("TX_"+strTAXDS+"FL").equals("X"))
			{
				if((strTAXDS.equals("STX"))||(strTAXDS.equals("OTH")))
					dosREPORT.writeBytes(M_rstRSSET.getString("TX_"+strTAXDS+"DS"));
				else
				dosREPORT.writeBytes(hstTAXCT.get(strTAXDS)+" AT ACTUALS");
			}
			else if(M_rstRSSET.getString("TX_"+strTAXDS+"FL").equals("I"))
			{
				if((strTAXDS.equals("STX"))||(strTAXDS.equals("OTH")))
					dosREPORT.writeBytes(M_rstRSSET.getString("TX_"+strTAXDS+"DS"));
				else
					dosREPORT.writeBytes(hstTAXCT.get(strTAXDS)+" INCLUDED");
			}
			cl_dat.M_intLINNO_pbst++;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnTAXDS");
		}
	}
	private class INPVF extends InputVerifier
	{		
		public boolean verify(JComponent input)
		{
			try
			{
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length()==0)
						return true;
				if(input == txtINDNO)
				{
					if(flgHELP)
					{	
						flgHELP=false;
						return true;
					}
					boolean L_flgRTNVL=false;
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY0("Select Distinct IN_MATCD from MM_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDNO='"+txtINDNO.getText()+"'");
					if(L_rstRSSET!=null)
					{
						vtrMATCD=new Vector<String>(10,2);
					
						while(L_rstRSSET.next())
						{
							L_flgRTNVL=true;
							vtrMATCD.addElement(L_rstRSSET.getString("IN_MATCD"));
						}
						L_rstRSSET.close();
						if(!L_flgRTNVL)
							setMSG("Invalid Indent Number ..",'E');
						return L_flgRTNVL;
					}
					setMSG("Invalid Indent Number ..",'E');
					return false;
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"INPVF");
				return false;
			}
			return true;
		}
	}
}