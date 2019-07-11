/*
System Name   : Production System
Program Name  : Production Request Query (Date Wise)
Program Desc. :
Author        : 
Date          : 18 Dec 2008
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
/*import java.sql.ResultSet;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTable;*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;

public class pr_qrprq extends cl_pbase
{
	
	private JTextField txtFMDAT;			
	private JTextField txtTODAT;
	
	private  cl_JTable tblPRQDL;
	
    int TB1_CHKFL = 0; 				JCheckBox chkCHKFL;
    int TB1_DOCNO = 1;				JTextField txtDOCNO;
    int TB1_DOCDT = 2;				JTextField txtDOCDT;
    int TB1_REQDT = 3;				JTextField txtREQDT;
    int TB1_LOTNO = 4;			    JTextField txtLOTNO; 
    int TB1_PRDCD = 5;				JTextField txtPRDCD;
    int TB1_REQQT = 6;				JTextField txtREQQT;
    int TB1_AUTDT = 7;				JTextField txtAUTDT;
    int TB1_FSBFL = 8;				JTextField txtFSBFL;
    int TB1_STATS = 9;				JTextField txtSTATS;
    
	pr_qrprq()
	{
		super(1);
		setMatrix(20,20);		
		try
		{
			add(new JLabel("From Date"),3,7,1,2,this,'L');
			add(txtFMDAT = new TxtDate(),3,9,1,2,this,'L');
						
			add(new JLabel("To Date"),3,11,1,2,this,'L');
			add(txtTODAT = new TxtDate(),3,13,1,2,this,'L');
			
			txtFMDAT.addKeyListener(this);
			txtTODAT.addKeyListener(this);
			
			String[] L_strTBLHD = {"","PR No","PR Date","Despatch Date"," Lot No","Grade","Quantity","Auth Date","Feasibility","Status"};
    		int[] L_intCOLSZ = {10,75,75,85,85,75,75,70,60,145};
    		tblPRQDL= crtTBLPNL1(this,L_strTBLHD,100,5,3,10,16,L_intCOLSZ,new int[]{0});
    	
    		tblPRQDL.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
    		tblPRQDL.setCellEditor(TB1_DOCNO,txtDOCNO=new TxtLimit(8));
    		tblPRQDL.setCellEditor(TB1_DOCDT,txtDOCDT=new TxtDate());
    		tblPRQDL.setCellEditor(TB1_REQDT,txtREQDT = new TxtDate());
    		tblPRQDL.setCellEditor(TB1_LOTNO,txtLOTNO = new TxtLimit(8)); 
    		tblPRQDL.setCellEditor(TB1_PRDCD,txtPRDCD=new TxtLimit(10));
    		tblPRQDL.setCellEditor(TB1_REQQT,txtREQQT = new TxtNumLimit(12.3));
    		tblPRQDL.setCellEditor(TB1_AUTDT,txtAUTDT = new TxtDate());
    		tblPRQDL.setCellEditor(TB1_FSBFL,txtFSBFL = new TxtLimit(1));
    		tblPRQDL.setCellEditor(TB1_STATS,txtSTATS = new TxtLimit(20));
    		
    		setENBL(false); 
    		setVisible(true);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
   void setENBL(boolean P_flgENBFL)
   {
		super.setENBL(P_flgENBFL);
   }
   public void actionPerformed(ActionEvent L_AE)
   {
       super.actionPerformed(L_AE);
       if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				setENBL(true);
				txtFMDAT.requestFocus();
				tblPRQDL.setEnabled(false);
				if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
				{
					txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
					txtFMDAT.setText("01"+cl_dat.M_strLOGDT_pbst.substring(2));
					setMSG("Please enter date to specify date range to generate Data..",'N');
				}
			}
			else
				setENBL(false);
		}
   }
   public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed(L_KE);
	    try
	    {
	    	if (L_KE.getKeyCode()== L_KE.VK_F1)
	    	{
			
	    	}
	    	else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtFMDAT)
				 {
					txtTODAT.requestFocus();
					setMSG("Enter To Date..",'N');
				 }
				else if(M_objSOURC == txtTODAT)
				{
					getDATA();
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
	    }
		catch(NullPointerException L_NPE)
		{
			 setMSG("Help not available",'N');                            
		}
		  
	}
   /**this method set focus in txtFMDAT & txtTODAT & set Message for enter Date**/
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == txtFMDAT)			
			setMSG("Enter From Date..",'N');			
		else if(M_objSOURC == txtTODAT)			
			setMSG("Enter To Date..",'N');		
	}	
   
   public void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
    }

	
	void getDATA()
	{
	    try
	    {
	    	int L_CNT=0;
	    	String L_strPRDCD="";
			tblPRQDL.clrTABLE();
			M_strSQLQRY= "SELECT RQ_DOCNO,RQ_DOCDT,RQ_REQDT,RQ_PRDCD,RQ_REQQT,RQ_AUTDT,RQ_FSBFL,CMT_CODDS from PR_RQTRN,CO_CDTRN";
			M_strSQLQRY+= " where RQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'AND RQ_STSFL<>'X'";
			M_strSQLQRY += " AND CMT_CGMTP='STS' AND CMT_CGSTP='PRXXREQ' AND CMT_CODCD=RQ_STSFL";
			
			if(txtFMDAT.getText().trim().length()>0 && txtTODAT.getText().trim().length()>0)
			{
				M_strSQLQRY +=" AND CONVERT(varchar,RQ_DOCDT,101) BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim())) +"' ";
			}
			M_strSQLQRY += " order by RQ_DOCNO";
			System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while( M_rstRSSET.next())
				{
					 L_strPRDCD=nvlSTRVL(M_rstRSSET.getString("RQ_PRDCD"),"");
					
					tblPRQDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("RQ_DOCNO"),""),L_CNT,TB1_DOCNO);
					if(!(M_rstRSSET.getDate("RQ_DOCDT")==null))
						tblPRQDL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("RQ_DOCDT")),L_CNT,TB1_DOCDT);
					else
						tblPRQDL.setValueAt("",L_CNT,TB1_DOCDT);
					
					if(!(M_rstRSSET.getDate("RQ_REQDT")==null))
						tblPRQDL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("RQ_REQDT")),L_CNT,TB1_REQDT);
					else
						tblPRQDL.setValueAt("",L_CNT,TB1_REQDT);
					//tblPRQDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("RQ_PRDCD"),""),L_CNT,TB1_PRDCD);
					tblPRQDL.setValueAt(L_strPRDCD,L_CNT,TB1_PRDCD);
					tblPRQDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("RQ_REQQT"),""),L_CNT,TB1_REQQT);
					
					if(!(M_rstRSSET.getDate("RQ_AUTDT")==null))
						tblPRQDL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("RQ_AUTDT")),L_CNT,TB1_AUTDT);
					else
				 		tblPRQDL.setValueAt("",L_CNT,TB1_AUTDT);
					
					tblPRQDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("RQ_FSBFL"),""),L_CNT,TB1_FSBFL);
					tblPRQDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),L_CNT,TB1_STATS);
					L_CNT++;
				}
			/*	String L_strSQLQRY= "SELECT ST_LOTNO from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
				L_strSQLQRY += " AND ST_PRDTP='"+M_strSBSCD.substring(2,4)+"' AND ST_PRDCD='"+L_strPRDCD+"'";
		    	System.out.println(">>>select>>"+ L_strSQLQRY );
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY); 
				if(L_rstRSSET != null)
				{
					while(L_rstRSSET.next())
					{
						tblPRQDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("ST_LOTNO"),""),L_CNT,TB1_LOTNO);
						L_CNT++;
					}
				}*/
			}
			else 
				setMSG("No Data Found..",'E');
			//M_rstRSSET.close();
            
   	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getDATA");
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
			System.out.println("exePRINT");
			tblPRQDL.clrTABLE();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				getDATA();
				
		}
		catch(Exception L_E)
	    {
	        setMSG(L_E,"exePRINT()");
	    }
	}	
	boolean vldDATA()
	{
		try
		{
			if(txtTODAT.getText().length()==0)
			{
				if(txtFMDAT.getText().length()>0)
				{
					txtFMDAT.setEnabled(true);
					setMSG("Enter To Date",'E');
					txtFMDAT.requestFocus();
					return false;
				}	
			}	
		}
		catch(Exception L_E)
	    {
	        setMSG(L_E,"vldDATA()");
	    }
		return true;
	}

}
