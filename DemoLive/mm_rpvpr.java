
/*
System Name   : Material Management System.
Program Name  : List of Issue
Program Desc. : Report to generate list of issues between given data range.
Author        : Mr.Aawaj Jain
Date          : 2007
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;

/**<pre>
System Name  : Material Management System.
 
Program Name : Supplier Analysis Report.

Purpose : Program to generate list of issues between given data range according to 
the selected Criteria.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_POMST       PO_STRTP,PO_PORTP,PO_PORNOPO_MATCD                      #
CO_PTMST       PT_PRTTP,PT_PRTCD									   #	
               
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtPRTCD    PT_PRTCD       CO_PTMST      VARCHAR(3)     Party Code
txtFRDAT    PO_CMPDT	   MM_POMST	
txtTODAT    PO_CMPDT	   MM_POMST
--------------------------------------------------------------------------------------


<I>
<B>Validations :</B>
    - To date must be greater then From data & Smaller than Current date.
    - Party Code must be valid.
</I> */

class mm_rpvpr extends cl_rbase 
{

	private JTextField txtPRTCD;			
	private JTextField txtFRDAT;			
	private JTextField txtTODAT;			
	private JLabel lblPRTNM;			

	private String strPRTNM;	/**String Var for Party Name	 */
	private String strMATCD;    /**String Var for mat code   	 */
	private String strUOMCD;
	private String strMATDS;	/**String Var for mat description*/
	private String strCMPDT;    /**String Var for PO Comp Date	 */
	private String strGRNNO;	/**String Var for grn no.   	 */
	private String strPORNO;
	private String strGRNDT;	/**String Var for grn date  	 */
	private float fltRECQT;	    /**String Var for rec qty   	 */
	private float fltACPQT;		/**String Var for accp qty  	 */
	private String strQLTFL;	/**String Var for quality rating */
	private String strDELFL;	/**String Var for del rating 	 */
	private String strDOCNO;
	private String strGINDT;	
	
	private String strFILNM;	/**String Var for File Name	 */						
	
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mm_rpvpr.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private float fltQLTFLA=0;
	private	float fltQLTFLB=0;
	private	float fltQLTFLC=0;
	private	float fltDELFLA=0;
	private	float fltDELFLB=0;
	private	float fltDELFLC=0;
	private	float intTOTREC=0;
	private float cntRECQT=0;
	private	float cntACPQT=0;
	
	mm_rpvpr()		/*  Constructor   */
	{
		super(2);
		try
		{
			lblPRTNM= new JLabel("");
			setMatrix(20,7);
			
			add(new JLabel("Vendor Code          "),5,3,1,1,this,'L');
			add(txtPRTCD= new TxtLimit(5),5,4,1,1,this,'L');
			
			add(new JLabel("Vendor Name          "),6,3,1,1,this,'L');
			add(lblPRTNM,6,4,1,3,this,'L');
			
			add(new JLabel("From Date            "),7,3,1,1,this,'L');
			add(txtFRDAT = new TxtDate(),7,4,1,1,this,'L');
						
			add(new JLabel("To Date              "),8,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),8,4,1,1,this,'L');
			
			setENBL(false);
			
			INPVF oINPVF=new INPVF();
			/*for(int i=0;i<M_vtrSCCOMP.size();i++)
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
			}*/
			txtPRTCD.setInputVerifier(oINPVF);
			txtFRDAT.setInputVerifier(oINPVF);
			txtTODAT.setInputVerifier(oINPVF);
			lblPRTNM.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);

		if(txtPRTCD.getText().length()==0)
			lblPRTNM.setText("");
		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
				M_cmbDESTN.requestFocus(); 
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst)){
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst)){
				setENBL(true);
			}
		}	
		
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC==txtPRTCD)
				{
					M_strHLPFLD="txtPRTCD";
					M_strSQLQRY="Select distinct po_vencd,pt_prtnm from MM_POMST,CO_PTMST where pt_prttp='S' and pt_prttp = po_ventp and pt_prtcd = po_vencd and po_strtp ='"+M_strSBSCD.substring(2,4)+"' ";
					M_strSQLQRY += " and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND po_vencd like '"+txtPRTCD.getText().trim().toUpperCase()+"%' order by po_vencd";
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Vendor Code", "Vendor Name"},2,"CT",new int[]{107,400});
				}		
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtPRTCD)
				{
					txtFRDAT.requestFocus();
				}	
				if(M_objSOURC == txtFRDAT)
				{
					txtTODAT.requestFocus();
				}
				if(M_objSOURC == txtTODAT)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}	
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is KeyPressed");
		}	
	
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				//System.out.println("inside help ok");
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtPRTCD.setText(L_STRTKN1.nextToken());
				lblPRTNM.setText(L_STRTKN1.nextToken());
				//System.out.println(">>>>>>>"+L_STRTKN1.nextToken());
			}
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL()
	{
		try
		{
			String strMATCDOLD="";
			
			
			
			F_OUT = new FileOutputStream(strRPFNM);
			D_OUT = new DataOutputStream(F_OUT);

			genRPHDR();
			
			
			//M_strSQLQRY="select pt_prtnm,isnull(po_matcd,' ') po_matcd,isnull(st_uomcd,' ') st_uomcd,isnull(st_matds,' ') st_matds,po_cmpdt,isnull(gr_grnno,' ') gr_grnno,isnull(po_porno,' ') po_porno,gr_grndt,isnull(gr_recqt,0)  gr_recqt,isnull(gr_acpqt,0) gr_acpqt,isnull(gr_qltfl,' ') gr_qltfl,isnull(gr_delfl,' ') gr_delfl,isnull(wb_docno,' ') wb_docno,wb_gindt from mm_pomst,co_ptmst,mm_stmst left outer join mm_grmst  on po_strtp=gr_strtp and po_porno=gr_porno and po_matcd = gr_matcd  left outer join mm_wbtrn on gr_ginno=wb_docno where po_matcd=st_matcd and pt_prttp='S' and pt_prtcd=po_vencd and  substr(po_matcd,1,4) in ('6804','6805','6810') and po_matcd not in ('6805010045','6805010035','6810150035') and po_strtp in ('07','08') and po_vencd='"+txtPRTCD.getText()+"' and date(po_cmpdt) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' order by pt_prtnm,po_matcd,gr_delfl";
			M_strSQLQRY="select distinct isnull(po_matcd,' ') po_matcd,isnull(st_uomcd,' ') st_uomcd,isnull(st_matds,' ') st_matds,po_cmpdt,isnull(gr_grnno,' ') gr_grnno,isnull(po_porno,' ') po_porno,gr_grndt,isnull(gr_recqt,0)  gr_recqt,isnull(gr_acpqt,0) gr_acpqt,isnull(gr_qltfl,' ') gr_qltfl,isnull(gr_delfl,' ') gr_delfl,isnull(wb_docno,' ') wb_docno,wb_gindt from mm_stmst ,mm_pomst left outer join mm_grmst  on po_strtp=gr_strtp and po_porno=gr_porno and po_matcd = gr_matcd  left outer join mm_wbtrn on gr_ginno=wb_docno and gr_cmpcd=wb_cmpcd where po_cmpcd=st_cmpcd and po_strtp = st_strtp and po_matcd=st_matcd and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND po_strtp ='"+M_strSBSCD.substring(2,4) +"' and po_vencd='"+txtPRTCD.getText()+"' and CONVERT(varchar,po_cmpdt,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' order by po_matcd,gr_delfl";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET!=null)
			{
				int i=0;
				//System.out.println(">>>>>>>>>>>"+M_rstRSSET);
				while(M_rstRSSET.next())
				{
					strPRTNM = lblPRTNM.getText();
					strMATCD = M_rstRSSET.getString("po_matcd");
					strUOMCD = M_rstRSSET.getString("st_uomcd");
					strMATDS = M_rstRSSET.getString("st_matds");
					//System.out.println(">>>>>>"+strMATCDOLD);
					if(M_rstRSSET.getDate("po_cmpdt") == null)
						strCMPDT = "";
					else
					    strCMPDT = M_fmtLCDAT.format(M_rstRSSET.getDate("po_cmpdt")).toString();
			
					strGRNNO = nvlSTRVL(M_rstRSSET.getString("gr_grnno"),"");
					strPORNO = nvlSTRVL(M_rstRSSET.getString("po_porno"),"");
			
					if(M_rstRSSET.getDate("gr_grndt") == null)
						strGRNDT = "";
					else
						strGRNDT = M_fmtLCDAT.format(M_rstRSSET.getDate("gr_grndt")).toString();
		    
					fltRECQT = M_rstRSSET.getFloat("gr_recqt");
					fltACPQT = M_rstRSSET.getFloat("gr_acpqt");
					strQLTFL = M_rstRSSET.getString("gr_qltfl");
					strDELFL = M_rstRSSET.getString("gr_delfl");
					strDOCNO = M_rstRSSET.getString("wb_docno");
					if(M_rstRSSET.getDate("wb_gindt") == null)
						strGINDT = " ";
					else
		
					strGINDT = M_fmtLCDAT.format(M_rstRSSET.getDate("wb_gindt")).toString();
					
					if(strMATCDOLD.equals(strMATCD)==false && strMATCDOLD.equals("")==false )
					{
						D_OUT.writeBytes(padSTRING('R',"",50));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(D_OUT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							D_OUT.writeBytes("<b>");
						crtNWLIN();
						prnTOTAL();
						D_OUT.writeBytes(padSTRING('R',"",50));
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						prnFMTCHR(D_OUT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							D_OUT.writeBytes("</b>");
						
						cntRECQT=0;
						cntACPQT=0;
						fltQLTFLA=0;
						fltQLTFLB=0;
						fltQLTFLC=0;
						fltDELFLA=0;
						fltDELFLB=0;
						fltDELFLC=0;
						intTOTREC=0;
					}	
					    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(D_OUT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							D_OUT.writeBytes("<b>");

						if(strMATCDOLD.equals(strMATCD)==false)	
						{	
							crtNWLIN();
							D_OUT.writeBytes(padSTRING('R',strMATCD,15));
							strMATCDOLD=strMATCD;
					
							D_OUT.writeBytes(padSTRING('R',strUOMCD,15));
							
									
							D_OUT.writeBytes(padSTRING('R',strMATDS,35));
							crtNWLIN();
							crtNWLIN();
						}
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(D_OUT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							D_OUT.writeBytes("</b>");
						
						D_OUT.writeBytes(padSTRING('R',String.valueOf(strPORNO),15));
						D_OUT.writeBytes(padSTRING('R',String.valueOf(strGRNNO),15));
						D_OUT.writeBytes(padSTRING('R',strCMPDT,12));
						D_OUT.writeBytes(padSTRING('R',strGINDT,13));
						D_OUT.writeBytes(padSTRING('R',strDELFL,10));
						D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltRECQT,3),11));
						cntRECQT+=fltRECQT;
						D_OUT.writeBytes(padSTRING('L',setNumberFormat(fltACPQT,3),11));
						cntACPQT+=fltACPQT;
						D_OUT.writeBytes(padSTRING('R',"",4));
						D_OUT.writeBytes(padSTRING('R',strQLTFL,11));
						if(strQLTFL.equals("A"))
							fltQLTFLA++;
						if(strQLTFL.equals("B"))
							fltQLTFLB++;
						if(strQLTFL.equals("C"))
							fltQLTFLC++;
						
						
						if(strDELFL.equals("A"))						
							fltDELFLA++;
						if(strDELFL.equals("B"))						
							fltDELFLB++;
						if(strDELFL.equals("C"))						
							fltDELFLC++;
						crtNWLIN();
						i++;		
						intTOTREC++;
				}
				D_OUT.writeBytes(padSTRING('R',"",50));
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("<b>");
				crtNWLIN();
				
				prnTOTAL();
				//D_OUT.writeBytes(padSTRING('L',String.valueOf(cntQLTFL*100/intTOTREC)+"%",15));
				//D_OUT.writeBytes(padSTRING('L',String.valueOf(cntDELFL*100/intTOTREC)+"%",30));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',"",50));
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("</b>");
				intTOTREC=0;		
			}	
			
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"strRPTFL");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	private void prnTOTAL() 
	{
		try
		{
			D_OUT.writeBytes(padSTRING('R',"",45));
			D_OUT.writeBytes(padSTRING('R',"Total",10));
			
			
			
			if(fltDELFLA>0)
			{
				D_OUT.writeBytes(padSTRING('R',"A = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltDELFLA*100/intTOTREC,0)+"%",5));
				fltDELFLA=0;
			}
			else if(fltDELFLB>0)
			{
				D_OUT.writeBytes(padSTRING('R',"B = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltDELFLB*100/intTOTREC,0)+"%",5));
				fltDELFLB=0;
			}
			else if(fltDELFLC>0)
			{
				
				D_OUT.writeBytes(padSTRING('R',"C = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltDELFLC*100/intTOTREC,0)+"%",5));
				fltDELFLC=0;
			}
			
			D_OUT.writeBytes(padSTRING('L',setNumberFormat(cntRECQT,3),11));
			D_OUT.writeBytes(padSTRING('L',setNumberFormat(cntACPQT,3),11));
			D_OUT.writeBytes(padSTRING('L',"",4));
			
			if(fltQLTFLA>0)
			{
				D_OUT.writeBytes(padSTRING('R',"A = ",5));
				//System.out.println(">>>>"+setNumberFormat(fltQLTFLA*100/intTOTREC,0));
				//D_OUT.writeBytes(padSTRING('R',String.valueOf(fltQLTFLA*100/intTOTREC)+"%",10));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltQLTFLA*100/intTOTREC,0)+"%",5));
				fltQLTFLA=0;
			}
			else if(fltQLTFLB>0)
			{
				D_OUT.writeBytes(padSTRING('R',"B = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltQLTFLB*100/intTOTREC,0)+"%",5));
				fltQLTFLB=0;
			}
			else if(fltQLTFLC>0)
			{
				D_OUT.writeBytes(padSTRING('R',"C = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltQLTFLC*100/intTOTREC,0)+"%",5));
				fltQLTFLC=0;
			}
						
			crtNWLIN();
						
			D_OUT.writeBytes(padSTRING('R',"",55));
						
			if(fltDELFLB>0)
			{
				D_OUT.writeBytes(padSTRING('R',"B = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltDELFLB*100/intTOTREC,0)+"%",5));
				fltDELFLB=0;
			}	
			else if(fltDELFLC>0)
			{
				D_OUT.writeBytes(padSTRING('R',"C = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltDELFLC*100/intTOTREC,0)+"%",5));
				fltDELFLC=0;
			}
			
			if(fltQLTFLB>0)
			{
				D_OUT.writeBytes(padSTRING('R',"",26));
				D_OUT.writeBytes(padSTRING('R',"B = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltQLTFLB*100/intTOTREC,0)+"%",5));
				fltQLTFLB=0;
				crtNWLIN();
			}	
			else if(fltQLTFLC>0)
			{
				D_OUT.writeBytes(padSTRING('R',"",26));
				D_OUT.writeBytes(padSTRING('R',"C = ",5));
				//System.out.println(">>>>"+setNumberFormat(fltQLTFLC*100/intTOTREC,0));
				//D_OUT.writeBytes(padSTRING('R',String.valueOf(fltQLTFLC*100/intTOTREC)+"%",10));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltQLTFLC*100/intTOTREC,0)+"%",5));
				fltQLTFLC=0;
				crtNWLIN();
			}
			
			D_OUT.writeBytes(padSTRING('R',"",55));
			
			if(fltDELFLC>0)
			{
				
				D_OUT.writeBytes(padSTRING('R',"C = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltDELFLC*100/intTOTREC,0)+"%",5));
				fltDELFLC=0;
			}
			if(fltQLTFLC>0)
			{	
				D_OUT.writeBytes(padSTRING('R',"",26));
				D_OUT.writeBytes(padSTRING('R',"C = ",5));
				D_OUT.writeBytes(padSTRING('R',setNumberFormat(fltQLTFLC*100/intTOTREC,0)+"%",5));
				fltQLTFLC=0;
			}	
			
		}
		catch(Exception e)
		{
		    setMSG(e,"prnTOTAL");
		}
	}
	
	
	
	private void crtNWLIN() 
	{
		try
		{
			
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(cl_dat.M_intLINNO_pbst >60)
			{		
				genRPFTR();
				genRPHDR();			
			}
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}
	
	void genRPHDR()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strCPI12);
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
			}	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			{	
				D_OUT.writeBytes("<b>");
			    D_OUT.writeBytes("<HTML><HEAD><Title>List of Issues </title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	
									
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOENH);
			
			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		//D_OUT.writeBytes(padSTRING('R',"",6));
    		prnFMTCHR(D_OUT,M_strBOLD);
    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
		    D_OUT.writeBytes(padSTRING('R',"  "+cl_dat.M_strLOGDT_pbst ,10));
    		crtNWLIN();
    		//D_OUT.writeBytes(padSTRING('R',"",6));
    		D_OUT.writeBytes(padSTRING('R',"Supplier Analysis Report  ",50));
    		D_OUT.writeBytes(padSTRING('L',"Page No    : ",40));
			D_OUT.writeBytes(padSTRING('R',"  "+cl_dat.M_PAGENO,10));
    		prnFMTCHR(D_OUT,M_strNOBOLD);
    		crtNWLIN();
    		//crtLINE(128);
    		crtNWLIN();
			
			D_OUT.writeBytes(padSTRING('R',"Supplier Name    : "+lblPRTNM.getText().toString()+" ("+txtPRTCD.getText()+") ",59));
			//D_OUT.writeBytes(padSTRING('L',"Period From   : "+txtFRDAT.getText().toString()+"  To  "+txtTODAT.getText().toString()),96);
			D_OUT.writeBytes(padSTRING('L',"Period From     "+txtFRDAT.getText().toString()+"   To  "+txtTODAT.getText().toString(),43));
			//D_OUT.writeBytes(padSTRING('L',"   To  "+txtTODAT.getText().toString(),20));
			/*D_OUT.writeBytes(padSTRING('L',"Page No : "+intPAGNO,20));*/
			crtNWLIN();         	
			crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("Item Code      UOM            Description																								  ");
			crtNWLIN();
			D_OUT.writeBytes("P.O. No.       GRIN No.       PO Comp Dt  Gate-In Dt   Del Rating    Rec Qty    Acp Qty    Qlty Rating");
			crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");
			
		}catch(IOException L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			/* commented to 01/02/2007 to remove total priniting prob after page break*/
			//strDPTMP = "";
			//strMTTMP = "";
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(IOException L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}	
	
	boolean vldDATA()
	{
		try
		{
			if(txtPRTCD.getText().trim().length() ==0)
    		{
				txtPRTCD.setEnabled(true);
				txtPRTCD.requestFocus();
    			setMSG("Enter Vendor Code",'E');
    			return false;
    		}
			if(txtFRDAT.getText().trim().length() ==0)
    		{
				txtFRDAT.setEnabled(true);
				txtFRDAT.requestFocus();
    			setMSG("Enter From Date",'E');
    			return false;
    		}
			if(txtTODAT.getText().trim().length() ==0)
    		{
				
				txtTODAT.requestFocus();
    			txtTODAT.setEnabled(true);
				setMSG("Enter To Date",'E');
    			return false;
    		}
			return true;
		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA()");
			return false;
		}
	}
	
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 0;
			cntRECQT=0;
			cntACPQT=0;
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "mm_rpvpr.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "mm_rpvpr.doc";
			
			genRPTFL();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				*/	
				}	
				else 
			    {    
					Runtime r = Runtime.getRuntime();
					Process p = null;					    
					p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
			    if(M_rdbHTML.isSelected())
			        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			    else
			        p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		//txtPRTCD.setEnabled(true);
		txtFRDAT.setEnabled(false);
		txtTODAT.setEnabled(false);
		txtPRTCD.requestFocus();
	}
	
	
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
					return true;
			
			if(input == txtPRTCD)
			{
				if(txtPRTCD.getText().length()<5)
				{
					setMSG("Enter Valid Vendor Code..",'E');
					return false;
				}
				else
				{	
					//M_strSQLQRY="Select distinct pt_prtcd,pt_prtnm from CO_PTMST where pt_prttp='S' and pt_prtcd ='"+txtPRTCD.getText().trim().toUpperCase()+"' order by pt_prtcd";	
					M_strSQLQRY="Select distinct po_vencd,pt_prtnm from MM_POMST,CO_PTMST where pt_prttp='S' and pt_prttp = po_ventp and pt_prtcd = po_vencd and po_strtp ='"+M_strSBSCD.substring(2,4)+"' ";
					M_strSQLQRY += " and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND po_vencd like '"+txtPRTCD.getText().trim().toUpperCase()+"%' order by po_vencd";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					//System.out.println("~~~~"+M_strSQLQRY);
					//System.out.println("~~~~"+M_rstRSSET);
					
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtPRTCD.setText(txtPRTCD.getText().toUpperCase().trim());
						txtFRDAT.setEnabled(true);
						//txtFRDAT.requestFocus();
						txtTODAT.setEnabled(true);
						lblPRTNM.setText(M_rstRSSET.getString("pt_prtnm"));
					}
					else 
					{
						setMSG("Enter Valid Vendor Code..",'E');
						return false;
					}
					
				}
			}
			if(input == txtFRDAT)
			{
				if(M_fmtLCDAT.parse(txtFRDAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From date can not be greater than Today's date..",'E');
					return false;
				}
				//txtTODAT.requestFocus();
			}
			if(input == txtTODAT)
			{
				if(M_fmtLCDAT.parse(txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("TO date can not be greater than Today's date..",'E');
					return false;
				}
				else if(M_fmtLCDAT.parse(txtFRDAT.getText()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
					return false;
				}
				//cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"verify()");
		}

		return true;	
	}
}
}



