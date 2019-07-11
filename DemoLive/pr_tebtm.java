/*
System Name   : Labortory Information Management System
Program Name  : Lot Master Enter (Batch Wise)
Program Desc. :
Author        : N.K.Virdi
Date          : 09 October 2006
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;
public class pr_tebtm extends cl_pbase
{
	private JTextField txtBATNO;
	private JTextField txtRCTNO;
	private JTextField txtLINNO;
	private JTextField txtBSTDT;
	private JTextField txtBSTTM;
	private JTextField txtBENDT;
	private JTextField txtBENTM;
	private JTextField txtREMDS;
	private JTextField txtPRDQT;
	private JTextField txtSAMQT;
	private JTextField txtTOTQT;
	private JTextField txtYLDFT;
	private JTextField txtMATCD;
	private JTextField txtPRDCD;
	private JTextField txtISSNO;
	private cl_JTable tblBTDTL;
	private cl_JTable tblRMDTL;
	private JTabbedPane jtpMAIN; 
	private JPanel pnlLTDTL;
	private JPanel pnlRMDTL;
	
	private String strPRDTP ="";
	private String strYRDGT ="7";
	private String strPRDGT ="3";
	private String strLOTNO ="";
	private String strBATNO ="";
	private String strDFRCL ="00";
	
	private final int TBL_CHKFL =0;
	private final int TBL_PRDCD =1;
	private final int TBL_PRDDS =2;
	private final int TBL_SRLNO =3;
	private final int TBL_SAMQT =4;
	private final int TBL_PRDQT =5;	
    private final int TBL_BAGQT =6;
    private final int TBL_SEQNO =7;
    
    private final int TB2_CHKFL =0;
	private final int TB2_MATCD =1;
	private final int TB2_UOMCD =2;
	private final int TB2_MATDS =3;
	private final int TB2_ISSQT =4;
    private final int TB2_STRTP =5;
	private final int TB2_ISSNO =6;
	private final int TB2_SEQNO =7;
		
	private String[] L_arrHEADR = new String[2];
	private String[] staHEADR = new String[2];
	private JOptionPane jopOPTPN = new JOptionPane();	/** Input varifier for master data validity Check.*/
	//private INPVF objINPVR = new INPVF();
	private TBLINVFR objTBLVRF;
	private boolean flgSLVLD =false;
	private int intOLDRW =0;
	private int intOLTRW =0;
	private ButtonGroup btgBATTP;    
	private JRadioButton rdbCLBAT;	 
	private JRadioButton rdbSEBAT;	
	pr_tebtm()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(rdbCLBAT=new JRadioButton("CL Batch",true),1,2,1,1,this,'L');
			add(rdbSEBAT=new JRadioButton("SE Batch"),1,3,1,2,this,'L');
			btgBATTP=new ButtonGroup();	
			btgBATTP.add(rdbCLBAT);
			btgBATTP.add(rdbSEBAT);
				
			add(new JLabel("Batch No"),2,2,1,1,this,'L');
			add(txtBATNO = new TxtLimit(8),2,3,1,1,this,'L');
		
			add(new JLabel("Line No."),2,4,1,1,this,'L');
			add(txtLINNO = new TxtNumLimit(2),2,5,1,1,this,'L');
			add(new JLabel("Reactor No."),2,6,1,1,this,'L');
			add(txtRCTNO = new TxtNumLimit(6),2,7,1,1,this,'L');
		
			add(new JLabel("Start Date"),3,2,1,1,this,'L');			
			add(txtBSTDT = new TxtDate(),3,3,1,1,this,'L');
			add(new JLabel("Time"),3,4,1,1,this,'L');			
			add(txtBSTTM = new TxtTime(),3,5,1,1,this,'L');
	    	add(new JLabel("Total Qty.(Kg.)"),3,6,1,1,this,'L');
			add(txtTOTQT = new TxtNumLimit(6.3),3,7,1,1,this,'L');
			
		    add(new JLabel("End Date"),4,2,1,1,this,'L');			
			add(txtBENDT = new TxtDate(),4,3,1,1,this,'L');
			add(new JLabel("Time"),4,4,1,1,this,'L');			
			add(txtBENTM = new TxtTime(),4,5,1,1,this,'L');
			add(new JLabel("Yield Factor"),4,6,1,1,this,'L');
			add(txtYLDFT = new TxtNumLimit(6.0),4,7,1,1,this,'L');
			add(new JLabel("Remarks"),17,2,1,1,this,'L');
			add(txtREMDS = new JTextField(),17,3,1,5.66,this,'L');
		    pnlLTDTL = new JPanel(null);
		    pnlRMDTL = new JPanel(null);
		    jtpMAIN=new JTabbedPane();
			//jtpMAIN.addMouseListener(this);
		//	setMatrix(15,8);
		//	setVGAP(12);
			String[] L_COLHD = {"Select","Prd Code","Grade","Serial No.","one min sample","Batch Qty(MT)","Bag Qty(MT)","Seq No"};
      		int[] L_COLSZ = {30,100,120,120,100,100,100,50};	    				
			tblBTDTL = crtTBLPNL1(pnlLTDTL,L_COLHD,15,1,1,8,7,L_COLSZ,new int[]{0});			
			add(jtpMAIN,6,2,9,7,this,'L');
			String[] L_COLHD1 = {"Select","Item Code","UOM","Description","Quantity","Str.Tp","Issue No","Seq No."};
      		int[] L_COLSZ1 = {30,100,50,200,100,30,100,50};	    				
			tblRMDTL = crtTBLPNL1(pnlRMDTL,L_COLHD1,25,1,1,8,7,L_COLSZ1,new int[]{0});			
			jtpMAIN.add(pnlLTDTL,"Lot Details");
			jtpMAIN.add(pnlRMDTL,"R.M. Details");
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);									
		//	txtBSTDT.setInputVerifier(objINPVR);
		//	txtBENTM.setInputVerifier(objINPVR);
		    txtMATCD = new TxtLimit(10);
    		txtMATCD.addFocusListener(this);
    		txtMATCD.addKeyListener(this);
    		txtPRDCD = new TxtLimit(10);
    		txtPRDCD.addFocusListener(this);
    		txtPRDCD.addKeyListener(this);
    		txtISSNO = new TxtLimit(8);
    		txtISSNO.addFocusListener(this);
    		txtISSNO.addKeyListener(this);
		    txtPRDQT = new TxtNumLimit(10.3);
		    txtSAMQT = new TxtNumLimit(10.3);		
		    tblBTDTL.setCellEditor(TBL_PRDCD,txtPRDCD);
            tblBTDTL.setCellEditor(TBL_SAMQT,txtSAMQT);
            tblBTDTL.setCellEditor(TBL_PRDQT,txtPRDQT);
            tblRMDTL.setCellEditor(TB2_MATCD,txtMATCD);
        	setENBL(false);
        
		    objTBLVRF = new TBLINVFR();
		    tblBTDTL.setInputVerifier(objTBLVRF);	

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
   void setENBL(boolean P_flgENBFL)
   {
		super.setENBL(P_flgENBFL);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
		{
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		        txtBATNO.setEnabled(false);
		    else
		        txtBATNO.setEnabled(true);
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		    {    
		       // tblBTDTL.setEnabled(false);
		        tblRMDTL.setEnabled(true);
		        tblBTDTL.cmpEDITR[TBL_PRDCD].setEnabled(true);
		        
		    }
		    //tblBTDTL.cmpEDITR[TBL_CHKFL].setEnabled(false);
		  //  tblBTDTL.cmpEDITR[TBL_PRDCD].setEnabled(false);
		    tblBTDTL.cmpEDITR[TBL_PRDDS].setEnabled(false);
		    tblBTDTL.cmpEDITR[TBL_SRLNO].setEnabled(false);
		    tblBTDTL.cmpEDITR[TBL_BAGQT].setEnabled(false);
		    tblRMDTL.cmpEDITR[TB2_UOMCD].setEnabled(false);
		    tblRMDTL.cmpEDITR[TB2_MATDS].setEnabled(false);
		}
   }
   private void getGRDDT()
   {
        try
        {
            String L_strPRBAT = "00000000";//String.valueOf(Integer.parseInt(txtBATNO.getText().trim()) - 1) ;
            if(txtLINNO.getText().length() ==0)
            {
                setMSG("Enter Reactor No. or press F1 for Help ..",'E');
                txtLINNO.requestFocus();
            }
          	String L_strLOTNO = "";
          	String L_strBATNO = "";
        	int L_intSRLNO ;
        	int L_intBATNO ;
		    // get PRV LOT
            M_strSQLQRY ="Select (CMT_CODCD + CMT_CCSVL) L_LOTNO,CMT_CHP02,CMT_CCSVL from CO_CDTRN WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cGSTP ='PRXXSRL'";
			M_strSQLQRY +=" AND CMT_CODCD = '"+strYRDGT+txtLINNO.getText().trim() +"'";
		//	System.out.println(M_strSQLQRY);
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
				    L_strLOTNO = M_rstRSSET.getString("CMT_CCSVL");
				    strLOTNO = L_strLOTNO;
				    for(int i=0;i<(5 - strLOTNO.length());i++)
				    {
				        strLOTNO += "0";
				    }
				    strLOTNO = strYRDGT+txtLINNO.getText().trim()+strLOTNO;
				}
			}
		    tblBTDTL.clrTABLE();tblRMDTL.clrTABLE();
			
			// get Grade Details
            M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS,BT_SRLNO from PR_BTMST,CO_PRMST WHERE ltrim(str(BT_GRDCD,20,0)) = PR_PRDCD and ";
            if(rdbCLBAT.isSelected())
            {
                M_strSQLQRY += " SUBSTRING(PR_PRDCD,1,6) ='512101'";
            }
            else
            {
                M_strSQLQRY += " SUBSTRING(PR_PRDCD,1,6) ='512102'";
            }
            M_strSQLQRY += " AND BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO  ='" + L_strPRBAT +"' ORDER BY BT_SRLNO ";
    	    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int i=0;
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
				    tblBTDTL.setValueAt(new Boolean("true"),i,TBL_CHKFL);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("PR_PRDCD"),i,TBL_PRDCD);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("PR_PRDDS"),i,TBL_PRDDS);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("BT_SRLNO"),i,TBL_SEQNO);
			        L_intSRLNO = Integer.parseInt(strLOTNO) + 1; 
			        strLOTNO = String.valueOf(L_intSRLNO);
			        tblBTDTL.setValueAt(strLOTNO,i,TBL_SRLNO);
			        i++;
				}
			}
		//	System.out.println("1");
	
        	// get RM Details
	       // System.out.println("2");
            M_strSQLQRY = "Select LTR_MATCD,CT_MATCD,CT_UOMCD,CT_MATDS,ltr_srlno from CO_CTMST,PR_LTRDT WHERE CT_MATCD = LTR_MATCD AND CT_CODTP ='CD' ";//substr(CT_MATCD,1,6) ='680601' AND
    	    M_strSQLQRY += " AND LTR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LTR_BATNO  ='" + L_strPRBAT +"' order by ltr_srlno";
    	    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			i=0;
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
				    
			        tblRMDTL.setValueAt(M_rstRSSET.getString("CT_MATCD"),i,TB2_MATCD);
			        tblRMDTL.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,TB2_UOMCD);
			        tblRMDTL.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,TB2_MATDS);
			        tblRMDTL.setValueAt(M_rstRSSET.getString("ltr_srlno"),i,TB2_SEQNO);
			        i++;
				}
			}
			txtTOTQT.requestFocus();

	    }
        catch(Exception L_E)
        {
            setMSG(L_E,"getGRDDT");
        }
   }
   private void getPRVLT()
   {
        try
        {
            M_strSQLQRY ="Select (CMT_CODCD + CMT_CCSVL) L_LOTNO,CMT_CHP02 from CO_CDTRN WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cGSTP ='PRXXLOT' AND isnull(CMT_STSFL,'') <>'X' ";
			M_strSQLQRY +=" AND CMT_CODCD = '"+txtLINNO.getText().trim() + strPRDGT + strYRDGT +"'";
			
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strLOTNO = "";
				while(M_rstRSSET.next())
				{
				    L_strLOTNO = M_rstRSSET.getString("CMT_CCSVL");
				    strLOTNO = L_strLOTNO;
				    for(int i=0;i<(4 - strLOTNO.length());i++)
				    {
				        strLOTNO += "0";
				    }
				    strLOTNO += txtLINNO.getText().trim() + strPRDGT + strYRDGT;
			   	}
			}
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"getPRVLT");
        }
   }
private void getPRVBT()
   {
        try
        {
            M_strSQLQRY ="Select CMT_CCSVL from CO_CDTRN WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cGSTP ='PRXXBAT' AND isnull(CMT_STSFL,'') <>'X' ";
			M_strSQLQRY +=" AND CMT_CODCD = '"+txtLINNO.getText().trim() +strPRDGT+strYRDGT;
			if(rdbCLBAT.isSelected())
            {
                M_strSQLQRY += "01"+"'";;
            }
            else
            {
                M_strSQLQRY += "02"+"'";;
            }
			
			
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strBATNO = "";
				int L_intBATNO =0;
				if(M_rstRSSET.next())
				{
				    L_intBATNO = M_rstRSSET.getInt("CMT_CCSVL");
				    L_intBATNO ++;
				    strBATNO = String.valueOf(L_intBATNO);
				}
				txtBATNO.setText(strBATNO);
				M_rstRSSET.close();
			}
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"getPRVBT");
        }
   }
   private boolean vldLINNO()
	{
		strPRDTP = M_strSBSCD.substring(2,4);
		if(txtLINNO.getText().trim().length()==0)
		{
			setMSG("Line No can not be Empty ..Enter some valid Line No ", 'E');
			txtLINNO.requestFocus();
			return false;
		}
		/*else if(!txtLOTNO.getText().trim().substring(0,2).equals(txtLINNO.getText().trim()))
		{
			setMSG("Line no. is not correct as per the Lot series .. ", 'E');
			txtLINNO.requestFocus();
			return false;
		}*/
		M_strSQLQRY ="Select count(*) from CO_CDTRN where cmt_cgmtp ='SYS' and CMT_CGSTP ='PRXXLIN' and ";
		M_strSQLQRY +=" cmt_codcd ='"+txtLINNO.getText().trim()+"'";
		M_strSQLQRY +=" and CMT_CCSVL like '%" +strPRDTP +"%'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
		if(cl_dat.getRECCNT( M_strSQLQRY)>0)
		{
			return true;
		}
		else
		{
			setMSG("Invalid Line No...Enter some valid Line No ", 'E');
			return false;
			
		}
	}
	private boolean vldSILNO()
	{
		if(txtRCTNO.getText().trim().length()==0)
		{
			setMSG("Reactor No. can not be Empty ..Enter some valid Reactor No ", 'E');
			return false;
		}	
		M_strSQLQRY ="Select count(*) from CO_CDTRN where cmt_cgmtp ='SYS' and CMT_CGSTP ='PRXXCYL' and ";
		M_strSQLQRY +=" cmt_codcd ='"+txtRCTNO.getText().trim()+"' AND isnull(CMT_STSFL,'') <>'X'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
	//	M_strSQLQRY += " AND CMT_CODCD like '"+txtLINNO.getText().trim() + "%'";
		if(cl_dat.getRECCNT( M_strSQLQRY)>0)
			return true;
		else
		{
			setMSG("Invalid Reactor No ..Enter some valid Reactor No ", 'E');
			return false;
		}
	}
    private String getPSTDTM()
	{
		if((txtBSTDT.getText().length()>0) && (txtBSTTM.getText().length()>0) )
		{
			return (txtBSTDT.getText().trim()+" "+txtBSTTM.getText().trim());
		}
		else
			return "";
	}
	private String getPENDTM()
	{
		if((txtBENDT.getText().length()>0) && (txtBENTM.getText().length()>0) )
		{
			return (txtBENDT.getText().trim()+" "+txtBENTM.getText().trim());
		}
		else
			return "";
	}

   public void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
	    if(M_strHLPFLD.equals("txtLINNO"))
			txtLINNO.setText(cl_dat.M_strHLPSTR_pbst);
		if(M_strHLPFLD.equals("txtBATNO"))
			txtBATNO.setText(cl_dat.M_strHLPSTR_pbst);
		else if(M_strHLPFLD.equals("txtRCTNO"))
		{
			txtRCTNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtYLDFT.setText(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2).toString());
			//txtSILST.setText((cl_dat.M_strHELP_pbst.charAt(cl_dat.M_strHELP_pbst.trim().length()-1) == 'F' ? "Full" : "Empty"));
		}
		else if(M_strHLPFLD.equals("txtMATCD"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_STRTKN.nextToken();
			txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			tblRMDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblRMDTL.getSelectedRow(),TB2_UOMCD);
			tblRMDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblRMDTL.getSelectedRow(),TB2_MATDS);
		//	txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
		//	strMATCD = tblRMDTL.getValueAt(tblRMDTL.getSelectedRow(),TB2_MATCD).toString();
			/*if(objTBLVRF.verify(tblRMDTL.getSelectedRow(),TB2_MATCD))
			{
				tblRMDTL.setRowSelectionInterval(tblRMDTL.getSelectedRow(),tblRMDTL.getSelectedRow());		
				tblRMDTL.setColumnSelectionInterval(TB2_MATCD,TB2_MATCD);		
				tblRMDTL.editCellAt(tblRMDTL.getEditingRow(),TB2_MATCD);
				tblRMDTL.cmpEDITR[TB2_MATCD].requestFocus();
			}*/
			//tblRMDTL.setValueAt(L_STRTKN.nextToken(),tblRMDTL.getSelectedRow(),TBL_MATDS);
		}
		else if(M_strHLPFLD.equals("txtPRDCD"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_STRTKN.nextToken();
			txtPRDCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			tblBTDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblBTDTL.getSelectedRow(),TBL_PRDDS);
			int  L_intLOTNO = Integer.parseInt(strLOTNO) + 1; 
			strLOTNO = String.valueOf(L_intLOTNO);
			tblBTDTL.setValueAt(strLOTNO,tblBTDTL.getSelectedRow(),TBL_SRLNO);
		}
		else if(M_strHLPFLD.equals("txtISSNO"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_STRTKN.nextToken();
			txtISSNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			tblRMDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblRMDTL.getSelectedRow(),TB2_STRTP);
		
		//	txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
		//	strMATCD = tblRMDTL.getValueAt(tblRMDTL.getSelectedRow(),TB2_MATCD).toString();
			/*if(objTBLVRF.verify(tblRMDTL.getSelectedRow(),TB2_MATCD))
			{
				tblRMDTL.setRowSelectionInterval(tblRMDTL.getSelectedRow(),tblRMDTL.getSelectedRow());		
				tblRMDTL.setColumnSelectionInterval(TB2_MATCD,TB2_MATCD);		
				tblRMDTL.editCellAt(tblRMDTL.getEditingRow(),TB2_MATCD);
				tblRMDTL.cmpEDITR[TB2_MATCD].requestFocus();
			}*/
			//tblRMDTL.setValueAt(L_STRTKN.nextToken(),tblRMDTL.getSelectedRow(),TBL_MATDS);
		}
  
    }

	public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed(L_KE);
	    if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{
			    if(M_objSOURC == txtBATNO)
				{
				    setCursor(cl_dat.M_curWTSTS_pbst);
				    staHEADR[0] = "Batch No.";
					staHEADR[1] = "Production Date";	
					M_strSQLQRY = "SELECT distinct BT_BATNO,BT_RCTNO,BT_BATDT FROM PR_BTMST WHERE BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO <>'00000000' ORDER BY BT_BATNO DESC";//WHERE LT_RUNNO ='"+txtBATNO.getText().trim()+"'";
					M_strHLPFLD = "txtBATNO";
					cl_hlp(M_strSQLQRY ,1,1,staHEADR,2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtLINNO)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					strPRDTP = M_strSBSCD.substring(2,4);
					staHEADR[0] = "Line No.";
					staHEADR[1] = "Description";	
					M_strSQLQRY = "SELECT cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp =  ";
					M_strSQLQRY = M_strSQLQRY + "'SYS'" + " AND cmt_cgstp = ";
					M_strSQLQRY = M_strSQLQRY + "'PRXXLIN' and isnull(cmt_stsfl,'') <>'X'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
					if(strPRDTP.equals("01"))
					    M_strSQLQRY += " AND CMT_CCSVL = " + "'"+strPRDTP + "'";
					else
					    M_strSQLQRY += " AND CMT_CCSVL <>'01' ";
					M_strHLPFLD = "txtLINNO";
					cl_hlp(M_strSQLQRY ,2,1,staHEADR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtRCTNO)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String []staHEADR1 = {"Code","Reactor No.","Capacity"};	
					M_strSQLQRY = "SELECT cmt_codcd,cmt_codds,cmt_ncsvl from co_cdtrn where cmt_cgmtp =  ";
					M_strSQLQRY += "'SYS'" + " AND cmt_cgstp = ";
					M_strSQLQRY += "'PRXXCYL' AND isnull(CMT_STSFL,'') <>'X' ";
					M_strSQLQRY += " AND CMT_CCSVL LIKE " + "'%"+strPRDTP + "%'  and CMT_MODLS = '"+cl_dat.M_strCMPCD_pbst+"'";
					//**NOTE if CMT_CHP01 is 1 then Line validation is applicable else not
					//M_strSQLQRY +=" and CMT_CODCD LIKE '" + txtLINNO.getText().trim()+"%'";
					M_strHLPFLD = "txtRCTNO";
					cl_hlp(M_strSQLQRY ,2,1,staHEADR1,3,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtMATCD)
    			{
    				cl_dat.M_flgHELPFL_pbst = true;
    				M_strHLPFLD = "txtMATCD";
    				String L_ARRHDR[] = {"Item Code","UOM", "Description"};
				if(rdbSEBAT.isSelected())
				{
					M_strSQLQRY = "Select CT_MATCD,ct_uomcd,CT_MATDS from CO_CTMST ";
    					M_strSQLQRY += " where CT_CODTP ='CD' and CT_GRPCD IN ('68','51') and isnull(CT_STSFL,'') not in('X','9')";
		
				}
				else
				{
					M_strSQLQRY = "Select CT_MATCD,ct_uomcd,CT_MATDS from CO_CTMST ";
    					M_strSQLQRY += " where CT_CODTP ='CD' and CT_GRPCD ='68' and isnull(CT_STSFL,'') not in('X','9')";
				}
    				if(txtMATCD.getText().trim().length() >0)
    					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
    				M_strSQLQRY += " Order by ct_matcd ";
    				cl_hlp(M_strSQLQRY,1,3,L_ARRHDR,3,"CT");
    			}
    			if(M_objSOURC == txtISSNO)
    			{
    				cl_dat.M_flgHELPFL_pbst = true;
    				M_strHLPFLD = "txtISSNO";
    				String L_ARRHDR[] = {"Store Type","Issue No"};
    				M_strSQLQRY = "Select IS_STRTP,IS_ISSNO from MM_ISMST ";
    				M_strSQLQRY += " where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STSFL ='2' AND IS_MATCD = '"+tblRMDTL.getValueAt(tblRMDTL.getSelectedRow(),TB2_MATCD)+"'";
    				if(txtISSNO.getText().trim().length() >0)
    					M_strSQLQRY += " and IS_MATCD like '" + txtISSNO.getText().trim() + "%'";
    				M_strSQLQRY += " Order by is_issno ";
    				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
    			}
    			if(M_objSOURC == txtPRDCD)
    			{
    				cl_dat.M_flgHELPFL_pbst = true;
    				M_strHLPFLD = "txtPRDCD";
    				String L_ARRHDR[] = {"Code", "Description"};
    			      //M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST WHERE substr(PR_PRDCD,1,6) ='512101' ";
				M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST WHERE SUBSTRING(PR_PRDCD,1,4) ='5121' ";

    				M_strSQLQRY += " Order by pr_prdds ";
    				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
    			}
    		}
			catch(NullPointerException L_NPE)
			{
			    setMSG("Help not available",'N');                            
			}
		  }
	}
	void getDATA()
	{
	    try
	    {
	        java.sql.Timestamp L_TMPTM;
	        String L_strPSTDT="",L_strPENDT="";
			M_strSQLQRY = "SELECT * FROM PR_BTMST,CO_PRMST WHERE ltrim(str(BT_GRDCD,20,0)) = PR_PRDCD AND BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO ='"+txtBATNO.getText().trim()+"' AND isnull(BT_STSFL,'')<>'X'";
			System.out.println(M_strSQLQRY);
	        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int i=0;
			String L_strRUNNO = txtBATNO.getText().trim();
			
	        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
       	    {
                  cl_dat.M_btnSAVE_pbst.setEnabled(true);
       	    }
       	    else
       	    {
       	        clrCOMP();
       	        rdbCLBAT.setSelected(true);
    			tblBTDTL.clrTABLE();
    			tblRMDTL.clrTABLE();
    			txtBATNO.setText(L_strRUNNO);
       	    }
        	if(M_rstRSSET != null)
			{
			  while(M_rstRSSET.next())
			  {
    			   if(i==0)
    			   { 
			         /*if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
        			    if(M_rstRSSET.getString("LT_CLSFL").equals("9"))
        			    {
        			        setMSG("Authorisation is completed, Operation not allowed..",'E');
        			        return;
        			    }*/
        			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
        			    {
                              setMSG("Batch Already exists..",'E');
                              cl_dat.M_btnSAVE_pbst.setEnabled(false);
                              return;   			        
        			    }
        			    txtLINNO.setText(M_rstRSSET.getString("BT_LINNO"));
        			    txtRCTNO.setText(M_rstRSSET.getString("BT_RCTNO"));
        			    L_TMPTM = M_rstRSSET.getTimestamp("BT_BSTDT");
        				if(L_TMPTM !=null)
        					L_strPSTDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BSTDT"));
        				L_TMPTM = M_rstRSSET.getTimestamp("BT_BENDT");
        				if(L_TMPTM !=null)
        				{
        					L_strPENDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BENDT"));
        				}
        				if(L_strPSTDT.trim().length() > 0)
        				{
        					txtBSTDT.setText(L_strPSTDT.substring(0,10));
        					txtBSTTM.setText(L_strPSTDT.substring(11,16));
        				}
        				if(L_strPENDT.trim().length() > 0)
        				{
        					txtBENDT.setText(L_strPENDT.substring(0,10));
        					txtBENTM.setText(L_strPENDT.substring(11,16));
        				}
    				}
				    tblBTDTL.setValueAt(M_rstRSSET.getString("BT_GRDCD"),i,TBL_PRDCD);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("PR_PRDDS"),i,TBL_PRDDS);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("BT_RUNNO"),i,TBL_SRLNO);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("BT_PRDQT"),i,TBL_PRDQT);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("BT_SRLNO"),i,TBL_SEQNO);
			        tblBTDTL.setValueAt(M_rstRSSET.getString("BT_BAGQT"),i,TBL_BAGQT);
			        i++;
    				}
					intOLTRW =i;
					System.out.println("Total rows");
    				M_rstRSSET.close();
    			}
    			M_strSQLQRY = "SELECT RM_REMDS from QC_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"
    		                + strPRDTP +"' AND RM_TSTTP ='BAT' and RM_TSTNO ='"
							+ txtBATNO.getText().trim() +"' AND isnull(RM_STSFL,'') <>'X'";
							System.out.println(M_strSQLQRY);
    		    M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
    		    if(M_rstRSSET !=null)
    		    {
    		        if(M_rstRSSET.next())
    		            txtREMDS.setText(M_rstRSSET.getString("RM_REMDS"));     
    		        M_rstRSSET.close();    
    		    }
    		    M_strSQLQRY = "SELECT * from PR_LTRDT,CO_CTMST WHERE LTR_MATCD = CT_MATCD AND LTR_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"' "+
							  " AND LTR_BATNO  ='" + txtBATNO.getText().trim() +"' AND isnull(LTR_STSFL,'') <>'X'";
				System.out.println(M_strSQLQRY);			  
    		    M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
    		    i=0;
    		    if(M_rstRSSET !=null)
    		    {
    		        while(M_rstRSSET.next())
    		        {
    		             tblRMDTL.setValueAt(M_rstRSSET.getString("LTR_MATCD"),i,TB2_MATCD);
        			     tblRMDTL.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,TB2_MATDS);
        			     tblRMDTL.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,TB2_UOMCD);
        			     tblRMDTL.setValueAt(M_rstRSSET.getString("LTR_ISSQT"),i,TB2_ISSQT);
        			     tblRMDTL.setValueAt(M_rstRSSET.getString("LTR_STRTP"),i,TB2_STRTP);
        			     tblRMDTL.setValueAt(M_rstRSSET.getString("LTR_ISSNO"),i,TB2_ISSNO);
        			     tblRMDTL.setValueAt(M_rstRSSET.getString("LTR_SRLNO"),i,TB2_SEQNO);
        			     i++;  
    		        }
    		        M_rstRSSET.close();
    		        intOLDRW =i;
    		    }
   	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getDATA");
	    }
	}
	boolean vldDATA()
	{
	    cl_dat.M_flgLCUPD_pbst = true;
	     if(tblBTDTL.isEditing())
			tblBTDTL.getCellEditor().stopCellEditing();
	    tblBTDTL.setRowSelectionInterval(0,0);
	    tblBTDTL.setColumnSelectionInterval(0,0);
        if(tblRMDTL.isEditing())
			tblRMDTL.getCellEditor().stopCellEditing();
	    tblRMDTL.setRowSelectionInterval(0,0);
	    tblRMDTL.setColumnSelectionInterval(0,0);

	    if(txtLINNO.getText().length() ==0)
	    {
	        setMSG("Line No. can not be Blank..",'E');
	        return false;
	    }
	    if(txtRCTNO.getText().length() ==0)
	    {
	        setMSG("Silo No. can not be Blank..",'E');
	        return false;
	    }
	    if(txtBSTDT.getText().length() ==0)
	    {
	        setMSG("Production Start date can not be Blank..",'E');
	        return false;
	    }
	    if(txtBSTTM.getText().length() ==0)
	    {
	        setMSG("Production Start Time can not be Blank..",'E');
	        return false;
	    }
	    if(txtBENDT.getText().length() ==0)
	    {
	        setMSG("Production End date can not be Blank..",'E');
	        return false;
	    }
	    if(txtBENTM.getText().length() ==0)
	    {
	        setMSG("Production End Time can not be Blank..",'E');
	        return false;
	    }
	    for(int i=0;i<tblBTDTL.getRowCount();i++)
        {
	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
	        {
	            if(nvlSTRVL(tblBTDTL.getValueAt(i,TBL_PRDQT).toString(),"").equals(""))
	            {
	                setMSG("Enter Production quantity ..",'E');
	                return false;
	            }
	        }
	    }
	    return true;
	}
   	void exeSAVE()
	{
	    try
	    {
	        if(!vldDATA())
	            return;
	        String L_strADDTM="";
	        String L_strENDTM="";
	        String strBATDT = null;
	       if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
	       {
	            getPRVBT();
	            if(txtBATNO.getText().trim().length() !=8)
	                return;
    	        for(int i=0;i<tblBTDTL.getRowCount();i++)
    	        {
        	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            if(Double.parseDouble(tblBTDTL.getValueAt(i,TBL_PRDQT).toString()) == 0)
        	                continue;
        	            strLOTNO = tblBTDTL.getValueAt(i,TBL_SRLNO).toString();
            	        M_strSQLQRY = "INSERT INTO PR_BTMST (BT_CMPCD,BT_SBSCD,BT_BATNO,BT_LINNO,BT_RCTNO,BT_BATDT,";
            			M_strSQLQRY += "BT_BSTDT,BT_BENDT,BT_PRDTP,BT_GRDCD,BT_RUNNO,BT_SRLNO,BT_SAMQT,BT_PRDQT,BT_BAGQT,";
            			M_strSQLQRY += "BT_STSFL,BT_TRNFL,BT_LUSBY,BT_LUPDT) VALUES(";
            			M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+ "',";
            			M_strSQLQRY += "'" + M_strSBSCD + "',";
						M_strSQLQRY += "'" + txtBATNO.getText().trim() + "',";
            			M_strSQLQRY += "'" + txtLINNO.getText().trim() + "',";
            			M_strSQLQRY += "'" + txtRCTNO.getText().trim() + "',";
            			if(strBATDT != null)
            			    M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(strBATDT)) + "',";
            			else    
            			    M_strSQLQRY += "null,";
            			
            			if(getPENDTM().length() >0)
            			    M_strSQLQRY += "'" +M_fmtDBDTM.format(M_fmtLCDTM.parse(getPSTDTM())) + "',";
            			else
            				M_strSQLQRY += "null,";			
            			if(getPENDTM().length() >0)
            				M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(getPENDTM())) + "',";			
            			else
            				M_strSQLQRY += "null,";			
            		    M_strSQLQRY += "'" + strPRDTP + "',";
            			M_strSQLQRY += "'" + tblBTDTL.getValueAt(i,TBL_PRDCD).toString() + "',";
            			M_strSQLQRY += "'" + tblBTDTL.getValueAt(i,TBL_SRLNO).toString() + "',";
            			M_strSQLQRY += "'" + tblBTDTL.getValueAt(i,TBL_SEQNO).toString() + "',";
            			M_strSQLQRY +=  nvlSTRVL(tblBTDTL.getValueAt(i,TBL_SAMQT).toString(),"0") + ","; 
            			M_strSQLQRY +=  tblBTDTL.getValueAt(i,TBL_PRDQT).toString() + ","; 
            			M_strSQLQRY += "0,";	// 0 Bagged Quantity
            			M_strSQLQRY += "'0',";		// Status flag 0 for fresh entry
            			M_strSQLQRY += "'0',";		// 0 trn flg
            			M_strSQLQRY +=  "'" + cl_dat.M_strUSRCD_pbst + "',";
            			M_strSQLQRY +=  "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "')";
            			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
            			
        	        }
        	    }
    	        for(int i=0;i<tblRMDTL.getRowCount();i++)
    	        {
        	        if(tblRMDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            if(Double.parseDouble(tblRMDTL.getValueAt(i,TB2_ISSQT).toString()) == 0)
        	                continue;
        	            M_strSQLQRY = "INSERT INTO PR_LTRDT (LTR_CMPCD,LTR_SBSCD,LTR_BATNO,LTR_STRTP,LTR_ISSNO,";
            			M_strSQLQRY += "LTR_MATCD,LTR_SRLNO,LTR_ISSQT,";
            			M_strSQLQRY += "LTR_STSFL,LTR_TRNFL,LTR_LUSBY,LTR_LUPDT) VALUES(";
            			M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
            			M_strSQLQRY += "'" + M_strSBSCD + "',";
            			M_strSQLQRY += "'" + txtBATNO.getText() + "',";
            			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_STRTP).toString() + "',";
            			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_ISSNO).toString() + "',";
            			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_MATCD).toString() + "',";
            			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_SEQNO).toString() + "',";
            			M_strSQLQRY +=  tblRMDTL.getValueAt(i,TB2_ISSQT).toString() + ","; 
            			M_strSQLQRY += "'','0',";
            			M_strSQLQRY += "'" + cl_dat.M_strUSRCD_pbst + "',";
            			M_strSQLQRY +=  "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "')";
            			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
            		}
    	        }
    	        cl_dat.exeSRLSET("D"+cl_dat.M_strCMPCD_pbst,"PRXXSRL",strLOTNO.substring(0,3),strLOTNO.substring(3));
        	    String L_strCODCD="";
        	    L_strCODCD = txtLINNO.getText().trim() +strPRDGT+strYRDGT;
        	    if(rdbCLBAT.isSelected())
                {
                    L_strCODCD += "01";
                }
                else
                {
                    L_strCODCD += "02";
                }
    	        cl_dat.exeSRLSET("D"+cl_dat.M_strCMPCD_pbst,"PRXXBAT",L_strCODCD,txtBATNO.getText());
            	if(txtREMDS.getText().length() >0)
            	{
                	M_strSQLQRY = "Insert into qc_rmmst(rm,cmpcd,rm_qcatp,rm_tsttp,rm_tstno,rm_remds,rm_trnfl,rm_stsfl,rm_lusby,rm_lupdt)values("
                    	            +"'"+cl_dat.M_strCMPCD_pbst+"',"
									+"'"+M_strSBSCD +"',"
									+"'"+strPRDTP+"',"
                    	            +"'BAT',"
                    	            +"'"+ txtBATNO.getText().trim()+"',"
                    	            +"'"+ txtREMDS.getText().trim()+"',";
                    M_strSQLQRY += getUSGDTL("RM",'I',"") + ")";
                  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
            	}
                if(cl_dat.exeDBCMT("exeSAVE"))
        		{
        		   		JOptionPane.showMessageDialog(this,"Please Note down Batch No :  "+txtBATNO.getText().trim(),"Message",JOptionPane.INFORMATION_MESSAGE);
        				setMSG("Batch Has been Added ..",'N');
        				clrCOMP();
        				rdbCLBAT.setSelected(true);
        		}
        		else
        		{
        				setMSG("Addition Failed ..",'E');
        		}
	       }
	       else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
	       {
	             for(int i=0;i<tblBTDTL.getRowCount();i++)
    	        {
        	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            if(i >= intOLTRW)
						{
        	                if(Double.parseDouble(tblBTDTL.getValueAt(i,TBL_PRDQT).toString()) == 0)
        	                continue;
            	            strLOTNO = tblBTDTL.getValueAt(i,TBL_SRLNO).toString();
                	        M_strSQLQRY = "INSERT INTO PR_BTMST (BT_CMPCD,BT_SBSCD,BT_BATNO,BT_LINNO,BT_RCTNO,BT_BATDT,";
                			M_strSQLQRY += "BT_BSTDT,BT_BENDT,BT_PRDTP,BT_GRDCD,BT_RUNNO,BT_SRLNO,BT_SAMQT,BT_PRDQT,BT_BAGQT,";
                			M_strSQLQRY += "BT_STSFL,BT_TRNFL,BT_LUSBY,BT_LUPDT) VALUES(";
                			M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+ "',";
							M_strSQLQRY += "'" +M_strSBSCD+ "',";
                			M_strSQLQRY += "'" + txtBATNO.getText().trim() + "',"; // batch No
                			M_strSQLQRY += "'" + txtLINNO.getText().trim() + "',";
                			M_strSQLQRY += "'" + txtRCTNO.getText().trim() + "',";
                			if(strBATDT != null)
                			    M_strSQLQRY += "'" +M_fmtDBDAT.format(M_fmtLCDAT.parse(strBATDT)) + "',";
                			else    
                			    M_strSQLQRY += "null,";
                			
                			if(getPENDTM().length() >0)
                			    M_strSQLQRY += "'" +M_fmtDBDTM.format(M_fmtLCDTM.parse(getPSTDTM())) + "',";
                			else
                				M_strSQLQRY += "null,";			
                			if(getPENDTM().length() >0)
                				M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(getPENDTM())) + "',";			
                			else
                				M_strSQLQRY += "null,";			
                		    M_strSQLQRY += "'" + strPRDTP + "',";
                			M_strSQLQRY += "'" + tblBTDTL.getValueAt(i,TBL_PRDCD).toString() + "',";
                			M_strSQLQRY += "'" + tblBTDTL.getValueAt(i,TBL_SRLNO).toString() + "',";
                			M_strSQLQRY += "'" + tblBTDTL.getValueAt(i,TBL_SEQNO).toString() + "',";
                			M_strSQLQRY +=  nvlSTRVL(tblBTDTL.getValueAt(i,TBL_SAMQT).toString(),"0") + ","; 
                			M_strSQLQRY +=  tblBTDTL.getValueAt(i,TBL_PRDQT).toString() + ","; 
                			M_strSQLQRY += "0,";	// 0 Bagged Quantity
                			M_strSQLQRY += "'0',";		// Status flag 0 for fresh entry
                			M_strSQLQRY += "'0',";		// 0 trn flg
                			M_strSQLQRY +=  "'" + cl_dat.M_strUSRCD_pbst + "',";
                			M_strSQLQRY +=  "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "')";
                			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
        	            }
						else
						{
							M_strSQLQRY = "Update PR_BTMST SET BT_PRDQT = "+tblBTDTL.getValueAt(i,TBL_PRDQT).toString()+
										" where BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_RUNNO ='"+ tblBTDTL.getValueAt(i,TBL_SRLNO).toString() + "'"+
										" and BT_BATNO ='"+txtBATNO.getText().trim()+"'"+
										" and BT_GRDCD ='"+tblBTDTL.getValueAt(i,TBL_PRDCD).toString()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						}
					}
    	        }
    	        ////
	            for(int i=0;i<tblRMDTL.getRowCount();i++)
    	        {
        	        if(tblRMDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            if(i >= intOLDRW)
        	            {
            	            if(Double.parseDouble(tblRMDTL.getValueAt(i,TB2_ISSQT).toString()) == 0)
            	                continue;
            	            M_strSQLQRY = "INSERT INTO PR_LTRDT (LTR_CMPCD,LTR_SBSCD,LTR_BATNO,LTR_STRTP,LTR_ISSNO,";
                			M_strSQLQRY += "LTR_MATCD,LTR_SRLNO,LTR_ISSQT,";
                			M_strSQLQRY += "LTR_STSFL,LTR_TRNFL,LTR_LUSBY,LTR_LUPDT) VALUES(";
                			M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
                			M_strSQLQRY += "'" + M_strSBSCD + "',";
                			M_strSQLQRY += "'" + txtBATNO.getText() + "',";
                			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_STRTP).toString() + "',";
                			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_ISSNO).toString() + "',";
                			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_MATCD).toString() + "',";
                			M_strSQLQRY += "'" + tblRMDTL.getValueAt(i,TB2_SEQNO).toString() + "',";
                			M_strSQLQRY +=  tblRMDTL.getValueAt(i,TB2_ISSQT).toString() + ","; 
                			M_strSQLQRY += "'','0',";
                			M_strSQLQRY += "'" + cl_dat.M_strUSRCD_pbst + "',";
                			M_strSQLQRY +=  "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "')";
                			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
        	            }
        	            else
        	            {
        	                M_strSQLQRY =" UPDATE PR_LTRDT SET LTR_ISSQT = "+tblRMDTL.getValueAt(i,TB2_ISSQT).toString();
        	                M_strSQLQRY += " WHERE LTR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LTR_BATNO ='"+txtBATNO.getText().trim() +"'";
        	                M_strSQLQRY += " AND LTR_MATCD ='"+tblRMDTL.getValueAt(i,TB2_MATCD).toString() + "'";
        	                M_strSQLQRY += " AND LTR_STRTP ='"+tblRMDTL.getValueAt(i,TB2_STRTP).toString() + "'";
        	                M_strSQLQRY += " AND LTR_ISSNO ='"+tblRMDTL.getValueAt(i,TB2_ISSNO).toString() + "'";
        	                cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
        	            }
        	      	}
    	        }
    	       if(cl_dat.exeDBCMT("exeSAVE"))
        		{
        				setMSG("Modification Completed ..",'N');
        				clrCOMP();
        				rdbCLBAT.setSelected(true);
        		}
        		else
        		{
        				setMSG("Error in Modification ..",'E');
        		}

	       }
	      /* else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
	       {
	            for(int i=0;i<tblBTDTL.getRowCount();i++)
    	        {
        	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
        	            M_strSQLQRY = "UPDATE PR_LTMST SET LT_STSFL ='X' WHERE LT_RUNNO ='"+txtBATNO.getText().trim()+"'";
        	            M_strSQLQRY += " AND LT_LOTNO = '"+tblBTDTL.getValueAt(i,TBL_SRLNO).toString()+"'";
        	            cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        	        }
    	        }
    	       if(cl_dat.exeDBCMT("exeSAVE"))
        		{
        				setMSG("Deletion Completed ..",'N');
        				clrCOMP();
        		}
        		else
        		{
        				setMSG("Error in Deletion ..",'E');
        		}
	       }
	       else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
	       {
	          M_strSQLQRY = "UPDATE PR_LTMST SET LT_CLSFL ='9' WHERE LT_RUNNO ='"+txtBATNO.getText().trim()+"' AND isnull(LT_STSFL,'')<>'X'";
	          cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	       if(cl_dat.exeDBCMT("exeSAVE"))
        		{
        				setMSG("Authorisation Completed ..",'N');
        				clrCOMP();
        		}
        		else
        		{
        				setMSG("Error in Authorisation ..",'E');
        		}
	       }*/
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"exeSAVE");
	    }
	}
	public void actionPerformed(ActionEvent L_AE)
    {
        super.actionPerformed(L_AE);
        if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
        {
            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
            {
                setENBL(true);
            }
            else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
            {
                setENBL(false);
            }
  
        }
        if(M_objSOURC == txtBATNO)
		{
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		    {
		        txtBATNO.setText(txtBATNO.getText().toUpperCase());
		        txtLINNO.requestFocus();
		        
		    }
		    else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		    {
		         setENBL(false);
		        tblBTDTL.cmpEDITR[TBL_PRDQT].setEnabled(true);
		    }
		    else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		    {
		        setENBL(false);
		        tblBTDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
		    }
		    else
		    {
		        setENBL(false);
		        tblBTDTL.cmpEDITR[TBL_PRDQT].setEnabled(false);
		    }
		     if(tblBTDTL.isEditing())
			tblBTDTL.getCellEditor().stopCellEditing();
    	    tblBTDTL.setRowSelectionInterval(0,0);
    	    tblBTDTL.setColumnSelectionInterval(0,0);
            if(tblRMDTL.isEditing())
    			tblRMDTL.getCellEditor().stopCellEditing();
    	    tblRMDTL.setRowSelectionInterval(0,0);
    	    tblRMDTL.setColumnSelectionInterval(0,0);
		    getDATA();
		}
	    if(M_objSOURC == txtLINNO)
		{
			if(vldLINNO())
			{
				txtRCTNO.setEnabled(true);
				txtRCTNO.requestFocus();
			}
		}
		else if(M_objSOURC == txtRCTNO)
		{
			if(txtRCTNO.getText().trim().length() ==0)
				setMSG("Reactor Number can not be blank..",'E');
			else
			{
				setMSG("",'N');
				if(vldSILNO())
				{
				    setMSG("Enter Production Start Date..",'N');
					txtBSTDT.requestFocus();
					//txtTPRCD.setEnabled(true);
					//txtTPRCD.requestFocus();
				}
			}
		}
		else if(M_objSOURC == txtBSTDT)
		{
			try
			{
				if(M_fmtLCDAT.parse(txtBSTDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date can not be greater than today's date..",'E');
				}
				else
				{
					txtBSTTM.setEnabled(true);
					txtBSTTM.requestFocus();
				    setMSG("Enter Production Start Time..",'N');
					txtBSTTM.requestFocus();
				
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Date Comparision");
			}
		}
		else if(M_objSOURC == txtBSTTM)
		{
			try
			{
				if(M_fmtLCDTM.parse(getPSTDTM()).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
				{
					setMSG("Time can not be greater than the current time..",'E');
				}
				else
				{
				    setMSG("Enter Production End Date..",'N');
					txtBENDT.requestFocus();
				
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Start time Comparision");
			}
		}
		else if(M_objSOURC == txtBENDT)
		{
			try
			{
				//if(txtBENDT.vldDATE() != null)
				//{
					if(M_fmtLCDAT.parse(txtBENDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Date can not be greater than today's date..",'E');
					}
					else if(M_fmtLCDAT.parse(txtBENDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtBENDT.getText().trim()))< 0)
					{
						setMSG("Lot End Date can not be smaler than start date..",'E');
					}
					else
					{
						txtBENTM.setEnabled(true);
						txtBENTM.requestFocus();
						setMSG("Enter Production End Time..",'N');
					    txtBENTM.requestFocus();
					}
				//}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Date Comparision");
			}
		}
		else if(M_objSOURC == txtBENTM)
		{
			try
			{
				if(M_fmtLCDTM.parse(getPENDTM()).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
				{
					setMSG("Date can not be greater than current date and time..",'E');
				}
				else if(M_fmtLCDTM.parse(getPSTDTM()).compareTo(M_fmtLCDTM.parse(getPENDTM()))>0)
					setMSG("Lot Start Date can not be greater than end date..",'E');
				else
				{
				    txtTOTQT.requestFocus();
                    getGRDDT();				    
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"End Time Comparision");
			}
		}
		else if(M_objSOURC == txtTOTQT)
		{
		    txtYLDFT.requestFocus();
		}
		else if(M_objSOURC == txtYLDFT)
		{
		    tblBTDTL.setRowSelectionInterval(0,0);		
			tblBTDTL.setColumnSelectionInterval(TBL_SAMQT,TBL_SAMQT);		
			tblBTDTL.editCellAt(0,TBL_SAMQT);
			tblBTDTL.cmpEDITR[TBL_SAMQT].requestFocus();
		    /*tblBTDTL.setRowSelectionInterval(0,0);		
			tblBTDTL.setColumnSelectionInterval(TBL_SAMQT,TBL_SAMQT);		
			tblBTDTL.editCellAt(0,TBL_SAMQT);
			tblBTDTL.cmpEDITR[TBL_SAMQT].requestFocus();*/
		}

    }
    private class TBLINVFR extends TableInputVerifier
    {
	    public boolean verify(int P_intROWID,int P_intCOLID)
		{
			String L_strTEMP ="";
			String strTEMP ="";
			try
			{
				if(P_intCOLID==TBL_SAMQT)
				{
					strTEMP = tblBTDTL.getValueAt(P_intROWID,TBL_SAMQT).toString();
					if(strTEMP.length()>0)
					{
						if(Double.parseDouble(strTEMP) < 0)
						{
							setMSG("Sample qty. can not be Negative ..",'E');
							return false;
						}
						else if(Double.parseDouble(strTEMP) == 0)
						{
							
						}
						else
						{
						    tblBTDTL.setValueAt(setNumberFormat((Double.parseDouble(strTEMP)/Double.parseDouble(txtTOTQT.getText().trim()))*Double.parseDouble(txtYLDFT.getText().trim())/1000,3),P_intROWID,TBL_PRDQT);
						}
					}
				}
			}
			catch(Exception L_E)
			{
			    setMSG(L_E,"Input Verifier");
			}
			return true;
		}
}
}
/*private class pr_teltmTBLINVFR extends TableInputVerifier
{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				String strMATCD ="";
				String strTEMP ="";
				if(P_intCOLID==TB2_MATCD)
				{
					if(tblRMDTL.getValueAt(P_intROWID,TB2_MATCD).toString().length() ==0)
						return true;
					if(strMATCD ==null)
					{
						strTEMP = tblRMDTL.getValueAt(P_intROWID,TB2_MATCD).toString();
						strMATCD = strTEMP;
					}
					else if(strMATCD.length() == 0)
					{
						strTEMP = tblRMDTL.getValueAt(P_intROWID,TB2_MATCD).toString();
						strMATCD = strTEMP;
					}
					//strTEMP = tblRMDTL.getValueAt(P_intROWID,TB2_MATCD).toString();
					if(strMATCD.length()>0)
						if(!vldMATCD(P_intROWID,strMATCD))
						{
							return false;
						}
						
				}
			}
			catch(Exception L_E)
			{
			    setMSG(L_E,"verify ");
			}
			return true;
		}
}*/
