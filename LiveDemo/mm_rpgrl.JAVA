
/*
System Name		: Materials Management System
Program Name	: GRIN List
Author			: Mr.S.R.Mehesare
Modified Date	: 20/07/2004  07/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import java.util.Hashtable;import java.util.Date;
import javax.swing.JCheckBox;import javax.swing.JComboBox;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.KeyEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;

class mm_rpgrl extends cl_rbase 
{								/** JTextField to display & enter From Date.*/
	private TxtDate txtFMDAT;	/** JTextField to display & enter To Date.*/
	private TxtDate txtTODAT;	/**	JComboBox to display & to select Gate in Type.*/
	private JComboBox cmbGRNTP;	/** JCheckBox to apply constaint for Accepted Quantity Greater Than Zero  */
	private JCheckBox chbACPQT;	/** String Variable for ISO Document Number1.*/
	private String strISOD1;	/** String Variable for ISO Document Number2.*/
	private String strISOD2;	/** String Variable for ISO Document Number3.*/
	private String strISOD3;	/** String Variable for GRIN Type.*/
	private String strGRNTP;	/** String Variable for GRIN Number.*/
	private String strGRNNO;	/** Integer Variable for GRIN Count. */
	private int intGRCNT;		/** Double variable to calculate the sum of bill Quantity.*/
	private double dblVALUE;
											/** FileOutputStream Object to generate Report File from Stream of Data.*/
	private FileOutputStream fosREPORT ;	/** DataOutputStream Object to hold Stream Of Data to genetate Report File from that Stream.*/
    private DataOutputStream dosREPORT ;	/** Hashtable object to Store GRIN Code & associated GRIN Name.*/
	private Hashtable<String,String> hstGRNNM;	
	private String strFILNM;
	private int intCOLCT=0;
	private String strDOTLN;
	private int intRECCT = 0;
	mm_rpgrl()
	{
		super(2);
		try
		{			
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);					
			add(new JLabel("GRIN Type"),4,3,1,1,this,'R');
			add(cmbGRNTP = new JComboBox(),4,4,1,1.5,this,'L');			
			add(new JLabel("From Date"),5,3,1,1,this,'R');
			add(txtFMDAT = new TxtDate(),5,4,1,1.5,this,'L');
			add(new JLabel("To Date"),6,3,1,1,this,'R');
			add(txtTODAT = new TxtDate(),6,4,1,1.5,this,'L');			
			add(chbACPQT = new JCheckBox("Acp. Qty. Greater Than Zero"),7,3,1,2,this,'L');
						
			//cmbGRNTP.addItem("Select");
			cmbGRNTP.addItem("All");
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
			hstGRNNM = new Hashtable<String,String>();			
			// Add GRIN Type Code And GRIN Name in cmbGRNTP and Hashtable 			
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' "
				+"AND CMT_CGSTP = 'MMXXGRN'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					cmbGRNTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
					hstGRNNM.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}
			//strISOD1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPGRN01");
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
				+ " AND CMT_CGSTP = 'MMXXRPT' AND isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET != null)
			{
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strTEMP.equals(""))
					{
						if(L_strTEMP.equals("MM_RPGRN01"))
							strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPGRN02"))
							strISOD2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPGRN03"))
							strISOD3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					}					
				}
				M_rstRSSET.close();
			}		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				txtFMDAT.requestFocus();
				if(txtFMDAT.getText().trim().length()==0)
				{
					try
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));	
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"VK_ENTER");
					}
				}
				setMSG("please Enter Date to generate Report..",'N');
				setENBL(true);
			}
			else			
				setENBL(false);			
		}
		else if (M_objSOURC == cmbGRNTP)
		{
			if(txtFMDAT.getText().trim().length()==0)
				txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtFMDAT.requestFocus();
			setMSG("please Enter Date to generate Report..",'N');
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			intGRCNT = 0;		//Initialise GRIN Count To 0 			
			strGRNNO = "";		//Initialise GRIN Number To ""
			dblVALUE = 0.0;			
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtFMDAT)
			{
				M_strHLPFLD = "txtFMDAT";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNDT FROM MM_GRMST "
					+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' ORDER BY GR_GRNDT ASC ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Date"},1,"CT");
			}
			if(M_objSOURC == txtTODAT)
			{
				txtTODAT.setText("");
				M_strHLPFLD = "txtTODAT";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNDT FROM MM_GRMST "
					+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' ORDER BY GR_GRNDT ASC ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN Date"},1,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{			
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
				{																								
					txtTODAT.requestFocus();
					setMSG("Please Enter Date to generate the Report..",'N');
				}
				else
					setMSG("From Date cannot be blank, please enter it..",'E');				
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length()==10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					setMSG("To Date cannot be blank, please enter it..",'E');
			}			
			if(M_objSOURC == chbACPQT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	/**
	 * Super Class Method to execuate F1 Help.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMDAT")
		{
			txtFMDAT.setText(cl_dat.M_strHLPSTR_pbst);
			txtTODAT.requestFocus();
		}
		if(M_strHLPFLD == "txtTODAT")
		{
			txtTODAT.setText(cl_dat.M_strHLPSTR_pbst);
			cmbGRNTP.requestFocus();
		}
	}
	/**
	 * Method to generate the Report File & to send at selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
	
		try
		{
		    if(M_rdbHTML.isSelected())
			{
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpgrl.html";
				strDOTLN = "----------------------------------------------------------------------------------------------------------------------";
				intCOLCT = 2;
			}
		    else if(M_rdbTEXT.isSelected())
			{
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgrl.doc";
				intCOLCT = 0;
				 strDOTLN ="----------------------------------------------------------------------------------------------------------------------------------------------";
			}
			intRECCT = 0;
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"GRIN List"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}	
	/**
	 * Method To Validate Inputs before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter Date Or Press F1 For Help..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Greater Than Today..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Please Enter Valid Date Or Press F1 For Help..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Greater Than Today..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Greater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount()==0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'E');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'E');
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
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	void getDATA()
	{
		try
		{			 
			setCursor(cl_dat.M_curWTSTS_pbst);	    	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>GRIN List </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			
			prnHEADER();
			
			strGRNTP = cmbGRNTP.getSelectedItem().toString().substring(0,2);
			
			M_strSQLQRY = "SELECT GR_GRNNO,GR_MATCD,GR_GRNDT,GR_GRNRT,GR_ACPQT,GR_REJQT,"
				+"GR_PORNO,GR_VENCD,GR_VENNM,(isnull(GR_BILVL,isnull(GR_PORVL,0))) VALUE, "
				+"GR_GRNTP,GR_GINNO,GR_RECQT,CT_MATDS,CT_UOMCD FROM MM_GRMST,CO_CTMST WHERE "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND CT_MATCD = GR_MATCD ";
			if(cmbGRNTP.getSelectedIndex() > 1)		//If GRIN Type Selection Other Than Select And All
				M_strSQLQRY += " AND GR_GRNTP = '"+strGRNTP+"' ";
			if(chbACPQT.isSelected())				//If Accpeted Quantity Greater Than Zero Is Selected
				M_strSQLQRY += " AND isnull(GR_ACPQT,0) > 0 ";
			M_strSQLQRY += " AND GR_GRNDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' ";
			M_strSQLQRY += " ORDER BY GR_GRNTP,GR_GRNNO ";
							//System.out.println(M_strSQLQRY);	
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			strGRNNO = "";
			strGRNTP = "";
			
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst > 60)		//Check Wherher Line Exceeds More Than 60
					{
						dosREPORT.writeBytes(strDOTLN);
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER();
					}
					//Check For Previous Gate GRIN Type With Current Record If Not Print GRIN Type
					if(!strGRNTP.equals(nvlSTRVL(M_rstRSSET.getString("GR_GRNTP"),"")))
					{
						if(!strGRNTP.equals(""))
						{
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 1;
						}
						strGRNTP = nvlSTRVL(M_rstRSSET.getString("GR_GRNTP"),"");
						dosREPORT.writeBytes("GRIN Type   : "+hstGRNNM.get(strGRNTP).toString());
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						
					}
					//Check Whether The GRIN Type Is not Equal To Previous One 
					//If Not Then Print GRIN Number,GRIN Date,P.O. Number,Vendor Code And Vendor Name
					if(!strGRNNO.equals(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")))
					{
						dosREPORT.writeBytes("\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						else if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
						//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),13));
						dosREPORT.writeBytes(padSTRING('R',strGRNNO,12));
						if(M_rstRSSET.getDate("GR_GRNDT") != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(M_rstRSSET.getDate("GR_GRNDT")),13));
						else
							dosREPORT.writeBytes(padSTRING('R',"",13));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""),7));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),40));
						dosREPORT.writeBytes("\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						else if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						intGRCNT += 1;	//Increment The GRIN Count By 1
						cl_dat.M_intLINNO_pbst += 2;
					}					
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),12));
					if(M_rdbTEXT.isSelected())
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),54));
					else
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),40));
					
					dosREPORT.writeBytes(" "+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("GR_RECQT"),3),14-intCOLCT));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("GR_ACPQT"),3),14-intCOLCT));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("GR_REJQT"),3),14-intCOLCT));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("GR_GRNRT"),2),14-intCOLCT));
					//Print The BilValue If Null Then Print P.O. Value  Value Is From Query
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("VALUE"),2),14-intCOLCT));
					dblVALUE = dblVALUE + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("VALUE"),"0.0"));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
				}				
				dosREPORT.writeBytes("\n"+ strDOTLN +"\n");
				dosREPORT.writeBytes(padSTRING('R',"Total GRIN : "+intGRCNT,20));	//Print Total GRIN Count
				dosREPORT.writeBytes(padSTRING('L',"GRIN Value : "+dblVALUE,20));													
				setMSG("Report completed.. ",'N');			
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				{
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strEJT);				
				}			
				if(M_rdbHTML.isSelected())			
				    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");				
				dosREPORT.close();		//Close Date OutPut Stream 
				fosREPORT.close();		//Close File OutPut Stream
				M_rstRSSET.close();
				setCursor(cl_dat.M_curDFSTS_pbst);						
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}	
	/**Method To Print Report Header  */
	void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO ++;
			dosREPORT.writeBytes("\n"+padSTRING('L',"------------------------------",strDOTLN.length()-1)+"\n");
			dosREPORT.writeBytes(padSTRING('L',"DOCUMENT REF : "+strISOD1,strDOTLN.length()-1)+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD2,strDOTLN.length()-1)+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD3,strDOTLN.length()-1)+"\n");
			dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length()-1)+"\n");			
			dosREPORT.writeBytes("SUPREME PETROCHEM LIMITED "+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"List Of Receipt From "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21)); 
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");     			
			dosREPORT.writeBytes(strDOTLN +"\n");			
			dosREPORT.writeBytes("GRIN No.    Date         P.O. No.    Gate In No. Vendor Code And Name                            \n");			
			if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes("Mat.Code    Description                                            UOM        Rcp.Qty       Acp.Qty       Rej.Qty       Unit Rt         Value\n");
			else
				dosREPORT.writeBytes("Mat.Code    Description                              UOM      Rcp.Qty     Acp.Qty     Rej.Qty     Unit Rt       Value\n");
			dosREPORT.writeBytes(strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 13;
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}