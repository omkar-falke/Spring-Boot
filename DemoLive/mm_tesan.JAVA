import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.util.Vector;
import javax.swing.InputVerifier;
import java.util.StringTokenizer;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class mm_tesan extends cl_pbase
{
	private JTextField txtSANNO,txtSANDT,txtMATCD,txtREMDS,txtPREBY,txtAPRBY,txtRECBY;
	private JButton btnPRNT;
	private cl_JTable tblSANDT;
	private String strSANNO,strREMDS,strSANDT,strTEMP;
	final String strTRNTP_fn = "SA";
	final String strRVMST_fn = "11"; // Revamp Stores
	private Hashtable<String,String>  hstITMDT = new Hashtable<String,String>();
	private Hashtable  hstWAVRT = new Hashtable();
	private mm_teSANTBLINVFR objTBLVRF;
	private final int TBL_CHKFL =0;
	private final int TBL_MATCD =1;
	private final int TBL_MATDS =2;
	private final int TBL_UOMCD =3;
	private final int TBL_SANQT =4;
	private final int TBL_SANRT =5;
	private final int TBL_STKQT =6;
	
	private final String strDOCTP_fn ="70";	
	
	mm_tesan()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(btnPRNT = new JButton("Print"),1,8,1,1,this,'L');
			add(new JLabel("SAN No."),2,1,1,1,this,'L');
			add(txtSANNO = new TxtLimit(8),2,2,1,1,this,'L');
			add(new JLabel("SAN Date"),2,3,1,1,this,'L');
			add(txtSANDT = new TxtDate(),2,4,1,1,this,'L');

			add(new JLabel("Prepared By"),3,1,1,1,this,'L');
			add(txtPREBY = new TxtLimit(3),3,2,1,1,this,'L');
			add(new JLabel("Recomm. By"),3,3,1,1,this,'L');
			add(txtRECBY = new TxtLimit(3),3,4,1,1,this,'L');
			add(new JLabel("Approved By"),3,5,1,1,this,'L');
			add(txtAPRBY = new TxtLimit(3),3,6,1,1,this,'L');
			
			String[] L_strTBLHD = {" ","Item Code","Description","UOM ","Adj.Qty","Adj. Rate","Stock"};
			int[] L_intCOLSZ = {15,80,375,80,90,90,30};
			tblSANDT = crtTBLPNL1(this,L_strTBLHD,100,4,1,10,7.9,L_intCOLSZ,new int[]{0});
			add(new JLabel("Remarks"),15,1,1,1,this,'L');
			add(txtREMDS=new TxtLimit(200),15,2,1,7,this,'L');
			txtMATCD = new TxtLimit(10);
			txtMATCD.addFocusListener(this);
			txtMATCD.addKeyListener(this);
			
			tblSANDT.setCellEditor(TBL_MATCD,txtMATCD);
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_teSANTBLINVFR();
			tblSANDT.setInputVerifier(objTBLVRF);		
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
			
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == btnPRNT)
		{
			try
			{
				if(txtSANNO.getText().trim().length() ==8)
					strSANNO = txtSANNO.getText().trim();
				if(strSANNO.length() ==0)
				{
					setMSG("SAN number is not specified..",'E');
					return;
				}
				mm_rpsan objSANRP  = new mm_rpsan(M_strSBSCD);
				objSANRP.getDATA(strSANNO,strSANNO,0);
				JComboBox L_cmbLOCAL = objSANRP.getPRNLS();
				objSANRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpsan.doc",L_cmbLOCAL.getSelectedIndex());
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"btnPRINT");
			}
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtSANNO.setEnabled(true);
				txtPREBY.setText(cl_dat.M_strUSRCD_pbst);
				txtSANNO.requestFocus();
			}
			else 
			{
				txtSANNO.setEnabled(false);
				txtSANDT.setText(cl_dat.M_strLOGDT_pbst);
			}
			
		}
		if(M_objSOURC == txtSANNO)
		{
			String L_strSANNO = txtSANNO.getText().trim();
			clrCOMP();
			txtSANNO.setText(L_strSANNO);
			getDATA(M_strSBSCD.substring(2,4),L_strSANNO);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int[] L_inaCOLSZ = new int[]{100,100,380};
		if(L_KE.getKeyCode() == L_KE.VK_F2)
		{
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCDF2";
			 	String L_ARRHDR[] = {"Item Code","UOM","Description"};
			  	cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				M_strSQLQRY = "Select CT_MATCD,CT_UOMCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and cT_MATCD like '"+txtMATCD.getText().trim() +"%'";
			
				M_strSQLQRY += " Order by ct_matds ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,3,"CT",L_inaCOLSZ);
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				String L_ARRHDR[] = {"Item Code","UOM","Description"};
				/*M_strSQLQRY = "Select ST_MATCD,ST_UOMCD,ST_MATDS from MM_STMST ";
				M_strSQLQRY += " where ST_STRTP ='"+M_strSBSCD.substring(2,4)+"' and isnull(ST_STSFL,'') <>'X'";*/
				M_strSQLQRY = "Select CT_MATCD,CT_UOMCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'";
				
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '"+txtMATCD.getText().trim() +"%'";
							
				M_strSQLQRY += " Order by Ct_matcd ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,3,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtSANNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtSANNO";
				String L_ARRHDR[] = {"SAN Number","San Date"};
				M_strSQLQRY = "Select distinct SA_SANNO,SA_SANDT from MM_SAMST ";
				M_strSQLQRY += " where isnull(SA_STSFL,'') <>'X'";
				if(txtSANNO.getText().trim().length() >0)
						M_strSQLQRY += " and SA_SANNO like '" + txtSANNO.getText().trim() + "%'";
				
				M_strSQLQRY += " and SA_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY +=" Order by SA_SANNO DESC ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtRECBY)
			{
				txtRECBY.transferFocus();
				txtRECBY.setText(txtRECBY.getText().toUpperCase());
			}
			if(M_objSOURC == txtAPRBY)
			{
				txtAPRBY.setText(txtAPRBY.getText().toUpperCase());
				tblSANDT.setRowSelectionInterval(tblSANDT.getSelectedRow(),tblSANDT.getSelectedRow());		
				tblSANDT.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);		
				tblSANDT.editCellAt(tblSANDT.getSelectedRow(),TBL_MATCD);
				tblSANDT.cmpEDITR[TBL_MATCD].requestFocus();
				
			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtSANNO"))
			{
				txtSANNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if((M_strHLPFLD.equals("txtMATCD"))||(M_strHLPFLD.equals("txtMATCDF2")))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				String L_strMATCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim();
				txtMATCD.setText(L_strMATCD);
				if(tblSANDT.isEditing())
					tblSANDT.getCellEditor().stopCellEditing();
				tblSANDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblSANDT.getSelectedRow(),TBL_UOMCD);
				tblSANDT.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblSANDT.getSelectedRow(),TBL_MATDS);
				//tblSANDT.setValueAt(L_strMATCD,tblSANDT.getSelectedRow(),TBL_SANQT);
				tblSANDT.cmpEDITR[TBL_SANQT].requestFocus();
				
				if(objTBLVRF.verify(tblSANDT.getSelectedRow(),TBL_MATCD))
				{
					tblSANDT.editCellAt(tblSANDT.getSelectedRow(),TBL_MATCD);
					tblSANDT.cmpEDITR[TBL_MATCD].requestFocus();
				}
				
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	private boolean getDATA(String P_strSTRTP,String P_strSANNO)
	{
		boolean L_FIRST = true;
		java.sql.Date L_datTMPDT;
		String L_strDPTCD="";
		try
		{
			String L_TPRCD,L_CHLQT,L_RECQT;
			String L_strMATCD ="",L_strINDNO ="",L_strSTSFL="",L_strORGCD="",L_strRCNVL ="";
		//	double L_REJQT;
			int i = 0;
			
			setCursor(cl_dat.M_curWTSTS_pbst);
			M_strSQLQRY = "Select SA_MMSBS,SA_STRTP,SA_SANNO,SA_SANDT,SA_MATCD,ST_MATDS,ST_UOMCD,";
			M_strSQLQRY +=" SA_SANQT,SA_SANRT,SA_RECBY,SA_PREBY,SA_APRBY,SA_LUSBY,SA_LUPDT from MM_SAMST,MM_STMST";
			M_strSQLQRY += " where SA_STRTP = ST_STRTP AND SA_MATCD = ST_MATCD AND SA_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_strSQLQRY += " and SA_SANNO = '" + P_strSANNO + "'";
			M_strSQLQRY += " and isnull(SA_STSFL,'') <> 'X' order by sa_sanqt";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(hstITMDT !=null)
				hstITMDT.clear();
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				if(L_FIRST){
					txtSANNO.setText(P_strSANNO);
					L_datTMPDT = M_rstRSSET.getDate("SA_SANDT");
					if(L_datTMPDT !=null)
					{
						txtSANDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						strSANDT = txtSANDT.getText().trim();
					}
					else
						txtSANDT.setText("");
				}
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("SA_MATCD"),"");
				txtPREBY.setText(nvlSTRVL(M_rstRSSET.getString("SA_PREBY"),""));
				txtRECBY.setText(nvlSTRVL(M_rstRSSET.getString("SA_RECBY"),""));
				txtAPRBY.setText(nvlSTRVL(M_rstRSSET.getString("SA_APRBY"),""));
				tblSANDT.setValueAt(L_strMATCD,i,TBL_MATCD);
				hstITMDT.put(L_strMATCD,"");
				tblSANDT.setValueAt(M_rstRSSET.getString("ST_MATDS"),i,TBL_MATDS);
				tblSANDT.setValueAt(M_rstRSSET.getString("ST_UOMCD"),i,TBL_UOMCD);
				tblSANDT.setValueAt(M_rstRSSET.getString("SA_SANQT"),i,TBL_SANQT);
				tblSANDT.setValueAt(M_rstRSSET.getString("SA_SANRT"),i,TBL_SANRT);
				//getITMDT(L_strMATCD,L_strORGCD,i);
				i++;
			}
				
			if(M_rstRSSET != null)
				M_rstRSSET.close();

			M_strSQLQRY = "Select RM_REMDS,RM_STSFL from MM_RMMST ";
			M_strSQLQRY += " where RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
			M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
	//		M_strSQLQRY += " and RM_DOCTP = '" + LP_ISSTP + "'";
			M_strSQLQRY += " and RM_DOCNO = '" + txtSANNO.getText().trim() + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(!nvlSTRVL(M_rstRSSET.getString("RM_STSFL"),"").equals("X"))
						txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
					else
						txtREMDS.setText("");
				}
				M_rstRSSET.close();
			}
		}catch(Exception e){
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(e,"getDATA");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
		return !L_FIRST;
	}
	boolean vldDATA()
	{
		try
		{
    		if(txtSANDT.getText().trim().length() ==0)
    		{
    			setMSG("SAN Date can not be blank..",'E');
    			return false;
    		}
    		if(txtRECBY.getText().trim().length() ==0)
    		{
    			setMSG("Enter the initial of Recommending autority..",'E');
    			return false;
    		}
    		if(txtAPRBY.getText().trim().length() ==0)
    		{
    			setMSG("Approved by can not be blank..",'E');
    			return false;
    		}
    		if(tblSANDT.isEditing())
    		{
    			if(tblSANDT.getValueAt(tblSANDT.getSelectedRow(),tblSANDT.getSelectedColumn()).toString().length()>0)
    			{
    				mm_teSANTBLINVFR obj=new mm_teSANTBLINVFR();
    				obj.setSource(tblSANDT);
    				if(obj.verify(tblSANDT.getSelectedRow(),tblSANDT.getSelectedColumn()))
    					tblSANDT.getCellEditor().stopCellEditing();
    				else
    					return false;
    			}
    		}
    		for(int i=0;i<tblSANDT.getRowCount();i++)
    		{
    			if(tblSANDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
    			{
    				strTEMP = nvlSTRVL(tblSANDT.getValueAt(i,TBL_MATCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("item Code can not be blank..",'E');
    					return false;
    				}
    				strTEMP = tblSANDT.getValueAt(i,TBL_SANQT).toString();
    				if(strTEMP.length() == 0)
    				{
    					setMSG("SAN Qty. can not be blank..",'E');
    					return false;
    				}
    				if(Double.parseDouble(tblSANDT.getValueAt(i,TBL_SANQT).toString())==0)
    				{
    					setMSG("Qty. can not be zero..",'E');
    					return false;
    				}
    				strTEMP = tblSANDT.getValueAt(i,TBL_SANRT).toString();
    				if(strTEMP.length() == 0)
    				{
    					setMSG("SAN Rate can not be blank..",'E');
    					return false;
    				}
    				if(Double.parseDouble(strTEMP)==0)
    				{
    					setMSG("Rate can not be zero..",'E');
    					return false;
    				}
    				// modified on 02/06/2005, B'COZ OF descripancies in precessing due to this. (GRIN - SAN value diff)  
    				// check for bill passing , if bill passing is pending for any GRIN , SAN is blocked.
    				/*  san check with bill passing removed on 19/07./2005
    				if(Double.parseDouble(tblSANDT.getValueAt(i,TBL_SANQT).toString())<0) // -ve qty san
    				{
    					if(!M_strSBSCD.substring(2,4).equals(strRVMST_fn))
    					{
        					strTEMP = nvlSTRVL(tblSANDT.getValueAt(i,TBL_MATCD).toString(),"");
        					M_strSQLQRY = "Select count(*)L_CNT from MM_GRMST WHERE GR_STSFL ='2' and GR_MATCD ='"+strTEMP +"'";
        					M_strSQLQRY +="AND GR_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND isnull(GR_BILVL,0) =0 AND GR_ACPDT >='07/01/2004'";
        					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
        					if(M_rstRSSET !=null)
        					{
        					    if(M_rstRSSET.next())
        					    if(M_rstRSSET.getInt("L_CNT") >0)
        					    {
        					        M_rstRSSET.close();
        					        setMSG("Bill passing is pending for item "+strTEMP +" SAN generation not allowed..",'E'); 
        					        return false;
        					    }    
        					    M_rstRSSET.close();
        					}
    					}
    				}
*/
///
    			}
    		}
          }
    	  catch(Exception L_E)
    	  {
    	    setMSG(L_E,"vldDATA");    
    	    return false;
    	  }
		return true;													 
														 
	}
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
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				strSANDT = cl_dat.M_strLOGDT_pbst;
				strSANNO = genSANNO();
				if(strSANNO !=null)
				{
					for(int i=0;i<tblSANDT.getRowCount();i++)
					{
						if(tblSANDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							double L_SANVL = Double.parseDouble(tblSANDT.getValueAt(i,TBL_SANRT).toString())*Double.parseDouble(tblSANDT.getValueAt(i,TBL_SANQT).toString());
							String L_strMATCD = tblSANDT.getValueAt(i,TBL_MATCD).toString();
							M_strSQLQRY = "Insert into MM_SAMST(SA_MMSBS,SA_STRTP,SA_SANNO,SA_SANDT,SA_MATCD,";
							M_strSQLQRY += "SA_SANQT,SA_SANRT,SA_SANVL,SA_PREBY,SA_RECBY,SA_APRBY,SA_TRNFL,SA_STSFL,SA_LUSBY,SA_LUPDT) values (";
							M_strSQLQRY += "'"+ M_strSBSCD + "',";;
							M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
							M_strSQLQRY += "'" + txtSANNO.getText().trim() + "',";
							M_strSQLQRY += "'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(strSANDT))+"',";
						//	M_strSQLQRY += "'" +L_strMATCD +"',";
							M_strSQLQRY += "'"+ tblSANDT.getValueAt(i,TBL_MATCD).toString() +"',";
							M_strSQLQRY +=  tblSANDT.getValueAt(i,TBL_SANQT).toString() +",";
							M_strSQLQRY +=  tblSANDT.getValueAt(i,TBL_SANRT).toString() +",";
							M_strSQLQRY +=  L_SANVL +",";
							M_strSQLQRY += "'" + txtPREBY.getText().trim() + "',";
							M_strSQLQRY += "'" + txtRECBY.getText().trim() + "',";
							M_strSQLQRY += "'" + txtAPRBY.getText().trim() + "',";
						//	M_strSQLQRY +=  L_SANVL +",";
							M_strSQLQRY += getUSGDTL("SA",'I',"0")+")";
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
						}
					}		
					exeSANNO(strSANNO);
				}
				
		}
		if(cl_dat.M_flgLCUPD_pbst)
		{
			strREMDS = txtREMDS.getText().trim();
			if(strREMDS.length() > 0)
			{								// To add new record
				M_strSQLQRY = "Insert into MM_RMMST(RM_STRTP,RM_TRNTP,";
				M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,";
				M_strSQLQRY += "RM_LUPDT) values (";
				M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
				M_strSQLQRY += "'" + strTRNTP_fn + "',";
				M_strSQLQRY += "'" + strTRNTP_fn + "',";
				M_strSQLQRY += "'" + txtSANNO.getText().trim() + "',";
				M_strSQLQRY += "'" + strREMDS + "',";
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
						JOptionPane.showMessageDialog(this,"Please, Note down the SAN No. " + strSANNO,"SAN No.",JOptionPane.INFORMATION_MESSAGE);
						strSANNO = txtSANNO.getText().trim();
						btnPRNT.setEnabled(true);
						clrCOMP();		
						txtSANDT.setText(cl_dat.M_strLOGDT_pbst);
						txtPREBY.setText(cl_dat.M_strUSRCD_pbst);
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
				}
				else
				{
					setMSG("Error in saving data..",'E');
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
void setENBL(boolean L_flgSTAT)
{
	super.setENBL(L_flgSTAT);
	if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		txtSANNO.setEnabled(true);
	else 
	{
		txtSANNO.setEnabled(false);
		txtPREBY.setText(cl_dat.M_strUSRCD_pbst);
		txtRECBY.requestFocus();
	}
	btnPRNT.setEnabled(false);
	txtPREBY.setEnabled(false);
	txtSANDT.setEnabled(false);
	tblSANDT.cmpEDITR[TBL_MATDS].setEnabled(false);
	tblSANDT.cmpEDITR[TBL_UOMCD].setEnabled(false);
	tblSANDT.cmpEDITR[TBL_STKQT].setEnabled(false);
	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		btnPRNT.setEnabled(true);
}
private String genSANNO()
{
	strSANNO ="";
		String L_SANNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for SAN;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"SAN' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn +"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP01.trim().length() >0)
					{
						M_rstRSSET.close();
						return null;
					}
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"SAN'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + "'";			
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY," ");
			if(cl_dat.exeDBCMT("genSANNO"))
		   {
				L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
				for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
					L_SANNO += "0";
			
				L_CCSVL = L_SANNO + L_CCSVL;
				L_SANNO = L_CODCD + L_CCSVL;
				txtSANNO.setText(L_SANNO);
				strSANNO = L_SANNO;
			}
			else return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genSANNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_SANNO;
	}
	// Method to update the last Issue No.in the CO_CDTRN
	private void exeSANNO(String P_strSANNO)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + P_strSANNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP ='MM"+M_strSBSCD.substring(2,4)+"SAN'";
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn +"'";			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeISSNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
private class mm_teSANTBLINVFR extends TableInputVerifier
{
	
	public boolean verify(int P_intROWID,int P_intCOLID)
		{
			String L_strTEMP ="";
			try
			{
				if(P_intCOLID==TBL_MATCD)
				{
					strTEMP = tblSANDT.getValueAt(P_intROWID,TBL_MATCD).toString();
		    		if(strTEMP.length()>0)
					{
						for(int i=0;i<=P_intROWID-1;i++)
						{
							if(tblSANDT.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
							if(tblSANDT.getValueAt(i,TBL_MATCD).toString().trim().equals(strTEMP.trim()))
							{
								setMSG("Duplicate entry ..",'E');
								return false;
							}
						}
						if(!vldMATCD(strTEMP,P_intROWID))
						{
							return false;
						}
					}
				}
				if(P_intCOLID==TBL_SANQT)
				{
					strTEMP = tblSANDT.getValueAt(P_intROWID,TBL_SANQT).toString();
		    		if(strTEMP.length()>0)
					{
						if(Double.parseDouble(strTEMP) <=0)
						{
							if(Double.parseDouble(tblSANDT.getValueAt(P_intROWID,TBL_STKQT).toString()) < Math.abs(Double.parseDouble(strTEMP)))
						    {
								setMSG("Adj. qty. can not be greater than stock qty..",'E');
								return false;
							}
						}
					}
				}
				
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				return false;
			}
			return true;
		}
		
}
private boolean vldMATCD(String P_strMATCD,int P_intROWID)
{
	try
	{
		M_strSQLQRY = "select CT_MATDS,CT_UOMCD from CO_CTMST where";
		M_strSQLQRY += " CT_MATCD = '" + P_strMATCD + "'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
		if(M_rstRSSET !=null)
		if(M_rstRSSET.next())
		{
			tblSANDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),P_intROWID,TBL_UOMCD);
			tblSANDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),P_intROWID,TBL_MATDS);
			setMSG("",'N');
			M_rstRSSET.close();			
			getWAVRT(P_strMATCD,P_intROWID);
			return true;
		}
		
		setMSG("Invalid Material Code.Press F1 for help",'E');
		if(M_rstRSSET != null)
			M_rstRSSET.close();
	}catch(Exception e){
		setMSG(e,"vldMATCD");
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
		return false;
	}
private void getWAVRT(String P_strMATCD,int P_intROWID)
{
	try
	{
		//M_strSQLQRY = "select ST_MATDS,ST_UOMCD,ST_STKQT,STP_WAVRT from MM_STMST,MM_STPRC where";
		M_strSQLQRY = "select ST_MATDS,ST_UOMCD,ST_STKQT,STP_YCLRT from MM_STMST,MM_STPRC where";
		M_strSQLQRY += " ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND ST_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
		M_strSQLQRY += " AND ST_MATCD = '" + P_strMATCD + "'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
		if(M_rstRSSET !=null)
		if(M_rstRSSET.next())
		{
			tblSANDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("STP_YCLRT"),"0"),P_intROWID,TBL_SANRT);
			tblSANDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"),P_intROWID,TBL_STKQT);
			setMSG("",'N');
			M_rstRSSET.close();			
		}
		if(M_rstRSSET != null)
			M_rstRSSET.close();
	}catch(Exception e){
		setMSG(e,"vldMATCD");
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
}
}